package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.PrintInspectionDTO;
import lk.informatics.ntc.model.dto.VehicleInspectionDTO;
import lk.informatics.ntc.model.service.InspectionActionPointService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "viewNormalVehicleInspectionBean")
@ViewScoped
public class ViewNormalVehicleInspectionBean {
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	private String selectedApplicationNo;
	private String selectedVehicleNo;
	private String viewedApplicationNo;
	private String viewInspectionURL;
	private boolean checkPressedSearchBtn = false;
	private boolean disableEdit = false;

	private PrintInspectionDTO printInspectionDTO = new PrintInspectionDTO();
	private PrintInspectionDTO selectedViewRow;
	private PrintInspectionDTO vehicleNoForSelectedApplicationNo;
	private PrintInspectionDTO currentVehicleNoChange;
	private PrintInspectionDTO currentApplicationNoChange;
	private PrintInspectionDTO selectedEditRow;
	private VehicleInspectionDTO inspectionDTO;

	private InspectionActionPointService inspectionActionPointService;

	public List<PrintInspectionDTO> applicationNoList = new ArrayList<PrintInspectionDTO>(0);
	public List<PrintInspectionDTO> vehcileNoList = new ArrayList<PrintInspectionDTO>(0);
	public List<PrintInspectionDTO> dataList = new ArrayList<PrintInspectionDTO>(0);
	public List<PrintInspectionDTO> viewList = new ArrayList<PrintInspectionDTO>(0);

	@PostConstruct
	public void init() {

		inspectionActionPointService = (InspectionActionPointService) SpringApplicationContex
				.getBean("inspectionActionPointService");

		applicationNoList = inspectionActionPointService.getAllApplicationNoListForViewInspectionNormal();
		vehcileNoList = inspectionActionPointService.getAllVehicleNoListForViewInspectionNew();
		checkPressedSearchBtn = sessionBackingBean.isCheckIsSearchPressed();
		inspectionDTO = new VehicleInspectionDTO();
		assignedActivity();
		if (checkPressedSearchBtn == false) {

			defaultValues();

		} else {

			loadSearchValues();
			sessionBackingBean.setCheckIsSearchPressed(false);
		}

	}

	public void assignedActivity() {

		inspectionDTO = inspectionActionPointService.getAssignedFunction(sessionBackingBean.getLoginUser());

		setDisableEdit(inspectionDTO.isDisabledEditBtnInGrid());
		RequestContext.getCurrentInstance().update("cmdbtnEdit");

	}

	public void onApplicationNoChange() {

		currentVehicleNoChange = inspectionActionPointService.getCurrentVehicleNo(selectedApplicationNo);
		setSelectedVehicleNo(currentVehicleNoChange.getVehicleNo());
	}

	public void onVehicleNoChange() {

		currentApplicationNoChange = inspectionActionPointService.getCurrentApplicationNo(selectedVehicleNo);
		setSelectedApplicationNo(currentApplicationNoChange.getApplicationNo());
	}

