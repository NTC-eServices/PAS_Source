package lk.informatics.ntc.view.beans;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.RouteScheduleDTO;
import lk.informatics.ntc.model.service.RouteScheduleService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "routeScheduleGeneratorBeanForNoValidations")
@ViewScoped
public class RouteScheduleGeneratorBackingBeanForNoValidations implements Serializable {

	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	private RouteScheduleService routeScheduleService;
	private RouteScheduleDTO routeScheduleDTO;
	private List<RouteScheduleDTO> busRouteList, groupNoList, selectLeavePositionList, busCategoryList, busForRouteList,
			leaveForRouteList, mainSaveList, editBusForRouteList, tempEditBusForRouteList;
	private List<String> leavePositionList;
	private List<ColumnModel> columns, columnsLeaves, editColumns;
	private boolean renderPanelTwo, disableGroupNo, disableBusCategory;
	private String alertMSG, successMessage, errorMessage, origin, destination, groupNo;
	private String tripType, rotationType; // Type "O" for origin and "D" for
											// destination
	private int noOfLeaves, noOfBuses, noOfTrips, noOfDaysFortimeTable, noOfTripsForSelectedSide, tripID,
			noOfDaysForEdit;
	private boolean disabledNoramlRotation, disabledZigzagRotation, renderTableOne, renderTableTwo,
			disabledgenerateReport, disabledClear, disabledSave, disabledSwap, disabledleavePositionList;

	private final int maximumLeaveForDay = 9;
	private final int maximumDays = 92;

	private boolean edit;
	private boolean originDataVisible;
	private boolean destinationDataVisible;
	private boolean lastPanelDateVisible;

	private Date selectedStartDate;
	private Date selectedEndDate;
	private List<String> dateList;
	private String selectedDateRange;
	private boolean rotationNew;
	int dateCount = 0;
	private boolean afterSearch = false;
	private String dayOne, dayTwo, dayThree, dayFour, dayFive;
	private String tripOne, tripTwo, tripThree, tripFour, tripFive;
	private String busOne, busTwo, busThree, busFour, busFive;
	private String coupleCount = null;
	private Date lastPanelStartDateNew;

	@PostConstruct
	public void init() {
		routeScheduleService = (RouteScheduleService) SpringApplicationContex.getBean("routeScheduleService");
		disableGroupNo = true;
		loadValue();
	}

	private void loadValue() {
		routeScheduleDTO = new RouteScheduleDTO();
		busRouteList = new ArrayList<RouteScheduleDTO>();
		busCategoryList = new ArrayList<RouteScheduleDTO>();
		mainSaveList = new ArrayList<RouteScheduleDTO>();
		busRouteList = routeScheduleService.getRouteNo();
		leavePositionList = new ArrayList<>();
		disableBusCategory = true;
		disabledNoramlRotation = true;
		disabledZigzagRotation = true;
		renderTableOne = false;
		renderTableTwo = false;
		disabledgenerateReport = true;
		disabledClear = true;
		disabledSave = true;
		disabledSwap = false;
		disabledleavePositionList = true;

		editBusForRouteList = new ArrayList<RouteScheduleDTO>();
		tempEditBusForRouteList = new ArrayList<RouteScheduleDTO>();

		edit = false;
		originDataVisible = false;
		destinationDataVisible = false;
		lastPanelDateVisible = false;

		dateList = new ArrayList<String>();
		selectedDateRange = null;
		rotationNew = false;

		afterSearch = false;
	}

	public void ajaxFillbusCategory() {
		busCategoryList = routeScheduleService.getBusCategoryList(routeScheduleDTO.getRouteNo());
		disableBusCategory = false;
	}

	public void ajaxSetGroupNo() {
		groupNo = routeScheduleDTO.getGroupNo();
	}

	public void ajaxFillRouteDetails() {

		RouteScheduleDTO dto = routeScheduleService.getRouteDetails(routeScheduleDTO.getRouteNo(),
				routeScheduleDTO.getBusCategory());

		if (dto.getOrigin() != null && !dto.getOrigin().trim().equalsIgnoreCase("")) {
			routeScheduleDTO.setOrigin(dto.getOrigin());
		}

		if (dto.getDestination() != null && !dto.getDestination().trim().equalsIgnoreCase("")) {
			routeScheduleDTO.setDestination(dto.getDestination());
		}

		if (dto.getGeneratedRefNo() != null && !dto.getGeneratedRefNo().trim().equalsIgnoreCase("")) {
			routeScheduleDTO.setGeneratedRefNo(dto.getGeneratedRefNo());
		}

		disableGroupNo = false;

		origin = routeScheduleDTO.getOrigin();
		destination = routeScheduleDTO.getDestination();

		groupNoList = new ArrayList<>();
		groupNoList = routeScheduleService.getGroupNoList(routeScheduleDTO.getRouteNo(),
				routeScheduleDTO.getGeneratedRefNo());

		tripType = "O";

	}

