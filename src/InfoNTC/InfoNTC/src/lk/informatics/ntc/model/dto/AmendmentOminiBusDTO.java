package lk.informatics.ntc.model.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

public class AmendmentOminiBusDTO {
	private String tenderRefNo;
	private Date currentDate;
	private String applicationNo;
	private String queueNo;
	private String serviceType;
	private String serviceTypeDesc;
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
	private String fullName;
	private String address1;
	private String address2;
	private String expireDate;
	private boolean tenderPermit;
	private String finalRemark;
	private String perferedLanguage;
	private String title;
	private String gender;
	private Date dob;
	private String nicNo;
	private String fullNameSinhala;
	private String fullNameTamil;
	private String nameWithInitials;
	private String maritalStatus;
	private String telephoneNo;
	private String mobileNo;
	private String address1Sinhala;
	private String address1Tamil;
	private String address2Sinhala;
	private String address2Tamil;
	private String address3;
	private String address3Sinhala;
	private String address3Tamil;
	private String city;
	private String citySinhala;
	private String cityTamil;
	private String province;
	private String district;
	private String divSec;
	private Timestamp cratedDate;
	private String strStringDob;
	private int permitPeroid;
	private String trnType;

	private String vehicleRegNo;
	private String engineNo;
	private String chassisNo;
	private String make;
	private String model;
	private String manufactureDate;
	private String strStringManufactureDate;
	private Date registrationDate;
	private String strStringRegistrationDate;
	private String seating;
	private String noofDoors;
	private String height;
	private String weight;

	// Fitness Certificate

	private String serialNo;
	private String garageRegNo;
	private Date fitnessCertiDate;
	private String garageName;

	// Insurance Cover Details

	private String insuCompName;
	private Date insuExpDate;
	private String insuCat;
	private String policyNo;

	// Loan Details

	private BigDecimal priceValOfBus;
	private BigDecimal bankLoan;
	private String isLoanObtained;
	private BigDecimal dueAmount;
	private String financeCompany;
	private Date dateObtained;
	private String lapsedInstall;
	private Date expiryDateRevLic;
	private Date dateOfFirstReg;
	private Date expiryDateEmissionTest;

	private String remarks;

	private String relationshipWithTransferor;
	private String relationshipWithTransferorRemarks;

	private String reasonForOwnerChange;
	

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

	public String getRouteFlag() {
		return routeFlag;
	}

