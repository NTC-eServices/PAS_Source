package lk.informatics.ntc.view.beans;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.AdvancedPaymentDTO;
import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.PaymentVoucherDTO;
import lk.informatics.ntc.model.service.EmployeeProfileService;
import lk.informatics.ntc.model.service.PaymentVoucherService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "viewAdvancePaymentVoucherBackingBean")
@ViewScoped
public class ViewAdvancedPaymentBckingBean {
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	public PaymentVoucherService paymentVoucherService;
	public EmployeeProfileService employeeProfileService;
	private List<CommonDTO> transactionTypeList;
	private List<CommonDTO> deparmentList;
	private List<CommonDTO> unitList;
	private PaymentVoucherDTO paymentVoucherDTO = new PaymentVoucherDTO();
	private AdvancedPaymentDTO advancedPaymentDTO = new AdvancedPaymentDTO();
	private List<AdvancedPaymentDTO> paymentDetails;
	private List<PaymentVoucherDTO> voucherDetails;
	private String date1;
	private String errorMsg, alertMsg;
	private String selectingApplicationNo;
	private BigDecimal totalfee;
	private BigDecimal voutotalfee;
	private boolean showInquiryBackButton = false;
	private boolean paymentInquiryMood = true;

	@PostConstruct
	public void init() {

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		setDate1(dateFormat.format(date));
		advancedPaymentDTO = new AdvancedPaymentDTO();
		paymentVoucherService = (PaymentVoucherService) SpringApplicationContex.getBean("paymentVoucherService");
		showInquiryBackButton = false;
		RequestContext.getCurrentInstance().update("btnBack");
		paymentInquiryMood = true;

		if (sessionBackingBean.isClicked == true) {
			paymentInquiryMood = false;
			showInquiryBackButton = true;

			RequestContext.getCurrentInstance().update("cmdbtnSearch");
			RequestContext.getCurrentInstance().update("cmdbtnAddClear");
			RequestContext.getCurrentInstance().update("voucherNo");
			RequestContext.getCurrentInstance().update("btnBack");

			if (sessionBackingBean.getPaymentType().equals("V")) {
				paymentVoucherDTO.setVoucherNo((sessionBackingBean.getVoucherNo()));
				searchVoucherDetails();

			} else if (sessionBackingBean.getPaymentType().equals("AP")) {
				paymentVoucherDTO.setVoucherNo((sessionBackingBean.getVoucherNo()));
				searchDetails();

			} else {

			}

		}

	}

	public void LoadValues() {
		paymentVoucherService = (PaymentVoucherService) SpringApplicationContex.getBean("paymentVoucherService");
		employeeProfileService = (EmployeeProfileService) SpringApplicationContex.getBean("employeeProfileService");
		setTransactionTypeList(paymentVoucherService.GetTransactionToDropdown());
		deparmentList = employeeProfileService.GetDepartmentsToDropdown();
		unitList = employeeProfileService.GetUnitsToDropdown();
	}

