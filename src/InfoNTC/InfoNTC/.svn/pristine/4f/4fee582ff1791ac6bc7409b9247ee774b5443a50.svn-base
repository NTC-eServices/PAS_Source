package lk.informatics.ntc.view.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.DocumentManagementDTO;
import lk.informatics.ntc.model.dto.SisuSeriyaDTO;
import lk.informatics.ntc.model.service.AdminService;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.DocumentManagementService;
import lk.informatics.ntc.model.service.SisuSariyaService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "sisuAuthorizationTeamBackingBean")
@ViewScoped
public class SisuAuthorizationTeamMaintenanceBackingBean {
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	// services
	private DocumentManagementService documentManagementService;
	private AdminService adminService;
	private CommonService commonService;

	private SisuSariyaService sisuSariyaService;
	// DTO

	private SisuSeriyaDTO sisuSariyaTeamAuthDTO;

	private SisuSeriyaDTO fillServiceNO;
	private List<SisuSeriyaDTO> fillServiceRefNoList = new ArrayList<SisuSeriyaDTO>();
	private List<SisuSeriyaDTO> serviceNOList = new ArrayList<SisuSeriyaDTO>();
	private List<SisuSeriyaDTO> serviceRefNOList = new ArrayList<SisuSeriyaDTO>();
	private List<SisuSeriyaDTO> operatorDepoNameList = new ArrayList<SisuSeriyaDTO>(0);
	private List<SisuSeriyaDTO> showSearchData;
	private List<SisuSeriyaDTO> showDataOnGrid;
	private SisuSeriyaDTO selectedRow;
	private SisuSeriyaDTO selectedRow1;
	private List<CommonDTO> drpdProvincelList;
	private List<CommonDTO> drpdDistrictList;
	private List<CommonDTO> drpdDevsecList;

	private int activeTabIndex;
	private String selectedReqNo, selectedRequestorType, selectedPrefLang, selectedReqType, selectedIdNo,
			successMessage, errorMessage, loginUser, errorMsg;
	private boolean editFlag;
	private boolean disableServiceNo, createMode, viewMode, renderButton;
	String endDateValue;
	String startDateValue;
	// ArrayLists
	private List<DocumentManagementDTO> mandatoryList = new ArrayList<DocumentManagementDTO>(0);
	private List<DocumentManagementDTO> optionalList = new ArrayList<DocumentManagementDTO>(0);

	@PostConstruct
	public void init() {
		sisuSariyaService = (SisuSariyaService) SpringApplicationContex.getBean("sisuSariyaService");
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");

		serviceNOList = sisuSariyaService.getServiceNoList1();
		serviceRefNOList = sisuSariyaService.getApprovedServiceRefNoListForTeam();
		operatorDepoNameList = sisuSariyaService.getApprovedOperatorDepoNameListForTeam();
		sisuSariyaTeamAuthDTO = new SisuSeriyaDTO();

		drpdProvincelList = adminService.getProvinceToDropdown();
		drpdDevsecList = adminService.getDivSecToDropdown();
		drpdDistrictList = adminService.getDistrictToDropdown();
		loginUser = sessionBackingBean.getLoginUser();
		activeTabIndex = 0;
		disableServiceNo = true;
		documentManagementService = (DocumentManagementService) SpringApplicationContex
				.getBean("documentManagementService");

		createMode = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN109", "C");
		viewMode = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN109", "V");

		if (createMode) {
			renderButton = true;
		}

	}

	public void onServicetStartDateChange(SelectEvent event) throws ParseException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		String dateFormat = "yyyy/MM/dd";
		SimpleDateFormat frm = new SimpleDateFormat(dateFormat);
		startDateValue = frm.format(event.getObject());

		Date startDateValueObj = frm.parse(startDateValue);

