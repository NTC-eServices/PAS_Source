package lk.informatics.ntc.view.beans;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.BusOwnerDTO;
import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.DocumentManagementDTO;
import lk.informatics.ntc.model.dto.IssuePermitDTO;
import lk.informatics.ntc.model.dto.OminiBusDTO;
import lk.informatics.ntc.model.dto.PaymentVoucherDTO;
import lk.informatics.ntc.model.dto.PermitRenewalsDTO;
import lk.informatics.ntc.model.dto.RouteDTO;
import lk.informatics.ntc.model.dto.UploadImageDTO;
import lk.informatics.ntc.model.dto.VehicleInspectionDTO;
import lk.informatics.ntc.model.service.AdminService;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.DocumentManagementService;
import lk.informatics.ntc.model.service.EmployeeProfileService;
import lk.informatics.ntc.model.service.HistoryService;
import lk.informatics.ntc.model.service.InspectionActionPointService;
import lk.informatics.ntc.model.service.IssuePermitService;
import lk.informatics.ntc.model.service.MigratedService;
import lk.informatics.ntc.model.service.PermitRenewalsService;
import lk.informatics.ntc.model.service.QueueManagementService;
import lk.informatics.ntc.model.service.VehicleInspectionService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "newPermitIssuanceBackingBean")
@ViewScoped

