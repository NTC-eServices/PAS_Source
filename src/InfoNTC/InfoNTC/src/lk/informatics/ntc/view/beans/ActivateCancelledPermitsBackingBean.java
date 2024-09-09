package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.ActivateCancelledPermitsDTO;
import lk.informatics.ntc.model.dto.DocumentManagementDTO;
import lk.informatics.ntc.model.service.CancellationsPermitService;
import lk.informatics.ntc.model.service.DocumentManagementService;
import lk.informatics.ntc.model.service.PermitRenewalsService;
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

@ManagedBean(name = "activateCancelledPermitsBackingBean")
@ViewScoped
public class ActivateCancelledPermitsBackingBean {
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private String selectedPermitNo;
	private String selectedVehicleNo;
	private String errorMsg;
	private String sucessMsg;
	private String alertMsg;
	private String taskCode = "PM402";
	private String activateTaskCode = "PM403";

	private boolean disabledActiveBtn = true;
	private boolean disablePrintActivationLetterBtn = true;
	private boolean disableUploadDocumentsBtn = true;

	private ActivateCancelledPermitsDTO activateCancelledPermitsDTO = new ActivateCancelledPermitsDTO();
	private ActivateCancelledPermitsDTO selectDTO;
	private ActivateCancelledPermitsDTO selectedVieweRow;
	private ActivateCancelledPermitsDTO loadCurrentApplicationNo;
	private ActivateCancelledPermitsDTO searchedDTO;
	private ActivateCancelledPermitsDTO defaultViewDTO;
	private ActivateCancelledPermitsDTO onChangedDetailsDTO;
	private ActivateCancelledPermitsDTO viewedDetailsForViewBtnDTO;
	private ActivateCancelledPermitsDTO viewedDistrictDesForDistrictCodeDTO;
	private ActivateCancelledPermitsDTO viewedDivisionDesForDivisionCodeDTO;

	CancellationsPermitService cancellationsPermitService;
	private DocumentManagementService documentManagementService;
	private PermitRenewalsService permitRenewalsService;

	public List<ActivateCancelledPermitsDTO> permitNoList = new ArrayList<ActivateCancelledPermitsDTO>(0);
	public List<ActivateCancelledPermitsDTO> currentvehicleNoList = new ArrayList<ActivateCancelledPermitsDTO>(0);
	public List<ActivateCancelledPermitsDTO> vehicleNoList = new ArrayList<ActivateCancelledPermitsDTO>(0);
	public List<ActivateCancelledPermitsDTO> dataList = new ArrayList<ActivateCancelledPermitsDTO>(0);
	public List<ActivateCancelledPermitsDTO> searchedList = new ArrayList<ActivateCancelledPermitsDTO>(0);
	public List<ActivateCancelledPermitsDTO> searchedListForSelectedCancelledDate = new ArrayList<ActivateCancelledPermitsDTO>(
			0);
	public List<ActivateCancelledPermitsDTO> documentsList = new ArrayList<ActivateCancelledPermitsDTO>(0);
	private List<DocumentManagementDTO> mandatoryList = new ArrayList<DocumentManagementDTO>(0);
	private boolean disablePrintPermit;
	public StreamedContent files;
	public String appNo = null;

	@PostConstruct
	public void init() {
		permitNoList = cancellationsPermitService.getPermitNoList();
		loadVehicleList();
		loadDefaultDataList();
		setDisablePrintPermit(true);
	}

	public ActivateCancelledPermitsBackingBean() {
		cancellationsPermitService = (CancellationsPermitService) SpringApplicationContex
				.getBean("cancellationsPermitService");
		documentManagementService = (DocumentManagementService) SpringApplicationContex
				.getBean("documentManagementService");
		permitRenewalsService = (PermitRenewalsService) SpringApplicationContex.getBean("permitRenewalsService");
	}

	private void loadVehicleList() {

		for (int i = 0; i < permitNoList.size(); i++) {
			String currentPermitNo = permitNoList.get(i).getPermitNo();
			loadCurrentApplicationNo = cancellationsPermitService.getCurrentApplicationNo(currentPermitNo);
			String currentApplicationNo = loadCurrentApplicationNo.getApplicationNo();
			currentvehicleNoList = cancellationsPermitService.getCurrentVehicleNo(currentApplicationNo,
					currentPermitNo);
			vehicleNoList.addAll(currentvehicleNoList);
		}
	}

