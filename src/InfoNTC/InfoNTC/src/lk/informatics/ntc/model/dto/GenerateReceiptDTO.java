package lk.informatics.ntc.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class GenerateReceiptDTO implements Serializable {
	
	private static final long serialVersionUID = 7354490592862995346L;
	
	private String transactionCode;
	private String transactionDescription;
	private String departmentCode;
	private String departmentDescription;
	private String unitCode;
	private String unitDiscription;
	private String receiptNo;
	private Date searchDate;
	
	private String voucherNo;
	private String permitNo;
	private String applicationNo;
	private String approveStatus;
	private String paymentDate;
	private String paymentType;
	private BigDecimal totalAmount;
	
	private String paymentModeCode;
	private String paymentModeDescription;
	private boolean depositToBank;
	private String bankCode;
	private String bankDescription;
	private String branchCode;
	private String branchDescription;
	private String chequeOrBankReceipt;
	private String remarks;
	private String isReceiptgenerated ="";
	private String rePrintedNo;
	private String queueNo;
	private String branchCodeDescription;
	private String sisuServiceNo;

	
	
	public String getSisuServiceNo() {
		return sisuServiceNo;
	}

	public void setSisuServiceNo(String sisuServiceNo) {
		this.sisuServiceNo = sisuServiceNo;
	}

	public String getIsReceiptgenerated() {
		return isReceiptgenerated;
	}

	public void setIsReceiptgenerated(String isReceiptgenerated) {
		this.isReceiptgenerated = isReceiptgenerated;
	}

	public String getRePrintedNo() {
		return rePrintedNo;
	}

	public void setRePrintedNo(String rePrintedNo) {
		this.rePrintedNo = rePrintedNo;
	}

	public boolean getDepositToBank() {
		return depositToBank;
	}

	public void setDepositToBank(boolean depositToBank) {
		this.depositToBank = depositToBank;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankDescription() {
		return bankDescription;
	}

	public void setBankDescription(String bankDescription) {
		this.bankDescription = bankDescription;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getBranchDescription() {
		return branchDescription;
	}

	public void setBranchDescription(String branchDescription) {
		this.branchDescription = branchDescription;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getPaymentModeCode() {
		return paymentModeCode;
	}

	public void setPaymentModeCode(String paymentModeCode) {
		this.paymentModeCode = paymentModeCode;
	}

	public String getPaymentModeDescription() {
		return paymentModeDescription;
	}

	public void setPaymentModeDescription(String paymentModeDescription) {
		this.paymentModeDescription = paymentModeDescription;
	}

	public String getChequeOrBankReceipt() {
		return chequeOrBankReceipt;
	}

	public void setChequeOrBankReceipt(String chequeOrBankReceipt) {
		this.chequeOrBankReceipt = chequeOrBankReceipt;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}

	public String getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}
	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public String getDepartmentDescription() {
		return departmentDescription;
	}

	public void setDepartmentDescription(String departmentDescription) {
		this.departmentDescription = departmentDescription;
	}

	public String getUnitCode() {
		return unitCode;
	}
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	public String getUnitDiscription() {
		return unitDiscription;
	}
	public void setUnitDiscription(String unitDiscription) {
		this.unitDiscription = unitDiscription;
	}
	public String getReceiptNo() {
		return receiptNo;
	}
	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public Date getSearchDate() {
		return searchDate;
	}

	public void setSearchDate(Date searchDate) {
		this.searchDate = searchDate;
	}

	public String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
	public String getPermitNo() {
		return permitNo;
	}
	public void setPermitNo(String permitNo) {
		this.permitNo = permitNo;
	}
	public String getTransactionCode() {
		return transactionCode;
	}
	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}
	public String getTransactionDescription() {
		return transactionDescription;
	}
	public void setTransactionDescription(String transactionDescription) {
		this.transactionDescription = transactionDescription;
	}
	public String getApplicationNo() {
		return applicationNo;
	}
	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public String getQueueNo() {
		return queueNo;
	}

	public void setQueueNo(String queueNo) {
		this.queueNo = queueNo;
	}

	public String getBranchCodeDescription() {
		return branchCodeDescription;
	}

	public void setBranchCodeDescription(String branchCodeDescription) {
		this.branchCodeDescription = branchCodeDescription;
	}
	
}
