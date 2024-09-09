package lk.informatics.ntc.model.service;

import java.util.List;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.ComplaintRequestDTO;
import lk.informatics.ntc.model.dto.PageFormatDTO;

public interface CommonService {

	public List<CommonDTO> counterDropdown(String trnType);

	public void counterStatus(String counterId, String user);

	void updateTaskStatus(String applicationNo, String prevTask, String currTask, String taskStatus, String userName);

	public void updateTaskStatusCompleted(String strApplicationNo, String string, String user);

	public boolean updateTaskStatusCompletedForAmendments(String strApplicationNo, String string);

	public void insertTaskDet(String vehicleNo, String appNo, String loginUSer, String taskCode, String taskStatus);

	public int updateDataInNT_T_TASK_DET(String vehicleNo, String appNo, String taskCode, String taskStatus);

	public void updateCounterQueueNo(String queueNo, String counterNo);

	public String taskStatus(String vehicleNo, String taskCode);

	public List<CommonDTO> getApplicationNo();

	public List<CommonDTO> getVehicleNo();

	public List<CommonDTO> getPermitNo();

	public List<CommonDTO> counterdropdown();

	public List<CommonDTO> getInquiryDetails(CommonDTO commonDTO);

	public CommonDTO getInquiryApplicationORVehicleNo(String queueNo);

	public boolean checkQueueNo(String queueNo);

	public String getPaymentType(CommonDTO commonDTO);

	public String getPermitNo(CommonDTO commonDTO);

	public String getVoucherNo(CommonDTO commonDTO);

	public boolean checkApplicationNoAvailable(String applicationNo);

	public boolean checkTaskDetails(String applicationNo, String taskCodeOne);

	public boolean checkTaskHistory(String applicationNo, String taskCodeOne);

	public List<CommonDTO> getHistoryData(CommonDTO commonDTO);

	// Survey Management - update task details & update history

	public void insertSurveyTaskDetails(String surveyReqNo, String currTask, String taskStatus, String loginUser);

	public void updateSurveyTaskDetails(String surveyReqNo, String surveyNo, String currTask, String taskStatus,
			String loginUser);

	// For Tender Evaluation Process and Approve Elected Bidder
	public void updateTenderTaskDetails(String tenderReferenceNo, String currTask, String taskStatus);

	List<CommonDTO> countersDropdown(String trnType);

	boolean checkTaskOnTaskDetails(String applicationNo, String taskCode, String status);

	public String taskStatusOnTaskDetail(String strVehicleNo, String taskCode, String applicationNo);

	public boolean checkTaskOnSurveyTaskDetails(String tenderReferanceNo, String taskCode, String status);

	void updateCounterNo(String counterNo);

	public void updatePhotoUploadStatus(String generatedApplicationNo, String photoUpload);

	public String getPhotoUploadStatus(String generatedApplicationNo);

	public void duplicatePhotoUpload(String generatedApplicationNo, String oldApplicationNo);

	public void duplicateActionPoints(String generatedApplicationNO, String oldApplicationNo);

	public boolean taskStatusOnSurveyTaskDetails(String surveyNo, String taskCode, String status);

	public String applicationStatus(String applicationNo);

	public void insertPermitPrintInfo(String applicationNo, String PermitNo, String VehicleNo, String logedUser);

	public Integer getPermitPrintedVersionNo(String applicationNo);

	public int dashboardPendingCount(String title);

	public boolean checkTaskOnSurveyHisDetails(String surveyNo, String taskCode, String status);

	public void updateSurveyTaskDet(String selectedRequestNo, String selectedSurveyNo, String string, String string2,
			String loginUser);

	void updateTaskStatusTenderInSurveyTaskTabel(String applicationNo, String prevTask, String currTask,
			String taskStatus, String userName);

	public void updateTaskStatusCompletedTenderInSurveyTaskTabel(String strApplicationNo, String string);

