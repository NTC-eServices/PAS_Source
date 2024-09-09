package lk.informatics.ntc.model.dto;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class CombinePanelGenaratorDTO {
	
	private long seq;

	private String routeDes;
	
	private String groupNo;
	private String defineSide;
	private String midPointName;
	private String takenTime;
	
	private String permitNo;
	
	private String time;
	private String bus;
	
	
	private String abbreviation;
	private String abbreviationDes;
	private String busNo;
	private String origin;
	private String destination;
	private String serviceType;
	private String routeNo;
	private String arrival;
	private String arrivalOne;
	private String depature;
	private String depatureOne;
	private String tripno;
	private String endTime;
	private String serviceTime;
	private String steringHours;
	private String restTime;
	private String break1;
	private String break2;
	private String break3;
	private String travelTime;
	
	private String startTime;
	private String dayNo;
	private String tripId;
	private String refNo;
	private int rowNo;
	
	private LinkedHashMap<String, List<String>> timesWithRoute;
	
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

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}

	public String getDefineSide() {
		return defineSide;
	}

	public void setDefineSide(String defineSide) {
		this.defineSide = defineSide;
	}

	public String getRouteDes() {
		return routeDes;
	}

	public void setRouteDes(String routeDes) {
		this.routeDes = routeDes;
	}

	public long getSeq() {
		return seq;
	}

	public void setSeq(long seq) {
		this.seq = seq;
	}

	public String getMidPointName() {
		return midPointName;
	}

	public void setMidPointName(String midPointName) {
		this.midPointName = midPointName;
	}

	public String getTakenTime() {
		return takenTime;
	}

	public void setTakenTime(String takenTime) {
		this.takenTime = takenTime;
	}

	public String getBusNo() {
		return busNo;
	}

	public void setBusNo(String busNo) {
		this.busNo = busNo;
	}

	public String getPermitNo() {
		return permitNo;
	}

	public void setPermitNo(String permitNo) {
		this.permitNo = permitNo;
	}

	public void setTimeTaken(String newValue) {
		// TODO Auto-generated method stub
		
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getBus() {
		return bus;
	}

	public void setBus(String bus) {
		this.bus = bus;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public String getArrival() {
		return arrival;
	}

	public void setArrival(String arrival) {
		this.arrival = arrival;
	}

	public String getDepature() {
		return depature;
	}

	public void setDepature(String depature) {
		this.depature = depature;
	}

	public String getTripno() {
		return tripno;
	}

	public void setTripno(String tripno) {
		this.tripno = tripno;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(String serviceTime) {
		this.serviceTime = serviceTime;
	}

	public String getSteringHours() {
		return steringHours;
	}

	public void setSteringHours(String steringHours) {
		this.steringHours = steringHours;
	}

	public String getRestTime() {
		return restTime;
	}

	public void setRestTime(String restTime) {
		this.restTime = restTime;
	}

	public String getBreak1() {
		return break1;
	}

	public void setBreak1(String break1) {
		this.break1 = break1;
	}

	public String getBreak2() {
		return break2;
	}

	public void setBreak2(String break2) {
		this.break2 = break2;
	}

	public String getBreak3() {
		return break3;
	}

	public void setBreak3(String break3) {
		this.break3 = break3;
	}

	public String getArrivalOne() {
		return arrivalOne;
	}

	public void setArrivalOne(String arrivalOne) {
		this.arrivalOne = arrivalOne;
	}

	public String getDepatureOne() {
		return depatureOne;
	}

	public void setDepatureOne(String depatureOne) {
		this.depatureOne = depatureOne;
	}

	public String getAbbreviationDes() {
		return abbreviationDes;
	}

	public void setAbbreviationDes(String abbreviationDes) {
		this.abbreviationDes = abbreviationDes;
	}

	public String getTravelTime() {
		return travelTime;
	}

	public void setTravelTime(String travelTime) {
		this.travelTime = travelTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getDayNo() {
		return dayNo;
	}

	public void setDayNo(String dayNo) {
		this.dayNo = dayNo;
	}

	public String getTripId() {
		return tripId;
	}

	public void setTripId(String tripId) {
		this.tripId = tripId;
	}

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public LinkedHashMap<String, List<String>> getTimesWithRoute() {
		return timesWithRoute;
	}

	public void setTimesWithRoute(LinkedHashMap<String, List<String>> timesWithRoute) {
		this.timesWithRoute = timesWithRoute;
	}

	public int getRowNo() {
		return rowNo;
	}

	public void setRowNo(int rowNo) {
		this.rowNo = rowNo;
	}

	

	
}
