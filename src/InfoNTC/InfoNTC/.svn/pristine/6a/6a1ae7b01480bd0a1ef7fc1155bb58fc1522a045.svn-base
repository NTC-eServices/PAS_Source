package lk.informatics.ntc.view.beans;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.InspectionActionPointService;
import lk.informatics.ntc.model.service.MigratedService;
import lk.informatics.ntc.model.service.QueueManagementService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "inquiry")
@ViewScoped
public class InquiryBackingBean {

	private CommonService commonService;
	private MigratedService migratedService;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private List<CommonDTO> applicationNoList = new ArrayList<CommonDTO>(0);
	private List<CommonDTO> permitNoList = new ArrayList<CommonDTO>(0);
	private List<CommonDTO> vehicleNoList = new ArrayList<CommonDTO>(0);
	private List<CommonDTO> inquiryList;
	private List<CommonDTO> historyList;
	private CommonDTO commonDTO, selectDTO, dto2;
	private boolean localcheckcounter = true;
	private String errorMessage, successMessage, alertMSG, counterNo, counterID;
	private boolean disableButton, disableCallNext, disableSkip;
	private List<CommonDTO> counterList = new ArrayList<>();
	private QueueManagementService queueManagementService;
	private InspectionActionPointService inspectionActionPointService;
	private String queueNo = null;

