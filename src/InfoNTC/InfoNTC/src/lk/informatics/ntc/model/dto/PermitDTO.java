package lk.informatics.ntc.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

public class PermitDTO implements Serializable {
	private static final long serialVersionUID = 8344243187243931903L;

	private long seq;
	private String permitNo;
	private String busRegNo;
	private String serviceType;
	private String routeNo;
	private String origin;
	private String destination;
	private String via;
	private Date permitEndDate;
	private Date permitExpire;
	private Date permitIssueDate;
	private BigDecimal busFare;
	private String isTenderPermit;
	private String isBacklogApp;
	private String applicationNo;
	private String status;
	private String createdBy;
	private Timestamp cratedDate;
	private String modifiedBy;
	private Timestamp modifiedDate;
	private String strPermitEndDate;
	private String strExpireDate;
	private String queueNo;
	private String validTo;
	// added
	private Date permitDate;
	private String expirDate;
	private String vehicleOwner;
	private String tenderRefNo;
	private String rejectReason;
	private int rePeriod;
	private String userId;
	private Date startDate;
	private Date endDate;
	private long taskSeq;
	private String taskCode;
	private String remarks;
	private String appdate;
	private Date newExpirDate;
	private String newPermitExpireDate;
	private String routeFlag;
	private String cancelReason;
	private String taskStatus;
	private String cancelType;
	private String cancelStatus;
	private boolean temCancellation;
	private java.sql.Date cancelTO;
	private java.sql.Date cancelFrom;
	private String tempcancelReason;
	private boolean temporary;
	private boolean checkBoxClicked;
	private Date ct;
	private Date cf;
	private String letterStatus;
	private String expireReason;
	private String specialRemark;
	private String organizationName;
	private String telephoneNo;
	private String nicNo;
	private String ownerGender;
	private String mobileNo;
	private String addressOne;
	private String province;
	private String district;
	private String addressTwo;
	private String city;
	private String division;
	private String newExpiryDateVal;
	private String functionCode;
	private String activityCode;
	private String permitIssueDateVal;
	private String permitIssueBy;
	private String inspectDateVal;
	private BigDecimal tenderAnnualFee;
	private BigDecimal installments;
	private String isPermitPrint;
	private String validFromDateVal;
	private String validToDateVal;
	private String approvedBy;
	private String approvedDateVal;
	private BigDecimal totBusFare;
	private String permitExpiredDateVal;
	private int renewalPeriod;
	private String nextInsDateSec1_2Val;
	private String pmNextInsDateSec3Val;
	private String inspectionRemark;
	private String isNewPermit;
	private String reInspectStatus;
	private String oldPermitNo;
	private String tempPermitNo;
	private String isFirstNotice;
	private String isSecondNotice;
	private String isThirdNotice;
	private String oldApplicationNo;
	private String depot;
	private String serviceTypeDesc;
	
	
	public String getOldApplicationNo() {
		return oldApplicationNo;
	}

	public void setOldApplicationNo(String oldApplicationNo) {
		this.oldApplicationNo = oldApplicationNo;
	}

	public String getIsFirstNotice() {
		return isFirstNotice;
	}

	public void setIsFirstNotice(String isFirstNotice) {
		this.isFirstNotice = isFirstNotice;
	}

	public String getIsSecondNotice() {
		return isSecondNotice;
	}

	public void setIsSecondNotice(String isSecondNotice) {
		this.isSecondNotice = isSecondNotice;
	}

	public String getIsThirdNotice() {
		return isThirdNotice;
	}

	public void setIsThirdNotice(String isThirdNotice) {
		this.isThirdNotice = isThirdNotice;
	}

	public String getTempPermitNo() {
		return tempPermitNo;
	}

	public void setTempPermitNo(String tempPermitNo) {
		this.tempPermitNo = tempPermitNo;
	}

	public String getOldPermitNo() {
		return oldPermitNo;
	}

	public void setOldPermitNo(String oldPermitNo) {
		this.oldPermitNo = oldPermitNo;
	}

	public String getReInspectStatus() {
		return reInspectStatus;
	}

	public void setReInspectStatus(String reInspectStatus) {
		this.reInspectStatus = reInspectStatus;
	}

	public String getIsNewPermit() {
		return isNewPermit;
	}

	public void setIsNewPermit(String isNewPermit) {
		this.isNewPermit = isNewPermit;
	}

	public String getInspectionRemark() {
		return inspectionRemark;
	}

	public void setInspectionRemark(String inspectionRemark) {
		this.inspectionRemark = inspectionRemark;
	}

	public String getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getAddressTwo() {
		return addressTwo;
	}

