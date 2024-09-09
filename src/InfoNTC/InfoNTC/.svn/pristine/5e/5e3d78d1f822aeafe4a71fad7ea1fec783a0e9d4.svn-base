package lk.informatics.ntc.view.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.AttorneyHolderDTO;
import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.service.AdminService;
import lk.informatics.ntc.model.service.EmployeeProfileService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "attorneyeditBackingBean")
@ViewScoped
public class AttorneyHolderEditBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	// Services
	private AdminService adminService;
	private EmployeeProfileService employeeProfileService;
	private AttorneyHolderDTO attorneyDTO = new AttorneyHolderDTO();

	private String strSelectedPermitNo;
	private String strSelectedApplicationNo;
	private String strSelectedBusRegNo;
	private Boolean booldisable;
	private Boolean disableMode;
	private String strSelectedLanguage;
	private String strSelectedStatus;
	private String strSelectedTitle;
	private String strSelectedGender;
	private String errorMsg;

	public List<String> permitNoList = new ArrayList<String>(0);
	public List<String> appNoList = new ArrayList<String>(0);
	public List<String> busRegNoList = new ArrayList<String>(0);

	private List<CommonDTO> titleList;
	private List<CommonDTO> genderList;

	@PostConstruct
	public void init() {
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");
		employeeProfileService = (EmployeeProfileService) SpringApplicationContex.getBean("employeeProfileService");
		permitNoList = adminService.getAllActivePermitsforAttorney();
		appNoList = adminService.getAllActiveApplicationsforAttorney();
		busRegNoList = adminService.getAllActiveBusRegNosforAttorney();
		titleList = employeeProfileService.GetTitleToDropdown();
		genderList = employeeProfileService.GetGenderToDropdown();

		String result = adminService.getUserActivity(sessionBackingBean.loginUser, "FN7_6");
		if (result != null) {
			setDisableMode(false);
		} else {
			setDisableMode(true);
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

	public List<String> completeAppNos(String query) {
		List<String> allAppNos = appNoList;
		List<String> filteredAppnos = new ArrayList<String>();
		for (int i = 0; i < allAppNos.size(); i++) {
			if (allAppNos.get(i).contains(query)) {
				filteredAppnos.add(allAppNos.get(i));
			}
		}

		return filteredAppnos;
	}

	public List<String> completeRegBusNos(String query) {
		List<String> allRegNos = busRegNoList;
		List<String> filteredRegNos = new ArrayList<String>();
		for (int i = 0; i < allRegNos.size(); i++) {
			if (allRegNos.get(i).contains(query)) {
				filteredRegNos.add(allRegNos.get(i));
			}
		}

		return filteredRegNos;
	}

	public void masterClear() {
		strSelectedPermitNo = null;
		strSelectedApplicationNo = null;
		strSelectedBusRegNo = null;
		attorneyDTO = new AttorneyHolderDTO();
		strSelectedStatus = null;
		strSelectedLanguage = null;
		strSelectedTitle = null;
		strSelectedGender = null;
		booldisable = false;
	}

	public void searchClear() {
		searchDetails();

	}

	public void searchDetails() {
		if (strSelectedPermitNo != "" || strSelectedApplicationNo != "" || strSelectedBusRegNo != "") {
			if (strSelectedPermitNo.equals("select"))
				strSelectedPermitNo = null;
			if (strSelectedApplicationNo.equals("select"))
				strSelectedApplicationNo = null;
			if (strSelectedBusRegNo.equals("select"))
				strSelectedBusRegNo = null;

			attorneyDTO = adminService.searchAttorneyDetails(strSelectedPermitNo, strSelectedApplicationNo,
					strSelectedBusRegNo);
			if (attorneyDTO.getPa_FullName_Eng() != null) {
				strSelectedApplicationNo = attorneyDTO.getApplication_No();
				strSelectedBusRegNo = attorneyDTO.getVehicle_No();
				strSelectedPermitNo = attorneyDTO.getPermit_No();
				strSelectedStatus = attorneyDTO.getStatus();
				strSelectedLanguage = attorneyDTO.getLanguage();
				strSelectedTitle = attorneyDTO.getTitle();
				strSelectedGender = attorneyDTO.getGender();
			}
		} else {
			RequestContext.getCurrentInstance().update("frmwarningMsge");
			RequestContext.getCurrentInstance().execute("PF('warningMsge').show()");
		}
	}

	public void updateAttorney() {
		if (strSelectedTitle != null && !strSelectedTitle.isEmpty() && !strSelectedTitle.equalsIgnoreCase("")) {
			if (attorneyDTO.getPa_FullName_Eng() != null && !attorneyDTO.getPa_FullName_Eng().isEmpty()
					&& !attorneyDTO.getPa_FullName_Eng().equalsIgnoreCase("")) {
				if (attorneyDTO.getPa_Address1_Eng() != null && !attorneyDTO.getPa_Address1_Eng().isEmpty()
						&& !attorneyDTO.getPa_Address1_Eng().equalsIgnoreCase("")) {
					if (attorneyDTO.getPa_Address2_Eng() != null && !attorneyDTO.getPa_Address2_Eng().isEmpty()
							&& !attorneyDTO.getPa_Address2_Eng().equalsIgnoreCase("")) {
						if (attorneyDTO.getPa_City_Eng() != null && !attorneyDTO.getPa_City_Eng().isEmpty()
								&& !attorneyDTO.getPa_City_Eng().equalsIgnoreCase("")) {
							if (strSelectedLanguage.equals("S")) {
								if (attorneyDTO.getPa_FullName_Sin() != null
										&& !attorneyDTO.getPa_FullName_Sin().isEmpty()
										&& !attorneyDTO.getPa_FullName_Sin().equalsIgnoreCase("")) {
									if (attorneyDTO.getPa_Address1_Sin() != null
											&& !attorneyDTO.getPa_Address1_Sin().isEmpty()
											&& !attorneyDTO.getPa_Address1_Sin().equalsIgnoreCase("")) {
										if (attorneyDTO.getPa_Address2_Sin() != null
												&& !attorneyDTO.getPa_Address2_Sin().isEmpty()
												&& !attorneyDTO.getPa_Address2_Sin().equalsIgnoreCase("")) {
											if (attorneyDTO.getPa_City_Sin() != null
													&& !attorneyDTO.getPa_City_Sin().isEmpty()
													&& !attorneyDTO.getPa_City_Sin().equalsIgnoreCase("")) {

												// Update
												updateAttorneyHolder();

											} else {
												setErrorMsg("City (Sinhala) should be entered.");
												RequestContext.getCurrentInstance().update("frmrequiredField");
												RequestContext.getCurrentInstance()
														.execute("PF('requiredField').show()");
											}
										} else {
											setErrorMsg("Address 2 (Sinhala) should be entered.");
											RequestContext.getCurrentInstance().update("frmrequiredField");
											RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
										}
									} else {
										setErrorMsg("Address 1 (Sinhala) should be entered.");
										RequestContext.getCurrentInstance().update("frmrequiredField");
										RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
									}
								} else {
									setErrorMsg("Nane in Full (Sinhala) should be entered.");
									RequestContext.getCurrentInstance().update("frmrequiredField");
									RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
								}
							} else if (strSelectedLanguage.equals("T")) {
								if (attorneyDTO.getPa_FullName_Tamil() != null
										&& !attorneyDTO.getPa_FullName_Tamil().isEmpty()
										&& !attorneyDTO.getPa_FullName_Tamil().equalsIgnoreCase("")) {
									if (attorneyDTO.getPa_Address1_Tamil() != null
											&& !attorneyDTO.getPa_Address1_Tamil().isEmpty()
											&& !attorneyDTO.getPa_Address1_Tamil().equalsIgnoreCase("")) {
										if (attorneyDTO.getPa_Address2_Tamil() != null
												&& !attorneyDTO.getPa_Address2_Tamil().isEmpty()
												&& !attorneyDTO.getPa_Address2_Tamil().equalsIgnoreCase("")) {
											if (attorneyDTO.getPa_City_Tamil() != null
													&& !attorneyDTO.getPa_City_Tamil().isEmpty()
													&& !attorneyDTO.getPa_City_Tamil().equalsIgnoreCase("")) {
												// Update
												updateAttorneyHolder();

											} else {
												setErrorMsg("City (Tamil) should be entered.");
												RequestContext.getCurrentInstance().update("frmrequiredField");
												RequestContext.getCurrentInstance()
														.execute("PF('requiredField').show()");
											}
										} else {
											setErrorMsg("Address 2 (Tamil) should be entered.");
											RequestContext.getCurrentInstance().update("frmrequiredField");
											RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
										}
									} else {
										setErrorMsg("Address 1 (Tamil) should be entered.");
										RequestContext.getCurrentInstance().update("frmrequiredField");
										RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
									}
								} else {
									setErrorMsg("Name in Full (Tamil) should be entered.");
									RequestContext.getCurrentInstance().update("frmrequiredField");
									RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
								}
							} else {
								// Update
								updateAttorneyHolder();

							}

						} else {
							setErrorMsg("City should be entered.");
							RequestContext.getCurrentInstance().update("frmrequiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
						}
					} else {
						setErrorMsg("Address 2 should be entered.");
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}
				} else {
					setErrorMsg("Address 1 should be entered.");
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}

			} else {
				setErrorMsg("Full Name should be entered.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		} else {
			setErrorMsg("Title should be selected.");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	private void updateAttorneyHolder() {
		attorneyDTO.setTitle(strSelectedTitle);
		attorneyDTO.setLanguage(strSelectedLanguage);
		attorneyDTO.setStatus(strSelectedStatus);
		attorneyDTO.setModifiedBy(sessionBackingBean.loginUser);

		int result = adminService.updateAttorneyHolder(attorneyDTO);

		if (result == 0) {
			RequestContext.getCurrentInstance().update("frmsuccessSve");
			RequestContext.getCurrentInstance().execute("PF('successSve').show()");

		} else {
			RequestContext.getCurrentInstance().execute("PF('generalError').show()");
		}
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

	public String getStrSelectedPermitNo() {
		return strSelectedPermitNo;
	}

	public void setStrSelectedPermitNo(String strSelectedPermitNo) {
		this.strSelectedPermitNo = strSelectedPermitNo;
	}

	public String getStrSelectedApplicationNo() {
		return strSelectedApplicationNo;
	}

	public void setStrSelectedApplicationNo(String strSelectedApplicationNo) {
		this.strSelectedApplicationNo = strSelectedApplicationNo;
	}

	public String getStrSelectedBusRegNo() {
		return strSelectedBusRegNo;
	}

	public void setStrSelectedBusRegNo(String strSelectedBusRegNo) {
		this.strSelectedBusRegNo = strSelectedBusRegNo;
	}

	public Boolean getBooldisable() {
		return booldisable;
	}

	public void setBooldisable(Boolean booldisable) {
		this.booldisable = booldisable;
	}

	public List<String> getPermitNoList() {
		return permitNoList;
	}

	public void setPermitNoList(List<String> permitNoList) {
		this.permitNoList = permitNoList;
	}

	public List<String> getAppNoList() {
		return appNoList;
	}

	public void setAppNoList(List<String> appNoList) {
		this.appNoList = appNoList;
	}

	public List<String> getBusRegNoList() {
		return busRegNoList;
	}

	public void setBusRegNoList(List<String> busRegNoList) {
		this.busRegNoList = busRegNoList;
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

	public Boolean getDisableMode() {
		return disableMode;
	}

	public void setDisableMode(Boolean disableMode) {
		this.disableMode = disableMode;
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

	public String getStrSelectedLanguage() {
		return strSelectedLanguage;
	}

	public void setStrSelectedLanguage(String strSelectedLanguage) {
		this.strSelectedLanguage = strSelectedLanguage;
	}

	public String getStrSelectedStatus() {
		return strSelectedStatus;
	}

	public void setStrSelectedStatus(String strSelectedStatus) {
		this.strSelectedStatus = strSelectedStatus;
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

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
