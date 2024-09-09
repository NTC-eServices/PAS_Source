package lk.informatics.ntc.view.beans;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.FlyingSquadGroupsDTO;
import lk.informatics.ntc.model.dto.FlyingSquadSteupDTO;
import lk.informatics.ntc.model.dto.UserDTO;
import lk.informatics.ntc.model.service.FlySetupSquadService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "flyingSquadSetupBackingBean")
@ViewScoped
public class FlyingSquadSetupBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	private String user;
	private FlySetupSquadService flySetupSquadService;
	private ArrayList<UserDTO> userList;
	private ArrayList<UserDTO> empNoList;
	private ArrayList<FlyingSquadGroupsDTO> groupList;
	private UserDTO userDTO;
	private FlyingSquadSteupDTO flyingSquadSteupDTO;
	private String savemsg;
	private ArrayList<FlyingSquadSteupDTO> detailList;
	private FlyingSquadSteupDTO selectedflyingSquadSteupDTO;
	private boolean edit;
	private String errormsg;

	@PostConstruct
	public void init() {

		setFlySetupSquadService((FlySetupSquadService) SpringApplicationContex.getBean("flySetupSquadService"));

		flyingSquadSteupDTO = new FlyingSquadSteupDTO();
		selectedflyingSquadSteupDTO = new FlyingSquadSteupDTO();
		userList = new ArrayList<UserDTO>();
		userList = flySetupSquadService.getUserList();

		empNoList = new ArrayList<UserDTO>();
		empNoList = flySetupSquadService.getEmpNoList();

		groupList = new ArrayList<FlyingSquadGroupsDTO>();
		groupList = flySetupSquadService.getGroupList();

		savemsg = null;
		setUser(sessionBackingBean.getLoginUser());

		detailList = new ArrayList<FlyingSquadSteupDTO>();
		detailList = flySetupSquadService.getAllDetails();

		edit = false;

	}

	public void getofferDetailFromEmpID() {
		flyingSquadSteupDTO = flySetupSquadService.getOfficerDetailsFromEmpId(flyingSquadSteupDTO.getEmpId());

	}

	public void getOfficerName() {

		flyingSquadSteupDTO = flySetupSquadService.getOfficerDetails(flyingSquadSteupDTO.getUserId());

	}

	public void addDetails() {
		if (edit) {

			flySetupSquadService.insertFlyingSquadHistory(selectedflyingSquadSteupDTO.getSeqNo(),
					selectedflyingSquadSteupDTO.getGroupcd(), selectedflyingSquadSteupDTO.getUserId(),
					selectedflyingSquadSteupDTO.getEmpId(), selectedflyingSquadSteupDTO.getOfficerName(),
					selectedflyingSquadSteupDTO.getStatus(), selectedflyingSquadSteupDTO.getCreatedBy(),
					selectedflyingSquadSteupDTO.getCreatedDate(), selectedflyingSquadSteupDTO.getModifiedBy(),
					selectedflyingSquadSteupDTO.getModifiedDate());
			flySetupSquadService.updteDetails(flyingSquadSteupDTO.getGroupcd(), flyingSquadSteupDTO.getUserId(),
					flyingSquadSteupDTO.getStatus(), user);
			setSavemsg("Successfully Updated!");
			RequestContext.getCurrentInstance().update("frmsuccessSve");
			RequestContext.getCurrentInstance().execute("PF('SaveSuccess').show()");
			detailList = flySetupSquadService.getAllDetails();
			clear();

		} else {
			boolean valid = checkMandatoryfeilds(flyingSquadSteupDTO);
			boolean duplicate = flySetupSquadService.checkduplicateValues(flyingSquadSteupDTO.getGroupcd(),
					flyingSquadSteupDTO.getUserId());

			if (duplicate) {
				setErrormsg("Duplicate Record Found.");
				RequestContext.getCurrentInstance().update("frmError");
				RequestContext.getCurrentInstance().execute("PF('Error').show()");
			} else {
				if (valid) {
					flySetupSquadService.insertFlyingSquad(flyingSquadSteupDTO.getGroupcd(),
							flyingSquadSteupDTO.getUserId(), flyingSquadSteupDTO.getEmpId(),
							flyingSquadSteupDTO.getOfficerName(), flyingSquadSteupDTO.getStatus(), user);
					setSavemsg("Successfully Saved!");
					RequestContext.getCurrentInstance().update("frmsuccessSve");
					RequestContext.getCurrentInstance().execute("PF('SaveSuccess').show()");
					detailList = flySetupSquadService.getAllDetails();
					clear();

				} else {
					RequestContext.getCurrentInstance().update("frmError");
					RequestContext.getCurrentInstance().execute("PF('Error').show()");
				}
			}

		}

	}

	public void editDetails() {
		edit = true;

		flyingSquadSteupDTO.setGroupcd(selectedflyingSquadSteupDTO.getGroupcd());
		flyingSquadSteupDTO.setUserId(selectedflyingSquadSteupDTO.getUserId());
		flyingSquadSteupDTO.setEmpId(selectedflyingSquadSteupDTO.getEmpId());
		flyingSquadSteupDTO.setOfficerName(selectedflyingSquadSteupDTO.getOfficerName());
		flyingSquadSteupDTO.setStatus(selectedflyingSquadSteupDTO.getStatus());

	}

	public void clear() {
		flyingSquadSteupDTO = new FlyingSquadSteupDTO();
		selectedflyingSquadSteupDTO = new FlyingSquadSteupDTO();
		edit = false;

	}

	public boolean checkMandatoryfeilds(FlyingSquadSteupDTO flyingSquadSteupDTO) {

		if (flyingSquadSteupDTO.getGroupcd().equalsIgnoreCase("") || flyingSquadSteupDTO.getGroupcd() == null) {

			setErrormsg("Please Enter Group Name");
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('Error').show()");
			return false;
		}

		else if (flyingSquadSteupDTO.getUserId().equalsIgnoreCase("") || flyingSquadSteupDTO.getUserId() == null) {

			setErrormsg("Please Enter User Id");
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('Error').show()");
			return false;
		}

		else if (flyingSquadSteupDTO.getEmpId().equalsIgnoreCase("") || flyingSquadSteupDTO.getEmpId() == null) {

			setErrormsg("Please Enter Employee No");
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('Error').show()");
			return false;
		}

		else if (flyingSquadSteupDTO.getStatus().equalsIgnoreCase("") || flyingSquadSteupDTO.getStatus() == null) {
			setErrormsg("Please Enter Status");
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('Error').show()");
			return false;

		}

		return true;

	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public FlySetupSquadService getFlySetupSquadService() {
		return flySetupSquadService;
	}

	public void setFlySetupSquadService(FlySetupSquadService flySetupSquadService) {
		this.flySetupSquadService = flySetupSquadService;
	}

	public ArrayList<UserDTO> getUserList() {
		return userList;
	}

	public void setUserList(ArrayList<UserDTO> userList) {
		this.userList = userList;
	}

	public ArrayList<UserDTO> getEmpNoList() {
		return empNoList;
	}

	public void setEmpNoList(ArrayList<UserDTO> empNoList) {
		this.empNoList = empNoList;
	}

	public UserDTO getUserDTO() {
		return userDTO;
	}

	public void setUserDTO(UserDTO userDTO) {
		this.userDTO = userDTO;
	}

	public ArrayList<FlyingSquadGroupsDTO> getGroupList() {
		return groupList;
	}

	public void setGroupList(ArrayList<FlyingSquadGroupsDTO> groupList) {
		this.groupList = groupList;
	}

	public FlyingSquadSteupDTO getFlyingSquadSteupDTO() {
		return flyingSquadSteupDTO;
	}

	public void setFlyingSquadSteupDTO(FlyingSquadSteupDTO flyingSquadSteupDTO) {
		this.flyingSquadSteupDTO = flyingSquadSteupDTO;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public String getSavemsg() {
		return savemsg;
	}

	public void setSavemsg(String savemsg) {
		this.savemsg = savemsg;
	}

	public ArrayList<FlyingSquadSteupDTO> getDetailList() {
		return detailList;
	}

	public void setDetailList(ArrayList<FlyingSquadSteupDTO> detailList) {
		this.detailList = detailList;
	}

	public FlyingSquadSteupDTO getSelectedflyingSquadSteupDTO() {
		return selectedflyingSquadSteupDTO;
	}

	public void setSelectedflyingSquadSteupDTO(FlyingSquadSteupDTO selectedflyingSquadSteupDTO) {
		this.selectedflyingSquadSteupDTO = selectedflyingSquadSteupDTO;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

}
