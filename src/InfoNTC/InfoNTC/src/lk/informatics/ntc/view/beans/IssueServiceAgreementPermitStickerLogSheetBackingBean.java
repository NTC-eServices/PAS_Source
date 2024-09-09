package lk.informatics.ntc.view.beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.LogSheetDTO;
import lk.informatics.ntc.model.dto.SisuSeriyaDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.SisuSariyaService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "issueServiceAgreementPermitStickerLogSheetBackingBean")
@ViewScoped

public class IssueServiceAgreementPermitStickerLogSheetBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private List<SisuSeriyaDTO> getRequestNoList = new ArrayList<SisuSeriyaDTO>(0);
	private List<SisuSeriyaDTO> getServiceReferenceNoList = new ArrayList<SisuSeriyaDTO>(0);
	private List<SisuSeriyaDTO> getServiceNoList = new ArrayList<SisuSeriyaDTO>(0);
	private List<SisuSeriyaDTO> getOperatorDepoNameList = new ArrayList<SisuSeriyaDTO>(0);
	private List<SisuSeriyaDTO> getBusNoList = new ArrayList<SisuSeriyaDTO>(0);
	private SisuSeriyaDTO sisuSeriyaDTO, selectDTO, viewDTO;
	private boolean disabledIssueServiceAgreement, disabledIssuePermitSticker, disabledIssueLogSheets;
	private String rejectReason, alertMSG, successMessage, errorMessage, loginUser;
	private SisuSariyaService sisuSariyaService;
	private CommonService commonService;
	private List<SisuSeriyaDTO> sisuSeriyaList;
	private DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	private boolean isSearch, serviceAgreementMode, permitStickerMode, logSheetMode, createMode, viewMode,
			renderLogSheet, renderPermitSticker, renderServiceAgreement;
	private String logSheetYear;
	private int logSheetCopies;
	private LogSheetDTO logSheetDTO;
	private List<LogSheetDTO> logSheetList;
	private List<LogSheetDTO> newLogSheetList;
	private boolean disableSaveLogSheets;

	@PostConstruct
	public void init() {

		sisuSariyaService = (SisuSariyaService) SpringApplicationContex.getBean("sisuSariyaService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");

		sisuSeriyaDTO = new SisuSeriyaDTO();

		loginUser = sessionBackingBean.getLoginUser();

		loadValues();

		createMode = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN105", "C");
		viewMode = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN105", "V");
		serviceAgreementMode = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN105", "IS");
		permitStickerMode = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN105", "IP");
		logSheetMode = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN105", "IL");

		if (createMode == true) {

			renderLogSheet = true;
			renderPermitSticker = true;
			renderServiceAgreement = true;
		} else {
			if (serviceAgreementMode == true) {
				renderServiceAgreement = true;
			}
			if (permitStickerMode == true) {
				renderPermitSticker = true;
			}
			if (logSheetMode == true) {
				renderLogSheet = true;
			}

		}

	}

	private void loadValues() {

		getRequestNoList = sisuSariyaService.getApprovedRequestNo();
		getServiceReferenceNoList = sisuSariyaService.getApprovedServiceRefNoList();
		getServiceNoList = sisuSariyaService.getApprovedServiceNoList();
		getOperatorDepoNameList = sisuSariyaService.getApprovedOperatorDepoNameList();
		getBusNoList = sisuSariyaService.getApprovedBusNoList();
		selectDTO = new SisuSeriyaDTO();
		viewDTO = new SisuSeriyaDTO();
		disabledIssueServiceAgreement = true;
		disabledIssuePermitSticker = true;
		disabledIssueLogSheets = true;
		sisuSeriyaList = new ArrayList<>();
		logSheetList = new ArrayList<>();
		newLogSheetList = new ArrayList<>();
		sisuSeriyaList = sisuSariyaService.getSisuSeriyaToIssue();
		logSheetCopies = 0;
		logSheetYear = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
		disableSaveLogSheets = true;

	}

	public void search() {

		if ((sisuSeriyaDTO.getServiceRefNo() == null || sisuSeriyaDTO.getServiceRefNo().trim().equalsIgnoreCase(""))
				&& (sisuSeriyaDTO.getRequestNo() == null || sisuSeriyaDTO.getRequestNo().trim().equalsIgnoreCase(""))
				&& (sisuSeriyaDTO.getServiceNo() == null || sisuSeriyaDTO.getServiceNo().trim().equalsIgnoreCase(""))
				&& (sisuSeriyaDTO.getRequestStartDate() == null) && (sisuSeriyaDTO.getRequestEndDate() == null)
				&& (sisuSeriyaDTO.getNameOfOperator() == null
						|| sisuSeriyaDTO.getNameOfOperator().trim().equalsIgnoreCase(""))
				&& (sisuSeriyaDTO.getBusRegNo() == null || sisuSeriyaDTO.getBusRegNo().trim().equalsIgnoreCase(""))) {

			setErrorMessage("Please select at least one field.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		} else {
			// need to change request date data

			sisuSeriyaList = new ArrayList<SisuSeriyaDTO>();
			sisuSeriyaList = sisuSariyaService.getSisuSeriyaToIssueBySearch(sisuSeriyaDTO);

			if (sisuSeriyaList.isEmpty()) {
				sisuSeriyaList = new ArrayList<SisuSeriyaDTO>();
				setErrorMessage("No data found.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			} else {
				isSearch = true;
			}
		}

	}

	public void selectRow() {

		if (selectDTO.getServiceNo() != null || !sisuSeriyaDTO.getServiceNo().trim().equalsIgnoreCase("")) {

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

		sisuSeriyaDTO = new SisuSeriyaDTO();
		disabledIssuePermitSticker = true;
		disabledIssueServiceAgreement = true;
		disabledIssueLogSheets = true;
		sisuSeriyaList = new ArrayList<>();
		sisuSeriyaList = sisuSariyaService.getSisuSeriyaToIssue();
	}

	public void clearTwo() {

		disabledIssuePermitSticker = true;
		disabledIssueServiceAgreement = true;
		disabledIssueLogSheets = true;
		selectDTO = new SisuSeriyaDTO();
		sisuSeriyaList = new ArrayList<>();
		sisuSeriyaList = sisuSariyaService.getSisuSeriyaToIssue();

	}

	public void issueServiceAgreement() {

		selectDTO.setCurrentUser(loginUser);

		if (selectDTO.getServiceAgreementIssuedDate() != null) {
			selectDTO.setIssueType(1);

			sisuSariyaService.updateIssueServiceAgreementPermitStickerLogSheets(selectDTO);
			try {
				sisuSariyaService.updateTaskCompleteSubsidyTaskTableForPrintAgreement(selectDTO.getRequestNo(),
						selectDTO.getServiceNo(), selectDTO.getServiceRefNo(), "SM005", "C",
						sessionBackingBean.getLoginUser());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			setSuccessMessage("Service Agreement Issued");
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

		} else {
			setErrorMessage("Invalid Service Agreement Issue Date");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
		sisuSeriyaList = new ArrayList<>();
		sisuSeriyaList = sisuSariyaService.getSisuSeriyaToIssue();

	}

	public void issuePermitSticker() {

		selectDTO.setCurrentUser(loginUser);
		if (selectDTO.getPermitStickerIssuedDate() != null) {
			selectDTO.setIssueType(2);
			sisuSariyaService.updateIssueServiceAgreementPermitStickerLogSheets(selectDTO);

			try {

				sisuSariyaService.updateTaskCompleteSubsidyTaskTableForPrintAgreement(selectDTO.getRequestNo(),
						selectDTO.getServiceNo(), selectDTO.getServiceRefNo(), "SM006", "C",
						sessionBackingBean.getLoginUser());

			} catch (Exception e) {
				e.printStackTrace();
			} finally {

			}

			setSuccessMessage("Permit Sticker Issued");
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

		} else {
			setErrorMessage("Invalid Permit Sticker Issue Date");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
		sisuSeriyaList = new ArrayList<>();
		sisuSeriyaList = sisuSariyaService.getSisuSeriyaToIssue();

	}

	public void issueLogSheets() {

		if (selectDTO.getLogSheetsIssuedDate() != null) {

			logSheetCopies = 0;
			logSheetYear = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));

			selectDTO.setServiceTypeCode("S01");

			RequestContext.getCurrentInstance().update("dialogIssueLogSheets");
			RequestContext.getCurrentInstance().execute("PF('issueLogSheetsDialog').show()");

		} else {
			setErrorMessage("Invalid Log Sheets Issue Date");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
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

						/* set id value : dhananjika.d (26/06/2024) */
						for (int i = 0; i <= logSheetCopies; i++) {
							LogSheetDTO dto = new LogSheetDTO();
							dto.setId(i+1);
							newLogSheetList.add(dto);
						}
						disableSaveLogSheets =false;
					} else {
						setErrorMessage("Invalid No. of Copies");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}
				} else {
					setErrorMessage("Invalid Year");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			} else {
				setErrorMessage("Please select the Service Reference No.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {
			setErrorMessage("Please select the Service Type");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void clearLogSheet() {

		logSheetCopies = 0;
		logSheetYear = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
		newLogSheetList = new ArrayList<LogSheetDTO>();
		disableSaveLogSheets = true;

	}

	public void saveLogSheets() {

		if (selectDTO.getServiceTypeCode() != null && !selectDTO.getServiceTypeCode().isEmpty()
				&& !selectDTO.getServiceTypeCode().equalsIgnoreCase("")) {
			if (selectDTO.getServiceRefNo() != null && !selectDTO.getServiceRefNo().isEmpty()
					&& !selectDTO.getServiceRefNo().equalsIgnoreCase("")) {
				if (logSheetYear != null && !logSheetYear.isEmpty() && !logSheetYear.equalsIgnoreCase("")) {
					if (logSheetCopies > 0) {
						if (checkLogSheetNo()) {
							return;
						} else {
							boolean success = sisuSariyaService.generateLogSheetRef(selectDTO, logSheetYear, logSheetCopies, loginUser,
									newLogSheetList);

							selectDTO.setCurrentUser(loginUser);
							selectDTO.setIssueType(3);
							sisuSariyaService.updateIssueServiceAgreementPermitStickerLogSheets(selectDTO);

							try {

								sisuSariyaService.updateTaskCompleteSubsidyTaskTableForPrintAgreement(
										selectDTO.getRequestNo(), selectDTO.getServiceNo(), selectDTO.getServiceRefNo(),
										"SM007", "C", sessionBackingBean.getLoginUser());

							} catch (Exception e) {
								e.printStackTrace();
							} finally {

							}

							logSheetCopies = 0;
							logSheetYear = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));

							if(success) {
								newLogSheetList = new ArrayList<LogSheetDTO>();
								setSuccessMessage("Log Sheets Issued");
								RequestContext.getCurrentInstance().execute("PF('issueLogSheetsDialog').hide()");
								RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
							}else {
								setErrorMessage("Failed to save.");
								RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
							}
							
						}
					} else {
						setErrorMessage("Invalid No. of Copies");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}
				} else {
					setErrorMessage("Invalid Year");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			} else {
				setErrorMessage("Please select the Service Reference No.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {
			setErrorMessage("Please select the Service Type");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void yearValidator() {

		if (logSheetYear != null && !logSheetYear.isEmpty()) {
			Pattern ptr = Pattern.compile("^[0-9]{4}$");
			boolean valid = ptr.matcher(logSheetYear).matches();
			if (valid) {
				DateFormat dateFormat = new SimpleDateFormat("yyyy");
				Date date = new Date();
				String year = dateFormat.format(date);
				int curYear = Integer.parseInt(year);

				int manuYear = Integer.parseInt(logSheetYear);

			} else {
				setErrorMessage("Invalid Year");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				logSheetYear = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
			}

		}

	}

	public boolean checkLogSheetNo() {
		boolean exists = false;
		boolean finalResult = false;
		List<String> refNoList = new ArrayList<>();
		StringBuilder builder = new StringBuilder();
		for (LogSheetDTO logSheetDTO : newLogSheetList) {
			if (logSheetDTO.getLogSheetRefNo() != null && logSheetDTO.getLogSheetRefNo() != "") {
				exists = commonService.existingLogSheetReferenceNo(logSheetDTO.getLogSheetRefNo(),selectDTO.getServiceTypeCode());
				if (exists || refNoList.contains(logSheetDTO.getLogSheetRefNo())) {
					builder.append(logSheetDTO.getLogSheetRefNo() + ", ");
					String concatenatedString = builder.toString();
					setErrorMessage("Duplicate Log Sheet Reference No. - "
							+ concatenatedString.substring(0, concatenatedString.length() - 2));
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					finalResult = true;
				}
				refNoList.add(logSheetDTO.getLogSheetRefNo());
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

	public List<SisuSeriyaDTO> getGetRequestNoList() {
		return getRequestNoList;
	}

	public void setGetRequestNoList(List<SisuSeriyaDTO> getRequestNoList) {
		this.getRequestNoList = getRequestNoList;
	}

	public List<SisuSeriyaDTO> getGetServiceReferenceNoList() {
		return getServiceReferenceNoList;
	}

	public void setGetServiceReferenceNoList(List<SisuSeriyaDTO> getServiceReferenceNoList) {
		this.getServiceReferenceNoList = getServiceReferenceNoList;
	}

	public List<SisuSeriyaDTO> getGetServiceNoList() {
		return getServiceNoList;
	}

	public void setGetServiceNoList(List<SisuSeriyaDTO> getServiceNoList) {
		this.getServiceNoList = getServiceNoList;
	}

	public SisuSeriyaDTO getSisuSeriyaDTO() {
		return sisuSeriyaDTO;
	}

	public void setSisuSeriyaDTO(SisuSeriyaDTO sisuSeriyaDTO) {
		this.sisuSeriyaDTO = sisuSeriyaDTO;
	}

	public SisuSeriyaDTO getSelectDTO() {
		return selectDTO;
	}

	public void setSelectDTO(SisuSeriyaDTO selectDTO) {
		this.selectDTO = selectDTO;
	}

	public SisuSeriyaDTO getViewDTO() {
		return viewDTO;
	}

	public void setViewDTO(SisuSeriyaDTO viewDTO) {
		this.viewDTO = viewDTO;
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

	public List<SisuSeriyaDTO> getSisuSeriyaList() {
		return sisuSeriyaList;
	}

	public void setSisuSeriyaList(List<SisuSeriyaDTO> sisuSeriyaList) {
		this.sisuSeriyaList = sisuSeriyaList;
	}

	public DateFormat getDf() {
		return df;
	}

	public void setDf(DateFormat df) {
		this.df = df;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	public String getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(String loginUser) {
		this.loginUser = loginUser;
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

	public int getLogSheetCopies() {
		return logSheetCopies;
	}

	public void setLogSheetCopies(int logSheetCopies) {
		this.logSheetCopies = logSheetCopies;
	}

	public List<LogSheetDTO> getNewLogSheetList() {
		return newLogSheetList;
	}

	public void setNewLogSheetList(List<LogSheetDTO> newLogSheetList) {
		this.newLogSheetList = newLogSheetList;
	}

	public boolean isServiceAgreementMode() {
		return serviceAgreementMode;
	}

	public void setServiceAgreementMode(boolean serviceAgreementMode) {
		this.serviceAgreementMode = serviceAgreementMode;
	}

	public boolean isPermitStickerMode() {
		return permitStickerMode;
	}

	public void setPermitStickerMode(boolean permitStickerMode) {
		this.permitStickerMode = permitStickerMode;
	}

	public boolean isLogSheetMode() {
		return logSheetMode;
	}

	public void setLogSheetMode(boolean logSheetMode) {
		this.logSheetMode = logSheetMode;
	}

	public boolean isCreateMode() {
		return createMode;
	}

	public void setCreateMode(boolean createMode) {
		this.createMode = createMode;
	}

	public boolean isViewMode() {
		return viewMode;
	}

	public void setViewMode(boolean viewMode) {
		this.viewMode = viewMode;
	}

	public boolean isRenderLogSheet() {
		return renderLogSheet;
	}

	public void setRenderLogSheet(boolean renderLogSheet) {
		this.renderLogSheet = renderLogSheet;
	}

	public boolean isRenderPermitSticker() {
		return renderPermitSticker;
	}

	public void setRenderPermitSticker(boolean renderPermitSticker) {
		this.renderPermitSticker = renderPermitSticker;
	}

	public boolean isRenderServiceAgreement() {
		return renderServiceAgreement;
	}

	public void setRenderServiceAgreement(boolean renderServiceAgreement) {
		this.renderServiceAgreement = renderServiceAgreement;
	}

	public List<SisuSeriyaDTO> getGetOperatorDepoNameList() {
		return getOperatorDepoNameList;
	}

	public void setGetOperatorDepoNameList(List<SisuSeriyaDTO> getOperatorDepoNameList) {
		this.getOperatorDepoNameList = getOperatorDepoNameList;
	}

	public List<SisuSeriyaDTO> getGetBusNoList() {
		return getBusNoList;
	}

	public void setGetBusNoList(List<SisuSeriyaDTO> getBusNoList) {
		this.getBusNoList = getBusNoList;
	}

	public boolean isDisableSaveLogSheets() {
		return disableSaveLogSheets;
	}

	public void setDisableSaveLogSheets(boolean disableSaveLogSheets) {
		this.disableSaveLogSheets = disableSaveLogSheets;
	}
	
	

}