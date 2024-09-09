package lk.informatics.ntc.model.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

public class IssuePermitDTO {

	private String tenderRefNo;
	private Date currentDate;
	private String applicationNo;
	private String queueNo;
	private String serviceType;
	private String busRegNo;

	private String permitNo;
	private String routeNo;
	private String routeFlag;
	private String via;
	private String destination;
	private String origin;
	private BigDecimal distance;
	private int trips;
	private String parkingPlace;
	private BigDecimal distanceParkingOrigin;
	private long serviceSeq;
	private String isBacklogApp;
	private String createdBy;
	private Timestamp createdDate;
	private String modifiedBy;
	private Timestamp modifiedDate;
	private String routeDivSec;
	private String parkingDivSec;
	private String isNewPermit;
	private String inspectionDate1;
	private String inspectionDate2;
	private String vehicleOwner;
	private String serviceDivSec;
	private float routeLength;
	private float parkingDesLength;

	public String getTenderRefNo() {
		return tenderRefNo;
	}

	public void setTenderRefNo(String tenderRefNo) {
		this.tenderRefNo = tenderRefNo;
	}

	public Date getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public String getQueueNo() {
		return queueNo;
	}

	public void setQueueNo(String queueNo) {
		this.queueNo = queueNo;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getBusRegNo() {
		return busRegNo;
	}

	public void setBusRegNo(String busRegNo) {
		this.busRegNo = busRegNo;
	}

	public String getPermitNo() {
		return permitNo;
	}

	public void setPermitNo(String permitNo) {
		this.permitNo = permitNo;
	}

	public String getRouteNo() {
		return routeNo;
	}

	public void setRouteNo(String routeNo) {
		this.routeNo = routeNo;
	}

	public String getVia() {
		return via;
	}

	public void setVia(String via) {
		this.via = via;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public BigDecimal getDistance() {
		return distance;
	}

	public void setDistance(BigDecimal distance) {
		this.distance = distance;
	}

	public String getParkingPlace() {
		return parkingPlace;
	}

	public void setParkingPlace(String parkingPlace) {
		this.parkingPlace = parkingPlace;
	}

	public long getServiceSeq() {
		return serviceSeq;
	}

	public void setServiceSeq(long serviceSeq) {
		this.serviceSeq = serviceSeq;
	}

	public String getIsBacklogApp() {
		return isBacklogApp;
	}

	public void setIsBacklogApp(String isBacklogApp) {
		this.isBacklogApp = isBacklogApp;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getRouteDivSec() {
		return routeDivSec;
	}

	public void setRouteDivSec(String routeDivSec) {
		this.routeDivSec = routeDivSec;
	}

	public String getParkingDivSec() {
		return parkingDivSec;
	}

	public void setParkingDivSec(String parkingDivSec) {
		this.parkingDivSec = parkingDivSec;
	}

	public int getTrips() {
		return trips;
	}

	public void setTrips(int trips) {
		this.trips = trips;
	}

	public BigDecimal getDistanceParkingOrigin() {
		return distanceParkingOrigin;
	}

	public void setDistanceParkingOrigin(BigDecimal distanceParkingOrigin) {
		this.distanceParkingOrigin = distanceParkingOrigin;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getRouteFlag() {
		return routeFlag;
	}

	public void setRouteFlag(String routeFlag) {
		this.routeFlag = routeFlag;
	}

	public String getIsNewPermit() {
		return isNewPermit;
	}

	public void setIsNewPermit(String isNewPermit) {
		this.isNewPermit = isNewPermit;
	}

	public String getInspectionDate1() {
		return inspectionDate1;
	}

	public void setInspectionDate1(String inspectionDate1) {
		this.inspectionDate1 = inspectionDate1;
	}

	public String getInspectionDate2() {
		return inspectionDate2;
	}

	public void setInspectionDate2(String inspectionDate2) {
		this.inspectionDate2 = inspectionDate2;
	}

	public String getVehicleOwner() {
		return vehicleOwner;
	}

	public void setVehicleOwner(String vehicleOwner) {
		this.vehicleOwner = vehicleOwner;
	}

	public String getServiceDivSec() {
		return serviceDivSec;
	}

	public void setServiceDivSec(String serviceDivSec) {
		this.serviceDivSec = serviceDivSec;
	}

	public float getRouteLength() {
		return routeLength;
	}

	public void setRouteLength(float routeLength) {
		this.routeLength = routeLength;
	}

	public float getParkingDesLength() {
		return parkingDesLength;
	}

	public void setParkingDesLength(float parkingDesLength) {
		this.parkingDesLength = parkingDesLength;
	}
	
	

}
