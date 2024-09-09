package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
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
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.AdvertisementDTO;
import lk.informatics.ntc.model.dto.TenderDTO;
import lk.informatics.ntc.model.exception.ApplicationException;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.TenderAdvertisementService;
import lk.informatics.ntc.model.service.TenderService;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.ntc.view.util.PropertyReader;
import lk.informatics.ntc.view.util.SpringApplicationContex;
import lk.informatics.unicode.UnicodeShaper;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

@ManagedBean(name = "advertisementBackingBean")
@ViewScoped

public class GenerateAdvertisementBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	private TenderAdvertisementService addService;
	private CommonService commonService;
	private TenderService tenderService;
	private AdvertisementDTO tempheadfootDTO = new AdvertisementDTO();
	private TenderDTO tenderDTO = new TenderDTO();
	private AdvertisementDTO advertisementDTO = new AdvertisementDTO();
	private List<AdvertisementDTO> dataList = new ArrayList<AdvertisementDTO>();
	private List<AdvertisementDTO> dataTableList = new ArrayList<AdvertisementDTO>();
	private String selectedTenderNo;
	private boolean editable = false;
	private int language = 0; // if english 1 if sinhala 2 if tamil 3
	private String errorMsg;
	private boolean searchcheck = false;
	private boolean savecheck = true;
	public String successMSG;
	private boolean printCheck = true;
	private boolean editCheck = true;
	private StreamedContent files;
	private String destination = "/tmp/";

	private JasperPrint jp;

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	@PostConstruct
	public void init() {

		addService = (TenderAdvertisementService) SpringApplicationContex.getBean("addService");
		tenderService = (TenderService) SpringApplicationContex.getBean("tenderService");
		tenderService = (TenderService) SpringApplicationContex.getBean("tenderService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		dataList = addService.refNodropdown();

	}

	public void onFooterchange() {

	}

	public void onHeaderChange() {

	}

	public void onrefNoChange() {

		String description = addService.description(advertisementDTO.getTenderNo());
		advertisementDTO.setDescription(description);
		searchcheck = false;

	}

	public void search() {

		if (advertisementDTO.getTenderNo().equals("")) {
			errorMsg = "Please select a Reference No.";
			RequestContext.getCurrentInstance().update("xx");
			RequestContext.getCurrentInstance().execute("PF('fill').show()");
		} else {

			if (addService.checkTenderStatus(advertisementDTO, null) == false) {

				searchcheck = true;
				editCheck = false;
				tempheadfootDTO = addService.headerfooter(advertisementDTO.getTenderNo());
				savecheck = false;
				advertisementDTO.setFooter(tempheadfootDTO.getFooter());
				advertisementDTO.setHeader(tempheadfootDTO.getHeader());
				dataTableList = addService.dataTable(advertisementDTO.getTenderNo());

				tenderDTO.setTenderRefNo(advertisementDTO.getTenderNo());

				commonService.updateTaskStatusTenderInSurveyTaskTabel(tenderDTO.getTenderRefNo(), "TD001", "TD002", "C",
						sessionBackingBean.getLoginUser());

			} else {
				errorMsg = "Advertisment Already Generated";
				RequestContext.getCurrentInstance().update("xx");
				RequestContext.getCurrentInstance().execute("PF('fill').show()");

				savecheck = true;
				printCheck = false;
				searchcheck = true;
				editCheck = false;

				tempheadfootDTO = addService.headerfooter(advertisementDTO.getTenderNo());
				advertisementDTO.setFooter(tempheadfootDTO.getFooter());
				advertisementDTO.setHeader(tempheadfootDTO.getHeader());
				dataTableList = addService.dataTable(advertisementDTO.getTenderNo());
				tenderDTO.setTenderRefNo(advertisementDTO.getTenderNo());
			}
		}

	}

	public void save() {

		if (advertisementDTO.getFooter() == null || advertisementDTO.getHeader().equals("")
				|| (advertisementDTO.getFooter() == null || advertisementDTO.getFooter().equals(""))) {

			errorMsg = "Please fill all mandatory fields.";
			RequestContext.getCurrentInstance().update("xx");
			RequestContext.getCurrentInstance().execute("PF('fill').show()");
		} else {
			if (language == 0) {
				language = 1;
			}

			boolean Save = addService.insertHeaderFooter(advertisementDTO, language, sessionBackingBean.getLoginUser());
			addService.updateTenderStatus(advertisementDTO);
			tenderDTO.setTenderRefNo(advertisementDTO.getTenderNo());

			if (Save == true) {

				commonService.updateTaskStatusCompletedTenderInSurveyTaskTabel(tenderDTO.getTenderRefNo(), "TD002");

				successMSG = "Data Saved Successfully.";
				RequestContext.getCurrentInstance().update("frmsuccess");
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
				printCheck = false;
				dataList = addService.refNodropdown();

			} else {

				errorMsg = "Data Saved Not Successfully";
				RequestContext.getCurrentInstance().update("xx");
				RequestContext.getCurrentInstance().execute("PF('fill').show()");
				printCheck = true;

			}

		}
	}

	public void changeTest(ValueChangeEvent event) {

	}

	public void english() {
		tempheadfootDTO = addService.headerfooter(advertisementDTO.getTenderNo());
		advertisementDTO.setFooter(tempheadfootDTO.getFooter());
		advertisementDTO.setHeader(tempheadfootDTO.getHeader());
		language = 1;
	}

	public void sinhala() {
		tempheadfootDTO = addService.headerfootersinhala(advertisementDTO.getTenderNo());
		advertisementDTO.setFooter(tempheadfootDTO.getFooter());
		advertisementDTO.setHeader(tempheadfootDTO.getHeader());
		language = 2;
	}

	public void tamil() {
		tempheadfootDTO = addService.headerfootertamil(advertisementDTO.getTenderNo());
		advertisementDTO.setFooter(tempheadfootDTO.getFooter());
		advertisementDTO.setHeader(tempheadfootDTO.getHeader());
		language = 3;
	}

	public void onRowEdit(RowEditEvent event) {

		AdvertisementDTO dto = new AdvertisementDTO();
		String tenderNo = advertisementDTO.getTenderNo();
		dto = ((AdvertisementDTO) event.getObject());
		addService.insertDataTable(dto, tenderNo);
	}

	public void onRowCancel(RowEditEvent event) {

	}

	public void clear() {
		advertisementDTO = new AdvertisementDTO();
		dataTableList = null;
		editCheck = true;
		language = 0;
		savecheck = true;
		searchcheck = false;
		printCheck = true;

	}

	public void edit() {
		editable = true;
	}

	// print advertisement
	public StreamedContent print(ActionEvent ae) throws JRException, FileNotFoundException {

		String tenderRefNo;
		tenderRefNo = advertisementDTO.getTenderNo();
		files = null;
		String sourceFileName = null;

		Connection conn = null;

		try {
			conn = ConnectionManager.getConnection();

			sourceFileName = "..//reports//TenderAdvertisetment.jrxml";
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_tender_ref_no", tenderRefNo);
			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));
			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", "Tender advertisement.pdf");

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("reportBytes", pdfByteArray);
			sessionMap.put("docType", "pdf");
			savePdf();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(conn);
		}
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		try {
			ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
		} catch (IOException e) {

			e.printStackTrace();
		}
		return files;

	}

	public void savePdf() throws JRException {

		Connection conn = null;
		String tenderRefNo = advertisementDTO.getTenderNo();

		conn = ConnectionManager.getConnection();
		String jrxmlFilePath = "..//reports//TenderAdvertisetment.jrxml";

		String exportDir = null;
		try {
			exportDir = PropertyReader.getPropertyValue("documentManagement.upload.ad.path");
			String exportPath = exportDir + "/Tender Advertisement" + tenderRefNo + ".pdf";
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_tender_ref_no", tenderRefNo);

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(jrxmlFilePath));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
			JasperExportManager.exportReportToPdfFile(jasperPrint, exportPath);
		} catch (ApplicationException e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(conn);
		}

	}

	public void footerChange() {

	}

	public void headerChange() {

	}

	public void status() {

	}

	// getters and setters
	public TenderAdvertisementService getAddService() {
		return addService;
	}

	public void setAddService(TenderAdvertisementService addService) {
		this.addService = addService;
	}

	public AdvertisementDTO getAdvertisementDTO() {
		return advertisementDTO;
	}

	public void setAdvertisementDTO(AdvertisementDTO advertisementDTO) {
		this.advertisementDTO = advertisementDTO;
	}

	public List<AdvertisementDTO> getDataList() {
		return dataList;
	}

	public void setDataList(List<AdvertisementDTO> dataList) {
		this.dataList = dataList;
	}

	public String getSelectedTenderNo() {
		return selectedTenderNo;
	}

	public void setSelectedTenderNo(String selectedTenderNo) {
		this.selectedTenderNo = selectedTenderNo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public AdvertisementDTO getTempheadfootDTO() {
		return tempheadfootDTO;
	}

	public void setTempheadfootDTO(AdvertisementDTO tempheadfootDTO) {
		this.tempheadfootDTO = tempheadfootDTO;
	}

	public List<AdvertisementDTO> getDataTableList() {
		return dataTableList;
	}

	public void setDataTableList(List<AdvertisementDTO> dataTableList) {
		this.dataTableList = dataTableList;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public int getLanguage() {
		return language;
	}

	public void setLanguage(int language) {
		this.language = language;
	}

	public boolean isSearchcheck() {
		return searchcheck;
	}

	public void setSearchcheck(boolean searchcheck) {
		this.searchcheck = searchcheck;
	}

	public boolean isSavecheck() {
		return savecheck;
	}

	public void setSavecheck(boolean savecheck) {
		this.savecheck = savecheck;
	}

	public TenderDTO getTenderDTO() {
		return tenderDTO;
	}

	public void setTenderDTO(TenderDTO tenderDTO) {
		this.tenderDTO = tenderDTO;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public TenderService getTenderService() {
		return tenderService;
	}

	public void setTenderService(TenderService tenderService) {
		this.tenderService = tenderService;
	}

	public String getSuccessMSG() {
		return successMSG;
	}

	public void setSuccessMSG(String successMSG) {
		this.successMSG = successMSG;
	}

	public boolean isPrintCheck() {
		return printCheck;
	}

	public void setPrintCheck(boolean printCheck) {
		this.printCheck = printCheck;
	}

	public boolean isEditCheck() {
		return editCheck;
	}

	public void setEditCheck(boolean editCheck) {
		this.editCheck = editCheck;
	}

	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
	}

}
