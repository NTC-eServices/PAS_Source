package lk.informatics.ntc.model.dto;

import java.io.Serializable;

import java.sql.Timestamp;
import java.math.BigDecimal;

public class RouteDTO implements Serializable {

	private static final long serialVersionUID = 8344243187243931903L;

	private long seq;
	private String routeNo;
	private String routeDes;
	private String origin;
	private String destination;
	private String via;
	private String status;
	private String createdBy;
	private Timestamp cratedDate;
	private String modifiedBy;
	private Timestamp modifiedDate;
	private BigDecimal distance;
	private String statusCode;
	private BigDecimal busFare;
	private int noOfPermits;
	private boolean routeFlag;
	private String routeFlagVal;
	private String fareChangedDate;
	

	private String originS;
	private String destinationS;
	private String viaS;

	private String originT;
	private String destinationT;
	private String viaT;

	public BigDecimal getBusFare() {
		return busFare;
	}

	public void setBusFare(BigDecimal busFare) {
		this.busFare = busFare;
	}

	public String getRouteNo() {
		return routeNo;
	}

	public void setRouteNo(String routeNo) {
		this.routeNo = routeNo;
	}

	public String getRouteDes() {
		return routeDes;
	}

	public void setRouteDes(String routeDes) {
		this.routeDes = routeDes;
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

	public String getVia() {
		return via;
	}

	public void setVia(String via) {
		this.via = via;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCratedDate() {
		return cratedDate;
	}

	public void setCratedDate(Timestamp cratedDate) {
		this.cratedDate = cratedDate;
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

	public long getSeq() {
		return seq;
	}

	public void setSeq(long seq) {
		this.seq = seq;
	}

	public BigDecimal getDistance() {
		return distance;
	}

	public void setDistance(BigDecimal distance) {
		this.distance = distance;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public int getNoOfPermits() {
		return noOfPermits;
	}

	public void setNoOfPermits(int noOfPermits) {
		this.noOfPermits = noOfPermits;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean isRouteFlag() {
		return routeFlag;
	}

	public void setRouteFlag(boolean routeFlag) {
		this.routeFlag = routeFlag;
	}

	public String getRouteFlagVal() {
		return routeFlagVal;
	}

	public void setRouteFlagVal(String routeFlagVal) {
		this.routeFlagVal = routeFlagVal;
	}

	public String getFareChangedDate() {
		return fareChangedDate;
	}

	public void setFareChangedDate(String fareChangedDate) {
		this.fareChangedDate = fareChangedDate;
	}

	public String getOriginS() {
		return originS;
	}

	public void setOriginS(String originS) {
		this.originS = originS;
	}

	public String getDestinationS() {
		return destinationS;
	}

	public void setDestinationS(String destinationS) {
		this.destinationS = destinationS;
	}

	public String getViaS() {
		return viaS;
	}

	public void setViaS(String viaS) {
		this.viaS = viaS;
	}

	public String getOriginT() {
		return originT;
	}

	public void setOriginT(String originT) {
		this.originT = originT;
	}

	public String getDestinationT() {
		return destinationT;
	}

	public void setDestinationT(String destinationT) {
		this.destinationT = destinationT;
	}

	public String getViaT() {
		return viaT;
	}

	public void setViaT(String viaT) {
		this.viaT = viaT;
	}

}
