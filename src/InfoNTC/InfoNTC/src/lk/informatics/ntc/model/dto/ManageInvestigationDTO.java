package lk.informatics.ntc.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

public class ManageInvestigationDTO implements Serializable {

	private static final long serialVersionUID = -32784720601071778L;

	private String invReferenceNo;
	private Date startInvestigationDate;
	private Date endInvestigationDate;
	private String chargeRefCode;
	private String chargeRefDesc;
	private String reportNo;
	private String vehicleNo;
	private String permitNo;
	private String permitOwnerName; 
	private String status;
	private String statusDesc;
	private String dateOfInvestigationStr;
	private String offenceName;
	
	private String routeNo;
	private String from;
	private String to;
	private String driverId;
	private String driverName;
	private String conductorId;
	private String conductorName;
	private String serviceType;
	private String serviceTypeDesc;
	private String currentPoints;
	private String finalPoints;
	
	private String chargeCode;
	private String chargeDesc;
	private String noOfAttempts;
	private BigDecimal amount;
	private BigDecimal finalAmount;
	private String specialRemark;
	private String chargeDeterminPoints;
	private String actionTypeCode;
	private String actionTypeDesc;
	private String actionDescription;
	private String actionDeterminPoints;
	
	private String permitDepartment;
	private String permitActionType;
	private String permitActionDesc;
	
	private String driverDepartment;
	private String driverActionType;
	private String driverActionDesc;
	
	private String conductorDepartment;
	private String conductorActionType;
	private String conductorActionDesc;
	
	private String attemptCode;
	private String attemptDesc;
	
	private String documentCode;
	private String documentDesc;
	private Date collectedDate;
	private Date releasedDate;
	private String releaseTo;
	private String idNum;
	
	private String retainDocCode;
	private String retainDocDesc;
	
	private boolean driverApplicable;
	private boolean conductorApplicable;
	private BigDecimal driverPoints;
	private BigDecimal conductorPoints;
	
	private BigDecimal totalDriverPoints;
	private BigDecimal totalConductorPoints;
	private BigDecimal totalDemeritPoints;
	
	private String departmentType;
	
	private String actionCode;
	private String actionDesc;
	
	private String loginUser;
	private String currentStatus;
	private String currentStatusDesc;
	
	private String driverNIC;
	private String conductorNIC;
	
	/** For Receipt **/
	private String voucherNo;
	private String voucherApprovedStatus;
	private String receiptNoForInves;
	private String paymentModeCodeForGrInves;
	private String bankCodeForInves;
	private boolean depositToBankForInves;
	private String branchCodeForInves;
	private String bankDesForInves;
	private String branchDesForInves;
	private String chequeOrBankReceipt;
	private String receiptRemarks;
	private String offenceDescription;
	private String vouAccount;
	private String vouAmmount;
	private BigDecimal totalAmount;
	private BigDecimal latePaymentFee;
	
	private Timestamp createdDate;
	private String createdBy;
	
	private String permitOwnerNic; 
	
