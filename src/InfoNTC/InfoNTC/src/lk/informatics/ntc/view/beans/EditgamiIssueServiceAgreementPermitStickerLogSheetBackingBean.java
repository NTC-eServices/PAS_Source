package lk.informatics.ntc.view.beans;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.GamiSeriyaDTO;
import lk.informatics.ntc.model.dto.LogSheetDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.SisuSariyaService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "editgamiIssueServiceAgreementPermitStickerLogSheetBackingBean")
@ViewScoped
public class EditgamiIssueServiceAgreementPermitStickerLogSheetBackingBean {
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private List<GamiSeriyaDTO> getTenderRefNoList = new ArrayList<GamiSeriyaDTO>(0);
	private List<GamiSeriyaDTO> getServiceReferenceNoList = new ArrayList<GamiSeriyaDTO>(0);
	private List<GamiSeriyaDTO> getServiceNoList = new ArrayList<GamiSeriyaDTO>(0);
	private GamiSeriyaDTO gamiSeriyaDTO, selectDTO, viewDTO;
	private LogSheetDTO selectedEditRow;
	private boolean disabledIssueServiceAgreement, disabledIssuePermitSticker, disabledIssueLogSheets;
	private String rejectReason, alertMSG, successMessage, errorMessage, loginUser;
	private SisuSariyaService sisuSariyaService;
	private CommonService commonService;
	private List<GamiSeriyaDTO> gamiSeriyaList;
	private DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	private boolean isSearch;
	private String logSheetYear;
	private int logSheetCopies;
	private LogSheetDTO logSheetDTO;
	private List<LogSheetDTO> logSheetList;
	private List<LogSheetDTO> newLogSheetList;

	private GamiSeriyaDTO loadValuesForSelectedReqNo;

	@PostConstruct
	public void init() {

		sisuSariyaService = (SisuSariyaService) SpringApplicationContex.getBean("sisuSariyaService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");

		gamiSeriyaDTO = new GamiSeriyaDTO();

		loginUser = sessionBackingBean.getLoginUser();

		loadValues();
	}

	private void loadValues() {
		getTenderRefNoList = sisuSariyaService.getApprovedTenderNoGamiSeriyaForEditMode();
		getServiceReferenceNoList = sisuSariyaService.getApprovedServiceRefNoListGamiSeriyaForEditMode();
		getServiceNoList = sisuSariyaService.getApprovedServiceNoListGamiSeriyaForEditMode();
		selectDTO = new GamiSeriyaDTO();
		viewDTO = new GamiSeriyaDTO();
		disabledIssueServiceAgreement = true;
		disabledIssuePermitSticker = true;
		disabledIssueLogSheets = true;
		gamiSeriyaList = new ArrayList<>();
		logSheetList = new ArrayList<>();
		newLogSheetList = new ArrayList<>();
		gamiSeriyaList = sisuSariyaService.getGamiSeriyaToIssueForEditMode();
		logSheetCopies = 0;
		logSheetYear = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
		loadValuesForSelectedReqNo = new GamiSeriyaDTO();
		selectedEditRow = new LogSheetDTO();
	}

	public void onRequestNoChange() {

	}

	public void search() {

		if ((gamiSeriyaDTO.getServiceRefNo() == null || gamiSeriyaDTO.getServiceRefNo().trim().equalsIgnoreCase(""))
				&& (gamiSeriyaDTO.getTenderRefNo() == null
						|| gamiSeriyaDTO.getTenderRefNo().trim().equalsIgnoreCase(""))
				&& (gamiSeriyaDTO.getServiceNo() == null || gamiSeriyaDTO.getServiceNo().trim().equalsIgnoreCase(""))
				&& (gamiSeriyaDTO.getRequestStartDate() == null) && (gamiSeriyaDTO.getRequestEndDate() == null)) {

			setErrorMessage("Please Select Atleast One Field");
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		} else {
			// need to change request date data

			gamiSeriyaList = new ArrayList<GamiSeriyaDTO>();
			gamiSeriyaList = sisuSariyaService.getGamiSeriyaToIssueBySearchForEditMode(gamiSeriyaDTO);

			if (gamiSeriyaList.isEmpty()) {
				gamiSeriyaList = new ArrayList<GamiSeriyaDTO>();
				setErrorMessage("No Data Found.");
				RequestContext.getCurrentInstance().update("frmError");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			} else {

				isSearch = true;
			}
		}

	}

