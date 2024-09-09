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

import lk.informatics.ntc.model.dto.DriverConductorRegistrationDTO;
import lk.informatics.ntc.model.dto.PaymentVoucherDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.DriverConductorTrainingService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "driverConductorVoucherApproval")
@ViewScoped
public class DriverConductorVoucherApprovalBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private List<PaymentVoucherDTO> receiptNoList = new ArrayList<PaymentVoucherDTO>(0);

	private String errorMessage, successMessage, rejectReason, advanceSpecialRemark, voucherSpecialRemark, date1,
			alertMSG, InfoMSG;
	private List<PaymentVoucherDTO> paymentList;
	private List<PaymentVoucherDTO> advancePaymentList, voucherPaymentList;
	private PaymentVoucherDTO selectDTO1, viewSelect;
	private boolean disableClear, disableSave1, disableSave2;
	private BigDecimal totalfee;
	private CommonService commonService;
	// driver conductor
	// services
	private DriverConductorTrainingService driverConductorApproveVoucherService;
	// DTO
	private DriverConductorRegistrationDTO driverConducPaymentApprovalDTO;
	private DriverConductorRegistrationDTO driverConducIdDto;
	private DriverConductorRegistrationDTO selectDTO, approveRejectDTO;
	// List
	private List<DriverConductorRegistrationDTO> traniningTypeList = new ArrayList<>();
	private List<DriverConductorRegistrationDTO> appNoList = new ArrayList<>();
	private List<DriverConductorRegistrationDTO> idNoList = new ArrayList<>();
	private List<DriverConductorRegistrationDTO> driverConducIdList = new ArrayList<>();
	private List<DriverConductorRegistrationDTO> voucherNoList = new ArrayList<>();
	private List<DriverConductorRegistrationDTO> paymentListForGrid;
	private boolean disableAppNoList, disableIdNoList, disableDcIdNoList, disableApprovebtn, disableRejectbtn;

	public DriverConductorVoucherApprovalBackingBean() {
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		date1 = dateFormat.format(date);

		// driver conductor

		driverConductorApproveVoucherService = (DriverConductorTrainingService) SpringApplicationContex
				.getBean("driverConductorTrainingService");//
		driverConducPaymentApprovalDTO = new DriverConductorRegistrationDTO();
		traniningTypeList = driverConductorApproveVoucherService.getTrainingTypeList();
		voucherNoList = driverConductorApproveVoucherService.getVoucherNoList();

		disableAppNoList = true;
		disableIdNoList = true;
		disableDcIdNoList = true;
		disableApprovebtn = true;
		disableRejectbtn = true;
		loadValues();
	}

	public void changeOnTrainingType() {
		disableDcIdNoList = true;
		disableIdNoList = true;
		disableAppNoList = false;
		// application numbers get where voucher no and receipt no is not null
		// only for
		// approve process
		appNoList = driverConductorApproveVoucherService
				.getAppNoForApproveByTrainingType(driverConducPaymentApprovalDTO.getTrainingTypeCode());

	}

	public void changeOnAppNo() {
		disableDcIdNoList = true;
		disableIdNoList = false;
		idNoList = driverConductorApproveVoucherService.getIdNoListForSelectedAppNo(
				driverConducPaymentApprovalDTO.getTrainingTypeCode(), driverConducPaymentApprovalDTO.getAppNo());

	}

	public List<DriverConductorRegistrationDTO> getPaymentListForGrid() {
		return paymentListForGrid;
	}

	public void setPaymentListForGrid(List<DriverConductorRegistrationDTO> paymentListForGrid) {
		this.paymentListForGrid = paymentListForGrid;
	}

	public void changeOnIdNo() {
		disableDcIdNoList = false;
		driverConducIdDto = driverConductorApproveVoucherService.getDriverConducByID(
				driverConducPaymentApprovalDTO.getAppNo(), driverConducPaymentApprovalDTO.getNic());
		driverConducPaymentApprovalDTO.setDriverConductorId(driverConducIdDto.getDriverConductorId());

	}

	public void loadValues() {
		paymentListForGrid = new ArrayList<DriverConductorRegistrationDTO>();
		paymentListForGrid = driverConductorApproveVoucherService
				.getPaymentDetailsOnGrid(driverConducPaymentApprovalDTO);
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

		if (driverConducPaymentApprovalDTO.getTrainingTypeCode() != null
				&& !driverConducPaymentApprovalDTO.getTrainingTypeCode().trim().isEmpty()) {
			if (driverConducPaymentApprovalDTO.getAppNo() != null
					&& !driverConducPaymentApprovalDTO.getAppNo().trim().isEmpty()) {
				paymentListForGrid = new ArrayList<DriverConductorRegistrationDTO>();
				paymentListForGrid = driverConductorApproveVoucherService
						.getPaymentDetailsOnGridBySearch(driverConducPaymentApprovalDTO);

				if (paymentListForGrid.isEmpty()) {
					setAlertMSG("No data for searched values.");
					RequestContext.getCurrentInstance().execute("PF('alertMSG').show()");
				} else {
					disableApprovebtn = false;
					disableRejectbtn = false;
					disableClear = false;
				}
			} else {
				setErrorMessage("Please select a Application No.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {
			setErrorMessage("Please select a Training  Type.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void reject() {

		String loginUser = sessionBackingBean.getLoginUser();
		String status = null;
		if (selectDTO != null) {
			approveRejectDTO = driverConductorApproveVoucherService.getVoucherSatus(selectDTO.getVoucher());
			if (approveRejectDTO.getReceiptNo() == null || approveRejectDTO.getReceiptNo().isEmpty()) {

				if (approveRejectDTO.getApproveStatus().equalsIgnoreCase("R")) {

					setErrorMessage("Selected data is already rejected.");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				} else if (approveRejectDTO.getApproveStatus().equalsIgnoreCase("A")) {
					setErrorMessage("Selected data is already approved.");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				} else {
					if (rejectReason == null || rejectReason.isEmpty()) {
						setErrorMessage("Please enter reject reason.");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					} else {
						driverConductorApproveVoucherService.updateApproveRejectVoucher(selectDTO.getVoucher(), "R",
								rejectReason, loginUser);
						loadValues();
						setRejectReason(null);
						setInfoMSG("Success");
						driverConductorApproveVoucherService.beanLinkMethod(driverConducPaymentApprovalDTO, sessionBackingBean.getLoginUser(), "Reject Payment", "Payment Voucher Approval For Driver/Conductor Training");
						setSuccessMessage("Successfully rejected.");
						RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
					}
				}

			} else {
				setErrorMessage("Receipt Already Generated.");
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
		if (selectDTO != null) {
			approveRejectDTO = driverConductorApproveVoucherService.getVoucherSatus(selectDTO.getVoucher());
			if (approveRejectDTO.getReceiptNo() == null || approveRejectDTO.getReceiptNo().isEmpty()) {

				if (approveRejectDTO.getApproveStatus().equalsIgnoreCase("R")) {

					setErrorMessage("Selected data is already rejected.");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				} else if (approveRejectDTO.getApproveStatus().equalsIgnoreCase("A")) {
					setErrorMessage("Selected data is already approved.");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				} else {

					driverConductorApproveVoucherService.updateApproveRejectVoucher(selectDTO.getVoucher(), "A",
							rejectReason, loginUser);
					// update status type as VA

					driverConductorApproveVoucherService.updateStatusType("VA", selectDTO.getAppNo());
					loadValues();
					setRejectReason(null);
					setInfoMSG("Success");
					driverConductorApproveVoucherService.beanLinkMethod(driverConducPaymentApprovalDTO, sessionBackingBean.getLoginUser(), "Approve Payment", "Payment Voucher Approval For Driver/Conductor Training");
					setSuccessMessage("Successfully approved.");
					RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

				}

			} else {
				setErrorMessage("Receipt Already Generated.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

			}

		} else {
			setErrorMessage("Please select a row.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		}

	}

	public void editAction() {

	}

	public void saveSpecialRemark() {

	}

	public void clearOne() {
		driverConducPaymentApprovalDTO = new DriverConductorRegistrationDTO();
	}

	public void clearTwo() {
		driverConducPaymentApprovalDTO = new DriverConductorRegistrationDTO();
		paymentListForGrid = new ArrayList<DriverConductorRegistrationDTO>();
		disableApprovebtn = true;
		disableRejectbtn = true;
		setRejectReason(null);
		loadValues();

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

	public PaymentVoucherDTO getViewSelect() {
		return viewSelect;
	}

	public void setViewSelect(PaymentVoucherDTO viewSelect) {
		this.viewSelect = viewSelect;
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
	// driver conductor

	public DriverConductorRegistrationDTO getDriverConducPaymentApprovalDTO() {
		return driverConducPaymentApprovalDTO;
	}

	public void setDriverConducPaymentApprovalDTO(DriverConductorRegistrationDTO driverConducPaymentApprovalDTO) {
		this.driverConducPaymentApprovalDTO = driverConducPaymentApprovalDTO;
	}

	public DriverConductorTrainingService getDriverConductorApproveVoucherService() {
		return driverConductorApproveVoucherService;
	}

	public void setDriverConductorApproveVoucherService(
			DriverConductorTrainingService driverConductorApproveVoucherService) {
		this.driverConductorApproveVoucherService = driverConductorApproveVoucherService;
	}

	public List<DriverConductorRegistrationDTO> getTraniningTypeList() {
		return traniningTypeList;
	}

	public void setTraniningTypeList(List<DriverConductorRegistrationDTO> traniningTypeList) {
		this.traniningTypeList = traniningTypeList;
	}

	public List<DriverConductorRegistrationDTO> getAppNoList() {
		return appNoList;
	}

	public void setAppNoList(List<DriverConductorRegistrationDTO> appNoList) {
		this.appNoList = appNoList;
	}

	public boolean isDisableAppNoList() {
		return disableAppNoList;
	}

	public void setDisableAppNoList(boolean disableAppNoList) {
		this.disableAppNoList = disableAppNoList;
	}

	public List<DriverConductorRegistrationDTO> getIdNoList() {
		return idNoList;
	}

	public void setIdNoList(List<DriverConductorRegistrationDTO> idNoList) {
		this.idNoList = idNoList;
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

	public List<DriverConductorRegistrationDTO> getDriverConducIdList() {
		return driverConducIdList;
	}

	public void setDriverConducIdList(List<DriverConductorRegistrationDTO> driverConducIdList) {
		this.driverConducIdList = driverConducIdList;
	}

	public List<DriverConductorRegistrationDTO> getVoucherNoList() {
		return voucherNoList;
	}

	public void setVoucherNoList(List<DriverConductorRegistrationDTO> voucherNoList) {
		this.voucherNoList = voucherNoList;
	}

	public DriverConductorRegistrationDTO getDriverConducIdDto() {
		return driverConducIdDto;
	}

	public void setDriverConducIdDto(DriverConductorRegistrationDTO driverConducIdDto) {
		this.driverConducIdDto = driverConducIdDto;
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

	public DriverConductorRegistrationDTO getSelectDTO() {
		return selectDTO;
	}

	public void setSelectDTO(DriverConductorRegistrationDTO selectDTO) {
		this.selectDTO = selectDTO;
	}

}