	public void setRouteFlag(String routeFlag) {
		this.routeFlag = routeFlag;
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

	public int getTrips() {
		return trips;
	}

	public void setTrips(int trips) {
		this.trips = trips;
	}

	public String getParkingPlace() {
		return parkingPlace;
	}

	public void setParkingPlace(String parkingPlace) {
		this.parkingPlace = parkingPlace;
	}

	public BigDecimal getDistanceParkingOrigin() {
		return distanceParkingOrigin;
	}

	public void setDistanceParkingOrigin(BigDecimal distanceParkingOrigin) {
		this.distanceParkingOrigin = distanceParkingOrigin;
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

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
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

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	public boolean isTenderPermit() {
		return tenderPermit;
	}

	public void setTenderPermit(boolean tenderPermit) {
		this.tenderPermit = tenderPermit;
	}

	public String getFinalRemark() {
		return finalRemark;
	}

	public void setFinalRemark(String finalRemark) {
		this.finalRemark = finalRemark;
	}

	public String getPerferedLanguage() {
		return perferedLanguage;
	}

	public void setPerferedLanguage(String perferedLanguage) {
		this.perferedLanguage = perferedLanguage;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getNicNo() {
		return nicNo;
	}

	public void setNicNo(String nicNo) {
		this.nicNo = nicNo;
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

	public String getNameWithInitials() {
		return nameWithInitials;
	}

	public void setNameWithInitials(String nameWithInitials) {
		this.nameWithInitials = nameWithInitials;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getTelephoneNo() {
		return telephoneNo;
	}

	public void setTelephoneNo(String telephoneNo) {
		this.telephoneNo = telephoneNo;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getAddress1Sinhala() {
		return address1Sinhala;
	}

	public void setAddress1Sinhala(String address1Sinhala) {
		this.address1Sinhala = address1Sinhala;
	}

	public String getAddress1Tamil() {
		return address1Tamil;
	}

	public void setAddress1Tamil(String address1Tamil) {
		this.address1Tamil = address1Tamil;
	}

	public String getAddress2Sinhala() {
		return address2Sinhala;
	}

	public void setAddress2Sinhala(String address2Sinhala) {
		this.address2Sinhala = address2Sinhala;
	}

	public String getAddress2Tamil() {
		return address2Tamil;
	}

	public void setAddress2Tamil(String address2Tamil) {
		this.address2Tamil = address2Tamil;
	}

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public String getAddress3Sinhala() {
		return address3Sinhala;
	}

	public void setAddress3Sinhala(String address3Sinhala) {
		this.address3Sinhala = address3Sinhala;
	}

	public String getAddress3Tamil() {
		return address3Tamil;
	}

	public void setAddress3Tamil(String address3Tamil) {
		this.address3Tamil = address3Tamil;
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

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getDivSec() {
		return divSec;
	}

	public void setDivSec(String divSec) {
		this.divSec = divSec;
	}

	public Timestamp getCratedDate() {
		return cratedDate;
	}

	public void setCratedDate(Timestamp cratedDate) {
		this.cratedDate = cratedDate;
	}

	public String getStrStringDob() {
		return strStringDob;
	}

	public void setStrStringDob(String strStringDob) {
		this.strStringDob = strStringDob;
	}

	public int getPermitPeroid() {
		return permitPeroid;
	}

	public void setPermitPeroid(int permitPeroid) {
		this.permitPeroid = permitPeroid;
	}

	public String getRelationshipWithTransferor() {
		return relationshipWithTransferor;
	}

	public void setRelationshipWithTransferor(String relationshipWithTransferor) {
		this.relationshipWithTransferor = relationshipWithTransferor;
	}

	public String getRelationshipWithTransferorRemarks() {
		return relationshipWithTransferorRemarks;
	}

	public void setRelationshipWithTransferorRemarks(String relationshipWithTransferorRemarks) {
		this.relationshipWithTransferorRemarks = relationshipWithTransferorRemarks;
	}

	public String getTrnType() {
		return trnType;
	}

	public void setTrnType(String trnType) {
		this.trnType = trnType;
	}

	public String getReasonForOwnerChange() {
		return reasonForOwnerChange;
	}

	public void setReasonForOwnerChange(String reasonForOwnerChange) {
		this.reasonForOwnerChange = reasonForOwnerChange;
	}

	public String getVehicleRegNo() {
		return vehicleRegNo;
	}

	public void setVehicleRegNo(String vehicleRegNo) {
		this.vehicleRegNo = vehicleRegNo;
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

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getManufactureDate() {
		return manufactureDate;
	}

	public void setManufactureDate(String manufactureDate) {
		this.manufactureDate = manufactureDate;
	}

	public String getStrStringManufactureDate() {
		return strStringManufactureDate;
	}

	public void setStrStringManufactureDate(String strStringManufactureDate) {
		this.strStringManufactureDate = strStringManufactureDate;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public String getStrStringRegistrationDate() {
		return strStringRegistrationDate;
	}

	public void setStrStringRegistrationDate(String strStringRegistrationDate) {
		this.strStringRegistrationDate = strStringRegistrationDate;
	}

	public String getSeating() {
		return seating;
	}

	public void setSeating(String seating) {
		this.seating = seating;
	}

	public String getNoofDoors() {
		return noofDoors;
	}

	public void setNoofDoors(String noofDoors) {
		this.noofDoors = noofDoors;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
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

	public String getInsuCompName() {
		return insuCompName;
	}

	public void setInsuCompName(String insuCompName) {
		this.insuCompName = insuCompName;
	}

	public Date getInsuExpDate() {
		return insuExpDate;
	}

	public void setInsuExpDate(Date insuExpDate) {
		this.insuExpDate = insuExpDate;
	}

	public String getInsuCat() {
		return insuCat;
	}

	public void setInsuCat(String insuCat) {
		this.insuCat = insuCat;
	}

	public BigDecimal getPriceValOfBus() {
		return priceValOfBus;
	}

	public void setPriceValOfBus(BigDecimal priceValOfBus) {
		this.priceValOfBus = priceValOfBus;
	}

	public BigDecimal getBankLoan() {
		return bankLoan;
	}

	public void setBankLoan(BigDecimal bankLoan) {
		this.bankLoan = bankLoan;
	}

	public String getIsLoanObtained() {
		return isLoanObtained;
	}

	public void setIsLoanObtained(String isLoanObtained) {
		this.isLoanObtained = isLoanObtained;
	}

	public BigDecimal getDueAmount() {
		return dueAmount;
	}

	public void setDueAmount(BigDecimal dueAmount) {
		this.dueAmount = dueAmount;
	}

	public String getFinanceCompany() {
		return financeCompany;
	}

	public void setFinanceCompany(String financeCompany) {
		this.financeCompany = financeCompany;
	}

	public Date getDateObtained() {
		return dateObtained;
	}

	public void setDateObtained(Date dateObtained) {
		this.dateObtained = dateObtained;
	}

	public String getLapsedInstall() {
		return lapsedInstall;
	}

	public void setLapsedInstall(String lapsedInstall) {
		this.lapsedInstall = lapsedInstall;
	}

	public Date getExpiryDateRevLic() {
		return expiryDateRevLic;
	}

	public void setExpiryDateRevLic(Date expiryDateRevLic) {
		this.expiryDateRevLic = expiryDateRevLic;
	}

	public Date getDateOfFirstReg() {
		return dateOfFirstReg;
	}

	public void setDateOfFirstReg(Date dateOfFirstReg) {
		this.dateOfFirstReg = dateOfFirstReg;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getServiceTypeDesc() {
		return serviceTypeDesc;
	}

	public void setServiceTypeDesc(String serviceTypeDesc) {
		this.serviceTypeDesc = serviceTypeDesc;
	}

	public Date getExpiryDateEmissionTest() {
		return expiryDateEmissionTest;
	}

	public void setExpiryDateEmissionTest(Date expiryDateEmissionTest) {
		this.expiryDateEmissionTest = expiryDateEmissionTest;
	}

}
