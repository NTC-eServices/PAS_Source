package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.RouteDTO;
import lk.informatics.ntc.model.dto.RouteSetUpDTO;
import lk.informatics.ntc.model.service.AdminService;
import lk.informatics.ntc.model.service.PanelGeneratorNewService;
import lk.informatics.ntc.model.service.PanelGeneratorService;
import lk.informatics.ntc.model.service.RouteSetUpForTimeTablePurposeService;
import lk.informatics.ntc.model.service.TimeTableService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "removeNFCPrintDetailsBean")
@ViewScoped
public class RemoveNFCCardPrintDetailsBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	
	private String selectedVehicleNo;
	private String selectedPermitNo;
	private String errorMessage, alertMSG, successMessage;
	private List<String> vehicleNoList;
	private List<String> permitNoList;
	private List<RouteSetUpDTO> busRouteList;
	private List<RouteSetUpDTO> selectedBusRouteList;
	private List<RouteSetUpDTO> historyList;
	private RouteSetUpDTO routeSetUpDTO;
	private String reason;
	private boolean renderData;
	
	private RouteSetUpForTimeTablePurposeService routeService;
	

	@PostConstruct
	public void init() {
		routeService = (RouteSetUpForTimeTablePurposeService) SpringApplicationContex.getBean("routeService");
		
		loadValue();
	}

	public void loadValue() {
		vehicleNoList = routeService.getBusNoList();
		permitNoList = routeService.getPermitNoList();
		
		routeSetUpDTO = new RouteSetUpDTO();
		busRouteList = new ArrayList<RouteSetUpDTO>();
		selectedBusRouteList = new ArrayList<RouteSetUpDTO>();
		historyList = new ArrayList<RouteSetUpDTO>();
		reason = null;
		renderData = false;
	}
	
	public void searchDetails() {
		if((selectedVehicleNo == null || selectedVehicleNo.trim().equalsIgnoreCase("")) 
				&& (selectedPermitNo == null || selectedPermitNo.trim().equalsIgnoreCase(""))) {
			setErrorMessage("Please select at least one field");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}else {
			busRouteList = new ArrayList<RouteSetUpDTO>();
			selectedBusRouteList = new ArrayList<RouteSetUpDTO>();
			
			busRouteList = routeService.getNFCDetails(selectedPermitNo, selectedVehicleNo);
			renderData = true;
			
			if(busRouteList.isEmpty()) {
				setErrorMessage("No data found");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		}
	}

	public void clearSearchedDetails() {
		vehicleNoList = routeService.getBusNoList();
		permitNoList = routeService.getPermitNoList();
		
		busRouteList = new ArrayList<RouteSetUpDTO>();
		selectedBusRouteList = new ArrayList<RouteSetUpDTO>();
		reason = null;
		selectedVehicleNo = null;
		selectedPermitNo = null;
		renderData = false;
	}
	
	
	public void removeAction() {
		if (selectedBusRouteList != null || !selectedBusRouteList.isEmpty()) {
			if (reason == null || reason.trim().equalsIgnoreCase("")) {
				setErrorMessage("Please fill in the reason.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			} else {
				boolean success = routeService.removeNFCData(selectedBusRouteList);
				boolean insertData = routeService.saveNFCDeletedDetails(selectedBusRouteList, reason, sessionBackingBean.getLoginUser());

				if (success && insertData) {
					clearSearchedDetails();
					renderData = true;
					setSuccessMessage("Data deleted successfully");
					RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
				} else {
					setErrorMessage("Failed to delete data");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			}
		} else {
			setErrorMessage("Please select row.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}
	
	
	public void getHistory() {
		historyList = new ArrayList<RouteSetUpDTO>();
		historyList = routeService.getDeletedNFCDetails();
		
		RequestContext.getCurrentInstance().execute("PF('history').show()");
	}
	
	public String getSelectedVehicleNo() {
		return selectedVehicleNo;
	}

	public void setSelectedVehicleNo(String selectedVehicleNo) {
		this.selectedVehicleNo = selectedVehicleNo;
	}

	public String getSelectedPermitNo() {
		return selectedPermitNo;
	}

	public void setSelectedPermitNo(String selectedPermitNo) {
		this.selectedPermitNo = selectedPermitNo;
	}

	public List<String> getVehicleNoList() {
		return vehicleNoList;
	}

	public void setVehicleNoList(List<String> vehicleNoList) {
		this.vehicleNoList = vehicleNoList;
	}

	public List<String> getPermitNoList() {
		return permitNoList;
	}

	public void setPermitNoList(List<String> permitNoList) {
		this.permitNoList = permitNoList;
	}

	public RouteSetUpForTimeTablePurposeService getRouteService() {
		return routeService;
	}

	public void setRouteService(RouteSetUpForTimeTablePurposeService routeService) {
		this.routeService = routeService;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getAlertMSG() {
		return alertMSG;
	}

	public void setAlertMSG(String alertMSG) {
		this.alertMSG = alertMSG;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	public List<RouteSetUpDTO> getBusRouteList() {
		return busRouteList;
	}

	public void setBusRouteList(List<RouteSetUpDTO> busRouteList) {
		this.busRouteList = busRouteList;
	}

	public RouteSetUpDTO getRouteSetUpDTO() {
		return routeSetUpDTO;
	}

	public void setRouteSetUpDTO(RouteSetUpDTO routeSetUpDTO) {
		this.routeSetUpDTO = routeSetUpDTO;
	}

	public List<RouteSetUpDTO> getSelectedBusRouteList() {
		return selectedBusRouteList;
	}

	public void setSelectedBusRouteList(List<RouteSetUpDTO> selectedBusRouteList) {
		this.selectedBusRouteList = selectedBusRouteList;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public List<RouteSetUpDTO> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<RouteSetUpDTO> historyList) {
		this.historyList = historyList;
	}

	public boolean isRenderData() {
		return renderData;
	}

	public void setRenderData(boolean renderData) {
		this.renderData = renderData;
	}
	

}
