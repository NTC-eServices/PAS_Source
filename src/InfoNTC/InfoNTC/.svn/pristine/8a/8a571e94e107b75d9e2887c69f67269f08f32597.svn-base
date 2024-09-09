
package lk.informatics.ntc.model.service;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import lk.informatics.ntc.model.dto.GamiSeriyaDTO;
import lk.informatics.ntc.model.dto.LogSheetDTO;
import lk.informatics.ntc.model.dto.LogSheetMaintenanceDTO;
import lk.informatics.ntc.model.dto.NisiSeriyaDTO;
import lk.informatics.ntc.model.dto.SisuSeriyaDTO;
import lk.informatics.ntc.model.dto.SubSidyDTO;

public interface SisuSariyaService extends Serializable {

	// get request type for request for sisu sariya page

	public List<SisuSeriyaDTO> getRequestorTypeForDropDown();

	// get Prefer languages for Request for Sisusariya page
	public List<SisuSeriyaDTO> getPrefLanguForDropDown();

	// save data from Request Sisusariya page
	public boolean saveRequestSisusariData(SisuSeriyaDTO ssDTO, String loginUser);

	// update data from edit page
	public void updateRequestSisusariData(SisuSeriyaDTO ssDTO);

	// generate request number for Request For Sisusariya page
	public String generateSisiRequestNo();

	// save route information data from Request Sisusariya Page
	public boolean saveRouteInformationData(SisuSeriyaDTO ssDTO, String loginUser1);

	public void updateRouteInformationData(SisuSeriyaDTO ssDTO, String string1, String string2, String string3,
			String string4);

	// show route information data on grid
	public List<SisuSeriyaDTO> getDataonGrid(SisuSeriyaDTO ssDTO);

	// delete route information data on grid

	public SisuSeriyaDTO showData(SisuSeriyaDTO ssDTO);

	public boolean isReqNoExist(SisuSeriyaDTO ssDTO);

	public SisuSeriyaDTO showDataFirstTab(SisuSeriyaDTO ssDTO);

	// delete route information data on grid
	public void deleteDataFromGrid(SisuSeriyaDTO ssDTO, String string1, String string2, String string3, String string4);

	/* For Sisu Seriya Service Authorization / Print Service Agreement */
	public boolean isAlreadyChecked(SisuSeriyaDTO dto);

	public boolean isAlreadyRecomended(SisuSeriyaDTO dto);

	public boolean isAlreadyApproved(SisuSeriyaDTO dto);

	public boolean isAlreadyRejected(SisuSeriyaDTO dto);

	// View Edit Request sisusariya page
	// get request number list for drop down
	public List<SisuSeriyaDTO> getRequesNoDropDown();

	public List<SisuSeriyaDTO> getOperatorDepoNameDropDown();

	public List<SisuSeriyaDTO> getOperatorDepoNameDropDownForRenewal();

	public List<SisuSeriyaDTO> getOperatorDepoNameDropDownForSisuApproval();

	public List<SisuSeriyaDTO> getOperatorDepoNameListForCommonInquiry(SisuSeriyaDTO dto);

	public List<SisuSeriyaDTO> getOperatorDepoNameListForVoucherCreation();

	/* For Sisu Seriya Authorization */
	public List<SisuSeriyaDTO> getRequestNo();

	public List<SisuSeriyaDTO> getServiceRefNoListForSisuApproval(SisuSeriyaDTO dto);

	public List<SisuSeriyaDTO> getServiceRefNoListForCommonInquiry(SisuSeriyaDTO dto);

	public List<SisuSeriyaDTO> searchDataForCommonInquiry(SisuSeriyaDTO dto);

	public List<SisuSeriyaDTO> getSisuSeriyaData(SisuSeriyaDTO dto);

	public boolean approveSisuSeriyaRequest(SisuSeriyaDTO selectDTO, String modifyBy, String servicesNo);

	public boolean checkSisuSariyaRequest(SisuSeriyaDTO selectDTO, String modifyBy);

	public boolean recommendSisuSariyaRequest(SisuSeriyaDTO selectDTO, String modifyBy);

	public String getRequestNoByServiceNo(String serviceRefNo, String serviceNo);

	public boolean rejectCheckBySisuSeriyaRequest(SisuSeriyaDTO selectDTO, String rejectedBy);

	public boolean rejectRecommendSisuSeriyaRequest(SisuSeriyaDTO selectDTO, String rejectedBy);