	public BigDecimal getLatePaymentFee() {
		return latePaymentFee;
	}
	public void setLatePaymentFee(BigDecimal latePaymentFee) {
		this.latePaymentFee = latePaymentFee;
	}
	public Date getStartInvestigationDate() {
		return startInvestigationDate;
	}
	public void setStartInvestigationDate(Date startInvestigationDate) {
		this.startInvestigationDate = startInvestigationDate;
	}
	public Date getEndInvestigationDate() {
		return endInvestigationDate;
	}
	public void setEndInvestigationDate(Date endInvestigationDate) {
		this.endInvestigationDate = endInvestigationDate;
	}
	public String getChargeRefCode() {
		return chargeRefCode;
	}
	public void setChargeRefCode(String chargeRefCode) {
		this.chargeRefCode = chargeRefCode;
	}
	public String getChargeRefDesc() {
		return chargeRefDesc;
	}
	public void setChargeRefDesc(String chargeRefDesc) {
		this.chargeRefDesc = chargeRefDesc;
	}
	public String getReportNo() {
		return reportNo;
	}
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}
	public String getVehicleNo() {
		return vehicleNo;
	}
	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}
	public String getPermitNo() {
		return permitNo;
	}
	public void setPermitNo(String permitNo) {
		this.permitNo = permitNo;
	}
	public String getPermitOwnerName() {
		return permitOwnerName;
	}
	public void setPermitOwnerName(String permitOwnerName) {
		this.permitOwnerName = permitOwnerName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDateOfInvestigationStr() {
		return dateOfInvestigationStr;
	}
	public void setDateOfInvestigationStr(String dateOfInvestigationStr) {
		this.dateOfInvestigationStr = dateOfInvestigationStr;
	}
	public String getRouteNo() {
		return routeNo;
	}
	public void setRouteNo(String routeNo) {
		this.routeNo = routeNo;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getDriverId() {
		return driverId;
	}
	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getConductorId() {
		return conductorId;
	}
	public void setConductorId(String conductorId) {
		this.conductorId = conductorId;
	}
	public String getConductorName() {
		return conductorName;
	}
	public void setConductorName(String conductorName) {
		this.conductorName = conductorName;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getCurrentPoints() {
		return currentPoints;
	}
	public void setCurrentPoints(String currentPoints) {
		this.currentPoints = currentPoints;
	}
	public String getFinalPoints() {
		return finalPoints;
	}
	public void setFinalPoints(String finalPoints) {
		this.finalPoints = finalPoints;
	}
	public String getChargeCode() {
		return chargeCode;
	}
	public void setChargeCode(String chargeCode) {
		this.chargeCode = chargeCode;
	}
	public String getChargeDesc() {
		return chargeDesc;
	}
	public void setChargeDesc(String chargeDesc) {
		this.chargeDesc = chargeDesc;
	}
	public String getNoOfAttempts() {
		return noOfAttempts;
	}
	public void setNoOfAttempts(String noOfAttempts) {
		this.noOfAttempts = noOfAttempts;
	}
	public String getSpecialRemark() {
		return specialRemark;
	}
	public void setSpecialRemark(String specialRemark) {
		this.specialRemark = specialRemark;
	}
	public String getChargeDeterminPoints() {
		return chargeDeterminPoints;
	}
	public void setChargeDeterminPoints(String chargeDeterminPoints) {
		this.chargeDeterminPoints = chargeDeterminPoints;
	}
	public String getActionTypeCode() {
		return actionTypeCode;
	}
	public void setActionTypeCode(String actionTypeCode) {
		this.actionTypeCode = actionTypeCode;
	}
	public String getActionTypeDesc() {
		return actionTypeDesc;
	}
	public void setActionTypeDesc(String actionTypeDesc) {
		this.actionTypeDesc = actionTypeDesc;
	}
	public String getActionDescription() {
		return actionDescription;
	}
	public void setActionDescription(String actionDescription) {
		this.actionDescription = actionDescription;
	}
	public String getActionDeterminPoints() {
		return actionDeterminPoints;
	}
	public void setActionDeterminPoints(String actionDeterminPoints) {
		this.actionDeterminPoints = actionDeterminPoints;
	}
	public String getDocumentCode() {
		return documentCode;
	}
	public void setDocumentCode(String documentCode) {
		this.documentCode = documentCode;
	}
	public String getDocumentDesc() {
		return documentDesc;
	}
	public void setDocumentDesc(String documentDesc) {
		this.documentDesc = documentDesc;
	}
	public Date getCollectedDate() {
		return collectedDate;
	}
	public void setCollectedDate(Date collectedDate) {
		this.collectedDate = collectedDate;
	}
	public Date getReleasedDate() {
		return releasedDate;
	}
	public void setReleasedDate(Date releasedDate) {
		this.releasedDate = releasedDate;
	}
	public String getReleaseTo() {
		return releaseTo;
	}
	public void setReleaseTo(String releaseTo) {
		this.releaseTo = releaseTo;
	}
	public String getIdNum() {
		return idNum;
	}
	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}
	public String getRetainDocCode() {
		return retainDocCode;
	}
	public void setRetainDocCode(String retainDocCode) {
		this.retainDocCode = retainDocCode;
	}
	public String getRetainDocDesc() {
		return retainDocDesc;
	}
	public void setRetainDocDesc(String retainDocDesc) {
		this.retainDocDesc = retainDocDesc;
	}
	public String getInvReferenceNo() {
		return invReferenceNo;
	}
	public void setInvReferenceNo(String invReferenceNo) {
		this.invReferenceNo = invReferenceNo;
	}
	public String getAttemptCode() {
		return attemptCode;
	}
	public void setAttemptCode(String attemptCode) {
		this.attemptCode = attemptCode;
	}
	public String getAttemptDesc() {
		return attemptDesc;
	}
	public void setAttemptDesc(String attemptDesc) {
		this.attemptDesc = attemptDesc;
	}
	public boolean isDriverApplicable() {
		return driverApplicable;
	}
	public void setDriverApplicable(boolean driverApplicable) {
		this.driverApplicable = driverApplicable;
	}
	public boolean isConductorApplicable() {
		return conductorApplicable;
	}
	public void setConductorApplicable(boolean conductorApplicable) {
		this.conductorApplicable = conductorApplicable;
	}
	public BigDecimal getDriverPoints() {
		return driverPoints;
	}
	public void setDriverPoints(BigDecimal driverPoints) {
		this.driverPoints = driverPoints;
	}
	public BigDecimal getConductorPoints() {
		return conductorPoints;
	}
	public void setConductorPoints(BigDecimal conductorPoints) {
		this.conductorPoints = conductorPoints;
	}
	public String getActionCode() {
		return actionCode;
	}
	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}
	public String getActionDesc() {
		return actionDesc;
	}
	public void setActionDesc(String actionDesc) {
		this.actionDesc = actionDesc;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getFinalAmount() {
		return finalAmount;
	}
	public void setFinalAmount(BigDecimal finalAmount) {
		this.finalAmount = finalAmount;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getLoginUser() {
		return loginUser;
	}
	public void setLoginUser(String loginUser) {
		this.loginUser = loginUser;
	}
	public BigDecimal getTotalDemeritPoints() {
		return totalDemeritPoints;
	}
	public void setTotalDemeritPoints(BigDecimal totalDemeritPoints) {
		this.totalDemeritPoints = totalDemeritPoints;
	}
	public BigDecimal getTotalDriverPoints() {
		return totalDriverPoints;
	}
	public void setTotalDriverPoints(BigDecimal totalDriverPoints) {
		this.totalDriverPoints = totalDriverPoints;
	}
	public BigDecimal getTotalConductorPoints() {
		return totalConductorPoints;
	}
	public void setTotalConductorPoints(BigDecimal totalConductorPoints) {
		this.totalConductorPoints = totalConductorPoints;
	}
	public String getStatusDesc() {
		return statusDesc;
	}
	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}
	public String getCurrentStatus() {
		return currentStatus;
	}
	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}
	public String getDepartmentType() {
		return departmentType;
	}
	public void setDepartmentType(String departmentType) {
		this.departmentType = departmentType;
	}
	public String getPermitDepartment() {
		return permitDepartment;
	}
	public void setPermitDepartment(String permitDepartment) {
		this.permitDepartment = permitDepartment;
	}
	public String getPermitActionType() {
		return permitActionType;
	}
	public void setPermitActionType(String permitActionType) {
		this.permitActionType = permitActionType;
	}
	public String getPermitActionDesc() {
		return permitActionDesc;
	}
	public void setPermitActionDesc(String permitActionDesc) {
		this.permitActionDesc = permitActionDesc;
	}
	public String getDriverDepartment() {
		return driverDepartment;
	}
	public void setDriverDepartment(String driverDepartment) {
		this.driverDepartment = driverDepartment;
	}
	public String getDriverActionType() {
		return driverActionType;
	}
	public void setDriverActionType(String driverActionType) {
		this.driverActionType = driverActionType;
	}
	public String getDriverActionDesc() {
		return driverActionDesc;
	}
	public void setDriverActionDesc(String driverActionDesc) {
		this.driverActionDesc = driverActionDesc;
	}
	public String getConductorDepartment() {
		return conductorDepartment;
	}
	public void setConductorDepartment(String conductorDepartment) {
		this.conductorDepartment = conductorDepartment;
	}
	public String getConductorActionType() {
		return conductorActionType;
	}
	public void setConductorActionType(String conductorActionType) {
		this.conductorActionType = conductorActionType;
	}
	public String getConductorActionDesc() {
		return conductorActionDesc;
	}
	public void setConductorActionDesc(String conductorActionDesc) {
		this.conductorActionDesc = conductorActionDesc;
	}
	public String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
	public String getVoucherApprovedStatus() {
		return voucherApprovedStatus;
	}
	public void setVoucherApprovedStatus(String voucherApprovedStatus) {
		this.voucherApprovedStatus = voucherApprovedStatus;
	}
	public String getReceiptNoForInves() {
		return receiptNoForInves;
	}
	public void setReceiptNoForInves(String receiptNoForInves) {
		this.receiptNoForInves = receiptNoForInves;
	}
	public String getPaymentModeCodeForGrInves() {
		return paymentModeCodeForGrInves;
	}
	public void setPaymentModeCodeForGrInves(String paymentModeCodeForGrInves) {
		this.paymentModeCodeForGrInves = paymentModeCodeForGrInves;
	}
	public String getBankCodeForInves() {
		return bankCodeForInves;
	}
	public void setBankCodeForInves(String bankCodeForInves) {
		this.bankCodeForInves = bankCodeForInves;
	}

	public boolean isDepositToBankForInves() {
		return depositToBankForInves;
	}
	public void setDepositToBankForInves(boolean depositToBankForInves) {
		this.depositToBankForInves = depositToBankForInves;
	}
	public String getBankDesForInves() {
		return bankDesForInves;
	}
	public void setBankDesForInves(String bankDesForInves) {
		this.bankDesForInves = bankDesForInves;
	}
	public String getBranchDesForInves() {
		return branchDesForInves;
	}
	public void setBranchDesForInves(String branchDesForInves) {
		this.branchDesForInves = branchDesForInves;
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
	public String getOffenceDescription() {
		return offenceDescription;
	}
	public void setOffenceDescription(String offenceDescription) {
		this.offenceDescription = offenceDescription;
	}
	public String getVouAccount() {
		return vouAccount;
	}
	public void setVouAccount(String vouAccount) {
		this.vouAccount = vouAccount;
	}
	public String getVouAmmount() {
		return vouAmmount;
	}
	public void setVouAmmount(String vouAmmount) {
		this.vouAmmount = vouAmmount;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getBranchCodeForInves() {
		return branchCodeForInves;
	}
	public void setBranchCodeForInves(String branchCodeForInves) {
		this.branchCodeForInves = branchCodeForInves;
	}
	public String getCurrentStatusDesc() {
		return currentStatusDesc;
	}
	public void setCurrentStatusDesc(String currentStatusDesc) {
		this.currentStatusDesc = currentStatusDesc;
	}
	public String getDriverNIC() {
		return driverNIC;
	}
	public void setDriverNIC(String driverNIC) {
		this.driverNIC = driverNIC;
	}
	public String getConductorNIC() {
		return conductorNIC;
	}
	public void setConductorNIC(String conductorNIC) {
		this.conductorNIC = conductorNIC;
	}
	public String getServiceTypeDesc() {
		return serviceTypeDesc;
	}
	public void setServiceTypeDesc(String serviceTypeDesc) {
		this.serviceTypeDesc = serviceTypeDesc;
	}
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getOffenceName() {
		return offenceName;
	}
	public void setOffenceName(String offenceName) {
		this.offenceName = offenceName;
	}
	public String getPermitOwnerNic() {
		return permitOwnerNic;
	}
	public void setPermitOwnerNic(String permitOwnerNic) {
		this.permitOwnerNic = permitOwnerNic;
	}

	
	
}
