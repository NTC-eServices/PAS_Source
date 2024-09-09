package lk.informatics.ntc.view.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

@ManagedBean(name = "assignStationToRoutesBackingBean")
@ViewScoped
public class AssignStationToRoutesBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	// services
	private RouteCreatorService routeCreatorService;
	private CommonService commonService;

	private static final String SERVICE_TYPE_NORMAL = "001";

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
	private List<StationDetailsDTO> showGridDataDTOList = new ArrayList<StationDetailsDTO>();

	private boolean stationDisa, stationDisa1;
	private boolean serviceTypeDisa, editFlag;
	private String successMessage, errorMessage, selectedServiceType, selectedStatus, loginUser, selectedstationCode,
			selectedStationSin, selectedStationTam, selectedStage;
	private List<RouteCreationDTO> routeNamesList;
	private List<String> stagesList;
	private String selectedRoute;
	private String selectedStageDlg;
	private String selectedStatusDlg;

	private List<RouteCreationDTO> routeList;
	private boolean copyRouteBtn, editButtonPermission;
	private String origin;
	private String destination;
	private StationDetailsDTO routeDetailsDTO;

	private boolean selectedLuxury, selectedSemiLuxury, selectedSuperLuxury, selectedExpress;
	private Map<String, String> stageAndStation;

	@PostConstruct
	public void init() {
		routeCreatorService = (RouteCreatorService) SpringApplicationContex.getBean("routeCreatorService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN205", "C");
		editButtonPermission = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN205", "E");
		stationToRoute = new RouteCreationDTO();
		stationToRouteSecondDTO = new StationDetailsDTO();
		routeNOList = routeCreatorService.getAllRoutes();
		
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

	public void fillRouteDetail() {
		showRouteDetails = routeCreatorService.getRouteDetails(stationToRoute);
		stationList = routeCreatorService.getAllStations();
		String routeNo = stationToRoute.getRouteNo();
		showGridDataDTOList = routeCreatorService.showDataonGrid(routeNo);
		stageAndStation = routeCreatorService.getDataForCheckDuplicate(routeNo);
		routeDetailsDTO = routeCreatorService.retrieveRouteDataForRouteNum(routeNo);
		stationDisa = false;

		if (routeDetailsDTO != null) {
			origin = routeDetailsDTO.getOrigin();
			destination = routeDetailsDTO.getDestination();
		}
		stationToRouteSecondDTO.setStage(Integer.toString(showGridDataDTOList.size()));
		stationToRouteSecondDTO.setServiceTypeCode(SERVICE_TYPE_NORMAL);
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
		long start2 = System.currentTimeMillis();
		
		if (stationToRoute.getRouteNo() != null && !stationToRoute.getRouteNo().trim().isEmpty()) {
			if (stationToRouteSecondDTO.getStationCode() != null
					&& !stationToRouteSecondDTO.getStationCode().trim().isEmpty()) {

				String status = stationToRoute.getStatusRouteToStation();

				if (stageAndStation != null
						&& stageAndStation.containsValue(stationToRouteSecondDTO.getStationCode())) {
					setErrorMessage("Station is already added");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					return;
				}

				StationDetailsDTO dto = new StationDetailsDTO();
				dto.setStationCode(stationToRouteSecondDTO.getStationCode());
				dto.setStatus(status);
				dto.setServiceTypeCode(stationToRouteSecondDTO.getServiceTypeCode());
				int stage = 0;
				if (showGridDataDTOList.size() != 0) {
					stage = Integer.valueOf(showGridDataDTOList.size());
				}
				dto.setStage(Integer.toString(stage));
				dto.setStationNameEn(routeCreatorService.getStationDescFromStationCode(dto.getStationCode()));
				dto.setEdit(true);

				showGridDataDTOList.add(dto);

				stageAndStation.put(dto.getStage(), dto.getStationCode());

				stationToRouteSecondDTO.setStationCode("");
				stationToRouteSecondDTO.setStage(Integer.toString(showGridDataDTOList.size()));

//				Collections.sort(showGridDataDTOList, new Comparator<StationDetailsDTO>() {
//					@Override
//					public int compare(StationDetailsDTO u1, StationDetailsDTO u2) {
//						return Integer.valueOf(u2.getStage()) - Integer.valueOf(u1.getStage());
//					}
//				});
//
//				Collections.reverse(showGridDataDTOList);
			} else {
				setErrorMessage("Please select a station");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

			}
		} else {
			setErrorMessage("Please select a route");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
		
		long end2 = System.currentTimeMillis();
//	    System.out.println("addDetails-Elapsed Time in milli seconds: "+ (end2-start2));
	}

	public void editButton() {
		RequestContext.getCurrentInstance().execute("PF('routeStation').show()");
		stationToRouteSecondDTO.setStage(selectedRow.getStage());
		stationToRouteSecondDTO.setStationCode(selectedRow.getStationCode());
		fillStationSinAndTamil();
		stationToRouteSecondDTO.setServiceTypeDes(selectedRow.getServiceTypeDes());
		stationToRouteSecondDTO.setServiceTypeCode(selectedRow.getServiceTypeCode());

		selectedServiceType = SERVICE_TYPE_NORMAL;

		if (selectedRow.isLuxury()) {
			selectedLuxury = true;
		} else {
			selectedLuxury = false;
		}
		if (selectedRow.isSemiLuxury()) {
			selectedSemiLuxury = true;
		} else {
			selectedSemiLuxury = false;
		}
		if (selectedRow.isSuperLuxury()) {
			selectedSuperLuxury = true;
		} else {
			selectedSuperLuxury = false;
		}
		if (selectedRow.isExpress()) {
			selectedExpress = true;
		} else {
			selectedExpress = false;
		}
		selectedStage = null;
		editFlag = true;

	}

	public void removeVal() {

		List<StationDetailsDTO> tempStationDTOList = new ArrayList<StationDetailsDTO>();

		routeCreatorService.updateStageSeq(stationToRoute.getRouteNo(), selectedRow.getStage(),
				selectedRow.getStationCode(), stationToRouteSecondDTO, loginUser);

		setSuccessMessage("Succesfully Deleted.");
		RequestContext.getCurrentInstance().update("successMSG");
		RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

		showGridDataDTOList.remove(selectedRow);
		stageAndStation.remove(selectedRow.getStage(), selectedRow.getStationCode());
		for (StationDetailsDTO dto : showGridDataDTOList) {
			StationDetailsDTO station2DTO = new StationDetailsDTO();
			if (Integer.parseInt(dto.getStage()) > Integer.parseInt(selectedRow.getStage())) {
				station2DTO = dto;
				int stageInt = Integer.parseInt(dto.getStage()) - 1;

				station2DTO.setStage(Integer.toString(stageInt));
				tempStationDTOList.add(station2DTO);
			} else {
				tempStationDTOList.add(dto);
			}

		}
		showGridDataDTOList = tempStationDTOList;

		stationToRouteSecondDTO.setStage(Integer.toString(showGridDataDTOList.size()));

		Collections.sort(showGridDataDTOList, new Comparator<StationDetailsDTO>() {
			@Override
			public int compare(StationDetailsDTO u1, StationDetailsDTO u2) {
				return Integer.valueOf(u2.getStage()) - Integer.valueOf(u1.getStage());
			}
		});

		Collections.reverse(showGridDataDTOList);
	}

	public void addChangeDetails() {
		if (selectedStage == null || selectedStage.isEmpty() || selectedStage.trim().equalsIgnoreCase("")) {
			setErrorMessage("Entered stage is empty.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			return;
		} else {
			if (selectedStage != null && !selectedStage.isEmpty() && !selectedStage.trim().equalsIgnoreCase("")) {

				// can be a new route adding
				StationDetailsDTO tempDto = new StationDetailsDTO();
				for (StationDetailsDTO dto : showGridDataDTOList) {
					if (dto.getStage() != null && !dto.getStage().isEmpty()
							&& !dto.getStage().trim().equalsIgnoreCase("")) {
						if (dto.getStage().equalsIgnoreCase(selectedStage)) {
							tempDto = dto;
						}
					}
				}

				if (tempDto != null && tempDto.getStage() != null && !tempDto.getStage().isEmpty()) {
					StationDetailsDTO addDTO1 = new StationDetailsDTO();
					StationDetailsDTO addDTO2 = new StationDetailsDTO();

					addDTO1 = selectedRow;
					addDTO1.setStage(selectedRow.getStage());
					addDTO2 = tempDto;

					showGridDataDTOList.remove(selectedRow);
					showGridDataDTOList.remove(tempDto);

					addDTO1.setStage(tempDto.getStage());
					addDTO2.setStage(stationToRouteSecondDTO.getStage());

					showGridDataDTOList.add(addDTO1);
					showGridDataDTOList.add(addDTO2);
				} else {
					setErrorMessage("Entered stage is not exist to swap.");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					return;
				}

			}

			Collections.sort(showGridDataDTOList, new Comparator<StationDetailsDTO>() {
				@Override
				public int compare(StationDetailsDTO u1, StationDetailsDTO u2) {
					return Integer.valueOf(u2.getStage()) - Integer.valueOf(u1.getStage());
				}
			});

			Collections.reverse(showGridDataDTOList);

			RequestContext.getCurrentInstance().execute("PF('routeStation').hide()");
			setSuccessMessage("Data succesfully updated.");
			RequestContext.getCurrentInstance().update("successMSG");
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
		}
	}

	public void reorderDetails() {

		if (selectedStage == null || selectedStage.isEmpty() || selectedStage.trim().equalsIgnoreCase("")) {
			setErrorMessage("Entered stage is empty.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			return;
		} else {
			if (selectedStage != null && !selectedStage.isEmpty() && !selectedStage.trim().equalsIgnoreCase("")) {

				// can be a new route adding
				StationDetailsDTO tempDto = new StationDetailsDTO();
				boolean found = true;
				for (StationDetailsDTO dto : showGridDataDTOList) {
					if (dto.getStage() != null && !dto.getStage().isEmpty()
							&& !dto.getStage().trim().equalsIgnoreCase("")) {
						if (dto.getStage().equalsIgnoreCase(selectedStage)) {
							found = true;
						}
					}
				}

				if (found) {

					StationDetailsDTO selectedDTO = new StationDetailsDTO();
					selectedDTO = selectedRow;
					boolean changeStart = false;

					for (int i = 0; i < showGridDataDTOList.size(); i++) {
						if (Integer.valueOf(showGridDataDTOList.get(i).getStage()) <= Integer
								.valueOf(selectedRow.getStage())) {
							if (showGridDataDTOList.get(i).getStage().equals(selectedStage)) {
								changeStart = true;
							}

							if (changeStart) {
								if (showGridDataDTOList.get(i).getStationCode().equals(selectedDTO.getStationCode())) {
									showGridDataDTOList.get(i).setStage(selectedStage);
								} else {
									showGridDataDTOList.get(i).setStage(Integer
											.toString((Integer.valueOf(showGridDataDTOList.get(i).getStage()) + 1)));
								}
							}
						}
					}

				} else {
					setErrorMessage("Entered stage is not exist to reorder.");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					return;
				}

			}

			Collections.sort(showGridDataDTOList, new Comparator<StationDetailsDTO>() {
				@Override
				public int compare(StationDetailsDTO u1, StationDetailsDTO u2) {
					return Integer.valueOf(u2.getStage()) - Integer.valueOf(u1.getStage());
				}
			});

			Collections.reverse(showGridDataDTOList);

			RequestContext.getCurrentInstance().execute("PF('routeStation').hide()");
			setSuccessMessage("Data succesfully updated.");
			RequestContext.getCurrentInstance().update("successMSG");
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

		}
	}

	public void clearDetail() {

		stationToRoute = new RouteCreationDTO();
		stationToRouteSecondDTO = new StationDetailsDTO();
		showRouteDetails = new ArrayList<RouteCreationDTO>();

		showGridDataDTOList = new ArrayList<StationDetailsDTO>();
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
		routeList = new ArrayList<RouteCreationDTO>();
		RequestContext.getCurrentInstance().update("copyRouteDets:frmcopyRoute:copyRouteDT");
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

		/** Check duplicate list start **/
		for (StationDetailsDTO stationDetailsDTO : showGridDataDTOList) {
			for (RouteCreationDTO routeDetailsDTO : routeList) {
				if (stationDetailsDTO.getStationCode() != null && !stationDetailsDTO.getStationCode().isEmpty()
						&& stationDetailsDTO.getStationCode().equalsIgnoreCase(routeDetailsDTO.getStation())) {

					setErrorMessage("Station " + stationDetailsDTO.getStationNameEn() + " is already added");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					return;

				}
			}
		}
		/** Check duplicate list end **/

		if (showGridDataDTOList != null && showGridDataDTOList.size() != 0) {
			stageCount = showGridDataDTOList.get(showGridDataDTOList.size() - 1).getStage();
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
				if (stage == 0) {
					stage = 0;
				}
				staionDto.setStage(Integer.toString(stage));
				stage += 1;
			}

			staionDto.setServiceTypeCode(SERVICE_TYPE_NORMAL);
			staionDto.setEdit(true);

			showGridDataDTOList.add(staionDto);
		}
		RequestContext.getCurrentInstance().update("form");
		RequestContext.getCurrentInstance().execute("PF('copyRouteDets').hide()");

		Collections.sort(showGridDataDTOList, new Comparator<StationDetailsDTO>() {
			@Override
			public int compare(StationDetailsDTO u1, StationDetailsDTO u2) {
				return Integer.valueOf(u2.getStage()) - Integer.valueOf(u1.getStage());
			}
		});

		Collections.reverse(showGridDataDTOList);

		sessionBackingBean.setMessage("Routes were copied successfully");
		RequestContext.getCurrentInstance().execute("PF('dlgCommonSuccess').show()");
	}

	public void saveDetails() {
		showGridDataDTOList = routeCreatorService.insertAssignStationToRoute(showGridDataDTOList, loginUser,
				stationToRoute.getRouteNo());

		Collections.sort(showGridDataDTOList, new Comparator<StationDetailsDTO>() {
			@Override
			public int compare(StationDetailsDTO u1, StationDetailsDTO u2) {
				return Integer.valueOf(u2.getStage()) - Integer.valueOf(u1.getStage());
			}
		});

		Collections.reverse(showGridDataDTOList);

		setSuccessMessage("Data saved successfully");
		RequestContext.getCurrentInstance().update("successMSG");
		RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
	}

	public void clearForm() {
		clearDetail();
		showGridDataDTOList = new ArrayList<StationDetailsDTO>();
		RequestContext.getCurrentInstance().update("form");
	}

	public void clearDetPop() {
		selectedstationCode = null;
		selectedStage = null;
		origin = null;
		destination = null;
		selectedLuxury = false;
		selectedSemiLuxury = false;
		selectedSuperLuxury = false;
		selectedExpress = false;

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

	public List<StationDetailsDTO> getShowGridDataDTOList() {
		return showGridDataDTOList;
	}

	public void setShowGridDataDTOList(List<StationDetailsDTO> showGridDataDTOList) {
		this.showGridDataDTOList = showGridDataDTOList;
	}

	public boolean isSelectedLuxury() {
		return selectedLuxury;
	}

	public void setSelectedLuxury(boolean selectedLuxury) {
		this.selectedLuxury = selectedLuxury;
	}

	public boolean isSelectedSemiLuxury() {
		return selectedSemiLuxury;
	}

	public void setSelectedSemiLuxury(boolean selectedSemiLuxury) {
		this.selectedSemiLuxury = selectedSemiLuxury;
	}

	public boolean isSelectedSuperLuxury() {
		return selectedSuperLuxury;
	}

	public void setSelectedSuperLuxury(boolean selectedSuperLuxury) {
		this.selectedSuperLuxury = selectedSuperLuxury;
	}

	public boolean isSelectedExpress() {
		return selectedExpress;
	}

	public void setSelectedExpress(boolean selectedExpress) {
		this.selectedExpress = selectedExpress;
	}

}