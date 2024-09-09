package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import lk.informatics.ntc.model.dto.DriverConductorRegistrationDTO;
import lk.informatics.ntc.model.service.UploadDriverConductorPhotoService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "uploadDriverConductorPhotoBackingBean")
@ViewScoped
public class UploadDriverConductorPhotoBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	// services
	private UploadDriverConductorPhotoService uploadDriverConducPhotoService;
	// DTO
	DriverConductorRegistrationDTO uploadDriConducPhotoDTO = new DriverConductorRegistrationDTO();
	DriverConductorRegistrationDTO photoDto = new DriverConductorRegistrationDTO();
	DriverConductorRegistrationDTO showFilterdDataDTO = new DriverConductorRegistrationDTO();

	// List

	private List<DriverConductorRegistrationDTO> appNoNicNoListDropDown = new ArrayList<DriverConductorRegistrationDTO>(
			0);
	private List<DriverConductorRegistrationDTO> driverIdList = new ArrayList<DriverConductorRegistrationDTO>();
	private List<DriverConductorRegistrationDTO> conductorIdList = new ArrayList<DriverConductorRegistrationDTO>(0);

	private StreamedContent files;
	private String sucessMsg;
	private String errorMsg;
	private boolean showUploadIcon, isPhotoHave, disableConducId, disableDriverId, saveButtonShow;
	private boolean disableApp;
	private String forVal;
	private String forVal2;

	@PostConstruct
	public void init() {
		uploadDriverConducPhotoService = (UploadDriverConductorPhotoService) SpringApplicationContex
				.getBean("uploadDriverConducPhotoService");
		uploadDriConducPhotoDTO = new DriverConductorRegistrationDTO();

		showUploadIcon = false;
		disableConducId = false;
		disableDriverId = false;
		saveButtonShow = false;
		disableApp = true;

	}

	public void showPanelData() {

		if (forVal.equalsIgnoreCase("Drivers")) {

			disableApp = false;
			appNoNicNoListDropDown = uploadDriverConducPhotoService.getAppNoByDriverOrConduc("d");
		} else if (forVal.equalsIgnoreCase("Conductors")) {

			disableApp = false;
			appNoNicNoListDropDown = uploadDriverConducPhotoService.getAppNoByDriverOrConduc("c");
		}
	}

	public void showFilterdData() {
		showUploadIcon = false;
		this.setImage(null);
		showFilterdDataDTO = uploadDriverConducPhotoService.getDataForFilters(uploadDriConducPhotoDTO);

		uploadDriConducPhotoDTO.setNic(showFilterdDataDTO.getNic());
		uploadDriConducPhotoDTO.setDriverId(showFilterdDataDTO.getDriverId());
		uploadDriConducPhotoDTO.setConductorId(showFilterdDataDTO.getConductorId());
		uploadDriConducPhotoDTO.setFullName(showFilterdDataDTO.getFullName());
		if (uploadDriConducPhotoDTO.getDriverId() == null) {
			disableDriverId = true;
			disableConducId = false;
		}

		if (uploadDriConducPhotoDTO.getConductorId() == null) {
			disableDriverId = false;
			disableConducId = true;
		}
		showFilterdDataFromId();
	}

	public void showFilterdDataFromId() {
		showUploadIcon = false;
		this.setImage(null);
		showFilterdDataDTO = uploadDriverConducPhotoService.getDataForFilters(uploadDriConducPhotoDTO);

		uploadDriConducPhotoDTO.setNic(showFilterdDataDTO.getNic());
		uploadDriConducPhotoDTO.setFullName(showFilterdDataDTO.getFullName());

		appNoNicNoListDropDown = uploadDriverConducPhotoService.getApplicationNoListFromId(uploadDriConducPhotoDTO);

	}

	public void searchAction() {
		if (!uploadDriConducPhotoDTO.getAppNo().trim().isEmpty() && !uploadDriConducPhotoDTO.getAppNo().equals(null)
				&& !uploadDriConducPhotoDTO.getNic().trim().isEmpty() && !uploadDriConducPhotoDTO.getNic().equals(null)
				&& !uploadDriConducPhotoDTO.getFullName().trim().isEmpty()
				&& !uploadDriConducPhotoDTO.getFullName().equals(null)) {

			// check availability of photos for selected app no
			isPhotoHave = uploadDriverConducPhotoService.checkAvailabilityOfPhoto(uploadDriConducPhotoDTO);

			if (isPhotoHave) {
				showUploadIcon = true;
				getImageContentsAsBase64();
				saveButtonShow = true;
			} else {

				errorMsg = "Please Upload a photo.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				showUploadIcon = true;
				saveButtonShow = false;
			}

		} else {

			errorMsg = "Please select mandotary fields.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void clearButAction() {
		uploadDriConducPhotoDTO = new DriverConductorRegistrationDTO();
		disableConducId = false;
		disableDriverId = false;
		showUploadIcon = false;
		disableApp = true;
		forVal = null;

	}

	public void clearButAction2() {
		uploadDriConducPhotoDTO = new DriverConductorRegistrationDTO();
		this.setImage(null);

		disableConducId = false;
		disableDriverId = false;
		disableApp = true;

	}

	public void uploadButnAction() {
		showUploadIcon = true;

	}

	private UploadedFile uploadedFile;
	private byte[] image = null;

	public void fileUploadListener(FileUploadEvent e) {
		// Get uploaded file from the FileUploadEvent
		this.uploadedFile = e.getFile();
		image = uploadedFile.getContents();
		// Print out the information of the file

		// Add message
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("File uploaded successfully."));
	}

	public String getImageContentsAsBase64() {

		// check availability of photos for selected app no
		isPhotoHave = uploadDriverConducPhotoService.checkAvailabilityOfPhoto(uploadDriConducPhotoDTO);
		if (isPhotoHave) {
			photoDto = uploadDriverConducPhotoService.showimage(uploadDriConducPhotoDTO.getAppNo());
			uploadDriConducPhotoDTO.setImage(photoDto.getImage());
			return Base64.getEncoder().encodeToString(uploadDriConducPhotoDTO.getImage());
		} else {
			if (image != null) {
				return Base64.getEncoder().encodeToString(image);
			}
			return null;
		}
	}

	public void saveAction() {
		String loginUser = sessionBackingBean.getLoginUser();
		if (!uploadDriConducPhotoDTO.getAppNo().trim().isEmpty() && !uploadDriConducPhotoDTO.getAppNo().equals(null)
				&& !uploadDriConducPhotoDTO.getNic().trim().isEmpty() && !uploadDriConducPhotoDTO.getNic().equals(null)
				&& !uploadDriConducPhotoDTO.getFullName().trim().isEmpty()
				&& !uploadDriConducPhotoDTO.getFullName().equals(null)) {
			if (uploadDriConducPhotoDTO.getDriverId() != null || uploadDriConducPhotoDTO.getConductorId() != null) {
				if (this.image != null) {
					uploadDriConducPhotoDTO.setImage(this.image);
					uploadDriverConducPhotoService.insertDataIntoPhotoUploadTable(uploadDriConducPhotoDTO, loginUser);
					
					uploadDriverConducPhotoService.beanLinkMethod(uploadDriConducPhotoDTO, loginUser, "Upload Driver/Conductor Photo", "Upload Driver/Conductor Photos");
					sucessMsg = "Successfully saved.";
					RequestContext.getCurrentInstance().update("frmsuccessSave");
					RequestContext.getCurrentInstance().execute("PF('successSave').show()");
				} else {
					errorMsg = "Please choose a photo.";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}

			} else {
				errorMsg = "Please select a driver or conductor ID.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		} else {
			errorMsg = "Please select mandotary fields.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public void deleteButAction() {

		if (this.image != null || uploadDriConducPhotoDTO.getImage() != null) {
			RequestContext.getCurrentInstance().execute("PF('dlgRejectConfirm').show()");
		}
	}

	public void deleteActionForYes() {
		String loginUser = sessionBackingBean.getLoginUser();
		uploadDriverConducPhotoService.deleteSelectedPhoto(uploadDriConducPhotoDTO, loginUser);
		uploadDriverConducPhotoService.beanLinkMethod(uploadDriConducPhotoDTO, loginUser, "Delete Driver/Conductor Photo", "Upload Driver/Conductor Photos");
		this.setImage(null);
		saveButtonShow = false;
	}

	public void deleteActionForNo() {

	}

	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public UploadDriverConductorPhotoService getUploadDriverConducPhotoService() {
		return uploadDriverConducPhotoService;
	}

	public void setUploadDriverConducPhotoService(UploadDriverConductorPhotoService uploadDriverConducPhotoService) {
		this.uploadDriverConducPhotoService = uploadDriverConducPhotoService;
	}

	public DriverConductorRegistrationDTO getUploadDriConducPhotoDTO() {
		return uploadDriConducPhotoDTO;
	}

	public void setUploadDriConducPhotoDTO(DriverConductorRegistrationDTO uploadDriConducPhotoDTO) {
		this.uploadDriConducPhotoDTO = uploadDriConducPhotoDTO;
	}

	public List<DriverConductorRegistrationDTO> getAppNoNicNoListDropDown() {
		return appNoNicNoListDropDown;
	}

	public void setAppNoNicNoListDropDown(List<DriverConductorRegistrationDTO> appNoNicNoListDropDown) {
		this.appNoNicNoListDropDown = appNoNicNoListDropDown;
	}

	public List<DriverConductorRegistrationDTO> getDriverIdList() {
		return driverIdList;
	}

	public void setDriverIdList(List<DriverConductorRegistrationDTO> driverIdList) {
		this.driverIdList = driverIdList;
	}

	public List<DriverConductorRegistrationDTO> getConductorIdList() {
		return conductorIdList;
	}

	public void setConductorIdList(List<DriverConductorRegistrationDTO> conductorIdList) {
		this.conductorIdList = conductorIdList;
	}

	public boolean isShowUploadIcon() {
		return showUploadIcon;
	}

	public void setShowUploadIcon(boolean showUploadIcon) {
		this.showUploadIcon = showUploadIcon;
	}

	public boolean isPhotoHave() {
		return isPhotoHave;
	}

	public void setPhotoHave(boolean isPhotoHave) {
		this.isPhotoHave = isPhotoHave;
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

	public DriverConductorRegistrationDTO getPhotoDto() {
		return photoDto;
	}

	public void setPhotoDto(DriverConductorRegistrationDTO photoDto) {
		this.photoDto = photoDto;
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

	public DriverConductorRegistrationDTO getShowFilterdDataDTO() {
		return showFilterdDataDTO;
	}

	public void setShowFilterdDataDTO(DriverConductorRegistrationDTO showFilterdDataDTO) {
		this.showFilterdDataDTO = showFilterdDataDTO;
	}

	public boolean isDisableConducId() {
		return disableConducId;
	}

	public void setDisableConducId(boolean disableConducId) {
		this.disableConducId = disableConducId;
	}

	public boolean isDisableDriverId() {
		return disableDriverId;
	}

	public void setDisableDriverId(boolean disableDriverId) {
		this.disableDriverId = disableDriverId;
	}

	public boolean isSaveButtonShow() {
		return saveButtonShow;
	}

	public void setSaveButtonShow(boolean saveButtonShow) {
		this.saveButtonShow = saveButtonShow;
	}

	public boolean isDisableApp() {
		return disableApp;
	}

	public void setDisableApp(boolean disableApp) {
		this.disableApp = disableApp;
	}

	public String getForVal() {
		return forVal;
	}

	public void setForVal(String forVal) {
		this.forVal = forVal;
	}

	public String getForVal2() {
		return forVal2;
	}

	public void setForVal2(String forVal2) {
		this.forVal2 = forVal2;
	}

}
