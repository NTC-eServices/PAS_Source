package lk.informatics.ntc.view.beans;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.DocumentManagementDTO;
import lk.informatics.ntc.model.dto.EmployeeAddressDTO;
import lk.informatics.ntc.model.dto.EmployeeDTO;
import lk.informatics.ntc.model.dto.EmployeeDeptDTO;
import lk.informatics.ntc.model.dto.EmployeeDesignationDTO;
import lk.informatics.ntc.model.service.DocumentManagementService;
import lk.informatics.ntc.model.service.EmployeeProfileService;
import lk.informatics.ntc.view.util.NICValidator;
import lk.informatics.ntc.view.util.SpringApplicationContex;
import lk.informatics.ntc.view.util.UtilityClass;

@ManagedBean(name = "employeeProfileBackingBean")
@ViewScoped

public class EmployeeProfileBackingBean {
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	// Services
	private EmployeeProfileService employeeProfileService;
	private DocumentManagementService documentManagementService;

	// DTOs
	private List<CommonDTO> titleList;
	private List<CommonDTO> carderList;
	private List<CommonDTO> qualificationList;
	private List<CommonDTO> genderList;
	private List<CommonDTO> locationList;
	private List<CommonDTO> martialList;
	private List<CommonDTO> addtypeList;
	private List<CommonDTO> cityList;
	private List<CommonDTO> deparmentList;
	private List<CommonDTO> unitList;
	private List<CommonDTO> designationList;
	private List<CommonDTO> gradeList;
	private List<CommonDTO> salaryCatList;
	private EmployeeDTO employeeDTO;
	private EmployeeAddressDTO empAddressDTO;
	private List<EmployeeAddressDTO> employeeAddresslist = new ArrayList<EmployeeAddressDTO>();
	private EmployeeAddressDTO strSelectedAddressSeq;
	private EmployeeDeptDTO empolyeeDeptDTO;
	private List<EmployeeDeptDTO> employeeDepartmentlist = new ArrayList<EmployeeDeptDTO>();
	private EmployeeDeptDTO strSelectedDeptSeq;
	private EmployeeDeptDTO chkemployeeDTO;
	private List<EmployeeDesignationDTO> employeeDesignationlist = new ArrayList<EmployeeDesignationDTO>();
	private EmployeeDesignationDTO employeeDesignationDTO;
	private EmployeeDesignationDTO strSelectedDesSeq;
	private List<DocumentManagementDTO> mandatoryList = new ArrayList<DocumentManagementDTO>(0);
	private List<DocumentManagementDTO> optionalList = new ArrayList<DocumentManagementDTO>(0);

	// SelectedValues
	private String strSelectedTitle;
	private String strSelectedCarder;
	private String strSelectedQualification;
	private String strSelectedLocation;
	private String strSelectedMartialStatus;
	private String strSelectedGenderStatus;
	private String strSelectedAddressType;
	private String strSelectedCity;
	private String strSelectedDeparmentCode;
	private String strSelectedDeparmentDesc;
	private String strSelectedUnit;
	private String strSelectedDesignation;
	private String strSelectedSalaryCat;
	private String strSelectedGrade;
	private String test;
	private Boolean boolEmpDet;
	private Boolean boolEmpAddDet;
	private Boolean boolEmpDesigDet;
	private Boolean boolEmpDeptDet;
	private String errorMsg;
	private Boolean boolDisableAddType;
	private Boolean boolDisableDeptName;
	private Boolean boolDisableUnitName;
	private Boolean boolDesignation;
	private Boolean boolGrade;
	private Boolean boolSalCate;
	private Boolean createMode;
	private Boolean editMode;
	private Boolean viewMode;
	private Boolean disable;
	private boolean checkUpload;
	private boolean checkView;
	private boolean editModeEpf = false;

	private int activeTabIndex;

	public EmployeeProfileBackingBean() {
		employeeProfileService = (EmployeeProfileService) SpringApplicationContex.getBean("employeeProfileService");
		documentManagementService = (DocumentManagementService) SpringApplicationContex
				.getBean("documentManagementService");
	}

	@PostConstruct
	public void init() {

		activeTabIndex = 0;
		employeeDTO = new EmployeeDTO();
		empAddressDTO = new EmployeeAddressDTO();
		empolyeeDeptDTO = new EmployeeDeptDTO();
		employeeDesignationDTO = new EmployeeDesignationDTO();
		LoadValuesforDropdown();
		editMode = false;
		createMode = true;
		viewMode = false;
		disable = false;
		editModeEpf = false;

		if (sessionBackingBean.employeeNo == null) {
			// Create Mode
			boolEmpDet = true;
			boolEmpAddDet = true;
			boolEmpDesigDet = true;
			boolEmpDeptDet = true;
			boolDisableAddType = false;
			boolDisableUnitName = false;
			boolDisableDeptName = false;
			boolDesignation = false;
			boolGrade = false;
			boolSalCate = false;
			errorMsg = null;
			setEditMode(false);

			RequestContext.getCurrentInstance().update("@form");
		} else {
			// View Mode
			if (sessionBackingBean.pageMode == "V") {
				viewMode = true;
				editMode = false;
				createMode = false;
				editModeEpf = false;
				searchEmployee();
				RequestContext.getCurrentInstance().update("@form");
			} else {
				// Edit Mode
				setEditMode(true);

				// check this employee is pending or active
				employeeProfileService = (EmployeeProfileService) SpringApplicationContex
						.getBean("employeeProfileService");
				String currentStatusOfEmp = employeeProfileService
						.checkCurrentStatusForEmp(sessionBackingBean.employeeNo);
				if (currentStatusOfEmp != null) {
					if (currentStatusOfEmp.equals("A")) {
						editModeEpf = true;
					} else if (currentStatusOfEmp.equals("P") || currentStatusOfEmp.equals("O")
							|| currentStatusOfEmp.equals("I")) {
						editModeEpf = false;
					} else {
						editModeEpf = false;
					}
				} else {

				}
				editMode = true;
				createMode = false;
				viewMode = false;

				searchEmployee();
				RequestContext.getCurrentInstance().update("@form");
			}
			sessionBackingBean.employeeNo = null;
		}
	}

	public void LoadValuesforDropdown() {
		employeeProfileService = (EmployeeProfileService) SpringApplicationContex.getBean("employeeProfileService");
		titleList = employeeProfileService.GetTitleToDropdown();
		carderList = employeeProfileService.GetCarderToDropdown();
		qualificationList = employeeProfileService.GetQualificationToDropdown();
		genderList = employeeProfileService.GetGenderToDropdown();
		locationList = employeeProfileService.GetLocationToDropdown();
		martialList = employeeProfileService.GetMartialToDropdown();
		addtypeList = employeeProfileService.GetAddressTypeToDropdown();
		cityList = employeeProfileService.GetCityToDropdown();
		deparmentList = employeeProfileService.GetDepartmentsToDropdown();
		unitList = employeeProfileService.GetUnitsToDropdown();
		designationList = employeeProfileService.GetDesignationToDropdown();
		salaryCatList = employeeProfileService.GetSalaryCatToDropdown();
		gradeList = employeeProfileService.GetEmpGradeToDropdown();
	}

	public void clearForm() {

		strSelectedTitle = null;
		strSelectedCarder = null;
		strSelectedQualification = null;
		strSelectedLocation = null;
		strSelectedMartialStatus = null;
		strSelectedGenderStatus = null;
		strSelectedAddressType = null;
		strSelectedCity = null;
		strSelectedDeparmentCode = null;
		strSelectedDeparmentDesc = null;
		strSelectedUnit = null;
		strSelectedDesignation = null;
		strSelectedSalaryCat = null;
		strSelectedGrade = null;
		employeeAddresslist = new ArrayList<EmployeeAddressDTO>();
		strSelectedAddressSeq = null;
		this.image = null;
		init();
		RequestContext.getCurrentInstance().update("@form");
	}

