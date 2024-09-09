package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.DocumentManagementDTO;
import lk.informatics.ntc.model.dto.GamiRouteDTO;
import lk.informatics.ntc.model.dto.GamiSeriyaDTO;
import lk.informatics.ntc.model.dto.SurveyDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.DocumentManagementService;
import lk.informatics.ntc.model.service.GamiSariyaService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "gsVerifySurveyInfoBackingBean")
@ViewScoped
public class GsVerifySurveyInformation {

	private static final long serialVersionUID = 1L;

	private SurveyDTO surveyDTO;
	private GamiRouteDTO gamiRouteDTO;
	private GamiSeriyaDTO gamiSeriyaDTO;

	private GamiSariyaService gamiSariyaService;
	private CommonService commonService;
	private DocumentManagementService documentManagementService;

	private List<SurveyDTO> drpdSurveyNoList;
	private List<GamiRouteDTO> tblRouteInfo;
	private List<GamiSeriyaDTO> drpdOriginList;
	private List<GamiSeriyaDTO> drpdDestinationList;
	private List<GamiSeriyaDTO> drpdViaList;
	private List<CommonDTO> drpdProvinceList;
	private List<GamiSeriyaDTO> drpdServiceTypeList;
	private List<GamiSeriyaDTO> drpdServiceMethodList;

	private boolean renderTblrouteInfo;
	private boolean tenderRequired;
	private boolean renderBtnUpdateRouteInfo;
	private boolean renderPnlRouteInfo;

	private boolean disableBtnSave, disableBtnApprove, disableBtnReject;

	private List<DocumentManagementDTO> mandatoryList = new ArrayList<DocumentManagementDTO>(0);
	private List<DocumentManagementDTO> optionalList = new ArrayList<DocumentManagementDTO>(0);

