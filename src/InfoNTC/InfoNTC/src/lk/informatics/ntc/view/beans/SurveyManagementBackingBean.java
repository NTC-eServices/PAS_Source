package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.DocumentManagementDTO;
import lk.informatics.ntc.model.dto.SurveyDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.DocumentManagementService;
import lk.informatics.ntc.model.service.IssuePermitService;
import lk.informatics.ntc.model.service.SurveyService;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.ntc.view.util.SpringApplicationContex;
import lk.informatics.unicode.UnicodeShaper;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

@ManagedBean(name = "surveyManagementBackingBean")
@ViewScoped
public class SurveyManagementBackingBean implements Serializable {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private static final long serialVersionUID = 1L;
	private IssuePermitService issuePermitService;
	private DocumentManagementService documentManagementService;
	private SurveyDTO surveyMgtDTO;
	private SurveyDTO routeDetailsDTO;
	private List<SurveyDTO> organisationList;
	private List<SurveyDTO> departmentList;
	private List<SurveyDTO> requestTypeList;
	private List<CommonDTO> routeNoList;
	private String selectRequestDate, selectAddressEnglish, selectOrganisation, selectDepartment, selectRequestType,
			selectReasonSurvey;
	private String currentDate;
	private boolean linkbtn, disableBeforeSave = true;
	private boolean linkbtnTwo, disableBeforeSaveTwo = true;
	private boolean displayBusFareField = true;
	private String errorMessage, successMessage, infoMessage;
	LocalDate localDate;

	private List<DocumentManagementDTO> mandatoryList = new ArrayList<DocumentManagementDTO>(0);
	private List<DocumentManagementDTO> optionalList = new ArrayList<DocumentManagementDTO>(0);
	private StreamedContent files;
	// Services
	private SurveyService surveyService;
	private CommonService commonService;

