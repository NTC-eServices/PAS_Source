package lk.informatics.ntc.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProgramDTO implements Serializable {
	private static final long serialVersionUID = 12312312451L;

	private Long detail_seq;
	private String subRefNo;
	private String province;
	private String district;
	private String zone;
	private String scheduleDate;
	private String place;
	private String type; //S-School, O-Other
	private String status;
	
	private Long evm_seq;
	private int noOfStudents;
	private String schoolName;
	private BigDecimal estimatedBudjet;
	private BigDecimal actualBudjet;
	private String remarks;
	private String year;
	
	
	public Long getDetail_seq() {
		return detail_seq;
	}
	public void setDetail_seq(Long detail_seq) {
		this.detail_seq = detail_seq;
	}
	public String getSubRefNo() {
		return subRefNo;
	}
	public void setSubRefNo(String subRefNo) {
		this.subRefNo = subRefNo;
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
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public String getScheduleDate() {
		return scheduleDate;
	}
	public void setScheduleDate(String scheduleDate) {
		this.scheduleDate = scheduleDate;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getNoOfStudents() {
		return noOfStudents;
	}
	public void setNoOfStudents(int noOfStudents) {
		this.noOfStudents = noOfStudents;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public BigDecimal getEstimatedBudjet() {
		return estimatedBudjet;
	}
	public void setEstimatedBudjet(BigDecimal estimatedBudjet) {
		this.estimatedBudjet = estimatedBudjet;
	}
	public BigDecimal getActualBudjet() {
		return actualBudjet;
	}
	public void setActualBudjet(BigDecimal actualBudjet) {
		this.actualBudjet = actualBudjet;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public Long getEvm_seq() {
		return evm_seq;
	}
	public void setEvm_seq(Long evm_seq) {
		this.evm_seq = evm_seq;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}