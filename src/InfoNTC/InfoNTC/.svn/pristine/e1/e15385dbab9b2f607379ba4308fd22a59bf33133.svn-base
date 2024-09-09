package lk.informatics.ntc.model.service;

import java.io.Serializable;
import java.util.List;

import lk.informatics.ntc.model.dto.CommitteeOrBoardApprovalDTO;
import lk.informatics.ntc.model.dto.EmployeeDTO;
import lk.informatics.ntc.model.dto.FormDTO;
import lk.informatics.ntc.model.dto.GenerateReceiptDTO;
import lk.informatics.ntc.model.dto.IndicatorsDTO;
import lk.informatics.ntc.model.dto.SurveyDTO;
import lk.informatics.ntc.model.dto.SurveyLocationTeamDTO;
import lk.informatics.ntc.model.dto.TrafficProposalDTO;

public interface SurveyService extends Serializable {

	// for Initiate Survey Request

	public String saveSurveyRequestData(SurveyDTO surveyMgtDTO, String user);

	public boolean updateSurveyRequestData(SurveyDTO surveyMgtDTO, String user);

	public String generateSurveyRequestNo();

	// For Edit Initiate Survey Request

	public boolean updateNewRouteDetails(SurveyDTO surveyMgtDTO, String user);

	// for Initiate Survey Process Page(get Request No which is status are I )
	public List<SurveyDTO> getSurveyReqOnGoingNoDropDown();

	// for Initiate Survey Process Page(get Request No which is status are S )
	public List<SurveyDTO> getSurveyReqNoDropDown();

	// get Approve and reject request Number list
	public List<SurveyDTO> getApprandRejDropDown();

	public List<SurveyDTO> getSurveyTypeToDropDown();

	public List<SurveyDTO> getSurveyMethodToDropDown();

	public List<SurveyDTO> getSurveyNoDropDown();

	public List<SurveyDTO> getRequestTypeToDropDown();

	// Mushtharq : Initiate Survey Request

	public boolean saveTempRouteDetails(SurveyDTO routeDetailsDTO, String user);

	public boolean isDuplicateRouteNo(SurveyDTO routeDetailsDTO);

	public List<SurveyDTO> getOrganisationToDropDown();

	public List<SurveyDTO> getDepartmentToDropDown();

	public List<SurveyDTO> getRouteNoListToDropDown();

	// save survey data for initiate Survey Process task

	public String saveSurveyData(SurveyDTO surveyDTO);

	// fill route details from route no
	public SurveyDTO fillRouteDetailsFromRouteNo(String routeNo);

	// fill Organization
	public String fillOrga(String requestNo);

	// fill Department
	public String fillDepart(String requestNo);

	// fill Request Type
	public String fillRequestType(String requestNo);

	// fill Survey Reason
	public SurveyDTO fillSUrveyReason(String requestNo);

	// fill Route No
	public String fillRouteNo(String requestNo);

	// fill Route Details
	SurveyDTO fillRouteDetails(String requestNo);

	// fill New Route Details
	SurveyDTO fillNewRouteDetails(String requestNo);

	String generateSurveyNo();

	// for edit Initiate page
	public SurveyDTO fillSurReq(String surveyNo);

	public SurveyDTO fillSurNo(String requestNo);

	// fill searched data for Edit Survey Initiate Page
	public SurveyDTO showDetails(String requestNo, String surveyNo);

	// save Edited Data from edit Survey Initiate page
	public void saveEditedSurveyData(String selectSurveyType, String selectSurveyMethod, String selectremarks,
			String selectrequestNo, String selectSurveyNo, String loginUser);

	// Approve Survey Process Request
	public boolean approveSurveyProcessRequest(SurveyDTO surveyDTO, String user);

	public boolean rejectSurveyProcessRequest(SurveyDTO surveyDTO, String user);

	public boolean saveTempRouteDetails1(SurveyDTO routeDetailsDTO, String user);

	// pathum

	// This method returns survey numbers
	public List<SurveyDTO> getApprovedSurveyNoDropDown();

	/**
	 * This method provide locations. condition : active = ‘Y’ :
	 * public.nt_r_location Table
	 * 
	 * @return List locationList : SurveyDTO (location)
	 * 
	 */
	public List<SurveyDTO> getLocationDropDown();

	// This method returns surveyNo,surveyType,surveyMethod
	public SurveyDTO getDetailsByServeyNo(String surveyNo);

	// This method add all the values to public.nt_t_survey_location table
	public void addLoactionTime(SurveyDTO surveyDTO);

	// This method retreive data for location table
	public List<SurveyDTO> getLocationTimeTable(String surveyNo);

	// This method returns user id
	public List<EmployeeDTO> getUserIdDropDown();

	// This method returns full name related to user id
	public String returnName(String userId);

	// This method provide list of organizations
	public List<SurveyDTO> getOrganizationListDropDown();

	public List<GenerateReceiptDTO> getBankListDropDown();

