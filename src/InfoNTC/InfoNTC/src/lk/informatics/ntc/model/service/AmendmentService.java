package lk.informatics.ntc.model.service;

import java.io.Serializable;
import java.util.List;

import lk.informatics.ntc.model.dto.AccidentDTO;
import lk.informatics.ntc.model.dto.AfterAccidentDTO;
import lk.informatics.ntc.model.dto.AmendmentBusOwnerDTO;
import lk.informatics.ntc.model.dto.AmendmentDTO;
import lk.informatics.ntc.model.dto.AmendmentOminiBusDTO;
import lk.informatics.ntc.model.dto.AmendmentServiceDTO;
import lk.informatics.ntc.model.dto.BusOwnerDTO;
import lk.informatics.ntc.model.dto.CommitteeOrBoardApprovalDTO;
import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.OminiBusDTO;
import lk.informatics.ntc.model.dto.OrganizationDTO;
import lk.informatics.ntc.model.dto.PermitRenewalsDTO;

public interface AmendmentService extends Serializable {

	BusOwnerDTO getOwnerDetailsByVehicleNo(String strVehicleNo);

	AmendmentBusOwnerDTO getApplicationDetailsByVehicleNo(String strVehicleNo);

	AmendmentBusOwnerDTO getApplicationDetailsByVehicleNoAndAppNo(String strVehicleNo, String appNo);

	AmendmentOminiBusDTO ominiBusByVehicleNo(String strVehicleNo);

	String generateApplicationNo();

	void saveOwnerDetails(BusOwnerDTO busOwnerDTO);

	void saveAmendmentRelationshipInformation(AmendmentDTO amendmentDTO);

	List<CommonDTO> getRelationships();

	String getTransactionType(String queueNo);

	List<PermitRenewalsDTO> getChecklistDocuments(String trnType);

	String getTransactionTypeDesc(String strTrnCode);

	boolean checkIsSubmitted(String currentDocCode, String applicationNo, String permitNo, String trnType);

	PermitRenewalsDTO getRemarkDetails(String currentDocCode, String applicationNo, String permitNo, String trnType);

	void saveNewOminiBusApplication(OminiBusDTO ominiBusDTO);

	int saveNewOminiBus(OminiBusDTO ominiBusDTO);

	void saveAmendmentNewOminiBus(AmendmentDTO amendmentDTO);

	List<OrganizationDTO> getOtherOrgList(String generatedApplicationNo);

	void saveOtherOrg(OrganizationDTO organizationDTO);

	List<CommonDTO> getAccidentTypesList();

	void saveAccident(AccidentDTO accidentDTO);

	List<AccidentDTO> getAccidentList(String vehicleNo);

	void saveAfterAccident(AfterAccidentDTO afterAccidentDTO);

	void saveMoreAccident(AccidentDTO accidentDTO);

	List<AccidentDTO> getMoreAccidentList(String vehicleRegNo);

	void removeOtherOrg(int selectedOtherOrg);

	void removeAccident(int selectedAccident);

	void removeMoreAccident(int selectedMoreAccident);

	public List<AmendmentDTO> getTransactionType();

	public List<AmendmentDTO> getApplicationNO(AmendmentDTO dto);

	public List<AmendmentDTO> getPermitNO(AmendmentDTO dto);

	public AmendmentDTO ajaxFillData(AmendmentDTO dto);

	public List<AmendmentDTO> getGrantApprovalDetails(AmendmentDTO dto);

	public List<AmendmentDTO> getGrantApprovalDefaultDetails(AmendmentDTO dto);

	public boolean isBusChange(AmendmentDTO dto);

	public boolean updateRejectData(AmendmentDTO dto, String rejectReason, String logedUser);

	public boolean checkTaskDetails(AmendmentDTO dto, String taskCode, String status);

	public boolean availabelForFirstApproval(AmendmentDTO dto);

	public boolean updateAmendmentsTableStatus(AmendmentDTO dto, String status, String logedUser);

	public boolean updateAmendmentsTableCommitteeStatus(AmendmentDTO dto, String status, String logedUser);

	public boolean updateAmendmentsTableBoardStatus(AmendmentDTO dto, String status, String logedUser);

	public boolean CheckAmendmentsTableStatus(AmendmentDTO dto, String status);

	public boolean CheckAmendmentsTableCommitteeStatus(AmendmentDTO dto);

	public boolean CheckAmendmentsTableBoarderStatus(AmendmentDTO dto);

	public boolean checkTaskHistory(AmendmentDTO dto, String taskCode, String status);

	public boolean insertTaskDetails(AmendmentDTO dto, String loginUSer, String taskCode, String status);

	void saveServiceChange(AmendmentServiceDTO amendmentServiceDTO, String logedUser);

	void saveAmendmentServiceChange(AmendmentDTO amendmentDTO);

