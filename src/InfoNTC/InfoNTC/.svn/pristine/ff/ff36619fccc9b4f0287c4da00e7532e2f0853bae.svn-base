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

@ManagedBean(name = "routeCreationBean")
@ViewScoped
public class RouteCreationBean implements Serializable {

	private static final long serialVersionUID = -7746519522706879481L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private RouteCreationDTO routeCreationDTO;
	private RouteCreatorService routeCreationService;

	private List<RouteCreationDTO> routes;
	private List<StationDetailsDTO> stations;
	private List<RouteTypeDTO> routeTypes;
	private LinkedList<MidpointUIDTO> midPoints;
	private LinkedList<MidpointUIDTO> midPointsSwap;
	private List<VehicleInspectionDTO> busTypes;
	private List<RouteCreationDTO> routeList;

	private StationDetailsDTO stationDetailsDTO = new StationDetailsDTO();
	private MidpointUIDTO midpoint = new MidpointUIDTO();
	private MidpointUIDTO midpointSwap = new MidpointUIDTO();

	private String message, user;
	private boolean editMode, searched;
	private boolean editMode1;

	@PostConstruct
	public void init() {
		routeCreationService = (RouteCreatorService) SpringApplicationContex.getBean("routeCreatorService");
		routeCreationDTO = new RouteCreationDTO();
		routes = new ArrayList<RouteCreationDTO>(0);
		stations = new ArrayList<StationDetailsDTO>(0);
		routeTypes = new ArrayList<RouteTypeDTO>(0);
		busTypes = new ArrayList<VehicleInspectionDTO>(0);
		midPoints = new LinkedList<MidpointUIDTO>();
		midPointsSwap = new LinkedList<MidpointUIDTO>();
		routeList = new ArrayList<RouteCreationDTO>(0);

		stationDetailsDTO = new StationDetailsDTO();
		midpoint = new MidpointUIDTO();
		midpointSwap = new MidpointUIDTO();

		user = sessionBackingBean.getLoginUser();
		editMode1 = true;

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
	}

	public void onRouteStations() {

		routeCreationDTO.setTravelTime(null);
		routeCreationDTO.setDriverRestTime(null);

		stations = routeCreationService.getAllStationsOnRoutes(routeCreationDTO.getRouteNo(),
				routeCreationDTO.getBusTypeStr()); // added by tharushi.e
		routeList = routeCreationService.getRouteDetailsByRouteandType(routeCreationDTO.getRouteNo(),
				routeCreationDTO.getBusTypeStr());

		if (!routeList.isEmpty()) {
			editMode1 = false;
			for (int a = 0; a < routeList.size(); a++) {
				routeCreationDTO.setAbbreviationStart(routeList.get(a).getAbbreviationStart());
				routeCreationDTO.setAbbreviationEnd(routeList.get(a).getAbbreviationEnd());
			}

		} else
			editMode1 = true;
	}

	public void onRouteSelect() {
		if (routeCreationDTO.getRouteNo() != null && !routeCreationDTO.getRouteNo().trim().equals("")) {

			List<RouteCreationDTO> route = routeCreationService
					.getRouteDetailsFromRouteNo(routeCreationDTO.getRouteNo(), null);

			for (RouteCreationDTO r : routes) {
				if (r.getRouteNo().equals(routeCreationDTO.getRouteNo())) {
					routeCreationDTO = new RouteCreationDTO();
					routeCreationDTO.setRouteNo(r.getRouteNo());
					routeCreationDTO.setStartFrom(r.getStartFrom());
					routeCreationDTO.setEndAt(r.getEndAt());
					routeCreationDTO.setLength(r.getLength());
					break;
				}
			}
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
			editMode = false;
			searched = true;

		} else {
			routeCreationDTO = new RouteCreationDTO();
			searched = false;
		}
	}