	public void onPermitNoChange() throws ParseException {
		if (!selectedPermitNo.equals("")) {
			onChangedDetailsDTO = cancellationsPermitService.getLoadDetailsForSelectedPermitNo(selectedPermitNo);
			activateCancelledPermitsDTO.setCancelledDate(onChangedDetailsDTO.getCancelledDate());
			activateCancelledPermitsDTO.setApplicationNo(onChangedDetailsDTO.getApplicationNo());
			setSelectedVehicleNo(onChangedDetailsDTO.getRegNoOfTheBus());
			String dateFormat = "dd/MM/yyyy";
			SimpleDateFormat frm = new SimpleDateFormat(dateFormat);
			Date cancelledDateObject = frm.parse(activateCancelledPermitsDTO.getCancelledDate());
			activateCancelledPermitsDTO.setCancelledDateObj(cancelledDateObject);
		} else {
			clearFields();
		}

	}

	public void onVehicleNoChange() throws ParseException {
		if (!selectedVehicleNo.equals("")) {
			onChangedDetailsDTO = cancellationsPermitService.getLoadDetailsForSelectedVehicleNo(selectedVehicleNo);
			activateCancelledPermitsDTO.setCancelledDate(onChangedDetailsDTO.getCancelledDate());
			activateCancelledPermitsDTO.setApplicationNo(onChangedDetailsDTO.getApplicationNo());
			setSelectedPermitNo(onChangedDetailsDTO.getPermitNo());
			String dateFormat = "dd/MM/yyyy";
			SimpleDateFormat frm = new SimpleDateFormat(dateFormat);
			Date cancelledDateObject = frm.parse(activateCancelledPermitsDTO.getCancelledDate());
			activateCancelledPermitsDTO.setCancelledDateObj(cancelledDateObject);
		} else {
			clearFields();
		}
	}

	public void onCanceledDateChange(SelectEvent event) {
		if (activateCancelledPermitsDTO.getCancelledDateObj() != null) {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			String dateFormat = "dd/MM/yyyy";
			SimpleDateFormat frm = new SimpleDateFormat(dateFormat);
			String canceledDateNewValue = frm.format(event.getObject());
			activateCancelledPermitsDTO.setCancelledDate(canceledDateNewValue);
			filterPermitNoList(activateCancelledPermitsDTO.getCancelledDate());
		} else {

		}
	}

	private void filterPermitNoList(String cancelledDate) {
		permitNoList = new ArrayList<ActivateCancelledPermitsDTO>(0);
		vehicleNoList = new ArrayList<ActivateCancelledPermitsDTO>(0);
		currentvehicleNoList = new ArrayList<ActivateCancelledPermitsDTO>(0);
		permitNoList = cancellationsPermitService.getPermitNoListForSelectedDate(cancelledDate);
		loadVehicleList();
	}

	public void searchAction() {

		if (selectedPermitNo.equals("") && selectedVehicleNo.equals("")
				&& activateCancelledPermitsDTO.getCancelledDate() == null) {

			errorMsg = "Permit No. or Registration No. of the Bus or Cancelled Date should be selected.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else if (!selectedPermitNo.equals("") && selectedVehicleNo.equals("")
				&& activateCancelledPermitsDTO.getCancelledDate() == null) {

			searchDetails();
		} else if (selectedPermitNo.equals("") && !selectedVehicleNo.equals("")
				&& activateCancelledPermitsDTO.getCancelledDate() == null) {

			searchDetails();
		} else if (selectedPermitNo.equals("") && selectedVehicleNo.equals("")
				&& activateCancelledPermitsDTO.getCancelledDate() != null) {

			searchedListForCancelledDate();
		} else if (!selectedPermitNo.equals("") && !selectedVehicleNo.equals("")
				&& activateCancelledPermitsDTO.getCancelledDate() == null) {

			searchDetails();
		} else if (!selectedPermitNo.equals("") && selectedVehicleNo.equals("")
				&& activateCancelledPermitsDTO.getCancelledDate() != null) {

			searchDetails();
		} else if (selectedPermitNo.equals("") && !selectedVehicleNo.equals("")
				&& activateCancelledPermitsDTO.getCancelledDate() != null) {

			searchDetails();
		} else if (!selectedPermitNo.equals("") && !selectedVehicleNo.equals("")
				&& activateCancelledPermitsDTO.getCancelledDate() != null) {

			searchDetails();
		}
	}

