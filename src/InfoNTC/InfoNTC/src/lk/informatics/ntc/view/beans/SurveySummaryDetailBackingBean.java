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

import lk.informatics.ntc.model.dto.DocumentManagementDTO;
import lk.informatics.ntc.model.dto.SurveyDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.DocumentManagementService;
import lk.informatics.ntc.model.service.SurveyService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "surveySummaryDetailsBackingBean")
@ViewScoped
public class SurveySummaryDetailBackingBean {
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	// services
	private SurveyService surveyService;
	private CommonService commonService;
	private DocumentManagementService documentManagementService;
	private boolean displayRouteNo;
	private String duplicateRoute;
	// DTO
	private SurveyDTO surveyDTO;
	private SurveyDTO showTypeMethodDTO;
	private SurveyDTO showInitiateData;
	private SurveyDTO routeDetDTO;
	private List<SurveyDTO> showData;
	private List<SurveyDTO> savedata;
	private List<SurveyDTO> updateDate;
	private List<SurveyDTO> showGridDataDTO;
	private List<SurveyDTO> deleteRow;
	private SurveyDTO dto;
	private SurveyDTO checkSeqNo;
	private List<SurveyDTO> showall = new ArrayList<SurveyDTO>(0);;

	public SurveyDTO getDto() {
		return dto;
	}

	public void setDto(SurveyDTO dto) {
		this.dto = dto;
	}

	public List<SurveyDTO> getDeleteRow() {
		return deleteRow;
	}

	public void setDeleteRow(List<SurveyDTO> deleteRow) {
		this.deleteRow = deleteRow;
	}

	private SurveyDTO selectList;
	private List<SurveyDTO> surveyNoList = new ArrayList<SurveyDTO>();
	private List<SurveyDTO> routeNOList;
	private List<DocumentManagementDTO> mandatoryList = new ArrayList<DocumentManagementDTO>(0);
	private List<DocumentManagementDTO> optionalList = new ArrayList<DocumentManagementDTO>(0);
	private SurveyDTO selectedRow;

	private String errorMessage;
	private String successMessage;
	private String currentDate;
	private String organization, deparment, reqType, routNo, showSurveyNo;
	SurveyDTO surReason;
	private String selectSurveyType, selectSurveyMethod, selectremarks, selectSurveyNo, selectRequestDate,
			selectSurReqNo, selectVia, selectDestination, selecteOrigin, selectServiceType;
	private boolean disabledReqNo = false;
	private String loginUser, taskStatus, status;
	private boolean routeFlag;
	private boolean editFlag;
	private boolean disabledBtns = false;

	public SurveySummaryDetailBackingBean() {
		surveyService = (SurveyService) SpringApplicationContex.getBean("surveyService");

	}

