package lk.informatics.ntc.model.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lk.informatics.ntc.model.dto.CommitteeOrBoardApprovalDTO;
import lk.informatics.ntc.model.dto.TenderApplicantDTO;
import lk.informatics.ntc.model.dto.TenderDTO;

public interface TenderService {

	/* Create Tender methods */
	public List<TenderDTO> getTrafficProposeNoList();

	public List<TenderDTO> getRouteDetails(TenderDTO tenderDTO);

	public String getSurveyNo(TenderDTO tenderDTO);

	public boolean insertINTOTenderDetails(TenderDTO tenderDTO, String logUser, List<TenderDTO> advertismentList);

	public boolean insertINTOTenderManagement(TenderDTO tenderDTO, String logUser, String date);

	public String getLastSerialNo(TenderDTO dto);

	public String generateReferenceNo();

	public String getreceivefromcode(String code);

	public String getTitlefromcode(String code);

	public boolean updateTrafficProposalNo(TenderDTO dto, String logingUser);

	public boolean checkTaskDetailsInSurvey(TenderDTO dto, String taskCode, String status);

	public boolean insertTaskDetailsSurvey(TenderDTO dto, String loginUSer, String taskCode, String status);

	/* Issue Tender methods */

	public String generateApplicationNo();

	public boolean insertTaskDetails(TenderDTO dto, String loginUSer, String taskCode, String status);

	public boolean saveIssuteTenderDetails(TenderDTO tenderDTO, String applicationNo, String logUser, String queueNo);

	public boolean checkTaskDetails(TenderDTO dto, String taskCode, String status);

	public List<TenderDTO> getApplicationTypeList();

	public List<TenderDTO> getTenderRefNoList();

	public List<TenderDTO> getTitleList();

	public List<TenderDTO> getIdTypeList();

	public TenderDTO getTenderDetails(TenderDTO tenderDTO);

	public boolean isVoucherGenerated(String appNo);

	public boolean isPaymentDone(String appNo);

	public boolean isReciptGenerated(String appNo);

	public TenderDTO getApplicantDetails(TenderDTO tenderDTO);

	public TenderDTO fillApplicantDetails(TenderDTO tenderDTO);

	public String getTenderRefNo(TenderDTO tenderDTO);

	public boolean isVoucherGenerated(TenderDTO tenderDTO);

	public boolean alreadyGeneratedTenderApplication(TenderDTO tenderDTO);

	// PUblish Tender Management
	public List<TenderDTO> updatePostponeReason(String selectTenderDate, TenderDTO tenderDTO, String loginUser);

	public List<TenderDTO> showDataToGrid(TenderDTO tenderDTO);

	public String getApproveRejectStatus(String tenderRefNo, String newTenderDateString);

	// get approvestatus
	public String getApproveAgainstTenderRefNo(String tenderRefNo);

	public List<TenderDTO> updateApprovedData(TenderDTO tenderDTO, String Status, String strDate, String loginUser);

	// update publish closing date
	public List<TenderDTO> updateDates(TenderDTO tenderDTO, String loginuser);

	/* Added By Gayathra, 8.9.5.3 Refunding Non-Elected Bidders */

	public List<TenderDTO> getRefundableDetails(TenderDTO dto);

	/* Added By Gayathra, 8.9.5.3 Generate Refund Voucher */

	public List<TenderDTO> getAccountNo(TenderDTO dto);

	public List<TenderDTO> getTenderRefNoForRefund(TenderDTO dto);

	public List<TenderDTO> getRejectedApplicationNo(TenderDTO dto);

	public BigDecimal getAmount(TenderDTO dto);

	public boolean isAlreadyGenerateVoucher(TenderDTO dto);

	public String generateReferenceNoForRefund();

	public boolean generateVoucher(TenderDTO dto, String value, String logUser);

	public boolean updateTenderApplicant(TenderDTO dto);

	public TenderDTO getGeneratedData(TenderDTO dto);

