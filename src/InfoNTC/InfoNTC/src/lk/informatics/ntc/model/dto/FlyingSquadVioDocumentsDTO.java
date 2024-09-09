package lk.informatics.ntc.model.dto;

import java.util.Date;

public class FlyingSquadVioDocumentsDTO {

	private String code;
	private String description;
	private String remark;
	private boolean violated;
	private boolean updated;
	private String releaseremark;
	private boolean isreleased;
	private Date releaseDate;
	
	public boolean isIsreleased() {
		return isreleased;
	}
	public void setIsreleased(boolean isreleased) {
		this.isreleased = isreleased;
	}
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
	public boolean isViolated() {
		return violated;
	}
	public void setViolated(boolean violated) {
		this.violated = violated;
	}
	public boolean isUpdated() {
		return updated;
	}
	public void setUpdated(boolean updated) {
		this.updated = updated;
	}
	public String getReleaseremark() {
		return releaseremark;
	}
	public void setReleaseremark(String releaseremark) {
		this.releaseremark = releaseremark;
	}
	public Date getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}
}