	public void setAddressTwo(String addressTwo) {
		this.addressTwo = addressTwo;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getAddressOne() {
		return addressOne;
	}

	public void setAddressOne(String addressOne) {
		this.addressOne = addressOne;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getNicNo() {
		return nicNo;
	}

	public void setNicNo(String nicNo) {
		this.nicNo = nicNo;
	}

	public String getTelephoneNo() {
		return telephoneNo;
	}

	public void setTelephoneNo(String telephoneNo) {
		this.telephoneNo = telephoneNo;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getSpecialRemark() {
		return specialRemark;
	}

	public void setSpecialRemark(String specialRemark) {
		this.specialRemark = specialRemark;
	}

	public String getExpireReason() {
		return expireReason;
	}

	public void setExpireReason(String expireReason) {
		this.expireReason = expireReason;
	}

	public String getLetterStatus() {
		return letterStatus;
	}

	public void setLetterStatus(String letterStatus) {
		this.letterStatus = letterStatus;
	}

	public Date getCt() {
		return ct;
	}

	public void setCt(Date ct) {
		this.ct = ct;
	}

	public Date getCf() {
		return cf;
	}

	public void setCf(Date cf) {
		this.cf = cf;
	}

	public boolean isCheckBoxClicked() {
		return checkBoxClicked;
	}

	public void setCheckBoxClicked(boolean checkBoxClicked) {
		this.checkBoxClicked = checkBoxClicked;
	}

	public boolean isTemporary() {
		return temporary;
	}

	public void setTemporary(boolean temporary) {
		this.temporary = temporary;
	}

	public String getTempcancelReason() {
		return tempcancelReason;
	}

	public void setTempcancelReason(String tempcancelReason) {
		this.tempcancelReason = tempcancelReason;
	}

	public java.sql.Date getCancelTO() {
		return cancelTO;
	}

	public void setCancelTO(java.sql.Date cancelTO) {
		this.cancelTO = cancelTO;
	}

	public java.sql.Date getCancelFrom() {
		return cancelFrom;
	}

	public void setCancelFrom(java.sql.Date cancelFrom) {
		this.cancelFrom = cancelFrom;
	}

	public boolean isTemCancellation() {
		return temCancellation;
	}

	public void setTemCancellation(boolean temCancellation) {
		this.temCancellation = temCancellation;
	}

	public String getCancelType() {
		return cancelType;
	}

	public void setCancelType(String cancelType) {
		this.cancelType = cancelType;
	}

	public String getCancelStatus() {
		return cancelStatus;
	}

	public void setCancelStatus(String cancelStatus) {
		this.cancelStatus = cancelStatus;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

	public Date getPermitIssueDate() {
		return permitIssueDate;
	}

	public void setPermitIssueDate(Date permitIssueDate) {
		this.permitIssueDate = permitIssueDate;
	}

	public String getNewPermitExpireDate() {
		return newPermitExpireDate;
	}

	public void setNewPermitExpireDate(String newPermitExpireDate) {
		this.newPermitExpireDate = newPermitExpireDate;
	}

	public Date getNewExpirDate() {
		return newExpirDate;
	}

	public void setNewExpirDate(Date newExpirDate) {
		this.newExpirDate = newExpirDate;
	}

	public String getValidTo() {
		return validTo;
	}

	public void setValidTo(String validTo) {
		this.validTo = validTo;
	}

	public String getAppdate() {
		return appdate;
	}

	public void setAppdate(String appdate) {
		this.appdate = appdate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}

	public long getTaskSeq() {
		return taskSeq;
	}

	public void setTaskSeq(long taskSeq) {
		this.taskSeq = taskSeq;
	}

	public int getRePeriod() {
		return rePeriod;
	}

	public void setRePeriod(int rePeriod) {
		this.rePeriod = rePeriod;
	}
	
	

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public String getVehicleOwner() {
		return vehicleOwner;
	}

	public void setVehicleOwner(String vehicleOwner) {
		this.vehicleOwner = vehicleOwner;
	}

	public String getExpirDate() {
		return expirDate;
	}

	public void setExpirDate(String expirDate) {
		this.expirDate = expirDate;
	}

	public long getSeq() {
		return seq;
	}

	public void setSeq(long seq) {
		this.seq = seq;
	}

	public String getQueueNo() {
		return queueNo;
	}

	public void setQueueNo(String queueNo) {
		this.queueNo = queueNo;
	}

	public String getPermitNo() {
		return permitNo;
	}

	public void setPermitNo(String permitNo) {
		this.permitNo = permitNo;
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

	public Date getPermitEndDate() {
		return permitEndDate;
	}

	public void setPermitEndDate(Date permitEndDate) {
		this.permitEndDate = permitEndDate;
	}

	public Date getPermitExpire() {
		return permitExpire;
	}

	public void setPermitExpire(Date permitExpire) {
		this.permitExpire = permitExpire;
	}

	public BigDecimal getBusFare() {
		return busFare;
	}

	public void setBusFare(BigDecimal busFare) {
		this.busFare = busFare;
	}

	public String getIsTenderPermit() {
		return isTenderPermit;
	}

	public void setIsTenderPermit(String isTenderPermit) {
		this.isTenderPermit = isTenderPermit;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsBacklogApp() {
		return isBacklogApp;
	}

	public void setIsBacklogApp(String isBacklogApp) {
		this.isBacklogApp = isBacklogApp;
	}

	public String getStrPermitEndDate() {
		return strPermitEndDate;
	}

	public void setStrPermitEndDate(String strPermitEndDate) {
		this.strPermitEndDate = strPermitEndDate;
	}

	public String getStrExpireDate() {
		return strExpireDate;
	}

	public void setStrExpireDate(String strExpireDate) {
		this.strExpireDate = strExpireDate;
	}

	public Date getPermitDate() {
		return permitDate;
	}

	public void setPermitDate(Date permitDate) {
		this.permitDate = permitDate;
	}

	public String getTenderRefNo() {
		return tenderRefNo;
	}

	public void setTenderRefNo(String tenderRefNo) {
		this.tenderRefNo = tenderRefNo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getRouteFlag() {
		return routeFlag;
	}

	public void setRouteFlag(String routeFlag) {
		this.routeFlag = routeFlag;
	}

	public String getNewExpiryDateVal() {
		return newExpiryDateVal;
	}

	public void setNewExpiryDateVal(String newExpiryDateVal) {
		this.newExpiryDateVal = newExpiryDateVal;
	}

	public String getPermitIssueDateVal() {
		return permitIssueDateVal;
	}

	public void setPermitIssueDateVal(String permitIssueDateVal) {
		this.permitIssueDateVal = permitIssueDateVal;
	}

	public String getPermitIssueBy() {
		return permitIssueBy;
	}

	public void setPermitIssueBy(String permitIssueBy) {
		this.permitIssueBy = permitIssueBy;
	}

	public String getInspectDateVal() {
		return inspectDateVal;
	}

	public void setInspectDateVal(String inspectDateVal) {
		this.inspectDateVal = inspectDateVal;
	}

	public BigDecimal getTenderAnnualFee() {
		return tenderAnnualFee;
	}

	public void setTenderAnnualFee(BigDecimal tenderAnnualFee) {
		this.tenderAnnualFee = tenderAnnualFee;
	}

	public BigDecimal getInstallments() {
		return installments;
	}

	public void setInstallments(BigDecimal installments) {
		this.installments = installments;
	}

	public String getIsPermitPrint() {
		return isPermitPrint;
	}

	public void setIsPermitPrint(String isPermitPrint) {
		this.isPermitPrint = isPermitPrint;
	}

	public String getValidFromDateVal() {
		return validFromDateVal;
	}

	public void setValidFromDateVal(String validFromDateVal) {
		this.validFromDateVal = validFromDateVal;
	}

	public String getValidToDateVal() {
		return validToDateVal;
	}

	public void setValidToDateVal(String validToDateVal) {
		this.validToDateVal = validToDateVal;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public String getApprovedDateVal() {
		return approvedDateVal;
	}

	public void setApprovedDateVal(String approvedDateVal) {
		this.approvedDateVal = approvedDateVal;
	}

	public BigDecimal getTotBusFare() {
		return totBusFare;
	}

	public void setTotBusFare(BigDecimal totBusFare) {
		this.totBusFare = totBusFare;
	}

	public String getPermitExpiredDateVal() {
		return permitExpiredDateVal;
	}

	public void setPermitExpiredDateVal(String permitExpiredDateVal) {
		this.permitExpiredDateVal = permitExpiredDateVal;
	}

	public int getRenewalPeriod() {
		return renewalPeriod;
	}

	public void setRenewalPeriod(int renewalPeriod) {
		this.renewalPeriod = renewalPeriod;
	}

	public String getNextInsDateSec1_2Val() {
		return nextInsDateSec1_2Val;
	}

	public void setNextInsDateSec1_2Val(String nextInsDateSec1_2Val) {
		this.nextInsDateSec1_2Val = nextInsDateSec1_2Val;
	}

	public String getPmNextInsDateSec3Val() {
		return pmNextInsDateSec3Val;
	}

	public void setPmNextInsDateSec3Val(String pmNextInsDateSec3Val) {
		this.pmNextInsDateSec3Val = pmNextInsDateSec3Val;
	}

	public String getOwnerGender() {
		return ownerGender;
	}

	public void setOwnerGender(String ownerGender) {
		this.ownerGender = ownerGender;
	}

	public String getDepot() {
		return depot;
	}

	public void setDepot(String depot) {
		this.depot = depot;
	}

	public String getServiceTypeDesc() {
		return serviceTypeDesc;
	}

	public void setServiceTypeDesc(String serviceTypeDesc) {
		this.serviceTypeDesc = serviceTypeDesc;
	}
	
}
