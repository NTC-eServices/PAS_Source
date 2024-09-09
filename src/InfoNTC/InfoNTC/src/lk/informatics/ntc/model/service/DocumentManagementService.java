package lk.informatics.ntc.model.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lk.informatics.ntc.model.dto.DocumentManagementDTO;
import lk.informatics.ntc.model.dto.RevenueCollectionDTO;

public interface DocumentManagementService extends Serializable {
	public List<String> GetTrasactionType(String typeCode);

	public List<String> GetDocCode();

	public String getDescriptionWithCode(DocumentManagementDTO documentManagement);

	public int saveDocumentTypes(DocumentManagementDTO documentManagementDTO);

	public int updateNewDocTypes(DocumentManagementDTO documentManagementEdit, String user);

	public int DeleteDoc(DocumentManagementDTO documentManagementDelete);

	public List<DocumentManagementDTO> serachAllDetailsForDoc(String typeCode, String doc_Code, boolean response,
			boolean mandatory, String type);

	public List<DocumentManagementDTO> mandatoryDocs(String typeCode, String uploadPermitNo);

	public List<DocumentManagementDTO> optionalDocs(String typeCode, String uploadPermitNo);

	public String getApplicationNoWithPermit(String uploadPermit);

	public String getPermitNoWithApplication(String uploadApplication);

	public int saveaddnewDocument(String newDocCode, String newDocDes, String createdBy);

	public int saveMandatoryUploads(String filePath, String doc_des, String doc_code, String permitNo,
			String applicationNo, String transaction_Code, String user);

	public String getTransactionCode(String transaction);

	public String getFilePath(String filePath, String doc_des, String permitNo, String transaction_Code,
			String doc_code, String appNo);

	public int replaceDoc(String doc_des, String permitNo, String transaction_Code, String user, String docuString);

	public String getTransactionCodeForAddDocument(String type);

	public int saveOptionalUploads(String filePath, String doc_des, String doc_code, String permitNo,
			String applicationNo, String transaction_Code, String user);

	public String getOptionalFilePath(String filePath, String doc_des, String permitNo, String transaction_Code,
			String appNo);

	public int replaceOptionalDoc(String doc_des, String permitNo, String transaction_Code, String user,
			String docuString);

	public int DeleteOptionalDoc(String permitNo, String applicationNo, String transaction_Code, String docCode,
			String path);

	public List<DocumentManagementDTO> mandatoryViewDocs(String typeCode, String uploadPermitNo);

	public List<DocumentManagementDTO> check(DocumentManagementDTO documentManagement, String typeCode);

	public List<DocumentManagementDTO> optionalViewDocs(String typeCode, String uploadPermitNo);

	public boolean getResponse(String doc_Code, String typeCode, boolean doc_Mandatory);

	public List<DocumentManagementDTO> getPermitHolderInfo(String applicationNo,
			DocumentManagementDTO documentPermitInfo);

	public int saveAlertMessage(DocumentManagementDTO documentPermitInfo, String alertMessage);

	public int getSelectedRow(Long seq, DocumentManagementDTO documentManagement);

	public int getDocumentDetailsCode(DocumentManagementDTO documentManagement, String newDocCode, String newDocDes);

	public int getDocumentDetailsDes(DocumentManagementDTO documentManagement, String newDocCode, String newDocDes);

	public int updateAddNewDocument(String documentCode);

	public int DeleteTemp();

	public String checkDocCode(String docCode);

	public int DeleteTempInDelete(String docCode);

	public String checkDocCodeWhenUploading(String documentCode);

	public int saveaddnewDocumentWhenUploading(String code, String description, String user);

	public List<DocumentManagementDTO> mandatoryDocsForUserManagement(String typeCode, String empNo);

	public List<DocumentManagementDTO> optionalDocsForUserManagement(String typeCode, String empNo);

	public int saveUserManagementMandatoryUploads(String filePath, String doc_des, String doc_code, String empNo,
			String transaction_Code, String user);

	public String getUserManagementFilePath(String filePath, String doc_des, String empNo, String transaction_Code);

	public int replaceUserManagementDoc(String doc_des, String empNo, String transaction_Code, String user,
			String docuString);

	public List<DocumentManagementDTO> getEmployeeInfo(String empNo, DocumentManagementDTO documentPermitInfo);

