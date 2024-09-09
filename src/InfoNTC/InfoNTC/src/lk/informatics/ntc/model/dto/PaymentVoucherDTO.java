package lk.informatics.ntc.model.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

public class PaymentVoucherDTO {

	public PaymentVoucherDTO() {
		super();
	}

	public PaymentVoucherDTO(String chargeDescription, String accountNO, BigDecimal amount, String chargeCode) {

		this.chargeDescription = chargeDescription;
		this.amount = amount;
		this.accountNO = accountNO;
		this.chargeCode = chargeCode;
	}

	private String transactionCode;
	private String transactionDescription;
	private String chargeCode;
	private String chargeDescription;
	private String activeStatus;
	private String createBy;
	private Timestamp createDate;
	private Timestamp modifiedDate;
	private String modifiedBy;
	private String descriptionSI;
	private String descriptionTA;
	private Date date;
	private String voucherNo;
	private String permitNo;
	private String applicationNo;
	private BigDecimal amount;
	private String accountNO;

	private String departmentCode;
	private String deaprtmentDiscription;
	private String unitCode;
	private String unitDiscription;
	private String receiptNo;
	private String approveStatus;
	private BigDecimal totalAmount;

	private String paymentDate;
	private String rejectReason;
	private int noOfUnits;
	private String detailsofservice;
	private String speacialRemark;
	private String busRegNo;
	private long taskSeq;
	private String taskCode;
	private String serviceNo;
	private String serviceRefNo;
	
	
	
	
	
	
	

	public String getServiceRefNo() {
		return serviceRefNo;
	}

	public void setServiceRefNo(String serviceRefNo) {
		this.serviceRefNo = serviceRefNo;
	}

	public long getTaskSeq() {
		return taskSeq;
	}

	public void setTaskSeq(long taskSeq) {
		this.taskSeq = taskSeq;
	}

	public String getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}

	public String getBusRegNo() {
		return busRegNo;
	}

	public void setBusRegNo(String busRegNo) {
		this.busRegNo = busRegNo;
	}

	public String getSpeacialRemark() {
		return speacialRemark;
	}

	public void setSpeacialRemark(String speacialRemark) {
		this.speacialRemark = speacialRemark;
	}

	public int getNoOfUnits() {
		return noOfUnits;
	}

	public void setNoOfUnits(int noOfUnits) {
		this.noOfUnits = noOfUnits;
	}

	public String getDetailsofservice() {
		return detailsofservice;
	}

	public void setDetailsofservice(String detailsofservice) {
		this.detailsofservice = detailsofservice;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
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

	public String getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public String getUnitDiscription() {
		return unitDiscription;
	}

	public void setUnitDiscription(String unitDiscription) {
		this.unitDiscription = unitDiscription;
	}

	public String getAccountNO() {
		return accountNO;
	}

	public void setAccountNO(String accountNO) {
		this.accountNO = accountNO;
	}

	public String getDeaprtmentDiscription() {
		return deaprtmentDiscription;
	}

	public void setDeaprtmentDiscription(String deaprtmentDiscription) {
		this.deaprtmentDiscription = deaprtmentDiscription;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getTransactionDescription() {
		return transactionDescription;
	}

	public void setTransactionDescription(String transactionDescription) {
		this.transactionDescription = transactionDescription;
	}

	public String getChargeCode() {
		return chargeCode;
	}

	public void setChargeCode(String chargeCode) {
		this.chargeCode = chargeCode;
	}

	public String getChargeDescription() {
		return chargeDescription;
	}

	public void setChargeDescription(String chargeDescription) {
		this.chargeDescription = chargeDescription;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public String getPermitNo() {
		return permitNo;
	}

	public void setPermitNo(String permitNo) {
		this.permitNo = permitNo;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getDescriptionSI() {
		return descriptionSI;
	}

	public void setDescriptionSI(String descriptionSI) {
		this.descriptionSI = descriptionSI;
	}

	public String getDescriptionTA() {
		return descriptionTA;
	}

	public void setDescriptionTA(String descriptionTA) {
		this.descriptionTA = descriptionTA;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	public String getServiceNo() {
		return serviceNo;
	}

	public void setServiceNo(String serviceNo) {
		this.serviceNo = serviceNo;
	}
	
	

}
