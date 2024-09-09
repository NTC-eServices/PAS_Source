package lk.informatics.ntc.model.dto;

import java.io.Serializable;

public class ServiceAmountDTO implements Serializable {

	private static final long serialVersionUID = 25L;
	
	private String serviceTypeCode;
	private String serviceType;
	private double basicAmount;
	private double penalty;
	
	
	
	public double getBasicAmount() {
		return basicAmount;
	}
	public void setBasicAmount(double basicAmount) {
		this.basicAmount = basicAmount;
	}
	public double getPenalty() {
		return penalty;
	}
	public void setPenalty(double penalty) {
		this.penalty = penalty;
	}
	public String getServiceTypeCode() {
		return serviceTypeCode;
	}
	public void setServiceTypeCode(String serviceTypeCode) {
		this.serviceTypeCode = serviceTypeCode;
	}
	public void setBasicAmount(Long basicAmount) {
		this.basicAmount = basicAmount;
	}
	
	public void setPenalty(Long penalty) {
		this.penalty = penalty;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	
}
