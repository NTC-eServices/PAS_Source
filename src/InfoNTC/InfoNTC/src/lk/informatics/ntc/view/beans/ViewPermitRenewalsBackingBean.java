package lk.informatics.ntc.view.beans;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.BusOwnerDTO;
import lk.informatics.ntc.model.dto.ComplaintRequestDTO;
import lk.informatics.ntc.model.dto.DocumentManagementDTO;
import lk.informatics.ntc.model.dto.FluingSquadVioConditionDTO;
import lk.informatics.ntc.model.dto.FlyingManageInvestigationLogDTO;
import lk.informatics.ntc.model.dto.FlyingSquadVioDocumentsDTO;
import lk.informatics.ntc.model.dto.IssuePermitDTO;
import lk.informatics.ntc.model.dto.OminiBusDTO;
import lk.informatics.ntc.model.dto.PaymentVoucherDTO;
import lk.informatics.ntc.model.dto.PermitDTO;
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
import lk.informatics.ntc.model.service.PermitRenewalsService;
import lk.informatics.ntc.model.service.PermitService;
import lk.informatics.ntc.model.service.QueueManagementService;
import lk.informatics.ntc.model.service.VehicleInspectionService;
import lk.informatics.ntc.view.util.PropertyReader;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "viewPermitRenewalBackingBean")
@ViewScoped
public class ViewPermitRenewalsBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	// DTO
	private PermitDTO permitDTO;
	private OminiBusDTO inspectionDetails;
	private BusOwnerDTO busownerDTO;
	public List<PermitRenewalsDTO> dataList = new ArrayList<PermitRenewalsDTO>(0);
	private List<DocumentManagementDTO> mandatoryList = new ArrayList<DocumentManagementDTO>();
	private List<DocumentManagementDTO> ApplicationList = new ArrayList<DocumentManagementDTO>();
	private List<DocumentManagementDTO> RenewalApplicationList = new ArrayList<DocumentManagementDTO>();
	private List<DocumentManagementDTO> optionalList = new ArrayList<DocumentManagementDTO>();
	public List<PermitRenewalsDTO> paymentHistoryDataList = new ArrayList<PermitRenewalsDTO>(0);
	private PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();
	private PermitRenewalsDTO checkAndDisplayRemarkValue;
	private VehicleInspectionDTO viewedDetails;
	private boolean showInquiryBackButton = false;
	private boolean renderedMood;
	private boolean showRenewalsBackButton = false;

	// services
	public PermitService permitservice;
	private PermitRenewalsService permitRenewalsService;
	private DocumentManagementService documentManagementService;
	private InspectionActionPointService inspectionActionPointService;
	private int activeTabIndex;
	private boolean active;
	public List<PermitRenewalsDTO> getPermitNo = new ArrayList<PermitRenewalsDTO>(0);
	public List<PermitRenewalsDTO> getApplicationNo = new ArrayList<PermitRenewalsDTO>(0);
	public List<PermitRenewalsDTO> currentPermitNoList = new ArrayList<PermitRenewalsDTO>(0);
	private String busRegNo, applicationNo, permitNo, queueNo;
	private String myDate;
	private String errorMessage;
	public PermitDTO viewPermitList;
	public PermitRenewalsDTO viewData;
	private PermitDTO loadValuesForSelectedOneDTO;
	private OminiBusDTO ominiBusDTO = new OminiBusDTO();
	private String strSelectedPermitNo;
	private String strSelectedApplicationNo;
	private String strSelectedQueueNo;
	private String strSelectedServicetype, strSelectedRouteNO, strselectedexpireDate;
	private String strSelectedOrigin, strSeletedDesination;
	private String selecPreferLanguage, selectTitle, selectGender, selectNIC, selectNameFull, slectNameIniti,
			selectMaritalStatus, selectTelePhone, selectMobile, selectProvince, selectDistrict, selectAddres1,
			selectAddres2, selectCity, selectdivsec, selectAddres1_sin, selectAddres1_tamil, selectAddres2_sin,
			selectAddres2_tamil, selectNameFull_sin, selectNameFull_tamil, selectCity_sin, selectCity_tamil, selecDOB,
			newExpireDate, selectedRemarks, selectNewExpire;
	private int selectedRenewdPeriod;
	private String viewInspectionURL;

	private boolean dissableNameSinhala;
	private boolean dissableNameTamil;
	private boolean dissableAddrs1Sinhla;
	private boolean dissableAddrs1Tamil;
	private boolean dissableAddrs2Sinhala;
	private boolean dissableAddrs2Tamil;
	private boolean dissableCitySinhala;
	private boolean dissableCityTamil;
	private boolean disabledPermitNo = false;
	private boolean disabledApplicationNo = false;
	private boolean disabledSearchBtn = false;
	private boolean showSearchedDetailsForm = false;
	private boolean isCheckedGotoVehicleInspectionViewMode = false;
	private PaymentVoucherDTO paymentVoucherDTO;
	private boolean renewalTyp;
	private boolean inquaryType;
	private boolean disabledVehicleInspection;
	private Boolean routeFlag;
	
	public VehicleInspectionDTO vehicleDTO = new VehicleInspectionDTO();
	
	private IssuePermitDTO issuePermitDTO = new IssuePermitDTO();
	private RouteDTO routeDTO;
	
	private IssuePermitService issuePermitService;
	private HistoryService historyService;
	private List<VehicleInspectionDTO> dataListViewInspection = new ArrayList<VehicleInspectionDTO>();
	private QueueManagementService queueManagementService;
	private CommonService commonService;
	private VehicleInspectionService vehicleInspectionService;
	
	private PermitRenewalsDTO vehicleOwnerHistoryDTO;
	private OminiBusDTO OminiBusHistoryDTO;
	private PermitRenewalsDTO applicationHistoryDTO;
	private UploadImageDTO uploadImageDTO;
	private IssuePermitDTO filteredValuesDTO;
	/** for public complain **/
	private ComplaintRequestDTO publicComplanDto;
	private List<ComplaintRequestDTO> complainDetViewList = new ArrayList<>() ;
	private ManageInquiryService manageInquiryService ;
	private boolean renderComplainDet;
	private ComplaintRequestDTO viewSelect;
	private ComplaintRequestDTO selectedComplaintDTO;

	/** end for public complain **/
	
	/** for investigation pop up**/
	
    private FlyingSquadChargeSheetService flyingSquadChargeSheetService;
	private ArrayList<FluingSquadVioConditionDTO>conditionList;
	private ArrayList<FlyingSquadVioDocumentsDTO>documentList;
	private FlyingManageInvestigationLogDTO  flyingManageInvestigationLogDTO;
	

	private List<FlyingManageInvestigationLogDTO> investigationDetViewList = new ArrayList<>() ;
	private FlyingManageInvestigationLogDTO viewInvSelect;
	private FlyingManageInvestigationLogDTO selectedInvDTO;
	
	/** end investigation pop up**/

	private SimRegistrationDTO simRegistrationDTO;
	private List<SimRegistrationDTO> emiDetViewList = new ArrayList<>() ;
	
	@PostConstruct
	public void init() {


		permitservice = (PermitService) SpringApplicationContex.getBean("permitservice");
		permitRenewalsService = (PermitRenewalsService) SpringApplicationContex.getBean("permitRenewalsService");
		documentManagementService = (DocumentManagementService) SpringApplicationContex
				.getBean("documentManagementService");
		
		inspectionActionPointService = (InspectionActionPointService) SpringApplicationContex
				.getBean("inspectionActionPointService");
		flyingSquadChargeSheetService=((FlyingSquadChargeSheetService) SpringApplicationContex.getBean("flyingSquadChargeSheetService"));
		getApplicationNo = permitservice.getAppNoForViewPermitRen();/** changed by tharushi.e for drop AAP application no from list**/
		getPermitNo= permitservice.getPermitNoList();
        permitDTO = new PermitDTO();
		viewData = new PermitRenewalsDTO();
		LocalDate localDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		myDate = localDate.format(formatter);
		active = true;
		showInquiryBackButton = false;
		showRenewalsBackButton = false;
		renderedMood = true;
		setCheckedGotoVehicleInspectionViewMode(sessionBackingBean.isCheckSessionBeanNullViewPermitRenewal());

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
			

			viewLoadedData();
		} 

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
			

			displayCheckDocumentDataList();
			displayPaymentHistoryDataList();
			sessionBackingBean.setCheckSessionBeanNullViewPermitRenewal(false);
		} 
		
		RequestContext.getCurrentInstance().update("btnBack");
		if (sessionBackingBean.isClicked == true || sessionBackingBean.isVehicelInsfection == true) {
			
			renderedMood = false;
			showInquiryBackButton = true;
			RequestContext.getCurrentInstance().update("btnBack");
			RequestContext.getCurrentInstance().update("cmdBtnSearch");
			RequestContext.getCurrentInstance().update("btnClear");
			RequestContext.getCurrentInstance().update("panelOne");
			permitDTO.setBusRegNo(sessionBackingBean.getBusRegNo());
			permitDTO.setApplicationNo(sessionBackingBean.getApplicationNo());
			permitDTO.setPermitNo(sessionBackingBean.getPermitNo());
			disabledVehicleInspection = true;
			searchData();
			sessionBackingBean.isClicked = false;
			sessionBackingBean.setInquiryTYpe(true);
		}

		RequestContext.getCurrentInstance().update("btnBackRenewal");
		if (sessionBackingBean.renewalViewMood == true) {
			renderedMood = false;
			showRenewalsBackButton = true;
			RequestContext.getCurrentInstance().update("btnBackRenewal");
			RequestContext.getCurrentInstance().update("cmdBtnSearch");
			RequestContext.getCurrentInstance().update("btnClear");
			RequestContext.getCurrentInstance().update("panelOne");
			permitDTO.setBusRegNo(sessionBackingBean.getBusRegNo());
			permitDTO.setApplicationNo(sessionBackingBean.getApplicationNo());
			permitDTO.setPermitNo(sessionBackingBean.getPermitNo());
			disabledVehicleInspection = true;
			searchData();
			sessionBackingBean.renewalViewMood = false;
			sessionBackingBean.setRenewalType(true);

		}
		renderComplainDet=false;
	}


	public void onLoadValuesForSelectedPermitNo() {
	
		loadValuesForSelectedOneDTO = permitservice.getLoadValuesForSelectedOne(permitDTO.getPermitNo(),
				permitDTO.getApplicationNo(), permitDTO.getQueueNo(), permitDTO.getBusRegNo());
		permitDTO.setPermitNo(loadValuesForSelectedOneDTO.getPermitNo());
		permitDTO.setApplicationNo(loadValuesForSelectedOneDTO.getApplicationNo());
		permitDTO.setBusRegNo(loadValuesForSelectedOneDTO.getBusRegNo());
		permitDTO.setQueueNo(loadValuesForSelectedOneDTO.getQueueNo());
		setDisabledApplicationNo(true);
		setDisabledPermitNo(true);
	}

	public void generateVoucher() {

		CreatePaymentVoucherBckingBean c = new CreatePaymentVoucherBckingBean();

		try {

			sessionBackingBean.setApplicationNo(permitDTO.getApplicationNo());
			sessionBackingBean.setTransactionDescription("RENEWAL");
			sessionBackingBean.setPermitNo(permitDTO.getPermitNo());

			paymentVoucherDTO = new PaymentVoucherDTO();
			paymentVoucherDTO.setApplicationNo(permitDTO.getApplicationNo());
			paymentVoucherDTO.setTransactionDescription("TENDER");
			paymentVoucherDTO.setPermitNo(permitDTO.getPermitNo());
			RequestContext.getCurrentInstance().execute("PF('generateVoucher').show()");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void backToInquiry() {
	

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
		setShowInquiryBackButton(false);
		RequestContext.getCurrentInstance().update("btnBack");
	}

	public void backToRenewals() {
		

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
		setShowInquiryBackButton(false);
		RequestContext.getCurrentInstance().update("btnBackRenewal");
	}

	public void completeRegNo() {

		busRegNo = permitservice.getBusRegNo(permitDTO.getApplicationNo(), permitDTO.getPermitNo(),
				permitDTO.getQueueNo());
		permitDTO.setBusRegNo(busRegNo);
	
	}

	public void completeApplicationNo() {
		applicationNo = permitservice.completeApplicationNo(permitDTO.getPermitNo(), permitDTO.getQueueNo());
		permitDTO.setApplicationNo(applicationNo);

	}

	public void completePermitNo() {
		permitNo = permitservice.completePermitNo(permitDTO.getApplicationNo(), permitDTO.getQueueNo());
		permitDTO.setPermitNo(permitNo);
		

	}

	public void completeQueueNo() {
		queueNo = permitservice.completeQueueNo(permitDTO.getApplicationNo(), permitDTO.getPermitNo());
		permitDTO.setQueueNo(queueNo);
	}

	public void searchData() {
		

		displayCheckDocumentDataList();
		

		if ((permitDTO.getPermitNo() == null || permitDTO.getPermitNo().trim().equalsIgnoreCase(""))
				&& (permitDTO.getApplicationNo() == null || permitDTO.getApplicationNo().trim().equalsIgnoreCase(""))
				&& (permitDTO.getQueueNo() == null || permitDTO.getQueueNo().trim().equalsIgnoreCase(""))) {
			
			errorMessage = "Please enter data for searching.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

		else if (viewPermitList == null && viewData == null) {

			errorMessage = "No data For Searched Value.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
		
		else {
			active = false;
  			viewPermitList = permitservice.showData(permitDTO.getApplicationNo(), permitDTO.getPermitNo(),
					permitDTO.getQueueNo());
 			strSelectedServicetype = viewPermitList.getServiceType();
			strSelectedRouteNO = viewPermitList.getRouteNo();
			strselectedexpireDate = viewPermitList.getExpirDate();
			strSelectedOrigin = viewPermitList.getOrigin();
			strSeletedDesination = viewPermitList.getDestination();
			selectedRenewdPeriod = viewPermitList.getRePeriod();
			selectedRemarks = viewPermitList.getRemarks();
			selectNewExpire = viewPermitList.getNewPermitExpireDate();
			setDisabledSearchBtn(true);
			

			try {

				viewData = permitservice.showDataFromVehiOwner(permitDTO.getPermitNo(), permitDTO.getApplicationNo());
				selecPreferLanguage = viewData.getPreferedLanguageCode();
				selectTitle = viewData.getTitleCode();
				selectGender = viewData.getGenderCode();
				selectNIC = viewData.getNic();
				selectNameFull = viewData.getFullName();
				slectNameIniti = viewData.getNameWithInitials();
				selectMaritalStatus = viewData.getMaterialStatusId();
				selectTelePhone = viewData.getTeleNo();
				selectMobile = viewData.getMobileNo();
				selectProvince = viewData.getProvinceDescription();
				selectDistrict = viewData.getDistrictCode();
				selectAddres1 = viewData.getAddressOne();
				selectAddres2 = viewData.getAddressTwo();
				selectCity = viewData.getCity();
				selectdivsec = viewData.getDivisionalSecretariatDivision();
				selecDOB = viewData.getDob();
				selectNameFull_sin = viewData.getFullNameSinhala();
				selectAddres1_sin = viewData.getAddressOneSinhala();
				selectAddres2_sin = viewData.getAddressTwoSinhala();
				selectCity_sin = viewData.getCitySinhala();

				selectNameFull_tamil = viewData.getFullNameTamil();
				selectAddres1_tamil = viewData.getAddressOneTamil();
				selectAddres2_tamil = viewData.getAddressTwoTamil();
				selectCity_tamil = viewData.getCityTamil();
				selectTitle = permitRenewalsService.getTitleDescription(viewData.getTitleCode());
				setShowSearchedDetailsForm(true);
				
			
				String dateFormat = "dd/MM/yyyy";
				SimpleDateFormat frm = new SimpleDateFormat(dateFormat);
				inspectionDetails = permitRenewalsService.getVehiDetailsYr(permitDTO.getApplicationNo(),
						permitDTO.getBusRegNo());
				setOminiBusDTO(inspectionDetails);
				if (ominiBusDTO.getRegistrationDate() != null) {
					ominiBusDTO.setDateOfRegVal(frm.format(ominiBusDTO.getRegistrationDate()));
				} else {

				}
				if (ominiBusDTO.getFitnessCertiDate() != null) {
					ominiBusDTO.setFitnessCarficateDateVal(frm.format(ominiBusDTO.getFitnessCertiDate()));
				} else {

				}
				if (ominiBusDTO.getInsuExpDate() != null) {
					ominiBusDTO.setInsuExpiryDateVal(frm.format(ominiBusDTO.getInsuExpDate()));
				} else {

				}
				
				if (ominiBusDTO.getRevenueExpDate() != null) {
					ominiBusDTO.setRevenueExpDateVal(frm.format(ominiBusDTO.getRevenueExpDate()));
				} else {

				}
				
				if (ominiBusDTO.getEmissionExpDate() != null) {
					ominiBusDTO.setEmissionExpDateVal(frm.format(ominiBusDTO.getEmissionExpDate()));
				} else {

				}

			} catch (NullPointerException e) {
				e.printStackTrace();

			}
		}
	}

	public void clearFields() {
	
		permitDTO = new PermitDTO();
		selectNewExpire = null;
		strSelectedServicetype = null;
		strSelectedRouteNO = null;
		strselectedexpireDate = null;
		strSelectedOrigin = null;
		strSeletedDesination = null;
		selecPreferLanguage = null;
		selectTitle = null;
		selectGender = null;
		selectNIC = null;
		selectNameFull = null;
		slectNameIniti = null;
		selectMaritalStatus = null;
		selectTelePhone = null;
		selectMobile = null;
		selectProvince = null;
		selectDistrict = null;
		selectAddres1 = null;
		selectAddres2 = null;
		selectCity = null;
		selectdivsec = null;
		selectAddres1_sin = null;
		selectAddres1_tamil = null;
		selectAddres2_sin = null;
		selectAddres2_tamil = null;
		selectNameFull_sin = null;
		selectNameFull_tamil = null;
		selectCity_sin = null;
		selectCity_tamil = null;
		newExpireDate = null;
		selecDOB = null;
		selectedRenewdPeriod = 0;
		selectedRemarks = null;
		active = true;
		setDisabledApplicationNo(false);
		setDisabledPermitNo(false);
		setDisabledSearchBtn(false);
		setShowSearchedDetailsForm(false);
		sessionBackingBean.setCheckSessionBeanNullViewPermitRenewal(false);
	}

	public void displayCheckDocumentDataList() {
		
		dataList = permitRenewalsService.getAllRecordsForDocumentsCheckings();
		for (int i = 0; i < dataList.size(); i++) {
			String currentDocCode = dataList.get(i).getDocumentCode();
			String currentDocCodeDescription = dataList.get(i).getDocumentDescription();
			boolean resultValue = permitRenewalsService.checkIsSumbiited(currentDocCode, permitDTO.getApplicationNo(),
					permitDTO.getPermitNo());
			
			boolean isMandatory = permitRenewalsService.isMandatory(currentDocCode, permitDTO.getApplicationNo(),
					permitDTO.getPermitNo());
			boolean isPhysicallyExit = permitRenewalsService.isPhysicallyExit(currentDocCode,
					permitDTO.getApplicationNo(), permitDTO.getPermitNo());
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

			checkAndDisplayRemarkValue = permitRenewalsService.getRemarkDetails(currentDocCode,
					permitDTO.getApplicationNo(), permitDTO.getPermitNo());
			dataList.get(i).setDocSeqChecked(checkAndDisplayRemarkValue.getDocSeqChecked());
			dataList.get(i).setRemark(checkAndDisplayRemarkValue.getRemark());
			dataList.get(i).setDocFilePath(checkAndDisplayRemarkValue.getDocFilePath());
		}
	}

	public String viewVehicleInspectionOld() {

		if (sessionBackingBean.isRenewalType() == false && sessionBackingBean.isInquiryTYpe() == false) {

			String viewedApplicationNo = permitDTO.getApplicationNo();
			String searchedQueueNum = permitDTO.getQueueNo();
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			viewInspectionURL = request.getRequestURL().toString();
			sessionBackingBean.setViewInspectionsURL(viewInspectionURL);
			sessionBackingBean.setViewInspectionsURLStatus(true);
			sessionBackingBean.setViewedInspectionApplicationNo(viewedApplicationNo);
			sessionBackingBean.setViewedPermitNo(permitDTO.getPermitNo());
			sessionBackingBean.setSelectedQueueNo(searchedQueueNum);
			sessionBackingBean.setCheckSessionBeanNullViewPermitRenewal(true);
			sessionBackingBean.setReadOnlyFieldsInspection(true);
			sessionBackingBean.setSelectedOptionType("VIEW");

		} else if (sessionBackingBean.isRenewalType() == true) {

			String viewedApplicationNo = permitDTO.getApplicationNo();
			String searchedQueueNum = permitDTO.getQueueNo();
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			viewInspectionURL = request.getRequestURL().toString();
			sessionBackingBean.setViewInspectionsURL(viewInspectionURL);
			sessionBackingBean.setViewInspectionsURLStatus(true);
			sessionBackingBean.setViewedInspectionApplicationNo(viewedApplicationNo);
			sessionBackingBean.setViewedPermitNo(permitDTO.getPermitNo());
			sessionBackingBean.setSelectedQueueNo(searchedQueueNum);
			sessionBackingBean.setCheckSessionBeanNullViewPermitRenewal(true);
			sessionBackingBean.setReadOnlyFieldsInspection(true);
			sessionBackingBean.setSelectedOptionType("VIEW");
			sessionBackingBean.renewalViewMood = true;

		} else if (sessionBackingBean.isInquiryTYpe() == true) {
			String viewedApplicationNo = permitDTO.getApplicationNo();
			String searchedQueueNum = permitDTO.getQueueNo();
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			viewInspectionURL = request.getRequestURL().toString();
			sessionBackingBean.setViewInspectionsURL(viewInspectionURL);
			sessionBackingBean.setViewInspectionsURLStatus(true);
			sessionBackingBean.setViewedInspectionApplicationNo(viewedApplicationNo);
			sessionBackingBean.setViewedPermitNo(permitDTO.getPermitNo());
			sessionBackingBean.setSelectedQueueNo(searchedQueueNum);
			sessionBackingBean.setCheckSessionBeanNullViewPermitRenewal(true);
			sessionBackingBean.setReadOnlyFieldsInspection(true);
			sessionBackingBean.setSelectedOptionType("VIEW");
			sessionBackingBean.isClicked = true;

		}

		return "/pages/vehicleInspectionSet/vehicleInspectionInfoViewMode.xhtml";
	}
	
	
	public void viewVehicleInspection() {
		
		
		String viewedApplicationNo = permitDTO.getApplicationNo();
		String selectedPermitNo = permitDTO.getPermitNo();
		String searchedQueueNum = permitDTO.getQueueNo();
		String selectedApplicationNo = permitDTO.getApplicationNo();

		sessionBackingBean.setViewedInspectionApplicationNo(viewedApplicationNo);
		sessionBackingBean.setViewedPermitNo(selectedPermitNo);
		sessionBackingBean.setSelectedQueueNo(searchedQueueNum);
		sessionBackingBean.setApplicationNo(selectedApplicationNo);
		sessionBackingBean.setCheckPermitIssueNewPermit(true);
		loadValuesForViewInspection();
		RequestContext.getCurrentInstance().execute("PF('vehicleInspectionViewModeId').show()");
		RequestContext.getCurrentInstance().update("vehicleInspectionViewModeId");
		RequestContext.getCurrentInstance().update("vehicleInspectionViewModeId:frmUploadPhotos");
		
	}
	
	private void loadValuesForViewInspection() {

		RequestContext.getCurrentInstance().update("bla");
		RequestContext.getCurrentInstance().update("footerDiv");

		viewedDetails = inspectionActionPointService
				.getRecordForCurrentApplicationNo(permitDTO.getApplicationNo());
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
				.getAllInspectionRecordsDetails(vehicleDTO.getApplicationNo());
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

	public void onRouteChange() {

		if (strSelectedRouteNO != null && !strSelectedRouteNO.equals("")) {

			routeDTO = issuePermitService.getDetailsbyRouteNo(strSelectedRouteNO);
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

	public void viewLoadedData() {

		
		active = false;
		viewPermitList = permitservice.showData(sessionBackingBean.getViewedInspectionApplicationNo(),
				sessionBackingBean.getViewedPermitNo(), sessionBackingBean.getSelectedQueueNo());
		strSelectedServicetype = viewPermitList.getServiceType();
		strSelectedRouteNO = viewPermitList.getRouteNo();
		strselectedexpireDate = viewPermitList.getExpirDate();
		strSelectedOrigin = viewPermitList.getOrigin();
		strSeletedDesination = viewPermitList.getDestination();
		selectedRenewdPeriod = viewPermitList.getRePeriod();
		selectedRemarks = viewPermitList.getRemarks();
		selectNewExpire = viewPermitList.getNewPermitExpireDate();



		viewData = permitservice.showDataFromVehiOwner(sessionBackingBean.getViewedPermitNo(),
				sessionBackingBean.getViewedInspectionApplicationNo());
	
	
		selecPreferLanguage = viewData.getPreferedLanguageCode();
		selectTitle = permitRenewalsService.getTitleDescription(viewData.getTitleCode());
		selectGender = viewData.getGenderCode();
		selectNIC = viewData.getNic();
		selectNameFull = viewData.getFullName();
		slectNameIniti = viewData.getNameWithInitials();
		selectMaritalStatus = viewData.getMaterialStatusId();
		selectTelePhone = viewData.getTeleNo();
		selectMobile = viewData.getMobileNo();
		selectProvince = viewData.getProvinceDescription();
		selectDistrict = viewData.getDistrictCode();
		selectAddres1 = viewData.getAddressOne();
		selectAddres2 = viewData.getAddressTwo();
		selectCity = viewData.getCity();
		selectdivsec = viewData.getDivisionalSecretariatDivision();
		selecDOB = viewData.getDob();
		permitDTO.setPermitNo(viewData.getPermitNo());
		permitDTO.setApplicationNo(viewData.getApplicationNo());
		permitDTO.setQueueNo(viewData.getQueueNo());
		permitDTO.setBusRegNo(viewData.getRegNoOfBus());
		dissableNameSinhala = true;
	
		selectNameFull_sin = viewData.getFullNameSinhala();
		selectAddres1_sin = viewData.getAddressOneSinhala();
		selectAddres2_sin = viewData.getAddressTwoSinhala();
		selectCity_sin = viewData.getCitySinhala();
	

		selectNameFull_tamil = viewData.getFullNameTamil();
		selectAddres1_tamil = viewData.getAddressOneTamil();
		selectAddres2_tamil = viewData.getAddressTwoTamil();
		selectCity_tamil = viewData.getCityTamil();

		setDisabledApplicationNo(true);
		setDisabledPermitNo(true);
		setDisabledSearchBtn(true);
		setShowSearchedDetailsForm(true);
		

	}

	
	public ViewPermitRenewalsBackingBean() {
		issuePermitService = (IssuePermitService) SpringApplicationContex.getBean("issuePermitService");
		permitRenewalsService = (PermitRenewalsService) SpringApplicationContex.getBean("permitRenewalsService");
		queueManagementService = (QueueManagementService) SpringApplicationContex.getBean("queueManagementService");
		inspectionActionPointService = (InspectionActionPointService) SpringApplicationContex
				.getBean("inspectionActionPointService");
		vehicleInspectionService = (VehicleInspectionService) SpringApplicationContex
				.getBean("vehicleInspectionService");
		documentManagementService = (DocumentManagementService) SpringApplicationContex
				.getBean("documentManagementService");
		manageInquiryService=(ManageInquiryService) SpringApplicationContex.getBean("manageInquiryService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		historyService = (HistoryService) SpringApplicationContex.getBean("historyService");
		uploadImageDTO = new UploadImageDTO();
		filteredValuesDTO = new IssuePermitDTO();
		viewedDetails = new VehicleInspectionDTO();

		vehicleOwnerHistoryDTO = new PermitRenewalsDTO();
		OminiBusHistoryDTO = new OminiBusDTO();
		applicationHistoryDTO = new PermitRenewalsDTO();
		renderComplainDet=false;
		selectedComplaintDTO = new ComplaintRequestDTO();
		viewSelect = new ComplaintRequestDTO();
	}
/**Public Complain Methods **/
	
	public void viewPublicComplain() {
		selectedComplaintDTO = new ComplaintRequestDTO();
		String viewedApplicationNo = permitDTO.getApplicationNo();
		String selectedPermitNo = permitDTO.getPermitNo();
		String searchedQueueNum = permitDTO.getQueueNo();
		String selectedApplicationNo = permitDTO.getApplicationNo();

		sessionBackingBean.setViewedInspectionApplicationNo(viewedApplicationNo);
		sessionBackingBean.setViewedPermitNo(selectedPermitNo);
		sessionBackingBean.setSelectedQueueNo(searchedQueueNum);
		sessionBackingBean.setApplicationNo(selectedApplicationNo);
		sessionBackingBean.setCheckPermitIssueNewPermit(true);
		
		
		
		
		loadValuesForViewPublicComplaint();
		RequestContext.getCurrentInstance().execute("PF('complaintDlg').show()");
		RequestContext.getCurrentInstance().update("complaintDlg");
	
	}
	
	public void loadValuesForViewPublicComplaint() {
		complainDetViewList=manageInquiryService.getComplaintDetailsForPublicComplain(permitDTO.getPermitNo());
		
	}
	
	
	public void viewAction() {

		
		selectedComplaintDTO =  manageInquiryService.getComplaintDetails(viewSelect.getComplaintNo(), viewSelect.getVehicleNo(), viewSelect.getPermitNo());
		RequestContext.getCurrentInstance().update("dlg-complaint");
		renderComplainDet=true;
	}
	


/** Public Complain Methods End **/	
	
/** investigation pop up **/

	
	public void viewInvestigationPopUp() {
		flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
		conditionList = null;
		documentList = null;
		loadValuesForViewInvestigation();		
		
		RequestContext.getCurrentInstance().execute("PF('showPageDialog').show()");
		RequestContext.getCurrentInstance().update("showPageDialog");
		
	}	
	
	public void viewInvesAction() {		
		flyingManageInvestigationLogDTO =flyingSquadChargeSheetService.getShowDetailsN(viewInvSelect.getInvesNo());
		conditionList =flyingSquadChargeSheetService.getConditionList(viewInvSelect.getInvesNo());
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
		setInvestigationDetViewList(manageInquiryService.getInvestigationDetails(permitDTO.getPermitNo()));
		
	}
		
	
/** investigation pop up end**/	
	
	public void viewGpsDetails() {		
		setSelectedComplaintDTO(new ComplaintRequestDTO());
		String selectedPermitNo = permitRenewalsDTO.getPermitNo();

		sessionBackingBean.setViewedPermitNo(selectedPermitNo);		
		loadValuesForViewGPS();		
		
		RequestContext.getCurrentInstance().execute("PF('GPSModeId').show()");
		RequestContext.getCurrentInstance().update("GPSModeId");
	}
	public void loadValuesForViewGPS() {
		setSimRegistrationDTO(manageInquiryService.getGPSDetails(permitDTO.getPermitNo()));
		if(simRegistrationDTO.getSimRegNo() != null){
		setEmiDetViewList(manageInquiryService.getEmiDetails(simRegistrationDTO.getSimRegNo()));
		}
	}
	public void documentManagmentPage() {
		
		DocumentManagementBackingBean documentManagementBackingBean = new DocumentManagementBackingBean();
		try {
			
			ApplicationList = documentManagementService.RenewalApplicationList(permitDTO.getPermitNo());
			
			
			RenewalApplicationList = documentManagementService.ConfirmRenewalApplicationList(ApplicationList);

			mandatoryList = documentManagementService.mandatoryViewDocs("04", permitDTO.getPermitNo());

			optionalList = documentManagementService.optionalViewDocs("04", permitDTO.getPermitNo());

			sessionBackingBean.newPermitMandatoryDocumentList = documentManagementService
					.newPermitMandatoryList(permitDTO.getPermitNo());
			sessionBackingBean.newPermitOptionalDocumentList = documentManagementService
					.newPermitOptionalList(permitDTO.getPermitNo());
			
		
				
			sessionBackingBean.permitRenewalMandatoryDocumentList = documentManagementService
					.permitRenewalMandatoryNewList(permitDTO.getPermitNo(),RenewalApplicationList);
			sessionBackingBean.permitRenewalOptionalDocumentList = documentManagementService
					.permitRenewalOptionalNewList(permitDTO.getPermitNo(),RenewalApplicationList);
			
			
			
			sessionBackingBean.backlogManagementOptionalDocumentList = documentManagementService
					.backlogManagementOptionalList(permitDTO.getPermitNo());
			sessionBackingBean.amendmentToBusOwnerMandatoryDocumentList = documentManagementService
					.amendmentToBusOwnerMandatoryList(permitDTO.getPermitNo());
			sessionBackingBean.amendmentToBusOwnerOptionalDocumentList = documentManagementService
					.amendmentToBusOwnerOptionalList(permitDTO.getPermitNo());
			sessionBackingBean.amendmentToBusMandatoryDocumentList = documentManagementService
					.amendmentToBusMandatoryList(permitDTO.getPermitNo());
			sessionBackingBean.amendmentToBusOptionalDocumentList = documentManagementService
					.amendmentToBusOptionalList(permitDTO.getPermitNo());
			sessionBackingBean.amendmentToOwnerBusMandatoryDocumentList = documentManagementService
					.amendmentToOwnerBusMandatoryList(permitDTO.getPermitNo());
			sessionBackingBean.amendmentToOwnerBusOptionalDocumentList = documentManagementService
					.amendmentToOwnerBusOptionalList(permitDTO.getPermitNo());
			sessionBackingBean.amendmentToServiceBusMandatoryDocumentList = documentManagementService
					.amendmentToServiceBusMandatoryList(permitDTO.getPermitNo());
			sessionBackingBean.amendmentToServiceBusOptionalDocumentList = documentManagementService
					.amendmentToServiceBusOptionalList(permitDTO.getPermitNo());
			sessionBackingBean.amendmentToServiceMandatoryDocumentList = documentManagementService
					.amendmentToServiceMandatoryList(permitDTO.getPermitNo());
			sessionBackingBean.amendmentToServiceOptionalDocumentList = documentManagementService
					.amendmentToServiceOptionalList(permitDTO.getPermitNo());

			RequestContext.getCurrentInstance().execute("PF('uploadDocument').show()");

		} catch (Exception e) {
			e.printStackTrace();
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

	public void clearCheckDocumentsTable() {
		
		displayCheckDocumentDataList();
	}

	
	/*
	 * GPS Integration
	 * 26-03-2020
	 * Ranjaka Liyanage
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

		if (permitDTO.getBusRegNo() != null) {
			busNo = permitDTO.getBusRegNo();
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

        URI newUri = new URI(oldUri.getScheme(), oldUri.getAuthority(),
                oldUri.getPath(), newQuery, oldUri.getFragment());

        return newUri;
    }
	
	
	public List<PermitRenewalsDTO> getDataList() {
		return dataList;
	}

	public void setDataList(List<PermitRenewalsDTO> dataList) {
		this.dataList = dataList;
	}

	public PermitRenewalsDTO getPermitRenewalsDTO() {
		return permitRenewalsDTO;
	}

	public void setPermitRenewalsDTO(PermitRenewalsDTO permitRenewalsDTO) {
		this.permitRenewalsDTO = permitRenewalsDTO;
	}

	public PermitRenewalsDTO getCheckAndDisplayRemarkValue() {
		return checkAndDisplayRemarkValue;
	}

	public void setCheckAndDisplayRemarkValue(PermitRenewalsDTO checkAndDisplayRemarkValue) {
		this.checkAndDisplayRemarkValue = checkAndDisplayRemarkValue;
	}

	public PermitRenewalsService getPermitRenewalsService() {
		return permitRenewalsService;
	}

	public void setPermitRenewalsService(PermitRenewalsService permitRenewalsService) {
		this.permitRenewalsService = permitRenewalsService;
	}

	public String getNewExpireDate() {
		return newExpireDate;
	}

	public void setNewExpireDate(String newExpireDate) {
		this.newExpireDate = newExpireDate;
	}

	public boolean isDissableNameSinhala() {
		return dissableNameSinhala;
	}

	public void setDissableNameSinhala(boolean dissableNameSinhala) {
		this.dissableNameSinhala = dissableNameSinhala;
	}

	public String getSelectdivsec() {
		return selectdivsec;
	}

	public void setSelectdivsec(String selectdivsec) {
		this.selectdivsec = selectdivsec;
	}

	public String getSelectAddres1_sin() {
		return selectAddres1_sin;
	}

	public boolean isRenderedMood() {
		return renderedMood;
	}

	public void setRenderedMood(boolean renderedMood) {
		this.renderedMood = renderedMood;
	}

	public PaymentVoucherDTO getPaymentVoucherDTO() {
		return paymentVoucherDTO;
	}

	public void setPaymentVoucherDTO(PaymentVoucherDTO paymentVoucherDTO) {
		this.paymentVoucherDTO = paymentVoucherDTO;
	}

	public void setSelectAddres1_sin(String selectAddres1_sin) {
		this.selectAddres1_sin = selectAddres1_sin;
	}

	public String getSelectAddres1_tamil() {
		return selectAddres1_tamil;
	}

	public void setSelectAddres1_tamil(String selectAddres1_tamil) {
		this.selectAddres1_tamil = selectAddres1_tamil;
	}

	public String getSelectAddres2_sin() {
		return selectAddres2_sin;
	}

	public void setSelectAddres2_sin(String selectAddres2_sin) {
		this.selectAddres2_sin = selectAddres2_sin;
	}

	public String getSelectAddres2_tamil() {
		return selectAddres2_tamil;
	}

	public void setSelectAddres2_tamil(String selectAddres2_tamil) {
		this.selectAddres2_tamil = selectAddres2_tamil;
	}

	public String getSelectNameFull_sin() {
		return selectNameFull_sin;
	}

	public void setSelectNameFull_sin(String selectNameFull_sin) {
		this.selectNameFull_sin = selectNameFull_sin;
	}

	public String getSelectNameFull_tamil() {
		return selectNameFull_tamil;
	}

	public void setSelectNameFull_tamil(String selectNameFull_tamil) {
		this.selectNameFull_tamil = selectNameFull_tamil;
	}

	public boolean isShowInquiryBackButton() {
		return showInquiryBackButton;
	}

	public void setShowInquiryBackButton(boolean showInquiryBackButton) {
		this.showInquiryBackButton = showInquiryBackButton;
	}

	public String getSelectCity_sin() {
		return selectCity_sin;
	}

	public void setSelectCity_sin(String selectCity_sin) {
		this.selectCity_sin = selectCity_sin;
	}

	public String getSelectCity_tamil() {
		return selectCity_tamil;
	}

	public void setSelectCity_tamil(String selectCity_tamil) {
		this.selectCity_tamil = selectCity_tamil;
	}

	public String getSelecPreferLanguage() {
		return selecPreferLanguage;
	}

	public void setSelecPreferLanguage(String selecPreferLanguage) {
		this.selecPreferLanguage = selecPreferLanguage;
	}

	public String getSelectTitle() {
		return selectTitle;
	}

	public void setSelectTitle(String selectTitle) {
		this.selectTitle = selectTitle;
	}

	public String getSelectGender() {
		return selectGender;
	}

	public void setSelectGender(String selectGender) {
		this.selectGender = selectGender;
	}

	public String getSelectNIC() {
		return selectNIC;
	}

	public void setSelectNIC(String selectNIC) {
		this.selectNIC = selectNIC;
	}

	public String getSelectNameFull() {
		return selectNameFull;
	}

	public void setSelectNameFull(String selectNameFull) {
		this.selectNameFull = selectNameFull;
	}

	public String getSlectNameIniti() {
		return slectNameIniti;
	}

	public void setSlectNameIniti(String slectNameIniti) {
		this.slectNameIniti = slectNameIniti;
	}

	public String getSelectMaritalStatus() {
		return selectMaritalStatus;
	}

	public void setSelectMaritalStatus(String selectMaritalStatus) {
		this.selectMaritalStatus = selectMaritalStatus;
	}

	public String getSelectTelePhone() {
		return selectTelePhone;
	}

	public void setSelectTelePhone(String selectTelePhone) {
		this.selectTelePhone = selectTelePhone;
	}

	public String getSelectMobile() {
		return selectMobile;
	}

	public void setSelectMobile(String selectMobile) {
		this.selectMobile = selectMobile;
	}

	public String getSelectProvince() {
		return selectProvince;
	}

	public void setSelectProvince(String selectProvince) {
		this.selectProvince = selectProvince;
	}

	public String getSelectDistrict() {
		return selectDistrict;
	}

	public void setSelectDistrict(String selectDistrict) {
		this.selectDistrict = selectDistrict;
	}

	public String getSelectAddres1() {
		return selectAddres1;
	}

	public void setSelectAddres1(String selectAddres1) {
		this.selectAddres1 = selectAddres1;
	}

	public String getSelectAddres2() {
		return selectAddres2;
	}

	public void setSelectAddres2(String selectAddres2) {
		this.selectAddres2 = selectAddres2;
	}

	public String getSelectCity() {
		return selectCity;
	}

	public void setSelectCity(String selectCity) {
		this.selectCity = selectCity;
	}

	public PermitRenewalsDTO getViewData() {
		return viewData;
	}

	public void setViewData(PermitRenewalsDTO viewData) {
		this.viewData = viewData;
	}

	public String getStrSelectedServicetype() {
		return strSelectedServicetype;
	}

	public void setStrSelectedServicetype(String strSelectedServicetype) {
		this.strSelectedServicetype = strSelectedServicetype;
	}

	public String getStrSelectedRouteNO() {
		return strSelectedRouteNO;
	}

	public void setStrSelectedRouteNO(String strSelectedRouteNO) {
		this.strSelectedRouteNO = strSelectedRouteNO;
	}

	public String getStrselectedexpireDate() {
		return strselectedexpireDate;
	}

	public void setStrselectedexpireDate(String strselectedexpireDate) {
		this.strselectedexpireDate = strselectedexpireDate;
	}

	public String getStrSelectedOrigin() {
		return strSelectedOrigin;
	}

	public void setStrSelectedOrigin(String strSelectedOrigin) {
		this.strSelectedOrigin = strSelectedOrigin;
	}

	public String getStrSeletedDesination() {
		return strSeletedDesination;
	}

	public void setStrSeletedDesination(String strSeletedDesination) {
		this.strSeletedDesination = strSeletedDesination;
	}

	public String getStrSelectedPermitNo() {
		return strSelectedPermitNo;
	}

	public void setStrSelectedPermitNo(String strSelectedPermitNo) {
		this.strSelectedPermitNo = strSelectedPermitNo;
	}

	public String getStrSelectedApplicationNo() {
		return strSelectedApplicationNo;
	}

	public void setStrSelectedApplicationNo(String strSelectedApplicationNo) {
		this.strSelectedApplicationNo = strSelectedApplicationNo;
	}

	public String getStrSelectedQueueNo() {
		return strSelectedQueueNo;
	}

	public void setStrSelectedQueueNo(String strSelectedQueueNo) {
		this.strSelectedQueueNo = strSelectedQueueNo;
	}

	public PermitService getPermitservice() {
		return permitservice;
	}

	public PermitDTO getViewPermitList() {
		return viewPermitList;
	}

	public void setViewPermitList(PermitDTO viewPermitList) {
		this.viewPermitList = viewPermitList;
	}

	public void setPermitservice(PermitService permitservice) {
		this.permitservice = permitservice;
	}


	public String getBusRegNo() {
		return busRegNo;
	}

	public void setBusRegNo(String busRegNo) {
		this.busRegNo = busRegNo;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public String getPermitNo() {
		return permitNo;
	}

	public void setPermitNo(String permitNo) {
		this.permitNo = permitNo;
	}

	public String getQueueNo() {
		return queueNo;
	}

	public void setQueueNo(String queueNo) {
		this.queueNo = queueNo;
	}

	public PermitDTO getPermitDTO() {
		return permitDTO;
	}

	public void setPermitDTO(PermitDTO permitDTO) {
		this.permitDTO = permitDTO;
	}

	public String getMyDate() {
		return myDate;
	}

	public void setMyDate(String myDate) {
		this.myDate = myDate;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public BusOwnerDTO getBusownerDTO() {
		return busownerDTO;
	}

	public void setBusownerDTO(BusOwnerDTO busownerDTO) {
		this.busownerDTO = busownerDTO;
	}

	public String getSelecDOB() {
		return selecDOB;
	}

	public void setSelecDOB(String selecDOB) {
		this.selecDOB = selecDOB;
	}

	public boolean isDissableNameTamil() {
		return dissableNameTamil;
	}

	public void setDissableNameTamil(boolean dissableNameTamil) {
		this.dissableNameTamil = dissableNameTamil;
	}

	public boolean isDissableAddrs1Sinhla() {
		return dissableAddrs1Sinhla;
	}

	public void setDissableAddrs1Sinhla(boolean dissableAddrs1Sinhla) {
		this.dissableAddrs1Sinhla = dissableAddrs1Sinhla;
	}

	public boolean isDissableAddrs1Tamil() {
		return dissableAddrs1Tamil;
	}

	public void setDissableAddrs1Tamil(boolean dissableAddrs1Tamil) {
		this.dissableAddrs1Tamil = dissableAddrs1Tamil;
	}

	public boolean isDissableAddrs2Sinhala() {
		return dissableAddrs2Sinhala;
	}

	public boolean isRenewalTyp() {
		return renewalTyp;
	}

	public void setRenewalTyp(boolean renewalTyp) {
		this.renewalTyp = renewalTyp;
	}

	public boolean isInquaryType() {
		return inquaryType;
	}

	public void setInquaryType(boolean inquaryType) {
		this.inquaryType = inquaryType;
	}

	public void setDissableAddrs2Sinhala(boolean dissableAddrs2Sinhala) {
		this.dissableAddrs2Sinhala = dissableAddrs2Sinhala;
	}

	public boolean isDissableAddrs2Tamil() {
		return dissableAddrs2Tamil;
	}

	public void setDissableAddrs2Tamil(boolean dissableAddrs2Tamil) {
		this.dissableAddrs2Tamil = dissableAddrs2Tamil;
	}

	public boolean isDissableCitySinhala() {
		return dissableCitySinhala;
	}

	public void setDissableCitySinhala(boolean dissableCitySinhala) {
		this.dissableCitySinhala = dissableCitySinhala;
	}

	public boolean isDissableCityTamil() {
		return dissableCityTamil;
	}

	public void setDissableCityTamil(boolean dissableCityTamil) {
		this.dissableCityTamil = dissableCityTamil;
	}

	public int getSelectedRenewdPeriod() {
		return selectedRenewdPeriod;
	}

	public void setSelectedRenewdPeriod(int selectedRenewdPeriod) {
		this.selectedRenewdPeriod = selectedRenewdPeriod;
	}

	public String getSelectedRemarks() {
		return selectedRemarks;
	}

	public void setSelectedRemarks(String selectedRemarks) {
		this.selectedRemarks = selectedRemarks;
	}

	public int getActiveTabIndex() {
		return activeTabIndex;
	}

	public void setActiveTabIndex(int activeTabIndex) {
		this.activeTabIndex = activeTabIndex;
	}

	public String getViewInspectionURL() {
		return viewInspectionURL;
	}

	public void setViewInspectionURL(String viewInspectionURL) {
		this.viewInspectionURL = viewInspectionURL;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
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

	public List<PermitRenewalsDTO> getPaymentHistoryDataList() {
		return paymentHistoryDataList;
	}

	public void setPaymentHistoryDataList(List<PermitRenewalsDTO> paymentHistoryDataList) {
		this.paymentHistoryDataList = paymentHistoryDataList;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getSelectNewExpire() {
		return selectNewExpire;
	}

	public void setSelectNewExpire(String selectNewExpire) {
		this.selectNewExpire = selectNewExpire;
	}

	public List<PermitRenewalsDTO> getGetApplicationNo() {
		return getApplicationNo;
	}

	public void setGetApplicationNo(List<PermitRenewalsDTO> getApplicationNo) {
		this.getApplicationNo = getApplicationNo;
	}

	public List<PermitRenewalsDTO> getGetPermitNo() {
		return getPermitNo;
	}

	public void setGetPermitNo(List<PermitRenewalsDTO> getPermitNo) {
		this.getPermitNo = getPermitNo;
	}

	public boolean isDisabledPermitNo() {
		return disabledPermitNo;
	}

	public void setDisabledPermitNo(boolean disabledPermitNo) {
		this.disabledPermitNo = disabledPermitNo;
	}

	public boolean isDisabledApplicationNo() {
		return disabledApplicationNo;
	}

	public void setDisabledApplicationNo(boolean disabledApplicationNo) {
		this.disabledApplicationNo = disabledApplicationNo;
	}

	public boolean isDisabledSearchBtn() {
		return disabledSearchBtn;
	}

	public void setDisabledSearchBtn(boolean disabledSearchBtn) {
		this.disabledSearchBtn = disabledSearchBtn;
	}

	public boolean isShowSearchedDetailsForm() {
		return showSearchedDetailsForm;
	}

	public void setShowSearchedDetailsForm(boolean showSearchedDetailsForm) {
		this.showSearchedDetailsForm = showSearchedDetailsForm;
	}

	public boolean isCheckedGotoVehicleInspectionViewMode() {
		return isCheckedGotoVehicleInspectionViewMode;
	}

	public void setCheckedGotoVehicleInspectionViewMode(boolean isCheckedGotoVehicleInspectionViewMode) {
		this.isCheckedGotoVehicleInspectionViewMode = isCheckedGotoVehicleInspectionViewMode;
	}

	public boolean isDisabledVehicleInspection() {
		return disabledVehicleInspection;
	}

	public void setDisabledVehicleInspection(boolean disabledVehicleInspection) {
		this.disabledVehicleInspection = disabledVehicleInspection;
	}

	public boolean isShowRenewalsBackButton() {
		return showRenewalsBackButton;
	}

	public void setShowRenewalsBackButton(boolean showRenewalsBackButton) {
		this.showRenewalsBackButton = showRenewalsBackButton;
	}

	public List<PermitRenewalsDTO> getCurrentPermitNoList() {
		return currentPermitNoList;
	}

	public void setCurrentPermitNoList(List<PermitRenewalsDTO> currentPermitNoList) {
		this.currentPermitNoList = currentPermitNoList;
	}

	public PermitDTO getLoadValuesForSelectedOneDTO() {
		return loadValuesForSelectedOneDTO;
	}

	public void setLoadValuesForSelectedOneDTO(PermitDTO loadValuesForSelectedOneDTO) {
		this.loadValuesForSelectedOneDTO = loadValuesForSelectedOneDTO;
	}

	public OminiBusDTO getOminiBusDTO() {
		return ominiBusDTO;
	}

	public void setOminiBusDTO(OminiBusDTO ominiBusDTO) {
		this.ominiBusDTO = ominiBusDTO;
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

	public IssuePermitDTO getIssuePermitDTO() {
		return issuePermitDTO;
	}

	public void setIssuePermitDTO(IssuePermitDTO issuePermitDTO) {
		this.issuePermitDTO = issuePermitDTO;
	}

	public OminiBusDTO getInspectionDetails() {
		return inspectionDetails;
	}

	public void setInspectionDetails(OminiBusDTO inspectionDetails) {
		this.inspectionDetails = inspectionDetails;
	}

	public InspectionActionPointService getInspectionActionPointService() {
		return inspectionActionPointService;
	}

	public void setInspectionActionPointService(InspectionActionPointService inspectionActionPointService) {
		this.inspectionActionPointService = inspectionActionPointService;
	}

	public RouteDTO getRouteDTO() {
		return routeDTO;
	}

	public void setRouteDTO(RouteDTO routeDTO) {
		this.routeDTO = routeDTO;
	}

	public IssuePermitService getIssuePermitService() {
		return issuePermitService;
	}

	public void setIssuePermitService(IssuePermitService issuePermitService) {
		this.issuePermitService = issuePermitService;
	}

	public HistoryService getHistoryService() {
		return historyService;
	}

	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}

	public List<VehicleInspectionDTO> getDataListViewInspection() {
		return dataListViewInspection;
	}

	public void setDataListViewInspection(List<VehicleInspectionDTO> dataListViewInspection) {
		this.dataListViewInspection = dataListViewInspection;
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

	public UploadImageDTO getUploadImageDTO() {
		return uploadImageDTO;
	}

	public void setUploadImageDTO(UploadImageDTO uploadImageDTO) {
		this.uploadImageDTO = uploadImageDTO;
	}

	public IssuePermitDTO getFilteredValuesDTO() {
		return filteredValuesDTO;
	}

	public void setFilteredValuesDTO(IssuePermitDTO filteredValuesDTO) {
		this.filteredValuesDTO = filteredValuesDTO;
	}

	public VehicleInspectionDTO getViewedDetails() {
		return viewedDetails;
	}

	public void setViewedDetails(VehicleInspectionDTO viewedDetails) {
		this.viewedDetails = viewedDetails;
	}

	public ComplaintRequestDTO getPublicComplanDto() {
		return publicComplanDto;
	}

	public void setPublicComplanDto(ComplaintRequestDTO publicComplanDto) {
		this.publicComplanDto = publicComplanDto;
	}


	public ManageInquiryService getManageInquiryService() {
		return manageInquiryService;
	}

	public void setManageInquiryService(ManageInquiryService manageInquiryService) {
		this.manageInquiryService = manageInquiryService;
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



}