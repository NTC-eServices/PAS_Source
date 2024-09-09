package lk.informatics.ntc.model.dto;

import java.io.Serializable;

import java.sql.Timestamp;
import java.math.BigDecimal;

public class TerminalPayCancellationDTO implements Serializable {
	private static final long serialVersionUID = 8344243187243931903L;

	private long seq;
	private String status;
	private String paymentTypeDesc;
	private String vehicleNo;
	private String owner;
	private BigDecimal paneltyAmt;
	private BigDecimal chargeAmt;
	private BigDecimal paidAmt;
	private String receiptNo;
	private String ModifiedBy;
	private Timestamp ModifiedDate;

	public long getSeq() {
		return seq;
	}

	public void setSeq(long seq) {
		this.seq = seq;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPaymentTypeDesc() {
		return paymentTypeDesc;
	}

	public void setPaymentTypeDesc(String paymentTypeDesc) {
		this.paymentTypeDesc = paymentTypeDesc;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public BigDecimal getPaneltyAmt() {
		return paneltyAmt;
	}

	public void setPaneltyAmt(BigDecimal paneltyAmt) {
		this.paneltyAmt = paneltyAmt;
	}

	public BigDecimal getChargeAmt() {
		return chargeAmt;
	}

	public void setChargeAmt(BigDecimal chargeAmt) {
		this.chargeAmt = chargeAmt;
	}

	public BigDecimal getPaidAmt() {
		return paidAmt;
	}

	public void setPaidAmt(BigDecimal paidAmt) {
		this.paidAmt = paidAmt;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public String getModifiedBy() {
		return ModifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		ModifiedBy = modifiedBy;
	}

	public Timestamp getModifiedDate() {
		return ModifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		ModifiedDate = modifiedDate;
	}

}
