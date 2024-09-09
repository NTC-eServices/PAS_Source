package lk.informatics.ntc.view.beans;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.BusOwnerDTO;
import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.DocumentManagementDTO;
import lk.informatics.ntc.model.dto.IssuePermitDTO;
import lk.informatics.ntc.model.dto.OminiBusDTO;
import lk.informatics.ntc.model.dto.PaymentVoucherDTO;
import lk.informatics.ntc.model.dto.PermitRenewalsDTO;
import lk.informatics.ntc.model.dto.RouteDTO;
import lk.informatics.ntc.model.service.AdminService;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.DocumentManagementService;
import lk.informatics.ntc.model.service.EmployeeProfileService;
import lk.informatics.ntc.model.service.IssuePermitService;
import lk.informatics.ntc.model.service.QueueManagementService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "viewNewPermitBackingBean")
@ViewScoped
public class ViewNewPermitBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private IssuePermitService issuePermitService;
	private AdminService adminService;
	private EmployeeProfileService employeeProfileService;
	private CommonService commonService;
	private boolean inquiryViewMode = true;
	private CommonDTO commonDTO;
	private IssuePermitDTO issuePermitDTO;
	private BusOwnerDTO busOwnerDTO;
	private OminiBusDTO ominiBusDTO;
	private RouteDTO routeDTO;
	private PaymentVoucherDTO paymentVoucherDTO;
	private PermitRenewalsDTO checkAndDisplayRemarkValue;
	private QueueManagementService queueManagementService;
	private DocumentManagementService documentManagementService;

	boolean check = true;
	boolean callNext = false;
	boolean skip = true;
	private boolean localcheckcounter = true;
	private boolean backToPrinting = false;

	private int activeTabIndex;

	private boolean next;

	private boolean applicationFound;
	private boolean loanObtained, noPermitIssued;
	private boolean showInquiryBackButton = false;

	private String strSelectedTitle;
	private String strSelectedGender;
	private String strSelectedMartial;
	private String strSelectedProvince;
	private String strSelectedDistrict;
	private String strSelectedDivSec;
	private String strSelectedLanguage;
	private String strNicNo;

	private String strTenderRefNo;
	private String strCurrentDate;
	private String strApplicationNo;
	private String strQueueNo;
	private String strServiceType;
	private String strBusRegNo;
	private String strPermitNo;
	private String strSelectedRoute;

	private String errorMsg;

	private List<CommonDTO> titleList;
	private List<CommonDTO> genderList;
	private List<CommonDTO> martialList;
	private List<CommonDTO> provincelList;
	private List<CommonDTO> districtList;
	private List<CommonDTO> divSecList;
	private List<CommonDTO> routefordropdownList;
	private List<CommonDTO> allDivSecList;
	private List<CommonDTO> counterList;

	public List<PermitRenewalsDTO> dataList = new ArrayList<PermitRenewalsDTO>(0);
	public List<PermitRenewalsDTO> checkDataList = new ArrayList<PermitRenewalsDTO>(0);

	private List<DocumentManagementDTO> mandatoryList = new ArrayList<DocumentManagementDTO>();
	private List<DocumentManagementDTO> optionalList = new ArrayList<DocumentManagementDTO>();

	private Boolean routeFlag;

	public ViewNewPermitBackingBean() {
		issuePermitService = (IssuePermitService) SpringApplicationContex.getBean("issuePermitService");
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");
		employeeProfileService = (EmployeeProfileService) SpringApplicationContex.getBean("employeeProfileService");
		queueManagementService = (QueueManagementService) SpringApplicationContex.getBean("queueManagementService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		documentManagementService = (DocumentManagementService) SpringApplicationContex
				.getBean("documentManagementService");
		showInquiryBackButton = false;
	}

	@PostConstruct
	public void init() {

		activeTabIndex = 0;
		issuePermitDTO = new IssuePermitDTO();
		busOwnerDTO = new BusOwnerDTO();
		ominiBusDTO = new OminiBusDTO();
		routeDTO = new RouteDTO();
		commonDTO = new CommonDTO();
		paymentVoucherDTO = new PaymentVoucherDTO();
		checkAndDisplayRemarkValue = new PermitRenewalsDTO();

		setNext(false);
		setApplicationFound(false);
		setLoanObtained(false);

		setCallNext(false);
		setSkip(true);
		setBackToPrinting(false);
		localcheckcounter = sessionBackingBean.isCounterCheck();
		loadValues();
		inquiryViewMode = true;
		showInquiryBackButton = false;
		RequestContext.getCurrentInstance().update("btnBack");
		FacesContext fcontext = FacesContext.getCurrentInstance();
		Object objCallerApplicationNo = fcontext.getExternalContext().getSessionMap().get("APPLICATION_NO");
		if (objCallerApplicationNo != null) {
			strApplicationNo = (String) objCallerApplicationNo;
			searchApplication();
			setBackToPrinting(true);
			fcontext.getExternalContext().getSessionMap().put("APPLICATION_NO", null);
		}

		if (sessionBackingBean.isClicked == true) {

			inquiryViewMode = false;
			setStrApplicationNo(sessionBackingBean.getApplicationNo());
			searchApplication();
			showInquiryBackButton = true;
			RequestContext.getCurrentInstance().update("btnBack");
			RequestContext.getCurrentInstance().update("panelOne");

			errorMsg = "Permit not issued";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			sessionBackingBean.isClicked = false;
		}
	}

	public void loadValues() {

		titleList = employeeProfileService.GetTitleToDropdown();
		genderList = employeeProfileService.GetGenderToDropdown();
		martialList = employeeProfileService.GetMartialToDropdown();

		provincelList = adminService.getProvinceToDropdown();
		allDivSecList = adminService.getDivSecToDropdown();

		routefordropdownList = issuePermitService.getRoutesToDropdown();

	}

	public void searchApplication() {

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		strCurrentDate = dateFormat.format(date).toString();

		if (strApplicationNo != null && strApplicationNo != " " && !strApplicationNo.isEmpty()) {
			issuePermitDTO = issuePermitService.getApplicationDetailsByApplicationNo(strApplicationNo);

			strTenderRefNo = issuePermitDTO.getTenderRefNo();
			strApplicationNo = issuePermitDTO.getApplicationNo();
			strQueueNo = issuePermitDTO.getQueueNo();
			strServiceType = issuePermitDTO.getServiceType();
			strBusRegNo = issuePermitDTO.getBusRegNo();
			strPermitNo = issuePermitDTO.getPermitNo();
			strSelectedRoute = issuePermitDTO.getRouteNo();

			if (issuePermitDTO.getPermitNo() != null && issuePermitDTO.getPermitNo() != " "
					&& !issuePermitDTO.getPermitNo().isEmpty()) {

				setApplicationFound(true);
				getOwnerDetailsByPermitNo();
				getOminiBusDetails();
				getServiceDetails();
				strSelectedRoute = issuePermitDTO.getRouteNo();

				displayCheckDocumentDataList();
			} else {
				errorMsg = "Permit not issued";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				noPermitIssued = true;
			}

		} else if (strBusRegNo != null && strBusRegNo != " " && !strBusRegNo.isEmpty()) {
			issuePermitDTO = issuePermitService.getApplicationDetailsByVehicleNo(strBusRegNo);

			strTenderRefNo = issuePermitDTO.getTenderRefNo();
			strApplicationNo = issuePermitDTO.getApplicationNo();
			strQueueNo = issuePermitDTO.getQueueNo();
			strServiceType = issuePermitDTO.getServiceType();
			strBusRegNo = issuePermitDTO.getBusRegNo();
			strPermitNo = issuePermitDTO.getPermitNo();
			strSelectedRoute = issuePermitDTO.getRouteNo();

			if (issuePermitDTO.getPermitNo() != null && issuePermitDTO.getPermitNo() != " "
					&& !issuePermitDTO.getPermitNo().isEmpty()) {
				setApplicationFound(true);
				getOwnerDetailsByPermitNo();
				getOminiBusDetails();
				getServiceDetails();
				strSelectedRoute = issuePermitDTO.getRouteNo();

				displayCheckDocumentDataList();
			} else {
				errorMsg = "Permit not issued";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				noPermitIssued = true;
			}

		} else {
			strCurrentDate = null;
			errorMsg = "Bus Registration No. or Application No. is required";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		}

	}

	public void backToInquiry() {

		String approveURL = sessionBackingBean.getApproveURL();
		String searchURL = sessionBackingBean.getSearchURL();

		if (approveURL != null) {
			
			try {
				sessionBackingBean.setApproveURLStatus(false);
				sessionBackingBean.isClicked = false;
				FacesContext.getCurrentInstance().getExternalContext().redirect(approveURL);
			} catch (IOException e) {

				e.printStackTrace();
			}

		} else if (searchURL != null) {

			try {
				sessionBackingBean.setSearchURLStatus(false);
				sessionBackingBean.isClicked = false;
				FacesContext.getCurrentInstance().getExternalContext().redirect(searchURL);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		setShowInquiryBackButton(false);
		RequestContext.getCurrentInstance().update("btnBack");
	}

	public void getOwnerDetailsByPermitNo() {
		if (issuePermitDTO.getApplicationNo() != null && !issuePermitDTO.getApplicationNo().isEmpty()
				&& issuePermitDTO.getApplicationNo() != "") {
			busOwnerDTO = issuePermitService.getOwnerDetailsByApplicationNo(issuePermitDTO.getApplicationNo());

			if (busOwnerDTO.getTitle() != null && busOwnerDTO.getTitle() != " " && !busOwnerDTO.getTitle().isEmpty()) {
				strSelectedTitle = busOwnerDTO.getTitle();
				strSelectedMartial = busOwnerDTO.getMaritalStatus();
				strSelectedGender = busOwnerDTO.getGender();
				strSelectedDivSec = busOwnerDTO.getDivSec();
				strSelectedDistrict = busOwnerDTO.getDistrict();
				strSelectedProvince = busOwnerDTO.getProvince();
				strSelectedLanguage = busOwnerDTO.getPerferedLanguage();
				strPermitNo = issuePermitDTO.getPermitNo();
				strNicNo = busOwnerDTO.getNicNo();

				onProvinceChange();
				onDistrictChange();
			}
		}
	}

	public void onDistrictChange() {
		if (strSelectedDistrict != null && !strSelectedDistrict.isEmpty()) {
			divSecList = adminService.getDivSecByDistrictToDropdown(strSelectedDistrict);
		}
	}

	public void onProvinceChange() {
		if (strSelectedProvince != null && !strSelectedProvince.isEmpty()) {
			districtList = adminService.getDistrictByProvinceToDropdown(strSelectedProvince);
		}
	}

	public void clearAll() {

		strTenderRefNo = null;
		strCurrentDate = null;
		strApplicationNo = null;
		strQueueNo = null;
		strServiceType = null;
		strBusRegNo = null;
		strSelectedRoute = null;

		errorMsg = null;

		clearOwnerDetails();
	}

	public void clearOwnerDetails() {
		strSelectedTitle = null;
		strSelectedGender = null;
		strSelectedMartial = null;
		strSelectedProvince = null;
		strSelectedDistrict = null;
		strSelectedDivSec = null;
		strSelectedLanguage = null;
		strNicNo = null;
		strPermitNo = null;

		init();

		setNext(false);
	}

	public void getOminiBusDetails() {
		if (issuePermitDTO.getPermitNo() != null && !issuePermitDTO.getPermitNo().isEmpty()
				&& issuePermitDTO.getPermitNo() != "") {
			ominiBusDTO = issuePermitService.ominiBusByVehicleNo(issuePermitDTO.getBusRegNo());

			if (ominiBusDTO.getIsLoanObtained() != null && !ominiBusDTO.getIsLoanObtained().isEmpty()
					&& !ominiBusDTO.getIsLoanObtained().equals("")) {
				if (ominiBusDTO.getIsLoanObtained().equals("Y")) {

					setLoanObtained(true);
				} else if (ominiBusDTO.getIsLoanObtained().equals("N")) {

					setLoanObtained(false);
				} else {
					setLoanObtained(false);
				}
			} else
				setLoanObtained(false);

		}
	}

	public void getServiceDetails() {
		if (issuePermitDTO.getServiceSeq() >= 0) {
			String flag = issuePermitDTO.getRouteFlag();
			issuePermitDTO = issuePermitService.serviceDetailsByPermitNo(issuePermitDTO.getPermitNo());
			strSelectedRoute = issuePermitDTO.getRouteNo();
			onRouteChange();
			if (flag != null && !flag.trim().equals("") && flag.equalsIgnoreCase("Y")) {
				routeFlag = true;
				routeFlagListener();

			}

		}

	}

	public void onRouteChange() {
		issuePermitDTO.setRouteNo(strSelectedRoute);

		if (issuePermitDTO.getRouteNo() != null && !issuePermitDTO.getRouteNo().equals("")) {
			routeDTO = issuePermitService.getDetailsbyRouteNo(issuePermitDTO.getRouteNo());
			if (routeDTO.getOrigin() != null && !routeDTO.getOrigin().equals("")) {
				issuePermitDTO.setVia(routeDTO.getVia());
				issuePermitDTO.setDestination(routeDTO.getDestination());
				issuePermitDTO.setOrigin(routeDTO.getOrigin());
				issuePermitDTO.setDistance(routeDTO.getDistance());

			}
		} else {
			routeDTO = null;
		}
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

	public void displayCheckDocumentDataList() {
		dataList = issuePermitService.getAllRecordsForDocumentsCheckings();
		for (int i = 0; i < dataList.size(); i++) {

			String currentDocCode = dataList.get(i).getDocumentCode();
			String currentDocCodeDescription = dataList.get(i).getDocumentDescription();
			// checkDataList=permitRenewalsService.checkIsSumbiited(currentDocCode,currentDocCodeDescription);
			boolean resultValue = issuePermitService.checkIsSumbiited(currentDocCode, strApplicationNo, strPermitNo);
			boolean isMandatory = issuePermitService.isMandatory(currentDocCode, strApplicationNo, strPermitNo);
			boolean isPhysicallyExit = issuePermitService.isPhysicallyExit(currentDocCode, strApplicationNo,
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
			checkAndDisplayRemarkValue = issuePermitService.getRemarkDetails(currentDocCode, strApplicationNo,
					strPermitNo);
			dataList.get(i).setDocSeqChecked(checkAndDisplayRemarkValue.getDocSeqChecked());
			dataList.get(i).setRemark(checkAndDisplayRemarkValue.getRemark());
			dataList.get(i).setDocFilePath(checkAndDisplayRemarkValue.getDocFilePath());
		}
	}

	public void backToPrinting() {

		String viewNewPermitURL = sessionBackingBean.getViewNewPermitURL();

		if (viewNewPermitURL != null) {

			try {
				sessionBackingBean.setViewNewPermitURLStatus(false);
				setBackToPrinting(false);
				FacesContext fcontext = FacesContext.getCurrentInstance();
				fcontext.getExternalContext().getSessionMap().put("APPLICATION_NO", issuePermitDTO.getApplicationNo());

				FacesContext.getCurrentInstance().getExternalContext().redirect(viewNewPermitURL);
			} catch (IOException e) {

				e.printStackTrace();
			}

		}

	}

	public void documentManagmentPage() {

		DocumentManagementBackingBean documentManagementBackingBean = new DocumentManagementBackingBean();
		try {

			mandatoryList = documentManagementService.mandatoryViewDocs("03", strPermitNo);

			optionalList = documentManagementService.optionalViewDocs("03", strPermitNo);

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

			RequestContext.getCurrentInstance().execute("PF('uploadDocument').show()");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public IssuePermitService getIssuePermitService() {
		return issuePermitService;
	}

	public void setIssuePermitService(IssuePermitService issuePermitService) {
		this.issuePermitService = issuePermitService;
	}

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	public EmployeeProfileService getEmployeeProfileService() {
		return employeeProfileService;
	}

	public void setEmployeeProfileService(EmployeeProfileService employeeProfileService) {
		this.employeeProfileService = employeeProfileService;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public boolean isNoPermitIssued() {
		return noPermitIssued;
	}

	public void setNoPermitIssued(boolean noPermitIssued) {
		this.noPermitIssued = noPermitIssued;
	}

	public CommonDTO getCommonDTO() {
		return commonDTO;
	}

	public void setCommonDTO(CommonDTO commonDTO) {
		this.commonDTO = commonDTO;
	}

	public IssuePermitDTO getIssuePermitDTO() {
		return issuePermitDTO;
	}

	public void setIssuePermitDTO(IssuePermitDTO issuePermitDTO) {
		this.issuePermitDTO = issuePermitDTO;
	}

	public BusOwnerDTO getBusOwnerDTO() {
		return busOwnerDTO;
	}

	public void setBusOwnerDTO(BusOwnerDTO busOwnerDTO) {
		this.busOwnerDTO = busOwnerDTO;
	}

	public OminiBusDTO getOminiBusDTO() {
		return ominiBusDTO;
	}

	public boolean isShowInquiryBackButton() {
		return showInquiryBackButton;
	}

	public void setShowInquiryBackButton(boolean showInquiryBackButton) {
		this.showInquiryBackButton = showInquiryBackButton;
	}

	public void setOminiBusDTO(OminiBusDTO ominiBusDTO) {
		this.ominiBusDTO = ominiBusDTO;
	}

	public RouteDTO getRouteDTO() {
		return routeDTO;
	}

	public void setRouteDTO(RouteDTO routeDTO) {
		this.routeDTO = routeDTO;
	}

	public PaymentVoucherDTO getPaymentVoucherDTO() {
		return paymentVoucherDTO;
	}

	public void setPaymentVoucherDTO(PaymentVoucherDTO paymentVoucherDTO) {
		this.paymentVoucherDTO = paymentVoucherDTO;
	}

	public PermitRenewalsDTO getCheckAndDisplayRemarkValue() {
		return checkAndDisplayRemarkValue;
	}

	public void setCheckAndDisplayRemarkValue(PermitRenewalsDTO checkAndDisplayRemarkValue) {
		this.checkAndDisplayRemarkValue = checkAndDisplayRemarkValue;
	}

	public QueueManagementService getQueueManagementService() {
		return queueManagementService;
	}

	public void setQueueManagementService(QueueManagementService queueManagementService) {
		this.queueManagementService = queueManagementService;
	}

	public DocumentManagementService getDocumentManagementService() {
		return documentManagementService;
	}

	public void setDocumentManagementService(DocumentManagementService documentManagementService) {
		this.documentManagementService = documentManagementService;
	}

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
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

	public boolean isLocalcheckcounter() {
		return localcheckcounter;
	}

	public void setLocalcheckcounter(boolean localcheckcounter) {
		this.localcheckcounter = localcheckcounter;
	}

	public int getActiveTabIndex() {
		return activeTabIndex;
	}

	public void setActiveTabIndex(int activeTabIndex) {
		this.activeTabIndex = activeTabIndex;
	}

	public boolean isNext() {
		return next;
	}

	public void setNext(boolean next) {
		this.next = next;
	}

	public boolean isApplicationFound() {
		return applicationFound;
	}

	public void setApplicationFound(boolean applicationFound) {
		this.applicationFound = applicationFound;
	}

	public boolean isLoanObtained() {
		return loanObtained;
	}

	public void setLoanObtained(boolean loanObtained) {
		this.loanObtained = loanObtained;
	}

	public String getStrSelectedTitle() {
		return strSelectedTitle;
	}

	public void setStrSelectedTitle(String strSelectedTitle) {
		this.strSelectedTitle = strSelectedTitle;
	}

	public String getStrSelectedGender() {
		return strSelectedGender;
	}

	public void setStrSelectedGender(String strSelectedGender) {
		this.strSelectedGender = strSelectedGender;
	}

	public String getStrSelectedMartial() {
		return strSelectedMartial;
	}

	public void setStrSelectedMartial(String strSelectedMartial) {
		this.strSelectedMartial = strSelectedMartial;
	}

	public String getStrSelectedProvince() {
		return strSelectedProvince;
	}

	public void setStrSelectedProvince(String strSelectedProvince) {
		this.strSelectedProvince = strSelectedProvince;
	}

	public String getStrSelectedDistrict() {
		return strSelectedDistrict;
	}

	public void setStrSelectedDistrict(String strSelectedDistrict) {
		this.strSelectedDistrict = strSelectedDistrict;
	}

	public String getStrSelectedDivSec() {
		return strSelectedDivSec;
	}

	public void setStrSelectedDivSec(String strSelectedDivSec) {
		this.strSelectedDivSec = strSelectedDivSec;
	}

	public String getStrSelectedLanguage() {
		return strSelectedLanguage;
	}

	public void setStrSelectedLanguage(String strSelectedLanguage) {
		this.strSelectedLanguage = strSelectedLanguage;
	}

	public String getStrNicNo() {
		return strNicNo;
	}

	public void setStrNicNo(String strNicNo) {
		this.strNicNo = strNicNo;
	}

	public String getStrTenderRefNo() {
		return strTenderRefNo;
	}

	public void setStrTenderRefNo(String strTenderRefNo) {
		this.strTenderRefNo = strTenderRefNo;
	}

	public String getStrCurrentDate() {
		return strCurrentDate;
	}

	public void setStrCurrentDate(String strCurrentDate) {
		this.strCurrentDate = strCurrentDate;
	}

	public String getStrApplicationNo() {
		return strApplicationNo;
	}

	public void setStrApplicationNo(String strApplicationNo) {
		this.strApplicationNo = strApplicationNo;
	}

	public String getStrQueueNo() {
		return strQueueNo;
	}

	public void setStrQueueNo(String strQueueNo) {
		this.strQueueNo = strQueueNo;
	}

	public String getStrServiceType() {
		return strServiceType;
	}

	public void setStrServiceType(String strServiceType) {
		this.strServiceType = strServiceType;
	}

	public String getStrBusRegNo() {
		return strBusRegNo;
	}

	public void setStrBusRegNo(String strBusRegNo) {
		this.strBusRegNo = strBusRegNo;
	}

	public String getStrPermitNo() {
		return strPermitNo;
	}

	public void setStrPermitNo(String strPermitNo) {
		this.strPermitNo = strPermitNo;
	}

	public String getStrSelectedRoute() {
		return strSelectedRoute;
	}

	public void setStrSelectedRoute(String strSelectedRoute) {
		this.strSelectedRoute = strSelectedRoute;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
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

	public List<CommonDTO> getRoutefordropdownList() {
		return routefordropdownList;
	}

	public void setRoutefordropdownList(List<CommonDTO> routefordropdownList) {
		this.routefordropdownList = routefordropdownList;
	}

	public List<CommonDTO> getAllDivSecList() {
		return allDivSecList;
	}

	public void setAllDivSecList(List<CommonDTO> allDivSecList) {
		this.allDivSecList = allDivSecList;
	}

	public List<CommonDTO> getCounterList() {
		return counterList;
	}

	public boolean isInquiryViewMode() {
		return inquiryViewMode;
	}

	public void setInquiryViewMode(boolean inquiryViewMode) {
		this.inquiryViewMode = inquiryViewMode;
	}

	public void setCounterList(List<CommonDTO> counterList) {
		this.counterList = counterList;
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

	public boolean isBackToPrinting() {
		return backToPrinting;
	}

	public void setBackToPrinting(boolean backToPrinting) {
		this.backToPrinting = backToPrinting;
	}

	public Boolean getRouteFlag() {
		return routeFlag;
	}

	public void setRouteFlag(Boolean routeFlag) {
		this.routeFlag = routeFlag;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
