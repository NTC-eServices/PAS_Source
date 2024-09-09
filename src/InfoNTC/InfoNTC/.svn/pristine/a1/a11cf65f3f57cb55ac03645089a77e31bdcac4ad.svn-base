package lk.informatics.ntc.view.beans;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.SurveyDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.SurveyService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "initiateSurveyProcessBackingBean")
@ViewScoped
public class InitiateSurveyProcessBackingBean {
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	// services
	private SurveyService surveyService;
	private CommonService commonService;

	// DTO

	private SurveyDTO surveyDTO;
	private SurveyDTO upadateRouteDetails;
	private SurveyDTO upadateNewRouteDetails;
	private List<SurveyDTO> surveyRequestNoList = new ArrayList<SurveyDTO>();
	private List<SurveyDTO> surveyTypeList = new ArrayList<SurveyDTO>();
	private List<SurveyDTO> surveyMethodList = new ArrayList<SurveyDTO>();

	private String errorMessage;
	private String successMessage;
	private String currentDate;
	private String organization, deparment, reqType, routNo, showSurveyNo;
	SurveyDTO surReason;
	private String selectSurveyType, selectSurveyMethod, selectremarks, selectSurveyNo, selectRequestDate,
			selectSurReqNo, selectVia, selectDestination, selecteOrigin, selectServiceType;
	private boolean disabledReqNo = false;

	public InitiateSurveyProcessBackingBean() {
		surveyService = (SurveyService) SpringApplicationContex.getBean("surveyService");

	}