	public String getTitleName(String titleNo);

	public int replaceOptionalUserManagementDoc(String doc_des, String empNo, String transaction_Code, String user,
			String docuString);

	public int saveUserManagementOptionalUploads(String filePath, String doc_des, String doc_code, String empNo,
			String transaction_Code, String user);

	public int getVersionNumber(DocumentManagementDTO uploaddocumentManagementDTO, String typeCodeTrans,
			String docType);

	public int saveMandatoryUploadsVersions(String filePath, String doc_des, String doc_code, String permitNo,
			String applicationNo, String transaction_Code, String user, int versionNo);

	public int saveOptionalUploadsVersions(String filePath, String doc_des, String doc_code, String permitNo,
			String applicationNo, String transaction_Code, String user, int versionNo);

	public int saveUserManagementMandatoryUploadsVersions(String transaction_Code, String empNo, String doc_code,
			String doc_des, String docuString, String user, int versionNo);

	public int getUserManagementVersionNumber(String empNo, String typeCodeTrans, String docType, String doc_code);

	public int getVersionNumberForDriverConductor(DocumentManagementDTO uploaddocumentManagementDTO,
			String typeCodeTrans, String docType);

	public int saveMandatoryUploadsVersionsForDriverConductor(String filePath, String doc_des, String doc_code,
			String permitNo, String applicationNo, String transaction_Code, String user, int versionNo);

	public int saveMandatoryUploadsForDriverConductor(String filePath, String doc_des, String doc_code, String permitNo,
			String applicationNo, String transaction_Code, String user);

	public String getOptionalFilePathForDriverConductor(String filePath, String doc_des, String permitNo,
			String transaction_Code, String doc_code, String appNo);

	public int replaceOptionalDocForDriverConductor(String doc_des, String permitNo, String transaction_Code,
			String user, String docuString);

	public int saveOptionalUploadsVersionsForDriverConductor(String filePath, String doc_des, String doc_code,
			String permitNo, String applicationNo, String transaction_Code, String user, int versionNo);

	public int saveOptionalUploadsForDriverConductor(String filePath, String doc_des, String doc_code, String permitNo,
			String applicationNo, String transaction_Code, String user);

	public List<DocumentManagementDTO> driverConductorOptionalListM(String driverConductorId,
			String strTransactionType);

	public List<DocumentManagementDTO> driverConductorMandatoryListM(String driverConductorId,
			String strTransactionType);

	public List<DocumentManagementDTO> optionalDocsFordriverConductor(String typeCode, String driverConductorId);

	public List<DocumentManagementDTO> mandatoryDocsFordriverConductor(String typeCode, String driverConductorId);

	public List<DocumentManagementDTO> driverConductorOptionalList(String requestNo, String driverConductorId);

	public List<DocumentManagementDTO> driverConductorMandatoryList(String requestNo, String driverConductorId);

	public String getFilePathForDriverConductor(String filePath, String doc_des, String permitNo,
			String transaction_Code, String doc_code, String appNo);

	public int replaceDocForDriverConductor(String doc_des, String permitNo, String transaction_Code, String user,
			String docuString, String refNo);

	public int saveUserManagementOptionalUploadsVersions(String transaction_Code, String empNo, String doc_code,
			String doc_des, String docuString, String user, int versionNo);

	public List<DocumentManagementDTO> ViewHistoryList(DocumentManagementDTO viewDocumentHistory);

	public int DeleteVersionDoc(String path);

	public String getuploadfilepath(DocumentManagementDTO viewDocumentHistory);

	public List<String> GetAddDocumentTrasactionType();

	public List<DocumentManagementDTO> userManagementMandatoryDocs(String typeCode, String uploadEmpNo);

	public List<DocumentManagementDTO> userManagementOptionalDocs(String typeCode, String uploadEmpNo);

	public int getVersionNumberForUserManagement(DocumentManagementDTO uploaddocumentManagementDTO,
			String typeCodeTrans, String docType);

	public List<DocumentManagementDTO> mandatoryUserManagementViewDocs(String typeCode, String uploadEmpNo);

	public List<DocumentManagementDTO> optionalUserManagementViewDocs(String typeCode, String uploadEmpNo);