	public void searchDetails() {
		checkPressedSearchBtn = true;

		if (!selectedApplicationNo.isEmpty() && selectedApplicationNo != null && !selectedApplicationNo.equals("")
				&& selectedVehicleNo.equals("")) {

			dataList = inspectionActionPointService.getAllRecordsForViewInspectionsNew(selectedApplicationNo,
					selectedVehicleNo);
			vehicleNoForSelectedApplicationNo = inspectionActionPointService
					.getDetailsForViewInspectionDetails(selectedApplicationNo, selectedVehicleNo);
			selectedApplicationNo = vehicleNoForSelectedApplicationNo.getApplicationNo();
			selectedVehicleNo = vehicleNoForSelectedApplicationNo.getVehicleNo();
		} else if (selectedApplicationNo.equals("") && !selectedVehicleNo.isEmpty() && selectedVehicleNo != null
				&& !selectedVehicleNo.equals("")) {

			dataList = inspectionActionPointService.getAllRecordsForViewInspectionsNew(selectedApplicationNo,
					selectedVehicleNo);
			vehicleNoForSelectedApplicationNo = inspectionActionPointService
					.getDetailsForViewInspectionDetails(selectedApplicationNo, selectedVehicleNo);
			selectedApplicationNo = vehicleNoForSelectedApplicationNo.getApplicationNo();
			selectedVehicleNo = vehicleNoForSelectedApplicationNo.getVehicleNo();
		} else if (!selectedApplicationNo.isEmpty() && selectedApplicationNo != null
				&& !selectedApplicationNo.equals("") && !selectedVehicleNo.isEmpty() && selectedVehicleNo != null
				&& !selectedVehicleNo.equals("")) {

			dataList = inspectionActionPointService.getAllRecordsForBothNew(selectedApplicationNo, selectedVehicleNo);
		} else if (selectedApplicationNo.equals("") && selectedVehicleNo.equals("")) {

			dataList = new ArrayList<PrintInspectionDTO>(0);

			applicationNoList = inspectionActionPointService.getAllApplicationNoListForViewInspectionNormal();
			viewList = new ArrayList<PrintInspectionDTO>(0);
			dataList = new ArrayList<PrintInspectionDTO>(0);
			viewList = inspectionActionPointService.getAllRecordsForViewInspectionsDefaultNew();
			dataList.addAll(viewList);

		}
	}

	public void clearFields() {

		setSelectedApplicationNo(null);
		setSelectedVehicleNo(null);
		dataList = new ArrayList<PrintInspectionDTO>(0);
		viewList = new ArrayList<PrintInspectionDTO>(0);
		applicationNoList = inspectionActionPointService.getAllApplicationNoListForViewInspectionNormal();

		viewList = inspectionActionPointService.getAllRecordsForViewInspectionsDefaultNew();
		dataList.addAll(viewList);
		checkPressedSearchBtn = false;

	}

	public String viewActButtonAction() {

		viewedApplicationNo = selectedViewRow.getOwnerApplicationNo();

		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		viewInspectionURL = request.getRequestURL().toString();
		sessionBackingBean.setViewInspectionsURL(viewInspectionURL);
		sessionBackingBean.setViewInspectionsURLStatus(true);
		sessionBackingBean.setViewedInspectionApplicationNo(viewedApplicationNo);
		sessionBackingBean.setSearchedApplicationNo(selectedApplicationNo);
		sessionBackingBean.setSearchedVehicleNo(selectedVehicleNo);
		sessionBackingBean.setCheckIsSearchPressed(checkPressedSearchBtn);
		sessionBackingBean.setReadOnlyFieldsInspection(true);
		sessionBackingBean.setSelectedOptionType("VIEW");
		sessionBackingBean.setVehicleInspectionType(true);

		return "/pages/vehicleInspectionSet/normalVehicleInspectionInfoViewMode.xhtml";
	}

	public void loadSearchValues() {

		selectedApplicationNo = sessionBackingBean.getSearchedApplicationNo();
		selectedVehicleNo = sessionBackingBean.getSearchedVehicleNo();
		checkPressedSearchBtn = false;
		if (!selectedApplicationNo.isEmpty() && selectedApplicationNo != null && !selectedApplicationNo.equals("")
				&& selectedVehicleNo.equals("")) {

			dataList = inspectionActionPointService.getAllRecordsForViewInspectionsNew(selectedApplicationNo,
					selectedVehicleNo);
			vehicleNoForSelectedApplicationNo = inspectionActionPointService
					.getDetailsForViewInspectionDetails(selectedApplicationNo, selectedVehicleNo);
			selectedApplicationNo = vehicleNoForSelectedApplicationNo.getApplicationNo();
			selectedVehicleNo = vehicleNoForSelectedApplicationNo.getVehicleNo();
		} else if (selectedApplicationNo.equals("") && !selectedVehicleNo.isEmpty() && selectedVehicleNo != null
				&& !selectedVehicleNo.equals("")) {

			dataList = inspectionActionPointService.getAllRecordsForViewInspectionsNew(selectedApplicationNo,
					selectedVehicleNo);
			vehicleNoForSelectedApplicationNo = inspectionActionPointService
					.getDetailsForViewInspectionDetails(selectedApplicationNo, selectedVehicleNo);
			selectedApplicationNo = vehicleNoForSelectedApplicationNo.getApplicationNo();
			selectedVehicleNo = vehicleNoForSelectedApplicationNo.getVehicleNo();
		} else if (!selectedApplicationNo.isEmpty() && selectedApplicationNo != null
				&& !selectedApplicationNo.equals("") && !selectedVehicleNo.isEmpty() && selectedVehicleNo != null
				&& !selectedVehicleNo.equals("")) {

			dataList = inspectionActionPointService.getAllRecordsForBothNew(selectedApplicationNo, selectedVehicleNo);
		} else if (selectedApplicationNo.equals("") && selectedVehicleNo.equals("")) {

			dataList = new ArrayList<PrintInspectionDTO>(0);

			defaultValues();

		}
	}

