package lk.informatics.ntc.view.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.MidpointUIDTO;
import lk.informatics.ntc.model.dto.RouteCreationDTO;
import lk.informatics.ntc.model.dto.RouteTypeDTO;
import lk.informatics.ntc.model.dto.StationDetailsDTO;
import lk.informatics.ntc.model.dto.VehicleInspectionDTO;
import lk.informatics.ntc.model.service.RouteCreatorService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "editRouteCreationBackingBean")
@ViewScoped
public class EditRouteCreationBackingBean implements Serializable {

	private static final long serialVersionUID = -7746519522706879481L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private RouteCreationDTO routeCreationDTO;
	private RouteCreatorService routeCreationService;

	private List<RouteCreationDTO> routes;
	private List<RouteCreationDTO> stations;
	private List<RouteCreationDTO> stations2;
	private List<RouteTypeDTO> routeTypes;
	private LinkedList<MidpointUIDTO> midPoints;
	private LinkedList<MidpointUIDTO> midPointsSwap;
	private List<VehicleInspectionDTO> busTypes;
	private List<RouteCreationDTO> routeList;
	public List<RouteCreationDTO> routeListEdit;

	private StationDetailsDTO stationDetailsDTO = new StationDetailsDTO();
	private MidpointUIDTO midpoint = new MidpointUIDTO();
	private MidpointUIDTO midpointSwap = new MidpointUIDTO();

	private String searchRoute, searchStatus;
	private String message, user;
	private boolean editMode, searched, rederBusType;

	@PostConstruct
	public void init() {
		routeCreationService = (RouteCreatorService) SpringApplicationContex.getBean("routeCreatorService");
		routeCreationDTO = new RouteCreationDTO();
		routes = new ArrayList<RouteCreationDTO>(0);
		stations = new ArrayList<RouteCreationDTO>(0);
		stations2 = new ArrayList<RouteCreationDTO>(0);
		routeTypes = new ArrayList<RouteTypeDTO>(0);
		busTypes = new ArrayList<VehicleInspectionDTO>(0);
		midPoints = new LinkedList<MidpointUIDTO>();
		midPointsSwap = new LinkedList<MidpointUIDTO>();
		routeList = new ArrayList<RouteCreationDTO>(0);
		routeListEdit = new ArrayList<RouteCreationDTO>(0);

		stationDetailsDTO = new StationDetailsDTO();
		midpoint = new MidpointUIDTO();
		midpointSwap = new MidpointUIDTO();

		user = sessionBackingBean.getLoginUser();

		loadValues();
	}

	public void loadValues() {
		editMode = false;
		searched = false;
		routes = routeCreationService.getAllRoutes();

		routeTypes = routeCreationService.getAllRouteTypes();
		busTypes = routeCreationService.getAllServiceTypes();

		midpoint.setStationDetailsDTO(stationDetailsDTO);
		midpoint.setDisplayAddNew(true);
		midpointSwap.setStationDetailsDTO(stationDetailsDTO);
		midpointSwap.setDisplayAddNew(true);
		midPoints.add(midpoint);
		midPointsSwap.add(midpointSwap);
		searchRoute = null;
		searchStatus = null;
		rederBusType = false;
	}

	@SuppressWarnings("deprecation")
	public void search() {
		routeList = routeCreationService.getRouteDetailsFromRouteNo(searchRoute, searchStatus);

		if (routeList.size() > 0) {
			searched = true;
		} else {
			setMessage("No data found.");
			RequestContext.getCurrentInstance().update("infoMSG");
			RequestContext.getCurrentInstance().execute("PF('infoMessage').show()");
		}
	}

	public void onRouteSelect() {
		if (routeCreationDTO.getRouteNo() != null && !routeCreationDTO.getRouteNo().trim().equals("")) {
			for (RouteCreationDTO r : routes) {
				if (r.getRouteNo().equals(routeCreationDTO.getRouteNo())) {
					routeCreationDTO.setStartFrom(r.getStartFrom());
					routeCreationDTO.setEndAt(r.getEndAt());
					routeCreationDTO.setLength(r.getLength());
					break;
				}
			}
		} else {
			routeCreationDTO = new RouteCreationDTO();
		}
	}

