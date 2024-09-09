package lk.informatics.ntc.model.dto;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class EmployeeDesignationDTO  implements Serializable  {
	private static final long serialVersionUID = 8344243187243931903L;
	private long seq;
	private String empNo;
	private String designationCode;
	private String designationDesc;
	private String gradeCode;
	private String gradeDesc;
	private String salCatCode;
	private String salCatDesc;
	private Date startDate;
	private Date endDate;
	private String strStartDate;
	private String strEndDate;
	private String status;
	private String createdBy;
	
	public long getSeq() {
		return seq;
	}
	public void setSeq(long seq) {
		this.seq = seq;
	}
	public String getEmpNo() {
		return empNo;
	}
	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}
	public String getDesignationCode() {
		return designationCode;
	}
	public void setDesignationCode(String designationCode) {
		this.designationCode = designationCode;
	}
	public String getDesignationDesc() {
		return designationDesc;
	}
	public void setDesignationDesc(String designationDesc) {
		this.designationDesc = designationDesc;
	}
	public String getGradeCode() {
		return gradeCode;
	}
	public void setGradeCode(String gradeCode) {
		this.gradeCode = gradeCode;
	}
	public String getSalCatCode() {
		return salCatCode;
	}
	public void setSalCatCode(String salCatCode) {
		this.salCatCode = salCatCode;
	}
	public String getSalCatDesc() {
		return salCatDesc;
	}
	public void setSalCatDesc(String salCatDesc) {
		this.salCatDesc = salCatDesc;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getStrStartDate() {
		return strStartDate;
	}
	public void setStrStartDate(String strStartDate) {
		this.strStartDate = strStartDate;
	}
	public String getStrEndDate() {
		return strEndDate;
	}
	public void setStrEndDate(String strEndDate) {
		this.strEndDate = strEndDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getGradeDesc() {
		return gradeDesc;
	}
	public void setGradeDesc(String gradeDesc) {
		this.gradeDesc = gradeDesc;
	}
	private Timestamp cratedDate;
	private String modifiedBy;
	private Timestamp modifiedDate;
}
