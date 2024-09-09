package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIInput;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.DriverConductorRegistrationDTO;
import lk.informatics.ntc.model.dto.PermitDTO;
import lk.informatics.ntc.model.service.AdminService;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.DriverConductorTrainingService;
import lk.informatics.ntc.view.util.SpringApplicationContex;
import lk.informatics.ntc.model.dto.DocumentManagementDTO;
import lk.informatics.ntc.model.service.DocumentManagementService;

@ManagedBean(name = "driverConductorRegistrationBean")
@ViewScoped
public class DriverConductorRegistrationBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private CommonService commonService;
	private DriverConductorTrainingService driverConductorTrainingService;
	private AdminService adminService;
	private DocumentManagementService documentManagementService;

	private DriverConductorRegistrationDTO registrationDto;
	private DriverConductorRegistrationDTO newRregistrationDto;

	private int totalPoints = 0;
	private List<CommonDTO> trainingTypeList;
	private List<CommonDTO> genderList;
	private List<CommonDTO> districtList;
	private List<PermitDTO> permitList;
	private List<CommonDTO> vehicleDetList;
	private List<DocumentManagementDTO> mandatoryList = new ArrayList<DocumentManagementDTO>(0);
	private List<DocumentManagementDTO> optionalList = new ArrayList<DocumentManagementDTO>(0);
	private PermitDTO selectedPermit;
	private String sucessMsg, errorMsg, loginUser, oldAppNo, isDuplicate, confirmationMsg;
	private boolean saveMode, updateMode, disableMode, disDocument, disableModeTrnType;
	Date issuedDate, expireDate;
	private String selectedLanguage;

	@PostConstruct
	public void init() {
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		driverConductorTrainingService = (DriverConductorTrainingService) SpringApplicationContex
				.getBean("driverConductorTrainingService");
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");
		documentManagementService = (DocumentManagementService) SpringApplicationContex
				.getBean("documentManagementService");

		loginUser = sessionBackingBean.getLoginUser();

		registrationDto = new DriverConductorRegistrationDTO();
		selectedPermit = new PermitDTO();
		trainingTypeList = driverConductorTrainingService.GetTrainingTypesByMode("C");
		permitList = driverConductorTrainingService.getActivePermitList();
		districtList = adminService.getDistrictToDropdown();
		genderList = driverConductorTrainingService.GetGenderToDropdown();
		saveMode = true;
		updateMode = false;
		disableMode = false;
		disableModeTrnType = false;
		disDocument = true;
		selectedLanguage = "S";
	}

	public void save() {
		issuedDate = registrationDto.getDateOfIssue();
		expireDate = registrationDto.getDateOfExpiry();
		if (validateForm()) {
			if (registrationDto.getNic() != null && !registrationDto.getNic().isEmpty()) {
				String strNic = registrationDto.getNic();
				String strTrainingType = registrationDto.getTrainingType().getCode();

				String strName = driverConductorTrainingService.checkDuplicateNICforSameTraining(strNic,
						strTrainingType);
				if ((strName != null)) {
					RequestContext.getCurrentInstance().execute("PF('dlgRejectConfirm').show()");
				} else {
					registrationDto.setCreatedBy(loginUser);
					registrationDto.setStatusType("P");
					registrationDto.setOldApp(oldAppNo);
					registrationDto.setIsDuplicate(isDuplicate);
					registrationDto.setTrainingLanguage(selectedLanguage);

					newRregistrationDto = driverConductorTrainingService.insertDriverConductorRegNew(registrationDto,
							loginUser);

					// if (isSuccess) {
					if (newRregistrationDto.getAppNo() != null) {
						registrationDto.setAppNo(newRregistrationDto.getAppNo());
						registrationDto.setDriverConductorId(newRregistrationDto.getDriverConductorId());

						setSucessMsg("Driver/Conductor Registration Saved Successfully");
						RequestContext.getCurrentInstance().update("successSve");
						RequestContext.getCurrentInstance().execute("PF('successSve').show()");

						driverConductorTrainingService.beanLinkMethod(newRregistrationDto, loginUser,
								"Driver/Conductor Registration Saved", "Driver/Conductor Registration");
						saveMode = false;
						updateMode = true;
						disableMode = true;
						disableModeTrnType = true;
						disDocument = false;
					} else {
						setErrorMsg("Data Not Saved.");
						RequestContext.getCurrentInstance().update("requiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}
				}
			}
		}
	}

	public void clear() {

		registrationDto = new DriverConductorRegistrationDTO();
		selectedPermit = new PermitDTO();
		totalPoints = 0;
		saveMode = true;
		updateMode = false;
		disableMode = false;
		disableModeTrnType = false;
		disDocument = true;
		selectedLanguage = "S";
	}

	public void onTrainingTypeChange() {

		if (registrationDto.getTrainingType().getCode() != null
				&& !registrationDto.getTrainingType().getCode().isEmpty()) {
			for (CommonDTO commonDTO : trainingTypeList) {
				if (commonDTO.getCode().equalsIgnoreCase(registrationDto.getTrainingType().getCode())) {
					totalPoints = commonDTO.getPoints();
					registrationDto.setTrainingType(commonDTO);
					registrationDto.setPoints(commonDTO.getPoints());
					disableModeTrnType = true;
					break;

				}
			}
		}
	}

	public void onPermitChange() {
		if (registrationDto.getPermitNo() != null && !registrationDto.getPermitNo().isEmpty()) {
			for (PermitDTO permit : permitList) {
				if (permit.getPermitNo().equalsIgnoreCase(registrationDto.getPermitNo())) {
					selectedPermit = permit;
					registrationDto.setRouteNo(permit.getRouteNo());
					registrationDto.setFrom(permit.getOrigin());
					registrationDto.setTo(permit.getDestination());
					break;
				}
			}
		}
	}

	public void update() {
		issuedDate = registrationDto.getDateOfIssue();
		expireDate = registrationDto.getDateOfExpiry();

		if (validateForm()) {
			registrationDto.setModifiedBy(loginUser);
			registrationDto.setStatus("P");
			registrationDto.setStatusType("P");
			registrationDto.setOldApp(oldAppNo);
			registrationDto.setIsDuplicate(isDuplicate);
			registrationDto.setTrainingLanguage(selectedLanguage);

			boolean isUpdated = driverConductorTrainingService.updateDriverConductorRegistration(registrationDto,
					loginUser);
			if (isUpdated) {
				driverConductorTrainingService.beanLinkMethod(registrationDto, loginUser,
						"Update Driver/Conductor Registration", "Driver/Conductor Registration");
				setSucessMsg("Data Updated Successfully");
				RequestContext.getCurrentInstance().update("successSve");
				RequestContext.getCurrentInstance().execute("PF('successSve').show()");

				saveMode = false;
				updateMode = true;
				disableMode = true;
				disableModeTrnType = true;
			} else {
				setErrorMsg("Data Not Updated.");
				RequestContext.getCurrentInstance().update("requiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		}
	}

	public void onVehicleChange() {
		if (registrationDto.getVehicleRegNo() != null && !registrationDto.getVehicleRegNo().isEmpty()) {
			vehicleDetList = driverConductorTrainingService.getVehicleDetails(registrationDto.getVehicleRegNo());
			if (!(vehicleDetList.isEmpty())) {
				for (CommonDTO commonDTO : vehicleDetList) {
					registrationDto.setPermitNo(commonDTO.getPermitNo());
					registrationDto.setFrom(commonDTO.getOrigin());
					registrationDto.setTo(commonDTO.getDestination());
					registrationDto.setRouteNo(commonDTO.getRouteNo());
					break;
				}
			} else {
				registrationDto.setPermitNo(null);
				registrationDto.setFrom(null);
				registrationDto.setTo(null);
				registrationDto.setRouteNo(null);
			}
		}
	}

	public void dateValidation() {
		if ((registrationDto.getDateOfIssue() != null) && (registrationDto.getExpiryDate() != null)
				&& (registrationDto.getExpiryDate().before(registrationDto.getDateOfIssue()))) {
			setErrorMsg("Expire date should be grater than Issue date.");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public void onNicChange() {
		if (registrationDto.getNic() != null && !registrationDto.getNic().isEmpty()) {
			String strNic = registrationDto.getNic();
			String strTrainingType = registrationDto.getTrainingType().getCode();

			String strName = driverConductorTrainingService.checkDuplicateNICforSameTraining(strNic, strTrainingType);
			if (!(strName.isEmpty()) || !(strName == null)) {

				RequestContext.getCurrentInstance().execute("PF('dlgRejectConfirm').show()");

			}
		}
	}

	public void documentManagement() {
		try {

			if (registrationDto.getTrainingType().getCode().equals("ND")) {
				sessionBackingBean.setTransactionType("NEW DRIVER");
			} else {
				sessionBackingBean.setTransactionType("NEW CONDUCTOR");
			}

			sessionBackingBean.setDriverConductorId(registrationDto.getDriverConductorId());
			sessionBackingBean.setDcAppNo(registrationDto.getAppNo());

			String strTransactionType = registrationDto.getTrainingType().getCode();

			setMandatoryList(documentManagementService.mandatoryDocsFordriverConductor(strTransactionType,
					registrationDto.getDriverConductorId()));
			setOptionalList(documentManagementService.optionalDocsFordriverConductor(strTransactionType,
					registrationDto.getDriverConductorId()));

			sessionBackingBean.sisuSariyaMandatoryDocumentList = documentManagementService
					.driverConductorMandatoryListM(registrationDto.getDriverConductorId(), strTransactionType);
			sessionBackingBean.sisuSariyaOptionalDocumentList = documentManagementService
					.driverConductorOptionalListM(registrationDto.getDriverConductorId(), strTransactionType);

			RequestContext.getCurrentInstance().execute("PF('uploadDocument').show()");

			driverConductorTrainingService.beanLinkMethod(registrationDto, loginUser, "Document Submit",
					"Driver/Conductor Registration");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void deleteRecordAction() {
		clear();
	}

	public void proceedAction() {
		isDuplicate = "Y";
		registrationDto.setCreatedBy(loginUser);
		registrationDto.setStatusType("P");
		registrationDto.setOldApp(oldAppNo);
		registrationDto.setIsDuplicate(isDuplicate);

		newRregistrationDto = driverConductorTrainingService.insertDriverConductorRegNew(registrationDto, loginUser);

		// if (isSuccess) {
		if (newRregistrationDto.getAppNo() != null) {
			registrationDto.setAppNo(newRregistrationDto.getAppNo());
			registrationDto.setDriverConductorId(newRregistrationDto.getDriverConductorId());

			setSucessMsg("Driver/Conductor Registration Saved Successfully");
			RequestContext.getCurrentInstance().update("successSve");
			RequestContext.getCurrentInstance().execute("PF('successSve').show()");

			driverConductorTrainingService.beanLinkMethod(newRregistrationDto, loginUser,
					"Driver/Conductor Registration Saved", "Driver/Conductor Registration");
			
			saveMode = false;
			updateMode = true;
			disableMode = true;
			disableModeTrnType = true;
		} else {
			setErrorMsg("Data Not Saved.");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	// private methods

	private boolean validateForm() {
		boolean ret = true;
		Date today = new Date();

		if (registrationDto.getTrainingType().getCode() == null
				|| registrationDto.getTrainingType().getCode().isEmpty()) {
			setErrorMsg("Please select a Training Type.");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			return false;
		}

		if (registrationDto.getNic() == null || registrationDto.getNic().trim().isEmpty()) {
			setErrorMsg("NIC required.");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

			return false;

		} else {
			String str = registrationDto.getNic().trim();
			registrationDto.setNic(str);
		}

		if (registrationDto.getNameWithInitials() == null || registrationDto.getNameWithInitials().trim().isEmpty()) {
			setErrorMsg("Name with Initials required.");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			return false;
		} else {
			String str = registrationDto.getNameWithInitials().trim();
			registrationDto.setNameWithInitials(str);
		}

		if (registrationDto.getFullNameEng() == null || registrationDto.getFullNameEng().trim().isEmpty()) {
			setErrorMsg("Full Name required.");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			return false;
		} else {
			String str = registrationDto.getFullNameEng().trim();
			registrationDto.setFullNameEng(str);
		}

		if (registrationDto.getGender().getCode() == null || registrationDto.getGender().getCode().isEmpty()) {
			setErrorMsg("Please select Gender.");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			return false;
		}

		if (registrationDto.getDob() == null) {
			setErrorMsg("Please select Date of Birth.");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			return false;
		} else if (registrationDto.getDob().getTime() > today.getTime()) {
			setErrorMsg("Invalid Date of Birth.");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			return false;
		}

		if (registrationDto.getDistrict().getCode() == null || registrationDto.getDistrict().getCode().isEmpty()) {
			setErrorMsg("Please select District.");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			return false;
		}

		if (registrationDto.getNic() == null && registrationDto.getNic().trim().isEmpty()) {

			return false;
		}

		if (registrationDto.getAdd1Eng() == null || registrationDto.getAdd1Eng().trim().isEmpty()) {
			setErrorMsg("Address 1 required.");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			return false;
		} else {
			String str = registrationDto.getAdd1Eng().trim();
			registrationDto.setAdd1Eng(str);
		}

		if (registrationDto.getCityEng() == null || registrationDto.getCityEng().trim().isEmpty()) {
			setErrorMsg("City required.");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			return false;
		} else {
			String str = registrationDto.getCityEng().trim();
			registrationDto.setCityEng(str);
		}

		if (registrationDto.getContactNo() == null || registrationDto.getContactNo().trim().isEmpty()) {
			setErrorMsg("Contact Number required.");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			return false;
		} else {
			String str = registrationDto.getContactNo().trim();
			registrationDto.setContactNo(str);
		}

		if (registrationDto.getEducation() == null || registrationDto.getEducation().trim().isEmpty()) {
			setErrorMsg("Education Qualification required.");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			return false;
		} else {
			String str = registrationDto.getEducation().trim();
			registrationDto.setEducation(str);
		}

		if (registrationDto.getGsCertificateNo() == null || registrationDto.getGsCertificateNo().trim().isEmpty()) {
			setErrorMsg("GS Certificate No. required.");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			return false;
		} else {
			String str = registrationDto.getGsCertificateNo().trim();
			registrationDto.setGsCertificateNo(str);
		}

		if (registrationDto.getTrainingType().getType().equalsIgnoreCase("D")) {
			if (registrationDto.getLicenseNo() == null || registrationDto.getLicenseNo().trim().isEmpty()) {
				setErrorMsg("Driving License No. required.");
				RequestContext.getCurrentInstance().update("requiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				return false;
			} else {
				String str = registrationDto.getLicenseNo().trim();
				registrationDto.setLicenseNo(str);
			}
		}

		if ((issuedDate != null) && (expireDate != null) && (expireDate.before(issuedDate))) {
			setErrorMsg("Expire date should be grater than Issue date.");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			return false;
		}
		/** added by dinushi.r on 26-05-2020 */

		if ((registrationDto.getDateOfIssueLicence() != null) && (registrationDto.getDateOfExpiryLicence() != null)
				&& (registrationDto.getDateOfExpiryLicence().before(registrationDto.getDateOfIssueLicence()))) {
			setErrorMsg("Driving License Expire Date should be grater than Driving License Issue Date.");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			return false;
		}

		if (registrationDto.getGscissuedate() == null) {
			setErrorMsg("Please select Issued Date for GS Certificate .");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			return false;
		}
		if (registrationDto.getPcissuedate() == null) {
			setErrorMsg("Please select Issued Date for Police Clearance.");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			return false;
		}
		if (registrationDto.getTrainingType().getType().equalsIgnoreCase("D")) {
			if (registrationDto.getMedicalcertificateNo() == null
					|| registrationDto.getMedicalcertificateNo().trim().isEmpty()) {
				setErrorMsg("Medical Certificate Number  required.");
				RequestContext.getCurrentInstance().update("requiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				return false;
			}
		}

		if (registrationDto.getTrainingType().getType().equalsIgnoreCase("D")) {
			if (registrationDto.getMrissuedate() == null) {
				setErrorMsg("Please select Issued Date for Medical Report .");
				RequestContext.getCurrentInstance().update("requiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				return false;
			}
		}

		if (registrationDto.getTrainingType().getType().equalsIgnoreCase("D")) {
			if (registrationDto.getMrexpiredate() == null) {
				setErrorMsg("Please select Expire Date for Medical Report .");
				RequestContext.getCurrentInstance().update("requiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				return false;
			}
		}

		if ((registrationDto.getMrissuedate() != null) && (registrationDto.getMrexpiredate() != null)
				&& (registrationDto.getMrexpiredate().before(registrationDto.getMrissuedate()))) {
			setErrorMsg("Medical Report Expire Date should be grater than Medical Report Issue Date.");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			return false;
		}
		/** end change **/
		return ret;
	}
	
	//added by danilka.j
	public void validateNicFromUserinput(AjaxBehaviorEvent event) {
	    String nic = (String) ((UIInput) event.getComponent()).getValue();
	    String strTrainingType = registrationDto.getTrainingType().getCode();
	    //System.out.println(nic + "'training type : " + strTrainingType);
	    String strName = driverConductorTrainingService.checkDuplicateNICforSameTraining(nic, strTrainingType);
	    if(strTrainingType != null) {
	    	if ((strName != null)) {
		    	//System.out.println(strName+" present");
		        RequestContext.getCurrentInstance().execute("PF('dlgRejectConfirm').show()");
		    }
	    }else {
	    	setErrorMsg("Please select Type of Training .");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
	    }
	    
	}


	// getters and setters

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public DriverConductorRegistrationDTO getRegistrationDto() {
		return registrationDto;
	}

	public void setRegistrationDto(DriverConductorRegistrationDTO registrationDto) {
		this.registrationDto = registrationDto;
	}

	public int getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(int totalPoints) {
		this.totalPoints = totalPoints;
	}

	public List<CommonDTO> getTrainingTypeList() {
		return trainingTypeList;
	}

	public void setTrainingTypeList(List<CommonDTO> trainingTypeList) {
		this.trainingTypeList = trainingTypeList;
	}

	public List<CommonDTO> getGenderList() {
		return genderList;
	}

	public void setGenderList(List<CommonDTO> genderList) {
		this.genderList = genderList;
	}

	public List<CommonDTO> getDistrictList() {
		return districtList;
	}

	public void setDistrictList(List<CommonDTO> districtList) {
		this.districtList = districtList;
	}

	public List<PermitDTO> getPermitList() {
		return permitList;
	}

	public void setPermitList(List<PermitDTO> permitList) {
		this.permitList = permitList;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(String loginUser) {
		this.loginUser = loginUser;
	}

	public PermitDTO getSelectedPermit() {
		return selectedPermit;
	}

	public void setSelectedPermit(PermitDTO selectedPermit) {
		this.selectedPermit = selectedPermit;
	}

	public DriverConductorTrainingService getDriverConductorTrainingService() {
		return driverConductorTrainingService;
	}

	public void setDriverConductorTrainingService(DriverConductorTrainingService driverConductorTrainingService) {
		this.driverConductorTrainingService = driverConductorTrainingService;
	}

	public boolean isSaveMode() {
		return saveMode;
	}

	public void setSaveMode(boolean saveMode) {
		this.saveMode = saveMode;
	}

	public boolean isUpdateMode() {
		return updateMode;
	}

	public void setUpdateMode(boolean updateMode) {
		this.updateMode = updateMode;
	}

	public DriverConductorRegistrationDTO getNewRregistrationDto() {
		return newRregistrationDto;
	}

	public void setNewRregistrationDto(DriverConductorRegistrationDTO newRregistrationDto) {
		this.newRregistrationDto = newRregistrationDto;
	}

	public boolean isDisableMode() {
		return disableMode;
	}

	public void setDisableMode(boolean disableMode) {
		this.disableMode = disableMode;
	}

	public boolean isDisableModeTrnType() {
		return disableModeTrnType;
	}

	public void setDisableModeTrnType(boolean disableModeTrnType) {
		this.disableModeTrnType = disableModeTrnType;
	}

	public List<CommonDTO> getVehicleDetList() {
		return vehicleDetList;
	}

	public void setVehicleDetList(List<CommonDTO> vehicleDetList) {
		this.vehicleDetList = vehicleDetList;
	}

	public String getSucessMsg() {
		return sucessMsg;
	}

	public void setSucessMsg(String sucessMsg) {
		this.sucessMsg = sucessMsg;
	}

	public String getOldAppNo() {
		return oldAppNo;
	}

	public void setOldAppNo(String oldAppNo) {
		this.oldAppNo = oldAppNo;
	}

	public String getIsDuplicate() {
		return isDuplicate;
	}

	public void setIsDuplicate(String isDuplicate) {
		this.isDuplicate = isDuplicate;
	}

	public String getConfirmationMsg() {
		return confirmationMsg;
	}

	public void setConfirmationMsg(String confirmationMsg) {
		this.confirmationMsg = confirmationMsg;
	}

	public boolean isDisDocument() {
		return disDocument;
	}

	public void setDisDocument(boolean disDocument) {
		this.disDocument = disDocument;
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

	public String getSelectedLanguage() {
		return selectedLanguage;
	}

	public void setSelectedLanguage(String selectedLanguage) {
		this.selectedLanguage = selectedLanguage;
	}

}
