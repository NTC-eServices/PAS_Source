package lk.informatics.ntc.model.dto;

import java.io.Serializable;
import java.util.List;

public class TerminalArrivalDepartureDTO implements Serializable {
	private static final long serialVersionUID = 8344243187243931205L;

	private String stationCode;
	private String stationDesc;
	private String terminal;
	private String block;
	private String arrivalDate;
	private int turnNo;
	private List<TerminalArrivalDepartureTimeDTO> scheduledTimeListForDay;
	
	private String applicationNo;
	private String permitNo;
	private String busRegNo;
	private String serviceTypeCode;
	private String serviceType;
	private String routeNo;
	private String routeDesc;
	private String origin;
	private String originDesc;
	private String destination;
	private String destinationDesc;
	private String permitIssueDate;
	private String permitExpireDate;
	private String owner;
	private String address1;
	private String address2;
	private String address3;
	private String city;
	private String telephoneNo;
	private String mobileNo;
	private String nicNo;
	
	/**new**/
	private String panelGenNumber;
	private String noOfDays;
	private String startDate;
	private String endDate;
	/**end**/
	

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
	public String getBusRegNo() {
		return busRegNo;
	}
	public void setBusRegNo(String busRegNo) {
		this.busRegNo = busRegNo;
	}
	public String getServiceTypeCode() {
		return serviceTypeCode;
	}
	public void setServiceTypeCode(String serviceTypeCode) {
		this.serviceTypeCode = serviceTypeCode;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getRouteNo() {
		return routeNo;
	}
	public void setRouteNo(String routeNo) {
		this.routeNo = routeNo;
	}
	public String getRouteDesc() {
		return routeDesc;
	}
	public void setRouteDesc(String routeDesc) {
		this.routeDesc = routeDesc;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getOriginDesc() {
		return originDesc;
	}
	public void setOriginDesc(String originDesc) {
		this.originDesc = originDesc;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getDestinationDesc() {
		return destinationDesc;
	}
	public void setDestinationDesc(String destinationDesc) {
		this.destinationDesc = destinationDesc;
	}
	public String getPermitIssueDate() {
		return permitIssueDate;
	}
	public void setPermitIssueDate(String permitIssueDate) {
		this.permitIssueDate = permitIssueDate;
	}
	public String getPermitExpireDate() {
		return permitExpireDate;
	}
	public void setPermitExpireDate(String permitExpireDate) {
		this.permitExpireDate = permitExpireDate;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getAddress3() {
		return address3;
	}
	public void setAddress3(String address3) {
		this.address3 = address3;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getTelephoneNo() {
		return telephoneNo;
	}
	public void setTelephoneNo(String telephoneNo) {
		this.telephoneNo = telephoneNo;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getNicNo() {
		return nicNo;
	}
	public void setNicNo(String nicNo) {
		this.nicNo = nicNo;
	}
	public String getTerminal() {
		return terminal;
	}
	public String getStationCode() {
		return stationCode;
	}
	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}
	public String getStationDesc() {
		return stationDesc;
	}
	public void setStationDesc(String stationDesc) {
		this.stationDesc = stationDesc;
	}
	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}
	public String getBlock() {
		return block;
	}
	public void setBlock(String block) {
		this.block = block;
	}
	public String getArrivalDate() {
		return arrivalDate;
	}
	public void setArrivalDate(String arrivalDate) {
		this.arrivalDate = arrivalDate;
	}
	public int getTurnNo() {
		return turnNo;
	}
	public void setTurnNo(int turnNo) {
		this.turnNo = turnNo;
	}
	public List<TerminalArrivalDepartureTimeDTO> getScheduledTimeListForDay() {
		return scheduledTimeListForDay;
	}
	public void setScheduledTimeListForDay(List<TerminalArrivalDepartureTimeDTO> scheduledTimeListForDay) {
		this.scheduledTimeListForDay = scheduledTimeListForDay;
	}
	public String getPanelGenNumber() {
		return panelGenNumber;
	}
	public void setPanelGenNumber(String panelGenNumber) {
		this.panelGenNumber = panelGenNumber;
	}
	public String getNoOfDays() {
		return noOfDays;
	}
	public void setNoOfDays(String noOfDays) {
		this.noOfDays = noOfDays;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	
}