	public void selectRow() {

		if (selectDTO.getServiceNo() != null || !gamiSeriyaDTO.getServiceNo().trim().equalsIgnoreCase("")) {

			if (selectDTO.isIssuedServiceAgreement()) {
				disabledIssueServiceAgreement = true;
			} else {
				disabledIssueServiceAgreement = false;
			}

			if (selectDTO.isIssuedPermitSticker()) {
				disabledIssuePermitSticker = true;
			} else {
				disabledIssuePermitSticker = false;
			}

			if (selectDTO.isIssuedLogSheets()) {
				disabledIssueLogSheets = true;
			} else {
				disabledIssueLogSheets = false;
			}

		} else {

			disabledIssueServiceAgreement = true;
			disabledIssuePermitSticker = true;
			disabledIssueLogSheets = true;

		}
	}

	public void clearOne() {

		gamiSeriyaDTO = new GamiSeriyaDTO();
		disabledIssuePermitSticker = true;
		disabledIssueServiceAgreement = true;
		disabledIssueLogSheets = true;
		gamiSeriyaList = new ArrayList<>();
		gamiSeriyaList = sisuSariyaService.getGamiSeriyaToIssueForEditMode();
	}

	public void clearTwo() {

		disabledIssuePermitSticker = true;
		disabledIssueServiceAgreement = true;
		disabledIssueLogSheets = true;
		selectDTO = new GamiSeriyaDTO();

	}

