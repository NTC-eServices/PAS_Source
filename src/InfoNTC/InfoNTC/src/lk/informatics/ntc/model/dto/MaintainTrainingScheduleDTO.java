package lk.informatics.ntc.model.dto;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

public class MaintainTrainingScheduleDTO {
	private long seq;
	private String scheduleCode;
	private String yearN;
	private String monthCode;
	private Date trainingDate;
	private String startTime;
	private String endTime;
	private String trainingTypeCode;
	private String location;
	private BigDecimal noofTrainees;
	private String staus;
	private String monthDesc;
	private String typeDesc;
	Date startTimeN;
	Date endTimeN;
	private String strtrainingDate;
	private String createdBy;
	private String modifiedBy;
	private Timestamp createdDate;
	private Timestamp modifiedDate;
	
	public long getSeq() {
		return seq;
	}
	public void setSeq(long seq) {
		this.seq = seq;
	}
	public String getScheduleCode() {
		return scheduleCode;
	}
	public void setScheduleCode(String scheduleCode) {
		this.scheduleCode = scheduleCode;
	}
	public String getYearN() {
		return yearN;
	}
	public void setYearN(String yearN) {
		this.yearN = yearN;
	}
	public String getMonthCode() {
		return monthCode;
	}
	public void setMonthCode(String monthCode) {
		this.monthCode = monthCode;
	}
	public Date getTrainingDate() {
		return trainingDate;
	}
	public void setTrainingDate(Date trainingDate) {
		this.trainingDate = trainingDate;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getTrainingTypeCode() {
		return trainingTypeCode;
	}
	public void setTrainingTypeCode(String trainingTypeCode) {
		this.trainingTypeCode = trainingTypeCode;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public BigDecimal getNoofTrainees() {
		return noofTrainees;
	}
	public void setNoofTrainees(BigDecimal noofTrainees) {
		this.noofTrainees = noofTrainees;
	}
	public String getStaus() {
		return staus;
	}
	public void setStaus(String staus) {
		this.staus = staus;
	}
	public String getMonthDesc() {
		return monthDesc;
	}
	public void setMonthDesc(String monthDesc) {
		this.monthDesc = monthDesc;
	}
	public String getTypeDesc() {
		return typeDesc;
	}
	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}
	public Date getStartTimeN() {
		return startTimeN;
	}
	public void setStartTimeN(Date startTimeN) {
		this.startTimeN = startTimeN;
	}
	public Date getEndTimeN() {
		return endTimeN;
	}
	public void setEndTimeN(Date endTimeN) {
		this.endTimeN = endTimeN;
	}
	public String getStrtrainingDate() {
		return strtrainingDate;
	}
	public void setStrtrainingDate(String strtrainingDate) {
		this.strtrainingDate = strtrainingDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	public Timestamp getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}


}

