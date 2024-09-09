package lk.informatics.ntc.view.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.TenderApplicantDTO;
import lk.informatics.ntc.model.dto.TenderDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.TenderService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "openTenderApp")
@ViewScoped
public class OpenTenderBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private TenderDTO tenderDTO;
	private TenderApplicantDTO tenderApplicantDTO;

	private TenderService tenderService;
	private CommonService commonService;

	private List<String> referenceNoList;
	private List<String> tenderApplicationNoList;
	private List<String> applicationNoList;
	private List<TenderApplicantDTO> routeNoList;

	private boolean isSecondEnvSaved;
	private boolean isFirstEnvSaved;
	private String errorMsg;
	private String alertMsg;
	private String applicationNo;
	private boolean disableFirstEnvSave;
	private boolean disableSecondtEnvSave;

	@PostConstruct
	public void init() {

		referenceNoList = new ArrayList<String>();
		applicationNoList = new ArrayList<String>();
		tenderApplicationNoList = new ArrayList<String>();
		routeNoList = new ArrayList<TenderApplicantDTO>();
		loadValues();
		tenderDTO = new TenderDTO();
		tenderApplicantDTO = new TenderApplicantDTO();
		disableSecondtEnvSave = true;

	}

	private void loadValues() {
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		tenderService = (TenderService) SpringApplicationContex.getBean("tenderService");
		referenceNoList = tenderService.getOnGoingReferenceNoList();

	}

	public void onReferenceNoChange() {

		String temptRefNo = tenderDTO.getTenderRefNo();
		tenderDTO = new TenderDTO();
		tenderApplicantDTO = new TenderApplicantDTO();
		tenderDTO.setTenderRefNo(temptRefNo);
		tenderDTO = tenderService.getDetailsByReferenceNo(tenderDTO);

		applicationNoList = tenderService.getOnGoingApplicationNoList(tenderDTO.getTenderRefNo());
		tenderApplicationNoList = tenderService.getNonCompletedOnGoingApplicationNoList(tenderDTO.getTenderRefNo());

		if (commonService.checkTaskOnSurveyTaskDetails(tenderDTO.getTenderRefNo(), "TD013", "C")) {
			disableSecondtEnvSave = true;
		} else {
			disableSecondtEnvSave = false;
		}

	}

	public void onApplicationNoChange() {
		if (getApplicationNo() != null && !getApplicationNo().isEmpty() && !getApplicationNo().equalsIgnoreCase("")) {
			disableSecondtEnvSave = true;
			disableFirstEnvSave = commonService.checkTaskHistory(getApplicationNo(), "TD012");

			if (commonService.checkTaskHistory(getApplicationNo(), "TD010") == true) {
				disableSecondtEnvSave = false;

			} else if (commonService.checkTaskHistory(getApplicationNo(), "TD011") == true) {
				disableSecondtEnvSave = false;

			}
			routeNoList = tenderService.getRouteNoList(tenderDTO.getTenderRefNo());
			tenderApplicantDTO = tenderService.getDetailsByApplicationNo(getApplicationNo());

			if (tenderApplicantDTO.getBankName() != null && !tenderApplicantDTO.getBankName().isEmpty()
					&& !tenderApplicantDTO.getBankName().equalsIgnoreCase("")) {
				disableSecondtEnvSave = false;
			} else if (commonService.checkTaskOnSurveyHisDetailsByTenderRefNo(tenderDTO.getTenderRefNo(), "TD013",
					"C")) {
				disableSecondtEnvSave = true;
			}

			if (commonService.checkTaskOnSurveyHisDetailsByTenderRefNo(tenderDTO.getTenderRefNo(), "TD013", "O")) {
				disableSecondtEnvSave = true;
				disableFirstEnvSave = true;
			}

		} else {
			errorMsg = "Application Number should be selected.";
			RequestContext.getCurrentInstance().update("frm_Tender");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public void onViewApplicationNoChange() {
		if (tenderApplicantDTO.getApplicationNo() != null && !tenderApplicantDTO.getApplicationNo().isEmpty()
				&& !tenderApplicantDTO.getApplicationNo().equalsIgnoreCase("")) {
			disableSecondtEnvSave = true;
			disableFirstEnvSave = commonService.checkTaskHistory(tenderApplicantDTO.getApplicationNo(), "TD012");

			if (commonService.checkTaskHistory(tenderApplicantDTO.getApplicationNo(), "TD010") == true) {
				disableSecondtEnvSave = false;

			} else if (commonService.checkTaskHistory(tenderApplicantDTO.getApplicationNo(), "TD011") == true) {
				disableSecondtEnvSave = false;

			}

			routeNoList = tenderService.getRouteNoList(tenderDTO.getTenderRefNo());
			tenderApplicantDTO = tenderService.getDetailsByApplicationNo(tenderApplicantDTO.getApplicationNo());

			if (tenderApplicantDTO.getBankName() != null && !tenderApplicantDTO.getBankName().isEmpty()
					&& !tenderApplicantDTO.getBankName().equalsIgnoreCase("")) {
				disableSecondtEnvSave = false;
			} else if (commonService.checkTaskOnSurveyHisDetailsByTenderRefNo(tenderDTO.getTenderRefNo(), "TD013",
					"C")) {
				disableSecondtEnvSave = true;
			}

			if (commonService.checkTaskOnSurveyHisDetailsByTenderRefNo(tenderDTO.getTenderRefNo(), "TD013", "O")) {
				disableSecondtEnvSave = true;
				disableFirstEnvSave = true;
			}

		} else {
			errorMsg = "Application Number should be selected.";
			RequestContext.getCurrentInstance().update("frm_Tender");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	// Search Button
	public void getDetailsByApplicationNo() {
		if (tenderApplicantDTO.getApplicationNo() != null && !tenderApplicantDTO.getApplicationNo().isEmpty()
				&& !tenderApplicantDTO.getApplicationNo().equalsIgnoreCase("")) {

			routeNoList = tenderService.getRouteNoList(tenderDTO.getTenderRefNo());
			tenderApplicantDTO = tenderService.getDetailsByApplicationNo(tenderApplicantDTO.getApplicationNo());

		} else {
			errorMsg = "Application Number should be selected.";
			RequestContext.getCurrentInstance().update("frm_Tender");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public void clear() {
		tenderDTO = new TenderDTO();
		tenderApplicantDTO = new TenderApplicantDTO();
		applicationNoList = new ArrayList<String>();

	}

	public void onRouteNoChange() {

		tenderApplicantDTO = tenderService.getDetailsByRouteNo(tenderApplicantDTO);

	}

	// click first save button
	public void saveFirstEvelop() {

		if (tenderApplicantDTO.getApplicationNo() != null && !tenderApplicantDTO.getApplicationNo().isEmpty()
				&& !tenderApplicantDTO.getApplicationNo().equalsIgnoreCase("")) {
			if (tenderApplicantDTO.getRefundableDepositAmount() != null) {
				if (tenderApplicantDTO.getReciptNo() != null && !tenderApplicantDTO.getReciptNo().isEmpty()
						&& !tenderApplicantDTO.getReciptNo().equalsIgnoreCase("")) {
					if (tenderApplicantDTO.getDeparture() != null && !tenderApplicantDTO.getDeparture().isEmpty()
							&& !tenderApplicantDTO.getDeparture().equalsIgnoreCase("")) {

						if (tenderApplicantDTO.getBankName() != null && !tenderApplicantDTO.getBankName().isEmpty()
								&& !tenderApplicantDTO.getBankName().equalsIgnoreCase("")) {

							isFirstEnvSaved = tenderService.saveFirstEvelop(tenderApplicantDTO);
							disableSecondtEnvSave = false;
							if (commonService.checkTaskHistory(tenderApplicantDTO.getApplicationNo(),
									"TD011") == false) {
								commonService.updateTaskStatus(tenderApplicantDTO.getApplicationNo(), "TD011", "TD012",
										"C", sessionBackingBean.getLoginUser());
								commonService.updateTaskStatusCompleted(tenderApplicantDTO.getApplicationNo(), "TD012",
										sessionBackingBean.getLoginUser());
							} else {
								disableSecondtEnvSave = false;

							}

							if (isFirstEnvSaved) {
								RequestContext.getCurrentInstance().update("frm_Tender");
								RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
							} else {
								errorMsg = "Data didn't save.";
								RequestContext.getCurrentInstance().update("frm_Tender");
								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
							}

						} else {
							errorMsg = "Bank Name shoud be entered.";
							RequestContext.getCurrentInstance().update("frm_Tender");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
						}

					} else {
						errorMsg = "Route No. shoud be entered.";
						RequestContext.getCurrentInstance().update("frm_Tender");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}

				} else {
					errorMsg = "Recipt No. shoud be entered.";
					RequestContext.getCurrentInstance().update("frm_Tender");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}

			} else {

				errorMsg = "Refundable Deposit Amount shoud be entered.";
				RequestContext.getCurrentInstance().update("frm_Tender");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

			}
		} else {
			errorMsg = "Application Number should be selected.";
			RequestContext.getCurrentInstance().update("frm_Tender");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	// click second save button
	public void saveSecondEvelop() {

		if (tenderApplicantDTO.getApplicationNo() != null && !tenderApplicantDTO.getApplicationNo().isEmpty()
				&& !tenderApplicantDTO.getApplicationNo().equalsIgnoreCase("")) {
			if (tenderApplicantDTO.getBidPrice() != null) {

				isSecondEnvSaved = tenderService.saveSecondEvelop(tenderApplicantDTO);

				commonService.updateTaskStatus(tenderApplicantDTO.getApplicationNo(), "TD012", "TD013", "C",
						sessionBackingBean.getLoginUser());

				if (!commonService.checkTaskHisByApplication(tenderApplicantDTO.getApplicationNo(), "TD013", "O")) {
					commonService.updateTaskStatusCompleted(tenderApplicantDTO.getApplicationNo(), "TD013",
							sessionBackingBean.getLoginUser());
				}

				commonService.updateTenderTaskDetails(tenderDTO.getTenderRefNo(), "TD013", "O");

				disableFirstEnvSave = commonService.checkTaskHistory(tenderApplicantDTO.getApplicationNo(), "TD012");

				if (isSecondEnvSaved) {
					tenderApplicationNoList = tenderService
							.getNonCompletedOnGoingApplicationNoList(tenderDTO.getTenderRefNo());
					RequestContext.getCurrentInstance().update("frm_Tender");
					RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
				} else {
					errorMsg = "Data didn't save.";
					RequestContext.getCurrentInstance().update("frm_Tender");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}

			} else {
				errorMsg = "Bid Price shoud be entered.";
				RequestContext.getCurrentInstance().update("frm_Tender");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		} else {
			errorMsg = "Application Number should be selected.";
			RequestContext.getCurrentInstance().update("frm_Tender");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void clearFirstEnvelop() {
		tenderApplicantDTO.setRefundableDepositAmount(null);
		tenderApplicantDTO.setReciptNo(null);
		tenderApplicantDTO.setRouteNo(null);
		tenderApplicantDTO.setBankName(null);
		tenderApplicantDTO.setDocumentAvailable(false);
		tenderApplicantDTO.setFirstEnvRemark(null);
	}

	public void clearSecondEnvelop() {
		tenderApplicantDTO.setOwnersSignature(false);
		tenderApplicantDTO.setParallelRootAvai(false);
		tenderApplicantDTO.setBidPrice(null);
		tenderApplicantDTO.setAcceptanceOfPermit(false);
		tenderApplicantDTO.setOwnersCapability(null);
		tenderApplicantDTO.setSecondEnvRemark(null);
	}

	public void completeOpenTender() {

		commonService.updateTenderTaskDetails(tenderDTO.getTenderRefNo(), "TD013", "C");

		if (commonService.checkTaskOnSurveyHisDetailsByTenderRefNo(tenderDTO.getTenderRefNo(), "TD013", "O")) {
			disableSecondtEnvSave = true;

		} else {
			disableSecondtEnvSave = false;
		}
	}

	public void setRouteNoList(List<TenderApplicantDTO> routeNoList) {
		routeNoList = routeNoList;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public TenderDTO getTenderDTO() {
		return tenderDTO;
	}

	public void setTenderDTO(TenderDTO tenderDTO) {
		this.tenderDTO = tenderDTO;
	}

	public TenderApplicantDTO getTenderApplicantDTO() {
		return tenderApplicantDTO;
	}

	public void setTenderApplicantDTO(TenderApplicantDTO tenderApplicantDTO) {
		this.tenderApplicantDTO = tenderApplicantDTO;
	}

	public TenderService getTenderService() {
		return tenderService;
	}

	public void setTenderService(TenderService tenderService) {
		this.tenderService = tenderService;
	}

	public List<String> getReferenceNoList() {
		return referenceNoList;
	}

	public void setReferenceNoList(List<String> referenceNoList) {
		this.referenceNoList = referenceNoList;
	}

	public List<String> getApplicationNoList() {
		return applicationNoList;
	}

	public void setApplicationNoList(List<String> applicationNoList) {
		this.applicationNoList = applicationNoList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean isSecondEnvSaved() {
		return isSecondEnvSaved;
	}

	public void setSecondEnvSaved(boolean isSecondEnvSaved) {
		this.isSecondEnvSaved = isSecondEnvSaved;
	}

	public boolean isFirstEnvSaved() {
		return isFirstEnvSaved;
	}

	public void setFirstEnvSaved(boolean isFirstEnvSaved) {
		this.isFirstEnvSaved = isFirstEnvSaved;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getAlertMsg() {
		return alertMsg;
	}

	public void setAlertMsg(String alertMsg) {
		this.alertMsg = alertMsg;
	}

	public List<TenderApplicantDTO> getRouteNoList() {
		return routeNoList;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public boolean isDisableFirstEnvSave() {
		return disableFirstEnvSave;
	}

	public void setDisableFirstEnvSave(boolean disableFirstEnvSave) {
		this.disableFirstEnvSave = disableFirstEnvSave;
	}

	public boolean isDisableSecondtEnvSave() {
		return disableSecondtEnvSave;
	}

	public void setDisableSecondtEnvSave(boolean disableSecondtEnvSave) {
		this.disableSecondtEnvSave = disableSecondtEnvSave;
	}

	public List<String> getTenderApplicationNoList() {
		return tenderApplicationNoList;
	}

	public void setTenderApplicationNoList(List<String> tenderApplicationNoList) {
		this.tenderApplicationNoList = tenderApplicationNoList;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

}