	public boolean rejectSisuSeriyaRequest(SisuSeriyaDTO selectDTO, String rejectedBy, String rejectReason);

	public List<SisuSeriyaDTO> getDefaultSisuSeriyaDataForSisuApproval(String status);

	public String generateServiceNo();

	public List<SisuSeriyaDTO> getServiceNoList();

	public List<SisuSeriyaDTO> getServiceNoListForCommonInquiry(SisuSeriyaDTO dto);

	public SisuSeriyaDTO retrieveSchoolInfoFromRequest(SisuSeriyaDTO sisuSeriyaDTO);

	public List<SisuSeriyaDTO> getApprovedRequestNo();

	public List<SisuSeriyaDTO> getRequestNoListForCommonInquiry(SisuSeriyaDTO dto);

	public List<SisuSeriyaDTO> getApprovedOperatorDepoNameList();

	public List<SisuSeriyaDTO> getApprovedBusNoList();

	public List<SisuSeriyaDTO> getBusNoListForCommonInquiry(SisuSeriyaDTO dto);

	public List<SisuSeriyaDTO> getApprovedServiceRefNoList();

	public List<SisuSeriyaDTO> getApprovedServiceRefNoListForTeam();

	public List<SisuSeriyaDTO> getApprovedOperatorDepoNameListForTeam();

	public List<SisuSeriyaDTO> getApprovedServiceNoList();

	public List<SisuSeriyaDTO> getApprovedSisuSeriyaData(SisuSeriyaDTO dto);

	public boolean generateLogSheetRef(SisuSeriyaDTO sisuSeriyaDTO, String logSheetYear, int logSheetCopies,
			String loginUser, List<LogSheetDTO> newLogSheetList);

	public List<LogSheetDTO> getLogSheetsByServiceRefNo(String serviceRefNo);

	public void updateIssueServiceAgreementPermitStickerLogSheets(SisuSeriyaDTO selectDTO);

	public List<LogSheetDTO> getLogSheetsByServiceRefNoAndYear(String serviceRefNo, String logSheetYear);

	public List<LogSheetDTO> getLogSheetsByServiceRefNoYearNameOfOperatorBusNo(String serviceRefNo, String logSheetYear,
			String nameOfOperator, String busNo, String serviceTypeCode);

	public List<SisuSeriyaDTO> getSisuSeriyaToIssue();

	public List<SisuSeriyaDTO> getSisuSeriyaToIssueBySearch(SisuSeriyaDTO dto);

	public List<LogSheetMaintenanceDTO> getSisuSeriyaForBulkPayment();

	public List<LogSheetMaintenanceDTO> getSisuSeriyaForBulkPaymentBySearch(SisuSeriyaDTO dto, String year);

	public List<SisuSeriyaDTO> serviceTypeDropDown();

	public String bulkPaymentGenerateVoucher(SubSidyDTO subSidyDTO, List<LogSheetMaintenanceDTO> selectedLogSheetList,
			String loginUser);

	public List<SisuSeriyaDTO> getDefaultValuesForRefNo();

	public List<SisuSeriyaDTO> getRefNoList(Date renewalRequestStartDate, Date renewalRequestEndDate);

	public List<SisuSeriyaDTO> getServiceNoList(Date requestStartDate, Date requestEndDate);

	public String getCurrentServiceNo(String selectedServiceRefNo);

	public String getCurrentRefNo(String selectedServiceAgreementNo);

	public List<SisuSeriyaDTO> getServiceTypeForDropdown();

	public boolean isTaskDone(String value, String taskCode, String status);

	public boolean isServiceNoFound(String value);

	public List<SisuSeriyaDTO> getVoucherPendingRequestNo();

	public List<SisuSeriyaDTO> getVoucherPendingAndVoucherCreatedRequestNo();

	public List<SisuSeriyaDTO> getVoucherPendingServiceRefNoList();

	public List<SisuSeriyaDTO> getFilterdVoucherPendingServiceRefNoList(String reqNo);// added

	public List<SisuSeriyaDTO> getVoucherPendingServiceNoList();

	public SisuSeriyaDTO getVoucherPendingServiceNo(String serviceRefNo);// added

	public boolean checkSisuSariyaRenewal(SisuSeriyaDTO dto, String checkedBy);

	public boolean recommendSisuSariyaRenewal(SisuSeriyaDTO dto, String approvedBy);

