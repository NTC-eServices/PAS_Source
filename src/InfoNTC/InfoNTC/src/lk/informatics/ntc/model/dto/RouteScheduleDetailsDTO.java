package lk.informatics.ntc.model.dto;

import java.io.Serializable;

/**
 * Created for combine control sheet
 * Created by: gayathra.m
 * */
public class RouteScheduleDetailsDTO implements Serializable {

	private static final long serialVersionUID = 5244863068799928927L;
	private String tripId;
	private String busNo;
	private String dayNo;

	public RouteScheduleDetailsDTO() {
	}

	public String getTripId() {
		return tripId;
	}

	public void setTripId(String tripId) {
		this.tripId = tripId;
	}

	public String getBusNo() {
		return busNo;
	}

	public void setBusNo(String busNo) {
		this.busNo = busNo;
	}

	public String getDayNo() {
		return dayNo;
	}

	public void setDayNo(String dayNo) {
		this.dayNo = dayNo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
