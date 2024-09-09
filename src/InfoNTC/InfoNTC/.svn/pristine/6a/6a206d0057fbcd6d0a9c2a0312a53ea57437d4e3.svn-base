package lk.informatics.ntc.view.beans;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
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
import org.primefaces.event.SelectEvent;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.DocumentManagementDTO;
import lk.informatics.ntc.model.dto.GenerateReceiptDTO;
import lk.informatics.ntc.model.dto.NisiSeriyaDTO;
import lk.informatics.ntc.model.dto.RouteDTO;
import lk.informatics.ntc.model.service.AdminService;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.DocumentManagementService;
import lk.informatics.ntc.model.service.EmployeeProfileService;
import lk.informatics.ntc.model.service.NisiSeriyaService;
import lk.informatics.ntc.model.service.SisuSariyaService;
import lk.informatics.ntc.model.service.SurveyService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "nisiSeriyaPermitHolderInfoBean")
@ViewScoped
public class NisiSeriyaPermitHolderInfoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	/** nisi seriay variables **/
	private NisiSeriyaService nisiSeriyaService;
	private SisuSariyaService sisuSariyaService;
	private String requestedNo;
	private String serviceRefNo;
	private boolean genReNumDisable;
	private boolean disableAddBtn;
	private boolean disableDataTable;
	// tab 01 start
	private NisiSeriyaDTO nisiSeriyaDTO;
	private List<RouteDTO> originList;
	private List<RouteDTO> destinationList;
	private List<RouteDTO> viaList;
	private List<NisiSeriyaDTO> nisiSeriyaDTOList;
	private NisiSeriyaDTO selectedNisiSeriyaDTO;
	private int activeTabIndex;
	private boolean view;
	private boolean renderEditMode;
	private boolean disableTab01;
	private boolean disableTab02;
	private boolean disableBankInfo;
	private String alertMsg;
	private List<CommonDTO> drpdServiceTypeList;
	private List<CommonDTO> drpdProvincelList;
	private List<CommonDTO> drpdDistrictList;
	private List<CommonDTO> drpdDevsecList;
	private List<GenerateReceiptDTO> drpdBankList;
	private List<GenerateReceiptDTO> drpdBankBranchList;
	private List<DocumentManagementDTO> mandatoryList = new ArrayList<DocumentManagementDTO>(0);
	private List<DocumentManagementDTO> optionalList = new ArrayList<DocumentManagementDTO>(0);
	// tab 01 end
	/** nisi seriay variables **/

	// tab01
	private AdminService adminService;
	private EmployeeProfileService employeeProfileService;
	private SurveyService surveyService;
	private DocumentManagementService documentManagementService;;
	private CommonService commonService;
	private String errorMsg;
	private Date requestStartDate;
	private Date requestEndtDate;

	String dateFormat = "dd/MM/yyyy";
	SimpleDateFormat frm = new SimpleDateFormat(dateFormat);

	String endDateValue;
	String startDateValue;

	private boolean disableVia;
	private boolean renderTabView;
	private boolean renderBtnSearch;
	private boolean renderViewMode;

	@PostConstruct
	public void init() {

		/** nisi seriya start **/
		nisiSeriyaService = (NisiSeriyaService) SpringApplicationContex.getBean("nisiSeriyaService");
		sisuSariyaService = (SisuSariyaService) SpringApplicationContex.getBean("sisuSariyaService");
		surveyService = (SurveyService) SpringApplicationContex.getBean("surveyService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		documentManagementService = (DocumentManagementService) SpringApplicationContex
				.getBean("documentManagementService");

		nisiSeriyaDTO = new NisiSeriyaDTO();
		requestedNo = null;
		genReNumDisable = false;
		originList = new ArrayList<RouteDTO>();
		originList = nisiSeriyaService.retrieveOriginList();
		destinationList = new ArrayList<RouteDTO>();
		viaList = new ArrayList<RouteDTO>();
		serviceRefNo = null;
		nisiSeriyaDTOList = new ArrayList<NisiSeriyaDTO>();
		setDisableAddBtn(false);
		disableTab02 = true;
		setDisableDataTable(false);
		selectedNisiSeriyaDTO = new NisiSeriyaDTO();

		view = false;
		disableBankInfo = false;
		endDateValue = "";
		startDateValue = "";
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");
		employeeProfileService = (EmployeeProfileService) SpringApplicationContex.getBean("employeeProfileService");

		drpdBankList = new ArrayList<GenerateReceiptDTO>();
		drpdBankBranchList = new ArrayList<GenerateReceiptDTO>();

		drpdServiceTypeList = adminService.getServiceTypeToDropdown();
		drpdProvincelList = adminService.getProvinceToDropdown();
		drpdDevsecList = adminService.getDivSecToDropdown();
		drpdDistrictList = adminService.getDistrictToDropdown();
		drpdBankList = surveyService.getBankListDropDown();

		if (sessionBackingBean.isSisuSariya() == true) {

			getServiceInformation(sessionBackingBean.getServiceRefNo());

			sessionBackingBean.setSisuSariya(false);

			renderTabView = true;
			renderBtnSearch = true;
		}
	}

	/** nisi seriya methods start **/
	// Tab 01 start
	public void generateRequestNoAction() {

		requestedNo = nisiSeriyaService.generateNisiSeriyaRequestNo();
		/* added by dinushi.r */
		sisuSariyaService.updateNumberGeneration("NSR", requestedNo, sessionBackingBean.loginUser);

		if (requestedNo != null && !requestedNo.isEmpty() && !requestedNo.trim().equalsIgnoreCase("")) {
			genReNumDisable = true;
		}

		RequestContext.getCurrentInstance().update("nisiSeriyaMainFrm");
	}

	public void onOriginChange() {

		if (nisiSeriyaDTO.getOriginCode() != null && !nisiSeriyaDTO.getOriginCode().isEmpty()
				&& !nisiSeriyaDTO.getOriginCode().equals("")) {

			destinationList = nisiSeriyaService.retrieveDestinationList(nisiSeriyaDTO.getOriginCode());
			disableVia = false;

		} else {

			disableVia = true;
		}
	}

	public void onDestinationChange() {

		if (nisiSeriyaDTO.getDestinationCode() != null && !nisiSeriyaDTO.getDestinationCode().isEmpty()
				&& !nisiSeriyaDTO.getDestinationCode().equals("") && nisiSeriyaDTO.getOriginCode() != null
				&& !nisiSeriyaDTO.getOriginCode().isEmpty() && !nisiSeriyaDTO.getOriginCode().equals("")) {

			viaList = nisiSeriyaService.retrieveViaList(nisiSeriyaDTO.getOriginCode(),
					nisiSeriyaDTO.getDestinationCode());
			disableVia = false;

		} else {
			disableVia = true;
		}

	}

	public void saveBtnActionForServiceInfo() {

		String dateFormat = "dd/MM/yyyy";
		SimpleDateFormat frm = new SimpleDateFormat(dateFormat);

		if (requestedNo != null && !requestedNo.isEmpty() && !requestedNo.trim().equalsIgnoreCase("")) {
			nisiSeriyaDTO.setRequestNo(requestedNo);
			if (nisiSeriyaDTO.getLanguageCode() != null && !nisiSeriyaDTO.getLanguageCode().isEmpty()
					&& !nisiSeriyaDTO.getLanguageCode().equalsIgnoreCase("")) {

				if (nisiSeriyaDTO.getServiceTypeCode() != null && !nisiSeriyaDTO.getServiceTypeCode().isEmpty()
						&& !nisiSeriyaDTO.getServiceTypeCode().equalsIgnoreCase("")) {

					if (nisiSeriyaDTO.getBusRegNo() != null && !nisiSeriyaDTO.getBusRegNo().isEmpty()
							&& !nisiSeriyaDTO.getBusRegNo().equalsIgnoreCase("")) {

						if (nisiSeriyaDTO.getOriginCode() != null && !nisiSeriyaDTO.getOriginCode().isEmpty()
								&& !nisiSeriyaDTO.getOriginCode().equalsIgnoreCase("")) {

							if (nisiSeriyaDTO.getDestinationCode() != null
									&& !nisiSeriyaDTO.getDestinationCode().isEmpty()
									&& !nisiSeriyaDTO.getDestinationCode().equalsIgnoreCase("")) {

								if (nisiSeriyaDTO.getDistance() != 0) {

									if (nisiSeriyaDTO.getTripsPerDay() != 0) {

										if (nisiSeriyaDTO.getNicNo() != null && !nisiSeriyaDTO.getNicNo().isEmpty()
												&& !nisiSeriyaDTO.getNicNo().equalsIgnoreCase("")) {

											if (nisiSeriyaDTO.getNameOfOperator() != null
													&& !nisiSeriyaDTO.getNameOfOperator().isEmpty()
													&& !nisiSeriyaDTO.getNameOfOperator().equalsIgnoreCase("")) {

												if (nisiSeriyaDTO.getAddressOne() != null
														&& !nisiSeriyaDTO.getAddressOne().isEmpty()
														&& !nisiSeriyaDTO.getAddressOne().equalsIgnoreCase("")) {

													if (nisiSeriyaDTO.getCity() != null
															&& !nisiSeriyaDTO.getCity().isEmpty()
															&& !nisiSeriyaDTO.getCity().equalsIgnoreCase("")) {

														if (nisiSeriyaDTO.getProvinceCode() != null
																&& !nisiSeriyaDTO.getProvinceCode().isEmpty()
																&& !nisiSeriyaDTO.getProvinceCode()
																		.equalsIgnoreCase("")) {

															/**
															 * data insert start
															 */
															if (nisiSeriyaDTO.getLanguageCode().equals("S")) {

																if (nisiSeriyaDTO.getAddressOneSin() != null
																		&& !nisiSeriyaDTO.getAddressOneSin().isEmpty()
																		&& !nisiSeriyaDTO.getAddressOneSin()
																				.equalsIgnoreCase("")) {

																	if (nisiSeriyaDTO.getCitySin() != null
																			&& !nisiSeriyaDTO.getCitySin().isEmpty()
																			&& !nisiSeriyaDTO.getCitySin()
																					.equalsIgnoreCase("")) {
																		// sinhala
																		// variables
																		// are
																		// set
																	} else {
																		errorMsg = "City Sinhala should be entered.";
																		RequestContext.getCurrentInstance()
																				.update("nisiSeriyaMainFrm");
																		RequestContext.getCurrentInstance()
																				.execute("PF('requiredField').show()");
																		return;
																	}

																} else {
																	errorMsg = "Address Line One Sinhala should be entered.";
																	RequestContext.getCurrentInstance()
																			.update("nisiSeriyaMainFrm");
																	RequestContext.getCurrentInstance()
																			.execute("PF('requiredField').show()");
																	return;
																}

															} else if (nisiSeriyaDTO.getLanguageCode().equals("T")) {

																if (nisiSeriyaDTO.getAddressOneTamil() != null
																		&& !nisiSeriyaDTO.getAddressOneTamil().isEmpty()
																		&& !nisiSeriyaDTO.getAddressOneTamil()
																				.equalsIgnoreCase("")) {

																	if (nisiSeriyaDTO.getCityTamil() != null
																			&& !nisiSeriyaDTO.getCityTamil().isEmpty()
																			&& !nisiSeriyaDTO.getCityTamil()
																					.equalsIgnoreCase("")) {

																		// tamil
																		// variables
																		// are
																		// set

																	} else {
																		errorMsg = "City Tamil should be entered.";
																		RequestContext.getCurrentInstance()
																				.update("nisiSeriyaMainFrm");
																		RequestContext.getCurrentInstance()
																				.execute("PF('requiredField').show()");
																		return;
																	}

																} else {
																	errorMsg = "Address Line One Tamil should be entered.";
																	RequestContext.getCurrentInstance()
																			.update("nisiSeriyaMainFrm");
																	RequestContext.getCurrentInstance()
																			.execute("PF('requiredField').show()");
																	return;
																}

															}

															if (nisiSeriyaDTO.getPermitNo() != null
																	&& !nisiSeriyaDTO.getPermitNo().isEmpty()
																	&& !nisiSeriyaDTO.getPermitNo().trim()
																			.equalsIgnoreCase("")) {
																boolean permitNumDuplicate = nisiSeriyaService
																		.validatePermitNumberDuplicate(nisiSeriyaDTO);

																if (permitNumDuplicate) {
																	errorMsg = "Duplicate Permit No.";
																	RequestContext.getCurrentInstance()
																			.update("nisiSeriyaMainFrm");
																	RequestContext.getCurrentInstance()
																			.execute("PF('requiredField').show()");
																	return;
																}
															}

															String serviceReferenceNum = nisiSeriyaService
																	.insertDataIntoNt_m_nri_requester_info(
																			nisiSeriyaDTO,
																			sessionBackingBean.loginUser);

															if (serviceReferenceNum != null
																	&& !serviceReferenceNum.isEmpty()
																	&& !serviceReferenceNum.trim()
																			.equalsIgnoreCase("")) {
																setServiceRefNo(serviceReferenceNum);
																view = true;
																setDisableAddBtn(true);
																nisiSeriyaDTO.setServiceRefNo(serviceReferenceNum);
																disableTab02 = false;
																disableBankInfo = false;
																activeTabIndex = 1;

																// add task
																// start
																commonService.insertTaskDetailsSubsidy(requestedNo,
																		nisiSeriyaDTO.getServiceAgreementNo(),
																		sessionBackingBean.getLoginUser(), "NS001", "O",
																		nisiSeriyaDTO.getServiceRefNo());
																// add task end

																RequestContext.getCurrentInstance()
																		.update("nisiSeriyaMainFrm:serviceRefNoId");
																RequestContext.getCurrentInstance()
																		.execute("PF('successMessage').show()");

															} else {
																errorMsg = "Data cannot be saved";
																RequestContext.getCurrentInstance()
																		.update("nisiSeriyaMainFrm");
																RequestContext.getCurrentInstance()
																		.execute("PF('requiredField').show()");
															}
															/**
															 * data insert end
															 */

														} else {
															errorMsg = "Province should be entered.";
															RequestContext.getCurrentInstance()
																	.update("nisiSeriyaMainFrm");
															RequestContext.getCurrentInstance()
																	.execute("PF('requiredField').show()");
														}

													} else {
														errorMsg = "City English should be entered.";
														RequestContext.getCurrentInstance().update("nisiSeriyaMainFrm");
														RequestContext.getCurrentInstance()
																.execute("PF('requiredField').show()");
													}

												} else {
													errorMsg = "Address Line 1 English should be entered.";
													RequestContext.getCurrentInstance().update("nisiSeriyaMainFrm");
													RequestContext.getCurrentInstance()
															.execute("PF('requiredField').show()");
												}

											} else {
												errorMsg = "Name of the Operator should be entered.";
												RequestContext.getCurrentInstance().update("nisiSeriyaMainFrm");
												RequestContext.getCurrentInstance()
														.execute("PF('requiredField').show()");
											}

										} else {
											errorMsg = "Nic/Org Reg.No. should be entered.";
											RequestContext.getCurrentInstance().update("nisiSeriyaMainFrm");
											RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
										}

									} else {
										errorMsg = "Trips Per Day should be entered.";
										RequestContext.getCurrentInstance().update("nisiSeriyaMainFrm");
										RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
									}

								} else {
									errorMsg = "Total Length KM should be entered.";
									RequestContext.getCurrentInstance().update("nisiSeriyaMainFrm");
									RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
								}

							} else {
								errorMsg = "Destination should be entered.";
								RequestContext.getCurrentInstance().update("nisiSeriyaMainFrm");
								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
							}

						} else {
							errorMsg = "Origin should be entered.";
							RequestContext.getCurrentInstance().update("nisiSeriyaMainFrm");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
						}

					} else {
						errorMsg = "Bus Number should be entered.";
						RequestContext.getCurrentInstance().update("nisiSeriyaMainFrm");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}

				} else {
					errorMsg = "Service Type should be entered.";
					RequestContext.getCurrentInstance().update("nisiSeriyaMainFrm");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}

			} else {
				errorMsg = "Preffered Language should be selected.";
				RequestContext.getCurrentInstance().update("nisiSeriyaMainFrm");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

		} else {
			errorMsg = "Please generate Request No.";
			RequestContext.getCurrentInstance().update("nisiSeriyaMainFrm");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void addButtonAction() {
		if (nisiSeriyaDTO.getStatusCode() == null || nisiSeriyaDTO.getStatusCode().isEmpty()
				|| nisiSeriyaDTO.getStatusCode().trim().equalsIgnoreCase("")) {
			nisiSeriyaDTO.setStatusCode("O");
			nisiSeriyaDTO.setStatusDes("ON GOING");
		}
		nisiSeriyaDTOList.add(nisiSeriyaDTO);

		nisiSeriyaDTO = new NisiSeriyaDTO();
		serviceRefNo = null;
		disableBankInfo = false;
		disableAddBtn = false;
		view = false;
		disableTab02 = true;
		setDisableDataTable(true);
		disableBankInfo = true;
		activeTabIndex = 0;

		RequestContext.getCurrentInstance().update("nisiSeriyaMainFrm");
	}

	public void deleteRecordConfirmAction(NisiSeriyaDTO nisiSeriyaDTO) {
		selectedNisiSeriyaDTO = new NisiSeriyaDTO();
		selectedNisiSeriyaDTO = nisiSeriyaDTO;

		RequestContext.getCurrentInstance().execute("PF('dlgConfirm').show()");
	}

	public void deleteRecordAction() {

		boolean deleted = nisiSeriyaService
				.deleteRecordFromNt_m_nri_requester_info(selectedNisiSeriyaDTO.getServiceRefNo());
		if (deleted) {
			nisiSeriyaDTOList.remove(selectedNisiSeriyaDTO);
			RequestContext.getCurrentInstance().update("nisiSeriyaMainFrm:permitHolderInfoTbl");
			sessionBackingBean.setMessage("Data successfully deleted");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonSuccess').show()");
		} else {
			errorMsg = "Data cannot be deleted";
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void onProvinceChange() {

		drpdDistrictList = adminService.getDistrictByProvinceToDropdown(nisiSeriyaDTO.getProvinceCode());

	}

	public void onDistrictChange() {
		drpdDevsecList = adminService.getDivSecByDistrictToDropdown(nisiSeriyaDTO.getDistrictCode());
	}

	public void btnClear() {

		nisiSeriyaDTO = new NisiSeriyaDTO();

		genReNumDisable = false;
		originList = new ArrayList<RouteDTO>();
		originList = nisiSeriyaService.retrieveOriginList();
		destinationList = new ArrayList<RouteDTO>();
		viaList = new ArrayList<RouteDTO>();
		serviceRefNo = null;
		nisiSeriyaDTOList = new ArrayList<NisiSeriyaDTO>();
		setDisableAddBtn(false);
		view = false;
		disableTab01 = true;
		disableTab02 = true;
		setDisableDataTable(false);
		renderTabView = false;
		requestedNo = null;
		RequestContext.getCurrentInstance().update("nisiSeriyaMainFrm");

	}

	public void mobNoValidation() {

		boolean mobnoVal = false;

		if (nisiSeriyaDTO.getMobileNo() != null && !nisiSeriyaDTO.getMobileNo().isEmpty()
				&& !nisiSeriyaDTO.getMobileNo().equalsIgnoreCase("")) {
			Pattern ptr = Pattern.compile("^[0-9]{10}$");
			boolean resultValidateMob = ptr.matcher(nisiSeriyaDTO.getMobileNo()).matches();
			if (resultValidateMob) {
				mobnoVal = true;
			} else {
				setErrorMsg("Invalid Mobile No.");
				RequestContext.getCurrentInstance().update("frmerrorMsge");
				RequestContext.getCurrentInstance().execute("PF('errorMsg').show()");
				nisiSeriyaDTO.setMobileNo(null);
				mobnoVal = false;
			}
		}
	}

	public void telNoValidation() {

		boolean telnoVal = false;

		if (nisiSeriyaDTO.getTelNo() != null && !nisiSeriyaDTO.getTelNo().isEmpty()
				&& !nisiSeriyaDTO.getTelNo().equalsIgnoreCase("")) {
			Pattern ptr = Pattern.compile("^[0-9]{10}$");
			boolean resultValidateMob = ptr.matcher(nisiSeriyaDTO.getTelNo()).matches();
			if (resultValidateMob) {
				telnoVal = true;
			} else {
				errorMsg = "Invalid Telephone No.";
				RequestContext.getCurrentInstance().update("frmerrorMsge");
				RequestContext.getCurrentInstance().execute("PF('errorMsg').show()");
				nisiSeriyaDTO.setTelNo(null);
				telnoVal = false;
			}
		}
	}

	// tab 01 end

	// tab 02 start
	public void onBankChange() {
		drpdBankBranchList = nisiSeriyaService.getBankBranchDropDown(nisiSeriyaDTO.getBankNameCode());
	}

	public void saveBtnActionForBankInfo() {

		boolean added = nisiSeriyaService.insertBankDetails(nisiSeriyaDTO, sessionBackingBean.loginUser);

		if (added) {

			disableBankInfo = true;
			RequestContext.getCurrentInstance().update("nisiSeriyaMainFrm");

			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
		} else {

			errorMsg = "Bank details cannot be addded";
			RequestContext.getCurrentInstance().update("nisiSeriyaMainFrm");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public void backToAuth() {

		String approveURL = sessionBackingBean.getApproveURL();
		String searchURL = sessionBackingBean.getSearchURL();

		if (approveURL != null) {

			try {
				sessionBackingBean.setApproveURLStatus(false);
				sessionBackingBean.setSisuSariya(false);
				sessionBackingBean.setSearchURLStatus(false);
				FacesContext.getCurrentInstance().getExternalContext().redirect(approveURL);
			} catch (IOException e) {

				e.printStackTrace();
			}

		} else if (searchURL != null) {

			try {

				sessionBackingBean.setApproveURLStatus(false);
				sessionBackingBean.setSisuSariya(false);
				sessionBackingBean.setSearchURLStatus(false);
				FacesContext.getCurrentInstance().getExternalContext().redirect(searchURL);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		RequestContext.getCurrentInstance().update("btnBackSisuSariyaAuth");
	}

	public void clearBankInformationAction() {
		nisiSeriyaDTO.setAccountNo(null);
		nisiSeriyaDTO.setBankNameCode(null);
		nisiSeriyaDTO.setBankBranchNameCode(null);

	}

	// tab 02 end
	/** nisi seriya methods end **/

	public void onServiceStartDateChange(SelectEvent event) throws ParseException {

	}

	public void onServiceEndDateChange(SelectEvent event) throws ParseException {

	}

	/**
	 * on change
	 */
	public void onRequestNoChange() {

		renderTabView = false;

	}

	public void btnSearch() {

		renderTabView = true;
		disableVia = false;
		disableTab02 = true;

	}

	/**
	 * tab 01
	 * -----------------------------------------------------------------------------------------------------------------------------------------
	 */

	public void btnTab01UploadRoadMap() {

	}

	public void btnTab01DocumentManagement() {

	}

	public void getServiceInformation(String serviceRefNo) {

		disableBankInfo = true;
		disableTab02 = false;

	}

	/**
	 * tab 02
	 * -----------------------------------------------------------------------------------------------------------------------------------------
	 */

	public void documentManagement() {
	}

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getActiveTabIndex() {
		return activeTabIndex;
	}

	public void setActiveTabIndex(int activeTabIndex) {
		this.activeTabIndex = activeTabIndex;
	}

	public List<CommonDTO> getDrpdDistrictList() {
		return drpdDistrictList;
	}

	public void setDrpdDistrictList(List<CommonDTO> drpdDistrictList) {
		this.drpdDistrictList = drpdDistrictList;
	}

	public List<CommonDTO> getDrpdDevsecList() {
		return drpdDevsecList;
	}

	public void setDrpdDevsecList(List<CommonDTO> drpdDevsecList) {
		this.drpdDevsecList = drpdDevsecList;
	}

	public EmployeeProfileService getEmployeeProfileService() {
		return employeeProfileService;
	}

	public void setEmployeeProfileService(EmployeeProfileService employeeProfileService) {
		this.employeeProfileService = employeeProfileService;
	}

	public List<GenerateReceiptDTO> getDrpdBankList() {
		return drpdBankList;
	}

	public void setDrpdBankList(List<GenerateReceiptDTO> drpdBankList) {
		this.drpdBankList = drpdBankList;
	}

	public List<GenerateReceiptDTO> getDrpdBankBranchList() {
		return drpdBankBranchList;
	}

	public void setDrpdBankBranchList(List<GenerateReceiptDTO> drpdBankBranchList) {
		this.drpdBankBranchList = drpdBankBranchList;
	}

	public SurveyService getSurveyService() {
		return surveyService;
	}

	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}

	public List<CommonDTO> getDrpdServiceTypeList() {
		return drpdServiceTypeList;
	}

	public void setDrpdServiceTypeList(List<CommonDTO> drpdServiceTypeList) {
		this.drpdServiceTypeList = drpdServiceTypeList;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Date getRequestStartDate() {
		return requestStartDate;
	}

	public void setRequestStartDate(Date requestStartDate) {
		this.requestStartDate = requestStartDate;
	}

	public Date getRequestEndtDate() {
		return requestEndtDate;
	}

	public void setRequestEndtDate(Date requestEndtDate) {
		this.requestEndtDate = requestEndtDate;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public SimpleDateFormat getFrm() {
		return frm;
	}

	public void setFrm(SimpleDateFormat frm) {
		this.frm = frm;
	}

	public String getEndDateValue() {
		return endDateValue;
	}

	public void setEndDateValue(String endDateValue) {
		this.endDateValue = endDateValue;
	}

	public String getStartDateValue() {
		return startDateValue;
	}

	public void setStartDateValue(String startDateValue) {
		this.startDateValue = startDateValue;
	}

	public boolean isView() {
		return view;
	}

	public void setView(boolean view) {
		this.view = view;
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

	public boolean isDisableTab01() {
		return disableTab01;
	}

	public void setDisableTab01(boolean disableTab01) {
		this.disableTab01 = disableTab01;
	}

	public boolean isDisableTab02() {
		return disableTab02;
	}

	public void setDisableTab02(boolean disableTab02) {
		this.disableTab02 = disableTab02;
	}

	public boolean isDisableBankInfo() {
		return disableBankInfo;
	}

	public void setDisableBankInfo(boolean disableBankInfo) {
		this.disableBankInfo = disableBankInfo;
	}

	public String getAlertMsg() {
		return alertMsg;
	}

	public void setAlertMsg(String alertMsg) {
		this.alertMsg = alertMsg;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public boolean isDisableVia() {
		return disableVia;
	}

	public void setDisableVia(boolean disableVia) {
		this.disableVia = disableVia;
	}

	public boolean isRenderTabView() {
		return renderTabView;
	}

	public void setRenderTabView(boolean renderTabView) {
		this.renderTabView = renderTabView;
	}

	public boolean isRenderBtnSearch() {
		return renderBtnSearch;
	}

	public void setRenderBtnSearch(boolean renderBtnSearch) {
		this.renderBtnSearch = renderBtnSearch;
	}

	public boolean isRenderEditMode() {
		return renderEditMode;
	}

	public void setRenderEditMode(boolean renderEditMode) {
		this.renderEditMode = renderEditMode;
	}

	public boolean isRenderViewMode() {
		return renderViewMode;
	}

	public void setRenderViewMode(boolean renderViewMode) {
		this.renderViewMode = renderViewMode;
	}

	public NisiSeriyaDTO getNisiSeriyaDTO() {
		return nisiSeriyaDTO;
	}

	public void setNisiSeriyaDTO(NisiSeriyaDTO nisiSeriyaDTO) {
		this.nisiSeriyaDTO = nisiSeriyaDTO;
	}

	public String getRequestedNo() {
		return requestedNo;
	}

	public void setRequestedNo(String requestedNo) {
		this.requestedNo = requestedNo;
	}

	public boolean isGenReNumDisable() {
		return genReNumDisable;
	}

	public void setGenReNumDisable(boolean genReNumDisable) {
		this.genReNumDisable = genReNumDisable;
	}

	public NisiSeriyaService getNisiSeriyaService() {
		return nisiSeriyaService;
	}

	public void setNisiSeriyaService(NisiSeriyaService nisiSeriyaService) {
		this.nisiSeriyaService = nisiSeriyaService;
	}

	public List<RouteDTO> getOriginList() {
		return originList;
	}

	public void setOriginList(List<RouteDTO> originList) {
		this.originList = originList;
	}

	public List<RouteDTO> getDestinationList() {
		return destinationList;
	}

	public void setDestinationList(List<RouteDTO> destinationList) {
		this.destinationList = destinationList;
	}

	public List<RouteDTO> getViaList() {
		return viaList;
	}

	public void setViaList(List<RouteDTO> viaList) {
		this.viaList = viaList;
	}

	public String getServiceRefNo() {
		return serviceRefNo;
	}

	public void setServiceRefNo(String serviceRefNo) {
		this.serviceRefNo = serviceRefNo;
	}

	public List<NisiSeriyaDTO> getNisiSeriyaDTOList() {
		return nisiSeriyaDTOList;
	}

	public void setNisiSeriyaDTOList(List<NisiSeriyaDTO> nisiSeriyaDTOList) {
		this.nisiSeriyaDTOList = nisiSeriyaDTOList;
	}

	public boolean isDisableAddBtn() {
		return disableAddBtn;
	}

	public void setDisableAddBtn(boolean disableAddBtn) {
		this.disableAddBtn = disableAddBtn;
	}

	public boolean isDisableDataTable() {
		return disableDataTable;
	}

	public void setDisableDataTable(boolean disableDataTable) {
		this.disableDataTable = disableDataTable;
	}

	public NisiSeriyaDTO getSelectedNisiSeriyaDTO() {
		return selectedNisiSeriyaDTO;
	}

	public void setSelectedNisiSeriyaDTO(NisiSeriyaDTO selectedNisiSeriyaDTO) {
		this.selectedNisiSeriyaDTO = selectedNisiSeriyaDTO;
	}

	public List<CommonDTO> getDrpdProvincelList() {
		return drpdProvincelList;
	}

	public void setDrpdProvincelList(List<CommonDTO> drpdProvincelList) {
		this.drpdProvincelList = drpdProvincelList;
	}

}
