package lk.informatics.ntc.view.beans;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.ManageUserDTO;
import lk.informatics.ntc.model.service.ManageUserService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "assignUserRolesBackingBean")
@ViewScoped
public class AssignUserRolesBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	// Services
	private ManageUserService manageUserService;

	// DTOs

	private List<ManageUserDTO> statList;
	private List<ManageUserDTO> roleList;
	private List<ManageUserDTO> displyUserName;
	private List<ManageUserDTO> empolyeeDetails;
	private List<ManageUserDTO> functiondetails;
	private List<ManageUserDTO> editUserDetails;
	private ManageUserDTO manageUserDTO;
	private ManageUserDTO selectedRoleName;
	private String selectRole;

	private String selectedUserName;
	private String selectedUserId;
	private String selectedNIC;
	private String selectedDept;
	private String selectedUser;
	private String currentRoleName;
	private String strSelectedRoleCode;
	private String strSelectedStatus;
	private String startDate;
	private String endDate;
	private String manageUserURL;
	private String errorMsg;
	private Boolean boolEdit;
	private boolean showbtnAddAfterEdit = false;
	private boolean showbtnAddBeforeEdit = true;
	private String userId;
	private boolean disableRoleCodeField = false;
	private boolean disableStartDateField = false;
	private boolean disableEndDateField = false;
	private ManageUserDTO strSelectedRoleSeq;
	private String sucessMsg;
	private String endDateBeforeEditing;
	private String userStatusBeforeEditing;

	@PostConstruct
	public void init() {
		manageUserDTO = new ManageUserDTO();

		userId = sessionBackingBean.getSelectedUser();

		loadValuesforDropdown();
		viewDetails(userId);

	}

	public void loadValuesforDropdown() {
		manageUserService = (ManageUserService) SpringApplicationContex.getBean("manageUserService");
		roleList = manageUserService.GetRolesToDropdown();
	}

	public void onroleChange() {

	}

	public void saveAsssignUserRoles() {

		manageUserDTO.setRoleCode(strSelectedRoleCode);
		manageUserDTO.setStartDateVal(manageUserDTO.getStartDateVal());
		manageUserDTO.setEndDateVal(manageUserDTO.getEndDateVal());
		manageUserDTO.setEmpStatus(strSelectedStatus);
		manageUserDTO.setUserId(userId);
		manageUserDTO.setUserName(userId);
		manageUserDTO.setCreatedBy(sessionBackingBean.loginUser);

		if (!(strSelectedRoleCode.isEmpty() || manageUserDTO.getStartDateVal() == null
				|| manageUserDTO.getEndDateVal() == null || strSelectedStatus.isEmpty())) {

			empolyeeDetails = manageUserService.viewDetails(userId);

			int size = empolyeeDetails.size();
			boolean isDuplicated = true;
			manageUserService = (ManageUserService) SpringApplicationContex.getBean("manageUserService");
			selectedRoleName = manageUserService.getCurrentRoleName(strSelectedRoleCode);
			String currentRoleNameDes = selectedRoleName.getRoleName();
			String loopRoleName;
			for (int i = 0; i < size; i++) {
				selectedRoleName = manageUserService.getCurrentRoleName(empolyeeDetails.get(i).getRoleCode());
				loopRoleName = selectedRoleName.getRoleName();

				if (currentRoleNameDes.equals(loopRoleName)) {

					isDuplicated = true;

					break;
				} else {
					isDuplicated = false;

				}
			}
			if (empolyeeDetails.isEmpty()) {

				isDuplicated = false;
			} else {
			}

			if (isDuplicated == false) {
				Date startDateObj = manageUserDTO.getStartDateVal();
				Date endDateObj = manageUserDTO.getEndDateVal();
				if (startDateObj.compareTo(endDateObj) > 0) {

					setErrorMsg("End Date should be after Start Date.");
					RequestContext.getCurrentInstance().update("frmwarningMsge");
					RequestContext.getCurrentInstance().execute("PF('warningrMsge').show()");
				} else if (startDateObj.compareTo(endDateObj) < 0 || startDateObj.compareTo(endDateObj) == 0) {

					manageUserService = (ManageUserService) SpringApplicationContex.getBean("manageUserService");
					int result = manageUserService.insertUserRoleAct(manageUserDTO);
					if (result == 0) {
						RequestContext.getCurrentInstance().update("frmsuccessSve");

						setSucessMsg("Successfully Saved.");
						RequestContext.getCurrentInstance().execute("PF('SaveSuccess').show()");
						viewDetails(userId);
						setStrSelectedRoleCode("");
						manageUserDTO.setStartDateVal(null);
						manageUserDTO.setEndDateVal(null);
						setStrSelectedStatus("");
					} else {
						RequestContext.getCurrentInstance().update("frmerrorMsge");
						setErrorMsg("Errors.");
						RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
					}
				}

			} else {
				RequestContext.getCurrentInstance().update("frmerrorMsge");
				errorMsg = "Role Name should be uniqued.";
				RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
			}
		} else if ((strSelectedRoleCode.isEmpty() || manageUserDTO.getStartDateVal() == null
				|| manageUserDTO.getEndDateVal() == null || strSelectedStatus.isEmpty())) {
			if (strSelectedRoleCode.isEmpty()) {

				setErrorMsg("Role Name should be selected.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

			} else if (manageUserDTO.getStartDateVal() == null) {

				setErrorMsg("Start Date should be selected.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			} else if (strSelectedStatus.isEmpty()) {

				setErrorMsg("Current Status should be selected.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			} else if (manageUserDTO.getEndDateVal() == null) {

				setErrorMsg("End Date should be selected.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		}

	}

	public void editDb() {

		manageUserDTO.setRoleCode(strSelectedRoleCode);
		manageUserDTO.setStartDateVal(manageUserDTO.getStartDateVal());
		manageUserDTO.setEndDateVal(manageUserDTO.getEndDateVal());
		manageUserDTO.setEmpStatus(strSelectedStatus);
		manageUserDTO.setUserId(userId);
		manageUserDTO.setUserName(userId);
		manageUserDTO.setSeq(manageUserDTO.getSeq());
		manageUserDTO.setModifyBy(sessionBackingBean.loginUser);

		if (!(strSelectedRoleCode.isEmpty() || manageUserDTO.getStartDateVal() == null
				|| manageUserDTO.getEndDateVal() == null || strSelectedStatus.isEmpty())) {
			Date startDateObj = manageUserDTO.getStartDateVal();
			Date endDateObj = manageUserDTO.getEndDateVal();
			if (startDateObj.compareTo(endDateObj) > 0) {

				setErrorMsg("End Date should be after Start Date.");
				RequestContext.getCurrentInstance().update("frmwarningMsge");
				RequestContext.getCurrentInstance().execute("PF('warningrMsge').show()");
			} else if (startDateObj.compareTo(endDateObj) < 0 || startDateObj.compareTo(endDateObj) == 0) {

				manageUserService = (ManageUserService) SpringApplicationContex.getBean("manageUserService");
				java.util.Date date = new java.util.Date();
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				String checkingEndDateValBeforeEditing = (dateFormat.format(manageUserDTO.getEndDateVal()));

				if (!(strSelectedStatus.equals(userStatusBeforeEditing)
						&& checkingEndDateValBeforeEditing.equals(endDateBeforeEditing))) {
					int result = manageUserService.updateGrantedUserRole(manageUserDTO);
					if (result == 0) {
						RequestContext.getCurrentInstance().update("frmsuccessSve");

						setSucessMsg("Successfully Saved");
						RequestContext.getCurrentInstance().execute("PF('SaveSuccess').show()");

						viewDetails(userId);
						setStrSelectedRoleCode("");
						manageUserDTO.setStartDateVal(null);
						manageUserDTO.setEndDateVal(null);
						setStrSelectedStatus("");
						setShowbtnAddAfterEdit(false);
						setShowbtnAddBeforeEdit(true);
						setDisableRoleCodeField(false);
						setDisableStartDateField(false);
					} else {
						RequestContext.getCurrentInstance().update("frmerrorMsge");
						errorMsg = "Errors.";
						RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
					}
				} else {

					errorMsg = "You should be updated.";
					RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
				}
			}

		} else if ((strSelectedRoleCode.isEmpty() || manageUserDTO.getStartDateVal() == null
				|| manageUserDTO.getEndDateVal() == null || strSelectedStatus.isEmpty())) {
			if (strSelectedRoleCode.isEmpty()) {

				setErrorMsg("Role Name should be selected.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			} else if (manageUserDTO.getStartDateVal() == null) {

				setErrorMsg("Start Date should be selected.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			} else if (strSelectedStatus.isEmpty()) {

				setErrorMsg("Current Status should be selected.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			} else if (manageUserDTO.getEndDateVal() == null) {

				setErrorMsg("End Date should be selected.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		}
	}

	public void viewDetails(String userId) {

		manageUserService = (ManageUserService) SpringApplicationContex.getBean("manageUserService");
		empolyeeDetails = manageUserService.viewDetails(userId);
		for (int i = 0; i < empolyeeDetails.size(); i++) {
			if (empolyeeDetails.get(i).getUserStatus().equals("A")) {
				manageUserDTO.setTableStatus("Active");
			} else if (empolyeeDetails.get(i).getUserStatus().equals("I")) {
				manageUserDTO.setTableStatus("Inactive");
			} else if (empolyeeDetails.get(i).getUserStatus().equals("T")) {
				manageUserDTO.setTableStatus("Temporary");
			}
		}

	}

	public void showFuncDetails() {

		setSelectRole(strSelectedRoleSeq.getRoleCode());
		manageUserService = (ManageUserService) SpringApplicationContex.getBean("manageUserService");
		functiondetails = manageUserService.showFuncDetails(strSelectedRoleSeq.getRoleCode());
		if (functiondetails.isEmpty()) {

			setErrorMsg("No Functions are available for selected role.");
			RequestContext.getCurrentInstance().update("frmwarningMsge");
			RequestContext.getCurrentInstance().execute("PF('warningrMsge').show()");
		}
	}

	public void showEditDetails() {

		manageUserService = (ManageUserService) SpringApplicationContex.getBean("manageUserService");

		manageUserDTO = manageUserService.showEditDetailsWithEditBtn(strSelectedRoleSeq.getRoleCode(), userId);

		strSelectedRoleCode = manageUserDTO.getRoleCode();
		strSelectedStatus = manageUserDTO.getUserStatus();

		userStatusBeforeEditing = strSelectedRoleSeq.getUserStatus();
		endDateBeforeEditing = strSelectedRoleSeq.getEndDate();

		setShowbtnAddAfterEdit(true);
		setShowbtnAddBeforeEdit(false);
		setDisableRoleCodeField(true);
		setDisableStartDateField(true);

	}

	public void clearDetails() {

		setStrSelectedRoleCode("");
		manageUserDTO.setStartDateVal(null);
		manageUserDTO.setEndDateVal(null);
		setStrSelectedStatus("");
		setShowbtnAddAfterEdit(false);
		setShowbtnAddBeforeEdit(true);
		setDisableRoleCodeField(false);
		setDisableStartDateField(false);
	}

	public void clearAllDetails() {

		if (functiondetails == null) {
			setStrSelectedRoleCode("");
			manageUserDTO.setStartDateVal(null);
			manageUserDTO.setEndDateVal(null);
			setStrSelectedStatus("");
			setShowbtnAddAfterEdit(false);
			setShowbtnAddBeforeEdit(true);
			setDisableRoleCodeField(false);
			setDisableStartDateField(false);
		} else {
			setStrSelectedRoleCode("");
			manageUserDTO.setStartDateVal(null);
			manageUserDTO.setEndDateVal(null);
			setStrSelectedStatus("");
			setShowbtnAddAfterEdit(false);
			setShowbtnAddBeforeEdit(true);
			setDisableRoleCodeField(false);
			setDisableStartDateField(false);
			functiondetails.clear();

		}

	}

	public void backMethod() {

		sessionBackingBean.setSelectedUser(userId);

		String editRolesURL = sessionBackingBean.getEditRolesURL();

		if (editRolesURL != null) {

			try {
				sessionBackingBean.setEditRolesURLStatus(false);
				FacesContext.getCurrentInstance().getExternalContext().redirect(editRolesURL);
			} catch (IOException e) {

				e.printStackTrace();
			}

		}

	}

	public List<ManageUserDTO> getEmpolyeeDetails() {
		return empolyeeDetails;
	}

	public void setEmpolyeeDetails(List<ManageUserDTO> empolyeeDetails) {
		this.empolyeeDetails = empolyeeDetails;
	}

	public List<ManageUserDTO> getFunctiondetails() {
		return functiondetails;
	}

	public void setFunctiondetails(List<ManageUserDTO> functiondetails) {
		this.functiondetails = functiondetails;
	}

	public List<ManageUserDTO> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<ManageUserDTO> roleList) {
		this.roleList = roleList;

	}

	public ManageUserService getManageUserService() {
		return manageUserService;
	}

	public void setManageUserService(ManageUserService manageUserService) {
		this.manageUserService = manageUserService;
	}

	public ManageUserDTO getManageUserDTO() {
		return manageUserDTO;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public void setManageUserDTO(ManageUserDTO manageUserDTO) {
		this.manageUserDTO = manageUserDTO;
	}

	public String getSelectedUserId() {
		return selectedUserId;
	}

	public void setSelectedUserId(String selectedUserId) {
		this.selectedUserId = selectedUserId;
	}

	public String getSelectedNIC() {
		return selectedNIC;
	}

	public void setSelectedNIC(String selectedNIC) {
		this.selectedNIC = selectedNIC;
	}

	public String getSelectedDept() {
		return selectedDept;
	}

	public void setSelectedDept(String selectedDept) {
		this.selectedDept = selectedDept;
	}

	public String getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(String selectedUser) {
		this.selectedUser = selectedUser;
	}

	public String getStrSelectedStatus() {
		return strSelectedStatus;
	}

	public void setStrSelectedStatus(String strSelectedStatus) {
		this.strSelectedStatus = strSelectedStatus;
	}

	public List<ManageUserDTO> getStatList() {
		return statList;
	}

	public void setStatList(List<ManageUserDTO> statList) {
		this.statList = statList;
	}

	public String getSelectedUserName() {
		return selectedUserName;
	}

	public void setSelectedUserName(String selectedUserName) {
		this.selectedUserName = selectedUserName;
	}

	public String getCurrentRoleName() {
		return currentRoleName;
	}

	public void setCurrentRoleName(String currentRoleName) {
		this.currentRoleName = currentRoleName;
	}

	public String getStrSelectedRoleCode() {
		return strSelectedRoleCode;
	}

	public void setStrSelectedRoleCode(String strSelectedRoleCode) {
		this.strSelectedRoleCode = strSelectedRoleCode;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public ManageUserDTO getStrSelectedRoleSeq() {
		return strSelectedRoleSeq;
	}

	public void setStrSelectedRoleSeq(ManageUserDTO strSelectedRoleSeq) {
		this.strSelectedRoleSeq = strSelectedRoleSeq;
	}

	public List<ManageUserDTO> getEditUserDetails() {
		return editUserDetails;
	}

	public void setEditUserDetails(List<ManageUserDTO> editUserDetails) {
		this.editUserDetails = editUserDetails;
	}

	public List<ManageUserDTO> getDisplyUserName() {
		return displyUserName;
	}

	public void setDisplyUserName(List<ManageUserDTO> displyUserName) {
		this.displyUserName = displyUserName;
	}

	public String getSucessMsg() {
		return sucessMsg;
	}

	public void setSucessMsg(String sucessMsg) {
		this.sucessMsg = sucessMsg;
	}

	public boolean isShowbtnAddAfterEdit() {
		return showbtnAddAfterEdit;
	}

	public void setShowbtnAddAfterEdit(boolean showbtnAddAfterEdit) {
		this.showbtnAddAfterEdit = showbtnAddAfterEdit;
	}

	public boolean isShowbtnAddBeforeEdit() {
		return showbtnAddBeforeEdit;
	}

	public void setShowbtnAddBeforeEdit(boolean showbtnAddBeforeEdit) {
		this.showbtnAddBeforeEdit = showbtnAddBeforeEdit;
	}

	public boolean isDisableRoleCodeField() {
		return disableRoleCodeField;
	}

	public void setDisableRoleCodeField(boolean disableRoleCodeField) {
		this.disableRoleCodeField = disableRoleCodeField;
	}

	public boolean isDisableStartDateField() {
		return disableStartDateField;
	}

	public void setDisableStartDateField(boolean disableStartDateField) {
		this.disableStartDateField = disableStartDateField;
	}

	public boolean isDisableEndDateField() {
		return disableEndDateField;
	}

	public void setDisableEndDateField(boolean disableEndDateField) {
		this.disableEndDateField = disableEndDateField;
	}

	public ManageUserDTO getSelectedRoleName() {
		return selectedRoleName;
	}

	public void setSelectedRoleName(ManageUserDTO selectedRoleName) {
		this.selectedRoleName = selectedRoleName;
	}

	public String getManageUserURL() {
		return manageUserURL;
	}

	public void setManageUserURL(String manageUserURL) {
		this.manageUserURL = manageUserURL;
	}

	public String getSelectRole() {
		return selectRole;
	}

	public void setSelectRole(String selectRole) {
		this.selectRole = selectRole;
	}

}
