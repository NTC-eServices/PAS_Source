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

@ManagedBean(name = "editAmendmentBackingBean")
@ViewScoped

public class EditAmendmentBackingBean implements Serializable {
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
	private boolean disableSaveInEndSave;
	public EditAmendmentBackingBean() {

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

		validationMsg = null;
		validationMsgforComplain = null;
		disableSaveInEndSave = false;
	}

	public void onChangeTitle() {

		String titleCode = adminService.getParaCodeForTitle();

		if (busOwnerDTO.getTitle().equals(titleCode)) {
			setDisabledDOB(true);
			setDisabledGender(true);
			setCheckValiationsForInputFields(true);
			busOwnerDTO.setGender(null);
			busOwnerDTO.setDob(null);
			RequestContext.getCurrentInstance().update("frmRelationOfInformation");
		} else {
			setDisabledDOB(false);
			setDisabledGender(false);
			setCheckValiationsForInputFields(false);
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
		boolean appStatus;
		if (generatedApplicationNo != null && generatedApplicationNo != " " && !generatedApplicationNo.isEmpty()) {
			strVehicleNo = amendmentService.getOldVehicleNoFromAmendment(generatedApplicationNo);

			ominiBusDTO.setVehicleRegNo(amendmentService.getNewVehicleNoFromAmendment(generatedApplicationNo));

			appStatus = commonService.checkTaskHistory(generatedApplicationNo, "AM105");

			if (appStatus) {
				errorMsg = "Amendment was completed";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				return;
			}

			/** 25-10-2019 **/ // task code PM100 C and O task added by
								// tharushi.e for edit amendment renewal
								// data after inspection
			VehicleInspectionDTO taskCodeDTO = vehicleInspectionService.getTaskDet(generatedApplicationNo,
					strVehicleNo);
			if (taskCodeDTO != null && taskCodeDTO.getTaskDetCode() != null && taskCodeDTO.getTaskDetStatus() != null && ((taskCodeDTO.getTaskDetCode().equals("AM100") && taskCodeDTO.getTaskDetStatus().equals("C"))
					|| (taskCodeDTO.getTaskDetCode().equals("AM100") && taskCodeDTO.getTaskDetStatus().equals("O"))
					|| taskCodeDTO.getTaskDetCode().equals("PM100") && taskCodeDTO.getTaskDetStatus().equals("O")
					|| taskCodeDTO.getTaskDetCode().equals("PM100") && taskCodeDTO.getTaskDetStatus().equals("C")
					|| taskCodeDTO.getTaskDetCode().equals("PM101") && taskCodeDTO.getTaskDetStatus().equals("O")
					|| taskCodeDTO.getTaskDetCode().equals("PM101") && taskCodeDTO.getTaskDetStatus().equals("C")
					|| taskCodeDTO.getTaskDetCode().equals("AM101") && taskCodeDTO.getTaskDetStatus().equals("O")
					|| taskCodeDTO.getTaskDetCode().equals("AM101") && taskCodeDTO.getTaskDetStatus().equals("C")
					|| taskCodeDTO.getTaskDetCode().equals("AM102") && taskCodeDTO.getTaskDetStatus().equals("O")
					|| taskCodeDTO.getTaskDetCode().equals("AM102") && taskCodeDTO.getTaskDetStatus().equals("C")))
			/**
			 * AM101,AM102 O and C added by tharushi.e for new requirement 2021-01-14
			 **/
			{
				disableSaveButton = false;
			} else
				disableSaveButton = true;
			/****/
			
			/***Restrict edit bus number after Inspection in BUS + SERVICE TYPE and BUS + OWNER Requested by Damith on 12/15/2021**/
			strTrnCode = amendmentService.getTrnTypeFromAmendment(generatedApplicationNo);
			if(strTrnCode != null && (strTrnCode.equals("15") || strTrnCode.equals("16"))) {
				boolean inspectionDoneOrOngoing = amendmentService.checkInspectionStatus(generatedApplicationNo);
				
				if (inspectionDoneOrOngoing) {

					disableSaveButton = true;
				} else {

					disableSaveButton = false;
				}
			}
			
			/**End**/
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
				if (permitRenewalsDTO.getPermitExpiryDate() != null) {
				Date permitExpiredDateForFromDate;
				try {
					permitExpiredDateForFromDate = sdf.parse(permitRenewalsDTO.getPermitExpiryDate());
					permitRenewalsDTO.setPermitExpiredFromDateObj(permitExpiredDateForFromDate);
				} catch (ParseException e) {

					e.printStackTrace();
				}
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

	public void onFromDateChange(SelectEvent event) {
		if (permitRenewalsDTO.getPermitExpiredFromDateObj() != null) {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			String dateFormat = "dd/MM/yyyy";
			SimpleDateFormat frm = new SimpleDateFormat(dateFormat);
			String fromDateNewValue = frm.format(event.getObject());

			permitRenewalsDTO.setFromToDate(fromDateNewValue);

		} else {

			setErrorMsg("From Date should be selected.");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public void onValidDateChange(SelectEvent event) {

		if (permitRenewalsDTO.getPermitExpiredValidToDateObj() != null) {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			String dateFormat = "dd/MM/yyyy";
			SimpleDateFormat frm = new SimpleDateFormat(dateFormat);

			String validDateNewValue = frm.format(event.getObject());

			permitRenewalsDTO.setValidToDate(validDateNewValue);

			setDisabledReqPeriodInput(false);
		} else {

			setErrorMsg("Valid to Date should be selected.");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public void onNewPermitDateChange() throws ParseException {

		if (permitRenewalsDTO.isCheckBacklogValue() == true && permitRenewalsDTO.getPermitExpiredFromDateObj() != null
				&& permitRenewalsDTO.getPermitExpiredValidToDateObj() != null) {
			String dateFormat = "dd/MM/yyyy";
			SimpleDateFormat frm = new SimpleDateFormat(dateFormat);
			Date newfromdateObj = permitRenewalsDTO.getPermitExpiredFromDateObj();
			Date newvaliddateObj = permitRenewalsDTO.getPermitExpiredValidToDateObj();
			String fromDateNewValue = frm.format(newfromdateObj);
			String validDateNewValue = frm.format(newvaliddateObj);
			permitRenewalsDTO.setFromToDate(fromDateNewValue);
			permitRenewalsDTO.setValidToDate(validDateNewValue);
		} else if (permitRenewalsDTO.isCheckBacklogValue() == false
				&& permitRenewalsDTO.getPermitExpiredFromDateObj() == null
				&& permitRenewalsDTO.getPermitExpiredValidToDateObj() == null) {

		}
		if (permitRenewalsDTO.getPermitExpiryDate() != null && permitRenewalsDTO.getValidToDate() != null) {

			if (permitRenewalsDTO.getRequestRenewPeriod() == 0) {

				permitRenewalsDTO.setNewPermitExpirtDate(null);
				RequestContext.getCurrentInstance().update("newExpiryDateId");
			} else {
				int noOfMonths = permitRenewalsDTO.getRequestRenewPeriod();
				String validDate = permitRenewalsDTO.getValidToDate();
				String fromDate = permitRenewalsDTO.getFromToDate();
				String beforepermitDate = permitRenewalsDTO.getPermitExpiryDate();
				String format = "dd/MM/yyyy";
				SimpleDateFormat sdf = new SimpleDateFormat(format);
				// Date permitdateObj = sdf.parse(beforepermitDate);
				Date permitdateObj = sdf.parse(fromDate);
				Date fromToDateObj = sdf.parse(fromDate);

				Calendar fromDateCal = Calendar.getInstance();
				fromDateCal.setTime(fromToDateObj);

				Calendar currentPermitExpiredDateCal = Calendar.getInstance();
				currentPermitExpiredDateCal.setTime(permitdateObj);

				int yearsInBetween = currentPermitExpiredDateCal.get(Calendar.YEAR) - fromDateCal.get(Calendar.YEAR);
				int numOfmonthsDiff = currentPermitExpiredDateCal.get(Calendar.MONTH) - fromDateCal.get(Calendar.MONTH);

				int currentMonthsCount = yearsInBetween * 12 + numOfmonthsDiff;

				int timeDuration = 12 - currentMonthsCount;
				Calendar cal = Calendar.getInstance();
				cal.setTime(permitdateObj);
				cal.add(Calendar.MONTH, noOfMonths);
				Date dateAsObjAfterAMonth = cal.getTime();

				Calendar newPermitDateCal = Calendar.getInstance();
				newPermitDateCal.setTime(dateAsObjAfterAMonth);

				int monthsCountAftreUpdated = newPermitDateCal.get(Calendar.MONTH)
						- currentPermitExpiredDateCal.get(Calendar.MONTH);
				int yearsCountAfterUpdated = newPermitDateCal.get(Calendar.YEAR)
						- currentPermitExpiredDateCal.get(Calendar.YEAR);
				int updatedTotalMonthsCount = yearsCountAfterUpdated * 12 + monthsCountAftreUpdated;
				Date validToDateObj = sdf.parse(validDate);
				if (updatedTotalMonthsCount <= timeDuration) {

				} else if (updatedTotalMonthsCount > timeDuration) {

				}
				if ((dateAsObjAfterAMonth.compareTo(validToDateObj) < 0
						|| dateAsObjAfterAMonth.compareTo(validToDateObj) == 0)
						&& updatedTotalMonthsCount <= timeDuration) {

					setCheckNewExpiryDateBoolean(true);
					String newPermitDateStringVal = sdf.format(dateAsObjAfterAMonth);
					permitRenewalsDTO.setNewPermitExpirtDate(newPermitDateStringVal);
				} else if (updatedTotalMonthsCount > timeDuration) {

					setErrorMsg("This cannot be updated. Only one year remaining from "
							+ permitRenewalsDTO.getFromToDate() + ".");
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					setCheckNewExpiryDateBoolean(false);
					permitRenewalsDTO.setNewPermitExpirtDate(null);
				} else if (dateAsObjAfterAMonth.compareTo(validToDateObj) > 0) {

					setErrorMsg("Valid Date:" + validDate + " should be after New Permit Expiry Date.");
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					setCheckNewExpiryDateBoolean(false);
					permitRenewalsDTO.setNewPermitExpirtDate(null);
				} else if (permitRenewalsDTO.getNewPermitExpirtDate() != null) {
					setCheckNewExpiryDateBoolean(true);
				}
			}
		} else {

			if (permitRenewalsDTO.getFromToDate() == null && permitRenewalsDTO.getValidToDate() == null) {

				setErrorMsg("From to date and Valid to date should be selected.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			} else if (permitRenewalsDTO.getFromToDate() == null && permitRenewalsDTO.getValidToDate() != null) {

				setErrorMsg("Valid from date should be selected.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			} else if (permitRenewalsDTO.getFromToDate() != null && permitRenewalsDTO.getValidToDate() == null) {

				setErrorMsg("Valid to date should be selected.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

			permitRenewalsDTO.setPermitExpiryDate(permitRenewalsDTO.getFromToDate());

			setNewPermitExpiryDateWhenBeforePermitExpiryDateIsEmpty();

		}
	}

	private void setNewPermitExpiryDateWhenBeforePermitExpiryDateIsEmpty() throws ParseException {

		permitRenewalsDTO.setPermitExpiryDate(permitRenewalsDTO.getFromToDate());
		if (permitRenewalsDTO.getPermitExpiryDate() != null && permitRenewalsDTO.getValidToDate() != null) {

			if (permitRenewalsDTO.getRequestRenewPeriod() == 0) {

				permitRenewalsDTO.setNewPermitExpirtDate(null);
				RequestContext.getCurrentInstance().update("newExpiryDateId");
			} else {
				int noOfMonths = permitRenewalsDTO.getRequestRenewPeriod();
				String validDate = permitRenewalsDTO.getValidToDate();
				String fromDate = permitRenewalsDTO.getFromToDate();
				String beforepermitDate = permitRenewalsDTO.getPermitExpiryDate();
				String format = "dd/MM/yyyy";
				SimpleDateFormat sdf = new SimpleDateFormat(format);
				Date permitdateObj = sdf.parse(beforepermitDate);
				Date fromToDateObj = sdf.parse(fromDate);

				Calendar fromDateCal = Calendar.getInstance();
				fromDateCal.setTime(fromToDateObj);

				Calendar currentPermitExpiredDateCal = Calendar.getInstance();
				currentPermitExpiredDateCal.setTime(permitdateObj);

				int yearsInBetween = currentPermitExpiredDateCal.get(Calendar.YEAR) - fromDateCal.get(Calendar.YEAR);
				int numOfmonthsDiff = currentPermitExpiredDateCal.get(Calendar.MONTH) - fromDateCal.get(Calendar.MONTH);

				int currentMonthsCount = yearsInBetween * 12 + numOfmonthsDiff;

				int timeDuration = 12 - currentMonthsCount;
				Calendar cal = Calendar.getInstance();
				cal.setTime(permitdateObj);
				cal.add(Calendar.MONTH, noOfMonths);
				Date dateAsObjAfterAMonth = cal.getTime();

				Calendar newPermitDateCal = Calendar.getInstance();
				newPermitDateCal.setTime(dateAsObjAfterAMonth);

				int monthsCountAftreUpdated = newPermitDateCal.get(Calendar.MONTH)
						- currentPermitExpiredDateCal.get(Calendar.MONTH);
				int yearsCountAfterUpdated = newPermitDateCal.get(Calendar.YEAR)
						- currentPermitExpiredDateCal.get(Calendar.YEAR);
				int updatedTotalMonthsCount = yearsCountAfterUpdated * 12 + monthsCountAftreUpdated;
				Date validToDateObj = sdf.parse(validDate);
				if (updatedTotalMonthsCount <= timeDuration) {

				} else if (updatedTotalMonthsCount > timeDuration) {

				}
				if ((dateAsObjAfterAMonth.compareTo(validToDateObj) < 0
						|| dateAsObjAfterAMonth.compareTo(validToDateObj) == 0)
						&& updatedTotalMonthsCount <= timeDuration) {

					setCheckNewExpiryDateBoolean(true);
					String newPermitDateStringVal = sdf.format(dateAsObjAfterAMonth);
					permitRenewalsDTO.setNewPermitExpirtDate(newPermitDateStringVal);
				} else if (updatedTotalMonthsCount > timeDuration) {

					setErrorMsg("You can not updated. You have only one year from " + permitRenewalsDTO.getFromToDate()
							+ ".");
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					setCheckNewExpiryDateBoolean(false);
					permitRenewalsDTO.setNewPermitExpirtDate(null);
				} else if (dateAsObjAfterAMonth.compareTo(validToDateObj) > 0) {

					setErrorMsg("Valid Date:" + validDate + " should be after New Permit Expiry Date.");
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					setCheckNewExpiryDateBoolean(false);
					permitRenewalsDTO.setNewPermitExpirtDate(null);
				}
			}
		}
	}

	public void renewalSave() throws ParseException {

		int resultOmniBusTable;
		if (disableSaveButton) {
			errorMsg = "Data not Updated. Ammendment Application is in Approval Process.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			searchByApplicationNo();
			return;
		}
		if (permitRenewalsDTO.getPermitExpiredValidToDateObj() != null) {
			if (permitRenewalsDTO.getRequestRenewPeriod() != 0) {
				if (!permitRenewalsDTO.getNewPermitExpirtDate().equals("")
						|| permitRenewalsDTO.getNewPermitExpirtDate() != null) {
					String modifyBy = sessionBackingBean.loginUser;
					String permitDate = permitRenewalsDTO.getPermitExpiryDate();
					Date permitdate1 = new SimpleDateFormat("dd/MM/yyyy").parse(permitDate);
					if (permitRenewalsDTO.getNewPermitExpirtDate() != null) {
						setCheckNewExpiryDateBoolean(true);
					}
					if (checkNewExpiryDateBoolean == true) {

						permitRenewalsDTO.setModifyBy(modifyBy);
						permitRenewalsDTO.setRequestRenewPeriod(permitRenewalsDTO.getRequestRenewPeriod());
						permitRenewalsDTO.setNewPermitExpirtDate(permitRenewalsDTO.getNewPermitExpirtDate());
						permitRenewalsDTO.setSpecialRemark(permitRenewalsDTO.getSpecialRemark());

						permitRenewalsDTO.setApplicationNo(generatedApplicationNo);

					} else {

						setErrorMsg("Valid Date should be after New Permit Expiry Date.");
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}

					/* Update */
					applicationHistoryDTO = historyService.getApplicationTableData(permitRenewalsDTO.getApplicationNo(),
							sessionBackingBean.getLoginUser());
					int resultApplicationTable = adminService
							.updatePermitRenewalRecordForAmmendments(permitRenewalsDTO);
					if (!newBus) {
						resultOmniBusTable = adminService
								.updatePermitRenewalRecordsInOmniForAmmendments(permitRenewalsDTO);
					} else {
						resultOmniBusTable = 0;
					}
					if (resultApplicationTable == 0 && resultOmniBusTable == 0) {

						RequestContext.getCurrentInstance().update("frmRenewalInfo");
						setSuccessMsg("Successfully Saved.");
						RequestContext.getCurrentInstance().update("frmsuccessSve");
						RequestContext.getCurrentInstance().execute("PF('successSve').show()");
						historyService.insertApplicationHistoryData(applicationHistoryDTO);

					} else {

						setErrorMsg("Can not Save.");
						RequestContext.getCurrentInstance().update("frmerrorMsge");
						RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
					}

				} else {

					errorMsg = "Valid Request Renew Period should be entered.";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			} else {

				errorMsg = "Request Renew Period should be entered.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		} else {

			errorMsg = "Permit Expired Valid to Date should be selected.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public void renewalClear() {
		permitRenewalsDTO.setRegNoOfBus(ominiBusDTO.getVehicleRegNo());
		getRenewalDetails();
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
			disableSaveInEndSave =true;
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
					amendmentServiceDTO.setOrigin(amendmentBusOwnerDTO.getDestination()); 
					amendmentServiceDTO.setDestination(amendmentBusOwnerDTO.getOrigin()); 
				} else {
					amendmentDTO.setOrigin(amendmentBusOwnerDTO.getOrigin());
					amendmentDTO.setDestination(amendmentBusOwnerDTO.getDestination());
					amendmentDTO.setRouteNo(amendmentBusOwnerDTO.getRouteNo());
					amendmentServiceDTO.setOrigin(amendmentBusOwnerDTO.getOrigin()); 
					amendmentServiceDTO.setDestination(amendmentBusOwnerDTO.getDestination());
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

			onNewRouteChange();
			busOwnerDTO = amendmentService.getOwnerDetails(generatedApplicationNo);

			amendmentDTO = amendmentService.getAmendmentDetails(generatedApplicationNo);

			displayCheckDocumentDataList();

			onChangeTitle();

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

	public void endChangeListener() {
		String location1;
		String location2;
		if (strTrnCode.equals("23")) {

			location1 = selectedOrgin;
			location2 = selectedDestination;
			selectedOrgin = location2;
			selectedDestination = location1;
			amendmentDTO.setOrigin(selectedOrgin);
			amendmentDTO.setDestination(selectedDestination);
		}
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
		amendmentDTO.setOldBusNo(strVehicleNo);
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

			/* Update Amendment History Table */
			amendmentHistoryDTO = historyService.getAmendmentTableData(generatedApplicationNo,
					sessionBackingBean.getLoginUser());
			amendmentService.updateAmendmentDetails(amendmentDTO);
			historyService.insertAmendmentsHistoryData(amendmentHistoryDTO);
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

		/* Update Vehicle Owner History Table */
		ownerHistoryDTO = historyService.getVehicleOwnerTableData(generatedApplicationNo,
				sessionBackingBean.getLoginUser());
		int result = amendmentService.updateBusOwner(busOwnerDTO);

		if (result == 0) {
			historyService.insertVehicleOwnerHistoryData(ownerHistoryDTO);
			busOwnerDTO = amendmentService.getOwnerDetails(generatedApplicationNo);

			/* Update Amendment History Table */
			amendmentHistoryDTO = historyService.getAmendmentTableData(generatedApplicationNo,
					sessionBackingBean.getLoginUser());
			amendmentService.updateAmendmentDetails(amendmentDTO);
			historyService.insertAmendmentsHistoryData(amendmentHistoryDTO);

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

		onChangeTitle();
	}

	public void clearOwnerAndAmendmentDetails() {

		busOwnerDTO = new BusOwnerDTO();
		amendmentDTO.setReasonForOwnerChange(null);
		amendmentDTO.setRelationshipWithTransferor(null);
		amendmentDTO.setRelationshipWithTransferorRemarks(null);
		setDisabledDOB(false);
		setDisabledGender(false);
	}

	public void busOwnerSave() {
		if (checkValiationsForInputFields == false) {
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
													if (busOwnerDTO.getNicNo() != null
															&& !busOwnerDTO.getNicNo().isEmpty()
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
																			&& !busOwnerDTO.getAddress2Sinhala()
																					.isEmpty()
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
																			RequestContext.getCurrentInstance().execute(
																					"PF('requiredField').show()");
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
																			RequestContext.getCurrentInstance().execute(
																					"PF('requiredField').show()");
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
												RequestContext.getCurrentInstance()
														.execute("PF('requiredField').show()");
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
		} else if (checkValiationsForInputFields == true) {
			if (amendmentDTO.getRelationshipWithTransferor() != null
					&& !amendmentDTO.getRelationshipWithTransferor().isEmpty()
					&& !amendmentDTO.getRelationshipWithTransferor().equalsIgnoreCase("")) {
				if (busOwnerDTO.getTitle() != null && !busOwnerDTO.getTitle().isEmpty()
						&& !busOwnerDTO.getTitle().equalsIgnoreCase("")) {

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
										if (busOwnerDTO.getProvince() != null && !busOwnerDTO.getProvince().isEmpty()
												&& !busOwnerDTO.getProvince().equalsIgnoreCase("")) {
											if (busOwnerDTO.getNicNo() != null && !busOwnerDTO.getNicNo().isEmpty()
													&& !busOwnerDTO.getNicNo().equalsIgnoreCase("")) {
												if (busOwnerDTO.getPerferedLanguage().equals("S")) {
													if (busOwnerDTO.getFullNameSinhala() != null
															&& !busOwnerDTO.getFullNameSinhala().isEmpty()
															&& !busOwnerDTO.getFullNameSinhala().equalsIgnoreCase("")) {
														if (busOwnerDTO.getAddress1Sinhala() != null
																&& !busOwnerDTO.getAddress1Sinhala().isEmpty()
																&& !busOwnerDTO.getAddress1Sinhala()
																		.equalsIgnoreCase("")) {
															if (busOwnerDTO.getAddress2Sinhala() != null
																	&& !busOwnerDTO.getAddress2Sinhala().isEmpty()
																	&& !busOwnerDTO.getAddress2Sinhala()
																			.equalsIgnoreCase("")) {
																if (busOwnerDTO.getAddress2Sinhala() != null
																		&& !busOwnerDTO.getAddress2Sinhala().isEmpty()
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
														RequestContext.getCurrentInstance().update("frmrequiredField");
														RequestContext.getCurrentInstance()
																.execute("PF('requiredField').show()");
													}
												} else if (busOwnerDTO.getPerferedLanguage().equals("T")) {
													if (busOwnerDTO.getFullNameTamil() != null
															&& !busOwnerDTO.getFullNameTamil().isEmpty()
															&& !busOwnerDTO.getFullNameTamil().equalsIgnoreCase("")) {
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
														RequestContext.getCurrentInstance().update("frmrequiredField");
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
											RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
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

				routeDTO = issuePermitService.getTempRouteDetails(generatedApplicationNo);
				tempNewRouteDTO = new AmendmentServiceDTO();

				if (routeDTO != null) {
					tempNewRouteDTO.setOrigin(routeDTO.getOrigin());
					tempNewRouteDTO.setDestination(routeDTO.getDestination());
					tempNewRouteDTO.setVia(routeDTO.getVia());
				}
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
				
				if(amendmentServiceDTO.getRouteFlag()!= null) {
					if(amendmentServiceDTO.getRouteFlag().equalsIgnoreCase("Y")) {
						amendmentServiceDTO.setDestination(routeDTO.getOrigin());
						amendmentServiceDTO.setOrigin(routeDTO.getDestination());
					}
				}

				routeDTO = issuePermitService.getTempRouteDetails(generatedApplicationNo);
				tempNewRouteDTO = new AmendmentServiceDTO();

				if (routeDTO != null) {
					tempNewRouteDTO.setOrigin(routeDTO.getOrigin());
					tempNewRouteDTO.setDestination(routeDTO.getDestination());
					tempNewRouteDTO.setVia(routeDTO.getVia());
				}
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

	public void onNewRouteChangeForSave() {

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
						dataList.get(i).setDisablephysicall(true);
					} else {
						dataList.get(i).setPhysicallyExists(false);
					}
				} else {
					dataList.get(i).setMandatory(false);

					if (isPhysicallyExit == true) {
						dataList.get(i).setPhysicallyExists(true);
						dataList.get(i).setDisablephysicall(true);
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
						dataList.get(i).setDisablephysicall(true);
					} else {
						dataList.get(i).setPhysicallyExists(false);
					}
				} else {
					dataList.get(i).setMandatory(false);

					if (isPhysicallyExit == true) {
						dataList.get(i).setPhysicallyExists(true);
						dataList.get(i).setDisablephysicall(true);
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

			sessionBackingBean.optionalList = documentManagementService.optionalDocs(strTrnCode, strPermitNo);

			sessionBackingBean.newPermitMandatoryDocumentList = documentManagementService
					.newPermitMandatoryList(strPermitNo);
			sessionBackingBean.newPermitOptionalDocumentList = documentManagementService
					.newPermitOptionalList(strPermitNo);
			sessionBackingBean.permitRenewalMandatoryDocumentList = documentManagementService
					.permitRenewalMandatoryList(strPermitNo);
			sessionBackingBean.permitRenewalOptionalDocumentList = documentManagementService
					.permitRenewalOptionalList(strPermitNo);
			sessionBackingBean.backlogManagementOptionalDocumentList = documentManagementService
					.backlogManagementOptionalList(strPermitNo);

			sessionBackingBean.amendmentToBusOwnerMandatoryDocumentList = documentManagementService
					.amendmentToBusOwnerMandatoryList(strPermitNo);
			sessionBackingBean.amendmentToBusOwnerOptionalDocumentList = documentManagementService
					.amendmentToBusOwnerOptionalList(strPermitNo);
			sessionBackingBean.amendmentToBusMandatoryDocumentList = documentManagementService
					.amendmentToBusMandatoryList(strPermitNo);
			sessionBackingBean.amendmentToBusOptionalDocumentList = documentManagementService
					.amendmentToBusOptionalList(strPermitNo);
			sessionBackingBean.amendmentToOwnerBusMandatoryDocumentList = documentManagementService
					.amendmentToOwnerBusMandatoryList(strPermitNo);
			sessionBackingBean.amendmentToOwnerBusOptionalDocumentList = documentManagementService
					.amendmentToOwnerBusOptionalList(strPermitNo);
			sessionBackingBean.amendmentToServiceBusMandatoryDocumentList = documentManagementService
					.amendmentToServiceBusMandatoryList(strPermitNo);
			sessionBackingBean.amendmentToServiceBusOptionalDocumentList = documentManagementService
					.amendmentToServiceBusOptionalList(strPermitNo);
			sessionBackingBean.amendmentToServiceMandatoryDocumentList = documentManagementService
					.amendmentToServiceMandatoryList(strPermitNo);
			sessionBackingBean.amendmentToServiceOptionalDocumentList = documentManagementService
					.amendmentToServiceOptionalList(strPermitNo);

			sessionBackingBean.tenderMandatoryDocumentList = documentManagementService.tenderMandatoryList(strPermitNo);
			sessionBackingBean.tenderOptionalDocumentList = documentManagementService.tenderOptionalList(strPermitNo);

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
			} else {

			}
		}
	}

	public void saveFinal() {
		boolean isOkToContinue = false;
		updateDocuments();

		for (int i = 0; i < dataList.size(); i++) {
			if ((dataList.get(i).isMandatory() == true
					&& (dataList.get(i).isExists() == true || dataList.get(i).isPhysicallyExists() == true))) {
				isOkToContinue = true;

			} else if ((dataList.get(i).isMandatory() == false
					&& (dataList.get(i).isExists() == true || dataList.get(i).isPhysicallyExists() == true
							|| dataList.get(i).isExists() == false || dataList.get(i).isExists() == false))) {
				isOkToContinue = true;
			} else {
				isOkToContinue = false;
				i = dataList.size();
			}
		}

		if (isOkToContinue == false) {
			RequestContext.getCurrentInstance().update("frmrequiredField");
			setErrorMsg("Please upload the mandatory document.");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else {

			if (!commonService.checkTaskHisByApplication(generatedApplicationNo, "AM100", "C")) {

				commonService.updateTaskStatusCompleted(generatedApplicationNo, "AM100",
						sessionBackingBean.getLoginUser());

				migratedService.updateStatusOfQueueNumberAfterCallNext(strQueueNo, "C");
				migratedService.updateQueueNumberTaskInQueueMaster(strQueueNo, generatedApplicationNo, "AM100", "C");

				migratedService.updateTransactionTypeCodeForQueueNo(strQueueNo, strTrnCode);

				clearAll();

			}

			RequestContext.getCurrentInstance().update("finalCheckList");

			RequestContext.getCurrentInstance().update("frmsuccessSve");
			setErrorMsg("Successfully Saved.");
			RequestContext.getCurrentInstance().execute("PF('successSve').show()");

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
									if (disableSaveButton) {

										if(strTrnCode.equals("15") || strTrnCode.equals("16")) {
										errorMsg = "Data not Updated. Amendment Inspection is Ongoing/Completed.";
										RequestContext.getCurrentInstance().update("frmrequiredField");
										RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
										return;
										}
										else {
											errorMsg = "Data not Updated. Amendment Application is in Approval Process.";
											RequestContext.getCurrentInstance().update("frmrequiredField");
											RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
											return;
										}
										//searchByApplicationNo();
										
									
									}
									else {

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

														if (ominiBusDTO.getSeq() != 0) {

															// update
															ominiBusDTO
																	.setModifiedBy(sessionBackingBean.getLoginUser());
															ominiBusDTO.setPermitNo(strPermitNo);

															ominiBusDTO.setApplicationNo(generatedApplicationNo);

															/*
															 * Update OminiBus History  and Vehicle Owner Data
															 */
															/*ominiBusHistoryDTO = historyService.getOminiBusTableData(
																	generatedApplicationNo,
																	sessionBackingBean.getLoginUser());*/
															
															/*** set permit renewal details into ominiBus DTO **/

															ominiBusDTO.setSerialNo(permitRenewalsDTO.getSerialNo());
															ominiBusDTO
																	.setGarageRegNo(permitRenewalsDTO.getGarageRegNo());
															ominiBusDTO
																	.setGarageName(permitRenewalsDTO.getGarageName());
															ominiBusDTO.setFitnessCertiDate(
																	permitRenewalsDTO.getFitnessCertiDate());

															ominiBusDTO.setExpiryDateRevLic(permitRenewalsDTO.getExpiryDateRevLicNew());
															ominiBusDTO.setEmissionExpDate(permitRenewalsDTO.getEmmissionTestExpireDate());

												
															/*** set permit renewal details into ominiBus DTO end **/
															
															
															
															
															int result = issuePermitService
																	.updateNewPermitOminiBus(ominiBusDTO);

															if (result == 0) {
															/*	historyService
																		.insertOminiBusHistoryData(ominiBusHistoryDTO);*/

																activeTabIndex = 4;

																/*
																 * Update Amendments History Data
																 */
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
																activeTabIndex = 4;
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
															
															
															/*** set permit renewal details into ominiBus DTO **/

															ominiBusDTO.setSerialNo(permitRenewalsDTO.getSerialNo());
															ominiBusDTO
																	.setGarageRegNo(permitRenewalsDTO.getGarageRegNo());
															ominiBusDTO
																	.setGarageName(permitRenewalsDTO.getGarageName());
															ominiBusDTO.setFitnessCertiDate(
																	permitRenewalsDTO.getFitnessCertiDate());

															ominiBusDTO.setExpiryDateRevLic(permitRenewalsDTO.getExpiryDateRevLicNew());
															ominiBusDTO.setEmissionExpDate(permitRenewalsDTO.getEmmissionTestExpireDate());

												
															/*** set permit renewal details into ominiBus DTO end **/
															
															
															
															
															int result = amendmentService.saveNewOminiBus(ominiBusDTO);

															if (result == 0) {

																if (amendmentDTO.getSeq() == 0) {
																	amendmentService
																			.saveAmendmentNewOminiBus(amendmentDTO);
																} else {
																	/*
																	 * Update Amendment History
																	 */
																	amendmentHistoryDTO = historyService
																			.getAmendmentTableData(
																					generatedApplicationNo,
																					sessionBackingBean.getLoginUser());
																	amendmentService
																			.updateAmendmentDetails(amendmentDTO);
																	historyService.insertAmendmentsHistoryData(
																			amendmentHistoryDTO);
																}

																saveActionTaskHandler();

																migratedService.updateStatusOfQueApp(strQueueNo,
																		generatedApplicationNo);
																migratedService.updateQueueNumberTaskInQueueMaster(
																		strQueueNo, generatedApplicationNo, "AM100",
																		"O");

																savedNewOminiBus = true;

																amendmentService.updateVehicleOfApplication(
																		ominiBusDTO.getVehicleRegNo(),
																		generatedApplicationNo,
																		sessionBackingBean.getLoginUser());

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
								}
								} else {
									if (disableSaveButton) {
										if(strTrnCode.equals("15") || strTrnCode.equals("16")) {
										errorMsg = "Data not Updated. Amendment Inspection is Ongoing/Completed.";
										RequestContext.getCurrentInstance().update("frmrequiredField");
										RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
										return;
										}
										else {
											errorMsg = "Data not Updated. Amendment Application is in Approval Process.";
											RequestContext.getCurrentInstance().update("frmrequiredField");
											RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
											return;
										}
										//searchByApplicationNo();
										
									} else {
										// no loan

										ominiBusDTO.setIsLoanObtained("N");

										if (ominiBusDTO.getSeq() != 0) {
											// update
											ominiBusDTO.setModifiedBy(sessionBackingBean.getLoginUser());
											ominiBusDTO.setPermitNo(strPermitNo);
											ominiBusDTO.setApplicationNo(generatedApplicationNo);

											/*
											 * ominiBusHistoryDTO =
											 * historyService.getOminiBusTableData(generatedApplicationNo,
											 * sessionBackingBean.getLoginUser());
											 */

											/* Update OminiBus History Table and VehiOwner History Table */
											/*** set permit renewal details into ominiBus DTO **/

											ominiBusDTO.setSerialNo(permitRenewalsDTO.getSerialNo());
											ominiBusDTO
													.setGarageRegNo(permitRenewalsDTO.getGarageRegNo());
											ominiBusDTO
													.setGarageName(permitRenewalsDTO.getGarageName());
											ominiBusDTO.setFitnessCertiDate(
													permitRenewalsDTO.getFitnessCertiDate());

											ominiBusDTO.setExpiryDateRevLic(permitRenewalsDTO.getExpiryDateRevLicNew());
											ominiBusDTO.setEmissionExpDate(permitRenewalsDTO.getEmmissionTestExpireDate());

											
											/*** set permit renewal details into ominiBus DTO end **/
											
											
											int result = issuePermitService.updateNewPermitOminiBus(ominiBusDTO);

											if (result == 0) {

												// historyService.insertOminiBusHistoryData(ominiBusHistoryDTO);
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

												getOminiBusDetails();
												otherOrgList = amendmentService.getOtherOrgList(generatedApplicationNo);

												RequestContext.getCurrentInstance().update("frmOminiBusInfo");
												RequestContext.getCurrentInstance().execute("PF('successSve').show()");

											} else {
												RequestContext.getCurrentInstance()
														.execute("PF('generalError').show()");
											}
										} else {
											// Save
											ominiBusDTO.setQueueNo(strQueueNo);
											if (generatedApplicationNo == null || generatedApplicationNo.isEmpty()
													|| generatedApplicationNo.equalsIgnoreCase("")) {
												generatedApplicationNo = amendmentService.generateApplicationNo();
												ominiBusDTO.setIsBacklogApp("N");
												ominiBusDTO.setCreatedBy(sessionBackingBean.getLoginUser());
												ominiBusDTO.setPermitNo(strPermitNo);
												ominiBusDTO.setApplicationNo(generatedApplicationNo);

												amendmentService.saveNewOminiBusApplication(ominiBusDTO);
											} else {
												amendmentService.updateVehicleOfApplication(
														ominiBusDTO.getVehicleRegNo(), generatedApplicationNo,
														sessionBackingBean.getLoginUser());
											}
											ominiBusDTO.setIsBacklogApp("N");
											ominiBusDTO.setCreatedBy(sessionBackingBean.getLoginUser());
											ominiBusDTO.setPermitNo(strPermitNo);
											ominiBusDTO.setApplicationNo(generatedApplicationNo);

											amendmentDTO.setCreatedBy(sessionBackingBean.getLoginUser());
											amendmentDTO.setPermitNo(strPermitNo);
											amendmentDTO.setApplicationNo(generatedApplicationNo);
											amendmentDTO.setOldBusNo(strVehicleNo);

											amendmentDTO.setBusRegNo(ominiBusDTO.getVehicleRegNo());
											amendmentDTO.setTrnType(strTrnCode);
											amendmentDTO.setPermitNo(ominiBusDTO.getPermitNo());
											amendmentDTO.setQueueNo(strQueueNo);
											/*** set permit renewal details into ominiBus DTO **/

											ominiBusDTO.setSerialNo(permitRenewalsDTO.getSerialNo());
											ominiBusDTO
													.setGarageRegNo(permitRenewalsDTO.getGarageRegNo());
											ominiBusDTO
													.setGarageName(permitRenewalsDTO.getGarageName());
											ominiBusDTO.setFitnessCertiDate(
													permitRenewalsDTO.getFitnessCertiDate());

											ominiBusDTO.setExpiryDateRevLic(permitRenewalsDTO.getExpiryDateRevLicNew());
											ominiBusDTO.setEmissionExpDate(permitRenewalsDTO.getEmmissionTestExpireDate());

										
											/*** set permit renewal details into ominiBus DTO end **/
											
											

											int result = amendmentService.saveNewOminiBus(ominiBusDTO);

											if (result == 0) {

												if (amendmentDTO.getSeq() == 0) {
													amendmentService.saveAmendmentNewOminiBus(amendmentDTO);
												} else {
													/* Update Amendment History */
													amendmentHistoryDTO = historyService.getAmendmentTableData(
															generatedApplicationNo, sessionBackingBean.getLoginUser());
													amendmentService.updateAmendmentDetails(amendmentDTO);
													historyService.insertAmendmentsHistoryData(amendmentHistoryDTO);
												}

												saveActionTaskHandler();

												migratedService.updateStatusOfQueApp(strQueueNo,
														generatedApplicationNo);
												migratedService.updateQueueNumberTaskInQueueMaster(strQueueNo,
														generatedApplicationNo, "AM100", "O");

												savedNewOminiBus = true;

												amendmentService.updateVehicleOfApplication(
														ominiBusDTO.getVehicleRegNo(), generatedApplicationNo,
														sessionBackingBean.getLoginUser());

												getOminiBusDetails();
												otherOrgList = amendmentService.getOtherOrgList(generatedApplicationNo);

												displayCheckDocumentDataList();

												RequestContext.getCurrentInstance().update("frmtab07");

												RequestContext.getCurrentInstance().update("frmOminiBusInfo");
												RequestContext.getCurrentInstance().execute("PF('successSve').show()");

											} else {
												RequestContext.getCurrentInstance()
														.execute("PF('generalError').show()");
											}
										}

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
			
			permitRenewalsDTO.setExpiryDateRevLicNew(ominiBusDTO.getExpiryDateRevLicNew());
			permitRenewalsDTO.setEmmissionTestExpireDate(ominiBusDTO.getEmmissionTestExpireDate());

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

	public void clearCheckDocumentsTable() {
		displayCheckDocumentDataList();

		for (int i = 0; i < dataList.size(); i++) {
			String currentDocCode = dataList.get(i).getDocumentCode();

			boolean isPhysicallyExit = amendmentService.isPhysicallyExit(currentDocCode, generatedApplicationNo,
					strPermitNo);

			boolean isMandatory = amendmentService.isMandatory(currentDocCode, generatedApplicationNo, strPermitNo,
					strTrnCode);

			if (isMandatory == true) {
				dataList.get(i).setMandatory(true);
			} else {
				dataList.get(i).setMandatory(false);
			}

			if (isPhysicallyExit == true) {
				dataList.get(i).setPhysicallyExists(true);
			} else {
				dataList.get(i).setPhysicallyExists(false);
			}

		}

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

		/* Update Accident Master History Data */
		accidentMasterHistoryDTO = historyService.getAccidentMasterTableData(afterAccidentDTO.getVehicleNo(),
				sessionBackingBean.getLoginUser());
		amendmentService.updateAfterAccident(afterAccidentDTO);
		historyService.insertAccidentMasterHistoryData(accidentMasterHistoryDTO);

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

	public void removeAccident() {
		if (selectedAccident != 0) {

			/* Update accident details History Table */
			accidentDetailsHistoryDTO = historyService.getAccidentDetailsTableData(selectedAccident,
					sessionBackingBean.getLoginUser());
			amendmentService.removeAccident(selectedAccident);
			historyService.insertAccidentDetailsHistoryData(accidentDetailsHistoryDTO);

			accidentList = new ArrayList<AccidentDTO>(0);
			accidentList = amendmentService.getAccidentList(ominiBusDTO.getVehicleRegNo());
			RequestContext.getCurrentInstance().update("accidentList");
			RequestContext.getCurrentInstance().update("frmsuccessSve");

			errorMsg = "Record deleted";
			RequestContext.getCurrentInstance().execute("PF('frmrequiredField').show()");
		} else {

			errorMsg = "You can not delete this record.";
			RequestContext.getCurrentInstance().execute("PF('frmrequiredField').show()");
		}
		RequestContext.getCurrentInstance().update("frmtab0lojb89");
	}

	public void removeMoreAccident() {
		if (selectedMoreAccident != 0) {

			/* Update Legal case History Table */
			moreAccidentDTO = historyService.getLegalCasesTableData(selectedMoreAccident,
					sessionBackingBean.getLoginUser());
			amendmentService.removeMoreAccident(selectedMoreAccident);
			historyService.insertLegalCasesHistoryData(moreAccidentDTO);

			accidentList = new ArrayList<AccidentDTO>(0);
			moreAccidentList = amendmentService.getMoreAccidentList(ominiBusDTO.getVehicleRegNo());
			RequestContext.getCurrentInstance().update("moreAccidentList");
			RequestContext.getCurrentInstance().update("frmsuccessSve");

			errorMsg = "Record deleted";
			RequestContext.getCurrentInstance().execute("PF('frmrequiredField').show()");
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
		String originVal = null;
		String DestinationVal = null;
		if (generatedApplicationNo == null || generatedApplicationNo.isEmpty()
				|| generatedApplicationNo.equalsIgnoreCase("")) {
			generatedApplicationNo = amendmentService.generateApplicationNo();
			busOwnerDTO.setApplicationNo(generatedApplicationNo);
			busOwnerDTO.setCreatedBy(sessionBackingBean.getLoginUser());
			amendmentService.saveDataApplication(busOwnerDTO);
		}
		if (disableSaveButton) {
			errorMsg = "Data not Updated. Ammendment Application is in Approval Process.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			searchByApplicationNo();
			return;
		}

		amendmentServiceDTO.setCreatedBy(sessionBackingBean.getLoginUser());

		amendmentServiceDTO.setApplicationNo(generatedApplicationNo);

		amendmentDTO.setCreatedBy(sessionBackingBean.getLoginUser());

		amendmentDTO.setApplicationNo(generatedApplicationNo);

		amendmentServiceDTO.setBusRegNo(strVehicleNo);

		amendmentServiceDTO.setPermitNo(amendmentBusOwnerDTO.getPermitNo());

		if (strTrnCode!=null && strTrnCode.equalsIgnoreCase("16")) {
			amendmentDTO.setBusRegNo(ominiBusDTO.getVehicleRegNo());
		} else if (strTrnCode!=null && strTrnCode.equalsIgnoreCase("21")) {
			amendmentDTO.setBusRegNo(ominiBusDTO.getVehicleRegNo());
		} else {
			amendmentDTO.setBusRegNo(strVehicleNo);
		}
		amendmentDTO.setOldBusNo(strVehicleNo);
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
			
			/**check previous route flag is Y or not. if it is Y and again route flag is Y in edit function
			 *  existing flag become a N**/
			if(amendmentServiceDTO.isTheRouteFlag()) {
				String existingFlag =amendmentService.getRouteFlag(generatedApplicationNo);
				if(existingFlag.equalsIgnoreCase("Y")) {
				amendmentServiceDTO.setTheRouteFlag(false);
				amendmentServiceDTO.setRouteFlag("N");
				}
				
			}
			
			/***end **/
		} else if (amendmentDTO.getServiceChangeType() != null && !amendmentDTO.getServiceChangeType().isEmpty()
				&& amendmentDTO.getServiceChangeType().trim().equalsIgnoreCase("E")) {
			amendmentServiceDTO.setRouteNo(amendmentOminiBusDTO.getRouteNo());
			disableSaveInEndSave =true;
			amendmentServiceDTO.setServiceType(amendmentOminiBusDTO.getServiceType());

		}
		if (amendmentDTO.getSeq() == 0) {
			applicationHistoryDTO = historyService.getApplicationTableData(generatedApplicationNo,
					sessionBackingBean.getLoginUser());
			amendmentService.saveServiceChange(amendmentServiceDTO, sessionBackingBean.getLoginUser());
			historyService.insertApplicationHistoryData(applicationHistoryDTO);
		} else {

			/* Update Application History Table */
			applicationHistoryDTO = historyService.getApplicationTableData(generatedApplicationNo,
					sessionBackingBean.getLoginUser());
			amendmentService.saveServiceChange(amendmentServiceDTO, sessionBackingBean.getLoginUser());
			historyService.insertApplicationHistoryData(applicationHistoryDTO);
		}
		originVal = amendmentServiceDTO.getOrigin();
		DestinationVal=amendmentServiceDTO.getDestination();
		amendmentServiceDTO = amendmentService.getServiceChange(generatedApplicationNo);
		if(amendmentServiceDTO.getRouteFlag()!= null) {
		
			amendmentServiceDTO.setOrigin(originVal);
			amendmentServiceDTO.setDestination(DestinationVal);
		
		
		}
		onNewRouteChangeForSave();
		if (amendmentDTO.getSeq() == 0) {
			amendmentService.saveAmendmentServiceChange(amendmentDTO);

		} else {
			amendmentDTO.setModifiedBy(sessionBackingBean.getLoginUser());

			/* Update Amendments History Table */
			amendmentHistoryDTO = historyService.getAmendmentTableData(generatedApplicationNo,
					sessionBackingBean.getLoginUser());
			amendmentService.updateAmendmentDetails(amendmentDTO);
			historyService.insertAmendmentsHistoryData(amendmentDTO);

			tempNewRouteDTO.setApplicationNo(generatedApplicationNo);
			tempNewRouteDTO.setModifiedBy(sessionBackingBean.getLoginUser());

			/* Update Route Request History Table */
			routeRequestHistoryDTO = historyService.getRouteRequsetedTableData(generatedApplicationNo,
					sessionBackingBean.getLoginUser());
			
//			check 'C' or 'P' temp route added on 19-01-2022
			boolean checkTempRoute = amendmentService.checkTempRoute(generatedApplicationNo);
			
			if(!checkTempRoute) {
				boolean updated = amendmentService.updateNewRouteRequest(tempNewRouteDTO);
				if (updated) {
					historyService.insertRouteRequsetedHistoryData(routeRequestHistoryDTO);
				}
			}

			tempNewRouteDTO = amendmentService.getNewRouteRequest(generatedApplicationNo);
		}

		amendmentDTO = amendmentService.getAmendmentDetails(generatedApplicationNo);

		displayCheckDocumentDataList();

		RequestContext.getCurrentInstance().update("frmtab07");
		RequestContext.getCurrentInstance().execute("PF('successSve').show()");

	}

	public StreamedContent print(ActionEvent ae) throws JRException {

		files = null;
		String sourceFileName = null;

		Connection conn = null;

		try {
			conn = ConnectionManager.getConnection();

			sourceFileName = "..//reports//RegistrationOmniBus.jrxml";

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_busNo", ominiBusDTO.getVehicleRegNo());

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", "Omni Bus Registration.pdf");

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("reportBytes", pdfByteArray);
			sessionMap.put("docType", "pdf");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(conn);
		}

		return files;

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

	public boolean isDisableSaveInEndSave() {
		return disableSaveInEndSave;
	}

	public void setDisableSaveInEndSave(boolean disableSaveInEndSave) {
		this.disableSaveInEndSave = disableSaveInEndSave;
	}
	
}