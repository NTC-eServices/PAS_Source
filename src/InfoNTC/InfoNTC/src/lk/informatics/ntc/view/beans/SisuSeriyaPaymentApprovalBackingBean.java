package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "sisuSeriyaPaymentApproval")
@ViewScoped
public class SisuSeriyaPaymentApprovalBackingBean {

	// need to change type to Sisuseriya DTO type
	private List<String> transactionTypeList = new ArrayList<String>(0);
	private List<String> permitNoList = new ArrayList<String>(0);
	private List<String> voucherList = new ArrayList<String>(0);
	private List<String> referenceNoList = new ArrayList<String>(0);
	private String rejectReason,alertMSG,successMessage,errorMessage;
	private boolean disabledApprove,disableClear,disableReject;
	
	public SisuSeriyaPaymentApprovalBackingBean() {
		
	}
	
	public void loadValues() {
		
	}
	
	public void selectRow() {
		
	}
	
	public void search() {
		
		
	}
	
	public void approve() {
		
	}
	
	public void reject() {
		
	}
	
	
	public void clearOne() {
		
	}
	
	public void clearTwo() {
		
	}

	public List<String> getTransactionTypeList() {
		return transactionTypeList;
	}

	public void setTransactionTypeList(List<String> transactionTypeList) {
		this.transactionTypeList = transactionTypeList;
	}

	public List<String> getPermitNoList() {
		return permitNoList;
	}

	public void setPermitNoList(List<String> permitNoList) {
		this.permitNoList = permitNoList;
	}

	public List<String> getVoucherList() {
		return voucherList;
	}

	public void setVoucherList(List<String> voucherList) {
		this.voucherList = voucherList;
	}

	public List<String> getReferenceNoList() {
		return referenceNoList;
	}

	public void setReferenceNoList(List<String> referenceNoList) {
		this.referenceNoList = referenceNoList;
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

	public boolean isDisabledApprove() {
		return disabledApprove;
	}

	public void setDisabledApprove(boolean disabledApprove) {
		this.disabledApprove = disabledApprove;
	}

	public boolean isDisableClear() {
		return disableClear;
	}

	public void setDisableClear(boolean disableClear) {
		this.disableClear = disableClear;
	}

	public boolean isDisableReject() {
		return disableReject;
	}

	public void setDisableReject(boolean disableReject) {
		this.disableReject = disableReject;
	}
	
	
	
	
}
