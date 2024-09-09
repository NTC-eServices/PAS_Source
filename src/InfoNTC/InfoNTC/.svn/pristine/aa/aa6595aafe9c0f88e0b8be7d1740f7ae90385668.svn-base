package lk.informatics.ntc.view.beans;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.CommonInquiryDTO;
import lk.informatics.ntc.model.dto.ComplaintActionDTO;
import lk.informatics.ntc.model.dto.IssuePermitDTO;
import lk.informatics.ntc.model.dto.ManageInvestigationDTO;
import lk.informatics.ntc.model.dto.PaymentVoucherDTO;
import lk.informatics.ntc.model.dto.PermitRenewalsDTO;
import lk.informatics.ntc.model.dto.RouteDTO;
import lk.informatics.ntc.model.dto.SisuSeriyaDTO;
import lk.informatics.ntc.model.dto.UploadImageDTO;
import lk.informatics.ntc.model.dto.VehicleInspectionDTO;
import lk.informatics.ntc.model.exception.ApplicationException;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.DriverConductorTrainingService;
import lk.informatics.ntc.model.service.GrievanceManagementDashboardService;
import lk.informatics.ntc.model.service.InspectionActionPointService;
import lk.informatics.ntc.model.service.IssuePermitService;
import lk.informatics.ntc.model.service.ManageInvestigationService;
import lk.informatics.ntc.model.service.PaymentVoucherService;
import lk.informatics.ntc.model.service.PermitRenewalsService;
import lk.informatics.ntc.model.service.SisuSariyaService;
import lk.informatics.ntc.model.service.TerminalManagementService;
import lk.informatics.ntc.model.service.VehicleInspectionService;
import lk.informatics.ntc.view.util.PropertyReader;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "inquiryDetails")
@ViewScoped
public class inquiryDetailsBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	public static Logger logger = Logger.getLogger("UploadPhotosBackingBean");

	// DTO
	private PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();
	private PermitRenewalsDTO searchDetailsForVehicleNoDTO = new PermitRenewalsDTO();
	private PermitRenewalsDTO newPermitDetailsDTO = new PermitRenewalsDTO();
	private PermitRenewalsDTO attorneyHolderDTO = new PermitRenewalsDTO();
	private PermitRenewalsDTO selectPemrmitHisDTO = new PermitRenewalsDTO();
	private PermitRenewalsDTO selectedPermitHistoryRowDTO = new PermitRenewalsDTO();
	private VehicleInspectionDTO viewedDetails;
	public VehicleInspectionDTO vehicleDTO = new VehicleInspectionDTO();
	private RouteDTO routeDTO;
	private IssuePermitDTO issuePermitDTO = new IssuePermitDTO();
	private IssuePermitDTO filteredValuesDTO;
	private UploadImageDTO uploadImageDTO;
	private PaymentVoucherDTO paymentVoucherDTO = new PaymentVoucherDTO();
	private PermitRenewalsDTO selectedViewRow;
	private SisuSeriyaDTO subsidyDTO = new SisuSeriyaDTO();	
	private ManageInvestigationDTO manageInvestigationDTO = new ManageInvestigationDTO();
	private CommonInquiryDTO commonInquiryDTO = new CommonInquiryDTO();

	// Service
	private PermitRenewalsService permitRenewalsService;
	private CommonService commonService;
	private InspectionActionPointService inspectionActionPointService;
	private IssuePermitService issuePermitService;
	private VehicleInspectionService vehicleInspectionService;
	public PaymentVoucherService paymentVoucherService;
	private SisuSariyaService sisuSariyaService;
	private ManageInvestigationService manageInvestigationService;
	private GrievanceManagementDashboardService grievanceManagementDashboardService;
	private TerminalManagementService terminalManagementService;
	private DriverConductorTrainingService driverConductorTrainingService;


	// List
	public List<PermitRenewalsDTO> vehicleNoList = new ArrayList<PermitRenewalsDTO>(0);
	public List<PermitRenewalsDTO> permitNoList = new ArrayList<PermitRenewalsDTO>(0);
	public List<PermitRenewalsDTO> selectedStatusList = new ArrayList<PermitRenewalsDTO>(0);
	public List<PermitRenewalsDTO> getPreAppNoForLatestInActiveStatusList = new ArrayList<PermitRenewalsDTO>(0);
	public List<PermitRenewalsDTO> dataList = new ArrayList<PermitRenewalsDTO>(0);
	private List<VehicleInspectionDTO> dataListViewInspection = new ArrayList<VehicleInspectionDTO>();
	private List<PaymentVoucherDTO> voucherDetails = new ArrayList<PaymentVoucherDTO>(0);
	private List<PaymentVoucherDTO> voucherDetForSelectedVoucherNoList = new ArrayList<PaymentVoucherDTO>(0);
	private List<CommonDTO> historyList;
	public List<PermitRenewalsDTO> paymentHistoryDataList = new ArrayList<PermitRenewalsDTO>(0);
	public List<PermitRenewalsDTO> chargeTypePaymentDataList = new ArrayList<PermitRenewalsDTO>(0);
	public List<PermitRenewalsDTO> oldHistoryList = new ArrayList<PermitRenewalsDTO>(0);
	public List<VehicleInspectionDTO> locationList = new ArrayList<VehicleInspectionDTO>(0);
	public List<VehicleInspectionDTO> servicelist = new ArrayList<VehicleInspectionDTO>(0);

	private List<SisuSeriyaDTO> operatorDepoNameList = new ArrayList<SisuSeriyaDTO>(0);
	private List<SisuSeriyaDTO> busNoList = new ArrayList<SisuSeriyaDTO>(0);
	private List<SisuSeriyaDTO> requestNoList = new ArrayList<SisuSeriyaDTO>(0);
	private List<SisuSeriyaDTO> serviceRefNoList = new ArrayList<SisuSeriyaDTO>(0);
	private List<SisuSeriyaDTO> serviceNoList = new ArrayList<SisuSeriyaDTO>(0);
	private List<SisuSeriyaDTO> sisuDataList = new ArrayList<SisuSeriyaDTO>(0);
	
	
	private List<CommonInquiryDTO> grievanceDataList = new ArrayList<CommonInquiryDTO>(0);
	private List<CommonInquiryDTO> grievanceBusNoList = new ArrayList<CommonInquiryDTO>(0);
	private List<CommonInquiryDTO> grievancePermitNoList = new ArrayList<CommonInquiryDTO>(0);
	private List<CommonInquiryDTO> grievanceComplaintNoList = new ArrayList<CommonInquiryDTO>(0);

	private List<ManageInvestigationDTO> investigationDataListForRefNo = new ArrayList<ManageInvestigationDTO>(0);
	private List<ManageInvestigationDTO> investigationDataListForOffenceName = new ArrayList<ManageInvestigationDTO>(0);
	private List<ManageInvestigationDTO> investigationRefNoList = new ArrayList<ManageInvestigationDTO>(0);
	private List<ManageInvestigationDTO> investigationBusNoList = new ArrayList<ManageInvestigationDTO>(0);
	private List<ManageInvestigationDTO> investigationPermitNoList = new ArrayList<ManageInvestigationDTO>(0);
	private List<ManageInvestigationDTO> investigationOffNameList = new ArrayList<ManageInvestigationDTO>(0);
	
	private List<CommonInquiryDTO> terminalTerminalDataList = new ArrayList<CommonInquiryDTO>(0);
	private List<CommonInquiryDTO> terminalVoucherDataList = new ArrayList<CommonInquiryDTO>(0);
	private List<CommonInquiryDTO> terminalPayDataList = new ArrayList<CommonInquiryDTO>(0);
	private List<CommonInquiryDTO> terminalBusDataList = new ArrayList<CommonInquiryDTO>(0);
	private List<CommonInquiryDTO> terminalBusNoList = new ArrayList<CommonInquiryDTO>(0);
	private List<CommonInquiryDTO> terminalVoucherNoList = new ArrayList<CommonInquiryDTO>(0);
	private List<CommonInquiryDTO> terminalTerminalNoList = new ArrayList<CommonInquiryDTO>(0);
	private List<CommonInquiryDTO> terminalPaymentTypeList = new ArrayList<CommonInquiryDTO>(0);
	
	private List<CommonInquiryDTO> driConApplicationDataList = new ArrayList<CommonInquiryDTO>(0);
	private List<CommonInquiryDTO> driConNICDataList = new ArrayList<CommonInquiryDTO>(0);
	private List<CommonInquiryDTO> driConIdDataList = new ArrayList<CommonInquiryDTO>(0);
	private List<CommonInquiryDTO> driConBusNoList = new ArrayList<CommonInquiryDTO>(0);
	private List<CommonInquiryDTO> driConNICDCIDDataList = new ArrayList<CommonInquiryDTO>(0);
	private List<CommonInquiryDTO> driConDateDataList = new ArrayList<CommonInquiryDTO>(0);

	private String selectedVehicleNo;
	private String selectedPermitNo;
	private int activeTabIndex;
	private boolean showActiveTab = false;
	private boolean showSisuSearchResultsPanel = false;
	private boolean showVouTable = false;
	private boolean showPayTable = false;
	private boolean showBusTable = false;
	private boolean disableDropBus = false;
	private boolean disableDropVou = false;

	boolean isSelectedVehicleNo = false;
	boolean isSelectedPermitNo = false;
	private boolean showNewPermitNoField = false;
	private boolean disableButton = false;
	private Boolean routeFlag;

	private String errorMsg;

	// upload photos method
	private String vehicleNo;
	private String applicationNo;
	private String vehicleOwnerName;
	private byte[] permitOwnerFaceImage;
	private byte[] firstVehicleImg;
	private byte[] secondVehicleImg;
	private byte[] thirdVehicleImg;
	private byte[] fourthVehicleImg;
	private byte[] fifthVehicleImg;
	private byte[] sixthVehicleImg;
	private boolean ownerImageUpload = false;

	private BigDecimal voutotalfee;

	private boolean disableAttorneyHolderBtn = false;
	private boolean showChargeTypeTable = false;
	private boolean showPermitRenewalBtn = false;
	private boolean showAmendmentsBtn = false;
	private boolean showInvTable = false;
	private boolean disableDropRef = false;
	private boolean disableDropOff = false;
	private boolean disableDCDrop = false;
	private boolean disableDCCal = false;
	private boolean disableVou = false;

	@PostConstruct
	public void init() {
		loadValues();
		routeFlag = false;
		selectedVehicleNo = null;
		selectedPermitNo = null;

		showPermitRenewalBtn = false;
		showAmendmentsBtn = false;
		if (!sessionBackingBean.approveURLStatus) {
			sessionBackingBean.setApproveURL(null);
			dataList = sessionBackingBean.getInquirySavedDataList();
			sessionBackingBean.setApproveURLStatus(true);
			RequestContext.getCurrentInstance().update("modelDataTable");
			disableButton = false;
			selectedVehicleNo = sessionBackingBean.getSelectedVehicleNoInquiryDet();
			selectedPermitNo = sessionBackingBean.getSelectedPermitNoInquiryDet();
			sessionBackingBean.setGoBackTOInquiryDetPg(false);
			isSelectedPermitNo = sessionBackingBean.isSearchedByPermitNo();
			isSelectedVehicleNo = sessionBackingBean.isSearchedByVehicleNo();
			search();
		}

	}

	private void loadValues() {
		vehicleNoList = permitRenewalsService.getAllVehicleNoList();
		permitNoList = permitRenewalsService.getAllPermitNoListInApplicationTable();

		// for subsidy
		operatorDepoNameList = sisuSariyaService.getOperatorDepoNameListForCommonInquiry(subsidyDTO);
		busNoList = sisuSariyaService.getBusNoListForCommonInquiry(subsidyDTO);
		requestNoList = sisuSariyaService.getRequestNoListForCommonInquiry(subsidyDTO);
		serviceRefNoList = sisuSariyaService.getServiceRefNoListForCommonInquiry(subsidyDTO);
		serviceNoList = sisuSariyaService.getServiceNoListForCommonInquiry(subsidyDTO);
		
		//for investigation
		investigationRefNoList = manageInvestigationService.getRefNoListForCommonInquiry();
		investigationBusNoList = manageInvestigationService.getBusNoListForCommonInquiry();
		investigationPermitNoList = manageInvestigationService.getPermitNoListForCommonInquiry();
		investigationOffNameList = manageInvestigationService.getOffNameListForCommonInquiry();
		
		//for grievance
		grievanceBusNoList = grievanceManagementDashboardService.getBusNoListForCommonInquiry();
		grievancePermitNoList = grievanceManagementDashboardService.getPermitNoListForCommonInquiry();
		grievanceComplaintNoList = grievanceManagementDashboardService.getComNoListForCommonInquiry();
		
		//for terminal
		terminalTerminalNoList = terminalManagementService.getTerminalNoListForCommonInquiry();
		terminalBusNoList = terminalManagementService.getBusNoListForCommonInquiry();
		terminalVoucherNoList = terminalManagementService.getVoucherNoListForCommonInquiry();
		terminalPaymentTypeList = terminalManagementService.getPaymentTypeListForCommonInquiry();
		
		//for driverConductor
		driConApplicationDataList = driverConductorTrainingService.getAppNoListForCommonInquiry();
		driConNICDataList = driverConductorTrainingService.getNICNoListForCommonInquiry();
		driConIdDataList = driverConductorTrainingService.getDriConNoListForCommonInquiry();
		driConBusNoList = driverConductorTrainingService.getBusNoListForCommonInquiry();
	}

	public inquiryDetailsBackingBean() {
		permitRenewalsService = (PermitRenewalsService) SpringApplicationContex.getBean("permitRenewalsService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		inspectionActionPointService = (InspectionActionPointService) SpringApplicationContex
				.getBean("inspectionActionPointService");
		issuePermitService = (IssuePermitService) SpringApplicationContex.getBean("issuePermitService");
		vehicleInspectionService = (VehicleInspectionService) SpringApplicationContex
				.getBean("vehicleInspectionService");
		paymentVoucherService = (PaymentVoucherService) SpringApplicationContex.getBean("paymentVoucherService");
		sisuSariyaService = (SisuSariyaService) SpringApplicationContex.getBean("sisuSariyaService");
		
		manageInvestigationService = (ManageInvestigationService) SpringApplicationContex.getBean("manageInvestigationService");
		grievanceManagementDashboardService = (GrievanceManagementDashboardService) SpringApplicationContex.getBean("grievanceManagementDashboardService");
		terminalManagementService = (TerminalManagementService) SpringApplicationContex.getBean("terminalManagementService");
		driverConductorTrainingService = (DriverConductorTrainingService) SpringApplicationContex.getBean("driverConductorTrainingService");
		
		viewedDetails = new VehicleInspectionDTO();
		filteredValuesDTO = new IssuePermitDTO();
		uploadImageDTO = new UploadImageDTO();
	}

	public void onVehicleNoChange() {
		isSelectedVehicleNo = true;
		setDefaultImages();
	}

	public void onPermitNoChange() {
		isSelectedPermitNo = true;
		setDefaultImages();
	}

	public void setDefaultImages() {
		Properties props;
		try {
			props = PropertyReader.loadPropertyFile();

			String ownerPath = props.getProperty("vehicle.inspection.upload.photo.owner.default.path");
			String busPath = props.getProperty("vehicle.inspection.upload.photo.bus.default.path");

			/** owner default images start **/
			BufferedImage ownerImage = ImageIO.read(new File(ownerPath));
			BufferedImage resized = resize(ownerImage, 600, 600);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(resized, "png", bos);
			permitOwnerFaceImage = bos.toByteArray();

			/** vehicle default images start **/
			BufferedImage vehicleImage = ImageIO.read(new File(busPath));
			BufferedImage resizedVehicleImg = resize(vehicleImage, 500, 500);
			ByteArrayOutputStream vehiclebos = new ByteArrayOutputStream();
			ImageIO.write(resizedVehicleImg, "png", vehiclebos);
			firstVehicleImg = vehiclebos.toByteArray();
			secondVehicleImg = vehiclebos.toByteArray();
			thirdVehicleImg = vehiclebos.toByteArray();
			fourthVehicleImg = vehiclebos.toByteArray();
			fifthVehicleImg = vehiclebos.toByteArray();
			sixthVehicleImg = vehiclebos.toByteArray();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void search() {

		activeTabIndex = 0;
		String needVehicleNo = null;
		String previousLatestInactiveAppNo = null;
		String selectedAppNo = null;
		String neededStatus = null;
		if (isSelectedVehicleNo) {
			if (!selectedVehicleNo.equals("")) {
				setShowActiveTab(true);
				selectedStatusList = new ArrayList<PermitRenewalsDTO>(0);
				selectedStatusList = permitRenewalsService.getStatusList(selectedVehicleNo);
				for (int i = 0; i < selectedStatusList.size(); i++) {
					if (selectedStatusList.get(i).getStatus().equals("A")) {
						needVehicleNo = selectedStatusList.get(i).getRegNoOfBus();
						selectedAppNo = selectedStatusList.get(i).getApplicationNo();
						neededStatus = "A";
						break;
					} else if (selectedStatusList.get(i).getStatus().equals("O")) {
						getPreAppNoForLatestInActiveStatusList = permitRenewalsService
								.getLatestInactiveAppNo(selectedVehicleNo);
						if (getPreAppNoForLatestInActiveStatusList.isEmpty()) {
							selectedAppNo = selectedStatusList.get(i).getApplicationNo();
							neededStatus = "O";
							needVehicleNo = selectedStatusList.get(i).getRegNoOfBus();
						} else {
							previousLatestInactiveAppNo = getPreAppNoForLatestInActiveStatusList
									.get(getPreAppNoForLatestInActiveStatusList.size() - 1).getLatestPreAppNo();
							selectedAppNo = previousLatestInactiveAppNo;
							neededStatus = "I";
							needVehicleNo = selectedStatusList.get(i).getRegNoOfBus();
						}
						break;
					} else if (selectedStatusList.get(i).getStatus().equals("P")) {
						getPreAppNoForLatestInActiveStatusList = permitRenewalsService
								.getLatestInactiveAppNo(selectedVehicleNo);
						if (getPreAppNoForLatestInActiveStatusList.isEmpty()) {
							selectedAppNo = selectedStatusList.get(i).getApplicationNo();
							neededStatus = "P";
							needVehicleNo = selectedStatusList.get(i).getRegNoOfBus();
						} else {
							previousLatestInactiveAppNo = getPreAppNoForLatestInActiveStatusList
									.get(getPreAppNoForLatestInActiveStatusList.size() - 1).getLatestPreAppNo();
							selectedAppNo = previousLatestInactiveAppNo;
							neededStatus = "I";
							needVehicleNo = selectedStatusList.get(i).getRegNoOfBus();
						}
						break;
					} else if (selectedStatusList.get(i).getStatus().equals("I")) {
						getPreAppNoForLatestInActiveStatusList = permitRenewalsService
								.getLatestInactiveAppNo(selectedVehicleNo);
						previousLatestInactiveAppNo = getPreAppNoForLatestInActiveStatusList
								.get(getPreAppNoForLatestInActiveStatusList.size() - 1).getLatestPreAppNo();
						selectedAppNo = previousLatestInactiveAppNo;
						neededStatus = "I";
						needVehicleNo = selectedStatusList.get(i).getRegNoOfBus();
						break;
					} else if (selectedStatusList.get(i).getStatus().equals("E")) {
						neededStatus = "E";
						needVehicleNo = selectedStatusList.get(i).getRegNoOfBus();
						selectedAppNo = selectedStatusList.get(i).getApplicationNo();
						break;
					}
				}
				searchDetailsForVehicleNoDTO = permitRenewalsService.getActiveDetailsForSelectedVehicleNo(needVehicleNo,
						neededStatus, selectedAppNo);
				setPermitRenewalsDTO(searchDetailsForVehicleNoDTO);
				if (permitRenewalsDTO.getDistrictCode() != null && permitRenewalsDTO.getProvinceCode() != null) {
					permitRenewalsDTO.setDistrictDescription(permitRenewalsService.getCurrentDistrictDes(
							permitRenewalsDTO.getProvinceCode(), permitRenewalsDTO.getDistrictCode()));
				} else {
					permitRenewalsDTO.setDistrictDescription("N/A");
				}

				if (permitRenewalsDTO.getDistrictCode() != null && permitRenewalsDTO.getProvinceCode() != null
						&& permitRenewalsDTO.getDivisionalSecretariatDivision() != null) {
					permitRenewalsDTO.setDivisionSectionDescription(permitRenewalsService.getCurrentDivSectionDes(
							permitRenewalsDTO.getProvinceCode(), permitRenewalsDTO.getDistrictCode(),
							permitRenewalsDTO.getDivisionalSecretariatDivision()));
				} else {
					permitRenewalsDTO.setDivisionSectionDescription("N/A");
				}

				if (permitRenewalsDTO.getGenderCode() != null) {
					permitRenewalsDTO.setGenderDescription(
							permitRenewalsService.getGenderDescription(permitRenewalsDTO.getGenderCode()));
				} else {
					permitRenewalsDTO.setGenderDescription("N/A");
				}

			} else {

			}
		} else if (isSelectedPermitNo) {
			if (!selectedPermitNo.equals("")) {
				setShowActiveTab(true);
				setShowNewPermitNoField(true);
				selectedStatusList = new ArrayList<PermitRenewalsDTO>(0);
				selectedStatusList = permitRenewalsService.getStatusListForSelectedPermitNo(selectedPermitNo);
				for (int i = 0; i < selectedStatusList.size(); i++) {
					if (selectedStatusList.get(i).getStatus().equals("A")) {
						needVehicleNo = selectedStatusList.get(i).getRegNoOfBus();
						selectedAppNo = selectedStatusList.get(i).getApplicationNo();
						neededStatus = "A";
						break;
					} else if (selectedStatusList.get(i).getStatus().equals("O")) {
						getPreAppNoForLatestInActiveStatusList = permitRenewalsService
								.getLatestInactiveAppNo(selectedStatusList.get(i).getRegNoOfBus());
						if (getPreAppNoForLatestInActiveStatusList.isEmpty()) {
							selectedAppNo = selectedStatusList.get(i).getApplicationNo();
							neededStatus = "O";
							needVehicleNo = selectedStatusList.get(i).getRegNoOfBus();
						} else {
							previousLatestInactiveAppNo = getPreAppNoForLatestInActiveStatusList.get(i).getLatestPreAppNo();
							selectedAppNo = previousLatestInactiveAppNo;
							neededStatus = "I";
							needVehicleNo = selectedStatusList.get(i).getRegNoOfBus();
						}
						break;
					} else if (selectedStatusList.get(i).getStatus().equals("P")) {
						getPreAppNoForLatestInActiveStatusList = permitRenewalsService
								.getLatestInactiveAppNo(selectedStatusList.get(i).getRegNoOfBus());
						if (getPreAppNoForLatestInActiveStatusList.isEmpty()) {
							selectedAppNo = selectedStatusList.get(i).getApplicationNo();
							neededStatus = "P";
							needVehicleNo = selectedStatusList.get(i).getRegNoOfBus();
						} else {
							previousLatestInactiveAppNo = getPreAppNoForLatestInActiveStatusList
									.get(getPreAppNoForLatestInActiveStatusList.size() - 1).getLatestPreAppNo();
							selectedAppNo = previousLatestInactiveAppNo;
							neededStatus = "I";
							needVehicleNo = selectedStatusList.get(i).getRegNoOfBus();
						}
						break;
					} else if (selectedStatusList.get(i).getStatus().equals("I")) {
						getPreAppNoForLatestInActiveStatusList = permitRenewalsService
								.getLatestInactiveAppNo(selectedStatusList.get(i).getRegNoOfBus());
						previousLatestInactiveAppNo = getPreAppNoForLatestInActiveStatusList
								.get(getPreAppNoForLatestInActiveStatusList.size() - 1).getLatestPreAppNo();
						selectedAppNo = previousLatestInactiveAppNo;
						neededStatus = "I";
						needVehicleNo = selectedStatusList.get(i).getRegNoOfBus();
						break;
					} else if (selectedStatusList.get(i).getStatus().equals("E")) {
						neededStatus = "E";
						needVehicleNo = selectedStatusList.get(i).getRegNoOfBus();
						selectedAppNo = selectedStatusList.get(i).getApplicationNo();
						break;
					}
				}
				searchDetailsForVehicleNoDTO = permitRenewalsService.getActiveDetailsForSelectedVehicleNo(needVehicleNo,
						neededStatus, selectedAppNo);
				setPermitRenewalsDTO(searchDetailsForVehicleNoDTO);
				newPermitDetailsDTO = permitRenewalsService.getNewPermitDetails(needVehicleNo,
						searchDetailsForVehicleNoDTO.getPermitNo());
				if (newPermitDetailsDTO.getNewPermitNo() != null) {
					permitRenewalsDTO.setNewPermitNo(newPermitDetailsDTO.getNewPermitNo());
				} else {
					permitRenewalsDTO.setNewPermitNo("N/A");
				}

				if (permitRenewalsDTO.getDistrictCode() != null && permitRenewalsDTO.getProvinceCode() != null) {
					permitRenewalsDTO.setDistrictDescription(permitRenewalsService.getCurrentDistrictDes(
							permitRenewalsDTO.getProvinceCode(), permitRenewalsDTO.getDistrictCode()));
				} else {
					permitRenewalsDTO.setDistrictDescription("N/A");
				}

				if (permitRenewalsDTO.getDistrictCode() != null && permitRenewalsDTO.getProvinceCode() != null
						&& permitRenewalsDTO.getDivisionalSecretariatDivision() != null) {
					permitRenewalsDTO.setDivisionSectionDescription(permitRenewalsService.getCurrentDivSectionDes(
							permitRenewalsDTO.getProvinceCode(), permitRenewalsDTO.getDistrictCode(),
							permitRenewalsDTO.getDivisionalSecretariatDivision()));
				} else {
					permitRenewalsDTO.setDivisionSectionDescription("N/A");
				}

			} else {

			}
		} else if (isSelectedPermitNo == false && isSelectedVehicleNo == false) {
			errorMsg = "Please select valid Vehicle No. or Permit No.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
		boolean hasActiveAttorneyHolder = permitRenewalsService
				.isCheckActiveAttorneyHolder(permitRenewalsDTO.getPermitNo());
		if (hasActiveAttorneyHolder == true) {
			setDisableAttorneyHolderBtn(false);
		} else {
			setDisableAttorneyHolderBtn(true);
		}
		loadSecondTab();

	}

	public void searchSisuSariya() {
		if (subsidyDTO.getServiceRefNo() != null && !subsidyDTO.getServiceRefNo().trim().equals("")) {
			sisuDataList = sisuSariyaService.searchDataForCommonInquiry(subsidyDTO);
			showSisuSearchResultsPanel = true;
		} else {
			errorMsg = "Please select a service reference no. ";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}
	
	public void searchInvestigation() {
		if (manageInvestigationDTO.getInvReferenceNo() != null && !manageInvestigationDTO.getInvReferenceNo().trim().equals("")) {
			investigationDataListForRefNo = manageInvestigationService.searchDataForCommonInquiry(manageInvestigationDTO.getInvReferenceNo(), manageInvestigationDTO.getVehicleNo(), manageInvestigationDTO.getPermitNo(), manageInvestigationDTO.getOffenceName());
			showSisuSearchResultsPanel = true;
			
			if(investigationDataListForRefNo.isEmpty()) {
				errorMsg = "Data Not Found.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		}
		else if(manageInvestigationDTO.getOffenceName() != null && !manageInvestigationDTO.getOffenceName().trim().equals("")){
			investigationDataListForOffenceName = manageInvestigationService.searchDataForCommonInquiry(manageInvestigationDTO.getInvReferenceNo(), manageInvestigationDTO.getVehicleNo(), manageInvestigationDTO.getPermitNo(), manageInvestigationDTO.getOffenceName());
			showInvTable = true;
			
			if(investigationDataListForOffenceName.isEmpty()) {
				errorMsg = "Data Not Found.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		}
		else {
			errorMsg = "Please Select Reference No Or Offence Name. ";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}
	
	public void searchGrievance() {
		if (commonInquiryDTO.getComplaintNo() != null && !commonInquiryDTO.getComplaintNo().trim().equals("")) {
			grievanceDataList = grievanceManagementDashboardService.searchDataForCommonInquiry(commonInquiryDTO.getComplaintNo(), commonInquiryDTO.getBusNo(), commonInquiryDTO.getPermitNo());
			showSisuSearchResultsPanel = true;
			
			if(grievanceDataList.isEmpty()) {
				errorMsg = "Data Not Found.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		}else {
			errorMsg = "Please Select ComplaintNo. ";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}
	
	public void searchTerminal() {
		if(commonInquiryDTO.getTerminalId() != null && !commonInquiryDTO.getTerminalId().trim().equals("")) {
			terminalTerminalDataList = terminalManagementService.searchTerminalDataForCommonInquiry(commonInquiryDTO.getTerminalId());
			disableDropOff = false;
			showSisuSearchResultsPanel = true;
			
			if(terminalTerminalDataList.isEmpty()) {
				errorMsg = "Data Not Found.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		}
		
		else if(commonInquiryDTO.getVoucherNo() != null && !commonInquiryDTO.getVoucherNo().trim().equals("")) {
			terminalVoucherDataList = terminalManagementService.searchVoucherDataForCommonInquiry(commonInquiryDTO.getVoucherNo());
			showVouTable = true;
			disableDropVou = false;
			
			if(terminalVoucherDataList.isEmpty()) {
				errorMsg = "Data Not Found.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		}
		else if(commonInquiryDTO.getPayType() != null && !commonInquiryDTO.getPayType().trim().equals("")) {
			terminalPayDataList = terminalManagementService.searchDataForCommonInquiry(commonInquiryDTO.getBusNo(),commonInquiryDTO.getPayType());
			showPayTable = true;
			
			if(terminalPayDataList.isEmpty()) {
				errorMsg = "Data Not Found.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		}

		else if(commonInquiryDTO.getBusNo() != null && !commonInquiryDTO.getBusNo().trim().equals("")) {
			terminalBusDataList = terminalManagementService.searchDataForCommonInquiry(commonInquiryDTO.getBusNo(),commonInquiryDTO.getPayType());
			showBusTable = true;
			
			if(terminalBusDataList.isEmpty()) {
				errorMsg = "Data Not Found.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		}else {
			errorMsg = "Please Select Field(s). ";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
		
	}
	
	public void searchDriverConductor() {
		if(commonInquiryDTO.getSheduleDate() != null ||commonInquiryDTO.getBusNo()!= null && !commonInquiryDTO.getBusNo().trim().equals("") || 
				commonInquiryDTO.getNicNo()!= null && !commonInquiryDTO.getNicNo().trim().equals("") ) {
			
			
			if(commonInquiryDTO.getSheduleDate() == null) {
				driConNICDCIDDataList = driverConductorTrainingService.searchDataForCommonInquiry(commonInquiryDTO.getNicNo(),commonInquiryDTO.getAppNo(),commonInquiryDTO.getBusNo(),commonInquiryDTO.getDriverConductorId());
				showSisuSearchResultsPanel = true;
				
				if(driConNICDCIDDataList.isEmpty()) {
					errorMsg = "Data Not Found.";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			}else {
				driConDateDataList = driverConductorTrainingService.searchDateDataForCommonInquiry(commonInquiryDTO.getSheduleDate());
				showVouTable = true;
				
				if(driConDateDataList.isEmpty()) {
					errorMsg = "Data Not Found.";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			}
			
			
		}else {
			errorMsg = "Please Select NIC No Or Date Or Bus No. ";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}
	
	
	public void onSelectDropDown() {
		if(manageInvestigationDTO.getInvReferenceNo() != null && !manageInvestigationDTO.getInvReferenceNo().trim().equals("")) {
			disableDropOff = true;
		}else if(manageInvestigationDTO.getOffenceName() != null && !manageInvestigationDTO.getOffenceName().trim().equals("")){
			disableDropRef = true;
		}else if(commonInquiryDTO.getTerminalId()!= null && !commonInquiryDTO.getTerminalId().trim().equals("")) {
			disableDropOff = true;
			disableDropBus = true;
			disableDropVou = true;
		}else if(commonInquiryDTO.getBusNo()!= null && !commonInquiryDTO.getBusNo().trim().equals("") || 
					commonInquiryDTO.getPayType()!= null && !commonInquiryDTO.getPayType().trim().equals("") ) {
			disableDropOff = true;
			disableDropVou = true;
		}else {
			disableDropBus = true;
			disableDropVou = true;
			disableDropOff = true;
		}
		
		
	}
	
	public void onSelectDropDownDC() {
		if(commonInquiryDTO.getSheduleDate() != null) {
			disableDCDrop = true;
		}else {
			disableDCCal = true;
		}
		
	}

	private void checkItisAmendmentsOrRenewalsApp(String selectedAppNo) {

		boolean isTaskCompleteInDetailsRenewal = false;
		boolean isTaskCompleteInHistoryRenewal = false;
		boolean isTaskCompleteInDetailsAmendments = false;
		boolean isTaskCompleteInHistoryAmendments = false;
		isTaskCompleteInDetailsRenewal = commonService.checkTaskDetails(selectedAppNo, "PR200");
		isTaskCompleteInHistoryRenewal = commonService.checkTaskHistory(selectedAppNo, "PR200");
		isTaskCompleteInDetailsAmendments = commonService.checkTaskDetails(selectPemrmitHisDTO.getApplicationNo(),
				"AM100");
		isTaskCompleteInHistoryAmendments = commonService.checkTaskHistory(selectPemrmitHisDTO.getApplicationNo(),
				"AM100");
		if ((isTaskCompleteInDetailsRenewal == true || isTaskCompleteInHistoryRenewal == true)) {
			setShowPermitRenewalBtn(true);
			setShowAmendmentsBtn(false);
		} else if (isTaskCompleteInDetailsAmendments == true || isTaskCompleteInHistoryAmendments == true) {
			setShowPermitRenewalBtn(false);
			setShowAmendmentsBtn(true);
		} else if ((isTaskCompleteInDetailsRenewal == true || isTaskCompleteInHistoryRenewal == true)
				&& (isTaskCompleteInDetailsAmendments == true || isTaskCompleteInHistoryAmendments == true)) {
			setShowPermitRenewalBtn(true);
			setShowAmendmentsBtn(true);
		} else {
			setShowPermitRenewalBtn(false);
			setShowAmendmentsBtn(false);
		}
	}

	private void loadSecondTab() {
		if (isSelectedVehicleNo) {
			if (!selectedVehicleNo.equals("")) {
				dataList = new ArrayList<PermitRenewalsDTO>(0);
				dataList = permitRenewalsService.getPermitHistoryListForSelectedVehicleNo(selectedVehicleNo);
			} else {

			}
		} else if (isSelectedPermitNo) {
			if (!selectedPermitNo.equals("")) {
				dataList = new ArrayList<PermitRenewalsDTO>(0);
				dataList = permitRenewalsService.getPermitHistoryListForSelectedPermitNo(selectedPermitNo);
			} else {

			}
		} else if (isSelectedPermitNo == false && isSelectedVehicleNo == false) {
			errorMsg = "Please select valid Vehicle No. or Permit No.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void clear() {
		setShowActiveTab(false);
		isSelectedPermitNo = false;
		isSelectedVehicleNo = false;
		permitRenewalsDTO = new PermitRenewalsDTO();
		selectedStatusList = new ArrayList<PermitRenewalsDTO>(0);
		getPreAppNoForLatestInActiveStatusList = new ArrayList<PermitRenewalsDTO>(0);
		setSelectedPermitNo(null);
		setSelectedVehicleNo(null);
		setShowNewPermitNoField(false);
		attorneyHolderDTO = new PermitRenewalsDTO();
		activeTabIndex = 0;
		sessionBackingBean.setSelectedVehicleNoInquiryDet(null);
		sessionBackingBean.setSelectedPermitNoInquiryDet(null);
		sessionBackingBean.setGoFromInquiryDetPg(false);
		sessionBackingBean.setGoBackTOInquiryDetPg(false);
		sessionBackingBean.setSearchedByPermitNo(false);
		sessionBackingBean.setSearchedByVehicleNo(false);
		voutotalfee = null;
		showPermitRenewalBtn = false;
		showAmendmentsBtn = false;
	}

	public void clearSisuSearch() {
		subsidyDTO = new SisuSeriyaDTO();
		showSisuSearchResultsPanel = false;
		// for subsidy
		operatorDepoNameList = sisuSariyaService.getOperatorDepoNameListForCommonInquiry(subsidyDTO);
		busNoList = sisuSariyaService.getBusNoListForCommonInquiry(subsidyDTO);
		requestNoList = sisuSariyaService.getRequestNoListForCommonInquiry(subsidyDTO);
		serviceRefNoList = sisuSariyaService.getServiceRefNoListForCommonInquiry(subsidyDTO);
		serviceNoList = sisuSariyaService.getServiceNoListForCommonInquiry(subsidyDTO);
	}
	
	public void clearInvestigationSearch() {
		showInvTable = false;
		disableDropRef = false;
		disableDropOff = false;
		showSisuSearchResultsPanel = false;
		
		manageInvestigationDTO = new ManageInvestigationDTO();
		
		investigationDataListForRefNo = new ArrayList<ManageInvestigationDTO>(0);
		investigationDataListForOffenceName = new ArrayList<ManageInvestigationDTO>(0);
		
	}
	
	public void clearGrievanceSearch() {
		showSisuSearchResultsPanel = false;
		
		commonInquiryDTO = new CommonInquiryDTO();
		
		grievanceDataList = new ArrayList<CommonInquiryDTO>(0);
	}

	public void clearTerminalSearch() {
		showSisuSearchResultsPanel = false;
		showVouTable = false;
		showPayTable = false;
		showBusTable = false;
		disableDropOff = false;
		disableDropBus = false;
		disableDropVou = false;
		
		terminalTerminalDataList = new ArrayList<CommonInquiryDTO>(0);
		terminalVoucherDataList = new ArrayList<CommonInquiryDTO>(0);
		terminalPayDataList = new ArrayList<CommonInquiryDTO>(0);
		terminalBusDataList = new ArrayList<CommonInquiryDTO>(0);

		commonInquiryDTO = new CommonInquiryDTO();
	}
	
	public void clearDriverConductorSearch() {
		commonInquiryDTO = new CommonInquiryDTO();
		
		driConNICDCIDDataList = new ArrayList<CommonInquiryDTO>(0);
		driConDateDataList = new ArrayList<CommonInquiryDTO>(0);
		
		showSisuSearchResultsPanel = false;
		disableDCDrop = false;
		disableDCCal = false;
		showVouTable = false;
	}
	
	public void onSubsidyBusNoChange() {
		operatorDepoNameList = sisuSariyaService.getOperatorDepoNameListForCommonInquiry(subsidyDTO);
		requestNoList = sisuSariyaService.getRequestNoListForCommonInquiry(subsidyDTO);
		serviceRefNoList = sisuSariyaService.getServiceRefNoListForCommonInquiry(subsidyDTO);
		serviceNoList = sisuSariyaService.getServiceNoListForCommonInquiry(subsidyDTO);
	}

	public void onSubsidyonRequestNoChangeoChange() {
		operatorDepoNameList = sisuSariyaService.getOperatorDepoNameListForCommonInquiry(subsidyDTO);
		busNoList = sisuSariyaService.getBusNoListForCommonInquiry(subsidyDTO);
		serviceRefNoList = sisuSariyaService.getServiceRefNoListForCommonInquiry(subsidyDTO);
		serviceNoList = sisuSariyaService.getServiceNoListForCommonInquiry(subsidyDTO);
	}

	public void onSubsidyOperatorNameDepoChange() {
		busNoList = sisuSariyaService.getBusNoListForCommonInquiry(subsidyDTO);
		requestNoList = sisuSariyaService.getRequestNoListForCommonInquiry(subsidyDTO);
		serviceRefNoList = sisuSariyaService.getServiceRefNoListForCommonInquiry(subsidyDTO);
		serviceNoList = sisuSariyaService.getServiceNoListForCommonInquiry(subsidyDTO);
	}

	public void onSubsidyReferenceNoChange() {
		operatorDepoNameList = sisuSariyaService.getOperatorDepoNameListForCommonInquiry(subsidyDTO);
		busNoList = sisuSariyaService.getBusNoListForCommonInquiry(subsidyDTO);
		requestNoList = sisuSariyaService.getRequestNoListForCommonInquiry(subsidyDTO);
		serviceNoList = sisuSariyaService.getServiceNoListForCommonInquiry(subsidyDTO);
	}

	public void onSubsidyServiceNoChange() {
		operatorDepoNameList = sisuSariyaService.getOperatorDepoNameListForCommonInquiry(subsidyDTO);
		busNoList = sisuSariyaService.getBusNoListForCommonInquiry(subsidyDTO);
		requestNoList = sisuSariyaService.getRequestNoListForCommonInquiry(subsidyDTO);
		serviceRefNoList = sisuSariyaService.getServiceRefNoListForCommonInquiry(subsidyDTO);
	}

	public void powerOfAttorneyAction() {
		RequestContext.getCurrentInstance().execute("PF('viewAttorneyHodlderId').show()");

		attorneyHolderDTO = permitRenewalsService.getAttorneyDetailsForSelectedVehicleNo(
				permitRenewalsDTO.getPermitNo(), permitRenewalsDTO.getRegNoOfBus(), permitRenewalsDTO.getStatus(),
				permitRenewalsDTO.getApplicationNo());
		if (attorneyHolderDTO.getTitleCode() != null) {
			attorneyHolderDTO
					.setTitleDescription(permitRenewalsService.getTitleDescription(attorneyHolderDTO.getTitleCode()));

		} else {
			attorneyHolderDTO.setTitleDescription("N/A");
		}

		if (attorneyHolderDTO.getGenderCode() != null) {
			attorneyHolderDTO.setGenderDescription(
					permitRenewalsService.getGenderDescription(attorneyHolderDTO.getGenderCode()));
		} else {
			attorneyHolderDTO.setGenderDescription("N/A");
		}

		RequestContext.getCurrentInstance().update("frmAttorneyHolder");
	}

	public void selectPermitHistoryRow() {

		checkItisAmendmentsOrRenewalsApp(selectPemrmitHisDTO.getApplicationNo());
	}

	public void viewAction() {

	}

	// btn panel methods
	public String viewInspection() {
		boolean isTaskCompleteInDetails = false;
		boolean isTaskCompleteInHistory = false;

		if (selectPemrmitHisDTO != null) {

			isTaskCompleteInDetails = commonService.checkTaskDetails(selectPemrmitHisDTO.getApplicationNo(), "PM100");
			isTaskCompleteInHistory = commonService.checkTaskHistory(selectPemrmitHisDTO.getApplicationNo(), "PM100");

			if (isTaskCompleteInDetails == true || isTaskCompleteInHistory == true) {

				HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
						.getRequest();

				sessionBackingBean.setPageMode("V");
				sessionBackingBean.setApproveURL(request.getRequestURL().toString());
				sessionBackingBean.setSearchURL(null);
				sessionBackingBean.setApproveURLStatus(true);
				sessionBackingBean.setApplicationNo(selectPemrmitHisDTO.getApplicationNo());
				sessionBackingBean.setBusRegNo(selectPemrmitHisDTO.getRegNoOfBus());
				sessionBackingBean.setPermitNo(selectPemrmitHisDTO.getPermitNo());
				sessionBackingBean.inquirySavedDataList = dataList;
				sessionBackingBean.setViewedInspectionApplicationNo(selectPemrmitHisDTO.getApplicationNo());
				sessionBackingBean.isClicked = true;
				sessionBackingBean.setSelectedOptionType("VIEW");
				sessionBackingBean.setVehicleInspectionMood(true);
				// add new sessions values
				sessionBackingBean.setSelectedVehicleNoInquiryDet(selectedVehicleNo);
				sessionBackingBean.setSelectedPermitNoInquiryDet(selectedPermitNo);
				sessionBackingBean.setGoFromInquiryDetPg(true);
				sessionBackingBean.setGoBackTOInquiryDetPg(false);
				sessionBackingBean.setSearchedByPermitNo(isSelectedPermitNo);
				sessionBackingBean.setSearchedByVehicleNo(isSelectedVehicleNo);

				return "/pages/vehicleInspectionSet/vehicleInspectionInfoViewMode.xhtml#!";

			} else {
				errorMsg = "Should be completed Vehicle Inspection task.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				return null;
			}
		} else {
			errorMsg = "Please select a row.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

			return null;
		}

	}

	public void viewVehicleInspectionAct() {

		boolean isTaskCompleteInDetails = false;
		boolean isTaskCompleteInHistory = false;

		if (selectPemrmitHisDTO != null) {

			if (selectPemrmitHisDTO.getStatus().equalsIgnoreCase("G")) {
				// if other inspection
				servicelist = vehicleInspectionService.servicetypedropdown();
				locationList = vehicleInspectionService.getInspectionLocationList();

				vehicleDTO = vehicleInspectionService.getVehicleInformation(null,
						selectPemrmitHisDTO.getApplicationNo(), true);
				uploadPhotoMethod();

				if (vehicleDTO != null) {
					onRouteChange();
					dataListViewInspection = vehicleInspectionService.Gridview(vehicleDTO);
					if (dataListViewInspection.isEmpty()) {
						dataListViewInspection = vehicleInspectionService.getActiveActionPointData();
					}
				}

				RequestContext.getCurrentInstance().execute("PF('otherInspectionView').show()");
				RequestContext.getCurrentInstance().update("otherInspectionView");
				RequestContext.getCurrentInstance().update("otherInspectionView:frmUploadPhotos");
			} else {
				// if normal inspection
				isTaskCompleteInDetails = commonService.checkTaskDetails(selectPemrmitHisDTO.getApplicationNo(),
						"PM100");
				isTaskCompleteInHistory = commonService.checkTaskHistory(selectPemrmitHisDTO.getApplicationNo(),
						"PM100");

				// if normal inspection skipped
				boolean isTaskCompleteInDetailsForSkipped = commonService
						.checkTaskDetails(selectPemrmitHisDTO.getApplicationNo(), "PM101");
				boolean isTaskCompleteInHistoryForSkipped = commonService
						.checkTaskHistory(selectPemrmitHisDTO.getApplicationNo(), "PM101");

				if (isTaskCompleteInDetails == true || isTaskCompleteInHistory == true
						|| isTaskCompleteInDetailsForSkipped == true || isTaskCompleteInHistoryForSkipped == true) {

					String viewedApplicationNo = selectPemrmitHisDTO.getApplicationNo();
					String selectedPermitNo = selectPemrmitHisDTO.getPermitNo();

					String selectedApplicationNo = selectPemrmitHisDTO.getApplicationNo();
					locationList = vehicleInspectionService.getInspectionLocationList();

					sessionBackingBean.setViewedInspectionApplicationNo(viewedApplicationNo);
					sessionBackingBean.setViewedPermitNo(selectedPermitNo);

					sessionBackingBean.setApplicationNo(selectedApplicationNo);
					sessionBackingBean.setCheckPermitIssueNewPermit(true);
					uploadPhotoMethod();
					loadValuesForViewInspection();
					RequestContext.getCurrentInstance().execute("PF('vehicleInspectionViewModeId').show()");
					RequestContext.getCurrentInstance().update("vehicleInspectionViewModeId");
					RequestContext.getCurrentInstance().update("vehicleInspectionViewModeId:frmUploadPhotos");

				} else {

					errorMsg = "No Vehicle Inspection record.";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			}

		} else {
			errorMsg = "Please select a row.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	private void uploadPhotoMethod() {
		RequestContext.getCurrentInstance().update("bla");
		RequestContext.getCurrentInstance().update("footerDiv");

		filteredValuesDTO = issuePermitService.getSelectedValuesForUploadPhotos(selectPemrmitHisDTO.getApplicationNo(),
				selectPemrmitHisDTO.getPermitNo());

		if (filteredValuesDTO.getVehicleOwner() != null) {
			selectPemrmitHisDTO.setFullName(filteredValuesDTO.getVehicleOwner());
		}

		if (filteredValuesDTO.getBusRegNo() != null) {
			selectPemrmitHisDTO.setRegNoOfBus(filteredValuesDTO.getBusRegNo());
		}

		try {

			FacesContext fcontext = FacesContext.getCurrentInstance();
			fcontext.getExternalContext().getSessionMap().put("VEHICLE_NO", permitRenewalsDTO.getRegNoOfBus());
			fcontext.getExternalContext().getSessionMap().put("APPLICATION_NO", selectPemrmitHisDTO.getApplicationNo());
			fcontext.getExternalContext().getSessionMap().put("OWNER_NAME", permitRenewalsDTO.getFullName());
			fcontext.getExternalContext().getSessionMap().put("VEHICLE_INSPECTION", "view");

		} catch (Exception e) {
			e.printStackTrace();
		}

		uploadImageDTO.setVehicleNo(selectPemrmitHisDTO.getRegNoOfBus());
		uploadImageDTO.setVehicleOwnerName(selectPemrmitHisDTO.getFullName());
		uploadImageDTO.setApplicationNo(selectPemrmitHisDTO.getApplicationNo());

		vehicleNo = uploadImageDTO.getVehicleNo();
		applicationNo = uploadImageDTO.getApplicationNo();
		vehicleOwnerName = uploadImageDTO.getVehicleOwnerName();

		if (vehicleNo != null && !vehicleNo.isEmpty() && !vehicleNo.trim().equalsIgnoreCase("") && applicationNo != null
				&& !applicationNo.isEmpty() && !applicationNo.trim().equalsIgnoreCase("")) {

			searchDataByVehicleNumber();

		}
	}

	private void searchDataByVehicleNumber() {
		RequestContext.getCurrentInstance().update("bla");
		RequestContext.getCurrentInstance().update("footerDiv");

		try {

			uploadImageDTO = new UploadImageDTO();
			uploadImageDTO = vehicleInspectionService.retrieveVehicleImageDataForVehicleNo(applicationNo);

			try {
				Properties props = PropertyReader.loadPropertyFile();
				String ownerPath = props.getProperty("vehicle.inspection.upload.photo.owner.default.path");
				String busPath = props.getProperty("vehicle.inspection.upload.photo.bus.default.path");

				if (uploadImageDTO != null) {
					/** set owner image start **/
					if (uploadImageDTO.getVehicleOwnerPhotoPath() != null
							&& !uploadImageDTO.getVehicleOwnerPhotoPath().isEmpty()
							&& !uploadImageDTO.getVehicleOwnerPhotoPath().trim().equalsIgnoreCase("")) {
						BufferedImage ownerImage = ImageIO.read(new File(uploadImageDTO.getVehicleOwnerPhotoPath()));
						// if image is corrupted display the default image
						if (ownerImage != null) {
							BufferedImage resized = resize(ownerImage, 600, 600);
							ByteArrayOutputStream bos = new ByteArrayOutputStream();
							ImageIO.write(resized, "png", bos);
							permitOwnerFaceImage = bos.toByteArray();

							ownerImageUpload = true;
						} else {
							BufferedImage ownerImageDefault = ImageIO.read(new File(ownerPath));
							BufferedImage resized = resize(ownerImageDefault, 600, 600);
							ByteArrayOutputStream bos = new ByteArrayOutputStream();
							ImageIO.write(resized, "png", bos);
							permitOwnerFaceImage = bos.toByteArray();
						}
					} else {
						BufferedImage ownerImage = ImageIO.read(new File(ownerPath));
						BufferedImage resized = resize(ownerImage, 600, 600);
						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						ImageIO.write(resized, "png", bos);
						permitOwnerFaceImage = bos.toByteArray();
					}

					/** set owner image end **/

					/** set vehicle images start **/
					if (uploadImageDTO.getFirstVehiImagePath() != null
							&& !uploadImageDTO.getFirstVehiImagePath().isEmpty()
							&& !uploadImageDTO.getFirstVehiImagePath().trim().equalsIgnoreCase("")) {
						BufferedImage vehicleImage = ImageIO.read(new File(uploadImageDTO.getFirstVehiImagePath()));
						BufferedImage resized = resize(vehicleImage, 500, 500);
						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						ImageIO.write(resized, "png", bos);
						firstVehicleImg = bos.toByteArray();
					} else {
						BufferedImage vehicleImage = ImageIO.read(new File(busPath));
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
					} else {
						BufferedImage vehicleImage = ImageIO.read(new File(busPath));
						BufferedImage resized = resize(vehicleImage, 500, 500);
						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						ImageIO.write(resized, "png", bos);
						secondVehicleImg = bos.toByteArray();
					}

					if (uploadImageDTO.getThirdVehiImagePath() != null
							&& !uploadImageDTO.getThirdVehiImagePath().isEmpty()
							&& !uploadImageDTO.getThirdVehiImagePath().trim().equalsIgnoreCase("")) {
						BufferedImage vehicleImage = ImageIO.read(new File(uploadImageDTO.getThirdVehiImagePath()));
						BufferedImage resized = resize(vehicleImage, 500, 500);
						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						ImageIO.write(resized, "png", bos);
						thirdVehicleImg = bos.toByteArray();
					} else {
						BufferedImage vehicleImage = ImageIO.read(new File(busPath));
						BufferedImage resized = resize(vehicleImage, 500, 500);
						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						ImageIO.write(resized, "png", bos);
						thirdVehicleImg = bos.toByteArray();
					}

					if (uploadImageDTO.getForthVehiImagePath() != null
							&& !uploadImageDTO.getForthVehiImagePath().isEmpty()
							&& !uploadImageDTO.getForthVehiImagePath().trim().equalsIgnoreCase("")) {
						BufferedImage vehicleImage = ImageIO.read(new File(uploadImageDTO.getForthVehiImagePath()));
						BufferedImage resized = resize(vehicleImage, 500, 500);
						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						ImageIO.write(resized, "png", bos);
						fourthVehicleImg = bos.toByteArray();
					} else {
						BufferedImage vehicleImage = ImageIO.read(new File(busPath));
						BufferedImage resized = resize(vehicleImage, 500, 500);
						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						ImageIO.write(resized, "png", bos);
						fourthVehicleImg = bos.toByteArray();
					}

					if (uploadImageDTO.getFifthVehiImagePath() != null
							&& !uploadImageDTO.getFifthVehiImagePath().isEmpty()
							&& !uploadImageDTO.getFifthVehiImagePath().trim().equalsIgnoreCase("")) {
						BufferedImage vehicleImage = ImageIO.read(new File(uploadImageDTO.getFifthVehiImagePath()));
						BufferedImage resized = resize(vehicleImage, 500, 500);
						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						ImageIO.write(resized, "png", bos);
						fifthVehicleImg = bos.toByteArray();
					} else {
						BufferedImage vehicleImage = ImageIO.read(new File(busPath));
						BufferedImage resized = resize(vehicleImage, 500, 500);
						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						ImageIO.write(resized, "png", bos);
						fifthVehicleImg = bos.toByteArray();
					}

					if (uploadImageDTO.getSixthVehiImagePath() != null
							&& !uploadImageDTO.getSixthVehiImagePath().isEmpty()
							&& !uploadImageDTO.getSixthVehiImagePath().trim().equalsIgnoreCase("")) {
						BufferedImage vehicleImage = ImageIO.read(new File(uploadImageDTO.getSixthVehiImagePath()));
						BufferedImage resized = resize(vehicleImage, 500, 500);
						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						ImageIO.write(resized, "png", bos);
						sixthVehicleImg = bos.toByteArray();
					} else {
						BufferedImage vehicleImage = ImageIO.read(new File(busPath));
						BufferedImage resized = resize(vehicleImage, 500, 500);
						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						ImageIO.write(resized, "png", bos);
						sixthVehicleImg = bos.toByteArray();
					}

					/** set vehicle images end **/

				} else {
					BufferedImage ownerImage = ImageIO.read(new File(ownerPath));
					BufferedImage resized = resize(ownerImage, 600, 600);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					ImageIO.write(resized, "png", bos);
					permitOwnerFaceImage = bos.toByteArray();

					/** vehicle default images start **/
					BufferedImage vehicleImage = ImageIO.read(new File(busPath));
					BufferedImage resizedVehicleImg = resize(vehicleImage, 500, 500);
					ByteArrayOutputStream vehiclebos = new ByteArrayOutputStream();
					ImageIO.write(resizedVehicleImg, "png", vehiclebos);
					firstVehicleImg = vehiclebos.toByteArray();
					secondVehicleImg = vehiclebos.toByteArray();
					thirdVehicleImg = vehiclebos.toByteArray();
					fourthVehicleImg = vehiclebos.toByteArray();
					fifthVehicleImg = vehiclebos.toByteArray();
					sixthVehicleImg = vehiclebos.toByteArray();

					uploadImageDTO = new UploadImageDTO();
				}

				uploadImageDTO.setVehicleNo(vehicleNo);
				uploadImageDTO.setApplicationNo(applicationNo);
				uploadImageDTO.setVehicleOwnerName(vehicleOwnerName);
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}

		RequestContext.getCurrentInstance().update("vehicleInspectionViewModeId");
		RequestContext.getCurrentInstance().update(":frmvehicleInspectionViewModeId:frmUploadPhotos");
	}

	private BufferedImage resize(BufferedImage img, int height, int width) {
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

	private void loadValuesForViewInspection() {
		RequestContext.getCurrentInstance().update("bla");
		RequestContext.getCurrentInstance().update("footerDiv");

		viewedDetails = inspectionActionPointService
				.getRecordForCurrentApplicationNo(selectPemrmitHisDTO.getApplicationNo());
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
		vehicleDTO.setInspectionTypeDes(viewedDetails.getInspectionTypeDes());
		vehicleDTO.setInspectionStatusCode(viewedDetails.getInspectionStatusCode());
		vehicleDTO.setInspecLocationCode(viewedDetails.getInspecLocationCode());

		dataListViewInspection = inspectionActionPointService
				.getAllInspectionRecordsDetails(selectPemrmitHisDTO.getApplicationNo());
		FacesContext fcontext = FacesContext.getCurrentInstance();
		fcontext.getExternalContext().getSessionMap().put("QUEUE_NO", vehicleDTO.getQueueNo());
		onRouteChange();
		if (vehicleDTO.getRouteFlag() != null && !vehicleDTO.getRouteFlag().trim().equals("")
				&& vehicleDTO.getRouteFlag().equalsIgnoreCase("Y")) {
			routeFlag = true;
			routeFlagListener();

		} else if (vehicleDTO.getRouteFlag() != null && !vehicleDTO.getRouteFlag().trim().equals("")
				&& vehicleDTO.getRouteFlag().equalsIgnoreCase("N")) {
			// routeFlag = true;
			// routeFlagListener();
		}

		else {
			// routeFlag = true;
			// routeFlagListener();
		}
	}

	private void loadValuesForOtherInspectionView() {

	}

	private void routeFlagListener() {
		String location1;
		String location2;
		if (routeFlag) {
			location1 = issuePermitDTO.getOrigin();
			location2 = issuePermitDTO.getDestination();
			issuePermitDTO.setOrigin(location2);
			issuePermitDTO.setDestination(location1);

		}
	}

	private void onRouteChange() {
		if (vehicleDTO.getRouteNo() != null && !vehicleDTO.getRouteNo().equals("")) {

			routeDTO = issuePermitService.getDetailsbyRouteNo(vehicleDTO.getRouteNo());
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

	public String viewIssueNewPermit() {

		boolean isTaskCompleteInDetails = false;
		boolean isTaskCompleteInHistory = false;

		if (selectPemrmitHisDTO != null) {

			isTaskCompleteInDetails = commonService.checkTaskDetails(selectPemrmitHisDTO.getApplicationNo(), "PM200");
			isTaskCompleteInHistory = commonService.checkTaskHistory(selectPemrmitHisDTO.getApplicationNo(), "PM200");

			if (isTaskCompleteInDetails == true || isTaskCompleteInHistory == true) {

				HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
						.getRequest();
				sessionBackingBean.setPageMode("V");
				sessionBackingBean.setApproveURL(request.getRequestURL().toString());
				sessionBackingBean.setSearchURL(null);
				sessionBackingBean.setApproveURLStatus(true);
				sessionBackingBean.setApplicationNo(selectPemrmitHisDTO.getApplicationNo());
				sessionBackingBean.setBusRegNo(selectPemrmitHisDTO.getRegNoOfBus());
				sessionBackingBean.setPermitNo(selectPemrmitHisDTO.getPermitNo());
				sessionBackingBean.inquirySavedDataList = dataList;
				sessionBackingBean.isClicked = true;

				// add new sessions values
				sessionBackingBean.setSelectedVehicleNoInquiryDet(selectedVehicleNo);
				sessionBackingBean.setSelectedPermitNoInquiryDet(selectedPermitNo);
				sessionBackingBean.setGoFromInquiryDetPg(true);
				sessionBackingBean.setGoBackTOInquiryDetPg(false);
				sessionBackingBean.setSearchedByPermitNo(isSelectedPermitNo);
				sessionBackingBean.setSearchedByVehicleNo(isSelectedVehicleNo);

				return "/pages/issueNewPermit/viewNewPermit.xhtml#!";

			} else {
				errorMsg = "No Issue New Permit record.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				return null;
			}

		} else {
			errorMsg = "Please select a row.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			return null;
		}
	}

	public String viewPermitRenewal() {

		boolean isTaskCompleteInDetails = false;
		boolean isTaskCompleteInHistory = false;

		if (selectPemrmitHisDTO != null) {

			isTaskCompleteInDetails = commonService.checkTaskDetails(selectPemrmitHisDTO.getApplicationNo(), "PR200");
			isTaskCompleteInHistory = commonService.checkTaskHistory(selectPemrmitHisDTO.getApplicationNo(), "PR200");

			if (isTaskCompleteInDetails == true || isTaskCompleteInHistory == true) {

				HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
						.getRequest();
				sessionBackingBean.setPageMode("V");
				sessionBackingBean.setApproveURL(request.getRequestURL().toString());
				sessionBackingBean.setSearchURL(null);
				sessionBackingBean.setApproveURLStatus(true);
				sessionBackingBean.setApplicationNo(selectPemrmitHisDTO.getApplicationNo());
				sessionBackingBean.setBusRegNo(selectPemrmitHisDTO.getRegNoOfBus());
				sessionBackingBean.setPermitNo(selectPemrmitHisDTO.getPermitNo());
				sessionBackingBean.inquirySavedDataList = dataList;
				sessionBackingBean.isClicked = true;

				// add new sessions values
				sessionBackingBean.setSelectedVehicleNoInquiryDet(selectedVehicleNo);
				sessionBackingBean.setSelectedPermitNoInquiryDet(selectedPermitNo);
				sessionBackingBean.setGoFromInquiryDetPg(true);
				sessionBackingBean.setGoBackTOInquiryDetPg(false);
				sessionBackingBean.setSearchedByPermitNo(isSelectedPermitNo);
				sessionBackingBean.setSearchedByVehicleNo(isSelectedVehicleNo);

				return "/pages/viewPermitRenewals/viewPermitRenewalsNew.xhtml#!";

			} else {

				errorMsg = "No Premit Renewal record.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				return null;
			}

		} else {
			errorMsg = "Please select a row.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			return null;
		}

	}

	public void viewPayments() {
		if (selectPemrmitHisDTO != null) {
			setShowChargeTypeTable(false);

			paymentHistoryDataList = permitRenewalsService.getAllPaymentHistoryList(
					selectPemrmitHisDTO.getApplicationNo(), selectPemrmitHisDTO.getPermitNo());

			if (!paymentHistoryDataList.isEmpty()) {

				for (int i = 0; i < paymentHistoryDataList.size(); i++) {

					String txnDate = paymentHistoryDataList.get(i).getTxnDate();
					String[] parts = txnDate.split(" ");
					String part1 = parts[0];
					String part2 = parts[1];

					paymentHistoryDataList.get(i).setDisplayTxnDate(part1);

				}

				RequestContext.getCurrentInstance().execute("PF('viewPaymentVoucherId').show()");
			} else {

				errorMsg = "No Generate Voucher record.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

		} else {
			errorMsg = "Please select a row.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public void viewActButtonAction() {

		chargeTypePaymentDataList = new ArrayList<PermitRenewalsDTO>(0);

		String selectedVoucherNo = selectedViewRow.getVoucherNo();

		chargeTypePaymentDataList = permitRenewalsService.getAllChargeTypeRecords(selectedVoucherNo);
		if (chargeTypePaymentDataList.size() > 0) {
			voutotalfee = paymentVoucherService.getTotAmtforVoucher(selectedVoucherNo);

		} else {
			errorMsg = "Search No. does not have voucher details.";
			sessionBackingBean.showMessage("Error", errorMsg, "ERROR_DIALOG");
			sessionBackingBean.isClicked = false;
		}
		setShowChargeTypeTable(true);
		RequestContext.getCurrentInstance().update("frmViewChargeTypesTable");
	}

	private void searchVoucherDetails() {
		if (paymentVoucherDTO.getVoucherNo() != null && !paymentVoucherDTO.getVoucherNo().isEmpty()) {
			voucherDetForSelectedVoucherNoList = new ArrayList<PaymentVoucherDTO>(0);
			voucherDetForSelectedVoucherNoList = paymentVoucherService
					.getVoucherPaymentDet(paymentVoucherDTO.getVoucherNo());
			voucherDetails.addAll(voucherDetForSelectedVoucherNoList);

			if (voucherDetails.size() > 0) {
				voutotalfee = paymentVoucherService.getTotAmtforVoucher(paymentVoucherDTO.getVoucherNo());

			} else {
				errorMsg = "Search No. does not have voucher details.";
				sessionBackingBean.showMessage("Error", errorMsg, "ERROR_DIALOG");
				sessionBackingBean.isClicked = false;
			}
			sessionBackingBean.isClicked = false;

		} else {
			errorMsg = "Voucher No. should be entered.";
			sessionBackingBean.showMessage("Error", errorMsg, "ERROR_DIALOG");
			sessionBackingBean.isClicked = false;

		}
	}

	public void viewHistory() {

		if (selectPemrmitHisDTO != null) {
			CommonDTO selectDTO = new CommonDTO();
			selectDTO.setApplicationNo(selectPemrmitHisDTO.getApplicationNo());
			historyList = new ArrayList<>();
			historyList = commonService.getHistoryData(selectDTO);

			if (historyList.isEmpty()) {

				errorMsg = "No data found.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

			} else {
				RequestContext.getCurrentInstance().execute("PF('historyDialog').show()");
				RequestContext.getCurrentInstance().update("frmhistorydialog");
			}

		} else {

			errorMsg = "Please select a row.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		}

	}

	public void viewOldHistory() {
		if (isSelectedVehicleNo) {
			if (!selectedVehicleNo.equals("")) {
				oldHistoryList = new ArrayList<PermitRenewalsDTO>(0);
				oldHistoryList = permitRenewalsService.getOldHistoryDataWithBusNo(selectedVehicleNo);
				if (oldHistoryList.isEmpty()) {

					errorMsg = "No data found.";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				} else {
					RequestContext.getCurrentInstance().execute("PF('oldHistoryDialog').show()");
					RequestContext.getCurrentInstance().update("frmoldhistorydialog");
				}
			} else {

			}
		} else if (isSelectedPermitNo) {
			if (!selectedPermitNo.equals("")) {
				oldHistoryList = new ArrayList<PermitRenewalsDTO>(0);
				oldHistoryList = permitRenewalsService.getOldHistoryDataWithPermitNo(selectedPermitNo);
				if (oldHistoryList.isEmpty()) {

					errorMsg = "No data found.";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				} else {
					RequestContext.getCurrentInstance().execute("PF('oldHistoryDialog').show()");
					RequestContext.getCurrentInstance().update("frmoldhistorydialog");
				}
			} else {

			}
		}

	}

	public String viewAmendments() {
		boolean isTaskCompleteInDetails = false;
		boolean isTaskCompleteInHistory = false;

		if (selectPemrmitHisDTO != null) {

			isTaskCompleteInDetails = commonService.checkTaskDetails(selectPemrmitHisDTO.getApplicationNo(), "AM100");
			isTaskCompleteInHistory = commonService.checkTaskHistory(selectPemrmitHisDTO.getApplicationNo(), "AM100");

			if (isTaskCompleteInDetails == true || isTaskCompleteInHistory == true) {
				HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
						.getRequest();
				sessionBackingBean.setPageMode("V");
				sessionBackingBean.setApproveURL(request.getRequestURL().toString());
				sessionBackingBean.setSearchURL(null);
				sessionBackingBean.setApproveURLStatus(true);
				sessionBackingBean.setApplicationNo(selectPemrmitHisDTO.getApplicationNo());
				sessionBackingBean.setBusRegNo(selectPemrmitHisDTO.getRegNoOfBus());
				sessionBackingBean.setPermitNo(selectPemrmitHisDTO.getPermitNo());
				sessionBackingBean.inquirySavedDataList = dataList;
				sessionBackingBean.isClicked = true;

				// add new sessions values
				sessionBackingBean.setSelectedVehicleNoInquiryDet(selectedVehicleNo);
				sessionBackingBean.setSelectedPermitNoInquiryDet(selectedPermitNo);
				sessionBackingBean.setGoFromInquiryDetPg(true);
				sessionBackingBean.setGoBackTOInquiryDetPg(false);
				sessionBackingBean.setSearchedByPermitNo(isSelectedPermitNo);
				sessionBackingBean.setSearchedByVehicleNo(isSelectedVehicleNo);

				return "/pages/amendmentsToBusOrOwner/viewBusOwnerServiceAmendment.xhtml#!";
			} else {
				errorMsg = "No Amendments record.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				return null;
			}
		} else {
			errorMsg = "Please select a row.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			return null;
		}
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public PermitRenewalsDTO getPermitRenewalsDTO() {
		return permitRenewalsDTO;
	}

	public void setPermitRenewalsDTO(PermitRenewalsDTO permitRenewalsDTO) {
		this.permitRenewalsDTO = permitRenewalsDTO;
	}

	public PermitRenewalsService getPermitRenewalsService() {
		return permitRenewalsService;
	}

	public void setPermitRenewalsService(PermitRenewalsService permitRenewalsService) {
		this.permitRenewalsService = permitRenewalsService;
	}

	public List<PermitRenewalsDTO> getVehicleNoList() {
		return vehicleNoList;
	}

	public void setVehicleNoList(List<PermitRenewalsDTO> vehicleNoList) {
		this.vehicleNoList = vehicleNoList;
	}

	public List<PermitRenewalsDTO> getPermitNoList() {
		return permitNoList;
	}

	public void setPermitNoList(List<PermitRenewalsDTO> permitNoList) {
		this.permitNoList = permitNoList;
	}

	public String getSelectedVehicleNo() {
		return selectedVehicleNo;
	}

	public void setSelectedVehicleNo(String selectedVehicleNo) {
		this.selectedVehicleNo = selectedVehicleNo;
	}

	public String getSelectedPermitNo() {
		return selectedPermitNo;
	}

	public void setSelectedPermitNo(String selectedPermitNo) {
		this.selectedPermitNo = selectedPermitNo;
	}

	public int getActiveTabIndex() {
		return activeTabIndex;
	}

	public void setActiveTabIndex(int activeTabIndex) {
		this.activeTabIndex = activeTabIndex;
	}

	public boolean isShowActiveTab() {
		return showActiveTab;
	}

	public void setShowActiveTab(boolean showActiveTab) {
		this.showActiveTab = showActiveTab;
	}

	public boolean isShowNewPermitNoField() {
		return showNewPermitNoField;
	}

	public void setShowNewPermitNoField(boolean showNewPermitNoField) {
		this.showNewPermitNoField = showNewPermitNoField;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public PermitRenewalsDTO getAttorneyHolderDTO() {
		return attorneyHolderDTO;
	}

	public void setAttorneyHolderDTO(PermitRenewalsDTO attorneyHolderDTO) {
		this.attorneyHolderDTO = attorneyHolderDTO;
	}

	public PermitRenewalsDTO getSelectPemrmitHisDTO() {
		return selectPemrmitHisDTO;
	}

	public void setSelectPemrmitHisDTO(PermitRenewalsDTO selectPemrmitHisDTO) {
		this.selectPemrmitHisDTO = selectPemrmitHisDTO;
	}

	public PermitRenewalsDTO getSelectedPermitHistoryRowDTO() {
		return selectedPermitHistoryRowDTO;
	}

	public void setSelectedPermitHistoryRowDTO(PermitRenewalsDTO selectedPermitHistoryRowDTO) {
		this.selectedPermitHistoryRowDTO = selectedPermitHistoryRowDTO;
	}

	public List<PermitRenewalsDTO> getDataList() {
		return dataList;
	}

	public void setDataList(List<PermitRenewalsDTO> dataList) {
		this.dataList = dataList;
	}

	public boolean isDisableButton() {
		return disableButton;
	}

	public void setDisableButton(boolean disableButton) {
		this.disableButton = disableButton;
	}

	public VehicleInspectionDTO getViewedDetails() {
		return viewedDetails;
	}

	public void setViewedDetails(VehicleInspectionDTO viewedDetails) {
		this.viewedDetails = viewedDetails;
	}

	public VehicleInspectionDTO getVehicleDTO() {
		return vehicleDTO;
	}

	public void setVehicleDTO(VehicleInspectionDTO vehicleDTO) {
		this.vehicleDTO = vehicleDTO;
	}

	public List<VehicleInspectionDTO> getDataListViewInspection() {
		return dataListViewInspection;
	}

	public void setDataListViewInspection(List<VehicleInspectionDTO> dataListViewInspection) {
		this.dataListViewInspection = dataListViewInspection;
	}

	public RouteDTO getRouteDTO() {
		return routeDTO;
	}

	public void setRouteDTO(RouteDTO routeDTO) {
		this.routeDTO = routeDTO;
	}

	public IssuePermitDTO getIssuePermitDTO() {
		return issuePermitDTO;
	}

	public void setIssuePermitDTO(IssuePermitDTO issuePermitDTO) {
		this.issuePermitDTO = issuePermitDTO;
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

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		inquiryDetailsBackingBean.logger = logger;
	}

	public PermitRenewalsDTO getSearchDetailsForVehicleNoDTO() {
		return searchDetailsForVehicleNoDTO;
	}

	public void setSearchDetailsForVehicleNoDTO(PermitRenewalsDTO searchDetailsForVehicleNoDTO) {
		this.searchDetailsForVehicleNoDTO = searchDetailsForVehicleNoDTO;
	}

	public PermitRenewalsDTO getNewPermitDetailsDTO() {
		return newPermitDetailsDTO;
	}

	public void setNewPermitDetailsDTO(PermitRenewalsDTO newPermitDetailsDTO) {
		this.newPermitDetailsDTO = newPermitDetailsDTO;
	}

	public IssuePermitDTO getFilteredValuesDTO() {
		return filteredValuesDTO;
	}

	public void setFilteredValuesDTO(IssuePermitDTO filteredValuesDTO) {
		this.filteredValuesDTO = filteredValuesDTO;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public InspectionActionPointService getInspectionActionPointService() {
		return inspectionActionPointService;
	}

	public void setInspectionActionPointService(InspectionActionPointService inspectionActionPointService) {
		this.inspectionActionPointService = inspectionActionPointService;
	}

	public IssuePermitService getIssuePermitService() {
		return issuePermitService;
	}

	public void setIssuePermitService(IssuePermitService issuePermitService) {
		this.issuePermitService = issuePermitService;
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

	public VehicleInspectionService getVehicleInspectionService() {
		return vehicleInspectionService;
	}
	

	public List<CommonInquiryDTO> getTerminalBusDataList() {
		return terminalBusDataList;
	}

	public void setTerminalBusDataList(List<CommonInquiryDTO> terminalBusDataList) {
		this.terminalBusDataList = terminalBusDataList;
	}

	public void setVehicleInspectionService(VehicleInspectionService vehicleInspectionService) {
		this.vehicleInspectionService = vehicleInspectionService;
	}

	public byte[] getThirdVehicleImg() {
		return thirdVehicleImg;
	}

	public void setThirdVehicleImg(byte[] thirdVehicleImg) {
		this.thirdVehicleImg = thirdVehicleImg;
	}

	public byte[] getSixthVehicleImg() {
		return sixthVehicleImg;
	}

	public void setSixthVehicleImg(byte[] sixthVehicleImg) {
		this.sixthVehicleImg = sixthVehicleImg;
	}

	public boolean isOwnerImageUpload() {
		return ownerImageUpload;
	}

	public void setOwnerImageUpload(boolean ownerImageUpload) {
		this.ownerImageUpload = ownerImageUpload;
	}

	public PaymentVoucherDTO getPaymentVoucherDTO() {
		return paymentVoucherDTO;
	}

	public void setPaymentVoucherDTO(PaymentVoucherDTO paymentVoucherDTO) {
		this.paymentVoucherDTO = paymentVoucherDTO;
	}

	public BigDecimal getVoutotalfee() {
		return voutotalfee;
	}

	public void setVoutotalfee(BigDecimal voutotalfee) {
		this.voutotalfee = voutotalfee;
	}

	public List<PaymentVoucherDTO> getVoucherDetails() {
		return voucherDetails;
	}

	public void setVoucherDetails(List<PaymentVoucherDTO> voucherDetails) {
		this.voucherDetails = voucherDetails;
	}

	public boolean isDisableAttorneyHolderBtn() {
		return disableAttorneyHolderBtn;
	}

	public void setDisableAttorneyHolderBtn(boolean disableAttorneyHolderBtn) {
		this.disableAttorneyHolderBtn = disableAttorneyHolderBtn;
	}

	public List<CommonDTO> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<CommonDTO> historyList) {
		this.historyList = historyList;
	}

	public List<PermitRenewalsDTO> getPaymentHistoryDataList() {
		return paymentHistoryDataList;
	}

	public void setPaymentHistoryDataList(List<PermitRenewalsDTO> paymentHistoryDataList) {
		this.paymentHistoryDataList = paymentHistoryDataList;
	}

	public List<PermitRenewalsDTO> getChargeTypePaymentDataList() {
		return chargeTypePaymentDataList;
	}

	public void setChargeTypePaymentDataList(List<PermitRenewalsDTO> chargeTypePaymentDataList) {
		this.chargeTypePaymentDataList = chargeTypePaymentDataList;
	}

	public PermitRenewalsDTO getSelectedViewRow() {
		return selectedViewRow;
	}

	public void setSelectedViewRow(PermitRenewalsDTO selectedViewRow) {
		this.selectedViewRow = selectedViewRow;
	}

	public boolean isShowChargeTypeTable() {
		return showChargeTypeTable;
	}

	public void setShowChargeTypeTable(boolean showChargeTypeTable) {
		this.showChargeTypeTable = showChargeTypeTable;
	}

	public boolean isShowPermitRenewalBtn() {
		return showPermitRenewalBtn;
	}

	public void setShowPermitRenewalBtn(boolean showPermitRenewalBtn) {
		this.showPermitRenewalBtn = showPermitRenewalBtn;
	}

	public boolean isShowAmendmentsBtn() {
		return showAmendmentsBtn;
	}

	public void setShowAmendmentsBtn(boolean showAmendmentsBtn) {
		this.showAmendmentsBtn = showAmendmentsBtn;
	}

	public List<PermitRenewalsDTO> getOldHistoryList() {
		return oldHistoryList;
	}

	public void setOldHistoryList(List<PermitRenewalsDTO> oldHistoryList) {
		this.oldHistoryList = oldHistoryList;
	}

	public List<VehicleInspectionDTO> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<VehicleInspectionDTO> locationList) {
		this.locationList = locationList;
	}

	public List<VehicleInspectionDTO> getServicelist() {
		return servicelist;
	}

	public void setServicelist(List<VehicleInspectionDTO> servicelist) {
		this.servicelist = servicelist;
	}

	public SisuSeriyaDTO getSubsidyDTO() {
		return subsidyDTO;
	}

	public void setSubsidyDTO(SisuSeriyaDTO subsidyDTO) {
		this.subsidyDTO = subsidyDTO;
	}

	public List<SisuSeriyaDTO> getOperatorDepoNameList() {
		return operatorDepoNameList;
	}

	public void setOperatorDepoNameList(List<SisuSeriyaDTO> operatorDepoNameList) {
		this.operatorDepoNameList = operatorDepoNameList;
	}

	public List<SisuSeriyaDTO> getBusNoList() {
		return busNoList;
	}

	public void setBusNoList(List<SisuSeriyaDTO> busNoList) {
		this.busNoList = busNoList;
	}

	public List<SisuSeriyaDTO> getRequestNoList() {
		return requestNoList;
	}

	public void setRequestNoList(List<SisuSeriyaDTO> requestNoList) {
		this.requestNoList = requestNoList;
	}

	public List<SisuSeriyaDTO> getServiceRefNoList() {
		return serviceRefNoList;
	}

	public void setServiceRefNoList(List<SisuSeriyaDTO> serviceRefNoList) {
		this.serviceRefNoList = serviceRefNoList;
	}

	public List<SisuSeriyaDTO> getServiceNoList() {
		return serviceNoList;
	}

	public void setServiceNoList(List<SisuSeriyaDTO> serviceNoList) {
		this.serviceNoList = serviceNoList;
	}

	public List<SisuSeriyaDTO> getSisuDataList() {
		return sisuDataList;
	}

	public void setSisuDataList(List<SisuSeriyaDTO> sisuDataList) {
		this.sisuDataList = sisuDataList;
	}

	public boolean isShowSisuSearchResultsPanel() {
		return showSisuSearchResultsPanel;
	}

	public void setShowSisuSearchResultsPanel(boolean showSisuSearchResultsPanel) {
		this.showSisuSearchResultsPanel = showSisuSearchResultsPanel;
	}

	public ManageInvestigationDTO getManageInvestigationDTO() {
		return manageInvestigationDTO;
	}

	public void setManageInvestigationDTO(ManageInvestigationDTO manageInvestigationDTO) {
		this.manageInvestigationDTO = manageInvestigationDTO;
	}

	public ManageInvestigationService getManageInvestigationService() {
		return manageInvestigationService;
	}

	public void setManageInvestigationService(ManageInvestigationService manageInvestigationService) {
		this.manageInvestigationService = manageInvestigationService;
	}

	public List<ManageInvestigationDTO> getInvestigationDataListForRefNo() {
		return investigationDataListForRefNo;
	}

	public void setInvestigationDataList(List<ManageInvestigationDTO> InvestigationDataListForRefNo) {
		this.investigationDataListForRefNo = InvestigationDataListForRefNo;
	}

	public List<ManageInvestigationDTO> getInvestigationRefNoList() {
		return investigationRefNoList;
	}

	public void setInvestigationRefNoList(List<ManageInvestigationDTO> investigationRefNoList) {
		this.investigationRefNoList = investigationRefNoList;
	}

	public List<ManageInvestigationDTO> getInvestigationBusNoList() {
		return investigationBusNoList;
	}

	public void setInvestigationBusNoList(List<ManageInvestigationDTO> investigationBusNoList) {
		this.investigationBusNoList = investigationBusNoList;
	}

	public List<ManageInvestigationDTO> getInvestigationPermitNoList() {
		return investigationPermitNoList;
	}

	public void setInvestigationPermitNoList(List<ManageInvestigationDTO> investigationPermitNoList) {
		this.investigationPermitNoList = investigationPermitNoList;
	}

	public List<ManageInvestigationDTO> getInvestigationOffNameList() {
		return investigationOffNameList;
	}

	public void setInvestigationOffNameList(List<ManageInvestigationDTO> investigationOffNameList) {
		this.investigationOffNameList = investigationOffNameList;
	}

	public List<ManageInvestigationDTO> getInvestigationDataListForOffenceName() {
		return investigationDataListForOffenceName;
	}

	public void setInvestigationDataListForOffenceName(List<ManageInvestigationDTO> investigationDataListForOffenceName) {
		this.investigationDataListForOffenceName = investigationDataListForOffenceName;
	}

	public boolean isShowInvTable() {
		return showInvTable;
	}

	public void setShowInvTable(boolean showInvTable) {
		this.showInvTable = showInvTable;
	}

	public boolean isDisableDropRef() {
		return disableDropRef;
	}

	public void setDisableDropRef(boolean disableDropRef) {
		this.disableDropRef = disableDropRef;
	}

	public boolean isDisableDropOff() {
		return disableDropOff;
	}

	public void setDisableDropOff(boolean disableDropOff) {
		this.disableDropOff = disableDropOff;
	}

	public List<CommonInquiryDTO> getGrievanceDataList() {
		return grievanceDataList;
	}

	public void setGrievanceDataList(List<CommonInquiryDTO> grievanceDataList) {
		this.grievanceDataList = grievanceDataList;
	}

	public List<CommonInquiryDTO> getGrievanceComplaintNoList() {
		return grievanceComplaintNoList;
	}

	public void setGrievanceComplaintNoList(List<CommonInquiryDTO> grievanceComplaintNoList) {
		this.grievanceComplaintNoList = grievanceComplaintNoList;
	}

	public List<CommonInquiryDTO> getGrievanceBusNoList() {
		return grievanceBusNoList;
	}

	public void setGrievanceBusNoList(List<CommonInquiryDTO> grievanceBusNoList) {
		this.grievanceBusNoList = grievanceBusNoList;
	}

	public List<CommonInquiryDTO> getGrievancePermitNoList() {
		return grievancePermitNoList;
	}

	public void setGrievancePermitNoList(List<CommonInquiryDTO> grievancePermitNoList) {
		this.grievancePermitNoList = grievancePermitNoList;
	}

	public GrievanceManagementDashboardService getGrievanceManagementDashboardService() {
		return grievanceManagementDashboardService;
	}

	public void setGrievanceManagementDashboardService(GrievanceManagementDashboardService grievanceManagementDashboardService) {
		this.grievanceManagementDashboardService = grievanceManagementDashboardService;
	}

	public CommonInquiryDTO getCommonInquiryDTO() {
		return commonInquiryDTO;
	}

	public void setCommonInquiryDTO(CommonInquiryDTO commonInquiryDTO) {
		this.commonInquiryDTO = commonInquiryDTO;
	}

	public TerminalManagementService getTerminalManagementService() {
		return terminalManagementService;
	}

	public void setTerminalManagementService(TerminalManagementService terminalManagementService) {
		this.terminalManagementService = terminalManagementService;
	}

	public List<CommonInquiryDTO> getTerminalBusNoList() {
		return terminalBusNoList;
	}

	public void setTerminalBusNoList(List<CommonInquiryDTO> terminalBusNoList) {
		this.terminalBusNoList = terminalBusNoList;
	}

	public List<CommonInquiryDTO> getTerminalVoucherNoList() {
		return terminalVoucherNoList;
	}

	public void setTerminalVoucherNoList(List<CommonInquiryDTO> terminalVoucherNoList) {
		this.terminalVoucherNoList = terminalVoucherNoList;
	}

	public List<CommonInquiryDTO> getTerminalTerminalNoList() {
		return terminalTerminalNoList;
	}

	public void setTerminalTerminalNoList(List<CommonInquiryDTO> terminalTerminalNoList) {
		this.terminalTerminalNoList = terminalTerminalNoList;
	}

	public List<CommonInquiryDTO> getTerminalPaymentTypeList() {
		return terminalPaymentTypeList;
	}

	public void setTerminalPaymentTypeList(List<CommonInquiryDTO> terminalPaymentTypeList) {
		this.terminalPaymentTypeList = terminalPaymentTypeList;
	}

	public void setInvestigationDataListForRefNo(List<ManageInvestigationDTO> investigationDataListForRefNo) {
		this.investigationDataListForRefNo = investigationDataListForRefNo;
	}

	public List<CommonInquiryDTO> getTerminalTerminalDataList() {
		return terminalTerminalDataList;
	}

	public void setTerminalTerminalDataList(List<CommonInquiryDTO> terminalTerminalDataList) {
		this.terminalTerminalDataList = terminalTerminalDataList;
	}

	public List<CommonInquiryDTO> getTerminalVoucherDataList() {
		return terminalVoucherDataList;
	}

	public void setTerminalVoucherDataList(List<CommonInquiryDTO> terminalVoucherDataList) {
		this.terminalVoucherDataList = terminalVoucherDataList;
	}

	public boolean isShowVouTable() {
		return showVouTable;
	}

	public void setShowVouTable(boolean showVouTable) {
		this.showVouTable = showVouTable;
	}

	public List<CommonInquiryDTO> getTerminalPayDataList() {
		return terminalPayDataList;
	}

	public void setTerminalPayDataList(List<CommonInquiryDTO> terminalPayDataList) {
		this.terminalPayDataList = terminalPayDataList;
	}

	public boolean isShowPayTable() {
		return showPayTable;
	}

	public void setShowPayTable(boolean showPayTable) {
		this.showPayTable = showPayTable;
	}

	public boolean isShowBusTable() {
		return showBusTable;
	}

	public void setShowBusTable(boolean showBusTable) {
		this.showBusTable = showBusTable;
	}

	public boolean isDisableDropBus() {
		return disableDropBus;
	}

	public void setDisableDropBus(boolean disableDropBus) {
		this.disableDropBus = disableDropBus;
	}

	public boolean isDisableDropVou() {
		return disableDropVou;
	}

	public void setDisableDropVou(boolean disableDropVou) {
		this.disableDropVou = disableDropVou;
	}

	public List<CommonInquiryDTO> getDriConApplicationDataList() {
		return driConApplicationDataList;
	}

	public void setDriConApplicationDataList(List<CommonInquiryDTO> driConApplicationDataList) {
		this.driConApplicationDataList = driConApplicationDataList;
	}

	public List<CommonInquiryDTO> getDriConNICDataList() {
		return driConNICDataList;
	}

	public void setDriConNICDataList(List<CommonInquiryDTO> driConNICDataList) {
		this.driConNICDataList = driConNICDataList;
	}

	public List<CommonInquiryDTO> getDriConIdDataList() {
		return driConIdDataList;
	}

	public void setDriConIdDataList(List<CommonInquiryDTO> driConIdDataList) {
		this.driConIdDataList = driConIdDataList;
	}

	public List<CommonInquiryDTO> getDriConBusNoList() {
		return driConBusNoList;
	}

	public void setDriConBusNoList(List<CommonInquiryDTO> driConBusNoList) {
		this.driConBusNoList = driConBusNoList;
	}

	public boolean isDisableDCDrop() {
		return disableDCDrop;
	}

	public void setDisableDCDrop(boolean disableDCDrop) {
		this.disableDCDrop = disableDCDrop;
	}

	public boolean isDisableDCCal() {
		return disableDCCal;
	}

	public void setDisableDCCal(boolean disableDCCal) {
		this.disableDCCal = disableDCCal;
	}

	public List<CommonInquiryDTO> getDriConNICDCIDDataList() {
		return driConNICDCIDDataList;
	}

	public void setDriConNICDCIDDataList(List<CommonInquiryDTO> driConNICDCIDDataList) {
		this.driConNICDCIDDataList = driConNICDCIDDataList;
	}

	public List<CommonInquiryDTO> getDriConDateDataList() {
		return driConDateDataList;
	}

	public void setDriConDateDataList(List<CommonInquiryDTO> driConDateDataList) {
		this.driConDateDataList = driConDateDataList;
	}

	public boolean isDisableVou() {
		return disableVou;
	}

	public void setDisableVou(boolean disableVou) {
		this.disableVou = disableVou;
	}
	
}
