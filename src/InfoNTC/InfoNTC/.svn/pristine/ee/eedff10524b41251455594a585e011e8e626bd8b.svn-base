package lk.informatics.ntc.model.service;

import java.io.Serializable;
import java.util.List;

import lk.informatics.ntc.model.dto.BusOwnerDTO;
import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.IssuePermitDTO;
import lk.informatics.ntc.model.dto.OminiBusDTO;
import lk.informatics.ntc.model.dto.PermitRenewalsDTO;
import lk.informatics.ntc.model.dto.RouteDTO;

public interface IssuePermitService extends Serializable {

	BusOwnerDTO getOwnerDetailsByNicNo(String nicNo);

	IssuePermitDTO getApplicationDetailsByApplicationNo(String strApplicationNo);

	IssuePermitDTO getApplicationDetailsByQueueNo(String strQueueNo);

	String saveBusOwnerDetails(BusOwnerDTO busOwnerDTO);

	int updateBusOwner(BusOwnerDTO busOwnerDTO);

	String checkDuplicateApplicationNo(String applicationNo);

	int updateNewPermitOminiBus(OminiBusDTO ominiBusDTO);

	int saveNewPermitOminiBus(OminiBusDTO ominiBusDTO);

	OminiBusDTO ominiBusByVehicleNo(String busRegNo);

	RouteDTO getDetailsbyRouteNo(String strSelectedRoute);

	RouteDTO getTempRouteDetails(String appNo);// added for get newly added route data

	List<CommonDTO> getRoutesToDropdown();

	int updateService(IssuePermitDTO issuePermitDTO);

	int saveService(IssuePermitDTO issuePermitDTO);

	IssuePermitDTO serviceDetailsByPermitNo(String permitNo);

	int updateDocumentRemark(Long currentRecordSeqNo, String currentUploadFilePath, String modifyBy,
			String currentRemark);

	PermitRenewalsDTO getRemarkDetails(String currentDocCode, String applicationNo, String permitNo);

	boolean checkIsSumbiited(String currentDocCode, String applicationNo, String permitNo);

	List<PermitRenewalsDTO> getAllRecordsForDocumentsCheckings();

	IssuePermitDTO getApplicationDetailsByVehicleNo(String strBusRegNo);

	BusOwnerDTO getOwnerDetailsByApplicationNo(String strApplicationNo);

	// This method check for application is incomplete or not
	String checkForIncompleteApplication(String applicatioNo);

	boolean getCompletedTab(String tabIndex, String applicationNo);

	public String checkForApplicationNo(String value);

	public BusOwnerDTO getOwnerDetails(String strApplicationNo);

	String getApplicationNoByVehicleNo(String strBusRegNo);

	String generatePermitNo();

	public String getTempPermit(String app);

	IssuePermitDTO getSelectedValuesForUploadPhotos(String applicationNo, String permitNo);

	public String checkPermitNoFromQueueNo(String queueNo);

	public String checkTsakStatusPM101FromQueueNo(String queueNo);

	public boolean isMandatory(String currentDocCode, String applicationNo, String permitNo);

	public boolean isPhysicallyExit(String currentDocCode, String applicationNo, String permitNo);

	public boolean insertDocumentPhysicallyExitsStatus(boolean exits, String currentDocCode, String applicationNo,
			String permitNo, String logUser);

	public boolean deleteDocumentPhysicallyExitsStatus(boolean exits, String currentDocCode, String applicationNo,
			String permitNo, String logUser);

	public String generatePermitNoNew(String applicationNo);

}
