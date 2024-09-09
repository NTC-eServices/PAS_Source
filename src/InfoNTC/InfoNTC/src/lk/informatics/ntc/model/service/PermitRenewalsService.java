package lk.informatics.ntc.model.service;

import java.io.Serializable;
import java.util.List;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.OminiBusDTO;
import lk.informatics.ntc.model.dto.PermitRenewalsDTO;

public interface PermitRenewalsService extends Serializable {
	// renewal services
	List<PermitRenewalsDTO> getAllPermitNoList();

	PermitRenewalsDTO getSearchedDetailsWithPermitNoOrQueueNo(String selectedPermitNo, String queueNo,
			String selectedApplicationNo, String currentStatus);

	List<PermitRenewalsDTO> getAllProvincesList();

	List<PermitRenewalsDTO> getAllDistrictsForCurrentProvince(String selectedProvinceCode);

	List<PermitRenewalsDTO> getAllMaterialStatusList();

	List<PermitRenewalsDTO> getAllDivisionSections(String selectedDistricCode);

	String getLangaugeDescription(String preferedLanguageCode);

	String getGenderDescription(String genderCode);

	String getTitleDescription(String titleCode);

//	int updatePermitRenewalRecord(PermitRenewalsDTO permitRenewalsDTO);

	PermitRenewalsDTO getCurrentSeqNoForPermitNo(String selectedPermitNo, String queueNo, String applicationNo);

	PermitRenewalsDTO getCurrentOwnerSeqNo(String selectedPermitNo, String queueNo, String applicationNo);

	int updatePermitRenewalOwnerRecord(PermitRenewalsDTO permitRenewalsDTO);

	List<PermitRenewalsDTO> getAllRecordsForDocumentsCheckings();

	boolean checkIsSumbiited(String currentDocCode, String applicationNo, String permitNo);

	PermitRenewalsDTO getRemarkDetails(String currentDocCode, String applicationNo, String permitNo);

	int updateDocumentRemark(Long currentRecordSeqNo, String currentUploadFilePath, String modifyBy,
			String currentRemark);

	List<PermitRenewalsDTO> getAllPaymentHistoryList(String applicationNo, String permitNo);

	List<PermitRenewalsDTO> getAllChargeTypeRecords(String selectedVoucherNo);

	PermitRenewalsDTO getRecordDetailsForCurrentQueueNo(String callingQueueNo, String currentAppNoForCallingQueueNo);

	PermitRenewalsDTO getAllDetailsForSearchedPermitNo(String selectedPermitNo);

	void counterStatus(String counterId, String loginUser);

	List<CommonDTO> counterdropdown();

	List<PermitRenewalsDTO> getAllApplicationNoList();

	PermitRenewalsDTO getAllDetailsForSearchedApplicationNo(String selectedApplicationNo);

	List<PermitRenewalsDTO> getAllOtherOptionalDocumentsList(String applicationNo, String permitNo);

	boolean checkTaskCodeForCurrentAppNo(String selectedApplicationNo, String regNoOfBus, String taskCode,
			String taskStatus);

	public boolean insertTaskDetails(String selectedApplicationNo, String regNoOfBus, String loginUser, String taskCode,
			String string2);

	boolean checkTaskDetails(String selectedApplicationNo, String regNoOfBus, String taskCode, String taskStatus);

	public boolean CopyTaskDetailsANDinsertTaskHistory(String selectedApplicationNo, String regNoOfBus,
			String loginUser, String taskCode);

	public boolean deleteTaskDetails(String selectedApplicationNo, String regNoOfBus, String taskCode);

	public boolean updateTaskDetails(String selectedApplicationNo, String regNoOfBus, String loginUser, String taskCode,
			String taskStatus);

	PermitRenewalsDTO getApplicationNoForSelectetedQueueNo(String callingQueueNo);

	PermitRenewalsDTO getRecordDetailsForCurrentAppNo(String currentAppNoForCallingQueueNo);

