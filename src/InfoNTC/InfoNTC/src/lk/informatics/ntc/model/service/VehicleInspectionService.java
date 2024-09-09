package lk.informatics.ntc.model.service;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.OminiBusDTO;
import lk.informatics.ntc.model.dto.PermitRenewalsDTO;
import lk.informatics.ntc.model.dto.UploadImageDTO;
import lk.informatics.ntc.model.dto.VehicleInspectionDTO;

public interface VehicleInspectionService extends Serializable {

	public VehicleInspectionDTO getDatafromnext(String queryNo, boolean inspectionForAmendment);

	public boolean saveDataApplication(VehicleInspectionDTO VehicleDTO, String generatedapplicationNO,
			String loginUser);

	public boolean saveDataVehicleOwnerWithOutApplicationNo(VehicleInspectionDTO VehicleDTO,
			String generatedapplicationNO);

	public boolean saveDataVehicleOwnerWithApplicatioNo(VehicleInspectionDTO VehicleDTO, String generatedapplicationNO);

	public boolean saveDataVehicleInspecDetails(VehicleInspectionDTO VehicleDTO, List<VehicleInspectionDTO> dataList,
			String generatedapplicationNO);

	public List<VehicleInspectionDTO> Gridview(VehicleInspectionDTO vehicleDTO);

	public VehicleInspectionDTO search(String queueNo, boolean inspectionForAmendment);

	public boolean checkQueueNo(String queueNo);

	public List<VehicleInspectionDTO> routeNodropdown();

	public List<VehicleInspectionDTO> makedropdown();

	public List<VehicleInspectionDTO> modeldropdown(String code);

	public List<VehicleInspectionDTO> servicetypedropdown();

	public List<CommonDTO> counterdropdown();

	public void counterStatus(String counterId, String user);

	public String generateApplicationNo();

	public String getVehicleNo(String queueNO);

	public VehicleInspectionDTO checkVehicleNo(String queueNo);

	public List<VehicleInspectionDTO> getActiveActionPointData();

	public void updateCounter(String username);

	public boolean routeDetails(VehicleInspectionDTO vehicleDTO, String routeno);

	public void insertVehicleUploadPhotosToNT_T_OWNER_VEHI_IMAGES(UploadImageDTO uploadImagesDTO, String loginUser,
			String status);

	public UploadImageDTO findUploadImageDetailsByVehicleNo(String vehicleNo, String applicationNo, String loginUser);

	public boolean updateTaskDetails(VehicleInspectionDTO dto, String taskCode, String status);

	public boolean checkTaskDetails(VehicleInspectionDTO dto, String taskCode, String status);

	public boolean insertTaskDetails(VehicleInspectionDTO dto, String loginUSer, String taskCode, String status);

	public boolean deleteTaskDetails(VehicleInspectionDTO dto, String taskCode);

	public boolean applicationType(String queueNo);

	public boolean CopyTaskDetailsANDinsertTaskHistory(VehicleInspectionDTO dto, String loginUSer, String taskCode);

	public String checkStatusFromApplicationNumber(String applicationNo);

	public VehicleInspectionDTO searchOnBackBtnAction(String queueNo);

	public VehicleInspectionDTO getDataForReinspection(String queueNo);

	public String applicationTaskCodeStatusPM100(String applicationNo);

	String applicationTaskCodeStatusPM101(String applicationNo);

	public void permanentSkip(String queueNo);

	public UploadImageDTO findVehicleOwnerByFormerApplicationNo(String vehicleNo);

	public VehicleInspectionDTO getApplicationForAmendment(String generatedApplicationNo);

	public void saveInspectionForAmendment(VehicleInspectionDTO vehicleDTO);

	public void updateOminiBusForInspection(OminiBusDTO ominiBusDTO);

	UploadImageDTO retrieveVehicleImageDataForVehicleNo(String applicationNo);

	public boolean saveDataVehicleInspecDetailsHistory(VehicleInspectionDTO vehicleDTO,
			List<VehicleInspectionDTO> dataList, String generatedApplicationNO);

	public void saveDataVehicleImagesWithApplicatioNo(VehicleInspectionDTO vehicleDTO, String generatedApplicationNO,
			String loginUser);

	public VehicleInspectionDTO getCheckAmmendments(String queueNo);

	public VehicleInspectionDTO getTaskDet(String applicationNo, String vehicleNo);

	public int updateTranstractionTypeForAmmendments(String transtractionTypeCode, String currentAppNo,
			String loginUser);

	public boolean updateDataApplicationTable(VehicleInspectionDTO VehicleDTO, String neededApplicationNo,
			String loginUser);

	public boolean updateDataVehicleOwnerWithApplicatioNo(VehicleInspectionDTO vehicleDTO, String neededApplicationNo);

	public boolean checkTaskDetDataExist(String vehicleNo, String applicationNo);

	String applicationTaskCodeStatusPM400(String applicationNo);

	public VehicleInspectionDTO getTaskDetForToday(String applicationNo, String vehicleNo);

	public boolean saveAllDataWithApp(VehicleInspectionDTO VehicleDTO, String generatedapplicationNO, String loginUser,
			OminiBusDTO ominiBusDTO, List<VehicleInspectionDTO> dataList, String taskoCode, String task);

	public boolean saveAllDataWithoutAppNo(VehicleInspectionDTO VehicleDTO, String generatedapplicationNO,
			String loginUser, OminiBusDTO ominiBusDTO, List<VehicleInspectionDTO> dataList, String taskoCode,
			String task);