	/* Swap Origin and Destination */
	public void ajaxSwapOriginDestination() {

		if (routeScheduleDTO.getOrigin() != null && !routeScheduleDTO.getOrigin().trim().equalsIgnoreCase("")
				&& routeScheduleDTO.getDestination() != null
				&& !routeScheduleDTO.getDestination().trim().equalsIgnoreCase("")) {

			if (routeScheduleDTO.isSwapEnds() == true) {

				routeScheduleDTO.setOrigin(destination);
				routeScheduleDTO.setDestination(origin);

				tripType = "D";

			} else {
				routeScheduleDTO.setOrigin(origin);
				routeScheduleDTO.setDestination(destination);
				tripType = "O";
			}

			routeScheduleDTO.setGroupNo(groupNo);

		} else {
			setErrorMessage("Origin / Destination can not be empty");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void searchAction() {

		if (routeScheduleDTO.getRouteNo() != null && !routeScheduleDTO.getRouteNo().trim().equalsIgnoreCase("")) {
			coupleCount = routeScheduleService.getCoupleForRef(routeScheduleDTO.getGeneratedRefNo(), tripType,
					routeScheduleDTO.getGroupNo());
			if (routeScheduleDTO.getBusCategory() != null
					&& !routeScheduleDTO.getBusCategory().trim().equalsIgnoreCase("")) {

				if (routeScheduleDTO.getGroupNo() != null
						&& !routeScheduleDTO.getGroupNo().trim().equalsIgnoreCase("")) {

					if (tripType != null && !tripType.isEmpty() && !tripType.trim().equalsIgnoreCase("")) {
						if (tripType.equalsIgnoreCase("O")) {
							originDataVisible = true;
							destinationDataVisible = false;
						}
						if (tripType.equalsIgnoreCase("D")) {
							destinationDataVisible = true;
							originDataVisible = false;
						}
					}

					boolean isRelatedDataFound = routeScheduleService.isRelatedDataFound(routeScheduleDTO.getRouteNo(),
							routeScheduleDTO.getBusCategory(), routeScheduleDTO.getGeneratedRefNo(),
							routeScheduleDTO.getGroupNo(), tripType);

					/** display data start **/
					int totalBus = routeScheduleService.getTotalBusesForRoute(routeScheduleDTO.getRouteNo(),
							routeScheduleDTO.getGeneratedRefNo());

					int totalTrips = routeScheduleService.getTotalTripsForRoute(routeScheduleDTO.getGeneratedRefNo(),
							tripType, routeScheduleDTO.getGroupNo());
					/** modify by tharushi.e */

					if (totalBus > 0) {
						if (totalTrips > 0) {

							noOfBuses = totalBus;
							noOfTrips = totalTrips;

							if (displayGroupData()) {

								if (displayOriginAndDestinationDetails()) {

									disabledNoramlRotation = false;
									disabledZigzagRotation = false;
									disabledSwap = true;
									disabledClear = false;
									disabledleavePositionList = false;
									RouteScheduleDTO dto = new RouteScheduleDTO();
									dto = routeScheduleService.getLastRouteScheduleData(routeScheduleDTO.getRouteNo(),
											routeScheduleDTO.getBusCategory(), routeScheduleDTO.getGeneratedRefNo(),
											routeScheduleDTO.getGroupNo(), tripType);

									try {
										DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
										if (dto.getStartDateSTR() != null) {
											Date date = formatter.parse(dto.getStartDateSTR());

											routeScheduleDTO.setStartDate(date);
										}

										if (dto.getEndDateSTR() != null) {
											Date date = formatter.parse(dto.getEndDateSTR());

											routeScheduleDTO.setEndDate(date);
											routeScheduleDTO.setLastPanelStartDate(date);
											Calendar lastPanelStartDate = Calendar.getInstance();
											lastPanelStartDate.setTime(date);
											lastPanelStartDate.add(Calendar.DAY_OF_MONTH, 1);
											lastPanelStartDateNew = lastPanelStartDate.getTime();

										}

									} catch (Exception e) {
										e.printStackTrace();
									}

								}
							}

						} else {
							setErrorMessage("Can not continue the flow. No. of trips are zero");
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						}

					} else {
						setErrorMessage("Can not continue the flow. No. of buses  are zero");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

					}
					/** display data end **/

					if (isRelatedDataFound) {
						edit = true;
						disabledNoramlRotation = false;
						disabledZigzagRotation = false;
						lastPanelDateVisible = true;
						RequestContext.getCurrentInstance().execute("PF('comfirmMSG').show()");
					}

				} else {
					setErrorMessage("Please select the group no.");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else {
				setErrorMessage("Please select the bus category");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Please select the route no.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void searchActionNew() {
		rotationNew = true;
		if (routeScheduleDTO.getRouteNo() != null && !routeScheduleDTO.getRouteNo().trim().equalsIgnoreCase("")) {

			if (routeScheduleDTO.getBusCategory() != null
					&& !routeScheduleDTO.getBusCategory().trim().equalsIgnoreCase("")) {

				if (routeScheduleDTO.getGroupNo() != null
						&& !routeScheduleDTO.getGroupNo().trim().equalsIgnoreCase("")) {

					if (tripType != null && !tripType.isEmpty() && !tripType.trim().equalsIgnoreCase("")) {
						if (tripType.equalsIgnoreCase("O")) {
							originDataVisible = true;
							destinationDataVisible = false;
						}
						if (tripType.equalsIgnoreCase("D")) {
							destinationDataVisible = true;
							originDataVisible = false;
						}
					}

					boolean assigned = routeScheduleService.checkBusAssignerDone(routeScheduleDTO.getBusCategory(),
							routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), tripType);

					if (assigned) {
						setErrorMessage("Buses are already assigned to searched data");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						return;
					}

					boolean isRelatedDataFound = routeScheduleService.isRelatedDataFound(routeScheduleDTO.getRouteNo(),
							routeScheduleDTO.getBusCategory(), routeScheduleDTO.getGeneratedRefNo(),
							routeScheduleDTO.getGroupNo(), tripType);

					/** display data start **/
					int totalBus = routeScheduleService.getTotalBusesForRoute(routeScheduleDTO.getRouteNo(),
							routeScheduleDTO.getGeneratedRefNo());

					int totalTrips = routeScheduleService.getTotalTripsForRoute(routeScheduleDTO.getGeneratedRefNo(),
							tripType, routeScheduleDTO.getGroupNo());

					if (isRelatedDataFound) {
						edit = true;
						disabledNoramlRotation = false;
						disabledZigzagRotation = false;
						lastPanelDateVisible = true;

						RequestContext.getCurrentInstance().execute("PF('comfirmMSG').show()");
					}

				} else {
					setErrorMessage("Please select the group no.");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else {
				setErrorMessage("Please select the bus category");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Please select the route no.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public boolean displayGroupData() {

		boolean continueFlow = false;

		int totalBusForGroup = routeScheduleService.getTotalBusForGroup(routeScheduleDTO.getRouteNo(),
				routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo());

		int origin_trips = routeScheduleService.getFixedOrWithoutFixedTripsCount(routeScheduleDTO.getGeneratedRefNo(),
				routeScheduleDTO.getGroupNo(), "O", "N");

		int destination_trips = routeScheduleService.getFixedOrWithoutFixedTripsCount(
				routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), "D", "N");

		int totalTripsForGroup = origin_trips + destination_trips;

		if (totalBusForGroup > 0) {

			if (totalTripsForGroup > 0) {

				int originTotalLeaves = routeScheduleService.getTotalLavesForGroupAndType(
						routeScheduleDTO.getGeneratedRefNo(), "O", routeScheduleDTO.getGroupNo());

				int destinationTotalLeaves = routeScheduleService.getTotalLavesForGroupAndType(
						routeScheduleDTO.getGeneratedRefNo(), "D", routeScheduleDTO.getGroupNo());

				routeScheduleDTO.setNoOfBusesForGroup(totalBusForGroup);
				routeScheduleDTO.setNoOfTripsForGroup(totalTripsForGroup);
				routeScheduleDTO.setNoOfLeavesForGroup(originTotalLeaves + destinationTotalLeaves);

				continueFlow = true;

			} else {
				setErrorMessage(
						"Can not continue the flow. No. of trips (without fixed time) can not be less than zero for group");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Can not continue the flow. No. of PVT buses can not be less than zero for group");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		}

		return continueFlow;

	}

	public boolean displayOriginAndDestinationDetails() {

		boolean continueFlow = false;

		/* Get no of without fixed buses for route/group/type */

		int origin_without_fixed_buses = routeScheduleService.getTotalWithoutFixedBusForGroupAndType(
				routeScheduleDTO.getRouteNo(), routeScheduleDTO.getGeneratedRefNo(), "O",
				routeScheduleDTO.getGroupNo());

		int destination_without_fixed_buses = routeScheduleService.getTotalWithoutFixedBusForGroupAndType(
				routeScheduleDTO.getRouteNo(), routeScheduleDTO.getGeneratedRefNo(), "D",
				routeScheduleDTO.getGroupNo());

		int originTotalLeaves = routeScheduleService.getTotalLavesForGroupAndType(routeScheduleDTO.getGeneratedRefNo(),
				"O", routeScheduleDTO.getGroupNo());

		int destinationTotalLeaves = routeScheduleService
				.getTotalLavesForGroupAndType(routeScheduleDTO.getGeneratedRefNo(), "D", routeScheduleDTO.getGroupNo());

		int origin_totalTrips = routeScheduleService.getFixedOrWithoutFixedTripsCount(
				routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), "O", "N");

		int destination_totalTrips = routeScheduleService.getFixedOrWithoutFixedTripsCount(
				routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), "D", "N");

		if (originTotalLeaves >= 0 && destinationTotalLeaves >= 0) {

			if (origin_totalTrips > 0) {

				if (destination_totalTrips > 0) {

					/* Maximum leave for each side is 9 */
					if (originTotalLeaves <= 9) {

						if (destinationTotalLeaves <= 9) {

							if (tripType.equals("O")) {

								routeScheduleDTO.setNoOfBusesOrigin(origin_without_fixed_buses);
								routeScheduleDTO.setNoOfLeavesOrigin(originTotalLeaves);

								routeScheduleDTO.setNoOfBusesDestination(destination_without_fixed_buses);
								routeScheduleDTO.setNoOfLeavesDestination(destinationTotalLeaves);

								/*
								 * need check again," without_fixed buses mean number of trips "
								 */
								routeScheduleDTO.setNoOfTripsOrigin(origin_totalTrips);
								routeScheduleDTO.setNoOfTripsDestination(destination_totalTrips);

								continueFlow = true;

								/* display leave position select menu */
								selectLeavePositionList = new ArrayList<>();

								for (int i = 1; i <= routeScheduleDTO.getNoOfTripsOrigin(); i++) {
									RouteScheduleDTO dtos = new RouteScheduleDTO(i);
									selectLeavePositionList.add(dtos);
								}

							} else {

								routeScheduleDTO.setNoOfBusesOrigin(origin_without_fixed_buses);
								routeScheduleDTO.setNoOfLeavesOrigin(originTotalLeaves);

								routeScheduleDTO.setNoOfBusesDestination(destination_without_fixed_buses);
								routeScheduleDTO.setNoOfLeavesDestination(destinationTotalLeaves);

								/*
								 * need check again," without_fixed buses mean number of trips "
								 */
								routeScheduleDTO.setNoOfTripsOrigin(origin_totalTrips);
								routeScheduleDTO.setNoOfTripsDestination(destination_totalTrips);

								continueFlow = true;
								/* display leave position select menu */
								selectLeavePositionList = new ArrayList<>();

								for (int i = 1; i <= routeScheduleDTO.getNoOfTripsDestination(); i++) {
									RouteScheduleDTO dtos = new RouteScheduleDTO(i);
									selectLeavePositionList.add(dtos);
								}
							}

						} else {

							setErrorMessage(
									"Can not continue the flow. No. of leaves for destination should be less than nine");
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

						}

					} else {
						setErrorMessage("Can not continue the flow. No. of leaves for origin should be less than nine");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

					}

				} else {
					setErrorMessage("Can not continue the flow. No. of trip for destination can not less than zero");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

				}

			} else {
				setErrorMessage("Can not continue the flow. No. of trip for origin can not less than zero");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

			}

		} else {
			setErrorMessage("Can not continue the flow. Origin and destination leaves can not less than zero");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		}

		return continueFlow;

	}

	public long calculateNoOfDays() {

		Date start_date = routeScheduleDTO.getStartDate();
		Date end_date = routeScheduleDTO.getEndDate();

		long diff = start_date.getTime() - end_date.getTime();
		long days = ((diff / (1000 * 60 * 60 * 24)) - 1);

		return Math.abs(days);
	}

	public long calculateNoOfDaysEdit() {

		Date start_date = routeScheduleDTO.getLastPanelStartDate();
		// Date start_date = lastPanelStartDateNew;
		Date end_date = routeScheduleDTO.getLastPanelEndDate();

		long diff = start_date.getTime() - end_date.getTime();
		long days = ((diff / (1000 * 60 * 60 * 24)));

		return Math.abs(days);
	}

	public long calculateNoOfDaysEditNew() {

		Date start_date = routeScheduleDTO.getLastPanelStartDate();
		Date end_date = routeScheduleDTO.getLastPanelEndDate();

		long diff = start_date.getTime() - end_date.getTime();
		long days = ((diff / (1000 * 60 * 60 * 24)));

		return Math.abs(days);
	}

	public void ajaxEndDateExpireDateValidator() {

		if (routeScheduleDTO.getEndDate() != null && routeScheduleDTO.getStartDate() != null
				&& routeScheduleDTO.getStartDate().after(routeScheduleDTO.getEndDate())) {

			setErrorMessage("End date should be greater than Start date");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

			routeScheduleDTO.setEndDate(null);

		} else if (routeScheduleDTO.getEndDate() != null && routeScheduleDTO.getStartDate() != null) {

			long days = calculateNoOfDays();

			if (days > maximumDays) {

				setErrorMessage("Please select data range less than three month");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		}
	}

	public void ajaxStartDateExpireDateValidator() {

		if (routeScheduleDTO.getEndDate() != null && routeScheduleDTO.getStartDate() != null
				&& routeScheduleDTO.getEndDate().before(routeScheduleDTO.getStartDate())) {

			setErrorMessage("Start date should be less than end date");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

			routeScheduleDTO.setStartDate(null);

		} else if (routeScheduleDTO.getEndDate() != null && routeScheduleDTO.getStartDate() != null) {

			long days = calculateNoOfDays();

			if (days > maximumDays) {

				setErrorMessage("Please select data range less than three month");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		}
	}

	public void clearOne() {

		routeScheduleDTO = new RouteScheduleDTO();
		busRouteList = new ArrayList<RouteScheduleDTO>();
		busCategoryList = new ArrayList<RouteScheduleDTO>();
		busRouteList = routeScheduleService.getRouteNo();
		groupNoList = new ArrayList<>();
		leavePositionList = new ArrayList<>();
		disableBusCategory = true;
		disabledNoramlRotation = true;
		disabledZigzagRotation = true;
		renderTableOne = false;
		renderTableTwo = false;
		disabledgenerateReport = true;
		disabledClear = true;
		disabledSave = true;
		disabledSwap = false;
		busForRouteList = new ArrayList<RouteScheduleDTO>();
		leaveForRouteList = new ArrayList<RouteScheduleDTO>();
		mainSaveList = new ArrayList<RouteScheduleDTO>();
		noOfLeaves = 0;
		noOfBuses = 0;
		noOfTrips = 0;
		noOfDaysFortimeTable = 0;
		noOfTripsForSelectedSide = 0;
		tripID = 0;
		disabledleavePositionList = true;
		rotationType = null;

		edit = false;
		originDataVisible = false;
		destinationDataVisible = false;
		lastPanelDateVisible = false;

		dateList = new ArrayList<String>();
		selectedDateRange = null;
		rotationNew = false;
	}

	public void noramlRotation() {

		if (tripType.equals("O")) {
			normalRotationOrigin();
		} else {
			normalRotationDestination();
		}
	}

	public void normalRotationOrigin() {

		if (!lastPanelDateVisible) {
			if (routeScheduleDTO.getStartDate() != null) {

				if (routeScheduleDTO.getEndDate() != null) {

					noOfDaysFortimeTable = (int) calculateNoOfDays();

					if (routeScheduleDTO.getNoOfLeavesOrigin() == 0) {

						busNoManagerNormal(routeScheduleDTO.getNoOfTripsOrigin());
						createMainDataTable();
						createLeavesDataTable();
						disabledleavePositionList = false;
						disabledZigzagRotation = true;
						rotationType = "N";
						disabledSave = false;

					} else if (routeScheduleDTO.getNoOfLeavesOrigin() != 0) {

						if (!leavePositionList.isEmpty()) {

							if (leavePositionList.size() == routeScheduleDTO.getNoOfLeavesOrigin()) {

								busNoManagerNormal(
										routeScheduleDTO.getNoOfTripsOrigin() + routeScheduleDTO.getNoOfLeavesOrigin());
								createMainDataTable();
								createLeavesDataTable();
								disabledleavePositionList = false;
								disabledZigzagRotation = true;
								rotationType = "N";
								disabledSave = false;

							} else {
								setErrorMessage(
										"Please select only " + routeScheduleDTO.getNoOfLeavesOrigin() + " position");
								RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
							}

						} else {
							setErrorMessage("Please select the leave positions for continue the flow");
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						}

					} else {
						setErrorMessage("Can not continue the flow");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}

				} else {
					setErrorMessage("Please select the end date");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else {
				setErrorMessage("Please select the start date");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {
			// edit route schedule by extending dates

			if (routeScheduleDTO.getLastPanelStartDate() != null) {

				if (routeScheduleDTO.getLastPanelEndDate() != null) {

					// noOfDaysFortimeTable = (int) calculateNoOfDaysEdit();
					noOfDaysFortimeTable = (int) calculateNoOfDaysEditNew();
					if (routeScheduleDTO.getNoOfLeavesOrigin() == 0) {

						busNoManagerNormalEdit(routeScheduleDTO.getNoOfTripsOrigin());
						createMainDataTable();
						createLeavesDataTable();
						disabledleavePositionList = false;
						disabledZigzagRotation = true;
						rotationType = "N";
						disabledSave = false;

					} else if (routeScheduleDTO.getNoOfLeavesOrigin() != 0) {

						/*
						 * busNoManagerNormalEdit( routeScheduleDTO.getNoOfTripsOrigin() +
						 * routeScheduleDTO.getNoOfLeavesOrigin()); createMainDataTable();
						 * createLeavesDataTable(); disabledleavePositionList = false;
						 * disabledZigzagRotation = true; rotationType = "N"; disabledSave = false;
						 */

						/*** ADDED BY THARUSHI.E **/
						busNoManagerNormalEditPrevious(
								routeScheduleDTO.getNoOfTripsOrigin() + routeScheduleDTO.getNoOfLeavesOrigin());
						createMainDataTable();
						createLeavesDataTable();
						disabledleavePositionList = false;
						disabledZigzagRotation = true;
						rotationType = "N";
						disabledSave = false;
						/** END **/

					} else {
						setErrorMessage("Can not continue the flow");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}

				} else {
					setErrorMessage("Please select the last panel date range end date");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else {
				setErrorMessage("Please select the last panel date range start date");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		}

	}

	public void normalRotationDestination() {

		if (!lastPanelDateVisible) {
			if (routeScheduleDTO.getStartDate() != null) {

				if (routeScheduleDTO.getEndDate() != null) {

					noOfDaysFortimeTable = (int) calculateNoOfDays();

					if (routeScheduleDTO.getNoOfLeavesDestination() == 0) {

						busNoManagerNormal(routeScheduleDTO.getNoOfTripsDestination());
						createMainDataTable();
						createLeavesDataTable();
						disabledleavePositionList = false;
						disabledZigzagRotation = true;
						rotationType = "N";
						disabledSave = false;

					} else if (routeScheduleDTO.getNoOfLeavesDestination() != 0) {

						if (!leavePositionList.isEmpty()) {

							if (leavePositionList.size() == routeScheduleDTO.getNoOfLeavesDestination()) {

								busNoManagerNormal(routeScheduleDTO.getNoOfTripsDestination()
										+ routeScheduleDTO.getNoOfLeavesDestination());
								createMainDataTable();
								createLeavesDataTable();
								disabledleavePositionList = false;
								disabledZigzagRotation = true;
								rotationType = "N";
								disabledSave = false;

							} else {
								setErrorMessage(
										"Please select only " + routeScheduleDTO.getNoOfLeavesOrigin() + " position");
								RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
							}

						} else {
							setErrorMessage("Please select the leave positions for continue the flow");
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						}

					} else {
						setErrorMessage("Can not continue the flow");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}

				} else {
					setErrorMessage("Please select the end date");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else {
				setErrorMessage("Please select the start date");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {
			// edit route schedule by extending dates

			if (routeScheduleDTO.getLastPanelStartDate() != null) {

				if (routeScheduleDTO.getLastPanelEndDate() != null) {

					noOfDaysFortimeTable = (int) calculateNoOfDaysEdit();

					if (routeScheduleDTO.getNoOfLeavesDestination() == 0) {

						busNoManagerNormalEdit(routeScheduleDTO.getNoOfTripsDestination());
						createMainDataTable();
						createLeavesDataTable();
						disabledleavePositionList = false;
						disabledZigzagRotation = true;
						rotationType = "N";
						disabledSave = false;

					} else if (routeScheduleDTO.getNoOfLeavesDestination() != 0) {

						leavePositionList = new ArrayList<>(routeScheduleDTO.getNoOfLeavesOrigin());

						busNoManagerNormalEditPrevious(routeScheduleDTO.getNoOfTripsDestination()
								+ routeScheduleDTO.getNoOfLeavesDestination());
						createMainDataTable();
						createLeavesDataTable();
						disabledleavePositionList = false;
						disabledZigzagRotation = true;
						rotationType = "N";
						disabledSave = false;

					} else {
						setErrorMessage("Can not continue the flow");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}

				} else {
					setErrorMessage("Please select the last panel date range end date");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else {
				setErrorMessage("Please select the last panel date range start date");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		}
	}

	public void zigzagRotation() {

		if (tripType.equals("O")) {
			zigzagRotationOrigin();
		} else {
			zigzagRotationDestination();
		}

	}

	public void zigzagRotationOrigin() {
		if (!lastPanelDateVisible) {
			if (routeScheduleDTO.getStartDate() != null) {

				if (routeScheduleDTO.getEndDate() != null) {

					noOfDaysFortimeTable = (int) calculateNoOfDays();

					if (routeScheduleDTO.getNoOfLeavesOrigin() == 0) {

						busNoManagerRandom(routeScheduleDTO.getNoOfTripsOrigin());
						createMainDataTable();
						createLeavesDataTable();
						disabledleavePositionList = false;
						disabledNoramlRotation = true;
						rotationType = "Z";
						disabledSave = false;

					} else if (routeScheduleDTO.getNoOfLeavesOrigin() != 0) {

						if (!leavePositionList.isEmpty()) {

							if (leavePositionList.size() == routeScheduleDTO.getNoOfLeavesOrigin()) {

								busNoManagerRandom(routeScheduleDTO.getNoOfTripsOrigin());
								createMainDataTable();
								createLeavesDataTable();
								disabledleavePositionList = false;
								disabledNoramlRotation = true;
								rotationType = "Z";
								disabledSave = false;

							} else {
								setErrorMessage(
										"Please select only " + routeScheduleDTO.getNoOfLeavesOrigin() + " position");
								RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
							}

						} else {
							setErrorMessage("Please select the leave positions for continue the flow");
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						}

					} else {
						setErrorMessage("Can not continue the flow");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}

				} else {
					setErrorMessage("Please select the end date");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else {
				setErrorMessage("Please select the start date");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {
			// edit
			if (routeScheduleDTO.getStartDate() != null) {

				if (routeScheduleDTO.getEndDate() != null) {

					noOfDaysFortimeTable = (int) calculateNoOfDays();

					if (routeScheduleDTO.getNoOfLeavesOrigin() == 0) {

						busNoManagerRandom(routeScheduleDTO.getNoOfTripsOrigin());
						createMainDataTable();
						createLeavesDataTable();
						disabledleavePositionList = false;
						disabledNoramlRotation = true;
						rotationType = "Z";
						disabledSave = false;

					} else if (routeScheduleDTO.getNoOfLeavesOrigin() != 0) {

						busNoManagerRandom(routeScheduleDTO.getNoOfTripsOrigin());
						createMainDataTable();
						createLeavesDataTable();
						disabledleavePositionList = false;
						disabledNoramlRotation = true;
						rotationType = "Z";
						disabledSave = false;

					} else {
						setErrorMessage("Can not continue the flow");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}

				} else {
					setErrorMessage("Please select the end date");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else {
				setErrorMessage("Please select the start date");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		}
	}

	public void zigzagRotationDestination() {
		if (!lastPanelDateVisible) {
			if (routeScheduleDTO.getStartDate() != null) {

				if (routeScheduleDTO.getEndDate() != null) {

					noOfDaysFortimeTable = (int) calculateNoOfDays();

					if (routeScheduleDTO.getNoOfLeavesDestination() == 0) {

						busNoManagerRandom(routeScheduleDTO.getNoOfTripsDestination());
						createMainDataTable();
						createLeavesDataTable();
						disabledleavePositionList = false;
						disabledNoramlRotation = true;
						rotationType = "Z";
						disabledSave = false;

					} else if (routeScheduleDTO.getNoOfLeavesDestination() != 0) {

						if (!leavePositionList.isEmpty()) {

							if (leavePositionList.size() == routeScheduleDTO.getNoOfLeavesDestination()) {

								busNoManagerRandom(routeScheduleDTO.getNoOfTripsDestination());
								createMainDataTable();
								createLeavesDataTable();
								disabledleavePositionList = false;
								disabledNoramlRotation = true;
								rotationType = "Z";
								disabledSave = false;

							} else {
								setErrorMessage("Please select only " + routeScheduleDTO.getNoOfLeavesDestination()
										+ " position");
								RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
							}

						} else {
							setErrorMessage("Please select the leave positions for continue the flow");
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						}

					} else {
						setErrorMessage("Can not continue the flow");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}

				} else {
					setErrorMessage("Please select the end date");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else {
				setErrorMessage("Please select the start date");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {
			// edit
			if (routeScheduleDTO.getStartDate() != null) {

				if (routeScheduleDTO.getEndDate() != null) {

					noOfDaysFortimeTable = (int) calculateNoOfDays();

					if (routeScheduleDTO.getNoOfLeavesDestination() == 0) {

						busNoManagerRandom(routeScheduleDTO.getNoOfTripsDestination());
						createMainDataTable();
						createLeavesDataTable();
						disabledleavePositionList = false;
						disabledNoramlRotation = true;
						rotationType = "Z";
						disabledSave = false;

					} else if (routeScheduleDTO.getNoOfLeavesDestination() != 0) {

						busNoManagerRandom(routeScheduleDTO.getNoOfTripsDestination());
						createMainDataTable();
						createLeavesDataTable();
						disabledleavePositionList = false;
						disabledNoramlRotation = true;
						rotationType = "Z";
						disabledSave = false;

					} else {
						setErrorMessage("Can not continue the flow");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}

				} else {
					setErrorMessage("Please select the end date");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else {
				setErrorMessage("Please select the start date");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		}
	}

	public void busNoManagerRandom(int noOfTrips) {

		busForRouteList = new ArrayList<>();
		leaveForRouteList = new ArrayList<>();
		tripID = 0;
		int noOfTripsPerSide = 0;

		List<RouteScheduleDTO> busNoList = new ArrayList<RouteScheduleDTO>();
		if (rotationNew) {
			busNoList = routeScheduleService.getBusNoListForRotation(routeScheduleDTO.getRouteNo(),
					routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), tripType,
					routeScheduleDTO.getBusCategory());
			noOfTripsPerSide = routeScheduleService.getNoOfTripsPerSideWithSltb(routeScheduleDTO.getRouteNo(),
					routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), tripType,
					routeScheduleDTO.getBusCategory());

		} else {
			busNoList = routeScheduleService.getBusNoListWithoutCTBBus(routeScheduleDTO.getRouteNo(),
					routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), tripType,
					routeScheduleDTO.getBusCategory());
			noOfTripsPerSide = routeScheduleService.getNoOfTripsPerSide(routeScheduleDTO.getRouteNo(),
					routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), tripType,
					routeScheduleDTO.getBusCategory());

		}

		if (busNoList != null && !busNoList.isEmpty() && busNoList.size() != 0) {
			for (int nooftrips = 0; nooftrips < noOfTripsPerSide; nooftrips++) {

				List<String> stringBusNoList = new ArrayList<>();

				while (stringBusNoList.size() < maximumDays) {

					int count = 0; // Count the number of buses

					for (int a = 0; a < busNoList.size(); a++) {

						if (count < busNoList.size() && stringBusNoList.size() < maximumDays) {
							String busNo = busNoList.get(a).getBusNo();
							stringBusNoList.add(busNo);
							count++;

						} else {
							count = 0;
						}
					}
				}

				List<String> randomList = new ArrayList<String>();
				randomList = randomize(stringBusNoList);

				RouteScheduleDTO dto = setBusToRouteScheduleDTO(randomList);
				busForRouteList.add(dto);
			}
		} else {
			sessionBackingBean.setMessage("No buses for selected data. Displayed opsite side bus rotation.");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
			String tripTypeOpsite =null;
			if(tripType == "O") {
				tripTypeOpsite ="D";
			}
			if(tripType == "D") {
				tripTypeOpsite ="O";
			}
			if (rotationNew) {
				busNoList = routeScheduleService.getBusNoListForRotation(routeScheduleDTO.getRouteNo(),
						routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), tripTypeOpsite,
						routeScheduleDTO.getBusCategory());
				noOfTripsPerSide = routeScheduleService.getNoOfTripsPerSideWithSltb(routeScheduleDTO.getRouteNo(),
						routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), tripTypeOpsite,
						routeScheduleDTO.getBusCategory());

			} else {
				busNoList = routeScheduleService.getBusNoListWithoutCTBBus(routeScheduleDTO.getRouteNo(),
						routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), tripTypeOpsite,
						routeScheduleDTO.getBusCategory());
				noOfTripsPerSide = routeScheduleService.getNoOfTripsPerSide(routeScheduleDTO.getRouteNo(),
						routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), tripTypeOpsite,
						routeScheduleDTO.getBusCategory());

			}
			
			if (busNoList != null && !busNoList.isEmpty() && busNoList.size() != 0) {
				for (int nooftrips = 0; nooftrips < noOfTripsPerSide; nooftrips++) {

					List<String> stringBusNoList = new ArrayList<>();

					while (stringBusNoList.size() < maximumDays) {

						int count = 0; // Count the number of buses

						for (int a = 0; a < busNoList.size(); a++) {

							if (count < busNoList.size() && stringBusNoList.size() < maximumDays) {
								String busNo = busNoList.get(a).getBusNo();
								stringBusNoList.add(busNo);
								count++;

							} else {
								count = 0;
							}
						}
					}

					List<String> randomList = new ArrayList<String>();
					randomList = randomize(stringBusNoList);

					RouteScheduleDTO dto = setBusToRouteScheduleDTO(randomList);
					busForRouteList.add(dto);
				}
			}
			
		}

		mainSaveList.addAll(busForRouteList);

		if (!busForRouteList.isEmpty()) {

			List<RouteScheduleDTO> removingListForLeaves = new ArrayList<>();

			/*
			 * Store leaves position array list object in removingListForLeaves
			 */
			for (int c = 0; c < leavePositionList.size(); c++) {

				int e = Integer.valueOf(leavePositionList.get(c));
				int position = e - 1;
				RouteScheduleDTO dto = busForRouteList.get(position);
				removingListForLeaves.add(dto);
			}
			/*
			 * remove above DTO from busForRouteList and add to leaveForRouteList
			 */

			leaveForRouteList.addAll(removingListForLeaves);
			renderTableOne = true;

			if (!leaveForRouteList.isEmpty()) {
				renderTableTwo = true;
			}

		} else {
			setErrorMessage("Data not found");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public List<String> randomize(List<String> stringBusNoList) {
		Random r = new Random();

		for (int i = stringBusNoList.size() - 1; i > 0; i--) {

			// Pick a random index from 0 to i
			int j = r.nextInt(i);

			// Swap arr[i] with the element at random index
			if (!stringBusNoList.get(i).equals(stringBusNoList.get(j))) {
				String temp = stringBusNoList.get(i);
				stringBusNoList.set(i, stringBusNoList.get(j));
				stringBusNoList.set(j, temp);
			}

		}

		for (int i = stringBusNoList.size() - 1; i > 0; i--) {

			// Pick a random index from 0 to i
			int j = r.nextInt(i);

			// Swap arr[i] with the element at random index
			if (!stringBusNoList.get(i).equals(stringBusNoList.get(j))) {
				String temp = stringBusNoList.get(i);
				stringBusNoList.set(i, stringBusNoList.get(j));
				stringBusNoList.set(j, temp);
			}

		}

		return stringBusNoList;
	}

	public void busNoManagerNormal(int noOfTrips) {

		busForRouteList = new ArrayList<>();
		leaveForRouteList = new ArrayList<>();
		tripID = 0;
		boolean first = false;
		int noOfTripsPerSide = 0; /** added by tharushi.e **/
		List<RouteScheduleDTO> busNoList = new ArrayList<RouteScheduleDTO>();
		boolean orginDestiLeave = false;

		orginDestiLeave = routeScheduleService.checkLeaveInTimeTableDet(routeScheduleDTO.getGeneratedRefNo(),
				routeScheduleDTO.getGroupNo(), tripType);
		if (rotationNew) {

			if (coupleCount.equals("2") && orginDestiLeave) {
				busNoList = routeScheduleService.getBusNoListForRotationWithCoupleTwo(routeScheduleDTO.getRouteNo(),
						routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), tripType,
						routeScheduleDTO.getBusCategory());
			} else {
				busNoList = routeScheduleService.getBusNoListForRotation(routeScheduleDTO.getRouteNo(),
						routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), tripType,
						routeScheduleDTO.getBusCategory());

			}
			noOfTripsPerSide = routeScheduleService.getNoOfTripsPerSideWithSltb(routeScheduleDTO.getRouteNo(),
					routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), tripType,
					routeScheduleDTO.getBusCategory());
		} else {

			if (coupleCount.equals("2") && orginDestiLeave) {
				busNoList = routeScheduleService.getBusNoListWithoutCTBBusWithCoupleTwo(routeScheduleDTO.getRouteNo(),
						routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), tripType,
						routeScheduleDTO.getBusCategory());
			} else {
				busNoList = routeScheduleService.getBusNoListWithoutCTBBus(routeScheduleDTO.getRouteNo(),
						routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), tripType,
						routeScheduleDTO.getBusCategory());
			}
			noOfTripsPerSide = routeScheduleService.getNoOfTripsPerSide(routeScheduleDTO.getRouteNo(),
					routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), tripType,
					routeScheduleDTO.getBusCategory());
		}

		if (busNoList != null && !busNoList.isEmpty() && busNoList.size() != 0) {

			for (int nooftrips = 0; nooftrips < noOfTripsPerSide; nooftrips++) {

				List<String> stringBusNoList = new ArrayList<>();

				List<RouteScheduleDTO> tempBusNoList = new ArrayList<RouteScheduleDTO>();
				for (int i = 0; i < busNoList.size(); i++) {

					if (!first) {

						break;
					} else {
						if (i == 0) {
							tempBusNoList.add(busNoList.get(busNoList.size() - 1));
						} else if (i > 0) {
							tempBusNoList.add(busNoList.get(i - 1));
						}
					}
				}
				first = true;
				if (tempBusNoList != null && !tempBusNoList.isEmpty() && tempBusNoList.size() != 0) {
					busNoList = tempBusNoList;
				}

				while (stringBusNoList.size() < maximumDays) {

					int count = 0;

					for (int a = 0; a < busNoList.size(); a++) {

						if (count < busNoList.size() && stringBusNoList.size() < maximumDays) {
							String busNo = busNoList.get(a).getBusNo();
							stringBusNoList.add(busNo);
							count++;

						} else {
							count = 0;
						}
					}
				}

				RouteScheduleDTO dto = setBusToRouteScheduleDTO(stringBusNoList);
				busForRouteList.add(dto);

			}
		} else {
			sessionBackingBean.setMessage("No buses for selected data. Displayed opsite side bus rotation.");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
			String tripTypeOpsite =null;
			if(tripType == "O") {
				tripTypeOpsite ="D";
			}
			if(tripType == "D") {
				tripTypeOpsite ="O";
			}
			if (rotationNew) {

				if (coupleCount.equals("2") && orginDestiLeave) {
					busNoList = routeScheduleService.getBusNoListForRotationWithCoupleTwo(routeScheduleDTO.getRouteNo(),
							routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), tripTypeOpsite,
							routeScheduleDTO.getBusCategory());
				} else {
					busNoList = routeScheduleService.getBusNoListForRotation(routeScheduleDTO.getRouteNo(),
							routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), tripTypeOpsite,
							routeScheduleDTO.getBusCategory());

				}
				noOfTripsPerSide = routeScheduleService.getNoOfTripsPerSideWithSltb(routeScheduleDTO.getRouteNo(),
						routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), tripTypeOpsite,
						routeScheduleDTO.getBusCategory());
			} else {

				if (coupleCount.equals("2") && orginDestiLeave) {
					busNoList = routeScheduleService.getBusNoListWithoutCTBBusWithCoupleTwo(routeScheduleDTO.getRouteNo(),
							routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), tripTypeOpsite,
							routeScheduleDTO.getBusCategory());
				} else {
					busNoList = routeScheduleService.getBusNoListWithoutCTBBus(routeScheduleDTO.getRouteNo(),
							routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), tripTypeOpsite,
							routeScheduleDTO.getBusCategory());
				}
				noOfTripsPerSide = routeScheduleService.getNoOfTripsPerSide(routeScheduleDTO.getRouteNo(),
						routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), tripTypeOpsite,
						routeScheduleDTO.getBusCategory());
			}

			if (busNoList != null && !busNoList.isEmpty() && busNoList.size() != 0) {

				for (int nooftrips = 0; nooftrips < noOfTripsPerSide; nooftrips++) {

					List<String> stringBusNoList = new ArrayList<>();

					List<RouteScheduleDTO> tempBusNoList = new ArrayList<RouteScheduleDTO>();
					for (int i = 0; i < busNoList.size(); i++) {

						if (!first) {

							break;
						} else {
							if (i == 0) {
								tempBusNoList.add(busNoList.get(busNoList.size() - 1));
							} else if (i > 0) {
								tempBusNoList.add(busNoList.get(i - 1));
							}
						}
					}
					first = true;
					if (tempBusNoList != null && !tempBusNoList.isEmpty() && tempBusNoList.size() != 0) {
						busNoList = tempBusNoList;
					}

					while (stringBusNoList.size() < maximumDays) {

						int count = 0;

						for (int a = 0; a < busNoList.size(); a++) {

							if (count < busNoList.size() && stringBusNoList.size() < maximumDays) {
								String busNo = busNoList.get(a).getBusNo();
								stringBusNoList.add(busNo);
								count++;

							} else {
								count = 0;
							}
						}
					}

					RouteScheduleDTO dto = setBusToRouteScheduleDTO(stringBusNoList);
					busForRouteList.add(dto);

				}
			}
		}

		mainSaveList.addAll(busForRouteList);

		if (!busForRouteList.isEmpty()) {

			List<RouteScheduleDTO> removingListForLeaves = new ArrayList<>();

			for (int c = 0; c < leavePositionList.size(); c++) {

				int e = Integer.valueOf(leavePositionList.get(c));
				int position = e - 1;
				RouteScheduleDTO dto = busForRouteList.get(position);
				removingListForLeaves.add(dto);
			}

			leaveForRouteList.addAll(removingListForLeaves);

			renderTableOne = true;

			if (!leaveForRouteList.isEmpty()) {
				renderTableTwo = true;
			}

		} else {
			setErrorMessage("Data not found");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void busNoManagerNormalEditPrevious(int noOfTrips) {

		busForRouteList = new ArrayList<>();
		leaveForRouteList = new ArrayList<>();
		tripID = 0;
		boolean first = false;
		int noOfTripsPerSide = 0; /** added by tharushi.e **/
		List<RouteScheduleDTO> busNoList = new ArrayList<RouteScheduleDTO>();
		/*
		 * List<RouteScheduleDTO> busNoList =
		 * routeScheduleService.getBusNoListSecondTime(routeScheduleDTO.getRouteNo(),
		 * routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(),
		 * tripType, routeScheduleDTO.getBusCategory());
		 */
		boolean orginDestiLeave = false;

		orginDestiLeave = routeScheduleService.checkLeaveInTimeTableDet(routeScheduleDTO.getGeneratedRefNo(),
				routeScheduleDTO.getGroupNo(), tripType);

		if (coupleCount.equals("2") && orginDestiLeave) {
			busNoList = routeScheduleService.getBusNoListForRotationWithCoupleTwoNew(routeScheduleDTO.getRouteNo(),
					routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), tripType,
					routeScheduleDTO.getBusCategory());
		} else {
			busNoList = routeScheduleService.getBusNoListForRotation(routeScheduleDTO.getRouteNo(),
					routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), tripType,
					routeScheduleDTO.getBusCategory());

		}
		noOfTripsPerSide = routeScheduleService.getNoOfTripsPerSide(routeScheduleDTO.getRouteNo(),
				routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), tripType,
				routeScheduleDTO.getBusCategory());

		/** get last bus number from previous saved data start **/
		String lastBusNo = routeScheduleService.retrieveLastBusNumOfRouteSchedule(routeScheduleDTO.getRouteNo(),
				routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), tripType,
				routeScheduleDTO.getBusCategory());

		/** get last bus number from previous saved data end **/

		/**
		 * re create bus number according to last bus number from previous route
		 * schedule generator start
		 **/
		boolean found = false;
		List<RouteScheduleDTO> firstPartList = new ArrayList<RouteScheduleDTO>();
		List<RouteScheduleDTO> secondPartList = new ArrayList<RouteScheduleDTO>();
		for (int i = 0; i < (busNoList.size()); i++) {
			if (busNoList.get(i).getBusNo().equals(lastBusNo)) {
				secondPartList.add(busNoList.get(i));
				found = true;
			} else {
				if (found) {
					firstPartList.add(busNoList.get(i));
				} else {
					secondPartList.add(busNoList.get(i));
				}
			}
		}

		busNoList = new ArrayList<RouteScheduleDTO>();

		busNoList.addAll(firstPartList);
		busNoList.addAll(secondPartList);
		/**
		 * re create bus number according to last bus number from previous route
		 * schedule generator end
		 **/

		// need to check noOfTripsForSelectedSide
		for (int nooftrips = 0; nooftrips < noOfTripsPerSide; nooftrips++) {

			List<String> stringBusNoList = new ArrayList<>();

			/*
			 * Rotate normally. Get the first number of list and send it to last index. then
			 * last index come to before the last one. likewise all index move
			 */
			List<RouteScheduleDTO> tempBusNoList = new ArrayList<RouteScheduleDTO>();
			for (int i = 0; i < busNoList.size(); i++) {

				if (!first) {

					break;
				} else {
					if (i == 0) {
						tempBusNoList.add(busNoList.get(busNoList.size() - 1));
					} else if (i > 0) {
						tempBusNoList.add(busNoList.get(i - 1));
					}
				}
			}
			first = true;
			if (tempBusNoList != null && !tempBusNoList.isEmpty() && tempBusNoList.size() != 0) {
				busNoList = tempBusNoList;
			}

			while (stringBusNoList.size() < maximumDays) {

				int count = 0; // Count the number of buses

				for (int a = 0; a < busNoList.size(); a++) {

					if (count < busNoList.size() && stringBusNoList.size() < maximumDays) {
						String busNo = busNoList.get(a).getBusNo();
						stringBusNoList.add(busNo);
						count++;

					} else {
						count = 0;
					}
				}
			}

			RouteScheduleDTO dto = setBusToRouteScheduleDTO(stringBusNoList);
			busForRouteList.add(dto);

		}

		mainSaveList.addAll(busForRouteList);

		if (!busForRouteList.isEmpty()) {

			List<RouteScheduleDTO> removingListForLeaves = new ArrayList<>();

			/*
			 * Store leaves position array list object in removingListForLeaves
			 */

			leavePositionList = routeScheduleService.getLeaveListPerSide(routeScheduleDTO.getGeneratedRefNo(),
					tripType);
			for (int c = 0; c < leavePositionList.size(); c++) {

				int e = Integer.valueOf(leavePositionList.get(c));
				int position = e - 1;
				RouteScheduleDTO dto = busForRouteList.get(position);
				removingListForLeaves.add(dto);
			}
			/*
			 * remove above DTO from busForRouteList and add to leaveForRouteList
			 */
			// busForRouteList.removeAll(removingListForLeaves);
			leaveForRouteList.addAll(removingListForLeaves);

			renderTableOne = true;

			if (!leaveForRouteList.isEmpty()) {
				renderTableTwo = true;
			}

		} else {
			setErrorMessage("Data not found");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void busNoManagerNormalEdit(int noOfTrips) {

		busForRouteList = new ArrayList<>();
		leaveForRouteList = new ArrayList<>();
		tripID = 0;
		boolean first = false;
		int noOfTripsPerSide = 0; /** added by tharushi.e **/
		List<RouteScheduleDTO> busNoList = new ArrayList<RouteScheduleDTO>();
		boolean orginDestiLeave = false;

		orginDestiLeave = routeScheduleService.checkLeaveInTimeTableDet(routeScheduleDTO.getGeneratedRefNo(),
				routeScheduleDTO.getGroupNo(), tripType);

		if (coupleCount.equals("2") && orginDestiLeave) {
			busNoList = routeScheduleService.getBusNoListWithoutCTBBusWithCoupleTwo(routeScheduleDTO.getRouteNo(),
					routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), tripType,
					routeScheduleDTO.getBusCategory());
		} else {
			busNoList = routeScheduleService.getBusNoListWithoutCTBBus(routeScheduleDTO.getRouteNo(),
					routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), tripType,
					routeScheduleDTO.getBusCategory());
		}
		noOfTripsPerSide = routeScheduleService.getNoOfTripsPerSide(routeScheduleDTO.getRouteNo(),
				routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), tripType,
				routeScheduleDTO.getBusCategory());

		/** get last bus number from previous saved data start **/
		String lastBusNo = routeScheduleService.retrieveLastBusNumOfRouteSchedule(routeScheduleDTO.getRouteNo(),
				routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), tripType,
				routeScheduleDTO.getBusCategory());

		/** get last bus number from previous saved data end **/

		/**
		 * re create bus number according to last bus number from previous route
		 * schedule generator start
		 **/

		/**
		 * for (int i = 0; i < (busNoList.size() - 1); i++) { if
		 * (busNoList.get(i).getBusNo().equals(lastBusNo)) { busNoList.add(0,
		 * busNoList.get(i)); break;
		 * 
		 * }
		 * 
		 * } for (int i = 0; i < (busNoList.size()); i++) { if
		 * (busNoList.get(i).getBusNo().equals(lastBusNo) && busNoList.indexOf(i) != -1)
		 * { busNoList.remove(busNoList.get(i));
		 * 
		 * }
		 * 
		 * }
		 **/

		boolean found = false;
		List<RouteScheduleDTO> firstPartList = new ArrayList<RouteScheduleDTO>();
		List<RouteScheduleDTO> secondPartList = new ArrayList<RouteScheduleDTO>();
		for (int i = 0; i < (busNoList.size()); i++) {
			if (busNoList.get(i).getBusNo().equals(lastBusNo)) {
				secondPartList.add(busNoList.get(i));
				found = true;
			} else {
				if (found) {
					firstPartList.add(busNoList.get(i));
				} else {
					secondPartList.add(busNoList.get(i));
				}
			}
		}

		busNoList = new ArrayList<RouteScheduleDTO>();

		busNoList.addAll(firstPartList);
		busNoList.addAll(secondPartList);
		/**
		 * re create bus number according to last bus number from previous route
		 * schedule generator end
		 **/
		/**
		 * re create bus number according to last bus number from previous route
		 * schedule generator end
		 **/

		if (busNoList != null && !busNoList.isEmpty() && busNoList.size() != 0) {
			// need to check noOfTripsForSelectedSide
			/* for (int nooftrips = 0; nooftrips < noOfTrips; nooftrips++) { */ /**
																				 * commented by tharushi.e
																				 **/
			for (int nooftrips = 0; nooftrips < noOfTripsPerSide; nooftrips++) {

				List<String> stringBusNoList = new ArrayList<>();

				/*
				 * Rotate normally. Get the first number of list and send it to last index. then
				 * last index come to before the last one. likewise all index move
				 */
				List<RouteScheduleDTO> tempBusNoList = new ArrayList<RouteScheduleDTO>();
				for (int i = 0; i < busNoList.size(); i++) {

					if (!first) {

						break;
					} else {
						if (i == 0) {
							tempBusNoList.add(busNoList.get(busNoList.size() - 1));
						} else if (i > 0) {
							tempBusNoList.add(busNoList.get(i - 1));
						}
					}
				}
				first = true;
				if (tempBusNoList != null && !tempBusNoList.isEmpty() && tempBusNoList.size() != 0) {
					busNoList = tempBusNoList;
				}

				while (stringBusNoList.size() < maximumDays) {

					int count = 0;

					for (int a = 0; a < busNoList.size(); a++) {

						if (count < busNoList.size() && stringBusNoList.size() < maximumDays) {
							String busNo = busNoList.get(a).getBusNo();
							stringBusNoList.add(busNo);
							count++;

						} else {
							count = 0;
						}
					}
				}

				RouteScheduleDTO dto = setBusToRouteScheduleDTO(stringBusNoList);
				busForRouteList.add(dto);

			}
		} else {
			sessionBackingBean.setMessage("There are no buses for selected data");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
			return;
		}

		mainSaveList.addAll(busForRouteList);

		if (!busForRouteList.isEmpty()) {

			List<RouteScheduleDTO> removingListForLeaves = new ArrayList<>();

			/*
			 * Store leaves position array list object in removingListForLeaves
			 */
			for (int c = 0; c < leavePositionList.size(); c++) {

				int e = Integer.valueOf(leavePositionList.get(c));
				int position = e - 1;
				RouteScheduleDTO dto = busForRouteList.get(position);
				removingListForLeaves.add(dto);
			}
			/*
			 * remove above DTO from busForRouteList and add to leaveForRouteList
			 */
			// busForRouteList.removeAll(removingListForLeaves);
			leaveForRouteList.addAll(removingListForLeaves);

			renderTableOne = true;

			if (!leaveForRouteList.isEmpty()) {
				renderTableTwo = true;
			}

		} else {
			setErrorMessage("Data not found");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public RouteScheduleDTO setBusToRouteScheduleDTO(List<String> stringBusNoList) {

		/*
		 * In this method, Add values to data table columns, stringBusNoList gives bus
		 * numbers for 92 days. add those values to according days and return the DTO
		 */

		int trip_id = ++this.tripID;

		String id = String.valueOf(trip_id);

		RouteScheduleDTO busNoDTO = new RouteScheduleDTO();

		if (stringBusNoList.size() == maximumDays) {

			busNoDTO.setTripId(id);
			busNoDTO.setBusNo1(stringBusNoList.get(0));
			busNoDTO.setBusNo2(stringBusNoList.get(1));
			busNoDTO.setBusNo3(stringBusNoList.get(2));
			busNoDTO.setBusNo4(stringBusNoList.get(3));
			busNoDTO.setBusNo5(stringBusNoList.get(4));
			busNoDTO.setBusNo6(stringBusNoList.get(5));
			busNoDTO.setBusNo7(stringBusNoList.get(6));
			busNoDTO.setBusNo8(stringBusNoList.get(7));
			busNoDTO.setBusNo9(stringBusNoList.get(8));
			busNoDTO.setBusNo10(stringBusNoList.get(9));
			busNoDTO.setBusNo11(stringBusNoList.get(10));
			busNoDTO.setBusNo12(stringBusNoList.get(11));
			busNoDTO.setBusNo13(stringBusNoList.get(12));
			busNoDTO.setBusNo14(stringBusNoList.get(13));
			busNoDTO.setBusNo15(stringBusNoList.get(14));
			busNoDTO.setBusNo16(stringBusNoList.get(15));
			busNoDTO.setBusNo17(stringBusNoList.get(16));
			busNoDTO.setBusNo18(stringBusNoList.get(17));
			busNoDTO.setBusNo19(stringBusNoList.get(18));
			busNoDTO.setBusNo20(stringBusNoList.get(19));
			busNoDTO.setBusNo21(stringBusNoList.get(20));
			busNoDTO.setBusNo22(stringBusNoList.get(21));
			busNoDTO.setBusNo23(stringBusNoList.get(22));
			busNoDTO.setBusNo24(stringBusNoList.get(23));
			busNoDTO.setBusNo25(stringBusNoList.get(24));
			busNoDTO.setBusNo26(stringBusNoList.get(25));
			busNoDTO.setBusNo27(stringBusNoList.get(26));
			busNoDTO.setBusNo28(stringBusNoList.get(27));
			busNoDTO.setBusNo29(stringBusNoList.get(28));
			busNoDTO.setBusNo30(stringBusNoList.get(29));
			busNoDTO.setBusNo31(stringBusNoList.get(30));
			busNoDTO.setBusNo32(stringBusNoList.get(31));
			busNoDTO.setBusNo33(stringBusNoList.get(32));
			busNoDTO.setBusNo34(stringBusNoList.get(33));
			busNoDTO.setBusNo35(stringBusNoList.get(34));
			busNoDTO.setBusNo36(stringBusNoList.get(35));
			busNoDTO.setBusNo37(stringBusNoList.get(36));
			busNoDTO.setBusNo38(stringBusNoList.get(37));
			busNoDTO.setBusNo39(stringBusNoList.get(38));
			busNoDTO.setBusNo40(stringBusNoList.get(39));
			busNoDTO.setBusNo41(stringBusNoList.get(40));
			busNoDTO.setBusNo42(stringBusNoList.get(41));
			busNoDTO.setBusNo43(stringBusNoList.get(42));
			busNoDTO.setBusNo44(stringBusNoList.get(43));
			busNoDTO.setBusNo45(stringBusNoList.get(44));
			busNoDTO.setBusNo46(stringBusNoList.get(45));
			busNoDTO.setBusNo47(stringBusNoList.get(46));
			busNoDTO.setBusNo48(stringBusNoList.get(47));
			busNoDTO.setBusNo49(stringBusNoList.get(48));
			busNoDTO.setBusNo50(stringBusNoList.get(49));
			busNoDTO.setBusNo51(stringBusNoList.get(50));
			busNoDTO.setBusNo52(stringBusNoList.get(51));
			busNoDTO.setBusNo53(stringBusNoList.get(52));
			busNoDTO.setBusNo54(stringBusNoList.get(53));
			busNoDTO.setBusNo55(stringBusNoList.get(54));
			busNoDTO.setBusNo56(stringBusNoList.get(55));
			busNoDTO.setBusNo57(stringBusNoList.get(56));
			busNoDTO.setBusNo58(stringBusNoList.get(57));
			busNoDTO.setBusNo59(stringBusNoList.get(58));
			busNoDTO.setBusNo60(stringBusNoList.get(59));
			busNoDTO.setBusNo61(stringBusNoList.get(60));
			busNoDTO.setBusNo62(stringBusNoList.get(61));
			busNoDTO.setBusNo63(stringBusNoList.get(62));
			busNoDTO.setBusNo64(stringBusNoList.get(63));
			busNoDTO.setBusNo65(stringBusNoList.get(64));
			busNoDTO.setBusNo66(stringBusNoList.get(65));
			busNoDTO.setBusNo67(stringBusNoList.get(66));
			busNoDTO.setBusNo68(stringBusNoList.get(67));
			busNoDTO.setBusNo69(stringBusNoList.get(68));
			busNoDTO.setBusNo70(stringBusNoList.get(69));
			busNoDTO.setBusNo71(stringBusNoList.get(70));
			busNoDTO.setBusNo72(stringBusNoList.get(71));
			busNoDTO.setBusNo73(stringBusNoList.get(72));
			busNoDTO.setBusNo74(stringBusNoList.get(73));
			busNoDTO.setBusNo75(stringBusNoList.get(74));
			busNoDTO.setBusNo76(stringBusNoList.get(75));
			busNoDTO.setBusNo77(stringBusNoList.get(76));
			busNoDTO.setBusNo78(stringBusNoList.get(77));
			busNoDTO.setBusNo79(stringBusNoList.get(78));
			busNoDTO.setBusNo80(stringBusNoList.get(79));
			busNoDTO.setBusNo81(stringBusNoList.get(80));
			busNoDTO.setBusNo82(stringBusNoList.get(81));
			busNoDTO.setBusNo83(stringBusNoList.get(82));
			busNoDTO.setBusNo84(stringBusNoList.get(83));
			busNoDTO.setBusNo85(stringBusNoList.get(84));
			busNoDTO.setBusNo86(stringBusNoList.get(85));
			busNoDTO.setBusNo87(stringBusNoList.get(86));
			busNoDTO.setBusNo88(stringBusNoList.get(87));
			busNoDTO.setBusNo89(stringBusNoList.get(88));
			busNoDTO.setBusNo90(stringBusNoList.get(89));
			busNoDTO.setBusNo91(stringBusNoList.get(90));
			busNoDTO.setBusNo92(stringBusNoList.get(91));

		} else {
			setErrorMessage("Can not continue the flow. Buses found only for " + stringBusNoList.size() + " days");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

		return busNoDTO;
	}

	public List<RouteScheduleDTO> saveListManager() {

		List<RouteScheduleDTO> saveList = new ArrayList<>();

		if (!mainSaveList.isEmpty()) {
			int days = 0;
			if (!lastPanelDateVisible) {
				days = (int) calculateNoOfDays();
			} else {
				days = (int) calculateNoOfDaysEdit();
			}

			for (int i = 0; i < mainSaveList.size(); i++) {

				List<RouteScheduleDTO> temporaryList = new ArrayList<>();

				RouteScheduleDTO dto1 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo1());
				temporaryList.add(dto1);
				RouteScheduleDTO dto2 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo2());
				temporaryList.add(dto2);
				RouteScheduleDTO dto3 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo3());
				temporaryList.add(dto3);
				RouteScheduleDTO dto4 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo4());
				temporaryList.add(dto4);
				RouteScheduleDTO dto5 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo5());
				temporaryList.add(dto5);
				RouteScheduleDTO dto6 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo6());
				temporaryList.add(dto6);
				RouteScheduleDTO dto7 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo7());
				temporaryList.add(dto7);
				RouteScheduleDTO dto8 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo8());
				temporaryList.add(dto8);
				RouteScheduleDTO dto9 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo9());
				temporaryList.add(dto9);
				RouteScheduleDTO dto10 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo10());
				temporaryList.add(dto10);
				RouteScheduleDTO dto11 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo11());
				temporaryList.add(dto11);
				RouteScheduleDTO dto12 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo12());
				temporaryList.add(dto12);
				RouteScheduleDTO dto13 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo13());
				temporaryList.add(dto13);
				RouteScheduleDTO dto14 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo14());
				temporaryList.add(dto14);
				RouteScheduleDTO dto15 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo15());
				temporaryList.add(dto15);
				RouteScheduleDTO dto16 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo16());
				temporaryList.add(dto16);
				RouteScheduleDTO dto17 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo17());
				temporaryList.add(dto17);
				RouteScheduleDTO dto18 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo18());
				temporaryList.add(dto18);
				RouteScheduleDTO dto19 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo19());
				temporaryList.add(dto19);
				RouteScheduleDTO dto20 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo20());
				temporaryList.add(dto20);
				RouteScheduleDTO dto21 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo21());
				temporaryList.add(dto21);
				RouteScheduleDTO dto22 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo22());
				temporaryList.add(dto22);
				RouteScheduleDTO dto23 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo23());
				temporaryList.add(dto23);
				RouteScheduleDTO dto24 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo24());
				temporaryList.add(dto24);
				RouteScheduleDTO dto25 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo25());
				temporaryList.add(dto25);
				RouteScheduleDTO dto26 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo26());
				temporaryList.add(dto26);
				RouteScheduleDTO dto27 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo27());
				temporaryList.add(dto27);
				RouteScheduleDTO dto28 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo28());
				temporaryList.add(dto28);
				RouteScheduleDTO dto29 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo29());
				temporaryList.add(dto29);
				RouteScheduleDTO dto30 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo30());
				temporaryList.add(dto30);
				RouteScheduleDTO dto31 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo31());
				temporaryList.add(dto31);
				RouteScheduleDTO dto32 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo32());
				temporaryList.add(dto32);
				RouteScheduleDTO dto33 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo33());
				temporaryList.add(dto33);
				RouteScheduleDTO dto34 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo34());
				temporaryList.add(dto34);
				RouteScheduleDTO dto35 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo35());
				temporaryList.add(dto35);
				RouteScheduleDTO dto36 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo36());
				temporaryList.add(dto36);
				RouteScheduleDTO dto37 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo37());
				temporaryList.add(dto37);
				RouteScheduleDTO dto38 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo38());
				temporaryList.add(dto38);
				RouteScheduleDTO dto39 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo39());
				temporaryList.add(dto39);
				RouteScheduleDTO dto40 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo40());
				temporaryList.add(dto40);
				RouteScheduleDTO dto41 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo41());
				temporaryList.add(dto41);
				RouteScheduleDTO dto42 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo42());
				temporaryList.add(dto42);
				RouteScheduleDTO dto43 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo43());
				temporaryList.add(dto43);
				RouteScheduleDTO dto44 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo44());
				temporaryList.add(dto44);
				RouteScheduleDTO dto45 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo45());
				temporaryList.add(dto45);
				RouteScheduleDTO dto46 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo46());
				temporaryList.add(dto46);
				RouteScheduleDTO dto47 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo47());
				temporaryList.add(dto47);
				RouteScheduleDTO dto48 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo48());
				temporaryList.add(dto48);
				RouteScheduleDTO dto49 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo49());
				temporaryList.add(dto49);
				RouteScheduleDTO dto50 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo50());
				temporaryList.add(dto50);
				RouteScheduleDTO dto51 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo51());
				temporaryList.add(dto51);
				RouteScheduleDTO dto52 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo52());
				temporaryList.add(dto52);
				RouteScheduleDTO dto53 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo53());
				temporaryList.add(dto53);
				RouteScheduleDTO dto54 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo54());
				temporaryList.add(dto54);
				RouteScheduleDTO dto55 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo55());
				temporaryList.add(dto55);
				RouteScheduleDTO dto56 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo56());
				temporaryList.add(dto56);
				RouteScheduleDTO dto57 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo57());
				temporaryList.add(dto57);
				RouteScheduleDTO dto58 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo58());
				temporaryList.add(dto58);
				RouteScheduleDTO dto59 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo59());
				temporaryList.add(dto59);
				RouteScheduleDTO dto60 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo60());
				temporaryList.add(dto60);
				RouteScheduleDTO dto61 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo61());
				temporaryList.add(dto61);
				RouteScheduleDTO dto62 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo62());
				temporaryList.add(dto62);
				RouteScheduleDTO dto63 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo63());
				temporaryList.add(dto63);
				RouteScheduleDTO dto64 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo64());
				temporaryList.add(dto64);
				RouteScheduleDTO dto65 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo65());
				temporaryList.add(dto65);
				RouteScheduleDTO dto66 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo66());
				temporaryList.add(dto66);
				RouteScheduleDTO dto67 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo67());
				temporaryList.add(dto67);
				RouteScheduleDTO dto68 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo68());
				temporaryList.add(dto68);
				RouteScheduleDTO dto69 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo69());
				temporaryList.add(dto69);
				RouteScheduleDTO dto70 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo70());
				temporaryList.add(dto70);
				RouteScheduleDTO dto71 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo71());
				temporaryList.add(dto71);
				RouteScheduleDTO dto72 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo72());
				temporaryList.add(dto72);
				RouteScheduleDTO dto73 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo73());
				temporaryList.add(dto73);
				RouteScheduleDTO dto74 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo74());
				temporaryList.add(dto74);
				RouteScheduleDTO dto75 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo75());
				temporaryList.add(dto75);
				RouteScheduleDTO dto76 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo76());
				temporaryList.add(dto76);
				RouteScheduleDTO dto77 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo77());
				temporaryList.add(dto77);
				RouteScheduleDTO dto78 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo78());
				temporaryList.add(dto78);
				RouteScheduleDTO dto79 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo79());
				temporaryList.add(dto79);
				RouteScheduleDTO dto80 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo80());
				temporaryList.add(dto80);
				RouteScheduleDTO dto81 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo81());
				temporaryList.add(dto81);
				RouteScheduleDTO dto82 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo82());
				temporaryList.add(dto82);
				RouteScheduleDTO dto83 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo83());
				temporaryList.add(dto83);
				RouteScheduleDTO dto84 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo84());
				temporaryList.add(dto84);
				RouteScheduleDTO dto85 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo85());
				temporaryList.add(dto85);
				RouteScheduleDTO dto86 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo86());
				temporaryList.add(dto86);
				RouteScheduleDTO dto87 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo87());
				temporaryList.add(dto87);
				RouteScheduleDTO dto88 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo88());
				temporaryList.add(dto88);
				RouteScheduleDTO dto89 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo89());
				temporaryList.add(dto89);
				RouteScheduleDTO dto90 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo90());
				temporaryList.add(dto90);
				RouteScheduleDTO dto91 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo91());
				temporaryList.add(dto91);
				RouteScheduleDTO dto92 = new RouteScheduleDTO(mainSaveList.get(i).getTripId(),
						mainSaveList.get(i).getBusNo92());
				temporaryList.add(dto92);

				for (int c = 0; c < days; c++) {

					int day = c + 1;

					RouteScheduleDTO routeDTO = new RouteScheduleDTO(temporaryList.get(c).getTripId(),
							temporaryList.get(c).getBusNo(), day);
					saveList.add(routeDTO);
				}

			}

		}

		return saveList;

	}

	public void saveAction() {

		if (!lastPanelDateVisible) {
			boolean isRelatedDataFound = routeScheduleService.isRelatedDataFound(routeScheduleDTO.getRouteNo(),
					routeScheduleDTO.getBusCategory(), routeScheduleDTO.getGeneratedRefNo(),
					routeScheduleDTO.getGroupNo(), tripType);

			if (isRelatedDataFound == false) {

				if (!mainSaveList.isEmpty()) {

					List<RouteScheduleDTO> listForSave = saveListManager();

					int day = (int) calculateNoOfDays();
					String referenceNo = routeScheduleService.generateReferenceNo();

					if (!listForSave.isEmpty()) {

						boolean isDataSave = routeScheduleService.insertRouteScheduleMasterTableData(routeScheduleDTO,
								rotationType, tripType, routeScheduleDTO.getGroupNo(),
								routeScheduleDTO.getGeneratedRefNo(), day, sessionBackingBean.getLoginUser(),
								referenceNo);

//						routeScheduleService.insertRouteScheduleDetOneTableData(routeScheduleDTO, listForSave,
//								routeScheduleDTO.getGeneratedRefNo(), sessionBackingBean.getLoginUser(), referenceNo,
//								false, routeScheduleDTO.isSwapEnds(), false, 0);

						routeScheduleService.insertRouteScheduleDetTwoTableData(routeScheduleDTO, leavePositionList,
								routeScheduleDTO.getGeneratedRefNo(), sessionBackingBean.getLoginUser(), referenceNo,
								tripType);

						if (isDataSave) {
							setSuccessMessage("Saved successfully.");
							RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
						} else {
							setErrorMessage("Data not saved.");
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						}

					} else {
						setErrorMessage("No data found.");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}

				} else {
					setErrorMessage("No data found.");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else {
				setErrorMessage("Same record found.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {
			// edit
			if (!mainSaveList.isEmpty()) {

				List<RouteScheduleDTO> listForSave = saveListManager();
				String referenceNo = routeScheduleService.retrieveReferenceNo(routeScheduleDTO.getRouteNo(),
						routeScheduleDTO.getBusCategory(), routeScheduleDTO.getGeneratedRefNo(),
						routeScheduleDTO.getGroupNo(), tripType);

				// update start end dates
				// Date start_date = routeScheduleDTO.getStartDate();
				Date start_date = lastPanelStartDateNew;
				Date end_date = routeScheduleDTO.getLastPanelEndDate();

				long diff = start_date.getTime() - end_date.getTime();

				long days = (diff / (1000 * 60 * 60 * 24) - 1);
				long dataDifference = Math.abs(days);
				int differenceInt = (int) (long) dataDifference;

				routeScheduleService.updateRouteScheduleGeneratorDates(routeScheduleDTO.getLastPanelEndDate(),
						routeScheduleDTO.getRouteNo(), routeScheduleDTO.getBusCategory(),
						routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), tripType, differenceInt,
						lastPanelStartDateNew);
				int masSeq = routeScheduleService.getMasterSeq(routeScheduleDTO.getRouteNo(),
						routeScheduleDTO.getBusCategory(), routeScheduleDTO.getGeneratedRefNo(),
						routeScheduleDTO.getGroupNo(), tripType);
				// insert data to DB table
//				routeScheduleService.insertRouteScheduleDetOneTableData(routeScheduleDTO, listForSave,
//						routeScheduleDTO.getGeneratedRefNo(), sessionBackingBean.getLoginUser(), referenceNo, true,
//						routeScheduleDTO.isSwapEnds(), true, masSeq);

				disabledNoramlRotation = true;
				disabledSave = true;

				setSuccessMessage("Saved successfully.");
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

			} else {
				setErrorMessage("No data found.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		}

	}

	public void editRouteScheduleData() {

		dateList = new ArrayList<String>();
		dateList = routeScheduleService.getDateRangesForRouteSchedule(routeScheduleDTO.getRouteNo(),
				routeScheduleDTO.getBusCategory(), routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(),
				tripType);
		editBusForRouteList = new ArrayList<RouteScheduleDTO>();
		RequestContext.getCurrentInstance().execute("PF('comfirmMSG').hide()");
		RequestContext.getCurrentInstance().execute("PF('editRouteScheduleVAR').show()");
	}

	public void searchEditRouteScheduleData() {
		dateCount = 0;
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		int noOfTripsPerSide = 0;

		try {

			for (String s : dateList) {
				if (s.equalsIgnoreCase(selectedDateRange)) {
					dateCount = dateCount + 1;
				}
			}

			int tripCounting = 0;
			lastPanelDateVisible = true;
			RouteScheduleDTO endTimeDTO = routeScheduleService.getLastRouteScheduleData(routeScheduleDTO.getRouteNo(),
					routeScheduleDTO.getBusCategory(), routeScheduleDTO.getGeneratedRefNo(),
					routeScheduleDTO.getGroupNo(), tripType);

			routeScheduleDTO.setLastPanelStartDate(formatter.parse(endTimeDTO.getEndDateSTR()));
			routeScheduleDTO.setStartDate(formatter.parse(endTimeDTO.getStartDateSTR()));
			routeScheduleDTO.setEndDate(formatter.parse(endTimeDTO.getEndDateSTR()));

			noOfDaysForEdit = routeScheduleService.getNoOfDays(routeScheduleDTO.getRouteNo(),
					routeScheduleDTO.getBusCategory(), routeScheduleDTO.getGeneratedRefNo(),
					routeScheduleDTO.getGroupNo(), tripType);

			String[] split = selectedDateRange.split("-");
			String firstSubString = split[0];
			String secondSubString = split[1];
			Date startDate = formatter.parse(firstSubString);
			Date endDate = formatter.parse(secondSubString);
			long diff = startDate.getTime() - endDate.getTime();
			long days = (diff / (1000 * 60 * 60 * 24)) - 1;
			long dataDifference = Math.abs(days);
			int differenceInt = (int) (long) dataDifference;

			noOfDaysForEdit = differenceInt;

			List<String> busNoList = new ArrayList<String>();

			busNoList = routeScheduleService.selectEditDataForRouteScheduleNew(routeScheduleDTO.getRouteNo(),
					routeScheduleDTO.getBusCategory(), routeScheduleDTO.getGeneratedRefNo(),
					routeScheduleDTO.getGroupNo(), tripType, dateCount);
			/** drop assign bus nu by tharushi.e **/

			int totalTrips = 0;
			if (tripType.equalsIgnoreCase("O")) {

				if (coupleCount.equals("2")) {
					totalTrips = routeScheduleService.getFixedOrWithoutFixedTripsCountNewWithCoupleTwo(
							routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), "O", "N",
							routeScheduleDTO.getRouteNo(), routeScheduleDTO.getBusCategory());
				} else {
					totalTrips = routeScheduleService.getFixedOrWithoutFixedTripsCountNew(
							routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), "O", "N",
							routeScheduleDTO.getRouteNo(), routeScheduleDTO.getBusCategory());
				}
				/** commented and newly added by tharushi.e **/

				int fixedTrips = routeScheduleService.getFixedOrWithoutFixedTripsCount(
						routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), "O", "Y");

				/** added tharushi.e **/

			} else if (tripType.equalsIgnoreCase("D")) {

				if (coupleCount.equals("2")) {

					totalTrips = routeScheduleService.getFixedOrWithoutFixedTripsCountNewWithCoupleTwo(
							routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), "D", "N",
							routeScheduleDTO.getRouteNo(), routeScheduleDTO.getBusCategory());
				} else {

					totalTrips = routeScheduleService.getFixedOrWithoutFixedTripsCountNew(
							routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), "D", "N",
							routeScheduleDTO.getRouteNo(), routeScheduleDTO.getBusCategory());
				}
				/** commented and newly added by tharushi.e **/

				int fixedTrips = routeScheduleService.getFixedOrWithoutFixedTripsCount(
						routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), "D", "Y");
				totalTrips = totalTrips + fixedTrips;
				/** added tharushi.e **/

			}

			List<RouteScheduleDTO> tempAllBusList = routeScheduleService
					.retrieveInsertedDataForEdit(routeScheduleDTO.getGeneratedRefNo(), tripType);
			int tempTripsNum = tempAllBusList.get(0).getTripCount();

			List<RouteScheduleDTO> tempList = new ArrayList<RouteScheduleDTO>();

			tripID = 0;
			boolean first = false;
			for (int nooftrips = 0; nooftrips < totalTrips; nooftrips++) {

				List<String> stringBusNoList = new ArrayList<>();

				/*
				 * Rotate normally. Get the first number of list and send it to last index. then
				 * last index come to before the last one. likewise all index move
				 */
				List<String> tempBusNoList = new ArrayList<String>();
				for (int i = 0; i < busNoList.size(); i++) {

					if (!first) {

						break;
					} else {
						if (i == 0) {
							tempBusNoList.add(busNoList.get(busNoList.size() - 1));
						} else if (i > 0) {
							tempBusNoList.add(busNoList.get(i - 1));
						}

					}
				}
				first = true;
				if (tempBusNoList != null && !tempBusNoList.isEmpty() && tempBusNoList.size() != 0) {
					busNoList = tempBusNoList;
				}

				while (stringBusNoList.size() < maximumDays) {

					int count = 0; // Count the number of buses

					for (int a = 0; a < busNoList.size(); a++) {

						if (count < busNoList.size() && stringBusNoList.size() < maximumDays) {
							String busNo = busNoList.get(a);
							stringBusNoList.add(busNo);
							count++;

						} else {
							count = 0;
						}
					}
				}

				/*****/
				for (int i = 0; i < tempTripsNum; i++) {

					if (tripCounting < tempAllBusList.size()) {

						stringBusNoList.set(i, tempAllBusList.get(tripCounting).getBusNo() + "-"
								+ tempAllBusList.get(tripCounting).getRouteSeq());

						tripCounting = tripCounting + 1;
					}
				}
				/****/

				RouteScheduleDTO dto = setBusToRouteScheduleDTOs(stringBusNoList);

				tempList.add(dto);
			}

			editBusForRouteList = new ArrayList<RouteScheduleDTO>();
			editBusForRouteList.addAll(tempList);
			/**
			 * here have to remove busNulist(assignbusnumber list by tharushi.e)
			 */

			createEditDataTable();
			afterSearch = true;

		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	public void onCellEdidAction() {

		List<String> busNoList = new ArrayList<String>();
		getTempBusListForRouteList();

		for (RouteScheduleDTO original : tempEditBusForRouteList) {
			for (RouteScheduleDTO edited : editBusForRouteList) {

				if (original.getBusNoSeq1().equals(edited.getBusNoSeq1())) {
					if (original.getBusNo1().equalsIgnoreCase(edited.getBusNo1())) {

					} else {

						editBusNumbers(original.getBusNo1(), edited.getBusNo1(), original.getBusNoSeq1());
						break;

					}
				}

				if (original.getBusNoSeq2().equals(edited.getBusNoSeq2())) {
					if (original.getBusNo2().equalsIgnoreCase(edited.getBusNo2())) {

					} else {

						editBusNumbers(original.getBusNo2(), edited.getBusNo2(), original.getBusNoSeq2());
						break;
					}
				}

				if (original.getBusNoSeq3().equals(edited.getBusNoSeq3())) {
					if (original.getBusNo3().equalsIgnoreCase(edited.getBusNo3())) {

					} else {

						editBusNumbers(original.getBusNo3(), edited.getBusNo3(), original.getBusNoSeq3());
						break;
					}
				}

				if (original.getBusNoSeq4().equals(edited.getBusNoSeq4())) {
					if (original.getBusNo4().equalsIgnoreCase(edited.getBusNo4())) {

					} else {

						editBusNumbers(original.getBusNo4(), edited.getBusNo4(), original.getBusNoSeq4());
						break;
					}
				}

				if (original.getBusNoSeq5().equals(edited.getBusNoSeq5())) {
					if (original.getBusNo5().equalsIgnoreCase(edited.getBusNo5())) {

					} else {

						editBusNumbers(original.getBusNo5(), edited.getBusNo5(), original.getBusNoSeq5());
						break;
					}
				}

				if (original.getBusNoSeq6().equals(edited.getBusNoSeq6())) {
					if (original.getBusNo6().equalsIgnoreCase(edited.getBusNo6())) {

					} else {

						editBusNumbers(original.getBusNo6(), edited.getBusNo6(), original.getBusNoSeq6());
						break;
					}
				}

				if (original.getBusNoSeq7().equals(edited.getBusNoSeq7())) {
					if (original.getBusNo7().equalsIgnoreCase(edited.getBusNo7())) {

					} else {

						editBusNumbers(original.getBusNo7(), edited.getBusNo7(), original.getBusNoSeq7());
						break;
					}
				}

				if (original.getBusNoSeq8().equals(edited.getBusNoSeq8())) {
					if (original.getBusNo8().equalsIgnoreCase(edited.getBusNo8())) {

					} else {

						editBusNumbers(original.getBusNo8(), edited.getBusNo8(), original.getBusNoSeq8());
						break;
					}
				}

				if (original.getBusNoSeq9().equals(edited.getBusNoSeq9())) {
					if (original.getBusNo9().equalsIgnoreCase(edited.getBusNo9())) {

					} else {

						editBusNumbers(original.getBusNo9(), edited.getBusNo9(), original.getBusNoSeq9());
						break;
					}
				}

				if (original.getBusNoSeq10().equals(edited.getBusNoSeq10())) {
					if (original.getBusNo10().equalsIgnoreCase(edited.getBusNo10())) {

					} else {

						editBusNumbers(original.getBusNo10(), edited.getBusNo10(), original.getBusNoSeq10());
						break;
					}
				}

				if (original.getBusNoSeq11().equals(edited.getBusNoSeq11())) {
					if (original.getBusNo11().equalsIgnoreCase(edited.getBusNo11())) {

					} else {

						editBusNumbers(original.getBusNo11(), edited.getBusNo11(), original.getBusNoSeq11());
						break;
					}
				}

				if (original.getBusNoSeq12().equals(edited.getBusNoSeq12())) {
					if (original.getBusNo12().equalsIgnoreCase(edited.getBusNo12())) {

					} else {

						editBusNumbers(original.getBusNo12(), edited.getBusNo12(), original.getBusNoSeq12());
						break;
					}
				}

				if (original.getBusNoSeq13().equals(edited.getBusNoSeq13())) {
					if (original.getBusNo13().equalsIgnoreCase(edited.getBusNo13())) {

					} else {

						editBusNumbers(original.getBusNo13(), edited.getBusNo13(), original.getBusNoSeq13());
						break;
					}
				}

				if (original.getBusNoSeq14().equals(edited.getBusNoSeq14())) {
					if (original.getBusNo14().equalsIgnoreCase(edited.getBusNo14())) {

					} else {

						editBusNumbers(original.getBusNo14(), edited.getBusNo14(), original.getBusNoSeq14());
						break;
					}
				}

				if (original.getBusNoSeq15().equals(edited.getBusNoSeq15())) {
					if (original.getBusNo15().equalsIgnoreCase(edited.getBusNo15())) {

					} else {

						editBusNumbers(original.getBusNo15(), edited.getBusNo15(), original.getBusNoSeq15());
						break;
					}
				}

				if (original.getBusNoSeq16().equals(edited.getBusNoSeq16())) {
					if (original.getBusNo16().equalsIgnoreCase(edited.getBusNo16())) {

					} else {

						editBusNumbers(original.getBusNo16(), edited.getBusNo16(), original.getBusNoSeq16());
						break;
					}
				}

				if (original.getBusNoSeq17().equals(edited.getBusNoSeq17())) {
					if (original.getBusNo17().equalsIgnoreCase(edited.getBusNo17())) {

					} else {

						editBusNumbers(original.getBusNo17(), edited.getBusNo17(), original.getBusNoSeq17());
						break;
					}
				}

				if (original.getBusNoSeq18().equals(edited.getBusNoSeq18())) {
					if (original.getBusNo18().equalsIgnoreCase(edited.getBusNo18())) {

					} else {

						editBusNumbers(original.getBusNo18(), edited.getBusNo18(), original.getBusNoSeq18());
						break;
					}
				}

				if (original.getBusNoSeq19().equals(edited.getBusNoSeq19())) {
					if (original.getBusNo19().equalsIgnoreCase(edited.getBusNo19())) {

					} else {

						editBusNumbers(original.getBusNo19(), edited.getBusNo19(), original.getBusNoSeq19());
						break;
					}
				}

				if (original.getBusNoSeq20().equals(edited.getBusNoSeq20())) {
					if (original.getBusNo20().equalsIgnoreCase(edited.getBusNo20())) {

					} else {

						editBusNumbers(original.getBusNo20(), edited.getBusNo20(), original.getBusNoSeq20());
						break;
					}
				}

				if (original.getBusNoSeq21().equals(edited.getBusNoSeq21())) {
					if (original.getBusNo21().equalsIgnoreCase(edited.getBusNo21())) {

					} else {

						editBusNumbers(original.getBusNo21(), edited.getBusNo21(), original.getBusNoSeq21());
						break;
					}
				}

				if (original.getBusNoSeq22().equals(edited.getBusNoSeq22())) {
					if (original.getBusNo22().equalsIgnoreCase(edited.getBusNo22())) {

					} else {

						editBusNumbers(original.getBusNo22(), edited.getBusNo22(), original.getBusNoSeq22());
						break;
					}
				}

				if (original.getBusNoSeq23().equals(edited.getBusNoSeq23())) {
					if (original.getBusNo23().equalsIgnoreCase(edited.getBusNo23())) {

					} else {

						editBusNumbers(original.getBusNo23(), edited.getBusNo23(), original.getBusNoSeq23());
						break;
					}
				}

				if (original.getBusNoSeq24().equals(edited.getBusNoSeq24())) {
					if (original.getBusNo24().equalsIgnoreCase(edited.getBusNo24())) {

					} else {

						editBusNumbers(original.getBusNo24(), edited.getBusNo24(), original.getBusNoSeq24());
						break;
					}
				}

				if (original.getBusNoSeq25().equals(edited.getBusNoSeq25())) {
					if (original.getBusNo25().equalsIgnoreCase(edited.getBusNo25())) {

					} else {

						editBusNumbers(original.getBusNo25(), edited.getBusNo25(), original.getBusNoSeq25());
						break;
					}
				}

				if (original.getBusNoSeq26().equals(edited.getBusNoSeq26())) {
					if (original.getBusNo26().equalsIgnoreCase(edited.getBusNo26())) {

					} else {

						editBusNumbers(original.getBusNo26(), edited.getBusNo26(), original.getBusNoSeq26());
						break;
					}
				}

				if (original.getBusNoSeq27().equals(edited.getBusNoSeq27())) {
					if (original.getBusNo27().equalsIgnoreCase(edited.getBusNo27())) {

					} else {

						editBusNumbers(original.getBusNo27(), edited.getBusNo27(), original.getBusNoSeq27());
						break;
					}
				}

				if (original.getBusNoSeq28().equals(edited.getBusNoSeq28())) {
					if (original.getBusNo28().equalsIgnoreCase(edited.getBusNo28())) {

					} else {

						editBusNumbers(original.getBusNo28(), edited.getBusNo28(), original.getBusNoSeq28());
						break;
					}
				}

				if (original.getBusNoSeq1().equals(edited.getBusNoSeq1())) {
					if (original.getBusNo1().equalsIgnoreCase(edited.getBusNo1())) {

					} else {

						editBusNumbers(original.getBusNo1(), edited.getBusNo1(), original.getBusNoSeq1());
						break;
					}
				}

				if (original.getBusNoSeq29().equals(edited.getBusNoSeq29())) {
					if (original.getBusNo29().equalsIgnoreCase(edited.getBusNo29())) {

					} else {

						editBusNumbers(original.getBusNo29(), edited.getBusNo29(), original.getBusNoSeq29());
						break;
					}
				}

				if (original.getBusNoSeq30().equals(edited.getBusNoSeq30())) {
					if (original.getBusNo30().equalsIgnoreCase(edited.getBusNo30())) {

					} else {

						editBusNumbers(original.getBusNo30(), edited.getBusNo30(), original.getBusNoSeq30());
						break;
					}
				}

				if (original.getBusNoSeq31().equals(edited.getBusNoSeq31())) {
					if (original.getBusNo31().equalsIgnoreCase(edited.getBusNo31())) {

					} else {

						editBusNumbers(original.getBusNo31(), edited.getBusNo31(), original.getBusNoSeq31());
						break;
					}
				}

				if (original.getBusNoSeq32().equals(edited.getBusNoSeq32())) {
					if (original.getBusNo32().equalsIgnoreCase(edited.getBusNo32())) {

					} else {

						editBusNumbers(original.getBusNo32(), edited.getBusNo32(), original.getBusNoSeq32());
						break;
					}
				}

				if (original.getBusNoSeq33().equals(edited.getBusNoSeq33())) {
					if (original.getBusNo33().equalsIgnoreCase(edited.getBusNo33())) {

					} else {

						editBusNumbers(original.getBusNo33(), edited.getBusNo33(), original.getBusNoSeq33());
						break;
					}
				}

				if (original.getBusNoSeq34().equals(edited.getBusNoSeq34())) {
					if (original.getBusNo34().equalsIgnoreCase(edited.getBusNo34())) {

					} else {

						editBusNumbers(original.getBusNo34(), edited.getBusNo34(), original.getBusNoSeq34());
						break;
					}
				}

				if (original.getBusNoSeq35().equals(edited.getBusNoSeq35())) {
					if (original.getBusNo35().equalsIgnoreCase(edited.getBusNo35())) {

					} else {

						editBusNumbers(original.getBusNo35(), edited.getBusNo35(), original.getBusNoSeq35());
						break;
					}
				}

				if (original.getBusNoSeq36().equals(edited.getBusNoSeq36())) {
					if (original.getBusNo36().equalsIgnoreCase(edited.getBusNo36())) {

					} else {

						editBusNumbers(original.getBusNo36(), edited.getBusNo36(), original.getBusNoSeq36());
						break;
					}
				}

				if (original.getBusNoSeq37().equals(edited.getBusNoSeq37())) {
					if (original.getBusNo37().equalsIgnoreCase(edited.getBusNo37())) {

					} else {

						editBusNumbers(original.getBusNo37(), edited.getBusNo37(), original.getBusNoSeq37());
						break;
					}
				}

				if (original.getBusNoSeq38().equals(edited.getBusNoSeq38())) {
					if (original.getBusNo38().equalsIgnoreCase(edited.getBusNo38())) {

					} else {

						editBusNumbers(original.getBusNo38(), edited.getBusNo38(), original.getBusNoSeq38());
						break;
					}
				}

				if (original.getBusNoSeq39().equals(edited.getBusNoSeq39())) {
					if (original.getBusNo39().equalsIgnoreCase(edited.getBusNo39())) {

					} else {

						editBusNumbers(original.getBusNo39(), edited.getBusNo39(), original.getBusNoSeq39());
						break;
					}
				}

				if (original.getBusNoSeq40().equals(edited.getBusNoSeq40())) {
					if (original.getBusNo40().equalsIgnoreCase(edited.getBusNo40())) {

					} else {

						editBusNumbers(original.getBusNo40(), edited.getBusNo40(), original.getBusNoSeq40());
						break;
					}
				}

				if (original.getBusNoSeq41().equals(edited.getBusNoSeq41())) {
					if (original.getBusNo41().equalsIgnoreCase(edited.getBusNo41())) {

					} else {

						editBusNumbers(original.getBusNo41(), edited.getBusNo41(), original.getBusNoSeq41());
						break;
					}
				}

				if (original.getBusNoSeq42().equals(edited.getBusNoSeq42())) {
					if (original.getBusNo42().equalsIgnoreCase(edited.getBusNo42())) {

					} else {

						editBusNumbers(original.getBusNo42(), edited.getBusNo42(), original.getBusNoSeq42());
						break;
					}
				}

				if (original.getBusNoSeq43().equals(edited.getBusNoSeq43())) {
					if (original.getBusNo43().equalsIgnoreCase(edited.getBusNo43())) {

					} else {

						editBusNumbers(original.getBusNo43(), edited.getBusNo43(), original.getBusNoSeq43());
						break;
					}
				}

				if (original.getBusNoSeq44().equals(edited.getBusNoSeq44())) {
					if (original.getBusNo44().equalsIgnoreCase(edited.getBusNo44())) {

					} else {

						editBusNumbers(original.getBusNo44(), edited.getBusNo44(), original.getBusNoSeq44());
						break;
					}
				}

				if (original.getBusNoSeq45().equals(edited.getBusNoSeq45())) {
					if (original.getBusNo45().equalsIgnoreCase(edited.getBusNo45())) {

					} else {

						editBusNumbers(original.getBusNo45(), edited.getBusNo45(), original.getBusNoSeq45());
						break;
					}
				}

				if (original.getBusNoSeq46().equals(edited.getBusNoSeq46())) {
					if (original.getBusNo46().equalsIgnoreCase(edited.getBusNo46())) {

					} else {

						editBusNumbers(original.getBusNo46(), edited.getBusNo46(), original.getBusNoSeq46());
						break;
					}
				}

				if (original.getBusNoSeq46().equals(edited.getBusNoSeq46())) {
					if (original.getBusNo46().equalsIgnoreCase(edited.getBusNo46())) {

					} else {

						editBusNumbers(original.getBusNo46(), edited.getBusNo46(), original.getBusNoSeq46());
						break;
					}
				}

				if (original.getBusNoSeq47().equals(edited.getBusNoSeq47())) {
					if (original.getBusNo47().equalsIgnoreCase(edited.getBusNo47())) {

					} else {

						editBusNumbers(original.getBusNo47(), edited.getBusNo47(), original.getBusNoSeq47());
						break;
					}
				}

				if (original.getBusNoSeq48().equals(edited.getBusNoSeq48())) {
					if (original.getBusNo48().equalsIgnoreCase(edited.getBusNo48())) {

					} else {

						editBusNumbers(original.getBusNo48(), edited.getBusNo48(), original.getBusNoSeq48());
						break;
					}
				}

				if (original.getBusNoSeq49().equals(edited.getBusNoSeq49())) {
					if (original.getBusNo49().equalsIgnoreCase(edited.getBusNo49())) {

					} else {

						editBusNumbers(original.getBusNo49(), edited.getBusNo49(), original.getBusNoSeq49());
						break;
					}
				}

				if (original.getBusNoSeq50().equals(edited.getBusNoSeq50())) {
					if (original.getBusNo50().equalsIgnoreCase(edited.getBusNo50())) {

					} else {

						editBusNumbers(original.getBusNo50(), edited.getBusNo50(), original.getBusNoSeq50());
						break;
					}
				}

				if (original.getBusNoSeq51().equals(edited.getBusNoSeq51())) {
					if (original.getBusNo51().equalsIgnoreCase(edited.getBusNo51())) {

					} else {

						editBusNumbers(original.getBusNo51(), edited.getBusNo51(), original.getBusNoSeq51());
						break;
					}
				}

				if (original.getBusNoSeq52().equals(edited.getBusNoSeq52())) {
					if (original.getBusNo52().equalsIgnoreCase(edited.getBusNo52())) {

					} else {

						editBusNumbers(original.getBusNo52(), edited.getBusNo52(), original.getBusNoSeq52());
						break;
					}
				}

				if (original.getBusNoSeq53().equals(edited.getBusNoSeq53())) {
					if (original.getBusNo53().equalsIgnoreCase(edited.getBusNo53())) {

					} else {

						editBusNumbers(original.getBusNo53(), edited.getBusNo53(), original.getBusNoSeq53());
						break;
					}
				}

				if (original.getBusNoSeq54().equals(edited.getBusNoSeq54())) {
					if (original.getBusNo54().equalsIgnoreCase(edited.getBusNo54())) {

					} else {

						editBusNumbers(original.getBusNo54(), edited.getBusNo54(), original.getBusNoSeq54());
						break;
					}
				}

				if (original.getBusNoSeq55().equals(edited.getBusNoSeq55())) {
					if (original.getBusNo55().equalsIgnoreCase(edited.getBusNo55())) {

					} else {

						editBusNumbers(original.getBusNo55(), edited.getBusNo55(), original.getBusNoSeq55());
						break;
					}
				}

				if (original.getBusNoSeq56().equals(edited.getBusNoSeq56())) {
					if (original.getBusNo56().equalsIgnoreCase(edited.getBusNo56())) {

					} else {

						editBusNumbers(original.getBusNo56(), edited.getBusNo56(), original.getBusNoSeq56());
						break;
					}
				}

				if (original.getBusNoSeq57().equals(edited.getBusNoSeq57())) {
					if (original.getBusNo57().equalsIgnoreCase(edited.getBusNo57())) {

					} else {

						editBusNumbers(original.getBusNo57(), edited.getBusNo57(), original.getBusNoSeq57());
						break;
					}
				}

				if (original.getBusNoSeq58().equals(edited.getBusNoSeq58())) {
					if (original.getBusNo58().equalsIgnoreCase(edited.getBusNo58())) {

					} else {

						editBusNumbers(original.getBusNo58(), edited.getBusNo58(), original.getBusNoSeq58());
						break;
					}
				}

				if (original.getBusNoSeq59().equals(edited.getBusNoSeq59())) {
					if (original.getBusNo59().equalsIgnoreCase(edited.getBusNo59())) {

					} else {

						editBusNumbers(original.getBusNo59(), edited.getBusNo59(), original.getBusNoSeq59());
						break;
					}
				}

				if (original.getBusNoSeq60().equals(edited.getBusNoSeq60())) {
					if (original.getBusNo60().equalsIgnoreCase(edited.getBusNo60())) {

					} else {

						editBusNumbers(original.getBusNo60(), edited.getBusNo60(), original.getBusNoSeq60());
						break;
					}
				}

				if (original.getBusNoSeq61().equals(edited.getBusNoSeq61())) {
					if (original.getBusNo61().equalsIgnoreCase(edited.getBusNo61())) {

					} else {

						editBusNumbers(original.getBusNo61(), edited.getBusNo61(), original.getBusNoSeq61());
						break;
					}
				}

				if (original.getBusNoSeq62().equals(edited.getBusNoSeq62())) {
					if (original.getBusNo62().equalsIgnoreCase(edited.getBusNo62())) {

					} else {

						editBusNumbers(original.getBusNo62(), edited.getBusNo62(), original.getBusNoSeq62());
						break;
					}
				}

				if (original.getBusNoSeq63().equals(edited.getBusNoSeq63())) {
					if (original.getBusNo63().equalsIgnoreCase(edited.getBusNo63())) {

					} else {

						editBusNumbers(original.getBusNo63(), edited.getBusNo63(), original.getBusNoSeq63());
						break;
					}
				}

				if (original.getBusNoSeq64().equals(edited.getBusNoSeq64())) {
					if (original.getBusNo64().equalsIgnoreCase(edited.getBusNo64())) {

					} else {

						editBusNumbers(original.getBusNo64(), edited.getBusNo64(), original.getBusNoSeq64());
						break;
					}
				}

				if (original.getBusNoSeq65().equals(edited.getBusNoSeq65())) {
					if (original.getBusNo65().equalsIgnoreCase(edited.getBusNo65())) {

					} else {

						editBusNumbers(original.getBusNo65(), edited.getBusNo65(), original.getBusNoSeq65());
						break;
					}
				}

				if (original.getBusNoSeq66().equals(edited.getBusNoSeq66())) {
					if (original.getBusNo66().equalsIgnoreCase(edited.getBusNo66())) {

					} else {

						editBusNumbers(original.getBusNo66(), edited.getBusNo66(), original.getBusNoSeq66());
						break;
					}
				}

				if (original.getBusNoSeq67().equals(edited.getBusNoSeq67())) {
					if (original.getBusNo67().equalsIgnoreCase(edited.getBusNo67())) {

					} else {

						editBusNumbers(original.getBusNo67(), edited.getBusNo67(), original.getBusNoSeq67());
						break;
					}
				}

				if (original.getBusNoSeq68().equals(edited.getBusNoSeq68())) {
					if (original.getBusNo68().equalsIgnoreCase(edited.getBusNo68())) {

					} else {

						editBusNumbers(original.getBusNo68(), edited.getBusNo68(), original.getBusNoSeq68());
						break;
					}
				}

				if (original.getBusNoSeq69().equals(edited.getBusNoSeq69())) {
					if (original.getBusNo69().equalsIgnoreCase(edited.getBusNo69())) {

					} else {

						editBusNumbers(original.getBusNo69(), edited.getBusNo69(), original.getBusNoSeq69());
						break;
					}
				}

				if (original.getBusNoSeq70().equals(edited.getBusNoSeq70())) {
					if (original.getBusNo70().equalsIgnoreCase(edited.getBusNo70())) {

					} else {

						editBusNumbers(original.getBusNo70(), edited.getBusNo70(), original.getBusNoSeq70());
						break;
					}
				}

				if (original.getBusNoSeq71().equals(edited.getBusNoSeq71())) {
					if (original.getBusNo71().equalsIgnoreCase(edited.getBusNo71())) {

					} else {

						editBusNumbers(original.getBusNo71(), edited.getBusNo71(), original.getBusNoSeq71());
						break;
					}
				}

				if (original.getBusNoSeq72().equals(edited.getBusNoSeq72())) {
					if (original.getBusNo72().equalsIgnoreCase(edited.getBusNo72())) {

					} else {

						editBusNumbers(original.getBusNo72(), edited.getBusNo72(), original.getBusNoSeq72());
						break;
					}
				}

				if (original.getBusNoSeq73().equals(edited.getBusNoSeq73())) {
					if (original.getBusNo73().equalsIgnoreCase(edited.getBusNo73())) {

					} else {

						editBusNumbers(original.getBusNo73(), edited.getBusNo73(), original.getBusNoSeq73());
						break;
					}
				}

				if (original.getBusNoSeq74().equals(edited.getBusNoSeq74())) {
					if (original.getBusNo74().equalsIgnoreCase(edited.getBusNo74())) {

					} else {

						editBusNumbers(original.getBusNo74(), edited.getBusNo74(), original.getBusNoSeq74());
						break;
					}
				}

				if (original.getBusNoSeq75().equals(edited.getBusNoSeq75())) {
					if (original.getBusNo75().equalsIgnoreCase(edited.getBusNo75())) {

					} else {

						editBusNumbers(original.getBusNo75(), edited.getBusNo75(), original.getBusNoSeq75());
						break;
					}
				}

				if (original.getBusNoSeq76().equals(edited.getBusNoSeq76())) {
					if (original.getBusNo76().equalsIgnoreCase(edited.getBusNo76())) {

					} else {

						editBusNumbers(original.getBusNo76(), edited.getBusNo76(), original.getBusNoSeq76());
						break;
					}
				}

				if (original.getBusNoSeq77().equals(edited.getBusNoSeq77())) {
					if (original.getBusNo77().equalsIgnoreCase(edited.getBusNo77())) {

					} else {

						editBusNumbers(original.getBusNo77(), edited.getBusNo77(), original.getBusNoSeq77());
						break;
					}
				}

				if (original.getBusNoSeq78().equals(edited.getBusNoSeq78())) {
					if (original.getBusNo78().equalsIgnoreCase(edited.getBusNo78())) {

					} else {

						editBusNumbers(original.getBusNo78(), edited.getBusNo78(), original.getBusNoSeq78());
						break;
					}
				}

				if (original.getBusNoSeq79().equals(edited.getBusNoSeq79())) {
					if (original.getBusNo79().equalsIgnoreCase(edited.getBusNo79())) {

					} else {

						editBusNumbers(original.getBusNo79(), edited.getBusNo79(), original.getBusNoSeq79());
						break;
					}
				}

				if (original.getBusNoSeq80().equals(edited.getBusNoSeq80())) {
					if (original.getBusNo80().equalsIgnoreCase(edited.getBusNo80())) {

					} else {

						editBusNumbers(original.getBusNo80(), edited.getBusNo80(), original.getBusNoSeq80());
						break;
					}
				}

				if (original.getBusNoSeq81().equals(edited.getBusNoSeq81())) {
					if (original.getBusNo81().equalsIgnoreCase(edited.getBusNo81())) {

					} else {

						editBusNumbers(original.getBusNo81(), edited.getBusNo81(), original.getBusNoSeq81());
						break;
					}
				}

				if (original.getBusNoSeq82().equals(edited.getBusNoSeq82())) {
					if (original.getBusNo82().equalsIgnoreCase(edited.getBusNo82())) {

					} else {

						editBusNumbers(original.getBusNo82(), edited.getBusNo82(), original.getBusNoSeq82());
						break;
					}
				}

				if (original.getBusNoSeq83().equals(edited.getBusNoSeq83())) {
					if (original.getBusNo83().equalsIgnoreCase(edited.getBusNo83())) {

					} else {

						editBusNumbers(original.getBusNo83(), edited.getBusNo83(), original.getBusNoSeq83());
						break;
					}
				}

				if (original.getBusNoSeq84().equals(edited.getBusNoSeq84())) {
					if (original.getBusNo84().equalsIgnoreCase(edited.getBusNo84())) {

					} else {

						editBusNumbers(original.getBusNo84(), edited.getBusNo84(), original.getBusNoSeq84());
						break;
					}
				}

				if (original.getBusNoSeq85().equals(edited.getBusNoSeq85())) {
					if (original.getBusNo85().equalsIgnoreCase(edited.getBusNo85())) {

					} else {

						editBusNumbers(original.getBusNo85(), edited.getBusNo85(), original.getBusNoSeq85());
						break;
					}
				}

				if (original.getBusNoSeq86().equals(edited.getBusNoSeq86())) {
					if (original.getBusNo86().equalsIgnoreCase(edited.getBusNo86())) {

					} else {

						editBusNumbers(original.getBusNo86(), edited.getBusNo86(), original.getBusNoSeq86());
						break;
					}
				}

				if (original.getBusNoSeq87().equals(edited.getBusNoSeq87())) {
					if (original.getBusNo87().equalsIgnoreCase(edited.getBusNo87())) {

					} else {

						editBusNumbers(original.getBusNo87(), edited.getBusNo87(), original.getBusNoSeq87());
						break;
					}
				}

				if (original.getBusNoSeq88().equals(edited.getBusNoSeq88())) {
					if (original.getBusNo88().equalsIgnoreCase(edited.getBusNo88())) {

					} else {

						editBusNumbers(original.getBusNo88(), edited.getBusNo88(), original.getBusNoSeq88());
						break;
					}
				}

				if (original.getBusNoSeq89().equals(edited.getBusNoSeq89())) {
					if (original.getBusNo89().equalsIgnoreCase(edited.getBusNo89())) {

					} else {

						editBusNumbers(original.getBusNo89(), edited.getBusNo89(), original.getBusNoSeq89());
						break;
					}
				}

				if (original.getBusNoSeq90().equals(edited.getBusNoSeq90())) {
					if (original.getBusNo90().equalsIgnoreCase(edited.getBusNo90())) {

					} else {

						editBusNumbers(original.getBusNo90(), edited.getBusNo90(), original.getBusNoSeq90());
						break;
					}
				}

				if (original.getBusNoSeq91().equals(edited.getBusNoSeq91())) {
					if (original.getBusNo91().equalsIgnoreCase(edited.getBusNo91())) {

					} else {

						editBusNumbers(original.getBusNo91(), edited.getBusNo91(), original.getBusNoSeq91());
						break;
					}
				}

				if (original.getBusNoSeq92().equals(edited.getBusNoSeq92())) {
					if (original.getBusNo92().equalsIgnoreCase(edited.getBusNo92())) {

					} else {

						editBusNumbers(original.getBusNo92(), edited.getBusNo92(), original.getBusNoSeq92());
						break;
					}
				}
			}
		}
	}

	public void getTempBusListForRouteList() {

		List<String> busNoList = new ArrayList<String>();

		busNoList = routeScheduleService.selectEditDataForRouteScheduleNew(routeScheduleDTO.getRouteNo(),
				routeScheduleDTO.getBusCategory(), routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(),
				tripType, dateCount);
		/** drop assign bus nu by tharushi.e **/

		int totalTrips = 0;
		if (tripType.equalsIgnoreCase("O")) {

			totalTrips = routeScheduleService.getFixedOrWithoutFixedTripsCountNew(routeScheduleDTO.getGeneratedRefNo(),
					routeScheduleDTO.getGroupNo(), "O", "N", routeScheduleDTO.getRouteNo(),
					routeScheduleDTO.getBusCategory());
			/** commented and newly added by tharushi.e **/
			int fixedTrips = routeScheduleService.getFixedOrWithoutFixedTripsCount(routeScheduleDTO.getGeneratedRefNo(),
					routeScheduleDTO.getGroupNo(), "O", "Y");

		} else if (tripType.equalsIgnoreCase("D")) {

			totalTrips = routeScheduleService.getFixedOrWithoutFixedTripsCountNew(routeScheduleDTO.getGeneratedRefNo(),
					routeScheduleDTO.getGroupNo(), "D", "N", routeScheduleDTO.getRouteNo(),
					routeScheduleDTO.getBusCategory());
			/** commented and newly added by tharushi.e **/
			int fixedTrips = routeScheduleService.getFixedOrWithoutFixedTripsCount(routeScheduleDTO.getGeneratedRefNo(),
					routeScheduleDTO.getGroupNo(), "D", "Y");

		}

		List<RouteScheduleDTO> tempList = new ArrayList<RouteScheduleDTO>();
		List<RouteScheduleDTO> tempTempList = new ArrayList<RouteScheduleDTO>();
		for (int nooftrips = 0; nooftrips < totalTrips; nooftrips++) {

			List<String> stringBusNoList = new ArrayList<>();

			while (stringBusNoList.size() < maximumDays) {

				int count = 0;

				for (int a = 0; a < busNoList.size(); a++) {

					if (count < busNoList.size() && stringBusNoList.size() < maximumDays) {
						String busNo = busNoList.get(a);
						stringBusNoList.add(busNo);
						count++;

					} else {
						count = 0;
					}
				}
			}

			RouteScheduleDTO dto = setBusToRouteScheduleDTOs(stringBusNoList);
			tempList.add(dto);
			tempTempList.add(dto);
		}

		tempEditBusForRouteList.addAll(tempTempList);

	}

	public void editBusNumbers(String originalBusNum, String editedBusNum, String seqNum) {

		routeScheduleService.updateEditedBusNumbersInRoute_schedule_generator_det01(originalBusNum, editedBusNum,
				seqNum, sessionBackingBean.getLoginUser(), tripType);

	}

	public void saveEditRouteScheduleDate() {
		boolean success1 = false;
		boolean success2 = false;
		boolean success3 = false;
		boolean success4 = false;
		boolean success5 = false;
		if (!dayOne.isEmpty() && dayOne != null && !tripOne.isEmpty() && tripOne != null && !busOne.isEmpty()
				&& busOne != null) {

			success1 = routeScheduleService.updateEditedBusNumbersAcordinTrip(routeScheduleDTO.getGeneratedRefNo(),
					tripType, dayOne, tripOne, busOne);
		}
		if (!dayTwo.isEmpty() && dayTwo != null && !tripTwo.isEmpty() && tripTwo != null && !busTwo.isEmpty()
				&& busTwo != null) {
			success2 = routeScheduleService.updateEditedBusNumbersAcordinTrip(routeScheduleDTO.getGeneratedRefNo(),
					tripType, dayTwo, tripTwo, busTwo);

		}
		if (!dayThree.isEmpty() && dayThree != null && !tripThree.isEmpty() && tripThree != null && !busThree.isEmpty()
				&& busThree != null) {

			success3 = routeScheduleService.updateEditedBusNumbersAcordinTrip(routeScheduleDTO.getGeneratedRefNo(),
					tripType, dayThree, tripThree, busThree);
		}
		if (!dayFour.isEmpty() && dayFour != null && !tripFour.isEmpty() && tripFour != null && !busFour.isEmpty()
				&& busFour != null) {
			success4 = routeScheduleService.updateEditedBusNumbersAcordinTrip(routeScheduleDTO.getGeneratedRefNo(),
					tripType, dayFour, tripFour, busFour);

		}
		if (!dayFive.isEmpty() && dayFive != null && !tripFive.isEmpty() && tripFive != null && !busFive.isEmpty()
				&& busFive != null) {

			success5 = routeScheduleService.updateEditedBusNumbersAcordinTrip(routeScheduleDTO.getGeneratedRefNo(),
					tripType, dayFive, tripFive, busFive);
		}
		if (success1 || success2 || success3 || success4 || success5) {

			RequestContext.getCurrentInstance().execute("PF('editRouteScheduleVAR').show()");
			clearEditDate();
			searchEditRouteScheduleData();
		}
	}

	public void clearEditDate() {
		dayOne = null;
		tripOne = null;
		busOne = null;

		dayTwo = null;
		tripTwo = null;
		busTwo = null;

		dayThree = null;
		tripThree = null;
		busThree = null;

		dayFour = null;
		tripFour = null;
		busFour = null;

		dayFive = null;
		tripFive = null;
		busFive = null;
		RequestContext.getCurrentInstance().execute("PF('editRouteScheduleVAR').show()");

	}

	/* create route schedule data table dynamically */
	public List<String> createMainDataTable() {

		List<String> VALID_COLUMN_KEYS = new ArrayList<>();

		for (int i = 1; i <= noOfDaysFortimeTable; i++) {
			VALID_COLUMN_KEYS.add(String.valueOf(i));
		}

		columns = new ArrayList<ColumnModel>();

		for (String columnKey : VALID_COLUMN_KEYS) {
			String key = columnKey;
			columns.add(new ColumnModel(key, "busNo" + key));
		}

		columns.add(0, new ColumnModel("#", "tripId"));

		return VALID_COLUMN_KEYS;
	}

	/* create route schedule leaves data table dynamically */
	public void createLeavesDataTable() {

		List<String> VALID_COLUMN_KEYS = new ArrayList<>();

		for (int i = 1; i <= noOfDaysFortimeTable; i++) {
			VALID_COLUMN_KEYS.add(String.valueOf(i));
		}

		columnsLeaves = new ArrayList<ColumnModel>();

		for (String columnKey : VALID_COLUMN_KEYS) {
			String key = columnKey;
			columnsLeaves.add(new ColumnModel(key, "busNo" + key));
		}

		columnsLeaves.add(0, new ColumnModel("#", "tripId"));

	}

	/* create route schedule leaves data table dynamically */
	public void createEditDataTable() {

		List<String> VALID_COLUMN_KEYS = new ArrayList<>();

		for (int i = 1; i <= noOfDaysForEdit; i++) {
			VALID_COLUMN_KEYS.add(String.valueOf(i));
		}

		editColumns = new ArrayList<ColumnModel>();

		for (String columnKey : VALID_COLUMN_KEYS) {
			String key = columnKey;
			editColumns.add(new ColumnModel(key, "busNo" + key));
		}

		editColumns.add(0, new ColumnModel("#", "tripId"));

	}

	/* DTO for Data Tables */
	static public class ColumnModel implements Serializable {

		private static final long serialVersionUID = 1L;
		private String header;
		private String property;

		public ColumnModel(String header, String property) {
			this.header = header;
			this.property = property;
		}

		public String getHeader() {
			return header;
		}

		public String getProperty() {
			return property;
		}
	}

	private RouteScheduleDTO setBusToRouteScheduleDTOs(List<String> stringBusNoList) {
		String separator = "-";
		int trip_id = ++this.tripID;

		String id = String.valueOf(trip_id);

		RouteScheduleDTO busNoDTO = new RouteScheduleDTO();

		if (stringBusNoList.size() == maximumDays) {

			busNoDTO.setTripId(id);

			if (stringBusNoList.get(0) != null && !stringBusNoList.get(0).isEmpty()) {
				String[] bus1 = stringBusNoList.get(0).split("-");
				busNoDTO.setBusNo1(stringBusNoList.get(0).substring(0, stringBusNoList.get(0).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq1(bus1[bus1.length - 1]);
			}

			if (stringBusNoList.get(1) != null && !stringBusNoList.get(1).isEmpty()) {
				String[] bus2 = stringBusNoList.get(1).split("-");
				busNoDTO.setBusNo2(stringBusNoList.get(1).substring(0, stringBusNoList.get(1).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq2(bus2[bus2.length - 1]);
			}

			if (stringBusNoList.get(2) != null && !stringBusNoList.get(2).isEmpty()) {
				String[] bus3 = stringBusNoList.get(2).split("-");
				busNoDTO.setBusNo3(stringBusNoList.get(2).substring(0, stringBusNoList.get(2).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq3(bus3[bus3.length - 1]);
			}

			if (stringBusNoList.get(3) != null && !stringBusNoList.get(3).isEmpty()) {
				String[] bus4 = stringBusNoList.get(3).split("-");
				busNoDTO.setBusNo4(stringBusNoList.get(3).substring(0, stringBusNoList.get(3).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq4(bus4[bus4.length - 1]);
			}

			if (stringBusNoList.get(4) != null && !stringBusNoList.get(4).isEmpty()) {
				String[] bus5 = stringBusNoList.get(4).split("-");
				busNoDTO.setBusNo5(stringBusNoList.get(4).substring(0, stringBusNoList.get(4).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq5(bus5[bus5.length - 1]);
			}

			if (stringBusNoList.get(5) != null && !stringBusNoList.get(5).isEmpty()) {
				String[] bus6 = stringBusNoList.get(5).split("-");
				busNoDTO.setBusNo6(stringBusNoList.get(5).substring(0, stringBusNoList.get(5).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq6(bus6[bus6.length - 1]);
			}

			if (stringBusNoList.get(6) != null && !stringBusNoList.get(6).isEmpty()) {
				String[] bus7 = stringBusNoList.get(6).split("-");
				busNoDTO.setBusNo7(stringBusNoList.get(6).substring(0, stringBusNoList.get(6).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq7(bus7[bus7.length - 1]);
			}

			if (stringBusNoList.get(7) != null && !stringBusNoList.get(7).isEmpty()) {
				String[] bus8 = stringBusNoList.get(7).split("-");
				busNoDTO.setBusNo8(stringBusNoList.get(7).substring(0, stringBusNoList.get(7).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq8(bus8[bus8.length - 1]);
			}

			if (stringBusNoList.get(8) != null && !stringBusNoList.get(8).isEmpty()) {
				String[] bus9 = stringBusNoList.get(8).split("-");
				busNoDTO.setBusNo9(stringBusNoList.get(8).substring(0, stringBusNoList.get(8).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq9(bus9[bus9.length - 1]);
			}

			if (stringBusNoList.get(9) != null && !stringBusNoList.get(9).isEmpty()) {
				String[] bus10 = stringBusNoList.get(9).split("-");
				busNoDTO.setBusNo10(stringBusNoList.get(9).substring(0, stringBusNoList.get(9).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq10(bus10[bus10.length - 1]);
			}

			if (stringBusNoList.get(10) != null && !stringBusNoList.get(10).isEmpty()) {
				String[] bus11 = stringBusNoList.get(10).split("-");
				busNoDTO.setBusNo11(
						stringBusNoList.get(10).substring(0, stringBusNoList.get(10).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq11(bus11[bus11.length - 1]);
			}

			if (stringBusNoList.get(11) != null && !stringBusNoList.get(11).isEmpty()) {
				String[] bus12 = stringBusNoList.get(11).split("-");
				busNoDTO.setBusNo12(
						stringBusNoList.get(11).substring(0, stringBusNoList.get(11).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq12(bus12[bus12.length - 1]);
			}

			if (stringBusNoList.get(12) != null && !stringBusNoList.get(12).isEmpty()) {
				String[] bus13 = stringBusNoList.get(12).split("-");
				busNoDTO.setBusNo13(
						stringBusNoList.get(12).substring(0, stringBusNoList.get(12).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq13(bus13[bus13.length - 1]);
			}

			if (stringBusNoList.get(13) != null && !stringBusNoList.get(13).isEmpty()) {
				String[] bus14 = stringBusNoList.get(13).split("-");
				busNoDTO.setBusNo14(
						stringBusNoList.get(13).substring(0, stringBusNoList.get(13).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq14(bus14[bus14.length - 1]);
			}

			if (stringBusNoList.get(14) != null && !stringBusNoList.get(14).isEmpty()) {
				String[] bus15 = stringBusNoList.get(14).split("-");
				busNoDTO.setBusNo15(
						stringBusNoList.get(14).substring(0, stringBusNoList.get(14).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq15(bus15[bus15.length - 1]);
			}

			if (stringBusNoList.get(15) != null && !stringBusNoList.get(15).isEmpty()) {
				String[] bus16 = stringBusNoList.get(15).split("-");
				busNoDTO.setBusNo16(
						stringBusNoList.get(15).substring(0, stringBusNoList.get(15).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq16(bus16[bus16.length - 1]);
			}

			if (stringBusNoList.get(16) != null && !stringBusNoList.get(16).isEmpty()) {
				String[] bus17 = stringBusNoList.get(16).split("-");
				busNoDTO.setBusNo17(
						stringBusNoList.get(16).substring(0, stringBusNoList.get(16).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq17(bus17[bus17.length - 1]);
			}

			if (stringBusNoList.get(17) != null && !stringBusNoList.get(17).isEmpty()) {
				String[] bus18 = stringBusNoList.get(17).split("-");
				busNoDTO.setBusNo18(
						stringBusNoList.get(17).substring(0, stringBusNoList.get(17).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq18(bus18[bus18.length - 1]);
			}

			if (stringBusNoList.get(18) != null && !stringBusNoList.get(18).isEmpty()) {
				String[] bus19 = stringBusNoList.get(18).split("-");
				busNoDTO.setBusNo19(
						stringBusNoList.get(18).substring(0, stringBusNoList.get(18).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq19(bus19[bus19.length - 1]);
			}

			if (stringBusNoList.get(19) != null && !stringBusNoList.get(19).isEmpty()) {
				String[] bus20 = stringBusNoList.get(19).split("-");
				busNoDTO.setBusNo20(
						stringBusNoList.get(19).substring(0, stringBusNoList.get(19).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq20(bus20[bus20.length - 1]);
			}

			if (stringBusNoList.get(20) != null && !stringBusNoList.get(20).isEmpty()) {
				String[] bus21 = stringBusNoList.get(20).split("-");
				busNoDTO.setBusNo21(
						stringBusNoList.get(20).substring(0, stringBusNoList.get(20).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq21(bus21[bus21.length - 1]);
			}

			if (stringBusNoList.get(21) != null && !stringBusNoList.get(21).isEmpty()) {
				String[] bus22 = stringBusNoList.get(21).split("-");
				busNoDTO.setBusNo22(
						stringBusNoList.get(21).substring(0, stringBusNoList.get(21).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq22(bus22[bus22.length - 1]);
			}

			if (stringBusNoList.get(22) != null && !stringBusNoList.get(22).isEmpty()) {
				String[] bus23 = stringBusNoList.get(22).split("-");
				busNoDTO.setBusNo23(
						stringBusNoList.get(22).substring(0, stringBusNoList.get(22).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq23(bus23[bus23.length - 1]);
			}

			if (stringBusNoList.get(23) != null && !stringBusNoList.get(23).isEmpty()) {
				String[] bus24 = stringBusNoList.get(23).split("-");
				busNoDTO.setBusNo24(
						stringBusNoList.get(23).substring(0, stringBusNoList.get(23).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq24(bus24[bus24.length - 1]);
			}

			if (stringBusNoList.get(24) != null && !stringBusNoList.get(24).isEmpty()) {
				String[] bus25 = stringBusNoList.get(24).split("-");
				busNoDTO.setBusNo25(
						stringBusNoList.get(24).substring(0, stringBusNoList.get(24).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq25(bus25[bus25.length - 1]);
			}

			if (stringBusNoList.get(25) != null && !stringBusNoList.get(25).isEmpty()) {
				String[] bus26 = stringBusNoList.get(25).split("-");
				busNoDTO.setBusNo26(
						stringBusNoList.get(25).substring(0, stringBusNoList.get(25).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq26(bus26[bus26.length - 1]);
			}

			if (stringBusNoList.get(26) != null && !stringBusNoList.get(26).isEmpty()) {
				String[] bus27 = stringBusNoList.get(26).split("-");
				busNoDTO.setBusNo27(
						stringBusNoList.get(26).substring(0, stringBusNoList.get(26).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq27(bus27[bus27.length - 1]);
			}

			if (stringBusNoList.get(27) != null && !stringBusNoList.get(27).isEmpty()) {
				String[] bus28 = stringBusNoList.get(27).split("-");
				busNoDTO.setBusNo28(
						stringBusNoList.get(27).substring(0, stringBusNoList.get(27).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq28(bus28[bus28.length - 1]);
			}

			if (stringBusNoList.get(28) != null && !stringBusNoList.get(28).isEmpty()) {
				String[] bus29 = stringBusNoList.get(28).split("-");
				busNoDTO.setBusNo29(
						stringBusNoList.get(28).substring(0, stringBusNoList.get(28).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq29(bus29[bus29.length - 1]);
			}

			if (stringBusNoList.get(29) != null && !stringBusNoList.get(29).isEmpty()) {
				String[] bus30 = stringBusNoList.get(29).split("-");
				busNoDTO.setBusNo30(
						stringBusNoList.get(29).substring(0, stringBusNoList.get(29).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq30(bus30[bus30.length - 1]);
			}

			if (stringBusNoList.get(30) != null && !stringBusNoList.get(30).isEmpty()) {
				String[] bus31 = stringBusNoList.get(30).split("-");
				busNoDTO.setBusNo31(
						stringBusNoList.get(30).substring(0, stringBusNoList.get(30).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq31(bus31[bus31.length - 1]);
			}

			if (stringBusNoList.get(31) != null && !stringBusNoList.get(31).isEmpty()) {
				String[] bus32 = stringBusNoList.get(31).split("-");
				busNoDTO.setBusNo32(
						stringBusNoList.get(31).substring(0, stringBusNoList.get(31).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq32(bus32[bus32.length - 1]);
			}

			if (stringBusNoList.get(32) != null && !stringBusNoList.get(32).isEmpty()) {
				String[] bus33 = stringBusNoList.get(32).split("-");
				busNoDTO.setBusNo33(
						stringBusNoList.get(32).substring(0, stringBusNoList.get(32).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq33(bus33[bus33.length - 1]);
			}

			if (stringBusNoList.get(33) != null && !stringBusNoList.get(33).isEmpty()) {
				String[] bus34 = stringBusNoList.get(33).split("-");
				busNoDTO.setBusNo34(
						stringBusNoList.get(33).substring(0, stringBusNoList.get(33).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq34(bus34[bus34.length - 1]);
			}

			if (stringBusNoList.get(34) != null && !stringBusNoList.get(34).isEmpty()) {
				String[] bus35 = stringBusNoList.get(34).split("-");
				busNoDTO.setBusNo35(
						stringBusNoList.get(34).substring(0, stringBusNoList.get(34).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq35(bus35[bus35.length - 1]);
			}

			if (stringBusNoList.get(35) != null && !stringBusNoList.get(35).isEmpty()) {
				String[] bus36 = stringBusNoList.get(35).split("-");
				busNoDTO.setBusNo36(
						stringBusNoList.get(35).substring(0, stringBusNoList.get(35).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq36(bus36[bus36.length - 1]);
			}

			if (stringBusNoList.get(36) != null && !stringBusNoList.get(36).isEmpty()) {
				String[] bus37 = stringBusNoList.get(36).split("-");
				busNoDTO.setBusNo37(
						stringBusNoList.get(36).substring(0, stringBusNoList.get(36).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq37(bus37[bus37.length - 1]);
			}

			if (stringBusNoList.get(37) != null && !stringBusNoList.get(37).isEmpty()) {
				String[] bus38 = stringBusNoList.get(37).split("-");
				busNoDTO.setBusNo38(
						stringBusNoList.get(37).substring(0, stringBusNoList.get(37).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq38(bus38[bus38.length - 1]);
			}

			if (stringBusNoList.get(38) != null && !stringBusNoList.get(38).isEmpty()) {
				String[] bus39 = stringBusNoList.get(38).split("-");
				busNoDTO.setBusNo39(
						stringBusNoList.get(38).substring(0, stringBusNoList.get(38).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq39(bus39[bus39.length - 1]);
			}

			if (stringBusNoList.get(39) != null && !stringBusNoList.get(39).isEmpty()) {
				String[] bus40 = stringBusNoList.get(39).split("-");
				busNoDTO.setBusNo40(
						stringBusNoList.get(39).substring(0, stringBusNoList.get(39).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq40(bus40[bus40.length - 1]);
			}

			if (stringBusNoList.get(40) != null && !stringBusNoList.get(40).isEmpty()) {
				String[] bus41 = stringBusNoList.get(40).split("-");
				busNoDTO.setBusNo41(
						stringBusNoList.get(40).substring(0, stringBusNoList.get(40).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq41(bus41[bus41.length - 1]);
			}

			if (stringBusNoList.get(41) != null && !stringBusNoList.get(41).isEmpty()) {
				String[] bus42 = stringBusNoList.get(41).split("-");
				busNoDTO.setBusNo42(
						stringBusNoList.get(41).substring(0, stringBusNoList.get(41).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq42(bus42[bus42.length - 1]);
			}

			if (stringBusNoList.get(42) != null && !stringBusNoList.get(42).isEmpty()) {
				String[] bus43 = stringBusNoList.get(42).split("-");
				busNoDTO.setBusNo43(
						stringBusNoList.get(42).substring(0, stringBusNoList.get(42).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq43(bus43[bus43.length - 1]);
			}

			if (stringBusNoList.get(43) != null && !stringBusNoList.get(43).isEmpty()) {
				String[] bus44 = stringBusNoList.get(43).split("-");
				busNoDTO.setBusNo44(
						stringBusNoList.get(43).substring(0, stringBusNoList.get(43).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq44(bus44[bus44.length - 1]);
			}

			if (stringBusNoList.get(44) != null && !stringBusNoList.get(44).isEmpty()) {
				String[] bus45 = stringBusNoList.get(44).split("-");
				busNoDTO.setBusNo45(
						stringBusNoList.get(44).substring(0, stringBusNoList.get(44).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq45(bus45[bus45.length - 1]);
			}

			if (stringBusNoList.get(45) != null && !stringBusNoList.get(45).isEmpty()) {
				String[] bus46 = stringBusNoList.get(45).split("-");
				busNoDTO.setBusNo46(
						stringBusNoList.get(45).substring(0, stringBusNoList.get(45).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq46(bus46[bus46.length - 1]);
			}

			if (stringBusNoList.get(46) != null && !stringBusNoList.get(46).isEmpty()) {
				String[] bus47 = stringBusNoList.get(46).split("-");
				busNoDTO.setBusNo47(
						stringBusNoList.get(46).substring(0, stringBusNoList.get(46).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq47(bus47[bus47.length - 1]);
			}

			if (stringBusNoList.get(47) != null && !stringBusNoList.get(47).isEmpty()) {
				String[] bus48 = stringBusNoList.get(47).split("-");
				busNoDTO.setBusNo48(
						stringBusNoList.get(47).substring(0, stringBusNoList.get(47).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq48(bus48[bus48.length - 1]);
			}

			if (stringBusNoList.get(48) != null && !stringBusNoList.get(48).isEmpty()) {
				String[] bus49 = stringBusNoList.get(48).split("-");
				busNoDTO.setBusNo49(
						stringBusNoList.get(48).substring(0, stringBusNoList.get(48).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq49(bus49[bus49.length - 1]);
			}

			if (stringBusNoList.get(49) != null && !stringBusNoList.get(49).isEmpty()) {
				String[] bus50 = stringBusNoList.get(49).split("-");
				busNoDTO.setBusNo50(
						stringBusNoList.get(49).substring(0, stringBusNoList.get(49).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq50(bus50[bus50.length - 1]);
			}

			if (stringBusNoList.get(50) != null && !stringBusNoList.get(50).isEmpty()) {
				String[] bus51 = stringBusNoList.get(50).split("-");
				busNoDTO.setBusNo51(
						stringBusNoList.get(50).substring(0, stringBusNoList.get(50).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq51(bus51[bus51.length - 1]);
			}

			if (stringBusNoList.get(51) != null && !stringBusNoList.get(51).isEmpty()) {
				String[] bus52 = stringBusNoList.get(51).split("-");
				busNoDTO.setBusNo52(
						stringBusNoList.get(51).substring(0, stringBusNoList.get(51).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq52(bus52[bus52.length - 1]);
			}

			if (stringBusNoList.get(52) != null && !stringBusNoList.get(52).isEmpty()) {
				String[] bus53 = stringBusNoList.get(52).split("-");
				busNoDTO.setBusNo53(
						stringBusNoList.get(52).substring(0, stringBusNoList.get(52).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq53(bus53[bus53.length - 1]);
			}

			if (stringBusNoList.get(53) != null && !stringBusNoList.get(53).isEmpty()) {
				String[] bus54 = stringBusNoList.get(53).split("-");
				busNoDTO.setBusNo54(
						stringBusNoList.get(53).substring(0, stringBusNoList.get(53).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq54(bus54[bus54.length - 1]);
			}

			if (stringBusNoList.get(54) != null && !stringBusNoList.get(54).isEmpty()) {
				String[] bus55 = stringBusNoList.get(54).split("-");
				busNoDTO.setBusNo55(
						stringBusNoList.get(54).substring(0, stringBusNoList.get(54).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq55(bus55[bus55.length - 1]);
			}

			if (stringBusNoList.get(55) != null && !stringBusNoList.get(55).isEmpty()) {
				String[] bus56 = stringBusNoList.get(55).split("-");
				busNoDTO.setBusNo56(
						stringBusNoList.get(55).substring(0, stringBusNoList.get(55).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq56(bus56[bus56.length - 1]);
			}

			if (stringBusNoList.get(56) != null && !stringBusNoList.get(56).isEmpty()) {
				String[] bus57 = stringBusNoList.get(56).split("-");
				busNoDTO.setBusNo57(
						stringBusNoList.get(56).substring(0, stringBusNoList.get(56).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq57(bus57[bus57.length - 1]);
			}

			if (stringBusNoList.get(57) != null && !stringBusNoList.get(57).isEmpty()) {
				String[] bus58 = stringBusNoList.get(57).split("-");
				busNoDTO.setBusNo58(
						stringBusNoList.get(57).substring(0, stringBusNoList.get(57).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq58(bus58[bus58.length - 1]);
			}

			if (stringBusNoList.get(58) != null && !stringBusNoList.get(58).isEmpty()) {
				String[] bus59 = stringBusNoList.get(58).split("-");
				busNoDTO.setBusNo59(
						stringBusNoList.get(58).substring(0, stringBusNoList.get(58).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq59(bus59[bus59.length - 1]);
			}

			if (stringBusNoList.get(59) != null && !stringBusNoList.get(59).isEmpty()) {
				String[] bus60 = stringBusNoList.get(59).split("-");
				busNoDTO.setBusNo60(
						stringBusNoList.get(59).substring(0, stringBusNoList.get(59).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq60(bus60[bus60.length - 1]);
			}

			if (stringBusNoList.get(60) != null && !stringBusNoList.get(60).isEmpty()) {
				String[] bus61 = stringBusNoList.get(60).split("-");
				busNoDTO.setBusNo61(
						stringBusNoList.get(60).substring(0, stringBusNoList.get(60).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq61(bus61[bus61.length - 1]);
			}

			if (stringBusNoList.get(61) != null && !stringBusNoList.get(61).isEmpty()) {
				String[] bus62 = stringBusNoList.get(61).split("-");
				busNoDTO.setBusNo62(
						stringBusNoList.get(61).substring(0, stringBusNoList.get(61).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq62(bus62[bus62.length - 1]);
			}

			if (stringBusNoList.get(62) != null && !stringBusNoList.get(62).isEmpty()) {
				String[] bus63 = stringBusNoList.get(62).split("-");
				busNoDTO.setBusNo63(
						stringBusNoList.get(62).substring(0, stringBusNoList.get(62).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq63(bus63[bus63.length - 1]);
			}

			if (stringBusNoList.get(63) != null && !stringBusNoList.get(63).isEmpty()) {
				String[] bus64 = stringBusNoList.get(63).split("-");
				busNoDTO.setBusNo64(
						stringBusNoList.get(63).substring(0, stringBusNoList.get(63).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq64(bus64[bus64.length - 1]);
			}

			if (stringBusNoList.get(64) != null && !stringBusNoList.get(64).isEmpty()) {
				String[] bus65 = stringBusNoList.get(64).split("-");
				busNoDTO.setBusNo65(
						stringBusNoList.get(64).substring(0, stringBusNoList.get(64).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq65(bus65[bus65.length - 1]);
			}

			if (stringBusNoList.get(65) != null && !stringBusNoList.get(65).isEmpty()) {
				String[] bus66 = stringBusNoList.get(65).split("-");
				busNoDTO.setBusNo66(
						stringBusNoList.get(65).substring(0, stringBusNoList.get(65).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq66(bus66[bus66.length - 1]);
			}

			if (stringBusNoList.get(66) != null && !stringBusNoList.get(66).isEmpty()) {
				String[] bus67 = stringBusNoList.get(66).split("-");
				busNoDTO.setBusNo67(
						stringBusNoList.get(66).substring(0, stringBusNoList.get(66).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq67(bus67[bus67.length - 1]);
			}

			if (stringBusNoList.get(67) != null && !stringBusNoList.get(67).isEmpty()) {
				String[] bus68 = stringBusNoList.get(67).split("-");
				busNoDTO.setBusNo68(
						stringBusNoList.get(67).substring(0, stringBusNoList.get(67).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq68(bus68[bus68.length - 1]);
			}

			if (stringBusNoList.get(68) != null && !stringBusNoList.get(68).isEmpty()) {
				String[] bus69 = stringBusNoList.get(68).split("-");
				busNoDTO.setBusNo69(
						stringBusNoList.get(68).substring(0, stringBusNoList.get(68).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq69(bus69[bus69.length - 1]);
			}

			if (stringBusNoList.get(69) != null && !stringBusNoList.get(69).isEmpty()) {
				String[] bus70 = stringBusNoList.get(69).split("-");
				busNoDTO.setBusNo70(
						stringBusNoList.get(69).substring(0, stringBusNoList.get(69).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq70(bus70[bus70.length - 1]);
			}

			if (stringBusNoList.get(70) != null && !stringBusNoList.get(70).isEmpty()) {
				String[] bus71 = stringBusNoList.get(70).split("-");
				busNoDTO.setBusNo71(
						stringBusNoList.get(70).substring(0, stringBusNoList.get(70).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq71(bus71[bus71.length - 1]);
			}

			if (stringBusNoList.get(71) != null && !stringBusNoList.get(71).isEmpty()) {
				String[] bus72 = stringBusNoList.get(71).split("-");
				busNoDTO.setBusNo72(
						stringBusNoList.get(71).substring(0, stringBusNoList.get(71).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq72(bus72[bus72.length - 1]);
			}

			if (stringBusNoList.get(72) != null && !stringBusNoList.get(72).isEmpty()) {
				String[] bus73 = stringBusNoList.get(72).split("-");
				busNoDTO.setBusNo73(
						stringBusNoList.get(72).substring(0, stringBusNoList.get(72).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq73(bus73[bus73.length - 1]);
			}

			if (stringBusNoList.get(73) != null && !stringBusNoList.get(73).isEmpty()) {
				String[] bus74 = stringBusNoList.get(73).split("-");
				busNoDTO.setBusNo74(
						stringBusNoList.get(73).substring(0, stringBusNoList.get(73).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq74(bus74[bus74.length - 1]);
			}

			if (stringBusNoList.get(74) != null && !stringBusNoList.get(74).isEmpty()) {
				String[] bus75 = stringBusNoList.get(74).split("-");
				busNoDTO.setBusNo75(
						stringBusNoList.get(74).substring(0, stringBusNoList.get(74).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq75(bus75[bus75.length - 1]);
			}

			if (stringBusNoList.get(75) != null && !stringBusNoList.get(75).isEmpty()) {
				String[] bus76 = stringBusNoList.get(75).split("-");
				busNoDTO.setBusNo76(
						stringBusNoList.get(75).substring(0, stringBusNoList.get(75).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq76(bus76[bus76.length - 1]);
			}

			if (stringBusNoList.get(76) != null && !stringBusNoList.get(76).isEmpty()) {
				String[] bus77 = stringBusNoList.get(76).split("-");
				busNoDTO.setBusNo77(
						stringBusNoList.get(76).substring(0, stringBusNoList.get(76).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq77(bus77[bus77.length - 1]);
			}

			if (stringBusNoList.get(77) != null && !stringBusNoList.get(77).isEmpty()) {
				String[] bus78 = stringBusNoList.get(77).split("-");
				busNoDTO.setBusNo78(
						stringBusNoList.get(77).substring(0, stringBusNoList.get(77).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq78(bus78[bus78.length - 1]);
			}

			if (stringBusNoList.get(78) != null && !stringBusNoList.get(78).isEmpty()) {
				String[] bus79 = stringBusNoList.get(78).split("-");
				busNoDTO.setBusNo79(
						stringBusNoList.get(78).substring(0, stringBusNoList.get(78).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq79(bus79[bus79.length - 1]);
			}

			if (stringBusNoList.get(79) != null && !stringBusNoList.get(79).isEmpty()) {
				String[] bus80 = stringBusNoList.get(79).split("-");
				busNoDTO.setBusNo80(
						stringBusNoList.get(79).substring(0, stringBusNoList.get(79).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq80(bus80[bus80.length - 1]);
			}

			if (stringBusNoList.get(80) != null && !stringBusNoList.get(80).isEmpty()) {
				String[] bus81 = stringBusNoList.get(80).split("-");
				busNoDTO.setBusNo81(
						stringBusNoList.get(80).substring(0, stringBusNoList.get(80).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq81(bus81[bus81.length - 1]);
			}

			if (stringBusNoList.get(81) != null && !stringBusNoList.get(81).isEmpty()) {
				String[] bus82 = stringBusNoList.get(81).split("-");
				busNoDTO.setBusNo82(
						stringBusNoList.get(81).substring(0, stringBusNoList.get(81).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq82(bus82[bus82.length - 1]);
			}

			if (stringBusNoList.get(82) != null && !stringBusNoList.get(82).isEmpty()) {
				String[] bus83 = stringBusNoList.get(82).split("-");
				busNoDTO.setBusNo83(
						stringBusNoList.get(82).substring(0, stringBusNoList.get(82).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq83(bus83[bus83.length - 1]);
			}

			if (stringBusNoList.get(83) != null && !stringBusNoList.get(83).isEmpty()) {
				String[] bus84 = stringBusNoList.get(83).split("-");
				busNoDTO.setBusNo84(
						stringBusNoList.get(83).substring(0, stringBusNoList.get(83).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq84(bus84[bus84.length - 1]);
			}

			if (stringBusNoList.get(84) != null && !stringBusNoList.get(84).isEmpty()) {
				String[] bus85 = stringBusNoList.get(84).split("-");
				busNoDTO.setBusNo85(
						stringBusNoList.get(84).substring(0, stringBusNoList.get(84).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq85(bus85[bus85.length - 1]);
			}

			if (stringBusNoList.get(85) != null && !stringBusNoList.get(85).isEmpty()) {
				String[] bus86 = stringBusNoList.get(85).split("-");
				busNoDTO.setBusNo86(
						stringBusNoList.get(85).substring(0, stringBusNoList.get(85).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq86(bus86[bus86.length - 1]);
			}

			if (stringBusNoList.get(86) != null && !stringBusNoList.get(86).isEmpty()) {
				String[] bus87 = stringBusNoList.get(86).split("-");
				busNoDTO.setBusNo87(
						stringBusNoList.get(86).substring(0, stringBusNoList.get(86).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq87(bus87[bus87.length - 1]);
			}

			if (stringBusNoList.get(87) != null && !stringBusNoList.get(87).isEmpty()) {
				String[] bus88 = stringBusNoList.get(87).split("-");
				busNoDTO.setBusNo88(
						stringBusNoList.get(87).substring(0, stringBusNoList.get(87).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq88(bus88[bus88.length - 1]);
			}

			if (stringBusNoList.get(88) != null && !stringBusNoList.get(88).isEmpty()) {
				String[] bus89 = stringBusNoList.get(88).split("-");
				busNoDTO.setBusNo89(
						stringBusNoList.get(88).substring(0, stringBusNoList.get(88).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq89(bus89[bus89.length - 1]);
			}

			if (stringBusNoList.get(89) != null && !stringBusNoList.get(89).isEmpty()) {
				String[] bus90 = stringBusNoList.get(89).split("-");
				busNoDTO.setBusNo90(
						stringBusNoList.get(89).substring(0, stringBusNoList.get(89).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq90(bus90[bus90.length - 1]);
			}

			if (stringBusNoList.get(90) != null && !stringBusNoList.get(90).isEmpty()) {
				String[] bus91 = stringBusNoList.get(90).split("-");
				busNoDTO.setBusNo91(
						stringBusNoList.get(90).substring(0, stringBusNoList.get(90).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq91(bus91[bus91.length - 1]);
			}

			if (stringBusNoList.get(91) != null && !stringBusNoList.get(91).isEmpty()) {
				String[] bus92 = stringBusNoList.get(91).split("-");
				busNoDTO.setBusNo92(
						stringBusNoList.get(91).substring(0, stringBusNoList.get(91).lastIndexOf(separator)));
				busNoDTO.setBusNoSeq92(bus92[bus92.length - 1]);
			}

		} else {
			setErrorMessage("Can not continue the flow. Buses found only for " + stringBusNoList.size() + " days");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

		return busNoDTO;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public RouteScheduleService getRouteScheduleService() {
		return routeScheduleService;
	}

	public void setRouteScheduleService(RouteScheduleService routeScheduleService) {
		this.routeScheduleService = routeScheduleService;
	}

	public List<RouteScheduleDTO> getGroupNoList() {
		return groupNoList;
	}

	public void setGroupNoList(List<RouteScheduleDTO> groupNoList) {
		this.groupNoList = groupNoList;
	}

	public RouteScheduleDTO getRouteScheduleDTO() {
		return routeScheduleDTO;
	}

	public void setRouteScheduleDTO(RouteScheduleDTO routeScheduleDTO) {
		this.routeScheduleDTO = routeScheduleDTO;
	}

	public List<RouteScheduleDTO> getBusRouteList() {
		return busRouteList;
	}

	public void setBusRouteList(List<RouteScheduleDTO> busRouteList) {
		this.busRouteList = busRouteList;
	}

	public List<String> getLeavePositionList() {
		return leavePositionList;
	}

	public void setLeavePositionList(List<String> leavePositionList) {
		this.leavePositionList = leavePositionList;
	}

	public int getTripID() {
		return tripID;
	}

	public void setTripID(int tripID) {
		this.tripID = tripID;
	}

	public int getMaximumDays() {
		return maximumDays;
	}

	public List<RouteScheduleDTO> getSelectLeavePositionList() {
		return selectLeavePositionList;
	}

	public void setSelectLeavePositionList(List<RouteScheduleDTO> selectLeavePositionList) {
		this.selectLeavePositionList = selectLeavePositionList;
	}

	public boolean isRenderPanelTwo() {
		return renderPanelTwo;
	}

	public void setRenderPanelTwo(boolean renderPanelTwo) {
		this.renderPanelTwo = renderPanelTwo;
	}

	public boolean isDisableGroupNo() {
		return disableGroupNo;
	}

	public void setDisableGroupNo(boolean disableGroupNo) {
		this.disableGroupNo = disableGroupNo;
	}

	public List<RouteScheduleDTO> getBusCategoryList() {
		return busCategoryList;
	}

	public void setBusCategoryList(List<RouteScheduleDTO> busCategoryList) {
		this.busCategoryList = busCategoryList;
	}

	public boolean isDisableBusCategory() {
		return disableBusCategory;
	}

	public void setDisableBusCategory(boolean disableBusCategory) {
		this.disableBusCategory = disableBusCategory;
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

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
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

	public String getTripType() {
		return tripType;
	}

	public void setTripType(String tripType) {
		this.tripType = tripType;
	}

	public int getNoOfLeaves() {
		return noOfLeaves;
	}

	public void setNoOfLeaves(int noOfLeaves) {
		this.noOfLeaves = noOfLeaves;
	}

	public int getNoOfBuses() {
		return noOfBuses;
	}

	public void setNoOfBuses(int noOfBuses) {
		this.noOfBuses = noOfBuses;
	}

	public int getNoOfTrips() {
		return noOfTrips;
	}

	public void setNoOfTrips(int noOfTrips) {
		this.noOfTrips = noOfTrips;
	}

	public String getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}

	public int getMaximumLeaveForDay() {
		return maximumLeaveForDay;
	}

	public boolean isDisabledNoramlRotation() {
		return disabledNoramlRotation;
	}

	public void setDisabledNoramlRotation(boolean disabledNoramlRotation) {
		this.disabledNoramlRotation = disabledNoramlRotation;
	}

	public boolean isDisabledZigzagRotation() {
		return disabledZigzagRotation;
	}

	public void setDisabledZigzagRotation(boolean disabledZigzagRotation) {
		this.disabledZigzagRotation = disabledZigzagRotation;
	}

	public List<RouteScheduleDTO> getBusForRouteList() {
		return busForRouteList;
	}

	public void setBusForRouteList(List<RouteScheduleDTO> busForRouteList) {
		this.busForRouteList = busForRouteList;
	}

	public List<ColumnModel> getColumns() {
		return columns;
	}

	public void setColumns(List<ColumnModel> columns) {
		this.columns = columns;
	}

	public int getNoOfDaysFortimeTable() {
		return noOfDaysFortimeTable;
	}

	public void setNoOfDaysFortimeTable(int noOfDaysFortimeTable) {
		this.noOfDaysFortimeTable = noOfDaysFortimeTable;
	}

	public int getNoOfTripsForSelectedSide() {
		return noOfTripsForSelectedSide;
	}

	public void setNoOfTripsForSelectedSide(int noOfTripsForSelectedSide) {
		this.noOfTripsForSelectedSide = noOfTripsForSelectedSide;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<RouteScheduleDTO> getLeaveForRouteList() {
		return leaveForRouteList;
	}

	public void setLeaveForRouteList(List<RouteScheduleDTO> leaveForRouteList) {
		this.leaveForRouteList = leaveForRouteList;
	}

	public List<ColumnModel> getColumnsLeaves() {
		return columnsLeaves;
	}

	public void setColumnsLeaves(List<ColumnModel> columnsLeaves) {
		this.columnsLeaves = columnsLeaves;
	}

	public boolean isDisabledgenerateReport() {
		return disabledgenerateReport;
	}

	public void setDisabledgenerateReport(boolean disabledgenerateReport) {
		this.disabledgenerateReport = disabledgenerateReport;
	}

	public boolean isDisabledClear() {
		return disabledClear;
	}

	public void setDisabledClear(boolean disabledClear) {
		this.disabledClear = disabledClear;
	}

	public boolean isDisabledSave() {
		return disabledSave;
	}

	public void setDisabledSave(boolean disabledSave) {
		this.disabledSave = disabledSave;
	}

	public boolean isDisabledSwap() {
		return disabledSwap;
	}

	public void setDisabledSwap(boolean disabledSwap) {
		this.disabledSwap = disabledSwap;
	}

	public boolean isRenderTableOne() {
		return renderTableOne;
	}

	public void setRenderTableOne(boolean renderTableOne) {
		this.renderTableOne = renderTableOne;
	}

	public boolean isRenderTableTwo() {
		return renderTableTwo;
	}

	public void setRenderTableTwo(boolean renderTableTwo) {
		this.renderTableTwo = renderTableTwo;
	}

	public List<RouteScheduleDTO> getMainSaveList() {
		return mainSaveList;
	}

	public void setMainSaveList(List<RouteScheduleDTO> mainSaveList) {
		this.mainSaveList = mainSaveList;
	}

	public String getRotationType() {
		return rotationType;
	}

	public void setRotationType(String rotationType) {
		this.rotationType = rotationType;
	}

	public boolean isDisabledleavePositionList() {
		return disabledleavePositionList;
	}

	public void setDisabledleavePositionList(boolean disabledleavePositionList) {
		this.disabledleavePositionList = disabledleavePositionList;
	}

	public List<RouteScheduleDTO> getEditBusForRouteList() {
		return editBusForRouteList;
	}

	public void setEditBusForRouteList(List<RouteScheduleDTO> editBusForRouteList) {
		this.editBusForRouteList = editBusForRouteList;
	}

	public List<ColumnModel> getEditColumns() {
		return editColumns;
	}

	public void setEditColumns(List<ColumnModel> editColumns) {
		this.editColumns = editColumns;
	}

	public int getNoOfDaysForEdit() {
		return noOfDaysForEdit;
	}

	public void setNoOfDaysForEdit(int noOfDaysForEdit) {
		this.noOfDaysForEdit = noOfDaysForEdit;
	}

	public List<RouteScheduleDTO> getTempEditBusForRouteList() {
		return tempEditBusForRouteList;
	}

	public void setTempEditBusForRouteList(List<RouteScheduleDTO> tempEditBusForRouteList) {
		this.tempEditBusForRouteList = tempEditBusForRouteList;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public boolean isOriginDataVisible() {
		return originDataVisible;
	}

	public void setOriginDataVisible(boolean originDataVisible) {
		this.originDataVisible = originDataVisible;
	}

	public boolean isDestinationDataVisible() {
		return destinationDataVisible;
	}

	public void setDestinationDataVisible(boolean destinationDataVisible) {
		this.destinationDataVisible = destinationDataVisible;
	}

	public boolean isLastPanelDateVisible() {
		return lastPanelDateVisible;
	}

	public void setLastPanelDateVisible(boolean lastPanelDateVisible) {
		this.lastPanelDateVisible = lastPanelDateVisible;
	}

	public Date getSelectedEndDate() {
		return selectedEndDate;
	}

	public void setSelectedEndDate(Date selectedEndDate) {
		this.selectedEndDate = selectedEndDate;
	}

	public Date getSelectedStartDate() {
		return selectedStartDate;
	}

	public void setSelectedStartDate(Date selectedStartDate) {
		this.selectedStartDate = selectedStartDate;
	}

	public List<String> getDateList() {
		return dateList;
	}

	public void setDateList(List<String> dateList) {
		this.dateList = dateList;
	}

	public String getSelectedDateRange() {
		return selectedDateRange;
	}

	public void setSelectedDateRange(String selectedDateRange) {
		this.selectedDateRange = selectedDateRange;
	}

	public boolean isAfterSearch() {
		return afterSearch;
	}

	public void setAfterSearch(boolean afterSearch) {
		this.afterSearch = afterSearch;
	}

	public String getDayOne() {
		return dayOne;
	}

	public void setDayOne(String dayOne) {
		this.dayOne = dayOne;
	}

	public String getDayTwo() {
		return dayTwo;
	}

	public void setDayTwo(String dayTwo) {
		this.dayTwo = dayTwo;
	}

	public String getDayThree() {
		return dayThree;
	}

	public void setDayThree(String dayThree) {
		this.dayThree = dayThree;
	}

	public String getDayFour() {
		return dayFour;
	}

	public void setDayFour(String dayFour) {
		this.dayFour = dayFour;
	}

	public String getDayFive() {
		return dayFive;
	}

	public void setDayFive(String dayFive) {
		this.dayFive = dayFive;
	}

	public String getTripOne() {
		return tripOne;
	}

	public void setTripOne(String tripOne) {
		this.tripOne = tripOne;
	}

	public String getTripThree() {
		return tripThree;
	}

	public void setTripThree(String tripThree) {
		this.tripThree = tripThree;
	}

	public String getTripFour() {
		return tripFour;
	}

	public void setTripFour(String tripFour) {
		this.tripFour = tripFour;
	}

	public String getTripFive() {
		return tripFive;
	}

	public void setTripFive(String tripFive) {
		this.tripFive = tripFive;
	}

	public String getBusOne() {
		return busOne;
	}

	public void setBusOne(String busOne) {
		this.busOne = busOne;
	}

	public String getBusTwo() {
		return busTwo;
	}

	public void setBusTwo(String busTwo) {
		this.busTwo = busTwo;
	}

	public String getBusThree() {
		return busThree;
	}

	public void setBusThree(String busThree) {
		this.busThree = busThree;
	}

	public String getBusFour() {
		return busFour;
	}

	public void setBusFour(String busFour) {
		this.busFour = busFour;
	}

	public String getBusFive() {
		return busFive;
	}

	public void setBusFive(String busFive) {
		this.busFive = busFive;
	}

	public String getTripTwo() {
		return tripTwo;
	}

	public void setTripTwo(String tripTwo) {
		this.tripTwo = tripTwo;
	}

	public Date getLastPanelStartDateNew() {
		return lastPanelStartDateNew;
	}

	public void setLastPanelStartDateNew(Date lastPanelStartDateNew) {
		this.lastPanelStartDateNew = lastPanelStartDateNew;
	}

}
