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

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.FormDTO;
import lk.informatics.ntc.model.dto.GamiAnalyzedDataDTO;
import lk.informatics.ntc.model.dto.GenerateSurveyFormDTO;
import lk.informatics.ntc.model.dto.IndicatorsDTO;
import lk.informatics.ntc.model.dto.MidPointSurveyDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.GamiSariyaService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "gsEnterSurveyDataBackingBean")
@ViewScoped
public class GsEnterSurveyData implements Serializable {

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
	public CommonService commonService;
	
	private String errMessage,alertMsg;
	
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
		
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		gamiSariyaService = (GamiSariyaService) SpringApplicationContex.getBean("gamiSariyaService");
		
		formList = gamiSariyaService.drpdFormIdList("SU005","O");
		
		FacesContext fcontext = FacesContext.getCurrentInstance();
		Object objCallerbackBtn = fcontext.getExternalContext().getSessionMap().get("GENERATE_GAMI_SURVEY");
		Object objCallerSurveyFormID = fcontext.getExternalContext().getSessionMap().get("SURVEY_GAMI_FORM_ID");
		Object objGamiSurveyNo = fcontext.getExternalContext().getSessionMap().get("GAMI_SURVEY_NO");
		if(objCallerbackBtn!=null){
			String backBtn=(String)objCallerbackBtn;	
			
			if(backBtn!=null && !backBtn.isEmpty() && backBtn.equalsIgnoreCase("true")) {
				renderBtnBack = true;
				String formID=(String)objCallerSurveyFormID;	
				formDTO.setSurveyFormNo((String)objGamiSurveyNo);
				selectedFormID = formID;
				onFormIdClick();
			}
		}
		
		fcontext.getExternalContext().getSessionMap().put("GENERATE_GAMI_SURVEY", "false");
	
	}
	
	public void onFormIdClick() {
		
		GenerateSurveyFormDTO tempt = new  GenerateSurveyFormDTO();
		tempt.setSurveyNo(formDTO.getSurveyFormNo());
	
		gamiSariyaService.test();
		tempt = gamiSariyaService.getGamiFormDetails(tempt);
		
		formDTO.setFormId(tempt.getFormID());
		formDTO.setFormDescription(tempt.getFormDescription());
		formDTO.setSurveyNo(tempt.getSurveyNo());
		formDTO.setSurveyTypeDes(tempt.getSurveyType_des());
		formDTO.setSurveyMethodDes(tempt.getSurveyMethod_des());
		
		if(!commonService. checkTaskOnSurveyHisDetails(tempt.getSurveyNo(), "SU005", "C")){
			commonService.updateSurveyTaskDetailsBySurveyNo("", tempt.getSurveyNo(),
					"SU006", "O", sessionBackingBean.getLoginUser());
		}
		
		tableIndicatorList = gamiSariyaService.tblQuestionsWithAnswers(formDTO.getFormId());
		
		midPointSurveyDTO = new MidPointSurveyDTO();
		
	}
	
	public void saveAction() {
		
	}
	
	public void btnBack() {
		try {
			
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("GAMI_GENERATE_SURVEY_BACK", "true");
			FacesContext.getCurrentInstance().getExternalContext().redirect("/InfoNTC/pages/gamiSariya/gamiSeriyaGenerateSurveyForm.xhtml");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void btnSave() {
		if(formDTO.getFormId()!=null &&! formDTO.getFormId().isEmpty() &&! formDTO.getFormId().equals("") ) {
		
		if (midPointSurveyDTO.getDirectinFrom()!=null && !midPointSurveyDTO.getDirectinFrom().isEmpty() &&! midPointSurveyDTO.getDirectinFrom().equals("")) {
			
			if (midPointSurveyDTO.getDirectionTo()!=null && !midPointSurveyDTO.getDirectionTo().isEmpty() && !midPointSurveyDTO.getDirectionTo().equals("") ) {
				
				if (midPointSurveyDTO.getNameOfRecorder()!=null && !midPointSurveyDTO.getNameOfRecorder().isEmpty()&&!midPointSurveyDTO.getNameOfRecorder().equals("")) {
					
					if (midPointSurveyDTO.getLocation()!=null &&!midPointSurveyDTO.getLocation().isEmpty()&&!midPointSurveyDTO.getLocation().equals("")) {
						
						if (midPointSurveyDTO.getDate()!=null) {
							
							if (midPointSurveyDTO.getTime()!=null) {
								
								if (midPointSurveyDTO.getRemarks()!=null&&!midPointSurveyDTO.getRemarks().isEmpty()&&!midPointSurveyDTO.getRemarks().equals("")) {
									
									midPointSurveyDTO.setFormId(formDTO.getFormId());
									
									// save form details
									String formId = gamiSariyaService.savaGamiFormDetails(midPointSurveyDTO,sessionBackingBean.loginUser);
									midPointSurveyDTO.setFormId(formId);
								
									if (formId!=null&&!formId.isEmpty()&&!formId.equals("")) {
										
										// save question answers
										boolean answersSaved = gamiSariyaService.saveGamiFormAnswers(tableIndicatorList,midPointSurveyDTO,sessionBackingBean.loginUser);
										
										if (answersSaved) {
											RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
											if (!commonService.checkTaskOnSurveyHisDetails(formDTO.getSurveyNo(),"SU006","O")) {
												commonService.updateSurveyTaskDetailsBySurveyNo("", formDTO.getSurveyNo(), "SU006", "C", sessionBackingBean.getLoginUser());
											}
											tableIndicatorList = gamiSariyaService.tblQuestionsWithAnswers(formDTO.getFormId());
											autoGenDate = null;
										} else {
											errMessage = "Question Answers did not saved.";
											RequestContext.getCurrentInstance().update("frm-search");
											RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
										}
									} else {
										errMessage = "Form Details did not saved.";
										RequestContext.getCurrentInstance().update("frm-search");
										RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
									}
									
									
								} else {
									errMessage = "Remarks should be inserted.";
									RequestContext.getCurrentInstance().update("frm-search");
									RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
								}
								
							} else {
								errMessage = "Time should be inserted.";
								RequestContext.getCurrentInstance().update("frm-search");
								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
							}
							
						}else {
							errMessage = "Date should be inserted.";
							RequestContext.getCurrentInstance().update("frm-search");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
						}
						
					} else {
						errMessage = "Location should be inserted.";
						RequestContext.getCurrentInstance().update("frm-search");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}
				} else {
					errMessage = "Name of the Recorder should be inserted.";
					RequestContext.getCurrentInstance().update("frm-search");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			} else {
				errMessage = "Direction To be inserted.";
				RequestContext.getCurrentInstance().update("frm-search");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		} else {
			errMessage = "Direction From should be inserted.";
			RequestContext.getCurrentInstance().update("frm-search");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
		} else {
			errMessage = "Form Id should be selected.";
			RequestContext.getCurrentInstance().update("frm-search");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

		
		
	}
	
	public void btnClearFormDetails() {
		midPointSurveyDTO = new MidPointSurveyDTO();
		tableIndicatorList = gamiSariyaService.tblQuestionsWithAnswers(formDTO.getFormId());
		autoGenDate = null;
	}
	
	public void btnClearForm() {
		autoGenDate = null;
		formDTO = new FormDTO();
		midPointSurveyDTO = new MidPointSurveyDTO();
		tableIndicatorList = gamiSariyaService.tblQuestionsWithAnswers(formDTO.getFormId());
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


	public CommonService getCommonService() {
		return commonService;
	}


	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

}