	@SuppressWarnings("deprecation")
	public void addStationsTableRow(String flag) {
		if (flag.equals("S")) {
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
		} else if (flag.equals("R")) {
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

			float travelTimeHours = routeCreationDTO.getTravelTime().getHours();
			float travelTimeMinutes = routeCreationDTO.getTravelTime().getMinutes();
			float hoursToMinute = travelTimeHours * 60;
			float totalMinutes = hoursToMinute + travelTimeMinutes;
			float totalTravelTimeHours = totalMinutes / 60;

			float totalDriverRestTimeHours;
			if (routeCreationDTO.getDriverRestTime() != null) {
				float driverRestTimeHours = routeCreationDTO.getDriverRestTime().getHours();
				float driverRestTimeMinutes = routeCreationDTO.getDriverRestTime().getMinutes();

				float hoursToMinuteRestTime = driverRestTimeHours * 60;
				float totalMinutesRestTime = hoursToMinuteRestTime + driverRestTimeMinutes;
				totalDriverRestTimeHours = totalMinutesRestTime / 60;
			} else {
				totalDriverRestTimeHours = 0;
			}

			// finalTravelTime in Hours
			float finalTravelTime = totalTravelTimeHours + totalDriverRestTimeHours;

			float busSpeed = (routeCreationDTO.getLength().longValue() / totalTravelTimeHours);
			BigDecimal busSpeedNew = java.math.BigDecimal.valueOf(busSpeed);
			routeCreationDTO.setBusSpeed(busSpeedNew.setScale(2, BigDecimal.ROUND_HALF_UP));
		}
	}