	public String getUserManagementuploadfilepath(DocumentManagementDTO viewUserManagementDocumentHistory);

	public List<DocumentManagementDTO> ViewUserManagementHistoryList(
			DocumentManagementDTO viewUserManagementDocumentHistory);

	public int DeleteUserManagementVersionDoc(String empNo, String transaction_Code, String docDes, int versionNo);

	public List<DocumentManagementDTO> FilePathList(String ApplicationNo);

	public int updateFilePaths(List<DocumentManagementDTO> newFilePathList, String showpermitNo, String user);

	public List<DocumentManagementDTO> FilePathVersionList(String ApplicationNo);

	public int updateVersionFilePaths(List<DocumentManagementDTO> newFilePathVersionList, String showpermitNo,
			String user);

	public String getAddNewDocFilePath(String filePath, String doc_des, String permitNo, String transaction_Code);

	public String getAddNewDocUserManagementFilePath(String filePath, String doc_des, String empNo,
			String transaction_Code);

	public List<String> GetDocDescription();

	public String getCodeWithDescription(DocumentManagementDTO documentManagement);

	public List<DocumentManagementDTO> newPermitMandatoryList(String permitNo);

	public List<DocumentManagementDTO> newPermitOptionalList(String permitNo);

	public List<DocumentManagementDTO> permitRenewalMandatoryList(String permitNo);

	public List<DocumentManagementDTO> permitRenewalOptionalList(String permitNo);

	public List<DocumentManagementDTO> backlogManagementOptionalList(String permitNo);

	public List<DocumentManagementDTO> amendmentToBusOwnerMandatoryList(String permitNo);

	public List<DocumentManagementDTO> amendmentToBusOwnerOptionalList(String permitNo);

	public List<DocumentManagementDTO> amendmentToBusMandatoryList(String permitNo);

	public List<DocumentManagementDTO> amendmentToBusOptionalList(String permitNo);

	public List<DocumentManagementDTO> amendmentToServiceMandatoryList(String permitNo);

	public List<DocumentManagementDTO> amendmentToServiceOptionalList(String permitNo);

	public List<DocumentManagementDTO> amendmentToOwnerBusMandatoryList(String permitNo);

	public List<DocumentManagementDTO> amendmentToOwnerBusOptionalList(String permitNo);

	public List<DocumentManagementDTO> amendmentToServiceBusMandatoryList(String permitNo);

	public List<DocumentManagementDTO> amendmentToServiceBusOptionalList(String permitNo);

	public List<DocumentManagementDTO> surveyMandatoryList(String surveyNo);

	public List<DocumentManagementDTO> surveyOptionalList(String surveyNo);

	public List<DocumentManagementDTO> tenderMandatoryList(String permitNo);

	public List<DocumentManagementDTO> tenderOptionalList(String permitNo);

	public List<DocumentManagementDTO> viewAmendmentsOptionalList(String permitNo, String transactionTypeCode);

	public List<DocumentManagementDTO> viewAmendmentsMandatoryList(String permitNo, String transactionTypeCode);

	public List<String> GetApplicationNoListForNewPermit();

	public List<String> GetApplicationNoListForPermitRenewal();

	public List<String> GetPermitNoListForNewPermit();

	public List<String> GetPermitNoListForAmendments(String transCode);

	public List<String> GetApplicationNoListForAmendments(String transCode);

	public List<String> GetSurveyNoListForSurvey();

	public List<String> GetSurveyRequestNoListForSurvey();

	public List<String> GetTenderApplicationNoListForTender();

	public String getApplicationNoWithPermitForAmendment(String uploadPermit);

	public String getSurveyRequestNoWithSurveyNoforSurvey(String uploadPermit);

	public String getPermitNoWithApplicationforAmendments(String uploadApplication);

	public String getSurveyNoWithRequestNoforSurvey(String uploadApplication);

	public List<DocumentManagementDTO> mandatoryDocsForTender(String typeCode, String uploadPermitNo);

	public List<DocumentManagementDTO> optionalDocsForTender(String typeCode, String uploadPermitNo);

	public String getApplicationNoWithPermitRenewal(String uploadPermit);

	public int getVersionNumberForSisuSariya(DocumentManagementDTO uploaddocumentManagementDTO, String typeCodeTrans,
			String docType);

