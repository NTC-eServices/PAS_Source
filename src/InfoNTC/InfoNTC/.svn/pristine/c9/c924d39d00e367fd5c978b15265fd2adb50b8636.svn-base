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

@ManagedBean(name = "routeSetUpForTimeTableBean")
@ViewScoped
public class RouteSetUpForTimeTableBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private AdminService adminService;
	private RouteSetUpForTimeTablePurposeService routeService;
	private List<RouteSetUpDTO> busRouteList, routeDetailsList, busRouteListEdit, routefordropdownEditList;
	private List<RouteSetUpDTO> modifiedDataList;
	private List<RouteSetUpDTO> modifiedDataListEdit;
	private RouteSetUpDTO routeSetUpDTO;
	private RouteSetUpDTO routeDetails;
	private String routeNo;
	private String permitAuthority;
	private String serviceType;
	private String groupNo;
	private boolean routeFlag, useForTimeTable, isSearch;
	private boolean disableSaveBtn;
	private String tripType;
	private String origin, destination, errorMessage, alertMSG, successMessage;

	private List<CommonDTO> drpdServiceTypeList;
	private List<RouteDTO> routefordropdownList;

	private boolean saveAgain;
	private boolean disableSaveButton;

	/* Remove Time Table - properties added by dhananjika.d 12/03/2024 */

	private List<String> refNoList;
	private TimeTableService timeTableService;
	private PanelGeneratorService panelGeneratorService;

	private String refNo;
	private String reason;

	@PostConstruct
	public void init() {
		routeService = (RouteSetUpForTimeTablePurposeService) SpringApplicationContex.getBean("routeService");
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");
		timeTableService = (TimeTableService) SpringApplicationContex.getBean("timeTableService");
		panelGeneratorService = (PanelGeneratorService) SpringApplicationContex.getBean("panelGeneratorService");
		loadValue();
	}

	public void loadValue() {
		routeSetUpDTO = new RouteSetUpDTO();
		busRouteList = new ArrayList<RouteSetUpDTO>();
		drpdServiceTypeList = adminService.getServiceTypeToDropdown();
		routefordropdownList = adminService.getRouteDetails();
		routefordropdownEditList = routeService.getRouteNoEdit();
		modifiedDataList = new ArrayList<RouteSetUpDTO>();
		modifiedDataListEdit = new ArrayList<RouteSetUpDTO>();

		disableSaveBtn = true;
		origin = null;
		destination = null;
		saveAgain = false;
		disableSaveButton = false;

		refNoList = panelGeneratorService.getRefNoListForDelete();
		reason = null;
	}

	public void searchAction() {
		if ((routeSetUpDTO.getRouteNo() == null || routeSetUpDTO.getRouteNo().trim().equalsIgnoreCase(""))
				|| (routeSetUpDTO.getServiceType() == null
						|| routeSetUpDTO.getServiceType().trim().equalsIgnoreCase(""))) {

			setErrorMessage("Please Select All Fields");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		} else {
			routeDetailsList = routeService.searchRouteDetails(routeSetUpDTO.getRouteNo(),
					routeSetUpDTO.getServiceType());

			int count = 1;
			for (RouteSetUpDTO data : routeDetailsList) {
				data.setRowNo(count);
				count++;
			}

			modifiedDataList.addAll(routeDetailsList);

			if (routeDetailsList.isEmpty()) {
				setErrorMessage("No Data Found.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			} else {
				isSearch = true;
				saveAgain = false;
				disableSaveButton = false;
			}
		}

	}

	public void saveAction() {
		if (routeSetUpDTO.getGroupNo().isEmpty()) {
			setErrorMessage("Please Select Group No");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		} else {
			if (!modifiedDataList.isEmpty()) {
				List<RouteSetUpDTO> swappedDataList = new ArrayList<>();

				for (RouteSetUpDTO route : modifiedDataList) {

					if (route.isRouteFlag()) {
						RouteSetUpDTO swappedRoute = new RouteSetUpDTO();
						swappedRoute.setRouteNo(route.getRouteNo());
						swappedRoute.setServiceType(routeSetUpDTO.getServiceType());
						swappedRoute.setPermitAuthority(route.getPermitAuthority());
						swappedRoute.setGroupNo(routeSetUpDTO.getGroupNo());
						swappedRoute.setPermitNo(route.getPermitNo());
						swappedRoute.setExpiryDate(route.getExpiryDate());
						swappedRoute.setBusNo(route.getBusNo());
						swappedRoute.setStatus(route.getStatus());
						swappedRoute.setOrigin(route.getDestination());
						swappedRoute.setDestination(route.getOrigin());
						swappedRoute.setSeq(route.getSeq());
						swappedRoute.setModifiedBy(sessionBackingBean.getLoginUser());
						swappedRoute.setRouteFlag(true);
						swappedRoute.setUseForTimeTable(true);

						swappedDataList.add(swappedRoute);
					} else {
						RouteSetUpDTO swappedRoute = new RouteSetUpDTO();
						swappedRoute.setRouteNo(route.getRouteNo());
						swappedRoute.setServiceType(routeSetUpDTO.getServiceType());
						swappedRoute.setPermitAuthority(route.getPermitAuthority());
						swappedRoute.setGroupNo(routeSetUpDTO.getGroupNo());
						swappedRoute.setPermitNo(route.getPermitNo());
						swappedRoute.setExpiryDate(route.getExpiryDate());
						swappedRoute.setBusNo(route.getBusNo());
						swappedRoute.setStatus(route.getStatus());
						swappedRoute.setOrigin(route.getOrigin());
						swappedRoute.setDestination(route.getDestination());
						swappedRoute.setSeq(route.getSeq());
						swappedRoute.setModifiedBy(sessionBackingBean.getLoginUser());
						swappedRoute.setRouteFlag(false);
						swappedRoute.setUseForTimeTable(true);

						swappedDataList.add(swappedRoute);
					}
				}
				modifiedDataList = swappedDataList;

				boolean notHaveData = routeService.searchHistory(routeSetUpDTO.getRouteNo(),
						routeSetUpDTO.getServiceType(), routeSetUpDTO.getGroupNo());

				if (notHaveData) {
					routeService.setTimeTableData(modifiedDataList);

					routeSetUpDTO.setServiceType(null);
					routeSetUpDTO.setRouteNo(null);
					routeSetUpDTO.setGroupNo(null);

					setSuccessMessage("Saved Successfull");
					RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
				} else {
					RequestContext.getCurrentInstance().execute("PF('comfirmMSG').show()");
				}

				disableSaveButton = true;

			} else {
				setErrorMessage("Please select Row");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

			routeDetailsList = new ArrayList<>();
		}

	}

	public void inactiveData() {
		saveAgain = false;
		boolean success = routeService.UpdateStatus(routeSetUpDTO.getRouteNo(), routeSetUpDTO.getServiceType(),
				routeSetUpDTO.getGroupNo());

		if (success) {
			routeService.setTimeTableData(modifiedDataList);

			routeSetUpDTO = new RouteSetUpDTO();

			RequestContext.getCurrentInstance().execute("PF('comfirmMSG').hide()");

			setSuccessMessage("Status updated and new data saved successfully");
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
		} else {
			setErrorMessage("Data not saved");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

//Edit/View RouteSetUp for Time Table
	public void searchActionEdit() {
		if ((routeSetUpDTO.getRouteNo() == null || routeSetUpDTO.getRouteNo().trim().equalsIgnoreCase(""))
				|| (routeSetUpDTO.getServiceType() == null
						|| routeSetUpDTO.getServiceType().trim().equalsIgnoreCase(""))
				|| (routeSetUpDTO.getGroupNo() == null || routeSetUpDTO.getGroupNo().trim().equalsIgnoreCase(""))) {

			setErrorMessage("Please Select All Field");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		} else {
			busRouteListEdit = routeService.searchRouteDetailsEditView(routeSetUpDTO.getRouteNo(),
					routeSetUpDTO.getServiceType(), routeSetUpDTO.getGroupNo());

			int count = 1;
			for (RouteSetUpDTO route : busRouteListEdit) {
				if (!route.isRouteFlag()) {
					origin = route.getOrigin();
					destination = route.getDestination();
				} else {
					origin = route.getDestination();
					destination = route.getOrigin();
				}

				route.setRowNo(count);
				count++;
			}

			List<RouteSetUpDTO> busRouteListEditChange = new ArrayList<>(busRouteListEdit);

			for (RouteSetUpDTO data : busRouteListEdit) {
				if (!data.isUseForTimeTable()) {
					busRouteListEditChange.remove(data);
					modifiedDataListEdit = busRouteListEditChange;
				} else {
					modifiedDataListEdit = busRouteListEditChange;
				}
			}

			if (busRouteListEdit.isEmpty()) {
				setErrorMessage("No Data Found.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			} else {
				isSearch = true;
				disableSaveBtn = false;
			}
		}

	}

	public void saveEditData() {
		if (!modifiedDataListEdit.isEmpty()) {
			List<RouteSetUpDTO> swappedDataList = new ArrayList<>();

			int busRouteListEditSize = busRouteListEdit.size();
			int modifiedDataListEditSize = modifiedDataListEdit.size();

			if (busRouteListEditSize != modifiedDataListEditSize) {
				List<RouteSetUpDTO> busRouteListEditChange = new ArrayList<>(busRouteListEdit);
				busRouteListEditChange.removeAll(modifiedDataListEdit);

				for (RouteSetUpDTO data : busRouteListEditChange) {
					data.setServiceType(routeSetUpDTO.getServiceType());
					data.setUseForTimeTable(false);
					data.setGroupNo(routeSetUpDTO.getGroupNo());
					data.setModifiedBy(sessionBackingBean.getLoginUser());
				}

				routeService.editedTimeTableData(busRouteListEditChange);
			}

			for (RouteSetUpDTO route : modifiedDataListEdit) {
				if (route.isRouteFlag()) {
					RouteSetUpDTO swappedRoute = new RouteSetUpDTO();
					swappedRoute.setRouteNo(route.getRouteNo());
					swappedRoute.setServiceType(routeSetUpDTO.getServiceType());
					swappedRoute.setPermitAuthority(route.getPermitAuthority());
					swappedRoute.setGroupNo(routeSetUpDTO.getGroupNo());
					swappedRoute.setPermitNo(route.getPermitNo());
					swappedRoute.setExpiryDate(route.getExpiryDate());
					swappedRoute.setBusNo(route.getBusNo());
					swappedRoute.setStatus(route.getStatus());
					swappedRoute.setOrigin(route.getDestination());
					swappedRoute.setDestination(route.getOrigin());
					swappedRoute.setSeq(route.getSeq());
					swappedRoute.setModifiedBy(sessionBackingBean.getLoginUser());
					swappedRoute.setRouteFlag(true);
					swappedRoute.setUseForTimeTable(true);

					swappedDataList.add(swappedRoute);
				} else {
					RouteSetUpDTO swappedRoute = new RouteSetUpDTO();
					swappedRoute.setRouteNo(route.getRouteNo());
					swappedRoute.setServiceType(routeSetUpDTO.getServiceType());
					swappedRoute.setPermitAuthority(route.getPermitAuthority());
					swappedRoute.setGroupNo(routeSetUpDTO.getGroupNo());
					swappedRoute.setPermitNo(route.getPermitNo());
					swappedRoute.setExpiryDate(route.getExpiryDate());
					swappedRoute.setBusNo(route.getBusNo());
					swappedRoute.setStatus(route.getStatus());
					swappedRoute.setOrigin(origin);
					swappedRoute.setDestination(destination);
					swappedRoute.setSeq(route.getSeq());
					swappedRoute.setModifiedBy(sessionBackingBean.getLoginUser());
					swappedRoute.setRouteFlag(false);
					swappedRoute.setUseForTimeTable(true);

					swappedDataList.add(swappedRoute);
				}
			}
			modifiedDataListEdit = swappedDataList;

			routeService.editedTimeTableData(modifiedDataListEdit);

			routeSetUpDTO.setServiceType(null);
			routeSetUpDTO.setRouteNo(null);
			routeSetUpDTO.setGroupNo(null);
			disableSaveBtn = true;

			setSuccessMessage("Saved Successfull");
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

		} else {
			setErrorMessage("Please select Row");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

		busRouteListEdit = new ArrayList<>();
	}

	public void clearOne() {
		routeDetailsList = new ArrayList<>();
		busRouteListEdit = new ArrayList<>();
		routeSetUpDTO.setPermitAuthority(null); // or set to default value
		routeNo = null;
		serviceType = null;
		permitAuthority = null;
		routeSetUpDTO.setServiceType(null);
		routeSetUpDTO.setRouteNo(null);
		routeSetUpDTO.setGroupNo(null);

		disableSaveBtn = true;
		disableSaveButton = false;
	}

	/* Remove Time Table - methods added by dhananjika.d 12/03/2024 */

	public void searchDetails() {
		if (refNo == null || refNo.trim().equalsIgnoreCase("")) {
			setErrorMessage("Please Select Reference No");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		} else {
			routeSetUpDTO = new RouteSetUpDTO();
			busRouteList = new ArrayList<RouteSetUpDTO>();
			busRouteList = routeService.getRouteDetails(refNo);
		}
	}

	public void clearSearchedDetails() {
		refNo = null;
		reason = null;
		busRouteList = new ArrayList<RouteSetUpDTO>();
		routeSetUpDTO = new RouteSetUpDTO();
	}

	public void removeAction() {
		if (routeSetUpDTO != null) {
			if (reason == null || reason.trim().equalsIgnoreCase("")) {
				setErrorMessage("Please fill in the reason.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			} else {
				boolean success = routeService.removeTimeTableData(refNo);
				boolean delete = routeService.removeTempTable(routeSetUpDTO);
				boolean insertData = routeService.saveDeletedDetails(routeSetUpDTO, reason,
						sessionBackingBean.getLoginUser(), refNo);

				if (success && insertData && delete) {
					clearSearchedDetails();
					refNoList = panelGeneratorService.getRefNoListForDelete();
					
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

	public List<RouteSetUpDTO> getBusRouteList() {
		return busRouteList;
	}

	public List<RouteSetUpDTO> getRouteDetailsList() {
		return routeDetailsList;
	}

	public void setRouteDetailsList(List<RouteSetUpDTO> routeDetailsList) {
		this.routeDetailsList = routeDetailsList;
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

	public RouteSetUpForTimeTablePurposeService getRouteService() {
		return routeService;
	}

	public void setRouteService(RouteSetUpForTimeTablePurposeService routeService) {
		this.routeService = routeService;
	}

	public String getRouteNo() {
		return routeNo;
	}

	public void setRouteNo(String routeNo) {
		this.routeNo = routeNo;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getPermitAuthority() {
		return permitAuthority;
	}

	public void setPermitAuthority(String permitAuthority) {
		this.permitAuthority = permitAuthority;
	}

	public String getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}

	public boolean isRouteFlag() {
		return routeFlag;
	}

	public void setRouteFlag(boolean routeFlag) {
		this.routeFlag = routeFlag;
	}

	public String getTripType() {
		return tripType;
	}

	public void setTripType(String tripType) {
		this.tripType = tripType;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public boolean isUseForTimeTable() {
		return useForTimeTable;
	}

	public void setUseForTimeTable(boolean useForTimeTable) {
		this.useForTimeTable = useForTimeTable;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public List<RouteSetUpDTO> getModifiedDataList() {
		return modifiedDataList;
	}

	public void setModifiedDataList(List<RouteSetUpDTO> modifiedDataList) {
		this.modifiedDataList = modifiedDataList;
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

	public boolean isSearch() {
		return isSearch;
	}

	public void setSearch(boolean isSearch) {
		this.isSearch = isSearch;
	}

	public List<RouteSetUpDTO> getBusRouteListEdit() {
		return busRouteListEdit;
	}

	public void setBusRouteListEdit(List<RouteSetUpDTO> busRouteListEdit) {
		this.busRouteListEdit = busRouteListEdit;
	}

	public List<CommonDTO> getDrpdServiceTypeList() {
		return drpdServiceTypeList;
	}

	public void setDrpdServiceTypeList(List<CommonDTO> drpdServiceTypeList) {
		this.drpdServiceTypeList = drpdServiceTypeList;
	}

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	public List<RouteDTO> getRoutefordropdownList() {
		return routefordropdownList;
	}

	public void setRoutefordropdownList(List<RouteDTO> routefordropdownList) {
		this.routefordropdownList = routefordropdownList;
	}

	public boolean isDisableSaveBtn() {
		return disableSaveBtn;
	}

	public void setDisableSaveBtn(boolean disableSaveBtn) {
		this.disableSaveBtn = disableSaveBtn;
	}

	public List<RouteSetUpDTO> getModifiedDataListEdit() {
		return modifiedDataListEdit;
	}

	public void setModifiedDataListEdit(List<RouteSetUpDTO> modifiedDataListEdit) {
		this.modifiedDataListEdit = modifiedDataListEdit;
	}

	public List<RouteSetUpDTO> getRoutefordropdownEditList() {
		return routefordropdownEditList;
	}

	public void setRoutefordropdownEditList(List<RouteSetUpDTO> routefordropdownEditList) {
		this.routefordropdownEditList = routefordropdownEditList;
	}

	public boolean isSaveAgain() {
		return saveAgain;
	}

	public void setSaveAgain(boolean saveAgain) {
		this.saveAgain = saveAgain;
	}

	public boolean isDisableSaveButton() {
		return disableSaveButton;
	}

	public void setDisableSaveButton(boolean disableSaveButton) {
		this.disableSaveButton = disableSaveButton;
	}

	public List<String> getRefNoList() {
		return refNoList;
	}

	public void setRefNoList(List<String> refNoList) {
		this.refNoList = refNoList;
	}

	public TimeTableService getTimeTableService() {
		return timeTableService;
	}

	public void setTimeTableService(TimeTableService timeTableService) {
		this.timeTableService = timeTableService;
	}

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public RouteSetUpDTO getRouteDetails() {
		return routeDetails;
	}

	public void setRouteDetails(RouteSetUpDTO routeDetails) {
		this.routeDetails = routeDetails;
	}

	public PanelGeneratorService getPanelGeneratorService() {
		return panelGeneratorService;
	}

	public void setPanelGeneratorService(PanelGeneratorService panelGeneratorService) {
		this.panelGeneratorService = panelGeneratorService;
	}

}