	public InquiryBackingBean() {
		inspectionActionPointService = (InspectionActionPointService) SpringApplicationContex
				.getBean("inspectionActionPointService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		migratedService = (MigratedService) SpringApplicationContex.getBean("migratedService");
		queueManagementService = (QueueManagementService) SpringApplicationContex.getBean("queueManagementService");
		loadValues();
		disableSkip = true;

	}

	@PostConstruct
	public void init() {
		sessionBackingBean.setRenewalViewMood(false);
		counterID = "12";

		RequestContext.getCurrentInstance().update("idCounter");

		localcheckcounter = sessionBackingBean.isCounterCheck();
		counterList = commonService.counterdropdown();

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
				sessionBackingBean.setViewedInspectionApplicationNo(selectDTO.getApplicationNo());
				sessionBackingBean.setApplicationNo(selectDTO.getApplicationNo());
				sessionBackingBean.setBusRegNo(selectDTO.getVehicleNo());
				sessionBackingBean.setPermitNo(selectDTO.getPermitNo());
				sessionBackingBean.tempInqueryDataList = inquiryList;
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
		disableSkip = true;
		disableCallNext = false;
		queueNo = null;
		dto2 = null;
	}

	public void search() {

		CommonDTO dto = new CommonDTO();
		boolean isQueueNoAvailable = false;

		if ((commonDTO.getQueueNo() == null || commonDTO.getQueueNo().trim().equalsIgnoreCase(""))) {

			setErrorMessage("Please enter Queue No. ");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		} else {
			String searchedQueueNo = commonDTO.getQueueNo();
			isQueueNoAvailable = commonService.checkQueueNo(searchedQueueNo);

			if (isQueueNoAvailable == true) {

				dto = commonService.getInquiryApplicationORVehicleNo(searchedQueueNo);

				if ((dto.getApplicationNo() != null && !dto.getApplicationNo().isEmpty()
						&& !dto.getApplicationNo().trim().equalsIgnoreCase("")) && dto.getVehicleNo() != null
						&& !dto.getVehicleNo().isEmpty() && !dto.getVehicleNo().trim().equalsIgnoreCase("")) {

					commonDTO.setApplicationNo(dto.getApplicationNo());
					commonDTO.setVehicleNo(dto.getVehicleNo());

				} else if (dto.getApplicationNo() != null && !dto.getApplicationNo().isEmpty()
						&& !dto.getApplicationNo().trim().equalsIgnoreCase("")) {

					commonDTO.setApplicationNo(dto.getApplicationNo());
					commonDTO.setVehicleNo(null);

				} else if (dto.getVehicleNo() != null && !dto.getVehicleNo().isEmpty()
						&& !dto.getVehicleNo().trim().equalsIgnoreCase("")) {

					commonDTO.setApplicationNo(null);
					commonDTO.setVehicleNo(dto.getVehicleNo());
				} else {
					commonDTO.setApplicationNo(null);
					commonDTO.setVehicleNo(null);
				}

				inquiryList = new ArrayList<>();
				inquiryList = commonService.getInquiryDetails(commonDTO);

				if (!inquiryList.isEmpty()) {

					disableButton = false;

				} else {
					setErrorMessage("No data found.");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			} else {
				setErrorMessage("Entered Queue No. is incorrect or outdated");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		}
	}

	public void callNext() {

		String queueNo = null;

		CommonDTO dto2 = new CommonDTO();

		queueNo = queueManagementService.callNextQueueNumberAction("09", null);

		if (queueNo != null && !queueNo.isEmpty() && !queueNo.trim().equalsIgnoreCase("")) {

			disableSkip = false;
			disableCallNext = true;

			commonDTO.setQueueNo(queueNo);

			dto2 = commonService.getInquiryApplicationORVehicleNo(queueNo);

			if ((dto2.getApplicationNo() != null && !dto2.getApplicationNo().isEmpty()
					&& !dto2.getApplicationNo().trim().equalsIgnoreCase("")) && dto2.getVehicleNo() != null
					&& !dto2.getVehicleNo().isEmpty() && !dto2.getVehicleNo().trim().equalsIgnoreCase("")) {

				commonDTO.setApplicationNo(dto2.getApplicationNo());
				commonDTO.setVehicleNo(dto2.getVehicleNo());

			} else if (dto2.getApplicationNo() != null && !dto2.getApplicationNo().isEmpty()
					&& !dto2.getApplicationNo().trim().equalsIgnoreCase("")) {

				commonDTO.setApplicationNo(dto2.getApplicationNo());
				commonDTO.setVehicleNo(null);

			} else if (dto2.getVehicleNo() != null && !dto2.getVehicleNo().isEmpty()
					&& !dto2.getVehicleNo().trim().equalsIgnoreCase("")) {

				commonDTO.setApplicationNo(null);
				commonDTO.setVehicleNo(dto2.getVehicleNo());
			} else {
				commonDTO.setApplicationNo(null);
				commonDTO.setVehicleNo(null);
			}

			inquiryList = new ArrayList<>();
			inquiryList = commonService.getInquiryDetails(commonDTO);

			if (!inquiryList.isEmpty()) {

				disableButton = false;

			} else {
				setErrorMessage("No data found.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

			migratedService.updateStatusOfQueueNumberAfterCallNext(queueNo, "O");
			commonService.updateCounterQueueNo(queueNo, sessionBackingBean.getCounterId());
			migratedService.updateCounterIdOfQueueNumberAfterCallNext(queueNo, sessionBackingBean.getCounterId());

		} else {
			setErrorMessage("Queue No. not found");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void skip() {

		migratedService.updateSkipQueueNumberStatus(queueNo);

		disableCallNext = false;
		disableSkip = true;
		disableButton = true;
		setSuccessMessage("Skipped successfully");
		RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
		commonDTO = new CommonDTO();
		inquiryList.clear();

	}

	public void onCounterSelect() {

		sessionBackingBean.setCounter(commonDTO.getCounter());
		sessionBackingBean.setCounterId(commonDTO.getCounterId());

		commonService.counterStatus(commonDTO.getCounterId(), sessionBackingBean.getLoginUser());
		localcheckcounter = false;
		sessionBackingBean.setCounterCheck(false);
		RequestContext context = RequestContext.getCurrentInstance();

		context.execute("PF('dlg2').hide();");
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		try {
			ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void handleClose() throws InterruptedException {

		try {

			FacesContext.getCurrentInstance().getExternalContext().redirect("/InfoNTC/pages/home/welcomePage.xhtml");

		} catch (IOException e) {
			e.printStackTrace();
		}
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

	public boolean isDisableCallNext() {
		return disableCallNext;
	}

	public void setDisableCallNext(boolean disableCallNext) {
		this.disableCallNext = disableCallNext;
	}

	public boolean isDisableSkip() {
		return disableSkip;
	}

	public void setDisableSkip(boolean disableSkip) {
		this.disableSkip = disableSkip;
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

	public MigratedService getMigratedService() {
		return migratedService;
	}

	public void setMigratedService(MigratedService migratedService) {
		this.migratedService = migratedService;
	}

}
