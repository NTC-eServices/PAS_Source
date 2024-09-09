package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import lk.informatics.ntc.model.dto.DocumentManagementDTO;
import lk.informatics.ntc.model.dto.GamiRouteDTO;
import lk.informatics.ntc.model.dto.GamiSeriyaDTO;
import lk.informatics.ntc.model.dto.SurveyDTO;
import lk.informatics.ntc.model.dto.TrafficProposalDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.DocumentManagementService;
import lk.informatics.ntc.model.service.GamiSariyaService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "gsTrafficProposalBackingBean")
@ViewScoped
public class GsTrafficProposalBackingBean {

	private static final long serialVersionUID = 1L;

	private TrafficProposalDTO trafficProposalDTO;
	private GamiRouteDTO gamiRouteDTO;

	private List<SurveyDTO> drpdSurveyNoList;
	private List<GamiRouteDTO> tblTrafficProposalRouteInfo;
	private List<GamiRouteDTO> tblTrafficProposalSelectedRouteInfo;
	private List<GamiSeriyaDTO> drpdServiceTypeList;
	private List<GamiSeriyaDTO> drpdServiceMethodList;
	private List<DocumentManagementDTO> mandatoryList = new ArrayList<DocumentManagementDTO>(0);
	private List<DocumentManagementDTO> optionalList = new ArrayList<DocumentManagementDTO>(0);

	private GamiSariyaService gamiSariyaService;
	private CommonService commonService;
	private DocumentManagementService documentManagementService;

	private String errorMsg, alertMsg;
	private String sucessMsg = "Successfully Saved.";
	private String SurveyReqNo;

	private boolean renderPnlRouteInfo;
	private boolean renderBtnUpdate;

	private boolean disableByFlow;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	@PostConstruct
	public void init() {

		gamiRouteDTO = new GamiRouteDTO();
		trafficProposalDTO = new TrafficProposalDTO();
		drpdSurveyNoList = new ArrayList<SurveyDTO>();
		tblTrafficProposalRouteInfo = new ArrayList<GamiRouteDTO>();
		tblTrafficProposalSelectedRouteInfo = new ArrayList<GamiRouteDTO>();
		drpdServiceMethodList = new ArrayList<GamiSeriyaDTO>();
		drpdServiceTypeList = new ArrayList<GamiSeriyaDTO>();

		loadValues();
	}

