package lk.informatics.ntc.view.beans;

import java.math.BigDecimal;
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
import lk.informatics.ntc.model.service.SurveyService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "editInitiateSurveyProcessBackingBean")
@ViewScoped
public class EditInitiateSurveyProcessBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	// services
	private SurveyService surveyService;

	// DTO

	private SurveyDTO surveyDTO;
	public SurveyDTO showDataDTO;

	private List<SurveyDTO> surveyRequestNoList = new ArrayList<SurveyDTO>();
	private List<SurveyDTO> surveyNoList = new ArrayList<SurveyDTO>();
	private List<SurveyDTO> surveyTypeList = new ArrayList<SurveyDTO>();
	private List<SurveyDTO> surveyMethodList = new ArrayList<SurveyDTO>();

	private String errorMessage;
	private String successMessage;
	private String currentDate;
	private SurveyDTO showSurveyReqNo;
	SurveyDTO showSurveyNo;
	private String selectSurveyType, selectSurveyMethod, selectremarks, selectrequestNo, selectRouteNoNew,
			selectSurveyNo, selectRequestDate, selectSurReqNo, selectOrganization, selectDepartmrnt, selectRequestType,
			selectRouteNo, selectVia, selectDestination, selecteOrigin, selectViaNew, selectDestinationNew,
			selecteOriginNew, selectSurveyReason, selectServiceType, serviceType, busFare;
	public BigDecimal SelectbusFare;
	private String selectRouteAvailable;
	private String loginuser;
	private boolean dissableReqSurNO, dissableSurNO = false;

	public EditInitiateSurveyProcessBackingBean() {
		surveyService = (SurveyService) SpringApplicationContex.getBean("surveyService");

	}

	@PostConstruct
	public void init() {
		surveyService = (SurveyService) SpringApplicationContex.getBean("surveyService");
		surveyDTO = new SurveyDTO();
		surveyRequestNoList = surveyService.getSurveyReqNoDropDown();
		surveyNoList = surveyService.getSurveyNoDropDown();
		surveyTypeList = surveyService.getSurveyTypeToDropDown();
		surveyMethodList = surveyService.getSurveyMethodToDropDown();
		LocalDate localDate = LocalDate.now();// For reference
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		currentDate = localDate.format(formatter);
	}

	public void fillSurveyNo() {

		showSurveyNo = surveyService.fillSurNo(selectrequestNo);

		selectSurveyNo = showSurveyNo.getSurveyNo();
		selectRequestDate = showSurveyNo.getRequestDate();

	}

	public void fillSurveyReqNo() {

		showSurveyReqNo = surveyService.fillSurReq(selectSurveyNo);

		selectrequestNo = showSurveyReqNo.getRequestNo();
		selectRequestDate = showSurveyReqNo.getRequestDate();

	}

	@SuppressWarnings("deprecation")
	public void searchDetail() {

		if (selectrequestNo == null || selectrequestNo.isEmpty() && selectSurveyNo == null
				|| selectSurveyNo.isEmpty()) {

			setErrorMessage("Please enter data for search.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		}

		else {
			showDataDTO = surveyService.showDetails(selectrequestNo, selectSurveyNo);
			selectOrganization = showDataDTO.getOrganisationDescription();
			selectDepartmrnt = showDataDTO.getDepartmentDescription();
			selectRequestType = showDataDTO.getRequestTypeDescription();
			selectRouteNo = showDataDTO.getRouteNo();
			selectVia = showDataDTO.getVia();
			selectDestination = showDataDTO.getDestination();
			selecteOrigin = showDataDTO.getOrigin();
			selectViaNew = showDataDTO.getViaNew();
			selectDestinationNew = showDataDTO.getDestinationNew();
			selecteOriginNew = showDataDTO.getOriginNew();
			selectSurveyReason = showDataDTO.getSurveyReason();
			selectSurveyType = showDataDTO.getSurveyTypeCode();
			selectSurveyMethod = showDataDTO.getSurveyMethodCode();
			selectremarks = showDataDTO.getRemarks();
			selectRouteNoNew = showDataDTO.getRouteNoNew();
			selectRouteAvailable = showDataDTO.getRouteAvailable();
			selectServiceType = showDataDTO.getServiceType();
			SelectbusFare = showDataDTO.getBusFare();

			dissableReqSurNO = true;
			dissableSurNO = true;

		}

	}

	public void clearSearchDetail() {
		surveyDTO = new SurveyDTO();
		showDataDTO = new SurveyDTO();
		selectrequestNo = null;
		selectSurveyNo = null;
		selectOrganization = null;
		selectDepartmrnt = null;
		selectRequestType = null;
		selectRouteNo = null;
		selectRouteNoNew = null;
		selectVia = null;
		selectDestination = null;
		selecteOrigin = null;
		selectViaNew = null;
		selectDestinationNew = null;
		selecteOriginNew = null;
		selectSurveyReason = null;
		selectSurveyType = null;
		selectSurveyMethod = null;
		selectremarks = null;
		selectRouteAvailable = null;
		selectRequestDate = null;
		dissableReqSurNO = false;
		dissableSurNO = false;
		selectServiceType = null;
		SelectbusFare = null;

	}

	@SuppressWarnings("deprecation")
	public void save() {
		if (selectSurveyMethod != null && !selectSurveyMethod.isEmpty() && selectSurveyType != null
				&& !selectSurveyType.isEmpty() && selectremarks != null && !selectremarks.isEmpty()) {
			loginuser = sessionBackingBean.getLoginUser();
			surveyService.saveEditedSurveyData(selectSurveyType, selectSurveyMethod, selectremarks, selectrequestNo,
					selectSurveyNo, loginuser);

			successMessage = "Succesfully saved.";
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
			surveyRequestNoList = surveyService.getSurveyReqNoDropDown();
		} else if (selectSurveyMethod == null || selectSurveyMethod.isEmpty()) {

			setErrorMessage("Please select Survey Method.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		} else if (selectSurveyType == null || selectSurveyType.isEmpty()) {

			setErrorMessage("Please select Survey Type.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		} else if (selectremarks == null || selectremarks.isEmpty()) {

			setErrorMessage("Please enter Remarks.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void clearSurDet() {

		selectSurveyType = showDataDTO.getSurveyTypeCode();
		selectSurveyMethod = showDataDTO.getSurveyMethodCode();
		selectremarks = showDataDTO.getRemarks();
		clearSearchDetail();
		dissableReqSurNO = false;
		dissableSurNO = false;

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

	public List<SurveyDTO> getSurveyNoList() {
		return surveyNoList;
	}

	public void setSurveyNoList(List<SurveyDTO> surveyNoList) {
		this.surveyNoList = surveyNoList;
	}

	public String getSelectOrganization() {
		return selectOrganization;
	}

	public void setSelectOrganization(String selectOrganization) {
		this.selectOrganization = selectOrganization;
	}

	public String getSelectDepartmrnt() {
		return selectDepartmrnt;
	}

	public void setSelectDepartmrnt(String selectDepartmrnt) {
		this.selectDepartmrnt = selectDepartmrnt;
	}

	public String getSelectRequestType() {
		return selectRequestType;
	}

	public void setSelectRequestType(String selectRequestType) {
		this.selectRequestType = selectRequestType;
	}

	public String getSelectRouteNo() {
		return selectRouteNo;
	}

	public void setSelectRouteNo(String selectRouteNo) {
		this.selectRouteNo = selectRouteNo;
	}

	public String getSelectViaNew() {
		return selectViaNew;
	}

	public void setSelectViaNew(String selectViaNew) {
		this.selectViaNew = selectViaNew;
	}

	public String getSelectDestinationNew() {
		return selectDestinationNew;
	}

	public void setSelectDestinationNew(String selectDestinationNew) {
		this.selectDestinationNew = selectDestinationNew;
	}

	public String getSelecteOriginNew() {
		return selecteOriginNew;
	}

	public void setSelecteOriginNew(String selecteOriginNew) {
		this.selecteOriginNew = selecteOriginNew;
	}

	public String getSelectSurveyReason() {
		return selectSurveyReason;
	}

	public void setSelectSurveyReason(String selectSurveyReason) {
		this.selectSurveyReason = selectSurveyReason;
	}

	public String getSelectrequestNo() {
		return selectrequestNo;
	}

	public void setSelectrequestNo(String selectrequestNo) {
		this.selectrequestNo = selectrequestNo;
	}

	public String getSelectRouteNoNew() {
		return selectRouteNoNew;
	}

	public void setSelectRouteNoNew(String selectRouteNoNew) {
		this.selectRouteNoNew = selectRouteNoNew;
	}

	public String getSelectRouteAvailable() {
		return selectRouteAvailable;
	}

	public void setSelectRouteAvailable(String selectRouteAvailable) {
		this.selectRouteAvailable = selectRouteAvailable;
	}

	public String getLoginuser() {
		return loginuser;
	}

	public void setLoginuser(String loginuser) {
		this.loginuser = loginuser;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public boolean isDissableReqSurNO() {
		return dissableReqSurNO;
	}

	public void setDissableReqSurNO(boolean dissableReqSurNO) {
		this.dissableReqSurNO = dissableReqSurNO;
	}

	public boolean isDissableSurNO() {
		return dissableSurNO;
	}

	public void setDissableSurNO(boolean dissableSurNO) {
		this.dissableSurNO = dissableSurNO;
	}

	public String getBusFare() {
		return busFare;
	}

	public void setBusFare(String busFare) {
		this.busFare = busFare;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getSelectServiceType() {
		return selectServiceType;
	}

	public void setSelectServiceType(String selectServiceType) {
		this.selectServiceType = selectServiceType;
	}

	public BigDecimal getSelectbusFare() {
		return SelectbusFare;
	}

	public void setSelectbusFare(BigDecimal selectbusFare) {
		SelectbusFare = selectbusFare;
	}

	public SurveyDTO getShowDataDTO() {
		return showDataDTO;
	}

	public void setShowDataDTO(SurveyDTO showDataDTO) {
		this.showDataDTO = showDataDTO;
	}

}