	public String getFilePathForSisuSariya(String filePath, String doc_des, String permitNo, String transaction_Code,
			String doc_code, String appNo);

	public int replaceDocForSisuSariya(String doc_des, String permitNo, String transaction_Code, String user,
			String docuString, Date expiryDate);

	public int saveMandatoryUploadsVersionsForSisuSariya(String filePath, String doc_des, String doc_code,
			String permitNo, String applicationNo, String transaction_Code, String user, int versionNo,
			Date expiryDate);

	public int saveMandatoryUploadsForSisuSariya(String filePath, String doc_des, String doc_code, String permitNo,
			String applicationNo, String transaction_Code, String user, Date expiryDate);

	public String getOptionalFilePathForSisuSariya(String filePath, String doc_des, String permitNo,
			String transaction_Code, String doc_code, String appNo);

	public int replaceOptionalDocForSisuSariya(String doc_des, String permitNo, String transaction_Code, String user,
			String docuString, Date expiryDate);

	public int saveOptionalUploadsVersionsForSisuSariya(String filePath, String doc_des, String doc_code,
			String permitNo, String applicationNo, String transaction_Code, String user, int versionNo,
			Date expiryDate);

	public int saveOptionalUploadsForSisuSariya(String filePath, String doc_des, String doc_code, String permitNo,
			String applicationNo, String transaction_Code, String user, Date expiryDate);

	public List<DocumentManagementDTO> mandatoryDocsForSisuSariya(String typeCode, String empNo);

	public List<DocumentManagementDTO> optionalDocsForSisuSariya(String typeCode, String empNo);

	public List<DocumentManagementDTO> sisuSariyaMandatoryList(String requestNo);

	public List<DocumentManagementDTO> sisuSariyaOptionalList(String requestNo);

	public int getVersionNumberForSisuSariyaPermitHolder(DocumentManagementDTO uploaddocumentManagementDTO,
			String typeCodeTrans, String docType);

	public String getFilePathForSisuSariyaPermitHolder(String filePath, String doc_des, String permitNo,
			String transaction_Code, String doc_code, String appNo);

	public int replaceDocForSisuSariyaPermitHolder(String doc_des, String permitNo, String transaction_Code,
			String user, String docuString, String refNo, Date expiryDate);

	public int saveMandatoryUploadsVersionsForSisuSariyaPermitHolder(String filePath, String doc_des, String doc_code,
			String permitNo, String applicationNo, String transaction_Code, String user, int versionNo,
			Date expiryDate);

	public int saveMandatoryUploadsForSisuSariyaPermitHolder(String filePath, String doc_des, String doc_code,
			String permitNo, String applicationNo, String transaction_Code, String user, Date expiryDate);

	public int replaceOptionalDocForSisuSariyaPermitHolder(String doc_des, String permitNo, String transaction_Code,
			String user, String docuString, String refNo, Date expiryDate);

	public int saveOptionalUploadsVersionsForSisuSariyaPermitHolder(String filePath, String doc_des, String doc_code,
			String permitNo, String applicationNo, String transaction_Code, String user, int versionNo,
			Date expiryDate);

	public String getOptionalFilePathForSisuSariyaPermitHolder(String filePath, String doc_des, String permitNo,
			String transaction_Code, String doc_code, String appNo);

	public int saveOptionalUploadsForSisuSariyaPermitHolder(String filePath, String doc_des, String doc_code,
			String permitNo, String applicationNo, String transaction_Code, String user, Date expiryDate);

	public List<DocumentManagementDTO> mandatoryDocsForSisuSariyaPermitHolder(String typeCode, String empNo);

	public List<DocumentManagementDTO> optionalDocsForSisuSariyaPermitHolder(String typeCode, String empNo);

	public List<DocumentManagementDTO> sisuSariyaPermitHolderMandatoryList(String requestNo, String refNo);

	public List<DocumentManagementDTO> sisuSariyaPermitHolderOptionalList(String requestNo, String refNo);

	public List<DocumentManagementDTO> mandatoryDocsForSisuSariyaAgreementRenewals(String typeCode, String empNo);

	public List<DocumentManagementDTO> optionalDocsForSisuSariyaAgreementRenewals(String typeCode, String empNo);

	public List<DocumentManagementDTO> sisuSariyaAgreementRenewalsMandatoryList(String requestNo, String refNo,
			String serviceNo);

