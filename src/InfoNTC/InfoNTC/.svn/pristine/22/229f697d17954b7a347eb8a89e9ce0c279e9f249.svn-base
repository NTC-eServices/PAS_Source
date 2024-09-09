package lk.informatics.ntc.view.beans;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.PermitDTO;
import lk.informatics.ntc.model.dto.RouteDTO;
import lk.informatics.ntc.model.dto.VehicleInspectionDTO;
import lk.informatics.ntc.model.service.AdminService;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.InspectionActionPointService;
import lk.informatics.ntc.model.service.PaymentVoucherService;
import lk.informatics.ntc.model.service.VehicleInspectionService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "viewInspectionInfoBean")
@ViewScoped
public class ViewInspectionInfoBean {
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	private String dateOne;
	private String dateTwo;
	private String selectedApplicationNumber;
	private String photoUploadStatus;
	private String preSearchedApplicationNum;
	private String preSearchedVehicleNum;
	private boolean checkSearchedBtn, checkSession;
	private boolean readOnlyValue;
	private boolean disabledActPointCheckedBox;
	private boolean hideField;
	private String title;
	private String errorMsg;
	private String selectedRouteNo;
	private String selectedServiceType;
	private String sucessMsg;

	private Boolean routeFlag;

	public VehicleInspectionDTO vehicleDTO = new VehicleInspectionDTO();
	private VehicleInspectionDTO viewedDetails;

	private List<VehicleInspectionDTO> dataList = new ArrayList<VehicleInspectionDTO>();
	private List<VehicleInspectionDTO> routeNoList = new ArrayList<VehicleInspectionDTO>();
	private List<VehicleInspectionDTO> serviceTypeList = new ArrayList<VehicleInspectionDTO>();

	private InspectionActionPointService inspectionActionPointService;
	private AdminService adminService;
	private VehicleInspectionService vehicleInspectionService;
	private CommonService commonService;
	private PaymentVoucherService paymentVoucherService;

	private boolean inquiryMoodUpload = true;
	private boolean renderInquiryBackButton;
	private boolean renderRenewalBackButton;
	private boolean renderMainBackButton;

	private PermitDTO permitDTO;
	private RouteDTO routeDTO;

	private VehicleInspectionDTO taskDetWithAppDetDTO;

