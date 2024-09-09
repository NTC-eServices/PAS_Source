package lk.informatics.ntc.view.beans;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;

import lk.informatics.ntc.model.dto.TimeTableDTO;
import lk.informatics.ntc.model.dto.WithoutFixedTimeValidationDTO;
import lk.informatics.ntc.model.service.PanelGeneratorWithoutFixedTimeService;
import lk.informatics.ntc.model.service.ReportService;
import lk.informatics.ntc.model.service.RouteScheduleService;
import lk.informatics.ntc.model.service.TimeTableService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "panelGeneratorWithoutFixedTimeBean")
@ViewScoped
public class PanelGeneratorWithoutFixedTimeBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	private List<TimeTableDTO> busRouteList = new ArrayList<>(0);// took
	private List<TimeTableDTO> routeDetailsList;
	private List<TimeTableDTO> originGroupOneList;
	private List<TimeTableDTO> originGroupTwoList;
	private List<TimeTableDTO> originGroupThreeList;
	private List<TimeTableDTO> destinationGroupOneList;
	private List<TimeTableDTO> destinationGroupTwoList;
	private List<TimeTableDTO> destinationGroupThreeList;
	private List<String> timeList;
	private TimeTableService timeTableService;
	private PanelGeneratorWithoutFixedTimeService panelGeneratorWithoutFixedTimeService;
	private int activeTabIndex;
	private TimeTableDTO timeTableDTO;
	private TimeTableDTO groupOneDTO;
	private TimeTableDTO groupTwoDTO;
	private TimeTableDTO groupThreeDTO;
	private TimeTableDTO tripsDTO;
	private TimeTableDTO routeDTO;
	private String alertMSG;
	private String successMessage;
	private String errorMessage;
	private int groupCount;
	private int pvtBusCount;
	private int originOneBuses;
	private int destinationOneBuses;
	private int originTwoBuses;
	private int destinationTwoBuses;
	private int originThreeBuses;
	private int destinationThreeBuses;
	private int restTime;
	private boolean disabledGroupTwo;
	private boolean disabledGroupThree;

	private String groupOneDays;
	private String groupTwoDays;
	private String groupThreeDays;
	private boolean coupling;
	private String couplesPerBus;
	private boolean couplesPerBusDisable;
	private List<TimeTableDTO> busCategoryList;
	private String selectedBusCategory;
	private boolean editColRender;
	private RouteScheduleService routeScheduleService;
	private ReportService reportService;
	private boolean leaveSaveDisable;

	private LinkedList<TimeTableDTO> midnightShiftBusesOrigin;
	private LinkedList<TimeTableDTO> midnightShiftBusesDestination;
	private LinkedList<TimeTableDTO> midnightShiftBusesOriginG2;
	private LinkedList<TimeTableDTO> midnightShiftBusesDestinationG2;
	private LinkedList<TimeTableDTO> midnightShiftBusesOriginG3;
	private LinkedList<TimeTableDTO> midnightShiftBusesDestinationG3;

	private String nightShiftBusType;

	// 24.02.2020
	private List<TimeTableDTO> busNoListG1 = new ArrayList<>(0);
	private List<TimeTableDTO> busNoListDestinationG1 = new ArrayList<>(0);
	private List<TimeTableDTO> busNoListG2 = new ArrayList<>(0);
	private List<TimeTableDTO> busNoListDestinationG2 = new ArrayList<>(0);
	private List<TimeTableDTO> busNoListG3 = new ArrayList<>(0);
	private List<TimeTableDTO> busNoListDestinationG3 = new ArrayList<>(0);

	private List<TimeTableDTO> busList1 = new ArrayList<>();
	private List<TimeTableDTO> busList2 = new ArrayList<>();
	private List<TimeTableDTO> busList3 = new ArrayList<>();

	int k = 0;
	int j = 0;
	boolean isEdit = false;
	private List<String> orginBusAbbriviationMain = new ArrayList<String>();
	private List<String> destinationBusAbbriviationMain = new ArrayList<String>();
	private List<String> orginBusAbbriviationMainWithLeave = new ArrayList<String>();
	private List<String> destinationBusAbbriviationMainWithLeave = new ArrayList<String>();
	private List<String> removedBusesFromOrgin = new ArrayList<String>();
	private List<String> removedBusesFromDestination = new ArrayList<String>();
	boolean leaveFromDestination = false;
	boolean leaveFromOrigin = false;

	@PostConstruct
	public void init() {
		timeTableService = (TimeTableService) SpringApplicationContex.getBean("timeTableService");
		panelGeneratorWithoutFixedTimeService = (PanelGeneratorWithoutFixedTimeService) SpringApplicationContex
				.getBean("panelGeneratorWithoutFixedTimeService");
		routeScheduleService = (RouteScheduleService) SpringApplicationContex.getBean("routeScheduleService");
		reportService = (ReportService) SpringApplicationContex.getBean("reportService");
		loadValue();
	}

	public void loadValue() {
		routeDetailsList = new ArrayList<TimeTableDTO>();
		timeTableDTO = new TimeTableDTO();
		groupOneDTO = new TimeTableDTO();
		groupTwoDTO = new TimeTableDTO();
		groupThreeDTO = new TimeTableDTO();
		tripsDTO = new TimeTableDTO();
		routeDTO = new TimeTableDTO();
		groupOneDays = null;
		groupTwoDays = null;
		groupThreeDays = null;
		busRouteList = timeTableService.getRouteNoList();// took
		coupling = false;
		couplesPerBus = null;
		couplesPerBusDisable = false;
		busCategoryList = new ArrayList<TimeTableDTO>();
		selectedBusCategory = null;
		editColRender = false;
		leaveSaveDisable = false;

		TimeTableDTO midnightShiftO1 = new TimeTableDTO();
		midnightShiftO1.setTripId("1");
		TimeTableDTO midnightShiftD1 = new TimeTableDTO();
		midnightShiftD1.setTripId("1");
		TimeTableDTO midnightShiftO2 = new TimeTableDTO();
		midnightShiftO2.setTripId("1");
		TimeTableDTO midnightShiftD2 = new TimeTableDTO();
		midnightShiftD2.setTripId("1");
		TimeTableDTO midnightShiftO3 = new TimeTableDTO();
		midnightShiftO3.setTripId("1");
		TimeTableDTO midnightShiftD3 = new TimeTableDTO();
		midnightShiftD3.setTripId("1");
		midnightShiftBusesOrigin = new LinkedList<TimeTableDTO>();
		midnightShiftBusesOrigin.add(midnightShiftO1);
		midnightShiftBusesDestination = new LinkedList<TimeTableDTO>();
		midnightShiftBusesDestination.add(midnightShiftD1);
		midnightShiftBusesOriginG2 = new LinkedList<TimeTableDTO>();
		midnightShiftBusesOriginG2.add(midnightShiftO2);
		midnightShiftBusesDestinationG2 = new LinkedList<TimeTableDTO>();
		midnightShiftBusesDestinationG2.add(midnightShiftD2);
		midnightShiftBusesOriginG3 = new LinkedList<TimeTableDTO>();
		midnightShiftBusesOriginG3.add(midnightShiftO3);
		midnightShiftBusesDestinationG3 = new LinkedList<TimeTableDTO>();
		midnightShiftBusesDestinationG3.add(midnightShiftD3);

		nightShiftBusType = null;
		k = 0;
		j = 0;
	}

	public void ajaxFillBusCategory() {

		busCategoryList = timeTableService.getBusCategoryList(timeTableDTO.getRouteNo());

	}

	public void ajaxSelectBusCategory() {
		
		routeDTO = timeTableService.getRouteData(timeTableDTO.getRouteNo(), timeTableDTO.getBusCategory());

		if (routeDTO.getOrigin() != null && !routeDTO.getOrigin().trim().equalsIgnoreCase("")) {
			timeTableDTO.setOrigin(routeDTO.getOrigin());
		}

		if (routeDTO.getDestination() != null && !routeDTO.getDestination().trim().equalsIgnoreCase("")) {
			timeTableDTO.setDestination(routeDTO.getDestination());
		}

		if (routeDTO.getGenereatedRefNo() != null && !routeDTO.getGenereatedRefNo().trim().equalsIgnoreCase("")) {
			timeTableDTO.setGenereatedRefNo(routeDTO.getGenereatedRefNo());
		}
	}

	public void searchAction() {

		if (timeTableDTO.getRouteNo() != null && !timeTableDTO.getRouteNo().trim().equalsIgnoreCase("")) {

			if (timeTableDTO.getBusCategory() != null && !timeTableDTO.getBusCategory().trim().equalsIgnoreCase("")) {

				groupCount = timeTableService.getGroupCount(timeTableDTO.getRouteNo(),
						timeTableDTO.getGenereatedRefNo());

				String groupC = null;
				groupC = String.valueOf(groupCount);

				isEdit = panelGeneratorWithoutFixedTimeService
						.checkLeavesOnPanelRefNo(timeTableDTO.getGenereatedRefNo(), groupC);

				String restTimeString = timeTableService.getRestTime(timeTableDTO.getRouteNo());

				String tempCouplesPerBus = panelGeneratorWithoutFixedTimeService
						.retrieveCouplesForRoute(timeTableDTO.getGenereatedRefNo(), String.valueOf(groupCount));
				if (tempCouplesPerBus != null && !tempCouplesPerBus.isEmpty()
						&& !tempCouplesPerBus.trim().equalsIgnoreCase("")) {
					// means -> already existing record
					couplesPerBus = tempCouplesPerBus;
					couplesPerBusDisable = true;
				} else {
					couplesPerBusDisable = false;
				}

				if (couplesPerBus != null && !couplesPerBus.isEmpty() && !couplesPerBus.trim().equals("")
						&& Integer.valueOf(couplesPerBus) == 2) {
					coupling = true;
				} else {
					coupling = false;
				}

				boolean notAssigned = checkBusesAssigned(groupCount);
				if (!notAssigned) {
					sessionBackingBean.setMessage("Searched data is not eligible. Buses are already assigned");
					RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
					return;
				}

				if (!couplesPerBusDisable) {
					groupManager(groupCount, restTimeString);

					/** set no of leaves by tharushi.e **/

					if (groupCount == 1) {
						groupOneDTO.setBusesOnLeaveOriginOne(panelGeneratorWithoutFixedTimeService
								.getNumOfLeaves(timeTableDTO.getGenereatedRefNo(), "1", "O"));
						groupOneDTO.setBusesOnLeaveDestinationOne(panelGeneratorWithoutFixedTimeService
								.getNumOfLeaves(timeTableDTO.getGenereatedRefNo(), "1", "D"));

					}
					if (groupCount == 2) {
						groupOneDTO.setBusesOnLeaveOriginOne(panelGeneratorWithoutFixedTimeService
								.getNumOfLeaves(timeTableDTO.getGenereatedRefNo(), "1", "O"));
						groupOneDTO.setBusesOnLeaveDestinationTwo(panelGeneratorWithoutFixedTimeService
								.getNumOfLeaves(timeTableDTO.getGenereatedRefNo(), "2", "D"));
					}

					if (groupCount == 3) {
						groupOneDTO.setBusesOnLeaveOriginOne(panelGeneratorWithoutFixedTimeService
								.getNumOfLeaves(timeTableDTO.getGenereatedRefNo(), "3", "O"));

						groupOneDTO.setBusesOnLeaveDestinationOne(panelGeneratorWithoutFixedTimeService
								.getNumOfLeaves(timeTableDTO.getGenereatedRefNo(), "3", "D"));
					}
					/** end **/
				} else {
					if (groupCount == 1) {
						disabledGroupTwo = true;
						disabledGroupThree = true;
						originGroupOneList = panelGeneratorWithoutFixedTimeService
								.selectDataForGroups(timeTableDTO.getGenereatedRefNo(), "1", "O");
						groupOneDTO.setBusesOnLeaveOriginOne(panelGeneratorWithoutFixedTimeService
								.retrieveNumOfLeaves(timeTableDTO.getGenereatedRefNo(), "1", "O"));
						destinationGroupOneList = panelGeneratorWithoutFixedTimeService
								.selectDataForGroups(timeTableDTO.getGenereatedRefNo(), "1", "D");
						groupOneDTO.setBusesOnLeaveDestinationOne(panelGeneratorWithoutFixedTimeService
								.retrieveNumOfLeaves(timeTableDTO.getGenereatedRefNo(), "1", "D"));

					} else if (groupCount == 2) {
						disabledGroupThree = true;
						originGroupOneList = panelGeneratorWithoutFixedTimeService
								.selectDataForGroups(timeTableDTO.getGenereatedRefNo(), "1", "O");
						groupOneDTO.setBusesOnLeaveOriginOne(panelGeneratorWithoutFixedTimeService
								.retrieveNumOfLeaves(timeTableDTO.getGenereatedRefNo(), "1", "O"));
						destinationGroupOneList = panelGeneratorWithoutFixedTimeService
								.selectDataForGroups(timeTableDTO.getGenereatedRefNo(), "1", "D");
						groupOneDTO.setBusesOnLeaveDestinationOne(panelGeneratorWithoutFixedTimeService
								.retrieveNumOfLeaves(timeTableDTO.getGenereatedRefNo(), "1", "D"));
						originGroupTwoList = panelGeneratorWithoutFixedTimeService
								.selectDataForGroups(timeTableDTO.getGenereatedRefNo(), "2", "O");
						groupOneDTO.setBusesOnLeaveOriginTwo(panelGeneratorWithoutFixedTimeService
								.retrieveNumOfLeaves(timeTableDTO.getGenereatedRefNo(), "2", "O"));
						destinationGroupTwoList = panelGeneratorWithoutFixedTimeService
								.selectDataForGroups(timeTableDTO.getGenereatedRefNo(), "2", "D");
						groupOneDTO.setBusesOnLeaveDestinationTwo(panelGeneratorWithoutFixedTimeService
								.retrieveNumOfLeaves(timeTableDTO.getGenereatedRefNo(), "2", "D"));

					} else if (groupCount == 3) {
						originGroupOneList = panelGeneratorWithoutFixedTimeService
								.selectDataForGroups(timeTableDTO.getGenereatedRefNo(), "1", "O");
						groupOneDTO.setBusesOnLeaveOriginOne(panelGeneratorWithoutFixedTimeService
								.retrieveNumOfLeaves(timeTableDTO.getGenereatedRefNo(), "1", "O"));
						destinationGroupOneList = panelGeneratorWithoutFixedTimeService
								.selectDataForGroups(timeTableDTO.getGenereatedRefNo(), "1", "D");
						groupOneDTO.setBusesOnLeaveDestinationOne(panelGeneratorWithoutFixedTimeService
								.retrieveNumOfLeaves(timeTableDTO.getGenereatedRefNo(), "1", "D"));
						originGroupTwoList = panelGeneratorWithoutFixedTimeService
								.selectDataForGroups(timeTableDTO.getGenereatedRefNo(), "2", "O");
						groupOneDTO.setBusesOnLeaveOriginTwo(panelGeneratorWithoutFixedTimeService
								.retrieveNumOfLeaves(timeTableDTO.getGenereatedRefNo(), "2", "O"));
						destinationGroupTwoList = panelGeneratorWithoutFixedTimeService
								.selectDataForGroups(timeTableDTO.getGenereatedRefNo(), "2", "D");
						groupOneDTO.setBusesOnLeaveDestinationTwo(panelGeneratorWithoutFixedTimeService
								.retrieveNumOfLeaves(timeTableDTO.getGenereatedRefNo(), "2", "D"));
						originGroupThreeList = panelGeneratorWithoutFixedTimeService
								.selectDataForGroups(timeTableDTO.getGenereatedRefNo(), "3", "O");
						groupOneDTO.setBusesOnLeaveOriginThree(panelGeneratorWithoutFixedTimeService
								.retrieveNumOfLeaves(timeTableDTO.getGenereatedRefNo(), "3", "O"));
						destinationGroupThreeList = panelGeneratorWithoutFixedTimeService
								.selectDataForGroups(timeTableDTO.getGenereatedRefNo(), "3", "D");
						groupOneDTO.setBusesOnLeaveDestinationThree(panelGeneratorWithoutFixedTimeService
								.retrieveNumOfLeaves(timeTableDTO.getGenereatedRefNo(), "3", "D"));

					}

				}

				// Date - 24th Feb 2020
				// Summary - Allow edit for no of busses and time slots
				// inconsistency.

				if (editColRender) {
					if (originGroupOneList != null && !originGroupOneList.isEmpty()) {
						for (TimeTableDTO timeTableDTO : originGroupOneList) {
							if (timeTableDTO.getBusNo() == null || timeTableDTO.getBusNo().isEmpty()) {
								editColRender = false;
								break;
							}
						}
					}
				}

				if (editColRender) {
					if (originGroupTwoList != null && !originGroupTwoList.isEmpty()) {
						for (TimeTableDTO timeTableDTO : originGroupTwoList) {
							if (timeTableDTO.getBusNo() == null || timeTableDTO.getBusNo().isEmpty()) {
								editColRender = false;
								break;
							}
						}
					}
				}

				if (editColRender) {
					if (originGroupThreeList != null && !originGroupThreeList.isEmpty()) {
						for (TimeTableDTO timeTableDTO : originGroupThreeList) {
							if (timeTableDTO.getBusNo() == null || timeTableDTO.getBusNo().isEmpty()) {
								editColRender = false;
								break;
							}
						}
					}
				}

				if (!editColRender) {

					TimeTableDTO abbriDTO = timeTableService.getAbriviatiosForRoute(timeTableDTO.getRouteNo(),
							timeTableDTO.getBusCategory());

					if (abbriDTO != null) {

						if (groupCount > 0) {

							List<TimeTableDTO> showTimeSlotDet1 = new ArrayList<>();

							showTimeSlotDet1 = timeTableService
									.getTimeSlotDetForGroups(timeTableDTO.getGenereatedRefNo(), "1", "O");

							List<TimeTableDTO> showTimeSlotDetDest1 = new ArrayList<>();

							showTimeSlotDetDest1 = timeTableService
									.getTimeSlotDetForGroups(timeTableDTO.getGenereatedRefNo(), "1", "D");

							TimeTableDTO detDTO1 = new TimeTableDTO();
							TimeTableDTO detDTO2 = new TimeTableDTO();

							TimeTableDTO timeTableObj = new TimeTableDTO();
							timeTableObj.setAssigneBusNo("SLTB-O");
							busNoListG1.add(timeTableObj);

							TimeTableDTO timeTableObj2 = new TimeTableDTO();
							timeTableObj2.setAssigneBusNo("ETC-O");
							busNoListG1.add(timeTableObj2);

							TimeTableDTO timeTableObj3 = new TimeTableDTO();
							timeTableObj3.setAssigneBusNo("SLTB-D");
							busNoListDestinationG1.add(timeTableObj3);

							TimeTableDTO timeTableObj4 = new TimeTableDTO();
							timeTableObj4.setAssigneBusNo("ETC-D");
							busNoListDestinationG1.add(timeTableObj4);

							// int j = 0;

							if (showTimeSlotDet1 != null && showTimeSlotDet1.size() > 0) {
								for (TimeTableDTO TTDto : showTimeSlotDet1) {

									int noOfBus = TTDto.getNoOfBuses();

									for (int i = 0; i < noOfBus; i++) {

										j++;

										detDTO1 = new TimeTableDTO();
										String busName = abbriDTO.getAbbriAtOrigin() + String.valueOf(j);
										detDTO1.setAssigneBusNo(busName);
										busNoListG1.add(detDTO1);

									}

								}
								busList1.addAll(busNoListG1);

							}

							// int k = 0;

							if (showTimeSlotDetDest1 != null && showTimeSlotDetDest1.size() > 0) {
								for (TimeTableDTO TTDto : showTimeSlotDetDest1) {

									int noOfBus = TTDto.getNoOfBuses();

									for (int i = 0; i < noOfBus; i++) {

										k++;

										detDTO2 = new TimeTableDTO();
										String busName2 = abbriDTO.getAbbriAtDestination() + String.valueOf(k);
										detDTO2.setAssigneBusNo(busName2);
										busNoListDestinationG1.add(detDTO2);

									}

								}

								busList1.addAll(busNoListDestinationG1);
							}

						}

						if (groupCount > 1) {

							List<TimeTableDTO> showTimeSlotDet1 = new ArrayList<>();

							showTimeSlotDet1 = timeTableService
									.getTimeSlotDetForGroups(timeTableDTO.getGenereatedRefNo(), "2", "O");

							List<TimeTableDTO> showTimeSlotDetDest1 = new ArrayList<>();

							showTimeSlotDetDest1 = timeTableService
									.getTimeSlotDetForGroups(timeTableDTO.getGenereatedRefNo(), "2", "D");

							TimeTableDTO detDTO1 = new TimeTableDTO();
							TimeTableDTO detDTO2 = new TimeTableDTO();

							TimeTableDTO timeTableObj = new TimeTableDTO();
							timeTableObj.setAssigneBusNo("SLTB-O");
							busNoListG2.add(timeTableObj);

							TimeTableDTO timeTableObj2 = new TimeTableDTO();
							timeTableObj2.setAssigneBusNo("ETC-O");
							busNoListG2.add(timeTableObj2);

							TimeTableDTO timeTableObj3 = new TimeTableDTO();
							timeTableObj3.setAssigneBusNo("SLTB-D");
							busNoListDestinationG2.add(timeTableObj3);

							TimeTableDTO timeTableObj4 = new TimeTableDTO();
							timeTableObj4.setAssigneBusNo("ETC-D");
							busNoListDestinationG2.add(timeTableObj4);

							// int j = 0;

							if (showTimeSlotDet1 != null && showTimeSlotDet1.size() > 0) {
								for (TimeTableDTO TTDto : showTimeSlotDet1) {

									int noOfBus = TTDto.getNoOfBuses();

									for (int i = 0; i < noOfBus; i++) {

										j++;

										detDTO1 = new TimeTableDTO();
										String busName = abbriDTO.getAbbriAtOrigin() + String.valueOf(j);
										detDTO1.setAssigneBusNo(busName);
										busNoListG2.add(detDTO1);

									}

								}

								busList2.addAll(busNoListG2);
							}

							// int k = 0;

							if (showTimeSlotDetDest1 != null && showTimeSlotDetDest1.size() > 0) {
								for (TimeTableDTO TTDto : showTimeSlotDetDest1) {

									int noOfBus = TTDto.getNoOfBuses();

									for (int i = 0; i < noOfBus; i++) {

										k++;

										detDTO2 = new TimeTableDTO();
										String busName2 = abbriDTO.getAbbriAtDestination() + String.valueOf(k);
										detDTO2.setAssigneBusNo(busName2);
										busNoListDestinationG2.add(detDTO2);

									}

								}
								busList2.addAll(busNoListDestinationG2);
							}

						}

						if (groupCount > 2) {

							List<TimeTableDTO> showTimeSlotDet1 = new ArrayList<>();

							showTimeSlotDet1 = timeTableService
									.getTimeSlotDetForGroups(timeTableDTO.getGenereatedRefNo(), "3", "O");

							List<TimeTableDTO> showTimeSlotDetDest1 = new ArrayList<>();

							showTimeSlotDetDest1 = timeTableService
									.getTimeSlotDetForGroups(timeTableDTO.getGenereatedRefNo(), "3", "D");

							TimeTableDTO detDTO1 = new TimeTableDTO();
							TimeTableDTO detDTO2 = new TimeTableDTO();

							TimeTableDTO timeTableObj = new TimeTableDTO();
							timeTableObj.setAssigneBusNo("SLTB-O");
							busNoListG3.add(timeTableObj);

							TimeTableDTO timeTableObj2 = new TimeTableDTO();
							timeTableObj2.setAssigneBusNo("ETC-O");
							busNoListG3.add(timeTableObj2);

							TimeTableDTO timeTableObj3 = new TimeTableDTO();
							timeTableObj3.setAssigneBusNo("SLTB-D");
							busNoListDestinationG3.add(timeTableObj3);

							TimeTableDTO timeTableObj4 = new TimeTableDTO();
							timeTableObj4.setAssigneBusNo("ETC-D");
							busNoListDestinationG3.add(timeTableObj4);

							// int j = 0;

							if (showTimeSlotDet1 != null && showTimeSlotDet1.size() > 0) {
								for (TimeTableDTO TTDto : showTimeSlotDet1) {

									int noOfBus = TTDto.getNoOfBuses();

									for (int i = 0; i < noOfBus; i++) {

										j++;

										detDTO1 = new TimeTableDTO();
										String busName = abbriDTO.getAbbriAtOrigin() + String.valueOf(j);
										detDTO1.setAssigneBusNo(busName);
										busNoListG3.add(detDTO1);

									}

								}

								busList3.addAll(busNoListG3);

							}

							// int k = 0;

							if (showTimeSlotDetDest1 != null && showTimeSlotDetDest1.size() > 0) {
								for (TimeTableDTO TTDto : showTimeSlotDetDest1) {

									int noOfBus = TTDto.getNoOfBuses();

									for (int i = 0; i < noOfBus; i++) {

										k++;

										detDTO2 = new TimeTableDTO();
										String busName2 = abbriDTO.getAbbriAtDestination() + String.valueOf(k);
										detDTO2.setAssigneBusNo(busName2);
										busNoListDestinationG3.add(detDTO2);

									}

								}
								busList3.addAll(busNoListDestinationG3);
							}

						}

					}

				}

			} else {
				sessionBackingBean.setMessage("Please Select Bus Category");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
			}
		} else {
			sessionBackingBean.setMessage("Please Select Route No.");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
		}
	}

	public void groupManager(int groupCount, String restTimeString) {

		boolean isFixedBusHaveO = timeTableService.checkFixedBuses(timeTableDTO.getGenereatedRefNo(), "O");
		boolean isFixedBusHaveD = timeTableService.checkFixedBuses(timeTableDTO.getGenereatedRefNo(), "D");
		routeDetailsList = timeTableService.getNewRouteDetails(timeTableDTO.getRouteNo(),
				timeTableDTO.getGenereatedRefNo());

		String driverRestTime = panelGeneratorWithoutFixedTimeService.retrieveDriverRestTime(timeTableDTO.getRouteNo());
		String busRideTime = panelGeneratorWithoutFixedTimeService.getBusRideTime(timeTableDTO.getRouteNo(),
				timeTableDTO.getBusCategory());

		if (groupCount == 1) {

			disabledGroupTwo = true;
			disabledGroupThree = true;

			// get abbreviation for bus numbers
			TimeTableDTO abbreviationDTO = timeTableService.getAbriviatiosForRoute(timeTableDTO.getRouteNo(),
					timeTableDTO.getBusCategory());
			String abbreviationOrigin = abbreviationDTO.getAbbriAtOrigin();
			String abbreviationDestination = abbreviationDTO.getAbbriAtDestination();

			/// ********************************GROUP ONE
			/// START*****************************************/
			/** Group one origin start **/
			groupOneOriginList(abbreviationOrigin);
			/** Group one origin end **/

			/** Group one destination start **/
			groupOneDestination(abbreviationDestination);
			/** Group one destination end **/

			/** validate start **/
			boolean valid = validateBusAndRecreateGroupOneLists(abbreviationOrigin, abbreviationDestination);
			/** validate end **/

			if (valid) {
				/**
				 * create destination buses start end time from origin start
				 **/
				groupOneDestinationBusesToOrigin(driverRestTime, busRideTime);
				/**
				 * create destination buses start end time from origin start
				 **/

				/**
				 * create origin buses start end time from destination start
				 **/
				groupOneOriginBusesToDestination(driverRestTime, busRideTime);
				/** create origin buses start end time from destination end **/

				/**
				 * check remaining empty time slots and fill bus numbers start
				 **/
				if (coupling) {
					if (isFixedBusHaveO && isFixedBusHaveD && !leaveFromDestination && !leaveFromOrigin) {
						checkRemainingTimeSlotsForDestinationDataForCoupling(driverRestTime, busRideTime);
						checkRemainingTimeSlotsForOriginDataForCoupling(driverRestTime, busRideTime);
					} else if (isFixedBusHaveO && isFixedBusHaveD && leaveFromDestination && leaveFromOrigin) {
						checkRemainingTimeSlotsForDestinationDataForCouplingWithLeave(driverRestTime, busRideTime);
						checkRemainingTimeSlotsForOriginDataForCouplingWithLeave(driverRestTime, busRideTime);
					} else {

						checkRemainingTimeSlotsForDestinationData(driverRestTime, busRideTime);
						checkRemainingTimeSlotsForOriginData(driverRestTime, busRideTime);
					}
				}
				/// ********************************GROUP ONE
				/// END*****************************************/

				/** validate Time Slot start **/
				String errorOrigin = validateOriginListTimeSlots(originGroupOneList);
				String errorDestination = validateDestinationListTimeSlots(destinationGroupOneList);

				if (errorOrigin != null && !errorOrigin.isEmpty() && !errorOrigin.trim().equals("")) {
					editColRender = true;
					sessionBackingBean.setMessage(errorOrigin + " Group 1");
					RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
					return;
				} else if (errorDestination != null && !errorDestination.isEmpty()
						&& !errorDestination.trim().equals("")) {
					editColRender = true;
					sessionBackingBean.setMessage(errorDestination + " Group 1");
					RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
					return;
				}
				/** validate Time Slot end **/

				/**
				 * validate origin and destination buses are in origin list and destination list
				 * start
				 **/
				boolean validBusesInBoth = validateBusesAreInBothOriginAndDestination(originGroupOneList,
						destinationGroupOneList);
				if (!validBusesInBoth) {
					sessionBackingBean.setMessage(
							"Cannot perform the time table process. Please check bus numbers and time slots - Group 1");
					RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
					return;
				}
				/**
				 * validate origin and destination buses are in origin list and destination list
				 * end
				 **/

				editColRender = false;
				leaveSaveDisable = false;
				saveAction();
			}
		} else if (groupCount == 2) {

			disabledGroupThree = true;

			// get abbreviation for bus numbers
			TimeTableDTO abbreviationDTO = timeTableService.getAbriviatiosForRoute(timeTableDTO.getRouteNo(),
					timeTableDTO.getBusCategory());
			String abbreviationOrigin = abbreviationDTO.getAbbriAtOrigin();
			String abbreviationDestination = abbreviationDTO.getAbbriAtDestination();

			/// ********************************GROUP ONE
			/// START*****************************************/

			/** Group one origin start **/
			groupOneOriginList(abbreviationOrigin);
			/** Group one origin end **/

			/** Group one destination start **/
			groupOneDestination(abbreviationDestination);
			/** Group one destination end **/

			/** validate start **/
			boolean validG1 = validateBusAndRecreateGroupOneLists(abbreviationOrigin, abbreviationDestination);
			/** validate end **/

			if (validG1) {
				/**
				 * create destination buses start end time from origin start
				 **/
				groupOneDestinationBusesToOrigin(driverRestTime, busRideTime);
				/**
				 * create destination buses start end time from origin start
				 **/

				/**
				 * create origin buses start end time from destination start
				 **/
				groupOneOriginBusesToDestination(driverRestTime, busRideTime);
				/** create origin buses start end time from destination end **/

				/**
				 * check remaining empty time slots and fill bus numbers start
				 **/
				if (coupling) {
					if (isFixedBusHaveO && isFixedBusHaveD) {

						checkRemainingTimeSlotsForDestinationDataForCoupling(driverRestTime, busRideTime);
						checkRemainingTimeSlotsForOriginDataForCoupling(driverRestTime, busRideTime);

					} else {

						checkRemainingTimeSlotsForDestinationData(driverRestTime, busRideTime);
						checkRemainingTimeSlotsForOriginData(driverRestTime, busRideTime);

					}
				}
				/**
				 * check remaining empty time slots and fill bus numbers end
				 **/
			}

			/** validate Time Slot start **/
			String errorOrigin = validateOriginListTimeSlots(originGroupOneList);
			String errorDestination = validateDestinationListTimeSlots(destinationGroupOneList);

			if (errorOrigin != null && !errorOrigin.isEmpty() && !errorOrigin.trim().equals("")) {
				editColRender = true;
				sessionBackingBean.setMessage(errorOrigin + " Group 1");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
				return;
			} else if (errorDestination != null && !errorDestination.isEmpty() && !errorDestination.trim().equals("")) {
				editColRender = true;
				sessionBackingBean.setMessage(errorDestination + " Group 1");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
				return;
			}
			/** validate Time Slot end **/

			/**
			 * validate origin and destination buses are in origin list and destination list
			 * start
			 **/
			boolean validBusesInBoth = validateBusesAreInBothOriginAndDestination(originGroupOneList,
					destinationGroupOneList);
			if (!validBusesInBoth) {
				sessionBackingBean.setMessage(
						"Cannot perform the time table process. Please check bus numbers and time slots - Group 1");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
				return;
			}
			/**
			 * validate origin and destination buses are in origin list and destination list
			 * end
			 **/
			/// ********************************GROUP ONE
			/// END*****************************************/

			/// ********************************GROUP TWO
			/// START*****************************************/
			/** Group one origin start **/
			groupTwoOriginList(abbreviationOrigin);
			/** Group one origin end **/

			/** Group one destination start **/
			groupTwoDestination(abbreviationDestination);
			/** Group one destination end **/

			/** validate start **/
			boolean validG2 = validateBusAndRecreateGroupTwoLists(abbreviationOrigin, abbreviationDestination);
			/** validate end **/

			if (validG2) {
				/**
				 * create destination buses start end time from origin start
				 **/
				groupTwoDestinationBusesToOrigin(driverRestTime, busRideTime);
				/**
				 * create destination buses start end time from origin start
				 **/

				/**
				 * create origin buses start end time from destination start
				 **/
				groupTwoOriginBusesToDestination(driverRestTime, busRideTime);
				/** create origin buses start end time from destination end **/

				/**
				 * check remaining empty time slots and fill bus numbers start
				 **/
				if (coupling) {
					if (isFixedBusHaveO && isFixedBusHaveD) {
						checkRemainingTimeSlotsForDestinationDataForCoupling(driverRestTime, busRideTime);
						checkRemainingTimeSlotsForOriginDataForCoupling(driverRestTime, busRideTime);
					} else {
						checkRemainingTimeSlotsForDestinationDataGroupTwo(driverRestTime, busRideTime);
						checkRemainingTimeSlotsForOriginDataGroupTwo(driverRestTime, busRideTime);
					}
				}

				/**
				 * check remaining empty time slots and fill bus numbers end
				 **/

				/** validate Time Slot start **/
				String errorOriginG2 = validateOriginListTimeSlots(originGroupTwoList);
				String errorDestinationG2 = validateDestinationListTimeSlots(destinationGroupTwoList);

				if (errorOriginG2 != null && !errorOriginG2.isEmpty() && !errorOriginG2.trim().equals("")) {
					editColRender = true;
					sessionBackingBean.setMessage(errorOriginG2 + " Group 2");
					RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
					return;
				} else if (errorDestinationG2 != null && !errorDestinationG2.isEmpty()
						&& !errorDestinationG2.trim().equals("")) {
					editColRender = true;
					sessionBackingBean.setMessage(errorDestinationG2 + " Group 2");
					RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
					return;
				}
				/** validate Time Slot end **/

				/**
				 * validate origin and destination buses are in origin list and destination list
				 * start
				 **/
				boolean validBusesInBoth2 = validateBusesAreInBothOriginAndDestination(originGroupTwoList,
						destinationGroupTwoList);
				if (!validBusesInBoth2) {
					sessionBackingBean.setMessage(
							"Cannot perform the time table process. Please check bus numbers and time slots - Group 2");
					RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
					return;
				}
				/**
				 * validate origin and destination buses are in origin list and destination list
				 * end
				 **/
				/// ********************************GROUP TWO
				/// END*******************************************/
				editColRender = false;
				saveAction();
			}

		} else if (groupCount == 3) {

			// get abbreviation for bus numbers
			TimeTableDTO abbreviationDTO = timeTableService.getAbriviatiosForRoute(timeTableDTO.getRouteNo(),
					timeTableDTO.getBusCategory());
			String abbreviationOrigin = abbreviationDTO.getAbbriAtOrigin();
			String abbreviationDestination = abbreviationDTO.getAbbriAtDestination();

			/// ********************************GROUP ONE
			/// START*****************************************/
			/** Group one origin start **/
			groupOneOriginList(abbreviationOrigin);
			/** Group one origin end **/

			/** Group one destination start **/
			groupOneDestination(abbreviationDestination);
			/** Group one destination end **/

			/** validate start **/
			boolean validG1 = validateBusAndRecreateGroupOneLists(abbreviationOrigin, abbreviationDestination);
			/** validate end **/

			if (validG1) {
				/**
				 * create destination buses start end time from origin start
				 **/
				groupOneDestinationBusesToOrigin(driverRestTime, busRideTime);
				/**
				 * create destination buses start end time from origin start
				 **/

				/**
				 * create origin buses start end time from destination start
				 **/
				groupOneOriginBusesToDestination(driverRestTime, busRideTime);
				/** create origin buses start end time from destination end **/

				/**
				 * check remaining empty time slots and fill bus numbers start
				 **/
				if (coupling) {
					if (isFixedBusHaveO && isFixedBusHaveD) {
						checkRemainingTimeSlotsForDestinationDataForCoupling(driverRestTime, busRideTime);
						checkRemainingTimeSlotsForOriginDataForCoupling(driverRestTime, busRideTime);
					} else {
						checkRemainingTimeSlotsForDestinationData(driverRestTime, busRideTime);
						checkRemainingTimeSlotsForOriginData(driverRestTime, busRideTime);
					}
				}
			}

			/** validate Time Slot start **/
			String errorOriginG1 = validateOriginListTimeSlots(originGroupOneList);
			String errorDestinationG1 = validateDestinationListTimeSlots(destinationGroupOneList);

			if (errorOriginG1 != null && !errorOriginG1.isEmpty() && !errorOriginG1.trim().equals("")) {
				editColRender = true;
				sessionBackingBean.setMessage(errorOriginG1 + " Group 1");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
				return;
			} else if (errorDestinationG1 != null && !errorDestinationG1.isEmpty()
					&& !errorDestinationG1.trim().equals("")) {
				editColRender = true;
				sessionBackingBean.setMessage(errorDestinationG1 + " Group 1");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
				return;
			}
			/** validate Time Slot end **/

			/**
			 * validate origin and destination buses are in origin list and destination list
			 * start
			 **/
			boolean validBusesInBoth1 = validateBusesAreInBothOriginAndDestination(originGroupOneList,
					destinationGroupOneList);
			if (!validBusesInBoth1) {
				sessionBackingBean.setMessage(
						"Cannot perform the time table process. Please check bus numbers and time slots - Group 1");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
				return;
			}
			/**
			 * validate origin and destination buses are in origin list and destination list
			 * end
			 **/
			/// ********************************GROUP ONE
			/// END*****************************************/

			/// ********************************GROUP TWO
			/// START*****************************************/
			/** Group one origin start **/
			groupTwoOriginList(abbreviationOrigin);
			/** Group one origin end **/

			/** Group one destination start **/
			groupTwoDestination(abbreviationDestination);
			/** Group one destination end **/

			/** validate start **/
			boolean validG2 = validateBusAndRecreateGroupTwoLists(abbreviationOrigin, abbreviationDestination);
			/** validate end **/

			if (validG2) {
				/**
				 * create destination buses start end time from origin start
				 **/
				groupTwoDestinationBusesToOrigin(driverRestTime, busRideTime);
				/**
				 * create destination buses start end time from origin start
				 **/

				/**
				 * create origin buses start end time from destination start
				 **/
				groupTwoOriginBusesToDestination(driverRestTime, busRideTime);
				/** create origin buses start end time from destination end **/

				/**
				 * check remaining empty time slots and fill bus numbers start
				 **/
				if (coupling) {
					if (isFixedBusHaveO && isFixedBusHaveD) {
						checkRemainingTimeSlotsForDestinationDataForCoupling(driverRestTime, busRideTime);
						checkRemainingTimeSlotsForOriginDataForCoupling(driverRestTime, busRideTime);
					} else {
						checkRemainingTimeSlotsForDestinationDataGroupTwo(driverRestTime, busRideTime);
						checkRemainingTimeSlotsForOriginDataGroupTwo(driverRestTime, busRideTime);
					}
				}

				/**
				 * check remaining empty time slots and fill bus numbers end
				 **/
			}

			/** validate Time Slot start **/
			String errorOriginG2 = validateOriginListTimeSlots(originGroupTwoList);
			String errorDestinationG2 = validateDestinationListTimeSlots(destinationGroupTwoList);

			if (errorOriginG2 != null && !errorOriginG2.isEmpty() && !errorOriginG2.trim().equals("")) {
				editColRender = true;
				sessionBackingBean.setMessage(errorOriginG2 + " Group 2");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
				return;
			} else if (errorDestinationG2 != null && !errorDestinationG2.isEmpty()
					&& !errorDestinationG2.trim().equals("")) {
				editColRender = true;
				sessionBackingBean.setMessage(errorDestinationG2 + " Group 2");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
				return;
			}
			/** validate Time Slot end **/

			/**
			 * validate origin and destination buses are in origin list and destination list
			 * start
			 **/
			boolean validBusesInBoth2 = validateBusesAreInBothOriginAndDestination(originGroupTwoList,
					destinationGroupTwoList);
			if (!validBusesInBoth2) {
				sessionBackingBean.setMessage(
						"Cannot perform the time table process. Please check bus numbers and time slots - Group 2");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
				return;
			}
			/**
			 * validate origin and destination buses are in origin list and destination list
			 * end
			 **/
			/// ********************************GROUP TWO
			/// END*******************************************/

			/// ********************************GROUP THREE
			/// START*****************************************/
			/** Group one origin start **/
			groupThreeOriginList(abbreviationOrigin);
			/** Group one origin end **/

			/** Group one destination start **/
			groupThreeDestination(abbreviationDestination);
			/** Group one destination end **/

			/** validate start **/
			boolean validG3 = validateBusAndRecreateGroupThreeLists(abbreviationOrigin, abbreviationDestination);
			/** validate end **/

			if (validG3) {
				/**
				 * create destination buses start end time from origin start
				 **/
				groupThreeDestinationBusesToOrigin(driverRestTime, busRideTime);
				/**
				 * create destination buses start end time from origin start
				 **/

				/**
				 * create origin buses start end time from destination start
				 **/
				groupThreeOriginBusesToDestination(driverRestTime, busRideTime);
				/** create origin buses start end time from destination end **/

				/**
				 * check remaining empty time slots and fill bus numbers start
				 **/
				if (coupling) {
					if (isFixedBusHaveO && isFixedBusHaveD) {
						checkRemainingTimeSlotsForDestinationDataForCoupling(driverRestTime, busRideTime);
						checkRemainingTimeSlotsForOriginDataForCoupling(driverRestTime, busRideTime);
					} else {
						checkRemainingTimeSlotsForDestinationDataGroupThree(driverRestTime, busRideTime);
						checkRemainingTimeSlotsForOriginDataGroupThree(driverRestTime, busRideTime);
					}
				}
				/**
				 * check remaining empty time slots and fill bus numbers end
				 **/

				/** validate Time Slot start **/
				String errorOriginG3 = validateOriginListTimeSlots(originGroupThreeList);
				String errorDestinationG3 = validateDestinationListTimeSlots(destinationGroupThreeList);

				if (errorOriginG3 != null && !errorOriginG3.isEmpty() && !errorOriginG3.trim().equals("")) {
					editColRender = true;
					sessionBackingBean.setMessage(errorOriginG3 + " Group 3");
					RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
					return;
				} else if (errorDestinationG3 != null && !errorDestinationG3.isEmpty()
						&& !errorDestinationG3.trim().equals("")) {
					editColRender = true;
					sessionBackingBean.setMessage(errorDestinationG3 + " Group 3");
					RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
					return;
				}
				/** validate Time Slot end **/

				/**
				 * validate origin and destination buses are in origin list and destination list
				 * start
				 **/
				boolean validBusesInBoth3 = validateBusesAreInBothOriginAndDestination(originGroupThreeList,
						destinationGroupThreeList);
				if (!validBusesInBoth3) {
					sessionBackingBean.setMessage(
							"Cannot perform the time table process. Please check bus numbers and time slots - Group 3");
					RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
					return;
				}
				/**
				 * validate origin and destination buses are in origin list and destination list
				 * end
				 **/
				/// ********************************GROUP THREE
				/// END*****************************************/

				editColRender = false;
				saveAction();
			}

			saveAction();
		} else {
			sessionBackingBean.setMessage("Group Count Range of Selected Route is Empty");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
		}

		return;

	}

	private boolean validateBusesAreInBothOriginAndDestination(List<TimeTableDTO> originGroupOneList,
			List<TimeTableDTO> destinationGroupOneList) {

		boolean found = false;

		for (TimeTableDTO originDTO : originGroupOneList) {
			found = false;
			for (TimeTableDTO destinationDTO : destinationGroupOneList) {
				if (originDTO.getBusNo().equals(destinationDTO.getBusNo())) {
					found = true;
				}
			}

			if (!found) {
				return found;
			}
		}

		return found;
	}

	private String validateDestinationListTimeSlots(List<TimeTableDTO> destinationGroupOneList) {

		String error = null;

		for (TimeTableDTO dto : destinationGroupOneList) {
			if (dto.getBusNo() == null || dto.getBusNo().isEmpty() || dto.getBusNo().trim().equalsIgnoreCase("")) {
				error = "There are empty slots without bus numbers. Please check bus numbers and time slots - Destination";
				return error;
			}
		}

		return error;
	}

	private String validateOriginListTimeSlots(List<TimeTableDTO> originGroupOneList2) {

		String error = null;

		for (TimeTableDTO dto : originGroupOneList2) {
			if (dto.getBusNo() == null || dto.getBusNo().isEmpty() || dto.getBusNo().trim().equalsIgnoreCase("")) {
				error = "There are empty slots without bus numbers. Please check bus numbers and time slots - Origin";
				return error;
			}
		}

		return error;
	}

	private boolean validateBusAndRecreateGroupThreeLists(String abbreviationOrigin, String abbreviationDestination) {
		WithoutFixedTimeValidationDTO validationDto = new WithoutFixedTimeValidationDTO();
		validationDto = validateBusCountGroupThree();

		if (validationDto != null && !validationDto.isSuccess()) {

			if (validationDto.getError() != null && !validationDto.getError().isEmpty()
					&& !validationDto.getError().trim().equals("")) {

				/**
				 * need to check bus num according to ref number in time table det if bus num
				 * not null can not goto this by tharushi.e
				 */

				boolean checkLeave = panelGeneratorWithoutFixedTimeService
						.checkLeavesOnPanelRefNo(timeTableDTO.getGenereatedRefNo(), "1");

				if (!checkLeave) {
					panelGeneratorWithoutFixedTimeService.updateLeaves(timeTableDTO.getGenereatedRefNo(), "1", 0, 0,
							sessionBackingBean.getLoginUser());
					panelGeneratorWithoutFixedTimeService.updateLeaves(timeTableDTO.getGenereatedRefNo(), "2", 0, 0,
							sessionBackingBean.getLoginUser());
					panelGeneratorWithoutFixedTimeService.updateLeaves(timeTableDTO.getGenereatedRefNo(), "3", 0, 0,
							sessionBackingBean.getLoginUser());

					sessionBackingBean.setMessage(validationDto.getError());
					RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
					return false;
				}
			}
			if (validationDto.getNoOfLeavesOrigin() > 0) {
				// get non fixed destination buses and remove this amount of
				// buses from destination list
				List<TimeTableDTO> tempdtoList = new ArrayList<TimeTableDTO>();
				List<TimeTableDTO> pvtList = new ArrayList<TimeTableDTO>();
				for (TimeTableDTO dto : originGroupThreeList) {
					tempdtoList.add(dto);
					if (dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& (dto.getBusNo().substring(0, abbreviationOrigin.length())).equals(abbreviationOrigin)
							&& !dto.isFixedTime()) {
						pvtList.add(dto);
					}
				}

				int keepPVTBuses = pvtList.size() - validationDto.getNoOfLeavesOrigin();
				List<TimeTableDTO> pvtListRemove = new ArrayList<TimeTableDTO>();
				int count = 0;
				for (TimeTableDTO dto : pvtList) {
					if (count >= keepPVTBuses) {
						pvtListRemove.add(pvtList.get(count));
					}
					count = count + 1;
				}

				for (TimeTableDTO dto : originGroupThreeList) {
					for (TimeTableDTO dto2 : pvtListRemove) {
						if (dto.getBusNo() != null && !dto.getBusNo().isEmpty() && dto2.getBusNo() != null
								&& !dto2.getBusNo().isEmpty() && dto.getBusNo().equalsIgnoreCase(dto2.getBusNo())) {
							TimeTableDTO tempDTO = new TimeTableDTO();
							tempDTO = dto;
							tempDTO.setBusNo(null);
							tempDTO.setFixedTime(false);
							tempdtoList.remove(dto);
							tempdtoList.add(tempDTO);
						}
					}
				}

				originGroupThreeList = new ArrayList<TimeTableDTO>();
				originGroupThreeList = tempdtoList;

			}
			if (validationDto.getNoOfLeavesDestination() > 0) {
				// get non fixed origin buses and remove this amount of buses
				// from destination list
				List<TimeTableDTO> tempdtoList = new ArrayList<TimeTableDTO>();
				List<TimeTableDTO> pvtList = new ArrayList<TimeTableDTO>();
				for (TimeTableDTO dto : destinationGroupThreeList) {
					tempdtoList.add(dto);
					if (dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& (dto.getBusNo().substring(0, abbreviationDestination.length()))
									.equals(abbreviationDestination)
							&& !dto.isFixedTime()) {
						pvtList.add(dto);
					}
				}

				int keepPVTBuses = pvtList.size() - validationDto.getNoOfLeavesDestination();
				List<TimeTableDTO> pvtListRemove = new ArrayList<TimeTableDTO>();
				int count = 0;
				for (TimeTableDTO dto : pvtList) {
					if (count >= keepPVTBuses) {
						pvtListRemove.add(pvtList.get(count));
					}
					count = count + 1;
				}

				for (TimeTableDTO dto : destinationGroupThreeList) {
					for (TimeTableDTO dto2 : pvtListRemove) {
						if (dto.getBusNo() != null && !dto.getBusNo().isEmpty() && dto2.getBusNo() != null
								&& !dto2.getBusNo().isEmpty() && dto.getBusNo().equalsIgnoreCase(dto2.getBusNo())) {
							TimeTableDTO tempDTO = new TimeTableDTO();
							tempDTO = dto;
							tempDTO.setBusNo(null);
							tempDTO.setFixedTime(false);
							tempdtoList.remove(dto);
							tempdtoList.add(tempDTO);
						}
					}
				}

				destinationGroupThreeList = new ArrayList<TimeTableDTO>();
				destinationGroupThreeList = tempdtoList;

			}
		}

		return true;
	}

	private WithoutFixedTimeValidationDTO validateBusCountGroupThree() {

		WithoutFixedTimeValidationDTO validationDto = new WithoutFixedTimeValidationDTO();

		// check whether time slots are equal
		if (originGroupThreeList.size() != destinationGroupThreeList.size()) {
			validationDto = new WithoutFixedTimeValidationDTO();
			validationDto.setError("Time slots are not equal - Group Three");
			return validationDto;
		}

		// origin
		int originPVTBus = panelGeneratorWithoutFixedTimeService
				.retrievePrivateBusNumbers(timeTableDTO.getGenereatedRefNo(), "3", "O");// get
																						// private
																						// bus
																						// count
		int originfixedPVTbuses = panelGeneratorWithoutFixedTimeService
				.retrieveFixedBusNumbers(timeTableDTO.getGenereatedRefNo(), "3", "O", "Y");// get
																							// fixed
																							// private
																							// bus
																							// count
		int originfixedCTBbuses = panelGeneratorWithoutFixedTimeService
				.retrieveFixedBusNumbers(timeTableDTO.getGenereatedRefNo(), "3", "O", "N");// get
																							// fixed
																							// CTB
																							// bus
																							// count

		int remaininPVTBusOrigin = originPVTBus - originfixedPVTbuses;
		int allBusesOrigin = originPVTBus + originfixedCTBbuses;
		// origin

		// destination
		int destinaionPVTBus = panelGeneratorWithoutFixedTimeService
				.retrievePrivateBusNumbers(timeTableDTO.getGenereatedRefNo(), "3", "D");// get
																						// private
																						// bus
																						// count
		int destinationfixedPVTbuses = panelGeneratorWithoutFixedTimeService
				.retrieveFixedBusNumbers(timeTableDTO.getGenereatedRefNo(), "3", "D", "Y");// get
																							// fixed
																							// private
																							// bus
																							// count
		int destinationfixedCTBbuses = panelGeneratorWithoutFixedTimeService
				.retrieveFixedBusNumbers(timeTableDTO.getGenereatedRefNo(), "3", "D", "N");// get
																							// fixed
																							// CTB
																							// bus
																							// count

		int remaininPVTBusDestination = destinaionPVTBus - destinationfixedPVTbuses;
		int allBusesDestinaion = destinaionPVTBus + destinationfixedCTBbuses;
		// destination

		int allBuses = allBusesOrigin + allBusesDestinaion;
		int allFixedBuses = originfixedPVTbuses + originfixedCTBbuses + destinationfixedPVTbuses
				+ destinationfixedCTBbuses;

		// if coupling
		if (coupling) {

			int couplingTimeSlots = originGroupThreeList.size() / 2;

			if (allBuses != couplingTimeSlots) {
				int noOfLeaves = (((allBusesOrigin * 2) + (allBusesDestinaion * 2)) - originGroupThreeList.size()) / 2;
				if (noOfLeaves < 0) {
					// cannot give minus leaves
					validationDto = new WithoutFixedTimeValidationDTO();
					validationDto.setError("cannot perform the operation please check time slots and bus numbers");
					return validationDto;
				} else {
					// check how to give leaves
					validationDto = new WithoutFixedTimeValidationDTO();

					double originLeaves = (((double) allBusesOrigin
							- ((double) originfixedPVTbuses + (double) originfixedCTBbuses))
							/ ((double) allBuses - (double) allFixedBuses)) * (double) noOfLeaves;
					double destinationLeaves = (((double) allBusesDestinaion
							- ((double) destinationfixedPVTbuses + (double) destinationfixedCTBbuses))
							/ ((double) allBuses - (double) allFixedBuses)) * (double) noOfLeaves;

					BigDecimal bdOrigin = new BigDecimal(originLeaves).setScale(1, RoundingMode.HALF_UP);
					double newInputOrigin = bdOrigin.doubleValue();
					int intInputOrigin = (int) Math.round(newInputOrigin);
					BigDecimal bdDestination = new BigDecimal(destinationLeaves).setScale(1, RoundingMode.HALF_UP);
					double newInputDestinaion = bdDestination.doubleValue();
					int intInputDestinaion = (int) Math.round(newInputDestinaion);

					int tempLeaves = (int) Math.round(newInputOrigin) + (int) Math.round(newInputDestinaion);
					if (tempLeaves < noOfLeaves) {
						int remainLeaves = noOfLeaves - tempLeaves;
						if (intInputOrigin < intInputDestinaion) {

							// check whether origin can give this amount of
							// leaves
							if (remaininPVTBusOrigin >= intInputOrigin) {
								// can give leaves
								intInputOrigin = intInputOrigin + remainLeaves;
							}

						} else if (intInputDestinaion < intInputOrigin) {

							// check whether destination can give this amount of
							// leaves
							if (remaininPVTBusDestination >= intInputDestinaion) {
								// can give leaves
								intInputDestinaion = intInputDestinaion + remainLeaves;
							}
						} else if ((intInputOrigin == 0 && remainLeaves > 0)
								|| (intInputDestinaion == 0 && remainLeaves > 0)) {
							if (newInputOrigin > newInputDestinaion) {
								// check whether origin can give this amount of
								// leaves
								if (remaininPVTBusOrigin >= intInputOrigin) {
									intInputOrigin = intInputOrigin + remainLeaves;
								} else if (remaininPVTBusDestination >= intInputDestinaion) {
									// if origin non fixed PVT buses are not
									// enough to give leaves get leaves from
									// destination
									intInputDestinaion = intInputDestinaion + remainLeaves;
								}
							} else if (newInputDestinaion > newInputOrigin) {
								// check whether destination can give this
								// amount of leaves
								if (remaininPVTBusDestination >= intInputDestinaion) {
									intInputDestinaion = intInputDestinaion + remainLeaves;
								} else if (remaininPVTBusOrigin >= intInputOrigin) {
									// if destination non fixed PVT buses are
									// not enough to give leaves get leaves from
									// origin
									intInputOrigin = intInputOrigin + remainLeaves;
								}
							}
						}
					} else if (tempLeaves > noOfLeaves) {
						int extraLeaves = tempLeaves - noOfLeaves;

						if (extraLeaves == 1) {
							if (intInputOrigin > intInputDestinaion && intInputOrigin >= 1) {
								intInputOrigin = intInputOrigin - 1;
							} else if (intInputDestinaion > intInputOrigin && intInputDestinaion >= 1) {
								intInputDestinaion = intInputDestinaion - 1;
							}
						} else {
							System.out.println("do something if leaves are greater than 1");
							// check extraLeaves are odd or even and remove
							// buses from both origin and destination
						}

					}
					if ((intInputOrigin + intInputDestinaion) == noOfLeaves) {

						// check the leaves can be given
						if (intInputOrigin <= remaininPVTBusOrigin && intInputDestinaion <= remaininPVTBusDestination) {
							// valid
						} else {
							if (intInputOrigin > remaininPVTBusOrigin) {

								int extraLeaves = intInputOrigin - remaininPVTBusOrigin;

								// check whether these leaves can be given from
								// destination start
								int remainDesLeaves = remaininPVTBusDestination - intInputDestinaion;
								if (remainDesLeaves >= extraLeaves) {
									intInputOrigin = intInputOrigin + extraLeaves;
									intInputDestinaion = intInputDestinaion - extraLeaves;
								} else {
									System.out.println("cannnot perform coupling for selected route");
									validationDto = new WithoutFixedTimeValidationDTO();
									validationDto.setError("cannnot perform coupling for selected route - Group Three");
									return validationDto;
								}
								// check whether these leaves can be given from
								// destination end

							} else if (intInputDestinaion > remaininPVTBusDestination) {
								int extraLeaves = intInputDestinaion - remaininPVTBusDestination;

								// check whether these leaves can be given from
								// origin start
								int remainOriLeaves = remaininPVTBusOrigin - intInputOrigin;
								if (remainOriLeaves >= extraLeaves) {
									intInputOrigin = intInputOrigin + extraLeaves;
									intInputDestinaion = intInputDestinaion - extraLeaves;
								} else {
									System.out.println("cannnot perform coupling for selected route");
									validationDto = new WithoutFixedTimeValidationDTO();
									validationDto.setError("cannnot perform coupling for selected route");
									return validationDto;
								}
								// check whether these leaves can be given from
								// origin end
							}
						}
						// check the leaves can be given

						if (intInputOrigin > 9) {
							validationDto.setNoOfLeavesOrigin(9);
						} else {
							validationDto.setNoOfLeavesOrigin(intInputOrigin);
						} // added by tharushi.e

						if (intInputDestinaion > 9) {
							validationDto.setNoOfLeavesDestination(9);
						} else {
							validationDto.setNoOfLeavesDestination(intInputDestinaion);
						} // added by tharushi.e
						if (intInputOrigin > 9) {
							groupOneDTO.setBusesOnLeaveOriginThree(9);
						} else {
							groupOneDTO.setBusesOnLeaveOriginThree(intInputOrigin);
						} // added by tharushi.e

						if (intInputDestinaion > 9) {
							groupOneDTO.setBusesOnLeaveDestinationThree(9);
						} else {
							groupOneDTO.setBusesOnLeaveDestinationThree(intInputDestinaion);
						} // added by tharushi.e

						panelGeneratorWithoutFixedTimeService.updateLeaves(timeTableDTO.getGenereatedRefNo(), "3", intInputOrigin, intInputDestinaion,
								sessionBackingBean.getLoginUser());
					}

					return validationDto;
				}
			} else {
				// can perform coupling
				int leftBuses = originGroupThreeList.size() - (couplingTimeSlots * 2);
				/*
				 * i.e: originGroupOneList.size()=21 to get num of leaves, calculate ->
				 * originGroupOneList.size())/2 = 10 and 1 bus is left when we couple id we
				 * didn't add that remaining bus/es one time slot will be left to add that time
				 * slot/s have to add the left bus/es int leftBuses = 21 - (10*2) = 1
				 */
				validationDto = new WithoutFixedTimeValidationDTO();

				if (leftBuses > 0) {
					if (remaininPVTBusOrigin >= leftBuses) {
						// add a bus from origin remaining buses
						validationDto.setNoOfLeavesOriginSec(leftBuses);
					} else if (remaininPVTBusDestination >= leftBuses) {
						// add a bus from destination remaining buses
						validationDto.setNoOfLeavesDestinationSec(leftBuses);
					} else {
						// one or more time slot/s will be left -> can't happen
						validationDto = new WithoutFixedTimeValidationDTO();
						validationDto.setError("cannot perform the operation please check time slots and bus numbers");
						return validationDto;
					}
				} else {
					validationDto.setSuccess(true);
				}

				return validationDto;
			}
			// if coupling

		} else {

			// if not coupling
			// check all time slots can be filled start
			if (allBuses == originGroupThreeList.size()) {
				System.out.println("time slots and buses are okay, can perform the operation successfully");
				validationDto = new WithoutFixedTimeValidationDTO();
				validationDto.setSuccess(true);
				return validationDto;
			}
			// check all time slots can be filled end

			// give leave start
			DecimalFormat df2 = new DecimalFormat("#.##");
			if (allBuses > originGroupThreeList.size()) {
				// int numberOfLeaves = allBuses - originGroupOneList.size();
				int numberOfLeaves = (((allBusesOrigin * 1) + (allBusesDestinaion * 1)) - originGroupThreeList.size())
						/ 1;
				validationDto = new WithoutFixedTimeValidationDTO();

				double originLeaves = (((double) allBusesOrigin
						- ((double) originfixedPVTbuses + (double) originfixedCTBbuses))
						/ ((double) allBuses - (double) allFixedBuses)) * (double) numberOfLeaves;
				double destinationLeaves = (((double) allBusesDestinaion
						- ((double) destinationfixedPVTbuses + (double) destinationfixedCTBbuses))
						/ ((double) allBuses - (double) allFixedBuses)) * (double) numberOfLeaves;
				;

				BigDecimal bdOrigin = new BigDecimal(originLeaves).setScale(1, RoundingMode.HALF_UP);
				double newInputOrigin = bdOrigin.doubleValue();
				int intInputOrigin = (int) Math.round(newInputOrigin);
				BigDecimal bdDestination = new BigDecimal(destinationLeaves).setScale(1, RoundingMode.HALF_UP);
				double newInputDestinaion = bdDestination.doubleValue();
				int intInputDestinaion = (int) newInputDestinaion;

				int tempLeaves = (int) Math.round(newInputOrigin) + (int) Math.round(newInputDestinaion);
				if (tempLeaves < numberOfLeaves) {
					int remainLeaves = numberOfLeaves - tempLeaves;
					if (intInputOrigin < intInputDestinaion) {
						// intInputOrigin = intInputOrigin+remainLeaves;

						// check whether origin can give this amount of leaves
						if (remaininPVTBusOrigin >= intInputOrigin) {
							// can give leaves
							intInputOrigin = intInputOrigin + remainLeaves;
						}

					} else if (intInputDestinaion < intInputOrigin) {
						// intInputDestinaion = intInputDestinaion+remainLeaves;

						// check whether origin can give this amount of leaves
						if (remaininPVTBusDestination >= intInputDestinaion) {
							// can give leaves
							intInputDestinaion = intInputDestinaion + remainLeaves;
						}
					}
				} else if (tempLeaves > numberOfLeaves) {
					int moreLeaves = tempLeaves - numberOfLeaves;
					if (originLeaves > destinationLeaves) {
						int oriLeave = intInputOrigin - moreLeaves;
						intInputOrigin = oriLeave;
					} else {
						int desLeave = intInputDestinaion - moreLeaves;
						intInputDestinaion = desLeave;
					}
				}

				if ((intInputOrigin + intInputDestinaion) == numberOfLeaves) {
					// validationDto.setNoOfLeavesOrigin(intInputOrigin);
					// validationDto.setNoOfLeavesDestination(intInputDestinaion);
					//
					// groupOneDTO.setBusesOnLeaveOriginThree(intInputOrigin);
					// groupOneDTO.setBusesOnLeaveDestinationThree(intInputDestinaion);

					if (intInputOrigin > 9) {
						validationDto.setNoOfLeavesOrigin(9);
					} else {
						validationDto.setNoOfLeavesOrigin(intInputOrigin);
					} // added by tharushi.e

					if (intInputDestinaion > 9) {
						validationDto.setNoOfLeavesDestination(9);
					} else {
						validationDto.setNoOfLeavesDestination(intInputDestinaion);
					} // added by tharushi.e
					if (intInputOrigin > 9) {
						groupOneDTO.setBusesOnLeaveOriginThree(9);
					} else {
						groupOneDTO.setBusesOnLeaveOriginThree(intInputOrigin);
					} // added by tharushi.e

					if (intInputDestinaion > 9) {
						groupOneDTO.setBusesOnLeaveDestinationThree(9);
					} else {
						groupOneDTO.setBusesOnLeaveDestinationThree(intInputDestinaion);
					} // added by tharushi.e

					panelGeneratorWithoutFixedTimeService.updateLeaves(timeTableDTO.getGenereatedRefNo(), "3", intInputOrigin, intInputDestinaion,
							sessionBackingBean.getLoginUser());
				} else {
					System.out.println(
							"origin leave buses and destination leave buses are not equal to number of leaves - Group one");
					validationDto = new WithoutFixedTimeValidationDTO();
					validationDto.setError("cannot perform the operation due to leave calculation error - Group One");
					return validationDto;
				}

				return validationDto;
			}
			// give leave end

			int remainingTimeSlots = originGroupThreeList.size() - allBuses;

			if (remainingTimeSlots > 0) {
				// return "Please perform coupling for the route";
				validationDto = new WithoutFixedTimeValidationDTO();
				validationDto.setError("Please perform coupling for the route - Group Three");
				return validationDto;
			}
			// if not coupling
		}

		return null;
	}

	private boolean validateBusAndRecreateGroupTwoLists(String abbreviationOrigin, String abbreviationDestination) {
		WithoutFixedTimeValidationDTO validationDto = new WithoutFixedTimeValidationDTO();
		validationDto = validateBusCountGroupTwo();

		if (validationDto != null && !validationDto.isSuccess()) {
			if (validationDto.getError() != null && !validationDto.getError().isEmpty()
					&& !validationDto.getError().trim().equals("")) {

				/**
				 * need to check bus num according to ref number in time table det if bus num
				 * not null can not goto this by tharushi.e
				 */

				boolean checkLeave = panelGeneratorWithoutFixedTimeService
						.checkLeavesOnPanelRefNo(timeTableDTO.getGenereatedRefNo(), "2");

				if (!checkLeave) {
					panelGeneratorWithoutFixedTimeService.updateLeaves(timeTableDTO.getGenereatedRefNo(), "1", 0, 0,
							sessionBackingBean.getLoginUser());
					panelGeneratorWithoutFixedTimeService.updateLeaves(timeTableDTO.getGenereatedRefNo(), "2", 0, 0,
							sessionBackingBean.getLoginUser());
					panelGeneratorWithoutFixedTimeService.updateLeaves(timeTableDTO.getGenereatedRefNo(), "3", 0, 0,
							sessionBackingBean.getLoginUser());
					
					sessionBackingBean.setMessage(validationDto.getError());
					RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
					return false;
				}
			}
			if (validationDto.getNoOfLeavesOrigin() > 0) {
				// get non fixed destination buses and remove this amount of
				// buses from destination list
				List<TimeTableDTO> tempdtoList = new ArrayList<TimeTableDTO>();
				List<TimeTableDTO> pvtList = new ArrayList<TimeTableDTO>();
				for (TimeTableDTO dto : originGroupTwoList) {
					tempdtoList.add(dto);
					if (dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& (dto.getBusNo().substring(0, abbreviationOrigin.length())).equals(abbreviationOrigin)
							&& !dto.isFixedTime()) {
						pvtList.add(dto);
					}
				}

				int keepPVTBuses = pvtList.size() - validationDto.getNoOfLeavesOrigin();
				List<TimeTableDTO> pvtListRemove = new ArrayList<TimeTableDTO>();
				int count = 0;
				for (TimeTableDTO dto : pvtList) {
					if (count >= keepPVTBuses) {
						pvtListRemove.add(pvtList.get(count));
					}
					count = count + 1;
				}

				for (TimeTableDTO dto : originGroupTwoList) {
					for (TimeTableDTO dto2 : pvtListRemove) {
						if (dto.getBusNo() != null && !dto.getBusNo().isEmpty() && dto2.getBusNo() != null
								&& !dto2.getBusNo().isEmpty() && dto.getBusNo().equalsIgnoreCase(dto2.getBusNo())) {
							TimeTableDTO tempDTO = new TimeTableDTO();
							tempDTO = dto;
							tempDTO.setBusNo(null);
							tempDTO.setFixedTime(false);
							tempdtoList.remove(dto);
							tempdtoList.add(tempDTO);
						}
					}
				}

				originGroupTwoList = new ArrayList<TimeTableDTO>();
				originGroupTwoList = tempdtoList;

			}
			if (validationDto.getNoOfLeavesDestination() > 0) {
				// get non fixed origin buses and remove this amount of buses
				// from destination list
				List<TimeTableDTO> tempdtoList = new ArrayList<TimeTableDTO>();
				List<TimeTableDTO> pvtList = new ArrayList<TimeTableDTO>();
				for (TimeTableDTO dto : destinationGroupTwoList) {
					tempdtoList.add(dto);
					if (dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& (dto.getBusNo().substring(0, abbreviationDestination.length()))
									.equals(abbreviationDestination)
							&& !dto.isFixedTime()) {
						pvtList.add(dto);
					}
				}

				int keepPVTBuses = pvtList.size() - validationDto.getNoOfLeavesDestination();
				List<TimeTableDTO> pvtListRemove = new ArrayList<TimeTableDTO>();
				int count = 0;
				for (TimeTableDTO dto : pvtList) {
					if (count >= keepPVTBuses) {
						pvtListRemove.add(pvtList.get(count));
					}
					count = count + 1;
				}

				for (TimeTableDTO dto : destinationGroupTwoList) {
					for (TimeTableDTO dto2 : pvtListRemove) {
						if (dto.getBusNo() != null && !dto.getBusNo().isEmpty() && dto2.getBusNo() != null
								&& !dto2.getBusNo().isEmpty() && dto.getBusNo().equalsIgnoreCase(dto2.getBusNo())) {
							TimeTableDTO tempDTO = new TimeTableDTO();
							tempDTO = dto;
							tempDTO.setBusNo(null);
							tempDTO.setFixedTime(false);
							tempdtoList.remove(dto);
							tempdtoList.add(tempDTO);
						}
					}
				}

				destinationGroupTwoList = new ArrayList<TimeTableDTO>();
				destinationGroupTwoList = tempdtoList;

				// update leave bus count in DB
			}
		}
		return true;
	}

	private WithoutFixedTimeValidationDTO validateBusCountGroupTwo() {

		WithoutFixedTimeValidationDTO validationDto = new WithoutFixedTimeValidationDTO();

		// check whether time slots are equal
		if (originGroupTwoList.size() != destinationGroupTwoList.size()) {
			validationDto = new WithoutFixedTimeValidationDTO();
			validationDto.setError("Time slots are not equal - Group Two");
			return validationDto;
		}

		// origin
		int originPVTBus = panelGeneratorWithoutFixedTimeService
				.retrievePrivateBusNumbers(timeTableDTO.getGenereatedRefNo(), "2", "O");// get
																						// private
																						// bus
																						// count
		int originfixedPVTbuses = panelGeneratorWithoutFixedTimeService
				.retrieveFixedBusNumbers(timeTableDTO.getGenereatedRefNo(), "2", "O", "Y");// get
																							// fixed
																							// private
																							// bus
																							// count
		int originfixedCTBbuses = panelGeneratorWithoutFixedTimeService
				.retrieveFixedBusNumbers(timeTableDTO.getGenereatedRefNo(), "2", "O", "N");// get
																							// fixed
																							// CTB
																							// bus
																							// count

		int remaininPVTBusOrigin = originPVTBus - originfixedPVTbuses;
		int allBusesOrigin = originPVTBus + originfixedCTBbuses;
		// origin

		// destination
		int destinaionPVTBus = panelGeneratorWithoutFixedTimeService
				.retrievePrivateBusNumbers(timeTableDTO.getGenereatedRefNo(), "2", "D");// get
																						// private
																						// bus
																						// count
		int destinationfixedPVTbuses = panelGeneratorWithoutFixedTimeService
				.retrieveFixedBusNumbers(timeTableDTO.getGenereatedRefNo(), "2", "D", "Y");// get
																							// fixed
																							// private
																							// bus
																							// count
		int destinationfixedCTBbuses = panelGeneratorWithoutFixedTimeService
				.retrieveFixedBusNumbers(timeTableDTO.getGenereatedRefNo(), "2", "D", "N");// get
																							// fixed
																							// CTB
																							// bus
																							// count

		int remaininPVTBusDestination = destinaionPVTBus - destinationfixedPVTbuses;
		int allBusesDestinaion = destinaionPVTBus + destinationfixedCTBbuses;
		// destination

		int allBuses = allBusesOrigin + allBusesDestinaion;
		int allFixedBuses = originfixedPVTbuses + originfixedCTBbuses + destinationfixedPVTbuses
				+ destinationfixedCTBbuses;

		// if coupling
		if (coupling) {

			int couplingTimeSlots = originGroupTwoList.size() / 2;

			if (allBuses != couplingTimeSlots) {
				int noOfLeaves = (((allBusesOrigin * 2) + (allBusesDestinaion * 2)) - originGroupTwoList.size()) / 2;
				if (noOfLeaves < 0) {
					// cannot give minus leaves
					validationDto = new WithoutFixedTimeValidationDTO();
					validationDto.setError(
							"cannot perform the operation please check time slots and bus numbers - Group Two");
					return validationDto;
				} else {
					// check how to give leaves
					validationDto = new WithoutFixedTimeValidationDTO();

					double originLeaves = (((double) allBusesOrigin
							- ((double) originfixedPVTbuses + (double) originfixedCTBbuses))
							/ ((double) allBuses - (double) allFixedBuses)) * (double) noOfLeaves;
					double destinationLeaves = (((double) allBusesDestinaion
							- ((double) destinationfixedPVTbuses + (double) destinationfixedCTBbuses))
							/ ((double) allBuses - (double) allFixedBuses)) * (double) noOfLeaves;
					;

					BigDecimal bdOrigin = new BigDecimal(originLeaves).setScale(1, RoundingMode.HALF_UP);
					double newInputOrigin = bdOrigin.doubleValue();
					int intInputOrigin = (int) Math.round(newInputOrigin);
					BigDecimal bdDestination = new BigDecimal(destinationLeaves).setScale(1, RoundingMode.HALF_UP);
					double newInputDestinaion = bdDestination.doubleValue();
					int intInputDestinaion = (int) Math.round(newInputDestinaion);

					int tempLeaves = (int) Math.round(newInputOrigin) + (int) Math.round(newInputDestinaion);
					if (tempLeaves < noOfLeaves) {
						int remainLeaves = noOfLeaves - tempLeaves;
						if (intInputOrigin < intInputDestinaion) {

							// check whether origin can give this amount of
							// leaves
							if (remaininPVTBusOrigin >= intInputOrigin) {
								// can give leaves
								intInputOrigin = intInputOrigin + remainLeaves;
							}

						} else if (intInputDestinaion < intInputOrigin) {

							// check whether destination can give this amount of
							// leaves
							if (remaininPVTBusDestination >= intInputDestinaion) {
								// can give leaves
								intInputDestinaion = intInputDestinaion + remainLeaves;
							}
						} else if ((intInputOrigin == 0 && remainLeaves > 0)
								|| (intInputDestinaion == 0 && remainLeaves > 0)) {
							if (newInputOrigin > newInputDestinaion) {
								// check whether origin can give this amount of
								// leaves
								if (remaininPVTBusOrigin >= intInputOrigin) {
									intInputOrigin = intInputOrigin + remainLeaves;
								} else if (remaininPVTBusDestination >= intInputDestinaion) {
									// if origin non fixed PVT buses are not
									// enough to give leaves get leaves from
									// destination
									intInputDestinaion = intInputDestinaion + remainLeaves;
								}
							} else if (newInputDestinaion > newInputOrigin) {
								// check whether destination can give this
								// amount of leaves
								if (remaininPVTBusDestination >= intInputDestinaion) {
									intInputDestinaion = intInputDestinaion + remainLeaves;
								} else if (remaininPVTBusOrigin >= intInputOrigin) {
									// if destination non fixed PVT buses are
									// not enough to give leaves get leaves from
									// origin
									intInputOrigin = intInputOrigin + remainLeaves;
								}
							}
						}
					} else if (tempLeaves > noOfLeaves) {
						int extraLeaves = tempLeaves - noOfLeaves;

						if (extraLeaves == 1) {
							if (intInputOrigin > intInputDestinaion && intInputOrigin >= 1) {
								intInputOrigin = intInputOrigin - 1;
							} else if (intInputDestinaion > intInputOrigin && intInputDestinaion >= 1) {
								intInputDestinaion = intInputDestinaion - 1;
							}
						} else {
							System.out.println("do something if leaves are greater than 1");
							// check extraLeaves are odd or even and remove
							// buses from both origin and destination
						}

					}
					if ((intInputOrigin + intInputDestinaion) == noOfLeaves) {

						// check the leaves can be given
						if (intInputOrigin <= remaininPVTBusOrigin && intInputDestinaion <= remaininPVTBusDestination) {
							// valid
						} else {
							if (intInputOrigin > remaininPVTBusOrigin) {

								int extraLeaves = intInputOrigin - remaininPVTBusOrigin;

								// check whether these leaves can be given from
								// destination start
								int remainDesLeaves = remaininPVTBusDestination - intInputDestinaion;
								if (remainDesLeaves >= extraLeaves) {
									intInputOrigin = intInputOrigin + extraLeaves;
									intInputDestinaion = intInputDestinaion - extraLeaves;
								} else {
									System.out.println("cannnot perform coupling for selected route");
									validationDto = new WithoutFixedTimeValidationDTO();
									validationDto.setError("cannnot perform coupling for selected route - Group Two");
									return validationDto;
								}
								// check whether these leaves can be given from
								// destination end

							} else if (intInputDestinaion > remaininPVTBusDestination) {
								int extraLeaves = intInputDestinaion - remaininPVTBusDestination;

								// check whether these leaves can be given from
								// origin start
								int remainOriLeaves = remaininPVTBusOrigin - intInputOrigin;
								if (remainOriLeaves >= extraLeaves) {
									intInputOrigin = intInputOrigin + extraLeaves;
									intInputDestinaion = intInputDestinaion - extraLeaves;
								} else {
									System.out.println("cannnot perform coupling for selected route");
									validationDto = new WithoutFixedTimeValidationDTO();
									validationDto.setError("cannnot perform coupling for selected route - Group Two");
									return validationDto;
								}
								// check whether these leaves can be given from
								// origin end
							}
						}
						// check the leaves can be given

						if (intInputOrigin > 9) {
							validationDto.setNoOfLeavesOrigin(9);
						} else {
							validationDto.setNoOfLeavesOrigin(intInputOrigin);
						} // added by tharushi.e

						if (intInputDestinaion > 9) {
							validationDto.setNoOfLeavesDestination(9);
						} else {
							validationDto.setNoOfLeavesDestination(intInputDestinaion);
						} // added by tharushi.e
						if (intInputOrigin > 9) {
							groupOneDTO.setBusesOnLeaveOriginTwo(9);
						} else {
							groupOneDTO.setBusesOnLeaveOriginTwo(intInputOrigin);
						} // added by tharushi.e

						if (intInputDestinaion > 9) {
							groupOneDTO.setBusesOnLeaveDestinationTwo(9);
						} else {
							groupOneDTO.setBusesOnLeaveDestinationTwo(intInputDestinaion);
						} // added by tharushi.e
						
						panelGeneratorWithoutFixedTimeService.updateLeaves(timeTableDTO.getGenereatedRefNo(), "2", intInputOrigin, intInputDestinaion,
								sessionBackingBean.getLoginUser());
					}

					return validationDto;
				}
			} else {
				// can perform coupling
				int leftBuses = originGroupTwoList.size() - (couplingTimeSlots * 2);
				/*
				 * i.e: originGroupOneList.size()=21 to get num of leaves, calculate ->
				 * originGroupOneList.size())/2 = 10 and 1 bus is left when we couple id we
				 * didn't add that remaining bus/es one time slot will be left to add that time
				 * slot/s have to add the left bus/es int leftBuses = 21 - (10*2) = 1
				 */
				validationDto = new WithoutFixedTimeValidationDTO();

				if (leftBuses > 0) {
					if (remaininPVTBusOrigin >= leftBuses) {
						// add a bus from origin remaining buses
						validationDto.setNoOfLeavesOriginSec(leftBuses);
					} else if (remaininPVTBusDestination >= leftBuses) {
						// add a bus from destination remaining buses
						validationDto.setNoOfLeavesDestinationSec(leftBuses);
					} else {
						// one or more time slot/s will be left -> can't happen
						validationDto = new WithoutFixedTimeValidationDTO();
						validationDto.setError(
								"cannot perform the operation please check time slots and bus numbers - Group Two");
						return validationDto;
					}
				} else {
					validationDto.setSuccess(true);
				}

				return validationDto;
			}
			// if coupling

		} else {

			// if not coupling
			// check all time slots can be filled start
			if (allBuses == originGroupTwoList.size()) {
				System.out.println("time slots and buses are okay, can perform the operation successfully");
				validationDto = new WithoutFixedTimeValidationDTO();
				validationDto.setSuccess(true);
				return validationDto;
			}
			// check all time slots can be filled end

			// give leave start
			DecimalFormat df2 = new DecimalFormat("#.##");
			if (allBuses > originGroupTwoList.size()) {
				// int numberOfLeaves = allBuses - originGroupOneList.size();
				int numberOfLeaves = (((allBusesOrigin * 1) + (allBusesDestinaion * 1)) - originGroupTwoList.size())
						/ 1;
				validationDto = new WithoutFixedTimeValidationDTO();

				double originLeaves = (((double) allBusesOrigin
						- ((double) originfixedPVTbuses + (double) originfixedCTBbuses))
						/ ((double) allBuses - (double) allFixedBuses)) * (double) numberOfLeaves;
				double destinationLeaves = (((double) allBusesDestinaion
						- ((double) destinationfixedPVTbuses + (double) destinationfixedCTBbuses))
						/ ((double) allBuses - (double) allFixedBuses)) * (double) numberOfLeaves;
				;

				BigDecimal bdOrigin = new BigDecimal(originLeaves).setScale(1, RoundingMode.HALF_UP);
				double newInputOrigin = bdOrigin.doubleValue();
				int intInputOrigin = (int) Math.round(newInputOrigin);
				BigDecimal bdDestination = new BigDecimal(destinationLeaves).setScale(1, RoundingMode.HALF_UP);
				double newInputDestinaion = bdDestination.doubleValue();
				int intInputDestinaion = (int) newInputDestinaion;

				int tempLeaves = (int) Math.round(newInputOrigin) + (int) Math.round(newInputDestinaion);
				if (tempLeaves < numberOfLeaves) {
					int remainLeaves = numberOfLeaves - tempLeaves;
					if (intInputOrigin < intInputDestinaion) {
						// intInputOrigin = intInputOrigin+remainLeaves;

						// check whether origin can give this amount of leaves
						if (remaininPVTBusOrigin >= intInputOrigin) {
							// can give leaves
							intInputOrigin = intInputOrigin + remainLeaves;
						}

					} else if (intInputDestinaion < intInputOrigin) {
						// intInputDestinaion = intInputDestinaion+remainLeaves;

						// check whether origin can give this amount of leaves
						if (remaininPVTBusDestination >= intInputDestinaion) {
							// can give leaves
							intInputDestinaion = intInputDestinaion + remainLeaves;
						}
					}
				} else if (tempLeaves > numberOfLeaves) {
					int moreLeaves = tempLeaves - numberOfLeaves;
					if (originLeaves > destinationLeaves) {
						int oriLeave = intInputOrigin - moreLeaves;
						intInputOrigin = oriLeave;
					} else {
						int desLeave = intInputDestinaion - moreLeaves;
						intInputDestinaion = desLeave;
					}
				}

				if ((intInputOrigin + intInputDestinaion) == numberOfLeaves) {

					if (intInputOrigin > 9) {
						validationDto.setNoOfLeavesOrigin(9);
					} else {
						validationDto.setNoOfLeavesOrigin(intInputOrigin);
					} // added by tharushi.e

					if (intInputDestinaion > 9) {
						validationDto.setNoOfLeavesDestination(9);
					} else {
						validationDto.setNoOfLeavesDestination(intInputDestinaion);
					} // added by tharushi.e
					if (intInputOrigin > 9) {
						groupOneDTO.setBusesOnLeaveOriginTwo(9);
					} else {
						groupOneDTO.setBusesOnLeaveOriginTwo(intInputOrigin);
					} // added by tharushi.e

					if (intInputDestinaion > 9) {
						groupOneDTO.setBusesOnLeaveDestinationTwo(9);
					} else {
						groupOneDTO.setBusesOnLeaveDestinationTwo(intInputDestinaion);
					} // added by tharushi.e

					panelGeneratorWithoutFixedTimeService.updateLeaves(timeTableDTO.getGenereatedRefNo(), "2", intInputOrigin, intInputDestinaion,
							sessionBackingBean.getLoginUser());
				} else {
					System.out.println(
							"origin leave buses and destination leave buses are not equal to number of leaves - Group one");
					validationDto = new WithoutFixedTimeValidationDTO();
					validationDto.setError("cannot perform the operation due to leave calculation error - Group One");
					return validationDto;
				}

				return validationDto;
			}
			// give leave end

			int remainingTimeSlots = originGroupTwoList.size() - allBuses;

			if (remainingTimeSlots > 0) {
				// return "Please perform coupling for the route";
				validationDto = new WithoutFixedTimeValidationDTO();
				validationDto.setError("Please perform coupling for the route - Group Two");
				return validationDto;
			}
			// if not coupling
		}

		return null;
	}

	private int getRemainingTimeSlotsOrigin(String group) {

		int timeSlots = 0;

		if (group.equals("1")) {
			for (TimeTableDTO dto : originGroupOneList) {
				if (dto.getBusNo() == null || dto.getBusNo().isEmpty() || dto.getBusNo().trim().equals("")) {
					timeSlots = timeSlots + 1;
				}
			}
		} else if (group.equals("2")) {
			for (TimeTableDTO dto : originGroupTwoList) {
				if (dto.getBusNo() == null || dto.getBusNo().isEmpty() || dto.getBusNo().trim().equals("")) {
					timeSlots = timeSlots + 1;
				}
			}
		} else if (group.equals("3")) {
			for (TimeTableDTO dto : originGroupThreeList) {
				if (dto.getBusNo() == null || dto.getBusNo().isEmpty() || dto.getBusNo().trim().equals("")) {
					timeSlots = timeSlots + 1;
				}
			}
		}

		return timeSlots;
	}

	private int getRemainingTimeSlotsDestination(String group) {

		int timeSlots = 0;

		if (group.equals("1")) {
			for (TimeTableDTO dto : destinationGroupOneList) {
				if (dto.getBusNo() == null || dto.getBusNo().isEmpty() || dto.getBusNo().trim().equals("")) {
					timeSlots = timeSlots + 1;
				}
			}
		} else if (group.equals("2")) {
			for (TimeTableDTO dto : destinationGroupTwoList) {
				if (dto.getBusNo() == null || dto.getBusNo().isEmpty() || dto.getBusNo().trim().equals("")) {
					timeSlots = timeSlots + 1;
				}
			}
		} else if (group.equals("3")) {
			for (TimeTableDTO dto : destinationGroupThreeList) {
				if (dto.getBusNo() == null || dto.getBusNo().isEmpty() || dto.getBusNo().trim().equals("")) {
					timeSlots = timeSlots + 1;
				}
			}
		}

		return timeSlots;
	}

	public void groupOneOriginList(String abbreviationOrigin) {
		originGroupOneList = new ArrayList<TimeTableDTO>();
		originGroupOneList = timeTableService.getDetailsOfFixedBuses(timeTableDTO, "1", "O"); // get
																								// time
																								// slots
																								// added
																								// and
																								// fixed
																								// buses

		int originPVTBus1 = panelGeneratorWithoutFixedTimeService
				.retrievePrivateBusNumbers(timeTableDTO.getGenereatedRefNo(), "1", "O");// get
																						// private
																						// bus
																						// count

		List<String> privateBusNumListO1 = new ArrayList<String>();
		for (int i = 0; i < originPVTBus1; i++) {
			int suffix = i + 1;
			privateBusNumListO1.add(abbreviationOrigin + suffix);
		}

		List<String> alreadyAddedprivateBusNumListO1 = new ArrayList<String>();// already
																				// added
																				// private
																				// bus
																				// numbers
																				// in
																				// fixed
																				// bus
																				// list
		alreadyAddedprivateBusNumListO1 = panelGeneratorWithoutFixedTimeService
				.retreivePrivateBusNumbersInFixedBuses(timeTableDTO, "1", "O", abbreviationOrigin);

//		added by thilna.d on 13-10-2021
		int alreadyAddedprivateBusNumListO1Count = 0;
		if (coupling) {
			alreadyAddedprivateBusNumListO1Count = alreadyAddedprivateBusNumListO1.size() / 4;
		} else {
			alreadyAddedprivateBusNumListO1Count = alreadyAddedprivateBusNumListO1.size();
		}

		int remainingBusCountO1 = privateBusNumListO1.size() - alreadyAddedprivateBusNumListO1Count;// remaining
																									// private
																									// bus
																									// count
																									// to
																									// add
																									// to
																									// main
																									// list

		boolean busNumFoundO1 = false;
		List<String> tempBusNumListO1 = new ArrayList<String>();
		// fixed private buses are not in ascending order so have to check which
		// bus numbers are in fixed private buses and what to add to final list
		for (String a : privateBusNumListO1) {
			busNumFoundO1 = false;
			for (String b : alreadyAddedprivateBusNumListO1) {
				if (a.equals(b)) {
					busNumFoundO1 = true;
				}
			}
			if (!busNumFoundO1) {
				tempBusNumListO1.add(a);
			}
		}

		List<String> finalPVTBusesO1 = new ArrayList<String>();// private buses
																// to add to
																// main list
		for (int i = 0; i < remainingBusCountO1; i++) {
			finalPVTBusesO1.add(tempBusNumListO1.get(i));
		}
		orginBusAbbriviationMain = finalPVTBusesO1;

		List<TimeTableDTO> tempOriginListOne = new ArrayList<TimeTableDTO>();
		int addBusCountO1 = 0;
		for (TimeTableDTO dto : originGroupOneList) {
			if (dto.getBusNo() != null && !dto.getBusNo().isEmpty() && !dto.getBusNo().trim().equals("")) {
				tempOriginListOne.add(dto);
			} else {
				if (addBusCountO1 < finalPVTBusesO1.size()) {
					TimeTableDTO tempDto = new TimeTableDTO();
					tempDto = dto;
					tempDto.setBusNo(finalPVTBusesO1.get(addBusCountO1));
					tempDto.setDuplicateBusNum(finalPVTBusesO1.get(addBusCountO1));
					tempOriginListOne.add(tempDto);
					addBusCountO1 = addBusCountO1 + 1;
				} else {
					tempOriginListOne.add(dto);
				}
			}
		}

		originGroupOneList = new ArrayList<TimeTableDTO>();
		originGroupOneList = tempOriginListOne;

	}

	public void groupOneDestination(String abbreviationDestination) {
		destinationGroupOneList = new ArrayList<TimeTableDTO>();
		destinationGroupOneList = timeTableService.getDetailsOfFixedBuses(timeTableDTO, "1", "D"); // get
																									// time
																									// slots
																									// added
																									// and
																									// fixed
																									// buses

		int destinationPVTBus1 = panelGeneratorWithoutFixedTimeService
				.retrievePrivateBusNumbers(timeTableDTO.getGenereatedRefNo(), "1", "D");// get
																						// private
																						// bus
																						// count

		List<String> privateBusNumListD1 = new ArrayList<String>();
		for (int i = 0; i < destinationPVTBus1; i++) {
			int suffix = i + 1;
			privateBusNumListD1.add(abbreviationDestination + suffix);
		}

		List<String> alreadyAddedprivateBusNumListD1 = new ArrayList<String>();// already
																				// added
																				// private
																				// bus
																				// numbers
																				// in
																				// fixed
																				// bus
																				// list
		alreadyAddedprivateBusNumListD1 = panelGeneratorWithoutFixedTimeService
				.retreivePrivateBusNumbersInFixedBuses(timeTableDTO, "1", "D", abbreviationDestination);

//		added by thilna.d on 13-10-2021
		int alreadyAddedprivateBusNumListD1Count = 0;
		if (coupling) {
			alreadyAddedprivateBusNumListD1Count = alreadyAddedprivateBusNumListD1.size() / 4;
		} else {
			alreadyAddedprivateBusNumListD1Count = alreadyAddedprivateBusNumListD1.size();
		}

		int remainingBusCountD1 = privateBusNumListD1.size() - alreadyAddedprivateBusNumListD1Count;// remaining
																									// private
																									// bus
																									// count
																									// to
																									// add
																									// to
																									// main
																									// list

		boolean busNumFoundD1 = false;
		List<String> tempBusNumListD1 = new ArrayList<String>();
		// fixed private buses are not in ascending order so have to check which
		// bus numbers are in fixed private buses and what to add to final list
		for (String a : privateBusNumListD1) {
			busNumFoundD1 = false;
			for (String b : alreadyAddedprivateBusNumListD1) {
				if (a.equals(b)) {
					busNumFoundD1 = true;
				}
			}
			if (!busNumFoundD1) {
				tempBusNumListD1.add(a);
			}
		}

		List<String> finalPVTBusesD1 = new ArrayList<String>();// private buses
																// to add to
																// main list
		for (int i = 0; i < remainingBusCountD1; i++) {
			finalPVTBusesD1.add(tempBusNumListD1.get(i));
		}
		destinationBusAbbriviationMain = finalPVTBusesD1;

		List<TimeTableDTO> tempDestinatinlistOne = new ArrayList<TimeTableDTO>();
		int addBusCountD1 = 0;
		for (TimeTableDTO dto : destinationGroupOneList) {
			if (dto.getBusNo() != null && !dto.getBusNo().isEmpty() && !dto.getBusNo().trim().equals("")) {
				tempDestinatinlistOne.add(dto);
			} else {
				if (addBusCountD1 < finalPVTBusesD1.size()) {
					TimeTableDTO tempDto = new TimeTableDTO();
					tempDto = dto;
					tempDto.setBusNo(finalPVTBusesD1.get(addBusCountD1));
					tempDto.setDuplicateBusNum(finalPVTBusesD1.get(addBusCountD1));
					tempDestinatinlistOne.add(tempDto);
					addBusCountD1 = addBusCountD1 + 1;
				} else {
					tempDestinatinlistOne.add(dto);
				}
			}
		}

		destinationGroupOneList = new ArrayList<TimeTableDTO>();
		destinationGroupOneList = tempDestinatinlistOne;

	}

	public void groupOneDestinationBusesToOrigin(String driverRestTime, String busRideTime) {
		// printTimeTableDTOList(originGroupOneList);
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		List<TimeTableDTO> destinationListFromOrigin = new ArrayList<TimeTableDTO>();
		for (TimeTableDTO destinationDTO : destinationGroupOneList) {
			destinationListFromOrigin.add(destinationDTO);
		}

		try {

			// driverRestTime
			// busRideTime

			for (TimeTableDTO destinationDTO : destinationGroupOneList) {

				Date driverRestInDate = timeFormat.parse(driverRestTime);
				Date destinationEndTimeDate = timeFormat.parse(destinationDTO.getEndTime());
				long originStartTime = destinationEndTimeDate.getTime() + driverRestInDate.getTime();// destination
																										// bus
																										// end
																										// time
																										// +
																										// driver
																										// rest
																										// time
				String originStartTimeStr = timeFormat.format(new Date(originStartTime));

				Date originStartTimeDate = timeFormat.parse(originStartTimeStr);
				Date busRideTimeDate = timeFormat.parse(busRideTime);
				long originEndTime = originStartTimeDate.getTime() + busRideTimeDate.getTime();// origin
																								// start
																								// time
																								// +
																								// bus
																								// ride
																								// time
				String originEndTimeStr = timeFormat.format(new Date(originEndTime));

				TimeTableDTO tempDTO = new TimeTableDTO();
				tempDTO = destinationDTO;
				tempDTO.setOriginStartTimeString(originStartTimeStr);
				tempDTO.setOriginEndTimeString(originEndTimeStr);

				destinationListFromOrigin.remove(destinationDTO);
				destinationListFromOrigin.add(tempDTO);
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		/** create destination buses start end time from origin end **/

		/** add destination buses to origin list start **/
		// create temp origin list
		List<TimeTableDTO> tempOriginGroupOneList = new ArrayList<TimeTableDTO>();
		for (TimeTableDTO originDTO : originGroupOneList) {
			tempOriginGroupOneList.add(originDTO);
		}

		boolean timeFound = false;
		List<TimeTableDTO> timeSlotNotFoundListD = new ArrayList<TimeTableDTO>();
		// printTimeTableDTOList(destinationListFromOrigin);
		for (TimeTableDTO destinationDTO : destinationListFromOrigin) {
			timeFound = false;
			for (TimeTableDTO originDTO : originGroupOneList) {
				if (destinationDTO.getOriginStartTimeString().equals(originDTO.getStartTime())
						&& destinationDTO.getOriginEndTimeString().equals(originDTO.getEndTime())) {
					if (originDTO.isFixedTime()) {
						timeSlotNotFoundListD.add(destinationDTO); // a fixed
																	// bus is
																	// already
																	// added to
																	// the time
																	// slot
						timeFound = true;
					} else if (originDTO.getBusNo() != null && !originDTO.getBusNo().isEmpty()
							&& !originDTO.getBusNo().trim().equals("")) {
						timeSlotNotFoundListD.add(destinationDTO); // a bus is
																	// added
																	// from
																	// origin
																	// and
																	// cannot
																	// put
																	// another
																	// to the
																	// time slot
						timeFound = true;
					} else {
						// can add this to origin list
						if (!destinationDTO.isFixedTime()) {
							TimeTableDTO tempDTO = new TimeTableDTO();
							tempDTO = originDTO;
							tempDTO.setBusNo(destinationDTO.getBusNo());
							tempDTO.setDuplicateBusNum(destinationDTO.getBusNo());
							tempDTO.setTripType(destinationDTO.getTripType());
							tempDTO.setTripId(destinationDTO.getTripId());

							tempOriginGroupOneList.remove(originDTO);
							tempOriginGroupOneList.add(tempDTO);
							timeFound = true;
							break;

						}

					}

				}
			}
			if (!timeFound) {
				if (destinationDTO.getBusNo() != null && !destinationDTO.getBusNo().isEmpty()) {
					timeSlotNotFoundListD.add(destinationDTO);
				}
			}
		}

		// find time slots for not added bus numbers from origin list
		try {
			// origin list order start
			Collections.sort(originGroupOneList, new Comparator<TimeTableDTO>() {
				@Override
				public int compare(TimeTableDTO u1, TimeTableDTO u2) {
					// return u2.getStage().compareTo(u1.getStage());
					return Integer.valueOf(u2.getId()) - Integer.valueOf(u1.getId());
				}
			});

			Collections.reverse(originGroupOneList);
			// origin list order end

			for (TimeTableDTO noSlotsDTO : timeSlotNotFoundListD) {
				for (TimeTableDTO originDTO : originGroupOneList) {
					if (originDTO.getBusNo() == null || originDTO.getBusNo().isEmpty()) {
						System.out.println(originDTO.getStartTime() + "  " + originDTO.getEndTime());

						Date startDate = timeFormat.parse(noSlotsDTO.getOriginStartTimeString());
						Date oStartDate = timeFormat.parse(originDTO.getStartTime());
						Date endDate = timeFormat.parse(noSlotsDTO.getOriginEndTimeString());
						Date oEndtDate = timeFormat.parse(originDTO.getEndTime());

						if (oStartDate.after(startDate) && oEndtDate.after(endDate)) {
							System.out.println("origin time is greater than start time");

							if (!noSlotsDTO.isFixedTime()) {

								// can add this to origin list
								TimeTableDTO tempDTO = new TimeTableDTO();
								tempDTO = originDTO;
								tempDTO.setBusNo(noSlotsDTO.getBusNo());
								tempDTO.setDuplicateBusNum(noSlotsDTO.getBusNo());
								tempDTO.setTripType(noSlotsDTO.getTripType());
								tempDTO.setTripId(noSlotsDTO.getTripId());

								tempOriginGroupOneList.remove(originDTO);
								tempOriginGroupOneList.add(tempDTO);
								break;
							}
						}

					}
				}
			}

			originGroupOneList = new ArrayList<TimeTableDTO>();
			originGroupOneList = tempOriginGroupOneList;

			// origin list order start
			Collections.sort(originGroupOneList, new Comparator<TimeTableDTO>() {
				@Override
				public int compare(TimeTableDTO u1, TimeTableDTO u2) {
					// return u2.getStage().compareTo(u1.getStage());
					return Integer.valueOf(u2.getId()) - Integer.valueOf(u1.getId());
				}
			});

			Collections.reverse(originGroupOneList);
			// origin list order end
		} catch (ParseException e) {
			e.printStackTrace();
		}
		/** add destination buses to origin list end **/
		// printTimeTableDTOList(originGroupOneList);
	}

	public void groupOneOriginBusesToDestination(String driverRestTime, String busRideTime) {
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		List<TimeTableDTO> originListFromDestination = new ArrayList<TimeTableDTO>();
		for (TimeTableDTO originDTO : originGroupOneList) {
			originListFromDestination.add(originDTO);
		}

		try {

			// driverRestTime
			// busRideTime

			for (TimeTableDTO originDTO : originGroupOneList) {

				Date driverRestInDate = timeFormat.parse(driverRestTime);
				Date originEndTimeDate = timeFormat.parse(originDTO.getEndTime());
				long destinationStartTime = originEndTimeDate.getTime() + driverRestInDate.getTime();// origin
																										// bus
																										// end
																										// time
																										// +
																										// driver
																										// rest
																										// time
				String destinationStartTimeStr = timeFormat.format(new Date(destinationStartTime));

				Date destinationStartTimeDate = timeFormat.parse(destinationStartTimeStr);
				Date busRideTimeDate = timeFormat.parse(busRideTime);
				long destinationEndTime = destinationStartTimeDate.getTime() + busRideTimeDate.getTime();// destination
																											// start
																											// time
																											// +
																											// bus
																											// ride
																											// time
				String destinationEndTimeStr = timeFormat.format(new Date(destinationEndTime));

				TimeTableDTO tempDTO = new TimeTableDTO();
				tempDTO = originDTO;
				tempDTO.setOriginStartTimeString(destinationStartTimeStr);
				tempDTO.setOriginEndTimeString(destinationEndTimeStr);

				originListFromDestination.remove(originDTO);
				originListFromDestination.add(tempDTO);
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		/** create origin buses start end time from destination end **/

		/** add origin buses to destination list start **/
		// create temp destinatino list
		List<TimeTableDTO> tempdestinationGroupOneList = new ArrayList<TimeTableDTO>();
		for (TimeTableDTO originDTO : destinationGroupOneList) {
			tempdestinationGroupOneList.add(originDTO);
		}

		boolean timeFoundO = false;
		List<TimeTableDTO> timeSlotNotFoundListO = new ArrayList<TimeTableDTO>();
		for (TimeTableDTO originDTO : originListFromDestination) {
			timeFoundO = false;
			for (TimeTableDTO destinationDTO : destinationGroupOneList) {
				if (originDTO.getTripType().equalsIgnoreCase("O")
						&& originDTO.getOriginStartTimeString().equals(destinationDTO.getStartTime())
						&& originDTO.getOriginEndTimeString().equals(destinationDTO.getEndTime())) {
					if (destinationDTO.isFixedTime()) {
						timeSlotNotFoundListO.add(originDTO); // a fixed bus is
																// already added
																// to the time
																// slot
						timeFoundO = true;
					} else if (destinationDTO.getBusNo() != null && !destinationDTO.getBusNo().isEmpty()
							&& !destinationDTO.getBusNo().trim().equals("")) {
						timeSlotNotFoundListO.add(originDTO);// a bus is added
																// from origin
																// and cannot
																// put another
																// to the time
																// slot
						timeFoundO = true;
					} else {
						if (!originDTO.isFixedTime()) {
							// can add this to origin list
							TimeTableDTO tempDTO = new TimeTableDTO();
							tempDTO = destinationDTO;
							tempDTO.setBusNo(originDTO.getBusNo());
							tempDTO.setDuplicateBusNum(originDTO.getBusNo());
							tempDTO.setTripType(originDTO.getTripType());
							tempDTO.setTripId(originDTO.getTripId());

							tempdestinationGroupOneList.remove(destinationDTO);
							tempdestinationGroupOneList.add(tempDTO);
							timeFoundO = true;
							break;

						}
					}
				}
			}
			if (!timeFoundO) {
				if (originDTO.getTripType().equalsIgnoreCase("O") && originDTO.getBusNo() != null
						&& !originDTO.getBusNo().isEmpty()) {
					timeSlotNotFoundListO.add(originDTO);
				}
			}
		}

		// find time slots for not added bus numbers from origin list
		try {
			// origin list order start
			Collections.sort(destinationGroupOneList, new Comparator<TimeTableDTO>() {
				@Override
				public int compare(TimeTableDTO u1, TimeTableDTO u2) {
					// return u2.getStage().compareTo(u1.getStage());
					return Integer.valueOf(u2.getId()) - Integer.valueOf(u1.getId());
				}
			});

			Collections.reverse(destinationGroupOneList);
			// origin list order end

			for (TimeTableDTO noSlotsDTO : timeSlotNotFoundListO) {
				for (TimeTableDTO destinationDTO : destinationGroupOneList) {
					if (destinationDTO.getBusNo() == null || destinationDTO.getBusNo().isEmpty()) {
						Date startDate = timeFormat.parse(noSlotsDTO.getOriginStartTimeString());
						Date dStartDate = timeFormat.parse(destinationDTO.getStartTime());
						Date endDate = timeFormat.parse(noSlotsDTO.getOriginEndTimeString());
						Date dEndtDate = timeFormat.parse(destinationDTO.getEndTime());

						if (dStartDate.after(startDate) && dEndtDate.after(endDate)) {
							System.out.println("destination time is greater than start time");

							if (!noSlotsDTO.isFixedTime()) {
								// can add this to origin list
								TimeTableDTO tempDTO = new TimeTableDTO();
								tempDTO = destinationDTO;
								tempDTO.setBusNo(noSlotsDTO.getBusNo());
								tempDTO.setDuplicateBusNum(noSlotsDTO.getBusNo());
								tempDTO.setTripType(noSlotsDTO.getTripType());
								tempDTO.setTripId(noSlotsDTO.getTripId());

								tempdestinationGroupOneList.remove(destinationDTO);
								tempdestinationGroupOneList.add(tempDTO);
								break;
							}
						}
					}
				}
			}

			destinationGroupOneList = new ArrayList<TimeTableDTO>();
			destinationGroupOneList = tempdestinationGroupOneList;

			// origin list order start
			Collections.sort(destinationGroupOneList, new Comparator<TimeTableDTO>() {
				@Override
				public int compare(TimeTableDTO u1, TimeTableDTO u2) {
					// return u2.getStage().compareTo(u1.getStage());
					return Integer.valueOf(u2.getId()) - Integer.valueOf(u1.getId());
				}
			});

			Collections.reverse(destinationGroupOneList);
			// origin list order end

		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	public void groupTwoOriginList(String abbreviationOrigin) {
		originGroupTwoList = new ArrayList<TimeTableDTO>();
		originGroupTwoList = timeTableService.getDetailsOfFixedBuses(timeTableDTO, "2", "O"); // get
																								// time
																								// slots
																								// added
																								// and
																								// fixed
																								// buses

		int originPVTBus1 = panelGeneratorWithoutFixedTimeService
				.retrievePrivateBusNumbers(timeTableDTO.getGenereatedRefNo(), "2", "O");// get
																						// private
																						// bus
																						// count
		List<String> privateBusNumListO2 = new ArrayList<String>();
		for (int i = 0; i < originPVTBus1; i++) {
			int suffix = i + 1;
			privateBusNumListO2.add(abbreviationOrigin + suffix);
		}

		List<String> alreadyAddedprivateBusNumListO2 = new ArrayList<String>();// already
																				// added
																				// private
																				// bus
																				// numbers
																				// in
																				// fixed
																				// bus
																				// list
		alreadyAddedprivateBusNumListO2 = panelGeneratorWithoutFixedTimeService
				.retreivePrivateBusNumbersInFixedBuses(timeTableDTO, "2", "O", abbreviationOrigin);

		int remainingBusCountO2 = privateBusNumListO2.size() - alreadyAddedprivateBusNumListO2.size();// remaining
																										// private
																										// bus
																										// count
																										// to
																										// add
																										// to
																										// main
																										// list

		boolean busNumFoundO2 = false;
		List<String> tempBusNumListO2 = new ArrayList<String>();
		// fixed private buses are not in ascending order so have to check which
		// bus numbers are in fixed private buses and what to add to final list
		for (String a : privateBusNumListO2) {
			busNumFoundO2 = false;
			for (String b : alreadyAddedprivateBusNumListO2) {
				if (a.equals(b)) {
					busNumFoundO2 = true;
				}
			}
			if (!busNumFoundO2) {
				tempBusNumListO2.add(a);
			}
		}

		List<String> finalPVTBusesO2 = new ArrayList<String>();// private buses
																// to add to
																// main list
		for (int i = 0; i < remainingBusCountO2; i++) {
			finalPVTBusesO2.add(tempBusNumListO2.get(i));
		}

		List<TimeTableDTO> tempOriginListTwo = new ArrayList<TimeTableDTO>();
		int addBusCountO1 = 0;
		for (TimeTableDTO dto : originGroupTwoList) {
			if (dto.getBusNo() != null && !dto.getBusNo().isEmpty() && !dto.getBusNo().trim().equals("")) {
				tempOriginListTwo.add(dto);
			} else {
				if (addBusCountO1 < finalPVTBusesO2.size()) {
					TimeTableDTO tempDto = new TimeTableDTO();
					tempDto = dto;
					tempDto.setBusNo(finalPVTBusesO2.get(addBusCountO1));
					tempDto.setDuplicateBusNum(finalPVTBusesO2.get(addBusCountO1));
					tempOriginListTwo.add(tempDto);
					addBusCountO1 = addBusCountO1 + 1;
				} else {
					tempOriginListTwo.add(dto);
				}
			}
		}

		originGroupTwoList = new ArrayList<TimeTableDTO>();
		originGroupTwoList = tempOriginListTwo;

	}

	public void groupTwoDestination(String abbreviationDestination) {
		destinationGroupTwoList = new ArrayList<TimeTableDTO>();
		destinationGroupTwoList = timeTableService.getDetailsOfFixedBuses(timeTableDTO, "2", "D"); // get
																									// time
																									// slots
																									// added
																									// and
																									// fixed
																									// buses

		int destinationPVTBus1 = panelGeneratorWithoutFixedTimeService
				.retrievePrivateBusNumbers(timeTableDTO.getGenereatedRefNo(), "2", "D");// get
																						// private
																						// bus
																						// count
		List<String> privateBusNumListD2 = new ArrayList<String>();
		for (int i = 0; i < destinationPVTBus1; i++) {
			int suffix = i + 1;
			privateBusNumListD2.add(abbreviationDestination + suffix);
		}

		List<String> alreadyAddedprivateBusNumListD2 = new ArrayList<String>();// already
																				// added
																				// private
																				// bus
																				// numbers
																				// in
																				// fixed
																				// bus
																				// list
		alreadyAddedprivateBusNumListD2 = panelGeneratorWithoutFixedTimeService
				.retreivePrivateBusNumbersInFixedBuses(timeTableDTO, "2", "D", abbreviationDestination);

		int remainingBusCountD2 = privateBusNumListD2.size() - alreadyAddedprivateBusNumListD2.size();// remaining
																										// private
																										// bus
																										// count
																										// to
																										// add
																										// to
																										// main
																										// list

		boolean busNumFoundD2 = false;
		List<String> tempBusNumListD2 = new ArrayList<String>();
		// fixed private buses are not in ascending order so have to check which
		// bus numbers are in fixed private buses and what to add to final list
		for (String a : privateBusNumListD2) {
			busNumFoundD2 = false;
			for (String b : alreadyAddedprivateBusNumListD2) {
				if (a.equals(b)) {
					busNumFoundD2 = true;
				}
			}
			if (!busNumFoundD2) {
				tempBusNumListD2.add(a);
			}
		}

		List<String> finalPVTBusesD2 = new ArrayList<String>();// private buses
																// to add to
																// main list
		for (int i = 0; i < remainingBusCountD2; i++) {
			finalPVTBusesD2.add(tempBusNumListD2.get(i));
		}

		List<TimeTableDTO> tempDestinatinlistTwo = new ArrayList<TimeTableDTO>();
		int addBusCountD2 = 0;
		for (TimeTableDTO dto : destinationGroupTwoList) {
			if (dto.getBusNo() != null && !dto.getBusNo().isEmpty() && !dto.getBusNo().trim().equals("")) {
				tempDestinatinlistTwo.add(dto);
			} else {
				if (addBusCountD2 < finalPVTBusesD2.size()) {
					TimeTableDTO tempDto = new TimeTableDTO();
					tempDto = dto;
					tempDto.setBusNo(finalPVTBusesD2.get(addBusCountD2));
					tempDto.setDuplicateBusNum(finalPVTBusesD2.get(addBusCountD2));
					tempDestinatinlistTwo.add(tempDto);
					addBusCountD2 = addBusCountD2 + 1;
				} else {
					tempDestinatinlistTwo.add(dto);
				}
			}
		}

		destinationGroupTwoList = new ArrayList<TimeTableDTO>();
		destinationGroupTwoList = tempDestinatinlistTwo;

	}

	public void groupTwoDestinationBusesToOrigin(String driverRestTime, String busRideTime) {

		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		List<TimeTableDTO> destinationListFromOrigin = new ArrayList<TimeTableDTO>();
		for (TimeTableDTO destinationDTO : destinationGroupTwoList) {
			destinationListFromOrigin.add(destinationDTO);
		}

		try {

			// driverRestTime
			// busRideTime

			for (TimeTableDTO destinationDTO : destinationGroupTwoList) {

				Date driverRestInDate = timeFormat.parse(driverRestTime);
				Date destinationEndTimeDate = timeFormat.parse(destinationDTO.getEndTime());
				long originStartTime = destinationEndTimeDate.getTime() + driverRestInDate.getTime();// destination
																										// bus
																										// end
																										// time
																										// +
																										// driver
																										// rest
																										// time
				String originStartTimeStr = timeFormat.format(new Date(originStartTime));

				Date originStartTimeDate = timeFormat.parse(originStartTimeStr);
				Date busRideTimeDate = timeFormat.parse(busRideTime);
				long originEndTime = originStartTimeDate.getTime() + busRideTimeDate.getTime();// origin
																								// start
																								// time
																								// +
																								// bus
																								// ride
																								// time
				String originEndTimeStr = timeFormat.format(new Date(originEndTime));

				TimeTableDTO tempDTO = new TimeTableDTO();
				tempDTO = destinationDTO;
				tempDTO.setOriginStartTimeString(originStartTimeStr);
				tempDTO.setOriginEndTimeString(originEndTimeStr);

				destinationListFromOrigin.remove(destinationDTO);
				destinationListFromOrigin.add(tempDTO);
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		/** create destination buses start end time from origin end **/

		/** add destination buses to origin list start **/
		// create temp origin list
		List<TimeTableDTO> tempOriginGroupOneList = new ArrayList<TimeTableDTO>();
		for (TimeTableDTO originDTO : originGroupTwoList) {
			tempOriginGroupOneList.add(originDTO);
		}

		boolean timeFound = false;
		List<TimeTableDTO> timeSlotNotFoundListD = new ArrayList<TimeTableDTO>();
		for (TimeTableDTO destinationDTO : destinationListFromOrigin) {
			timeFound = false;
			for (TimeTableDTO originDTO : originGroupTwoList) {
				if (destinationDTO.getOriginStartTimeString().equals(originDTO.getStartTime())
						&& destinationDTO.getOriginEndTimeString().equals(originDTO.getEndTime())) {
					if (originDTO.isFixedTime()) {
						timeSlotNotFoundListD.add(destinationDTO); // a fixed
																	// bus is
																	// already
																	// added to
																	// the time
																	// slot
						timeFound = true;
					} else if (originDTO.getBusNo() != null && !originDTO.getBusNo().isEmpty()
							&& !originDTO.getBusNo().trim().equals("")) {
						timeSlotNotFoundListD.add(destinationDTO); // a bus is
																	// added
																	// from
																	// origin
																	// and
																	// cannot
																	// put
																	// another
																	// to the
																	// time slot
						timeFound = true;
					} else {
						// can add this to origin list
						TimeTableDTO tempDTO = new TimeTableDTO();
						tempDTO = originDTO;
						tempDTO.setBusNo(destinationDTO.getBusNo());
						tempDTO.setDuplicateBusNum(destinationDTO.getBusNo());
						tempDTO.setTripType(destinationDTO.getTripType());
						tempDTO.setTripId(destinationDTO.getTripId());

						tempOriginGroupOneList.remove(originDTO);
						tempOriginGroupOneList.add(tempDTO);
						timeFound = true;
						break;

					}

				}
			}
			if (!timeFound) {
				if (destinationDTO.getBusNo() != null && !destinationDTO.getBusNo().isEmpty()) {
					timeSlotNotFoundListD.add(destinationDTO);
				}
			}
		}

		// find time slots for not added bus numbers from origin list
		try {
			// origin list order start
			Collections.sort(originGroupTwoList, new Comparator<TimeTableDTO>() {
				@Override
				public int compare(TimeTableDTO u1, TimeTableDTO u2) {
					// return u2.getStage().compareTo(u1.getStage());
					return Integer.valueOf(u2.getId()) - Integer.valueOf(u1.getId());
				}
			});

			Collections.reverse(originGroupTwoList);
			// origin list order end
			for (TimeTableDTO noSlotsDTO : timeSlotNotFoundListD) {
				for (TimeTableDTO originDTO : originGroupTwoList) {
					if (originDTO.getBusNo() == null || originDTO.getBusNo().isEmpty()) {

						Date startDate = timeFormat.parse(noSlotsDTO.getOriginStartTimeString());
						Date oStartDate = timeFormat.parse(originDTO.getStartTime());
						Date endDate = timeFormat.parse(noSlotsDTO.getOriginEndTimeString());
						Date oEndtDate = timeFormat.parse(originDTO.getEndTime());

						if (oStartDate.after(startDate) && oEndtDate.after(endDate)) {
							System.out.println("origin time is greater than start time");

							// can add this to origin list
							TimeTableDTO tempDTO = new TimeTableDTO();
							tempDTO = originDTO;
							tempDTO.setBusNo(noSlotsDTO.getBusNo());
							tempDTO.setDuplicateBusNum(noSlotsDTO.getBusNo());
							tempDTO.setTripType(noSlotsDTO.getTripType());
							tempDTO.setTripId(noSlotsDTO.getTripId());

							tempOriginGroupOneList.remove(originDTO);
							tempOriginGroupOneList.add(tempDTO);
							break;
						}

					}
				}
			}

			originGroupTwoList = new ArrayList<TimeTableDTO>();
			originGroupTwoList = tempOriginGroupOneList;

		} catch (ParseException e) {
			e.printStackTrace();
		}
		/** add destination buses to origin list end **/
	}

	public void groupTwoOriginBusesToDestination(String driverRestTime, String busRideTime) {

		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		List<TimeTableDTO> originListFromDestination = new ArrayList<TimeTableDTO>();
		for (TimeTableDTO originDTO : originGroupTwoList) {
			originListFromDestination.add(originDTO);
		}

		try {

			// driverRestTime
			// busRideTime

			for (TimeTableDTO originDTO : originGroupTwoList) {

				Date driverRestInDate = timeFormat.parse(driverRestTime);
				Date originEndTimeDate = timeFormat.parse(originDTO.getEndTime());
				long destinationStartTime = originEndTimeDate.getTime() + driverRestInDate.getTime();// origin
																										// bus
																										// end
																										// time
																										// +
																										// driver
																										// rest
																										// time
				String destinationStartTimeStr = timeFormat.format(new Date(destinationStartTime));

				Date destinationStartTimeDate = timeFormat.parse(destinationStartTimeStr);
				Date busRideTimeDate = timeFormat.parse(busRideTime);
				long destinationEndTime = destinationStartTimeDate.getTime() + busRideTimeDate.getTime();// destination
																											// start
																											// time
																											// +
																											// bus
																											// ride
																											// time
				String destinationEndTimeStr = timeFormat.format(new Date(destinationEndTime));

				TimeTableDTO tempDTO = new TimeTableDTO();
				tempDTO = originDTO;
				tempDTO.setOriginStartTimeString(destinationStartTimeStr);
				tempDTO.setOriginEndTimeString(destinationEndTimeStr);

				originListFromDestination.remove(originDTO);
				originListFromDestination.add(tempDTO);
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		/** create origin buses start end time from destination end **/

		/** add origin buses to destination list start **/
		// create temp destinatino list
		List<TimeTableDTO> tempdestinationGroupTwoList = new ArrayList<TimeTableDTO>();
		for (TimeTableDTO originDTO : destinationGroupTwoList) {
			tempdestinationGroupTwoList.add(originDTO);
		}

		boolean timeFoundO = false;
		List<TimeTableDTO> timeSlotNotFoundListO = new ArrayList<TimeTableDTO>();
		for (TimeTableDTO originDTO : originListFromDestination) {
			timeFoundO = false;
			for (TimeTableDTO destinationDTO : destinationGroupTwoList) {
				if (originDTO.getTripType().equalsIgnoreCase("O")
						&& originDTO.getOriginStartTimeString().equals(destinationDTO.getStartTime())
						&& originDTO.getOriginEndTimeString().equals(destinationDTO.getEndTime())) {
					if (destinationDTO.isFixedTime()) {
						timeSlotNotFoundListO.add(originDTO); // a fixed bus is
																// already added
																// to the time
																// slot
						timeFoundO = true;
					} else if (destinationDTO.getBusNo() != null && !destinationDTO.getBusNo().isEmpty()
							&& !destinationDTO.getBusNo().trim().equals("")) {
						timeSlotNotFoundListO.add(originDTO);// a bus is added
																// from origin
																// and cannot
																// put another
																// to the time
																// slot
						timeFoundO = true;
					} else {
						// can add this to origin list
						TimeTableDTO tempDTO = new TimeTableDTO();
						tempDTO = destinationDTO;
						tempDTO.setBusNo(originDTO.getBusNo());
						tempDTO.setDuplicateBusNum(originDTO.getBusNo());
						tempDTO.setTripType(originDTO.getTripType());
						tempDTO.setTripId(originDTO.getTripId());

						tempdestinationGroupTwoList.remove(destinationDTO);
						tempdestinationGroupTwoList.add(tempDTO);
						timeFoundO = true;
						break;
					}
				}
			}
			if (!timeFoundO) {
				if (originDTO.getTripType().equalsIgnoreCase("O") && originDTO.getBusNo() != null
						&& !originDTO.getBusNo().isEmpty()) {
					timeSlotNotFoundListO.add(originDTO);
				}
			}
		}

		// find time slots for not added bus numbers from origin list
		try {
			// destination list order start
			Collections.sort(destinationGroupTwoList, new Comparator<TimeTableDTO>() {
				@Override
				public int compare(TimeTableDTO u1, TimeTableDTO u2) {
					// return u2.getStage().compareTo(u1.getStage());
					return Integer.valueOf(u2.getId()) - Integer.valueOf(u1.getId());
				}
			});

			Collections.reverse(destinationGroupTwoList);
			// destination list order end
			for (TimeTableDTO noSlotsDTO : timeSlotNotFoundListO) {
				for (TimeTableDTO destinationDTO : destinationGroupTwoList) {
					if (destinationDTO.getBusNo() == null || destinationDTO.getBusNo().isEmpty()) {
						Date startDate = timeFormat.parse(noSlotsDTO.getOriginStartTimeString());
						Date dStartDate = timeFormat.parse(destinationDTO.getStartTime());
						Date endDate = timeFormat.parse(noSlotsDTO.getOriginEndTimeString());
						Date dEndtDate = timeFormat.parse(destinationDTO.getEndTime());

						if (dStartDate.after(startDate) && dEndtDate.after(endDate)) {
							System.out.println("destination time is greater than start time");

							// can add this to origin list
							TimeTableDTO tempDTO = new TimeTableDTO();
							tempDTO = destinationDTO;
							tempDTO.setBusNo(noSlotsDTO.getBusNo());
							tempDTO.setDuplicateBusNum(noSlotsDTO.getBusNo());
							tempDTO.setTripType(noSlotsDTO.getTripType());
							tempDTO.setTripType(noSlotsDTO.getTripType());

							tempdestinationGroupTwoList.remove(destinationDTO);
							tempdestinationGroupTwoList.add(tempDTO);
							break;
						}
					}
				}
			}

			destinationGroupTwoList = new ArrayList<TimeTableDTO>();
			destinationGroupTwoList = tempdestinationGroupTwoList;

		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void groupThreeOriginList(String abbreviationOrigin) {
		originGroupThreeList = new ArrayList<TimeTableDTO>();
		originGroupThreeList = timeTableService.getDetailsOfFixedBuses(timeTableDTO, "3", "O"); // get
																								// time
																								// slots
																								// added
																								// and
																								// fixed
																								// buses

		int originPVTBus = panelGeneratorWithoutFixedTimeService
				.retrievePrivateBusNumbers(timeTableDTO.getGenereatedRefNo(), "3", "O");// get
																						// private
																						// bus
																						// count
		List<String> privateBusNumListO3 = new ArrayList<String>();
		for (int i = 0; i < originPVTBus; i++) {
			int suffix = i + 1;
			privateBusNumListO3.add(abbreviationOrigin + suffix);
		}

		List<String> alreadyAddedprivateBusNumListO3 = new ArrayList<String>();// already
																				// added
																				// private
																				// bus
																				// numbers
																				// in
																				// fixed
																				// bus
																				// list
		alreadyAddedprivateBusNumListO3 = panelGeneratorWithoutFixedTimeService
				.retreivePrivateBusNumbersInFixedBuses(timeTableDTO, "3", "O", abbreviationOrigin);

		int remainingBusCountO3 = privateBusNumListO3.size() - alreadyAddedprivateBusNumListO3.size();// remaining
																										// private
																										// bus
																										// count
																										// to
																										// add
																										// to
																										// main
																										// list

		boolean busNumFoundO3 = false;
		List<String> tempBusNumListO3 = new ArrayList<String>();
		// fixed private buses are not in ascending order so have to check which
		// bus numbers are in fixed private buses and what to add to final list
		for (String a : privateBusNumListO3) {
			busNumFoundO3 = false;
			for (String b : alreadyAddedprivateBusNumListO3) {
				if (a.equals(b)) {
					busNumFoundO3 = true;
				}
			}
			if (!busNumFoundO3) {
				tempBusNumListO3.add(a);
			}
		}

		List<String> finalPVTBusesO3 = new ArrayList<String>();// private buses
																// to add to
																// main list
		for (int i = 0; i < remainingBusCountO3; i++) {
			finalPVTBusesO3.add(tempBusNumListO3.get(i));
		}

		List<TimeTableDTO> tempOriginListThree = new ArrayList<TimeTableDTO>();
		int addBusCountO3 = 0;
		for (TimeTableDTO dto : originGroupThreeList) {
			if (dto.getBusNo() != null && !dto.getBusNo().isEmpty() && !dto.getBusNo().trim().equals("")) {
				tempOriginListThree.add(dto);
			} else {
				if (addBusCountO3 < finalPVTBusesO3.size()) {
					TimeTableDTO tempDto = new TimeTableDTO();
					tempDto = dto;
					tempDto.setBusNo(finalPVTBusesO3.get(addBusCountO3));
					tempDto.setDuplicateBusNum(finalPVTBusesO3.get(addBusCountO3));
					tempOriginListThree.add(tempDto);
					addBusCountO3 = addBusCountO3 + 1;
				} else {
					tempOriginListThree.add(dto);
				}
			}
		}

		originGroupThreeList = new ArrayList<TimeTableDTO>();
		originGroupThreeList = tempOriginListThree;

	}

	public void groupThreeDestination(String abbreviationDestination) {
		destinationGroupThreeList = new ArrayList<TimeTableDTO>();
		destinationGroupThreeList = timeTableService.getDetailsOfFixedBuses(timeTableDTO, "3", "D"); // get
																										// time
																										// slots
																										// added
																										// and
																										// fixed
																										// buses

		int destinationPVTBus3 = panelGeneratorWithoutFixedTimeService
				.retrievePrivateBusNumbers(timeTableDTO.getGenereatedRefNo(), "3", "D");// get
																						// private
																						// bus
																						// count
		List<String> privateBusNumListD3 = new ArrayList<String>();
		for (int i = 0; i < destinationPVTBus3; i++) {
			int suffix = i + 1;
			privateBusNumListD3.add(abbreviationDestination + suffix);
		}

		List<String> alreadyAddedprivateBusNumListD3 = new ArrayList<String>();// already
																				// added
																				// private
																				// bus
																				// numbers
																				// in
																				// fixed
																				// bus
																				// list
		alreadyAddedprivateBusNumListD3 = panelGeneratorWithoutFixedTimeService
				.retreivePrivateBusNumbersInFixedBuses(timeTableDTO, "3", "D", abbreviationDestination);

		int remainingBusCountD3 = privateBusNumListD3.size() - alreadyAddedprivateBusNumListD3.size();// remaining
																										// private
																										// bus
																										// count
																										// to
																										// add
																										// to
																										// main
																										// list

		boolean busNumFoundD3 = false;
		List<String> tempBusNumListD3 = new ArrayList<String>();
		// fixed private buses are not in ascending order so have to check which
		// bus numbers are in fixed private buses and what to add to final list
		for (String a : privateBusNumListD3) {
			busNumFoundD3 = false;
			for (String b : alreadyAddedprivateBusNumListD3) {
				if (a.equals(b)) {
					busNumFoundD3 = true;
				}
			}
			if (!busNumFoundD3) {
				tempBusNumListD3.add(a);
			}
		}

		List<String> finalPVTBusesD3 = new ArrayList<String>();// private buses
																// to add to
																// main list
		for (int i = 0; i < remainingBusCountD3; i++) {
			finalPVTBusesD3.add(tempBusNumListD3.get(i));
		}

		List<TimeTableDTO> tempDestinatinlistThree = new ArrayList<TimeTableDTO>();
		int addBusCountD3 = 0;
		for (TimeTableDTO dto : destinationGroupThreeList) {
			if (dto.getBusNo() != null && !dto.getBusNo().isEmpty() && !dto.getBusNo().trim().equals("")) {
				tempDestinatinlistThree.add(dto);
			} else {
				if (addBusCountD3 < finalPVTBusesD3.size()) {
					TimeTableDTO tempDto = new TimeTableDTO();
					tempDto = dto;
					tempDto.setBusNo(finalPVTBusesD3.get(addBusCountD3));
					tempDto.setDuplicateBusNum(finalPVTBusesD3.get(addBusCountD3));
					tempDestinatinlistThree.add(tempDto);
					addBusCountD3 = addBusCountD3 + 1;
				} else {
					tempDestinatinlistThree.add(dto);
				}
			}
		}

		destinationGroupThreeList = new ArrayList<TimeTableDTO>();
		destinationGroupThreeList = tempDestinatinlistThree;

	}

	public void groupThreeDestinationBusesToOrigin(String driverRestTime, String busRideTime) {

		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		List<TimeTableDTO> destinationListFromOrigin = new ArrayList<TimeTableDTO>();
		for (TimeTableDTO destinationDTO : destinationGroupThreeList) {
			destinationListFromOrigin.add(destinationDTO);
		}

		try {

			// driverRestTime
			// busRideTime

			for (TimeTableDTO destinationDTO : destinationGroupThreeList) {

				Date driverRestInDate = timeFormat.parse(driverRestTime);
				Date destinationEndTimeDate = timeFormat.parse(destinationDTO.getEndTime());
				long originStartTime = destinationEndTimeDate.getTime() + driverRestInDate.getTime();// destination
																										// bus
																										// end
																										// time
																										// +
																										// driver
																										// rest
																										// time
				String originStartTimeStr = timeFormat.format(new Date(originStartTime));

				Date originStartTimeDate = timeFormat.parse(originStartTimeStr);
				Date busRideTimeDate = timeFormat.parse(busRideTime);
				long originEndTime = originStartTimeDate.getTime() + busRideTimeDate.getTime();// origin
																								// start
																								// time
																								// +
																								// bus
																								// ride
																								// time
				String originEndTimeStr = timeFormat.format(new Date(originEndTime));

				TimeTableDTO tempDTO = new TimeTableDTO();
				tempDTO = destinationDTO;
				tempDTO.setOriginStartTimeString(originStartTimeStr);
				tempDTO.setOriginEndTimeString(originEndTimeStr);

				destinationListFromOrigin.remove(destinationDTO);
				destinationListFromOrigin.add(tempDTO);
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		/** create destination buses start end time from origin end **/

		/** add destination buses to origin list start **/
		// create temp origin list
		List<TimeTableDTO> tempOriginGroupThreeList = new ArrayList<TimeTableDTO>();
		for (TimeTableDTO originDTO : originGroupThreeList) {
			tempOriginGroupThreeList.add(originDTO);
		}

		boolean timeFound = false;
		List<TimeTableDTO> timeSlotNotFoundListD = new ArrayList<TimeTableDTO>();
		for (TimeTableDTO destinationDTO : destinationListFromOrigin) {
			timeFound = false;
			for (TimeTableDTO originDTO : originGroupThreeList) {
				if (destinationDTO.getOriginStartTimeString().equals(originDTO.getStartTime())
						&& destinationDTO.getOriginEndTimeString().equals(originDTO.getEndTime())) {
					if (originDTO.isFixedTime()) {
						timeSlotNotFoundListD.add(destinationDTO); // a fixed
																	// bus is
																	// already
																	// added to
																	// the time
																	// slot
						timeFound = true;
					} else if (originDTO.getBusNo() != null && !originDTO.getBusNo().isEmpty()
							&& !originDTO.getBusNo().trim().equals("")) {
						timeSlotNotFoundListD.add(destinationDTO); // a bus is
																	// added
																	// from
																	// origin
																	// and
																	// cannot
																	// put
																	// another
																	// to the
																	// time slot
						timeFound = true;
					} else {
						// can add this to origin list
						TimeTableDTO tempDTO = new TimeTableDTO();
						tempDTO = originDTO;
						tempDTO.setBusNo(destinationDTO.getBusNo());
						tempDTO.setDuplicateBusNum(destinationDTO.getBusNo());
						tempDTO.setTripType(destinationDTO.getTripType());
						tempDTO.setTripId(destinationDTO.getTripId());

						tempOriginGroupThreeList.remove(originDTO);
						tempOriginGroupThreeList.add(tempDTO);
						timeFound = true;
						break;

					}

				}
			}
			if (!timeFound) {
				if (destinationDTO.getBusNo() != null && !destinationDTO.getBusNo().isEmpty()) {
					timeSlotNotFoundListD.add(destinationDTO);
				}
			}
		}

		// find time slots for not added bus numbers from origin list
		try {
			// origin list order start
			Collections.sort(originGroupThreeList, new Comparator<TimeTableDTO>() {
				@Override
				public int compare(TimeTableDTO u1, TimeTableDTO u2) {
					// return u2.getStage().compareTo(u1.getStage());
					return Integer.valueOf(u2.getId()) - Integer.valueOf(u1.getId());
				}
			});

			Collections.reverse(originGroupThreeList);
			// origin list order end

			for (TimeTableDTO noSlotsDTO : timeSlotNotFoundListD) {
				for (TimeTableDTO originDTO : originGroupThreeList) {
					if (originDTO.getBusNo() == null || originDTO.getBusNo().isEmpty()) {

						Date startDate = timeFormat.parse(noSlotsDTO.getOriginStartTimeString());
						Date oStartDate = timeFormat.parse(originDTO.getStartTime());
						Date endDate = timeFormat.parse(noSlotsDTO.getOriginEndTimeString());
						Date oEndtDate = timeFormat.parse(originDTO.getEndTime());

						if (oStartDate.after(startDate) && oEndtDate.after(endDate)) {
							System.out.println("origin time is greater than start time");

							// can add this to origin list
							TimeTableDTO tempDTO = new TimeTableDTO();
							tempDTO = originDTO;
							tempDTO.setBusNo(noSlotsDTO.getBusNo());
							tempDTO.setDuplicateBusNum(noSlotsDTO.getBusNo());
							tempDTO.setTripType(noSlotsDTO.getTripType());
							tempDTO.setTripId(noSlotsDTO.getTripId());

							tempOriginGroupThreeList.remove(originDTO);
							tempOriginGroupThreeList.add(tempDTO);
							break;
						}

					}
				}
			}

			originGroupThreeList = new ArrayList<TimeTableDTO>();
			originGroupThreeList = tempOriginGroupThreeList;

		} catch (ParseException e) {
			e.printStackTrace();
		}
		/** add destination buses to origin list end **/
	}

	public void groupThreeOriginBusesToDestination(String driverRestTime, String busRideTime) {

		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		List<TimeTableDTO> originListFromDestination = new ArrayList<TimeTableDTO>();
		for (TimeTableDTO originDTO : originGroupThreeList) {
			originListFromDestination.add(originDTO);
		}

		try {

			// driverRestTime
			// busRideTime

			for (TimeTableDTO originDTO : originGroupThreeList) {

				Date driverRestInDate = timeFormat.parse(driverRestTime);
				Date originEndTimeDate = timeFormat.parse(originDTO.getEndTime());
				long destinationStartTime = originEndTimeDate.getTime() + driverRestInDate.getTime();// origin
																										// bus
																										// end
																										// time
																										// +
																										// driver
																										// rest
																										// time
				String destinationStartTimeStr = timeFormat.format(new Date(destinationStartTime));

				Date destinationStartTimeDate = timeFormat.parse(destinationStartTimeStr);
				Date busRideTimeDate = timeFormat.parse(busRideTime);
				long destinationEndTime = destinationStartTimeDate.getTime() + busRideTimeDate.getTime();// destination
																											// start
																											// time
																											// +
																											// bus
																											// ride
																											// time
				String destinationEndTimeStr = timeFormat.format(new Date(destinationEndTime));

				TimeTableDTO tempDTO = new TimeTableDTO();
				tempDTO = originDTO;
				tempDTO.setOriginStartTimeString(destinationStartTimeStr);
				tempDTO.setOriginEndTimeString(destinationEndTimeStr);

				originListFromDestination.remove(originDTO);
				originListFromDestination.add(tempDTO);
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		/** create origin buses start end time from destination end **/

		/** add origin buses to destination list start **/
		// create temp destinatino list
		List<TimeTableDTO> tempdestinationGroupThreeList = new ArrayList<TimeTableDTO>();
		for (TimeTableDTO originDTO : destinationGroupThreeList) {
			tempdestinationGroupThreeList.add(originDTO);
		}

		boolean timeFoundO = false;
		List<TimeTableDTO> timeSlotNotFoundListO = new ArrayList<TimeTableDTO>();
		for (TimeTableDTO originDTO : originListFromDestination) {
			timeFoundO = false;
			for (TimeTableDTO destinationDTO : destinationGroupThreeList) {
				if (originDTO.getTripType().equalsIgnoreCase("O")
						&& originDTO.getOriginStartTimeString().equals(destinationDTO.getStartTime())
						&& originDTO.getOriginEndTimeString().equals(destinationDTO.getEndTime())) {
					if (destinationDTO.isFixedTime()) {
						timeSlotNotFoundListO.add(originDTO); // a fixed bus is
																// already added
																// to the time
																// slot
						timeFoundO = true;
					} else if (destinationDTO.getBusNo() != null && !destinationDTO.getBusNo().isEmpty()
							&& !destinationDTO.getBusNo().trim().equals("")) {
						timeSlotNotFoundListO.add(originDTO);// a bus is added
																// from origin
																// and cannot
																// put another
																// to the time
																// slot
						timeFoundO = true;
					} else {
						// can add this to origin list
						TimeTableDTO tempDTO = new TimeTableDTO();
						tempDTO = destinationDTO;
						tempDTO.setBusNo(originDTO.getBusNo());
						tempDTO.setDuplicateBusNum(originDTO.getBusNo());
						tempDTO.setTripType(originDTO.getTripType());
						tempDTO.setTripId(originDTO.getTripId());

						tempdestinationGroupThreeList.remove(destinationDTO);
						tempdestinationGroupThreeList.add(tempDTO);
						timeFoundO = true;
						break;
					}
				}
			}
			if (!timeFoundO) {
				if (originDTO.getTripType().equalsIgnoreCase("O") && originDTO.getBusNo() != null
						&& !originDTO.getBusNo().isEmpty()) {
					timeSlotNotFoundListO.add(originDTO);
				}
			}
		}

		// find time slots for not added bus numbers from origin list
		try {
			// origin list order start
			Collections.sort(destinationGroupThreeList, new Comparator<TimeTableDTO>() {
				@Override
				public int compare(TimeTableDTO u1, TimeTableDTO u2) {
					// return u2.getStage().compareTo(u1.getStage());
					return Integer.valueOf(u2.getId()) - Integer.valueOf(u1.getId());
				}
			});

			Collections.reverse(destinationGroupThreeList);
			// origin list order end

			for (TimeTableDTO noSlotsDTO : timeSlotNotFoundListO) {
				for (TimeTableDTO destinationDTO : destinationGroupThreeList) {
					if (destinationDTO.getBusNo() == null || destinationDTO.getBusNo().isEmpty()) {
						Date startDate = timeFormat.parse(noSlotsDTO.getOriginStartTimeString());
						Date dStartDate = timeFormat.parse(destinationDTO.getStartTime());
						Date endDate = timeFormat.parse(noSlotsDTO.getOriginEndTimeString());
						Date dEndtDate = timeFormat.parse(destinationDTO.getEndTime());

						if (dStartDate.after(startDate) && dEndtDate.after(endDate)) {
							System.out.println("destination time is greater than start time");

							// can add this to origin list
							TimeTableDTO tempDTO = new TimeTableDTO();
							tempDTO = destinationDTO;
							tempDTO.setBusNo(noSlotsDTO.getBusNo());
							tempDTO.setDuplicateBusNum(noSlotsDTO.getBusNo());
							tempDTO.setTripType(noSlotsDTO.getTripType());
							tempDTO.setTripType(noSlotsDTO.getTripType());

							tempdestinationGroupThreeList.remove(destinationDTO);
							tempdestinationGroupThreeList.add(tempDTO);
							break;
						}
					}
				}
			}

			destinationGroupThreeList = new ArrayList<TimeTableDTO>();
			destinationGroupThreeList = tempdestinationGroupThreeList;

		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void checkRemainingTimeSlotsForDestinationDataGroupThree(String driverRestTime, String busRideTime) {
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		// check destination buses in origin list and verify whether they can
		// send to origin and take back in given time slots
		/*
		 * List<TimeTableDTO> listOriginTimes = new ArrayList<TimeTableDTO>();
		 * List<TimeTableDTO> listDestinationTimes = new ArrayList<TimeTableDTO>();
		 */

		Collections.sort(originGroupThreeList, new Comparator<TimeTableDTO>() {
			@Override
			public int compare(TimeTableDTO u1, TimeTableDTO u2) {
				// return u2.getStage().compareTo(u1.getStage());
				return Integer.valueOf(u2.getId()) - Integer.valueOf(u1.getId());
			}
		});

		Collections.reverse(originGroupThreeList);

		int coupleCount = originGroupThreeList.size() / 2;

		List<TimeTableDTO> dtoList = new ArrayList<TimeTableDTO>();
		for (int i = 0; i <= coupleCount; i++) {
			dtoList.add(originGroupThreeList.get(i));
		}

		List<TimeTableDTO> tempList1 = new ArrayList<TimeTableDTO>();
		List<TimeTableDTO> tempList2 = new ArrayList<TimeTableDTO>();
		int xxCount = coupleCount + 1;
		int yyCount = (xxCount / 2);
		boolean yyCountChange = false;
		for (int i = 0; i < coupleCount; i++) {
			// System.out.println(xxCount +"
			// "+originGroupOneList.get(xxCount).getStartTime() +" "+
			// originGroupOneList.get(xxCount).getEndTime());

			TimeTableDTO tempDto = new TimeTableDTO();

			/*
			 * tempDto.setBusNo(dtoList.get(i).getBusNo());
			 * tempDto.setDuplicateBusNum(dtoList.get(i).getBusNo());
			 */

			if (xxCount < originGroupThreeList.size()) {

				if (yyCount >= coupleCount + 1 || yyCountChange) {
					if (!yyCountChange) {
						yyCount = (coupleCount + 1) / 2;
						yyCountChange = true;
					}
					tempDto.setBusNo(originGroupThreeList.get(yyCount).getBusNo());
					tempDto.setDuplicateBusNum(originGroupThreeList.get(yyCount).getBusNo());
				} else {
					tempDto.setBusNo(destinationGroupThreeList.get(yyCount).getBusNo());
					tempDto.setDuplicateBusNum(destinationGroupThreeList.get(yyCount).getBusNo());
				}

				tempDto.setCtbBus(originGroupThreeList.get(xxCount).isCtbBus());
				tempDto.setEndTime(originGroupThreeList.get(xxCount).getEndTime());
				tempDto.setStartTime(originGroupThreeList.get(xxCount).getStartTime());
				tempDto.setId(originGroupThreeList.get(xxCount).getId());
				tempDto.setOriginEndTimeString(originGroupThreeList.get(xxCount).getOriginEndTimeString());
				tempDto.setOriginStartTimeString(originGroupThreeList.get(xxCount).getOriginStartTimeString());
				tempDto.setTempStartTime(originGroupThreeList.get(xxCount).getTempStartTime());
				tempDto.setTempEndTime(originGroupThreeList.get(xxCount).getTempEndTime());
				tempDto.setTimeTableDetSeq(originGroupThreeList.get(xxCount).getTimeTableDetSeq());
				tempDto.setTripId(originGroupThreeList.get(xxCount).getTripId());
				tempDto.setTripType(originGroupThreeList.get(xxCount).getTripType());
				tempList1.add(tempDto);

				tempList2.add(originGroupThreeList.get(xxCount));

				xxCount = xxCount + 1;
				yyCount = yyCount + 1;
			}
		}

		for (TimeTableDTO dto : tempList2) {
			originGroupThreeList.remove(dto);

		}

		for (TimeTableDTO dto : tempList1) {
			originGroupThreeList.add(dto);

		}

	}

	public void checkRemainingTimeSlotsForOriginDataGroupThree(String driverRestTime, String busRideTime) {

		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		// check origin buses in destination list and verify whether they can
		// send to destination and take back in given time slots
		/*
		 * List<TimeTableDTO> listOriginTimes = new ArrayList<TimeTableDTO>();
		 * List<TimeTableDTO> listDestinationTimes = new ArrayList<TimeTableDTO>();
		 */

		Collections.sort(destinationGroupThreeList, new Comparator<TimeTableDTO>() {
			@Override
			public int compare(TimeTableDTO u1, TimeTableDTO u2) {
				// return u2.getStage().compareTo(u1.getStage());
				return Integer.valueOf(u2.getId()) - Integer.valueOf(u1.getId());
			}
		});

		Collections.reverse(destinationGroupThreeList);

		int coupleCount = destinationGroupThreeList.size() / 2;

		List<TimeTableDTO> dtoList = new ArrayList<TimeTableDTO>();
		for (int i = 0; i <= coupleCount; i++) {
			dtoList.add(destinationGroupThreeList.get(i));
		}

		List<TimeTableDTO> tempList1 = new ArrayList<TimeTableDTO>();
		List<TimeTableDTO> tempList2 = new ArrayList<TimeTableDTO>();
		int xxCount = coupleCount + 1;
		int yyCount = (xxCount / 2);
		boolean yyCountChange = false;

		for (int i = 0; i < coupleCount; i++) {
			// System.out.println(xxCount +"
			// "+originGroupOneList.get(xxCount).getStartTime() +" "+
			// originGroupOneList.get(xxCount).getEndTime());

			TimeTableDTO tempDto = new TimeTableDTO();

			/*
			 * tempDto.setBusNo(dtoList.get(i).getBusNo());
			 * tempDto.setDuplicateBusNum(dtoList.get(i).getBusNo());
			 */

			if (xxCount < destinationGroupThreeList.size()) {

				if (yyCount >= coupleCount || yyCountChange) {
					if (!yyCountChange) {
						yyCount = (coupleCount + 1) / 2;
						yyCountChange = true;
					}
					tempDto.setBusNo(destinationGroupThreeList.get(yyCount).getBusNo());
					tempDto.setDuplicateBusNum(destinationGroupThreeList.get(yyCount).getBusNo());
				} else {
					tempDto.setBusNo(originGroupThreeList.get(yyCount).getBusNo());
					tempDto.setDuplicateBusNum(originGroupThreeList.get(yyCount).getBusNo());
				}

				tempDto.setCtbBus(destinationGroupThreeList.get(xxCount).isCtbBus());
				tempDto.setEndTime(destinationGroupThreeList.get(xxCount).getEndTime());
				tempDto.setStartTime(destinationGroupThreeList.get(xxCount).getStartTime());
				tempDto.setId(destinationGroupThreeList.get(xxCount).getId());
				tempDto.setOriginEndTimeString(destinationGroupThreeList.get(xxCount).getOriginEndTimeString());
				tempDto.setOriginStartTimeString(destinationGroupThreeList.get(xxCount).getOriginStartTimeString());
				tempDto.setTempStartTime(destinationGroupThreeList.get(xxCount).getTempStartTime());
				tempDto.setTempEndTime(destinationGroupThreeList.get(xxCount).getTempEndTime());
				tempDto.setTimeTableDetSeq(destinationGroupThreeList.get(xxCount).getTimeTableDetSeq());
				tempDto.setTripId(destinationGroupThreeList.get(xxCount).getTripId());
				tempDto.setTripType(destinationGroupThreeList.get(xxCount).getTripType());
				tempList1.add(tempDto);

				tempList2.add(destinationGroupThreeList.get(xxCount));
				/*
				 * System.out.println(xxCount +" "+tempDto.getStartTime() +" "+
				 * tempDto.getEndTime()); System.out.println(tempDto.getStartTime() +" *** "+
				 * tempDto.getEndTime()); System.out.println(originGroupOneList.get(xxCount).
				 * getStartTime() +" ### "+ originGroupOneList.get(xxCount).getEndTime());
				 */

				xxCount = xxCount + 1;
				yyCount = yyCount + 1;
			}
		}

		for (TimeTableDTO dto : tempList2) {
			destinationGroupThreeList.remove(dto);

		}

		for (TimeTableDTO dto : tempList1) {
			destinationGroupThreeList.add(dto);

		}

	}

	public void checkRemainingTimeSlotsForDestinationData(String driverRestTime, String busRideTime) {
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		// check destination buses in origin list and verify whether they can
		// send to origin and take back in given time slots
		/*
		 * List<TimeTableDTO> listOriginTimes = new ArrayList<TimeTableDTO>();
		 * List<TimeTableDTO> listDestinationTimes = new ArrayList<TimeTableDTO>();
		 */

		Collections.sort(originGroupOneList, new Comparator<TimeTableDTO>() {
			@Override
			public int compare(TimeTableDTO u1, TimeTableDTO u2) {
				// return u2.getStage().compareTo(u1.getStage());
				return Integer.valueOf(u2.getId()) - Integer.valueOf(u1.getId());
			}
		});

		Collections.reverse(originGroupOneList);

		int coupleCount = originGroupOneList.size() / 2;

		List<TimeTableDTO> dtoList = new ArrayList<TimeTableDTO>();
		for (int i = 0; i <= coupleCount; i++) {
			dtoList.add(originGroupOneList.get(i));
		}

		List<TimeTableDTO> tempList1 = new ArrayList<TimeTableDTO>();
		List<TimeTableDTO> tempList2 = new ArrayList<TimeTableDTO>();
		int xxCount = coupleCount + 1;
		int yyCount = (xxCount / 2);
		boolean yyCountChange = false;
		for (int i = 0; i < coupleCount; i++) {
			// System.out.println(xxCount +"
			// "+originGroupOneList.get(xxCount).getStartTime() +" "+
			// originGroupOneList.get(xxCount).getEndTime());

			TimeTableDTO tempDto = new TimeTableDTO();

			/*
			 * tempDto.setBusNo(dtoList.get(i).getBusNo());
			 * tempDto.setDuplicateBusNum(dtoList.get(i).getBusNo());
			 */

			if (xxCount < originGroupOneList.size()) {

				if (yyCount >= coupleCount + 1 || yyCountChange) {
					if (!yyCountChange) {
						yyCount = (coupleCount + 1) / 2;
						yyCountChange = true;
					}
					tempDto.setBusNo(originGroupOneList.get(yyCount).getBusNo());
					tempDto.setDuplicateBusNum(originGroupOneList.get(yyCount).getBusNo());
				} else {
					tempDto.setBusNo(destinationGroupOneList.get(yyCount).getBusNo());
					tempDto.setDuplicateBusNum(destinationGroupOneList.get(yyCount).getBusNo());
				}

				tempDto.setCtbBus(originGroupOneList.get(xxCount).isCtbBus());
				tempDto.setEndTime(originGroupOneList.get(xxCount).getEndTime());
				tempDto.setStartTime(originGroupOneList.get(xxCount).getStartTime());
				tempDto.setId(originGroupOneList.get(xxCount).getId());
				tempDto.setOriginEndTimeString(originGroupOneList.get(xxCount).getOriginEndTimeString());
				tempDto.setOriginStartTimeString(originGroupOneList.get(xxCount).getOriginStartTimeString());
				tempDto.setTempStartTime(originGroupOneList.get(xxCount).getTempStartTime());
				tempDto.setTempEndTime(originGroupOneList.get(xxCount).getTempEndTime());
				tempDto.setTimeTableDetSeq(originGroupOneList.get(xxCount).getTimeTableDetSeq());
				tempDto.setTripId(originGroupOneList.get(xxCount).getTripId());
				tempDto.setTripType(originGroupOneList.get(xxCount).getTripType());
				tempList1.add(tempDto);

				tempList2.add(originGroupOneList.get(xxCount));
				/*
				 * System.out.println(xxCount +" "+tempDto.getStartTime() +" "+
				 * tempDto.getEndTime()); System.out.println(tempDto.getStartTime() +" *** "+
				 * tempDto.getEndTime()); System.out.println(originGroupOneList.get(xxCount).
				 * getStartTime() +" ### "+ originGroupOneList.get(xxCount).getEndTime());
				 */

				xxCount = xxCount + 1;
				yyCount = yyCount + 1;
			}

		}

		for (TimeTableDTO dto : tempList2) {
			originGroupOneList.remove(dto);

		}

		for (TimeTableDTO dto : tempList1) {
			originGroupOneList.add(dto);

		}

	}

	public void checkRemainingTimeSlotsForOriginData(String driverRestTime, String busRideTime) {

		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		// check origin buses in destination list and verify whether they can
		// send to destination and take back in given time slots
		/*
		 * List<TimeTableDTO> listOriginTimes = new ArrayList<TimeTableDTO>();
		 * List<TimeTableDTO> listDestinationTimes = new ArrayList<TimeTableDTO>();
		 */

		Collections.sort(destinationGroupOneList, new Comparator<TimeTableDTO>() {
			@Override
			public int compare(TimeTableDTO u1, TimeTableDTO u2) {
				// return u2.getStage().compareTo(u1.getStage());
				return Integer.valueOf(u2.getId()) - Integer.valueOf(u1.getId());
			}
		});

		Collections.reverse(destinationGroupOneList);

		int coupleCount = destinationGroupOneList.size() / 2;

		List<TimeTableDTO> dtoList = new ArrayList<TimeTableDTO>();
		for (int i = 0; i <= coupleCount; i++) {
			dtoList.add(destinationGroupOneList.get(i));
		}

		List<TimeTableDTO> tempList1 = new ArrayList<TimeTableDTO>();
		List<TimeTableDTO> tempList2 = new ArrayList<TimeTableDTO>();
		int xxCount = coupleCount + 1;
		int yyCount = (xxCount / 2);
		boolean yyCountChange = false;
		for (int i = 0; i < coupleCount; i++) {
			// System.out.println(xxCount +"
			// "+originGroupOneList.get(xxCount).getStartTime() +" "+
			// originGroupOneList.get(xxCount).getEndTime());

			TimeTableDTO tempDto = new TimeTableDTO();

			/*
			 * tempDto.setBusNo(dtoList.get(i).getBusNo());
			 * tempDto.setDuplicateBusNum(dtoList.get(i).getBusNo());
			 */

			if (xxCount < destinationGroupOneList.size()) {

				if (yyCount >= coupleCount || yyCountChange) {
					if (!yyCountChange) {
						yyCount = (coupleCount + 1) / 2;
						yyCountChange = true;
					}
					tempDto.setBusNo(destinationGroupOneList.get(yyCount).getBusNo());
					tempDto.setDuplicateBusNum(destinationGroupOneList.get(yyCount).getBusNo());
				} else {
					tempDto.setBusNo(originGroupOneList.get(yyCount).getBusNo());
					tempDto.setDuplicateBusNum(originGroupOneList.get(yyCount).getBusNo());
				}

				tempDto.setCtbBus(destinationGroupOneList.get(xxCount).isCtbBus());
				tempDto.setEndTime(destinationGroupOneList.get(xxCount).getEndTime());
				tempDto.setStartTime(destinationGroupOneList.get(xxCount).getStartTime());
				tempDto.setId(destinationGroupOneList.get(xxCount).getId());
				tempDto.setOriginEndTimeString(destinationGroupOneList.get(xxCount).getOriginEndTimeString());
				tempDto.setOriginStartTimeString(destinationGroupOneList.get(xxCount).getOriginStartTimeString());
				tempDto.setTempStartTime(destinationGroupOneList.get(xxCount).getTempStartTime());
				tempDto.setTempEndTime(destinationGroupOneList.get(xxCount).getTempEndTime());
				tempDto.setTimeTableDetSeq(destinationGroupOneList.get(xxCount).getTimeTableDetSeq());
				tempDto.setTripId(destinationGroupOneList.get(xxCount).getTripId());
				tempDto.setTripType(destinationGroupOneList.get(xxCount).getTripType());
				tempList1.add(tempDto);

				tempList2.add(destinationGroupOneList.get(xxCount));
				/*
				 * System.out.println(xxCount +" "+tempDto.getStartTime() +" "+
				 * tempDto.getEndTime()); System.out.println(tempDto.getStartTime() +" *** "+
				 * tempDto.getEndTime()); System.out.println(originGroupOneList.get(xxCount).
				 * getStartTime() +" ### "+ originGroupOneList.get(xxCount).getEndTime());
				 */

				xxCount = xxCount + 1;
				yyCount = yyCount + 1;
			}
		}

		for (TimeTableDTO dto : tempList2) {
			destinationGroupOneList.remove(dto);

		}

		for (TimeTableDTO dto : tempList1) {
			destinationGroupOneList.add(dto);

		}

	}

	public void checkRemainingTimeSlotsForDestinationDataGroupTwo(String driverRestTime, String busRideTime) {
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		// check destination buses in origin list and verify whether they can
		// send to origin and take back in given time slots
		/*
		 * List<TimeTableDTO> listOriginTimes = new ArrayList<TimeTableDTO>();
		 * List<TimeTableDTO> listDestinationTimes = new ArrayList<TimeTableDTO>();
		 */

		Collections.sort(originGroupTwoList, new Comparator<TimeTableDTO>() {
			@Override
			public int compare(TimeTableDTO u1, TimeTableDTO u2) {
				// return u2.getStage().compareTo(u1.getStage());
				return Integer.valueOf(u2.getId()) - Integer.valueOf(u1.getId());
			}
		});

		Collections.reverse(originGroupTwoList);

		int coupleCount = originGroupTwoList.size() / 2;

		List<TimeTableDTO> dtoList = new ArrayList<TimeTableDTO>();
		for (int i = 0; i <= coupleCount; i++) {
			dtoList.add(originGroupTwoList.get(i));
		}

		List<TimeTableDTO> tempList1 = new ArrayList<TimeTableDTO>();
		List<TimeTableDTO> tempList2 = new ArrayList<TimeTableDTO>();
		int xxCount = coupleCount + 1;
		int yyCount = (xxCount / 2);
		boolean yyCountChange = false;

		for (int i = 0; i < coupleCount; i++) {
			// System.out.println(xxCount +"
			// "+originGroupOneList.get(xxCount).getStartTime() +" "+
			// originGroupOneList.get(xxCount).getEndTime());

			TimeTableDTO tempDto = new TimeTableDTO();

			/*
			 * tempDto.setBusNo(dtoList.get(i).getBusNo());
			 * tempDto.setDuplicateBusNum(dtoList.get(i).getBusNo());
			 */
			if (xxCount < originGroupTwoList.size()) {

				if (yyCount >= coupleCount + 1 || yyCountChange) {
					if (!yyCountChange) {
						yyCount = (coupleCount + 1) / 2;
						yyCountChange = true;
					}
					tempDto.setBusNo(originGroupTwoList.get(yyCount).getBusNo());
					tempDto.setDuplicateBusNum(originGroupTwoList.get(yyCount).getBusNo());
				} else {
					tempDto.setBusNo(destinationGroupTwoList.get(yyCount).getBusNo());
					tempDto.setDuplicateBusNum(destinationGroupTwoList.get(yyCount).getBusNo());
				}

				tempDto.setCtbBus(originGroupTwoList.get(xxCount).isCtbBus());
				tempDto.setEndTime(originGroupTwoList.get(xxCount).getEndTime());
				tempDto.setStartTime(originGroupTwoList.get(xxCount).getStartTime());
				tempDto.setId(originGroupTwoList.get(xxCount).getId());
				tempDto.setOriginEndTimeString(originGroupTwoList.get(xxCount).getOriginEndTimeString());
				tempDto.setOriginStartTimeString(originGroupTwoList.get(xxCount).getOriginStartTimeString());
				tempDto.setTempStartTime(originGroupTwoList.get(xxCount).getTempStartTime());
				tempDto.setTempEndTime(originGroupTwoList.get(xxCount).getTempEndTime());
				tempDto.setTimeTableDetSeq(originGroupTwoList.get(xxCount).getTimeTableDetSeq());
				tempDto.setTripId(originGroupTwoList.get(xxCount).getTripId());
				tempDto.setTripType(originGroupTwoList.get(xxCount).getTripType());
				tempList1.add(tempDto);

				tempList2.add(originGroupTwoList.get(xxCount));

				xxCount = xxCount + 1;
				yyCount = yyCount + 1;
			}
		}

		for (TimeTableDTO dto : tempList2) {
			originGroupTwoList.remove(dto);
			System.out.println(dto.getStartTime() + " *** " + dto.getEndTime());
		}

		for (TimeTableDTO dto : tempList1) {
			originGroupTwoList.add(dto);
			System.out.println(dto.getStartTime() + " ### " + dto.getEndTime());
		}

	}

	public void checkRemainingTimeSlotsForOriginDataGroupTwo(String driverRestTime, String busRideTime) {

		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		// check origin buses in destination list and verify whether they can
		// send to destination and take back in given time slots
		/*
		 * List<TimeTableDTO> listOriginTimes = new ArrayList<TimeTableDTO>();
		 * List<TimeTableDTO> listDestinationTimes = new ArrayList<TimeTableDTO>();
		 */

		Collections.sort(destinationGroupTwoList, new Comparator<TimeTableDTO>() {
			@Override
			public int compare(TimeTableDTO u1, TimeTableDTO u2) {
				// return u2.getStage().compareTo(u1.getStage());
				return Integer.valueOf(u2.getId()) - Integer.valueOf(u1.getId());
			}
		});

		Collections.reverse(destinationGroupTwoList);

		int coupleCount = destinationGroupTwoList.size() / 2;

		List<TimeTableDTO> dtoList = new ArrayList<TimeTableDTO>();
		for (int i = 0; i <= coupleCount; i++) {
			dtoList.add(destinationGroupTwoList.get(i));
		}

		List<TimeTableDTO> tempList1 = new ArrayList<TimeTableDTO>();
		List<TimeTableDTO> tempList2 = new ArrayList<TimeTableDTO>();
		int xxCount = coupleCount + 1;
		int yyCount = (xxCount / 2);
		boolean yyCountChange = false;
		for (int i = 0; i < coupleCount; i++) {
			// System.out.println(xxCount +"
			// "+originGroupOneList.get(xxCount).getStartTime() +" "+
			// originGroupOneList.get(xxCount).getEndTime());

			TimeTableDTO tempDto = new TimeTableDTO();

			/*
			 * tempDto.setBusNo(dtoList.get(i).getBusNo());
			 * tempDto.setDuplicateBusNum(dtoList.get(i).getBusNo());
			 */

			if (xxCount < destinationGroupTwoList.size()) {

				if (yyCount >= coupleCount || yyCountChange) {
					if (!yyCountChange) {
						yyCount = (coupleCount + 1) / 2;
						yyCountChange = true;
					}
					tempDto.setBusNo(destinationGroupTwoList.get(yyCount).getBusNo());
					tempDto.setDuplicateBusNum(destinationGroupTwoList.get(yyCount).getBusNo());
				} else {
					tempDto.setBusNo(originGroupTwoList.get(yyCount).getBusNo());
					tempDto.setDuplicateBusNum(originGroupTwoList.get(yyCount).getBusNo());
				}

				tempDto.setCtbBus(destinationGroupTwoList.get(xxCount).isCtbBus());
				tempDto.setEndTime(destinationGroupTwoList.get(xxCount).getEndTime());
				tempDto.setStartTime(destinationGroupTwoList.get(xxCount).getStartTime());
				tempDto.setId(destinationGroupTwoList.get(xxCount).getId());
				tempDto.setOriginEndTimeString(destinationGroupTwoList.get(xxCount).getOriginEndTimeString());
				tempDto.setOriginStartTimeString(destinationGroupTwoList.get(xxCount).getOriginStartTimeString());
				tempDto.setTempStartTime(destinationGroupTwoList.get(xxCount).getTempStartTime());
				tempDto.setTempEndTime(destinationGroupTwoList.get(xxCount).getTempEndTime());
				tempDto.setTimeTableDetSeq(destinationGroupTwoList.get(xxCount).getTimeTableDetSeq());
				tempDto.setTripId(destinationGroupTwoList.get(xxCount).getTripId());
				tempDto.setTripType(destinationGroupTwoList.get(xxCount).getTripType());
				tempList1.add(tempDto);

				tempList2.add(destinationGroupTwoList.get(xxCount));

				xxCount = xxCount + 1;
				yyCount = yyCount + 1;
			}
		}

		for (TimeTableDTO dto : tempList2) {
			destinationGroupTwoList.remove(dto);
			System.out.println(dto.getStartTime() + " *** " + dto.getEndTime());
		}

		for (TimeTableDTO dto : tempList1) {
			destinationGroupTwoList.add(dto);
			System.out.println(dto.getStartTime() + " ### " + dto.getEndTime());
		}

	}

	public List<TimeTableDTO> timeRangeManager(long begin, long end, long interval) {

		/* Store String values of every time */
		List<String> valueList = intervalCalculator(begin, end, interval);

		List<TimeTableDTO> commonTypeList = new ArrayList<>();

		for (int i = 0; i < valueList.size(); i++) {

			/* Validation For Index Out of */
			if (i != valueList.size() - 1) {

				String x = valueList.get(i).toString();
				String y = valueList.get(i + 1).toString();

				TimeTableDTO v = new TimeTableDTO(String.valueOf(i + 1), x, y, 0, 0);
				commonTypeList.add(v);
			}

		}

		return commonTypeList;
	}

	public boolean validateBusAndRecreateGroupOneLists(String abbreviationOrigin, String abbreviationDestination) {
		WithoutFixedTimeValidationDTO validationDto = new WithoutFixedTimeValidationDTO();
		validationDto = validateBusCountGroupOne();
		boolean leaveHaveD = false;
		boolean leaveHaveO = false;
		if (validationDto != null && !validationDto.isSuccess()) {
			if (validationDto.getError() != null && !validationDto.getError().isEmpty()
					&& !validationDto.getError().trim().equals("")) {
				/**
				 * need to check bus num according to ref number in time table det if bus num
				 * not null can not goto this by tharushi.e
				 */

				boolean checkLeave = panelGeneratorWithoutFixedTimeService
						.checkLeavesOnPanelRefNo(timeTableDTO.getGenereatedRefNo(), "3");

				if (!checkLeave) {
					panelGeneratorWithoutFixedTimeService.updateLeaves(timeTableDTO.getGenereatedRefNo(), "1", 0, 0,
							sessionBackingBean.getLoginUser());
					panelGeneratorWithoutFixedTimeService.updateLeaves(timeTableDTO.getGenereatedRefNo(), "2", 0, 0,
							sessionBackingBean.getLoginUser());
					panelGeneratorWithoutFixedTimeService.updateLeaves(timeTableDTO.getGenereatedRefNo(), "3", 0, 0,
							sessionBackingBean.getLoginUser());
					
					sessionBackingBean.setMessage(validationDto.getError());
					RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
					return false;
				}
			}
			if (validationDto.getNoOfLeavesOrigin() > 0) {
				// get non fixed destination buses and remove this amount of
				// buses from destination list
				List<TimeTableDTO> tempdtoList = new ArrayList<TimeTableDTO>();
				List<TimeTableDTO> pvtList = new ArrayList<TimeTableDTO>();
				for (TimeTableDTO dto : originGroupOneList) {
					tempdtoList.add(dto);
					if (dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& (dto.getBusNo().substring(0, abbreviationOrigin.length())).equals(abbreviationOrigin)
							&& !dto.isFixedTime()) {
						pvtList.add(dto);
					}
				}
				// check tharushi.e keep only fixedtime changed1 for etc issue

				/*
				 * for (TimeTableDTO dto : originGroupOneList) { tempdtoList.add(dto); if
				 * (dto.getBusNo() != null && !dto.getBusNo().isEmpty() //&&
				 * (dto.getBusNo().substring(0,
				 * abbreviationOrigin.length())).equals(abbreviationOrigin) //&&
				 * !dto.isFixedTime() ) { pvtList.add(dto); } }
				 */
				int keepPVTBuses = pvtList.size() - validationDto.getNoOfLeavesOrigin();
				List<TimeTableDTO> pvtListRemove = new ArrayList<TimeTableDTO>();
				int count = 0;
				// count

				// fixed
				if (coupling && validationDto.getNoOfLeavesOrigin() > 0) {
					leaveFromOrigin = true;
					for (TimeTableDTO dto : pvtList) {
						if (count >= keepPVTBuses) {
							pvtListRemove.add(pvtList.get(count));
						}
						count = count + 1;
					}
					for (TimeTableDTO o : pvtListRemove) {
						removedBusesFromOrgin.add(o.getBusNo());
					}

				} else {
					if (validationDto.getAllBusesDestination() != 0) {
						for (TimeTableDTO dto : pvtList) {
							if (count >= keepPVTBuses) {
								pvtListRemove.add(pvtList.get(count));
							}
							count = count + 1;
						}
					}
				}

				for (TimeTableDTO dto : originGroupOneList) {
					for (TimeTableDTO dto2 : pvtListRemove) {
						if (dto.getBusNo() != null && !dto.getBusNo().isEmpty() && dto2.getBusNo() != null
								&& !dto2.getBusNo().isEmpty() && dto.getBusNo().equalsIgnoreCase(dto2.getBusNo())) {
							TimeTableDTO tempDTO = new TimeTableDTO();
							tempDTO = dto;
							tempDTO.setBusNo(null);
							tempDTO.setFixedTime(false);
							tempdtoList.remove(dto);
							tempdtoList.add(tempDTO);
						}
					}
				}

				originGroupOneList = new ArrayList<TimeTableDTO>();
				originGroupOneList = tempdtoList;

			}
			if (validationDto.getNoOfLeavesDestination() > 0) {
				// get non fixed origin buses and remove this amount of buses
				// from destination list
				List<TimeTableDTO> tempdtoList = new ArrayList<TimeTableDTO>();
				List<TimeTableDTO> pvtList = new ArrayList<TimeTableDTO>();
				for (TimeTableDTO dto : destinationGroupOneList) {
					tempdtoList.add(dto);
					if (dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& (dto.getBusNo().substring(0, abbreviationDestination.length()))
									.equals(abbreviationDestination)
							&& !dto.isFixedTime()) {
						pvtList.add(dto);
					}
				}
				// check tharushi.e keep only fixedtime changed2 for etc issue

				/*
				 * for (TimeTableDTO dto : destinationGroupOneList) { tempdtoList.add(dto); if
				 * (dto.getBusNo() != null && !dto.getBusNo().isEmpty()) { //&&
				 * (dto.getBusNo().substring(0, abbreviationDestination.length()))
				 * //.equals(abbreviationDestination)) { //&& !dto.isFixedTime()) {//check
				 * tharushi.e keep only fixedtime changed2 for etc issue pvtList.add(dto); } }
				 */
				int keepPVTBuses = pvtList.size() - validationDto.getNoOfLeavesDestination();
				List<TimeTableDTO> pvtListRemove = new ArrayList<TimeTableDTO>();
				int count = 0;

				if (coupling && validationDto.getNoOfLeavesDestination() > 0) {
					leaveFromDestination = true;
					for (TimeTableDTO dto : pvtList) {
						if (count >= keepPVTBuses) {
							pvtListRemove.add(pvtList.get(count));
						}
						count = count + 1;
					}

					for (TimeTableDTO o : pvtListRemove) {
						removedBusesFromDestination.add(o.getBusNo());
					}

				} else {
					if (validationDto.getAllbusesOrigin() != 0) {
						for (TimeTableDTO dto : pvtList) {
							if (count >= keepPVTBuses) {
								pvtListRemove.add(pvtList.get(count));
							}
							count = count + 1;
						}
					}
				}
				for (TimeTableDTO dto : destinationGroupOneList) {
					for (TimeTableDTO dto2 : pvtListRemove) {
						if (dto.getBusNo() != null && !dto.getBusNo().isEmpty() && dto2.getBusNo() != null
								&& !dto2.getBusNo().isEmpty() && dto.getBusNo().equalsIgnoreCase(dto2.getBusNo())) {
							TimeTableDTO tempDTO = new TimeTableDTO();
							tempDTO = dto;
							tempDTO.setBusNo(null);
							tempDTO.setFixedTime(false);
							tempdtoList.remove(dto);
							tempdtoList.add(tempDTO);
						}
					}
				}

				destinationGroupOneList = new ArrayList<TimeTableDTO>();
				destinationGroupOneList = tempdtoList;

				// update leave bus count in DB
			}
		}

		return true;
	}

	public WithoutFixedTimeValidationDTO validateBusCountGroupOne() {

		WithoutFixedTimeValidationDTO validationDto = new WithoutFixedTimeValidationDTO();

		// check whether time slots are equal
		if (originGroupOneList.size() != destinationGroupOneList.size()) {
			validationDto = new WithoutFixedTimeValidationDTO();
			validationDto.setError("Time slots are not equal - Group One");
			return validationDto;
		}

		// origin
		int originPVTBus = panelGeneratorWithoutFixedTimeService
				.retrievePrivateBusNumbers(timeTableDTO.getGenereatedRefNo(), "1", "O");// get
																						// private
																						// bus
																						// count
		int originfixedPVTbuses = panelGeneratorWithoutFixedTimeService
				.retrieveFixedBusNumbers(timeTableDTO.getGenereatedRefNo(), "1", "O", "Y");// get
																							// fixed
																							// private
																							// bus
																							// count
		int originfixedCTBbuses = panelGeneratorWithoutFixedTimeService
				.retrieveFixedBusNumbers(timeTableDTO.getGenereatedRefNo(), "1", "O", "N");// get
																							// fixed
		/** drop D type **/ // CTB
		// bus
		// count

		if (coupling) {

			originfixedCTBbuses = originfixedCTBbuses / 2;
			originfixedPVTbuses = originfixedPVTbuses / 2;

		}

		int remaininPVTBusOrigin = originPVTBus - originfixedPVTbuses;
		int allBusesOrigin = originPVTBus + originfixedCTBbuses; // count not 9 its 7
		// origin

		// destination
		int destinaionPVTBus = panelGeneratorWithoutFixedTimeService
				.retrievePrivateBusNumbers(timeTableDTO.getGenereatedRefNo(), "1", "D");// get
																						// private
																						// bus
																						// count
		int destinationfixedPVTbuses = panelGeneratorWithoutFixedTimeService
				.retrieveFixedBusNumbers(timeTableDTO.getGenereatedRefNo(), "1", "D", "Y");// get
																							// fixed
																							// private
																							// bus
																							// count
		int destinationfixedCTBbuses = panelGeneratorWithoutFixedTimeService
				.retrieveFixedBusNumbers(timeTableDTO.getGenereatedRefNo(), "1", "D", "N");// get
																							// fixed
																							// CTB
																							// bus
																							// count

		if (coupling) {

			destinationfixedCTBbuses = destinationfixedCTBbuses / 2;
			destinationfixedPVTbuses = destinationfixedPVTbuses / 2;

		}

		int remaininPVTBusDestination = destinaionPVTBus - destinationfixedPVTbuses;
		int allBusesDestinaion = destinaionPVTBus + destinationfixedCTBbuses;
		// destination

		int allBuses = allBusesOrigin + allBusesDestinaion;
		int allFixedBuses = originfixedPVTbuses + originfixedCTBbuses + destinationfixedPVTbuses
				+ destinationfixedCTBbuses;

		// if coupling
		if (coupling) {

			int couplingTimeSlots = originGroupOneList.size() / 2;

			if (allBuses != couplingTimeSlots) {
				int noOfLeaves = (((allBusesOrigin * 2) + (allBusesDestinaion * 2)) - originGroupOneList.size()) / 2;
				if (noOfLeaves < 0) {
					// cannot give minus leaves
					validationDto = new WithoutFixedTimeValidationDTO();
					validationDto.setError(
							"cannot perform the operation please check time slots and bus numbers - Group One");
					return validationDto;
				} else {
					// check how to give leaves
					validationDto = new WithoutFixedTimeValidationDTO();

					double originLeaves = (((double) allBusesOrigin
							- ((double) originfixedPVTbuses + (double) originfixedCTBbuses))
							/ ((double) allBuses - (double) allFixedBuses)) * (double) noOfLeaves;
					double destinationLeaves = (((double) allBusesDestinaion
							- ((double) destinationfixedPVTbuses + (double) destinationfixedCTBbuses))
							/ ((double) allBuses - (double) allFixedBuses)) * (double) noOfLeaves;
					;

					BigDecimal bdOrigin = new BigDecimal(originLeaves).setScale(1, RoundingMode.HALF_UP);
					double newInputOrigin = bdOrigin.doubleValue();
					int intInputOrigin = (int) Math.round(newInputOrigin);
					BigDecimal bdDestination = new BigDecimal(destinationLeaves).setScale(1, RoundingMode.HALF_UP);
					double newInputDestinaion = bdDestination.doubleValue();
					int intInputDestinaion = (int) Math.round(newInputDestinaion);

					int tempLeaves = (int) Math.round(newInputOrigin) + (int) Math.round(newInputDestinaion);
					if (tempLeaves < noOfLeaves) {
						int remainLeaves = noOfLeaves - tempLeaves;
						if (intInputOrigin < intInputDestinaion) {

							// check whether origin can give this amount of
							// leaves
							if (remaininPVTBusOrigin >= intInputOrigin) {
								// can give leaves
								intInputOrigin = intInputOrigin + remainLeaves;
							}

						} else if (intInputDestinaion < intInputOrigin) {

							// check whether destination can give this amount of
							// leaves
							if (remaininPVTBusDestination >= intInputDestinaion) {
								// can give leaves
								intInputDestinaion = intInputDestinaion + remainLeaves;
							}
						} else if ((intInputOrigin == 0 && remainLeaves > 0)
								|| (intInputDestinaion == 0 && remainLeaves > 0)) {
							if (newInputOrigin > newInputDestinaion) {
								// check whether origin can give this amount of
								// leaves
								if (remaininPVTBusOrigin >= intInputOrigin) {
									intInputOrigin = intInputOrigin + remainLeaves;
								} else if (remaininPVTBusDestination >= intInputDestinaion) {
									// if origin non fixed PVT buses are not
									// enough to give leaves get leaves from
									// destination
									intInputDestinaion = intInputDestinaion + remainLeaves;
								}
							} else if (newInputDestinaion > newInputOrigin) {
								// check whether destination can give this
								// amount of leaves
								if (remaininPVTBusDestination >= intInputDestinaion) {
									intInputDestinaion = intInputDestinaion + remainLeaves;
								} else if (remaininPVTBusOrigin >= intInputOrigin) {
									// if destination non fixed PVT buses are
									// not enough to give leaves get leaves from
									// origin
									intInputOrigin = intInputOrigin + remainLeaves;
								}
							}
						}
					} else if (tempLeaves > noOfLeaves) {
						int extraLeaves = tempLeaves - noOfLeaves;

						if (extraLeaves == 1) {
							if (intInputOrigin > intInputDestinaion && intInputOrigin >= 1) {
								intInputOrigin = intInputOrigin - 1;
							} else if (intInputDestinaion > intInputOrigin && intInputDestinaion >= 1) {
								intInputDestinaion = intInputDestinaion - 1;
							}
						} else {
							System.out.println("do something if leaves are greater than 1");
							// check extraLeaves are odd or even and remove
							// buses from both origin and destination
						}

					}
					if ((intInputOrigin + intInputDestinaion) == noOfLeaves) {

						// check the leaves can be given
						if (intInputOrigin <= remaininPVTBusOrigin && intInputDestinaion <= remaininPVTBusDestination) {
							// valid
						} else {
							if (intInputOrigin > remaininPVTBusOrigin) {

								int extraLeaves = intInputOrigin - remaininPVTBusOrigin;

								// check whether these leaves can be given from
								// destination start
								int remainDesLeaves = remaininPVTBusDestination - intInputDestinaion;
								if (remainDesLeaves >= extraLeaves) {
									intInputOrigin = intInputOrigin + extraLeaves;
									intInputDestinaion = intInputDestinaion - extraLeaves;
								} else {
									System.out.println("cannnot perform coupling for selected route");
									validationDto = new WithoutFixedTimeValidationDTO();
									validationDto.setError("cannnot perform coupling for selected route - Group One");
									return validationDto;
								}
								// check whether these leaves can be given from
								// destination end

							} else if (intInputDestinaion > remaininPVTBusDestination) {
								int extraLeaves = intInputDestinaion - remaininPVTBusDestination;

								// check whether these leaves can be given from
								// origin start
								int remainOriLeaves = remaininPVTBusOrigin - intInputOrigin;
								if (remainOriLeaves >= extraLeaves) {
									intInputOrigin = intInputOrigin + extraLeaves;
									intInputDestinaion = intInputDestinaion - extraLeaves;
								} else {
									System.out.println("cannnot perform coupling for selected route");
									validationDto = new WithoutFixedTimeValidationDTO();
									validationDto.setError("cannnot perform coupling for selected route - Group One");
									return validationDto;
								}
								// check whether these leaves can be given from
								// origin end
							}
						}
						// check the leaves can be given
						if (intInputOrigin > 9) {
							validationDto.setNoOfLeavesOrigin(9);
						} // added by tharushi.e
						else {

							validationDto.setNoOfLeavesOrigin(intInputOrigin);
						}
						if (intInputDestinaion > 9) {
							validationDto.setNoOfLeavesDestination(9); // added by tharushi.e

						} else {
							validationDto.setNoOfLeavesDestination(intInputDestinaion);
						}

						if (intInputOrigin > 9) {
							validationDto.setNoOfLeavesOrigin(9);
						} else {
							validationDto.setNoOfLeavesOrigin(intInputOrigin);
						} // added by tharushi.e

						if (intInputDestinaion > 9) {
							validationDto.setNoOfLeavesDestination(9);
						} else {
							validationDto.setNoOfLeavesDestination(intInputDestinaion);
						} // added by tharushi.e
						if (intInputOrigin > 9) {
							groupOneDTO.setBusesOnLeaveOriginOne(9);
						} else {
							groupOneDTO.setBusesOnLeaveOriginOne(intInputOrigin);
						} // added by tharushi.e

						if (intInputDestinaion > 9) {
							groupOneDTO.setBusesOnLeaveDestinationOne(9);
						} else {
							groupOneDTO.setBusesOnLeaveDestinationOne(intInputDestinaion);
						} // added by tharushi.e

						panelGeneratorWithoutFixedTimeService.updateLeaves(timeTableDTO.getGenereatedRefNo(), "1", intInputOrigin, intInputDestinaion,
								sessionBackingBean.getLoginUser());
					}

					return validationDto;
				}
			} else {
				// can perform coupling
				int leftBuses = originGroupOneList.size() - (couplingTimeSlots * 2);
				/*
				 * i.e: originGroupOneList.size()=21 to get num of leaves, calculate ->
				 * originGroupOneList.size())/2 = 10 and 1 bus is left when we couple id we
				 * didn't add that remaining bus/es one time slot will be left to add that time
				 * slot/s have to add the left bus/es int leftBuses = 21 - (10*2) = 1
				 */
				validationDto = new WithoutFixedTimeValidationDTO();
				validationDto.setAllbusesOrigin(allBusesOrigin);
				validationDto.setAllBusesDestination(allBusesDestinaion);
				if (leftBuses > 0) {
					if (remaininPVTBusOrigin >= leftBuses) {
						// add a bus from origin remaining buses
						validationDto.setNoOfLeavesOriginSec(leftBuses);
					} else if (remaininPVTBusDestination >= leftBuses) {
						// add a bus from destination remaining buses
						validationDto.setNoOfLeavesDestinationSec(leftBuses);
					} else {
						// one or more time slot/s will be left -> can't happen
						validationDto = new WithoutFixedTimeValidationDTO();
						validationDto.setError(
								"cannot perform the operation please check time slots and bus numbers - Group One");
						return validationDto;
					}
				} else {
					validationDto.setSuccess(true);
				}

				return validationDto;
			}
			// if coupling

		} else {

			// if not coupling
			// check all time slots can be filled start
			if (allBuses == originGroupOneList.size()) {
				System.out.println("time slots and buses are okay, can perform the operation successfully");
				validationDto = new WithoutFixedTimeValidationDTO();
				validationDto.setSuccess(true);
				return validationDto;
			}
			// check all time slots can be filled end

			// give leave start
			DecimalFormat df2 = new DecimalFormat("#.##");
			if (allBuses > originGroupOneList.size()) {
				// int numberOfLeaves = allBuses - originGroupOneList.size();
				int numberOfLeaves = (((allBusesOrigin * 1) + (allBusesDestinaion * 1)) - originGroupOneList.size())
						/ 1;
				validationDto = new WithoutFixedTimeValidationDTO();

				double originLeaves = (((double) allBusesOrigin
						- ((double) originfixedPVTbuses + (double) originfixedCTBbuses))
						/ ((double) allBuses - (double) allFixedBuses)) * (double) numberOfLeaves;
				double destinationLeaves = (((double) allBusesDestinaion
						- ((double) destinationfixedPVTbuses + (double) destinationfixedCTBbuses))
						/ ((double) allBuses - (double) allFixedBuses)) * (double) numberOfLeaves;
				;

				BigDecimal bdOrigin = new BigDecimal(originLeaves).setScale(1, RoundingMode.HALF_UP);
				double newInputOrigin = bdOrigin.doubleValue();
				int intInputOrigin = (int) Math.round(newInputOrigin);
				BigDecimal bdDestination = new BigDecimal(destinationLeaves).setScale(1, RoundingMode.HALF_UP);
				double newInputDestinaion = bdDestination.doubleValue();
				int intInputDestinaion = (int) Math.round(newInputDestinaion);

				int tempLeaves = (int) Math.round(newInputOrigin) + (int) Math.round(newInputDestinaion);
				if (tempLeaves < numberOfLeaves) {
					int remainLeaves = numberOfLeaves - tempLeaves;
					if (intInputOrigin <= intInputDestinaion) {
						// intInputOrigin = intInputOrigin+remainLeaves;

						// check whether origin can give this amount of leaves
						if (remaininPVTBusOrigin >= intInputOrigin) {
							// can give leaves
							intInputOrigin = intInputOrigin + remainLeaves;
						}

					} else if (intInputDestinaion <= intInputOrigin) {
						// intInputDestinaion = intInputDestinaion+remainLeaves;

						// check whether origin can give this amount of leaves
						if (remaininPVTBusDestination >= intInputDestinaion) {
							// can give leaves
							intInputDestinaion = intInputDestinaion + remainLeaves;
						}
					}
				} else if (tempLeaves > numberOfLeaves) {
					int moreLeaves = tempLeaves - numberOfLeaves;
					if (originLeaves > destinationLeaves) {
						int oriLeave = intInputOrigin - moreLeaves;
						intInputOrigin = oriLeave;
					} else {
						int desLeave = intInputDestinaion - moreLeaves;
						intInputDestinaion = desLeave;
					}
				}

				if ((intInputOrigin + intInputDestinaion) == numberOfLeaves) {
					if (intInputOrigin > 9) {
						validationDto.setNoOfLeavesOrigin(9);
					} // added by tharushi.e
					else {
						validationDto.setNoOfLeavesOrigin(intInputOrigin);
					}
					if (intInputDestinaion > 9) {
						validationDto.setNoOfLeavesDestination(9);
					} else {
						validationDto.setNoOfLeavesDestination(intInputDestinaion);
					}
					validationDto.setAllbusesOrigin(allBusesOrigin);
					validationDto.setAllBusesDestination(allBusesDestinaion);
					if (intInputOrigin > 9) {
						groupOneDTO.setBusesOnLeaveOriginOne(9);
					} else {
						groupOneDTO.setBusesOnLeaveOriginOne(intInputOrigin);
					} // added by tharushi.e

					if (intInputDestinaion > 9) {
						groupOneDTO.setBusesOnLeaveDestinationOne(9);
					} else {
						groupOneDTO.setBusesOnLeaveDestinationOne(intInputDestinaion);
					} // added by tharushi.e

					panelGeneratorWithoutFixedTimeService.updateLeaves(timeTableDTO.getGenereatedRefNo(), "1", intInputOrigin, intInputDestinaion,
							sessionBackingBean.getLoginUser());
				} else {
					System.out.println(
							"origin leave buses and destination leave buses are not equal to number of leaves - Group one");
					validationDto = new WithoutFixedTimeValidationDTO();
					validationDto.setError("cannot perform the operation due to leave calculation error - Group One");
					return validationDto;
				}

				return validationDto;
			}
			// give leave end

			int remainingTimeSlots = originGroupOneList.size() - allBuses;

			if (remainingTimeSlots > 0) {
				// return "Please perform coupling for the route";
				validationDto = new WithoutFixedTimeValidationDTO();
				validationDto.setError("Please perform coupling for the route - Group One");
				return validationDto;
			}
			// if not coupling
		}

		return null;
	}

	public long calculateTotalInterval(String i) {
		String[] parts = i.split(":");

		long hours = Long.parseLong(parts[0]);
		long minute = Long.parseLong(parts[1]);

		long total_minute = minute + (hours * 60);

		return total_minute;
	}

	public long calculateTotalMinutesForStart(String s) {

		String[] parts = s.split(":");

		long start_hours = Long.parseLong(parts[0]);
		long start_minute = Long.parseLong(parts[1]);

		long total_minute = start_minute + (start_hours * 60);

		return total_minute;
	}

	public long calculateTotalMinutesForEnd(String e) {

		String[] parts = e.split(":");

		long start_hours = Long.parseLong(parts[0]);
		long start_minute = Long.parseLong(parts[1]);

		long total_minute = start_minute + (start_hours * 60);

		return total_minute;
	}

	public List<String> intervalCalculator(long begin, long end, long interval) {

		timeList = new ArrayList<>();
		String final_time = null;

		for (long time = begin; time <= end; time += interval) {

			final_time = String.format("%02d:%02d", time / 60, time % 60);

			timeList.add(final_time);

			if (time < end && end - time < interval) {

				final_time = String.format("%02d:%02d", time / 60, (end - time) % 60);

				timeList.add(final_time);
			}

		}

		return timeList;
	}

	/* Type 1 for Origin, Type 2 for destination */
	public boolean validate(Object newValue, int type, int group) {

		routeDetailsList = timeTableService.getNewRouteDetails(timeTableDTO.getRouteNo(),
				timeTableDTO.getGenereatedRefNo());
		boolean available = false;

		if (groupCount == 1) {

			long o = calculateTotalInterval(routeDetailsList.get(0).getOriginDivideRangeString());
			long d = calculateTotalInterval(routeDetailsList.get(0).getDestinationDivideRangeString());

			String stringToConvert = String.valueOf(newValue);
			Long convertedLong = Long.parseLong(stringToConvert);
			if (group == 1) {
				if (type == 1) {

					if (o >= convertedLong) {
						available = true;

					} else {
						available = false;
					}
				} else if (type == 2) {
					if (d >= convertedLong) {
						available = true;

					} else {
						available = false;
					}
				} else {
					available = false;
				}
			}

		} else if (groupCount == 2) {

			long o = calculateTotalInterval(routeDetailsList.get(0).getOriginDivideRangeString());
			long d = calculateTotalInterval(routeDetailsList.get(0).getDestinationDivideRangeString());

			long c = calculateTotalInterval(routeDetailsList.get(1).getOriginDivideRangeString());
			long e = calculateTotalInterval(routeDetailsList.get(1).getDestinationDivideRangeString());

			String stringToConvert = String.valueOf(newValue);
			Long convertedLong = Long.parseLong(stringToConvert);

			if (group == 1) {

				if (type == 1) {

					if (o >= convertedLong) {
						available = true;

					} else {
						available = false;

					}
				} else if (type == 2) {
					if (d >= convertedLong) {
						available = true;

					} else {
						available = false;

					}
				}

			} else if (group == 2) {

				if (type == 1) {

					if (c >= convertedLong) {
						available = true;

					} else {
						available = false;

					}
				} else if (type == 2) {
					if (e >= convertedLong) {
						available = true;

					} else {
						available = false;

					}
				}
			}

		} else if (groupCount == 3) {

			long o = calculateTotalInterval(routeDetailsList.get(0).getOriginDivideRangeString());
			long d = calculateTotalInterval(routeDetailsList.get(0).getDestinationDivideRangeString());

			long c = calculateTotalInterval(routeDetailsList.get(1).getOriginDivideRangeString());
			long e = calculateTotalInterval(routeDetailsList.get(1).getDestinationDivideRangeString());

			long l = calculateTotalInterval(routeDetailsList.get(2).getOriginDivideRangeString());
			long m = calculateTotalInterval(routeDetailsList.get(2).getDestinationDivideRangeString());

			String stringToConvert = String.valueOf(newValue);
			Long convertedLong = Long.parseLong(stringToConvert);

			if (group == 1) {

				if (type == 1) {

					if (o >= convertedLong) {
						available = true;

					} else {
						available = false;

					}
				} else if (type == 2) {
					if (d >= convertedLong) {
						available = true;

					} else {
						available = false;

					}
				}

			} else if (group == 2) {

				if (type == 1) {

					if (c >= convertedLong) {
						available = true;

					} else {
						available = false;

					}
				} else if (type == 2) {
					if (e >= convertedLong) {
						available = true;

					} else {
						available = false;

					}
				}
			} else if (group == 3) {

				if (type == 1) {

					if (l >= convertedLong) {
						available = true;

					} else {
						available = false;

					}
				} else if (type == 2) {
					if (m >= convertedLong) {
						available = true;

					} else {
						available = false;

					}
				}
			}
		}

		return available;
	}

	public long calculateTotalMinute(String firstValue, String secondValue) {

		String[] parts01 = firstValue.split(":");
		String[] parts02 = secondValue.split(":");

		long start_hours01 = Long.parseLong(parts01[0]);
		long start_minute01 = Long.parseLong(parts01[1]);

		long start_hours02 = Long.parseLong(parts02[0]);
		long start_minute02 = Long.parseLong(parts02[1]);

		long time01 = (start_minute01 + (start_hours01 * 60));
		long time02 = (start_minute02 + (start_hours02 * 60));

		if (time01 > time02) {

			long total_minute = time01 - time02;

			return total_minute;
		} else {

			long total_minute = time02 - time01;

			return total_minute;
		}

	}

	/* Update Group One Origin Buses Details */
	public void ajaxOnRowEditOriginOne(RowEditEvent event) {

		/*
		 * Connection conn = null; PreparedStatement ps = null; ResultSet rs = null;
		 */

		try {
			/** get travel time start **/
			TimeTableDTO travleTimeDTO = new TimeTableDTO();
			travleTimeDTO = timeTableService.getTravleTimeForRouteBusCate(timeTableDTO.getRouteNo(),
					timeTableDTO.getBusCategory());
			/** get travel time end **/

			String busNum = ((TimeTableDTO) event.getObject()).getBusNo();
			String startTime = ((TimeTableDTO) event.getObject()).getStartTime();
			String endTime = ((TimeTableDTO) event.getObject()).getEndTime();
			String duplicateBusNum = ((TimeTableDTO) event.getObject()).getDuplicateBusNum();
			String referanceNum = timeTableDTO.getGenereatedRefNo();
			String routeNum = timeTableDTO.getRouteNo();
			String tripType = "O";
			String groupNum = "1";
			String tempBusNum = ((TimeTableDTO) event.getObject()).getDuplicateBusNum();
			;
			String tempStartTime = ((TimeTableDTO) event.getObject()).getTempStartTime();
			String tempEndTime = ((TimeTableDTO) event.getObject()).getTempEndTime();

			if (!isValidDate(startTime) || startTime.matches(".*[a-zA-Z]+.*")) {
				((TimeTableDTO) event.getObject()).setStartTime(tempStartTime);
				((TimeTableDTO) event.getObject()).setEndTime(tempEndTime);
				((TimeTableDTO) event.getObject()).setBusNo(tempBusNum);
				sessionBackingBean.setMessage("Start time is not valid. Please check the format");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
				return;
			}
			if (!isValidDate(endTime) || endTime.matches(".*[a-zA-Z]+.*")) {
				((TimeTableDTO) event.getObject()).setStartTime(tempStartTime);
				((TimeTableDTO) event.getObject()).setEndTime(tempEndTime);
				((TimeTableDTO) event.getObject()).setBusNo(tempBusNum);
				sessionBackingBean.setMessage("Start time is not valid. Please check the format");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
				return;
			}

			/** set end time according to start time start **/
			if (!startTime.equals(tempStartTime)) {

				SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
				timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

				String plusTime = travleTimeDTO.getTimeRangeEnd();// travel time

				Date startTimeDate = timeFormat.parse(startTime);
				Date plusTimeDate = timeFormat.parse(plusTime);

				long addTime = startTimeDate.getTime() + plusTimeDate.getTime();
				String finalAddedTime = timeFormat.format(new Date(addTime));

				((TimeTableDTO) event.getObject()).setEndTime(finalAddedTime);
				endTime = finalAddedTime;
			}
			/** set end time according to start time end **/

			TimeTableDTO dto = new TimeTableDTO();
			dto.setBusNo(busNum);
			dto.setStartTime(startTime);
			dto.setEndTime(endTime);
			dto.setDuplicateBusNum(duplicateBusNum);
			dto.setGenereatedRefNo(referanceNum);
			dto.setRouteNo(routeNum);
			dto.setTripType(tripType);
			dto.setGroup(groupNum);
			dto.setTripId(((TimeTableDTO) event.getObject()).getTripId());
			dto.setTempStartTime(tempStartTime);
			dto.setTempEndTime(tempEndTime);

			panelGeneratorWithoutFixedTimeService.updateTimetableGeneratorNew(dto, groupNum, tripType, referanceNum,
					sessionBackingBean.getLoginUser(), dto.getDuplicateBusNum());

			((TimeTableDTO) event.getObject()).setDuplicateBusNum(busNum);

		} catch (Exception ex) {
			ex.printStackTrace();
			sessionBackingBean.setMessage("Data cannot be saved");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
		}

		RequestContext.getCurrentInstance().update("@form,originGroupOne");
		RequestContext.getCurrentInstance().update("destinationTableGroupOne");

		RequestContext.getCurrentInstance().execute("PF('successUpdateMSG').show()");

	}

	/* Update Group One Destination Buses Details */
	public void ajaxOnRowEditDestinationOne(RowEditEvent event) {
		/*
		 * Connection conn = null; PreparedStatement ps = null; ResultSet rs = null;
		 */
		try {
			/** get travel time start **/
			TimeTableDTO travleTimeDTO = new TimeTableDTO();
			travleTimeDTO = timeTableService.getTravleTimeForRouteBusCate(timeTableDTO.getRouteNo(),
					timeTableDTO.getBusCategory());
			/** get travel time end **/

			String busNum = ((TimeTableDTO) event.getObject()).getBusNo();
			String startTime = ((TimeTableDTO) event.getObject()).getStartTime();
			String endTime = ((TimeTableDTO) event.getObject()).getEndTime();
			String duplicateBusNum = ((TimeTableDTO) event.getObject()).getDuplicateBusNum();
			String referanceNum = timeTableDTO.getGenereatedRefNo();
			String routeNum = timeTableDTO.getRouteNo();
			String tripType = "D";
			String groupNum = "1";
			String tempBusNum = ((TimeTableDTO) event.getObject()).getDuplicateBusNum();
			;
			String tempStartTime = ((TimeTableDTO) event.getObject()).getTempStartTime();
			String tempEndTime = ((TimeTableDTO) event.getObject()).getTempEndTime();

			if (!isValidDate(startTime) || startTime.matches(".*[a-zA-Z]+.*")) {
				((TimeTableDTO) event.getObject()).setStartTime(tempStartTime);
				((TimeTableDTO) event.getObject()).setEndTime(tempEndTime);
				((TimeTableDTO) event.getObject()).setBusNo(tempBusNum);
				sessionBackingBean.setMessage("Start time is not valid. Please check the format");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
				return;
			}
			if (!isValidDate(endTime) || endTime.matches(".*[a-zA-Z]+.*")) {
				((TimeTableDTO) event.getObject()).setStartTime(tempStartTime);
				((TimeTableDTO) event.getObject()).setEndTime(tempEndTime);
				((TimeTableDTO) event.getObject()).setBusNo(tempBusNum);
				sessionBackingBean.setMessage("Start time is not valid. Please check the format");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
				return;
			}

			/** set end time according to start time start **/
			if (!startTime.equals(tempStartTime)) {

				SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
				timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

				String plusTime = travleTimeDTO.getTimeRangeEnd();// travel time

				Date startTimeDate = timeFormat.parse(startTime);
				Date plusTimeDate = timeFormat.parse(plusTime);

				long addTime = startTimeDate.getTime() + plusTimeDate.getTime();
				String finalAddedTime = timeFormat.format(new Date(addTime));

				((TimeTableDTO) event.getObject()).setEndTime(finalAddedTime);
				endTime = finalAddedTime;
			}
			/** set end time according to start time end **/

			TimeTableDTO dto = new TimeTableDTO();
			dto.setBusNo(busNum);
			dto.setStartTime(startTime);
			dto.setEndTime(endTime);
			dto.setDuplicateBusNum(duplicateBusNum);
			dto.setGenereatedRefNo(referanceNum);
			dto.setRouteNo(routeNum);
			dto.setTripType(tripType);
			dto.setGroup(groupNum);
			dto.setTripId(((TimeTableDTO) event.getObject()).getTripId());
			dto.setTempStartTime(tempStartTime);
			dto.setTempEndTime(tempEndTime);

			panelGeneratorWithoutFixedTimeService.updateTimetableGeneratorNew(dto, groupNum, tripType, referanceNum,
					sessionBackingBean.getLoginUser(), dto.getDuplicateBusNum());

			((TimeTableDTO) event.getObject()).setDuplicateBusNum(busNum);
			// conn.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			sessionBackingBean.setMessage("Data cannot be saved ");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
		}
		RequestContext.getCurrentInstance().update("destinationTableGroupOne");

		RequestContext.getCurrentInstance().execute("PF('successUpdateMSG').show()");
	}

	/* Update Group Two Origin Buses Details */
	public void ajaxOnRowEditOriginTwo(RowEditEvent event) {

		try {
			/** get travel time start **/
			TimeTableDTO travleTimeDTO = new TimeTableDTO();
			travleTimeDTO = timeTableService.getTravleTimeForRouteBusCate(timeTableDTO.getRouteNo(),
					timeTableDTO.getBusCategory());
			/** get travel time end **/
			String busNum = ((TimeTableDTO) event.getObject()).getBusNo();
			String startTime = ((TimeTableDTO) event.getObject()).getStartTime();
			String endTime = ((TimeTableDTO) event.getObject()).getEndTime();
			String duplicateBusNum = ((TimeTableDTO) event.getObject()).getDuplicateBusNum();
			String referanceNum = timeTableDTO.getGenereatedRefNo();
			String routeNum = timeTableDTO.getRouteNo();
			String tripType = "O";
			String groupNum = "2";
			String tempBusNum = ((TimeTableDTO) event.getObject()).getDuplicateBusNum();
			;
			String tempStartTime = ((TimeTableDTO) event.getObject()).getTempStartTime();
			String tempEndTime = ((TimeTableDTO) event.getObject()).getTempEndTime();

			if (!isValidDate(startTime) || startTime.matches(".*[a-zA-Z]+.*")) {
				((TimeTableDTO) event.getObject()).setStartTime(tempStartTime);
				((TimeTableDTO) event.getObject()).setEndTime(tempEndTime);
				((TimeTableDTO) event.getObject()).setBusNo(tempBusNum);
				sessionBackingBean.setMessage("Start time is not valid. Please check the format");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
				return;
			}
			if (!isValidDate(endTime) || endTime.matches(".*[a-zA-Z]+.*")) {
				((TimeTableDTO) event.getObject()).setStartTime(tempStartTime);
				((TimeTableDTO) event.getObject()).setEndTime(tempEndTime);
				((TimeTableDTO) event.getObject()).setBusNo(tempBusNum);
				sessionBackingBean.setMessage("Start time is not valid. Please check the format");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
				return;
			}

			/** set end time according to start time start **/
			if (!startTime.equals(tempStartTime)) {

				SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
				timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

				String plusTime = travleTimeDTO.getTimeRangeEnd();// travel time

				Date startTimeDate = timeFormat.parse(startTime);
				Date plusTimeDate = timeFormat.parse(plusTime);

				long addTime = startTimeDate.getTime() + plusTimeDate.getTime();
				String finalAddedTime = timeFormat.format(new Date(addTime));

				((TimeTableDTO) event.getObject()).setEndTime(finalAddedTime);
				endTime = finalAddedTime;
			}
			/** set end time according to start time end **/

			TimeTableDTO dto = new TimeTableDTO();
			dto.setBusNo(busNum);
			dto.setStartTime(startTime);
			dto.setEndTime(endTime);
			dto.setDuplicateBusNum(duplicateBusNum);
			dto.setGenereatedRefNo(referanceNum);
			dto.setRouteNo(routeNum);
			dto.setTripType(tripType);
			dto.setGroup(groupNum);
			dto.setTripId(((TimeTableDTO) event.getObject()).getTripId());
			dto.setTempStartTime(tempStartTime);
			dto.setTempEndTime(tempEndTime);

			RequestContext.getCurrentInstance().update("originGroupTwo");

			panelGeneratorWithoutFixedTimeService.updateTimetableGeneratorNew(dto, groupNum, tripType, referanceNum,
					sessionBackingBean.getLoginUser(), dto.getDuplicateBusNum());

			((TimeTableDTO) event.getObject()).setDuplicateBusNum(busNum);
			// conn.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			sessionBackingBean.setMessage("Data cannot be saved");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
		}
		RequestContext.getCurrentInstance().execute("PF('successUpdateMSG').show()");
	}

	/* Update Group Two Destination Buses Details */
	public void ajaxOnRowEditDestinationTwo(RowEditEvent event) {

		try {

			/** get travel time start **/
			TimeTableDTO travleTimeDTO = new TimeTableDTO();
			travleTimeDTO = timeTableService.getTravleTimeForRouteBusCate(timeTableDTO.getRouteNo(),
					timeTableDTO.getBusCategory());
			/** get travel time end **/

			String busNum = ((TimeTableDTO) event.getObject()).getBusNo();
			String startTime = ((TimeTableDTO) event.getObject()).getStartTime();
			String endTime = ((TimeTableDTO) event.getObject()).getEndTime();
			String duplicateBusNum = ((TimeTableDTO) event.getObject()).getDuplicateBusNum();
			String referanceNum = timeTableDTO.getGenereatedRefNo();
			String routeNum = timeTableDTO.getRouteNo();
			String tripType = "D";
			String groupNum = "2";
			String tempBusNum = ((TimeTableDTO) event.getObject()).getDuplicateBusNum();
			;
			String tempStartTime = ((TimeTableDTO) event.getObject()).getTempStartTime();
			String tempEndTime = ((TimeTableDTO) event.getObject()).getTempEndTime();

			if (!isValidDate(startTime) || startTime.matches(".*[a-zA-Z]+.*")) {
				((TimeTableDTO) event.getObject()).setStartTime(tempStartTime);
				((TimeTableDTO) event.getObject()).setEndTime(tempEndTime);
				((TimeTableDTO) event.getObject()).setBusNo(tempBusNum);
				sessionBackingBean.setMessage("Start time is not valid. Please check the format");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
				return;
			}
			if (!isValidDate(endTime) || endTime.matches(".*[a-zA-Z]+.*")) {
				((TimeTableDTO) event.getObject()).setStartTime(tempStartTime);
				((TimeTableDTO) event.getObject()).setEndTime(tempEndTime);
				((TimeTableDTO) event.getObject()).setBusNo(tempBusNum);
				sessionBackingBean.setMessage("Start time is not valid. Please check the format");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
				return;
			}

			/** set end time according to start time start **/
			if (!startTime.equals(tempStartTime)) {

				SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
				timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

				String plusTime = travleTimeDTO.getTimeRangeEnd();// travel time

				Date startTimeDate = timeFormat.parse(startTime);
				Date plusTimeDate = timeFormat.parse(plusTime);

				long addTime = startTimeDate.getTime() + plusTimeDate.getTime();
				String finalAddedTime = timeFormat.format(new Date(addTime));

				((TimeTableDTO) event.getObject()).setEndTime(finalAddedTime);
				endTime = finalAddedTime;
			}
			/** set end time according to start time end **/

			TimeTableDTO dto = new TimeTableDTO();
			dto.setBusNo(busNum);
			dto.setStartTime(startTime);
			dto.setEndTime(endTime);
			dto.setDuplicateBusNum(duplicateBusNum);
			dto.setGenereatedRefNo(referanceNum);
			dto.setRouteNo(routeNum);
			dto.setTripType(tripType);
			dto.setGroup(groupNum);
			dto.setTripId(((TimeTableDTO) event.getObject()).getTripId());
			dto.setTempStartTime(tempStartTime);
			dto.setTempEndTime(tempEndTime);

			RequestContext.getCurrentInstance().update("originGroupThree");

			panelGeneratorWithoutFixedTimeService.updateTimetableGeneratorNew(dto, groupNum, tripType, referanceNum,
					sessionBackingBean.getLoginUser(), dto.getDuplicateBusNum());

			((TimeTableDTO) event.getObject()).setDuplicateBusNum(busNum);

		} catch (Exception ex) {
			ex.printStackTrace();
			sessionBackingBean.setMessage("Data cannot be saved ");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
		}

		RequestContext.getCurrentInstance().execute("PF('successUpdateMSG').show()");
	}

	public void ajaxOnRowEditOriginThree(RowEditEvent event) {

		try {

			/** get travel time start **/
			TimeTableDTO travleTimeDTO = new TimeTableDTO();
			travleTimeDTO = timeTableService.getTravleTimeForRouteBusCate(timeTableDTO.getRouteNo(),
					timeTableDTO.getBusCategory());
			/** get travel time end **/

			String busNum = ((TimeTableDTO) event.getObject()).getBusNo();
			String startTime = ((TimeTableDTO) event.getObject()).getStartTime();
			String endTime = ((TimeTableDTO) event.getObject()).getEndTime();
			String duplicateBusNum = ((TimeTableDTO) event.getObject()).getDuplicateBusNum();
			String referanceNum = timeTableDTO.getGenereatedRefNo();
			String routeNum = timeTableDTO.getRouteNo();
			String tripType = "O";
			String groupNum = "3";
			String tempBusNum = ((TimeTableDTO) event.getObject()).getDuplicateBusNum();
			;
			String tempStartTime = ((TimeTableDTO) event.getObject()).getTempStartTime();
			String tempEndTime = ((TimeTableDTO) event.getObject()).getTempEndTime();

			if (!isValidDate(startTime) || startTime.matches(".*[a-zA-Z]+.*")) {
				((TimeTableDTO) event.getObject()).setStartTime(tempStartTime);
				((TimeTableDTO) event.getObject()).setEndTime(tempEndTime);
				((TimeTableDTO) event.getObject()).setBusNo(tempBusNum);
				sessionBackingBean.setMessage("Start time is not valid. Please check the format");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
				return;
			}
			if (!isValidDate(endTime) || endTime.matches(".*[a-zA-Z]+.*")) {
				((TimeTableDTO) event.getObject()).setStartTime(tempStartTime);
				((TimeTableDTO) event.getObject()).setEndTime(tempEndTime);
				((TimeTableDTO) event.getObject()).setBusNo(tempBusNum);
				sessionBackingBean.setMessage("Start time is not valid. Please check the format");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
				return;
			}

			/** set end time according to start time start **/
			if (!startTime.equals(tempStartTime)) {

				SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
				timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

				String plusTime = travleTimeDTO.getTimeRangeEnd();// travel time

				Date startTimeDate = timeFormat.parse(startTime);
				Date plusTimeDate = timeFormat.parse(plusTime);

				long addTime = startTimeDate.getTime() + plusTimeDate.getTime();
				String finalAddedTime = timeFormat.format(new Date(addTime));

				((TimeTableDTO) event.getObject()).setEndTime(finalAddedTime);
				endTime = finalAddedTime;
			}
			/** set end time according to start time end **/

			TimeTableDTO dto = new TimeTableDTO();
			dto.setBusNo(busNum);
			dto.setStartTime(startTime);
			dto.setEndTime(endTime);
			dto.setDuplicateBusNum(duplicateBusNum);
			dto.setGenereatedRefNo(referanceNum);
			dto.setRouteNo(routeNum);
			dto.setTripType(tripType);
			dto.setGroup(groupNum);
			dto.setTripId(((TimeTableDTO) event.getObject()).getTripId());
			dto.setTempStartTime(tempStartTime);
			dto.setTempEndTime(tempEndTime);

			RequestContext.getCurrentInstance().update("originGroupThree");

			panelGeneratorWithoutFixedTimeService.updateTimetableGeneratorNew(dto, groupNum, tripType, referanceNum,
					sessionBackingBean.getLoginUser(), dto.getDuplicateBusNum());
			((TimeTableDTO) event.getObject()).setDuplicateBusNum(busNum);
			// conn.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			sessionBackingBean.setMessage("Data cannot be saved ");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
		}
		RequestContext.getCurrentInstance().execute("PF('successUpdateMSG').show()");
	}

	public void ajaxOnRowEditDestinationThree(RowEditEvent event) {

		try {
			/** get travel time start **/
			TimeTableDTO travleTimeDTO = new TimeTableDTO();
			travleTimeDTO = timeTableService.getTravleTimeForRouteBusCate(timeTableDTO.getRouteNo(),
					timeTableDTO.getBusCategory());
			/** get travel time end **/

			String busNum = ((TimeTableDTO) event.getObject()).getBusNo();
			String startTime = ((TimeTableDTO) event.getObject()).getStartTime();
			String endTime = ((TimeTableDTO) event.getObject()).getEndTime();
			String duplicateBusNum = ((TimeTableDTO) event.getObject()).getDuplicateBusNum();
			String referanceNum = timeTableDTO.getGenereatedRefNo();
			String routeNum = timeTableDTO.getRouteNo();
			String tripType = "D";
			String groupNum = "3";
			String tempBusNum = ((TimeTableDTO) event.getObject()).getDuplicateBusNum();
			String tempStartTime = ((TimeTableDTO) event.getObject()).getTempStartTime();
			String tempEndTime = ((TimeTableDTO) event.getObject()).getTempEndTime();

			if (!isValidDate(startTime)) {
				((TimeTableDTO) event.getObject()).setStartTime(tempStartTime);
				((TimeTableDTO) event.getObject()).setEndTime(tempEndTime);
				((TimeTableDTO) event.getObject()).setBusNo(tempBusNum);
				sessionBackingBean.setMessage("Start time is not valid. Please check the format");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
				return;
			}
			if (!isValidDate(endTime)) {
				((TimeTableDTO) event.getObject()).setStartTime(tempStartTime);
				((TimeTableDTO) event.getObject()).setEndTime(tempEndTime);
				((TimeTableDTO) event.getObject()).setBusNo(tempBusNum);
				sessionBackingBean.setMessage("Start time is not valid. Please check the format");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
				return;
			}

			/** set end time according to start time start **/
			if (!startTime.equals(tempStartTime)) {

				SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
				timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

				String plusTime = travleTimeDTO.getTimeRangeEnd();// travel time

				Date startTimeDate = timeFormat.parse(startTime);
				Date plusTimeDate = timeFormat.parse(plusTime);

				long addTime = startTimeDate.getTime() + plusTimeDate.getTime();
				String finalAddedTime = timeFormat.format(new Date(addTime));

				((TimeTableDTO) event.getObject()).setEndTime(finalAddedTime);
				endTime = finalAddedTime;
			}
			/** set end time according to start time end **/

			TimeTableDTO dto = new TimeTableDTO();
			dto.setBusNo(busNum);
			dto.setStartTime(startTime);
			dto.setEndTime(endTime);
			dto.setDuplicateBusNum(duplicateBusNum);
			dto.setGenereatedRefNo(referanceNum);
			dto.setRouteNo(routeNum);
			dto.setTripType(tripType);
			dto.setGroup(groupNum);
			dto.setTripId(((TimeTableDTO) event.getObject()).getTripId());
			dto.setTempStartTime(tempStartTime);
			dto.setTempEndTime(tempEndTime);

			RequestContext.getCurrentInstance().update("destinationTableGroupThree");

			panelGeneratorWithoutFixedTimeService.updateTimetableGeneratorNew(dto, groupNum, tripType, referanceNum,
					sessionBackingBean.getLoginUser(), dto.getDuplicateBusNum());
			((TimeTableDTO) event.getObject()).setDuplicateBusNum(busNum);
			// conn.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			sessionBackingBean.setMessage("Data cannot be saved ");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
		}

		RequestContext.getCurrentInstance().execute("PF('successUpdateMSG').show()");
	}

	public void ajaxCalculateTotalNoOfBus() {

		int CTB_O01 = groupOneDTO.getNoOfCTBbuessOriginOne();
		int PVT_O01 = groupOneDTO.getNoOfPVTbuessOriginOne();
		int OTHERS_O01 = groupOneDTO.getNoOfOtherbuessOriginOne();
		groupOneDTO.setTotalBusesOriginOne(CTB_O01 + PVT_O01 + OTHERS_O01);

		int CTB_D01 = groupOneDTO.getNoOfCTBbuessDestinationOne();
		int PVT_D01 = groupOneDTO.getNoOfPVTbuessDestinationOne();
		int OTHERS_D01 = groupOneDTO.getNoOfOtherbuessDestinationOne();
		groupOneDTO.setTotalBusesDestinationOne(CTB_D01 + PVT_D01 + OTHERS_D01);

		int CTB_O02 = groupTwoDTO.getNoOfCTBbuessOriginTwo();
		int PVT_O02 = groupTwoDTO.getNoOfPVTbuessOriginTwo();
		int OTHERS_O02 = groupTwoDTO.getNoOfOtherbuessOriginTwo();
		groupTwoDTO.setTotalBusesOriginTwo(CTB_O02 + PVT_O02 + OTHERS_O02);

		int CTB_D02 = groupTwoDTO.getNoOfCTBbuessDestinationTwo();
		int PVT_D02 = groupTwoDTO.getNoOfPVTbuessDestinationTwo();
		int OTHERS_D02 = groupTwoDTO.getNoOfOtherbuessDestinationTwo();
		groupTwoDTO.setTotalBusesDestinationTwo(CTB_D02 + PVT_D02 + OTHERS_D02);

		int CTB_O03 = groupThreeDTO.getNoOfCTBbuessOriginThree();
		int PVT_O03 = groupThreeDTO.getNoOfPVTbuessOriginThree();
		int OTHERS_O03 = groupThreeDTO.getNoOfOtherbuessOriginThree();
		groupThreeDTO.setTotalBusesOriginThree(CTB_O03 + PVT_O03 + OTHERS_O03);

		int CTB_D03 = groupThreeDTO.getNoOfCTBbuessDestinationThree();
		int PVT_D03 = groupThreeDTO.getNoOfPVTbuessDestinationThree();
		int OTHERS_D03 = groupThreeDTO.getNoOfOtherbuessDestinationThree();
		groupThreeDTO.setTotalBusesDestinationThree(CTB_D03 + PVT_D03 + OTHERS_D03);

	}

	public void onClickUpdateSuccessDialog() {
		System.out.println("came");
		searchAction();

		RequestContext.getCurrentInstance().execute("PF('successUpdateMSG').hide()");
	}

	public void clearOne() {

		routeDetailsList = new ArrayList<>();
		timeTableDTO = new TimeTableDTO();
		groupOneDTO = new TimeTableDTO();
		groupTwoDTO = new TimeTableDTO();
		groupThreeDTO = new TimeTableDTO();
		tripsDTO = new TimeTableDTO();
		busRouteList = timeTableService.getRouteNoList();// took
		coupling = false;
		couplesPerBus = null;
		couplesPerBusDisable = false;

		routeDetailsList = new ArrayList<>();
		originGroupOneList = new ArrayList<>();
		originGroupTwoList = new ArrayList<>();
		originGroupThreeList = new ArrayList<>();
		destinationGroupOneList = new ArrayList<>();
		destinationGroupTwoList = new ArrayList<>();
		destinationGroupThreeList = new ArrayList<>();

		groupCount = 0;
		timeList = new ArrayList<>();

		originOneBuses = 0;
		destinationOneBuses = 0;
		originTwoBuses = 0;
		destinationTwoBuses = 0;
		originThreeBuses = 0;
		destinationThreeBuses = 0;

		busCategoryList = new ArrayList<TimeTableDTO>();

		selectedBusCategory = null;
		editColRender = false;
		leaveSaveDisable = false;

		TimeTableDTO midnightShiftO1 = new TimeTableDTO();
		TimeTableDTO midnightShiftD1 = new TimeTableDTO();
		TimeTableDTO midnightShiftO2 = new TimeTableDTO();
		TimeTableDTO midnightShiftD2 = new TimeTableDTO();
		TimeTableDTO midnightShiftO3 = new TimeTableDTO();
		TimeTableDTO midnightShiftD3 = new TimeTableDTO();
		midnightShiftBusesOrigin = new LinkedList<TimeTableDTO>();
		midnightShiftBusesOrigin.add(midnightShiftO1);
		midnightShiftBusesDestination = new LinkedList<TimeTableDTO>();
		midnightShiftBusesDestination.add(midnightShiftD1);
		midnightShiftBusesOriginG2 = new LinkedList<TimeTableDTO>();
		midnightShiftBusesOriginG2.add(midnightShiftO2);
		midnightShiftBusesDestinationG2 = new LinkedList<TimeTableDTO>();
		midnightShiftBusesDestinationG2.add(midnightShiftD2);
		midnightShiftBusesOriginG3 = new LinkedList<TimeTableDTO>();
		midnightShiftBusesOriginG3.add(midnightShiftO3);
		midnightShiftBusesDestinationG3 = new LinkedList<TimeTableDTO>();
		midnightShiftBusesDestinationG3.add(midnightShiftD3);

		busNoListG1 = new ArrayList<>(0);
		busNoListDestinationG1 = new ArrayList<>(0);
		busNoListG2 = new ArrayList<>(0);
		busNoListDestinationG2 = new ArrayList<>(0);
		busNoListG3 = new ArrayList<>(0);
		busNoListDestinationG3 = new ArrayList<>(0);
	}

	public void saveAction() {

		boolean isCoupling = false;
		if (couplesPerBus.equals("1")) {
			isCoupling = false;
		} else {
			isCoupling = true;
		}

		if (groupCount == 1) {

			// 1
			panelGeneratorWithoutFixedTimeService.addDataIntoTimetableGenerator(originGroupOneList, destinationGroupOneList,
					timeTableDTO.getRouteNo(), "1", timeTableDTO.getGenereatedRefNo(), sessionBackingBean.getLoginUser(), isCoupling);

		} else if (groupCount == 2) {
			// 1
			panelGeneratorWithoutFixedTimeService.addDataIntoTimetableGenerator(originGroupOneList, destinationGroupOneList,
					timeTableDTO.getRouteNo(), "1", timeTableDTO.getGenereatedRefNo(), sessionBackingBean.getLoginUser(), isCoupling);

			// 2
			panelGeneratorWithoutFixedTimeService.addDataIntoTimetableGenerator(originGroupTwoList, destinationGroupTwoList,
					timeTableDTO.getRouteNo(), "2", timeTableDTO.getGenereatedRefNo(), sessionBackingBean.getLoginUser(), isCoupling);

		} else if (groupCount == 3) {
			// 1
			panelGeneratorWithoutFixedTimeService.addDataIntoTimetableGenerator(originGroupOneList, destinationGroupOneList,
					timeTableDTO.getRouteNo(), "1", timeTableDTO.getGenereatedRefNo(), sessionBackingBean.getLoginUser(), isCoupling);

			// 2
			panelGeneratorWithoutFixedTimeService.addDataIntoTimetableGenerator(originGroupTwoList, destinationGroupTwoList,
					timeTableDTO.getRouteNo(), "2", timeTableDTO.getGenereatedRefNo(), sessionBackingBean.getLoginUser(), isCoupling);

			// 3
			panelGeneratorWithoutFixedTimeService.addDataIntoTimetableGenerator(originGroupThreeList, destinationGroupThreeList,
					timeTableDTO.getRouteNo(), "2", timeTableDTO.getGenereatedRefNo(), sessionBackingBean.getLoginUser(), isCoupling);

		} else {
			System.out.println("Record cannot be updated.");
			sessionBackingBean.setMessage("Record cannot be updated.");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
		}

	}

	public void saveNumberOfLeavesAction() {
		boolean succeess = false;

		if (validateTimeSlots()) {

			if (groupCount == 1) {
				succeess = panelGeneratorWithoutFixedTimeService.updateLeaves(timeTableDTO.getGenereatedRefNo(), "1", 
						groupOneDTO.getBusesOnLeaveOriginOne(), groupOneDTO.getBusesOnLeaveDestinationOne(), sessionBackingBean.getLoginUser());

				panelGeneratorWithoutFixedTimeService.updateCouplesInMasterTable(timeTableDTO.getGenereatedRefNo(), "1",
						couplesPerBus, couplesPerBus);

			} else if (groupCount == 2) {

				succeess = panelGeneratorWithoutFixedTimeService.updateLeaves(timeTableDTO.getGenereatedRefNo(), "2", 
						groupOneDTO.getBusesOnLeaveOriginTwo(), groupOneDTO.getBusesOnLeaveDestinationTwo(), sessionBackingBean.getLoginUser());

				panelGeneratorWithoutFixedTimeService.updateCouplesInMasterTable(timeTableDTO.getGenereatedRefNo(), "2",
						couplesPerBus, couplesPerBus);

			} else if (groupCount == 3) {

				succeess = panelGeneratorWithoutFixedTimeService.updateLeaves(timeTableDTO.getGenereatedRefNo(), "3", 
						groupOneDTO.getBusesOnLeaveOriginThree(), groupOneDTO.getBusesOnLeaveDestinationThree(), sessionBackingBean.getLoginUser());
				
				panelGeneratorWithoutFixedTimeService.updateCouplesInMasterTable(timeTableDTO.getGenereatedRefNo(), "3",
						couplesPerBus, couplesPerBus);

			}

			/** modify by tharushi,e **/
			if (isEdit) {
				updateTimeSlotsEdit();
			} else {
				updateTimeSlots();
			}
			/** end modification **/

			if (succeess) {
				sessionBackingBean.setMessage("Saved successfully.");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonSuccess').show()");

				searchAction();
			} else {
				System.out.println("Leaves cannot be saved.");
				sessionBackingBean.setMessage("Leaves cannot be saved.");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
			}

		}

	}

	public void addMigNightShiftBuses(String type) {
		// group one origin
		if (type.equals("O1")) {
			System.out.println(type);

			if (midnightShiftBusesOrigin.get(midnightShiftBusesOrigin.size() - 1).getBusNo() != null
					&& !midnightShiftBusesOrigin.get(midnightShiftBusesOrigin.size() - 1).getBusNo().isEmpty()
					&& !midnightShiftBusesOrigin.get(midnightShiftBusesOrigin.size() - 1).getBusNo().trim()
							.equals("")) {
				if (midnightShiftBusesOrigin.get(midnightShiftBusesOrigin.size() - 1).getStartTime() != null
						&& !midnightShiftBusesOrigin.get(midnightShiftBusesOrigin.size() - 1).getStartTime().isEmpty()
						&& !midnightShiftBusesOrigin.get(midnightShiftBusesOrigin.size() - 1).getStartTime().trim()
								.equals("")) {
					if (midnightShiftBusesOrigin.get(midnightShiftBusesOrigin.size() - 1).getEndTime() != null
							&& !midnightShiftBusesOrigin.get(midnightShiftBusesOrigin.size() - 1).getEndTime().isEmpty()
							&& !midnightShiftBusesOrigin.get(midnightShiftBusesOrigin.size() - 1).getEndTime().trim()
									.equals("")) {

						TimeTableDTO midnightShiftO1 = new TimeTableDTO();
						midnightShiftO1.setTripId(Integer.toString(midnightShiftBusesOrigin.size() + 1));
						midnightShiftO1.setRowDisable(false);
						midnightShiftBusesOrigin.add(midnightShiftO1);
						midnightShiftBusesOrigin.get(midnightShiftBusesOrigin.size() - 2).setRowDisable(true);

					} else {
						// please add end time no
						sessionBackingBean.setMessage("Please add End Time");
						RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
						return;
					}
				} else {
					// please add start time no
					sessionBackingBean.setMessage("Please add Start Time");
					RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
					return;
				}
			} else {
				// please add bus no
				sessionBackingBean.setMessage("Please add Bus No");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
				return;
			}
		}
		// group one destination
		if (type.equals("D1")) {
			System.out.println(type);

			if (midnightShiftBusesDestination.get(midnightShiftBusesDestination.size() - 1).getBusNo() != null
					&& !midnightShiftBusesDestination.get(midnightShiftBusesDestination.size() - 1).getBusNo().isEmpty()
					&& !midnightShiftBusesDestination.get(midnightShiftBusesDestination.size() - 1).getBusNo().trim()
							.equals("")) {
				if (midnightShiftBusesDestination.get(midnightShiftBusesDestination.size() - 1).getStartTime() != null
						&& !midnightShiftBusesDestination.get(midnightShiftBusesDestination.size() - 1).getStartTime()
								.isEmpty()
						&& !midnightShiftBusesDestination.get(midnightShiftBusesDestination.size() - 1).getStartTime()
								.trim().equals("")) {
					if (midnightShiftBusesDestination.get(midnightShiftBusesDestination.size() - 1).getEndTime() != null
							&& !midnightShiftBusesDestination.get(midnightShiftBusesDestination.size() - 1).getEndTime()
									.isEmpty()
							&& !midnightShiftBusesDestination.get(midnightShiftBusesDestination.size() - 1).getEndTime()
									.trim().equals("")) {

						TimeTableDTO midnightShiftO1 = new TimeTableDTO();
						midnightShiftO1.setTripId(Integer.toString(midnightShiftBusesDestination.size() + 1));
						midnightShiftO1.setRowDisable(false);
						midnightShiftBusesDestination.add(midnightShiftO1);
						midnightShiftBusesDestination.get(midnightShiftBusesDestination.size() - 2).setRowDisable(true);

					} else {
						// please add end time no
						sessionBackingBean.setMessage("Please add End Time");
						RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
						return;
					}
				} else {
					// please add start time no
					sessionBackingBean.setMessage("Please add Start Time");
					RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
					return;
				}
			} else {
				// please add bus no
				sessionBackingBean.setMessage("Please add Bus No");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
				return;
			}
		}

		// group two origin
		if (type.equals("O2")) {
			System.out.println(type);

			if (midnightShiftBusesOriginG2.get(midnightShiftBusesOriginG2.size() - 1).getBusNo() != null
					&& !midnightShiftBusesOriginG2.get(midnightShiftBusesOriginG2.size() - 1).getBusNo().isEmpty()
					&& !midnightShiftBusesOriginG2.get(midnightShiftBusesOriginG2.size() - 1).getBusNo().trim()
							.equals("")) {
				if (midnightShiftBusesOriginG2.get(midnightShiftBusesOriginG2.size() - 1).getStartTime() != null
						&& !midnightShiftBusesOriginG2.get(midnightShiftBusesOriginG2.size() - 1).getStartTime()
								.isEmpty()
						&& !midnightShiftBusesOriginG2.get(midnightShiftBusesOriginG2.size() - 1).getStartTime().trim()
								.equals("")) {
					if (midnightShiftBusesOriginG2.get(midnightShiftBusesOriginG2.size() - 1).getEndTime() != null
							&& !midnightShiftBusesOriginG2.get(midnightShiftBusesOriginG2.size() - 1).getEndTime()
									.isEmpty()
							&& !midnightShiftBusesOriginG2.get(midnightShiftBusesOriginG2.size() - 1).getEndTime()
									.trim().equals("")) {

						TimeTableDTO midnightShiftO1 = new TimeTableDTO();
						midnightShiftO1.setTripId(Integer.toString(midnightShiftBusesOriginG2.size() + 1));
						midnightShiftO1.setRowDisable(false);
						midnightShiftBusesOriginG2.add(midnightShiftO1);
						midnightShiftBusesOriginG2.get(midnightShiftBusesOriginG2.size() - 2).setRowDisable(true);

					} else {
						// please add end time no
						sessionBackingBean.setMessage("Please add End Time");
						RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
						return;
					}
				} else {
					// please add start time no
					sessionBackingBean.setMessage("Please add Start Time");
					RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
					return;
				}
			} else {
				// please add bus no
				sessionBackingBean.setMessage("Please add Bus No");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
				return;
			}
		}
		// group one destination
		if (type.equals("D2")) {
			System.out.println(type);

			if (midnightShiftBusesDestinationG2.get(midnightShiftBusesDestinationG2.size() - 1).getBusNo() != null
					&& !midnightShiftBusesDestinationG2.get(midnightShiftBusesDestinationG2.size() - 1).getBusNo()
							.isEmpty()
					&& !midnightShiftBusesDestinationG2.get(midnightShiftBusesDestinationG2.size() - 1).getBusNo()
							.trim().equals("")) {
				if (midnightShiftBusesDestinationG2.get(midnightShiftBusesDestinationG2.size() - 1)
						.getStartTime() != null
						&& !midnightShiftBusesDestinationG2.get(midnightShiftBusesDestinationG2.size() - 1)
								.getStartTime().isEmpty()
						&& !midnightShiftBusesDestinationG2.get(midnightShiftBusesDestinationG2.size() - 1)
								.getStartTime().trim().equals("")) {
					if (midnightShiftBusesDestinationG2.get(midnightShiftBusesDestinationG2.size() - 1)
							.getEndTime() != null
							&& !midnightShiftBusesDestinationG2.get(midnightShiftBusesDestinationG2.size() - 1)
									.getEndTime().isEmpty()
							&& !midnightShiftBusesDestinationG2.get(midnightShiftBusesDestinationG2.size() - 1)
									.getEndTime().trim().equals("")) {

						TimeTableDTO midnightShiftO1 = new TimeTableDTO();
						midnightShiftO1.setTripId(Integer.toString(midnightShiftBusesDestinationG2.size() + 1));
						midnightShiftO1.setRowDisable(false);
						midnightShiftBusesDestinationG2.add(midnightShiftO1);
						midnightShiftBusesDestinationG2.get(midnightShiftBusesDestinationG2.size() - 2)
								.setRowDisable(true);

					} else {
						// please add end time no
						sessionBackingBean.setMessage("Please add End Time");
						RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
						return;
					}
				} else {
					// please add start time no
					sessionBackingBean.setMessage("Please add Start Time");
					RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
					return;
				}
			} else {
				// please add bus no
				sessionBackingBean.setMessage("Please add Bus No");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
				return;
			}
		}

		// group three origin
		if (type.equals("O3")) {
			System.out.println(type);

			if (midnightShiftBusesOriginG3.get(midnightShiftBusesOriginG3.size() - 1).getBusNo() != null
					&& !midnightShiftBusesOriginG3.get(midnightShiftBusesOriginG3.size() - 1).getBusNo().isEmpty()
					&& !midnightShiftBusesOriginG3.get(midnightShiftBusesOriginG3.size() - 1).getBusNo().trim()
							.equals("")) {
				if (midnightShiftBusesOriginG3.get(midnightShiftBusesOriginG3.size() - 1).getStartTime() != null
						&& !midnightShiftBusesOriginG3.get(midnightShiftBusesOriginG3.size() - 1).getStartTime()
								.isEmpty()
						&& !midnightShiftBusesOriginG3.get(midnightShiftBusesOriginG3.size() - 1).getStartTime().trim()
								.equals("")) {
					if (midnightShiftBusesOriginG3.get(midnightShiftBusesOriginG3.size() - 1).getEndTime() != null
							&& !midnightShiftBusesOriginG3.get(midnightShiftBusesOriginG3.size() - 1).getEndTime()
									.isEmpty()
							&& !midnightShiftBusesOriginG3.get(midnightShiftBusesOriginG3.size() - 1).getEndTime()
									.trim().equals("")) {

						TimeTableDTO midnightShiftO1 = new TimeTableDTO();
						midnightShiftO1.setTripId(Integer.toString(midnightShiftBusesOriginG3.size() + 1));
						midnightShiftO1.setRowDisable(false);
						midnightShiftBusesOriginG3.add(midnightShiftO1);
						midnightShiftBusesOriginG3.get(midnightShiftBusesOriginG3.size() - 2).setRowDisable(true);

					} else {
						// please add end time no
						sessionBackingBean.setMessage("Please add End Time");
						RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
						return;
					}
				} else {
					// please add start time no
					sessionBackingBean.setMessage("Please add Start Time");
					RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
					return;
				}
			} else {
				// please add bus no
				sessionBackingBean.setMessage("Please add Bus No");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
				return;
			}
		}
		// group one destination
		if (type.equals("D3")) {
			System.out.println(type);

			if (midnightShiftBusesDestinationG3.get(midnightShiftBusesDestinationG3.size() - 1).getBusNo() != null
					&& !midnightShiftBusesDestinationG3.get(midnightShiftBusesDestinationG3.size() - 1).getBusNo()
							.isEmpty()
					&& !midnightShiftBusesDestinationG3.get(midnightShiftBusesDestinationG3.size() - 1).getBusNo()
							.trim().equals("")) {
				if (midnightShiftBusesDestinationG3.get(midnightShiftBusesDestinationG3.size() - 1)
						.getStartTime() != null
						&& !midnightShiftBusesDestinationG3.get(midnightShiftBusesDestinationG3.size() - 1)
								.getStartTime().isEmpty()
						&& !midnightShiftBusesDestinationG3.get(midnightShiftBusesDestinationG3.size() - 1)
								.getStartTime().trim().equals("")) {
					if (midnightShiftBusesDestinationG3.get(midnightShiftBusesDestinationG3.size() - 1)
							.getEndTime() != null
							&& !midnightShiftBusesDestinationG3.get(midnightShiftBusesDestinationG3.size() - 1)
									.getEndTime().isEmpty()
							&& !midnightShiftBusesDestinationG3.get(midnightShiftBusesDestinationG3.size() - 1)
									.getEndTime().trim().equals("")) {

						TimeTableDTO midnightShiftO1 = new TimeTableDTO();
						midnightShiftO1.setTripId(Integer.toString(midnightShiftBusesDestinationG3.size() + 1));
						midnightShiftO1.setRowDisable(false);
						midnightShiftBusesDestinationG3.add(midnightShiftO1);
						midnightShiftBusesDestinationG3.get(midnightShiftBusesDestinationG3.size() - 2)
								.setRowDisable(true);

					} else {
						// please add end time no
						sessionBackingBean.setMessage("Please add End Time");
						RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
						return;
					}
				} else {
					// please add start time no
					sessionBackingBean.setMessage("Please add Start Time");
					RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
					return;
				}
			} else {
				// please add bus no
				sessionBackingBean.setMessage("Please add Bus No");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
				return;
			}
		}
	}

	public void deleteMigNightShiftBuses(String type) {
		nightShiftBusType = type;
		RequestContext.getCurrentInstance().execute("PF('confirmDeleteDlg').show()");
	}

	public void deleteNightShiftBusBtnAction() {
		String type = nightShiftBusType;
		if (type.equals("O1")) {
			if (midnightShiftBusesOrigin.size() >= 1) {
				TimeTableDTO deleteDTO = new TimeTableDTO();

				if (midnightShiftBusesOrigin.size() == 1) {
					deleteDTO = midnightShiftBusesOrigin.get(0);
					midnightShiftBusesOrigin.remove(0);

					TimeTableDTO midnightShiftO1 = new TimeTableDTO();
					midnightShiftO1.setTripId("1");

					midnightShiftBusesOrigin.add(midnightShiftO1);
				} else {
					deleteDTO = midnightShiftBusesOrigin.get(midnightShiftBusesOrigin.size() - 1);

					midnightShiftBusesOrigin.remove(midnightShiftBusesOrigin.size() - 1);
					midnightShiftBusesOrigin.get(midnightShiftBusesOrigin.size() - 1).setRowDisable(false);

				}

				timeTableService.deleteNightShiftBuses(deleteDTO, timeTableDTO.getRouteNo(),
						timeTableDTO.getGenereatedRefNo(), "1", "O", sessionBackingBean.getLoginUser());

			}
		}
		if (type.equals("D1")) {
			if (midnightShiftBusesDestination.size() >= 1) {
				TimeTableDTO deleteDTO = new TimeTableDTO();

				if (midnightShiftBusesDestination.size() == 1) {
					deleteDTO = midnightShiftBusesDestination.get(0);
					midnightShiftBusesDestination.remove(0);

					TimeTableDTO midnightShiftO1 = new TimeTableDTO();
					midnightShiftO1.setTripId("1");

					midnightShiftBusesDestination.add(midnightShiftO1);
				} else {
					deleteDTO = midnightShiftBusesDestination.get(midnightShiftBusesDestination.size() - 1);

					midnightShiftBusesDestination.remove(midnightShiftBusesDestination.size() - 1);
					midnightShiftBusesDestination.get(midnightShiftBusesDestination.size() - 1).setRowDisable(false);

				}

				timeTableService.deleteNightShiftBuses(deleteDTO, timeTableDTO.getRouteNo(),
						timeTableDTO.getGenereatedRefNo(), "1", "D", sessionBackingBean.getLoginUser());

			}
		}

		if (type.equals("O2")) {
			if (midnightShiftBusesOriginG2.size() >= 1) {
				midnightShiftBusesOriginG2.remove(midnightShiftBusesOriginG2.size() - 1);
				midnightShiftBusesOriginG2.get(midnightShiftBusesOriginG2.size() - 1).setRowDisable(false);
			}
		}
		if (type.equals("D2")) {
			if (midnightShiftBusesDestinationG2.size() >= 1) {
				midnightShiftBusesDestinationG2.remove(midnightShiftBusesDestinationG2.size() - 1);
				midnightShiftBusesDestinationG2.get(midnightShiftBusesDestinationG2.size() - 1).setRowDisable(false);
			}
		}

		if (type.equals("O3")) {
			if (midnightShiftBusesOriginG3.size() >= 1) {
				midnightShiftBusesOriginG3.remove(midnightShiftBusesOriginG3.size() - 1);
				midnightShiftBusesOriginG3.get(midnightShiftBusesOriginG3.size() - 1).setRowDisable(false);
			}
		}
		if (type.equals("D3")) {
			if (midnightShiftBusesDestinationG3.size() >= 1) {
				midnightShiftBusesDestinationG3.remove(midnightShiftBusesDestinationG3.size() - 1);
				midnightShiftBusesDestinationG3.get(midnightShiftBusesDestinationG3.size() - 1).setRowDisable(false);
			}
		}
	}

	public void clearTwo() {
		routeDetailsList = new ArrayList<>();
		timeTableDTO = new TimeTableDTO();
		groupOneDTO = new TimeTableDTO();
		groupTwoDTO = new TimeTableDTO();
		groupThreeDTO = new TimeTableDTO();
		tripsDTO = new TimeTableDTO();
		busRouteList = timeTableService.getRouteNoList();// took
		coupling = false;
		couplesPerBus = null;
		couplesPerBusDisable = false;

		routeDetailsList = new ArrayList<>();
		originGroupOneList = new ArrayList<>();
		originGroupTwoList = new ArrayList<>();
		originGroupThreeList = new ArrayList<>();
		destinationGroupOneList = new ArrayList<>();
		destinationGroupTwoList = new ArrayList<>();
		destinationGroupThreeList = new ArrayList<>();

		groupCount = 0;
		timeList = new ArrayList<>();

		originOneBuses = 0;
		destinationOneBuses = 0;
		originTwoBuses = 0;
		destinationTwoBuses = 0;
		originThreeBuses = 0;
		destinationThreeBuses = 0;

		busCategoryList = new ArrayList<TimeTableDTO>();
		;
		selectedBusCategory = null;
		editColRender = false;
		leaveSaveDisable = false;

		TimeTableDTO midnightShiftO1 = new TimeTableDTO();
		TimeTableDTO midnightShiftD1 = new TimeTableDTO();
		TimeTableDTO midnightShiftO2 = new TimeTableDTO();
		TimeTableDTO midnightShiftD2 = new TimeTableDTO();
		TimeTableDTO midnightShiftO3 = new TimeTableDTO();
		TimeTableDTO midnightShiftD3 = new TimeTableDTO();
		midnightShiftBusesOrigin = new LinkedList<TimeTableDTO>();
		midnightShiftBusesOrigin.add(midnightShiftO1);
		midnightShiftBusesDestination = new LinkedList<TimeTableDTO>();
		midnightShiftBusesDestination.add(midnightShiftD1);
		midnightShiftBusesOriginG2 = new LinkedList<TimeTableDTO>();
		midnightShiftBusesOriginG2.add(midnightShiftO2);
		midnightShiftBusesDestinationG2 = new LinkedList<TimeTableDTO>();
		midnightShiftBusesDestinationG2.add(midnightShiftD2);
		midnightShiftBusesOriginG3 = new LinkedList<TimeTableDTO>();
		midnightShiftBusesOriginG3.add(midnightShiftO3);
		midnightShiftBusesDestinationG3 = new LinkedList<TimeTableDTO>();
		midnightShiftBusesDestinationG3.add(midnightShiftD3);
	}

	public List<TimeTableDTO> createAllDatesDataForStartEndTime(List<String> startEndTimes, int timeGap, String busRide,
			boolean origin) {
		List<TimeTableDTO> timeList = new ArrayList<TimeTableDTO>();

		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		/*
		 * List<String> timeStartList= new ArrayList<String>(); List<String>
		 * timeEndList= new ArrayList<String>();
		 */

		try {
			String dateStart = null;
			String dateStop = null;
			if (origin) {
				dateStart = startEndTimes.get(0);
				dateStop = startEndTimes.get(1);
			} else {
				dateStart = startEndTimes.get(2);
				dateStop = startEndTimes.get(3);
			}

			// HH converts hour in 24 hours format (0-23), day calculation
			SimpleDateFormat format = new SimpleDateFormat("HH:mm");

			Date d1 = null;
			Date d2 = null;

			d1 = format.parse(dateStart);
			d2 = format.parse(dateStop);

			// in milliseconds
			long diff = d2.getTime() - d1.getTime();

			// long diffSeconds = diff / 1000 % 60;
			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000) % 24;
			// long diffDays = diff / (24 * 60 * 60 * 1000);

			// System.out.print(diffDays + " days, ");
			System.out.print(diffHours + " hours, ");
			System.out.print(diffMinutes + " minutes, ");
			// System.out.print(diffSeconds + " seconds.");

			int gapInMinutes = timeGap; // span-of-time.
			String addTimeString = busRide;// "01:00"; //Define time of bus ride
			int loops = ((int) Duration.ofHours(diffHours).toMinutes() / gapInMinutes);
			List<LocalTime> times = new ArrayList<>(loops);
			LocalTime time = LocalTime.parse(dateStart);// '00:00'

			Date dt1 = timeFormat.parse(dateStart);
			Date dt2 = timeFormat.parse(busRide);
			long sumtest = dt1.getTime() + dt2.getTime();
			String endTimeFirstVal = timeFormat.format(new Date(sumtest));
			// timeEndList.add(endTimeFirstVal);

			String date3 = null;
			for (int i = 1; i <= loops; i++) {
				TimeTableDTO dto = new TimeTableDTO();
				times.add(time);
				// timeStartList.add(time.toString());
				dto.setStartTime(time.toString());
				if (date3 != null && !date3.isEmpty()) {
					dto.setEndTime(date3);
				} else {
					dto.setEndTime(endTimeFirstVal);
				}

				// Set up next loop.
				time = time.plusMinutes(gapInMinutes);

				Date date1 = timeFormat.parse(time.toString());
				Date date2 = timeFormat.parse(addTimeString);
				long sum = date1.getTime() + date2.getTime();
				date3 = timeFormat.format(new Date(sum));
				System.out.println("The end sum is " + date3);
				// timeEndList.add(date3);

				timeList.add(dto);
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return timeList;
	}

	public List<TimeTableDTO> createGroupOneDataForOrigin(String restTimeString, List<String> startEndTimes,
			String driverRestTime, String abbreviation) {

		// time gap of buses
		List<TimeTableDTO> dtoList = new ArrayList<TimeTableDTO>();
		dtoList = timeTableService.showTimeSlotDetForGroups(timeTableDTO.getGenereatedRefNo(), "1", "O");
		int breakTime = dtoList.get(0).getFrequency();

		// time taken for bus ride
		String addTimeString = restTimeString;

		// get all time slots between start and end time
		List<TimeTableDTO> originTimeList = new ArrayList<TimeTableDTO>();
		originTimeList = createAllDatesDataForStartEndTime(startEndTimes, breakTime, addTimeString, true);

		// get already added data in panel genarator with fixed time for origin
		// and destination
		originGroupOneList = new ArrayList<TimeTableDTO>();
		originGroupOneList = timeTableService.getDetailsOfFixedBuses(timeTableDTO, "1", "O");

		// to generate temp bus number for without fixed time get last private
		// bus number of with fixed time
		int count = 0;
		String lastBusNumber = panelGeneratorWithoutFixedTimeService
				.retrieveLastPrivateBusNumber(timeTableDTO.getGenereatedRefNo(), "1", "O", abbreviation);
		if (lastBusNumber != null && !lastBusNumber.isEmpty()) {
			count = Integer.valueOf(lastBusNumber.substring(abbreviation.length()));
		}

		count = count + 1;

		// combine origin list and filled origin list
		boolean timeFound = false;
		TimeTableDTO tempDto = new TimeTableDTO();
		List<TimeTableDTO> finalTimeListOrigin = new ArrayList<TimeTableDTO>();
		for (TimeTableDTO dto : originTimeList) {
			for (TimeTableDTO dto2 : originGroupOneList) {
				if (dto.getStartTime().equalsIgnoreCase(dto2.getStartTime())) {
					tempDto = dto2;
					if (!dto2.isFixedTime()) {
						String tempBusNum = abbreviation + count;
						tempDto.setBusNo(tempBusNum);
						count = count + 1;
					}

					finalTimeListOrigin.add(tempDto);
					timeFound = true;
					break;

				} else {
					tempDto = dto;
					tempDto.setTripType("O");
					timeFound = false;

				}
			}
			if (originGroupOneList == null || originGroupOneList.size() == 0) {
				tempDto.setTripType("O");
				tempDto = dto;
			}
			if (!timeFound) {
				String tempBusNum = abbreviation + count;
				tempDto.setBusNo(tempBusNum);
				finalTimeListOrigin.add(tempDto);
				count = count + 1;
			}
		}

		return finalTimeListOrigin;
	}

	public List<TimeTableDTO> createGroupOneDataForDestination(String restTimeString, List<String> startEndTimes,
			String driverRestTime, String abbreviation) {
		// time gap of buses
		List<TimeTableDTO> dtoList = new ArrayList<TimeTableDTO>();
		dtoList = timeTableService.showTimeSlotDetForGroups(timeTableDTO.getGenereatedRefNo(), "1", "D");
		int breakTime = dtoList.get(0).getFrequency();

		// time taken for bus ride
		String addTimeString = restTimeString;

		// get all time slots between start and end time
		List<TimeTableDTO> destinationTimeList = new ArrayList<TimeTableDTO>();
		destinationTimeList = createAllDatesDataForStartEndTime(startEndTimes, breakTime, addTimeString, false);

		// get already added data in panel genarator with fixed time for origin
		// and destination
		destinationGroupOneList = new ArrayList<TimeTableDTO>();
		destinationGroupOneList = timeTableService.getDetailsOfFixedBuses(timeTableDTO, "1", "D");

		// to generate temp bus number for without fixed time get last private
		// bus number of with fixed time
		int count = 0;
		String lastBusNumber = panelGeneratorWithoutFixedTimeService
				.retrieveLastPrivateBusNumber(timeTableDTO.getGenereatedRefNo(), "1", "D", abbreviation);
		if (lastBusNumber != null && !lastBusNumber.isEmpty()) {
			count = Integer.valueOf(lastBusNumber.substring(1));
		}

		count = count + 1;

		// combine origin list and filled origin list
		boolean timeFound = false;
		TimeTableDTO tempDto = new TimeTableDTO();
		List<TimeTableDTO> finalTimeListDestination = new ArrayList<TimeTableDTO>();
		for (TimeTableDTO dto : destinationTimeList) {
			for (TimeTableDTO dto2 : destinationGroupOneList) {
				if (dto.getStartTime().equalsIgnoreCase(dto2.getStartTime())) {
					tempDto = dto2;
					if (!dto2.isFixedTime()) {
						String tempBusNum = abbreviation + count;
						tempDto.setBusNo(tempBusNum);
						count = count + 1;
					}

					finalTimeListDestination.add(tempDto);
					timeFound = true;
					break;

				} else {
					tempDto = dto;
					tempDto.setTripType("D");
					timeFound = false;

				}
			}
			if (destinationGroupOneList == null || destinationGroupOneList.size() == 0) {
				tempDto.setTripType("D");
				tempDto = dto;
			}
			if (!timeFound) {
				String tempBusNum = abbreviation + count;
				tempDto.setTripType("D");
				tempDto.setBusNo(tempBusNum);
				finalTimeListDestination.add(tempDto);
				count = count + 1;
			}
		}

		return finalTimeListDestination;
	}

	public List<TimeTableDTO> createGroupTwoDataForOrigin(String restTimeString, List<String> startEndTimes,
			String driverRestTime, String abbreviation) {

		// time gap of buses
		List<TimeTableDTO> dtoList = new ArrayList<TimeTableDTO>();
		dtoList = timeTableService.showTimeSlotDetForGroups(timeTableDTO.getGenereatedRefNo(), "2", "O");
		int breakTime = dtoList.get(0).getFrequency();

		// time taken for bus ride
		String addTimeString = restTimeString;

		// get all time slots between start and end time
		List<TimeTableDTO> originTimeList = new ArrayList<TimeTableDTO>();
		originTimeList = createAllDatesDataForStartEndTime(startEndTimes, breakTime, addTimeString, true);

		// get already added data in panel genarator with fixed time for origin
		// and destination
		originGroupTwoList = new ArrayList<TimeTableDTO>();
		originGroupTwoList = timeTableService.getDetailsOfFixedBuses(timeTableDTO, "2", "O");

		// to generate temp bus number for without fixed time get last private
		// bus number of with fixed time
		int count = 0;
		String lastBusNumber = panelGeneratorWithoutFixedTimeService
				.retrieveLastPrivateBusNumber(timeTableDTO.getGenereatedRefNo(), "2", "O", abbreviation);
		if (lastBusNumber != null && !lastBusNumber.isEmpty()) {
			count = Integer.valueOf(lastBusNumber.substring(1));
		}

		count = count + 1;

		// combine origin list and filled origin list
		boolean timeFound = false;
		TimeTableDTO tempDto = new TimeTableDTO();
		List<TimeTableDTO> finalTimeListOrigin = new ArrayList<TimeTableDTO>();
		for (TimeTableDTO dto : originTimeList) {
			for (TimeTableDTO dto2 : originGroupTwoList) {
				if (dto.getStartTime().equalsIgnoreCase(dto2.getStartTime())) {
					tempDto = dto2;
					if (!dto2.isFixedTime()) {
						String tempBusNum = abbreviation + count;
						tempDto.setBusNo(tempBusNum);
						count = count + 1;
					}

					finalTimeListOrigin.add(tempDto);
					timeFound = true;
					break;
				} else {
					tempDto = dto;
					timeFound = false;

				}
			}
			if (originGroupTwoList == null || originGroupTwoList.size() == 0) {
				tempDto = dto;
			}
			if (!timeFound) {
				String tempBusNum = abbreviation + count;
				tempDto.setBusNo(tempBusNum);
				finalTimeListOrigin.add(tempDto);
				count = count + 1;
			}
		}

		return finalTimeListOrigin;
	}

	public List<TimeTableDTO> createGroupTwoDataForDestination(String restTimeString, List<String> startEndTimes,
			String driverRestTime, String abbreviation) {
		// time gap of buses
		List<TimeTableDTO> dtoList = new ArrayList<TimeTableDTO>();
		dtoList = timeTableService.showTimeSlotDetForGroups(timeTableDTO.getGenereatedRefNo(), "2", "D");
		int breakTime = dtoList.get(0).getFrequency();

		// time taken for bus ride
		String addTimeString = restTimeString;

		// get all time slots between start and end time
		List<TimeTableDTO> destinationTimeList = new ArrayList<TimeTableDTO>();
		destinationTimeList = createAllDatesDataForStartEndTime(startEndTimes, breakTime, addTimeString, false);

		// get already added data in panel genarator with fixed time for origin
		// and destination
		destinationGroupTwoList = new ArrayList<TimeTableDTO>();
		destinationGroupTwoList = timeTableService.getDetailsOfFixedBuses(timeTableDTO, "2", "D");

		// to generate temp bus number for without fixed time get last private
		// bus number of with fixed time
		int count = 0;
		String lastBusNumber = panelGeneratorWithoutFixedTimeService
				.retrieveLastPrivateBusNumber(timeTableDTO.getGenereatedRefNo(), "2", "D", abbreviation);
		if (lastBusNumber != null && !lastBusNumber.isEmpty()) {
			count = Integer.valueOf(lastBusNumber.substring(1));
		}

		count = count + 1;

		// combine origin list and filled origin list
		boolean timeFound = false;
		TimeTableDTO tempDto = new TimeTableDTO();
		List<TimeTableDTO> finalTimeListDestination = new ArrayList<TimeTableDTO>();
		for (TimeTableDTO dto : destinationTimeList) {
			for (TimeTableDTO dto2 : destinationGroupTwoList) {
				if (dto.getStartTime().equalsIgnoreCase(dto2.getStartTime())) {
					tempDto = dto2;
					if (!dto2.isFixedTime()) {
						String tempBusNum = abbreviation + count;
						tempDto.setBusNo(tempBusNum);
						count = count + 1;
					}

					finalTimeListDestination.add(tempDto);
					timeFound = true;
					break;
				} else {
					tempDto = dto;
					timeFound = false;

				}
			}
			if (destinationGroupTwoList == null || destinationGroupTwoList.size() == 0) {
				tempDto = dto;
			}
			if (!timeFound) {
				String tempBusNum = abbreviation + count;
				tempDto.setBusNo(tempBusNum);
				finalTimeListDestination.add(tempDto);
				count = count + 1;
			}
		}

		/*
		 * destinationGroupTwoList = new ArrayList<TimeTableDTO>();
		 * destinationGroupTwoList = finalTimeListDestination;
		 */
		return finalTimeListDestination;
	}

	public List<TimeTableDTO> createGroupThreeDataForOrigin(String restTimeString, List<String> startEndTimes,
			String driverRestTime, String abbreviation) {

		// time gap of buses
		List<TimeTableDTO> dtoList = new ArrayList<TimeTableDTO>();
		dtoList = timeTableService.showTimeSlotDetForGroups(timeTableDTO.getGenereatedRefNo(), "3", "O");
		int breakTime = dtoList.get(0).getFrequency();

		// time taken for bus ride
		String addTimeString = restTimeString;

		// get all time slots between start and end time
		List<TimeTableDTO> originTimeList = new ArrayList<TimeTableDTO>();
		originTimeList = createAllDatesDataForStartEndTime(startEndTimes, breakTime, addTimeString, true);

		// get already added data in panel genarator with fixed time for origin
		// and destination
		originGroupThreeList = new ArrayList<TimeTableDTO>();
		originGroupThreeList = timeTableService.getDetailsOfFixedBuses(timeTableDTO, "3", "O");

		// to generate temp bus number for without fixed time get last private
		// bus number of with fixed time
		int count = 0;
		String lastBusNumber = panelGeneratorWithoutFixedTimeService
				.retrieveLastPrivateBusNumber(timeTableDTO.getGenereatedRefNo(), "3", "O", abbreviation);
		if (lastBusNumber != null && !lastBusNumber.isEmpty()) {
			count = Integer.valueOf(lastBusNumber.substring(1));
		}

		count = count + 1;

		// combine origin list and filled origin list
		boolean timeFound = false;
		TimeTableDTO tempDto = new TimeTableDTO();
		List<TimeTableDTO> finalTimeListOrigin = new ArrayList<TimeTableDTO>();
		for (TimeTableDTO dto : originTimeList) {
			for (TimeTableDTO dto2 : originGroupThreeList) {
				if (dto.getStartTime().equalsIgnoreCase(dto2.getStartTime())) {
					tempDto = dto2;
					if (!dto2.isFixedTime()) {
						String tempBusNum = abbreviation + count;
						tempDto.setBusNo(tempBusNum);
						count = count + 1;
					}

					finalTimeListOrigin.add(tempDto);
					timeFound = true;
					break;
				} else {
					tempDto = dto;
					timeFound = false;

				}
			}
			if (originGroupThreeList == null || originGroupThreeList.size() == 0) {
				tempDto = dto;
			}
			if (!timeFound) {
				String tempBusNum = abbreviation + count;
				tempDto.setBusNo(tempBusNum);
				finalTimeListOrigin.add(tempDto);
				count = count + 1;
			}
		}

		/*
		 * originGroupThreeList = new ArrayList<TimeTableDTO>(); originGroupThreeList =
		 * finalTimeListOrigin;
		 */
		return finalTimeListOrigin;
	}

	public List<TimeTableDTO> createGroupThreeDataForDestination(String restTimeString, List<String> startEndTimes,
			String driverRestTime, String abbreviation) {
		// time gap of buses
		List<TimeTableDTO> dtoList = new ArrayList<TimeTableDTO>();
		dtoList = timeTableService.showTimeSlotDetForGroups(timeTableDTO.getGenereatedRefNo(), "3", "D");
		int breakTime = dtoList.get(0).getFrequency();

		// time taken for bus ride
		String addTimeString = restTimeString;

		// get all time slots between start and end time
		List<TimeTableDTO> destinationTimeList = new ArrayList<TimeTableDTO>();
		destinationTimeList = createAllDatesDataForStartEndTime(startEndTimes, breakTime, addTimeString, false);

		// get already added data in panel genarator with fixed time for origin
		// and destination
		destinationGroupThreeList = new ArrayList<TimeTableDTO>();
		destinationGroupThreeList = timeTableService.getDetailsOfFixedBuses(timeTableDTO, "3", "D");

		// to generate temp bus number for without fixed time get last private
		// bus number of with fixed time
		int count = 0;
		String lastBusNumber = panelGeneratorWithoutFixedTimeService
				.retrieveLastPrivateBusNumber(timeTableDTO.getGenereatedRefNo(), "3", "D", abbreviation);
		if (lastBusNumber != null && !lastBusNumber.isEmpty()) {
			count = Integer.valueOf(lastBusNumber.substring(1));
		}

		count = count + 1;

		// combine origin list and filled origin list
		boolean timeFound = false;
		TimeTableDTO tempDto = new TimeTableDTO();
		List<TimeTableDTO> finalTimeListDestination = new ArrayList<TimeTableDTO>();
		for (TimeTableDTO dto : destinationTimeList) {
			for (TimeTableDTO dto2 : destinationGroupThreeList) {
				if (dto.getStartTime().equalsIgnoreCase(dto2.getStartTime())) {
					tempDto = dto2;
					if (!dto2.isFixedTime()) {
						String tempBusNum = abbreviation + count;
						tempDto.setBusNo(tempBusNum);
						count = count + 1;
					}

					finalTimeListDestination.add(tempDto);
					timeFound = true;
					break;
				} else {
					tempDto = dto;
					timeFound = false;

				}
			}
			if (destinationGroupThreeList == null || destinationGroupThreeList.size() == 0) {
				tempDto = dto;
			}
			if (!timeFound) {
				String tempBusNum = abbreviation + count;
				tempDto.setBusNo(tempBusNum);
				finalTimeListDestination.add(tempDto);
				count = count + 1;
			}
		}

		return finalTimeListDestination;
	}

	public void origintoDetinationBusSendGroupOne(List<TimeTableDTO> finalTimeListOrigin, String driverRestTime,
			String addTimeString, String abbreviation) {
		List<TimeTableDTO> tempOriginListMissing = new ArrayList<TimeTableDTO>();

		// keep a duplicate temp list of finalTimeListDestination to add and
		// remove DTOs after new busNo added to destinationList
		List<TimeTableDTO> tempOriginList = new ArrayList<TimeTableDTO>();
		for (TimeTableDTO destDTO : finalTimeListOrigin) {
			tempOriginList.add(destDTO);
		}

		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		try {
			boolean timeSlotAvailable = false;
			String originStartTime = null;
			String originEndTime = null;
			for (TimeTableDTO oriDTO : destinationGroupOneList) {
				for (TimeTableDTO destDTO : finalTimeListOrigin) {
					if (oriDTO.getEndTime() != null && !oriDTO.getEndTime().isEmpty()) {
						int gapInMinutes = (int) calculateTotalMinutesForEnd(driverRestTime);
						System.out.println("minutes:" + gapInMinutes);
						LocalTime time = LocalTime.parse(oriDTO.getEndTime());
						time = time.plusMinutes(gapInMinutes);// originDTO end
																// time + driver
																// rest time =
																// destination
																// departure
																// time

						originStartTime = time.toString();
						System.out.println("destination start time: " + originStartTime);

						Date busRdeTime = timeFormat.parse(addTimeString);// addTimeString
																			// is
																			// the
																			// time
																			// takes
																			// for
																			// bus
																			// ride
						Date destinationStartDate = timeFormat.parse(time.toString());
						long sum = busRdeTime.getTime() + destinationStartDate.getTime();
						originEndTime = timeFormat.format(new Date(sum));
						System.out.println("destination end time: " + originEndTime);

						if (destDTO.getStartTime().equals(originStartTime)
								&& destDTO.getEndTime().equals(originEndTime)) {
							if (!destDTO.isFixedTime()) {
								TimeTableDTO tempDTO = new TimeTableDTO();
								tempDTO.setStartTime(originStartTime);
								tempDTO.setEndTime(originEndTime);
								tempDTO.setBusNo(oriDTO.getBusNo());
								tempDTO.setTripType(oriDTO.getTripType());
								tempDTO.setFixedTime(oriDTO.isFixedTime());

								tempOriginList.remove(destDTO);// remove DTO
																// with tempBus
																// num
								tempOriginList.add(tempDTO);// add new DTO for
															// particular time
															// slot
							} else {

								tempOriginList.add(destDTO);

								TimeTableDTO tempDTO = new TimeTableDTO();
								tempDTO.setStartTime(originStartTime);
								tempDTO.setEndTime(originEndTime);
								tempDTO.setBusNo(oriDTO.getBusNo());
								tempDTO.setTripType(oriDTO.getTripType());
								tempDTO.setFixedTime(oriDTO.isFixedTime());
								tempOriginListMissing.add(tempDTO);
							}
							timeSlotAvailable = true;
							break;
						} else {
							timeSlotAvailable = false;
						}
					}
				}

				if (!timeSlotAvailable) {
					TimeTableDTO tempDTO = new TimeTableDTO();
					tempDTO.setStartTime(originStartTime);
					tempDTO.setEndTime(originEndTime);
					tempDTO.setBusNo(oriDTO.getBusNo());
					tempDTO.setTripType(oriDTO.getTripType());
					tempDTO.setFixedTime(oriDTO.isFixedTime());
					tempOriginList.add(tempDTO);
				}
			}

			// keep a duplicate temp list of finalTimeListOrigin
			List<TimeTableDTO> tempList = new ArrayList<TimeTableDTO>();
			int count = 0;
			boolean added = false;
			for (TimeTableDTO missingData : tempOriginListMissing) {
				added = false;
				for (TimeTableDTO destinationData : tempOriginList) {
					if (!destinationData.isFixedTime()) {
						if ((destinationData.getStartTime().compareTo(missingData.getStartTime()) > 0)
								&& (destinationData.getEndTime().compareTo(missingData.getEndTime()) > 0)) {
							TimeTableDTO tempDTO = new TimeTableDTO();
							tempDTO.setStartTime(destinationData.getStartTime());
							tempDTO.setEndTime(destinationData.getEndTime());
							tempDTO.setBusNo(missingData.getBusNo());
							tempDTO.setTripType(missingData.getTripType());
							tempDTO.setFixedTime(missingData.isFixedTime());
							tempList.add(tempDTO);
							count = count + 1;
							added = true;
							break;
						}
					}
				}
				if (!added) {
					System.out.println(
							"get last time range of tempDestinationList plus driver rest time add to tempList");
					System.out.println(missingData.getBusNo());
				}
			}

			List<TimeTableDTO> finalAddedList = new ArrayList<TimeTableDTO>();
			boolean add = false;
			for (TimeTableDTO destinationData : tempOriginList) {
				add = false;
				for (TimeTableDTO tempData : tempList) {
					if (tempData.getStartTime().equals(destinationData.getStartTime())
							&& tempData.getEndTime().equals(destinationData.getEndTime())) {
						finalAddedList.add(tempData);
						add = true;
						break;
					}
				}
				if (!add) {
					finalAddedList.add(destinationData);
				}
			}

			// some data in temp list are not added to final list add that data
			List<TimeTableDTO> tempfinalAddedList = new ArrayList<TimeTableDTO>();
			boolean found = false;
			for (TimeTableDTO tempData : tempList) {
				found = false;
				for (TimeTableDTO destinationData : tempOriginList) {
					if (tempData.getBusNo().equalsIgnoreCase(destinationData.getBusNo())) {
						found = true;
						break;
					}
				}
				if (!found) {
					tempfinalAddedList.add(tempData);
				}
			}

			finalAddedList.addAll(tempfinalAddedList);
			// some data in temp list are not added to final list add that data

			/** remove duplicates in list **/
			List<TimeTableDTO> temp = new ArrayList<TimeTableDTO>();
			for (TimeTableDTO t : finalAddedList) {
				temp.add(t);
			}
			System.out.println("finalAddedList size 1: " + finalAddedList.size());
			int counti = 0;
			for (TimeTableDTO d : temp) {
				counti = 0;
				Iterator<TimeTableDTO> it = finalAddedList.iterator();
				while (it.hasNext()) {
					TimeTableDTO dto = it.next();
					if (dto.getBusNo() != null && !dto.getBusNo().isEmpty() && d.getBusNo() != null
							&& !d.getBusNo().isEmpty() && dto.getBusNo().equals(d.getBusNo())) {
						counti = counti + 1;
						if (counti > 1) {
							it.remove();
						}
					} else {
						if (dto.getBusNo() == null || dto.getBusNo().isEmpty()
								|| dto.getBusNo().trim().equalsIgnoreCase("")) {
							it.remove();
						}
					}
				}
			}

			System.out.println("finalAddedList size 2: " + finalAddedList.size());
			/** remove duplicates in list **/

			/** correction of bus numbers start **/
			int busNoSuffix = 0;// abbreviation
			List<TimeTableDTO> busNoCorrectionList = new ArrayList<TimeTableDTO>();
			List<String> busNoStringList = new ArrayList<String>();
			List<String> currBusNoList = new ArrayList<String>();
			for (TimeTableDTO data : finalAddedList) {
				System.out.println("************************* " + data.getBusNo() + " " + data.getStartTime() + " "
						+ data.getEndTime());
				busNoCorrectionList.add(data);
				if (data.getBusNo().substring(0, abbreviation.length()).equals(abbreviation)) {
					busNoSuffix = busNoSuffix + 1;
					busNoStringList.add(abbreviation + busNoSuffix);
					currBusNoList.add(data.getBusNo());
				}
			}

			/** correction list **/
			HashMap<String, String> hm = new HashMap<String, String>();
			Collections.sort(currBusNoList);
			final int abbLen = abbreviation.length();
			Collections.sort(currBusNoList, new Comparator<String>() {
				@Override
				public int compare(String u1, String u2) {
					return Integer.valueOf(u2.substring(abbLen)) - Integer.valueOf(u1.substring(abbLen));
				}
			});

			Collections.reverse(currBusNoList);

			int sortCount = 0;
			for (String s : busNoStringList) {
				hm.put(s, currBusNoList.get(sortCount));
				sortCount = sortCount + 1;
			}
			/** correction list **/

			for (Map.Entry m : hm.entrySet()) {
				for (TimeTableDTO data : finalAddedList) {
					if (m.getValue().equals(data.getBusNo())) {
						TimeTableDTO addDTO = new TimeTableDTO();
						// addDTO = data;
						addDTO.setBusNo(m.getKey().toString());
						addDTO.setStartTime(data.getStartTime());
						addDTO.setEndTime(data.getEndTime());
						addDTO.setTripType(data.getTripType());
						addDTO.setFixedTime(data.isFixedTime());
						busNoCorrectionList.add(addDTO);
						busNoCorrectionList.remove(data);
					}
				}
			}
			/** correction of bus numbers end **/

			/**
			 * if trip count=2 divide non fixed bus count send bus twice start
			 **/
			if (coupling) {
				// origin
				List<TimeTableDTO> tripTwoList = new ArrayList<TimeTableDTO>();
				for (TimeTableDTO d : busNoCorrectionList) {
					if (d.getTripType() != null && !d.getTripType().equals("") && d.getTripType().equals("O")) {
						if (!d.isFixedTime()) {
							tripTwoList.add(d);
						}
					}
				}
				Collections.sort(tripTwoList, new Comparator<TimeTableDTO>() {
					@Override
					public int compare(TimeTableDTO u1, TimeTableDTO u2) {
						return Integer.valueOf(u2.getBusNo().substring(abbLen))
								- Integer.valueOf(u1.getBusNo().substring(abbLen));
					}
				});

				Collections.reverse(tripTwoList);

				HashMap<String, String> hashm = new HashMap<String, String>();
				int trips = tripTwoList.size() / 2;
				if (tripTwoList.size() % 2 != 0) {
					trips = trips + 1;
				}
				int addC = trips;
				int newC = 0;
				for (int i = 0; i < tripTwoList.size(); i++) {
					if (i < trips) {
						hashm.put(tripTwoList.get(i).getBusNo(), tripTwoList.get(i).getBusNo());
					} else {
						hashm.put(tripTwoList.get(addC).getBusNo(), tripTwoList.get(newC).getBusNo());
						addC = addC + 1;
						newC = newC + 1;
					}
				}

				List<TimeTableDTO> tempBusNoList = new ArrayList<TimeTableDTO>();
				for (TimeTableDTO t : busNoCorrectionList) {
					System.out.println(t.getBusNo() + " " + t.getStartTime() + " " + t.getEndTime());
					tempBusNoList.add(t);
				}

				for (Map.Entry m : hashm.entrySet()) {
					for (TimeTableDTO t : busNoCorrectionList) {
						if (m.getKey().toString().equals(t.getBusNo())) {
							TimeTableDTO tempDto = new TimeTableDTO();
							tempDto.setBusNo(m.getValue().toString());
							tempDto.setStartTime(t.getStartTime());
							tempDto.setEndTime(t.getEndTime());
							tempDto.setTripType(t.getTripType());
							tempDto.setFixedTime(t.isFixedTime());
							tempBusNoList.remove(t);
							tempBusNoList.add(tempDto);
						}
					}
				}
				for (TimeTableDTO t : tempBusNoList) {
					System.out.println(t.getBusNo() + " " + t.getStartTime() + " " + t.getEndTime());
				}
				// origin
				// destination
				List<TimeTableDTO> tripTwoListD = new ArrayList<TimeTableDTO>();
				for (TimeTableDTO d : busNoCorrectionList) {
					if (d.getTripType() != null && !d.getTripType().equals("") && d.getTripType().equals("D")) {
						if (!d.isFixedTime()) {
							tripTwoListD.add(d);
						}
					}
				}
				Collections.sort(tripTwoListD, new Comparator<TimeTableDTO>() {
					@Override
					public int compare(TimeTableDTO u1, TimeTableDTO u2) {
						return Integer.valueOf(u2.getBusNo().substring(abbLen))
								- Integer.valueOf(u1.getBusNo().substring(abbLen));
					}
				});

				Collections.reverse(tripTwoListD);

				HashMap<String, String> hashmD = new HashMap<String, String>();
				int tripsD = tripTwoListD.size() / 2;
				if (tripTwoListD.size() % 2 != 0) {
					tripsD = tripsD + 1;
				}
				int addCD = tripsD;
				int newCD = 0;
				for (int i = 0; i < tripTwoListD.size(); i++) {
					if (i < tripsD) {
						hashmD.put(tripTwoListD.get(i).getBusNo(), tripTwoListD.get(i).getBusNo());
					} else {
						hashmD.put(tripTwoListD.get(addCD).getBusNo(), tripTwoListD.get(newCD).getBusNo());
						addCD = addCD + 1;
						newCD = newCD + 1;
					}
				}

				List<TimeTableDTO> tempBusNoListD = new ArrayList<TimeTableDTO>();
				for (TimeTableDTO t : busNoCorrectionList) {
					tempBusNoListD.add(t);
				}

				for (Map.Entry m : hashmD.entrySet()) {
					for (TimeTableDTO t : busNoCorrectionList) {
						if (m.getKey().toString().equals(t.getBusNo())) {
							TimeTableDTO tempDto = new TimeTableDTO();
							tempDto.setStartTime(t.getStartTime());
							tempDto.setEndTime(t.getEndTime());
							tempDto.setTripType(t.getTripType());
							tempDto.setFixedTime(t.isFixedTime());
							tempDto.setBusNo(m.getValue().toString());
							tempBusNoListD.remove(t);
							tempBusNoListD.add(tempDto);
						}
					}
				}
				// destination
				busNoCorrectionList = tempBusNoListD;

			}
			/**
			 * if trip count=2 divide non fixed bus count send bus twice end
			 **/

			// keep a duplicateBusNum
			originGroupOneList = new ArrayList<TimeTableDTO>();
			for (TimeTableDTO dto : busNoCorrectionList) {
				dto.setDuplicateBusNum(dto.getBusNo());
				dto.setGroup("1");
				dto.setTempStartTime(dto.getStartTime());
				dto.setTempEndTime(dto.getEndTime());
				originGroupOneList.add(dto);
			}
			// keep a duplicateBusNum

		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void detinationToOriginBusSendGroupOne(List<TimeTableDTO> finalTimeListDestination, String driverRestTime,
			String addTimeString, String abbreviation) {
		List<TimeTableDTO> tempDestinationListMissing = new ArrayList<TimeTableDTO>();

		// keep a duplicate temp list of finalTimeListDestination to add and
		// remove DTOs after new busNo added to destinationList
		List<TimeTableDTO> tempDestinationList = new ArrayList<TimeTableDTO>();
		for (TimeTableDTO destDTO : finalTimeListDestination) {
			tempDestinationList.add(destDTO);
		}

		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		try {
			boolean timeSlotAvailable = false;
			String detinationStartTime = null;
			String destinationEndTime = null;
			for (TimeTableDTO oriDTO : originGroupOneList) {
				System.out.println("###########: bus num: " + oriDTO.getBusNo());
				for (TimeTableDTO destDTO : finalTimeListDestination) {

					int gapInMinutes = (int) calculateTotalMinutesForEnd(driverRestTime);
					System.out.println("minutes:" + gapInMinutes);
					LocalTime time = LocalTime.parse(oriDTO.getEndTime());
					time = time.plusMinutes(gapInMinutes);// originDTO end time
															// + driver rest
															// time =
															// destination
															// departure time

					detinationStartTime = time.toString();
					System.out.println("destination start time: " + detinationStartTime);

					Date busRdeTime = timeFormat.parse(addTimeString);// addTimeString
																		// is
																		// the
																		// time
																		// takes
																		// for
																		// bus
																		// ride
					Date destinationStartDate = timeFormat.parse(time.toString());
					long sum = busRdeTime.getTime() + destinationStartDate.getTime();
					destinationEndTime = timeFormat.format(new Date(sum));
					System.out.println("destination end time: " + destinationEndTime);

					if (destDTO.getStartTime().equals(detinationStartTime)
							&& destDTO.getEndTime().equals(destinationEndTime)) {
						if (!destDTO.isFixedTime()) {
							TimeTableDTO tempDTO = new TimeTableDTO();
							tempDTO.setStartTime(detinationStartTime);
							tempDTO.setEndTime(destinationEndTime);
							tempDTO.setBusNo(oriDTO.getBusNo());
							tempDTO.setTripType(oriDTO.getTripType());
							tempDTO.setFixedTime(oriDTO.isFixedTime());
							tempDestinationList.remove(destDTO);// remove DTO
																// with tempBus
																// num
							tempDestinationList.add(tempDTO);// add new DTO for
																// particular
																// time slot
							System.out.println("tempDestinationList.add " + oriDTO.getBusNo());
						} else {

							tempDestinationList.add(destDTO);

							TimeTableDTO tempDTO = new TimeTableDTO();
							tempDTO.setStartTime(detinationStartTime);
							tempDTO.setEndTime(destinationEndTime);
							tempDTO.setBusNo(oriDTO.getBusNo());
							tempDTO.setTripType(oriDTO.getTripType());
							tempDTO.setFixedTime(oriDTO.isFixedTime());
							tempDestinationListMissing.add(tempDTO);
							System.out.println("tempDestinationListMissing.add " + oriDTO.getBusNo());
						}
						timeSlotAvailable = true;
						break;
					} else {
						timeSlotAvailable = false;
					}
				}

				if (!timeSlotAvailable) {
					TimeTableDTO tempDTO = new TimeTableDTO();
					tempDTO.setStartTime(detinationStartTime);
					tempDTO.setEndTime(destinationEndTime);
					tempDTO.setBusNo(oriDTO.getBusNo());
					tempDTO.setTripType(oriDTO.getTripType());
					tempDTO.setFixedTime(oriDTO.isFixedTime());
					tempDestinationList.add(tempDTO);
					System.out.println("tempDestinationList.add " + oriDTO.getBusNo());
				}
			}

			// keep a duplicate temp list of finalTimeListDestination
			List<TimeTableDTO> tempList = new ArrayList<TimeTableDTO>();
			int count = 0;
			boolean added = false;
			for (TimeTableDTO missingData : tempDestinationListMissing) {
				added = false;
				for (TimeTableDTO destinationData : tempDestinationList) {
					if (!destinationData.isFixedTime()) {
						if ((destinationData.getStartTime().compareTo(missingData.getStartTime()) > 0)
								&& (destinationData.getEndTime().compareTo(missingData.getEndTime()) > 0)) {
							TimeTableDTO tempDTO = new TimeTableDTO();
							tempDTO.setStartTime(destinationData.getStartTime());
							tempDTO.setEndTime(destinationData.getEndTime());
							tempDTO.setBusNo(missingData.getBusNo());
							tempDTO.setTripType(missingData.getTripType());
							tempDTO.setFixedTime(missingData.isFixedTime());
							tempList.add(tempDTO);
							count = count + 1;
							added = true;
							break;
						}
					}
				}
				if (!added) {
					System.out.println(
							"get last time range of tempDestinationList plus driver rest time add to tempList");
					System.out.println(missingData.getBusNo());
				}
			}

			List<TimeTableDTO> finalAddedList = new ArrayList<TimeTableDTO>();
			boolean add = false;
			for (TimeTableDTO destinationData : tempDestinationList) {
				add = false;
				for (TimeTableDTO tempData : tempList) {
					if (tempData.getStartTime().equals(destinationData.getStartTime())
							&& tempData.getEndTime().equals(destinationData.getEndTime())) {
						finalAddedList.add(tempData);
						add = true;
						break;
					}
				}
				if (!add) {
					finalAddedList.add(destinationData);
				}
			}

			// some data in tempList arent added to final list check that data
			// and add to final list
			List<TimeTableDTO> tempfinalAddedList = new ArrayList<TimeTableDTO>();
			boolean found = false;
			for (TimeTableDTO tempData : tempList) {
				found = false;
				for (TimeTableDTO destinationData : tempDestinationList) {
					if (tempData.getBusNo().equalsIgnoreCase(destinationData.getBusNo())) {
						found = true;
						break;
					}
				}
				if (!found) {
					tempfinalAddedList.add(tempData);
				}
			}

			finalAddedList.addAll(tempfinalAddedList);
			// some data in tempList arent added to final list check that data
			// and add to final list

			/** remove duplicates in list **/
			List<TimeTableDTO> temp = new ArrayList<TimeTableDTO>();
			for (TimeTableDTO t : finalAddedList) {
				temp.add(t);
			}
			System.out.println("finalAddedList size 1: " + finalAddedList.size());
			int counti = 0;
			for (TimeTableDTO d : temp) {
				counti = 0;
				Iterator<TimeTableDTO> it = finalAddedList.iterator();
				while (it.hasNext()) {
					TimeTableDTO dto = it.next();
					if (dto.getBusNo() != null && !dto.getBusNo().isEmpty() && d.getBusNo() != null
							&& !d.getBusNo().isEmpty() && dto.getBusNo().equals(d.getBusNo())) {
						counti = counti + 1;
						if (counti > 1) {
							it.remove();
						}
					} else {
						if (dto.getBusNo() == null || dto.getBusNo().isEmpty()
								|| dto.getBusNo().trim().equalsIgnoreCase("")) {
							it.remove();
						}
					}
				}
			}

			System.out.println("finalAddedList size 2: " + finalAddedList.size());
			/** remove duplicates in list **/

			/** correction of bus numbers start **/
			int busNoSuffix = 0;// abbreviation
			List<TimeTableDTO> busNoCorrectionList = new ArrayList<TimeTableDTO>();
			List<String> busNoStringList = new ArrayList<String>();
			List<String> currBusNoList = new ArrayList<String>();
			for (TimeTableDTO data : finalAddedList) {
				System.out.println("************************* " + data.getBusNo() + " " + data.getStartTime() + " "
						+ data.getEndTime());
				busNoCorrectionList.add(data);
				if (data.getBusNo().substring(0, abbreviation.length()).equals(abbreviation)) {
					busNoSuffix = busNoSuffix + 1;
					busNoStringList.add(abbreviation + busNoSuffix);
					currBusNoList.add(data.getBusNo());
				}
			}

			/** correction list **/
			HashMap<String, String> hm = new HashMap<String, String>();
			Collections.sort(currBusNoList);
			final int abbLen = abbreviation.length();
			Collections.sort(currBusNoList, new Comparator<String>() {
				@Override
				public int compare(String u1, String u2) {
					return Integer.valueOf(u2.substring(abbLen)) - Integer.valueOf(u1.substring(abbLen));
				}
			});

			Collections.reverse(currBusNoList);

			int sortCount = 0;
			for (String s : busNoStringList) {
				hm.put(s, currBusNoList.get(sortCount));
				sortCount = sortCount + 1;
			}
			/** correction list **/
			for (Map.Entry m : hm.entrySet()) {
				for (TimeTableDTO data : finalAddedList) {
					if (m.getValue().equals(data.getBusNo())) {
						TimeTableDTO addDTO = new TimeTableDTO();
						addDTO.setStartTime(data.getStartTime());
						addDTO.setEndTime(data.getEndTime());
						addDTO.setTripType(data.getTripType());
						addDTO.setFixedTime(data.isFixedTime());
						addDTO.setBusNo(m.getKey().toString());
						busNoCorrectionList.add(addDTO);
						busNoCorrectionList.remove(data);

					}
				}
			}
			/** correction of bus numbers end **/

			/**
			 * if trip count=2 divide non fixed bus count send bus twice start
			 **/
			if (coupling) {
				// destination
				List<TimeTableDTO> tripTwoList = new ArrayList<TimeTableDTO>();
				for (TimeTableDTO d : busNoCorrectionList) {
					if (d.getTripType() != null && !d.getTripType().equals("") && d.getTripType().equals("D")) {
						if (!d.isFixedTime()) {
							tripTwoList.add(d);
						}
					}
				}
				Collections.sort(tripTwoList, new Comparator<TimeTableDTO>() {
					@Override
					public int compare(TimeTableDTO u1, TimeTableDTO u2) {
						return Integer.valueOf(u2.getBusNo().substring(abbLen))
								- Integer.valueOf(u1.getBusNo().substring(abbLen));
					}
				});

				Collections.reverse(tripTwoList);

				HashMap<String, String> hashm = new HashMap<String, String>();
				int trips = tripTwoList.size() / 2;
				if (tripTwoList.size() % 2 != 0) {
					trips = trips + 1;
				}
				int addC = trips;
				int newC = 0;
				for (int i = 0; i < tripTwoList.size(); i++) {
					if (i < trips) {
						hashm.put(tripTwoList.get(i).getBusNo(), tripTwoList.get(i).getBusNo());
					} else {
						hashm.put(tripTwoList.get(addC).getBusNo(), tripTwoList.get(newC).getBusNo());
						addC = addC + 1;
						newC = newC + 1;
					}
				}

				List<TimeTableDTO> tempBusNoList = new ArrayList<TimeTableDTO>();
				for (TimeTableDTO t : busNoCorrectionList) {
					tempBusNoList.add(t);
				}

				for (Map.Entry m : hashm.entrySet()) {
					for (TimeTableDTO t : busNoCorrectionList) {
						if (m.getKey().toString().equals(t.getBusNo())) {
							TimeTableDTO tempDto = new TimeTableDTO();
							tempDto.setStartTime(t.getStartTime());
							tempDto.setEndTime(t.getEndTime());
							tempDto.setTripType(t.getTripType());
							tempDto.setFixedTime(t.isFixedTime());
							tempDto.setBusNo(m.getValue().toString());
							tempBusNoList.remove(t);
							tempBusNoList.add(tempDto);
						}
					}
				}
				// destination
				// origin
				List<TimeTableDTO> tripTwoListO = new ArrayList<TimeTableDTO>();
				for (TimeTableDTO d : busNoCorrectionList) {
					if (d.getTripType() != null && !d.getTripType().equals("") && d.getTripType().equals("O")) {
						if (!d.isFixedTime()) {
							tripTwoListO.add(d);
						}
					}
				}
				Collections.sort(tripTwoListO, new Comparator<TimeTableDTO>() {
					@Override
					public int compare(TimeTableDTO u1, TimeTableDTO u2) {
						return Integer.valueOf(u2.getBusNo().substring(abbLen))
								- Integer.valueOf(u1.getBusNo().substring(abbLen));
					}
				});

				Collections.reverse(tripTwoListO);

				HashMap<String, String> hashmO = new HashMap<String, String>();
				int tripsO = tripTwoListO.size() / 2;
				if (tripTwoListO.size() % 2 != 0) {
					tripsO = tripsO + 1;
				}
				int addCO = tripsO;
				int newCO = 0;
				for (int i = 0; i < tripTwoListO.size(); i++) {
					if (i < tripsO) {
						hashmO.put(tripTwoListO.get(i).getBusNo(), tripTwoListO.get(i).getBusNo());
					} else {
						hashmO.put(tripTwoListO.get(addCO).getBusNo(), tripTwoListO.get(newCO).getBusNo());
						addCO = addCO + 1;
						newCO = newCO + 1;
					}
				}

				List<TimeTableDTO> tempBusNoListO = new ArrayList<TimeTableDTO>();
				for (TimeTableDTO t : busNoCorrectionList) {
					tempBusNoListO.add(t);
				}

				for (Map.Entry m : hashmO.entrySet()) {
					for (TimeTableDTO t : busNoCorrectionList) {
						if (m.getKey().toString().equals(t.getBusNo())) {
							TimeTableDTO tempDto = new TimeTableDTO();
							tempDto.setStartTime(t.getStartTime());
							tempDto.setEndTime(t.getEndTime());
							tempDto.setTripType(t.getTripType());
							tempDto.setFixedTime(t.isFixedTime());
							tempDto.setBusNo(m.getValue().toString());
							tempBusNoListO.remove(t);
							tempBusNoListO.add(tempDto);
						}
					}
				}
				// origin

				busNoCorrectionList = tempBusNoList;
			}
			/**
			 * if trip count=2 divide non fixed bus count send bus twice end
			 **/

			// keep a duplicateBusNum
			destinationGroupOneList = new ArrayList<TimeTableDTO>();
			for (TimeTableDTO dto : busNoCorrectionList) {
				dto.setDuplicateBusNum(dto.getBusNo());
				dto.setGroup("1");
				dto.setTempStartTime(dto.getStartTime());
				dto.setTempEndTime(dto.getEndTime());
				destinationGroupOneList.add(dto);
			}
			// keep a duplicateBusNum

		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void origintoDetinationBusSendGroupTwo(List<TimeTableDTO> finalTimeListOrigin, String driverRestTime,
			String addTimeString, String abbreviation) {
		List<TimeTableDTO> tempOriginListMissing = new ArrayList<TimeTableDTO>();

		// keep a duplicate temp list of finalTimeListDestination to add and
		// remove DTOs after new busNo added to destinationList
		List<TimeTableDTO> tempOriginList = new ArrayList<TimeTableDTO>();
		for (TimeTableDTO destDTO : finalTimeListOrigin) {
			tempOriginList.add(destDTO);
		}

		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		try {
			boolean timeSlotAvailable = false;
			String originStartTime = null;
			String originEndTime = null;
			for (TimeTableDTO oriDTO : destinationGroupTwoList) {
				for (TimeTableDTO destDTO : finalTimeListOrigin) {

					int gapInMinutes = (int) calculateTotalMinutesForEnd(driverRestTime);
					System.out.println("minutes:" + gapInMinutes);
					LocalTime time = LocalTime.parse(oriDTO.getEndTime());
					time = time.plusMinutes(gapInMinutes);// originDTO end time
															// + driver rest
															// time =
															// destination
															// departure time

					originStartTime = time.toString();
					System.out.println("destination start time: " + originStartTime);

					Date busRdeTime = timeFormat.parse(addTimeString);// addTimeString
																		// is
																		// the
																		// time
																		// takes
																		// for
																		// bus
																		// ride
					Date destinationStartDate = timeFormat.parse(time.toString());
					long sum = busRdeTime.getTime() + destinationStartDate.getTime();
					originEndTime = timeFormat.format(new Date(sum));
					System.out.println("destination end time: " + originEndTime);

					if (destDTO.getStartTime().equals(originStartTime) && destDTO.getEndTime().equals(originEndTime)) {
						if (!destDTO.isFixedTime()) {
							TimeTableDTO tempDTO = new TimeTableDTO();
							tempDTO.setStartTime(originStartTime);
							tempDTO.setEndTime(originEndTime);
							tempDTO.setBusNo(oriDTO.getBusNo());
							tempDTO.setTripType(oriDTO.getTripType());
							tempDTO.setFixedTime(oriDTO.isFixedTime());

							tempOriginList.remove(destDTO);// remove DTO with
															// tempBus num
							tempOriginList.add(tempDTO);// add new DTO for
														// particular time slot
						} else {

							tempOriginList.add(destDTO);

							TimeTableDTO tempDTO = new TimeTableDTO();
							tempDTO.setStartTime(originStartTime);
							tempDTO.setEndTime(originEndTime);
							tempDTO.setBusNo(oriDTO.getBusNo());
							tempDTO.setTripType(oriDTO.getTripType());
							tempDTO.setFixedTime(oriDTO.isFixedTime());
							tempOriginListMissing.add(tempDTO);
						}
						timeSlotAvailable = true;
						break;
					} else {
						timeSlotAvailable = false;
					}
				}

				if (!timeSlotAvailable) {
					TimeTableDTO tempDTO = new TimeTableDTO();
					tempDTO.setStartTime(originStartTime);
					tempDTO.setEndTime(originEndTime);
					tempDTO.setBusNo(oriDTO.getBusNo());
					tempDTO.setTripType(oriDTO.getTripType());
					tempDTO.setFixedTime(oriDTO.isFixedTime());
					tempOriginList.add(tempDTO);
				}
			}

			// keep a duplicate temp list of finalTimeListOrigin
			List<TimeTableDTO> tempList = new ArrayList<TimeTableDTO>();
			int count = 0;
			boolean added = false;
			for (TimeTableDTO missingData : tempOriginListMissing) {
				added = false;
				for (TimeTableDTO destinationData : tempOriginList) {
					if (!destinationData.isFixedTime()) {
						if ((destinationData.getStartTime().compareTo(missingData.getStartTime()) > 0)
								&& (destinationData.getEndTime().compareTo(missingData.getEndTime()) > 0)) {
							TimeTableDTO tempDTO = new TimeTableDTO();
							tempDTO.setStartTime(destinationData.getStartTime());
							tempDTO.setEndTime(destinationData.getEndTime());
							tempDTO.setBusNo(missingData.getBusNo());
							tempDTO.setTripType(missingData.getTripType());
							tempDTO.setFixedTime(missingData.isFixedTime());
							tempList.add(tempDTO);
							count = count + 1;
							added = true;
							break;
						}
					}
				}
				if (!added) {
					System.out.println(
							"get last time range of tempDestinationList plus driver rest time add to tempList");
				}
			}

			List<TimeTableDTO> finalAddedList = new ArrayList<TimeTableDTO>();
			boolean add = false;
			for (TimeTableDTO destinationData : tempOriginList) {
				add = false;
				for (TimeTableDTO tempData : tempList) {
					if (tempData.getStartTime().equals(destinationData.getStartTime())
							&& tempData.getEndTime().equals(destinationData.getEndTime())) {
						finalAddedList.add(tempData);
						add = true;
						break;
					}
				}
				if (!add) {
					finalAddedList.add(destinationData);
				}
			}

			// some data in temp list are not added to final list add that data
			List<TimeTableDTO> tempfinalAddedList = new ArrayList<TimeTableDTO>();
			boolean found = false;
			for (TimeTableDTO tempData : tempList) {
				found = false;
				for (TimeTableDTO destinationData : tempOriginList) {
					if (tempData.getBusNo().equalsIgnoreCase(destinationData.getBusNo())) {
						found = true;
						break;
					}
				}
				if (!found) {
					tempfinalAddedList.add(tempData);
				}
			}

			finalAddedList.addAll(tempfinalAddedList);
			// some data in temp list are not added to final list add that data

			/** remove duplicates in list **/
			List<TimeTableDTO> temp = new ArrayList<TimeTableDTO>();
			for (TimeTableDTO t : finalAddedList) {
				temp.add(t);
			}
			System.out.println("finalAddedList size 1: " + finalAddedList.size());
			int counti = 0;
			for (TimeTableDTO d : temp) {
				counti = 0;
				Iterator<TimeTableDTO> it = finalAddedList.iterator();
				while (it.hasNext()) {
					TimeTableDTO dto = it.next();
					if (dto.getBusNo() != null && !dto.getBusNo().isEmpty() && d.getBusNo() != null
							&& !d.getBusNo().isEmpty() && dto.getBusNo().equals(d.getBusNo())) {
						counti = counti + 1;
						if (counti > 1) {
							it.remove();
						}
					} else {
						if (dto.getBusNo() == null || dto.getBusNo().isEmpty()
								|| dto.getBusNo().trim().equalsIgnoreCase("")) {
							it.remove();
						}
					}
				}
			}

			System.out.println("finalAddedList size 2: " + finalAddedList.size());
			/** remove duplicates in list **/

			/** correction of bus numbers start **/
			int busNoSuffix = 0;// abbreviation
			List<TimeTableDTO> busNoCorrectionList = new ArrayList<TimeTableDTO>();
			List<String> busNoStringList = new ArrayList<String>();
			List<String> currBusNoList = new ArrayList<String>();
			for (TimeTableDTO data : finalAddedList) {
				System.out.println("************************* " + data.getBusNo() + " " + data.getStartTime() + " "
						+ data.getEndTime());
				busNoCorrectionList.add(data);
				if (data.getBusNo().substring(0, abbreviation.length()).equals(abbreviation)) {
					busNoSuffix = busNoSuffix + 1;
					busNoStringList.add(abbreviation + busNoSuffix);
					currBusNoList.add(data.getBusNo());
				}
			}

			/** correction list **/
			HashMap<String, String> hm = new HashMap<String, String>();
			Collections.sort(currBusNoList);
			final int abbLen = abbreviation.length();
			Collections.sort(currBusNoList, new Comparator<String>() {
				@Override
				public int compare(String u1, String u2) {
					return Integer.valueOf(u2.substring(abbLen)) - Integer.valueOf(u1.substring(abbLen));
				}
			});

			Collections.reverse(currBusNoList);

			int sortCount = 0;
			for (String s : busNoStringList) {
				hm.put(s, currBusNoList.get(sortCount));
				sortCount = sortCount + 1;
			}
			/** correction list **/

			for (Map.Entry m : hm.entrySet()) {
				for (TimeTableDTO data : finalAddedList) {
					if (m.getValue().equals(data.getBusNo())) {
						TimeTableDTO addDTO = new TimeTableDTO();
						addDTO.setStartTime(data.getStartTime());
						addDTO.setEndTime(data.getEndTime());
						addDTO.setTripType(data.getTripType());
						addDTO.setFixedTime(data.isFixedTime());
						addDTO.setBusNo(m.getKey().toString());
						busNoCorrectionList.add(addDTO);
						busNoCorrectionList.remove(data);
					}
				}
			}
			/** correction of bus numbers end **/

			/**
			 * if trip count=2 divide non fixed bus count send bus twice start
			 **/
			if (coupling) {
				// origin
				List<TimeTableDTO> tripTwoList = new ArrayList<TimeTableDTO>();
				for (TimeTableDTO d : busNoCorrectionList) {
					if (d.getTripType() != null && !d.getTripType().equals("") && d.getTripType().equals("O")) {
						if (!d.isFixedTime()) {
							tripTwoList.add(d);
						}
					}
				}
				Collections.sort(tripTwoList, new Comparator<TimeTableDTO>() {
					@Override
					public int compare(TimeTableDTO u1, TimeTableDTO u2) {
						return Integer.valueOf(u2.getBusNo().substring(abbLen))
								- Integer.valueOf(u1.getBusNo().substring(abbLen));
					}
				});

				Collections.reverse(tripTwoList);

				HashMap<String, String> hashm = new HashMap<String, String>();
				int trips = tripTwoList.size() / 2;
				if (tripTwoList.size() % 2 != 0) {
					trips = trips + 1;
				}
				int addC = trips;
				int newC = 0;
				for (int i = 0; i < tripTwoList.size(); i++) {
					if (i < trips) {
						hashm.put(tripTwoList.get(i).getBusNo(), tripTwoList.get(i).getBusNo());
					} else {
						hashm.put(tripTwoList.get(addC).getBusNo(), tripTwoList.get(newC).getBusNo());
						addC = addC + 1;
						newC = newC + 1;
					}
				}

				List<TimeTableDTO> tempBusNoList = new ArrayList<TimeTableDTO>();
				for (TimeTableDTO t : busNoCorrectionList) {
					System.out.println(t.getBusNo() + " " + t.getStartTime() + " " + t.getEndTime());
					tempBusNoList.add(t);
				}

				for (Map.Entry m : hashm.entrySet()) {
					for (TimeTableDTO t : busNoCorrectionList) {
						if (m.getKey().toString().equals(t.getBusNo())) {
							TimeTableDTO tempDto = new TimeTableDTO();
							tempDto.setStartTime(t.getStartTime());
							tempDto.setEndTime(t.getEndTime());
							tempDto.setTripType(t.getTripType());
							tempDto.setFixedTime(t.isFixedTime());
							tempDto.setBusNo(m.getValue().toString());
							tempBusNoList.remove(t);
							tempBusNoList.add(tempDto);
						}
					}
				}
				for (TimeTableDTO t : tempBusNoList) {
					System.out.println(t.getBusNo() + " " + t.getStartTime() + " " + t.getEndTime());
				}
				// origin
				// destination
				List<TimeTableDTO> tripTwoListD = new ArrayList<TimeTableDTO>();
				for (TimeTableDTO d : busNoCorrectionList) {
					if (d.getTripType() != null && !d.getTripType().equals("") && d.getTripType().equals("D")) {
						if (!d.isFixedTime()) {
							tripTwoListD.add(d);
						}
					}
				}
				Collections.sort(tripTwoListD, new Comparator<TimeTableDTO>() {
					@Override
					public int compare(TimeTableDTO u1, TimeTableDTO u2) {
						return Integer.valueOf(u2.getBusNo().substring(abbLen))
								- Integer.valueOf(u1.getBusNo().substring(abbLen));
					}
				});

				Collections.reverse(tripTwoListD);

				HashMap<String, String> hashmD = new HashMap<String, String>();
				int tripsD = tripTwoListD.size() / 2;
				if (tripTwoListD.size() % 2 != 0) {
					tripsD = tripsD + 1;
				}
				int addCD = tripsD;
				int newCD = 0;
				for (int i = 0; i < tripTwoListD.size(); i++) {
					if (i < tripsD) {
						hashmD.put(tripTwoListD.get(i).getBusNo(), tripTwoListD.get(i).getBusNo());
					} else {
						hashmD.put(tripTwoListD.get(addCD).getBusNo(), tripTwoListD.get(newCD).getBusNo());
						addCD = addCD + 1;
						newCD = newCD + 1;
					}
				}

				List<TimeTableDTO> tempBusNoListD = new ArrayList<TimeTableDTO>();
				for (TimeTableDTO t : busNoCorrectionList) {
					tempBusNoListD.add(t);
				}

				for (Map.Entry m : hashmD.entrySet()) {
					for (TimeTableDTO t : busNoCorrectionList) {
						if (m.getKey().toString().equals(t.getBusNo())) {
							TimeTableDTO tempDto = new TimeTableDTO();
							tempDto.setStartTime(t.getStartTime());
							tempDto.setEndTime(t.getEndTime());
							tempDto.setTripType(t.getTripType());
							tempDto.setFixedTime(t.isFixedTime());
							tempDto.setBusNo(m.getValue().toString());
							tempBusNoListD.remove(t);
							tempBusNoListD.add(tempDto);
						}
					}
				}
				// destination
				busNoCorrectionList = tempBusNoListD;
			}
			/**
			 * if trip count=2 divide non fixed bus count send bus twice end
			 **/

			// keep a duplicateBusNum
			originGroupTwoList = new ArrayList<TimeTableDTO>();
			for (TimeTableDTO dto : busNoCorrectionList) {
				dto.setDuplicateBusNum(dto.getBusNo());
				dto.setGroup("2");
				dto.setTempStartTime(dto.getStartTime());
				dto.setTempEndTime(dto.getEndTime());
				originGroupTwoList.add(dto);
			}
			// keep a duplicateBusNum

		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void detinationToOriginBusSendGroupTwo(List<TimeTableDTO> finalTimeListDestination, String driverRestTime,
			String addTimeString, String abbreviation) {
		List<TimeTableDTO> tempDestinationListMissing = new ArrayList<TimeTableDTO>();

		// keep a duplicate temp list of finalTimeListDestination to add and
		// remove DTOs after new busNo added to destinationList
		List<TimeTableDTO> tempDestinationList = new ArrayList<TimeTableDTO>();
		for (TimeTableDTO destDTO : finalTimeListDestination) {
			tempDestinationList.add(destDTO);
		}

		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		try {
			boolean timeSlotAvailable = false;
			String detinationStartTime = null;
			String destinationEndTime = null;
			for (TimeTableDTO oriDTO : originGroupTwoList) {
				for (TimeTableDTO destDTO : finalTimeListDestination) {

					int gapInMinutes = (int) calculateTotalMinutesForEnd(driverRestTime);
					System.out.println("minutes:" + gapInMinutes);
					LocalTime time = LocalTime.parse(oriDTO.getEndTime());
					time = time.plusMinutes(gapInMinutes);// originDTO end time
															// + driver rest
															// time =
															// destination
															// departure time

					detinationStartTime = time.toString();
					System.out.println("destination start time: " + detinationStartTime);

					Date busRdeTime = timeFormat.parse(addTimeString);// addTimeString
																		// is
																		// the
																		// time
																		// takes
																		// for
																		// bus
																		// ride
					Date destinationStartDate = timeFormat.parse(time.toString());
					long sum = busRdeTime.getTime() + destinationStartDate.getTime();
					destinationEndTime = timeFormat.format(new Date(sum));
					System.out.println("destination end time: " + destinationEndTime);

					if (destDTO.getStartTime().equals(detinationStartTime)
							&& destDTO.getEndTime().equals(destinationEndTime)) {
						if (!destDTO.isFixedTime()) {
							TimeTableDTO tempDTO = new TimeTableDTO();
							tempDTO.setStartTime(detinationStartTime);
							tempDTO.setEndTime(destinationEndTime);
							tempDTO.setBusNo(oriDTO.getBusNo());
							tempDTO.setTripType(oriDTO.getTripType());
							tempDTO.setFixedTime(oriDTO.isFixedTime());
							tempDestinationList.remove(destDTO);// remove DTO
																// with tempBus
																// num
							tempDestinationList.add(tempDTO);// add new DTO for
																// particular
																// time slot
						} else {

							tempDestinationList.add(destDTO);

							TimeTableDTO tempDTO = new TimeTableDTO();
							tempDTO.setStartTime(detinationStartTime);
							tempDTO.setEndTime(destinationEndTime);
							tempDTO.setBusNo(oriDTO.getBusNo());
							tempDTO.setTripType(oriDTO.getTripType());
							tempDTO.setFixedTime(oriDTO.isFixedTime());
							tempDestinationListMissing.add(tempDTO);
						}
						timeSlotAvailable = true;
						break;
					} else {
						timeSlotAvailable = false;
					}
				}

				if (!timeSlotAvailable) {
					TimeTableDTO tempDTO = new TimeTableDTO();
					tempDTO.setStartTime(detinationStartTime);
					tempDTO.setEndTime(destinationEndTime);
					tempDTO.setBusNo(oriDTO.getBusNo());
					tempDTO.setTripType(oriDTO.getTripType());
					tempDTO.setFixedTime(oriDTO.isFixedTime());
					tempDestinationList.add(tempDTO);
				}
			}

			// keep a duplicate temp list of finalTimeListDestination
			List<TimeTableDTO> tempList = new ArrayList<TimeTableDTO>();
			int count = 0;
			boolean added = false;
			for (TimeTableDTO missingData : tempDestinationListMissing) {
				added = false;
				for (TimeTableDTO destinationData : tempDestinationList) {
					if (!destinationData.isFixedTime()) {
						if ((destinationData.getStartTime().compareTo(missingData.getStartTime()) > 0)
								&& (destinationData.getEndTime().compareTo(missingData.getEndTime()) > 0)) {
							TimeTableDTO tempDTO = new TimeTableDTO();
							tempDTO.setStartTime(destinationData.getStartTime());
							tempDTO.setEndTime(destinationData.getEndTime());
							tempDTO.setBusNo(missingData.getBusNo());
							tempDTO.setTripType(missingData.getTripType());
							tempDTO.setFixedTime(missingData.isFixedTime());
							tempList.add(tempDTO);
							count = count + 1;
							added = true;
							break;
						}
					}
				}
				if (!added) {
					System.out.println(
							"get last time range of tempDestinationList plus driver rest time add to tempList");
				}
			}

			List<TimeTableDTO> finalAddedList = new ArrayList<TimeTableDTO>();
			boolean add = false;
			for (TimeTableDTO destinationData : tempDestinationList) {
				add = false;
				for (TimeTableDTO tempData : tempList) {
					if (tempData.getStartTime().equals(destinationData.getStartTime())
							&& tempData.getEndTime().equals(destinationData.getEndTime())) {
						finalAddedList.add(tempData);
						add = true;
						break;
					}
				}
				if (!add) {
					finalAddedList.add(destinationData);
				}
			}

			// some data in tempList arent added to final list check that data
			// and add to final list
			List<TimeTableDTO> tempfinalAddedList = new ArrayList<TimeTableDTO>();
			boolean found = false;
			for (TimeTableDTO tempData : tempList) {
				found = false;
				for (TimeTableDTO destinationData : tempDestinationList) {
					if (tempData.getBusNo().equalsIgnoreCase(destinationData.getBusNo())) {
						found = true;
						break;
					}
				}
				if (!found) {
					tempfinalAddedList.add(tempData);
				}
			}

			finalAddedList.addAll(tempfinalAddedList);
			// some data in tempList arent added to final list check that data
			// and add to final list

			/** remove duplicates in list **/
			List<TimeTableDTO> temp = new ArrayList<TimeTableDTO>();
			for (TimeTableDTO t : finalAddedList) {
				temp.add(t);
			}
			System.out.println("finalAddedList size 1: " + finalAddedList.size());
			int counti = 0;
			for (TimeTableDTO d : temp) {
				counti = 0;
				Iterator<TimeTableDTO> it = finalAddedList.iterator();
				while (it.hasNext()) {
					TimeTableDTO dto = it.next();
					if (dto.getBusNo() != null && !dto.getBusNo().isEmpty() && d.getBusNo() != null
							&& !d.getBusNo().isEmpty() && dto.getBusNo().equals(d.getBusNo())) {
						counti = counti + 1;
						if (counti > 1) {
							it.remove();
						}
					} else {
						if (dto.getBusNo() == null || dto.getBusNo().isEmpty()
								|| dto.getBusNo().trim().equalsIgnoreCase("")) {
							it.remove();
						}
					}
				}
			}

			System.out.println("finalAddedList size 2: " + finalAddedList.size());
			/** remove duplicates in list **/

			/** correction of bus numbers start **/
			int busNoSuffix = 0;// abbreviation
			List<TimeTableDTO> busNoCorrectionList = new ArrayList<TimeTableDTO>();
			List<String> busNoStringList = new ArrayList<String>();
			List<String> currBusNoList = new ArrayList<String>();
			for (TimeTableDTO data : finalAddedList) {
				System.out.println("************************* " + data.getBusNo() + " " + data.getStartTime() + " "
						+ data.getEndTime());
				busNoCorrectionList.add(data);
				if (data.getBusNo().substring(0, abbreviation.length()).equals(abbreviation)) {
					busNoSuffix = busNoSuffix + 1;
					busNoStringList.add(abbreviation + busNoSuffix);
					currBusNoList.add(data.getBusNo());
				}
			}

			/** correction list **/
			HashMap<String, String> hm = new HashMap<String, String>();
			Collections.sort(currBusNoList);
			final int abbLen = abbreviation.length();
			Collections.sort(currBusNoList, new Comparator<String>() {
				@Override
				public int compare(String u1, String u2) {
					return Integer.valueOf(u2.substring(abbLen)) - Integer.valueOf(u1.substring(abbLen));
				}
			});

			Collections.reverse(currBusNoList);

			int sortCount = 0;
			for (String s : busNoStringList) {
				hm.put(s, currBusNoList.get(sortCount));
				sortCount = sortCount + 1;
			}
			/** correction list **/

			for (Map.Entry m : hm.entrySet()) {
				for (TimeTableDTO data : finalAddedList) {
					if (m.getValue().equals(data.getBusNo())) {
						TimeTableDTO addDTO = new TimeTableDTO();
						addDTO.setStartTime(data.getStartTime());
						addDTO.setEndTime(data.getEndTime());
						addDTO.setBusNo(m.getKey().toString());
						addDTO.setFixedTime(data.isFixedTime());
						busNoCorrectionList.add(addDTO);
						busNoCorrectionList.remove(data);
					}
				}
			}
			/** correction of bus numbers end **/
			/**
			 * if trip count=2 divide non fixed bus count send bus twice start
			 **/
			if (coupling) {
				// destination
				List<TimeTableDTO> tripTwoList = new ArrayList<TimeTableDTO>();
				for (TimeTableDTO d : busNoCorrectionList) {
					if (d.getTripType() != null && !d.getTripType().equals("") && d.getTripType().equals("D")) {
						if (!d.isFixedTime()) {
							tripTwoList.add(d);
						}
					}
				}
				Collections.sort(tripTwoList, new Comparator<TimeTableDTO>() {
					@Override
					public int compare(TimeTableDTO u1, TimeTableDTO u2) {
						return Integer.valueOf(u2.getBusNo().substring(abbLen))
								- Integer.valueOf(u1.getBusNo().substring(abbLen));
					}
				});

				Collections.reverse(tripTwoList);

				HashMap<String, String> hashm = new HashMap<String, String>();
				int trips = tripTwoList.size() / 2;
				if (tripTwoList.size() % 2 != 0) {
					trips = trips + 1;
				}
				int addC = trips;
				int newC = 0;
				for (int i = 0; i < tripTwoList.size(); i++) {
					if (i < trips) {
						hashm.put(tripTwoList.get(i).getBusNo(), tripTwoList.get(i).getBusNo());
					} else {
						hashm.put(tripTwoList.get(addC).getBusNo(), tripTwoList.get(newC).getBusNo());
						addC = addC + 1;
						newC = newC + 1;
					}
				}

				List<TimeTableDTO> tempBusNoList = new ArrayList<TimeTableDTO>();
				for (TimeTableDTO t : busNoCorrectionList) {
					tempBusNoList.add(t);
				}

				for (Map.Entry m : hashm.entrySet()) {
					for (TimeTableDTO t : busNoCorrectionList) {
						if (m.getKey().toString().equals(t.getBusNo())) {
							TimeTableDTO tempDto = new TimeTableDTO();
							tempDto.setStartTime(t.getStartTime());
							tempDto.setEndTime(t.getEndTime());
							tempDto.setTripType(t.getTripType());
							tempDto.setFixedTime(t.isFixedTime());
							tempDto.setBusNo(m.getValue().toString());
							tempBusNoList.remove(t);
							tempBusNoList.add(tempDto);
						}
					}
				}
				// destination
				// origin
				List<TimeTableDTO> tripTwoListO = new ArrayList<TimeTableDTO>();
				for (TimeTableDTO d : busNoCorrectionList) {
					if (d.getTripType() != null && !d.getTripType().equals("") && d.getTripType().equals("O")) {
						if (!d.isFixedTime()) {
							tripTwoListO.add(d);
						}
					}
				}
				Collections.sort(tripTwoListO, new Comparator<TimeTableDTO>() {
					@Override
					public int compare(TimeTableDTO u1, TimeTableDTO u2) {
						return Integer.valueOf(u2.getBusNo().substring(abbLen))
								- Integer.valueOf(u1.getBusNo().substring(abbLen));
					}
				});

				Collections.reverse(tripTwoListO);

				HashMap<String, String> hashmO = new HashMap<String, String>();
				int tripsO = tripTwoListO.size() / 2;
				if (tripTwoListO.size() % 2 != 0) {
					tripsO = tripsO + 1;
				}
				int addCO = tripsO;
				int newCO = 0;
				for (int i = 0; i < tripTwoListO.size(); i++) {
					if (i < tripsO) {
						hashmO.put(tripTwoListO.get(i).getBusNo(), tripTwoListO.get(i).getBusNo());
					} else {
						hashmO.put(tripTwoListO.get(addCO).getBusNo(), tripTwoListO.get(newCO).getBusNo());
						addCO = addCO + 1;
						newCO = newCO + 1;
					}
				}

				List<TimeTableDTO> tempBusNoListO = new ArrayList<TimeTableDTO>();
				for (TimeTableDTO t : busNoCorrectionList) {
					tempBusNoListO.add(t);
				}

				for (Map.Entry m : hashmO.entrySet()) {
					for (TimeTableDTO t : busNoCorrectionList) {
						if (m.getKey().toString().equals(t.getBusNo())) {
							TimeTableDTO tempDto = new TimeTableDTO();
							tempDto.setStartTime(t.getStartTime());
							tempDto.setEndTime(t.getEndTime());
							tempDto.setTripType(t.getTripType());
							tempDto.setFixedTime(t.isFixedTime());
							tempDto.setBusNo(m.getValue().toString());
							tempBusNoListO.remove(t);
							tempBusNoListO.add(tempDto);
						}
					}
				}
				// origin

				busNoCorrectionList = tempBusNoList;
			}
			/**
			 * if trip count=2 divide non fixed bus count send bus twice end
			 **/

			// keep a duplicateBusNum
			destinationGroupTwoList = new ArrayList<TimeTableDTO>();
			for (TimeTableDTO dto : busNoCorrectionList) {
				dto.setDuplicateBusNum(dto.getBusNo());
				dto.setGroup("2");
				dto.setTempStartTime(dto.getStartTime());
				dto.setTempEndTime(dto.getEndTime());
				destinationGroupTwoList.add(dto);
			}
			// keep a duplicateBusNum

		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void origintoDetinationBusSendGroupThree(List<TimeTableDTO> finalTimeListOrigin, String driverRestTime,
			String addTimeString, String abbreviation) {
		List<TimeTableDTO> tempOriginListMissing = new ArrayList<TimeTableDTO>();

		// keep a duplicate temp list of finalTimeListDestination to add and
		// remove DTOs after new busNo added to destinationList
		List<TimeTableDTO> tempOriginList = new ArrayList<TimeTableDTO>();
		for (TimeTableDTO destDTO : finalTimeListOrigin) {
			tempOriginList.add(destDTO);
		}

		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		try {
			boolean timeSlotAvailable = false;
			String originStartTime = null;
			String originEndTime = null;
			for (TimeTableDTO oriDTO : destinationGroupThreeList) {
				for (TimeTableDTO destDTO : finalTimeListOrigin) {

					int gapInMinutes = (int) calculateTotalMinutesForEnd(driverRestTime);
					System.out.println("minutes:" + gapInMinutes);
					LocalTime time = LocalTime.parse(oriDTO.getEndTime());
					time = time.plusMinutes(gapInMinutes);// originDTO end time
															// + driver rest
															// time =
															// destination
															// departure time

					originStartTime = time.toString();
					System.out.println("destination start time: " + originStartTime);

					Date busRdeTime = timeFormat.parse(addTimeString);// addTimeString
																		// is
																		// the
																		// time
																		// takes
																		// for
																		// bus
																		// ride
					Date destinationStartDate = timeFormat.parse(time.toString());
					long sum = busRdeTime.getTime() + destinationStartDate.getTime();
					originEndTime = timeFormat.format(new Date(sum));
					System.out.println("destination end time: " + originEndTime);

					if (destDTO.getStartTime().equals(originStartTime) && destDTO.getEndTime().equals(originEndTime)) {
						if (!destDTO.isFixedTime()) {
							TimeTableDTO tempDTO = new TimeTableDTO();
							tempDTO.setStartTime(originStartTime);
							tempDTO.setEndTime(originEndTime);
							tempDTO.setBusNo(oriDTO.getBusNo());
							tempDTO.setTripType(oriDTO.getTripType());
							tempDTO.setFixedTime(oriDTO.isFixedTime());
							tempOriginList.remove(destDTO);// remove DTO with
															// tempBus num
							tempOriginList.add(tempDTO);// add new DTO for
														// particular time slot
						} else {

							tempOriginList.add(destDTO);

							TimeTableDTO tempDTO = new TimeTableDTO();
							tempDTO.setStartTime(originStartTime);
							tempDTO.setEndTime(originEndTime);
							tempDTO.setBusNo(oriDTO.getBusNo());
							tempDTO.setTripType(oriDTO.getTripType());
							tempDTO.setFixedTime(oriDTO.isFixedTime());
							tempOriginListMissing.add(tempDTO);
						}
						timeSlotAvailable = true;
						break;
					} else {
						timeSlotAvailable = false;
					}
				}

				if (!timeSlotAvailable) {
					TimeTableDTO tempDTO = new TimeTableDTO();
					tempDTO.setStartTime(originStartTime);
					tempDTO.setEndTime(originEndTime);
					tempDTO.setBusNo(oriDTO.getBusNo());
					tempDTO.setTripType(oriDTO.getTripType());
					tempDTO.setFixedTime(oriDTO.isFixedTime());
					tempOriginList.add(tempDTO);
				}
			}

			// keep a duplicate temp list of finalTimeListOrigin
			List<TimeTableDTO> tempList = new ArrayList<TimeTableDTO>();
			int count = 0;
			boolean added = false;
			for (TimeTableDTO missingData : tempOriginListMissing) {
				added = false;
				for (TimeTableDTO destinationData : tempOriginList) {
					if (!destinationData.isFixedTime()) {
						if ((destinationData.getStartTime().compareTo(missingData.getStartTime()) > 0)
								&& (destinationData.getEndTime().compareTo(missingData.getEndTime()) > 0)) {
							TimeTableDTO tempDTO = new TimeTableDTO();
							tempDTO.setStartTime(destinationData.getStartTime());
							tempDTO.setEndTime(destinationData.getEndTime());
							tempDTO.setBusNo(missingData.getBusNo());
							tempDTO.setTripType(missingData.getTripType());
							tempDTO.setFixedTime(missingData.isFixedTime());
							tempList.add(tempDTO);
							count = count + 1;
							added = true;
							break;
						}
					}
				}
				if (!added) {
					System.out.println(
							"get last time range of tempDestinationList plus driver rest time add to tempList");
				}
			}

			List<TimeTableDTO> finalAddedList = new ArrayList<TimeTableDTO>();
			boolean add = false;
			for (TimeTableDTO destinationData : tempOriginList) {
				add = false;
				for (TimeTableDTO tempData : tempList) {
					if (tempData.getStartTime().equals(destinationData.getStartTime())
							&& tempData.getEndTime().equals(destinationData.getEndTime())) {
						finalAddedList.add(tempData);
						add = true;
						break;
					}
				}
				if (!add) {
					finalAddedList.add(destinationData);
				}
			}

			// some data in temp list are not added to final list add that data
			List<TimeTableDTO> tempfinalAddedList = new ArrayList<TimeTableDTO>();
			boolean found = false;
			for (TimeTableDTO tempData : tempList) {
				found = false;
				for (TimeTableDTO destinationData : tempOriginList) {
					if (tempData.getBusNo().equalsIgnoreCase(destinationData.getBusNo())) {
						found = true;
						break;
					}
				}
				if (!found) {
					tempfinalAddedList.add(tempData);
				}
			}

			finalAddedList.addAll(tempfinalAddedList);
			// some data in temp list are not added to final list add that data

			/** remove duplicates in list **/
			List<TimeTableDTO> temp = new ArrayList<TimeTableDTO>();
			for (TimeTableDTO t : finalAddedList) {
				temp.add(t);
			}
			System.out.println("finalAddedList size 1: " + finalAddedList.size());
			int counti = 0;
			for (TimeTableDTO d : temp) {
				counti = 0;
				Iterator<TimeTableDTO> it = finalAddedList.iterator();
				while (it.hasNext()) {
					TimeTableDTO dto = it.next();
					if (dto.getBusNo() != null && !dto.getBusNo().isEmpty() && d.getBusNo() != null
							&& !d.getBusNo().isEmpty() && dto.getBusNo().equals(d.getBusNo())) {
						counti = counti + 1;
						if (counti > 1) {
							it.remove();
						}
					} else {
						if (dto.getBusNo() == null || dto.getBusNo().isEmpty()
								|| dto.getBusNo().trim().equalsIgnoreCase("")) {
							it.remove();
						}
					}
				}
			}

			System.out.println("finalAddedList size 2: " + finalAddedList.size());
			/** remove duplicates in list **/

			/** correction of bus numbers start **/
			int busNoSuffix = 0;// abbreviation
			List<TimeTableDTO> busNoCorrectionList = new ArrayList<TimeTableDTO>();
			List<String> busNoStringList = new ArrayList<String>();
			List<String> currBusNoList = new ArrayList<String>();
			for (TimeTableDTO data : finalAddedList) {
				System.out.println("************************* " + data.getBusNo() + " " + data.getStartTime() + " "
						+ data.getEndTime());
				busNoCorrectionList.add(data);
				if (data.getBusNo().substring(0, abbreviation.length()).equals(abbreviation)) {
					busNoSuffix = busNoSuffix + 1;
					busNoStringList.add(abbreviation + busNoSuffix);
					currBusNoList.add(data.getBusNo());
				}
			}

			/** correction list **/
			HashMap<String, String> hm = new HashMap<String, String>();
			Collections.sort(currBusNoList);
			final int abbLen = abbreviation.length();
			Collections.sort(currBusNoList, new Comparator<String>() {
				@Override
				public int compare(String u1, String u2) {
					return Integer.valueOf(u2.substring(abbLen)) - Integer.valueOf(u1.substring(abbLen));
				}
			});

			Collections.reverse(currBusNoList);

			int sortCount = 0;
			for (String s : busNoStringList) {
				hm.put(s, currBusNoList.get(sortCount));
				sortCount = sortCount + 1;
			}
			/** correction list **/

			for (Map.Entry m : hm.entrySet()) {
				for (TimeTableDTO data : finalAddedList) {
					if (m.getValue().equals(data.getBusNo())) {
						TimeTableDTO addDTO = new TimeTableDTO();
						addDTO.setStartTime(data.getStartTime());
						addDTO.setEndTime(data.getEndTime());
						addDTO.setFixedTime(data.isFixedTime());
						addDTO.setBusNo(m.getKey().toString());
						busNoCorrectionList.add(addDTO);
						busNoCorrectionList.remove(data);
					}
				}
			}
			/** correction of bus numbers end **/

			/**
			 * if trip count=2 divide non fixed bus count send bus twice start
			 **/
			if (coupling) {
				// origin
				List<TimeTableDTO> tripTwoList = new ArrayList<TimeTableDTO>();
				for (TimeTableDTO d : busNoCorrectionList) {
					if (d.getTripType() != null && !d.getTripType().equals("") && d.getTripType().equals("O")) {
						if (!d.isFixedTime()) {
							tripTwoList.add(d);
						}
					}
				}
				Collections.sort(tripTwoList, new Comparator<TimeTableDTO>() {
					@Override
					public int compare(TimeTableDTO u1, TimeTableDTO u2) {
						return Integer.valueOf(u2.getBusNo().substring(abbLen))
								- Integer.valueOf(u1.getBusNo().substring(abbLen));
					}
				});

				Collections.reverse(tripTwoList);

				HashMap<String, String> hashm = new HashMap<String, String>();
				int trips = tripTwoList.size() / 2;
				if (tripTwoList.size() % 2 != 0) {
					trips = trips + 1;
				}
				int addC = trips;
				int newC = 0;
				for (int i = 0; i < tripTwoList.size(); i++) {
					if (i < trips) {
						hashm.put(tripTwoList.get(i).getBusNo(), tripTwoList.get(i).getBusNo());
					} else {
						hashm.put(tripTwoList.get(addC).getBusNo(), tripTwoList.get(newC).getBusNo());
						addC = addC + 1;
						newC = newC + 1;
					}
				}

				List<TimeTableDTO> tempBusNoList = new ArrayList<TimeTableDTO>();
				for (TimeTableDTO t : busNoCorrectionList) {
					System.out.println(t.getBusNo() + " " + t.getStartTime() + " " + t.getEndTime());
					tempBusNoList.add(t);
				}

				for (Map.Entry m : hashm.entrySet()) {
					for (TimeTableDTO t : busNoCorrectionList) {
						if (m.getKey().toString().equals(t.getBusNo())) {
							TimeTableDTO tempDto = new TimeTableDTO();
							tempDto.setStartTime(t.getStartTime());
							tempDto.setEndTime(t.getEndTime());
							tempDto.setTripType(t.getTripType());
							tempDto.setFixedTime(t.isFixedTime());
							tempDto.setBusNo(m.getValue().toString());
							tempBusNoList.remove(t);
							tempBusNoList.add(tempDto);
						}
					}
				}
				for (TimeTableDTO t : tempBusNoList) {
					System.out.println(t.getBusNo() + " " + t.getStartTime() + " " + t.getEndTime());
				}
				// origin
				// destination
				List<TimeTableDTO> tripTwoListD = new ArrayList<TimeTableDTO>();
				for (TimeTableDTO d : busNoCorrectionList) {
					if (d.getTripType() != null && !d.getTripType().equals("") && d.getTripType().equals("D")) {
						if (!d.isFixedTime()) {
							tripTwoListD.add(d);
						}
					}
				}
				Collections.sort(tripTwoListD, new Comparator<TimeTableDTO>() {
					@Override
					public int compare(TimeTableDTO u1, TimeTableDTO u2) {
						return Integer.valueOf(u2.getBusNo().substring(abbLen))
								- Integer.valueOf(u1.getBusNo().substring(abbLen));
					}
				});

				Collections.reverse(tripTwoListD);

				HashMap<String, String> hashmD = new HashMap<String, String>();
				int tripsD = tripTwoListD.size() / 2;
				if (tripTwoListD.size() % 2 != 0) {
					tripsD = tripsD + 1;
				}
				int addCD = tripsD;
				int newCD = 0;
				for (int i = 0; i < tripTwoListD.size(); i++) {
					if (i < tripsD) {
						hashmD.put(tripTwoListD.get(i).getBusNo(), tripTwoListD.get(i).getBusNo());
					} else {
						hashmD.put(tripTwoListD.get(addCD).getBusNo(), tripTwoListD.get(newCD).getBusNo());
						addCD = addCD + 1;
						newCD = newCD + 1;
					}
				}

				List<TimeTableDTO> tempBusNoListD = new ArrayList<TimeTableDTO>();
				for (TimeTableDTO t : busNoCorrectionList) {
					tempBusNoListD.add(t);
				}

				for (Map.Entry m : hashmD.entrySet()) {
					for (TimeTableDTO t : busNoCorrectionList) {
						if (m.getKey().toString().equals(t.getBusNo())) {
							TimeTableDTO tempDto = new TimeTableDTO();
							tempDto.setStartTime(t.getStartTime());
							tempDto.setEndTime(t.getEndTime());
							tempDto.setTripType(t.getTripType());
							tempDto.setFixedTime(t.isFixedTime());
							tempDto.setBusNo(m.getValue().toString());
							tempBusNoListD.remove(t);
							tempBusNoListD.add(tempDto);
						}
					}
				}
				// destination
				busNoCorrectionList = tempBusNoListD;

			}
			/**
			 * if trip count=2 divide non fixed bus count send bus twice end
			 **/

			// keep a duplicateBusNum
			originGroupThreeList = new ArrayList<TimeTableDTO>();
			for (TimeTableDTO dto : busNoCorrectionList) {
				dto.setDuplicateBusNum(dto.getBusNo());
				dto.setGroup("3");
				dto.setTempStartTime(dto.getStartTime());
				dto.setTempEndTime(dto.getEndTime());
				originGroupThreeList.add(dto);
			}
			// keep a duplicateBusNum

		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void detinationToOriginBusSendGroupThree(List<TimeTableDTO> finalTimeListDestination, String driverRestTime,
			String addTimeString, String abbreviation) {
		List<TimeTableDTO> tempDestinationListMissing = new ArrayList<TimeTableDTO>();

		// keep a duplicate temp list of finalTimeListDestination to add and
		// remove DTOs after new busNo added to destinationList
		List<TimeTableDTO> tempDestinationList = new ArrayList<TimeTableDTO>();
		for (TimeTableDTO destDTO : finalTimeListDestination) {
			tempDestinationList.add(destDTO);
		}

		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		try {
			boolean timeSlotAvailable = false;
			String detinationStartTime = null;
			String destinationEndTime = null;
			for (TimeTableDTO oriDTO : originGroupThreeList) {
				for (TimeTableDTO destDTO : finalTimeListDestination) {

					int gapInMinutes = (int) calculateTotalMinutesForEnd(driverRestTime);
					System.out.println("minutes:" + gapInMinutes);
					LocalTime time = LocalTime.parse(oriDTO.getEndTime());
					time = time.plusMinutes(gapInMinutes);// originDTO end time
															// + driver rest
															// time =
															// destination
															// departure time

					detinationStartTime = time.toString();
					System.out.println("destination start time: " + detinationStartTime);

					Date busRdeTime = timeFormat.parse(addTimeString);// addTimeString
																		// is
																		// the
																		// time
																		// takes
																		// for
																		// bus
																		// ride
					Date destinationStartDate = timeFormat.parse(time.toString());
					long sum = busRdeTime.getTime() + destinationStartDate.getTime();
					destinationEndTime = timeFormat.format(new Date(sum));
					System.out.println("destination end time: " + destinationEndTime);

					if (destDTO.getStartTime().equals(detinationStartTime)
							&& destDTO.getEndTime().equals(destinationEndTime)) {
						if (!destDTO.isFixedTime()) {
							TimeTableDTO tempDTO = new TimeTableDTO();
							tempDTO.setStartTime(detinationStartTime);
							tempDTO.setEndTime(destinationEndTime);
							tempDTO.setBusNo(oriDTO.getBusNo());
							tempDTO.setTripType(oriDTO.getTripType());
							tempDTO.setFixedTime(oriDTO.isFixedTime());
							tempDestinationList.remove(destDTO);// remove DTO
																// with tempBus
																// num
							tempDestinationList.add(tempDTO);// add new DTO for
																// particular
																// time slot
						} else {

							tempDestinationList.add(destDTO);

							TimeTableDTO tempDTO = new TimeTableDTO();
							tempDTO.setStartTime(detinationStartTime);
							tempDTO.setEndTime(destinationEndTime);
							tempDTO.setBusNo(oriDTO.getBusNo());
							tempDTO.setTripType(oriDTO.getTripType());
							tempDTO.setFixedTime(oriDTO.isFixedTime());
							tempDestinationListMissing.add(tempDTO);
						}
						timeSlotAvailable = true;
						break;
					} else {
						timeSlotAvailable = false;
					}
				}

				if (!timeSlotAvailable) {
					TimeTableDTO tempDTO = new TimeTableDTO();
					tempDTO.setStartTime(detinationStartTime);
					tempDTO.setEndTime(destinationEndTime);
					tempDTO.setBusNo(oriDTO.getBusNo());
					tempDTO.setTripType(oriDTO.getTripType());
					tempDTO.setFixedTime(oriDTO.isFixedTime());
					tempDestinationList.add(tempDTO);
				}
			}

			// keep a duplicate temp list of finalTimeListDestination
			List<TimeTableDTO> tempList = new ArrayList<TimeTableDTO>();
			int count = 0;
			boolean added = false;
			for (TimeTableDTO missingData : tempDestinationListMissing) {
				added = false;
				for (TimeTableDTO destinationData : tempDestinationList) {
					if (!destinationData.isFixedTime()) {
						if ((destinationData.getStartTime().compareTo(missingData.getStartTime()) > 0)
								&& (destinationData.getEndTime().compareTo(missingData.getEndTime()) > 0)) {
							TimeTableDTO tempDTO = new TimeTableDTO();
							tempDTO.setStartTime(destinationData.getStartTime());
							tempDTO.setEndTime(destinationData.getEndTime());
							tempDTO.setBusNo(missingData.getBusNo());
							tempDTO.setTripType(missingData.getTripType());
							tempDTO.setFixedTime(missingData.isFixedTime());
							tempList.add(tempDTO);
							count = count + 1;
							added = true;
							break;
						}
					}
				}
				if (!added) {
					System.out.println(
							"get last time range of tempDestinationList plus driver rest time add to tempList");
				}
			}

			List<TimeTableDTO> finalAddedList = new ArrayList<TimeTableDTO>();
			boolean add = false;
			for (TimeTableDTO destinationData : tempDestinationList) {
				add = false;
				for (TimeTableDTO tempData : tempList) {
					if (tempData.getStartTime().equals(destinationData.getStartTime())
							&& tempData.getEndTime().equals(destinationData.getEndTime())) {
						finalAddedList.add(tempData);
						add = true;
						break;
					}
				}
				if (!add) {
					finalAddedList.add(destinationData);
				}
			}

			// some data in tempList arent added to final list check that data
			// and add to final list
			List<TimeTableDTO> tempfinalAddedList = new ArrayList<TimeTableDTO>();
			boolean found = false;
			for (TimeTableDTO tempData : tempList) {
				found = false;
				for (TimeTableDTO destinationData : tempDestinationList) {
					if (tempData.getBusNo().equalsIgnoreCase(destinationData.getBusNo())) {
						found = true;
						break;
					}
				}
				if (!found) {
					tempfinalAddedList.add(tempData);
				}
			}

			finalAddedList.addAll(tempfinalAddedList);
			// some data in tempList arent added to final list check that data
			// and add to final list

			/** remove duplicates in list **/
			List<TimeTableDTO> temp = new ArrayList<TimeTableDTO>();
			for (TimeTableDTO t : finalAddedList) {
				temp.add(t);
			}
			System.out.println("finalAddedList size 1: " + finalAddedList.size());
			int counti = 0;
			for (TimeTableDTO d : temp) {
				counti = 0;
				Iterator<TimeTableDTO> it = finalAddedList.iterator();
				while (it.hasNext()) {
					TimeTableDTO dto = it.next();
					if (dto.getBusNo() != null && !dto.getBusNo().isEmpty() && d.getBusNo() != null
							&& !d.getBusNo().isEmpty() && dto.getBusNo().equals(d.getBusNo())) {
						counti = counti + 1;
						if (counti > 1) {
							it.remove();
						}
					} else {
						if (dto.getBusNo() == null || dto.getBusNo().isEmpty()
								|| dto.getBusNo().trim().equalsIgnoreCase("")) {
							it.remove();
						}
					}
				}
			}

			System.out.println("finalAddedList size 2: " + finalAddedList.size());
			/** remove duplicates in list **/

			/** correction of bus numbers start **/
			int busNoSuffix = 0;// abbreviation
			List<TimeTableDTO> busNoCorrectionList = new ArrayList<TimeTableDTO>();
			List<String> busNoStringList = new ArrayList<String>();
			List<String> currBusNoList = new ArrayList<String>();
			for (TimeTableDTO data : finalAddedList) {
				System.out.println("************************* " + data.getBusNo() + " " + data.getStartTime() + " "
						+ data.getEndTime());
				busNoCorrectionList.add(data);
				if (data.getBusNo().substring(0, abbreviation.length()).equals(abbreviation)) {
					busNoSuffix = busNoSuffix + 1;
					busNoStringList.add(abbreviation + busNoSuffix);
					currBusNoList.add(data.getBusNo());
				}
			}

			/** correction list **/
			HashMap<String, String> hm = new HashMap<String, String>();
			Collections.sort(currBusNoList);
			final int abbLen = abbreviation.length();
			Collections.sort(currBusNoList, new Comparator<String>() {
				@Override
				public int compare(String u1, String u2) {
					return Integer.valueOf(u2.substring(abbLen)) - Integer.valueOf(u1.substring(abbLen));
				}
			});

			Collections.reverse(currBusNoList);

			int sortCount = 0;
			for (String s : busNoStringList) {
				hm.put(s, currBusNoList.get(sortCount));
				sortCount = sortCount + 1;
			}
			/** correction list **/

			for (Map.Entry m : hm.entrySet()) {
				for (TimeTableDTO data : finalAddedList) {
					if (m.getValue().equals(data.getBusNo())) {
						TimeTableDTO addDTO = new TimeTableDTO();
						addDTO.setStartTime(data.getStartTime());
						addDTO.setEndTime(data.getEndTime());
						addDTO.setFixedTime(data.isFixedTime());
						addDTO.setBusNo(m.getKey().toString());
						busNoCorrectionList.add(addDTO);
						busNoCorrectionList.remove(data);
					}
				}
			}
			/** correction of bus numbers end **/

			/**
			 * if trip count=2 divide non fixed bus count send bus twice start
			 **/
			if (coupling) {
				// destination
				List<TimeTableDTO> tripTwoList = new ArrayList<TimeTableDTO>();
				for (TimeTableDTO d : busNoCorrectionList) {
					if (d.getTripType() != null && !d.getTripType().equals("") && d.getTripType().equals("D")) {
						if (!d.isFixedTime()) {
							tripTwoList.add(d);
						}
					}
				}
				Collections.sort(tripTwoList, new Comparator<TimeTableDTO>() {
					@Override
					public int compare(TimeTableDTO u1, TimeTableDTO u2) {
						return Integer.valueOf(u2.getBusNo().substring(abbLen))
								- Integer.valueOf(u1.getBusNo().substring(abbLen));
					}
				});

				Collections.reverse(tripTwoList);

				HashMap<String, String> hashm = new HashMap<String, String>();
				int trips = tripTwoList.size() / 2;
				if (tripTwoList.size() % 2 != 0) {
					trips = trips + 1;
				}
				int addC = trips;
				int newC = 0;
				for (int i = 0; i < tripTwoList.size(); i++) {
					if (i < trips) {
						hashm.put(tripTwoList.get(i).getBusNo(), tripTwoList.get(i).getBusNo());
					} else {
						hashm.put(tripTwoList.get(addC).getBusNo(), tripTwoList.get(newC).getBusNo());
						addC = addC + 1;
						newC = newC + 1;
					}
				}

				List<TimeTableDTO> tempBusNoList = new ArrayList<TimeTableDTO>();
				for (TimeTableDTO t : busNoCorrectionList) {
					tempBusNoList.add(t);
				}

				for (Map.Entry m : hashm.entrySet()) {
					for (TimeTableDTO t : busNoCorrectionList) {
						if (m.getKey().toString().equals(t.getBusNo())) {
							TimeTableDTO tempDto = new TimeTableDTO();
							tempDto.setStartTime(t.getStartTime());
							tempDto.setEndTime(t.getEndTime());
							tempDto.setTripType(t.getTripType());
							tempDto.setFixedTime(t.isFixedTime());
							tempDto.setBusNo(m.getValue().toString());
							tempBusNoList.remove(t);
							tempBusNoList.add(tempDto);
						}
					}
				}
				// destination
				// origin
				List<TimeTableDTO> tripTwoListO = new ArrayList<TimeTableDTO>();
				for (TimeTableDTO d : busNoCorrectionList) {
					if (d.getTripType() != null && !d.getTripType().equals("") && d.getTripType().equals("O")) {
						if (!d.isFixedTime()) {
							tripTwoListO.add(d);
						}
					}
				}
				Collections.sort(tripTwoListO, new Comparator<TimeTableDTO>() {
					@Override
					public int compare(TimeTableDTO u1, TimeTableDTO u2) {
						return Integer.valueOf(u2.getBusNo().substring(abbLen))
								- Integer.valueOf(u1.getBusNo().substring(abbLen));
					}
				});

				Collections.reverse(tripTwoListO);

				HashMap<String, String> hashmO = new HashMap<String, String>();
				int tripsO = tripTwoListO.size() / 2;
				if (tripTwoListO.size() % 2 != 0) {
					tripsO = tripsO + 1;
				}
				int addCO = tripsO;
				int newCO = 0;
				for (int i = 0; i < tripTwoListO.size(); i++) {
					if (i < tripsO) {
						hashmO.put(tripTwoListO.get(i).getBusNo(), tripTwoListO.get(i).getBusNo());
					} else {
						hashmO.put(tripTwoListO.get(addCO).getBusNo(), tripTwoListO.get(newCO).getBusNo());
						addCO = addCO + 1;
						newCO = newCO + 1;
					}
				}

				List<TimeTableDTO> tempBusNoListO = new ArrayList<TimeTableDTO>();
				for (TimeTableDTO t : busNoCorrectionList) {
					tempBusNoListO.add(t);
				}

				for (Map.Entry m : hashmO.entrySet()) {
					for (TimeTableDTO t : busNoCorrectionList) {
						if (m.getKey().toString().equals(t.getBusNo())) {
							TimeTableDTO tempDto = new TimeTableDTO();
							tempDto.setStartTime(t.getStartTime());
							tempDto.setEndTime(t.getEndTime());
							tempDto.setTripType(t.getTripType());
							tempDto.setFixedTime(t.isFixedTime());
							tempDto.setBusNo(m.getValue().toString());
							tempBusNoListO.remove(t);
							tempBusNoListO.add(tempDto);
						}
					}
				}
				// origin

				busNoCorrectionList = tempBusNoList;
			}
			/**
			 * if trip count=2 divide non fixed bus count send bus twice end
			 **/
			// keep a duplicateBusNum
			destinationGroupThreeList = new ArrayList<TimeTableDTO>();
			for (TimeTableDTO dto : busNoCorrectionList) {
				dto.setDuplicateBusNum(dto.getBusNo());
				dto.setGroup("3");
				dto.setTempStartTime(dto.getStartTime());
				dto.setTempEndTime(dto.getEndTime());
				destinationGroupThreeList.add(dto);
			}
			// keep a duplicateBusNum

		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void removeDuplicatesInGroupOneOrigin() {
		/** remove duplicates in list **/
		List<TimeTableDTO> temp = new ArrayList<TimeTableDTO>();
		for (TimeTableDTO t : originGroupOneList) {
			temp.add(t);
		}
		System.out.println("originGroupOneList size 1: " + originGroupOneList.size());
		int count = 0;
		for (TimeTableDTO d : temp) {
			count = 0;
			Iterator<TimeTableDTO> it = originGroupOneList.iterator();
			while (it.hasNext()) {
				TimeTableDTO dto = it.next();
				if (dto.getBusNo() != null && !dto.getBusNo().isEmpty() && d.getBusNo() != null
						&& !d.getBusNo().isEmpty() && dto.getBusNo().equals(d.getBusNo())) {
					count = count + 1;
					if (count > 1) {
						it.remove();
					}
				} else {
					if (dto.getBusNo() == null || dto.getBusNo().isEmpty()
							|| dto.getBusNo().trim().equalsIgnoreCase("")) {
						it.remove();
					}
				}
			}
		}

		System.out.println("originGroupOneList size 2: " + originGroupTwoList.size());
		/** remove duplicates in list **/
	}

	public void removeDuplicatesInGroupTwoOrigin() {
		/** remove duplicates in list **/
		List<TimeTableDTO> temp = new ArrayList<TimeTableDTO>();
		for (TimeTableDTO t : originGroupTwoList) {
			temp.add(t);
		}
		System.out.println("originGroupTwoList size 1: " + originGroupTwoList.size());
		int count = 0;
		for (TimeTableDTO d : temp) {
			count = 0;
			Iterator<TimeTableDTO> it = originGroupTwoList.iterator();
			while (it.hasNext()) {
				TimeTableDTO dto = it.next();
				if (dto.getBusNo() != null && !dto.getBusNo().isEmpty() && d.getBusNo() != null
						&& !d.getBusNo().isEmpty() && dto.getBusNo().equals(d.getBusNo())) {
					count = count + 1;
					if (count > 1) {
						it.remove();
					}
				} else {
					if (dto.getBusNo() == null || dto.getBusNo().isEmpty()
							|| dto.getBusNo().trim().equalsIgnoreCase("")) {
						it.remove();
					}
				}
			}
		}

		System.out.println("originGroupTwoList size 2: " + originGroupTwoList.size());
		/** remove duplicates in list **/
	}

	public void removeDuplicatesInGroupThreeOrigin() {
		/** remove duplicates in list **/
		List<TimeTableDTO> temp = new ArrayList<TimeTableDTO>();
		for (TimeTableDTO t : originGroupThreeList) {
			temp.add(t);
		}
		System.out.println("originGroupThreeList size 1: " + originGroupThreeList.size());
		int count = 0;
		for (TimeTableDTO d : temp) {
			count = 0;
			Iterator<TimeTableDTO> it = originGroupThreeList.iterator();
			while (it.hasNext()) {
				TimeTableDTO dto = it.next();
				if (dto.getBusNo() != null && !dto.getBusNo().isEmpty() && d.getBusNo() != null
						&& !d.getBusNo().isEmpty() && dto.getBusNo().equals(d.getBusNo())) {
					count = count + 1;
					if (count > 1) {
						it.remove();
					}
				} else {
					if (dto.getBusNo() == null || dto.getBusNo().isEmpty()
							|| dto.getBusNo().trim().equalsIgnoreCase("")) {
						it.remove();
					}
				}
			}
		}

		System.out.println("originGroupThreeList size 2: " + originGroupThreeList.size());
		/** remove duplicates in list **/
	}

	public boolean checkBusesAssigned(int groupCount) {
		if (groupCount == 1) {
			boolean assigned = routeScheduleService.checkBusAssignerDone(timeTableDTO.getBusCategory(),
					timeTableDTO.getGenereatedRefNo(), "1", "O");

			if (assigned) {
				return false;
			}

			boolean assignedD = routeScheduleService.checkBusAssignerDone(timeTableDTO.getBusCategory(),
					timeTableDTO.getGenereatedRefNo(), "1", "D");

			if (assignedD) {
				return false;
			}

		} else if (groupCount == 2) {
			boolean assigned = routeScheduleService.checkBusAssignerDone(timeTableDTO.getBusCategory(),
					timeTableDTO.getGenereatedRefNo(), "1", "O");

			if (assigned) {
				return false;
			}

			boolean assignedD = routeScheduleService.checkBusAssignerDone(timeTableDTO.getBusCategory(),
					timeTableDTO.getGenereatedRefNo(), "1", "D");

			if (assignedD) {
				return false;
			}

			boolean assigned2 = routeScheduleService.checkBusAssignerDone(timeTableDTO.getBusCategory(),
					timeTableDTO.getGenereatedRefNo(), "2", "O");

			if (assigned2) {
				return false;
			}

			boolean assignedD2 = routeScheduleService.checkBusAssignerDone(timeTableDTO.getBusCategory(),
					timeTableDTO.getGenereatedRefNo(), "2", "D");

			if (assignedD2) {
				return false;
			}

		} else if (groupCount == 3) {
			boolean assigned = routeScheduleService.checkBusAssignerDone(timeTableDTO.getBusCategory(),
					timeTableDTO.getGenereatedRefNo(), "1", "O");

			if (assigned) {
				return false;
			}

			boolean assignedD = routeScheduleService.checkBusAssignerDone(timeTableDTO.getBusCategory(),
					timeTableDTO.getGenereatedRefNo(), "1", "D");

			if (assignedD) {
				return false;
			}

			boolean assigned2 = routeScheduleService.checkBusAssignerDone(timeTableDTO.getBusCategory(),
					timeTableDTO.getGenereatedRefNo(), "2", "O");

			if (assigned2) {
				return false;
			}

			boolean assignedD2 = routeScheduleService.checkBusAssignerDone(timeTableDTO.getBusCategory(),
					timeTableDTO.getGenereatedRefNo(), "2", "D");

			if (assignedD2) {
				return false;
			}

			boolean assigned3 = routeScheduleService.checkBusAssignerDone(timeTableDTO.getBusCategory(),
					timeTableDTO.getGenereatedRefNo(), "3", "O");

			if (assigned3) {
				return false;
			}

			boolean assignedD3 = routeScheduleService.checkBusAssignerDone(timeTableDTO.getBusCategory(),
					timeTableDTO.getGenereatedRefNo(), "3", "D");

			if (assignedD3) {
				return false;
			}
		}

		return true;
	}

	private void updateTimeSlots() {

		try {

			// added by thilna.d on 14-10-2021
			if (originGroupOneList != null && !originGroupOneList.isEmpty() && destinationGroupOneList != null
					&& !destinationGroupOneList.isEmpty()) {
				
				panelGeneratorWithoutFixedTimeService.updateTimetableGeneratorNew("1", timeTableDTO.getGenereatedRefNo(),
						sessionBackingBean.getLoginUser(), originGroupOneList, destinationGroupOneList);
			}

			if (groupCount > 1) {

				// added by thilna.d on 14-10-2021
				if (originGroupTwoList != null && !originGroupTwoList.isEmpty() && destinationGroupTwoList != null
						&& !destinationGroupTwoList.isEmpty()) {
					
					panelGeneratorWithoutFixedTimeService.updateTimetableGeneratorNew("2", timeTableDTO.getGenereatedRefNo(),
							sessionBackingBean.getLoginUser(), originGroupTwoList, destinationGroupTwoList);
				}

			}

			if (groupCount > 2) {

				// added by thilna.d on 14-10-2021
				if (originGroupThreeList != null && !originGroupThreeList.isEmpty() && destinationGroupThreeList != null
						&& !destinationGroupThreeList.isEmpty()) {
					
					panelGeneratorWithoutFixedTimeService.updateTimetableGeneratorNew("3", timeTableDTO.getGenereatedRefNo(),
							sessionBackingBean.getLoginUser(), originGroupThreeList, destinationGroupThreeList);
				}

			}

		} catch (Exception ex) {
			ex.printStackTrace();
			sessionBackingBean.setMessage("Data cannot be saved");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
		}

	}

	private boolean validateTimeSlots() {

		for (TimeTableDTO dto : originGroupOneList) {

			String startTime = dto.getStartTime();
			String endTime = dto.getEndTime();

			if (dto.getBusNo().isEmpty() || dto.getBusNo().equals(null)) {

				sessionBackingBean.setMessage("Please select bus numbers for all time slots");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
				return false;
			}

			if (!isValidDate(startTime) || startTime.matches(".*[a-zA-Z]+.*")) {

				sessionBackingBean.setMessage("Start time is not valid. Please check the format");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
				return false;
			}
			if (!isValidDate(endTime) || endTime.matches(".*[a-zA-Z]+.*")) {

				sessionBackingBean.setMessage("Start time is not valid. Please check the format");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
				return false;
			}

		}

		for (TimeTableDTO dto : destinationGroupOneList) {

			String startTime = dto.getStartTime();
			String endTime = dto.getEndTime();

			if (dto.getBusNo().isEmpty() || dto.getBusNo().equals(null)) {

				sessionBackingBean.setMessage("Please select bus numbers for all time slots");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
				return false;
			}

			if (!isValidDate(startTime) || startTime.matches(".*[a-zA-Z]+.*")) {

				sessionBackingBean.setMessage("Start time is not valid. Please check the format");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
				return false;
			}
			if (!isValidDate(endTime) || endTime.matches(".*[a-zA-Z]+.*")) {

				sessionBackingBean.setMessage("Start time is not valid. Please check the format");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
				return false;
			}

		}

		if (groupCount > 1) {

			for (TimeTableDTO dto : originGroupTwoList) {

				String startTime = dto.getStartTime();
				String endTime = dto.getEndTime();

				if (!isValidDate(startTime) || startTime.matches(".*[a-zA-Z]+.*")) {

					sessionBackingBean.setMessage("Start time is not valid. Please check the format");
					RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
					return false;
				}
				if (!isValidDate(endTime) || endTime.matches(".*[a-zA-Z]+.*")) {

					sessionBackingBean.setMessage("Start time is not valid. Please check the format");
					RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
					return false;
				}

			}

			for (TimeTableDTO dto : destinationGroupTwoList) {

				String startTime = dto.getStartTime();
				String endTime = dto.getEndTime();

				if (!isValidDate(startTime) || startTime.matches(".*[a-zA-Z]+.*")) {

					sessionBackingBean.setMessage("Start time is not valid. Please check the format");
					RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
					return false;
				}
				if (!isValidDate(endTime) || endTime.matches(".*[a-zA-Z]+.*")) {

					sessionBackingBean.setMessage("Start time is not valid. Please check the format");
					RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
					return false;
				}

			}

		}

		if (groupCount > 2) {

			for (TimeTableDTO dto : originGroupThreeList) {

				String startTime = dto.getStartTime();
				String endTime = dto.getEndTime();

				if (!isValidDate(startTime) || startTime.matches(".*[a-zA-Z]+.*")) {

					sessionBackingBean.setMessage("Start time is not valid. Please check the format");
					RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
					return false;
				}
				if (!isValidDate(endTime) || endTime.matches(".*[a-zA-Z]+.*")) {

					sessionBackingBean.setMessage("Start time is not valid. Please check the format");
					RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
					return false;
				}

			}

			for (TimeTableDTO dto : destinationGroupThreeList) {

				String startTime = dto.getStartTime();
				String endTime = dto.getEndTime();

				if (!isValidDate(startTime) || startTime.matches(".*[a-zA-Z]+.*")) {

					sessionBackingBean.setMessage("Start time is not valid. Please check the format");
					RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
					return false;
				}
				if (!isValidDate(endTime) || endTime.matches(".*[a-zA-Z]+.*")) {

					sessionBackingBean.setMessage("Start time is not valid. Please check the format");
					RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
					return false;
				}

			}

		}

		return true;

	}

	private void updateTimeSlotsEdit() {

		try {

			if (originGroupOneList != null && !originGroupOneList.isEmpty() && destinationGroupOneList != null
					&& !destinationGroupOneList.isEmpty()) {
				// added by thilna.d on 1-12-2021
				panelGeneratorWithoutFixedTimeService.updateTimetableGeneratorEditNew("1",
						timeTableDTO.getGenereatedRefNo(), sessionBackingBean.getLoginUser(), originGroupOneList,
						destinationGroupOneList);
			}

			if (groupCount > 1) {

				if (originGroupTwoList != null && !originGroupTwoList.isEmpty() && destinationGroupTwoList != null
						&& !destinationGroupTwoList.isEmpty()) {

					// added by thilna.d on 1-12-2021
					panelGeneratorWithoutFixedTimeService.updateTimetableGeneratorEditNew("2",
							timeTableDTO.getGenereatedRefNo(), sessionBackingBean.getLoginUser(), originGroupTwoList,
							destinationGroupTwoList);
				}

			}

			if (groupCount > 2) {

				if (originGroupThreeList != null && !originGroupThreeList.isEmpty() && destinationGroupThreeList != null
						&& !destinationGroupThreeList.isEmpty()) {

					// added by thilna.d on 1-12-2021
					panelGeneratorWithoutFixedTimeService.updateTimetableGeneratorEditNew("3",
							timeTableDTO.getGenereatedRefNo(), sessionBackingBean.getLoginUser(), originGroupThreeList,
							destinationGroupThreeList);
				}

			}

		} catch (Exception ex) {
			ex.printStackTrace();
			sessionBackingBean.setMessage("Data cannot be saved");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
		}

	}

	public void checkRemainingTimeSlotsForDestinationDataForCoupling(String driverRestTime, String busRideTime) {
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		// check destination buses in origin list and verify whether they can
		// send to origin and take back in given time slots

		Collections.sort(originGroupOneList, new Comparator<TimeTableDTO>() {
			@Override
			public int compare(TimeTableDTO u1, TimeTableDTO u2) {
				return Integer.valueOf(u2.getId()) - Integer.valueOf(u1.getId());
			}
		});

		Collections.reverse(originGroupOneList);

		int coupleCount = originGroupOneList.size() / 2;
		List<TimeTableDTO> dtoList = new ArrayList<TimeTableDTO>();
		for (int i = 0; i <= coupleCount; i++) {
			dtoList.add(originGroupOneList.get(i));
		}

		List<TimeTableDTO> tempList1 = new ArrayList<TimeTableDTO>();
		List<TimeTableDTO> tempList2 = new ArrayList<TimeTableDTO>();
		int xxCount = coupleCount;
		int yyCount = (xxCount / 2);
		boolean yyCountChange = false;
		List<String> destinationBusAbbriviation = new ArrayList<String>();
		destinationBusAbbriviation.addAll(orginBusAbbriviationMain);
		destinationBusAbbriviation.addAll(destinationBusAbbriviationMain);
		for (int i = 0; i < coupleCount; i++) {

			TimeTableDTO tempDto = new TimeTableDTO();

			if (xxCount < originGroupOneList.size()) {

				if (!originGroupOneList.get(xxCount).isFixedTime()) {
					// check size added by thilna.d on 15-10-2021
					if (destinationBusAbbriviation.size() > 0) {
						tempDto.setBusNo(destinationBusAbbriviation.get(0));
						tempDto.setDuplicateBusNum(destinationBusAbbriviation.get(0));
						destinationBusAbbriviation.remove(0);
					}
				} else {
					tempDto.setBusNo(originGroupOneList.get(xxCount).getBusNo());
					tempDto.setDuplicateBusNum(originGroupOneList.get(xxCount).getBusNo());
				}

				tempDto.setCtbBus(originGroupOneList.get(xxCount).isCtbBus());
				tempDto.setEndTime(originGroupOneList.get(xxCount).getEndTime());
				tempDto.setStartTime(originGroupOneList.get(xxCount).getStartTime());
				tempDto.setId(originGroupOneList.get(xxCount).getId());
				tempDto.setOriginEndTimeString(originGroupOneList.get(xxCount).getOriginEndTimeString());
				tempDto.setOriginStartTimeString(originGroupOneList.get(xxCount).getOriginStartTimeString());
				tempDto.setTempStartTime(originGroupOneList.get(xxCount).getTempStartTime());
				tempDto.setTempEndTime(originGroupOneList.get(xxCount).getTempEndTime());
				tempDto.setTimeTableDetSeq(originGroupOneList.get(xxCount).getTimeTableDetSeq());
				tempDto.setTripId(originGroupOneList.get(xxCount).getTripId());
				tempDto.setTripType(originGroupOneList.get(xxCount).getTripType());
				tempList1.add(tempDto);

				tempList2.add(originGroupOneList.get(xxCount));

				xxCount = xxCount + 1;
				yyCount = yyCount + 1;
			}

		}

		for (TimeTableDTO dto : tempList2) {
			originGroupOneList.remove(dto);
			// System.out.println(dto.getStartTime() + " *** " + dto.getEndTime());
		}

		for (TimeTableDTO dto : tempList1) {
			originGroupOneList.add(dto);
			// System.out.println(dto.getStartTime() + " ### " + dto.getEndTime());
		}

	}

	public void checkRemainingTimeSlotsForOriginDataForCoupling(String driverRestTime, String busRideTime) {

		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		// check origin buses in destination list and verify whether they can
		// send to destination and take back in given time slots

		Collections.sort(destinationGroupOneList, new Comparator<TimeTableDTO>() {
			@Override
			public int compare(TimeTableDTO u1, TimeTableDTO u2) {
				return Integer.valueOf(u2.getId()) - Integer.valueOf(u1.getId());
			}
		});

		Collections.reverse(destinationGroupOneList);

		int coupleCount = destinationGroupOneList.size() / 2;

		List<TimeTableDTO> dtoList = new ArrayList<TimeTableDTO>();
		for (int i = 0; i <= coupleCount; i++) {
			dtoList.add(destinationGroupOneList.get(i));
		}

		List<TimeTableDTO> tempList1 = new ArrayList<TimeTableDTO>();
		List<TimeTableDTO> tempList2 = new ArrayList<TimeTableDTO>();
		int xxCount = coupleCount;
		int yyCount = (xxCount / 2);
		boolean yyCountChange = false;
		List<String> orginBusAbbriviation = new ArrayList<String>();
		orginBusAbbriviation.addAll(destinationBusAbbriviationMain);
		orginBusAbbriviation.addAll(orginBusAbbriviationMain);
		for (int i = 0; i < coupleCount; i++) {

			TimeTableDTO tempDto = new TimeTableDTO();

			if (xxCount < destinationGroupOneList.size()) {

				if (!destinationGroupOneList.get(xxCount).isFixedTime()) {
					// check size added by thilna.d on 15-10-2021
					if (orginBusAbbriviation.size() > 0) {
						tempDto.setBusNo(orginBusAbbriviation.get(0));
						tempDto.setDuplicateBusNum(orginBusAbbriviation.get(0));
						orginBusAbbriviation.remove(0);
					}
				} else {
					tempDto.setBusNo(destinationGroupOneList.get(xxCount).getBusNo());
					tempDto.setDuplicateBusNum(destinationGroupOneList.get(xxCount).getBusNo());
				}

				tempDto.setCtbBus(destinationGroupOneList.get(xxCount).isCtbBus());
				tempDto.setEndTime(destinationGroupOneList.get(xxCount).getEndTime());
				tempDto.setStartTime(destinationGroupOneList.get(xxCount).getStartTime());
				tempDto.setId(destinationGroupOneList.get(xxCount).getId());
				tempDto.setOriginEndTimeString(destinationGroupOneList.get(xxCount).getOriginEndTimeString());
				tempDto.setOriginStartTimeString(destinationGroupOneList.get(xxCount).getOriginStartTimeString());
				tempDto.setTempStartTime(destinationGroupOneList.get(xxCount).getTempStartTime());
				tempDto.setTempEndTime(destinationGroupOneList.get(xxCount).getTempEndTime());
				tempDto.setTimeTableDetSeq(destinationGroupOneList.get(xxCount).getTimeTableDetSeq());
				tempDto.setTripId(destinationGroupOneList.get(xxCount).getTripId());
				tempDto.setTripType(destinationGroupOneList.get(xxCount).getTripType());
				tempList1.add(tempDto);

				tempList2.add(destinationGroupOneList.get(xxCount));

				xxCount = xxCount + 1;
				yyCount = yyCount + 1;
			}
		}

		for (TimeTableDTO dto : tempList2) {
			destinationGroupOneList.remove(dto);
			// System.out.println(dto.getStartTime() + " *** " + dto.getEndTime());
		}

		for (TimeTableDTO dto : tempList1) {
			destinationGroupOneList.add(dto);
			// System.out.println(dto.getStartTime() + " ### " + dto.getEndTime());
		}

	}

	public void checkRemainingTimeSlotsForDestinationDataForCouplingWithLeave(String driverRestTime,
			String busRideTime) {

		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		// check destination buses in origin list and verify whether they can
		// send to origin and take back in given time slots

		Collections.sort(originGroupOneList, new Comparator<TimeTableDTO>() {
			@Override
			public int compare(TimeTableDTO u1, TimeTableDTO u2) {
				return Integer.valueOf(u2.getId()) - Integer.valueOf(u1.getId());
			}
		});

		Collections.reverse(originGroupOneList);

		int coupleCount = originGroupOneList.size() / 2;
		List<TimeTableDTO> dtoList = new ArrayList<TimeTableDTO>();
		for (int i = 0; i <= coupleCount; i++) {
			dtoList.add(originGroupOneList.get(i));
		}

		List<TimeTableDTO> tempList1 = new ArrayList<TimeTableDTO>();
		List<TimeTableDTO> tempList2 = new ArrayList<TimeTableDTO>();
		int xxCount = coupleCount;
		int yyCount = (xxCount / 2);
		boolean yyCountChange = false;
		List<String> destinationBusAbbriviation = new ArrayList<String>();

		destinationBusAbbriviationMain.remove(removedBusesFromDestination);
		orginBusAbbriviationMain.remove(removedBusesFromOrgin);
		for (int i = 0; i < destinationBusAbbriviationMain.size(); i++) {
			for (String b : removedBusesFromDestination)
				if (destinationBusAbbriviationMain.get(i).equalsIgnoreCase(b)) {
					destinationBusAbbriviationMain.remove(i);
				}
		}

		for (int i = 0; i < orginBusAbbriviationMain.size(); i++) {
			for (String b : removedBusesFromOrgin)
				if (orginBusAbbriviationMain.get(i).equalsIgnoreCase(b)) {
					orginBusAbbriviationMain.remove(i);
				}
		}

		destinationBusAbbriviation.addAll(orginBusAbbriviationMain);
		destinationBusAbbriviation.addAll(destinationBusAbbriviationMain);
		for (int i = 0; i < coupleCount; i++) {

			TimeTableDTO tempDto = new TimeTableDTO();

			if (xxCount < originGroupOneList.size()) {

				if (!originGroupOneList.get(xxCount).isFixedTime()) {
					tempDto.setBusNo(destinationBusAbbriviation.get(0));
					tempDto.setDuplicateBusNum(destinationBusAbbriviation.get(0));
					destinationBusAbbriviation.remove(0);
				} else {
					tempDto.setBusNo(originGroupOneList.get(xxCount).getBusNo());
					tempDto.setDuplicateBusNum(originGroupOneList.get(xxCount).getBusNo());
				}
				tempDto.setCtbBus(originGroupOneList.get(xxCount).isCtbBus());
				tempDto.setEndTime(originGroupOneList.get(xxCount).getEndTime());
				tempDto.setStartTime(originGroupOneList.get(xxCount).getStartTime());
				tempDto.setId(originGroupOneList.get(xxCount).getId());
				tempDto.setOriginEndTimeString(originGroupOneList.get(xxCount).getOriginEndTimeString());
				tempDto.setOriginStartTimeString(originGroupOneList.get(xxCount).getOriginStartTimeString());
				tempDto.setTempStartTime(originGroupOneList.get(xxCount).getTempStartTime());
				tempDto.setTempEndTime(originGroupOneList.get(xxCount).getTempEndTime());
				tempDto.setTimeTableDetSeq(originGroupOneList.get(xxCount).getTimeTableDetSeq());
				tempDto.setTripId(originGroupOneList.get(xxCount).getTripId());
				tempDto.setTripType(originGroupOneList.get(xxCount).getTripType());
				tempList1.add(tempDto);

				tempList2.add(originGroupOneList.get(xxCount));

				xxCount = xxCount + 1;
				yyCount = yyCount + 1;
			}

		}

		for (TimeTableDTO dto : tempList2) {
			originGroupOneList.remove(dto);
			// System.out.println(dto.getStartTime() + " *** " + dto.getEndTime());
		}

		for (TimeTableDTO dto : tempList1) {
			originGroupOneList.add(dto);
			// System.out.println(dto.getStartTime() + " ### " + dto.getEndTime());
		}

	}

	public void checkRemainingTimeSlotsForOriginDataForCouplingWithLeave(String driverRestTime, String busRideTime) {
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		// check origin buses in destination list and verify whether they can
		// send to destination and take back in given time slots

		Collections.sort(destinationGroupOneList, new Comparator<TimeTableDTO>() {
			@Override
			public int compare(TimeTableDTO u1, TimeTableDTO u2) {
				return Integer.valueOf(u2.getId()) - Integer.valueOf(u1.getId());
			}
		});

		Collections.reverse(destinationGroupOneList);

		int coupleCount = destinationGroupOneList.size() / 2;

		List<TimeTableDTO> dtoList = new ArrayList<TimeTableDTO>();
		for (int i = 0; i <= coupleCount; i++) {
			dtoList.add(destinationGroupOneList.get(i));
		}

		List<TimeTableDTO> tempList1 = new ArrayList<TimeTableDTO>();
		List<TimeTableDTO> tempList2 = new ArrayList<TimeTableDTO>();
		int xxCount = coupleCount;
		int yyCount = (xxCount / 2);
		boolean yyCountChange = false;
		List<String> orginBusAbbriviation = new ArrayList<String>();

		for (int i = 0; i < destinationBusAbbriviationMain.size(); i++) {
			for (String b : removedBusesFromDestination)
				if (destinationBusAbbriviationMain.get(i).equalsIgnoreCase(b)) {
					destinationBusAbbriviationMain.remove(i);
				}
		}

		for (int i = 0; i < orginBusAbbriviationMain.size(); i++) {
			for (String b : removedBusesFromOrgin)
				if (orginBusAbbriviationMain.get(i).equalsIgnoreCase(b)) {
					orginBusAbbriviationMain.remove(i);
				}
		}

		orginBusAbbriviation.addAll(destinationBusAbbriviationMain);
		orginBusAbbriviation.addAll(orginBusAbbriviationMain);
		for (int i = 0; i < coupleCount; i++) {

			TimeTableDTO tempDto = new TimeTableDTO();

			if (xxCount < destinationGroupOneList.size()) {

				if (!destinationGroupOneList.get(xxCount).isFixedTime()) {
					tempDto.setBusNo(orginBusAbbriviation.get(0));
					tempDto.setDuplicateBusNum(orginBusAbbriviation.get(0));
					orginBusAbbriviation.remove(0);
				} else {
					tempDto.setBusNo(destinationGroupOneList.get(xxCount).getBusNo());
					tempDto.setDuplicateBusNum(destinationGroupOneList.get(xxCount).getBusNo());
				}

				tempDto.setCtbBus(destinationGroupOneList.get(xxCount).isCtbBus());
				tempDto.setEndTime(destinationGroupOneList.get(xxCount).getEndTime());
				tempDto.setStartTime(destinationGroupOneList.get(xxCount).getStartTime());
				tempDto.setId(destinationGroupOneList.get(xxCount).getId());
				tempDto.setOriginEndTimeString(destinationGroupOneList.get(xxCount).getOriginEndTimeString());
				tempDto.setOriginStartTimeString(destinationGroupOneList.get(xxCount).getOriginStartTimeString());
				tempDto.setTempStartTime(destinationGroupOneList.get(xxCount).getTempStartTime());
				tempDto.setTempEndTime(destinationGroupOneList.get(xxCount).getTempEndTime());
				tempDto.setTimeTableDetSeq(destinationGroupOneList.get(xxCount).getTimeTableDetSeq());
				tempDto.setTripId(destinationGroupOneList.get(xxCount).getTripId());
				tempDto.setTripType(destinationGroupOneList.get(xxCount).getTripType());
				tempList1.add(tempDto);

				tempList2.add(destinationGroupOneList.get(xxCount));

				xxCount = xxCount + 1;
				yyCount = yyCount + 1;
			}
		}

		for (TimeTableDTO dto : tempList2) {
			destinationGroupOneList.remove(dto);
			// System.out.println(dto.getStartTime() + " *** " + dto.getEndTime());
		}

		for (TimeTableDTO dto : tempList1) {
			destinationGroupOneList.add(dto);
			// System.out.println(dto.getStartTime() + " ### " + dto.getEndTime());
		}

	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public List<TimeTableDTO> getBusRouteList() {
		return busRouteList;
	}

	public void setBusRouteList(List<TimeTableDTO> busRouteList) {
		this.busRouteList = busRouteList;
	}

	public TimeTableService getTimeTableService() {
		return timeTableService;
	}

	public void setTimeTableService(TimeTableService timeTableService) {
		this.timeTableService = timeTableService;
	}

	public int getActiveTabIndex() {
		return activeTabIndex;
	}

	public void setActiveTabIndex(int activeTabIndex) {
		this.activeTabIndex = activeTabIndex;
	}

	public TimeTableDTO getTimeTableDTO() {
		return timeTableDTO;
	}

	public void setTimeTableDTO(TimeTableDTO timeTableDTO) {
		this.timeTableDTO = timeTableDTO;
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

	public List<TimeTableDTO> getRouteDetailsList() {
		return routeDetailsList;
	}

	public void setRouteDetailsList(List<TimeTableDTO> routeDetailsList) {
		this.routeDetailsList = routeDetailsList;
	}

	public List<TimeTableDTO> getOriginGroupOneList() {
		return originGroupOneList;
	}

	public void setOriginGroupOneList(List<TimeTableDTO> originGroupOneList) {
		this.originGroupOneList = originGroupOneList;
	}

	public List<TimeTableDTO> getOriginGroupTwoList() {
		return originGroupTwoList;
	}

	public void setOriginGroupTwoList(List<TimeTableDTO> originGroupTwoList) {
		this.originGroupTwoList = originGroupTwoList;
	}

	public List<TimeTableDTO> getOriginGroupThreeList() {
		return originGroupThreeList;
	}

	public void setOriginGroupThreeList(List<TimeTableDTO> originGroupThreeList) {
		this.originGroupThreeList = originGroupThreeList;
	}

	public List<TimeTableDTO> getDestinationGroupOneList() {
		return destinationGroupOneList;
	}

	public void setDestinationGroupOneList(List<TimeTableDTO> destinationGroupOneList) {
		this.destinationGroupOneList = destinationGroupOneList;
	}

	public List<TimeTableDTO> getDestinationGroupTwoList() {
		return destinationGroupTwoList;
	}

	public void setDestinationGroupTwoList(List<TimeTableDTO> destinationGroupTwoList) {
		this.destinationGroupTwoList = destinationGroupTwoList;
	}

	public List<TimeTableDTO> getDestinationGroupThreeList() {
		return destinationGroupThreeList;
	}

	public void setDestinationGroupThreeList(List<TimeTableDTO> destinationGroupThreeList) {
		this.destinationGroupThreeList = destinationGroupThreeList;
	}

	public int getGroupCount() {
		return groupCount;
	}

	public void setGroupCount(int groupCount) {
		this.groupCount = groupCount;
	}

	public List<String> getTimeList() {
		return timeList;
	}

	public void setTimeList(List<String> timeList) {
		this.timeList = timeList;
	}

	public TimeTableDTO getGroupOneDTO() {
		return groupOneDTO;
	}

	public void setGroupOneDTO(TimeTableDTO groupOneDTO) {
		this.groupOneDTO = groupOneDTO;
	}

	public int getPvtBusCount() {
		return pvtBusCount;
	}

	public void setPvtBusCount(int pvtBusCount) {
		this.pvtBusCount = pvtBusCount;
	}

	public int getRestTime() {
		return restTime;
	}

	public void setRestTime(int restTime) {
		this.restTime = restTime;
	}

	public int getOriginOneBuses() {
		return originOneBuses;
	}

	public void setOriginOneBuses(int originOneBuses) {
		this.originOneBuses = originOneBuses;
	}

	public int getDestinationOneBuses() {
		return destinationOneBuses;
	}

	public void setDestinationOneBuses(int destinationOneBuses) {
		this.destinationOneBuses = destinationOneBuses;
	}

	public boolean isDisabledGroupTwo() {
		return disabledGroupTwo;
	}

	public void setDisabledGroupTwo(boolean disabledGroupTwo) {
		this.disabledGroupTwo = disabledGroupTwo;
	}

	public boolean isDisabledGroupThree() {
		return disabledGroupThree;
	}

	public void setDisabledGroupThree(boolean disabledGroupThree) {
		this.disabledGroupThree = disabledGroupThree;
	}

	public int getOriginTwoBuses() {
		return originTwoBuses;
	}

	public void setOriginTwoBuses(int originTwoBuses) {
		this.originTwoBuses = originTwoBuses;
	}

	public int getDestinationTwoBuses() {
		return destinationTwoBuses;
	}

	public void setDestinationTwoBuses(int destinationTwoBuses) {
		this.destinationTwoBuses = destinationTwoBuses;
	}

	public int getOriginThreeBuses() {
		return originThreeBuses;
	}

	public void setOriginThreeBuses(int originThreeBuses) {
		this.originThreeBuses = originThreeBuses;
	}

	public int getDestinationThreeBuses() {
		return destinationThreeBuses;
	}

	public void setDestinationThreeBuses(int destinationThreeBuses) {
		this.destinationThreeBuses = destinationThreeBuses;
	}

	public TimeTableDTO getGroupTwoDTO() {
		return groupTwoDTO;
	}

	public void setGroupTwoDTO(TimeTableDTO groupTwoDTO) {
		this.groupTwoDTO = groupTwoDTO;
	}

	public TimeTableDTO getGroupThreeDTO() {
		return groupThreeDTO;
	}

	public void setGroupThreeDTO(TimeTableDTO groupThreeDTO) {
		this.groupThreeDTO = groupThreeDTO;
	}

	public TimeTableDTO getTripsDTO() {
		return tripsDTO;
	}

	public void setTripsDTO(TimeTableDTO tripsDTO) {
		this.tripsDTO = tripsDTO;
	}

	public TimeTableDTO getRouteDTO() {
		return routeDTO;
	}

	public void setRouteDTO(TimeTableDTO routeDTO) {
		this.routeDTO = routeDTO;
	}

	public String getGroupOneDays() {
		return groupOneDays;
	}

	public void setGroupOneDays(String groupOneDays) {
		this.groupOneDays = groupOneDays;
	}

	public String getGroupTwoDays() {
		return groupTwoDays;
	}

	public void setGroupTwoDays(String groupTwoDays) {
		this.groupTwoDays = groupTwoDays;
	}

	public String getGroupThreeDays() {
		return groupThreeDays;
	}

	public void setGroupThreeDays(String groupThreeDays) {
		this.groupThreeDays = groupThreeDays;
	}

	public PanelGeneratorWithoutFixedTimeService getPanelGeneratorWithoutFixedTimeService() {
		return panelGeneratorWithoutFixedTimeService;
	}

	public void setPanelGeneratorWithoutFixedTimeService(
			PanelGeneratorWithoutFixedTimeService panelGeneratorWithoutFixedTimeService) {
		this.panelGeneratorWithoutFixedTimeService = panelGeneratorWithoutFixedTimeService;
	}

	public boolean isCoupling() {
		return coupling;
	}

	public void setCoupling(boolean coupling) {
		this.coupling = coupling;
	}

	public static boolean isValidDate(String inDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
		dateFormat.setLenient(false);
		try {
			dateFormat.parse(inDate.trim());
		} catch (ParseException pe) {
			return false;
		}
		return true;
	}

	public String getCouplesPerBus() {
		return couplesPerBus;
	}

	public void setCouplesPerBus(String couplesPerBus) {
		this.couplesPerBus = couplesPerBus;
	}

	public boolean isCouplesPerBusDisable() {
		return couplesPerBusDisable;
	}

	public void setCouplesPerBusDisable(boolean couplesPerBusDisable) {
		this.couplesPerBusDisable = couplesPerBusDisable;
	}

	public List<TimeTableDTO> getBusCategoryList() {
		return busCategoryList;
	}

	public void setBusCategoryList(List<TimeTableDTO> busCategoryList) {
		this.busCategoryList = busCategoryList;
	}

	public String getSelectedBusCategory() {
		return selectedBusCategory;
	}

	public void setSelectedBusCategory(String selectedBusCategory) {
		this.selectedBusCategory = selectedBusCategory;
	}

	public boolean isEditColRender() {
		return editColRender;
	}

	public void setEditColRender(boolean editColRender) {
		this.editColRender = editColRender;
	}

	public RouteScheduleService getRouteScheduleService() {
		return routeScheduleService;
	}

	public void setRouteScheduleService(RouteScheduleService routeScheduleService) {
		this.routeScheduleService = routeScheduleService;
	}

	public boolean isLeaveSaveDisable() {
		return leaveSaveDisable;
	}

	public void setLeaveSaveDisable(boolean leaveSaveDisable) {
		this.leaveSaveDisable = leaveSaveDisable;
	}

	public LinkedList<TimeTableDTO> getMidnightShiftBusesOrigin() {
		return midnightShiftBusesOrigin;
	}

	public void setMidnightShiftBusesOrigin(LinkedList<TimeTableDTO> midnightShiftBusesOrigin) {
		this.midnightShiftBusesOrigin = midnightShiftBusesOrigin;
	}

	public LinkedList<TimeTableDTO> getMidnightShiftBusesDestination() {
		return midnightShiftBusesDestination;
	}

	public void setMidnightShiftBusesDestination(LinkedList<TimeTableDTO> midnightShiftBusesDestination) {
		this.midnightShiftBusesDestination = midnightShiftBusesDestination;
	}

	public LinkedList<TimeTableDTO> getMidnightShiftBusesOriginG2() {
		return midnightShiftBusesOriginG2;
	}

	public void setMidnightShiftBusesOriginG2(LinkedList<TimeTableDTO> midnightShiftBusesOriginG2) {
		this.midnightShiftBusesOriginG2 = midnightShiftBusesOriginG2;
	}

	public LinkedList<TimeTableDTO> getMidnightShiftBusesDestinationG2() {
		return midnightShiftBusesDestinationG2;
	}

	public void setMidnightShiftBusesDestinationG2(LinkedList<TimeTableDTO> midnightShiftBusesDestinationG2) {
		this.midnightShiftBusesDestinationG2 = midnightShiftBusesDestinationG2;
	}

	public LinkedList<TimeTableDTO> getMidnightShiftBusesOriginG3() {
		return midnightShiftBusesOriginG3;
	}

	public void setMidnightShiftBusesOriginG3(LinkedList<TimeTableDTO> midnightShiftBusesOriginG3) {
		this.midnightShiftBusesOriginG3 = midnightShiftBusesOriginG3;
	}

	public LinkedList<TimeTableDTO> getMidnightShiftBusesDestinationG3() {
		return midnightShiftBusesDestinationG3;
	}

	public void setMidnightShiftBusesDestinationG3(LinkedList<TimeTableDTO> midnightShiftBusesDestinationG3) {
		this.midnightShiftBusesDestinationG3 = midnightShiftBusesDestinationG3;
	}

	public String getNightShiftBusType() {
		return nightShiftBusType;
	}

	public void setNightShiftBusType(String nightShiftBusType) {
		this.nightShiftBusType = nightShiftBusType;
	}

	public List<TimeTableDTO> getBusNoList() {
		return busNoListG1;
	}

	public void setBusNoList(List<TimeTableDTO> busNoList) {
		this.busNoListG1 = busNoList;
	}

	public List<TimeTableDTO> getBusNoListDestination() {
		return busNoListDestinationG1;
	}

	public void setBusNoListDestination(List<TimeTableDTO> busNoListDestination) {
		this.busNoListDestinationG1 = busNoListDestination;
	}

	public List<TimeTableDTO> getBusNoListG1() {
		return busNoListG1;
	}

	public void setBusNoListG1(List<TimeTableDTO> busNoListG1) {
		this.busNoListG1 = busNoListG1;
	}

	public List<TimeTableDTO> getBusNoListDestinationG1() {
		return busNoListDestinationG1;
	}

	public void setBusNoListDestinationG1(List<TimeTableDTO> busNoListDestinationG1) {
		this.busNoListDestinationG1 = busNoListDestinationG1;
	}

	public List<TimeTableDTO> getBusNoListG2() {
		return busNoListG2;
	}

	public void setBusNoListG2(List<TimeTableDTO> busNoListG2) {
		this.busNoListG2 = busNoListG2;
	}

	public List<TimeTableDTO> getBusNoListDestinationG2() {
		return busNoListDestinationG2;
	}

	public void setBusNoListDestinationG2(List<TimeTableDTO> busNoListDestinationG2) {
		this.busNoListDestinationG2 = busNoListDestinationG2;
	}

	public List<TimeTableDTO> getBusNoListG3() {
		return busNoListG3;
	}

	public void setBusNoListG3(List<TimeTableDTO> busNoListG3) {
		this.busNoListG3 = busNoListG3;
	}

	public List<TimeTableDTO> getBusNoListDestinationG3() {
		return busNoListDestinationG3;
	}

	public void setBusNoListDestinationG3(List<TimeTableDTO> busNoListDestinationG3) {
		this.busNoListDestinationG3 = busNoListDestinationG3;
	}

	public List<TimeTableDTO> getBusList1() {
		return busList1;
	}

	public void setBusList1(List<TimeTableDTO> busList1) {
		this.busList1 = busList1;
	}

	public List<TimeTableDTO> getBusList2() {
		return busList2;
	}

	public void setBusList2(List<TimeTableDTO> busList2) {
		this.busList2 = busList2;
	}

	public List<TimeTableDTO> getBusList3() {
		return busList3;
	}

	public void setBusList3(List<TimeTableDTO> busList3) {
		this.busList3 = busList3;
	}

}
