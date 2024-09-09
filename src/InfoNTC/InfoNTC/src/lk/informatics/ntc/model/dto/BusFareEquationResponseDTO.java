package lk.informatics.ntc.model.dto;

import java.util.List;

public class BusFareEquationResponseDTO {

	private List<String> abbreviationList;
	private String status;
	private String message;

	public BusFareEquationResponseDTO(List<String> abbreviationList, String status, String message) {
		this.abbreviationList = abbreviationList;
		this.status = status;
		this.message = message;
	}

	public List<String> getAbbreviationList() {
		return abbreviationList;
	}

	public void setAbbreviationList(List<String> abbreviationList) {
		this.abbreviationList = abbreviationList;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
