package lk.informatics.ntc.model.dto;

public class SearchEmployeeDTO {

	private String epfNo;
	private String empStatus;
	private String nicNo;
	private String empLocation;
	private String userId;

	private String fullName;
	private String userRole;
	private String accountReq;
	private String department;
	private String designation;
	private boolean accountrequest;
	private String accountstring;
	private String empNo;
	
	public boolean isAccountrequest() {
		return accountrequest;
	}
	public void setAccountrequest(boolean accountrequest) {
		this.accountrequest = accountrequest;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	
	
	public String getAccountReq() {
		return accountReq;
	}
	public void setAccountReq(String accountReq) {
		this.accountReq = accountReq;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public String getEpfNo() {
		return epfNo;
	}
	public void setEpfNo(String epfNo) {
		this.epfNo = epfNo.toUpperCase();
	}
	public String getEmpStatus() {
		return empStatus;
	}
	public void setEmpStatus(String empStatus) {
		this.empStatus = empStatus;
	}
	public String getNicNo() {
		return nicNo;
	}
	public void setNicNo(String nicNo) {
		this.nicNo = nicNo;
	}
	public String getEmpLocation() {
		return empLocation;
	}
	public void setEmpLocation(String empLocation) {
		this.empLocation = empLocation;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

	
	public String getAccountstring() {
		return accountstring;
	}
	public void setAccountstring(String accountstring) {
		this.accountstring = accountstring;
		
		if (accountstring.equals("y")){
			
			setAccountrequest(true);
		}else {
			setAccountrequest(false);
		}
			
		
	}
	public String getEmpNo() {
		return empNo;
	}
	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}
	
	
	
}