	public void defaultValues() {

		viewList = new ArrayList<PrintInspectionDTO>(0);
		dataList = new ArrayList<PrintInspectionDTO>(0);
		viewList = inspectionActionPointService.getAllRecordsForViewInspectionsDefaultNew();
		dataList.addAll(viewList);

	}

	public String editAction() {

		viewedApplicationNo = selectedEditRow.getOwnerApplicationNo();

		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		viewInspectionURL = request.getRequestURL().toString();
		sessionBackingBean.setViewInspectionsURL(viewInspectionURL);
		sessionBackingBean.setViewInspectionsURLStatus(true);
		sessionBackingBean.setViewedInspectionApplicationNo(viewedApplicationNo);
		sessionBackingBean.setSearchedApplicationNo(selectedApplicationNo);
		sessionBackingBean.setSearchedVehicleNo(selectedVehicleNo);
		sessionBackingBean.setCheckIsSearchPressed(checkPressedSearchBtn);
		sessionBackingBean.setReadOnlyFieldsInspection(false);
		sessionBackingBean.setSelectedOptionType("EDIT");

		return "/pages/vehicleInspectionSet/normalVehicleInspectionInfoViewMode.xhtml";
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

	public PrintInspectionDTO getPrintInspectionDTO() {
		return printInspectionDTO;
	}

	public void setPrintInspectionDTO(PrintInspectionDTO printInspectionDTO) {
		this.printInspectionDTO = printInspectionDTO;
	}

	public PrintInspectionDTO getSelectedViewRow() {
		return selectedViewRow;
	}

	public void setSelectedViewRow(PrintInspectionDTO selectedViewRow) {
		this.selectedViewRow = selectedViewRow;
	}

	public InspectionActionPointService getInspectionActionPointService() {
		return inspectionActionPointService;
	}

	public void setInspectionActionPointService(InspectionActionPointService inspectionActionPointService) {
		this.inspectionActionPointService = inspectionActionPointService;
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

	public List<PrintInspectionDTO> getViewList() {
		return viewList;
	}

	public void setViewList(List<PrintInspectionDTO> viewList) {
		this.viewList = viewList;
	}

	public String getViewInspectionURL() {
		return viewInspectionURL;
	}

	public void setViewInspectionURL(String viewInspectionURL) {
		this.viewInspectionURL = viewInspectionURL;
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

	public PrintInspectionDTO getSelectedEditRow() {
		return selectedEditRow;
	}

	public void setSelectedEditRow(PrintInspectionDTO selectedEditRow) {
		this.selectedEditRow = selectedEditRow;
	}

	public boolean isDisableEdit() {
		return disableEdit;
	}

	public void setDisableEdit(boolean disableEdit) {
		this.disableEdit = disableEdit;
	}

	public VehicleInspectionDTO getInspectionDTO() {
		return inspectionDTO;
	}

	public void setInspectionDTO(VehicleInspectionDTO inspectionDTO) {
		this.inspectionDTO = inspectionDTO;
	}

}