	public void backToInquiry() {

		String approveURL = sessionBackingBean.getApproveURL();
		String searchURL = sessionBackingBean.getSearchURL();

		if (approveURL != null) {
			try {
				sessionBackingBean.setApproveURLStatus(false);
				sessionBackingBean.isClicked = false;
				paymentInquiryMood = true;
				FacesContext.getCurrentInstance().getExternalContext().redirect(approveURL);
			} catch (IOException e) {

				e.printStackTrace();
			}

		} else if (searchURL != null) {

			try {
				sessionBackingBean.setSearchURLStatus(false);
				sessionBackingBean.isClicked = false;
				paymentInquiryMood = true;
				FacesContext.getCurrentInstance().getExternalContext().redirect(searchURL);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		setShowInquiryBackButton(false);
		RequestContext.getCurrentInstance().update("btnBack");

	}

	public void completeAppNo() {

		selectingApplicationNo = paymentVoucherService.getAppNoforVoucher(paymentVoucherDTO.getVoucherNo(), "AP");
		if (selectingApplicationNo == null) {

		} else
			paymentVoucherDTO.setApplicationNo(selectingApplicationNo);

	}

	public void completeAppNoforVoucher() {

		selectingApplicationNo = paymentVoucherService.getAppNoforVoucher(paymentVoucherDTO.getVoucherNo(), "V");
		if (selectingApplicationNo == null) {

		} else
			paymentVoucherDTO.setApplicationNo(selectingApplicationNo);

	}

	public void clearMain() {
		advancedPaymentDTO = new AdvancedPaymentDTO();
		paymentVoucherDTO = new PaymentVoucherDTO();
		paymentDetails = null;
		totalfee = null;
	}

	public void clearVoucherMain() {
		paymentVoucherDTO = new PaymentVoucherDTO();
		voucherDetails = null;
		voutotalfee = null;
	}

	public void searchDetails() {
		if (paymentVoucherDTO.getVoucherNo() != null && !paymentVoucherDTO.getVoucherNo().isEmpty()) {
			paymentDetails = paymentVoucherService.getAdvancedPaymentDet(paymentVoucherDTO.getVoucherNo());

			if (paymentDetails.size() > 0) {

				totalfee = paymentVoucherService.getTotAmtforVoucher(paymentVoucherDTO.getVoucherNo());
			} else {
				alertMsg = "Search No. does not have voucher details.";
				sessionBackingBean.showMessage("Alert", alertMsg, "WARNING_DIALOG");
				sessionBackingBean.isClicked = false;
			}

			sessionBackingBean.isClicked = false;

		} else {
			errorMsg = "Voucher No. should be entered.";
			sessionBackingBean.showMessage("Error", errorMsg, "ERROR_DIALOG");
			sessionBackingBean.isClicked = false;
		}
	}

	public void searchVoucherDetails() {
		if (paymentVoucherDTO.getVoucherNo() != null && !paymentVoucherDTO.getVoucherNo().isEmpty()) {
			voucherDetails = paymentVoucherService.getVoucherPaymentDet(paymentVoucherDTO.getVoucherNo());

			if (voucherDetails.size() > 0) {
				voutotalfee = paymentVoucherService.getTotAmtforVoucher(paymentVoucherDTO.getVoucherNo());

			} else {
				alertMsg = "Search No. does not have voucher details.";
				sessionBackingBean.showMessage("Alert", alertMsg, "WARNING_DIALOG");
				sessionBackingBean.isClicked = false;
			}
			sessionBackingBean.isClicked = false;

		} else {
			errorMsg = "Voucher No. should be entered.";
			sessionBackingBean.showMessage("Error", errorMsg, "ERROR_DIALOG");
			sessionBackingBean.isClicked = false;

		}
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public PaymentVoucherService getPaymentVoucherService() {
		return paymentVoucherService;
	}

	public void setPaymentVoucherService(PaymentVoucherService paymentVoucherService) {
		this.paymentVoucherService = paymentVoucherService;
	}

	public EmployeeProfileService getEmployeeProfileService() {
		return employeeProfileService;
	}

	public void setEmployeeProfileService(EmployeeProfileService employeeProfileService) {
		this.employeeProfileService = employeeProfileService;
	}

	public List<CommonDTO> getTransactionTypeList() {
		return transactionTypeList;
	}

	public void setTransactionTypeList(List<CommonDTO> transactionTypeList) {
		this.transactionTypeList = transactionTypeList;
	}

	public List<CommonDTO> getDeparmentList() {
		return deparmentList;
	}

	public void setDeparmentList(List<CommonDTO> deparmentList) {
		this.deparmentList = deparmentList;
	}

	public List<CommonDTO> getUnitList() {
		return unitList;
	}

	public void setUnitList(List<CommonDTO> unitList) {
		this.unitList = unitList;
	}

	public PaymentVoucherDTO getPaymentVoucherDTO() {
		return paymentVoucherDTO;
	}

	public void setPaymentVoucherDTO(PaymentVoucherDTO paymentVoucherDTO) {
		this.paymentVoucherDTO = paymentVoucherDTO;
	}

	public AdvancedPaymentDTO getAdvancedPaymentDTO() {
		return advancedPaymentDTO;
	}

	public void setAdvancedPaymentDTO(AdvancedPaymentDTO advancedPaymentDTO) {
		this.advancedPaymentDTO = advancedPaymentDTO;
	}

	public List<AdvancedPaymentDTO> getPaymentDetails() {
		return paymentDetails;
	}

	public void setPaymentDetails(List<AdvancedPaymentDTO> paymentDetails) {
		this.paymentDetails = paymentDetails;
	}

	public String getSelectingApplicationNo() {
		return selectingApplicationNo;
	}

	public void setSelectingApplicationNo(String selectingApplicationNo) {
		this.selectingApplicationNo = selectingApplicationNo;
	}

	public boolean isPaymentInquiryMood() {
		return paymentInquiryMood;
	}

	public void setPaymentInquiryMood(boolean paymentInquiryMood) {
		this.paymentInquiryMood = paymentInquiryMood;
	}

	public String getDate1() {
		return date1;
	}

	public void setDate1(String date1) {
		this.date1 = date1;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public BigDecimal getTotalfee() {
		return totalfee;
	}

	public void setTotalfee(BigDecimal totalfee) {
		this.totalfee = totalfee;
	}

	public List<PaymentVoucherDTO> getVoucherDetails() {
		return voucherDetails;
	}

	public void setVoucherDetails(List<PaymentVoucherDTO> voucherDetails) {
		this.voucherDetails = voucherDetails;
	}

	public BigDecimal getVoutotalfee() {
		return voutotalfee;
	}

	public void setVoutotalfee(BigDecimal voutotalfee) {
		this.voutotalfee = voutotalfee;
	}

	public boolean isShowInquiryBackButton() {
		return showInquiryBackButton;
	}

	public void setShowInquiryBackButton(boolean showInquiryBackButton) {
		this.showInquiryBackButton = showInquiryBackButton;
	}

}