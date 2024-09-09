package lk.informatics.ntc.model.dto;

import java.sql.Timestamp;

public class FlyingSquadInvestiDetailDTO {
	
      private String empNo;
	  private String name;
	  private String designation;
	  private boolean allow;
	  private String createdBy;
	  private String modifiedBy;
	  private Timestamp createdDate;
	  private Timestamp modifiedDate;
  
  
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public boolean isAllow() {
		return allow;
	}
	public void setAllow(boolean allow) {
		this.allow = allow;
	}
	
	public String getCreatedBy() {
			return createdBy;
    }
    
	public void setCreatedBy(String createdBy) {
			this.createdBy = createdBy;
	}
	public String getModifiedBy() {
			return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
			this.modifiedBy = modifiedBy;
	}
	public Timestamp getCreatedDate() {
			return createdDate;
	}
	public void setCreatedDate(Timestamp createdDate) {
			this.createdDate = createdDate;
	}
	public Timestamp getModifiedDate() {
			return modifiedDate;
	}
	public void setModifiedDate(Timestamp modifiedDate) {
			this.modifiedDate = modifiedDate;
	}
	public String getEmpNo() {
		return empNo;
	}
	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}
}
