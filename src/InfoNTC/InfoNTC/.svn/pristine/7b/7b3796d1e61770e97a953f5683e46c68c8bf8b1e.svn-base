package lk.informatics.ntc.view.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.RouteCreationDTO;
import lk.informatics.ntc.model.dto.StationDetailsDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.RouteCreatorService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "assignStationToRouteCreateBackingBean")
@ViewScoped
public class RouteStationCreateBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	// services
	private RouteCreatorService routeCreatorService;
	private CommonService commonService;

	// DTO

	private RouteCreationDTO stationToRoute;
	private StationDetailsDTO stationToRouteSecondDTO;
	private RouteCreationDTO selectedDTO;
	private StationDetailsDTO removeSelectedDTO;
	private StationDetailsDTO fillStationsInSinTam;
	private StationDetailsDTO fillStationsInSinTamSecond;
	private StationDetailsDTO selectedRow;

	// ArrayLists

	private List<RouteCreationDTO> routeNOList = new ArrayList<RouteCreationDTO>(0);
	private List<StationDetailsDTO> stationList = new ArrayList<StationDetailsDTO>();
	private List<RouteCreationDTO> showRouteDetails = new ArrayList<RouteCreationDTO>();
	private List<StationDetailsDTO> serviceTypeList = new ArrayList<StationDetailsDTO>();
	private List<StationDetailsDTO> showGridDataDTO = new ArrayList<StationDetailsDTO>();

	private boolean stationDisa, stationDisa1;
	private boolean serviceTypeDisa, editFlag;
	private String successMessage, errorMessage, selectedServiceType, selectedStatus, loginUser, selectedstationCode,
			selectedStationSin, selectedStationTam, selectedStage;
	private List<RouteCreationDTO> routeNamesList;
	private List<String> stagesList;
	private String selectedRoute;
	private String selectedStageDlg;// start
	private String selectedStatusDlg;// end

	private List<RouteCreationDTO> routeList;
	private boolean copyRouteBtn, editButtonPermission;
	private String origin;
	private String destination;
	private StationDetailsDTO routeDetailsDTO;

	@PostConstruct
	public void init() {
		routeCreatorService = (RouteCreatorService) SpringApplicationContex.getBean("routeCreatorService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN205", "C");
		editButtonPermission = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN205", "E");
		stationToRoute = new RouteCreationDTO();
		stationToRouteSecondDTO = new StationDetailsDTO();
		routeNOList = routeCreatorService.getAllRoutes();
		stationList = routeCreatorService.getAllStations();
		serviceTypeList = routeCreatorService.getAllServiceType();
		stationDisa = true;
		stationDisa1 = true;
		serviceTypeDisa = true;
		loginUser = sessionBackingBean.getLoginUser();
		routeNamesList = new ArrayList<RouteCreationDTO>();
		selectedRoute = null;
		stagesList = new ArrayList<String>();
		routeList = new ArrayList<RouteCreationDTO>();
		selectedStage = null;
		selectedStatus = null;
		copyRouteBtn = false;
		origin = null;
		destination = null;
		routeDetailsDTO = new StationDetailsDTO();

	}

	public void fillRouteDet() {

		showRouteDetails = routeCreatorService.getRouteDetails(stationToRoute);
		String routeNo = stationToRoute.getRouteNo();
		showGridDataDTO = routeCreatorService.showDataonGrid(routeNo);
		routeDetailsDTO = routeCreatorService.retrieveRouteDataForRouteNum(routeNo);
		stationDisa = false;

		if (routeDetailsDTO != null) {
			origin = routeDetailsDTO.getOrigin();
			destination = routeDetailsDTO.getDestination();
		}

		stationToRouteSecondDTO.setServiceTypeCode(" ");
	}

	public void fillStationSinAndTamil() {
		String routeCode;
		routeCode = stationToRouteSecondDTO.getStationCode();
		fillStationsInSinTam = routeCreatorService.getStationsInSinAndTamil(routeCode);

		stationToRouteSecondDTO.setStationNameSin(fillStationsInSinTam.getStationNameSin());
		stationToRouteSecondDTO.setStationNameTam(fillStationsInSinTam.getStationNameTam());

	}

	public void fillStationsSecond() {

		String routeCode1;
		routeCode1 = stationToRouteSecondDTO.getStationCode();
		fillStationsInSinTamSecond = routeCreatorService.getStaInSinAndTamilSec(selectedstationCode);

		selectedStationSin = fillStationsInSinTamSecond.getStationNameSin();
		selectedStationTam = fillStationsInSinTamSecond.getStationNameTam();

	}

	public void addDetails() {
		if (stationToRouteSecondDTO.getStationCode() != null
				&& !stationToRouteSecondDTO.getStationCode().trim().isEmpty()) {
			if (stationToRouteSecondDTO.getServiceTypeCode() != null
					&& !stationToRouteSecondDTO.getServiceTypeCode().trim().isEmpty()) {

				String routeNoEnterd = stationToRoute.getRouteNo();
				String status = stationToRoute.getStatusRouteToStation();

				boolean checkStation;
				checkStation = routeCreatorService.checkDuplicateStation(routeNoEnterd,
						stationToRouteSecondDTO.getStationCode());
				if (checkStation != true) {
					routeCreatorService.addAssignStationToRoute(stationToRouteSecondDTO, routeNoEnterd, loginUser,
							status);
					setSuccessMessage("Succesfully Added.");
					RequestContext.getCurrentInstance().update("successMSG");
					RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
					stationToRouteSecondDTO.setStationCode("");

					stationToRouteSecondDTO.setStage("");
					String routeNo = stationToRoute.getRouteNo();
					showGridDataDTO = routeCreatorService.showDataonGrid(routeNo);

				} else {

					setErrorMessage("This Station already added.");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else {
				setErrorMessage("Please select a service type");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

			}

		} else {
			setErrorMessage("Please select a station");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		}

	}

	public void editButton() {

		RequestContext.getCurrentInstance().execute("PF('routeStation').show()");
		stationToRouteSecondDTO.setStage(selectedRow.getStage());
		stationToRouteSecondDTO.setStationCode(selectedRow.getStationCode());
		fillStationSinAndTamil();
		stationToRouteSecondDTO.setServiceTypeDes(selectedRow.getServiceTypeDes());
		stationToRouteSecondDTO.setServiceTypeCode(selectedRow.getServiceTypeCode());

		editFlag = true;

	}

	public void removeVal() {
		routeCreatorService.updateStageSeq(stationToRoute.getRouteNo(), selectedRow.getStage(),
				selectedRow.getStationCode(), stationToRouteSecondDTO, loginUser);

		setSuccessMessage("Succesfully Deleted.");
		RequestContext.getCurrentInstance().update("successMSG");
		RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

		showGridDataDTO = routeCreatorService.showDataonGrid(stationToRoute.getRouteNo());
	}

	public void addChangeDetails() {
		if ((selectedstationCode != null && !selectedstationCode.trim().isEmpty())
				|| (selectedServiceType != null && !selectedServiceType.trim().isEmpty())
				|| (selectedStage != null && !selectedStage.trim().isEmpty())) {
			String routeNo1 = stationToRoute.getRouteNo();
			String stageCost = stationToRouteSecondDTO.getStage();
			boolean checkStation;
			checkStation = routeCreatorService.checkDuplicateStation(routeNo1, selectedstationCode);
			if (checkStation != true) {
				routeCreatorService.updatedEditedData(selectedstationCode, stageCost, loginUser, selectedServiceType,
						selectedStatus, routeNo1, selectedStage, stationToRouteSecondDTO);
				setSuccessMessage("Succesfully updated.");
				RequestContext.getCurrentInstance().update("successMSG");
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
				String routeNo = stationToRoute.getRouteNo();
				showGridDataDTO = routeCreatorService.showDataonGrid(routeNo);
				if (selectedstationCode != null && !selectedstationCode.trim().isEmpty()) {
					stationToRouteSecondDTO.setStationCode(selectedstationCode);
					fillStationSinAndTamil();
				}

				if (selectedServiceType != null && !selectedServiceType.trim().isEmpty()) {
					stationToRouteSecondDTO.setServiceTypeCode(selectedServiceType);
					stationToRouteSecondDTO.setServiceTypeDes(selectedServiceType);
				}
				if (selectedStage != null && !selectedStage.trim().isEmpty()) {
					stationToRouteSecondDTO.setStage(selectedStage);

				}
				selectedstationCode = "";
				selectedServiceType = "";
				selectedStage = "";

			}

			else {

				setErrorMessage("This Station already added.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		}

		else {
			setErrorMessage("Please enter data for change");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		}

	}

	public void clearDet() {
		stationToRoute = new RouteCreationDTO();
		stationToRouteSecondDTO = new StationDetailsDTO();
		showRouteDetails = new ArrayList<RouteCreationDTO>();

		showGridDataDTO = new ArrayList<StationDetailsDTO>();
		copyRouteBtn = false;
		origin = null;
		destination = null;
	}

	public void copyRouteLocationAction() {
		copyRouteBtn = false;
		if (stationToRoute.getRouteNo() != null && !stationToRoute.getRouteNo().isEmpty()
				&& !stationToRoute.getRouteNo().trim().equalsIgnoreCase("")) {
			routeNamesList = routeCreatorService.getCreatedRouteData();
			routeList = new ArrayList<RouteCreationDTO>();
			selectedStageDlg = null;
			selectedStatusDlg = null;
			selectedRoute = null;
			RequestContext.getCurrentInstance().update("copyRouteDets");
			RequestContext.getCurrentInstance().execute("PF('copyRouteDets').show()");

		} else {

			sessionBackingBean.setMessage("Please select Route No.");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonWarning').show()");
			return;
		}

	}

	public void onSelectRouteName() {
		stagesList = routeCreatorService.getStagesList(selectedRoute);
		RequestContext.getCurrentInstance().update("copyRouteDets:frmcopyRoute:stageId");
		RequestContext.getCurrentInstance().update("copyRouteDets:frmcopyRoute:statusId");

	}

	public void copyRouteAddAction() {
		if (selectedRoute != null && !selectedRoute.isEmpty() && !selectedRoute.trim().equalsIgnoreCase("")) {
			if (selectedStageDlg != null && !selectedStageDlg.isEmpty()
					&& !selectedStageDlg.trim().equalsIgnoreCase("")) {
				if (selectedStatusDlg != null && !selectedStatusDlg.isEmpty()
						&& !selectedStatusDlg.trim().equalsIgnoreCase("")) {

					routeList = new ArrayList<RouteCreationDTO>();
					routeList = routeCreatorService.getLocationList(selectedRoute, selectedStageDlg, selectedStatusDlg);

					copyRouteBtn = true;

					RequestContext.getCurrentInstance().update("copyRouteDets:frmcopyRoute");

				} else {
					sessionBackingBean.setMessage("Please select Status (End)");
					RequestContext.getCurrentInstance().execute("PF('dlgCommonWarning').show()");
					return;
				}
			} else {
				sessionBackingBean.setMessage("Please select Stage (Start)");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonWarning').show()");
				return;
			}
		} else {
			sessionBackingBean.setMessage("Please select Route Name");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonWarning').show()");
			return;
		}
	}

	public void copyRouteDetailsAction() {
		String stageCount = "0";
		int stage = 0;
		boolean listExist = false;
		if (showGridDataDTO != null && showGridDataDTO.size() != 0) {
			stageCount = showGridDataDTO.get(showGridDataDTO.size() - 1).getStage();
			stage = Integer.parseInt(stageCount);
			listExist = true;
		}

		for (RouteCreationDTO dto : routeList) {
			StationDetailsDTO staionDto = new StationDetailsDTO();

			staionDto.setStationCode(dto.getStation());
			staionDto.setStationNameEn(dto.getStationDetailsDTO().getStationNameEn());
			staionDto.setStationNameSin(dto.getStationDetailsDTO().getStationNameSin());
			staionDto.setStationNameTam(dto.getStationDetailsDTO().getStationNameTam());

			if (listExist) {
				stage += 1;
				int tempStage = stage;
				staionDto.setStage(Integer.toString(tempStage));
			} else {
				staionDto.setStage(dto.getStage());
			}

			staionDto.setServiceTypeCode("N");

			routeCreatorService.addAssignStationToRoute(staionDto, stationToRoute.getRouteNo(), loginUser, null);
			showGridDataDTO.add(staionDto);// showGridDataDTO -> List
		}
		RequestContext.getCurrentInstance().update("form");
		RequestContext.getCurrentInstance().execute("PF('copyRouteDets').hide()");

		sessionBackingBean.setMessage("Routes were copied successfully");
		RequestContext.getCurrentInstance().execute("PF('dlgCommonSuccess').show()");
	}

	public void clearDetPop() {
		selectedstationCode = null;
		selectedStage = null;
		origin = null;
		destination = null;

	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public RouteCreatorService getRouteCreatorService() {
		return routeCreatorService;
	}

	public void setRouteCreatorService(RouteCreatorService routeCreatorService) {
		this.routeCreatorService = routeCreatorService;
	}

	public StationDetailsDTO getStationToRouteSecondDTO() {
		return stationToRouteSecondDTO;
	}

	public void setStationToRouteSecondDTO(StationDetailsDTO stationToRouteSecondDTO) {
		this.stationToRouteSecondDTO = stationToRouteSecondDTO;
	}

	public List<StationDetailsDTO> getStationList() {
		return stationList;
	}

	public void setStationList(List<StationDetailsDTO> stationList) {
		this.stationList = stationList;
	}

	public RouteCreationDTO getStationToRoute() {
		return stationToRoute;
	}

	public void setStationToRoute(RouteCreationDTO stationToRoute) {
		this.stationToRoute = stationToRoute;
	}

	public List<RouteCreationDTO> getRouteNOList() {
		return routeNOList;
	}

	public void setRouteNOList(List<RouteCreationDTO> routeNOList) {
		this.routeNOList = routeNOList;
	}

	public RouteCreationDTO getSelectedDTO() {
		return selectedDTO;
	}

	public void setSelectedDTO(RouteCreationDTO selectedDTO) {
		this.selectedDTO = selectedDTO;
	}

	public List<RouteCreationDTO> getShowRouteDetails() {
		return showRouteDetails;
	}

	public void setShowRouteDetails(List<RouteCreationDTO> showRouteDetails) {
		this.showRouteDetails = showRouteDetails;
	}

	public boolean isStationDisa() {
		return stationDisa;
	}

	public void setStationDisa(boolean stationDisa) {
		this.stationDisa = stationDisa;
	}

	public StationDetailsDTO getFillStationsInSinTam() {
		return fillStationsInSinTam;
	}

	public void setFillStationsInSinTam(StationDetailsDTO fillStationsInSinTam) {
		this.fillStationsInSinTam = fillStationsInSinTam;
	}

	public List<StationDetailsDTO> getServiceTypeList() {
		return serviceTypeList;
	}

	public void setServiceTypeList(List<StationDetailsDTO> serviceTypeList) {
		this.serviceTypeList = serviceTypeList;
	}

	public boolean isServiceTypeDisa() {
		return serviceTypeDisa;
	}

	public void setServiceTypeDisa(boolean serviceTypeDisa) {
		this.serviceTypeDisa = serviceTypeDisa;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(String loginUser) {
		this.loginUser = loginUser;
	}

	public StationDetailsDTO getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(StationDetailsDTO selectedRow) {
		this.selectedRow = selectedRow;
	}

	public List<StationDetailsDTO> getShowGridDataDTO() {
		return showGridDataDTO;
	}

	public void setShowGridDataDTO(List<StationDetailsDTO> showGridDataDTO) {
		this.showGridDataDTO = showGridDataDTO;
	}

	public List<RouteCreationDTO> getRouteNamesList() {
		return routeNamesList;
	}

	public void setRouteNamesList(List<RouteCreationDTO> routeNamesList) {
		this.routeNamesList = routeNamesList;
	}

	public String getSelectedRoute() {
		return selectedRoute;
	}

	public void setSelectedRoute(String selectedRoute) {
		this.selectedRoute = selectedRoute;
	}

	public List<String> getStagesList() {
		return stagesList;
	}

	public void setStagesList(List<String> stagesList) {
		this.stagesList = stagesList;
	}

	public String getSelectedStage() {
		return selectedStage;
	}

	public void setSelectedStage(String selectedStage) {
		this.selectedStage = selectedStage;
	}

	public String getSelectedStatus() {
		return selectedStatus;
	}

	public void setSelectedStatus(String selectedStatus) {
		this.selectedStatus = selectedStatus;
	}

	public List<RouteCreationDTO> getRouteList() {
		return routeList;
	}

	public void setRouteList(List<RouteCreationDTO> routeList) {
		this.routeList = routeList;
	}

	public boolean isCopyRouteBtn() {
		return copyRouteBtn;
	}

	public void setCopyRouteBtn(boolean copyRouteBtn) {
		this.copyRouteBtn = copyRouteBtn;
	}

	public boolean isEditFlag() {
		return editFlag;
	}

	public void setEditFlag(boolean editFlag) {
		this.editFlag = editFlag;
	}

	public String getSelectedstationCode() {
		return selectedstationCode;
	}

	public void setSelectedstationCode(String selectedstationCode) {
		this.selectedstationCode = selectedstationCode;
	}

	public String getSelectedStationSin() {
		return selectedStationSin;
	}

	public void setSelectedStationSin(String selectedStationSin) {
		this.selectedStationSin = selectedStationSin;
	}

	public String getSelectedStationTam() {
		return selectedStationTam;
	}

	public void setSelectedStationTam(String selectedStationTam) {
		this.selectedStationTam = selectedStationTam;
	}

	public StationDetailsDTO getFillStationsInSinTamSecond() {
		return fillStationsInSinTamSecond;
	}

	public void setFillStationsInSinTamSecond(StationDetailsDTO fillStationsInSinTamSecond) {
		this.fillStationsInSinTamSecond = fillStationsInSinTamSecond;
	}

	public boolean isStationDisa1() {
		return stationDisa1;
	}

	public void setStationDisa1(boolean stationDisa1) {
		this.stationDisa1 = stationDisa1;
	}

	public String getSelectedServiceType() {
		return selectedServiceType;
	}

	public void setSelectedServiceType(String selectedServiceType) {
		this.selectedServiceType = selectedServiceType;
	}

	public String getSelectedStageDlg() {
		return selectedStageDlg;
	}

	public void setSelectedStageDlg(String selectedStageDlg) {
		this.selectedStageDlg = selectedStageDlg;
	}

	public StationDetailsDTO getRemoveSelectedDTO() {
		return removeSelectedDTO;
	}

	public void setRemoveSelectedDTO(StationDetailsDTO removeSelectedDTO) {
		this.removeSelectedDTO = removeSelectedDTO;
	}

	public String getSelectedStatusDlg() {
		return selectedStatusDlg;
	}

	public void setSelectedStatusDlg(String selectedStatusDlg) {
		this.selectedStatusDlg = selectedStatusDlg;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public boolean isEditButtonPermission() {
		return editButtonPermission;
	}

	public void setEditButtonPermission(boolean editButtonPermission) {
		this.editButtonPermission = editButtonPermission;
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

	public StationDetailsDTO getRouteDetailsDTO() {
		return routeDetailsDTO;
	}

	public void setRouteDetailsDTO(StationDetailsDTO routeDetailsDTO) {
		this.routeDetailsDTO = routeDetailsDTO;
	}

}