	@PostConstruct
	public void init() {

		surveyService = (SurveyService) SpringApplicationContex.getBean("surveyService");
		documentManagementService = (DocumentManagementService) SpringApplicationContex
				.getBean("documentManagementService");
		issuePermitService = (IssuePermitService) SpringApplicationContex.getBean("issuePermitService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		surveyMgtDTO = new SurveyDTO();
		routeDetailsDTO = new SurveyDTO();
		organisationList = new ArrayList<SurveyDTO>();
		departmentList = new ArrayList<SurveyDTO>();
		requestTypeList = new ArrayList<SurveyDTO>();
		routeNoList = issuePermitService.getRoutesToDropdown();
		surveyMgtDTO.setRouteAvailable("true");
		setLinkbtn(true);
		setLinkbtnTwo(true);

		LocalDate localDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String currentDate = localDate.format(formatter);
		surveyMgtDTO.setRequestDate(currentDate);

		organisationList = surveyService.getOrganisationToDropDown();
		departmentList = surveyService.getDepartmentToDropDown();
		requestTypeList = surveyService.getRequestTypeToDropDown();

	}

	public void onChangeAction() {
		if (surveyMgtDTO.getRouteAvailable().trim().equals("true")) {
			setLinkbtn(true);
			surveyMgtDTO.setRouteNoNew(null);
			surveyMgtDTO.setOriginNew(null);
			surveyMgtDTO.setDestinationNew(null);
			surveyMgtDTO.setViaNew(null);
			surveyMgtDTO.setBusFare(null);
			setDisplayBusFareField(true);
		} else {
			setLinkbtn(false);
			surveyMgtDTO.setRouteNo(null);
			surveyMgtDTO.setOrigin(null);
			surveyMgtDTO.setDestination(null);
			surveyMgtDTO.setVia(null);
			surveyMgtDTO.setBusFare(null);
			setDisplayBusFareField(false);
		}
	}

	@SuppressWarnings("deprecation")
	public void saveSurveyRequestData() {
		if (surveyMgtDTO.getOrganisationCode() != null && !surveyMgtDTO.getOrganisationCode().trim().isEmpty()) {
			if (surveyMgtDTO.getDepartmentCode() != null && !surveyMgtDTO.getDepartmentCode().trim().isEmpty()) {
				if (surveyMgtDTO.getRequestType() != null && !surveyMgtDTO.getRequestType().trim().isEmpty()) {
					if (surveyMgtDTO.getAddressEnglish() != null
							&& !surveyMgtDTO.getAddressEnglish().trim().isEmpty()) {
						if (surveyMgtDTO.getSurveyReason() != null
								&& !surveyMgtDTO.getSurveyReason().trim().isEmpty()) {

							if ((surveyMgtDTO.getOrigin() != null && !surveyMgtDTO.getOrigin().trim().isEmpty())
									|| (surveyMgtDTO.getOriginNew() != null
											&& !surveyMgtDTO.getOriginNew().trim().isEmpty())) {

								if (surveyMgtDTO.getRequestNo() != null
										&& !surveyMgtDTO.getRequestNo().trim().isEmpty()) {
									Boolean updateSuccess = surveyService.updateSurveyRequestData(surveyMgtDTO,
											sessionBackingBean.getLoginUser());
									if (displayBusFareField == false) {

										boolean updateRouteBusFare = surveyService.updateRouteBusFare(surveyMgtDTO,
												sessionBackingBean.getLoginUser());

									} else {

									}
									if (updateSuccess == true) {
										setSuccessMessage("Updated successfully.");
										RequestContext.getCurrentInstance().update("successMSG");
										RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
									} else {
										setErrorMessage("Update error.");
										RequestContext.getCurrentInstance().update("errorMSG");
										RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
										RequestContext.getCurrentInstance().update("formOne");
									}
								} else {
									String returnSurveyReqNo = surveyService.saveSurveyRequestData(surveyMgtDTO,
											sessionBackingBean.getLoginUser());

									if (returnSurveyReqNo != null && !returnSurveyReqNo.trim().isEmpty()) {
										surveyMgtDTO.setRequestNo(returnSurveyReqNo);
										commonService.insertSurveyTaskDetails(surveyMgtDTO.getRequestNo(), "SU001", "O",
												sessionBackingBean.getLoginUser());
										commonService.updateSurveyTaskDetails(surveyMgtDTO.getRequestNo(),
												surveyMgtDTO.getSurveyNo(), "SU001", "C",
												sessionBackingBean.getLoginUser());
										disableBeforeSave = false;
										disableBeforeSaveTwo = false;
										if (displayBusFareField == false) {

											boolean updateRouteBusFare = surveyService.updateRouteBusFare(surveyMgtDTO,
													sessionBackingBean.getLoginUser());

										} else {

										}
										setSuccessMessage("Saved successfully.");
										RequestContext.getCurrentInstance().update("successMSG");
										RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
									} else {
										setErrorMessage("Save error.");
										RequestContext.getCurrentInstance().update("errorMSG");
										RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
										RequestContext.getCurrentInstance().update("formOne");
									}
								}

							} else {
								setErrorMessage("Please fill Route details.");
								RequestContext.getCurrentInstance().update("errorMSG");
								RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
								RequestContext.getCurrentInstance().update("formOne");
							}

						} else {
							setErrorMessage("Please fill Reason for Survey.");
							RequestContext.getCurrentInstance().update("errorMSG");
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
							RequestContext.getCurrentInstance().update("formOne");
						}
					} else {
						setErrorMessage("Please fill Address(English).");
						RequestContext.getCurrentInstance().update("errorMSG");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						RequestContext.getCurrentInstance().update("formOne");
					}
				} else {
					setErrorMessage("Please select Request Type.");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					RequestContext.getCurrentInstance().update("formOne");
				}
			} else {
				setErrorMessage("Please select Department.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				RequestContext.getCurrentInstance().update("formOne");
			}
		} else {
			setErrorMessage("Please select Organization.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			RequestContext.getCurrentInstance().update("formOne");
		}
	}

	public void cancelInitiateSurveyRequest() {
		surveyMgtDTO = new SurveyDTO();
		surveyMgtDTO.setRouteAvailable("true");
		setLinkbtn(true);
		LocalDate localDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String currentDate = localDate.format(formatter);
		surveyMgtDTO.setRequestDate(currentDate);
		disableBeforeSave = true;
		disableBeforeSaveTwo = true;
		setDisplayBusFareField(true);
	}

	@SuppressWarnings("deprecation")
	public void viewAddNewRoute() {
		RequestContext.getCurrentInstance().update("routeDetails");
		RequestContext.getCurrentInstance().execute("PF('routeDetailsDialog').show()");
	}

	@SuppressWarnings("deprecation")
	public void saveRouteDetails() {
		if (routeDetailsDTO.getRouteNoNew() != null && !routeDetailsDTO.getRouteNoNew().trim().isEmpty()) {
			if (routeDetailsDTO.getOriginNew() != null && !routeDetailsDTO.getOriginNew().trim().isEmpty()) {
				if (routeDetailsDTO.getDestinationNew() != null
						&& !routeDetailsDTO.getDestinationNew().trim().isEmpty()) {
					if (routeDetailsDTO.getViaNew() != null && !routeDetailsDTO.getViaNew().trim().isEmpty()) {
						if (routeDetailsDTO.getDistanceNew() > 0) {
							if (surveyService.isDuplicateRouteNo(routeDetailsDTO)) {
								setErrorMessage("Route No. already exists.");
								RequestContext.getCurrentInstance().update("errorMSG");
								RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
							} else {

								boolean rs = surveyService.saveTempRouteDetails(routeDetailsDTO,
										sessionBackingBean.getLoginUser());

								if (rs) {
									surveyMgtDTO.setRouteNoNew(routeDetailsDTO.getRouteNoNew());
									surveyMgtDTO.setOriginNew(routeDetailsDTO.getOriginNew());
									surveyMgtDTO.setDestinationNew(routeDetailsDTO.getDestinationNew());
									surveyMgtDTO.setViaNew(routeDetailsDTO.getViaNew());

									setSuccessMessage("Added successfully.");
									RequestContext.getCurrentInstance().update("successMSG");
									RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
								} else {
									setErrorMessage("Adding unsuccessfull.");
									RequestContext.getCurrentInstance().update("errorMSG");
									RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
								}
								clearRouteDetails();
								RequestContext.getCurrentInstance().execute("PF('routeDetailsDialog').hide()");
							}
						} else {
							setErrorMessage("Please enter Distance.");
							RequestContext.getCurrentInstance().update("errorMSG");
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						}
					} else {
						setErrorMessage("Please enter Via.");
						RequestContext.getCurrentInstance().update("errorMSG");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}
				} else {
					setErrorMessage("Please enter Destination.");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			} else {
				setErrorMessage("Please enter Origin.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {
			setErrorMessage("Please enter Route No.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public StreamedContent printResponseLet() throws JRException {

		String surveyReqNo = surveyMgtDTO.getRequestNo();

		files = null;
		String sourceFileName = null;

		Connection conn = null;

		try {
			conn = ConnectionManager.getConnection();

			sourceFileName = "..//reports//InitiateRequestLetter.jrxml";

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_request_no", surveyReqNo);

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", "Response Letter For survey Request.pdf");

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("reportBytes", pdfByteArray);
			sessionMap.put("docType", "pdf");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(conn);
		}

		return files;

	}

	public void clearRouteDetails() {
		routeDetailsDTO = new SurveyDTO();
	}

	public void onRouteNoChange() {
		SurveyDTO data = surveyService.fillRouteDetailsFromRouteNo(surveyMgtDTO.getRouteNo());
		surveyMgtDTO.setOrigin(data.getOrigin());
		surveyMgtDTO.setDestination(data.getDestination());
		surveyMgtDTO.setVia(data.getVia());
		surveyMgtDTO.setBusFare(data.getBusFare());
	}

	// Document Management

	@SuppressWarnings("deprecation")
	public void documentManagement() {
		try {

			sessionBackingBean.setEmpNo(surveyMgtDTO.getRequestNo());
			sessionBackingBean.setEmpTransaction("SURVEY");

			mandatoryList = documentManagementService.mandatoryDocsForUserManagement("08", surveyMgtDTO.getRequestNo());
			optionalList = documentManagementService.optionalDocsForUserManagement("08", surveyMgtDTO.getRequestNo());

			sessionBackingBean.surveyMandatoryDocumentList = documentManagementService
					.surveyMandatoryList(surveyMgtDTO.getRequestNo());
			sessionBackingBean.surveyOptionalDocumentList = documentManagementService
					.surveyOptionalList(surveyMgtDTO.getRequestNo());

			RequestContext.getCurrentInstance().execute("PF('uploadDocument').show()");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public SurveyDTO getSurveyMgtDTO() {
		return surveyMgtDTO;
	}

	public void setSurveyMgtDTO(SurveyDTO surveyMgtDTO) {
		this.surveyMgtDTO = surveyMgtDTO;
	}

	public List<SurveyDTO> getOrganisationList() {
		return organisationList;
	}

	public void setOrganisationList(List<SurveyDTO> organisationList) {
		this.organisationList = organisationList;
	}

	public SurveyService getSurveyMgtService() {
		return surveyService;
	}

	public void setSurveyMgtService(SurveyService surveyMgtService) {
		this.surveyService = surveyMgtService;
	}

	public List<SurveyDTO> getDepartmentList() {
		return departmentList;
	}

	public void setDepartmentList(List<SurveyDTO> departmentList) {
		this.departmentList = departmentList;
	}

	public List<SurveyDTO> getRequestTypeList() {
		return requestTypeList;
	}

	public void setRequestTypeList(List<SurveyDTO> requestTypeList) {
		this.requestTypeList = requestTypeList;
	}

	public List<CommonDTO> getRouteNoList() {
		return routeNoList;
	}

	public void setRouteNoList(List<CommonDTO> routeNoList) {
		this.routeNoList = routeNoList;
	}

	public String getSelectDepartment() {
		return selectDepartment;
	}

	public void setSelectDepartment(String selectDepartment) {
		this.selectDepartment = selectDepartment;
	}

	public String getSelectOrganisation() {
		return selectOrganisation;
	}

	public void setSelectOrganisation(String selectOrganisation) {
		this.selectOrganisation = selectOrganisation;
	}

	public String getSelectRequestType() {
		return selectRequestType;
	}

	public void setSelectRequestType(String selectRequestType) {
		this.selectRequestType = selectRequestType;
	}

	public String getSelectAddressEnglish() {
		return selectAddressEnglish;
	}

	public void setSelectAddressEnglish(String selectAddressEnglish) {
		this.selectAddressEnglish = selectAddressEnglish;
	}

	public String getSelectReasonSurvey() {
		return selectReasonSurvey;
	}

	public void setSelectReasonSurvey(String selectReasonSurvey) {
		this.selectReasonSurvey = selectReasonSurvey;
	}

	public String getSelectRequestDate() {
		return selectRequestDate;
	}

	public void setSelectRequestDate(String selectRequestDate) {
		this.selectRequestDate = selectRequestDate;
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

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	public void saveInitiateSurveyRequest() {
	}

	public void clearAll() {
	}

	public boolean isLinkbtn() {
		return linkbtn;
	}

	public void setLinkbtn(boolean linkbtn) {
		this.linkbtn = linkbtn;
	}

	public SurveyDTO getRouteDetailsDTO() {
		return routeDetailsDTO;
	}

	public void setRouteDetailsDTO(SurveyDTO routeDetailsDTO) {
		this.routeDetailsDTO = routeDetailsDTO;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public String getInfoMessage() {
		return infoMessage;
	}

	public void setInfoMessage(String infoMessage) {
		this.infoMessage = infoMessage;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public boolean isDisableBeforeSave() {
		return disableBeforeSave;
	}

	public void setDisableBeforeSave(boolean disableBeforeSave) {
		this.disableBeforeSave = disableBeforeSave;
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

	public boolean isDisplayBusFareField() {
		return displayBusFareField;
	}

	public void setDisplayBusFareField(boolean displayBusFareField) {
		this.displayBusFareField = displayBusFareField;
	}

	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
	}

	public IssuePermitService getIssuePermitService() {
		return issuePermitService;
	}

	public void setIssuePermitService(IssuePermitService issuePermitService) {
		this.issuePermitService = issuePermitService;
	}

	public LocalDate getLocalDate() {
		return localDate;
	}

	public void setLocalDate(LocalDate localDate) {
		this.localDate = localDate;
	}

	public SurveyService getSurveyService() {
		return surveyService;
	}

	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean isLinkbtnTwo() {
		return linkbtnTwo;
	}

	public void setLinkbtnTwo(boolean linkbtnTwo) {
		this.linkbtnTwo = linkbtnTwo;
	}

	public boolean isDisableBeforeSaveTwo() {
		return disableBeforeSaveTwo;
	}

	public void setDisableBeforeSaveTwo(boolean disableBeforeSaveTwo) {
		this.disableBeforeSaveTwo = disableBeforeSaveTwo;
	}

}
