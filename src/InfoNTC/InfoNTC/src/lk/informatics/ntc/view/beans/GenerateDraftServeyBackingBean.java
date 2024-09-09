package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.FormDTO;
import lk.informatics.ntc.model.dto.IndicatorsDTO;
import lk.informatics.ntc.model.dto.MidPointSurveyDTO;
import lk.informatics.ntc.model.service.GenerateDraftSurveyService;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.ntc.view.util.SpringApplicationContex;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

@ManagedBean(name = "generateDraftServeyBackingBean")
@ViewScoped
public class GenerateDraftServeyBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<FormDTO> formList;
	private String selectedFormID;
	private FormDTO formDTO;
	private List<IndicatorsDTO> tableIndicatorList;
	private List<IndicatorsDTO> inputDataIndicatorList;
	private String noOfRows;
	private String noOfCopies;
	private boolean printCopies;
	private int rowCount;
	private int stepVal;
	private List<Integer> integerIndicatorList;
	private boolean backBtnRender;
	private StreamedContent files;
	private int noOfPages;
	private List<String> noOfRowsList;

	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
	}

	private GenerateDraftSurveyService generateDraftSurveyService;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	@PostConstruct
	public void init() {
		generateDraftSurveyService = (GenerateDraftSurveyService) SpringApplicationContex
				.getBean("generateDraftSurveyService");
		generateDraftSurveyService.deleteEnterdRows(selectedFormID);
		formDTO = new FormDTO();
		formList = new ArrayList<FormDTO>();
		formList = generateDraftSurveyService.retrieveFormsList();
		tableIndicatorList = new ArrayList<IndicatorsDTO>();
		inputDataIndicatorList = new ArrayList<IndicatorsDTO>();
		integerIndicatorList = new ArrayList<Integer>();
		noOfRowsList = new ArrayList<String>();
		printCopies = false;
		rowCount = 0;
		stepVal = 0;
		backBtnRender = false;
		dispalyRowsNo();

		FacesContext fcontext = FacesContext.getCurrentInstance();
		Object objCallerbackBtn = fcontext.getExternalContext().getSessionMap().get("GENERATE_SURVEY");
		Object objCallerSurveyFormID = fcontext.getExternalContext().getSessionMap().get("SURVEY_FORM_ID");

		if (objCallerbackBtn != null) {
			String backBtn = (String) objCallerbackBtn;
			if (backBtn != null && !backBtn.isEmpty() && backBtn.equalsIgnoreCase("true")) {
				backBtnRender = true;
				String formID = (String) objCallerSurveyFormID;
				selectedFormID = formID;
				onFormIdClick();
			}
		}

		fcontext.getExternalContext().getSessionMap().put("GENERATE_SURVEY", "false");

	}

	public List<String> getNoOfRowsList() {
		return noOfRowsList;
	}

	public void setNoOfRowsList(List<String> noOfRowsList) {
		this.noOfRowsList = noOfRowsList;
	}

	public void onFormIdClick() {
		
		noOfRows = "0";
		integerIndicatorList = new ArrayList<Integer>();
		inputDataIndicatorList = new ArrayList<IndicatorsDTO>();

		formDTO = generateDraftSurveyService.retrieveFormDataFromFormId(selectedFormID);

		tableIndicatorList = retrieveIndicatorList(selectedFormID);

		for (IndicatorsDTO dto : tableIndicatorList) {
			IndicatorsDTO addDto = new IndicatorsDTO();
			inputDataIndicatorList.add(addDto);
		}

		printCopies = true;

		RequestContext.getCurrentInstance().update("frmGenSurvey");
	}

	public List<IndicatorsDTO> retrieveIndicatorList(String selectedFormId) {
		List<IndicatorsDTO> indicatorList = new ArrayList<IndicatorsDTO>();

		indicatorList = generateDraftSurveyService.retrieveIndicatorsForFormId(selectedFormId);

		return indicatorList;
	}

	public void deleteRowAction(int selectedDto) {
		int rowNums = Integer.parseInt(noOfRows);
		rowNums = rowNums - 1;
		noOfRows = Integer.toString(rowNums);
		generateDraftSurveyService.deleteEnterdRows(selectedFormID);
		addRowsAction();
	}

	public void dispalyRowsNo() {

		noOfRowsList.add("1");
		noOfRowsList.add("2");
		noOfRowsList.add("3");
		noOfRowsList.add("4");
		noOfRowsList.add("5");
		noOfRowsList.add("6");
		noOfRowsList.add("7");
		noOfRowsList.add("8");
		noOfRowsList.add("9");
		noOfRowsList.add("10");
		noOfRowsList.add("11");
		noOfRowsList.add("12");
		noOfRowsList.add("13");
		noOfRowsList.add("14");
		noOfRowsList.add("15");

	}

	public StreamedContent printCopiesAction() throws JRException {
		files = null;
		String sourceFileName = null;
		Connection conn = null;
		MidPointSurveyDTO midPointSurveyDTO = new MidPointSurveyDTO();
		midPointSurveyDTO.setFormId(selectedFormID);
		midPointSurveyDTO.setSurveyNo(formDTO.getSurveyNo());

		try {
			conn = ConnectionManager.getConnection();
			sourceFileName = "..//reports//DraftSurveyFormNew.jrxml";

			String logopath = "//lk//informatics//ntc//view//reports//";

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();
	
			parameters.put("P_FORM_ID", selectedFormID);
			parameters.put("P_LOGO", logopath);

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));
			
			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", "Draft Syrvey Form.pdf");
		
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("reportBytes", pdfByteArray);
			sessionMap.put("docType", "pdf");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(conn);
		}

		generateDraftSurveyService.insertDataIntoNt_t_survey_print_tracking(midPointSurveyDTO,
				sessionBackingBean.getLoginUser());

		RequestContext.getCurrentInstance().execute("PF('successSve').show()");
		generateDraftSurveyService.deleteEnterdRows(selectedFormID);
		
		return files;

	}

	public void addRowsAction() {
		rowCount = Integer.parseInt(noOfRows);
		integerIndicatorList = new ArrayList<Integer>();
		for (int i = 1; i <= (rowCount + 1); i++) {
			integerIndicatorList.add(i);
		}
		generateDraftSurveyService.insertEnteredRows(selectedFormID, integerIndicatorList);

		RequestContext.getCurrentInstance().update("frmGenSurvey");
	}

	public void backButonAction() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("GENERATE_SURVEY_BACK", "true");
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("/InfoNTC/pages/surveyManagement/generateSurveyForm.xhtml");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void cleaForm() {
		formDTO = new FormDTO();
		tableIndicatorList = new ArrayList<IndicatorsDTO>();
		printCopies = false;
		selectedFormID = "";
		backBtnRender = false;

		RequestContext.getCurrentInstance().update("frmGenSurvey");
	}

	public GenerateDraftSurveyService getGenerateDraftSurveyService() {
		return generateDraftSurveyService;
	}

	public void setGenerateDraftSurveyService(GenerateDraftSurveyService generateDraftSurveyService) {
		this.generateDraftSurveyService = generateDraftSurveyService;
	}

	public List<FormDTO> getFormList() {
		return formList;
	}

	public void setFormList(List<FormDTO> formList) {
		this.formList = formList;
	}

	public String getSelectedFormID() {
		return selectedFormID;
	}

	public void setSelectedFormID(String selectedFormID) {
		this.selectedFormID = selectedFormID;
	}

	public FormDTO getFormDTO() {
		return formDTO;
	}

	public void setFormDTO(FormDTO formDTO) {
		this.formDTO = formDTO;
	}

	public List<IndicatorsDTO> getTableIndicatorList() {
		return tableIndicatorList;
	}

	public void setTableIndicatorList(List<IndicatorsDTO> tableIndicatorList) {
		this.tableIndicatorList = tableIndicatorList;
	}

	public String getNoOfRows() {
		return noOfRows;
	}

	public void setNoOfRows(String noOfRows) {
		this.noOfRows = noOfRows;
	}

	public String getNoOfCopies() {
		return noOfCopies;
	}

	public void setNoOfCopies(String noOfCopies) {
		this.noOfCopies = noOfCopies;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public boolean isPrintCopies() {
		return printCopies;
	}

	public void setPrintCopies(boolean printCopies) {
		this.printCopies = printCopies;
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	public List<IndicatorsDTO> getInputDataIndicatorList() {
		return inputDataIndicatorList;
	}

	public void setInputDataIndicatorList(List<IndicatorsDTO> inputDataIndicatorList) {
		this.inputDataIndicatorList = inputDataIndicatorList;
	}

	public int getStepVal() {
		return stepVal;
	}

	public void setStepVal(int stepVal) {
		this.stepVal = stepVal;
	}

	public List<Integer> getIntegerIndicatorList() {
		return integerIndicatorList;
	}

	public void setIntegerIndicatorList(List<Integer> integerIndicatorList) {
		this.integerIndicatorList = integerIndicatorList;
	}

	public boolean isBackBtnRender() {
		return backBtnRender;
	}

	public void setBackBtnRender(boolean backBtnRender) {
		this.backBtnRender = backBtnRender;
	}

}
