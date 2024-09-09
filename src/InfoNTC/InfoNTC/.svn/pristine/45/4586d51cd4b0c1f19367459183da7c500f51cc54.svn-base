package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.PrintInspectionDTO;
import lk.informatics.ntc.model.service.IncompleteApprovalService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "incompleteApprovalBackingBean")
@ViewScoped
public class IncompleteApprovalBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private String selectedApplicationNo;
	private String selectedVehicleNo;
	private String selectedInspectionStatus;
	private String viewedApplicationNo;
	private String incompleteApprovalURL;
	private boolean checkPressedSearchBtn = false;

	private PrintInspectionDTO selectedViewRow;
	private PrintInspectionDTO vehicleNoForSelectedApplicationNo;
	private PrintInspectionDTO currentVehicleNoChange;
	private PrintInspectionDTO currentApplicationNoChange;

	private IncompleteApprovalService incompleteApprovalService;

	public List<PrintInspectionDTO> applicationNoList = new ArrayList<PrintInspectionDTO>(0);
	public List<PrintInspectionDTO> vehcileNoList = new ArrayList<PrintInspectionDTO>(0);
	public List<PrintInspectionDTO> dataList = new ArrayList<PrintInspectionDTO>(0);

	public String errorMessage;

	@PostConstruct
	public void init() {
		incompleteApprovalService = (IncompleteApprovalService) SpringApplicationContex
				.getBean("incompleteApprovalService");
		loadInitData();
	}

	private void loadInitData() {
		applicationNoList = incompleteApprovalService.getAllApplicationNoListForIncompleteApproval();
		vehcileNoList = incompleteApprovalService.getAllVehicleNoListForIncompleteApproval();
		checkPressedSearchBtn = sessionBackingBean.isCheckIsSearchPressed();

		if (checkPressedSearchBtn == true && sessionBackingBean.isCheckIsBackPressed()) {

			loadSearchValues();
			sessionBackingBean.setCheckIsBackPressed(false);
		} else {

			setSelectedInspectionStatus("I");// I for INCOMPLETE
			defaultValues();
			sessionBackingBean.setCheckIsSearchPressed(false);
			sessionBackingBean.setCheckIsBackPressed(false);
		}

	}

	public void onApplicationNoChange() {
		currentVehicleNoChange = incompleteApprovalService.getCurrentVehicleNo(selectedApplicationNo);
		setSelectedVehicleNo(currentVehicleNoChange.getVehicleNo());
	}

	public void searchDetails() {
		checkPressedSearchBtn = true;

		dataList = incompleteApprovalService.searchApplications(selectedApplicationNo, selectedVehicleNo,
				selectedInspectionStatus);
	}

	public void clear() {
		setSelectedApplicationNo(null);
		setSelectedVehicleNo(null);
		setSelectedInspectionStatus("I");// I for INCOMPLETE
		applicationNoList = incompleteApprovalService.getAllApplicationNoListForIncompleteApproval();
		vehcileNoList = incompleteApprovalService.getAllVehicleNoListForIncompleteApproval();
		dataList = incompleteApprovalService.searchApplications(selectedApplicationNo, selectedVehicleNo,
				selectedInspectionStatus);
		checkPressedSearchBtn = false;

		sessionBackingBean.setSearchedApplicationNo(null);
		sessionBackingBean.setSearchedVehicleNo(null);
		sessionBackingBean.setSearchedInspectionStatus(null);
		sessionBackingBean.setCheckIsSearchPressed(false);
		sessionBackingBean.setCheckIsBackPressed(false);
		sessionBackingBean.setViewedInspectionApplicationNo(null);
		sessionBackingBean.setSelectedOptionType("");
	}

	public void viewActButtonAction() {

		sessionBackingBean.setSearchedApplicationNo(selectedApplicationNo);
		sessionBackingBean.setSearchedVehicleNo(selectedVehicleNo);
		sessionBackingBean.setSearchedInspectionStatus(selectedInspectionStatus);
		sessionBackingBean.setCheckIsSearchPressed(checkPressedSearchBtn);
		sessionBackingBean.setCheckIsBackPressed(false);

		try {

			if (selectedViewRow.getInspectionType().equals("PI") || selectedViewRow.getInspectionType().equals("AI")) {
				sessionBackingBean.setViewedInspectionApplicationNo(selectedViewRow.getOwnerApplicationNo());// for
																												// normal
																												// inspection
																												// only
				sessionBackingBean.setSelectedOptionType("VIEW");

				// redirect to normal inspection view mode
				FacesContext fcontext = FacesContext.getCurrentInstance();
				fcontext.getExternalContext().getSessionMap().put("VIEW_NORMAL_INCOMPLETE", selectedViewRow);
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect("/InfoNTC/pages/vehicleInspectionSet/normalVehicleInspectionInfoViewMode.xhtml");

			} else if (selectedViewRow.getInspectionType().equals("CI")
					|| selectedViewRow.getInspectionType().equals("II")
					|| selectedViewRow.getInspectionType().equals("SI")) {

				// redirect to other inspection view mode
				FacesContext fcontext = FacesContext.getCurrentInstance();
				fcontext.getExternalContext().getSessionMap().put("VIEW_OTHER_INCOMPLETE", selectedViewRow);
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect("/InfoNTC/pages/vehicleInspectionSet/viewOtherInspection.xhtml");

			}

		} catch (Exception e) {
			sessionBackingBean.setSearchedApplicationNo(null);
			sessionBackingBean.setViewedInspectionApplicationNo(null);
			sessionBackingBean.setSearchedVehicleNo(null);
			sessionBackingBean.setSearchedInspectionStatus(null);
			sessionBackingBean.setCheckIsSearchPressed(false);
			sessionBackingBean.setCheckIsBackPressed(false);

			setErrorMessage("Error: Cannot view the selected record.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			e.printStackTrace();
		}
	}

	public void loadSearchValues() {
		selectedApplicationNo = sessionBackingBean.getSearchedApplicationNo();
		selectedVehicleNo = sessionBackingBean.getSearchedVehicleNo();
		selectedInspectionStatus = sessionBackingBean.getSearchedInspectionStatus();

		// search matching applications for entered parameters
		dataList = incompleteApprovalService.searchApplications(selectedApplicationNo, selectedVehicleNo,
				selectedInspectionStatus);
	}

	public void defaultValues() {
		dataList = incompleteApprovalService.searchApplications(selectedApplicationNo, selectedVehicleNo,
				selectedInspectionStatus);
	}

	public String getSelectedApplicationNo() {
		return selectedApplicationNo;
	}

	public void setSelectedApplicationNo(String selectedApplicationNo) {
		this.selectedApplicationNo = selectedApplicationNo;
	}

	public String getSelectedVehicleNo() {
		return selectedVehicleNo;
	}

	public void setSelectedVehicleNo(String selectedVehicleNo) {
		this.selectedVehicleNo = selectedVehicleNo;
	}

	public String getSelectedInspectionStatus() {
		return selectedInspectionStatus;
	}

	public void setSelectedInspectionStatus(String selectedInspectionStatus) {
		this.selectedInspectionStatus = selectedInspectionStatus;
	}

	public PrintInspectionDTO getSelectedViewRow() {
		return selectedViewRow;
	}

	public void setSelectedViewRow(PrintInspectionDTO selectedViewRow) {
		this.selectedViewRow = selectedViewRow;
	}

	public IncompleteApprovalService getIncompleteApprovalService() {
		return incompleteApprovalService;
	}

	public void setIncompleteApprovalService(IncompleteApprovalService incompleteApprovalService) {
		this.incompleteApprovalService = incompleteApprovalService;
	}

	public List<PrintInspectionDTO> getApplicationNoAndVheicleNoList() {
		return applicationNoList;
	}

	public void setApplicationNoAndVheicleNoList(List<PrintInspectionDTO> applicationNoAndVheicleNoList) {
		this.applicationNoList = applicationNoAndVheicleNoList;
	}

	public List<PrintInspectionDTO> getDataList() {
		return dataList;
	}

	public void setDataList(List<PrintInspectionDTO> dataList) {
		this.dataList = dataList;
	}

	public String getIncompleteApprovalURL() {
		return incompleteApprovalURL;
	}

	public void setIncompleteApprovalURL(String incompleteApprovalURL) {
		this.incompleteApprovalURL = incompleteApprovalURL;
	}

	public PrintInspectionDTO getVehicleNoForSelectedApplicationNo() {
		return vehicleNoForSelectedApplicationNo;
	}

	public void setVehicleNoForSelectedApplicationNo(PrintInspectionDTO vehicleNoForSelectedApplicationNo) {
		this.vehicleNoForSelectedApplicationNo = vehicleNoForSelectedApplicationNo;
	}

	public String getViewedApplicationNo() {
		return viewedApplicationNo;
	}

	public void setViewedApplicationNo(String viewedApplicationNo) {
		this.viewedApplicationNo = viewedApplicationNo;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public boolean isCheckPressedSearchBtn() {
		return checkPressedSearchBtn;
	}

	public void setCheckPressedSearchBtn(boolean checkPressedSearchBtn) {
		this.checkPressedSearchBtn = checkPressedSearchBtn;
	}

	public List<PrintInspectionDTO> getApplicationNoList() {
		return applicationNoList;
	}

	public void setApplicationNoList(List<PrintInspectionDTO> applicationNoList) {
		this.applicationNoList = applicationNoList;
	}

	public List<PrintInspectionDTO> getVehcileNoList() {
		return vehcileNoList;
	}

	public void setVehcileNoList(List<PrintInspectionDTO> vehcileNoList) {
		this.vehcileNoList = vehcileNoList;
	}

	public PrintInspectionDTO getCurrentVehicleNoChange() {
		return currentVehicleNoChange;
	}

	public void setCurrentVehicleNoChange(PrintInspectionDTO currentVehicleNoChange) {
		this.currentVehicleNoChange = currentVehicleNoChange;
	}

	public PrintInspectionDTO getCurrentApplicationNoChange() {
		return currentApplicationNoChange;
	}

	public void setCurrentApplicationNoChange(PrintInspectionDTO currentApplicationNoChange) {
		this.currentApplicationNoChange = currentApplicationNoChange;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
