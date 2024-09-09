package lk.informatics.ntc.model.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

public class PermitRenewalsDTO {
	private String permitNo;
	private String dateOne;
	private String renewalApplicationNo;
	private String queueNo;
	private String regNoOfBus;
	private String serviceTypeCode;
	private String serviceTypeDescription;
	private String placeOfOrginOfTheService;
	private String routeNo;
	private String placeOfDestination;
	private String permitExpiryDate;
	private String reminderLetterStatus;
	private int requestRenewPeriod;
	private String via;
	// private Date newPermitExpirtDate;
	private String newPermitExpirtDate;
	private String specialRemark;
	private String preferedLanguageDescription;
	private String preferedLanguageCode;
	private String titleCode;
	private String addressOne;
	private String genderCode;
	private String addressOneSinhala;
	private String dob;
	private String addressOneTamil;
	private String nic;
	private String addressTwo;
	private String addressTwoSinhala;
	private String addressTwoTamil;
	private String fullName;
	private String fullNameSinhala;
	private String fullNameTamil;
	private String city;
	private String citySinhala;
	private String cityTamil;
	private String nameWithInitials;
	private String teleNo;
	private String mobileNo;
	private String provinceCode;
	private String provinceDescription;
	private String districtCode;
	private String districtDescription;
	private String divisionalSecretariatDivision;
	private String divisionSectionDescription;
	private String materialStatusId;
	private String materialStatusDescription;
	private String applicationNo;
	private Long seqno;
	private String genderDescription;
	private String titleDescription;
	private Long ownerSeqNo;
	private String modifyBy;
	private String documentCode;
	private String documentDescription;
	private String remark;
	private boolean exists;
	private Long docSeqChecked;
	private String docFilePath;
	private boolean hasRecord;
	private String reciptNo;
	private String voucherNo;
	private String txnDate;
	private Double amount;
	private String displayAmount;
	private Long paymentVocherSeq;
	private String displayTxnDate;
	private String chargeTypeCode;
	private String chargeTypeDescription;
	private Double chargeTypeAmount;
	private Long voucherDetailsSeqNo;
	private String chargeTypeDisplayAmount;
	private String validToDate;
	private String fromToDate;
	private String inspectionDate1;
	private String inspectionDate2;
	private String BacklogAppValue;
	private boolean checkBacklogValue;
	private Date permitExpiredFromDateObj;
	private Date permitExpiredValidToDateObj;
	private boolean routeFlagChecked;
	private String routeFlageValue;
	private long taskSeq;
	private String taskCode;
	private String taskStatus;
	private String createBy;
	private Timestamp createDate;
	private String applicationTableQueueNo;
	private boolean mandatory;
	private boolean physicallyExists;
	private boolean disablephysicall;
	private String preSpecialRemark;
	private String modifyDate;
	private String latestPreAppNo;
	private String beforePermitExpiredDate;
	private Date newPermitExpirtDateObj;
	private String addressThree;
	private String addressThreeSinhala;
	private String addressThreeTamil;
	private Timestamp modifyByTimestamp;
	private String engineNo;
	private String chassisNo;
	private String permitIssueDateVal;
	private String permitIssueBy;
	private String tenderRefNo;
	private String status;
	private BigDecimal tenderAnualFee;
	private BigDecimal installment;
	private String permitPrint;
	private String rejectReason;
	private String validFromDate;
	private String approveBy;
	private String approveDate;
	private String isTenderPermit;
	private BigDecimal totalBusFare;
	private String inspectionDate;
	private String inspectionRemark;
	private String isNewPermit;
	private String reInspectionStatus;
	private String oldPermitNo;
	private String tempPermitNo;
	private String firstNotice;
	private String secondNotice;
	private String oldApplicationNo;
	private String isSkipInspection;
	private String thirdNotice;
	private String tenderFeeValidValue;
	private String statusDes;
	private String newPermitNo;
	private String paymentType;
	
