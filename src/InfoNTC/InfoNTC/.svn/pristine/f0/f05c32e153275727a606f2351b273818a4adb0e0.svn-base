package lk.informatics.ntc.view.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import lk.informatics.ntc.model.dto.FormDTO;
import lk.informatics.ntc.model.dto.GamiAnalyzedDataDTO;
import lk.informatics.ntc.model.dto.GenerateSurveyFormDTO;
import lk.informatics.ntc.model.dto.IndicatorsDTO;
import lk.informatics.ntc.model.dto.MidPointSurveyDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.GamiSariyaService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "gsAnalyzeDataBackingBean")
@ViewScoped
public class GsAnalyzeSurveyDataBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String selectedFormID;
	private GenerateSurveyFormDTO tempt;
	private List<FormDTO> formList;
	private FormDTO formDTO;
	private MidPointSurveyDTO midPointSurveyDTO;

	private List<IndicatorsDTO> tableIndicatorList;
	private List<GamiAnalyzedDataDTO> tblAnalyzedDataList;
	private boolean printCopies;
	private Date autoGenDate;

	private GamiSariyaService gamiSariyaService;
	public CommonService commonService;

	private String errMessage, alertMsg;

	private boolean renderBtnBack;
	private boolean renderAnalyzedData;

	private String location;
	private int noOfApplications;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	@PostConstruct
	public void init() {

		noOfApplications = 0;
		formDTO = new FormDTO();
		tempt = new GenerateSurveyFormDTO();
		midPointSurveyDTO = new MidPointSurveyDTO();

		formList = new ArrayList<FormDTO>();
		tableIndicatorList = new ArrayList<IndicatorsDTO>();
		tblAnalyzedDataList = new ArrayList<GamiAnalyzedDataDTO>();
		loadValues();

	}

	public void loadValues() {

		gamiSariyaService = (GamiSariyaService) SpringApplicationContex.getBean("gamiSariyaService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");

		formList = gamiSariyaService.drpdFormIdList("SU006", "O");

		FacesContext fcontext = FacesContext.getCurrentInstance();
		Object objCallerbackBtn = fcontext.getExternalContext().getSessionMap().get("GENERATE_GAMI_SURVEY");
		Object objCallerSurveyFormID = fcontext.getExternalContext().getSessionMap().get("SURVEY_GAMI_FORM_ID");
		Object objGamiSurveyNo = fcontext.getExternalContext().getSessionMap().get("GAMI_SURVEY_NO");
		if (objCallerbackBtn != null) {
			String backBtn = (String) objCallerbackBtn;

			if (backBtn != null && !backBtn.isEmpty() && backBtn.equalsIgnoreCase("true")) {
				renderBtnBack = true;
				String formID = (String) objCallerSurveyFormID;
				formDTO.setSurveyFormNo((String) objGamiSurveyNo);
				selectedFormID = formID;
				onFormIdClick();
			}
		}

		fcontext.getExternalContext().getSessionMap().put("GENERATE_GAMI_SURVEY", "false");

	}

	public void onFormIdClick() {

		tempt = new GenerateSurveyFormDTO();
		tempt.setSurveyNo(formDTO.getSurveyFormNo());

		gamiSariyaService.test();
		tempt = gamiSariyaService.getGamiFormDetails(tempt);

		formDTO.setFormId(tempt.getFormID());
		formDTO.setFormDescription(tempt.getFormDescription());
		formDTO.setSurveyFormNo(tempt.getSurveyNo());
		formDTO.setSurveyTypeDes(tempt.getSurveyType_des());
		formDTO.setSurveyMethodDes(tempt.getSurveyMethod_des());

		if (!commonService.checkTaskOnSurveyHisDetails(tempt.getSurveyNo(), "SU006", "C")) {
			commonService.updateSurveyTaskDetailsBySurveyNo("", tempt.getSurveyNo(), "SU007", "O",
					sessionBackingBean.getLoginUser());
		}

		tableIndicatorList = gamiSariyaService.tblQuestionsWithAnswers(formDTO.getFormId());

		midPointSurveyDTO = new MidPointSurveyDTO();

		renderAnalyzedData = false;

	}

	public void btnClearFormDetails() {
		midPointSurveyDTO = new MidPointSurveyDTO();
		tableIndicatorList = gamiSariyaService.tblQuestionsWithAnswers(formDTO.getFormId());
	}

	public void btnAnalyzeData() {

		noOfApplications = gamiSariyaService.getGamiNumberOfFormApplications(formDTO);
		tblAnalyzedDataList = gamiSariyaService.getGamiAnalyzedData(formDTO);
		renderAnalyzedData = true;

		if (!commonService.checkTaskOnSurveyHisDetails(tempt.getSurveyNo(), "SU007", "O")) {
			commonService.updateSurveyTaskDetailsBySurveyNo("", tempt.getSurveyNo(), "SU007", "C",
					sessionBackingBean.getLoginUser());
		}

	}

	public void btnClearForm() {

		formDTO = new FormDTO();
		midPointSurveyDTO = new MidPointSurveyDTO();
		tableIndicatorList = gamiSariyaService.tblQuestionsWithAnswers(formDTO.getFormId());
		tblAnalyzedDataList = new ArrayList<GamiAnalyzedDataDTO>();
		renderAnalyzedData = false;
	}

	public void btnDownloadData() {

	}

	public void btnPrintData() {

	}

	// getters and setters

	public String getSelectedFormID() {
		return selectedFormID;
	}

	public void setSelectedFormID(String selectedFormID) {
		this.selectedFormID = selectedFormID;
	}

	public List<FormDTO> getFormList() {
		return formList;
	}

	public void setFormList(List<FormDTO> formList) {
		this.formList = formList;
	}

	public FormDTO getFormDTO() {
		return formDTO;
	}

	public void setFormDTO(FormDTO formDTO) {
		this.formDTO = formDTO;
	}

	public List<IndicatorsDTO> getTableIndicatorList() {
		return tableIndicatorList;
	}

	public void setTableIndicatorList(List<IndicatorsDTO> tableIndicatorList) {
		this.tableIndicatorList = tableIndicatorList;
	}

	public boolean isPrintCopies() {
		return printCopies;
	}

	public void setPrintCopies(boolean printCopies) {
		this.printCopies = printCopies;
	}

	public String getErrMessage() {
		return errMessage;
	}

	public void setErrMessage(String errMessage) {
		this.errMessage = errMessage;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
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

	public Date getAutoGenDate() {
		return autoGenDate;
	}

	public void setAutoGenDate(Date autoGenDate) {
		this.autoGenDate = autoGenDate;
	}

	public boolean isRenderBtnBack() {
		return renderBtnBack;
	}

	public void setRenderBtnBack(boolean renderBtnBack) {
		this.renderBtnBack = renderBtnBack;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public MidPointSurveyDTO getMidPointSurveyDTO() {
		return midPointSurveyDTO;
	}

	public void setMidPointSurveyDTO(MidPointSurveyDTO midPointSurveyDTO) {
		this.midPointSurveyDTO = midPointSurveyDTO;
	}

	public List<GamiAnalyzedDataDTO> getTblAnalyzedDataList() {
		return tblAnalyzedDataList;
	}

	public void setTblAnalyzedDataList(List<GamiAnalyzedDataDTO> tblAnalyzedDataList) {
		this.tblAnalyzedDataList = tblAnalyzedDataList;
	}

	public String getAlertMsg() {
		return alertMsg;
	}

	public void setAlertMsg(String alertMsg) {
		this.alertMsg = alertMsg;
	}

	public boolean isRenderAnalyzedData() {
		return renderAnalyzedData;
	}

	public void setRenderAnalyzedData(boolean renderAnalyzedData) {
		this.renderAnalyzedData = renderAnalyzedData;
	}

	public int getNoOfApplications() {
		return noOfApplications;
	}

	public void setNoOfApplications(int noOfApplications) {
		this.noOfApplications = noOfApplications;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public GenerateSurveyFormDTO getTempt() {
		return tempt;
	}

	public void setTempt(GenerateSurveyFormDTO tempt) {
		this.tempt = tempt;
	}

}
