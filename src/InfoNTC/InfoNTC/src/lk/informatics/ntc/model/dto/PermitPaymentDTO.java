package lk.informatics.ntc.model.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class PermitPaymentDTO implements Serializable {
	private static final long serialVersionUID = 8344243187243931903L;
	
	private long seq;
	private String applicationNo;
	private String permitNo;
	private String vehicleRegNo;
	private BigDecimal totalPermitAmt;
	private BigDecimal excessAmt;
	private String specialRemark;
	private BigDecimal renewalAmt;
    private BigDecimal penaltyAmt;
    private BigDecimal tenderFee;
    private BigDecimal serviceFee;
    private BigDecimal otherFee;
    private BigDecimal totalFee;    
	private String isBacklogApp;
	private String createdBy;
	private Timestamp cratedDate;
	private String modifiedBy;
	private Timestamp modifiedDate;
	
	
	public long getSeq() {
		return seq;
	}
	public void setSeq(long seq) {
		this.seq = seq;
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
	public String getVehicleRegNo() {
		return vehicleRegNo;
	}
	public void setVehicleRegNo(String vehicleRegNo) {
		this.vehicleRegNo = vehicleRegNo;
	}
	public BigDecimal getTotalPermitAmt() {
		return totalPermitAmt;
	}
	public void setTotalPermitAmt(BigDecimal totalPermitAmt) {
		this.totalPermitAmt = totalPermitAmt;
	}
	public BigDecimal getExcessAmt() {
		return excessAmt;
	}
	public void setExcessAmt(BigDecimal excessAmt) {
		this.excessAmt = excessAmt;
	}
	public String getSpecialRemark() {
		return specialRemark;
	}
	public void setSpecialRemark(String specialRemark) {
		this.specialRemark = specialRemark;
	}
	public BigDecimal getRenewalAmt() {
		return renewalAmt;
	}
	public void setRenewalAmt(BigDecimal renewalAmt) {
		this.renewalAmt = renewalAmt;
	}
	public BigDecimal getPenaltyAmt() {
		return penaltyAmt;
	}
	public void setPenaltyAmt(BigDecimal penaltyAmt) {
		this.penaltyAmt = penaltyAmt;
	}
	public BigDecimal getTenderFee() {
		return tenderFee;
	}
	public void setTenderFee(BigDecimal tenderFee) {
		this.tenderFee = tenderFee;
	}
	public BigDecimal getServiceFee() {
		return serviceFee;
	}
	public void setServiceFee(BigDecimal serviceFee) {
		this.serviceFee = serviceFee;
	}
	public BigDecimal getOtherFee() {
		return otherFee;
	}
	public void setOtherFee(BigDecimal otherFee) {
		this.otherFee = otherFee;
	}
	public BigDecimal getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
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

	
	
}
