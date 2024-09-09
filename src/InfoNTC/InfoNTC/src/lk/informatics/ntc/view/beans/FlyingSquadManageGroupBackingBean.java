package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.FlyingSquadGroupsDTO;
import lk.informatics.ntc.model.service.FlyManageGroupService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "flyingSquadManageGroupBean")
@ViewScoped
public class FlyingSquadManageGroupBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	private String user;
	private FlyManageGroupService flyManageGroupService;
	private FlyingSquadGroupsDTO flyingSquadGroupsDTO;
	private List<FlyingSquadGroupsDTO> groupList = new ArrayList<FlyingSquadGroupsDTO>();
	private boolean allowUpdate;
	private String groupCode;
	private String groupCodeN;
	private FlyingSquadGroupsDTO strSelectedGroupCode;
	private boolean disableUpdateMode, disableSaveMode;

	private String errormsg;

	@PostConstruct
	public void init() {

		flyManageGroupService = (FlyManageGroupService) SpringApplicationContex.getBean("flyManageGroupService");
		setUser(sessionBackingBean.getLoginUser());
		flyingSquadGroupsDTO = new FlyingSquadGroupsDTO();
		allowUpdate = false;
		disableUpdateMode = false;
		disableSaveMode = true;
		loadValues();
	}

	public void loadValues() {
		setGroupList(flyManageGroupService.getGroupDetails());
	}

	public void add() {

		if (flyingSquadGroupsDTO.getGroupName() != null && !flyingSquadGroupsDTO.getGroupName().isEmpty()
				&& !flyingSquadGroupsDTO.getGroupName().equalsIgnoreCase("")) {

			if (flyingSquadGroupsDTO.getStatus() != null && !flyingSquadGroupsDTO.getStatus().isEmpty()
					&& !flyingSquadGroupsDTO.getStatus().equalsIgnoreCase("")) {

				if (allowUpdate) {
					flyManageGroupService.updateGroups(flyingSquadGroupsDTO.getGroupCd(),
							flyingSquadGroupsDTO.getStatus(), user);

					RequestContext.getCurrentInstance().update("frmsuccessSve");
					RequestContext.getCurrentInstance().execute("PF('SaveSuccess').show()");
					clearAction();
					loadValues();
				} else {
					groupCodeN = flyManageGroupService.insertGroupsNew(flyingSquadGroupsDTO.getGroupName(),
							flyingSquadGroupsDTO.getStatus(), user);

					RequestContext.getCurrentInstance().update("frmsuccessSve");
					RequestContext.getCurrentInstance().execute("PF('SaveSuccess').show()");
					clearAction();
					loadValues();
				}

			}

			else {

				setErrormsg("Please Enter Status");
				RequestContext.getCurrentInstance().update("frmError");
				RequestContext.getCurrentInstance().execute("PF('Error').show()");

			}

		}

		else {

			setErrormsg("Please Enter Group Name");
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('Error').show()");
		}
	}

	public void clearAction() {
		flyingSquadGroupsDTO = new FlyingSquadGroupsDTO();
		allowUpdate = false;
		disableUpdateMode = false;
		disableSaveMode = true;
		groupCode = null;
	}

	public void search() {
		if (flyingSquadGroupsDTO.getGroupName() != null && !flyingSquadGroupsDTO.getGroupName().isEmpty()
				&& !flyingSquadGroupsDTO.getGroupName().equalsIgnoreCase("")) {

			allowUpdate = true;
			flyingSquadGroupsDTO = flyManageGroupService.search(flyingSquadGroupsDTO.getGroupName());
			RequestContext.getCurrentInstance().update("frmAddGroup");

		}

		else {

			setErrormsg("Please Enter Group Name");
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('Error').show()");
		}

	}

	public void editAction() {

		groupCode = strSelectedGroupCode.getGroupCd();
		flyingSquadGroupsDTO.setGroupName(strSelectedGroupCode.getGroupName());
		flyingSquadGroupsDTO.setStatus(strSelectedGroupCode.getStatus());
		disableUpdateMode = true;
		disableSaveMode = false;
	}

	public void update() {

		flyManageGroupService.updateGroups(strSelectedGroupCode.getGroupCd(), flyingSquadGroupsDTO.getStatus(), user);
		RequestContext.getCurrentInstance().update("frmsuccessSve");
		RequestContext.getCurrentInstance().execute("PF('SaveSuccess').show()");
		clearAction();
		loadValues();
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public FlyManageGroupService getFlyManageGroupService() {
		return flyManageGroupService;
	}

	public void setFlyManageGroupService(FlyManageGroupService flyManageGroupService) {
		this.flyManageGroupService = flyManageGroupService;
	}

	public FlyingSquadGroupsDTO getFlyingSquadGroupsDTO() {
		return flyingSquadGroupsDTO;
	}

	public void setFlyingSquadGroupsDTO(FlyingSquadGroupsDTO flyingSquadGroupsDTO) {
		this.flyingSquadGroupsDTO = flyingSquadGroupsDTO;
	}

	public boolean isAllowUpdate() {
		return allowUpdate;
	}

	public void setAllowUpdate(boolean allowUpdate) {
		this.allowUpdate = allowUpdate;
	}

	public List<FlyingSquadGroupsDTO> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<FlyingSquadGroupsDTO> groupList) {
		this.groupList = groupList;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getGroupCodeN() {
		return groupCodeN;
	}

	public void setGroupCodeN(String groupCodeN) {
		this.groupCodeN = groupCodeN;
	}

	public FlyingSquadGroupsDTO getStrSelectedGroupCode() {
		return strSelectedGroupCode;
	}

	public void setStrSelectedGroupCode(FlyingSquadGroupsDTO strSelectedGroupCode) {
		this.strSelectedGroupCode = strSelectedGroupCode;
	}

	public boolean isDisableUpdateMode() {
		return disableUpdateMode;
	}

	public void setDisableUpdateMode(boolean disableUpdateMode) {
		this.disableUpdateMode = disableUpdateMode;
	}

	public boolean isDisableSaveMode() {
		return disableSaveMode;
	}

	public void setDisableSaveMode(boolean disableSaveMode) {
		this.disableSaveMode = disableSaveMode;
	}

}
