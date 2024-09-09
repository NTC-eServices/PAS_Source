package lk.informatics.ntc.model.dto;

import java.io.Serializable;
import java.util.Date;

public class ExpiredPermitDTO implements Serializable {

	private static final long serialVersionUID = 3327383347682378754L;
	private String permitNo;
	private Date searchDate;
	private Date permitExpiryDate;
	private Date newPermitExpiryDate;
	private String renewalApplicationNo;
	private String queueNo;
	private String regNoOfBus;
	private String vehicleNo;
	private String rejectedReason;
	private String rePeriod;
	private String permitOwner;
	
	public String getPermitNo() {
		return permitNo;
	}
	public void setPermitNo(String permitNo) {
		this.permitNo = permitNo;
	}
	public Date getPermitExpiryDate() {
		return permitExpiryDate;
	}
	public void setPermitExpiryDate(Date permitExpiryDate) {
		this.permitExpiryDate = permitExpiryDate;
	}
	public Date getNewPermitExpiryDate() {
		return newPermitExpiryDate;
	}
	public void setNewPermitExpiryDate(Date newPermitExpiryDate) {
		this.newPermitExpiryDate = newPermitExpiryDate;
	}
	public String getRenewalApplicationNo() {
		return renewalApplicationNo;
	}
	public void setRenewalApplicationNo(String renewalApplicationNo) {
		this.renewalApplicationNo = renewalApplicationNo;
	}
	public String getQueueNo() {
		return queueNo;
	}
	public void setQueueNo(String queueNo) {
		this.queueNo = queueNo;
	}
	public String getRegNoOfBus() {
		return regNoOfBus;
	}
	public void setRegNoOfBus(String regNoOfBus) {
		this.regNoOfBus = regNoOfBus;
	}
	public String getVehicleNo() {
		return vehicleNo;
	}
	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}
	public String getRejectedReason() {
		return rejectedReason;
	}
	public void setRejectedReason(String rejectedReason) {
		this.rejectedReason = rejectedReason;
	}
	public String getRePeriod() {
		return rePeriod;
	}
	public void setRePeriod(String rePeriod) {
		this.rePeriod = rePeriod;
	}
	public String getPermitOwner() {
		return permitOwner;
	}
	public void setPermitOwner(String permitOwner) {
		this.permitOwner = permitOwner;
	}
	public Date getSearchDate() {
		return searchDate;
	}
	public void setSearchDate(Date searchDate) {
		this.searchDate = searchDate;
	}
	
}
