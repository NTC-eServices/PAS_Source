package lk.informatics.ntc.model.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class CombineControlSheetDTO implements Serializable {

	private static final long serialVersionUID = 2702170216759167561L;

	private String routeNo;
	private String startFrom;
	private String endAt;
	private String serviceType;
	private String serviceTypeDes;
	private String midPointCode;
	private String midPointDes;
	private String groupNo;
	private String side;
	private boolean applicableForControlSheet;
	private Date startDate;
	private Date endDate;

	public CombineControlSheetDTO() {
	}

	public String getRouteNo() {
		return routeNo;
	}

	public void setRouteNo(String routeNo) {
		this.routeNo = routeNo;
	}

	public String getStartFrom() {
		return startFrom;
	}

	public void setStartFrom(String startFrom) {
		this.startFrom = startFrom;
	}

	public String getEndAt() {
		return endAt;
	}

	public void setEndAt(String endAt) {
		this.endAt = endAt;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getServiceTypeDes() {
		return serviceTypeDes;
	}

	public void setServiceTypeDes(String serviceTypeDes) {
		this.serviceTypeDes = serviceTypeDes;
	}

	public String getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}

	public String getSide() {
		return side;
	}

	public void setSide(String side) {
		this.side = side;
	}

	public boolean isApplicableForControlSheet() {
		return applicableForControlSheet;
	}

	public void setApplicableForControlSheet(boolean applicableForControlSheet) {
		this.applicableForControlSheet = applicableForControlSheet;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getMidPointCode() {
		return midPointCode;
	}

	public void setMidPointCode(String midPointCode) {
		this.midPointCode = midPointCode;
	}

	public String getMidPointDes() {
		return midPointDes;
	}

	public void setMidPointDes(String midPointDes) {
		this.midPointDes = midPointDes;
	}

}
