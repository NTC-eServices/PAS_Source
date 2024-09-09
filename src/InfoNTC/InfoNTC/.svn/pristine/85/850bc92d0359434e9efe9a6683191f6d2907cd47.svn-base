package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.AccessPermissionDTO;
import lk.informatics.ntc.model.service.AccessPermissionService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "assignAccessPermissionsBackingBean")
@ViewScoped
public class AssignAccessPermissionsBackingBean {

	// Services
	private AccessPermissionService accessPermissionService;

	// DTOs
	private List<AccessPermissionDTO> deptList = new ArrayList<AccessPermissionDTO>();
	private List<AccessPermissionDTO> roleList = new ArrayList<AccessPermissionDTO>();
	private List<AccessPermissionDTO> funList = new ArrayList<AccessPermissionDTO>();
	private List<AccessPermissionDTO> actList = new ArrayList<AccessPermissionDTO>();
	private AccessPermissionDTO accessPermissionDTO;

	private List<AccessPermissionDTO> funActList = new ArrayList<AccessPermissionDTO>();

	// SelectedValues
	private String selectedDept;
	private String selectedRole;
	private String selectedFunCode;
	private String selectedFunDesc;
	private String selectedActCode;

	private int selectedFRA;
	private String errorMsg;
	private String sucessMsg;
	private boolean checkOnlyDep = false;
	private boolean checkDepAndRole = false;
	private boolean afterSavingMethod = false;
	private boolean disableFunIdTxtField = true;
	private boolean disableFunNameTxtField = true;
	private boolean disableActivityTxtField = true;
	private boolean disableSaveBtn = true;
	private boolean disableClearBtn = true;

	@PostConstruct
	public void init() {
		setAccessPermissionDTO(new AccessPermissionDTO());

		LoadValuesforDropdown();
	}

	// Methods

	public void LoadValuesforDropdown() {
		accessPermissionService = (AccessPermissionService) SpringApplicationContex.getBean("accessPermissionService");
		deptList = accessPermissionService.getDeptToDropdown();

	}

	// handle changes
	public void onDepartmentChange() {

		if (selectedDept != null) {

			checkOnlyDep = true;
			checkDepAndRole = false;
			accessPermissionService = (AccessPermissionService) SpringApplicationContex
					.getBean("accessPermissionService");

			roleList = accessPermissionService.getRoleToDropdown(selectedDept);
		}

	}

	public void onroleChange() {
		if (selectedRole.isEmpty()) {
			checkOnlyDep = true;
			checkDepAndRole = false;
		} else {
			checkDepAndRole = true;
			checkOnlyDep = false;
		}

	}

