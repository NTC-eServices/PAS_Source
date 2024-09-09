package lk.informatics.ntc.model.dto;


import java.math.BigDecimal;
import java.util.Date;

public class SimRegistrationDTO {
	
	private String simRegNo;
	private String permitNo;
	private String simNo;
	private String serviceCategory;
	private String BusNo;
	
	private String emiNo;
	private String emiBusNo;
	private String emiStatus;
	private Date emiReIssueDate;
	private String emiReIssueDateString;
	
	private String receiversName;
	private String nicNo;
	private Date issueDate;
	private String issueDateString;
	private Date validUntilDate;
	private String validUntilDateString;
	private Date renewalUntilDate;
	private String paymentReceiptNo;
	private String remarks;
	private String simStatusType;
	
	private Date simCreatedDate;
	private String simCreatedBy;
	
	private Date simModifiedDate;
	private String simModifiedBy;
	
	private String vouChargeType;
	private String vouChargeTypeDetails;
	private String vouAccountNo;
	private Double vouAmmount;
	private Double vouTotalAmount;
	private String vouCreatedBy;
	private Date vouCreatedDate;
	private String vouModifiedBy;
	private Date vouModifiedDate;
	private String vouNo;
	private String receiptNo;
	private String createdDateString;
	private String simRenewalNo;
	private String paymentModeCodeForSimReg;
	private String bankCodeForSimReg;
	private boolean depositToBankForSimReg;
	private String bankDesForSimReg;
	private String branchCodeForSimReg;
	private String branchDesForSimReg;
	private String chequeOrBankReceipt;
	private String receiptRemarks;
    private BigDecimal totalAmount;
    private String issueDateN;
    private String validN;
    private String voucherApprovedStatus;
	private String voucherSeqNo;
	private String vouPrint;
	
    
	public String getSimRegNo() {
		return simRegNo;
	}
	public void setSimRegNo(String simRegNo) {
		this.simRegNo = simRegNo;
	}
	public String getPermitNo() {
		return permitNo;
	}
	public void setPermitNo(String permitNo) {
		this.permitNo = permitNo;
	}
	public String getSimNo() {
		return simNo;
	}
	public void setSimNo(String simNo) {
		this.simNo = simNo;
	}
	public String getServiceCategory() {
		return serviceCategory;
	}
	public void setServiceCategory(String serviceCategory) {
		this.serviceCategory = serviceCategory;
	}
	public String getBusNo() {
		return BusNo;
	}
	public void setBusNo(String busNo) {
		BusNo = busNo;
	}
	public String getReceiversName() {
		return receiversName;
	}
	public void setReceiversName(String receiversName) {
		this.receiversName = receiversName;
	}
	public String getNicNo() {
		return nicNo;
	}
	public void setNicNo(String nicNo) {
		this.nicNo = nicNo;
	}
	public Date getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}
	public Date getValidUntilDate() {
		return validUntilDate;
	}
	public void setValidUntilDate(Date validUntilDate) {
		this.validUntilDate = validUntilDate;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getEmiBusNo() {
		return emiBusNo;
	}
	public void setEmiBusNo(String emiBusNo) {
		this.emiBusNo = emiBusNo;
	}
	public String getEmiStatus() {
		return emiStatus;
	}
	public void setEmiStatus(String emiStatus) {
		this.emiStatus = emiStatus;
	}
	public Date getEmiReIssueDate() {
		return emiReIssueDate;
	}
	public void setEmiReIssueDate(Date emiReIssueDate) {
		this.emiReIssueDate = emiReIssueDate;
	}
	public String getPaymentReceiptNo() {
		return paymentReceiptNo;
	}
	public void setPaymentReceiptNo(String paymentReceiptNo) {
		this.paymentReceiptNo = paymentReceiptNo;
	}
	public Date getSimCreatedDate() {
		return simCreatedDate;
	}
	public void setSimCreatedDate(Date simCreatedDate) {
		this.simCreatedDate = simCreatedDate;
	}
	public String getSimCreatedBy() {
		return simCreatedBy;
	}
	public void setSimCreatedBy(String simCreatedBy) {
		this.simCreatedBy = simCreatedBy;
	}
	public String getEmiReIssueDateString() {
		return emiReIssueDateString;
	}
	public void setEmiReIssueDateString(String emiReIssueDateString) {
		this.emiReIssueDateString = emiReIssueDateString;
	}
	public Date getSimModifiedDate() {
		return simModifiedDate;
	}
	public void setSimModifiedDate(Date simModifiedDate) {
		this.simModifiedDate = simModifiedDate;
	}
	public String getSimModifiedBy() {
		return simModifiedBy;
	}
	public void setSimModifiedBy(String simModifiedBy) {
		this.simModifiedBy = simModifiedBy;
	}
	public String getVouChargeType() {
		return vouChargeType;
	}
	public void setVouChargeType(String vouChargeType) {
		this.vouChargeType = vouChargeType;
	}
	public String getVouAccountNo() {
		return vouAccountNo;
	}
	public void setVouAccountNo(String vouAccountNo) {
		this.vouAccountNo = vouAccountNo;
	}
	public Double getVouAmmount() {
		return vouAmmount;
	}
	public void setVouAmmount(Double vouAmmount) {
		this.vouAmmount = vouAmmount;
	}
	public String getVouNo() {
		return vouNo;
	}
	public void setVouNo(String vouNo) {
		this.vouNo = vouNo;
	}
	public String getVouCreatedBy() {
		return vouCreatedBy;
	}
	public void setVouCreatedBy(String vouCreatedBy) {
		this.vouCreatedBy = vouCreatedBy;
	}
	public Date getVouCreatedDate() {
		return vouCreatedDate;
	}
	public void setVouCreatedDate(Date vouCreatedDate) {
		this.vouCreatedDate = vouCreatedDate;
	}


	public Double getVouTotalAmount() {
		return vouTotalAmount;
	}
	public void setVouTotalAmount(Double vouTotalAmount) {
		this.vouTotalAmount = vouTotalAmount;
	}
	public String getVouChargeTypeDetails() {
		return vouChargeTypeDetails;
	}
	public void setVouChargeTypeDetails(String vouChargeTypeDetails) {
		this.vouChargeTypeDetails = vouChargeTypeDetails;
	}
	public String getReceiptNo() {
		return receiptNo;
	}
	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}
	public String getCreatedDateString() {
		return createdDateString;
	}
	public void setCreatedDateString(String createdDateString) {
		this.createdDateString = createdDateString;
	}
	public String getSimRenewalNo() {
		return simRenewalNo;
	}
	public void setSimRenewalNo(String simRenewalNo) {
		this.simRenewalNo = simRenewalNo;
	}
	public String getVouModifiedBy() {
		return vouModifiedBy;
	}
	public void setVouModifiedBy(String vouModifiedBy) {
		this.vouModifiedBy = vouModifiedBy;
	}
	public Date getVouModifiedDate() {
		return vouModifiedDate;
	}
	public void setVouModifiedDate(Date vouModifiedDate) {
		this.vouModifiedDate = vouModifiedDate;
	}
	public String getEmiNo() {
		return emiNo;
	}
	public void setEmiNo(String emiNo) {
		this.emiNo = emiNo;
	}
	public String getPaymentModeCodeForSimReg() {
		return paymentModeCodeForSimReg;
	}
	public void setPaymentModeCodeForSimReg(String paymentModeCodeForSimReg) {
		this.paymentModeCodeForSimReg = paymentModeCodeForSimReg;
	}
	public String getBankCodeForSimReg() {
		return bankCodeForSimReg;
	}
	public void setBankCodeForSimReg(String bankCodeForSimReg) {
		this.bankCodeForSimReg = bankCodeForSimReg;
	}
	public boolean isDepositToBankForSimReg() {
		return depositToBankForSimReg;
	}
	public void setDepositToBankForSimReg(boolean depositToBankForSimReg) {
		this.depositToBankForSimReg = depositToBankForSimReg;
	}
	public String getBankDesForSimReg() {
		return bankDesForSimReg;
	}
	public void setBankDesForSimReg(String bankDesForSimReg) {
		this.bankDesForSimReg = bankDesForSimReg;
	}
	public String getBranchCodeForSimReg() {
		return branchCodeForSimReg;
	}
	public void setBranchCodeForSimReg(String branchCodeForSimReg) {
		this.branchCodeForSimReg = branchCodeForSimReg;
	}
	public String getBranchDesForSimReg() {
		return branchDesForSimReg;
	}
	public void setBranchDesForSimReg(String branchDesForSimReg) {
		this.branchDesForSimReg = branchDesForSimReg;
	}
	public String getChequeOrBankReceipt() {
		return chequeOrBankReceipt;
	}
	public void setChequeOrBankReceipt(String chequeOrBankReceipt) {
		this.chequeOrBankReceipt = chequeOrBankReceipt;
	}
	public String getReceiptRemarks() {
		return receiptRemarks;
	}
	public void setReceiptRemarks(String receiptRemarks) {
		this.receiptRemarks = receiptRemarks;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getIssueDateN() {
		return issueDateN;
	}
	public void setIssueDateN(String issueDateN) {
		this.issueDateN = issueDateN;
	}
	public String getValidN() {
		return validN;
	}
	public void setValidN(String validN) {
		this.validN = validN;
	}
	public String getVoucherApprovedStatus() {
		return voucherApprovedStatus;
	}
	public void setVoucherApprovedStatus(String voucherApprovedStatus) {
		this.voucherApprovedStatus = voucherApprovedStatus;
	}
	public String getVoucherSeqNo() {
		return voucherSeqNo;
	}
	public void setVoucherSeqNo(String voucherSeqNo) {
		this.voucherSeqNo = voucherSeqNo;
	}
	public Date getRenewalUntilDate() {
		return renewalUntilDate;
	}
	public void setRenewalUntilDate(Date renewalUntilDate) {
		this.renewalUntilDate = renewalUntilDate;
	}
	public String getSimStatusType() {
		return simStatusType;
	}
	public void setSimStatusType(String simStatusType) {
		this.simStatusType = simStatusType;
	}
	public String getVouPrint() {
		return vouPrint;
	}
	public void setVouPrint(String vouPrint) {
		this.vouPrint = vouPrint;
	}
	public String getIssueDateString() {
		return issueDateString;
	}
	public void setIssueDateString(String issueDateString) {
		this.issueDateString = issueDateString;
	}
	public String getValidUntilDateString() {
		return validUntilDateString;
	}
	public void setValidUntilDateString(String validUntilDateString) {
		this.validUntilDateString = validUntilDateString;
	}




}
