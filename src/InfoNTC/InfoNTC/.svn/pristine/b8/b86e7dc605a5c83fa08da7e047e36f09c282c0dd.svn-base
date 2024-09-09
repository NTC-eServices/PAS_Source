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

@ViewScoped
@ManagedBean(name = "createNewRoleBean")
public class CreateNewRoleBean {
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private String dep;
	private String errorMsg;
	private String sucessMsg;
	String depDescription;
	String beforeEditingRoleName;
	private boolean showbtnAddAfterEdit = false;
	private boolean showbtnAddBeforeEdit = true;
	private boolean disableRoleCodeField = false;
	private boolean editBtnPressToClean = false;
	private String depCodeBeforeEditing;
	private String roleNameBeforeEditing;

	public List<CommonDTO> departmentList = new ArrayList<CommonDTO>(0);
	public List<CommonDTO> roleNameList = new ArrayList<CommonDTO>(0);
	public List<ManageRoleDTO> dataList = new ArrayList<ManageRoleDTO>(0);

	private EmployeeProfileService employeeProfileService;
	private ManageRoleDTO manageRoleDTO = new ManageRoleDTO();
	private CommonDTO commonDTO = new CommonDTO();
	private ManageRoleDTO selectedEditRow;
	private ManageRoleDTO selectedDeleteRow;
	private ManageRoleDTO selectedRoleDepartmentDes;

	public CreateNewRoleBean() {
		employeeProfileService = (EmployeeProfileService) SpringApplicationContex.getBean("employeeProfileService");
		departmentList = employeeProfileService.GetDepartmentsToDropdown();

	}