	private String currentAttorneyFullName;
	private String currentAttorneyAddressOne;
	private String currentAttorneyAddressTwo;
	private String currentAttorneyCity;
	private String attorneyHolderStartDate;
	private String attorneyHolderEndDate;
	
	private String transtractionTypeCode;
    
	private String policyNo;
	private String insuCat;
	private Date insuExpDate;
	private String insuCompName;
	private Date revenueExpDate;
	private Date emissionExpDate;
	
	private String serialNo;
	private String garageRegNo;
	private Date fitnessCertiDate;
	private String  garageName;
	private Date  emmissionTestExpireDate;
	private Date expiryDateRevLicNew;
	
	

	public String getIsTenderPermit() {
		return isTenderPermit;
	}

	public void setIsTenderPermit(String isTenderPermit) {
		this.isTenderPermit = isTenderPermit;
	}

	public boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	public boolean isPhysicallyExists() {
		return physicallyExists;
	}

	public void setPhysicallyExists(boolean physicallyExists) {
		this.physicallyExists = physicallyExists;
	}

	public String getTitleDescription() {
		return titleDescription;
	}

	public void setTitleDescription(String titleDescription) {
		this.titleDescription = titleDescription;
	}

	public String getMaterialStatusId() {
		return materialStatusId;
	}

	public void setMaterialStatusId(String materialStatusId) {
		this.materialStatusId = materialStatusId;
	}

	public String getPermitNo() {
		return permitNo;
	}

	public void setPermitNo(String permitNo) {
		this.permitNo = permitNo;
	}

	public boolean isDisablephysicall() {
		return disablephysicall;
	}

	public void setDisablephysicall(boolean disablephysicall) {
		this.disablephysicall = disablephysicall;
	}

	public String getDateOne() {
		return dateOne;
	}

	public void setDateOne(String dateOne) {
		this.dateOne = dateOne;
	}

	public String getRenewalApplicationNo() {
		return renewalApplicationNo;
	}

	public void setRenewalApplicationNo(String renewalApplicationNo) {
		this.renewalApplicationNo = renewalApplicationNo;
	}

	public String getQueueNo() {
		return queueNo;
	}

	public void setQueueNo(String queueNo) {
		this.queueNo = queueNo;
	}

	public String getRegNoOfBus() {
		return regNoOfBus;
	}

	public void setRegNoOfBus(String regNoOfBus) {
		this.regNoOfBus = regNoOfBus;
	}

	public String getServiceTypeCode() {
		return serviceTypeCode;
	}

	public void setServiceTypeCode(String serviceTypeCode) {
		this.serviceTypeCode = serviceTypeCode;
	}

	public String getServiceTypeDescription() {
		return serviceTypeDescription;
	}

	public void setServiceTypeDescription(String serviceTypeDescription) {
		this.serviceTypeDescription = serviceTypeDescription;
	}

	public String getPlaceOfOrginOfTheService() {
		return placeOfOrginOfTheService;
	}

	public void setPlaceOfOrginOfTheService(String placeOfOrginOfTheService) {
		this.placeOfOrginOfTheService = placeOfOrginOfTheService;
	}

	public String getRouteNo() {
		return routeNo;
	}

	public void setRouteNo(String routeNo) {
		this.routeNo = routeNo;
	}

	public String getPlaceOfDestination() {
		return placeOfDestination;
	}

	public void setPlaceOfDestination(String placeOfDestination) {
		this.placeOfDestination = placeOfDestination;
	}

	public String getPermitExpiryDate() {
		return permitExpiryDate;
	}

	public void setPermitExpiryDate(String permitExpiryDate) {
		this.permitExpiryDate = permitExpiryDate;
	}

	public String getReminderLetterStatus() {
		return reminderLetterStatus;
	}

	public void setReminderLetterStatus(String reminderLetterStatus) {
		this.reminderLetterStatus = reminderLetterStatus;
	}

	public int getRequestRenewPeriod() {
		return requestRenewPeriod;
	}

	public void setRequestRenewPeriod(int requestRenewPeriod) {
		this.requestRenewPeriod = requestRenewPeriod;
	}

