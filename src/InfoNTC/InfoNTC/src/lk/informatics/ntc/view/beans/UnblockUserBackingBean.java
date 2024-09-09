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
import lk.informatics.ntc.model.service.UserService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "unblockUserBackingBean")
@ViewScoped
public class UnblockUserBackingBean {
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	// Services
	private ManageUserService manageUserService;

	// DTOs
	private List<ManageUserDTO> userIdList = new ArrayList<ManageUserDTO>();
	private List<ManageUserDTO> userList = new ArrayList<ManageUserDTO>();

	private ManageUserDTO manageUserDTO;

	private String errorMsg;
	private String successMsg;

	@PostConstruct
	public void init() {
		
		manageUserDTO = new ManageUserDTO();
		userIdList = new ArrayList<ManageUserDTO>();
		userList = new ArrayList<ManageUserDTO>();
		
		manageUserService = (ManageUserService) SpringApplicationContex.getBean("manageUserService");
		
		LoadValues();

	}

	private void LoadValues() {
		userIdList = manageUserService.getUserToDropdownUnblockUser();
		userList = manageUserService.getBlockedUsers();
	}

	public void searchUser() {
		userList = manageUserService.searchBlockedUsers(manageUserDTO);
	}

	public void clearSearch() {
		init();
	}

	public void unblockUser(ManageUserDTO dto) {
		
		boolean success= manageUserService.unblockUser(dto, sessionBackingBean.getLoginUser());
		if(success) {
			successMsg = "User unblocked successfully.";
			RequestContext.getCurrentInstance().update("successMessage");
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
			clearSearch();
		} else {
			errorMsg = "Error occurred. Could not unblock the user.";
			RequestContext.getCurrentInstance().update("errorMessage");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
		

	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public ManageUserService getManageUserService() {
		return manageUserService;
	}

	public void setManageUserService(ManageUserService manageUserService) {
		this.manageUserService = manageUserService;
	}

	public List<ManageUserDTO> getUserIdList() {
		return userIdList;
	}

	public void setUserIdList(List<ManageUserDTO> userIdList) {
		this.userIdList = userIdList;
	}

	public List<ManageUserDTO> getUserList() {
		return userList;
	}

	public void setUserList(List<ManageUserDTO> userList) {
		this.userList = userList;
	}

	public ManageUserDTO getManageUserDTO() {
		return manageUserDTO;
	}

	public void setManageUserDTO(ManageUserDTO manageUserDTO) {
		this.manageUserDTO = manageUserDTO;
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

}