	public List<GenerateReceiptDTO> getBranchListDropDown(String bankCode);

	public List<SurveyLocationTeamDTO> getResponsibilitiesList();

	public GenerateReceiptDTO getBranchListByCodeDropDown(String branchCode, String bankCode);

	public List<GenerateReceiptDTO> getBankBranchDropDown(String bankCode);

	public void addTeamByLocation(SurveyDTO surveyDTO, SurveyLocationTeamDTO surveyLocationTeamDTO);

	public List<SurveyLocationTeamDTO> getTeamDetailsTableList(SurveyDTO surveyDTO);

	public void deleteRawFromLocationTable(String locationSeqNo);

	public void deleteRawFromTeamDetailsTable(String memberSeqNo);

	public void deleteTeamDetailsByLocation(String locationSeqNo);

	public List<String> getCostCodeDropDown();
	
	public List<SurveyLocationTeamDTO> getCostCodeDropDownForGSSurveyManagement();

	public String getCodeDescription(String costCode);

	public void addCostEstimation(SurveyLocationTeamDTO surveyLocationTeamDTO, SurveyDTO surveyDTO);

	public List<SurveyLocationTeamDTO> getCostEstimationTableList(SurveyDTO surveyDTO);

	public void deleteRawFromCostEstimateTable(String seqNO);

	public void updateTaskOnDetTable(String surveyNo);

	public void deleteTaskOnDetTable(String surveyNo);

	public void updateTaskOnHisTable(String surveyNo);

	public void approveRejectCostEstimation(String surveyNo, String isApproved,
			SurveyLocationTeamDTO surveyLocationTeamDTO);

	public String getCostEstimationApproveStatus(String surveyNo);

	public List<TrafficProposalDTO> getRequestNoList();

	public TrafficProposalDTO displayValuesForSelectedReqNo(String requestNo);

	public List<TrafficProposalDTO> getRouteDetailsForSelectedSurveyNo(String selectedSurveyNo,
			String selectedRequestNo);

	public int updateRouteDet(TrafficProposalDTO selectedRow);

	public boolean checkTaskCodeForCurrentSurveyNo(String selectedRequestNo, String selectedSurveyNo, String taskCode,
			String taskStatus);

	public void insertTaskDetails(String selectedRequestNo, String selectedSurveyNo, String loginUser, String taskCode,
			String taskStatus);

	public boolean checkTaskDetails(String selectedRequestNo, String selectedSurveyNo, String taskCode,
			String taskStatus);

	public boolean CopyTaskDetailsANDinsertTaskHistory(String selectedRequestNo, String selectedSurveyNo,
			String loginUser, String taskCode);

	public boolean deleteTaskDetails(String selectedRequestNo, String selectedSurveyNo, String taskCode);

	public String generateReferenceNo();

	public int insertDataIntoTrafficProTb(TrafficProposalDTO trafficProposalDTO, String createdBy);

	public int updateTaskCode(String selectedRequestNo, String selectedSurveyNo, String loginUser, String taskCode,
			String taskStatus);

	public boolean checkTrafficProposalNo(String selectedRequestNo, String selectedSurveyNo);

	public int updateDataInTrafficProTb(TrafficProposalDTO trafficProposalDTO, String createdBy);

	public String getTrafficProposalNoForSelectedSurveyNo(String selectedSurveyNo);

	// for survey Summary Details
	public List<SurveyDTO> getSurveySummarySurveyNo();

	public SurveyDTO fillSurveyTypeMethod(String surveyNo);

	public SurveyDTO filldet(String surveyNo);

	public List<SurveyDTO> showRouteDetails(String surveyNo);

	public SurveyDTO showSeqNo(SurveyDTO surveyDTO, String string1, Integer intg, String string2, String string3,
			String string4);

	public List<SurveyDTO> removeEditedData(SurveyDTO surveyDTO, String string1, Integer intg, String string2,
			String string3, String string4);

	public List<SurveyDTO> updateEditedData(SurveyDTO surveyDTO, String string1, Integer intg, String string2,
			String string3, String string4);

	// public List<SurveyDTO> showAddDetails(SurveyDTO surveyDTO,String
	// loginUser,List<SurveyDTO> showGridDataDTO);
	public List<SurveyDTO> showAddDetails(SurveyDTO surveyDTO, String loginUser);

	public List<SurveyDTO> addDetailsToGrid(SurveyDTO surveyDTO);

//get route numbers from surveydata table
	public List<SurveyDTO> getSurveyRouteNo(String surveyNo);

	// update initiate_survey table
	public List<SurveyDTO> updateSurveyData(SurveyDTO surveyDTO, String loginUser);

	public List<CommitteeOrBoardApprovalDTO> getSurveyNoForCommitteeApproval();

	public String getSurveyType(String surveyNo);

	// added by tharushi.e for gami sariya
	public String getGamiSurveyType(String surveyNo);

	public String getSurveyMethod(String surveyNo);

	// added by tharushi.e for gami sariya
	public String getGamiSurveyMethod(String surveyNo);