	private void searchedListForCancelledDate() {

		searchedListForSelectedCancelledDate = cancellationsPermitService
				.getAllRecordsForSelectedCancelledDate(activateCancelledPermitsDTO.getCancelledDate());
		if (searchedListForSelectedCancelledDate.isEmpty()) {
			setErrorMsg("No records for selected cancelled date.");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else {

		}
		dataList = new ArrayList<ActivateCancelledPermitsDTO>(0);
		dataList.addAll(searchedListForSelectedCancelledDate);
	}

	private void searchDetails() {

		searchedDTO = cancellationsPermitService.getAllDetailsForSelectedDetails(selectedPermitNo, selectedVehicleNo,
				activateCancelledPermitsDTO.getCancelledDate());
		activateCancelledPermitsDTO.setSeqNoCancelRecord(searchedDTO.getSeqNoCancelRecord());
		activateCancelledPermitsDTO.setPermitNo(searchedDTO.getPermitNo());
		activateCancelledPermitsDTO.setApplicationNo(searchedDTO.getApplicationNo());
		activateCancelledPermitsDTO.setCancelStatus(searchedDTO.getCancelStatus());
		activateCancelledPermitsDTO.setCancelFromDate(searchedDTO.getCancelFromDate());
		activateCancelledPermitsDTO.setCancelToDate(searchedDTO.getCancelToDate());
		activateCancelledPermitsDTO.setCancelledReason(searchedDTO.getCancelledReason());
		activateCancelledPermitsDTO.setActiveStatus(searchedDTO.getActiveStatus());
		activateCancelledPermitsDTO.setSpecialRemarks(searchedDTO.getSpecialRemarks());
		activateCancelledPermitsDTO.setIsActiveLetterPrinted(searchedDTO.getIsActiveLetterPrinted());
		activateCancelledPermitsDTO.setCancelledDate(searchedDTO.getCancelledDate());
		activateCancelledPermitsDTO.setRegNoOfTheBus(searchedDTO.getRegNoOfTheBus());
		activateCancelledPermitsDTO.setPermitOwner(searchedDTO.getPermitOwner());
		searchedList = new ArrayList<ActivateCancelledPermitsDTO>(0);
		searchedList.add(searchedDTO);
		dataList = new ArrayList<ActivateCancelledPermitsDTO>(0);
		dataList.addAll(searchedList);

	}

	public void clearFields() {

		setSelectedPermitNo(null);
		setSelectedVehicleNo(null);
		activateCancelledPermitsDTO.setCancelledDate(null);
		activateCancelledPermitsDTO.setCancelledDateObj(null);
		activateCancelledPermitsDTO.setSpecialRemarks(null);
		activateCancelledPermitsDTO.setCancelledDateObj(null);
		searchedList.clear();
		dataList.clear();
		permitNoList.clear();
		vehicleNoList.clear();
		permitNoList = new ArrayList<ActivateCancelledPermitsDTO>(0);
		permitNoList = cancellationsPermitService.getPermitNoList();
		vehicleNoList = new ArrayList<ActivateCancelledPermitsDTO>(0);
		currentvehicleNoList = new ArrayList<ActivateCancelledPermitsDTO>(0);
		loadVehicleList();
		loadDefaultDataList();
		selectDTO = new ActivateCancelledPermitsDTO();
		setDisabledActiveBtn(true);
		setDisablePrintActivationLetterBtn(true);
		setDisableUploadDocumentsBtn(true);
		setDisablePrintPermit(true);
		RequestContext.getCurrentInstance().update("btnPrint");
		RequestContext.getCurrentInstance().update("btnActive");
		RequestContext.getCurrentInstance().update("reject");
		RequestContext.getCurrentInstance().update("btnUploadDocument");
	}

	private void loadDefaultDataList() {
		setDataList(cancellationsPermitService.getDefaultRecords());
	}

	public void selectRow(SelectEvent event) {
		activateCancelledPermitsDTO.setSpecialRemarks(selectDTO.getSpecialRemarks());
		activateCancelledPermitsDTO.setApplicationNo(selectDTO.getApplicationNo());
		activateCancelledPermitsDTO.setPermitNo(selectDTO.getPermitNo());
		activateCancelledPermitsDTO.setRegNoOfTheBus(selectDTO.getRegNoOfTheBus());
		activateCancelledPermitsDTO.setSeqNoCancelRecord(selectDTO.getSeqNoCancelRecord());
		appNo = selectDTO.getApplicationNo();
		if (selectDTO.getActiveStatus() != null) {
			if (selectDTO.getActiveStatus().equals("A")) {
				setDisablePrintPermit(false);

			} else {
				setDisablePrintPermit(true);
			}
		}
		setDisabledActiveBtn(false);
		setDisableUploadDocumentsBtn(false);
		RequestContext.getCurrentInstance().update("btnActive");
		RequestContext.getCurrentInstance().update("reject");
		RequestContext.getCurrentInstance().update("btnUploadDocument");
	}

	public void ViewActButtonAction() {

		if (selectedVieweRow.getApplicationNo() != null && selectedVieweRow.getPermitNo() != null) {
			RequestContext.getCurrentInstance().execute("PF('renewalPermitDetailsDB').show()");
			loadViewDetailsForCurrentRecord();
		} else {
			errorMsg = "Permit No. or Application No. has no values";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	private void loadViewDetailsForCurrentRecord() {
		viewedDetailsForViewBtnDTO = cancellationsPermitService.getAllDetailsForSelectedViewBtn(
				selectedVieweRow.getApplicationNo(), selectedVieweRow.getPermitNo(),
				selectedVieweRow.getRegNoOfTheBus());
		activateCancelledPermitsDTO.setServiceTypeCode(viewedDetailsForViewBtnDTO.getServiceTypeCode());
		activateCancelledPermitsDTO.setRouteNo(viewedDetailsForViewBtnDTO.getRouteNo());
		activateCancelledPermitsDTO.setRouteFlagCode(viewedDetailsForViewBtnDTO.getRouteFlagCode());
		activateCancelledPermitsDTO.setRouteFlagIsChecked(viewedDetailsForViewBtnDTO.isRouteFlagIsChecked());
		activateCancelledPermitsDTO.setPermitExpiryDate(viewedDetailsForViewBtnDTO.getPermitExpiryDate());
		activateCancelledPermitsDTO.setServiceTypeDes(viewedDetailsForViewBtnDTO.getServiceTypeDes());
		activateCancelledPermitsDTO.setExpiredReason(viewedDetailsForViewBtnDTO.getExpiredReason());
		activateCancelledPermitsDTO.setCancelStatus(viewedDetailsForViewBtnDTO.getCancelStatus());
		activateCancelledPermitsDTO.setCancelFromDate(viewedDetailsForViewBtnDTO.getCancelFromDate());
		activateCancelledPermitsDTO.setCancelToDate(viewedDetailsForViewBtnDTO.getCancelToDate());
		activateCancelledPermitsDTO.setCancelledReason(viewedDetailsForViewBtnDTO.getCancelledReason());
		activateCancelledPermitsDTO.setSpecialRemarks(viewedDetailsForViewBtnDTO.getSpecialRemarks());
		activateCancelledPermitsDTO.setCancelledDate(viewedDetailsForViewBtnDTO.getCancelledDate());
		activateCancelledPermitsDTO.setPermitNo(viewedDetailsForViewBtnDTO.getPermitNo());
		activateCancelledPermitsDTO.setApplicationNo(viewedDetailsForViewBtnDTO.getApplicationNo());
		activateCancelledPermitsDTO.setPermitOwner(viewedDetailsForViewBtnDTO.getPermitOwner());
		activateCancelledPermitsDTO.setNicNo(viewedDetailsForViewBtnDTO.getNicNo());
		activateCancelledPermitsDTO.setTelNo(viewedDetailsForViewBtnDTO.getTelNo());
		activateCancelledPermitsDTO.setMobileNo(viewedDetailsForViewBtnDTO.getMobileNo());
		activateCancelledPermitsDTO.setProvinceCode(viewedDetailsForViewBtnDTO.getProvinceCode());
		activateCancelledPermitsDTO.setDistrictCode(viewedDetailsForViewBtnDTO.getDistrictCode());
		activateCancelledPermitsDTO.setDivSectionCode(viewedDetailsForViewBtnDTO.getDivSectionCode());
		activateCancelledPermitsDTO.setAddressOne(viewedDetailsForViewBtnDTO.getAddressOne());
		activateCancelledPermitsDTO.setAddressTwo(viewedDetailsForViewBtnDTO.getAddressTwo());
		activateCancelledPermitsDTO.setCity(viewedDetailsForViewBtnDTO.getCity());
		activateCancelledPermitsDTO.setProvinceDes(viewedDetailsForViewBtnDTO.getProvinceDes());
		activateCancelledPermitsDTO.setTempStatusChecked(viewedDetailsForViewBtnDTO.isTempStatusChecked());
		if (activateCancelledPermitsDTO.isRouteFlagIsChecked() == true) {
			activateCancelledPermitsDTO.setRouteOrigine(viewedDetailsForViewBtnDTO.getRouteServiceDestination());
			activateCancelledPermitsDTO.setRouteServiceDestination(viewedDetailsForViewBtnDTO.getRouteOrigine());
		} else if (activateCancelledPermitsDTO.isRouteFlagIsChecked() == false) {
			activateCancelledPermitsDTO.setRouteOrigine(viewedDetailsForViewBtnDTO.getRouteOrigine());
			activateCancelledPermitsDTO
					.setRouteServiceDestination(viewedDetailsForViewBtnDTO.getRouteServiceDestination());
		}
		if (activateCancelledPermitsDTO.getDistrictCode() != null
				|| activateCancelledPermitsDTO.getDivSectionCode() != null) {
			if (activateCancelledPermitsDTO.getDistrictCode() != null
					|| !activateCancelledPermitsDTO.getDistrictCode().equals("")) {
				viewedDistrictDesForDistrictCodeDTO = cancellationsPermitService.getDistrictDescription(
						activateCancelledPermitsDTO.getProvinceCode(), activateCancelledPermitsDTO.getDistrictCode());
				activateCancelledPermitsDTO.setDistrictDes(viewedDistrictDesForDistrictCodeDTO.getDistrictDes());
			} else {

			}

			if (activateCancelledPermitsDTO.getDivSectionCode() != null
					|| !activateCancelledPermitsDTO.getDivSectionCode().equals("")) {
				viewedDivisionDesForDivisionCodeDTO = cancellationsPermitService.getDivisionSectionDescription(
						activateCancelledPermitsDTO.getDistrictCode(), activateCancelledPermitsDTO.getDivSectionCode());
				activateCancelledPermitsDTO.setDivSectionDes(viewedDivisionDesForDivisionCodeDTO.getDivSectionDes());
			}
		} else {

		}
	}

	public void activeRecords() {

		String modifiedBy = sessionBackingBean.getLoginUser();
		activateCancelledPermitsDTO.setModifiedBy(modifiedBy);
		activateCancelledPermitsDTO.setSpecialRemarks(activateCancelledPermitsDTO.getSpecialRemarks());
		int resultForApplicationTB = cancellationsPermitService
				.updateApplicationTableStatus(activateCancelledPermitsDTO);
		int resultForCancelTB = cancellationsPermitService.updatePermitCancelTable(activateCancelledPermitsDTO);
		if (resultForApplicationTB == 0 && resultForCancelTB == 0) {
			setDisablePrintPermit(false);
			RequestContext.getCurrentInstance().update("frmsuccessSve");
			setSucessMsg("Successfully Activated.");
			RequestContext.getCurrentInstance().execute("PF('successSve').show()");
			setDisablePrintActivationLetterBtn(false);
			RequestContext.getCurrentInstance().update("btnPrint");
			updateTaskTables();
			loadDefaultDataList();
			setDisabledActiveBtn(true);
			RequestContext.getCurrentInstance().update("btnActive");
		} else {

			RequestContext.getCurrentInstance().update("frmerrorMsge");
			setErrorMsg("No saved.");
			RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
		}
	}

	private void updateTaskTables() {
		String loginUser = sessionBackingBean.getLoginUser();
		boolean checkTaskCodePM402IsValid = false;

		boolean checkTaskCodePM403IsValid = permitRenewalsService.checkTaskCodeForCurrentAppNo(
				selectDTO.getApplicationNo(), selectDTO.getRegNoOfTheBus(), activateTaskCode, "C");

		if (checkTaskCodePM403IsValid == false) {

			checkTaskCodePM402IsValid = permitRenewalsService.checkTaskDetails(selectDTO.getApplicationNo(),
					selectDTO.getRegNoOfTheBus(), taskCode, "C");
			if (checkTaskCodePM402IsValid == false) {

			} else if (checkTaskCodePM402IsValid == true) {

				permitRenewalsService.CopyTaskDetailsANDinsertTaskHistory(selectDTO.getApplicationNo(),
						selectDTO.getRegNoOfTheBus(), loginUser, taskCode);
				permitRenewalsService.deleteTaskDetails(selectDTO.getApplicationNo(), selectDTO.getRegNoOfTheBus(),
						taskCode);
			}
			/*
			 * int taskDetPM403Inserted =
			 * cancellationsPermitService.insertPM403RecordTaskDet(selectDTO.
			 * getApplicationNo(), selectDTO.getRegNoOfTheBus(), taskCode,
			 * activateTaskCode, loginUser);
			 */

			int taskDetPM403Inserted = cancellationsPermitService.insertPM403RecordTaskHis(selectDTO.getApplicationNo(),
					selectDTO.getRegNoOfTheBus(), taskCode, activateTaskCode, loginUser);
			if (taskDetPM403Inserted == 0) {

			} else {

			}
		} else if (checkTaskCodePM403IsValid == true) {

			permitRenewalsService.updateTaskDetails(selectDTO.getApplicationNo(), selectDTO.getRegNoOfTheBus(),
					loginUser, activateTaskCode, "C");
			checkTaskCodePM402IsValid = permitRenewalsService.checkTaskDetails(selectDTO.getApplicationNo(),
					selectDTO.getRegNoOfTheBus(), taskCode, "C");
			if (checkTaskCodePM402IsValid == false) {

			} else if (checkTaskCodePM402IsValid == true) {

				permitRenewalsService.CopyTaskDetailsANDinsertTaskHistory(selectDTO.getApplicationNo(),
						selectDTO.getRegNoOfTheBus(), loginUser, taskCode);
				permitRenewalsService.deleteTaskDetails(selectDTO.getApplicationNo(), selectDTO.getRegNoOfTheBus(),
						taskCode);
			}
		}
	}

	public StreamedContent printActivationLetterRecords() throws JRException {

		files = null;
		String sourceFileName = null;
		Connection conn = null;
		String reprintLabel = null;
		boolean checkData = false;
		checkData = cancellationsPermitService.checkDataForReport(appNo);
		try {
			conn = ConnectionManager.getConnection();

			sourceFileName = "..//reports//PermitActivationLetter.jrxml";
			String logopath = "//lk//informatics//ntc//view//reports//";

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_AppNO", appNo);
			parameters.put("P_LOGO", logopath);
			parameters.put("P_REPRINT", reprintLabel);

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", "ActivateLetter.pdf");

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("reportBytes", pdfByteArray);
			sessionMap.put("docType", "pdf");

			if (checkData) {

				cancellationsPermitService.updateLetterPrintStatus(appNo);
			}
			setDisablePrintActivationLetterBtn(true);

			return files;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			ConnectionManager.close(conn);
		}

	}

	public void uploadDocuments() {

		DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();
		try {

			sessionBackingBean.setCancelationPermitNo(selectDTO.getPermitNo());
			sessionBackingBean.setCancelationTranstractionTypeDes("CANCEL");
			sessionBackingBean.setClickedCancelationDocPopup(true);

			uploaddocumentManagementDTO.setUpload_Permit(selectDTO.getPermitNo());
			uploaddocumentManagementDTO.setTransaction_Type("CANCEL");
			mandatoryList = documentManagementService.mandatoryDocs("07", selectDTO.getPermitNo());

			sessionBackingBean.optionalList = documentManagementService.optionalDocs("07", selectDTO.getPermitNo());

			RequestContext.getCurrentInstance().execute("PF('uploadDocument').show()");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void goBack() {
		RequestContext.getCurrentInstance().execute("PF('uploadDocumentsDialog').hide()");
	}

	public void clearCheckDocumentsTable() {
		displayCheckDocumentDataList();
	}

	private void displayCheckDocumentDataList() {
		documentsList = cancellationsPermitService.getAllRecordsForDocumentsCheckings();
	}

	public StreamedContent printPermit() throws JRException {

		files = null;
		String sourceFileName = null;
		Connection conn = null;
		String reprintLabel = null;

		try {
			conn = ConnectionManager.getConnection();

			sourceFileName = "..//reports//NewPermitInformation.jrxml";
			String logopath = "//lk//informatics//ntc//view//reports//";

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_APP", appNo);
			parameters.put("P_LOGO", logopath);
			parameters.put("P_REPRINT", reprintLabel);

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", "New Permit Printing.pdf");

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("reportBytes", pdfByteArray);
			sessionMap.put("docType", "pdf");

			setDisablePrintPermit(true);

			return files;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			ConnectionManager.close(conn);
		}

	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public String getSelectedPermitNo() {
		return selectedPermitNo;
	}

	public void setSelectedPermitNo(String selectedPermitNo) {
		this.selectedPermitNo = selectedPermitNo;
	}

	public String getSelectedVehicleNo() {
		return selectedVehicleNo;
	}

	public void setSelectedVehicleNo(String selectedVehicleNo) {
		this.selectedVehicleNo = selectedVehicleNo;
	}

	public ActivateCancelledPermitsDTO getActivateCancelledPermitsDTO() {
		return activateCancelledPermitsDTO;
	}

	public void setActivateCancelledPermitsDTO(ActivateCancelledPermitsDTO activateCancelledPermitsDTO) {
		this.activateCancelledPermitsDTO = activateCancelledPermitsDTO;
	}

	public ActivateCancelledPermitsDTO getSelectDTO() {
		return selectDTO;
	}

	public void setSelectDTO(ActivateCancelledPermitsDTO selectDTO) {
		this.selectDTO = selectDTO;
	}

	public ActivateCancelledPermitsDTO getSelectedVieweRow() {
		return selectedVieweRow;
	}

	public void setSelectedVieweRow(ActivateCancelledPermitsDTO selectedVieweRow) {
		this.selectedVieweRow = selectedVieweRow;
	}

	public List<ActivateCancelledPermitsDTO> getPermitNoList() {
		return permitNoList;
	}

	public void setPermitNoList(List<ActivateCancelledPermitsDTO> permitNoList) {
		this.permitNoList = permitNoList;
	}

	public List<ActivateCancelledPermitsDTO> getVehicleNoList() {
		return vehicleNoList;
	}

	public void setVehicleNoList(List<ActivateCancelledPermitsDTO> vehicleNoList) {
		this.vehicleNoList = vehicleNoList;
	}

	public List<ActivateCancelledPermitsDTO> getDataList() {
		return dataList;
	}

	public void setDataList(List<ActivateCancelledPermitsDTO> dataList) {
		this.dataList = dataList;
	}

	public ActivateCancelledPermitsDTO getSearchedDTO() {
		return searchedDTO;
	}

	public void setSearchedDTO(ActivateCancelledPermitsDTO searchedDTO) {
		this.searchedDTO = searchedDTO;
	}

	public List<ActivateCancelledPermitsDTO> getSearchedList() {
		return searchedList;
	}

	public void setSearchedList(List<ActivateCancelledPermitsDTO> searchedList) {
		this.searchedList = searchedList;
	}

	public ActivateCancelledPermitsDTO getDefaultViewDTO() {
		return defaultViewDTO;
	}

	public void setDefaultViewDTO(ActivateCancelledPermitsDTO defaultViewDTO) {
		this.defaultViewDTO = defaultViewDTO;
	}

	public boolean isDisabledActiveBtn() {
		return disabledActiveBtn;
	}

	public void setDisabledActiveBtn(boolean disabledActiveBtn) {
		this.disabledActiveBtn = disabledActiveBtn;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getSucessMsg() {
		return sucessMsg;
	}

	public void setSucessMsg(String sucessMsg) {
		this.sucessMsg = sucessMsg;
	}

	public boolean isDisablePrintActivationLetterBtn() {
		return disablePrintActivationLetterBtn;
	}

	public void setDisablePrintActivationLetterBtn(boolean disablePrintActivationLetterBtn) {
		this.disablePrintActivationLetterBtn = disablePrintActivationLetterBtn;
	}

	public List<ActivateCancelledPermitsDTO> getSearchedListForSelectedCancelledDate() {
		return searchedListForSelectedCancelledDate;
	}

	public void setSearchedListForSelectedCancelledDate(
			List<ActivateCancelledPermitsDTO> searchedListForSelectedCancelledDate) {
		this.searchedListForSelectedCancelledDate = searchedListForSelectedCancelledDate;
	}

	public List<ActivateCancelledPermitsDTO> getDocumentsList() {
		return documentsList;
	}

	public void setDocumentsList(List<ActivateCancelledPermitsDTO> documentsList) {
		this.documentsList = documentsList;
	}

	public List<DocumentManagementDTO> getMandatoryList() {
		return mandatoryList;
	}

	public void setMandatoryList(List<DocumentManagementDTO> mandatoryList) {
		this.mandatoryList = mandatoryList;
	}

	public DocumentManagementService getDocumentManagementService() {
		return documentManagementService;
	}

	public void setDocumentManagementService(DocumentManagementService documentManagementService) {
		this.documentManagementService = documentManagementService;
	}

	public boolean isDisableUploadDocumentsBtn() {
		return disableUploadDocumentsBtn;
	}

	public void setDisableUploadDocumentsBtn(boolean disableUploadDocumentsBtn) {
		this.disableUploadDocumentsBtn = disableUploadDocumentsBtn;
	}

	public String getAlertMsg() {
		return alertMsg;
	}

	public void setAlertMsg(String alertMsg) {
		this.alertMsg = alertMsg;
	}

	public ActivateCancelledPermitsDTO getLoadCurrentApplicationNo() {
		return loadCurrentApplicationNo;
	}

	public void setLoadCurrentApplicationNo(ActivateCancelledPermitsDTO loadCurrentApplicationNo) {
		this.loadCurrentApplicationNo = loadCurrentApplicationNo;
	}

	public ActivateCancelledPermitsDTO getOnChangedDetailsDTO() {
		return onChangedDetailsDTO;
	}

	public void setOnChangedDetailsDTO(ActivateCancelledPermitsDTO onChangedDetailsDTO) {
		this.onChangedDetailsDTO = onChangedDetailsDTO;
	}

	public ActivateCancelledPermitsDTO getViewedDetailsForViewBtnDTO() {
		return viewedDetailsForViewBtnDTO;
	}

	public void setViewedDetailsForViewBtnDTO(ActivateCancelledPermitsDTO viewedDetailsForViewBtnDTO) {
		this.viewedDetailsForViewBtnDTO = viewedDetailsForViewBtnDTO;
	}

	public ActivateCancelledPermitsDTO getViewedDistrictDesForDistrictCodeDTO() {
		return viewedDistrictDesForDistrictCodeDTO;
	}

	public void setViewedDistrictDesForDistrictCodeDTO(
			ActivateCancelledPermitsDTO viewedDistrictDesForDistrictCodeDTO) {
		this.viewedDistrictDesForDistrictCodeDTO = viewedDistrictDesForDistrictCodeDTO;
	}

	public ActivateCancelledPermitsDTO getViewedDivisionDesForDivisionCodeDTO() {
		return viewedDivisionDesForDivisionCodeDTO;
	}

	public void setViewedDivisionDesForDivisionCodeDTO(
			ActivateCancelledPermitsDTO viewedDivisionDesForDivisionCodeDTO) {
		this.viewedDivisionDesForDivisionCodeDTO = viewedDivisionDesForDivisionCodeDTO;
	}

	public CancellationsPermitService getCancellationsPermitService() {
		return cancellationsPermitService;
	}

	public void setCancellationsPermitService(CancellationsPermitService cancellationsPermitService) {
		this.cancellationsPermitService = cancellationsPermitService;
	}

	public PermitRenewalsService getPermitRenewalsService() {
		return permitRenewalsService;
	}

	public void setPermitRenewalsService(PermitRenewalsService permitRenewalsService) {
		this.permitRenewalsService = permitRenewalsService;
	}

	public List<ActivateCancelledPermitsDTO> getCurrentvehicleNoList() {
		return currentvehicleNoList;
	}

	public void setCurrentvehicleNoList(List<ActivateCancelledPermitsDTO> currentvehicleNoList) {
		this.currentvehicleNoList = currentvehicleNoList;
	}

	public String getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}

	public String getActivateTaskCode() {
		return activateTaskCode;
	}

	public void setActivateTaskCode(String activateTaskCode) {
		this.activateTaskCode = activateTaskCode;
	}

	public boolean isDisablePrintPermit() {
		return disablePrintPermit;
	}

	public void setDisablePrintPermit(boolean disablePrintPermit) {
		this.disablePrintPermit = disablePrintPermit;
	}

}