	@PostConstruct
	public void init() {
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");
		inspectionActionPointService = (InspectionActionPointService) SpringApplicationContex
				.getBean("inspectionActionPointService");
		vehicleInspectionService = (VehicleInspectionService) SpringApplicationContex
				.getBean("vehicleInspectionService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		paymentVoucherService = (PaymentVoucherService) SpringApplicationContex.getBean("paymentVoucherService");

		permitDTO = new PermitDTO();
		routeDTO = new RouteDTO();

		selectedApplicationNumber = sessionBackingBean.getViewedInspectionApplicationNo();
		preSearchedApplicationNum = sessionBackingBean.getSearchedApplicationNo();
		preSearchedVehicleNum = sessionBackingBean.getSearchedVehicleNo();
		checkSearchedBtn = sessionBackingBean.isCheckIsSearchPressed();

		photoUploadStatus = commonService.getPhotoUploadStatus(selectedApplicationNumber);

		routeNoList = inspectionActionPointService.getRouteNoList();
		serviceTypeList = inspectionActionPointService.getServiceTypesList();
		loadValues();
		renderMainBackButton = true;

		taskDetWithAppDetDTO = new VehicleInspectionDTO();

		if (sessionBackingBean.getSelectedOptionType().equals("VIEW")) {

			readOnlyValue = sessionBackingBean.isReadOnlyFieldsInspection();
			disabledActPointCheckedBox = true;
			hideField = false;
			title = "View Vehicle Inspection";

		} else if (sessionBackingBean.getSelectedOptionType().equals("EDIT")) {

			readOnlyValue = sessionBackingBean.isReadOnlyFieldsInspection();
			disabledActPointCheckedBox = false;
			hideField = true;
			title = "Edit Vehicle Inspection";
			selectedRouteNo = vehicleDTO.getRouteNo();

			selectedServiceType = vehicleDTO.getServiceTypeCode();

			if (vehicleDTO.getDate1() != null && vehicleDTO.getDate2() != null) {
				loadDateVal();
			} else {

			}

		}

		if (sessionBackingBean.isClicked == true) {
			renderInquiryBackButton = true;
			renderMainBackButton = false;
			inquiryMoodUpload = false;

			sessionBackingBean.isClicked = false;

		}

		if (sessionBackingBean.renewalViewMood == true) {
			renderRenewalBackButton = true;
			renderMainBackButton = false;
			sessionBackingBean.renewalViewMood = false;
			inquiryMoodUpload = false;

		}

	}

	private void loadDateVal() {
		String dateFormat = "dd/MM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Date cal01DateObj = null;
		try {
			cal01DateObj = sdf.parse(vehicleDTO.getDate1());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date cal02DateObj = null;
		try {
			cal02DateObj = sdf.parse(vehicleDTO.getDate2());
		} catch (ParseException e) {

			e.printStackTrace();
		}
		vehicleDTO.setCalender1(cal01DateObj);
		vehicleDTO.setCalender2(cal02DateObj);
	}

	private void loadValues() {

		RequestContext.getCurrentInstance().update("bla");
		RequestContext.getCurrentInstance().update("footerDiv");
		viewedDetails = inspectionActionPointService.getRecordForCurrentApplicationNo(selectedApplicationNumber);
		vehicleDTO.setPermitSeqNo(viewedDetails.getPermitSeqNo());
		vehicleDTO.setApplicationNo(viewedDetails.getApplicationNo());
		vehicleDTO.setQueueNo(viewedDetails.getQueueNo());
		vehicleDTO.setPermitNo(viewedDetails.getPermitNo());
		vehicleDTO.setVehicleNo(viewedDetails.getVehicleNo());
		vehicleDTO.setServiceTypeCode(viewedDetails.getServiceTypeCode());
		vehicleDTO.setRouteDetails(viewedDetails.getRouteDetails());
		vehicleDTO.setRouteNo(viewedDetails.getRouteNo());
		vehicleDTO.setRouteFlag(viewedDetails.getRouteFlag());
		vehicleDTO.setServiceType(viewedDetails.getServiceType());
		vehicleDTO.setPermitOwner(viewedDetails.getPermitOwner());
		vehicleDTO.setNicreg(viewedDetails.getNicreg());
		vehicleDTO.setChassisNo(viewedDetails.getChassisNo());
		vehicleDTO.setMakeTypeCode(viewedDetails.getMakeTypeCode());
		vehicleDTO.setModelTypeCode(viewedDetails.getModelTypeCode());
		vehicleDTO.setProductDate(viewedDetails.getProductDate());
		vehicleDTO.setMake(viewedDetails.getMake());
		vehicleDTO.setModel(viewedDetails.getModel());
		vehicleDTO.setManyear(viewedDetails.getManyear());
		vehicleDTO.setDate1(viewedDetails.getDate1());
		vehicleDTO.setDate2(viewedDetails.getDate2());
		vehicleDTO.setFinalremarkDescription(viewedDetails.getFinalremarkDescription());
		vehicleDTO.setServiceTypeCode(viewedDetails.getServiceTypeCode());
		setSelectedServiceType(viewedDetails.getServiceTypeCode());

		dataList = inspectionActionPointService.getAllInspectionRecordsDetails(selectedApplicationNumber);
		FacesContext fcontext = FacesContext.getCurrentInstance();
		fcontext.getExternalContext().getSessionMap().put("QUEUE_NO", vehicleDTO.getQueueNo());
		onRouteChange();
		if (vehicleDTO.getRouteFlag() != null && !vehicleDTO.getRouteFlag().trim().equals("")
				&& vehicleDTO.getRouteFlag().equalsIgnoreCase("Y")) {
			routeFlag = true;
			routeFlagListener();

		} else if (vehicleDTO.getRouteFlag() != null && !vehicleDTO.getRouteFlag().trim().equals("")
				&& vehicleDTO.getRouteFlag().equalsIgnoreCase("N")) {

		}

	}

	public String backToView() {

		if (sessionBackingBean.isVehicleInspectionType() == true) {
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			sessionBackingBean.setPageMode("V");
			sessionBackingBean.setApproveURL(request.getRequestURL().toString());
			sessionBackingBean.setSearchURL(null);
			sessionBackingBean.setApproveURLStatus(true);
			sessionBackingBean.setVehicleInspectionType(false);
			return "/pages/vehicleInspectionSet/viewVehicleInspection.xhtml#!";

		} else if (sessionBackingBean.getSelectedOptionType().equals("EDIT")) {

			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			sessionBackingBean.setPageMode("V");
			sessionBackingBean.setApproveURL(request.getRequestURL().toString());
			sessionBackingBean.setSearchURL(null);
			sessionBackingBean.setApproveURLStatus(true);
			sessionBackingBean.setVehicleInspectionType(false);
			return "/pages/vehicleInspectionSet/viewVehicleInspection.xhtml#!";

		} else {
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			sessionBackingBean.setPageMode("V");
			sessionBackingBean.setApproveURL(request.getRequestURL().toString());
			sessionBackingBean.setSearchURL(null);
			sessionBackingBean.setApproveURLStatus(true);

			return "/pages/viewPermitRenewals/viewPermitRenewalsNew.xhtml#!";
		}

	}

	public void backForRenewal() {

		String approveURL = null;
		if (sessionBackingBean.isVehicleInspectionMood() == true) {
			approveURL = sessionBackingBean.getApproveURL();
			sessionBackingBean.renewalViewMood = true;
			sessionBackingBean.setVehicleInspectionMood(false);

		} else {
			approveURL = "/InfoNTC/pages/viewPermitRenewals/viewPermitRenewalsNew.xhtml#!";
			sessionBackingBean.renewalViewMood = true;
		}
		if (approveURL != null) {
			try {
				sessionBackingBean.setApproveURLStatus(false);
				sessionBackingBean.renewalViewMood = true;
				FacesContext.getCurrentInstance().getExternalContext().redirect(approveURL);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void backForInquiry() {

		String approveURL = null;
		if (sessionBackingBean.isVehicleInspectionMood() == true) {
			approveURL = sessionBackingBean.getApproveURL();
			sessionBackingBean.setVehicleInspectionMood(false);
			sessionBackingBean.isClicked = true;

		} else {
			approveURL = "/InfoNTC/pages/viewPermitRenewals/viewPermitRenewalsNew.xhtml#!";
			sessionBackingBean.isClicked = true;
		}
		if (approveURL != null) {
			try {
				sessionBackingBean.setApproveURLStatus(false);
				sessionBackingBean.isClicked = true;
				sessionBackingBean.setGoFromInquiryDetPg(false);
				sessionBackingBean.setGoBackTOInquiryDetPg(true);
				FacesContext.getCurrentInstance().getExternalContext().redirect(approveURL);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void onRouteNoChange() {
		vehicleDTO.setRouteNo(selectedRouteNo);

		if (vehicleDTO.getRouteNo() != null && !vehicleDTO.getRouteNo().equals("")) {
			routeDTO = adminService.getDetailsbyRouteNo(vehicleDTO.getRouteNo());
			if (routeDTO.getOrigin() != null && !routeDTO.getOrigin().equals("")) {
				permitDTO.setVia(routeDTO.getVia());
				if (routeDTO.getRouteFlagVal() != null && !routeDTO.getRouteFlagVal().trim().equals("")
						&& routeDTO.getRouteFlagVal().equalsIgnoreCase("Y")) {
					routeDTO.setRouteFlag(true);
					routeFlag = true;
					routeFlagListener();
					vehicleDTO.setRouteDetails(permitDTO.getOrigin() + "-" + permitDTO.getDestination());
				} else if (routeDTO.getRouteFlagVal() != null && !routeDTO.getRouteFlagVal().trim().equals("")
						&& routeDTO.getRouteFlagVal().equalsIgnoreCase("N")) {
					permitDTO.setDestination(routeDTO.getDestination());
					permitDTO.setOrigin(routeDTO.getOrigin());
					permitDTO.setVia(routeDTO.getVia());
					vehicleDTO.setRouteDetails(permitDTO.getOrigin() + "-" + permitDTO.getDestination());
				}

			}
		} else {
			routeDTO = null;
		}
	}

	public void onRouteChange() {

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

	public void routeFlagListener() {
		String location1;
		String location2;
		if (routeFlag) {
			location1 = permitDTO.getOrigin();
			location2 = permitDTO.getDestination();
			permitDTO.setOrigin(location2);
			permitDTO.setDestination(location1);

		}

	}

	public void onServiceTypeChange() {
		vehicleDTO.setServiceType(selectedServiceType);
	}

	public void uploadPhotoMethod() {
		try {

			if (checkSession == true) {
				sessionBackingBean.isClicked = true;
			}

			FacesContext fcontext = FacesContext.getCurrentInstance();
			fcontext.getExternalContext().getSessionMap().put("VEHICLE_NO", vehicleDTO.getVehicleNo());
			fcontext.getExternalContext().getSessionMap().put("APPLICATION_NO", selectedApplicationNumber);
			fcontext.getExternalContext().getSessionMap().put("OWNER_NAME", vehicleDTO.getPermitOwner());
			fcontext.getExternalContext().getSessionMap().put("VEHICLE_INSPECTION", "view");

			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("/InfoNTC/pages/vehicleInspectionSet/uploadPhotos.xhtml");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void skipPhotoUpload() {
		// delete PM100 task from task detail table - already done
		VehicleInspectionDTO vDto = new VehicleInspectionDTO();
		vDto.setApplicationNo(selectedApplicationNumber);
		vehicleInspectionService.deleteTaskDetails(vDto, "PM100");
		/*
		 * int seqNo =
		 * commonService.updateDataInNT_T_TASK_DET(uploadImageDTO.getVehicleNo(),
		 * uploadImageDTO.getApplicationNo(), "PM101", "C");
		 * commonService.insertDataIntoTaskHistory(seqNo, uploadImageDTO.getVehicleNo(),
		 * uploadImageDTO.getApplicationNo(), sessionBackingBean.getLoginUser(),
		 * "PM101", "O");
		 */

		/** check if same task exists in task history start **/
		boolean exists = paymentVoucherService.checkPhotoUploadHistory(selectedApplicationNumber, "O");
		/** check if same task exists in task history end **/

		if (!exists) {
			commonService.updateTaskStatusCompleted(selectedApplicationNumber, "PM101",
					sessionBackingBean.getLoginUser());
		}

		commonService.updatePhotoUploadStatus(selectedApplicationNumber, "Y");

		setSucessMsg("Successfully skipped inspection.");
		RequestContext.getCurrentInstance().execute("PF('successSve').show()");

	}

	public void SaveActionInEditMode() {
		String loginUser = sessionBackingBean.getLoginUser();
		if (vehicleDTO.getPermitOwner() == null) {
			errorMsg = "Permit Owner should be entered.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else if (vehicleDTO.getNicreg() == null) {
			errorMsg = "NIC / Registration No. should be entered.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else if (vehicleDTO.getMake() == null) {
			errorMsg = "Make should be selected.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else if (vehicleDTO.getModel() == null) {
			errorMsg = "Model should be selected.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else if (selectedRouteNo.equals("") || selectedRouteNo == null) {
			errorMsg = "Route No. should be selected.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else if (permitDTO.getOrigin() == null) {
			errorMsg = "Place of Origin of the Service should be entered.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else if (permitDTO.getDestination() == null) {
			errorMsg = "Place of Destination should be entered.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			// }else if(permitDTO.getVia() == null){
			// errorMsg = "Via should be entered.";
			// RequestContext.getCurrentInstance().update("frmrequiredField");
			// RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else if (vehicleDTO.getRouteDetails() == null) {
			errorMsg = "Route Details should be entered.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else if (vehicleDTO.getProductDate() == null) {
			errorMsg = "Manufacture Year should be entered.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else if (selectedServiceType.equals("") || selectedServiceType == null) {
			errorMsg = "Service Type should be selected.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else if (vehicleDTO.getChassisNo() == null) {
			errorMsg = "Chassis No. should be entered.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else if (vehicleDTO.getApplicationNo() == null) {
			errorMsg = "Application No. should be entered.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else if ((!vehicleDTO.getPermitOwner().equals("") || vehicleDTO.getPermitOwner() != null)
				&& (!vehicleDTO.getNicreg().equals("") || vehicleDTO.getNicreg() != null)
				&& (!vehicleDTO.getMake().equals("") || vehicleDTO.getMake() != null)
				&& (!vehicleDTO.getModel().equals("") || vehicleDTO.getModel() != null)
				&& (!vehicleDTO.getRouteNo().equals("") || vehicleDTO.getRouteNo() != null)
				&& (!permitDTO.getOrigin().equals("") || permitDTO.getOrigin() != null)
				&& (!permitDTO.getDestination().equals("") || permitDTO.getDestination() != null)
				&& (!permitDTO.getVia().equals("") || permitDTO.getVia() != null)
				&& (!vehicleDTO.getRouteDetails().equals("") || vehicleDTO.getRouteDetails() != null)
				&& (!vehicleDTO.getProductDate().equals("") || vehicleDTO.getProductDate() != null)
				&& (!vehicleDTO.getServiceType().equals("") || vehicleDTO.getServiceType() != null)
				&& (!vehicleDTO.getChassisNo().equals("") || vehicleDTO.getChassisNo() != null)
				&& (!vehicleDTO.getApplicationNo().equals("") || vehicleDTO.getApplicationNo() != null)) {

			vehicleDTO.setServiceTypeCode(selectedServiceType);
			int resultApplicationNo = inspectionActionPointService.updateInspectionRecord(vehicleDTO, permitDTO,
					loginUser);
			int resultPermitOwner = inspectionActionPointService.updatePermitOwnerInspectionRecord(vehicleDTO,
					permitDTO, loginUser);
			int resultOminiBusDet = inspectionActionPointService.updateOminiBusDetInspectionRecord(vehicleDTO,
					permitDTO, loginUser);
			boolean result = inspectionActionPointService.saveDataVehicleInspecDetails(vehicleDTO, dataList,
					vehicleDTO.getApplicationNo(), loginUser);
			vehicleInspectionService.saveDataVehicleInspecDetailsHistory(vehicleDTO, dataList,
					vehicleDTO.getApplicationNo());
			if (resultApplicationNo == 0 && resultPermitOwner == 0 && resultOminiBusDet == 0 && result == true) {
				RequestContext.getCurrentInstance().update("frmsuccessSve");
				setSucessMsg("Successfully Saved.");
				RequestContext.getCurrentInstance().execute("PF('successSve').show()");
				taskDetWithAppDetDTO = new VehicleInspectionDTO();
				taskDetWithAppDetDTO = inspectionActionPointService
						.getDetailsWithModiFyDate(vehicleDTO.getApplicationNo());
				boolean checkValueWithSameTaskCode = inspectionActionPointService
						.isCheckedSameTaskCodeForSelectedApp(vehicleDTO.getApplicationNo(), "PM100", "C");
				if (checkValueWithSameTaskCode == true) {
					int resultInTaskDetWithAppDet = inspectionActionPointService.updateNewTaskDetWithAppDet(
							taskDetWithAppDetDTO, loginUser, vehicleDTO.getApplicationNo(), "PM100", "C");
				} else {
					int resultInTaskDetWithAppDet = inspectionActionPointService.insertNewTaskDetWithAppDet(
							taskDetWithAppDetDTO, loginUser, vehicleDTO.getApplicationNo(), "PM100", "C");
				}

			} else {

				setErrorMsg("Errors.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		}

	}

	public void ClearInEditMode() {

		selectedApplicationNumber = sessionBackingBean.getViewedInspectionApplicationNo();

		loadValues();
		String dateFormat = "dd/MM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Date cal01DateObj = null;
		if (vehicleDTO.getDate1() != null) {
			try {
				cal01DateObj = sdf.parse(vehicleDTO.getDate1());
				vehicleDTO.setCalender1(cal01DateObj);
			} catch (ParseException e1) {

				e1.printStackTrace();
			}
		} else {

		}

		Date cal02DateObj = null;
		if (vehicleDTO.getDate2() != null) {
			try {
				cal02DateObj = sdf.parse(vehicleDTO.getDate2());
				vehicleDTO.setCalender2(cal02DateObj);
			} catch (ParseException e) {

				e.printStackTrace();
			}
		} else {

		}
	}

	public VehicleInspectionDTO getViewedDetails() {
		return viewedDetails;
	}

	public void setViewedDetails(VehicleInspectionDTO viewedDetails) {
		this.viewedDetails = viewedDetails;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
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

	public InspectionActionPointService getInspectionActionPointService() {
		return inspectionActionPointService;
	}

	public void setInspectionActionPointService(InspectionActionPointService inspectionActionPointService) {
		this.inspectionActionPointService = inspectionActionPointService;
	}

	public String getDateOne() {
		return dateOne;
	}

	public void setDateOne(String dateOne) {
		this.dateOne = dateOne;
	}

	public String getDateTwo() {
		return dateTwo;
	}

	public void setDateTwo(String dateTwo) {
		this.dateTwo = dateTwo;
	}

	public String getSelectedApplicationNumber() {
		return selectedApplicationNumber;
	}

	public void setSelectedApplicationNumber(String selectedApplicationNumber) {
		this.selectedApplicationNumber = selectedApplicationNumber;
	}

	public String getPreSearchedApplicationNum() {
		return preSearchedApplicationNum;
	}

	public void setPreSearchedApplicationNum(String preSearchedApplicationNum) {
		this.preSearchedApplicationNum = preSearchedApplicationNum;
	}

	public String getPreSearchedVehicleNum() {
		return preSearchedVehicleNum;
	}

	public void setPreSearchedVehicleNum(String preSearchedVehicleNum) {
		this.preSearchedVehicleNum = preSearchedVehicleNum;
	}

	public boolean isCheckSearchedBtn() {
		return checkSearchedBtn;
	}

	public void setCheckSearchedBtn(boolean checkSearchedBtn) {
		this.checkSearchedBtn = checkSearchedBtn;
	}

	public Boolean getRouteFlag() {
		return routeFlag;
	}

	public void setRouteFlag(Boolean routeFlag) {
		this.routeFlag = routeFlag;
	}

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	public PermitDTO getPermitDTO() {
		return permitDTO;
	}

	public void setPermitDTO(PermitDTO permitDTO) {
		this.permitDTO = permitDTO;
	}

	public RouteDTO getRouteDTO() {
		return routeDTO;
	}

	public void setRouteDTO(RouteDTO routeDTO) {
		this.routeDTO = routeDTO;
	}

	public boolean isInquiryMoodUpload() {
		return inquiryMoodUpload;
	}

	public void setInquiryMoodUpload(boolean inquiryMoodUpload) {
		this.inquiryMoodUpload = inquiryMoodUpload;
	}

	public boolean isReadOnlyValue() {
		return readOnlyValue;
	}

	public void setReadOnlyValue(boolean readOnlyValue) {
		this.readOnlyValue = readOnlyValue;
	}

	public boolean isDisabledActPointCheckedBox() {
		return disabledActPointCheckedBox;
	}

	public void setDisabledActPointCheckedBox(boolean disabledActPointCheckedBox) {
		this.disabledActPointCheckedBox = disabledActPointCheckedBox;
	}

	public boolean isHideField() {
		return hideField;
	}

	public void setHideField(boolean hideField) {
		this.hideField = hideField;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getSelectedRouteNo() {
		return selectedRouteNo;
	}

	public void setSelectedRouteNo(String selectedRouteNo) {
		this.selectedRouteNo = selectedRouteNo;
	}

	public List<VehicleInspectionDTO> getRouteNoList() {
		return routeNoList;
	}

	public void setRouteNoList(List<VehicleInspectionDTO> routeNoList) {
		this.routeNoList = routeNoList;
	}

	public List<VehicleInspectionDTO> getServiceTypeList() {
		return serviceTypeList;
	}

	public void setServiceTypeList(List<VehicleInspectionDTO> serviceTypeList) {
		this.serviceTypeList = serviceTypeList;
	}

	public String getSelectedServiceType() {
		return selectedServiceType;
	}

	public void setSelectedServiceType(String selectedServiceType) {
		this.selectedServiceType = selectedServiceType;
	}

	public String getSucessMsg() {
		return sucessMsg;
	}

	public void setSucessMsg(String sucessMsg) {
		this.sucessMsg = sucessMsg;
	}

	public boolean isCheckSession() {
		return checkSession;
	}

	public void setCheckSession(boolean checkSession) {
		this.checkSession = checkSession;
	}

	public boolean isRenderInquiryBackButton() {
		return renderInquiryBackButton;
	}

	public void setRenderInquiryBackButton(boolean renderInquiryBackButton) {
		this.renderInquiryBackButton = renderInquiryBackButton;
	}

	public boolean isRenderRenewalBackButton() {
		return renderRenewalBackButton;
	}

	public void setRenderRenewalBackButton(boolean renderRenewalBackButton) {
		this.renderRenewalBackButton = renderRenewalBackButton;
	}

	public boolean isRenderMainBackButton() {
		return renderMainBackButton;
	}

	public void setRenderMainBackButton(boolean renderMainBackButton) {
		this.renderMainBackButton = renderMainBackButton;
	}

	public VehicleInspectionService getVehicleInspectionService() {
		return vehicleInspectionService;
	}

	public void setVehicleInspectionService(VehicleInspectionService vehicleInspectionService) {
		this.vehicleInspectionService = vehicleInspectionService;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public PaymentVoucherService getPaymentVoucherService() {
		return paymentVoucherService;
	}

	public void setPaymentVoucherService(PaymentVoucherService paymentVoucherService) {
		this.paymentVoucherService = paymentVoucherService;
	}

	public String getPhotoUploadStatus() {
		return photoUploadStatus;
	}

	public void setPhotoUploadStatus(String photoUploadStatus) {
		this.photoUploadStatus = photoUploadStatus;
	}

	public VehicleInspectionDTO getTaskDetWithAppDetDTO() {
		return taskDetWithAppDetDTO;
	}

	public void setTaskDetWithAppDetDTO(VehicleInspectionDTO taskDetWithAppDetDTO) {
		this.taskDetWithAppDetDTO = taskDetWithAppDetDTO;
	}

}
