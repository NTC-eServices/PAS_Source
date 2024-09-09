package lk.informatics.ntc.view.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.FlyingSquadGroupsDTO;
import lk.informatics.ntc.model.dto.FlyingSquadInvestiDetailDTO;
import lk.informatics.ntc.model.dto.FlyingSquadInvestiMasterDTO;
import lk.informatics.ntc.model.service.FlyingSquadInvestigationService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "flyingSquadInvestigationBackingBeann")
@ViewScoped
public class FlyingSquadInvestigationViewBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private FlyingSquadInvestiDetailDTO flyingSquadInvestiDetailDTO;
	private FlyingSquadInvestiMasterDTO flyingSquadInvestiMasterDTO;
	private String user;
	private FlyingSquadInvestigationService flyingSquadInvestigationService;
	private ArrayList<FlyingSquadGroupsDTO> flyingSquadGroupsList;
	private ArrayList<FlyingSquadInvestiDetailDTO> flyingSquadTeamList;
	private ArrayList<FlyingSquadInvestiMasterDTO> refNoList;
	private String refNo;
	private String savemsg;
	private String errormsg;
	private boolean disableSave;
	private String flyingRefNo;

	@PostConstruct
	public void init() {
		setFlyingSquadInvestigationService(
				(FlyingSquadInvestigationService) SpringApplicationContex.getBean("flyingSquadInvestigationService"));
		setUser(sessionBackingBean.getLoginUser());

		flyingSquadInvestiDetailDTO = new FlyingSquadInvestiDetailDTO();
		flyingSquadInvestiMasterDTO = new FlyingSquadInvestiMasterDTO();
		flyingSquadGroupsList = new ArrayList<FlyingSquadGroupsDTO>();
		flyingSquadTeamList = new ArrayList<FlyingSquadInvestiDetailDTO>();
		refNoList = flyingSquadInvestigationService.getrefNo();
		setDisableSave(true);
		flyingRefNo = null;
		flyingRefNo = sessionBackingBean.getFlyrefNo();

		if (flyingRefNo != null) {
			searchnew();
		}
	}

	public void getgroups() {

		java.util.Date utilDate = new java.util.Date(flyingSquadInvestiMasterDTO.getInvestigationDate().getTime());

		Calendar cal = Calendar.getInstance();
		cal.setTime(utilDate);
		int month = cal.get(Calendar.MONTH); // 0 being January
		int year = cal.get(Calendar.YEAR);
		flyingSquadGroupsList = flyingSquadInvestigationService.getGroupcode(year, month);

	}

	public void getTeam() {

		flyingSquadTeamList = flyingSquadInvestigationService.groupDetail(flyingSquadInvestiMasterDTO.getGroupCd());

	}

	public void cancel() {
		flyingSquadInvestiDetailDTO = new FlyingSquadInvestiDetailDTO();
		flyingSquadInvestiMasterDTO = new FlyingSquadInvestiMasterDTO();
		flyingSquadGroupsList = new ArrayList<FlyingSquadGroupsDTO>();
		flyingSquadTeamList = new ArrayList<FlyingSquadInvestiDetailDTO>();
		setDisableSave(true);

	}

	public void searchnew() {
		flyingSquadInvestiMasterDTO = flyingSquadInvestigationService.getmasterDetails(flyingRefNo);
		flyingSquadTeamList = flyingSquadInvestigationService.getmemberDetails(flyingRefNo);
		flyingSquadInvestiMasterDTO.setRefNo(flyingRefNo);
		RequestContext.getCurrentInstance().update("frmeditinvl");

	}

	public void refNo() {
		refNoList = flyingSquadInvestigationService.getrefNo();
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public FlyingSquadInvestiDetailDTO getFlyingSquadInvestiDetailDTO() {
		return flyingSquadInvestiDetailDTO;
	}

	public void setFlyingSquadInvestiDetailDTO(FlyingSquadInvestiDetailDTO flyingSquadInvestiDetailDTO) {
		this.flyingSquadInvestiDetailDTO = flyingSquadInvestiDetailDTO;
	}

	public FlyingSquadInvestiMasterDTO getFlyingSquadInvestiMasterDTO() {
		return flyingSquadInvestiMasterDTO;
	}

	public void setFlyingSquadInvestiMasterDTO(FlyingSquadInvestiMasterDTO flyingSquadInvestiMasterDTO) {
		this.flyingSquadInvestiMasterDTO = flyingSquadInvestiMasterDTO;
	}

	public FlyingSquadInvestigationService getFlyingSquadInvestigationService() {
		return flyingSquadInvestigationService;
	}

	public void setFlyingSquadInvestigationService(FlyingSquadInvestigationService flyingSquadInvestigationService) {
		this.flyingSquadInvestigationService = flyingSquadInvestigationService;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public ArrayList<FlyingSquadGroupsDTO> getFlyingSquadGroupsList() {
		return flyingSquadGroupsList;
	}

	public void setFlyingSquadGroupsList(ArrayList<FlyingSquadGroupsDTO> flyingSquadGroupsList) {
		flyingSquadGroupsList = flyingSquadGroupsList;
	}

	public ArrayList<FlyingSquadInvestiDetailDTO> getFlyingSquadTeamList() {
		return flyingSquadTeamList;
	}

	public void setFlyingSquadTeamList(ArrayList<FlyingSquadInvestiDetailDTO> flyingSquadTeamList) {
		this.flyingSquadTeamList = flyingSquadTeamList;
	}

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public String getSavemsg() {
		return savemsg;
	}

	public void setSavemsg(String savemsg) {
		this.savemsg = savemsg;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	public boolean isDisableSave() {
		return disableSave;
	}

	public void setDisableSave(boolean disableSave) {
		this.disableSave = disableSave;
	}

	public ArrayList<FlyingSquadInvestiMasterDTO> getRefNoList() {
		return refNoList;
	}

	public void setRefNoList(ArrayList<FlyingSquadInvestiMasterDTO> refNoList) {
		this.refNoList = refNoList;
	}

	public String getFlyingRefNo() {
		return flyingRefNo;
	}

	public void setFlyingRefNo(String flyingRefNo) {
		this.flyingRefNo = flyingRefNo;
	}

}
