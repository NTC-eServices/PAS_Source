package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.ManageRoleDTO;

import lk.informatics.ntc.model.service.EmployeeProfileService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "manageRole")
@ViewScoped
public class ManageRole {
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private String lblValue1 = "Department";
	private String lblValue2 = "Role Name";
	private String dep;
	private String currentRoleName;
	private String sucessMsg;
	private String errorMsg;
	private String errorMessage;

	String beforeEditingRoleName;
	private boolean disableSaveBtn = true;
	private boolean disableClearBtn = true;
	private boolean disableRoleCode = true;
	private boolean checkOnlyDep = false;
	private boolean checkDepAndRole = false;
	private boolean disableRoleName = true;
	private boolean disableEditDepDes = true;
	private String depCodeBeforeEditing;
	private String roleNameBeforeEditing;
	public List<CommonDTO> departmentList = new ArrayList<CommonDTO>(0);
	public List<CommonDTO> roleNameList = new ArrayList<CommonDTO>(0);
	public List<ManageRoleDTO> dataList = new ArrayList<ManageRoleDTO>(0);

	private EmployeeProfileService employeeProfileService;
	private ManageRoleDTO manageRoleDTO = new ManageRoleDTO();
	private CommonDTO commonDTO = new CommonDTO();
	private ManageRoleDTO selectedRow;
	private ManageRoleDTO selectedDeleteRow;
	private ManageRoleDTO selectedRoleDepartmentDes;

	public ManageRole() {
		employeeProfileService = (EmployeeProfileService) SpringApplicationContex.getBean("employeeProfileService");
		departmentList = employeeProfileService.GetDepartmentsToDropdown();

	}

	public List<String> completeTheme(String query) {
		List<CommonDTO> allThemes = departmentList;
		List<String> filteredThemes = new ArrayList<String>();
		for (int i = 0; i < allThemes.size(); i++) {
			if (allThemes.get(i).toString().contains(query)) {
				filteredThemes.add(allThemes.get(i).getDescription().toString());
			}
		}

		return filteredThemes;

	}

