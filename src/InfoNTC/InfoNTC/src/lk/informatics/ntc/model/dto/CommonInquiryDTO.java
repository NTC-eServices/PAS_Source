package lk.informatics.ntc.model.dto;

import java.sql.Timestamp;
import java.util.Date;

public class CommonInquiryDTO {
	private String complaintNo;
	private String permitNo;
	private String busNo;
	private String createdBy;
	private Timestamp createdDate;
	private String statusDes;
	private String functionDes;
	private String terminalId;
	private String payType;
	private String voucherNo;
	private Date sheduleDate;
	private String nicNo;
	private String appNo;
	private String driverConductorId;
	
	
	public String getNicNo() {
		return nicNo;
	}
	public void setNicNo(String nicNo) {
		this.nicNo = nicNo;
	}
	public String getAppNo() {
		return appNo;
	}
	public void setAppNo(String appNo) {
		this.appNo = appNo;
	}
	
	public String getDriverConductorId() {
		return driverConductorId;
	}
	public void setDriverConductorId(String driverConductorId) {
		this.driverConductorId = driverConductorId;
	}
	public String getComplaintNo() {
		return complaintNo;
	}
	public void setComplaintNo(String complaintNo) {
		this.complaintNo = complaintNo;
	}
	public String getPermitNo() {
		return permitNo;
	}
	public void setPermitNo(String permitNo) {
		this.permitNo = permitNo;
	}
	public String getBusNo() {
		return busNo;
	}
	public void setBusNo(String busNo) {
		this.busNo = busNo;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	
	public String getFunctionDes() {
		return functionDes;
	}
	public void setFunctionDes(String functionDes) {
		this.functionDes = functionDes;
	}
	public String getStatusDes() {
		return statusDes;
	}
	public void setStatusDes(String statusDes) {
		this.statusDes = statusDes;
	}
	public String getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
	public Date getSheduleDate() {
		return sheduleDate;
	}
	public void setSheduleDate(Date sheduleDate) {
		this.sheduleDate = sheduleDate;
	}

	
}
