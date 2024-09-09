package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.EmployeeDTO;
import lk.informatics.ntc.model.service.EmployeeProfileService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "roleBackingBean")
@ViewScoped
public class RoleBackingBean {

	private List<CommonDTO> deparmentList;
	private List<CommonDTO> roleList;
	private EmployeeProfileService employeeProfileService;
	private String strSelectedDeparmentCode;
	private String strSelectedRoleCode;
	private List<EmployeeDTO> roleDetaillist = new ArrayList<EmployeeDTO>();

	@PostConstruct
	public void init() {
		loadValuesforDropdown();
	}

	public void loadValuesforDropdown() {
		employeeProfileService = (EmployeeProfileService) SpringApplicationContex.getBean("employeeProfileService");
		deparmentList = employeeProfileService.GetDepartmentsToDropdown();
		roleList = employeeProfileService.GetRolesToDropdown();
		setRoleDetaillist(employeeProfileService.GetDeptandRoles());
	}

	public void clearForm() {

		strSelectedDeparmentCode = null;
		strSelectedRoleCode = null;
		deparmentList = null;
		roleList = null;
		init();
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
		roleDetaillist = employeeProfileService.SearchDeptandRoles(strSelectedDeparmentCode, strSelectedRoleCode);
	}

	public List<CommonDTO> getDeparmentList() {
		return deparmentList;
	}

	public void setDeparmentList(List<CommonDTO> deparmentList) {
		this.deparmentList = deparmentList;
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

	public List<CommonDTO> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<CommonDTO> roleList) {
		this.roleList = roleList;
	}

	public List<EmployeeDTO> getRoleDetaillist() {
		return roleDetaillist;
	}

	public void setRoleDetaillist(List<EmployeeDTO> roleDetaillist) {
		this.roleDetaillist = roleDetaillist;
	}

}
