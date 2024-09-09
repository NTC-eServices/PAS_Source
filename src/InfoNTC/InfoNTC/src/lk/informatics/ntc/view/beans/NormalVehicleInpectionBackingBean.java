package lk.informatics.ntc.view.beans;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
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
import lk.informatics.ntc.model.dto.OminiBusDTO;
import lk.informatics.ntc.model.dto.PermitDTO;
import lk.informatics.ntc.model.dto.PermitRenewalsDTO;
import lk.informatics.ntc.model.dto.RouteDTO;
import lk.informatics.ntc.model.dto.VehicleInspectionDTO;
import lk.informatics.ntc.model.service.AdminService;
import lk.informatics.ntc.model.service.AmendmentService;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.HistoryService;
import lk.informatics.ntc.model.service.InspectionActionPointService;
import lk.informatics.ntc.model.service.MigratedService;
import lk.informatics.ntc.model.service.PaymentVoucherService;
import lk.informatics.ntc.model.service.QueueManagementService;
import lk.informatics.ntc.model.service.VehicleInspectionService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "normalVehicleBackingBean")

@ViewScoped
public class NormalVehicleInpectionBackingBean implements Serializable {

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
	private VehicleInspectionDTO tempVehicleDTO;

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
	boolean callnext = false;
	boolean skip = true;
	private boolean localcheckcounter = true;
	private String successmessage, queueNo;
	private boolean data = true, disableSave = false;
	private boolean upload = true, disablePermitNo, disableQueueNo, disableAppNo, readOnlyVehicleNo;
	private AmendmentService amendmentService;
	private boolean reinspection = false;

	private boolean printReport = true;

	private String strTrnCode;
	private String strTrnDesc;

	private boolean inspectionForAmendment;

	private PermitRenewalsDTO applicationHistoryDTO;
	private HistoryService historyService;

