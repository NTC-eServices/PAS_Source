package lk.informatics.ntc.model.dto;
import java.io.Serializable;
import java.sql.Timestamp;


public class EmployeeAddressDTO implements Serializable {
	private static final long serialVersionUID = 8344243187243931903L;
	
	private long seq;
	private String empNo;
	private String addressType;
	private String addressTypeDesc;
	private String address1;
	private String address2;
	private String city;
	private String cityDesc;
	private String status;
	private String createdBy;
	private Timestamp cratedDate;
	private String modifiedBy;
	private Timestamp modifiedDate;
	
	
	public String getEmpNo() {
		return empNo;
	}
	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}
	public String getAddressType() {
		return addressType;
	}
	public void setAddressType(String addressType) {
		this.addressType = addressType;
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
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getAddressTypeDesc() {
		return addressTypeDesc;
	}
	public void setAddressTypeDesc(String addressTypeDesc) {
		this.addressTypeDesc = addressTypeDesc;
	}
	public String getCityDesc() {
		return cityDesc;
	}
	public void setCityDesc(String cityDesc) {
		this.cityDesc = cityDesc;
	}
	public long getSeq() {
		return seq;
	}
	public void setSeq(long seq) {
		this.seq = seq;
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
}
