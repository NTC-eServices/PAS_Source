package lk.informatics.ntc.model.service;

import java.util.Date;
import java.util.List;

import lk.informatics.ntc.model.dto.ActivateCancelledPermitsDTO;
import lk.informatics.ntc.model.dto.PermitDTO;

public interface CancellationsPermitService {

	public List<PermitDTO> getExpiredPermitNo();

	public List<PermitDTO> getExpiredBusRegNo();

	public List<PermitDTO> getCancellationData(PermitDTO permitDTO, Date date);

	public List<PermitDTO> getdefaultData();

	public boolean checkTaskDetails(PermitDTO dto, String taskCode, String status);

	public boolean insertTaskDetails(PermitDTO dto, String loginUSer, String taskCode, String status);

	public boolean deleteTaskDetails(PermitDTO dto, String taskCode);

	public boolean CopyTaskDetailsANDinsertTaskHistory(PermitDTO dto, String loginUSer, String taskCode);

	public boolean updateApplicationTableStatus(PermitDTO dto, String logingUser, boolean cancelType);

	public boolean insertPermitCancleApproveDetails(PermitDTO dto, PermitDTO dto2, String logUer, boolean cancelType);

	public boolean updatePermitCancleApproveDetails(PermitDTO dto, PermitDTO dto2, String logUer, boolean cancelType);

	public boolean insertPermitCancleRejectDetails(PermitDTO dto, PermitDTO dto2, String logUer);

	public boolean updatePermitCancleRejectDetails(PermitDTO dto, PermitDTO dto2, String logUer);

	public boolean checkDataInPermitCancellation(PermitDTO dto, String status);

	public boolean checkApplicationTabel(PermitDTO dto, String status);

	public PermitDTO completePermitNoData(PermitDTO dto);

	public PermitDTO completeVehicleNoData(PermitDTO dto);

	public PermitDTO getViewData(PermitDTO dto);

	public List<ActivateCancelledPermitsDTO> getPermitNoList();

	public ActivateCancelledPermitsDTO getCurrentApplicationNo(String currentPermitNo);

	public List<ActivateCancelledPermitsDTO> getCurrentVehicleNo(String currentApplicationNo, String currentPermitNo);

	public ActivateCancelledPermitsDTO getAllDetailsForSelectedDetails(String selectedPermitNo,
			String selectedVehicleNo, String cancelledDate);

	public ActivateCancelledPermitsDTO getDefaultRecords(String currentPermitNo, String currentVehicleNo,
			String currentApplicationNo);

	public ActivateCancelledPermitsDTO getLoadDetailsForSelectedPermitNo(String selectedPermitNo);

	public ActivateCancelledPermitsDTO getLoadDetailsForSelectedVehicleNo(String selectedVehicleNo);

	public List<ActivateCancelledPermitsDTO> getPermitNoListForSelectedDate(String cancelledDate);

	public int updateApplicationTableStatus(ActivateCancelledPermitsDTO activateCancelledPermitsDTO);

	public int updatePermitCancelTable(ActivateCancelledPermitsDTO activateCancelledPermitsDTO);

	public List<ActivateCancelledPermitsDTO> getAllRecordsForSelectedCancelledDate(String cancelledDate);

	public ActivateCancelledPermitsDTO getAllDetailsForSelectedViewBtn(String applicationNo, String permitNo,
			String regNoOfTheBus);

	public ActivateCancelledPermitsDTO getDistrictDescription(String provinceCode, String districtCode);

	public ActivateCancelledPermitsDTO getDivisionSectionDescription(String districtCode, String divSectionCode);

	public List<ActivateCancelledPermitsDTO> getAllRecordsForDocumentsCheckings();

	public int insertPM403RecordTaskDet(String applicationNo, String regNoOfTheBus, String previousTaskCode,
			String currentUpdatedTaskCode, String loginUser);

	public List<ActivateCancelledPermitsDTO> getDefaultRecords();

	public boolean checkDataForReport(String Appno);

	public void updateLetterPrintStatus(String AppNo);

	public void updateCancelLetterPrintStatus(String AppNo);

	public int insertPM403RecordTaskHis(String applicationNo, String regNoOfTheBus, String previousTaskCode,
			String currentUpdatedTaskCode, String loginUser);

}
