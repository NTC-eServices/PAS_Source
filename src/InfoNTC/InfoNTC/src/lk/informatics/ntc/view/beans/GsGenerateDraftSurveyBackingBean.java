package lk.informatics.ntc.view.beans;

import java.io.IOException;
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
import lk.informatics.ntc.model.service.GamiSariyaService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "gsGenerateDraftSurveyBackingBean")
@ViewScoped
public class GsGenerateDraftSurveyBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private String selectedFormID;
	private List<FormDTO> formList;
	private FormDTO formDTO;
	private MidPointSurveyDTO midPointSurveyDTO;
	private List<IndicatorsDTO> tableIndicatorList;
	private List<GamiAnalyzedDataDTO> tblAnalyzedDataList;
	private boolean printCopies;
	private Date autoGenDate;
	private GamiSariyaService gamiSariyaService;
	private String errMessage;
	private boolean renderBtnBack;
	private String location;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	@PostConstruct
	public void init() {
		formDTO = new FormDTO();
		midPointSurveyDTO = new MidPointSurveyDTO();
		formList = new ArrayList<FormDTO>();
		tableIndicatorList = new ArrayList<IndicatorsDTO>();
		loadValues();

	}

	public void loadValues() {
		gamiSariyaService = (GamiSariyaService) SpringApplicationContex.getBean("gamiSariyaService");
		formList = gamiSariyaService.drpdFormIdList("SU003", "C");
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

		GenerateSurveyFormDTO tempt = new GenerateSurveyFormDTO();
		tempt.setSurveyNo(formDTO.getSurveyFormNo());

		gamiSariyaService.test();
		tempt = gamiSariyaService.getGamiFormDetails(tempt);

		formDTO.setFormId(tempt.getFormID());
		formDTO.setFormDescription(tempt.getFormDescription());
		formDTO.setSurveyFormNo(tempt.getSurveyNo());
		formDTO.setSurveyTypeDes(tempt.getSurveyType_des());
		formDTO.setSurveyMethodDes(tempt.getSurveyMethod_des());

		tableIndicatorList = gamiSariyaService.tblQuestionsWithAnswers(formDTO.getFormId());

	}

	public void saveAction() {

	}

	public void btnBack() {
		try {

			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("GAMI_GENERATE_SURVEY_BACK",
					"true");
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("/InfoNTC/pages/gamiSariya/gamiSeriyaGenerateSurveyForm.xhtml");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void btnPrint() {
		tblAnalyzedDataList = gamiSariyaService.getGamiAnalyzedData(formDTO);

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

}