	public boolean updateStatusOfQueApp(Connection con, String queueNo, String appNo);

	public boolean updateTransactionTypeCodeForQueueNo(Connection con, String queueNumber, String currentQueueType);

	public boolean updateQueueNumberTaskInQueueMaster(Connection conn, String queueNo, String appNo, String taskCode,
			String taskStatus);

	public boolean updatePhotoUploadStatus(Connection con, String generatedApplicationNo, String photoUpload);

	public String getTokenType(String queueNo);

	public VehicleInspectionDTO searchForOtherInspection(String queueNo);

	public List<VehicleInspectionDTO> getInspectionLocationList();

	/* Other inspection CR start here */

	public VehicleInspectionDTO getVehicleInformation(String queueNo, String applicationNo, boolean isViewEditMode);

	public VehicleInspectionDTO getLatestApplicationNo(String permitNo);

	public String getPermitNo(String queueNo);

	public boolean saveOtherVehicleInspection(VehicleInspectionDTO VehicleDTO, String generatedapplicationNO,
			String loginUser, OminiBusDTO ominiBusDTO, List<VehicleInspectionDTO> dataList);

	public void updateQueueMasterStatus(String queueNo, VehicleInspectionDTO dto, String status, String taskCode,
			String taskStatus, String loginUser, boolean isApplicationUpdate);

	public String generateOtherInspectionApplicationNo();

	public List<VehicleInspectionDTO> getApplicationNoList();

	public List<VehicleInspectionDTO> getVehicleNoList();

	public List<VehicleInspectionDTO> getOtherInspectionRecords(VehicleInspectionDTO dto, boolean isLoadDefaultData);

	public String getApplicationNumberOrPermitNumber(String number, boolean isApplicationNoChange);

	/* Other inspection CR end here */
	/** added for normal inspection ***/
	public VehicleInspectionDTO searchForNormalInspection(String queueNo, boolean inspectionForAmendment,
			String activeApplicationNUmber);

	public String getLatestApplicationNumber(String vehicleNumber);

	public List<VehicleInspectionDTO> GridviewNew(String latestApplicationNumber);

	public boolean saveAllDataWithoutAppNoNew(VehicleInspectionDTO VehicleDTO, String generatedapplicationNO,
			String loginUser, OminiBusDTO ominiBusDTO, List<VehicleInspectionDTO> dataList, String inspectionStatus,
			String inspectionType);

	public boolean saveAllDataWithAppNew(VehicleInspectionDTO VehicleDTO, String generatedapplicationNO,
			String loginUser, OminiBusDTO ominiBusDTO, List<VehicleInspectionDTO> dataList, String inspectionStatus,
			String inspectionType);

	public boolean insertIncomplteTaskHistroy(VehicleInspectionDTO dto, String loginUSer,
			String taskCode, String status);

	public VehicleInspectionDTO getDataForNormalReinspection(String queueNo);

	public boolean insertTaskDetailsNew(Connection conn, VehicleInspectionDTO dto, String loginUSer, String taskCode,
			String status);

	public boolean updateAmendmentInspectionDeatils(OminiBusDTO ominiBusDTO, VehicleInspectionDTO VehicleDTO,
			List<VehicleInspectionDTO> dataList, String getApplication, String generatedApplicationNO,
			String getTranstractionTypeCode, String currentAppNo, String loginUser,
			PermitRenewalsDTO applicationHistoryDTO, String inspectionStatus, String inspectionType);

	/** end ***/

	/** Other inspection start **/
	public boolean updateOtherInspection(VehicleInspectionDTO inspectionDTO, String loginUser,
			List<VehicleInspectionDTO> dataList);

	public VehicleInspectionDTO getQueueMasterTableData(VehicleInspectionDTO dto, String queueNo);

	public void updateQueueMasterStatusUsingAppNo(String queueNo, VehicleInspectionDTO dto, String status,
			String taskCode, String taskStatus, String loginUser);

	/** Other inspection end **/
	public boolean taskCodeStatusINQueuePM100(String tokenNumber);

	public boolean validTokenInQueue(String tokenNumber);

	public VehicleInspectionDTO getTaskDetForTodayNew(String vehicleNo);

	public String getActiveApplicationNumber(String vehicleNumber);

	public String getAmendmentType(String applicationNo);

	public String getLatestApplicationNumberByPermitNo(String permitNo);

	public String getActiveApplicationNumberByPermitNo(String permitNo);

	public List<String> getLatestApplicationNumberList(String vehicleNumber);

	public List<String> getLatestApplicationNumberListByPermitNo(String permitNo);

	public boolean updateActionPoints(List<VehicleInspectionDTO> dataList, VehicleInspectionDTO inspectionDTO,
			String loginUser);

	public VehicleInspectionDTO searchOnBackBtnActionNew(String queueNo);

	public String getLatestInspectionStatus(String latestAppNo);

	public boolean checkStausForAppNoList(String busNo);

	public boolean checkInspectionDataForVehiNo(String busNo);

	public String getLatestApplicationNumberForAmendmend(String vehicleNumber);

	public VehicleInspectionDTO getTaskDetForTodayNewForAmendmed(String vehicleNo, String appNo);

	public boolean isNewPermit(String queueNo);

	public boolean isOngoingRenewalsExists(String activeApplication);

	public String generateNormalInspectionApplicationNo();

}