	public void onFunctionChange() {
		if (!selectedDept.isEmpty() && selectedRole.isEmpty()) {
			setErrorMsg("Role should be selected.");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
		if (selectedFunCode != null) {

			accessPermissionService = (AccessPermissionService) SpringApplicationContex
					.getBean("accessPermissionService");

			this.selectedFunDesc = accessPermissionService.getFunDescToText(selectedFunCode);
			actList = accessPermissionService.getActivityToDropdown(selectedFunCode);
		}

	}

	public void searchDeptRole() {
		if (selectedDept != null && !selectedDept.isEmpty() && !selectedDept.equalsIgnoreCase("")) {
			if (selectedDept != null && !selectedDept.isEmpty() && !selectedDept.equalsIgnoreCase("")
					&& checkOnlyDep == true) {
				funList = accessPermissionService.getFuncToDropdown();
				funActList = accessPermissionService.getFunctionActivityForOnlyDep(selectedDept);
				// checkOnlyDep = false;
			} else if (selectedDept != null && !selectedDept.isEmpty() && !selectedDept.equalsIgnoreCase("")
					&& selectedRole != null && !selectedRole.isEmpty() && !selectedRole.equalsIgnoreCase("")
					&& checkDepAndRole == true) {
				funList = accessPermissionService.getFuncToDropdown();
				funActList = accessPermissionService.getFunctionActivity(selectedDept, selectedRole);
				checkDepAndRole = false;
				setDisableFunIdTxtField(false);
				setDisableFunNameTxtField(false);
				setDisableActivityTxtField(false);
				setDisableClearBtn(false);
				setDisableSaveBtn(false);

			}
			if (afterSavingMethod == true && selectedDept != null && !selectedDept.isEmpty()
					&& !selectedDept.equalsIgnoreCase("") && selectedRole != null && !selectedRole.isEmpty()
					&& !selectedRole.equalsIgnoreCase("")) {
				funList = accessPermissionService.getFuncToDropdown();
				funActList = accessPermissionService.getFunctionActivity(selectedDept, selectedRole);
			}
		} else {
			setErrorMsg("Department should be selected.");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void saveFunctionActivity() {

		if (selectedFunCode != null && !selectedFunCode.isEmpty() && !selectedFunCode.equalsIgnoreCase("")) {
			if (selectedActCode != null && !selectedActCode.isEmpty() && !selectedActCode.equalsIgnoreCase("")) {

				accessPermissionService = (AccessPermissionService) SpringApplicationContex
						.getBean("accessPermissionService");

				String strResult = accessPermissionService.chkDuplicates(selectedDept, selectedRole, selectedFunCode,
						selectedActCode);
				if (strResult != null) {
					// Duplicate Record.
					errorMsg = "Duplicate data.";
					RequestContext.getCurrentInstance().update("frmerrorMsge");
					RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
					afterSavingMethod = false;
				} else {
					accessPermissionService.saveFunctionActivity(selectedDept, selectedRole, selectedFunCode,
							selectedActCode, "Admin");
					afterSavingMethod = true;
					RequestContext.getCurrentInstance().update("frmEmpDetails");
					sucessMsg = "Successfully Saved.";
					RequestContext.getCurrentInstance().execute("PF('successSve').show()");
					clearAddForm();

				}

			} else {
				setErrorMsg("Activity should be selected.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		} else {
			setErrorMsg("Function ID should be selected.");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void clearAddForm() {
		selectedFunCode = null;
		selectedFunDesc = null;
		selectedActCode = null;
		selectedFRA = 0;
		actList = null;
		searchDeptRole();
	}

	public void clearPage() {
		deptList = null;
		roleList = null;
		funList = null;
		actList = null;
		funActList = null;
		selectedDept = null;
		selectedRole = null;
		selectedFunCode = null;
		selectedFunDesc = null;
		selectedActCode = null;
		selectedFRA = 0;
		init();
		setDisableFunIdTxtField(true);
		setDisableFunNameTxtField(true);
		setDisableActivityTxtField(true);
		setDisableSaveBtn(true);
		setDisableClearBtn(true);
	}

	public void deleteActButtonAction() {
		RequestContext.getCurrentInstance().execute("PF('deleteconfirmationAct').show()");
	}

	public void removeFunctionActivity() {
		accessPermissionService = (AccessPermissionService) SpringApplicationContex.getBean("accessPermissionService");
		if (selectedFRA != 0) {
			accessPermissionService.removeFunctionActivity(selectedFRA);
			RequestContext.getCurrentInstance().update("frmFunActTable");
			RequestContext.getCurrentInstance().update("frmsuccessSve");
			funActList = new ArrayList<AccessPermissionDTO>();
			funActList = accessPermissionService.getFunctionActivity(selectedDept, selectedRole);

			setSucessMsg("Successfully Deleted.");
			RequestContext.getCurrentInstance().execute("PF('successSve').show()");

		} else {
			errorMsg = "You can not delete this record.";
			RequestContext.getCurrentInstance().execute("PF('errorMsgeForDel').show()");
		}
		RequestContext.getCurrentInstance().update("frmEmpDetails");

	}

	
	public boolean isDisableFunIdTxtField() {
		return disableFunIdTxtField;
	}

	public void setDisableFunIdTxtField(boolean disableFunIdTxtField) {
		this.disableFunIdTxtField = disableFunIdTxtField;
	}

	public boolean isDisableFunNameTxtField() {
		return disableFunNameTxtField;
	}

	public void setDisableFunNameTxtField(boolean disableFunNameTxtField) {
		this.disableFunNameTxtField = disableFunNameTxtField;
	}

	public boolean isDisableActivityTxtField() {
		return disableActivityTxtField;
	}

	public void setDisableActivityTxtField(boolean disableActivityTxtField) {
		this.disableActivityTxtField = disableActivityTxtField;
	}

	public boolean isDisableSaveBtn() {
		return disableSaveBtn;
	}

	public void setDisableSaveBtn(boolean disableSaveBtn) {
		this.disableSaveBtn = disableSaveBtn;
	}

	public boolean isDisableClearBtn() {
		return disableClearBtn;
	}

	public void setDisableClearBtn(boolean disableClearBtn) {
		this.disableClearBtn = disableClearBtn;
	}

	public List<AccessPermissionDTO> getDeptList() {
		return deptList;
	}

	public List<AccessPermissionDTO> getRoleList() {
		return roleList;
	}

	public List<AccessPermissionDTO> getFunList() {
		return funList;
	}

	public void setFunList(List<AccessPermissionDTO> funList) {
		this.funList = funList;
	}

	public List<AccessPermissionDTO> getFunActList() {
		return funActList;
	}

	public void setFunActList(List<AccessPermissionDTO> funActList) {
		this.funActList = funActList;
	}

	public List<AccessPermissionDTO> getActList() {
		return actList;
	}

	public void setActList(List<AccessPermissionDTO> actList) {
		this.actList = actList;
	}

	public String getSelectedDept() {
		return selectedDept;
	}

	public void setSelectedDept(String selectedDept) {
		this.selectedDept = selectedDept;
	}

	public String getSelectedFunCode() {
		return selectedFunCode;
	}

	public void setSelectedFunCode(String selectedFunCode) {
		this.selectedFunCode = selectedFunCode;
	}

	public String getSelectedFunDesc() {
		return selectedFunDesc;
	}

	public String getSelectedActCode() {
		return selectedActCode;
	}

	public void setSelectedActCode(String selectedActCode) {
		this.selectedActCode = selectedActCode;
	}

	public void setSelectedFunDesc(String selectedFunDesc) {
		this.selectedFunDesc = selectedFunDesc;
	}

	public int getSelectedFRA() {
		return selectedFRA;
	}

	public void setSelectedFRA(int selectedFRA) {
		this.selectedFRA = selectedFRA;
	}

	public void setDeptList(List<AccessPermissionDTO> deptList) {
		this.deptList = deptList;
	}

	public void setRoleList(List<AccessPermissionDTO> roleList) {
		this.roleList = roleList;
	}

	public String getSelectedRole() {
		return selectedRole;
	}

	public void setSelectedRole(String selectedRole) {
		this.selectedRole = selectedRole;
	}

	public String getSucessMsg() {
		return sucessMsg;
	}

	public void setSucessMsg(String sucessMsg) {
		this.sucessMsg = sucessMsg;
	}
	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public AccessPermissionDTO getAccessPermissionDTO() {
		return accessPermissionDTO;
	}

	public void setAccessPermissionDTO(AccessPermissionDTO accessPermissionDTO) {
		this.accessPermissionDTO = accessPermissionDTO;
	}
}
