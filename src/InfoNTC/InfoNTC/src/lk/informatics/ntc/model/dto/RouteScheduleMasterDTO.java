package lk.informatics.ntc.model.dto;

import java.io.Serializable;

/**
 * Created for combine control sheet
 * Created by: gayathra.m
 * */
public class RouteScheduleMasterDTO implements Serializable {

	private static final long serialVersionUID = -6428548518190451487L;

	private String rsRouteRefNo;
	private String rsGeneratorRefNo;
	private String rsRotationType;
	private String rsIsSwap;
	private String rsStartDate;
	private String rsEndDate;
	private String rsNoOfDates;
	
	private String rsRouteNo;
	private String rsServiceType;
	private String rsGroupNO;
	private String rsSide;
	

	public RouteScheduleMasterDTO() {
	}

	public String getRsRouteRefNo() {
		return rsRouteRefNo;
	}

	public void setRsRouteRefNo(String rsRouteRefNo) {
		this.rsRouteRefNo = rsRouteRefNo;
	}

	public String getRsGeneratorRefNo() {
		return rsGeneratorRefNo;
	}

	public void setRsGeneratorRefNo(String rsGeneratorRefNo) {
		this.rsGeneratorRefNo = rsGeneratorRefNo;
	}

	public String getRsRotationType() {
		return rsRotationType;
	}

	public void setRsRotationType(String rsRotationType) {
		this.rsRotationType = rsRotationType;
	}

	public String getRsIsSwap() {
		return rsIsSwap;
	}

	public void setRsIsSwap(String rsIsSwap) {
		this.rsIsSwap = rsIsSwap;
	}

	public String getRsStartDate() {
		return rsStartDate;
	}

	public void setRsStartDate(String rsStartDate) {
		this.rsStartDate = rsStartDate;
	}

	public String getRsEndDate() {
		return rsEndDate;
	}

	public void setRsEndDate(String rsEndDate) {
		this.rsEndDate = rsEndDate;
	}

	public String getRsNoOfDates() {
		return rsNoOfDates;
	}

	public void setRsNoOfDates(String rsNoOfDates) {
		this.rsNoOfDates = rsNoOfDates;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getRsRouteNo() {
		return rsRouteNo;
	}

	public void setRsRouteNo(String rsRouteNo) {
		this.rsRouteNo = rsRouteNo;
	}

	public String getRsServiceType() {
		return rsServiceType;
	}

	public void setRsServiceType(String rsServiceType) {
		this.rsServiceType = rsServiceType;
	}

	public String getRsGroupNO() {
		return rsGroupNO;
	}

	public void setRsGroupNO(String rsGroupNO) {
		this.rsGroupNO = rsGroupNO;
	}

	public String getRsSide() {
		return rsSide;
	}

	public void setRsSide(String rsSide) {
		this.rsSide = rsSide;
	}

}