	// public Date getNewPermitExpirtDate() {
	// return newPermitExpirtDate;
	// }
	// public void setNewPermitExpirtDate(Date newPermitExpirtDate) {
	// this.newPermitExpirtDate = newPermitExpirtDate;
	// }
	public String getSpecialRemark() {
		return specialRemark;
	}

	public void setSpecialRemark(String specialRemark) {
		this.specialRemark = specialRemark;
	}

	public String getPreferedLanguageDescription() {
		return preferedLanguageDescription;
	}

	public void setPreferedLanguageDescription(String preferedLanguageDescription) {
		this.preferedLanguageDescription = preferedLanguageDescription;
	}

	public String getPreferedLanguageCode() {
		return preferedLanguageCode;
	}

	public void setPreferedLanguageCode(String preferedLanguageCode) {
		this.preferedLanguageCode = preferedLanguageCode;
	}

	public String getTitleCode() {
		return titleCode;
	}

	public void setTitleCode(String titleCode) {
		this.titleCode = titleCode;
	}

	public String getAddressOne() {
		return addressOne;
	}

	public void setAddressOne(String addressOne) {
		this.addressOne = addressOne;
	}

	public String getGenderCode() {
		return genderCode;
	}

	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}

	public String getAddressOneSinhala() {
		return addressOneSinhala;
	}

	public void setAddressOneSinhala(String addressOneSinhala) {
		this.addressOneSinhala = addressOneSinhala;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getAddressOneTamil() {
		return addressOneTamil;
	}

	public void setAddressOneTamil(String addressOneTamil) {
		this.addressOneTamil = addressOneTamil;
	}

	public String getNic() {
		return nic;
	}

	public void setNic(String nic) {
		this.nic = nic;
	}

	public String getAddressTwo() {
		return addressTwo;
	}

	public void setAddressTwo(String addressTwo) {
		this.addressTwo = addressTwo;
	}

	public String getAddressTwoSinhala() {
		return addressTwoSinhala;
	}

	public void setAddressTwoSinhala(String addressTwoSinhala) {
		this.addressTwoSinhala = addressTwoSinhala;
	}

	public String getAddressTwoTamil() {
		return addressTwoTamil;
	}

	public void setAddressTwoTamil(String addressTwoTamil) {
		this.addressTwoTamil = addressTwoTamil;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getFullNameSinhala() {
		return fullNameSinhala;
	}

	public void setFullNameSinhala(String fullNameSinhala) {
		this.fullNameSinhala = fullNameSinhala;
	}

	public String getFullNameTamil() {
		return fullNameTamil;
	}

	public void setFullNameTamil(String fullNameTamil) {
		this.fullNameTamil = fullNameTamil;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCitySinhala() {
		return citySinhala;
	}

	public void setCitySinhala(String citySinhala) {
		this.citySinhala = citySinhala;
	}

	public String getCityTamil() {
		return cityTamil;
	}

	public void setCityTamil(String cityTamil) {
		this.cityTamil = cityTamil;
	}

	public String getNameWithInitials() {
		return nameWithInitials;
	}

	public void setNameWithInitials(String nameWithInitials) {
		this.nameWithInitials = nameWithInitials;
	}

	public String getTeleNo() {
		return teleNo;
	}

	public void setTeleNo(String teleNo) {
		this.teleNo = teleNo;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	public String getDivisionalSecretariatDivision() {
		return divisionalSecretariatDivision;
	}

	public void setDivisionalSecretariatDivision(String divisionalSecretariatDivision) {
		this.divisionalSecretariatDivision = divisionalSecretariatDivision;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public Long getSeqno() {
		return seqno;
	}

	public void setSeqno(Long seqno) {
		this.seqno = seqno;
	}

	public String getProvinceDescription() {
		return provinceDescription;
	}

	public void setProvinceDescription(String provinceDescription) {
		this.provinceDescription = provinceDescription;
	}

	public String getDistrictDescription() {
		return districtDescription;
	}

	public void setDistrictDescription(String districtDescription) {
		this.districtDescription = districtDescription;
	}

	public String getMaterialStatusDescription() {
		return materialStatusDescription;
	}

	public void setMaterialStatusDescription(String materialStatusDescription) {
		this.materialStatusDescription = materialStatusDescription;
	}

	public String getDivisionSectionDescription() {
		return divisionSectionDescription;
	}

	public void setDivisionSectionDescription(String divisionSectionDescription) {
		this.divisionSectionDescription = divisionSectionDescription;
	}

	public String getGenderDescription() {
		return genderDescription;
	}

	public void setGenderDescription(String genderDescription) {
		this.genderDescription = genderDescription;
	}

	public Long getOwnerSeqNo() {
		return ownerSeqNo;
	}

	public void setOwnerSeqNo(Long ownerSeqNo) {
		this.ownerSeqNo = ownerSeqNo;
	}

	public String getModifyBy() {
		return modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}

	public String getDocumentCode() {
		return documentCode;
	}

	public void setDocumentCode(String documentCode) {
		this.documentCode = documentCode;
	}

	public String getDocumentDescription() {
		return documentDescription;
	}

	public void setDocumentDescription(String documentDescription) {
		this.documentDescription = documentDescription;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public boolean isExists() {
		return exists;
	}

	public void setExists(boolean exists) {
		this.exists = exists;
	}

	public Long getDocSeqChecked() {
		return docSeqChecked;
	}

	public void setDocSeqChecked(Long docSeqChecked) {
		this.docSeqChecked = docSeqChecked;
	}

	public String getDocFilePath() {
		return docFilePath;
	}

	public void setDocFilePath(String docFilePath) {
		this.docFilePath = docFilePath;
	}

	public boolean isHasRecord() {
		return hasRecord;
	}

	public void setHasRecord(boolean hasRecord) {
		this.hasRecord = hasRecord;
	}

	public String getReciptNo() {
		return reciptNo;
	}

	public void setReciptNo(String reciptNo) {
		this.reciptNo = reciptNo;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public String getTxnDate() {
		return txnDate;
	}

	public void setTxnDate(String txnDate) {
		this.txnDate = txnDate;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Long getPaymentVocherSeq() {
		return paymentVocherSeq;
	}

	public void setPaymentVocherSeq(Long paymentVocherSeq) {
		this.paymentVocherSeq = paymentVocherSeq;
	}

	public String getDisplayTxnDate() {
		return displayTxnDate;
	}

	public void setDisplayTxnDate(String displayTxnDate) {
		this.displayTxnDate = displayTxnDate;
	}

	public String getChargeTypeCode() {
		return chargeTypeCode;
	}

	public void setChargeTypeCode(String chargeTypeCode) {
		this.chargeTypeCode = chargeTypeCode;
	}

	public String getChargeTypeDescription() {
		return chargeTypeDescription;
	}

	public void setChargeTypeDescription(String chargeTypeDescription) {
		this.chargeTypeDescription = chargeTypeDescription;
	}

	public Double getChargeTypeAmount() {
		return chargeTypeAmount;
	}

	public void setChargeTypeAmount(Double chargeTypeAmount) {
		this.chargeTypeAmount = chargeTypeAmount;
	}

	public Long getVoucherDetailsSeqNo() {
		return voucherDetailsSeqNo;
	}

	public void setVoucherDetailsSeqNo(Long voucherDetailsSeqNo) {
		this.voucherDetailsSeqNo = voucherDetailsSeqNo;
	}

	public String getChargeTypeDisplayAmount() {
		return chargeTypeDisplayAmount;
	}

	public void setChargeTypeDisplayAmount(String chargeTypeDisplayAmount) {
		this.chargeTypeDisplayAmount = chargeTypeDisplayAmount;
	}

	public String getDisplayAmount() {
		return displayAmount;
	}

	public void setDisplayAmount(String displayAmount) {
		this.displayAmount = displayAmount;
	}

	public String getValidToDate() {
		return validToDate;
	}

	public void setValidToDate(String validToDate) {
		this.validToDate = validToDate;
	}

	public String getNewPermitExpirtDate() {
		return newPermitExpirtDate;
	}

	public void setNewPermitExpirtDate(String newPermitExpirtDate) {
		this.newPermitExpirtDate = newPermitExpirtDate;
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

	public String getFromToDate() {
		return fromToDate;
	}

	public void setFromToDate(String fromToDate) {
		this.fromToDate = fromToDate;
	}

	public String getBacklogAppValue() {
		return BacklogAppValue;
	}

	public void setBacklogAppValue(String backlogAppValue) {
		BacklogAppValue = backlogAppValue;
	}

	public boolean isCheckBacklogValue() {
		return checkBacklogValue;
	}

	public void setCheckBacklogValue(boolean checkBacklogValue) {
		this.checkBacklogValue = checkBacklogValue;
	}

	public Date getPermitExpiredFromDateObj() {
		return permitExpiredFromDateObj;
	}

	public void setPermitExpiredFromDateObj(Date permitExpiredFromDateObj) {
		this.permitExpiredFromDateObj = permitExpiredFromDateObj;
	}

	public Date getPermitExpiredValidToDateObj() {
		return permitExpiredValidToDateObj;
	}

	public void setPermitExpiredValidToDateObj(Date permitExpiredValidToDateObj) {
		this.permitExpiredValidToDateObj = permitExpiredValidToDateObj;
	}

	public boolean isRouteFlagChecked() {
		return routeFlagChecked;
	}

	public void setRouteFlagChecked(boolean routeFlagChecked) {
		this.routeFlagChecked = routeFlagChecked;
	}

	public String getRouteFlageValue() {
		return routeFlageValue;
	}

	public void setRouteFlageValue(String routeFlageValue) {
		this.routeFlageValue = routeFlageValue;
	}

	public long getTaskSeq() {
		return taskSeq;
	}

	public void setTaskSeq(long taskSeq) {
		this.taskSeq = taskSeq;
	}

	public String getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public String getApplicationTableQueueNo() {
		return applicationTableQueueNo;
	}

	public void setApplicationTableQueueNo(String applicationTableQueueNo) {
		this.applicationTableQueueNo = applicationTableQueueNo;
	}

	public String getPreSpecialRemark() {
		return preSpecialRemark;
	}

	public void setPreSpecialRemark(String preSpecialRemark) {
		this.preSpecialRemark = preSpecialRemark;
	}

	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getLatestPreAppNo() {
		return latestPreAppNo;
	}

	public void setLatestPreAppNo(String latestPreAppNo) {
		this.latestPreAppNo = latestPreAppNo;
	}

	public String getBeforePermitExpiredDate() {
		return beforePermitExpiredDate;
	}

	public void setBeforePermitExpiredDate(String beforePermitExpiredDate) {
		this.beforePermitExpiredDate = beforePermitExpiredDate;
	}

	public Date getNewPermitExpirtDateObj() {
		return newPermitExpirtDateObj;
	}

	public void setNewPermitExpirtDateObj(Date newPermitExpirtDateObj) {
		this.newPermitExpirtDateObj = newPermitExpirtDateObj;
	}

	public String getAddressThree() {
		return addressThree;
	}

	public void setAddressThree(String addressThree) {
		this.addressThree = addressThree;
	}

	public String getAddressThreeSinhala() {
		return addressThreeSinhala;
	}

	public void setAddressThreeSinhala(String addressThreeSinhala) {
		this.addressThreeSinhala = addressThreeSinhala;
	}

	public String getAddressThreeTamil() {
		return addressThreeTamil;
	}

	public void setAddressThreeTamil(String addressThreeTamil) {
		this.addressThreeTamil = addressThreeTamil;
	}

	public Timestamp getModifyByTimestamp() {
		return modifyByTimestamp;
	}

	public void setModifyByTimestamp(Timestamp modifyByTimestamp) {
		this.modifyByTimestamp = modifyByTimestamp;
	}

	public String getEngineNo() {
		return engineNo;
	}

	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	public String getChassisNo() {
		return chassisNo;
	}

	public void setChassisNo(String chassisNo) {
		this.chassisNo = chassisNo;
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

	public String getTenderRefNo() {
		return tenderRefNo;
	}

	public void setTenderRefNo(String tenderRefNo) {
		this.tenderRefNo = tenderRefNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getTenderAnualFee() {
		return tenderAnualFee;
	}

	public void setTenderAnualFee(BigDecimal tenderAnualFee) {
		this.tenderAnualFee = tenderAnualFee;
	}

	public BigDecimal getInstallment() {
		return installment;
	}

	public void setInstallment(BigDecimal installment) {
		this.installment = installment;
	}

	public String getPermitPrint() {
		return permitPrint;
	}

	public void setPermitPrint(String permitPrint) {
		this.permitPrint = permitPrint;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public String getValidFromDate() {
		return validFromDate;
	}

	public void setValidFromDate(String validFromDate) {
		this.validFromDate = validFromDate;
	}

	public String getApproveBy() {
		return approveBy;
	}

	public void setApproveBy(String approveBy) {
		this.approveBy = approveBy;
	}

	public String getApproveDate() {
		return approveDate;
	}

	public void setApproveDate(String approveDate) {
		this.approveDate = approveDate;
	}

	public BigDecimal getTotalBusFare() {
		return totalBusFare;
	}

	public void setTotalBusFare(BigDecimal totalBusFare) {
		this.totalBusFare = totalBusFare;
	}

	public String getInspectionDate() {
		return inspectionDate;
	}

	public void setInspectionDate(String inspectionDate) {
		this.inspectionDate = inspectionDate;
	}

	public String getInspectionRemark() {
		return inspectionRemark;
	}

	public void setInspectionRemark(String inspectionRemark) {
		this.inspectionRemark = inspectionRemark;
	}

	public String getIsNewPermit() {
		return isNewPermit;
	}

	public void setIsNewPermit(String isNewPermit) {
		this.isNewPermit = isNewPermit;
	}

	public String getReInspectionStatus() {
		return reInspectionStatus;
	}

	public void setReInspectionStatus(String reInspectionStatus) {
		this.reInspectionStatus = reInspectionStatus;
	}

	public String getOldPermitNo() {
		return oldPermitNo;
	}

	public void setOldPermitNo(String oldPermitNo) {
		this.oldPermitNo = oldPermitNo;
	}

	public String getTempPermitNo() {
		return tempPermitNo;
	}

	public void setTempPermitNo(String tempPermitNo) {
		this.tempPermitNo = tempPermitNo;
	}

	public String getFirstNotice() {
		return firstNotice;
	}

	public void setFirstNotice(String firstNotice) {
		this.firstNotice = firstNotice;
	}

	public String getSecondNotice() {
		return secondNotice;
	}

	public void setSecondNotice(String secondNotice) {
		this.secondNotice = secondNotice;
	}

	public String getOldApplicationNo() {
		return oldApplicationNo;
	}

	public void setOldApplicationNo(String oldApplicationNo) {
		this.oldApplicationNo = oldApplicationNo;
	}

	public String getIsSkipInspection() {
		return isSkipInspection;
	}

	public void setIsSkipInspection(String isSkipInspection) {
		this.isSkipInspection = isSkipInspection;
	}

	public String getThirdNotice() {
		return thirdNotice;
	}

	public void setThirdNotice(String thirdNotice) {
		this.thirdNotice = thirdNotice;
	}

	public String getVia() {
		return via;
	}

	public void setVia(String via) {
		this.via = via;
	}

	public String getTenderFeeValidValue() {
		return tenderFeeValidValue;
	}

	public void setTenderFeeValidValue(String tenderFeeValidValue) {
		this.tenderFeeValidValue = tenderFeeValidValue;
	}

	public String getStatusDes() {
		return statusDes;
	}

	public void setStatusDes(String statusDes) {
		this.statusDes = statusDes;
	}

	public String getNewPermitNo() {
		return newPermitNo;
	}

	public void setNewPermitNo(String newPermitNo) {
		this.newPermitNo = newPermitNo;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getCurrentAttorneyFullName() {
		return currentAttorneyFullName;
	}

	public void setCurrentAttorneyFullName(String currentAttorneyFullName) {
		this.currentAttorneyFullName = currentAttorneyFullName;
	}

	public String getCurrentAttorneyAddressOne() {
		return currentAttorneyAddressOne;
	}

	public void setCurrentAttorneyAddressOne(String currentAttorneyAddressOne) {
		this.currentAttorneyAddressOne = currentAttorneyAddressOne;
	}

	public String getCurrentAttorneyAddressTwo() {
		return currentAttorneyAddressTwo;
	}

	public void setCurrentAttorneyAddressTwo(String currentAttorneyAddressTwo) {
		this.currentAttorneyAddressTwo = currentAttorneyAddressTwo;
	}

	public String getCurrentAttorneyCity() {
		return currentAttorneyCity;
	}

	public void setCurrentAttorneyCity(String currentAttorneyCity) {
		this.currentAttorneyCity = currentAttorneyCity;
	}

	public String getAttorneyHolderStartDate() {
		return attorneyHolderStartDate;
	}

	public void setAttorneyHolderStartDate(String attorneyHolderStartDate) {
		this.attorneyHolderStartDate = attorneyHolderStartDate;
	}

	public String getAttorneyHolderEndDate() {
		return attorneyHolderEndDate;
	}

	public void setAttorneyHolderEndDate(String attorneyHolderEndDate) {
		this.attorneyHolderEndDate = attorneyHolderEndDate;
	}

	public String getTranstractionTypeCode() {
		return transtractionTypeCode;
	}

	public void setTranstractionTypeCode(String transtractionTypeCode) {
		this.transtractionTypeCode = transtractionTypeCode;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getInsuCat() {
		return insuCat;
	}

	public void setInsuCat(String insuCat) {
		this.insuCat = insuCat;
	}



	public Date getInsuExpDate() {
		return insuExpDate;
	}

	public void setInsuExpDate(Date insuExpDate) {
		this.insuExpDate = insuExpDate;
	}

	public Date getRevenueExpDate() {
		return revenueExpDate;
	}

	public void setRevenueExpDate(Date revenueExpDate) {
		this.revenueExpDate = revenueExpDate;
	}

	public Date getEmissionExpDate() {
		return emissionExpDate;
	}

	public void setEmissionExpDate(Date emissionExpDate) {
		this.emissionExpDate = emissionExpDate;
	}

	public String getInsuCompName() {
		return insuCompName;
	}

	public void setInsuCompName(String insuCompName) {
		this.insuCompName = insuCompName;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getGarageRegNo() {
		return garageRegNo;
	}

	public void setGarageRegNo(String garageRegNo) {
		this.garageRegNo = garageRegNo;
	}

	public Date getFitnessCertiDate() {
		return fitnessCertiDate;
	}

	public void setFitnessCertiDate(Date fitnessCertiDate) {
		this.fitnessCertiDate = fitnessCertiDate;
	}

	public String getGarageName() {
		return garageName;
	}

	public void setGarageName(String garageName) {
		this.garageName = garageName;
	}

	public Date getEmmissionTestExpireDate() {
		return emmissionTestExpireDate;
	}

	public void setEmmissionTestExpireDate(Date emmissionTestExpireDate) {
		this.emmissionTestExpireDate = emmissionTestExpireDate;
	}

	public Date getExpiryDateRevLicNew() {
		return expiryDateRevLicNew;
	}

	public void setExpiryDateRevLicNew(Date expiryDateRevLicNew) {
		this.expiryDateRevLicNew = expiryDateRevLicNew;
	}

	
   
}
