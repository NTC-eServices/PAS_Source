package lk.informatics.ntc.model.service;

import java.io.Serializable;
import java.util.List;

import lk.informatics.ntc.model.dto.ManageUserDTO;

public interface ManageUserService extends Serializable {

	public List<ManageUserDTO> getUserToDropdown();

	public List<ManageUserDTO> getDeptToDropdown();

	public List<ManageUserDTO> getEpfNoToDropdown();

	public List<ManageUserDTO> getUsers();

	public List<ManageUserDTO> GetRolesToDropdown();

	public void rejectUser(String string, String rejectReason);

	// added for save Edit user status
	public List<ManageUserDTO> saveEditStatus(String selectedUserStatus, String selectedInactRe);

	// added for assign user role save data

	public List<ManageUserDTO> saveAsssignUserRoles(String strSelectedRoleCode, String startDate, String endDate,
			String strSelectedStatus, String userId);

	public List<ManageUserDTO> searchUser(ManageUserDTO manageUserDTO);

	public List<ManageUserDTO> getSelectUser(String strSelectedRoleCode, String startDate, String endDate,
			String strSelectedStatus);

	public String generateUser(String empNo);

	public List<ManageUserDTO> viewDetails(String userId);

	// new added for view function id and name
	public List<ManageUserDTO> showFuncDetails(String roleCode);

	public List<ManageUserDTO> viewUserRole(String selectedUserId);

	public List<ManageUserDTO> viewUserRoleFromTemp(String selectedUserId);

	public ManageUserDTO showEditDetailsWithEditBtn(String roleCode, String userId);

	public int insertUserRoleAct(ManageUserDTO manageUserDTO);

	public ManageUserDTO getCurrentRoleName(String strSelectedRoleCode);

	public int updateGrantedUserRole(ManageUserDTO manageUserDTO);

	public void updateUserStatus(String userId, String userStatus, String userOldStatus, String inactiveReason);

	/*
	 * added for approve suspend permit/driver/conductor function
	 */
	List<ManageUserDTO> getFuncRoleActivity(String roleCode, String functionCode);
	
	// for block / unblock user

	public List<ManageUserDTO> getUserToDropdownUnblockUser();

	public List<ManageUserDTO> getBlockedUsers();

	public List<ManageUserDTO> searchBlockedUsers(ManageUserDTO dto);

	public boolean unblockUser(ManageUserDTO blockedUser, String loginUser);


}