	public String getRequestNo(String surveyNo);

	public String getTaskStatusForApproval(String requestNo);

	public String getBoardTaskStatusForApproval(String requestNo);

	public List<CommitteeOrBoardApprovalDTO> getTransactionTypeDropDown();

	public String getRefNo(String transactionCode, String comType);

	public List<CommitteeOrBoardApprovalDTO> getDataToCommitteBoardApprovalList(String refNo);

	public String checkCommitteeApproval(String requestNo);

	// added by tharushi.e for gami sariya
	public String checkCommitteeApprovalForGamiSariya(String surveyNo);

	public int insertCommitteeApprovalData(String surveyNo, String refNo, String remark, String status, String user,
			CommitteeOrBoardApprovalDTO committeeApprovalDTO);

	public int insertCommitteeApprovalDetailData(long committeeSeqNo, String userName, String status, String date,
			String remark, String user);

	public int checkBoardApproveStatus(long cadSeq);

	public int insertBoardApproveStatus(String requestNo, String proccessStatus, String proccessBy);

	public int insertBoardRejectStatus(String requestNo, String proccessStatus, String proccessBy, String remark);

	// added by tharushi.e for gamisariya
	public int insertBoardRejectStatusInGamiSariya(String surveyNo, String proccessStatus, String proccessBy,
			String remark);

	public List<SurveyDTO> updateApproveRejectStatus(SurveyDTO surveyDTO, String loginUser);

	public List<SurveyDTO> updateRejectStatus(SurveyDTO surveyDTO, String loginUser);

	// public int getCurrentNoOfPermitsReqToIssue(String selectedSurveyNo, String
	// selectedRouteNo);
	public String getTaskStatus(String surveyNo);

	public String getApproveRejectStatus(String surveyNo);

	public int insertApproveStatus(String requestNo, String proccessStatus, String proccessBy, String remark);

	// added by tharushi.e for gamisariya
	public int insertApproveStatusInGamisariya(String surveyNo, String proccessStatus, String proccessBy,
			String remark);

	public int getCurrentNoOfPermitsReqToIssue(String selectedSurveyNo, String selectedRouteNo);

	public boolean checkIsBoardApproved(String selectedSurveyNo, String selectedRequestNo);

	// pathum
	public List<String> get_Drpd_FormIdList();

	public FormDTO getDetailsByFormId(FormDTO formDTO);

	public List<FormDTO> get_drpd_FormulaNameList();

	public List<String> get_drpd_FieldNameList(String string);

	public List<String> get_drpd_OperatorList(String formId);

	public List<String> get_drpd_FormulaIdList(String formId);

	public String addFormulaToGrid(FormDTO formDTO);

	public List<FormDTO> get_tbl_formulaList(FormDTO formDTO);

	public boolean removeSelectedFormula(String formulaId);

	public boolean getSurManApprovalStatus(String loginUser);

	public TrafficProposalDTO getDetailsForTrafficProNo(String selectedSurveyNo, String selectedRequestNo,
			String selectedProposalNo);

	public String insertInToTblCostEstimation(SurveyDTO surveyDTO, SurveyLocationTeamDTO surveyLocationTeamDTO);

	public boolean getCostApprovedStatus(String surveyNo, String status);

	// pathum - analyze survey data
	public List<IndicatorsDTO> get_drp_FieldNamesList(String formId);

	public String getSelectedFormulaName(String formulaCode);
	//

	public String checkDuplicateRouteNo(SurveyDTO surveyDTO);

	public List<CommitteeOrBoardApprovalDTO> getViewSurveyNoForCommitteeApprovalStatus();

	public List<CommitteeOrBoardApprovalDTO> getDetails01(String refNo);

	public List<CommitteeOrBoardApprovalDTO> getDetails02(int seqNo);

	public int insertCommitteeApprovalDetailDataNoUserName(long committeeSeqNo, String status, String date,
			String remark, String user);

	public String getMainRemark(String refNo, String applicationNo);

	String getBoardApproveStatus(String surveyNo);

	int getSeqNo(String refNo, String applicationNo);

	String getBoardApproveStatusForAmendments(String applicationNo);

	public String checkAuthStatus(String refNo);

	// pathum 28/03/2019 => analyze surveyData
	public int getFormulaCount(FormDTO formDTO);

	public boolean updateRouteBusFare(SurveyDTO surveyMgtDTO, String loginUser);

	public boolean getTaskBoolean(String surveyNo);

	public String returnNICNo(String userId);

	public String returnSurveyTypeDes(String surveyType);

	public String returnSurveyMethodDes(String surveyMethod);

	public List<CommitteeOrBoardApprovalDTO> getGamiSurveyNoForCommitteeApproval();

	public List<CommitteeOrBoardApprovalDTO> getGamiViewSurveyNoForCommitteeApprovalStatus();

	public String getGamiRequestNo(String surveyNo);

	public String getGamiBoardApproveStatus(String surveyNo);
}