	public void dateValidation() {
		boolean dateValApp = false;
		boolean dateValPer = false;
		boolean dateValReq = false;

		if ((employeeDTO.getAppoinmentDate() != null) && (employeeDTO.getDob() != null)
				&& (employeeDTO.getAppoinmentDate().before(employeeDTO.getDob()))) {
			errorMsg = "Appointment Date should be greater than DOB";
			RequestContext.getCurrentInstance().update("frmerrorMsge");
			RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
			employeeDTO.setAppoinmentDate(null);
			dateValApp = true;
		}
		if (dateValApp) {
			return;
		}

		if ((employeeDTO.getPermanentDate() != null) && (employeeDTO.getAppoinmentDate() != null)
				&& (employeeDTO.getPermanentDate().before(employeeDTO.getAppoinmentDate()))) {
			errorMsg = "Permanent Date should be greater than Appoinment Date";
			RequestContext.getCurrentInstance().update("frmerrorMsge");
			RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
			employeeDTO.setPermanentDate(null);
			dateValPer = true;
		}
		if (dateValPer) {
			return;
		}

		if ((employeeDTO.getRecruitmentDate() != null && employeeDTO.getAppoinmentDate() != null)
				&& (employeeDTO.getAppoinmentDate().before(employeeDTO.getRecruitmentDate()))) {
			errorMsg = "Appointment Date should be greater than Recruitment Date";
			RequestContext.getCurrentInstance().update("frmerrorMsge");
			RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
			employeeDTO.setRecruitmentDate(null);
			dateValReq = true;
		}
		if (dateValReq) {
			return;
		}

	}

	public void telNoValidation() {

		boolean telnoVal = false;

		if (employeeDTO.getTelNo() != null && !employeeDTO.getTelNo().isEmpty()
				&& !employeeDTO.getTelNo().equalsIgnoreCase("")) {
			Pattern ptr = Pattern.compile("^[0-9]{10}$");
			boolean resultValidateTel = ptr.matcher(employeeDTO.getTelNo()).matches();
			if (resultValidateTel) {
				telnoVal = true;
			} else {
				errorMsg = "Invalid Telephone No.";
				RequestContext.getCurrentInstance().update("frmerrorMsge");
				RequestContext.getCurrentInstance().execute("PF('errorMsg').show()");
				employeeDTO.setTelNo(null);
				telnoVal = false;
			}
		}
	}

	public void mobNoValidation() {

		boolean mobnoVal = false;

		if (employeeDTO.getMobileNo() != null && !employeeDTO.getMobileNo().isEmpty()
				&& !employeeDTO.getMobileNo().equalsIgnoreCase("")) {
			Pattern ptr = Pattern.compile("^[0-9]{10}$");
			boolean resultValidateMob = ptr.matcher(employeeDTO.getMobileNo()).matches();
			if (resultValidateMob) {
				mobnoVal = true;
			} else {
				errorMsg = "Invalid Mobile No.";
				RequestContext.getCurrentInstance().update("frmerrorMsge");
				RequestContext.getCurrentInstance().execute("PF('errorMsg').show()");
				employeeDTO.setMobileNo(null);
				mobnoVal = false;
			}
		}
	}

	public void chkDuplicateNIC() {
		boolean isValidate = false;
		boolean isduplicates = false;

		if ((employeeDTO.getDob() != null) && employeeDTO.getNicNo() != null && !employeeDTO.getNicNo().isEmpty()
				&& !employeeDTO.getNicNo().equalsIgnoreCase("")) {
			java.sql.Date Dob = new java.sql.Date(employeeDTO.getDob().getTime());
			boolean resultValidate = NICValidator.validateNIC(employeeDTO.getNicNo(), employeeDTO.getGender(), Dob);
			if (resultValidate == false) {
				errorMsg = "Invalid NIC No.";
				RequestContext.getCurrentInstance().update("frmerrorMsge");
				RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
				employeeDTO.setNicNo(null);
				isValidate = false;
			} else {
				isValidate = true;
			}
			if (isValidate == false) {
				return;
			}
		}

		if (employeeDTO.getNicNo() != null && !employeeDTO.getNicNo().isEmpty()
				&& !employeeDTO.getNicNo().equalsIgnoreCase("")) {
			String resultValue = employeeProfileService.isNICNoAvilable(employeeDTO.getNicNo());
			if (resultValue != null) {
				errorMsg = "NIC No. already exist";
				RequestContext.getCurrentInstance().update("frmerrorMsge");
				RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
				employeeDTO.setNicNo(null);
				isduplicates = false;
			} else {
				isduplicates = true;
			}
			if (isduplicates == false) {
				return;
			}
		}

	}

	public void chkDuplicateEMP() {

		if (employeeDTO.getEmpNo() != null && !employeeDTO.getEmpNo().isEmpty()
				&& !employeeDTO.getEmpNo().equalsIgnoreCase("")) {
			empolyeeDeptDTO = employeeProfileService.isEmpNoAvilable(employeeDTO.getEmpNo());
			if (empolyeeDeptDTO.getEmpNo() != null) {
				errorMsg = "Employee No. already exist";
				RequestContext.getCurrentInstance().update("frmerrorMsge");
				RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
				employeeDTO.setEmpNo(null);
			}
		}

	}

	public void empNumberValidator() {
		if (employeeDTO.getEmpNo() != null && !employeeDTO.getEmpNo().isEmpty()) {
			Pattern ptr = Pattern.compile("^[a-zA-Z0-9\\-]*$");
			boolean valid = ptr.matcher(employeeDTO.getEmpNo()).matches();
			if (valid) {

			} else {
				errorMsg = "Invalid Employee Number";
				RequestContext.getCurrentInstance().update("frmerrorMsge");
				RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
				employeeDTO.setEmpNo(null);
			}

		}

	}

	public void chkDuplicateEPF() {

		if (employeeDTO.getEmpEpfNo() != null && !employeeDTO.getEmpEpfNo().isEmpty()
				&& !employeeDTO.getEmpEpfNo().equalsIgnoreCase("")) {
			String strEpfNo = employeeProfileService.isEpfNoAvilable(employeeDTO.getEmpEpfNo());
			if (strEpfNo != null) {
				errorMsg = "EPF No. already exist";
				RequestContext.getCurrentInstance().update("frmerrorMsge");
				RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
				employeeDTO.setEmpEpfNo(null);
			}
		}
	}

	public void emailValidation() {

		if (employeeDTO.getEmail() != null && !employeeDTO.getEmail().isEmpty()
				&& !employeeDTO.getEmail().equalsIgnoreCase("")) {
			boolean resultValidateEmail = UtilityClass.isValidEmailAddress(employeeDTO.getEmail());
			if (resultValidateEmail == false) {
				errorMsg = "Invalid Email Address";
				RequestContext.getCurrentInstance().update("frmerrorMsge");
				RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
				employeeDTO.setEmail(null);
			}
		}
	}

