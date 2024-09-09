package lk.informatics.ntc.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class AdvancedPaymentDTO implements Serializable {
	private static final long serialVersionUID = 8344243187243931903L;

	public AdvancedPaymentDTO() {
		super();
	}

	public AdvancedPaymentDTO(String serviceDetails, BigDecimal noofUnits, BigDecimal amount, String date) {

		this.serviceDetails = serviceDetails;
		this.noofUnits = noofUnits;
		this.setAmount(amount);
		this.date = date;
	}

	private String voucherNo;
	private String serviceDetails;
	private BigDecimal noofUnits;
	private BigDecimal amount;
	private String date;
	private String createdBy;
	private Timestamp cratedDate;
	private String modifiedBy;
	private Timestamp modifiedDate;

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public String getServiceDetails() {
		return serviceDetails;
	}

	public void setServiceDetails(String serviceDetails) {
		this.serviceDetails = serviceDetails;
	}

	public BigDecimal getNoofUnits() {
		return noofUnits;
	}

	public void setNoofUnits(BigDecimal noofUnits) {
		this.noofUnits = noofUnits;
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}
