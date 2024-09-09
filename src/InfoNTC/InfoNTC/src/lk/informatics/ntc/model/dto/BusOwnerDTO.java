package lk.informatics.ntc.model.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class BusOwnerDTO implements Serializable {
	private static final long serialVersionUID = 8344243187243931903L;

	private long seq;
	private String perferedLanguage;
	private String busRegNo;
	private String applicationNo;
	private String permitNo;
	private String title;
	private String gender;
	private Date dob;
	private String nicNo;
	private String fullName;
	private String fullNameSinhala;
	private String fullNameTamil;
	private String nameWithInitials;
	private String maritalStatus;
	private String telephoneNo;
	private String mobileNo;
	private String address1;
	private String address1Sinhala;
	private String address1Tamil;
	private String address2;
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
	private String isBacklogApp;
	private String createdBy;
	private Timestamp cratedDate;
	private String modifiedBy;
	private Timestamp modifiedDate;
	private String strStringDob;
	private int permitPeroid;
	private String queueNo;
	private String dobVal;
	
	//
	private String routeFlag;

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

	public String getNicNo() {
		return nicNo;
	}

	public void setNicNo(String nicNo) {
		this.nicNo = nicNo;
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

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
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

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
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

	public String getStrStringDob() {
		return strStringDob;
	}

	public void setStrStringDob(String strStringDob) {
		this.strStringDob = strStringDob;
	}

	public String getBusRegNo() {
		return busRegNo;
	}

	public void setBusRegNo(String busRegNo) {
		this.busRegNo = busRegNo;
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

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public long getSeq() {
		return seq;
	}

	public void setSeq(long seq) {
		this.seq = seq;
	}

	public int getPermitPeroid() {
		return permitPeroid;
	}

	public void setPermitPeroid(int permitPeroid) {
		this.permitPeroid = permitPeroid;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getQueueNo() {
		return queueNo;
	}

	public void setQueueNo(String queueNo) {
		this.queueNo = queueNo;
	}

	public String getDobVal() {
		return dobVal;
	}

	public void setDobVal(String dobVal) {
		this.dobVal = dobVal;
	}

	public String getRouteFlag() {
		return routeFlag;
	}

	public void setRouteFlag(String routeFlag) {
		this.routeFlag = routeFlag;
	}
	

}
