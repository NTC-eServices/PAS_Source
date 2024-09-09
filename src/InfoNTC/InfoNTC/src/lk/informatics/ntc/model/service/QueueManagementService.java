package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.util.List;

import lk.informatics.ntc.model.dto.DepartmentDTO;
import lk.informatics.ntc.model.dto.DisplayQueueNumbersDTO;
import lk.informatics.ntc.model.dto.DivisionDTO;
import lk.informatics.ntc.model.dto.MainCounterDTO;
import lk.informatics.ntc.model.dto.QueueNumberDTO;
import lk.informatics.ntc.model.dto.TransactionTypeDTO;
import lk.informatics.ntc.model.dto.UserClosedDTO;

public interface QueueManagementService {

	public List<TransactionTypeDTO> retrieveTransactionTypeList();

	public String insertQueueDataIntoNT_M_QUEUE_MASTER(QueueNumberDTO queueNumberDTO, String loginUser);

	public String retrieveTransDescFromTransCode(String transCode);

	public String retrieveOwnerName(QueueNumberDTO queueNumberDTO);

	public QueueNumberDTO validateUserInputValues(QueueNumberDTO queueNumberDTO);

	public boolean checkSelectedTransactionTypeValidForQueueNumber(QueueNumberDTO queueNumberDTO, String transType);

	public boolean validateDuplicateQueueNoGenerate(QueueNumberDTO queueNumberDTO);

	public String callNextQueueNumberAction(String transactionType, String previousTransactionType);

	public void createQueuNumberOrder(String queueTypeCode, String loginUser);

	public boolean validateApplicationNoIsBackLog(String applicationNo);

	public boolean validateDuplicateQueueNoGenerateInQueuMaster(QueueNumberDTO queueNumberDTO);

	public boolean validateTokenforSameDay(String vehicleNo);

	public QueueNumberDTO validateUserInputValuesForRNAndNP(QueueNumberDTO queueNumberDTO);

	public boolean checkAmmenmentValidation(QueueNumberDTO queueNumberDTO);

	public List<String> callNextQueueNumbersForDisplayAction(String transactionType, String previousTransType);

	public List<String> skippedQueueNumberForDisplay(Connection con, String transactionType, String previousTransType);

	boolean checkAmmenmentValidationForOngoingQueueNumbers(QueueNumberDTO queueNumberDTO);

	List<DisplayQueueNumbersDTO> counterDetails();

	public List<DisplayQueueNumbersDTO> currentNumbers();

	public void updateCounter(String counterId, String counterStatus);

	public String checkTaskHistoryForInspection(String vehicleNo);

	public boolean checkTaskHistoryForInspectionUploadPhotosComplete(String vehicleNo);

	public String getApplicationNumOfOldQueueFromAmendment(String permitNo, String vehicleNo);

	public boolean validateTasksOfApplicatinNum(String applicationNo);

	public boolean checkTaskHistoryForInspectionComplete(String vehicleNo);

	public boolean checkInspectionTokeGenerated(String vehicleNo);

	public QueueNumberDTO validateUserInputValueForInspectionAmmendment(QueueNumberDTO queueNumberDTO);

	public String checkProcessIsDoneForAmmendment(QueueNumberDTO queueNumberDTO);

	public boolean checkTaskHistoryForInspectionAmmendment(String vehicleNo);

	public void updateQueueNoInApplication(String queueNo, String appNo);

	/*
	 * Description: KIOSK CHANGES Date: 01/19/2021 Done By: THILINA.D
	 * 
	 */

	// Main Display-KIOSK 01
	public QueueNumberDTO generateMainTokenNumber(String sectionCode);

	public List<DivisionDTO> getAllDivisions();

	// QMS User Console
	public List<MainCounterDTO> getActiveCountersOfDivision(String sectionCode);

	public boolean isQueueNoProceed(String queueno);

	public int isValidQueueNo(String queueno);// For CALL button before calling callTokenNo method

	public boolean callTokenNo(String sectionCode, String counterId, String queueno);// For CALL button

	public String getNextToken(String sectionCode);// For CALL NEXT button

	// For QMS Customer Dashboard Console
	public List<DepartmentDTO> getAllActiveDepartments();

	public List<MainCounterDTO> getCountersOfDepartments(String departmentCode);

	// User Closed Console
	public List<MainCounterDTO> getCountersOfDivision(String sectionCode);

	public List<UserClosedDTO> getOngoingQueueRecordsForClosedConsole();

	public List<UserClosedDTO> getUserClosedSearchResults(UserClosedDTO userClosedDTO);

	public boolean completeMainQueue(UserClosedDTO userClosedDTO);

	/* New queue generate CR start here */

	public List<QueueNumberDTO> getQueuesForTransactionType(String transactionType);

	public boolean checkSelectedTransactionTypeValidForQueueNumberNewMethod(QueueNumberDTO queueNumberDTO,
			String transType);

	public boolean isVehicleDetailsFoundInTaskDetails(String vehicleNo);

	public boolean isInspectionComplete(String vehicleNo);

	public QueueNumberDTO getPermitInfo(String permitNo);

	public boolean isApplicatioinDetailsFoundInTaskDetails(String appNo);

	public boolean isInspectionStatusNull(String appNo);

	public String insertQueueDataIntoQueueMasterTable(QueueNumberDTO queueNumberDTO, String loginUser);

	public void updateQueueMasterTable(QueueNumberDTO dto, String tokenNo);

	public QueueNumberDTO getPermitInfoWithoutCheckingStatus(String permitNo);

	public QueueNumberDTO getAmendmentVehicleNo(String permitNo, String applicationNO);

	/* New queue generate CR end here */

	/* Other inspection CR start here */

	public QueueNumberDTO getCallNextQueueNo();

	public int updateQueueNoSkipCount(String queueNO, String permitNo, String loginUser);

	/* Other inspection CR end here */

	/* Inspection renewal CR start here */
	public QueueNumberDTO getInspectionProceedStatus(String vehicleNo, String inspectionType);
	/* Inspection renewal CR end here */
	
	public boolean checkTokenNumberAvailableForQueueNoGenerate(String tokenNumber, String tokenStatus);
}
