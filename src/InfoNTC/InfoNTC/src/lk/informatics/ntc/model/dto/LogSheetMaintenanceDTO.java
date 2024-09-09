package lk.informatics.ntc.model.dto;

import java.math.BigDecimal;

public class LogSheetMaintenanceDTO {
	
private long seq;
private String serviceType;
private String serviceNo;
private String referenceNo;
private String serviceCode;
private String serviceDescription;
private String nameOfOperator;
private String endOfStartDate;
private String serviceEndDate;
private String origin;
private String year;
private String month;
private String destination;
private String via;
private String status;
private String distance;
private String receivedDate;
private String logRefNo;

private int noOfTurns;
private int noOfTurnsinGPS;
private int logSheetDelay;
private boolean approvalBoolean;
private int averagePasengers;
private int schoolRequiredRunningDate;
private int lateTrips;
private String schoolApprovals;
private int arrears;	
private int deductions;
private String specialRemark;
private String logType;
private String paymentType;
private int running;
private int totalLength;
private int subsidy;
private int lateFee; 
private int penaltyFee;
private int payment;
private long logMasterSeq;
private BigDecimal voucherAmount;


private BigDecimal totalLengthD;
private BigDecimal lateFeeD; 
private BigDecimal paymentD;
private BigDecimal otherPercentageCalculationValD;
private BigDecimal penaltyFeeD;
private BigDecimal runningD;
private BigDecimal SubsidyD;
private BigDecimal requestedD;


private double otherPercentage;
private double otherPercentageCalculationVal;

private int tripsPerDay;
private int requested;

private String busNo;
private String reCalculateBy;

private String isChecked;

public String getLogRefNo() {
	return logRefNo;
}
public void setLogRefNo(String logRefNo) {
	this.logRefNo = logRefNo;
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
public String getDistance() {
	return distance;
}
public void setDistance(String distance) {
	this.distance = distance;
}

public String getYear() {
	return year;
}
public void setYear(String year) {
	this.year = year;
}
public String getMonth() {
	return month;
}
public void setMonth(String month) {
	this.month = month;
}
public String getNameOfOperator() {
	return nameOfOperator;
}
public void setNameOfOperator(String nameOfOperator) {
	this.nameOfOperator = nameOfOperator;
}
public String getEndOfStartDate() {
	return endOfStartDate;
}
public void setEndOfStartDate(String endOfStartDate) {
	this.endOfStartDate = endOfStartDate;
}
public String getServiceEndDate() {
	return serviceEndDate;
}
public void setServiceEndDate(String serviceEndDate) {
	this.serviceEndDate = serviceEndDate;
}

public String getServiceCode() {
	return serviceCode;
}
public void setServiceCode(String serviceCode) {
	this.serviceCode = serviceCode;
}
public String getServiceDescription() {
	return serviceDescription;
}
public void setServiceDescription(String serviceDescription) {
	this.serviceDescription = serviceDescription;
}
public String getServiceType() {
	return serviceType;
}
public void setServiceType(String serviceType) {
	this.serviceType = serviceType;
}
public String getServiceNo() {
	return serviceNo;
}
public void setServiceNo(String serviceNo) {
	this.serviceNo = serviceNo;
}
public String getReferenceNo() {
	return referenceNo;
}
public void setReferenceNo(String referenceNo) {
	this.referenceNo = referenceNo;
}
public String getReceivedDate() {
	return receivedDate;
}
public void setReceivedDate(String receivedDate) {
	this.receivedDate = receivedDate;
}
public int getNoOfTurns() {
	return noOfTurns;
}
public void setNoOfTurns(int noOfTurns) {
	this.noOfTurns = noOfTurns;
}
public int getNoOfTurnsinGPS() {
	return noOfTurnsinGPS;
}
public void setNoOfTurnsinGPS(int noOfTurnsinGPS) {
	this.noOfTurnsinGPS = noOfTurnsinGPS;
}
public int getLogSheetDelay() {
	return logSheetDelay;
}
public void setLogSheetDelay(int logSheetDelay) {
	this.logSheetDelay = logSheetDelay;
}
public int getAveragePasengers() {
	return averagePasengers;
}
public void setAveragePasengers(int averagePasengers) {
	this.averagePasengers = averagePasengers;
}
public int getSchoolRequiredRunningDate() {
	return schoolRequiredRunningDate;
}
public void setSchoolRequiredRunningDate(int schoolRequiredRunningDate) {
	this.schoolRequiredRunningDate = schoolRequiredRunningDate;
}
public int getLateTrips() {
	return lateTrips;
}
public void setLateTrips(int lateTrips) {
	this.lateTrips = lateTrips;
}
public String getSchoolApprovals() {
	return schoolApprovals;
}
public void setSchoolApprovals(String schoolApprovals) {
	this.schoolApprovals = schoolApprovals;
}
public int getArrears() {
	return arrears;
}
public void setArrears(int arrears) {
	this.arrears = arrears;
}
public int getDeductions() {
	return deductions;
}
public void setDeductions(int deductions) {
	this.deductions = deductions;
}
public String getSpecialRemark() {
	return specialRemark;
}
public void setSpecialRemark(String specialRemark) {
	this.specialRemark = specialRemark;
}

public String getLogType() {
	return logType;
}
public void setLogType(String logType) {
	this.logType = logType;
}
public String getPaymentType() {
	return paymentType;
}
public void setPaymentType(String paymentType) {
	this.paymentType = paymentType;
}
public int getRunning() {
	return running;
}
public void setRunning(int running) {
	this.running = running;
}
public int getTotalLength() {
	return totalLength;
}
public void setTotalLength(int totalLength) {
	this.totalLength = totalLength;
}
public int getSubsidy() {
	return subsidy;
}
public void setSubsidy(int subsidy) {
	this.subsidy = subsidy;
}
public int getLateFee() {
	return lateFee;
}
public void setLateFee(int lateFee) {
	this.lateFee = lateFee;
}
public int getPenaltyFee() {
	return penaltyFee;
}
public void setPenaltyFee(int penaltyFee) {
	this.penaltyFee = penaltyFee;
}
public int getPayment() {
	return payment;
}
public void setPayment(int payment) {
	this.payment = payment;
}
public boolean isApprovalBoolean() {
	return approvalBoolean;
}
public void setApprovalBoolean(boolean approvalBoolean) {
	this.approvalBoolean = approvalBoolean;
}
public long getSeq() {
	return seq;
}
public void setSeq(long seq) {
	this.seq = seq;
}
public long getLogMasterSeq() {
	return logMasterSeq;
}
public void setLogMasterSeq(long logMasterSeq) {
	this.logMasterSeq = logMasterSeq;
}
public BigDecimal getVoucherAmount() {
	return voucherAmount;
}
public void setVoucherAmount(BigDecimal voucherAmount) {
	this.voucherAmount = voucherAmount;
}
public double getOtherPercentage() {
	return otherPercentage;
}
public void setOtherPercentage(double otherPercentage) {
	this.otherPercentage = otherPercentage;
}
public double getOtherPercentageCalculationVal() {
	return otherPercentageCalculationVal;
}
public void setOtherPercentageCalculationVal(double otherPercentageCalculationVal) {
	this.otherPercentageCalculationVal = otherPercentageCalculationVal;
}
public int getTripsPerDay() {
	return tripsPerDay;
}
public void setTripsPerDay(int tripsPerDay) {
	this.tripsPerDay = tripsPerDay;
}
public int getRequested() {
	return requested;
}
public void setRequested(int requested) {
	this.requested = requested;
}
public BigDecimal getTotalLengthD() {
	return totalLengthD;
}
public void setTotalLengthD(BigDecimal totalLengthD) {
	this.totalLengthD = totalLengthD;
}
public BigDecimal getLateFeeD() {
	return lateFeeD;
}
public void setLateFeeD(BigDecimal lateFeeD) {
	this.lateFeeD = lateFeeD;
}
public BigDecimal getPaymentD() {
	return paymentD;
}
public void setPaymentD(BigDecimal paymentD) {
	this.paymentD = paymentD;
}
public BigDecimal getOtherPercentageCalculationValD() {
	return otherPercentageCalculationValD;
}
public void setOtherPercentageCalculationValD(BigDecimal otherPercentageCalculationValD) {
	this.otherPercentageCalculationValD = otherPercentageCalculationValD;
}
public BigDecimal getPenaltyFeeD() {
	return penaltyFeeD;
}
public void setPenaltyFeeD(BigDecimal penaltyFeeD) {
	this.penaltyFeeD = penaltyFeeD;
}
public BigDecimal getRunningD() {
	return runningD;
}
public void setRunningD(BigDecimal runningD) {
	this.runningD = runningD;
}
public BigDecimal getSubsidyD() {
	return SubsidyD;
}
public void setSubsidyD(BigDecimal subsidyD) {
	SubsidyD = subsidyD;
}
public BigDecimal getRequestedD() {
	return requestedD;
}
public void setRequestedD(BigDecimal requestedD) {
	this.requestedD = requestedD;
}
public String getBusNo() {
	return busNo;
}
public void setBusNo(String busNo) {
	this.busNo = busNo;
}
public String getReCalculateBy() {
	return reCalculateBy;
}
public void setReCalculateBy(String reCalculateBy) {
	this.reCalculateBy = reCalculateBy;
}
public String getIsChecked() {
	return isChecked;
}
public void setIsChecked(String isChecked) {
	this.isChecked = isChecked;
}



}
