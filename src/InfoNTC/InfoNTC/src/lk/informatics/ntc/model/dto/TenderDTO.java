package lk.informatics.ntc.model.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

public class TenderDTO {

	private String tenderRefNo;
	private String tenderDes;
	private String trafficProposalNo;
	private String time;
	private String titleDescription;
	private Timestamp closeDate;
	private int serialNo;
	private String titleCode;
	private String routeNo;	
	private String via;
	private String typeOfServices;
	private BigDecimal oneWayBusFare;
	private int turnsPerDay;
	private BigDecimal treasureHolderPrice;
	private String parallelRoads;
	private String effectiveRoutes;
	private String tenderStatus;
	private String tenderApproveStatus;
	private String surveyNo;
	private String tenderAppNo;
	private String tenderAppType;
	private String tenderAppTypeCode;
	private String tenderQueueNo;
	private String title;
	private String applicantName;
	private String addressOne;
	private String addressTwo;
	private String city;
	private String nicNo;
	private String contactNo;
	private String voucherNo;
	private String applicationRevPerson;
	private String recPersonIdTypeDescription;
	private String recPersonIdTypeCode;
	private String recPersonIdNo;
	private String remark;
	private int noOfPermits;
	private String departure;
	private String arrival;
	private String slNumber;
	private Timestamp publishDate;
	private String postPoneReason;
	private Date newTenderDate;
	private String stringCloseDate;
	private String newTenderDateString;
	private String stringPublishDate;
	private String test;
	private String specialRemark;
	private BigDecimal bidPrice;
	private String transType;
	private String date;
	private String refundvoucherNo;
	private String accountNo;
	private BigDecimal amount;
	private String voucherRemark;
	private String transCode;
	private String iniCloseDate;
	private BigDecimal treasureHolderPricePercentage;
	private boolean savedAgreementPrintedCount=false;
	private String province;
	private String totalLength;
	private int letterCountString;
	private String tenderApplicationStatus;
	

	// Print Agreement
	private String tenderBankSlipRefNo;
		private boolean agreementIssuedCheck;

	public TenderDTO(String slNumber, String routeNo, String via, String typeOfServices, BigDecimal oneWayBusFare,
			int turnsPerDay, BigDecimal treasureHolderPrice, String parallelRoads, String effectiveRoutes,
			int noOfPermits, String departure, String arrival) {

		this.slNumber = slNumber;
		this.routeNo = routeNo;
		this.via = via;
		this.typeOfServices = typeOfServices;
		this.oneWayBusFare = oneWayBusFare;
		this.turnsPerDay = turnsPerDay;
		this.treasureHolderPrice = treasureHolderPrice;
		this.parallelRoads = parallelRoads;
		this.effectiveRoutes = effectiveRoutes;
		this.noOfPermits = noOfPermits;
		this.departure = departure;
		this.arrival = arrival;
	}

	private String tenClosingDate;
	private String tenClosingTime;
	
	private Date tenderClosinDate;
	private Date tenderClosinTime;
	
	
	

	public String getTitleDescription() {
		return titleDescription;
	}

	public String getTitleCode() {
		return titleCode;
	}

	public String getTest() {
		return test;
	}



	public String getTotalLength() {
		return totalLength;
	}

	public void setTotalLength(String totalLength) {
		this.totalLength = totalLength;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getTransCode() {
		return transCode;
	}

	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}

	public String getVoucherRemark() {
		return voucherRemark;
	}

	public void setVoucherRemark(String voucherRemark) {
		this.voucherRemark = voucherRemark;
	}


	

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getRefundvoucherNo() {
		return refundvoucherNo;
	}

	public void setRefundvoucherNo(String refundvoucherNo) {
		this.refundvoucherNo = refundvoucherNo;
	}
	
	

	public String getAccountNo() {
		return accountNo;
	}

	
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public BigDecimal getBidPrice() {
		return bidPrice;
	}

	public void setBidPrice(BigDecimal bidPrice) {
		this.bidPrice = bidPrice;
	}

	public String getSpecialRemark() {
		return specialRemark;
	}

	public void setSpecialRemark(String specialRemark) {
		this.specialRemark = specialRemark;
	}

	public void setTest(String test) {
		this.test = test;
	}

	public void setTitleCode(String titleCode) {
		this.titleCode = titleCode;
	}

	public void setTitleDescription(String titleDescription) {
		this.titleDescription = titleDescription;
	}

