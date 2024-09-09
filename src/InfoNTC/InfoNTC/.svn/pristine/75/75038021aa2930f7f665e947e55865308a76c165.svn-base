package lk.informatics.ntc.model.service;

import java.io.Serializable;
import java.util.List;

import lk.informatics.ntc.model.dto.FormDTO;
import lk.informatics.ntc.model.dto.GamiAnalyzedDataDTO;
import lk.informatics.ntc.model.dto.GamiRouteDTO;
import lk.informatics.ntc.model.dto.GamiSeriyaDTO;
import lk.informatics.ntc.model.dto.GenerateSurveyFormDTO;
import lk.informatics.ntc.model.dto.IndicatorsDTO;
import lk.informatics.ntc.model.dto.MidPointSurveyDTO;
import lk.informatics.ntc.model.dto.SisuSeriyaDTO;
import lk.informatics.ntc.model.dto.SurveyDTO;
import lk.informatics.ntc.model.dto.SurveyLocationTeamDTO;
import lk.informatics.ntc.model.dto.TenderDTO;
import lk.informatics.ntc.model.dto.TrafficProposalDTO;

public interface GamiSariyaService extends Serializable {
	public String generateGamiRequestNo();

	public List requestorDropDown();

	public GamiSeriyaDTO saveGamiRequestorInfo(GamiSeriyaDTO gamiDTO, String loginUser);

	public boolean updateGamiRequestorStatus(GamiSeriyaDTO gamiDTO);

	public boolean saveRequstorRouteInfo(GamiSeriyaDTO gamiDTO, String loginUser);

	public List<GamiSeriyaDTO> tblGamiRequestorRouteList(GamiSeriyaDTO gamiDTO);

	public void delete(String value);

	public GamiSeriyaDTO searchForViewEdit(GamiSeriyaDTO gamiDTO);

	public boolean checkViewEdit(GamiSeriyaDTO gamiDTO);

	public List refNodropDown();

	public void editReqInfo(GamiSeriyaDTO gamiDTO, String loginUser);

	public void editRouteInfo(GamiSeriyaDTO gamiDTO, String loginUser);

	public List<GamiSeriyaDTO> drpdRequestNoList();

	public List<SisuSeriyaDTO> drpdSurveyRequestNoList();

	public List<SisuSeriyaDTO> drpdOrganizationList();

	public List<SisuSeriyaDTO> departmentList();

	public List<GamiSeriyaDTO> drpdRequestType();

	public GamiSeriyaDTO getRequestDetailsForInitiateByRequestNo(GamiSeriyaDTO gamiSeriyaDTO);

	public GamiSeriyaDTO getInitiateDetailsByServeyReqNo(GamiSeriyaDTO gamiSeriyaDTO);

	public List<GamiSeriyaDTO> tblOriginDestionDetailsByRequestNo(GamiSeriyaDTO gamiSeriyaDTO);

	public boolean updateInitiateSurveyDetailsBySurveyReqNo(GamiSeriyaDTO gamiSeriyaDTO, String loginUser);

	public String generateGamiSurveyRequestNo();

	public boolean insertInitiateSurveyReqDet(GamiSeriyaDTO gamiSeriyaDTO, String loginUser);

	// initiate survey process
	public List<GamiSeriyaDTO> drpdSurveyProcessRequestNoList();

	public List<GamiSeriyaDTO> drpdServiceTypeList();

	public List<GamiSeriyaDTO> drpdServiceMethodsList();

	public GamiSeriyaDTO insertSurveyProcessDet(GamiSeriyaDTO gamiSeriyaDTO);

	public String generateGamiSurveyNo();

	public List<GamiSeriyaDTO> drpdSurveyNoList();

	public boolean updateGamiSariyaStatus(GamiSeriyaDTO gamiSeriyaDTO, String loginUser);

	public List<GamiSeriyaDTO> drpdSurveyReqNoListForApprove();

	public List<GamiSeriyaDTO> tblGrantApprovalSurveyProcess(GamiSeriyaDTO gamiSeriyaDTO);

	public List<GamiSeriyaDTO> drpdRequestNoListForSurveyProcess();

	public List<GamiSeriyaDTO> tblInitiateSurveyRequest(GamiSeriyaDTO gamiSeriyaDTO);

	// survey management
	public List<SurveyDTO> drpdSurveyNoListForSurveyManagement();

	public SurveyDTO getSurveyNoDet(SurveyDTO surveyDTO);

	public boolean approveRejectCostEstimation(String surveyNo, String status, String loginUser,
			SurveyLocationTeamDTO surveyLocationTeamDTO);

	public boolean updateApproveCostEstimateStatus(String status, String surveyNo);

	// generate survey form
	public GenerateSurveyFormDTO getSurveyNoDetForGenerateSurveyForm(GenerateSurveyFormDTO generateSurveyFormDTO);

	public List<GenerateSurveyFormDTO> drpdSurveyNoListForGenerateSurveyForm(String taskCode, String taskStatus,
			String status);

	public boolean gamiSeriyaSaveForm(GenerateSurveyFormDTO generateSurveyFormDTO,
			List<GenerateSurveyFormDTO> indicators_list, String user);

	public GenerateSurveyFormDTO getGamiFormDetails(GenerateSurveyFormDTO generateSurveyFormDTO);

	public List<GenerateSurveyFormDTO> getGamiIndicatorsList(GenerateSurveyFormDTO generateSurveyFormDTO);

	public List<GenerateSurveyFormDTO> getGamiTblIndicatorsList(GenerateSurveyFormDTO generateSurveyFormDTO);

	public boolean gamiSeriyaUpdateGenerateSurveyForm(GenerateSurveyFormDTO generateSurveyFormDTO,
			List<GenerateSurveyFormDTO> indicators_list, String user);

