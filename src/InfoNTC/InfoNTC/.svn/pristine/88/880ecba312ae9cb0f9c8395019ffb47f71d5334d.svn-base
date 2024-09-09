package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.DocumentManagementDTO;
import lk.informatics.ntc.model.dto.SurveyDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.DocumentManagementService;
import lk.informatics.ntc.model.service.SurveyService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "approveSurveyProcessRequestsBackingBean")
@ViewScoped
public class ApproveSurveyProcessRequestsBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	// services
	private SurveyService surveyService;
	private CommonService commonService;
	private DocumentManagementService documentManagementService;

	// DTO
	private SurveyDTO surveyDTO;

	private List<SurveyDTO> surveyRequestNoList = new ArrayList<SurveyDTO>();
	private List<SurveyDTO> surveyTypeList = new ArrayList<SurveyDTO>();
	private List<SurveyDTO> surveyMethodList = new ArrayList<SurveyDTO>();

	private List<DocumentManagementDTO> mandatoryList = new ArrayList<DocumentManagementDTO>(0);
	private List<DocumentManagementDTO> optionalList = new ArrayList<DocumentManagementDTO>(0);

	private String errorMessage, successMessage, infoMessage;
	boolean disablePanelThree = true;

	@PostConstruct
	public void init() {
		surveyDTO = new SurveyDTO();
		surveyService = (SurveyService) SpringApplicationContex.getBean("surveyService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		documentManagementService = (DocumentManagementService) SpringApplicationContex
				.getBean("documentManagementService");

		surveyRequestNoList = surveyService.getSurveyReqNoDropDown();
		surveyTypeList = surveyService.getSurveyTypeToDropDown();
		surveyMethodList = surveyService.getSurveyMethodToDropDown();
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

	public void clearSearch() {
		surveyDTO = new SurveyDTO();
		disablePanelThree = true;
		surveyRequestNoList = surveyService.getSurveyReqNoDropDown();
	}

	@SuppressWarnings("deprecation")
	public void searchAction() {
		if (surveyDTO.getRequestNo() != null && !surveyDTO.getRequestNo().trim().isEmpty()) {
			// search action
			SurveyDTO dto = surveyService.showDetails(surveyDTO.getRequestNo(), surveyDTO.getSurveyNo());

			surveyDTO.setOrganisationCode(dto.getOrganisationCode());
			surveyDTO.setDepartmentCode(dto.getDepartmentCode());
			surveyDTO.setOrganisationDescription(dto.getOrganisationDescription());
			surveyDTO.setDepartmentDescription(dto.getDepartmentDescription());
			surveyDTO.setRequestType(dto.getRequestType());
			surveyDTO.setRequestTypeDescription(dto.getRequestTypeDescription());
			surveyDTO.setAddressEnglish(dto.getAddressEnglish());
			surveyDTO.setAddressSinhala(dto.getAddressSinhala());
			surveyDTO.setAddressTamil(dto.getAddressTamil());
			surveyDTO.setRouteAvailable(dto.getRouteAvailable());
			surveyDTO.setRouteNo(dto.getRouteNo());
			surveyDTO.setOrigin(dto.getOrigin());
			surveyDTO.setDestination(dto.getDestination());
			surveyDTO.setVia(dto.getVia());
			surveyDTO.setRouteNoNew(dto.getRouteNoNew());
			surveyDTO.setOriginNew(dto.getOriginNew());
			surveyDTO.setDestinationNew(dto.getDestinationNew());
			surveyDTO.setViaNew(dto.getViaNew());
			surveyDTO.setServiceType(dto.getServiceType());
			surveyDTO.setBusFare(dto.getBusFare());
			surveyDTO.setSurveyReason(dto.getSurveyReason());
			surveyDTO.setSurveyTypeCode(dto.getSurveyTypeCode());
			surveyDTO.setSurveyMethodCode(dto.getSurveyMethodCode());
			surveyDTO.setRemarks(dto.getRemarks());

			disablePanelThree = false;
			// update task details & history
			commonService.updateSurveyTaskDetails(surveyDTO.getRequestNo(), surveyDTO.getSurveyNo(), "SU003", "O",
					sessionBackingBean.getLoginUser());
		} else {
			setErrorMessage("Please select Survey Request No.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	@SuppressWarnings("deprecation")
	public void approveAction() {
		boolean flag = surveyService.approveSurveyProcessRequest(surveyDTO, sessionBackingBean.getLoginUser());
		if (flag == true) {
			// update task details & history
			commonService.updateSurveyTaskDetails(surveyDTO.getRequestNo(), surveyDTO.getSurveyNo(), "SU003", "C",
					sessionBackingBean.getLoginUser());

			setSuccessMessage("Approved successfully.");
			RequestContext.getCurrentInstance().update("successMSG");
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
			surveyDTO = new SurveyDTO();
			disablePanelThree = true;
			surveyRequestNoList = surveyService.getSurveyReqNoDropDown();
		} else {
			setErrorMessage("Approve unsuccessful.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	@SuppressWarnings("deprecation")
	public void rejectAction() {
		if (surveyDTO.getSpecialRemarks() != null && !surveyDTO.getSpecialRemarks().trim().isEmpty()) {
			boolean flag = surveyService.rejectSurveyProcessRequest(surveyDTO, sessionBackingBean.getLoginUser());
			if (flag == true) {
				// update task details & history
				commonService.updateSurveyTaskDetails(surveyDTO.getRequestNo(), surveyDTO.getSurveyNo(), "SU003", "C",
						sessionBackingBean.getLoginUser());

				setSuccessMessage("Rejected successfully.");
				RequestContext.getCurrentInstance().update("successMSG");
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
				surveyDTO = new SurveyDTO();
				disablePanelThree = true;
				surveyRequestNoList = surveyService.getSurveyReqNoDropDown();
			} else {
				setErrorMessage("Reject unsuccessful.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {
			setErrorMessage("Special Remarks should not be empty.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void documentManagement() {
		try {

			sessionBackingBean.setEmpNo(surveyDTO.getRequestNo());
			sessionBackingBean.setEmpTransaction("SURVEY");

			mandatoryList = documentManagementService.mandatoryDocsForUserManagement("08", surveyDTO.getRequestNo());
			optionalList = documentManagementService.optionalDocsForUserManagement("08", surveyDTO.getRequestNo());

			sessionBackingBean.surveyMandatoryDocumentList = documentManagementService
					.surveyMandatoryList(surveyDTO.getRequestNo());
			sessionBackingBean.surveyOptionalDocumentList = documentManagementService
					.surveyOptionalList(surveyDTO.getRequestNo());

			RequestContext.getCurrentInstance().execute("PF('uploadDocument').show()");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void clearPage() {
		surveyDTO = new SurveyDTO();
		disablePanelThree = true;
		surveyRequestNoList = surveyService.getSurveyReqNoDropDown();
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

	public List<SurveyDTO> getSurveyMethodList() {
		return surveyMethodList;
	}

	public void setSurveyMethodList(List<SurveyDTO> surveyMethodList) {
		this.surveyMethodList = surveyMethodList;
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

	public String getInfoMessage() {
		return infoMessage;
	}

	public void setInfoMessage(String infoMessage) {
		this.infoMessage = infoMessage;
	}

	public boolean isDisablePanelThree() {
		return disablePanelThree;
	}

	public void setDisablePanelThree(boolean disablePanelThree) {
		this.disablePanelThree = disablePanelThree;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public DocumentManagementService getDocumentManagementService() {
		return documentManagementService;
	}

	public void setDocumentManagementService(DocumentManagementService documentManagementService) {
		this.documentManagementService = documentManagementService;
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

}
