package lk.informatics.ntc.view.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;  
import java.util.Date;  

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.FlyingSquadActionPointsDTO;
import lk.informatics.ntc.model.service.FlyManageGroupService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name ="flyingSquadActionPointsBean")
@ViewScoped
public class FlyingSquadActionPointsBean {
	
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	
	private String user;
	private boolean allowUpdate;
	private FlyingSquadActionPointsDTO strSelectedActionCode;
	private boolean disableUpdateMode,disableSaveMode;	
	private String errormsg;
	private FlyManageGroupService flyManageGroupService;
	private FlyingSquadActionPointsDTO flyingActionDTO;
	private List<FlyingSquadActionPointsDTO> actionList = new ArrayList<FlyingSquadActionPointsDTO>();
	
	@PostConstruct
	public void init() {
		
		setFlyManageGroupService((FlyManageGroupService) SpringApplicationContex
				.getBean("flyManageGroupService"));
		setUser(sessionBackingBean.getLoginUser());	
		setFlyingActionDTO(new FlyingSquadActionPointsDTO());
		allowUpdate = false;
		disableUpdateMode=false;
		disableSaveMode=true;
		loadValues();
	}
 	public void loadValues(){
 		setActionList(flyManageGroupService.getActionPointDetails());	
 	}
	public void add()
	{
		if (flyingActionDTO.getActionCode() != null && !flyingActionDTO.getActionCode().isEmpty()
				&& !flyingActionDTO.getActionCode().equalsIgnoreCase("")) {
			if (flyingActionDTO.getActionDescription() != null && !flyingActionDTO.getActionDescription().isEmpty()
					&& !flyingActionDTO.getActionDescription().equalsIgnoreCase("")) {
				if (flyingActionDTO.getStartTimeN() != null) {
					if (flyingActionDTO.getEndTimeN() != null) {
						String chkDuplicates = flyManageGroupService.checkDuplicate(flyingActionDTO.getActionCode());
						if (chkDuplicates == null) {
						
						String ssDate=new SimpleDateFormat("HH:mm").format(flyingActionDTO.getStartTimeN());
						String eeDate=new SimpleDateFormat("HH:mm").format(flyingActionDTO.getEndTimeN());												
						flyingActionDTO.setStartTime(ssDate);
						flyingActionDTO.setEndTime(eeDate);
						
						boolean isInserted = flyManageGroupService.insertActionPoints(flyingActionDTO, user);
						if(isInserted)
						{
							RequestContext.getCurrentInstance().update("frmsuccessSve");
							RequestContext.getCurrentInstance().execute("PF('SaveSuccess').show()");
							clearAction();
							loadValues();	
						}
						else
						{
							setErrormsg("Data not Saved.");
							RequestContext.getCurrentInstance().update("frmError");
							RequestContext.getCurrentInstance().execute("PF('Error').show()");
						}
						
						}
						else
						{
							setErrormsg("Duplicate Action Code.");
							RequestContext.getCurrentInstance().update("frmError");
							RequestContext.getCurrentInstance().execute("PF('Error').show()");
							flyingActionDTO.setActionCode(null);
						}
					}
					else
					{
						setErrormsg("Please Enter Service End Time.");
						RequestContext.getCurrentInstance().update("frmError");
						RequestContext.getCurrentInstance().execute("PF('Error').show()");
					}			
				}
				else
				{
					setErrormsg("Please Enter Service Start Time.");
					RequestContext.getCurrentInstance().update("frmError");
					RequestContext.getCurrentInstance().execute("PF('Error').show()");
				}		
			}
			else
			{
				setErrormsg("Please Enter Service Description.");
				RequestContext.getCurrentInstance().update("frmError");
				RequestContext.getCurrentInstance().execute("PF('Error').show()");
			}	
		}
		else
		{
			setErrormsg("Please Enter Service Code.");
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('Error').show()");
		}
	}
	public void clearAction()
	{
		flyingActionDTO = new FlyingSquadActionPointsDTO();
		allowUpdate = false;
		disableUpdateMode=false;
		disableSaveMode=true;
	}	
	public void update()
	{
		String ssDate=new SimpleDateFormat("HH:mm").format(flyingActionDTO.getStartTimeN());
		String eeDate=new SimpleDateFormat("HH:mm").format(flyingActionDTO.getEndTimeN());												
		flyingActionDTO.setStartTime(ssDate);
		flyingActionDTO.setEndTime(eeDate);
		
		boolean isUpdated = flyManageGroupService.updateActionPoints(flyingActionDTO, user);
		if(isUpdated)
		{
			RequestContext.getCurrentInstance().update("frmsuccessSve");
			RequestContext.getCurrentInstance().execute("PF('SaveSuccess').show()");
			clearAction();
			loadValues();
		}
		else
		{
			setErrormsg("Data not Updated.");
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('Error').show()");
		}
	}
	public void editAction() throws ParseException
	{
		flyingActionDTO.setActionCode(strSelectedActionCode.getActionCode());
		flyingActionDTO.setActionDescription(strSelectedActionCode.getActionDescription());
		flyingActionDTO.setAllowforInvestigation(strSelectedActionCode.getAllowforInvestigation());
		flyingActionDTO.setStatus(strSelectedActionCode.getStatus());
				
		String sdate =strSelectedActionCode.getStartTime();
		Date snDate = new SimpleDateFormat("HH:mm").parse(sdate);  
		String edate =strSelectedActionCode.getEndTime();
		Date enDate = new SimpleDateFormat("HH:mm").parse(edate);  
		
		flyingActionDTO.setStartTimeN(snDate);
		flyingActionDTO.setEndTimeN(enDate);
		
		disableUpdateMode=true;
		disableSaveMode=false;
	}
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public boolean isAllowUpdate() {
		return allowUpdate;
	}
	public void setAllowUpdate(boolean allowUpdate) {
		this.allowUpdate = allowUpdate;
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
	public String getErrormsg() {
		return errormsg;
	}
	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}
	public FlyManageGroupService getFlyManageGroupService() {
		return flyManageGroupService;
	}
	public void setFlyManageGroupService(FlyManageGroupService flyManageGroupService) {
		this.flyManageGroupService = flyManageGroupService;
	}
	public FlyingSquadActionPointsDTO getStrSelectedActionCode() {
		return strSelectedActionCode;
	}
	public void setStrSelectedActionCode(FlyingSquadActionPointsDTO strSelectedActionCode) {
		this.strSelectedActionCode = strSelectedActionCode;
	}
	public List<FlyingSquadActionPointsDTO> getActionList() {
		return actionList;
	}
	public void setActionList(List<FlyingSquadActionPointsDTO> actionList) {
		this.actionList = actionList;
	}
	public FlyingSquadActionPointsDTO getFlyingActionDTO() {
		return flyingActionDTO;
	}
	public void setFlyingActionDTO(FlyingSquadActionPointsDTO flyingActionDTO) {
		this.flyingActionDTO = flyingActionDTO;
	}
	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}
	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}
	
}
