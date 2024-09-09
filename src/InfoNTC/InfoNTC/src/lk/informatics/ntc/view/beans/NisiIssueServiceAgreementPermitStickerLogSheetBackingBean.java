package lk.informatics.ntc.view.beans;

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
import lk.informatics.ntc.model.dto.NisiSeriyaDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.NisiSeriyaService;
import lk.informatics.ntc.model.service.SisuSariyaService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "nisiIssueServiceAgreementPermitStickerLogSheetBackingBean")
@ViewScoped
public class NisiIssueServiceAgreementPermitStickerLogSheetBackingBean {
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private List<NisiSeriyaDTO> getRequestNoList = new ArrayList<NisiSeriyaDTO>(0);
	private List<NisiSeriyaDTO> getServiceReferenceNoList = new ArrayList<NisiSeriyaDTO>(0);
	private List<NisiSeriyaDTO> getServiceNoList = new ArrayList<NisiSeriyaDTO>(0);
	private NisiSeriyaDTO nisiSeriyaDTO, selectDTO, viewDTO;
	private boolean disabledIssueServiceAgreement, disabledIssuePermitSticker, disabledIssueLogSheets;
	private String rejectReason, alertMSG, successMessage, errorMessage, loginUser;
	private SisuSariyaService sisuSariyaService;
	private CommonService commonService;
	private List<NisiSeriyaDTO> nisiSeriyaList;
	private DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	private boolean isSearch, serviceAgreementMode, permitStickerMode, logSheetMode, createMode, viewMode,
			renderLogSheet, renderPermitSticker, renderServiceAgreement;
	private String logSheetYear;
	private int logSheetCopies;
	private LogSheetDTO logSheetDTO;
	private List<LogSheetDTO> logSheetList;
	private List<LogSheetDTO> newLogSheetList;
	private NisiSeriyaService nisiSeriyaService;

	@PostConstruct
	public void init() {
		nisiSeriyaService = (NisiSeriyaService) SpringApplicationContex.getBean("nisiSeriyaService");
		sisuSariyaService = (SisuSariyaService) SpringApplicationContex.getBean("sisuSariyaService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");

		nisiSeriyaDTO = new NisiSeriyaDTO();

		loginUser = sessionBackingBean.getLoginUser();

		loadValues();

		renderServiceAgreement = true;
		renderPermitSticker = true;
		renderLogSheet = true;
	}

	private void loadValues() {
		getRequestNoList = sisuSariyaService.getApprovedRequestNoForNisiSeriya();
		getServiceReferenceNoList = sisuSariyaService.getApprovedServiceRefNoListForNisiSeriya();
		getServiceNoList = sisuSariyaService.getApprovedServiceNoListForNisiSeriya();
		selectDTO = new NisiSeriyaDTO();
		viewDTO = new NisiSeriyaDTO();
		disabledIssueServiceAgreement = true;
		disabledIssuePermitSticker = true;
		disabledIssueLogSheets = true;
		nisiSeriyaList = new ArrayList<>();
		logSheetList = new ArrayList<>();
		newLogSheetList = new ArrayList<>();
		nisiSeriyaList = sisuSariyaService.getNisiSeriyaToIssue();
		logSheetCopies = 0;
		logSheetYear = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
	}

