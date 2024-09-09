package lk.informatics.ntc.model.dto;

public class FluingSquadVioConditionDTO {

	private String code;
	private String description;
	private boolean ststus;
	private String remarks;
	private boolean updated;
	
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
	public boolean isStstus() {
		return ststus;
	}
	public void setStstus(boolean ststus) {
		this.ststus = ststus;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public boolean isUpdated() {
		return updated;
	}
	public void setUpdated(boolean updated) {
		this.updated = updated;
	}
}
