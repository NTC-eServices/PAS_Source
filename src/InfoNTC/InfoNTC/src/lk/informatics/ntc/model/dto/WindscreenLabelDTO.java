package lk.informatics.ntc.model.dto;

import java.io.Serializable;

public class WindscreenLabelDTO implements Serializable {

	private static final long serialVersionUID = 5860334383329501696L;

	// Fields for Windscreen Label UI

	private String transactionTypeDes;
	private String transactionTypeCode;
	private String applicationNo;
	private String permitNo;
	private String busNo;
	private String serviceType;
	private String routeNo;
	private String origin, destination, via;
	private String expiryDate;
	private String status;

	// ---End---

	// Getters and Setters

	public String getTransactionTypeDes() {
		return transactionTypeDes;
	}

	public void setTransactionTypeDes(String transactionTypeDes) {
		this.transactionTypeDes = transactionTypeDes;
	}

	public String getTransactionTypeCode() {
		return transactionTypeCode;
	}

	public void setTransactionTypeCode(String transactionTypeCode) {
		this.transactionTypeCode = transactionTypeCode;
	}

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

	public String getBusNo() {
		return busNo;
	}

	public void setBusNo(String busNo) {
		this.busNo = busNo;
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

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
