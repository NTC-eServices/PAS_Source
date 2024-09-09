package lk.informatics.ntc.view.beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.DocumentManagementDTO;
import lk.informatics.ntc.model.dto.GenerateReceiptDTO;
import lk.informatics.ntc.model.dto.NisiSeriyaDTO;
import lk.informatics.ntc.model.dto.RouteDTO;
import lk.informatics.ntc.model.dto.SisuSeriyaDTO;
import lk.informatics.ntc.model.service.AdminService;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.DocumentManagementService;
import lk.informatics.ntc.model.service.NisiSeriyaService;
import lk.informatics.ntc.model.service.SurveyService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "nisiSeriyaPermitHolderInfoEditBean")
@ViewScoped
public class NisiSeriyaPermitHolderInfoEditBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	/** nisi seriya variables **/
	private NisiSeriyaService nisiSeriyaService;

	private boolean genReNumDisable;
	private boolean disableAddBtn;
	private boolean disableDataTable;
	// tab 01 start
	/** edit **/
	private List<String> requestNumList;
	private List<String> serviceRefNumList;
	private List<String> permitNumList;
	private List<String> serviceAgreementNumList;
	private String requestedNo;
	private String serviceRefNo;
	private String permitNo;
	private String serviceAgreementNo;
	private boolean searchEditDisable;
	private NisiSeriyaDTO nisiSeriyaDTO;
	private List<RouteDTO> originList;
	private List<RouteDTO> destinationList;
	private List<RouteDTO> viaList;
	private List<NisiSeriyaDTO> nisiSeriyaDTOList;
	private int activeTabIndex;
	private boolean view;
	private boolean disableTab01;
	private boolean disableTab02;
	private boolean disableBankInfo;
	private boolean renderClear;
	private String alertMsg;
	private List<CommonDTO> drpdServiceTypeList;
	private List<SisuSeriyaDTO> drpdOriginList;
	private List<SisuSeriyaDTO> drpdDestinationList;
	private List<SisuSeriyaDTO> drpdViaList;
	private List<CommonDTO> drpdProvincelList;
	private List<CommonDTO> drpdDistrictList;
	private List<CommonDTO> drpdDevsecList;
	private List<GenerateReceiptDTO> drpdBankList;
	private List<GenerateReceiptDTO> drpdBankBranchList;
	private List<DocumentManagementDTO> mandatoryList = new ArrayList<DocumentManagementDTO>(0);
	private List<DocumentManagementDTO> optionalList = new ArrayList<DocumentManagementDTO>(0);
	/** edit **/

	// tab 01 end
	/** nisi seriay variables **/

	// drop down lists

	private AdminService adminService;
	private SurveyService surveyService;
	private DocumentManagementService documentManagementService;;
	private CommonService commonService;
	private String errorMsg;
	String dateFormat = "dd/MM/yyyy";
	SimpleDateFormat frm = new SimpleDateFormat(dateFormat);
	private boolean renderBtnSearch;

	@PostConstruct
	public void init() {

		/** nisi seriya start **/
		nisiSeriyaService = (NisiSeriyaService) SpringApplicationContex.getBean("nisiSeriyaService");
		surveyService = (SurveyService) SpringApplicationContex.getBean("surveyService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		documentManagementService = (DocumentManagementService) SpringApplicationContex
				.getBean("documentManagementService");

		setRequestNumList(new ArrayList<String>());
		serviceRefNumList = new ArrayList<String>();
		permitNumList = new ArrayList<String>();
		serviceAgreementNumList = new ArrayList<String>();
		setRequestNumList(nisiSeriyaService.retrieveRequestNumbers());
		nisiSeriyaDTO = new NisiSeriyaDTO();
		requestedNo = null;
		serviceRefNo = null;
		permitNo = null;
		serviceAgreementNo = null;
		searchEditDisable = false;
		view = true;
		disableBankInfo = true;

		genReNumDisable = false;
		originList = new ArrayList<RouteDTO>();
		originList = nisiSeriyaService.retrieveOriginList();
		destinationList = new ArrayList<RouteDTO>();
		viaList = new ArrayList<RouteDTO>();
		nisiSeriyaDTOList = new ArrayList<NisiSeriyaDTO>();
		setDisableAddBtn(false);
		disableTab02 = true;
		setDisableDataTable(false);

		renderClear = true;

		adminService = (AdminService) SpringApplicationContex.getBean("adminService");

		drpdOriginList = new ArrayList<SisuSeriyaDTO>();
		drpdDestinationList = new ArrayList<SisuSeriyaDTO>();
		drpdBankList = new ArrayList<GenerateReceiptDTO>();
		drpdBankBranchList = new ArrayList<GenerateReceiptDTO>();
		drpdViaList = new ArrayList<SisuSeriyaDTO>();

		drpdServiceTypeList = adminService.getServiceTypeToDropdown();
		drpdProvincelList = adminService.getProvinceToDropdown();
		drpdDevsecList = adminService.getDivSecToDropdown();
		drpdDistrictList = adminService.getDistrictToDropdown();
		drpdBankList = surveyService.getBankListDropDown();

		if (sessionBackingBean.isSisuSariya() == true) {

			renderClear = false;
			sessionBackingBean.setSisuSariya(false);
			renderBtnSearch = true;
		}
	}

	/** nisi seriya edit methods start **/
	public void onRequestNumChange() {
		serviceRefNumList = new ArrayList<String>();
		serviceRefNumList = nisiSeriyaService.retrieveServiceRefNumbers(requestedNo, permitNo, serviceAgreementNo);

		permitNumList = new ArrayList<String>();
		permitNumList = nisiSeriyaService.retrievePermitNumbers(requestedNo, serviceRefNo, serviceAgreementNo);

		serviceAgreementNumList = new ArrayList<String>();
		serviceAgreementNumList = nisiSeriyaService.retrieveServiceAgreementNumbers(requestedNo, serviceRefNo,
				permitNo);

		searchEditDisable = true;

		RequestContext.getCurrentInstance().update("nisiSeriyaMainFrm");
	}

	public void onServiceRefNumChange() {

		permitNumList = new ArrayList<String>();
		permitNumList = nisiSeriyaService.retrievePermitNumbers(requestedNo, serviceRefNo, serviceAgreementNo);

		serviceAgreementNumList = new ArrayList<String>();
		serviceAgreementNumList = nisiSeriyaService.retrieveServiceAgreementNumbers(requestedNo, serviceRefNo,
				permitNo);

		RequestContext.getCurrentInstance().update("nisiSeriyaMainFrm");
	}

	public void onPermitNumChange() {
		serviceRefNumList = new ArrayList<String>();
		serviceRefNumList = nisiSeriyaService.retrieveServiceRefNumbers(requestedNo, permitNo, serviceAgreementNo);

		serviceAgreementNumList = new ArrayList<String>();
		serviceAgreementNumList = nisiSeriyaService.retrieveServiceAgreementNumbers(requestedNo, serviceRefNo,
				permitNo);

		RequestContext.getCurrentInstance().update("nisiSeriyaMainFrm");
	}

	public void onServiceAgreeNumChange() {
		serviceRefNumList = new ArrayList<String>();
		serviceRefNumList = nisiSeriyaService.retrieveServiceRefNumbers(requestedNo, permitNo, serviceAgreementNo);

		permitNumList = new ArrayList<String>();
		permitNumList = nisiSeriyaService.retrievePermitNumbers(requestedNo, serviceRefNo, serviceAgreementNo);

		RequestContext.getCurrentInstance().update("nisiSeriyaMainFrm");
	}

	public void btnSearch() {

		disableTab02 = false;
		disableDataTable = true;
		nisiSeriyaDTOList = nisiSeriyaService.searchDataFromNt_t_task_det(requestedNo, permitNo, serviceAgreementNo,
				serviceRefNo); //

		RequestContext.getCurrentInstance().update("btnBackSisuSariyaAuth");

	}

	public void viewEditDataListener(NisiSeriyaDTO dTO) {

		nisiSeriyaDTO = dTO;
		originList = nisiSeriyaService.retrieveOriginList();
		destinationList = nisiSeriyaService.retrieveDestinationList(nisiSeriyaDTO.getOriginCode());
		viaList = nisiSeriyaService.retrieveViaList(nisiSeriyaDTO.getOriginCode(), nisiSeriyaDTO.getDestinationCode());
		drpdBankBranchList = nisiSeriyaService.getBankBranchDropDown(nisiSeriyaDTO.getBankNameCode());
		view = true;
		disableBankInfo = true;
		RequestContext.getCurrentInstance().update("btnBackSisuSariyaAuth");
	}

	public void editDataListener(NisiSeriyaDTO dTO) {

		nisiSeriyaDTO = dTO;
		originList = nisiSeriyaService.retrieveOriginList();
		destinationList = nisiSeriyaService.retrieveDestinationList(nisiSeriyaDTO.getOriginCode());
		viaList = nisiSeriyaService.retrieveViaList(nisiSeriyaDTO.getOriginCode(), nisiSeriyaDTO.getDestinationCode());
		drpdBankBranchList = nisiSeriyaService.getBankBranchDropDown(nisiSeriyaDTO.getBankNameCode());
		view = false;
		disableBankInfo = false;
		RequestContext.getCurrentInstance().update("btnBackSisuSariyaAuth");
	}

	public void btnClear() {

		serviceRefNumList = new ArrayList<String>();
		permitNumList = new ArrayList<String>();
		serviceAgreementNumList = new ArrayList<String>();
		searchEditDisable = false;
		requestedNo = null;
		serviceRefNo = null;
		permitNo = null;
		serviceAgreementNo = null;
		view = true;
		disableBankInfo = true;
		nisiSeriyaDTO = new NisiSeriyaDTO();
		nisiSeriyaDTOList = new ArrayList<NisiSeriyaDTO>();
		originList = new ArrayList<RouteDTO>();
		destinationList = new ArrayList<RouteDTO>();
		viaList = new ArrayList<RouteDTO>();

		requestNumList = new ArrayList<String>();
		requestNumList = nisiSeriyaService.retrieveRequestNumbers();

		RequestContext.getCurrentInstance().update("nisiSeriyaMainFrm");

	}

	/** nisi seriya edit methods end **/

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

	public void onOriginChange() {

		if (nisiSeriyaDTO.getOriginCode() != null && !nisiSeriyaDTO.getOriginCode().isEmpty()
				&& !nisiSeriyaDTO.getOriginCode().equals("")) {

			destinationList = nisiSeriyaService.retrieveDestinationList(nisiSeriyaDTO.getOriginCode());

		}
	}

	public void onDestinationChange() {

		if (nisiSeriyaDTO.getDestinationCode() != null && !nisiSeriyaDTO.getDestinationCode().isEmpty()
				&& !nisiSeriyaDTO.getDestinationCode().equals("") && nisiSeriyaDTO.getOriginCode() != null
				&& !nisiSeriyaDTO.getOriginCode().isEmpty() && !nisiSeriyaDTO.getOriginCode().equals("")) {

			viaList = nisiSeriyaService.retrieveViaList(nisiSeriyaDTO.getOriginCode(),
					nisiSeriyaDTO.getDestinationCode());
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

															nisiSeriyaService.updateDataIntoNt_m_nri_requester_info(
																	nisiSeriyaDTO, sessionBackingBean.loginUser);

															activeTabIndex = 1;

															RequestContext.getCurrentInstance()
																	.update("nisiSeriyaMainFrm:serviceRefNoId");
															RequestContext.getCurrentInstance()
																	.execute("PF('successMessage').show()");

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
									errorMsg = "Distance should be entered.";
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

	public void onProvinceChange() {

		drpdDistrictList = adminService.getDistrictByProvinceToDropdown(nisiSeriyaDTO.getProvinceCode());

	}

	public void onDistrictChange() {
		drpdDevsecList = adminService.getDivSecByDistrictToDropdown(nisiSeriyaDTO.getDistrictCode());
	}

	// tab 01 end

	// tab 02 start
	public void onBankChange() {
		drpdBankBranchList = nisiSeriyaService.getBankBranchDropDown(nisiSeriyaDTO.getBankNameCode());
	}

	public void saveBtnActionForBankInfo() {

		boolean added = nisiSeriyaService.insertBankDetails(nisiSeriyaDTO, sessionBackingBean.loginUser);

		if (added) {

			RequestContext.getCurrentInstance().update("nisiSeriyaMainFrm");

			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
		} else {

			errorMsg = "Bank details cannot be addded";
			RequestContext.getCurrentInstance().update("nisiSeriyaMainFrm");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public void clearBankInformationAction() {
		nisiSeriyaDTO.setAccountNo(null);
		nisiSeriyaDTO.setBankNameCode(null);
		nisiSeriyaDTO.setBankBranchNameCode(null);

	}

	// tab 02 end
	/** nisi seriya methods end **/

	// getters and setters
	public List<CommonDTO> getDrpdProvincelList() {
		return drpdProvincelList;
	}

	public void setDrpdProvincelList(List<CommonDTO> drpdProvincelList) {
		this.drpdProvincelList = drpdProvincelList;
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

	public List<SisuSeriyaDTO> getDrpdOriginList() {
		return drpdOriginList;
	}

	public void setDrpdOriginList(List<SisuSeriyaDTO> drpdOriginList) {
		this.drpdOriginList = drpdOriginList;
	}

	public List<SisuSeriyaDTO> getDrpdDestinationList() {
		return drpdDestinationList;
	}

	public void setDrpdDestinationList(List<SisuSeriyaDTO> drpdDestinationList) {
		this.drpdDestinationList = drpdDestinationList;
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

	public boolean isRenderClear() {
		return renderClear;
	}

	public void setRenderClear(boolean renderClear) {
		this.renderClear = renderClear;
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

	public List<SisuSeriyaDTO> getDrpdViaList() {
		return drpdViaList;
	}

	public void setDrpdViaList(List<SisuSeriyaDTO> drpdViaList) {
		this.drpdViaList = drpdViaList;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public boolean isRenderBtnSearch() {
		return renderBtnSearch;
	}

	public void setRenderBtnSearch(boolean renderBtnSearch) {
		this.renderBtnSearch = renderBtnSearch;
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

	public List<String> getRequestNumList() {
		return requestNumList;
	}

	public void setRequestNumList(List<String> requestNumList) {
		this.requestNumList = requestNumList;
	}

	public List<String> getServiceRefNumList() {
		return serviceRefNumList;
	}

	public void setServiceRefNumList(List<String> serviceRefNumList) {
		this.serviceRefNumList = serviceRefNumList;
	}

	public List<String> getPermitNumList() {
		return permitNumList;
	}

	public void setPermitNumList(List<String> permitNumList) {
		this.permitNumList = permitNumList;
	}

	public List<String> getServiceAgreementNumList() {
		return serviceAgreementNumList;
	}

	public void setServiceAgreementNumList(List<String> serviceAgreementNumList) {
		this.serviceAgreementNumList = serviceAgreementNumList;
	}

	public String getPermitNo() {
		return permitNo;
	}

	public void setPermitNo(String permitNo) {
		this.permitNo = permitNo;
	}

	public String getServiceAgreementNo() {
		return serviceAgreementNo;
	}

	public void setServiceAgreementNo(String serviceAgreementNo) {
		this.serviceAgreementNo = serviceAgreementNo;
	}

	public boolean isSearchEditDisable() {
		return searchEditDisable;
	}

	public void setSearchEditDisable(boolean searchEditDisable) {
		this.searchEditDisable = searchEditDisable;
	}

}