	public void ondepartmnetChange() {

		dep = manageRoleDTO.getDepartment();
		selectedRoleDepartmentDes = employeeProfileService.getCurrentDepartmentDes(dep);
		String selectedDepDes = selectedRoleDepartmentDes.getDepartmentDescription();
		int depcodeSize = dep.length();
		
		String newDesValue;
		if (depcodeSize >= 3) {
			newDesValue = dep.substring(0, 3);
			manageRoleDTO.setDepFirstCharForRoleCode(newDesValue);
		} else if (depcodeSize == 2) {
			manageRoleDTO.setDepFirstCharForRoleCode(null);
			
			newDesValue = dep + "D";
			manageRoleDTO.setDepFirstCharForRoleCode(newDesValue);
		} else if (depcodeSize < 2) {
			
			errorMsg = "You need to select another department.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

		if (dep != null && !dep.equals(""))
			roleNameList = employeeProfileService.getAllRoleNamesList(dep);
	}

	public void clearDetails() {
		if (editBtnPressToClean == true) {
			clearAfterEditBtn();
		} else {

			manageRoleDTO.setCurrentRoleName("");
			manageRoleDTO.setCurrentRoleCode("");
			manageRoleDTO.setDepartment("");
			manageRoleDTO.setDepFirstCharForRoleCode("");
		}

	}

	public void clearAfterEditBtn() {
		manageRoleDTO.setCurrentRoleName("");
		manageRoleDTO.setDepartment("");
		manageRoleDTO.setCurrentRoleCode("");
		manageRoleDTO.setDepFirstCharForRoleCode("");

		setShowbtnAddAfterEdit(false);
		setShowbtnAddBeforeEdit(true);
		setDisableRoleCodeField(false);
	}

	public void onroleChange() {

	}

	public void updateDb() {
		if (!(manageRoleDTO.getCurrentRoleCode().isEmpty() || manageRoleDTO.getCurrentRoleName().isEmpty()
				|| manageRoleDTO.getDepartment().isEmpty())) {
			
			String newRoleCode = manageRoleDTO.getCurrentRoleCode();
			String depcode = manageRoleDTO.getDepartment();
			String newRoleName = manageRoleDTO.getCurrentRoleName();
			String newStatus = "A";
			manageRoleDTO.setRoleStatus(newStatus);

			manageRoleDTO.setCreatedBy("admin");
			String createdBy = sessionBackingBean.loginUser;
			roleNameList = employeeProfileService.getAllRoleNamesList(depcode);

			int size = roleNameList.size();
			boolean isDuplicated = true;
			for (int i = 0; i < size; i++) {

				if (newRoleName.equals(roleNameList.get(i).getRoleName())) {

					isDuplicated = true;

					break;
				} else {
					isDuplicated = false;

				}
			}
			if (roleNameList.isEmpty()) {
				isDuplicated = false;
			} else {

			}

			if (isDuplicated == false) {
				selectedRoleDepartmentDes = employeeProfileService.getCurrentDepartmentDes(depcode);
				String selectedCurrentDepDes = selectedRoleDepartmentDes.getDepartmentDescription();

				String newValue = manageRoleDTO.getDepFirstCharForRoleCode() + "-" + newRoleCode;

				int result = employeeProfileService.insertNewRole(newValue, depcode, newRoleName, newStatus, createdBy);
				if (result == 0) {
					RequestContext.getCurrentInstance().update("frmEmpDetails");

					sucessMsg = "Successfully Saved.";
					RequestContext.getCurrentInstance().execute("PF('successSve').show()");
					selectedRoleDepartmentDes = employeeProfileService.getCurrentDepartmentDes(depcode);

					ManageRoleDTO dtoObj = new ManageRoleDTO();
					dtoObj.setDepartment(depcode);
					dtoObj.setDepartmentDescription(selectedRoleDepartmentDes.getDepartmentDescription());
					dtoObj.setDepCode(depcode);
					dtoObj.setRoleCode(newValue);
					dtoObj.setRoleName(newRoleName);
					dtoObj.setNewRoleCodeWithoutDepName(newRoleCode);
					dtoObj.setRoleStatus("Active");

					dataList.add(dtoObj);

					manageRoleDTO.setCurrentRoleName("");
					manageRoleDTO.setCurrentRoleCode("");
					manageRoleDTO.setDepartment("");
					manageRoleDTO.setDepFirstCharForRoleCode("");

				} else {
					errorMsg = "Role Code should be uniqued.";
					RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
				}
			} else {
				errorMsg = "Role Name should be uniqued.";
				RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");

			}

		} else if (manageRoleDTO.getCurrentRoleCode().isEmpty() || manageRoleDTO.getCurrentRoleName().isEmpty()
				|| manageRoleDTO.getDepartment().isEmpty()) {
			if (manageRoleDTO.getCurrentRoleCode().isEmpty()) {

				errorMsg = "Role Code should be entered.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			} else if (manageRoleDTO.getCurrentRoleName().isEmpty()) {

				errorMsg = "Role Name should be entered.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			} else if (manageRoleDTO.getDepartment().isEmpty()) {

				errorMsg = "Department should be selected.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

		}
	}

	public void editAction() {
		manageRoleDTO.setCurrentRoleName(selectedEditRow.getRoleName());
		manageRoleDTO.setCurrentRoleCode(selectedEditRow.getNewRoleCodeWithoutDepName());
		manageRoleDTO.setDepartment(selectedEditRow.getDepCode());
		manageRoleDTO.setCurrentStatus("A");
		String newDesValue = selectedEditRow.getRoleCode().substring(0, 3);
		manageRoleDTO.setDepFirstCharForRoleCode(newDesValue);

		beforeEditingRoleName = selectedEditRow.getRoleName();
		roleNameBeforeEditing = selectedEditRow.getRoleName();
		depCodeBeforeEditing = selectedEditRow.getDepCode();

		setShowbtnAddAfterEdit(true);
		setShowbtnAddBeforeEdit(false);
		setDisableRoleCodeField(true);
		setEditBtnPressToClean(true);
	}

	public void deleteRoleButtonAction() {

		RequestContext.getCurrentInstance().execute("PF('deleteconfirmationRole').show()");
		setShowbtnAddAfterEdit(false);
		setShowbtnAddBeforeEdit(true);
		setDisableRoleCodeField(false);
		setEditBtnPressToClean(false);
		manageRoleDTO.setCurrentRoleName("");
		manageRoleDTO.setCurrentRoleCode("");
		manageRoleDTO.setDepartment("");
	}

	public void removeAction() {
		String deleteRoleName = selectedDeleteRow.getRoleName();
		String deleteRoleCode = selectedDeleteRow.getRoleCode();
		String delteDepCode = selectedDeleteRow.getDepCode();
		boolean resultValue = employeeProfileService.checkHaveFunction(deleteRoleCode, delteDepCode);

		if (resultValue == false) {
			int result = employeeProfileService.deleteCurrentRoleDetails(deleteRoleCode);
			if (result == 0) {
				RequestContext.getCurrentInstance().update("frmEmpDetails");

				sucessMsg = "Successfully Deleted.";
				RequestContext.getCurrentInstance().execute("PF('successSve').show()");
				dataList.remove(selectedDeleteRow);

			} else {
				errorMsg = "You can not delete this record.";
				RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
			}
		} else {

			errorMsg = "You can not delete this record.";
			RequestContext.getCurrentInstance().execute("PF('errorMsgeForDel').show()");
		}

	}

	public void editDb() {
		if (!(manageRoleDTO.getDepartment().isEmpty() || manageRoleDTO.getCurrentRoleCode().isEmpty()
				|| manageRoleDTO.getCurrentRoleName().isEmpty())) {
			manageRoleDTO.setCurrentDepCode(manageRoleDTO.getDepartment());

			manageRoleDTO.setCurrentRoleCode(selectedEditRow.getRoleCode());
			manageRoleDTO.setCurrentRoleName(manageRoleDTO.getCurrentRoleName());

			String status = "A";
			String beforerolecode = manageRoleDTO.getCurrentRoleCode();
			String rolecode = selectedEditRow.getNewRoleCodeWithoutDepName();
			String depcode = manageRoleDTO.getCurrentDepCode();
			String roleName = manageRoleDTO.getCurrentRoleName();

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

					String newValue = manageRoleDTO.getDepFirstCharForRoleCode() + "-" + rolecode;

					String modifyBy = sessionBackingBean.loginUser;
					int result = employeeProfileService.updateRoleManagement(newValue, depcode, roleName, status,
							beforerolecode, modifyBy);
					if (result == 0) {
						RequestContext.getCurrentInstance().update("frmEmpDetails");

						sucessMsg = "Successfully Updated.";
						RequestContext.getCurrentInstance().execute("PF('successSve').show()");
						selectedRoleDepartmentDes = employeeProfileService.getCurrentDepartmentDes(depcode);
						selectedEditRow.setRoleName(roleName);
						selectedEditRow.setRoleCode(newValue);
						selectedEditRow.setDepCode(depcode);
						selectedEditRow.setRoleStatus("Active");
						selectedEditRow.setNewRoleCodeWithoutDepName(rolecode);
						selectedEditRow.setDepartmentDescription(selectedRoleDepartmentDes.getDepartmentDescription());

						setShowbtnAddAfterEdit(false);
						setShowbtnAddBeforeEdit(true);
						setDisableRoleCodeField(false);
						manageRoleDTO.setCurrentRoleName("");
						manageRoleDTO.setCurrentRoleCode("");
						manageRoleDTO.setDepartment("");
						manageRoleDTO.setDepFirstCharForRoleCode("");
						setEditBtnPressToClean(false);
					} else {
						errorMsg = "Role Code should be uniqued";
						RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
						setDisableRoleCodeField(true);
					}
				} else {

					errorMsg = "Role Name should be uniqued.";
					RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
					setDisableRoleCodeField(true);
				}
			} else {

				errorMsg = "Please update record before save.";
				RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");

			}
		} else if (manageRoleDTO.getDepartment().isEmpty() || manageRoleDTO.getCurrentRoleName().isEmpty()
				|| manageRoleDTO.getCurrentRoleCode().isEmpty()) {

			if (manageRoleDTO.getCurrentRoleCode().isEmpty()) {

				errorMsg = "Role Code should be entered.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			} else if (manageRoleDTO.getCurrentRoleName().isEmpty()) {

				errorMsg = "Role Name should be entered.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			} else if (manageRoleDTO.getDepartment().isEmpty()) {

				errorMsg = "Department should be selected.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		}

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

	public CommonDTO getCommonDTO() {
		return commonDTO;
	}

	public void setCommonDTO(CommonDTO commonDTO) {
		this.commonDTO = commonDTO;
	}

	public List<CommonDTO> getDepartmentList() {
		return departmentList;
	}

	public void setDepartmentList(List<CommonDTO> departmentList) {
		this.departmentList = departmentList;
	}

	public List<CommonDTO> getRoleNameList() {
		return roleNameList;
	}

	public void setRoleNameList(List<CommonDTO> roleNameList) {
		this.roleNameList = roleNameList;
	}

	public List<ManageRoleDTO> getDataList() {
		return dataList;
	}

	public void setDataList(List<ManageRoleDTO> dataList) {
		this.dataList = dataList;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public ManageRoleDTO getSelectedEditRow() {
		return selectedEditRow;
	}

	public void setSelectedEditRow(ManageRoleDTO selectedEditRow) {
		this.selectedEditRow = selectedEditRow;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public ManageRoleDTO getSelectedDeleteRow() {
		return selectedDeleteRow;
	}

	public void setSelectedDeleteRow(ManageRoleDTO selectedDeleteRow) {
		this.selectedDeleteRow = selectedDeleteRow;
	}

	public String getSucessMsg() {
		return sucessMsg;
	}

	public void setSucessMsg(String sucessMsg) {
		this.sucessMsg = sucessMsg;
	}

	public ManageRoleDTO getSelectedRoleDepartmentDes() {
		return selectedRoleDepartmentDes;
	}

	public void setSelectedRoleDepartmentDes(ManageRoleDTO selectedRoleDepartmentDes) {
		this.selectedRoleDepartmentDes = selectedRoleDepartmentDes;
	}

	public boolean isShowbtnAddAfterEdit() {
		return showbtnAddAfterEdit;
	}

	public boolean isShowbtnAddBeforeEdit() {
		return showbtnAddBeforeEdit;
	}

	public boolean isDisableRoleCodeField() {
		return disableRoleCodeField;
	}

	public void setDisableRoleCodeField(boolean disableRoleCodeField) {
		this.disableRoleCodeField = disableRoleCodeField;
	}

	public void setShowbtnAddBeforeEdit(boolean showbtnAddBeforeEdit) {
		this.showbtnAddBeforeEdit = showbtnAddBeforeEdit;
	}

	public void setShowbtnAddAfterEdit(boolean showbtnAddAfterEdit) {
		this.showbtnAddAfterEdit = showbtnAddAfterEdit;
	}

	public boolean isEditBtnPressToClean() {
		return editBtnPressToClean;
	}

	public void setEditBtnPressToClean(boolean editBtnPressToClean) {
		this.editBtnPressToClean = editBtnPressToClean;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}
}