public class NewPermitIssuanceBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public static Logger logger = Logger.getLogger("UploadPhotosBackingBean");

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private IssuePermitService issuePermitService;
	private AdminService adminService;
	private EmployeeProfileService employeeProfileService;
	private CommonService commonService;
	private MigratedService migratedService;
	private VehicleInspectionService vehicleInspectionService;
	private InspectionActionPointService inspectionActionPointService;
	private PermitRenewalsService permitRenewalsService;

	private CommonDTO commonDTO;
	private IssuePermitDTO issuePermitDTO;
	private BusOwnerDTO busOwnerDTO;
	private OminiBusDTO ominiBusDTO;
	private RouteDTO routeDTO;
	private PaymentVoucherDTO paymentVoucherDTO;
	private PermitRenewalsDTO checkAndDisplayRemarkValue;
	public VehicleInspectionDTO vehicleDTO = new VehicleInspectionDTO();
	private VehicleInspectionDTO viewedDetails;
	private UploadImageDTO uploadImageDTO;

	private byte[] permitOwnerFaceImage;
	private byte[] firstVehicleImg;
	private byte[] secondVehicleImg;
	private byte[] thirdVehicleImg;
	private byte[] fourthVehicleImg;
	private byte[] fifthVehicleImg;
	private byte[] sixthVehicleImg;

	private QueueManagementService queueManagementService;

	private DocumentManagementService documentManagementService;

	boolean check = true;
	boolean callNext = false;
	boolean skip = true;
	private boolean localcheckcounter = true;

	private int activeTabIndex;

	private boolean next;

	private boolean applicationFound;
	private boolean loanObtained;

	private String strSelectedTitle;
	private String strSelectedGender;
	private String strSelectedMartial;
	private String strSelectedProvince;
	private String strSelectedDistrict;
	private String strSelectedDivSec;
	private String strSelectedLanguage;
	private String strNicNo;
	private int permitPeriod;

	private String strTenderRefNo;
	private String strCurrentDate;
	private String strApplicationNo;
	private String strQueueNo;
	private String strServiceType;
	private String strBusRegNo;
	private String strPermitNo;
	private String strSelectedRoute;

	private String errorMsg;

	private String vehicleNo;
	private String applicationNo;
	private String vehicleOwnerName;

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
	private List<VehicleInspectionDTO> dataListViewInspection = new ArrayList<VehicleInspectionDTO>();

	private List<DocumentManagementDTO> mandatoryList = new ArrayList<DocumentManagementDTO>();
	private List<DocumentManagementDTO> optionalList = new ArrayList<DocumentManagementDTO>();

	private IssuePermitDTO filteredValuesDTO;

	private Boolean routeFlag;
	private boolean ownerImageUpload = false;

	private String tempCounterId;

	private IssuePermitDTO serviceHistoryDTO;
	private PermitRenewalsDTO vehicleOwnerHistoryDTO;
	private OminiBusDTO OminiBusHistoryDTO;
	private PermitRenewalsDTO applicationHistoryDTO;
	private HistoryService historyService;

	public NewPermitIssuanceBackingBean() {

	}

	@PostConstruct
	public void init() {

		activeTabIndex = 0;
		issuePermitDTO = new IssuePermitDTO();
		busOwnerDTO = new BusOwnerDTO();
		ominiBusDTO = new OminiBusDTO();
		routeDTO = new RouteDTO();
		commonDTO = new CommonDTO();
		uploadImageDTO = new UploadImageDTO();
		paymentVoucherDTO = new PaymentVoucherDTO();
		checkAndDisplayRemarkValue = new PermitRenewalsDTO();
		routeFlag = false;
		tempCounterId = sessionBackingBean.getCounterId();
		applicationHistoryDTO = new PermitRenewalsDTO();

		setNext(false);
		setApplicationFound(false);
		setLoanObtained(false);

		setCallNext(false);
		setSkip(true);
		localcheckcounter = sessionBackingBean.isCounterCheck();

		loadValues();
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

	@SuppressWarnings("deprecation")
	public void searchApplication() {
		String permitNoCheck, tsakStatusCheck;

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		strCurrentDate = dateFormat.format(date).toString();

		if (strQueueNo != null && strQueueNo != " " && !strQueueNo.isEmpty()) {
			issuePermitDTO = issuePermitService.getApplicationDetailsByQueueNo(strQueueNo);

			if (issuePermitDTO.getIsNewPermit() != null && issuePermitDTO.getIsNewPermit().equalsIgnoreCase("Y")
					&& !issuePermitDTO.getApplicationNo().isEmpty()) {
				errorMsg = "Permit issued already, Continue with Permit Renewals";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				return;
			}

			tsakStatusCheck = issuePermitService.checkTsakStatusPM101FromQueueNo(strQueueNo);

			if (tsakStatusCheck == null || tsakStatusCheck.trim().isEmpty()) {
				errorMsg = "Inspection not completed";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				return;
			}

			permitNoCheck = issuePermitService.checkPermitNoFromQueueNo(strQueueNo);

			if (permitNoCheck != null && !permitNoCheck.trim().isEmpty()) {
				errorMsg = "Continue with Edit New Permit";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				return;
			}

			strTenderRefNo = issuePermitDTO.getTenderRefNo();
			strApplicationNo = issuePermitDTO.getApplicationNo();
			strQueueNo = issuePermitDTO.getQueueNo();
			strServiceType = issuePermitDTO.getServiceType();
			strBusRegNo = issuePermitDTO.getBusRegNo();

			if (issuePermitDTO.getApplicationNo() != null && issuePermitDTO.getApplicationNo() != " "
					&& !issuePermitDTO.getApplicationNo().isEmpty()) {
				setApplicationFound(true);
			} else {
				strCurrentDate = null;
				errorMsg = "Application not found";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

		} else if (strApplicationNo != null && strApplicationNo != " " && !strApplicationNo.isEmpty()) {

			issuePermitDTO = issuePermitService.getApplicationDetailsByApplicationNo(strApplicationNo);

			String appStatus = vehicleInspectionService
					.applicationTaskCodeStatusPM101(issuePermitDTO.getApplicationNo());

			if (appStatus == null || appStatus.isEmpty()) {
				errorMsg = "Inspection not completed";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				return;
			}

			strTenderRefNo = issuePermitDTO.getTenderRefNo();
			strApplicationNo = issuePermitDTO.getApplicationNo();
			strQueueNo = issuePermitDTO.getQueueNo();
			strServiceType = issuePermitDTO.getServiceType();
			strBusRegNo = issuePermitDTO.getBusRegNo();

			if (issuePermitDTO.getApplicationNo() != null && issuePermitDTO.getApplicationNo() != " "
					&& !issuePermitDTO.getApplicationNo().isEmpty()) {
				setApplicationFound(true);
			} else {
				strCurrentDate = null;
				errorMsg = "Application not found";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

		} else {
			strCurrentDate = null;
			errorMsg = "Queue No. is required";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		}

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

		vehicleInspectionService = (VehicleInspectionService) SpringApplicationContex
				.getBean("vehicleInspectionService");

		historyService = (HistoryService) SpringApplicationContex.getBean("historyService");

		permitRenewalsService = (PermitRenewalsService) SpringApplicationContex.getBean("permitRenewalsService");

		titleList = employeeProfileService.GetTitleToDropdown();
		genderList = employeeProfileService.GetGenderToDropdown();
		martialList = employeeProfileService.GetMartialToDropdown();

		provincelList = adminService.getProvinceToDropdown();
		allDivSecList = adminService.getDivSecToDropdown();

		routefordropdownList = issuePermitService.getRoutesToDropdown();

		counterList = commonService.counterDropdown("03");
		inspectionActionPointService = (InspectionActionPointService) SpringApplicationContex
				.getBean("inspectionActionPointService");
	}

	public void getOwnerDetails() {

		if (strApplicationNo != null && !strApplicationNo.isEmpty() && strApplicationNo != "") {

			busOwnerDTO = issuePermitService.getOwnerDetailsByApplicationNo(strApplicationNo);

			activeTabIndex = 1;
			activeTabIndex = 2;
			activeTabIndex = 3;

			if (busOwnerDTO.getNicNo() != null && busOwnerDTO.getNicNo() != " " && !busOwnerDTO.getNicNo().isEmpty()) {
				strSelectedTitle = busOwnerDTO.getTitle();
				strSelectedMartial = busOwnerDTO.getMaritalStatus();
				strSelectedGender = busOwnerDTO.getGender();
				strSelectedDivSec = busOwnerDTO.getDivSec();
				strSelectedDistrict = busOwnerDTO.getDistrict();
				strSelectedProvince = busOwnerDTO.getProvince();
				strSelectedLanguage = busOwnerDTO.getPerferedLanguage();
				strNicNo = busOwnerDTO.getNicNo();
				strPermitNo = null;

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

			busOwnerDTO.setNicNo(strNicNo);
			setNext(true);
			commonService.updateTaskStatus(strApplicationNo, "PM101", "PM200", "C", sessionBackingBean.getLoginUser());

		}
		activeTabIndex = 0;

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

	@SuppressWarnings("deprecation")
	public void busOwnerSave() {
		if (strSelectedTitle != null && !strSelectedTitle.isEmpty() && !strSelectedTitle.equalsIgnoreCase("")) {
			if (strSelectedGender != null && !strSelectedGender.isEmpty() && !strSelectedGender.equalsIgnoreCase("")) {
				if (busOwnerDTO.getDob() != null) {
					if (busOwnerDTO.getNicNo() != null && !busOwnerDTO.getNicNo().isEmpty()
							&& !busOwnerDTO.getNicNo().equalsIgnoreCase("")) {
						if (busOwnerDTO.getFullName() != null && !busOwnerDTO.getFullName().isEmpty()
								&& !busOwnerDTO.getFullName().equalsIgnoreCase("")) {
							// START - separate validations for three languages
							if (strSelectedLanguage.equals("E")) {
								// validations for ENGLISH
								if (busOwnerDTO.getAddress1() != null && !busOwnerDTO.getAddress1().isEmpty()
										&& !busOwnerDTO.getAddress1().equalsIgnoreCase("")) {
									if (busOwnerDTO.getAddress2() != null && !busOwnerDTO.getAddress2().isEmpty()
											&& !busOwnerDTO.getAddress2().equalsIgnoreCase("")) {
										if (busOwnerDTO.getCity() != null && !busOwnerDTO.getCity().isEmpty()
												&& !busOwnerDTO.getCity().equalsIgnoreCase("")) {
											if (strSelectedMartial != null && !strSelectedMartial.isEmpty()
													&& !strSelectedMartial.equalsIgnoreCase("")) {
												if (strSelectedProvince != null && !strSelectedProvince.isEmpty()
														&& !strSelectedProvince.equalsIgnoreCase("")) {
													if (permitPeriod > 0) {
														if (busOwnerDTO.getSeq() != 0) {
															// Update
															updateOwner();
														} else {
															// save
															saveOwner();
														}
													} else {
														errorMsg = "Permit Period should be selected.";
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
												errorMsg = "Marital Status should be selected.";
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
							} else if (strSelectedLanguage.equals("S")) {
								// validations for SINHALA
								if (busOwnerDTO.getFullNameSinhala() != null
										&& !busOwnerDTO.getFullNameSinhala().isEmpty()
										&& !busOwnerDTO.getFullNameSinhala().equalsIgnoreCase("")) {
									if (busOwnerDTO.getAddress1() != null && !busOwnerDTO.getAddress1().isEmpty()
											&& !busOwnerDTO.getAddress1().equalsIgnoreCase("")) {
										if (busOwnerDTO.getAddress2() != null && !busOwnerDTO.getAddress2().isEmpty()
												&& !busOwnerDTO.getAddress2().equalsIgnoreCase("")) {
											if (busOwnerDTO.getCity() != null && !busOwnerDTO.getCity().isEmpty()
													&& !busOwnerDTO.getCity().equalsIgnoreCase("")) {
												if (busOwnerDTO.getAddress1Sinhala() != null
														&& !busOwnerDTO.getAddress1Sinhala().isEmpty()
														&& !busOwnerDTO.getAddress1Sinhala().equalsIgnoreCase("")) {
													if (busOwnerDTO.getAddress2Sinhala() != null
															&& !busOwnerDTO.getAddress2Sinhala().isEmpty()
															&& !busOwnerDTO.getAddress2Sinhala().equalsIgnoreCase("")) {
														if (busOwnerDTO.getCitySinhala() != null
																&& !busOwnerDTO.getCitySinhala().isEmpty()
																&& !busOwnerDTO.getCitySinhala().equalsIgnoreCase("")) {
															if (strSelectedMartial != null
																	&& !strSelectedMartial.isEmpty()
																	&& !strSelectedMartial.equalsIgnoreCase("")) {
																if (strSelectedProvince != null
																		&& !strSelectedProvince.isEmpty()
																		&& !strSelectedProvince.equalsIgnoreCase("")) {
																	if (permitPeriod > 0) {
																		if (busOwnerDTO.getSeq() != 0) {
																			// Update
																			updateOwner();
																		} else {
																			// save
																			saveOwner();
																		}
																	} else {
																		errorMsg = "Permit Period should be selected.";
																		RequestContext.getCurrentInstance()
																				.update("frmrequiredField");
																		RequestContext.getCurrentInstance()
																				.execute("PF('requiredField').show()");
																	}
																} else {
																	errorMsg = "Province should be selected.";
																	RequestContext.getCurrentInstance()
																			.update("frmrequiredField");
																	RequestContext.getCurrentInstance()
																			.execute("PF('requiredField').show()");
																}
															} else {
																errorMsg = "Marital Status should be selected.";
																RequestContext.getCurrentInstance()
																		.update("frmrequiredField");
																RequestContext.getCurrentInstance()
																		.execute("PF('requiredField').show()");
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
														RequestContext.getCurrentInstance().update("frmrequiredField");
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
												errorMsg = "City should be entered.";
												RequestContext.getCurrentInstance().update("frmrequiredField");
												RequestContext.getCurrentInstance()
														.execute("PF('requiredField').show()");
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
									errorMsg = "Name in Full (Sinhala) should be entered.";
									RequestContext.getCurrentInstance().update("frmrequiredField");
									RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
								}
							} else if (strSelectedLanguage.equals("T")) {
								// validations for TAMIL
								if (busOwnerDTO.getFullNameTamil() != null && !busOwnerDTO.getFullNameTamil().isEmpty()
										&& !busOwnerDTO.getFullNameTamil().equalsIgnoreCase("")) {
									if (busOwnerDTO.getAddress1() != null && !busOwnerDTO.getAddress1().isEmpty()
											&& !busOwnerDTO.getAddress1().equalsIgnoreCase("")) {
										if (busOwnerDTO.getAddress2() != null && !busOwnerDTO.getAddress2().isEmpty()
												&& !busOwnerDTO.getAddress2().equalsIgnoreCase("")) {
											if (busOwnerDTO.getCity() != null && !busOwnerDTO.getCity().isEmpty()
													&& !busOwnerDTO.getCity().equalsIgnoreCase("")) {
												if (busOwnerDTO.getAddress1Tamil() != null
														&& !busOwnerDTO.getAddress1Tamil().isEmpty()
														&& !busOwnerDTO.getAddress1Tamil().equalsIgnoreCase("")) {
													if (busOwnerDTO.getAddress2Tamil() != null
															&& !busOwnerDTO.getAddress2Tamil().isEmpty()
															&& !busOwnerDTO.getAddress2Tamil().equalsIgnoreCase("")) {
														if (busOwnerDTO.getCityTamil() != null
																&& !busOwnerDTO.getCityTamil().isEmpty()
																&& !busOwnerDTO.getCityTamil().equalsIgnoreCase("")) {
															if (strSelectedMartial != null
																	&& !strSelectedMartial.isEmpty()
																	&& !strSelectedMartial.equalsIgnoreCase("")) {
																if (strSelectedProvince != null
																		&& !strSelectedProvince.isEmpty()
																		&& !strSelectedProvince.equalsIgnoreCase("")) {
																	if (permitPeriod > 0) {
																		if (busOwnerDTO.getSeq() != 0) {
																			// Update
																			updateOwner();
																		} else {
																			// save
																			saveOwner();
																		}
																	} else {
																		errorMsg = "Permit Period should be selected.";
																		RequestContext.getCurrentInstance()
																				.update("frmrequiredField");
																		RequestContext.getCurrentInstance()
																				.execute("PF('requiredField').show()");
																	}
																} else {
																	errorMsg = "Province should be selected.";
																	RequestContext.getCurrentInstance()
																			.update("frmrequiredField");
																	RequestContext.getCurrentInstance()
																			.execute("PF('requiredField').show()");
																}
															} else {
																errorMsg = "Marital Status should be selected.";
																RequestContext.getCurrentInstance()
																		.update("frmrequiredField");
																RequestContext.getCurrentInstance()
																		.execute("PF('requiredField').show()");
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
														RequestContext.getCurrentInstance().update("frmrequiredField");
														RequestContext.getCurrentInstance()
																.execute("PF('requiredField').show()");
													}
												} else {
													errorMsg = "Address  Line 1 (Tamil) should be entered.";
													RequestContext.getCurrentInstance().update("frmrequiredField");
													RequestContext.getCurrentInstance()
															.execute("PF('requiredField').show()");
												}
											} else {
												errorMsg = "City should be entered.";
												RequestContext.getCurrentInstance().update("frmrequiredField");
												RequestContext.getCurrentInstance()
														.execute("PF('requiredField').show()");
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
									errorMsg = "Name in Full (Tamil) should be entered.";
									RequestContext.getCurrentInstance().update("frmrequiredField");
									RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
								}
							}

							// END - separate validations for three languages
						} else {
							errorMsg = "Full Name should be entered.";
							RequestContext.getCurrentInstance().update("frmrequiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
						}
					} else {
						errorMsg = "NIC No. should be entered.";
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
	}

	private void saveOwner() {
		busOwnerDTO.setPermitPeroid(permitPeriod);
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

			/* Update Owner History Table */
			vehicleOwnerHistoryDTO = historyService.getVehicleOwnerTableData(busOwnerDTO.getApplicationNo(),
					sessionBackingBean.getLoginUser());
			strPermitNo = issuePermitService.saveBusOwnerDetails(busOwnerDTO);

			applicationHistoryDTO = historyService.getApplicationTableData(strApplicationNo,
					sessionBackingBean.getLoginUser());

			if (strPermitNo != null && strPermitNo != " " && !strPermitNo.isEmpty()) {
				historyService.insertVehicleOwnerHistoryData(vehicleOwnerHistoryDTO);
				issuePermitDTO.setPermitNo(strPermitNo);

				vehicleDTO.setTranstractionTypeCode("03");
				String currentAppNo = strApplicationNo;
				int updateTranTypeInApplication = permitRenewalsService.updateTranstractionType(
						vehicleDTO.getTranstractionTypeCode(), currentAppNo, sessionBackingBean.getLoginUser());
				historyService.insertApplicationHistoryData(applicationHistoryDTO);

				ominiBusDTO.setVehicleRegNo(strBusRegNo);
				getOwnerDetailsByPermitNo();
				getOminiBusDetails2();

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
			}
		}
	}

	private void updateOwner() {
		busOwnerDTO.setPermitPeroid(permitPeriod);
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

		/* Update Owner History Table */
		vehicleOwnerHistoryDTO = historyService.getVehicleOwnerTableData(busOwnerDTO.getApplicationNo(),
				sessionBackingBean.getLoginUser());
		int result = issuePermitService.updateBusOwner(busOwnerDTO);

		if (result == 0) {
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
		issuePermitDTO.setApplicationNo(strApplicationNo);
		issuePermitDTO.setBusRegNo(strBusRegNo);
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

														// loan

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

																				// update
																				ominiBusDTO.setModifiedBy(
																						sessionBackingBean
																								.getLoginUser());
																				ominiBusDTO.setPermitNo(
																						issuePermitDTO.getPermitNo());

																				ominiBusDTO
																						.setApplicationNo(issuePermitDTO
																								.getApplicationNo());

																				/* Update Omini Bus History Data */
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
																					activeTabIndex = 2;

																					historyService
																							.insertOminiBusHistoryData(
																									OminiBusHistoryDTO);
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

																				/* Update Omini Bus History Data */
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

																					activeTabIndex = 2;
																					historyService
																							.insertOminiBusHistoryData(
																									OminiBusHistoryDTO);
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

														// no loan

														ominiBusDTO.setIsLoanObtained("N");

														if (ominiBusDTO.getSeq() != 0) {
															// update
															ominiBusDTO
																	.setModifiedBy(sessionBackingBean.getLoginUser());
															ominiBusDTO.setPermitNo(issuePermitDTO.getPermitNo());
															ominiBusDTO.setApplicationNo(
																	issuePermitDTO.getApplicationNo());

															/* Update Omini Bus History Data */
															OminiBusHistoryDTO = historyService.getOminiBusTableData(
																	ominiBusDTO.getApplicationNo(),
																	sessionBackingBean.getLoginUser());
															int result = issuePermitService
																	.updateNewPermitOminiBus(ominiBusDTO);

															if (result == 0) {
																activeTabIndex = 2;
																historyService
																		.insertOminiBusHistoryData(OminiBusHistoryDTO);
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

															/* Update Omini Bus History Data */
															OminiBusHistoryDTO = historyService.getOminiBusTableData(
																	ominiBusDTO.getApplicationNo(),
																	sessionBackingBean.getLoginUser());
															int result = issuePermitService
																	.saveNewPermitOminiBus(ominiBusDTO);

															if (result == 0) {

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
		getOminiBusDetails2();
	}

	public void getOminiBusDetails() {

		if (issuePermitDTO.getPermitNo() != null && !issuePermitDTO.getPermitNo().isEmpty()
				&& issuePermitDTO.getPermitNo() != "") {

			ominiBusDTO = issuePermitService.ominiBusByVehicleNo(issuePermitDTO.getBusRegNo());

			if (ominiBusDTO.getIsLoanObtained() != null && !ominiBusDTO.getIsLoanObtained().isEmpty()
					&& ominiBusDTO.getIsLoanObtained().equals("Y")) {

				setLoanObtained(true);
			} else if (ominiBusDTO.getIsLoanObtained() != null && !ominiBusDTO.getIsLoanObtained().isEmpty()
					&& ominiBusDTO.getIsLoanObtained().equals("N")) {

				setLoanObtained(false);
			} else {
				setLoanObtained(false);
			}

		}
	}

	public void getOminiBusDetails2() {
		ominiBusDTO.setVehicleRegNo(strBusRegNo);
		if (issuePermitDTO.getPermitNo() != null && !issuePermitDTO.getPermitNo().isEmpty()
				&& issuePermitDTO.getPermitNo() != "") {

			ominiBusDTO = issuePermitService.ominiBusByVehicleNo(issuePermitDTO.getBusRegNo());

			ominiBusDTO.setSeq(0);
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

		DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();
		try {

			sessionBackingBean.setApplicationNoForDoc(strApplicationNo);
			sessionBackingBean.setPermitRenewalPermitNo(strPermitNo);
			sessionBackingBean.setPermitRenewalTranstractionTypeDescription("NEW PERMIT");
			sessionBackingBean.setGoToPermitRenewalUploadDocPopUp(true);

			uploaddocumentManagementDTO.setUpload_Permit(strPermitNo);
			uploaddocumentManagementDTO.setTransaction_Type("NEW PERMIT");

			mandatoryList = documentManagementService.mandatoryDocs("03", strPermitNo);

			sessionBackingBean.optionalList = documentManagementService.optionalDocs("03", strPermitNo);

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
						// update
						issuePermitDTO.setModifiedBy(sessionBackingBean.getLoginUser());

						/* Update Service History Table */
						serviceHistoryDTO = historyService.getServiceTableData(issuePermitDTO.getApplicationNo(),
								sessionBackingBean.getLoginUser());
						int result = issuePermitService.updateService(issuePermitDTO);

						if (result == 0) {
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
						// save

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
			errorMsg = "No. of Trips per Day should be entered.";
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
		onRouteChange();
		if (issuePermitDTO.getRouteFlag() != null && !issuePermitDTO.getRouteFlag().trim().equals("")
				&& issuePermitDTO.getRouteFlag().equalsIgnoreCase("Y")) {
			routeFlag = true;
			routeFlagListener();

		} else if (issuePermitDTO.getRouteFlag() != null && !issuePermitDTO.getRouteFlag().trim().equals("")
				&& issuePermitDTO.getRouteFlag().equalsIgnoreCase("N")) {

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
		issuePermitDTO.setApplicationNo(strApplicationNo);
		issuePermitDTO.setBusRegNo(strBusRegNo);
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

	public void viewVehicleInspectionAct() {

		String viewedApplicationNo = issuePermitDTO.getApplicationNo();
		String selectedPermitNo = issuePermitDTO.getPermitNo();
		String searchedQueueNum = issuePermitDTO.getQueueNo();
		String selectedApplicationNo = issuePermitDTO.getApplicationNo();
		sessionBackingBean.setViewedInspectionApplicationNo(viewedApplicationNo);
		sessionBackingBean.setViewedPermitNo(selectedPermitNo);
		sessionBackingBean.setSelectedQueueNo(searchedQueueNum);
		sessionBackingBean.setApplicationNo(selectedApplicationNo);
		sessionBackingBean.setCheckPermitIssueNewPermit(true);
		uploadPhotoMethod();

		loadValuesForViewInspection();
		RequestContext.getCurrentInstance().execute("PF('vehicleInspectionViewModeId').show()");
		RequestContext.getCurrentInstance().update("vehicleInspectionViewModeId");
		RequestContext.getCurrentInstance().update("vehicleInspectionViewModeId:frmUploadPhotos");
	}

	private void loadValuesForViewInspection() {

		RequestContext.getCurrentInstance().update("bla");
		RequestContext.getCurrentInstance().update("footerDiv");
		viewedDetails = inspectionActionPointService
				.getRecordForCurrentApplicationNo(issuePermitDTO.getApplicationNo());
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
		dataListViewInspection = inspectionActionPointService
				.getAllInspectionRecordsDetails(issuePermitDTO.getApplicationNo());
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

	private void uploadPhotoMethod() {
		RequestContext.getCurrentInstance().update("bla");
		RequestContext.getCurrentInstance().update("footerDiv");

		issuePermitDTO.setPermitNo("TP1800040");

		filteredValuesDTO = issuePermitService.getSelectedValuesForUploadPhotos(issuePermitDTO.getApplicationNo(),
				issuePermitDTO.getPermitNo());
		try {

			FacesContext fcontext = FacesContext.getCurrentInstance();
			fcontext.getExternalContext().getSessionMap().put("VEHICLE_NO", filteredValuesDTO.getBusRegNo());
			fcontext.getExternalContext().getSessionMap().put("APPLICATION_NO", issuePermitDTO.getApplicationNo());
			fcontext.getExternalContext().getSessionMap().put("OWNER_NAME", filteredValuesDTO.getVehicleOwner());
			fcontext.getExternalContext().getSessionMap().put("VEHICLE_INSPECTION", "view");

		} catch (Exception e) {
			e.printStackTrace();
		}

		uploadImageDTO.setVehicleNo(filteredValuesDTO.getBusRegNo());
		uploadImageDTO.setVehicleOwnerName(filteredValuesDTO.getVehicleOwner());
		uploadImageDTO.setApplicationNo(issuePermitDTO.getApplicationNo());
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

			uploadImageDTO = vehicleInspectionService.retrieveVehicleImageDataForVehicleNo(applicationNo);

			if (uploadImageDTO != null) {
				/** set owner image start **/
				if (uploadImageDTO.getVehicleOwnerPhotoPath() != null
						&& !uploadImageDTO.getVehicleOwnerPhotoPath().isEmpty()
						&& !uploadImageDTO.getVehicleOwnerPhotoPath().trim().equalsIgnoreCase("")) {
					BufferedImage ownerImage = ImageIO.read(new File(uploadImageDTO.getVehicleOwnerPhotoPath()));
					BufferedImage resized = resize(ownerImage, 600, 600);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					ImageIO.write(resized, "png", bos);
					permitOwnerFaceImage = bos.toByteArray();

					ownerImageUpload = true;
				}
				/** set owner image end **/

				/** set vehicle images start **/
				if (uploadImageDTO.getFirstVehiImagePath() != null && !uploadImageDTO.getFirstVehiImagePath().isEmpty()
						&& !uploadImageDTO.getFirstVehiImagePath().trim().equalsIgnoreCase("")) {
					BufferedImage vehicleImage = ImageIO.read(new File(uploadImageDTO.getFirstVehiImagePath()));
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
				}
				if (uploadImageDTO.getThirdVehiImagePath() != null && !uploadImageDTO.getThirdVehiImagePath().isEmpty()
						&& !uploadImageDTO.getThirdVehiImagePath().trim().equalsIgnoreCase("")) {
					BufferedImage vehicleImage = ImageIO.read(new File(uploadImageDTO.getThirdVehiImagePath()));
					BufferedImage resized = resize(vehicleImage, 500, 500);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					ImageIO.write(resized, "png", bos);
					thirdVehicleImg = bos.toByteArray();
				}
				if (uploadImageDTO.getForthVehiImagePath() != null && !uploadImageDTO.getForthVehiImagePath().isEmpty()
						&& !uploadImageDTO.getForthVehiImagePath().trim().equalsIgnoreCase("")) {
					BufferedImage vehicleImage = ImageIO.read(new File(uploadImageDTO.getForthVehiImagePath()));
					BufferedImage resized = resize(vehicleImage, 500, 500);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					ImageIO.write(resized, "png", bos);
					fourthVehicleImg = bos.toByteArray();
				}
				if (uploadImageDTO.getFifthVehiImagePath() != null && !uploadImageDTO.getFifthVehiImagePath().isEmpty()
						&& !uploadImageDTO.getFifthVehiImagePath().trim().equalsIgnoreCase("")) {
					BufferedImage vehicleImage = ImageIO.read(new File(uploadImageDTO.getFifthVehiImagePath()));
					BufferedImage resized = resize(vehicleImage, 500, 500);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					ImageIO.write(resized, "png", bos);
					fifthVehicleImg = bos.toByteArray();
				}
				if (uploadImageDTO.getSixthVehiImagePath() != null && !uploadImageDTO.getSixthVehiImagePath().isEmpty()
						&& !uploadImageDTO.getSixthVehiImagePath().trim().equalsIgnoreCase("")) {
					BufferedImage vehicleImage = ImageIO.read(new File(uploadImageDTO.getSixthVehiImagePath()));
					BufferedImage resized = resize(vehicleImage, 500, 500);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					ImageIO.write(resized, "png", bos);
					sixthVehicleImg = bos.toByteArray();
				}

				/** set vehicle images end **/

			} else {

				uploadImageDTO = new UploadImageDTO();
			}

			uploadImageDTO.setVehicleNo(vehicleNo);
			uploadImageDTO.setApplicationNo(applicationNo);
			uploadImageDTO.setVehicleOwnerName(vehicleOwnerName);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}

		RequestContext.getCurrentInstance().update("vehicleInspectionViewModeId");
		RequestContext.getCurrentInstance().update(":frmvehicleInspectionViewModeId:frmUploadPhotos");

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

	private static BufferedImage resize(BufferedImage img, int height, int width) {
		try {

			Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);

			BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

			Graphics2D g2d = resized.createGraphics();

			g2d.drawImage(tmp, 0, 0, null);

			g2d.dispose();

			return resized;

		} catch (Exception e) {
			e.printStackTrace();

		}

		return null;
	}

	public void callNext() {

		String queueNo = queueManagementService.callNextQueueNumberAction("03", "02");
		strQueueNo = queueNo;

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		strCurrentDate = dateFormat.format(date).toString();

		if (strQueueNo != null && !strQueueNo.isEmpty() && !strQueueNo.trim().equalsIgnoreCase("")) {

			issuePermitDTO = issuePermitService.getApplicationDetailsByQueueNo(queueNo);

			if (issuePermitDTO.getIsNewPermit() != null && issuePermitDTO.getIsNewPermit().equalsIgnoreCase("Y")
					&& !issuePermitDTO.getApplicationNo().isEmpty()) {
				errorMsg = "Permit issued already, Continue with Permit Renewals";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				return;
			}

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

			commonService.updateCounterNo(sessionBackingBean.getCounterId());
			setCallNext(false);
			setSkip(true);
			clearAll();

		} else {
			strApplicationNo = null;
		}

	}

	public void onCounterSelect() {
		sessionBackingBean.setCounter(commonDTO.getCounter());
		sessionBackingBean.setCounterId(commonDTO.getCounterId());

		vehicleInspectionService = (VehicleInspectionService) SpringApplicationContex
				.getBean("vehicleInspectionService");
		vehicleInspectionService.counterStatus(commonDTO.getCounterId(), sessionBackingBean.getLoginUser());
		localcheckcounter = false;
		sessionBackingBean.setCounterCheck(false);
		RequestContext context = RequestContext.getCurrentInstance();

		context.execute("PF('dlg2').hide();");
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		try {
			ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());

		} catch (IOException e) {

			e.printStackTrace();
		}
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

	public IssuePermitDTO getIssuePermitDTO() {
		return issuePermitDTO;
	}

	public void setIssuePermitDTO(IssuePermitDTO issuePermitDTO) {
		this.issuePermitDTO = issuePermitDTO;
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

	public String getStrSelectedDivSec() {
		return strSelectedDivSec;
	}

	public void setStrSelectedDivSec(String strSelectedDivSec) {
		this.strSelectedDivSec = strSelectedDivSec;
	}

	public BusOwnerDTO getBusOwnerDTO() {
		return busOwnerDTO;
	}

	public void setBusOwnerDTO(BusOwnerDTO busOwnerDTO) {
		this.busOwnerDTO = busOwnerDTO;
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

	public InspectionActionPointService getInspectionActionPointService() {
		return inspectionActionPointService;
	}

	public void setInspectionActionPointService(InspectionActionPointService inspectionActionPointService) {
		this.inspectionActionPointService = inspectionActionPointService;
	}

	public VehicleInspectionDTO getViewedDetails() {
		return viewedDetails;
	}

	public void setViewedDetails(VehicleInspectionDTO viewedDetails) {
		this.viewedDetails = viewedDetails;
	}

	public String getStrPermitNo() {
		return strPermitNo;
	}

	public void setStrPermitNo(String strPermitNo) {
		this.strPermitNo = strPermitNo;
	}

	public OminiBusDTO getOminiBusDTO() {
		return ominiBusDTO;
	}

	public void setOminiBusDTO(OminiBusDTO ominiBusDTO) {
		this.ominiBusDTO = ominiBusDTO;
	}

	public boolean isLoanObtained() {
		return loanObtained;
	}

	public void setLoanObtained(boolean loanObtained) {
		this.loanObtained = loanObtained;
	}

	public RouteDTO getRouteDTO() {
		return routeDTO;
	}

	public void setRouteDTO(RouteDTO routeDTO) {
		this.routeDTO = routeDTO;
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

	public List<CommonDTO> getAllDivSecList() {
		return allDivSecList;
	}

	public void setAllDivSecList(List<CommonDTO> allDivSecList) {
		this.allDivSecList = allDivSecList;
	}

	public PaymentVoucherDTO getPaymentVoucherDTO() {
		return paymentVoucherDTO;
	}

	public void setPaymentVoucherDTO(PaymentVoucherDTO paymentVoucherDTO) {
		this.paymentVoucherDTO = paymentVoucherDTO;
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

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
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

	public CommonDTO getCommonDTO() {
		return commonDTO;
	}

	public void setCommonDTO(CommonDTO commonDTO) {
		this.commonDTO = commonDTO;
	}

	public List<CommonDTO> getCounterList() {
		return counterList;
	}

	public void setCounterList(List<CommonDTO> counterList) {
		this.counterList = counterList;
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

	public String getTempCounterId() {
		return tempCounterId;
	}

	public void setTempCounterId(String tempCounterId) {
		this.tempCounterId = tempCounterId;
	}

	public int getPermitPeriod() {
		return permitPeriod;
	}

	public void setPermitPeriod(int permitPeriod) {
		this.permitPeriod = permitPeriod;
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

	public VehicleInspectionService getVehicleInspectionService() {
		return vehicleInspectionService;
	}

	public void setVehicleInspectionService(VehicleInspectionService vehicleInspectionService) {
		this.vehicleInspectionService = vehicleInspectionService;
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

	public byte[] getThirdVehicleImg() {
		return thirdVehicleImg;
	}

	public void setThirdVehicleImg(byte[] thirdVehicleImg) {
		this.thirdVehicleImg = thirdVehicleImg;
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

	public byte[] getSixthVehicleImg() {
		return sixthVehicleImg;
	}

	public void setSixthVehicleImg(byte[] sixthVehicleImg) {
		this.sixthVehicleImg = sixthVehicleImg;
	}

	public IssuePermitDTO getFilteredValuesDTO() {
		return filteredValuesDTO;
	}

	public void setFilteredValuesDTO(IssuePermitDTO filteredValuesDTO) {
		this.filteredValuesDTO = filteredValuesDTO;
	}

	public boolean isOwnerImageUpload() {
		return ownerImageUpload;
	}

	public void setOwnerImageUpload(boolean ownerImageUpload) {
		this.ownerImageUpload = ownerImageUpload;
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

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		NewPermitIssuanceBackingBean.logger = logger;
	}

	public MigratedService getMigratedService() {
		return migratedService;
	}

	public void setMigratedService(MigratedService migratedService) {
		this.migratedService = migratedService;
	}

	public PermitRenewalsService getPermitRenewalsService() {
		return permitRenewalsService;
	}

	public void setPermitRenewalsService(PermitRenewalsService permitRenewalsService) {
		this.permitRenewalsService = permitRenewalsService;
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

}