	public List<DocumentManagementDTO> sisuSariyaAgreementRenewalsOptionalList(String requestNo, String refNo,
			String serviceNo);

	public int getVersionNumberForSisuSariyaAgreementRenewals(DocumentManagementDTO uploaddocumentManagementDTO,
			String typeCodeTrans, String docType);

	public String getFilePathForSisuSariyaAgreementRenewals(String filePath, String doc_des, String permitNo,
			String transaction_Code, String doc_code, String appNo, String serviceNo);

	public int replaceDocForSisuSariyaAgreementRenwals(String doc_des, String permitNo, String transaction_Code,
			String user, String docuString, String refNo, String serviceNo);

	public int saveMandatoryUploadsVersionsForSisuSariyaAgreementRenewals(String filePath, String doc_des,
			String doc_code, String permitNo, String applicationNo, String transaction_Code, String user, int versionNo,
			String serviceNo);

	public int saveMandatoryUploadsForSisuSariyaAgreementRenewals(String filePath, String doc_des, String doc_code,
			String permitNo, String applicationNo, String transaction_Code, String user, String serviceNo);

	public String getOptionalFilePathForSisuSariyaAgreementRenewals(String filePath, String doc_des, String permitNo,
			String transaction_Code, String doc_code, String appNo, String serviceNo);

	public int replaceOptionalDocForSisuSariyaAgreementRenewals(String doc_des, String permitNo,
			String transaction_Code, String user, String docuString, String refNo, String serviceNo);

	public int saveOptionalUploadsVersionsForSisuSariyaAgreementRenewals(String filePath, String doc_des,
			String doc_code, String permitNo, String applicationNo, String transaction_Code, String user, int versionNo,
			String serviceNo);

	public int saveOptionalUploadsForSisuSariyaAgreementRenewals(String filePath, String doc_des, String doc_code,
			String permitNo, String applicationNo, String transaction_Code, String user, String serviceNo);

	public String getBacklogApplicationByPermitNo(String permitNo);

	public List<DocumentManagementDTO> RenewalApplicationList(String uploadPermitNo);

	public List<DocumentManagementDTO> ConfirmRenewalApplicationList(List<DocumentManagementDTO> ApplicationList);

	public List<DocumentManagementDTO> permitRenewalMandatoryNewList(String permitNo,
			List<DocumentManagementDTO> RenewalApplicationList);

	public List<DocumentManagementDTO> permitRenewalOptionalNewList(String permitNo,
			List<DocumentManagementDTO> RenewalApplicationList);

	public int updateBacklogDocumentAppNo(String permitNo, String applicationNo);

	public int updateBacklogDocumentVersionAppNo(String permitNo, String applicationNo);

	public List<String> GetJoinApplicationNoListForPermitRenewal();

	public List<String> GetJoinVehicleNoListForPermitRenewal();

	public List<String> GetVehicleNoListForPermitRenewal();

	public List<String> GetPermitNoListForPermitRenewal(List<String> applicationList);

	public List<DocumentManagementDTO> optionalDocsForAmendments(String typeCode, String uploadPermitNo,
			String uploadApplicationNo);

	public List<DocumentManagementDTO> mandatoryDocsForAmendments(String typeCode, String uploadPermitNo,
			String uploadApplicationNo);

	public List<DocumentManagementDTO> getDeleteDocumentInfo(String path);

	public int insertDeleteRecord(List<DocumentManagementDTO> deleteDoc, String user);

	public List<DocumentManagementDTO> gamiSariyaMandatoryList(String requestNo);

	public List<DocumentManagementDTO> gamiSariyaOptionalList(String requestNo);

	public List<DocumentManagementDTO> gamiSariyaSurveyRequestMandatoryList(String requestNo, String refNo);

	public List<DocumentManagementDTO> gamiSariyaSurveyRequestOptionalList(String requestNo, String refNo);

	public List<DocumentManagementDTO> gamiSariyaSurveyMandatoryList(String requestNo, String refNo, String serviceNo);

	public List<DocumentManagementDTO> gamiSariyaSurveyOptionalList(String requestNo, String refNo, String serviceNo);

	public List<DocumentManagementDTO> mandatoryDocsForGrievance(String typeCode, String complainNo);

