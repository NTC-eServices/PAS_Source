package lk.informatics.ntc.model.dto;

import java.io.Serializable;
import java.util.Date;

public class MidPointSurveyDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String surveyNo;
	private String location;
	private String directinFrom;
	private String directionTo;
	private String nameOfRecorder;
	private String remarks;
	private String dateStr;
	private String timeStr;
	private int noOfRows;
	private int noOfCopies;
	
	private String formId;
	private Date date;
	private Date time;
	//added 
	private String rows;
	
	
	public String getRows() {
		return rows;
	}
	public void setRows(String rows) {
		this.rows = rows;
	}
	public String getSurveyNo() {
		return surveyNo;
	}
	public void setSurveyNo(String surveyNo) {
		this.surveyNo = surveyNo;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDirectinFrom() {
		return directinFrom;
	}
	public void setDirectinFrom(String directinFrom) {
		this.directinFrom = directinFrom;
	}
	public String getDirectionTo() {
		return directionTo;
	}
	public void setDirectionTo(String directionTo) {
		this.directionTo = directionTo;
	}
	public String getNameOfRecorder() {
		return nameOfRecorder;
	}
	public void setNameOfRecorder(String nameOfRecorder) {
		this.nameOfRecorder = nameOfRecorder;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public int getNoOfRows() {
		return noOfRows;
	}
	public void setNoOfRows(int noOfRows) {
		this.noOfRows = noOfRows;
	}
	public int getNoOfCopies() {
		return noOfCopies;
	}
	public int setNoOfCopies(int noOfCopies) {
		return this.noOfCopies = noOfCopies;
	}
	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId) {
		this.formId = formId;
	}
	public String getDateStr() {
		return dateStr;
	}
	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getTimeStr() {
		return timeStr;
	}
	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	
	
}
