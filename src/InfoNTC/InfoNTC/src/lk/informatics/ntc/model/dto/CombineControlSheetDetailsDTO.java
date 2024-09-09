package lk.informatics.ntc.model.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class CombineControlSheetDetailsDTO implements Serializable {

	private static final long serialVersionUID = -4941524677444950847L;
	
	private String busNo;
	private String routeNo;
	private String serviceType;
	private String tripId;
	private String time;
	private LocalDateTime localTime;

	public CombineControlSheetDetailsDTO() {
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getBusNo() {
		return busNo;
	}

	public void setBusNo(String busNo) {
		this.busNo = busNo;
	}

	public String getRouteNo() {
		return routeNo;
	}

	public void setRouteNo(String routeNo) {
		this.routeNo = routeNo;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTripId() {
		return tripId;
	}

	public void setTripId(String tripId) {
		this.tripId = tripId;
	}

	public LocalDateTime getLocalTime() {
		return localTime;
	}

	public void setLocalTime(LocalDateTime localTime) {
		this.localTime = localTime;
	}

	

}
