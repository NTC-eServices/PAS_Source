package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.time.LocalDate;
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

@ManagedBean(name = "editIntiateSurveyRequestBackingBean")
@ViewScoped
public class EditIntiateSurveyRequestBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private static final long serialVersionUID = 1L;
	private IssuePermitService issuePermitService;
	private SurveyDTO surveyMgtDTO;
	private SurveyDTO routeDetailsDTO;
	private List<SurveyDTO> surveyRequestNoList;
	private List<SurveyDTO> organisationList;
	private List<SurveyDTO> departmentList;
	private List<SurveyDTO> requestTypeList;
	private List<CommonDTO> routeNoList;
	private String selectRequestDate, selectAddressEnglish, selectOrganisation, selectDepartment, selectRequestType,
			selectReasonSurvey;
	private String currentDate;
	private boolean linkbtn, disableBeforeSave = false, disabledReqNo = false;
	private boolean linkbtn2, disableBeforeSave2 = false, disabledReqNo2 = false;
	private boolean displayBusFareField = true;
	private boolean showInputPanelGrid = false;
	private String errorMessage, successMessage, infoMessage;
	LocalDate localDate;
	private StreamedContent files;

	// Services
	private SurveyService surveyService;
	private CommonService commonService;
	private DocumentManagementService documentManagementService;

	private List<DocumentManagementDTO> mandatoryList = new ArrayList<DocumentManagementDTO>(0);
	private List<DocumentManagementDTO> optionalList = new ArrayList<DocumentManagementDTO>(0);
	// DTO

	@PostConstruct
	public void init() {
		surveyService = (SurveyService) SpringApplicationContex.getBean("surveyService");
		documentManagementService = (DocumentManagementService) SpringApplicationContex
				.getBean("documentManagementService");
		issuePermitService = (IssuePermitService) SpringApplicationContex.getBean("issuePermitService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		surveyMgtDTO = new SurveyDTO();
		routeDetailsDTO = new SurveyDTO();
		surveyRequestNoList = new ArrayList<SurveyDTO>();
		organisationList = new ArrayList<SurveyDTO>();
		departmentList = new ArrayList<SurveyDTO>();
		requestTypeList = new ArrayList<SurveyDTO>();
		routeNoList = issuePermitService.getRoutesToDropdown();
		surveyMgtDTO.setRouteAvailable("true");
		setLinkbtn(true);
		setLinkbtn2(true);

		organisationList = surveyService.getOrganisationToDropDown();
		departmentList = surveyService.getDepartmentToDropDown();
		requestTypeList = surveyService.getRequestTypeToDropDown();
		surveyRequestNoList = surveyService.getSurveyReqOnGoingNoDropDown();
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
	public void searchAction() {
		if (surveyMgtDTO.getRequestNo() != null && !surveyMgtDTO.getRequestNo().trim().isEmpty()) {
			// search action
			SurveyDTO dto = surveyService.showDetails(surveyMgtDTO.getRequestNo(), null);

			surveyMgtDTO.setOrganisationCode(dto.getOrganisationCode());
			surveyMgtDTO.setDepartmentCode(dto.getDepartmentCode());
			surveyMgtDTO.setRequestType(dto.getRequestType());
			surveyMgtDTO.setRequestTypeDescription(dto.getRequestTypeDescription());
			surveyMgtDTO.setAddressEnglish(dto.getAddressEnglish());
			surveyMgtDTO.setAddressSinhala(dto.getAddressSinhala());
			surveyMgtDTO.setAddressTamil(dto.getAddressTamil());
			surveyMgtDTO.setRouteAvailable(dto.getRouteAvailable());
			if (surveyMgtDTO.getRouteAvailable().trim().equals("true")) {
				setLinkbtn(true);
				setDisplayBusFareField(true);
			} else {
				setLinkbtn(false);
				setDisplayBusFareField(false);
			}
			surveyMgtDTO.setRouteNo(dto.getRouteNo());
			surveyMgtDTO.setOrigin(dto.getOrigin());
			surveyMgtDTO.setDestination(dto.getDestination());
			surveyMgtDTO.setVia(dto.getVia());
			surveyMgtDTO.setRouteNoNew(dto.getRouteNoNew());
			surveyMgtDTO.setOriginNew(dto.getOriginNew());
			surveyMgtDTO.setDestinationNew(dto.getDestinationNew());
			surveyMgtDTO.setViaNew(dto.getViaNew());
			surveyMgtDTO.setServiceType(dto.getServiceTypeCode());
			surveyMgtDTO.setBusFare(dto.getBusFare());
			surveyMgtDTO.setSurveyReason(dto.getSurveyReason());
			surveyMgtDTO.setSurveyTypeCode(dto.getSurveyTypeCode());
			surveyMgtDTO.setSurveyMethodCode(dto.getSurveyMethodCode());
			surveyMgtDTO.setRemarks(dto.getRemarks());
			disabledReqNo = true;
			setShowInputPanelGrid(true);
		} else {
			setErrorMessage("Please select Survey Request No.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	// Save Function for whole Page -Edit Initiate Survey Request
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

									} 
									if (updateSuccess == true) {
										setSuccessMessage("Updated successfully.");
										RequestContext.getCurrentInstance().update("successMSG");
										RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
									} else {
										setErrorMessage("Update error.");
										RequestContext.getCurrentInstance().update("errorMSG");
										RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
									}
								} else {
									setErrorMessage("Update error.");
									RequestContext.getCurrentInstance().update("errorMSG");
									RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
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
				}
			} else {
				setErrorMessage("Please select Department.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {
			setErrorMessage("Please select Organization.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void cancelInitiateSurveyRequest() {
		surveyMgtDTO = new SurveyDTO();
		surveyMgtDTO.setRouteAvailable("true");
		setLinkbtn(true);
		disableBeforeSave = false;
		disabledReqNo = false;
		surveyRequestNoList = surveyService.getSurveyReqOnGoingNoDropDown();
		setDisplayBusFareField(true);
		setShowInputPanelGrid(false);
	}

	@SuppressWarnings("deprecation")
	public void viewAddNewRoute() {
		RequestContext.getCurrentInstance().update("routeDetails");
		RequestContext.getCurrentInstance().execute("PF('routeDetailsDialog').show()");
	}

	// Save Function for Route Details Pop-up Dialog
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
								// save in the “nt_r_route” table
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
									setErrorMessage("Adding unsuccessful.");
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

	// Update Function of Route Details Dialog - Update Temp New Route

	@SuppressWarnings("deprecation")
	public void viewUpdateNewRoute() {
		RequestContext.getCurrentInstance().update("updateRouteDetails");
		RequestContext.getCurrentInstance().execute("PF('routeDetailsDialogUpdate').show()");
	}

	public void updateRouteDetails() {
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
								// save in the “nt_r_route” table
								boolean rs = surveyService.updateNewRouteDetails(routeDetailsDTO,
										sessionBackingBean.getLoginUser());

								if (rs) {
									surveyMgtDTO.setRouteNoNew(routeDetailsDTO.getRouteNoNew());
									surveyMgtDTO.setOriginNew(routeDetailsDTO.getOriginNew());
									surveyMgtDTO.setDestinationNew(routeDetailsDTO.getDestinationNew());
									surveyMgtDTO.setViaNew(routeDetailsDTO.getViaNew());

									setSuccessMessage("Updated successfully.");
									RequestContext.getCurrentInstance().update("successMSG");
									RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
								} else {
									setErrorMessage("Updating unsuccessful.");
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

	// Update Route Button Dialog
	public void cancelUpdateRouteDetails() {
		routeDetailsDTO = new SurveyDTO();
	}

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

	public void onSurveyRequestNoChange() {
		if (surveyMgtDTO.getRequestNo() != null) {
			for (SurveyDTO dto : surveyRequestNoList) {
				if (surveyMgtDTO.getRequestNo().equals(dto.getRequestNo())) {
					surveyMgtDTO.setSurveyNo(dto.getSurveyNo());
					surveyMgtDTO.setRequestDate(dto.getRequestDate());
				}
			}
		} else {
			surveyMgtDTO = new SurveyDTO();
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

	public List<SurveyDTO> getSurveyRequestNoList() {
		return surveyRequestNoList;
	}

	public void setSurveyRequestNoList(List<SurveyDTO> surveyRequestNoList) {
		this.surveyRequestNoList = surveyRequestNoList;
	}

	public boolean isDisabledReqNo() {
		return disabledReqNo;
	}

	public void setDisabledReqNo(boolean disabledReqNo) {
		this.disabledReqNo = disabledReqNo;
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

	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
	}

	public boolean isDisplayBusFareField() {
		return displayBusFareField;
	}

	public void setDisplayBusFareField(boolean displayBusFareField) {
		this.displayBusFareField = displayBusFareField;
	}

	public boolean isShowInputPanelGrid() {
		return showInputPanelGrid;
	}

	public void setShowInputPanelGrid(boolean showInputPanelGrid) {
		this.showInputPanelGrid = showInputPanelGrid;
	}

	public boolean isLinkbtn2() {
		return linkbtn2;
	}

	public void setLinkbtn2(boolean linkbtn2) {
		this.linkbtn2 = linkbtn2;
	}

	public boolean isDisableBeforeSave2() {
		return disableBeforeSave2;
	}

	public void setDisableBeforeSave2(boolean disableBeforeSave2) {
		this.disableBeforeSave2 = disableBeforeSave2;
	}

	public boolean isDisabledReqNo2() {
		return disabledReqNo2;
	}

	public void setDisabledReqNo2(boolean disabledReqNo2) {
		this.disabledReqNo2 = disabledReqNo2;
	}

}
