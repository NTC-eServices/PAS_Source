package lk.informatics.ntc.view.beans;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
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

import lk.informatics.ntc.model.dto.AccidentDTO;
import lk.informatics.ntc.model.dto.AfterAccidentDTO;
import lk.informatics.ntc.model.dto.AmendmentBusOwnerDTO;
import lk.informatics.ntc.model.dto.AmendmentDTO;
import lk.informatics.ntc.model.dto.AmendmentOminiBusDTO;
import lk.informatics.ntc.model.dto.AmendmentServiceDTO;
import lk.informatics.ntc.model.dto.BusOwnerDTO;
import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.ComplaintRequestDTO;
import lk.informatics.ntc.model.dto.DocumentManagementDTO;
import lk.informatics.ntc.model.dto.FluingSquadVioConditionDTO;
import lk.informatics.ntc.model.dto.FlyingManageInvestigationLogDTO;
import lk.informatics.ntc.model.dto.FlyingSquadVioDocumentsDTO;
import lk.informatics.ntc.model.dto.OminiBusDTO;
import lk.informatics.ntc.model.dto.OrganizationDTO;
import lk.informatics.ntc.model.dto.PaymentVoucherDTO;
import lk.informatics.ntc.model.dto.PermitRenewalsDTO;
import lk.informatics.ntc.model.dto.RouteDTO;
import lk.informatics.ntc.model.dto.SetupCommitteeBoardDTO;
import lk.informatics.ntc.model.dto.SimRegistrationDTO;
import lk.informatics.ntc.model.exception.ApplicationException;
import lk.informatics.ntc.model.service.AdminService;
import lk.informatics.ntc.model.service.AmendmentService;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.DocumentManagementService;
import lk.informatics.ntc.model.service.EmployeeProfileService;
import lk.informatics.ntc.model.service.FlyingSquadChargeSheetService;
import lk.informatics.ntc.model.service.IssuePermitService;
import lk.informatics.ntc.model.service.ManageInquiryService;
import lk.informatics.ntc.model.service.MigratedService;
import lk.informatics.ntc.model.service.QueueManagementService;
import lk.informatics.ntc.model.service.SetupCommitteeBoardService;
import lk.informatics.ntc.model.service.VehicleInspectionService;
import lk.informatics.ntc.view.util.PropertyReader;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "viewAmendmentBackingBean")
@ViewScoped

