package lk.informatics.ntc.view.beans;

import java.io.Serializable;
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

import org.primefaces.PrimeFaces;
import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.LogSheetDTO;
import lk.informatics.ntc.model.dto.LogSheetMaintenanceDTO;
import lk.informatics.ntc.model.dto.SisuSeriyaDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.LogsheetMaintenanceService;
import lk.informatics.ntc.model.service.SisuSariyaService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "issueLogSheetsBackingBean")
@ViewScoped

public class IssueLogSheetsBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private List<SisuSeriyaDTO> getRequestNoList = new ArrayList<SisuSeriyaDTO>(0);
	private List<SisuSeriyaDTO> getServiceReferenceNoList = new ArrayList<SisuSeriyaDTO>(0);
	private List<SisuSeriyaDTO> getServiceNoList = new ArrayList<SisuSeriyaDTO>(0);
	private List<SisuSeriyaDTO> operatorDepoNameList = new ArrayList<SisuSeriyaDTO>(0);
	private List<SisuSeriyaDTO> busNoList = new ArrayList<SisuSeriyaDTO>(0);
	private List<LogSheetMaintenanceDTO> getServiceTypeList = new ArrayList<LogSheetMaintenanceDTO>();
	private SisuSeriyaDTO sisuSeriyaDTO, selectDTO, viewDTO;
	private boolean disabledIssueServiceAgreement, disabledIssuePermitSticker, disabledIssueLogSheets;
	private String rejectReason, alertMSG, successMessage, errorMessage, loginUser;
	private SisuSariyaService sisuSariyaService;
	private CommonService commonService;
	private LogsheetMaintenanceService logsheetMaintenanceService;
	private List<SisuSeriyaDTO> sisuSeriyaList;
	private List<LogSheetDTO> chekPrevLogSheetList = new ArrayList<LogSheetDTO>(0);;
	private DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	private boolean isSearch, issueNew, createMode, viewMode, renderGenerate;
	private String logSheetYear;
	private int logSheetCopies;
	private LogSheetDTO logSheetDTO;
	private List<LogSheetDTO> logSheetList;
	private List<LogSheetDTO> newLogSheetList;

	private boolean disabledServiceRefNoMenu = true;
	private List<SisuSeriyaDTO> drpdServiceNoList;
	
	private boolean alreadyHaveData;

	@PostConstruct
	public void init() {

		sisuSariyaService = (SisuSariyaService) SpringApplicationContex.getBean("sisuSariyaService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		logsheetMaintenanceService = (LogsheetMaintenanceService) SpringApplicationContex
				.getBean("logSheetMaintenanceService");
		sisuSeriyaDTO = new SisuSeriyaDTO();

		loginUser = sessionBackingBean.getLoginUser();

		loadValues();

		createMode = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN106", "C");
		viewMode = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN106", "V");
		drpdServiceNoList = new ArrayList<SisuSeriyaDTO>();
		if (createMode == true) {
			renderGenerate = true;
		}
	}

	private void loadValues() {

		getRequestNoList = sisuSariyaService.getApprovedRequestNoForEditMode();
		getServiceNoList = sisuSariyaService.getApprovedServiceNoListForEditMode();
		getServiceTypeList = logsheetMaintenanceService.serviceTypeDropDown();
		operatorDepoNameList = sisuSariyaService
				.getOperatorDepoNameListForIssueLogSheets(sisuSeriyaDTO.getServiceTypeCode());
		busNoList = sisuSariyaService.getBusNoListForIssueLogSheets(sisuSeriyaDTO.getServiceTypeCode());
		selectDTO = new SisuSeriyaDTO();
		viewDTO = new SisuSeriyaDTO();
		disabledIssueServiceAgreement = true;
		disabledIssuePermitSticker = true;
		disabledIssueLogSheets = true;
		sisuSeriyaList = new ArrayList<SisuSeriyaDTO>();
		logSheetList = new ArrayList<LogSheetDTO>();
		newLogSheetList = new ArrayList<LogSheetDTO>();
		sisuSeriyaList = sisuSariyaService.getSisuSeriyaToIssue();
		logSheetCopies = 0;
		logSheetYear = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
		issueNew = false;
		alreadyHaveData = false;
		renderGenerate = false;

	}

	public void ajaxselectType() {
		if (!sisuSeriyaDTO.getServiceTypeCode().equals("") && sisuSeriyaDTO.getServiceTypeCode() != null) {
			if (sisuSeriyaDTO.getServiceTypeCode().equals("S01")) {
				getServiceReferenceNoList = new ArrayList<SisuSeriyaDTO>(0);
				getServiceReferenceNoList = sisuSariyaService.getApprovedServiceRefNo();
				drpdServiceNoList = sisuSariyaService.getServiceNoListForIssueLogSheet();
			} else if (sisuSeriyaDTO.getServiceTypeCode().equals("S02")) {
				getServiceReferenceNoList = new ArrayList<SisuSeriyaDTO>(0);
				getServiceReferenceNoList = sisuSariyaService
						.getApprovedServiceRefNoListGamiSeriyaLogSheet();
			} else if (sisuSeriyaDTO.getServiceTypeCode().equals("S03")) {
				getServiceReferenceNoList = new ArrayList<SisuSeriyaDTO>(0);
				getServiceReferenceNoList = sisuSariyaService
						.getApprovedServiceRefNoListForEditModeNisiSeriyaForIssueLogSheet();
			}
			operatorDepoNameList = sisuSariyaService
					.getOperatorDepoNameListForIssueLogSheets(sisuSeriyaDTO.getServiceTypeCode());
			busNoList = sisuSariyaService.getBusNoListForIssueLogSheets(sisuSeriyaDTO.getServiceTypeCode());
			setDisabledServiceRefNoMenu(false);
		}
	}

	public void onSelectOperatorDepoName() {
		if (!sisuSeriyaDTO.getServiceTypeCode().equals("") && sisuSeriyaDTO.getServiceTypeCode() != null
				&& !sisuSeriyaDTO.getNameOfOperator().equals("") && sisuSeriyaDTO.getNameOfOperator() != null) {
			if (sisuSeriyaDTO.getServiceTypeCode().equals("S01")) {
				getServiceReferenceNoList = new ArrayList<SisuSeriyaDTO>(0);
				getServiceReferenceNoList = sisuSariyaService.getApprovedServiceRefNoListForEditMode(sisuSeriyaDTO);
			} else if (sisuSeriyaDTO.getServiceTypeCode().equals("S02")) {
				getServiceReferenceNoList = new ArrayList<SisuSeriyaDTO>(0);
				getServiceReferenceNoList = sisuSariyaService
						.getApprovedServiceRefNoListGamiSeriyaForEditModeIssueLogSheet(sisuSeriyaDTO);
			} else if (sisuSeriyaDTO.getServiceTypeCode().equals("S03")) {
				getServiceReferenceNoList = new ArrayList<SisuSeriyaDTO>(0);
				getServiceReferenceNoList = sisuSariyaService
						.getApprovedServiceRefNoListForEditModeNisiSeriyaForIssueLogSheet(sisuSeriyaDTO);
			}
		}
	}

	public void onSelectBusNo() {
		if (!sisuSeriyaDTO.getServiceTypeCode().equals("") && sisuSeriyaDTO.getServiceTypeCode() != null
				&& !sisuSeriyaDTO.getBusRegNo().equals("") && sisuSeriyaDTO.getBusRegNo() != null) {
			if (sisuSeriyaDTO.getServiceTypeCode().equals("S01")) {
				getServiceReferenceNoList = new ArrayList<SisuSeriyaDTO>(0);
				getServiceReferenceNoList = sisuSariyaService.getApprovedServiceRefNoListForEditMode(sisuSeriyaDTO);
			} else if (sisuSeriyaDTO.getServiceTypeCode().equals("S02")) {
				getServiceReferenceNoList = new ArrayList<SisuSeriyaDTO>(0);
				getServiceReferenceNoList = sisuSariyaService
						.getApprovedServiceRefNoListGamiSeriyaForEditModeIssueLogSheet(sisuSeriyaDTO);
			} else if (sisuSeriyaDTO.getServiceTypeCode().equals("S03")) {
				getServiceReferenceNoList = new ArrayList<SisuSeriyaDTO>(0);
				getServiceReferenceNoList = sisuSariyaService
						.getApprovedServiceRefNoListForEditModeNisiSeriyaForIssueLogSheet(sisuSeriyaDTO);
			}
		}
	}
	 public void onServiceNoChange() {
		 if (sisuSeriyaDTO.getServiceTypeCode().equals("S01")) {
				getServiceReferenceNoList = new ArrayList<SisuSeriyaDTO>(0);
				getServiceReferenceNoList = sisuSariyaService.getServiceRefNumberByServiceNo(sisuSeriyaDTO);
		 }
	 }
	 
	 
	 public void onSelectReferenceNumber() {
		if(sisuSeriyaDTO.getServiceRefNo() != null && !sisuSeriyaDTO.getServiceRefNo().trim().equals("")) {
			StringBuilder builder = new StringBuilder();
			String concatenatedString = null;
			List<String> pendingRefNum =sisuSariyaService.pendingReferenceNumberList(sisuSeriyaDTO.getServiceRefNo());
			
			if(!pendingRefNum.isEmpty()) {
				renderGenerate = false;
				for(int i = 0; i < pendingRefNum.size(); i++) {
					builder.append( pendingRefNum.get(i) + ", ");
					concatenatedString = builder.toString();
				}	
				setAlertMSG("Renewal pending record: "
						+ concatenatedString.substring(0, concatenatedString.length() - 2) + ".");
				RequestContext.getCurrentInstance().execute("PF('alertMSG').show()");
				
				
			}else {
				// To find the ref no has expired or not : dhananjika.d 16/08/2024
				
				String expireResult = sisuSariyaService.isRefNoHasExpired(sisuSeriyaDTO.getServiceRefNo());
				
				if(expireResult != null) {
					errorMessage = expireResult;
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
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

	public void clearTwo() {

		newLogSheetList = new ArrayList<LogSheetDTO>();

	}

	public void generateLogSheetRef() {
		alreadyHaveData = false;
		if (sisuSeriyaDTO.getServiceTypeCode() != null && !sisuSeriyaDTO.getServiceTypeCode().isEmpty()
				&& !sisuSeriyaDTO.getServiceTypeCode().equalsIgnoreCase("")) {
			if (sisuSeriyaDTO.getServiceRefNo() != null && !sisuSeriyaDTO.getServiceRefNo().isEmpty()
					&& !sisuSeriyaDTO.getServiceRefNo().equalsIgnoreCase("")) {
				if (logSheetYear != null && !logSheetYear.isEmpty() && !logSheetYear.equalsIgnoreCase("")) {
					if (logSheetCopies > 0) {
						issueNew = true;
						newLogSheetList = new ArrayList<LogSheetDTO>();

						/* set id value : dhananjika.d (26/06/2024) */
						for (int i = 0; i <= logSheetCopies; i++) {
							LogSheetDTO dto = new LogSheetDTO();
							dto.setId(i+1);
							newLogSheetList.add(dto);
						}
						
						/* Added by dhanajika.d 06/03/2024
						 * 		Check already have a data for this refno and year and service type */
						
						alreadyHaveData = sisuSariyaService.checkAlreadyHave(sisuSeriyaDTO.getServiceRefNo(), logSheetYear, sisuSeriyaDTO.getServiceTypeCode());

					} else {
						setErrorMessage("Invalid No. of Copies");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}
				} else {
					setErrorMessage("Invalid Year!");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			} else {
				setErrorMessage("Please select the Service Reference No.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {
			setErrorMessage("Please select the Service Type.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void saveLogSheets() {
		if (sisuSeriyaDTO.getServiceTypeCode() != null && !sisuSeriyaDTO.getServiceTypeCode().isEmpty()
				&& !sisuSeriyaDTO.getServiceTypeCode().equalsIgnoreCase("")) {
			if (sisuSeriyaDTO.getServiceRefNo() != null && !sisuSeriyaDTO.getServiceRefNo().isEmpty()
					&& !sisuSeriyaDTO.getServiceRefNo().equalsIgnoreCase("")) {
				if (logSheetYear != null && !logSheetYear.isEmpty() && !logSheetYear.equalsIgnoreCase("")) {
					if (logSheetCopies > 0) {
						if(!checkLogSheetNo()) {
							
							/* Added by dhanajika.d 07/03/2024
							 * 		If already not have then insert new data to nt_m_log_sheets */
							
							if(!alreadyHaveData) {
								sisuSariyaService.saveToLogSheets(sisuSeriyaDTO.getServiceRefNo(), logSheetYear, logSheetCopies, loginUser,
										sisuSeriyaDTO.getServiceTypeCode());
							}
							
							boolean insertSuccess = sisuSariyaService.generateLogSheetRefUpadte(sisuSeriyaDTO, logSheetYear, logSheetCopies,
									loginUser, newLogSheetList,sisuSeriyaDTO.getServiceTypeCode());

							sisuSeriyaDTO.setCurrentUser(loginUser);

							logSheetList = new ArrayList<LogSheetDTO>();
							logSheetList = sisuSariyaService.getLogSheetsByServiceRefNo(sisuSeriyaDTO.getServiceRefNo());
							newLogSheetList = new ArrayList<LogSheetDTO>();
							logSheetCopies = 0;
							logSheetYear = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
							issueNew = false;

							if(insertSuccess) {
								setSuccessMessage("Log Sheets Issued");
								RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
							}else {
								setErrorMessage("Unsuccessful !!");
								RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
							}
						}
						
						
					} else {
						setErrorMessage("Invalid No. of Copies");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}
				} else {
					setErrorMessage("Invalid Year!");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			} else {
				setErrorMessage("Please select the Service Reference No.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {
			setErrorMessage("Please select the Service Type.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	private boolean checkDuplicateLogSheetsRefNo(SisuSeriyaDTO sisuSeriyaDTO2, List<LogSheetDTO> newLogSheetList2) {

		boolean isCheckedDuplicateValue = false;

		chekPrevLogSheetList = new ArrayList<LogSheetDTO>();
		chekPrevLogSheetList = sisuSariyaService.getLogSheetsByServiceRefNoAndYear(sisuSeriyaDTO.getServiceRefNo(),
				logSheetYear);
		int prevListSize = chekPrevLogSheetList.size();
		int newListSize = newLogSheetList2.size();
		for (int i = 0; i < prevListSize; i++) {
			for (int j = 1; j < newListSize; j++) {

				if (chekPrevLogSheetList.get(i).getLogSheetRefNo().equals(newLogSheetList2.get(j).getLogSheetRefNo())) {
					isCheckedDuplicateValue = true;
					break;
				} else {
					isCheckedDuplicateValue = false;
				}
			}
			if (isCheckedDuplicateValue == true) {
				break;
			} else {

			}
		}

		if (chekPrevLogSheetList.isEmpty()) {
			isCheckedDuplicateValue = false;
		}

		return isCheckedDuplicateValue;
	}

	public void searchLogSheet() {
		if (sisuSeriyaDTO.getServiceRefNo() != null && !sisuSeriyaDTO.getServiceRefNo().isEmpty()
				&& !sisuSeriyaDTO.getServiceRefNo().equalsIgnoreCase("")) {
			if (logSheetYear != null && !logSheetYear.isEmpty() && !logSheetYear.equalsIgnoreCase("")) {
				logSheetList = new ArrayList<LogSheetDTO>();

				logSheetList = sisuSariyaService.getLogSheetsByServiceRefNoYearNameOfOperatorBusNo(
						sisuSeriyaDTO.getServiceRefNo(), logSheetYear, sisuSeriyaDTO.getNameOfOperator(),
						sisuSeriyaDTO.getBusRegNo(), sisuSeriyaDTO.getServiceTypeCode());

			} else {
				setErrorMessage("Invalid Year!");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {
			setErrorMessage("Please select the Service Reference No.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void clearLogSheet() {
		issueNew = false;
		sisuSeriyaDTO = new SisuSeriyaDTO();
		logSheetCopies = 0;
		logSheetYear = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
		logSheetList = new ArrayList<LogSheetDTO>();
		newLogSheetList = new ArrayList<LogSheetDTO>();
		getServiceTypeList = logsheetMaintenanceService.serviceTypeDropDown();
		setDisabledServiceRefNoMenu(true);
		getServiceReferenceNoList = new ArrayList<SisuSeriyaDTO>(0);
		operatorDepoNameList = sisuSariyaService
				.getOperatorDepoNameListForIssueLogSheets(sisuSeriyaDTO.getServiceTypeCode());
		busNoList = sisuSariyaService.getBusNoListForIssueLogSheets(sisuSeriyaDTO.getServiceTypeCode());
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
				setErrorMessage("Invalid Year!");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				logSheetYear = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
			}

		}

	}

	public boolean checkLogSheetNo() {
		boolean exists = false;
		boolean finalResult = false;
		StringBuilder builder = new StringBuilder();
		for (LogSheetDTO logSheetDTO : newLogSheetList) {
			if (logSheetDTO.getLogSheetRefNo() != null && logSheetDTO.getLogSheetRefNo() != "") {
				exists = commonService.existingLogSheetReferenceNo(logSheetDTO.getLogSheetRefNo(), sisuSeriyaDTO.getServiceTypeCode());
				if (exists) {
					builder.append(logSheetDTO.getLogSheetRefNo() + ", ");
					String concatenatedString = builder.toString();
					setErrorMessage("Duplicate Log Sheet Reference No. - "
							+ concatenatedString.substring(0, concatenatedString.length() - 2));
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					finalResult = true;
				}
			}
		}
		
		return finalResult;
	}
	
	
	/* New method to remove spaces : dhananjika.d (25/06/2024) */
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

	public List<LogSheetMaintenanceDTO> getGetServiceTypeList() {
		return getServiceTypeList;
	}

	public void setGetServiceTypeList(List<LogSheetMaintenanceDTO> getServiceTypeList) {
		this.getServiceTypeList = getServiceTypeList;
	}

	public LogsheetMaintenanceService getLogsheetMaintenanceService() {
		return logsheetMaintenanceService;
	}

	public void setLogsheetMaintenanceService(LogsheetMaintenanceService logsheetMaintenanceService) {
		this.logsheetMaintenanceService = logsheetMaintenanceService;
	}

	public boolean isIssueNew() {
		return issueNew;
	}

	public void setIssueNew(boolean issueNew) {
		this.issueNew = issueNew;
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

	public boolean isRenderGenerate() {
		return renderGenerate;
	}

	public void setRenderGenerate(boolean renderGenerate) {
		this.renderGenerate = renderGenerate;
	}

	public boolean isDisabledServiceRefNoMenu() {
		return disabledServiceRefNoMenu;
	}

	public void setDisabledServiceRefNoMenu(boolean disabledServiceRefNoMenu) {
		this.disabledServiceRefNoMenu = disabledServiceRefNoMenu;
	}

	public List<SisuSeriyaDTO> getOperatorDepoNameList() {
		return operatorDepoNameList;
	}

	public void setOperatorDepoNameList(List<SisuSeriyaDTO> operatorDepoNameList) {
		this.operatorDepoNameList = operatorDepoNameList;
	}

	public List<SisuSeriyaDTO> getBusNoList() {
		return busNoList;
	}

	public void setBusNoList(List<SisuSeriyaDTO> busNoList) {
		this.busNoList = busNoList;
	}

	public List<SisuSeriyaDTO> getDrpdServiceNoList() {
		return drpdServiceNoList;
	}

	public void setDrpdServiceNoList(List<SisuSeriyaDTO> drpdServiceNoList) {
		this.drpdServiceNoList = drpdServiceNoList;
	}

	public boolean isAlreadyHaveData() {
		return alreadyHaveData;
	}

	public void setAlreadyHaveData(boolean alreadyHaveData) {
		this.alreadyHaveData = alreadyHaveData;
	}
	
}