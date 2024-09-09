package lk.informatics.ntc.view.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.PaymentVoucherDTO;
import lk.informatics.ntc.model.dto.SimRegistrationDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.ManageInquiryService;
import lk.informatics.ntc.model.service.SimRegistrationService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "simRegistrationVoucherApproval")
@ViewScoped
public class SimRegistrationVoucherApprovalBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private List<PaymentVoucherDTO> receiptNoList = new ArrayList<PaymentVoucherDTO>(0);

	private String errorMessage, successMessage, rejectReason, advanceSpecialRemark, voucherSpecialRemark, date1,
			alertMSG, InfoMSG;
	private List<PaymentVoucherDTO> paymentList;
	private List<PaymentVoucherDTO> advancePaymentList, voucherPaymentList;
	private PaymentVoucherDTO selectDTO1;
	private boolean disableClear, disableSave1, disableSave2;
	private BigDecimal totalfee;
	private CommonService commonService;

	// services
	private SimRegistrationService simRegistrationService;
	private ManageInquiryService manageInquiryService;
	// DTO

	private SimRegistrationDTO simregDTO;
	private SimRegistrationDTO simSelectDTO, approveRejectDTO;
	private SimRegistrationDTO filterDataDTO;
	private SimRegistrationDTO viewSelect;
	private List<SimRegistrationDTO> simRegNoList = new ArrayList<>();
	private List<SimRegistrationDTO> dataGridList = new ArrayList<>();

	// List

	private boolean disableAppNoList, disableIdNoList, disableDcIdNoList, disableApprovebtn, disableRejectbtn;
	private List<SimRegistrationDTO> emiDetViewList = new ArrayList<>();

	public SimRegistrationVoucherApprovalBackingBean() {
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");

		simRegistrationService = (SimRegistrationService) SpringApplicationContex.getBean("simRegistrationService");
		manageInquiryService = (ManageInquiryService) SpringApplicationContex.getBean("manageInquiryService");
		simregDTO = new SimRegistrationDTO();

		simRegNoList = simRegistrationService.simRegNoList();
		dataGridList = simRegistrationService.searchedList(null, null, null, null, null, null, "P");

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		date1 = dateFormat.format(date);

		disableAppNoList = true;
		disableIdNoList = true;
		disableDcIdNoList = true;
		disableApprovebtn = true;
		disableRejectbtn = true;

	}

	public void filterDataBySimReg() {
		filterDataDTO = simRegistrationService.filterDTO(simregDTO.getSimRegNo(), null, null, null, null, null);
		simregDTO.setSimNo(filterDataDTO.getSimNo());
		simregDTO.setEmiBusNo(filterDataDTO.getEmiBusNo());
		simregDTO.setPermitNo(filterDataDTO.getPermitNo());
		simregDTO.setVouNo(filterDataDTO.getVouNo());
	}

	public void filterDataBySimNo() {
		filterDataDTO = simRegistrationService.filterDTO(null, simregDTO.getSimNo(), null, null, null, null);
		simregDTO.setSimRegNo(filterDataDTO.getSimRegNo());
		simregDTO.setEmiBusNo(filterDataDTO.getEmiBusNo());
		simregDTO.setPermitNo(filterDataDTO.getPermitNo());
		simregDTO.setVouNo(filterDataDTO.getVouNo());
	}

	public void filterDataByBusNo() {
		filterDataDTO = simRegistrationService.filterDTO(null, null, null, simregDTO.getEmiBusNo(), null, null);
		simregDTO.setSimRegNo(filterDataDTO.getSimRegNo());
		simregDTO.setSimNo(filterDataDTO.getSimNo());
		simregDTO.setPermitNo(filterDataDTO.getPermitNo());
		simregDTO.setVouNo(filterDataDTO.getVouNo());
	}

	public void filterDataPermitNo() {
		filterDataDTO = simRegistrationService.filterDTO(null, null, null, null, simregDTO.getPermitNo(), null);
		simregDTO.setSimRegNo(filterDataDTO.getSimRegNo());
		simregDTO.setSimNo(filterDataDTO.getSimNo());
		simregDTO.setEmiBusNo(filterDataDTO.getEmiBusNo());
		simregDTO.setVouNo(filterDataDTO.getVouNo());
	}

	public void filterDataByVoucher() {
		filterDataDTO = simRegistrationService.filterDTO(null, null, null, null, null, simregDTO.getVouNo());
		simregDTO.setSimRegNo(filterDataDTO.getSimRegNo());
		simregDTO.setSimNo(filterDataDTO.getSimNo());
		simregDTO.setEmiBusNo(filterDataDTO.getEmiBusNo());
		simregDTO.setEmiBusNo(filterDataDTO.getEmiBusNo());
	}

	public void unselectRow() {
		RequestContext.getCurrentInstance().update("dataTable");
		RequestContext.getCurrentInstance().reset("dataTable");
	}

	public void selectRow() {

		String loginUser = sessionBackingBean.getLoginUser();

		disableApprovebtn = false;
		disableRejectbtn = false;

	}

	public void search() {

		if (simregDTO.getSimRegNo() != null && !simregDTO.getSimRegNo().trim().isEmpty()) {
			if (simregDTO.getSimNo() != null && !simregDTO.getSimNo().trim().isEmpty()) {
				dataGridList = new ArrayList<SimRegistrationDTO>();
				dataGridList = simRegistrationService.searchedList(simregDTO.getSimRegNo(), simregDTO.getSimNo(),
						simregDTO.getSimRenewalNo(), simregDTO.getEmiBusNo(), simregDTO.getPermitNo(),
						simregDTO.getVouNo(), null);

				if (dataGridList.isEmpty()) {
					setAlertMSG("No data for searched values.");
					RequestContext.getCurrentInstance().execute("PF('alertMSG').show()");
				} else {

					disableClear = false;
				}
			} else {
				setErrorMessage("Please select data for search");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {
			setErrorMessage("Please select data for search");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void reject() {

		String loginUser = sessionBackingBean.getLoginUser();
		String status = null;
		if (simSelectDTO != null) {
			approveRejectDTO = simRegistrationService.filterDTO(simSelectDTO.getSimRegNo(), simSelectDTO.getSimNo(),
					simSelectDTO.getSimRenewalNo(), simSelectDTO.getBusNo(), simSelectDTO.getPermitNo(),
					simSelectDTO.getVouNo());
			if (approveRejectDTO.getReceiptNo() == null || approveRejectDTO.getReceiptNo().isEmpty()) {

				if (approveRejectDTO.getEmiStatus().equalsIgnoreCase("Rejected")) {

					setErrorMessage("Selected data is already rejected.");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

				else {
					if (rejectReason == null || rejectReason.isEmpty()) {
						setErrorMessage("Please enter reject reason.");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					} else {
						simRegistrationService.updateApproveRejectVoucher(simSelectDTO.getVouNo(), "R", rejectReason,
								loginUser);

						setInfoMSG("Success");
						setSuccessMessage("Successfully rejected.");
						RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
					}
				}

			} else {
				setErrorMessage("Receipt already generated.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

			}

		} else {
			setErrorMessage("Please select a row.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		}

	}

	public void approvePayment() {

		String loginUser = sessionBackingBean.getLoginUser();
		String status = null;
		if (simSelectDTO != null) {
			approveRejectDTO = simRegistrationService.filterDTO(simSelectDTO.getSimRegNo(), simSelectDTO.getSimNo(),
					simSelectDTO.getSimRenewalNo(), simSelectDTO.getBusNo(), simSelectDTO.getPermitNo(),
					simSelectDTO.getVouNo());
			if (approveRejectDTO.getReceiptNo() == null || approveRejectDTO.getReceiptNo().isEmpty()) {

				if (approveRejectDTO.getEmiStatus().equalsIgnoreCase("Rejected")) {

					setErrorMessage("Selected data is already rejected.");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				} else if (approveRejectDTO.getEmiStatus().equalsIgnoreCase("Approved")) {
					setErrorMessage("Selected data is already approved.");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				} else {

					simRegistrationService.updateApproveRejectVoucher(simSelectDTO.getVouNo(), "A", rejectReason,
							loginUser);
					// update status type as VA

					simRegistrationService.updateStatusType("VA", simSelectDTO.getSimRegNo());

					setInfoMSG("Success");
					setSuccessMessage("Successfully approved.");
					RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

				}

			} else {
				setErrorMessage("Receipt already generated.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

			}

		} else {
			setErrorMessage("Please select a row.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		}

	}

	public void clearSearchingData() {

		simregDTO.setSimRegNo(null);
		simregDTO.setSimNo(null);
		simregDTO.setEmiBusNo(null);
		simregDTO.setPermitNo(null);
		simregDTO.setSimRenewalNo(null);
		simregDTO.setVouNo(null);
		dataGridList = simRegistrationService.searchedList(null, null, null, null, null, null, "P");

	}

	public void clearTwo() {
		clearSearchingData();
		disableApprovebtn = true;
		disableRejectbtn = true;
		setRejectReason(null);
		// unselectRow() ;

	}

	public void viewAction() {
		emiDetViewList = manageInquiryService.getEmiDetails(viewSelect.getSimRegNo());
		RequestContext.getCurrentInstance().update("dialogSimReg");
		RequestContext.getCurrentInstance().execute("PF('dialogForSimReg').show()");

	}

	public List<PaymentVoucherDTO> getPaymentList() {
		return paymentList;
	}

	public List<PaymentVoucherDTO> getVoucherPaymentList() {
		return voucherPaymentList;
	}

	public void setVoucherPaymentList(List<PaymentVoucherDTO> voucherPaymentList) {
		this.voucherPaymentList = voucherPaymentList;
	}

	public List<PaymentVoucherDTO> getAdvancePaymentList() {
		return advancePaymentList;
	}

	public void setAdvancePaymentList(List<PaymentVoucherDTO> advancePaymentList) {
		this.advancePaymentList = advancePaymentList;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public String getAdvanceSpecialRemark() {
		return advanceSpecialRemark;
	}

	public String getDate1() {
		return date1;
	}

	public void setDate1(String date1) {
		this.date1 = date1;
	}

	public void setAdvanceSpecialRemark(String advanceSpecialRemark) {
		this.advanceSpecialRemark = advanceSpecialRemark;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public void setPaymentList(List<PaymentVoucherDTO> paymentList) {
		this.paymentList = paymentList;
	}

	public boolean isDisableClear() {
		return disableClear;
	}

	public void setDisableClear(boolean disableClear) {
		this.disableClear = disableClear;
	}

	public boolean isDisableSave2() {
		return disableSave2;
	}

	public void setDisableSave2(boolean disableSave2) {
		this.disableSave2 = disableSave2;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public String getInfoMSG() {
		return InfoMSG;
	}

	public void setInfoMSG(String infoMSG) {
		InfoMSG = infoMSG;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
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

	public List<PaymentVoucherDTO> getReceiptNoList() {
		return receiptNoList;
	}

	public void setReceiptNoList(List<PaymentVoucherDTO> receiptNoList) {
		this.receiptNoList = receiptNoList;
	}

	public String getVoucherSpecialRemark() {
		return voucherSpecialRemark;
	}

	public void setVoucherSpecialRemark(String voucherSpecialRemark) {
		this.voucherSpecialRemark = voucherSpecialRemark;
	}

	public BigDecimal getTotalfee() {
		return totalfee;
	}

	public boolean isDisableSave1() {
		return disableSave1;
	}

	public void setDisableSave1(boolean disableSave1) {
		this.disableSave1 = disableSave1;
	}

	public String getAlertMSG() {
		return alertMSG;
	}

	public void setAlertMSG(String alertMSG) {
		this.alertMSG = alertMSG;
	}

	public void setTotalfee(BigDecimal totalfee) {
		this.totalfee = totalfee;
	}

	public boolean isDisableAppNoList() {
		return disableAppNoList;
	}

	public void setDisableAppNoList(boolean disableAppNoList) {
		this.disableAppNoList = disableAppNoList;
	}

	public boolean isDisableIdNoList() {
		return disableIdNoList;
	}

	public void setDisableIdNoList(boolean disableIdNoList) {
		this.disableIdNoList = disableIdNoList;
	}

	public boolean isDisableDcIdNoList() {
		return disableDcIdNoList;
	}

	public void setDisableDcIdNoList(boolean disableDcIdNoList) {
		this.disableDcIdNoList = disableDcIdNoList;
	}

	public boolean isDisableApprovebtn() {
		return disableApprovebtn;
	}

	public void setDisableApprovebtn(boolean disableApprovebtn) {
		this.disableApprovebtn = disableApprovebtn;
	}

	public boolean isDisableRejectbtn() {
		return disableRejectbtn;
	}

	public void setDisableRejectbtn(boolean disableRejectbtn) {
		this.disableRejectbtn = disableRejectbtn;
	}

	public SimRegistrationService getSimRegistrationService() {
		return simRegistrationService;
	}

	public void setSimRegistrationService(SimRegistrationService simRegistrationService) {
		this.simRegistrationService = simRegistrationService;
	}

	public SimRegistrationDTO getSimregDTO() {
		return simregDTO;
	}

	public void setSimregDTO(SimRegistrationDTO simregDTO) {
		this.simregDTO = simregDTO;
	}

	public List<SimRegistrationDTO> getSimRegNoList() {
		return simRegNoList;
	}

	public void setSimRegNoList(List<SimRegistrationDTO> simRegNoList) {
		this.simRegNoList = simRegNoList;
	}

	public List<SimRegistrationDTO> getDataGridList() {
		return dataGridList;
	}

	public void setDataGridList(List<SimRegistrationDTO> dataGridList) {
		this.dataGridList = dataGridList;
	}

	public SimRegistrationDTO getSimSelectDTO() {
		return simSelectDTO;
	}

	public void setSimSelectDTO(SimRegistrationDTO simSelectDTO) {
		this.simSelectDTO = simSelectDTO;
	}

	public SimRegistrationDTO getApproveRejectDTO() {
		return approveRejectDTO;
	}

	public void setApproveRejectDTO(SimRegistrationDTO approveRejectDTO) {
		this.approveRejectDTO = approveRejectDTO;
	}

	public SimRegistrationDTO getFilterDataDTO() {
		return filterDataDTO;
	}

	public void setFilterDataDTO(SimRegistrationDTO filterDataDTO) {
		this.filterDataDTO = filterDataDTO;
	}

	public SimRegistrationDTO getViewSelect() {
		return viewSelect;
	}

	public void setViewSelect(SimRegistrationDTO viewSelect) {
		this.viewSelect = viewSelect;
	}

	public List<SimRegistrationDTO> getEmiDetViewList() {
		return emiDetViewList;
	}

	public void setEmiDetViewList(List<SimRegistrationDTO> emiDetViewList) {
		this.emiDetViewList = emiDetViewList;
	}

	public ManageInquiryService getManageInquiryService() {
		return manageInquiryService;
	}

	public void setManageInquiryService(ManageInquiryService manageInquiryService) {
		this.manageInquiryService = manageInquiryService;
	}

}