public class ViewAmendmentBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private AmendmentDTO amendmentDTO;
	private AmendmentBusOwnerDTO amendmentBusOwnerDTO;
	private AmendmentOminiBusDTO amendmentOminiBusDTO;
	private OminiBusDTO ominiBusDTO;
	private RouteDTO routeDTO;
	private BusOwnerDTO busOwnerDTO;
	private PermitRenewalsDTO checkAndDisplayRemarkValue;
	private PaymentVoucherDTO paymentVoucherDTO;
	private SetupCommitteeBoardDTO setupCommitteeBoardDTO;
	private OrganizationDTO organizationDTO;
	private AccidentDTO accidentDTO;
	private CommonDTO commonDTO;
	private AmendmentServiceDTO amendmentServiceDTO;
	private AmendmentServiceDTO tempNewRouteDTO;
	private AfterAccidentDTO afterAccidentDTO;
	private PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();
	private boolean renderBack = false;
	private boolean renderThreeButtons = true;

	private List<CommonDTO> provincelList;
	private List<CommonDTO> districtList;
	private List<CommonDTO> divSecList;

	private List<CommonDTO> relationshipList;

	private List<CommonDTO> routefordropdownList;

	private List<CommonDTO> titleList;
	private List<CommonDTO> genderList;
	private List<CommonDTO> martialList;

	public List<PermitRenewalsDTO> dataList = new ArrayList<PermitRenewalsDTO>(0);

	private List<DocumentManagementDTO> mandatoryList = new ArrayList<DocumentManagementDTO>();
	private List<DocumentManagementDTO> optionalList = new ArrayList<DocumentManagementDTO>();
	private List<SetupCommitteeBoardDTO> organizationList = new ArrayList<SetupCommitteeBoardDTO>(0);
	private List<OrganizationDTO> otherOrgList = new ArrayList<OrganizationDTO>(0);
	private List<CommonDTO> accidentTypesList = new ArrayList<CommonDTO>(0);
	private List<AccidentDTO> accidentList = new ArrayList<AccidentDTO>(0);
	private List<AccidentDTO> moreAccidentList = new ArrayList<AccidentDTO>(0);
	private List<CommonDTO> counterList = new ArrayList<>();

	private SimRegistrationDTO simRegistrationDTO;
	private List<SimRegistrationDTO> emiDetViewList = new ArrayList<>();
	private ManageInquiryService manageInquiryService;

	/** for investigation pop up **/

	private FlyingSquadChargeSheetService flyingSquadChargeSheetService;
	private ArrayList<FluingSquadVioConditionDTO> conditionList;
	private ArrayList<FlyingSquadVioDocumentsDTO> documentList;
	private FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO;

	private List<FlyingManageInvestigationLogDTO> investigationDetViewList = new ArrayList<>();
	private FlyingManageInvestigationLogDTO viewInvSelect;
	private FlyingManageInvestigationLogDTO selectedInvDTO;

	/** end investigation pop up **/
	/** for public complain **/
	private ComplaintRequestDTO publicComplanDto;
	private List<ComplaintRequestDTO> complainDetViewList = new ArrayList<>();
	private boolean renderComplainDet;
	private ComplaintRequestDTO viewSelect;
	private ComplaintRequestDTO selectedComplaintDTO;

	/** end for public complain **/

	private AmendmentService amendmentService;
	private AdminService adminService;
	private IssuePermitService issuePermitService;
	private QueueManagementService queueManagementService;
	private CommonService commonService;
	private MigratedService migratedService;
	private VehicleInspectionService vehicleInspectionService;
	private DocumentManagementService documentManagementService;
	private EmployeeProfileService employeeProfileService;
	public SetupCommitteeBoardService setupCommitteeBoardService;

	private String strVehicleNo;
	private String strPermitNo;
	private String strQueueNo;
	private String strAmendmentType;
	private String generatedApplicationNo;

	private String strTrnCode;
	private String strTrnDesc;

	private int activeTabIndex;

	private int selectedOtherOrg;
	private int selectedAccident;
	private int selectedMoreAccident;

	private String errorMsg;

	private boolean oldOwner;
	private boolean newOwner;
	private boolean oldBus;
	private boolean newBus;
	private boolean serviceChange;
	private boolean finalCheckList;
	private boolean renderCancel = true;
	private boolean renderSearch = true;
	private boolean renderBackToAmendmentsApproval = false;

	private boolean routeFlag;

	private boolean loanObtained;

	private boolean callNext = false;
	private boolean skip = true;

	private boolean localcheckcounter = true;

	private boolean savedNewOminiBus = false;

	private boolean otherPermits = false;

	private boolean showInquiryBack = false;

	private boolean renderHighAuthBackButton = false;

	public ViewAmendmentBackingBean() {

	}

	@PostConstruct
	public void init() {
		amendmentDTO = new AmendmentDTO();
		ominiBusDTO = new OminiBusDTO();
		routeDTO = new RouteDTO();
		busOwnerDTO = new BusOwnerDTO();
		checkAndDisplayRemarkValue = new PermitRenewalsDTO();
		paymentVoucherDTO = new PaymentVoucherDTO();
		amendmentBusOwnerDTO = new AmendmentBusOwnerDTO();
		amendmentOminiBusDTO = new AmendmentOminiBusDTO();
		organizationDTO = new OrganizationDTO();
		accidentDTO = new AccidentDTO();
		commonDTO = new CommonDTO();
		amendmentServiceDTO = new AmendmentServiceDTO();
		tempNewRouteDTO = new AmendmentServiceDTO();
		afterAccidentDTO = new AfterAccidentDTO();
		viewSelect = new ComplaintRequestDTO();

		oldOwner = false;
		newOwner = false;
		oldBus = false;
		newBus = false;
		serviceChange = false;
		finalCheckList = false;

		routeFlag = false;

		strVehicleNo = null;
		strPermitNo = null;
		strQueueNo = null;
		strAmendmentType = null;
		generatedApplicationNo = null;

		errorMsg = null;
		strTrnCode = null;
		strTrnDesc = null;

		localcheckcounter = sessionBackingBean.isCounterCheck();

		callNext = false;
		skip = true;
		loanObtained = false;

		issuePermitService = (IssuePermitService) SpringApplicationContex.getBean("issuePermitService");
		amendmentService = (AmendmentService) SpringApplicationContex.getBean("amendmentService");
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");
		queueManagementService = (QueueManagementService) SpringApplicationContex.getBean("queueManagementService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		migratedService = (MigratedService) SpringApplicationContex.getBean("migratedService");
		vehicleInspectionService = (VehicleInspectionService) SpringApplicationContex
				.getBean("vehicleInspectionService");
		documentManagementService = (DocumentManagementService) SpringApplicationContex
				.getBean("documentManagementService");
		employeeProfileService = (EmployeeProfileService) SpringApplicationContex.getBean("employeeProfileService");
		setupCommitteeBoardService = (SetupCommitteeBoardService) SpringApplicationContex
				.getBean("setupCommitteeBoardService");
		manageInquiryService = (ManageInquiryService) SpringApplicationContex.getBean("manageInquiryService");
		flyingSquadChargeSheetService = ((FlyingSquadChargeSheetService) SpringApplicationContex
				.getBean("flyingSquadChargeSheetService"));

		provincelList = adminService.getProvinceToDropdown();

		relationshipList = amendmentService.getRelationships();

		routefordropdownList = issuePermitService.getRoutesToDropdown();

		titleList = employeeProfileService.GetTitleToDropdown();
		genderList = employeeProfileService.GetGenderToDropdown();
		martialList = employeeProfileService.GetMartialToDropdown();

		organizationList = setupCommitteeBoardService.getOrganizationListList();

		accidentTypesList = amendmentService.getAccidentTypesList();

		counterList = commonService.countersDropdown("13,05,14,15,16,21,22,23");

		if (sessionBackingBean.amendmentsViewMood == true) {
			setGeneratedApplicationNo(sessionBackingBean.getAmendmentsApplicationNo());
			searchByApplicationNo();
			renderCancel = false;
			renderSearch = false;
			renderBackToAmendmentsApproval = true;
			sessionBackingBean.amendmentsViewMood = false;
			renderHighAuthBackButton = true;
			setRenderThreeButtons(false);

		}

		if (sessionBackingBean.isCommitteeView() == true) {

			sessionBackingBean.setCommitteeView(false);
			setRenderBack(true);
			setRenderThreeButtons(false);
			generatedApplicationNo = sessionBackingBean.getCommitteeApplicationNo();
			searchByApplicationNo();
		}

		RequestContext.getCurrentInstance().update("btnBack");
		if (sessionBackingBean.isClicked == true) {

			generatedApplicationNo = sessionBackingBean.getApplicationNo();
			searchByApplicationNo();
			sessionBackingBean.isClicked = false;
			sessionBackingBean.setInquiryTYpe(true);
			setShowInquiryBack(true);

		}

	}

	public void callNext() {
		String queueNo = queueManagementService.callNextQueueNumberAction("13,05,14,15,16,21,22,23", null);
		strQueueNo = queueNo;

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		String strCurrentDate = dateFormat.format(date).toString();

		if (strQueueNo != null && !strQueueNo.isEmpty() && !strQueueNo.trim().equalsIgnoreCase("")) {

			strVehicleNo = vehicleInspectionService.getVehicleNo(queueNo);

			migratedService.updateStatusOfQueueNumberAfterCallNext(strQueueNo, "O");

			callNext = true;
			skip = false;

			migratedService.updateCounterIdOfQueueNumberAfterCallNext(strQueueNo, sessionBackingBean.getCounterId());

			commonService.updateCounterQueueNo(strQueueNo, sessionBackingBean.getCounterId());

			if (strVehicleNo != null && !strVehicleNo.isEmpty() && !strVehicleNo.trim().equalsIgnoreCase("")) {
				getApplicationDetailsByVehicleNo();
			}

		} else {

			strCurrentDate = null;
			errorMsg = "The queue is empty";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void searchByApplicationNo() {

		if (generatedApplicationNo != null && generatedApplicationNo != " " && !generatedApplicationNo.isEmpty()) {
			
			/**if generated application is in Active status strVehicleNo is new bus number other wise old vehicle number***/
			if(amendmentService.getApplicationStatus(generatedApplicationNo).equals("A")) {
				strVehicleNo=amendmentService.getNewVehicleNoFromAmendment(generatedApplicationNo);
			}
			else {
			strVehicleNo = amendmentService.getOldVehicleNoFromAmendment(generatedApplicationNo);
			}

			ominiBusDTO.setVehicleRegNo(amendmentService.getNewVehicleNoFromAmendment(generatedApplicationNo));

			if (strVehicleNo != null && !strVehicleNo.isEmpty() && !strVehicleNo.trim().equalsIgnoreCase("")) {
				callNext = true;
				getApplicationDetailsByVehicleNo();
				if (ominiBusDTO.getVehicleRegNo() != null && !ominiBusDTO.getVehicleRegNo().isEmpty()
						&& !ominiBusDTO.getVehicleRegNo().trim().equalsIgnoreCase("")) {
					permitRenewalsDTO.setRegNoOfBus(ominiBusDTO.getVehicleRegNo());
					getRenewalDetails();
				}
			}

		} else {

			errorMsg = "Application No. is required";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		}

	}

	private void getRenewalDetails() {
		OminiBusDTO omniDetDto = new OminiBusDTO();
		OminiBusDTO omniDetDtoTwo = new OminiBusDTO();
		if (!strVehicleNo.equals("") && strVehicleNo != null) {

			permitRenewalsDTO = amendmentService.renewalsByBusRegNoNew(permitRenewalsDTO.getRegNoOfBus(),
					generatedApplicationNo);
			omniDetDto = amendmentService.insuranceDetByBusRegNoNew(strVehicleNo, generatedApplicationNo);
			permitRenewalsDTO.setEmissionExpDate(omniDetDto.getEmissionExpDate());
			permitRenewalsDTO.setRevenueExpDate(omniDetDto.getExpiryDateRevLic());
			permitRenewalsDTO.setInsuCompName(omniDetDto.getInsuCompName());
			permitRenewalsDTO.setInsuExpDate(omniDetDto.getInsuExpDate());
			permitRenewalsDTO.setInsuCat(omniDetDto.getInsuCat());
			permitRenewalsDTO.setPolicyNo(omniDetDto.getPolicyNo());
			
	
			
			omniDetDtoTwo =amendmentService.emmissionDetByAppNo(generatedApplicationNo);
			permitRenewalsDTO.setSerialNo(omniDetDtoTwo.getSerialNo());
			permitRenewalsDTO.setGarageName(omniDetDtoTwo.getGarageName());
			permitRenewalsDTO.setGarageRegNo(omniDetDtoTwo.getGarageRegNo());
			permitRenewalsDTO.setFitnessCertiDate(omniDetDtoTwo.getFitnessCertiDate());
			
			permitRenewalsDTO.setExpiryDateRevLicNew(omniDetDtoTwo.getExpiryDateRevLicNew());
			permitRenewalsDTO.setEmmissionTestExpireDate(omniDetDtoTwo.getEmmissionTestExpireDate());

			if (omniDetDto.getInsuCat() == null) {
				omniDetDto.setInsuCat("UNLIMITED");
				permitRenewalsDTO.setInsuCat(omniDetDto.getInsuCat());
			} else if (omniDetDto.getInsuCat() != null) {
				omniDetDto.setInsuCat(omniDetDto.getInsuCat());
				permitRenewalsDTO.setInsuCat(omniDetDto.getInsuCat());
			}
		}
	}

	public void backToAmendmentsApproval() {

		String approveURL = sessionBackingBean.getApproveURL();
		String searchURL = sessionBackingBean.getSearchURL();

		if (approveURL != null) {

			try {
				sessionBackingBean.setApproveURLStatus(false);
				sessionBackingBean.amendmentsViewMood = false;
				FacesContext.getCurrentInstance().getExternalContext().redirect(approveURL);
			} catch (IOException e) {

				e.printStackTrace();
			}

		} else if (searchURL != null) {

			try {
				sessionBackingBean.setSearchURLStatus(false);
				sessionBackingBean.amendmentsViewMood = false;
				FacesContext.getCurrentInstance().getExternalContext().redirect(searchURL);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		setRenderBackToAmendmentsApproval(false);
		setRenderThreeButtons(true);
		RequestContext.getCurrentInstance().update("btnBack");
	}

	public void getApplicationDetailsByVehicleNo() {

		strTrnCode = amendmentService.getTrnTypeFromAmendment(generatedApplicationNo);

		strTrnDesc = amendmentService.getTransactionTypeDesc(strTrnCode);

		// bus=13
		// service=05
		// owner=14
		// owner & bus=15
		// bus & service=16

		switch (strTrnCode) {
		case "13":
			strAmendmentType = strTrnDesc;
			// oldOwner = true;
			oldBus = true;
			newBus = true;
			finalCheckList = true;
			break;
		case "05":
			strAmendmentType = strTrnDesc;
			oldOwner = true;
			oldBus = true;
			serviceChange = true;
			finalCheckList = true;
			break;
		case "21":
			strAmendmentType = strTrnDesc;
			oldOwner = true;
			oldBus = true;
			serviceChange = true;
			finalCheckList = true;
			amendmentDTO.setServiceChangeType("S");
			break;
		case "22":
			strAmendmentType = strTrnDesc;
			oldOwner = true;
			oldBus = true;
			serviceChange = true;
			finalCheckList = true;
			amendmentDTO.setServiceChangeType("R");
			break;
		case "23":
			strAmendmentType = strTrnDesc;
			oldOwner = true;
			oldBus = true;
			serviceChange = true;
			finalCheckList = true;
			amendmentDTO.setServiceChangeType("E");
			break;
		case "14":
			strAmendmentType = strTrnDesc;
			oldOwner = true;
			// oldBus = true;
			newOwner = true;
			finalCheckList = true;
			break;
		case "15":
			strAmendmentType = strTrnDesc;
			oldOwner = true;
			newOwner = true;
			oldBus = true;
			newBus = true;
			finalCheckList = true;
			break;
		case "16":
			strAmendmentType = strTrnDesc;
			// oldOwner = true;
			oldBus = true;
			newBus = true;
			serviceChange = true;
			finalCheckList = true;
			break;
		default:
			strAmendmentType = null;
			// finalCheckList = true;
			break;
		}

		if (strVehicleNo != null && !strVehicleNo.isEmpty() && strVehicleNo != "") {

			/** newly added tharushi.e 2020-07-03 **/
			if (strTrnCode.equals("13") || strTrnCode == "13" || strTrnCode.equals("14") || strTrnCode == "14") {
				amendmentBusOwnerDTO = amendmentService.getApplicationDetailsByVehicleNo(strVehicleNo);
			} else {
				amendmentBusOwnerDTO = amendmentService.getApplicationDetailsByVehicleNoAndAppNo(strVehicleNo,
						generatedApplicationNo);// modify by tharushi.e
			}

			/** end **/

			/** change by tharushi.e for get route no for current app number */

//			check 113 history changed on 18-01-2022
			boolean checkAM113Exists = amendmentService.checkAM113Exists(generatedApplicationNo);
			if(checkAM113Exists) {
				amendmentOminiBusDTO = amendmentService.ominiBusByVehicleNoOldData(strVehicleNo);
			} else {
				amendmentOminiBusDTO = amendmentService.ominiBusByVehicleNo(strVehicleNo);
			}

			/** added by tharushi.e for temp route issue **/
			AmendmentDTO existRouteDetDTO = new AmendmentDTO();
			existRouteDetDTO = amendmentService.getRouteNO(generatedApplicationNo);
			amendmentOminiBusDTO.setRouteNo(existRouteDetDTO.getRouteNo());
			amendmentOminiBusDTO.setOrigin(existRouteDetDTO.getOrigin());
			amendmentOminiBusDTO.setDestination(existRouteDetDTO.getDestination());
			amendmentOminiBusDTO.setVia(existRouteDetDTO.getVia());
			/** end **/
			amendmentDTO.setRouteNo(amendmentOminiBusDTO.getRouteNo());

			amendmentBusOwnerDTO.setVehicleRegNo(strVehicleNo);

			busOwnerDTO.setBusRegNo(strVehicleNo);
			strPermitNo = amendmentService.getPermitNoByVehicleNo(strVehicleNo);
			amendmentBusOwnerDTO.setPermitNo(strPermitNo);

			busOwnerDTO.setPermitNo(amendmentBusOwnerDTO.getPermitNo());
			busOwnerDTO.setIsBacklogApp(amendmentBusOwnerDTO.getIsBacklogApp());
			amendmentBusOwnerDTO.setBusRegNo(strVehicleNo);

			onRouteChange();

			if (amendmentBusOwnerDTO.getRouteNo() != null && !amendmentBusOwnerDTO.getRouteNo().equals("")) {
				routeDTO = issuePermitService.getDetailsbyRouteNo(amendmentBusOwnerDTO.getRouteNo());
				if (routeDTO.getOrigin() != null && !routeDTO.getOrigin().equals("")) {
					amendmentBusOwnerDTO.setDestination(routeDTO.getDestination());
					amendmentBusOwnerDTO.setOrigin(routeDTO.getOrigin());
				}

				if (amendmentBusOwnerDTO.getRouteFlag() != null
						&& !amendmentBusOwnerDTO.getRouteFlag().trim().equals("")
						&& amendmentBusOwnerDTO.getRouteFlag().equalsIgnoreCase("Y")) {
					routeFlag = true;
					routeFlagListener();

				} else if (amendmentBusOwnerDTO.getRouteFlag() != null
						&& !amendmentBusOwnerDTO.getRouteFlag().trim().equals("")
						&& amendmentBusOwnerDTO.getRouteFlag().equalsIgnoreCase("N")) {

				}

			} else {
				routeDTO = null;
			}

			if (amendmentOminiBusDTO.getRouteNo() != null && !amendmentOminiBusDTO.getRouteNo().equals("")) {
				routeDTO = issuePermitService.getDetailsbyRouteNo(amendmentOminiBusDTO.getRouteNo());
				if (routeDTO.getOrigin() != null && !routeDTO.getOrigin().equals("")) {
					amendmentOminiBusDTO.setDestination(routeDTO.getDestination());
					amendmentOminiBusDTO.setOrigin(routeDTO.getOrigin());
				}

				if (amendmentOminiBusDTO.getRouteFlag() != null
						&& !amendmentOminiBusDTO.getRouteFlag().trim().equals("")
						&& amendmentOminiBusDTO.getRouteFlag().equalsIgnoreCase("Y")) {
					routeFlag = true;
					routeFlagListener();

				} else if (amendmentOminiBusDTO.getRouteFlag() != null
						&& !amendmentOminiBusDTO.getRouteFlag().trim().equals("")
						&& amendmentOminiBusDTO.getRouteFlag().equalsIgnoreCase("N")) {

				}

			} else {
				routeDTO = null;
			}

			getOminiBusDetails();

			otherOrgList = amendmentService.getOtherOrgList(generatedApplicationNo);

			accidentList = amendmentService.getAccidentList(ominiBusDTO.getVehicleRegNo());

			moreAccidentList = amendmentService.getMoreAccidentList(ominiBusDTO.getVehicleRegNo());

			afterAccidentDTO = amendmentService.getAfterAccident(ominiBusDTO.getVehicleRegNo());

			amendmentServiceDTO = amendmentService.getServiceChange(generatedApplicationNo);
			onNewRouteChange();
			busOwnerDTO = amendmentService.getOwnerDetails(generatedApplicationNo);

			amendmentDTO = amendmentService.getAmendmentDetails(generatedApplicationNo);

			tempNewRouteDTO = amendmentService.getNewRouteRequest(generatedApplicationNo);
			/**
			 * modify by tharushi.e
			 */
			if (!strTrnCode.equals("22")) {
				tempNewRouteDTO = amendmentService.getNewRouteRequestEndChange(generatedApplicationNo);
			}
			displayCheckDocumentDataList();

		}

	}

	public void saveActionTaskHandler() {
		boolean taskStatus;
		taskStatus = commonService.checkTaskOnTaskDetails(generatedApplicationNo, "AM100", "O");
		if (!taskStatus) {
			commonService.insertTaskDet(strVehicleNo, generatedApplicationNo, sessionBackingBean.getLoginUser(),
					"AM100", "O");
		}
	}

	public void routeFlagListener() {
		String location1;
		String location2;
		if (routeFlag) {
			location1 = amendmentBusOwnerDTO.getOrigin();
			location2 = amendmentBusOwnerDTO.getDestination();
			amendmentBusOwnerDTO.setOrigin(location2);
			amendmentBusOwnerDTO.setDestination(location1);

		}

		if (routeFlag) {
			location1 = amendmentOminiBusDTO.getOrigin();
			location2 = amendmentOminiBusDTO.getDestination();
			amendmentOminiBusDTO.setOrigin(location2);
			amendmentOminiBusDTO.setDestination(location1);

		}

		if (routeFlag) {
			location1 = amendmentDTO.getOrigin();
			location2 = amendmentDTO.getDestination();
			amendmentDTO.setOrigin(location2);
			amendmentDTO.setDestination(location1);

		}
	}

	public void newRouteFlagListener() {
		String location1;
		String location2;
		if (amendmentServiceDTO.isTheRouteFlag()) {
			location1 = amendmentServiceDTO.getOrigin();
			location2 = amendmentServiceDTO.getDestination();
			amendmentServiceDTO.setOrigin(location2);
			amendmentServiceDTO.setDestination(location1);

		}

	}

	public void endChangeListener() {
		String location1;
		String location2;

		location1 = amendmentDTO.getOrigin();
		location2 = amendmentDTO.getDestination();
		amendmentDTO.setOrigin(location2);
		amendmentDTO.setDestination(location1);

	}

	public void saveRelationNonRelation() {

		busOwnerDTO.setCreatedBy(sessionBackingBean.getLoginUser());
		amendmentDTO.setCreatedBy(sessionBackingBean.getLoginUser());

		busOwnerDTO.setBusRegNo(strVehicleNo);
		busOwnerDTO.setPermitNo(amendmentBusOwnerDTO.getPermitNo());

		amendmentDTO.setBusRegNo(strVehicleNo);
		amendmentDTO.setTrnType(strTrnCode);
		amendmentDTO.setPermitNo(amendmentBusOwnerDTO.getPermitNo());
		amendmentDTO.setQueueNo(strQueueNo);

		busOwnerDTO.setQueueNo(strQueueNo);
		busOwnerDTO.setIsBacklogApp(amendmentBusOwnerDTO.getIsBacklogApp());

		if (generatedApplicationNo == null || generatedApplicationNo.isEmpty()
				|| generatedApplicationNo.equalsIgnoreCase("")) {
			generatedApplicationNo = amendmentService.generateApplicationNo();
			busOwnerDTO.setApplicationNo(generatedApplicationNo);
			amendmentService.saveDataApplication(busOwnerDTO);
		}

		busOwnerDTO.setApplicationNo(generatedApplicationNo);

		amendmentDTO.setApplicationNo(generatedApplicationNo);

		amendmentService.saveOwnerDetails(busOwnerDTO);

		busOwnerDTO = amendmentService.getOwnerDetails(generatedApplicationNo);

		saveActionTaskHandler();

		if (amendmentDTO.getSeq() == 0) {
			amendmentService.saveAmendmentRelationshipInformation(amendmentDTO);
		} else {
			amendmentDTO.setModifiedBy(sessionBackingBean.getLoginUser());
			amendmentService.updateAmendmentDetails(amendmentDTO);
		}

		amendmentDTO = amendmentService.getAmendmentDetails(generatedApplicationNo);

		migratedService.updateStatusOfQueApp(strQueueNo, generatedApplicationNo);
		migratedService.updateTransactionTypeCodeForQueueNo(strQueueNo, strTrnCode);
		migratedService.updateQueueNumberTaskInQueueMaster(strQueueNo, generatedApplicationNo, "AM100", "O");

		displayCheckDocumentDataList();

		RequestContext.getCurrentInstance().update("frmtab07");
		RequestContext.getCurrentInstance().execute("PF('successSve').show()");

	}

	private void updateOwner() {

		busOwnerDTO.setModifiedBy(sessionBackingBean.getLoginUser());
		amendmentDTO.setModifiedBy(sessionBackingBean.getLoginUser());

		int result = amendmentService.updateBusOwner(busOwnerDTO);

		if (result == 0) {

			busOwnerDTO = amendmentService.getOwnerDetails(generatedApplicationNo);

			amendmentService.updateAmendmentDetails(amendmentDTO);

			amendmentDTO = amendmentService.getAmendmentDetails(generatedApplicationNo);

			RequestContext.getCurrentInstance().update("frmtab07");
			RequestContext.getCurrentInstance().execute("PF('successSve').show()");
		} else {
			RequestContext.getCurrentInstance().execute("PF('generalError').show()");
		}
	}

	public void getOwnerAndAmendmentDetails() {

		busOwnerDTO = amendmentService.getOwnerDetails(generatedApplicationNo);

		amendmentDTO = amendmentService.getAmendmentDetails(generatedApplicationNo);

	}

	public void clearOwnerAndAmendmentDetails() {

		busOwnerDTO = new BusOwnerDTO();
		amendmentDTO.setReasonForOwnerChange(null);
		amendmentDTO.setRelationshipWithTransferor(null);
		amendmentDTO.setRelationshipWithTransferorRemarks(null);

	}

	public void busOwnerSave() {
		if (amendmentDTO.getRelationshipWithTransferor() != null
				&& !amendmentDTO.getRelationshipWithTransferor().isEmpty()
				&& !amendmentDTO.getRelationshipWithTransferor().equalsIgnoreCase("")) {
			if (busOwnerDTO.getTitle() != null && !busOwnerDTO.getTitle().isEmpty()
					&& !busOwnerDTO.getTitle().equalsIgnoreCase("")) {
				if (busOwnerDTO.getGender() != null && !busOwnerDTO.getGender().isEmpty()
						&& !busOwnerDTO.getGender().equalsIgnoreCase("")) {
					if (busOwnerDTO.getDob() != null) {
						if (busOwnerDTO.getFullName() != null && !busOwnerDTO.getFullName().isEmpty()
								&& !busOwnerDTO.getFullName().equalsIgnoreCase("")) {
							if (busOwnerDTO.getMaritalStatus() != null && !busOwnerDTO.getMaritalStatus().isEmpty()
									&& !busOwnerDTO.getMaritalStatus().equalsIgnoreCase("")) {
								if (busOwnerDTO.getAddress1() != null && !busOwnerDTO.getAddress1().isEmpty()
										&& !busOwnerDTO.getAddress1().equalsIgnoreCase("")) {
									if (busOwnerDTO.getAddress2() != null && !busOwnerDTO.getAddress2().isEmpty()
											&& !busOwnerDTO.getAddress2().equalsIgnoreCase("")) {
										if (busOwnerDTO.getCity() != null && !busOwnerDTO.getCity().isEmpty()
												&& !busOwnerDTO.getCity().equalsIgnoreCase("")) {
											if (busOwnerDTO.getProvince() != null
													&& !busOwnerDTO.getProvince().isEmpty()
													&& !busOwnerDTO.getProvince().equalsIgnoreCase("")) {
												if (busOwnerDTO.getNicNo() != null && !busOwnerDTO.getNicNo().isEmpty()
														&& !busOwnerDTO.getNicNo().equalsIgnoreCase("")) {
													if (busOwnerDTO.getPerferedLanguage().equals("S")) {
														if (busOwnerDTO.getFullNameSinhala() != null
																&& !busOwnerDTO.getFullNameSinhala().isEmpty()
																&& !busOwnerDTO.getFullNameSinhala()
																		.equalsIgnoreCase("")) {
															if (busOwnerDTO.getAddress1Sinhala() != null
																	&& !busOwnerDTO.getAddress1Sinhala().isEmpty()
																	&& !busOwnerDTO.getAddress1Sinhala()
																			.equalsIgnoreCase("")) {
																if (busOwnerDTO.getAddress2Sinhala() != null
																		&& !busOwnerDTO.getAddress2Sinhala().isEmpty()
																		&& !busOwnerDTO.getAddress2Sinhala()
																				.equalsIgnoreCase("")) {
																	if (busOwnerDTO.getAddress2Sinhala() != null
																			&& !busOwnerDTO.getAddress2Sinhala()
																					.isEmpty()
																			&& !busOwnerDTO.getAddress2Sinhala()
																					.equalsIgnoreCase("")) {
																		if (busOwnerDTO.getSeq() != 0) {
																			// Update
																			updateOwner();
																		} else {
																			// save
																			saveRelationNonRelation();
																		}
																	} else {
																		errorMsg = "City (Sinhala) should be entered.";
																		RequestContext.getCurrentInstance()
																				.update("frmrequiredField");
																		RequestContext.getCurrentInstance()
																				.execute("PF('requiredField').show()");
																	}
																} else {
																	errorMsg = "Address 2 (Sinhala) should be entered.";
																	RequestContext.getCurrentInstance()
																			.update("frmrequiredField");
																	RequestContext.getCurrentInstance()
																			.execute("PF('requiredField').show()");
																}
															} else {
																errorMsg = "Address 1 (Sinhala) should be entered.";
																RequestContext.getCurrentInstance()
																		.update("frmrequiredField");
																RequestContext.getCurrentInstance()
																		.execute("PF('requiredField').show()");
															}
														} else {
															errorMsg = "Name in Full (Sinhala) should be entered.";
															RequestContext.getCurrentInstance()
																	.update("frmrequiredField");
															RequestContext.getCurrentInstance()
																	.execute("PF('requiredField').show()");
														}
													} else if (busOwnerDTO.getPerferedLanguage().equals("T")) {
														if (busOwnerDTO.getFullNameTamil() != null
																&& !busOwnerDTO.getFullNameTamil().isEmpty()
																&& !busOwnerDTO.getFullNameTamil()
																		.equalsIgnoreCase("")) {
															if (busOwnerDTO.getAddress1Tamil() != null
																	&& !busOwnerDTO.getAddress1Tamil().isEmpty()
																	&& !busOwnerDTO.getAddress1Tamil()
																			.equalsIgnoreCase("")) {
																if (busOwnerDTO.getAddress2Tamil() != null
																		&& !busOwnerDTO.getAddress2Tamil().isEmpty()
																		&& !busOwnerDTO.getAddress2Tamil()
																				.equalsIgnoreCase("")) {
																	if (busOwnerDTO.getCity() != null
																			&& !busOwnerDTO.getCity().isEmpty()
																			&& !busOwnerDTO.getCity()
																					.equalsIgnoreCase("")) {
																		if (busOwnerDTO.getSeq() != 0) {
																			// Update
																			updateOwner();
																		} else {
																			// save
																			saveRelationNonRelation();
																		}
																	} else {
																		errorMsg = "City (Tamil) should be entered.";
																		RequestContext.getCurrentInstance()
																				.update("frmrequiredField");
																		RequestContext.getCurrentInstance()
																				.execute("PF('requiredField').show()");
																	}
																} else {
																	errorMsg = "Address 2 (Tamil) should be entered.";
																	RequestContext.getCurrentInstance()
																			.update("frmrequiredField");
																	RequestContext.getCurrentInstance()
																			.execute("PF('requiredField').show()");
																}
															} else {
																errorMsg = "Address 1 (Tamil) should be entered.";
																RequestContext.getCurrentInstance()
																		.update("frmrequiredField");
																RequestContext.getCurrentInstance()
																		.execute("PF('requiredField').show()");
															}
														} else {
															errorMsg = "Name in Full (Tamil) should be entered.";
															RequestContext.getCurrentInstance()
																	.update("frmrequiredField");
															RequestContext.getCurrentInstance()
																	.execute("PF('requiredField').show()");
														}
													} else {
														if (busOwnerDTO.getSeq() != 0) {
															// Update
															updateOwner();
														} else {
															// save
															saveRelationNonRelation();
														}
													}

												} else {
													errorMsg = "NIC No. should be entered.";
													RequestContext.getCurrentInstance().update("frmrequiredField");
													RequestContext.getCurrentInstance()
															.execute("PF('requiredField').show()");
												}
											} else {
												errorMsg = "Province should be selected.";
												RequestContext.getCurrentInstance().update("frmrequiredField");
												RequestContext.getCurrentInstance()
														.execute("PF('requiredField').show()");
											}
										} else {
											errorMsg = "City should be entered.";
											RequestContext.getCurrentInstance().update("frmrequiredField");
											RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
										}
									} else {
										errorMsg = "Address 2 should be entered.";
										RequestContext.getCurrentInstance().update("frmrequiredField");
										RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
									}
								} else {
									errorMsg = "Address 1 should be entered.";
									RequestContext.getCurrentInstance().update("frmrequiredField");
									RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
								}
							} else {
								errorMsg = "Marital Status should be selected.";
								RequestContext.getCurrentInstance().update("frmrequiredField");
								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
							}
						} else {
							errorMsg = "Full Name should be entered.";
							RequestContext.getCurrentInstance().update("frmrequiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
						}
					} else {
						errorMsg = "Date of Birth should be selected.";
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}
				} else {
					errorMsg = "Gender should be selected.";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			} else {
				errorMsg = "Title should be selected.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		} else {
			errorMsg = "Realationship with transferor should be selected.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void onDistrictChange() {
		if (busOwnerDTO.getDistrict() != null && !busOwnerDTO.getDistrict().isEmpty()) {
			divSecList = adminService.getDivSecByDistrictToDropdown(busOwnerDTO.getDistrict());
		}
	}

	public void onProvinceChange() {
		if (busOwnerDTO.getProvince() != null && !busOwnerDTO.getProvince().isEmpty()) {
			districtList = adminService.getDistrictByProvinceToDropdown(busOwnerDTO.getProvince());
		}
	}

	public void phoneNumberValidator() {
		if (busOwnerDTO.getTelephoneNo() != null && !busOwnerDTO.getTelephoneNo().isEmpty()) {
			Pattern ptr = Pattern.compile("^[0-9]{10}$");
			boolean valid = ptr.matcher(busOwnerDTO.getTelephoneNo()).matches();
			if (valid) {

			} else {
				errorMsg = "Invalid Telephone Number";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				busOwnerDTO.setTelephoneNo(null);
			}

		}
		if (busOwnerDTO.getMobileNo() != null && !busOwnerDTO.getMobileNo().isEmpty()) {
			Pattern ptr = Pattern.compile("^[0-9]{10}$");
			boolean valid = ptr.matcher(busOwnerDTO.getMobileNo()).matches();
			if (valid) {

			} else {
				errorMsg = "Invalid Mobile Number";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				busOwnerDTO.setMobileNo(null);
			}

		}
	}

	public void onRouteChange() {

		if (amendmentDTO.getRouteNo() != null && !amendmentDTO.getRouteNo().equals("")) {
			routeDTO = issuePermitService.getDetailsbyRouteNo(amendmentDTO.getRouteNo());
			if (routeDTO.getOrigin() != null && !routeDTO.getOrigin().equals("")) {
				amendmentDTO.setVia(routeDTO.getVia());
				amendmentDTO.setDestination(routeDTO.getDestination());
				amendmentDTO.setOrigin(routeDTO.getOrigin());
				amendmentDTO.setDistance(routeDTO.getDistance());

			}
		} else {

			routeDTO = issuePermitService.getTempRouteDetails(generatedApplicationNo);
			tempNewRouteDTO = new AmendmentServiceDTO();

			if (routeDTO != null) {

				tempNewRouteDTO.setOrigin(routeDTO.getOrigin());
				tempNewRouteDTO.setDestination(routeDTO.getDestination());
				tempNewRouteDTO.setVia(routeDTO.getVia());
			}
		}

		routeFlag = false;
	}

	public void onNewRouteChange() {

		if (amendmentServiceDTO.getRouteNo() != null && !amendmentServiceDTO.getRouteNo().equals("")) {
			routeDTO = issuePermitService.getDetailsbyRouteNo(amendmentServiceDTO.getRouteNo());
			if (routeDTO.getOrigin() != null && !routeDTO.getOrigin().equals("")) {
				amendmentServiceDTO.setVia(routeDTO.getVia());
				amendmentServiceDTO.setDestination(routeDTO.getDestination());
				amendmentServiceDTO.setOrigin(routeDTO.getOrigin());
				amendmentServiceDTO.setDistance(routeDTO.getDistance());

			}
		} else {

			routeDTO = issuePermitService.getTempRouteDetails(generatedApplicationNo);
			tempNewRouteDTO = new AmendmentServiceDTO();

			if (routeDTO != null) {

				tempNewRouteDTO.setOrigin(routeDTO.getOrigin());
				tempNewRouteDTO.setDestination(routeDTO.getDestination());
				tempNewRouteDTO.setVia(routeDTO.getVia());
			}
		}

		routeFlag = false;
	}

	public void clearAll() {
		init();
	}

	public void skip() {
		if (strQueueNo != null && !strQueueNo.isEmpty() && !strQueueNo.trim().equalsIgnoreCase("")) {

			migratedService.updateSkipQueueNumberStatus(strQueueNo);

			callNext = false;
			skip = true;
			clearAll();

			strVehicleNo = null;
			strPermitNo = null;
			strQueueNo = null;
			strAmendmentType = null;

		} else {

		}

	}

	public void displayCheckDocumentDataList() {
		dataList = amendmentService.getChecklistDocuments(strTrnCode);
		for (int i = 0; i < dataList.size(); i++) {

			String currentDocCode = dataList.get(i).getDocumentCode();
			String currentDocCodeDescription = dataList.get(i).getDocumentDescription();

			boolean resultValue = amendmentService.checkIsSubmitted(currentDocCode, generatedApplicationNo, strPermitNo,
					strTrnCode);
			boolean isMandatory = amendmentService.isMandatory(currentDocCode, generatedApplicationNo, strPermitNo,
					strTrnCode);
			boolean isPhysicallyExit = amendmentService.isPhysicallyExit(currentDocCode, generatedApplicationNo,
					strPermitNo);

			if (resultValue == false) {

				if (isMandatory == true) {
					dataList.get(i).setMandatory(true);

					if (isPhysicallyExit == true) {
						dataList.get(i).setPhysicallyExists(true);
					} else {
						dataList.get(i).setPhysicallyExists(false);
					}
				} else {
					dataList.get(i).setMandatory(false);

					if (isPhysicallyExit == true) {
						dataList.get(i).setPhysicallyExists(true);
					} else {
						dataList.get(i).setPhysicallyExists(false);
					}
				}

				dataList.get(i).setExists(false);
				dataList.get(i).setHasRecord(false);
			} else {
				if (isMandatory == true) {
					dataList.get(i).setMandatory(true);

					if (isPhysicallyExit == true) {
						dataList.get(i).setPhysicallyExists(true);
					} else {
						dataList.get(i).setPhysicallyExists(false);
					}
				} else {
					dataList.get(i).setMandatory(false);

					if (isPhysicallyExit == true) {
						dataList.get(i).setPhysicallyExists(true);
					} else {
						dataList.get(i).setPhysicallyExists(false);
					}
				}

				dataList.get(i).setExists(true);
				dataList.get(i).setHasRecord(true);

			}
			checkAndDisplayRemarkValue = amendmentService.getRemarkDetails(currentDocCode, generatedApplicationNo,
					strPermitNo, strTrnCode);
			dataList.get(i).setDocSeqChecked(checkAndDisplayRemarkValue.getDocSeqChecked());
			dataList.get(i).setRemark(checkAndDisplayRemarkValue.getRemark());
			dataList.get(i).setDocFilePath(checkAndDisplayRemarkValue.getDocFilePath());
		}
	}

	public void generateVoucher() {

		CreatePaymentVoucherBckingBean c = new CreatePaymentVoucherBckingBean();

		try {

			sessionBackingBean.setApplicationNo(amendmentDTO.getApplicationNo());
			sessionBackingBean.setTransactionDescription(strTrnDesc);
			sessionBackingBean.setPermitNo(amendmentDTO.getPermitNo());

			paymentVoucherDTO = new PaymentVoucherDTO();
			paymentVoucherDTO.setApplicationNo(amendmentDTO.getApplicationNo());
			paymentVoucherDTO.setTransactionDescription(strTrnDesc);
			paymentVoucherDTO.setPermitNo(amendmentDTO.getPermitNo());
			RequestContext.getCurrentInstance().execute("PF('generateVoucher').show()");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void documentManagement() {
		DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();
		try {
			sessionBackingBean.setApplicationNoForDoc(generatedApplicationNo);
			sessionBackingBean.setPermitRenewalPermitNo(strPermitNo);
			sessionBackingBean.setPermitRenewalTranstractionTypeDescription(strTrnDesc);
			sessionBackingBean.setAmendmentPopUp(true);

			uploaddocumentManagementDTO.setUpload_Permit(strPermitNo);
			uploaddocumentManagementDTO.setTransaction_Type(strTrnDesc);

			mandatoryList = documentManagementService.mandatoryDocs(strTrnCode, strPermitNo);
			optionalList = documentManagementService.optionalDocs(strTrnCode, strPermitNo);

			RequestContext.getCurrentInstance().execute("PF('uploadDocument').show()");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateDocuments() {
		for (int i = 0; i < dataList.size(); i++) {

			if (dataList.get(i).isHasRecord() == true) {

				Long currentRecordSeqNo = dataList.get(i).getDocSeqChecked();
				String currentUploadFilePath = dataList.get(i).getDocFilePath();
				String modifyBy = sessionBackingBean.loginUser;
				String currentRemark = dataList.get(i).getRemark();
				int result = issuePermitService.updateDocumentRemark(currentRecordSeqNo, currentUploadFilePath,
						modifyBy, currentRemark);
				if (result == 0) {

				} else {

					RequestContext.getCurrentInstance().update("frmrequiredField");
					setErrorMsg("Errors.");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			}
		}
	}

	public void saveFinal() {
		updateDocuments();

		commonService.updateTaskStatusCompleted(generatedApplicationNo, "AM100", sessionBackingBean.getLoginUser());

		migratedService.updateStatusOfQueueNumberAfterCallNext(strQueueNo, "C");
		migratedService.updateQueueNumberTaskInQueueMaster(strQueueNo, generatedApplicationNo, "AM100", "C");

		migratedService.updateTransactionTypeCodeForQueueNo(strQueueNo, strTrnCode);

		clearAll();

		RequestContext.getCurrentInstance().update("finalCheckList");

		RequestContext.getCurrentInstance().update("frmsuccessSve");
		setErrorMsg("Successfully Saved.");
		RequestContext.getCurrentInstance().execute("PF('successSve').show()");

	}

	public void seatingCapacityValidation() {

		if (ominiBusDTO.getSeating() != null && ominiBusDTO.getSeating().charAt(0) == '-') {
			errorMsg = "Invalid Seating Capacity";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			ominiBusDTO.setSeating(null);

		}

	}

	public void noOfDoorsValidation() {

		if (ominiBusDTO.getNoofDoors() != null && ominiBusDTO.getNoofDoors().charAt(0) == '-') {
			errorMsg = "Invalid No. of Doors";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			ominiBusDTO.setNoofDoors(null);

		}

	}

	public void weightValidation() {

		if (ominiBusDTO.getWeight() != null && ominiBusDTO.getWeight().charAt(0) == '-') {
			errorMsg = "Invalid Weight";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			ominiBusDTO.setWeight(null);

		}

	}

	public void manufactureYearValidator() {

		if (ominiBusDTO.getManufactureDate() != null && !ominiBusDTO.getManufactureDate().isEmpty()) {
			Pattern ptr = Pattern.compile("^[0-9]{4}$");
			boolean valid = ptr.matcher(ominiBusDTO.getManufactureDate()).matches();
			if (valid) {
				DateFormat dateFormat = new SimpleDateFormat("yyyy");
				Date date = new Date();
				String year = dateFormat.format(date);
				int curYear = Integer.parseInt(year);

				int manuYear = Integer.parseInt(ominiBusDTO.getManufactureDate());

				if (curYear >= manuYear) {

				} else {
					errorMsg = "Invalid Manufacture Year";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					ominiBusDTO.setManufactureDate(null);
				}

			} else {
				errorMsg = "Invalid Manufacture Year";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				ominiBusDTO.setManufactureDate(null);
			}

		}

		if (ominiBusDTO.getRegistrationDate() != null && ominiBusDTO.getManufactureDate() != null) {
			int regYear = ominiBusDTO.getRegistrationDate().getYear();
			regYear = regYear + 1900;
			int manuYear = Integer.parseInt(ominiBusDTO.getManufactureDate());

			if (manuYear > regYear) {
				errorMsg = "Date of Registration should be greater than or same as Manufacture Year";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				ominiBusDTO.setRegistrationDate(null);

			}

		}
	}

	public void ominiBusSave() {

		if (ominiBusDTO.getVehicleRegNo() != null && !ominiBusDTO.getVehicleRegNo().isEmpty()
				&& !ominiBusDTO.getVehicleRegNo().equalsIgnoreCase("")) {

			if (ominiBusDTO.getHeight() != null && !ominiBusDTO.getHeight().isEmpty()
					&& !ominiBusDTO.getHeight().equalsIgnoreCase("")) {

				if (ominiBusDTO.getSeating() != null && !ominiBusDTO.getSeating().isEmpty()
						&& !ominiBusDTO.getSeating().equalsIgnoreCase("")) {

					if (ominiBusDTO.getManufactureDate() != null && !ominiBusDTO.getManufactureDate().isEmpty()
							&& !ominiBusDTO.getManufactureDate().equalsIgnoreCase("")) {

						if (ominiBusDTO.getNoofDoors() != null && !ominiBusDTO.getNoofDoors().isEmpty()
								&& !ominiBusDTO.getNoofDoors().equalsIgnoreCase("")) {

							if (ominiBusDTO.getWeight() != null && !ominiBusDTO.getWeight().isEmpty()
									&& !ominiBusDTO.getWeight().equalsIgnoreCase("")) {

								if (ominiBusDTO.getInsuCompName() != null && !ominiBusDTO.getInsuCompName().isEmpty()
										&& !ominiBusDTO.getInsuCompName().equalsIgnoreCase("")) {

									if (ominiBusDTO.getInsuExpDate() != null) {

										if (ominiBusDTO.getInsuCat() != null && !ominiBusDTO.getInsuCat().isEmpty()
												&& !ominiBusDTO.getInsuCat().equalsIgnoreCase("")) {

											if (ominiBusDTO.getExpiryDateRevLic() != null) {

												if (ominiBusDTO.getDateOfFirstReg() != null) {

													if (loanObtained) {

														ominiBusDTO.setIsLoanObtained("Y");

														// loan

														if (ominiBusDTO.getBankLoan() != null) {

															if (ominiBusDTO.getDueAmount() != null) {

																if (ominiBusDTO.getFinanceCompany() != null
																		&& !ominiBusDTO.getFinanceCompany().isEmpty()
																		&& !ominiBusDTO.getFinanceCompany()
																				.equalsIgnoreCase("")) {

																	if (ominiBusDTO.getDateObtained() != null) {

																		if (ominiBusDTO.getLapsedInstall() != null
																				&& !ominiBusDTO.getLapsedInstall()
																						.isEmpty()
																				&& !ominiBusDTO.getLapsedInstall()
																						.equalsIgnoreCase("")) {

																			if (ominiBusDTO.getSeq() != 0) {

																				// update
																				ominiBusDTO.setModifiedBy(
																						sessionBackingBean
																								.getLoginUser());
																				ominiBusDTO.setPermitNo(strPermitNo);

																				ominiBusDTO.setApplicationNo(
																						generatedApplicationNo);

																				int result = issuePermitService
																						.updateNewPermitOminiBus(
																								ominiBusDTO);

																				if (result == 0) {

																					amendmentService
																							.updateAmendmentDetails(
																									amendmentDTO);

																					getOminiBusDetails();
																					otherOrgList = amendmentService
																							.getOtherOrgList(
																									generatedApplicationNo);

																					RequestContext.getCurrentInstance()
																							.update("frmOminiBusInfo");
																					RequestContext.getCurrentInstance()
																							.execute(
																									"PF('successSve').show()");

																				} else {
																					RequestContext.getCurrentInstance()
																							.execute(
																									"PF('generalError').show()");
																				}
																			} else {
																				// Save
																				ominiBusDTO.setQueueNo(strQueueNo);
																				if (generatedApplicationNo == null
																						|| generatedApplicationNo
																								.isEmpty()
																						|| generatedApplicationNo
																								.equalsIgnoreCase("")) {
																					generatedApplicationNo = amendmentService
																							.generateApplicationNo();
																					ominiBusDTO.setIsBacklogApp("N");
																					ominiBusDTO.setCreatedBy(
																							sessionBackingBean
																									.getLoginUser());
																					ominiBusDTO
																							.setPermitNo(strPermitNo);
																					ominiBusDTO.setApplicationNo(
																							generatedApplicationNo);

																					amendmentService
																							.saveNewOminiBusApplication(
																									ominiBusDTO);
																				} else {
																					amendmentService
																							.updateVehicleOfApplication(
																									ominiBusDTO
																											.getVehicleRegNo(),
																									generatedApplicationNo,
																									sessionBackingBean
																											.getLoginUser());
																				}

																				ominiBusDTO.setIsBacklogApp("N");
																				ominiBusDTO
																						.setCreatedBy(sessionBackingBean
																								.getLoginUser());
																				ominiBusDTO.setPermitNo(strPermitNo);
																				ominiBusDTO.setApplicationNo(
																						generatedApplicationNo);

																				amendmentDTO
																						.setCreatedBy(sessionBackingBean
																								.getLoginUser());
																				amendmentDTO.setPermitNo(strPermitNo);
																				amendmentDTO.setApplicationNo(
																						generatedApplicationNo);
																				amendmentDTO.setOldBusNo(strVehicleNo);

																				amendmentDTO.setBusRegNo(
																						ominiBusDTO.getVehicleRegNo());
																				amendmentDTO.setTrnType(strTrnCode);
																				amendmentDTO.setPermitNo(
																						ominiBusDTO.getPermitNo());
																				amendmentDTO.setQueueNo(strQueueNo);

																				int result = amendmentService
																						.saveNewOminiBus(ominiBusDTO);

																				if (result == 0) {

																					if (amendmentDTO.getSeq() == 0) {
																						amendmentService
																								.saveAmendmentNewOminiBus(
																										amendmentDTO);
																					} else {

																						amendmentService
																								.updateAmendmentDetails(
																										amendmentDTO);
																					}

																					saveActionTaskHandler();

																					migratedService
																							.updateStatusOfQueApp(
																									strQueueNo,
																									generatedApplicationNo);
																					migratedService
																							.updateQueueNumberTaskInQueueMaster(
																									strQueueNo,
																									generatedApplicationNo,
																									"AM100", "O");

																					savedNewOminiBus = true;

																					getOminiBusDetails();
																					otherOrgList = amendmentService
																							.getOtherOrgList(
																									generatedApplicationNo);

																					displayCheckDocumentDataList();

																					RequestContext.getCurrentInstance()
																							.update("frmtab07");

																					RequestContext.getCurrentInstance()
																							.update("frmOminiBusInfo");
																					RequestContext.getCurrentInstance()
																							.execute(
																									"PF('successSve').show()");

																				} else {
																					RequestContext.getCurrentInstance()
																							.execute(
																									"PF('generalError').show()");
																				}
																			}

																		} else {
																			errorMsg = "Lapsed Installments should be entered.";
																			RequestContext.getCurrentInstance()
																					.update("frmrequiredField");
																			RequestContext.getCurrentInstance().execute(
																					"PF('requiredField').show()");
																		}

																	} else {
																		errorMsg = "Date Obtained should be entered.";
																		RequestContext.getCurrentInstance()
																				.update("frmrequiredField");
																		RequestContext.getCurrentInstance()
																				.execute("PF('requiredField').show()");
																	}

																} else {
																	errorMsg = "Name of Finance Company / Loan Obtained from should be entered.";
																	RequestContext.getCurrentInstance()
																			.update("frmrequiredField");
																	RequestContext.getCurrentInstance()
																			.execute("PF('requiredField').show()");
																}

															} else {
																errorMsg = "Due amount at present should be entered.";
																RequestContext.getCurrentInstance()
																		.update("frmrequiredField");
																RequestContext.getCurrentInstance()
																		.execute("PF('requiredField').show()");
															}

														} else {
															errorMsg = "Bank Loan should be entered.";
															RequestContext.getCurrentInstance()
																	.update("frmrequiredField");
															RequestContext.getCurrentInstance()
																	.execute("PF('requiredField').show()");
														}

													} else {

														ominiBusDTO.setIsLoanObtained("N");

														if (ominiBusDTO.getSeq() != 0) {
															// update
															ominiBusDTO
																	.setModifiedBy(sessionBackingBean.getLoginUser());
															ominiBusDTO.setPermitNo(strPermitNo);
															ominiBusDTO.setApplicationNo(generatedApplicationNo);

															int result = issuePermitService
																	.updateNewPermitOminiBus(ominiBusDTO);

															if (result == 0) {

																savedNewOminiBus = true;
																amendmentDTO.setModifiedBy(
																		sessionBackingBean.getLoginUser());
																amendmentService.updateAmendmentDetails(amendmentDTO);

																getOminiBusDetails();
																otherOrgList = amendmentService
																		.getOtherOrgList(generatedApplicationNo);

																RequestContext.getCurrentInstance()
																		.update("frmOminiBusInfo");
																RequestContext.getCurrentInstance()
																		.execute("PF('successSve').show()");

															} else {
																RequestContext.getCurrentInstance()
																		.execute("PF('generalError').show()");
															}
														} else {
															// Save
															ominiBusDTO.setQueueNo(strQueueNo);
															if (generatedApplicationNo == null
																	|| generatedApplicationNo.isEmpty()
																	|| generatedApplicationNo.equalsIgnoreCase("")) {
																generatedApplicationNo = amendmentService
																		.generateApplicationNo();
																ominiBusDTO.setIsBacklogApp("N");
																ominiBusDTO.setCreatedBy(
																		sessionBackingBean.getLoginUser());
																ominiBusDTO.setPermitNo(strPermitNo);
																ominiBusDTO.setApplicationNo(generatedApplicationNo);

																amendmentService
																		.saveNewOminiBusApplication(ominiBusDTO);
															} else {
																amendmentService.updateVehicleOfApplication(
																		ominiBusDTO.getVehicleRegNo(),
																		generatedApplicationNo,
																		sessionBackingBean.getLoginUser());
															}
															ominiBusDTO.setIsBacklogApp("N");
															ominiBusDTO.setCreatedBy(sessionBackingBean.getLoginUser());
															ominiBusDTO.setPermitNo(strPermitNo);
															ominiBusDTO.setApplicationNo(generatedApplicationNo);

															amendmentDTO
																	.setCreatedBy(sessionBackingBean.getLoginUser());
															amendmentDTO.setPermitNo(strPermitNo);
															amendmentDTO.setApplicationNo(generatedApplicationNo);
															amendmentDTO.setOldBusNo(strVehicleNo);

															amendmentDTO.setBusRegNo(ominiBusDTO.getVehicleRegNo());
															amendmentDTO.setTrnType(strTrnCode);
															amendmentDTO.setPermitNo(ominiBusDTO.getPermitNo());
															amendmentDTO.setQueueNo(strQueueNo);

															int result = amendmentService.saveNewOminiBus(ominiBusDTO);

															if (result == 0) {

																if (amendmentDTO.getSeq() == 0) {
																	amendmentService
																			.saveAmendmentNewOminiBus(amendmentDTO);
																} else {

																	amendmentService
																			.updateAmendmentDetails(amendmentDTO);
																}

																saveActionTaskHandler();

																migratedService.updateStatusOfQueApp(strQueueNo,
																		generatedApplicationNo);
																migratedService.updateQueueNumberTaskInQueueMaster(
																		strQueueNo, generatedApplicationNo, "AM100",
																		"O");

																savedNewOminiBus = true;

																getOminiBusDetails();
																otherOrgList = amendmentService
																		.getOtherOrgList(generatedApplicationNo);

																displayCheckDocumentDataList();

																RequestContext.getCurrentInstance().update("frmtab07");

																RequestContext.getCurrentInstance()
																		.update("frmOminiBusInfo");
																RequestContext.getCurrentInstance()
																		.execute("PF('successSve').show()");

															} else {
																RequestContext.getCurrentInstance()
																		.execute("PF('generalError').show()");
															}
														}

													}

												} else {
													errorMsg = "Date of First Registration in Sri Lanka should be entered.";
													RequestContext.getCurrentInstance().update("frmrequiredField");
													RequestContext.getCurrentInstance()
															.execute("PF('requiredField').show()");
												}

											} else {
												errorMsg = "Expiry Date of Revenue License should be entered.";
												RequestContext.getCurrentInstance().update("frmrequiredField");
												RequestContext.getCurrentInstance()
														.execute("PF('requiredField').show()");
											}

										} else {
											errorMsg = "Category of Insurance should be entered.";
											RequestContext.getCurrentInstance().update("frmrequiredField");
											RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
										}

									} else {
										errorMsg = "Insurance Expiry Date should be entered.";
										RequestContext.getCurrentInstance().update("frmrequiredField");
										RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
									}

								} else {
									errorMsg = "Insurance Company/Corporation Name should be entered.";
									RequestContext.getCurrentInstance().update("frmrequiredField");
									RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
								}

							} else {
								errorMsg = "Weight should be entered.";
								RequestContext.getCurrentInstance().update("frmrequiredField");
								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
							}

						} else {
							errorMsg = "No. of Doors should be entered.";
							RequestContext.getCurrentInstance().update("frmrequiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
						}
					} else {
						errorMsg = "Manufacture year should be entered.";
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}

				} else {
					errorMsg = "Seating Capacity should be entered.";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			} else {
				errorMsg = "Height should be entered.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

		} else {
			errorMsg = "Registration No. of the Bus should be entered.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public void priceValOfBusValidation() {

		if (ominiBusDTO.getPriceValOfBus().signum() < 0) {
			errorMsg = "Invalid Price value of the Bus";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			ominiBusDTO.setPriceValOfBus(null);
		}

	}

	public void priceValOfPropertyValidation() {

		if (accidentDTO.getValueOfPropertyDamaged().signum() < 0) {
			errorMsg = "Invalid Value of Property Damaged";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			accidentDTO.setValueOfPropertyDamaged(null);
		}

	}

	public void bankLoanValidation() {

		if (ominiBusDTO.getBankLoan().signum() < 0) {
			errorMsg = "Invalid Bank Loan Amount";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			ominiBusDTO.setBankLoan(null);
		}

		int res = ominiBusDTO.getBankLoan().compareTo(ominiBusDTO.getPriceValOfBus());

		if (res == 1) {
			errorMsg = "Invalid Bank Loan Amount";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			ominiBusDTO.setBankLoan(null);
		}

	}

	public void dueAmountValidation() {

		if (ominiBusDTO.getDueAmount().signum() < 0) {
			errorMsg = "Invalid Due amount at present";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			ominiBusDTO.setDueAmount(null);
		}

		int res = ominiBusDTO.getDueAmount().compareTo(ominiBusDTO.getBankLoan());

		if (res == 1) {
			errorMsg = "Invalid Due amount at present";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			ominiBusDTO.setDueAmount(null);
		}

	}

	public void regDateExpireDateValidator() {

		if (ominiBusDTO.getDateOfFirstReg() != null && ominiBusDTO.getExpiryDateRevLic() != null
				&& ominiBusDTO.getDateOfFirstReg().after(ominiBusDTO.getExpiryDateRevLic())) {

			errorMsg = "Expiry Date of Revenue License should be greater than Date of First Registration in Sri Lanka";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			ominiBusDTO.setExpiryDateRevLic(null);

		}

	}

	public void getOminiBusDetails() {

		if (strPermitNo != null && !strPermitNo.isEmpty() && strPermitNo != "") {

			ominiBusDTO = issuePermitService.ominiBusByVehicleNo(ominiBusDTO.getVehicleRegNo());

			if (ominiBusDTO.getIsLoanObtained() != null && !ominiBusDTO.getIsLoanObtained().isEmpty()
					&& ominiBusDTO.getIsLoanObtained().equals("Y")) {

				setLoanObtained(true);
			} else if (ominiBusDTO.getIsLoanObtained() != null && !ominiBusDTO.getIsLoanObtained().isEmpty()
					&& ominiBusDTO.getIsLoanObtained().equals("N")) {

				setLoanObtained(false);
			} else {
				setLoanObtained(false);
			}

			amendmentDTO = amendmentService.getAmendmentDetails(generatedApplicationNo);
			String dateFormat = "dd/MM/yyyy";
			SimpleDateFormat frm = new SimpleDateFormat(dateFormat);
			if (ominiBusDTO.getFitnessCertiDate() != null) {
				ominiBusDTO.setFitnessCarficateDateVal(frm.format(ominiBusDTO.getFitnessCertiDate()));
			} else {
				ominiBusDTO.setFitnessCarficateDateVal(null);
			}

			if (ominiBusDTO.getInsuCat() == null) {
				ominiBusDTO.setInsuCat("UNLIMITED");
			} else if (ominiBusDTO.getInsuCat() != null) {
				ominiBusDTO.setInsuCat(ominiBusDTO.getInsuCat());
			}

			if (ominiBusDTO.getInsuExpDate() != null) {
				ominiBusDTO.setInsuExpiryDateVal(frm.format(ominiBusDTO.getInsuExpDate()));
			} else {
				ominiBusDTO.setInsuExpiryDateVal(null);
			}

			if (ominiBusDTO.getExpiryDateRevLic() != null) {
				ominiBusDTO.setExpiryDateRevLicVal(frm.format(ominiBusDTO.getExpiryDateRevLic()));
			} else {
				ominiBusDTO.setExpiryDateRevLicVal(null);
			}

			if (ominiBusDTO.getDateOfFirstReg() != null) {
				ominiBusDTO.setDateOfFirstRegVal(frm.format(ominiBusDTO.getDateOfFirstReg()));
			} else {
				ominiBusDTO.setDateOfFirstRegVal(null);
			}

			if (ominiBusDTO.getEmissionExpDate() != null) {
				ominiBusDTO.setEmissionExpDateVal(frm.format(ominiBusDTO.getEmissionExpDate()));
			} else {
				ominiBusDTO.setEmissionExpDateVal(null);
			}

		}
	}

	public void clearBusDetails() {
		ominiBusDTO = new OminiBusDTO();

	}

	public void clearCheckDocumentsTable() {
		displayCheckDocumentDataList();
	}

	public void clearOtherOrg() {
		organizationDTO = new OrganizationDTO();
	}

	public void addOtherOrg() {
		organizationDTO.setApplicationNo(generatedApplicationNo);
		organizationDTO.setCreatedBy(sessionBackingBean.getLoginUser());
		organizationDTO.setVehicleNo(ominiBusDTO.getVehicleRegNo());
		organizationDTO.setAmendmentSeq(amendmentDTO.getSeq());
		amendmentService.saveOtherOrg(organizationDTO);
		clearOtherOrg();
		otherOrgList = amendmentService.getOtherOrgList(generatedApplicationNo);

		RequestContext.getCurrentInstance().update("otherPermits");
		RequestContext.getCurrentInstance().execute("PF('successSve').show()");
	}

	public void addAccident() {

		accidentDTO.setCreatedBy(sessionBackingBean.getLoginUser());
		accidentDTO.setVehicleNo(ominiBusDTO.getVehicleRegNo());

		amendmentService.saveAccident(accidentDTO);
		clearAccident();
		accidentList = amendmentService.getAccidentList(ominiBusDTO.getVehicleRegNo());

		RequestContext.getCurrentInstance().update("accidentList");
		RequestContext.getCurrentInstance().execute("PF('successSve').show()");

	}

	public void clearAccident() {
		accidentDTO = new AccidentDTO();
	}

	public void addMoreAccident() {

		accidentDTO.setCreatedBy(sessionBackingBean.getLoginUser());
		accidentDTO.setVehicleNo(ominiBusDTO.getVehicleRegNo());

		amendmentService.saveMoreAccident(accidentDTO);
		clearMoreAccident();
		moreAccidentList = amendmentService.getMoreAccidentList(ominiBusDTO.getVehicleRegNo());

		RequestContext.getCurrentInstance().update("moreAccidentList");
		RequestContext.getCurrentInstance().execute("PF('successSve').show()");

	}

	public void clearMoreAccident() {
		accidentDTO = new AccidentDTO();

	}

	public void saveAfterAccident() {
		afterAccidentDTO.setCreatedBy(sessionBackingBean.getLoginUser());
		afterAccidentDTO.setVehicleNo(ominiBusDTO.getVehicleRegNo());

		amendmentService.saveAfterAccident(afterAccidentDTO);
		afterAccidentDTO = amendmentService.getAfterAccident(ominiBusDTO.getVehicleRegNo());

		RequestContext.getCurrentInstance().update("afterAccident");
		RequestContext.getCurrentInstance().execute("PF('successSve').show()");

	}

	public void updateAfterAccident() {
		afterAccidentDTO.setModifiedBy(sessionBackingBean.getLoginUser());
		afterAccidentDTO.setVehicleNo(ominiBusDTO.getVehicleRegNo());

		amendmentService.updateAfterAccident(afterAccidentDTO);

		afterAccidentDTO = amendmentService.getAfterAccident(ominiBusDTO.getVehicleRegNo());

		RequestContext.getCurrentInstance().update("afterAccident");
		RequestContext.getCurrentInstance().execute("PF('successSve').show()");
	}

	public void clearAfterAccident() {
		afterAccidentDTO = new AfterAccidentDTO();
	}

	public void clearUpdateAfterAccident() {
		afterAccidentDTO = new AfterAccidentDTO();
		afterAccidentDTO = amendmentService.getAfterAccident(ominiBusDTO.getVehicleRegNo());
	}

	public void removeOtherOrg() {

		if (selectedOtherOrg != 0) {
			amendmentService.removeOtherOrg(selectedOtherOrg);
			RequestContext.getCurrentInstance().update("otherOrgList");
			RequestContext.getCurrentInstance().update("frmsuccessSve");
			otherOrgList = new ArrayList<OrganizationDTO>(0);
			otherOrgList = amendmentService.getOtherOrgList(generatedApplicationNo);
			setErrorMsg("Successfully Saved.");
			RequestContext.getCurrentInstance().execute("PF('successSve').show()");
		} else {

			errorMsg = "You can not delete this record.";
			RequestContext.getCurrentInstance().execute("PF('frmrequiredField').show()");
		}
		RequestContext.getCurrentInstance().update("frmNewOmnibus1");
	}

	public void removeAccident() {

		if (selectedAccident != 0) {
			amendmentService.removeAccident(selectedAccident);
			RequestContext.getCurrentInstance().update("accidentList");
			RequestContext.getCurrentInstance().update("frmsuccessSve");
			accidentList = new ArrayList<AccidentDTO>(0);
			accidentList = amendmentService.getAccidentList(generatedApplicationNo);
			setErrorMsg("Successfully Saved.");
			RequestContext.getCurrentInstance().execute("PF('successSve').show()");
		} else {

			errorMsg = "You can not delete this record.";
			RequestContext.getCurrentInstance().execute("PF('frmrequiredField').show()");
		}
		RequestContext.getCurrentInstance().update("frmtab0lojb89");
	}

	public void removeMoreAccident() {

		if (selectedMoreAccident != 0) {
			amendmentService.removeMoreAccident(selectedMoreAccident);
			RequestContext.getCurrentInstance().update("moreAccidentList");
			RequestContext.getCurrentInstance().update("frmsuccessSve");
			accidentList = new ArrayList<AccidentDTO>(0);
			moreAccidentList = amendmentService.getMoreAccidentList(generatedApplicationNo);
			setErrorMsg("Successfully Saved.");
			RequestContext.getCurrentInstance().execute("PF('successSve').show()");
		} else {

			errorMsg = "You can not delete this record.";
			RequestContext.getCurrentInstance().execute("PF('frmrequiredField').show()");
		}
		RequestContext.getCurrentInstance().update("frmtab0lojb89");
	}

	public void handleClose() throws InterruptedException {

		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("/InfoNTC/pages/home/welcomePage.xhtml");

		} catch (IOException e) {
			e.printStackTrace();
		}
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

	public void busNoValidator() {
		if (ominiBusDTO.getVehicleRegNo() != null && !ominiBusDTO.getVehicleRegNo().isEmpty()) {
			String chkDuplicates = adminService.checkVehiNo(ominiBusDTO.getVehicleRegNo());
			if (chkDuplicates != null) {
				errorMsg = "Duplicate Registration No. of the Bus";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				ominiBusDTO.setVehicleRegNo(null);
			}
		}
	}

	public void saveServiceChange() {

		if (generatedApplicationNo == null || generatedApplicationNo.isEmpty()
				|| generatedApplicationNo.equalsIgnoreCase("")) {
			generatedApplicationNo = amendmentService.generateApplicationNo();
			busOwnerDTO.setApplicationNo(generatedApplicationNo);
			busOwnerDTO.setCreatedBy(sessionBackingBean.getLoginUser());
			amendmentService.saveDataApplication(busOwnerDTO);
		}

		amendmentServiceDTO.setCreatedBy(sessionBackingBean.getLoginUser());

		amendmentServiceDTO.setApplicationNo(generatedApplicationNo);

		amendmentDTO.setCreatedBy(sessionBackingBean.getLoginUser());

		amendmentDTO.setApplicationNo(generatedApplicationNo);

		amendmentServiceDTO.setBusRegNo(strVehicleNo);
		amendmentServiceDTO.setPermitNo(amendmentBusOwnerDTO.getPermitNo());

		amendmentDTO.setBusRegNo(strVehicleNo);
		amendmentDTO.setTrnType(strTrnCode);
		amendmentDTO.setPermitNo(amendmentBusOwnerDTO.getPermitNo());
		amendmentDTO.setQueueNo(strQueueNo);

		amendmentServiceDTO.setQueueNo(strQueueNo);

		if (amendmentDTO.getServiceChangeType() != null && !amendmentDTO.getServiceChangeType().isEmpty()
				&& amendmentDTO.getServiceChangeType().trim().equalsIgnoreCase("S")) {
			amendmentServiceDTO.setRouteNo(amendmentOminiBusDTO.getRouteNo());
			amendmentServiceDTO.setRouteFlag(amendmentOminiBusDTO.getRouteFlag());

		} else if (amendmentDTO.getServiceChangeType() != null && !amendmentDTO.getServiceChangeType().isEmpty()
				&& amendmentDTO.getServiceChangeType().trim().equalsIgnoreCase("R")) {

			amendmentServiceDTO.setServiceType(amendmentOminiBusDTO.getServiceType());
		} else if (amendmentDTO.getServiceChangeType() != null && !amendmentDTO.getServiceChangeType().isEmpty()
				&& amendmentDTO.getServiceChangeType().trim().equalsIgnoreCase("E")) {
			amendmentServiceDTO.setRouteNo(amendmentOminiBusDTO.getRouteNo());

			amendmentServiceDTO.setServiceType(amendmentOminiBusDTO.getServiceType());

		}
		amendmentService.saveServiceChange(amendmentServiceDTO, sessionBackingBean.getLoginUser());

		amendmentServiceDTO = amendmentService.getServiceChange(generatedApplicationNo);
		onNewRouteChange();
		if (amendmentDTO.getSeq() == 0) {
			amendmentService.saveAmendmentServiceChange(amendmentDTO);
		} else {
			amendmentDTO.setModifiedBy(sessionBackingBean.getLoginUser());
			amendmentService.updateAmendmentDetails(amendmentDTO);
		}

		amendmentDTO = amendmentService.getAmendmentDetails(generatedApplicationNo);

		saveActionTaskHandler();

		migratedService.updateStatusOfQueApp(strQueueNo, generatedApplicationNo);
		migratedService.updateTransactionTypeCodeForQueueNo(strQueueNo, strTrnCode);
		migratedService.updateQueueNumberTaskInQueueMaster(strQueueNo, generatedApplicationNo, "AM100", "O");

		displayCheckDocumentDataList();

		RequestContext.getCurrentInstance().update("frmtab07");
		RequestContext.getCurrentInstance().execute("PF('successSve').show()");

	}

	public void back() throws InterruptedException {

		sessionBackingBean.setBackCommittee(true);
		sessionBackingBean.setCommitteeView(false);
		String back = "backCommittee";
		FacesContext fcontext = FacesContext.getCurrentInstance();
		Object backClicked = fcontext.getExternalContext().getSessionMap().put("backbtn", back);
		try {
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("/InfoNTC/pages/surveyManagement/CommitteeBoardApproval.xhtml");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void backInquiry() {

		String approveURL = sessionBackingBean.getApproveURL();
		String searchURL = sessionBackingBean.getSearchURL();

		if (approveURL != null) {

			try {
				sessionBackingBean.setApproveURLStatus(false);
				sessionBackingBean.isClicked = false;
				sessionBackingBean.isVehicelInsfection = false;
				sessionBackingBean.setInquiryTYpe(false);
				sessionBackingBean.renewalViewMood = false;
				sessionBackingBean.setRenewalType(false);
				sessionBackingBean.setSearchURLStatus(false);
				FacesContext.getCurrentInstance().getExternalContext().redirect(approveURL);
			} catch (IOException e) {

				e.printStackTrace();
			}

		} else if (searchURL != null) {

			try {
				sessionBackingBean.setApproveURLStatus(false);
				sessionBackingBean.isClicked = false;
				sessionBackingBean.isVehicelInsfection = false;
				sessionBackingBean.setInquiryTYpe(false);
				sessionBackingBean.renewalViewMood = false;
				sessionBackingBean.setRenewalType(false);
				sessionBackingBean.setSearchURLStatus(false);
				FacesContext.getCurrentInstance().getExternalContext().redirect(searchURL);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		setShowInquiryBack(false);
		RequestContext.getCurrentInstance().update("btnBack");
	}

	public void backForHigherAuth() {

		String approveURL = null;
		if (sessionBackingBean.getPageMode().equalsIgnoreCase("V")) {
			approveURL = sessionBackingBean.getApproveURL();
			sessionBackingBean.amendmentsViewMood = true;
			sessionBackingBean.setAmendmentsViewMood(false);

		} else {
			approveURL = "/InfoNTC/pages/amendmentsToBusOrOwner/viewBusOwnerServiceAmendment.xhtml#!";
			sessionBackingBean.amendmentsViewMood = true;
		}
		if (approveURL != null) {
			try {
				sessionBackingBean.setApproveURLStatus(false);
				sessionBackingBean.amendmentsViewMood = true;
				FacesContext.getCurrentInstance().getExternalContext().redirect(approveURL);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		sessionBackingBean.setAmendmentsViewMood(false);
	}

	public void viewGpsDetails() {
		loadValuesForViewGPS();

		RequestContext.getCurrentInstance().execute("PF('GPSModeId').show()");
		RequestContext.getCurrentInstance().update("GPSModeId");
	}

	public void loadValuesForViewGPS() {
		setSimRegistrationDTO(manageInquiryService.getGPSDetails(strPermitNo));
		if (simRegistrationDTO.getSimRegNo() != null) {
			setEmiDetViewList(manageInquiryService.getEmiDetails(simRegistrationDTO.getSimRegNo()));
		}
	}

	/** investigation pop up start **/
	public void viewInvestigationPopUp() {
		flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
		conditionList = null;
		documentList = null;
		loadValuesForViewInvestigation();

		RequestContext.getCurrentInstance().execute("PF('showPageDialog').show()");
		RequestContext.getCurrentInstance().update("showPageDialog");

	}

	public void viewInvesAction() {
		flyingManageInvestigationLogDTO = flyingSquadChargeSheetService.getShowDetailsN(viewInvSelect.getInvesNo());
		conditionList = flyingSquadChargeSheetService.getConditionList(viewInvSelect.getInvesNo());
		documentList = flyingSquadChargeSheetService.getdocumentlist(viewInvSelect.getInvesNo());

		RequestContext.getCurrentInstance().update("dlg-Investigation");
	}

	public void close() {
		sessionBackingBean.setInvesNo("");
		flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
		RequestContext.getCurrentInstance().update("frmview");
		RequestContext.getCurrentInstance().execute("PF('showPageDialog').hide()");
	}

	public void loadValuesForViewInvestigation() {
		setInvestigationDetViewList(manageInquiryService.getInvestigationDetails(strPermitNo));

	}

	/** investigation pop up end **/
	public void viewPublicComplain() {
		selectedComplaintDTO = new ComplaintRequestDTO();
		loadValuesForViewPublicComplaint();
		RequestContext.getCurrentInstance().execute("PF('complaintDlg').show()");
		RequestContext.getCurrentInstance().update("complaintDlg");
	}

	public void loadValuesForViewPublicComplaint() {
		complainDetViewList = manageInquiryService.getComplaintDetailsForPublicComplain(strPermitNo);

	}

	public void viewAction() {
		selectedComplaintDTO = manageInquiryService.getComplaintDetails(viewSelect.getComplaintNo(),
				viewSelect.getVehicleNo(), viewSelect.getPermitNo());
		RequestContext.getCurrentInstance().update("dlg-complaint");
		renderComplainDet = true;
	}

	/** Public Complain Methods End **/

	/*
	 * GPS Integration 30-03-2020 Ranjaka Liyanage
	 */

	public void redirectGPS() throws IOException {

		String url = null;
		String busNo = null;
		String key = null;

		URI urlParam1 = null;
		URI urlParam2 = null;

		try {
			url = PropertyReader.getPropertyValue("gps.redirect.url");
		} catch (ApplicationException e1) {
			e1.printStackTrace();
		}

		try {
			key = PropertyReader.getPropertyValue("gps.redirect.key");
		} catch (ApplicationException e1) {
			e1.printStackTrace();
		}

		if (amendmentBusOwnerDTO.getBusRegNo() != null) {
			busNo = amendmentBusOwnerDTO.getBusRegNo();
		}

		if (url != null && key != null && busNo != null) {

			try {
				urlParam1 = appendUri(url, "busNo=" + busNo);

				urlParam2 = appendUri(urlParam1.toString(), "key=" + key);

			} catch (URISyntaxException e) {
				e.printStackTrace();
			}

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

			externalContext.redirect(urlParam2.toString());
		}
	}

	public static URI appendUri(String uri, String appendQuery) throws URISyntaxException {
		URI oldUri = new URI(uri);

		String newQuery = oldUri.getQuery();
		if (newQuery == null) {
			newQuery = appendQuery;
		} else {
			newQuery += "&" + appendQuery;
		}

		URI newUri = new URI(oldUri.getScheme(), oldUri.getAuthority(), oldUri.getPath(), newQuery,
				oldUri.getFragment());

		return newUri;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public AmendmentDTO getAmendmentDTO() {
		return amendmentDTO;
	}

	public void setAmendmentDTO(AmendmentDTO amendmentDTO) {
		this.amendmentDTO = amendmentDTO;
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

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	public String getStrPermitNo() {
		return strPermitNo;
	}

	public void setStrPermitNo(String strPermitNo) {
		this.strPermitNo = strPermitNo;
	}

	public String getStrVehicleNo() {
		return strVehicleNo;
	}

	public void setStrVehicleNo(String strVehicleNo) {
		this.strVehicleNo = strVehicleNo;
	}

	public String getStrQueueNo() {
		return strQueueNo;
	}

	public void setStrQueueNo(String strQueueNo) {
		this.strQueueNo = strQueueNo;
	}

	public boolean isServiceChange() {
		return serviceChange;
	}

	public void setServiceChange(boolean serviceChange) {
		this.serviceChange = serviceChange;
	}

	public String getStrAmendmentType() {
		return strAmendmentType;
	}

	public void setStrAmendmentType(String strAmendmentType) {
		this.strAmendmentType = strAmendmentType;
	}

	public OminiBusDTO getOminiBusDTO() {
		return ominiBusDTO;
	}

	public void setOminiBusDTO(OminiBusDTO ominiBusDTO) {
		this.ominiBusDTO = ominiBusDTO;
	}

	public IssuePermitService getIssuePermitService() {
		return issuePermitService;
	}

	public void setIssuePermitService(IssuePermitService issuePermitService) {
		this.issuePermitService = issuePermitService;
	}

	public RouteDTO getRouteDTO() {
		return routeDTO;
	}

	public void setRouteDTO(RouteDTO routeDTO) {
		this.routeDTO = routeDTO;
	}

	public Boolean getRouteFlag() {
		return routeFlag;
	}

	public void setRouteFlag(Boolean routeFlag) {
		this.routeFlag = routeFlag;
	}

	public String getGeneratedApplicationNo() {
		return generatedApplicationNo;
	}

	public void setGeneratedApplicationNo(String generatedApplicationNo) {
		this.generatedApplicationNo = generatedApplicationNo;
	}

	public BusOwnerDTO getBusOwnerDTO() {
		return busOwnerDTO;
	}

	public void setBusOwnerDTO(BusOwnerDTO busOwnerDTO) {
		this.busOwnerDTO = busOwnerDTO;
	}

	public List<CommonDTO> getProvincelList() {
		return provincelList;
	}

	public void setProvincelList(List<CommonDTO> provincelList) {
		this.provincelList = provincelList;
	}

	public List<CommonDTO> getDistrictList() {
		return districtList;
	}

	public void setDistrictList(List<CommonDTO> districtList) {
		this.districtList = districtList;
	}

	public List<CommonDTO> getDivSecList() {
		return divSecList;
	}

	public void setDivSecList(List<CommonDTO> divSecList) {
		this.divSecList = divSecList;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public List<CommonDTO> getRelationshipList() {
		return relationshipList;
	}

	public void setRelationshipList(List<CommonDTO> relationshipList) {
		this.relationshipList = relationshipList;
	}

	public List<CommonDTO> getRoutefordropdownList() {
		return routefordropdownList;
	}

	public void setRoutefordropdownList(List<CommonDTO> routefordropdownList) {
		this.routefordropdownList = routefordropdownList;
	}

	public QueueManagementService getQueueManagementService() {
		return queueManagementService;
	}

	public void setQueueManagementService(QueueManagementService queueManagementService) {
		this.queueManagementService = queueManagementService;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public VehicleInspectionService getVehicleInspectionService() {
		return vehicleInspectionService;
	}

	public void setVehicleInspectionService(VehicleInspectionService vehicleInspectionService) {
		this.vehicleInspectionService = vehicleInspectionService;
	}

	public boolean isFinalCheckList() {
		return finalCheckList;
	}

	public void setFinalCheckList(boolean finalCheckList) {
		this.finalCheckList = finalCheckList;
	}

	public void setRouteFlag(boolean routeFlag) {
		this.routeFlag = routeFlag;
	}

	public boolean isCallNext() {
		return callNext;
	}

	public void setCallNext(boolean callNext) {
		this.callNext = callNext;
	}

	public boolean isSkip() {
		return skip;
	}

	public void setSkip(boolean skip) {
		this.skip = skip;
	}

	public String getStrTrnCode() {
		return strTrnCode;
	}

	public void setStrTrnCode(String strTrnCode) {
		this.strTrnCode = strTrnCode;
	}

	public PermitRenewalsDTO getCheckAndDisplayRemarkValue() {
		return checkAndDisplayRemarkValue;
	}

	public void setCheckAndDisplayRemarkValue(PermitRenewalsDTO checkAndDisplayRemarkValue) {
		this.checkAndDisplayRemarkValue = checkAndDisplayRemarkValue;
	}

	public List<PermitRenewalsDTO> getDataList() {
		return dataList;
	}

	public void setDataList(List<PermitRenewalsDTO> dataList) {
		this.dataList = dataList;
	}

	public int getActiveTabIndex() {
		return activeTabIndex;
	}

	public void setActiveTabIndex(int activeTabIndex) {
		this.activeTabIndex = activeTabIndex;
	}

	public List<DocumentManagementDTO> getMandatoryList() {
		return mandatoryList;
	}

	public void setMandatoryList(List<DocumentManagementDTO> mandatoryList) {
		this.mandatoryList = mandatoryList;
	}

	public List<DocumentManagementDTO> getOptionalList() {
		return optionalList;
	}

	public void setOptionalList(List<DocumentManagementDTO> optionalList) {
		this.optionalList = optionalList;
	}

	public DocumentManagementService getDocumentManagementService() {
		return documentManagementService;
	}

	public void setDocumentManagementService(DocumentManagementService documentManagementService) {
		this.documentManagementService = documentManagementService;
	}

	public PaymentVoucherDTO getPaymentVoucherDTO() {
		return paymentVoucherDTO;
	}

	public void setPaymentVoucherDTO(PaymentVoucherDTO paymentVoucherDTO) {
		this.paymentVoucherDTO = paymentVoucherDTO;
	}

	public String getStrTrnDesc() {
		return strTrnDesc;
	}

	public void setStrTrnDesc(String strTrnDesc) {
		this.strTrnDesc = strTrnDesc;
	}

	public boolean isLoanObtained() {
		return loanObtained;
	}

	public void setLoanObtained(boolean loanObtained) {
		this.loanObtained = loanObtained;
	}

	public List<CommonDTO> getTitleList() {
		return titleList;
	}

	public void setTitleList(List<CommonDTO> titleList) {
		this.titleList = titleList;
	}

	public List<CommonDTO> getGenderList() {
		return genderList;
	}

	public void setGenderList(List<CommonDTO> genderList) {
		this.genderList = genderList;
	}

	public List<CommonDTO> getMartialList() {
		return martialList;
	}

	public void setMartialList(List<CommonDTO> martialList) {
		this.martialList = martialList;
	}

	public EmployeeProfileService getEmployeeProfileService() {
		return employeeProfileService;
	}

	public void setEmployeeProfileService(EmployeeProfileService employeeProfileService) {
		this.employeeProfileService = employeeProfileService;
	}

	public AmendmentBusOwnerDTO getAmendmentBusOwnerDTO() {
		return amendmentBusOwnerDTO;
	}

	public void setAmendmentBusOwnerDTO(AmendmentBusOwnerDTO amendmentBusOwnerDTO) {
		this.amendmentBusOwnerDTO = amendmentBusOwnerDTO;
	}

	public AmendmentOminiBusDTO getAmendmentOminiBusDTO() {
		return amendmentOminiBusDTO;
	}

	public void setAmendmentOminiBusDTO(AmendmentOminiBusDTO amendmentOminiBusDTO) {
		this.amendmentOminiBusDTO = amendmentOminiBusDTO;
	}

	public SetupCommitteeBoardDTO getSetupCommitteeBoardDTO() {
		return setupCommitteeBoardDTO;
	}

	public void setSetupCommitteeBoardDTO(SetupCommitteeBoardDTO setupCommitteeBoardDTO) {
		this.setupCommitteeBoardDTO = setupCommitteeBoardDTO;
	}

	public List<SetupCommitteeBoardDTO> getOrganizationList() {
		return organizationList;
	}

	public void setOrganizationList(List<SetupCommitteeBoardDTO> organizationList) {
		this.organizationList = organizationList;
	}

	public SetupCommitteeBoardService getSetupCommitteeBoardService() {
		return setupCommitteeBoardService;
	}

	public void setSetupCommitteeBoardService(SetupCommitteeBoardService setupCommitteeBoardService) {
		this.setupCommitteeBoardService = setupCommitteeBoardService;
	}

	public OrganizationDTO getOrganizationDTO() {
		return organizationDTO;
	}

	public void setOrganizationDTO(OrganizationDTO organizationDTO) {
		this.organizationDTO = organizationDTO;
	}

	public List<OrganizationDTO> getOtherOrgList() {
		return otherOrgList;
	}

	public void setOtherOrgList(List<OrganizationDTO> otherOrgList) {
		this.otherOrgList = otherOrgList;
	}

	public List<CommonDTO> getAccidentTypesList() {
		return accidentTypesList;
	}

	public void setAccidentTypesList(List<CommonDTO> accidentTypesList) {
		this.accidentTypesList = accidentTypesList;
	}

	public AccidentDTO getAccidentDTO() {
		return accidentDTO;
	}

	public void setAccidentDTO(AccidentDTO accidentDTO) {
		this.accidentDTO = accidentDTO;
	}

	public List<AccidentDTO> getAccidentList() {
		return accidentList;
	}

	public void setAccidentList(List<AccidentDTO> accidentList) {
		this.accidentList = accidentList;
	}

	public List<AccidentDTO> getMoreAccidentList() {
		return moreAccidentList;
	}

	public void setMoreAccidentList(List<AccidentDTO> moreAccidentList) {
		this.moreAccidentList = moreAccidentList;
	}

	public int getSelectedOtherOrg() {
		return selectedOtherOrg;
	}

	public void setSelectedOtherOrg(int selectedOtherOrg) {
		this.selectedOtherOrg = selectedOtherOrg;
	}

	public int getSelectedAccident() {
		return selectedAccident;
	}

	public void setSelectedAccident(int selectedAccident) {
		this.selectedAccident = selectedAccident;
	}

	public int getSelectedMoreAccident() {
		return selectedMoreAccident;
	}

	public void setSelectedMoreAccident(int selectedMoreAccident) {
		this.selectedMoreAccident = selectedMoreAccident;
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

	public boolean isLocalcheckcounter() {
		return localcheckcounter;
	}

	public void setLocalcheckcounter(boolean localcheckcounter) {
		this.localcheckcounter = localcheckcounter;
	}

	public boolean isSavedNewOminiBus() {
		return savedNewOminiBus;
	}

	public void setSavedNewOminiBus(boolean savedNewOminiBus) {
		this.savedNewOminiBus = savedNewOminiBus;
	}

	public boolean isOtherPermits() {
		return otherPermits;
	}

	public void setOtherPermits(boolean otherPermits) {
		this.otherPermits = otherPermits;
	}

	public boolean isOldOwner() {
		return oldOwner;
	}

	public void setOldOwner(boolean oldOwner) {
		this.oldOwner = oldOwner;
	}

	public boolean isNewOwner() {
		return newOwner;
	}

	public void setNewOwner(boolean newOwner) {
		this.newOwner = newOwner;
	}

	public boolean isOldBus() {
		return oldBus;
	}

	public void setOldBus(boolean oldBus) {
		this.oldBus = oldBus;
	}

	public boolean isNewBus() {
		return newBus;
	}

	public void setNewBus(boolean newBus) {
		this.newBus = newBus;
	}

	public AmendmentServiceDTO getAmendmentServiceDTO() {
		return amendmentServiceDTO;
	}

	public void setAmendmentServiceDTO(AmendmentServiceDTO amendmentServiceDTO) {
		this.amendmentServiceDTO = amendmentServiceDTO;
	}

	public AfterAccidentDTO getAfterAccidentDTO() {
		return afterAccidentDTO;
	}

	public void setAfterAccidentDTO(AfterAccidentDTO afterAccidentDTO) {
		this.afterAccidentDTO = afterAccidentDTO;
	}

	public boolean isRenderCancel() {
		return renderCancel;
	}

	public void setRenderCancel(boolean renderCancel) {
		this.renderCancel = renderCancel;
	}

	public boolean isRenderSearch() {
		return renderSearch;
	}

	public void setRenderSearch(boolean renderSearch) {
		this.renderSearch = renderSearch;
	}

	public boolean isRenderBackToAmendmentsApproval() {
		return renderBackToAmendmentsApproval;
	}

	public void setRenderBackToAmendmentsApproval(boolean renderBackToAmendmentsApproval) {
		this.renderBackToAmendmentsApproval = renderBackToAmendmentsApproval;
	}

	public PermitRenewalsDTO getPermitRenewalsDTO() {
		return permitRenewalsDTO;
	}

	public void setPermitRenewalsDTO(PermitRenewalsDTO permitRenewalsDTO) {
		this.permitRenewalsDTO = permitRenewalsDTO;
	}

	public AmendmentServiceDTO getTempNewRouteDTO() {
		return tempNewRouteDTO;
	}

	public void setTempNewRouteDTO(AmendmentServiceDTO tempNewRouteDTO) {
		this.tempNewRouteDTO = tempNewRouteDTO;
	}

	public boolean isRenderBack() {
		return renderBack;
	}

	public void setRenderBack(boolean renderBack) {
		this.renderBack = renderBack;
	}

	public boolean isShowInquiryBack() {
		return showInquiryBack;
	}

	public void setShowInquiryBack(boolean showInquiryBack) {
		this.showInquiryBack = showInquiryBack;
	}

	public boolean isRenderHighAuthBackButton() {
		return renderHighAuthBackButton;
	}

	public void setRenderHighAuthBackButton(boolean renderHighAuthBackButton) {
		this.renderHighAuthBackButton = renderHighAuthBackButton;
	}

	public SimRegistrationDTO getSimRegistrationDTO() {
		return simRegistrationDTO;
	}

	public void setSimRegistrationDTO(SimRegistrationDTO simRegistrationDTO) {
		this.simRegistrationDTO = simRegistrationDTO;
	}

	public List<SimRegistrationDTO> getEmiDetViewList() {
		return emiDetViewList;
	}

	public void setEmiDetViewList(List<SimRegistrationDTO> emiDetViewList) {
		this.emiDetViewList = emiDetViewList;
	}

	public ManageInquiryService getManageInquiryService() {
		return manageInquiryService;
	}

	public void setManageInquiryService(ManageInquiryService manageInquiryService) {
		this.manageInquiryService = manageInquiryService;
	}

	public FlyingSquadChargeSheetService getFlyingSquadChargeSheetService() {
		return flyingSquadChargeSheetService;
	}

	public void setFlyingSquadChargeSheetService(FlyingSquadChargeSheetService flyingSquadChargeSheetService) {
		this.flyingSquadChargeSheetService = flyingSquadChargeSheetService;
	}

	public ArrayList<FluingSquadVioConditionDTO> getConditionList() {
		return conditionList;
	}

	public void setConditionList(ArrayList<FluingSquadVioConditionDTO> conditionList) {
		this.conditionList = conditionList;
	}

	public ArrayList<FlyingSquadVioDocumentsDTO> getDocumentList() {
		return documentList;
	}

	public void setDocumentList(ArrayList<FlyingSquadVioDocumentsDTO> documentList) {
		this.documentList = documentList;
	}

	public FlyingManageInvestigationLogDTO getFlyingManageInvestigationLogDTO() {
		return flyingManageInvestigationLogDTO;
	}

	public void setFlyingManageInvestigationLogDTO(FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO) {
		this.flyingManageInvestigationLogDTO = flyingManageInvestigationLogDTO;
	}

	public List<FlyingManageInvestigationLogDTO> getInvestigationDetViewList() {
		return investigationDetViewList;
	}

	public void setInvestigationDetViewList(List<FlyingManageInvestigationLogDTO> investigationDetViewList) {
		this.investigationDetViewList = investigationDetViewList;
	}

	public FlyingManageInvestigationLogDTO getViewInvSelect() {
		return viewInvSelect;
	}

	public void setViewInvSelect(FlyingManageInvestigationLogDTO viewInvSelect) {
		this.viewInvSelect = viewInvSelect;
	}

	public FlyingManageInvestigationLogDTO getSelectedInvDTO() {
		return selectedInvDTO;
	}

	public void setSelectedInvDTO(FlyingManageInvestigationLogDTO selectedInvDTO) {
		this.selectedInvDTO = selectedInvDTO;
	}

	public ComplaintRequestDTO getPublicComplanDto() {
		return publicComplanDto;
	}

	public void setPublicComplanDto(ComplaintRequestDTO publicComplanDto) {
		this.publicComplanDto = publicComplanDto;
	}

	public List<ComplaintRequestDTO> getComplainDetViewList() {
		return complainDetViewList;
	}

	public void setComplainDetViewList(List<ComplaintRequestDTO> complainDetViewList) {
		this.complainDetViewList = complainDetViewList;
	}

	public boolean isRenderComplainDet() {
		return renderComplainDet;
	}

	public void setRenderComplainDet(boolean renderComplainDet) {
		this.renderComplainDet = renderComplainDet;
	}

	public ComplaintRequestDTO getViewSelect() {
		return viewSelect;
	}

	public void setViewSelect(ComplaintRequestDTO viewSelect) {
		this.viewSelect = viewSelect;
	}

	public ComplaintRequestDTO getSelectedComplaintDTO() {
		return selectedComplaintDTO;
	}

	public void setSelectedComplaintDTO(ComplaintRequestDTO selectedComplaintDTO) {
		this.selectedComplaintDTO = selectedComplaintDTO;
	}

	public boolean isRenderThreeButtons() {
		return renderThreeButtons;
	}

	public void setRenderThreeButtons(boolean renderThreeButtons) {
		this.renderThreeButtons = renderThreeButtons;
	}

	public MigratedService getMigratedService() {
		return migratedService;
	}

	public void setMigratedService(MigratedService migratedService) {
		this.migratedService = migratedService;
	}

}