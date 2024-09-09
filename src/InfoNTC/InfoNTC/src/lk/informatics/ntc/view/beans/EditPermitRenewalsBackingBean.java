package lk.informatics.ntc.view.beans;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.ComplaintRequestDTO;
import lk.informatics.ntc.model.dto.DocumentManagementDTO;
import lk.informatics.ntc.model.dto.FluingSquadVioConditionDTO;
import lk.informatics.ntc.model.dto.FlyingManageInvestigationLogDTO;
import lk.informatics.ntc.model.dto.FlyingSquadVioDocumentsDTO;
import lk.informatics.ntc.model.dto.IssuePermitDTO;
import lk.informatics.ntc.model.dto.OminiBusDTO;
import lk.informatics.ntc.model.dto.PaymentVoucherDTO;
import lk.informatics.ntc.model.dto.PermitRenewalsDTO;
import lk.informatics.ntc.model.dto.RouteDTO;
import lk.informatics.ntc.model.dto.SimRegistrationDTO;
import lk.informatics.ntc.model.dto.UploadImageDTO;
import lk.informatics.ntc.model.dto.VehicleInspectionDTO;
import lk.informatics.ntc.model.exception.ApplicationException;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.DocumentManagementService;
import lk.informatics.ntc.model.service.FlyingSquadChargeSheetService;
import lk.informatics.ntc.model.service.HistoryService;
import lk.informatics.ntc.model.service.InspectionActionPointService;
import lk.informatics.ntc.model.service.IssuePermitService;
import lk.informatics.ntc.model.service.ManageInquiryService;
import lk.informatics.ntc.model.service.MigratedService;
import lk.informatics.ntc.model.service.PermitRenewalsService;
import lk.informatics.ntc.model.service.QueueManagementService;
import lk.informatics.ntc.model.service.VehicleInspectionService;
import lk.informatics.ntc.view.util.PropertyReader;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "editPermitRenewalsBackingBean")
@ViewScoped
public class EditPermitRenewalsBackingBean {
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	private String selectedPermitNo;
	private String selectedProvinceCode;
	private String selectedDistricCode;
	private String selectedMaterialStatus;
	private String selectedDivisionSecCode;
	public static Logger logger = Logger.getLogger("UploadPhotosBackingBean");
	String callingQueueNo;
	private String errorMsg;
	private String sucessMsg;
	private String validationMsg;
	private String validationMsgforComplain;
	private String viewInspectionURL;
	private RouteDTO routeDTO;
	private String strQueueNo;
	private String selectedApplicationNo;
	private Boolean routeFlag;
	private String strBusRegNo;
	private UploadImageDTO uploadImageDTO;
	private String vehicleNo;
	private String applicationNo;
	public VehicleInspectionDTO vehicleDTO = new VehicleInspectionDTO();
	private String vehicleOwnerName;
	private VehicleInspectionDTO viewedDetails;
	private InspectionActionPointService inspectionActionPointService;
	private int activeTabIndex;
	private boolean editMode = true;
	private boolean showThirdForm = false;
	private boolean showbacklogvalueLoad = false;
	private boolean showbacklogvalue12 = false;
	private List<VehicleInspectionDTO> dataListViewInspection = new ArrayList<VehicleInspectionDTO>();

	public PermitRenewalsDTO getRenewalsDTO() {
		return renewalsDTO;
	}

	public void setRenewalsDTO(PermitRenewalsDTO renewalsDTO) {
		this.renewalsDTO = renewalsDTO;
	}

	private boolean queueNoChecked = false;
	private boolean disableCallNextBtn = false;
	private boolean disableSkipBtn = true;
	boolean numCheckTel = false;
	boolean numCheckMobile = false;
	private boolean openFinalCheckListTab = false;
	private boolean checkExists = false;
	private boolean disableSearchBtn = false;
	private boolean checkNewExpiryDateBoolean = false;
	private boolean disabledPermitNoMenu = false;
	private boolean disabledApplicationNoMenu = false;
	private boolean showbacklogvalue = false;
	private boolean requestNewPeriodReadOnly = false;
	private boolean showNewPermitDateInput = true;
	private boolean disabledSaveBtnOne = false;
	private boolean isCheckedGotoVehicleInspectionViewMode = false;
	private boolean ownerImageUpload = false;
	private boolean disabledTab = true;
	private boolean disabledReqPeriodInput = true;

	private PaymentVoucherDTO paymentVoucherDTO;
	private CommonDTO commonDTO = new CommonDTO();
	private OminiBusDTO ominiBusDTO = new OminiBusDTO();;
	private VehicleInspectionService vehicleInspectionService;
	private PermitRenewalsService permitRenewalsService;
	private QueueManagementService queueManagementService;
	private DocumentManagementService documentManagementService;
	private CommonService commonService;
	private MigratedService migratedService;
	private FlyingSquadChargeSheetService flyingSquadChargeSheetService;

	private PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();
	private PermitRenewalsDTO searchedDTO;
	private PermitRenewalsDTO requestRenewDetSearchedDTO;
	private IssuePermitDTO filteredValuesDTO;
	private PermitRenewalsDTO renewalsDTO;
	private PermitRenewalsDTO searchedCurrentSeqNoForApplicationTable;
	private PermitRenewalsDTO searchedSeqNoForOwnerTable;
	private PermitRenewalsDTO checkAndDisplayRemarkValue;
	private PermitRenewalsDTO selectedViewRow;
	private PermitRenewalsDTO detialsForSearchedQueueNo;
	private PermitRenewalsDTO ApplicationNoForSearchedQueueNo;
	private PermitRenewalsDTO detailsForSearchedPermitNo;
	private PermitRenewalsDTO detailsForSearchedApplicationNo;
	private OminiBusDTO inspectionDetails;
	private IssuePermitDTO issuePermitDTO = new IssuePermitDTO();
	private IssuePermitService issuePermitService;
	private FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO;

	public List<PermitRenewalsDTO> permitNoList = new ArrayList<PermitRenewalsDTO>(0);
	public List<PermitRenewalsDTO> applicationNoList = new ArrayList<PermitRenewalsDTO>(0);
	public List<PermitRenewalsDTO> provinceList = new ArrayList<PermitRenewalsDTO>(0);
	public List<PermitRenewalsDTO> districtList = new ArrayList<PermitRenewalsDTO>(0);
	public List<PermitRenewalsDTO> materialStatusList = new ArrayList<PermitRenewalsDTO>(0);
	public List<PermitRenewalsDTO> divisionSectionList = new ArrayList<PermitRenewalsDTO>(0);
	public List<PermitRenewalsDTO> dataList = new ArrayList<PermitRenewalsDTO>(0);
	public List<PermitRenewalsDTO> checkDataList = new ArrayList<PermitRenewalsDTO>(0);
	public List<PermitRenewalsDTO> paymentHistoryDataList = new ArrayList<PermitRenewalsDTO>(0);
	public List<PermitRenewalsDTO> chargeTypePaymentDataList = new ArrayList<PermitRenewalsDTO>(0);
	private List<DocumentManagementDTO> mandatoryList = new ArrayList<DocumentManagementDTO>(0);
	private List<DocumentManagementDTO> optionalList = new ArrayList<DocumentManagementDTO>(0);
	private List<DocumentManagementDTO> ApplicationList = new ArrayList<DocumentManagementDTO>();
	private List<DocumentManagementDTO> RenewalApplicationList = new ArrayList<DocumentManagementDTO>();
	private List<CommonDTO> counterList = new ArrayList<>(0);
	public List<PermitRenewalsDTO> otherOptionalDocumentsList = new ArrayList<PermitRenewalsDTO>(0);
	public List<PermitRenewalsDTO> editingApplNoList = new ArrayList<PermitRenewalsDTO>(0);
	public List<PermitRenewalsDTO> filterPermitNoList = new ArrayList<PermitRenewalsDTO>(0);
	private ArrayList<FluingSquadVioConditionDTO> conditionList;
	private ArrayList<FlyingSquadVioDocumentsDTO> documentList;

	private byte[] permitOwnerFaceImage;
	private byte[] firstVehicleImg;
	private byte[] secondVehicleImg;
	private byte[] thirdVehicleImg;
	private byte[] fourthVehicleImg;
	private byte[] fifthVehicleImg;
	private byte[] sixthVehicleImg;

	private PermitRenewalsDTO vehicleOwnerHistoryDTO;
	private OminiBusDTO OminiBusHistoryDTO;
	private PermitRenewalsDTO applicationHistoryDTO;
	private HistoryService historyService;

	private VehicleInspectionDTO taskDetWithAppDetDTO;
	private ComplaintRequestDTO publicComplanDto;
	private List<ComplaintRequestDTO> complainDetViewList = new ArrayList<>();
	private List<FlyingManageInvestigationLogDTO> investigationDetViewList = new ArrayList<>();
	private ManageInquiryService manageInquiryService;
	private boolean renderComplainDet;
	private SimRegistrationDTO simRegistrationDTO;
	private List<SimRegistrationDTO> emiDetViewList = new ArrayList<>();

	private ComplaintRequestDTO viewSelect;
	private ComplaintRequestDTO selectedComplaintDTO;

	private FlyingManageInvestigationLogDTO viewInvSelect;
	private FlyingManageInvestigationLogDTO selectedInvDTO;
	private boolean isIncompleteInspection = false;

	@PostConstruct
	public void init() {

		routeFlag = false;
		counterList = permitRenewalsService.counterdropdown();
		editingApplNoList = permitRenewalsService.getEditingApplicationNoList();
		provinceList = permitRenewalsService.getAllProvincesList();
		materialStatusList = permitRenewalsService.getAllMaterialStatusList();
		activeTabIndex = 0;
		renderComplainDet = false;
		validationMsg = null;
		validationMsgforComplain = null;

		java.util.Date date = new java.util.Date();

		taskDetWithAppDetDTO = new VehicleInspectionDTO();

		Date todaysDate = new Date();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String testDateString = df.format(todaysDate);

		permitRenewalsDTO.setDateOne(testDateString);
		if ((sessionBackingBean.getViewedInspectionApplicationNo() == null
				|| sessionBackingBean.getViewedInspectionApplicationNo().isEmpty()
				|| sessionBackingBean.getViewedInspectionApplicationNo().equals(""))
				&& (sessionBackingBean.getViewedPermitNo() == null || sessionBackingBean.getViewedPermitNo().isEmpty()
						|| sessionBackingBean.getViewedPermitNo().equals(""))
				&& (sessionBackingBean.getSelectedQueueNo() == null || sessionBackingBean.getSelectedQueueNo().isEmpty()
						|| sessionBackingBean.getSelectedQueueNo().equals(""))
				&& isCheckedGotoVehicleInspectionViewMode == false) {

		} else if (((sessionBackingBean.getViewedInspectionApplicationNo() != null
				|| !sessionBackingBean.getViewedInspectionApplicationNo().isEmpty()
				|| !sessionBackingBean.getViewedInspectionApplicationNo().equals(""))
				|| (sessionBackingBean.getViewedPermitNo() != null || !sessionBackingBean.getViewedPermitNo().isEmpty()
						|| !sessionBackingBean.getViewedPermitNo().equals(""))
				|| (sessionBackingBean.getSelectedQueueNo() != null
						|| !sessionBackingBean.getSelectedQueueNo().isEmpty()
						|| !sessionBackingBean.getSelectedQueueNo().equals("")))
				&& isCheckedGotoVehicleInspectionViewMode == true) {

			loadSearchDetails();
			activeTabIndex = 1;
			displayCheckDocumentDataList();
			displayPaymentHistoryDataList();
		} else {

		}
	}