		sisuSariyaTeamAuthDTO.setServiceStartDateVal(startDateValue);
		if (sisuSariyaTeamAuthDTO.getServiceStartDateObj() != null) {

			serviceRefNOList = sisuSariyaService.getServiceRefNoList(sisuSariyaTeamAuthDTO);
		}

	}

	public void onRequestEndDateChange(SelectEvent event) throws ParseException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		String dateFormat = "yyyy/MM/dd";
		SimpleDateFormat frm = new SimpleDateFormat(dateFormat);
		endDateValue = frm.format(event.getObject());

		Date startDateValueObj = frm.parse(endDateValue);

		sisuSariyaTeamAuthDTO.setServiceEndDateVal(endDateValue);
		if (sisuSariyaTeamAuthDTO.getServiceEndDateVal() != null
				&& !sisuSariyaTeamAuthDTO.getServiceEndDateVal().isEmpty()
				&& !sisuSariyaTeamAuthDTO.getServiceEndDateVal().equalsIgnoreCase("")) {

			serviceRefNOList = sisuSariyaService.getServiceRefNoList(sisuSariyaTeamAuthDTO);
		}

	}

	public void fillServiceNo() {
		fillServiceNO = sisuSariyaService.fillSerrviceNO(sisuSariyaTeamAuthDTO);
		sisuSariyaTeamAuthDTO.setServiceNo(fillServiceNO.getServiceNo());
		disableServiceNo = false;

	}

	public void searchActionForFirst() {
		if ((sisuSariyaTeamAuthDTO.getServiceRefNo() == null
				|| sisuSariyaTeamAuthDTO.getServiceRefNo().trim().equalsIgnoreCase(""))
				&& (sisuSariyaTeamAuthDTO.getServiceNo() == null
						|| sisuSariyaTeamAuthDTO.getServiceNo().trim().equalsIgnoreCase(""))
				&& (sisuSariyaTeamAuthDTO.getServiceStartDateObj() == null)
				&& (sisuSariyaTeamAuthDTO.getServiceEndDateObj() == null)
				&& (sisuSariyaTeamAuthDTO.getNameOfOperator() == null
						|| sisuSariyaTeamAuthDTO.getNameOfOperator().trim().equalsIgnoreCase(""))) {

			setErrorMessage("Please select at least one field.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		} else {
			showSearchData = sisuSariyaService.showSearchedDataForAuthorizationTeamMaintenance(
					sisuSariyaTeamAuthDTO.getServiceStartDateObj(), sisuSariyaTeamAuthDTO.getServiceEndDateObj(),
					sisuSariyaTeamAuthDTO.getServiceRefNo(), sisuSariyaTeamAuthDTO.getServiceNo(),
					sisuSariyaTeamAuthDTO.getNameOfOperator());

			showDataOnGrid = sisuSariyaService.getTeamMainteanDataonGrid(sisuSariyaTeamAuthDTO);
		}

	}

	public void clearFields() {
		sisuSariyaTeamAuthDTO = new SisuSeriyaDTO();
		showSearchData = new ArrayList<SisuSeriyaDTO>();
	}

	public void clearFields2() {
		sisuSariyaTeamAuthDTO = new SisuSeriyaDTO();
		showSearchData = new ArrayList<SisuSeriyaDTO>();
		showDataOnGrid = new ArrayList<SisuSeriyaDTO>();
	}

	public void addButton() {
		if (selectedRow != null && !selectedRow.getServiceRefNo().trim().equals("")) {
			sisuSariyaTeamAuthDTO.setServiceRefNo(selectedRow.getServiceRefNo());
		}
		
		if (sisuSariyaTeamAuthDTO.getServiceRefNo() != null
				&& !sisuSariyaTeamAuthDTO.getServiceRefNo().trim().equals("")) {
			if (editFlag) {

				sisuSariyaService.updateTeamInformationData(sisuSariyaTeamAuthDTO, loginUser,
						selectedRow1.getAuthPerson(), selectedRow1.getDesignationCode(), selectedRow1.getNicNo());

				showDataOnGrid = sisuSariyaService.getTeamMainteanDataonGrid(sisuSariyaTeamAuthDTO);
				sisuSariyaTeamAuthDTO.setAuthPerson("");
				sisuSariyaTeamAuthDTO.setDesignationCode("");
				sisuSariyaTeamAuthDTO.setNicNo("");
				sisuSariyaTeamAuthDTO.setTelNo("");
				sisuSariyaTeamAuthDTO.setIsActive("");
			} else {
				if (sisuSariyaTeamAuthDTO.getAuthPerson() != null
						&& !sisuSariyaTeamAuthDTO.getAuthPerson().trim().isEmpty()) {
					if (sisuSariyaTeamAuthDTO.getDesignationCode() != null
							&& !sisuSariyaTeamAuthDTO.getDesignationCode().trim().isEmpty()) {
						sisuSariyaService.saveTeamInformationData(sisuSariyaTeamAuthDTO, loginUser);

						showDataOnGrid = sisuSariyaService.getTeamMainteanDataonGrid(sisuSariyaTeamAuthDTO);
						setSuccessMessage("Succesfully Added.");
						RequestContext.getCurrentInstance().update("successMSG");
						RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
						sisuSariyaTeamAuthDTO.setAuthPerson("");
						sisuSariyaTeamAuthDTO.setDesignationCode("");
						sisuSariyaTeamAuthDTO.setNicNo("");
						sisuSariyaTeamAuthDTO.setTelNo("");
						sisuSariyaTeamAuthDTO.setIsActive("");

					} else {
						setErrorMessage("Please Enter Designation");
						RequestContext.getCurrentInstance().update("errorMSG");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}
				}

				else {
					setErrorMessage("Please Enter Authorized Person Name");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			}
		} else {
			setErrorMessage("Service Reference No. is empty.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void deleteButton() {

		sisuSariyaService.deleteTeamAuthData(sisuSariyaTeamAuthDTO, selectedRow1.getAuthPerson(),
				selectedRow1.getDesignationCode(), selectedRow1.getNicNo(), selectedRow1.getTelNo());
		showDataOnGrid = sisuSariyaService.getTeamMainteanDataonGrid(sisuSariyaTeamAuthDTO);
		setSuccessMessage("Succesfully Deleted.");
		RequestContext.getCurrentInstance().update("successMSG");
		RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

	}

	public void editButton() {

		sisuSariyaTeamAuthDTO.setAuthPerson(selectedRow1.getAuthPerson());

		sisuSariyaTeamAuthDTO.setDesignationCode(selectedRow1.getDesignationCode());

		sisuSariyaTeamAuthDTO.setNicNo(selectedRow1.getNicNo());

		sisuSariyaTeamAuthDTO.setTelNo(selectedRow1.getTelNo());

		if (selectedRow1.getIsActive().equals("Active")) {
			sisuSariyaTeamAuthDTO.setIsActive("true");

		} else {
			sisuSariyaTeamAuthDTO.setIsActive("false");
		}

		editFlag = true;

	}

	public void clearAddDet() {

		sisuSariyaTeamAuthDTO = new SisuSeriyaDTO();
		showDataOnGrid = new ArrayList<SisuSeriyaDTO>();

	}

	public void documentManagement() {

		try {

			sessionBackingBean.setServiceNoForSisuSariya(sisuSariyaTeamAuthDTO.getServiceNo());
			sessionBackingBean.setServiceRefNo(sisuSariyaTeamAuthDTO.getServiceRefNo());

			String requestNo = sisuSariyaService.getRequestNoByServiceNo(sisuSariyaTeamAuthDTO.getServiceRefNo(),
					sisuSariyaTeamAuthDTO.getServiceNo());

			sessionBackingBean.setRequestNoForSisuSariya(requestNo);

			sessionBackingBean.setTransactionType("SISU SARIYA");

			mandatoryList = documentManagementService.mandatoryDocsForSisuSariyaAgreementRenewals("18",
					sisuSariyaTeamAuthDTO.getServiceNo());
			optionalList = documentManagementService.optionalDocsForSisuSariyaAgreementRenewals("18",
					sisuSariyaTeamAuthDTO.getServiceNo());

			sessionBackingBean.sisuSariyaMandatoryDocumentList = documentManagementService
					.sisuSariyaMandatoryList(sisuSariyaTeamAuthDTO.getRequestNo());
			sessionBackingBean.sisuSariyaOptionalDocumentList = documentManagementService
					.sisuSariyaOptionalList(sisuSariyaTeamAuthDTO.getRequestNo());

			if (sisuSariyaTeamAuthDTO.getServiceRefNo() != null && sisuSariyaTeamAuthDTO.getRequestNo() != null) {

				sessionBackingBean.sisuSariyaServiceRefMandatoryDocumentList = documentManagementService
						.sisuSariyaPermitHolderMandatoryList(sisuSariyaTeamAuthDTO.getRequestNo(),
								sisuSariyaTeamAuthDTO.getServiceRefNo());
				sessionBackingBean.sisuSariyaServiceRefOptionalDocumentList = documentManagementService
						.sisuSariyaPermitHolderOptionalList(sisuSariyaTeamAuthDTO.getRequestNo(),
								sisuSariyaTeamAuthDTO.getServiceRefNo());
			}

			if (sisuSariyaTeamAuthDTO.getServiceNo() != null && sisuSariyaTeamAuthDTO.getServiceRefNo() != null
					&& sisuSariyaTeamAuthDTO.getRequestNo() != null) {

				sessionBackingBean.sisuSariyaServiceMandatoryDocumentList = documentManagementService
						.sisuSariyaAgreementRenewalsMandatoryList(sisuSariyaTeamAuthDTO.getRequestNo(),
								sisuSariyaTeamAuthDTO.getServiceRefNo(), sisuSariyaTeamAuthDTO.getServiceNo());
				sessionBackingBean.sisuSariyaServiceOptionalDocumentList = documentManagementService
						.sisuSariyaAgreementRenewalsOptionalList(sisuSariyaTeamAuthDTO.getRequestNo(),
								sisuSariyaTeamAuthDTO.getServiceRefNo(), sisuSariyaTeamAuthDTO.getServiceNo());

			}

			RequestContext.getCurrentInstance().execute("PF('uploadDocument').show()");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getActiveTabIndex() {
		return activeTabIndex;
	}

	public void setActiveTabIndex(int activeTabIndex) {
		this.activeTabIndex = activeTabIndex;
	}

	public String getSelectedReqNo() {
		return selectedReqNo;
	}

	public void setSelectedReqNo(String selectedReqNo) {
		this.selectedReqNo = selectedReqNo;
	}

	public String getSelectedRequestorType() {
		return selectedRequestorType;
	}

	public void setSelectedRequestorType(String selectedRequestorType) {
		this.selectedRequestorType = selectedRequestorType;
	}

	public String getSelectedPrefLang() {
		return selectedPrefLang;
	}

	public void setSelectedPrefLang(String selectedPrefLang) {
		this.selectedPrefLang = selectedPrefLang;
	}

	public String getSelectedReqType() {
		return selectedReqType;
	}

	public void setSelectedReqType(String selectedReqType) {
		this.selectedReqType = selectedReqType;
	}

	public String getSelectedIdNo() {
		return selectedIdNo;
	}

	public void setSelectedIdNo(String selectedIdNo) {
		this.selectedIdNo = selectedIdNo;
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

	public SisuSeriyaDTO getsisuSariyaTeamAuthDTO() {
		return sisuSariyaTeamAuthDTO;
	}

	public void setsisuSariyaTeamAuthDTO(SisuSeriyaDTO sisuSariyaTeamAuthDTO) {
		this.sisuSariyaTeamAuthDTO = sisuSariyaTeamAuthDTO;
	}

	public SisuSariyaService getSisuSariyaService() {
		return sisuSariyaService;
	}

	public void setSisuSariyaService(SisuSariyaService sisuSariyaService) {
		this.sisuSariyaService = sisuSariyaService;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	public String getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(String loginUser) {
		this.loginUser = loginUser;
	}

	public SisuSeriyaDTO getSisuSariyaTeamAuthDTO() {
		return sisuSariyaTeamAuthDTO;
	}

	public void setSisuSariyaTeamAuthDTO(SisuSeriyaDTO sisuSariyaTeamAuthDTO) {
		this.sisuSariyaTeamAuthDTO = sisuSariyaTeamAuthDTO;
	}

	public List<SisuSeriyaDTO> getServiceRefNOList() {
		return serviceRefNOList;
	}

	public void setServiceRefNOList(List<SisuSeriyaDTO> serviceRefNOList) {
		this.serviceRefNOList = serviceRefNOList;
	}

	public List<SisuSeriyaDTO> getShowDataOnGrid() {
		return showDataOnGrid;
	}

	public void setShowDataOnGrid(List<SisuSeriyaDTO> showDataOnGrid) {
		this.showDataOnGrid = showDataOnGrid;
	}

	public SisuSeriyaDTO getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(SisuSeriyaDTO selectedRow) {
		this.selectedRow = selectedRow;
	}

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public List<CommonDTO> getDrpdProvincelList() {
		return drpdProvincelList;
	}

	public void setDrpdProvincelList(List<CommonDTO> drpdProvincelList) {
		this.drpdProvincelList = drpdProvincelList;
	}

	public List<SisuSeriyaDTO> getServiceNOList() {
		return serviceNOList;
	}

	public void setServiceNOList(List<SisuSeriyaDTO> serviceNOList) {
		this.serviceNOList = serviceNOList;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public List<CommonDTO> getDrpdDistrictList() {
		return drpdDistrictList;
	}

	public void setDrpdDistrictList(List<CommonDTO> drpdDistrictList) {
		this.drpdDistrictList = drpdDistrictList;
	}

	public List<CommonDTO> getDrpdDevsecList() {
		return drpdDevsecList;
	}

	public void setDrpdDevsecList(List<CommonDTO> drpdDevsecList) {
		this.drpdDevsecList = drpdDevsecList;
	}

	public SisuSeriyaDTO getFillServiceNO() {
		return fillServiceNO;
	}

	public void setFillServiceNO(SisuSeriyaDTO fillServiceNO) {
		this.fillServiceNO = fillServiceNO;
	}

	public List<SisuSeriyaDTO> getShowSearchData() {
		return showSearchData;
	}

	public void setShowSearchData(List<SisuSeriyaDTO> showSearchData) {
		this.showSearchData = showSearchData;
	}

	public SisuSeriyaDTO getSelectedRow1() {
		return selectedRow1;
	}

	public void setSelectedRow1(SisuSeriyaDTO selectedRow1) {
		this.selectedRow1 = selectedRow1;
	}

	public List<SisuSeriyaDTO> getFillServiceRefNoList() {
		return fillServiceRefNoList;
	}

	public void setFillServiceRefNoList(List<SisuSeriyaDTO> fillServiceRefNoList) {
		this.fillServiceRefNoList = fillServiceRefNoList;
	}

	public String getEndDateValue() {
		return endDateValue;
	}

	public void setEndDateValue(String endDateValue) {
		this.endDateValue = endDateValue;
	}

	public String getStartDateValue() {
		return startDateValue;
	}

	public void setStartDateValue(String startDateValue) {
		this.startDateValue = startDateValue;
	}

	public boolean isEditFlag() {
		return editFlag;
	}

	public void setEditFlag(boolean editFlag) {
		this.editFlag = editFlag;
	}

	public boolean isDisableServiceNo() {
		return disableServiceNo;
	}

	public void setDisableServiceNo(boolean disableServiceNo) {
		this.disableServiceNo = disableServiceNo;
	}

	public boolean isCreateMode() {
		return createMode;
	}

	public void setCreateMode(boolean createMode) {
		this.createMode = createMode;
	}

	public boolean isViewMode() {
		return viewMode;
	}

	public void setViewMode(boolean viewMode) {
		this.viewMode = viewMode;
	}

	public boolean isRenderButton() {
		return renderButton;
	}

	public void setRenderButton(boolean renderButton) {
		this.renderButton = renderButton;
	}

	public List<SisuSeriyaDTO> getOperatorDepoNameList() {
		return operatorDepoNameList;
	}

	public void setOperatorDepoNameList(List<SisuSeriyaDTO> operatorDepoNameList) {
		this.operatorDepoNameList = operatorDepoNameList;
	}

}