	public void issueServiceAgreement() {

		selectDTO.setCurrentUser(loginUser);

		if (selectDTO.getServiceAgreementIssuedDate() != null) {

			sisuSariyaService.insetOldRecordIntoGamiSeriyaInfoHis(selectDTO);
			selectDTO.setIssueType(1);
			sisuSariyaService.updateIssueServiceAgreementPermitStickerLogSheetsForGamiSeriyaEditMode(selectDTO);

			setSuccessMessage("Service Agreement Issued");
			RequestContext.getCurrentInstance().update("frmsuccess");
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

		} else {
			setErrorMessage("Invalid Service Agreement Issue Date");
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
		gamiSeriyaList = new ArrayList<>();
		gamiSeriyaList = sisuSariyaService.getGamiSeriyaToIssueForEditMode();

	}

	public void issuePermitSticker() {

		selectDTO.setCurrentUser(loginUser);
		if (selectDTO.getPermitStickerIssuedDate() != null) {

			sisuSariyaService.insetOldRecordIntoGamiSeriyaInfoHis(selectDTO);
			selectDTO.setIssueType(2);
			sisuSariyaService.updateIssueServiceAgreementPermitStickerLogSheetsForGamiSeriyaEditMode(selectDTO);

			setSuccessMessage("Permit Sticker Issued");
			RequestContext.getCurrentInstance().update("frmsuccess");
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

		} else {
			setErrorMessage("Invalid Permit Sticker Issue Date");
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
		gamiSeriyaList = new ArrayList<>();
		gamiSeriyaList = sisuSariyaService.getGamiSeriyaToIssueForEditMode();

	}

	public void issueLogSheets() {

		if (selectDTO.getLogSheetsIssuedDate() != null) {

			logSheetCopies = 0;
			logSheetYear = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));

			selectDTO.setServiceTypeCode("S02");

			RequestContext.getCurrentInstance().update("dialogIssueLogSheets");
			RequestContext.getCurrentInstance().execute("PF('issueLogSheetsDialog').show()");

		} else {
			setErrorMessage("Invalid Log Sheets Issue Date");
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void viewLogSheetRef() {
		if (selectDTO.getServiceTypeCode() != null && !selectDTO.getServiceTypeCode().isEmpty()
				&& !selectDTO.getServiceTypeCode().equalsIgnoreCase("")) {
			if (selectDTO.getServiceRefNo() != null && !selectDTO.getServiceRefNo().isEmpty()
					&& !selectDTO.getServiceRefNo().equalsIgnoreCase("")) {
				if (logSheetYear != null && !logSheetYear.isEmpty() && !logSheetYear.equalsIgnoreCase("")) {
					newLogSheetList = new ArrayList<LogSheetDTO>();
					// Modified By :Dinushi.R on 19-01-2021
					// Purpose: Fixing Zoho Issue No. A4T6-I177
					// newLogSheetList=
					// sisuSariyaService.getLogSheetsByServiceRefNo(selectDTO.getServiceRefNo());
					newLogSheetList = sisuSariyaService.getLogSheetsByServiceRefNoYear(selectDTO.getServiceRefNo(),
							logSheetYear);
					// End Modification

				} else {
					setErrorMessage("Invalid Year");
					RequestContext.getCurrentInstance().update("frmError");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			} else {
				setErrorMessage("Please select the Service Reference No.");
				RequestContext.getCurrentInstance().update("frmError");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {
			setErrorMessage("Please select the Service Type");
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void editAction() {
		if(!checkLogSheetNo(selectedEditRow.getLogSheetRefNo())) {
			sisuSariyaService.updateLogSheetValues(selectedEditRow.getLogSheetSeqNo(), selectedEditRow.getLogSheetRefNo(),
					sessionBackingBean.getLoginUser());
			setSuccessMessage("Log Sheets Updated");
			RequestContext.getCurrentInstance().update("frmsuccess");
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
		}
	}

	public void generateLogSheetRef() {

		if (selectDTO.getServiceTypeCode() != null && !selectDTO.getServiceTypeCode().isEmpty()
				&& !selectDTO.getServiceTypeCode().equalsIgnoreCase("")) {
			if (selectDTO.getServiceRefNo() != null && !selectDTO.getServiceRefNo().isEmpty()
					&& !selectDTO.getServiceRefNo().equalsIgnoreCase("")) {
				if (logSheetYear != null && !logSheetYear.isEmpty() && !logSheetYear.equalsIgnoreCase("")) {
					if (logSheetCopies > 0) {

						newLogSheetList = new ArrayList<LogSheetDTO>();

						for (int i = 0; i <= logSheetCopies; i++) {
							LogSheetDTO dto = new LogSheetDTO();
							newLogSheetList.add(dto);
						}

					} else {
						setErrorMessage("Invalid No. of Copies");
						RequestContext.getCurrentInstance().update("frmError");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}
				} else {
					setErrorMessage("Invalid Year");
					RequestContext.getCurrentInstance().update("frmError");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			} else {
				setErrorMessage("Please select the Service Reference No.");
				RequestContext.getCurrentInstance().update("frmError");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {
			setErrorMessage("Please select the Service Type");
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void clearLogSheet() {

		logSheetYear = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
		newLogSheetList = new ArrayList<LogSheetDTO>();

	}

	public void saveLogSheets() {
		if (selectDTO.getServiceTypeCode() != null && !selectDTO.getServiceTypeCode().isEmpty()
				&& !selectDTO.getServiceTypeCode().equalsIgnoreCase("")) {
			if (selectDTO.getServiceRefNo() != null && !selectDTO.getServiceRefNo().isEmpty()
					&& !selectDTO.getServiceRefNo().equalsIgnoreCase("")) {
				if (logSheetYear != null && !logSheetYear.isEmpty() && !logSheetYear.equalsIgnoreCase("")) {

					if(!checkLogSheetNo()) {
						sisuSariyaService.insetOldRecordIntoGamiSeriyaInfoHis(selectDTO);
						selectDTO.setCurrentUser(loginUser);
						selectDTO.setIssueType(3);
						sisuSariyaService.updateIssueServiceAgreementPermitStickerLogSheetsForGamiSeriyaEditMode(selectDTO);

						setSuccessMessage("Log Sheets Issued");
						RequestContext.getCurrentInstance().execute("PF('issueLogSheetsDialog').hide()");
						RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
					}

				} else {
					setErrorMessage("Invalid Year");
					RequestContext.getCurrentInstance().update("frmError");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			} else {
				setErrorMessage("Please select the Service Reference No.");
				RequestContext.getCurrentInstance().update("frmError");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {
			setErrorMessage("Please select the Service Type");
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public boolean checkLogSheetNo() {
		boolean exists = false;
		boolean finalResult = false;

		for (LogSheetDTO logSheetDTO : newLogSheetList) {
			if (logSheetDTO.getLogSheetRefNo() != null && logSheetDTO.getLogSheetRefNo() != "") {
				exists = commonService.existingLogSheetReferenceNo(logSheetDTO.getLogSheetRefNo(), selectDTO.getServiceTypeCode());
				if (exists) {
					setErrorMessage("Duplicate Log Sheet Reference No. - " + logSheetDTO.getLogSheetRefNo());
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					finalResult = true;
				}
			}
		}
		
		return finalResult;
	}
	
	/* new method for edit reference number: dhananjika.d (26/06/2024) */
	public boolean checkLogSheetNo(String logRefNum) {
		boolean exists = false;
		boolean finalResult = false;

		if (logRefNum != null && logRefNum != "") {
			exists = commonService.existingLogSheetReferenceNo(logRefNum, selectDTO.getServiceTypeCode());
			if (exists) {
				setErrorMessage("Duplicate Log Sheet Reference No. - " + logRefNum);
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				finalResult = true;
			}
		}
		return finalResult;
	}
	
	/* New method to remove spaces : dhananjika.d (26/06/2024) */
	public void logSheetRefNoValidate(LogSheetDTO logSheetDTO) {
		if (logSheetDTO != null && logSheetDTO.getLogSheetRefNo() != null) {
			String refNoWithoutSpaces = logSheetDTO.getLogSheetRefNo().replaceAll("\\s", "");
	        logSheetDTO.setLogSheetRefNo(refNoWithoutSpaces);
		}
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public List<GamiSeriyaDTO> getGetTenderRefNoList() {
		return getTenderRefNoList;
	}

	public void setGetTenderRefNoList(List<GamiSeriyaDTO> getTenderRefNoList) {
		this.getTenderRefNoList = getTenderRefNoList;
	}

	public List<GamiSeriyaDTO> getGetServiceReferenceNoList() {
		return getServiceReferenceNoList;
	}

	public void setGetServiceReferenceNoList(List<GamiSeriyaDTO> getServiceReferenceNoList) {
		this.getServiceReferenceNoList = getServiceReferenceNoList;
	}

	public List<GamiSeriyaDTO> getGetServiceNoList() {
		return getServiceNoList;
	}

	public void setGetServiceNoList(List<GamiSeriyaDTO> getServiceNoList) {
		this.getServiceNoList = getServiceNoList;
	}

	public GamiSeriyaDTO getGamiSeriyaDTO() {
		return gamiSeriyaDTO;
	}

	public void setGamiSeriyaDTO(GamiSeriyaDTO gamiSeriyaDTO) {
		this.gamiSeriyaDTO = gamiSeriyaDTO;
	}

	public GamiSeriyaDTO getSelectDTO() {
		return selectDTO;
	}

	public void setSelectDTO(GamiSeriyaDTO selectDTO) {
		this.selectDTO = selectDTO;
	}

	public GamiSeriyaDTO getViewDTO() {
		return viewDTO;
	}

	public void setViewDTO(GamiSeriyaDTO viewDTO) {
		this.viewDTO = viewDTO;
	}

	public LogSheetDTO getSelectedEditRow() {
		return selectedEditRow;
	}

	public void setSelectedEditRow(LogSheetDTO selectedEditRow) {
		this.selectedEditRow = selectedEditRow;
	}

	public boolean isDisabledIssueServiceAgreement() {
		return disabledIssueServiceAgreement;
	}

	public void setDisabledIssueServiceAgreement(boolean disabledIssueServiceAgreement) {
		this.disabledIssueServiceAgreement = disabledIssueServiceAgreement;
	}

	public boolean isDisabledIssuePermitSticker() {
		return disabledIssuePermitSticker;
	}

	public void setDisabledIssuePermitSticker(boolean disabledIssuePermitSticker) {
		this.disabledIssuePermitSticker = disabledIssuePermitSticker;
	}

	public boolean isDisabledIssueLogSheets() {
		return disabledIssueLogSheets;
	}

	public void setDisabledIssueLogSheets(boolean disabledIssueLogSheets) {
		this.disabledIssueLogSheets = disabledIssueLogSheets;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public String getAlertMSG() {
		return alertMSG;
	}

	public void setAlertMSG(String alertMSG) {
		this.alertMSG = alertMSG;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(String loginUser) {
		this.loginUser = loginUser;
	}

	public SisuSariyaService getSisuSariyaService() {
		return sisuSariyaService;
	}

	public void setSisuSariyaService(SisuSariyaService sisuSariyaService) {
		this.sisuSariyaService = sisuSariyaService;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public List<GamiSeriyaDTO> getGamiSeriyaList() {
		return gamiSeriyaList;
	}

	public void setGamiSeriyaList(List<GamiSeriyaDTO> gamiSeriyaList) {
		this.gamiSeriyaList = gamiSeriyaList;
	}

	public DateFormat getDf() {
		return df;
	}

	public void setDf(DateFormat df) {
		this.df = df;
	}

	public boolean isSearch() {
		return isSearch;
	}

	public void setSearch(boolean isSearch) {
		this.isSearch = isSearch;
	}

	public String getLogSheetYear() {
		return logSheetYear;
	}

	public void setLogSheetYear(String logSheetYear) {
		this.logSheetYear = logSheetYear;
	}

	public int getLogSheetCopies() {
		return logSheetCopies;
	}

	public void setLogSheetCopies(int logSheetCopies) {
		this.logSheetCopies = logSheetCopies;
	}

	public LogSheetDTO getLogSheetDTO() {
		return logSheetDTO;
	}

	public void setLogSheetDTO(LogSheetDTO logSheetDTO) {
		this.logSheetDTO = logSheetDTO;
	}

	public List<LogSheetDTO> getLogSheetList() {
		return logSheetList;
	}

	public void setLogSheetList(List<LogSheetDTO> logSheetList) {
		this.logSheetList = logSheetList;
	}

	public List<LogSheetDTO> getNewLogSheetList() {
		return newLogSheetList;
	}

	public void setNewLogSheetList(List<LogSheetDTO> newLogSheetList) {
		this.newLogSheetList = newLogSheetList;
	}

	public GamiSeriyaDTO getLoadValuesForSelectedReqNo() {
		return loadValuesForSelectedReqNo;
	}

	public void setLoadValuesForSelectedReqNo(GamiSeriyaDTO loadValuesForSelectedReqNo) {
		this.loadValuesForSelectedReqNo = loadValuesForSelectedReqNo;
	}

}