	public List<DocumentManagementDTO> optionalDocsForGrievance(String typeCode, String complainNo);

	public List<DocumentManagementDTO> grievanceMandatoryListM(String complainNo, String strTransactionType);

	public List<DocumentManagementDTO> grievanceOptionalListM(String complainNo, String strTransactionType);

	public int getVersionNumberForGrievance(DocumentManagementDTO uploaddocumentManagementDTO, String typeCodeTrans,
			String docType);

	public String getFilePathForGrievance(String filePath, String doc_des, String permitNo, String transaction_Code,
			String doc_code);

	public int replaceDocForGrievance(String doc_des, String permitNo, String transaction_Code, String user,
			String docuString);

	public int saveMandatoryUploadsVersionsForGrievance(String filePath, String doc_des, String doc_code,
			String permitNo, String transaction_Code, String user, int versionNo);

	public int saveMandatoryUploadsForGrievance(String filePath, String doc_des, String doc_code, String permitNo,
			String transaction_Code, String user);

	public String getOptionalFilePathForGrievance(String filePath, String doc_des, String permitNo,
			String transaction_Code, String doc_code);

	public int replaceOptionalDocForGrievance(String doc_des, String permitNo, String transaction_Code, String user,
			String docuString);

	public int saveOptionalUploadsVersionsForGrievance(String filePath, String doc_des, String doc_code,
			String permitNo, String transaction_Code, String user, int versionNo);

	public int saveOptionalUploadsForGrievance(String filePath, String doc_des, String doc_code, String permitNo,
			String transaction_Code, String user);

	public List<RevenueCollectionDTO> GetAllDocumentTrasactionTypes();

	public List<DocumentManagementDTO> mandatoryDocsForSIMReg(String typeCode, String simRegNo);

	public List<DocumentManagementDTO> optionalDocsForSIMReg(String typeCode, String simRegNo);

	public List<DocumentManagementDTO> simRegMandatoryListM(String simRegNo, String strTransactionType);

	public List<DocumentManagementDTO> simRegOptionalListM(String simRegNo, String strTransactionType);

	public int getVersionNumberForSim(DocumentManagementDTO uploaddocumentManagementDTO, String typeCodeTrans,
			String docType);

	public String getFilePathForSIM(String filePath, String doc_des, String permitNo, String transaction_Code,
			String doc_code);

	public int replaceDocForSIM(String doc_des, String permitNo, String transaction_Code, String user,
			String docuString);

	public int saveMandatoryUploadsVersionsForSIM(String filePath, String doc_des, String doc_code, String permitNo,
			String transaction_Code, String user, int versionNo);

	public int saveMandatoryUploadsForSIM(String filePath, String doc_des, String doc_code, String permitNo,
			String transaction_Code, String user);

	public String getOptionalFilePathForSIM(String filePath, String doc_des, String permitNo, String transaction_Code,
			String doc_code);

	public int replaceOptionalDocForSIM(String doc_des, String permitNo, String transaction_Code, String user,
			String docuString);

	public int saveOptionalUploadsVersionsForSIM(String filePath, String doc_des, String doc_code, String permitNo,
			String transaction_Code, String user, int versionNo);

	public int saveOptionalUploadsForSIM(String filePath, String doc_des, String doc_code, String permitNo,
			String transaction_Code, String user);
	// newly added for changes in view upload document function
	List<String> getPermitNumbersForView();
	List<DocumentManagementDTO>getTransactionsByPermitNumber(String permitNo);
	List<String>GetApplicationNoListByPermitAndTransType(String transactionType , String permitNo);
	String getTranactionDescription(String code);
	 List<DocumentManagementDTO> optionalDocsForAmendmentsWithOutAppNo(String typeCode, String uploadPermitNo);

	List<DocumentManagementDTO> mandatoryDocsForAmendmentsWithOutAppNo(String typeCode, String uploadPermitNo);
	List<DocumentManagementDTO> mandatoryViewDocsByAppNo(String typeCode, String uploadPermitNo,String appNo);
	List<DocumentManagementDTO> optionalViewDocsByAppNo(String typeCode, String uploadPermitNo,String appNo);

	void beanLinkMethod(DocumentManagementDTO registrationDto, String user, String des, String funDes);
}
