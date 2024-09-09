package lk.informatics.ntc.model.service;

import java.util.List;

import lk.informatics.ntc.model.dto.DropDownDTO;
import lk.informatics.ntc.model.dto.SuspendDTO;

public interface SuspendService {

	List<SuspendDTO> getChargeRefNoList();

	SuspendDTO searchInvestigationDetails(SuspendDTO searchDTO);

	List<DropDownDTO> getSuspendTypeList();

	boolean saveSuspendMaster(SuspendDTO suspendDTO);

	List<SuspendDTO> getSavedSuspendRecords();

	boolean updateSuspendRecord(SuspendDTO suspendDTO);

	int checkDuplicate(SuspendDTO checkDTO);

	List<String> filterPermitsForApproval();

	List<String> filterVehicleForApproval();

	List<String> filterDriverForApproval();

	List<String> filterConductorForApproval();

	boolean updateApproveStatus(SuspendDTO approveDto, String remark,String des);

	boolean updateDirectorApproveStatus(SuspendDTO dirApproveDto, String prevApp, String preAppStatus, String des);

	List<SuspendDTO> searchSuspendDetailsForApprove(SuspendDTO searchDTO, String isDirector);

	List<SuspendDTO> getSavedSuspendRecordsByChargeRef(String chargeRef);

	boolean updatePermitStatus(SuspendDTO selectedDTO, String permitNo, String status, String user);

	boolean saveTaskHis(String vehicleNo, String chargeRef, String taskCode, String user);

	List<SuspendDTO> getSuspendListByPermitNo(String permitNo);

	SuspendDTO getDetasilsDTOByPermitNO(String permitNO);

	public String getNameByDCId(String id);

	String getStatusDesc(String code);

	boolean updateDriverConductorStatus(SuspendDTO selectedDTO, String DCid, String status, String user,String des);

	SuspendDTO getPreviousAppDet(String permitNo, String driverId, String conductorId);

	public List<SuspendDTO> getChargeRefNoListforSearch();

	public List<String> getAllVehicle();

	public List<String> getAllPermit();

	public List<SuspendDTO> getSavedSuspendRecordsSearch(SuspendDTO searchDTO);

	void beanLinkMethod(SuspendDTO selectedDTO, String des,String funDes );

}