	public void ondepartmnetChange() {
		dep = manageRoleDTO.getDepartment();
		manageRoleDTO.setDepFirstCharForRoleCode(null);
		if (dep != null && !dep.equals("")) {
			checkOnlyDep = true;
			checkDepAndRole = false;
			roleNameList = employeeProfileService.getAllRoleNamesList(dep);
		}
		String dep22 = manageRoleDTO.getCurrentDepCode();
		if (manageRoleDTO.getCurrentDepCode() != null && !manageRoleDTO.getCurrentDepCode().equals("")
				&& manageRoleDTO.getCurrentDepCode().length() != 0) {
			selectedRoleDepartmentDes = employeeProfileService
					.getCurrentDepartmentDes(manageRoleDTO.getCurrentDepCode());
			String selectedDepDes = selectedRoleDepartmentDes.getDepartmentDescription();

			int depcodeSize = manageRoleDTO.getCurrentDepCode().length();

			String newDesValue;
			if (depcodeSize >= 3) {
				newDesValue = manageRoleDTO.getCurrentDepCode().substring(0, 3);
				manageRoleDTO.setDepFirstCharForRoleCode(newDesValue);
			} else if (depcodeSize == 2) {
				manageRoleDTO.setDepFirstCharForRoleCode(null);

				newDesValue = manageRoleDTO.getCurrentDepCode() + "D";
				manageRoleDTO.setDepFirstCharForRoleCode(newDesValue);
			} else if (depcodeSize < 2) {

				errorMsg = "You need to select another department.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		}
	}

	public List<String> DisplayroleNameList(String query) {
		String dep1 = commonDTO.getCode();

		roleNameList = employeeProfileService.getAllRoleNamesList(dep1);
		List<CommonDTO> allRoleNames = roleNameList;
		List<String> filteredRoleNames = new ArrayList<String>();
		for (int i = 0; i < allRoleNames.size(); i++) {
			if (allRoleNames.get(i).toString().contains(query)) {
				filteredRoleNames.add(allRoleNames.get(i).getRoleName().toString());
			}
		}
		return filteredRoleNames;

	}

	public void onroleChange() {

		currentRoleName = manageRoleDTO.getRoleName();
		checkOnlyDep = false;
		checkDepAndRole = true;
	}

	public void clearDetails() {

		manageRoleDTO.setCurrentRoleName("");
		manageRoleDTO.setDepartmenteditName("");
		manageRoleDTO.setCurrentDepCode("");
		manageRoleDTO.setCurrentRoleCode("");
		manageRoleDTO.setDepFirstCharForRoleCode("");
		setDisableClearBtn(true);
		setDisableSaveBtn(true);
		setDisableRoleCode(false);
	}

	public void clearAllDetails() {

		manageRoleDTO.setDepartment(null);
		manageRoleDTO.setRoleName(null);
		manageRoleDTO.setCurrentRoleCode(null);
		manageRoleDTO.setCurrentRoleName(null);
		manageRoleDTO.setCurrentDepCode(null);
		manageRoleDTO.setDepFirstCharForRoleCode(null);
		setDisableSaveBtn(true);
		setDisableClearBtn(true);
		dataList.clear();
		setDisableRoleName(true);
		setDisableEditDepDes(true);
	}

	public void searchMethod() {

		if (manageRoleDTO.getDepartment().isEmpty()) {

			setErrorMsg("Department should be selected.");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else if (!manageRoleDTO.getDepartment().isEmpty() && checkOnlyDep == true) {

			dataList = new ArrayList<ManageRoleDTO>(0);
			dataList = employeeProfileService.serachAllDetailsForDep(dep);
			setDisableRoleName(false);
			setDisableEditDepDes(false);
			checkOnlyDep = false;

			RequestContext.getCurrentInstance().update("@form");

		} else if (!(manageRoleDTO.getDepartment().isEmpty() && currentRoleName.isEmpty()) && checkDepAndRole == true) {
			dataList = new ArrayList<ManageRoleDTO>(0);
			dataList = employeeProfileService.searchDetails(dep, currentRoleName);
			setDisableRoleName(false);
			setDisableEditDepDes(false);
			RequestContext.getCurrentInstance().update("@form");
			checkDepAndRole = false;
		}

	}

	public void editAction() {

		String roleCode = selectedRow.getRoleCode();
		String depCode = selectedRow.getDepCode();
		String newDesValue = roleCode.substring(0, 3);
		manageRoleDTO.setDepFirstCharForRoleCode(newDesValue);
		String roleCodeWithoutDepCode = roleCode.substring(4, roleCode.length());
		boolean resultValue = employeeProfileService.checkHaveFunction(roleCode, depCode);

		if (resultValue == false) {

			setDisableSaveBtn(false);
			setDisableClearBtn(false);
			setDisableRoleCode(true);

			manageRoleDTO.setCurrentDepCode(selectedRow.getDepCode());
			manageRoleDTO.setCurrentRoleCode(roleCodeWithoutDepCode);
			manageRoleDTO.setCurrentRoleName(selectedRow.getRoleName());
			manageRoleDTO.setDepartmenteditName(selectedRow.getDepartmentDescription());
			manageRoleDTO.setCurrentStatus(selectedRow.getRoleStatus());
			beforeEditingRoleName = selectedRow.getRoleName();
			roleNameBeforeEditing = selectedRow.getRoleName();
			depCodeBeforeEditing = selectedRow.getDepCode();
			RequestContext.getCurrentInstance().update("roleEditForm");
		} else {

			RequestContext.getCurrentInstance().update("frmerrorMsgeDel");
			errorMsg = "You can not edit this record.";
			RequestContext.getCurrentInstance().execute("PF('errorMsgeForDel').show()");
		}
	}

	public void updateDb() {

		if (!(manageRoleDTO.getCurrentRoleName().isEmpty() || manageRoleDTO.getCurrentRoleCode().isEmpty()
				|| manageRoleDTO.getCurrentDepCode().isEmpty())) {
			manageRoleDTO.setCurrentDepCode(manageRoleDTO.getCurrentDepCode());
			manageRoleDTO.setCurrentRoleCode(manageRoleDTO.getCurrentRoleCode());
			manageRoleDTO.setCurrentRoleName(manageRoleDTO.getCurrentRoleName());

			String status = "A";
			String rolecode = manageRoleDTO.getCurrentRoleCode();
			String depcode = manageRoleDTO.getCurrentDepCode();
			String roleName = manageRoleDTO.getCurrentRoleName();
			String perviousRoleCode = selectedRow.getRoleCode();

			roleNameList = employeeProfileService.getAllRoleNamesList(depcode);
			boolean isDuplicated = true;
			for (int i = 0; i < roleNameList.size(); i++) {

				if (roleName.equals(roleNameList.get(i).getRoleName())) {
					
					isDuplicated = true;

					break;
				} else {
					isDuplicated = false;

				}
			}

			if (!(depcode.equals(depCodeBeforeEditing) && roleName.equals(roleNameBeforeEditing))) {
				if (isDuplicated == false || beforeEditingRoleName.equals(roleName)) {
					selectedRoleDepartmentDes = employeeProfileService.getCurrentDepartmentDes(depcode);
					String selectedCurrentDepDes = selectedRoleDepartmentDes.getDepartmentDescription();

					String newValue = manageRoleDTO.getDepFirstCharForRoleCode() + "_" + rolecode;
					String modifyBy = sessionBackingBean.loginUser;
					int result = employeeProfileService.updateRoleManagement(newValue, depcode, roleName, status,
							perviousRoleCode, modifyBy);
					if (result == 0) {
						RequestContext.getCurrentInstance().update("frmEmpDetails");
						sucessMsg = "Successfully Saved.";
						RequestContext.getCurrentInstance().execute("PF('successSve').show()");

						selectedRoleDepartmentDes = employeeProfileService.getCurrentDepartmentDes(depcode);
						selectedRow.setRoleName(roleName);
						selectedRow.setDepartmentDescription(selectedRoleDepartmentDes.getDepartmentDescription());
						selectedRow.setRoleCode(rolecode);
						selectedRow.setDepCode(depcode);
						selectedRow.setRoleStatus("Active");
						String updateRoleCode = selectedRow.getRoleCode();
						String updateRoleName = selectedRow.getRoleName();
						String updateDepCode = selectedRow.getDepCode();

						if (!dep.isEmpty() && currentRoleName == null) {
							dataList = new ArrayList<ManageRoleDTO>(0);
							dataList = employeeProfileService.serachAllDetailsForDep(dep);
						} else if (!dep.isEmpty() && !currentRoleName.isEmpty()) {

							dataList = new ArrayList<ManageRoleDTO>(0);
							dataList = employeeProfileService.searchDetails(dep, currentRoleName);
						}
						manageRoleDTO.setCurrentRoleCode("");
						manageRoleDTO.setCurrentRoleName("");
						manageRoleDTO.setCurrentDepCode("");
						manageRoleDTO.setDepFirstCharForRoleCode("");
						setDisableSaveBtn(true);
						setDisableClearBtn(true);

					} else {
						errorMsg = "Role Code should be uniqued.";
						RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
					}
				} else {

					errorMsg = "Role Name should be uniqued.";
					RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
				}
			} else {

				errorMsg = "Please update record before save.";
				RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");

			}
		} else if (manageRoleDTO.getCurrentRoleCode().isEmpty() || manageRoleDTO.getCurrentRoleName().isEmpty()
				|| manageRoleDTO.getCurrentDepCode().isEmpty()) {
			if (manageRoleDTO.getCurrentRoleCode().isEmpty()) {

				errorMsg = "Role Code should be entered.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			} else if (manageRoleDTO.getCurrentRoleName().isEmpty()) {

				errorMsg = "Role Name should be entered.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			} else if (manageRoleDTO.getCurrentDepCode().isEmpty()) {

				errorMsg = "Department should be selected.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

		}

	}

	public void deleteRoleButtonAction() {

		String roleCode = selectedDeleteRow.getRoleCode();
		String depCode = selectedDeleteRow.getDepCode();

		boolean resultValue = employeeProfileService.checkHaveFunction(roleCode, depCode);

		if (resultValue == false) {
			RequestContext.getCurrentInstance().execute("PF('deleteconfirmationRole').show()");
		} else {

			errorMsg = "You can not delete this record.";
			RequestContext.getCurrentInstance().update("frmerrorMsgeDel");
			RequestContext.getCurrentInstance().execute("PF('errorMsgeForDel').show()");
		}

		setDisableRoleCode(false);
		manageRoleDTO.setCurrentRoleCode("");
		manageRoleDTO.setCurrentRoleName("");
		manageRoleDTO.setDepCode("");
		manageRoleDTO.setDepFirstCharForRoleCode("");
	}

	public void deleteRole() {

		RequestContext.getCurrentInstance().update("@form");
		String roleCode = selectedDeleteRow.getRoleCode();
		String depCode = selectedDeleteRow.getDepCode();

		int result = employeeProfileService.deleteCurrentRoleDetails(roleCode);
		if (result == 0) {
			RequestContext.getCurrentInstance().update("frmEmpDetails");

			sucessMsg = "Successfully Deleted.";
			RequestContext.getCurrentInstance().execute("PF('successSve').show()");
			dataList.remove(selectedDeleteRow);

			RequestContext.getCurrentInstance().execute("PF('deleteWid').hide()");
		} else {
			errorMsg = "You can not delete this record.";
			RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
		}

	}

	public boolean isDisableSaveBtn() {
		return disableSaveBtn;
	}

	public void setDisableSaveBtn(boolean disableSaveBtn) {
		this.disableSaveBtn = disableSaveBtn;
	}

	public String getLblValue2() {
		return lblValue2;
	}

	public void setLblValue2(String lblValue2) {
		this.lblValue2 = lblValue2;
	}

	public List<ManageRoleDTO> getDataList() {
		return dataList;
	}

	public void setDataList(List<ManageRoleDTO> dataList) {
		this.dataList = dataList;
	}

	public List<CommonDTO> getRoleNameList() {
		return roleNameList;
	}

	public void setRoleNameList(List<CommonDTO> roleNameList) {
		this.roleNameList = roleNameList;
	}

	public String getLblValue1() {
		return lblValue1;
	}

	public List<CommonDTO> getDepartmentList() {
		return departmentList;
	}

	public void setDepartmentList(List<CommonDTO> departmentList) {
		this.departmentList = departmentList;
	}

	public EmployeeProfileService getEmployeeProfileService() {
		return employeeProfileService;
	}

	public void setEmployeeProfileService(EmployeeProfileService employeeProfileService) {
		this.employeeProfileService = employeeProfileService;
	}

	public ManageRoleDTO getManageRoleDTO() {
		return manageRoleDTO;
	}

	public void setManageRoleDTO(ManageRoleDTO manageRoleDTO) {
		this.manageRoleDTO = manageRoleDTO;
	}

	public void setLblValue1(String lblValue1) {
		this.lblValue1 = lblValue1;
	}

	public ManageRoleDTO getSelectedDeleteRow() {
		return selectedDeleteRow;
	}

	public void setSelectedDeleteRow(ManageRoleDTO selectedDeleteRow) {
		this.selectedDeleteRow = selectedDeleteRow;
	}

	public ManageRoleDTO getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(ManageRoleDTO selectedRow) {
		this.selectedRow = selectedRow;
	}

	public CommonDTO getCommonDTO() {
		return commonDTO;
	}

	public void setCommonDTO(CommonDTO commonDTO) {
		this.commonDTO = commonDTO;
	}

	public boolean isDisableClearBtn() {
		return disableClearBtn;
	}

	public boolean isCheckOnlyDep() {
		return checkOnlyDep;
	}

	public void setCheckOnlyDep(boolean checkOnlyDep) {
		this.checkOnlyDep = checkOnlyDep;
	}

	public boolean isCheckDepAndRole() {
		return checkDepAndRole;
	}

	public void setCheckDepAndRole(boolean checkDepAndRole) {
		this.checkDepAndRole = checkDepAndRole;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public void setDisableClearBtn(boolean disableClearBtn) {
		this.disableClearBtn = disableClearBtn;
	}

	public ManageRoleDTO getSelectedRoleDepartmentDes() {
		return selectedRoleDepartmentDes;
	}

	public void setSelectedRoleDepartmentDes(ManageRoleDTO selectedRoleDepartmentDes) {
		this.selectedRoleDepartmentDes = selectedRoleDepartmentDes;
	}

	public String getSucessMsg() {
		return sucessMsg;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public void setSucessMsg(String sucessMsg) {
		this.sucessMsg = sucessMsg;
	}

	public boolean isDisableRoleCode() {
		return disableRoleCode;
	}

	public void setDisableRoleCode(boolean disableRoleCode) {
		this.disableRoleCode = disableRoleCode;
	}

	public boolean isDisableRoleName() {
		return disableRoleName;
	}

	public void setDisableRoleName(boolean disableRoleName) {
		this.disableRoleName = disableRoleName;
	}

	public boolean isDisableEditDepDes() {
		return disableEditDepDes;
	}

	public void setDisableEditDepDes(boolean disableEditDepDes) {
		this.disableEditDepDes = disableEditDepDes;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

}