	public String getSlNumber() {
		return slNumber;
	}

	public void setSlNumber(String slNumber) {
		this.slNumber = slNumber;
	}
	
	

	public String getDeparture() {
		return departure;
	}

	public void setDeparture(String departure) {
		this.departure = departure;
	}

	public String getArrival() {
		return arrival;
	}

	public String getTenderAppTypeCode() {
		return tenderAppTypeCode;
	}

	public void setTenderAppTypeCode(String tenderAppTypeCode) {
		this.tenderAppTypeCode = tenderAppTypeCode;
	}

	public void setArrival(String arrival) {
		this.arrival = arrival;
	}

	public int getNoOfPermits() {
		return noOfPermits;
	}

	public void setNoOfPermits(int noOfPermits) {
		this.noOfPermits = noOfPermits;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public String getApplicationRevPerson() {
		return applicationRevPerson;
	}

	public void setApplicationRevPerson(String applicationRevPerson) {
		this.applicationRevPerson = applicationRevPerson;
	}

	public String getRecPersonIdTypeDescription() {
		return recPersonIdTypeDescription;
	}

	public void setRecPersonIdTypeDescription(String recPersonIdTypeDescription) {
		this.recPersonIdTypeDescription = recPersonIdTypeDescription;
	}

	public String getRecPersonIdTypeCode() {
		return recPersonIdTypeCode;
	}

	public void setRecPersonIdTypeCode(String recPersonIdTypeCode) {
		this.recPersonIdTypeCode = recPersonIdTypeCode;
	}

	public String getStringCloseDate() {
		return stringCloseDate;
	}

	public void setStringCloseDate(String stringCloseDate) {
		this.stringCloseDate = stringCloseDate;
	}

	public String getRecPersonIdNo() {
		return recPersonIdNo;
	}

	public void setRecPersonIdNo(String recPersonIdNo) {
		this.recPersonIdNo = recPersonIdNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getNicNo() {
		return nicNo;
	}

	public void setNicNo(String nicNo) {
		this.nicNo = nicNo;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddressTwo() {
		return addressTwo;
	}

	public void setAddressTwo(String addressTwo) {
		this.addressTwo = addressTwo;
	}

	public String getAddressOne() {
		return addressOne;
	}

	public void setAddressOne(String addressOne) {
		this.addressOne = addressOne;
	}

	public String getApplicantName() {
		return applicantName;
	}

	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTenderQueueNo() {
		return tenderQueueNo;
	}

	public void setTenderQueueNo(String tenderQueueNo) {
		this.tenderQueueNo = tenderQueueNo;
	}

	public String getTenderAppType() {
		return tenderAppType;
	}

	public void setTenderAppType(String tenderAppType) {
		this.tenderAppType = tenderAppType;
	}

	public String getTenderAppNo() {
		return tenderAppNo;
	}

	public void setTenderAppNo(String tenderAppNo) {
		this.tenderAppNo = tenderAppNo;
	}

	public TenderDTO() {
	}

	public String getSurveyNo() {
		return surveyNo;
	}

	public void setSurveyNo(String surveyNo) {
		this.surveyNo = surveyNo;
	}

	/*************/
	public String getTenClosingDate() {
		return tenClosingDate;
	}

	public void setTenClosingDate(String tenClosingDate) {
		this.tenClosingDate = tenClosingDate;
	}

	public String getTenClosingTime() {
		return tenClosingTime;
	}

	public void setTenClosingTime(String tenClosingTime) {
		this.tenClosingTime = tenClosingTime;
	}

	/*************/

	public String getTenderRefNo() {
		return tenderRefNo;
	}

	public void setTenderRefNo(String tenderRefNo) {
		this.tenderRefNo = tenderRefNo;
	}

	public String getTenderDes() {
		return tenderDes;
	}

	public void setTenderDes(String tenderDes) {
		this.tenderDes = tenderDes;
	}

	public String getTrafficProposalNo() {
		return trafficProposalNo;
	}

	public void setTrafficProposalNo(String trafficProposalNo) {
		this.trafficProposalNo = trafficProposalNo;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Timestamp getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Timestamp closeDate) {
		this.closeDate = closeDate;
	}

	public int getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
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

	public String getTypeOfServices() {
		return typeOfServices;
	}

	public void setTypeOfServices(String typeOfServices) {
		this.typeOfServices = typeOfServices;
	}

	public BigDecimal getOneWayBusFare() {
		return oneWayBusFare;
	}

	public void setOneWayBusFare(BigDecimal oneWayBusFare) {
		this.oneWayBusFare = oneWayBusFare;
	}

	public int getTurnsPerDay() {
		return turnsPerDay;
	}

	public void setTurnsPerDay(int turnsPerDay) {
		this.turnsPerDay = turnsPerDay;
	}

	public BigDecimal getTreasureHolderPrice() {
		return treasureHolderPrice;
	}

	public void setTreasureHolderPrice(BigDecimal treasureHolderPrice) {
		this.treasureHolderPrice = treasureHolderPrice;
	}

	public String getParallelRoads() {
		return parallelRoads;
	}

	public void setParallelRoads(String parallelRoads) {
		this.parallelRoads = parallelRoads;
	}

	public String getEffectiveRoutes() {
		return effectiveRoutes;
	}

	public void setEffectiveRoutes(String effectiveRoutes) {
		this.effectiveRoutes = effectiveRoutes;
	}

	public String getTenderStatus() {
		return tenderStatus;
	}

	public void setTenderStatus(String tenderStatus) {
		this.tenderStatus = tenderStatus;
	}

	public String getTenderApproveStatus() {
		return tenderApproveStatus;
	}

	public void setTenderApproveStatus(String tenderApproveStatus) {
		this.tenderApproveStatus = tenderApproveStatus;
	}

	public Timestamp getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Timestamp publishDate) {
		this.publishDate = publishDate;
	}

	public String getPostPoneReason() {
		return postPoneReason;
	}

	public void setPostPoneReason(String postPoneReason) {
		this.postPoneReason = postPoneReason;
	}

	public Date getNewTenderDate() {
		return newTenderDate;
	}

	public void setNewTenderDate(Date newTenderDate) {
		this.newTenderDate = newTenderDate;
	}

	public String getTenderBankSlipRefNo() {
		return tenderBankSlipRefNo;
	}

	public void setTenderBankSlipRefNo(String tenderBankSlipRefNo) {
		this.tenderBankSlipRefNo = tenderBankSlipRefNo;
	}

	public boolean isAgreementIssuedCheck() {
		return agreementIssuedCheck;
	}

	public void setAgreementIssuedCheck(boolean agreementIssuedCheck) {
		this.agreementIssuedCheck = agreementIssuedCheck;
	}
	public String getNewTenderDateString() {
		return newTenderDateString;
	}

	public void setNewTenderDateString(String newTenderDateString) {
		this.newTenderDateString = newTenderDateString;
	}

	public String getStringPublishDate() {
		return stringPublishDate;
	}

	public void setStringPublishDate(String stringPublishDate) {
		this.stringPublishDate = stringPublishDate;
	}

	public String getIniCloseDate() {
		return iniCloseDate;
	}

	public void setIniCloseDate(String iniCloseDate) {
		this.iniCloseDate = iniCloseDate;
	}

	public BigDecimal getTreasureHolderPricePercentage() {
		return treasureHolderPricePercentage;
	}

	public void setTreasureHolderPricePercentage(BigDecimal treasureHolderPricePercentage) {
		this.treasureHolderPricePercentage = treasureHolderPricePercentage;
	}

	public boolean isSavedAgreementPrintedCount() {
		return savedAgreementPrintedCount;
	}

	public void setSavedAgreementPrintedCount(boolean savedAgreementPrintedCount) {
		this.savedAgreementPrintedCount = savedAgreementPrintedCount;
	}



	public int getLetterCountString() {
		return letterCountString;
	}

	public void setLetterCountString(int letterCountString) {
		this.letterCountString = letterCountString;
	}

	public String getTenderApplicationStatus() {
		return tenderApplicationStatus;
	}

	public void setTenderApplicationStatus(String tenderApplicationStatus) {
		this.tenderApplicationStatus = tenderApplicationStatus;
	}

	public Date getTenderClosinDate() {
		return tenderClosinDate;
	}

	public void setTenderClosinDate(Date tenderClosinDate) {
		this.tenderClosinDate = tenderClosinDate;
	}

	public Date getTenderClosinTime() {
		return tenderClosinTime;
	}

	public void setTenderClosinTime(Date tenderClosinTime) {
		this.tenderClosinTime = tenderClosinTime;
	}
	
	
	
	
}