	public void search() {
		if ((nisiSeriyaDTO.getServiceRefNo() == null || nisiSeriyaDTO.getServiceRefNo().trim().equalsIgnoreCase(""))
				&& (nisiSeriyaDTO.getRequestNo() == null || nisiSeriyaDTO.getRequestNo().trim().equalsIgnoreCase(""))
				&& (nisiSeriyaDTO.getServiceNo() == null || nisiSeriyaDTO.getServiceNo().trim().equalsIgnoreCase(""))
				&& (nisiSeriyaDTO.getRequestStartDate() == null) && (nisiSeriyaDTO.getRequestEndDate() == null)) {

			setErrorMessage("Please Select Atleast One Field");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		} else {

			nisiSeriyaList = new ArrayList<NisiSeriyaDTO>();
			nisiSeriyaList = sisuSariyaService.getNisiSeriyaToIssueBySearch(nisiSeriyaDTO);

			if (nisiSeriyaList.isEmpty()) {
				nisiSeriyaList = new ArrayList<NisiSeriyaDTO>();
				setErrorMessage("No Data Found.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			} else {

				isSearch = true;
			}
		}
	}

	public void selectRow() {
		if (selectDTO.getServiceNo() != null || !nisiSeriyaDTO.getServiceNo().trim().equalsIgnoreCase("")) {

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
		nisiSeriyaDTO = new NisiSeriyaDTO();
		disabledIssuePermitSticker = true;
		disabledIssueServiceAgreement = true;
		disabledIssueLogSheets = true;
		nisiSeriyaList = new ArrayList<>();
		nisiSeriyaList = sisuSariyaService.getNisiSeriyaToIssue();
		getRequestNoList = new ArrayList<NisiSeriyaDTO>(0);
		getServiceReferenceNoList = new ArrayList<NisiSeriyaDTO>(0);
		getServiceNoList = new ArrayList<NisiSeriyaDTO>(0);
		getRequestNoList = sisuSariyaService.getApprovedRequestNoForNisiSeriya();
		getServiceReferenceNoList = sisuSariyaService.getApprovedServiceRefNoListForNisiSeriya();
		getServiceNoList = sisuSariyaService.getApprovedServiceNoListForNisiSeriya();
	}

	public void issueServiceAgreement() {
		selectDTO.setCurrentUser(loginUser);

		if (selectDTO.getServiceAgreementIssuedDate() != null) {
			selectDTO.setIssueType(1);

			sisuSariyaService.updateIssueServiceAgreementPermitStickerLogSheetsForNisiSeriya(selectDTO);

			setSuccessMessage("Service Agreement Issued");
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

		} else {
			setErrorMessage("Invalid Service Agreement Issue Date");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
		nisiSeriyaList = new ArrayList<>();
		nisiSeriyaList = sisuSariyaService.getNisiSeriyaToIssue();
	}

	public void issuePermitSticker() {
		selectDTO.setCurrentUser(loginUser);
		if (selectDTO.getPermitStickerIssuedDate() != null) {
			selectDTO.setIssueType(2);
			sisuSariyaService.updateIssueServiceAgreementPermitStickerLogSheetsForNisiSeriya(selectDTO);

			setSuccessMessage("Permit Sticker Issued");
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

		} else {
			setErrorMessage("Invalid Permit Sticker Issue Date");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
		nisiSeriyaList = new ArrayList<>();
		nisiSeriyaList = sisuSariyaService.getNisiSeriyaToIssue();
	}

	public void issueLogSheets() {
		if (selectDTO.getLogSheetsIssuedDate() != null) {

			logSheetCopies = 0;
			logSheetYear = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));

			selectDTO.setServiceTypeCode("S03");

			RequestContext.getCurrentInstance().update("dialogIssueLogSheets");
			RequestContext.getCurrentInstance().execute("PF('issueLogSheetsDialog').show()");

		} else {
			setErrorMessage("Invalid Log Sheets Issue Date");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void clearTwo() {
		disabledIssuePermitSticker = true;
		disabledIssueServiceAgreement = true;
		disabledIssueLogSheets = true;
		selectDTO = new NisiSeriyaDTO();
		nisiSeriyaList = new ArrayList<>();
		nisiSeriyaList = sisuSariyaService.getNisiSeriyaToIssue();
	}

	public void generateLogSheetRef() {
		if (selectDTO.getServiceTypeCode() != null && !selectDTO.getServiceTypeCode().isEmpty()
				&& !selectDTO.getServiceTypeCode().equalsIgnoreCase("")) {
			if (selectDTO.getServiceRefNo() != null && !selectDTO.getServiceRefNo().isEmpty()
					&& !selectDTO.getServiceRefNo().equalsIgnoreCase("")) {
				if (logSheetYear != null && !logSheetYear.isEmpty() && !logSheetYear.equalsIgnoreCase("")) {
					if (logSheetCopies > 0) {
						
						/* set id value : dhananjika.d (26/06/2024) */
						newLogSheetList = new ArrayList<LogSheetDTO>();
						for (int i = 0; i <= logSheetCopies; i++) {
							LogSheetDTO dto = new LogSheetDTO();
							dto.setId(i+1);
							newLogSheetList.add(dto);
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
							boolean success = sisuSariyaService.generateLogSheetRefNisiSeriya(selectDTO, logSheetYear, logSheetCopies,
									loginUser, newLogSheetList);

							selectDTO.setCurrentUser(loginUser);
							selectDTO.setIssueType(3);
							sisuSariyaService.updateIssueServiceAgreementPermitStickerLogSheetsForNisiSeriya(selectDTO);

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

	public void clearLogSheet() {
		logSheetCopies = 0;
		logSheetYear = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
		newLogSheetList = new ArrayList<LogSheetDTO>();
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

	public List<NisiSeriyaDTO> getGetRequestNoList() {
		return getRequestNoList;
	}

	public void setGetRequestNoList(List<NisiSeriyaDTO> getRequestNoList) {
		this.getRequestNoList = getRequestNoList;
	}

	public List<NisiSeriyaDTO> getGetServiceReferenceNoList() {
		return getServiceReferenceNoList;
	}

	public void setGetServiceReferenceNoList(List<NisiSeriyaDTO> getServiceReferenceNoList) {
		this.getServiceReferenceNoList = getServiceReferenceNoList;
	}

	public List<NisiSeriyaDTO> getGetServiceNoList() {
		return getServiceNoList;
	}

	public void setGetServiceNoList(List<NisiSeriyaDTO> getServiceNoList) {
		this.getServiceNoList = getServiceNoList;
	}

	public NisiSeriyaDTO getNisiSeriyaDTO() {
		return nisiSeriyaDTO;
	}

	public void setNisiSeriyaDTO(NisiSeriyaDTO nisiSeriyaDTO) {
		this.nisiSeriyaDTO = nisiSeriyaDTO;
	}

	public NisiSeriyaDTO getSelectDTO() {
		return selectDTO;
	}

	public void setSelectDTO(NisiSeriyaDTO selectDTO) {
		this.selectDTO = selectDTO;
	}

	public NisiSeriyaDTO getViewDTO() {
		return viewDTO;
	}

	public void setViewDTO(NisiSeriyaDTO viewDTO) {
		this.viewDTO = viewDTO;
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

	public List<NisiSeriyaDTO> getNisiSeriyaList() {
		return nisiSeriyaList;
	}

	public void setNisiSeriyaList(List<NisiSeriyaDTO> nisiSeriyaList) {
		this.nisiSeriyaList = nisiSeriyaList;
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

	public NisiSeriyaService getNisiSeriyaService() {
		return nisiSeriyaService;
	}

	public void setNisiSeriyaService(NisiSeriyaService nisiSeriyaService) {
		this.nisiSeriyaService = nisiSeriyaService;
	}

}