	public boolean updateVoucherDetails(TenderDTO dto, String value, String logUser, LocalDateTime ldt);

	// get edit postpone date for publish tender managment
	TenderDTO getEditPostpone(TenderDTO tenderDTO);

	// update edit details for publish tender managment
	public List<TenderDTO> updateEditData(String selectTenderDate, String postponeReason, TenderDTO tenderDTO,
			String loginUser);

	// Print Agreement : by Mushtharq

	public List<TenderDTO> getTenderApplicationNoList();

	public TenderDTO getPrintDetails(String applicationNo);

	public boolean updatePrintAgreementData(TenderDTO tenderDto, String user);

	public TenderDTO checkedIsPrinted(String tenderAppNo, String tenderRefNo, String user);

	// print offer letter. Added by Gayathra

	public List<TenderDTO> getBoardApprovedTenderRefNoList();

	public List<TenderDTO> getrouteNoFillterdByTenderRefNo(TenderDTO tenderDTO);

	public TenderDTO fillRouteDetails(TenderDTO tenderDTO);

	public List<TenderDTO> getPrintOfferLetterDetails(TenderDTO tenderDTO);

	public List<CommitteeOrBoardApprovalDTO> getTenderApplicationNoForCommitteeApproval();

	public String getRefNo(String applicationNo);

	public String getFirstApprovalStatus(String applicationNo);

	public String getSecondApprovalStatus(String applicationNo);

	public String getBoardApprovalStatus(String applicationNo);

	public int insertFirstApproveStatus(String applicationNo, String remark, String user);

	public int insertSecondApproveStatus(String applicationNo, String remark, String user);

	public int insertBoardStatus(String applicationNo, String status, String remark, String user);

	List<String> getOnGoingReferenceNoList();

	List<String> getOnGoingApplicationNoList(String referenceNo);

	TenderApplicantDTO getDetailsByApplicationNo(String applicationNo);

	List<TenderApplicantDTO> getRouteNoList(String tenderRefNo);

	TenderApplicantDTO getDetailsByRouteNo(TenderApplicantDTO tenderApplicantDTO);

	boolean saveSecondEvelop(TenderApplicantDTO tenderApplicantDTO);

	boolean saveFirstEvelop(TenderApplicantDTO tenderApplicantDTO);

	TenderDTO getDetailsByReferenceNo(TenderDTO tenderDTO);

	List<TenderApplicantDTO> getSelectingApplications(String serialNo, String tenderRefNo);

	List<TenderApplicantDTO> getSelectedApplications(String tenderRefNo);

	boolean completeTender(String tenderRefNo);

	boolean addApplications(List<TenderApplicantDTO> temptSelectedApplicationList);

	public boolean removeSelectedApplication(String applicationNo);

	public boolean checkTenderEvalStatus(String tenderReferanceNo, String status);

	// print offer letter. Added by Gayathra

	public int getOfferLetterCount(TenderDTO dto);

	public boolean updateOfferLetterCountandApplicationNoStatus(TenderDTO dto, int count);

	public List<TenderDTO> checkingForTenderClosing(TenderDTO dto);

	public boolean closeTenderReferenceNo(TenderDTO dto);

	public boolean isTenderRefNoClosed(TenderDTO dto);

	public String getApplicationNoFromRefNo(String refNo);

	// view tender details

	public TenderDTO getDetails_tender_management(String tenderRefNo);

	public List<TenderDTO> getDetails_tender_details(String tenderRefNo);

	public List<CommitteeOrBoardApprovalDTO> getViewTenderApplicationNoForCommitteeApproval();

	public String getsecondCommitteeApprovalStatus(String applicationNo);

	public List<TenderDTO> getTenderRouteDetails(TenderDTO tenderDTO);

	// Print offer letter

	public List<TenderDTO> getDefaultPrintOfferLetterDetails(TenderDTO tenderDTO);

	public int insertTenderBoardStatus(String applicationNo);

	public List<String> getNonCompletedOnGoingApplicationNoList(String tenderRefNo);

}
