package lk.informatics.ntc.model.dto;

import java.util.List;

public class TerminalTimeTableDTO {
	private String routeNo;
	private String serviceType;
	private List<String> busNo;
	private List<String> startTime;
	private List<String> dayList;
	private String permitNo;
	private String terminalCode;
	private String scheduleTime;
	private String actualTime;
	
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
	public String getPermitNo() {
		return permitNo;
	}
	public void setPermitNo(String permitNo) {
		this.permitNo = permitNo;
	}
	public String getTerminalCode() {
		return terminalCode;
	}
	public void setTerminalCode(String terminalCode) {
		this.terminalCode = terminalCode;
	}
	public String getScheduleTime() {
		return scheduleTime;
	}
	public void setScheduleTime(String scheduleTime) {
		this.scheduleTime = scheduleTime;
	}
	public String getActualTime() {
		return actualTime;
	}
	public void setActualTime(String actualTime) {
		this.actualTime = actualTime;
	}
	public List<String> getBusNo() {
		return busNo;
	}
	public void setBusNo(List<String> busNo) {
		this.busNo = busNo;
	}
	public List<String> getStartTime() {
		return startTime;
	}
	public void setStartTime(List<String> startTime) {
		this.startTime = startTime;
	}
	public List<String> getDayList() {
		return dayList;
	}
	public void setDayList(List<String> dayList) {
		this.dayList = dayList;
	}
	
	
}
