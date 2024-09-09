package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.ManageUserDTO;
import lk.informatics.ntc.model.service.ManageUserService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "manageUserBackingBean")
@ViewScoped
public class ManageUserBackingBean {
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	// Services
	private ManageUserService manageUserService;

	// DTOs
	private List<ManageUserDTO> userIdList = new ArrayList<ManageUserDTO>();
	private List<ManageUserDTO> deptList = new ArrayList<ManageUserDTO>();
	private List<ManageUserDTO> epfList = new ArrayList<ManageUserDTO>();
	private List<ManageUserDTO> userList = new ArrayList<ManageUserDTO>();
	private List<ManageUserDTO> userRoles = new ArrayList<ManageUserDTO>();
	private ManageUserDTO selectedUserRadio;

	private ManageUserDTO manageUserDTO;

	private ManageUserDTO selectedUserEdit;

	private ManageUserDTO selectedViewRole;

	private String user;
	private String selectedEpfNo;
	private String selectedUserId;
	private String selectedNIC;
	private String selectedDept;
	private String selectedUser;
	private String rejectReason;
	private String manageUserURL;
	private String selectedStatus;
	private String inactiveReason;

	private String errorMsg;
	private String successMsg;

	@PostConstruct
	public void init() {

		if (sessionBackingBean.editRolesURLStatus) {
			manageUserDTO = new ManageUserDTO();

			selectedUserRadio = null;
			LoadValuesforDropdown();
		}
		if (!sessionBackingBean.editRolesURLStatus) {
			sessionBackingBean.setEditRolesURL(null);
			manageUserDTO = new ManageUserDTO();
			LoadValuesforDropdown();
			userList = sessionBackingBean.getTempUserDataList();
			sessionBackingBean.setEditRolesURLStatus(true);
			RequestContext.getCurrentInstance().update("userTable");
		}

	}

	public ManageUserBackingBean() {
		manageUserService = (ManageUserService) SpringApplicationContex.getBean("manageUserService");
	}

	private void LoadValuesforDropdown() {
		manageUserService = (ManageUserService) SpringApplicationContex.getBean("manageUserService");
		userIdList = manageUserService.getUserToDropdown();
		deptList = manageUserService.getDeptToDropdown();
		epfList = manageUserService.getEpfNoToDropdown();
		userList = manageUserService.getUsers();
		rejectReason = null;

	}

	public void searchUser() {
		selectedUserRadio = null;
		userList = manageUserService.searchUser(manageUserDTO);
	}

	public void clearSearch() {
		rejectReason = null;
		init();
	}

	public void clearPage() {
		manageUserDTO = new ManageUserDTO();

		selectedUserRadio = null;

		rejectReason = null;

		LoadValuesforDropdown();
	}

	public void generateUser() {

		if (selectedUserRadio != null) {
			manageUserService = (ManageUserService) SpringApplicationContex.getBean("manageUserService");
			if (selectedUserRadio.getUserStatus().equals("PENDING")) {
				String usernamePassword = manageUserService.generateUser(selectedUserRadio.getEmpNo());
				user = usernamePassword;
				rejectReason = null;
				userIdList = manageUserService.getUserToDropdown();
				successMsg = "User ID generated successfully.";
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
			} else if (selectedUserRadio.getUserStatus().equals("REJECT")) {
				errorMsg = "User account is Rejected";
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			} else {
				errorMsg = "User account is Active";
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

			}

		} else {
			errorMsg = "Please select a user";
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		}
		selectedUserRadio = null;
		userList = manageUserService.getUsers();
	}

	public void rejectUser() {

		if (selectedUserRadio != null) {
			manageUserService = (ManageUserService) SpringApplicationContex.getBean("manageUserService");
			if (selectedUserRadio.getUserStatus().equals("PENDING")) {

				if (rejectReason != null && !rejectReason.isEmpty()) {
					manageUserService.rejectUser(selectedUserRadio.getEmpNo(), rejectReason);
					successMsg = "User Rejected successfully";
					RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
				} else {
					errorMsg = "Please provide reject reason";
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

				}

			} else if (selectedUserRadio.getUserStatus().equals("ACTIVE")) {

				if (rejectReason != null && !rejectReason.isEmpty()) {
					manageUserService.rejectUser(selectedUserRadio.getEmpNo(), rejectReason);
					successMsg = "User Rejected successfully";
					RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
				} else {
					errorMsg = "Please provide reject reason";
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

				}

			} else {
				errorMsg = "User account is Rejected";
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

			}

		} else {
			errorMsg = "Please select a user";
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		}
		rejectReason = null;
		selectedUserRadio = null;
		userList = manageUserService.getUsers();

	}

	public void viewUserRole() {
		if (selectedViewRole.getUserId() == null) {
			userRoles = manageUserService.viewUserRoleFromTemp(selectedViewRole.getEmpNo());
		} else {
			userRoles = manageUserService.viewUserRole(selectedViewRole.getEmpNo());
		}

		selectedViewRole = null;
	}

