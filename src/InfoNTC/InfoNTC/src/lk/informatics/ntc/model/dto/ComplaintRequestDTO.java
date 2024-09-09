package lk.informatics.ntc.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ComplaintRequestDTO implements Serializable {

	private static final long serialVersionUID = 568345455L;

	private Long complainSeq;
	private String complaintNo;
	private String complainTypeCode;
	private String complainMedia;
	private String priorityOrder;
	private String severityNo;
	private String eventPlace;
	private String eventDateTime;
	private String complainerName;
	private String complainerName_si;
	private String complainerName_ta;
	private String address1;
	private String address1_si;
	private String address1_ta;
	private String address2;
	private String address2_si;
	private String address2_ta;
	private String city;
	private String city_si;
	private String city_ta;
	private String contact1;
	private String contact1_si;
	private String contact1_ta;
	private String contact2;
	private String contact2_si;
	private String contact2_ta;
	private boolean complainerParticipation;
	private boolean hasWrittenProof;
	private String otherOffence;
	private String detailOfComplain;
	private List<CommittedOffencesDTO> committedOffences;
	private Date investigationDate;
	private String processStatus;

	private String permitAuthority;
	private String permitNo;
	private String vehicleNo;
	private String routeNo;
	private String origin;
	private String destination;
	private String depot;
	private String province;
	private String province_des;
	private String ownerNic;
	private String ownerName;

	private Long actionSeq;
	private String actionCode;
	private String departmentType;
	private String actionDesc;

	private BigDecimal totalCharge;
	private String voucherNo;
	private String voucherApprovedStatus;
	private String userId;
	private String receiptNoForGr;
	private String paymentModeCodeForGr;
	private String bankCodeForGr;
	private String bankDesForGr;
	private String branchCodeForGr;
	private String branchDesForGr;
	private boolean depositToBankForGr;
	private String chequeOrBankReceipt;
	private String offenceCode;
	private String offenceDes;
	private String accountNO;
	private BigDecimal ammount;
	private String receiptRemarks;
	private BigDecimal totalAmount;
	private java.sql.Timestamp createdDate;
	/** added for show public complain button in view permit renewal page **/
	private String process_status_des;
	/***
	 * added for shoe committed offense in initiate complain request view Edit Page
	 ***/
	private boolean applicableYes;
	private String offence;
	private String remarks;
	private String serviceTypeDescription;

	private String recommendRemark;
	private String recommended;
	private String approvedRemark;
	private String approved;

	public String getComplaintNo() {
		return complaintNo;
	}

	public void setComplaintNo(String complaintNo) {
		this.complaintNo = complaintNo;
	}

	public String getComplainTypeCode() {
		return complainTypeCode;
	}

	public void setComplainTypeCode(String complainTypeCode) {
		this.complainTypeCode = complainTypeCode;
	}

	public String getChequeOrBankReceipt() {
		return chequeOrBankReceipt;
	}

	public void setChequeOrBankReceipt(String chequeOrBankReceipt) {
		this.chequeOrBankReceipt = chequeOrBankReceipt;
	}

	public String getComplainMedia() {
		return complainMedia;
	}

	public void setComplainMedia(String complainMedia) {
		this.complainMedia = complainMedia;
	}

	public String getPriorityOrder() {
		return priorityOrder;
	}

	public void setPriorityOrder(String priorityOrder) {
		this.priorityOrder = priorityOrder;
	}

	public String getSeverityNo() {
		return severityNo;
	}

	public void setSeverityNo(String severityNo) {
		this.severityNo = severityNo;
	}

	public String getEventPlace() {
		return eventPlace;
	}

	public void setEventPlace(String eventPlace) {
		this.eventPlace = eventPlace;
	}

	public String getEventDateTime() {
		return eventDateTime;
	}

	public void setEventDateTime(String eventDateTime) {
		this.eventDateTime = eventDateTime;
	}

	public String getComplainerName() {
		return complainerName;
	}

	public void setComplainerName(String complainerName) {
		this.complainerName = complainerName;
	}

	public String getComplainerName_si() {
		return complainerName_si;
	}

	public void setComplainerName_si(String complainerName_si) {
		this.complainerName_si = complainerName_si;
	}

	public String getComplainerName_ta() {
		return complainerName_ta;
	}

	public void setComplainerName_ta(String complainerName_ta) {
		this.complainerName_ta = complainerName_ta;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress1_si() {
		return address1_si;
	}

	public void setAddress1_si(String address1_si) {
		this.address1_si = address1_si;
	}

	public String getAddress1_ta() {
		return address1_ta;
	}

	public void setAddress1_ta(String address1_ta) {
		this.address1_ta = address1_ta;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAddress2_si() {
		return address2_si;
	}

	public void setAddress2_si(String address2_si) {
		this.address2_si = address2_si;
	}

	public String getAddress2_ta() {
		return address2_ta;
	}

	public void setAddress2_ta(String address2_ta) {
		this.address2_ta = address2_ta;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCity_si() {
		return city_si;
	}

	public void setCity_si(String city_si) {
		this.city_si = city_si;
	}

	public String getCity_ta() {
		return city_ta;
	}

	public void setCity_ta(String city_ta) {
		this.city_ta = city_ta;
	}

	public String getContact1() {
		return contact1;
	}

	public void setContact1(String contact1) {
		this.contact1 = contact1;
	}

	public String getContact2() {
		return contact2;
	}

	public void setContact2(String contact2) {
		this.contact2 = contact2;
	}

	public boolean isComplainerParticipation() {
		return complainerParticipation;
	}

	public void setComplainerParticipation(boolean complainerParticipation) {
		this.complainerParticipation = complainerParticipation;
	}

	public boolean isHasWrittenProof() {
		return hasWrittenProof;
	}

	public void setHasWrittenProof(boolean hasWrittenProof) {
		this.hasWrittenProof = hasWrittenProof;
	}

	public String getOtherOffence() {
		return otherOffence;
	}

	public void setOtherOffence(String otherOffence) {
		this.otherOffence = otherOffence;
	}

	public String getDetailOfComplain() {
		return detailOfComplain;
	}

	public void setDetailOfComplain(String detailOfComplain) {
		this.detailOfComplain = detailOfComplain;
	}

	public String getPermitAuthority() {
		return permitAuthority;
	}

	public void setPermitAuthority(String permitAuthority) {
		this.permitAuthority = permitAuthority;
	}

	public String getPermitNo() {
		return permitNo;
	}

	public void setPermitNo(String permitNo) {
		this.permitNo = permitNo;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public String getRouteNo() {
		return routeNo;
	}

	public void setRouteNo(String routeNo) {
		this.routeNo = routeNo;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDepot() {
		return depot;
	}

	public void setDepot(String depot) {
		this.depot = depot;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public List<CommittedOffencesDTO> getCommittedOffences() {
		return committedOffences;
	}

	public void setCommittedOffences(List<CommittedOffencesDTO> committedOffences) {
		this.committedOffences = committedOffences;
	}

	public Long getComplainSeq() {
		return complainSeq;
	}

	public void setComplainSeq(Long complainSeq) {
		this.complainSeq = complainSeq;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getContact1_si() {
		return contact1_si;
	}

	public void setContact1_si(String contact1_si) {
		this.contact1_si = contact1_si;
	}

	public String getContact1_ta() {
		return contact1_ta;
	}

	public void setContact1_ta(String contact1_ta) {
		this.contact1_ta = contact1_ta;
	}

	public String getContact2_si() {
		return contact2_si;
	}

	public void setContact2_si(String contact2_si) {
		this.contact2_si = contact2_si;
	}

	public String getContact2_ta() {
		return contact2_ta;
	}

	public void setContact2_ta(String contact2_ta) {
		this.contact2_ta = contact2_ta;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

//	public String getDriverId() {
//		return driverId;
//	}
//	public void setDriverId(String driverId) {
//		this.driverId = driverId;
//	}
//	public String getDriverNIC() {
//		return driverNIC;
//	}
//	public void setDriverNIC(String driverNIC) {
//		this.driverNIC = driverNIC;
//	}
//	public String getDriverName() {
//		return driverName;
//	}
//	public void setDriverName(String driverName) {
//		this.driverName = driverName;
//	}
//	public String getDriverAddress1() {
//		return driverAddress1;
//	}
//	public void setDriverAddress1(String driverAddress1) {
//		this.driverAddress1 = driverAddress1;
//	}
//	public String getDriverAddress2() {
//		return driverAddress2;
//	}
//	public void setDriverAddress2(String driverAddress2) {
//		this.driverAddress2 = driverAddress2;
//	}
//	public String getDriverAddress3() {
//		return driverAddress3;
//	}
//	public void setDriverAddress3(String driverAddress3) {
//		this.driverAddress3 = driverAddress3;
//	}
//	public String getConductorId() {
//		return conductorId;
//	}
//	public void setConductorId(String conductorId) {
//		this.conductorId = conductorId;
//	}
//	public String getConductorNIC() {
//		return conductorNIC;
//	}
//	public void setConductorNIC(String conductorNIC) {
//		this.conductorNIC = conductorNIC;
//	}
//	public String getConductorName() {
//		return conductorName;
//	}
//	public void setConductorName(String conductorName) {
//		this.conductorName = conductorName;
//	}
//	public String getConductorAddress1() {
//		return conductorAddress1;
//	}
//	public void setConductorAddress1(String conductorAddress1) {
//		this.conductorAddress1 = conductorAddress1;
//	}
//	public String getConductorAddress2() {
//		return conductorAddress2;
//	}
//	public void setConductorAddress2(String conductorAddress2) {
//		this.conductorAddress2 = conductorAddress2;
//	}
//	public String getConductorAddress3() {
//		return conductorAddress3;
//	}
//	public void setConductorAddress3(String conductorAddress3) {
//		this.conductorAddress3 = conductorAddress3;
//	}
	public Date getInvestigationDate() {
		return investigationDate;
	}

	public void setInvestigationDate(Date investigationDate) {
		this.investigationDate = investigationDate;
	}

	public Long getActionSeq() {
		return actionSeq;
	}

	public void setActionSeq(Long actionSeq) {
		this.actionSeq = actionSeq;
	}

	public String getActionCode() {
		return actionCode;
	}

	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}

	public String getDepartmentType() {
		return departmentType;
	}

	public void setDepartmentType(String departmentType) {
		this.departmentType = departmentType;
	}

	public String getActionDesc() {
		return actionDesc;
	}

	public void setActionDesc(String actionDesc) {
		this.actionDesc = actionDesc;
	}

	public String getOwnerNic() {
		return ownerNic;
	}

	public void setOwnerNic(String ownerNic) {
		this.ownerNic = ownerNic;
	}

	public BigDecimal getTotalCharge() {
		return totalCharge;
	}

	public void setTotalCharge(BigDecimal totalCharge) {
		this.totalCharge = totalCharge;
	}

	public String getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getReceiptNoForGr() {
		return receiptNoForGr;
	}

	public void setReceiptNoForGr(String receiptNoForGr) {
		this.receiptNoForGr = receiptNoForGr;
	}

	public String getPaymentModeCodeForGr() {
		return paymentModeCodeForGr;
	}

	public void setPaymentModeCodeForGr(String paymentModeCodeForGr) {
		this.paymentModeCodeForGr = paymentModeCodeForGr;
	}

	public String getBankCodeForGr() {
		return bankCodeForGr;
	}

	public void setBankCodeForGr(String bankCodeForGr) {
		this.bankCodeForGr = bankCodeForGr;
	}

	public String getBankDesForGr() {
		return bankDesForGr;
	}

	public void setBankDesForGr(String bankDesForGr) {
		this.bankDesForGr = bankDesForGr;
	}

	public String getBranchCodeForGr() {
		return branchCodeForGr;
	}

	public void setBranchCodeForGr(String branchCodeForGr) {
		this.branchCodeForGr = branchCodeForGr;
	}

	public String getBranchDesForGr() {
		return branchDesForGr;
	}

	public void setBranchDesForGr(String branchDesForGr) {
		this.branchDesForGr = branchDesForGr;
	}

	public boolean getDepositToBankForGr() {
		return depositToBankForGr;
	}

	public void setDepositToBankForDC(boolean depositToBankForGr) {
		this.depositToBankForGr = depositToBankForGr;
	}

	public String getOffenceCode() {
		return offenceCode;
	}

	public void setOffenceCode(String offenceCode) {
		this.offenceCode = offenceCode;
	}

	public String getOffenceDes() {
		return offenceDes;
	}

	public void setOffenceDes(String offenceDes) {
		this.offenceDes = offenceDes;
	}

	public String getAccountNO() {
		return accountNO;
	}

	public void setAccountNO(String accountNO) {
		this.accountNO = accountNO;
	}

	public BigDecimal getAmmount() {
		return ammount;
	}

	public void setAmmount(BigDecimal ammount) {
		this.ammount = ammount;
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

	public void setDepositToBankForGr(boolean depositToBankForGr) {
		this.depositToBankForGr = depositToBankForGr;
	}

	public java.sql.Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(java.sql.Timestamp timestamp) {
		this.createdDate = timestamp;
	}

	public String getProcess_status_des() {
		return process_status_des;
	}

	public void setProcess_status_des(String process_status_des) {
		this.process_status_des = process_status_des;
	}

	public boolean isApplicableYes() {
		return applicableYes;
	}

	public void setApplicableYes(boolean applicableYes) {
		this.applicableYes = applicableYes;
	}

	public String getOffence() {
		return offence;
	}

	public void setOffence(String offence) {
		this.offence = offence;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getServiceTypeDescription() {
		return serviceTypeDescription;
	}

	public void setServiceTypeDescription(String serviceTypeDescription) {
		this.serviceTypeDescription = serviceTypeDescription;
	}

	public String getProvince_des() {
		return province_des;
	}

	public void setProvince_des(String province_des) {
		this.province_des = province_des;
	}

	public String getRecommendRemark() {
		return recommendRemark;
	}

	public void setRecommendRemark(String recommendRemark) {
		this.recommendRemark = recommendRemark;
	}

	public String getRecommended() {
		return recommended;
	}

	public void setRecommended(String recommended) {
		this.recommended = recommended;
	}

	public String getApprovedRemark() {
		return approvedRemark;
	}

	public void setApprovedRemark(String approvedRemark) {
		this.approvedRemark = approvedRemark;
	}

	public String getApproved() {
		return approved;
	}

	public void setApproved(String approved) {
		this.approved = approved;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
