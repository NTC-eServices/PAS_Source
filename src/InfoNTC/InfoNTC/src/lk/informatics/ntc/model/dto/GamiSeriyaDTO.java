package lk.informatics.ntc.model.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

public class GamiSeriyaDTO {
	private String seqNo;
	private String requestNo;
	private String preferedLanguage;
	private Date requestDate;
	private Date requestDate2;
	private String requestortype;
	private String requestorCode;
	private String idNo;
	private String address1;
	private String nameinFull;
	private String address1sinhala;
	private String nameinFullSinhala;
	private String address1Tamil;
	private String nameinFullTamil;
	private String address2;
	private String telephoneNo;
	private String address2Sinhala;
	private String mobileNo;
	private String address2Tamil;
	private String email;
	private String city;
	private String citySinhala;
	private String cityTamil;

	// request route information
	private String origin;
	private String destination;
	private String originSinhala;
	private String destinationSinhala;
	private String OriginTamil;
	private String destinationTamil;
	private String via;
	private String province;
	private String viaSinhala;
	private String viaTamil;
	private double unservedPotion;

	// pathum
	private String surveyRequestNo;
	private Date requestDateObj;

	private String organizationCode;
	private String organizationDesc;

	private String departmentCode;
	private String departmentDesc;

	private String requestTypeCode;
	private String requestTypeDesk;

	private String routeSeqNo;

	private String originCode;
	private String originDesc;

	private String destinationCode;
	private String destinationDesc;

	private String provinceCode;
	private String provinceDesc;

	private double totalLength;
	private double unservedPortion;

	private boolean routeSelected;

	private String reasonForSurvey;

	private String surviceTypeCode;
	private String surviceTypeDesc;

	private String surviceMethodCode;
	private String surviceMethodDesc;

	private String remark;
	private String surveyNo;

	private String approveProcessRemark;
	private String status;
	private String statusDesc;

	// gami perit holder information

	private String tenderRefNo;
	private String serviceRefNo;
	private String serviceNo;
	private String permitNo;
	private String busNo;
	private String sequenceNo;

	private BigDecimal agreedAmount;

	private Date serviceStartDate;
	private Date serviceEndDate;

	private String serviceTypeCode;
	private String serviceTypeDesc;

	private String districtCode;
	private String districtDesc;

	private String divisionalSecCode;
	private String divisionalSecDesc;
	private double distance;

	private String accountNo;

	private String bankNameCode;
	private String bankNameDesc;

	private String bankBranchNameCode;
	private String bankBranchNameDesc;

	private String subCityType;
	private int noOfPermits;

	private String aprroveRejectMark;

	private String strServiceEndDate;
	private String strServiceStartDate;

	// Log Sheet
	private Date requestStartDate;
	private Date requestEndDate;
	private String requestStartDateString;
	private String requestEndDateString;
	private String gpsStatus;
	private Date serviceAgreementIssuedDate;
	private Date permitStickerIssuedDate;
	private Date logSheetsIssuedDate;
	private String gpsStatusCode;
	private String serviceAgreementIssuedBy;
	private String permitStickerIssuedBy;
	private String logSheetsIssuedBy;
	private boolean issuedServiceAgreement;
	private boolean issuedPermitSticker;
	private boolean issuedLogSheets;
	private String currentUser;
	private int issueType;
	
	private Long seqNum;
	private String nicNo;
	private String createdBy;
	private Timestamp cratedDate;
	private String modifiedBy;
	private Timestamp modifiedDate;
	private String approveRejectedBy;
	private Timestamp approveRejectDate;
	private String rejectReason;
	private String serviceRenewalStatus;
	private Date newExpiryDateObj;
	private String newExpiryDateVal;
	private String issueServiceAgreementVal;
	private String issuePermitStickerVal;
	private String issueLogSheetVal;
	private String serviceAgrrementIssuedDateVal;
	private String permitStickerIssuedDateVal;
	private String logsheetIssuedDateVal;
	private String is_sltbCode;
	private String issueLogSheetsEditedVal;
	private String issueServiceAgreementEdited;
	private String issuePermitStickerEdited;
	private Date serviceStartDateObj;
	private Date serviceEndDateObj;
	
	
	
