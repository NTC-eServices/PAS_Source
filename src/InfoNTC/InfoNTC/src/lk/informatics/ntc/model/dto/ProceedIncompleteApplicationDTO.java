package lk.informatics.ntc.model.dto;

import java.sql.Timestamp;

public class ProceedIncompleteApplicationDTO {
	private String applicationNo;
	private String vehicleNo;
	private String inspectionType; // PI AI CI SI II
	private String taskCode;
	private String taskStatus;
	private String proceedRemark;
	private String loginUser;// for proceedGivenBy
	private Timestamp proceedGivenDate;
	
	public String getApplicationNo() {
		return applicationNo;
	}
	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}
	public String getVehicleNo() {
		return vehicleNo;
	}
	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}
	public String getInspectionType() {
		return inspectionType;
	}
	public void setInspectionType(String inspectionType) {
		this.inspectionType = inspectionType;
	}
	public String getTaskCode() {
		return taskCode;
	}
	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}
	public String getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	public String getProceedRemark() {
		return proceedRemark;
	}
	public void setProceedRemark(String proceedRemark) {
		this.proceedRemark = proceedRemark;
	}
	public String getLoginUser() {
		return loginUser;
	}
	public void setLoginUser(String loginUser) {
		this.loginUser = loginUser;
	}
	public Timestamp getProceedGivenDate() {
		return proceedGivenDate;
	}
	public void setProceedGivenDate(Timestamp proceedGivenDate) {
		this.proceedGivenDate = proceedGivenDate;
	}
}
