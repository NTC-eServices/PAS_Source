package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import lk.informatics.ntc.model.dto.AccessPermissionDTO;
import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.EmployeeDTO;
import lk.informatics.ntc.model.service.AccessPermissionService;
import lk.informatics.ntc.model.service.EmployeeProfileService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "viewAccessPermissionBackingBean")
@ViewScoped
public class ViewAccessPermissionBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private List<CommonDTO> deparmentList;
	private List<CommonDTO> roleList;
	private List<CommonDTO> funList;
	private List<CommonDTO> activityList;
	private EmployeeProfileService employeeProfileService;
	private AccessPermissionService accessPermissionService;

	private String strSelectedDeparmentCode;
	private String strSelectedRoleCode;
	private String strSelectedFunCode;
	private String strSelectedActivityCode;
	private List<EmployeeDTO> roleDetaillist = new ArrayList<EmployeeDTO>();
	private List<AccessPermissionDTO> searchDetaillist = new ArrayList<AccessPermissionDTO>();

	@PostConstruct
	public void init() {
		loadValuesforDropdown();
	}

	public void loadValuesforDropdown() {
		employeeProfileService = (EmployeeProfileService) SpringApplicationContex.getBean("employeeProfileService");
		accessPermissionService = (AccessPermissionService) SpringApplicationContex.getBean("accessPermissionService");
		deparmentList = employeeProfileService.GetDepartmentsToDropdown();
		roleList = employeeProfileService.GetRolesToDropdown();
		funList = accessPermissionService.GetFunctionToDropdown();
		activityList = accessPermissionService.GetActivityToDropdown();
		searchDetaillist = accessPermissionService.GetAllAccessPermission();
	}

	public void ondepartmnetChange() {

		if (strSelectedDeparmentCode != null && !strSelectedDeparmentCode.equals("")) {
			roleList = employeeProfileService.getAllRoleNamesList(strSelectedDeparmentCode);
		} else {
			// Get All Roles
			roleList = null;
		}
	}

	public void searchDetails() {
		searchDetaillist = accessPermissionService.SearchAccessPermission(strSelectedDeparmentCode, strSelectedRoleCode,
				strSelectedFunCode, strSelectedActivityCode);
	}

	public void clearForm() {

		strSelectedDeparmentCode = null;
		strSelectedRoleCode = null;
		strSelectedFunCode = null;
		strSelectedActivityCode = null;
		deparmentList = null;
		roleList = null;
		funList = null;
		activityList = null;
		init();
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public List<CommonDTO> getDeparmentList() {
		return deparmentList;
	}

	public void setDeparmentList(List<CommonDTO> deparmentList) {
		this.deparmentList = deparmentList;
	}

	public List<CommonDTO> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<CommonDTO> roleList) {
		this.roleList = roleList;
	}

	public EmployeeProfileService getEmployeeProfileService() {
		return employeeProfileService;
	}

	public void setEmployeeProfileService(EmployeeProfileService employeeProfileService) {
		this.employeeProfileService = employeeProfileService;
	}

	public String getStrSelectedDeparmentCode() {
		return strSelectedDeparmentCode;
	}

	public void setStrSelectedDeparmentCode(String strSelectedDeparmentCode) {
		this.strSelectedDeparmentCode = strSelectedDeparmentCode;
	}

	public String getStrSelectedRoleCode() {
		return strSelectedRoleCode;
	}

	public void setStrSelectedRoleCode(String strSelectedRoleCode) {
		this.strSelectedRoleCode = strSelectedRoleCode;
	}

	public List<EmployeeDTO> getRoleDetaillist() {
		return roleDetaillist;
	}

	public void setRoleDetaillist(List<EmployeeDTO> roleDetaillist) {
		this.roleDetaillist = roleDetaillist;
	}

	public String getStrSelectedFunCode() {
		return strSelectedFunCode;
	}

	public void setStrSelectedFunCode(String strSelectedFunCode) {
		this.strSelectedFunCode = strSelectedFunCode;
	}

	public List<CommonDTO> getFunList() {
		return funList;
	}

	public void setFunList(List<CommonDTO> funList) {
		this.funList = funList;
	}

	public List<CommonDTO> getActivityList() {
		return activityList;
	}

	public void setActivityList(List<CommonDTO> activityList) {
		this.activityList = activityList;
	}

	public AccessPermissionService getAccessPermissionService() {
		return accessPermissionService;
	}

	public void setAccessPermissionService(AccessPermissionService accessPermissionService) {
		this.accessPermissionService = accessPermissionService;
	}

	public String getStrSelectedActivityCode() {
		return strSelectedActivityCode;
	}

	public void setStrSelectedActivityCode(String strSelectedActivityCode) {
		this.strSelectedActivityCode = strSelectedActivityCode;
	}

	public List<AccessPermissionDTO> getSearchDetaillist() {
		return searchDetaillist;
	}

	public void setSearchDetaillist(List<AccessPermissionDTO> searchDetaillist) {
		this.searchDetaillist = searchDetaillist;
	}

}
