package lk.informatics.ntc.view.beans;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
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
import org.primefaces.event.SelectEvent;

import lk.informatics.ntc.model.dto.BusOwnerDTO;
import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.DocumentManagementDTO;
import lk.informatics.ntc.model.dto.GenerateReceiptDTO;
import lk.informatics.ntc.model.dto.SisuSeriyaDTO;
import lk.informatics.ntc.model.dto.SurveyDTO;
import lk.informatics.ntc.model.service.AdminService;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.DocumentManagementService;
import lk.informatics.ntc.model.service.EmployeeProfileService;
import lk.informatics.ntc.model.service.SisuSariyaService;
import lk.informatics.ntc.model.service.SurveyService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "ssPermitHolderInfoBean")
@ViewScoped
public class SsPermitHolderInfoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private SurveyDTO surveyDTO;
	private SisuSeriyaDTO sisuSeriyaDTO;
	private GenerateReceiptDTO branchDTO;
	private int activeTabIndex;

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
	private boolean renderView;
	private boolean renderEditMode;

	private boolean disableTab01;
	private boolean disableTab02;
	private boolean disableTab03;

	private boolean disableBankInfo;

	private boolean disabledReqNo;
	private boolean renderClear;
	private boolean renderBack;

	private boolean disablePermitNo;

	private boolean sltb;

	private String alertMsg;

	// drop down lists
	private List<SisuSeriyaDTO> drpdRequestNoList;
	private List<CommonDTO> drpdServiceTypeList;
	private List<SisuSeriyaDTO> drpdOriginList;
	private List<SisuSeriyaDTO> drpdDestinationList;
	private List<SisuSeriyaDTO> drpdViaList;

	private List<CommonDTO> drpdProvincelList;
	private List<CommonDTO> drpdDistrictList;
	private List<CommonDTO> drpdDevsecList;

	private List<CommonDTO> drpdSchoolProvincelList;
	private List<CommonDTO> drpdSchoolDistrictList;
	private List<CommonDTO> drpdSchoolDevsecList;

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

	boolean permissionCreate = false;
	boolean permissionEdit = false;
	boolean permissionView = false;

	@PostConstruct
	public void init() {
		disabledReqNo = false;
		renderClear = true;
		renderBack = false;
		view = false;
		disableBankInfo = false;
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

		drpdRequestNoList = new ArrayList<SisuSeriyaDTO>();

		drpdOriginList = new ArrayList<SisuSeriyaDTO>();
		drpdDestinationList = new ArrayList<SisuSeriyaDTO>();
		drpdBankList = new ArrayList<GenerateReceiptDTO>();
		drpdBankBranchList = new ArrayList<GenerateReceiptDTO>();
		tblPermitHolderInfo = new ArrayList<SisuSeriyaDTO>();
		drpdViaList = new ArrayList<SisuSeriyaDTO>();

		loadValues();

		if (sessionBackingBean.isSisuSariya() == true) {
			drpdOriginList = sisuSariyaService.drpdOriginList(sessionBackingBean.getRequestNo());
			drpdDestinationList = sisuSariyaService.drpdDestinationList(sessionBackingBean.getRequestNo());

			getServiceInformation(sessionBackingBean.getServiceRefNo());
			disabledReqNo = true;
			renderClear = false;
			renderBack = true;

			sessionBackingBean.setSisuSariya(false);

			renderTabView = true;
			renderBtnSearch = true;
		}
	}

	private void loadValues() {
		drpdRequestNoList = sisuSariyaService.getRequesNoDropDown();
		drpdServiceTypeList = adminService.getServiceTypeToDropdown();

		drpdProvincelList = adminService.getProvinceToDropdown();
		drpdDevsecList = adminService.getDivSecToDropdown();
		drpdDistrictList = adminService.getDistrictToDropdown();

		drpdSchoolProvincelList = adminService.getProvinceToDropdown();
		drpdSchoolDistrictList = adminService.getDistrictToDropdown();
		drpdSchoolDevsecList = adminService.getDivSecToDropdown();

		drpdStatuslist = employeeProfileService.GetMartialToDropdown();
		drpdBankList = surveyService.getBankListDropDown();

		permissionCreate = commonService.checkAccessPermission(sessionBackingBean.loginUser, "FN102", "C");
		permissionEdit = commonService.checkAccessPermission(sessionBackingBean.loginUser, "FN102", "E");
		permissionView = commonService.checkAccessPermission(sessionBackingBean.loginUser, "FN102", "V");

		if (permissionCreate && permissionEdit && permissionView) {
			edit = false;
			renderEditMode = true;
			renderViewMode = true;
		} else if (permissionCreate && permissionEdit && !permissionView) {
			edit = false;
			renderEditMode = true;
			renderViewMode = false;
		} else if (permissionCreate && permissionView && !permissionEdit) {
			edit = false;
			renderEditMode = false;
			renderViewMode = true;
		} else if (permissionEdit && permissionView && !permissionCreate) {
			edit = true;
			renderEditMode = true;
			renderViewMode = true;
		} else if (permissionCreate && !permissionView && !permissionEdit) {
			edit = false;
			renderEditMode = false;
			renderViewMode = false;
		} else if (permissionEdit && !permissionView && !permissionCreate) {
			edit = true;
			renderEditMode = true;
			renderViewMode = false;
		} else if (permissionView && !permissionCreate && !permissionEdit) {
			view = true;
			renderEditMode = false;
			renderViewMode = true;
		}

		// Set the Request Start Date to current date by default
		Date today = new Date();
		sisuSeriyaDTO.setRequestStartDate(today);
		SimpleDateFormat frm = new SimpleDateFormat("yyyy/MM/dd");
		sisuSeriyaDTO.setRequestStartDateString(frm.format(today));
		if (sisuSeriyaDTO.getRequestStartDateString() != null && !sisuSeriyaDTO.getRequestStartDateString().isEmpty()
				&& !sisuSeriyaDTO.getRequestStartDateString().equalsIgnoreCase("")) {
			drpdRequestNoList = sisuSariyaService.drpdRequestNoList(sisuSeriyaDTO);
		}
	}

	public void onOriginChange() {

		if (sisuSeriyaDTO.getDestinationCode() != null && !sisuSeriyaDTO.getDestinationCode().isEmpty()
				&& !sisuSeriyaDTO.getDestinationCode().equals("") && sisuSeriyaDTO.getOriginCode() != null
				&& !sisuSeriyaDTO.getOriginCode().isEmpty() && !sisuSeriyaDTO.getOriginCode().equals("")) {

			drpdDestinationList = sisuSariyaService.drpdDestinationListByOrigin(sisuSeriyaDTO);
			drpdViaList = sisuSariyaService.drpdViaList(sisuSeriyaDTO);
			disableVia = false;

		} else {
			drpdOriginList = sisuSariyaService.drpdOriginList(sisuSeriyaDTO.getRequestNo());
			drpdDestinationList = sisuSariyaService.drpdDestinationList(sisuSeriyaDTO.getRequestNo());
			drpdViaList = sisuSariyaService.drpdViaList(sisuSeriyaDTO);
			disableVia = true;
		}
	}

	public void onDestinationChange() {

		drpdViaList = sisuSariyaService.drpdViaList(sisuSeriyaDTO);

		if (sisuSeriyaDTO.getDestinationCode() != null && !sisuSeriyaDTO.getDestinationCode().isEmpty()
				&& !sisuSeriyaDTO.getDestinationCode().equals("") && sisuSeriyaDTO.getOriginCode() != null
				&& !sisuSeriyaDTO.getOriginCode().isEmpty() && !sisuSeriyaDTO.getOriginCode().equals("")) {

			drpdOriginList = sisuSariyaService.drpdOriginList(sisuSeriyaDTO.getRequestNo());
			drpdViaList = sisuSariyaService.drpdViaList(sisuSeriyaDTO);

			if (!(drpdViaList.size() > 0)) {
				sisuSeriyaDTO.setOriginCode(null);
			}
			disableVia = false;

		} else {

			drpdOriginList = sisuSariyaService.drpdOriginList(sisuSeriyaDTO.getRequestNo());
			drpdDestinationList = sisuSariyaService.drpdDestinationList(sisuSeriyaDTO.getRequestNo());
			drpdViaList = sisuSariyaService.drpdViaList(sisuSeriyaDTO);
			disableVia = true;
		}

	}

	public void onProvinceChange() {
		drpdDistrictList = adminService.getDistrictByProvinceToDropdown(sisuSeriyaDTO.getProvinceCode());
		sisuSeriyaDTO.setDivisionalSecCode(null);
	}

	public void onDistrictChange() {
		drpdDevsecList = adminService.getDivSecByDistrictToDropdown(sisuSeriyaDTO.getDistrictCode());
	}

	public void onSchoolProvinceChange() {
		drpdSchoolDistrictList = adminService.getDistrictByProvinceToDropdown(sisuSeriyaDTO.getSchoolProvinceCode());
		sisuSeriyaDTO.setSchoolDivisinSecCode(null);
	}

	public void onSchoolDistrictChange() {
		drpdSchoolDevsecList = adminService.getDivSecByDistrictToDropdown(sisuSeriyaDTO.getSchoolDistrictCode());
	}

	public void onRequestStartDateChange(SelectEvent event) throws ParseException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		String dateFormat = "yyyy/MM/dd";
		SimpleDateFormat frm = new SimpleDateFormat(dateFormat);
		startDateValue = frm.format(event.getObject());
		Date startDateValueObj = frm.parse(startDateValue);
		sisuSeriyaDTO.setRequestStartDateString(startDateValue);
		if (sisuSeriyaDTO.getRequestStartDateString() != null && !sisuSeriyaDTO.getRequestStartDateString().isEmpty()
				&& !sisuSeriyaDTO.getRequestStartDateString().equalsIgnoreCase("")) {
			drpdRequestNoList = sisuSariyaService.drpdRequestNoList(sisuSeriyaDTO);
		}

	}

	public void onRequestEndDateChange(SelectEvent event) throws ParseException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		String dateFormat = "yyyy/MM/dd";
		SimpleDateFormat frm = new SimpleDateFormat(dateFormat);
		endDateValue = frm.format(event.getObject());
		Date startDateValueObj = frm.parse(endDateValue);
		// sisuSeriyaDTO.setRequestEndDate(startDateValueObj);
		sisuSeriyaDTO.setRequestEndDateString(endDateValue);
		if (sisuSeriyaDTO.getRequestEndDateString() != null && !sisuSeriyaDTO.getRequestEndDateString().isEmpty()
				&& !sisuSeriyaDTO.getRequestEndDateString().equalsIgnoreCase("")) {

			drpdRequestNoList = sisuSariyaService.drpdRequestNoList(sisuSeriyaDTO);
		}

	}

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

	public void btnClear() {

		renderTabView = false;
		sisuSeriyaDTO = new SisuSeriyaDTO();
		tblPermitHolderInfo = new ArrayList<SisuSeriyaDTO>();
		view = false;
		disableTab01 = true;
		disableTab02 = true;
		disableTab03 = true;

		// Set the Request Start Date to current date by default
		Date today = new Date();
		sisuSeriyaDTO.setRequestStartDate(today);
		SimpleDateFormat frm = new SimpleDateFormat("yyyy/MM/dd");
		sisuSeriyaDTO.setRequestStartDateString(frm.format(today));
		if (sisuSeriyaDTO.getRequestStartDateString() != null && !sisuSeriyaDTO.getRequestStartDateString().isEmpty()
				&& !sisuSeriyaDTO.getRequestStartDateString().equalsIgnoreCase("")) {
			drpdRequestNoList = sisuSariyaService.drpdRequestNoList(sisuSeriyaDTO);
		}
	}

	public void onSltbChange() {

		if (sltb) {
			sisuSeriyaDTO.setSltbStatus("Y");
		} else {
			sisuSeriyaDTO.setSltbStatus("N");
		}
	}

	public void btnSearch() {

		renderTabView = true;
		disableVia = false;
		String tempt = sisuSeriyaDTO.getRequestNo();
		sisuSeriyaDTO = new SisuSeriyaDTO();
		sisuSeriyaDTO.setRequestNo(tempt);

		if (sisuSeriyaDTO.getRequestNo() != null && !sisuSeriyaDTO.getRequestNo().isEmpty()
				&& !sisuSeriyaDTO.getRequestNo().equalsIgnoreCase("")) {

			drpdOriginList = sisuSariyaService.drpdOriginList(sisuSeriyaDTO.getRequestNo());
			drpdDestinationList = sisuSariyaService.drpdDestinationList(sisuSeriyaDTO.getRequestNo());
			tblPermitHolderInfo = sisuSariyaService.getTblPermitHolderInfo(sisuSeriyaDTO);

			if (tblPermitHolderInfo != null && !tblPermitHolderInfo.isEmpty()) {

				alertMsg = "To Edit/View current existing records use the grid at end of the page.";

				RequestContext.getCurrentInstance().update("frm-serviceInformation");
				RequestContext.getCurrentInstance().execute("PF('alertMessage').show()");

			}

			sisuSeriyaDTO = sisuSariyaService.retrieveSchoolInfoFromRequest(sisuSeriyaDTO);

			if (sisuSeriyaDTO.getSchoolProvinceCode() != null) {
				drpdSchoolDistrictList = adminService
						.getDistrictByProvinceToDropdown(sisuSeriyaDTO.getSchoolProvinceCode());
			}

			if (sisuSeriyaDTO.getSchoolDistrictCode() != null) {
				drpdSchoolDevsecList = adminService
						.getDivSecByDistrictToDropdown(sisuSeriyaDTO.getSchoolDistrictCode());
			}

			sisuSeriyaDTO.setRequestNo(tempt);

			if (permissionCreate) {
				edit = false;
			} else {
				edit = true;
			}

			renderEdit = false;
			view = false;
			disableBankInfo = false;

			if (permissionView && !permissionCreate && !permissionEdit) {
				view = true;
			}

		}

		if (sisuSeriyaDTO.getServiceRefNo() != null && !sisuSeriyaDTO.getServiceRefNo().isEmpty()
				&& !sisuSeriyaDTO.getServiceRefNo().equalsIgnoreCase("")) {

		} else {
			disableTab03 = true;
			disableTab02 = true;
		}

	}

	/**
	 * tab 01
	 * -----------------------------------------------------------------------------------------------------------------------------------------
	 */
	public void btnTab01Save() {

		if (sltb) {
			sisuSeriyaDTO.setSltbStatus("Y");
		} else {
			sisuSeriyaDTO.setSltbStatus("N");
		}

		String dateFormat = "dd/MM/yyyy";
		SimpleDateFormat frm = new SimpleDateFormat(dateFormat);
		if (sisuSeriyaDTO.getServiceStartDateObj() != null && sisuSeriyaDTO.getServiceEndDateObj() != null) {
			sisuSeriyaDTO.setServiceStartDateVal(frm.format(sisuSeriyaDTO.getServiceStartDateObj()));
			sisuSeriyaDTO.setServiceEndDateVal(frm.format(sisuSeriyaDTO.getServiceEndDateObj()));

		}

		if (sisuSeriyaDTO.getRequestNo() != null && !sisuSeriyaDTO.getRequestNo().isEmpty()
				&& !sisuSeriyaDTO.getRequestNo().equalsIgnoreCase("")) {

			if (sisuSeriyaDTO.getLanguageCode() != null && !sisuSeriyaDTO.getLanguageCode().isEmpty()
					&& !sisuSeriyaDTO.getLanguageCode().equalsIgnoreCase("")) {

				if (sisuSeriyaDTO.getServiceTypeCode() != null && !sisuSeriyaDTO.getServiceTypeCode().isEmpty()
						&& !sisuSeriyaDTO.getServiceTypeCode().equalsIgnoreCase("")) {

					if (sisuSeriyaDTO.getSltbStatus().equals("Y") || (sisuSeriyaDTO.getSltbStatus().equals("N")
							&& sisuSeriyaDTO.getBusRegNo() != null && !sisuSeriyaDTO.getBusRegNo().isEmpty()
							&& !sisuSeriyaDTO.getBusRegNo().trim().equalsIgnoreCase(""))) {

						if (sisuSeriyaDTO.getSltbStatus().equals("Y") || (sisuSeriyaDTO.getSltbStatus().equals("N")
								&& sisuSeriyaDTO.getPermitNo() != null && !sisuSeriyaDTO.getPermitNo().isEmpty()
								&& !sisuSeriyaDTO.getPermitNo().trim().equalsIgnoreCase(""))) {

							if (sisuSeriyaDTO.getOriginCode() != null && !sisuSeriyaDTO.getOriginCode().isEmpty()
									&& !sisuSeriyaDTO.getOriginCode().equalsIgnoreCase("")) {

								if (sisuSeriyaDTO.getDestinationCode() != null
										&& !sisuSeriyaDTO.getDestinationCode().isEmpty()
										&& !sisuSeriyaDTO.getDestinationCode().equalsIgnoreCase("")) {

									if (sisuSeriyaDTO.getDistance() != 0) {

										if (sisuSeriyaDTO.getTripsPerDay() != 0) {

											if (sisuSeriyaDTO.getNicNo() != null && !sisuSeriyaDTO.getNicNo().isEmpty()
													&& !sisuSeriyaDTO.getNicNo().trim().equalsIgnoreCase("")) {

												if (sisuSeriyaDTO.getNameOfOperator() != null
														&& !sisuSeriyaDTO.getNameOfOperator().isEmpty()
														&& !sisuSeriyaDTO.getNameOfOperator().trim()
																.equalsIgnoreCase("")) {

													if (sisuSeriyaDTO.getAddressOne() != null
															&& !sisuSeriyaDTO.getAddressOne().isEmpty()
															&& !sisuSeriyaDTO.getAddressOne().trim()
																	.equalsIgnoreCase("")) {

														if (sisuSeriyaDTO.getCity() != null
																&& !sisuSeriyaDTO.getCity().isEmpty() && !sisuSeriyaDTO
																		.getCity().trim().equalsIgnoreCase("")) {

															if (sisuSeriyaDTO.getProvinceCode() != null
																	&& !sisuSeriyaDTO.getProvinceCode().isEmpty()
																	&& !sisuSeriyaDTO.getProvinceCode()
																			.equalsIgnoreCase("")) {

																/** new start */
																if (sisuSeriyaDTO.getLanguageCode().equals("S")) {

																	if (sisuSeriyaDTO.getAddressOneSin() != null
																			&& !sisuSeriyaDTO.getAddressOneSin()
																					.isEmpty()
																			&& !sisuSeriyaDTO.getAddressOneSin()
																					.equalsIgnoreCase("")) {

																		if (sisuSeriyaDTO.getCitySin() != null
																				&& !sisuSeriyaDTO.getCitySin().isEmpty()
																				&& !sisuSeriyaDTO.getCitySin()
																						.equalsIgnoreCase("")) {

																			/**
																			 * update
																			 */
																			if (edit) {

																				if (sisuSeriyaDTO
																						.getServiceRefNo() != null
																						&& !sisuSeriyaDTO
																								.getServiceRefNo()
																								.isEmpty()
																						&& !sisuSeriyaDTO
																								.getServiceRefNo()
																								.equalsIgnoreCase("")) {

																					if (sisuSariyaService
																							.updateServiceInformationByServiceRefNo(
																									sisuSeriyaDTO)) {

																						RequestContext
																								.getCurrentInstance()
																								.execute(
																										"PF('successMessage').show()");
																					} else {

																						errorMsg = "Could not update data.";
																						RequestContext
																								.getCurrentInstance()
																								.update("frm-serviceInformation");
																						RequestContext
																								.getCurrentInstance()
																								.execute(
																										"PF('requiredField').show()");
																					}
																				} else {
																					errorMsg = "Service Reference No. is empty. Select an application from the grid.";
																					RequestContext.getCurrentInstance()
																							.update("frm-serviceInformation");
																					RequestContext.getCurrentInstance()
																							.execute(
																									"PF('requiredField').show()");
																				}

																				/**
																				 * save
																				 */
																			} else {
																				sisuSeriyaDTO = sisuSariyaService
																						.insertServiceInformation(
																								sisuSeriyaDTO,
																								sessionBackingBean.loginUser);
																				tblPermitHolderInfo = sisuSariyaService
																						.getTblPermitHolderInfo(
																								sisuSeriyaDTO);

																				if (sisuSeriyaDTO
																						.getServiceRefNo() != null
																						&& !sisuSeriyaDTO
																								.getServiceRefNo()
																								.isEmpty()
																						&& !sisuSeriyaDTO
																								.getServiceRefNo()
																								.equalsIgnoreCase("")) {

																					disableTab03 = false;
																					disableTab02 = false;
																					renderEdit = false;
																					edit = true;

																					commonService
																							.insertTaskDetailsSubsidy(
																									sisuSeriyaDTO
																											.getRequestNo(),
																									null,
																									sessionBackingBean.loginUser,
																									"SS002", "C",
																									sisuSeriyaDTO
																											.getServiceRefNo());
																					commonService
																							.updateTaskStatusCompletedSubsidyTaskTabel(
																									sisuSeriyaDTO
																											.getRequestNo(),
																									null,
																									sisuSeriyaDTO
																											.getServiceRefNo(),
																									"SS003", "O");
																					commonService
																							.updateTaskStatusCompletedSubsidyTaskTabel(
																									sisuSeriyaDTO
																											.getRequestNo(),
																									null,
																									sisuSeriyaDTO
																											.getServiceRefNo(),
																									"SS003", "C");
																					commonService
																							.updateTaskStatusCompletedSubsidyTaskTabel(
																									sisuSeriyaDTO
																											.getRequestNo(),
																									null,
																									sisuSeriyaDTO
																											.getServiceRefNo(),
																									"SS004", "O");

																					RequestContext.getCurrentInstance()
																							.execute(
																									"PF('successMessage').show()");
																					activeTabIndex = 1;
																					alertMsg = "Save School Information to continue.";

																					RequestContext.getCurrentInstance()
																							.update("frmAlert");
																					RequestContext.getCurrentInstance()
																							.execute(
																									"PF('alertMessage').show()");
																				} else {
																					errorMsg = "Could not add data.";
																					RequestContext.getCurrentInstance()
																							.update("frm-serviceInformation");
																					RequestContext.getCurrentInstance()
																							.execute(
																									"PF('requiredField').show()");

																				}
																			}

																		} else {
																			errorMsg = "City (Sinhala) should be entered.";
																			RequestContext.getCurrentInstance()
																					.update("frm-serviceInformation");
																			RequestContext.getCurrentInstance().execute(
																					"PF('requiredField').show()");
																		}

																	} else {
																		errorMsg = "Address Line 1 (Sinhala) should be entered.";
																		RequestContext.getCurrentInstance()
																				.update("frm-serviceInformation");
																		RequestContext.getCurrentInstance()
																				.execute("PF('requiredField').show()");
																	}

																} else if (sisuSeriyaDTO.getLanguageCode()
																		.equals("T")) {

																	if (sisuSeriyaDTO.getAddressOneTamil() != null
																			&& !sisuSeriyaDTO.getAddressOneTamil()
																					.isEmpty()
																			&& !sisuSeriyaDTO.getAddressOneTamil()
																					.equalsIgnoreCase("")) {

																		if (sisuSeriyaDTO.getCityTamil() != null
																				&& !sisuSeriyaDTO.getCityTamil()
																						.isEmpty()
																				&& !sisuSeriyaDTO.getCityTamil()
																						.equalsIgnoreCase("")) {

																			/**
																			 * update
																			 */
																			if (edit) {

																				if (sisuSeriyaDTO
																						.getServiceRefNo() != null
																						&& !sisuSeriyaDTO
																								.getServiceRefNo()
																								.isEmpty()
																						&& !sisuSeriyaDTO
																								.getServiceRefNo()
																								.equalsIgnoreCase("")) {

																					if (sisuSariyaService
																							.updateServiceInformationByServiceRefNo(
																									sisuSeriyaDTO)) {

																						RequestContext
																								.getCurrentInstance()
																								.execute(
																										"PF('successMessage').show()");
																					} else {

																						errorMsg = "Could not update data.";
																						RequestContext
																								.getCurrentInstance()
																								.update("frm-serviceInformation");
																						RequestContext
																								.getCurrentInstance()
																								.execute(
																										"PF('requiredField').show()");
																					}

																				} else {
																					errorMsg = "Service Reference No. is empty. Select an application from the grid.";
																					RequestContext.getCurrentInstance()
																							.update("frm-serviceInformation");
																					RequestContext.getCurrentInstance()
																							.execute(
																									"PF('requiredField').show()");
																				}
																				/**
																				 * save
																				 */
																			} else {
																				sisuSeriyaDTO = sisuSariyaService
																						.insertServiceInformation(
																								sisuSeriyaDTO,
																								sessionBackingBean.loginUser);
																				tblPermitHolderInfo = sisuSariyaService
																						.getTblPermitHolderInfo(
																								sisuSeriyaDTO);

																				if (sisuSeriyaDTO
																						.getServiceRefNo() != null
																						&& !sisuSeriyaDTO
																								.getServiceRefNo()
																								.isEmpty()
																						&& !sisuSeriyaDTO
																								.getServiceRefNo()
																								.equalsIgnoreCase("")) {

																					disableTab03 = false;
																					disableTab02 = false;
																					renderEdit = false;
																					edit = true;

																					commonService
																							.insertTaskDetailsSubsidy(
																									sisuSeriyaDTO
																											.getRequestNo(),
																									null,
																									sessionBackingBean.loginUser,
																									"SS002", "C",
																									sisuSeriyaDTO
																											.getServiceRefNo());
																					commonService
																							.updateTaskStatusCompletedSubsidyTaskTabel(
																									sisuSeriyaDTO
																											.getRequestNo(),
																									null,
																									sisuSeriyaDTO
																											.getServiceRefNo(),
																									"SS003", "O");
																					commonService
																							.updateTaskStatusCompletedSubsidyTaskTabel(
																									sisuSeriyaDTO
																											.getRequestNo(),
																									null,
																									sisuSeriyaDTO
																											.getServiceRefNo(),
																									"SS003", "C");
																					commonService
																							.updateTaskStatusCompletedSubsidyTaskTabel(
																									sisuSeriyaDTO
																											.getRequestNo(),
																									null,
																									sisuSeriyaDTO
																											.getServiceRefNo(),
																									"SS004", "O");

																					activeTabIndex = 1;
																					RequestContext.getCurrentInstance()
																							.execute(
																									"PF('successMessage').show()");
																					alertMsg = "Save School Information to continue.";

																					RequestContext.getCurrentInstance()
																							.update("frmAlert");
																					RequestContext.getCurrentInstance()
																							.execute(
																									"PF('alertMessage').show()");
																				} else {
																					errorMsg = "Could not add data.";
																					RequestContext.getCurrentInstance()
																							.update("frm-serviceInformation");
																					RequestContext.getCurrentInstance()
																							.execute(
																									"PF('requiredField').show()");

																				}
																			}

																		} else {
																			errorMsg = "City (Tamil) should be entered.";
																			RequestContext.getCurrentInstance()
																					.update("frm-serviceInformation");
																			RequestContext.getCurrentInstance().execute(
																					"PF('requiredField').show()");
																		}

																	} else {
																		errorMsg = "Address Line 1 (Tamil) should be entered.";
																		RequestContext.getCurrentInstance()
																				.update("frm-serviceInformation");
																		RequestContext.getCurrentInstance()
																				.execute("PF('requiredField').show()");
																	}

																} else {

																	/**
																	 * update
																	 */
																	if (edit) {
																		if (sisuSeriyaDTO.getServiceRefNo() != null
																				&& !sisuSeriyaDTO.getServiceRefNo()
																						.isEmpty()
																				&& !sisuSeriyaDTO.getServiceRefNo()
																						.equalsIgnoreCase("")) {
																			if (sisuSariyaService
																					.updateServiceInformationByServiceRefNo(
																							sisuSeriyaDTO)) {

																				RequestContext.getCurrentInstance()
																						.execute(
																								"PF('successMessage').show()");
																			} else {

																				errorMsg = "Could not update data successfully";
																				RequestContext.getCurrentInstance()
																						.update("frm-serviceInformation");
																				RequestContext.getCurrentInstance()
																						.execute(
																								"PF('requiredField').show()");
																			}
																		} else {
																			errorMsg = "Service Reference No. is empty. Select an application from the grid.";
																			RequestContext.getCurrentInstance()
																					.update("frm-serviceInformation");
																			RequestContext.getCurrentInstance().execute(
																					"PF('requiredField').show()");
																		}
																		/**
																		 * save
																		 */
																	} else {
																		sisuSeriyaDTO = sisuSariyaService
																				.insertServiceInformation(sisuSeriyaDTO,
																						sessionBackingBean.loginUser);
																		tblPermitHolderInfo = sisuSariyaService
																				.getTblPermitHolderInfo(sisuSeriyaDTO);

																		if (sisuSeriyaDTO.getServiceRefNo() != null
																				&& !sisuSeriyaDTO.getServiceRefNo()
																						.isEmpty()
																				&& !sisuSeriyaDTO.getServiceRefNo()
																						.equalsIgnoreCase("")) {

																			disableTab03 = false;
																			disableTab02 = false;
																			renderEdit = false;
																			edit = true;

																			commonService.insertTaskDetailsSubsidy(
																					sisuSeriyaDTO.getRequestNo(), null,
																					sessionBackingBean.loginUser,
																					"SS002", "C",
																					sisuSeriyaDTO.getServiceRefNo());
																			commonService
																					.updateTaskStatusCompletedSubsidyTaskTabel(
																							sisuSeriyaDTO
																									.getRequestNo(),
																							null,
																							sisuSeriyaDTO
																									.getServiceRefNo(),
																							"SS003", "O");
																			commonService
																					.updateTaskStatusCompletedSubsidyTaskTabel(
																							sisuSeriyaDTO
																									.getRequestNo(),
																							null,
																							sisuSeriyaDTO
																									.getServiceRefNo(),
																							"SS003", "C");
																			commonService
																					.updateTaskStatusCompletedSubsidyTaskTabel(
																							sisuSeriyaDTO
																									.getRequestNo(),
																							null,
																							sisuSeriyaDTO
																									.getServiceRefNo(),
																							"SS004", "O");

																			RequestContext.getCurrentInstance().execute(
																					"PF('successMessage').show()");
																			activeTabIndex = 1;

																			alertMsg = "Save School Information to continue.";

																			RequestContext.getCurrentInstance()
																					.update("frmAlert");
																			RequestContext.getCurrentInstance().execute(
																					"PF('alertMessage').show()");

																		} else {
																			errorMsg = "Could not add data.";
																			RequestContext.getCurrentInstance()
																					.update("frm-serviceInformation");
																			RequestContext.getCurrentInstance().execute(
																					"PF('requiredField').show()");

																		}
																	}

																}
																/** new end */

															} else {
																errorMsg = "Province should be selected.";
																RequestContext.getCurrentInstance()
																		.update("frm-serviceInformation");
																RequestContext.getCurrentInstance()
																		.execute("PF('requiredField').show()");
															}

														} else {
															errorMsg = "City (English) should be entered.";
															RequestContext.getCurrentInstance()
																	.update("frm-serviceInformation");
															RequestContext.getCurrentInstance()
																	.execute("PF('requiredField').show()");
														}

													} else {
														errorMsg = "Address Line 1 (English) should be entered.";
														RequestContext.getCurrentInstance()
																.update("frm-serviceInformation");
														RequestContext.getCurrentInstance()
																.execute("PF('requiredField').show()");
													}

												} else {
													errorMsg = "Name of Operator/Depo should be entered.";
													RequestContext.getCurrentInstance()
															.update("frm-serviceInformation");
													RequestContext.getCurrentInstance()
															.execute("PF('requiredField').show()");
												}

											} else {
												errorMsg = "Nic/Org Reg.No. should be entered.";
												RequestContext.getCurrentInstance().update("frm-serviceInformation");
												RequestContext.getCurrentInstance()
														.execute("PF('requiredField').show()");
											}

										} else {
											errorMsg = "Trips Per Day should be entered.";
											RequestContext.getCurrentInstance().update("frm-serviceInformation");
											RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
										}

									} else {
										errorMsg = "Distance should be entered.";
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
							errorMsg = "Permit No. should be entered. (Mandatory for NTC buses)";
							RequestContext.getCurrentInstance().update("frm-serviceInformation");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
						}

					} else {
						errorMsg = "Bus Number should be entered. (Mandatory for NTC buses)";
						RequestContext.getCurrentInstance().update("frm-serviceInformation");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}

				} else {
					errorMsg = "Service Type should be selected.";
					RequestContext.getCurrentInstance().update("frm-serviceInformation");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}

			} else {
				errorMsg = "Preferred Language should be selected.";
				RequestContext.getCurrentInstance().update("frm-serviceInformation");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

		} else {
			errorMsg = "Request No. should be selected.";
			RequestContext.getCurrentInstance().update("frm-serviceInformation");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void btnTab01Update() {

	}

	public void btnTab01Clear() {

		String trmptReqNo = sisuSeriyaDTO.getRequestNo();
		sisuSeriyaDTO = new SisuSeriyaDTO();
		sisuSeriyaDTO.setRequestNo(trmptReqNo);
		sisuSeriyaDTO = sisuSariyaService.retrieveSchoolInfoFromRequest(sisuSeriyaDTO);
		drpdRequestNoList = sisuSariyaService.getRequesNoDropDown();
		edit = false;
		view = false;
		disableBankInfo = false;
		renderEdit = false;
		disableTab03 = true;
		disableTab02 = true;

		drpdOriginList = sisuSariyaService.drpdOriginList(sisuSeriyaDTO.getRequestNo());
		drpdDestinationList = sisuSariyaService.drpdDestinationList(sisuSeriyaDTO.getRequestNo());

	}

	public void btnTab01UploadRoadMap() {

	}

	public void btnTab01DocumentManagement() {

	}

	public void getServiceInformation(String serviceRefNo) {

		this.sisuSeriyaDTO = sisuSariyaService.getServiceInformationByServiceRefNo(serviceRefNo);

		if (sisuSeriyaDTO.getBankNameCode() != null) {
			drpdBankBranchList = surveyService.getBranchListDropDown(sisuSeriyaDTO.getBankNameCode());
		}

		if (sisuSeriyaDTO.getRequestNo() != null && sisuSeriyaDTO.getOriginCode() != null
				&& sisuSeriyaDTO.getDestinationCode() != null) {
			drpdViaList = sisuSariyaService.drpdViaList(sisuSeriyaDTO);
		}

		if (sisuSeriyaDTO.getSltbStatus() != null && sisuSeriyaDTO.getSltbStatus().equals("Y")) {
			disablePermitNo = true;
			sltb = true;
		} else {
			disablePermitNo = false;
			sltb = false;
		}

		this.sisuSeriyaDTO = sisuSariyaService.getSchoolInformationByServiceRefNo(this.sisuSeriyaDTO);
		this.startDateValue = sisuSeriyaDTO.getRequestStartDateString();
		this.endDateValue = sisuSeriyaDTO.getRequestEndDateString();

		view = true;
		if (view) {
			disablePermitNo = true;
		}
		disableBankInfo = true;
		edit = false;
		disableTab03 = false;
		disableTab02 = false;

	}

	public void editServiceInformation(String serviceRefNo) {

		this.sisuSeriyaDTO = sisuSariyaService.getServiceInformationByServiceRefNo(serviceRefNo);
		if (sisuSeriyaDTO.getBankNameCode() != null) {
			drpdBankBranchList = surveyService.getBranchListDropDown(sisuSeriyaDTO.getBankNameCode());
		}
		if (sisuSeriyaDTO.getBankBranchNameCode() != null) {
			branchDTO = surveyService.getBranchListByCodeDropDown(sisuSeriyaDTO.getBankBranchNameCode(),
					sisuSeriyaDTO.getBankNameCode());
			if (branchDTO.getBranchCode() != null) {
				sisuSeriyaDTO.setBankBranchNameCode(branchDTO.getBranchCode());
				sisuSeriyaDTO.setBankBranchNameDes(branchDTO.getBranchDescription());
			}
		} else {
			if (sisuSeriyaDTO.getBankNameCode() != null) {
				drpdBankBranchList = surveyService.getBranchListDropDown(sisuSeriyaDTO.getBankNameCode());
			}
		}

		if (sisuSeriyaDTO.getRequestNo() != null && sisuSeriyaDTO.getOriginCode() != null
				&& sisuSeriyaDTO.getDestinationCode() != null) {
			drpdViaList = sisuSariyaService.drpdViaList(sisuSeriyaDTO);
		}

		if (sisuSeriyaDTO.getProvinceCode() != null) {

			drpdDistrictList = adminService.getDistrictByProvinceToDropdown(sisuSeriyaDTO.getProvinceCode());
		}

		if (sisuSeriyaDTO.getDistrictCode() != null) {

			drpdDevsecList = adminService.getDivSecByDistrictToDropdown(sisuSeriyaDTO.getDistrictCode());
		}

		if (sisuSeriyaDTO.getSltbStatus() != null && sisuSeriyaDTO.getSltbStatus().equals("Y")) {
			disablePermitNo = true;
			sltb = true;
		} else {
			disablePermitNo = false;
			sltb = false;
		}

		if (renderEdit) {
			this.sisuSeriyaDTO = sisuSariyaService.getSchoolInformationByServiceRefNo(sisuSeriyaDTO);
		} else {
			this.sisuSeriyaDTO = sisuSariyaService.retrieveSchoolInfoFromRequest(sisuSeriyaDTO);
		}

		if (sisuSeriyaDTO.getSchoolProvinceCode() != null) {
			drpdSchoolDistrictList = adminService
					.getDistrictByProvinceToDropdown(sisuSeriyaDTO.getSchoolProvinceCode());
		}

		if (sisuSeriyaDTO.getSchoolDistrictCode() != null) {
			drpdSchoolDevsecList = adminService.getDivSecByDistrictToDropdown(sisuSeriyaDTO.getSchoolDistrictCode());
		}

		if (!sisuSeriyaDTO.getStatusCode().equalsIgnoreCase("o")) {
			renderEdit = true;
			this.sisuSeriyaDTO = sisuSariyaService.getSchoolInformationByServiceRefNo(sisuSeriyaDTO);
		} else {
			activeTabIndex = 1;
			this.sisuSeriyaDTO = sisuSariyaService.retrieveSchoolInfoFromRequest(sisuSeriyaDTO);
			renderEdit = false;
			alertMsg = "Save School Information to continue.";
			RequestContext.getCurrentInstance().update("frmAlert");
			RequestContext.getCurrentInstance().execute("PF('alertMessage').show()");
		}

		this.startDateValue = sisuSeriyaDTO.getRequestStartDateString();
		this.endDateValue = sisuSeriyaDTO.getRequestEndDateString();

		view = false;
		disableBankInfo = false;
		edit = true;
		disableTab03 = false;
		disableTab02 = false;
	}

	/**
	 * tab 02
	 * -----------------------------------------------------------------------------------------------------------------------------------------
	 */

	public void btnTab02Save() {

		if (sisuSeriyaDTO.getServiceRefNo() != null && !sisuSeriyaDTO.getServiceRefNo().isEmpty()
				&& !sisuSeriyaDTO.getServiceRefNo().equalsIgnoreCase("")) {

			if (sisuSeriyaDTO.getSchoolName() != null && !sisuSeriyaDTO.getSchoolName().isEmpty()
					&& !sisuSeriyaDTO.getSchoolName().trim().equalsIgnoreCase("")) {

				if (sisuSeriyaDTO.getSchoolAddressOne() != null && !sisuSeriyaDTO.getSchoolAddressOne().isEmpty()
						&& !sisuSeriyaDTO.getSchoolAddressOne().trim().equalsIgnoreCase("")) {

					if (sisuSeriyaDTO.getSchoolCity() != null && !sisuSeriyaDTO.getSchoolCity().isEmpty()
							&& !sisuSeriyaDTO.getSchoolCity().trim().equalsIgnoreCase("")) {

						if (sisuSeriyaDTO.getSchoolProvinceCode() != null
								&& !sisuSeriyaDTO.getSchoolProvinceCode().isEmpty()
								&& !sisuSeriyaDTO.getSchoolProvinceCode().equalsIgnoreCase("")) {

							if (sisuSeriyaDTO.getLanguageCode().equals("S")) {

								if (sisuSeriyaDTO.getSchoolAdrressOneSin() != null
										&& !sisuSeriyaDTO.getSchoolAdrressOneSin().isEmpty()
										&& !sisuSeriyaDTO.getSchoolAdrressOneSin().trim().equalsIgnoreCase("")) {

									if (sisuSeriyaDTO.getSchoolCitySin() != null
											&& !sisuSeriyaDTO.getSchoolCitySin().isEmpty()
											&& !sisuSeriyaDTO.getSchoolCitySin().trim().equalsIgnoreCase("")) {

										/**
										 * update
										 **/
										if (renderEdit) {
											if (sisuSariyaService.updateSchoolInformationByServiceRefNo(sisuSeriyaDTO,
													sessionBackingBean.loginUser)) {

												RequestContext.getCurrentInstance()
														.execute("PF('successMessage').show()");
											} else {

												errorMsg = "Could not update data.";
												RequestContext.getCurrentInstance().update("dlg-errorMsg");
												RequestContext.getCurrentInstance()
														.execute("PF('requiredField').show()");
											}
											/**
											 * insert
											 **/
										} else {

											if (sisuSariyaService.insertSchoolInformation(sisuSeriyaDTO,
													sessionBackingBean.loginUser)) {

												renderEdit = true;
												drpdDevsecList = adminService.getDivSecToDropdown();
												drpdDistrictList = adminService.getDistrictToDropdown();

												commonService.updateTaskStatusCompletedSubsidyTaskTabel(
														sisuSeriyaDTO.getRequestNo(), null,
														sisuSeriyaDTO.getServiceRefNo(), "SS004", "C");
												commonService.updateTaskStatusCompletedSubsidyTaskTabel(
														sisuSeriyaDTO.getRequestNo(), null,
														sisuSeriyaDTO.getServiceRefNo(), "SS005", "O");

												RequestContext.getCurrentInstance()
														.execute("PF('successMessage').show()");
												tblPermitHolderInfo = sisuSariyaService
														.getTblPermitHolderInfo(sisuSeriyaDTO);
											} else {

												errorMsg = "Could not add data.";
												RequestContext.getCurrentInstance().update("dlg-errorMsg");
												RequestContext.getCurrentInstance()
														.execute("PF('requiredField').show()");
											}
										}

									} else {
										errorMsg = "City (Sinhala) should be entered.";
										RequestContext.getCurrentInstance().update("dlg-errorMsg");
										RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
									}

								} else {
									errorMsg = "Address Line 1 (Sinhala) should be entered.";
									RequestContext.getCurrentInstance().update("dlg-errorMsg");
									RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
								}

							} else if (sisuSeriyaDTO.getLanguageCode().equals("T")) {

								if (sisuSeriyaDTO.getSchoolAddressOneTamil() != null
										&& !sisuSeriyaDTO.getSchoolAddressOneTamil().isEmpty()
										&& !sisuSeriyaDTO.getSchoolAddressOneTamil().trim().equalsIgnoreCase("")) {

									if (sisuSeriyaDTO.getSchoolCityTamil() != null
											&& !sisuSeriyaDTO.getSchoolCityTamil().isEmpty()
											&& !sisuSeriyaDTO.getSchoolCityTamil().trim().equalsIgnoreCase("")) {

										/**
										 * update
										 **/
										if (renderEdit) {
											if (sisuSariyaService.updateSchoolInformationByServiceRefNo(sisuSeriyaDTO,
													sessionBackingBean.loginUser)) {
												RequestContext.getCurrentInstance()
														.execute("PF('successMessage').show()");
											} else {
												errorMsg = "Could not update data.";
												RequestContext.getCurrentInstance().update("fdlg-errorMsg");
												RequestContext.getCurrentInstance()
														.execute("PF('requiredField').show()");
											}
											/**
											 * insert
											 **/
										} else {

											if (sisuSariyaService.insertSchoolInformation(sisuSeriyaDTO,
													sessionBackingBean.loginUser)) {

												renderEdit = true;
												drpdDevsecList = adminService.getDivSecToDropdown();
												drpdDistrictList = adminService.getDistrictToDropdown();
												commonService.updateTaskStatusCompletedSubsidyTaskTabel(
														sisuSeriyaDTO.getRequestNo(), null,
														sisuSeriyaDTO.getServiceRefNo(), "SS004", "C");
												commonService.updateTaskStatusCompletedSubsidyTaskTabel(
														sisuSeriyaDTO.getRequestNo(), null,
														sisuSeriyaDTO.getServiceRefNo(), "SS005", "O");
												RequestContext.getCurrentInstance()
														.execute("PF('successMessage').show()");
												tblPermitHolderInfo = sisuSariyaService
														.getTblPermitHolderInfo(sisuSeriyaDTO);
											} else {
												errorMsg = "Could not add data.";
												RequestContext.getCurrentInstance().update("dlg-errorMsg");
												RequestContext.getCurrentInstance()
														.execute("PF('requiredField').show()");
											}
										}

									} else {
										errorMsg = "City (Tamil) should be entered.";
										RequestContext.getCurrentInstance().update("dlg-errorMsg");
										RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
									}

								} else {

									errorMsg = "Address Line 1 (Tamil) should be entered.";
									RequestContext.getCurrentInstance().update("dlg-errorMsg");
									RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
								}

							} else {

								/**
								 * update
								 **/
								if (renderEdit) {
									if (sisuSariyaService.updateSchoolInformationByServiceRefNo(sisuSeriyaDTO,
											sessionBackingBean.loginUser)) {
										RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
									} else {
										errorMsg = "Could not update data successfully";
										RequestContext.getCurrentInstance().update("dlg-errorMsg");
										RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
									}
									/**
									 * insert
									 **/
								} else {

									if (sisuSariyaService.insertSchoolInformation(sisuSeriyaDTO,
											sessionBackingBean.loginUser)) {

										renderEdit = true;
										drpdDevsecList = adminService.getDivSecToDropdown();
										drpdDistrictList = adminService.getDistrictToDropdown();
										commonService.updateTaskStatusCompletedSubsidyTaskTabel(
												sisuSeriyaDTO.getRequestNo(), null, sisuSeriyaDTO.getServiceRefNo(),
												"SS004", "C");
										commonService.updateTaskStatusCompletedSubsidyTaskTabel(
												sisuSeriyaDTO.getRequestNo(), null, sisuSeriyaDTO.getServiceRefNo(),
												"SS005", "O");
										RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
										tblPermitHolderInfo = sisuSariyaService.getTblPermitHolderInfo(sisuSeriyaDTO);

									} else {
										errorMsg = "Could not add data.";
										RequestContext.getCurrentInstance().update("dlg-errorMsg");
										RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
									}
								}

							}

							/**
							 * end
							 */

						} else {
							errorMsg = "Province should be selected.";
							RequestContext.getCurrentInstance().update("dlg-errorMsg");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
						}

					} else {
						errorMsg = "City should be entered.";
						RequestContext.getCurrentInstance().update("dlg-errorMsg");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}

				} else {
					errorMsg = "Address Line 1 name should be entered.";
					RequestContext.getCurrentInstance().update("dlg-errorMsg");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}

			} else {
				errorMsg = "School name should be entered.";
				RequestContext.getCurrentInstance().update("dlg-errorMsg");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

		} else {
			errorMsg = "Service Reference No. cannot be empty.";
			RequestContext.getCurrentInstance().update("dlg-errorMsg");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void btnTab02Clear() {
		if (sisuSeriyaDTO.getServiceRefNo() != null && !sisuSeriyaDTO.getServiceRefNo().isEmpty()
				&& !sisuSeriyaDTO.getServiceRefNo().equalsIgnoreCase("")) {
			sisuSeriyaDTO = sisuSariyaService.getSchoolInformationByServiceRefNo(sisuSeriyaDTO);
		} else {
			errorMsg = "Service Referenece No. cannot be empty.";
			RequestContext.getCurrentInstance().update("dlg-errorMsg");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void documentManagement() {

		try {

			sessionBackingBean.setRequestNoForSisuSariya(sisuSeriyaDTO.getRequestNo());
			sessionBackingBean.setRefNoSisuSariya(sisuSeriyaDTO.getServiceRefNo());
			sessionBackingBean.setTransactionType("SISU SARIYA");

			mandatoryList = documentManagementService.mandatoryDocsForSisuSariyaPermitHolder("18",
					sisuSeriyaDTO.getServiceRefNo());
			optionalList = documentManagementService.optionalDocsForSisuSariyaPermitHolder("18",
					sisuSeriyaDTO.getServiceRefNo());

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

	/**
	 * tab 03
	 * -----------------------------------------------------------------------------------------------------------------------------------------
	 */

	public void onBankChange() {
		drpdBankBranchList = surveyService.getBankBranchDropDown(sisuSeriyaDTO.getBankNameCode());
	}

	public void editBankInformation(String serviceRefNo) {
		view = true;
		if (view) {
			disablePermitNo = true;
		}
		disableTab01 = false;
		disableTab02 = false;
		disableTab03 = false;
		disableBankInfo = false;

		sisuSeriyaDTO = sisuSariyaService.getServiceInformationByServiceRefNo(serviceRefNo);
		sisuSeriyaDTO = sisuSariyaService.retrieveSchoolInfoFromRequest(sisuSeriyaDTO);

		/* Modified By: dinushi.r on 15/07/2020 */
		if (sisuSeriyaDTO.getBankNameCode() != null) {
			drpdBankBranchList = surveyService.getBranchListDropDown(sisuSeriyaDTO.getBankNameCode());
		}
		if (sisuSeriyaDTO.getBankBranchNameCode() != null) {
			branchDTO = surveyService.getBranchListByCodeDropDown(sisuSeriyaDTO.getBankBranchNameCode(),
					sisuSeriyaDTO.getBankNameCode());
			if (branchDTO.getBranchCode() != null) {
				sisuSeriyaDTO.setBankBranchNameCode(branchDTO.getBranchCode());
				sisuSeriyaDTO.setBankBranchNameDes(branchDTO.getBranchDescription());
			}
		} else {
			if (sisuSeriyaDTO.getBankNameCode() != null) {
				drpdBankBranchList = surveyService.getBranchListDropDown(sisuSeriyaDTO.getBankNameCode());
			}
		}
		/* end modification */

	}

	public void btnTab03Save() {

		boolean added = sisuSariyaService.insertBankDetails(sisuSeriyaDTO, sessionBackingBean.loginUser);

		if (added) {

			if (!commonService.checkTaskDetailsInSubsidyByServiceRefNo(sisuSeriyaDTO.getServiceRefNo(), "SS005", "C")) {
				commonService.updateTaskStatusCompletedSubsidyTaskTabel(sisuSeriyaDTO.getRequestNo(), null,
						sisuSeriyaDTO.getServiceRefNo(), "SS005", "C");
			}

			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
		} else {
			errorMsg = "Could not add bank details.";
			RequestContext.getCurrentInstance().update("dlg-errorMsg");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public void btnTab03Clear() {
		sisuSeriyaDTO.setAccountNo(null);
		sisuSeriyaDTO.setBankNameCode(null);
		sisuSeriyaDTO.setBankBranchNameCode(null);
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
		setRenderBack(true);
		setRenderClear(true);
		setDisabledReqNo(false);
		RequestContext.getCurrentInstance().update("btnBackSisuSariyaAuth");
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

	public List<SisuSeriyaDTO> getDrpdRequestNoList() {
		return drpdRequestNoList;
	}

	public void setDrpdRequestNoList(List<SisuSeriyaDTO> drpdRequestNoList) {
		this.drpdRequestNoList = drpdRequestNoList;
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

	public boolean isDisabledReqNo() {
		return disabledReqNo;
	}

	public void setDisabledReqNo(boolean disabledReqNo) {
		this.disabledReqNo = disabledReqNo;
	}

	public boolean isRenderClear() {
		return renderClear;
	}

	public void setRenderClear(boolean renderClear) {
		this.renderClear = renderClear;
	}

	public boolean isRenderBack() {
		return renderBack;
	}

	public void setRenderBack(boolean renderBack) {
		this.renderBack = renderBack;
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

	public boolean isDisableTab03() {
		return disableTab03;
	}

	public void setDisableTab03(boolean disableTab03) {
		this.disableTab03 = disableTab03;
	}

	public boolean isDisableBankInfo() {
		return disableBankInfo;
	}

	public void setDisableBankInfo(boolean disableBankInfo) {
		this.disableBankInfo = disableBankInfo;
	}

	public List<CommonDTO> getDrpdSchoolProvincelList() {
		return drpdSchoolProvincelList;
	}

	public void setDrpdSchoolProvincelList(List<CommonDTO> drpdSchoolProvincelList) {
		this.drpdSchoolProvincelList = drpdSchoolProvincelList;
	}

	public List<CommonDTO> getDrpdSchoolDistrictList() {
		return drpdSchoolDistrictList;
	}

	public void setDrpdSchoolDistrictList(List<CommonDTO> drpdSchoolDistrictList) {
		this.drpdSchoolDistrictList = drpdSchoolDistrictList;
	}

	public List<CommonDTO> getDrpdSchoolDevsecList() {
		return drpdSchoolDevsecList;
	}

	public void setDrpdSchoolDevsecList(List<CommonDTO> drpdSchoolDevsecList) {
		this.drpdSchoolDevsecList = drpdSchoolDevsecList;
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

	public boolean isRenderView() {
		return renderView;
	}

	public void setRenderView(boolean renderView) {
		this.renderView = renderView;
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

	public boolean isPermissionCreate() {
		return permissionCreate;
	}

	public void setPermissionCreate(boolean permissionCreate) {
		this.permissionCreate = permissionCreate;
	}

	public boolean isPermissionEdit() {
		return permissionEdit;
	}

	public void setPermissionEdit(boolean permissionEdit) {
		this.permissionEdit = permissionEdit;
	}

	public boolean isPermissionView() {
		return permissionView;
	}

	public void setPermissionView(boolean permissionView) {
		this.permissionView = permissionView;
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

	public GenerateReceiptDTO getBranchDTO() {
		return branchDTO;
	}

	public void setBranchDTO(GenerateReceiptDTO branchDTO) {
		this.branchDTO = branchDTO;
	}

}
