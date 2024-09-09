package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

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
import lk.informatics.ntc.model.dto.VehicleInspectionDTO;
import lk.informatics.ntc.model.exception.ApplicationException;
import lk.informatics.ntc.model.service.AdminService;
import lk.informatics.ntc.model.service.AmendmentService;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.DocumentManagementService;
import lk.informatics.ntc.model.service.EmployeeProfileService;
import lk.informatics.ntc.model.service.FlyingSquadChargeSheetService;
import lk.informatics.ntc.model.service.HistoryService;
import lk.informatics.ntc.model.service.IssuePermitService;
import lk.informatics.ntc.model.service.ManageInquiryService;
import lk.informatics.ntc.model.service.MigratedService;
import lk.informatics.ntc.model.service.PermitRenewalsService;
import lk.informatics.ntc.model.service.QueueManagementService;
import lk.informatics.ntc.model.service.SetupCommitteeBoardService;
import lk.informatics.ntc.model.service.VehicleInspectionService;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.ntc.view.util.PropertyReader;
import lk.informatics.ntc.view.util.SpringApplicationContex;
import lk.informatics.unicode.UnicodeShaper;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

@ManagedBean(name = "editBusAmendmentBackingBeanTwo")
@ViewScoped

public class EditBusAmendmentBackingBeanTwo implements Serializable {
	/**
	 * 
	 */
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
	private String validationMsg;
	private String validationMsgforComplain;

	private boolean oldOwner;
	private boolean newOwner;
	private boolean oldBus;
	private boolean newBus;
	private boolean serviceChange;
	private boolean finalCheckList;

	private boolean routeFlag;

	private boolean loanObtained;

	private boolean callNext = false;
	private boolean skip = true;

	private boolean localcheckcounter = true;

	private boolean savedNewOminiBus = false;

	private boolean otherPermits = false;

	private StreamedContent files;

	private boolean disabledGender = false;
	private boolean disabledDOB = false;
	private boolean checkValiationsForInputFields = false;

	// Add renewals attributes
	private boolean showbacklogvalue = false;
	private boolean showbacklogvalue12 = false;
	private boolean disabledReqPeriodInput = true;
	private boolean checkNewExpiryDateBoolean = false;
	private boolean showbacklogvalueLoad = false;
	private boolean requestNewPeriodReadOnly = false;
	private boolean showNewPermitDateInput = true;
	private String successMsg;

	private boolean disableSaveButton = false;

	private AmendmentDTO amendmentHistoryDTO, newOminiBusHistoryDTO;
	private PermitRenewalsService permitRenewalsService;
	private PermitRenewalsDTO ownerHistoryDTO, applicationHistoryDTO;
	private AmendmentServiceDTO routeRequestHistoryDTO;
	private OminiBusDTO ominiBusHistoryDTO;
	private AccidentDTO accidentMasterHistoryDTO, accidentDetailsHistoryDTO, moreAccidentDTO;;
	private HistoryService historyService;

	private String selectedVia;
	private String selectedOrgin;
	private String selectedDestination, selectedRoute, selectedDistance;

	public EditBusAmendmentBackingBeanTwo() {

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

		disableSaveButton = false;

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

		permitRenewalsService = (PermitRenewalsService) SpringApplicationContex.getBean("permitRenewalsService");

		historyService = (HistoryService) SpringApplicationContex.getBean("historyService");

		manageInquiryService = (ManageInquiryService) SpringApplicationContex.getBean("manageInquiryService");
		

		/*provincelList = adminService.getProvinceToDropdown();

		relationshipList = amendmentService.getRelationships();

		routefordropdownList = issuePermitService.getRoutesToDropdown();

		titleList = employeeProfileService.GetTitleToDropdown();
		genderList = employeeProfileService.GetGenderToDropdown();
		martialList = employeeProfileService.GetMartialToDropdown();

		organizationList = setupCommitteeBoardService.getOrganizationListList();

		accidentTypesList = amendmentService.getAccidentTypesList();*/

		counterList = commonService.countersDropdown("13,05,14,15,16,21,22,23");
		organizationList = setupCommitteeBoardService.getOrganizationListList();
		routefordropdownList = issuePermitService.getRoutesToDropdown();
		validationMsg = null;
		validationMsgforComplain = null;

	}




