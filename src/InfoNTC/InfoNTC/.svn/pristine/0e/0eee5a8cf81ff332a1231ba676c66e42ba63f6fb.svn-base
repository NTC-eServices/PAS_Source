package lk.informatics.ntc.view.beans;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
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
import javax.faces.event.AjaxBehaviorEvent;

import org.apache.commons.io.FileUtils;
import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.AssignModelDTO;
import lk.informatics.ntc.model.dto.BusOwnerDTO;
import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.DocumentManagementDTO;
import lk.informatics.ntc.model.dto.OminiBusDTO;
import lk.informatics.ntc.model.dto.PermitDTO;
import lk.informatics.ntc.model.dto.PermitPaymentDTO;
import lk.informatics.ntc.model.dto.RouteDTO;
import lk.informatics.ntc.model.exception.ApplicationException;
import lk.informatics.ntc.model.service.AdminService;
import lk.informatics.ntc.model.service.DocumentManagementService;
import lk.informatics.ntc.model.service.EmployeeProfileService;
import lk.informatics.ntc.model.service.MigratedService;
import lk.informatics.ntc.view.util.PropertyReader;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "backlogPermitBackingBean")
@ViewScoped
public class BacklogPermitBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	// Services
	private AdminService adminService;
	private MigratedService migratedService;
	private EmployeeProfileService employeeProfileService;
	private DocumentManagementService documentManagementService;
	// DTOs
	private List<RouteDTO> routeList = new ArrayList<RouteDTO>();
	private List<CommonDTO> routefordropdownList;
	private PermitDTO permitDTO;
	private BusOwnerDTO busOwnerDTO;
	private OminiBusDTO ominiBusDTO;
	private PermitPaymentDTO permitPaymentDTO;
	private RouteDTO routeDTO;
	private List<CommonDTO> serviceTypedropdownList;
	private List<CommonDTO> titleList;
	private List<CommonDTO> genderList;
	private List<CommonDTO> martialList;
	private List<CommonDTO> provincelList;
	private List<CommonDTO> districtList;
	private List<CommonDTO> divSecList;
	private List<CommonDTO> makeList;
	private List<CommonDTO> modelList;

	// SelectedValues
	private String strSelectedRoute;
	private String strSelectedVal;
	private String strSelectedServiceType;
	private String strSelectedTitle;
	private String strSelectedGender;
	private String strSelectedMartial;
	private String strSelectedProvince;
	private String strSelectedDistrict;
	private String strSelectedDivSec;
	private String strSelectedLanguage;
	private String strSelectedMake;
	private String strSelectedModel;
	private BigDecimal calTot, busFare;
	private int progress = 0;
	private int pollValue = 0;

	private Date date;
	private String errorMsg;
	private Boolean boolEmpAddDet;
	private Boolean booldisable;
	private Boolean bisablebutton;
	private Boolean checkValiationsForInputFields = false;
	private boolean disabledModelsMenu = true;
	private boolean disabledModelsPopUpBtn = false;
	private boolean pollValueShow;

	private boolean disabledGender = false;
	private boolean disabledDOB = false;
	static boolean renameDone;

	private Boolean routeFlag, readOnlyFare;

	private List<DocumentManagementDTO> optionalList = new ArrayList<DocumentManagementDTO>();

	private AssignModelDTO assignModelDTO = new AssignModelDTO();

	@PostConstruct
	public void init() {
		permitDTO = new PermitDTO();
		busOwnerDTO = new BusOwnerDTO();
		ominiBusDTO = new OminiBusDTO();
		permitPaymentDTO = new PermitPaymentDTO();
		LoadValues();
		booldisable = true;
		bisablebutton = false;
		routeFlag = false;
		permitDTO.setRouteFlag("N");

	}

	// Methods
	public void LoadValues() {
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");
		migratedService = (MigratedService) SpringApplicationContex.getBean("migratedService");
		employeeProfileService = (EmployeeProfileService) SpringApplicationContex.getBean("employeeProfileService");
		documentManagementService = (DocumentManagementService) SpringApplicationContex
				.getBean("documentManagementService");

		routefordropdownList = adminService.getRoutesToDropdown();
		serviceTypedropdownList = adminService.getServiceTypeToDropdown();
		titleList = employeeProfileService.GetTitleToDropdown();
		genderList = employeeProfileService.GetGenderToDropdown();
		martialList = employeeProfileService.GetMartialToDropdown();

		provincelList = adminService.getProvinceToDropdown();
		districtList = adminService.getDistrictToDropdown();
		divSecList = adminService.getDivSecToDropdown();

		makeList = adminService.getMakesToDropdown();
		modelList = adminService.getModelsToDropdown();
	}

	public void searchPermitDetails() {
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");
		permitDTO = adminService.searchByPermitNo(sessionBackingBean.getPermitNo());
		if (permitDTO.getBusRegNo() != null && !permitDTO.getBusRegNo().isEmpty()
				&& !permitDTO.getBusRegNo().equalsIgnoreCase("")) {
			booldisable = false;
			strSelectedRoute = permitDTO.getRouteNo();
			strSelectedServiceType = permitDTO.getServiceType();
			strSelectedVal = permitDTO.getIsTenderPermit();

			getOwnerDetails();
			getOminiBusDetails();
			getPaymentDetails();
		} else {
			RequestContext.getCurrentInstance().update("frmnoDataMsge");
			RequestContext.getCurrentInstance().execute("PF('noDataMsge').show()");
		}
	}

	// 1st Tab
	public void mainClear() {
		strSelectedRoute = null;
		strSelectedServiceType = null;
		strSelectedVal = null;
		strSelectedTitle = null;
		strSelectedGender = null;
		strSelectedMartial = null;
		strSelectedProvince = null;
		strSelectedDistrict = null;
		strSelectedDivSec = null;
		strSelectedLanguage = null;
		strSelectedMake = null;
		strSelectedModel = null;
		permitDTO = new PermitDTO();
		init();
	}

	public void onRouteChange() {

		if (strSelectedRoute != null && !strSelectedRoute.equals("")) {
			routeDTO = adminService.getDetailsbyRouteNo(strSelectedRoute);
			if (routeDTO.getOrigin() != null && !routeDTO.getOrigin().equals("")) {
				permitDTO.setVia(routeDTO.getVia());
				permitDTO.setDestination(routeDTO.getDestination());
				permitDTO.setOrigin(routeDTO.getOrigin());
			}
		} else {
			routeDTO = null;
		}
		routeFlag = false;

	}

	public void onChangeTitle() {

		String titleCode = adminService.getParaCodeForTitle();

		if (strSelectedTitle.equals(titleCode)) {
			setDisabledDOB(true);
			setDisabledGender(true);
			setCheckValiationsForInputFields(true);
		} else {
			setDisabledDOB(false);
			setDisabledGender(false);
			setCheckValiationsForInputFields(false);
		}
	}

	public void completeBusFare() {

		busFare = new BigDecimal(0);
		busFare = adminService.completeBusFare(strSelectedRoute);

		if (busFare != null) {

			permitDTO.setBusFare(busFare);
			readOnlyFare = true;
		} else {
			readOnlyFare = false;
			permitDTO.setBusFare(null);
		}

	}

	public boolean routeFlagListener() {
		String location1;
		String location2;
		if (routeFlag) {
			location1 = permitDTO.getOrigin();
			location2 = permitDTO.getDestination();
			permitDTO.setOrigin(location2);
			permitDTO.setDestination(location1);
			permitDTO.setRouteFlag("Y");
			return false;
		} else {
			location1 = permitDTO.getOrigin();
			location2 = permitDTO.getDestination();
			permitDTO.setOrigin(location2);
			permitDTO.setDestination(location1);
			permitDTO.setRouteFlag("N");
			return true;
		}

	}

	public void savePermit() {

		if (permitDTO.getPermitNo() != null && !permitDTO.getPermitNo().isEmpty()
				&& !permitDTO.getPermitNo().equalsIgnoreCase("")) {
			// check duplicates
			String chkDuplicates = adminService.checkDuplicatePermitNo(permitDTO.getPermitNo());
			if (chkDuplicates == null) {
				if (permitDTO.getBusRegNo() != null && !permitDTO.getBusRegNo().isEmpty()
						&& !permitDTO.getBusRegNo().equalsIgnoreCase("")) {
					String chkVehi = adminService.checkVehiNo(permitDTO.getBusRegNo());

					if (chkVehi == null) {
						if (strSelectedServiceType != null && !strSelectedServiceType.isEmpty()
								&& !strSelectedServiceType.equalsIgnoreCase("")) {
							if (strSelectedRoute != null && !strSelectedRoute.isEmpty()
									&& !strSelectedRoute.equalsIgnoreCase("")) {

								if (permitDTO.getPermitExpire() != null) {
									if (permitDTO.getBusFare() != null) {
										// save
										permitDTO.setRouteNo(strSelectedRoute);
										permitDTO.setServiceType(strSelectedServiceType);
										permitDTO.setIsTenderPermit(strSelectedVal);
										permitDTO.setIsBacklogApp("Y");
										permitDTO.setCreatedBy(sessionBackingBean.loginUser);
										permitDTO.setStatus("A");

										ominiBusDTO.setVehicleRegNo(permitDTO.getBusRegNo());

										int result = adminService.saveBacklogPermit(permitDTO);

										if (result == 0) {

											RequestContext.getCurrentInstance().update("frmPermitInfo");
											RequestContext.getCurrentInstance().execute("PF('successSve').show()");
											booldisable = false;
											permitDTO = adminService.searchByPermitNo(permitDTO.getPermitNo());

											documentManagementService.updateBacklogDocumentAppNo(
													permitDTO.getPermitNo(), permitDTO.getApplicationNo());
											documentManagementService.updateBacklogDocumentVersionAppNo(
													permitDTO.getPermitNo(), permitDTO.getApplicationNo());

											if (permitDTO.getRouteFlag().equalsIgnoreCase("Y")
													&& permitDTO.getRouteFlag() != null
													&& !permitDTO.getRouteFlag().trim().equals("")) {
												routeFlag = true;
												routeFlagListener();

											} else if (permitDTO.getRouteFlag().equalsIgnoreCase("N")
													&& permitDTO.getRouteFlag() != null
													&& !permitDTO.getRouteFlag().trim().equals("")) {

											}
											
											bisablebutton = true;

										} else {
											RequestContext.getCurrentInstance().execute("PF('generalError').show()");
										}

									} else {
										errorMsg = "Total Bus Fare should be entered.";
										RequestContext.getCurrentInstance().update("frmrequiredField");
										RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
									}
								} else {
									errorMsg = "Permit Expire Date should be selected.";
									RequestContext.getCurrentInstance().update("frmrequiredField");
									RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
								}

							} else {
								errorMsg = "Route No. should be selected.";
								RequestContext.getCurrentInstance().update("frmrequiredField");
								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
							}
						} else {
							errorMsg = "Service Type should be selected.";
							RequestContext.getCurrentInstance().update("frmrequiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
						}
					} else {
						// Entered Vehicle No is already allocated for Permit
						// No.
						errorMsg = "Entered Vehicle No. is already allocated for Permit No. : " + chkVehi;
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}
				}

				else {
					errorMsg = "Registration No. of the Bus should be entered.";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			}

			else {
				errorMsg = "Duplicate Permit No.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		} else {
			errorMsg = "Permit No. should be entered.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void documentManagement() {

		DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();
		try {
			sessionBackingBean.setApplicationNoForDoc(permitDTO.getApplicationNo());
			sessionBackingBean.setPermitRenewalPermitNo(permitDTO.getPermitNo());
			sessionBackingBean.setPermitRenewalTranstractionTypeDescription("BACKLOG PERMIT");
			sessionBackingBean.setGoToPermitRenewalUploadDocPopUp(true);

			uploaddocumentManagementDTO.setUpload_Permit(permitDTO.getPermitNo());
			uploaddocumentManagementDTO.setTransaction_Type("BACKLOG PERMIT");

			optionalList = documentManagementService.optionalDocs("10", permitDTO.getPermitNo());

			sessionBackingBean.newPermitMandatoryDocumentList = documentManagementService
					.newPermitMandatoryList(permitDTO.getPermitNo());
			sessionBackingBean.newPermitOptionalDocumentList = documentManagementService
					.newPermitOptionalList(permitDTO.getPermitNo());
			sessionBackingBean.permitRenewalMandatoryDocumentList = documentManagementService
					.permitRenewalMandatoryList(permitDTO.getPermitNo());
			sessionBackingBean.permitRenewalOptionalDocumentList = documentManagementService
					.permitRenewalOptionalList(permitDTO.getPermitNo());
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

			sessionBackingBean.tenderMandatoryDocumentList = documentManagementService
					.tenderMandatoryList(permitDTO.getPermitNo());
			sessionBackingBean.tenderOptionalDocumentList = documentManagementService
					.tenderOptionalList(permitDTO.getPermitNo());
			RequestContext.getCurrentInstance().execute("PF('uploadDocument').show()");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updatePermit() {
		if (strSelectedServiceType != null && !strSelectedServiceType.isEmpty()
				&& !strSelectedServiceType.equalsIgnoreCase("")) {
			if (strSelectedRoute != null && !strSelectedRoute.isEmpty() && !strSelectedRoute.equalsIgnoreCase("")) {
				if (permitDTO.getPermitEndDate() != null) {
					if (permitDTO.getPermitExpire() != null) {
						if (permitDTO.getBusFare() != null) {
							// update
							permitDTO.setRouteNo(strSelectedRoute);
							permitDTO.setServiceType(strSelectedServiceType);
							permitDTO.setIsTenderPermit(strSelectedVal);
							permitDTO.setIsBacklogApp("Y");
							permitDTO.setModifiedBy(sessionBackingBean.loginUser);
							permitDTO.setStatus("A");
							int result = adminService.updateBacklogPermit(permitDTO);

							if (result == 0) {
								RequestContext.getCurrentInstance().update("frmPermitInfo");
								RequestContext.getCurrentInstance().execute("PF('successSve').show()");
								booldisable = false;
								permitDTO = adminService.searchByPermitNo(permitDTO.getPermitNo());
							} else {
								RequestContext.getCurrentInstance().execute("PF('generalError').show()");
							}

						} else {
							errorMsg = "Total Bus Fare should be entered.";
							RequestContext.getCurrentInstance().update("frmrequiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
						}
					} else {
						errorMsg = "Permit Expire Date should be selected.";
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}
				} else {
					errorMsg = "End Date of the Permit should be selected.";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			} else {
				errorMsg = "Route No. should be selected.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		} else {
			errorMsg = "Service Type should be selected.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	// 2nd Tab
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

	public void getOwnerDetails() {
		if (permitDTO.getPermitNo() != null && !permitDTO.getPermitNo().isEmpty() && permitDTO.getPermitNo() != "") {
			busOwnerDTO = adminService.ownerDetailsByPermitNo(permitDTO.getPermitNo());
			if (busOwnerDTO.getTitle() != null && busOwnerDTO.getTitle() != " " && !busOwnerDTO.getTitle().isEmpty()) {
				strSelectedTitle = busOwnerDTO.getTitle();
				strSelectedMartial = busOwnerDTO.getMaritalStatus();
				strSelectedGender = busOwnerDTO.getGender();
				strSelectedDivSec = busOwnerDTO.getDivSec();
				strSelectedDistrict = busOwnerDTO.getDistrict();
				strSelectedProvince = busOwnerDTO.getProvince();
				strSelectedLanguage = busOwnerDTO.getPerferedLanguage();
			}
		}
	}

	public void busOwnerClear() {
		strSelectedTitle = null;
		strSelectedMartial = null;
		strSelectedGender = null;
		strSelectedDivSec = null;
		strSelectedDistrict = null;
		strSelectedProvince = null;
		strSelectedLanguage = null;
		busOwnerDTO = new BusOwnerDTO();
		getOwnerDetails();
		setDisabledDOB(false);
		setDisabledGender(false);
	}

	public void busOwnerSave() {
		if (checkValiationsForInputFields == false) {
			if (strSelectedTitle != null && !strSelectedTitle.isEmpty() && !strSelectedTitle.equalsIgnoreCase("")) {
				if (strSelectedGender != null && !strSelectedGender.isEmpty()
						&& !strSelectedGender.equalsIgnoreCase("")) {
					if (busOwnerDTO.getDob() != null) {
						if (busOwnerDTO.getNameWithInitials() != null && !busOwnerDTO.getNameWithInitials().isEmpty()
								&& !busOwnerDTO.getNameWithInitials().equalsIgnoreCase("")) {
							if (busOwnerDTO.getFullName() != null && !busOwnerDTO.getFullName().isEmpty()
									&& !busOwnerDTO.getFullName().equalsIgnoreCase("")) {

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
																&& !busOwnerDTO.getFullNameSinhala()
																		.equalsIgnoreCase("")) {
															if (busOwnerDTO.getAddress1Sinhala() != null
																	&& !busOwnerDTO.getAddress1Sinhala().isEmpty()
																	&& !busOwnerDTO.getAddress1Sinhala()
																			.equalsIgnoreCase("")) {
																if (busOwnerDTO.getAddress2Sinhala() != null
																		&& !busOwnerDTO.getAddress2Sinhala().isEmpty()
																		&& !busOwnerDTO.getAddress2Sinhala()
																				.equalsIgnoreCase("")) {
																	if (busOwnerDTO.getAddress2Sinhala() != null
																			&& !busOwnerDTO.getAddress2Sinhala()
																					.isEmpty()
																			&& !busOwnerDTO.getAddress2Sinhala()
																					.equalsIgnoreCase("")) {
																		if (busOwnerDTO.getSeq() != 0) {
																			// Update
																			updateOwner();
																		} else {
																			// save
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
																	errorMsg = "Address Line 2 (Sinhala) should be entered.";
																	RequestContext.getCurrentInstance()
																			.update("frmrequiredField");
																	RequestContext.getCurrentInstance()
																			.execute("PF('requiredField').show()");
																}
															} else {
																errorMsg = "Address Line 1 (Sinhala) should be entered.";
																RequestContext.getCurrentInstance()
																		.update("frmrequiredField");
																RequestContext.getCurrentInstance()
																		.execute("PF('requiredField').show()");
															}
														} else {
															errorMsg = "Name in Full (Sinhala) should be entered.";
															RequestContext.getCurrentInstance()
																	.update("frmrequiredField");
															RequestContext.getCurrentInstance()
																	.execute("PF('requiredField').show()");
														}
													} else if (strSelectedLanguage.equals("T")) {
														if (busOwnerDTO.getFullNameTamil() != null
																&& !busOwnerDTO.getFullNameTamil().isEmpty()
																&& !busOwnerDTO.getFullNameTamil()
																		.equalsIgnoreCase("")) {
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
																	errorMsg = "Address Line 2 (Tamil) should be entered.";
																	RequestContext.getCurrentInstance()
																			.update("frmrequiredField");
																	RequestContext.getCurrentInstance()
																			.execute("PF('requiredField').show()");
																}
															} else {
																errorMsg = "Address Line 1 (Tamil) should be entered.";
																RequestContext.getCurrentInstance()
																		.update("frmrequiredField");
																RequestContext.getCurrentInstance()
																		.execute("PF('requiredField').show()");
															}
														} else {
															errorMsg = "Name in Full (Tamil) should be entered.";
															RequestContext.getCurrentInstance()
																	.update("frmrequiredField");
															RequestContext.getCurrentInstance()
																	.execute("PF('requiredField').show()");
														}
													} else {
														if (busOwnerDTO.getSeq() != 0) {
															// Update
															updateOwner();
														} else {
															// save
															saveOwner();
														}
													}

												} else {
													errorMsg = "NIC/Org.Reg. No. should be entered.";
													RequestContext.getCurrentInstance().update("frmrequiredField");
													RequestContext.getCurrentInstance()
															.execute("PF('requiredField').show()");
												}
											} else {
												errorMsg = "Province should be selected.";
												RequestContext.getCurrentInstance().update("frmrequiredField");
												RequestContext.getCurrentInstance()
														.execute("PF('requiredField').show()");
											}
										} else {
											errorMsg = "City should be entered.";
											RequestContext.getCurrentInstance().update("frmrequiredField");
											RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
										}
									} else {
										errorMsg = "Address Line 2 should be entered.";
										RequestContext.getCurrentInstance().update("frmrequiredField");
										RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
									}
								} else {
									errorMsg = "Address Line 1 should be entered.";
									RequestContext.getCurrentInstance().update("frmrequiredField");
									RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
								}

							} else {
								errorMsg = "Full Name should be entered.";
								RequestContext.getCurrentInstance().update("frmrequiredField");
								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
							}
						} else {
							errorMsg = "Name with Initial should be entered.";
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
		} else if (checkValiationsForInputFields == true) {
			if (strSelectedTitle != null && !strSelectedTitle.isEmpty() && !strSelectedTitle.equalsIgnoreCase("")) {
				if (busOwnerDTO.getNameWithInitials() != null && !busOwnerDTO.getNameWithInitials().isEmpty()
						&& !busOwnerDTO.getNameWithInitials().equalsIgnoreCase("")) {
					if (busOwnerDTO.getFullName() != null && !busOwnerDTO.getFullName().isEmpty()
							&& !busOwnerDTO.getFullName().equalsIgnoreCase("")) {

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
															&& !busOwnerDTO.getAddress1Sinhala().equalsIgnoreCase("")) {
														if (busOwnerDTO.getAddress2Sinhala() != null
																&& !busOwnerDTO.getAddress2Sinhala().isEmpty()
																&& !busOwnerDTO.getAddress2Sinhala()
																		.equalsIgnoreCase("")) {
															if (busOwnerDTO.getAddress2Sinhala() != null
																	&& !busOwnerDTO.getAddress2Sinhala().isEmpty()
																	&& !busOwnerDTO.getAddress2Sinhala()
																			.equalsIgnoreCase("")) {
																if (busOwnerDTO.getSeq() != 0) {
																	// Update
																	updateOwner();
																} else {
																	// save
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
															errorMsg = "Address Line 2 (Sinhala) should be entered.";
															RequestContext.getCurrentInstance()
																	.update("frmrequiredField");
															RequestContext.getCurrentInstance()
																	.execute("PF('requiredField').show()");
														}
													} else {
														errorMsg = "Address Line 1 (Sinhala) should be entered.";
														RequestContext.getCurrentInstance().update("frmrequiredField");
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
															&& !busOwnerDTO.getAddress1Tamil().equalsIgnoreCase("")) {
														if (busOwnerDTO.getAddress2Tamil() != null
																&& !busOwnerDTO.getAddress2Tamil().isEmpty()
																&& !busOwnerDTO.getAddress2Tamil()
																		.equalsIgnoreCase("")) {
															if (busOwnerDTO.getCity() != null
																	&& !busOwnerDTO.getCity().isEmpty()
																	&& !busOwnerDTO.getCity().equalsIgnoreCase("")) {
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
															errorMsg = "Address Line 2 (Tamil) should be entered.";
															RequestContext.getCurrentInstance()
																	.update("frmrequiredField");
															RequestContext.getCurrentInstance()
																	.execute("PF('requiredField').show()");
														}
													} else {
														errorMsg = "Address Line 1 (Tamil) should be entered.";
														RequestContext.getCurrentInstance().update("frmrequiredField");
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
													// Update
													updateOwner();
												} else {
													// save
													saveOwner();
												}
											}

										} else {
											errorMsg = "NIC/Org.Reg. No. should be entered.";
											RequestContext.getCurrentInstance().update("frmrequiredField");
											RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
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
								errorMsg = "Address Line 2 should be entered.";
								RequestContext.getCurrentInstance().update("frmrequiredField");
								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
							}
						} else {
							errorMsg = "Address Line 1 should be entered.";
							RequestContext.getCurrentInstance().update("frmrequiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
						}

					} else {
						errorMsg = "Full Name should be entered.";
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}
				} else {
					errorMsg = "Name with Initial should be entered.";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			} else {
				errorMsg = "Title should be selected.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		}
	}

	private void saveOwner() {
		busOwnerDTO.setTitle(strSelectedTitle);
		busOwnerDTO.setGender(strSelectedGender);
		busOwnerDTO.setPerferedLanguage(strSelectedLanguage);
		busOwnerDTO.setProvince(strSelectedProvince);
		busOwnerDTO.setDistrict(strSelectedDistrict);
		busOwnerDTO.setDivSec(strSelectedDivSec);
		busOwnerDTO.setApplicationNo(permitDTO.getApplicationNo());
		busOwnerDTO.setPermitNo(permitDTO.getPermitNo());
		busOwnerDTO.setBusRegNo(permitDTO.getBusRegNo());
		busOwnerDTO.setCreatedBy(sessionBackingBean.loginUser);
		busOwnerDTO.setIsBacklogApp("Y");

		int result = adminService.saveBusOwnerDetails(busOwnerDTO);

		if (result == 0) {
			getOwnerDetails();
			RequestContext.getCurrentInstance().update("frmBusOwnerInfo");
			RequestContext.getCurrentInstance().execute("PF('successSve').show()");

		} else {
			RequestContext.getCurrentInstance().execute("PF('generalError').show()");
		}
	}

	private void updateOwner() {
		busOwnerDTO.setTitle(strSelectedTitle);
		busOwnerDTO.setGender(strSelectedGender);
		busOwnerDTO.setPerferedLanguage(strSelectedLanguage);
		busOwnerDTO.setProvince(strSelectedProvince);
		busOwnerDTO.setDistrict(strSelectedDistrict);
		busOwnerDTO.setDivSec(strSelectedDivSec);
		busOwnerDTO.setApplicationNo(permitDTO.getApplicationNo());
		busOwnerDTO.setPermitNo(permitDTO.getPermitNo());
		busOwnerDTO.setBusRegNo(permitDTO.getBusRegNo());
		busOwnerDTO.setModifiedBy(sessionBackingBean.loginUser);
		busOwnerDTO.setIsBacklogApp("Y");

		int result = adminService.updateBusOwner(busOwnerDTO);

		if (result == 0) {
			getOwnerDetails();
			RequestContext.getCurrentInstance().update("frmBusOwnerInfo");
			RequestContext.getCurrentInstance().execute("PF('successSve').show()");

		} else {
			RequestContext.getCurrentInstance().execute("PF('generalError').show()");
		}
	}

	// 3rd Tab
	public void onMakeChange(final AjaxBehaviorEvent event) {
		if (strSelectedMake != null && !strSelectedMake.isEmpty()) {
			modelList = adminService.getModelsByMakeToDropdown(strSelectedMake);
			setDisabledModelsMenu(false);
			setDisabledModelsPopUpBtn(true);
		} else {
			setStrSelectedModel(null);
			setDisabledModelsMenu(true);
			setDisabledModelsPopUpBtn(false);
		}
	}

	public void ominiBusClear() {
		String busReg = ominiBusDTO.getVehicleRegNo();

		strSelectedMake = null;
		strSelectedModel = null;
		ominiBusDTO = new OminiBusDTO();
		getOminiBusDetails();
		ominiBusDTO.setVehicleRegNo(busReg);
		setDisabledModelsMenu(true);
		setDisabledModelsPopUpBtn(false);
	}

	public void ominiBusSave() {
		if (ominiBusDTO.getVehicleRegNo() != null && !ominiBusDTO.getVehicleRegNo().isEmpty()
				&& !ominiBusDTO.getVehicleRegNo().equalsIgnoreCase("")) {
			if (ominiBusDTO.getVehicleRegNo().equals(permitDTO.getBusRegNo())) {
				if (ominiBusDTO.getSeating() != null && !ominiBusDTO.getSeating().isEmpty()
						&& !ominiBusDTO.getSeating().equalsIgnoreCase("")) {
					if (ominiBusDTO.getNoofDoors() != null && !ominiBusDTO.getNoofDoors().isEmpty()
							&& !ominiBusDTO.getNoofDoors().equalsIgnoreCase("")) {
						if (ominiBusDTO.getWeight() != null && !ominiBusDTO.getWeight().isEmpty()
								&& !ominiBusDTO.getWeight().equalsIgnoreCase("")) {
							if (strSelectedMake != null && !strSelectedMake.isEmpty()
									&& !strSelectedMake.equalsIgnoreCase("")) {
								if (strSelectedModel != null && !strSelectedModel.isEmpty()
										&& !strSelectedModel.equalsIgnoreCase("")) {
									if (ominiBusDTO.getSeq() != 0) {
										// update
										ominiBusDTO.setModifiedBy(sessionBackingBean.getLoginUser());
										ominiBusDTO.setMake(strSelectedMake);
										ominiBusDTO.setModel(strSelectedModel);
										ominiBusDTO.setApplicationNo(permitDTO.getApplicationNo());
										ominiBusDTO.setPermitNo(permitDTO.getPermitNo());

										int result = adminService.updateOminiBus(ominiBusDTO);

										if (result == 0) {
											getOminiBusDetails();
											RequestContext.getCurrentInstance().update("frmOminiBusInfo");
											RequestContext.getCurrentInstance().execute("PF('successSve').show()");

										} else {
											RequestContext.getCurrentInstance().execute("PF('generalError').show()");
										}
									} else {
										// Save
										ominiBusDTO.setIsBacklogApp("Y");
										ominiBusDTO.setCreatedBy(sessionBackingBean.getLoginUser());
										ominiBusDTO.setMake(strSelectedMake);
										ominiBusDTO.setModel(strSelectedModel);
										ominiBusDTO.setApplicationNo(permitDTO.getApplicationNo());
										ominiBusDTO.setPermitNo(permitDTO.getPermitNo());

										int result = adminService.saveBacklogOminiBus(ominiBusDTO);

										if (result == 0) {
											getOminiBusDetails();
											RequestContext.getCurrentInstance().update("frmOminiBusInfo");
											RequestContext.getCurrentInstance().execute("PF('successSve').show()");

										} else {
											RequestContext.getCurrentInstance().execute("PF('generalError').show()");
										}
									}
								} else {
									errorMsg = "Model should be entered.";
									RequestContext.getCurrentInstance().update("frmrequiredField");
									RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
								}

							} else {
								errorMsg = "Make should be entered.";
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
					errorMsg = "Seating Capacity should be entered.";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			} else {
				errorMsg = "Entered Registration No. is mismatch with Permit Information.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		} else {
			errorMsg = "Registration No. of the Bus should be entered.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public void getOminiBusDetails() {
		if (permitDTO.getPermitNo() != null && !permitDTO.getPermitNo().isEmpty() && permitDTO.getPermitNo() != "") {
			ominiBusDTO = adminService.ominiBusByVehicleNo(permitDTO.getBusRegNo());
			if (ominiBusDTO.getVehicleRegNo() != null && ominiBusDTO.getVehicleRegNo() != " "
					&& !ominiBusDTO.getVehicleRegNo().isEmpty()) {
				strSelectedModel = ominiBusDTO.getModel();
				strSelectedMake = ominiBusDTO.getMake();
			}
		}
	}

	// 4th Tab
	public void paymentClear() {
		permitPaymentDTO = new PermitPaymentDTO();
		getPaymentDetails();
	}

	public void paymentSave() {
		if (permitPaymentDTO.getSeq() != 0) {
			// update
			permitPaymentDTO.setModifiedBy(sessionBackingBean.getLoginUser());

			int result = adminService.updateBacklogPayments(permitPaymentDTO);

			if (result == 0) {
				getPaymentDetails();
				RequestContext.getCurrentInstance().update("frmPaymentInfo");
				RequestContext.getCurrentInstance().execute("PF('successSve').show()");

			} else {
				RequestContext.getCurrentInstance().execute("PF('generalError').show()");
			}
		} else {
			// save
			permitPaymentDTO.setIsBacklogApp("Y");
			permitPaymentDTO.setCreatedBy(sessionBackingBean.getLoginUser());
			permitPaymentDTO.setPermitNo(permitDTO.getPermitNo());
			permitPaymentDTO.setApplicationNo(permitDTO.getApplicationNo());
			permitPaymentDTO.setVehicleRegNo(permitDTO.getBusRegNo());

			int result = adminService.saveBacklogPayments(permitPaymentDTO);

			if (result == 0) {
				getPaymentDetails();
				RequestContext.getCurrentInstance().update("frmPaymentInfo");
				RequestContext.getCurrentInstance().execute("PF('successSve').show()");

			} else {
				RequestContext.getCurrentInstance().execute("PF('generalError').show()");
			}
		}

	}

	public void getPaymentDetails() {
		if (permitDTO.getPermitNo() != null && !permitDTO.getPermitNo().isEmpty() && permitDTO.getPermitNo() != "") {
			permitPaymentDTO = adminService.paymentBypermitNo(permitDTO.getPermitNo());
		}
	}

	public void calculateTotal() {
		BigDecimal ren1 = null;
		BigDecimal penal1 = null;
		BigDecimal tender1 = null;
		BigDecimal ser1 = null;
		BigDecimal oth1 = null;
		BigDecimal sum = null;

		BigDecimal d = new BigDecimal(0);
		if (permitPaymentDTO.getRenewalAmt() != null)
			ren1 = permitPaymentDTO.getRenewalAmt();
		else
			ren1 = new BigDecimal(0);
		if (permitPaymentDTO.getPenaltyAmt() != null)
			penal1 = permitPaymentDTO.getPenaltyAmt();
		else
			penal1 = new BigDecimal(0);
		if (permitPaymentDTO.getTenderFee() != null)
			tender1 = permitPaymentDTO.getTenderFee();
		else
			tender1 = new BigDecimal(0);
		if (permitPaymentDTO.getServiceFee() != null)
			ser1 = permitPaymentDTO.getServiceFee();
		else
			ser1 = new BigDecimal(0);
		if (permitPaymentDTO.getOtherFee() != null)
			oth1 = permitPaymentDTO.getOtherFee();
		else
			oth1 = new BigDecimal(0);

		sum = d.add(ren1).add(penal1).add(tender1).add(ser1).add(oth1);
		permitPaymentDTO.setTotalFee(sum);
	}

	public void routeMaintenance() {
		RequestContext.getCurrentInstance().execute("PF('routeMaintenance').show()");
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	public List<RouteDTO> getRouteList() {
		return routeList;
	}

	public void setRouteList(List<RouteDTO> routeList) {
		this.routeList = routeList;
	}

	public String getStrSelectedRoute() {
		return strSelectedRoute;
	}

	public void setStrSelectedRoute(String strSelectedRoute) {
		this.strSelectedRoute = strSelectedRoute;
	}

	public List<CommonDTO> getRoutefordropdownList() {
		return routefordropdownList;
	}

	public void setRoutefordropdownList(List<CommonDTO> routefordropdownList) {
		this.routefordropdownList = routefordropdownList;
	}

	public PermitDTO getPermitDTO() {
		return permitDTO;
	}

	public void setPermitDTO(PermitDTO permitDTO) {
		this.permitDTO = permitDTO;
	}

	public String getStrSelectedVal() {
		return strSelectedVal;
	}

	public void setStrSelectedVal(String strSelectedVal) {
		this.strSelectedVal = strSelectedVal;
	}

	public RouteDTO getRouteDTO() {
		return routeDTO;
	}

	public void setRouteDTO(RouteDTO routeDTO) {
		this.routeDTO = routeDTO;
	}

	public List<CommonDTO> getServiceTypedropdownList() {
		return serviceTypedropdownList;
	}

	public void setServiceTypedropdownList(List<CommonDTO> serviceTypedropdownList) {
		this.serviceTypedropdownList = serviceTypedropdownList;
	}

	public String getStrSelectedServiceType() {
		return strSelectedServiceType;
	}

	public void setStrSelectedServiceType(String strSelectedServiceType) {
		this.strSelectedServiceType = strSelectedServiceType;
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

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Boolean getBoolEmpAddDet() {
		return boolEmpAddDet;
	}

	public void setBoolEmpAddDet(Boolean boolEmpAddDet) {
		this.boolEmpAddDet = boolEmpAddDet;
	}

	public Boolean getBooldisable() {
		return booldisable;
	}

	public void setBooldisable(Boolean booldisable) {
		this.booldisable = booldisable;
	}

	public EmployeeProfileService getEmployeeProfileService() {
		return employeeProfileService;
	}

	public void setEmployeeProfileService(EmployeeProfileService employeeProfileService) {
		this.employeeProfileService = employeeProfileService;
	}

	public BusOwnerDTO getBusOwnerDTO() {
		return busOwnerDTO;
	}

	public void setBusOwnerDTO(BusOwnerDTO busOwnerDTO) {
		this.busOwnerDTO = busOwnerDTO;
	}

	public String getStrSelectedLanguage() {
		return strSelectedLanguage;
	}

	public void setStrSelectedLanguage(String strSelectedLanguage) {
		this.strSelectedLanguage = strSelectedLanguage;
	}

	public List<CommonDTO> getMakeList() {
		return makeList;
	}

	public void setMakeList(List<CommonDTO> makeList) {
		this.makeList = makeList;
	}

	public List<CommonDTO> getModelList() {
		return modelList;
	}

	public void setModelList(List<CommonDTO> modelList) {
		this.modelList = modelList;
	}

	public String getStrSelectedMake() {
		return strSelectedMake;
	}

	public void setStrSelectedMake(String strSelectedMake) {
		this.strSelectedMake = strSelectedMake;
	}

	public String getStrSelectedModel() {
		return strSelectedModel;
	}

	public void setStrSelectedModel(String strSelectedModel) {
		this.strSelectedModel = strSelectedModel;
	}

	public OminiBusDTO getOminiBusDTO() {
		return ominiBusDTO;
	}

	public void setOminiBusDTO(OminiBusDTO ominiBusDTO) {
		this.ominiBusDTO = ominiBusDTO;
	}

	public PermitPaymentDTO getPermitPaymentDTO() {
		return permitPaymentDTO;
	}

	public void setPermitPaymentDTO(PermitPaymentDTO permitPaymentDTO) {
		this.permitPaymentDTO = permitPaymentDTO;
	}

	public BigDecimal getCalTot() {
		return calTot;
	}

	public void setCalTot(BigDecimal calTot) {
		this.calTot = calTot;
	}

	public Boolean getBisablebutton() {
		return bisablebutton;
	}

	public void setBisablebutton(Boolean bisablebutton) {
		this.bisablebutton = bisablebutton;
	}

	public void totalBusFareValidation() {

		if (permitDTO.getBusFare() != null && permitDTO.getBusFare().signum() < 0) {
			errorMsg = "Invalid Total Bus Fare";
			RequestContext.getCurrentInstance().update("frmerrorMsge");
			RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
			permitDTO.setBusFare(null);
		}

	}

	public void seatingCapacityValidation() {

		if (ominiBusDTO.getSeating() != null && ominiBusDTO.getSeating().charAt(0) == '-') {
			errorMsg = "Invalid Seating Capacity";
			RequestContext.getCurrentInstance().update("frmerrorMsge");
			RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
			ominiBusDTO.setSeating(null);

		}

	}

	public void noOfDoorsValidation() {

		if (ominiBusDTO.getNoofDoors() != null && ominiBusDTO.getNoofDoors().charAt(0) == '-') {
			errorMsg = "Invalid No. of Doors";
			RequestContext.getCurrentInstance().update("frmerrorMsge");
			RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
			ominiBusDTO.setNoofDoors(null);

		}

	}

	public void weightValidation() {

		if (ominiBusDTO.getWeight() != null && ominiBusDTO.getWeight().charAt(0) == '-') {
			errorMsg = "Invalid Weight";
			RequestContext.getCurrentInstance().update("frmerrorMsge");
			RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
			ominiBusDTO.setWeight(null);

		}

	}

	public void totalPermitAmountValidation() {

		if (permitPaymentDTO.getTotalPermitAmt().signum() < 0) {
			errorMsg = "Invalid Total Permit Amount";
			RequestContext.getCurrentInstance().update("frmerrorMsge");
			RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
			permitPaymentDTO.setTotalPermitAmt(null);
		}

	}

	public void excessAmountValidation() {

		if (permitPaymentDTO.getExcessAmt().signum() < 0) {
			errorMsg = "Invalid Excess Amount";
			RequestContext.getCurrentInstance().update("frmerrorMsge");
			RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
			permitPaymentDTO.setExcessAmt(null);
		}

	}

	public void renewalAmountValidation() {

		if (permitPaymentDTO.getRenewalAmt() != null && permitPaymentDTO.getRenewalAmt().signum() < 0) {
			errorMsg = "Invalid Renewal Fee";
			RequestContext.getCurrentInstance().update("frmerrorMsge");
			RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
			permitPaymentDTO.setRenewalAmt(null);
		}
		calculateTotal();
	}

	public void penaltyAmountValidation() {

		if (permitPaymentDTO.getPenaltyAmt() != null && permitPaymentDTO.getPenaltyAmt().signum() < 0) {
			errorMsg = "Invalid Penalty Fee";
			RequestContext.getCurrentInstance().update("frmerrorMsge");
			RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
			permitPaymentDTO.setPenaltyAmt(null);
		}
		calculateTotal();
	}

	public void tenderAmountValidation() {

		if (permitPaymentDTO.getTenderFee() != null && permitPaymentDTO.getTenderFee().signum() < 0) {
			errorMsg = "Invalid Tender Fee";
			RequestContext.getCurrentInstance().update("frmerrorMsge");
			RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
			permitPaymentDTO.setTenderFee(null);
		}
		calculateTotal();

	}

	public void serviceAmountValidation() {

		if (permitPaymentDTO.getServiceFee() != null && permitPaymentDTO.getServiceFee().signum() < 0) {
			errorMsg = "Invalid Service Fee";
			RequestContext.getCurrentInstance().update("frmerrorMsge");
			RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
			permitPaymentDTO.setServiceFee(null);
		}
		calculateTotal();

	}

	public void otherAmountValidation() {
		if (permitPaymentDTO.getOtherFee() != null && permitPaymentDTO.getOtherFee().signum() < 0) {
			errorMsg = "Invalid Other Fee";
			RequestContext.getCurrentInstance().update("frmerrorMsge");
			RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
			permitPaymentDTO.setOtherFee(null);
		}
		calculateTotal();
	}

	public void phoneNumberValidator() {
		if (busOwnerDTO.getTelephoneNo() != null && !busOwnerDTO.getTelephoneNo().isEmpty()) {
			Pattern ptr = Pattern.compile("^[0-9]{10}$");
			boolean valid = ptr.matcher(busOwnerDTO.getTelephoneNo()).matches();
			if (valid) {

			} else {
				errorMsg = "Invalid Telephone Number";
				RequestContext.getCurrentInstance().update("frmerrorMsge");
				RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
				busOwnerDTO.setTelephoneNo(null);
			}

		}
		if (busOwnerDTO.getMobileNo() != null && !busOwnerDTO.getMobileNo().isEmpty()) {
			Pattern ptr = Pattern.compile("^[0-9]{10}$");
			boolean valid = ptr.matcher(busOwnerDTO.getMobileNo()).matches();
			if (valid) {

			} else {
				errorMsg = "Invalid Mobile Number";
				RequestContext.getCurrentInstance().update("frmerrorMsge");
				RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
				busOwnerDTO.setMobileNo(null);
			}

		}
	}

	public void permitNumberValidator() {
		if (permitDTO.getPermitNo() != null && !permitDTO.getPermitNo().isEmpty()) {
			Pattern ptr = Pattern.compile("^[a-zA-Z0-9\\-]*$");
			boolean valid = ptr.matcher(permitDTO.getPermitNo()).matches();
			if (valid) {
				String chkDuplicates = adminService.checkDuplicatePermitNo(permitDTO.getPermitNo());
				if (chkDuplicates != null) {
					errorMsg = "You have already entered this Permit Number";
					RequestContext.getCurrentInstance().update("frmerrorMsge");
					RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
					permitDTO.setPermitNo(null);
				}
			} else {
				errorMsg = "Invalid Permit Number";
				RequestContext.getCurrentInstance().update("frmerrorMsge");
				RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
				permitDTO.setPermitNo(null);
			}

		}

	}

	public void busNoValidator() {
		if (permitDTO.getBusRegNo() != null && !permitDTO.getBusRegNo().isEmpty()) {
			String chkDuplicates = adminService.checkVehiNo(permitDTO.getBusRegNo());
			if (chkDuplicates != null) {
				errorMsg = "Duplicate Registration No. of the Bus";
				RequestContext.getCurrentInstance().update("frmerrorMsge");
				RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
				permitDTO.setBusRegNo(null);
			}
		}
	}

	public void endDateExpireDateValidator() {

		if (permitDTO.getPermitExpire() != null && permitDTO.getPermitEndDate() != null
				&& permitDTO.getPermitExpire().after(permitDTO.getPermitEndDate())) {

			errorMsg = "Permit Expire Date should be less than or same as End Date of the Permit";
			RequestContext.getCurrentInstance().update("frmerrorMsge");
			RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
			permitDTO.setPermitExpire(null);

		}
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
					RequestContext.getCurrentInstance().update("frmerrorMsge");
					RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
					ominiBusDTO.setManufactureDate(null);
				}

			} else {
				errorMsg = "Invalid Manufacture Year";
				RequestContext.getCurrentInstance().update("frmerrorMsge");
				RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
				ominiBusDTO.setManufactureDate(null);
			}

		}

		if (ominiBusDTO.getRegistrationDate() != null && ominiBusDTO.getManufactureDate() != null) {
			int regYear = ominiBusDTO.getRegistrationDate().getYear();
			regYear = regYear + 1900;
			int manuYear = Integer.parseInt(ominiBusDTO.getManufactureDate());

			if (manuYear > regYear) {
				errorMsg = "Date of Registration should be greater than or same as Manufacture Year";
				RequestContext.getCurrentInstance().update("frmerrorMsge");
				RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
				ominiBusDTO.setRegistrationDate(null);

			}

		}
	}

	public void goModelsPopUp() {
		AssignModelsBackingBean assignModelsBackingBean = new AssignModelsBackingBean();
		assignModelsBackingBean.clearOne();
		RequestContext.getCurrentInstance().update("frmcreateModels");
		RequestContext.getCurrentInstance().execute("PF('viewCreateModels').show()");
	}

	public void updateModelsMenu() {
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");
		modelList = adminService.getModelsToDropdown();

		for (int i = 0; i < modelList.size(); i++) {

		}
		RequestContext.getCurrentInstance().execute("PF('viewCreateModels').hide()");
		RequestContext.getCurrentInstance().update("tabviewId:tcOminiBusDet:frmOminiBusInfo:txtmodel");
	}

	private void clearPopup() {
		assignModelDTO.setMakeCode(null);
		assignModelDTO.setMakeDescription(null);
		assignModelDTO.setModelCode(null);
		assignModelDTO.setModelDescription(null);

		AssignModelsBackingBean assignModelsBackingBean = new AssignModelsBackingBean();
		assignModelsBackingBean.loadValues();

		RequestContext.getCurrentInstance().update("viewCreateModels");

	}

	public static void copyDocument(String permitNo, String DestPath) {

		String backlogfilepath = null;
		try {
			backlogfilepath = PropertyReader.getPropertyValue("documentManagement.backlog.file.path");// Path
																										// to
																										// move
																										// folders
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String path = backlogfilepath;

		String source = path + File.separator + permitNo;
		File srcDir = new File(source);

		String destination = DestPath;
		File destDir = new File(destination);

		try {
			FileUtils.copyDirectory(srcDir, destDir);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void moveDocuments() {

		String backlogfilepath = null;
		try {
			backlogfilepath = PropertyReader.getPropertyValue("documentManagement.backlog.file.path");// Path
																										// to
																										// move
																										// folders
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		File file = new File(backlogfilepath);// Path to NTC folders that
												// already exists
		File[] files = file.listFiles();

		for (File f : files) {

			progress++;

			String applicationNo = documentManagementService.getBacklogApplicationByPermitNo(f.getName());

			if (applicationNo != null) {

				String mainPath = null;

				String propertyFilePath = null;
				try {
					propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");// Path
																												// to
																												// move
																												// folders
				} catch (ApplicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String originalPath = propertyFilePath;

				String newCreatedPath = originalPath + File.separator + f.getName();

				File file1 = new File(newCreatedPath);
				if (!file1.exists()) {
					if (file1.mkdir()) {

					} else {

					}
				} else {

				}

				String optionalPath = newCreatedPath + File.separator + "Optional Documents";

				File file2 = new File(optionalPath);
				if (!file2.exists()) {
					if (file2.mkdir()) {

					} else {

					}
				} else {

				}

				String backlogpath = optionalPath + File.separator + "Backlog Management";

				File file3 = new File(backlogpath);
				if (!file3.exists()) {
					if (file3.mkdir()) {

					} else {

					}
				} else {

				}

				copyDocument(f.getName(), backlogpath);

				String ntcpath = backlogfilepath;
				String ntcPermitPath = ntcpath + File.separator + f.getName();

				File fileNtc = new File(ntcPermitPath);
				File[] filesNtc = fileNtc.listFiles();
				int i = 0;
				for (File fNtc : filesNtc) {
					i++;

					String str = fNtc.getName();
					String ext = str.substring(str.lastIndexOf('.'), str.length()).toLowerCase();
					String newDocName = "SCAN DOCUMENT_" + i + ext;

					String oldpath = backlogpath + File.separator + fNtc.getName();
					String newPath = backlogpath + File.separator + newDocName;

					mainPath = newPath;

					File oldName = new File(oldpath);
					File newName = new File(newPath);

					if (oldName.renameTo(newName)) {

						renameDone = true;
					} else {
						renameDone = false;

					}

					if (renameDone == true) {
						migratedService.moveDocumentsA(applicationNo, f.getName(), newPath, i);
					}

				}

				if (renameDone == true) {
					migratedService.moveDocumentsB(applicationNo, f.getName(), mainPath);
				}

			} else {
				migratedService.moveDocumentsC(f.getName());
			}

			RequestContext.getCurrentInstance().update("frmPaymentInfo");
			RequestContext.getCurrentInstance().execute("PF('successSve').show()");

		}

	}

	public void poll() {

		pollValue = progress;

	}

	public DocumentManagementService getDocumentManagementService() {
		return documentManagementService;
	}

	public void setDocumentManagementService(DocumentManagementService documentManagementService) {
		this.documentManagementService = documentManagementService;
	}

	public List<DocumentManagementDTO> getOptionalList() {
		return optionalList;
	}

	public void setOptionalList(List<DocumentManagementDTO> optionalList) {
		this.optionalList = optionalList;
	}

	public Boolean getRouteFlag() {
		return routeFlag;
	}

	public void setRouteFlag(Boolean routeFlag) {
		this.routeFlag = routeFlag;
	}

	public BigDecimal getBusFare() {
		return busFare;
	}

	public void setBusFare(BigDecimal busFare) {
		this.busFare = busFare;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Boolean getReadOnlyFare() {
		return readOnlyFare;
	}

	public void setReadOnlyFare(Boolean readOnlyFare) {
		this.readOnlyFare = readOnlyFare;
	}

	public boolean isDisabledGender() {
		return disabledGender;
	}

	public void setDisabledGender(boolean disabledGender) {
		this.disabledGender = disabledGender;
	}

	public boolean isDisabledDOB() {
		return disabledDOB;
	}

	public void setDisabledDOB(boolean disabledDOB) {
		this.disabledDOB = disabledDOB;
	}

	public void getDetailsByNicNo() {
		if (adminService.ownerDetailsByNicNo(busOwnerDTO.getNicNo()).getNicNo() != null) {
			busOwnerDTO = adminService.ownerDetailsByNicNo(busOwnerDTO.getNicNo());
			strSelectedTitle = busOwnerDTO.getTitle();
			strSelectedMartial = busOwnerDTO.getMaritalStatus();
			strSelectedGender = busOwnerDTO.getGender();
			strSelectedDivSec = busOwnerDTO.getDivSec();
			strSelectedDistrict = busOwnerDTO.getDistrict();
			strSelectedProvince = busOwnerDTO.getProvince();
			strSelectedLanguage = busOwnerDTO.getPerferedLanguage();
		} else {

			String nic = busOwnerDTO.getNicNo();
			busOwnerDTO = new BusOwnerDTO();
			busOwnerDTO.setNicNo(nic);
			setStrSelectedLanguage(null);
			setStrSelectedGender(null);
			setStrSelectedTitle(null);
			setStrSelectedProvince(null);
			setStrSelectedDistrict(null);
			setStrSelectedDivSec(null);

		}

	}

	public Boolean getCheckValiationsForInputFields() {
		return checkValiationsForInputFields;
	}

	public void setCheckValiationsForInputFields(Boolean checkValiationsForInputFields) {
		this.checkValiationsForInputFields = checkValiationsForInputFields;
	}

	public boolean isDisabledModelsMenu() {
		return disabledModelsMenu;
	}

	public void setDisabledModelsMenu(boolean disabledModelsMenu) {
		this.disabledModelsMenu = disabledModelsMenu;
	}

	public boolean isDisabledModelsPopUpBtn() {
		return disabledModelsPopUpBtn;
	}

	public void setDisabledModelsPopUpBtn(boolean disabledModelsPopUpBtn) {
		this.disabledModelsPopUpBtn = disabledModelsPopUpBtn;
	}

	public static boolean isRenameDone() {
		return renameDone;
	}

	public static void setRenameDone(boolean renameDone) {
		BacklogPermitBackingBean.renameDone = renameDone;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public int getPollValue() {
		return pollValue;
	}

	public void setPollValue(int pollValue) {
		this.pollValue = pollValue;
	}

	public boolean isPollValueShow() {
		return pollValueShow;
	}

	public void setPollValueShow(boolean pollValueShow) {
		this.pollValueShow = pollValueShow;
	}

	public int getProgress() {
		return progress;
	}

	public MigratedService getMigratedService() {
		return migratedService;
	}

	public void setMigratedService(MigratedService migratedService) {
		this.migratedService = migratedService;
	}

	public AssignModelDTO getAssignModelDTO() {
		return assignModelDTO;
	}

	public void setAssignModelDTO(AssignModelDTO assignModelDTO) {
		this.assignModelDTO = assignModelDTO;
	}

}
