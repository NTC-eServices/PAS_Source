package lk.informatics.ntc.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class CommittedOffencesDTO implements Serializable {

	private static final long serialVersionUID = 5689685L;
	
	private Long seq;
	private String code;
	private String description;
	private String remark;
	private boolean applicable;
	private Long chr_seq;
	private BigDecimal charge;
	private Long p_seq;
	private boolean driverApplicable;
	private boolean conductorApplicable;
	private BigDecimal driverPoints;
	private BigDecimal conductorPoints;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public boolean isApplicable() {
		return applicable;
	}
	public void setApplicable(boolean applicable) {
		this.applicable = applicable;
	}
	public Long getSeq() {
		return seq;
	}
	public void setSeq(Long seq) {
		this.seq = seq;
	}
	public BigDecimal getCharge() {
		return charge;
	}
	public void setCharge(BigDecimal charge) {
		this.charge = charge;
	}
	public Long getChr_seq() {
		return chr_seq;
	}
	public void setChr_seq(Long chr_seq) {
		this.chr_seq = chr_seq;
	}
	public boolean isDriverApplicable() {
		return driverApplicable;
	}
	public void setDriverApplicable(boolean driverApplicable) {
		this.driverApplicable = driverApplicable;
	}
	public boolean isConductorApplicable() {
		return conductorApplicable;
	}
	public void setConductorApplicable(boolean conductorApplicable) {
		this.conductorApplicable = conductorApplicable;
	}
	public BigDecimal getDriverPoints() {
		return driverPoints;
	}
	public void setDriverPoints(BigDecimal driverPoints) {
		this.driverPoints = driverPoints;
	}
	public BigDecimal getConductorPoints() {
		return conductorPoints;
	}
	public void setConductorPoints(BigDecimal conductorPoints) {
		this.conductorPoints = conductorPoints;
	}
	public Long getP_seq() {
		return p_seq;
	}
	public void setP_seq(Long p_seq) {
		this.p_seq = p_seq;
	}
	
}
