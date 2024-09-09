package lk.informatics.ntc.model.service;

import java.io.Serializable;
import java.util.List;

import lk.informatics.ntc.model.dto.ActionPointsManagementDTO;
import lk.informatics.ntc.model.dto.PermitDTO;
import lk.informatics.ntc.model.dto.PrintInspectionDTO;
import lk.informatics.ntc.model.dto.VehicleInspectionDTO;

public interface InspectionActionPointService extends Serializable {

	List<ActionPointsManagementDTO> getSectionList();

	ActionPointsManagementDTO getDescriptionForSectionCode(String selectedSection);

	List<ActionPointsManagementDTO> getStatusList();

	ActionPointsManagementDTO getDescriptionForStatusCode(String selectedStatus);

	int insertNewSeqNo(ActionPointsManagementDTO actionPointsManagementDTO);

	int updateSeqRecord(ActionPointsManagementDTO actionPointsManagementDTO);

	ActionPointsManagementDTO getGeneratedSeqNo(String sequence);

	List<ActionPointsManagementDTO> getAllSequenceCodeList();

	boolean checkAsigned(String sequence, Long seq);

	int deleteSequenceRecord(Long seq);

	List<ActionPointsManagementDTO> viewAllRecords();

	List<ActionPointsManagementDTO> getSearchedRecords(String serachedSeqCode, String searchedSection,
			String searchedStatus);

	List<ActionPointsManagementDTO> getSearchedRecordsOne(String serachedSeqCode, String searchedSection);

	List<ActionPointsManagementDTO> getSearchedRecordsTwo(String serachedSeqCode, String searchedStatus);

	List<ActionPointsManagementDTO> getSearchedRecordsThree(String searchedSection, String searchedStatus);

	List<ActionPointsManagementDTO> getSearchedRecordsFour(String serachedSeqCode, String searchedSection,
			String searchedStatus);

	List<PrintInspectionDTO> getAllApplicationNoList();

	List<PrintInspectionDTO> getAllRecords(String currentApplicationNo, String currentVehicleNo);

	List<PrintInspectionDTO> getAllRecordsForBoth(String selectedApplicationNo, String selectedVehicleNo);

	PrintInspectionDTO getDetails(String selectedApplicationNo, String selectedVehicleNo);

	List<PrintInspectionDTO> getAllApplicationNoListForViewInspection();

	List<PrintInspectionDTO> getAllRecordsForViewInspections(String currentApplicationNo, String currentVehicleNo);

	PrintInspectionDTO getDetailsForViewInspectionDetails(String selectedApplicationNo, String selectedVehicleNo);

	List<PrintInspectionDTO> getAllRecordsForBothViewInspection(String selectedApplicationNo, String selectedVehicleNo);

	VehicleInspectionDTO getRecordForCurrentApplicationNo(String selectedApplicationNumber);

	List<VehicleInspectionDTO> getAllInspectionRecordsDetails(String selectedApplicationNumber);

	List<ActionPointsManagementDTO> getAllDesccritionsForCurrentSectionCode(String selectedSection);

	ActionPointsManagementDTO getCurrentSectionAndStatus(String serachedSeqCode);

	List<PrintInspectionDTO> getAllVehicleNoListForViewInspection(); // to be change

	List<PrintInspectionDTO> getAllRecordsForViewInspectionsDefault();

	PrintInspectionDTO getCurrentVehicleNo(String selectedApplicationNo);

	PrintInspectionDTO getCurrentApplicationNo(String selectedVehicleNo);

	List<PrintInspectionDTO> getAllVehicleNoList();

	PrintInspectionDTO getCurrentApplicationNoPrint(String selectedVehicleNo);

	PrintInspectionDTO getCurrentVehicleNoPrint(String selectedApplicationNo);

	boolean checkInspection(String applicationNo);

	List<VehicleInspectionDTO> getRouteNoList();

	List<VehicleInspectionDTO> getServiceTypesList();

	String getServiceTypeDes(String serviceType);

	int updateInspectionRecord(VehicleInspectionDTO vehicleDTO, PermitDTO permitDTO, String loginUser);

	int updatePermitOwnerInspectionRecord(VehicleInspectionDTO vehicleDTO, PermitDTO permitDTO, String loginUser);

	int updateOminiBusDetInspectionRecord(VehicleInspectionDTO vehicleDTO, PermitDTO permitDTO, String loginUser);

	boolean saveDataVehicleInspecDetails(VehicleInspectionDTO vehicleDTO, List<VehicleInspectionDTO> dataList,
			String applicationNo, String loginUser);

	VehicleInspectionDTO getAssignedFunction(String logedUser);

	VehicleInspectionDTO getDetailsWithModiFyDate(String applicationNo);

	int insertNewTaskDetWithAppDet(VehicleInspectionDTO taskDetWithAppDetDTO, String loginUser, String applicationNo,
			String taskCode, String taskStatus);

	boolean isCheckedSameTaskCodeForSelectedApp(String applicationNo, String taskCode, String taskStatus);

	int updateNewTaskDetWithAppDet(VehicleInspectionDTO taskDetWithAppDetDTO, String loginUser, String applicationNo,
			String taskCode, String taskStatus);

	boolean isCheckedTaskCodeInTaskDetCompleted(String applicationNo, String taskCode);

	int updateOminiBusDetInspectionRecordWithApplicationNo(VehicleInspectionDTO vehicleDTO, PermitDTO permitDTO,
			String loginUser);

	List<PrintInspectionDTO> getAllApplicationNoListForViewInspectionNormal();

	List<PrintInspectionDTO> getAllVehicleNoListForViewInspectionNew();

	List<PrintInspectionDTO> getAllRecordsForViewInspectionsNew(String currentApplicationNo, String currentVehicleNo);

	PrintInspectionDTO getDetailsForViewInspectionDetailsNew(String selectedApplicationNo, String selectedVehicleNo);

	List<PrintInspectionDTO> getAllRecordsForBothNew(String selectedApplicationNo, String selectedVehicleNo);

	List<PrintInspectionDTO> getAllRecordsForViewInspectionsDefaultNew();

	VehicleInspectionDTO getRecordForCurrentApplicationNoNew(String selectedApplicationNumber);

	int updateInspectionRecordNew(VehicleInspectionDTO vehicleDTO, PermitDTO permitDTO, String loginUser,
			String inspectionStatus, String inspectionType);

	int updateOminiBusDetAmendmentInspectionRecord(VehicleInspectionDTO vehicleDTO, PermitDTO permitDTO,
			String loginUser);

}
