package lk.informatics.ntc.model.dto;

import java.io.Serializable;

public class AssignBranchesDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6843609485505528384L;
	
	private String bankCode;
	private String bankDes;
	private String branchCode;
	private String branch_description_english;
	private String branch_description_sinhala;
	private String branch_description_tamil;
	private boolean editRecord;
	
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getBankDes() {
		return bankDes;
	}
	public void setBankDes(String bankDes) {
		this.bankDes = bankDes;
	}
	public String getBranch_description_english() {
		return branch_description_english;
	}
	public void setBranch_description_english(String branch_description_english) {
		this.branch_description_english = branch_description_english;
	}
	public String getBranch_description_sinhala() {
		return branch_description_sinhala;
	}
	public void setBranch_description_sinhala(String branch_description_sinhala) {
		this.branch_description_sinhala = branch_description_sinhala;
	}
	public String getBranch_description_tamil() {
		return branch_description_tamil;
	}
	public void setBranch_description_tamil(String branch_description_tamil) {
		this.branch_description_tamil = branch_description_tamil;
	}
	public String getBranchCode() {
		return branchCode;
	}
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public boolean isEditRecord() {
		return editRecord;
	}
	public void setEditRecord(boolean editRecord) {
		this.editRecord = editRecord;
	}
}
