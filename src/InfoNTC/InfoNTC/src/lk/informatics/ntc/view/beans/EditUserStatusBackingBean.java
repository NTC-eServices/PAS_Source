package lk.informatics.ntc.view.beans;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.ManageUserDTO;
import lk.informatics.ntc.model.service.ManageUserService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "editUserStatusBackingBean")
@ViewScoped
public class EditUserStatusBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	// Services
	private ManageUserService manageUserService;
	// selected items

	private ManageUserDTO manageUserDTO;

	private String selectedUserStatus;
	private String selectedInactRe;
	private String userId;
	private String sucessMsg;
	private String manageUseRoleURL;

	@PostConstruct
	public void init() {
		manageUserDTO = new ManageUserDTO();

		userId = sessionBackingBean.getSelectedUser();

	}

	public String getSelectedUserStatus() {
		return selectedUserStatus;
	}

	public void setSelectedUserStatus(String selectedUserStatus) {
		this.selectedUserStatus = selectedUserStatus;
	}

	public String getSelectedInactRe() {
		return selectedInactRe;
	}

	public void setSelectedInactRe(String selectedInactRe) {
		this.selectedInactRe = selectedInactRe;
	}

	public void saveEditStatus() {

		manageUserService = (ManageUserService) SpringApplicationContex
				.getBean("manageUserService");
		
		manageUserService.saveEditStatus(selectedUserStatus, selectedInactRe);

		RequestContext.getCurrentInstance().update("frmEmpDetails");
		
		sucessMsg = "Successfully Created.";
		RequestContext.getCurrentInstance().execute("PF('SaveSuccess').show()");

		clearEditStaus();
	}

	public void clearEditStaus() {
		
		manageUserDTO = new ManageUserDTO();
		selectedUserStatus = null;
		selectedInactRe = null;

	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public ManageUserDTO getManageUserDTO() {
		return manageUserDTO;
	}

	public void setManageUserDTO(ManageUserDTO manageUserDTO) {
		this.manageUserDTO = manageUserDTO;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public ManageUserService getManageUserService() {
		return manageUserService;
	}

	public void setManageUserService(ManageUserService manageUserService) {
		this.manageUserService = manageUserService;
	}

	public String getSucessMsg() {
		return sucessMsg;
	}

	public void setSucessMsg(String sucessMsg) {
		this.sucessMsg = sucessMsg;
	}

	public String getManageUseRoleURL() {
		return manageUseRoleURL;
	}

	public void setManageUseRoleURL(String manageUseRoleURL) {
		this.manageUseRoleURL = manageUseRoleURL;
	}

}
