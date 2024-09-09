package lk.informatics.ntc.model.service;

import java.util.List;

import lk.informatics.ntc.model.dto.DriverConductorRegistrationDTO;

public interface UploadDriverConductorPhotoService {
	public List<DriverConductorRegistrationDTO> getApplicationNoList();

	public List<DriverConductorRegistrationDTO> getDriverIdList();

	public List<DriverConductorRegistrationDTO> getConductorIdList();

	public boolean checkAvailabilityOfPhoto(DriverConductorRegistrationDTO uploadPhotoDTO);

	public int insertDataIntoPhotoUploadTable(DriverConductorRegistrationDTO uploadPhotoDTO, String loginUser);

	public DriverConductorRegistrationDTO showimage(String appNo);

	public void deleteSelectedPhoto(DriverConductorRegistrationDTO uploadPhotoDTO, String loginUser);

	public DriverConductorRegistrationDTO getDataForFilters(DriverConductorRegistrationDTO uploadPhotoDTO);

	public List<DriverConductorRegistrationDTO> getDriverIdListFromId(DriverConductorRegistrationDTO uploadPhotoDTO);

	public List<DriverConductorRegistrationDTO> getConductorIdListFromId(DriverConductorRegistrationDTO uploadPhotoDTO);

	public List<DriverConductorRegistrationDTO> getApplicationNoListFromId(
			DriverConductorRegistrationDTO uploadPhotoDTO);

	public List<DriverConductorRegistrationDTO> getAppNoByDriverOrConduc(String s);

	public boolean checkAvailabilityOfPhotoByNIC(String id);

	public String getAppNoByNIC(String id);
	public DriverConductorRegistrationDTO showimageByNIC(String nicNo);

	void beanLinkMethod(DriverConductorRegistrationDTO registrationDto, String user, String des, String funDes);
}
