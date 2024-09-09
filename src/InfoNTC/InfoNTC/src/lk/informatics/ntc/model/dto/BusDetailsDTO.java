package lk.informatics.ntc.model.dto;

import java.util.ArrayList;
import java.util.List;

import org.primefaces.component.column.Column;

public class BusDetailsDTO {
	
	private String abbreviation;
	private String abbreviationDestination;
	private String busNo;
	private String permitNo;
	private String startTime;
	private String startTimeDestination;
	private String endTime;
	private String endTimeDestination;
	private boolean fixedBus;
	private boolean fixedBusDes;
	private String fix;
	private long seq;
	private List<String> takenTime = new ArrayList<String>();
	private List<String> takenTimeDestination = new ArrayList<String>();
	private List<String> midList = new ArrayList<String>();
	private List<Column> generatedColumns = new ArrayList<>();
	private List<Column> generatedColumns2 = new ArrayList<>();
	private String routeNo;
	private String serviceType;
	private String id;
	
	
	public String getAbbreviationDestination() {
		return abbreviationDestination;
	}
	public void setAbbreviationDestination(String abbreviationDestination) {
		this.abbreviationDestination = abbreviationDestination;
	}
	public String getStartTimeDestination() {
		return startTimeDestination;
	}
	public void setStartTimeDestination(String startTimeDestination) {
		this.startTimeDestination = startTimeDestination;
	}
	public String getEndTimeDestination() {
		return endTimeDestination;
	}
	public void setEndTimeDestination(String endTimeDestination) {
		this.endTimeDestination = endTimeDestination;
	}
	public List<String> getTakenTimeDestination() {
		return takenTimeDestination;
	}
	public void setTakenTimeDestination(List<String> takenTimeDestination) {
		this.takenTimeDestination = takenTimeDestination;
	}

	public String getAbbreviation() {
		return abbreviation;
	}
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
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
	public boolean isFixedBus() {
		return fixedBus;
	}
	public void setFixedBus(boolean fixedBus) {
		this.fixedBus = fixedBus;
	}
	public List<String> getTakenTime() {
		return takenTime;
	}
	public void setTakenTime(List<String> takenTime) {
		this.takenTime = takenTime;
	}

	public long getSeq() {
		return seq;
	}
	public void setSeq(long seq) {
		this.seq = seq;
	}
	public List<String> getMidList() {
		return midList;
	}
	public void setMidList(List<String> selectedOrgToDesMidPointList) {
		this.midList = selectedOrgToDesMidPointList;
	}
	public String getRouteNo() {
		return routeNo;
	}
	public void setRouteNo(String routeNo) {
		this.routeNo = routeNo;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isFixedBusDes() {
		return fixedBusDes;
	}
	public void setFixedBusDes(boolean fixedBusDes) {
		this.fixedBusDes = fixedBusDes;
	}
	public String getFix() {
		return fix;
	}
	public void setFix(String fix) {
		this.fix = fix;
	}
	public List<Column> getGeneratedColumns() {
		return generatedColumns;
	}
	public void setGeneratedColumns(List<Column> generatedColumns) {
		this.generatedColumns = generatedColumns;
	}
	public List<Column> getGeneratedColumns2() {
		return generatedColumns2;
	}
	public void setGeneratedColumns2(List<Column> generatedColumns2) {
		this.generatedColumns2 = generatedColumns2;
	}

	
	
	
	
    
}
