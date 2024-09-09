package lk.informatics.ntc.model.dto;

import java.sql.Timestamp;

public class ManageRetainDocsDTO {

	private String releaseDate;
	private String releaseTo;
	private String releaseId;
	private String specialRemarks;
	private String releaseStatus;
	private String code;
	private String codeDescription;
	private String collectedDate;
	private String chargeRefNo;
	private Boolean check = false;
	private Timestamp modifiedDate;
	private String modifiedBy;

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getReleaseTo() {
		return releaseTo;
	}

	public void setReleaseTo(String releaseTo) {
		this.releaseTo = releaseTo;
	}

	public String getReleaseId() {
		return releaseId;
	}

	public void setReleaseId(String releaseId) {
		this.releaseId = releaseId;
	}

	public String getSpecialRemarks() {
		return specialRemarks;
	}

	public void setSpecialRemarks(String specialRemarks) {
		this.specialRemarks = specialRemarks;
	}

	public String getReleaseStatus() {
		return releaseStatus;
	}

	public void setReleaseStatus(String releaseStatus) {
		this.releaseStatus = releaseStatus;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCodeDescription() {
		return codeDescription;
	}

	public void setCodeDescription(String codeDescription) {
		this.codeDescription = codeDescription;
	}

	public String getChargeRefNo() {
		return chargeRefNo;
	}

	public void setChargeRefNo(String chargeRefNo) {
		this.chargeRefNo = chargeRefNo;
	}

	public String getCollectedDate() {
		return collectedDate;
	}

	public void setCollectedDate(String collectedDate) {
		this.collectedDate = collectedDate;
	}

	public Boolean getCheck() {
		return check;
	}

	public void setCheck(Boolean check) {
		this.check = check;
	}

	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Timestamp timestamp) {
		this.modifiedDate = timestamp;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

}