	public String editUserRole() {

		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		manageUserURL = request.getRequestURL().toString();

		sessionBackingBean.setEditRolesURL(manageUserURL);

		sessionBackingBean.setEditRolesURLStatus(true);

		sessionBackingBean.tempUserDataList = userList;

		sessionBackingBean.setSelectedUser(selectedUser);

		return "/pages/manageUser/assignUserRoles.xhtml";

	}

	public void editUserStatus() {

		if (selectedUserEdit.getUserStatus().equals("ACTIVE")) {
			this.selectedStatus = "INACTIVE";

		} else if (selectedUserEdit.getUserStatus().equals("INACTIVE")) {
			this.selectedStatus = "ACTIVE";

		}
		RequestContext.getCurrentInstance().execute("PF('dlg2').show()");
	}

	public void updateUserStatus() {

		String userStatus;
		String userOldStatus;

		if (this.selectedStatus.equals("ACTIVE")) {
			userStatus = "A";
			userOldStatus = "I";
			manageUserService.updateUserStatus(selectedUserEdit.getUserId(), userStatus, userOldStatus, inactiveReason);
			userList = null;
			inactiveReason = null;
			userList = manageUserService.getUsers();
			successMsg = "User activated successfully";
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
		} else if (this.selectedStatus.equals("INACTIVE")) {
			userStatus = "I";
			userOldStatus = "A";
			if (inactiveReason != null && !inactiveReason.isEmpty()) {
				manageUserService.updateUserStatus(selectedUserEdit.getUserId(), userStatus, userOldStatus,
						inactiveReason);
				userList = null;
				inactiveReason = null;
				userList = manageUserService.getUsers();
				successMsg = "User inactivated successfully";
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
			} else {
				inactiveReason = null;
				errorMsg = "Please provide inactive reason";
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

			}

		}

		if (sessionBackingBean.editRolesURLStatus) {
			manageUserDTO = new ManageUserDTO();

			selectedUserRadio = null;
			LoadValuesforDropdown();
		}
		if (!sessionBackingBean.editRolesURLStatus) {
			sessionBackingBean.setEditRolesURL(null);
			manageUserDTO = new ManageUserDTO();
			LoadValuesforDropdown();
			userList = sessionBackingBean.getTempUserDataList();
			sessionBackingBean.setEditRolesURLStatus(true);
			RequestContext.getCurrentInstance().update("userTable");
		}

	}

	public void cancelUserStatus() {
		inactiveReason = null;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public List<ManageUserDTO> getUserIdList() {
		return userIdList;
	}

	public void setUserIdList(List<ManageUserDTO> userIdList) {
		this.userIdList = userIdList;
	}

	public List<ManageUserDTO> getDeptList() {
		return deptList;
	}

	public void setDeptList(List<ManageUserDTO> deptList) {
		this.deptList = deptList;
	}

	public List<ManageUserDTO> getEpfList() {
		return epfList;
	}

	public void setEpfList(List<ManageUserDTO> epfList) {
		this.epfList = epfList;
	}

	public List<ManageUserDTO> getUserList() {
		return userList;
	}

	public void setUserList(List<ManageUserDTO> userList) {
		this.userList = userList;
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

	public void setManageUserDTO(ManageUserDTO manageUserDTO) {
		this.manageUserDTO = manageUserDTO;
	}

	public String getSelectedEpfNo() {
		return selectedEpfNo;
	}

	public void setSelectedEpfNo(String selectedEpfNo) {
		this.selectedEpfNo = selectedEpfNo;
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

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public String getInactiveReason() {
		return inactiveReason;
	}

	public void setInactiveReason(String inactiveReason) {
		this.inactiveReason = inactiveReason;
	}

	public String getManageUserURL() {
		return manageUserURL;
	}

	public void setManageUserURL(String manageUserURL) {
		this.manageUserURL = manageUserURL;
	}

	public List<ManageUserDTO> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(List<ManageUserDTO> userRoles) {
		this.userRoles = userRoles;
	}

	public ManageUserDTO getSelectedUserRadio() {
		return selectedUserRadio;
	}

	public void setSelectedUserRadio(ManageUserDTO selectedUserRadio) {
		this.selectedUserRadio = selectedUserRadio;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getSuccessMsg() {
		return successMsg;
	}

	public void setSuccessMsg(String successMsg) {
		this.successMsg = successMsg;
	}

	public String getSelectedStatus() {
		return selectedStatus;
	}

	public void setSelectedStatus(String selectedStatus) {
		this.selectedStatus = selectedStatus;
	}

	public ManageUserDTO getSelectedUserEdit() {
		return selectedUserEdit;
	}

	public void setSelectedUserEdit(ManageUserDTO selectedUserEdit) {
		this.selectedUserEdit = selectedUserEdit;
	}

	public ManageUserDTO getSelectedViewRole() {
		return selectedViewRole;
	}

	public void setSelectedViewRole(ManageUserDTO selectedViewRole) {
		this.selectedViewRole = selectedViewRole;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

}