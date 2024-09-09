package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.SisuSeriyaDTO;
import lk.informatics.ntc.model.service.SisuSariyaService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "commonSisuSeriyaInquiryBackingBean")
@ViewScoped
public class CommonSisuSeriyaInquiryBackingBean {
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	// Service
	private SisuSariyaService sisuSariyaService;

	// List
	private List<SisuSeriyaDTO> vehicleNoList = new ArrayList<SisuSeriyaDTO>(0);
	private List<SisuSeriyaDTO> serviceNoList = new ArrayList<SisuSeriyaDTO>(0);
	private List<SisuSeriyaDTO> viaValueList = new ArrayList<SisuSeriyaDTO>(0);
	private List<SisuSeriyaDTO> viaDataList = new ArrayList<SisuSeriyaDTO>(0);
	private List<SisuSeriyaDTO> ownerNameList = new ArrayList<SisuSeriyaDTO>(0);
	private List<SisuSeriyaDTO> serviceDataList = new ArrayList<SisuSeriyaDTO>(0);

	private String console;
	private boolean showServiceInfoDropdownMenu = false;
	private String selectedVehicleNo;
	private String selectedServiceNo;
	private String selectedViaValue;
	private boolean showRouteInfoDropdownMenu = false;
	private String errorMsg;
	private boolean showRouteInfoDetails = false;
	private String selectedOwnerName;
	private boolean disabledServiceNoMenu = true;
	private boolean showServiceInfoDetails = false;

	boolean isSelectedVehicleNo = false;
	boolean isSelectedOwnerName = false;

	@PostConstruct
	public void init() {
		sisuSariyaService = (SisuSariyaService) SpringApplicationContex.getBean("sisuSariyaService");
		showServiceInfoDropdownMenu = false;
		showRouteInfoDropdownMenu = false;
	}

	public void onInfoPanelChange() {

		if (console != null) {
			if (console.equals("routeInfo")) {

				setShowServiceInfoDropdownMenu(false);
				setShowRouteInfoDropdownMenu(true);
				viaValueList = new ArrayList<SisuSeriyaDTO>(0);
				viaValueList = sisuSariyaService.getViaList();
			} else if (console.equals("serviceInfo")) {

				setShowServiceInfoDropdownMenu(true);
				setShowRouteInfoDropdownMenu(false);
				viaDataList = new ArrayList<SisuSeriyaDTO>(0);
				viaValueList = new ArrayList<SisuSeriyaDTO>(0);
				vehicleNoList = new ArrayList<SisuSeriyaDTO>(0);
				vehicleNoList = sisuSariyaService.getVehicleNoList();
				ownerNameList = new ArrayList<SisuSeriyaDTO>(0);
				ownerNameList = sisuSariyaService.getOwnerNameList();
			} else {
				setShowServiceInfoDropdownMenu(false);
				setShowRouteInfoDropdownMenu(false);
			}
		} else {

		}
	}

	public void onVehicleNoChange() {
		isSelectedVehicleNo = true;
		setDisabledServiceNoMenu(false);
		serviceNoList = new ArrayList<SisuSeriyaDTO>(0);
		serviceNoList = sisuSariyaService.getServiceNoListForSelectedBusNo(selectedVehicleNo);
	}

	public void onServiceNoChange() {

	}

	public void onOwnerNameChange() {
		isSelectedOwnerName = true;
		setDisabledServiceNoMenu(false);
		serviceNoList = new ArrayList<SisuSeriyaDTO>(0);
		serviceNoList = sisuSariyaService.getServiceNoListForSelectedOwnerName(selectedOwnerName);
	}

	public void searchServiceInfo() {
		if (isSelectedVehicleNo) {
			if (selectedVehicleNo != null) {
				setShowServiceInfoDetails(true);
				if (selectedServiceNo != null && !selectedServiceNo.isEmpty()
						&& !selectedServiceNo.equalsIgnoreCase("")) {
					serviceDataList = new ArrayList<SisuSeriyaDTO>(0);
					serviceDataList = sisuSariyaService.getServiceDataListWithServiceNo(selectedVehicleNo,
							selectedServiceNo);
				} else {
					serviceDataList = new ArrayList<SisuSeriyaDTO>(0);
					serviceDataList = sisuSariyaService.getServiceDataListWithBusNo(selectedVehicleNo);
				}
				if (serviceDataList.isEmpty()) {
					errorMsg = "No Data Found";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				} else {

				}
			} else {
				errorMsg = "Please select a  Vehicle No. or Owner Name";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				setShowServiceInfoDetails(false);
			}

		} else if (isSelectedOwnerName) {
			if (selectedOwnerName != null) {
				setShowServiceInfoDetails(true);
				if (selectedServiceNo != null && !selectedServiceNo.isEmpty()
						&& !selectedServiceNo.equalsIgnoreCase("")) {
					serviceDataList = new ArrayList<SisuSeriyaDTO>(0);
					serviceDataList = sisuSariyaService.getServiceDataListWithServiceNoAndOwnerName(selectedOwnerName,
							selectedServiceNo);
				} else {
					serviceDataList = new ArrayList<SisuSeriyaDTO>(0);
					serviceDataList = sisuSariyaService.getServiceDataListWithOwnerName(selectedOwnerName);
				}

				if (serviceDataList.isEmpty()) {
					errorMsg = "No Data Found";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				} else {

				}
			} else {
				errorMsg = "Please select a  Vehicle No. or Owner Name";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				setShowServiceInfoDetails(false);
			}

		} else {

		}
	}

