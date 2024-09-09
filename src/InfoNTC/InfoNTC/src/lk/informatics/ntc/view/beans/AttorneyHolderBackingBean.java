package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.AttorneyHolderDTO;
import lk.informatics.ntc.model.dto.PermitDTO;
import lk.informatics.ntc.model.service.AdminService;
import lk.informatics.ntc.model.service.EmployeeProfileService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "attorneyBean")
@ViewScoped
public class AttorneyHolderBackingBean {
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	private AdminService adminService;
	String selectedLanguage;
	private String errorMsg;
	private String sucessMsg;
	boolean searchBtnChecked = false;
	boolean numCheckTel = false;
	boolean numCheckMobile = false;
	private boolean disabledGender = false;
	private boolean disabledDOB = false;
	private Boolean checkValiationsForInputFields = false;
	public List<AttorneyHolderDTO> attorneyList = new ArrayList<>(0);
	public List<AttorneyHolderDTO> attorneyCheckList = new ArrayList<>(0);
	public List<String> permitNoList = new ArrayList<String>(0);
	public List<String> applicationNoList = new ArrayList<String>(0);
	public List<String> vehicleNoList = new ArrayList<String>(0);
	private EmployeeProfileService employeeProfileService;
	private AttorneyHolderDTO attorneyDTO = new AttorneyHolderDTO();
	private String statusPermitcheck;
	private String statusApplicationcheck;
	private String statusVehiclecheck;
	private String searchErrorMessage;
	private PermitDTO permitDTO;

	public AttorneyHolderBackingBean() {

		employeeProfileService = (EmployeeProfileService) SpringApplicationContex.getBean("employeeProfileService");
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");
		permitNoList = employeeProfileService.getAllVehiclePermit();
		applicationNoList = employeeProfileService.getAllApplicationNo();
		vehicleNoList = employeeProfileService.getAllVehicleNo();
	}

	public void onChangeTitle() {

		String titleCode = adminService.getParaCodeForTitle();

		if (attorneyDTO.getTitle().equals(titleCode)) {
			setDisabledDOB(true);
			setDisabledGender(true);
			setCheckValiationsForInputFields(true);
		} else {
			setDisabledDOB(false);
			setDisabledGender(false);
			setCheckValiationsForInputFields(false);
		}
	}

	public List<String> completePermits(String query) {
		List<String> allPermits = permitNoList;
		List<String> filteredPermits = new ArrayList<String>();
		for (int i = 0; i < allPermits.size(); i++) {
			if (allPermits.get(i).contains(query)) {
				filteredPermits.add(allPermits.get(i));
			}
		}

		return filteredPermits;
	}

	public List<String> completeApplication(String query) {
		List<String> allPermits = applicationNoList;
		List<String> filteredPermits = new ArrayList<String>();
		for (int i = 0; i < allPermits.size(); i++) {
			if (allPermits.get(i).contains(query)) {
				filteredPermits.add(allPermits.get(i));
			}
		}

		return filteredPermits;
	}

	public List<String> completeVehicle(String query) {
		List<String> allPermits = vehicleNoList;
		List<String> filteredPermits = new ArrayList<String>();
		for (int i = 0; i < allPermits.size(); i++) {
			if (allPermits.get(i).contains(query)) {
				filteredPermits.add(allPermits.get(i));
			}
		}

		return filteredPermits;
	}

