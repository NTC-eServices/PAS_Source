package lk.informatics.ntc.model.dto;

import java.util.Date;

public class FlyingSquadInvestiMasterDTO {

	private String refNo;
	private Date investigationDate;
	private String groupCd;
	private String vehicleNo;
	private String driverName;
	private String startPlace;
	private String endPlace;
	private String investigationDetails;
	private String nightParkPlace;
	private String remarks;
	private Date endtime;
	private Date startTime;
	private String status;
	private String groupName;

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public Date getInvestigationDate() {
		return investigationDate;
	}

	public void setInvestigationDate(Date investigationDate) {
		this.investigationDate = investigationDate;
	}

	public String getGroupCd() {
		return groupCd;
	}

	public void setGroupCd(String groupCd) {
		this.groupCd = groupCd;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getStartPlace() {
		return startPlace;
	}

	public void setStartPlace(String startPlace) {
		this.startPlace = startPlace;
	}

	public String getInvestigationDetails() {
		return investigationDetails;
	}

	public void setInvestigationDetails(String investigationDetails) {
		this.investigationDetails = investigationDetails;
	}

	public String getNightParkPlace() {
		return nightParkPlace;
	}

	public void setNightParkPlace(String nightParkPlace) {
		this.nightParkPlace = nightParkPlace;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getEndtime() {
		return endtime;
	}

	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getEndPlace() {
		return endPlace;
	}

	public void setEndPlace(String endPlace) {
		this.endPlace = endPlace;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

}