	public EditPermitRenewalsBackingBean() {
		issuePermitService = (IssuePermitService) SpringApplicationContex.getBean("issuePermitService");
		permitRenewalsService = (PermitRenewalsService) SpringApplicationContex.getBean("permitRenewalsService");
		queueManagementService = (QueueManagementService) SpringApplicationContex.getBean("queueManagementService");
		inspectionActionPointService = (InspectionActionPointService) SpringApplicationContex
				.getBean("inspectionActionPointService");
		vehicleInspectionService = (VehicleInspectionService) SpringApplicationContex
				.getBean("vehicleInspectionService");
		documentManagementService = (DocumentManagementService) SpringApplicationContex
				.getBean("documentManagementService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		migratedService = (MigratedService) SpringApplicationContex.getBean("migratedService");
		historyService = (HistoryService) SpringApplicationContex.getBean("historyService");
		manageInquiryService = (ManageInquiryService) SpringApplicationContex.getBean("manageInquiryService");
		flyingSquadChargeSheetService = ((FlyingSquadChargeSheetService) SpringApplicationContex
				.getBean("flyingSquadChargeSheetService"));
		uploadImageDTO = new UploadImageDTO();
		filteredValuesDTO = new IssuePermitDTO();
		viewedDetails = new VehicleInspectionDTO();

		vehicleOwnerHistoryDTO = new PermitRenewalsDTO();
		OminiBusHistoryDTO = new OminiBusDTO();
		applicationHistoryDTO = new PermitRenewalsDTO();
		conditionList = new ArrayList<FluingSquadVioConditionDTO>();
		documentList = new ArrayList<FlyingSquadVioDocumentsDTO>();

	}

	public void onApplicationNoChange() {

		detailsForSearchedApplicationNo = permitRenewalsService
				.getAllDetailsForSelectedEditingAppNo(selectedApplicationNo);
		permitRenewalsDTO.setPermitNo(detailsForSearchedApplicationNo.getPermitNo());
		permitRenewalsDTO.setQueueNo(detailsForSearchedApplicationNo.getQueueNo());
		permitRenewalsDTO.setRegNoOfBus(detailsForSearchedApplicationNo.getRegNoOfBus());
		permitRenewalsDTO.setApplicationNo(detailsForSearchedApplicationNo.getApplicationNo());
		setSelectedPermitNo(permitRenewalsDTO.getPermitNo());
		setSelectedApplicationNo(permitRenewalsDTO.getApplicationNo());
	}

	public void weightValidation() {
		if (ominiBusDTO.getWeight() != null && ominiBusDTO.getWeight().charAt(0) == '-') {
			errorMsg = "Invalid Weight";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			ominiBusDTO.setWeight(null);

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

	public void noOfDoorsValidation() {
		if (ominiBusDTO.getNoofDoors() != null && ominiBusDTO.getNoofDoors().charAt(0) == '-') {
			errorMsg = "Invalid No. of Doors";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			ominiBusDTO.setNoofDoors(null);

		}
	}

	public void onProvinceChange() {

		if (!selectedProvinceCode.equals("") && !selectedProvinceCode.isEmpty() && selectedProvinceCode != null) {
			districtList = permitRenewalsService.getAllDistrictsForCurrentProvince(selectedProvinceCode);
		}
	}

	public void onDistrictChange() {

		if (!selectedDistricCode.isEmpty() && !selectedDistricCode.equals("") && selectedDistricCode != null) {
			divisionSectionList = permitRenewalsService.getAllDivisionSections(selectedDistricCode);
		}
	}

	public void onDivisionSecChange() {

	}

	public void onMartialStatusChange() {

	}

	public void powerOfAttorney() {

	}

	public void manufactureYearRegistrationYearValidator() {

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
				int days = -1;
				int noOfMonths = permitRenewalsDTO.getRequestRenewPeriod();
				String validDate = permitRenewalsDTO.getValidToDate();
				String fromDate = permitRenewalsDTO.getFromToDate();
				String beforepermitDate = permitRenewalsDTO.getPermitExpiryDate();
				String format = "dd/MM/yyyy";
				SimpleDateFormat sdf = new SimpleDateFormat(format);

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
				cal.add(Calendar.DAY_OF_MONTH, days);
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

	public void updateRecord() throws ParseException {

		if (permitRenewalsDTO.getMobileNo().isEmpty() || permitRenewalsDTO.getMobileNo().equals("")
				|| permitRenewalsDTO.getMobileNo() == null) {

			errorMsg = "Mobile Number should be entered.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else if (selectedProvinceCode.isEmpty() || selectedProvinceCode.equals("") || selectedProvinceCode == null) {

			errorMsg = "Province should be selected.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else if (selectedDistricCode.isEmpty() || selectedDistricCode.equals("") || selectedDistricCode == null) {

			errorMsg = "District should be selected.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		} else if (permitRenewalsDTO.getAddressOne().isEmpty() || permitRenewalsDTO.getAddressOne().equals("")
				|| permitRenewalsDTO.getAddressOne() == null) {

			errorMsg = "Address One should be entered.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else if (permitRenewalsDTO.getAddressTwo().isEmpty() || permitRenewalsDTO.getAddressTwo().equals("")
				|| permitRenewalsDTO.getAddressTwo() == null) {

			errorMsg = "Address Two should be entered.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else if (permitRenewalsDTO.getCity().isEmpty() || permitRenewalsDTO.getCity().equals("")
				|| permitRenewalsDTO.getCity() == null) {

			errorMsg = "City should be entered.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else if ((!permitRenewalsDTO.getMobileNo().isEmpty() || !permitRenewalsDTO.getMobileNo().equals("")
				|| permitRenewalsDTO.getMobileNo() != null)
				&& (!selectedProvinceCode.isEmpty() || !selectedProvinceCode.equals("") || selectedProvinceCode != null)
				&& (!selectedDistricCode.isEmpty() || !selectedDistricCode.equals("") || selectedDistricCode != null)
				&& (!selectedDivisionSecCode.isEmpty() || !selectedDivisionSecCode.equals("")
						|| selectedDivisionSecCode != null)
				&& (!permitRenewalsDTO.getAddressOne().isEmpty() || !permitRenewalsDTO.getAddressOne().equals("")
						|| permitRenewalsDTO.getAddressOne() != null)
				&& (!permitRenewalsDTO.getAddressTwo().isEmpty() || !permitRenewalsDTO.getAddressTwo().equals("")
						|| permitRenewalsDTO.getAddressTwo() != null)
				&& (!permitRenewalsDTO.getCity().isEmpty() || !permitRenewalsDTO.getCity().equals("")
						|| permitRenewalsDTO.getCity() != null)) {

			String modifyBy = sessionBackingBean.loginUser;
			checkMobileNum();
			checkTelNum();

			String format = "dd/MM/yyyy";
			SimpleDateFormat sdf = new SimpleDateFormat(format);

			if (permitRenewalsDTO.getAddressOne().equals(searchedDTO.getAddressOne())
					&& permitRenewalsDTO.getAddressOneSinhala().equals(searchedDTO.getAddressOneSinhala())
					&& permitRenewalsDTO.getAddressOneTamil().equals(searchedDTO.getAddressOneTamil())
					&& permitRenewalsDTO.getAddressTwo().equals(searchedDTO.getAddressTwo())
					&& permitRenewalsDTO.getAddressTwoSinhala().equals(searchedDTO.getAddressTwoSinhala())
					&& permitRenewalsDTO.getAddressTwoTamil().equals(searchedDTO.getAddressTwoTamil())
					&& permitRenewalsDTO.getCity().equals(searchedDTO.getCity())
					&& permitRenewalsDTO.getCitySinhala().equals(searchedDTO.getCitySinhala())
					&& permitRenewalsDTO.getCityTamil().equals(searchedDTO.getCityTamil())
					&& selectedMaterialStatus.equals(searchedDTO.getMaterialStatusId())
					&& selectedProvinceCode.equals(searchedDTO.getProvinceCode())
					&& permitRenewalsDTO.getTeleNo().equals(searchedDTO.getTeleNo())
					&& selectedDistricCode.equals(searchedDTO.getDistrictCode())
					&& permitRenewalsDTO.getMobileNo().equals(searchedDTO.getMobileNo())
					&& selectedDivisionSecCode.equals(searchedDTO.getDivisionalSecretariatDivision())) {

				setErrorMsg("Please update Particulars of Bus / Owner before save.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			} else {

				if (numCheckTel == false && numCheckMobile == false) {
					permitRenewalsDTO.setModifyBy(modifyBy);
					permitRenewalsDTO.setMaterialStatusId(selectedMaterialStatus);
					permitRenewalsDTO.setTeleNo(permitRenewalsDTO.getTeleNo());
					permitRenewalsDTO.setProvinceCode(selectedProvinceCode);
					permitRenewalsDTO.setDistrictCode(selectedDistricCode);
					permitRenewalsDTO.setDivisionalSecretariatDivision(selectedDivisionSecCode);
					permitRenewalsDTO.setAddressOne(permitRenewalsDTO.getAddressOne());
					permitRenewalsDTO.setAddressTwo(permitRenewalsDTO.getAddressTwo());
					permitRenewalsDTO.setCity(permitRenewalsDTO.getCity());
					searchedCurrentSeqNoForApplicationTable = permitRenewalsService.getCurrentSeqNoForPermitNo(
							selectedPermitNo, permitRenewalsDTO.getQueueNo(), permitRenewalsDTO.getApplicationNo());
					searchedSeqNoForOwnerTable = permitRenewalsService.getCurrentOwnerSeqNo(selectedPermitNo,
							permitRenewalsDTO.getQueueNo(), permitRenewalsDTO.getApplicationNo());
					permitRenewalsDTO.setSeqno(searchedCurrentSeqNoForApplicationTable.getSeqno());
					permitRenewalsDTO.setApplicationNo(searchedCurrentSeqNoForApplicationTable.getApplicationNo());
					permitRenewalsDTO.setOwnerSeqNo(searchedSeqNoForOwnerTable.getOwnerSeqNo());
					permitRenewalsDTO.setPermitNo(selectedPermitNo);
					permitRenewalsDTO.setFromToDate(permitRenewalsDTO.getFromToDate());

					vehicleOwnerHistoryDTO = historyService.getVehicleOwnerTableData(
							permitRenewalsDTO.getApplicationNo(), sessionBackingBean.getLoginUser());
					int resultOwnerTable = permitRenewalsService.updatePermitRenewalOwnerRecord(permitRenewalsDTO);

					if (resultOwnerTable == 0) {
						activeTabIndex = 1;
						RequestContext.getCurrentInstance().update("frmsuccessSve");
						setSucessMsg("Successfully Saved.");
						RequestContext.getCurrentInstance().execute("PF('successSve').show()");

						displayCheckDocumentDataList();
						displayPaymentHistoryDataList();

						/* Insert to Vehicle Owner History Table */
						historyService.insertVehicleOwnerHistoryData(vehicleOwnerHistoryDTO);

						boolean checkTaskCodeInTaskDet = inspectionActionPointService
								.isCheckedTaskCodeInTaskDetCompleted(permitRenewalsDTO.getApplicationNo(), "PR200");
						if (checkTaskCodeInTaskDet) {
							taskDetWithAppDetDTO = new VehicleInspectionDTO();
							taskDetWithAppDetDTO = inspectionActionPointService
									.getDetailsWithModiFyDate(permitRenewalsDTO.getApplicationNo());
							boolean checkValueWithSameTaskCode = inspectionActionPointService
									.isCheckedSameTaskCodeForSelectedApp(permitRenewalsDTO.getApplicationNo(), "PR200",
											"C");
							if (checkValueWithSameTaskCode == true) {
								int resultInTaskDetWithAppDet = inspectionActionPointService.updateNewTaskDetWithAppDet(
										taskDetWithAppDetDTO, sessionBackingBean.getLoginUser(),
										permitRenewalsDTO.getApplicationNo(), "PR200", "C");
							} else {
								int resultInTaskDetWithAppDet = inspectionActionPointService.insertNewTaskDetWithAppDet(
										taskDetWithAppDetDTO, sessionBackingBean.getLoginUser(),
										permitRenewalsDTO.getApplicationNo(), "PR200", "C");
							}
						} else {
							taskDetWithAppDetDTO = new VehicleInspectionDTO();
							taskDetWithAppDetDTO = inspectionActionPointService
									.getDetailsWithModiFyDate(permitRenewalsDTO.getApplicationNo());
							boolean checkValueWithSameTaskCode = inspectionActionPointService
									.isCheckedSameTaskCodeForSelectedApp(permitRenewalsDTO.getApplicationNo(), "PR200",
											"O");
							if (checkValueWithSameTaskCode == true) {
								int resultInTaskDetWithAppDet = inspectionActionPointService.updateNewTaskDetWithAppDet(
										taskDetWithAppDetDTO, sessionBackingBean.getLoginUser(),
										permitRenewalsDTO.getApplicationNo(), "PR200", "O");
							} else {
								int resultInTaskDetWithAppDet = inspectionActionPointService.insertNewTaskDetWithAppDet(
										taskDetWithAppDetDTO, sessionBackingBean.getLoginUser(),
										permitRenewalsDTO.getApplicationNo(), "PR200", "O");
							}
						}

					} else {

						RequestContext.getCurrentInstance().update("frmerrorMsge");
						setErrorMsg("Errors.");
						RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
					}
				} else {

				}

			}

		}
	}

	private void updateStatusOfQueueNumberAfterCallNextComplete() {
		if (strQueueNo != null && !strQueueNo.isEmpty() && !strQueueNo.trim().equalsIgnoreCase("")) {

			migratedService.updateStatusOfQueueNumberAfterCallNext(strQueueNo, "C");

			commonService.updateTaskStatus(selectedApplicationNo, "PM101", "PR200", "C",
					sessionBackingBean.getLoginUser());
			disableCallNextBtn = true;
			disableSkipBtn = false;

		} else {

		}
	}

	private void displayPaymentHistoryDataList() {

		paymentHistoryDataList = permitRenewalsService.getAllPaymentHistoryList(permitRenewalsDTO.getApplicationNo(),
				permitRenewalsDTO.getPermitNo());

		for (int i = 0; i < paymentHistoryDataList.size(); i++) {

			String txnDate = paymentHistoryDataList.get(i).getTxnDate();
			String[] parts = txnDate.split(" ");
			String part1 = parts[0];
			String part2 = parts[1];

			paymentHistoryDataList.get(i).setDisplayTxnDate(part1);

		}

	}

	public void clearFields() {

		permitRenewalsDTO = new PermitRenewalsDTO();
		permitRenewalsDTO.setSeqno(null);
		permitRenewalsDTO.setApplicationNo(null);
		permitRenewalsDTO.setQueueNo(null);
		permitRenewalsDTO.setPermitNo(null);
		permitRenewalsDTO.setRegNoOfBus(null);
		permitRenewalsDTO.setSpecialRemark(null);
		permitRenewalsDTO.setServiceTypeCode(null);
		permitRenewalsDTO.setRouteNo(null);
		permitRenewalsDTO.setPermitExpiryDate(null);
		permitRenewalsDTO.setRequestRenewPeriod(0);
		permitRenewalsDTO.setServiceTypeDescription(null);
		permitRenewalsDTO.setPlaceOfOrginOfTheService(null);
		permitRenewalsDTO.setPlaceOfDestination(null);
		permitRenewalsDTO.setPreferedLanguageCode(null);
		permitRenewalsDTO.setTitleCode(null);
		permitRenewalsDTO.setFullName(null);
		permitRenewalsDTO.setFullNameSinhala(null);
		permitRenewalsDTO.setFullNameTamil(null);
		permitRenewalsDTO.setNameWithInitials(null);
		permitRenewalsDTO.setNic(null);
		permitRenewalsDTO.setGenderCode(null);
		permitRenewalsDTO.setDob(null);
		permitRenewalsDTO.setMaterialStatusId(null);
		permitRenewalsDTO.setTeleNo(null);
		permitRenewalsDTO.setMobileNo(null);
		permitRenewalsDTO.setProvinceCode(null);
		permitRenewalsDTO.setDistrictCode(null);
		permitRenewalsDTO.setDivisionalSecretariatDivision(null);
		permitRenewalsDTO.setAddressOne(null);
		permitRenewalsDTO.setAddressOneSinhala(null);
		permitRenewalsDTO.setAddressOneTamil(null);
		permitRenewalsDTO.setAddressTwo(null);
		permitRenewalsDTO.setAddressTwoSinhala(null);
		permitRenewalsDTO.setAddressTwoTamil(null);
		permitRenewalsDTO.setCity(null);
		permitRenewalsDTO.setCitySinhala(null);
		permitRenewalsDTO.setCityTamil(null);
		permitRenewalsDTO.setNewPermitExpirtDate(null);
		selectedProvinceCode = null;
		selectedDistricCode = null;
		selectedMaterialStatus = null;
		divisionSectionList = permitRenewalsService.getAllDivisionSections(selectedDistricCode);
		selectedDivisionSecCode = permitRenewalsDTO.getDivisionalSecretariatDivision();
		permitRenewalsDTO.setPreferedLanguageDescription(null);
		permitRenewalsDTO.setGenderDescription(null);
		permitRenewalsDTO.setTitleDescription(null);
		setSelectedPermitNo(null);
		setSelectedApplicationNo(null);
		setQueueNoChecked(false);
		setDisableCallNextBtn(false);
		setDisableSkipBtn(true);
		setShowThirdForm(false);
		activeTabIndex = 0;
		setDisableSearchBtn(false);
		setDisabledPermitNoMenu(false);
		setDisabledApplicationNoMenu(false);

		setShowbacklogvalueLoad(false);
		setShowbacklogvalue12(false);
		setRequestNewPeriodReadOnly(false);
		setShowNewPermitDateInput(true);
		setDisabledSaveBtnOne(false);
		setDisabledTab(true);
		java.util.Date date = new java.util.Date();

		Date todaysDate = new Date();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String testDateString = df.format(todaysDate);

		permitRenewalsDTO.setDateOne(testDateString);
		validationMsg = null;
		validationMsgforComplain = null;
		isIncompleteInspection = false;

	}

	public void callNext() throws ParseException {

		String para = null;
		callingQueueNo = queueManagementService.callNextQueueNumberAction("04", "02");
		strQueueNo = callingQueueNo;
		permitRenewalsDTO.setQueueNo(callingQueueNo);

		ApplicationNoForSearchedQueueNo = permitRenewalsService.getApplicationNoForSelectetedQueueNo(callingQueueNo);
		String currentAppNoForCallingQueueNo = ApplicationNoForSearchedQueueNo.getApplicationNo();
		if (currentAppNoForCallingQueueNo != null) {
			detialsForSearchedQueueNo = permitRenewalsService
					.getRecordDetailsForCurrentAppNo(currentAppNoForCallingQueueNo);
			permitRenewalsDTO.setApplicationNo(detialsForSearchedQueueNo.getApplicationNo());
			permitRenewalsDTO.setPermitNo(detialsForSearchedQueueNo.getPermitNo());
			permitRenewalsDTO.setRegNoOfBus(detialsForSearchedQueueNo.getRegNoOfBus());

			setSelectedPermitNo(permitRenewalsDTO.getPermitNo());
			setSelectedApplicationNo(permitRenewalsDTO.getApplicationNo());

			setQueueNoChecked(true);
		} else {

		}

		if (strQueueNo != null && !strQueueNo.isEmpty() && !strQueueNo.trim().equalsIgnoreCase("")) {
			commonService.updateCounterQueueNo(strQueueNo, commonDTO.getCounterId());

			migratedService.updateStatusOfQueueNumberAfterCallNext(strQueueNo, "O");

			setDisableCallNextBtn(true);
			setDisableSkipBtn(false);

			migratedService.updateCounterIdOfQueueNumberAfterCallNext(strQueueNo, sessionBackingBean.getCounterId());

		} else {
			setErrorMsg("Queue is empty.");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public void skipAction() {

		if (strQueueNo != null && !strQueueNo.isEmpty() && !strQueueNo.trim().equalsIgnoreCase("")) {

			migratedService.updateSkipQueueNumberStatus(strQueueNo);

			commonService.updateCounterNo(sessionBackingBean.getCounterId());
			disableCallNextBtn = true;
			disableSkipBtn = false;
			clearFields();
		}
	}

	public void searchAction() throws ParseException {

		if (selectedPermitNo == null && permitRenewalsDTO.getQueueNo().equals("") && selectedApplicationNo == null) {

			errorMsg = "Queue No. should be entered or should be called next queue no.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else if (selectedPermitNo != null && permitRenewalsDTO.getQueueNo().equals("")
				&& selectedApplicationNo == null) {

			errorMsg = " This Queue No. is not eligible for permit renewals";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		} else if (selectedPermitNo == null && !permitRenewalsDTO.getQueueNo().equals("")
				&& selectedApplicationNo == null) {

			errorMsg = " This Queue No. is not eligible for permit renewals";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		} else if (selectedPermitNo != null && !permitRenewalsDTO.getQueueNo().equals("")
				&& selectedApplicationNo == null) {

			errorMsg = " This Queue No. is not eligible for permit renewals";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		} else if (selectedPermitNo == null && permitRenewalsDTO.getQueueNo().equals("")
				&& selectedApplicationNo != null) {

			errorMsg = " This Queue No. is not eligible for permit renewals";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		} else if (selectedPermitNo != null && permitRenewalsDTO.getQueueNo().equals("")
				&& selectedApplicationNo != null) {

			errorMsg = " This Queue No. is not eligible for permit renewals";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		} else if (selectedPermitNo == null && !permitRenewalsDTO.getQueueNo().equals("")
				&& selectedApplicationNo != null) {

			errorMsg = " This Queue No. is not eligible for permit renewals";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		} else if (selectedPermitNo != null && !permitRenewalsDTO.getQueueNo().equals("")
				&& selectedApplicationNo != null) {

			// Validate Vehicle no having incomplete inspection
			isIncompleteInspection = commonService.IsHavingIncompleteInspection(permitRenewalsDTO.getRegNoOfBus());
			if (isIncompleteInspection) {
				errorMsg = " Vehicle Inspection Incomplete - Please Refer to High Authorized officer";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			} else {
				if (!(permitRenewalsDTO.getRegNoOfBus().equals(null)) || (!permitRenewalsDTO.getRegNoOfBus().equals(""))
						|| (!permitRenewalsDTO.getRegNoOfBus().isEmpty())) {
					commonDTO = commonService.vehicleNoValidation(permitRenewalsDTO.getRegNoOfBus());
					// only have complain record
					if ((commonDTO.getComplainNo() != null) && (commonDTO.getGrantComplainNo() == null)) {
						errorMsg = " Active/Ongoing Complain is Available.";
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}
					else
					{
						searchChecking();
						//Complain have grant permission
						if(commonDTO.getGrantComplainNo() != null)
						{
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
						
						if(validationMsgforComplain != null)
						{
							RequestContext.getCurrentInstance().update("frmvehicleComplainValidate");
							RequestContext.getCurrentInstance().execute("PF('vehicleComplainValidate').show()");
						}
						else if(validationMsg != null) {
							RequestContext.getCurrentInstance().update("frmvehicleValidate");
							RequestContext.getCurrentInstance().execute("PF('vehicleValidate').show()");
						}
					}
					
				}
			}
		}

	}

	public void searchChecking() throws ParseException {

		String loginUser = sessionBackingBean.getLoginUser();

		setDisableSearchBtn(true);
		setShowThirdForm(true);

		queueNoChecked = true;
		String previouesSpecialRemark = permitRenewalsService.getPreSpecialRemark(selectedPermitNo,
				permitRenewalsDTO.getRegNoOfBus());
		searchedDTO = permitRenewalsService.getSearchedDetailsWithPermitNoOrQueueNo(selectedPermitNo,
				permitRenewalsDTO.getQueueNo(), selectedApplicationNo, "O");

		inspectionDetails = permitRenewalsService.getVehiDetailsYr(selectedApplicationNo,
				permitRenewalsDTO.getRegNoOfBus());

		setOminiBusDTO(inspectionDetails);
		if (inspectionDetails.getInsuCat() == null) {
			ominiBusDTO.setInsuCat("UNLIMITED");
		} else if (inspectionDetails.getInsuCat() != null) {
			ominiBusDTO.setInsuCat(inspectionDetails.getInsuCat());
		}
		displayCheckDocumentDataList();
		displayPaymentHistoryDataList();

		permitRenewalsDTO.setSeqno(searchedDTO.getSeqno());
		permitRenewalsDTO.setApplicationNo(searchedDTO.getApplicationNo());
		permitRenewalsDTO.setQueueNo(searchedDTO.getQueueNo());
		permitRenewalsDTO.setPermitNo(searchedDTO.getPermitNo());
		permitRenewalsDTO.setRegNoOfBus(searchedDTO.getRegNoOfBus());
		permitRenewalsDTO.setPreSpecialRemark(previouesSpecialRemark);
		permitRenewalsDTO.setSpecialRemark(searchedDTO.getSpecialRemark());
		permitRenewalsDTO.setServiceTypeCode(searchedDTO.getServiceTypeCode());
		permitRenewalsDTO.setRouteNo(searchedDTO.getRouteNo());
		permitRenewalsDTO.setPermitExpiryDate(searchedDTO.getPermitExpiryDate());
		// permitRenewalsDTO.setRequestRenewPeriod(searchedDTO.getRequestRenewPeriod());
		permitRenewalsDTO.setServiceTypeDescription(searchedDTO.getServiceTypeDescription());
		permitRenewalsDTO.setPlaceOfOrginOfTheService(searchedDTO.getPlaceOfOrginOfTheService());
		permitRenewalsDTO.setPlaceOfDestination(searchedDTO.getPlaceOfDestination());
		permitRenewalsDTO.setPreferedLanguageCode(searchedDTO.getPreferedLanguageCode());
		permitRenewalsDTO.setTitleCode(searchedDTO.getTitleCode());
		permitRenewalsDTO.setFullName(searchedDTO.getFullName());
		permitRenewalsDTO.setFullNameSinhala(searchedDTO.getFullNameSinhala());
		permitRenewalsDTO.setFullNameTamil(searchedDTO.getFullNameTamil());
		permitRenewalsDTO.setNameWithInitials(searchedDTO.getNameWithInitials());
		permitRenewalsDTO.setNic(searchedDTO.getNic());
		permitRenewalsDTO.setGenderCode(searchedDTO.getGenderCode());
		permitRenewalsDTO.setDob(searchedDTO.getDob());
		permitRenewalsDTO.setMaterialStatusId(searchedDTO.getMaterialStatusId());
		permitRenewalsDTO.setTeleNo(searchedDTO.getTeleNo());
		permitRenewalsDTO.setMobileNo(searchedDTO.getMobileNo());
		permitRenewalsDTO.setProvinceCode(searchedDTO.getProvinceCode());
		permitRenewalsDTO.setDistrictCode(searchedDTO.getDistrictCode());
		permitRenewalsDTO.setDivisionalSecretariatDivision(searchedDTO.getDivisionalSecretariatDivision());
		permitRenewalsDTO.setAddressOne(searchedDTO.getAddressOne());
		permitRenewalsDTO.setAddressOneSinhala(searchedDTO.getAddressOneSinhala());
		permitRenewalsDTO.setAddressOneTamil(searchedDTO.getAddressOneTamil());
		permitRenewalsDTO.setAddressTwo(searchedDTO.getAddressTwo());
		permitRenewalsDTO.setAddressTwoSinhala(searchedDTO.getAddressTwoSinhala());
		permitRenewalsDTO.setAddressTwoTamil(searchedDTO.getAddressTwoTamil());
		permitRenewalsDTO.setCity(searchedDTO.getCity());
		permitRenewalsDTO.setCitySinhala(searchedDTO.getCitySinhala());
		permitRenewalsDTO.setCityTamil(searchedDTO.getCityTamil());
		permitRenewalsDTO.setRequestRenewPeriod(searchedDTO.getRequestRenewPeriod());
		permitRenewalsDTO.setNewPermitExpirtDate(searchedDTO.getNewPermitExpirtDate());
		selectedPermitNo = permitRenewalsDTO.getPermitNo();
		selectedProvinceCode = permitRenewalsDTO.getProvinceCode();
		districtList = permitRenewalsService.getAllDistrictsForCurrentProvince(selectedProvinceCode);
		selectedDistricCode = permitRenewalsDTO.getDistrictCode();
		selectedMaterialStatus = permitRenewalsDTO.getMaterialStatusId();
		divisionSectionList = permitRenewalsService.getAllDivisionSections(selectedDistricCode);
		selectedDivisionSecCode = permitRenewalsDTO.getDivisionalSecretariatDivision();

		RequestContext.getCurrentInstance().update("martialStatusId");
		String languageDescription = permitRenewalsService
				.getLangaugeDescription(permitRenewalsDTO.getPreferedLanguageCode());
		permitRenewalsDTO.setPreferedLanguageDescription(languageDescription);
		String genderDescription = permitRenewalsService.getGenderDescription(permitRenewalsDTO.getGenderCode());
		permitRenewalsDTO.setGenderDescription(genderDescription);
		String titleDescription = permitRenewalsService.getTitleDescription(permitRenewalsDTO.getTitleCode());
		permitRenewalsDTO.setTitleDescription(titleDescription);
		permitRenewalsDTO.setValidToDate(searchedDTO.getValidToDate());
		permitRenewalsDTO.setFromToDate(searchedDTO.getFromToDate());
		permitRenewalsDTO.setBacklogAppValue(searchedDTO.getBacklogAppValue());
		permitRenewalsDTO.setCheckBacklogValue(searchedDTO.isCheckBacklogValue());
		String format = "dd/MM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		if (permitRenewalsDTO.getValidToDate() != null) {

			Date validDateObj = sdf.parse(permitRenewalsDTO.getValidToDate());
			permitRenewalsDTO.setPermitExpiredValidToDateObj(validDateObj);

		} else {

		}
		if (permitRenewalsDTO.getFromToDate() != null) {

			Date fromDateObj = sdf.parse(permitRenewalsDTO.getFromToDate());
			permitRenewalsDTO.setPermitExpiredFromDateObj(fromDateObj);

		} else {

			Date permitExpiredDateForFromDate = sdf.parse(permitRenewalsDTO.getPermitExpiryDate());
			permitRenewalsDTO.setPermitExpiredFromDateObj(permitExpiredDateForFromDate);
		}
		setDisableCallNextBtn(true);
		setDisableSkipBtn(true);
		setDisabledPermitNoMenu(true);
		setDisabledApplicationNoMenu(true);
		setDisabledTab(false);
		setShowbacklogvalue(permitRenewalsDTO.isCheckBacklogValue());
		setShowbacklogvalue12(permitRenewalsDTO.isCheckBacklogValue());

		permitRenewalsDTO.setRouteFlageValue(searchedDTO.getRouteFlageValue());
		permitRenewalsDTO.setRouteFlagChecked(searchedDTO.isRouteFlagChecked());
		if (permitRenewalsDTO.isRouteFlagChecked() == true) {
			permitRenewalsDTO.setPlaceOfOrginOfTheService(searchedDTO.getPlaceOfDestination());
			permitRenewalsDTO.setPlaceOfDestination(searchedDTO.getPlaceOfOrginOfTheService());
		} else if (permitRenewalsDTO.isRouteFlagChecked() == false) {
			permitRenewalsDTO.setPlaceOfOrginOfTheService(searchedDTO.getPlaceOfOrginOfTheService());
			permitRenewalsDTO.setPlaceOfDestination(searchedDTO.getPlaceOfDestination());

		}

		if (permitRenewalsDTO.getPermitExpiredValidToDateObj() != null) {
			setDisabledReqPeriodInput(false);
		}
	}

	public void clearBusDetails() {

		ominiBusDTO = new OminiBusDTO();
		getOminiBusDetails2();
		getRequestRenewalDet();
		// setDisabledReqPeriodInput(true);
		activeTabIndex = 1;
	}

	private void getRequestRenewalDet() {
		String previouesSpecialRemark = permitRenewalsService.getPreSpecialRemark(selectedPermitNo,
				permitRenewalsDTO.getRegNoOfBus());
		requestRenewDetSearchedDTO = permitRenewalsService.getSearchedRequestRenewalDetForCurrentQueueNoOrPermitNo(
				selectedPermitNo, permitRenewalsDTO.getQueueNo(), selectedApplicationNo, "O");

		permitRenewalsDTO.setPreSpecialRemark(previouesSpecialRemark);
		permitRenewalsDTO.setSpecialRemark(requestRenewDetSearchedDTO.getSpecialRemark());

		permitRenewalsDTO.setPermitExpiryDate(requestRenewDetSearchedDTO.getPermitExpiryDate());

		permitRenewalsDTO.setRequestRenewPeriod(requestRenewDetSearchedDTO.getRequestRenewPeriod());
		permitRenewalsDTO.setNewPermitExpirtDate(requestRenewDetSearchedDTO.getNewPermitExpirtDate());
		permitRenewalsDTO.setValidToDate(requestRenewDetSearchedDTO.getValidToDate());
		permitRenewalsDTO.setFromToDate(requestRenewDetSearchedDTO.getFromToDate());
		permitRenewalsDTO.setBacklogAppValue(requestRenewDetSearchedDTO.getBacklogAppValue());
		permitRenewalsDTO.setCheckBacklogValue(requestRenewDetSearchedDTO.isCheckBacklogValue());
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

			permitRenewalsDTO.setPermitExpiredValidToDateObj(null);
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

	}

	private void getOminiBusDetails2() {
		ominiBusDTO.setVehicleRegNo(permitRenewalsDTO.getRegNoOfBus());
		issuePermitDTO.setPermitNo(selectedPermitNo);
		if (issuePermitDTO.getPermitNo() != null && !issuePermitDTO.getPermitNo().isEmpty()
				&& issuePermitDTO.getPermitNo() != "") {

			inspectionDetails = permitRenewalsService.getVehiDetailsYr(selectedApplicationNo,
					permitRenewalsDTO.getRegNoOfBus());

			setOminiBusDTO(inspectionDetails);

			ominiBusDTO.setSeq(0);

			if (inspectionDetails.getInsuCat() == null) {
				ominiBusDTO.setInsuCat("UNLIMITED");
			} else if (inspectionDetails.getInsuCat() != null) {
				ominiBusDTO.setInsuCat(inspectionDetails.getInsuCat());
			}
		}
	}

	public void getOminiBusDetails() {
		ominiBusDTO.setVehicleRegNo(permitRenewalsDTO.getRegNoOfBus());
		issuePermitDTO.setPermitNo(selectedPermitNo);
		ominiBusDTO = new OminiBusDTO();
		if (issuePermitDTO.getPermitNo() != null && !issuePermitDTO.getPermitNo().isEmpty()
				&& issuePermitDTO.getPermitNo() != "") {

			inspectionDetails = permitRenewalsService.getVehiDetailsYr(selectedApplicationNo,
					permitRenewalsDTO.getRegNoOfBus());

			setOminiBusDTO(inspectionDetails);

		}
	}

	public void checkTelNum() {
		if (!permitRenewalsDTO.getTeleNo().isEmpty() && permitRenewalsDTO.getTeleNo() != null
				&& !permitRenewalsDTO.getTeleNo().equalsIgnoreCase("")) {

			Pattern pattern = Pattern.compile("\\d{3}\\d{7}");
			Matcher matcher = pattern.matcher(permitRenewalsDTO.getTeleNo());
			if (matcher.matches()) {
				numCheckTel = false;
			} else {

				setErrorMsg("Valid Tel Number should be entered.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				numCheckTel = true;
			}
		}

	}

	public void checkMobileNum() {
		if (!permitRenewalsDTO.getMobileNo().isEmpty() && permitRenewalsDTO.getMobileNo() != null
				&& !permitRenewalsDTO.getMobileNo().equalsIgnoreCase("")) {
			Pattern pattern = Pattern.compile("\\d{3}\\d{7}");
			Matcher mobileNoMatcher = pattern.matcher(permitRenewalsDTO.getMobileNo());
			if (mobileNoMatcher.matches()) {
				numCheckMobile = false;
			} else {

				setErrorMsg("Valid Mobile Number should be entered.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				numCheckMobile = true;
			}
		}
	}

	public void ajaxInsertPhysicalExitRecord() {

		for (int i = 0; i < dataList.size(); i++) {

			if (dataList.get(i).isPhysicallyExists() == true) {

				permitRenewalsService.insertDocumentPhysicallyExitsStatus(dataList.get(i).isPhysicallyExists(),
						dataList.get(i).getDocumentCode(), permitRenewalsDTO.getApplicationNo(),
						permitRenewalsDTO.getPermitNo(), sessionBackingBean.getLoginUser());

			} else {
				permitRenewalsService.deleteDocumentPhysicallyExitsStatus(dataList.get(i).isPhysicallyExists(),
						dataList.get(i).getDocumentCode(), permitRenewalsDTO.getApplicationNo(),
						permitRenewalsDTO.getPermitNo(), sessionBackingBean.getLoginUser());
			}
		}

	}

	public void displayCheckDocumentDataList() {
		dataList = permitRenewalsService.getAllRecordsForDocumentsCheckings();
		for (int i = 0; i < dataList.size(); i++) {

			String currentDocCode = dataList.get(i).getDocumentCode();
			String currentDocCodeDescription = dataList.get(i).getDocumentDescription();

			boolean resultValue = permitRenewalsService.checkIsSumbiited(currentDocCode,
					permitRenewalsDTO.getApplicationNo(), permitRenewalsDTO.getPermitNo());
			if (resultValue == false) {

				dataList.get(i).setExists(false);
				dataList.get(i).setHasRecord(false);

			} else {
				dataList.get(i).setExists(true);
				dataList.get(i).setHasRecord(true);
				setCheckExists(true);
			}

			boolean isMandatory = permitRenewalsService.isMandatory(currentDocCode,
					permitRenewalsDTO.getApplicationNo(), permitRenewalsDTO.getPermitNo());

			boolean isPhysicallyExit = permitRenewalsService.isPhysicallyExit(currentDocCode,
					permitRenewalsDTO.getApplicationNo(), permitRenewalsDTO.getPermitNo());

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
				setCheckExists(true);
			}

			checkAndDisplayRemarkValue = permitRenewalsService.getRemarkDetails(currentDocCode,
					permitRenewalsDTO.getApplicationNo(), permitRenewalsDTO.getPermitNo());
			dataList.get(i).setDocSeqChecked(checkAndDisplayRemarkValue.getDocSeqChecked());
			dataList.get(i).setRemark(checkAndDisplayRemarkValue.getRemark());
			dataList.get(i).setDocFilePath(checkAndDisplayRemarkValue.getDocFilePath());
		}
	}

	public void updateDocuments() {

		boolean isOkToContinue = false;
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
			errorMsg = "Please upload the mandatory document.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else {

			for (int i = 0; i < dataList.size(); i++) {

				if (dataList.get(i).isHasRecord() == true) {
					Long currentRecordSeqNo = dataList.get(i).getDocSeqChecked();
					String currentUploadFilePath = dataList.get(i).getDocFilePath();
					String modifyBy = sessionBackingBean.loginUser;
					String currentRemark = dataList.get(i).getRemark();
					int result = permitRenewalsService.updateDocumentRemark(currentRecordSeqNo, currentUploadFilePath,
							modifyBy, currentRemark);

					if (result == 0) {

						if (dataList.get(i).isPhysicallyExists() == true) {
							permitRenewalsService.insertDocumentPhysicallyExitsStatus(
									dataList.get(i).isPhysicallyExists(), dataList.get(i).getDocumentCode(),
									permitRenewalsDTO.getApplicationNo(), permitRenewalsDTO.getPermitNo(),
									sessionBackingBean.getLoginUser());
						} else {

						}

						RequestContext.getCurrentInstance().update("frmsuccessSve");
						setSucessMsg("Successfully Saved.");
						RequestContext.getCurrentInstance().execute("PF('successSve').show()");
						updateTaskCodeInQueueMaster(permitRenewalsDTO.getQueueNo(),
								permitRenewalsDTO.getApplicationNo());

						updateStatusOfQueueNumberAfterCallNextComplete();
						updatePR200TaskStatus();

						migratedService.updateTransactionTypeCodeForQueueNo(permitRenewalsDTO.getQueueNo(), "04");

					} else {

						RequestContext.getCurrentInstance().update("frmerrorMsge");
						setErrorMsg("Errors.");
						RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
					}
				} else {

					/* Added By Gayathra. For save Physically Exit Books */

					if (dataList.get(i).isPhysicallyExists() == true) {
						permitRenewalsService.insertDocumentPhysicallyExitsStatus(dataList.get(i).isPhysicallyExists(),
								dataList.get(i).getDocumentCode(), permitRenewalsDTO.getApplicationNo(),
								permitRenewalsDTO.getPermitNo(), sessionBackingBean.getLoginUser());
						RequestContext.getCurrentInstance().update("frmsuccessSve");
						setSucessMsg("Successfully Saved.");
						RequestContext.getCurrentInstance().execute("PF('successSve').show()");
						updateTaskCodeInQueueMaster(permitRenewalsDTO.getQueueNo(),
								permitRenewalsDTO.getApplicationNo());

						updateStatusOfQueueNumberAfterCallNextComplete();
						updatePR200TaskStatus();

						migratedService.updateTransactionTypeCodeForQueueNo(permitRenewalsDTO.getQueueNo(), "04");

					} else {

						RequestContext.getCurrentInstance().update("frmsuccessSve");
						setSucessMsg("Successfully Saved.");
						RequestContext.getCurrentInstance().execute("PF('successSve').show()");
						updateTaskCodeInQueueMaster(permitRenewalsDTO.getQueueNo(),
								permitRenewalsDTO.getApplicationNo());

						updateStatusOfQueueNumberAfterCallNextComplete();
						updatePR200TaskStatus();

						migratedService.updateTransactionTypeCodeForQueueNo(permitRenewalsDTO.getQueueNo(), "04");

					}

				}
			}

			taskDetWithAppDetDTO = new VehicleInspectionDTO();
			taskDetWithAppDetDTO = inspectionActionPointService
					.getDetailsWithModiFyDate(permitRenewalsDTO.getApplicationNo());
			boolean checkValueWithSameTaskCode = inspectionActionPointService
					.isCheckedSameTaskCodeForSelectedApp(permitRenewalsDTO.getApplicationNo(), "PR200", "C");
			if (checkValueWithSameTaskCode == true) {
				int resultInTaskDetWithAppDet = inspectionActionPointService.updateNewTaskDetWithAppDet(
						taskDetWithAppDetDTO, sessionBackingBean.getLoginUser(), permitRenewalsDTO.getApplicationNo(),
						"PR200", "C");
			} else {
				int resultInTaskDetWithAppDet = inspectionActionPointService.insertNewTaskDetWithAppDet(
						taskDetWithAppDetDTO, sessionBackingBean.getLoginUser(), permitRenewalsDTO.getApplicationNo(),
						"PR200", "C");
			}

		}

	}

	private void updateTaskCodeInQueueMaster(String queueNo, String appNum) {

		migratedService.updateQueueNumberTaskInQueueMaster(queueNo, appNum, "PR200", "C");

	}

	private void updatePR200TaskStatus() {

		String loginUser = sessionBackingBean.getLoginUser();
		boolean checkTaskCodePR200IsValid = permitRenewalsService.checkTaskCodeForCurrentAppNo(selectedApplicationNo,
				permitRenewalsDTO.getRegNoOfBus(), "PR200", "O");

		if (checkTaskCodePR200IsValid == false) {

		} else if (checkTaskCodePR200IsValid == true) {

			permitRenewalsService.CopyTaskDetailsANDinsertTaskHistory(selectedApplicationNo,
					permitRenewalsDTO.getRegNoOfBus(), loginUser, "PR200");
			permitRenewalsService.updateTaskDetails(selectedApplicationNo, permitRenewalsDTO.getRegNoOfBus(), loginUser,
					"PR200", "C");
		}

	}

	public void generateVoucher() {
		CreatePaymentVoucherBckingBean c = new CreatePaymentVoucherBckingBean();

		try {
			sessionBackingBean.setApplicationNo(permitRenewalsDTO.getApplicationNo());
			sessionBackingBean.setTransactionDescription("RENEWAL");
			sessionBackingBean.setPermitNo(permitRenewalsDTO.getPermitNo());

			paymentVoucherDTO = new PaymentVoucherDTO();
			paymentVoucherDTO.setApplicationNo(permitRenewalsDTO.getApplicationNo());
			paymentVoucherDTO.setTransactionDescription("RENEWAL");
			paymentVoucherDTO.setPermitNo(permitRenewalsDTO.getPermitNo());
			RequestContext.getCurrentInstance().update("");
			RequestContext.getCurrentInstance().execute("PF('generateVoucher').show()");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void viewActButtonAction() {

		RequestContext.getCurrentInstance().execute("PF('viewChargeTypeDetails').show()");
		String selectedVoucherNo = selectedViewRow.getVoucherNo();

		chargeTypePaymentDataList = permitRenewalsService.getAllChargeTypeRecords(selectedVoucherNo);
		RequestContext.getCurrentInstance().update("frmViewChargeTypesTable");
	}

	public void viewVehicleInspectionAct() {

		String viewedApplicationNo = permitRenewalsDTO.getApplicationNo();
		String selectedPermitNo = permitRenewalsDTO.getPermitNo();
		String searchedQueueNum = permitRenewalsDTO.getQueueNo();
		String selectedApplicationNo = permitRenewalsDTO.getApplicationNo();

		sessionBackingBean.setViewedInspectionApplicationNo(viewedApplicationNo);
		sessionBackingBean.setViewedPermitNo(selectedPermitNo);
		sessionBackingBean.setSelectedQueueNo(searchedQueueNum);
		sessionBackingBean.setApplicationNo(selectedApplicationNo);
		sessionBackingBean.setCheckPermitIssueNewPermit(true);
		uploadPhotoMethod();
		loadValuesForViewInspection();
		RequestContext.getCurrentInstance().execute("PF('vehicleInspectionViewModeId').show()");
		RequestContext.getCurrentInstance().update("vehicleInspectionViewModeId");
		RequestContext.getCurrentInstance().update("vehicleInspectionViewModeId:frmUploadPhotos");
	}

	public void viewPublicComplain() {
		setSelectedComplaintDTO(new ComplaintRequestDTO());
		String viewedApplicationNo = permitRenewalsDTO.getApplicationNo();
		String selectedPermitNo = permitRenewalsDTO.getPermitNo();
		String searchedQueueNum = permitRenewalsDTO.getQueueNo();
		String selectedApplicationNo = permitRenewalsDTO.getApplicationNo();

		sessionBackingBean.setViewedInspectionApplicationNo(viewedApplicationNo);
		sessionBackingBean.setViewedPermitNo(selectedPermitNo);
		sessionBackingBean.setSelectedQueueNo(searchedQueueNum);
		sessionBackingBean.setApplicationNo(selectedApplicationNo);
		sessionBackingBean.setCheckPermitIssueNewPermit(true);

		loadValuesForViewPublicComplaint();
		RequestContext.getCurrentInstance().execute("PF('publicComplainModeId').show()");
		RequestContext.getCurrentInstance().update("publicComplainModeId");
	}

	public void viewInvestigation() {

		flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
		conditionList = null;
		documentList = null;
		loadValuesForViewInvestigation();

		RequestContext.getCurrentInstance().execute("PF('showPageDialog').show()");
		RequestContext.getCurrentInstance().update("showPageDialog");

	}

	public void viewGpsDetails() {
		setSelectedComplaintDTO(new ComplaintRequestDTO());
		String selectedPermitNo = permitRenewalsDTO.getPermitNo();

		sessionBackingBean.setViewedPermitNo(selectedPermitNo);
		loadValuesForViewGPS();

		RequestContext.getCurrentInstance().execute("PF('GPSModeId').show()");
		RequestContext.getCurrentInstance().update("GPSModeId");
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

	public void loadValuesForViewPublicComplaint() {
		setComplainDetViewList(
				manageInquiryService.getComplaintDetailsForPublicComplain(permitRenewalsDTO.getPermitNo()));
		
		Collections.sort(this.complainDetViewList, new Comparator<ComplaintRequestDTO>() {
            public int compare(ComplaintRequestDTO obj1, ComplaintRequestDTO obj2) {
                // Compare the "status" property of the objects
                return obj2.getProcessStatus().compareTo(obj1.getProcessStatus());
            }
        });

	}

	public void loadValuesForViewInvestigation() {
		setInvestigationDetViewList(manageInquiryService.getInvestigationDetails(permitRenewalsDTO.getPermitNo()));

	}

	public void loadValuesForViewGPS() {
		setSimRegistrationDTO(manageInquiryService.getGPSDetails(permitRenewalsDTO.getPermitNo()));
		if (simRegistrationDTO.getSimRegNo() != null) {
			setEmiDetViewList(manageInquiryService.getEmiDetails(simRegistrationDTO.getSimRegNo()));
		}
	}

	public void viewAction() {
		selectedComplaintDTO = manageInquiryService.getComplaintDetails(viewSelect.getComplaintNo(),
				viewSelect.getVehicleNo(), viewSelect.getPermitNo());
		RequestContext.getCurrentInstance().update("dlg-complaint");
		setRenderComplainDet(true);
	}

	private void uploadPhotoMethod() {
		RequestContext.getCurrentInstance().update("bla");
		RequestContext.getCurrentInstance().update("footerDiv");

		filteredValuesDTO = issuePermitService.getSelectedValuesForUploadPhotos(permitRenewalsDTO.getApplicationNo(),
				permitRenewalsDTO.getPermitNo());

		if (filteredValuesDTO.getVehicleOwner() != null) {
			permitRenewalsDTO.setFullName(filteredValuesDTO.getVehicleOwner());
		} else {

		}

		if (filteredValuesDTO.getBusRegNo() != null) {
			permitRenewalsDTO.setRegNoOfBus(filteredValuesDTO.getBusRegNo());
		} else {

		}

		try {

			FacesContext fcontext = FacesContext.getCurrentInstance();
			fcontext.getExternalContext().getSessionMap().put("VEHICLE_NO", permitRenewalsDTO.getRegNoOfBus());
			fcontext.getExternalContext().getSessionMap().put("APPLICATION_NO", permitRenewalsDTO.getApplicationNo());
			fcontext.getExternalContext().getSessionMap().put("OWNER_NAME", permitRenewalsDTO.getFullName());
			fcontext.getExternalContext().getSessionMap().put("VEHICLE_INSPECTION", "view");

		} catch (Exception e) {
			e.printStackTrace();
		}

		uploadImageDTO.setVehicleNo(permitRenewalsDTO.getRegNoOfBus());
		uploadImageDTO.setVehicleOwnerName(permitRenewalsDTO.getFullName());
		uploadImageDTO.setApplicationNo(permitRenewalsDTO.getApplicationNo());

		vehicleNo = uploadImageDTO.getVehicleNo();
		applicationNo = uploadImageDTO.getApplicationNo();
		vehicleOwnerName = uploadImageDTO.getVehicleOwnerName();

		if (vehicleNo != null && !vehicleNo.isEmpty() && !vehicleNo.trim().equalsIgnoreCase("") && applicationNo != null
				&& !applicationNo.isEmpty() && !applicationNo.trim().equalsIgnoreCase("")) {

			searchDataByVehicleNumber();

		}
	}

	private void loadValuesForViewInspection() {

		RequestContext.getCurrentInstance().update("bla");
		RequestContext.getCurrentInstance().update("footerDiv");

		viewedDetails = inspectionActionPointService
				.getRecordForCurrentApplicationNo(permitRenewalsDTO.getApplicationNo());
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
		vehicleDTO.setInspectedBy(viewedDetails.getInspectedBy());
		vehicleDTO.setInspectedDate(viewedDetails.getInspectedDate());

		dataListViewInspection = inspectionActionPointService
				.getAllInspectionRecordsDetails(permitRenewalsDTO.getApplicationNo());
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

		else {

		}
	}

	public void onRouteChange() {

		if (permitRenewalsDTO.getRouteNo() != null && !permitRenewalsDTO.getRouteNo().equals("")) {

			routeDTO = issuePermitService.getDetailsbyRouteNo(permitRenewalsDTO.getRouteNo());
			if (routeDTO.getOrigin() != null && !routeDTO.getOrigin().equals("")) {
				issuePermitDTO.setVia(routeDTO.getVia());
				issuePermitDTO.setDestination(routeDTO.getDestination());
				issuePermitDTO.setOrigin(routeDTO.getOrigin());
				issuePermitDTO.setDistance(routeDTO.getDistance());
			}

		} else {
			routeDTO = null;
		}
		routeFlag = false;
	}

	public void routeFlagListener() {
		String location1;
		String location2;
		if (routeFlag) {
			location1 = issuePermitDTO.getOrigin();
			location2 = issuePermitDTO.getDestination();
			issuePermitDTO.setOrigin(location2);
			issuePermitDTO.setDestination(location1);

		}
	}

	private void searchDataByVehicleNumber() {
		RequestContext.getCurrentInstance().update("bla");
		RequestContext.getCurrentInstance().update("footerDiv");

		try {

			uploadImageDTO = vehicleInspectionService.retrieveVehicleImageDataForVehicleNo(applicationNo);

			if (uploadImageDTO != null) {
				/** set owner image start **/
				if (uploadImageDTO.getVehicleOwnerPhotoPath() != null
						&& !uploadImageDTO.getVehicleOwnerPhotoPath().isEmpty()
						&& !uploadImageDTO.getVehicleOwnerPhotoPath().trim().equalsIgnoreCase("")) {
					BufferedImage ownerImage = ImageIO.read(new File(uploadImageDTO.getVehicleOwnerPhotoPath()));
					BufferedImage resized = resize(ownerImage, 600, 600);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					ImageIO.write(resized, "png", bos);
					permitOwnerFaceImage = bos.toByteArray();

					ownerImageUpload = true;
				}
				/** set owner image end **/

				/** set vehicle images start **/
				if (uploadImageDTO.getFirstVehiImagePath() != null && !uploadImageDTO.getFirstVehiImagePath().isEmpty()
						&& !uploadImageDTO.getFirstVehiImagePath().trim().equalsIgnoreCase("")) {
					BufferedImage vehicleImage = ImageIO.read(new File(uploadImageDTO.getFirstVehiImagePath()));
					BufferedImage resized = resize(vehicleImage, 500, 500);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					ImageIO.write(resized, "png", bos);
					firstVehicleImg = bos.toByteArray();
				}
				if (uploadImageDTO.getSecondVehiImagePath() != null
						&& !uploadImageDTO.getSecondVehiImagePath().isEmpty()
						&& !uploadImageDTO.getSecondVehiImagePath().trim().equalsIgnoreCase("")) {
					BufferedImage vehicleImage = ImageIO.read(new File(uploadImageDTO.getSecondVehiImagePath()));
					BufferedImage resized = resize(vehicleImage, 500, 500);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					ImageIO.write(resized, "png", bos);
					secondVehicleImg = bos.toByteArray();
				}
				if (uploadImageDTO.getThirdVehiImagePath() != null && !uploadImageDTO.getThirdVehiImagePath().isEmpty()
						&& !uploadImageDTO.getThirdVehiImagePath().trim().equalsIgnoreCase("")) {
					BufferedImage vehicleImage = ImageIO.read(new File(uploadImageDTO.getThirdVehiImagePath()));
					BufferedImage resized = resize(vehicleImage, 500, 500);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					ImageIO.write(resized, "png", bos);
					thirdVehicleImg = bos.toByteArray();
				}
				if (uploadImageDTO.getForthVehiImagePath() != null && !uploadImageDTO.getForthVehiImagePath().isEmpty()
						&& !uploadImageDTO.getForthVehiImagePath().trim().equalsIgnoreCase("")) {
					BufferedImage vehicleImage = ImageIO.read(new File(uploadImageDTO.getForthVehiImagePath()));
					BufferedImage resized = resize(vehicleImage, 500, 500);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					ImageIO.write(resized, "png", bos);
					fourthVehicleImg = bos.toByteArray();
				}
				if (uploadImageDTO.getFifthVehiImagePath() != null && !uploadImageDTO.getFifthVehiImagePath().isEmpty()
						&& !uploadImageDTO.getFifthVehiImagePath().trim().equalsIgnoreCase("")) {
					BufferedImage vehicleImage = ImageIO.read(new File(uploadImageDTO.getFifthVehiImagePath()));
					BufferedImage resized = resize(vehicleImage, 500, 500);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					ImageIO.write(resized, "png", bos);
					fifthVehicleImg = bos.toByteArray();
				}
				if (uploadImageDTO.getSixthVehiImagePath() != null && !uploadImageDTO.getSixthVehiImagePath().isEmpty()
						&& !uploadImageDTO.getSixthVehiImagePath().trim().equalsIgnoreCase("")) {
					BufferedImage vehicleImage = ImageIO.read(new File(uploadImageDTO.getSixthVehiImagePath()));
					BufferedImage resized = resize(vehicleImage, 500, 500);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					ImageIO.write(resized, "png", bos);
					sixthVehicleImg = bos.toByteArray();
				}

				/** set vehicle images end **/

			} else {

				uploadImageDTO = new UploadImageDTO();
			}

			uploadImageDTO.setVehicleNo(vehicleNo);
			uploadImageDTO.setApplicationNo(applicationNo);
			uploadImageDTO.setVehicleOwnerName(vehicleOwnerName);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}

		RequestContext.getCurrentInstance().update("vehicleInspectionViewModeId");
		RequestContext.getCurrentInstance().update(":frmvehicleInspectionViewModeId:frmUploadPhotos");

	}

	private static BufferedImage resize(BufferedImage img, int height, int width) {
		logger.info("BufferedImage resize(" + height + "," + width + ") start");

		try {

			Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			logger.info("Image tmp done");

			BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			logger.info("BufferedImage resized: " + width + "," + height);

			Graphics2D g2d = resized.createGraphics();
			logger.info("Graphics2D g2d done");

			g2d.drawImage(tmp, 0, 0, null);
			logger.info("g2d.drawImage done");

			g2d.dispose();
			logger.info(" g2d.dispose done");

			return resized;

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error upload photos: " + e.toString());
		}

		logger.info("BufferedImage resize(" + height + "," + width + ") end");
		return null;
	}

	// permit owner image
	public String getImageOfPermitOwner() {
		if (permitOwnerFaceImage != null) {
			return Base64.getEncoder().encodeToString(permitOwnerFaceImage);
		}
		return null;

	}

	// vehicle image 6
	public String getFirstImageOfVehicle() {
		if (firstVehicleImg != null) {
			return Base64.getEncoder().encodeToString(firstVehicleImg);
		}
		return null;

	}

	// vehicle image 2
	public String getSecondImageOfVehicle() {
		if (secondVehicleImg != null) {
			return Base64.getEncoder().encodeToString(secondVehicleImg);
		}
		return null;

	}

	// vehicle image 3
	public String getThirdImageOfVehicle() {
		if (thirdVehicleImg != null) {
			return Base64.getEncoder().encodeToString(thirdVehicleImg);
		}
		return null;

	}

	// vehicle image 4
	public String getFourthImageOfVehicle() {
		if (fourthVehicleImg != null) {
			return Base64.getEncoder().encodeToString(fourthVehicleImg);
		}
		return null;

	}

	// vehicle image 5
	public String getFifthImageOfVehicle() {
		if (fifthVehicleImg != null) {
			return Base64.getEncoder().encodeToString(fifthVehicleImg);
		}
		return null;

	}

	// vehicle image 6
	public String getSixthImageOfVehicle() {
		if (sixthVehicleImg != null) {
			return Base64.getEncoder().encodeToString(sixthVehicleImg);
		}
		return null;

	}

	public void loadSearchDetails() {

		String selectedApplicationNo = sessionBackingBean.getViewedInspectionApplicationNo();
		String selectedPermitNo = sessionBackingBean.getViewedPermitNo();
		String selectedQueueNo = sessionBackingBean.getSelectedQueueNo();
		setDisableSearchBtn(true);
		if ((selectedPermitNo.isEmpty() || selectedPermitNo.equals("") || selectedPermitNo == null)
				&& (selectedQueueNo.isEmpty() || selectedQueueNo.isEmpty() || selectedQueueNo == null)
				&& (selectedApplicationNo.isEmpty() || selectedApplicationNo.equals("")
						|| selectedApplicationNo == null)) {

			errorMsg = "Permit No or Queue No should be selected.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else if ((!selectedPermitNo.isEmpty() || !selectedPermitNo.equals("") || selectedPermitNo != null)
				&& (selectedQueueNo.isEmpty() || selectedQueueNo.equals("") || selectedQueueNo == null)
				&& (selectedApplicationNo.isEmpty() || selectedApplicationNo.equals("")
						|| selectedApplicationNo == null)) {

			loadSearchChecking();
		} else if ((selectedPermitNo.isEmpty() || selectedPermitNo.equals("") || selectedPermitNo == null)
				&& (!selectedQueueNo.isEmpty() || !selectedQueueNo.equals("") || selectedQueueNo != null)
				&& (selectedApplicationNo.isEmpty() || selectedApplicationNo.equals("")
						|| selectedApplicationNo == null)) {

			loadSearchChecking();
		} else if ((!selectedPermitNo.isEmpty() || !selectedPermitNo.equals("") || selectedPermitNo != null)
				&& (!selectedQueueNo.isEmpty() || !selectedQueueNo.equals("") || selectedQueueNo != null)
				&& (selectedApplicationNo.isEmpty() || selectedApplicationNo.equals("")
						|| selectedApplicationNo == null)) {

			loadSearchChecking();
		} else if ((selectedPermitNo.isEmpty() || selectedPermitNo.equals("") || selectedPermitNo == null)
				&& (selectedQueueNo.isEmpty() || selectedQueueNo.isEmpty() || selectedQueueNo == null)
				&& (!selectedApplicationNo.isEmpty() || !selectedApplicationNo.equals("")
						|| selectedApplicationNo != null)) {

			loadSearchChecking();
		} else if ((!selectedPermitNo.isEmpty() || !selectedPermitNo.equals("") || selectedPermitNo != null)
				&& (selectedQueueNo.isEmpty() || selectedQueueNo.isEmpty() || selectedQueueNo == null)
				&& (!selectedApplicationNo.isEmpty() || !selectedApplicationNo.equals("")
						|| selectedApplicationNo != null)) {

			loadSearchChecking();
		} else if ((selectedPermitNo.isEmpty() || selectedPermitNo.equals("") || selectedPermitNo == null)
				&& (!selectedQueueNo.isEmpty() || !selectedQueueNo.isEmpty() || selectedQueueNo != null)
				&& (!selectedApplicationNo.isEmpty() || !selectedApplicationNo.equals("")
						|| selectedApplicationNo != null)) {

			loadSearchChecking();
		} else if ((!selectedPermitNo.isEmpty() || !selectedPermitNo.equals("") || selectedPermitNo != null)
				&& (!selectedQueueNo.isEmpty() || !selectedQueueNo.isEmpty() || selectedQueueNo != null)
				&& (!selectedApplicationNo.isEmpty() || !selectedApplicationNo.equals("")
						|| selectedApplicationNo != null)) {

			loadSearchChecking();
		}
	}

	public void loadSearchChecking() {

		setShowThirdForm(true);
		queueNoChecked = true;
		setDisableCallNextBtn(true);
		setDisableSkipBtn(true);
		String previouesSpecialRemark = permitRenewalsService.getPreSpecialRemark(selectedPermitNo,
				permitRenewalsDTO.getRegNoOfBus());
		searchedDTO = permitRenewalsService.getSearchedDetailsWithPermitNoOrQueueNo(
				sessionBackingBean.getViewedPermitNo(), sessionBackingBean.getSelectedQueueNo(),
				sessionBackingBean.getApplicationNo(), "O");
		permitRenewalsDTO.setSeqno(searchedDTO.getSeqno());
		permitRenewalsDTO.setApplicationNo(searchedDTO.getApplicationNo());
		permitRenewalsDTO.setQueueNo(searchedDTO.getQueueNo());
		permitRenewalsDTO.setPermitNo(searchedDTO.getPermitNo());
		permitRenewalsDTO.setRegNoOfBus(searchedDTO.getRegNoOfBus());
		permitRenewalsDTO.setPreSpecialRemark(previouesSpecialRemark);
		permitRenewalsDTO.setSpecialRemark(searchedDTO.getSpecialRemark());
		permitRenewalsDTO.setServiceTypeCode(searchedDTO.getServiceTypeCode());
		permitRenewalsDTO.setRouteNo(searchedDTO.getRouteNo());
		permitRenewalsDTO.setPermitExpiryDate(searchedDTO.getPermitExpiryDate());
		permitRenewalsDTO.setRequestRenewPeriod(searchedDTO.getRequestRenewPeriod());
		permitRenewalsDTO.setServiceTypeDescription(searchedDTO.getServiceTypeDescription());
		permitRenewalsDTO.setPlaceOfOrginOfTheService(searchedDTO.getPlaceOfOrginOfTheService());
		permitRenewalsDTO.setPlaceOfDestination(searchedDTO.getPlaceOfDestination());
		permitRenewalsDTO.setPreferedLanguageCode(searchedDTO.getPreferedLanguageCode());
		permitRenewalsDTO.setTitleCode(searchedDTO.getTitleCode());
		permitRenewalsDTO.setFullName(searchedDTO.getFullName());
		permitRenewalsDTO.setFullNameSinhala(searchedDTO.getFullNameSinhala());
		permitRenewalsDTO.setFullNameTamil(searchedDTO.getFullNameTamil());
		permitRenewalsDTO.setNameWithInitials(searchedDTO.getNameWithInitials());
		permitRenewalsDTO.setNic(searchedDTO.getNic());
		permitRenewalsDTO.setGenderCode(searchedDTO.getGenderCode());
		permitRenewalsDTO.setDob(searchedDTO.getDob());
		permitRenewalsDTO.setMaterialStatusId(searchedDTO.getMaterialStatusId());
		permitRenewalsDTO.setTeleNo(searchedDTO.getTeleNo());
		permitRenewalsDTO.setMobileNo(searchedDTO.getMobileNo());
		permitRenewalsDTO.setProvinceCode(searchedDTO.getProvinceCode());
		permitRenewalsDTO.setDistrictCode(searchedDTO.getDistrictCode());
		permitRenewalsDTO.setDivisionalSecretariatDivision(searchedDTO.getDivisionalSecretariatDivision());
		permitRenewalsDTO.setAddressOne(searchedDTO.getAddressOne());
		permitRenewalsDTO.setAddressOneSinhala(searchedDTO.getAddressOneSinhala());
		permitRenewalsDTO.setAddressOneTamil(searchedDTO.getAddressOneTamil());
		permitRenewalsDTO.setAddressTwo(searchedDTO.getAddressTwo());
		permitRenewalsDTO.setAddressTwoSinhala(searchedDTO.getAddressTwoSinhala());
		permitRenewalsDTO.setAddressTwoTamil(searchedDTO.getAddressTwoTamil());
		permitRenewalsDTO.setCity(searchedDTO.getCity());
		permitRenewalsDTO.setCitySinhala(searchedDTO.getCitySinhala());
		permitRenewalsDTO.setCityTamil(searchedDTO.getCityTamil());
		selectedPermitNo = permitRenewalsDTO.getPermitNo();
		selectedApplicationNo = permitRenewalsDTO.getApplicationNo();
		selectedProvinceCode = permitRenewalsDTO.getProvinceCode();
		districtList = permitRenewalsService.getAllDistrictsForCurrentProvince(selectedProvinceCode);
		selectedDistricCode = permitRenewalsDTO.getDistrictCode();
		selectedMaterialStatus = permitRenewalsDTO.getMaterialStatusId();
		divisionSectionList = permitRenewalsService.getAllDivisionSections(selectedDistricCode);
		selectedDivisionSecCode = permitRenewalsDTO.getDivisionalSecretariatDivision();
		System.out
				.println("codes: division code " + selectedDivisionSecCode + " district code: " + selectedDistricCode);
		String languageDescription = permitRenewalsService
				.getLangaugeDescription(permitRenewalsDTO.getPreferedLanguageCode());
		permitRenewalsDTO.setPreferedLanguageDescription(languageDescription);
		String genderDescription = permitRenewalsService.getGenderDescription(permitRenewalsDTO.getGenderCode());
		permitRenewalsDTO.setGenderDescription(genderDescription);
		String titleDescription = permitRenewalsService.getTitleDescription(permitRenewalsDTO.getTitleCode());
		permitRenewalsDTO.setTitleDescription(titleDescription);
		permitRenewalsDTO.setValidToDate(searchedDTO.getValidToDate());
		permitRenewalsDTO.setFromToDate(searchedDTO.getFromToDate());
		permitRenewalsDTO.setBacklogAppValue(searchedDTO.getBacklogAppValue());
		permitRenewalsDTO.setCheckBacklogValue(searchedDTO.isCheckBacklogValue());
		permitRenewalsDTO.setRouteFlageValue(searchedDTO.getRouteFlageValue());
		permitRenewalsDTO.setRouteFlagChecked(searchedDTO.isRouteFlagChecked());
		if (permitRenewalsDTO.isRouteFlagChecked() == true) {
			permitRenewalsDTO.setPlaceOfOrginOfTheService(searchedDTO.getPlaceOfDestination());
			permitRenewalsDTO.setPlaceOfDestination(searchedDTO.getPlaceOfOrginOfTheService());
		} else if (permitRenewalsDTO.isRouteFlagChecked() == false) {
			permitRenewalsDTO.setPlaceOfOrginOfTheService(searchedDTO.getPlaceOfOrginOfTheService());
			permitRenewalsDTO.setPlaceOfDestination(searchedDTO.getPlaceOfDestination());

		}
		setShowbacklogvalueLoad(permitRenewalsDTO.isCheckBacklogValue());
		setShowbacklogvalue12(permitRenewalsDTO.isCheckBacklogValue());
		setDisabledPermitNoMenu(true);
		setDisabledApplicationNoMenu(true);
		setRequestNewPeriodReadOnly(true);
		setShowNewPermitDateInput(false);
		setDisabledSaveBtnOne(true);
	}

	public void goToDocumentsManagementBtn() {

		DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();

		try {
			ApplicationList = documentManagementService.RenewalApplicationList(permitRenewalsDTO.getPermitNo());

			RenewalApplicationList = documentManagementService.ConfirmRenewalApplicationList(ApplicationList);

			sessionBackingBean.setPermitRenewalPermitNo(permitRenewalsDTO.getPermitNo());
			sessionBackingBean.setPermitRenewalTranstractionTypeDescription("RENEWAL");
			sessionBackingBean.setApplicationNoForDoc(selectedApplicationNo);
			sessionBackingBean.setGoToPermitRenewalUploadDocPopUp(true);

			uploaddocumentManagementDTO.setUpload_Permit(permitRenewalsDTO.getPermitNo());
			uploaddocumentManagementDTO.setTransaction_Type("RENEWAL");
			mandatoryList = documentManagementService.mandatoryDocs("04", permitRenewalsDTO.getPermitNo());

			sessionBackingBean.optionalList = documentManagementService.optionalDocs("04",
					permitRenewalsDTO.getPermitNo());

			sessionBackingBean.newPermitMandatoryDocumentList = documentManagementService
					.newPermitMandatoryList(permitRenewalsDTO.getPermitNo());
			sessionBackingBean.newPermitOptionalDocumentList = documentManagementService
					.newPermitOptionalList(permitRenewalsDTO.getPermitNo());

			sessionBackingBean.permitRenewalMandatoryDocumentList = documentManagementService
					.permitRenewalMandatoryNewList(permitRenewalsDTO.getPermitNo(), RenewalApplicationList);
			sessionBackingBean.permitRenewalOptionalDocumentList = documentManagementService
					.permitRenewalOptionalNewList(permitRenewalsDTO.getPermitNo(), RenewalApplicationList);

			sessionBackingBean.backlogManagementOptionalDocumentList = documentManagementService
					.backlogManagementOptionalList(permitRenewalsDTO.getPermitNo());

			sessionBackingBean.amendmentToBusOwnerMandatoryDocumentList = documentManagementService
					.amendmentToBusOwnerMandatoryList(permitRenewalsDTO.getPermitNo());
			sessionBackingBean.amendmentToBusOwnerOptionalDocumentList = documentManagementService
					.amendmentToBusOwnerOptionalList(permitRenewalsDTO.getPermitNo());
			sessionBackingBean.amendmentToBusMandatoryDocumentList = documentManagementService
					.amendmentToBusMandatoryList(permitRenewalsDTO.getPermitNo());
			sessionBackingBean.amendmentToBusOptionalDocumentList = documentManagementService
					.amendmentToBusOptionalList(permitRenewalsDTO.getPermitNo());
			sessionBackingBean.amendmentToOwnerBusMandatoryDocumentList = documentManagementService
					.amendmentToOwnerBusMandatoryList(permitRenewalsDTO.getPermitNo());
			sessionBackingBean.amendmentToOwnerBusOptionalDocumentList = documentManagementService
					.amendmentToOwnerBusOptionalList(permitRenewalsDTO.getPermitNo());
			sessionBackingBean.amendmentToServiceBusMandatoryDocumentList = documentManagementService
					.amendmentToServiceBusMandatoryList(permitRenewalsDTO.getPermitNo());
			sessionBackingBean.amendmentToServiceBusOptionalDocumentList = documentManagementService
					.amendmentToServiceBusOptionalList(permitRenewalsDTO.getPermitNo());
			sessionBackingBean.amendmentToServiceMandatoryDocumentList = documentManagementService
					.amendmentToServiceMandatoryList(permitRenewalsDTO.getPermitNo());
			sessionBackingBean.amendmentToServiceOptionalDocumentList = documentManagementService
					.amendmentToServiceOptionalList(permitRenewalsDTO.getPermitNo());

			sessionBackingBean.tenderMandatoryDocumentList = documentManagementService
					.tenderMandatoryList(permitRenewalsDTO.getPermitNo());
			sessionBackingBean.tenderOptionalDocumentList = documentManagementService
					.tenderOptionalList(permitRenewalsDTO.getPermitNo());

			RequestContext.getCurrentInstance().execute("PF('uploadDocument').show()");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clearCheckDocumentsTable() {
		displayCheckDocumentDataList();

	}

	@SuppressWarnings("deprecation")
	public void onCounterSelect() {
		sessionBackingBean.setCounterId(commonDTO.getCounterId());
		permitRenewalsService = (PermitRenewalsService) SpringApplicationContex.getBean("permitRenewalsService");
		permitRenewalsService.counterStatus(sessionBackingBean.getCounterId(), sessionBackingBean.getLoginUser());
		sessionBackingBean.setCounterCheck(false);
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('selectCounterDialog').hide();");
	}

	public void handleClose() throws InterruptedException {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("/InfoNTC/pages/home/welcomePage.xhtml");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void closeUpadet() {

	}

	public void goBack() {

		String applicationNo = permitRenewalsDTO.getApplicationNo();
		String permitNo = permitRenewalsDTO.getPermitNo();
		activeTabIndex = 2;

		dataList = permitRenewalsService.getAllRecordsForDocumentsCheckings();

		for (int i = 0; i < dataList.size(); i++) {
			String currentDocCode = dataList.get(i).getDocumentCode();

			boolean isPhysicallyExit = permitRenewalsService.isPhysicallyExit(currentDocCode,
					permitRenewalsDTO.getApplicationNo(), permitRenewalsDTO.getPermitNo());

			boolean isMandatory = permitRenewalsService.isMandatory(currentDocCode,
					permitRenewalsDTO.getApplicationNo(), permitRenewalsDTO.getPermitNo());

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
		displayCheckDocumentDataList();
		RequestContext.getCurrentInstance().update("@form");

		RequestContext.getCurrentInstance().execute("PF('uploadDocumentsDialog').hide()");
		RequestContext.getCurrentInstance().update("abd:docPanelGridId:checkDocumentsTable");
	}

	public void checkQueueNoisValid() throws ParseException {

		boolean valueOne = true;

		valueOne = migratedService.findCancelledQueueNumber(permitRenewalsDTO.getQueueNo());

		if (valueOne == true) {

			searchChecking();
		} else {

			errorMsg = "You can not searched values for this Queue No.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public void ominiBusSave() throws ParseException {
		issuePermitDTO.setApplicationNo(selectedApplicationNo);
		issuePermitDTO.setBusRegNo(permitRenewalsDTO.getRegNoOfBus());
		issuePermitDTO.setPermitNo(selectedPermitNo);
		ominiBusDTO.setVehicleRegNo(permitRenewalsDTO.getRegNoOfBus());
		ominiBusDTO.setDateOfFirstReg(ominiBusDTO.getRegistrationDate());

		if (permitRenewalsDTO.getPermitExpiredValidToDateObj() != null) {
			if (permitRenewalsDTO.getRequestRenewPeriod() != 0) {
				if (!permitRenewalsDTO.getNewPermitExpirtDate().equals("")
						|| permitRenewalsDTO.getNewPermitExpirtDate() != null) {

					if (ominiBusDTO.getVehicleRegNo() != null && !ominiBusDTO.getVehicleRegNo().isEmpty()
							&& !ominiBusDTO.getVehicleRegNo().equalsIgnoreCase("")) {

						if (ominiBusDTO.getDateOfFirstReg() != null) {

							if (ominiBusDTO.getSeating() != null && !ominiBusDTO.getSeating().isEmpty()
									&& !ominiBusDTO.getSeating().equalsIgnoreCase("")) {

								if (ominiBusDTO.getManufactureDate() != null
										&& !ominiBusDTO.getManufactureDate().isEmpty()
										&& !ominiBusDTO.getManufactureDate().equalsIgnoreCase("")) {

									if (ominiBusDTO.getNoofDoors() != null && !ominiBusDTO.getNoofDoors().isEmpty()
											&& !ominiBusDTO.getNoofDoors().equalsIgnoreCase("")) {

										if (ominiBusDTO.getWeight() != null && !ominiBusDTO.getWeight().isEmpty()
												&& !ominiBusDTO.getWeight().equalsIgnoreCase("")) {

											if (ominiBusDTO.getFitnessCertiDate() != null) {

												if (ominiBusDTO.getInsuCompName() != null
														&& !ominiBusDTO.getInsuCompName().isEmpty()
														&& !ominiBusDTO.getInsuCompName().equalsIgnoreCase("")) {

													if (ominiBusDTO.getInsuExpDate() != null) {

														if (ominiBusDTO.getInsuCat() != null
																&& !ominiBusDTO.getInsuCat().isEmpty()
																&& !ominiBusDTO.getInsuCat().equalsIgnoreCase("")) {

															if (ominiBusDTO.getDateOfFirstReg() != null) {
																String format = "dd/MM/yyyy";
																SimpleDateFormat sdf = new SimpleDateFormat(format);

																String modifyBy = sessionBackingBean.loginUser;
																String permitDate = permitRenewalsDTO
																		.getPermitExpiryDate();
																Date permitdate1 = new SimpleDateFormat("dd/MM/yyyy")
																		.parse(permitDate);
																if (permitRenewalsDTO
																		.getNewPermitExpirtDate() != null) {
																	setCheckNewExpiryDateBoolean(true);
																}
																if (checkNewExpiryDateBoolean == true) {

																	permitRenewalsDTO.setModifyBy(modifyBy);
																	permitRenewalsDTO.setRequestRenewPeriod(
																			permitRenewalsDTO.getRequestRenewPeriod());
																	permitRenewalsDTO.setNewPermitExpirtDate(
																			permitRenewalsDTO.getNewPermitExpirtDate());
																	permitRenewalsDTO.setSpecialRemark(
																			permitRenewalsDTO.getSpecialRemark());
																	searchedCurrentSeqNoForApplicationTable = permitRenewalsService
																			.getCurrentSeqNoForPermitNo(
																					selectedPermitNo,
																					permitRenewalsDTO.getQueueNo(),
																					permitRenewalsDTO
																							.getApplicationNo());
																	permitRenewalsDTO.setSeqno(
																			searchedCurrentSeqNoForApplicationTable
																					.getSeqno());
																	permitRenewalsDTO.setApplicationNo(
																			searchedCurrentSeqNoForApplicationTable
																					.getApplicationNo());
																	permitRenewalsDTO.setPermitNo(selectedPermitNo);
																	permitRenewalsDTO.setFromToDate(
																			permitRenewalsDTO.getFromToDate());

																} else {

																	setErrorMsg(
																			"Valid Date should be after New Permit Expiry Date.");
																	RequestContext.getCurrentInstance()
																			.update("frmrequiredField");
																	RequestContext.getCurrentInstance()
																			.execute("PF('requiredField').show()");
																}

																if (ominiBusDTO.getSeq() != 0) {

																	// update
																	ominiBusDTO.setModifiedBy(
																			sessionBackingBean.getLoginUser());
																	ominiBusDTO
																			.setPermitNo(issuePermitDTO.getPermitNo());
																	ominiBusDTO.setApplicationNo(
																			issuePermitDTO.getApplicationNo());
																	ominiBusDTO.setVehicleRegNo(
																			issuePermitDTO.getBusRegNo());

																	/* Save OminiBus Data */
																	OminiBusHistoryDTO = historyService
																			.getOminiBusTableData(
																					ominiBusDTO.getApplicationNo(),
																					sessionBackingBean.getLoginUser());

																	/* Save Application Data */
																	applicationHistoryDTO = historyService
																			.getApplicationTableData(
																					permitRenewalsDTO
																							.getApplicationNo(),
																					sessionBackingBean.getLoginUser());
																	
																	int finalResult = permitRenewalsService.updatePermitRenewalRecordAllinOne(ominiBusDTO, permitRenewalsDTO);

																	if (finalResult == 0) {

																		activeTabIndex = 2;
																		RequestContext.getCurrentInstance()
																				.update("frmOminiBusInfo");
																		RequestContext.getCurrentInstance()
																				.update("frmsuccessSve");
																		setSucessMsg("Successfully Saved.");
																		RequestContext.getCurrentInstance()
																				.execute("PF('successSve').show()");
																		
																		historyService.insertApplicationAndOminiBusHistory(applicationHistoryDTO ,OminiBusHistoryDTO);

																		boolean checkTaskCodeInTaskDet = inspectionActionPointService
																				.isCheckedTaskCodeInTaskDetCompleted(
																						permitRenewalsDTO
																								.getApplicationNo(),
																						"PR200");
																		if (checkTaskCodeInTaskDet) {
																			taskDetWithAppDetDTO = new VehicleInspectionDTO();
																			taskDetWithAppDetDTO = inspectionActionPointService
																					.getDetailsWithModiFyDate(
																							permitRenewalsDTO
																									.getApplicationNo());
																			boolean checkValueWithSameTaskCode = inspectionActionPointService
																					.isCheckedSameTaskCodeForSelectedApp(
																							permitRenewalsDTO
																									.getApplicationNo(),
																							"PR200", "C");
																			if (checkValueWithSameTaskCode == true) {
																				int resultInTaskDetWithAppDet = inspectionActionPointService
																						.updateNewTaskDetWithAppDet(
																								taskDetWithAppDetDTO,
																								sessionBackingBean
																										.getLoginUser(),
																								permitRenewalsDTO
																										.getApplicationNo(),
																								"PR200", "C");
																			} else {
																				int resultInTaskDetWithAppDet = inspectionActionPointService
																						.insertNewTaskDetWithAppDet(
																								taskDetWithAppDetDTO,
																								sessionBackingBean
																										.getLoginUser(),
																								permitRenewalsDTO
																										.getApplicationNo(),
																								"PR200", "C");
																			}
																		} else {
																			taskDetWithAppDetDTO = new VehicleInspectionDTO();
																			taskDetWithAppDetDTO = inspectionActionPointService
																					.getDetailsWithModiFyDate(
																							permitRenewalsDTO
																									.getApplicationNo());
																			boolean checkValueWithSameTaskCode = inspectionActionPointService
																					.isCheckedSameTaskCodeForSelectedApp(
																							permitRenewalsDTO
																									.getApplicationNo(),
																							"PR200", "O");
																			if (checkValueWithSameTaskCode == true) {
																				int resultInTaskDetWithAppDet = inspectionActionPointService
																						.updateNewTaskDetWithAppDet(
																								taskDetWithAppDetDTO,
																								sessionBackingBean
																										.getLoginUser(),
																								permitRenewalsDTO
																										.getApplicationNo(),
																								"PR200", "O");
																			} else {
																				int resultInTaskDetWithAppDet = inspectionActionPointService
																						.insertNewTaskDetWithAppDet(
																								taskDetWithAppDetDTO,
																								sessionBackingBean
																										.getLoginUser(),
																								permitRenewalsDTO
																										.getApplicationNo(),
																								"PR200", "O");
																			}
																		}

																	} else {

																		setErrorMsg("Errors.");
																		RequestContext.getCurrentInstance()
																				.update("frmrequiredField");
																		RequestContext.getCurrentInstance()
																				.execute("PF('requiredField').show()");
																	}
																} else {
																	// Save
																	ominiBusDTO.setCreatedBy(
																			sessionBackingBean.getLoginUser());
																	ominiBusDTO
																			.setPermitNo(issuePermitDTO.getPermitNo());
																	ominiBusDTO.setApplicationNo(
																			issuePermitDTO.getApplicationNo());
																	ominiBusDTO.setVehicleRegNo(
																			issuePermitDTO.getBusRegNo());

																	/* Save OminiBus Data */
																	OminiBusHistoryDTO = historyService
																			.getOminiBusTableData(
																					ominiBusDTO.getApplicationNo(),
																					sessionBackingBean.getLoginUser());

																	/* Save Application Data */
																	applicationHistoryDTO = historyService
																			.getApplicationTableData(
																					permitRenewalsDTO
																							.getApplicationNo(),
																					sessionBackingBean.getLoginUser());

//																	int resultApplicationTable = permitRenewalsService
//																			.updatePermitRenewalRecord(
//																					permitRenewalsDTO);
//																	int result = permitRenewalsService
//																			.saveNewPermitOminiBus(ominiBusDTO);
																	
																	int finalResult = permitRenewalsService.savePermitRenewalRecordAllinOne(ominiBusDTO, permitRenewalsDTO);
																	
																	if (finalResult == 0) {

																		activeTabIndex = 2;

																		RequestContext.getCurrentInstance()
																				.update("frmOminiBusInfo");
																		RequestContext.getCurrentInstance()
																				.update("frmsuccessSve");
																		setSucessMsg("Successfully Saved.");
																		RequestContext.getCurrentInstance()
																				.execute("PF('successSve').show()");

																		/* Insert in to history tables */
//																		historyService.insertOminiBusHistoryData(OminiBusHistoryDTO);
//																		historyService.insertApplicationHistoryData(applicationHistoryDTO);
																		
																		historyService.insertApplicationAndOminiBusHistory(applicationHistoryDTO ,OminiBusHistoryDTO);

																		boolean checkTaskCodeInTaskDet = inspectionActionPointService
																				.isCheckedTaskCodeInTaskDetCompleted(
																						permitRenewalsDTO
																								.getApplicationNo(),
																						"PR200");
																		if (checkTaskCodeInTaskDet) {
																			taskDetWithAppDetDTO = new VehicleInspectionDTO();
																			taskDetWithAppDetDTO = inspectionActionPointService
																					.getDetailsWithModiFyDate(
																							permitRenewalsDTO
																									.getApplicationNo());
																			boolean checkValueWithSameTaskCode = inspectionActionPointService
																					.isCheckedSameTaskCodeForSelectedApp(
																							permitRenewalsDTO
																									.getApplicationNo(),
																							"PR200", "C");
																			if (checkValueWithSameTaskCode == true) {
																				int resultInTaskDetWithAppDet = inspectionActionPointService
																						.updateNewTaskDetWithAppDet(
																								taskDetWithAppDetDTO,
																								sessionBackingBean
																										.getLoginUser(),
																								permitRenewalsDTO
																										.getApplicationNo(),
																								"PR200", "C");
																			} else {
																				int resultInTaskDetWithAppDet = inspectionActionPointService
																						.insertNewTaskDetWithAppDet(
																								taskDetWithAppDetDTO,
																								sessionBackingBean
																										.getLoginUser(),
																								permitRenewalsDTO
																										.getApplicationNo(),
																								"PR200", "C");
																			}
																		} else {
																			taskDetWithAppDetDTO = new VehicleInspectionDTO();
																			taskDetWithAppDetDTO = inspectionActionPointService
																					.getDetailsWithModiFyDate(
																							permitRenewalsDTO
																									.getApplicationNo());
																			boolean checkValueWithSameTaskCode = inspectionActionPointService
																					.isCheckedSameTaskCodeForSelectedApp(
																							permitRenewalsDTO
																									.getApplicationNo(),
																							"PR200", "O");
																			if (checkValueWithSameTaskCode == true) {
																				int resultInTaskDetWithAppDet = inspectionActionPointService
																						.updateNewTaskDetWithAppDet(
																								taskDetWithAppDetDTO,
																								sessionBackingBean
																										.getLoginUser(),
																								permitRenewalsDTO
																										.getApplicationNo(),
																								"PR200", "O");
																			} else {
																				int resultInTaskDetWithAppDet = inspectionActionPointService
																						.insertNewTaskDetWithAppDet(
																								taskDetWithAppDetDTO,
																								sessionBackingBean
																										.getLoginUser(),
																								permitRenewalsDTO
																										.getApplicationNo(),
																								"PR200", "O");
																			}
																		}

																	} else {

																		setErrorMsg("Errors.");
																		RequestContext.getCurrentInstance()
																				.update("frmrequiredField");
																		RequestContext.getCurrentInstance()
																				.execute("PF('requiredField').show()");
																	}
																}
																// }

															} else {
																errorMsg = "Date of First Registration in Sri Lanka should be entered.";
																RequestContext.getCurrentInstance()
																		.update("frmrequiredField");
																RequestContext.getCurrentInstance()
																		.execute("PF('requiredField').show()");
															}

														} else {
															errorMsg = "Category of Insurance should be entered.";
															RequestContext.getCurrentInstance()
																	.update("frmrequiredField");
															RequestContext.getCurrentInstance()
																	.execute("PF('requiredField').show()");
														}

													} else {
														errorMsg = "Insurance Expiry Date should be entered.";
														RequestContext.getCurrentInstance().update("frmrequiredField");
														RequestContext.getCurrentInstance()
																.execute("PF('requiredField').show()");
													}

												} else {
													errorMsg = "Insurance Company/Corporation Name should be entered.";
													RequestContext.getCurrentInstance().update("frmrequiredField");
													RequestContext.getCurrentInstance()
															.execute("PF('requiredField').show()");
												}

											} else {
												errorMsg = "Fitness Certificate Expiry Date should be entered.";
												RequestContext.getCurrentInstance().update("frmrequiredField");
												RequestContext.getCurrentInstance()
														.execute("PF('requiredField').show()");
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
									errorMsg = "Production year should be entered.";
									RequestContext.getCurrentInstance().update("frmrequiredField");
									RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
								}

							} else {
								errorMsg = "Seating Capacity should be entered.";
								RequestContext.getCurrentInstance().update("frmrequiredField");
								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
							}
						} else {
							errorMsg = "Date of Registration should be selected.";
							RequestContext.getCurrentInstance().update("frmrequiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
						}

					} else {
						errorMsg = "Registration No. of the Bus should be entered.";
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
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

	/*
	 * GPS Integration 26-03-2020 Ranjaka Liyanage
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

		if (permitRenewalsDTO.getRegNoOfBus() != null) {
			busNo = permitRenewalsDTO.getRegNoOfBus();
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

	public PermitRenewalsService getPermitRenewalsService() {
		return permitRenewalsService;
	}

	public void setPermitRenewalsService(PermitRenewalsService permitRenewalsService) {
		this.permitRenewalsService = permitRenewalsService;
	}

	public String getSelectedPermitNo() {
		return selectedPermitNo;
	}

	public PaymentVoucherDTO getPaymentVoucherDTO() {
		return paymentVoucherDTO;
	}

	public void setPaymentVoucherDTO(PaymentVoucherDTO paymentVoucherDTO) {
		this.paymentVoucherDTO = paymentVoucherDTO;
	}

	public void setSelectedPermitNo(String selectedPermitNo) {
		this.selectedPermitNo = selectedPermitNo;
	}

	public PermitRenewalsDTO getPermitRenewalsDTO() {
		return permitRenewalsDTO;
	}

	public void setPermitRenewalsDTO(PermitRenewalsDTO permitRenewalsDTO) {
		this.permitRenewalsDTO = permitRenewalsDTO;
	}

	public List<PermitRenewalsDTO> getPermitNoList() {
		return permitNoList;
	}

	public void setPermitNoList(List<PermitRenewalsDTO> permitNoList) {
		this.permitNoList = permitNoList;
	}

	public QueueManagementService getQueueManagementService() {
		return queueManagementService;
	}

	public void setQueueManagementService(QueueManagementService queueManagementService) {
		this.queueManagementService = queueManagementService;
	}

	public PermitRenewalsDTO getSearchedDTO() {
		return searchedDTO;
	}

	public void setSearchedDTO(PermitRenewalsDTO searchedDTO) {
		this.searchedDTO = searchedDTO;
	}

	public String getCallingQueueNo() {
		return callingQueueNo;
	}

	public void setCallingQueueNo(String callingQueueNo) {
		this.callingQueueNo = callingQueueNo;
	}

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	public boolean isShowThirdForm() {
		return showThirdForm;
	}

	public void setShowThirdForm(boolean showThirdForm) {
		this.showThirdForm = showThirdForm;
	}

	public List<PermitRenewalsDTO> getProvinceList() {
		return provinceList;
	}

	public void setProvinceList(List<PermitRenewalsDTO> provinceList) {
		this.provinceList = provinceList;
	}

	public List<PermitRenewalsDTO> getDistrictList() {
		return districtList;
	}

	public void setDistrictList(List<PermitRenewalsDTO> districtList) {
		this.districtList = districtList;
	}

	public String getSelectedProvinceCode() {
		return selectedProvinceCode;
	}

	public void setSelectedProvinceCode(String selectedProvinceCode) {
		this.selectedProvinceCode = selectedProvinceCode;
	}

	public String getSelectedDistricCode() {
		return selectedDistricCode;
	}

	public void setSelectedDistricCode(String selectedDistricCode) {
		this.selectedDistricCode = selectedDistricCode;
	}

	public List<PermitRenewalsDTO> getMaterialStatusList() {
		return materialStatusList;
	}

	public void setMaterialStatusList(List<PermitRenewalsDTO> materialStatusList) {
		this.materialStatusList = materialStatusList;
	}

	public String getSelectedMaterialStatus() {
		return selectedMaterialStatus;
	}

	public void setSelectedMaterialStatus(String selectedMaterialStatus) {
		this.selectedMaterialStatus = selectedMaterialStatus;
	}

	public String getSelectedDivisionSecCode() {
		return selectedDivisionSecCode;
	}

	public void setSelectedDivisionSecCode(String selectedDivisionSecCode) {
		this.selectedDivisionSecCode = selectedDivisionSecCode;
	}

	public List<PermitRenewalsDTO> getDivisionSectionList() {
		return divisionSectionList;
	}

	public void setDivisionSectionList(List<PermitRenewalsDTO> divisionSectionList) {
		this.divisionSectionList = divisionSectionList;
	}

	public boolean isQueueNoChecked() {
		return queueNoChecked;
	}

	public void setQueueNoChecked(boolean queueNoChecked) {
		this.queueNoChecked = queueNoChecked;
	}

	public boolean isDisableCallNextBtn() {
		return disableCallNextBtn;
	}

	public void setDisableCallNextBtn(boolean disableCallNextBtn) {
		this.disableCallNextBtn = disableCallNextBtn;
	}

	public boolean isDisableSkipBtn() {
		return disableSkipBtn;
	}

	public void setDisableSkipBtn(boolean disableSkipBtn) {
		this.disableSkipBtn = disableSkipBtn;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getSucessMsg() {
		return sucessMsg;
	}

	public void setSucessMsg(String sucessMsg) {
		this.sucessMsg = sucessMsg;
	}

	public PermitRenewalsDTO getSearchedCurrentSeqNoForApplicationTable() {
		return searchedCurrentSeqNoForApplicationTable;
	}

	public void setSearchedCurrentSeqNoForApplicationTable(PermitRenewalsDTO searchedCurrentSeqNoForApplicationTable) {
		this.searchedCurrentSeqNoForApplicationTable = searchedCurrentSeqNoForApplicationTable;
	}

	public PermitRenewalsDTO getSearchedSeqNoForOwnerTable() {
		return searchedSeqNoForOwnerTable;
	}

	public void setSearchedSeqNoForOwnerTable(PermitRenewalsDTO searchedSeqNoForOwnerTable) {
		this.searchedSeqNoForOwnerTable = searchedSeqNoForOwnerTable;
	}

	public boolean isOpenFinalCheckListTab() {
		return openFinalCheckListTab;
	}

	public void setOpenFinalCheckListTab(boolean openFinalCheckListTab) {
		this.openFinalCheckListTab = openFinalCheckListTab;
	}

	public int getActiveTabIndex() {
		return activeTabIndex;
	}

	public void setActiveTabIndex(int activeTabIndex) {
		this.activeTabIndex = activeTabIndex;
	}

	public boolean isNumCheckTel() {
		return numCheckTel;
	}

	public void setNumCheckTel(boolean numCheckTel) {
		this.numCheckTel = numCheckTel;
	}

	public boolean isNumCheckMobile() {
		return numCheckMobile;
	}

	public void setNumCheckMobile(boolean numCheckMobile) {
		this.numCheckMobile = numCheckMobile;
	}

	public OminiBusDTO getInspectionDetails() {
		return inspectionDetails;
	}

	public void setInspectionDetails(OminiBusDTO inspectionDetails) {
		this.inspectionDetails = inspectionDetails;
	}

	public IssuePermitDTO getIssuePermitDTO() {
		return issuePermitDTO;
	}

	public void setIssuePermitDTO(IssuePermitDTO issuePermitDTO) {
		this.issuePermitDTO = issuePermitDTO;
	}

	public IssuePermitService getIssuePermitService() {
		return issuePermitService;
	}

	public void setIssuePermitService(IssuePermitService issuePermitService) {
		this.issuePermitService = issuePermitService;
	}

	public List<PermitRenewalsDTO> getDataList() {
		return dataList;
	}

	public void setDataList(List<PermitRenewalsDTO> dataList) {
		this.dataList = dataList;
	}

	public List<PermitRenewalsDTO> getCheckDataList() {
		return checkDataList;
	}

	public void setCheckDataList(List<PermitRenewalsDTO> checkDataList) {
		this.checkDataList = checkDataList;
	}

	public boolean isCheckExists() {
		return checkExists;
	}

	public void setCheckExists(boolean checkExists) {
		this.checkExists = checkExists;
	}

	public PermitRenewalsDTO getCheckAndDisplayRemarkValue() {
		return checkAndDisplayRemarkValue;
	}

	public void setCheckAndDisplayRemarkValue(PermitRenewalsDTO checkAndDisplayRemarkValue) {
		this.checkAndDisplayRemarkValue = checkAndDisplayRemarkValue;
	}

	public List<PermitRenewalsDTO> getPaymentHistoryDataList() {
		return paymentHistoryDataList;
	}

	public void setPaymentHistoryDataList(List<PermitRenewalsDTO> paymentHistoryDataList) {
		this.paymentHistoryDataList = paymentHistoryDataList;
	}

	public PermitRenewalsDTO getSelectedViewRow() {
		return selectedViewRow;
	}

	public void setSelectedViewRow(PermitRenewalsDTO selectedViewRow) {
		this.selectedViewRow = selectedViewRow;
	}

	public List<PermitRenewalsDTO> getChargeTypePaymentDataList() {
		return chargeTypePaymentDataList;
	}

	public void setChargeTypePaymentDataList(List<PermitRenewalsDTO> chargeTypePaymentDataList) {
		this.chargeTypePaymentDataList = chargeTypePaymentDataList;
	}

	public String getViewInspectionURL() {
		return viewInspectionURL;
	}

	public void setViewInspectionURL(String viewInspectionURL) {
		this.viewInspectionURL = viewInspectionURL;
	}

	public PermitRenewalsDTO getDetialsForSearchedQueueNo() {
		return detialsForSearchedQueueNo;
	}

	public void setDetialsForSearchedQueueNo(PermitRenewalsDTO detialsForSearchedQueueNo) {
		this.detialsForSearchedQueueNo = detialsForSearchedQueueNo;
	}

	public boolean isDisableSearchBtn() {
		return disableSearchBtn;
	}

	public void setDisableSearchBtn(boolean disableSearchBtn) {
		this.disableSearchBtn = disableSearchBtn;
	}

	public PermitRenewalsDTO getDetailsForSearchedPermitNo() {
		return detailsForSearchedPermitNo;
	}

	public void setDetailsForSearchedPermitNo(PermitRenewalsDTO detailsForSearchedPermitNo) {
		this.detailsForSearchedPermitNo = detailsForSearchedPermitNo;
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

	public String getStrQueueNo() {
		return strQueueNo;
	}

	public void setStrQueueNo(String strQueueNo) {
		this.strQueueNo = strQueueNo;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public String getSelectedApplicationNo() {
		return selectedApplicationNo;
	}

	public void setSelectedApplicationNo(String selectedApplicationNo) {
		this.selectedApplicationNo = selectedApplicationNo;
	}

	public List<PermitRenewalsDTO> getApplicationNoList() {
		return applicationNoList;
	}

	public void setApplicationNoList(List<PermitRenewalsDTO> applicationNoList) {
		this.applicationNoList = applicationNoList;
	}

	public PermitRenewalsDTO getDetailsForSearchedApplicationNo() {
		return detailsForSearchedApplicationNo;
	}

	public void setDetailsForSearchedApplicationNo(PermitRenewalsDTO detailsForSearchedApplicationNo) {
		this.detailsForSearchedApplicationNo = detailsForSearchedApplicationNo;
	}

	public boolean isCheckNewExpiryDateBoolean() {
		return checkNewExpiryDateBoolean;
	}

	public void setCheckNewExpiryDateBoolean(boolean checkNewExpiryDateBoolean) {
		this.checkNewExpiryDateBoolean = checkNewExpiryDateBoolean;
	}

	public List<PermitRenewalsDTO> getOtherOptionalDocumentsList() {
		return otherOptionalDocumentsList;
	}

	public void setOtherOptionalDocumentsList(List<PermitRenewalsDTO> otherOptionalDocumentsList) {
		this.otherOptionalDocumentsList = otherOptionalDocumentsList;
	}

	public boolean isDisabledPermitNoMenu() {
		return disabledPermitNoMenu;
	}

	public void setDisabledPermitNoMenu(boolean disabledPermitNoMenu) {
		this.disabledPermitNoMenu = disabledPermitNoMenu;
	}

	public boolean isDisabledApplicationNoMenu() {
		return disabledApplicationNoMenu;
	}

	public void setDisabledApplicationNoMenu(boolean disabledApplicationNoMenu) {
		this.disabledApplicationNoMenu = disabledApplicationNoMenu;
	}

	public boolean isShowbacklogvalue() {
		return showbacklogvalue;
	}

	public void setShowbacklogvalue(boolean showbacklogvalue) {
		this.showbacklogvalue = showbacklogvalue;
	}

	public List<PermitRenewalsDTO> getEditingApplNoList() {
		return editingApplNoList;
	}

	public void setEditingApplNoList(List<PermitRenewalsDTO> editingApplNoList) {
		this.editingApplNoList = editingApplNoList;
	}

	public List<PermitRenewalsDTO> getFilterPermitNoList() {
		return filterPermitNoList;
	}

	public void setFilterPermitNoList(List<PermitRenewalsDTO> filterPermitNoList) {
		this.filterPermitNoList = filterPermitNoList;
	}

	public PermitRenewalsDTO getApplicationNoForSearchedQueueNo() {
		return ApplicationNoForSearchedQueueNo;
	}

	public void setApplicationNoForSearchedQueueNo(PermitRenewalsDTO applicationNoForSearchedQueueNo) {
		ApplicationNoForSearchedQueueNo = applicationNoForSearchedQueueNo;
	}

	public boolean isShowbacklogvalueLoad() {
		return showbacklogvalueLoad;
	}

	public void setShowbacklogvalueLoad(boolean showbacklogvalueLoad) {
		this.showbacklogvalueLoad = showbacklogvalueLoad;
	}

	public boolean isShowbacklogvalue12() {
		return showbacklogvalue12;
	}

	public void setShowbacklogvalue12(boolean showbacklogvalue12) {
		this.showbacklogvalue12 = showbacklogvalue12;
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

	public boolean isDisabledSaveBtnOne() {
		return disabledSaveBtnOne;
	}

	public void setDisabledSaveBtnOne(boolean disabledSaveBtnOne) {
		this.disabledSaveBtnOne = disabledSaveBtnOne;
	}

	public boolean isCheckedGotoVehicleInspectionViewMode() {
		return isCheckedGotoVehicleInspectionViewMode;
	}

	public void setCheckedGotoVehicleInspectionViewMode(boolean isCheckedGotoVehicleInspectionViewMode) {
		this.isCheckedGotoVehicleInspectionViewMode = isCheckedGotoVehicleInspectionViewMode;
	}

	public String getStrBusRegNo() {
		return strBusRegNo;
	}

	public void setStrBusRegNo(String strBusRegNo) {
		this.strBusRegNo = strBusRegNo;
	}

	public OminiBusDTO getOminiBusDTO() {
		return ominiBusDTO;
	}

	public void setOminiBusDTO(OminiBusDTO ominiBusDTO) {
		this.ominiBusDTO = ominiBusDTO;
	}

	public IssuePermitDTO getFilteredValuesDTO() {
		return filteredValuesDTO;
	}

	public void setFilteredValuesDTO(IssuePermitDTO filteredValuesDTO) {
		this.filteredValuesDTO = filteredValuesDTO;
	}

	public UploadImageDTO getUploadImageDTO() {
		return uploadImageDTO;
	}

	public void setUploadImageDTO(UploadImageDTO uploadImageDTO) {
		this.uploadImageDTO = uploadImageDTO;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public String getVehicleOwnerName() {
		return vehicleOwnerName;
	}

	public void setVehicleOwnerName(String vehicleOwnerName) {
		this.vehicleOwnerName = vehicleOwnerName;
	}

	public Boolean getRouteFlag() {
		return routeFlag;
	}

	public void setRouteFlag(Boolean routeFlag) {
		this.routeFlag = routeFlag;
	}

	public VehicleInspectionDTO getVehicleDTO() {
		return vehicleDTO;
	}

	public void setVehicleDTO(VehicleInspectionDTO vehicleDTO) {
		this.vehicleDTO = vehicleDTO;
	}

	public VehicleInspectionDTO getViewedDetails() {
		return viewedDetails;
	}

	public void setViewedDetails(VehicleInspectionDTO viewedDetails) {
		this.viewedDetails = viewedDetails;
	}

	public InspectionActionPointService getInspectionActionPointService() {
		return inspectionActionPointService;
	}

	public void setInspectionActionPointService(InspectionActionPointService inspectionActionPointService) {
		this.inspectionActionPointService = inspectionActionPointService;
	}

	public List<VehicleInspectionDTO> getDataListViewInspection() {
		return dataListViewInspection;
	}

	public void setDataListViewInspection(List<VehicleInspectionDTO> dataListViewInspection) {
		this.dataListViewInspection = dataListViewInspection;
	}

	public VehicleInspectionService getVehicleInspectionService() {
		return vehicleInspectionService;
	}

	public void setVehicleInspectionService(VehicleInspectionService vehicleInspectionService) {
		this.vehicleInspectionService = vehicleInspectionService;
	}

	public RouteDTO getRouteDTO() {
		return routeDTO;
	}

	public void setRouteDTO(RouteDTO routeDTO) {
		this.routeDTO = routeDTO;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		PermitRenewalsBackingBean.logger = logger;
	}

	public byte[] getPermitOwnerFaceImage() {
		return permitOwnerFaceImage;
	}

	public void setPermitOwnerFaceImage(byte[] permitOwnerFaceImage) {
		this.permitOwnerFaceImage = permitOwnerFaceImage;
	}

	public byte[] getFirstVehicleImg() {
		return firstVehicleImg;
	}

	public void setFirstVehicleImg(byte[] firstVehicleImg) {
		this.firstVehicleImg = firstVehicleImg;
	}

	public byte[] getSecondVehicleImg() {
		return secondVehicleImg;
	}

	public void setSecondVehicleImg(byte[] secondVehicleImg) {
		this.secondVehicleImg = secondVehicleImg;
	}

	public byte[] getThirdVehicleImg() {
		return thirdVehicleImg;
	}

	public void setThirdVehicleImg(byte[] thirdVehicleImg) {
		this.thirdVehicleImg = thirdVehicleImg;
	}

	public byte[] getFourthVehicleImg() {
		return fourthVehicleImg;
	}

	public void setFourthVehicleImg(byte[] fourthVehicleImg) {
		this.fourthVehicleImg = fourthVehicleImg;
	}

	public byte[] getFifthVehicleImg() {
		return fifthVehicleImg;
	}

	public void setFifthVehicleImg(byte[] fifthVehicleImg) {
		this.fifthVehicleImg = fifthVehicleImg;
	}

	public byte[] getSixthVehicleImg() {
		return sixthVehicleImg;
	}

	public void setSixthVehicleImg(byte[] sixthVehicleImg) {
		this.sixthVehicleImg = sixthVehicleImg;
	}

	public boolean isDisabledTab() {
		return disabledTab;
	}

	public void setDisabledTab(boolean disabledTab) {
		this.disabledTab = disabledTab;
	}

	public boolean isDisabledReqPeriodInput() {
		return disabledReqPeriodInput;
	}

	public void setDisabledReqPeriodInput(boolean disabledReqPeriodInput) {
		this.disabledReqPeriodInput = disabledReqPeriodInput;
	}

	public List<DocumentManagementDTO> getApplicationList() {
		return ApplicationList;
	}

	public void setApplicationList(List<DocumentManagementDTO> applicationList) {
		ApplicationList = applicationList;
	}

	public List<DocumentManagementDTO> getRenewalApplicationList() {
		return RenewalApplicationList;
	}

	public void setRenewalApplicationList(List<DocumentManagementDTO> renewalApplicationList) {
		RenewalApplicationList = renewalApplicationList;
	}

	public boolean isOwnerImageUpload() {
		return ownerImageUpload;
	}

	public void setOwnerImageUpload(boolean ownerImageUpload) {
		this.ownerImageUpload = ownerImageUpload;
	}

	public PermitRenewalsDTO getRequestRenewDetSearchedDTO() {
		return requestRenewDetSearchedDTO;
	}

	public void setRequestRenewDetSearchedDTO(PermitRenewalsDTO requestRenewDetSearchedDTO) {
		this.requestRenewDetSearchedDTO = requestRenewDetSearchedDTO;
	}

	public PermitRenewalsDTO getVehicleOwnerHistoryDTO() {
		return vehicleOwnerHistoryDTO;
	}

	public void setVehicleOwnerHistoryDTO(PermitRenewalsDTO vehicleOwnerHistoryDTO) {
		this.vehicleOwnerHistoryDTO = vehicleOwnerHistoryDTO;
	}

	public OminiBusDTO getOminiBusHistoryDTO() {
		return OminiBusHistoryDTO;
	}

	public void setOminiBusHistoryDTO(OminiBusDTO ominiBusHistoryDTO) {
		OminiBusHistoryDTO = ominiBusHistoryDTO;
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

	public VehicleInspectionDTO getTaskDetWithAppDetDTO() {
		return taskDetWithAppDetDTO;
	}

	public void setTaskDetWithAppDetDTO(VehicleInspectionDTO taskDetWithAppDetDTO) {
		this.taskDetWithAppDetDTO = taskDetWithAppDetDTO;
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

	public ComplaintRequestDTO getSelectedComplaintDTO() {
		return selectedComplaintDTO;
	}

	public void setSelectedComplaintDTO(ComplaintRequestDTO selectedComplaintDTO) {
		this.selectedComplaintDTO = selectedComplaintDTO;
	}

	public ManageInquiryService getManageInquiryService() {
		return manageInquiryService;
	}

	public void setManageInquiryService(ManageInquiryService manageInquiryService) {
		this.manageInquiryService = manageInquiryService;
	}

	public ComplaintRequestDTO getViewSelect() {
		return viewSelect;
	}

	public void setViewSelect(ComplaintRequestDTO viewSelect) {
		this.viewSelect = viewSelect;
	}

	public FlyingManageInvestigationLogDTO getFlyingManageInvestigationLogDTO() {
		return flyingManageInvestigationLogDTO;
	}

	public void setFlyingManageInvestigationLogDTO(FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO) {
		this.flyingManageInvestigationLogDTO = flyingManageInvestigationLogDTO;
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

	public String getValidationMsg() {
		return validationMsg;
	}

	public void setValidationMsg(String validationMsg) {
		this.validationMsg = validationMsg;
	}

	public boolean isIncompleteInspection() {
		return isIncompleteInspection;
	}

	public void setIncompleteInspection(boolean isIncompleteInspection) {
		this.isIncompleteInspection = isIncompleteInspection;
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