	public void saveEmployee() {

		employeeDTO.setTitle(strSelectedTitle);
		employeeDTO.setCarder(strSelectedCarder);
		employeeDTO.setQualification(strSelectedQualification);
		employeeDTO.setLocation(strSelectedLocation);
		employeeDTO.setMaritalStatus(strSelectedMartialStatus);
		employeeDTO.setGender(strSelectedGenderStatus);
		employeeDTO.setCreatedBy(sessionBackingBean.loginUser);
		employeeDTO.setFullName(
				employeeDTO.getFirstName() + " " + employeeDTO.getMiddleName() + " " + employeeDTO.getSureName());
		employeeDTO.setIsInitial("N");
		employeeDTO.setImage(this.image);

		if (employeeDTO.getTitle() != null && !employeeDTO.getTitle().isEmpty()
				&& !employeeDTO.getTitle().equalsIgnoreCase("")) {
			if (employeeDTO.getFirstName() != null && !employeeDTO.getFirstName().isEmpty()
					&& !employeeDTO.getFirstName().equalsIgnoreCase("")) {
				if (employeeDTO.getSureName() != null && !employeeDTO.getSureName().isEmpty()
						&& !employeeDTO.getSureName().equalsIgnoreCase("")) {
					if (employeeDTO.getMobileNo() != null && !employeeDTO.getMobileNo().isEmpty()
							&& !employeeDTO.getMobileNo().equalsIgnoreCase("")) {
						if (employeeDTO.getEmpNo() != null && !employeeDTO.getEmpNo().isEmpty()
								&& !employeeDTO.getEmpNo().equalsIgnoreCase("")) {
							if (employeeDTO.getEmpEpfNo() != null && !employeeDTO.getEmpEpfNo().isEmpty()
									&& !employeeDTO.getEmpEpfNo().equalsIgnoreCase("")) {
								if (employeeDTO.getEmail() != null && !employeeDTO.getEmail().isEmpty()
										&& !employeeDTO.getEmail().equalsIgnoreCase("")) {
									if (employeeDTO.getGender() != null && !employeeDTO.getGender().isEmpty()
											&& !employeeDTO.getGender().equalsIgnoreCase("")) {
										empolyeeDeptDTO = employeeProfileService
												.isEmpNoAvilable(employeeDTO.getEmpNo());
										if (employeeDTO.getDob() != null) {

											if (empolyeeDeptDTO.getEmpNo() != null) {
												errorMsg = "Employee No. already exist";
												RequestContext.getCurrentInstance().update("frmerrorMsge");
												RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
											} else {
												String strEpfNo = employeeProfileService
														.isEpfNoAvilable(employeeDTO.getEmpEpfNo());
												if (strEpfNo != null) {
													errorMsg = "EPF No. already exist";
													RequestContext.getCurrentInstance().update("frmerrorMsge");
													RequestContext.getCurrentInstance()
															.execute("PF('errorMsge').show()");
												} else {

													if (employeeDTO.getNicNo() != null
															&& !employeeDTO.getNicNo().isEmpty()
															&& !employeeDTO.getNicNo().equalsIgnoreCase("")) {
														java.sql.Date Dob = new java.sql.Date(
																employeeDTO.getDob().getTime());

														boolean resultValidateEmail = UtilityClass
																.isValidEmailAddress(employeeDTO.getEmail());
														if (resultValidateEmail) {
															boolean resultValidate = NICValidator.validateNIC(
																	employeeDTO.getNicNo(), employeeDTO.getGender(),
																	Dob);
															if (resultValidate) {

																employeeDTO.setStatus("P");
																employeeDTO.setUserStatus("P");
																int result = employeeProfileService
																		.saveEmployee(employeeDTO);
																employeeDTO.setStatus("Pending");
																if (result == 0) {
																	RequestContext.getCurrentInstance()
																			.update("frmEmpDetails");
																	RequestContext.getCurrentInstance()
																			.execute("PF('successSve').show()");
																	boolEmpAddDet = false;
																	boolEmpDesigDet = false;
																	boolEmpDeptDet = false;
																	disable = true;

																} else {
																	RequestContext.getCurrentInstance()
																			.execute("PF('generalError').show()");
																}
															} else {
																errorMsg = "Invalid NIC No.";
																RequestContext.getCurrentInstance()
																		.update("frmerrorMsge");
																RequestContext.getCurrentInstance()
																		.execute("PF('errorMsge').show()");
															}
														} else {
															errorMsg = "Invalid Email Address";
															RequestContext.getCurrentInstance().update("frmerrorMsge");
															RequestContext.getCurrentInstance()
																	.execute("PF('errorMsge').show()");
														}
													} else {

														boolean resultValidateEmail = UtilityClass
																.isValidEmailAddress(employeeDTO.getEmail());
														if (resultValidateEmail) {

															employeeDTO.setStatus("P");
															employeeDTO.setUserStatus("P");
															int result = employeeProfileService
																	.saveEmployee(employeeDTO);
															employeeDTO.setStatus("Pending");
															if (result == 0) {
																RequestContext.getCurrentInstance()
																		.update("frmEmpDetails");
																RequestContext.getCurrentInstance()
																		.execute("PF('successSve').show()");
																boolEmpAddDet = false;
																boolEmpDesigDet = false;
																boolEmpDeptDet = false;
																disable = true;

															} else {
																RequestContext.getCurrentInstance()
																		.execute("PF('generalError').show()");
															}
														} else {
															errorMsg = "Invalid Email Address";
															RequestContext.getCurrentInstance().update("frmerrorMsge");
															RequestContext.getCurrentInstance()
																	.execute("PF('errorMsge').show()");

														}
													}

												}

											}
										} else {
											errorMsg = "DOB should be selected";
											RequestContext.getCurrentInstance().update("frmrequiredField");
											RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
										}
									} else {
										errorMsg = "Gender should be selected";
										RequestContext.getCurrentInstance().update("frmrequiredField");
										RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
									}
								} else {
									errorMsg = "Email Address should be entered";
									RequestContext.getCurrentInstance().update("frmrequiredField");
									RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
								}
							} else {
								errorMsg = "EPF No. should be entered";
								RequestContext.getCurrentInstance().update("frmrequiredField");
								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
							}

						} else {
							errorMsg = "Employee No. should be entered";
							RequestContext.getCurrentInstance().update("frmrequiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
						}
					} else {
						errorMsg = "Mobile No. should be entered";
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}
				} else {
					errorMsg = "Surname should be entered";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			} else {
				errorMsg = "First Name should be entered";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		} else {
			errorMsg = "Title should be selected";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		}

	}

	public void searchEmployee() {

		employeeProfileService = (EmployeeProfileService) SpringApplicationContex.getBean("employeeProfileService");

		employeeDTO = employeeProfileService.getEmployeeDetails(sessionBackingBean.getEmployeeNo());
		if (employeeDTO.getFirstName() != null && !employeeDTO.getFirstName().isEmpty()
				&& !employeeDTO.getFirstName().equalsIgnoreCase("")) {
			boolEmpDet = true;
			boolEmpAddDet = false;
			boolEmpDesigDet = false;
			boolEmpDeptDet = false;

			strSelectedTitle = employeeDTO.getTitle();
			strSelectedQualification = employeeDTO.getQualification();
			strSelectedGenderStatus = employeeDTO.getGender();
			strSelectedMartialStatus = employeeDTO.getMaritalStatus();
			strSelectedLocation = employeeDTO.getLocation();
			strSelectedCarder = employeeDTO.getCarder();
			image = employeeDTO.getImage();

			employeeAddresslist = employeeProfileService.findAddressDetByEmpNo(employeeDTO.getEmpNo());
			employeeDepartmentlist = employeeProfileService.findDeparmentDetByEmpNo(employeeDTO.getEmpNo());
			employeeDesignationlist = employeeProfileService.findDesignationDetByEmpNo(employeeDTO.getEmpNo());
		} else {
			RequestContext.getCurrentInstance().update("frmnoDataMsge");
			RequestContext.getCurrentInstance().execute("PF('noDataMsge').show()");
		}

	}

	public void updateEmployee() {
		employeeDTO.setModifiedBy(sessionBackingBean.loginUser);
		employeeDTO.setCarder(strSelectedCarder);
		employeeDTO.setQualification(strSelectedQualification);
		employeeDTO.setLocation(strSelectedLocation);
		employeeDTO.setTitle(strSelectedTitle);
		employeeDTO.setMaritalStatus(strSelectedMartialStatus);
		employeeDTO.setGender(strSelectedGenderStatus);
		employeeDTO.setImage(this.image);

		if (employeeDTO.getTitle() != null && !employeeDTO.getTitle().isEmpty()
				&& !employeeDTO.getTitle().equalsIgnoreCase("")) {
			if (employeeDTO.getFirstName() != null && !employeeDTO.getFirstName().isEmpty()
					&& !employeeDTO.getFirstName().equalsIgnoreCase("")) {
				if (employeeDTO.getSureName() != null && !employeeDTO.getSureName().isEmpty()
						&& !employeeDTO.getSureName().equalsIgnoreCase("")) {

					if (employeeDTO.getMobileNo() != null && !employeeDTO.getMobileNo().isEmpty()
							&& !employeeDTO.getMobileNo().equalsIgnoreCase("")) {

						if (employeeDTO.getEmpNo() != null && !employeeDTO.getEmpNo().isEmpty()
								&& !employeeDTO.getEmpNo().equalsIgnoreCase("")) {
							if (employeeDTO.getEmpEpfNo() != null && !employeeDTO.getEmpEpfNo().isEmpty()
									&& !employeeDTO.getEmpEpfNo().equalsIgnoreCase("")) {
								if (employeeDTO.getEmail() != null && !employeeDTO.getEmail().isEmpty()
										&& !employeeDTO.getEmail().equalsIgnoreCase("")) {
									if (employeeDTO.getGender() != null && !employeeDTO.getGender().isEmpty()
											&& !employeeDTO.getGender().equalsIgnoreCase("")) {
										empolyeeDeptDTO = employeeProfileService
												.isEmpNoAvilable(employeeDTO.getEmpNo());
										if (employeeDTO.getDob() != null) {

											boolean resultValidateEmail = UtilityClass
													.isValidEmailAddress(employeeDTO.getEmail());
											if (resultValidateEmail) {

												if (employeeDTO.getNicNo() != null && !employeeDTO.getNicNo().isEmpty()
														&& !employeeDTO.getNicNo().equalsIgnoreCase("")) {
													java.sql.Date Dob = new java.sql.Date(
															employeeDTO.getDob().getTime());
													boolean resultValidate = NICValidator.validateNIC(
															employeeDTO.getNicNo(), employeeDTO.getGender(), Dob);
													if (resultValidate) {

														int result = employeeProfileService.updateEmployee(employeeDTO);
														if (result == 0) {
															RequestContext.getCurrentInstance().update("frmEmpDetails");
															RequestContext.getCurrentInstance()
																	.execute("PF('successSve').show()");
														} else {
															RequestContext.getCurrentInstance()
																	.execute("PF('generalError').show()");
														}
													} else {
														errorMsg = "Invalid NIC No.";
														RequestContext.getCurrentInstance().update("frmerrorMsge");
														RequestContext.getCurrentInstance()
																.execute("PF('errorMsge').show()");
													}
												} else {
													int result = employeeProfileService.updateEmployee(employeeDTO);
													if (result == 0) {
														RequestContext.getCurrentInstance().update("frmEmpDetails");
														RequestContext.getCurrentInstance()
																.execute("PF('successSve').show()");
													} else {
														RequestContext.getCurrentInstance()
																.execute("PF('generalError').show()");
													}
												}
											} else {
												errorMsg = "Invalid Email Address";
												RequestContext.getCurrentInstance().update("frmerrorMsge");
												RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
											}

										} else {
											errorMsg = "DOB should be selected";
											RequestContext.getCurrentInstance().update("frmrequiredField");
											RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
										}
									} else {
										errorMsg = "Gender should be selected";
										RequestContext.getCurrentInstance().update("frmrequiredField");
										RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
									}
								} else {
									errorMsg = "Email Address should be entered";
									RequestContext.getCurrentInstance().update("frmrequiredField");
									RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
								}
							} else {
								errorMsg = "EPF No. should be entered";
								RequestContext.getCurrentInstance().update("frmrequiredField");
								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
							}
						} else {
							errorMsg = "Employee No. should be entered";
							RequestContext.getCurrentInstance().update("frmrequiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
						}
					} else {
						errorMsg = "Mobile No. should be entered";
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}
				} else {
					errorMsg = "Surname should be entered";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			} else {
				errorMsg = "First Name should be entered";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		} else {
			errorMsg = "Title should be selected";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public void moveNextPage() {
		String approveURL = sessionBackingBean.getApproveURL();
		String searchURL = sessionBackingBean.getSearchURL();

		if (approveURL != null) {
			try {
				sessionBackingBean.setApproveURLStatus(false);
				sessionBackingBean.employeeNo = null;
				FacesContext.getCurrentInstance().getExternalContext().redirect(approveURL);
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else if (searchURL != null) {
			try {
				sessionBackingBean.setSearchURLStatus(false);
				sessionBackingBean.employeeNo = null;
				FacesContext.getCurrentInstance().getExternalContext().redirect(searchURL);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	// Address Tab
	public void addEmpAddress() {
		if (strSelectedAddressType != null && !strSelectedAddressType.isEmpty()
				&& !strSelectedAddressType.equalsIgnoreCase("")) {
			if (empAddressDTO.getAddress1() != null && !empAddressDTO.getAddress1().isEmpty()
					&& !empAddressDTO.getAddress1().equalsIgnoreCase("")) {
				if (strSelectedCity != null && !strSelectedCity.isEmpty() && !strSelectedCity.equalsIgnoreCase("")) {

					if (empAddressDTO.getSeq() != 0) {
						// update

						empAddressDTO.setStatus("A");
						empAddressDTO.setModifiedBy(sessionBackingBean.loginUser);
						empAddressDTO.setAddressType(strSelectedAddressType);
						empAddressDTO.setCity(strSelectedCity);
						empAddressDTO.setEmpNo(employeeDTO.getEmpNo());

						int result = employeeProfileService.updateAddressDetails(empAddressDTO);
						if (result == 0) {
							RequestContext.getCurrentInstance().execute("PF('successSve').show()");
							clearEmpAddress();
							employeeAddresslist = employeeProfileService
									.findAddressDetByEmpNo(empAddressDTO.getEmpNo());

						} else {
							RequestContext.getCurrentInstance().execute("PF('generalError').show()");

						}

					} else {
						boolean isExistAddress = false;
						// insert
						if (!employeeAddresslist.isEmpty()) {

							for (EmployeeAddressDTO empaddress : employeeAddresslist) {
								if (empaddress.getAddressType().equalsIgnoreCase(strSelectedAddressType)) {
									isExistAddress = true;
								}
								if (!isExistAddress) {
									isExistAddress = false;
								}
							}

							if (isExistAddress) {
								// duplicate
								errorMsg = "Duplicate Address Type";
								RequestContext.getCurrentInstance().update("frmerrorMsge");
								RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
							} else {

								empAddressDTO.setStatus("A");
								empAddressDTO.setCreatedBy(sessionBackingBean.loginUser);
								empAddressDTO.setAddressType(strSelectedAddressType);
								empAddressDTO.setCity(strSelectedCity);
								empAddressDTO.setEmpNo(employeeDTO.getEmpNo());

								int result = employeeProfileService.saveEmpAddress(empAddressDTO);
								if (result == 0) {
									RequestContext.getCurrentInstance().execute("PF('successSve').show()");
									clearEmpAddress();
									employeeAddresslist = employeeProfileService
											.findAddressDetByEmpNo(empAddressDTO.getEmpNo());

								} else {
									RequestContext.getCurrentInstance().execute("PF('generalError').show()");

								}
							}

						} else {

							empAddressDTO.setStatus("A");
							empAddressDTO.setCreatedBy(sessionBackingBean.loginUser);
							empAddressDTO.setAddressType(strSelectedAddressType);
							empAddressDTO.setCity(strSelectedCity);
							empAddressDTO.setEmpNo(employeeDTO.getEmpNo());

							int result = employeeProfileService.saveEmpAddress(empAddressDTO);
							if (result == 0) {
								RequestContext.getCurrentInstance().execute("PF('successSve').show()");
								clearEmpAddress();
								employeeAddresslist = employeeProfileService
										.findAddressDetByEmpNo(empAddressDTO.getEmpNo());

							} else {
								RequestContext.getCurrentInstance().execute("PF('generalError').show()");

							}
						}

					}

				} else {
					errorMsg = "City should be selected";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			} else {
				errorMsg = "Address1 should be entered";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		} else {
			errorMsg = "Address Type should be selected";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public void clearEmpAddress() {

		strSelectedAddressType = null;
		strSelectedCity = null;
		addtypeList = employeeProfileService.GetAddressTypeToDropdown();
		cityList = employeeProfileService.GetCityToDropdown();
		empAddressDTO.setAddress1(null);
		empAddressDTO.setAddress2(null);
		boolDisableAddType = false;
		employeeAddresslist = employeeProfileService.findAddressDetByEmpNo(employeeDTO.getEmpNo());

	}

	public void editAction() {

		empAddressDTO.setAddress1(strSelectedAddressSeq.getAddress1());
		empAddressDTO.setAddress2(strSelectedAddressSeq.getAddress2());
		empAddressDTO.setSeq(strSelectedAddressSeq.getSeq());
		strSelectedAddressType = (strSelectedAddressSeq.getAddressType());
		strSelectedCity = (strSelectedAddressSeq.getCity());
		empAddressDTO.setCreatedBy(strSelectedAddressSeq.getCreatedBy());
		empAddressDTO.setCratedDate(strSelectedAddressSeq.getCratedDate());
		boolDisableAddType = true;
	}

	public void deleteAddButtonAction() {
		RequestContext.getCurrentInstance().execute("PF('deleteconfirmation').show()");
	}

	public void addressDeleteAction() {
		int result = employeeProfileService.statusUpdate(strSelectedAddressSeq.getSeq());

		if (result == 0) {

			clearEmpAddress();
			RequestContext.getCurrentInstance().update(":frmAddDetails");

			employeeAddresslist = employeeProfileService.findAddressDetByEmpNo(strSelectedAddressSeq.getEmpNo());
			RequestContext.getCurrentInstance().execute("PF('successSveDel').show()");
			activeTabIndex = 1;

		} else {
			RequestContext.getCurrentInstance().execute("PF('generalError').show()");

		}

	}

	// Department Tab
	public boolean validateTranDate() {
		boolean strComparewithAppDate = false;

		if ((employeeDTO.getAppoinmentDate() != null) && (empolyeeDeptDTO.getStartDate() != null)
				&& (employeeDTO.getAppoinmentDate().before(empolyeeDeptDTO.getStartDate()))) {
			errorMsg = "Transfer Date should be greater than Appointment Date";
			RequestContext.getCurrentInstance().update("frmerrorMsge");
			RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
			employeeDTO.setAppoinmentDate(null);
			strComparewithAppDate = true;
		} else if ((employeeDTO.getRecruitmentDate() != null) && (empolyeeDeptDTO.getStartDate() != null)
				&& (empolyeeDeptDTO.getStartDate().before(employeeDTO.getRecruitmentDate()))) {
			errorMsg = "Transfer Date should be greater than Recruitment Date";
			RequestContext.getCurrentInstance().update("frmerrorMsge");
			RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
			employeeDTO.setAppoinmentDate(null);
			strComparewithAppDate = true;
		} else if ((employeeDTO.getPermanentDate() != null) && (empolyeeDeptDTO.getStartDate() != null)
				&& (empolyeeDeptDTO.getStartDate().before(employeeDTO.getPermanentDate()))) {
			errorMsg = "Transfer Date should be greater than Permanent Date";
			RequestContext.getCurrentInstance().update("frmerrorMsge");
			RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
			employeeDTO.setAppoinmentDate(null);
			strComparewithAppDate = true;
		} else
			strComparewithAppDate = false;

		return strComparewithAppDate;
	}

	public void addDepDetails() {
		empolyeeDeptDTO.setDeptCode(strSelectedDeparmentCode);
		empolyeeDeptDTO.setUnitCode(strSelectedUnit);
		if (empolyeeDeptDTO.getDeptCode() != null && !empolyeeDeptDTO.getDeptCode().isEmpty()
				&& !empolyeeDeptDTO.getDeptCode().equalsIgnoreCase("")) {
			if (empolyeeDeptDTO.getUnitCode() != null && !empolyeeDeptDTO.getUnitCode().isEmpty()
					&& !empolyeeDeptDTO.getUnitCode().equalsIgnoreCase("")) {
				if (empolyeeDeptDTO.getSeq() != 0) {
					// update

					if (empolyeeDeptDTO.getStartDate() == null)
						empolyeeDeptDTO.setStatus("A");
					else
						empolyeeDeptDTO.setStatus("I");

					boolean dateValidation = validateTranDate();
					if (dateValidation) {
						return;
					}

					empolyeeDeptDTO.setModifiedBy(sessionBackingBean.loginUser);
					empolyeeDeptDTO.setDeptCode(strSelectedDeparmentCode);
					empolyeeDeptDTO.setUnitCode(strSelectedUnit);
					empolyeeDeptDTO.setEmpNo(employeeDTO.getEmpNo());

					int result = employeeProfileService.updateDepDetails(empolyeeDeptDTO);
					if (result == 0) {
						RequestContext.getCurrentInstance().execute("PF('successSve').show()");
						clearDepDetails();
						employeeDepartmentlist = employeeProfileService.findDeparmentDetByEmpNo(employeeDTO.getEmpNo());

					} else {
						RequestContext.getCurrentInstance().execute("PF('generalError').show()");

					}

				} else {

					if (employeeDepartmentlist.isEmpty()) {

						empolyeeDeptDTO.setStatus("A");
						empolyeeDeptDTO.setCreatedBy(sessionBackingBean.loginUser);
						empolyeeDeptDTO.setDeptCode(strSelectedDeparmentCode);
						empolyeeDeptDTO.setUnitCode(strSelectedUnit);
						empolyeeDeptDTO.setEmpNo(employeeDTO.getEmpNo());

						boolean dateValidation = validateTranDate();
						if (dateValidation) {
							return;
						}

						int result = employeeProfileService.saveDepDetais(empolyeeDeptDTO);
						if (result == 0) {
							RequestContext.getCurrentInstance().execute("PF('successSve').show()");
							clearDepDetails();
							employeeDepartmentlist = employeeProfileService
									.findDeparmentDetByEmpNo(employeeDTO.getEmpNo());

						} else {
							RequestContext.getCurrentInstance().execute("PF('generalError').show()");

						}
					} else {
						boolean isExist = false;
						for (EmployeeDeptDTO empdeparment : employeeDepartmentlist) {
							if (!empdeparment.getDeptCode().equalsIgnoreCase(strSelectedDeparmentCode)
									&& empdeparment.getStartDate() == null) {
								isExist = true;
							}
							if (!isExist) {
								isExist = false;
							}
						}
						if (isExist) {
							errorMsg = "Previous Department's Transfer Date should be updated when allocating to another department";
							RequestContext.getCurrentInstance().update("frmerrorMsge");
							RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
						} else {
							boolean isExistN = false;
							for (EmployeeDeptDTO empdepartment : employeeDepartmentlist) {
								if (empdepartment.getDeptCode().equalsIgnoreCase(strSelectedDeparmentCode)
										&& empdepartment.getUnitCode().equalsIgnoreCase(strSelectedUnit)
										&& empdepartment.getStatus().equals("Active")) {
									isExistN = true;
								}
								if (!isExistN) {
									isExistN = false;
								}
							}

							if (isExistN) {
								// Duplicate Record.
								errorMsg = "Duplicate data";
								RequestContext.getCurrentInstance().update("frmerrorMsge");
								RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
							} else {

								empolyeeDeptDTO.setStatus("A");
								empolyeeDeptDTO.setCreatedBy(sessionBackingBean.loginUser);
								empolyeeDeptDTO.setDeptCode(strSelectedDeparmentCode);
								empolyeeDeptDTO.setUnitCode(strSelectedUnit);
								empolyeeDeptDTO.setEmpNo(employeeDTO.getEmpNo());

								boolean dateValidation = validateTranDate();
								if (dateValidation) {
									return;
								}

								int result = employeeProfileService.saveDepDetais(empolyeeDeptDTO);
								if (result == 0) {
									RequestContext.getCurrentInstance().execute("PF('successSve').show()");
									clearDepDetails();
									employeeDepartmentlist = employeeProfileService
											.findDeparmentDetByEmpNo(employeeDTO.getEmpNo());

								} else {
									RequestContext.getCurrentInstance().execute("PF('generalError').show()");

								}
							}
						}
					}
				}
			} else {
				errorMsg = "Sub Unit Name should be selected";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		} else {
			errorMsg = "Deparment Name should be selected";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public void clearDepDetails() {

		strSelectedDeparmentCode = null;
		strSelectedUnit = null;
		deparmentList = employeeProfileService.GetDepartmentsToDropdown();
		unitList = employeeProfileService.GetUnitsToDropdown();
		empolyeeDeptDTO.setStartDate(null);
		boolDisableDeptName = false;
		boolDisableUnitName = false;
		empolyeeDeptDTO = new EmployeeDeptDTO();

	}

	public void editDeptAction() {

		empolyeeDeptDTO.setStartDate(strSelectedDeptSeq.getStartDate());
		empolyeeDeptDTO.setEndDate(strSelectedDeptSeq.getEndDate());
		empolyeeDeptDTO.setStatus(strSelectedDeptSeq.getStatus());
		empolyeeDeptDTO.setSeq(strSelectedDeptSeq.getSeq());
		strSelectedDeparmentCode = (strSelectedDeptSeq.getDeptCode());
		strSelectedUnit = (strSelectedDeptSeq.getUnitCode());
		empolyeeDeptDTO.setCreatedBy(strSelectedDeptSeq.getCreatedBy());
		empolyeeDeptDTO.setCratedDate(strSelectedDeptSeq.getCratedDate());
		boolDisableUnitName = true;
		boolDisableDeptName = true;
	}

	public void deleteDeptButtonAction() {
		RequestContext.getCurrentInstance().execute("PF('deleteconfirmationDept').show()");
	}

	public void departmentDeleteAction() {

		int result = employeeProfileService.statusUpdateDept(strSelectedDeptSeq.getSeq());
		if (result == 0) {

			clearEmpAddress();
			employeeDepartmentlist = employeeProfileService.findDeparmentDetByEmpNo(employeeDTO.getEmpNo());
			RequestContext.getCurrentInstance().execute("PF('successSveDel').show()");
			activeTabIndex = 2;

		} else {
			RequestContext.getCurrentInstance().execute("PF('generalError').show()");

		}

	}

	// Designation Tab
	public void addDesignationDetails() {
		employeeDesignationDTO.setDesignationCode(strSelectedDesignation);
		employeeDesignationDTO.setSalCatCode(strSelectedSalaryCat);
		employeeDesignationDTO.setGradeCode(strSelectedGrade);

		if (employeeDesignationDTO.getDesignationCode() != null
				&& !employeeDesignationDTO.getDesignationCode().isEmpty()
				&& !employeeDesignationDTO.getDesignationCode().equalsIgnoreCase("")) {
			if (employeeDesignationDTO.getGradeCode() != null && !employeeDesignationDTO.getGradeCode().isEmpty()
					&& !employeeDesignationDTO.getGradeCode().equalsIgnoreCase("")) {
				if (employeeDesignationDTO.getSalCatCode() != null && !employeeDesignationDTO.getSalCatCode().isEmpty()
						&& !employeeDesignationDTO.getSalCatCode().equalsIgnoreCase("")) {
					if (employeeDesignationDTO.getStartDate() != null) {
						if (employeeDesignationDTO.getEndDate() != null) {
							if (employeeDesignationDTO.getEndDate().before(employeeDesignationDTO.getStartDate())) {
								errorMsg = "Designation To date cannot be greater than Designated From date";
								RequestContext.getCurrentInstance().update("frmerrorMsge");
								RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
							} else {
								if (employeeDesignationDTO.getSeq() != 0) {
									// update

									if (employeeDesignationDTO.getEndDate() == null) {
										employeeDesignationDTO.setStatus("A");
									} else
										employeeDesignationDTO.setStatus("I");

									// employeeDesignationDTO.setStatus("A");
									employeeDesignationDTO.setModifiedBy(sessionBackingBean.loginUser);
									employeeDesignationDTO.setDesignationCode(strSelectedDesignation);
									employeeDesignationDTO.setGradeCode(strSelectedGrade);
									employeeDesignationDTO.setSalCatCode(strSelectedSalaryCat);
									employeeDesignationDTO.setEmpNo(employeeDTO.getEmpNo());

									int result = employeeProfileService.updateDesigDetails(employeeDesignationDTO);
									if (result == 0) {
										RequestContext.getCurrentInstance().execute("PF('successSve').show()");
										clearDesignationDetails();
										employeeDesignationlist = employeeProfileService
												.findDesignationDetByEmpNo(employeeDTO.getEmpNo());

									} else {
										RequestContext.getCurrentInstance().execute("PF('generalError').show()");

									}

								} else {
									boolean isExistDesig = false;
									// insert
									if (employeeDesignationlist != null) {
										for (EmployeeDesignationDTO empdesignation : employeeDesignationlist) {
											if (empdesignation.getDesignationCode()
													.equalsIgnoreCase(strSelectedDesignation)
													&& empdesignation.getGradeCode().equalsIgnoreCase(strSelectedGrade)
													&& empdesignation.getSalCatCode()
															.equalsIgnoreCase(strSelectedSalaryCat)) {
												isExistDesig = true;
											}
											if (!isExistDesig) {
												isExistDesig = false;
											}
										}

										if (isExistDesig) {
											// Duplicate Record.
											errorMsg = "Duplicate data";
											RequestContext.getCurrentInstance().update("frmerrorMsge");
											RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
										} else {

											boolean isExist = false;
											for (EmployeeDesignationDTO empdesignation : employeeDesignationlist) {
												if (empdesignation.getDesignationCode()
														.equalsIgnoreCase(strSelectedDesignation)
														&& empdesignation.getStatus().equals("Active")) {
													isExist = true;
												}
												if (!isExist) {
													isExist = false;
												}
											}

											if (isExist) {
												errorMsg = "Previous Designation Status should be updated as Inactive when allocating to another designation";
												RequestContext.getCurrentInstance().update("frmerrorMsge");
												RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
											} else {

												employeeDesignationDTO.setStatus("A");
												employeeDesignationDTO.setCreatedBy(sessionBackingBean.loginUser);
												employeeDesignationDTO.setDesignationCode(strSelectedDesignation);
												employeeDesignationDTO.setGradeCode(strSelectedGrade);
												employeeDesignationDTO.setSalCatCode(strSelectedSalaryCat);
												employeeDesignationDTO.setEmpNo(employeeDTO.getEmpNo());

												int result = employeeProfileService
														.saveDesigDetais(employeeDesignationDTO);
												if (result == 0) {
													RequestContext.getCurrentInstance()
															.execute("PF('successSve').show()");
													clearDesignationDetails();
													employeeDesignationlist = employeeProfileService
															.findDesignationDetByEmpNo(employeeDTO.getEmpNo());

												} else {
													RequestContext.getCurrentInstance()
															.execute("PF('generalError').show()");

												}
											}
										}
									}
								}
							}
						} else {
							if (employeeDesignationlist.isEmpty()) {
								insertUpdateDesignation();
							} else {
								boolean isExist = false;
								for (EmployeeDesignationDTO empdesignation : employeeDesignationlist) {
									if (empdesignation.getDesignationCode().equalsIgnoreCase(strSelectedDesignation)
											&& empdesignation.getEndDate() == null) {
										isExist = true;
									}
									if (!isExist) {
										isExist = false;
									}
								}

								if (isExist) {
									errorMsg = "Previous Designated To Date should be updated when allocating to another designation";
									RequestContext.getCurrentInstance().update("frmerrorMsge");
									RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
								} else {

									/* insertUpdateDesignation(); */
									for (EmployeeDesignationDTO empdesignation : employeeDesignationlist) {
										if (!empdesignation.getDesignationCode()
												.equalsIgnoreCase(strSelectedDesignation)
												&& empdesignation.getEndDate() == null) {
											isExist = true;
										}
										if (!isExist) {
											isExist = false;
										}
									}
									if (isExist) {
										errorMsg = "Previous Designated To Date should be updated when allocating to another designation";
										RequestContext.getCurrentInstance().update("frmerrorMsge");
										RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
									} else {

										boolean isExistN = false;
										for (EmployeeDesignationDTO empdesignation : employeeDesignationlist) {
											if (empdesignation.getStatus().equals("Active")) {
												isExistN = true;
											}
											if (!isExistN) {
												isExistN = false;
											}
										}

										if (isExistN) {
											errorMsg = "Previous Designation Status should be updated as Inactive when allocating to another designation";
											RequestContext.getCurrentInstance().update("frmerrorMsge");
											RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
										} else {

											insertUpdateDesignation();
										}

									}
								}

							}

						}
					} else {
						errorMsg = "Designated From should be selected";
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}

				} else {
					errorMsg = "Salary Category should be selected";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			} else {
				errorMsg = "Grade should be selected";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		} else {
			errorMsg = "Designation should be selected";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public void clearDesignationDetails() {

		strSelectedDesignation = null;
		strSelectedGrade = null;
		strSelectedSalaryCat = null;
		designationList = employeeProfileService.GetDesignationToDropdown();
		salaryCatList = employeeProfileService.GetSalaryCatToDropdown();
		gradeList = employeeProfileService.GetEmpGradeToDropdown();
		employeeDesignationDTO.setStartDate(null);
		employeeDesignationDTO.setEndDate(null);
		employeeDesignationDTO = new EmployeeDesignationDTO();
		boolDesignation = false;
		boolGrade = false;
		boolSalCate = false;
	}

	public void editDesigAction() {

		employeeDesignationDTO.setStartDate(strSelectedDesSeq.getStartDate());
		employeeDesignationDTO.setEndDate(strSelectedDesSeq.getEndDate());
		employeeDesignationDTO.setStatus(strSelectedDesSeq.getStatus());
		employeeDesignationDTO.setSeq(strSelectedDesSeq.getSeq());
		strSelectedDesignation = (strSelectedDesSeq.getDesignationCode());
		strSelectedGrade = (strSelectedDesSeq.getGradeCode());
		strSelectedSalaryCat = (strSelectedDesSeq.getSalCatCode());
		employeeDesignationDTO.setCreatedBy(strSelectedDesSeq.getCreatedBy());
		employeeDesignationDTO.setCratedDate(strSelectedDesSeq.getCratedDate());
		boolDesignation = true;
		boolGrade = true;
		boolSalCate = true;
	}

	public void deleteDesigButtonAction() {
		RequestContext.getCurrentInstance().execute("PF('deleteconfirmationDesig').show()");
	}

	public void designationDeleteAction() {

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();

		String currentDate = (dateFormat.format(date));

		int result = employeeProfileService.statusUpdateDesig(strSelectedDesSeq.getSeq(), currentDate);
		if (result == 0) {
			clearEmpAddress();
			RequestContext.getCurrentInstance().execute("PF('successSveDel').show()");
			employeeDesignationlist = employeeProfileService.findDesignationDetByEmpNo(employeeDTO.getEmpNo());
			activeTabIndex = 3;
		} else {
			RequestContext.getCurrentInstance().execute("PF('generalError').show()");

		}

	}

	public void insertUpdateDesignation() {
		if (employeeDesignationDTO.getSeq() != 0) {
			// update
			if (employeeDesignationDTO.getEndDate() == null) {
				employeeDesignationDTO.setStatus("A");
			} else
				employeeDesignationDTO.setStatus("I");

			employeeDesignationDTO.setModifiedBy(sessionBackingBean.loginUser);
			employeeDesignationDTO.setDesignationCode(strSelectedDesignation);
			employeeDesignationDTO.setGradeCode(strSelectedGrade);
			employeeDesignationDTO.setSalCatCode(strSelectedSalaryCat);
			employeeDesignationDTO.setEmpNo(employeeDTO.getEmpNo());

			if (employeeDesignationDTO.getEndDate() == null) {
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				Date date = new Date();

				employeeDesignationDTO.setEndDate(date);
			}

			int result = employeeProfileService.updateDesigDetails(employeeDesignationDTO);
			if (result == 0) {
				RequestContext.getCurrentInstance().execute("PF('successSve').show()");
				clearDesignationDetails();
				employeeDesignationlist = employeeProfileService.findDesignationDetByEmpNo(employeeDTO.getEmpNo());

			} else {
				RequestContext.getCurrentInstance().execute("PF('generalError').show()");

			}

		} else {
			boolean isExistDesig = false;
			// insert
			if (!employeeDesignationlist.isEmpty()) {
				for (EmployeeDesignationDTO empdesignation : employeeDesignationlist) {
					if (empdesignation.getDesignationCode().equalsIgnoreCase(strSelectedDesignation)
							&& empdesignation.getGradeCode().equalsIgnoreCase(strSelectedGrade)
							&& empdesignation.getSalCatCode().equalsIgnoreCase(strSelectedSalaryCat)
							&& empdesignation.getStatus().equals("Active")) {
						isExistDesig = true;
					}
					if (!isExistDesig) {
						isExistDesig = false;
					}
				}

				if (isExistDesig) {
					// Duplicate Record.
					errorMsg = "Duplicate data";
					RequestContext.getCurrentInstance().update("frmerrorMsge");
					RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
				} else {
					employeeDesignationDTO.setStatus("A");
					employeeDesignationDTO.setCreatedBy(sessionBackingBean.loginUser);
					employeeDesignationDTO.setDesignationCode(strSelectedDesignation);
					employeeDesignationDTO.setGradeCode(strSelectedGrade);
					employeeDesignationDTO.setSalCatCode(strSelectedSalaryCat);
					employeeDesignationDTO.setEmpNo(employeeDTO.getEmpNo());

					int result = employeeProfileService.saveDesigDetais(employeeDesignationDTO);
					if (result == 0) {
						RequestContext.getCurrentInstance().execute("PF('successSve').show()");
						clearDesignationDetails();
						employeeDesignationlist = employeeProfileService
								.findDesignationDetByEmpNo(employeeDTO.getEmpNo());

					} else {
						RequestContext.getCurrentInstance().execute("PF('generalError').show()");

					}
				}
			}

			else {
				employeeDesignationDTO.setStatus("A");
				employeeDesignationDTO.setCreatedBy(sessionBackingBean.loginUser);
				employeeDesignationDTO.setDesignationCode(strSelectedDesignation);
				employeeDesignationDTO.setGradeCode(strSelectedGrade);
				employeeDesignationDTO.setSalCatCode(strSelectedSalaryCat);
				employeeDesignationDTO.setEmpNo(employeeDTO.getEmpNo());

				int result = employeeProfileService.saveDesigDetais(employeeDesignationDTO);
				if (result == 0) {
					RequestContext.getCurrentInstance().execute("PF('successSve').show()");
					clearDesignationDetails();
					employeeDesignationlist = employeeProfileService.findDesignationDetByEmpNo(employeeDTO.getEmpNo());

				} else {
					RequestContext.getCurrentInstance().execute("PF('generalError').show()");

				}
			}
		}
	}

	// handle changes
	public void handletitleChange() {

	}

	public void documentManagement() {
		if (editMode == true && viewMode == false) {
			checkUpload = true;
			checkView = false;
		}
		if (editMode == false && viewMode == true) {
			checkUpload = false;
			checkView = true;
		}
		if (editMode == false && viewMode == false) {
			checkUpload = true;
			checkView = true;
		}

		DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();

		try {

			sessionBackingBean.setEmpNo(employeeDTO.getEmpNo());
			sessionBackingBean.setEmpTransaction("USER MANAGEMENT");

			mandatoryList = documentManagementService.mandatoryDocsForUserManagement("11", employeeDTO.getEmpNo());
			optionalList = documentManagementService.optionalDocsForUserManagement("11", employeeDTO.getEmpNo());

			RequestContext.getCurrentInstance().execute("PF('uploadDocument').show()");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public List<CommonDTO> getTitleList() {
		return titleList;
	}

	public void setTitleList(List<CommonDTO> titleList) {
		this.titleList = titleList;
	}

	public String getStrSelectedTitle() {
		return strSelectedTitle;
	}

	public void setStrSelectedTitle(String strSelectedTitle) {
		this.strSelectedTitle = strSelectedTitle;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public List<CommonDTO> getCarderList() {
		return carderList;
	}

	public void setCarderList(List<CommonDTO> carderList) {
		this.carderList = carderList;
	}

	public List<CommonDTO> getQualificationList() {
		return qualificationList;
	}

	public void setQualificationList(List<CommonDTO> qualificationList) {
		this.qualificationList = qualificationList;
	}

	public List<CommonDTO> getGenderList() {
		return genderList;
	}

	public void setGenderList(List<CommonDTO> genderList) {
		this.genderList = genderList;
	}

	public List<CommonDTO> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<CommonDTO> locationList) {
		this.locationList = locationList;
	}

	public List<CommonDTO> getMartialList() {
		return martialList;
	}

	public void setMartialList(List<CommonDTO> martialList) {
		this.martialList = martialList;
	}

	public String getStrSelectedCarder() {
		return strSelectedCarder;
	}

	public void setStrSelectedCarder(String strSelectedCarder) {
		this.strSelectedCarder = strSelectedCarder;
	}

	public String getStrSelectedQualification() {
		return strSelectedQualification;
	}

	public void setStrSelectedQualification(String strSelectedQualification) {
		this.strSelectedQualification = strSelectedQualification;
	}

	public String getStrSelectedLocation() {
		return strSelectedLocation;
	}

	public void setStrSelectedLocation(String strSelectedLocation) {
		this.strSelectedLocation = strSelectedLocation;
	}

	public String getStrSelectedMartialStatus() {
		return strSelectedMartialStatus;
	}

	public void setStrSelectedMartialStatus(String strSelectedMartialStatus) {
		this.strSelectedMartialStatus = strSelectedMartialStatus;
	}

	public String getStrSelectedGenderStatus() {
		return strSelectedGenderStatus;
	}

	public void setStrSelectedGenderStatus(String strSelectedGenderStatus) {
		this.strSelectedGenderStatus = strSelectedGenderStatus;
	}

	public List<CommonDTO> getAddtypeList() {
		return addtypeList;
	}

	public void setAddtypeList(List<CommonDTO> addtypeList) {
		this.addtypeList = addtypeList;
	}

	public String getStrSelectedAddressType() {
		return strSelectedAddressType;
	}

	public void setStrSelectedAddressType(String strSelectedAddressType) {
		this.strSelectedAddressType = strSelectedAddressType;
	}

	public String getStrSelectedCity() {
		return strSelectedCity;
	}

	public void setStrSelectedCity(String strSelectedCity) {
		this.strSelectedCity = strSelectedCity;
	}

	public List<CommonDTO> getCityList() {
		return cityList;
	}

	public void setCityList(List<CommonDTO> cityList) {
		this.cityList = cityList;
	}

	public List<CommonDTO> getDeparmentList() {
		return deparmentList;
	}

	public void setDeparmentList(List<CommonDTO> deparmentList) {
		this.deparmentList = deparmentList;
	}

	public List<CommonDTO> getUnitList() {
		return unitList;
	}

	public void setUnitList(List<CommonDTO> unitList) {
		this.unitList = unitList;
	}

	public String getStrSelectedDeparmentCode() {
		return strSelectedDeparmentCode;
	}

	public void setStrSelectedDeparmentCode(String strSelectedDeparmentCode) {
		this.strSelectedDeparmentCode = strSelectedDeparmentCode;
	}

	public String getStrSelectedDeparmentDesc() {
		return strSelectedDeparmentDesc;
	}

	public void setStrSelectedDeparmentDesc(String strSelectedDeparmentDesc) {
		this.strSelectedDeparmentDesc = strSelectedDeparmentDesc;
	}

	public String getStrSelectedUnit() {
		return strSelectedUnit;
	}

	public void setStrSelectedUnit(String strSelectedUnit) {
		this.strSelectedUnit = strSelectedUnit;
	}

	public List<CommonDTO> getDesignationList() {
		return designationList;
	}

	public void setDesignationList(List<CommonDTO> designationList) {
		this.designationList = designationList;
	}

	public List<CommonDTO> getGradeList() {
		return gradeList;
	}

	public void setGradeList(List<CommonDTO> gradeList) {
		this.gradeList = gradeList;
	}

	public List<CommonDTO> getSalaryCatList() {
		return salaryCatList;
	}

	public void setSalaryCatList(List<CommonDTO> salaryCatList) {
		this.salaryCatList = salaryCatList;
	}

	public String getStrSelectedSalaryCat() {
		return strSelectedSalaryCat;
	}

	public void setStrSelectedSalaryCat(String strSelectedSalaryCat) {
		this.strSelectedSalaryCat = strSelectedSalaryCat;
	}

	public String getStrSelectedDesignation() {
		return strSelectedDesignation;
	}

	public void setStrSelectedDesignation(String strSelectedDesignation) {
		this.strSelectedDesignation = strSelectedDesignation;
	}

	public String getStrSelectedGrade() {
		return strSelectedGrade;
	}

	public void setStrSelectedGrade(String strSelectedGrade) {
		this.strSelectedGrade = strSelectedGrade;
	}

	public EmployeeDTO getEmployeeDTO() {
		return employeeDTO;
	}

	public void setEmployeeDTO(EmployeeDTO employeeDTO) {
		this.employeeDTO = employeeDTO;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

	public Boolean getBoolEmpDet() {
		return boolEmpDet;
	}

	public void setBoolEmpDet(Boolean boolEmpDet) {
		this.boolEmpDet = boolEmpDet;
	}

	public Boolean getBoolEmpAddDet() {
		return boolEmpAddDet;
	}

	public void setBoolEmpAddDet(Boolean boolEmpAddDet) {
		this.boolEmpAddDet = boolEmpAddDet;
	}

	public Boolean getBoolEmpDesigDet() {
		return boolEmpDesigDet;
	}

	public void setBoolEmpDesigDet(Boolean boolEmpDesigDet) {
		this.boolEmpDesigDet = boolEmpDesigDet;
	}

	public Boolean getBoolEmpDeptDet() {
		return boolEmpDeptDet;
	}

	public void setBoolEmpDeptDet(Boolean boolEmpDeptDet) {
		this.boolEmpDeptDet = boolEmpDeptDet;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public EmployeeAddressDTO getEmpAddressDTO() {
		return empAddressDTO;
	}

	public void setEmpAddressDTO(EmployeeAddressDTO empAddressDTO) {
		this.empAddressDTO = empAddressDTO;
	}

	public List<EmployeeAddressDTO> getEmployeeAddresslist() {
		return employeeAddresslist;
	}

	public void setEmployeeAddresslist(List<EmployeeAddressDTO> employeeAddresslist) {
		this.employeeAddresslist = employeeAddresslist;
	}

	public EmployeeAddressDTO getStrSelectedAddressSeq() {
		return strSelectedAddressSeq;
	}

	public void setStrSelectedAddressSeq(EmployeeAddressDTO strSelectedAddressSeq) {
		this.strSelectedAddressSeq = strSelectedAddressSeq;
	}

	public EmployeeDeptDTO getEmpolyeeDeptDTO() {
		return empolyeeDeptDTO;
	}

	public void setEmpolyeeDeptDTO(EmployeeDeptDTO empolyeeDeptDTO) {
		this.empolyeeDeptDTO = empolyeeDeptDTO;
	}

	public List<EmployeeDeptDTO> getEmployeeDepartmentlist() {
		return employeeDepartmentlist;
	}

	public void setEmployeeDepartmentlist(List<EmployeeDeptDTO> employeeDepartmentlist) {
		this.employeeDepartmentlist = employeeDepartmentlist;
	}

	public EmployeeDeptDTO getStrSelectedDeptSeq() {
		return strSelectedDeptSeq;
	}

	public void setStrSelectedDeptSeq(EmployeeDeptDTO strSelectedDeptSeq) {
		this.strSelectedDeptSeq = strSelectedDeptSeq;
	}

	public EmployeeDeptDTO getChkemployeeDTO() {
		return chkemployeeDTO;
	}

	public void setChkemployeeDTO(EmployeeDeptDTO chkemployeeDTO) {
		this.chkemployeeDTO = chkemployeeDTO;
	}

	public List<EmployeeDesignationDTO> getEmployeeDesignationlist() {
		return employeeDesignationlist;
	}

	public void setEmployeeDesignationlist(List<EmployeeDesignationDTO> employeeDesignationlist) {
		this.employeeDesignationlist = employeeDesignationlist;
	}

	public EmployeeDesignationDTO getEmployeeDesignationDTO() {
		return employeeDesignationDTO;
	}

	public void setEmployeeDesignationDTO(EmployeeDesignationDTO employeeDesignationDTO) {
		this.employeeDesignationDTO = employeeDesignationDTO;
	}

	public EmployeeDesignationDTO getStrSelectedDesSeq() {
		return strSelectedDesSeq;
	}

	public void setStrSelectedDesSeq(EmployeeDesignationDTO strSelectedDesSeq) {
		this.strSelectedDesSeq = strSelectedDesSeq;
	}

	private UploadedFile uploadedFile; // +getter+setter
	private byte[] image = null;

	public void fileUploadListener(FileUploadEvent e) {
		// Get uploaded file from the FileUploadEvent
		this.uploadedFile = e.getFile();
		image = uploadedFile.getContents();

		// Add message
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("File Uploaded Successfully"));
	}

	public EmployeeProfileService getEmployeeProfileService() {
		return employeeProfileService;
	}

	public void setEmployeeProfileService(EmployeeProfileService employeeProfileService) {
		this.employeeProfileService = employeeProfileService;
	}

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getImageContentsAsBase64() {
		if (image != null) {
			return Base64.getEncoder().encodeToString(image);
		}
		return null;

	}

	public Boolean getBoolDisableAddType() {
		return boolDisableAddType;
	}

	public void setBoolDisableAddType(Boolean boolDisableAddType) {
		this.boolDisableAddType = boolDisableAddType;
	}

	public Boolean getBoolDisableUnitName() {
		return boolDisableUnitName;
	}

	public void setBoolDisableUnitName(Boolean boolDisableUnitName) {
		this.boolDisableUnitName = boolDisableUnitName;
	}

	public Boolean getBoolDisableDeptName() {
		return boolDisableDeptName;
	}

	public void setBoolDisableDeptName(Boolean boolDisableDeptName) {
		this.boolDisableDeptName = boolDisableDeptName;
	}

	public Boolean getBoolDesignation() {
		return boolDesignation;
	}

	public void setBoolDesignation(Boolean boolDesignation) {
		this.boolDesignation = boolDesignation;
	}

	public Boolean getBoolGrade() {
		return boolGrade;
	}

	public void setBoolGrade(Boolean boolGrade) {
		this.boolGrade = boolGrade;
	}

	public Boolean getBoolSalCate() {
		return boolSalCate;
	}

	public void setBoolSalCate(Boolean boolSalCate) {
		this.boolSalCate = boolSalCate;
	}

	public Boolean getCreateMode() {
		return createMode;
	}

	public void setCreateMode(Boolean createMode) {
		this.createMode = createMode;
	}

	public Boolean getEditMode() {
		return editMode;
	}

	public void setEditMode(Boolean editMode) {
		this.editMode = editMode;
	}

	public Boolean getViewMode() {
		return viewMode;
	}

	public void setViewMode(Boolean viewMode) {
		this.viewMode = viewMode;
	}

	public Boolean getDisable() {
		return disable;
	}

	public void setDisable(Boolean disable) {
		this.disable = disable;
	}

	public int getActiveTabIndex() {
		return activeTabIndex;
	}

	public void setActiveTabIndex(int activeTabIndex) {
		this.activeTabIndex = activeTabIndex;
	}

	public DocumentManagementService getDocumentManagementService() {
		return documentManagementService;
	}

	public void setDocumentManagementService(DocumentManagementService documentManagementService) {
		this.documentManagementService = documentManagementService;
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

	public boolean isCheckUpload() {
		return checkUpload;
	}

	public void setCheckUpload(boolean checkUpload) {
		this.checkUpload = checkUpload;
	}

	public boolean isCheckView() {
		return checkView;
	}

	public void setCheckView(boolean checkView) {
		this.checkView = checkView;
	}

	public boolean isEditModeEpf() {
		return editModeEpf;
	}

	public void setEditModeEpf(boolean editModeEpf) {
		this.editModeEpf = editModeEpf;
	}

}
