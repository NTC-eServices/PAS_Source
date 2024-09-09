package lk.informatics.ntc.model.dto;

import java.io.Serializable;

public class WithoutFixedTimeValidationDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean success;
	private String error;
	private int noOfLeavesOrigin;
	private int noOfLeavesDestination;
	private int noOfLeavesOriginSec;
	private int noOfLeavesDestinationSec;
	private int remainOriginBuses;
	private int remainDestBuses;
	private int allbusesOrigin;
	private int allBusesDestination;
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public int getNoOfLeavesOrigin() {
		return noOfLeavesOrigin;
	}
	public void setNoOfLeavesOrigin(int noOfLeavesOrigin) {
		this.noOfLeavesOrigin = noOfLeavesOrigin;
	}
	public int getNoOfLeavesDestination() {
		return noOfLeavesDestination;
	}
	public void setNoOfLeavesDestination(int noOfLeavesDestination) {
		this.noOfLeavesDestination = noOfLeavesDestination;
	}
	public int getNoOfLeavesOriginSec() {
		return noOfLeavesOriginSec;
	}
	public void setNoOfLeavesOriginSec(int noOfLeavesOriginSec) {
		this.noOfLeavesOriginSec = noOfLeavesOriginSec;
	}
	public int getNoOfLeavesDestinationSec() {
		return noOfLeavesDestinationSec;
	}
	public void setNoOfLeavesDestinationSec(int noOfLeavesDestinationSec) {
		this.noOfLeavesDestinationSec = noOfLeavesDestinationSec;
	}
	public int getRemainOriginBuses() {
		return remainOriginBuses;
	}
	public void setRemainOriginBuses(int remainOriginBuses) {
		this.remainOriginBuses = remainOriginBuses;
	}
	public int getRemainDestBuses() {
		return remainDestBuses;
	}
	public void setRemainDestBuses(int remainDestBuses) {
		this.remainDestBuses = remainDestBuses;
	}
	public int getAllbusesOrigin() {
		return allbusesOrigin;
	}
	public void setAllbusesOrigin(int allbusesOrigin) {
		this.allbusesOrigin = allbusesOrigin;
	}
	public int getAllBusesDestination() {
		return allBusesDestination;
	}
	public void setAllBusesDestination(int allBusesDestination) {
		this.allBusesDestination = allBusesDestination;
	}
	
	
}
