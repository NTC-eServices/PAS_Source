package lk.informatics.ntc.view.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import lk.informatics.ntc.model.dto.DocumentManagementDTO;
import lk.informatics.ntc.model.dto.GamiSeriyaDTO;
import lk.informatics.ntc.model.dto.PaymentVoucherDTO;
import lk.informatics.ntc.model.dto.SisuSeriyaDTO;
import lk.informatics.ntc.model.dto.SurveyDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.DocumentManagementService;
import lk.informatics.ntc.model.service.GamiSariyaService;
import lk.informatics.ntc.model.service.PaymentVoucherService;
import lk.informatics.ntc.model.service.SurveyService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "gsInitiateSurveyRequestBean")
@ViewScoped
public class GsInitiateSurveyRequestBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private GamiSeriyaDTO gamiSeriyaDTO;
	private GamiSeriyaDTO selectedRoute;

	private List<GamiSeriyaDTO> drpdRequestNoList;
	private List<SisuSeriyaDTO> drpdSurveyRequestNoList;
	private List<SurveyDTO> drpdOrganizationList;
	private List<PaymentVoucherDTO> drpdDepatmentList;
	private List<GamiSeriyaDTO> drpdRequestType;
	private List<GamiSeriyaDTO> tbl_RouteDetailsList;

	private List<DocumentManagementDTO> mandatoryList = new ArrayList<DocumentManagementDTO>(0);
	private List<DocumentManagementDTO> optionalList = new ArrayList<DocumentManagementDTO>(0);

	private GamiSariyaService gamiSariyaService;
	private SurveyService surveyService;
	private PaymentVoucherService paymentVoucherService;
	private CommonService commonService;
	private DocumentManagementService documentManagementService;

	private boolean renderUpdate;
	private boolean renderPnlMain;
	private boolean renderBtnStatus = true;
	private String errorMsg;
	private String alertMsg;

	@PostConstruct
	public void init() {

		gamiSeriyaDTO = new GamiSeriyaDTO();

		drpdRequestNoList = new ArrayList<GamiSeriyaDTO>();
		drpdSurveyRequestNoList = new ArrayList<SisuSeriyaDTO>();
		drpdOrganizationList = new ArrayList<SurveyDTO>();
		drpdDepatmentList = new ArrayList<PaymentVoucherDTO>();
		drpdRequestType = new ArrayList<GamiSeriyaDTO>();
		tbl_RouteDetailsList = new ArrayList<GamiSeriyaDTO>();

		renderUpdate = false;

		gamiSariyaService = (GamiSariyaService) SpringApplicationContex.getBean("gamiSariyaService");
		surveyService = (SurveyService) SpringApplicationContex.getBean("surveyService");
		paymentVoucherService = (PaymentVoucherService) SpringApplicationContex.getBean("paymentVoucherService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		documentManagementService = (DocumentManagementService) SpringApplicationContex
				.getBean("documentManagementService");

		loadValues();
	}

	public void loadValues() {

		drpdRequestNoList = gamiSariyaService.drpdRequestNoList();
		drpdOrganizationList = surveyService.getOrganizationListDropDown();
		drpdDepatmentList = paymentVoucherService.getDepartment();
		drpdRequestType = gamiSariyaService.drpdRequestType();

	}

	public void onRequestNoChange() {

		renderPnlMain = false;
		String temptReqNo = gamiSeriyaDTO.getRequestNo();
		btnClear();
		gamiSeriyaDTO.setRequestNo(temptReqNo);

	}

	public void btnSearch() {

		if (gamiSeriyaDTO.getRequestNo() != null && !gamiSeriyaDTO.getRequestNo().isEmpty()
				&& !gamiSeriyaDTO.getRequestNo().equalsIgnoreCase("")) {

			renderPnlMain = true;

			gamiSeriyaDTO = gamiSariyaService.getRequestDetailsForInitiateByRequestNo(gamiSeriyaDTO);
			tbl_RouteDetailsList = gamiSariyaService.tblInitiateSurveyRequest(gamiSeriyaDTO);

		} else {

			errorMsg = "Request Number should be selected.";
			RequestContext.getCurrentInstance().update("frm-serviceInformation");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

		for (GamiSeriyaDTO gs : tbl_RouteDetailsList) {

		}
	}

	public void btnClear() {

		renderPnlMain = false;
		gamiSeriyaDTO = new GamiSeriyaDTO();
		renderUpdate = false;
		tbl_RouteDetailsList = new ArrayList<GamiSeriyaDTO>();

	}

	public void onRowSelect(SelectEvent event) {

		gamiSeriyaDTO.setOrganizationCode(null);
		gamiSeriyaDTO.setDepartmentCode(null);
		gamiSeriyaDTO.setReasonForSurvey(null);

		renderUpdate = false;
		gamiSeriyaDTO.setSurveyRequestNo(null);

		gamiSeriyaDTO.setRouteSeqNo(((GamiSeriyaDTO) event.getObject()).getRouteSeqNo().toString());

		String temptSurveyReqNo = null;
		if (((GamiSeriyaDTO) event.getObject()).getSurveyRequestNo() != null) {

			temptSurveyReqNo = ((GamiSeriyaDTO) event.getObject()).getSurveyRequestNo().toString();

		}

		if (temptSurveyReqNo != null && !temptSurveyReqNo.isEmpty() && !temptSurveyReqNo.equalsIgnoreCase("")) {

			renderUpdate = true;
			gamiSeriyaDTO.setSurveyRequestNo(temptSurveyReqNo);
			gamiSeriyaDTO = gamiSariyaService.getInitiateDetailsByServeyReqNo(gamiSeriyaDTO);

			if (gamiSeriyaDTO.getStatus() != null
					&& (gamiSeriyaDTO.getStatus().equals("A") || gamiSeriyaDTO.getStatus().equals("R"))) {
				renderBtnStatus = false;
			} else {
				renderBtnStatus = true;
			}

		}

	}

	public void onRowUnselect(UnselectEvent event) {
		gamiSeriyaDTO.setSurveyRequestNo(null);
		gamiSeriyaDTO.setOrganizationCode(null);
		gamiSeriyaDTO.setDepartmentCode(null);
		gamiSeriyaDTO.setReasonForSurvey(null);
	}

	/**
	 * Buttons
	 */
	public void btnSave() {

		if (gamiSeriyaDTO.getRouteSeqNo() != null && !gamiSeriyaDTO.getRouteSeqNo().isEmpty()
				&& !gamiSeriyaDTO.getRouteSeqNo().equalsIgnoreCase("")) {

			if (gamiSariyaService.insertInitiateSurveyReqDet(gamiSeriyaDTO, sessionBackingBean.getLoginUser())) {

				tbl_RouteDetailsList = gamiSariyaService.tblInitiateSurveyRequest(gamiSeriyaDTO);
				renderUpdate = true;

				commonService.insertSurveyTaskDetails(gamiSeriyaDTO.getSurveyRequestNo(), "SU001", "O",
						sessionBackingBean.getLoginUser());

				commonService.updateSurveyTaskDetails(gamiSeriyaDTO.getSurveyRequestNo(), gamiSeriyaDTO.getSurveyNo(),
						"SU001", "C", sessionBackingBean.getLoginUser());
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

			} else {

				errorMsg = "Could not save data successfully";
				RequestContext.getCurrentInstance().update("frm-serviceInformation");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

			}

		} else {
			errorMsg = "Select Sequence No from the grid.";
			RequestContext.getCurrentInstance().update("frm-serviceInformation");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public void btnUpdate() {

		if (gamiSeriyaDTO.getSurveyRequestNo() != null && !gamiSeriyaDTO.getSurveyRequestNo().isEmpty()
				&& !gamiSeriyaDTO.getSurveyRequestNo().equalsIgnoreCase("")) {

			if (gamiSariyaService.updateInitiateSurveyDetailsBySurveyReqNo(gamiSeriyaDTO,
					sessionBackingBean.getLoginUser())) {

				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

			} else {
				errorMsg = "Could not update data successfully";
				RequestContext.getCurrentInstance().update("frm-serviceInformation");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

		} else {
			errorMsg = "Select Service Request Number from the grid.";
			RequestContext.getCurrentInstance().update("frm-serviceInformation");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void btnCancel() {

	}

	public void documentManagement() {

		try {

			sessionBackingBean.setRequestNoForSisuSariya(gamiSeriyaDTO.getRequestNo());
			sessionBackingBean.setRefNoSisuSariya(gamiSeriyaDTO.getSurveyRequestNo());

			sessionBackingBean.setTransactionType("GAMI SARIYA");

			mandatoryList = documentManagementService.mandatoryDocsForSisuSariyaPermitHolder("19",
					gamiSeriyaDTO.getSurveyRequestNo());
			optionalList = documentManagementService.optionalDocsForSisuSariyaPermitHolder("19",
					gamiSeriyaDTO.getSurveyRequestNo());

			sessionBackingBean.sisuSariyaMandatoryDocumentList = documentManagementService
					.gamiSariyaMandatoryList(gamiSeriyaDTO.getRequestNo());
			sessionBackingBean.sisuSariyaOptionalDocumentList = documentManagementService
					.gamiSariyaOptionalList(gamiSeriyaDTO.getRequestNo());

			if (gamiSeriyaDTO.getSurveyRequestNo() != null && gamiSeriyaDTO.getRequestNo() != null) {

				sessionBackingBean.sisuSariyaServiceRefMandatoryDocumentList = documentManagementService
						.gamiSariyaSurveyRequestMandatoryList(gamiSeriyaDTO.getRequestNo(),
								gamiSeriyaDTO.getSurveyRequestNo());
				sessionBackingBean.sisuSariyaServiceRefOptionalDocumentList = documentManagementService
						.gamiSariyaSurveyRequestOptionalList(gamiSeriyaDTO.getRequestNo(),
								gamiSeriyaDTO.getSurveyRequestNo());
			}

			if (gamiSeriyaDTO.getSurveyNo() != null && gamiSeriyaDTO.getSurveyRequestNo() != null
					&& gamiSeriyaDTO.getRequestNo() != null) {

				sessionBackingBean.sisuSariyaServiceMandatoryDocumentList = documentManagementService
						.gamiSariyaSurveyMandatoryList(gamiSeriyaDTO.getRequestNo(), gamiSeriyaDTO.getSurveyRequestNo(),
								gamiSeriyaDTO.getSurveyNo());
				sessionBackingBean.sisuSariyaServiceOptionalDocumentList = documentManagementService
						.gamiSariyaSurveyOptionalList(gamiSeriyaDTO.getRequestNo(), gamiSeriyaDTO.getSurveyRequestNo(),
								gamiSeriyaDTO.getSurveyNo());

			}

			RequestContext.getCurrentInstance().execute("PF('uploadDocument').show()");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void btnGenerateRespondLetter() {

	}

	// getters and setters
	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public List<GamiSeriyaDTO> getDrpdRequestNoList() {
		return drpdRequestNoList;
	}

	public void setDrpdRequestNoList(List<GamiSeriyaDTO> drpdRequestNoList) {
		this.drpdRequestNoList = drpdRequestNoList;
	}

	public List<SisuSeriyaDTO> getDrpdSurveyRequestNoList() {
		return drpdSurveyRequestNoList;
	}

	public void setDrpdSurveyRequestNoList(List<SisuSeriyaDTO> drpdSurveyRequestNoList) {
		this.drpdSurveyRequestNoList = drpdSurveyRequestNoList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public GamiSariyaService getGamiSariyaService() {
		return gamiSariyaService;
	}

	public void setGamiSariyaService(GamiSariyaService gamiSariyaService) {
		this.gamiSariyaService = gamiSariyaService;
	}

	public GamiSeriyaDTO getGamiSeriyaDTO() {
		return gamiSeriyaDTO;
	}

	public void setGamiSeriyaDTO(GamiSeriyaDTO gamiSeriyaDTO) {
		this.gamiSeriyaDTO = gamiSeriyaDTO;
	}

	public List<SisuSeriyaDTO> getDrpdSurveyNoList() {
		return drpdSurveyRequestNoList;
	}

	public void setDrpdSurveyNoList(List<SisuSeriyaDTO> drpdSurveyNoList) {
		this.drpdSurveyRequestNoList = drpdSurveyNoList;
	}

	public List<GamiSeriyaDTO> getDrpdRequestType() {
		return drpdRequestType;
	}

	public void setDrpdRequestType(List<GamiSeriyaDTO> drpdRequestType) {
		this.drpdRequestType = drpdRequestType;
	}

	public List<GamiSeriyaDTO> getTbl_RouteDetailsList() {
		return tbl_RouteDetailsList;
	}

	public void setTbl_RouteDetailsList(List<GamiSeriyaDTO> tbl_RouteDetailsList) {
		this.tbl_RouteDetailsList = tbl_RouteDetailsList;
	}

	public SurveyService getSurveyService() {
		return surveyService;
	}

	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}

	public List<SurveyDTO> getDrpdOrganizationList() {
		return drpdOrganizationList;
	}

	public void setDrpdOrganizationList(List<SurveyDTO> drpdOrganizationList) {
		this.drpdOrganizationList = drpdOrganizationList;
	}

	public PaymentVoucherService getPaymentVoucherService() {
		return paymentVoucherService;
	}

	public void setPaymentVoucherService(PaymentVoucherService paymentVoucherService) {
		this.paymentVoucherService = paymentVoucherService;
	}

	public void setDrpdDepatmentList(List<PaymentVoucherDTO> drpdDepatmentList) {
		this.drpdDepatmentList = drpdDepatmentList;
	}

	public List<PaymentVoucherDTO> getDrpdDepatmentList() {
		return drpdDepatmentList;
	}

	public GamiSeriyaDTO getSelectedRoute() {
		return selectedRoute;
	}

	public void setSelectedRoute(GamiSeriyaDTO selectedRoute) {
		this.selectedRoute = selectedRoute;
	}

	public boolean isRenderUpdate() {
		return renderUpdate;
	}

	public void setRenderUpdate(boolean renderUpdate) {
		this.renderUpdate = renderUpdate;
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

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public boolean isRenderPnlMain() {
		return renderPnlMain;
	}

	public void setRenderPnlMain(boolean renderPnlMain) {
		this.renderPnlMain = renderPnlMain;
	}

	public boolean isRenderBtnStatus() {
		return renderBtnStatus;
	}

	public void setRenderBtnStatus(boolean renderBtnStatus) {
		this.renderBtnStatus = renderBtnStatus;
	}

}
