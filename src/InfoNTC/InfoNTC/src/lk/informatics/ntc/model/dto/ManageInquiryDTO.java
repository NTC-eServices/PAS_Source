package lk.informatics.ntc.model.dto;

import java.io.Serializable;
import java.util.Date;

public class ManageInquiryDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String complainNo;
	private Date startDate;
	private Date endDate;
	private String prioirtyOrderCode;
	private String priorityOrderDesc;
	private String severityCode;
	private String severityDesc;
	private Date investigationDate;
	private String investigationTime;
	private String authority;
	private String vehicleNum;
	private Date eventDate;

	private Date availableDateSelect;
	private String startTime;
	private String endTime;
	private boolean timeTaken;
	private boolean lunchTime;

	private String permitNum;
	private String investigationDateStr;
	private String contactName;
	private String contactAddress;
	private String notifySentenceOne;
	private String notifySentenceTwo;
	private String notifySentenceThree;
	private String notifySentencFour;
	private String email;
	private String contactNum;
	private String driverConductorId;
	private String permitOwnerName;
	private String process_status;
	private String process_status_des;

	private String province;
	private String province_desc;
	private String specialRemark;
	private String actionDepartment;// code
	private String actionDepartment_desc;
	private String actionStatus;
	private String actionStatus_desc;

	private String actionTakenByGM;
	private String actionTakenByGMDate;
	private String actionTakenByDepartment;
	private String actionTakenByDepartmentDate;
	private String closedDate;

	public String getProcess_status() {
		return process_status;
	}

	public void setProcess_status(String process_status) {
		this.process_status = process_status;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactAddress() {
		return contactAddress;
	}

	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}

	public String getNotifySentenceOne() {
		return notifySentenceOne;
	}

	public void setNotifySentenceOne(String notifySentenceOne) {
		this.notifySentenceOne = notifySentenceOne;
	}

	public String getNotifySentenceTwo() {
		return notifySentenceTwo;
	}

	public void setNotifySentenceTwo(String notifySentenceTwo) {
		this.notifySentenceTwo = notifySentenceTwo;
	}

	public String getNotifySentenceThree() {
		return notifySentenceThree;
	}

	public void setNotifySentenceThree(String notifySentenceThree) {
		this.notifySentenceThree = notifySentenceThree;
	}

	public String getComplainNo() {
		return complainNo;
	}

	public void setComplainNo(String complainNo) {
		this.complainNo = complainNo;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getPrioirtyOrderCode() {
		return prioirtyOrderCode;
	}

	public void setPrioirtyOrderCode(String prioirtyOrderCode) {
		this.prioirtyOrderCode = prioirtyOrderCode;
	}

	public String getPriorityOrderDesc() {
		return priorityOrderDesc;
	}

	public void setPriorityOrderDesc(String priorityOrderDesc) {
		this.priorityOrderDesc = priorityOrderDesc;
	}

	public String getSeverityCode() {
		return severityCode;
	}

	public void setSeverityCode(String severityCode) {
		this.severityCode = severityCode;
	}

	public Date getInvestigationDate() {
		return investigationDate;
	}

	public void setInvestigationDate(Date investigationDate) {
		this.investigationDate = investigationDate;
	}

	public String getInvestigationTime() {
		return investigationTime;
	}

	public void setInvestigationTime(String investigationTime) {
		this.investigationTime = investigationTime;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public String getVehicleNum() {
		return vehicleNum;
	}

	public void setVehicleNum(String vehicleNum) {
		this.vehicleNum = vehicleNum;
	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public String getSeverityDesc() {
		return severityDesc;
	}

	public void setSeverityDesc(String severityDesc) {
		this.severityDesc = severityDesc;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public boolean isLunchTime() {
		return lunchTime;
	}

	public void setLunchTime(boolean lunchTime) {
		this.lunchTime = lunchTime;
	}

	public Date getAvailableDateSelect() {
		return availableDateSelect;
	}

	public void setAvailableDateSelect(Date availableDateSelect) {
		this.availableDateSelect = availableDateSelect;
	}

	public boolean isTimeTaken() {
		return timeTaken;
	}

	public void setTimeTaken(boolean timeTaken) {
		this.timeTaken = timeTaken;
	}

	public String getPermitNum() {
		return permitNum;
	}

	public void setPermitNum(String permitNum) {
		this.permitNum = permitNum;
	}

	public String getInvestigationDateStr() {
		return investigationDateStr;
	}

	public void setInvestigationDateStr(String investigationDateStr) {
		this.investigationDateStr = investigationDateStr;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContactNum() {
		return contactNum;
	}

	public void setContactNum(String contactNum) {
		this.contactNum = contactNum;
	}

	public String getNotifySentencFour() {
		return notifySentencFour;
	}

	public void setNotifySentencFour(String notifySentencFour) {
		this.notifySentencFour = notifySentencFour;
	}

	public String getDriverConductorId() {
		return driverConductorId;
	}

	public void setDriverConductorId(String driverConductorId) {
		this.driverConductorId = driverConductorId;
	}

	public String getPermitOwnerName() {
		return permitOwnerName;
	}

	public void setPermitOwnerName(String permitOwnerName) {
		this.permitOwnerName = permitOwnerName;
	}

	public String getProcess_status_des() {
		return process_status_des;
	}

	public void setProcess_status_des(String process_status_des) {
		this.process_status_des = process_status_des;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getSpecialRemark() {
		return specialRemark;
	}

	public void setSpecialRemark(String specialRemark) {
		this.specialRemark = specialRemark;
	}

	public String getProvince_desc() {
		return province_desc;
	}

	public void setProvince_desc(String province_desc) {
		this.province_desc = province_desc;
	}

	public String getActionDepartment() {
		return actionDepartment;
	}

	public void setActionDepartment(String actionDepartment) {
		this.actionDepartment = actionDepartment;
	}

	public String getActionDepartment_desc() {
		return actionDepartment_desc;
	}

	public void setActionDepartment_desc(String actionDepartment_desc) {
		this.actionDepartment_desc = actionDepartment_desc;
	}

	public String getActionStatus() {
		return actionStatus;
	}

	public void setActionStatus(String actionStatus) {
		this.actionStatus = actionStatus;
	}

	public String getActionStatus_desc() {
		return actionStatus_desc;
	}

	public void setActionStatus_desc(String actionStatus_desc) {
		this.actionStatus_desc = actionStatus_desc;
	}

	public String getActionTakenByGM() {
		return actionTakenByGM;
	}

	public void setActionTakenByGM(String actionTakenByGM) {
		this.actionTakenByGM = actionTakenByGM;
	}

	public String getActionTakenByGMDate() {
		return actionTakenByGMDate;
	}

	public void setActionTakenByGMDate(String actionTakenByGMDate) {
		this.actionTakenByGMDate = actionTakenByGMDate;
	}

	public String getActionTakenByDepartment() {
		return actionTakenByDepartment;
	}

	public void setActionTakenByDepartment(String actionTakenByDepartment) {
		this.actionTakenByDepartment = actionTakenByDepartment;
	}

	public String getActionTakenByDepartmentDate() {
		return actionTakenByDepartmentDate;
	}

	public void setActionTakenByDepartmentDate(String actionTakenByDepartmentDate) {
		this.actionTakenByDepartmentDate = actionTakenByDepartmentDate;
	}

	public String getClosedDate() {
		return closedDate;
	}

	public void setClosedDate(String closedDate) {
		this.closedDate = closedDate;
	}

}