	@PostConstruct
	public void init() {
		surveyService = (SurveyService) SpringApplicationContex.getBean("surveyService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		documentManagementService = (DocumentManagementService) SpringApplicationContex
				.getBean("documentManagementService");

		surveyDTO = new SurveyDTO();
		surveyNoList = surveyService.getSurveySummarySurveyNo();
		routeNOList = surveyService.getSurveyRouteNo(surveyDTO.getSurveyNo());
		LocalDate localDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		currentDate = localDate.format(formatter);
		loginUser = sessionBackingBean.getLoginUser();
		displayRouteNo = false;
	}

	public void fillSurveyTypeMethod() {
		setDisabledBtns(false);
		showTypeMethodDTO = surveyService.fillSurveyTypeMethod(surveyDTO.getSurveyNo());
		surveyDTO.setSurveyType(showTypeMethodDTO.getSurveyType());
		surveyDTO.setSurveyMethod(showTypeMethodDTO.getSurveyMethod());
		surveyDTO.setRequestNo(showTypeMethodDTO.getRequestNo());
		routeNOList = surveyService.getSurveyRouteNo(surveyDTO.getSurveyNo());
	}

	public void dispalyInitiateDet() {
		showInitiateData = surveyService.filldet(surveyDTO.getSurveyNo());
		surveyDTO.setSpecialRemarks(showInitiateData.getSpecialRemarks());
		surveyDTO.setApproveRejectStatus(showInitiateData.getApproveRejectStatus());
		surveyDTO.setTenderRequire(showInitiateData.getTenderRequire());

	}

	public void clearSearchDetail() {
		surveyDTO = new SurveyDTO();
		displayRouteNo = false;

	}

	public void fillRouteDet(String routeNo) {

		if (editFlag) {
			routeDetDTO = surveyService.fillRouteDetailsFromRouteNo(surveyDTO.getRouteNo());
			surveyDTO.setDestination(routeDetDTO.getDestination());
			surveyDTO.setOrigin(routeDetDTO.getOrigin());
			surveyDTO.setVia(routeDetDTO.getVia());

		} else {
			routeDetDTO = surveyService.fillRouteDetailsFromRouteNo(surveyDTO.getRouteNo());
			surveyDTO.setDestination(routeDetDTO.getDestination());
			surveyDTO.setOrigin(routeDetDTO.getOrigin());
			surveyDTO.setVia(routeDetDTO.getVia());
			surveyDTO.setEffectiveRoute("");
			surveyDTO.setNoOfPermits(0);
		}

	}

	public void searchDetails() {
		if (surveyDTO.getSurveyNo() != null && !surveyDTO.getSurveyNo().trim().isEmpty()) {
			showGridDataDTO = surveyService.showRouteDetails(surveyDTO.getSurveyNo());

			showGridDataDTO = surveyService.showRouteDetails(surveyDTO.getSurveyNo());

		} else {

			setErrorMessage("Please select survey  No.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public boolean routeFlagListener() {
		String location1;
		String location2;
		if (routeFlag) {
			location1 = surveyDTO.getOrigin();
			location2 = surveyDTO.getDestination();
			surveyDTO.setOrigin(location2);
			surveyDTO.setDestination(location1);
			surveyDTO.setRouteFlag("Y");
			return false;
		} else {
			location1 = surveyDTO.getOrigin();
			location2 = surveyDTO.getDestination();
			surveyDTO.setOrigin(location2);
			surveyDTO.setDestination(location1);
			surveyDTO.setRouteFlag("N");
			return true;
		}

	}

	public void clearAddDet() {
		surveyDTO.setRouteNo("");
		;
		surveyDTO.setRouteFlag("N");
		surveyDTO.setOrigin("");
		surveyDTO.setDestination("");
		surveyDTO.setVia("");
		surveyDTO.setEffectiveRoute("");
		surveyDTO.setNoOfPermits(0);

	}

	public void addButton() {
		status = surveyService.getApproveRejectStatus(surveyDTO.getSurveyNo());
		if (status == null) {
			if (surveyDTO.getRouteNo() != null && !surveyDTO.getRouteNo().trim().isEmpty()) {

				if (surveyDTO.getOrigin() != null && !surveyDTO.getOrigin().trim().isEmpty()) {
					if (surveyDTO.getDestination() != null && !surveyDTO.getDestination().trim().isEmpty()) {
						if (surveyDTO.getEffectiveRoute() != null && !surveyDTO.getEffectiveRoute().trim().isEmpty()) {
							if (surveyDTO.getNoOfPermits() != null) {
								if (surveyDTO.getNoOfPermits() > 0) {
									if (editFlag) {
										checkSeqNo = surveyService.showSeqNo(surveyDTO, selectedRow.getRouteNo(),
												selectedRow.getNoOfPermits(), selectedRow.getEffectiveRoute(),
												selectedRow.getDestination(), selectedRow.getOrigin());

										if (checkSeqNo.getSeq() > 0) {
											surveyService.updateEditedData(surveyDTO, selectedRow.getRouteNo(),
													selectedRow.getNoOfPermits(), selectedRow.getEffectiveRoute(),
													selectedRow.getDestination(), selectedRow.getOrigin());
											showGridDataDTO = surveyService.addDetailsToGrid(surveyDTO);
											clearAddDet();
											displayRouteNo = false;
											editFlag = false;
										}
									} else {
										duplicateRoute = surveyService.checkDuplicateRouteNo(surveyDTO);

										if (duplicateRoute == null) {
											showData = surveyService.showAddDetails(surveyDTO, loginUser);
											showGridDataDTO = surveyService.addDetailsToGrid(surveyDTO);
											clearAddDet();
											displayRouteNo = false;
										} else if (duplicateRoute.equals(surveyDTO.getRouteNo())) {

											setErrorMessage("This Route No.already added. Please use edit mode");
											RequestContext.getCurrentInstance().update("errorMSG");
											RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
										} else {
											showData = surveyService.showAddDetails(surveyDTO, loginUser);
											showGridDataDTO = surveyService.addDetailsToGrid(surveyDTO);
											clearAddDet();
											displayRouteNo = false;

										}
									}
								} else {
									setErrorMessage("Please enter number of permit require to use");
									RequestContext.getCurrentInstance().update("errorMSG");
									RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
								}
							} else {
								setErrorMessage("Please enter number of permit require to use");
								RequestContext.getCurrentInstance().update("errorMSG");
								RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
							}

						} else {
							setErrorMessage("Please enter effective route ");
							RequestContext.getCurrentInstance().update("errorMSG");
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

						}

					} else {
						setErrorMessage("Please select destination");
						RequestContext.getCurrentInstance().update("errorMSG");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}

				} else {
					setErrorMessage("Please select origine");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

				}

			} else {
				setErrorMessage("Please select a route  No.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

			}

		} else if (status.equals("A")) {

			setErrorMessage("Survey summery already approved");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		} else if (status.equals("R")) {
			setErrorMessage("Survey summery already rejected");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		}
	}

	public void clearValues() {
		surveyDTO.setEffectiveRoute("");
		surveyDTO.setRouteNo("");
		surveyDTO.setOrigin("");
		surveyDTO.setDestination("");
		surveyDTO.setVia("");
		surveyDTO.setNoOfPermits(0);
		displayRouteNo = false;
	}

	public void saveButton() {

		status = surveyService.getApproveRejectStatus(surveyDTO.getSurveyNo());
		if (!"A".equals(status) && !"R".equals(status)) {
			if (surveyDTO.getSpecialRemarks() != null && !surveyDTO.getSpecialRemarks().trim().isEmpty()) {
				if (showGridDataDTO.size() > 0) {
					savedata = surveyService.updateSurveyData(surveyDTO, loginUser);
					setSuccessMessage("Succesfully Saved.");
					RequestContext.getCurrentInstance().update("successMSG");
					RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
					commonService.updateSurveyTaskDetails(surveyDTO.getRequestNo(), surveyDTO.getSurveyNo(), "SU008",
							"O", sessionBackingBean.getLoginUser());

					displayRouteNo = false;
				} else {
					setErrorMessage("No data enter  for save ");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			} else {
				setErrorMessage("Please enter special remarks");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else if (status.equals("A")) {

			setErrorMessage("Survey summery already approved");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		} else if (status.equals("R")) {
			setErrorMessage("Survey summery already rejected");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		}

	}

	public void editButton() {

		surveyDTO.setRouteNo(selectedRow.getRouteNo());
		surveyDTO.setNoOfPermits(selectedRow.getNoOfPermits());
		surveyDTO.setEffectiveRoute(selectedRow.getEffectiveRoute());
		fillRouteDet(selectedRow.getRouteNo());

		editFlag = true;
		displayRouteNo = true;

	}

	public void clearSurDet() {
		surveyDTO = new SurveyDTO();
		selectSurveyType = null;
		selectSurveyMethod = null;
		selectremarks = null;
		displayRouteNo = false;
		disabledReqNo = false;
	}

	public void clearGrid() {
		surveyDTO = new SurveyDTO();
		showGridDataDTO = new ArrayList<SurveyDTO>();
		displayRouteNo = false;
		setDisabledBtns(false);
	}

	public void delete() {

		status = surveyService.getApproveRejectStatus(surveyDTO.getSurveyNo());
		if (status == null) {
			surveyService.removeEditedData(surveyDTO, selectedRow.getRouteNo(), selectedRow.getNoOfPermits(),
					selectedRow.getEffectiveRoute(), selectedRow.getDestination(), selectedRow.getOrigin());
			showGridDataDTO = surveyService.addDetailsToGrid(surveyDTO);
			setSuccessMessage("Succesfully deleted.");
			RequestContext.getCurrentInstance().update("successMSG");
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
			displayRouteNo = false;
		} else if (status.equals("A")) {

			setErrorMessage("Survey summery already approved");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		} else if (status.equals("R")) {
			setErrorMessage("Survey summery already rejected");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		}
	}

	public void approveButton() {

		taskStatus = surveyService.getTaskStatus(surveyDTO.getSurveyNo());
		status = surveyService.getApproveRejectStatus(surveyDTO.getSurveyNo());

		if (taskStatus != null) {
			if (taskStatus.equals("O")) {
				if (surveyDTO.getApproveRejectStatus() != null
						&& !surveyDTO.getApproveRejectStatus().trim().isEmpty()) {
					if (!"A".equals(status) && !"R".equals(status)) {
						if (surveyDTO.getApproveRejectStatus() != null
								&& !surveyDTO.getApproveRejectStatus().trim().isEmpty()) {

							updateDate = surveyService.updateApproveRejectStatus(surveyDTO, loginUser);
							setSuccessMessage("Succesfully Approved.");
							RequestContext.getCurrentInstance().update("successMSG");
							RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
							commonService.updateSurveyTaskDetails(surveyDTO.getRequestNo(), surveyDTO.getSurveyNo(),
									"SU008", "C", sessionBackingBean.getLoginUser());
							setDisabledBtns(true);
						} else {
							setErrorMessage("Please enter  remarks");
							RequestContext.getCurrentInstance().update("errorMSG");
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						}

					}

					else if (status.equals("A")) {

						setErrorMessage("Survey summery already approved");
						RequestContext.getCurrentInstance().update("errorMSG");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					} else if (status.equals("R")) {
						setErrorMessage("Survey summery already rejected");
						RequestContext.getCurrentInstance().update("errorMSG");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

					}

				} else {

					setErrorMessage("Please enter remarks");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			} else {
				setErrorMessage("Survey summary already approved");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

			}
		} else {

			setErrorMessage("Previos task is not completed");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		}

	}

	public void rejectButton() {

		taskStatus = surveyService.getTaskStatus(surveyDTO.getSurveyNo());
		status = surveyService.getApproveRejectStatus(surveyDTO.getSurveyNo());

		if (taskStatus != null) {
			if (taskStatus.equals("O")) {
				if (surveyDTO.getApproveRejectStatus() != null
						&& !surveyDTO.getApproveRejectStatus().trim().isEmpty()) {
					if (!"A".equals(status) && !"R".equals(status)) {
						if (surveyDTO.getApproveRejectStatus() != null
								&& !surveyDTO.getApproveRejectStatus().trim().isEmpty()) {

							updateDate = surveyService.updateRejectStatus(surveyDTO, loginUser);
							setSuccessMessage("Succesfully Rejected.");
							RequestContext.getCurrentInstance().update("successMSG");
							RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
							commonService.updateSurveyTaskDetails(surveyDTO.getRequestNo(), surveyDTO.getSurveyNo(),
									"SU008", "C", sessionBackingBean.getLoginUser());
							setDisabledBtns(true);
						} else {
							setErrorMessage("Please enter  remarks");
							RequestContext.getCurrentInstance().update("errorMSG");
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						}

					} else if (status.equals("A")) {

						setErrorMessage("Survey summery already approved");
						RequestContext.getCurrentInstance().update("errorMSG");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					} else if (status.equals("R")) {
						setErrorMessage("Survey summery already rejected");
						RequestContext.getCurrentInstance().update("errorMSG");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

					}

				} else {

					setErrorMessage("Please enter remarks");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			} else {

				setErrorMessage("Survey summary already saved.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

			}
		} else {

			setErrorMessage("Previos task is not completed");
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

	public List<SurveyDTO> getSurveyNoList() {
		return surveyNoList;
	}

	public void setSurveyNoList(List<SurveyDTO> surveyNoList) {
		this.surveyNoList = surveyNoList;
	}

	public List<SurveyDTO> getRouteNOList() {
		return routeNOList;
	}

	public void setRouteNOList(List<SurveyDTO> routeNOList) {
		this.routeNOList = routeNOList;
	}

	public boolean isRouteFlag() {
		return routeFlag;
	}

	public void setRouteFlag(boolean routeFlag) {
		this.routeFlag = routeFlag;
	}

	public List<SurveyDTO> getShowGridDataDTO() {
		return showGridDataDTO;
	}

	public void setShowGridDataDTO(List<SurveyDTO> showGridDataDTO) {
		this.showGridDataDTO = showGridDataDTO;
	}

	public SurveyDTO getSelectList() {
		return selectList;
	}

	public void setSelectList(SurveyDTO selectList) {
		this.selectList = selectList;
	}

	public List<SurveyDTO> getShowData() {
		return showData;
	}

	public void setShowData(List<SurveyDTO> showData) {
		this.showData = showData;
	}

	public String getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(String loginUser) {
		this.loginUser = loginUser;
	}

	public SurveyDTO getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(SurveyDTO selectedRow) {
		this.selectedRow = selectedRow;
	}

	public List<SurveyDTO> getSavedata() {
		return savedata;
	}

	public void setSavedata(List<SurveyDTO> savedata) {
		this.savedata = savedata;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	public List<SurveyDTO> getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(List<SurveyDTO> updateDate) {
		this.updateDate = updateDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean getEditFlag() {
		return editFlag;
	}

	public void setEditFlag(boolean editFlag) {
		this.editFlag = editFlag;
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

	public boolean isDisplayRouteNo() {
		return displayRouteNo;
	}

	public void setDisplayRouteNo(boolean displayRouteNo) {
		this.displayRouteNo = displayRouteNo;
	}

	public String getDuplicateRoute() {
		return duplicateRoute;
	}

	public void setDuplicateRoute(String duplicateRoute) {
		this.duplicateRoute = duplicateRoute;
	}

	public boolean isDisabledBtns() {
		return disabledBtns;
	}

	public void setDisabledBtns(boolean disabledBtns) {
		this.disabledBtns = disabledBtns;
	}

}