	public void loadValues() {

		gamiSariyaService = (GamiSariyaService) SpringApplicationContex.getBean("gamiSariyaService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		documentManagementService = (DocumentManagementService) SpringApplicationContex
				.getBean("documentManagementService");

		drpdSurveyNoList = gamiSariyaService.drpdSurveyNoListForTrafficProposal();
		drpdServiceTypeList = gamiSariyaService.drpdServiceTypeList();
		drpdServiceMethodList = gamiSariyaService.drpdServiceMethodsList();
	}

	public void onSurveyNoChange() {

		tblTrafficProposalRouteInfo = new ArrayList<GamiRouteDTO>();
		String temptSurveyNo = trafficProposalDTO.getSurveyNo();
		trafficProposalDTO = new TrafficProposalDTO();
		trafficProposalDTO.setSurveyNo(temptSurveyNo);
		renderPnlRouteInfo = false;

		if (trafficProposalDTO.getSurveyNo() != null && !trafficProposalDTO.getSurveyNo().isEmpty()
				&& !trafficProposalDTO.getSurveyNo().equals("")) {

			trafficProposalDTO = gamiSariyaService.getGamiTrafficProposalInfo(trafficProposalDTO);
			SurveyReqNo = trafficProposalDTO.getRequestNo();

			gamiRouteDTO.setTrafficProposalNo(trafficProposalDTO.getTrafficProposalNo());
			if (trafficProposalDTO.getStatus() != null && !trafficProposalDTO.getStatus().isEmpty()
					&& !trafficProposalDTO.getStatus().equals("")) {
				renderBtnUpdate = true;

			} else {
				renderBtnUpdate = false;
			}
		} else {
			errorMsg = "Survey No. did not found.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void btnSearch() {

		disableByFlow = false;

		if (trafficProposalDTO.getTrafficProposalNo() != null && !trafficProposalDTO.getTrafficProposalNo().isEmpty()
				&& !trafficProposalDTO.getTrafficProposalNo().equals("")) {
			disableByFlow = gamiSariyaService.getTenderStatus(trafficProposalDTO);
		} else {
			disableByFlow = false;
		}

		if (trafficProposalDTO.getSurveyNo() != null && !trafficProposalDTO.getSurveyNo().isEmpty()
				&& !trafficProposalDTO.getSurveyNo().equals("")) {
			renderPnlRouteInfo = true;
			SurveyDTO surveyDTO = new SurveyDTO();
			surveyDTO.setRequestNo(trafficProposalDTO.getRequestNo());
			tblTrafficProposalRouteInfo = new ArrayList<GamiRouteDTO>();
			tblTrafficProposalRouteInfo = gamiSariyaService.getTblGamiRouteInfo(surveyDTO);
			gamiRouteDTO.setTrafficProposalNo(trafficProposalDTO.getTrafficProposalNo());
			gamiRouteDTO.setSurveyReqNo(trafficProposalDTO.getRequestNo());
			tblTrafficProposalSelectedRouteInfo = gamiSariyaService
					.getTblTrafficProposalSelectedRouteInfo(gamiRouteDTO);

			if (!(commonService.checkTaskOnSurveyHisDetails(trafficProposalDTO.getSurveyNo(), "SU008", "C"))) {
				commonService.updateSurveyTaskDetailsBySurveyNo("", trafficProposalDTO.getSurveyNo(), "SU009", "O",
						sessionBackingBean.getLoginUser());
			}
		} else {
			errorMsg = "Survey No. sholud be selected.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void btnClear() {
		trafficProposalDTO = new TrafficProposalDTO();
		tblTrafficProposalRouteInfo = new ArrayList<GamiRouteDTO>();
		tblTrafficProposalSelectedRouteInfo = new ArrayList<GamiRouteDTO>();
	}

	public void btnUpdate() {

		if (trafficProposalDTO.getSpecialRemark() != null && !trafficProposalDTO.getSpecialRemark().isEmpty()
				&& !trafficProposalDTO.getSpecialRemark().equals("")) {
			if (trafficProposalDTO.getSpecialPrintNote() != null && !trafficProposalDTO.getSpecialPrintNote().isEmpty()
					&& !trafficProposalDTO.getSpecialPrintNote().equals("")) {
				if (trafficProposalDTO.getSuggestions() != null && !trafficProposalDTO.getSuggestions().isEmpty()
						&& !trafficProposalDTO.getSuggestions().equals("")) {

					if (trafficProposalDTO.getTrafficProposalNo() != null
							&& !trafficProposalDTO.getTrafficProposalNo().isEmpty()
							&& !trafficProposalDTO.getTrafficProposalNo().equals("")) {

						if (gamiSariyaService.updateTrafficProposalInfo(trafficProposalDTO,
								sessionBackingBean.loginUser)) {
							sucessMsg = "Successfuly Updated.";
							RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
						} else {
							errorMsg = "Data did not saved.";
							RequestContext.getCurrentInstance().update("frmrequiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
						}

					} else {
						errorMsg = "Traffic Proposal No. did not found.";
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}

				} else {
					errorMsg = "Suggestions should be inserted.";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			} else {
				errorMsg = "Special print note should be inserted.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		} else {
			errorMsg = "Special Remarks should be inserted.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void btnAdd() {

		if (gamiRouteDTO.getSeqNo() != 0) {

			tblTrafficProposalSelectedRouteInfo.add(gamiRouteDTO);
			gamiRouteDTO.setTrafficProposalNo(trafficProposalDTO.getTrafficProposalNo());
			gamiRouteDTO.setSurveyReqNo(SurveyReqNo);
			gamiRouteDTO.setIsTrfficProposalSelected("Y");

			boolean saved = gamiSariyaService.saveSelectedRouteInfo(gamiRouteDTO, sessionBackingBean.loginUser);
			if (saved) {

				if (gamiSariyaService.insertGamiRouteHistory(gamiRouteDTO, sessionBackingBean.loginUser)) {
					sucessMsg = "Successfuly saved.";
					RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
					gamiRouteDTO = new GamiRouteDTO();
				} else {
					errorMsg = "Data did not save.";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}

			} else {
				errorMsg = "Data did not save.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

			gamiRouteDTO.setTrafficProposalNo(trafficProposalDTO.getTrafficProposalNo());
			gamiRouteDTO.setSurveyReqNo(trafficProposalDTO.getRequestNo());
			tblTrafficProposalSelectedRouteInfo = gamiSariyaService
					.getTblTrafficProposalSelectedRouteInfo(gamiRouteDTO);

		} else {
			errorMsg = "Select a route from the table.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public void btnSave() {

		if (trafficProposalDTO.getSpecialRemark() != null && !trafficProposalDTO.getSpecialRemark().isEmpty()
				&& !trafficProposalDTO.getSpecialRemark().equals("")) {
			if (trafficProposalDTO.getSpecialPrintNote() != null && !trafficProposalDTO.getSpecialPrintNote().isEmpty()
					&& !trafficProposalDTO.getSpecialPrintNote().equals("")) {
				if (trafficProposalDTO.getSuggestions() != null && !trafficProposalDTO.getSuggestions().isEmpty()
						&& !trafficProposalDTO.getSuggestions().equals("")) {

					trafficProposalDTO = gamiSariyaService.saveTrafficProposalInfo(trafficProposalDTO,
							sessionBackingBean.loginUser);

					if (trafficProposalDTO.getTrafficProposalNo() != null
							&& !trafficProposalDTO.getTrafficProposalNo().isEmpty()
							&& !trafficProposalDTO.getTrafficProposalNo().equals("")) {

						commonService.updateSurveyTaskDetailsBySurveyNo("", trafficProposalDTO.getSurveyNo(), "SU009",
								"C", sessionBackingBean.getLoginUser());

						boolean saved = gamiSariyaService.updateTrafficProposalNo(tblTrafficProposalSelectedRouteInfo,
								trafficProposalDTO.getTrafficProposalNo());
						if (saved) {
							sucessMsg = "Successfuly Saved.";
							renderBtnUpdate = true;
							RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
						} else {
							errorMsg = "Data did not save.";
							RequestContext.getCurrentInstance().update("frmrequiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
						}

					} else {
						errorMsg = "Data did not save.";
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}

				} else {
					errorMsg = "Suggestions should be inserted.";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			} else {
				errorMsg = "Special print note should be inserted.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		} else {
			errorMsg = "Special Remarks should be inserted.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void btnClearTrafficProposalInfo() {
		trafficProposalDTO = new TrafficProposalDTO();
		tblTrafficProposalRouteInfo = new ArrayList<GamiRouteDTO>();
	}

	public void btnPrintTrafficProposal() {

	}

	public void deleteTblTrafficProSelectedRouteRaw(GamiRouteDTO tblRouteInfo) {

		tblRouteInfo.setIsTrfficProposalSelected(null);
		boolean saved = gamiSariyaService.saveSelectedRouteInfo(tblRouteInfo, sessionBackingBean.loginUser);
		if (saved) {

			if (gamiSariyaService.insertGamiRouteHistory(gamiRouteDTO, sessionBackingBean.loginUser)) {
				sucessMsg = "Successfuly saved.";
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
			} else {
				errorMsg = "Data did not save.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
			tblTrafficProposalSelectedRouteInfo = gamiSariyaService
					.getTblTrafficProposalSelectedRouteInfo(gamiRouteDTO);
		} else {
			errorMsg = "Data did not save.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void onRouteRowSelect(SelectEvent event) {

		gamiRouteDTO.setSeqNo(((GamiRouteDTO) event.getObject()).getSeqNo());
		gamiRouteDTO.setSequenceNo(((GamiRouteDTO) event.getObject()).getSequenceNo());
		gamiRouteDTO.setOriginDesc(((GamiRouteDTO) event.getObject()).getOriginDesc());
		gamiRouteDTO.setDestinationDesc(((GamiRouteDTO) event.getObject()).getDestinationDesc());
		gamiRouteDTO.setViaDesc(((GamiRouteDTO) event.getObject()).getViaDesc());
		gamiRouteDTO.setProvinceDesc(((GamiRouteDTO) event.getObject()).getProvinceDesc());
		gamiRouteDTO.setTotalLength(((GamiRouteDTO) event.getObject()).getTotalLength());
		gamiRouteDTO.setUnservedPortion(((GamiRouteDTO) event.getObject()).getUnservedPortion());
		gamiRouteDTO.setNoOfPermitsRequired(((GamiRouteDTO) event.getObject()).getNoOfPermitsRequired());

	}

	public void btnClearRouteInfo() {
		gamiRouteDTO = new GamiRouteDTO();
	}

	public void btnDocumentMangement() {

		try {
			String requestNo = gamiSariyaService.getGamiRequestNo(trafficProposalDTO.getSurveyNo());
			String surveyRequestNo = gamiSariyaService.getGamiSurveyRequestNo(trafficProposalDTO.getSurveyNo());
			sessionBackingBean.setServiceNoForSisuSariya(trafficProposalDTO.getSurveyNo());
			sessionBackingBean.setServiceRefNo(surveyRequestNo);
			sessionBackingBean.setRequestNoForSisuSariya(requestNo);

			sessionBackingBean.setTransactionType("GAMI SARIYA");

			mandatoryList = documentManagementService.mandatoryDocsForSisuSariyaAgreementRenewals("19",
					trafficProposalDTO.getSurveyNo());
			optionalList = documentManagementService.optionalDocsForSisuSariyaAgreementRenewals("19",
					trafficProposalDTO.getSurveyNo());

			sessionBackingBean.sisuSariyaMandatoryDocumentList = documentManagementService
					.gamiSariyaMandatoryList(requestNo);
			sessionBackingBean.sisuSariyaOptionalDocumentList = documentManagementService
					.gamiSariyaOptionalList(requestNo);

			if (surveyRequestNo != null && requestNo != null) {

				sessionBackingBean.sisuSariyaServiceRefMandatoryDocumentList = documentManagementService
						.gamiSariyaSurveyRequestMandatoryList(requestNo, surveyRequestNo);
				sessionBackingBean.sisuSariyaServiceRefOptionalDocumentList = documentManagementService
						.gamiSariyaSurveyRequestOptionalList(requestNo, surveyRequestNo);
			}

			if (trafficProposalDTO.getSurveyNo() != null && surveyRequestNo != null && requestNo != null) {

				sessionBackingBean.sisuSariyaServiceMandatoryDocumentList = documentManagementService
						.gamiSariyaSurveyMandatoryList(requestNo, surveyRequestNo, trafficProposalDTO.getSurveyNo());
				sessionBackingBean.sisuSariyaServiceOptionalDocumentList = documentManagementService
						.gamiSariyaSurveyOptionalList(requestNo, surveyRequestNo, trafficProposalDTO.getSurveyNo());

			}

			RequestContext.getCurrentInstance().execute("PF('uploadDocument').show()");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// gettes and setters
	public TrafficProposalDTO getTrafficProposalDTO() {
		return trafficProposalDTO;
	}

	public void setTrafficProposalDTO(TrafficProposalDTO trafficProposalDTO) {
		this.trafficProposalDTO = trafficProposalDTO;
	}

	public List<SurveyDTO> getDrpdSurveyNoList() {
		return drpdSurveyNoList;
	}

	public void setDrpdSurveyNoList(List<SurveyDTO> drpdSurveyNoList) {
		this.drpdSurveyNoList = drpdSurveyNoList;
	}

	public GamiSariyaService getGamiSariyaService() {
		return gamiSariyaService;
	}

	public void setGamiSariyaService(GamiSariyaService gamiSariyaService) {
		this.gamiSariyaService = gamiSariyaService;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<GamiRouteDTO> getTblTrafficProposalRouteInfo() {
		return tblTrafficProposalRouteInfo;
	}

	public void setTblTrafficProposalRouteInfo(List<GamiRouteDTO> tblTrafficProposalRouteInfo) {
		this.tblTrafficProposalRouteInfo = tblTrafficProposalRouteInfo;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getAlertMsg() {
		return alertMsg;
	}

	public void setAlertMsg(String alertMsg) {
		this.alertMsg = alertMsg;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public boolean isRenderPnlRouteInfo() {
		return renderPnlRouteInfo;
	}

	public void setRenderPnlRouteInfo(boolean renderPnlRouteInfo) {
		this.renderPnlRouteInfo = renderPnlRouteInfo;
	}

	public String getSucessMsg() {
		return sucessMsg;
	}

	public void setSucessMsg(String sucessMsg) {
		this.sucessMsg = sucessMsg;
	}

	public boolean isRenderBtnUpdate() {
		return renderBtnUpdate;
	}

	public void setRenderBtnUpdate(boolean renderBtnUpdate) {
		this.renderBtnUpdate = renderBtnUpdate;
	}

	public List<GamiRouteDTO> getTblTrafficProposalSelectedRouteInfo() {
		return tblTrafficProposalSelectedRouteInfo;
	}

	public void setTblTrafficProposalSelectedRouteInfo(List<GamiRouteDTO> tblTrafficProposalSelectedRouteInfo) {
		this.tblTrafficProposalSelectedRouteInfo = tblTrafficProposalSelectedRouteInfo;
	}

	public GamiRouteDTO getGamiRouteDTO() {
		return gamiRouteDTO;
	}

	public void setGamiRouteDTO(GamiRouteDTO gamiRouteDTO) {
		this.gamiRouteDTO = gamiRouteDTO;
	}

	public List<GamiSeriyaDTO> getDrpdServiceTypeList() {
		return drpdServiceTypeList;
	}

	public void setDrpdServiceTypeList(List<GamiSeriyaDTO> drpdServiceTypeList) {
		this.drpdServiceTypeList = drpdServiceTypeList;
	}

	public List<GamiSeriyaDTO> getDrpdServiceMethodList() {
		return drpdServiceMethodList;
	}

	public void setDrpdServiceMethodList(List<GamiSeriyaDTO> drpdServiceMethodList) {
		this.drpdServiceMethodList = drpdServiceMethodList;
	}

	public String getSurveyReqNo() {
		return SurveyReqNo;
	}

	public void setSurveyReqNo(String surveyReqNo) {
		SurveyReqNo = surveyReqNo;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public boolean isDisableByFlow() {
		return disableByFlow;
	}

	public void setDisableByFlow(boolean disableByFlow) {
		this.disableByFlow = disableByFlow;
	}

	public List<DocumentManagementDTO> getMandatoryList() {
		return mandatoryList;
	}

	public void setMandatoryList(List<DocumentManagementDTO> mandatoryList) {
		this.mandatoryList = mandatoryList;
	}

	public List<DocumentManagementDTO> getOptionalList() {
		return optionalList;
	}

	public void setOptionalList(List<DocumentManagementDTO> optionalList) {
		this.optionalList = optionalList;
	}

	public DocumentManagementService getDocumentManagementService() {
		return documentManagementService;
	}

	public void setDocumentManagementService(DocumentManagementService documentManagementService) {
		this.documentManagementService = documentManagementService;
	}

}
