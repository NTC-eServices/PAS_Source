package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;
import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.InspectionActionPointService;
import lk.informatics.ntc.model.service.PaymentVoucherService;
import lk.informatics.ntc.model.service.QueueManagementService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "inquiryAdmin")
@ViewScoped
public class InquiryAdminBackingBean {

	private CommonService commonService;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private List<CommonDTO> applicationNoList = new ArrayList<CommonDTO>(0);
	private List<CommonDTO> permitNoList = new ArrayList<CommonDTO>(0);
	private List<CommonDTO> vehicleNoList = new ArrayList<CommonDTO>(0);
	private List<CommonDTO> inquiryList;
	private List<CommonDTO> historyList;
	public PaymentVoucherService paymentVoucherService;
	private CommonDTO commonDTO, selectDTO, dto2;
	private boolean localcheckcounter = true;
	private String errorMessage, successMessage, alertMSG, counterNo, counterID;
	private boolean disableButton;
	private List<CommonDTO> counterList = new ArrayList<>();
	private QueueManagementService queueManagementService;
	private InspectionActionPointService inspectionActionPointService;
	private String queueNo = null;

	public InquiryAdminBackingBean() {
		paymentVoucherService = (PaymentVoucherService) SpringApplicationContex.getBean("paymentVoucherService");
		inspectionActionPointService = (InspectionActionPointService) SpringApplicationContex
				.getBean("inspectionActionPointService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		queueManagementService = (QueueManagementService) SpringApplicationContex.getBean("queueManagementService");
		loadValues();
	}

	@PostConstruct
	public void init() {
		sessionBackingBean.setRenewalViewMood(false);

		if (sessionBackingBean.approveURLStatus) {
			loadValues();
		}

		if (!sessionBackingBean.approveURLStatus) {
			sessionBackingBean.setApproveURL(null);
			inquiryList = sessionBackingBean.getTempInqueryDataList();
			sessionBackingBean.setApproveURLStatus(true);
			commonDTO = new CommonDTO();
			RequestContext.getCurrentInstance().update("modelDataTable");
			disableButton = false;

		}

	}

	public void loadValues() {
		applicationNoList = commonService.getApplicationNo();
		vehicleNoList = commonService.getVehicleNo();
		permitNoList = commonService.getPermitNo();
		commonDTO = new CommonDTO();
		selectDTO = new CommonDTO();
		disableButton = true;

		dto2 = new CommonDTO();

	}

	public void search() {

		if ((commonDTO.getApplicationNo() == null || commonDTO.getApplicationNo().trim().equalsIgnoreCase(""))
				&& (commonDTO.getVehicleNo() == null || commonDTO.getVehicleNo().trim().equalsIgnoreCase(""))
				&& (commonDTO.getPermitNo() == null || commonDTO.getPermitNo().trim().equalsIgnoreCase(""))) {

			setErrorMessage("Please enter Vehicle No. / Application No./ Permit No. ");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		} else {

			boolean isApplicationNoCorrect = paymentVoucherService.checkApplicationNumber(commonDTO.getApplicationNo());

			boolean isVehicleNoCoorect = paymentVoucherService.checkVehicleNumber(commonDTO.getVehicleNo());

			boolean ispermitCoorect = paymentVoucherService.checkPermitNumber(commonDTO.getPermitNo());

			if (isApplicationNoCorrect == true || isVehicleNoCoorect == true || ispermitCoorect == true) {

				inquiryList = new ArrayList<>();
				inquiryList = commonService.getInquiryDetails(commonDTO);

				if (!inquiryList.isEmpty()) {

					disableButton = false;

				} else {
					setErrorMessage("No data found.");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else {
				setErrorMessage("Entered data is incorrect.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		}
	}

	public String viewInspection() {
		boolean isTaskCompleteInDetails = false;
		boolean isTaskCompleteInHistory = false;

		if (selectDTO != null) {

			isTaskCompleteInDetails = commonService.checkTaskDetails(selectDTO.getApplicationNo(), "PM100");
			isTaskCompleteInHistory = commonService.checkTaskHistory(selectDTO.getApplicationNo(), "PM100");

			if (isTaskCompleteInDetails == true || isTaskCompleteInHistory == true) {

				HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
						.getRequest();

				sessionBackingBean.setPageMode("V");
				sessionBackingBean.setApproveURL(request.getRequestURL().toString());
				sessionBackingBean.setSearchURL(null);
				sessionBackingBean.setApproveURLStatus(true);
				sessionBackingBean.setApplicationNo(selectDTO.getApplicationNo());
				sessionBackingBean.setBusRegNo(selectDTO.getVehicleNo());
				sessionBackingBean.setPermitNo(selectDTO.getPermitNo());
				sessionBackingBean.tempInqueryDataList = inquiryList;
				sessionBackingBean.setViewedInspectionApplicationNo(selectDTO.getApplicationNo());
				sessionBackingBean.isClicked = true;
				sessionBackingBean.setSelectedOptionType("VIEW");
				sessionBackingBean.setVehicleInspectionMood(true);
				return "/pages/vehicleInspectionSet/vehicleInspectionInfoViewMode.xhtml#!";

			} else {
				setAlertMSG("Should be completed Vehicle Inspection task.");
				RequestContext.getCurrentInstance().execute("PF('alertMSG').show()");
				return null;
			}
		} else {
			setErrorMessage("Please select a row.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			return null;
		}

	}

	public String viewIssueNewPermit() {

		boolean isTaskCompleteInDetails = false;
		boolean isTaskCompleteInHistory = false;

		if (selectDTO != null) {

			isTaskCompleteInDetails = commonService.checkTaskDetails(selectDTO.getApplicationNo(), "PM200");
			isTaskCompleteInHistory = commonService.checkTaskHistory(selectDTO.getApplicationNo(), "PM200");

			if (isTaskCompleteInDetails == true || isTaskCompleteInHistory == true) {

				HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
						.getRequest();
				sessionBackingBean.setPageMode("V");
				sessionBackingBean.setApproveURL(request.getRequestURL().toString());
				sessionBackingBean.setSearchURL(null);
				sessionBackingBean.setApproveURLStatus(true);
				sessionBackingBean.setApplicationNo(selectDTO.getApplicationNo());
				sessionBackingBean.setBusRegNo(selectDTO.getVehicleNo());
				sessionBackingBean.setPermitNo(selectDTO.getPermitNo());
				sessionBackingBean.tempInqueryDataList = inquiryList;
				sessionBackingBean.isClicked = true;

				return "/pages/issueNewPermit/viewNewPermit.xhtml#!";

			} else {
				setAlertMSG("Should be completed Issue Permit task.");
				RequestContext.getCurrentInstance().execute("PF('alertMSG').show()");
				return null;
			}

		} else {
			setErrorMessage("Please select a row.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			return null;
		}
	}

	public String viewPermitRenewal() {

		boolean isTaskCompleteInDetails = false;
		boolean isTaskCompleteInHistory = false;

		if (selectDTO != null) {

			isTaskCompleteInDetails = commonService.checkTaskDetails(selectDTO.getApplicationNo(), "PR200");
			isTaskCompleteInHistory = commonService.checkTaskHistory(selectDTO.getApplicationNo(), "PR200");

			if (isTaskCompleteInDetails == true || isTaskCompleteInHistory == true) {

				HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
						.getRequest();
				sessionBackingBean.setPageMode("V");
				sessionBackingBean.setApproveURL(request.getRequestURL().toString());
				sessionBackingBean.setSearchURL(null);
				sessionBackingBean.setApproveURLStatus(true);
				sessionBackingBean.setApplicationNo(selectDTO.getApplicationNo());
				sessionBackingBean.setBusRegNo(selectDTO.getVehicleNo());
				sessionBackingBean.setPermitNo(selectDTO.getPermitNo());
				sessionBackingBean.tempInqueryDataList = inquiryList;
				sessionBackingBean.isClicked = true;

				return "/pages/viewPermitRenewals/viewPermitRenewalsNew.xhtml#!";

			} else {

				setAlertMSG("Should be completed Permit Renewal task.");
				RequestContext.getCurrentInstance().execute("PF('alertMSG').show()");
				return null;
			}

		} else {

			setErrorMessage("Please select a row.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			return null;
		}

	}

	public String viewPayments() {

		String paymentType = null;

		if (selectDTO != null) {

			commonDTO.setVoucherNo(commonService.getVoucherNo(selectDTO));

			if (commonDTO.getVoucherNo() != null) {
				paymentType = commonService.getPaymentType(selectDTO);

				if (paymentType == null) {

					setAlertMSG("No data found");
					RequestContext.getCurrentInstance().execute("PF('alertMSG').show()");
					return null;

				} else if (paymentType.equals("V")) {

					HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance()
							.getExternalContext().getRequest();
					sessionBackingBean.setPageMode("V");
					sessionBackingBean.setApproveURL(request.getRequestURL().toString());
					sessionBackingBean.setSearchURL(null);
					sessionBackingBean.setApproveURLStatus(true);
					sessionBackingBean.setApplicationNo(commonDTO.getApplicationNo());
					sessionBackingBean.isClicked = true;
					sessionBackingBean.setPaymentType("V");
					sessionBackingBean.setVoucherNo(commonDTO.getVoucherNo());
					sessionBackingBean.tempInqueryDataList = inquiryList;
					return "/pages/payment/viewPaymentVoucher.xhtml#!";

				} else if (paymentType.equals("AP")) {

					HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance()
							.getExternalContext().getRequest();
					sessionBackingBean.setPageMode("V");
					sessionBackingBean.setApproveURL(request.getRequestURL().toString());
					sessionBackingBean.setSearchURL(null);
					sessionBackingBean.setApproveURLStatus(true);
					sessionBackingBean.setApplicationNo(commonDTO.getApplicationNo());
					sessionBackingBean.setVoucherNo(commonDTO.getVoucherNo());
					sessionBackingBean.isClicked = true;
					sessionBackingBean.setPaymentType("AP");
					sessionBackingBean.tempInqueryDataList = inquiryList;
					return "/pages/payment/viewAdvancedPaymentVoucher.xhtml#!";

				} else {
					setAlertMSG("Can not identify the payment type");
					RequestContext.getCurrentInstance().execute("PF('alertMSG').show()");
					return null;
				}
			} else {
				setAlertMSG("Should be completed Generate Voucher task.");
				RequestContext.getCurrentInstance().execute("PF('alertMSG').show()");
				return null;
			}
		} else {

			setErrorMessage("Please select a row.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			return null;
		}

	}

	public void viewHistory() {

		if (selectDTO != null) {

			historyList = new ArrayList<>();
			historyList = commonService.getHistoryData(selectDTO);

			if (historyList.isEmpty()) {

				setAlertMSG("No data found.");
				RequestContext.getCurrentInstance().execute("PF('alertMSG').show()");

			} else {
				RequestContext.getCurrentInstance().execute("PF('historyDialog').show()");
			}

		} else {

			setErrorMessage("Please select a row.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		}

	}

	public void clear() {
		inquiryList = new ArrayList<>();
		commonDTO = new CommonDTO();
		disableButton = true;
		queueNo = null;
		dto2 = null;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public List<CommonDTO> getApplicationNoList() {
		return applicationNoList;
	}

	public void setApplicationNoList(List<CommonDTO> applicationNoList) {
		this.applicationNoList = applicationNoList;
	}

	public List<CommonDTO> getPermitNoList() {
		return permitNoList;
	}

	public void setPermitNoList(List<CommonDTO> permitNoList) {
		this.permitNoList = permitNoList;
	}

	public InspectionActionPointService getInspectionActionPointService() {
		return inspectionActionPointService;
	}

	public void setInspectionActionPointService(InspectionActionPointService inspectionActionPointService) {
		this.inspectionActionPointService = inspectionActionPointService;
	}

	public List<CommonDTO> getVehicleNoList() {
		return vehicleNoList;
	}

	public void setVehicleNoList(List<CommonDTO> vehicleNoList) {
		this.vehicleNoList = vehicleNoList;
	}

	public CommonDTO getCommonDTO() {
		return commonDTO;
	}

	public void setCommonDTO(CommonDTO commonDTO) {
		this.commonDTO = commonDTO;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public List<CommonDTO> getInquiryList() {
		return inquiryList;
	}

	public void setInquiryList(List<CommonDTO> inquiryList) {
		this.inquiryList = inquiryList;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public CommonDTO getSelectDTO() {
		return selectDTO;
	}

	public void setSelectDTO(CommonDTO selectDTO) {
		this.selectDTO = selectDTO;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	public boolean isDisableButton() {
		return disableButton;
	}

	public void setDisableButton(boolean disableButton) {
		this.disableButton = disableButton;
	}

	public String getAlertMSG() {
		return alertMSG;
	}

	public void setAlertMSG(String alertMSG) {
		this.alertMSG = alertMSG;
	}

	public boolean isLocalcheckcounter() {
		return localcheckcounter;
	}

	public void setLocalcheckcounter(boolean localcheckcounter) {
		this.localcheckcounter = localcheckcounter;
	}

	public String getCounterID() {
		return counterID;
	}

	public void setCounterID(String counterID) {
		this.counterID = counterID;
	}

	public List<CommonDTO> getCounterList() {
		return counterList;
	}

	public void setCounterList(List<CommonDTO> counterList) {
		this.counterList = counterList;
	}

	public String getCounterNo() {
		return counterNo;
	}

	public void setCounterNo(String counterNo) {
		this.counterNo = counterNo;
	}

	public QueueManagementService getQueueManagementService() {
		return queueManagementService;
	}

	public void setQueueManagementService(QueueManagementService queueManagementService) {
		this.queueManagementService = queueManagementService;
	}

	public String getQueueNo() {
		return queueNo;
	}

	public List<CommonDTO> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<CommonDTO> historyList) {
		this.historyList = historyList;
	}

	public CommonDTO getDto2() {
		return dto2;
	}

	public void setDto2(CommonDTO dto2) {
		this.dto2 = dto2;
	}

	public void setQueueNo(String queueNo) {
		this.queueNo = queueNo;
	}

}