	private String errorMsg;
	private String sucessMsg;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	@PostConstruct
	public void init() {

		gamiSariyaService = (GamiSariyaService) SpringApplicationContex.getBean("gamiSariyaService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		documentManagementService = (DocumentManagementService) SpringApplicationContex
				.getBean("documentManagementService");

		surveyDTO = new SurveyDTO();
		gamiRouteDTO = new GamiRouteDTO();
		gamiSeriyaDTO = new GamiSeriyaDTO();

		drpdSurveyNoList = new ArrayList<SurveyDTO>();
		tblRouteInfo = new ArrayList<GamiRouteDTO>();
		drpdOriginList = new ArrayList<GamiSeriyaDTO>();
		drpdProvinceList = new ArrayList<CommonDTO>();
		drpdDestinationList = new ArrayList<GamiSeriyaDTO>();
		drpdViaList = new ArrayList<GamiSeriyaDTO>();
		drpdServiceMethodList = new ArrayList<GamiSeriyaDTO>();
		drpdServiceTypeList = new ArrayList<GamiSeriyaDTO>();

		loadValues();
	}

	public void loadValues() {
		drpdServiceTypeList = gamiSariyaService.drpdServiceTypeList();
		drpdServiceMethodList = gamiSariyaService.drpdServiceMethodsList();
		renderTblrouteInfo = true;
		drpdSurveyNoList = gamiSariyaService.drpdSurveyNoListForVerifySurveyInfo("SU006", "O", "A");

	}

	public void onSurveyNoChange() {

		renderPnlRouteInfo = false;

		if (surveyDTO.getSurveyNo() != null && !surveyDTO.getSurveyNo().isEmpty()
				&& !surveyDTO.getSurveyNo().equals("")) {

			surveyDTO = gamiSariyaService.getGamiVerifySurveyInfo(surveyDTO);
			gamiRouteDTO.setSurveyReqNo(surveyDTO.getRequestNo());

			if (surveyDTO.getStatus() != null
					&& (surveyDTO.getStatus().equals("A") || surveyDTO.getStatus().equals("R"))) {

				disableBtnApprove = true;
				disableBtnReject = true;
				disableBtnSave = true;

			} else {
				disableBtnApprove = false;
				disableBtnReject = false;
				disableBtnSave = false;
			}

		} else {
			errorMsg = "Traffic proposal No. should be selected.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void btnSearch() {

		if (surveyDTO.getSurveyNo() != null && !surveyDTO.getSurveyNo().isEmpty()
				&& !surveyDTO.getSurveyNo().equals("")) {
			renderPnlRouteInfo = true;
			renderBtnUpdateRouteInfo = false;
			renderTblrouteInfo = true;
			tblRouteInfo = gamiSariyaService.getTblGamiRouteInfo(surveyDTO);

			if (!(commonService.checkTaskOnSurveyHisDetails(surveyDTO.getSurveyNo(), "SU007", "C"))) {
				commonService.updateSurveyTaskDetailsBySurveyNo("", surveyDTO.getSurveyNo(), "SU008", "O",
						sessionBackingBean.getLoginUser());
			}
		} else {
			errorMsg = "Survey No. should be selected.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void btnClear() {

		surveyDTO = new SurveyDTO();
		gamiRouteDTO = new GamiRouteDTO();
		tblRouteInfo = new ArrayList<GamiRouteDTO>();
		renderBtnUpdateRouteInfo = false;
	}

	public void onOriginChange() {

	}

	public void btnAdd() {

		gamiRouteDTO.setSurveyReqNo(surveyDTO.getRequestNo());

		if (gamiRouteDTO.getSurveyReqNo() != null && !gamiRouteDTO.getSurveyReqNo().isEmpty()
				&& !gamiRouteDTO.getSurveyReqNo().equals("")) {

			if (gamiRouteDTO.getProvinceDesc() != null && !gamiRouteDTO.getProvinceDesc().isEmpty()
					&& !gamiRouteDTO.getProvinceDesc().equals("")) {

				if (gamiRouteDTO.getOriginDesc() != null && !gamiRouteDTO.getOriginDesc().isEmpty()
						&& !gamiRouteDTO.getOriginDesc().equals("")) {

					if (gamiRouteDTO.getTotalLength() != 0) {

						if (gamiRouteDTO.getDestinationDesc() != null && !gamiRouteDTO.getDestinationDesc().isEmpty()
								&& !gamiRouteDTO.getDestinationDesc().equals("")) {

							if (gamiRouteDTO.getUnservedPortion() != 0) {

								if (gamiRouteDTO.getViaDesc() != null && !gamiRouteDTO.getViaDesc().isEmpty()
										&& !gamiRouteDTO.getViaDesc().equals("")) {

									if (gamiRouteDTO.getNoOfPermitsRequired() != 0) {

										/* Save **/
										if (!renderBtnUpdateRouteInfo) {

											gamiRouteDTO = gamiSariyaService.saveTblGamiRouteInfoRaw(gamiRouteDTO,
													sessionBackingBean.loginUser);
											if (gamiRouteDTO.getSequenceNo() != null
													&& !gamiRouteDTO.getSequenceNo().isEmpty()
													&& !gamiRouteDTO.getSequenceNo().equals("")) {

												if (gamiSariyaService.insertGamiRouteHistory(gamiRouteDTO,
														sessionBackingBean.loginUser)) {

													tblRouteInfo = gamiSariyaService.getTblGamiRouteInfo(surveyDTO);
													sucessMsg = "Successfully Saved.";
													RequestContext.getCurrentInstance()
															.execute("PF('successMessage').show()");
													gamiRouteDTO = new GamiRouteDTO();
												} else {
													errorMsg = "Data did not save.";
													RequestContext.getCurrentInstance().update("frmrequiredField");
													RequestContext.getCurrentInstance()
															.execute("PF('requiredField').show()");
												}
												gamiRouteDTO = new GamiRouteDTO();
											} else {
												errorMsg = "Data did not save.";
												RequestContext.getCurrentInstance().update("frmrequiredField");
												RequestContext.getCurrentInstance()
														.execute("PF('requiredField').show()");
											}
											/* update **/
										} else {

											if (gamiSariyaService.UpdateTblGamiRouteInfoRaw(gamiRouteDTO,
													sessionBackingBean.loginUser)) {
												tblRouteInfo = gamiSariyaService.getTblGamiRouteInfo(surveyDTO);
												sucessMsg = "Successfully Saved.";
												RequestContext.getCurrentInstance()
														.execute("PF('successMessage').show()");
												renderBtnUpdateRouteInfo = false;
												gamiRouteDTO = new GamiRouteDTO();
											} else {
												errorMsg = "Data did not save.";
												RequestContext.getCurrentInstance().update("frmrequiredField");
												RequestContext.getCurrentInstance()
														.execute("PF('requiredField').show()");
											}

										}

									} else {
										errorMsg = "No. Of Permits Req. To Issue should be selected.";
										RequestContext.getCurrentInstance().update("frmrequiredField");
										RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
									}

								} else {
									errorMsg = "Via should be selected.";
									RequestContext.getCurrentInstance().update("frmrequiredField");
									RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
								}

							} else {
								errorMsg = "Unserved Potion should be selected.";
								RequestContext.getCurrentInstance().update("frmrequiredField");
								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
							}

						} else {
							errorMsg = "Destination should be selected.";
							RequestContext.getCurrentInstance().update("frmrequiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
						}

					} else {
						errorMsg = "Total Length should be selected.";
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}

				} else {
					errorMsg = "Origin should be selected.";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}

			} else {
				errorMsg = "Province should be selected.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

		} else {

			errorMsg = "Survey No. should be selected.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void editTblGamiRouteInfoRaw(GamiRouteDTO tblRouteInfo) {

		renderBtnUpdateRouteInfo = true;

		gamiRouteDTO = tblRouteInfo;
		gamiRouteDTO.setSurveyReqNo(surveyDTO.getRequestNo());

	}

	public void deleteTblGamiRouteInfoRaw(GamiRouteDTO tblRouteInfo) {

		if (tblRouteInfo.getSequenceNo() != null && !tblRouteInfo.getSequenceNo().isEmpty()
				&& !tblRouteInfo.getSequenceNo().equals("")) {
			gamiRouteDTO = tblRouteInfo;
			if (gamiSariyaService.deleteTblGamiRouteInfoRaw(gamiRouteDTO, sessionBackingBean.loginUser)) {
				this.tblRouteInfo = gamiSariyaService.getTblGamiRouteInfo(surveyDTO);
				sucessMsg = "Successfully Deleted.";
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
			} else {
				errorMsg = "Data did not deleted.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		} else {
			errorMsg = "Sequence No. did not found.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void btnClearRouteInfo() {

		gamiRouteDTO = new GamiRouteDTO();
		renderBtnUpdateRouteInfo = false;

	}

	public void btnSave() {

		surveyDTO.setSpecialRemarks(null);
		surveyDTO.setTenderRequire("S");
		if (gamiSariyaService.updateGamiVerifyInfo(surveyDTO, "")) {
			sucessMsg = "Successfully Saved.";
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
		} else {
			errorMsg = "Data did not saved.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void btnCancel() {

	}

	public void btnDocumentManagement() {

		String requestNo = gamiSariyaService.getGamiRequestNo(surveyDTO.getSurveyNo());
		String surveyRequestNo = gamiSariyaService.getGamiSurveyRequestNo(surveyDTO.getSurveyNo());
		mandatoryList = documentManagementService.gamiSariyaSurveyMandatoryList(requestNo, surveyRequestNo,
				surveyDTO.getSurveyNo());
		optionalList = documentManagementService.gamiSariyaSurveyOptionalList(requestNo, surveyRequestNo,
				surveyDTO.getSurveyNo());

		sessionBackingBean.sisuSariyaMandatoryDocumentList = documentManagementService
				.gamiSariyaMandatoryList(requestNo);
		sessionBackingBean.sisuSariyaOptionalDocumentList = documentManagementService.gamiSariyaOptionalList(requestNo);

		if (surveyRequestNo != null && requestNo != null) {

			sessionBackingBean.sisuSariyaServiceRefMandatoryDocumentList = documentManagementService
					.gamiSariyaSurveyRequestMandatoryList(requestNo, surveyRequestNo);
			sessionBackingBean.sisuSariyaServiceRefOptionalDocumentList = documentManagementService
					.gamiSariyaSurveyRequestOptionalList(requestNo, surveyRequestNo);
		}

		if (surveyDTO.getSurveyNo() != null && surveyRequestNo != null && requestNo != null) {

			sessionBackingBean.sisuSariyaServiceMandatoryDocumentList = documentManagementService
					.gamiSariyaSurveyMandatoryList(requestNo, surveyRequestNo, surveyDTO.getSurveyNo());
			sessionBackingBean.sisuSariyaServiceOptionalDocumentList = documentManagementService
					.gamiSariyaSurveyOptionalList(requestNo, surveyRequestNo, surveyDTO.getSurveyNo());

		}

		RequestContext.getCurrentInstance().execute("PF('uploadDocument').show()");

	}

	public void btnApprove() {

		surveyDTO.setStatus("A");
		if (gamiSariyaService.updateGamiVerifyInfo(surveyDTO, sessionBackingBean.loginUser)) {
			sucessMsg = "Successfully Saved.";
			disableBtnApprove = true;
			disableBtnReject = true;
			disableBtnSave = true;
			commonService.updateSurveyTaskDetailsBySurveyNo("", surveyDTO.getSurveyNo(), "SU008", "C",
					sessionBackingBean.getLoginUser());
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
		} else {
			errorMsg = "Data did not saved.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void btnReject() {
		surveyDTO.setTenderRequire("R");
		if (gamiSariyaService.updateGamiVerifyInfo(surveyDTO, sessionBackingBean.loginUser)) {
			sucessMsg = "Successfully Saved.";
			disableBtnApprove = true;
			disableBtnReject = true;
			disableBtnSave = true;
			commonService.updateSurveyTaskDetailsBySurveyNo("", surveyDTO.getSurveyNo(), "SU008", "C",
					sessionBackingBean.getLoginUser());
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
		} else {
			errorMsg = "Data did not saved.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	// getters and setters
	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public SurveyDTO getSurveyDTO() {
		return surveyDTO;
	}

	public void setSurveyDTO(SurveyDTO surveyDTO) {
		this.surveyDTO = surveyDTO;
	}

	public List<SurveyDTO> getDrpdSurveyNoList() {
		return drpdSurveyNoList;
	}

	public void setDrpdSurveyNoList(List<SurveyDTO> drpdSurveyNoList) {
		this.drpdSurveyNoList = drpdSurveyNoList;
	}

	public boolean isRenderTblrouteInfo() {
		return renderTblrouteInfo;
	}

	public void setRenderTblrouteInfo(boolean renderTblrouteInfo) {
		this.renderTblrouteInfo = renderTblrouteInfo;
	}

	public List<GamiSeriyaDTO> getDrpdOriginList() {
		return drpdOriginList;
	}

	public void setDrpdOriginList(List<GamiSeriyaDTO> drpdOriginList) {
		this.drpdOriginList = drpdOriginList;
	}

	public List<CommonDTO> getDrpdProvinceList() {
		return drpdProvinceList;
	}

	public void setDrpdProvinceList(List<CommonDTO> drpdProvinceList) {
		this.drpdProvinceList = drpdProvinceList;
	}

	public GamiSeriyaDTO getGamiSeriyaDTO() {
		return gamiSeriyaDTO;
	}

	public void setGamiSeriyaDTO(GamiSeriyaDTO gamiSeriyaDTO) {
		this.gamiSeriyaDTO = gamiSeriyaDTO;
	}

	public List<GamiSeriyaDTO> getDrpdDestinationList() {
		return drpdDestinationList;
	}

	public void setDrpdDestinationList(List<GamiSeriyaDTO> drpdDestinationList) {
		this.drpdDestinationList = drpdDestinationList;
	}

	public List<GamiSeriyaDTO> getDrpdViaList() {
		return drpdViaList;
	}

	public void setDrpdViaList(List<GamiSeriyaDTO> drpdViaList) {
		this.drpdViaList = drpdViaList;
	}

	public boolean isTenderRequired() {
		return tenderRequired;
	}

	public void setTenderRequired(boolean tenderRequired) {
		this.tenderRequired = tenderRequired;
	}

	public GamiRouteDTO getGamiRouteDTO() {
		return gamiRouteDTO;
	}

	public void setGamiRouteDTO(GamiRouteDTO gamiRouteDTO) {
		this.gamiRouteDTO = gamiRouteDTO;
	}

	public GamiSariyaService getGamiSariyaService() {
		return gamiSariyaService;
	}

	public void setGamiSariyaService(GamiSariyaService gamiSariyaService) {
		this.gamiSariyaService = gamiSariyaService;
	}

	public List<GamiRouteDTO> getTblRouteInfo() {
		return tblRouteInfo;
	}

	public void setTblRouteInfo(List<GamiRouteDTO> tblRouteInfo) {
		this.tblRouteInfo = tblRouteInfo;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getSucessMsg() {
		return sucessMsg;
	}

	public void setSucessMsg(String sucessMsg) {
		this.sucessMsg = sucessMsg;
	}

	public boolean isRenderBtnUpdateRouteInfo() {
		return renderBtnUpdateRouteInfo;
	}

	public void setRenderBtnUpdateRouteInfo(boolean renderBtnUpdateRouteInfo) {
		this.renderBtnUpdateRouteInfo = renderBtnUpdateRouteInfo;
	}

	public boolean isRenderPnlRouteInfo() {
		return renderPnlRouteInfo;
	}

	public void setRenderPnlRouteInfo(boolean renderPnlRouteInfo) {
		this.renderPnlRouteInfo = renderPnlRouteInfo;
	}

	public boolean isDisableBtnSave() {
		return disableBtnSave;
	}

	public void setDisableBtnSave(boolean disableBtnSave) {
		this.disableBtnSave = disableBtnSave;
	}

	public boolean isDisableBtnApprove() {
		return disableBtnApprove;
	}

	public void setDisableBtnApprove(boolean disableBtnApprove) {
		this.disableBtnApprove = disableBtnApprove;
	}

	public boolean isDisableBtnReject() {
		return disableBtnReject;
	}

	public void setDisableBtnReject(boolean disableBtnReject) {
		this.disableBtnReject = disableBtnReject;
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

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
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
