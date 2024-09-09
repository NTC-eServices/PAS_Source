package lk.informatics.ntc.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class RouteCreationDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8762610598735172701L;

	private String routeNo;
	private String startFrom;
	private String endAt;
	private String routeVia;
	private String abbreviationStart;
	private String abbreviationEnd;
	private BigDecimal length;
	private Date travelTime;
	private BigDecimal busSpeed;
	private Date driverRestTime;
	private String routeType;
	private String status;
	private String travelTimeString;
	private String[] busTypes;
	private String timetaken;
	private LinkedList<MidpointUIDTO> midPoints;
	private LinkedList<MidpointUIDTO> midPointsSwap;
	private String serviceTypeCode;
	private String serviceTypeDes;
	private Number stageCost;
	private String statusRouteToStation;

	private String routeDesc;
	private String stage;
	private String station;
	private StationDetailsDTO stationDetailsDTO;
	private int stageFee;
	private String stationCode;
	private String staionName;
	private String busTypeStr;
	private String timeTaken;
	
	private Double distance;
	private String travelTimeStr;
	
	public String getAbbreviationStart() {
		return abbreviationStart;
	}

	public void setAbbreviationStart(String abbreviationStart) {
		this.abbreviationStart = abbreviationStart;
	}

	public String getAbbreviationEnd() {
		return abbreviationEnd;
	}

	public void setAbbreviationEnd(String abbreviationEnd) {
		this.abbreviationEnd = abbreviationEnd;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getTravelTime() {
		return travelTime;
	}

	public void setTravelTime(Date travelTime) {
		this.travelTime = travelTime;
	}

	public Date getDriverRestTime() {
		return driverRestTime;
	}

	public void setDriverRestTime(Date driverRestTime) {
		this.driverRestTime = driverRestTime;
	}

	public String getStartFrom() {
		return startFrom;
	}

	public void setStartFrom(String startFrom) {
		this.startFrom = startFrom;
	}

	public String getEndAt() {
		return endAt;
	}

	public void setEndAt(String endAt) {
		this.endAt = endAt;
	}

	public String getRouteNo() {
		return routeNo;
	}

	public void setRouteNo(String routeNo) {
		this.routeNo = routeNo;
	}

	public String getRouteType() {
		return routeType;
	}

	public void setRouteType(String routeType) {
		this.routeType = routeType;
	}

	public String getTravelTimeString() {
		return travelTimeString;
	}

	public void setTravelTimeString(String travelTimeString) {
		this.travelTimeString = travelTimeString;
	}

	public String[] getBusTypes() {
		return busTypes;
	}

	public void setBusTypes(String[] busTypes) {
		this.busTypes = busTypes;
	}

	public BigDecimal getLength() {
		return length;
	}

	public void setLength(BigDecimal length) {
		this.length = length;
	}

	public BigDecimal getBusSpeed() {
		return busSpeed;
	}

	public void setBusSpeed(BigDecimal busSpeed) {
		this.busSpeed = busSpeed;
	}

	public String getTimetaken() {
		if (travelTime != null) {
			DateFormat timeFormat = new SimpleDateFormat("HH:mm");
			timetaken = timeFormat.format(travelTime);
			return timetaken;
		} else {
			return timetaken;
		}
	}

	public LinkedList<MidpointUIDTO> getMidPoints() {
		return midPoints;
	}

	public void setMidPoints(LinkedList<MidpointUIDTO> midPoints) {
		this.midPoints = midPoints;
	}

	public LinkedList<MidpointUIDTO> getMidPointsSwap() {
		return midPointsSwap;
	}

	public void setMidPointsSwap(LinkedList<MidpointUIDTO> midPointsSwap) {
		this.midPointsSwap = midPointsSwap;
	}

	public void setTimetaken(String timetaken) {
		this.timetaken = timetaken;
	}

	public String getServiceTypeCode() {
		return serviceTypeCode;
	}

	public void setServiceTypeCode(String serviceTypeCode) {
		this.serviceTypeCode = serviceTypeCode;
	}

	public String getServiceTypeDes() {
		return serviceTypeDes;
	}

	public void setServiceTypeDes(String serviceTypeDes) {
		this.serviceTypeDes = serviceTypeDes;
	}

	public Number getStageCost() {
		return stageCost;
	}

	public void setStageCost(Number stageCost) {
		this.stageCost = stageCost;
	}

	public String getStatusRouteToStation() {
		return statusRouteToStation;
	}

	public void setStatusRouteToStation(String statusRouteToStation) {
		this.statusRouteToStation = statusRouteToStation;
	}

	public String getRouteDesc() {
		return routeDesc;
	}

	public void setRouteDesc(String routeDesc) {
		this.routeDesc = routeDesc;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public StationDetailsDTO getStationDetailsDTO() {
		return stationDetailsDTO;
	}

	public void setStationDetailsDTO(StationDetailsDTO stationDetailsDTO) {
		this.stationDetailsDTO = stationDetailsDTO;
	}

	public int getStageFee() {
		return stageFee;
	}

	public void setStageFee(int stageFee) {
		this.stageFee = stageFee;
	}

	public String getBusTypeStr() {
		return busTypeStr;
	}

	public void setBusTypeStr(String busTypeStr) {
		this.busTypeStr = busTypeStr;
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public String getStaionName() {
		return staionName;
	}

	public void setStaionName(String staionName) {
		this.staionName = staionName;
	}

	public String getTimeTaken() {
		return timeTaken;
	}

	public void setTimeTaken(String timeTaken) {
		this.timeTaken = timeTaken;
	}

	public String getRouteVia() {
		return routeVia;
	}

	public void setRouteVia(String routeVia) {
		this.routeVia = routeVia;
	}



	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public String getTravelTimeStr() {
		return travelTimeStr;
	}

	public void setTravelTimeStr(String travelTimeStr) {
		this.travelTimeStr = travelTimeStr;
	}




}
