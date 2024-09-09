package lk.informatics.ntc.model.dto;

import java.io.Serializable;
import java.util.ArrayList;

public class FlyingSquadChargeSheetReportDTO implements Serializable{

	private static final long serialVersionUID = 5406211018233032508L;
	
	private String user;
	private String invesno;
	private String vehicleNo;
	private String ownerName;
	private String route;
	private String from;
	private String to;
	private ArrayList<FluingSquadVioConditionDTO>conditions;
	private ArrayList<FlyingSquadVioDocumentsDTO>documents;
	private String permitno;
    private String serviceTypeDet;
    private String driverId;
    private String conductorId;
    private String driverName;
    private String conductorName;
    private String reportNo;
	private String driverNIC;
	private String conductorNIC;

	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getInvesno() {
		return invesno;
	}
	public void setInvesno(String invesno) {
		this.invesno = invesno;
	}
	public String getVehicleNo() {
		return vehicleNo;
	}
	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getRoute() {
		return route;
	}
	public void setRoute(String route) {
		this.route = route;
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
	public ArrayList<FluingSquadVioConditionDTO> getConditions() {
		return conditions;
	}
	public void setConditions(ArrayList<FluingSquadVioConditionDTO> conditions) {
		this.conditions = conditions;
	}
	public ArrayList<FlyingSquadVioDocumentsDTO> getDocuments() {
		return documents;
	}
	public void setDocuments(ArrayList<FlyingSquadVioDocumentsDTO> documents) {
		this.documents = documents;
	}
	public String getPermitno() {
		return permitno;
	}
	public void setPermitno(String permitno) {
		this.permitno = permitno;
	}
	public String getServiceTypeDet() {
		return serviceTypeDet;
	}
	public void setServiceTypeDet(String serviceTypeDet) {
		this.serviceTypeDet = serviceTypeDet;
	}
	public String getDriverId() {
		return driverId;
	}
	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}
	public String getConductorId() {
		return conductorId;
	}
	public void setConductorId(String conductorId) {
		this.conductorId = conductorId;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getConductorName() {
		return conductorName;
	}
	public void setConductorName(String conductorName) {
		this.conductorName = conductorName;
	}
	public String getReportNo() {
		return reportNo;
	}
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
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
	
    
}