	private String inspectionStatus;
	private List<VehicleInspectionDTO> locationList = new ArrayList<>();
	private String inspectionType;
	public String tokenType;
	private boolean permitNew = true;

	
	@PostConstruct
	public void init() {

		vehicleInspectionService = (VehicleInspectionService) SpringApplicationContex
				.getBean("vehicleInspectionService");
		queueManagementService = (QueueManagementService) SpringApplicationContex.getBean("queueManagementService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");
		amendmentService = (AmendmentService) SpringApplicationContex.getBean("amendmentService");
		paymentVoucherService = (PaymentVoucherService) SpringApplicationContex.getBean("paymentVoucherService");
		historyService = (HistoryService) SpringApplicationContex.getBean("historyService");
		inspectionActionPointService = (InspectionActionPointService) SpringApplicationContex
				.getBean("inspectionActionPointService");
		migratedService =(MigratedService) SpringApplicationContex.getBean("migratedService");
		locationList = vehicleInspectionService.getInspectionLocationList();

		localcheckcounter = sessionBackingBean.isCounterCheck();
		vehicleDTO = new VehicleInspectionDTO();
		permitDTO = new PermitDTO();
		routeDTO = new RouteDTO();
		dropdown();
		disableSave = true;
		disablePermitNo = false;
		disableQueueNo = false;
		disableAppNo = false;
		routeFlag = false;
		permitNew=false;

		strTrnCode = null;
		strTrnDesc = null;

		printReport = true;

		inspectionForAmendment = false;

		tempVehicleDTO = new VehicleInspectionDTO();
		applicationHistoryDTO = new PermitRenewalsDTO();
		String tempQueueNo = null;

		FacesContext fcontext = FacesContext.getCurrentInstance();

		String uploadPhotoBtnStr = null;
		Object objCallerQueueNo2 = fcontext.getExternalContext().getSessionMap().get("UPLOAD_PHOTO_REDIRECT_NORMAL");
		if (objCallerQueueNo2 != null) {
			uploadPhotoBtnStr = (String) objCallerQueueNo2;

			if (uploadPhotoBtnStr != null && !uploadPhotoBtnStr.isEmpty()
					&& !uploadPhotoBtnStr.trim().equalsIgnoreCase("")) {
				if (uploadPhotoBtnStr.equalsIgnoreCase("true")) {
					upload = false;
				}
			}

			Object showNormalInspectionData = fcontext.getExternalContext().getSessionMap()
					.get("NORMAL_INSPECTION_DATA_SHOW");
			if (showNormalInspectionData != null) {
				if (showNormalInspectionData.equals("true")) {

					Object objCallerQueueNo = fcontext.getExternalContext().getSessionMap().get("QUEUE_NO");
					if (objCallerQueueNo != null) {
						tempQueueNo = (String) objCallerQueueNo;
						

						fcontext.getExternalContext().getSessionMap().remove("NORMAL_INSPECTION_DATA_SHOW");
					}

				}
			}
		}

		fcontext.getExternalContext().getSessionMap().put("QUEUE_NO", null);

		if (tempQueueNo != null && !tempQueueNo.isEmpty() && !tempQueueNo.trim().equalsIgnoreCase("")) {
			vehicleDTO.setQueueNo(tempQueueNo);

			vehicleDTO = vehicleInspectionService.searchOnBackBtnActionNew(vehicleDTO.getQueueNo());
			inspectionStatus = vehicleDTO.getInspectioncomplIncomplStatus();
			if (vehicleDTO.getInspectionTypeCode().equals("AI")) {
				inspectionType = "Amendment";
				inspectionForAmendment = true;
				
			} else if (vehicleDTO.getInspectionTypeCode().equals("PI")) {
				inspectionType = "Renewal";

			}
			else if(vehicleDTO.getInspectionTypeCode().equals("NI")) {
				inspectionType = "New Permit";
			}
			dataList = vehicleInspectionService.Gridview(vehicleDTO);
			if (dataList.isEmpty()) {
				dataList = vehicleInspectionService.getActiveActionPointData();
			}
			printReport = false;

			if (vehicleDTO.getRouteFlag() != null && !vehicleDTO.getRouteFlag().trim().equals("")
					&& vehicleDTO.getRouteFlag().equalsIgnoreCase("Y")) {

				routeFlag = true;

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
				routeFlagListener();

			} else if (vehicleDTO.getRouteFlag() != null && !vehicleDTO.getRouteFlag().trim().equals("")
					&& vehicleDTO.getRouteFlag().equalsIgnoreCase("N")) {

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
			}

			else {

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
			}

			RequestContext.getCurrentInstance().update("field");

			dataList = vehicleInspectionService.Gridview(vehicleDTO);
			vehicleDTO.setCalender1(sessionBackingBean.getCalender1());
			vehicleDTO.setCalender2(sessionBackingBean.getCalender2());
			vehicleDTO.setFinalRemark(sessionBackingBean.getFinalRemark());
		}

		fcontext.getExternalContext().getSessionMap().put("UPLOAD_PHOTO_REDIRECT_NORMAL", "false");
		
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

			if (tempVehicleDTO.getInspectionTypeCode() != null && !tempVehicleDTO.getInspectionTypeCode().isEmpty()
					&& !tempVehicleDTO.getInspectionTypeCode().trim().equalsIgnoreCase("")) {
				fcontext.getExternalContext().getSessionMap().put("INSPECTION_TYPE",
						tempVehicleDTO.getInspectionTypeCode());
			} else if (vehicleDTO.getInspectionTypeCode() != null && !vehicleDTO.getInspectionTypeCode().isEmpty()
					&& !vehicleDTO.getInspectionTypeCode().trim().equalsIgnoreCase("")) {
				fcontext.getExternalContext().getSessionMap().put("INSPECTION_TYPE",
						vehicleDTO.getInspectionTypeCode());
			}

			if (tempVehicleDTO.getInspectioncomplIncomplStatus() != null
					&& !tempVehicleDTO.getInspectioncomplIncomplStatus().isEmpty()
					&& !tempVehicleDTO.getInspectioncomplIncomplStatus().trim().equalsIgnoreCase("")) {
				fcontext.getExternalContext().getSessionMap().put("INSPECTION_STATUS",
						tempVehicleDTO.getInspectioncomplIncomplStatus());
			} else if (vehicleDTO.getInspectioncomplIncomplStatus() != null
					&& !vehicleDTO.getInspectioncomplIncomplStatus().isEmpty()
					&& !vehicleDTO.getInspectioncomplIncomplStatus().trim().equalsIgnoreCase("")) {
				fcontext.getExternalContext().getSessionMap().put("INSPECTION_STATUS",
						vehicleDTO.getInspectioncomplIncomplStatus());
			}

			if (tempVehicleDTO.getInspecLocationCode() != null && !tempVehicleDTO.getInspecLocationCode().isEmpty()
					&& !tempVehicleDTO.getInspecLocationCode().trim().equalsIgnoreCase("")) {
				fcontext.getExternalContext().getSessionMap().put("LOCATION", tempVehicleDTO.getInspecLocationCode());
			} else if (vehicleDTO.getInspecLocationCode() != null && !vehicleDTO.getInspecLocationCode().isEmpty()
					&& !vehicleDTO.getInspecLocationCode().trim().equalsIgnoreCase("")) {
				fcontext.getExternalContext().getSessionMap().put("LOCATION", vehicleDTO.getInspecLocationCode());
			}

			if (tempVehicleDTO.getVehicleNo() != null && !tempVehicleDTO.getVehicleNo().isEmpty()
					&& !tempVehicleDTO.getVehicleNo().trim().equalsIgnoreCase("")) {
				fcontext.getExternalContext().getSessionMap().put("VEHICLE_NO", tempVehicleDTO.getVehicleNo());
			} else if (vehicleDTO.getVehicleNo() != null && !vehicleDTO.getVehicleNo().isEmpty()
					&& !vehicleDTO.getVehicleNo().trim().equalsIgnoreCase("")) {
				fcontext.getExternalContext().getSessionMap().put("VEHICLE_NO", vehicleDTO.getVehicleNo());
			}

			if (tempVehicleDTO.getApplicationNo() != null && !tempVehicleDTO.getApplicationNo().isEmpty()
					&& !tempVehicleDTO.getApplicationNo().equalsIgnoreCase("")) {
				fcontext.getExternalContext().getSessionMap().put("APPLICATION_NO", tempVehicleDTO.getApplicationNo());
			} else if (vehicleDTO.getApplicationNo() != null && !vehicleDTO.getApplicationNo().isEmpty()
					&& !vehicleDTO.getApplicationNo().equalsIgnoreCase("")) {
				fcontext.getExternalContext().getSessionMap().put("APPLICATION_NO", vehicleDTO.getApplicationNo());
			}

			if (tempVehicleDTO.getPermitOwner() != null && !tempVehicleDTO.getPermitOwner().isEmpty()
					&& !tempVehicleDTO.getPermitOwner().equalsIgnoreCase("")) {
				fcontext.getExternalContext().getSessionMap().put("OWNER_NAME", tempVehicleDTO.getPermitOwner());
			} else if (vehicleDTO.getPermitOwner() != null && !vehicleDTO.getPermitOwner().isEmpty()
					&& !vehicleDTO.getPermitOwner().equalsIgnoreCase("")) {
				fcontext.getExternalContext().getSessionMap().put("OWNER_NAME", vehicleDTO.getPermitOwner());
			}

			if (tempVehicleDTO.getQueueNo() != null && !tempVehicleDTO.getQueueNo().isEmpty()
					&& !tempVehicleDTO.getQueueNo().equalsIgnoreCase("")) {
				fcontext.getExternalContext().getSessionMap().put("QUEUE_NO", tempVehicleDTO.getQueueNo());
			} else if (vehicleDTO.getQueueNo() != null && !vehicleDTO.getQueueNo().isEmpty()
					&& !vehicleDTO.getQueueNo().equalsIgnoreCase("")) {
				fcontext.getExternalContext().getSessionMap().put("QUEUE_NO", vehicleDTO.getQueueNo());
			}

			fcontext.getExternalContext().getSessionMap().put("VEHICLE_NORMAL_INSPECTION", "main");
			fcontext.getExternalContext().getSessionMap().put("UPLOAD_PHOTO_REDIRECT_NORMAL", "true");

			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("/InfoNTC/pages/vehicleInspectionSet/uploadPhotosForNormalInspection.xhtml");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void nextAction() {
		String appStatus, applicationStatus;
		List<String> latestApplicationNoList = new ArrayList<>();

		String latestAppNoForBusChange = null;
		String latestApplicationNo = null;
		String activeApplication = null;
		String amendmendType = null;
		boolean checkInspect = true;
		boolean checkInspectStatus = true;
		String queueBusNo = null;

		queueNo = queueManagementService.callNextQueueNumberAction("02,17", "01");
		if (queueNo != null) {

			callnext = true;
			skip = false;
			String vehicleNo = null;
			boolean backlogTest = false;
			backlogTest = vehicleInspectionService.applicationType(queueNo);

			routeDTO = new RouteDTO();
			permitDTO = new PermitDTO();
			routeFlag = false;

			tokenType = vehicleInspectionService.getTokenType(queueNo);
			//Check whether it's new permit or not
			boolean newpermitHv = vehicleInspectionService.isNewPermit(queueNo);
			if (newpermitHv == true) {
				inspectionType = "New Permit";
				inspectionForAmendment = false;
				permitNew = true;
				tokenType = "NI";
				vehicleDTO.setTenderPermit("Y");
			} else {
				if (tokenType.equals("AI")) {
					inspectionType = "Amendment";
					inspectionForAmendment = true;
				} else if (tokenType.equalsIgnoreCase("PI")) {
					inspectionType = "Renewal";
					inspectionForAmendment = false;
				}
				permitNew = false;
			}

			VehicleInspectionDTO queueDTO = vehicleInspectionService.getCheckAmmendments(queueNo);
			if (tokenType.equals("AI")) {
				amendmendType = vehicleInspectionService.getAmendmentType(queueDTO.getApplicationNo());
			}

			if (tokenType.equals("AI") && amendmendType.equals("13")) {

				latestApplicationNo = vehicleInspectionService.getLatestApplicationNumber(queueDTO.getVehicleNo());
				activeApplication = vehicleInspectionService
						.getActiveApplicationNumberByPermitNo(queueDTO.getPermitNo());
				latestApplicationNoList = vehicleInspectionService
						.getLatestApplicationNumberList(queueDTO.getVehicleNo());

			} else {

				if (tokenType.equals("AI")) {
					latestApplicationNo = vehicleInspectionService
							.getLatestApplicationNumberForAmendmend(queueDTO.getVehicleNo());
					activeApplication = vehicleInspectionService.getActiveApplicationNumber(queueDTO.getVehicleNo());
					latestApplicationNoList = vehicleInspectionService
							.getLatestApplicationNumberList(queueDTO.getVehicleNo());
				} else {
					latestApplicationNo = vehicleInspectionService.getLatestApplicationNumber(queueDTO.getVehicleNo());
					activeApplication = vehicleInspectionService.getActiveApplicationNumber(queueDTO.getVehicleNo());
					latestApplicationNoList = vehicleInspectionService
							.getLatestApplicationNumberList(queueDTO.getVehicleNo());
					
					// check ongoing renewals exists
					boolean ongoingExists = vehicleInspectionService.isOngoingRenewalsExists(activeApplication);
					
					if (ongoingExists) {
						setErrorMsg("Invalid action. Ongoing renewal in process. Token No.: "+queueDTO.getQueueNo());
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
						vehicleDTO = new VehicleInspectionDTO();
						return;
					}
				}

			}
			queueBusNo = queueDTO.getVehicleNo();

			VehicleInspectionDTO taskCodeDTO = vehicleInspectionService.getTaskDet(activeApplication,
					queueDTO.getVehicleNo());
			VehicleInspectionDTO disabledTaskCodeDTO = new VehicleInspectionDTO();

			if (tokenType.equals("AI")) {
				disabledTaskCodeDTO = vehicleInspectionService.getTaskDetForTodayNewForAmendmed(queueDTO.getVehicleNo(),
						queueDTO.getApplicationNo());
			} else {
				disabledTaskCodeDTO = vehicleInspectionService.getTaskDetForTodayNew(queueDTO.getVehicleNo());
			}

			if (taskCodeDTO.getTaskDetCode() != null) {
				if (taskCodeDTO.getTaskDetCode().equals("AM106") && taskCodeDTO.getTaskDetStatus().equals("C")
						|| inspectionForAmendment) {
					inspectionForAmendment = true;
				} else {
					inspectionForAmendment = false;

					/**
					 * disable skip inspection/save/ upload photo buttons for already skipped and
					 * inspection done application by tharushi.e
					 **/
					if (disabledTaskCodeDTO.getTaskDetCode() != null
							&& disabledTaskCodeDTO.getTaskDetStatus() != null) {
						if (disabledTaskCodeDTO.getTaskDetCode().equals("PM101")
								|| disabledTaskCodeDTO.getTaskDetCode().equals("PM100")
										&& disabledTaskCodeDTO.getTaskDetStatus().equals("C")
								|| disabledTaskCodeDTO.getTaskDetCode().equals("PR200")
								|| disabledTaskCodeDTO.getTaskDetCode().equals("PM201")
								|| disabledTaskCodeDTO.getTaskDetCode().equals("PM300")
								|| disabledTaskCodeDTO.getTaskDetCode().equals("PM301")
								|| disabledTaskCodeDTO.getTaskDetCode().equals("PM302")
								|| disabledTaskCodeDTO.getTaskDetCode().equals("PM400")) {
							disableSave = true;
							upload = true;

						}
					}
					/** end **/
				}

			} else {

				inspectionForAmendment = false;

				/**
				 * disable skip inspection/save/ upload photo buttons for already skipped and
				 * inspection done application by tharushi.e
				 **/
				if (disabledTaskCodeDTO.getTaskDetCode() != null && disabledTaskCodeDTO.getTaskDetStatus() != null) {
					if (disabledTaskCodeDTO.getTaskDetCode().equals("PM101")
							|| disabledTaskCodeDTO.getTaskDetCode().equals("PM100")
									&& disabledTaskCodeDTO.getTaskDetStatus().equals("C")
							|| disabledTaskCodeDTO.getTaskDetCode().equals("PR200")
							|| disabledTaskCodeDTO.getTaskDetCode().equals("PM201")
							|| disabledTaskCodeDTO.getTaskDetCode().equals("PM300")
							|| disabledTaskCodeDTO.getTaskDetCode().equals("PM301")
							|| disabledTaskCodeDTO.getTaskDetCode().equals("PM302")
							|| disabledTaskCodeDTO.getTaskDetCode().equals("PM400")) {
						disableSave = true;
						upload = true;

					}
				}
				/** end **/

			}

			if (tokenType.equals("AI")) {
				vehicleDTO = vehicleInspectionService.searchForNormalInspection(queueNo, inspectionForAmendment,
						latestApplicationNo);
			} else {
				vehicleDTO = vehicleInspectionService.searchForNormalInspection(queueNo, inspectionForAmendment,
						activeApplication);
			}

			transactionTypeHandler();
			oldApplicationNo = vehicleDTO.getApplicationNo();
			vehicleDTO.setVehicleNo(queueDTO.getVehicleNo());
			inspectionStatus = vehicleDTO.getInspectioncomplIncomplStatus();

			queueNo = vehicleDTO.getQueueNo();
			if ((vehicleDTO.getInspectionStatus() != null && vehicleDTO.getInspectionStatus().equals("P"))) {
				reinspection = true;
			} else {
				reinspection = false;
			}

			if (vehicleDTO.getInspectionStatus() != null && !vehicleDTO.getInspectionStatus().isEmpty()
					&& vehicleDTO.getInspectionStatus().equalsIgnoreCase("P")) {

				reinspection = true;

			}

			generatedApplicationNO = vehicleDTO.getApplicationNo();

			applicationStatus = commonService.applicationStatus(activeApplication);

			if (applicationStatus != null && !applicationStatus.isEmpty() && applicationStatus.equalsIgnoreCase("E")) {

				errorMsg = "Expired permit.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				return;

			}

			appStatus = vehicleInspectionService.applicationTaskCodeStatusPM100(activeApplication);
			appStatus = vehicleInspectionService.applicationTaskCodeStatusPM101(activeApplication);

			if (appStatus != null && !appStatus.isEmpty()) {

				/**
				 * checked PM400 is completed in task table,for let to re inspect application
				 * which are complete inspection by today by tharushi.e (not used)
				 **/

				String processComplteByToday = null;
				processComplteByToday = vehicleInspectionService.applicationTaskCodeStatusPM400(latestApplicationNo);

			}

			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date date = new Date();

			if (vehicleDTO.getDate2() != null && !vehicleDTO.getDate2().isEmpty()) {

				Date inspectionDate2 = null;
				try {
					inspectionDate2 = dateFormat.parse(vehicleDTO.getDate2());
				} catch (ParseException e) {

					e.printStackTrace();
				}

				if (inspectionDate2.after(date)) {
					vehicleInspectionService.permanentSkip(vehicleDTO.getQueueNo());
					errorMsg = "Queue No. " + vehicleDTO.getQueueNo() + "\n" + " re-inspection date is "
							+ vehicleDTO.getDate2();
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				}

			}

			if (vehicleDTO.getDate1() != null && !vehicleDTO.getDate1().isEmpty()) {

				Date inspectionDate1 = null;
				try {
					inspectionDate1 = dateFormat.parse(vehicleDTO.getDate1());
				} catch (ParseException e) {

					e.printStackTrace();
				}

				if (inspectionDate1.after(date)) {
					vehicleInspectionService.permanentSkip(vehicleDTO.getQueueNo());
					errorMsg = "Queue No. " + vehicleDTO.getQueueNo() + "\n" + " re-inspection date is "
							+ vehicleDTO.getDate1();
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				}

			}

			onRouteChange();

			if (vehicleDTO.getRouteFlag() != null && !vehicleDTO.getRouteFlag().trim().equals("")
					&& vehicleDTO.getRouteFlag().equalsIgnoreCase("Y")) {
				routeFlag = true;

				routeFlagListener();

			} else if (vehicleDTO.getRouteFlag() != null && !vehicleDTO.getRouteFlag().trim().equals("")
					&& vehicleDTO.getRouteFlag().equalsIgnoreCase("N")) {

			}

			// get inspection status for latest appliation number
			boolean haveStatusData = vehicleInspectionService.checkStausForAppNoList(queueBusNo);

			inspectionStatus = vehicleInspectionService.getLatestInspectionStatus(latestApplicationNo);
			if ((inspectionStatus == null || inspectionStatus.isEmpty()) && haveStatusData) {
				while (checkInspectStatus) {
					for (String appNo : latestApplicationNoList) {
						inspectionStatus = vehicleInspectionService.getLatestInspectionStatus(appNo);
						if (inspectionStatus != null && !inspectionStatus.isEmpty()) {
							checkInspectStatus = false;
							break;
						}
					}
				}
				;
			}
			dataList = vehicleInspectionService.GridviewNew(latestApplicationNo);
			boolean haveInspectData = vehicleInspectionService.checkInspectionDataForVehiNo(queueBusNo);
			if ((dataList == null || dataList.isEmpty()) && haveInspectData) {

				while (checkInspect) {
					for (String appNo : latestApplicationNoList) {
						dataList = vehicleInspectionService.GridviewNew(appNo);
						if (dataList != null && !dataList.isEmpty()) {
							checkInspect = false;
							break;
						}
					}
				}
				;

			}

			disableSave = false;

			migratedService.updateStatusOfQueueNumberAfterCallNext(queueNo, "O");

			commonService.updateCounterQueueNo(queueNo, sessionBackingBean.getCounterId());
			migratedService.updateCounterIdOfQueueNumberAfterCallNext(queueNo, sessionBackingBean.getCounterId());

			if (vehicleDTO.getVehicleNo() != null) {

				/* New permit renewal vehicle inspection */

				vehicleInspectionService.checkVehicleNo(queueNo);
				vehicleNo = vehicleInspectionService.getVehicleNo(queueNo);

				vehicleDTO.setVehicleNo(vehicleNo);
				vehicleDTO.setQueueNo(queueNo);

				if (dataList.isEmpty()) {
					dataList = vehicleInspectionService.getActiveActionPointData();

				}

				setAlertMSG("Please fill and save the form to continue.");
				RequestContext.getCurrentInstance().execute("PF('alertMSG').show()");

				disableSave = false;
				disablePermitNo = true;
				disableQueueNo = true;
				disableAppNo = true;

			} else {
				/* New vehicle inspection */

				vehicleNo = vehicleInspectionService.getVehicleNo(queueNo);

				vehicleDTO.setVehicleNo(vehicleNo);
				vehicleDTO.setQueueNo(queueNo);

				if (dataList.isEmpty()) {
					dataList = vehicleInspectionService.getActiveActionPointData();
				}

				setAlertMSG("Please fill and save the form to continue.");
				RequestContext.getCurrentInstance().execute("PF('alertMSG').show()");

				disableSave = false;
				disablePermitNo = true;
				disableQueueNo = true;
				disableAppNo = true;

			}

		} else {
			setErrorMsg("Queue numbers not found.");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			skip = true;
		}

	}

	public void skipAction() {

		migratedService.updateSkipQueueNumberStatus(queueNo);
		callnext = false;
		skip = true;
		commonService.updateCounterNo(sessionBackingBean.getCounterId());
		disableSave = true;
		setSuccessMSG("Skipped Successfully.");
		RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
		vehicleDTO = new VehicleInspectionDTO();
		dataList.clear();

		routeDTO = new RouteDTO();
		permitDTO = new PermitDTO();
		routeFlag = false;
		reinspection = false;

	}

	public void searchAction() {

		String latestApplicationNo = null;
		String latestAppNoForBusChange = null;
		String activeApplication = null;
		List<String> latestApplicationNoList = new ArrayList<>();
		boolean tokenStatus = true;
		boolean validTokenForToday = true;
		String appStatus, applicationStatus;
		String amendmendType = null;
		boolean checkInspect = true;
		boolean checkInspectStatus = true;
		String queueBusNo = null;

		if ((vehicleDTO.getQueueNo() == null || vehicleDTO.getQueueNo().equals(""))) {

			setErrorMsg("Please enter queue no. to search.");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			vehicleDTO = new VehicleInspectionDTO();
		} else {
			// check entered token number is inspection complete or not for today
			tokenStatus = vehicleInspectionService.taskCodeStatusINQueuePM100(vehicleDTO.getQueueNo());

			// check entered token number is correct one for today
			validTokenForToday = vehicleInspectionService.validTokenInQueue(vehicleDTO.getQueueNo());
			if (!validTokenForToday) {

				setErrorMsg("Invalid token. Please check again.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				vehicleDTO = new VehicleInspectionDTO();
			} else {

				if (!tokenStatus) {

					setErrorMsg("Inspection was completed for this token number.");
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					vehicleDTO = new VehicleInspectionDTO();
				} else {
					/*** check entered token is in normal inspection type ***/
					tokenType = vehicleInspectionService.getTokenType(vehicleDTO.getQueueNo());
					
					
					//Check whether it's new permit or not
					boolean newpermitHv = vehicleInspectionService.isNewPermit(queueNo);
					if (newpermitHv == true)
					{
						inspectionType = "New Permit";
						inspectionForAmendment = false;
						permitNew=true;
						tokenType = "NI";
						vehicleDTO.setTenderPermit("Y");
					}
					else
					{
						if (tokenType.equals("AI")) {
							inspectionType = "Amendment";
							inspectionForAmendment = true;
						} else if (tokenType.equalsIgnoreCase("PI")) {
							inspectionType = "Renewal";
							inspectionForAmendment = false;
						}
						permitNew=false;
					}
					
					if ((tokenType.equals("PI") || tokenType.equals("AI"))) {

						VehicleInspectionDTO queueDTO = vehicleInspectionService
								.getCheckAmmendments(vehicleDTO.getQueueNo());

						if (tokenType.equals("AI")) {
							amendmendType = vehicleInspectionService.getAmendmentType(queueDTO.getApplicationNo());
						}
						VehicleInspectionDTO taskCodeDTO = new VehicleInspectionDTO();

						if (tokenType.equals("AI") && amendmendType.equals("13")) {

							latestApplicationNo = vehicleInspectionService
									.getLatestApplicationNumber(queueDTO.getVehicleNo());
							activeApplication = vehicleInspectionService
									.getActiveApplicationNumberByPermitNo(queueDTO.getPermitNo());
							latestApplicationNoList = vehicleInspectionService
									.getLatestApplicationNumberList(queueDTO.getVehicleNo());

						} else {
							if (tokenType.equals("AI")) {
								latestApplicationNo = vehicleInspectionService
										.getLatestApplicationNumberForAmendmend(queueDTO.getVehicleNo());
								activeApplication = vehicleInspectionService
										.getActiveApplicationNumber(queueDTO.getVehicleNo());
								latestApplicationNoList = vehicleInspectionService
										.getLatestApplicationNumberList(queueDTO.getVehicleNo());
							} else {
								latestApplicationNo = vehicleInspectionService
										.getLatestApplicationNumber(queueDTO.getVehicleNo());
								activeApplication = vehicleInspectionService
										.getActiveApplicationNumber(queueDTO.getVehicleNo());
								latestApplicationNoList = vehicleInspectionService
										.getLatestApplicationNumberList(queueDTO.getVehicleNo());
								
								// check ongoing renewals exists
								boolean ongoingExists = vehicleInspectionService.isOngoingRenewalsExists(activeApplication);
								
								if (ongoingExists) {
									setErrorMsg("Invalid action. Ongoing renewal in process. Token No.: "+queueDTO.getQueueNo());
									RequestContext.getCurrentInstance().update("frmrequiredField");
									RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
									vehicleDTO = new VehicleInspectionDTO();
									return;
								}
							}
						}

						queueBusNo = queueDTO.getVehicleNo();
						taskCodeDTO = vehicleInspectionService.getTaskDet(activeApplication, queueDTO.getVehicleNo());

						VehicleInspectionDTO disabledTaskCodeDTO = new VehicleInspectionDTO();
						if (tokenType.equals("AI")) {
							disabledTaskCodeDTO = vehicleInspectionService.getTaskDetForTodayNewForAmendmed(
									queueDTO.getVehicleNo(), queueDTO.getApplicationNo());
						} else {
							disabledTaskCodeDTO = vehicleInspectionService
									.getTaskDetForTodayNew(queueDTO.getVehicleNo());
						}
						if (taskCodeDTO.getTaskDetCode() != null) {
							if (taskCodeDTO.getTaskDetCode().equals("AM106")
									&& taskCodeDTO.getTaskDetStatus().equals("C") || inspectionForAmendment) {
								inspectionForAmendment = true;
							} else {
								inspectionForAmendment = false;

								/**
								 * disable skip inspection/save/ upload photo buttons for already skipped and
								 * inspection done application by tharushi.e
								 **/
								if (disabledTaskCodeDTO.getTaskDetCode() != null
										&& disabledTaskCodeDTO.getTaskDetStatus() != null) {
									if (disabledTaskCodeDTO.getTaskDetCode().equals("PM101")
											|| disabledTaskCodeDTO.getTaskDetCode().equals("PM100")
													&& disabledTaskCodeDTO.getTaskDetStatus().equals("C")
											|| disabledTaskCodeDTO.getTaskDetCode().equals("PR200")
											|| disabledTaskCodeDTO.getTaskDetCode().equals("PM201")
											|| disabledTaskCodeDTO.getTaskDetCode().equals("PM300")
											|| disabledTaskCodeDTO.getTaskDetCode().equals("PM301")
											|| disabledTaskCodeDTO.getTaskDetCode().equals("PM302")
											|| disabledTaskCodeDTO.getTaskDetCode().equals("PM400")) {
										disableSave = true;
										upload = true;

									}
								}
								/** end **/
							}

						} else {

							inspectionForAmendment = false;

							/**
							 * disable skip inspection/save/ upload photo buttons for already skipped and
							 * inspection done application by tharushi.e
							 **/
							if (disabledTaskCodeDTO.getTaskDetCode() != null
									&& disabledTaskCodeDTO.getTaskDetStatus() != null) {
								if (disabledTaskCodeDTO.getTaskDetCode().equals("PM101")
										|| disabledTaskCodeDTO.getTaskDetCode().equals("PM100")
												&& disabledTaskCodeDTO.getTaskDetStatus().equals("C")
										|| disabledTaskCodeDTO.getTaskDetCode().equals("PR200")
										|| disabledTaskCodeDTO.getTaskDetCode().equals("PM201")
										|| disabledTaskCodeDTO.getTaskDetCode().equals("PM300")
										|| disabledTaskCodeDTO.getTaskDetCode().equals("PM301")
										|| disabledTaskCodeDTO.getTaskDetCode().equals("PM302")
										|| disabledTaskCodeDTO.getTaskDetCode().equals("PM400")) {
									disableSave = true;
									upload = true;

								}
							}
							/** end **/

						}
						if (tokenType.equals("AI")) {
							vehicleDTO = vehicleInspectionService.searchForNormalInspection(vehicleDTO.getQueueNo(),
									inspectionForAmendment, latestApplicationNo);

						}

						else {
							vehicleDTO = vehicleInspectionService.searchForNormalInspection(vehicleDTO.getQueueNo(),
									inspectionForAmendment, activeApplication);
						}

						vehicleDTO.setVehicleNo(queueDTO.getVehicleNo());

						inspectionStatus = vehicleDTO.getInspectioncomplIncomplStatus();

						queueNo = vehicleDTO.getQueueNo();

						if ((vehicleDTO.getInspectionStatus() != null
								&& vehicleDTO.getInspectionStatus().equals("P"))) {
							reinspection = true;
						} else {
							reinspection = false;
						}

						if (vehicleDTO.getApplicationNo() == null) {

							vehicleDTO = vehicleInspectionService.getDataForNormalReinspection(vehicleDTO.getQueueNo());

							if (vehicleDTO.getInspectionStatus() != null && !vehicleDTO.getInspectionStatus().isEmpty()
									&& vehicleDTO.getInspectionStatus().equalsIgnoreCase("P")) {

								reinspection = true;

							} else {
								reinspection = false;
							}
						}

						generatedApplicationNO = vehicleDTO.getApplicationNo();

						applicationStatus = commonService.applicationStatus(activeApplication);

						if (applicationStatus != null && !applicationStatus.isEmpty()
								&& applicationStatus.equalsIgnoreCase("E")) {

							errorMsg = "Expired permit.";
							RequestContext.getCurrentInstance().update("frmrequiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
							return;

						}

						appStatus = vehicleInspectionService.applicationTaskCodeStatusPM101(activeApplication);
						if (appStatus != null && !appStatus.isEmpty()) {
							/**
							 * checked PM400 is completed in task table,for let to re inspect application
							 * which are complete inspection by today by tharushi.e
							 **/

							String processComplteByToday = null;

							processComplteByToday = vehicleInspectionService
									.applicationTaskCodeStatusPM400(latestApplicationNo);

						}

						DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
						Date date = new Date();

						if (vehicleDTO.getDate2() != null && !vehicleDTO.getDate2().isEmpty()) {

							Date inspectionDate2 = null;
							try {
								inspectionDate2 = dateFormat.parse(vehicleDTO.getDate2());
							} catch (ParseException e) {

								e.printStackTrace();
							}

							if (inspectionDate2.after(date)) {
								vehicleInspectionService.permanentSkip(vehicleDTO.getQueueNo());
								errorMsg = "Queue No. " + vehicleDTO.getQueueNo() + "\n" + " re-inspection date is "
										+ vehicleDTO.getDate2();
								RequestContext.getCurrentInstance().update("frmrequiredField");
								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

							}

						}

						if (vehicleDTO.getDate1() != null && !vehicleDTO.getDate1().isEmpty()) {

							Date inspectionDate1 = null;
							try {
								inspectionDate1 = dateFormat.parse(vehicleDTO.getDate1());
							} catch (ParseException e) {

								e.printStackTrace();
							}

							if (inspectionDate1.after(date)) {

								vehicleInspectionService.permanentSkip(vehicleDTO.getQueueNo());
								errorMsg = "Queue No. " + vehicleDTO.getQueueNo() + "\n" + " re-inspection date is "
										+ vehicleDTO.getDate1();
								RequestContext.getCurrentInstance().update("frmrequiredField");
								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

							}

						}

						onRouteChange();
						if (vehicleDTO.getRouteFlag() != null && !vehicleDTO.getRouteFlag().trim().equals("")
								&& vehicleDTO.getRouteFlag().equalsIgnoreCase("Y")) {
							routeFlag = true;
							routeFlagListener();

						} else if (vehicleDTO.getRouteFlag() != null && !vehicleDTO.getRouteFlag().trim().equals("")
								&& vehicleDTO.getRouteFlag().equalsIgnoreCase("N")) {

						}

						else {

						}

						dataList = vehicleInspectionService.GridviewNew(latestApplicationNo);

						// get inspection status for latest appliation number
						inspectionStatus = vehicleInspectionService.getLatestInspectionStatus(latestApplicationNo);
						// check is there statusfor latestapplication number list
						boolean haveStatusData = vehicleInspectionService.checkStausForAppNoList(queueBusNo);

						if ((inspectionStatus == null || inspectionStatus.isEmpty())
								&&  haveStatusData) {
							while (checkInspectStatus) {
								for (String appNo : latestApplicationNoList) {
									inspectionStatus = vehicleInspectionService.getLatestInspectionStatus(appNo);
									if (inspectionStatus != null && !inspectionStatus.isEmpty()) {
										checkInspectStatus = false;
										break;
									}
								}
							}
							;
						}

						boolean haveInspectData = vehicleInspectionService.checkInspectionDataForVehiNo(queueBusNo);
						if ((dataList == null || dataList.isEmpty())&& haveInspectData) {

							while (checkInspect) {
								for (String appNo : latestApplicationNoList) {
									dataList = vehicleInspectionService.GridviewNew(appNo);
									if (dataList != null && !dataList.isEmpty()) {
										checkInspect = false;
										break;
									}
								}
							}
							;

						}

						if (vehicleDTO.getQueueNo() != null) {
							/**
							 * disable skip inspection/save/ upload photo buttons for already skipped
							 * application by tharushi.e
							 **/
							if (disabledTaskCodeDTO.getTaskDetCode() != null
									&& disabledTaskCodeDTO.getTaskDetStatus() != null) {
								if (disabledTaskCodeDTO.getTaskDetCode().equals("PM101")
										|| disabledTaskCodeDTO.getTaskDetCode().equals("PM100")
												&& disabledTaskCodeDTO.getTaskDetStatus().equals("C")
										|| disabledTaskCodeDTO.getTaskDetCode().equals("PR200")
										|| disabledTaskCodeDTO.getTaskDetCode().equals("PM201")
										|| disabledTaskCodeDTO.getTaskDetCode().equals("PM300")
										|| disabledTaskCodeDTO.getTaskDetCode().equals("PM301")
										|| disabledTaskCodeDTO.getTaskDetCode().equals("PM302")
										|| disabledTaskCodeDTO.getTaskDetCode().equals("PM400")) {
									disableSave = true;
									upload = true;

								}

								else {

									disableSave = false;
								}
							} else {
								disableSave = false;
							}
							/** end **/
							callnext = true;
							updateCounterCommit(vehicleDTO.getQueueNo());
							transactionTypeHandler();
							if (vehicleDTO.getVehicleNo() != null) {

								vehicleInspectionService.checkVehicleNo(vehicleDTO.getQueueNo());

								if (dataList.isEmpty()) {
									dataList = vehicleInspectionService.getActiveActionPointData();
								}

								setAlertMSG("Please fill and save the form to continue.");
								RequestContext.getCurrentInstance().execute("PF('alertMSG').show()");
								/**
								 * disable skip inspection/save/ upload photo buttons for already skipped
								 * application by tharushi.e
								 **/
								if (disabledTaskCodeDTO.getTaskDetCode() != null
										&& disabledTaskCodeDTO.getTaskDetStatus() != null) {
									if (disabledTaskCodeDTO.getTaskDetCode().equals("PM101")
											|| disabledTaskCodeDTO.getTaskDetCode().equals("PM100")
													&& disabledTaskCodeDTO.getTaskDetStatus().equals("C")
											|| disabledTaskCodeDTO.getTaskDetCode().equals("PR200")
											|| disabledTaskCodeDTO.getTaskDetCode().equals("PM201")
											|| disabledTaskCodeDTO.getTaskDetCode().equals("PM300")
											|| disabledTaskCodeDTO.getTaskDetCode().equals("PM301")
											|| disabledTaskCodeDTO.getTaskDetCode().equals("PM302")
											|| disabledTaskCodeDTO.getTaskDetCode().equals("PM400")) {
										disableSave = true;
										upload = true;

									}

									else {
										disableSave = false;
									}
								} else {
									disableSave = false;
								}
								/** end **/
								disablePermitNo = true;
								disableQueueNo = true;
								disableAppNo = true;
								readonlyQueueNo = true;
								readonlyAppNo = true;

								return;
							}

							String queueNo = vehicleDTO.getQueueNo();
							boolean queuNumberCancell = migratedService.findCancelledQueueNumber(queueNo);// queue
																											// number
																											// is
																											// cancelled
																											// or
																											// lost
																											// during
																											// and
							// should be able get the queue number and let continue the process
							if (queuNumberCancell) {

								setAlertMSG("Please fill and save the form to continue.");
								RequestContext.getCurrentInstance().execute("PF('alertMSG').show()");
								/**
								 * disable skip inspection/save/ upload photo buttons for already skipped
								 * application by tharushi.e
								 **/
								if (disabledTaskCodeDTO.getTaskDetCode() != null
										&& disabledTaskCodeDTO.getTaskDetStatus() != null) {
									if (disabledTaskCodeDTO.getTaskDetCode().equals("PM101")
											|| disabledTaskCodeDTO.getTaskDetCode().equals("PM100")
													&& disabledTaskCodeDTO.getTaskDetStatus().equals("C")
											|| disabledTaskCodeDTO.getTaskDetCode().equals("PR200")
											|| disabledTaskCodeDTO.getTaskDetCode().equals("PM201")
											|| disabledTaskCodeDTO.getTaskDetCode().equals("PM300")
											|| disabledTaskCodeDTO.getTaskDetCode().equals("PM301")
											|| disabledTaskCodeDTO.getTaskDetCode().equals("PM302")
											|| disabledTaskCodeDTO.getTaskDetCode().equals("PM400")) {
										disableSave = true;
										upload = true;

									}

									else {
										disableSave = false;
									}
								} else {
									disableSave = false;
								}
								/** end **/
								disablePermitNo = true;
								disableQueueNo = true;
								disableAppNo = true;
								readonlyQueueNo = true;
								readonlyAppNo = true;

								return;
							}

						}

						if (vehicleDTO.getVehicleNo() == null) {
							setErrorMsg("Queue No. not found.");
							RequestContext.getCurrentInstance().execute("PF('queueError').show()");
							skip = true;
						}

						/*************************/

					} else {
						setErrorMsg("Please enter the normal inspection queue no.");
						RequestContext.getCurrentInstance().execute("PF('queueError').show()");
					}
				}
			}
		}
	}

	public void transactionTypeHandler() {
		strTrnCode = amendmentService.getTransactionType(queueNo);

		strTrnDesc = amendmentService.getTransactionTypeDesc(strTrnCode);

		switch (strTrnCode) {
		case "02":
			break;
		case "17":
			inspectionForAmendment = true;
			vehicleDTO.setBacklogApp(false);
			break;
		default:
			break;
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

	public void saveAction() {

		if (!vehicleDTO.isBacklogApp()) {
			vehicleDTO.setBacklogStatus("N");
		}
		if (permitNew == true) {
			vehicleDTO.setTenderPermit("Y");
		}
		String loginUser = sessionBackingBean.getLoginUser();
		boolean incomplete = false;

		if (permitNew == true && vehicleDTO.getTenderRefNo().equals("")) {
			
			errorMsg = "Please fill all mandatory fields.";
			RequestContext.getCurrentInstance().update("xx");
			RequestContext.getCurrentInstance().execute("PF('fill').show()");
			incomplete = true;
			
		} else if (vehicleDTO.getNicreg().equals("") || vehicleDTO.getPermitOwner().equals("")
				|| vehicleDTO.getServiceTypeCode().equals("")
				|| (vehicleDTO.getVehicleNo().equals("") || vehicleDTO.getMakeTypeCode().equals("")
						|| vehicleDTO.getModelTypeCode().equals(""))
				|| vehicleDTO.getManyear().equals("") || vehicleDTO.getChassisNo().equals("")
				|| inspectionStatus.equals(null) || inspectionStatus.isEmpty()) {

			errorMsg = "Please fill all mandatory fields.";
			RequestContext.getCurrentInstance().update("xx");
			RequestContext.getCurrentInstance().execute("PF('fill').show()");
			incomplete = true;

		} else if (vehicleDTO != null || incomplete == false) {

			vehicleInspectionService = (VehicleInspectionService) SpringApplicationContex
					.getBean("vehicleInspectionService");
			boolean check = vehicleInspectionService.checkQueueNo(vehicleDTO.getQueueNo());
			if (check == true) {

				upload = false;
				tempVehicleDTO = vehicleDTO;
			}

			vehicleDTO.setCalender1(vehicleDTO.getCalender1());
			vehicleDTO.setLoginUser(sessionBackingBean.getLoginUser());
			vehicleDTO.setCalender2(vehicleDTO.getCalender2());
			boolean save1 = false;

			if (vehicleDTO.getCalender1() == null && vehicleDTO.getCalender2() == null) {
				vehicleDTO.setInspectionStatus("C");
			} else {
				vehicleDTO.setInspectionStatus("P");
			}

			/** check application no should generate or not start **/
			boolean geneAppNo = false;
			if (vehicleDTO.getApplicationNo() != null && !vehicleDTO.getApplicationNo().isEmpty()
					&& !vehicleDTO.getApplicationNo().trim().equalsIgnoreCase("")) {
				
				String appStatus = vehicleInspectionService
						.applicationTaskCodeStatusPM100(vehicleDTO.getApplicationNo());
				if (appStatus != null && !appStatus.isEmpty()) {
					geneAppNo = true;
				}
				
			} else {
				geneAppNo = true;
			}
			/** check application no should generate or not end **/

			if (vehicleDTO.getApplicationNo() == null || vehicleDTO.getApplicationNo().equals("")) {

				if (geneAppNo || vehicleDTO.getBacklogStatus().equals("Y")) {
					generatedApplicationNO = vehicleInspectionService.generateNormalInspectionApplicationNo();
				} else {
					generatedApplicationNO = vehicleDTO.getApplicationNo();

					upload = false;
					printReport = false;
					disableSave = true;
					sessionBackingBean.setCalender1(vehicleDTO.getCalender1());
					sessionBackingBean.setCalender2(vehicleDTO.getCalender2());
					sessionBackingBean.setFinalRemark(vehicleDTO.getFinalRemark());
					tempVehicleDTO = vehicleDTO;
					tempVehicleDTO.setApplicationNo(generatedApplicationNO);
					skip = true;

					return;
				}

				vehicleDTO.setApplicationNo(generatedApplicationNO);
				vehicleDTO.setTranstractionTypeCode("02");

				OminiBusDTO ominiBusDTO = new OminiBusDTO();
				ominiBusDTO.setApplicationNo(generatedApplicationNO);
				ominiBusDTO.setVehicleRegNo(vehicleDTO.getVehicleNo());
				ominiBusDTO.setChassisNo(vehicleDTO.getChassisNo());
				ominiBusDTO.setMake(vehicleDTO.getMakeTypeCode());
				ominiBusDTO.setModel(vehicleDTO.getModelTypeCode());
				ominiBusDTO.setManufactureDate(vehicleDTO.getManyear());
				ominiBusDTO.setIsBacklogApp(vehicleDTO.getBacklogStatus());
				save1 = vehicleInspectionService.saveAllDataWithoutAppNoNew(vehicleDTO, generatedApplicationNO,
						loginUser, ominiBusDTO, dataList, inspectionStatus, tokenType);

				/*** new code end ***/
				if (vehicleDTO.getRouteFlag() != null && !vehicleDTO.getRouteFlag().trim().equals("")
						&& vehicleDTO.getRouteFlag().equalsIgnoreCase("Y")) {
					routeFlag = true;

				} else if (vehicleDTO.getRouteFlag() != null && !vehicleDTO.getRouteFlag().trim().equals("")
						&& vehicleDTO.getRouteFlag().equalsIgnoreCase("N")) {
				}

			} else {

				if (geneAppNo || vehicleDTO.getBacklogStatus().equals("Y")) {
					generatedApplicationNO = vehicleInspectionService.generateNormalInspectionApplicationNo();
				} else {
					generatedApplicationNO = vehicleDTO.getApplicationNo();

					upload = false;
					printReport = false;
					disableSave = true;
					sessionBackingBean.setCalender1(vehicleDTO.getCalender1());
					sessionBackingBean.setCalender2(vehicleDTO.getCalender2());
					sessionBackingBean.setFinalRemark(vehicleDTO.getFinalRemark());
					tempVehicleDTO = vehicleDTO;
					tempVehicleDTO.setApplicationNo(generatedApplicationNO);
					skip = true;

					return;
				}

				vehicleDTO.setTranstractionTypeCode("02");

				OminiBusDTO ominiBusDTO = new OminiBusDTO();
				ominiBusDTO.setApplicationNo(vehicleDTO.getApplicationNo());
				ominiBusDTO.setVehicleRegNo(vehicleDTO.getVehicleNo());
				ominiBusDTO.setChassisNo(vehicleDTO.getChassisNo());
				ominiBusDTO.setMake(vehicleDTO.getMakeTypeCode());
				ominiBusDTO.setModel(vehicleDTO.getModelTypeCode());
				ominiBusDTO.setManufactureDate(vehicleDTO.getManyear());
				ominiBusDTO.setIsBacklogApp(vehicleDTO.getBacklogStatus());
				save1 = vehicleInspectionService.saveAllDataWithAppNew(vehicleDTO, generatedApplicationNO, loginUser,
						ominiBusDTO, dataList, inspectionStatus, tokenType);

				/*** new code end ***/
			}

			if (save1) {
				setSuccessMSG("Successfully saved.");
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
				upload = false;
				printReport = false;
				disableSave = true;
				sessionBackingBean.setCalender1(vehicleDTO.getCalender1());
				sessionBackingBean.setCalender2(vehicleDTO.getCalender2());
				sessionBackingBean.setFinalRemark(vehicleDTO.getFinalRemark());
				tempVehicleDTO = vehicleDTO;
				tempVehicleDTO.setApplicationNo(generatedApplicationNO);
				skip = true;
			} else {
				setErrorMsg("Error occurred. Could not save the data.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		} else {
			setErrorMsg("No record to save.");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void saveReinspection() {

		if (!vehicleDTO.isBacklogApp()) {
			vehicleDTO.setBacklogStatus("N");

		}
		String loginUser = sessionBackingBean.getLoginUser();
		boolean incomplete = false;

		if (vehicleDTO.getNicreg().equals("") || vehicleDTO.getPermitOwner().equals("")
				|| vehicleDTO.getServiceTypeCode().equals("")
				|| (vehicleDTO.getVehicleNo().equals("") || vehicleDTO.getMakeTypeCode().equals("")
						|| vehicleDTO.getModelTypeCode().equals(""))
				|| vehicleDTO.getManyear().equals("") || vehicleDTO.getChassisNo().equals("")) {

			errorMsg = "Please fill all mandatory fields.";
			RequestContext.getCurrentInstance().update("xx");
			RequestContext.getCurrentInstance().execute("PF('fill').show()");
			incomplete = true;

		} else if (vehicleDTO != null || incomplete == false) {

			vehicleInspectionService = (VehicleInspectionService) SpringApplicationContex
					.getBean("vehicleInspectionService");
			boolean check = vehicleInspectionService.checkQueueNo(vehicleDTO.getQueueNo());
			if (check == true) {

				upload = false;
				tempVehicleDTO = vehicleDTO;
			}

			vehicleDTO.setCalender1(vehicleDTO.getCalender1());
			vehicleDTO.setLoginUser(sessionBackingBean.getLoginUser());
			vehicleDTO.setCalender2(vehicleDTO.getCalender2());
			boolean save1 = false;

			if (vehicleDTO.getApplicationNo() == null || vehicleDTO.getApplicationNo().equals("")) {

				generatedApplicationNO = vehicleInspectionService.generateNormalInspectionApplicationNo();

				vehicleDTO.setApplicationNo(generatedApplicationNO);

				OminiBusDTO ominiBusDTO = new OminiBusDTO();
				ominiBusDTO.setApplicationNo(generatedApplicationNO);
				ominiBusDTO.setVehicleRegNo(vehicleDTO.getVehicleNo());
				ominiBusDTO.setChassisNo(vehicleDTO.getChassisNo());
				ominiBusDTO.setMake(vehicleDTO.getMakeTypeCode());
				ominiBusDTO.setModel(vehicleDTO.getModelTypeCode());
				ominiBusDTO.setManufactureDate(vehicleDTO.getManyear());
				ominiBusDTO.setIsBacklogApp(vehicleDTO.getBacklogStatus());
				save1 = vehicleInspectionService.saveAllDataWithoutAppNo(vehicleDTO, generatedApplicationNO, loginUser,
						ominiBusDTO, dataList, "PM100", "C");

				if (vehicleDTO.getRouteFlag() != null && !vehicleDTO.getRouteFlag().trim().equals("")
						&& vehicleDTO.getRouteFlag().equalsIgnoreCase("Y")) {
					routeFlag = true;

				} else if (vehicleDTO.getRouteFlag() != null && !vehicleDTO.getRouteFlag().trim().equals("")
						&& vehicleDTO.getRouteFlag().equalsIgnoreCase("N")) {

				}

			} else {

				generatedApplicationNO = vehicleInspectionService.generateNormalInspectionApplicationNo();

				OminiBusDTO ominiBusDTO = new OminiBusDTO();
				ominiBusDTO.setApplicationNo(vehicleDTO.getApplicationNo());
				ominiBusDTO.setVehicleRegNo(vehicleDTO.getVehicleNo());
				ominiBusDTO.setChassisNo(vehicleDTO.getChassisNo());
				ominiBusDTO.setMake(vehicleDTO.getMakeTypeCode());
				ominiBusDTO.setModel(vehicleDTO.getModelTypeCode());
				ominiBusDTO.setManufactureDate(vehicleDTO.getManyear());
				ominiBusDTO.setIsBacklogApp(vehicleDTO.getBacklogStatus());
				save1 = vehicleInspectionService.saveAllDataWithApp(vehicleDTO, generatedApplicationNO, loginUser,
						ominiBusDTO, dataList, "PM100", "C");

			}

			if (save1 == true) {

				vehicleDTO.setApplicationNo(generatedApplicationNO);
				boolean isTasKPM100Available = vehicleInspectionService.checkTaskDetails(vehicleDTO, "PM100", "C");

				if (isTasKPM100Available == false) {

					vehicleInspectionService.insertTaskDetails(vehicleDTO, loginUser, "PM100", "C");

				}

				migratedService.updateStatusOfQueApp(vehicleDTO.getQueueNo(), generatedApplicationNO);
				migratedService.updateTransactionTypeCodeForQueueNo(vehicleDTO.getQueueNo(), "02");
				migratedService.updateQueueNumberTaskInQueueMaster(vehicleDTO.getQueueNo(), generatedApplicationNO,
						"PM100", "C");

				commonService.updatePhotoUploadStatus(generatedApplicationNO, "N");

				setSuccessMSG("Successfully saved.");
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
				upload = false;
				disableSave = true;
				sessionBackingBean.setCalender1(vehicleDTO.getCalender1());
				sessionBackingBean.setCalender2(vehicleDTO.getCalender2());
				sessionBackingBean.setFinalRemark(vehicleDTO.getFinalRemark());
				tempVehicleDTO = vehicleDTO;
				tempVehicleDTO.setApplicationNo(generatedApplicationNO);
				skip = true;
			} else {

				setErrorMsg("Error occurred. Could not save the data.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

			}

		} else {
			setErrorMsg("No record to save.");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		}

	}

	public void saveReinspectionOne() {

		if (!vehicleDTO.isBacklogApp()) {
			vehicleDTO.setBacklogStatus("N");

		}
		String loginUser = sessionBackingBean.getLoginUser();
		boolean incomplete = false;

		if (vehicleDTO.getNicreg().equals("") || vehicleDTO.getPermitOwner().equals("")
				|| vehicleDTO.getServiceTypeCode().equals("")
				|| (vehicleDTO.getVehicleNo().equals("") || vehicleDTO.getMakeTypeCode().equals("")
						|| vehicleDTO.getModelTypeCode().equals(""))
				|| vehicleDTO.getManyear().equals("") || vehicleDTO.getChassisNo().equals("")) {

			errorMsg = "Please fill all mandatory fields.";
			RequestContext.getCurrentInstance().update("xx");
			RequestContext.getCurrentInstance().execute("PF('fill').show()");
			incomplete = true;

		} else if (vehicleDTO != null || incomplete == false) {
			vehicleInspectionService = (VehicleInspectionService) SpringApplicationContex
					.getBean("vehicleInspectionService");

			vehicleDTO.setLoginUser(sessionBackingBean.getLoginUser());

			if (vehicleDTO.getCalender1() == null && vehicleDTO.getCalender2() == null) {

				vehicleDTO.setInspectionStatus("C");

			} else {
				vehicleDTO.setInspectionStatus("P");
			}

			vehicleDTO.setCalender1(vehicleDTO.getCalender1());
			vehicleDTO.setLoginUser(sessionBackingBean.getLoginUser());
			vehicleDTO.setCalender2(vehicleDTO.getCalender2());
			boolean save1 = false;
			boolean save2 = false;

			String neededApplicationNo = vehicleDTO.getApplicationNo();

			vehicleDTO.setTranstractionTypeCode("02");
			save1 = vehicleInspectionService.updateDataApplicationTable(vehicleDTO, neededApplicationNo, loginUser);
			save2 = vehicleInspectionService.updateDataVehicleOwnerWithApplicatioNo(vehicleDTO, neededApplicationNo);
			boolean result = inspectionActionPointService.saveDataVehicleInspecDetails(vehicleDTO, dataList,
					neededApplicationNo, loginUser);
			vehicleInspectionService.saveDataVehicleInspecDetailsHistory(vehicleDTO, dataList, neededApplicationNo);

			OminiBusDTO ominiBusDTO = new OminiBusDTO();
			ominiBusDTO.setApplicationNo(vehicleDTO.getApplicationNo());
			ominiBusDTO.setVehicleRegNo(vehicleDTO.getVehicleNo());
			ominiBusDTO.setChassisNo(vehicleDTO.getChassisNo());
			ominiBusDTO.setMake(vehicleDTO.getMakeTypeCode());
			ominiBusDTO.setModel(vehicleDTO.getModelTypeCode());
			ominiBusDTO.setManufactureDate(vehicleDTO.getManyear());
			ominiBusDTO.setIsBacklogApp(vehicleDTO.getBacklogStatus());

			int resultOminiBusDet = inspectionActionPointService
					.updateOminiBusDetInspectionRecordWithApplicationNo(vehicleDTO, permitDTO, loginUser);
			;

			if (save1 == true && save2 == true) {
				vehicleDTO.setApplicationNo(neededApplicationNo);
				boolean isTasKPM100Available = vehicleInspectionService.checkTaskDetails(vehicleDTO, "PM100", "C");

				if (isTasKPM100Available == false) {

					vehicleInspectionService.insertTaskDetails(vehicleDTO, loginUser, "PM100", "C");

				}

				migratedService.updateStatusOfQueApp(vehicleDTO.getQueueNo(), neededApplicationNo);
				migratedService.updateTransactionTypeCodeForQueueNo(vehicleDTO.getQueueNo(), "02");
				migratedService.updateQueueNumberTaskInQueueMaster(vehicleDTO.getQueueNo(), neededApplicationNo,
						"PM100", "C");

				commonService.updatePhotoUploadStatus(neededApplicationNo, "N");

				setSuccessMSG("Successfully saved.");
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
				upload = false;
				disableSave = true;
				sessionBackingBean.setCalender1(vehicleDTO.getCalender1());
				sessionBackingBean.setCalender2(vehicleDTO.getCalender2());
				sessionBackingBean.setFinalRemark(vehicleDTO.getFinalRemark());
				tempVehicleDTO = vehicleDTO;
				tempVehicleDTO.setApplicationNo(neededApplicationNo);
				skip = true;
			} else {

				setErrorMsg("Error occurred. Could not save the data.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

			}

		} else {
			setErrorMsg("No record to save.");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		}

	}

	public void saveInspectionForAmendment() {

		if (!vehicleDTO.isBacklogApp()) {
			vehicleDTO.setBacklogStatus("N");
		}

		String loginUser = sessionBackingBean.getLoginUser();
		vehicleDTO.setCreateBy(loginUser);
		boolean incomplete = false;

		if (vehicleDTO.getNicreg().equals("") || vehicleDTO.getPermitOwner().equals("")
				|| vehicleDTO.getServiceTypeCode().equals("")
				|| (vehicleDTO.getVehicleNo().equals("") || vehicleDTO.getMakeTypeCode().equals("")
						|| vehicleDTO.getModelTypeCode().equals(""))
				|| vehicleDTO.getManyear().equals("") || vehicleDTO.getChassisNo().equals("")
				|| inspectionStatus.equals(null) || inspectionStatus.isEmpty()) {

			errorMsg = "Please fill all mandatory fields.";
			RequestContext.getCurrentInstance().update("xx");
			RequestContext.getCurrentInstance().execute("PF('fill').show()");
			incomplete = true;

		} else if (vehicleDTO != null || incomplete == false) {

			vehicleInspectionService = (VehicleInspectionService) SpringApplicationContex
					.getBean("vehicleInspectionService");

			vehicleDTO.setLoginUser(sessionBackingBean.getLoginUser());

			if (vehicleDTO.getCalender1() == null && vehicleDTO.getCalender2() == null) {

				vehicleDTO.setInspectionStatus("C");

			} else {
				vehicleDTO.setInspectionStatus("P");
			}

			vehicleInspectionService.saveInspectionForAmendment(vehicleDTO);

			OminiBusDTO ominiBusDTO = new OminiBusDTO();
			ominiBusDTO.setApplicationNo(vehicleDTO.getApplicationNo());
			ominiBusDTO.setVehicleRegNo(vehicleDTO.getVehicleNo());
			ominiBusDTO.setChassisNo(vehicleDTO.getChassisNo());
			ominiBusDTO.setEngineNo(vehicleDTO.getEngineNo());
			ominiBusDTO.setMake(vehicleDTO.getMakeTypeCode());
			ominiBusDTO.setModel(vehicleDTO.getModelTypeCode());
			ominiBusDTO.setManufactureDate(vehicleDTO.getManyear());
			ominiBusDTO.setIsBacklogApp(vehicleDTO.getBacklogStatus());
			applicationHistoryDTO = historyService.getApplicationTableData(generatedApplicationNO,
					sessionBackingBean.getLoginUser());
			String amendmendTrnType = amendmentService
					.getTransactionTypeFromAmendmendTable(vehicleDTO.getApplicationNo());
			vehicleDTO.setTranstractionTypeCode(amendmendTrnType);
			String currentAppNo = generatedApplicationNO;

			historyService.insertApplicationHistoryData(applicationHistoryDTO);
			boolean amendmentInspectUpdate = vehicleInspectionService.updateAmendmentInspectionDeatils(ominiBusDTO,
					vehicleDTO, dataList, vehicleDTO.getApplicationNo(), generatedApplicationNO,
					vehicleDTO.getTranstractionTypeCode(), currentAppNo, sessionBackingBean.getLoginUser(),
					applicationHistoryDTO, inspectionStatus, tokenType);
			boolean isTasKPM100Available;

			String strTrnCode;
			strTrnCode = amendmentService.getTrnTypeFromAmendment(generatedApplicationNO);

			strTrnDesc = amendmentService.getTransactionTypeDesc(strTrnCode);

			// bus=13
			// service=05
			// owner=14
			// owner & bus=15
			// bus & service=16

			vehicleDTO.setTranstractionTypeCode(strTrnCode);

			switch (strTrnCode) {
			case "13":
				commonService.updateTaskStatus(generatedApplicationNO, "AM100", "PM100", "C",
						sessionBackingBean.getLoginUser());
				commonService.updateTaskStatusCompleted(generatedApplicationNO, "PM100",
						sessionBackingBean.getLoginUser());
				break;

			case "05":

				commonService.updateTaskStatus(generatedApplicationNO, "AM106", "PM100", "C",
						sessionBackingBean.getLoginUser());
				commonService.updateTaskStatusCompleted(generatedApplicationNO, "PM100",
						sessionBackingBean.getLoginUser());

				break;
			case "21":

				commonService.updateTaskStatus(generatedApplicationNO, "AM106", "PM100", "C",
						sessionBackingBean.getLoginUser());
				commonService.updateTaskStatusCompleted(generatedApplicationNO, "PM100",
						sessionBackingBean.getLoginUser());

				break;
			case "22":

				commonService.updateTaskStatus(generatedApplicationNO, "AM106", "PM100", "C",
						sessionBackingBean.getLoginUser());
				commonService.updateTaskStatusCompleted(generatedApplicationNO, "PM100",
						sessionBackingBean.getLoginUser());

				break;
			case "23":

				commonService.updateTaskStatus(generatedApplicationNO, "AM106", "PM100", "C",
						sessionBackingBean.getLoginUser());
				commonService.updateTaskStatusCompleted(generatedApplicationNO, "PM100",
						sessionBackingBean.getLoginUser());

				break;
			case "14":

				commonService.updateTaskStatus(generatedApplicationNO, "AM106", "PM100", "C",
						sessionBackingBean.getLoginUser());
				commonService.updateTaskStatusCompleted(generatedApplicationNO, "PM100",
						sessionBackingBean.getLoginUser());

				break;
			case "15":
				commonService.updateTaskStatus(generatedApplicationNO, "AM106", "PM100", "C",
						sessionBackingBean.getLoginUser());
				commonService.updateTaskStatusCompleted(generatedApplicationNO, "PM100",
						sessionBackingBean.getLoginUser());

				break;
			case "16":
				commonService.updateTaskStatus(generatedApplicationNO, "AM106", "PM100", "C",
						sessionBackingBean.getLoginUser());
				commonService.updateTaskStatusCompleted(generatedApplicationNO, "PM100",
						sessionBackingBean.getLoginUser());

				break;
			default:

				break;
			}

			migratedService.updateStatusOfQueApp(vehicleDTO.getQueueNo(), generatedApplicationNO);
			migratedService.updateTransactionTypeCodeForQueueNo(vehicleDTO.getQueueNo(), strTrnCode);
			migratedService.updateQueueNumberTaskInQueueMaster(vehicleDTO.getQueueNo(), generatedApplicationNO, "PM100",
					"C");

			commonService.updatePhotoUploadStatus(generatedApplicationNO, "N");

			setSuccessMSG("Successfully saved.");
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
			upload = false;

			printReport = false;

			disableSave = true;
			sessionBackingBean.setCalender1(vehicleDTO.getCalender1());
			sessionBackingBean.setCalender2(vehicleDTO.getCalender2());
			sessionBackingBean.setFinalRemark(vehicleDTO.getFinalRemark());
			tempVehicleDTO = vehicleDTO;
			tempVehicleDTO.setApplicationNo(vehicleDTO.getApplicationNo());
			skip = true;

		} else {
			setErrorMsg("No record to save.");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		}

	}

	public void skipPhotoUpload() {

		if (!vehicleDTO.isBacklogApp()) {
			vehicleDTO.setBacklogStatus("N");

		}
		String loginUser = sessionBackingBean.getLoginUser();
		boolean incomplete = false;

		if (vehicleDTO.getNicreg().equals("") || vehicleDTO.getPermitOwner().equals("")
				|| vehicleDTO.getServiceTypeCode().equals("")
				|| (vehicleDTO.getVehicleNo().equals("") || vehicleDTO.getMakeTypeCode().equals("")
						|| vehicleDTO.getModelTypeCode().equals(""))
				|| vehicleDTO.getManyear().equals("") || vehicleDTO.getChassisNo().equals("")
				|| inspectionStatus.equals(null) || inspectionStatus.isEmpty()) {

			errorMsg = "Please fill all mandatory fields.";
			RequestContext.getCurrentInstance().update("xx");
			RequestContext.getCurrentInstance().execute("PF('fill').show()");
			incomplete = true;

		} else if (vehicleDTO != null || incomplete == false) {

			vehicleInspectionService = (VehicleInspectionService) SpringApplicationContex
					.getBean("vehicleInspectionService");
			boolean check = vehicleInspectionService.checkQueueNo(vehicleDTO.getQueueNo());
			if (check == true) {

				upload = false;
				tempVehicleDTO = vehicleDTO;
			}
			vehicleDTO.setCalender1(vehicleDTO.getCalender1());
			vehicleDTO.setLoginUser(sessionBackingBean.getLoginUser());
			vehicleDTO.setCalender2(vehicleDTO.getCalender2());
			boolean save1 = false;
			boolean save2 = false;

			if (vehicleDTO.getCalender1() == null && vehicleDTO.getCalender2() == null) {

				vehicleDTO.setInspectionStatus("C");

			} else {
				vehicleDTO.setInspectionStatus("P");
			}

			/** check application no should generate or not start **/
			boolean geneAppNo = false;
			if (vehicleDTO.getApplicationNo() != null && !vehicleDTO.getApplicationNo().isEmpty()
					&& !vehicleDTO.getApplicationNo().trim().equalsIgnoreCase("")) {
				String appStatus = vehicleInspectionService
						.applicationTaskCodeStatusPM100(vehicleDTO.getApplicationNo());
				if (appStatus != null && !appStatus.isEmpty() && !appStatus.trim().equalsIgnoreCase("")) {
					geneAppNo = true;
				}
			} else {
				geneAppNo = true;
			}
			/** check application no should generate or not end **/
			geneAppNo = true;
			if (vehicleDTO.getApplicationNo() == null || vehicleDTO.getApplicationNo().equals("")) {

				if (geneAppNo || vehicleDTO.getBacklogStatus().equals("Y")) {
					generatedApplicationNO = vehicleInspectionService.generateNormalInspectionApplicationNo();
				} else {
					generatedApplicationNO = vehicleDTO.getApplicationNo();

					upload = false;

					printReport = false;

					disableSave = true;
					sessionBackingBean.setCalender1(vehicleDTO.getCalender1());
					sessionBackingBean.setCalender2(vehicleDTO.getCalender2());
					sessionBackingBean.setFinalRemark(vehicleDTO.getFinalRemark());
					tempVehicleDTO = vehicleDTO;
					tempVehicleDTO.setApplicationNo(generatedApplicationNO);
					skip = true;

				}

				vehicleDTO.setApplicationNo(generatedApplicationNO);

				vehicleDTO.setTranstractionTypeCode("02");

				commonService.duplicateActionPoints(generatedApplicationNO, oldApplicationNo);

				if (commonService.checkActionPointsCount(oldApplicationNo) < 1) {
					vehicleInspectionService.saveDataVehicleInspecDetails(vehicleDTO, dataList, generatedApplicationNO);
					vehicleInspectionService.saveDataVehicleInspecDetailsHistory(vehicleDTO, dataList,
							generatedApplicationNO);
				}

				OminiBusDTO ominiBusDTO = new OminiBusDTO();
				ominiBusDTO.setApplicationNo(generatedApplicationNO);
				ominiBusDTO.setVehicleRegNo(vehicleDTO.getVehicleNo());
				ominiBusDTO.setChassisNo(vehicleDTO.getChassisNo());
				ominiBusDTO.setMake(vehicleDTO.getMakeTypeCode());
				ominiBusDTO.setModel(vehicleDTO.getModelTypeCode());
				ominiBusDTO.setManufactureDate(vehicleDTO.getManyear());
				ominiBusDTO.setIsBacklogApp(vehicleDTO.getBacklogStatus());

				save1 = vehicleInspectionService.saveAllDataWithoutAppNoNew(vehicleDTO, generatedApplicationNO,
						loginUser, ominiBusDTO, dataList, inspectionStatus, tokenType);
				if (vehicleDTO.getRouteFlag() != null && !vehicleDTO.getRouteFlag().trim().equals("")
						&& vehicleDTO.getRouteFlag().equalsIgnoreCase("Y")) {
					routeFlag = true;

				} else if (vehicleDTO.getRouteFlag() != null && !vehicleDTO.getRouteFlag().trim().equals("")
						&& vehicleDTO.getRouteFlag().equalsIgnoreCase("N")) {

				}

			} else {

				if (geneAppNo || vehicleDTO.getBacklogStatus().equals("Y")) {
					generatedApplicationNO = vehicleInspectionService.generateNormalInspectionApplicationNo();
				} else {
					generatedApplicationNO = vehicleDTO.getApplicationNo();
					upload = false;

					printReport = false;

					disableSave = true;
					sessionBackingBean.setCalender1(vehicleDTO.getCalender1());
					sessionBackingBean.setCalender2(vehicleDTO.getCalender2());
					sessionBackingBean.setFinalRemark(vehicleDTO.getFinalRemark());
					tempVehicleDTO = vehicleDTO;
					tempVehicleDTO.setApplicationNo(generatedApplicationNO);
					skip = true;

				}

				vehicleDTO.setTranstractionTypeCode("02");

				OminiBusDTO ominiBusDTO = new OminiBusDTO();
				ominiBusDTO.setApplicationNo(vehicleDTO.getApplicationNo());
				ominiBusDTO.setVehicleRegNo(vehicleDTO.getVehicleNo());
				ominiBusDTO.setChassisNo(vehicleDTO.getChassisNo());
				ominiBusDTO.setMake(vehicleDTO.getMakeTypeCode());
				ominiBusDTO.setModel(vehicleDTO.getModelTypeCode());
				ominiBusDTO.setManufactureDate(vehicleDTO.getManyear());
				ominiBusDTO.setIsBacklogApp(vehicleDTO.getBacklogStatus());

				save1 = vehicleInspectionService.saveAllDataWithAppNew(vehicleDTO, generatedApplicationNO, loginUser,
						ominiBusDTO, dataList, inspectionStatus, tokenType);
			}

			if (save1) {

				VehicleInspectionDTO vDto = new VehicleInspectionDTO();
				vDto.setApplicationNo(generatedApplicationNO);
				vehicleInspectionService.deleteTaskDetails(vDto, "PM100");

				/** check if same task exists in task history start **/
				boolean exists = paymentVoucherService.checkPhotoUploadHistory(generatedApplicationNO, "O");
				/** check if same task exists in task history end **/

				if (!exists) {
					commonService.updateTaskStatusCompleted(generatedApplicationNO, "PM101",
							sessionBackingBean.getLoginUser());
					vehicleInspectionService.insertTaskDetails(vehicleDTO, loginUser, "PM101", "C");
				}

				migratedService.updateStatusOfQueueNumberAfterCallNext(queueNo, "C");

				commonService.updatePhotoUploadStatus(generatedApplicationNO, "Y");

				commonService.duplicatePhotoUpload(generatedApplicationNO, oldApplicationNo);

				setSuccessMSG("Successfully skipped inspection.");
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

				upload = false;

				printReport = false;

				disableSave = true;
				sessionBackingBean.setCalender1(vehicleDTO.getCalender1());
				sessionBackingBean.setCalender2(vehicleDTO.getCalender2());
				sessionBackingBean.setFinalRemark(vehicleDTO.getFinalRemark());
				tempVehicleDTO = vehicleDTO;
				tempVehicleDTO.setApplicationNo(generatedApplicationNO);
				skip = true;
			} else {

				errorMsg = "Error occurred. Could not save the data.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

			}

		} else {

			errorMsg = "No record to save.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		}

		commonService.updateCounterQueueNo(null, sessionBackingBean.getCounterId());

	}

	public void skipPhotoUploadOld() {

		if (!vehicleDTO.isBacklogApp()) {
			vehicleDTO.setBacklogStatus("N");

		}
		String loginUser = sessionBackingBean.getLoginUser();
		boolean incomplete = false;

		if (vehicleDTO.getNicreg().equals("") || vehicleDTO.getPermitOwner().equals("")
				|| vehicleDTO.getServiceTypeCode().equals("")
				|| (vehicleDTO.getVehicleNo().equals("") || vehicleDTO.getMakeTypeCode().equals("")
						|| vehicleDTO.getModelTypeCode().equals(""))
				|| vehicleDTO.getManyear().equals("") || vehicleDTO.getChassisNo().equals("")
				|| inspectionStatus.equals(null) || inspectionStatus.isEmpty()) {

			errorMsg = "Please fill all mandatory fields.";
			RequestContext.getCurrentInstance().update("xx");
			RequestContext.getCurrentInstance().execute("PF('fill').show()");
			incomplete = true;

		} else if (vehicleDTO != null || incomplete == false) {

			vehicleInspectionService = (VehicleInspectionService) SpringApplicationContex
					.getBean("vehicleInspectionService");
			boolean check = vehicleInspectionService.checkQueueNo(vehicleDTO.getQueueNo());
			if (check == true) {

				upload = false;
				tempVehicleDTO = vehicleDTO;
			}

			vehicleDTO.setCalender1(vehicleDTO.getCalender1());
			vehicleDTO.setLoginUser(sessionBackingBean.getLoginUser());
			vehicleDTO.setCalender2(vehicleDTO.getCalender2());
			boolean save1 = false;
			boolean save2 = false;

			if (vehicleDTO.getCalender1() == null && vehicleDTO.getCalender2() == null) {

				vehicleDTO.setInspectionStatus("C");

			} else {
				vehicleDTO.setInspectionStatus("P");
			}

			/** check application no should generate or not start **/
			boolean geneAppNo = false;
			if (vehicleDTO.getApplicationNo() != null && !vehicleDTO.getApplicationNo().isEmpty()
					&& !vehicleDTO.getApplicationNo().trim().equalsIgnoreCase("")) {
				String appStatus = vehicleInspectionService
						.applicationTaskCodeStatusPM100(vehicleDTO.getApplicationNo());
				if (appStatus != null && !appStatus.isEmpty() && !appStatus.trim().equalsIgnoreCase("")) {
					geneAppNo = true;
				}
			} else {
				geneAppNo = true;
			}
			/** check application no should generate or not end **/

			if (vehicleDTO.getApplicationNo() == null || vehicleDTO.getApplicationNo().equals("")) {

				if (geneAppNo || vehicleDTO.getBacklogStatus().equals("Y")) {
					generatedApplicationNO = vehicleInspectionService.generateNormalInspectionApplicationNo();
				} else {
					generatedApplicationNO = vehicleDTO.getApplicationNo();

					upload = false;

					printReport = false;

					disableSave = true;
					sessionBackingBean.setCalender1(vehicleDTO.getCalender1());
					sessionBackingBean.setCalender2(vehicleDTO.getCalender2());
					sessionBackingBean.setFinalRemark(vehicleDTO.getFinalRemark());
					tempVehicleDTO = vehicleDTO;
					tempVehicleDTO.setApplicationNo(generatedApplicationNO);
					skip = true;

				}

				vehicleDTO.setApplicationNo(generatedApplicationNO);

				vehicleDTO.setTranstractionTypeCode("02");
				save1 = vehicleInspectionService.saveDataApplication(vehicleDTO, generatedApplicationNO, loginUser);
				save2 = vehicleInspectionService.saveDataVehicleOwnerWithOutApplicationNo(vehicleDTO,
						generatedApplicationNO);

				commonService.duplicateActionPoints(generatedApplicationNO, oldApplicationNo);

				if (commonService.checkActionPointsCount(oldApplicationNo) < 1) {
					vehicleInspectionService.saveDataVehicleInspecDetails(vehicleDTO, dataList, generatedApplicationNO);
					vehicleInspectionService.saveDataVehicleInspecDetailsHistory(vehicleDTO, dataList,
							generatedApplicationNO);
				}

				OminiBusDTO ominiBusDTO = new OminiBusDTO();
				ominiBusDTO.setApplicationNo(generatedApplicationNO);
				ominiBusDTO.setVehicleRegNo(vehicleDTO.getVehicleNo());
				ominiBusDTO.setChassisNo(vehicleDTO.getChassisNo());
				ominiBusDTO.setMake(vehicleDTO.getMakeTypeCode());
				ominiBusDTO.setModel(vehicleDTO.getModelTypeCode());
				ominiBusDTO.setManufactureDate(vehicleDTO.getManyear());
				ominiBusDTO.setIsBacklogApp(vehicleDTO.getBacklogStatus());
				int test = adminService.saveBacklogOminiBus(ominiBusDTO);

				if (vehicleDTO.getRouteFlag() != null && !vehicleDTO.getRouteFlag().trim().equals("")
						&& vehicleDTO.getRouteFlag().equalsIgnoreCase("Y")) {
					routeFlag = true;

				} else if (vehicleDTO.getRouteFlag() != null && !vehicleDTO.getRouteFlag().trim().equals("")
						&& vehicleDTO.getRouteFlag().equalsIgnoreCase("N")) {

				}

			} else {

				if (geneAppNo || vehicleDTO.getBacklogStatus().equals("Y")) {
					generatedApplicationNO = vehicleInspectionService.generateNormalInspectionApplicationNo();
				} else {
					generatedApplicationNO = vehicleDTO.getApplicationNo();

					upload = false;

					printReport = false;

					disableSave = true;
					sessionBackingBean.setCalender1(vehicleDTO.getCalender1());
					sessionBackingBean.setCalender2(vehicleDTO.getCalender2());
					sessionBackingBean.setFinalRemark(vehicleDTO.getFinalRemark());
					tempVehicleDTO = vehicleDTO;
					tempVehicleDTO.setApplicationNo(generatedApplicationNO);
					skip = true;

				}

				vehicleDTO.setTranstractionTypeCode("02");
				save1 = vehicleInspectionService.saveDataApplication(vehicleDTO, generatedApplicationNO, loginUser);
				save2 = vehicleInspectionService.saveDataVehicleOwnerWithApplicatioNo(vehicleDTO,
						generatedApplicationNO);

				OminiBusDTO ominiBusDTO = new OminiBusDTO();
				ominiBusDTO.setApplicationNo(vehicleDTO.getApplicationNo());
				ominiBusDTO.setVehicleRegNo(vehicleDTO.getVehicleNo());
				ominiBusDTO.setChassisNo(vehicleDTO.getChassisNo());
				ominiBusDTO.setMake(vehicleDTO.getMakeTypeCode());
				ominiBusDTO.setModel(vehicleDTO.getModelTypeCode());
				ominiBusDTO.setManufactureDate(vehicleDTO.getManyear());
				ominiBusDTO.setIsBacklogApp(vehicleDTO.getBacklogStatus());
				int test = adminService.saveBacklogOminiBusWithApplicationNo(ominiBusDTO, generatedApplicationNO);

			}

			if (save1 == true && save2 == true) {

				vehicleDTO.setApplicationNo(generatedApplicationNO);
				boolean isTasKPM100Available = vehicleInspectionService.checkTaskDetails(vehicleDTO, "PM100", "C");

				if (isTasKPM100Available == false) {

					vehicleInspectionService.insertTaskDetails(vehicleDTO, loginUser, "PM100", "C");

				}

				migratedService.updateStatusOfQueApp(vehicleDTO.getQueueNo(), generatedApplicationNO);
				migratedService.updateTransactionTypeCodeForQueueNo(vehicleDTO.getQueueNo(), "02");
				migratedService.updateQueueNumberTaskInQueueMaster(vehicleDTO.getQueueNo(), generatedApplicationNO,
						"PM100", "C");

				commonService.updatePhotoUploadStatus(generatedApplicationNO, "N");

				VehicleInspectionDTO vDto = new VehicleInspectionDTO();
				vDto.setApplicationNo(generatedApplicationNO);
				vehicleInspectionService.deleteTaskDetails(vDto, "PM100");

				/** check if same task exists in task history start **/
				boolean exists = paymentVoucherService.checkPhotoUploadHistory(generatedApplicationNO, "O");
				/** check if same task exists in task history end **/

				if (!exists) {
					commonService.updateTaskStatusCompleted(generatedApplicationNO, "PM101",
							sessionBackingBean.getLoginUser());
					vehicleInspectionService.insertTaskDetails(vehicleDTO, loginUser, "PM101", "C");
				}

				migratedService.updateStatusOfQueueNumberAfterCallNext(queueNo, "C");

				commonService.updatePhotoUploadStatus(generatedApplicationNO, "Y");

				commonService.duplicatePhotoUpload(generatedApplicationNO, oldApplicationNo);

				setSuccessMSG("Successfully skipped inspection.");
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

				upload = false;

				printReport = false;

				disableSave = true;
				sessionBackingBean.setCalender1(vehicleDTO.getCalender1());
				sessionBackingBean.setCalender2(vehicleDTO.getCalender2());
				sessionBackingBean.setFinalRemark(vehicleDTO.getFinalRemark());
				tempVehicleDTO = vehicleDTO;
				tempVehicleDTO.setApplicationNo(generatedApplicationNO);
				skip = true;
			} else {

				errorMsg = "Error occurred. Could not save the data.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

			}

		} else {

			errorMsg = "No record to save.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		}

		commonService.updateCounterQueueNo(null, sessionBackingBean.getCounterId());

	}

	public void dropdown() {
		vehicleInspectionService = (VehicleInspectionService) SpringApplicationContex
				.getBean("vehicleInspectionService");
		modellist = vehicleInspectionService.modeldropdown("");
		makelist = vehicleInspectionService.makedropdown();
		servicelist = vehicleInspectionService.servicetypedropdown();
		routelist = inspectionActionPointService.getRouteNoList();
		counterList = vehicleInspectionService.counterdropdown();

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
		skip = true;
		inspectionType = null;
		inspectionForAmendment = false;
		vehicleDTO = new VehicleInspectionDTO();
		dataList.clear();
		disableSave = true;
		disableQueueNo = false;
		callnext = false;
		readonlyQueueNo = false;
		readonlyAppNo = false;
		routeDTO = new RouteDTO();
		permitDTO = new PermitDTO();
		routeFlag = false;
		reinspection = false;
		commonService.updateCounterQueueNo(null, sessionBackingBean.getCounterId());
		permitNew=false;
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

	public boolean isCallnext() {
		return callnext;
	}

	public void setCallnext(boolean callnext) {
		this.callnext = callnext;
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

	public VehicleInspectionDTO getTempVehicleDTO() {
		return tempVehicleDTO;
	}

	public void setTempVehicleDTO(VehicleInspectionDTO tempVehicleDTO) {
		this.tempVehicleDTO = tempVehicleDTO;
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

	public AmendmentService getAmendmentService() {
		return amendmentService;
	}

	public void setAmendmentService(AmendmentService amendmentService) {
		this.amendmentService = amendmentService;
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

	public String getInspectionStatus() {
		return inspectionStatus;
	}

	public void setInspectionStatus(String inspectionStatus) {
		this.inspectionStatus = inspectionStatus;
	}

	public List<VehicleInspectionDTO> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<VehicleInspectionDTO> locationList) {
		this.locationList = locationList;
	}

	public String getInspectionType() {
		return inspectionType;
	}

	public void setInspectionType(String inspectionType) {
		this.inspectionType = inspectionType;
	}

	public MigratedService getMigratedService() {
		return migratedService;
	}

	public void setMigratedService(MigratedService migratedService) {
		this.migratedService = migratedService;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public boolean isPermitNew() {
		return permitNew;
	}

	public void setPermitNew(boolean permitNew) {
		this.permitNew = permitNew;
	}

	
	
	
	

}