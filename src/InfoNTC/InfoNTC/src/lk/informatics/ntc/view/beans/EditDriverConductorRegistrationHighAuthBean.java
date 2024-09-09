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

@ManagedBean(name = "editDriverConductorRegistrationHighAuthBean")
@ViewScoped
public class EditDriverConductorRegistrationHighAuthBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private CommonService commonService;
	private DriverConductorTrainingService driverConductorTrainingService;

	private AdminService adminService;
	private DocumentManagementService documentManagementService;
	private UploadDriverConductorPhotoService uploadDriverConducPhotoService;
	private DriverConductorRegistrationDTO registrationDto;
	private DriverConductorRegistrationDTO newRregistrationDto;
	DriverConductorRegistrationDTO photoDto = new DriverConductorRegistrationDTO();
	private List<DriverConductorRegistrationDTO> languageList;
	private List<DriverConductorRegistrationDTO> receiptList;
	private int totalPoints = 0;
	private List<CommonDTO> trainingTypeList;
	private List<CommonDTO> genderList;
	private List<CommonDTO> districtList;
	private List<PermitDTO> permitList;
	private List<CommonDTO> vehicleDetList;
	private List<CommonDTO> idNoList;
	private List<CommonDTO> idNoListForView;
	private List<CommonDTO> driverConList;
	private List<CommonDTO> driverConListForView;
	private PermitDTO selectedPermit;
	private String sucessMsg, errorMsg, loginUser, oldAppNo, isDuplicate, confirmationMsg, strSelectedType,
			strSelectedNic, strSelectedID, strSelectedGender, photoPath, strSelectedMedium, strSelectedRec;
	private boolean disableMode, hidePanel, disableNic, disableDCId, showPhotoPanel, photoHave, duplicateTraining,
			conductor;
	private Date issuedDate;
	private Date expireDate;
	private Date firstIssuedDate;
	private Date liceneExpireDate;
	private Date liceneIssuedDate;
	private List<DocumentManagementDTO> mandatoryList = new ArrayList<DocumentManagementDTO>(0);
	private List<DocumentManagementDTO> optionalList = new ArrayList<DocumentManagementDTO>();
	String appNo = null;
	private String noOfDuplicateTraining = null;
	private byte[] image = null;
	private NICValidator nicValidator;
	private boolean disableUpdate;
	private boolean disableType;
	@PostConstruct
	public void init() {
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		driverConductorTrainingService = (DriverConductorTrainingService) SpringApplicationContex
				.getBean("driverConductorTrainingService");
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");

		documentManagementService = (DocumentManagementService) SpringApplicationContex
				.getBean("documentManagementService");
		uploadDriverConducPhotoService = (UploadDriverConductorPhotoService) SpringApplicationContex
				.getBean("uploadDriverConducPhotoService");
		loginUser = sessionBackingBean.getLoginUser();

		registrationDto = new DriverConductorRegistrationDTO();
		selectedPermit = new PermitDTO();
		// trainingTypeList =
		// driverConductorTrainingService.GetTrainingTypesByMode("C");
		trainingTypeList = driverConductorTrainingService.GetAllTrainingTypes();
		permitList = driverConductorTrainingService.getActivePermitList();
		districtList = adminService.getDistrictToDropdown();
		genderList = driverConductorTrainingService.GetGenderToDropdown();
		idNoList = driverConductorTrainingService.getPendingIDList();
		idNoListForView = driverConductorTrainingService.getPendingIDListForView();
		driverConList = driverConductorTrainingService.getPendingDCIDList();
		driverConListForView = driverConductorTrainingService.getPendingDCIDListForView();
		languageList = driverConductorTrainingService.getTrainingLanguageList();

		disableMode = false;
		hidePanel = false;
		disableNic = true;
		disableDCId = true;
		showPhotoPanel = false;
		photoHave = false;
		duplicateTraining = false;
		conductor = false;
		disableUpdate = false;
		disableType =  true;
	}

	public void onTrainingTypeChange() {
		duplicateTraining = false;
		photoHave = false;
		if (strSelectedType != "") {
			disableNic = false;
			idNoList= driverConductorTrainingService.getAllIDListForViewByTrainingType(strSelectedType);
			driverConList = driverConductorTrainingService.getAllDCIDListByTrainingType(strSelectedType);;
			receiptList = driverConductorTrainingService.getReceiptNoForDriverConductor(strSelectedType);
		}
	}

	public void onIDNoChange() {
		if (strSelectedType != "") {
			if (strSelectedNic != "") {
				disableDCId = false;
				driverConList = driverConductorTrainingService.getDCIDListByTrainingandID(strSelectedType,
						strSelectedNic);
				if (strSelectedType.equalsIgnoreCase("DD") || strSelectedType.equalsIgnoreCase("DC")) {
					duplicateTraining = true;
					noOfDuplicateTraining = driverConductorTrainingService.getNoOfDuplicateTraining(strSelectedType,
							strSelectedNic);
				}

			} else {
				hidePanel = false;
				setErrorMsg("ID Number required.");
				RequestContext.getCurrentInstance().update("requiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		} else {
			hidePanel = false;
			setErrorMsg("Type of Training required.");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}
// for View Page 

	public void onDCIDNoChange() {

		registrationDto = new DriverConductorRegistrationDTO();
		hidePanel = false;
		showPhotoPanel = true;
		photoHave = uploadDriverConducPhotoService.checkAvailabilityOfPhotoByNIC(strSelectedNic);
		receiptList = driverConductorTrainingService.getReceiptNoForDriverConductor(strSelectedType);
		if (photoHave) {
			
			getImageContentsAsBase64();
		}
	}
	
	

	public String getImageContentsAsBase64() {

		// check availability of photos for selected app no
		photoHave = uploadDriverConducPhotoService.checkAvailabilityOfPhotoByNIC(strSelectedNic);
	
			if (photoHave) {
				appNo = uploadDriverConducPhotoService.getAppNoByNIC(strSelectedNic);

			photoDto = uploadDriverConducPhotoService.showimage(appNo);
			
			return Base64.getEncoder().encodeToString(photoDto.getImage());
		} else {
			if (image != null) {
				return Base64.getEncoder().encodeToString(image);
			}
			return null;
		}
	}

	public void mainSearch() throws ParseException {
		if (getStrSelectedType() != "") {
			if (strSelectedNic != "") {
				if (strSelectedID != "") {

					if (strSelectedType.equals("NC") || strSelectedType.equals("RC") || strSelectedType.equals("RRC")
							|| strSelectedType.equals("RRRC") || strSelectedType.equals("DC")
							|| strSelectedType.equals("FC")) {
						conductor = true;
					} else {
						conductor = false;
					}
					hidePanel = true;

					registrationDto = driverConductorTrainingService.getDetailsByDCIdandType(strSelectedID,
							strSelectedType);
					strSelectedGender = registrationDto.getNewGender();

					String strFirstIssueDate = registrationDto.getFirstDateOfIssueN();
					if (strFirstIssueDate != null) {
						if (!strFirstIssueDate.isEmpty()) {
							firstIssuedDate = new SimpleDateFormat("dd/MM/yyyy").parse(strFirstIssueDate);
							registrationDto.setFirstDateOfIssue(firstIssuedDate);
						}
					}

					String strIssueDate = registrationDto.getDateOfIssueN();
					if (strIssueDate != null) {
						if (!strIssueDate.isEmpty()) {
							issuedDate = new SimpleDateFormat("dd/MM/yyyy").parse(strIssueDate);
							registrationDto.setDateOfIssue(issuedDate);
						}
					}
					String strExpireDate = registrationDto.getExpiryDateN();
					if (strExpireDate != null) {
						if (!strExpireDate.isEmpty()) {
							expireDate = new SimpleDateFormat("dd/MM/yyyy").parse(strExpireDate);
							registrationDto.setExpiryDate(expireDate);
						}
					}

					String strLiceneIssueDate = registrationDto.getDateOfIssueLicenceN();
					if (strLiceneIssueDate != null) {
						if (!strLiceneIssueDate.isEmpty()) {
							liceneIssuedDate = new SimpleDateFormat("dd/MM/yyyy").parse(strLiceneIssueDate);
							registrationDto.setDateOfIssueLicence(liceneIssuedDate);
						}
					}

					String strLiceneExpireDate = registrationDto.getDateOfExpiryLicenceN();
					if (strLiceneExpireDate != null) {
						if (!strLiceneExpireDate.isEmpty()) {
							liceneExpireDate = new SimpleDateFormat("dd/MM/yyyy").parse(strLiceneExpireDate);
							registrationDto.setDateOfExpiryLicence(liceneExpireDate);
						}
					}

				} else {
					hidePanel = false;
					setErrorMsg("Driver/Conductor ID required.");
					RequestContext.getCurrentInstance().update("requiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			} else {
				hidePanel = false;
				setErrorMsg("ID Number required.");
				RequestContext.getCurrentInstance().update("requiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		} else {
			hidePanel = false;
			setErrorMsg("Type of Training required.");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void mainClear() {
		registrationDto = new DriverConductorRegistrationDTO();

		trainingTypeList = driverConductorTrainingService.GetAllTrainingTypes();
		idNoList = driverConductorTrainingService.getPendingIDList();
		driverConList = driverConductorTrainingService.getPendingDCIDList();
		driverConListForView = driverConductorTrainingService.getPendingDCIDListForView();
		hidePanel = false;
		strSelectedID = null;
		strSelectedNic = null;
		strSelectedType = null;
		strSelectedRec = null;
		disableNic = true;
		disableDCId = true;
		photoPath = null;
		photoHave = false;
		duplicateTraining = false;
		disableUpdate = false;
		disableType =true;
	}

	public void onNicChange() {
		if (registrationDto.getNic() != null && !registrationDto.getNic().isEmpty()) {
			
			String strNic = registrationDto.getNic();
			String strTrainingType = registrationDto.getTrainingType().getCode();
		/***NIC Validation removed - requested by pramitha***/	
			/*if(nicValidator.validateNIC(registrationDto.getNic(),strSelectedGender,registrationDto.getDob())) {
				
				disableUpdate= false;

			String strName = driverConductorTrainingService.checkDuplicateNICforSameTraining(strNic, strTrainingType);
			if (strName != null) {
				if (!(strName.isEmpty()) || !(strName == null)) {

					RequestContext.getCurrentInstance().execute("PF('dlgRejectConfirm').show()");

				}
			}
		}
			else {
				disableUpdate= true;
				setErrorMsg("Invalid NIC format");
				RequestContext.getCurrentInstance().update("requiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}*/
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

	public void update() throws ParseException {

	//	boolean receiptGenerated = driverConductorTrainingService.receiptGenerated(registrationDto.getAppNo());
	//	if (!receiptGenerated) {
			issuedDate = registrationDto.getDateOfIssue();
			expireDate = registrationDto.getExpiryDate();

			if (validateForm()) {
				registrationDto.setModifiedBy(loginUser);
				registrationDto.setStatus("P");
				registrationDto.setStatusType("P");
				registrationDto.setUpdateByHighAuth("Y");
				photoHave = uploadDriverConducPhotoService.checkAvailabilityOfPhotoByNIC(strSelectedNic);
				if (photoHave && registrationDto.getNic() != strSelectedNic) {
					driverConductorTrainingService.updateNICInPhotoUpload(strSelectedNic, registrationDto.getNic(),
							registrationDto.getAppNo(), loginUser);

				}
				boolean isUpdated = driverConductorTrainingService.updateDriverConductorByHighAuthOfficer(registrationDto,
						strSelectedType);

				if (isUpdated) {
					driverConductorTrainingService.beanLinkMethod(registrationDto, loginUser, "Update Driver Conductor By High Auth Officer", "Edit Driver/Conductor Details- Authorized");
					setSucessMsg("Data Updated Successfully");
					RequestContext.getCurrentInstance().update("successSve");
					RequestContext.getCurrentInstance().execute("PF('successSve').show()");

				} else {
					setErrorMsg("Data Not Updated.");
					RequestContext.getCurrentInstance().update("requiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			}
		/*} else {
			setErrorMsg("Payment process completed. Can not update");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}*/
	}

	public void clear() throws ParseException {
		mainClear();

	}

	private boolean validateForm() {
		boolean ret = true;
		Date today = new Date();

		if (registrationDto.getNic() == null || registrationDto.getNic().trim().isEmpty()) {
			setErrorMsg("NIC required.");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			return false;
		} else {
			String str = registrationDto.getNic().trim();
			registrationDto.setNic(str);
		}
		/***NIC Validation removed - requested by pramitha***/	
		/*if(!nicValidator.validateNIC(registrationDto.getNic(),strSelectedGender,registrationDto.getDob())) {
			setErrorMsg("Invalid NIC format.");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			return false;
		}*/

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

		if (registrationDto.getNewGender() == null || registrationDto.getNewGender().isEmpty()
				|| strSelectedGender == null) {
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

		if ((issuedDate != null) && (expireDate != null) && (expireDate.before(issuedDate))) {
			setErrorMsg("Expire date should be grater than Issue date.");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			return false;
		}

		if ((registrationDto.getDateOfIssue() != null) && (registrationDto.getExpiryDate() != null)
				&& (registrationDto.getExpiryDate().before(registrationDto.getDateOfIssue()))) {
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
				&& (strSelectedType.equals("ND") || strSelectedType.equals("RD") || strSelectedType.equals("RRD")
						|| strSelectedType.equals("RRRD") || strSelectedType.equals("DD")
						|| strSelectedType.equals("FD"))) {
			setErrorMsg("Medical Certificate Number  required.");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			return false;
		}
		if (registrationDto.getMrissuedate() == null && (strSelectedType.equals("ND") || strSelectedType.equals("RD")
				|| strSelectedType.equals("RRD") || strSelectedType.equals("RRRD") || strSelectedType.equals("DD")
				|| strSelectedType.equals("FD"))) {
			setErrorMsg("Please select Issued Date for Medical Report .");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			return false;
		}
		if (registrationDto.getMrexpiredate() == null && (strSelectedType.equals("ND") || strSelectedType.equals("RD")
				|| strSelectedType.equals("RRD") || strSelectedType.equals("RRRD") || strSelectedType.equals("DD")
				|| strSelectedType.equals("FD"))) {
			setErrorMsg("Please select Expire Date for Medical Report .");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			return false;
		}
		if ((registrationDto.getMrissuedate() != null) && (registrationDto.getMrexpiredate() != null)
				&& (registrationDto.getMrexpiredate().before(registrationDto.getMrissuedate()))
				&& (strSelectedType.equals("ND") || strSelectedType.equals("RD") || strSelectedType.equals("RRD")
						|| strSelectedType.equals("RRRD") || strSelectedType.equals("DD")
						|| strSelectedType.equals("FD"))) {
			setErrorMsg("Medical Report Expire Date should be grater than Medical Report Issue Date.");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			return false;
		}

		if ((registrationDto.getDateOfIssueLicence() != null) && (registrationDto.getDateOfExpiryLicence() != null)
				&& (registrationDto.getDateOfExpiryLicence().before(registrationDto.getDateOfIssueLicence()))) {
			setErrorMsg("Driving License Expire Date should be grater than Driving License Issue Date.");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			return false;
		}
		/** end change **/
		return ret;
	}

	public void mainSearchForView() throws ParseException {

		if (getStrSelectedType() != "") {
			if (strSelectedNic != "" || strSelectedRec != "") {
				if (strSelectedID != "") {

					if (strSelectedType.equals("NC") || strSelectedType.equals("RC") || strSelectedType.equals("RRC")
							|| strSelectedType.equals("RRRC") || strSelectedType.equals("DC")
							|| strSelectedType.equals("FC")) {
						conductor = true;
					} else {
						conductor = false;
					}
					hidePanel = true;

					registrationDto = driverConductorTrainingService.getDetailsByDCIdandTypeForView(strSelectedID,
							strSelectedType, strSelectedRec);
					strSelectedGender = registrationDto.getNewGender();

					if (registrationDto.getAppNo() != null && !registrationDto.getAppNo().isEmpty()) {
						String strFirstIssueDate = registrationDto.getFirstDateOfIssueN();
						if (strFirstIssueDate != null) {
							if (!strFirstIssueDate.isEmpty()) {
								firstIssuedDate = new SimpleDateFormat("dd/MM/yyyy").parse(strFirstIssueDate);
								registrationDto.setFirstDateOfIssue(firstIssuedDate);
							}
						}

						String strIssueDate = registrationDto.getDateOfIssueN();
						if (strIssueDate != null) {
							if (!strIssueDate.isEmpty()) {
								issuedDate = new SimpleDateFormat("dd/MM/yyyy").parse(strIssueDate);
								registrationDto.setDateOfIssue(issuedDate);
							}
						}
						String strExpireDate = registrationDto.getExpiryDateN();
						if (strExpireDate != null) {
							if (!strExpireDate.isEmpty()) {
								expireDate = new SimpleDateFormat("dd/MM/yyyy").parse(strExpireDate);
								registrationDto.setExpiryDate(expireDate);
							}
						}

						String strLiceneIssueDate = registrationDto.getDateOfIssueLicenceN();
						if (strLiceneIssueDate != null) {
							if (!strLiceneIssueDate.isEmpty()) {
								liceneIssuedDate = new SimpleDateFormat("dd/MM/yyyy").parse(strLiceneIssueDate);
								registrationDto.setDateOfIssueLicence(liceneIssuedDate);
							}
						}

						String strLiceneExpireDate = registrationDto.getDateOfExpiryLicenceN();
						if (strLiceneExpireDate != null) {
							if (!strLiceneExpireDate.isEmpty()) {
								liceneExpireDate = new SimpleDateFormat("dd/MM/yyyy").parse(strLiceneExpireDate);
								registrationDto.setDateOfExpiryLicence(liceneExpireDate);
							}
						}

					} else {

						setErrorMsg("No data for selected criteria");
						RequestContext.getCurrentInstance().update("requiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
						registrationDto = new DriverConductorRegistrationDTO();
					}
				}

				else {
					hidePanel = false;
					setErrorMsg("Driver/Conductor ID  required.");
					RequestContext.getCurrentInstance().update("requiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				}
			} else {
				hidePanel = false;
				setErrorMsg("ID Number or Receipt No. required.");
				RequestContext.getCurrentInstance().update("requiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		} else {
			hidePanel = false;
			setErrorMsg("Type of Training required.");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	/** added by tharushi.e for document management **/
	public void goToDocumentsManagementBtn() {

		try {
			String trainingType = null;
			trainingType = driverConductorTrainingService.getTrainingTypeDes(strSelectedType);

			sessionBackingBean.setTransactionType(trainingType);
			sessionBackingBean.setDriverConductorId(registrationDto.getDriverConductorId());

			String appNo = driverConductorTrainingService.getAppNoByDriverID(registrationDto.getDriverConductorId());
			sessionBackingBean.setDcAppNo(appNo);
			String strTransactionType = strSelectedType;

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

	/** document managemnt finished **/

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public DriverConductorRegistrationDTO getRegistrationDto() {
		return registrationDto;
	}

	public void setRegistrationDto(DriverConductorRegistrationDTO registrationDto) {
		this.registrationDto = registrationDto;
	}

	public DriverConductorRegistrationDTO getNewRregistrationDto() {
		return newRregistrationDto;
	}

	public void setNewRregistrationDto(DriverConductorRegistrationDTO newRregistrationDto) {
		this.newRregistrationDto = newRregistrationDto;
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

	public List<CommonDTO> getVehicleDetList() {
		return vehicleDetList;
	}

	public void setVehicleDetList(List<CommonDTO> vehicleDetList) {
		this.vehicleDetList = vehicleDetList;
	}

	public PermitDTO getSelectedPermit() {
		return selectedPermit;
	}

	public void setSelectedPermit(PermitDTO selectedPermit) {
		this.selectedPermit = selectedPermit;
	}

	public String getSucessMsg() {
		return sucessMsg;
	}

	public void setSucessMsg(String sucessMsg) {
		this.sucessMsg = sucessMsg;
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

	public boolean isDisableMode() {
		return disableMode;
	}

	public void setDisableMode(boolean disableMode) {
		this.disableMode = disableMode;
	}

	public Date getIssuedDate() {
		return issuedDate;
	}

	public void setIssuedDate(Date issuedDate) {
		this.issuedDate = issuedDate;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public DriverConductorTrainingService getDriverConductorTrainingService() {
		return driverConductorTrainingService;
	}

	public void setDriverConductorTrainingService(DriverConductorTrainingService driverConductorTrainingService) {
		this.driverConductorTrainingService = driverConductorTrainingService;
	}

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	public List<CommonDTO> getIdNoList() {
		return idNoList;
	}

	public void setIdNoList(List<CommonDTO> idNoList) {
		this.idNoList = idNoList;
	}

	public List<CommonDTO> getDriverConList() {
		return driverConList;
	}

	public void setDriverConList(List<CommonDTO> driverConList) {
		this.driverConList = driverConList;
	}

	public List<CommonDTO> getDriverConListForView() {
		return driverConListForView;
	}

	public void setDriverConListForView(List<CommonDTO> driverConListForView) {
		this.driverConListForView = driverConListForView;
	}

	public boolean isHidePanel() {
		return hidePanel;
	}

	public void setHidePanel(boolean hidePanel) {
		this.hidePanel = hidePanel;
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

	public String getStrSelectedID() {
		return strSelectedID;
	}

	public void setStrSelectedID(String strSelectedID) {
		this.strSelectedID = strSelectedID;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public String getStrSelectedGender() {
		return strSelectedGender;
	}

	public void setStrSelectedGender(String strSelectedGender) {
		this.strSelectedGender = strSelectedGender;
	}

	public boolean isDisableNic() {
		return disableNic;
	}

	public void setDisableNic(boolean disableNic) {
		this.disableNic = disableNic;
	}

	public boolean isDisableDCId() {
		return disableDCId;
	}

	public void setDisableDCId(boolean disableDCId) {
		this.disableDCId = disableDCId;
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

	public boolean isShowPhotoPanel() {
		return showPhotoPanel;
	}

	public void setShowPhotoPanel(boolean showPhotoPanel) {
		this.showPhotoPanel = showPhotoPanel;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public String getAppNo() {
		return appNo;
	}

	public void setAppNo(String appNo) {
		this.appNo = appNo;
	}

	public DocumentManagementService getDocumentManagementService() {
		return documentManagementService;
	}

	public void setDocumentManagementService(DocumentManagementService documentManagementService) {
		this.documentManagementService = documentManagementService;
	}

	public boolean isPhotoHave() {
		return photoHave;
	}

	public void setPhotoHave(boolean photoHave) {
		this.photoHave = photoHave;
	}

	public String getStrSelectedMedium() {
		return strSelectedMedium;
	}

	public void setStrSelectedMedium(String strSelectedMedium) {
		this.strSelectedMedium = strSelectedMedium;
	}

	public UploadDriverConductorPhotoService getUploadDriverConducPhotoService() {
		return uploadDriverConducPhotoService;
	}

	public void setUploadDriverConducPhotoService(UploadDriverConductorPhotoService uploadDriverConducPhotoService) {
		this.uploadDriverConducPhotoService = uploadDriverConducPhotoService;
	}

	public List<DriverConductorRegistrationDTO> getLanguageList() {
		return languageList;
	}

	public void setLanguageList(List<DriverConductorRegistrationDTO> languageList) {
		this.languageList = languageList;
	}

	public String getStrSelectedRec() {
		return strSelectedRec;
	}

	public void setStrSelectedRec(String strSelectedRec) {
		this.strSelectedRec = strSelectedRec;
	}

	public Date getFirstIssuedDate() {
		return firstIssuedDate;
	}

	public void setFirstIssuedDate(Date firstIssuedDate) {
		this.firstIssuedDate = firstIssuedDate;
	}

	public Date getLiceneExpireDate() {
		return liceneExpireDate;
	}

	public void setLiceneExpireDate(Date liceneExpireDate) {
		this.liceneExpireDate = liceneExpireDate;
	}

	public Date getLiceneIssuedDate() {
		return liceneIssuedDate;
	}

	public void setLiceneIssuedDate(Date liceneIssuedDate) {
		this.liceneIssuedDate = liceneIssuedDate;
	}

	public List<DriverConductorRegistrationDTO> getReceiptList() {
		return receiptList;
	}

	public void setReceiptList(List<DriverConductorRegistrationDTO> receiptList) {
		this.receiptList = receiptList;
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

	public List<CommonDTO> getIdNoListForView() {
		return idNoListForView;
	}

	public void setIdNoListForView(List<CommonDTO> idNoListForView) {
		this.idNoListForView = idNoListForView;
	}

	public boolean isConductor() {
		return conductor;
	}

	public void setConductor(boolean conductor) {
		this.conductor = conductor;
	}

	public NICValidator getNicValidator() {
		return nicValidator;
	}

	public void setNicValidator(NICValidator nicValidator) {
		this.nicValidator = nicValidator;
	}

	public boolean isDisableUpdate() {
		return disableUpdate;
	}

	public void setDisableUpdate(boolean disableUpdate) {
		this.disableUpdate = disableUpdate;
	}

	public boolean isDisableType() {
		return disableType;
	}

	public void setDisableType(boolean disableType) {
		this.disableType = disableType;
	}
	
	
}