	public boolean renewalApprove(SisuSeriyaDTO selectDTO, String loginUser);

	public List<SisuSeriyaDTO> getListForSelectedRefNoOrServiceNo(String selectedServiceRefNo,
			String selectedServiceAgreementNo);

	public List<SisuSeriyaDTO> getListForSelectedRefNoOrServiceNoAfterApproved(String selectedServiceRefNo);

	public boolean renewalReject(SisuSeriyaDTO selectDTO, String loginUser, String rejectReasonVal);

	public boolean rejectCheckBySisuSeriyaRenewal(SisuSeriyaDTO selectDTO, String rejectedBy);

	public boolean rejectRecommendSisuSeriyaRenewal(SisuSeriyaDTO selectDTO, String rejectedBy);

	public SisuSeriyaDTO getSearchedServiceInfo(String serviceRefNo, String serviceNo, String requestNo);

	public List<SisuSeriyaDTO> getDefaultSisuSariyaVoucherData();

	/*
	 * Method use for get only SS006 and SM001 complete data at voucher
	 * searching.NEVER USE
	 **/
	public List<SisuSeriyaDTO> getSisuSariyaVoucherData(String serviceNo, SisuSeriyaDTO dto);

	// added for Team Maintenance
	public SisuSeriyaDTO fillSerrviceNO(SisuSeriyaDTO sisuSariyaTeamAuthDTO);

	public List<SisuSeriyaDTO> showSearchedDataForAuthorizationTeamMaintenance(Date date1, Date date2,
			String serviceRefNo, String serviceNo, String nameOfOperator);

	public void saveTeamInformationData(SisuSeriyaDTO ssTeamDTO, String loginUser1);

	public List<SisuSeriyaDTO> getTeamMainteanDataonGrid(SisuSeriyaDTO ssTeamDTO);

	public List<SisuSeriyaDTO> getServiceRefNoList(SisuSeriyaDTO sisuSeriyaDTO);

	public void deleteTeamAuthData(SisuSeriyaDTO ssDTO, String string1, String string2, String string3, String string4);

	public void updateTeamInformationData(SisuSeriyaDTO ssTeamDTO, String loginUser1, String string1, String string2,
			String string3);

	public List<SisuSeriyaDTO> getServiceNoList1();

	// for Team Maintenance
	public String getProvincesDescription(String provinceCode);

	public String getDistrictDescription(String districtCode, String provinceCode);

	public boolean isServiceDataEnterd(SisuSeriyaDTO dto);

	public List<SisuSeriyaDTO> getSelectSisuSariyaData(String serviceNo, SisuSeriyaDTO dto);

	public String getDivisionSectionDescription(String districtCode, String provinceCode, String divisionalSecCode);

	public String getServiceTypeDescription(String serviceTypeCode);

	public SisuSeriyaDTO getSearchedSchoolInfo(String serviceRefNo, String serviceNo, String requestNo);

	public List<SisuSeriyaDTO> getBankList();

	public List<SisuSeriyaDTO> getBranchesForBanksList(String bankCode);

	public int updateBankInfoDTO(SisuSeriyaDTO bankInfoDTO, String loginUser, String selectedServiceNoInGrid,
			String selectedRefNoInGrid);

	public SisuSeriyaDTO getBankDetDTO(String selectedServiceNoInGrid, String selectedRefNoInGrid);

	public List<SisuSeriyaDTO> drpdOriginList(String requestNo);

	public List<SisuSeriyaDTO> drpdDestinationList(String requestNo);

	public List<SisuSeriyaDTO> drpdDistrictList(String requestNo);

	public String generateServiceReferenceNo();

	public SisuSeriyaDTO insertServiceInformation(SisuSeriyaDTO sisuSeriyaDTO, String loginUser);

	public boolean insertSchoolInformation(SisuSeriyaDTO sisuSeriyaDTO, String loginUser);

	public boolean insertBankDetails(SisuSeriyaDTO sisuSeriyaDTO, String loginUser);

	public List<SisuSeriyaDTO> getTblPermitHolderInfo(SisuSeriyaDTO sisuSeriyaDTO);

	public SisuSeriyaDTO getServiceInformationByServiceRefNo(String requestNo);

	public boolean updateServiceInformationByServiceRefNo(SisuSeriyaDTO sisuSeriyaDTO);