	public List<GenerateSurveyFormDTO> getGamiTemplateIdFormList();

	public List<IndicatorsDTO> tblQuestionsWithAnswers(String formId);

	public boolean validateGamiSeriyaFormId(String formID);

	public List<FormDTO> drpdFormIdList(String string, String string2);

	public void test();

	// gami permit permit holder information
	public GamiSeriyaDTO saveGamiServiceInformation(GamiSeriyaDTO gamiSeriyaDTO, String loginUser);

	public List<GamiSeriyaDTO> getTblGamiServiceInformation(GamiSeriyaDTO gamiSeriyaDTO);

	public GamiSeriyaDTO viewGamiServiceInformation(GamiSeriyaDTO gamiSeriyaDTO);

	public boolean updateGamiServiceInformation(GamiSeriyaDTO gamiSeriyaDTO, String loginUser);

	public boolean updateBankInformation(GamiSeriyaDTO gamiSeriyaDTO, String loginUser);

	public List<GamiSeriyaDTO> getDrpdTenderReferenceNoList();

	public List<GamiSeriyaDTO> getDrpdServiceReferenceNoList();

	public List<GamiSeriyaDTO> getDrpdPermitNoList();

	public List<GamiSeriyaDTO> getDrpdServiceNoList();

	// gami create tender
	public boolean getTenderStatus(TrafficProposalDTO trafficProposalDTO);

	public GamiRouteDTO onTraficProposalChange(TenderDTO tenderDTO);

	public List<GamiRouteDTO> getTblTrafficProposalRouteInfoForTender(TenderDTO tenderDTO);

	public List<TenderDTO> getDrpdTrafficProposalList(String taskCode, String taskStatus, String status);

	public List<GamiRouteDTO> getTblTrafficProposalRouteInfo(TenderDTO tenderDTO);

	public boolean saveSelectedRouteInfo(GamiRouteDTO gamiRouteDTO, String loginUser);

	public List<GamiRouteDTO> getTblTenderSelectedRouteInfo(GamiRouteDTO gamiRouteDTO);

	public TenderDTO saveCreateTenderInfo(TenderDTO tenderDTO, String loginUser);

	public TenderDTO getGamiTenderDetails(TenderDTO tenderDTO);

	public boolean updateCreateTenderInfo(TenderDTO tenderDTO, String loginUser);

	public List<GamiSeriyaDTO> gamiServiceAuthorization(List<GamiSeriyaDTO> selectedTenderApplications,
			String loginUser);

	public TrafficProposalDTO saveTrafficProposalInfo(TrafficProposalDTO trafficProposalDTO, String loginUser);

	// gami verify survey information
	public List<SurveyDTO> drpdSurveyNoListForVerifySurveyInfo(String taskCode, String taskStaus, String status);

	public SurveyDTO getGamiVerifySurveyInfo(SurveyDTO surveyDTO);

	public GamiRouteDTO saveTblGamiRouteInfoRaw(GamiRouteDTO gamiRouteDTO, String loginUser);

	public List<GamiRouteDTO> getTblGamiRouteInfo(SurveyDTO surveyDTO);

	public boolean updateGamiVerifyInfo(SurveyDTO surveyDTO, String loginUser);

	public boolean UpdateTblGamiRouteInfoRaw(GamiRouteDTO gamiRouteDTO, String loginUser);

	public boolean deleteTblGamiRouteInfoRaw(GamiRouteDTO gamiRouteDTO, String loginUser);

	public TrafficProposalDTO getGamiTrafficProposalInfo(TrafficProposalDTO trafficProposalDTO);

	public boolean updateTrafficProposalInfo(TrafficProposalDTO trafficProposalDTO, String loginUser);

	public boolean addTblTrafficProSelectedRouteRaw(GamiRouteDTO tblRouteInfo, String loginUser);

	public List<GamiRouteDTO> getTblTrafficProposalSelectedRouteInfo(GamiRouteDTO gamiRouteDTO);

	/** request for gami sariya */
	public GamiSeriyaDTO getRequesterInfoByNicNo(GamiSeriyaDTO gamiDTO);

	public boolean insertGamiRouteHistory(GamiRouteDTO gamiRouteDTO, String loginUser);

	public List<SurveyDTO> drpdSurveyNoListForTrafficProposal();

	public boolean updateTrafficProposalNo(List<GamiRouteDTO> tblTrafficProposalSelectedRouteInfo, String surveyNo);

	public List<GamiSeriyaDTO> drpdOriginListForPermotHolderInfo(GamiSeriyaDTO gamiSeriyaDTO);

	public GamiSeriyaDTO getOriginDetails(GamiSeriyaDTO gamiSeriyaDTO);

	public List<GamiRouteDTO> drpdSequenceNoListForPermotHolderInfo(GamiSeriyaDTO gamiSeriyaDTO);

	public GamiRouteDTO getGamiRouteDetailsBySequenceNo(GamiSeriyaDTO gamiSeriyaDTO);

	public List<GamiAnalyzedDataDTO> getGamiAnalyzedData(FormDTO formDTO);

	public String savaGamiFormDetails(MidPointSurveyDTO midPointSurveyDTO, String loginUser);

	public boolean saveGamiFormAnswers(List<IndicatorsDTO> tableIndicatorList, MidPointSurveyDTO midPointSurveyDTO,
			String loginUser);

	public int getGamiNumberOfFormApplications(FormDTO formDTO);

	public void updateNumberGeneration(String code, String appNo, String loginUser);

	public String getGamiRequestNo(String surveyNo);

	public String getGamiSurveyRequestNo(String surveyNo);
}
