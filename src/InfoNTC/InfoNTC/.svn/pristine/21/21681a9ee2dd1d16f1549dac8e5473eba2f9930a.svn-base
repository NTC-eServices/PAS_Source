package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.DriverConductorTrainingDTO;
import lk.informatics.ntc.model.dto.DropDownDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.DriverConductorTrainingService;
import lk.informatics.ntc.model.service.GrievanceManagementService;
import lk.informatics.ntc.view.util.ConnectionManager;
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

/**
 * 
 * @author dilakshi.h
 *
 */
@ViewScoped
@ManagedBean(name = "driverConductorTrainingBackingBean")
public class DriverConductorTrainingBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private DriverConductorTrainingService driverConductorTrainingService;
	private GrievanceManagementService grievanceManagementService;
	private CommonService commonService;

	private String sucessMsg;
	private String errorMsg;

	private String dateFormatStr = "dd/MM/yyyy";
	private DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
	DateFormat oldDf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
	SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatStr);

	private List<CommonDTO> trainingTypeList;
	private List<DropDownDTO> applicationNoList;
	private List<DropDownDTO> driverList;
	private List<DropDownDTO> conductorList;
	private List<DriverConductorTrainingDTO> dcTrainingDatesList = new ArrayList<DriverConductorTrainingDTO>();
	private List<DriverConductorTrainingDTO> selectedTrainingDatesList = new ArrayList<DriverConductorTrainingDTO>();
	private List<String> scheduleDates = new ArrayList<String>();
	private DriverConductorTrainingDTO saveData = new DriverConductorTrainingDTO();
	private String selectedTrainingType;
	private Date registerStart;
	private Date registerEnd;
	private String driConName;
	private String applicationNo;
	private String driverId;
	private String conductorId;
	private String allDate;
	private boolean dataSaved;
	private boolean letterPrinted;
	private StreamedContent files;
	private String languageMediumCode;

	public DriverConductorTrainingBackingBean() {
		driverConductorTrainingService = (DriverConductorTrainingService) SpringApplicationContex
				.getBean("driverConductorTrainingService");
		grievanceManagementService = (GrievanceManagementService) SpringApplicationContex
				.getBean("grievanceManagementService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");

		trainingTypeList = driverConductorTrainingService.GetAllTrainingTypesWithoutDuplicate();
		getApplicationNos(null);
		getDrivers(null);
		getConductors(null);
		clearSearchView();
	}

	public List<String> completeAppNo(String query) {
		query = query.toUpperCase();
		List<String> filteredNo = new ArrayList<String>();

		for (int i = 0; i < applicationNoList.size(); i++) {
			String cm = applicationNoList.get(i).getCode();
			if (cm.toUpperCase().contains(query)) {
				filteredNo.add(cm);
			}
		}
		return filteredNo;
	}

	public List<String> completeDriverId(String query) {
		query = query.toUpperCase();
		List<String> filteredNo = new ArrayList<String>();

		for (int i = 0; i < driverList.size(); i++) {
			String cm = driverList.get(i).getCode();
			if (cm.toUpperCase().contains(query)) {
				filteredNo.add(cm);
			}
		}
		return filteredNo;
	}

	public List<String> completeConductorId(String query) {
		query = query.toUpperCase();
		List<String> filteredNo = new ArrayList<String>();

		for (int i = 0; i < conductorList.size(); i++) {
			String cm = conductorList.get(i).getCode();
			if (cm.toUpperCase().contains(query)) {
				filteredNo.add(cm);
			}
		}
		return filteredNo;
	}

	public void loadTrainingData(boolean pendingOnly) {
		scheduleDates = driverConductorTrainingService.getScheduledTrainingDates(selectedTrainingType);
		dcTrainingDatesList = driverConductorTrainingService.getDriverConductorTrainings(pendingOnly,
				selectedTrainingType, registerStart, registerEnd, applicationNo, driConName, driverId, conductorId,
				languageMediumCode);
		selectedTrainingDatesList = new ArrayList<DriverConductorTrainingDTO>();
		allDate = null;
		dataSaved = false;
		letterPrinted = false;
	}

	public void clearSearchView() {
		selectedTrainingType = "";
		registerStart = null;
		registerEnd = null;
		applicationNo = null;
		driConName = null;
		driverId = null;
		conductorId = null;
		languageMediumCode = null;
		if (trainingTypeList != null && !trainingTypeList.isEmpty()) {
			selectedTrainingType = trainingTypeList.get(0).getCode();
			getApplicationNos(selectedTrainingType);
		}
		loadTrainingData(true);
	}

	public void searchTraining() {
		if ((selectedTrainingType == null || selectedTrainingType.isEmpty()) && registerStart == null
				&& registerEnd == null && applicationNo == null && driverId == null && conductorId == null) {
			errorMsg = "Please enter a value first.";
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
			return;
		}

		loadTrainingData(false);

		if (dcTrainingDatesList.isEmpty()) {
			showMsg("ERROR", "No Data found.");
			return;
		}
	}

	public void setAllDate() {
		if (allDate == null || allDate.isEmpty()) {
			showMsg("ERROR", "Please Select a Date first");
			return;
		}
		for (DriverConductorTrainingDTO row : dcTrainingDatesList) {
			row.setSelected(true);
			row.setTrainingDate(allDate);
		}
		selectedTrainingDatesList = dcTrainingDatesList;
		allDate = null;
	}

	public void saveTrainingDates() {
		String user = sessionBackingBean.loginUser;

		if (selectedTrainingDatesList == null || selectedTrainingDatesList.isEmpty()) {
			showMsg("ERROR", "No Changes have been done");
			return;
		}

		for (DriverConductorTrainingDTO row : selectedTrainingDatesList) {
			if (row.isSelected() && row.getTrainingDate() == null) {
				showMsg("ERROR", "Please select a training date");
				return;
			}
			if (!driverConductorTrainingService.saveTraingingDates(row, user)) {
				showMsg("ERROR", "Error saving Training dates");

				return;
			}
		}
		
		for(DriverConductorTrainingDTO data : selectedTrainingDatesList) {
			saveData.setAppNo(data.getAppNo());
			saveData.setNic(data.getNic());
			saveData.setDriverConductorId(data.getDriverConductorId());
			
			driverConductorTrainingService.beanLinkMethod(saveData, sessionBackingBean.getLoginUser(), "Save Training Dates", "Maintain Driver / Conductor Training Dates");
		}
		
		showMsg("SUCCESS", "Saved Successfully");
		dataSaved = true;
		selectedTrainingDatesList = new ArrayList<DriverConductorTrainingDTO>();
	}

	public void printLetters() throws JRException {
		String user = sessionBackingBean.loginUser;
		if (selectedTrainingDatesList == null || selectedTrainingDatesList.isEmpty()) {
			showMsg("ERROR", "Please select a application number");
			return;
		}

		if (selectedTrainingDatesList.get(0).getTrainingDate() == null
				|| selectedTrainingDatesList.get(0).getTrainingDate().isEmpty()) {
			showMsg("ERROR", "Please save a training date.");

			return;

		} else {

			if (selectedTrainingDatesList.get(0).getTrainingDate() != null) {
				if (!driverConductorTrainingService.updateStatus("A", null,
						selectedTrainingDatesList.get(0).getStatusTypeCode(),
						selectedTrainingDatesList.get(0).getStatusCode(), selectedTrainingDatesList.get(0).getAppNo(),
						user)) {
					showMsg("ERROR", "Error printing letters");

				}
			}
			// Driving Training Letter generate
			if (selectedTrainingType.equalsIgnoreCase("ND")) {
				downloadLetter();
				
				for(DriverConductorTrainingDTO data : selectedTrainingDatesList) {
					saveData.setAppNo(data.getAppNo());
					saveData.setNic(data.getNic());
					saveData.setDriverConductorId(data.getDriverConductorId());
					
					driverConductorTrainingService.beanLinkMethod(saveData, sessionBackingBean.getLoginUser(),"Print Training Letter(ND)", "Maintain Driver / Conductor Training Dates");
				}
				
				
			}

			if (selectedTrainingType.equalsIgnoreCase("NC")) {
				downloadLetter1();
				
				for(DriverConductorTrainingDTO data : selectedTrainingDatesList) {
					saveData.setAppNo(data.getAppNo());
					saveData.setNic(data.getNic());
					saveData.setDriverConductorId(data.getDriverConductorId());
					
					driverConductorTrainingService.beanLinkMethod(saveData, sessionBackingBean.getLoginUser(),"Print Training Letter(NC)", "Maintain Driver / Conductor Training Dates");
				}
			}

			if (selectedTrainingType.equalsIgnoreCase("RD") || selectedTrainingType.equalsIgnoreCase("RC")) {
				downloadLetter2();
				
				for(DriverConductorTrainingDTO data : selectedTrainingDatesList) {
					saveData.setAppNo(data.getAppNo());
					saveData.setNic(data.getNic());
					saveData.setDriverConductorId(data.getDriverConductorId());
					
					driverConductorTrainingService.beanLinkMethod(saveData, sessionBackingBean.getLoginUser(),"Print Training Letter(DC)", "Maintain Driver / Conductor Training Dates");
				}
			}

			if (selectedTrainingType.equalsIgnoreCase("RRD") || selectedTrainingType.equalsIgnoreCase("RRRD")
					|| selectedTrainingType.equalsIgnoreCase("RRC") || selectedTrainingType.equalsIgnoreCase("RRRC")) {
				downloadLetter3();
				
				for(DriverConductorTrainingDTO data : selectedTrainingDatesList) {
					saveData.setAppNo(data.getAppNo());
					saveData.setNic(data.getNic());
					saveData.setDriverConductorId(data.getDriverConductorId());
					
					driverConductorTrainingService.beanLinkMethod(saveData, sessionBackingBean.getLoginUser(),"Print Training Letter(RDC)", "Maintain Driver / Conductor Training Dates");
				}
			}

		}

		selectedTrainingDatesList = new ArrayList<DriverConductorTrainingDTO>();

	}

	public StreamedContent downloadLetter() throws JRException {
		files = null;
		String sourceFileName = null;

		Connection conn = null;

		try {
			conn = ConnectionManager.getConnection();

			sourceFileName = "..//reports//DriverTrainingCourse.jrxml";

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_training_type", selectedTrainingType);
			if (selectedTrainingDatesList != null && !selectedTrainingDatesList.isEmpty()) {
				parameters.put("P_app_no", selectedTrainingDatesList.get(0).getAppNo());

			}
			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", "Letter" + ".pdf");
			
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("reportBytes", pdfByteArray);
			sessionMap.put("docType", "pdf");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(conn);
		}

		return files;

	}

	public StreamedContent downloadLetter1() throws JRException {
		files = null;
		String sourceFileName = null;

		Connection conn = null;

		try {
			conn = ConnectionManager.getConnection();

			sourceFileName = "..//reports//ConductorTrainingCourse.jrxml";

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_training_type", selectedTrainingType);
			if (selectedTrainingDatesList != null && !selectedTrainingDatesList.isEmpty()) {
				parameters.put("P_app_no", selectedTrainingDatesList.get(0).getAppNo());
			}
			
			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", "Letter" + ".pdf");
			
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("reportBytes", pdfByteArray);
			sessionMap.put("docType", "pdf");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(conn);
		}
		return files;

	}

	public StreamedContent downloadLetter2() throws JRException {
		files = null;
		String sourceFileName = null;

		Connection conn = null;

		try {
			conn = ConnectionManager.getConnection();

			sourceFileName = "..//reports//DriverConductorKnowladgeUpdate.jrxml";

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_training_type", selectedTrainingType);
			if (selectedTrainingDatesList != null && !selectedTrainingDatesList.isEmpty()) {
				parameters.put("P_driver_id", selectedTrainingDatesList.get(0).getDriverConductorId());
			}

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", "Letter" + ".pdf");
			
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("reportBytes", pdfByteArray);
			sessionMap.put("docType", "pdf");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(conn);
		}

		return files;

	}

	public StreamedContent downloadLetter3() throws JRException {
		files = null;
		String sourceFileName = null;

		Connection conn = null;

		try {
			conn = ConnectionManager.getConnection();

			sourceFileName = "..//reports//BlackListedLetter.jrxml";

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_training_type", selectedTrainingType);
			if (selectedTrainingDatesList != null && !selectedTrainingDatesList.isEmpty()) {
				parameters.put("P_driver_id", selectedTrainingDatesList.get(0).getDriverConductorId());
			}

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", "Letter" + ".pdf");
			
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("reportBytes", pdfByteArray);
			sessionMap.put("docType", "pdf");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(conn);
		}

		return files;

	}

	public void printAddressLabels() {

	}

	public void changeTrainingType() {
		getApplicationNos(selectedTrainingType);
		getDrivers(selectedTrainingType);
		getConductors(selectedTrainingType);
	}

	private void getApplicationNos(String trainingType) {
		if (trainingType != null && !trainingType.trim().isEmpty())
			applicationNoList = grievanceManagementService.getApplicationNoByTrainingType(trainingType);
		else
			applicationNoList = grievanceManagementService.getApplicationNos();
	}

	private void getDrivers(String trainingType) {
		if (trainingType != null && !trainingType.trim().isEmpty())
			driverList = grievanceManagementService.getDriversByTrainingType(trainingType);
		else
			driverList = grievanceManagementService.getDrivers();
	}

	private void getConductors(String trainingType) {
		if (trainingType != null && !trainingType.trim().isEmpty())
			conductorList = grievanceManagementService.getConductorsByTrainingType(trainingType);
		else
			conductorList = grievanceManagementService.getConductors();
	}

	public void onRowSelect(SelectEvent event) {
		DriverConductorTrainingDTO selectedRow = (DriverConductorTrainingDTO) event.getObject();

		for (DriverConductorTrainingDTO r : dcTrainingDatesList) {
			if (r.getAppNo().equalsIgnoreCase(selectedRow.getAppNo()))
				r.setSelected(true);
		}
	}

	public void onRowUnselect(UnselectEvent event) {
		DriverConductorTrainingDTO selectedRow = (DriverConductorTrainingDTO) event.getObject();

		for (DriverConductorTrainingDTO r : dcTrainingDatesList) {
			if (r.getAppNo().equalsIgnoreCase(selectedRow.getAppNo()))
				r.setSelected(false);
		}
	}

	// Common methods
	public void showMsg(String type, String msg) {
		if (type.equalsIgnoreCase("ERROR")) {
			errorMsg = msg;
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
		} else {
			sucessMsg = msg;
			RequestContext.getCurrentInstance().update("frmSuccess");
			RequestContext.getCurrentInstance().execute("PF('successDialog').show()");
		}
	}

	private static <T> Iterable<T> nullSafe(Iterable<T> iterable) {
		return iterable == null ? Collections.<T>emptyList() : iterable;
	}

	//////////////// getter & setters /////////////////////

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public GrievanceManagementService getGrievanceManagementService() {
		return grievanceManagementService;
	}

	public void setGrievanceManagementService(GrievanceManagementService grievanceManagementService) {
		this.grievanceManagementService = grievanceManagementService;
	}

	public String getSucessMsg() {
		return sucessMsg;
	}

	public void setSucessMsg(String sucessMsg) {
		this.sucessMsg = sucessMsg;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getDateFormatStr() {
		return dateFormatStr;
	}

	public void setDateFormatStr(String dateFormatStr) {
		this.dateFormatStr = dateFormatStr;
	}

	public DateTimeFormatter getTimeFormat() {
		return timeFormat;
	}

	public void setTimeFormat(DateTimeFormatter timeFormat) {
		this.timeFormat = timeFormat;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public List<DropDownDTO> getApplicationNoList() {
		return applicationNoList;
	}

	public void setApplicationNoList(List<DropDownDTO> applicationNoList) {
		this.applicationNoList = applicationNoList;
	}

	public List<DropDownDTO> getDriverList() {
		return driverList;
	}

	public void setDriverList(List<DropDownDTO> driverList) {
		this.driverList = driverList;
	}

	public List<DropDownDTO> getConductorList() {
		return conductorList;
	}

	public void setConductorList(List<DropDownDTO> conductorList) {
		this.conductorList = conductorList;
	}

	public String getSelectedTrainingType() {
		return selectedTrainingType;
	}

	public void setSelectedTrainingType(String selectedTrainingType) {
		this.selectedTrainingType = selectedTrainingType;
	}

	public Date getRegisterStart() {
		return registerStart;
	}

	public void setRegisterStart(Date registerStart) {
		this.registerStart = registerStart;
	}

	public Date getRegisterEnd() {
		return registerEnd;
	}

	public void setRegisterEnd(Date registerEnd) {
		this.registerEnd = registerEnd;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public String getConductorId() {
		return conductorId;
	}

	public void setConductorId(String conductorId) {
		this.conductorId = conductorId;
	}

	public String getAllDate() {
		return allDate;
	}

	public void setAllDate(String allDate) {
		this.allDate = allDate;
	}

	public List<DriverConductorTrainingDTO> getDcTrainingDatesList() {
		return dcTrainingDatesList;
	}

	public void setDcTrainingDatesList(List<DriverConductorTrainingDTO> dcTrainingDatesList) {
		this.dcTrainingDatesList = dcTrainingDatesList;
	}

	public List<String> getScheduleDates() {
		return scheduleDates;
	}

	public void setScheduleDates(List<String> scheduleDates) {
		this.scheduleDates = scheduleDates;
	}

	public DriverConductorTrainingService getDriverConductorTrainingService() {
		return driverConductorTrainingService;
	}

	public void setDriverConductorTrainingService(DriverConductorTrainingService driverConductorTrainingService) {
		this.driverConductorTrainingService = driverConductorTrainingService;
	}

	public List<CommonDTO> getTrainingTypeList() {
		return trainingTypeList;
	}

	public void setTrainingTypeList(List<CommonDTO> trainingTypeList) {
		this.trainingTypeList = trainingTypeList;
	}

	public String getDriConName() {
		return driConName;
	}

	public void setDriConName(String driConName) {
		this.driConName = driConName;
	}

	public boolean isDataSaved() {
		return dataSaved;
	}

	public void setDataSaved(boolean dataSaved) {
		this.dataSaved = dataSaved;
	}

	public boolean isLetterPrinted() {
		return letterPrinted;
	}

	public void setLetterPrinted(boolean letterPrinted) {
		this.letterPrinted = letterPrinted;
	}

	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
	}

	public List<DriverConductorTrainingDTO> getSelectedTrainingDatesList() {
		return selectedTrainingDatesList;
	}

	public void setSelectedTrainingDatesList(List<DriverConductorTrainingDTO> selectedTrainingDatesList) {
		this.selectedTrainingDatesList = selectedTrainingDatesList;
	}

	public String getLanguageMediumCode() {
		return languageMediumCode;
	}

	public void setLanguageMediumCode(String languageMediumCode) {
		this.languageMediumCode = languageMediumCode;
	}

	public DriverConductorTrainingDTO getSaveData() {
		return saveData;
	}

	public void setSaveData(DriverConductorTrainingDTO saveData) {
		this.saveData = saveData;
	}
	

}