	public SisuSeriyaDTO getSchoolInformationByServiceRefNo(SisuSeriyaDTO sisuSeriyaDTO);

	// update school information
	public boolean updateSchoolInformationByRequestNo(SisuSeriyaDTO sisuSeriyaDTO);

	public List<SisuSeriyaDTO> drpdRequestNoList(SisuSeriyaDTO sisuSeriyaDTO);

	public List<SisuSeriyaDTO> drpdOperatorDepoNameList(SisuSeriyaDTO sisuSeriyaDTO);

	public List<SisuSeriyaDTO> getOperatorDepoNameDropDownForSisuApproval(SisuSeriyaDTO sisuSeriyaDTO);

	public List<SisuSeriyaDTO> drpdOperatorDepoNameListForRenewal(String startDate, String endDate);

	public List<SisuSeriyaDTO> getRefNoListForRenewal(String startDate, String endDate, String operator);

	public List<SisuSeriyaDTO> getServiceNoListForRenewal(String startDate, String endDate, String operator);

	public List<SisuSeriyaDTO> drpdOperatorDepoNameListByRequestNoServiceRefNo(SisuSeriyaDTO sisuSeriyaDTO);

	public boolean updateRenewalStatus(String status, String serviceRefNo);

	// permit renewal

	public SisuSeriyaDTO insertRenewalServiceInformation(SisuSeriyaDTO sisuSeriyaDTO, String loginUser);

	public boolean updateRenewalServiceInformation(SisuSeriyaDTO sisuSeriyaDTO, String loginUser);

	public SisuSeriyaDTO getSchoolInformationByRequestNo(SisuSeriyaDTO sisuSeriyaDTO);

	public List<SisuSeriyaDTO> getRenewalServiceNoList();

	public List<SisuSeriyaDTO> getRenewalServiceNoList(SisuSeriyaDTO sisuSeriyaDTO);

	public List<SisuSeriyaDTO> getRenewalRefNoList();

	public boolean updateSchoolInformationByServiceRefNo(SisuSeriyaDTO sisuSeriyaDTO, String loginUser);

	public boolean updateSchoolInformationByServiceRefNo(String PreServiceRefNo, String newServiceRefNo);

	public boolean insertRenewalServiceToHistory(SisuSeriyaDTO sisuSeriyaDTO, String loginUser);

	public boolean insertSchoolInformationToHistory(SisuSeriyaDTO sisuSeriyaDTO, String loginUser);

	public boolean saveSchoolInformationToHistory(SisuSeriyaDTO sisuSeriyaDTO, String refNo, String loginUser);

	public List<SisuSeriyaDTO> drpdViaList(SisuSeriyaDTO sisuSeriyaDTO);

	public List<SisuSeriyaDTO> drpdDestinationListByOrigin(SisuSeriyaDTO sisuSeriyaDTO);

	public List<SisuSeriyaDTO> drpdOriginListByDestination(SisuSeriyaDTO sisuSeriyaDTO);

	public boolean updateServiceStartEndDateInApproval(String serviceReferenceNo, Date startDate, Date endDate);

	public boolean updatePermitHolderServiceInfoStatus(String status, String serviceRefNo);

	public List<SisuSeriyaDTO> getApprovedRequestNoForEditMode();

	public List<SisuSeriyaDTO> getApprovedServiceRefNoListForEditMode();

	public List<SisuSeriyaDTO> getApprovedServiceRefNoListForEditMode(SisuSeriyaDTO dto);

	public List<SisuSeriyaDTO> getApprovedServiceNoListForEditMode();

	public List<SisuSeriyaDTO> getOperatorDepoNameListForIssueLogSheets(String serviceTypeCode);

	public List<SisuSeriyaDTO> getBusNoListForIssueLogSheets(String serviceTypeCode);

	public List<SisuSeriyaDTO> getSisuSeriyaToIssueBySearchForEditMode(SisuSeriyaDTO sisuSeriyaDTO);

	public List<SisuSeriyaDTO> getSisuSeriyaToIssueForEditMode();

	public void updateIssueServiceAgreementPermitStickerLogSheetsForEditMode(SisuSeriyaDTO selectDTO);

	public void insetOldRecordIntoHis(SisuSeriyaDTO selectDTO);

	public void updateLogSheetValues(int logSheetSeqNo, String logSheetRefNo, String userName);

	public SisuSeriyaDTO getServiceDates(String serviceReferenceNo);