	public void searchAction() {
		statusPermitcheck = employeeProfileService.getStatus(attorneyDTO);
		statusApplicationcheck = employeeProfileService.getApplication_No_status(attorneyDTO);
		statusVehiclecheck = employeeProfileService.getVehicle_No_status(attorneyDTO);

		attorneyCheckList = new ArrayList<>(0);
		attorneyCheckList = employeeProfileService.attorneyCheck(attorneyDTO);
		setSearchErrorMessage(null);

		if ((attorneyDTO.getPermit_No() == null || attorneyDTO.getPermit_No().trim().equalsIgnoreCase(""))
				&& (attorneyDTO.getApplication_No() == null
						|| attorneyDTO.getApplication_No().trim().equalsIgnoreCase(""))
				&& (attorneyDTO.getVehicle_No() == null || attorneyDTO.getVehicle_No().trim().equalsIgnoreCase(""))) {

			setErrorMsg("Search Fields should contain at least one Value.");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else {

			if (attorneyCheckList.isEmpty()) {
				setErrorMsg("No Data Found.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				attorneyDTO.setPermit_No(null);
				attorneyDTO.setApplication_No(null);
				attorneyDTO.setVehicle_No(null);
				attorneyDTO.setCo_Name(null);
				attorneyDTO.setCo_Address1(null);
				attorneyDTO.setCo_Address2(null);
				attorneyDTO.setCo_City(null);

			} else if (statusPermitcheck != null && statusPermitcheck.equals("A")) {
				setErrorMsg("Active Attorney Holder Already Exists.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			} else if (statusApplicationcheck != null && statusApplicationcheck.equals("A")) {
				setErrorMsg("Active Attorney Holder Already Exists.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			} else if (statusVehiclecheck != null && statusVehiclecheck.equals("A")) {
				setErrorMsg("Active Attorney Holder Already Exists.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			} else {
				attorneyList = new ArrayList<>(0);
				attorneyList = employeeProfileService.attorney(attorneyDTO);
				attorneyDTO.setPermit_No(attorneyDTO.getCheck_Permit_No());
				attorneyDTO.setApplication_No(attorneyDTO.getCheck_Application_No());
				attorneyDTO.setVehicle_No(attorneyDTO.getCheck_Vehicle_No());

				searchBtnChecked = true;
			}

		}

	}

	public void clearAction() {
		attorneyDTO.setPermit_No(null);
		attorneyDTO.setApplication_No(null);
		attorneyDTO.setVehicle_No(null);
		attorneyDTO.setCo_Name(null);
		attorneyDTO.setCo_Address1(null);
		attorneyDTO.setCo_Address2(null);
		attorneyDTO.setCo_City(null);
		attorneyDTO.setLanguage(null);
		setSearchErrorMessage(null);
		searchBtnChecked = false;
		setDisabledDOB(false);
		setDisabledGender(false);
		setCheckValiationsForInputFields(false);
		attorneyDTO.setTitle(null);
	}

	public void clearPAForm() {

		attorneyDTO.setTitle(null);
		attorneyDTO.setPa_Gender(null);
		attorneyDTO.setPa_Nic_or_Org(null);
		attorneyDTO.setDob(null);
		attorneyDTO.setPa_FullName_Eng(null);
		attorneyDTO.setPa_FullName_Sin(null);
		attorneyDTO.setPa_FullName_Tamil(null);
		attorneyDTO.setPa_NameWithInitial(null);
		attorneyDTO.setPa_TeleNum(null);
		attorneyDTO.setPa_MobileNum(null);
		attorneyDTO.setPa_Address1_Eng(null);
		attorneyDTO.setPa_Address1_Sin(null);
		attorneyDTO.setPa_Address1_Tamil(null);
		attorneyDTO.setPa_Address2_Eng(null);
		attorneyDTO.setPa_Address2_Sin(null);
		attorneyDTO.setPa_Address2_Tamil(null);
		attorneyDTO.setPa_City_Eng(null);
		attorneyDTO.setPa_City_Sin(null);
		attorneyDTO.setPa_City_Tamil(null);
		attorneyDTO.setStatus(null);
		setDisabledDOB(false);
		setDisabledGender(false);
		setCheckValiationsForInputFields(false);
	}

	public void saveAttorney() {
		attorneyDTO.setUserName(sessionBackingBean.getLoginUser());
		if (searchBtnChecked == true) {
			if (checkValiationsForInputFields == false) {
				if (!attorneyDTO.getLanguage().isEmpty() && attorneyDTO.getLanguage() != null
						&& !attorneyDTO.getLanguage().equalsIgnoreCase("")) {
					if (!attorneyDTO.getTitle().isEmpty() && attorneyDTO.getTitle() != null
							&& !attorneyDTO.getTitle().equalsIgnoreCase("")) {
						if (!attorneyDTO.getPa_Gender().isEmpty() && attorneyDTO.getPa_Gender() != null
								&& !attorneyDTO.getPa_Gender().equalsIgnoreCase("")) {
							if (attorneyDTO.getDob() != null) {
								if (!attorneyDTO.getPa_Nic_or_Org().isEmpty() && attorneyDTO.getPa_Nic_or_Org() != null
										&& !attorneyDTO.getPa_Nic_or_Org().equalsIgnoreCase("")) {

									if (!attorneyDTO.getPa_FullName_Eng().isEmpty()
											&& attorneyDTO.getPa_FullName_Eng() != null
											&& !attorneyDTO.getPa_FullName_Eng().equalsIgnoreCase("")) {

										if (!attorneyDTO.getPa_Address1_Eng().isEmpty()
												&& attorneyDTO.getPa_Address1_Eng() != null
												&& !attorneyDTO.getPa_Address1_Eng().equalsIgnoreCase("")) {

											if (!attorneyDTO.getPa_Address2_Eng().isEmpty()
													&& attorneyDTO.getPa_Address2_Eng() != null
													&& !attorneyDTO.getPa_Address2_Eng().equalsIgnoreCase("")) {

												if (!attorneyDTO.getPa_City_Eng().isEmpty()
														&& attorneyDTO.getPa_City_Eng() != null
														&& !attorneyDTO.getPa_City_Eng().equalsIgnoreCase("")) {

													if (!attorneyDTO.getStatus().isEmpty()
															&& attorneyDTO.getStatus() != null
															&& !attorneyDTO.getStatus().equalsIgnoreCase("")) {
														if (attorneyDTO.getLanguage().equals("E")) {

															checkTelNum(attorneyDTO.getPa_TeleNum(), false);
															checkTelNum(attorneyDTO.getPa_MobileNum(), true);

															if (numCheckMobile == false && numCheckTel == false) {
																int result = employeeProfileService
																		.AttorneyHolderInfo(attorneyDTO);
																if (result == 0) {
																	searchBtnChecked = false;
																	sucessMsg = "Successfully Saved.";
																	RequestContext.getCurrentInstance()
																			.update("successSve");
																	RequestContext.getCurrentInstance()
																			.execute("PF('successSve').show()");

																	clearAction();
																	clearPAForm();

																}
															}
														} else if (attorneyDTO.getLanguage().equals("S")) {
															if (!attorneyDTO.getPa_FullName_Sin().isEmpty()
																	&& attorneyDTO.getPa_FullName_Sin() != null
																	&& !attorneyDTO.getPa_FullName_Sin()
																			.equalsIgnoreCase("")) {

																if (!attorneyDTO.getPa_Address1_Sin().isEmpty()
																		&& attorneyDTO.getPa_Address1_Sin() != null
																		&& !attorneyDTO.getPa_Address1_Sin()
																				.equalsIgnoreCase("")) {
																	if (!attorneyDTO.getPa_Address2_Sin().isEmpty()
																			&& attorneyDTO.getPa_Address2_Sin() != null
																			&& !attorneyDTO.getPa_Address2_Sin()
																					.equalsIgnoreCase("")) {
																		if (!attorneyDTO.getPa_City_Sin().isEmpty()
																				&& attorneyDTO.getPa_City_Sin() != null
																				&& !attorneyDTO.getPa_City_Sin()
																						.equalsIgnoreCase("")) {

																			checkTelNum(attorneyDTO.getPa_TeleNum(),
																					false);
																			checkTelNum(attorneyDTO.getPa_MobileNum(),
																					true);

																			if (numCheckMobile == false
																					&& numCheckTel == false) {
																				int result = employeeProfileService
																						.AttorneyHolderInfo(
																								attorneyDTO);
																				if (result == 0) {
																					searchBtnChecked = false;
																					sucessMsg = "Successfully Saved.";
																					RequestContext.getCurrentInstance()
																							.update("successSve");
																					RequestContext.getCurrentInstance()
																							.execute(
																									"PF('successSve').show()");

																					clearAction();
																					clearPAForm();

																				}
																			}
																		} else {
																			setErrorMsg(
																					"Sinhala City should be entered.");
																			RequestContext.getCurrentInstance()
																					.update("frmrequiredField");
																			RequestContext.getCurrentInstance().execute(
																					"PF('requiredField').show()");
																		}
																	} else {
																		setErrorMsg(
																				"Sinhala Address 02 should be entered.");
																		RequestContext.getCurrentInstance()
																				.update("frmrequiredField");
																		RequestContext.getCurrentInstance()
																				.execute("PF('requiredField').show()");
																	}
																} else {
																	setErrorMsg(
																			"Sinhala Address 01 should be entered.");
																	RequestContext.getCurrentInstance()
																			.update("frmrequiredField");
																	RequestContext.getCurrentInstance()
																			.execute("PF('requiredField').show()");
																}
															} else {
																setErrorMsg(
																		"Name in Full (Sinhala) should be entered.");
																RequestContext.getCurrentInstance()
																		.update("frmrequiredField");
																RequestContext.getCurrentInstance()
																		.execute("PF('requiredField').show()");
															}
														} else if (attorneyDTO.getLanguage().equals("T")) {
															if (!attorneyDTO.getPa_FullName_Tamil().isEmpty()
																	&& attorneyDTO.getPa_FullName_Tamil() != null
																	&& !attorneyDTO.getPa_FullName_Tamil()
																			.equalsIgnoreCase("")) {
																if (!attorneyDTO.getPa_Address1_Tamil().isEmpty()
																		&& attorneyDTO.getPa_Address1_Tamil() != null
																		&& !attorneyDTO.getPa_Address1_Tamil()
																				.equalsIgnoreCase("")) {
																	if (!attorneyDTO.getPa_Address2_Tamil().isEmpty()
																			&& attorneyDTO
																					.getPa_Address2_Tamil() != null
																			&& !attorneyDTO.getPa_Address2_Tamil()
																					.equalsIgnoreCase("")) {

																		checkTelNum(attorneyDTO.getPa_TeleNum(), false);
																		checkTelNum(attorneyDTO.getPa_MobileNum(),
																				true);

																		if (numCheckMobile == false
																				&& numCheckTel == false) {
																			int result = employeeProfileService
																					.AttorneyHolderInfo(attorneyDTO);
																			if (result == 0) {
																				searchBtnChecked = false;
																				sucessMsg = "Successfully Saved.";
																				RequestContext.getCurrentInstance()
																						.update("successSve");
																				RequestContext.getCurrentInstance()
																						.execute(
																								"PF('successSve').show()");

																				clearAction();
																				clearPAForm();

																			}
																		}
																	} else {
																		setErrorMsg(
																				"Tamil Address 02 should be entered.");
																		RequestContext.getCurrentInstance()
																				.update("frmrequiredField");
																		RequestContext.getCurrentInstance()
																				.execute("PF('requiredField').show()");
																	}
																} else {
																	setErrorMsg("Tamil Address 01 should be entered.");
																	RequestContext.getCurrentInstance()
																			.update("frmrequiredField");
																	RequestContext.getCurrentInstance()
																			.execute("PF('requiredField').show()");
																}
															} else {
																setErrorMsg("Name in Full (Tamil) should be entered.");
																RequestContext.getCurrentInstance()
																		.update("frmrequiredField");
																RequestContext.getCurrentInstance()
																		.execute("PF('requiredField').show()");
															}
														}

													} else {
														setErrorMsg("Status should be selected.");
														RequestContext.getCurrentInstance().update("frmrequiredField");
														RequestContext.getCurrentInstance()
																.execute("PF('requiredField').show()");
													}
												} else {
													setErrorMsg("City should be entered.");
													RequestContext.getCurrentInstance().update("frmrequiredField");
													RequestContext.getCurrentInstance()
															.execute("PF('requiredField').show()");
												}
											} else {
												setErrorMsg("Address 02 should be entered.");
												RequestContext.getCurrentInstance().update("frmrequiredField");
												RequestContext.getCurrentInstance()
														.execute("PF('requiredField').show()");
											}
										} else {
											setErrorMsg("Address 01 should be entered.");
											RequestContext.getCurrentInstance().update("frmrequiredField");
											RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
										}
									} else {
										setErrorMsg("Full Name should be entered.");
										RequestContext.getCurrentInstance().update("frmrequiredField");
										RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
									}

								} else {
									setErrorMsg("Valid NIC should be entered.");
									RequestContext.getCurrentInstance().update("frmrequiredField");
									RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
								}
							} else {
								setErrorMsg("DOB should be selected.");
								RequestContext.getCurrentInstance().update("frmrequiredField");
								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
							}
						} else {
							setErrorMsg("Gender should be selected.");
							RequestContext.getCurrentInstance().update("frmrequiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
						}
					} else {
						setErrorMsg("Title should be selected.");
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}
				}
			} else if (checkValiationsForInputFields == true) {
				if (!attorneyDTO.getLanguage().isEmpty() && attorneyDTO.getLanguage() != null
						&& !attorneyDTO.getLanguage().equalsIgnoreCase("")) {
					if (!attorneyDTO.getTitle().isEmpty() && attorneyDTO.getTitle() != null
							&& !attorneyDTO.getTitle().equalsIgnoreCase("")) {

						if (!attorneyDTO.getPa_Nic_or_Org().isEmpty() && attorneyDTO.getPa_Nic_or_Org() != null
								&& !attorneyDTO.getPa_Nic_or_Org().equalsIgnoreCase("")) {

							if (!attorneyDTO.getPa_FullName_Eng().isEmpty() && attorneyDTO.getPa_FullName_Eng() != null
									&& !attorneyDTO.getPa_FullName_Eng().equalsIgnoreCase("")) {

								if (!attorneyDTO.getPa_Address1_Eng().isEmpty()
										&& attorneyDTO.getPa_Address1_Eng() != null
										&& !attorneyDTO.getPa_Address1_Eng().equalsIgnoreCase("")) {

									if (!attorneyDTO.getPa_Address2_Eng().isEmpty()
											&& attorneyDTO.getPa_Address2_Eng() != null
											&& !attorneyDTO.getPa_Address2_Eng().equalsIgnoreCase("")) {

										if (!attorneyDTO.getPa_City_Eng().isEmpty()
												&& attorneyDTO.getPa_City_Eng() != null
												&& !attorneyDTO.getPa_City_Eng().equalsIgnoreCase("")) {

											if (!attorneyDTO.getStatus().isEmpty() && attorneyDTO.getStatus() != null
													&& !attorneyDTO.getStatus().equalsIgnoreCase("")) {
												if (attorneyDTO.getLanguage().equals("E")) {

													checkTelNum(attorneyDTO.getPa_TeleNum(), false);
													checkTelNum(attorneyDTO.getPa_MobileNum(), true);

													if (numCheckMobile == false && numCheckTel == false) {
														int result = employeeProfileService
																.AttorneyHolderInfo(attorneyDTO);
														if (result == 0) {
															searchBtnChecked = false;

															sucessMsg = "Successfully Saved.";
															RequestContext.getCurrentInstance().update("successSve");
															RequestContext.getCurrentInstance()
																	.execute("PF('successSve').show()");

															clearAction();
															clearPAForm();

														}
													}
												} else if (attorneyDTO.getLanguage().equals("S")) {
													if (!attorneyDTO.getPa_FullName_Sin().isEmpty()
															&& attorneyDTO.getPa_FullName_Sin() != null
															&& !attorneyDTO.getPa_FullName_Sin().equalsIgnoreCase("")) {

														if (!attorneyDTO.getPa_Address1_Sin().isEmpty()
																&& attorneyDTO.getPa_Address1_Sin() != null
																&& !attorneyDTO.getPa_Address1_Sin()
																		.equalsIgnoreCase("")) {
															if (!attorneyDTO.getPa_Address2_Sin().isEmpty()
																	&& attorneyDTO.getPa_Address2_Sin() != null
																	&& !attorneyDTO.getPa_Address2_Sin()
																			.equalsIgnoreCase("")) {
																if (!attorneyDTO.getPa_City_Sin().isEmpty()
																		&& attorneyDTO.getPa_City_Sin() != null
																		&& !attorneyDTO.getPa_City_Sin()
																				.equalsIgnoreCase("")) {

																	checkTelNum(attorneyDTO.getPa_TeleNum(), false);
																	checkTelNum(attorneyDTO.getPa_MobileNum(), true);

																	if (numCheckMobile == false
																			&& numCheckTel == false) {
																		int result = employeeProfileService
																				.AttorneyHolderInfo(attorneyDTO);
																		if (result == 0) {
																			searchBtnChecked = false;
																			sucessMsg = "Successfully Saved.";
																			RequestContext.getCurrentInstance()
																					.update("successSve");
																			RequestContext.getCurrentInstance()
																					.execute("PF('successSve').show()");

																			clearAction();
																			clearPAForm();

																		}
																	}
																} else {

																	setErrorMsg("Sinhala City should be entered.");
																	RequestContext.getCurrentInstance()
																			.update("frmrequiredField");
																	RequestContext.getCurrentInstance()
																			.execute("PF('requiredField').show()");
																}
															} else {
																setErrorMsg("Sinhala Address 02 should be entered.");
																RequestContext.getCurrentInstance()
																		.update("frmrequiredField");
																RequestContext.getCurrentInstance()
																		.execute("PF('requiredField').show()");
															}
														} else {

															setErrorMsg("Sinhala Address 01 should be entered.");
															RequestContext.getCurrentInstance()
																	.update("frmrequiredField");
															RequestContext.getCurrentInstance()
																	.execute("PF('requiredField').show()");
														}
													} else {
														setErrorMsg("Name in Full (Sinhala) should be entered.");
														RequestContext.getCurrentInstance().update("frmrequiredField");
														RequestContext.getCurrentInstance()
																.execute("PF('requiredField').show()");
													}
												} else if (attorneyDTO.getLanguage().equals("T")) {
													if (!attorneyDTO.getPa_FullName_Tamil().isEmpty()
															&& attorneyDTO.getPa_FullName_Tamil() != null
															&& !attorneyDTO.getPa_FullName_Tamil()
																	.equalsIgnoreCase("")) {
														if (!attorneyDTO.getPa_Address1_Tamil().isEmpty()
																&& attorneyDTO.getPa_Address1_Tamil() != null
																&& !attorneyDTO.getPa_Address1_Tamil()
																		.equalsIgnoreCase("")) {

															if (!attorneyDTO.getPa_Address2_Tamil().isEmpty()
																	&& attorneyDTO.getPa_Address2_Tamil() != null
																	&& !attorneyDTO.getPa_Address2_Tamil()
																			.equalsIgnoreCase("")) {

																checkTelNum(attorneyDTO.getPa_TeleNum(), false);
																checkTelNum(attorneyDTO.getPa_MobileNum(), true);

																if (numCheckMobile == false && numCheckTel == false) {
																	int result = employeeProfileService
																			.AttorneyHolderInfo(attorneyDTO);
																	if (result == 0) {
																		searchBtnChecked = false;

																		sucessMsg = "Successfully Saved.";
																		RequestContext.getCurrentInstance()
																				.update("successSve");
																		RequestContext.getCurrentInstance()
																				.execute("PF('successSve').show()");

																		clearAction();
																		clearPAForm();

																	}
																}
															} else {

																setErrorMsg("Tamil Address 02 should be entered.");
																RequestContext.getCurrentInstance()
																		.update("frmrequiredField");
																RequestContext.getCurrentInstance()
																		.execute("PF('requiredField').show()");
															}
														} else {
															setErrorMsg("Tamil Address 01 should be entered.");
															RequestContext.getCurrentInstance()
																	.update("frmrequiredField");
															RequestContext.getCurrentInstance()
																	.execute("PF('requiredField').show()");
														}
													} else {
														setErrorMsg("Name in Full (Tamil) should be entered.");
														RequestContext.getCurrentInstance().update("frmrequiredField");
														RequestContext.getCurrentInstance()
																.execute("PF('requiredField').show()");
													}
												}

											} else {
												setErrorMsg("Status should be selected.");
												RequestContext.getCurrentInstance().update("frmrequiredField");
												RequestContext.getCurrentInstance()
														.execute("PF('requiredField').show()");
											}
										} else {
											setErrorMsg("City should be entered.");
											RequestContext.getCurrentInstance().update("frmrequiredField");
											RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
										}
									} else {
										setErrorMsg("Address 02 should be entered.");
										RequestContext.getCurrentInstance().update("frmrequiredField");
										RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
									}
								} else {
									setErrorMsg("Address 01 should be entered.");
									RequestContext.getCurrentInstance().update("frmrequiredField");
									RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
								}
							} else {
								setErrorMsg("Full Name should be entered.");
								RequestContext.getCurrentInstance().update("frmrequiredField");
								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
							}

						} else {
							setErrorMsg("Valid NIC should be entered.");
							RequestContext.getCurrentInstance().update("frmrequiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
						}
					} else {
						setErrorMsg("Title should be selected.");
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}
				}
			}

		} else if (searchBtnChecked == false) {
			setErrorMsg("First you should be searched.");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	/** Validate Telephone/Mobile Number ***/
	public void checkTelNum(String telNo, boolean mobileNo) {
		if (!telNo.isEmpty() && telNo != null && !telNo.equalsIgnoreCase("")) {

			Pattern pattern = Pattern.compile("\\d{3}\\d{7}");
			Matcher matcher = pattern.matcher(telNo);
			if (matcher.matches()) {
				if (mobileNo) {

					numCheckMobile = false;
				} else {
					numCheckTel = false;
				}
			} else {
				setErrorMsg("Valid Tel./Mobile Number should be entered.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				if (mobileNo) {
					numCheckMobile = true;
				} else {
					numCheckTel = true;
				}

			}
		}

	}

	public List<AttorneyHolderDTO> getAttorneyList() {
		return attorneyList;
	}

	public void setAttorneyList(List<AttorneyHolderDTO> attorneyList) {
		this.attorneyList = attorneyList;
	}

	public EmployeeProfileService getEmployeeProfileService() {
		return employeeProfileService;
	}

	public void setEmployeeProfileService(EmployeeProfileService employeeProfileService) {
		this.employeeProfileService = employeeProfileService;
	}

	public AttorneyHolderDTO getAttorneyDTO() {
		return attorneyDTO;
	}

	public void setAttorneyDTO(AttorneyHolderDTO attorneyDTO) {
		this.attorneyDTO = attorneyDTO;
	}

	public String getSearchErrorMessage() {
		return searchErrorMessage;
	}

	public void setSearchErrorMessage(String searchErrorMessage) {
		this.searchErrorMessage = searchErrorMessage;
	}

	public String getSucessMsg() {
		return sucessMsg;
	}

	public void setSucessMsg(String sucessMsg) {
		this.sucessMsg = sucessMsg;
	}

	public List<String> getPermitNoList() {
		return permitNoList;
	}

	public void setPermitNoList(List<String> permitNoList) {
		this.permitNoList = permitNoList;
	}

	public List<String> getApplicationNoList() {
		return applicationNoList;
	}

	public void setApplicationNoList(List<String> applicationNoList) {
		this.applicationNoList = applicationNoList;
	}

	public List<String> getVehicleNoList() {
		return vehicleNoList;
	}

	public void setVehicleNoList(List<String> vehicleNoList) {
		this.vehicleNoList = vehicleNoList;
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

	public Boolean getCheckValiationsForInputFields() {
		return checkValiationsForInputFields;
	}

	public void setCheckValiationsForInputFields(Boolean checkValiationsForInputFields) {
		this.checkValiationsForInputFields = checkValiationsForInputFields;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public List<AttorneyHolderDTO> getAttorneyCheckList() {
		return attorneyCheckList;
	}

	public void setAttorneyCheckList(List<AttorneyHolderDTO> attorneyCheckList) {
		this.attorneyCheckList = attorneyCheckList;
	}

	public PermitDTO getPermitDTO() {
		return permitDTO;
	}

	public void setPermitDTO(PermitDTO permitDTO) {
		this.permitDTO = permitDTO;
	}
}
