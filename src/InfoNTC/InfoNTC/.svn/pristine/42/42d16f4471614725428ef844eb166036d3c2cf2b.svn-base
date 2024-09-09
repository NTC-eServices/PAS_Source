package lk.informatics.ntc.view.beans;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import lk.informatics.ntc.model.dto.BusOwnerDTO;
import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.DocumentManagementDTO;
import lk.informatics.ntc.model.dto.GenerateReceiptDTO;
import lk.informatics.ntc.model.dto.PaymentVoucherDTO;
import lk.informatics.ntc.model.dto.SisuSeriyaDTO;
import lk.informatics.ntc.model.dto.SurveyDTO;
import lk.informatics.ntc.model.service.AdminService;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.DocumentManagementService;
import lk.informatics.ntc.model.service.EmployeeProfileService;
import lk.informatics.ntc.model.service.SisuSariyaService;
import lk.informatics.ntc.model.service.SurveyService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "ssPermitRenewalBean")
@ViewScoped
public class SisuSeriyaServiceAgreementRenewalsBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private SurveyDTO surveyDTO;
	private SisuSeriyaDTO sisuSeriyaDTO;

	private int activeTabIndex;
	private int activeTabIndexViewHistory;

	// tab01
	private BusOwnerDTO busOwnerDTO;
	private String serviceType;
	private boolean disableTab01Save;
	private boolean disableTab01Cancel;
	private boolean disableTab01UploadRoadMap;
	private boolean disableTab01DocumentManagement;
	private boolean view;
	private boolean edit;
	private boolean renderEdit;
	private boolean disableTab03;
	private boolean disableTab02;

	private boolean disablePermitNo;
	private boolean sltb;
	private boolean cancellation;

	private String alertMsg = "This application is not elegible for Renewal Process.";

	// drop down lists
	private List<CommonDTO> drpdServiceTypeList;
	private List<SisuSeriyaDTO> drpdOriginList;
	private List<SisuSeriyaDTO> drpdDestinationList;
	private List<SisuSeriyaDTO> drpdServiceReferanceNoList;
	private List<SisuSeriyaDTO> drpdServiceNoList;
	private List<SisuSeriyaDTO> drpdOperatorDepoNameList;

	private List<CommonDTO> drpdProvincelList;
	private List<CommonDTO> drpdDistrictList;
	private List<CommonDTO> drpdDevsecList;
	private List<CommonDTO> drpdStatuslist;
	private List<GenerateReceiptDTO> drpdBankList;
	private List<GenerateReceiptDTO> drpdBankBranchList;
	private List<DocumentManagementDTO> mandatoryList = new ArrayList<DocumentManagementDTO>(0);
	private List<DocumentManagementDTO> optionalList = new ArrayList<DocumentManagementDTO>(0);
	private List<SisuSeriyaDTO> tblPermitHolderInfo;
	private SisuSariyaService sisuSariyaService;
	private AdminService adminService;
	private EmployeeProfileService employeeProfileService;
	private SurveyService surveyService;
	private DocumentManagementService documentManagementService;
	private CommonService commonService;
	private String errorMsg;
	private Date requestStartDate;
	private Date requestEndtDate;
	private boolean select, createMode, viewMode, renderButton;

	String dateFormat = "dd/MM/yyyy";
	SimpleDateFormat frm = new SimpleDateFormat(dateFormat);

	String endDateValue;
	String startDateValue;
	Date temptServiceEndDate;
	Date oldtServiceEndDate;

	private List<SisuSeriyaDTO> serviceHistoryList;

	// for view history panel
	private SisuSeriyaDTO viewSelect = new SisuSeriyaDTO();
	private boolean showDetailsForm = false;
	private String selectedLangauge;
	private String selectedServiceNoInGrid;
	private String selectedRefNoInGrid;
	private String selectedServiceNoForDoc;
	private String selectedRefNoForDoc;
	private SisuSeriyaDTO searchedServiceInfoDTO = new SisuSeriyaDTO();
	private SisuSeriyaDTO historyAgreementsDTO = new SisuSeriyaDTO();
	private SisuSeriyaDTO historyBankInfoDTO = new SisuSeriyaDTO();
	private SisuSeriyaDTO searchedSchoolInfoDTO = new SisuSeriyaDTO();
	private SisuSeriyaDTO historySchoolInfoDTO = new SisuSeriyaDTO();
	private List<SisuSeriyaDTO> languageList = new ArrayList<SisuSeriyaDTO>();
	private List<SisuSeriyaDTO> bankNameList = new ArrayList<SisuSeriyaDTO>();
	private List<SisuSeriyaDTO> bankBranchNameList = new ArrayList<SisuSeriyaDTO>();
	private List<SisuSeriyaDTO> selectedHistoryRecord = new ArrayList<SisuSeriyaDTO>();

	@PostConstruct
	public void init() {

		endDateValue = "";
		startDateValue = "";
		sisuSariyaService = (SisuSariyaService) SpringApplicationContex.getBean("sisuSariyaService");
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");
		employeeProfileService = (EmployeeProfileService) SpringApplicationContex.getBean("employeeProfileService");
		surveyService = (SurveyService) SpringApplicationContex.getBean("surveyService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");

		documentManagementService = (DocumentManagementService) SpringApplicationContex
				.getBean("documentManagementService");

		sisuSeriyaDTO = new SisuSeriyaDTO();

		drpdServiceReferanceNoList = new ArrayList<SisuSeriyaDTO>();
		drpdOperatorDepoNameList = new ArrayList<SisuSeriyaDTO>();

		drpdOriginList = new ArrayList<SisuSeriyaDTO>();
		drpdDestinationList = new ArrayList<SisuSeriyaDTO>();
		drpdBankList = new ArrayList<GenerateReceiptDTO>();
		drpdBankBranchList = new ArrayList<GenerateReceiptDTO>();
		tblPermitHolderInfo = new ArrayList<SisuSeriyaDTO>();
		drpdServiceNoList = new ArrayList<SisuSeriyaDTO>();

		loadValues();
		edit = false;
		temptServiceEndDate = null;

		createMode = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN107", "C");
		viewMode = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN107", "V");

		if (viewMode == true && createMode == false) {
			view = true;
		} else {
			renderButton = true;
			disableTab02 = true;
			disableTab03 = true;
		}

		searchedServiceInfoDTO = new SisuSeriyaDTO();
		historyAgreementsDTO = new SisuSeriyaDTO();
		historyBankInfoDTO = new SisuSeriyaDTO();
		searchedSchoolInfoDTO = new SisuSeriyaDTO();
		historySchoolInfoDTO = new SisuSeriyaDTO();

	}

	private void loadValues() {
		drpdServiceTypeList = adminService.getServiceTypeToDropdown();
		drpdOperatorDepoNameList = sisuSariyaService.getOperatorDepoNameDropDownForRenewal();
		drpdProvincelList = adminService.getProvinceToDropdown();
		drpdDevsecList = adminService.getDivSecToDropdown();
		drpdDistrictList = adminService.getDistrictToDropdown();
		drpdStatuslist = employeeProfileService.GetMartialToDropdown();
		drpdBankList = surveyService.getBankListDropDown();
		drpdServiceReferanceNoList = sisuSariyaService.getRenewalRefNoList();
		drpdServiceNoList = sisuSariyaService.getRenewalServiceNoList();
		languageList = sisuSariyaService.getPrefLanguForDropDown();
		bankNameList = sisuSariyaService.getBankList();

		loadHistoryData();
	}

	public void loadHistoryData() {
		serviceHistoryList = new ArrayList<SisuSeriyaDTO>();
		serviceHistoryList = sisuSariyaService.getServiceHistoryDeatils(sisuSeriyaDTO);
	}

	public void onOperatorDepoNameChange() {
		if (sisuSeriyaDTO.getNameOfOperator() != null && !sisuSeriyaDTO.getNameOfOperator().isEmpty()
				&& !sisuSeriyaDTO.getNameOfOperator().equalsIgnoreCase("")) {
			drpdServiceNoList = sisuSariyaService.getRenewalServiceNoList(sisuSeriyaDTO);
			drpdServiceReferanceNoList  = sisuSariyaService.getRenewalServiceReferenceNoList(sisuSeriyaDTO);
		}
	}

	public void onProvinceChange() {
		drpdDistrictList = adminService.getDistrictByProvinceToDropdown(sisuSeriyaDTO.getProvinceCode());
	}

	public void onDistrictChange() {
		drpdDevsecList = adminService.getDivSecByDistrictToDropdown(sisuSeriyaDTO.getDistrictCode());
	}

	public void onRequestNoChange() {
		String tempt = sisuSeriyaDTO.getRequestNo();
		sisuSeriyaDTO = new SisuSeriyaDTO();
		sisuSeriyaDTO.setRequestNo(tempt);

		if (sisuSeriyaDTO.getRequestNo() != null && !sisuSeriyaDTO.getRequestNo().isEmpty()
				&& !sisuSeriyaDTO.getRequestNo().equalsIgnoreCase("")) {

			drpdOriginList = sisuSariyaService.drpdOriginList(sisuSeriyaDTO.getRequestNo());
			drpdDestinationList = sisuSariyaService.drpdDestinationList(sisuSeriyaDTO.getRequestNo());
			tblPermitHolderInfo = sisuSariyaService.getTblPermitHolderInfo(sisuSeriyaDTO);
			sisuSeriyaDTO = sisuSariyaService.retrieveSchoolInfoFromRequest(sisuSeriyaDTO);

			sisuSeriyaDTO.setRequestNo(tempt);
			edit = false;
			renderEdit = false;

			if (viewMode == true && createMode == false) {
				view = true;
			} else {
				view = false;
			}

		}

	}

	public void onServiceReferanceNoChange() {
		String tempt = sisuSeriyaDTO.getServiceRefNo();
		sisuSeriyaDTO = new SisuSeriyaDTO();
		sisuSeriyaDTO.setServiceRefNo(tempt);

		if (sisuSeriyaDTO.getServiceRefNo() != null && !sisuSeriyaDTO.getServiceRefNo().isEmpty()
				&& !sisuSeriyaDTO.getServiceRefNo().equalsIgnoreCase("")) {

			this.sisuSeriyaDTO = sisuSariyaService.getServiceInformationByServiceRefNo(sisuSeriyaDTO.getServiceRefNo());
			if (sisuSeriyaDTO.getSltbStatus() != null && sisuSeriyaDTO.getSltbStatus().equals("Y")) {
				disablePermitNo = true;
				sltb = true;
			} else {
				disablePermitNo = false;
				sltb = false;
			}
			if (sisuSeriyaDTO.getCancellationStatus() != null && sisuSeriyaDTO.getCancellationStatus().equals("Y")) {
				cancellation = true;
			} else {
				cancellation = false;
			}

			setOldtServiceEndDate(sisuSeriyaDTO.getServiceEndDateObj());
			if (sisuSeriyaDTO.getServiceRenewalStatus() != null
					&& sisuSeriyaDTO.getServiceRenewalStatus().equals("P")) {
				this.setTemptServiceEndDate(sisuSeriyaDTO.getServiceExpireDate());
				edit = true;
			} else {
				edit = false;
				this.setTemptServiceEndDate(sisuSeriyaDTO.getServiceEndDateObj());
			}

			this.sisuSeriyaDTO = sisuSariyaService.getSchoolInformationByServiceRefNo(sisuSeriyaDTO);

		}

		if (viewMode == true && createMode == false) {
			view = true;
		} else {
			view = false;
		}

		loadHistoryData();

	}

	public void onServiceNoChange() {
		/***as per the request of BA Team first search by service No/ Agreement No. then appear ref No***/
		drpdServiceReferanceNoList  = sisuSariyaService.getRenewalServiceReferenceNoList(sisuSeriyaDTO);
	}

	public void btnClear() {
		sisuSeriyaDTO = new SisuSeriyaDTO();
		tblPermitHolderInfo = new ArrayList<SisuSeriyaDTO>();
		
	}

	public boolean validateDate(Date applicationDte) {

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		// convert date to calendar
		Calendar c = Calendar.getInstance();
		c.setTime(applicationDte);

		c.add(Calendar.DATE, -30); // same with c.add(Calendar.DAY_OF_MONTH, 1);

		Date dateBeforeRenwal = c.getTime();

		Date date = new Date();

		if (date.compareTo(dateBeforeRenwal) > 0) {
			// ("Date1 is after Date2 = > This application is eligible for
			// renewal process");
			return true;
		} else {
			// ("Date1 is after Date2 = > This application is NOT eligible
			// for renewal process");
			return false;
		}

	}

	public void onBtnClickHistory() {
		if (sisuSeriyaDTO != null && sisuSeriyaDTO.getServiceNo() != null
				&& !sisuSeriyaDTO.getServiceNo().trim().equals("")) {
			viewHistoryBack();
			RequestContext.getCurrentInstance().execute("PF('renewalHistoryDialog').show()");
		} else {
			errorMsg = "Service No. should be selected.";
			RequestContext.getCurrentInstance().update("frm-serviceInformation");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	// tab 01

	public void btnTab01Save() {

		if (sltb) {
			sisuSeriyaDTO.setSltbStatus("Y");
		} else {

			sisuSeriyaDTO.setSltbStatus("N");
		}

		if (cancellation) {
			sisuSeriyaDTO.setCancellationStatus("Y");
		} else {
			sisuSeriyaDTO.setCancellationStatus(null);
		}

		String dateFormat = "dd/MM/yyyy";
		SimpleDateFormat frm = new SimpleDateFormat(dateFormat);
		if (sisuSeriyaDTO.getServiceStartDateObj() != null) {
			sisuSeriyaDTO.setServiceStartDateVal(frm.format(sisuSeriyaDTO.getServiceStartDateObj()));
		}

		if (sisuSeriyaDTO.getServiceEndDateObj() != null) {
			sisuSeriyaDTO.setServiceEndDateVal(frm.format(sisuSeriyaDTO.getServiceEndDateObj()));
		}
		
		

		if (sisuSeriyaDTO.getRequestNo() != null && !sisuSeriyaDTO.getRequestNo().isEmpty()
				&& !sisuSeriyaDTO.getRequestNo().equalsIgnoreCase("")) {

			if (sisuSeriyaDTO.getServiceEndDateObj() != null) {
				sisuSeriyaDTO.setServiceExpireDate(getTemptServiceEndDate());
				sisuSeriyaDTO.setServiceRenewalStatus("P");
			}

			if (sisuSeriyaDTO.getLanguageCode() != null && !sisuSeriyaDTO.getLanguageCode().isEmpty()
					&& !sisuSeriyaDTO.getLanguageCode().equalsIgnoreCase("")) {

				if (sisuSeriyaDTO.getServiceTypeCode() != null && !sisuSeriyaDTO.getServiceTypeCode().isEmpty()
						&& !sisuSeriyaDTO.getServiceTypeCode().equalsIgnoreCase("")) {

					if (sisuSeriyaDTO.getBusRegNo() != null && !sisuSeriyaDTO.getBusRegNo().isEmpty()
							&& !sisuSeriyaDTO.getBusRegNo().equalsIgnoreCase("")) {

						if (sisuSeriyaDTO.getOriginCode() != null && !sisuSeriyaDTO.getOriginCode().isEmpty()
								&& !sisuSeriyaDTO.getOriginCode().equalsIgnoreCase("")) {

							if (sisuSeriyaDTO.getDestinationCode() != null
									&& !sisuSeriyaDTO.getDestinationCode().isEmpty()
									&& !sisuSeriyaDTO.getDestinationCode().equalsIgnoreCase("")) {

								if (sisuSeriyaDTO.getNicNo() != null && !sisuSeriyaDTO.getNicNo().isEmpty()
										&& !sisuSeriyaDTO.getNicNo().equalsIgnoreCase("")) {

									if (sisuSeriyaDTO.getNameOfOperator() != null
											&& !sisuSeriyaDTO.getNameOfOperator().isEmpty()
											&& !sisuSeriyaDTO.getNameOfOperator().equalsIgnoreCase("")) {

										if (sisuSeriyaDTO.getAddressOne() != null
												&& !sisuSeriyaDTO.getAddressOne().isEmpty()
												&& !sisuSeriyaDTO.getAddressOne().equalsIgnoreCase("")) {

											if (sisuSeriyaDTO.getCity() != null && !sisuSeriyaDTO.getCity().isEmpty()
													&& !sisuSeriyaDTO.getCity().equalsIgnoreCase("")) {

												if (sisuSeriyaDTO.getProvinceCode() != null
														&& !sisuSeriyaDTO.getProvinceCode().isEmpty()
														&& !sisuSeriyaDTO.getProvinceCode().equalsIgnoreCase("")) {

													/**
													 * update
													 */
													if (edit) {

														sisuSeriyaDTO.setServiceExpireDate(temptServiceEndDate);
														if (sisuSeriyaDTO.getServiceExpireDate() != null) {
															sisuSeriyaDTO.setServiceExpireDateVal(frm.format(sisuSeriyaDTO.getServiceExpireDate()));
														}
														String bankBranchCode =sisuSariyaService.getBranchCodeByRequestNo(sisuSeriyaDTO.getServiceRefNo());
														sisuSeriyaDTO.setBankBranchNameCode(bankBranchCode);
														drpdBankBranchList = surveyService.getBankBranchDropDown(sisuSeriyaDTO.getBankNameCode());
														sisuSariyaService.insertRenewalServiceToHistory(sisuSeriyaDTO,
																sessionBackingBean.loginUser);

														if (sisuSariyaService.updateRenewalServiceInformation(
																sisuSeriyaDTO, sessionBackingBean.getLoginUser())) {

															RequestContext.getCurrentInstance()
																	.execute("PF('successMessage').show()");
															if (sisuSeriyaDTO.getCancellationStatus() != null
																	&& sisuSeriyaDTO.getCancellationStatus()
																			.equals("Y")) {
																disableTab02 = true;
																disableTab03 = true;
															} else {
																disableTab02 = false;
																disableTab03 = false;
															}

														} else {
															errorMsg = "Could not update data successfully.";
															RequestContext.getCurrentInstance()
																	.update("frm-serviceInformation");
															RequestContext.getCurrentInstance()
																	.execute("PF('requiredField').show()");
														}

														/**
														 * save
														 */
													} else {
														
														/***As per the request of BA avoid another renewal when current renewal is ongoing*****/
														
														/**new request from BA, do multiple renewal for each request
														 * commented by: gayathra.m 2024-01-31
														 *
														 * boolean haveGoingRenewal =sisuSariyaService.getRenewalStatusByRequestNo(sisuSeriyaDTO.getRequestNo());
														 * if(!haveGoingRenewal) {
														 *	
														 */
														
														if(true) {

														sisuSeriyaDTO.setServiceExpireDate(temptServiceEndDate);
														if (sisuSeriyaDTO.getServiceExpireDate() != null) {

															sisuSeriyaDTO.setServiceRenewalStatus("P");
															sisuSeriyaDTO.setServiceEndDateObj(oldtServiceEndDate);
															sisuSeriyaDTO.setStatus(null);
															String bankBranchCode =sisuSariyaService.getBranchCodeByRequestNo(sisuSeriyaDTO.getServiceRefNo());
															sisuSeriyaDTO.setBankBranchNameCode(bankBranchCode);
															drpdBankBranchList = surveyService.getBankBranchDropDown(sisuSeriyaDTO.getBankNameCode());
															sisuSeriyaDTO = sisuSariyaService
																	.insertRenewalServiceInformation(sisuSeriyaDTO,
																			sessionBackingBean.loginUser);
															boolean isSchoolInfoSave = false;
															if (sisuSeriyaDTO.getCancellationStatus() == null) {
																isSchoolInfoSave = sisuSariyaService
																		.insertSchoolInformation(sisuSeriyaDTO,
																				sessionBackingBean.loginUser);

															}
															if ((sisuSeriyaDTO.getCancellationStatus() == null
																	&& sisuSeriyaDTO.isDataSave() == true
																	&& isSchoolInfoSave)
																	|| (sisuSeriyaDTO.getCancellationStatus() != null
																			&& sisuSeriyaDTO.isDataSave() == true)) {

																renderEdit = true;
																edit = true;
																drpdServiceReferanceNoList = sisuSariyaService
																		.getRenewalRefNoList();
																drpdServiceNoList = sisuSariyaService
																		.getRenewalServiceNoList();

																if (sisuSeriyaDTO.getCancellationStatus() == null
																		&& sisuSeriyaDTO.isDataSave() == true
																		&& isSchoolInfoSave) {
																	// add task Sisu Sariya Renewals / Amendment / Cancellation
																	// no need to add to task history because this is a new record
																	commonService.insertTaskDetailsSubsidy(
																			sisuSeriyaDTO.getRequestNo(),
																			sisuSeriyaDTO.getServiceNo(),
																			sessionBackingBean.getLoginUser(), "SS007", "C",
																			sisuSeriyaDTO.getServiceRefNo());
																	
																}

																RequestContext.getCurrentInstance()
																		.execute("PF('successMessage').show()");

																if (sisuSeriyaDTO.getCancellationStatus() != null
																		&& sisuSeriyaDTO.getCancellationStatus()
																				.equals("Y")) {
																	renderEdit = false;
																	edit = false;
																	view = true;
																}

																disableTab02 = false;
																disableTab03 = false;
															
															
															} else {
																errorMsg = "Could not add data successfully.";
																RequestContext.getCurrentInstance()
																		.update("frm-serviceInformation");
																RequestContext.getCurrentInstance()
																		.execute("PF('requiredField').show()");
															}

														} else {
															errorMsg = "Service Expire Date should be entered.";
															RequestContext.getCurrentInstance()
																	.update("frm-serviceInformation");
															RequestContext.getCurrentInstance()
																	.execute("PF('requiredField').show()");
														}
													}
													else {
														errorMsg = "Another Renewal Process is Ongoing for "+sisuSeriyaDTO.getRequestNo();
														RequestContext.getCurrentInstance()
																.update("frm-serviceInformation");
														RequestContext.getCurrentInstance()
																.execute("PF('requiredField').show()");
													}
													}

												} else {
													errorMsg = "Province should be entered.";
													RequestContext.getCurrentInstance()
															.update("frm-serviceInformation");
													RequestContext.getCurrentInstance()
															.execute("PF('requiredField').show()");
												}
											} else {
												errorMsg = "City English should be entered.";
												RequestContext.getCurrentInstance().update("frm-serviceInformation");
												RequestContext.getCurrentInstance()
														.execute("PF('requiredField').show()");
											}
										} else {
											errorMsg = "Address Line 1 English should be entered.";
											RequestContext.getCurrentInstance().update("frm-serviceInformation");
											RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
										}

									} else {
										errorMsg = "Name of the Operator should be entered.";
										RequestContext.getCurrentInstance().update("frm-serviceInformation");
										RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
									}

								} else {
									errorMsg = "Nic/Org Reg.No. should be entered.";
									RequestContext.getCurrentInstance().update("frm-serviceInformation");
									RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
								}

							} else {
								errorMsg = "Destination should be entered.";
								RequestContext.getCurrentInstance().update("frm-serviceInformation");
								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
							}

						} else {
							errorMsg = "Origin should be entered.";
							RequestContext.getCurrentInstance().update("frm-serviceInformation");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
						}

					} else {
						errorMsg = "Bus Number should be entered.";
						RequestContext.getCurrentInstance().update("frm-serviceInformation");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}

				} else {
					errorMsg = "Service Type should be entered.";
					RequestContext.getCurrentInstance().update("frm-serviceInformation");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}

			} else {
				errorMsg = "Preffered Language should be entered.";
				RequestContext.getCurrentInstance().update("frm-serviceInformation");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

		} else {
			errorMsg = "Request No. should be entered.";
			RequestContext.getCurrentInstance().update("frm-serviceInformation");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void btnTab01Clear() {

		String trmptReqNo = sisuSeriyaDTO.getRequestNo();
		sisuSeriyaDTO = new SisuSeriyaDTO();

		sisuSeriyaDTO = sisuSariyaService.retrieveSchoolInfoFromRequest(sisuSeriyaDTO);
		edit = false;

		if (viewMode == true && createMode == false) {
			view = true;
		} else {
			view = false;
		}
		temptServiceEndDate = null;
		serviceHistoryList = new ArrayList<SisuSeriyaDTO>();
		renderEdit = false;

		disableTab02 = true;
		disableTab03 = true;
		
		cancellation = false;
		loadValues();

	}

	public void btnTab01UploadRoadMap() {

	}

	public void btnTab01DocumentManagement() {

	}

	public void getServiceInformation(String serviceRefNo) {

		this.sisuSeriyaDTO = sisuSariyaService.getServiceInformationByServiceRefNo(serviceRefNo);
		this.sisuSeriyaDTO = sisuSariyaService.getSchoolInformationByServiceRefNo(this.sisuSeriyaDTO);
		this.startDateValue = sisuSeriyaDTO.getRequestStartDateString();
		this.endDateValue = sisuSeriyaDTO.getRequestEndDateString();

		view = true;
		edit = false;

	}

	public void editServiceInformation(String serviceRefNo) {

		this.sisuSeriyaDTO = sisuSariyaService.getServiceInformationByServiceRefNo(serviceRefNo);
		this.sisuSeriyaDTO = sisuSariyaService.retrieveSchoolInfoFromRequest(sisuSeriyaDTO);

		if (!sisuSeriyaDTO.getStatusCode().equalsIgnoreCase("o")) {
			renderEdit = true;
			this.sisuSeriyaDTO = sisuSariyaService.getSchoolInformationByServiceRefNo(sisuSeriyaDTO);
		}

		this.startDateValue = sisuSeriyaDTO.getRequestStartDateString();
		this.endDateValue = sisuSeriyaDTO.getRequestEndDateString();

		if (viewMode == true && createMode == false) {
			view = true;
		} else {
			view = false;
		}

		edit = true;
	}

	// tab 02

	public void btnTab02Save() {

		if (sisuSeriyaDTO.getServiceRefNo() != null && !sisuSeriyaDTO.getServiceRefNo().isEmpty()
				&& !sisuSeriyaDTO.getServiceRefNo().equalsIgnoreCase("")) {
			if (sisuSeriyaDTO.getSchoolName() != null && !sisuSeriyaDTO.getSchoolName().isEmpty()
					&& !sisuSeriyaDTO.getSchoolName().equalsIgnoreCase("")) {

				if (sisuSeriyaDTO.getAddressOne() != null && !sisuSeriyaDTO.getAddressOne().isEmpty()
						&& !sisuSeriyaDTO.getAddressOne().equalsIgnoreCase("")) {

					if (sisuSeriyaDTO.getCity() != null && !sisuSeriyaDTO.getCity().isEmpty()
							&& !sisuSeriyaDTO.getCity().equalsIgnoreCase("")) {

						if (sisuSeriyaDTO.getProvinceCode() != null && !sisuSeriyaDTO.getSchoolProvinceCode().isEmpty()
								&& !sisuSeriyaDTO.getProvinceCode().equalsIgnoreCase("")) {

							if (sisuSariyaService.checkSchoolInfo(sisuSeriyaDTO)) {
								boolean isHistoryUpdated = true;

								boolean isSchoolInfoUpdated = sisuSariyaService.updateSchoolInformationByServiceRefNo(
										sisuSeriyaDTO, sessionBackingBean.loginUser);

								if (isSchoolInfoUpdated && isHistoryUpdated) {
									RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

								} else {
									errorMsg = "Could not update data successfully.";
									RequestContext.getCurrentInstance().update("frm-serviceInformation");
									RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
								}

							} else {

								/* Old Reference No. user for update history */
								boolean isHistoryUpdated = sisuSariyaService.saveSchoolInformationToHistory(
										sisuSeriyaDTO, sisuSeriyaDTO.getOldServiceRefNo(),
										sessionBackingBean.loginUser);

								boolean isSchoolInfoUpdated = sisuSariyaService.updateSchoolInformationByServiceRefNo(
										sisuSeriyaDTO, sessionBackingBean.loginUser);

								if (isSchoolInfoUpdated && isHistoryUpdated) {
									renderEdit = true;
									RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

								} else {
									errorMsg = "Could not add data successfully.";
									RequestContext.getCurrentInstance().update("frm-serviceInformation");
									RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
								}
							}

						} else {
							errorMsg = "Province should be entered.";
							RequestContext.getCurrentInstance().update("frm-serviceInformation");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
						}

					} else {
						errorMsg = "City should be entered.";
						RequestContext.getCurrentInstance().update("frm-serviceInformation");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}

				} else {
					errorMsg = "Address Line 1 name should be entered.";
					RequestContext.getCurrentInstance().update("frm-serviceInformation");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}

			} else {
				errorMsg = "School name should be entered.";
				RequestContext.getCurrentInstance().update("frm-serviceInformation");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

		} else {
			errorMsg = "Service Reference No. should be entered.";
			RequestContext.getCurrentInstance().update("frm-serviceInformation");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void btnTab02Clear() {
		if (sisuSeriyaDTO.getServiceRefNo() != null && !sisuSeriyaDTO.getServiceRefNo().isEmpty()
				&& !sisuSeriyaDTO.getServiceRefNo().equalsIgnoreCase("")) {
			sisuSeriyaDTO = sisuSariyaService.getSchoolInformationByServiceRefNo(sisuSeriyaDTO);
		} else {
			errorMsg = "Service Referenece No. should be entered.";
			RequestContext.getCurrentInstance().update("frm-serviceInformation");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void documentManagement() {

		try {

			sessionBackingBean.setServiceNoForSisuSariya(sisuSeriyaDTO.getServiceNo());
			sessionBackingBean.setServiceRefNo(sisuSeriyaDTO.getServiceRefNo());
			sessionBackingBean.setRequestNoForSisuSariya(sisuSeriyaDTO.getRequestNo());

			sessionBackingBean.setTransactionType("SISU SARIYA");

			mandatoryList = documentManagementService.mandatoryDocsForSisuSariyaAgreementRenewals("18",
					sisuSeriyaDTO.getServiceNo());
			optionalList = documentManagementService.optionalDocsForSisuSariyaAgreementRenewals("18",
					sisuSeriyaDTO.getServiceNo());

			sessionBackingBean.sisuSariyaMandatoryDocumentList = documentManagementService
					.sisuSariyaMandatoryList(sisuSeriyaDTO.getRequestNo());
			sessionBackingBean.sisuSariyaOptionalDocumentList = documentManagementService
					.sisuSariyaOptionalList(sisuSeriyaDTO.getRequestNo());

			if (sisuSeriyaDTO.getServiceRefNo() != null && sisuSeriyaDTO.getRequestNo() != null) {

				sessionBackingBean.sisuSariyaServiceRefMandatoryDocumentList = documentManagementService
						.sisuSariyaPermitHolderMandatoryList(sisuSeriyaDTO.getRequestNo(),
								sisuSeriyaDTO.getServiceRefNo());
				sessionBackingBean.sisuSariyaServiceRefOptionalDocumentList = documentManagementService
						.sisuSariyaPermitHolderOptionalList(sisuSeriyaDTO.getRequestNo(),
								sisuSeriyaDTO.getServiceRefNo());
			}

			if (sisuSeriyaDTO.getServiceNo() != null && sisuSeriyaDTO.getServiceRefNo() != null
					&& sisuSeriyaDTO.getRequestNo() != null) {

				sessionBackingBean.sisuSariyaServiceMandatoryDocumentList = documentManagementService
						.sisuSariyaAgreementRenewalsMandatoryList(sisuSeriyaDTO.getRequestNo(),
								sisuSeriyaDTO.getServiceRefNo(), sisuSeriyaDTO.getServiceNo());
				sessionBackingBean.sisuSariyaServiceOptionalDocumentList = documentManagementService
						.sisuSariyaAgreementRenewalsOptionalList(sisuSeriyaDTO.getRequestNo(),
								sisuSeriyaDTO.getServiceRefNo(), sisuSeriyaDTO.getServiceNo());

			}

			RequestContext.getCurrentInstance().execute("PF('uploadDocument').show()");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// tab 03

	public void onBankChange() {
		drpdBankBranchList = surveyService.getBankBranchDropDown(sisuSeriyaDTO.getBankNameCode());
	}

	public void btnTab03Save() {

		boolean added = sisuSariyaService.insertBankDetails(sisuSeriyaDTO, sessionBackingBean.loginUser);

		if (added) {
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

		} else {
			errorMsg = "Could not add bank details.";
			RequestContext.getCurrentInstance().update("frm-serviceInformation");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public void btnTab03Clear() {
		sisuSeriyaDTO.setAccountNo(null);
		sisuSeriyaDTO.setBankNameCode(null);
		sisuSeriyaDTO.setBankBranchNameCode(null);
	}

	public void viewDetailedHistory() {
		// clear all dtos
		searchedServiceInfoDTO = new SisuSeriyaDTO();
		historyAgreementsDTO = new SisuSeriyaDTO();
		historyBankInfoDTO = new SisuSeriyaDTO();
		searchedSchoolInfoDTO = new SisuSeriyaDTO();
		historySchoolInfoDTO = new SisuSeriyaDTO();
		selectedHistoryRecord = new ArrayList<SisuSeriyaDTO>();

		selectedHistoryRecord.add(viewSelect);

		setShowDetailsForm(true);
		activeTabIndexViewHistory = 0;
		selectedServiceNoInGrid = viewSelect.getServiceNo();
		selectedRefNoInGrid = viewSelect.getServiceRefNo();
		selectedServiceNoForDoc = viewSelect.getServiceNo();
		selectedRefNoForDoc = viewSelect.getServiceRefNo();
		searchedServiceInfoDTO = sisuSariyaService.getSearchedServiceInfo(viewSelect.getServiceRefNo(),
				viewSelect.getServiceNo(), viewSelect.getRequestNo());
		setHistoryAgreementsDTO(searchedServiceInfoDTO);
		setSelectedLangauge(searchedServiceInfoDTO.getLanguageCode());
		if (searchedServiceInfoDTO.getProvinceCode() != null) {
			historyAgreementsDTO.setProvinceDes(
					sisuSariyaService.getProvincesDescription(searchedServiceInfoDTO.getProvinceCode()));
		} else if (searchedServiceInfoDTO.getDistrictCode() != null
				&& searchedServiceInfoDTO.getProvinceCode() != null) {
			historyAgreementsDTO.setDistrictDes(sisuSariyaService.getDistrictDescription(
					searchedServiceInfoDTO.getDistrictCode(), searchedServiceInfoDTO.getProvinceCode()));
		} else if (searchedServiceInfoDTO.getDistrictCode() != null && searchedServiceInfoDTO.getProvinceCode() != null
				&& searchedServiceInfoDTO.getDivisionalSecCode() != null) {
			historyAgreementsDTO.setDivisionalSecCode(
					sisuSariyaService.getDivisionSectionDescription(searchedServiceInfoDTO.getDistrictCode(),
							searchedServiceInfoDTO.getProvinceCode(), searchedServiceInfoDTO.getDivisionalSecCode()));
		}

		if (searchedServiceInfoDTO.getServiceTypeCode() != null) {
			historyAgreementsDTO.setServiceTypeDes(
					sisuSariyaService.getServiceTypeDescription(searchedServiceInfoDTO.getServiceTypeCode()));
		} else {
			historyAgreementsDTO.setServiceTypeDes("N/A");
		}

		historyBankInfoDTO.setAccountNo(searchedServiceInfoDTO.getAccountNo());

		if (searchedServiceInfoDTO.getBankNameCode() != null) {
			historyBankInfoDTO.setBankNameCode(searchedServiceInfoDTO.getBankNameCode());
			bankBranchNameList = new ArrayList<SisuSeriyaDTO>();
			bankBranchNameList = sisuSariyaService.getBranchesForBanksList(searchedServiceInfoDTO.getBankNameCode());
			if (searchedServiceInfoDTO.getBankBranchNameCode().equals("")
					|| searchedServiceInfoDTO.getBankBranchNameCode() != null) {
				historyBankInfoDTO.setBankBranchNameCode(searchedServiceInfoDTO.getBankBranchNameCode());
			}
		} else {

			historyBankInfoDTO.setAccountNo(null);
			historyBankInfoDTO.setBankNameCode(null);
			historyBankInfoDTO.setBankBranchNameCode(null);
		}

		searchedSchoolInfoDTO = sisuSariyaService.getSearchedSchoolInfo(viewSelect.getServiceRefNo(),
				viewSelect.getServiceNo(), viewSelect.getRequestNo());
		setHistorySchoolInfoDTO(searchedSchoolInfoDTO);
		if (searchedSchoolInfoDTO.getSchoolProvinceCode() != null) {
			historySchoolInfoDTO.setSchoolProvinceDes(
					sisuSariyaService.getProvincesDescription(searchedSchoolInfoDTO.getSchoolProvinceCode()));
		} else if (searchedSchoolInfoDTO.getSchoolDistrictCode() != null
				&& searchedSchoolInfoDTO.getSchoolProvinceCode() != null) {
			historySchoolInfoDTO.setSchoolDistrictDes(sisuSariyaService.getDistrictDescription(
					searchedSchoolInfoDTO.getSchoolDistrictCode(), searchedSchoolInfoDTO.getSchoolProvinceCode()));
		} else if (searchedSchoolInfoDTO.getSchoolDistrictCode() != null
				&& searchedSchoolInfoDTO.getSchoolProvinceCode() != null
				&& searchedSchoolInfoDTO.getSchoolDivisinSecCode() != null) {
			historySchoolInfoDTO.setSchoolDivisionSecDes(sisuSariyaService.getDivisionSectionDescription(
					searchedSchoolInfoDTO.getSchoolDistrictCode(), searchedSchoolInfoDTO.getSchoolProvinceCode(),
					searchedSchoolInfoDTO.getSchoolDivisinSecCode()));
		}
	}

	public void viewHistoryBack() {
		setShowDetailsForm(false);
		searchedServiceInfoDTO = new SisuSeriyaDTO();
		historyAgreementsDTO = new SisuSeriyaDTO();
		historyBankInfoDTO = new SisuSeriyaDTO();
		searchedSchoolInfoDTO = new SisuSeriyaDTO();
		historySchoolInfoDTO = new SisuSeriyaDTO();
		selectedHistoryRecord = new ArrayList<SisuSeriyaDTO>();
	}

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

	public SurveyDTO getSurveyDTO() {
		return surveyDTO;
	}

	public void setSurveyDTO(SurveyDTO surveyDTO) {
		this.surveyDTO = surveyDTO;
	}

	public SisuSeriyaDTO getSisuSeriyaDTO() {
		return sisuSeriyaDTO;
	}

	public void setSisuSeriyaDTO(SisuSeriyaDTO sisuSeriyaDTO) {
		this.sisuSeriyaDTO = sisuSeriyaDTO;
	}

	public BusOwnerDTO getBusOwnerDTO() {
		return busOwnerDTO;
	}

	public void setBusOwnerDTO(BusOwnerDTO busOwnerDTO) {
		this.busOwnerDTO = busOwnerDTO;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
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

	public List<SisuSeriyaDTO> getTblPermitHolderInfo() {
		return tblPermitHolderInfo;
	}

	public void setTblPermitHolderInfo(List<SisuSeriyaDTO> tblPermitHolderInfo) {
		this.tblPermitHolderInfo = tblPermitHolderInfo;
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

	public boolean isDisableTab01Save() {
		return disableTab01Save;
	}

	public void setDisableTab01Save(boolean disableTab01Save) {
		this.disableTab01Save = disableTab01Save;
	}

	public boolean isDisableTab01Cancel() {
		return disableTab01Cancel;
	}

	public void setDisableTab01Cancel(boolean disableTab01Cancel) {
		this.disableTab01Cancel = disableTab01Cancel;
	}

	public boolean isDisableTab01UploadRoadMap() {
		return disableTab01UploadRoadMap;
	}

	public void setDisableTab01UploadRoadMap(boolean disableTab01UploadRoadMap) {
		this.disableTab01UploadRoadMap = disableTab01UploadRoadMap;
	}

	public boolean isDisableTab01DocumentManagement() {
		return disableTab01DocumentManagement;
	}

	public void setDisableTab01DocumentManagement(boolean disableTab01DocumentManagement) {
		this.disableTab01DocumentManagement = disableTab01DocumentManagement;
	}

	public SisuSariyaService getSisuSariyaService() {
		return sisuSariyaService;
	}

	public void setSisuSariyaService(SisuSariyaService sisuSariyaService) {
		this.sisuSariyaService = sisuSariyaService;
	}

	public List<CommonDTO> getDrpdStatuslist() {
		return drpdStatuslist;
	}

	public void setDrpdStatuslist(List<CommonDTO> drpdStatuslist) {
		this.drpdStatuslist = drpdStatuslist;
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

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
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

	public boolean isRenderEdit() {
		return renderEdit;
	}

	public void setRenderEdit(boolean renderEdit) {
		this.renderEdit = renderEdit;
	}

	public List<SisuSeriyaDTO> getDrpdServiceReferanceNoList() {
		return drpdServiceReferanceNoList;
	}

	public void setDrpdServiceReferanceNoList(List<SisuSeriyaDTO> drpdServiceReferanceNoList) {
		this.drpdServiceReferanceNoList = drpdServiceReferanceNoList;
	}

	public Date getTemptServiceEndDate() {
		return temptServiceEndDate;
	}

	public void setTemptServiceEndDate(Date temptServiceEndDate) {
		this.temptServiceEndDate = temptServiceEndDate;
	}

	public Date getOldtServiceEndDate() {
		return oldtServiceEndDate;
	}

	public void setOldtServiceEndDate(Date oldtServiceEndDate) {
		this.oldtServiceEndDate = oldtServiceEndDate;
	}

	public List<SisuSeriyaDTO> getDrpdServiceNoList() {
		return drpdServiceNoList;
	}

	public void setDrpdServiceNoList(List<SisuSeriyaDTO> drpdServiceNoList) {
		this.drpdServiceNoList = drpdServiceNoList;
	}

	public boolean isSelect() {
		return select;
	}

	public void setSelect(boolean select) {
		this.select = select;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public boolean isDisablePermitNo() {
		return disablePermitNo;
	}

	public void setDisablePermitNo(boolean disablePermitNo) {
		this.disablePermitNo = disablePermitNo;
	}

	public boolean isSltb() {
		return sltb;
	}

	public void setSltb(boolean sltb) {
		this.sltb = sltb;
	}

	public String getAlertMsg() {
		return alertMsg;
	}

	public void setAlertMsg(String alertMsg) {
		this.alertMsg = alertMsg;
	}

	public boolean isCreateMode() {
		return createMode;
	}

	public void setCreateMode(boolean createMode) {
		this.createMode = createMode;
	}

	public boolean isViewMode() {
		return viewMode;
	}

	public void setViewMode(boolean viewMode) {
		this.viewMode = viewMode;
	}

	public boolean isRenderButton() {
		return renderButton;
	}

	public void setRenderButton(boolean renderButton) {
		this.renderButton = renderButton;
	}

	public List<SisuSeriyaDTO> getServiceHistoryList() {
		return serviceHistoryList;
	}

	public void setServiceHistoryList(List<SisuSeriyaDTO> serviceHistoryList) {
		this.serviceHistoryList = serviceHistoryList;
	}

	public boolean isDisableTab03() {
		return disableTab03;
	}

	public void setDisableTab03(boolean disableTab03) {
		this.disableTab03 = disableTab03;
	}

	public boolean isDisableTab02() {
		return disableTab02;
	}

	public void setDisableTab02(boolean disableTab02) {
		this.disableTab02 = disableTab02;
	}

	public List<SisuSeriyaDTO> getDrpdOperatorDepoNameList() {
		return drpdOperatorDepoNameList;
	}

	public void setDrpdOperatorDepoNameList(List<SisuSeriyaDTO> drpdOperatorDepoNameList) {
		this.drpdOperatorDepoNameList = drpdOperatorDepoNameList;
	}

	public boolean isCancellation() {
		return cancellation;
	}

	public void setCancellation(boolean cancellation) {
		this.cancellation = cancellation;
	}

	public boolean isShowDetailsForm() {
		return showDetailsForm;
	}

	public void setShowDetailsForm(boolean showDetailsForm) {
		this.showDetailsForm = showDetailsForm;
	}

	public SisuSeriyaDTO getViewSelect() {
		return viewSelect;
	}

	public void setViewSelect(SisuSeriyaDTO viewSelect) {
		this.viewSelect = viewSelect;
	}

	public String getSelectedServiceNoInGrid() {
		return selectedServiceNoInGrid;
	}

	public void setSelectedServiceNoInGrid(String selectedServiceNoInGrid) {
		this.selectedServiceNoInGrid = selectedServiceNoInGrid;
	}

	public String getSelectedRefNoInGrid() {
		return selectedRefNoInGrid;
	}

	public void setSelectedRefNoInGrid(String selectedRefNoInGrid) {
		this.selectedRefNoInGrid = selectedRefNoInGrid;
	}

	public String getSelectedServiceNoForDoc() {
		return selectedServiceNoForDoc;
	}

	public void setSelectedServiceNoForDoc(String selectedServiceNoForDoc) {
		this.selectedServiceNoForDoc = selectedServiceNoForDoc;
	}

	public String getSelectedRefNoForDoc() {
		return selectedRefNoForDoc;
	}

	public void setSelectedRefNoForDoc(String selectedRefNoForDoc) {
		this.selectedRefNoForDoc = selectedRefNoForDoc;
	}

	public String getSelectedLangauge() {
		return selectedLangauge;
	}

	public void setSelectedLangauge(String selectedLangauge) {
		this.selectedLangauge = selectedLangauge;
	}

	public SisuSeriyaDTO getSearchedServiceInfoDTO() {
		return searchedServiceInfoDTO;
	}

	public void setSearchedServiceInfoDTO(SisuSeriyaDTO searchedServiceInfoDTO) {
		this.searchedServiceInfoDTO = searchedServiceInfoDTO;
	}

	public SisuSeriyaDTO getHistoryAgreementsDTO() {
		return historyAgreementsDTO;
	}

	public void setHistoryAgreementsDTO(SisuSeriyaDTO historyAgreementsDTO) {
		this.historyAgreementsDTO = historyAgreementsDTO;
	}

	public SisuSeriyaDTO getHistoryBankInfoDTO() {
		return historyBankInfoDTO;
	}

	public void setHistoryBankInfoDTO(SisuSeriyaDTO historyBankInfoDTO) {
		this.historyBankInfoDTO = historyBankInfoDTO;
	}

	public SisuSeriyaDTO getSearchedSchoolInfoDTO() {
		return searchedSchoolInfoDTO;
	}

	public void setSearchedSchoolInfoDTO(SisuSeriyaDTO searchedSchoolInfoDTO) {
		this.searchedSchoolInfoDTO = searchedSchoolInfoDTO;
	}

	public SisuSeriyaDTO getHistorySchoolInfoDTO() {
		return historySchoolInfoDTO;
	}

	public void setHistorySchoolInfoDTO(SisuSeriyaDTO historySchoolInfoDTO) {
		this.historySchoolInfoDTO = historySchoolInfoDTO;
	}

	public List<SisuSeriyaDTO> getBankBranchNameList() {
		return bankBranchNameList;
	}

	public void setBankBranchNameList(List<SisuSeriyaDTO> bankBranchNameList) {
		this.bankBranchNameList = bankBranchNameList;
	}

	public List<SisuSeriyaDTO> getLanguageList() {
		return languageList;
	}

	public void setLanguageList(List<SisuSeriyaDTO> languageList) {
		this.languageList = languageList;
	}

	public List<SisuSeriyaDTO> getBankNameList() {
		return bankNameList;
	}

	public void setBankNameList(List<SisuSeriyaDTO> bankNameList) {
		this.bankNameList = bankNameList;
	}

	public int getActiveTabIndexViewHistory() {
		return activeTabIndexViewHistory;
	}

	public void setActiveTabIndexViewHistory(int activeTabIndexViewHistory) {
		this.activeTabIndexViewHistory = activeTabIndexViewHistory;
	}

	public List<SisuSeriyaDTO> getSelectedHistoryRecord() {
		return selectedHistoryRecord;
	}

	public void setSelectedHistoryRecord(List<SisuSeriyaDTO> selectedHistoryRecord) {
		this.selectedHistoryRecord = selectedHistoryRecord;
	}

}