	public boolean generateLogSheetRefUpadte(SisuSeriyaDTO sisuSeriyaDTO, String logSheetYear, int logSheetCopies,
			String loginUser, List<LogSheetDTO> newLogSheetList, String serviceCode);

	public List<GamiSeriyaDTO> getApprovedGamiSeriyaRequestNo();

	public List<GamiSeriyaDTO> getApprovedGamiSeriyaServiceRefNoList();

	public List<GamiSeriyaDTO> getApprovedGamiSeriyaServiceNoList();

	public List<GamiSeriyaDTO> getGamiSeriyaToIssue();

	public List<GamiSeriyaDTO> getGamiSeriyaToIssueBySearch(GamiSeriyaDTO gamiSeriyaDTO);

	public void updateIssueServiceAgreementPermitStickerLogSheetsGamiSeriya(GamiSeriyaDTO selectDTO);

	public boolean generateLogSheetRefGamiSeriya(GamiSeriyaDTO selectDTO, String logSheetYear, int logSheetCopies,
			String loginUser, List<LogSheetDTO> newLogSheetList);

	public List<GamiSeriyaDTO> getApprovedTenderNoGamiSeriyaForEditMode();

	public List<GamiSeriyaDTO> getApprovedServiceRefNoListGamiSeriyaForEditMode();

	public List<GamiSeriyaDTO> getApprovedServiceNoListGamiSeriyaForEditMode();

	public List<GamiSeriyaDTO> getGamiSeriyaToIssueForEditMode();

	public List<GamiSeriyaDTO> getGamiSeriyaToIssueBySearchForEditMode(GamiSeriyaDTO gamiSeriyaDTO);

	public void insetOldRecordIntoGamiSeriyaInfoHis(GamiSeriyaDTO selectDTO);

	public void updateIssueServiceAgreementPermitStickerLogSheetsForGamiSeriyaEditMode(GamiSeriyaDTO selectDTO);

	public List<NisiSeriyaDTO> getApprovedRequestNoForNisiSeriya();

	public List<NisiSeriyaDTO> getApprovedServiceRefNoListForNisiSeriya();

	public List<NisiSeriyaDTO> getApprovedServiceNoListForNisiSeriya();

	public List<NisiSeriyaDTO> getNisiSeriyaToIssue();

	public List<NisiSeriyaDTO> getNisiSeriyaToIssueBySearch(NisiSeriyaDTO nisiSeriyaDTO);

	public void updateIssueServiceAgreementPermitStickerLogSheetsForNisiSeriya(NisiSeriyaDTO selectDTO);

	public boolean generateLogSheetRefNisiSeriya(NisiSeriyaDTO selectDTO, String logSheetYear, int logSheetCopies,
			String loginUser, List<LogSheetDTO> newLogSheetList);

	public List<NisiSeriyaDTO> getApprovedRequestNoForEditModeNisiSeriya();

	public List<NisiSeriyaDTO> getApprovedServiceRefNoListForEditModeNisiSeriya();

	public List<NisiSeriyaDTO> getApprovedServiceNoListForEditModeNisiSeriya();

	public List<NisiSeriyaDTO> getSisuSeriyaToIssueForEditModeNisiSeriya();

	public List<NisiSeriyaDTO> getSisuSeriyaToIssueBySearchForEditModeNisiSeriya(NisiSeriyaDTO nisiSeriyaDTO);

	public void updateIssueServiceAgreementPermitStickerLogSheetsForEditModeNisiSeriya(NisiSeriyaDTO selectDTO);

	public List<SisuSeriyaDTO> getApprovedServiceRefNoListGamiSeriyaForEditModeIssueLogSheet();

	public List<SisuSeriyaDTO> getApprovedServiceRefNoListGamiSeriyaForEditModeIssueLogSheet(SisuSeriyaDTO dto);

	public List<SisuSeriyaDTO> getApprovedServiceRefNoListForEditModeNisiSeriyaForIssueLogSheet();

	public List<SisuSeriyaDTO> getApprovedServiceRefNoListForEditModeNisiSeriyaForIssueLogSheet(SisuSeriyaDTO dto);

	public List<SisuSeriyaDTO> getViaList();

	public List<SisuSeriyaDTO> getRouteDetailsForVia(String selectedViaValue);

	public List<SisuSeriyaDTO> getVehicleNoList();

	public List<SisuSeriyaDTO> getOwnerNameList();

