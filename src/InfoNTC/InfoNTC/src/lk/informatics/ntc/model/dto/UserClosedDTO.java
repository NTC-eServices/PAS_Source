package lk.informatics.ntc.model.dto;

import java.io.Serializable;
import java.util.Date;

public class UserClosedDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1558906312133782413L;

	private String sectionCode;
	private String counterId;
	private Date date;
	private String status;
	private String queueno;
	private String startDateTime;
	private String endDateTime;
	private String referenceNo;
	private String completedBy;

	public UserClosedDTO() {
		super();
	}

	public UserClosedDTO(String sectionCode, String counterId, Date date, String status, String queueno,
			String startDateTime, String endDateTime, String referenceNo, String completedBy) {
		super();
		this.sectionCode = sectionCode;
		this.counterId = counterId;
		this.date = date;
		this.status = status;
		this.queueno = queueno;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.referenceNo = referenceNo;
		this.completedBy = completedBy;
	}

	public String getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(String sectionCode) {
		this.sectionCode = sectionCode;
	}

	public String getCounterId() {
		return counterId;
	}

	public void setCounterId(String counterId) {
		this.counterId = counterId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getQueueno() {
		return queueno;
	}

	public void setQueueno(String queueno) {
		this.queueno = queueno;
	}

	public String getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(String startDateTime) {
		this.startDateTime = startDateTime;
	}

	public String getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(String endDateTime) {
		this.endDateTime = endDateTime;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public String getCompletedBy() {
		return completedBy;
	}

	public void setCompletedBy(String completedBy) {
		this.completedBy = completedBy;
	}

}
