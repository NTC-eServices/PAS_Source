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
import lk.informatics.ntc.model.service.HistoryService;
import lk.informatics.ntc.model.service.IssuePermitService;
import lk.informatics.ntc.model.service.MigratedService;
import lk.informatics.ntc.model.service.QueueManagementService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "editPermitIssuanceBackingBean")
@ViewScoped
public class EditPermitIssuanceBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private AdminService adminService;
	private EmployeeProfileService employeeProfileService;
	private CommonService commonService;
	private MigratedService migratedService;

	private CommonDTO commonDTO;
	private OminiBusDTO ominiBusDTO;
	private RouteDTO routeDTO;
	private PaymentVoucherDTO paymentVoucherDTO;
	private PermitRenewalsDTO checkAndDisplayRemarkValue;

	private QueueManagementService queueManagementService;

	private DocumentManagementService documentManagementService;

	boolean check = true;
	boolean skip = true;
	private Boolean routeFlag;
	private boolean localcheckcounter = true;

	private boolean loanObtained;

	private String strSelectedTitle;
	private String strSelectedGender;
	private String strSelectedMartial;
	private String strSelectedProvince;
	private String strSelectedDistrict;
	private String strSelectedDivSec;
	private String strSelectedLanguage;
	private String strNicNo;
	private long strSeqNo;

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

	// Rendering tab view section
	private boolean applicationFound;
	boolean callNext = false;

	// Search form variables
	private String strTenderRefNo;
	private String strCurrentDate;
	private String strApplicationNo;
	private String strQueueNo;
	private String strServiceType;
	private String strBusRegNo;
	private String strPermitNo;
	private String strSelectedRoute;
	private String errorMsg;
	private String alertMSG;

	private IssuePermitDTO issuePermitDTO;
	private BusOwnerDTO busOwnerDTO;
	private IssuePermitService issuePermitService;

	private boolean next;
	private int activeTabIndex;

	private IssuePermitDTO serviceHistoryDTO;
	private PermitRenewalsDTO vehicleOwnerHistoryDTO;
	private OminiBusDTO OminiBusHistoryDTO;
	private PermitRenewalsDTO applicationHistoryDTO;
	private HistoryService historyService;

	@PostConstruct
	public void init() {

		activeTabIndex = 0;
		issuePermitDTO = new IssuePermitDTO();
		busOwnerDTO = new BusOwnerDTO();
		ominiBusDTO = new OminiBusDTO();
		routeDTO = new RouteDTO();
		commonDTO = new CommonDTO();
		routeFlag = false;
		paymentVoucherDTO = new PaymentVoucherDTO();
		checkAndDisplayRemarkValue = new PermitRenewalsDTO();

		setNext(false);
		setApplicationFound(false);
		setLoanObtained(false);

		setCallNext(false);
		setSkip(true);
		localcheckcounter = sessionBackingBean.isCounterCheck();
		loadValues();
	}

	public void loadValues() {

		issuePermitService = (IssuePermitService) SpringApplicationContex.getBean("issuePermitService");
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");
		employeeProfileService = (EmployeeProfileService) SpringApplicationContex.getBean("employeeProfileService");
		queueManagementService = (QueueManagementService) SpringApplicationContex.getBean("queueManagementService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		migratedService = (MigratedService) SpringApplicationContex.getBean("migratedService");
		documentManagementService = (DocumentManagementService) SpringApplicationContex
				.getBean("documentManagementService");
		historyService = (HistoryService) SpringApplicationContex.getBean("historyService");

		titleList = employeeProfileService.GetTitleToDropdown();
		genderList = employeeProfileService.GetGenderToDropdown();
		martialList = employeeProfileService.GetMartialToDropdown();

		provincelList = adminService.getProvinceToDropdown();

		allDivSecList = adminService.getDivSecToDropdown();

		routefordropdownList = issuePermitService.getRoutesToDropdown();

		counterList = commonService.counterDropdown("03");
	}

	public void searchApplication() {
		setStrApplicationNo(strApplicationNo.trim());

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		strCurrentDate = dateFormat.format(date).toString();

		// If application number not null , not empty string , not empty
		if (strApplicationNo != null && strApplicationNo != " " && !strApplicationNo.isEmpty()) {
			// If application number is valid
			// // when user go to issue new permit and clicked next button
			search();

		} else if (strBusRegNo != null && strBusRegNo != " " && !strBusRegNo.isEmpty()) {
			strApplicationNo = issuePermitService.getApplicationNoByVehicleNo(strBusRegNo.trim());
			search();

		} else {
			strCurrentDate = null;
			errorMsg = "Application No. or Bus Registraion No. is required.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		}

	}

	public void search() {
		// If application number is valid
		// // when user go to issue new permit and clicked next button
		String checkForApplicationNo = issuePermitService.checkForApplicationNo(strApplicationNo);
		if (!checkForApplicationNo.equals("invalid") && !checkForApplicationNo.equals("false")) {

			// If incomplete application true get permit no
			String tmpPermitNo = issuePermitService.checkForIncompleteApplication(strApplicationNo);
			if (!tmpPermitNo.equalsIgnoreCase("false") && !tmpPermitNo.equalsIgnoreCase("completed")
					&& !tmpPermitNo.equalsIgnoreCase("before")) {
				strPermitNo = tmpPermitNo;
				// Render tab view section
				applicationFound = true;

				// Get details related to strApplicationNo for search panel
				issuePermitDTO = issuePermitService.getApplicationDetailsByApplicationNo(strApplicationNo);
				// Set details to bean
				strTenderRefNo = issuePermitDTO.getTenderRefNo();
				strApplicationNo = issuePermitDTO.getApplicationNo();
				strQueueNo = issuePermitDTO.getQueueNo();
				strServiceType = issuePermitDTO.getServiceType();
				strBusRegNo = issuePermitDTO.getBusRegNo();
				ominiBusDTO.setVehicleRegNo(strBusRegNo);

				// Get active index
				// If tab 1 is incomplete
				if (!issuePermitService.getCompletedTab("one", strApplicationNo)) {

				} else {
					// If tab 1 is completed true
					activeTabIndex = 1;

					// If tab 2 is complete true
					if (issuePermitService.getCompletedTab("two", strApplicationNo)) {
						activeTabIndex = 2;
					}
					// If tab 3 is completed true
					if (issuePermitService.getCompletedTab("three", strApplicationNo)) {

						activeTabIndex = 3;
					}
				}

				// Tab one completed. load tab 01
				if (activeTabIndex > 0) {
					getOwnerDetails();
					ominiBusDTO = issuePermitService.ominiBusByVehicleNo(issuePermitDTO.getBusRegNo());
					ominiBusDTO.setSeq(0);

				}
				// Tab two completed. load tab 02
				if (activeTabIndex > 1) {
					getOminiBusDetails();
				}
				// Tab three completed. load tab 03
				if (activeTabIndex > 2) {
					getServiceDetails();
					displayCheckDocumentDataList();
				}

			} else if (tmpPermitNo.equalsIgnoreCase("completed")) {
				// Show information massage by saying your application already completed

				strCurrentDate = null;

				setAlertMSG("Application already completed.");

				RequestContext.getCurrentInstance().update("frmAlert");
				RequestContext.getCurrentInstance().execute("PF('alertMessage').show()");

			} else if (tmpPermitNo.equalsIgnoreCase("before")) {

				setAlertMSG("Permit not issued.");
				RequestContext.getCurrentInstance().update("frmAlert");
				RequestContext.getCurrentInstance().execute("PF('alertMessage').show()");

				// when user go to issue new permit and clicked next button
			} else if (tmpPermitNo.equalsIgnoreCase("false")) {

				setAlertMSG("Permit not issued.");
				RequestContext.getCurrentInstance().update("frmAlert");
				RequestContext.getCurrentInstance().execute("PF('alertMessage').show()");

			}

			// when user didn't go to issue new permit and didn't click next button
		} else if (checkForApplicationNo.equals("false")) {

			setAlertMSG("Permit not issued.");
			RequestContext.getCurrentInstance().update("frmAlert");
			RequestContext.getCurrentInstance().execute("PF('alertMessage').show()");

		} else {

			setStrApplicationNo(null);
			strCurrentDate = null;
			errorMsg = "Invalid Application Number Or Bus Registration Number.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public String getStrTenderRefNo() {
		return strTenderRefNo;
	}

	public String getStrCurrentDate() {
		return strCurrentDate;
	}

	public String getStrApplicationNo() {
		return strApplicationNo;
	}

	public String getStrQueueNo() {
		return strQueueNo;
	}

	public String getStrServiceType() {
		return strServiceType;
	}

	public String getStrBusRegNo() {
		return strBusRegNo;
	}

	public String getStrPermitNo() {
		return strPermitNo;
	}

	public String getStrSelectedRoute() {
		return strSelectedRoute;
	}

	public void setStrTenderRefNo(String strTenderRefNo) {
		this.strTenderRefNo = strTenderRefNo;
	}

	public void setStrCurrentDate(String strCurrentDate) {
		this.strCurrentDate = strCurrentDate;
	}

	public void setStrApplicationNo(String strApplicationNo) {
		this.strApplicationNo = strApplicationNo;
	}

	public void setStrQueueNo(String strQueueNo) {
		this.strQueueNo = strQueueNo;
	}

	public void setStrServiceType(String strServiceType) {
		this.strServiceType = strServiceType;
	}

	public void setStrBusRegNo(String strBusRegNo) {
		this.strBusRegNo = strBusRegNo;
	}

	public void setStrPermitNo(String strPermitNo) {
		this.strPermitNo = strPermitNo;
	}

	public void setStrSelectedRoute(String strSelectedRoute) {
		this.strSelectedRoute = strSelectedRoute;
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

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public boolean isApplicationFound() {
		return applicationFound;
	}

	public void setApplicationFound(boolean applicationFound) {
		this.applicationFound = applicationFound;
	}

	public BusOwnerDTO getBusOwnerDTO() {
		return busOwnerDTO;
	}

	public void setBusOwnerDTO(BusOwnerDTO busOwnerDTO) {
		this.busOwnerDTO = busOwnerDTO;
	}

	public boolean isCallNext() {
		return callNext;
	}

	public void setCallNext(boolean callNext) {
		this.callNext = callNext;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public AdminService getAdminService() {
		return adminService;
	}

	public EmployeeProfileService getEmployeeProfileService() {
		return employeeProfileService;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public CommonDTO getCommonDTO() {
		return commonDTO;
	}

	public OminiBusDTO getOminiBusDTO() {
		return ominiBusDTO;
	}

	public RouteDTO getRouteDTO() {
		return routeDTO;
	}

	public PaymentVoucherDTO getPaymentVoucherDTO() {
		return paymentVoucherDTO;
	}

	public PermitRenewalsDTO getCheckAndDisplayRemarkValue() {
		return checkAndDisplayRemarkValue;
	}

	public QueueManagementService getQueueManagementService() {
		return queueManagementService;
	}

	public DocumentManagementService getDocumentManagementService() {
		return documentManagementService;
	}

	public boolean isCheck() {
		return check;
	}

	public boolean isSkip() {
		return skip;
	}

	public boolean isLocalcheckcounter() {
		return localcheckcounter;
	}

	public int getActiveTabIndex() {
		return activeTabIndex;
	}

	public boolean isLoanObtained() {
		return loanObtained;
	}

	public String getStrSelectedTitle() {
		return strSelectedTitle;
	}

	public String getStrSelectedGender() {
		return strSelectedGender;
	}

	public String getStrSelectedMartial() {
		return strSelectedMartial;
	}

	public String getStrSelectedProvince() {
		return strSelectedProvince;
	}

	public String getStrSelectedDistrict() {
		return strSelectedDistrict;
	}

	public String getStrSelectedDivSec() {
		return strSelectedDivSec;
	}

	public String getStrSelectedLanguage() {
		return strSelectedLanguage;
	}

	public String getStrNicNo() {
		return strNicNo;
	}

	public List<CommonDTO> getTitleList() {
		return titleList;
	}

	public List<CommonDTO> getGenderList() {
		return genderList;
	}

	public List<CommonDTO> getMartialList() {
		return martialList;
	}

	public List<CommonDTO> getProvincelList() {
		return provincelList;
	}

	public List<CommonDTO> getDistrictList() {
		return districtList;
	}

	public List<CommonDTO> getDivSecList() {
		return divSecList;
	}

	public List<CommonDTO> getRoutefordropdownList() {
		return routefordropdownList;
	}

	public List<CommonDTO> getAllDivSecList() {
		return allDivSecList;
	}

	public List<CommonDTO> getCounterList() {
		return counterList;
	}

	public List<PermitRenewalsDTO> getDataList() {
		return dataList;
	}

	public List<PermitRenewalsDTO> getCheckDataList() {
		return checkDataList;
	}

	public List<DocumentManagementDTO> getMandatoryList() {
		return mandatoryList;
	}

	public List<DocumentManagementDTO> getOptionalList() {
		return optionalList;
	}

	public boolean isNext() {
		return next;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	public void setEmployeeProfileService(EmployeeProfileService employeeProfileService) {
		this.employeeProfileService = employeeProfileService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public void setAlertMSG(String alertMSG) {
		this.alertMSG = alertMSG;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getAlertMSG() {
		return alertMSG;
	}

	public void setCommonDTO(CommonDTO commonDTO) {
		this.commonDTO = commonDTO;
	}

	public void setOminiBusDTO(OminiBusDTO ominiBusDTO) {
		this.ominiBusDTO = ominiBusDTO;
	}

	public void setRouteDTO(RouteDTO routeDTO) {
		this.routeDTO = routeDTO;
	}

	public void setPaymentVoucherDTO(PaymentVoucherDTO paymentVoucherDTO) {
		this.paymentVoucherDTO = paymentVoucherDTO;
	}

	public void setCheckAndDisplayRemarkValue(PermitRenewalsDTO checkAndDisplayRemarkValue) {
		this.checkAndDisplayRemarkValue = checkAndDisplayRemarkValue;
	}

	public void setQueueManagementService(QueueManagementService queueManagementService) {
		this.queueManagementService = queueManagementService;
	}

	public void setDocumentManagementService(DocumentManagementService documentManagementService) {
		this.documentManagementService = documentManagementService;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	public void setSkip(boolean skip) {
		this.skip = skip;
	}

	public void setLocalcheckcounter(boolean localcheckcounter) {
		this.localcheckcounter = localcheckcounter;
	}

	public void setActiveTabIndex(int activeTabIndex) {
		this.activeTabIndex = activeTabIndex;
	}

	public void setLoanObtained(boolean loanObtained) {
		this.loanObtained = loanObtained;
	}

	public void setStrSelectedTitle(String strSelectedTitle) {
		this.strSelectedTitle = strSelectedTitle;
	}

	public void setStrSelectedGender(String strSelectedGender) {
		this.strSelectedGender = strSelectedGender;
	}

	public void setStrSelectedMartial(String strSelectedMartial) {
		this.strSelectedMartial = strSelectedMartial;
	}

	public void setStrSelectedProvince(String strSelectedProvince) {
		this.strSelectedProvince = strSelectedProvince;
	}

	public void setStrSelectedDistrict(String strSelectedDistrict) {
		this.strSelectedDistrict = strSelectedDistrict;
	}

	public void setStrSelectedDivSec(String strSelectedDivSec) {
		this.strSelectedDivSec = strSelectedDivSec;
	}

	public void setStrSelectedLanguage(String strSelectedLanguage) {
		this.strSelectedLanguage = strSelectedLanguage;
	}

	public void setStrNicNo(String strNicNo) {
		this.strNicNo = strNicNo;
	}

	public void setTitleList(List<CommonDTO> titleList) {
		this.titleList = titleList;
	}

	public void setGenderList(List<CommonDTO> genderList) {
		this.genderList = genderList;
	}

	public void setMartialList(List<CommonDTO> martialList) {
		this.martialList = martialList;
	}

	public void setProvincelList(List<CommonDTO> provincelList) {
		this.provincelList = provincelList;
	}

	public void setDistrictList(List<CommonDTO> districtList) {
		this.districtList = districtList;
	}

	public void setDivSecList(List<CommonDTO> divSecList) {
		this.divSecList = divSecList;
	}

	public void setRoutefordropdownList(List<CommonDTO> routefordropdownList) {
		this.routefordropdownList = routefordropdownList;
	}

	public void setAllDivSecList(List<CommonDTO> allDivSecList) {
		this.allDivSecList = allDivSecList;
	}

	public void setCounterList(List<CommonDTO> counterList) {
		this.counterList = counterList;
	}

	public void setDataList(List<PermitRenewalsDTO> dataList) {
		this.dataList = dataList;
	}

	public void setCheckDataList(List<PermitRenewalsDTO> checkDataList) {
		this.checkDataList = checkDataList;
	}

	public void setMandatoryList(List<DocumentManagementDTO> mandatoryList) {
		this.mandatoryList = mandatoryList;
	}

	public void setOptionalList(List<DocumentManagementDTO> optionalList) {
		this.optionalList = optionalList;
	}

	public void setNext(boolean next) {
		this.next = next;
	}

	// atheeb

	public void setStrSeqNo(long l) {
		this.strSeqNo = l;
	}

	public Long getStrSeqNo() {
		return strSeqNo;
	}

	public void setRouteFlag(Boolean routeFlag) {
		this.routeFlag = routeFlag;
	}

	public Boolean getRouteFlag() {
		return routeFlag;
	}

	public void callNext() {
		String queueNo = queueManagementService.callNextQueueNumberAction("03", "02");
		strQueueNo = queueNo;

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		strCurrentDate = dateFormat.format(date).toString();

		if (strQueueNo != null && !strQueueNo.isEmpty() && !strQueueNo.trim().equalsIgnoreCase("")) {

			issuePermitDTO = issuePermitService.getApplicationDetailsByQueueNo(queueNo);

			migratedService.updateStatusOfQueueNumberAfterCallNext(strQueueNo, "O");

			setCallNext(true);
			setSkip(false);

			migratedService.updateCounterIdOfQueueNumberAfterCallNext(strQueueNo, sessionBackingBean.getCounterId());

			commonService.updateCounterQueueNo(strQueueNo, sessionBackingBean.getCounterId());

			strTenderRefNo = issuePermitDTO.getTenderRefNo();

			strApplicationNo = issuePermitDTO.getApplicationNo();
			strQueueNo = issuePermitDTO.getQueueNo();
			strServiceType = issuePermitDTO.getServiceType();
			strBusRegNo = issuePermitDTO.getBusRegNo();

			if (issuePermitDTO.getApplicationNo() != null && !issuePermitDTO.getApplicationNo().isEmpty()
					&& !issuePermitDTO.getApplicationNo().trim().equalsIgnoreCase("")) {
				setApplicationFound(true);
			}

		} else {
			strApplicationNo = null;
			strCurrentDate = null;
			errorMsg = "The queue is empty";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void skip() {
		if (strQueueNo != null && !strQueueNo.isEmpty() && !strQueueNo.trim().equalsIgnoreCase("")) {

			migratedService.updateSkipQueueNumberStatus(strQueueNo);

			setCallNext(false);
			setSkip(true);
			clearAll();

		} else {
			strApplicationNo = null;
		}

	}

	public void onCounterSelect() {

		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		commonService.counterStatus(sessionBackingBean.getCounterId(), sessionBackingBean.getLoginUser());
		localcheckcounter = false;
		sessionBackingBean.setCounterCheck(false);
		RequestContext context = RequestContext.getCurrentInstance();

		context.execute("PF('dlg2').hide();");
	}

	public void handleClose() throws InterruptedException {
		error();

		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("/InfoNTC/pages/home/welcomePage.xhtml");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void error() {

		RequestContext.getCurrentInstance().update("@all");
		RequestContext.getCurrentInstance().update("counterfield");
		RequestContext.getCurrentInstance().execute("PF('counterselectionerror').show()");
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

	public void onRouteChange() {

		issuePermitDTO.setRouteNo(issuePermitDTO.getRouteNo());

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

		routeFlag = false;
	}

	public void generateVoucher() {
		issuePermitDTO.setApplicationNo(strApplicationNo);
		issuePermitDTO.setBusRegNo(strBusRegNo);
		CreatePaymentVoucherBckingBean c = new CreatePaymentVoucherBckingBean();

		try {

			sessionBackingBean.setApplicationNo(issuePermitDTO.getApplicationNo());
			sessionBackingBean.setTransactionDescription("NEW PERMIT");
			sessionBackingBean.setPermitNo(issuePermitDTO.getPermitNo());

			paymentVoucherDTO = new PaymentVoucherDTO();
			paymentVoucherDTO.setApplicationNo(issuePermitDTO.getApplicationNo());
			paymentVoucherDTO.setTransactionDescription("NEW PERMIT");
			paymentVoucherDTO.setPermitNo(issuePermitDTO.getPermitNo());
			RequestContext.getCurrentInstance().execute("PF('generateVoucher').show()");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void documentManagement() {
		DocumentManagementBackingBean documentManagementBackingBean = new DocumentManagementBackingBean();
		try {

			sessionBackingBean.setPermitRenewalPermitNo(strPermitNo);
			sessionBackingBean.setPermitRenewalTranstractionTypeDescription("NEW PERMIT");
			sessionBackingBean.setGoToPermitRenewalUploadDocPopUp(true);
			sessionBackingBean.setApplicationNoForDoc(strApplicationNo);

			documentManagementBackingBean.setUploadPermit(strPermitNo);
			documentManagementBackingBean.setTransactionType("NEW PERMIT");

			mandatoryList = documentManagementService.mandatoryDocs("03", strPermitNo);

			optionalList = documentManagementService.optionalDocs("03", strPermitNo);

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

	public void serviceSaveUpdate() {
		if (issuePermitDTO.getTrips() > 0) {
			if (issuePermitDTO.getRouteDivSec() != null && !issuePermitDTO.getRouteDivSec().isEmpty()
					&& !issuePermitDTO.getRouteDivSec().equalsIgnoreCase("")) {
				if (issuePermitDTO.getParkingPlace() != null) {
					if (issuePermitDTO.getServiceSeq() != 0) {

						issuePermitDTO.setModifiedBy(sessionBackingBean.getLoginUser());

						/* Update Service History Table */
						serviceHistoryDTO = historyService.getServiceTableData(issuePermitDTO.getApplicationNo(),
								sessionBackingBean.getLoginUser());
						int result = issuePermitService.updateService(issuePermitDTO);

						if (result == 0) {

							/* Save History Data */
							historyService.insertServiceHistoryData(serviceHistoryDTO);

							activeTabIndex = 3;
							displayCheckDocumentDataList();
							getServiceDetails();

							RequestContext.getCurrentInstance().update("frmPaymentInfo");
							RequestContext.getCurrentInstance().execute("PF('successSve').show()");

						} else {
							RequestContext.getCurrentInstance().execute("PF('generalError').show()");
						}
					} else {

						issuePermitDTO.setCreatedBy(sessionBackingBean.getLoginUser());

						int result = issuePermitService.saveService(issuePermitDTO);

						if (result == 0) {
							activeTabIndex = 3;
							displayCheckDocumentDataList();
							getServiceDetails();

							RequestContext.getCurrentInstance().update("frmPaymentInfo");
							RequestContext.getCurrentInstance().execute("PF('successSve').show()");

						} else {
							RequestContext.getCurrentInstance().execute("PF('generalError').show()");
						}
					}
				} else {
					errorMsg = "Parking Place should be entered.";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			} else {
				errorMsg = "DS Division should be entered.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		} else {
			errorMsg = "No. of trips per Day should be entered.";
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

	public void getServiceDetails() {
		if (issuePermitDTO.getServiceSeq() >= 0) {
			issuePermitDTO = issuePermitService.serviceDetailsByPermitNo(issuePermitDTO.getPermitNo());
		}

		setStrSelectedRoute(issuePermitDTO.getRouteNo());
		onRouteChange();

		if (issuePermitDTO.getRouteFlag() != null && !issuePermitDTO.getRouteFlag().trim().equals("")
				&& issuePermitDTO.getRouteFlag().equalsIgnoreCase("Y")) {
			routeFlag = true;
			routeFlagListener();

		} else if (issuePermitDTO.getRouteFlag() != null && !issuePermitDTO.getRouteFlag().trim().equals("")
				&& issuePermitDTO.getRouteFlag().equalsIgnoreCase("N")) {

		}

		else {

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

	public void clearServiceDetails() {
		strSelectedRoute = null;
		issuePermitDTO.setOrigin(null);
		issuePermitDTO.setRouteDivSec(null);
		issuePermitDTO.setDestination(null);
		issuePermitDTO.setVia(null);
		issuePermitDTO.setDistance(null);
		issuePermitDTO.setTrips(0);
		issuePermitDTO.setRouteNo(null);
		issuePermitDTO.setParkingPlace(null);
		issuePermitDTO.setParkingDivSec(null);
		issuePermitDTO.setDistanceParkingOrigin(null);
	}

	public void ajaxInsertPhysicalExitRecord() {

		for (int i = 0; i < dataList.size(); i++) {

			if (dataList.get(i).isPhysicallyExists() == true) {
				issuePermitService.insertDocumentPhysicallyExitsStatus(dataList.get(i).isPhysicallyExists(),
						dataList.get(i).getDocumentCode(), strApplicationNo, strPermitNo,
						sessionBackingBean.getLoginUser());
			} else {
				issuePermitService.deleteDocumentPhysicallyExitsStatus(dataList.get(i).isPhysicallyExists(),
						dataList.get(i).getDocumentCode(), strApplicationNo, strPermitNo,
						sessionBackingBean.getLoginUser());
			}
		}

	}

	public void displayCheckDocumentDataList() {
		dataList = issuePermitService.getAllRecordsForDocumentsCheckings();
		for (int i = 0; i < dataList.size(); i++) {

			String currentDocCode = dataList.get(i).getDocumentCode();
			String currentDocCodeDescription = dataList.get(i).getDocumentDescription();

			boolean resultValue = issuePermitService.checkIsSumbiited(currentDocCode, strApplicationNo, strPermitNo);
			boolean isMandatory = issuePermitService.isMandatory(currentDocCode, strApplicationNo, strPermitNo);
			boolean isPhysicallyExit = issuePermitService.isPhysicallyExit(currentDocCode, strApplicationNo,
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

			checkAndDisplayRemarkValue = issuePermitService.getRemarkDetails(currentDocCode, strApplicationNo,
					strPermitNo);
			dataList.get(i).setDocSeqChecked(checkAndDisplayRemarkValue.getDocSeqChecked());
			dataList.get(i).setRemark(checkAndDisplayRemarkValue.getRemark());
			dataList.get(i).setDocFilePath(checkAndDisplayRemarkValue.getDocFilePath());
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

			commonService.updateTaskStatusCompleted(strApplicationNo, "PM200", sessionBackingBean.getLoginUser());

			migratedService.updateStatusOfQueueNumberAfterCallNext(strQueueNo, "C");

			migratedService.updateTransactionTypeCodeForQueueNo(strQueueNo, "03");
			migratedService.updateQueueNumberTaskInQueueMaster(strQueueNo, strApplicationNo, "PM200", "C");

			clearAll();

			RequestContext.getCurrentInstance().update("finalCheckList");

			RequestContext.getCurrentInstance().update("frmsuccessSve");
			setErrorMsg("Successfully Saved.");
			RequestContext.getCurrentInstance().execute("PF('successSve').show()");
		}

	}

	public void clearCheckDocumentsTable() {

		displayCheckDocumentDataList();

	}

	public void clearAll() {

		strTenderRefNo = null;
		strCurrentDate = null;
		strApplicationNo = null;
		strQueueNo = null;
		strServiceType = null;
		strBusRegNo = null;
		strSelectedRoute = null;
		strSeqNo = 0;

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

	public void busOwnerSave() {

		if (strSelectedTitle != null && !strSelectedTitle.isEmpty() && !strSelectedTitle.equalsIgnoreCase("")) {
			if (strSelectedGender != null && !strSelectedGender.isEmpty() && !strSelectedGender.equalsIgnoreCase("")) {
				if (busOwnerDTO.getDob() != null) {
					if (busOwnerDTO.getFullName() != null && !busOwnerDTO.getFullName().isEmpty()
							&& !busOwnerDTO.getFullName().equalsIgnoreCase("")) {
						if (strSelectedMartial != null && !strSelectedMartial.isEmpty()
								&& !strSelectedMartial.equalsIgnoreCase("")) {
							if (busOwnerDTO.getAddress1() != null && !busOwnerDTO.getAddress1().isEmpty()
									&& !busOwnerDTO.getAddress1().equalsIgnoreCase("")) {
								if (busOwnerDTO.getAddress2() != null && !busOwnerDTO.getAddress2().isEmpty()
										&& !busOwnerDTO.getAddress2().equalsIgnoreCase("")) {
									if (busOwnerDTO.getCity() != null && !busOwnerDTO.getCity().isEmpty()
											&& !busOwnerDTO.getCity().equalsIgnoreCase("")) {
										if (strSelectedProvince != null && !strSelectedProvince.isEmpty()
												&& !strSelectedProvince.equalsIgnoreCase("")) {
											if (busOwnerDTO.getNicNo() != null && !busOwnerDTO.getNicNo().isEmpty()
													&& !busOwnerDTO.getNicNo().equalsIgnoreCase("")) {
												if (strSelectedLanguage.equals("S")) {
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

																		updateOwner();

																	} else {

																		saveOwner();

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
												} else if (strSelectedLanguage.equals("T")) {
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
																		saveOwner();
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

														updateOwner();
													} else {

														saveOwner();
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

		setStrSelectedRoute("02");

	}

	private void saveOwner() {
		busOwnerDTO.setTitle(strSelectedTitle);
		busOwnerDTO.setGender(strSelectedGender);
		busOwnerDTO.setPerferedLanguage(strSelectedLanguage);
		busOwnerDTO.setProvince(strSelectedProvince);
		busOwnerDTO.setMaritalStatus(strSelectedMartial);
		busOwnerDTO.setDistrict(strSelectedDistrict);
		busOwnerDTO.setDivSec(strSelectedDivSec);
		busOwnerDTO.setApplicationNo(issuePermitDTO.getApplicationNo());
		busOwnerDTO.setBusRegNo(issuePermitDTO.getBusRegNo());
		busOwnerDTO.setCreatedBy(sessionBackingBean.loginUser);
		busOwnerDTO.setIsBacklogApp("N");

		String chkDuplicates = issuePermitService.checkDuplicateApplicationNo(busOwnerDTO.getApplicationNo());

		if (chkDuplicates == null) {

			/* Update Vehicle Owner History */
			vehicleOwnerHistoryDTO = historyService.getVehicleOwnerTableData(busOwnerDTO.getApplicationNo(),
					sessionBackingBean.getLoginUser());
			strPermitNo = issuePermitService.saveBusOwnerDetails(busOwnerDTO);

			if (strPermitNo != null && strPermitNo != " " && !strPermitNo.isEmpty()) {

				/* Save History Data */
				historyService.insertVehicleOwnerHistoryData(vehicleOwnerHistoryDTO);
				issuePermitDTO.setPermitNo(strPermitNo);

				ominiBusDTO.setVehicleRegNo(strBusRegNo);

				getOwnerDetailsByPermitNo();

				activeTabIndex = 1;

				RequestContext.getCurrentInstance().update("frmBusOwnerInfo");
				RequestContext.getCurrentInstance().execute("PF('successSve').show()");

			} else {
				errorMsg = "Please try again.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

			}
		} else {
			errorMsg = "Permit issued already.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		}

	}

	public void getOwnerDetailsByPermitNo() {
		setStrPermitNo(strPermitNo);
		if (issuePermitDTO.getPermitNo() != null && !issuePermitDTO.getPermitNo().isEmpty()
				&& issuePermitDTO.getPermitNo() != "") {

			busOwnerDTO = adminService.ownerDetailsByPermitNo(issuePermitDTO.getPermitNo());

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
				strSeqNo = busOwnerDTO.getSeq();
			}
		}
	}

	private void updateOwner() {
		busOwnerDTO.setSeq(strSeqNo);
		busOwnerDTO.setTitle(strSelectedTitle);
		busOwnerDTO.setGender(strSelectedGender);
		busOwnerDTO.setPerferedLanguage(strSelectedLanguage);
		busOwnerDTO.setProvince(strSelectedProvince);
		busOwnerDTO.setMaritalStatus(strSelectedMartial);
		busOwnerDTO.setDistrict(strSelectedDistrict);
		busOwnerDTO.setDivSec(strSelectedDivSec);
		busOwnerDTO.setApplicationNo(issuePermitDTO.getApplicationNo());
		busOwnerDTO.setPermitNo(issuePermitDTO.getPermitNo());
		busOwnerDTO.setBusRegNo(issuePermitDTO.getBusRegNo());
		busOwnerDTO.setModifiedBy(sessionBackingBean.loginUser);
		busOwnerDTO.setIsBacklogApp("N");

		/* Update Vehicle Owner History */
		vehicleOwnerHistoryDTO = historyService.getVehicleOwnerTableData(busOwnerDTO.getApplicationNo(),
				sessionBackingBean.getLoginUser());
		int result = issuePermitService.updateBusOwner(busOwnerDTO);

		if (result == 0) {

			/* Save History Data */
			historyService.insertVehicleOwnerHistoryData(vehicleOwnerHistoryDTO);

			getOwnerDetailsByPermitNo();

			activeTabIndex = 1;

			RequestContext.getCurrentInstance().update("frmBusOwnerInfo");
			RequestContext.getCurrentInstance().execute("PF('successSve').show()");

		} else {
			RequestContext.getCurrentInstance().execute("PF('generalError').show()");
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

																				ominiBusDTO.setModifiedBy(
																						sessionBackingBean
																								.getLoginUser());
																				ominiBusDTO.setPermitNo(
																						issuePermitDTO.getPermitNo());

																				ominiBusDTO
																						.setApplicationNo(issuePermitDTO
																								.getApplicationNo());

																				/* Update OminiBus History Table */
																				OminiBusHistoryDTO = historyService
																						.getOminiBusTableData(
																								ominiBusDTO
																										.getApplicationNo(),
																								sessionBackingBean
																										.getLoginUser());
																				int result = issuePermitService
																						.updateNewPermitOminiBus(
																								ominiBusDTO);

																				if (result == 0) {

																					/* Save OminiBus History Data */
																					historyService
																							.insertOminiBusHistoryData(
																									OminiBusHistoryDTO);
																					activeTabIndex = 2;

																					getOminiBusDetails();
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
																				ominiBusDTO.setIsBacklogApp("N");
																				ominiBusDTO
																						.setCreatedBy(sessionBackingBean
																								.getLoginUser());
																				ominiBusDTO.setPermitNo(
																						issuePermitDTO.getPermitNo());
																				ominiBusDTO
																						.setApplicationNo(issuePermitDTO
																								.getApplicationNo());

																				/* Update OminiBus History Table */
																				OminiBusHistoryDTO = historyService
																						.getOminiBusTableData(
																								ominiBusDTO
																										.getApplicationNo(),
																								sessionBackingBean
																										.getLoginUser());
																				int result = issuePermitService
																						.saveNewPermitOminiBus(
																								ominiBusDTO);

																				if (result == 0) {

																					/* Save OminiBus History Data */
																					historyService
																							.insertOminiBusHistoryData(
																									OminiBusHistoryDTO);
																					activeTabIndex = 2;

																					getOminiBusDetails();

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

															ominiBusDTO
																	.setModifiedBy(sessionBackingBean.getLoginUser());
															ominiBusDTO.setPermitNo(issuePermitDTO.getPermitNo());
															ominiBusDTO.setApplicationNo(
																	issuePermitDTO.getApplicationNo());

															/* Update OminiBus History Table */
															OminiBusHistoryDTO = historyService.getOminiBusTableData(
																	ominiBusDTO.getApplicationNo(),
																	sessionBackingBean.getLoginUser());

															int result = issuePermitService
																	.updateNewPermitOminiBus(ominiBusDTO);

															if (result == 0) {

																/* Save OminiBus History Data */
																historyService
																		.insertOminiBusHistoryData(OminiBusHistoryDTO);
																activeTabIndex = 2;

																getOminiBusDetails();

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
															ominiBusDTO.setIsBacklogApp("N");
															ominiBusDTO.setCreatedBy(sessionBackingBean.getLoginUser());
															ominiBusDTO.setPermitNo(issuePermitDTO.getPermitNo());
															ominiBusDTO.setApplicationNo(
																	issuePermitDTO.getApplicationNo());

															/* Update OminiBus History Table */
															OminiBusHistoryDTO = historyService.getOminiBusTableData(
																	ominiBusDTO.getApplicationNo(),
																	sessionBackingBean.getLoginUser());
															int result = issuePermitService
																	.saveNewPermitOminiBus(ominiBusDTO);

															if (result == 0) {

																/* Save OminiBus History Data */
																historyService
																		.insertOminiBusHistoryData(OminiBusHistoryDTO);

																activeTabIndex = 2;

																getOminiBusDetails();

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

	public void clearBusDetails() {
		ominiBusDTO = new OminiBusDTO();
	}

	public void getOminiBusDetails() {
		if (issuePermitDTO.getPermitNo() != null && !issuePermitDTO.getPermitNo().isEmpty()
				&& issuePermitDTO.getPermitNo() != "") {

			issuePermitDTO.setBusRegNo(strBusRegNo);
			ominiBusDTO = issuePermitService.ominiBusByVehicleNo(issuePermitDTO.getBusRegNo());

			if (ominiBusDTO.getIsLoanObtained().equals("Y")) {

				setLoanObtained(true);
			} else if (ominiBusDTO.getIsLoanObtained().equals("N")) {

				setLoanObtained(false);
			} else {
				setLoanObtained(false);
			}

			IssuePermitDTO getRouteNo = issuePermitService
					.getApplicationDetailsByApplicationNo(ominiBusDTO.getApplicationNo());
			strSelectedRoute = getRouteNo.getRouteNo();
			onRouteChange();

			if (issuePermitDTO.getRouteFlag() != null && !issuePermitDTO.getRouteFlag().trim().equals("")
					&& issuePermitDTO.getRouteFlag().equalsIgnoreCase("Y")) {
				routeFlag = true;
				routeFlagListener();

			} else if (issuePermitDTO.getRouteFlag() != null && !issuePermitDTO.getRouteFlag().trim().equals("")
					&& issuePermitDTO.getRouteFlag().equalsIgnoreCase("N")) {

			}

			else {

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

	public void getOwnerDetails() {

		if (strApplicationNo != null && !strApplicationNo.isEmpty() && strApplicationNo != "") {

			busOwnerDTO = issuePermitService.getOwnerDetails(strApplicationNo);

			if (busOwnerDTO.getNicNo() != null && busOwnerDTO.getNicNo() != " " && !busOwnerDTO.getNicNo().isEmpty()) {
				strSelectedTitle = busOwnerDTO.getTitle();
				strSelectedMartial = busOwnerDTO.getMaritalStatus();
				strSelectedGender = busOwnerDTO.getGender();
				strSelectedDivSec = busOwnerDTO.getDivSec();
				strSelectedDistrict = busOwnerDTO.getDistrict();
				strSelectedProvince = busOwnerDTO.getProvince();
				strSelectedLanguage = busOwnerDTO.getPerferedLanguage();
				strNicNo = busOwnerDTO.getNicNo();

				setStrSelectedTitle(busOwnerDTO.getTitle());
				setStrSelectedMartial(busOwnerDTO.getMaritalStatus());
				setStrSelectedGender(busOwnerDTO.getGender());
				setStrSelectedDivSec(busOwnerDTO.getDivSec());
				setStrSelectedDistrict(busOwnerDTO.getDistrict());
				setStrSelectedProvince(busOwnerDTO.getProvince());
				setStrSelectedLanguage(busOwnerDTO.getPerferedLanguage());
				setStrNicNo(busOwnerDTO.getNicNo());
				setStrSeqNo(busOwnerDTO.getSeq());

				onProvinceChange();
				onDistrictChange();
			}

			issuePermitDTO = issuePermitService.getApplicationDetailsByApplicationNo(strApplicationNo);
			strSelectedRoute = issuePermitDTO.getRouteNo();
			onRouteChange();
			if (issuePermitDTO.getRouteFlag() != null && !issuePermitDTO.getRouteFlag().trim().equals("")
					&& issuePermitDTO.getRouteFlag().equalsIgnoreCase("Y")) {
				routeFlag = true;
				routeFlagListener();

			} else if (issuePermitDTO.getRouteFlag() != null && !issuePermitDTO.getRouteFlag().trim().equals("")
					&& issuePermitDTO.getRouteFlag().equalsIgnoreCase("N")) {

			}

			else {

			}

			busOwnerDTO.setNicNo(strNicNo);
			setNext(true);

		}

	}

	public MigratedService getMigratedService() {
		return migratedService;
	}

	public void setMigratedService(MigratedService migratedService) {
		this.migratedService = migratedService;
	}

	public IssuePermitDTO getServiceHistoryDTO() {
		return serviceHistoryDTO;
	}

	public void setServiceHistoryDTO(IssuePermitDTO serviceHistoryDTO) {
		this.serviceHistoryDTO = serviceHistoryDTO;
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

}