	void saveDataApplication(BusOwnerDTO busOwnerDTO);

	AfterAccidentDTO getAfterAccident(String vehicleRegNo);

	public List<AmendmentDTO> getFilePaths(String oldPermit);

	void updateAfterAccident(AfterAccidentDTO afterAccidentDTO);

	void updateAmendmentDetails(AmendmentDTO amendmentDTO);

	AmendmentDTO getAmendmentDetails(String generatedApplicationNo);

	BusOwnerDTO getOwnerDetails(String strApplicationNo);

	int updateBusOwner(BusOwnerDTO busOwnerDTO);

	void updateVehicleOfApplication(String vehicleNo, String applicationNo, String logingUser);

	String getPermitNoByVehicleNo(String vehicleNo);

	AmendmentServiceDTO getServiceChange(String generatedApplicationNo);

	String getOldVehicleNoFromAmendment(String generatedApplicationNo);

	String getNewVehicleNoFromAmendment(String generatedApplicationNo);

	String getTrnTypeFromAmendment(String generatedApplicationNo);

	public boolean isMandatory(String currentDocCode, String applicationNo, String permitNo, String strTrnCode);

	public boolean isPhysicallyExit(String currentDocCode, String applicationNo, String permitNo);

	// committee approval

	public List<CommitteeOrBoardApprovalDTO> getAmendmentsApplicationNoForCommitteeApproval(String code);

	public List<CommitteeOrBoardApprovalDTO> getAmendmentsPermitNoForCommitteeApproval();

	public String getAmendmentPermitNo(String applicationNo);

	public String getAmendmentApplicationNo(String permitNo);

	public String getApprovalStatus(String applicationNo);

	public int insertApproveStatus(String applicationNo, String remark, String user);

	public int insertBoardRejectStatus(String applicationNo, String status, String remark, String user);

	public int insertBoardApproveStatus(String applicationNo, String status, String user);

	public boolean insertDocumentPhysicallyExitsStatus(boolean exits, String currentDocCode, String applicationNo,
			String permitNo, String logUser, String tranCode);

	public boolean updateNewPermitNoOnAmendmentForOwnerChange(AmendmentDTO dto, String newPermitNo, String logingUser);

	// need to add modify date and modify by
	public boolean updateApplicationTableOldRecordForOwnerChange(AmendmentDTO dto, String newPermitNo);

	public boolean updateApplicationTableNewRecordForOwnerChange(AmendmentDTO dto, String newPermitNo,
			String logingUser);

	public boolean updateAmendmentForServiceAndBusChange(AmendmentDTO dto, String logingUser);

	// need to add modify date and modify by
	public boolean updateApplicationTableOldRecordForServiceAndBusChange(AmendmentDTO dto);

	public boolean updateApplicationTableNewRecordForServiceAndBusChange(AmendmentDTO dto, String logingUser);

	void saveDataVehicleOwner(String newApplicationNo, String oldApplicationNo, String createdBy, String busNo);

	void saveDataOminiBus(String generatedApplicationNo, String oldApplicationNo, String loginUser);

	void updateOldApplicationNoOfApplication(String oldApplicationNo, String applicationNo, String logingUser);

	public boolean deleteDocumentPhysicallyExitsStatus(boolean exits, String currentDocCode, String applicationNo,
			String permitNo, String logUser, String tranCode);

	public boolean checkTwoChanges(AmendmentDTO dto, String transActionOne, String transActionTwo);

	public boolean checkOneChanges(AmendmentDTO dto, String transActionOne);

	public String getBoardApprovalStatus(String applicationNo);

	public List<CommitteeOrBoardApprovalDTO> getViewAmendmentsNoForCommitteeApprovalStatus(String transCode);

	public List<AmendmentDTO> getAmendmentsAuthorizationDefaultDetails(AmendmentDTO dto, int accessLevel);

	public List<AmendmentDTO> getAmendmentsHigherApporvalApplicationNO(AmendmentDTO dto);

	PermitRenewalsDTO renewalsByBusRegNo(String strVehicleNo);

	public PermitRenewalsDTO renewalsByBusRegNoNew(String strVehicleNo, String strApplicationNo);

	public List<AmendmentDTO> getAmendmentsHigherApporvalPermitNO(AmendmentDTO dto);

	boolean CheckAmendmentsTableCommitteeStatus(AmendmentDTO selectDTO, String string);

	boolean CheckAmendmentsTableBoardStatus(AmendmentDTO selectDTO, String string);

	List<AmendmentDTO> getGrantApprovalDefaultDetailsForSelectedValue(AmendmentDTO amendmentDTO);

	public String getOldApplicationNo(AmendmentDTO dto);

	public String getOldPermitNo(String oldApplicationNo);

