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

import lk.informatics.ntc.model.dto.GamiSeriyaDTO;
import lk.informatics.ntc.model.dto.PaymentVoucherDTO;
import lk.informatics.ntc.model.dto.SurveyDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.GamiSariyaService;
import lk.informatics.ntc.model.service.PaymentVoucherService;
import lk.informatics.ntc.model.service.SurveyService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "gsInitiateSurveyProcessBean")
@ViewScoped
public class GsInitiateSurveyProcessBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private GamiSeriyaDTO gamiSeriyaDTO;
	private GamiSeriyaDTO selectedRoute;

	private List<GamiSeriyaDTO> drpdRequestNoList;
	private List<GamiSeriyaDTO> drpdSurveyRequestNoList;
	private List<SurveyDTO> drpdOrganizationList;
	private List<PaymentVoucherDTO> drpdDepatmentList;
	private List<GamiSeriyaDTO> drpdRequestType;
	private List<GamiSeriyaDTO> tbl_RouteDetailsList;
	private List<GamiSeriyaDTO> drpdServiceTypeList;
	private List<GamiSeriyaDTO> drpdServiceMethodList;
	private List<GamiSeriyaDTO> drpdSurveyNoList;

	private GamiSariyaService gamiSariyaService;
	private SurveyService surveyService;
	private PaymentVoucherService paymentVoucherService;
	private CommonService commonService;

	private boolean renderUpdate;
	private boolean renderPnlMain;
	private boolean renderbtnStatus = true;
	private String errorMsg;
	private String alertMsg;
	private boolean renderTable;

	@PostConstruct
	public void init() {

		gamiSeriyaDTO = new GamiSeriyaDTO();

		drpdRequestNoList = new ArrayList<GamiSeriyaDTO>();
		drpdSurveyRequestNoList = new ArrayList<GamiSeriyaDTO>();
		drpdOrganizationList = new ArrayList<SurveyDTO>();
		drpdDepatmentList = new ArrayList<PaymentVoucherDTO>();
		drpdRequestType = new ArrayList<GamiSeriyaDTO>();
		tbl_RouteDetailsList = new ArrayList<GamiSeriyaDTO>();
		drpdServiceMethodList = new ArrayList<GamiSeriyaDTO>();
		drpdServiceTypeList = new ArrayList<GamiSeriyaDTO>();
		drpdSurveyNoList = new ArrayList<GamiSeriyaDTO>();

		renderUpdate = false;

		gamiSariyaService = (GamiSariyaService) SpringApplicationContex.getBean("gamiSariyaService");
		surveyService = (SurveyService) SpringApplicationContex.getBean("surveyService");
		paymentVoucherService = (PaymentVoucherService) SpringApplicationContex.getBean("paymentVoucherService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");

		loadValues();
	}

	public void loadValues() {

		drpdRequestNoList = gamiSariyaService.drpdRequestNoListForSurveyProcess();
		drpdSurveyRequestNoList = gamiSariyaService.drpdSurveyProcessRequestNoList();
		drpdOrganizationList = surveyService.getOrganizationListDropDown();
		drpdDepatmentList = paymentVoucherService.getDepartment();
		drpdRequestType = gamiSariyaService.drpdRequestType();
		drpdServiceTypeList = gamiSariyaService.drpdServiceTypeList();
		drpdServiceMethodList = gamiSariyaService.drpdServiceMethodsList();
		drpdSurveyNoList = gamiSariyaService.drpdSurveyNoList();

	}

	public void onRequestNoChange() {

		renderPnlMain = false;
		String temptReqNo = gamiSeriyaDTO.getRequestNo();
		btnClear();
		tbl_RouteDetailsList = new ArrayList<GamiSeriyaDTO>();

		gamiSeriyaDTO.setRequestNo(temptReqNo);
		gamiSeriyaDTO.setSurveyRequestNo(null);
	}

	public void onSurveyNoChange() {

		String temptSurNo = gamiSeriyaDTO.getSurveyNo();
		gamiSeriyaDTO = new GamiSeriyaDTO();
		gamiSeriyaDTO.setSurveyRequestNo(temptSurNo);

		for (GamiSeriyaDTO surNo : drpdSurveyNoList) {

			if (gamiSeriyaDTO.getSurveyNo().equals(surNo.getSurveyNo())) {
				gamiSeriyaDTO.setSurveyRequestNo(surNo.getSurveyRequestNo());
			}

		}

	}

	public void onSurveyReqChange() {

		renderPnlMain = false;
		renderTable = false;
		String temptSurReqNo = gamiSeriyaDTO.getSurveyRequestNo();
		gamiSeriyaDTO = new GamiSeriyaDTO();
		gamiSeriyaDTO.setSurveyRequestNo(temptSurReqNo);

	}

	public void btnSearch() {

		renderPnlMain = true;
		renderUpdate = false;

		// search by Request no
		if (gamiSeriyaDTO.getRequestNo() != null && !gamiSeriyaDTO.getRequestNo().isEmpty()
				&& !gamiSeriyaDTO.getRequestNo().equalsIgnoreCase("")) {

			renderTable = true;
			gamiSeriyaDTO = gamiSariyaService.getRequestDetailsForInitiateByRequestNo(gamiSeriyaDTO);
			tbl_RouteDetailsList = gamiSariyaService.tblOriginDestionDetailsByRequestNo(gamiSeriyaDTO);

			// Search by Survey Request No
		} else if (gamiSeriyaDTO.getSurveyRequestNo() != null && !gamiSeriyaDTO.getSurveyRequestNo().isEmpty()
				&& !gamiSeriyaDTO.getSurveyRequestNo().equalsIgnoreCase("")) {

			gamiSeriyaDTO = gamiSariyaService.getInitiateDetailsByServeyReqNo(gamiSeriyaDTO);
			if (gamiSeriyaDTO.getStatus() != null
					&& (gamiSeriyaDTO.getStatus().equals("A") || gamiSeriyaDTO.getStatus().equals("R"))) {
				renderbtnStatus = false;
			} else {
				renderbtnStatus = true;
			}

			if (gamiSeriyaDTO.getSurveyNo() != null && !gamiSeriyaDTO.getSurveyNo().isEmpty()
					&& !gamiSeriyaDTO.getSurveyNo().equalsIgnoreCase("")) {
				renderUpdate = true;
			} else {
				renderUpdate = false;
			}

			renderTable = false;

		} else {

			errorMsg = "Request Number or Survey Request No. should be selected.";
			RequestContext.getCurrentInstance().update("frm-serviceInformation");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void btnClear() {
		renderPnlMain = false;
		gamiSeriyaDTO = new GamiSeriyaDTO();
		tbl_RouteDetailsList = new ArrayList<GamiSeriyaDTO>();

	}

	/**
	 * Buttons
	 */

	public void onRowSelect(SelectEvent event) {
		renderUpdate = false;
		gamiSeriyaDTO.setSurveyRequestNo(null);

		gamiSeriyaDTO.setRouteSeqNo(((GamiSeriyaDTO) event.getObject()).getRouteSeqNo().toString());

		String temptSurveyReqNo = null;
		if (((GamiSeriyaDTO) event.getObject()).getSurveyRequestNo() != null) {

			temptSurveyReqNo = ((GamiSeriyaDTO) event.getObject()).getSurveyRequestNo().toString();

		}

		if (temptSurveyReqNo != null && !temptSurveyReqNo.isEmpty() && !temptSurveyReqNo.equalsIgnoreCase("")) {

			gamiSeriyaDTO.setSurveyRequestNo(temptSurveyReqNo);
			gamiSeriyaDTO = gamiSariyaService.getInitiateDetailsByServeyReqNo(gamiSeriyaDTO);

			if (gamiSeriyaDTO.getStatus() != null
					&& (gamiSeriyaDTO.getStatus().equals("A") || gamiSeriyaDTO.getStatus().equals("R"))) {
				renderbtnStatus = false;
			} else {
				renderbtnStatus = true;
			}

			if (gamiSeriyaDTO.getSurveyNo() != null && !gamiSeriyaDTO.getSurveyNo().isEmpty()
					&& !gamiSeriyaDTO.getSurveyNo().equalsIgnoreCase("")) {
				renderUpdate = true;
			}
		}

	}

	public void onRowUnselect(UnselectEvent event) {
		gamiSeriyaDTO.setSurveyRequestNo(null);
		gamiSeriyaDTO.setOrganizationCode(null);
		gamiSeriyaDTO.setDepartmentCode(null);
		gamiSeriyaDTO.setReasonForSurvey(null);
	}

	public void btnSave() {

		if (gamiSeriyaDTO.getSurveyRequestNo() != null && !gamiSeriyaDTO.getSurveyRequestNo().isEmpty()
				&& !gamiSeriyaDTO.getSurveyRequestNo().equals("")) {
			gamiSeriyaDTO = gamiSariyaService.insertSurveyProcessDet(gamiSeriyaDTO);

			if (gamiSeriyaDTO.getSurveyNo() != null && !gamiSeriyaDTO.getSurveyNo().isEmpty()
					&& !gamiSeriyaDTO.getSurveyNo().equalsIgnoreCase("")) {

				if (!renderUpdate) {
					commonService.updateSurveyTaskDetails(gamiSeriyaDTO.getSurveyRequestNo(),
							gamiSeriyaDTO.getSurveyNo(), "SU002", "O", sessionBackingBean.getLoginUser());
					commonService.updateSurveyTaskDetails(gamiSeriyaDTO.getSurveyRequestNo(),
							gamiSeriyaDTO.getSurveyNo(), "SU002", "C", sessionBackingBean.getLoginUser());

					renderUpdate = true;

					tbl_RouteDetailsList = gamiSariyaService.tblOriginDestionDetailsByRequestNo(gamiSeriyaDTO);
				}

				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
			} else {
				errorMsg = "Data did not saved.";
				RequestContext.getCurrentInstance().update("frm-search");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		} else {
			errorMsg = "Select Survey Request No. from the grid to continue.";
			RequestContext.getCurrentInstance().update("frm-serviceInformation");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void btnCancel() {

	}

	public void btnDocumentMangement() {

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

	public GamiSeriyaDTO getGamiSeriyaDTO() {
		return gamiSeriyaDTO;
	}

	public void setGamiSeriyaDTO(GamiSeriyaDTO gamiSeriyaDTO) {
		this.gamiSeriyaDTO = gamiSeriyaDTO;
	}

	public GamiSariyaService getGamiSariyaService() {
		return gamiSariyaService;
	}

	public void setGamiSariyaService(GamiSariyaService gamiSariyaService) {
		this.gamiSariyaService = gamiSariyaService;
	}

	public GamiSeriyaDTO getSelectedRoute() {
		return selectedRoute;
	}

	public void setSelectedRoute(GamiSeriyaDTO selectedRoute) {
		this.selectedRoute = selectedRoute;
	}

	public List<GamiSeriyaDTO> getDrpdRequestNoList() {
		return drpdRequestNoList;
	}

	public void setDrpdRequestNoList(List<GamiSeriyaDTO> drpdRequestNoList) {
		this.drpdRequestNoList = drpdRequestNoList;
	}

	public List<GamiSeriyaDTO> getDrpdSurveyRequestNoList() {
		return drpdSurveyRequestNoList;
	}

	public void setDrpdSurveyRequestNoList(List<GamiSeriyaDTO> drpdSurveyRequestNoList) {
		this.drpdSurveyRequestNoList = drpdSurveyRequestNoList;
	}

	public List<SurveyDTO> getDrpdOrganizationList() {
		return drpdOrganizationList;
	}

	public void setDrpdOrganizationList(List<SurveyDTO> drpdOrganizationList) {
		this.drpdOrganizationList = drpdOrganizationList;
	}

	public List<PaymentVoucherDTO> getDrpdDepatmentList() {
		return drpdDepatmentList;
	}

	public void setDrpdDepatmentList(List<PaymentVoucherDTO> drpdDepatmentList) {
		this.drpdDepatmentList = drpdDepatmentList;
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

	public PaymentVoucherService getPaymentVoucherService() {
		return paymentVoucherService;
	}

	public void setPaymentVoucherService(PaymentVoucherService paymentVoucherService) {
		this.paymentVoucherService = paymentVoucherService;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	public List<GamiSeriyaDTO> getDrpdSurveyNoList() {
		return drpdSurveyNoList;
	}

	public void setDrpdSurveyNoList(List<GamiSeriyaDTO> drpdSurveyNoList) {
		this.drpdSurveyNoList = drpdSurveyNoList;
	}

	public boolean isRenderTable() {
		return renderTable;
	}

	public void setRenderTable(boolean renderTable) {
		this.renderTable = renderTable;
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

	public boolean isRenderbtnStatus() {
		return renderbtnStatus;
	}

	public void setRenderbtnStatus(boolean renderbtnStatus) {
		this.renderbtnStatus = renderbtnStatus;
	}

}