	public Date getServiceStartDateObj() {
		return serviceStartDateObj;
	}

	public void setServiceStartDateObj(Date serviceStartDateObj) {
		this.serviceStartDateObj = serviceStartDateObj;
	}

	public Date getServiceEndDateObj() {
		return serviceEndDateObj;
	}

	public void setServiceEndDateObj(Date serviceEndDateObj) {
		this.serviceEndDateObj = serviceEndDateObj;
	}

	public String getPermitStickerIssuedDateVal() {
		return permitStickerIssuedDateVal;
	}

	public void setPermitStickerIssuedDateVal(String permitStickerIssuedDateVal) {
		this.permitStickerIssuedDateVal = permitStickerIssuedDateVal;
	}

	public String getLogsheetIssuedDateVal() {
		return logsheetIssuedDateVal;
	}

	public void setLogsheetIssuedDateVal(String logsheetIssuedDateVal) {
		this.logsheetIssuedDateVal = logsheetIssuedDateVal;
	}

	public String getIs_sltbCode() {
		return is_sltbCode;
	}

	public void setIs_sltbCode(String is_sltbCode) {
		this.is_sltbCode = is_sltbCode;
	}

	public String getIssueLogSheetsEditedVal() {
		return issueLogSheetsEditedVal;
	}

	public void setIssueLogSheetsEditedVal(String issueLogSheetsEditedVal) {
		this.issueLogSheetsEditedVal = issueLogSheetsEditedVal;
	}

	public String getIssueServiceAgreementEdited() {
		return issueServiceAgreementEdited;
	}

	public void setIssueServiceAgreementEdited(String issueServiceAgreementEdited) {
		this.issueServiceAgreementEdited = issueServiceAgreementEdited;
	}

	public String getIssuePermitStickerEdited() {
		return issuePermitStickerEdited;
	}

	public void setIssuePermitStickerEdited(String issuePermitStickerEdited) {
		this.issuePermitStickerEdited = issuePermitStickerEdited;
	}

	public String getServiceAgrrementIssuedDateVal() {
		return serviceAgrrementIssuedDateVal;
	}

	public void setServiceAgrrementIssuedDateVal(String serviceAgrrementIssuedDateVal) {
		this.serviceAgrrementIssuedDateVal = serviceAgrrementIssuedDateVal;
	}

	public String getIssueServiceAgreementVal() {
		return issueServiceAgreementVal;
	}

	public void setIssueServiceAgreementVal(String issueServiceAgreementVal) {
		this.issueServiceAgreementVal = issueServiceAgreementVal;
	}

	public String getIssuePermitStickerVal() {
		return issuePermitStickerVal;
	}

	public void setIssuePermitStickerVal(String issuePermitStickerVal) {
		this.issuePermitStickerVal = issuePermitStickerVal;
	}

	public String getIssueLogSheetVal() {
		return issueLogSheetVal;
	}

	public void setIssueLogSheetVal(String issueLogSheetVal) {
		this.issueLogSheetVal = issueLogSheetVal;
	}

	public Date getNewExpiryDateObj() {
		return newExpiryDateObj;
	}

	public void setNewExpiryDateObj(Date newExpiryDateObj) {
		this.newExpiryDateObj = newExpiryDateObj;
	}

	public String getNewExpiryDateVal() {
		return newExpiryDateVal;
	}

	public void setNewExpiryDateVal(String newExpiryDateVal) {
		this.newExpiryDateVal = newExpiryDateVal;
	}

	public String getServiceRenewalStatus() {
		return serviceRenewalStatus;
	}

	public void setServiceRenewalStatus(String serviceRenewalStatus) {
		this.serviceRenewalStatus = serviceRenewalStatus;
	}