	public void clearServiceInfo() {
		setDisabledServiceNoMenu(true);
		isSelectedOwnerName = false;
		isSelectedVehicleNo = false;
		selectedOwnerName = null;
		selectedServiceNo = null;
		selectedVehicleNo = null;
		setShowServiceInfoDetails(false);
		setShowRouteInfoDetails(false);
	}

	public void onViaChange() {

	}

	public void searchRouteInfo() {
		if (selectedViaValue != null && !selectedViaValue.isEmpty() && !selectedViaValue.equals("")) {
			setShowRouteInfoDetails(true);
			viaDataList = new ArrayList<SisuSeriyaDTO>(0);
			viaDataList = sisuSariyaService.getRouteDetailsForVia(selectedViaValue);
		} else {
			errorMsg = "Please select a Via";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			setShowRouteInfoDetails(false);
		}
	}

	public void clearRouteInfo() {
		setShowRouteInfoDetails(false);
		setShowServiceInfoDetails(false);
		viaDataList = new ArrayList<SisuSeriyaDTO>(0);
		setSelectedViaValue(null);
		viaDataList = new ArrayList<SisuSeriyaDTO>(0);
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public String getConsole() {
		return console;
	}

	public void setConsole(String console) {
		this.console = console;
	}

	public boolean isShowServiceInfoDropdownMenu() {
		return showServiceInfoDropdownMenu;
	}

	public void setShowServiceInfoDropdownMenu(boolean showServiceInfoDropdownMenu) {
		this.showServiceInfoDropdownMenu = showServiceInfoDropdownMenu;
	}

	public List<SisuSeriyaDTO> getVehicleNoList() {
		return vehicleNoList;
	}

	public void setVehicleNoList(List<SisuSeriyaDTO> vehicleNoList) {
		this.vehicleNoList = vehicleNoList;
	}

	public String getSelectedVehicleNo() {
		return selectedVehicleNo;
	}

	public void setSelectedVehicleNo(String selectedVehicleNo) {
		this.selectedVehicleNo = selectedVehicleNo;
	}

	public String getSelectedServiceNo() {
		return selectedServiceNo;
	}

	public void setSelectedServiceNo(String selectedServiceNo) {
		this.selectedServiceNo = selectedServiceNo;
	}

	public List<SisuSeriyaDTO> getServiceNoList() {
		return serviceNoList;
	}

	public void setServiceNoList(List<SisuSeriyaDTO> serviceNoList) {
		this.serviceNoList = serviceNoList;
	}

	public List<SisuSeriyaDTO> getViaValueList() {
		return viaValueList;
	}

	public void setViaValueList(List<SisuSeriyaDTO> viaValueList) {
		this.viaValueList = viaValueList;
	}

	public String getSelectedViaValue() {
		return selectedViaValue;
	}

	public void setSelectedViaValue(String selectedViaValue) {
		this.selectedViaValue = selectedViaValue;
	}

	public boolean isShowRouteInfoDropdownMenu() {
		return showRouteInfoDropdownMenu;
	}

	public void setShowRouteInfoDropdownMenu(boolean showRouteInfoDropdownMenu) {
		this.showRouteInfoDropdownMenu = showRouteInfoDropdownMenu;
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

	public List<SisuSeriyaDTO> getViaDataList() {
		return viaDataList;
	}

	public void setViaDataList(List<SisuSeriyaDTO> viaDataList) {
		this.viaDataList = viaDataList;
	}

	public boolean isShowRouteInfoDetails() {
		return showRouteInfoDetails;
	}

	public void setShowRouteInfoDetails(boolean showRouteInfoDetails) {
		this.showRouteInfoDetails = showRouteInfoDetails;
	}

	public List<SisuSeriyaDTO> getOwnerNameList() {
		return ownerNameList;
	}

	public void setOwnerNameList(List<SisuSeriyaDTO> ownerNameList) {
		this.ownerNameList = ownerNameList;
	}

	public String getSelectedOwnerName() {
		return selectedOwnerName;
	}

	public void setSelectedOwnerName(String selectedOwnerName) {
		this.selectedOwnerName = selectedOwnerName;
	}

	public boolean isDisabledServiceNoMenu() {
		return disabledServiceNoMenu;
	}

	public void setDisabledServiceNoMenu(boolean disabledServiceNoMenu) {
		this.disabledServiceNoMenu = disabledServiceNoMenu;
	}

	public boolean isShowServiceInfoDetails() {
		return showServiceInfoDetails;
	}

	public void setShowServiceInfoDetails(boolean showServiceInfoDetails) {
		this.showServiceInfoDetails = showServiceInfoDetails;
	}

	public List<SisuSeriyaDTO> getServiceDataList() {
		return serviceDataList;
	}

	public void setServiceDataList(List<SisuSeriyaDTO> serviceDataList) {
		this.serviceDataList = serviceDataList;
	}

}