	public void updateNewPermitDocuments(AmendmentDTO dto, String permitNo, String user, String filepath);

	public List<AmendmentDTO> getVersionFilePaths(String oldPermit);

	public void updateNewPermitDocumentsVersion(AmendmentDTO dto, String permitNo, String user, String filepath);

	public void saveNewRouteRequest(AmendmentServiceDTO tempNewRouteDTO);

	public boolean updateNewRouteRequest(AmendmentServiceDTO tempNewRouteDTO);

	public AmendmentServiceDTO getNewRouteRequest(String generatedApplicationNo);

	public AmendmentServiceDTO getNewRouteRequestEndChange(String generatedApplicationNo);// added by tharushi.e

	public boolean checkPTAStatus(AmendmentDTO dto, String status);

	public boolean checkTimeApprovalStatus(AmendmentDTO dto, String status);

	public boolean updatePTAStatus(AmendmentDTO dto, String status, String user);

	public boolean updateTimeApprovalStatus(AmendmentDTO dto, String status, String user);

	public boolean checkTempRouteStatus(AmendmentDTO dto, String status);

	public boolean updateTempRouteStatus(AmendmentDTO dto, String status, String user);

	public boolean checkFourChanges(AmendmentDTO dto, String transActionOne, String transActionTwo,
			String transActionThree, String transActionFour);

	public boolean updateVehicelOwnerTableOwnerChange(AmendmentDTO dto, String newPermitNo, String user);

	public boolean updateOminiBusTableOwnerChange(AmendmentDTO dto, String newPermitNo, String user);

	public AmendmentBusOwnerDTO getOldOriginDestination(String permitNo);

	void updateOldPermitNoInAppTb(String newApplicationNo, String oldPermitNo, String permitNo);

	List<CommonDTO> getRoutesToDropdown();

	// added for re print permit, checked selected app no is completed PM400
	public boolean isPrintPermit(String appno);

	public boolean checkRouteStatus(String applicationNo);

	// newly added for route change temp route by tharushi.e
	public AmendmentDTO getRouteNO(String appNo);

	public OminiBusDTO insuranceDetByBusRegNoNew(String strVehicleNo, String strApplicationNo);

	String getTransactionTypeFromAmendmendTable(String appNo);

	public boolean updateApplicationTableRemarkOldRecordForOwnerChange(AmendmentDTO dto, String newPermitNo);
	
	List<AmendmentDTO> getTransactionTypeForTimeApproval();
	public String generateAmendmendApplicationNo();
	void updateNumberGeneration(String generateApplicationNumber, String loggingUser,String type);
	AmendmentBusOwnerDTO getApplicationDetailsByExistingVehicleNo(String strVehicleNo);
	boolean checkInspectionStatus(String appNo);
	String getRouteFlag(String appNo);

	public boolean checkAM113Exists(String generatedApplicationNo);

	AmendmentOminiBusDTO ominiBusByVehicleNoOldData(String strVehicleNo);

	boolean checkTempRoute(String generatedApplicationNo);
	int insertHistoryAndUpdateTable(OminiBusDTO ominiBusDTO,AmendmentDTO amendmentDTO,AmendmentDTO amendmentHistoryDTO,String vehiNo,
			String generatedApplicationNo,String loggingUser,PermitRenewalsDTO applicationHistoryDTO,String string1);
	int insertDataAppVehiOminiAmend(OminiBusDTO ominiBusDTO,String oldApplicationNo, String generatedApplicationNo,String loginUser,String ominiVehiNumber,String numGennCode ,AmendmentDTO amendmentDTO,String strVehicleNo,AmendmentDTO amendmentHistoryDTO);
	int insertDataAppVehiOminiAmend(String ominiVehicleNo,String generatedApplicationNo,String loginUser,String numGennCode ,OminiBusDTO ominiBusDTO,AmendmentDTO amendmentDTO,String strVehicleNo,AmendmentDTO amendmentHistoryDTO);
	int insertAppOminiVehiAmendInOwnerChange(BusOwnerDTO busOwnerDTO,String oldApplicationNo,String generatedApplicationNo,String loginUser,String numGennCode, AmendmentDTO amendmentDTO,String strVehicleNo,String ominiBusNumForAmenment,AmendmentDTO amendmentHistoryDTO);
	int updateInsertForServiceChange(BusOwnerDTO busOwnerDTO,String oldApplicationNo,String generatedApplicationNo,String loginUser,String strVehicleNo,String numGennCode, String busOwnerRouteFlag, AmendmentServiceDTO amendmentServiceDTO,PermitRenewalsDTO applicationHistoryDTO);
	String getApplicationStatus(String applicationNo);
	OminiBusDTO emmissionDetByAppNo(String appNo);
	boolean checkSavedTaskAlreadyInTaskHistoryTable(String appNo);
}
