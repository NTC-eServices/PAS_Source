package lk.informatics.ntc.view.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.CourtCaseDTO;
import lk.informatics.ntc.model.dto.PermitDTO;
import lk.informatics.ntc.model.dto.SuspendDTO;
import lk.informatics.ntc.model.service.AdminService;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.ManageCourtCaseService;
import lk.informatics.ntc.model.service.RouteCreatorService;
import lk.informatics.ntc.model.service.SuspendService;
import lk.informatics.ntc.model.service.TerminalManagementService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ViewScoped
@ManagedBean(name = "manageCourtCaseNBackingBean")

public class ManageCourtCaseNBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private CommonService commonService;
	private RouteCreatorService routeCreatorService;
	private AdminService adminService;
	private TerminalManagementService terminalManagementService;
	private ManageCourtCaseService manageCourtCaseService;
	private CourtCaseDTO courtCaseDTO;
	private String sucessMsg;
	private String errorMsg;
	private String vehicleNo;
	private String permitNo;
	private String routeNo;
	private String originDesc;
	private String destinationDesc;
	private String strSelectedGroup;
	private List<String> vehicleNoList;
	private List<String> permitNoList;
	private List<CommonDTO> groupList;
	private String user;
	private boolean viewMode;
	private boolean disabledSaved;
	private boolean createMode;
	private boolean editMode;
	private boolean view;
	private ArrayList<CourtCaseDTO> courtlaseList;
	private CourtCaseDTO selectedcourtCaseDTO;
	private boolean disableSaveMode;
	private boolean disableUpdateMode;
	private boolean edit;

	// search fields
	private String scrSataus;
	private String scrvehicleno;
	private String scrpermitNo;
	private String courtcaseNo;
	private String groupname;
	private Date startDate;
	private Date endDate;
	private ArrayList<CourtCaseDTO> courtcaseNoList;

	private SuspendService suspendService;
	private List<SuspendDTO> suspendDetViewList;
	private SuspendDTO viewSelect;
	private SuspendDTO suspendDTO;
	private String popUpRemarks = null;
	private boolean renderPanel;
	private CourtCaseDTO selectednextCallingDateDTO;
	private ArrayList<CourtCaseDTO> nextCourtDateList;
	private boolean nextCallingDateEdit;

	@PostConstruct
	public void init() {
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");
		routeCreatorService = (RouteCreatorService) SpringApplicationContex.getBean("routeCreatorService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		terminalManagementService = (TerminalManagementService) SpringApplicationContex
				.getBean("terminalManagementService");
		manageCourtCaseService = (ManageCourtCaseService) SpringApplicationContex.getBean("manageCourtCaseService");
		user = sessionBackingBean.getLoginUser();
		createMode = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN362", "C");
		editMode = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN362", "E");
		viewMode = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN362", "V");
		loadValus();
		suspendService = (SuspendService) SpringApplicationContex.getBean("suspendService");
		if (createMode == true) {
			disabledSaved = true;
		} else if (editMode == true) {
			disabledSaved = true;
		} else if (editMode == true && viewMode == true) {
			disabledSaved = true;
		} else if (viewMode == true) {
			disabledSaved = false;
			view = true;
		}

		disableSaveMode = true;
		disableUpdateMode = false;
		courtCaseDTO = new CourtCaseDTO();
		edit = false;
		courtcaseNoList = manageCourtCaseService.courtcaseNoList();
	}

	public void loadValus() {
		groupList = commonService.GetCourtGroups();
		renderPanel = false;
		nextCallingDateEdit = false;
	}

	public void add() {
		String courtCaseNo = null;
		boolean valid = false;

		valid = validateForm();

		if (valid) {

			if (courtCaseDTO.getCaseNo() != null && !courtCaseDTO.getCaseNo().equalsIgnoreCase("")
					&& !courtCaseDTO.getCaseNo().isEmpty()) {

				boolean isSpaceFound = courtCaseDTO.getCaseNo().contains(" ");

				if (isSpaceFound == false) {

					boolean isCaseNoFound = manageCourtCaseService.checkCourtCaseNo(courtCaseDTO.getCaseNo());
					if (isCaseNoFound == false) {

						manageCourtCaseService.insertdataN(courtCaseDTO, user);
						manageCourtCaseService.updateOffenceCodeInNumberGenTable(courtCaseDTO.getCaseNo(), user);
						courtlaseList = manageCourtCaseService.getcourtcaseListN("C");
						courtCaseDTO = new CourtCaseDTO();
						disableSaveMode = true;
						disableUpdateMode = false;
						originDesc = null;
						destinationDesc = null;
						sucessMsg = "Successfully Saved";
						RequestContext.getCurrentInstance().update("frmsuccessSve");
						RequestContext.getCurrentInstance().execute("PF('SaveSuccess').show()");

					} else {
						setErrorMsg("Duplicate Court Case No. found.");
						RequestContext.getCurrentInstance().update("frmError");
						RequestContext.getCurrentInstance().execute("PF('Error').show()");
					}
				} else {
					setErrorMsg("Court Case No. cannot contain spaces.");
					RequestContext.getCurrentInstance().update("frmError");
					RequestContext.getCurrentInstance().execute("PF('Error').show()");
				}
			} else {
				setErrorMsg("Court Case No.required. ");
				RequestContext.getCurrentInstance().update("frmError");
				RequestContext.getCurrentInstance().execute("PF('Error').show()");

			}

		}

	}

	public void update() {

		boolean valid = false;
		valid = validateForm();

		if (valid) {

			manageCourtCaseService.updatedataN(courtCaseDTO, user);
			courtlaseList = manageCourtCaseService.getcourtcaseListN("C");
			courtCaseDTO = new CourtCaseDTO();
			selectedcourtCaseDTO = new CourtCaseDTO();
			disableSaveMode = true;
			disableUpdateMode = false;
			originDesc = null;
			destinationDesc = null;
			sucessMsg = "Successfully Updated";
			RequestContext.getCurrentInstance().update("frmsuccessSve");
			RequestContext.getCurrentInstance().execute("PF('SaveSuccess').show()");

		}
	}

	private boolean validateForm() {
		boolean ret;
		if (courtCaseDTO.getVehicleNo() != null && !courtCaseDTO.getVehicleNo().equalsIgnoreCase("")) {
			if (courtCaseDTO.getCourtName() != null && !courtCaseDTO.getCourtName().equalsIgnoreCase("")
					&& !courtCaseDTO.getCourtName().isEmpty()) {
				if (courtCaseDTO.getCaseNo() != null && !courtCaseDTO.getCaseNo().equalsIgnoreCase("")
						&& !courtCaseDTO.getCaseNo().isEmpty()) {
					if (courtCaseDTO.getDateofCourtCase() != null) {
						if (courtCaseDTO.getGroupCode() != null && !courtCaseDTO.getGroupCode().trim().isEmpty()) {
							ret = true;
						} else {
							setErrorMsg("Group No. required.");
							RequestContext.getCurrentInstance().update("frmError");
							RequestContext.getCurrentInstance().execute("PF('Error').show()");
							return false;
						}
					} else {
						setErrorMsg("Date of Court Case required.");
						RequestContext.getCurrentInstance().update("frmError");
						RequestContext.getCurrentInstance().execute("PF('Error').show()");
						return false;
					}
				} else {
					setErrorMsg("Court Case No. required.");
					RequestContext.getCurrentInstance().update("frmError");
					RequestContext.getCurrentInstance().execute("PF('Error').show()");
					return false;
				}
			} else {
				setErrorMsg("Court Name required.");
				RequestContext.getCurrentInstance().update("frmError");
				RequestContext.getCurrentInstance().execute("PF('Error').show()");
				return false;
			}
		} else {
			setErrorMsg("Vehicle No. required.");
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('Error').show()");
			return false;
		}

		return ret;
	}

	public void clearAction() {
		strSelectedGroup = null;
		vehicleNo = null;
		permitNo = null;
		routeNo = null;
		originDesc = null;
		destinationDesc = null;
		courtCaseDTO = new CourtCaseDTO();
		loadValus();
		courtlaseList = manageCourtCaseService.getcourtcaseListN("C");
		disableSaveMode = true;
		disableUpdateMode = false;
		selectedcourtCaseDTO = new CourtCaseDTO();
		edit = false;

		scrSataus = null;
		scrvehicleno = null;
		scrpermitNo = null;
		courtcaseNo = null;
		groupname = null;
		startDate = null;
		endDate = null;
	}

	public void editaction() throws ParseException {
		courtCaseDTO.setCourtCaseSeq(selectedcourtCaseDTO.getCourtCaseSeq());
		courtCaseDTO.setVehicleNo(selectedcourtCaseDTO.getVehicleNo());
		courtCaseDTO.setPermitNo(selectedcourtCaseDTO.getPermitNo());
		courtCaseDTO.setRouteNo(selectedcourtCaseDTO.getRouteNo());
		courtCaseDTO.setCourtName(selectedcourtCaseDTO.getCourtName());
		courtCaseDTO.setCaseNo(selectedcourtCaseDTO.getCaseNo());
		Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(selectedcourtCaseDTO.getDateOfCourt());
		courtCaseDTO.setDateofCourtCase(date1);
		courtCaseDTO.setGroupCode(selectedcourtCaseDTO.getGroupCode());
		courtCaseDTO.setGroupName(selectedcourtCaseDTO.getGroupName());
		courtCaseDTO.setStatus(selectedcourtCaseDTO.getStatus());
		courtCaseDTO.setCreatedBy(selectedcourtCaseDTO.getCreatedBy());
		courtCaseDTO.setModifyBy(selectedcourtCaseDTO.getModifyBy());
		courtCaseDTO.setCreatedDate(selectedcourtCaseDTO.getCreatedDate());
		courtCaseDTO.setModifiedDate(selectedcourtCaseDTO.getModifiedDate());
		courtCaseDTO.setOrgin(selectedcourtCaseDTO.getOrgin());
		courtCaseDTO.setDestination(selectedcourtCaseDTO.getDestination());

		if (selectedcourtCaseDTO.getInspDate() != null) {
			Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(selectedcourtCaseDTO.getInspDate());
			courtCaseDTO.setInspectStartDate(date2);
		}
		courtCaseDTO.setRemarks(selectedcourtCaseDTO.getRemarks());
		courtCaseDTO.setActionByCourt(selectedcourtCaseDTO.getActionByCourt());
		PermitDTO permitDetailDTO = terminalManagementService.getPermitInfoByBusNoPermitNo(null,
				selectedcourtCaseDTO.getVehicleNo());
		originDesc = permitDetailDTO.getOrigin();
		destinationDesc = permitDetailDTO.getDestination();

		disableSaveMode = false;
		disableUpdateMode = true;
		edit = true;
	}

	public void search() {

		courtlaseList = manageCourtCaseService.getcourtcaseListwithparamN(scrSataus, scrvehicleno, scrpermitNo,
				courtcaseNo, groupname, startDate, endDate);
	}

	public void clean() {
		scrSataus = null;
		scrvehicleno = null;
		scrpermitNo = null;
		courtcaseNo = null;
		groupname = null;
		startDate = null;
		endDate = null;
		renderPanel = false;
		courtlaseList = manageCourtCaseService.getcourtcaseListN("C");

	}

	public void viewaction() {

		suspendDetViewList = suspendService.getSuspendListByPermitNo(selectedcourtCaseDTO.getPermitNo());
		RequestContext.getCurrentInstance().execute("PF('suspendDlg').show()");
		RequestContext.getCurrentInstance().update("suspendDlg");
		suspendDTO = new SuspendDTO();
	}

	public void nextCallingdateAdd() {
		if (courtCaseDTO.getNextCallingDate() != null && courtCaseDTO.getCourtNameS() != null
				&& !courtCaseDTO.getCourtNameS().trim().equals("")) {

			if (!nextCallingDateEdit) {
				courtCaseDTO.setCaseNo(selectedcourtCaseDTO.getCaseNo());
				courtCaseDTO.setCourtCaseSeq(selectedcourtCaseDTO.getCourtCaseSeq());
				if (selectedcourtCaseDTO.getStatusdes() != null) {

					if (selectedcourtCaseDTO.getStatusdes().equalsIgnoreCase("ACTIVE")) {
						courtCaseDTO.setStatus("A");
					} else {
						courtCaseDTO.setStatus("I");
					}

				}

				manageCourtCaseService.insertNextCallingDate(courtCaseDTO, user);
			} else {

				courtCaseDTO.setCaseNoS(selectednextCallingDateDTO.getCaseNoS());
				courtCaseDTO.setCourtCaseSeqS(selectednextCallingDateDTO.getCourtCaseSeqS());
				manageCourtCaseService.updateNextCallingDate(courtCaseDTO, user);
				nextCallingDateEdit = false;

			}
			nextCourtDateList = manageCourtCaseService.getnextCallingDateValues(courtCaseDTO.getCaseNo());
		}
	}

	public void closeAction(String statusdes) {
		if (statusdes != null) {
			if (statusdes.equalsIgnoreCase("Active")) {
				Date date = new Date();
				courtCaseDTO.setCourtCaseCloseDate(date);
				RequestContext.getCurrentInstance().execute("PF('closeBtnDlg').show()");
				RequestContext.getCurrentInstance().update("closeBtnDlg");
				courtlaseList = manageCourtCaseService.getcourtcaseListN("C");
			} else {
				setErrorMsg("Inactive records cannot  closed");
				RequestContext.getCurrentInstance().update("frmError");
				RequestContext.getCurrentInstance().execute("PF('Error').show()");

			}
		}
	}

	public void clearPopupVal() {
		popUpRemarks = null;
		Date date = new Date();

		courtCaseDTO.setCourtCaseCloseDate(date);

	}

	public void saveCloseDatePopup() {
		courtCaseDTO.setCaseNo(selectedcourtCaseDTO.getCaseNo());
		if (courtCaseDTO.getCourtCaseCloseDate() != null) {
			manageCourtCaseService.updateCourtCaseCloseDate(courtCaseDTO, popUpRemarks, user);
			sucessMsg = "Successfully Closed";
			RequestContext.getCurrentInstance().update("frmsuccessSve");
			RequestContext.getCurrentInstance().execute("PF('SaveSuccess').show()");
			clearPopupVal();
			courtlaseList = manageCourtCaseService.getcourtcaseListN("C");
			selectedcourtCaseDTO = new CourtCaseDTO();
			courtCaseDTO = new CourtCaseDTO();
			nextCourtDateList = new ArrayList<>();
		}

		else {
			setErrorMsg("Court Case Close Date required.");
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('Error').show()");

		}

	}

	public void selectRow() {
		nextCourtDateList = new ArrayList<>();
		courtCaseDTO.setNextCallingDate(null);
		courtCaseDTO.setCourtNameS(null);
		renderPanel = true;
		nextCourtDateList = manageCourtCaseService.getnextCallingDateValues(selectedcourtCaseDTO.getCaseNo());
	}

	public void nextCallingDateEdit() throws ParseException {

		nextCallingDateEdit = true;
		courtCaseDTO.setCaseNo(selectednextCallingDateDTO.getCaseNoS());
		courtCaseDTO.setCourtNameS(selectednextCallingDateDTO.getCourtNameS());
		courtCaseDTO.setNextCallingDate(
				new SimpleDateFormat("dd/MM/yyyy").parse(selectednextCallingDateDTO.getNextCallingDateS()));

	}

	public void viewGridDataInfield() {
		suspendDTO = suspendService.getDetasilsDTOByPermitNO(viewSelect.getPermitNo());

	}

	public void deleteAction() {
		manageCourtCaseService.recordUpdateAsDelete(selectednextCallingDateDTO, user);
		nextCourtDateList = manageCourtCaseService.getnextCallingDateValues(courtCaseDTO.getCaseNo());

	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public RouteCreatorService getRouteCreatorService() {
		return routeCreatorService;
	}

	public void setRouteCreatorService(RouteCreatorService routeCreatorService) {
		this.routeCreatorService = routeCreatorService;
	}

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	public TerminalManagementService getTerminalManagementService() {
		return terminalManagementService;
	}

	public void setTerminalManagementService(TerminalManagementService terminalManagementService) {
		this.terminalManagementService = terminalManagementService;
	}

	public String getSucessMsg() {
		return sucessMsg;
	}

	public void setSucessMsg(String sucessMsg) {
		this.sucessMsg = sucessMsg;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public String getPermitNo() {
		return permitNo;
	}

	public void setPermitNo(String permitNo) {
		this.permitNo = permitNo;
	}

	public String getRouteNo() {
		return routeNo;
	}

	public void setRouteNo(String routeNo) {
		this.routeNo = routeNo;
	}

	public String getOriginDesc() {
		return originDesc;
	}

	public void setOriginDesc(String originDesc) {
		this.originDesc = originDesc;
	}

	public String getDestinationDesc() {
		return destinationDesc;
	}

	public void setDestinationDesc(String destinationDesc) {
		this.destinationDesc = destinationDesc;
	}

	public List<String> getVehicleNoList() {
		return vehicleNoList;
	}

	public void setVehicleNoList(List<String> vehicleNoList) {
		this.vehicleNoList = vehicleNoList;
	}

	public List<String> getPermitNoList() {
		return permitNoList;
	}

	public void setPermitNoList(List<String> permitNoList) {
		this.permitNoList = permitNoList;
	}

	public List<CommonDTO> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<CommonDTO> groupList) {
		this.groupList = groupList;
	}

	public String getStrSelectedGroup() {
		return strSelectedGroup;
	}

	public void setStrSelectedGroup(String strSelectedGroup) {
		this.strSelectedGroup = strSelectedGroup;
	}

	public CourtCaseDTO getCourtCaseDTO() {
		return courtCaseDTO;
	}

	public void setCourtCaseDTO(CourtCaseDTO courtCaseDTO) {
		this.courtCaseDTO = courtCaseDTO;
	}

	public ManageCourtCaseService getManageCourtCaseService() {
		return manageCourtCaseService;
	}

	public void setManageCourtCaseService(ManageCourtCaseService manageCourtCaseService) {
		this.manageCourtCaseService = manageCourtCaseService;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
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

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	public boolean isView() {
		return view;
	}

	public void setView(boolean view) {
		this.view = view;
	}

	public ArrayList<CourtCaseDTO> getCourtlaseList() {
		return courtlaseList;
	}

	public void setCourtlaseList(ArrayList<CourtCaseDTO> courtlaseList) {
		this.courtlaseList = courtlaseList;
	}

	public CourtCaseDTO getSelectedcourtCaseDTO() {
		return selectedcourtCaseDTO;
	}

	public void setSelectedcourtCaseDTO(CourtCaseDTO selectedcourtCaseDTO) {
		this.selectedcourtCaseDTO = selectedcourtCaseDTO;
	}

	public boolean isDisableSaveMode() {
		return disableSaveMode;
	}

	public void setDisableSaveMode(boolean disableSaveMode) {
		this.disableSaveMode = disableSaveMode;
	}

	public boolean isDisableUpdateMode() {
		return disableUpdateMode;
	}

	public void setDisableUpdateMode(boolean disableUpdateMode) {
		this.disableUpdateMode = disableUpdateMode;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public String getScrSataus() {
		return scrSataus;
	}

	public void setScrSataus(String scrSataus) {
		this.scrSataus = scrSataus;
	}

	public String getScrvehicleno() {
		return scrvehicleno;
	}

	public void setScrvehicleno(String scrvehicleno) {
		this.scrvehicleno = scrvehicleno;
	}

	public String getScrpermitNo() {
		return scrpermitNo;
	}

	public void setScrpermitNo(String scrpermitNo) {
		this.scrpermitNo = scrpermitNo;
	}

	public String getCourtcaseNo() {
		return courtcaseNo;
	}

	public void setCourtcaseNo(String courtcaseNo) {
		this.courtcaseNo = courtcaseNo;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public ArrayList<CourtCaseDTO> getCourtcaseNoList() {
		return courtcaseNoList;
	}

	public void setCourtcaseNoList(ArrayList<CourtCaseDTO> courtcaseNoList) {
		this.courtcaseNoList = courtcaseNoList;
	}

	public SuspendService getSuspendService() {
		return suspendService;
	}

	public void setSuspendService(SuspendService suspendService) {
		this.suspendService = suspendService;
	}

	public List<SuspendDTO> getSuspendDetViewList() {
		return suspendDetViewList;
	}

	public void setSuspendDetViewList(List<SuspendDTO> suspendDetViewList) {
		this.suspendDetViewList = suspendDetViewList;
	}

	public SuspendDTO getViewSelect() {
		return viewSelect;
	}

	public void setViewSelect(SuspendDTO viewSelect) {
		this.viewSelect = viewSelect;
	}

	public SuspendDTO getSuspendDTO() {
		return suspendDTO;
	}

	public void setSuspendDTO(SuspendDTO suspendDTO) {
		this.suspendDTO = suspendDTO;
	}

	public String getPopUpRemarks() {
		return popUpRemarks;
	}

	public void setPopUpRemarks(String popUpRemarks) {
		this.popUpRemarks = popUpRemarks;
	}

	public boolean isRenderPanel() {
		return renderPanel;
	}

	public void setRenderPanel(boolean renderPanel) {
		this.renderPanel = renderPanel;
	}

	public CourtCaseDTO getSelectednextCallingDateDTO() {
		return selectednextCallingDateDTO;
	}

	public void setSelectednextCallingDateDTO(CourtCaseDTO selectednextCallingDateDTO) {
		this.selectednextCallingDateDTO = selectednextCallingDateDTO;
	}

	public ArrayList<CourtCaseDTO> getNextCourtDateList() {
		return nextCourtDateList;
	}

	public void setNextCourtDateList(ArrayList<CourtCaseDTO> nextCourtDateList) {
		this.nextCourtDateList = nextCourtDateList;
	}

}