	@SuppressWarnings("deprecation")
	public void addStationsTableRow(String flag) {
		if (flag.equals("S")) { // 'S' for stations
			if (midPoints.getLast().getStationCode() != null
					&& !midPoints.getLast().getStationCode().trim().equalsIgnoreCase("")) {
				if (midPoints.getLast().getTimeTaken() != null) {
					createNewMidpointUI(null, flag);
				} else {
					setMessage("Time Taken cannot be empty.");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			} else {
				setMessage("Mid Points cannot be empty.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else if (flag.equals("R")) { // 'R' for stations reverse
			if (midPointsSwap.getLast().getStationCode() != null
					&& !midPointsSwap.getLast().getStationCode().trim().equalsIgnoreCase("")) {
				if (midPointsSwap.getLast().getTimeTaken() != null) {
					createNewMidpointUI(null, flag);
				} else {
					setMessage("Time Taken cannot be empty.");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			} else {
				setMessage("Mid Points cannot be empty.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		}

	}

	private void createNewMidpointUI(MidpointUIDTO dto, String flag) {
		if (flag.equals("S")) {
			if (midPoints != null) {
				if (dto == null) {
					dto = new MidpointUIDTO();
				}

				dto.setStationDetailsDTO(stationDetailsDTO);
				dto.setTimeTaken(null);

				midPoints.add(dto);

				shapeUpMidpointUIList(flag);
			}
		} else if (flag.equals("R")) {
			if (midPointsSwap != null) {
				if (dto == null) {
					dto = new MidpointUIDTO();
				}

				dto.setStationDetailsDTO(stationDetailsDTO);
				dto.setTimeTaken(null);

				midPointsSwap.add(dto);

				shapeUpMidpointUIList(flag);
			}
		}

	}

	public void removeStationsTableLastRow(String flag) {
		if (flag.equals("S")) {
			if (midPoints != null && !midPoints.isEmpty()) {
				midPoints.removeLast();
				shapeUpMidpointUIList(flag);
			}
		} else if (flag.equals("R")) {
			if (midPointsSwap != null && !midPointsSwap.isEmpty()) {
				midPointsSwap.removeLast();
				shapeUpMidpointUIList(flag);
			}
		}

	}

	public void shapeUpMidpointUIList(String flag) {
		if (flag.equals("S")) {
			// Set displayAddNew boolean to false of all records
			if (midPoints != null) {
				for (MidpointUIDTO r : midPoints) {
					r.setDisplayAddNew(false);
					r.setDisplayRemove(false);
					r.setDisabled(true);
				}

				MidpointUIDTO rLast = midPoints.getLast();
				rLast.setDisplayAddNew(true); // Display add icon only in last
				rLast.setDisabled(false); // Allow to edit only last row

				if (midPoints.size() > 1) {
					rLast.setDisplayRemove(true); // No need to display the
													// remove icon if there is
													// only one row
				}
			}
		} else if (flag.equals("R")) {
			// Set displayAddNew boolean to false of all records
			if (midPointsSwap != null) {
				for (MidpointUIDTO r : midPointsSwap) {
					r.setDisplayAddNew(false);
					r.setDisplayRemove(false);
					r.setDisabled(true);
				}

				MidpointUIDTO rLast = midPointsSwap.getLast();
				rLast.setDisplayAddNew(true); // Display add icon only in last
				rLast.setDisabled(false); // Allow to edit only last row

				if (midPointsSwap.size() > 1) {
					rLast.setDisplayRemove(true); // No need to display the
													// remove icon if there is
													// only one row
				}
			}
		} else if (flag.equals("EDIT_VIEW")) {
			// Set displayAddNew boolean to false of all records
			if (midPoints != null) {
				for (MidpointUIDTO r : midPoints) {
					r.setDisplayAddNew(false);
					r.setDisplayRemove(false);
					r.setDisabled(true);
				}

				MidpointUIDTO rLast = midPoints.getLast();
				rLast.setDisplayAddNew(true); // Display add icon only in last
				rLast.setDisabled(false); // Allow to edit only last row

				if (midPoints.size() > 1) {
					rLast.setDisplayRemove(true); // No need to display the
													// remove icon if there is
													// only one row
				}
			}

			// Set displayAddNew boolean to false of all records
			if (midPointsSwap != null) {
				for (MidpointUIDTO r : midPointsSwap) {
					r.setDisplayAddNew(false);
					r.setDisplayRemove(false);
					r.setDisabled(true);
				}

				MidpointUIDTO rLast = midPointsSwap.getLast();
				rLast.setDisplayAddNew(true); // Display add icon only in last
				rLast.setDisabled(false); // Allow to edit only last row

				if (midPointsSwap.size() > 1) {
					rLast.setDisplayRemove(true); // No need to display the
													// remove icon if there is
													// only one row
				}
			}
		}

	}

	@SuppressWarnings("deprecation")
	public void calculateBusSpeed() {
		if (routeCreationDTO.getTravelTime() != null
				&& !(routeCreationDTO.getLength().compareTo(BigDecimal.ZERO) == 0)) {

			int travelTimeHours = routeCreationDTO.getTravelTime().getHours();
			int travelTimeMinutes = routeCreationDTO.getTravelTime().getMinutes();
			long travelTimeMinutesToHours = travelTimeMinutes / 60;
			long totalTravelTimeHours = travelTimeHours + travelTimeMinutesToHours;

			long totalDriverRestTimeHours;
			if (routeCreationDTO.getDriverRestTime() != null) {
				int driverRestTimeHours = routeCreationDTO.getDriverRestTime().getHours();
				int driverRestTimeMinutes = routeCreationDTO.getDriverRestTime().getMinutes();
				long driverRestTimeMinutesToHours = driverRestTimeMinutes / 60;
				totalDriverRestTimeHours = driverRestTimeHours + driverRestTimeMinutesToHours;
			} else {
				totalDriverRestTimeHours = 0;
			}

			// finalTravelTime in Hours
			long finalTravelTime = totalTravelTimeHours + totalDriverRestTimeHours;

			long busSpeed = (routeCreationDTO.getLength().longValue() / finalTravelTime);
			routeCreationDTO.setBusSpeed(java.math.BigDecimal.valueOf(busSpeed));
		}
	}

	@SuppressWarnings("deprecation")
	public void add() {
		Boolean success = routeCreationService.saveRouteDetails(routeCreationDTO, midPoints, midPointsSwap, user);
		if (success) {
			setMessage("Route added successfully.");
			RequestContext.getCurrentInstance().update("successMSG");
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
			routeCreationDTO.setMidPoints(midPoints);
			routeCreationDTO.setMidPointsSwap(midPointsSwap);
			routeList.add(routeCreationDTO);
			clearRecord();
		}
	}

	@SuppressWarnings("deprecation")
	public void save() {
		String busType = null;
		if (routeCreationDTO.getRouteNo() != null && !routeCreationDTO.getRouteNo().trim().equals("")) {
			if (routeCreationDTO.getTravelTime() != null) {

				if (routeCreationDTO.getDriverRestTime() != null) {
					if (midPoints.size() == 1 && (midPoints.getFirst().getStationCode() == null
							|| midPoints.getFirst().getStationCode().equals(""))) {
						setMessage("Atleast one Midpoint should be added.");
						RequestContext.getCurrentInstance().update("infoMSG");
						RequestContext.getCurrentInstance().execute("PF('infoMessage').show()");
					} else if (midPoints.size() == 1 && midPoints.getFirst().getTimeTaken() == null) {
						setMessage("Time Taken cannot be empty.");
						RequestContext.getCurrentInstance().update("infoMSG");
						RequestContext.getCurrentInstance().execute("PF('infoMessage').show()");
					} else if (midPointsSwap.size() == 1 && (midPointsSwap.getFirst().getStationCode() == null
							|| midPointsSwap.getFirst().getStationCode().equals(""))) {
						setMessage("Atleast one Midpoint should be added.");
						RequestContext.getCurrentInstance().update("infoMSG");
						RequestContext.getCurrentInstance().execute("PF('infoMessage').show()");
					} else if (midPointsSwap.size() == 1 && midPointsSwap.getFirst().getTimeTaken() == null) {
						setMessage("Time Taken cannot be empty.");
						RequestContext.getCurrentInstance().update("infoMSG");
						RequestContext.getCurrentInstance().execute("PF('infoMessage').show()");
					} else if (midPoints.getLast().getStationCode() == null
							|| midPoints.getLast().getStationCode().equals("")) {
						setMessage("Midpoint cannot be empty..");
						RequestContext.getCurrentInstance().update("infoMSG");
						RequestContext.getCurrentInstance().execute("PF('infoMessage').show()");
					} else if (midPoints.getLast().getTimeTaken() == null) {
						setMessage("Time Taken cannot be empty.");
						RequestContext.getCurrentInstance().update("infoMSG");
						RequestContext.getCurrentInstance().execute("PF('infoMessage').show()");
					} else if (midPointsSwap.getLast().getStationCode() == null
							|| midPointsSwap.getLast().getStationCode().equals("")) {
						setMessage("Midpoint cannot be empty..");
						RequestContext.getCurrentInstance().update("infoMSG");
						RequestContext.getCurrentInstance().execute("PF('infoMessage').show()");
					} else if (midPointsSwap.getLast().getTimeTaken() == null) {
						setMessage("Time Taken cannot be empty.");
						RequestContext.getCurrentInstance().update("infoMSG");
						RequestContext.getCurrentInstance().execute("PF('infoMessage').show()");
					} else {
						Boolean success = routeCreationService.updateRouteDetailsForEdit(routeCreationDTO, midPoints,
								midPointsSwap, user);
						if (success) {
							setMessage("Route Updated successfully.");
							RequestContext.getCurrentInstance().update("successMSG");
							RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
							String busVal = routeCreationDTO.getBusTypeStr();
							clearRecord();

							routeList = routeCreationService.getRouteDetailsFromRouteNo(searchRoute, searchStatus);
							if (busVal.equalsIgnoreCase("NORMAL")) {
								busType = "001";
							} else if (busVal.equalsIgnoreCase("LUXURY")) {
								busType = "002";
							} else if (busVal.equalsIgnoreCase("SUPER LUXURY")) {
								busType = "003";
							} else if (busVal.equalsIgnoreCase("SEMI-LUXURY")) {
								busType = "004";
							} else if (busVal.equalsIgnoreCase("EXPRESSWAY BUS")) {
								busType = "EB";
							} else {
								busType = routeCreationDTO.getBusTypeStr();
							}

							routeListEdit = routeCreationService.getRouteDetailsFromRouteNoForEdit(searchRoute,
									searchStatus, busType);
							if (routeList.size() > 0) {
								searched = true;
							}
						}
					}
				} else {
					setMessage("Enter Driver Rest Time.");
					RequestContext.getCurrentInstance().update("infoMSG");
					RequestContext.getCurrentInstance().execute("PF('infoMessage').show()");
				}

			} else {
				setMessage("Enter Travel Time.");
				RequestContext.getCurrentInstance().update("infoMSG");
				RequestContext.getCurrentInstance().execute("PF('infoMessage').show()");
			}
		} else {
			setMessage("Select a Route No.");
			RequestContext.getCurrentInstance().update("infoMSG");
			RequestContext.getCurrentInstance().execute("PF('infoMessage').show()");
		}
	}

	public void clearRecord() {
		editMode = false;
		searched = false;
		routeCreationDTO = new RouteCreationDTO();
		midPoints = new LinkedList<MidpointUIDTO>();
		midPointsSwap = new LinkedList<MidpointUIDTO>();
		stationDetailsDTO = new StationDetailsDTO();
		midpoint = new MidpointUIDTO();
		midpointSwap = new MidpointUIDTO();

		midpoint.setStationDetailsDTO(stationDetailsDTO);
		midpoint.setDisplayAddNew(true);
		midpointSwap.setStationDetailsDTO(stationDetailsDTO);
		midpointSwap.setDisplayAddNew(true);
		midPoints.add(midpoint);
		midPointsSwap.add(midpointSwap);
	}

	public void searchAgain(RouteCreationDTO r) {
		String busType = null;
		if (r != null) {
			if (r.getBusTypeStr().equalsIgnoreCase("NORMAL")) {
				busType = "001";
			}
			else if (r.getBusTypeStr().equalsIgnoreCase("LUXURY")) {
				busType = "002";
			}
			else if (r.getBusTypeStr().equalsIgnoreCase("SUPER LUXURY")) {
				busType = "003";
			}
			else if (r.getBusTypeStr().equalsIgnoreCase("SEMI-LUXURY")) {
				busType = "004";
			}
			else if (r.getBusTypeStr().equalsIgnoreCase("EXPRESSWAY BUS")) {
				busType = "EB";
			}

			routeListEdit = routeCreationService.getRouteDetailsFromRouteNoForEdit(searchRoute, r.getStatus(), busType);

			stations = routeCreationService.getAllStationsForRouteCreaterEdit(searchRoute, busType);
			stations2 = routeCreationService.getAllStationsForRouteCreaterEdit(searchRoute, busType);

		}

	}

	public void edit(RouteCreationDTO r) {
		editMode = true;
		if (editMode) {
			searchAgain(r);
		}

		String busType = null;
		if (r != null) {
			if (r.getBusTypeStr().equalsIgnoreCase("NORMAL")) {
				busType = "001";
			}
			else if (r.getBusTypeStr().equalsIgnoreCase("LUXURY")) {
				busType = "002";
			}
			else if (r.getBusTypeStr().equalsIgnoreCase("SUPER LUXURY")) {
				busType = "003";
			}
			else if (r.getBusTypeStr().equalsIgnoreCase("SEMI-LUXURY")) {
				busType = "004";
			}
			else if (r.getBusTypeStr().equalsIgnoreCase("EXPRESSWAY BUS")) {
				busType = "EB";
			}
		}

		setRouteCreationDTO(r);
		midPoints = routeCreationService.getMidPointsFromRouteNoAndBusType(r.getRouteNo(), busType);
		midPointsSwap = routeCreationService.getMidPointsReverseFromRouteNoAndBusType(r.getRouteNo(), busType);

		shapeUpMidpointUIList("EDIT_VIEW");

		rederBusType = true;

	}

	public void clearForm() {
		init();
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public RouteCreationDTO getRouteCreationDTO() {
		return routeCreationDTO;
	}

	public void setRouteCreationDTO(RouteCreationDTO routeCreationDTO) {
		this.routeCreationDTO = routeCreationDTO;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public RouteCreatorService getRouteCreationService() {
		return routeCreationService;
	}

	public void setRouteCreationService(RouteCreatorService routeCreationService) {
		this.routeCreationService = routeCreationService;
	}

	public List<RouteCreationDTO> getStations() {
		return stations;
	}

	public void setStations(List<RouteCreationDTO> stations) {
		this.stations = stations;
	}

	public List<RouteCreationDTO> getRoutes() {
		return routes;
	}

	public void setRoutes(List<RouteCreationDTO> routes) {
		this.routes = routes;
	}

	public List<MidpointUIDTO> getMidPointsSwap() {
		return midPointsSwap;
	}

	public void setMidPointsSwap(LinkedList<MidpointUIDTO> midPointsSwap) {
		this.midPointsSwap = midPointsSwap;
	}

	public List<RouteTypeDTO> getRouteTypes() {
		return routeTypes;
	}

	public void setRouteTypes(List<RouteTypeDTO> routeTypes) {
		this.routeTypes = routeTypes;
	}

	public StationDetailsDTO getStationDetailsDTO() {
		return stationDetailsDTO;
	}

	public void setStationDetailsDTO(StationDetailsDTO stationDetailsDTO) {
		this.stationDetailsDTO = stationDetailsDTO;
	}

	public MidpointUIDTO getMidpoint() {
		return midpoint;
	}

	public void setMidpoint(MidpointUIDTO midpoint) {
		this.midpoint = midpoint;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public List<RouteCreationDTO> getRouteList() {
		return routeList;
	}

	public void setRouteList(List<RouteCreationDTO> routeList) {
		this.routeList = routeList;
	}

	public void setMidPoints(LinkedList<MidpointUIDTO> midPoints) {
		this.midPoints = midPoints;
	}

	public LinkedList<MidpointUIDTO> getMidPoints() {
		return midPoints;
	}

	public MidpointUIDTO getMidpointSwap() {
		return midpointSwap;
	}

	public void setMidpointSwap(MidpointUIDTO midpointSwap) {
		this.midpointSwap = midpointSwap;
	}

	public List<VehicleInspectionDTO> getBusTypes() {
		return busTypes;
	}

	public void setBusTypes(List<VehicleInspectionDTO> busTypes) {
		this.busTypes = busTypes;
	}

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	public String getSearchRoute() {
		return searchRoute;
	}

	public void setSearchRoute(String searchRoute) {
		this.searchRoute = searchRoute;
	}

	public String getSearchStatus() {
		return searchStatus;
	}

	public void setSearchStatus(String searchStatus) {
		this.searchStatus = searchStatus;
	}

	public boolean isSearched() {
		return searched;
	}

	public void setSearched(boolean searched) {
		this.searched = searched;
	}

	public boolean isRederBusType() {
		return rederBusType;
	}

	public void setRederBusType(boolean rederBusType) {
		this.rederBusType = rederBusType;
	}

	public List<RouteCreationDTO> getRouteListEdit() {
		return routeListEdit;
	}

	public void setRouteListEdit(List<RouteCreationDTO> routeListEdit) {
		this.routeListEdit = routeListEdit;
	}

	public List<RouteCreationDTO> getStations2() {
		return stations2;
	}

	public void setStations2(List<RouteCreationDTO> stations2) {
		this.stations2 = stations2;
	}

}
