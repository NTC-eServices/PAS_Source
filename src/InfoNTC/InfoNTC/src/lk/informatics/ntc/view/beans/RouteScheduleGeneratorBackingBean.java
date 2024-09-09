package lk.informatics.ntc.view.beans;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.axis2.databinding.types.soapencoding.Array;
import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.RouteScheduleDTO;
import lk.informatics.ntc.model.dto.RouteScheduleHelperDTO;
import lk.informatics.ntc.model.dto.TimeTableDTO;
import lk.informatics.ntc.model.service.RouteScheduleService;
import lk.informatics.ntc.model.service.TimeTableService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "routeScheduleGeneratorBean")
@ViewScoped
public class RouteScheduleGeneratorBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	private RouteScheduleService routeScheduleService;
	private TimeTableService timeTableService;
	private RouteScheduleDTO routeScheduleDTO;
	private List<RouteScheduleDTO> busRouteList, groupNoList, selectLeavePositionList, busCategoryList, busForRouteList,
			leaveForRouteList, mainSaveList, editBusForRouteList, tempEditBusForRouteList, editBusForRouteListLeave,
			editBusForRouteListTwoDay, editBusForRouteListTwoDayLeave, selectLeavePositionListTwoDay;
	private List<RouteScheduleDTO> mainSaveForTwoDay, busForRouteListForTwoDay, selectedleaveForRouteList,
			selectedleaveForRouteListNextSide;
	private List<String> leavePositionList;
	private List<String> leavePositionListTwoDay;
	private List<String> fullBusNoList;
	private List<Integer> dayList;
	private List<ColumnModel> columns, columnsLeaves, editColumns, editColumnsLeave, editColumnsTwoDay,
			editColumnsTwoDayLeave;
	private boolean renderPanelTwo, disableGroupNo, disableBusCategory;
	private String alertMSG, successMessage, errorMessage, origin, destination, groupNo;
	private String tripType, rotationType; // Type "O" for origin and "D" for
											// destination
	private int noOfLeaves, noOfBuses, noOfTrips, noOfDaysFortimeTable, noOfTripsForSelectedSide, tripID,
			noOfDaysForEdit;
	private boolean disabledNoramlRotation, disabledZigzagRotation, renderTableOne, renderTableTwo, renderTableThree,
			disabledgenerateReport, disabledClear, disabledSave, disabledSwap, disabledleavePositionList;

	private int maximumDays;

	private boolean edit;
	private boolean originDataVisible;
	private boolean destinationDataVisible;
	private boolean lastPanelDateVisible;

	private Date selectedStartDate;
	private Date selectedEndDate;
	private List<String> dateList;
	private String selectedDateRange;
	int dateCount = 0;
	private boolean afterSearch = false;
	private String dayOne, dayTwo, dayThree, dayFour, dayFive;
	private String tripOne, tripTwo, tripThree, tripFour, tripFive;
	private String busOne, busTwo, busThree, busFour, busFive;
	private int coupleCount = 0;
	private Date lastPanelStartDateNew;

	private boolean coupleTwo;
	private boolean twoDayRotation;
	private int coupleCountLoop = 0;
	private int indexPoint = 0;
	private static String lastBus = null;
	private static String firstBusSecondDay = null;
	private List<RouteScheduleDTO> leaveBusTemp = new ArrayList<>();
	private boolean renderLeave;
	private boolean editTable, editTableLeave, editTableTwoDay, editTableTwoDayLeave;

	private boolean twoDay;

	@PostConstruct
	public void init() {
		timeTableService = (TimeTableService) SpringApplicationContex.getBean("timeTableService");
		routeScheduleService = (RouteScheduleService) SpringApplicationContex.getBean("routeScheduleService");
		disableGroupNo = true;
		loadValue();
	}

	private void loadValue() {
		routeScheduleDTO = new RouteScheduleDTO();
		mainSaveForTwoDay = new ArrayList<RouteScheduleDTO>();
		busForRouteListForTwoDay = new ArrayList<RouteScheduleDTO>();
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
		renderTableThree = false;
		disabledgenerateReport = true;
		disabledClear = true;
		disabledSave = true;
		disabledSwap = false;
		disabledleavePositionList = true;

		editBusForRouteList = new ArrayList<RouteScheduleDTO>();
		editBusForRouteListLeave = new ArrayList<RouteScheduleDTO>();
		editBusForRouteListTwoDay = new ArrayList<RouteScheduleDTO>();
		editBusForRouteListTwoDayLeave = new ArrayList<RouteScheduleDTO>();
		tempEditBusForRouteList = new ArrayList<RouteScheduleDTO>();
		selectedleaveForRouteList = new ArrayList<RouteScheduleDTO>();
		selectedleaveForRouteListNextSide = new ArrayList<RouteScheduleDTO>();

		edit = false;
		originDataVisible = false;
		destinationDataVisible = false;
		lastPanelDateVisible = false;

		dateList = new ArrayList<String>();
		selectedDateRange = null;

		afterSearch = false;
		twoDayRotation = false;
		renderTableThree = false;
		leaveBusTemp = new ArrayList<>();
		renderLeave = false;
		leavePositionListTwoDay = new ArrayList<>();
		editTable = false;
		editTableLeave = false;
		editTableTwoDay = false;
		editTableTwoDayLeave = false;
		twoDay = true;
		selectLeavePositionListTwoDay = new ArrayList<>();
	}

	public void ajaxFillbusCategory() {
		busCategoryList = routeScheduleService.getBusCategoryList(routeScheduleDTO.getRouteNo());
		groupNoList = new ArrayList<>();
		groupNoList = routeScheduleService.getGroupNoList(routeScheduleDTO.getRouteNo(),
				routeScheduleDTO.getGeneratedRefNo());
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

		disableGroupNo = false;

		origin = routeScheduleDTO.getOrigin();
		destination = routeScheduleDTO.getDestination();

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

			groupNoList = new ArrayList<>();
			groupNoList = routeScheduleService.getGroupNoList(routeScheduleDTO.getRouteNo(),
					routeScheduleDTO.getGeneratedRefNo());

			routeScheduleDTO.setGroupNo(groupNo);

		} else {
			setErrorMessage("Origin / Destination can not be empty");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void searchAction() {

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

					RouteScheduleDTO ref = routeScheduleService.getRouteDetailsGroup(routeScheduleDTO.getRouteNo(),
							routeScheduleDTO.getBusCategory(), routeScheduleDTO.getGroupNo());

					if (ref.getGeneratedRefNo() != null && !ref.getGeneratedRefNo().trim().equalsIgnoreCase("")) {
						routeScheduleDTO.setGeneratedRefNo(ref.getGeneratedRefNo());
					}

					boolean isRelatedDataFound = routeScheduleService.isRelatedDataFound(routeScheduleDTO.getRouteNo(),
							routeScheduleDTO.getBusCategory(), routeScheduleDTO.getGeneratedRefNo(),
							routeScheduleDTO.getGroupNo(), tripType);

					/** display data start **/

					TimeTableDTO routeDTO = timeTableService
							.getRouteDataForEditPanelGenerator(routeScheduleDTO.getGeneratedRefNo());

					coupleTwo = routeDTO.isCoupleTwo();
					twoDayRotation = timeTableService.getTwoDayRotation(routeScheduleDTO.getGeneratedRefNo(), null,
							null);

					if (twoDayRotation) {
						twoDay = false;
					}

					int totalBus = tripType.equalsIgnoreCase("O") ? routeDTO.getNoOfPvtBusesOrigin()
							: routeDTO.getNoOfPvtBusesDestination();

					int totalTrips = tripType.equalsIgnoreCase("O") ? routeDTO.getNoOfTripsOrigin()
							: routeDTO.getNoOfTripsDestination();

					if (totalBus > 0) {
						if (totalTrips > 0) {

							noOfBuses = totalBus;
							noOfTrips = totalTrips;

							if (displayGroupData(routeDTO)) {

								if (displayOriginAndDestinationDetails(routeDTO)) {

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

	public boolean displayGroupData(TimeTableDTO routeDTO) {

		boolean continueFlow = false;

		int totalBusForGroup = routeDTO.getNoOfPvtBusesOrigin() + routeDTO.getNoOfPvtBusesDestination();

		int origin_trips = routeDTO.getNoOfTripsOrigin();

		int destination_trips = routeDTO.getNoOfTripsDestination();

		int totalTripsForGroup = origin_trips + destination_trips;

		if (totalBusForGroup > 0) {

			if (totalTripsForGroup > 0) {

				int originTotalLeaves = routeDTO.getNoOfPvtLeaveBusesOrigin();

				int destinationTotalLeaves = routeDTO.getNoOfPvtLeaveBusesDestination();

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

	public boolean displayOriginAndDestinationDetails(TimeTableDTO routeDTO) {

		boolean continueFlow = false;

		int origin_without_fixed_buses = routeDTO.getNoOfPvtBusesOrigin();

		int destination_without_fixed_buses = routeDTO.getNoOfPvtBusesDestination();

		int originTotalLeaves = routeDTO.getNoOfPvtLeaveBusesOrigin();

		int destinationTotalLeaves = routeDTO.getNoOfPvtLeaveBusesDestination();

		int origin_totalTrips = routeDTO.getNoOfTripsOrigin();

		int destination_totalTrips = routeDTO.getNoOfTripsDestination();

		if (originTotalLeaves >= 0 && destinationTotalLeaves >= 0) {

			if (origin_totalTrips > 0) {

				if (destination_totalTrips > 0) {

					/* Maximum leave for each side is 9 */
//					if (originTotalLeaves <= 9) {

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
						selectLeavePositionListTwoDay = new ArrayList<>();
						int fixCount = 0;

						List<TimeTableDTO> panelGeneratorOriginRouteList = timeTableService
								.getOriginDtoListForScheduleWithoutFix(routeScheduleDTO.getGeneratedRefNo());
						List<TimeTableDTO> panelGeneratorOriginRouteLeaveList = timeTableService
								.getLeaveBusesDtoListForEdit(routeScheduleDTO.getGeneratedRefNo());

						for (TimeTableDTO panelGeneratorOriginRouteDto : panelGeneratorOriginRouteList) {

							if (!panelGeneratorOriginRouteDto.getBusNoOrigin().contains("SLTB")
									&& !panelGeneratorOriginRouteDto.getBusNoOrigin().contains("ETC")) {
								fixCount++;
							}

						}

						for (TimeTableDTO panelGeneratorOriginRouteDto : panelGeneratorOriginRouteLeaveList) {

							if (!panelGeneratorOriginRouteDto.getBusNoLeave().contains("SLTB")
									&& !panelGeneratorOriginRouteDto.getBusNoLeave().contains("ETC")) {
								fixCount++;
							}

						}

						for (int i = 1; i <= fixCount; i++) {
							RouteScheduleDTO dtos = new RouteScheduleDTO(i);
							selectLeavePositionList.add(dtos);
							selectLeavePositionListTwoDay.add(dtos);
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
						selectLeavePositionListTwoDay = new ArrayList<>();
						int fixCount = 0;
						List<TimeTableDTO> panelGeneratorDestinationRouteList = timeTableService
								.getDestinationDtoListForScheduleWithoutFix(routeScheduleDTO.getGeneratedRefNo());
						List<TimeTableDTO> panelGeneratorDestinationRouteLeaveList = timeTableService
								.getLeaveBusesDtoListForEditDes(routeScheduleDTO.getGeneratedRefNo());

						for (TimeTableDTO panelGeneratorDestinationRouteDto : panelGeneratorDestinationRouteList) {

							if (!panelGeneratorDestinationRouteDto.getBusNoDestination().contains("SLTB")
									&& !panelGeneratorDestinationRouteDto.getBusNoDestination().contains("ETC")) {
								fixCount++;
							}

						}

						for (TimeTableDTO panelGeneratorDestinationRouteDto : panelGeneratorDestinationRouteLeaveList) {

							if (!panelGeneratorDestinationRouteDto.getBusNoLeaveDes().contains("SLTB")
									&& !panelGeneratorDestinationRouteDto.getBusNoLeaveDes().contains("ETC")) {
								fixCount++;
							}

						}

						for (int i = 1; i <= fixCount; i++) {
							RouteScheduleDTO dtos = new RouteScheduleDTO(i);
							selectLeavePositionList.add(dtos);
							selectLeavePositionListTwoDay.add(dtos);
						}
					}

//					} else {
//						setErrorMessage("Can not continue the flow. No. of leaves for origin should be less than nine");
//						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
//
//					}

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

		long diff = end_date.getTime() - start_date.getTime();
		long days = diff / (1000 * 60 * 60 * 24);

		return days;
	}

	public List<Integer> calculateNoOfDaysForGroup() {
		List<String> daysList = timeTableService.getDaysOfGroup(routeScheduleDTO.getGeneratedRefNo());
		List<Integer> days = new ArrayList<>();

		Date start_date = routeScheduleDTO.getStartDate();
		Date end_date = routeScheduleDTO.getEndDate();

		LocalDate startLocalDate = start_date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate endLocalDate = end_date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		int startYear = startLocalDate.getYear();
		int endYear = endLocalDate.getYear();

		int startMonth = startLocalDate.getMonthValue();
		int endMonth = endLocalDate.getMonthValue();

		int startDate = startLocalDate.getDayOfMonth();
		int endDate = endLocalDate.getDayOfMonth();

		int stopRange = 0;
		if (endYear <= startYear) {
			stopRange = endMonth - startMonth;
		} else {
			stopRange = (12 - startMonth) + endMonth;
		}

		for (int month = 0; month <= stopRange; month++) {
			LocalDate firstDayOfMonth = LocalDate.of(startYear, startMonth, startDate);
			LocalDate lastDayOfMonth = firstDayOfMonth.with(TemporalAdjusters.lastDayOfMonth());
			if (month == stopRange) {
				lastDayOfMonth = LocalDate.of(endYear, endMonth, endDate);
			}
			List<Integer> daysForMonth = new ArrayList<>();

			LocalDate currentDay = firstDayOfMonth;

			while (currentDay.isBefore(lastDayOfMonth) || currentDay.isEqual(lastDayOfMonth)) {
				if (daysList.contains("monday")) {
					LocalDate monday = currentDay.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
					if (!monday.isAfter(lastDayOfMonth)) {
						daysForMonth.add(monday.getDayOfMonth());
					}
				}
				if (daysList.contains("tuesday")) {
					LocalDate tuesday = currentDay.with(TemporalAdjusters.nextOrSame(DayOfWeek.TUESDAY));
					if (!tuesday.isAfter(lastDayOfMonth)) {
						daysForMonth.add(tuesday.getDayOfMonth());
					}
				}
				if (daysList.contains("wednesday")) {
					LocalDate wednesday = currentDay.with(TemporalAdjusters.nextOrSame(DayOfWeek.WEDNESDAY));
					if (!wednesday.isAfter(lastDayOfMonth)) {
						daysForMonth.add(wednesday.getDayOfMonth());
					}
				}
				if (daysList.contains("thursday")) {
					LocalDate thursday = currentDay.with(TemporalAdjusters.nextOrSame(DayOfWeek.THURSDAY));
					if (!thursday.isAfter(lastDayOfMonth)) {
						daysForMonth.add(thursday.getDayOfMonth());
					}
				}
				if (daysList.contains("friday")) {
					LocalDate friday = currentDay.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
					if (!friday.isAfter(lastDayOfMonth)) {
						daysForMonth.add(friday.getDayOfMonth());
					}
				}
				if (daysList.contains("saturday")) {
					LocalDate saturday = currentDay.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
					if (!saturday.isAfter(lastDayOfMonth)) {
						daysForMonth.add(saturday.getDayOfMonth());
					}
				}
				if (daysList.contains("sunday")) {
					LocalDate sunday = currentDay.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
					if (!sunday.isAfter(lastDayOfMonth)) {
						daysForMonth.add(sunday.getDayOfMonth());
					}
				}

				currentDay = currentDay.plusWeeks(1);

			}
			Collections.sort(daysForMonth);
			days.addAll(daysForMonth);

			if (startMonth != 12) {
				startMonth = startMonth % 12 + 1;
				startDate = 1;
			} else {
				startMonth = startMonth % 12 + 1;
				startYear = startYear + 1;
				startDate = 1;
			}
		}

		return days;
	}

	public long calculateNoOfDaysEdit() {

		Date start_date = routeScheduleDTO.getLastPanelStartDate();
		// Date start_date = lastPanelStartDateNew;
		Date end_date = routeScheduleDTO.getLastPanelEndDate();

		long diff = start_date.getTime() - end_date.getTime();
		long days = ((diff / (1000 * 60 * 60 * 24)));

		return Math.abs(days);
	}

	public List<Integer> calculateNoOfDaysForGroupEdit() {
		List<String> daysList = timeTableService.getDaysOfGroup(routeScheduleDTO.getGeneratedRefNo());
		List<Integer> days = new ArrayList<>();

		Date start_date = routeScheduleDTO.getLastPanelStartDate();
		Date end_date = routeScheduleDTO.getLastPanelEndDate();

		LocalDate startLocalDate = start_date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate endLocalDate = end_date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		int startYear = startLocalDate.getYear();
		int endYear = endLocalDate.getYear();

		int startMonth = startLocalDate.getMonthValue();
		int endMonth = endLocalDate.getMonthValue();

		int startDate = startLocalDate.getDayOfMonth();
		int endDate = endLocalDate.getDayOfMonth();

		int stopRange = 0;
		if (endYear <= startYear) {
			stopRange = endMonth - startMonth;
		} else {
			stopRange = (12 - startMonth) + endMonth;
		}

		for (int month = 0; month <= stopRange; month++) {
			LocalDate firstDayOfMonth = LocalDate.of(startYear, startMonth, startDate);
			LocalDate lastDayOfMonth = firstDayOfMonth.with(TemporalAdjusters.lastDayOfMonth());
			List<Integer> daysForMonth = new ArrayList<>();

			LocalDate currentDay = firstDayOfMonth;

			while (currentDay.isBefore(lastDayOfMonth) || currentDay.isEqual(lastDayOfMonth)) {
				if (daysList.contains("monday")) {
					LocalDate monday = currentDay.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
					if (monday.isAfter(lastDayOfMonth)) {
						break;
					} else {
						daysForMonth.add(monday.getDayOfMonth());
					}
				}
				if (daysList.contains("tuesday")) {
					LocalDate tuesday = currentDay.with(TemporalAdjusters.nextOrSame(DayOfWeek.TUESDAY));
					if (tuesday.isAfter(lastDayOfMonth)) {
						break;
					} else {
						daysForMonth.add(tuesday.getDayOfMonth());
					}
				}
				if (daysList.contains("wednesday")) {
					LocalDate wednesday = currentDay.with(TemporalAdjusters.nextOrSame(DayOfWeek.WEDNESDAY));
					if (wednesday.isAfter(lastDayOfMonth)) {
						break;
					} else {
						daysForMonth.add(wednesday.getDayOfMonth());
					}
				}
				if (daysList.contains("thursday")) {
					LocalDate thursday = currentDay.with(TemporalAdjusters.nextOrSame(DayOfWeek.THURSDAY));
					if (thursday.isAfter(lastDayOfMonth)) {
						break;
					} else {
						daysForMonth.add(thursday.getDayOfMonth());
					}
				}
				if (daysList.contains("friday")) {
					LocalDate friday = currentDay.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
					if (friday.isAfter(lastDayOfMonth)) {
						break;
					} else {
						daysForMonth.add(friday.getDayOfMonth());
					}
				}
				if (daysList.contains("saturday")) {
					LocalDate saturday = currentDay.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
					if (saturday.isAfter(lastDayOfMonth)) {
						break;
					} else {
						daysForMonth.add(saturday.getDayOfMonth());
					}
				}
				if (daysList.contains("sunday")) {
					LocalDate sunday = currentDay.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
					if (sunday.isAfter(lastDayOfMonth)) {
						break;
					} else {
						daysForMonth.add(sunday.getDayOfMonth());
					}
				}

				currentDay = currentDay.plusWeeks(1);

			}
			Collections.sort(daysForMonth);
			days.addAll(daysForMonth);
		}

		return days;
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
			System.out.println(days);

			if (days >= 184) {

				setErrorMessage("Please select data range less than six month");
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

			if (days > 184) {

				setErrorMessage("Please select data range less than six month");
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
		disableGroupNo = true;

		loadValue();
	}

	public void noramlRotation() {
		dayList = new ArrayList<>();
		dayList = calculateNoOfDaysForGroup();
		maximumDays = dayList.size();

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
					twoDayRotation = timeTableService.getTwoDayRotation(routeScheduleDTO.getGeneratedRefNo(), null,
							null);
					if (routeScheduleDTO.getNoOfLeavesOrigin() == 0) {

						if (coupleTwo) {
							busNoManagerNormalCoupleTwoWithLeave(
									routeScheduleDTO.getNoOfTripsOrigin() + routeScheduleDTO.getNoOfLeavesOrigin());
							createMainDataTable();
							createLeavesDataTable();
							disabledleavePositionList = false;
							disabledZigzagRotation = true;
							rotationType = "N";
							disabledSave = false;
						} else if (!twoDayRotation && !coupleTwo) {
							busNoManagerNormalCoupleTwoWithLeave(routeScheduleDTO.getNoOfTripsOrigin());
							createMainDataTable();
							createLeavesDataTable();
							disabledleavePositionList = false;
							disabledZigzagRotation = true;
							rotationType = "N";
							disabledSave = false;
						} else if (twoDayRotation) {
							busManagerTwoDayRotation(
									routeScheduleDTO.getNoOfTripsOrigin() + routeScheduleDTO.getNoOfLeavesOrigin());

							System.out.println(busForRouteList.get(0).getBusNoList());
							createMainDataTable();
							createLeavesDataTable();
							disabledleavePositionList = false;
							disabledZigzagRotation = true;
							rotationType = "N";
							disabledSave = false;
						}

					} else if (routeScheduleDTO.getNoOfLeavesOrigin() != 0) {

						if (!leavePositionList.isEmpty() || !leavePositionListTwoDay.isEmpty()) {

							if (coupleTwo && !twoDayRotation) {
								busNoManagerNormalCoupleTwoWithLeave(
										routeScheduleDTO.getNoOfTripsOrigin() + routeScheduleDTO.getNoOfLeavesOrigin());
								createMainDataTable();
								createLeavesDataTable();
								disabledleavePositionList = false;
								disabledZigzagRotation = true;
								rotationType = "N";
								disabledSave = false;
							} else {
								if (!twoDayRotation) {

									busNoManagerNormalCoupleTwoWithLeave(routeScheduleDTO.getNoOfTripsOrigin()
											+ routeScheduleDTO.getNoOfLeavesOrigin());
									createMainDataTable();
									createLeavesDataTable();
									disabledleavePositionList = false;
									disabledZigzagRotation = true;
									rotationType = "N";
									disabledSave = false;

								} else {
									twoDayRotation = timeTableService
											.getTwoDayRotation(routeScheduleDTO.getGeneratedRefNo(), null, null);
									if (twoDayRotation) {
										busManagerTwoDayRotation(routeScheduleDTO.getNoOfTripsOrigin()
												+ routeScheduleDTO.getNoOfLeavesOrigin());
										createMainDataTable();
										createLeavesDataTable();
										disabledleavePositionList = false;
										disabledZigzagRotation = true;
										rotationType = "N";
										disabledSave = false;
									} else {
										setErrorMessage("Please select only " + routeScheduleDTO.getNoOfLeavesOrigin()
												+ " position");
										RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
									}
								}
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

			dayList = calculateNoOfDaysForGroup();
			maximumDays = dayList.size();

			if (routeScheduleDTO.getLastPanelStartDate() != null) {

				if (routeScheduleDTO.getLastPanelEndDate() != null) {

					// noOfDaysFortimeTable = (int) calculateNoOfDaysEdit();
					noOfDaysFortimeTable = (int) calculateNoOfDaysEditNew();
					if (routeScheduleDTO.getNoOfLeavesOrigin() == 0) {

						busNoManagerNormalEditNew(
								routeScheduleDTO.getNoOfTripsOrigin() + routeScheduleDTO.getNoOfLeavesOrigin());
//						busNoManagerNormalEdit(routeScheduleDTO.getNoOfTripsOrigin());
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
						busNoManagerNormalEditNew(
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

		dayList = calculateNoOfDaysForGroup();

		if (!lastPanelDateVisible) {
			if (routeScheduleDTO.getStartDate() != null) {

				if (routeScheduleDTO.getEndDate() != null) {

					noOfDaysFortimeTable = (int) calculateNoOfDays();
					dayList = calculateNoOfDaysForGroup();
					twoDayRotation = timeTableService.getTwoDayRotation(routeScheduleDTO.getGeneratedRefNo(), null,
							null);

					if (routeScheduleDTO.getNoOfLeavesDestination() == 0) {

						if (coupleTwo) {
							busNoManagerNormalCoupleTwoWithLeave(
									routeScheduleDTO.getNoOfTripsOrigin() + routeScheduleDTO.getNoOfLeavesOrigin());
							createMainDataTable();
							createLeavesDataTable();
							disabledleavePositionList = false;
							disabledZigzagRotation = true;
							rotationType = "N";
							disabledSave = false;
						} else if (!twoDayRotation && !coupleTwo) {
							busNoManagerNormalCoupleTwoWithLeave(routeScheduleDTO.getNoOfTripsDestination());
							createMainDataTable();
							createLeavesDataTable();
							disabledleavePositionList = false;
							disabledZigzagRotation = true;
							rotationType = "N";
							disabledSave = false;
						} else if (twoDayRotation) {
							busManagerTwoDayRotation(routeScheduleDTO.getNoOfTripsDestination()
									+ routeScheduleDTO.getNoOfLeavesDestination());
							createMainDataTable();
							createLeavesDataTable();
							disabledleavePositionList = false;
							disabledZigzagRotation = true;
							rotationType = "N";
							disabledSave = false;
						}

					} else if (routeScheduleDTO.getNoOfLeavesDestination() != 0) {

						if (!leavePositionList.isEmpty() || !leavePositionListTwoDay.isEmpty()) {

							if (coupleTwo && !twoDayRotation) {
								busNoManagerNormalCoupleTwoWithLeave(routeScheduleDTO.getNoOfTripsDestination()
										+ routeScheduleDTO.getNoOfLeavesDestination());
								createMainDataTable();
								createLeavesDataTable();
								disabledleavePositionList = false;
								disabledZigzagRotation = true;
								rotationType = "N";
								disabledSave = false;
							} else {

								if (!twoDayRotation) {

									busNoManagerNormalCoupleTwoWithLeave(routeScheduleDTO.getNoOfTripsDestination()
											+ routeScheduleDTO.getNoOfLeavesDestination());
									createMainDataTable();
									createLeavesDataTable();
									disabledleavePositionList = false;
									disabledZigzagRotation = true;
									rotationType = "N";
									disabledSave = false;

								} else {
									twoDayRotation = timeTableService
											.getTwoDayRotation(routeScheduleDTO.getGeneratedRefNo(), null, null);
									if (twoDayRotation) {
										busManagerTwoDayRotation(routeScheduleDTO.getNoOfTripsDestination()
												+ routeScheduleDTO.getNoOfLeavesDestination());
										createMainDataTable();
										createLeavesDataTable();
										disabledleavePositionList = false;
										disabledZigzagRotation = true;
										rotationType = "N";
										disabledSave = false;
									} else {
										setErrorMessage("Please select only " + routeScheduleDTO.getNoOfLeavesOrigin()
												+ " position");
										RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
									}
								}
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

			dayList = calculateNoOfDaysForGroup();
			maximumDays = dayList.size();

			if (routeScheduleDTO.getLastPanelStartDate() != null) {

				if (routeScheduleDTO.getLastPanelEndDate() != null) {

					noOfDaysFortimeTable = (int) calculateNoOfDaysEdit();

					if (routeScheduleDTO.getNoOfLeavesDestination() == 0) {

						busNoManagerNormalEditNew(routeScheduleDTO.getNoOfTripsDestination());
						createMainDataTable();
						createLeavesDataTable();
						disabledleavePositionList = false;
						disabledZigzagRotation = true;
						rotationType = "N";
						disabledSave = false;

					} else if (routeScheduleDTO.getNoOfLeavesDestination() != 0) {

						leavePositionList = new ArrayList<>(routeScheduleDTO.getNoOfLeavesOrigin());

						busNoManagerNormalEditNew(routeScheduleDTO.getNoOfTripsDestination()
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

//	public void zigzagRotation() {
//
//		if (tripType.equals("O")) {
//			zigzagRotationOrigin();
//		} else {
//			zigzagRotationDestination();
//		}
//
//	}
//
//	public void zigzagRotationOrigin() {
//		if (!lastPanelDateVisible) {
//			if (routeScheduleDTO.getStartDate() != null) {
//
//				if (routeScheduleDTO.getEndDate() != null) {
//
//					noOfDaysFortimeTable = (int) calculateNoOfDays();
//
//					if (routeScheduleDTO.getNoOfLeavesOrigin() == 0) {
//
//						busNoManagerRandom(routeScheduleDTO.getNoOfTripsOrigin());
//						createMainDataTable();
//						createLeavesDataTable();
//						disabledleavePositionList = false;
//						disabledNoramlRotation = true;
//						rotationType = "Z";
//						disabledSave = false;
//
//					} else if (routeScheduleDTO.getNoOfLeavesOrigin() != 0) {
//
//						if (!leavePositionList.isEmpty()) {
//
//							if (leavePositionList.size() == routeScheduleDTO.getNoOfLeavesOrigin()) {
//
//								busNoManagerRandom(routeScheduleDTO.getNoOfTripsOrigin());
//								createMainDataTable();
//								createLeavesDataTable();
//								disabledleavePositionList = false;
//								disabledNoramlRotation = true;
//								rotationType = "Z";
//								disabledSave = false;
//
//							} else {
//								setErrorMessage(
//										"Please select only " + routeScheduleDTO.getNoOfLeavesOrigin() + " position");
//								RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
//							}
//
//						} else {
//							setErrorMessage("Please select the leave positions for continue the flow");
//							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
//						}
//
//					} else {
//						setErrorMessage("Can not continue the flow");
//						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
//					}
//
//				} else {
//					setErrorMessage("Please select the end date");
//					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
//				}
//
//			} else {
//				setErrorMessage("Please select the start date");
//				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
//			}
//		} else {
//			// edit
//			if (routeScheduleDTO.getStartDate() != null) {
//
//				if (routeScheduleDTO.getEndDate() != null) {
//
//					noOfDaysFortimeTable = (int) calculateNoOfDays();
//
//					if (routeScheduleDTO.getNoOfLeavesOrigin() == 0) {
//
//						busNoManagerRandom(routeScheduleDTO.getNoOfTripsOrigin());
//						createMainDataTable();
//						createLeavesDataTable();
//						disabledleavePositionList = false;
//						disabledNoramlRotation = true;
//						rotationType = "Z";
//						disabledSave = false;
//
//					} else if (routeScheduleDTO.getNoOfLeavesOrigin() != 0) {
//
//						busNoManagerRandom(routeScheduleDTO.getNoOfTripsOrigin());
//						createMainDataTable();
//						createLeavesDataTable();
//						disabledleavePositionList = false;
//						disabledNoramlRotation = true;
//						rotationType = "Z";
//						disabledSave = false;
//
//					} else {
//						setErrorMessage("Can not continue the flow");
//						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
//					}
//
//				} else {
//					setErrorMessage("Please select the end date");
//					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
//				}
//
//			} else {
//				setErrorMessage("Please select the start date");
//				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
//			}
//		}
//	}
//
//	public void zigzagRotationDestination() {
//		if (!lastPanelDateVisible) {
//			if (routeScheduleDTO.getStartDate() != null) {
//
//				if (routeScheduleDTO.getEndDate() != null) {
//
//					noOfDaysFortimeTable = (int) calculateNoOfDays();
//
//					if (routeScheduleDTO.getNoOfLeavesDestination() == 0) {
//
//						busNoManagerRandom(routeScheduleDTO.getNoOfTripsDestination());
//						createMainDataTable();
//						createLeavesDataTable();
//						disabledleavePositionList = false;
//						disabledNoramlRotation = true;
//						rotationType = "Z";
//						disabledSave = false;
//
//					} else if (routeScheduleDTO.getNoOfLeavesDestination() != 0) {
//
//						if (!leavePositionList.isEmpty()) {
//
//							if (leavePositionList.size() == routeScheduleDTO.getNoOfLeavesDestination()) {
//
//								busNoManagerRandom(routeScheduleDTO.getNoOfTripsDestination());
//								createMainDataTable();
//								createLeavesDataTable();
//								disabledleavePositionList = false;
//								disabledNoramlRotation = true;
//								rotationType = "Z";
//								disabledSave = false;
//
//							} else {
//								setErrorMessage("Please select only " + routeScheduleDTO.getNoOfLeavesDestination()
//										+ " position");
//								RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
//							}
//
//						} else {
//							setErrorMessage("Please select the leave positions for continue the flow");
//							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
//						}
//
//					} else {
//						setErrorMessage("Can not continue the flow");
//						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
//					}
//
//				} else {
//					setErrorMessage("Please select the end date");
//					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
//				}
//
//			} else {
//				setErrorMessage("Please select the start date");
//				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
//			}
//		} else {
//			// edit
//			if (routeScheduleDTO.getStartDate() != null) {
//
//				if (routeScheduleDTO.getEndDate() != null) {
//
//					noOfDaysFortimeTable = (int) calculateNoOfDays();
//
//					if (routeScheduleDTO.getNoOfLeavesDestination() == 0) {
//
//						busNoManagerRandom(routeScheduleDTO.getNoOfTripsDestination());
//						createMainDataTable();
//						createLeavesDataTable();
//						disabledleavePositionList = false;
//						disabledNoramlRotation = true;
//						rotationType = "Z";
//						disabledSave = false;
//
//					} else if (routeScheduleDTO.getNoOfLeavesDestination() != 0) {
//
//						busNoManagerRandom(routeScheduleDTO.getNoOfTripsDestination());
//						createMainDataTable();
//						createLeavesDataTable();
//						disabledleavePositionList = false;
//						disabledNoramlRotation = true;
//						rotationType = "Z";
//						disabledSave = false;
//
//					} else {
//						setErrorMessage("Can not continue the flow");
//						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
//					}
//
//				} else {
//					setErrorMessage("Please select the end date");
//					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
//				}
//
//			} else {
//				setErrorMessage("Please select the start date");
//				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
//			}
//		}
//	}

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

	public void busNoManagerNormalCoupleTwoWithLeave(int noOfTrips) {
		List<RouteScheduleDTO> busListSample = new ArrayList<>();
		List<RouteScheduleDTO> busNoList = new ArrayList<RouteScheduleDTO>();
		busForRouteList = new ArrayList<>();
		fullBusNoList = new ArrayList<>();
		leaveForRouteList = new ArrayList<>();
		selectedleaveForRouteList = new ArrayList<RouteScheduleDTO>();
		List<String> leavePositionForRouteList = new ArrayList<>();
		List<String> leaveBusList = new ArrayList<>();
		List<String> lastBusList = new ArrayList<>();
		List<String> FullbusListWithoutLeave = new ArrayList<>();
		LinkedHashSet<String> uniqueCharsWithoutLeave = new LinkedHashSet<>(FullbusListWithoutLeave);
		LinkedHashSet<String> uniqueCharsLeave = new LinkedHashSet<>();
		LinkedHashSet<String> uniqueChars = new LinkedHashSet<>();
		LinkedHashSet<String> uniqueCharsWithoutFix = new LinkedHashSet<>();
		LinkedHashMap<LinkedHashSet<String>, RouteScheduleHelperDTO> panelGeneratorRouteBusList = new LinkedHashMap<>();
		LinkedHashSet<String> panelGeneratorRouteLeaveBusList = new LinkedHashSet<>();
		tripID = 0;
		int noOfTripsPerSide = 0;
		int fixCount = 0;
		int leaveCount = 0;

		TimeTableDTO routeDTO = timeTableService
				.getRouteDataForEditPanelGenerator(routeScheduleDTO.getGeneratedRefNo());

		noOfTripsPerSide = tripType.equalsIgnoreCase("O") ? routeDTO.getNoOfTripsOrigin()
				: routeDTO.getNoOfTripsDestination();

		TimeTableDTO abbriDTO = timeTableService.getAbriviatiosForRoute(routeDTO.getRouteNo(),
				routeDTO.getBusCategory());

		if (tripType.equalsIgnoreCase("O")) {
			panelGeneratorRouteBusList = timeTableService.getBusFixAndNotFixed(routeScheduleDTO.getGeneratedRefNo(),
					abbriDTO.getAbbriAtOrigin(), "O");
			RouteScheduleHelperDTO leaveBus = timeTableService.getLeaveBusesList(routeScheduleDTO.getGeneratedRefNo(),
					abbriDTO.getAbbriAtOrigin(), "O");

			Map.Entry<LinkedHashSet<String>, RouteScheduleHelperDTO> entry = panelGeneratorRouteBusList.entrySet()
					.stream().skip(0).findFirst().orElse(null);

			if (entry != null) {
				RouteScheduleHelperDTO dto = entry.getValue();
				uniqueCharsWithoutFix = dto.getBusSet();
				FullbusListWithoutLeave = dto.getBusList();
				
				uniqueCharsWithoutLeave.addAll(FullbusListWithoutLeave);

				uniqueCharsLeave = leaveBus.getBusSet();
				uniqueCharsWithoutFix.addAll(uniqueCharsLeave);

				uniqueChars.addAll(uniqueCharsWithoutLeave);
				uniqueChars.addAll(uniqueCharsLeave);

				fullBusNoList.addAll(FullbusListWithoutLeave);
				fullBusNoList.addAll(leaveBus.getBusList());

				fixCount = entry.getKey().size();
				leaveCount = uniqueCharsLeave.size();
				noOfTripsPerSide = uniqueChars.size();
			}
		} else if (tripType.equalsIgnoreCase("D")) {
			panelGeneratorRouteBusList = timeTableService.getBusFixAndNotFixed(routeScheduleDTO.getGeneratedRefNo(),
					abbriDTO.getAbbriAtDestination(), "D");
			RouteScheduleHelperDTO leaveBus = timeTableService.getLeaveBusesList(routeScheduleDTO.getGeneratedRefNo(),
					abbriDTO.getAbbriAtDestination(), "D");

			Map.Entry<LinkedHashSet<String>, RouteScheduleHelperDTO> entry = panelGeneratorRouteBusList.entrySet()
					.stream().skip(0).findFirst().orElse(null);

			if (entry != null) {
				RouteScheduleHelperDTO dto = entry.getValue();
				uniqueCharsWithoutFix = dto.getBusSet();
				FullbusListWithoutLeave = dto.getBusList();

				uniqueCharsWithoutLeave.addAll(FullbusListWithoutLeave);

				uniqueCharsLeave = leaveBus.getBusSet();
				uniqueCharsWithoutFix.addAll(uniqueCharsLeave);

				uniqueChars.addAll(uniqueCharsWithoutLeave);
				uniqueChars.addAll(uniqueCharsLeave);

				fullBusNoList.addAll(FullbusListWithoutLeave);
				fullBusNoList.addAll(leaveBus.getBusList());

				fixCount = entry.getKey().size();
				leaveCount = uniqueCharsLeave.size();
				noOfTripsPerSide = uniqueChars.size();
			}
		}

		if (uniqueChars != null && !uniqueChars.isEmpty() && uniqueChars.size() != 0
				&& leavePositionList.size() == leaveCount) {
			if (!leavePositionList.isEmpty()) {
				leavePositionForRouteList.addAll(leavePositionList);
				HashMap<String[], int[]> mainBus = new HashMap<String[], int[]>();
				int[] position = new int[leavePositionForRouteList.size()];
				List<String[]> allRotations = new ArrayList<>();
				int stopPoint = 0;

				if (fixCount == 0) {
					String[] busArray = uniqueChars.toArray(new String[uniqueChars.size()]);

					for (int c = 0; c < leavePositionForRouteList.size(); c++) {
						leaveBusList = new ArrayList<>();
						lastBusList = new ArrayList<>();
						stopPoint = 0;

						int e = Integer.parseInt(leavePositionForRouteList.get(c)); // Use parseInt instead of valueOf
						List<String[]> rotations = getRotations(busArray, e);
						allRotations.addAll(rotations);
						position[c] = e;

						for (String[] array : rotations) {
							Collections.addAll(lastBusList, array);
						}

						int listSize = lastBusList.size();
						for (int i = 0; i < maximumDays; i++) {
							leaveBusList.add(lastBusList.get(stopPoint));
							stopPoint = (stopPoint + 1) % listSize;
						}

						RouteScheduleDTO dto = new RouteScheduleDTO();
						dto.setTripId(leavePositionForRouteList.get(c));
						dto.setLeaveBusNoList(leaveBusList);
						leaveForRouteList.add(dto);
					}
					selectedleaveForRouteList.addAll(leaveForRouteList);
					List<String[]> allRotationsMain = new ArrayList<>();

					lastBus = busArray[0];

					for (int i = 0; i < uniqueChars.size(); i++) {
						List<String> leaveIndex = new ArrayList<>(allRotations.size());

						for (String[] rotation : allRotations) {
							leaveIndex.add(rotation[i]);
						}

						List<String[]> rotations = generateRotation(busArray, leaveIndex);
						allRotationsMain.addAll(rotations);

						String[] firstRotation = rotations.get(0);
						lastBus = firstRotation[firstRotation.length - 1];
					}

					List<String> stringBusNoList = new ArrayList<>();
					LinkedHashMap<String, String> order = new LinkedHashMap<>();
					List<String> busListWithoutLeave = new ArrayList<>(uniqueCharsWithoutLeave);
					tripID = 1;

					for (int i = 0; i < allRotationsMain.get(0).length; i++) {
						stopPoint = 0;
						stringBusNoList = new ArrayList<>();
						List<String> leaveIndex = new ArrayList<>();

						for (String[] rotation : allRotationsMain) {
							leaveIndex.add(rotation[i]);
						}

						int listSize = lastBusList.size();
						for (int c = 0; c < maximumDays; c++) {
							stringBusNoList.add(leaveIndex.get(stopPoint));
							stopPoint = (stopPoint + 1) % listSize;
						}

						RouteScheduleDTO dto = new RouteScheduleDTO();
						dto.setBusNoList(stringBusNoList);
						busListSample.add(dto);
						order.put(busListWithoutLeave.get(i), stringBusNoList.get(0));
					}

					int routeIndex = 0;
					int fullSize = FullbusListWithoutLeave.size();

					while (routeIndex < fullSize) {
						String bus = order.get(fullBusNoList.get(routeIndex++));

						for (RouteScheduleDTO busSample : busListSample) {
							if (busSample.getBusNoList().get(0).equalsIgnoreCase(bus)) {
								RouteScheduleDTO dto = new RouteScheduleDTO();
								dto.setTripId(String.valueOf(tripID++));
								dto.setBusNoList(busSample.getBusNoList());
								busForRouteList.add(dto);
							}
						}
					}

					mainSaveList.addAll(busForRouteList);
					renderTableOne = true;
					if (!leaveForRouteList.isEmpty()) {
						renderTableTwo = true;
					}
				} else {
					String[] busArrayWithoutFix = uniqueCharsWithoutFix
							.toArray(new String[uniqueCharsWithoutFix.size()]);
					String[] busArray = uniqueChars.toArray(new String[uniqueChars.size()]);

					for (int c = 0; c < leavePositionForRouteList.size(); c++) {
						leaveBusList = new ArrayList<>();
						lastBusList = new ArrayList<>();
						stopPoint = 0;

						int e = Integer.parseInt(leavePositionForRouteList.get(c)); // Use parseInt instead of valueOf
						List<String[]> rotations = getRotations(busArrayWithoutFix, e);
						allRotations.addAll(rotations);
						position[c] = e;

						for (String[] array : rotations) {
							Collections.addAll(lastBusList, array);
						}

						int listSize = lastBusList.size();
						for (int i = 0; i < maximumDays; i++) {
							leaveBusList.add(lastBusList.get(stopPoint));
							stopPoint = (stopPoint + 1) % listSize;
						}

						RouteScheduleDTO dto = new RouteScheduleDTO();
						dto.setTripId(leavePositionForRouteList.get(c));
						dto.setLeaveBusNoList(leaveBusList);
						leaveForRouteList.add(dto);
					}

					selectedleaveForRouteList.addAll(leaveForRouteList);
					tripID = 1;
					int index = 0;
					int finalIndex = 0;
					List<String> leaveIndex = new ArrayList<>();
					List<String[]> allRotationsMain = new ArrayList<>();
					lastBus = busArrayWithoutFix[0];

					for (int i = 0; i < uniqueChars.size(); i++) {
						if (busArray[i].endsWith("F")) {
							String bus = busArray[i];
							
							if (fixCount < busArrayWithoutFix.length) {
								index = 0;
								List<String[]> rotation = generateRotationWithoutleaveMainFix(bus,
										busArrayWithoutFix.length - fixCount);
								allRotationsMain.addAll(rotation);
								busArray[i] = null;

								index++;
								finalIndex += index;
							} else if(busArrayWithoutFix.length != 0){
								index = 0;
								List<String[]> rotation = generateRotationWithoutleaveMainFix(bus,
										busArrayWithoutFix.length);
								allRotationsMain.addAll(rotation);
								busArray[i] = null;

								index++;
								finalIndex += index;
							}else {
								index = 0;
								List<String[]> rotation = generateRotationWithoutleaveMainFix(bus, 1);
								allRotationsMain.addAll(rotation);
								busArray[i] = null;

								index++;
								finalIndex += index;
							}
						} else {
							leaveIndex = new ArrayList<>();

							for (String[] rotation : allRotations) {
								leaveIndex.add(rotation[i - finalIndex]);
							}

							List<String[]> rotations = generateRotation(busArrayWithoutFix, leaveIndex);

							if(rotations.get(0).length != 0) {
								allRotationsMain.addAll(rotations);
								String[] firstRotation = rotations.get(0);
								lastBus = firstRotation[firstRotation.length - 1];
							}
							
						}
					}

					boolean continueFlow;
					busListSample = new ArrayList<>();
					LinkedHashMap<String, String> order = new LinkedHashMap<>();
					List<String> busListWithoutLeave = new ArrayList<>(uniqueCharsWithoutLeave);
					int indexFix = 0;
					int indexFixFinal = 0;

					for (int i = 0; i < allRotationsMain.size(); i++) {
						continueFlow = true;
						List<String> stringBusNoList = new ArrayList<>();
						leaveIndex = new ArrayList<>();

						for (int count = 0; count < allRotationsMain.size(); count++) {
							if (allRotationsMain.get(i)[0].endsWith("F")) {
								indexFix = 0;
								String fixBus = allRotationsMain.get(i)[0];
								String extractedBus =  fixBus.replaceAll("F$", "");
								leaveIndex.add(extractedBus);
								indexFix++;
								if (count == allRotationsMain.size() - 1) {
									indexFixFinal += indexFix;
								}

							} else {
								int sizeOfFirstArray = allRotationsMain.get(0).length;
								if (i - indexFixFinal >= sizeOfFirstArray) {
									continueFlow = false;
									break;
								} else {
									if (!allRotationsMain.get(count)[0].endsWith("F")) {
										leaveIndex.add(allRotationsMain.get(count)[i - indexFixFinal]);
									}
								}

							}
						}

						// Only proceed if continueFlow is true
						if (continueFlow) {
							stopPoint = 0;
							int listSize = leaveIndex.size();
							for (int c = 0; c < maximumDays; c++) {
								stringBusNoList.add(leaveIndex.get(stopPoint));
								stopPoint = (stopPoint + 1) % listSize;
							}

							RouteScheduleDTO dto = new RouteScheduleDTO();
							dto.setBusNoList(stringBusNoList);
							busListSample.add(dto);
							order.put(busListWithoutLeave.get(i), stringBusNoList.get(0));
						}
					}

					int routeIndex = 0;
					int fullSize = FullbusListWithoutLeave.size();

					while (routeIndex < fullSize) {
						String bus = order.get(fullBusNoList.get(routeIndex++));

						for (RouteScheduleDTO busSample : busListSample) {
							if (busSample.getBusNoList().get(0).equalsIgnoreCase(bus)) {
								RouteScheduleDTO dto = new RouteScheduleDTO();
								dto.setBusNoList(busSample.getBusNoList());
								dto.setTripId(String.valueOf(tripID++));
								busForRouteList.add(dto);
								break;
							}
						}
					}

					mainSaveList.addAll(busForRouteList);
					renderTableOne = true;
					if (!leaveForRouteList.isEmpty()) {
						renderTableTwo = true;
					}
				}

			} else {
				List<String[]> allRotations = new ArrayList<>();
				String[] busArray = uniqueChars.toArray(new String[uniqueChars.size()]);
				List<String> leaveIndex = new ArrayList<>();
				String bus = null;
				int shifBy = 0;

				for (int i = 0; i < uniqueChars.size(); i++) {
					if (busArray[i].endsWith("F")) {
						bus = busArray[i];
						if (fixCount != busArray.length) {
							List<String[]> rotation = generateRotationWithoutleaveMainFix(bus,
									busArray.length - fixCount);
							allRotations.addAll(rotation);
							busArray[i] = null;
						} else {
							List<String[]> rotation = generateRotationWithoutleaveMainFix(bus, busArray.length);
							allRotations.addAll(rotation);
							busArray[i] = null;
						}
					} else {
						if (fixCount == 0) {
							List<String[]> rotation = generateRotationWithoutleaveMain(busArray, i);
							allRotations.addAll(rotation);
						} else {

							int nonNullCount = 0;
							String[] rotated = new String[busArray.length - fixCount];
							for (String bus1 : busArray) {
								if (bus1 != null && !bus1.endsWith("F")) {
									rotated[nonNullCount++] = bus1;
								}
							}

							List<String[]> rotation = generateRotationWithoutleaveMain(rotated, shifBy);
							allRotations.addAll(rotation);
							shifBy++;
						}

					}

				}

				List<String> stringBusNoList = new ArrayList<>();
				LinkedHashMap<String, String> order = new LinkedHashMap<>();
				List<String> busListWithoutLeave = new ArrayList<>(uniqueCharsWithoutLeave);

				if (fixCount == 0) {
					for (int i = 0; i < allRotations.get(0).length; i++) {
						int stopPoint = 0;
						stringBusNoList = new ArrayList<>();
						leaveIndex = new ArrayList<>();

						for (String[] array : allRotations) {
							leaveIndex.add(array[i]);
						}

						int listSize = leaveIndex.size();
						for (int c = 0; c < maximumDays; c++) {
							stringBusNoList.add(leaveIndex.get(stopPoint));
							stopPoint = (stopPoint + 1) % listSize;
						}

						RouteScheduleDTO dto = new RouteScheduleDTO();
						dto.setBusNoList(stringBusNoList);
						busListSample.add(dto);
						order.put(busListWithoutLeave.get(i), stringBusNoList.get(0));
					}
				} else {
					int index = 0;
					int indexFix = 0;
					int indexFixFinal = 0;

					for (int i = 0; i < allRotations.size(); i++) {
						int stopPoint = 0;
						stringBusNoList = new ArrayList<>();
						leaveIndex = new ArrayList<>();
						index++;
						for (int count = 0; count < allRotations.size(); count++) {
							if (allRotations.get(i)[0].endsWith("F")) {
								indexFix = 0;
								String fixBus = allRotations.get(i)[0];
								String extractedBus = fixBus.replaceAll("F$", "");
								leaveIndex.add(extractedBus);
								indexFix++;
								if (count == allRotations.size() - 1) {
									indexFixFinal += indexFix;
								}

							} else {
								if (!allRotations.get(count)[0].endsWith("F")) {
									leaveIndex.add(allRotations.get(count)[i - indexFixFinal]);
								}

							}

						}

						int listSize = leaveIndex.size();
						for (int c = 0; c < maximumDays; c++) {
							stringBusNoList.add(leaveIndex.get(stopPoint));
							stopPoint = (stopPoint + 1) % listSize;
						}

						RouteScheduleDTO dto = new RouteScheduleDTO();
						dto.setBusNoList(stringBusNoList);
						busListSample.add(dto);
						order.put(busListWithoutLeave.get(i), stringBusNoList.get(0));
					}
				}

				tripID = 1;

				int routeIndex = 0;
				int fullSize = FullbusListWithoutLeave.size();

				while (routeIndex < fullSize) {
					String busNo = order.get(fullBusNoList.get(routeIndex++));

					for (RouteScheduleDTO busSample : busListSample) {
						if (busSample.getBusNoList().get(0).equalsIgnoreCase(busNo)) {
							RouteScheduleDTO dto = new RouteScheduleDTO();
							dto.setTripId(String.valueOf(tripID++));
							dto.setBusNoList(busSample.getBusNoList());
							busForRouteList.add(dto);
						}
					}
				}
				mainSaveList.addAll(busForRouteList);
				renderTableOne = true;
			}
		} else {
			sessionBackingBean.setMessage("There are no buses for selected data");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
			return;
		}

	}

	public static List<String[]> generateRotationWithoutleaveMainFix(String bus, int size) {
		String[] rotation = new String[size];
		Arrays.fill(rotation, bus);

		return Collections.singletonList(rotation);
	}

	public static List<String[]> generateRotationWithoutleaveMain(String[] buses, int shiftBy) {
		String[] rotation = generateRotationWithoutLeave(buses, shiftBy);
		return Collections.singletonList(rotation);
	}

	public static String[] generateRotationWithoutLeave(String[] buses, int shiftBy) {
		int n = buses.length;
		String[] rotatedBuses = new String[n];

		for (int i = 0; i < n; i++) {
			rotatedBuses[(i + shiftBy) % n] = buses[i];
		}

		return rotatedBuses;
	}

	public static List<String[]> generateRotation(String[] buses, List<String> leaveBus) {
		String[] rotated = new String[buses.length - leaveBus.size()];

		if (!leaveBus.isEmpty()) {
			int nullCount = 0;
			boolean startBusFound = false;

			for (String bus : buses) {
				if (!startBusFound && bus.equals(lastBus)) {
					startBusFound = true;
				}

				if (startBusFound && !leaveBus.contains(bus)) {
					rotated[nullCount++] = bus;
				}
			}

			for (String bus : buses) {
				if (nullCount >= rotated.length) {
					break;
				}

				if (!leaveBus.contains(bus) && !Arrays.asList(rotated).contains(bus)) {
					rotated[nullCount++] = bus;
				}
			}
		} else {
			System.arraycopy(buses, 0, rotated, 0, buses.length - leaveBus.size());
		}

		return Collections.singletonList(rotated);
	}

	private static List<String[]> getRotations(String[] elements, int leavePoint) {
		int length = elements.length;
		String[] rotated = new String[length];

		System.arraycopy(elements, leavePoint, rotated, 0, length - leavePoint);
		System.arraycopy(elements, 0, rotated, length - leavePoint, leavePoint);

		reverse(rotated);

		return Collections.singletonList(rotated);
	}

	public static void reverse(String[] array) {
		for (int i = 0; i < array.length / 2; i++) {
			String temp = array[i];
			array[i] = array[array.length - 1 - i];
			array[array.length - 1 - i] = temp;
		}
	}

	public static List<String> nextRotationForEdit(List<String> buses, int shiftBy) {
		int n = buses.size();
		List<String> rotatedBuses = new ArrayList<>(Collections.nCopies(n, ""));

		for (int i = 0; i < n; i++) {
			rotatedBuses.set((i + shiftBy) % n, buses.get(i));
		}

		return rotatedBuses;
	}

	public static List<String> getFinalNextRotationForEdit(List<String> busesWithFix, List<String> busesWithoutFix) {
		List<String> rotatedBuses = new ArrayList<>();
		HashMap<Integer, String> indexesOfStartBus = new HashMap<>();
		int index = 0;

		for (int i = 0; i < busesWithFix.size(); i++) {
			if (busesWithFix.get(i).contains("F")) {
				indexesOfStartBus.put(i, busesWithFix.get(i));
			}
		}

		for (int i = 0; i < busesWithFix.size(); i++) {
			if (indexesOfStartBus.containsKey(i)) {
				rotatedBuses.add(indexesOfStartBus.get(i));
			} else {
				rotatedBuses.add(busesWithoutFix.get(index));
				index++;
			}
		}

		return rotatedBuses;
	}

	public void busManagerTwoDayRotation(int noOfTrips) {
		List<RouteScheduleDTO> busListSample = new ArrayList<>();
		List<RouteScheduleDTO> busNoList = new ArrayList<RouteScheduleDTO>();
		busForRouteList = new ArrayList<>();
		fullBusNoList = new ArrayList<>();
		leaveForRouteList = new ArrayList<>();
		selectedleaveForRouteList = new ArrayList<RouteScheduleDTO>();
		List<String> leavePositionForRouteList = new ArrayList<>();
		List<String> leaveBusList = new ArrayList<>();
		List<String> lastBusList = new ArrayList<>();
		List<String> FullbusListWithoutLeave = new ArrayList<>();
		LinkedHashSet<String> uniqueCharsWithoutLeave = new LinkedHashSet<>(FullbusListWithoutLeave);
		LinkedHashSet<String> uniqueCharsLeave = new LinkedHashSet<>();
		LinkedHashSet<String> uniqueChars = new LinkedHashSet<>();
		LinkedHashSet<String> uniqueCharsWithoutFix = new LinkedHashSet<>();
		LinkedHashMap<LinkedHashSet<String>, RouteScheduleHelperDTO> panelGeneratorRouteBusList = new LinkedHashMap<>();
		LinkedHashSet<String> panelGeneratorRouteLeaveBusList = new LinkedHashSet<>();
		tripID = 1;
		int noOfTripsPerSide = 0;
		int fixCount = 0;
		int leaveCount = 0;

		TimeTableDTO routeDTO = timeTableService
				.getRouteDataForEditPanelGenerator(routeScheduleDTO.getGeneratedRefNo());

		noOfTripsPerSide = tripType.equalsIgnoreCase("O") ? routeDTO.getNoOfTripsOrigin()
				: routeDTO.getNoOfTripsDestination();

		TimeTableDTO abbriDTO = timeTableService.getAbriviatiosForRoute(routeDTO.getRouteNo(),
				routeDTO.getBusCategory());

		if (tripType.equalsIgnoreCase("O")) {
			panelGeneratorRouteBusList = timeTableService.getBusFixAndNotFixed(routeScheduleDTO.getGeneratedRefNo(),
					abbriDTO.getAbbriAtOrigin(), "O");
			RouteScheduleHelperDTO leaveBus = timeTableService.getLeaveBusesList(routeScheduleDTO.getGeneratedRefNo(),
					abbriDTO.getAbbriAtOrigin(), "O");

			Map.Entry<LinkedHashSet<String>, RouteScheduleHelperDTO> entry = panelGeneratorRouteBusList.entrySet()
					.stream().skip(0).findFirst().orElse(null);

			if (entry != null) {
				RouteScheduleHelperDTO dto = entry.getValue();
				uniqueCharsWithoutFix = dto.getBusSet();
				FullbusListWithoutLeave = dto.getBusList();

				uniqueCharsWithoutLeave.addAll(FullbusListWithoutLeave);

				uniqueCharsLeave = leaveBus.getBusSet();

				uniqueChars.addAll(uniqueCharsWithoutLeave);
				uniqueChars.addAll(uniqueCharsLeave);

				fullBusNoList.addAll(FullbusListWithoutLeave);
				fullBusNoList.addAll(leaveBus.getBusList());

				fixCount = entry.getKey().size();
				leaveCount = uniqueCharsLeave.size();
				noOfTripsPerSide = uniqueChars.size();
			}
		} else if (tripType.equalsIgnoreCase("D")) {
			panelGeneratorRouteBusList = timeTableService.getBusFixAndNotFixed(routeScheduleDTO.getGeneratedRefNo(),
					abbriDTO.getAbbriAtDestination(), "D");
			RouteScheduleHelperDTO leaveBus = timeTableService.getLeaveBusesList(routeScheduleDTO.getGeneratedRefNo(),
					abbriDTO.getAbbriAtDestination(), "D");

			Map.Entry<LinkedHashSet<String>, RouteScheduleHelperDTO> entry = panelGeneratorRouteBusList.entrySet()
					.stream().skip(0).findFirst().orElse(null);

			if (entry != null) {
				RouteScheduleHelperDTO dto = entry.getValue();
				uniqueCharsWithoutFix = dto.getBusSet();
				FullbusListWithoutLeave = dto.getBusList();

				uniqueCharsWithoutLeave.addAll(FullbusListWithoutLeave);

				uniqueCharsLeave = leaveBus.getBusSet();

				uniqueChars.addAll(uniqueCharsWithoutLeave);
				uniqueChars.addAll(uniqueCharsLeave);

				fullBusNoList.addAll(FullbusListWithoutLeave);
				fullBusNoList.addAll(leaveBus.getBusList());

				fixCount = entry.getKey().size();
				leaveCount = uniqueCharsLeave.size();
				noOfTripsPerSide = uniqueChars.size();
			}
		}

		if (uniqueChars != null && !uniqueChars.isEmpty() && uniqueChars.size() != 0) {
			List<String> busListWithoutLeave = new ArrayList<>(uniqueCharsWithoutLeave);
			String[] busArray = uniqueChars.toArray(new String[uniqueChars.size()]);
			if (!leavePositionList.isEmpty()) {
				leavePositionForRouteList.addAll(leavePositionList);
				int[] position = new int[leavePositionForRouteList.size()];
				List<String[]> allRotations = new ArrayList<>();
				int stopPoint = 0;
				if (fixCount == 0) {
					for (int c = 0; c < leavePositionForRouteList.size(); c++) {
						leaveBusList = new ArrayList<>();
						lastBusList = new ArrayList<>();
						stopPoint = 0;

						int e = Integer.valueOf(leavePositionForRouteList.get(c));
						List<String[]> rotations = getRotations(busArray, e);
						allRotations.addAll(rotations);
						position[c] = e;

						for (String[] array : rotations) {
							Collections.addAll(lastBusList, array);
						}

						int listSize = lastBusList.size();
						for (int i = 0; i < maximumDays; i++) {
							leaveBusList.add(lastBusList.get(stopPoint));
							stopPoint = (stopPoint + 1) % listSize;
						}

						RouteScheduleDTO dto = new RouteScheduleDTO();
						dto.setTripId(leavePositionForRouteList.get(c));
						dto.setLeaveBusNoList(leaveBusList);
						leaveForRouteList.add(dto);
					}
					selectedleaveForRouteList.addAll(leaveForRouteList);
					int realSize = 0;
					List<String[]> allRotationsMain = new ArrayList<>();
					lastBus = busArray[0];
					LinkedHashMap<String, String> order = new LinkedHashMap<>();
					List<RouteScheduleDTO> busForRouteListSample = new ArrayList<>();

					for (int i = 0; i < uniqueChars.size(); i++) {
						List<String> leaveIndex = new ArrayList<>(allRotations.size());

						for (String[] rotation : allRotations) {
							leaveIndex.add(rotation[i]);
						}

						List<String[]> rotations = generateRotation(busArray, leaveIndex);
						allRotationsMain.addAll(rotations);

						String[] firstRotation = rotations.get(0);
						lastBus = firstRotation[firstRotation.length - 1];
					}

					List<String> stringBusNoList = new ArrayList<>();
					tripID = 1;

					int index = (uniqueChars.size() > maximumDays) ? uniqueChars.size() : maximumDays;
					for (int i = 0; i < allRotationsMain.get(0).length; i++) {
						stopPoint = 0;
						stringBusNoList = new ArrayList<>();
						List<String> leaveIndex = new ArrayList<>();
						for (String[] rotation : allRotationsMain) {
							leaveIndex.add(rotation[i]);
						}

						int listSize = lastBusList.size();

						for (int c = 0; c < index; c++) {
							stringBusNoList.add(leaveIndex.get(stopPoint));
							stopPoint = (stopPoint + 1) % listSize;
						}

						RouteScheduleDTO dto = new RouteScheduleDTO();
						dto.setBusNoList(stringBusNoList);
						order.put(busListWithoutLeave.get(i), stringBusNoList.get(0));
						busForRouteListSample.add(dto);

					}

					int routeIndex = 0;
					int fullSize = FullbusListWithoutLeave.size();

					while (routeIndex < fullSize) {
						String busNo = order.get(fullBusNoList.get(routeIndex++));

						for (RouteScheduleDTO busSample : busForRouteListSample) {
							if (busSample.getBusNoList().get(0).equalsIgnoreCase(busNo)) {
								RouteScheduleDTO dto = new RouteScheduleDTO();
								dto.setTripId(String.valueOf(tripID++));
								dto.setBusNoList(busSample.getBusNoList());
								busForRouteList.add(dto);
							}
						}
					}

					int dayOneBusCount = timeTableService.getDayOneBusCount(routeScheduleDTO.getGeneratedRefNo(),
							tripType);
					int fullBusCountWithoutLeave = uniqueCharsWithoutLeave.size();
					int fullBusCount = uniqueChars.size();
					List<String> leavePointsNew = new ArrayList<>();

					/* Issue */
					lastBus = busForRouteList.get(dayOneBusCount - 1).getBusNoList().get(fullBusCount - 1);
					if (dayOneBusCount <= busForRouteList.size() - 1) {
						firstBusSecondDay = busForRouteList.get(dayOneBusCount).getBusNoList().get(0);
					} else {
						firstBusSecondDay = busForRouteList.get(dayOneBusCount - 1).getBusNoList().get(0);
					}

					busManagerTwoDayRotationNextSide(uniqueChars, fullBusCountWithoutLeave - dayOneBusCount,
							leavePointsNew, fullBusCountWithoutLeave);

					mainSaveList.addAll(busForRouteList);
					renderTableOne = true;
					if (!leaveForRouteList.isEmpty()) {
						renderTableTwo = true;
					}
				} else {
					String[] busArrayWithoutFix = uniqueCharsWithoutFix
							.toArray(new String[uniqueCharsWithoutFix.size()]);

					for (int c = 0; c < leavePositionForRouteList.size(); c++) {
						leaveBusList = new ArrayList<>();
						lastBusList = new ArrayList<>();
						stopPoint = 0;

						int e = Integer.valueOf(leavePositionForRouteList.get(c));
						List<String[]> rotations = getRotations(busArrayWithoutFix, e);
						allRotations.addAll(rotations);
						position[c] = e;

						for (String[] array : rotations) {
							Collections.addAll(lastBusList, array);
						}

						int listSize = lastBusList.size();
						for (int i = 0; i < maximumDays; i++) {
							leaveBusList.add(lastBusList.get(stopPoint));
							stopPoint = (stopPoint + 1) % listSize;
						}

						RouteScheduleDTO dto = new RouteScheduleDTO();
						dto.setTripId(leavePositionForRouteList.get(c));
						dto.setLeaveBusNoList(leaveBusList);
						leaveForRouteList.add(dto);
					}
					selectedleaveForRouteList.addAll(leaveForRouteList);
					tripID = 0;
					int realSize = 0;
					int index = 0;
					int finalIndex = 0;
					List<String> leaveIndex = new ArrayList<>();
					List<String[]> allRotationsMain = new ArrayList<>();
					lastBus = busArrayWithoutFix[0];
					LinkedHashMap<String, String> order = new LinkedHashMap<>();
					List<RouteScheduleDTO> busForRouteListSample = new ArrayList<>();

					for (int i = 0; i < uniqueChars.size(); i++) {
						if (busArray[i].endsWith("F")) {
							String bus = busArray[i];

							if (fixCount < busArrayWithoutFix.length) {
								index = 0;
								List<String[]> rotation = generateRotationWithoutleaveMainFix(bus,
										busArrayWithoutFix.length - fixCount);
								allRotationsMain.addAll(rotation);
								busArray[i] = null;

								index++;
								finalIndex += index;
							} else if(busArrayWithoutFix.length != 0){
								index = 0;
								List<String[]> rotation = generateRotationWithoutleaveMainFix(bus,
										busArrayWithoutFix.length);
								allRotationsMain.addAll(rotation);
								busArray[i] = null;

								index++;
								finalIndex += index;
							}else {
								index = 0;
								List<String[]> rotation = generateRotationWithoutleaveMainFix(bus, 1);
								allRotationsMain.addAll(rotation);
								busArray[i] = null;

								index++;
								finalIndex += index;
							}
							
						} else {
							leaveIndex = new ArrayList<>();
							for (String[] rotation : allRotations) {
								leaveIndex.add(rotation[i - finalIndex]);
							}
							List<String[]> rotations = generateRotation(busArrayWithoutFix, leaveIndex);
							if(rotations.get(0).length != 0) {
								allRotationsMain.addAll(rotations);
								String[] firstRotation = rotations.get(0);
								lastBus = firstRotation[firstRotation.length - 1];
							}
						}

					}

					boolean continueFlow = true;
					List<String> stringBusNoList = new ArrayList<>();
					int indexFix = 0;
					int indexFixFinal = 0;
					tripID = 1;

					for (int i = 0; i < allRotationsMain.size(); i++) {
						stopPoint = 0;
						stringBusNoList = new ArrayList<>();
						leaveIndex = new ArrayList<>();
						for (int count = 0; count < allRotationsMain.size(); count++) {
							if (allRotationsMain.get(i)[0].endsWith("F")) {
								indexFix = 0;
								String fixBus = allRotationsMain.get(i)[0];
								String extractedBus =  fixBus.replaceAll("F$", "");
								leaveIndex.add(extractedBus);
								indexFix++;
								if (count == allRotationsMain.size() - 1) {
									indexFixFinal += indexFix;
								}

							} else {
								int sizeOfFirstArray = allRotationsMain.get(0).length;
								if (i - indexFixFinal >= sizeOfFirstArray) {
									continueFlow = false;
									break;
								} else {
									if (!allRotationsMain.get(count)[0].endsWith("F")) {
										leaveIndex.add(allRotationsMain.get(count)[i - indexFixFinal]);
									}
								}

							}
						}

						int indexFlow = (uniqueChars.size() > maximumDays) ? uniqueChars.size() : maximumDays;
						if (continueFlow) {
							int listSize = leaveIndex.size();
							for (int c = 0; c < indexFlow; c++) {
								stringBusNoList.add(leaveIndex.get(stopPoint++));
								stopPoint = (stopPoint + 1) % listSize;
							}

							RouteScheduleDTO dto = new RouteScheduleDTO();
							dto.setBusNoList(stringBusNoList);
							order.put(busListWithoutLeave.get(i), stringBusNoList.get(0));
							busForRouteListSample.add(dto);

						}

					}

					int routeIndex = 0;
					int fullSize = FullbusListWithoutLeave.size();

					while (routeIndex < fullSize) {
						String busNo = order.get(fullBusNoList.get(routeIndex++));

						for (RouteScheduleDTO busSample : busForRouteListSample) {
							if (busSample.getBusNoList().get(0).equalsIgnoreCase(busNo)) {
								RouteScheduleDTO dto = new RouteScheduleDTO();
								dto.setTripId(String.valueOf(tripID++));
								dto.setBusNoList(busSample.getBusNoList());
								busForRouteList.add(dto);
							}
						}
					}

					int dayOneBusCount = timeTableService.getDayOneBusCount(routeScheduleDTO.getGeneratedRefNo(),
							tripType);
					int fullBusCountWithoutLeave = uniqueCharsWithoutLeave.size();
					int fullBusCount = uniqueChars.size();

					List<String> leavePointsNew = new ArrayList<>();
					for (RouteScheduleDTO data : leaveForRouteList) {
						String leavePointBus = data.getLeaveBusNoList().get(fullBusCount);
						int indexes = new ArrayList<>(uniqueChars).indexOf(leavePointBus);
						leavePointsNew.add(String.valueOf(indexes));
					}

					busManagerTwoDayRotationNextSide(uniqueChars, fullBusCountWithoutLeave - dayOneBusCount,
							leavePointsNew, fullBusCountWithoutLeave);

					mainSaveList.addAll(busForRouteList);
					renderTableOne = true;
					if (!leaveForRouteList.isEmpty()) {
						renderTableTwo = true;
					}
				}

			} else {
				List<String[]> allRotations = new ArrayList<>();
				List<String> leaveIndex = new ArrayList<>();
				String bus = null;
				int shifBy = 0;

				for (int i = 0; i < uniqueChars.size(); i++) {
					if (busArray[i].endsWith("F")) {
						bus = busArray[i];
						if (fixCount != busArray.length) {
							List<String[]> rotation = generateRotationWithoutleaveMainFix(bus,
									busArray.length - fixCount);
							allRotations.addAll(rotation);
							busArray[i] = null;
						} else {
							List<String[]> rotation = generateRotationWithoutleaveMainFix(bus, busArray.length);
							allRotations.addAll(rotation);
							busArray[i] = null;
						}
					} else {
						if (fixCount == 0) {
							List<String[]> rotation = generateRotationWithoutleaveMain(busArray, i);
							allRotations.addAll(rotation);
						} else {
							int nonNullCount = 0;
							String[] rotated = new String[busArray.length - fixCount];
							for (String bus1 : busArray) {
								if (bus1 != null && !bus1.endsWith("F")) {
									rotated[nonNullCount++] = bus1;
								}
							}

							List<String[]> rotation = generateRotationWithoutleaveMain(rotated, shifBy);
							allRotations.addAll(rotation);
							shifBy++;
						}

					}

				}

				List<String> stringBusNoList = new ArrayList<>();
				int dayOneBusCount = timeTableService.getDayOneBusCount(routeScheduleDTO.getGeneratedRefNo(), tripType);
				int fullBusCountWithoutLeave = uniqueCharsWithoutLeave.size();
				int fullBusCount = uniqueChars.size();
				tripID = 1;
				int indexFlow = (uniqueChars.size() > maximumDays) ? uniqueChars.size() : maximumDays;
				LinkedHashMap<String, String> order = new LinkedHashMap<>();
				List<RouteScheduleDTO> busForRouteListSample = new ArrayList<>();

				if (fixCount == 0) {
					for (int i = 0; i < allRotations.get(0).length; i++) {
						int stopPoint = 0;
						stringBusNoList = new ArrayList<>();
						leaveIndex = new ArrayList<>();

						for (String[] array : allRotations) {
							leaveIndex.add(array[i]);
						}

						int listSize = leaveIndex.size();
						for (int c = 0; c < indexFlow; c++) {
							stringBusNoList.add(leaveIndex.get(stopPoint));
							stopPoint = (stopPoint + 1) % listSize;
						}

						RouteScheduleDTO dto = new RouteScheduleDTO();
						dto.setBusNoList(stringBusNoList);
						order.put(busListWithoutLeave.get(i), stringBusNoList.get(0));
						busForRouteListSample.add(dto);

					}

					int routeIndex = 0;
					int fullSize = FullbusListWithoutLeave.size();

					while (routeIndex < fullSize) {
						String busNo = order.get(fullBusNoList.get(routeIndex++));

						for (RouteScheduleDTO busSample : busForRouteListSample) {
							if (busSample.getBusNoList().get(0).equalsIgnoreCase(busNo)) {
								RouteScheduleDTO dto = new RouteScheduleDTO();
								dto.setTripId(String.valueOf(tripID++));
								dto.setBusNoList(busSample.getBusNoList());
								busForRouteList.add(dto);
							}
						}
					}

					lastBus = busForRouteList.get(dayOneBusCount).getBusNoList().get(fullBusCount - 1);
				} else {
					int index = -1;
					int indexFix = 0;
					int indexFixFinal = 0;
					tripID = 1;

					for (int i = 0; i < allRotations.size(); i++) {
						int stopPoint = 0;
						stringBusNoList = new ArrayList<>();
						leaveIndex = new ArrayList<>();
						index++;
						for (int count = 0; count < allRotations.size(); count++) {
							if (allRotations.get(i)[0].endsWith("F")) {
								indexFix = 0;
								String fixBus = allRotations.get(i)[0];
								String extractedBus = fixBus.replaceAll("F$", "");
								leaveIndex.add(extractedBus);
								indexFix++;
								if (count == allRotations.size() - 1) {
									indexFixFinal += indexFix;
								}

							} else {
								if (!allRotations.get(count)[0].endsWith("F")) {
									leaveIndex.add(allRotations.get(count)[i - indexFixFinal]);
								}

							}

						}

						int listSize = leaveIndex.size();
						for (int c = 0; c < indexFlow; c++) {
							stringBusNoList.add(leaveIndex.get(stopPoint));
							stopPoint = (stopPoint + 1) % listSize;
						}

						RouteScheduleDTO dto = new RouteScheduleDTO();
						dto.setBusNoList(stringBusNoList);
						order.put(busListWithoutLeave.get(i), stringBusNoList.get(0));
						busForRouteListSample.add(dto);
					}

					int routeIndex = 0;
					int fullSize = FullbusListWithoutLeave.size();

					while (routeIndex < fullSize) {
						String busNo = order.get(fullBusNoList.get(routeIndex++));

						for (RouteScheduleDTO busSample : busForRouteListSample) {
							if (busSample.getBusNoList().get(0).equalsIgnoreCase(busNo)) {
								RouteScheduleDTO dto = new RouteScheduleDTO();
								dto.setTripId(String.valueOf(tripID++));
								dto.setBusNoList(busSample.getBusNoList());
								busForRouteList.add(dto);
							}
						}
					}
				}

				List<String> leavePointsNew = new ArrayList<>();
				for (RouteScheduleDTO data : leaveForRouteList) {
					String leavePointBus = data.getBusNoList().get(fullBusCount);
					int indexes = new ArrayList<>(uniqueChars).indexOf(leavePointBus);
					leavePointsNew.add(String.valueOf(indexes));
				}

				busManagerTwoDayRotationNextSide(uniqueChars, fullBusCountWithoutLeave - dayOneBusCount, leavePointsNew,
						fullBusCountWithoutLeave);

				mainSaveList.addAll(busForRouteList);
				renderTableOne = true;
			}
		} else {
			sessionBackingBean.setMessage("There are no buses for selected data");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
			return;
		}

	}

	public void busManagerTwoDayRotationNextSide(LinkedHashSet<String> busList, int indexOfBus,
			List<String> leavePositionList, int fullBusCountWithoutLeave) {
		List<RouteScheduleDTO> busListSample = new ArrayList<>();
		List<RouteScheduleDTO> busNoList = new ArrayList<RouteScheduleDTO>();
		fullBusNoList = new ArrayList<>();
		leaveBusTemp = new ArrayList<>();
		busForRouteListForTwoDay = new ArrayList<>();
		selectedleaveForRouteListNextSide = new ArrayList<>();
		List<String> leavePositionForRouteList = new ArrayList<>();
		List<String> leaveBusList = new ArrayList<>();
		List<String> lastBusList = new ArrayList<>();
		List<String> FullbusListWithoutLeave = new ArrayList<>();
		LinkedHashSet<String> uniqueCharsWithoutLeave = new LinkedHashSet<>(FullbusListWithoutLeave);
		LinkedHashSet<String> uniqueCharsLeave = new LinkedHashSet<>();
		LinkedHashSet<String> uniqueChars = new LinkedHashSet<>();
		LinkedHashSet<String> uniqueCharsWithoutFix = new LinkedHashSet<>();
		LinkedHashMap<LinkedHashSet<String>, RouteScheduleHelperDTO> panelGeneratorRouteBusList = new LinkedHashMap<>();
		LinkedHashSet<String> panelGeneratorRouteLeaveBusList = new LinkedHashSet<>();
		tripID = 1;
		int noOfTripsPerSide = 0;
		int fixCount = 0;
		int leaveCount = 0;

		List<String> busCheck = new ArrayList<>();
		List<String> fullBusList = new ArrayList<>();

		TimeTableDTO routeDTO = timeTableService
				.getRouteDataForEditPanelGenerator(routeScheduleDTO.getGeneratedRefNo());

		noOfTripsPerSide = tripType.equalsIgnoreCase("O") ? routeDTO.getNoOfTripsOrigin()
				: routeDTO.getNoOfTripsDestination();

		TimeTableDTO abbriDTO = timeTableService.getAbriviatiosForRoute(routeDTO.getRouteNo(),
				routeDTO.getBusCategory());

		if (tripType.equalsIgnoreCase("D")) {
			panelGeneratorRouteBusList = timeTableService.getBusFixAndNotFixed(routeScheduleDTO.getGeneratedRefNo(),
					abbriDTO.getAbbriAtDestination(), "O");
			RouteScheduleHelperDTO leaveBus = timeTableService.getLeaveBusesList(routeScheduleDTO.getGeneratedRefNo(),
					abbriDTO.getAbbriAtDestination(), "O");

			Map.Entry<LinkedHashSet<String>, RouteScheduleHelperDTO> entry = panelGeneratorRouteBusList.entrySet()
					.stream().skip(0).findFirst().orElse(null);

			if (entry != null) {
				RouteScheduleHelperDTO dto = entry.getValue();
				uniqueCharsWithoutFix = dto.getBusSet();
				FullbusListWithoutLeave = dto.getBusList();

				uniqueCharsWithoutLeave.addAll(FullbusListWithoutLeave);

				uniqueCharsLeave = leaveBus.getBusSet();

				fixCount = entry.getKey().size();
				
				if(fixCount > 0) {
					uniqueChars.addAll(uniqueCharsWithoutLeave);
					uniqueChars.addAll(uniqueCharsLeave);
				}else {
					uniqueChars.addAll(busList);
				}

				fullBusNoList.addAll(FullbusListWithoutLeave);
				fullBusNoList.addAll(leaveBus.getBusList());

				leaveCount = uniqueCharsLeave.size();
				noOfTripsPerSide = uniqueChars.size();
			}
		} else if (tripType.equalsIgnoreCase("O")) {
			panelGeneratorRouteBusList = timeTableService.getBusFixAndNotFixed(routeScheduleDTO.getGeneratedRefNo(),
					abbriDTO.getAbbriAtOrigin(), "D");
			RouteScheduleHelperDTO leaveBus = timeTableService.getLeaveBusesList(routeScheduleDTO.getGeneratedRefNo(),
					abbriDTO.getAbbriAtOrigin(), "D");

			Map.Entry<LinkedHashSet<String>, RouteScheduleHelperDTO> entry = panelGeneratorRouteBusList.entrySet()
					.stream().skip(0).findFirst().orElse(null);

			if (entry != null) {
				RouteScheduleHelperDTO dto = entry.getValue();
				uniqueCharsWithoutFix = dto.getBusSet();
				FullbusListWithoutLeave = dto.getBusList();

				uniqueCharsWithoutLeave.addAll(FullbusListWithoutLeave);

				uniqueCharsLeave = leaveBus.getBusSet();

				fixCount = entry.getKey().size();
				
				if(fixCount > 0) {
					uniqueChars.addAll(uniqueCharsWithoutLeave);
					uniqueChars.addAll(uniqueCharsLeave);
				}else {
					uniqueChars.addAll(busList);
				}

				fullBusNoList.addAll(FullbusListWithoutLeave);
				fullBusNoList.addAll(leaveBus.getBusList());

				
				leaveCount = uniqueCharsLeave.size();
				noOfTripsPerSide = uniqueChars.size();
			}
		}

		if (uniqueChars != null && !uniqueChars.isEmpty() && uniqueChars.size() != 0) {
			List<String> busListWithoutLeave = new ArrayList<>(uniqueCharsWithoutLeave);
			if (!leavePositionListTwoDay.isEmpty()) {

				HashMap<String[], int[]> mainBus = new HashMap<String[], int[]>();
				int[] position = new int[leavePositionListTwoDay.size()];
				List<String[]> allRotations = new ArrayList<>();
				List<String> newList = new ArrayList<>();
				int stopPoint = 0;
				if (fixCount == 0) {
					String[] busArray = uniqueChars.toArray(new String[uniqueChars.size()]);

					for (int c = 0; c < leavePositionListTwoDay.size(); c++) {
						leaveBusList = new ArrayList<>();
						lastBusList = new ArrayList<>();
						stopPoint = 0;

						int e = Integer.valueOf(leavePositionListTwoDay.get(c));
						List<String[]> rotations = getRotations(busArray, e);
						allRotations.addAll(rotations);
						position[c] = e;

						for (String[] array : rotations) {
							Collections.addAll(lastBusList, array);
						}

						int listSize = lastBusList.size();
						for (int i = 0; i < maximumDays; i++) {
							leaveBusList.add(lastBusList.get(stopPoint));
							stopPoint = (stopPoint + 1) % listSize;
						}

						RouteScheduleDTO dto = new RouteScheduleDTO();
						dto.setTripId(leavePositionListTwoDay.get(c));
						dto.setLeaveBusNoList(leaveBusList);
						leaveBusTemp.add(dto);
					}
					selectedleaveForRouteListNextSide.addAll(leaveBusTemp);
					int realSize = 0;
					int indexOfTargetBus = -1;
					List<String[]> allRotationsMain = new ArrayList<>();
					LinkedHashMap<String, String> order = new LinkedHashMap<>();
					List<RouteScheduleDTO> busForRouteListForTwoDaySample = new ArrayList<>();

					for (int i = 0; i < uniqueChars.size(); i++) {
						List<String> leaveIndex = new ArrayList<>();
						for (String[] rotation : allRotations) {
							leaveIndex.add(rotation[i]);
						}

						List<String[]> rotations = generateRotation(busArray, leaveIndex);
						allRotationsMain.addAll(rotations);

						if (i != 0) {
							String[] firstRotation = rotations.get(0);
							lastBus = firstRotation[firstRotation.length - 1];
						} else {
							lastBus = firstBusSecondDay;
						}

					}
					List<String> stringBusNoList = new ArrayList<>();
					tripID = 1;

					for (String bus : uniqueChars) {
						if (bus.equals(lastBus)) {
							break;
						}
						busCheck.add(bus);
					}

					for (int i = 0; i < allRotationsMain.get(0).length; i++) {
						stopPoint = 0;
						stringBusNoList = new ArrayList<>();
						List<String> leaveIndex = new ArrayList<>();

						for (String[] rotation : allRotationsMain) {
							leaveIndex.add(rotation[i]);
						}

						int listSize = lastBusList.size();
						for (int c = 0; c < maximumDays; c++) {
							stringBusNoList.add(leaveIndex.get(stopPoint));
							stopPoint = (stopPoint + 1) % listSize;
						}

						RouteScheduleDTO dto = new RouteScheduleDTO();
						dto.setBusNoList(stringBusNoList);
						order.put(busListWithoutLeave.get(i), stringBusNoList.get(0));
						busForRouteListForTwoDaySample.add(dto);
					}

					int routeIndex = 0;
					int fullSize = FullbusListWithoutLeave.size();
					tripID = 1;

					while (routeIndex < fullSize) {
						String busNo = order.get(fullBusNoList.get(routeIndex++));

						for (RouteScheduleDTO busSample : busForRouteListForTwoDaySample) {
							if (busSample.getBusNoList().get(0).equalsIgnoreCase(busNo)) {
								RouteScheduleDTO dto = new RouteScheduleDTO();
								dto.setTripId(String.valueOf(tripID++));
								dto.setBusNoList(busSample.getBusNoList());
								busForRouteListForTwoDay.add(dto);
							}
						}
					}

					mainSaveForTwoDay.addAll(busForRouteListForTwoDay);
					renderTableThree = true;
					renderLeave = true;
				} else {
					String[] busArrayWithoutFix = uniqueCharsWithoutFix
							.toArray(new String[uniqueCharsWithoutFix.size()]);
					String[] busArray = uniqueChars.toArray(new String[uniqueChars.size()]);

					for (int c = 0; c < leavePositionListTwoDay.size(); c++) {
						leaveBusList = new ArrayList<>();
						lastBusList = new ArrayList<>();
						stopPoint = 0;

						int e = Integer.valueOf(leavePositionListTwoDay.get(c));
						List<String[]> rotations = getRotations(busArrayWithoutFix, e + 1);
						allRotations.addAll(rotations);
						position[c] = e;

						for (String[] array : rotations) {
							Collections.addAll(lastBusList, array);
						}

						int listSize = lastBusList.size();
						for (int i = 0; i < maximumDays; i++) {
							leaveBusList.add(lastBusList.get(stopPoint));
							stopPoint = (stopPoint + 1) % listSize;
						}

						RouteScheduleDTO dto = new RouteScheduleDTO();
						dto.setTripId(leavePositionListTwoDay.get(c));
						dto.setLeaveBusNoList(leaveBusList);
						leaveBusTemp.add(dto);

					}
					selectedleaveForRouteListNextSide.addAll(leaveBusTemp);
					tripID = 1;
					int index = 0;
					int finalIndex = 0;
					List<String> leaveIndex = new ArrayList<>();
					List<String[]> allRotationsMain = new ArrayList<>();

					for (int i = 0; i < uniqueChars.size(); i++) {
						if (busArray[i].endsWith("F")) {
							String bus = busArray[i];
							if (fixCount < busArrayWithoutFix.length) {
								index = 0;
								List<String[]> rotation = generateRotationWithoutleaveMainFix(bus,
										busArrayWithoutFix.length - fixCount);
								allRotationsMain.addAll(rotation);
								busArray[i] = null;

								index++;
								finalIndex += index;
							} else if(busArrayWithoutFix.length != 0){
								index = 0;
								List<String[]> rotation = generateRotationWithoutleaveMainFix(bus,
										busArrayWithoutFix.length);
								allRotationsMain.addAll(rotation);
								busArray[i] = null;

								index++;
								finalIndex += index;
							}else {
								index = 0;
								List<String[]> rotation = generateRotationWithoutleaveMainFix(bus, 1);
								allRotationsMain.addAll(rotation);
								busArray[i] = null;

								index++;
								finalIndex += index;
							}
						} else {
							int indexOfTargetBus = -1;
							leaveIndex = new ArrayList<>();

							for (String[] rotation : allRotations) {
								leaveIndex.add(rotation[i - finalIndex]);
							}

							if (i == 0) {
								for (int t = 0; t < busArray.length; t++) {
									if (busArray[t].equals(lastBus)) {
										indexOfTargetBus = t;
										break;
									}
								}
								leaveIndex.add(busArray[indexOfTargetBus + indexOfBus]);
							}

							List<String[]> rotations = generateRotation(busArrayWithoutFix, leaveIndex);
							
							if(rotations.get(0).length != 0) {
								allRotationsMain.addAll(rotations);
								String[] firstRotation = rotations.get(0);
								lastBus = firstRotation[firstRotation.length - 1];
							}	
						}

					}

					busCheck = new ArrayList<>();
					for (int i = 0; i < uniqueChars.size() && !busArray[i].equals(lastBus); i++) {
						busCheck.add(busArray[i]);
					}

					int indexFixFinal = 0;
					boolean continueFlow = true;
					int indexFix = 0;
					tripID = 1;
					LinkedHashMap<String, String> order = new LinkedHashMap<>();
					List<RouteScheduleDTO> busForRouteListForTwoDaySample = new ArrayList<>();

					for (int i = 0; i < allRotationsMain.size(); i++) {
						continueFlow = true;
						List<String> stringBusNoList = new ArrayList<>();
						leaveIndex = new ArrayList<>();

						for (int count = 0; count < allRotationsMain.size(); count++) {
							if (allRotationsMain.get(i)[0].endsWith("F")) {
								indexFix = 0;
								String fixBus = allRotationsMain.get(i)[0];
								String extractedBus =  fixBus.replaceAll("F$", "");
								leaveIndex.add(extractedBus);
								indexFix++;
								if (count == allRotationsMain.size() - 1) {
									indexFixFinal += indexFix;
								}

							} else {
								int sizeOfFirstArray = allRotationsMain.get(0).length;
								if (i - indexFixFinal >= sizeOfFirstArray) {
									continueFlow = false;
									break;
								} else {
									if (!allRotationsMain.get(count)[0].endsWith("F")) {
										leaveIndex.add(allRotationsMain.get(count)[i - indexFixFinal]);
									}
								}

							}
						}

						if (continueFlow) {
							int listSize = leaveIndex.size();
							for (int c = 0; c < maximumDays; c++) {
								stringBusNoList.add(leaveIndex.get(stopPoint++));
								stopPoint = (stopPoint + 1) % listSize;
							}

							RouteScheduleDTO dto = new RouteScheduleDTO();
							dto.setBusNoList(stringBusNoList);
							order.put(busListWithoutLeave.get(i), stringBusNoList.get(0));
							busForRouteListForTwoDaySample.add(dto);
							busForRouteListForTwoDay.add(dto);

						}

						if (i >= indexOfBus && !busCheck.contains(leaveIndex.get(0))) {
							continue;
						}
					}

					int routeIndex = 0;
					int fullSize = FullbusListWithoutLeave.size();
					tripID = 1;

					while (routeIndex < fullSize) {
						String busNo = order.get(fullBusNoList.get(routeIndex++));

						for (RouteScheduleDTO busSample : busForRouteListForTwoDaySample) {
							if (busSample.getBusNoList().get(0).equalsIgnoreCase(busNo)) {
								RouteScheduleDTO dto = new RouteScheduleDTO();
								dto.setTripId(String.valueOf(tripID++));
								dto.setBusNoList(busSample.getBusNoList());
								busForRouteListForTwoDay.add(dto);
							}
						}
					}

					mainSaveForTwoDay.addAll(busForRouteListForTwoDay);
					if (!selectedleaveForRouteListNextSide.isEmpty()) {
					}
					renderLeave = true;
					renderTableThree = true;
				}

			} else {
				List<String[]> allRotations = new ArrayList<>();
				String[] busArray = uniqueChars.toArray(new String[uniqueChars.size()]);
				List<String> leaveIndex = new ArrayList<>();
				String bus = null;
				int shifBy = 0;

				for (int i = 0; i < uniqueChars.size(); i++) {
					if (busArray[i].endsWith("F")) {
						bus = busArray[i];
						if (fixCount != busArray.length) {
							List<String[]> rotation = generateRotationWithoutleaveMainFix(bus,
									busArray.length - fixCount);
							allRotations.addAll(rotation);
							busArray[i] = null;
						} else {
							List<String[]> rotation = generateRotationWithoutleaveMainFix(bus, busArray.length);
							allRotations.addAll(rotation);
							busArray[i] = null;
						}
					} else {
						if (fixCount == 0) {
							List<String[]> rotation = generateRotationWithoutleaveMain(busArray, i);
							allRotations.addAll(rotation);
						} else {
							int nonNullCount = 0;
							String[] rotated = new String[busArray.length - fixCount];
							for (String bus1 : busArray) {
								if (bus1 != null && !bus1.endsWith("F")) {
									rotated[nonNullCount++] = bus1;
								}
							}

							List<String[]> rotation = generateRotationWithoutleaveMain(rotated, shifBy);
							allRotations.addAll(rotation);
							shifBy++;
						}

					}

				}

				List<String> stringBusNoList = new ArrayList<>();
				tripID = 1;
				LinkedHashMap<String, String> order = new LinkedHashMap<>();
				List<RouteScheduleDTO> busForRouteListForTwoDaySample = new ArrayList<>();

				if (fixCount == 0) {
					for (int i = 0; i < allRotations.get(0).length; i++) {
						int stopPoint = 0;
						stringBusNoList = new ArrayList<>();
						leaveIndex = new ArrayList<>();

						for (String[] array : allRotations) {
							leaveIndex.add(array[i]);
						}

						int listSize = leaveIndex.size();
						for (int c = 0; c < maximumDays; c++) {
							stringBusNoList.add(leaveIndex.get(stopPoint));
							stopPoint = (stopPoint + 1) % listSize;
						}

						RouteScheduleDTO dto = new RouteScheduleDTO();
						dto.setBusNoList(stringBusNoList);
						order.put(busListWithoutLeave.get(i), stringBusNoList.get(0));
						busForRouteListForTwoDaySample.add(dto);
					}
				} else {
					int index = -1;
					int indexFix = 0;
					int indexFixFinal = 0;

					for (int i = 0; i < allRotations.size(); i++) {
						int stopPoint = 0;
						stringBusNoList = new ArrayList<>();
						leaveIndex = new ArrayList<>();
						index++;
						for (int count = 0; count < allRotations.size(); count++) {
							if (allRotations.get(i)[0].endsWith("F")) {
								indexFix = 0;
								String fixBus = allRotations.get(i)[0];
								String extractedBus = fixBus.replaceAll("F$", "");
								leaveIndex.add(extractedBus);
								indexFix++;
								if (count == allRotations.size() - 1) {
									indexFixFinal += indexFix;
								}

							} else {
								if (!allRotations.get(count)[0].endsWith("F")) {
									leaveIndex.add(allRotations.get(count)[i - indexFixFinal]);
								}

							}

						}

						int listSize = leaveIndex.size();
						for (int c = 0; c < maximumDays; c++) {
							stringBusNoList.add(leaveIndex.get(stopPoint));
							stopPoint = (stopPoint + 1) % listSize;
						}

						RouteScheduleDTO dto = new RouteScheduleDTO();
						dto.setBusNoList(stringBusNoList);
						order.put(busListWithoutLeave.get(i), stringBusNoList.get(0));
						busForRouteListForTwoDaySample.add(dto);

					}
				}

				int routeIndex = 0;
				int fullSize = FullbusListWithoutLeave.size();
				tripID = 1;

				while (routeIndex < fullSize) {
					String busNo = order.get(fullBusNoList.get(routeIndex++));

					for (RouteScheduleDTO busSample : busForRouteListForTwoDaySample) {
						if (busSample.getBusNoList().get(0).equalsIgnoreCase(busNo)) {
							RouteScheduleDTO dto = new RouteScheduleDTO();
							dto.setTripId(String.valueOf(tripID++));
							dto.setBusNoList(busSample.getBusNoList());
							busForRouteListForTwoDay.add(dto);
						}
					}
				}

				mainSaveForTwoDay.addAll(busForRouteListForTwoDay);
				renderTableThree = true;

			}
		} else {
			sessionBackingBean.setMessage("There are no buses for selected data");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
			return;
		}

	}

	public void busNoManagerNormalEditNew(int noOfTrips) {

		List<RouteScheduleDTO> busListSample = new ArrayList<>();
		busForRouteList = new ArrayList<>();
		fullBusNoList = new ArrayList<>();
		leaveForRouteList = new ArrayList<>();
		List<String> leavePositionForRouteList = new ArrayList<>();
		List<String> leaveBusList = new ArrayList<>();
		List<String> lastBusList = new ArrayList<>();
		LinkedHashSet<String> uniqueChars = new LinkedHashSet<>();
		LinkedHashSet<String> uniqueCharsWithoutFix = new LinkedHashSet<>();
		tripID = 0;
		boolean first = false;
		boolean couple = false;
		int noOfTripsPerSide = 0;
		int busCount = 0;
		int fixCount = 0;
		int countBreak = 0;
		int leaveCount = 0;

		List<RouteScheduleDTO> busNoList = new ArrayList<RouteScheduleDTO>();

		busForRouteList = new ArrayList<>();
		leaveForRouteList = new ArrayList<>();
		List<String> busList = new ArrayList<>();
		List<String> nextBusList = new ArrayList<>();
		tripID = 0;
		List<String> fixBusList = new ArrayList<>();
		List<String> fullBusNoListWithoutFix = new ArrayList<>();

		List<RouteScheduleDTO> busNoListEdit = new ArrayList<RouteScheduleDTO>();

		TimeTableDTO routeDTO = timeTableService
				.getRouteDataForEditPanelGenerator(routeScheduleDTO.getGeneratedRefNo());

		noOfTripsPerSide = tripType.equalsIgnoreCase("O") ? routeDTO.getNoOfTripsOrigin()
				: routeDTO.getNoOfTripsDestination();

		if (tripType.equalsIgnoreCase("O")) {
			HashMap<List<TimeTableDTO>, List<TimeTableDTO>> panelHashMap = timeTableService
					.getPreviousData(routeScheduleDTO.getGeneratedRefNo(), tripType, groupNo);

			List<List<TimeTableDTO>> keysAsList = new ArrayList<>(panelHashMap.keySet());
			List<List<TimeTableDTO>> valuesAsList = new ArrayList<>(panelHashMap.values());

			List<TimeTableDTO> panelGeneratorOriginRouteList = keysAsList.get(0);
			List<TimeTableDTO> panelGeneratorLeaveDestinationRouteList = valuesAsList.get(0);

			fixBusList = timeTableService.getOriginDtoListForScheduleFix(routeScheduleDTO.getGeneratedRefNo());

			for (TimeTableDTO panelGeneratorOriginRouteDto : panelGeneratorOriginRouteList) {
				RouteScheduleDTO o = new RouteScheduleDTO();
				o.setBusNo(panelGeneratorOriginRouteDto.getBusNoOrigin());
				busNoList.add(o);
				String bus = o.getBusNo();
				if (fixBusList.contains(bus)) {
					fullBusNoList.add(bus + "F");
				} else {
					fullBusNoList.add(bus);
					fullBusNoListWithoutFix.add(bus);
				}

			}

			for (TimeTableDTO panelGeneratorDestinationRouteDto : panelGeneratorLeaveDestinationRouteList) {
				fullBusNoList.add(panelGeneratorDestinationRouteDto.getBusNoOrigin());
			}
			
			noOfTripsPerSide = busNoList.size();

		} else if (tripType.equalsIgnoreCase("D")) {
			HashMap<List<TimeTableDTO>, List<TimeTableDTO>> panelHashMap = timeTableService
					.getPreviousData(routeScheduleDTO.getGeneratedRefNo(), tripType, groupNo);

			List<List<TimeTableDTO>> keysAsList = new ArrayList<>(panelHashMap.keySet());
			List<List<TimeTableDTO>> valuesAsList = new ArrayList<>(panelHashMap.values());

			List<TimeTableDTO> panelGeneratorDestinationRouteList = keysAsList.get(0);
			List<TimeTableDTO> panelGeneratorLeaveDestinationRouteList = valuesAsList.get(0);

			fixBusList = timeTableService.getDestinationDtoListForScheduleFix(routeScheduleDTO.getGeneratedRefNo());

			for (TimeTableDTO panelGeneratorDestinationRouteDto : panelGeneratorDestinationRouteList) {
				RouteScheduleDTO o = new RouteScheduleDTO();
				o.setBusNo(panelGeneratorDestinationRouteDto.getBusNoDestination());
				busNoList.add(o);
				String bus = o.getBusNo();
				if (fixBusList.contains(bus)) {
					fullBusNoList.add(bus + "F");
				} else {
					fullBusNoList.add(bus);
					fullBusNoListWithoutFix.add(bus);
				}
			}
			noOfTripsPerSide = busNoList.size();
			
			for (TimeTableDTO panelGeneratorDestinationRouteDto : panelGeneratorLeaveDestinationRouteList) {
				fullBusNoList.add(panelGeneratorDestinationRouteDto.getBusNoDestination());
			}
		}

		if (coupleTwo) {
			if (fixBusList.isEmpty()) {
				nextBusList = nextRotationForEdit(fullBusNoList, 1);
			} else {
				nextBusList = nextRotationForEdit(fullBusNoListWithoutFix, 1);
				nextBusList = getFinalNextRotationForEdit(fullBusNoList, nextBusList);
			}

		} else {
			if (fixBusList.isEmpty()) {
				nextBusList = nextRotationForEdit(fullBusNoList, 1);
			} else {
				nextBusList = nextRotationForEdit(fullBusNoListWithoutFix, 1);
				nextBusList = getFinalNextRotationForEdit(fullBusNoList, fullBusNoListWithoutFix);
			}
		}

		for (String data : nextBusList) {
			uniqueChars.add(data);
		}

		for (String uniqueChar : uniqueChars) {
			if (uniqueChar.endsWith("F")) {
				fixCount++;
			} else {
				uniqueCharsWithoutFix.add(uniqueChar);
			}
		}

		if (busNoList != null && !busNoList.isEmpty() && busNoList.size() != 0) {
			leavePositionList = timeTableService.getLeavePositionList(routeScheduleDTO.getGeneratedRefNo(), tripType);
			if (!leavePositionList.isEmpty()) {
				leavePositionForRouteList.addAll(leavePositionList);
				HashMap<String[], int[]> mainBus = new HashMap<String[], int[]>();
				int[] position = new int[leavePositionForRouteList.size()];
				List<String[]> allRotations = new ArrayList<>();
				int days = maximumDays / uniqueChars.size();
				int stopPoint = 0;
				if (fixCount == 0) {
					String[] busArray = uniqueChars.toArray(new String[uniqueChars.size()]);

					for (int c = 0; c < leavePositionForRouteList.size(); c++) {
						leaveBusList = new ArrayList<>();
						lastBusList = new ArrayList<>();
						stopPoint = 0;

						int e = Integer.valueOf(leavePositionForRouteList.get(c));
						List<String[]> rotations = getRotations(busArray, e);
						allRotations.addAll(rotations);
						position[c] = e;

						for (String[] array : rotations) {
							Collections.addAll(lastBusList, array);
						}

						int listSize = lastBusList.size();
						for (int i = 0; i < maximumDays; i++) {
							leaveBusList.add(lastBusList.get(stopPoint));
							stopPoint = (stopPoint + 1) % listSize;
						}

						RouteScheduleDTO dto = new RouteScheduleDTO();
						dto.setTripId(leavePositionForRouteList.get(c));
						dto.setLeaveBusNoList(leaveBusList);
						leaveForRouteList.add(dto);
					}

					List<String[]> allRotationsMain = new ArrayList<>();
					lastBus = busArray[0];

					for (int i = 0; i < uniqueChars.size(); i++) {
						List<String> leaveIndex = new ArrayList<>();

						for (String[] rotation : allRotations) {
							leaveIndex.add(rotation[i]);
						}

						List<String[]> rotations = generateRotation(busArray, leaveIndex);
						allRotationsMain.addAll(rotations);

						String[] firstRotation = rotations.get(0);
						lastBus = firstRotation[firstRotation.length - 1];
					}

					List<String> stringBusNoList = new ArrayList<>();

					for (int i = 0; i < allRotationsMain.get(0).length; i++) {
						stopPoint = 0;
						stringBusNoList = new ArrayList<>();
						List<String> leaveIndex = new ArrayList<>();

						for (String[] rotation : allRotationsMain) {
							leaveIndex.add(rotation[i]);
						}

						int listSize = lastBusList.size();
						for (int c = 0; c < maximumDays; c++) {
							stringBusNoList.add(leaveIndex.get(stopPoint));
							stopPoint = (stopPoint + 1) % listSize;
						}

						RouteScheduleDTO dto = new RouteScheduleDTO();
						dto.setBusNoList(stringBusNoList);
						busListSample.add(dto);
					}

					tripID = 1;
					int loopBreaker = 0;
					int listSize = busListSample.size();
					for (int routeIndex = 0; routeIndex < busNoList.size() - leavePositionList.size(); routeIndex++) {
						RouteScheduleDTO dto = new RouteScheduleDTO();
						dto.setBusNoList(busListSample.get(loopBreaker).getBusNoList());
						dto.setTripId(String.valueOf(tripID++));
						busForRouteList.add(dto);
						loopBreaker = (loopBreaker + 1) % listSize;
					}

					mainSaveList.addAll(busForRouteList);
					renderTableOne = true;
					if (!leaveForRouteList.isEmpty()) {
						renderTableTwo = true;
					}
				} else {
					String[] busArrayWithoutFix = uniqueCharsWithoutFix
							.toArray(new String[uniqueCharsWithoutFix.size()]);
					String[] busArray = uniqueChars.toArray(new String[uniqueChars.size()]);

					for (int c = 0; c < leavePositionForRouteList.size(); c++) {
						leaveBusList = new ArrayList<>();
						lastBusList = new ArrayList<>();
						stopPoint = 0;

						int e = Integer.parseInt(leavePositionForRouteList.get(c));
						List<String[]> rotations = getRotations(busArrayWithoutFix, e);
						allRotations.addAll(rotations);
						position[c] = e;

						for (String[] array : rotations) {
							Collections.addAll(lastBusList, array);
						}

						int listSize = lastBusList.size();
						for (int i = 0; i < maximumDays; i++) {
							leaveBusList.add(lastBusList.get(stopPoint));
							stopPoint = (stopPoint + 1) % listSize;
						}

						RouteScheduleDTO dto = new RouteScheduleDTO();
						dto.setTripId(leavePositionForRouteList.get(c));
						dto.setLeaveBusNoList(leaveBusList);
						leaveForRouteList.add(dto);

					}

					tripID = 0;
					int realSize = 0;
					int index = 0;
					int finalIndex = 0;
					List<String> leaveIndex = new ArrayList<>();
					List<String[]> allRotationsMain = new ArrayList<>();
					lastBus = busArrayWithoutFix[0];

					for (int i = 0; i < uniqueChars.size(); i++) {
						if (busArray[i].endsWith("F")) {
							String bus = busArray[i];

							if (fixCount != busArrayWithoutFix.length) {
								index = 0;
								List<String[]> rotation = generateRotationWithoutleaveMainFix(bus,
										uniqueCharsWithoutFix.size() - leaveIndex.size());
								allRotationsMain.addAll(rotation);
								busArray[i] = null;

								index++;
								finalIndex += index;
							} else {
								index = 0;
								List<String[]> rotation = generateRotationWithoutleaveMainFix(bus,
										uniqueCharsWithoutFix.size());
								allRotationsMain.addAll(rotation);
								busArray[i] = null;

								index++;
								finalIndex += index;
							}
						} else {
							leaveIndex = new ArrayList<>();

							for (String[] rotation : allRotations) {
								leaveIndex.add(rotation[i - finalIndex]);
							}

							List<String[]> rotations = generateRotation(busArrayWithoutFix, leaveIndex);
							allRotationsMain.addAll(rotations);

							String[] firstRotation = rotations.get(0);
							lastBus = firstRotation[firstRotation.length - 1];
						}
					}

					boolean continueFlow;
					busListSample = new ArrayList<>();

					for (int i = 0; i < allRotationsMain.size(); i++) {
						continueFlow = true;
						List<String> stringBusNoList = new ArrayList<>();
						leaveIndex = new ArrayList<>();
						int indexFix = 0;
						int indexFixFinal = 0;

						for (int count = 0; count < allRotationsMain.size(); count++) {
							String firstElement = allRotationsMain.get(count)[0];
							if (firstElement.endsWith("F")) {
								String extractedBus = firstElement.replaceAll("F$", "");
								leaveIndex.add(extractedBus);
								indexFix++;
								if (count == allRotationsMain.size() - 1) {
									indexFixFinal += indexFix;
								}
							} else if (i - indexFixFinal < allRotationsMain.get(0).length) {
								if (!firstElement.endsWith("F")) {
									leaveIndex.add(allRotationsMain.get(count)[i - indexFixFinal]);
								}
							} else {
								continueFlow = false;
								break;
							}
						}

						if (continueFlow) {
							stopPoint = 0;
							int listSize = leaveIndex.size();
							for (int c = 0; c < maximumDays; c++) {
								stringBusNoList.add(leaveIndex.get(stopPoint++));
								stopPoint = (stopPoint + 1) % listSize;
							}

							RouteScheduleDTO dto = new RouteScheduleDTO();
							dto.setBusNoList(stringBusNoList);
							busListSample.add(dto);
						}

					}

					tripID = 1;
					int loopBreaker = 0;
					int listSize = busListSample.size();
					for (int routeIndex = 0; routeIndex < busNoList.size() - leavePositionList.size(); routeIndex++) {
						RouteScheduleDTO dto = new RouteScheduleDTO();
						dto.setBusNoList(busListSample.get(loopBreaker).getBusNoList());
						dto.setTripId(String.valueOf(tripID++));
						busForRouteList.add(dto);
						loopBreaker = (loopBreaker + 1) % listSize;
					}

					mainSaveList.addAll(busForRouteList);
					renderTableOne = true;
					if (!leaveForRouteList.isEmpty()) {
						renderTableTwo = true;
					}
				}

			} else {
				List<String[]> allRotations = new ArrayList<>();
				int days = maximumDays / uniqueChars.size();
				String[] busArray = uniqueChars.toArray(new String[uniqueChars.size()]);
				List<String> leaveIndex = new ArrayList<>();
				String bus = null;
				int shifBy = 0;

				for (int i = 0; i < uniqueChars.size(); i++) {
					if (busArray[i].endsWith("F")) {
						bus = busArray[i];
						if (fixCount != busArray.length) {
							List<String[]> rotation = generateRotationWithoutleaveMainFix(bus,
									busArray.length - fixCount);
							allRotations.addAll(rotation);
							busArray[i] = null;
						} else {
							List<String[]> rotation = generateRotationWithoutleaveMainFix(bus, busArray.length);
							allRotations.addAll(rotation);
							busArray[i] = null;
						}
					} else {
						if (fixCount == 0) {
							List<String[]> rotation = generateRotationWithoutleaveMain(busArray, i);
							allRotations.addAll(rotation);
						} else {

							int nonNullCount = 0;
							String[] rotated = new String[busArray.length - fixCount];
							for (String bus1 : busArray) {
								if (bus1 != null && !bus1.endsWith("F")) {
									rotated[nonNullCount++] = bus1;
								}
							}

							List<String[]> rotation = generateRotationWithoutleaveMain(rotated, shifBy);
							allRotations.addAll(rotation);
							shifBy++;
						}

					}

				}

				List<String> stringBusNoList = new ArrayList<>();

				if (fixCount == 0) {
					for (int i = 0; i < allRotations.get(0).length; i++) {
						int stopPoint = 0;
						stringBusNoList = new ArrayList<>();
						leaveIndex = new ArrayList<>();

						for (String[] array : allRotations) {
							leaveIndex.add(array[i]);
						}

						int listSize = leaveIndex.size();
						for (int c = 0; c < maximumDays; c++) {
							stringBusNoList.add(leaveIndex.get(stopPoint));
							stopPoint = (stopPoint + 1) % listSize;
						}

						RouteScheduleDTO dto = new RouteScheduleDTO();
						dto.setBusNoList(stringBusNoList);
						busListSample.add(dto);
					}
				} else {
					int index = -1;
					int indexFix = 0;
					int indexFixFinal = 0;

					for (int i = 0; i < allRotations.size(); i++) {
						int stopPoint = 0;
						stringBusNoList = new ArrayList<>();
						leaveIndex = new ArrayList<>();
						index++;
						for (int count = 0; count < allRotations.size(); count++) {
							if (allRotations.get(i)[0].endsWith("F")) {
								indexFix = 0;
								String fixBus = allRotations.get(i)[0];
								String extractedBus = fixBus.replaceAll("F$", "");
								leaveIndex.add(extractedBus);
								indexFix++;
								if (count == allRotations.size() - 1) {
									indexFixFinal += indexFix;
								}

							} else {
								if (!allRotations.get(count)[0].endsWith("F")) {
									leaveIndex.add(allRotations.get(count)[i - indexFixFinal]);
								}

							}

						}

						int listSize = leaveIndex.size();
						for (int c = 0; c < maximumDays; c++) {
							stringBusNoList.add(leaveIndex.get(stopPoint));
							stopPoint = (stopPoint + 1) % listSize;
						}

						RouteScheduleDTO dto = new RouteScheduleDTO();
						dto.setBusNoList(stringBusNoList);
						busListSample.add(dto);
					}
				}

				tripID = 1;
				int loopBreaker = 0;
				int listSize = busListSample.size();
				for (int routeIndex = 0; routeIndex < busNoList.size() - leavePositionList.size(); routeIndex++) {
					RouteScheduleDTO dto = new RouteScheduleDTO();
					dto.setBusNoList(busListSample.get(loopBreaker).getBusNoList());
					dto.setTripId(String.valueOf(tripID++));
					busForRouteList.add(dto);
					loopBreaker = (loopBreaker + 1) % listSize;
				}

				mainSaveList.addAll(busForRouteList);
				renderTableOne = true;
			}
		} else {
			sessionBackingBean.setMessage("There are no buses for selected data");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
			return;
		}
	}

	public void saveAction() {

		if (!lastPanelDateVisible) {
			boolean isRelatedDataFound = routeScheduleService.isRelatedDataFound(routeScheduleDTO.getRouteNo(),
					routeScheduleDTO.getBusCategory(), routeScheduleDTO.getGeneratedRefNo(),
					routeScheduleDTO.getGroupNo(), tripType);
			int days = maximumDays;
			boolean success3 = false;

			if (isRelatedDataFound == false) {

				if (!mainSaveList.isEmpty()) {

					int day = (int) calculateNoOfDays();
					String referenceNo = routeScheduleService.generateReferenceNo();

					if (!mainSaveList.isEmpty()) {

						if (routeScheduleDTO.getNoOfLeavesOrigin() != 0
								|| routeScheduleDTO.getNoOfLeavesDestination() != 0) {

							boolean isDataSave = routeScheduleService.insertRouteScheduleMasterTableData(
									routeScheduleDTO, rotationType, tripType, routeScheduleDTO.getGroupNo(),
									routeScheduleDTO.getGeneratedRefNo(), day, sessionBackingBean.getLoginUser(),
									referenceNo);

							boolean success = routeScheduleService.insertRouteScheduleDetOneTableData(mainSaveList,
									routeScheduleDTO.getGeneratedRefNo(), sessionBackingBean.getLoginUser(),
									referenceNo, routeScheduleDTO.isSwapEnds(), days);

							boolean success1 = routeScheduleService.insertRouteScheduleDetTwoTableData(routeScheduleDTO,
									leavePositionList, routeScheduleDTO.getGeneratedRefNo(),
									sessionBackingBean.getLoginUser(), referenceNo, tripType);

							boolean success2 = routeScheduleService.updateRouteScheduleDetOneTableData(
									selectedleaveForRouteList, sessionBackingBean.getLoginUser(), referenceNo,
									routeScheduleDTO.getGeneratedRefNo(), tripType, days);

							if (!mainSaveForTwoDay.isEmpty()) {
								boolean success4 = routeScheduleService.insertRouteScheduleDetOneTableDataTwoDay(
										mainSaveForTwoDay, routeScheduleDTO.getGeneratedRefNo(),
										sessionBackingBean.getLoginUser(), referenceNo, routeScheduleDTO.isSwapEnds(),
										days);

								if (!selectedleaveForRouteListNextSide.isEmpty()) {
									boolean success5 = routeScheduleService.insertRouteScheduleDetTwoTableDataTwoDay(
											routeScheduleDTO, leavePositionListTwoDay,
											routeScheduleDTO.getGeneratedRefNo(), sessionBackingBean.getLoginUser(),
											referenceNo, tripType);

									boolean success6 = routeScheduleService.updateRouteScheduleDetOneTableDataTwoDay(
											selectedleaveForRouteListNextSide, sessionBackingBean.getLoginUser(),
											referenceNo, routeScheduleDTO.getGeneratedRefNo(), tripType, days);

									success3 = success4 && success5 && success6;
								} else {
									success3 = success4;
								}

							}

							boolean saved = mainSaveForTwoDay.isEmpty()
									? (isDataSave && success && success1 && success2)
									: (isDataSave && success && success1 && success2 && success3);

							if (saved) {
								setSuccessMessage("Saved successfully.");
								RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
							} else {
								setErrorMessage("Data not saved.");
								RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
							}
						} else {
							boolean isDataSave = routeScheduleService.insertRouteScheduleMasterTableData(
									routeScheduleDTO, rotationType, tripType, routeScheduleDTO.getGroupNo(),
									routeScheduleDTO.getGeneratedRefNo(), day, sessionBackingBean.getLoginUser(),
									referenceNo);

							boolean success = routeScheduleService.insertRouteScheduleDetOneTableData(mainSaveList,
									routeScheduleDTO.getGeneratedRefNo(), sessionBackingBean.getLoginUser(),
									referenceNo, routeScheduleDTO.isSwapEnds(), days);

							boolean success1 = routeScheduleService.insertRouteScheduleDetTwoTableData(routeScheduleDTO,
									leavePositionList, routeScheduleDTO.getGeneratedRefNo(),
									sessionBackingBean.getLoginUser(), referenceNo, tripType);

							if (!mainSaveForTwoDay.isEmpty()) {
								success3 = routeScheduleService.insertRouteScheduleDetOneTableDataTwoDay(
										mainSaveForTwoDay, routeScheduleDTO.getGeneratedRefNo(),
										sessionBackingBean.getLoginUser(), referenceNo, routeScheduleDTO.isSwapEnds(),
										days);
							}

							boolean saved = mainSaveForTwoDay.isEmpty() ? (isDataSave && success && success1)
									: (isDataSave && success && success1 && success3);

							if (saved) {
								setSuccessMessage("Saved successfully.");
								RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
							} else {
								setErrorMessage("Data not saved.");
								RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
							}
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
				routeScheduleService.insertRouteScheduleDetOneTableData(mainSaveList,
						routeScheduleDTO.getGeneratedRefNo(), sessionBackingBean.getLoginUser(), referenceNo,
						routeScheduleDTO.isSwapEnds(), differenceInt);

				if (routeScheduleDTO.getNoOfLeavesOrigin() != 0 || routeScheduleDTO.getNoOfLeavesDestination() != 0) {
					routeScheduleService.updateRouteScheduleDetOneTableData(selectedleaveForRouteList,
							sessionBackingBean.getLoginUser(), referenceNo, routeScheduleDTO.getGeneratedRefNo(),
							tripType, differenceInt);
				}

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

			TimeTableDTO routeDTO = timeTableService
					.getRouteDataForEditPanelGenerator(routeScheduleDTO.getGeneratedRefNo());

			int totalTrips = 0;
			if (tripType.equalsIgnoreCase("O")) {

				if (coupleCount == 2) {
					totalTrips = routeDTO.getNoOfTripsOrigin();
				} else {
					totalTrips = routeDTO.getNoOfTripsOrigin();
				}
				/** commented and newly added by tharushi.e **/

				int fixedTrips = routeScheduleService.getFixedOrWithoutFixedTripsCount(
						routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), "O", "Y");

				/** added tharushi.e **/

			} else if (tripType.equalsIgnoreCase("D")) {

				if (coupleCount == 2) {

					totalTrips = routeDTO.getNoOfTripsDestination();
				} else {

					totalTrips = routeDTO.getNoOfTripsDestination();
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

	// For edit rotation
	public void editRouteScheduleData() {
		selectedDateRange = null;
		dateList = new ArrayList<String>();
		dateList = routeScheduleService.getDateRangesForRouteSchedule(routeScheduleDTO.getRouteNo(),
				routeScheduleDTO.getBusCategory(), routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(),
				tripType);
		editBusForRouteList = new ArrayList<RouteScheduleDTO>();
		editBusForRouteListLeave = new ArrayList<RouteScheduleDTO>();
		editBusForRouteListTwoDay = new ArrayList<RouteScheduleDTO>();
		editBusForRouteListTwoDayLeave = new ArrayList<RouteScheduleDTO>();
		RequestContext.getCurrentInstance().execute("PF('comfirmMSG').hide()");
		RequestContext.getCurrentInstance().execute("PF('editRouteScheduleVAR').show()");
	}

	public void searchForEdit() throws ParseException {
		editBusForRouteList = new ArrayList<>();
		editBusForRouteListLeave = new ArrayList<>();
		editBusForRouteListTwoDay = new ArrayList<>();
		editBusForRouteListTwoDayLeave = new ArrayList<>();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		tripID = 0;
		int noOfTrips = 0;
		List<String> leaveTrips = new ArrayList<>();
		String table = null;
		twoDayRotation = timeTableService.getTwoDayRotation(routeScheduleDTO.getGeneratedRefNo(), null, null);

		if (selectedDateRange != null) {
			String[] split = selectedDateRange.split("-");
			routeScheduleDTO.setStartDate(formatter.parse(split[0]));
			routeScheduleDTO.setEndDate(formatter.parse(split[1]));
			dayList = calculateNoOfDaysForGroup();
		}

		table = "m";
		noOfTrips = routeScheduleService.getNoOfTrips(routeScheduleDTO.getGeneratedRefNo(), tripType, table);
		populateEditBusForRouteList(noOfTrips, table, editBusForRouteList);

		leaveTrips = routeScheduleService.getNoOfTripsLeave(routeScheduleDTO.getGeneratedRefNo(), tripType, table);
		populateEditBusForRouteListLeave(leaveTrips, table, editBusForRouteListLeave);

		if (twoDayRotation) {
			table = "t";
			noOfTrips = routeScheduleService.getNoOfTrips(routeScheduleDTO.getGeneratedRefNo(), tripType, table);
			populateEditBusForRouteList(noOfTrips, table, editBusForRouteListTwoDay);

			leaveTrips = routeScheduleService.getNoOfTripsLeave(routeScheduleDTO.getGeneratedRefNo(), tripType, table);
			populateEditBusForRouteListLeave(leaveTrips, table, editBusForRouteListTwoDayLeave);
		}

		createEditDataTable();
		afterSearch = true;
	}

	private void populateEditBusForRouteList(int noOfTrips, String table, List<RouteScheduleDTO> list) {
		for (int i = 1; i <= noOfTrips; i++) {
			String tripId = String.valueOf(i);
			List<String> busList = routeScheduleService.getBusNoForEdit(routeScheduleDTO.getGeneratedRefNo(), tripId,
					tripType, table);
			RouteScheduleDTO dto = new RouteScheduleDTO();
			dto.setTripId(tripId);
			dto.setBusNoList(busList);
			list.add(dto);
		}
	}

	// Helper method to reduce code duplication
	private void populateEditBusForRouteListLeave(List<String> leaveTrips, String table, List<RouteScheduleDTO> list) {
		tripID = 0;
		for (String tripId : leaveTrips) {
			List<String> busList = routeScheduleService.getBusNoForEditLeave(routeScheduleDTO.getGeneratedRefNo(),
					tripId, tripType, table);
			RouteScheduleDTO dto = new RouteScheduleDTO();
			dto.setTripId(tripId);
			dto.setLeaveBusNoList(busList);
			list.add(dto);
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

	public void saveEditedData() {
		String table = "m";
		routeScheduleService.updateEditedBusNumbersInRoute_schedule_generator_det01ForEdit(
				routeScheduleDTO.getGeneratedRefNo(), tripType, sessionBackingBean.getLoginUser(), editBusForRouteList,
				dayList.size(), table);

		if (!editBusForRouteListLeave.isEmpty()) {
			routeScheduleService.updateEditedBusNumbersInRoute_schedule_generator_det01ForEditLeaves(
					routeScheduleDTO.getGeneratedRefNo(), tripType, sessionBackingBean.getLoginUser(),
					editBusForRouteListLeave, table, dayList.size());
		}

		if (!editBusForRouteListTwoDay.isEmpty()) {
			table = "t";
			routeScheduleService.updateEditedBusNumbersInRoute_schedule_generator_det01ForEdit(
					routeScheduleDTO.getGeneratedRefNo(), tripType, sessionBackingBean.getLoginUser(),
					editBusForRouteListTwoDay, dayList.size(), table);
		}

		if (!editBusForRouteListTwoDayLeave.isEmpty()) {
			table = "t";
			routeScheduleService.updateEditedBusNumbersInRoute_schedule_generator_det01ForEditLeaves(
					routeScheduleDTO.getGeneratedRefNo(), tripType, sessionBackingBean.getLoginUser(),
					editBusForRouteListTwoDayLeave, table, dayList.size());
		}

		setSuccessMessage("Updated successfully.");
		RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
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
	public void createMainDataTable() {
		columns = new ArrayList<ColumnModel>();

		for (int i = 0; i < dayList.size(); i++) {
			columns.add(new ColumnModel(String.valueOf(dayList.get(i)), i));
		}

	}

	/* create route schedule leaves data table dynamically */
	public void createLeavesDataTable() {
		columnsLeaves = new ArrayList<ColumnModel>();

		for (int i = 0; i < dayList.size(); i++) {
			columnsLeaves.add(new ColumnModel(String.valueOf(dayList.get(i)), i));
		}

	}

	/* create route schedule leaves data table dynamically */
	public void createEditDataTable() {

		editColumns = new ArrayList<ColumnModel>();

		for (int i = 0; i < dayList.size(); i++) {
			editColumns.add(new ColumnModel(String.valueOf(dayList.get(i)), i));
		}

	}

	public void createEditDataTableLeave() {

		editColumnsLeave = new ArrayList<ColumnModel>();
		editColumnsTwoDayLeave = new ArrayList<ColumnModel>();

	}

	/* DTO for Data Tables */
	static public class ColumnModel implements Serializable {

		private static final long serialVersionUID = 1L;
		private String header;
		private int property;

		public ColumnModel(String header, int property) {
			this.header = header;
			this.property = property;
		}

		public String getHeader() {
			return header;
		}

		public int getProperty() {
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

//	public int getMaximumLeaveForDay() {
//		return maximumLeaveForDay;
//	}

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

	public boolean isCoupleTwo() {
		return coupleTwo;
	}

	public void setCoupleTwo(boolean coupleTwo) {
		this.coupleTwo = coupleTwo;
	}

	public List<String> getFullBusNoList() {
		return fullBusNoList;
	}

	public void setFullBusNoList(List<String> fullBusNoList) {
		this.fullBusNoList = fullBusNoList;
	}

	public int getCoupleCountLoop() {
		return coupleCountLoop;
	}

	public void setCoupleCountLoop(int coupleCountLoop) {
		this.coupleCountLoop = coupleCountLoop;
	}

	public List<Integer> getDayList() {
		return dayList;
	}

	public void setDayList(List<Integer> dayList) {
		this.dayList = dayList;
	}

	public int getIndexPoint() {
		return indexPoint;
	}

	public void setIndexPoint(int indexPoint) {
		this.indexPoint = indexPoint;
	}

	public String getLastBus() {
		return lastBus;
	}

	public void setLastBus(String lastBus) {
		this.lastBus = lastBus;
	}

	public TimeTableService getTimeTableService() {
		return timeTableService;
	}

	public void setTimeTableService(TimeTableService timeTableService) {
		this.timeTableService = timeTableService;
	}

	public int getDateCount() {
		return dateCount;
	}

	public void setDateCount(int dateCount) {
		this.dateCount = dateCount;
	}

	public int getCoupleCount() {
		return coupleCount;
	}

	public void setCoupleCount(int coupleCount) {
		this.coupleCount = coupleCount;
	}

	public boolean isTwoDayRotation() {
		return twoDayRotation;
	}

	public void setTwoDayRotation(boolean twoDayRotation) {
		this.twoDayRotation = twoDayRotation;
	}

	public List<RouteScheduleDTO> getMainSaveForTwoDay() {
		return mainSaveForTwoDay;
	}

	public void setMainSaveForTwoDay(List<RouteScheduleDTO> mainSaveForTwoDay) {
		this.mainSaveForTwoDay = mainSaveForTwoDay;
	}

	public List<RouteScheduleDTO> getBusForRouteListForTwoDay() {
		return busForRouteListForTwoDay;
	}

	public void setBusForRouteListForTwoDay(List<RouteScheduleDTO> busForRouteListForTwoDay) {
		this.busForRouteListForTwoDay = busForRouteListForTwoDay;
	}

	public boolean isRenderTableThree() {
		return renderTableThree;
	}

	public void setRenderTableThree(boolean renderTableThree) {
		this.renderTableThree = renderTableThree;
	}

	public boolean isRenderLeave() {
		return renderLeave;
	}

	public void setRenderLeave(boolean renderLeave) {
		this.renderLeave = renderLeave;
	}

	public List<String> getLeavePositionListTwoDay() {
		return leavePositionListTwoDay;
	}

	public void setLeavePositionListTwoDay(List<String> leavePositionListTwoDay) {
		this.leavePositionListTwoDay = leavePositionListTwoDay;
	}

	public List<RouteScheduleDTO> getEditBusForRouteListLeave() {
		return editBusForRouteListLeave;
	}

	public void setEditBusForRouteListLeave(List<RouteScheduleDTO> editBusForRouteListLeave) {
		this.editBusForRouteListLeave = editBusForRouteListLeave;
	}

	public List<ColumnModel> getEditColumnsLeave() {
		return editColumnsLeave;
	}

	public void setEditColumnsLeave(List<ColumnModel> editColumnsLeave) {
		this.editColumnsLeave = editColumnsLeave;
	}

	public List<RouteScheduleDTO> getEditBusForRouteListTwoDay() {
		return editBusForRouteListTwoDay;
	}

	public void setEditBusForRouteListTwoDay(List<RouteScheduleDTO> editBusForRouteListTwoDay) {
		this.editBusForRouteListTwoDay = editBusForRouteListTwoDay;
	}

	public List<ColumnModel> getEditColumnsTwoDay() {
		return editColumnsTwoDay;
	}

	public void setEditColumnsTwoDay(List<ColumnModel> editColumnsTwoDay) {
		this.editColumnsTwoDay = editColumnsTwoDay;
	}

	public List<RouteScheduleDTO> getEditBusForRouteListTwoDayLeave() {
		return editBusForRouteListTwoDayLeave;
	}

	public void setEditBusForRouteListTwoDayLeave(List<RouteScheduleDTO> editBusForRouteListTwoDayLeave) {
		this.editBusForRouteListTwoDayLeave = editBusForRouteListTwoDayLeave;
	}

	public List<ColumnModel> getEditColumnsTwoDayLeave() {
		return editColumnsTwoDayLeave;
	}

	public void setEditColumnsTwoDayLeave(List<ColumnModel> editColumnsTwoDayLeave) {
		this.editColumnsTwoDayLeave = editColumnsTwoDayLeave;
	}

	public boolean isEditTable() {
		return editTable;
	}

	public void setEditTable(boolean editTable) {
		this.editTable = editTable;
	}

	public boolean isEditTableLeave() {
		return editTableLeave;
	}

	public void setEditTableLeave(boolean editTableLeave) {
		this.editTableLeave = editTableLeave;
	}

	public boolean isEditTableTwoDay() {
		return editTableTwoDay;
	}

	public void setEditTableTwoDay(boolean editTableTwoDay) {
		this.editTableTwoDay = editTableTwoDay;
	}

	public boolean isEditTableTwoDayLeave() {
		return editTableTwoDayLeave;
	}

	public void setEditTableTwoDayLeave(boolean editTableTwoDayLeave) {
		this.editTableTwoDayLeave = editTableTwoDayLeave;
	}

	public List<RouteScheduleDTO> getSelectLeavePositionListTwoDay() {
		return selectLeavePositionListTwoDay;
	}

	public void setSelectLeavePositionListTwoDay(List<RouteScheduleDTO> selectLeavePositionListTwoDay) {
		this.selectLeavePositionListTwoDay = selectLeavePositionListTwoDay;
	}

	public boolean isTwoDay() {
		return twoDay;
	}

	public void setTwoDay(boolean twoDay) {
		this.twoDay = twoDay;
	}

	public List<RouteScheduleDTO> getLeaveBusTemp() {
		return leaveBusTemp;
	}

	public void setLeaveBusTemp(List<RouteScheduleDTO> leaveBusTemp) {
		this.leaveBusTemp = leaveBusTemp;
	}

	public List<RouteScheduleDTO> getSelectedleaveForRouteList() {
		return selectedleaveForRouteList;
	}

	public void setSelectedleaveForRouteList(List<RouteScheduleDTO> selectedleaveForRouteList) {
		this.selectedleaveForRouteList = selectedleaveForRouteList;
	}

	public List<RouteScheduleDTO> getSelectedleaveForRouteListNextSide() {
		return selectedleaveForRouteListNextSide;
	}

	public void setSelectedleaveForRouteListNextSide(List<RouteScheduleDTO> selectedleaveForRouteListNextSide) {
		this.selectedleaveForRouteListNextSide = selectedleaveForRouteListNextSide;
	}

	public static String getFirstBusSecondDay() {
		return firstBusSecondDay;
	}

	public static void setFirstBusSecondDay(String firstBusSecondDay) {
		RouteScheduleGeneratorBackingBean.firstBusSecondDay = firstBusSecondDay;
	}

}
