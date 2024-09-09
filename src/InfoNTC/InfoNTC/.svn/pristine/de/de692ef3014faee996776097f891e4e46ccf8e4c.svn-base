package lk.informatics.ntc.view.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;

import lk.informatics.ntc.model.dto.FlyingSquadApprovalInvestigationDTO;
import lk.informatics.ntc.model.dto.FlyngSquadAttendanceDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.FlyingSquadAttendanceService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "flyingSquadAttendanceBean")
@ViewScoped
public class FlyingSquadAttendanceBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	private ArrayList<FlyngSquadAttendanceDTO> detailList;
	private Date invDate;
	private String refNo;
	private String groupcd;
	private String groupName;
	private String user;
	private String errormsg;
	private String savemsg;
	private boolean updated;
	private FlyingSquadAttendanceService flyingSquadAttendanceService;
	private ArrayList<FlyingSquadApprovalInvestigationDTO> refnoList;
	private FlyngSquadAttendanceDTO flyngSquadAttendanceDTO;
	private ArrayList<FlyngSquadAttendanceDTO> detailListhis;
	private ArrayList<FlyngSquadAttendanceDTO> groupList;
	private boolean viewMode;
	private CommonService commonService;
	private boolean disabledSaved;
	private boolean createMode;
	private int loadedcount;
	private int noneditedcount;
	private boolean btnRender;
	private boolean btnRenderSave;

	@PostConstruct
	public void init() {
		setFlyingSquadAttendanceService(
				(FlyingSquadAttendanceService) SpringApplicationContex.getBean("flyingSquadAttendanceService"));
		setUser(sessionBackingBean.getLoginUser());
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		createMode = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN360", "C");
		viewMode = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN360", "V");
		detailListhis = new ArrayList<FlyngSquadAttendanceDTO>();
		refnoList = new ArrayList<FlyingSquadApprovalInvestigationDTO>();
		detailList = new ArrayList<FlyngSquadAttendanceDTO>();
		groupList = new ArrayList<FlyngSquadAttendanceDTO>();
		flyngSquadAttendanceDTO = new FlyngSquadAttendanceDTO();
		refnoList = flyingSquadAttendanceService.getReferenceNo();
		groupList = flyingSquadAttendanceService.getgroups();
		updated = false;
		loadedcount = 0;
		noneditedcount = 0;
		btnRender = false;
		btnRenderSave = true;

		if (viewMode == true) {
			disabledSaved = true;
		} else {
			disabledSaved = false;
		}

		if (createMode == true) {
			disabledSaved = false;
		} else {
			disabledSaved = true;
		}

	}

	public void getreferenceData() {
		flyngSquadAttendanceDTO = flyingSquadAttendanceService.getRefData(refNo);
		groupcd = flyngSquadAttendanceDTO.getGroupCd();
		groupName = flyngSquadAttendanceDTO.getGroupdes();
		invDate = flyngSquadAttendanceDTO.getIvesDate();
		detailList = new ArrayList<FlyngSquadAttendanceDTO>();
		loadedcount = 0;
		noneditedcount = 0;

	}

	public void getdata() {
		refNo = flyingSquadAttendanceService.getRefNo(invDate, groupcd);
		detailList = new ArrayList<FlyngSquadAttendanceDTO>();

		loadedcount = 0;
		noneditedcount = 0;
	}

	public void savedata() {
		invDate = flyngSquadAttendanceDTO.getIvesDate();
		for (FlyngSquadAttendanceDTO flyngSquadAttendanceDTOO : detailList) {
			if (flyngSquadAttendanceDTOO.getIsattend() == false) {
				noneditedcount++;
			}
		}

		if (loadedcount == noneditedcount) {
			errormsg = "No Changes to Save.";
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('ErrorMsg').show()");

		} else {
			for (FlyngSquadAttendanceDTO flyngSquadAttendanceDTO : detailList) {

				if (flyngSquadAttendanceDTO.getIsattend() == true) {

					if (flyngSquadAttendanceDTO.getStartTime() == null) {

						errormsg = "Please Enter In Time.";
						RequestContext.getCurrentInstance().update("frmError");
						RequestContext.getCurrentInstance().execute("PF('ErrorMsg').show()");
						return;
					} else {
						if (flyngSquadAttendanceDTO.getIsupdate() == false) {
							flyingSquadAttendanceService.saveDetailDta(flyngSquadAttendanceDTO, user, refNo, groupcd,
									invDate);
							updated = false;

						} else {
							updated = true;
							flyingSquadAttendanceService.updateDetailDta(flyngSquadAttendanceDTO, user, refNo, groupcd,
									invDate);

						}

					}

				} else {
					if (flyngSquadAttendanceDTO.getIsupdate() == false) {
						flyingSquadAttendanceService.saveDetailDta(flyngSquadAttendanceDTO, user, refNo, groupcd,
								invDate);
						updated = false;

					} else {
						updated = true;
						flyingSquadAttendanceService.updateDetailDta(flyngSquadAttendanceDTO, user, refNo, groupcd,
								invDate);

					}

				}

			}

			if (updated == true) {
				flyingSquadAttendanceService.saveHistoryDetailDta(detailListhis, user, refNo, groupcd, invDate);
				setSavemsg("Successfully Updated.");
				RequestContext.getCurrentInstance().update("frmsuccessSve");
				RequestContext.getCurrentInstance().execute("PF('SaveSuccess').show()");
			} else {
				setSavemsg("Successfully  Saved");
				RequestContext.getCurrentInstance().update("frmsuccessSve");
				RequestContext.getCurrentInstance().execute("PF('SaveSuccess').show()");
				btnRender = true; // display Update Button
				btnRenderSave = false;// hide save button
			}
		}

	}

	public void updatedata() {
		for (FlyngSquadAttendanceDTO flyngSquadAttendanceDTO : detailList) {

			if (flyngSquadAttendanceDTO.getIsattend() == true) {

				if (flyngSquadAttendanceDTO.getStartTime() == null) {
					errormsg = "Please Enter In Time.";
					RequestContext.getCurrentInstance().update("frmError");
					RequestContext.getCurrentInstance().execute("PF('ErrorMsg').show()");
					return;
				} else {
					if (flyngSquadAttendanceDTO.getStartTime() != null
							&& flyngSquadAttendanceDTO.getEndtime() != null) {
						if (flyngSquadAttendanceDTO.getStartTime()
								.compareTo(flyngSquadAttendanceDTO.getEndtime()) > 0) {
							errormsg = "Out Time Cannot Less Than In Time.";
							RequestContext.getCurrentInstance().update("frmError");
							RequestContext.getCurrentInstance().execute("PF('ErrorMsg').show()");
							return;
						} else {
							updated = true;
							flyingSquadAttendanceService.updateDetailDta(flyngSquadAttendanceDTO, user, refNo, groupcd,
									invDate);
							/*flyingSquadAttendanceService.saveHistoryDetailDta(detailListhis, user, refNo, groupcd,
									invDate);*/
							setSavemsg("Successfully Updated.");
							RequestContext.getCurrentInstance().update("frmsuccessSve");
							RequestContext.getCurrentInstance().execute("PF('SaveSuccess').show()");
						}

					} else {
						updated = true;
						flyingSquadAttendanceService.updateDetailDta(flyngSquadAttendanceDTO, user, refNo, groupcd,
								invDate);
						flyingSquadAttendanceService.saveHistoryDetailDta(detailListhis, user, refNo, groupcd, invDate);
						setSavemsg("Successfully Updated.");
						RequestContext.getCurrentInstance().update("frmsuccessSve");
						RequestContext.getCurrentInstance().execute("PF('SaveSuccess').show()");
					}

				}

			} else {
				updated = true;
				flyingSquadAttendanceService.updateDetailDta(flyngSquadAttendanceDTO, user, refNo, groupcd, invDate);
				flyingSquadAttendanceService.saveHistoryDetailDta(detailListhis, user, refNo, groupcd, invDate);
				setSavemsg("Successfully Updated.");
				RequestContext.getCurrentInstance().update("frmsuccessSve");
				RequestContext.getCurrentInstance().execute("PF('SaveSuccess').show()");
			}

		}

	}

	public void clear() {
		detailListhis = new ArrayList<FlyngSquadAttendanceDTO>();
		refnoList = new ArrayList<FlyingSquadApprovalInvestigationDTO>();
		detailList = new ArrayList<FlyngSquadAttendanceDTO>();
		flyngSquadAttendanceDTO = new FlyngSquadAttendanceDTO();
		refnoList = flyingSquadAttendanceService.getReferenceNo();
		updated = false;
		invDate = null;
		refNo = null;
		groupcd = null;
		groupName = null;
		groupList = flyingSquadAttendanceService.getgroups();
		loadedcount = 0;
		noneditedcount = 0;
		btnRender = false;
		btnRenderSave = true;
	}

	public void emptymethod() {

		invDate = flyngSquadAttendanceDTO.getIvesDate();

	}

	public void search() {
		if (refNo == null || refNo.equalsIgnoreCase("") || refNo.isEmpty()) {
			errormsg = "Please Select the Reference No.";
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('ErrorMsg').show()");
		} else {
			if (groupcd == null || groupcd.equalsIgnoreCase("") || groupcd.isEmpty()) {
				errormsg = "Please Select the Group Code";
				RequestContext.getCurrentInstance().update("frmError");
				RequestContext.getCurrentInstance().execute("PF('ErrorMsg').show()");
			} else {
				if (invDate == null) {
					errormsg = "Please Select the Investigation Date";
					RequestContext.getCurrentInstance().update("frmError");
					RequestContext.getCurrentInstance().execute("PF('ErrorMsg').show()");
				} else {
					detailList = flyingSquadAttendanceService.getexsistingdetails(refNo,
							flyngSquadAttendanceDTO.getIvesDate());
					detailListhis = detailList;
					loadedcount = detailList.size();

					String isExist = flyingSquadAttendanceService.isAttended(refNo,
							flyngSquadAttendanceDTO.getIvesDate());
					if ((isExist != null)) {
						btnRender = true; // display Update Button
						btnRenderSave = false;// hide save button
					} else {
						btnRender = false; // hide update button
						btnRenderSave = true; // display save button
					}
				}
			}
		}

	}

	public void onCellEdit(CellEditEvent event) {
		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();
	}

	public ArrayList<FlyngSquadAttendanceDTO> getDetailList() {
		return detailList;
	}

	public void setDetailList(ArrayList<FlyngSquadAttendanceDTO> detailList) {
		this.detailList = detailList;
	}

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	public FlyingSquadAttendanceService getFlyingSquadAttendanceService() {
		return flyingSquadAttendanceService;
	}

	public void setFlyingSquadAttendanceService(FlyingSquadAttendanceService flyingSquadAttendanceService) {
		this.flyingSquadAttendanceService = flyingSquadAttendanceService;
	}

	public ArrayList<FlyingSquadApprovalInvestigationDTO> getRefnoList() {
		return refnoList;
	}

	public void setRefnoList(ArrayList<FlyingSquadApprovalInvestigationDTO> refnoList) {
		this.refnoList = refnoList;
	}

	public FlyngSquadAttendanceDTO getFlyngSquadAttendanceDTO() {
		return flyngSquadAttendanceDTO;
	}

	public void setFlyngSquadAttendanceDTO(FlyngSquadAttendanceDTO flyngSquadAttendanceDTO) {
		this.flyngSquadAttendanceDTO = flyngSquadAttendanceDTO;
	}

	public String getGroupcd() {
		return groupcd;
	}

	public void setGroupcd(String groupcd) {
		this.groupcd = groupcd;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public ArrayList<FlyngSquadAttendanceDTO> getDetailListhis() {
		return detailListhis;
	}

	public void setDetailListhis(ArrayList<FlyngSquadAttendanceDTO> detailListhis) {
		this.detailListhis = detailListhis;
	}

	public String getSavemsg() {
		return savemsg;
	}

	public void setSavemsg(String savemsg) {
		this.savemsg = savemsg;
	}

	public boolean isUpdated() {
		return updated;
	}

	public void setUpdated(boolean updated) {
		this.updated = updated;
	}

	public ArrayList<FlyngSquadAttendanceDTO> getGroupList() {
		return groupList;
	}

	public void setGroupList(ArrayList<FlyngSquadAttendanceDTO> groupList) {
		this.groupList = groupList;
	}

	public Date getInvDate() {
		return invDate;
	}

	public void setInvDate(Date invDate) {
		this.invDate = invDate;
	}

	public boolean isViewMode() {
		return viewMode;
	}

	public void setViewMode(boolean viewMode) {
		this.viewMode = viewMode;
	}

	public boolean isDisabledSaved() {
		return disabledSaved;
	}

	public void setDisabledSaved(boolean disabledSaved) {
		this.disabledSaved = disabledSaved;
	}

	public boolean isCreateMode() {
		return createMode;
	}

	public void setCreateMode(boolean createMode) {
		this.createMode = createMode;
	}

	public int getLoadedcount() {
		return loadedcount;
	}

	public void setLoadedcount(int loadedcount) {
		this.loadedcount = loadedcount;
	}

	public int getNoneditedcount() {
		return noneditedcount;
	}

	public void setNoneditedcount(int noneditedcount) {
		this.noneditedcount = noneditedcount;
	}

	public boolean isBtnRender() {
		return btnRender;
	}

	public void setBtnRender(boolean btnRender) {
		this.btnRender = btnRender;
	}

	public boolean isBtnRenderSave() {
		return btnRenderSave;
	}

	public void setBtnRenderSave(boolean btnRenderSave) {
		this.btnRenderSave = btnRenderSave;
	}

}