	public boolean checkTaskOnSurveyHisDetailsByTenderRefNo(String tenderRefNo, String taskCode, String status);

	public boolean checkTaskHisByApplication(String applicationNo, String taskCode, String taskStatus);

	public boolean checkTaskDetailsInSubsidy(String reqNo, String taskCode, String status);

	public boolean insertTaskDetailsSubsidy(String reqValue, String value, String loginUSer, String taskCode,
			String status, String surviceReferenceNo);

	// previous task add to history and add my task on going
	void updateTaskStatusSubsidyTaskTabel(String requestNo, String serviceNo, String referenceNo, String prevTask,
			String currTask, String taskStatus, String userName);

	// my on going task add to history and add my task complete
	public boolean updateTaskStatusCompletedSubsidyTaskTabel(String requestNo, String serviceNo, String referenceNo,
			String currTask, String status);

	public boolean checkAccessPermission(String loginUser, String functionCode, String activityCode);

	public int checkActionPointsCount(String appNo);

	public void sendEmail(String empNo, String message, String subject);

	public void sendSMS(String empNo, String message, String subject);

	public void updateSurveyTaskDetailsBySurveyNo(String surveyReqNo, String surveyNo, String currTask,
			String taskStatus, String loginUser);

	public List<CommonDTO> routeRequest();

	public List<CommonDTO> timeApproval();

	public void timeapproveRequest(String user,String timeSlots, String appNo);

	public void completed(String seqNo, String user);

	public boolean checkTaskDetailsInSubsidyByServiceRefNo(String serviceRefNo, String taskCode, String status);

	public String selectPreviousTaskCodeSubsidyTaskTabel(String requestNo, String serviceNo, String referenceNo);

	public void updateQueMasterTableTask(String appno, String taskCode, String task);

	public String generateNewRefNo(String code);

	public String updateRefNoSeq(String code, String no, String user);

	public String[] getOwnerByVehicleNo(String permitNo);

	public List<String> getAllVehicle();

	public List<String> getAllPermit();

	/**
	 * for bus removal and modify permit data task update
	 */
	public void updateCommonTaskHistory(String busNo, String appNo, String task, String taskCode, String loginUser);

	public List<CommonDTO> GetCourtGroups();

	public List<PageFormatDTO> getPageFormats();

	public Boolean IsAppAvailable(String appNo);
	public Boolean IsAppAvailableForDel(String appNo) ;

	public Boolean IsPaymentProcessOngoing(String appNo);

	public boolean updateApplication(String strActAppNo, String strDetAppNo, String loginUser);

	public Boolean IsSamePermitNo(String strActAppNo, String strDetAppNo);

	public CommonDTO vehicleNoValidation(String vehicleNo);

	/** Other Inspection task insert and update method start, added by GAYATHRA **/

	public void otherInspectionTasksUpdate(String applicationNo, String currentTask, String currentStatus,
			String createBy, String counterID, String vehicleNo);

	/** Other Inspection task insert and update method end **/

	public Boolean IsHavingIncompleteInspection(String vehicleNo);

	/** Other inspection skip queue no. -start **/

	public void updateOtherInspectionSkipQueueNumber(String queueNumber);

	/** other inspection skip queue no - end **/

	public String getSisuRequstNoForServiceNoInTaskTable(String serviceNo);

	public String getSisuRefNoForServiceNoInTaskTable(String serviceNo);

	public boolean existingLogSheetReferenceNo(String referenceNo, String serviceNo);
	
	public List<String> getAllTemporaryVehicle();

	public List<String> getAllTemporaryPermit();
	
	public boolean isTemporaryActiveBusNumber(String vehiNo);
	
	public boolean insertDataIntoComplainRequestHistoryAndUpdate(String busNo, String user);
	String getSisurServiceNoInTaskTable(String refNo);
	String getQueueNumberFromQueMasterByAppNo(String appNo);

	public boolean existingLogSheetReferenceNo(String logSheetRefNo);

}