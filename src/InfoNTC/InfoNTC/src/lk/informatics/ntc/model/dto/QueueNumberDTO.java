package lk.informatics.ntc.model.dto;

import java.io.Serializable;

public class QueueNumberDTO implements Serializable {

	private static final long serialVersionUID = -6874709078293380811L;

	private String transTypeCode;
	private String permitNo;
	private String vehicleNo;
	private String applicationNo;
	private String tenderRefNo;
	private String queueService;// Normal, Priority
	private String queueNumber;
	private String orderSet;
	private String counterNo;
	private boolean isPermitInfoFound;
	private String inspectionType;

	private String tokenNo;

	private String applicationPmStatus;
	private int skipCount;
	
	private boolean isQueueNoFound;
	
	private String inspectionStatus;
	private String proceedStatus;
	private boolean isProceedDataFound;

	public String getTransTypeCode() {
		return transTypeCode;
	}

	public void setTransTypeCode(String transTypeCode) {
		this.transTypeCode = transTypeCode;
	}

	public String getPermitNo() {
		return permitNo;
	}

	public void setPermitNo(String permitNo) {
		this.permitNo = permitNo;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public String getTenderRefNo() {
		return tenderRefNo;
	}

	public void setTenderRefNo(String tenderRefNo) {
		this.tenderRefNo = tenderRefNo;
	}

	public String getQueueService() {
		return queueService;
	}

	public void setQueueService(String queueService) {
		this.queueService = queueService;
	}

	public String getQueueNumber() {
		return queueNumber;
	}

	public void setQueueNumber(String queueNumber) {
		this.queueNumber = queueNumber;
	}

	public String getOrderSet() {
		return orderSet;
	}

	public void setOrderSet(String orderSet) {
		this.orderSet = orderSet;
	}

	public String getApplicationPmStatus() {
		return applicationPmStatus;
	}

	public void setApplicationPmStatus(String applicationPmStatus) {
		this.applicationPmStatus = applicationPmStatus;
	}

	public int getSkipCount() {
		return skipCount;
	}

	public void setSkipCount(int skipCount) {
		this.skipCount = skipCount;
	}

	public String getCounterNo() {
		return counterNo;
	}

	public void setCounterNo(String counterNo) {
		this.counterNo = counterNo;
	}

	public String getTokenNo() {
		return tokenNo;
	}

	public void setTokenNo(String tokenNo) {
		this.tokenNo = tokenNo;
	}

	public boolean isPermitInfoFound() {
		return isPermitInfoFound;
	}

	public void setPermitInfoFound(boolean isPermitInfoFound) {
		this.isPermitInfoFound = isPermitInfoFound;
	}

	public String getInspectionType() {
		return inspectionType;
	}

	public void setInspectionType(String inspectionType) {
		this.inspectionType = inspectionType;
	}

	public boolean isQueueNoFound() {
		return isQueueNoFound;
	}

	public void setQueueNoFound(boolean isQueueNoFound) {
		this.isQueueNoFound = isQueueNoFound;
	}

	public String getInspectionStatus() {
		return inspectionStatus;
	}

	public void setInspectionStatus(String inspectionStatus) {
		this.inspectionStatus = inspectionStatus;
	}

	public String getProceedStatus() {
		return proceedStatus;
	}

	public void setProceedStatus(String proceedStatus) {
		this.proceedStatus = proceedStatus;
	}

	public boolean isProceedDataFound() {
		return isProceedDataFound;
	}

	public void setProceedDataFound(boolean isProceedDataFound) {
		this.isProceedDataFound = isProceedDataFound;
	}
	
	

}
