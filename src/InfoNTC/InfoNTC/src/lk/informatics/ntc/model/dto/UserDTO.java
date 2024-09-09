package lk.informatics.ntc.model.dto;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


public class UserDTO implements Serializable,  UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2318166231921515996L;

	
	private String username;
	private String password;
	private String userRole;
	private boolean accountNonExpired;
	private boolean accountNonLocked;
	private boolean credentialsNonExpired;
	private boolean enabled;
	public String sessionID;
	private String status;
	String moduleTD =null;
	String moduleVISA =null;
	String moduleCIT =null;
	private String sessionId;
	
	//added by kalpanie on 2019/10/23
	private String empNo;
	private String empFullname;
	

	private String userFunction; 
	private Map<String,String> functionList = new HashMap<String,String>(0);  


	private Set<String> grantedAuthorities = new HashSet<String>();
	private Collection<GrantedAuthority> authorities;

	private Map<String,String> roleList = new HashMap<String,String>(0);
	
	private String emailAddress;
	private String initialPasswordChange;
	 
	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getInitialPasswordChange() {
		return initialPasswordChange;
	}

	public void setInitialPasswordChange(String initialPasswordChange) {
		this.initialPasswordChange = initialPasswordChange;
	}

	public UserDTO() {
		super();
	}
	
	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	
	public Collection<GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Collection<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	public Set<String> getGrantedAuthorities() {
		return grantedAuthorities;
	}

	public void setGrantedAuthorities(Set<String> grantedAuthorities) {
		this.grantedAuthorities = grantedAuthorities;
	}

	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getModuleTD() {
		return moduleTD;
	}

	public void setModuleTD(String moduleTD) {
		this.moduleTD = moduleTD;
	}

	public String getModuleVISA() {
		return moduleVISA;
	}

	public void setModuleVISA(String moduleVISA) {
		this.moduleVISA = moduleVISA;
	}

	public String getModuleCIT() {
		return moduleCIT;
	}

	public void setModuleCIT(String moduleCIT) {
		this.moduleCIT = moduleCIT;
	}

	public Map<String, String> getRoleList() {
		return roleList;
	}

	public void setRoleList(Map<String, String> roleList) {
		this.roleList = roleList;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserFunction() {
		return userFunction;
	}

	public void setUserFunction(String userFunction) {
		this.userFunction = userFunction;
	}

	public Map<String,String> getFunctionList() {
		return functionList;
	}

	public void setFunctionList(Map<String,String> functionList) {
		this.functionList = functionList;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getEmpNo() {
		return empNo;
	}

	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}

	public String getEmpFullname() {
		return empFullname;
	}

	public void setEmpFullname(String empFullname) {
		this.empFullname = empFullname;
	}



}