	/* Rounding Bus Speed */
	public static float round(float d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(Float.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.floatValue();
	}

	@SuppressWarnings("deprecation")
	public void add() {
		if (routeCreationDTO.getRouteNo() != null && !routeCreationDTO.getRouteNo().trim().equals("")) {
			if (routeCreationDTO.getBusTypeStr() != null && !routeCreationDTO.getBusTypeStr().trim().equals("")) {
				if (routeCreationDTO.getTravelTime() != null) {
					if (routeCreationDTO.getDriverRestTime() != null) {
						if (routeCreationDTO.getAbbreviationStart() != null
								&& !routeCreationDTO.getAbbreviationStart().isEmpty()
								&& !routeCreationDTO.getAbbreviationStart().equalsIgnoreCase("")) {
							if (routeCreationDTO.getAbbreviationEnd() != null
									&& !routeCreationDTO.getAbbreviationEnd().isEmpty()
									&& !routeCreationDTO.getAbbreviationEnd().equalsIgnoreCase("")) {
								if (midPoints.size() == 1 && (midPoints.getFirst().getStationCode() == null
										|| midPoints.getFirst().getStationCode().equals(""))) {
									setMessage("Atleast one Midpoint should be added.");
									RequestContext.getCurrentInstance().update("infoMSG");
									RequestContext.getCurrentInstance().execute("PF('infoMessage').show()");
								} else if (midPoints.size() == 1 && midPoints.getFirst().getTimeTaken() == null) {
									setMessage("Time Taken cannot be empty.");
									RequestContext.getCurrentInstance().update("infoMSG");
									RequestContext.getCurrentInstance().execute("PF('infoMessage').show()");
								} else if (midPointsSwap.size() == 1
										&& (midPointsSwap.getFirst().getStationCode() == null
												|| midPointsSwap.getFirst().getStationCode().equals(""))) {
									setMessage("Atleast one Midpoint should be added.");
									RequestContext.getCurrentInstance().update("infoMSG");
									RequestContext.getCurrentInstance().execute("PF('infoMessage').show()");
								} else if (midPointsSwap.size() == 1
										&& midPointsSwap.getFirst().getTimeTaken() == null) {
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
									String timetaken = routeCreationDTO.getTimetaken();
									routeCreationDTO.setTimetaken(timetaken);
									Boolean success = routeCreationService.saveRouteDetails(routeCreationDTO, midPoints,
											midPointsSwap, user);
									if (success) {
										setMessage("Route added successfully.");
										RequestContext.getCurrentInstance().update("successMSG");
										RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
										// get all midpoints with sequence no.

										LinkedList<MidpointUIDTO> mp = routeCreationService
												.getMidPointsFromRouteNo(routeCreationDTO.getRouteNo());
										LinkedList<MidpointUIDTO> mps = routeCreationService
												.getMidPointsReverseFromRouteNo(routeCreationDTO.getRouteNo());

										routeCreationDTO.setMidPoints(mp);
										routeCreationDTO.setMidPointsSwap(mps);
										routeList.add(routeCreationDTO);

										clearMidpoints();
									}
								}

							} else {
								setMessage("Enter Abbrivation Letter End At.");
								RequestContext.getCurrentInstance().update("infoMSG");
								RequestContext.getCurrentInstance().execute("PF('infoMessage').show()");
							}

						} else {
							setMessage("Enter Abbrivation Letter From.");
							RequestContext.getCurrentInstance().update("infoMSG");
							RequestContext.getCurrentInstance().execute("PF('infoMessage').show()");
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
				setMessage("Select a Bus Type.");
				RequestContext.getCurrentInstance().update("infoMSG");
				RequestContext.getCurrentInstance().execute("PF('infoMessage').show()");
			}
		} else {
			setMessage("Select a Route No.");
			RequestContext.getCurrentInstance().update("infoMSG");
			RequestContext.getCurrentInstance().execute("PF('infoMessage').show()");
		}
	}

	@SuppressWarnings("deprecation")
	public void save() {
		if (routeCreationDTO.getRouteNo() != null && !routeCreationDTO.getRouteNo().trim().equals("")) {
			if (routeCreationDTO.getBusTypeStr() != null && !routeCreationDTO.getBusTypeStr().trim().equals("")) {
				if (routeCreationDTO.getTravelTime() != null) {
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
						Boolean success = routeCreationService.updateRouteDetails(routeCreationDTO, midPoints,
								midPointsSwap, user);
						if (success) {
							setMessage("Route Updated successfully.");
							RequestContext.getCurrentInstance().update("successMSG");
							RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
							// get all midpoints with sequence no.

							LinkedList<MidpointUIDTO> mp = routeCreationService
									.getMidPointsFromRouteNo(routeCreationDTO.getRouteNo());
							LinkedList<MidpointUIDTO> mps = routeCreationService
									.getMidPointsReverseFromRouteNo(routeCreationDTO.getRouteNo());

							routeCreationDTO.setMidPoints(mp);
							routeCreationDTO.setMidPointsSwap(mps);
							for (RouteCreationDTO r : routeList) {
								if (r.getRouteNo().equals(routeCreationDTO.getRouteNo())) {
									routeList.remove(r);
									break;
								}
							}
							routeList.add(routeCreationDTO);

							clearMidpoints();
						}
					}
				} else {
					setMessage("Enter Travel Time.");
					RequestContext.getCurrentInstance().update("infoMSG");
					RequestContext.getCurrentInstance().execute("PF('infoMessage').show()");
				}
			} else {
				setMessage("Select a Bus Type.");
				RequestContext.getCurrentInstance().update("infoMSG");
				RequestContext.getCurrentInstance().execute("PF('infoMessage').show()");
			}

		} else

		{
			setMessage("Select a Route No.");
			RequestContext.getCurrentInstance().update("infoMSG");
			RequestContext.getCurrentInstance().execute("PF('infoMessage').show()");
		}
	}

	public void clearRecord() {
		editMode = false;
		searched = false;
		// routeCreationDTO = new RouteCreationDTO();
		routeCreationDTO.setBusTypeStr(null);

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
		editMode1 = true;
	}

	public void clearMidpoints() {
		midpoint = new MidpointUIDTO();
		midpointSwap = new MidpointUIDTO();
		midPoints.clear();
		midPointsSwap.clear();
		routeCreationDTO.setBusTypeStr(null);

		midpoint.setStationDetailsDTO(stationDetailsDTO);
		midpoint.setDisplayAddNew(true);
		midpointSwap.setStationDetailsDTO(stationDetailsDTO);
		midpointSwap.setDisplayAddNew(true);
		midPoints.add(midpoint);
		midPointsSwap.add(midpointSwap);
		editMode1 = true;
	}

	public void edit(RouteCreationDTO r) {
		if (r != null) {
			setRouteCreationDTO(r);
			midPoints = r.getMidPoints();
			midPointsSwap = r.getMidPointsSwap();
			editMode = true;
			shapeUpMidpointUIList("EDIT_VIEW");
		}
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

	public List<StationDetailsDTO> getStations() {
		return stations;
	}

	public void setStations(List<StationDetailsDTO> stations) {
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

	public boolean isEditMode1() {
		return editMode1;
	}

	public void setEditMode1(boolean editMode1) {
		this.editMode1 = editMode1;
	}

	public boolean isSearched() {
		return searched;
	}

	public void setSearched(boolean searched) {
		this.searched = searched;
	}

}