	public List<SisuSeriyaDTO> getServiceNoListForSelectedBusNo(String selectedVehicleNo);

	public List<SisuSeriyaDTO> getServiceNoListForSelectedOwnerName(String selectedOwnerName);

	public List<SisuSeriyaDTO> getServiceDataListWithServiceNo(String selectedVehicleNo, String selectedServiceNo);

	public List<SisuSeriyaDTO> getServiceDataListWithBusNo(String selectedVehicleNo);

	public List<SisuSeriyaDTO> getServiceDataListWithServiceNoAndOwnerName(String selectedOwnerName,
			String selectedServiceNo);

	public List<SisuSeriyaDTO> getServiceDataListWithOwnerName(String selectedOwnerName);

	public boolean updateServiceInformationByServiceRefNoEdit(SisuSeriyaDTO sisuSeriyaDTO, String user);

	/** for subsidy Information Page **/
	public List<SisuSeriyaDTO> getdropDownDataByService(String serviceType);

	public List<SisuSeriyaDTO> getReqNoByService(String serviceType);

	public List<SisuSeriyaDTO> getRefNoByService(String serviceType);

	public List<SisuSeriyaDTO> getAgreeNoByService(String serviceType);

	public List<SisuSeriyaDTO> getVehiNumByService(String serviceType);

	public List<SisuSeriyaDTO> getNamesByService(String serviceType);

	public List<SisuSeriyaDTO> getSearchedData(SisuSeriyaDTO ssDTO);

	public String generateServiceNoN(String loginUser, String type);

	public String retrieveLastNoForNumberGeneration(String code);

	public void updateNumberGeneration(String code, String appNo, String loginUser);

	public boolean updateSchoolInformationByServiceRefNoNew(SisuSeriyaDTO sisuSeriyaDTO, String user);

	public boolean insertBankDetailsNew(SisuSeriyaDTO sisuSeriyaDTO, String loginUser);

	public SisuSeriyaDTO retrieveSchoolInfoFromRequestNew(SisuSeriyaDTO sisuSeriyaDTO);

	public List<SisuSeriyaDTO> getRequesNoDropDownNew();

	public List<SisuSeriyaDTO> drpdRequestNoListNew(SisuSeriyaDTO sisuSeriyaDTO);

	public boolean checkSchoolInfo(SisuSeriyaDTO sisuSeriyaDTO);

	public List<SisuSeriyaDTO> getSchoolHistoryDeatils(SisuSeriyaDTO sisuSeriyaDTO);

	public List<SisuSeriyaDTO> getServiceHistoryDeatils(SisuSeriyaDTO sisuSeriyaDTO);

	public List<LogSheetDTO> getLogSheetsByServiceRefNoYear(String serviceRefNo, String year);

	public void updateTaskStatusOngoingSubsidyTaskTable(String requestNo, String serviceNo, String referenceNo,
			String currTask, String status, String loggedUser);

	public void updateTaskCompleteSubsidyTaskTableForPrintAgreement(String requestNo, String serviceRefNo,
			String serviceNo, String currTask, String status, String loginUser) throws SQLException;

	public List<SisuSeriyaDTO> getRenewalServiceReferenceNoList(SisuSeriyaDTO sisuSeriyaDTO);
	String getBranchCodeByRequestNo(String requestNo);
	String getServiceRefNoByServiceNo(String serviceNo); // newly should remove
	List<SisuSeriyaDTO> getServiceNoListForRenewalNew();
	List<SisuSeriyaDTO> getListForSelectedServiceAgreementNo(String selectedServiceAgreementNo);
	List<SisuSeriyaDTO> getServiceNoListForIssueLogSheet();
	List<SisuSeriyaDTO> getServiceRefNumberByServiceNo(SisuSeriyaDTO ssDto);
	
	boolean getRenewalStatusByRequestNo(String requestNo);

	boolean checkAlreadyHave(String serviceRefNo, String year, String serviceType);

	long saveToLogSheets(String serviceRefNo, String logSheetYear, int logSheetCopies, String loginUser,
			String serviceType);

	List<SisuSeriyaDTO> getApprovedServiceRefNo();

	List<SisuSeriyaDTO> getApprovedServiceRefNoListGamiSeriyaLogSheet();

	List<String> pendingReferenceNumberList(String refNum);

	// Added dhananjika.d 16/08/2024
	String isRefNoHasExpired(String refNum);
}
