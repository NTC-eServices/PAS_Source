package lk.informatics.ntc.view.beans;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.PermitDTO;
import lk.informatics.ntc.model.dto.PermitRenewalsDTO;
import lk.informatics.ntc.model.dto.PrintInspectionDTO;
import lk.informatics.ntc.model.dto.ProceedIncompleteApplicationDTO;
import lk.informatics.ntc.model.dto.RouteDTO;
import lk.informatics.ntc.model.dto.VehicleInspectionDTO;
import lk.informatics.ntc.model.service.AdminService;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.HistoryService;
import lk.informatics.ntc.model.service.IncompleteApprovalService;
import lk.informatics.ntc.model.service.InspectionActionPointService;
import lk.informatics.ntc.model.service.MigratedService;
import lk.informatics.ntc.model.service.PaymentVoucherService;
import lk.informatics.ntc.model.service.QueueManagementService;
import lk.informatics.ntc.model.service.VehicleInspectionService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "viewOtherVehicleBackingBean")

@ViewScoped
public class ViewOtherInspectionBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	public List<VehicleInspectionDTO> vehiclelist = new ArrayList<>(0);
	public List<VehicleInspectionDTO> makelist = new ArrayList<>();
	public List<VehicleInspectionDTO> servicelist = new ArrayList<>();
	public List<VehicleInspectionDTO> modellist = new ArrayList<>();
	public List<VehicleInspectionDTO> routelist = new ArrayList<>();
	private List<VehicleInspectionDTO> dataList = new ArrayList<VehicleInspectionDTO>();

	private VehicleInspectionDTO vehicleDTO;

	private List<CommonDTO> counterList = new ArrayList<>();
	private String errorMsg, generatedApplicationNO, successMSG, alertMSG, oldApplicationNo;

	private boolean readonlyAppNo = false;
	private boolean readonlyQueueNo = false;
	private Boolean routeFlag;

	private String countererrorMsg;

	private CommonDTO commonDTO = new CommonDTO();
	private RouteDTO routeDTO;
	private PermitDTO permitDTO;
	private VehicleInspectionService vehicleInspectionService;
	private AdminService adminService;
	private QueueManagementService queueManagementService;
	private CommonService commonService;
	private MigratedService migratedService;
	private PaymentVoucherService paymentVoucherService;
	private InspectionActionPointService inspectionActionPointService;
	boolean check = true;
	private boolean disableCallNext, disableSkip, disableUpdate;
	boolean skip = true;
	private boolean localcheckcounter = false;
	private String successmessage, queueNo;
	private boolean data = true, disableSave = false;
	private boolean upload = true, disablePermitNo, disableQueueNo, disableAppNo, readOnlyVehicleNo;
	private boolean reinspection = false;
	private boolean printReport = true;
	private String strTrnCode;
	private String strTrnDesc;
	private boolean inspectionForAmendment;
	private PermitRenewalsDTO applicationHistoryDTO;
	private HistoryService historyService;
	public List<VehicleInspectionDTO> locationList = new ArrayList<>();
	private boolean renderBackOtherInspection;
	private boolean renderBackIncompleteApproval = false;
	private boolean disableProceedIncompleteApplicationBtn = false;
	private ProceedIncompleteApplicationDTO proceedIncompleteApplicationDTO = new ProceedIncompleteApplicationDTO();
	private IncompleteApprovalService incompleteApprovalService;

	private boolean renderSave, renderUpdate;
	private boolean editMode, viewMode;

	@PostConstruct
	public void init() {

		vehicleInspectionService = (VehicleInspectionService) SpringApplicationContex
				.getBean("vehicleInspectionService");
		queueManagementService = (QueueManagementService) SpringApplicationContex.getBean("queueManagementService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		migratedService = (MigratedService) SpringApplicationContex.getBean("migratedService");
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");
		paymentVoucherService = (PaymentVoucherService) SpringApplicationContex.getBean("paymentVoucherService");
		historyService = (HistoryService) SpringApplicationContex.getBean("historyService");
		inspectionActionPointService = (InspectionActionPointService) SpringApplicationContex
				.getBean("inspectionActionPointService");
		incompleteApprovalService = (IncompleteApprovalService) SpringApplicationContex
				.getBean("incompleteApprovalService");

		vehicleDTO = new VehicleInspectionDTO();
		permitDTO = new PermitDTO();
		routeDTO = new RouteDTO();
		dropdown();
		disableSave = true;
		disablePermitNo = false;
		disableQueueNo = false;
		disableAppNo = false;
		routeFlag = false;
		strTrnCode = null;
		strTrnDesc = null;
		printReport = true;
		disableSkip = true;
		renderSave = true;
		disableUpdate = false;

		FacesContext fcontext = FacesContext.getCurrentInstance();

		Object vehicleObjView = fcontext.getExternalContext().getSessionMap().get("VIEW_OTHER");
		if (vehicleObjView != null) {
			VehicleInspectionDTO dto = (VehicleInspectionDTO) vehicleObjView;

			vehicleDTO = vehicleInspectionService.getVehicleInformation(dto.getQueueNo(), dto.getApplicationNo(), true);

			if (vehicleDTO != null) {
				fcontext.getExternalContext().getSessionMap().put("UPLOAD_VIEW", "true");
				onRouteChange();
				disableSave = true;
				renderBackOtherInspection = true;
				upload = false;
				disableSkip = true;
				disableCallNext = true;
				renderSave = false;
				printReport = false;
				dataList = vehicleInspectionService.Gridview(vehicleDTO);
				viewMode = true;
				if (dataList.isEmpty()) {
					dataList = vehicleInspectionService.getActiveActionPointData();
				}
			}
		}

		Object vehicleObjEdit = fcontext.getExternalContext().getSessionMap().get("EDIT_OTHER");
		if (vehicleObjEdit != null) {
			VehicleInspectionDTO dto = (VehicleInspectionDTO) vehicleObjEdit;

			vehicleDTO = vehicleInspectionService.getVehicleInformation(dto.getQueueNo(), dto.getApplicationNo(), true);

			if (vehicleDTO != null) {
				fcontext.getExternalContext().getSessionMap().put("UPLOAD_VIEW", "false");
				renderUpdate = true;
				renderSave = false;
				onRouteChange();
				disableSave = false;
				renderBackOtherInspection = true;
				disableSkip = true;
				disableCallNext = true;
				printReport = false;
				upload = false;
				editMode = true;
				dataList = vehicleInspectionService.Gridview(vehicleDTO);
				if (dataList.isEmpty()) {
					dataList = vehicleInspectionService.getActiveActionPointData();
				}
			}
		}

		Object incompleteAppObjView = fcontext.getExternalContext().getSessionMap().get("VIEW_OTHER_INCOMPLETE");
		if (incompleteAppObjView != null) {
			proceedIncompleteApplicationDTO = new ProceedIncompleteApplicationDTO();
			PrintInspectionDTO dto = (PrintInspectionDTO) incompleteAppObjView;

			vehicleDTO = vehicleInspectionService.getVehicleInformation(null, dto.getOwnerApplicationNo(), true);

			if (vehicleDTO != null) {
				onRouteChange();
				disableSave = true;
				renderBackOtherInspection = false;
				upload = true;
				renderBackIncompleteApproval = true;
				disableSkip = true;
				disableCallNext = true;
				renderSave = false;
				printReport = true;
				viewMode = true;
				disableUpdate = true;
				dataList = vehicleInspectionService.Gridview(vehicleDTO);
				if (dataList.isEmpty()) {
					dataList = vehicleInspectionService.getActiveActionPointData();
				}

				boolean enable = incompleteApprovalService.enableProceedBtn(dto.getOwnerApplicationNo());
				if (enable) {
					disableProceedIncompleteApplicationBtn = false;
				} else {
					disableProceedIncompleteApplicationBtn = true;
				}
			}
		}

	}

	public void backToOtherInspection() {

		try {
			FacesContext fcontext = FacesContext.getCurrentInstance();
			fcontext.getExternalContext().getSessionMap().remove("VIEW_OTHER");
			fcontext.getExternalContext().getSessionMap().remove("EDIT_OTHER");
			fcontext.getExternalContext().getSessionMap().remove("UPLOAD_VIEW");
			fcontext.getExternalContext().getSessionMap().remove("VIEW_OTHER_INCOMPLETE");

			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("/InfoNTC/pages/vehicleInspectionSet/otherInspectionEditView.xhtml#!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void backToIncompleteApproval() {
		sessionBackingBean.setCheckIsBackPressed(true);
		try {
			FacesContext fcontext = FacesContext.getCurrentInstance();
			fcontext.getExternalContext().getSessionMap().remove("VIEW_OTHER");
			fcontext.getExternalContext().getSessionMap().remove("EDIT_OTHER");
			fcontext.getExternalContext().getSessionMap().remove("VIEW_OTHER_INCOMPLETE");
			fcontext.getExternalContext().getSessionMap().remove("UPLOAD_VIEW");

			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("/InfoNTC/pages/vehicleInspectionSet/incompleteApproval.xhtml");
		} catch (Exception e) {
			sessionBackingBean.setCheckIsBackPressed(false);
			e.printStackTrace();
		}
	}

	public void handleClose() throws InterruptedException {

		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("/InfoNTC/pages/home/welcomePage.xhtml");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void uploadAction() {
		try {

			FacesContext fcontext = FacesContext.getCurrentInstance();

			if (vehicleDTO.getVehicleNo() != null && !vehicleDTO.getVehicleNo().isEmpty()
					&& !vehicleDTO.getVehicleNo().trim().equalsIgnoreCase("")) {
				fcontext.getExternalContext().getSessionMap().put("VEHICLE_NO", vehicleDTO.getVehicleNo());
			}

			if (vehicleDTO.getApplicationNo() != null && !vehicleDTO.getApplicationNo().isEmpty()
					&& !vehicleDTO.getApplicationNo().equalsIgnoreCase("")) {
				fcontext.getExternalContext().getSessionMap().put("APPLICATION_NO", vehicleDTO.getApplicationNo());
			}

			if (vehicleDTO.getPermitOwner() != null && !vehicleDTO.getPermitOwner().isEmpty()
					&& !vehicleDTO.getPermitOwner().equalsIgnoreCase("")) {
				fcontext.getExternalContext().getSessionMap().put("OWNER_NAME", vehicleDTO.getPermitOwner());
			}

			if (vehicleDTO.getQueueNo() != null && !vehicleDTO.getQueueNo().isEmpty()
					&& !vehicleDTO.getQueueNo().equalsIgnoreCase("")) {

				fcontext.getExternalContext().getSessionMap().put("QUEUE_NO", vehicleDTO.getQueueNo());
			}

			if (vehicleDTO.getInspectionTypeCode() != null && !vehicleDTO.getInspectionTypeCode().isEmpty()
					&& !vehicleDTO.getInspectionTypeCode().equalsIgnoreCase("")) {
				fcontext.getExternalContext().getSessionMap().put("INSPECTION_TYPE",
						vehicleDTO.getInspectionTypeCode());
			}

			fcontext.getExternalContext().getSessionMap().put("UPLOAD_PHOTO_REDIRECT", "true");

			if (editMode || viewMode) {
				fcontext.getExternalContext().getSessionMap().put("OTHER_VEHICLE_INSPECTION", "view_other");

				if (vehicleDTO.getInspectionTypeCode().equals("CI") && editMode) {
					commonService.otherInspectionTasksUpdate(vehicleDTO.getApplicationNo(), "CI103", "O",
							sessionBackingBean.getLoginUser(), sessionBackingBean.getCounterId(),
							vehicleDTO.getVehicleNo());

					vehicleInspectionService.updateQueueMasterStatus(vehicleDTO.getQueueNo(), vehicleDTO, "C", "CI103",
							"O", sessionBackingBean.getLoginUser(), false);

				} else if (vehicleDTO.getInspectionTypeCode().equals("II") && editMode) {
					commonService.otherInspectionTasksUpdate(vehicleDTO.getApplicationNo(), "II103", "O",
							sessionBackingBean.getLoginUser(), sessionBackingBean.getCounterId(),
							vehicleDTO.getVehicleNo());

					vehicleInspectionService.updateQueueMasterStatus(vehicleDTO.getQueueNo(), vehicleDTO, "C", "II103",
							"O", sessionBackingBean.getLoginUser(), false);

				} else if (vehicleDTO.getInspectionTypeCode().equals("SI") && editMode) {
					commonService.otherInspectionTasksUpdate(vehicleDTO.getApplicationNo(), "SI103", "O",
							sessionBackingBean.getLoginUser(), sessionBackingBean.getCounterId(),
							vehicleDTO.getVehicleNo());

					vehicleInspectionService.updateQueueMasterStatus(vehicleDTO.getQueueNo(), vehicleDTO, "C", "SI103",
							"O", sessionBackingBean.getLoginUser(), false);
				}

			} else {

				fcontext.getExternalContext().getSessionMap().put("OTHER_VEHICLE_INSPECTION", "main_other");

				if (vehicleDTO.getInspectionTypeCode().equals("CI")) {
					commonService.otherInspectionTasksUpdate(vehicleDTO.getApplicationNo(), "CI101", "O",
							sessionBackingBean.getLoginUser(), sessionBackingBean.getCounterId(),
							vehicleDTO.getVehicleNo());

					vehicleInspectionService.updateQueueMasterStatus(queueNo, vehicleDTO, "C", "CI101", "O",
							sessionBackingBean.getLoginUser(), false);

				} else if (vehicleDTO.getInspectionTypeCode().equals("II")) {
					commonService.otherInspectionTasksUpdate(vehicleDTO.getApplicationNo(), "II101", "O",
							sessionBackingBean.getLoginUser(), sessionBackingBean.getCounterId(),
							vehicleDTO.getVehicleNo());

					vehicleInspectionService.updateQueueMasterStatus(queueNo, vehicleDTO, "C", "II101", "O",
							sessionBackingBean.getLoginUser(), false);

				} else if (vehicleDTO.getInspectionTypeCode().equals("SI")) {
					commonService.otherInspectionTasksUpdate(vehicleDTO.getApplicationNo(), "SI101", "O",
							sessionBackingBean.getLoginUser(), sessionBackingBean.getCounterId(),
							vehicleDTO.getVehicleNo());

					vehicleInspectionService.updateQueueMasterStatus(queueNo, vehicleDTO, "C", "SI101", "O",
							sessionBackingBean.getLoginUser(), false);
				}
			}

			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("/InfoNTC/pages/vehicleInspectionSet/otherInspectionUploadPhoto.xhtml");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void updateMethod() {

		if (vehicleDTO.getInspectionStatusCode() != null
				&& !vehicleDTO.getInspectionStatusCode().trim().equalsIgnoreCase("")) {

			if (vehicleDTO.getInspecLocationCode() != null
					&& !vehicleDTO.getInspecLocationCode().trim().equalsIgnoreCase("")) {

				boolean isUpdated = vehicleInspectionService.updateOtherInspection(vehicleDTO,
						sessionBackingBean.getLoginUser(), dataList);

				if (isUpdated) {

					upload = false;
					printReport = false;
					disableUpdate = true;

					if (vehicleDTO.getInspectionTypeCode().equals("CI")) {
						commonService.otherInspectionTasksUpdate(vehicleDTO.getApplicationNo(), "CI102",
								vehicleDTO.getInspectionStatusCode(), sessionBackingBean.getLoginUser(),
								sessionBackingBean.getCounterId(), vehicleDTO.getVehicleNo());

						vehicleInspectionService.updateQueueMasterStatus(vehicleDTO.getQueueNo(), vehicleDTO, "C",
								"CI102", vehicleDTO.getInspectionStatusCode(), sessionBackingBean.getLoginUser(),
								false);

					} else if (vehicleDTO.getInspectionTypeCode().equals("II")) {
						commonService.otherInspectionTasksUpdate(vehicleDTO.getApplicationNo(), "II102",
								vehicleDTO.getInspectionStatusCode(), sessionBackingBean.getLoginUser(),
								sessionBackingBean.getCounterId(), vehicleDTO.getVehicleNo());

						vehicleInspectionService.updateQueueMasterStatus(vehicleDTO.getQueueNo(), vehicleDTO, "C",
								"II102", vehicleDTO.getInspectionStatusCode(), sessionBackingBean.getLoginUser(),
								false);

					} else if (vehicleDTO.getInspectionTypeCode().equals("SI")) {
						commonService.otherInspectionTasksUpdate(vehicleDTO.getApplicationNo(), "SI102",
								vehicleDTO.getInspectionStatusCode(), sessionBackingBean.getLoginUser(),
								sessionBackingBean.getCounterId(), vehicleDTO.getVehicleNo());

						vehicleInspectionService.updateQueueMasterStatus(vehicleDTO.getQueueNo(), vehicleDTO, "C",
								"SI102", vehicleDTO.getInspectionStatusCode(), sessionBackingBean.getLoginUser(),
								false);
					}

					successMSG = "Inspection details updated successfully.";
					RequestContext.getCurrentInstance().update("frmsuccess");
					RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

				} else {
					errorMsg = "Error occurred. Connot update inspection details.";
					RequestContext.getCurrentInstance().update("xx");
					RequestContext.getCurrentInstance().execute("PF('fill').show()");
				}

			} else {
				errorMsg = "Please select inspection location.";
				RequestContext.getCurrentInstance().update("xx");
				RequestContext.getCurrentInstance().execute("PF('fill').show()");
			}

		} else {
			errorMsg = "Please select inspection status.";
			RequestContext.getCurrentInstance().update("xx");
			RequestContext.getCurrentInstance().execute("PF('fill').show()");
		}

	}

	public void onRouteChange() {
		boolean check = vehicleInspectionService.routeDetails(vehicleDTO, vehicleDTO.getRouteNo());
		if (vehicleDTO.getRouteNo() != null && !vehicleDTO.getRouteNo().equals("")) {
			routeDTO = adminService.getDetailsbyRouteNo(vehicleDTO.getRouteNo());
			if (routeDTO.getOrigin() != null && !routeDTO.getOrigin().equals("")) {
				permitDTO.setVia(routeDTO.getVia());
				permitDTO.setDestination(routeDTO.getDestination());
				permitDTO.setOrigin(routeDTO.getOrigin());
			}
		} else {
			routeDTO = null;
		}
		routeFlag = false;

	}

	public boolean routeFlagListener() {
		String location1;
		String location2;
		if (routeFlag) {
			location1 = permitDTO.getOrigin();
			location2 = permitDTO.getDestination();
			permitDTO.setOrigin(location2);
			permitDTO.setDestination(location1);
			vehicleDTO.setRouteFlag("Y");
			return false;
		} else {
			location1 = permitDTO.getOrigin();
			location2 = permitDTO.getDestination();
			permitDTO.setOrigin(location2);
			permitDTO.setDestination(location1);
			vehicleDTO.setRouteFlag("N");
			return true;
		}

	}

	public void updateCounterCommit(String queueNo) {

		migratedService.updateStatusOfQueueNumberAfterCallNext(queueNo, "O");
		commonService.updateCounterQueueNo(queueNo, sessionBackingBean.getCounterId());
		migratedService.updateCounterIdOfQueueNumberAfterCallNext(queueNo, sessionBackingBean.getCounterId());

	}

	public void dropdown() {
		vehicleInspectionService = (VehicleInspectionService) SpringApplicationContex
				.getBean("vehicleInspectionService");
		modellist = vehicleInspectionService.modeldropdown("");
		makelist = vehicleInspectionService.makedropdown();
		servicelist = vehicleInspectionService.servicetypedropdown();
		routelist = vehicleInspectionService.routeNodropdown();
		counterList = vehicleInspectionService.counterdropdown();
		locationList = vehicleInspectionService.getInspectionLocationList();

	}

	public void onMakeTypeChange() {
		modellist = vehicleInspectionService.modeldropdown(vehicleDTO.getMakeTypeCode());

	}

	public void onCounterSelect() {
		sessionBackingBean.setCounter(commonDTO.getCounter());
		sessionBackingBean.setCounterId(commonDTO.getCounterId());
		vehicleInspectionService = (VehicleInspectionService) SpringApplicationContex
				.getBean("vehicleInspectionService");
		vehicleInspectionService.counterStatus(commonDTO.getCounterId(), sessionBackingBean.getLoginUser());
		localcheckcounter = false;
		sessionBackingBean.setCounterCheck(false);
		RequestContext context = RequestContext.getCurrentInstance();

		context.execute("PF('dlg2').hide();");
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		try {
			ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void clearAction() {

		vehicleDTO = new VehicleInspectionDTO();
		dataList.clear();
		disableSave = true;
		disableQueueNo = false;
		disableCallNext = false;
		disableSkip = true;
		readonlyQueueNo = false;
		readonlyAppNo = false;
		routeDTO = new RouteDTO();
		permitDTO = new PermitDTO();
		routeFlag = false;
		reinspection = false;
		commonService.updateCounterQueueNo(null, sessionBackingBean.getCounterId());
		renderSave = true;
		renderUpdate = false;
		editMode = false;
		viewMode = false;
	}

	public void manufactureYearRegistrationYearValidator() {

		if (vehicleDTO.getManyear() != null && !vehicleDTO.getManyear().isEmpty()) {
			Pattern ptr = Pattern.compile("^[0-9]{4}$");
			boolean valid = ptr.matcher(vehicleDTO.getManyear()).matches();
			if (valid) {
				DateFormat dateFormat = new SimpleDateFormat("yyyy");
				Date date = new Date();
				String year = dateFormat.format(date);
				int curYear = Integer.parseInt(year);
				int manuYear = Integer.parseInt(vehicleDTO.getManyear());
				if (curYear >= manuYear) {

				} else {
					errorMsg = "Invalid manufacture year.";
					RequestContext.getCurrentInstance().update("frmValidator");
					RequestContext.getCurrentInstance().execute("PF('valid').show()");
					vehicleDTO.setManyear(null);
				}

			}

		}

	}

	public void proceedIncompleteApplicationBtnAction() {
		proceedIncompleteApplicationDTO.setProceedRemark(null);

		RequestContext.getCurrentInstance().update("proceedIncompleteAppConfirm");
		RequestContext.getCurrentInstance().execute("PF('proceedIncompleteAppConfirm').show()");
	}

	public void proceedIncompleteApplication() {
		if (proceedIncompleteApplicationDTO.getProceedRemark() != null
				&& !proceedIncompleteApplicationDTO.getProceedRemark().trim().equals("")) {
			proceedIncompleteApplicationDTO.setApplicationNo(vehicleDTO.getApplicationNo());
			proceedIncompleteApplicationDTO.setLoginUser(sessionBackingBean.getLoginUser());
			proceedIncompleteApplicationDTO.setInspectionType(vehicleDTO.getInspectionTypeCode());
			proceedIncompleteApplicationDTO.setVehicleNo(vehicleDTO.getVehicleNo());

			boolean success = incompleteApprovalService.proceedIncompleteApplication(proceedIncompleteApplicationDTO);
			if (success) {
				successMSG = "Application proceeded successfully.";
				RequestContext.getCurrentInstance().update("proceedSuccess");
				RequestContext.getCurrentInstance().execute("PF('proceedSuccess').show()");
			} else {
				errorMsg = "Error occurred. Application could not proceed.";
				RequestContext.getCurrentInstance().update("queueError");
				RequestContext.getCurrentInstance().execute("PF('queueError').show()");
			}
		} else {
			errorMsg = "Special remarks cannot be empty.";
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public void reloadInit() {
		RequestContext.getCurrentInstance().execute("PF('proceedSuccess').hide()");
		RequestContext.getCurrentInstance().execute("PF('proceedIncompleteAppConfirm').hide()");
		init();
	}

	public VehicleInspectionService getVehicleInspectionService() {
		return vehicleInspectionService;
	}

	public void setVehicleInspectionService(VehicleInspectionService vehicleInspectionService) {
		this.vehicleInspectionService = vehicleInspectionService;
	}

	public VehicleInspectionDTO getVehicleDTO() {
		return vehicleDTO;
	}

	public void setVehicleDTO(VehicleInspectionDTO vehicleDTO) {
		this.vehicleDTO = vehicleDTO;
	}

	public List<VehicleInspectionDTO> getDataList() {
		return dataList;
	}

	public void setDataList(List<VehicleInspectionDTO> dataList) {
		this.dataList = dataList;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public QueueManagementService getQueueManagementService() {
		return queueManagementService;
	}

	public void setQueueManagementService(QueueManagementService queueManagementService) {
		this.queueManagementService = queueManagementService;
	}

	public boolean isSkip() {
		return skip;
	}

	public void setSkip(boolean skip) {
		this.skip = skip;
	}

	public List<VehicleInspectionDTO> getVehiclelist() {
		return vehiclelist;
	}

	public void setVehiclelist(List<VehicleInspectionDTO> vehiclelist) {
		this.vehiclelist = vehiclelist;
	}

	public List<VehicleInspectionDTO> getMakelist() {
		return makelist;
	}

	public void setMakelist(List<VehicleInspectionDTO> makelist) {
		this.makelist = makelist;
	}

	public List<VehicleInspectionDTO> getServicelist() {
		return servicelist;
	}

	public boolean isDisablePermitNo() {
		return disablePermitNo;
	}

	public void setDisablePermitNo(boolean disablePermitNo) {
		this.disablePermitNo = disablePermitNo;
	}

	public boolean isDisableQueueNo() {
		return disableQueueNo;
	}

	public void setDisableQueueNo(boolean disableQueueNo) {
		this.disableQueueNo = disableQueueNo;
	}

	public boolean isDisableAppNo() {
		return disableAppNo;
	}

	public void setDisableAppNo(boolean disableAppNo) {
		this.disableAppNo = disableAppNo;
	}

	public boolean isReadOnlyVehicleNo() {
		return readOnlyVehicleNo;
	}

	public void setReadOnlyVehicleNo(boolean readOnlyVehicleNo) {
		this.readOnlyVehicleNo = readOnlyVehicleNo;
	}

	public void setServicelist(List<VehicleInspectionDTO> servicelist) {
		this.servicelist = servicelist;
	}

	public List<VehicleInspectionDTO> getModellist() {
		return modellist;
	}

	public void setModellist(List<VehicleInspectionDTO> modellist) {
		this.modellist = modellist;
	}

	public List<VehicleInspectionDTO> getRoutelist() {
		return routelist;
	}

	public void setRoutelist(List<VehicleInspectionDTO> routelist) {
		this.routelist = routelist;
	}

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	public CommonDTO getCommonDTO() {
		return commonDTO;
	}

	public void setCommonDTO(CommonDTO commonDTO) {
		this.commonDTO = commonDTO;
	}

	public List<CommonDTO> getCounterList() {
		return counterList;
	}

	public void setCounterList(List<CommonDTO> counterList) {
		this.counterList = counterList;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public boolean isDisableSave() {
		return disableSave;
	}

	public void setDisableSave(boolean disableSave) {
		this.disableSave = disableSave;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public boolean isLocalcheckcounter() {
		return localcheckcounter;
	}

	public void setLocalcheckcounter(boolean localcheckcounter) {
		this.localcheckcounter = localcheckcounter;
	}

	public String getCountererrorMsg() {
		return countererrorMsg;
	}

	public void setCountererrorMsg(String countererrorMsg) {
		this.countererrorMsg = countererrorMsg;
	}

	public String getSuccessMessage() {
		return successmessage;
	}

	public void setSuccessMessage(String successmessage) {
		this.successmessage = successmessage;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public String getSuccessMSG() {
		return successMSG;
	}

	public void setSuccessMSG(String successMSG) {
		this.successMSG = successMSG;
	}

	public boolean isUpload() {
		return upload;
	}

	public String getGeneratedApplicationNO() {
		return generatedApplicationNO;
	}

	public void setGeneratedApplicationNO(String generatedApplicationNO) {
		this.generatedApplicationNO = generatedApplicationNO;
	}

	public String getAlertMSG() {
		return alertMSG;
	}

	public void setAlertMSG(String alertMSG) {
		this.alertMSG = alertMSG;
	}

	public String getSuccessmessage() {
		return successmessage;
	}

	public void setSuccessmessage(String successmessage) {
		this.successmessage = successmessage;
	}

	public boolean isData() {
		return data;
	}

	public void setData(boolean data) {
		this.data = data;
	}

	public String getQueueNo() {
		return queueNo;
	}

	public void setQueueNo(String queueNo) {
		this.queueNo = queueNo;
	}

	public void setUpload(boolean upload) {
		this.upload = upload;
	}

	public boolean isReadonlyAppNo() {
		return readonlyAppNo;
	}

	public void setReadonlyAppNo(boolean readonlyAppNo) {
		this.readonlyAppNo = readonlyAppNo;
	}

	public boolean isReadonlyQueueNo() {
		return readonlyQueueNo;
	}

	public void setReadonlyQueueNo(boolean readonlyQueueNo) {
		this.readonlyQueueNo = readonlyQueueNo;
	}

	public Boolean getRouteFlag() {
		return routeFlag;
	}

	public void setRouteFlag(Boolean routeFlag) {
		this.routeFlag = routeFlag;
	}

	public RouteDTO getRouteDTO() {
		return routeDTO;
	}

	public void setRouteDTO(RouteDTO routeDTO) {
		this.routeDTO = routeDTO;
	}

	public PermitDTO getPermitDTO() {
		return permitDTO;
	}

	public void setPermitDTO(PermitDTO permitDTO) {
		this.permitDTO = permitDTO;
	}

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	public boolean isReinspection() {
		return reinspection;
	}

	public void setReinspection(boolean reinspection) {
		this.reinspection = reinspection;
	}

	public boolean isPrintReport() {
		return printReport;
	}

	public void setPrintReport(boolean printReport) {
		this.printReport = printReport;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getStrTrnCode() {
		return strTrnCode;
	}

	public void setStrTrnCode(String strTrnCode) {
		this.strTrnCode = strTrnCode;
	}

	public String getStrTrnDesc() {
		return strTrnDesc;
	}

	public void setStrTrnDesc(String strTrnDesc) {
		this.strTrnDesc = strTrnDesc;
	}

	public boolean isInspectionForAmendment() {
		return inspectionForAmendment;
	}

	public void setInspectionForAmendment(boolean inspectionForAmendment) {
		this.inspectionForAmendment = inspectionForAmendment;
	}

	public PaymentVoucherService getPaymentVoucherService() {
		return paymentVoucherService;
	}

	public void setPaymentVoucherService(PaymentVoucherService paymentVoucherService) {
		this.paymentVoucherService = paymentVoucherService;
	}

	public String getOldApplicationNo() {
		return oldApplicationNo;
	}

	public void setOldApplicationNo(String oldApplicationNo) {
		this.oldApplicationNo = oldApplicationNo;
	}

	public PermitRenewalsDTO getApplicationHistoryDTO() {
		return applicationHistoryDTO;
	}

	public void setApplicationHistoryDTO(PermitRenewalsDTO applicationHistoryDTO) {
		this.applicationHistoryDTO = applicationHistoryDTO;
	}

	public HistoryService getHistoryService() {
		return historyService;
	}

	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}

	public InspectionActionPointService getInspectionActionPointService() {
		return inspectionActionPointService;
	}

	public void setInspectionActionPointService(InspectionActionPointService inspectionActionPointService) {
		this.inspectionActionPointService = inspectionActionPointService;
	}

	public List<VehicleInspectionDTO> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<VehicleInspectionDTO> locationList) {
		this.locationList = locationList;
	}

	public boolean isRenderBackOtherInspection() {
		return renderBackOtherInspection;
	}

	public void setRenderBackOtherInspection(boolean renderBackOtherInspection) {
		this.renderBackOtherInspection = renderBackOtherInspection;
	}

	public boolean isRenderBackIncompleteApproval() {
		return renderBackIncompleteApproval;
	}

	public void setRenderBackIncompleteApproval(boolean renderBackIncompleteApproval) {
		this.renderBackIncompleteApproval = renderBackIncompleteApproval;
	}

	public boolean isDisableProceedIncompleteApplicationBtn() {
		return disableProceedIncompleteApplicationBtn;
	}

	public void setDisableProceedIncompleteApplicationBtn(boolean disableProceedIncompleteApplicationBtn) {
		this.disableProceedIncompleteApplicationBtn = disableProceedIncompleteApplicationBtn;
	}

	public ProceedIncompleteApplicationDTO getProceedIncompleteApplicationDTO() {
		return proceedIncompleteApplicationDTO;
	}

	public void setProceedIncompleteApplicationDTO(ProceedIncompleteApplicationDTO proceedIncompleteApplicationDTO) {
		this.proceedIncompleteApplicationDTO = proceedIncompleteApplicationDTO;
	}

	public boolean isDisableCallNext() {
		return disableCallNext;
	}

	public void setDisableCallNext(boolean disableCallNext) {
		this.disableCallNext = disableCallNext;
	}

	public boolean isDisableSkip() {
		return disableSkip;
	}

	public void setDisableSkip(boolean disableSkip) {
		this.disableSkip = disableSkip;
	}

	public boolean isRenderSave() {
		return renderSave;
	}

	public void setRenderSave(boolean renderSave) {
		this.renderSave = renderSave;
	}

	public boolean isRenderUpdate() {
		return renderUpdate;
	}

	public void setRenderUpdate(boolean renderUpdate) {
		this.renderUpdate = renderUpdate;
	}

	public boolean isDisableUpdate() {
		return disableUpdate;
	}

	public void setDisableUpdate(boolean disableUpdate) {
		this.disableUpdate = disableUpdate;
	}

	public IncompleteApprovalService getIncompleteApprovalService() {
		return incompleteApprovalService;
	}

	public void setIncompleteApprovalService(IncompleteApprovalService incompleteApprovalService) {
		this.incompleteApprovalService = incompleteApprovalService;
	}

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	public boolean isViewMode() {
		return viewMode;
	}

	public void setViewMode(boolean viewMode) {
		this.viewMode = viewMode;
	}

	public MigratedService getMigratedService() {
		return migratedService;
	}

	public void setMigratedService(MigratedService migratedService) {
		this.migratedService = migratedService;
	}

}