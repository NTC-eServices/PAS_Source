package lk.informatics.ntc.model.dto;

import java.io.Serializable;

public class UserRoleDTO implements Serializable{

	private static final long serialVersionUID = 8344243187243931903L;
	
	private String userName;
	private String roleCode;
	private String roleDescription;
	
	
	public String getRoleDescription() {
		return roleDescription;
	}
	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRoleCode() {
		return roleCode;
	}
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
	@Override
	public String toString() {
		return "UserRoleDTO [ roleCode=" + roleCode + "]";
	}
	
	
	

}
