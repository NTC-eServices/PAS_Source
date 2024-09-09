package lk.informatics.ntc.model.service;

import java.io.Serializable;
import java.util.List;

import lk.informatics.ntc.model.dto.AccessPermissionDTO;
import lk.informatics.ntc.model.dto.CommonDTO;

public interface AccessPermissionService extends Serializable {

	public List<AccessPermissionDTO> GetDeptToDropdown();

	public List<AccessPermissionDTO> GetRoleToDropdown(String selectedDept);

	public List<CommonDTO> GetFunctionToDropdown();

	public List<CommonDTO> GetActivityToDropdown();

	public List<AccessPermissionDTO> SearchAccessPermission(String deptCode, String roleCode, String funCode,
			String activityCode);

	public List<AccessPermissionDTO> GetAllAccessPermission();

	public List<AccessPermissionDTO> getDeptToDropdown();

	public List<AccessPermissionDTO> getRoleToDropdown(String selectedDept);

	public List<AccessPermissionDTO> getFunctionActivity(String deptCode, String roleCode);

	public List<AccessPermissionDTO> getFuncToDropdown();

	public String getFunDescToText(String selectedFunCode);

	public List<AccessPermissionDTO> getActivityToDropdown(String selectedFunCode);

	public List<AccessPermissionDTO> saveFunctionActivity(String selectedDept, String selectedRole,
			String selectedFunCode, String selectedActCode, String createdBy);

	public void removeFunctionActivity(int selectedFRA);

	public String chkDuplicates(String selectedDept, String selectedRole, String selectedFunCode,
			String selectedActivity);

	public List<AccessPermissionDTO> getFunctionActivityForOnlyDep(String selectedDept);
	String getRemovedFunctionId(int selectedFRA);
}
