package lk.informatics.ntc.view.beans;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.io.FileUtils;
import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.DocumentManagementDTO;
import lk.informatics.ntc.model.dto.DriverConductorRegistrationDTO;
import lk.informatics.ntc.model.dto.PermitDTO;
import lk.informatics.ntc.model.exception.ApplicationException;
import lk.informatics.ntc.model.service.AdminService;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.DocumentManagementService;
import lk.informatics.ntc.model.service.DriverConductorTrainingService;
import lk.informatics.ntc.model.service.UploadDriverConductorPhotoService;
import lk.informatics.ntc.view.util.NICValidator;
import lk.informatics.ntc.view.util.PropertyReader;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "reDriverConductorRegistrationBean")
@ViewScoped
public class ReDriverConductorRegistrationBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private CommonService commonService;
	private DriverConductorTrainingService driverConductorTrainingService;
	private AdminService adminService;
	private DocumentManagementService documentManagementService;
	private UploadDriverConductorPhotoService uploadDriverConducPhotoService;

	private DriverConductorRegistrationDTO registrationDto;
	private DriverConductorRegistrationDTO newRregistrationDto;
	private DriverConductorRegistrationDTO onDriConducIdChangeDto;
	private int totalPoints = 0;
	private List<CommonDTO> trainingTypeList;
	private List<CommonDTO> genderList;
	private List<CommonDTO> districtList;
	private List<PermitDTO> permitList;
	private List<CommonDTO> vehicleDetList;
	private List<DriverConductorRegistrationDTO> nicNoList;
	private List<DriverConductorRegistrationDTO> driverConNoList;
	private PermitDTO selectedPermit;
	private String sucessMsg, errorMsg, loginUser, oldAppNo, isDuplicate, confirmationMsg, strSelectedType,
			strSelectedNic, photoPath, editedNIC, selectedLanguage;
	private boolean saveMode, updateMode, disableMode, disDocument, disableModeNic, photoHave, duplicateTraining;
	Date issuedDate, expireDate;
	private List<DocumentManagementDTO> mandatoryList = new ArrayList<DocumentManagementDTO>(0);
	private List<DocumentManagementDTO> optionalList = new ArrayList<DocumentManagementDTO>(0);
	DriverConductorRegistrationDTO photoDto = new DriverConductorRegistrationDTO();
	private boolean disableModeID, disableModeDcID, disabledForDriver;
	String appNo = null;
	private String noOfDuplicateTraining = null;
	private byte[] image = null;
	private NICValidator nicValidator;

	@PostConstruct
	public void init() {
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		driverConductorTrainingService = (DriverConductorTrainingService) SpringApplicationContex
				.getBean("driverConductorTrainingService");
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");
		documentManagementService = (DocumentManagementService) SpringApplicationContex
				.getBean("documentManagementService");
		loginUser = sessionBackingBean.getLoginUser();
		uploadDriverConducPhotoService = (UploadDriverConductorPhotoService) SpringApplicationContex
				.getBean("uploadDriverConducPhotoService");
		registrationDto = new DriverConductorRegistrationDTO();
		selectedPermit = new PermitDTO();
		trainingTypeList = driverConductorTrainingService.GetTrainingTypesByModeForRe("C");

		permitList = driverConductorTrainingService.getActivePermitList();
		districtList = adminService.getDistrictToDropdown();
		genderList = driverConductorTrainingService.GetGenderToDropdown();
		saveMode = true;
		updateMode = false;
		disableMode = false;
		disDocument = true;
		disableModeID = true;
		disableModeDcID = true;
		photoHave = false;
		disabledForDriver = true;
		duplicateTraining = false;
		selectedLanguage = "S";
	}

	public void save() {
		issuedDate = registrationDto.getDateOfIssue();
		expireDate = registrationDto.getDateOfExpiry();

		String oldAppno;
		String nicNo;
		String trainingType;
		String driverConducId;
		if (validateForm()) {
			registrationDto.setTrainingLanguage(selectedLanguage);
			if (registrationDto.getNic() != null && !registrationDto.getNic().isEmpty()) {
				String strNic = registrationDto.getNic();
				String strTrainingType = registrationDto.getTrainingType().getCode();

				int count = driverConductorTrainingService.checkAlreadyActiveTrainingForNIC(editedNIC);
				int blacklistCount = driverConductorTrainingService.checkBlackListCount(editedNIC, "B");
				int pendinglacklistCount = driverConductorTrainingService.checkBlackListCount(editedNIC, "PB");

				if (count == 0) {
					if (pendinglacklistCount == 0) {
						if (blacklistCount == 0) {

							registrationDto.setCreatedBy(loginUser);
							registrationDto.setStatusType("P");

							registrationDto.setIsDuplicate(isDuplicate);
							trainingType = registrationDto.getTrainingType().getCode();
							driverConducId = registrationDto.getDriverConductorId();
							nicNo = registrationDto.getNic();
							oldAppno = driverConductorTrainingService.getOldAppNo(driverConducId, nicNo);
							registrationDto.setOldApp(oldAppno);
							if (!editedNIC.equals(nicNo)) {
								photoHave = uploadDriverConducPhotoService.checkAvailabilityOfPhotoByNIC(nicNo);
								driverConductorTrainingService.updateNewNic(editedNIC, oldAppno, nicNo, loginUser,
										photoHave);
								registrationDto.setNic(editedNIC);
							}
							newRregistrationDto = driverConductorTrainingService
									.insertDriverConductorReReg(registrationDto);

							if (newRregistrationDto.getAppNo() != null) {
								registrationDto.setAppNo(newRregistrationDto.getAppNo());
								registrationDto.setDriverConductorId(newRregistrationDto.getDriverConductorId());

								setSucessMsg("Driver/Conductor Registration Saved Successfully");
								RequestContext.getCurrentInstance().update("successSve");
								RequestContext.getCurrentInstance().execute("PF('successSve').show()");

								driverConductorTrainingService.beanLinkMethod(registrationDto, loginUser, "Driver/Conductor Registration Save", "Driver/Conductor Re-Registration");
								saveMode = false;
								updateMode = true;
								disableMode = true;
								disDocument = false;
							} else {
								setErrorMsg("Data Not Saved.");
								RequestContext.getCurrentInstance().update("requiredField");
								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
							}

						} else {
							setErrorMsg("Already found approved blacklist record.");
							RequestContext.getCurrentInstance().update("requiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
						}
					} else {
						setErrorMsg("Already found pending blacklist record.");
						RequestContext.getCurrentInstance().update("requiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}
				} else {
					setErrorMsg("Already found pending record.");
					RequestContext.getCurrentInstance().update("requiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				}
			} else {
				setErrorMsg("NIC number is empty.");
				RequestContext.getCurrentInstance().update("requiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
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
		disableModeID = true;
		disableModeDcID = true;
		disableModeDcID = true;
		photoHave = false;
		photoPath = null;
		disabledForDriver = true;
		editedNIC = null;
		duplicateTraining = false;
	}

	public void onIdNoChange() {
		disableModeDcID = false;
		driverConNoList = driverConductorTrainingService.getDCIDListByID(registrationDto.getNic());
	}

	public void onDriverConductorChange() throws ParseException {

		onDriConducIdChangeDto = driverConductorTrainingService
				.getDetailsByDCId(registrationDto.getDriverConductorId());
		registrationDto.setNic(onDriConducIdChangeDto.getNic());
		editedNIC = onDriConducIdChangeDto.getNic();
		registrationDto.setNameWithInitials(onDriConducIdChangeDto.getNameWithInitials());
		registrationDto.setFullNameEng(onDriConducIdChangeDto.getFullNameEng());
		registrationDto.setFullNameSin(onDriConducIdChangeDto.getFullNameSin());
		registrationDto.setFullNameTam(onDriConducIdChangeDto.getFullNameTam());
		registrationDto.setNewGender(onDriConducIdChangeDto.getNewGender());
		registrationDto.setDob(onDriConducIdChangeDto.getDob());
		registrationDto.setNewDistrict(onDriConducIdChangeDto.getNewDistrict());
		registrationDto.setAdd1Eng(onDriConducIdChangeDto.getAdd1Eng());
		registrationDto.setAdd1Sin(onDriConducIdChangeDto.getAdd1Sin());
		registrationDto.setAdd1Tam(onDriConducIdChangeDto.getAdd1Tam());
		registrationDto.setAdd2Eng(onDriConducIdChangeDto.getAdd2Eng());
		registrationDto.setAdd2Sin(onDriConducIdChangeDto.getAdd2Sin());
		registrationDto.setAdd2Tam(onDriConducIdChangeDto.getAdd2Tam());
		registrationDto.setCityEng(onDriConducIdChangeDto.getCityEng());
		registrationDto.setCitySin(onDriConducIdChangeDto.getCitySin());
		registrationDto.setCityTam(onDriConducIdChangeDto.getCityTam());
		registrationDto.setContactNo(onDriConducIdChangeDto.getContactNo());
		registrationDto.setLicenseNo(onDriConducIdChangeDto.getLicenseNo());
		totalPoints = onDriConducIdChangeDto.getViewDemeritPoint();
		if (onDriConducIdChangeDto.getDateOfIssueN() != null
				&& !onDriConducIdChangeDto.getDateOfIssueN().trim().isEmpty()) {
			Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(onDriConducIdChangeDto.getDateOfIssueN());
			registrationDto.setDateOfIssue(date1);
		}
		if (onDriConducIdChangeDto.getExpiryDateN() != null
				&& !onDriConducIdChangeDto.getExpiryDateN().trim().isEmpty()) {
			Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(onDriConducIdChangeDto.getExpiryDateN());
			registrationDto.setDateOfExpiry(date2);
		}

		if (onDriConducIdChangeDto.getFirstDateOfIssueN() != null
				&& !onDriConducIdChangeDto.getFirstDateOfIssueN().trim().isEmpty()) {
			Date date3 = new SimpleDateFormat("dd/MM/yyyy").parse(onDriConducIdChangeDto.getFirstDateOfIssueN());
			registrationDto.setFirstDateOfIssue(date3);
		}

		if (onDriConducIdChangeDto.getDateOfIssueLicenceN() != null
				&& !onDriConducIdChangeDto.getDateOfIssueLicenceN().trim().isEmpty()) {
			Date date4 = new SimpleDateFormat("dd/MM/yyyy").parse(onDriConducIdChangeDto.getDateOfIssueLicenceN());
			registrationDto.setDateOfIssueLicence(date4);
		}

		if (onDriConducIdChangeDto.getDateOfExpiryLicenceN() != null
				&& !onDriConducIdChangeDto.getDateOfExpiryLicenceN().trim().isEmpty()) {
			Date date5 = new SimpleDateFormat("dd/MM/yyyy").parse(onDriConducIdChangeDto.getDateOfExpiryLicenceN());
			registrationDto.setDateOfExpiryLicence(date5);
		}
		registrationDto.setEducation(onDriConducIdChangeDto.getEducation());
		registrationDto.setGsCertificateNo(onDriConducIdChangeDto.getGsCertificateNo());
		registrationDto.setPoliceClearanceStation(onDriConducIdChangeDto.getPoliceClearanceStation());
		registrationDto.setVehicleRegNo(onDriConducIdChangeDto.getVehicleRegNo());
		registrationDto.setPermitNo(onDriConducIdChangeDto.getPermitNo());
		registrationDto.setRouteNo(onDriConducIdChangeDto.getRouteNo());
		registrationDto.setFrom(onDriConducIdChangeDto.getFrom());
		registrationDto.setTo(onDriConducIdChangeDto.getTo());

		registrationDto.setGscissuedate(onDriConducIdChangeDto.getGscissuedate());
		registrationDto.setPcissuedate(onDriConducIdChangeDto.getPcissuedate());
		registrationDto.setMedicalcertificateNo(onDriConducIdChangeDto.getMedicalcertificateNo());
		registrationDto.setMrissuedate(onDriConducIdChangeDto.getMrissuedate());
		registrationDto.setMrexpiredate(onDriConducIdChangeDto.getMrexpiredate());
		registrationDto.setSpecialRemarks(onDriConducIdChangeDto.getSpecialRemarks());
		photoHave = uploadDriverConducPhotoService.checkAvailabilityOfPhotoByNIC(registrationDto.getNic());

		if (photoHave) {

			getImageContentsAsBase64();
		}

		if (registrationDto.getTrainingType().getCode().equalsIgnoreCase("DD")
				|| registrationDto.getTrainingType().getCode().equalsIgnoreCase("DC")) {
			duplicateTraining = true;
			noOfDuplicateTraining = driverConductorTrainingService
					.getNoOfDuplicateTraining(registrationDto.getTrainingType().getCode(), registrationDto.getNic());
		}
	}

	public String getImageContentsAsBase64() {

		// check availability of photos for selected app no
		photoHave = uploadDriverConducPhotoService.checkAvailabilityOfPhotoByNIC(registrationDto.getNic());

		if (photoHave) {

			photoDto = uploadDriverConducPhotoService.showimageByNIC(registrationDto.getNic());

			return Base64.getEncoder().encodeToString(photoDto.getImage());
		} else {
			if (image != null) {
				return Base64.getEncoder().encodeToString(image);
			}
			return null;
		}
	}

	public void onTrainingTypeChange() {
		duplicateTraining = false;
		photoHave = false;
		if (registrationDto.getTrainingType().getCode() != null
				&& !registrationDto.getTrainingType().getCode().isEmpty()) {
			for (CommonDTO commonDTO : trainingTypeList) {
				if (commonDTO.getCode().equalsIgnoreCase(registrationDto.getTrainingType().getCode())) {
					totalPoints = commonDTO.getPoints();
					registrationDto.setTrainingType(commonDTO);
					registrationDto.setPoints(commonDTO.getPoints());
					break;
				}
			}

			disableModeID = false;
			disableModeDcID = false;
			nicNoList = driverConductorTrainingService.getNicNumberList();
			registrationDto.setNic(null);

			registrationDto.setNameWithInitials(null);
			registrationDto.setFullNameEng(null);
			registrationDto.setFullNameSin(null);
			registrationDto.setFullNameTam(null);
			registrationDto.setNewGender(null);
			registrationDto.setDob(null);
			registrationDto.setNewDistrict(null);
			registrationDto.setAdd1Eng(null);
			registrationDto.setAdd1Sin(null);
			registrationDto.setAdd1Tam(null);
			registrationDto.setAdd2Eng(null);
			registrationDto.setAdd2Sin(null);
			registrationDto.setAdd2Tam(null);
			registrationDto.setCityEng(null);
			registrationDto.setCitySin(null);
			registrationDto.setCityTam(null);
			registrationDto.setContactNo(null);
			registrationDto.setLicenseNo(null);

			registrationDto.setDateOfIssue(null);

			registrationDto.setDateOfExpiry(null);

			registrationDto.setFirstDateOfIssue(null);

			registrationDto.setDateOfIssueLicence(null);

			registrationDto.setDateOfExpiryLicence(null);

			registrationDto.setEducation(null);
			registrationDto.setGsCertificateNo(null);
			registrationDto.setPoliceClearanceStation(null);
			registrationDto.setVehicleRegNo(null);
			registrationDto.setPermitNo(null);
			registrationDto.setRouteNo(null);
			registrationDto.setFrom(null);
			registrationDto.setTo(null);

			registrationDto.setGscissuedate(null);
			registrationDto.setPcissuedate(null);
			registrationDto.setMedicalcertificateNo(null);
			registrationDto.setMrissuedate(null);
			registrationDto.setMrexpiredate(null);
			registrationDto.setSpecialRemarks(null);
			registrationDto.setDriverConductorId(null);
			editedNIC = null;
			// driverConNoList=driverConductorTrainingService.getNicNumberList();
			// // commented by tharushi.e for filter driver conductor nos
			// according to training type
			driverConNoList = driverConductorTrainingService
					.getDriverConducFilterListForReReg(registrationDto.getTrainingType().getCode());
			if (registrationDto.getTrainingType().getCode().equals("FD")
					|| registrationDto.getTrainingType().getCode().equals("DD")
					|| registrationDto.getTrainingType().getCode().equals("RD")
					|| registrationDto.getTrainingType().getCode().equals("RRD")
					|| registrationDto.getTrainingType().getCode().equals("RRRD")) {

				disabledForDriver = false;
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
		String oldAppno;
		String nicNo;
		String driverConducId;

		issuedDate = registrationDto.getDateOfIssue();
		expireDate = registrationDto.getDateOfExpiry();
		if (validateForm()) {
			registrationDto.setModifiedBy(loginUser);
			registrationDto.setStatus("P");
			registrationDto.setStatusType("P");
			registrationDto.setOldApp(oldAppNo);
			registrationDto.setIsDuplicate(isDuplicate);

			driverConducId = registrationDto.getDriverConductorId();
			nicNo = registrationDto.getNic();
			oldAppno = driverConductorTrainingService.getOldAppNo(driverConducId, nicNo);
			registrationDto.setOldApp(oldAppno);
			if (!editedNIC.equals(nicNo)) {
				photoHave = uploadDriverConducPhotoService.checkAvailabilityOfPhotoByNIC(nicNo);
				driverConductorTrainingService.updateNewNic(editedNIC, oldAppno, nicNo, loginUser, photoHave);
				registrationDto.setNic(editedNIC);
			}

			boolean isUpdated = driverConductorTrainingService.updateDriverConductorRegistration(registrationDto, loginUser);
			if (isUpdated) {

				driverConductorTrainingService.beanLinkMethod(registrationDto, loginUser, "Update Driver/Conductor Registration", "Driver/Conductor Re-Registration");

				setSucessMsg("Data Updated Successfully");
				RequestContext.getCurrentInstance().update("successSve");
				RequestContext.getCurrentInstance().execute("PF('successSve').show()");

				saveMode = false;
				updateMode = true;
				disableMode = true;
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

		if (newRregistrationDto.getAppNo() != null) {
			registrationDto.setAppNo(newRregistrationDto.getAppNo());
			registrationDto.setDriverConductorId(newRregistrationDto.getDriverConductorId());

			setSucessMsg("Driver/Conductor Registration Saved Successfully");
			RequestContext.getCurrentInstance().update("successSve");
			RequestContext.getCurrentInstance().execute("PF('successSve').show()");

			saveMode = false;
			updateMode = true;
			disableMode = true;
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

		if (registrationDto.getNewGender() == null || registrationDto.getNewGender().isEmpty()) {
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

		if (registrationDto.getNewDistrict() == null || registrationDto.getNewDistrict().isEmpty()) {
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

		if (registrationDto.getTrainingType().getCode().equalsIgnoreCase("RD")
				|| registrationDto.getTrainingType().getCode().equalsIgnoreCase("DD")
				|| registrationDto.getTrainingType().getCode().equalsIgnoreCase("FD")
				|| registrationDto.getTrainingType().getCode().equalsIgnoreCase("RRD")
				|| registrationDto.getTrainingType().getCode().equalsIgnoreCase("RRRD")) {
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
		if ((registrationDto.getMedicalcertificateNo() == null
				|| registrationDto.getMedicalcertificateNo().trim().isEmpty())
				&& (registrationDto.getTrainingType().getCode().equalsIgnoreCase("RD")
						|| registrationDto.getTrainingType().getCode().equalsIgnoreCase("DD")
						|| registrationDto.getTrainingType().getCode().equalsIgnoreCase("FD")
						|| registrationDto.getTrainingType().getCode().equalsIgnoreCase("RRD")
						|| registrationDto.getTrainingType().getCode().equalsIgnoreCase("RRRD"))) {
			setErrorMsg("Medical Certificate Number  required.");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			return false;
		}
		if (registrationDto.getMrissuedate() == null
				&& (registrationDto.getTrainingType().getCode().equalsIgnoreCase("RD")
						|| registrationDto.getTrainingType().getCode().equalsIgnoreCase("DD")
						|| registrationDto.getTrainingType().getCode().equalsIgnoreCase("FD")
						|| registrationDto.getTrainingType().getCode().equalsIgnoreCase("RRD")
						|| registrationDto.getTrainingType().getCode().equalsIgnoreCase("RRRD"))) {
			setErrorMsg("Please select Issued Date for Medical Report .");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			return false;
		}
		if (registrationDto.getMrexpiredate() == null
				&& (registrationDto.getTrainingType().getCode().equalsIgnoreCase("RD")
						|| registrationDto.getTrainingType().getCode().equalsIgnoreCase("DD")
						|| registrationDto.getTrainingType().getCode().equalsIgnoreCase("FD")
						|| registrationDto.getTrainingType().getCode().equalsIgnoreCase("RRD")
						|| registrationDto.getTrainingType().getCode().equalsIgnoreCase("RRRD"))) {
			setErrorMsg("Please select Expire Date for Medical Report .");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			return false;
		}
		if ((registrationDto.getMrissuedate() != null) && (registrationDto.getMrexpiredate() != null)
				&& (registrationDto.getMrexpiredate().before(registrationDto.getMrissuedate()))
				&& (registrationDto.getTrainingType().getCode().equalsIgnoreCase("RD")
						|| registrationDto.getTrainingType().getCode().equalsIgnoreCase("DD")
						|| registrationDto.getTrainingType().getCode().equalsIgnoreCase("FD")
						|| registrationDto.getTrainingType().getCode().equalsIgnoreCase("RRD")
						|| registrationDto.getTrainingType().getCode().equalsIgnoreCase("RRRD"))) {
			setErrorMsg("Medical Report Expire Date should be grater than Medical Report Issue Date.");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			return false;
		}
		/** end change **/
		return ret;
	}

	public void documentManagement() {
		try {

			String trainingType = null;
			trainingType = driverConductorTrainingService
					.getTrainingTypeDes(registrationDto.getTrainingType().getCode());

			sessionBackingBean.setTransactionType(trainingType.toUpperCase());

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

		} catch (Exception e) {
			e.printStackTrace();
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

	public List<DriverConductorRegistrationDTO> getNicNoList() {
		return nicNoList;
	}

	public void setNicNoList(List<DriverConductorRegistrationDTO> nicNoList) {
		this.nicNoList = nicNoList;
	}

	public List<DriverConductorRegistrationDTO> getDriverConNoList() {
		return driverConNoList;
	}

	public void setDriverConNoList(List<DriverConductorRegistrationDTO> driverConNoList) {
		this.driverConNoList = driverConNoList;
	}

	public String getStrSelectedType() {
		return strSelectedType;
	}

	public void setStrSelectedType(String strSelectedType) {
		this.strSelectedType = strSelectedType;
	}

	public String getStrSelectedNic() {
		return strSelectedNic;
	}

	public void setStrSelectedNic(String strSelectedNic) {
		this.strSelectedNic = strSelectedNic;
	}

	public DriverConductorRegistrationDTO getOnDriConducIdChangeDto() {
		return onDriConducIdChangeDto;
	}

	public void setOnDriConducIdChangeDto(DriverConductorRegistrationDTO onDriConducIdChangeDto) {
		this.onDriConducIdChangeDto = onDriConducIdChangeDto;
	}

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
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

	public boolean isDisDocument() {
		return disDocument;
	}

	public void setDisDocument(boolean disDocument) {
		this.disDocument = disDocument;
	}

	public DocumentManagementService getDocumentManagementService() {
		return documentManagementService;
	}

	public void setDocumentManagementService(DocumentManagementService documentManagementService) {
		this.documentManagementService = documentManagementService;
	}

	public boolean isDisableModeNic() {
		return disableModeNic;
	}

	public void setDisableModeNic(boolean disableModeNic) {
		this.disableModeNic = disableModeNic;
	}

	public boolean isDisableModeID() {
		return disableModeID;
	}

	public void setDisableModeID(boolean disableModeID) {
		this.disableModeID = disableModeID;
	}

	public boolean isDisableModeDcID() {
		return disableModeDcID;
	}

	public void setDisableModeDcID(boolean disableModeDcID) {
		this.disableModeDcID = disableModeDcID;
	}

	public UploadDriverConductorPhotoService getUploadDriverConducPhotoService() {
		return uploadDriverConducPhotoService;
	}

	public void setUploadDriverConducPhotoService(UploadDriverConductorPhotoService uploadDriverConducPhotoService) {
		this.uploadDriverConducPhotoService = uploadDriverConducPhotoService;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public boolean isPhotoHave() {
		return photoHave;
	}

	public void setPhotoHave(boolean photoHave) {
		this.photoHave = photoHave;
	}

	public String getAppNo() {
		return appNo;
	}

	public void setAppNo(String appNo) {
		this.appNo = appNo;
	}

	public Date getIssuedDate() {
		return issuedDate;
	}

	public void setIssuedDate(Date issuedDate) {
		this.issuedDate = issuedDate;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public boolean isDisabledForDriver() {
		return disabledForDriver;
	}

	public void setDisabledForDriver(boolean disabledForDriver) {
		this.disabledForDriver = disabledForDriver;
	}

	public String getEditedNIC() {
		return editedNIC;
	}

	public void setEditedNIC(String editedNIC) {
		this.editedNIC = editedNIC;
	}

	public boolean isDuplicateTraining() {
		return duplicateTraining;
	}

	public void setDuplicateTraining(boolean duplicateTraining) {
		this.duplicateTraining = duplicateTraining;
	}

	public String getNoOfDuplicateTraining() {
		return noOfDuplicateTraining;
	}

	public void setNoOfDuplicateTraining(String noOfDuplicateTraining) {
		this.noOfDuplicateTraining = noOfDuplicateTraining;
	}

	public String getSelectedLanguage() {
		return selectedLanguage;
	}

	public void setSelectedLanguage(String selectedLanguage) {
		this.selectedLanguage = selectedLanguage;
	}

}