	boolean isCheckedRecordInTaskHistory(String selectedApplicationNo, String string);

	OminiBusDTO getVehiDetailsYr(String selectedApplicationNo, String regNoOfBus);

//	int updateNewPermitOminiBus(OminiBusDTO ominiBusDTO);

//	int saveNewPermitOminiBus(OminiBusDTO ominiBusDTO);

	boolean isMandatory(String currentDocCode, String applicationNo, String permitNo);

	boolean isPhysicallyExit(String currentDocCode, String applicationNo, String permitNo);

	public boolean insertDocumentPhysicallyExitsStatus(boolean exits, String currentDocCode, String applicationNo,
			String permitNo, String logUser);

	public boolean deleteDocumentPhysicallyExitsStatus(boolean exits, String currentDocCode, String applicationNo,
			String permitNo, String logUser);

	List<PermitRenewalsDTO> getEditingApplicationNoList();

	PermitRenewalsDTO getAllDetailsForSelectedEditingAppNo(String selectedApplicationNo);

	String getPreSpecialRemark(String selectedPermitNo, String busRegNo);

	String getCurrentStatusForSelectedRd(String selectedApplicationNo, String selectedPermitNo, String queueNo,
			String regNoOfBus);

	List<PermitRenewalsDTO> getLatestInactiveAppNo(String selectedPermitNo, String regNoOfBus);

	PermitRenewalsDTO getSearchedRequestRenewalDetForCurrentQueueNoOrPermitNo(String selectedPermitNo, String queueNo,
			String selectedApplicationNo, String taskStatus);

	List<PermitRenewalsDTO> getAllVehicleNoList();

	List<PermitRenewalsDTO> getAllPermitNoListInApplicationTable();

	List<PermitRenewalsDTO> getStatusList(String selectedVehicleNo);

	List<PermitRenewalsDTO> getLatestInactiveAppNo(String selectedVehicleNo);

	PermitRenewalsDTO getActiveDetailsForSelectedVehicleNo(String needVehicleNo, String neededStatus,
			String selectedAppNo);

	String getCurrentDistrictDes(String provinceCode, String districtCode);

	String getCurrentDivSectionDes(String provinceCode, String districtCode, String divisionalSecretariatDivision);

	List<PermitRenewalsDTO> getStatusListForSelectedPermitNo(String selectedPermitNo);

	PermitRenewalsDTO getNewPermitDetails(String needVehicleNo, String permitNo);

	PermitRenewalsDTO getAttorneyDetailsForSelectedVehicleNo(String currentPermitNo, String regNoOfBus, String status,
			String applicationNo);

	String getCurrentMaterialStatusDes(String materialStatusId);

	List<PermitRenewalsDTO> getPermitHistoryListForSelectedVehicleNo(String selectedVehicleNo);

	List<PermitRenewalsDTO> getPermitHistoryListForSelectedPermitNo(String selectedPermitNo);

	List<PermitRenewalsDTO> getVoucherNoList(PermitRenewalsDTO selectPemrmitHisDTO);

	List<PermitRenewalsDTO> getSelectedPaymentTypeList(PermitRenewalsDTO selectPemrmitHisDTO);

	List<PermitRenewalsDTO> getSelectedPaymentTypeListWithOldPermitNo(PermitRenewalsDTO selectPemrmitHisDTO);

	boolean isCheckActiveAttorneyHolder(String permitNo);

	int updateTranstractionType(String transtractionTypeCode, String currentAppNo, String loginUser);

	List<PermitRenewalsDTO> getOldHistoryDataWithPermitNo(String selectedPermitNo);

	List<PermitRenewalsDTO> getOldHistoryDataWithBusNo(String selectedVehicleNo);

	public int savePermitRenewalRecordAllinOne(OminiBusDTO ominiBusDTO, PermitRenewalsDTO permitRenewalsDTO);

	public int updatePermitRenewalRecordAllinOne(OminiBusDTO ominiBusDTO, PermitRenewalsDTO permitRenewalsDTO);

}
