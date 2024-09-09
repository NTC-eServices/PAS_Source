package lk.informatics.ntc.model.dto;

import java.io.Serializable;

public class DepartmentDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8468359294818171313L;
	private String code;
	private String description;
	private String descriptionSinhala;
	private String descriptionTamil;
	private String active;

	public DepartmentDTO() {
		super();
	}

	public DepartmentDTO(String code, String description, String descriptionSinhala, String descriptionTamil,
			String active) {
		super();
		this.code = code;
		this.description = description;
		this.descriptionSinhala = descriptionSinhala;
		this.descriptionTamil = descriptionTamil;
		this.active = active;
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

	public String getDescriptionSinhala() {
		return descriptionSinhala;
	}

	public void setDescriptionSinhala(String descriptionSinhala) {
		this.descriptionSinhala = descriptionSinhala;
	}

	public String getDescriptionTamil() {
		return descriptionTamil;
	}

	public void setDescriptionTamil(String descriptionTamil) {
		this.descriptionTamil = descriptionTamil;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

}