	@PostConstruct
	public void init() {
		surveyService = (SurveyService) SpringApplicationContex.getBean("surveyService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		surveyDTO = new SurveyDTO();
		surveyRequestNoList = surveyService.getSurveyReqOnGoingNoDropDown();
		surveyTypeList = surveyService.getSurveyTypeToDropDown();
		surveyMethodList = surveyService.getSurveyMethodToDropDown();
		LocalDate localDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		currentDate = localDate.format(formatter);
	}

	public void onSurveyRequestNoChange() {
		if (surveyDTO.getRequestNo() != null) {
			for (SurveyDTO dto : surveyRequestNoList) {
				if (surveyDTO.getRequestNo().equals(dto.getRequestNo())) {
					surveyDTO.setSurveyNo(dto.getSurveyNo());
					surveyDTO.setRequestDate(dto.getRequestDate());
					break;
				}
			}
		} else {
			surveyDTO = new SurveyDTO();
		}
	}

	// fill details when enter the survey request No
	public void fillOrganization() {

		organization = surveyService.fillOrga(surveyDTO.getRequestNo());
		surveyDTO.setOrganisationCode(organization);

	}

	public void fillDepartment() {
		deparment = surveyService.fillDepart(surveyDTO.getRequestNo());
		surveyDTO.setDepartmentCode(deparment);

	}

	public void fillRequestType() {

		reqType = surveyService.fillRequestType(surveyDTO.getRequestNo());
		surveyDTO.setRequestType(reqType);

	}

	public void fillReasonSurvey() {
		surReason = surveyService.fillSUrveyReason(surveyDTO.getRequestNo());
		surveyDTO.setSurveyReason(surReason.getSurveyReason());
		surveyDTO.setServiceType(surReason.getServiceType());
		selectServiceType = surReason.getServiceType();
		surveyDTO.setBusFare(surReason.getBusFare());

	}

	public void fillRouteNo() {
		routNo = surveyService.fillRouteNo(surveyDTO.getRequestNo());
		surveyDTO.setRouteNo(routNo);

	}

	public void fillRouteDetails() {
		upadateRouteDetails = surveyService.fillRouteDetails(surveyDTO.getRequestNo());
		surveyDTO.setVia(upadateRouteDetails.getVia());
		surveyDTO.setOrigin(upadateRouteDetails.getOrigin());
		surveyDTO.setDestination(upadateRouteDetails.getDestination());
		surveyDTO.setRouteAvailable(upadateRouteDetails.getRouteAvailable());

	}

	public void fillNewRouteDetails() {

		upadateNewRouteDetails = surveyService.fillNewRouteDetails(surveyDTO.getRequestNo());
		surveyDTO.setViaNew(upadateNewRouteDetails.getViaNew());
		surveyDTO.setOriginNew(upadateNewRouteDetails.getOriginNew());
		surveyDTO.setDestinationNew(upadateNewRouteDetails.getDestinationNew());
		surveyDTO.setRouteNoNew(upadateNewRouteDetails.getRouteNoNew());

	}

	public void fillTaskdet() {
		surveyDTO.setSurveyNo(null);
		surveyDTO.setSurveyMethodCode(null);
		surveyDTO.setSurveyTypeCode(null);
		surveyDTO.setRemarks(null);
		commonService.updateSurveyTaskDetails(surveyDTO.getRequestNo(), null, "SU002", "O",
				sessionBackingBean.getLoginUser());
	}

	public void clearSearchDetail() {

	}

	@SuppressWarnings("deprecation")
	public void save() {

		surveyDTO.setLoginuser(sessionBackingBean.getLoginUser());
		if (surveyDTO.getSurveyMethod() != null && !surveyDTO.getSurveyMethod().isEmpty()
				&& surveyDTO.getSurveyType() != null && !surveyDTO.getSurveyType().isEmpty()
				&& surveyDTO.getRemarks() != null && !surveyDTO.getRemarks().isEmpty()) {

			showSurveyNo = surveyService.saveSurveyData(surveyDTO);

			commonService.updateSurveyTaskDetails(surveyDTO.getRequestNo(), showSurveyNo, "SU002", "C",
					sessionBackingBean.getLoginUser());

			disabledReqNo = true;
			if (surveyDTO.getSurveyNo() != null && !surveyDTO.getSurveyNo().trim().isEmpty()) {
				successMessage = "Succesfully updated.";
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
			} else {
				successMessage = "Succesfully saved.";
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
				surveyDTO.setSurveyNo(showSurveyNo);
			}

		} else if (surveyDTO.getRequestNo() == null || surveyDTO.getRequestNo().isEmpty()) {
			setErrorMessage("Please select Survey Request No.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		} else if (surveyDTO.getSurveyType() == null || surveyDTO.getSurveyType().isEmpty()) {
			setErrorMessage("Please select Survey Type.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		} else if (surveyDTO.getSurveyMethod() == null || surveyDTO.getSurveyMethod().isEmpty()) {
			setErrorMessage("Please select Survey Method.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		} else if (surveyDTO.getRemarks() == null || surveyDTO.getRemarks().isEmpty()) {
			setErrorMessage("Please enter Remarks.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void clearSurDet() {
		surveyDTO = new SurveyDTO();
		selectSurveyType = null;
		selectSurveyMethod = null;
		selectremarks = null;
		surveyRequestNoList = surveyService.getSurveyReqOnGoingNoDropDown();
		disabledReqNo = false;
	}

	public List<SurveyDTO> getSurveyMethodList() {
		return surveyMethodList;
	}

	public void setSurveyMethodList(List<SurveyDTO> surveyMethodList) {
		this.surveyMethodList = surveyMethodList;
	}

	public SurveyService getSurveyService() {
		return surveyService;
	}

	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}

	public SurveyDTO getSurveyDTO() {
		return surveyDTO;
	}

	public void setSurveyDTO(SurveyDTO surveyDTO) {
		this.surveyDTO = surveyDTO;
	}

	public List<SurveyDTO> getSurveyRequestNoList() {
		return surveyRequestNoList;
	}

	public void setSurveyRequestNoList(List<SurveyDTO> surveyRequestNoList) {
		this.surveyRequestNoList = surveyRequestNoList;
	}

	public List<SurveyDTO> getSurveyTypeList() {
		return surveyTypeList;
	}

	public void setSurveyTypeList(List<SurveyDTO> surveyTypeList) {
		this.surveyTypeList = surveyTypeList;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	public String getSelectSurveyType() {
		return selectSurveyType;
	}

	public void setSelectSurveyType(String selectSurveyType) {
		this.selectSurveyType = selectSurveyType;
	}

	public String getSelectSurveyMethod() {
		return selectSurveyMethod;
	}

	public void setSelectSurveyMethod(String selectSurveyMethod) {
		this.selectSurveyMethod = selectSurveyMethod;
	}

	public String getSelectremarks() {
		return selectremarks;
	}

	public void setSelectremarks(String selectremarks) {
		this.selectremarks = selectremarks;
	}

	public String getSelectSurveyNo() {
		return selectSurveyNo;
	}

	public void setSelectSurveyNo(String selectSurveyNo) {
		this.selectSurveyNo = selectSurveyNo;
	}

	public String getSelectRequestDate() {
		return selectRequestDate;
	}

	public void setSelectRequestDate(String selectRequestDate) {
		this.selectRequestDate = selectRequestDate;
	}

	public String getSelectSurReqNo() {
		return selectSurReqNo;
	}

	public void setSelectSurReqNo(String selectSurReqNo) {
		this.selectSurReqNo = selectSurReqNo;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getDeparment() {
		return deparment;
	}

	public void setDeparment(String deparment) {
		this.deparment = deparment;
	}

	public String getRequestType() {
		return reqType;
	}

	public void setRequestType(String requestType) {
		this.reqType = requestType;
	}

	public String getSelectVia() {
		return selectVia;
	}

	public void setSelectVia(String selectVia) {
		this.selectVia = selectVia;
	}

	public String getSelectDestination() {
		return selectDestination;
	}

	public void setSelectDestination(String selectDestination) {
		this.selectDestination = selectDestination;
	}

	public String getSelecteOrigin() {
		return selecteOrigin;
	}

	public void setSelecteOrigin(String selecteOrigin) {
		this.selecteOrigin = selecteOrigin;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public boolean isDisabledReqNo() {
		return disabledReqNo;
	}

	public void setDisabledReqNo(boolean disabledReqNo) {
		this.disabledReqNo = disabledReqNo;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public String getSelectServiceType() {
		return selectServiceType;
	}

	public void setSelectServiceType(String selectServiceType) {
		this.selectServiceType = selectServiceType;
	}

}
