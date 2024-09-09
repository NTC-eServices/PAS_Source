package lk.informatics.ntc.model.dto;

import java.io.Serializable;
import java.util.Date;

public class SetupCommitteeBoardDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4950355540030719139L;

	private String type;
	private String referenceNo;
	private String transactionTypeCode;
	private String transactionTypeDes;
	private String status;
	private Date activeFromDate;
	private Date activeToDate;
	private String activeFromDateStr;
	private String activeToDateStr;
	private String authorization;
	private String organizationCode;
	private String organizationDes;
	private String designationCode;
	private String designationDes;
	private String userID;
	private String name;
	private String responsibilities;
	private String isauthorized;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getReferenceNo() {
		return referenceNo;
	}
	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}
	public String getTransactionTypeCode() {
		return transactionTypeCode;
	}
	public void setTransactionTypeCode(String transactionTypeCode) {
		this.transactionTypeCode = transactionTypeCode;
	}
	public String getTransactionTypeDes() {
		return transactionTypeDes;
	}
	public void setTransactionTypeDes(String transactionTypeDes) {
		this.transactionTypeDes = transactionTypeDes;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getActiveFromDate() {
		return activeFromDate;
	}
	public void setActiveFromDate(Date activeFromDate) {
		this.activeFromDate = activeFromDate;
	}
	public Date getActiveToDate() {
		return activeToDate;
	}
	public void setActiveToDate(Date activeToDate) {
		this.activeToDate = activeToDate;
	}
	public String getAuthorization() {
		return authorization;
	}
	public void setAuthorization(String authorization) {
		this.authorization = authorization;
	}
	public String getOrganizationCode() {
		return organizationCode;
	}
	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}
	public String getOrganizationDes() {
		return organizationDes;
	}
	public void setOrganizationDes(String organizationDes) {
		this.organizationDes = organizationDes;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getResponsibilities() {
		return responsibilities;
	}
	public void setResponsibilities(String responsibilities) {
		this.responsibilities = responsibilities;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getDesignationCode() {
		return designationCode;
	}
	public void setDesignationCode(String designationCode) {
		this.designationCode = designationCode;
	}
	public String getDesignationDes() {
		return designationDes;
	}
	public void setDesignationDes(String designationDes) {
		this.designationDes = designationDes;
	}
	public String getActiveFromDateStr() {
		return activeFromDateStr;
	}
	public void setActiveFromDateStr(String activeFromDateStr) {
		this.activeFromDateStr = activeFromDateStr;
	}
	public String getActiveToDateStr() {
		return activeToDateStr;
	}
	public void setActiveToDateStr(String activeToDateStr) {
		this.activeToDateStr = activeToDateStr;
	}
	public String getIsauthorized() {
		return isauthorized;
	}
	public void setIsauthorized(String isauthorized) {
		this.isauthorized = isauthorized;
	}
	
}