	public void searchByApplicationNo() {
		boolean inspectionCompleted;
		if (generatedApplicationNo != null && generatedApplicationNo != " " && !generatedApplicationNo.isEmpty()) {
			strVehicleNo = amendmentService.getOldVehicleNoFromAmendment(generatedApplicationNo);

			ominiBusDTO.setVehicleRegNo(amendmentService.getNewVehicleNoFromAmendment(generatedApplicationNo));

			inspectionCompleted = commonService.checkTaskHistory(generatedApplicationNo, "PM100");
	
			
			if (strVehicleNo != null && !strVehicleNo.isEmpty() && !strVehicleNo.trim().equalsIgnoreCase("")) {
				callNext = true;
				if (ominiBusDTO.getVehicleRegNo() != null && !ominiBusDTO.getVehicleRegNo().isEmpty()
						&& !ominiBusDTO.getVehicleRegNo().trim().equalsIgnoreCase("")) {
					permitRenewalsDTO.setRegNoOfBus(ominiBusDTO.getVehicleRegNo());

					/*
					 * Validation for having ongoing complain,investigation or expired SIM
					 */
					if (strVehicleNo != null && !strVehicleNo.isEmpty() && !strVehicleNo.trim().equalsIgnoreCase("")) {
						commonDTO = commonService.vehicleNoValidation(strVehicleNo);
						// only have complain record
						if ((commonDTO.getComplainNo() != null) && (commonDTO.getGrantComplainNo() == null)) {
							errorMsg = " Active/Ongoing Complain is Available.";
							RequestContext.getCurrentInstance().update("frmrequiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
						} else {

							getApplicationDetailsByVehicleNo();
							getRenewalDetails();

							// Complain have grant permission
							if (commonDTO.getGrantComplainNo() != null) {
								validationMsgforComplain = "Remark : " + commonDTO.getGrantPermissionRemark();
							}

							// only have sim expire record
							if (commonDTO.getSimRegNo() != null) {
								validationMsg = "SIM is expired / payment is not completed.";
							}
							// only have investigation record
							if (commonDTO.getChargeRefNo() != null) {
								validationMsg = "Active/Ongoing Investigation is Available.";
							}

							// have both sim expire & investigation record
							else if (commonDTO.getSimRegNo() != null && commonDTO.getChargeRefNo() != null) {
								validationMsg = "SIM is Expired and Active/Ongoing Investigation is Available.";
							}

							if (validationMsgforComplain != null) {
								RequestContext.getCurrentInstance().update("frmvehicleComplainValidate");
								RequestContext.getCurrentInstance().execute("PF('vehicleComplainValidate').show()");
							} else if (validationMsg != null) {
								RequestContext.getCurrentInstance().update("frmvehicleValidate");
								RequestContext.getCurrentInstance().execute("PF('vehicleValidate').show()");
							}

						}
					}

				}
			}
			else {
				errorMsg = "Application No. is required";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

			}
		
			

			VehicleInspectionDTO taskCodeDTO = vehicleInspectionService.getTaskDet(generatedApplicationNo,
					strVehicleNo);
			if ((taskCodeDTO.getTaskDetCode().equals("AM100") && taskCodeDTO.getTaskDetStatus().equals("C"))
					|| (taskCodeDTO.getTaskDetCode().equals("AM100") && taskCodeDTO.getTaskDetStatus().equals("O"))
					|| taskCodeDTO.getTaskDetCode().equals("AM101") && taskCodeDTO.getTaskDetStatus().equals("O")
					|| taskCodeDTO.getTaskDetCode().equals("AM107") && taskCodeDTO.getTaskDetStatus().equals("C")
					|| taskCodeDTO.getTaskDetCode().equals("AM109") && taskCodeDTO.getTaskDetStatus().equals("C")
					|| taskCodeDTO.getTaskDetCode().equals("AM111") && taskCodeDTO.getTaskDetStatus().equals("C")
					|| taskCodeDTO.getTaskDetCode().equals("AM112") && taskCodeDTO.getTaskDetStatus().equals("C")
					|| taskCodeDTO.getTaskDetCode().equals("AM101") && taskCodeDTO.getTaskDetStatus().equals("C")
					|| taskCodeDTO.getTaskDetCode().equals("AM102") && taskCodeDTO.getTaskDetStatus().equals("O")
					|| taskCodeDTO.getTaskDetCode().equals("AM102") && taskCodeDTO.getTaskDetStatus().equals("C")
					|| taskCodeDTO.getTaskDetCode().equals("AM103") && taskCodeDTO.getTaskDetStatus().equals("O")
					|| taskCodeDTO.getTaskDetCode().equals("AM103") && taskCodeDTO.getTaskDetStatus().equals("C"))
			/**
			 * AM101,AM102 O and C added by tharushi.e for new requirement 2021-01-14
			 **/
			{
				disableSaveButton = false;
				
			} else if(taskCodeDTO.getTaskDetCode().equals("PM100") && taskCodeDTO.getTaskDetStatus().equals("C")){
					
				disableSaveButton = true;
				errorMsg = " Amendment Inspection is Ongoing.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				return;
			}
			 else if(inspectionCompleted){
				 
					disableSaveButton = true;
					errorMsg = " Amendment Inspection is  Completed.";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					return;
				} 
			else{
				
				disableSaveButton = true;
			}
			
		} else {
			errorMsg = "Application No. is required";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		}

	}

	private void getRenewalDetails() {
		OminiBusDTO omniDetDto = new OminiBusDTO();
		if (!strVehicleNo.equals("") && strVehicleNo != null) {
			// 24-10-2019
			// permitRenewalsDTO =
			// amendmentService.renewalsByBusRegNo(permitRenewalsDTO.getRegNoOfBus());
			permitRenewalsDTO = amendmentService.renewalsByBusRegNoNew(permitRenewalsDTO.getRegNoOfBus(),
					generatedApplicationNo);
			omniDetDto = amendmentService.insuranceDetByBusRegNoNew(strVehicleNo, generatedApplicationNo);
			permitRenewalsDTO.setEmissionExpDate(omniDetDto.getEmissionExpDate());
			permitRenewalsDTO.setRevenueExpDate(omniDetDto.getExpiryDateRevLic());
			permitRenewalsDTO.setInsuCompName(omniDetDto.getInsuCompName());
			permitRenewalsDTO.setInsuExpDate(omniDetDto.getInsuExpDate());
			permitRenewalsDTO.setInsuCat(omniDetDto.getInsuCat());
			permitRenewalsDTO.setPolicyNo(omniDetDto.getPolicyNo());
			
			permitRenewalsDTO.setSerialNo(omniDetDto.getSerialNo());
			permitRenewalsDTO.setGarageName(omniDetDto.getGarageName());
			permitRenewalsDTO.setGarageRegNo(omniDetDto.getGarageRegNo());
			permitRenewalsDTO.setFitnessCertiDate(omniDetDto.getFitnessCertiDate());

			if (omniDetDto.getInsuCat() == null) {
				omniDetDto.setInsuCat("UNLIMITED");
				permitRenewalsDTO.setInsuCat(omniDetDto.getInsuCat());
			} else if (omniDetDto.getInsuCat() != null) {
				omniDetDto.setInsuCat(omniDetDto.getInsuCat());
				permitRenewalsDTO.setInsuCat(omniDetDto.getInsuCat());
			}
			String format = "dd/MM/yyyy";
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			if (permitRenewalsDTO.getValidToDate() != null) {

				Date validDateObj;
				try {
					validDateObj = sdf.parse(permitRenewalsDTO.getValidToDate());
					permitRenewalsDTO.setPermitExpiredValidToDateObj(validDateObj);

				} catch (ParseException e) {

					e.printStackTrace();
				}

			} else {

				setDisabledReqPeriodInput(true);
			}
			if (permitRenewalsDTO.getFromToDate() != null) {

				Date fromDateObj;
				try {
					fromDateObj = sdf.parse(permitRenewalsDTO.getFromToDate());
					permitRenewalsDTO.setPermitExpiredFromDateObj(fromDateObj);

				} catch (ParseException e) {

					e.printStackTrace();
				}

			} else {

				Date permitExpiredDateForFromDate;
				try {
					permitExpiredDateForFromDate = sdf.parse(permitRenewalsDTO.getPermitExpiryDate());
					permitRenewalsDTO.setPermitExpiredFromDateObj(permitExpiredDateForFromDate);
				} catch (ParseException e) {

					e.printStackTrace();
				}

			}
			setShowbacklogvalue(permitRenewalsDTO.isCheckBacklogValue());
			setShowbacklogvalue12(permitRenewalsDTO.isCheckBacklogValue());
			if (permitRenewalsDTO.getPermitExpiredValidToDateObj() != null) {
				setDisabledReqPeriodInput(false);
			}

		} else {

		}
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

			oldBus = true;
			newBus = true;
			serviceChange = true;
			finalCheckList = true;
			break;
		default:
			strAmendmentType = null;

			break;
		}

		if (strVehicleNo != null && !strVehicleNo.isEmpty() && strVehicleNo != "") {

			if (strTrnCode.equals("13") || strTrnCode == "13" || strTrnCode.equals("14") || strTrnCode == "14") {
				amendmentBusOwnerDTO = amendmentService.getApplicationDetailsByVehicleNo(strVehicleNo);
			} 
			
			else if(strTrnCode.equals("15")) {
				amendmentBusOwnerDTO = amendmentService.getApplicationDetailsByExistingVehicleNo(strVehicleNo);
			}
			
			else {
				amendmentBusOwnerDTO = amendmentService.getApplicationDetailsByVehicleNoAndAppNo(strVehicleNo,
						generatedApplicationNo);// modify by tharushi.e
			}

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

			/**
			 * added by tharushi.e for showing Service End change data
			 */
			if (amendmentBusOwnerDTO.getRouteFlag() != null) {
				if (amendmentBusOwnerDTO.getRouteFlag().equalsIgnoreCase("Y")) {
					amendmentDTO.setOrigin(amendmentBusOwnerDTO.getDestination());
					amendmentDTO.setDestination(amendmentBusOwnerDTO.getOrigin());
					amendmentDTO.setRouteNo(amendmentBusOwnerDTO.getRouteNo());
				} else {
					amendmentDTO.setOrigin(amendmentBusOwnerDTO.getOrigin());
					amendmentDTO.setDestination(amendmentBusOwnerDTO.getDestination());
					amendmentDTO.setRouteNo(amendmentBusOwnerDTO.getRouteNo());
				}
			}
			amendmentDTO.setVia(amendmentBusOwnerDTO.getVia());
			amendmentDTO.setDistance(amendmentBusOwnerDTO.getDistance());
			/**
			 * End
			 */
			selectedVia = amendmentDTO.getVia();
			selectedOrgin = amendmentDTO.getOrigin();
			selectedDestination = amendmentDTO.getDestination();
			selectedRoute = amendmentDTO.getRouteNo();
			/** added by tharushi.e for distance null issue **/
			if (amendmentDTO.getDistance() == null) {
				errorMsg = "Please fill the distance for this route no.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

			} else if (!amendmentDTO.getDistance().equals(null)) {
				selectedDistance = amendmentDTO.getDistance().toString();
			}

			

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

				else {

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

				else {

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

			busOwnerDTO = amendmentService.getOwnerDetails(generatedApplicationNo);

			amendmentDTO = amendmentService.getAmendmentDetails(generatedApplicationNo);


			//onChangeTitle();

		}

	}

	public void saveActionTaskHandler() {
		if (!commonService.checkTaskHisByApplication(generatedApplicationNo, "AM100", "C")) {
			boolean taskStatus;
			taskStatus = commonService.checkTaskOnTaskDetails(generatedApplicationNo, "AM100", "O");
			if (!taskStatus) {
				commonService.insertTaskDet(strVehicleNo, generatedApplicationNo, sessionBackingBean.getLoginUser(),
						"AM100", "O");
			}
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

	



	

	
	public void clearAll() {
		init();
	}

	
	public void ajaxInsertPhysicalExitRecord() {

		for (int i = 0; i < dataList.size(); i++) {

			if (dataList.get(i).isPhysicallyExists() == true) {

				amendmentService.insertDocumentPhysicallyExitsStatus(dataList.get(i).isPhysicallyExists(),
						dataList.get(i).getDocumentCode(), generatedApplicationNo, strPermitNo,
						sessionBackingBean.getLoginUser(), strTrnCode);

			} else {
				amendmentService.deleteDocumentPhysicallyExitsStatus(dataList.get(i).isPhysicallyExists(),
						dataList.get(i).getDocumentCode(), generatedApplicationNo, strPermitNo,
						sessionBackingBean.getLoginUser(), strTrnCode);
			}
		}

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

								if (loanObtained) {
									
										//searchByApplicationNo();
									
									ominiBusDTO.setIsLoanObtained("Y");

									if (ominiBusDTO.getBankLoan() != null) {

										if (ominiBusDTO.getDueAmount() != null) {

											if (ominiBusDTO.getFinanceCompany() != null
													&& !ominiBusDTO.getFinanceCompany().isEmpty()
													&& !ominiBusDTO.getFinanceCompany().equalsIgnoreCase("")) {

												if (ominiBusDTO.getDateObtained() != null) {

													if (ominiBusDTO.getLapsedInstall() != null
															&& !ominiBusDTO.getLapsedInstall().isEmpty()
															&& !ominiBusDTO.getLapsedInstall().equalsIgnoreCase("")) {

														

															// update
															ominiBusDTO
																	.setModifiedBy(sessionBackingBean.getLoginUser());
															ominiBusDTO.setPermitNo(strPermitNo);

															ominiBusDTO.setApplicationNo(generatedApplicationNo);

															int result = issuePermitService
																	.updateNewPermitOminiBus(ominiBusDTO);

															if (result == 0) {
															

																activeTabIndex = 4;

																amendmentHistoryDTO = historyService
																		.getAmendmentTableData(generatedApplicationNo,
																				sessionBackingBean.getLoginUser());
																amendmentDTO.setBusRegNo(ominiBusDTO.getVehicleRegNo());
																amendmentService.updateAmendmentDetails(amendmentDTO);
																historyService.insertAmendmentsHistoryData(
																		amendmentHistoryDTO);

																/*
																 * Update Application History Data
																 */
																applicationHistoryDTO = historyService
																		.getApplicationTableData(generatedApplicationNo,
																				sessionBackingBean.getLoginUser());
																amendmentService.updateVehicleOfApplication(
																		ominiBusDTO.getVehicleRegNo(),
																		generatedApplicationNo,
																		sessionBackingBean.getLoginUser());
																historyService.insertApplicationHistoryData(
																		applicationHistoryDTO);
																
																/**update task history**/
																commonService.updateCommonTaskHistory(ominiBusDTO.getVehicleRegNo(), generatedApplicationNo, "AM113", "C", sessionBackingBean.getLoginUser());
																/**end update task history**/
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
														errorMsg = "Lapsed Installments should be entered.";
														RequestContext.getCurrentInstance().update("frmrequiredField");
														RequestContext.getCurrentInstance()
																.execute("PF('requiredField').show()");
													}

												} else {
													errorMsg = "Date Obtained should be entered.";
													RequestContext.getCurrentInstance().update("frmrequiredField");
													RequestContext.getCurrentInstance()
															.execute("PF('requiredField').show()");
												}

											} else {
												errorMsg = "Name of Finance Company / Loan Obtained from should be entered.";
												RequestContext.getCurrentInstance().update("frmrequiredField");
												RequestContext.getCurrentInstance()
														.execute("PF('requiredField').show()");
											}

										} else {
											errorMsg = "Due amount at present should be entered.";
											RequestContext.getCurrentInstance().update("frmrequiredField");
											RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
										}

									} else {
										errorMsg = "Bank Loan should be entered.";
										RequestContext.getCurrentInstance().update("frmrequiredField");
										RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
									}
								
								} else {
									
										// no loan

										ominiBusDTO.setIsLoanObtained("N");

								
											// update
											ominiBusDTO.setModifiedBy(sessionBackingBean.getLoginUser());
											ominiBusDTO.setPermitNo(strPermitNo);
											ominiBusDTO.setApplicationNo(generatedApplicationNo);

										

											/* Update OminiBus History Table and VehiOwner History Table */
											int result = issuePermitService.updateNewPermitOminiBus(ominiBusDTO);

											if (result == 0) {

												savedNewOminiBus = true;
												amendmentDTO.setModifiedBy(sessionBackingBean.getLoginUser());

												/* Update Amendment History Table */
												amendmentHistoryDTO = historyService.getAmendmentTableData(
														generatedApplicationNo, sessionBackingBean.getLoginUser());
												amendmentDTO.setBusRegNo(ominiBusDTO.getVehicleRegNo());
												amendmentService.updateAmendmentDetails(amendmentDTO);
												historyService.insertAmendmentsHistoryData(amendmentHistoryDTO);

												/*
												 * Update Application Table History Table
												 */
												applicationHistoryDTO = historyService.getApplicationTableData(
														generatedApplicationNo, sessionBackingBean.getLoginUser());
												amendmentService.updateVehicleOfApplication(
														ominiBusDTO.getVehicleRegNo(), generatedApplicationNo,
														sessionBackingBean.getLoginUser());
												historyService.insertApplicationHistoryData(applicationHistoryDTO);
												/**update task history**/
												commonService.updateCommonTaskHistory(ominiBusDTO.getVehicleRegNo(), generatedApplicationNo, "AM113", "C", sessionBackingBean.getLoginUser());
												/**end update task history**/

												getOminiBusDetails();
												otherOrgList = amendmentService.getOtherOrgList(generatedApplicationNo);

												RequestContext.getCurrentInstance().update("frmOminiBusInfo");
												RequestContext.getCurrentInstance().execute("PF('successSve').show()");

											} else {
												RequestContext.getCurrentInstance()
														.execute("PF('generalError').show()");
											}
										

									
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
			permitRenewalsDTO.setEmissionExpDate(ominiBusDTO.getEmissionExpDate());
			permitRenewalsDTO.setRevenueExpDate(ominiBusDTO.getRevenueExpDate());
			permitRenewalsDTO.setInsuCompName(ominiBusDTO.getInsuCompName());
			permitRenewalsDTO.setInsuExpDate(ominiBusDTO.getInsuExpDate());
			permitRenewalsDTO.setInsuCat(ominiBusDTO.getInsuCat());
			permitRenewalsDTO.setPolicyNo(ominiBusDTO.getPolicyNo());

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

			if (ominiBusDTO.getInsuCat() == null) {
				ominiBusDTO.setInsuCat("UNLIMITED");
				permitRenewalsDTO.setInsuCat(ominiBusDTO.getInsuCat());
			} else if (ominiBusDTO.getInsuCat() != null) {
				ominiBusDTO.setInsuCat(ominiBusDTO.getInsuCat());
				permitRenewalsDTO.setInsuCat(ominiBusDTO.getInsuCat());
			}

		}
	}

	public void clearBusDetails() {
		ominiBusDTO = new OminiBusDTO();

	}

	
	public void clearOtherOrg() {
		organizationDTO = new OrganizationDTO();
		otherOrgList = amendmentService.getOtherOrgList(generatedApplicationNo);
	}

	public void addOtherOrg() {
		organizationDTO.setApplicationNo(generatedApplicationNo);
		organizationDTO.setCreatedBy(sessionBackingBean.getLoginUser());
		organizationDTO.setVehicleNo(ominiBusDTO.getVehicleRegNo());
		organizationDTO.setAmendmentSeq(amendmentDTO.getSeq());
		amendmentService.saveOtherOrg(organizationDTO);
		clearOtherOrg();

		RequestContext.getCurrentInstance().update("otherPermits");
		RequestContext.getCurrentInstance().execute("PF('successSve').show()");
	}

	public void addAccident() {

		accidentDTO.setCreatedBy(sessionBackingBean.getLoginUser());
		accidentDTO.setVehicleNo(ominiBusDTO.getVehicleRegNo());

		amendmentService.saveAccident(accidentDTO);
		clearAccident();

		RequestContext.getCurrentInstance().update("accidentList");
		RequestContext.getCurrentInstance().execute("PF('successSve').show()");

	}

	public void clearAccident() {
		accidentDTO = new AccidentDTO();
		accidentList = amendmentService.getAccidentList(ominiBusDTO.getVehicleRegNo());
	}

	public void addMoreAccident() {

		accidentDTO.setCreatedBy(sessionBackingBean.getLoginUser());
		accidentDTO.setVehicleNo(ominiBusDTO.getVehicleRegNo());

		amendmentService.saveMoreAccident(accidentDTO);
		clearMoreAccident();

		RequestContext.getCurrentInstance().update("moreAccidentList");
		RequestContext.getCurrentInstance().execute("PF('successSve').show()");

	}

	public void clearMoreAccident() {
		accidentDTO = new AccidentDTO();
		moreAccidentList = amendmentService.getMoreAccidentList(ominiBusDTO.getVehicleRegNo());

	}

	

	public void removeOtherOrg() {

		if (selectedOtherOrg != 0) {

			/* Update newOmini Bus History Table */
			newOminiBusHistoryDTO = historyService.getNewOminiBusTableData(selectedOtherOrg,
					sessionBackingBean.getLoginUser());
			amendmentService.removeOtherOrg(selectedOtherOrg);
			historyService.insertNewOminiBusHistoryData(newOminiBusHistoryDTO);

			otherOrgList = new ArrayList<OrganizationDTO>(0);
			otherOrgList = amendmentService.getOtherOrgList(generatedApplicationNo);
			RequestContext.getCurrentInstance().update("otherOrgList");
			RequestContext.getCurrentInstance().update("frmsuccessSve");

			errorMsg = "Record deleted";
			RequestContext.getCurrentInstance().execute("PF('frmrequiredField').show()");
		} else {

			errorMsg = "You can not delete this record.";
			RequestContext.getCurrentInstance().execute("PF('frmrequiredField').show()");
		}
		RequestContext.getCurrentInstance().update("frmNewOmnibus1");
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

	public boolean isDisabledGender() {
		return disabledGender;
	}

	public void setDisabledGender(boolean disabledGender) {
		this.disabledGender = disabledGender;
	}

	public boolean isDisabledDOB() {
		return disabledDOB;
	}

	public void setDisabledDOB(boolean disabledDOB) {
		this.disabledDOB = disabledDOB;
	}

	public boolean isCheckValiationsForInputFields() {
		return checkValiationsForInputFields;
	}

	public void setCheckValiationsForInputFields(boolean checkValiationsForInputFields) {
		this.checkValiationsForInputFields = checkValiationsForInputFields;
	}

	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
	}

	public boolean isShowbacklogvalue() {
		return showbacklogvalue;
	}

	public void setShowbacklogvalue(boolean showbacklogvalue) {
		this.showbacklogvalue = showbacklogvalue;
	}

	public boolean isShowbacklogvalue12() {
		return showbacklogvalue12;
	}

	public void setShowbacklogvalue12(boolean showbacklogvalue12) {
		this.showbacklogvalue12 = showbacklogvalue12;
	}

	public boolean isDisabledReqPeriodInput() {
		return disabledReqPeriodInput;
	}

	public void setDisabledReqPeriodInput(boolean disabledReqPeriodInput) {
		this.disabledReqPeriodInput = disabledReqPeriodInput;
	}

	public boolean isCheckNewExpiryDateBoolean() {
		return checkNewExpiryDateBoolean;
	}

	public void setCheckNewExpiryDateBoolean(boolean checkNewExpiryDateBoolean) {
		this.checkNewExpiryDateBoolean = checkNewExpiryDateBoolean;
	}

	public boolean isShowbacklogvalueLoad() {
		return showbacklogvalueLoad;
	}

	public void setShowbacklogvalueLoad(boolean showbacklogvalueLoad) {
		this.showbacklogvalueLoad = showbacklogvalueLoad;
	}

	public boolean isRequestNewPeriodReadOnly() {
		return requestNewPeriodReadOnly;
	}

	public void setRequestNewPeriodReadOnly(boolean requestNewPeriodReadOnly) {
		this.requestNewPeriodReadOnly = requestNewPeriodReadOnly;
	}

	public boolean isShowNewPermitDateInput() {
		return showNewPermitDateInput;
	}

	public void setShowNewPermitDateInput(boolean showNewPermitDateInput) {
		this.showNewPermitDateInput = showNewPermitDateInput;
	}

	public String getSuccessMsg() {
		return successMsg;
	}

	public void setSuccessMsg(String successMsg) {
		this.successMsg = successMsg;
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

	public AmendmentDTO getAmendmentHistoryDTO() {
		return amendmentHistoryDTO;
	}

	public void setAmendmentHistoryDTO(AmendmentDTO amendmentHistoryDTO) {
		this.amendmentHistoryDTO = amendmentHistoryDTO;
	}

	public AmendmentDTO getNewOminiBusHistoryDTO() {
		return newOminiBusHistoryDTO;
	}

	public void setNewOminiBusHistoryDTO(AmendmentDTO newOminiBusHistoryDTO) {
		this.newOminiBusHistoryDTO = newOminiBusHistoryDTO;
	}

	public PermitRenewalsService getPermitRenewalsService() {
		return permitRenewalsService;
	}

	public void setPermitRenewalsService(PermitRenewalsService permitRenewalsService) {
		this.permitRenewalsService = permitRenewalsService;
	}

	public PermitRenewalsDTO getOwnerHistoryDTO() {
		return ownerHistoryDTO;
	}

	public void setOwnerHistoryDTO(PermitRenewalsDTO ownerHistoryDTO) {
		this.ownerHistoryDTO = ownerHistoryDTO;
	}

	public PermitRenewalsDTO getApplicationHistoryDTO() {
		return applicationHistoryDTO;
	}

	public void setApplicationHistoryDTO(PermitRenewalsDTO applicationHistoryDTO) {
		this.applicationHistoryDTO = applicationHistoryDTO;
	}

	public AmendmentServiceDTO getRouteRequestHistoryDTO() {
		return routeRequestHistoryDTO;
	}

	public void setRouteRequestHistoryDTO(AmendmentServiceDTO routeRequestHistoryDTO) {
		this.routeRequestHistoryDTO = routeRequestHistoryDTO;
	}

	public OminiBusDTO getOminiBusHistoryDTO() {
		return ominiBusHistoryDTO;
	}

	public void setOminiBusHistoryDTO(OminiBusDTO ominiBusHistoryDTO) {
		this.ominiBusHistoryDTO = ominiBusHistoryDTO;
	}

	public AccidentDTO getAccidentMasterHistoryDTO() {
		return accidentMasterHistoryDTO;
	}

	public void setAccidentMasterHistoryDTO(AccidentDTO accidentMasterHistoryDTO) {
		this.accidentMasterHistoryDTO = accidentMasterHistoryDTO;
	}

	public AccidentDTO getAccidentDetailsHistoryDTO() {
		return accidentDetailsHistoryDTO;
	}

	public void setAccidentDetailsHistoryDTO(AccidentDTO accidentDetailsHistoryDTO) {
		this.accidentDetailsHistoryDTO = accidentDetailsHistoryDTO;
	}

	public HistoryService getHistoryService() {
		return historyService;
	}

	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}

	public String getSelectedVia() {
		return selectedVia;
	}

	public void setSelectedVia(String selectedVia) {
		this.selectedVia = selectedVia;
	}

	public String getSelectedOrgin() {
		return selectedOrgin;
	}

	public void setSelectedOrgin(String selectedOrgin) {
		this.selectedOrgin = selectedOrgin;
	}

	public String getSelectedDestination() {
		return selectedDestination;
	}

	public void setSelectedDestination(String selectedDestination) {
		this.selectedDestination = selectedDestination;
	}

	public String getSelectedRoute() {
		return selectedRoute;
	}

	public void setSelectedRoute(String selectedRoute) {
		this.selectedRoute = selectedRoute;
	}

	public String getSelectedDistance() {
		return selectedDistance;
	}

	public void setSelectedDistance(String selectedDistance) {
		this.selectedDistance = selectedDistance;
	}

	public boolean isDisableSaveButton() {
		return disableSaveButton;
	}

	public void setDisableSaveButton(boolean disableSaveButton) {
		this.disableSaveButton = disableSaveButton;
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

	

	public AccidentDTO getMoreAccidentDTO() {
		return moreAccidentDTO;
	}

	public void setMoreAccidentDTO(AccidentDTO moreAccidentDTO) {
		this.moreAccidentDTO = moreAccidentDTO;
	}

	public String getValidationMsg() {
		return validationMsg;
	}

	public void setValidationMsg(String validationMsg) {
		this.validationMsg = validationMsg;
	}

	public MigratedService getMigratedService() {
		return migratedService;
	}

	public void setMigratedService(MigratedService migratedService) {
		this.migratedService = migratedService;
	}

	public String getValidationMsgforComplain() {
		return validationMsgforComplain;
	}

	public void setValidationMsgforComplain(String validationMsgforComplain) {
		this.validationMsgforComplain = validationMsgforComplain;
	}

}