	public boolean isIssuedServiceAgreement() {
		return issuedServiceAgreement;
	}

	public void setIssuedServiceAgreement(boolean issuedServiceAgreement) {
		this.issuedServiceAgreement = issuedServiceAgreement;
	}

	public boolean isIssuedPermitSticker() {
		return issuedPermitSticker;
	}

	public void setIssuedPermitSticker(boolean issuedPermitSticker) {
		this.issuedPermitSticker = issuedPermitSticker;
	}

	public boolean isIssuedLogSheets() {
		return issuedLogSheets;
	}

	public void setIssuedLogSheets(boolean issuedLogSheets) {
		this.issuedLogSheets = issuedLogSheets;
	}

	public String getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}

	public String getPreferedLanguage() {
		return preferedLanguage;
	}

	public void setPreferedLanguage(String preferedLanguage) {
		this.preferedLanguage = preferedLanguage;
	}

	public String getRequestortype() {
		return requestortype;
	}

	public void setRequestortype(String requestortype) {
		this.requestortype = requestortype;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getNameinFull() {
		return nameinFull;
	}

	public void setNameinFull(String nameinFull) {
		this.nameinFull = nameinFull;
	}

	public String getAddress1sinhala() {
		return address1sinhala;
	}

	public void setAddress1sinhala(String address1sinhala) {
		this.address1sinhala = address1sinhala;
	}

	public String getNameinFullSinhala() {
		return nameinFullSinhala;
	}

	public void setNameinFullSinhala(String nameinFullSinhala) {
		this.nameinFullSinhala = nameinFullSinhala;
	}

	public String getAddress1Tamil() {
		return address1Tamil;
	}

	public void setAddress1Tamil(String address1Tamil) {
		this.address1Tamil = address1Tamil;
	}

	public String getNameinFullTamil() {
		return nameinFullTamil;
	}

	public void setNameinFullTamil(String nameinFullTamil) {
		this.nameinFullTamil = nameinFullTamil;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getTelephoneNo() {
		return telephoneNo;
	}

	public void setTelephoneNo(String telephoneNo) {
		this.telephoneNo = telephoneNo;
	}

	public String getAddress2Sinhala() {
		return address2Sinhala;
	}

	public void setAddress2Sinhala(String address2Sinhala) {
		this.address2Sinhala = address2Sinhala;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getAddress2Tamil() {
		return address2Tamil;
	}

	public void setAddress2Tamil(String address2Tamil) {
		this.address2Tamil = address2Tamil;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getOriginSinhala() {
		return originSinhala;
	}

	public void setOriginSinhala(String originSinhala) {
		this.originSinhala = originSinhala;
	}

	public String getDestinationSinhala() {
		return destinationSinhala;
	}

	public void setDestinationSinhala(String destinationSinhala) {
		this.destinationSinhala = destinationSinhala;
	}

	public String getOriginTamil() {
		return OriginTamil;
	}

	public void setOriginTamil(String originTamil) {
		OriginTamil = originTamil;
	}

	public String getDestinationTamil() {
		return destinationTamil;
	}

	public void setDestinationTamil(String destinationTamil) {
		this.destinationTamil = destinationTamil;
	}

	public String getVia() {
		return via;
	}

	public void setVia(String via) {
		this.via = via;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getViaSinhala() {
		return viaSinhala;
	}

	public void setViaSinhala(String viaSinhala) {
		this.viaSinhala = viaSinhala;
	}

	public String getViaTamil() {
		return viaTamil;
	}

	public void setViaTamil(String viaTamil) {
		this.viaTamil = viaTamil;
	}

	public String getRequestorCode() {
		return requestorCode;
	}

	public void setRequestorCode(String requestorCode) {
		this.requestorCode = requestorCode;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public void setTotalLength(Double totalLength) {
		this.totalLength = totalLength;
	}

	public String getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}

	public Date getRequestDate2() {
		return requestDate2;
	}

	public void setRequestDate2(Date requestDate2) {
		this.requestDate2 = requestDate2;
	}

	public String getSurveyRequestNo() {
		return surveyRequestNo;
	}

	public void setSurveyRequestNo(String surveyRequestNo) {
		this.surveyRequestNo = surveyRequestNo;
	}

	public Date getRequestDateObj() {
		return requestDateObj;
	}

	public void setRequestDateObj(Date requestDateObj) {
		this.requestDateObj = requestDateObj;
	}

	public String getOrganizationCode() {
		return organizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	public String getOrganizationDesc() {
		return organizationDesc;
	}

	public void setOrganizationDesc(String organizationDesc) {
		this.organizationDesc = organizationDesc;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public String getDepartmentDesc() {
		return departmentDesc;
	}

	public void setDepartmentDesc(String departmentDesc) {
		this.departmentDesc = departmentDesc;
	}

	public String getRequestTypeCode() {
		return requestTypeCode;
	}

	public void setRequestTypeCode(String requestTypeCode) {
		this.requestTypeCode = requestTypeCode;
	}

	public String getRequestTypeDesk() {
		return requestTypeDesk;
	}

	public void setRequestTypeDesk(String requestTypeDesk) {
		this.requestTypeDesk = requestTypeDesk;
	}

	public String getRouteSeqNo() {
		return routeSeqNo;
	}

	public void setRouteSeqNo(String routeSeqNo) {
		this.routeSeqNo = routeSeqNo;
	}

	public String getOriginCode() {
		return originCode;
	}

	public void setOriginCode(String originCode) {
		this.originCode = originCode;
	}

	public String getOriginDesc() {
		return originDesc;
	}

	public void setOriginDesc(String originDesc) {
		this.originDesc = originDesc;
	}

	public String getDestinationCode() {
		return destinationCode;
	}

	public void setDestinationCode(String destinationCode) {
		this.destinationCode = destinationCode;
	}

	public String getDestinationDesc() {
		return destinationDesc;
	}

	public void setDestinationDesc(String destinationDesc) {
		this.destinationDesc = destinationDesc;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getProvinceDesc() {
		return provinceDesc;
	}

	public void setProvinceDesc(String provinceDesc) {
		this.provinceDesc = provinceDesc;
	}

	public double getTotalLength() {
		return totalLength;
	}

	public void setTotalLength(double totalLength) {
		this.totalLength = totalLength;
	}

	public double getUnservedPortion() {
		return unservedPortion;
	}

	public void setUnservedPortion(double unservedPortion) {
		this.unservedPortion = unservedPortion;
	}

	public boolean isRouteSelected() {
		return routeSelected;
	}

	public void setRouteSelected(boolean routeSelected) {
		this.routeSelected = routeSelected;
	}

	public String getReasonForSurvey() {
		return reasonForSurvey;
	}

	public void setReasonForSurvey(String reasonForSurvey) {
		this.reasonForSurvey = reasonForSurvey;
	}

	public String getSurviceTypeCode() {
		return surviceTypeCode;
	}

	public void setSurviceTypeCode(String surviceTypeCode) {
		this.surviceTypeCode = surviceTypeCode;
	}

	public String getSurviceTypeDesc() {
		return surviceTypeDesc;
	}

	public void setSurviceTypeDesc(String surviceTypeDesc) {
		this.surviceTypeDesc = surviceTypeDesc;
	}

	public String getSurviceMethodCode() {
		return surviceMethodCode;
	}

	public void setSurviceMethodCode(String surviceMethodCode) {
		this.surviceMethodCode = surviceMethodCode;
	}

	public String getSurviceMethodDesc() {
		return surviceMethodDesc;
	}

	public void setSurviceMethodDesc(String surviceMethodDesc) {
		this.surviceMethodDesc = surviceMethodDesc;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSurveyNo() {
		return surveyNo;
	}

	public void setSurveyNo(String surveyNo) {
		this.surveyNo = surveyNo;
	}

	public String getApproveProcessRemark() {
		return approveProcessRemark;
	}

	public void setApproveProcessRemark(String approveProcessRemark) {
		this.approveProcessRemark = approveProcessRemark;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTenderRefNo() {
		return tenderRefNo;
	}

	public void setTenderRefNo(String tenderRefNo) {
		this.tenderRefNo = tenderRefNo;
	}

	public String getServiceRefNo() {
		return serviceRefNo;
	}

	public void setServiceRefNo(String serviceRefNo) {
		this.serviceRefNo = serviceRefNo;
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

	public String getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(String sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	public BigDecimal getAgreedAmount() {
		return agreedAmount;
	}

	public void setAgreedAmount(BigDecimal agreedAmount) {
		this.agreedAmount = agreedAmount;
	}

	public Date getServiceStartDate() {
		return serviceStartDate;
	}

	public void setServiceStartDate(Date serviceStartDate) {
		this.serviceStartDate = serviceStartDate;
	}

	public Date getServiceEndDate() {
		return serviceEndDate;
	}

	public void setServiceEndDate(Date serviceEndDate) {
		this.serviceEndDate = serviceEndDate;
	}

	public String getServiceTypeCode() {
		return serviceTypeCode;
	}

	public void setServiceTypeCode(String serviceTypeCode) {
		this.serviceTypeCode = serviceTypeCode;
	}

	public String getServiceTypeDesc() {
		return serviceTypeDesc;
	}

	public void setServiceTypeDesc(String serviceTypeDesc) {
		this.serviceTypeDesc = serviceTypeDesc;
	}

	public double getUnservedPotion() {
		return unservedPotion;
	}

	public void setUnservedPotion(double unservedPotion) {
		this.unservedPotion = unservedPotion;
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	public String getDistrictDesc() {
		return districtDesc;
	}

	public void setDistrictDesc(String districtDesc) {
		this.districtDesc = districtDesc;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public String getDivisionalSecCode() {
		return divisionalSecCode;
	}

	public void setDivisionalSecCode(String divisionalSecCode) {
		this.divisionalSecCode = divisionalSecCode;
	}

	public String getDivisionalSecDesc() {
		return divisionalSecDesc;
	}

	public void setDivisionalSecDesc(String divisionalSecDesc) {
		this.divisionalSecDesc = divisionalSecDesc;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getBankNameCode() {
		return bankNameCode;
	}

	public void setBankNameCode(String bankNameCode) {
		this.bankNameCode = bankNameCode;
	}

	public String getBankNameDesc() {
		return bankNameDesc;
	}

	public void setBankNameDesc(String bankNameDesc) {
		this.bankNameDesc = bankNameDesc;
	}

	public String getBankBranchNameCode() {
		return bankBranchNameCode;
	}

	public void setBankBranchNameCode(String bankBranchNameCode) {
		this.bankBranchNameCode = bankBranchNameCode;
	}

	public String getBankBranchNameDesc() {
		return bankBranchNameDesc;
	}

	public void setBankBranchNameDesc(String bankBranchNameDesc) {
		this.bankBranchNameDesc = bankBranchNameDesc;
	}

	public String getServiceNo() {
		return serviceNo;
	}

	public void setServiceNo(String serviceNo) {
		this.serviceNo = serviceNo;
	}

	public String getSubCityType() {
		return subCityType;
	}

	public void setSubCityType(String subCityType) {
		this.subCityType = subCityType;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public int getNoOfPermits() {
		return noOfPermits;
	}

	public void setNoOfPermits(int noOfPermits) {
		this.noOfPermits = noOfPermits;
	}

	public String getAprroveRejectMark() {
		return aprroveRejectMark;
	}

	public void setAprroveRejectMark(String aprroveRejectMark) {
		this.aprroveRejectMark = aprroveRejectMark;
	}

	public String getStrServiceEndDate() {
		return strServiceEndDate;
	}

	public void setStrServiceEndDate(String strServiceEndDate) {
		this.strServiceEndDate = strServiceEndDate;
	}

	public String getStrServiceStartDate() {
		return strServiceStartDate;
	}

	public void setStrServiceStartDate(String strServiceStartDate) {
		this.strServiceStartDate = strServiceStartDate;
	}

	public Date getRequestStartDate() {
		return requestStartDate;
	}

	public void setRequestStartDate(Date requestStartDate) {
		this.requestStartDate = requestStartDate;
	}

	public Date getRequestEndDate() {
		return requestEndDate;
	}

	public void setRequestEndDate(Date requestEndDate) {
		this.requestEndDate = requestEndDate;
	}

	public String getRequestStartDateString() {
		return requestStartDateString;
	}

	public void setRequestStartDateString(String requestStartDateString) {
		this.requestStartDateString = requestStartDateString;
	}

	public String getRequestEndDateString() {
		return requestEndDateString;
	}

	public void setRequestEndDateString(String requestEndDateString) {
		this.requestEndDateString = requestEndDateString;
	}

	public String getGpsStatus() {
		return gpsStatus;
	}

	public void setGpsStatus(String gpsStatus) {
		this.gpsStatus = gpsStatus;
	}

	public Date getServiceAgreementIssuedDate() {
		return serviceAgreementIssuedDate;
	}

	public void setServiceAgreementIssuedDate(Date serviceAgreementIssuedDate) {
		this.serviceAgreementIssuedDate = serviceAgreementIssuedDate;
	}

	public Date getPermitStickerIssuedDate() {
		return permitStickerIssuedDate;
	}

	public void setPermitStickerIssuedDate(Date permitStickerIssuedDate) {
		this.permitStickerIssuedDate = permitStickerIssuedDate;
	}

	public Date getLogSheetsIssuedDate() {
		return logSheetsIssuedDate;
	}

	public void setLogSheetsIssuedDate(Date logSheetsIssuedDate) {
		this.logSheetsIssuedDate = logSheetsIssuedDate;
	}

	public String getGpsStatusCode() {
		return gpsStatusCode;
	}

	public void setGpsStatusCode(String gpsStatusCode) {
		this.gpsStatusCode = gpsStatusCode;
	}

	public String getServiceAgreementIssuedBy() {
		return serviceAgreementIssuedBy;
	}

	public void setServiceAgreementIssuedBy(String serviceAgreementIssuedBy) {
		this.serviceAgreementIssuedBy = serviceAgreementIssuedBy;
	}

	public String getPermitStickerIssuedBy() {
		return permitStickerIssuedBy;
	}

	public void setPermitStickerIssuedBy(String permitStickerIssuedBy) {
		this.permitStickerIssuedBy = permitStickerIssuedBy;
	}

	public String getLogSheetsIssuedBy() {
		return logSheetsIssuedBy;
	}

	public void setLogSheetsIssuedBy(String logSheetsIssuedBy) {
		this.logSheetsIssuedBy = logSheetsIssuedBy;
	}

	public String getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}

	public int getIssueType() {
		return issueType;
	}

	public void setIssueType(int issueType) {
		this.issueType = issueType;
	}

	public Long getSeqNum() {
		return seqNum;
	}

	public void setSeqNum(Long seqNum) {
		this.seqNum = seqNum;
	}

	public String getNicNo() {
		return nicNo;
	}

	public void setNicNo(String nicNo) {
		this.nicNo = nicNo;
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

	public String getApproveRejectedBy() {
		return approveRejectedBy;
	}

	public void setApproveRejectedBy(String approveRejectedBy) {
		this.approveRejectedBy = approveRejectedBy;
	}

	public Timestamp getApproveRejectDate() {
		return approveRejectDate;
	}

	public void setApproveRejectDate(Timestamp approveRejectDate) {
		this.approveRejectDate = approveRejectDate;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

}
