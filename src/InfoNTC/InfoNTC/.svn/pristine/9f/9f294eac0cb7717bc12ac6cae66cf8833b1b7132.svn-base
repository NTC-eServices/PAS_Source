package lk.informatics.ntc.view.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.AmendmentDTO;
import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.DocumentManagementDTO;
import lk.informatics.ntc.model.dto.EmployeeDTO;
import lk.informatics.ntc.model.dto.GamiSeriyaDTO;
import lk.informatics.ntc.model.dto.LogSheetMaintenanceDTO;
import lk.informatics.ntc.model.dto.ManageUserDTO;
import lk.informatics.ntc.model.dto.PermitDTO;
import lk.informatics.ntc.model.dto.PermitRenewalsDTO;
import lk.informatics.ntc.model.dto.SearchEmployeeDTO;
import lk.informatics.ntc.model.dto.SisuSeriyaDTO;
import lk.informatics.ntc.model.dto.TenderDTO;

@ManagedBean(name = "sessionBackingBean")
@SessionScoped
public class SessionBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public String viewedInspectionApplicationNo;
	public String viewInspectionsURL;
	public String incompleteApprovalURL;
	public String viewRenewalURL;
	public boolean viewInspectionsURLStatus = true;
	public boolean incompleteApprovalURLStatus = true;
	public String loginUser;
	public String password;
	public String ipAddress;
	public long logHistorySeq;
	public String userFullName;
	public String searchedApplicationNo;
	public String searchedVehicleNo;
	public String searchedInspectionStatus;
	public boolean rolesSet = false;
	public boolean checkIsSearchPressed = false;
	public boolean checkIsBackPressed = false;
	public String viewedPermitNo;
	public String selectedQueueNo;
	private boolean isVehicleInspectionMood;
	public String viewNewPermitURL;
	public boolean viewNewPermitURLStatus = true;
	public String serviceType;
	public String busRegNo;
	public String permitRenewalPermitNo;
	public String permitRenewalTranstractionTypeDescription;
	public String applicationNoForDoc;
	private String counter;
	private String counterId;
	public boolean goToPermitRenewalUploadDocPopUp, isClicked, renewalViewMood;
	private boolean issueTenderMood;
	private Locale locale;
	private String voucherNo;
	private String paymentType;
	private String trafficProposalNo;
	public boolean amendmentPopUp;
	public String viewCreateTenderURL;
	public boolean viewCreateTenderURLStatus = true;
	public String viewedTenderRefNo;
	private boolean readOnlyFieldsInspection;
	private String selectedOptionType;
	private boolean renewalType;
	private boolean inquiryTYpe;
	private boolean vehicleInspectionType;
	public boolean amendmentsViewMood;
	private String amendmentsApplicationNo;
	private boolean isPrintOffterLetterMode;
	private String tenderApplicationNo;
	private boolean isSisuSariya;
	private String committeeApplicationNo;
	private boolean committeeView;
	private boolean backCommittee;
	private String selectedTransactionType;
	private String flyrefNo;
	private String invesNo;

	private String driverConductorId;
	private String dcAppNo;
	private String complainNo;

	private String simNo;
	private String simRegNo;
	private String stationName;
	private String stationCode;

	public String getViewCreateTenderURL() {
		return viewCreateTenderURL;
	}

	public void setViewCreateTenderURL(String viewCreateTenderURL) {
		this.viewCreateTenderURL = viewCreateTenderURL;
	}

	public boolean isViewCreateTenderURLStatus() {
		return viewCreateTenderURLStatus;
	}

	public void setViewCreateTenderURLStatus(boolean viewCreateTenderURLStatus) {
		this.viewCreateTenderURLStatus = viewCreateTenderURLStatus;
	}

	public String getViewedTenderRefNo() {
		return viewedTenderRefNo;
	}

	public void setViewedTenderRefNo(String viewedTenderRefNo) {
		this.viewedTenderRefNo = viewedTenderRefNo;
	}

	public String getIncompleteApprovalURL() {
		return incompleteApprovalURL;
	}

	public void setIncompleteApprovalURL(String incompleteApprovalURL) {
		this.incompleteApprovalURL = incompleteApprovalURL;
	}

	public boolean isIncompleteApprovalURLStatus() {
		return incompleteApprovalURLStatus;
	}

	public void setIncompleteApprovalURLStatus(boolean incompleteApprovalURLStatus) {
		this.incompleteApprovalURLStatus = incompleteApprovalURLStatus;
	}

	public String empNo;
	public String empTransaction;
	public String requestNoForSisuSariya;
	public String refNoSisuSariya;
	public String transactionType;
	public String agreementNo;
	public String serviceNoForSisuSariya;

	public String requestNoForGamiSariya;
	public String refNoGamiSariya;
	public String transactionTypeForGamiSariya;
	public String serviceNoForGamiSariya;

	public String permitNoForDocs;

	private Date calender1;
	private Date calender2;
	private String finalRemark;

	public String userRole;

	private String cancelationPermitNo;
	private String cancelationTranstractionTypeDes;
	private boolean isClickedCancelationDocPopup;

	private String message;
	private String messageHeader;
	public static final String MSG_TYPE_INFO = "INFO_DIALOG";
	public static final String MSG_TYPE_ERROR = "ERROR_DIALOG";
	public static final String MSG_TYPE_WARNING = "WARNING_DIALOG";
	public static final String MSG_TYPE_CONFIRM = "CONFIRM_DIALOG";
	public static final String MSG_TYPE_SUCCESS = "SUCCESS_DIALOG";

	// Advertisement
	public String tenderNo;
	public String description;
	public String header;
	public boolean saveCheck;
	public boolean editCheck;
	public boolean searchCheck;
	public String footer;
	public int language;
	public Date publishDate;
	public String rejectReason;
	public boolean approvebutton;
	public boolean rejectbutton;
	public boolean printCheck;
	public boolean pdfCheck;
	// Role wise function manage booleans \\NOTE: brief a small description about
	// each individual roles.
	private boolean OR_RR;
	private boolean OR_AR;
	private boolean OR_NC;
	private boolean OR_AN;
	private boolean OR_ADMIN;
	private boolean OR_TR;

	// function manage boolean
	// --New Modification--
	private boolean FN0_1;
	private boolean FN0_2;
	private boolean FN0_3;
	private boolean FN0_4;

	private boolean FN1_1; // User Management
	private boolean FN1_2;
	private boolean FN1_3;
	private boolean FN1_4;
	private boolean FN1_5;
	private boolean FN1_6;
	private boolean FN1_7;
	private boolean FN1_8;
	private boolean FN1_9; // Queue
	private boolean FN001;
	private boolean FN002;
	private boolean FN003;
	private boolean FN004;
	private boolean FN1_10;
	private boolean FN1_11;

	private boolean FN2_1; // Permit
	private boolean FN2_2;
	private boolean FN2_3;
	private boolean FN2_4;
	private boolean FN2_5;
	private boolean FN2_6;
	private boolean FN2_7;
	private boolean FN2_8;
	private boolean FN2_9;
	private boolean FN2_0;
	private boolean FN210;
	private boolean FN211;
	private boolean FN212;
	private boolean FN213;
	private boolean FN214;
	private boolean FN215;
	private boolean FN216;

	private boolean FN3_0;
	private boolean FN3_1;// Renewals
	private boolean FN3_2;
	private boolean FN3_3;
	private boolean FN3_4; // Amendments
	private boolean FN3_5;
	private boolean FN3_6;
	private boolean FN3_7;
	private boolean FN3_8;
	private boolean FN3_9;
	private boolean FN310;
	private boolean FN316;
	private boolean FN317;

	private boolean FN4_1; // Payment
	private boolean FN4_2;
	private boolean FN4_3;
	private boolean FN4_4;
	private boolean FN4_5;
	private boolean FN4_6;
	private boolean FN4_7;
	private boolean FN4_8;

	private boolean FN5_1; // Survey
	private boolean FN5_2;
	private boolean FN5_3;
	private boolean FN5_4;
	private boolean FN5_5;
	private boolean FN5_6;
	private boolean FN5_7;
	private boolean FN5_8;
	private boolean FN5_9;

	private boolean FN6_1;
	private boolean FN6_2;
	private boolean FN6_3;
	private boolean FN6_4;
	private boolean FN6_5;

	private boolean FN7_0;
	private boolean FN7_1; // Common
	private boolean FN7_2;
	private boolean FN7_3;
	private boolean FN7_4;
	private boolean FN7_5;
	private boolean FN7_6;
	private boolean FN7_7;
	private boolean FN7_8;
	private boolean FN7_9;

	private boolean FN8_0;
	private boolean FN8_1;
	private boolean FN8_2;
	private boolean FN8_3;
	private boolean FN8_4;
	private boolean FN8_5;
	private boolean FN8_6;
	private boolean FN8_7;
	private boolean FN8_8;
	private boolean FN8_9;

	private boolean FN9_0;
	private boolean FN9_1; // Tender
	private boolean FN9_2;
	private boolean FN9_3;
	private boolean FN9_4;
	private boolean FN9_5;
	private boolean FN9_6;
	private boolean FN9_7;
	private boolean FN9_8;
	private boolean FN9_9;

	private boolean FN100; // Sisu Sariya
	private boolean FN101;
	private boolean FN102;
	private boolean FN103;
	private boolean FN104;
	private boolean FN105;
	private boolean FN106;
	private boolean FN107;
	private boolean FN108;
	private boolean FN109;
	private boolean FN110;
	private boolean FN111;
	private boolean FN112;
	private boolean FN113;
	private boolean FN114;
	private boolean FN115;
	private boolean FN116;
	private boolean FN117;
	private boolean FN118;
	private boolean FN119;

	private boolean FN120;
	private boolean FN121;
	private boolean FN122;
	private boolean FN123;

	private boolean FN130; // Gami SAriya
	private boolean FN131;
	private boolean FN132;
	private boolean FN133;
	private boolean FN134;
	private boolean FN135;
	private boolean FN136;
	private boolean FN137;
	private boolean FN138;
	private boolean FN139;
	private boolean FN140;
	private boolean FN141;
	private boolean FN142;
	private boolean FN143;
	private boolean FN144;
	private boolean FN145;
	private boolean FN146;
	private boolean FN147;
	private boolean FN148;
	private boolean FN149;

	private boolean FN161; // Nisi Sariya
	private boolean FN162;
	private boolean FN163;
	private boolean FN164;
	private boolean FN318;
	private boolean FN319;

	private boolean FN200; // Bus Fare
	private boolean FN201;
	private boolean FN202;
	private boolean FN203;
	private boolean FN204;
	private boolean FN205;
	private boolean FN206;
	private boolean FN207;
	private boolean FN208;
	private boolean FN209;

	private boolean FN230; // Reports
	private boolean FN231;
	private boolean FN232;
	private boolean FN233;

	private boolean FN250;
	private boolean FN251;
	private boolean FN252;// Transaction report
	private boolean FN253;// Application Task Status Report
	private boolean FN254;
	private boolean FN600;

	private boolean FN270;
	private boolean FN271;
	private boolean FN272;
	private boolean FN273;
	private boolean FN274;
	private boolean FN275;

	private boolean FN330; // Terminal Management
	private boolean FN331;
	private boolean FN332;
	private boolean FN333;
	private boolean FN334;
	private boolean FN335;
	private boolean FN336;
	private boolean FN337;
	private boolean FN338;
	private boolean FN339;

	private boolean FN350; // Flying Squad Management
	private boolean FN351;
	private boolean FN352;
	private boolean FN353;
	private boolean FN354;
	private boolean FN355;
	private boolean FN356;
	private boolean FN357;
	private boolean FN358;
	private boolean FN359;
	private boolean FN360;
	private boolean FN361;
	private boolean FN362;
	private boolean FN363;
	private boolean FN364;
	private boolean FN365;
	private boolean FN366;
	private boolean FN367;
	private boolean FN368;

	private boolean FN400;// Driver-Conductor Management
	private boolean FN401;
	private boolean FN402;
	private boolean FN403;
	private boolean FN404;
	private boolean FN405;
	private boolean FN406;
	private boolean FN407;
	private boolean FN408;
	private boolean FN409;
	private boolean FN410;
	private boolean FN411;
	private boolean FN412;
	private boolean FN413;
	private boolean FN414;
	private boolean FN415;
	private boolean FN416;
	private boolean FN417;

	private boolean FN450; // Grievance Management
	private boolean FN451;
	private boolean FN452;
	private boolean FN453;
	private boolean FN454;
	private boolean FN455;
	private boolean FN456;
	private boolean FN457;

	private boolean FN459;
	private boolean FN460;
	private boolean FN461;

	private boolean FN500; // Awareness Management
	private boolean FN501;

	private boolean FN520; // Investigation
	private boolean FN521;
	private boolean FN522;
	private boolean FN523;
	private boolean FN524;
	private boolean FN525;

	private boolean FN550; // SIM
	private boolean FN551;
	private boolean FN552;
	private boolean FN553;
	private boolean FN554;

	private boolean FN300; // Time Table
	private boolean FN301;
	private boolean FN302;
	private boolean FN303;
	private boolean FN304;
	private boolean FN305;
	private boolean FN306;
	private boolean FN307;
	private boolean FN308;
	private boolean FN309;
	private boolean FN311;
	private boolean FN312;
	private boolean FN313;
	private boolean FN314;
	private boolean FN315;

	public String userFunction;
	public String employeeNo; // ="EMP08"
	public String pageMode; // V - View, E - Edit
	public String testUrl;
	public String permitNo;
	public String selectedUser;
	private String serviceRefNo;
	private String requestNo;
	private String serviceNo;

	public String searchURL;
	public String approveURL;
	public String editRolesURL;
	public String applicationNo;
	public String transactionDescription;

	public boolean isVehicelInsfection;
	public boolean searchURLStatus = true;
	public boolean approveURLStatus = true;
	public boolean editRolesURLStatus = true;
	public boolean counterCheck = true;
	public boolean checkPermitIssueNewPermit = false;
	public boolean checkSessionBeanNullViewPermitRenewal = false;

	public List<SearchEmployeeDTO> tempDataList = new ArrayList<SearchEmployeeDTO>();

	public List<EmployeeDTO> tempEmpDataList = new ArrayList<EmployeeDTO>();

	public List<CommonDTO> tempInqueryDataList = new ArrayList<>();

	public List<AmendmentDTO> amendmentsList = new ArrayList<>();

	public List<PermitDTO> tempRenewalsDataList = new ArrayList<>();

	public List<SisuSeriyaDTO> tempSisuSeriyaDataList = new ArrayList<>();

	public List<TenderDTO> tempOfferLetterDataList = new ArrayList<>();

	public List<TenderDTO> tempIssueTenderDataList = new ArrayList<>();

	public List<ManageUserDTO> tempUserDataList = new ArrayList<ManageUserDTO>();

	public List<DocumentManagementDTO> optionalList = new ArrayList<DocumentManagementDTO>();
	public List<DocumentManagementDTO> newPermitMandatoryDocumentList = new ArrayList<DocumentManagementDTO>();
	public List<DocumentManagementDTO> newPermitOptionalDocumentList = new ArrayList<DocumentManagementDTO>();
	public List<DocumentManagementDTO> permitRenewalMandatoryDocumentList = new ArrayList<DocumentManagementDTO>();
	public List<DocumentManagementDTO> permitRenewalOptionalDocumentList = new ArrayList<DocumentManagementDTO>();

	public List<DocumentManagementDTO> backlogManagementOptionalDocumentList = new ArrayList<DocumentManagementDTO>();

	public List<DocumentManagementDTO> amendmentToBusOwnerMandatoryDocumentList = new ArrayList<DocumentManagementDTO>();
	public List<DocumentManagementDTO> amendmentToBusOwnerOptionalDocumentList = new ArrayList<DocumentManagementDTO>();
	public List<DocumentManagementDTO> amendmentToBusMandatoryDocumentList = new ArrayList<DocumentManagementDTO>();
	public List<DocumentManagementDTO> amendmentToBusOptionalDocumentList = new ArrayList<DocumentManagementDTO>();
	public List<DocumentManagementDTO> amendmentToServiceMandatoryDocumentList = new ArrayList<DocumentManagementDTO>();
	public List<DocumentManagementDTO> amendmentToServiceOptionalDocumentList = new ArrayList<DocumentManagementDTO>();
	public List<DocumentManagementDTO> amendmentToOwnerBusMandatoryDocumentList = new ArrayList<DocumentManagementDTO>();
	public List<DocumentManagementDTO> amendmentToOwnerBusOptionalDocumentList = new ArrayList<DocumentManagementDTO>();
	public List<DocumentManagementDTO> amendmentToServiceBusMandatoryDocumentList = new ArrayList<DocumentManagementDTO>();
	public List<DocumentManagementDTO> amendmentToServiceBusOptionalDocumentList = new ArrayList<DocumentManagementDTO>();

	public List<DocumentManagementDTO> tenderMandatoryDocumentList = new ArrayList<DocumentManagementDTO>();
	public List<DocumentManagementDTO> tenderOptionalDocumentList = new ArrayList<DocumentManagementDTO>();

	public List<DocumentManagementDTO> surveyMandatoryDocumentList = new ArrayList<DocumentManagementDTO>();
	public List<DocumentManagementDTO> surveyOptionalDocumentList = new ArrayList<DocumentManagementDTO>();

	public List<DocumentManagementDTO> sisuSariyaMandatoryDocumentList = new ArrayList<DocumentManagementDTO>();
	public List<DocumentManagementDTO> sisuSariyaOptionalDocumentList = new ArrayList<DocumentManagementDTO>();

	public List<DocumentManagementDTO> sisuSariyaServiceRefMandatoryDocumentList = new ArrayList<DocumentManagementDTO>();
	public List<DocumentManagementDTO> sisuSariyaServiceRefOptionalDocumentList = new ArrayList<DocumentManagementDTO>();

	public List<DocumentManagementDTO> sisuSariyaServiceMandatoryDocumentList = new ArrayList<DocumentManagementDTO>();
	public List<DocumentManagementDTO> sisuSariyaServiceOptionalDocumentList = new ArrayList<DocumentManagementDTO>();

	// logsheetMaintenance dont touch
	public LogSheetMaintenanceDTO sessLogDTO;
	public List<LogSheetMaintenanceDTO> sessiondatatableList = new ArrayList<LogSheetMaintenanceDTO>();
	public boolean sessiondisable = true;
	public boolean sesssionapprovalBoolean = true;

	// Gami Sariya

	public GamiSeriyaDTO gamiDTO;
	public String gamiRefNo;

	// Inquiry Details
	public List<PermitRenewalsDTO> inquirySavedDataList = new ArrayList<>(0);
	public String selectedVehicleNoInquiryDet;
	public String selectedPermitNoInquiryDet;
	public boolean goFromInquiryDetPg;
	public boolean goBackTOInquiryDetPg;
	public boolean searchedByVehicleNo;
	public boolean searchedByPermitNo;

	// Login page
	public String currentSessionId;
	// --End Modification--

	public String getTestUrl() {
		return testUrl;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public void setTestUrl(String testUrl) {
		this.testUrl = testUrl;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public String getSearchURL() {
		return searchURL;
	}

	public String getServiceType() {
		return serviceType;
	}

	public String getTrafficProposalNo() {
		return trafficProposalNo;
	}

	public void setTrafficProposalNo(String trafficProposalNo) {
		this.trafficProposalNo = trafficProposalNo;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getBusRegNo() {
		return busRegNo;
	}

	public void setBusRegNo(String busRegNo) {
		this.busRegNo = busRegNo;
	}

	public String getServiceRefNo() {
		return serviceRefNo;
	}

	public void setServiceRefNo(String serviceRefNo) {
		this.serviceRefNo = serviceRefNo;
	}

	public String getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}

	public String getServiceNo() {
		return serviceNo;
	}

	public void setServiceNo(String serviceNo) {
		this.serviceNo = serviceNo;
	}

	public List<CommonDTO> getTempInqueryDataList() {
		return tempInqueryDataList;
	}

	public void setTempInqueryDataList(List<CommonDTO> tempInqueryDataList) {
		this.tempInqueryDataList = tempInqueryDataList;
	}

	public String getTransactionDescription() {
		return transactionDescription;
	}

	public void setTransactionDescription(String transactionDescription) {
		this.transactionDescription = transactionDescription;
	}

	public void setSearchURL(String searchURL) {
		this.searchURL = searchURL;
	}

	public String getApproveURL() {
		return approveURL;
	}

	public void setApproveURL(String approveURL) {
		this.approveURL = approveURL;
	}

	public List<TenderDTO> getTempIssueTenderDataList() {
		return tempIssueTenderDataList;
	}

	public void setTempIssueTenderDataList(List<TenderDTO> tempIssueTenderDataList) {
		this.tempIssueTenderDataList = tempIssueTenderDataList;
	}

	public boolean isVehicleInspectionMood() {
		return isVehicleInspectionMood;
	}

	public void setVehicleInspectionMood(boolean isVehicleInspectionMood) {
		this.isVehicleInspectionMood = isVehicleInspectionMood;
	}

	public boolean isSearchURLStatus() {
		return searchURLStatus;
	}

	public void setSearchURLStatus(boolean searchURLStatus) {
		this.searchURLStatus = searchURLStatus;
	}

	public boolean isApproveURLStatus() {
		return approveURLStatus;
	}

	public void setApproveURLStatus(boolean approveURLStatus) {
		this.approveURLStatus = approveURLStatus;
	}

	public List<SearchEmployeeDTO> getTempDataList() {
		return tempDataList;
	}

	public void setTempDataList(List<SearchEmployeeDTO> tempDataList) {
		this.tempDataList = tempDataList;
	}

	public List<EmployeeDTO> getTempEmpDataList() {
		return tempEmpDataList;
	}

	public void setTempEmpDataList(List<EmployeeDTO> tempEmpDataList) {
		this.tempEmpDataList = tempEmpDataList;
	}

	public List<ManageUserDTO> getTempUserDataList() {
		return tempUserDataList;
	}

	public void setTempUserDataList(List<ManageUserDTO> tempUserDataList) {
		this.tempUserDataList = tempUserDataList;
	}

	public String getPageMode() {
		return pageMode;
	}

	public void setPageMode(String pageMode) {
		this.pageMode = pageMode;
	}

	public String getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(String selectedUser) {
		this.selectedUser = selectedUser;
	}

	public String getEditRolesURL() {
		return editRolesURL;
	}

	public void setEditRolesURL(String editRolesURL) {
		this.editRolesURL = editRolesURL;
	}

	public boolean isEditRolesURLStatus() {
		return editRolesURLStatus;
	}

	public void setEditRolesURLStatus(boolean editRolesURLStatus) {
		this.editRolesURLStatus = editRolesURLStatus;
	}

	public boolean isIssueTenderMood() {
		return issueTenderMood;
	}

	public void setIssueTenderMood(boolean issueTenderMood) {
		this.issueTenderMood = issueTenderMood;
	}

	public boolean isClicked() {
		return isClicked;
	}

	public void setClicked(boolean isClicked) {
		this.isClicked = isClicked;
	}

	private String functionName;
	private String initialPasswordChange;
	private String RedirecttrnNumber;

	private boolean passwordExpire;

	public void updateDialogAction() {
		this.message = null;
		this.messageHeader = null;
		RequestContext.getCurrentInstance().update("frmCommonDialogs");
	}

	public void showMessage(String messageHeader, String message, String type) {
		if (messageHeader != null && messageHeader.isEmpty())
			messageHeader = null;
		if (message != null && message.isEmpty())
			message = null;

		this.messageHeader = messageHeader;
		this.message = message;

		RequestContext.getCurrentInstance().update("frmCommonDialogs");

		if (type != null) {
			if (type.equals(MSG_TYPE_INFO)) {
				RequestContext.getCurrentInstance().execute("PF('dlgCommonInfo').show()");
			} else if (type.equals(MSG_TYPE_ERROR)) {
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
			} else if (type.equals(MSG_TYPE_WARNING)) {
				RequestContext.getCurrentInstance().execute("PF('dlgCommonWarning').show()");
			} else if (type.equals(MSG_TYPE_CONFIRM)) {
				RequestContext.getCurrentInstance().execute("PF('dlgCommonConfirm').show()");
			} else if (type.equals(MSG_TYPE_SUCCESS)) {
				RequestContext.getCurrentInstance().execute("PF('dlgCommonSuccess').show()");
			}
		}
	}

	public boolean hasAccessRights(boolean... rights) {
		boolean hasRights = false;
		for (boolean right : rights) {
			if (right) {
				hasRights = true;
				break;
			}
		}
		if (!hasRights) {
			ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
			try {
				context.redirect(context.getRequestContextPath() + "\\pages\\dashboard\\userDashboard.jsf");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}
		return true;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public boolean isRolesSet() {
		return rolesSet;
	}

	public void setRolesSet(boolean rolesSet) {
		this.rolesSet = rolesSet;
	}

	public long getLogHistorySeq() {
		return logHistorySeq;
	}

	public void setLogHistorySeq(long logHistorySeq) {
		this.logHistorySeq = logHistorySeq;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(String loginUser) {
		this.loginUser = loginUser;
	}

	public String getUserFullName() {
		return userFullName;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getPassword() {
		return password;
	}

	public boolean isVehicelInsfection() {
		return isVehicelInsfection;
	}

	public void setVehicelInsfection(boolean isVehicelInsfection) {
		this.isVehicelInsfection = isVehicelInsfection;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isOR_RR() {
		return OR_RR;
	}

	public void setOR_RR(boolean oR_RR) {
		OR_RR = oR_RR;
	}

	public boolean isOR_AR() {
		return OR_AR;
	}

	public void setOR_AR(boolean oR_AR) {
		OR_AR = oR_AR;
	}

	public boolean isOR_NC() {
		return OR_NC;
	}

	public void setOR_NC(boolean oR_NC) {
		OR_NC = oR_NC;
	}

	public boolean isOR_AN() {
		return OR_AN;
	}

	public void setOR_AN(boolean oR_AN) {
		OR_AN = oR_AN;
	}

	public String getMessageHeader() {
		return messageHeader;
	}

	public void setMessageHeader(String messageHeader) {
		this.messageHeader = messageHeader;
	}

	/**
	 * @return the functionName
	 */
	public String getFunctionName() {
		return functionName;
	}

	/**
	 * @param functionName the functionName to set
	 */
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public String getInitialPasswordChange() {
		return initialPasswordChange;
	}

	public void setInitialPasswordChange(String initialPasswordChange) {
		this.initialPasswordChange = initialPasswordChange;
	}

	public String getRedirecttrnNumber() {
		return RedirecttrnNumber;
	}

	public void setRedirecttrnNumber(String redirecttrnNumber) {
		RedirecttrnNumber = redirecttrnNumber;
	}

	public boolean isOR_ADMIN() {
		return OR_ADMIN;
	}

	public void setOR_ADMIN(boolean oR_ADMIN) {
		OR_ADMIN = oR_ADMIN;
	}

	public String getViewNewPermitURL() {
		return viewNewPermitURL;
	}

	public void setViewNewPermitURL(String viewNewPermitURL) {
		this.viewNewPermitURL = viewNewPermitURL;
	}

	public boolean isOR_TR() {
		return OR_TR;
	}

	public void setOR_TR(boolean oR_TR) {
		OR_TR = oR_TR;
	}

	public boolean isPasswordExpire() {
		return passwordExpire;
	}

	public void setPasswordExpire(boolean passwordExpire) {
		this.passwordExpire = passwordExpire;
	}

	public boolean isFN1_1() {
		return FN1_1;
	}

	public void setFN1_1(boolean fN1_1) {
		FN1_1 = fN1_1;
	}

	public boolean isFN1_2() {
		return FN1_2;
	}

	public void setFN1_2(boolean fN1_2) {
		FN1_2 = fN1_2;
	}

	public boolean isFN1_3() {
		return FN1_3;
	}

	public void setFN1_3(boolean fN1_3) {
		FN1_3 = fN1_3;
	}

	public String getUserFunction() {
		return userFunction;
	}

	public void setUserFunction(String UserFunction) {
		this.userFunction = UserFunction;
	}

	public boolean isFN2_2() {
		return FN2_2;
	}

	public void setFN2_2(boolean fN2_2) {
		FN2_2 = fN2_2;
	}

	public boolean isFN2_1() {
		return FN2_1;
	}

	public void setFN2_1(boolean fN2_1) {
		FN2_1 = fN2_1;
	}

	public boolean isFN2_3() {
		return FN2_3;
	}

	public void setFN2_3(boolean fN2_3) {
		FN2_3 = fN2_3;
	}

	public boolean isFN210() {
		return FN210;
	}

	public void setFN210(boolean fN210) {
		FN210 = fN210;
	}

	public static String getMsgTypeInfo() {
		return MSG_TYPE_INFO;
	}

	public static String getMsgTypeError() {
		return MSG_TYPE_ERROR;
	}

	public static String getMsgTypeWarning() {
		return MSG_TYPE_WARNING;
	}

	public static String getMsgTypeConfirm() {
		return MSG_TYPE_CONFIRM;
	}

	public static String getMsgTypeSuccess() {
		return MSG_TYPE_SUCCESS;
	}

	public String getEmployeeNo() {
		return employeeNo;
	}

	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}

	public boolean isFN7_1() {
		return FN7_1;
	}

	public void setFN7_1(boolean fN7_1) {
		FN7_1 = fN7_1;
	}

	public boolean isFN7_2() {
		return FN7_2;
	}

	public void setFN7_2(boolean fN7_2) {
		FN7_2 = fN7_2;
	}

	public boolean isFN1_4() {
		return FN1_4;
	}

	public void setFN1_4(boolean fN1_4) {
		FN1_4 = fN1_4;
	}

	public boolean isFN1_5() {
		return FN1_5;
	}

	public void setFN1_5(boolean fN1_5) {
		FN1_5 = fN1_5;
	}

	public boolean isFN1_6() {
		return FN1_6;
	}

	public void setFN1_6(boolean fN1_6) {
		FN1_6 = fN1_6;
	}

	public boolean isFN1_7() {
		return FN1_7;
	}

	public void setFN1_7(boolean fN1_7) {
		FN1_7 = fN1_7;
	}

	public boolean isFN1_8() {
		return FN1_8;
	}

	public void setFN1_8(boolean fN1_8) {
		FN1_8 = fN1_8;
	}

	public boolean isFN7_3() {
		return FN7_3;
	}

	public void setFN7_3(boolean fN7_3) {
		FN7_3 = fN7_3;
	}

	public String getPermitNo() {
		return permitNo;
	}

	public void setPermitNo(String permitNo) {
		this.permitNo = permitNo;
	}

	public boolean isFN7_4() {
		return FN7_4;
	}

	public void setFN7_4(boolean fN7_4) {
		FN7_4 = fN7_4;
	}

	public boolean isFN7_5() {
		return FN7_5;
	}

	public void setFN7_5(boolean fN7_5) {
		FN7_5 = fN7_5;
	}

	public boolean isFN7_6() {
		return FN7_6;
	}

	public void setFN7_6(boolean fN7_6) {
		FN7_6 = fN7_6;
	}

	public boolean isFN4_1() {
		return FN4_1;
	}

	public void setFN4_1(boolean fN4_1) {
		FN4_1 = fN4_1;
	}

	public boolean isFN4_2() {
		return FN4_2;
	}

	public void setFN4_2(boolean fN4_2) {
		FN4_2 = fN4_2;
	}

	public boolean isFN7_7() {
		return FN7_7;
	}

	public void setFN7_7(boolean fN7_7) {
		FN7_7 = fN7_7;
	}

	public boolean isFN1_9() {
		return FN1_9;
	}

	public void setFN1_9(boolean fN1_9) {
		FN1_9 = fN1_9;
	}

	public boolean isFN7_8() {
		return FN7_8;
	}

	public void setFN7_8(boolean fN7_8) {
		FN7_8 = fN7_8;
	}

	public boolean isFN7_9() {
		return FN7_9;
	}

	public void setFN7_9(boolean fN7_9) {
		FN7_9 = fN7_9;
	}

	public boolean isFN8_1() {
		return FN8_1;
	}

	public void setFN8_1(boolean fN8_1) {
		FN8_1 = fN8_1;
	}

	public boolean isFN3_1() {
		return FN3_1;
	}

	public void setFN3_1(boolean fN3_1) {
		FN3_1 = fN3_1;
	}

	public boolean isFN3_2() {
		return FN3_2;
	}

	public void setFN3_2(boolean fN3_2) {
		FN3_2 = fN3_2;
	}

	public boolean isFN3_3() {
		return FN3_3;
	}

	public void setFN3_3(boolean fN3_3) {
		FN3_3 = fN3_3;
	}

	public boolean isFN4_3() {
		return FN4_3;
	}

	public void setFN4_3(boolean fN4_3) {
		FN4_3 = fN4_3;
	}

	public boolean isFN4_4() {
		return FN4_4;
	}

	public void setFN4_4(boolean fN4_4) {
		FN4_4 = fN4_4;
	}

	public boolean isFN4_5() {
		return FN4_5;
	}

	public void setFN4_5(boolean fN4_5) {
		FN4_5 = fN4_5;
	}

	public boolean isFN4_6() {
		return FN4_6;
	}

	public void setFN4_6(boolean fN4_6) {
		FN4_6 = fN4_6;
	}

	public boolean isFN4_7() {
		return FN4_7;
	}

	public void setFN4_7(boolean fN4_7) {
		FN4_7 = fN4_7;
	}

	public boolean isFN2_4() {
		return FN2_4;
	}

	public void setFN2_4(boolean fN2_4) {
		FN2_4 = fN2_4;
	}

	public boolean isFN2_5() {
		return FN2_5;
	}

	public void setFN2_5(boolean fN2_5) {
		FN2_5 = fN2_5;
	}

	public boolean isFN2_6() {
		return FN2_6;
	}

	public void setFN2_6(boolean fN2_6) {
		FN2_6 = fN2_6;
	}

	public boolean isFN5_5() {
		return FN5_5;
	}

	public void setFN5_5(boolean fN5_5) {
		FN5_5 = fN5_5;
	}

	public String getViewedInspectionApplicationNo() {
		return viewedInspectionApplicationNo;
	}

	public void setViewedInspectionApplicationNo(String viewedInspectionApplicationNo) {
		this.viewedInspectionApplicationNo = viewedInspectionApplicationNo;
	}

	public String getViewInspectionsURL() {
		return viewInspectionsURL;
	}

	public void setViewInspectionsURL(String viewInspectionsURL) {
		this.viewInspectionsURL = viewInspectionsURL;
	}

	public boolean isViewInspectionsURLStatus() {
		return viewInspectionsURLStatus;
	}

	public void setViewInspectionsURLStatus(boolean viewInspectionsURLStatus) {
		this.viewInspectionsURLStatus = viewInspectionsURLStatus;
	}

	public String getSearchedApplicationNo() {
		return searchedApplicationNo;
	}

	public void setSearchedApplicationNo(String searchedApplicationNo) {
		this.searchedApplicationNo = searchedApplicationNo;
	}

	public String getSearchedVehicleNo() {
		return searchedVehicleNo;
	}

	public void setSearchedVehicleNo(String searchedVehicleNo) {
		this.searchedVehicleNo = searchedVehicleNo;
	}

	public String getSearchedInspectionStatus() {
		return searchedInspectionStatus;
	}

	public void setSearchedInspectionStatus(String searchedInspectionStatus) {
		this.searchedInspectionStatus = searchedInspectionStatus;
	}

	public boolean isCheckIsSearchPressed() {
		return checkIsSearchPressed;
	}

	public void setCheckIsSearchPressed(boolean checkIsSearchPressed) {
		this.checkIsSearchPressed = checkIsSearchPressed;
	}

	public boolean isCheckIsBackPressed() {
		return checkIsBackPressed;
	}

	public void setCheckIsBackPressed(boolean checkIsBackPressed) {
		this.checkIsBackPressed = checkIsBackPressed;
	}

	public boolean isCounterCheck() {
		return counterCheck;
	}

	public void setCounterCheck(boolean counterCheck) {
		this.counterCheck = counterCheck;
	}

	public boolean isViewNewPermitURLStatus() {
		return viewNewPermitURLStatus;
	}

	public void setViewNewPermitURLStatus(boolean viewNewPermitURLStatus) {
		this.viewNewPermitURLStatus = viewNewPermitURLStatus;
	}

	public boolean isFN8_2() {
		return FN8_2;
	}

	public void setFN8_2(boolean fN8_2) {
		FN8_2 = fN8_2;
	}

	public String getCounter() {
		return counter;
	}

	public void setCounter(String counter) {
		this.counter = counter;
	}

	public String getCounterId() {
		return counterId;
	}

	public void setCounterId(String counterId) {
		this.counterId = counterId;
	}

	public String getViewedPermitNo() {
		return viewedPermitNo;
	}

	public void setViewedPermitNo(String viewedPermitNo) {
		this.viewedPermitNo = viewedPermitNo;
	}

	public String getSelectedQueueNo() {
		return selectedQueueNo;
	}

	public void setSelectedQueueNo(String selectedQueueNo) {
		this.selectedQueueNo = selectedQueueNo;
	}

	public boolean isFN4_8() {
		return FN4_8;
	}

	public void setFN4_8(boolean fN4_8) {
		FN4_8 = fN4_8;
	}

	public String getPermitRenewalPermitNo() {
		return permitRenewalPermitNo;
	}

	public void setPermitRenewalPermitNo(String permitRenewalPermitNo) {
		this.permitRenewalPermitNo = permitRenewalPermitNo;
	}

	public String getPermitRenewalTranstractionTypeDescription() {
		return permitRenewalTranstractionTypeDescription;
	}

	public void setPermitRenewalTranstractionTypeDescription(String permitRenewalTranstractionTypeDescription) {
		this.permitRenewalTranstractionTypeDescription = permitRenewalTranstractionTypeDescription;
	}

	public boolean isGoToPermitRenewalUploadDocPopUp() {
		return goToPermitRenewalUploadDocPopUp;
	}

	public void setGoToPermitRenewalUploadDocPopUp(boolean goToPermitRenewalUploadDocPopUp) {
		this.goToPermitRenewalUploadDocPopUp = goToPermitRenewalUploadDocPopUp;
	}

	public List<DocumentManagementDTO> getOptionalList() {
		return optionalList;
	}

	public void setOptionalList(List<DocumentManagementDTO> optionalList) {
		this.optionalList = optionalList;
	}

	public boolean isFN8_3() {
		return FN8_3;
	}

	public void setFN8_3(boolean fN8_3) {
		FN8_3 = fN8_3;
	}

	public String getEmpNo() {
		return empNo;
	}

	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}

	public String getEmpTransaction() {
		return empTransaction;
	}

	public void setEmpTransaction(String empTransaction) {
		this.empTransaction = empTransaction;
	}

	public boolean isFN2_7() {
		return FN2_7;
	}

	public void setFN2_7(boolean fN2_7) {
		FN2_7 = fN2_7;
	}

	public Date getCalender1() {
		return calender1;
	}

	public void setCalender1(Date calender1) {
		this.calender1 = calender1;
	}

	public Date getCalender2() {
		return calender2;
	}

	public void setCalender2(Date calender2) {
		this.calender2 = calender2;
	}

	public String getFinalRemark() {
		return finalRemark;
	}

	public void setFinalRemark(String finalRemark) {
		this.finalRemark = finalRemark;
	}

	public boolean isFN8_4() {
		return FN8_4;
	}

	public void setFN8_4(boolean fN8_4) {
		FN8_4 = fN8_4;
	}

	public boolean isFN8_5() {
		return FN8_5;
	}

	public boolean isFN8_6() {
		return FN8_6;
	}

	public boolean isFN8_7() {
		return FN8_7;
	}

	public boolean isFN8_8() {
		return FN8_8;
	}

	public boolean isFN3_4() {
		return FN3_4;
	}

	public boolean isFN3_5() {
		return FN3_5;
	}

	public void setFN3_4(boolean fN3_4) {
		FN3_4 = fN3_4;
	}

	public void setFN3_5(boolean fN3_5) {
		FN3_5 = fN3_5;
	}

	public void setFN8_5(boolean fN8_5) {
		FN8_5 = fN8_5;
	}

	public void setFN8_6(boolean fN8_6) {
		FN8_6 = fN8_6;
	}

	public void setFN8_7(boolean fN8_7) {
		FN8_7 = fN8_7;
	}

	public void setFN8_8(boolean fN8_8) {
		FN8_8 = fN8_8;
	}

	public boolean isFN5_3() {
		return FN5_3;
	}

	public void setFN5_3(boolean fN5_3) {
		FN5_3 = fN5_3;
	}

	public boolean isFN3_6() {
		return FN3_6;
	}

	public boolean isFN3_7() {
		return FN3_7;
	}

	public void setFN3_6(boolean fN3_6) {
		FN3_6 = fN3_6;
	}

	public void setFN3_7(boolean fN3_7) {
		FN3_7 = fN3_7;
	}

	public boolean isFN5_2() {
		return FN5_2;
	}

	public void setFN5_2(boolean fN5_2) {
		FN5_2 = fN5_2;
	}

	public boolean isFN3_9() {
		return FN3_9;
	}

	public void setFN3_9(boolean fN3_9) {
		FN3_9 = fN3_9;
	}

	public boolean isFN3_8() {
		return FN3_8;
	}

	public void setFN3_8(boolean fN3_8) {
		FN3_8 = fN3_8;
	}

	public String getCancelationPermitNo() {
		return cancelationPermitNo;
	}

	public void setCancelationPermitNo(String cancelationPermitNo) {
		this.cancelationPermitNo = cancelationPermitNo;
	}

	public String getCancelationTranstractionTypeDes() {
		return cancelationTranstractionTypeDes;
	}

	public void setCancelationTranstractionTypeDes(String cancelationTranstractionTypeDes) {
		this.cancelationTranstractionTypeDes = cancelationTranstractionTypeDes;
	}

	public boolean isClickedCancelationDocPopup() {
		return isClickedCancelationDocPopup;
	}

	public void setClickedCancelationDocPopup(boolean isClickedCancelationDocPopup) {
		this.isClickedCancelationDocPopup = isClickedCancelationDocPopup;
	}

	public boolean isFN5_1() {
		return FN5_1;
	}

	public void setFN5_1(boolean fN5_1) {
		FN5_1 = fN5_1;
	}

	public boolean isFN6_1() {
		return FN6_1;
	}

	public boolean isFN6_2() {
		return FN6_2;
	}

	public void setFN6_1(boolean fN6_1) {
		FN6_1 = fN6_1;
	}

	public void setFN6_2(boolean fN6_2) {
		FN6_2 = fN6_2;
	}

	public boolean isFN5_4() {
		return FN5_4;
	}

	public void setFN5_4(boolean fN5_4) {
		FN5_4 = fN5_4;
	}

	public boolean isFN9_1() {
		return FN9_1;
	}

	public boolean isFN9_2() {
		return FN9_2;
	}

	public void setFN9_1(boolean fN9_1) {
		FN9_1 = fN9_1;
	}

	public void setFN9_2(boolean fN9_2) {
		FN9_2 = fN9_2;
	}

	public String getApplicationNoForDoc() {
		return applicationNoForDoc;
	}

	public boolean isCheckPermitIssueNewPermit() {
		return checkPermitIssueNewPermit;
	}

	public void setApplicationNoForDoc(String applicationNoForDoc) {
		this.applicationNoForDoc = applicationNoForDoc;
	}

	public void setCheckPermitIssueNewPermit(boolean checkPermitIssueNewPermit) {
		this.checkPermitIssueNewPermit = checkPermitIssueNewPermit;
	}

	public boolean isCheckSessionBeanNullViewPermitRenewal() {
		return checkSessionBeanNullViewPermitRenewal;
	}

	public void setCheckSessionBeanNullViewPermitRenewal(boolean checkSessionBeanNullViewPermitRenewal) {
		this.checkSessionBeanNullViewPermitRenewal = checkSessionBeanNullViewPermitRenewal;
	}

	public boolean isAmendmentPopUp() {
		return amendmentPopUp;
	}

	public void setAmendmentPopUp(boolean amendmentPopUp) {
		this.amendmentPopUp = amendmentPopUp;
	}

	public String getTenderNo() {
		return tenderNo;
	}

	public void setTenderNo(String tenderNo) {
		this.tenderNo = tenderNo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getFooter() {
		return footer;
	}

	public void setFooter(String footer) {
		this.footer = footer;
	}

	public int getLanguage() {
		return language;
	}

	public void setLanguage(int language) {
		this.language = language;
	}

	public List<DocumentManagementDTO> getNewPermitMandatoryDocumentList() {
		return newPermitMandatoryDocumentList;
	}

	public void setNewPermitMandatoryDocumentList(List<DocumentManagementDTO> newPermitMandatoryDocumentList) {
		this.newPermitMandatoryDocumentList = newPermitMandatoryDocumentList;
	}

	public List<DocumentManagementDTO> getNewPermitOptionalDocumentList() {
		return newPermitOptionalDocumentList;
	}

	public void setNewPermitOptionalDocumentList(List<DocumentManagementDTO> newPermitOptionalDocumentList) {
		this.newPermitOptionalDocumentList = newPermitOptionalDocumentList;
	}

	public String getPermitNoForDocs() {
		return permitNoForDocs;
	}

	public void setPermitNoForDocs(String permitNoForDocs) {
		this.permitNoForDocs = permitNoForDocs;
	}

	public List<DocumentManagementDTO> getPermitRenewalMandatoryDocumentList() {
		return permitRenewalMandatoryDocumentList;
	}

	public void setPermitRenewalMandatoryDocumentList(List<DocumentManagementDTO> permitRenewalMandatoryDocumentList) {
		this.permitRenewalMandatoryDocumentList = permitRenewalMandatoryDocumentList;
	}

	public List<DocumentManagementDTO> getPermitRenewalOptionalDocumentList() {
		return permitRenewalOptionalDocumentList;
	}

	public void setPermitRenewalOptionalDocumentList(List<DocumentManagementDTO> permitRenewalOptionalDocumentList) {
		this.permitRenewalOptionalDocumentList = permitRenewalOptionalDocumentList;
	}

	public boolean isFN5_6() {
		return FN5_6;
	}

	public void setFN5_6(boolean fN5_6) {
		FN5_6 = fN5_6;
	}

	public boolean isFN5_7() {
		return FN5_7;
	}

	public void setFN5_7(boolean fN5_7) {
		FN5_7 = fN5_7;
	}

	public boolean isFN9_3() {
		return FN9_3;
	}

	public void setFN9_3(boolean fN9_3) {
		FN9_3 = fN9_3;
	}

	public boolean isFN9_4() {
		return FN9_4;
	}

	public void setFN9_4(boolean fN9_4) {
		FN9_4 = fN9_4;
	}

	public boolean isFN9_5() {
		return FN9_5;
	}

	public void setFN9_5(boolean fN9_5) {
		FN9_5 = fN9_5;
	}

	public boolean isFN9_6() {
		return FN9_6;
	}

	public void setFN9_6(boolean fN9_6) {
		FN9_6 = fN9_6;
	}

	public List<DocumentManagementDTO> getBacklogManagementOptionalDocumentList() {
		return backlogManagementOptionalDocumentList;
	}

	public void setBacklogManagementOptionalDocumentList(
			List<DocumentManagementDTO> backlogManagementOptionalDocumentList) {
		this.backlogManagementOptionalDocumentList = backlogManagementOptionalDocumentList;
	}

	public List<DocumentManagementDTO> getAmendmentToBusOwnerMandatoryDocumentList() {
		return amendmentToBusOwnerMandatoryDocumentList;
	}

	public void setAmendmentToBusOwnerMandatoryDocumentList(
			List<DocumentManagementDTO> amendmentToBusOwnerMandatoryDocumentList) {
		this.amendmentToBusOwnerMandatoryDocumentList = amendmentToBusOwnerMandatoryDocumentList;
	}

	public List<DocumentManagementDTO> getAmendmentToBusOwnerOptionalDocumentList() {
		return amendmentToBusOwnerOptionalDocumentList;
	}

	public void setAmendmentToBusOwnerOptionalDocumentList(
			List<DocumentManagementDTO> amendmentToBusOwnerOptionalDocumentList) {
		this.amendmentToBusOwnerOptionalDocumentList = amendmentToBusOwnerOptionalDocumentList;
	}

	public List<DocumentManagementDTO> getAmendmentToBusMandatoryDocumentList() {
		return amendmentToBusMandatoryDocumentList;
	}

	public void setAmendmentToBusMandatoryDocumentList(
			List<DocumentManagementDTO> amendmentToBusMandatoryDocumentList) {
		this.amendmentToBusMandatoryDocumentList = amendmentToBusMandatoryDocumentList;
	}

	public List<DocumentManagementDTO> getAmendmentToBusOptionalDocumentList() {
		return amendmentToBusOptionalDocumentList;
	}

	public void setAmendmentToBusOptionalDocumentList(List<DocumentManagementDTO> amendmentToBusOptionalDocumentList) {
		this.amendmentToBusOptionalDocumentList = amendmentToBusOptionalDocumentList;
	}

	public List<DocumentManagementDTO> getAmendmentToServiceMandatoryDocumentList() {
		return amendmentToServiceMandatoryDocumentList;
	}

	public void setAmendmentToServiceMandatoryDocumentList(
			List<DocumentManagementDTO> amendmentToServiceMandatoryDocumentList) {
		this.amendmentToServiceMandatoryDocumentList = amendmentToServiceMandatoryDocumentList;
	}

	public List<DocumentManagementDTO> getAmendmentToServiceOptionalDocumentList() {
		return amendmentToServiceOptionalDocumentList;
	}

	public void setAmendmentToServiceOptionalDocumentList(
			List<DocumentManagementDTO> amendmentToServiceOptionalDocumentList) {
		this.amendmentToServiceOptionalDocumentList = amendmentToServiceOptionalDocumentList;
	}

	public List<DocumentManagementDTO> getAmendmentToOwnerBusMandatoryDocumentList() {
		return amendmentToOwnerBusMandatoryDocumentList;
	}

	public void setAmendmentToOwnerBusMandatoryDocumentList(
			List<DocumentManagementDTO> amendmentToOwnerBusMandatoryDocumentList) {
		this.amendmentToOwnerBusMandatoryDocumentList = amendmentToOwnerBusMandatoryDocumentList;
	}

	public List<DocumentManagementDTO> getAmendmentToOwnerBusOptionalDocumentList() {
		return amendmentToOwnerBusOptionalDocumentList;
	}

	public void setAmendmentToOwnerBusOptionalDocumentList(
			List<DocumentManagementDTO> amendmentToOwnerBusOptionalDocumentList) {
		this.amendmentToOwnerBusOptionalDocumentList = amendmentToOwnerBusOptionalDocumentList;
	}

	public List<DocumentManagementDTO> getAmendmentToServiceBusMandatoryDocumentList() {
		return amendmentToServiceBusMandatoryDocumentList;
	}

	public void setAmendmentToServiceBusMandatoryDocumentList(
			List<DocumentManagementDTO> amendmentToServiceBusMandatoryDocumentList) {
		this.amendmentToServiceBusMandatoryDocumentList = amendmentToServiceBusMandatoryDocumentList;
	}

	public List<DocumentManagementDTO> getAmendmentToServiceBusOptionalDocumentList() {
		return amendmentToServiceBusOptionalDocumentList;
	}

	public void setAmendmentToServiceBusOptionalDocumentList(
			List<DocumentManagementDTO> amendmentToServiceBusOptionalDocumentList) {
		this.amendmentToServiceBusOptionalDocumentList = amendmentToServiceBusOptionalDocumentList;
	}

	public boolean isFN8_0() {
		return FN8_0;
	}

	public void setFN8_0(boolean fN8_0) {
		FN8_0 = fN8_0;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public boolean isApprovebutton() {
		return approvebutton;
	}

	public void setApprovebutton(boolean approvebutton) {
		this.approvebutton = approvebutton;
	}

	public boolean isRejectbutton() {
		return rejectbutton;
	}

	public void setRejectbutton(boolean rejectbutton) {
		this.rejectbutton = rejectbutton;
	}

	public boolean isFN5_8() {
		return FN5_8;
	}

	public void setFN5_8(boolean fN5_8) {
		FN5_8 = fN5_8;
	}

	public boolean isReadOnlyFieldsInspection() {
		return readOnlyFieldsInspection;
	}

	public void setReadOnlyFieldsInspection(boolean readOnlyFieldsInspection) {
		this.readOnlyFieldsInspection = readOnlyFieldsInspection;
	}

	public String getSelectedOptionType() {
		return selectedOptionType;
	}

	public void setSelectedOptionType(String selectedOptionType) {
		this.selectedOptionType = selectedOptionType;
	}

	public boolean isFN9_7() {
		return FN9_7;
	}

	public void setFN9_7(boolean fN9_7) {
		FN9_7 = fN9_7;
	}

	public boolean isFN9_8() {
		return FN9_8;
	}

	public void setFN9_8(boolean fN9_8) {
		FN9_8 = fN9_8;
	}

	public boolean isFN9_9() {
		return FN9_9;
	}

	public void setFN9_9(boolean fN9_9) {
		FN9_9 = fN9_9;
	}

	public boolean isFN9_0() {
		return FN9_0;
	}

	public void setFN9_0(boolean fN9_0) {
		FN9_0 = fN9_0;
	}

	public boolean isSaveCheck() {
		return saveCheck;
	}

	public void setSaveCheck(boolean saveCheck) {
		this.saveCheck = saveCheck;
	}

	public boolean isEditCheck() {
		return editCheck;
	}

	public void setEditCheck(boolean editCheck) {
		this.editCheck = editCheck;
	}

	public boolean isSearchCheck() {
		return searchCheck;
	}

	public void setSearchCheck(boolean searchCheck) {
		this.searchCheck = searchCheck;
	}

	public boolean isFN3_0() {
		return FN3_0;
	}

	public void setFN3_0(boolean fN3_0) {
		FN3_0 = fN3_0;
	}

	public boolean isFN5_9() {
		return FN5_9;
	}

	public void setFN5_9(boolean fN5_9) {
		FN5_9 = fN5_9;
	}

	public boolean isFN0_1() {
		return FN0_1;
	}

	public void setFN0_1(boolean fN0_1) {
		FN0_1 = fN0_1;
	}

	public boolean isFN0_2() {
		return FN0_2;
	}

	public void setFN0_2(boolean fN0_2) {
		FN0_2 = fN0_2;
	}

	public boolean isPrintCheck() {
		return printCheck;
	}

	public void setPrintCheck(boolean printCheck) {
		this.printCheck = printCheck;
	}

	public boolean isFN0_3() {
		return FN0_3;
	}

	public void setFN0_3(boolean fN0_3) {
		FN0_3 = fN0_3;
	}

	public boolean isPdfCheck() {
		return pdfCheck;
	}

	public void setPdfCheck(boolean pdfCheck) {
		this.pdfCheck = pdfCheck;
	}

	public boolean isFN7_0() {
		return FN7_0;
	}

	public void setFN7_0(boolean fN7_0) {
		FN7_0 = fN7_0;
	}

	public List<DocumentManagementDTO> getSurveyMandatoryDocumentList() {
		return surveyMandatoryDocumentList;
	}

	public void setSurveyMandatoryDocumentList(List<DocumentManagementDTO> surveyMandatoryDocumentList) {
		this.surveyMandatoryDocumentList = surveyMandatoryDocumentList;
	}

	public List<DocumentManagementDTO> getSurveyOptionalDocumentList() {
		return surveyOptionalDocumentList;
	}

	public void setSurveyOptionalDocumentList(List<DocumentManagementDTO> surveyOptionalDocumentList) {
		this.surveyOptionalDocumentList = surveyOptionalDocumentList;
	}

	public boolean isFN6_3() {
		return FN6_3;
	}

	public void setFN6_3(boolean fN6_3) {
		FN6_3 = fN6_3;
	}

	public List<DocumentManagementDTO> getTenderMandatoryDocumentList() {
		return tenderMandatoryDocumentList;
	}

	public List<SisuSeriyaDTO> getTempSisuSeriyaDataList() {
		return tempSisuSeriyaDataList;
	}

	public void setTempSisuSeriyaDataList(List<SisuSeriyaDTO> tempSisuSeriyaDataList) {
		this.tempSisuSeriyaDataList = tempSisuSeriyaDataList;
	}

	public void setTenderMandatoryDocumentList(List<DocumentManagementDTO> tenderMandatoryDocumentList) {
		this.tenderMandatoryDocumentList = tenderMandatoryDocumentList;
	}

	public List<DocumentManagementDTO> getTenderOptionalDocumentList() {
		return tenderOptionalDocumentList;
	}

	public void setTenderOptionalDocumentList(List<DocumentManagementDTO> tenderOptionalDocumentList) {
		this.tenderOptionalDocumentList = tenderOptionalDocumentList;
	}

	public List<PermitDTO> getTempRenewalsDataList() {
		return tempRenewalsDataList;
	}

	public void setTempRenewalsDataList(List<PermitDTO> tempRenewalsDataList) {
		this.tempRenewalsDataList = tempRenewalsDataList;
	}

	public List<TenderDTO> getTempOfferLetterDataList() {
		return tempOfferLetterDataList;
	}

	public void setTempOfferLetterDataList(List<TenderDTO> tempOfferLetterDataList) {
		this.tempOfferLetterDataList = tempOfferLetterDataList;
	}

	public boolean isRenewalViewMood() {
		return renewalViewMood;
	}

	public void setRenewalViewMood(boolean renewalViewMood) {
		this.renewalViewMood = renewalViewMood;
	}

	public boolean isRenewalType() {
		return renewalType;
	}

	public void setRenewalType(boolean renewalType) {
		this.renewalType = renewalType;
	}

	public boolean isInquiryTYpe() {
		return inquiryTYpe;
	}

	public void setInquiryTYpe(boolean inquiryTYpe) {
		this.inquiryTYpe = inquiryTYpe;
	}

	public String getViewRenewalURL() {
		return viewRenewalURL;
	}

	public void setViewRenewalURL(String viewRenewalURL) {
		this.viewRenewalURL = viewRenewalURL;
	}

	public boolean isVehicleInspectionType() {
		return vehicleInspectionType;
	}

	public void setVehicleInspectionType(boolean vehicleInspectionType) {
		this.vehicleInspectionType = vehicleInspectionType;
	}

	public boolean isFN2_8() {
		return FN2_8;
	}

	public void setFN2_8(boolean fN2_8) {
		FN2_8 = fN2_8;
	}

	public boolean isFN2_9() {
		return FN2_9;
	}

	public void setFN2_9(boolean fN2_9) {
		FN2_9 = fN2_9;
	}

	public boolean isFN2_0() {
		return FN2_0;
	}

	public void setFN2_0(boolean fN2_0) {
		FN2_0 = fN2_0;
	}

	public List<AmendmentDTO> getAmendmentsList() {
		return amendmentsList;
	}

	public void setAmendmentsList(List<AmendmentDTO> amendmentsList) {
		this.amendmentsList = amendmentsList;
	}

	public boolean isAmendmentsViewMood() {
		return amendmentsViewMood;
	}

	public void setAmendmentsViewMood(boolean amendmentsViewMood) {
		this.amendmentsViewMood = amendmentsViewMood;
	}

	public String getAmendmentsApplicationNo() {
		return amendmentsApplicationNo;
	}

	public void setAmendmentsApplicationNo(String amendmentsApplicationNo) {
		this.amendmentsApplicationNo = amendmentsApplicationNo;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public List<DocumentManagementDTO> getSisuSariyaMandatoryDocumentList() {
		return sisuSariyaMandatoryDocumentList;
	}

	public void setSisuSariyaMandatoryDocumentList(List<DocumentManagementDTO> sisuSariyaMandatoryDocumentList) {
		this.sisuSariyaMandatoryDocumentList = sisuSariyaMandatoryDocumentList;
	}

	public List<DocumentManagementDTO> getSisuSariyaOptionalDocumentList() {
		return sisuSariyaOptionalDocumentList;
	}

	public void setSisuSariyaOptionalDocumentList(List<DocumentManagementDTO> sisuSariyaOptionalDocumentList) {
		this.sisuSariyaOptionalDocumentList = sisuSariyaOptionalDocumentList;
	}

	public String getRequestNoForSisuSariya() {
		return requestNoForSisuSariya;
	}

	public void setRequestNoForSisuSariya(String requestNoForSisuSariya) {
		this.requestNoForSisuSariya = requestNoForSisuSariya;
	}

	public boolean isFN8_9() {
		return FN8_9;
	}

	public void setFN8_9(boolean fN8_9) {
		FN8_9 = fN8_9;
	}

	public boolean isFN6_4() {
		return FN6_4;
	}

	public void setFN6_4(boolean fN6_4) {
		FN6_4 = fN6_4;
	}

	public boolean isFN0_4() {
		return FN0_4;
	}

	public void setFN0_4(boolean fN0_4) {
		FN0_4 = fN0_4;
	}

	public boolean isFN6_5() {
		return FN6_5;
	}

	public void setFN6_5(boolean fN6_5) {
		FN6_5 = fN6_5;
	}

	public String getRefNoSisuSariya() {
		return refNoSisuSariya;
	}

	public String getTenderApplicationNo() {
		return tenderApplicationNo;
	}

	public void setTenderApplicationNo(String tenderApplicationNo) {
		this.tenderApplicationNo = tenderApplicationNo;
	}

	public void setRefNoSisuSariya(String refNoSisuSariya) {
		this.refNoSisuSariya = refNoSisuSariya;
	}

	public boolean isPrintOffterLetterMode() {
		return isPrintOffterLetterMode;
	}

	public void setPrintOffterLetterMode(boolean isPrintOffterLetterMode) {
		this.isPrintOffterLetterMode = isPrintOffterLetterMode;
	}

	public String getAgreementNo() {
		return agreementNo;
	}

	public void setAgreementNo(String agreementNo) {
		this.agreementNo = agreementNo;
	}

	public boolean isSisuSariya() {
		return isSisuSariya;
	}

	public void setSisuSariya(boolean isSisuSariya) {
		this.isSisuSariya = isSisuSariya;
	}

	public String getServiceNoForSisuSariya() {
		return serviceNoForSisuSariya;
	}

	public void setServiceNoForSisuSariya(String serviceNoForSisuSariya) {
		this.serviceNoForSisuSariya = serviceNoForSisuSariya;
	}

	public LogSheetMaintenanceDTO getSessLogDTO() {
		return sessLogDTO;
	}

	public void setSessLogDTO(LogSheetMaintenanceDTO sessLogDTO) {
		this.sessLogDTO = sessLogDTO;
	}

	public List<LogSheetMaintenanceDTO> getSessiondatatableList() {
		return sessiondatatableList;
	}

	public void setSessiondatatableList(List<LogSheetMaintenanceDTO> sessiondatatableList) {
		this.sessiondatatableList = sessiondatatableList;
	}

	public boolean isSessiondisable() {
		return sessiondisable;
	}

	public void setSessiondisable(boolean sessiondisable) {
		this.sessiondisable = sessiondisable;
	}

	public boolean isSesssionapprovalBoolean() {
		return sesssionapprovalBoolean;
	}

	public void setSesssionapprovalBoolean(boolean sesssionapprovalBoolean) {
		this.sesssionapprovalBoolean = sesssionapprovalBoolean;
	}

	public GamiSeriyaDTO getGamiDTO() {
		return gamiDTO;
	}

	public void setGamiDTO(GamiSeriyaDTO gamiDTO) {
		this.gamiDTO = gamiDTO;
	}

	public String getGamiRefNo() {
		return gamiRefNo;
	}

	public void setGamiRefNo(String gamiRefNo) {
		this.gamiRefNo = gamiRefNo;
	}

	public boolean isFN310() {
		return FN310;
	}

	public void setFN310(boolean fN310) {
		FN310 = fN310;
	}

	public boolean isFN100() {
		return FN100;
	}

	public void setFN100(boolean fN100) {
		FN100 = fN100;
	}

	public boolean isFN101() {
		return FN101;
	}

	public void setFN101(boolean fN101) {
		FN101 = fN101;
	}

	public boolean isFN102() {
		return FN102;
	}

	public void setFN102(boolean fN102) {
		FN102 = fN102;
	}

	public boolean isFN103() {
		return FN103;
	}

	public void setFN103(boolean fN103) {
		FN103 = fN103;
	}

	public boolean isFN104() {
		return FN104;
	}

	public void setFN104(boolean fN104) {
		FN104 = fN104;
	}

	public boolean isFN105() {
		return FN105;
	}

	public void setFN105(boolean fN105) {
		FN105 = fN105;
	}

	public boolean isFN106() {
		return FN106;
	}

	public void setFN106(boolean fN106) {
		FN106 = fN106;
	}

	public boolean isFN107() {
		return FN107;
	}

	public void setFN107(boolean fN107) {
		FN107 = fN107;
	}

	public boolean isFN108() {
		return FN108;
	}

	public void setFN108(boolean fN108) {
		FN108 = fN108;
	}

	public boolean isFN109() {
		return FN109;
	}

	public void setFN109(boolean fN109) {
		FN109 = fN109;
	}

	public boolean isFN110() {
		return FN110;
	}

	public void setFN110(boolean fN110) {
		FN110 = fN110;
	}

	public boolean isFN111() {
		return FN111;
	}

	public void setFN111(boolean fN111) {
		FN111 = fN111;
	}

	public boolean isFN112() {
		return FN112;
	}

	public void setFN112(boolean fN112) {
		FN112 = fN112;
	}

	public boolean isFN113() {
		return FN113;
	}

	public void setFN113(boolean fN113) {
		FN113 = fN113;
	}

	public boolean isFN114() {
		return FN114;
	}

	public void setFN114(boolean fN114) {
		FN114 = fN114;
	}

	public boolean isFN200() {
		return FN200;
	}

	public void setFN200(boolean fN200) {
		FN200 = fN200;
	}

	public boolean isFN201() {
		return FN201;
	}

	public void setFN201(boolean fN201) {
		FN201 = fN201;
	}

	public boolean isFN202() {
		return FN202;
	}

	public void setFN202(boolean fN202) {
		FN202 = fN202;
	}

	public boolean isFN203() {
		return FN203;
	}

	public void setFN203(boolean fN203) {
		FN203 = fN203;
	}

	public boolean isFN204() {
		return FN204;
	}

	public void setFN204(boolean fN204) {
		FN204 = fN204;
	}

	public boolean isFN205() {
		return FN205;
	}

	public void setFN205(boolean fN205) {
		FN205 = fN205;
	}

	public boolean isFN206() {
		return FN206;
	}

	public void setFN206(boolean fN206) {
		FN206 = fN206;
	}

	public boolean isFN207() {
		return FN207;
	}

	public void setFN207(boolean fN207) {
		FN207 = fN207;
	}

	public List<DocumentManagementDTO> getSisuSariyaServiceRefMandatoryDocumentList() {
		return sisuSariyaServiceRefMandatoryDocumentList;
	}

	public void setSisuSariyaServiceRefMandatoryDocumentList(
			List<DocumentManagementDTO> sisuSariyaServiceRefMandatoryDocumentList) {
		this.sisuSariyaServiceRefMandatoryDocumentList = sisuSariyaServiceRefMandatoryDocumentList;
	}

	public List<DocumentManagementDTO> getSisuSariyaServiceRefOptionalDocumentList() {
		return sisuSariyaServiceRefOptionalDocumentList;
	}

	public void setSisuSariyaServiceRefOptionalDocumentList(
			List<DocumentManagementDTO> sisuSariyaServiceRefOptionalDocumentList) {
		this.sisuSariyaServiceRefOptionalDocumentList = sisuSariyaServiceRefOptionalDocumentList;
	}

	public List<DocumentManagementDTO> getSisuSariyaServiceMandatoryDocumentList() {
		return sisuSariyaServiceMandatoryDocumentList;
	}

	public void setSisuSariyaServiceMandatoryDocumentList(
			List<DocumentManagementDTO> sisuSariyaServiceMandatoryDocumentList) {
		this.sisuSariyaServiceMandatoryDocumentList = sisuSariyaServiceMandatoryDocumentList;
	}

	public List<DocumentManagementDTO> getSisuSariyaServiceOptionalDocumentList() {
		return sisuSariyaServiceOptionalDocumentList;
	}

	public void setSisuSariyaServiceOptionalDocumentList(
			List<DocumentManagementDTO> sisuSariyaServiceOptionalDocumentList) {
		this.sisuSariyaServiceOptionalDocumentList = sisuSariyaServiceOptionalDocumentList;
	}

	public String getRequestNoForGamiSariya() {
		return requestNoForGamiSariya;
	}

	public void setRequestNoForGamiSariya(String requestNoForGamiSariya) {
		this.requestNoForGamiSariya = requestNoForGamiSariya;
	}

	public String getRefNoGamiSariya() {
		return refNoGamiSariya;
	}

	public void setRefNoGamiSariya(String refNoGamiSariya) {
		this.refNoGamiSariya = refNoGamiSariya;
	}

	public String getTransactionTypeForGamiSariya() {
		return transactionTypeForGamiSariya;
	}

	public void setTransactionTypeForGamiSariya(String transactionTypeForGamiSariya) {
		this.transactionTypeForGamiSariya = transactionTypeForGamiSariya;
	}

	public String getServiceNoForGamiSariya() {
		return serviceNoForGamiSariya;
	}

	public void setServiceNoForGamiSariya(String serviceNoForGamiSariya) {
		this.serviceNoForGamiSariya = serviceNoForGamiSariya;
	}

	public boolean isFN120() {
		return FN120;
	}

	public void setFN120(boolean fN120) {
		FN120 = fN120;
	}

	public boolean isFN121() {
		return FN121;
	}

	public void setFN121(boolean fN121) {
		FN121 = fN121;
	}

	public boolean isFN130() {
		return FN130;
	}

	public void setFN130(boolean fN130) {
		FN130 = fN130;
	}

	public boolean isFN131() {
		return FN131;
	}

	public void setFN131(boolean fN131) {
		FN131 = fN131;
	}

	public boolean isFN132() {
		return FN132;
	}

	public void setFN132(boolean fN132) {
		FN132 = fN132;
	}

	public boolean isFN133() {
		return FN133;
	}

	public void setFN133(boolean fN133) {
		FN133 = fN133;
	}

	public boolean isFN134() {
		return FN134;
	}

	public void setFN134(boolean fN134) {
		FN134 = fN134;
	}

	public boolean isFN135() {
		return FN135;
	}

	public void setFN135(boolean fN135) {
		FN135 = fN135;
	}

	public boolean isFN136() {
		return FN136;
	}

	public void setFN136(boolean fN136) {
		FN136 = fN136;
	}

	public boolean isFN115() {
		return FN115;
	}

	public void setFN115(boolean fN115) {
		FN115 = fN115;
	}

	public boolean isFN208() {
		return FN208;
	}

	public void setFN208(boolean fN208) {
		FN208 = fN208;
	}

	public boolean isFN209() {
		return FN209;
	}

	public void setFN209(boolean fN209) {
		FN209 = fN209;
	}

	public boolean isFN230() {
		return FN230;
	}

	public void setFN230(boolean fN230) {
		FN230 = fN230;
	}

	public boolean isFN232() {
		return FN232;
	}

	public void setFN232(boolean fN232) {
		FN232 = fN232;
	}

	public boolean isFN231() {
		return FN231;
	}

	public void setFN231(boolean fN231) {
		FN231 = fN231;
	}

	public boolean isFN233() {
		return FN233;
	}

	public void setFN233(boolean fN233) {
		FN233 = fN233;
	}

	public boolean isFN250() {
		return FN250;
	}

	public void setFN250(boolean fN250) {
		FN250 = fN250;
	}

	public String getCommitteeApplicationNo() {
		return committeeApplicationNo;
	}

	public void setCommitteeApplicationNo(String committeeApplicationNo) {
		this.committeeApplicationNo = committeeApplicationNo;
	}

	public boolean isCommitteeView() {
		return committeeView;
	}

	public void setCommitteeView(boolean committeeView) {
		this.committeeView = committeeView;
	}

	public boolean isBackCommittee() {
		return backCommittee;
	}

	public void setBackCommittee(boolean backCommittee) {
		this.backCommittee = backCommittee;
	}

	public String getSelectedTransactionType() {
		return selectedTransactionType;
	}

	public void setSelectedTransactionType(String selectedTransactionType) {
		this.selectedTransactionType = selectedTransactionType;
	}

	public boolean isFN251() {
		return FN251;
	}

	public void setFN251(boolean fN251) {
		FN251 = fN251;
	}

	public List<PermitRenewalsDTO> getInquirySavedDataList() {
		return inquirySavedDataList;
	}

	public void setInquirySavedDataList(List<PermitRenewalsDTO> inquirySavedDataList) {
		this.inquirySavedDataList = inquirySavedDataList;
	}

	public String getSelectedVehicleNoInquiryDet() {
		return selectedVehicleNoInquiryDet;
	}

	public void setSelectedVehicleNoInquiryDet(String selectedVehicleNoInquiryDet) {
		this.selectedVehicleNoInquiryDet = selectedVehicleNoInquiryDet;
	}

	public String getSelectedPermitNoInquiryDet() {
		return selectedPermitNoInquiryDet;
	}

	public void setSelectedPermitNoInquiryDet(String selectedPermitNoInquiryDet) {
		this.selectedPermitNoInquiryDet = selectedPermitNoInquiryDet;
	}

	public boolean isGoFromInquiryDetPg() {
		return goFromInquiryDetPg;
	}

	public void setGoFromInquiryDetPg(boolean goFromInquiryDetPg) {
		this.goFromInquiryDetPg = goFromInquiryDetPg;
	}

	public boolean isGoBackTOInquiryDetPg() {
		return goBackTOInquiryDetPg;
	}

	public void setGoBackTOInquiryDetPg(boolean goBackTOInquiryDetPg) {
		this.goBackTOInquiryDetPg = goBackTOInquiryDetPg;
	}

	public boolean isSearchedByVehicleNo() {
		return searchedByVehicleNo;
	}

	public void setSearchedByVehicleNo(boolean searchedByVehicleNo) {
		this.searchedByVehicleNo = searchedByVehicleNo;
	}

	public boolean isSearchedByPermitNo() {
		return searchedByPermitNo;
	}

	public void setSearchedByPermitNo(boolean searchedByPermitNo) {
		this.searchedByPermitNo = searchedByPermitNo;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public boolean isFN161() {
		return FN161;
	}

	public void setFN161(boolean fN161) {
		FN161 = fN161;
	}

	public boolean isFN162() {
		return FN162;
	}

	public void setFN162(boolean fN162) {
		FN162 = fN162;
	}

	public boolean isFN163() {
		return FN163;
	}

	public void setFN163(boolean fN163) {
		FN163 = fN163;
	}

	public boolean isFN137() {
		return FN137;
	}

	public void setFN137(boolean fN137) {
		FN137 = fN137;
	}

	public boolean isFN138() {
		return FN138;
	}

	public void setFN138(boolean fN138) {
		FN138 = fN138;
	}

	public boolean isFN139() {
		return FN139;
	}

	public void setFN139(boolean fN139) {
		FN139 = fN139;
	}

	public boolean isFN140() {
		return FN140;
	}

	public void setFN140(boolean fN140) {
		FN140 = fN140;
	}

	public boolean isFN141() {
		return FN141;
	}

	public void setFN141(boolean fN141) {
		FN141 = fN141;
	}

	public boolean isFN142() {
		return FN142;
	}

	public void setFN142(boolean fN142) {
		FN142 = fN142;
	}

	public boolean isFN143() {
		return FN143;
	}

	public void setFN143(boolean fN143) {
		FN143 = fN143;
	}

	public boolean isFN144() {
		return FN144;
	}

	public void setFN144(boolean fN144) {
		FN144 = fN144;
	}

	public String getCurrentSessionId() {
		return currentSessionId;
	}

	public void setCurrentSessionId(String currentSessionId) {
		this.currentSessionId = currentSessionId;
	}

	public boolean isFN145() {
		return FN145;
	}

	public void setFN145(boolean fN145) {
		FN145 = fN145;
	}

	public boolean isFN146() {
		return FN146;
	}

	public void setFN146(boolean fN146) {
		FN146 = fN146;
	}

	public boolean isFN147() {
		return FN147;
	}

	public void setFN147(boolean fN147) {
		FN147 = fN147;
	}

	public boolean isFN148() {
		return FN148;
	}

	public void setFN148(boolean fN148) {
		FN148 = fN148;
	}

	public boolean isFN116() {
		return FN116;
	}

	public void setFN116(boolean fN116) {
		FN116 = fN116;
	}

	public boolean isFN300() {
		return FN300;
	}

	public void setFN300(boolean fN300) {
		FN300 = fN300;
	}

	public boolean isFN301() {
		return FN301;
	}

	public void setFN301(boolean fN301) {
		FN301 = fN301;
	}

	public boolean isFN302() {
		return FN302;
	}

	public void setFN302(boolean fN302) {
		FN302 = fN302;
	}

	public boolean isFN303() {
		return FN303;
	}

	public void setFN303(boolean fN303) {
		FN303 = fN303;
	}

	public boolean isFN304() {
		return FN304;
	}

	public void setFN304(boolean fN304) {
		FN304 = fN304;
	}

	public boolean isFN305() {
		return FN305;
	}

	public void setFN305(boolean fN305) {
		FN305 = fN305;
	}

	public boolean isFN306() {
		return FN306;
	}

	public void setFN306(boolean fN306) {
		FN306 = fN306;
	}

	public boolean isFN307() {
		return FN307;
	}

	public void setFN307(boolean fN307) {
		FN307 = fN307;
	}

	public boolean isFN308() {
		return FN308;
	}

	public void setFN308(boolean fN308) {
		FN308 = fN308;
	}

	public boolean isFN270() {
		return FN270;
	}

	public void setFN270(boolean fN270) {
		FN270 = fN270;
	}

	public boolean isFN271() {
		return FN271;
	}

	public void setFN271(boolean fN271) {
		FN271 = fN271;
	}

	public boolean isFN272() {
		return FN272;
	}

	public void setFN272(boolean fN272) {
		FN272 = fN272;
	}

	public boolean isFN273() {
		return FN273;
	}

	public void setFN273(boolean fN273) {
		FN273 = fN273;
	}

	public boolean isFN330() {
		return FN330;
	}

	public void setFN330(boolean fN330) {
		FN330 = fN330;
	}

	public boolean isFN331() {
		return FN331;
	}

	public void setFN331(boolean fN331) {
		FN331 = fN331;
	}

	public boolean isFN274() {
		return FN274;
	}

	public void setFN274(boolean fN274) {
		FN274 = fN274;
	}

	public boolean isFN275() {
		return FN275;
	}

	public void setFN275(boolean fN275) {
		FN275 = fN275;
	}

	public boolean isFN332() {
		return FN332;
	}

	public void setFN332(boolean fN332) {
		FN332 = fN332;
	}

	public boolean isFN122() {
		return FN122;
	}

	public void setFN122(boolean fN122) {
		FN122 = fN122;
	}

	public boolean isFN333() {
		return FN333;
	}

	public void setFN333(boolean fN333) {
		FN333 = fN333;
	}

	public boolean isFN252() {
		return FN252;
	}

	public void setFN252(boolean fN252) {
		FN252 = fN252;
	}

	public boolean isFN334() {
		return FN334;
	}

	public void setFN334(boolean fN334) {
		FN334 = fN334;
	}

	public boolean isFN336() {
		return FN336;
	}

	public void setFN336(boolean fN336) {
		FN336 = fN336;
	}

	public boolean isFN337() {
		return FN337;
	}

	public void setFN337(boolean fN337) {
		FN337 = fN337;
	}

	public boolean isFN253() {
		return FN253;
	}

	public void setFN253(boolean fN253) {
		FN253 = fN253;
	}

	public boolean isFN338() {
		return FN338;
	}

	public void setFN338(boolean fN338) {
		FN338 = fN338;
	}

	public boolean isFN164() {
		return FN164;
	}

	public void setFN164(boolean fN164) {
		FN164 = fN164;
	}

	public boolean isFN350() {
		return FN350;
	}

	public void setFN350(boolean fN350) {
		FN350 = fN350;
	}

	public boolean isFN351() {
		return FN351;
	}

	public void setFN351(boolean fN351) {
		FN351 = fN351;
	}

	public boolean isFN352() {
		return FN352;
	}

	public void setFN352(boolean fN352) {
		FN352 = fN352;
	}

	public boolean isFN353() {
		return FN353;
	}

	public void setFN353(boolean fN353) {
		FN353 = fN353;
	}

	public boolean isFN309() {
		return FN309;
	}

	public void setFN309(boolean fN309) {
		FN309 = fN309;
	}

	public boolean isFN311() {
		return FN311;
	}

	public void setFN311(boolean fN311) {
		FN311 = fN311;
	}

	public boolean isFN312() {
		return FN312;
	}

	public void setFN312(boolean fN312) {
		FN312 = fN312;
	}

	public boolean isFN400() {
		return FN400;
	}

	public void setFN400(boolean fN400) {
		FN400 = fN400;
	}

	public boolean isFN401() {
		return FN401;
	}

	public void setFN401(boolean fN401) {
		FN401 = fN401;
	}

	public boolean isFN402() {
		return FN402;
	}

	public void setFN402(boolean fN402) {
		FN402 = fN402;
	}

	public boolean isFN403() {
		return FN403;
	}

	public void setFN403(boolean fN403) {
		FN403 = fN403;
	}

	public boolean isFN450() {
		return FN450;
	}

	public void setFN450(boolean fN450) {
		FN450 = fN450;
	}

	public boolean isFN451() {
		return FN451;
	}

	public void setFN451(boolean fN451) {
		FN451 = fN451;
	}

	public boolean isFN452() {
		return FN452;
	}

	public void setFN452(boolean fN452) {
		FN452 = fN452;
	}

	public boolean isFN453() {
		return FN453;
	}

	public void setFN453(boolean fN453) {
		FN453 = fN453;
	}

	public boolean isFN500() {
		return FN500;
	}

	public void setFN500(boolean fN500) {
		FN500 = fN500;
	}

	public boolean isFN404() {
		return FN404;
	}

	public void setFN404(boolean fN404) {
		FN404 = fN404;
	}

	public boolean isFN405() {
		return FN405;
	}

	public void setFN405(boolean fN405) {
		FN405 = fN405;
	}

	public boolean isFN354() {
		return FN354;
	}

	public void setFN354(boolean fN354) {
		FN354 = fN354;
	}

	public boolean isFN501() {
		return FN501;
	}

	public void setFN501(boolean fN501) {
		FN501 = fN501;
	}

	public boolean isFN355() {
		return FN355;
	}

	public void setFN355(boolean fN355) {
		FN355 = fN355;
	}

	public boolean isFN356() {
		return FN356;
	}

	public void setFN356(boolean fN356) {
		FN356 = fN356;
	}

	public boolean isFN357() {
		return FN357;
	}

	public void setFN357(boolean fN357) {
		FN357 = fN357;
	}

	public String getFlyrefNo() {
		return flyrefNo;
	}

	public void setFlyrefNo(String flyrefNo) {
		this.flyrefNo = flyrefNo;
	}

	public boolean isFN406() {
		return FN406;
	}

	public void setFN406(boolean fN406) {
		FN406 = fN406;
	}

	public boolean isFN407() {
		return FN407;
	}

	public void setFN407(boolean fN407) {
		FN407 = fN407;
	}

	public boolean isFN408() {
		return FN408;
	}

	public void setFN408(boolean fN408) {
		FN408 = fN408;
	}

	public boolean isFN409() {
		return FN409;
	}

	public void setFN409(boolean fN409) {
		FN409 = fN409;
	}

	public boolean isFN410() {
		return FN410;
	}

	public void setFN410(boolean fN410) {
		FN410 = fN410;
	}

	public boolean isFN411() {
		return FN411;
	}

	public void setFN411(boolean fN411) {
		FN411 = fN411;
	}

	public boolean isFN412() {
		return FN412;
	}

	public void setFN412(boolean fN412) {
		FN412 = fN412;
	}

	public boolean isFN413() {
		return FN413;
	}

	public void setFN413(boolean fN413) {
		FN413 = fN413;
	}

	public boolean isFN313() {
		return FN313;
	}

	public void setFN313(boolean fN313) {
		FN313 = fN313;
	}

	public boolean isFN358() {
		return FN358;
	}

	public void setFN358(boolean fN358) {
		FN358 = fN358;
	}

	public boolean isFN414() {
		return FN414;
	}

	public void setFN414(boolean fN414) {
		FN414 = fN414;
	}

	public boolean isFN459() {
		return FN459;
	}

	public void setFN459(boolean fN459) {
		FN459 = fN459;
	}

	public boolean isFN460() {
		return FN460;
	}

	public void setFN460(boolean fN460) {
		FN460 = fN460;
	}

	public boolean isFN461() {
		return FN461;
	}

	public void setFN461(boolean fN461) {
		FN461 = fN461;
	}

	public boolean isFN359() {
		return FN359;
	}

	public void setFN359(boolean fN359) {
		FN359 = fN359;
	}

	public boolean isFN335() {
		return FN335;
	}

	public void setFN335(boolean fN335) {
		FN335 = fN335;
	}

	public boolean isFN360() {
		return FN360;
	}

	public void setFN360(boolean fN360) {
		FN360 = fN360;
	}

	public boolean isFN361() {
		return FN361;
	}

	public void setFN361(boolean fN361) {
		FN361 = fN361;
	}

	public boolean isFN314() {
		return FN314;
	}

	public void setFN314(boolean fN314) {
		FN314 = fN314;
	}

	public boolean isFN362() {
		return FN362;
	}

	public void setFN362(boolean fN362) {
		FN362 = fN362;
	}

	public boolean isFN454() {
		return FN454;
	}

	public void setFN454(boolean fN454) {
		FN454 = fN454;
	}

	public boolean isFN455() {
		return FN455;
	}

	public void setFN455(boolean fN455) {
		FN455 = fN455;
	}

	public boolean isFN363() {
		return FN363;
	}

	public void setFN363(boolean fN363) {
		FN363 = fN363;
	}

	public String getInvesNo() {
		return invesNo;
	}

	public void setInvesNo(String invesNo) {
		this.invesNo = invesNo;
	}

	public boolean isFN364() {
		return FN364;
	}

	public void setFN364(boolean fN364) {
		FN364 = fN364;
	}

	public String getDriverConductorId() {
		return driverConductorId;
	}

	public void setDriverConductorId(String driverConductorId) {
		this.driverConductorId = driverConductorId;
	}

	public String getDcAppNo() {
		return dcAppNo;
	}

	public void setDcAppNo(String dcAppNo) {
		this.dcAppNo = dcAppNo;
	}

	public boolean isFN365() {
		return FN365;
	}

	public void setFN365(boolean fN365) {
		FN365 = fN365;
	}

	public String getComplainNo() {
		return complainNo;
	}

	public void setComplainNo(String complainNo) {
		this.complainNo = complainNo;
	}

	public boolean isFN117() {
		return FN117;
	}

	public void setFN117(boolean fN117) {
		FN117 = fN117;
	}

	public boolean isFN520() {
		return FN520;
	}

	public void setFN520(boolean fN520) {
		FN520 = fN520;
	}

	public boolean isFN521() {
		return FN521;
	}

	public void setFN521(boolean fN521) {
		FN521 = fN521;
	}

	public boolean isFN522() {
		return FN522;
	}

	public void setFN522(boolean fN522) {
		FN522 = fN522;
	}

	public boolean isFN523() {
		return FN523;
	}

	public void setFN523(boolean fN523) {
		FN523 = fN523;
	}

	public boolean isFN315() {
		return FN315;
	}

	public void setFN315(boolean fN315) {
		FN315 = fN315;
	}

	public boolean isFN524() {
		return FN524;
	}

	public void setFN524(boolean fN524) {
		FN524 = fN524;
	}

	public boolean isFN550() {
		return FN550;
	}

	public void setFN550(boolean fN550) {
		FN550 = fN550;
	}

	public boolean isFN551() {
		return FN551;
	}

	public void setFN551(boolean fN551) {
		FN551 = fN551;
	}

	public boolean isFN552() {
		return FN552;
	}

	public void setFN552(boolean fN552) {
		FN552 = fN552;
	}

	public boolean isFN553() {
		return FN553;
	}

	public void setFN553(boolean fN553) {
		FN553 = fN553;
	}

	public String getSimNo() {
		return simNo;
	}

	public void setSimNo(String simNo) {
		this.simNo = simNo;
	}

	public String getSimRegNo() {
		return simRegNo;
	}

	public void setSimRegNo(String simRegNo) {
		this.simRegNo = simRegNo;
	}

	public boolean isFN554() {
		return FN554;
	}

	public void setFN554(boolean fN554) {
		FN554 = fN554;
	}

	public boolean isFN254() {
		return FN254;
	}

	public void setFN254(boolean fN254) {
		FN254 = fN254;
	}

	public boolean isFN118() {
		return FN118;
	}

	public void setFN118(boolean fN118) {
		FN118 = fN118;
	}

	public boolean isFN366() {
		return FN366;
	}

	public void setFN366(boolean fN366) {
		FN366 = fN366;
	}

	public boolean isFN367() {
		return FN367;
	}

	public void setFN367(boolean fN367) {
		FN367 = fN367;
	}

	public boolean isFN368() {
		return FN368;
	}

	public void setFN368(boolean fN368) {
		FN368 = fN368;
	}

	public boolean isFN415() {
		return FN415;
	}

	public void setFN415(boolean fN415) {
		FN415 = fN415;
	}

	public boolean isFN119() {
		return FN119;
	}

	public void setFN119(boolean fN119) {
		FN119 = fN119;
	}

	public boolean isFN1_10() {
		return FN1_10;
	}

	public void setFN1_10(boolean fN1_10) {
		FN1_10 = fN1_10;
	}

	public boolean isFN001() {
		return FN001;
	}

	public void setFN001(boolean fN001) {
		FN001 = fN001;
	}

	public boolean isFN002() {
		return FN002;
	}

	public void setFN002(boolean fN002) {
		FN002 = fN002;
	}

	public boolean isFN003() {
		return FN003;
	}

	public void setFN003(boolean fN003) {
		FN003 = fN003;
	}

	public boolean isFN004() {
		return FN004;
	}

	public void setFN004(boolean fN004) {
		FN004 = fN004;
	}

	public boolean isFN211() {
		return FN211;
	}

	public void setFN211(boolean fN211) {
		FN211 = fN211;
	}

	public boolean isFN212() {
		return FN212;
	}

	public void setFN212(boolean fN212) {
		FN212 = fN212;
	}

	public boolean isFN213() {
		return FN213;
	}

	public void setFN213(boolean fN213) {
		FN213 = fN213;
	}

	public boolean isFN214() {
		return FN214;
	}

	public void setFN214(boolean fN214) {
		FN214 = fN214;
	}

	public boolean isFN215() {
		return FN215;
	}

	public void setFN215(boolean fN215) {
		FN215 = fN215;
	}

	public boolean isFN216() {
		return FN216;
	}

	public void setFN216(boolean fN216) {
		FN216 = fN216;
	}

	public boolean isFN123() {
		return FN123;
	}

	public void setFN123(boolean fN123) {
		FN123 = fN123;
	}

	public boolean isFN416() {
		return FN416;
	}

	public void setFN416(boolean fN416) {
		FN416 = fN416;
	}

	public boolean isFN456() {
		return FN456;
	}

	public void setFN456(boolean fN456) {
		FN456 = fN456;
	}

	public boolean isFN457() {
		return FN457;
	}

	public void setFN457(boolean fN457) {
		FN457 = fN457;
	}

	public boolean isFN1_11() {
		return FN1_11;
	}

	public void setFN1_11(boolean fN1_11) {
		FN1_11 = fN1_11;
	}

	public boolean isFN525() {
		return FN525;
	}

	public void setFN525(boolean fN525) {
		FN525 = fN525;
	}

	public boolean isFN600() {
		return FN600;
	}

	public void setFN600(boolean fN600) {
		FN600 = fN600;
	}

	public boolean isFN149() {
		return FN149;
	}

	public void setFN149(boolean fN149) {
		FN149 = fN149;
	}

	public boolean isFN316() {
		return FN316;
	}

	public void setFN316(boolean fN316) {
		FN316 = fN316;
	}

	public boolean isFN317() {
		return FN317;
	}

	public void setFN317(boolean fN317) {
		FN317 = fN317;
	}

	public boolean isFN318() {
		return FN318;
	}

	public void setFN318(boolean fN318) {
		FN318 = fN318;
	}

	public boolean isFN319() {
		return FN319;
	}

	public void setFN319(boolean fN319) {
		FN319 = fN319;
	}

	public boolean isFN417() {
		return FN417;
	}

	public void setFN417(boolean fN417) {
		FN417 = fN417;
	}

	public boolean isFN339() {
		return FN339;
	}

	public void setFN339(boolean fN339) {
		FN339 = fN339;
	}

}
