package lk.informatics.ntc.model.service;

import java.io.Serializable;
import java.util.List;

import lk.informatics.ntc.model.dto.GenerateReceiptDTO;
import lk.informatics.ntc.model.dto.NisiSeriyaDTO;
import lk.informatics.ntc.model.dto.RouteDTO;

public interface NisiSeriyaService extends Serializable {

	public String generateNisiSeriyaRequestNo();

	public List<RouteDTO> retrieveOriginList();

	public List<RouteDTO> retrieveDestinationList(String originCode);

	public List<RouteDTO> retrieveViaList(String originCode, String originCode2);

	public String insertDataIntoNt_m_nri_requester_info(NisiSeriyaDTO nisiSeriyaDTO, String loginUser);

	public List<GenerateReceiptDTO> getBankBranchDropDown(String bankCode);

	public boolean insertBankDetails(NisiSeriyaDTO nisiSeriyaDTO, String loginUser);

	public boolean deleteRecordFromNt_m_nri_requester_info(String serviceRefNo);

	public List<String> retrieveRequestNumbers();

	public List<String> retrieveServiceRefNumbers(String requestNo, String permitNum, String serviceAgreementNo);

	public List<String> retrievePermitNumbers(String requestedNo, String serviceRefNum, String serviceAgreementNo);

	public List<String> retrieveServiceAgreementNumbers(String requestedNo, String serviceRefNum, String permitNo);

	public List<NisiSeriyaDTO> searchDataFromNt_t_task_det(String requestedNo, String permitNo,
			String serviceAgreementNo, String serviceRefNo);

	public void updateDataIntoNt_m_nri_requester_info(NisiSeriyaDTO nisiSeriyaDTO, String loginUser);

	public String generateServiceAgreementNumber(String serviceRefNum, String loginUser);

	public boolean serviceConfirmationReject(String serviceRefNo, String rejectReason, String loginUser,
			String oldSerAgreementNum);

	public void approveServiceConfirmation(String serviceRefNo, String loginUser);

	public void rejectServiceConfirmation(String serviceRefNo, String loginUser);

	public boolean validatePermitNumberDuplicate(NisiSeriyaDTO nisiSeriyaDTO);

	public List<NisiSeriyaDTO> getTblGamiIssueLogSheetList(NisiSeriyaDTO nisiSeriyaDTO);

	public List<NisiSeriyaDTO> getDrpdRequestNoListForNsIssueLogSheets();

	public List<NisiSeriyaDTO> getDrpdServiceRefNoListForNsIssueLogSheets();

	public List<NisiSeriyaDTO> getDrpdServiceNoListForNsIssueLogSheets();

	public boolean checkServiceConfirmationAndRejectStatus(NisiSeriyaDTO dto);

	public boolean checkServiceApproveAndRejectStatus(NisiSeriyaDTO dto);

	public boolean checkServiceApproved(NisiSeriyaDTO dto);

	public void updateNumberGeneration(String code, String appNo, String loginUser);
}
