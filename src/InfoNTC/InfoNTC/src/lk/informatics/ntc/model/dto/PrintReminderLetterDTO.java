package lk.informatics.ntc.model.dto;

import java.sql.Timestamp;
import java.util.Date;

public class PrintReminderLetterDTO {
	private String letterType;
	private Date startDate;
	private Date endDate;
	private String permitNo;
	private String firstNoticeType;
	private String secondNoticeType;
	private String thirdNoticeType;
	private String reminderType;
	private Date genrateDate;
	private String letterTypeCode;
	private String firstPrint;
	private String secondPrint;
	private String thirdPrint;
	private Timestamp createdDate;
	private String appNo;

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getLetterType() {
		return letterType;
	}

	public void setLetterType(String letterType) {
		this.letterType = letterType;
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

	public String getPermitNo() {
		return permitNo;
	}

	public void setPermitNo(String permitNo) {
		this.permitNo = permitNo;
	}

	public String getReminderType() {
		return reminderType;
	}

	public void setReminderType(String reminderType) {
		this.reminderType = reminderType;
	}

	public Date getGenrateDate() {
		return genrateDate;
	}

	public void setGenrateDate(Date genrateDate) {
		this.genrateDate = genrateDate;
	}

	public String getLetterTypeCode() {
		return letterTypeCode;
	}

	public void setLetterTypeCode(String letterTypeCode) {
		this.letterTypeCode = letterTypeCode;
	}

	public String getFirstNoticeType() {
		return firstNoticeType;
	}

	public void setFirstNoticeType(String firstNoticeType) {
		this.firstNoticeType = firstNoticeType;
	}

	public String getSecondNoticeType() {
		return secondNoticeType;
	}

	public void setSecondNoticeType(String secondNoticeType) {
		this.secondNoticeType = secondNoticeType;
	}

	public String getThirdNoticeType() {
		return thirdNoticeType;
	}

	public void setThirdNoticeType(String thirdNoticeType) {
		this.thirdNoticeType = thirdNoticeType;
	}

	public String getFirstPrint() {
		return firstPrint;
	}

	public void setFirstPrint(String firstPrint) {
		this.firstPrint = firstPrint;
	}

	public String getSecondPrint() {
		return secondPrint;
	}

	public void setSecondPrint(String secondPrint) {
		this.secondPrint = secondPrint;
	}

	public String getThirdPrint() {
		return thirdPrint;
	}

	public void setThirdPrint(String thirdPrint) {
		this.thirdPrint = thirdPrint;
	}

	public String getAppNo() {
		return appNo;
	}

	public void setAppNo(String appNo) {
		this.appNo = appNo;
	}

}
