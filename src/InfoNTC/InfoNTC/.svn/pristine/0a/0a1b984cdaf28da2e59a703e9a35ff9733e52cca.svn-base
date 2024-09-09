package lk.informatics.ntc.view.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.TimeTableDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.TimeTableService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "panelWithFixedTimeBackingBean")
@ViewScoped
public class PanelGeneratorWithFixedTimeBackingBean {
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	// services

	private TimeTableService panelGenWithFixedTimeService;
	private CommonService commonService;

	// DTO
	private TimeTableDTO panelGenFixedTimeDTO = new TimeTableDTO();
	private TimeTableDTO travleTimeDTO = new TimeTableDTO();
	private TimeTableDTO busCategoryVal = new TimeTableDTO();
	private TimeTableDTO selectedRow = new TimeTableDTO();
	private TimeTableDTO refNoDTO = new TimeTableDTO();
	private TimeTableDTO abbriDTO = new TimeTableDTO();
	private TimeTableDTO routeDet = new TimeTableDTO();
	private TimeTableDTO nonPrivateBusDTO = new TimeTableDTO();
	private ArrayList<String> groupAndTripTypeDTO = new ArrayList<>();
	// list
	private List<TimeTableDTO> routeNoList = new ArrayList<>();
	private List<TimeTableDTO> busCategoryList = new ArrayList<>();
	private List<TimeTableDTO> showTimeSlotDet1 = new ArrayList<>();
	private List<TimeTableDTO> showTimeSlotDet2 = new ArrayList<>();
	private List<TimeTableDTO> showTimeSlotDet3 = new ArrayList<>();
	private List<TimeTableDTO> timeStartList = new ArrayList<>();
	private List<TimeTableDTO> timeStartList2 = new ArrayList<>();
	private List<TimeTableDTO> timeStartList3 = new ArrayList<>();
	private List<TimeTableDTO> timeStartListSecond = new ArrayList<>();
	private List<TimeTableDTO> timeStartList2Second = new ArrayList<>();
	private List<TimeTableDTO> timeStartList3Second = new ArrayList<>();

	private List<TimeTableDTO> showInsertedDataG1O = new ArrayList<>();
	private List<TimeTableDTO> showInsertedDataG2O = new ArrayList<>();
	private List<TimeTableDTO> showInsertedDataG3O = new ArrayList<>();

	private List<TimeTableDTO> showInsertedDataG1D = new ArrayList<>();
	private List<TimeTableDTO> showInsertedDataG2D = new ArrayList<>();
	private List<TimeTableDTO> showInsertedDataG3D = new ArrayList<>();

	private List<TimeTableDTO> busTypeList = new ArrayList<>();
	private List<TimeTableDTO> busTypeSetList = new ArrayList<>();
	private List<TimeTableDTO> busTypeSetList2 = new ArrayList<>();
	private List<TimeTableDTO> busTypeSetList3 = new ArrayList<>();
	private List<TimeTableDTO> busCategoryListMain = new ArrayList<>();
	private boolean busTypeDisable1, busTypeDisable2, busTypeDisable3, busTypeDisableD1;
	private int noOfBus;
	private String errorMsg, loginUser, sucessMsg, orgin, destination;
	String restTime = "00:00";
	String afterAddStr = null;
	String afterAddStrCor = null;
	long afterAdd;
	String TimeStartVal = null;
	int j, k, l, jD, kD, lD = 0;
	int noOFTripsValGOne;
	int noOFTripsValGTwo;
	int noOFTripsValGThree;
	private boolean bool;

	private List<TimeTableDTO> busCatList = new ArrayList<>();
	private List<TimeTableDTO> busCatList2 = new ArrayList<>();
	private List<TimeTableDTO> busCatList3 = new ArrayList<>();
	private List<TimeTableDTO> busCatListDtoO = new ArrayList<>();
	private List<TimeTableDTO> busCatListDtoO2 = new ArrayList<>();
	private List<TimeTableDTO> busCatListDtoO3 = new ArrayList<>();

	private boolean saveG1OrtoDe;
	private boolean saveG2OrtoDe;
	private boolean saveG3OrtoDe;
	private boolean saveG1DetoOr;
	private boolean saveG2DetoOr;
	private boolean saveG3DetoOr;

	private int tripsG1O, tripsG2O, tripsG3O, tripsG1D, tripsG2D, tripsG3D;
	private boolean checkDataAvailable = false;

	private boolean saveBtnDisable;

	private TimeTableDTO nonSltbEtcBusDTO = new TimeTableDTO();

	@PostConstruct
	public void init() {
		panelGenWithFixedTimeService = (TimeTableService) SpringApplicationContex.getBean("timeTableService");
		panelGenFixedTimeDTO = new TimeTableDTO();
		routeNoList = panelGenWithFixedTimeService.getRouteNoListFromTripsGen();
		loginUser = sessionBackingBean.getLoginUser();

		busTypeDisable1 = false;
		busTypeDisable2 = false;
		busTypeDisable3 = false;
		busTypeDisableD1 = false;

		saveG1OrtoDe = false;
		saveG2OrtoDe = false;
		saveG3OrtoDe = false;
		saveG1DetoOr = false;
		saveG2DetoOr = false;
		saveG3DetoOr = false;
		saveBtnDisable = false;

	}

	public void getBusCategory() {
		System.out.println("show bus category");
		busCategoryVal = panelGenWithFixedTimeService.getBusCategory(panelGenFixedTimeDTO.getRouteNo());
		busCategoryListMain = panelGenWithFixedTimeService.getBusCategoryList(panelGenFixedTimeDTO.getRouteNo());

	}

	public void getrouteOrDes() {

		routeDet = panelGenWithFixedTimeService.getRouteData(panelGenFixedTimeDTO.getRouteNo(),
				panelGenFixedTimeDTO.getBusCategoryDes());
		panelGenFixedTimeDTO.setOrigin(routeDet.getOrigin());
		panelGenFixedTimeDTO.setDestination(routeDet.getDestination());
	}

	public void Search() throws ParseException {
		String time = null;
		j = 0;

		timeStartList = new ArrayList<TimeTableDTO>();
		timeStartList2 = new ArrayList<>();
		timeStartList3 = new ArrayList<>();
		timeStartListSecond = new ArrayList<>();
		timeStartList2Second = new ArrayList<>();
		timeStartList3Second = new ArrayList<>();

		saveBtnDisable = false;
		System.out.println("Search details");
		if (panelGenFixedTimeDTO.getRouteNo() != null && !panelGenFixedTimeDTO.getRouteNo().trim().isEmpty()) {
			if (panelGenFixedTimeDTO.getBusCategoryDes() != null
					&& !panelGenFixedTimeDTO.getBusCategoryDes().trim().isEmpty()) {
				// show already saved data

				refNoDTO = panelGenWithFixedTimeService.getReffrenceNo(panelGenFixedTimeDTO.getRouteNo(),
						panelGenFixedTimeDTO.getBusCategoryDes());
				panelGenFixedTimeDTO.setTripRefNo(refNoDTO.getTripRefNo());
				panelGenFixedTimeDTO.setPanelGenNo(refNoDTO.getPanelGenNo());

				abbriDTO = panelGenWithFixedTimeService.getAbriviatiosForRoute(panelGenFixedTimeDTO.getRouteNo(),
						panelGenFixedTimeDTO.getBusCategoryDes());

				panelGenFixedTimeDTO.setAbbriAtOrigin(abbriDTO.getAbbriAtOrigin());
				panelGenFixedTimeDTO.setAbbriAtDestination(abbriDTO.getAbbriAtDestination());
				System.out.println("Ref no" + panelGenFixedTimeDTO.getTripRefNo());
				checkDataAvailable = panelGenWithFixedTimeService
						.dataAvailableForShow(panelGenFixedTimeDTO.getPanelGenNo());
				if (!checkDataAvailable) {
					travleTimeDTO = panelGenWithFixedTimeService.getTravleTimeForRouteBusCate(
							panelGenFixedTimeDTO.getRouteNo(), panelGenFixedTimeDTO.getBusCategoryDes());
					if (travleTimeDTO.getTimeRangeEnd() != null && !travleTimeDTO.getTimeRangeEnd().trim().isEmpty()) {
						groupAndTripTypeDTO = panelGenWithFixedTimeService.isGroup(panelGenFixedTimeDTO.getTripRefNo(),
								"O");

						if (groupAndTripTypeDTO.contains("1")) {

							panelGenFixedTimeDTO.setGroupOne("Y");
						}

						if (groupAndTripTypeDTO.contains("2")) {
							panelGenFixedTimeDTO.setGroupTwo("Y");

						}
						if (groupAndTripTypeDTO.contains("3")) {
							panelGenFixedTimeDTO.setGroupThree("Y");

						}

						if (panelGenFixedTimeDTO.getGroupOne() == null) {

							panelGenFixedTimeDTO.setGroupOne("N");
						}

						if (panelGenFixedTimeDTO.getGroupTwo() == null) {

							panelGenFixedTimeDTO.setGroupTwo("N");
						}

						if (panelGenFixedTimeDTO.getGroupThree() == null) {

							panelGenFixedTimeDTO.setGroupThree("N");
						}

						if (panelGenFixedTimeDTO.getGroupOne().equalsIgnoreCase("Y")) {

							showTimeSlotDet1 = panelGenWithFixedTimeService
									.showTimeSlotDetForGroups(panelGenFixedTimeDTO.getTripRefNo(), "1", "O");

							for (TimeTableDTO TTDto : showTimeSlotDet1) {

								getDataForTime1(TTDto);

							}

							// 28/10/2019 change done bye Hasanga.s start
							/** get last value of time and add travel time to it and add to the List **/
							SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
							timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

							TimeTableDTO tempDTO = new TimeTableDTO();
							tempDTO = panelGenWithFixedTimeService
									.getLastEndTimeOfTimeRange(panelGenFixedTimeDTO.getTripRefNo(), "1", "O");

							travleTimeDTO = panelGenWithFixedTimeService.getTravleTimeForRouteBusCate(
									panelGenFixedTimeDTO.getRouteNo(), panelGenFixedTimeDTO.getBusCategoryDes());

							Date date1 = timeFormat.parse(tempDTO.getEndTime());
							Date date2 = timeFormat.parse(travleTimeDTO.getTimeRangeEnd());

							long sum = date1.getTime() + date2.getTime();

							String endTimeVal = timeFormat.format(new Date(sum));
							TimeTableDTO dto = new TimeTableDTO();
							dto.setTimeStartVal(tempDTO.getEndTime());
							dto.setTimeRangeStart(tempDTO.getEndTime());
							dto.setTimeEndVal(endTimeVal);
							timeStartList.add(dto);
							// 28/10/2019 change done bye Hasanga.s end
						}
						if (panelGenFixedTimeDTO.getGroupTwo() == null) {

						}
						if (panelGenFixedTimeDTO.getGroupTwo().equalsIgnoreCase("Y")
								&& panelGenFixedTimeDTO.getGroupTwo() != null) {
							showTimeSlotDet2 = panelGenWithFixedTimeService
									.showTimeSlotDetForGroups(panelGenFixedTimeDTO.getTripRefNo(), "2", "O");

							for (TimeTableDTO TTDto : showTimeSlotDet2) {

								getDataForTime2(TTDto);

							}

							// 28/10/2019 change done bye Hasanga.s start
							/** get last value of time and add travel time to it and add to the List **/
							SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
							timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

							TimeTableDTO tempDTO = new TimeTableDTO();
							tempDTO = panelGenWithFixedTimeService
									.getLastEndTimeOfTimeRange(panelGenFixedTimeDTO.getTripRefNo(), "2", "O");

							Date date1 = timeFormat.parse(tempDTO.getEndTime());
							Date date2 = timeFormat.parse(travleTimeDTO.getTimeRangeEnd());

							travleTimeDTO = panelGenWithFixedTimeService.getTravleTimeForRouteBusCate(
									panelGenFixedTimeDTO.getRouteNo(), panelGenFixedTimeDTO.getBusCategoryDes());

							long sum = date1.getTime() + date2.getTime();

							String endTimeVal = timeFormat.format(new Date(sum));
							TimeTableDTO dto = new TimeTableDTO();
							dto.setTimeStartVal(tempDTO.getEndTime());
							dto.setTimeRangeStart(tempDTO.getEndTime());
							dto.setTimeEndVal(endTimeVal);
							timeStartList2.add(dto);
							// 28/10/2019 change done bye Hasanga.s end
						}
						if (panelGenFixedTimeDTO.getGroupThree() == null) {

						}
						if (panelGenFixedTimeDTO.getGroupThree().equalsIgnoreCase("Y")
								&& panelGenFixedTimeDTO.getGroupThree() != null) {
							showTimeSlotDet3 = panelGenWithFixedTimeService
									.showTimeSlotDetForGroups(panelGenFixedTimeDTO.getTripRefNo(), "3", "O");

							for (TimeTableDTO TTDto : showTimeSlotDet3) {

								getDataForTime3(TTDto);

							}

							// 28/10/2019 change done bye Hasanga.s start
							/** get last value of time and add travel time to it and add to the List **/
							SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
							timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

							TimeTableDTO tempDTO = new TimeTableDTO();
							tempDTO = panelGenWithFixedTimeService
									.getLastEndTimeOfTimeRange(panelGenFixedTimeDTO.getTripRefNo(), "3", "O");

							Date date1 = timeFormat.parse(tempDTO.getEndTime());
							Date date2 = timeFormat.parse(travleTimeDTO.getTimeRangeEnd());

							travleTimeDTO = panelGenWithFixedTimeService.getTravleTimeForRouteBusCate(
									panelGenFixedTimeDTO.getRouteNo(), panelGenFixedTimeDTO.getBusCategoryDes());

							long sum = date1.getTime() + date2.getTime();

							String endTimeVal = timeFormat.format(new Date(sum));
							TimeTableDTO dto = new TimeTableDTO();
							dto.setTimeStartVal(tempDTO.getEndTime());
							dto.setTimeRangeStart(tempDTO.getEndTime());
							dto.setTimeEndVal(endTimeVal);
							timeStartList3.add(dto);
							// 28/10/2019 change done bye Hasanga.s end

						}

						searchForDestinationToOrgin();

						// adding des abbriv to origin & origin abbriv to des
						List<TimeTableDTO> tempL1 = new ArrayList<>();
						tempL1.addAll(busCatList);
						tempL1.addAll(busCatListDtoO);
						List<TimeTableDTO> tempL1D = new ArrayList<>();
						tempL1D.addAll(busCatListDtoO);
						tempL1D.addAll(busCatList);
						busCatList = tempL1;
						busCatListDtoO = tempL1D;

						List<TimeTableDTO> tempL2 = new ArrayList<>();
						tempL2.addAll(busCatList2);
						tempL2.addAll(busCatListDtoO2);
						List<TimeTableDTO> tempL2D = new ArrayList<>();
						tempL2D.addAll(busCatList2);
						tempL2D.addAll(busCatListDtoO2);
						busCatList2 = tempL2;
						busCatListDtoO2 = tempL2D;

						List<TimeTableDTO> tempL3 = new ArrayList<>();
						tempL3.addAll(busCatList3);
						tempL3.addAll(busCatListDtoO3);
						List<TimeTableDTO> tempL3D = new ArrayList<>();
						tempL3D.addAll(busCatListDtoO3);
						tempL3D.addAll(busCatList3);
						busCatList3 = tempL3;
						busCatListDtoO3 = tempL3D;

					} else {
						setErrorMsg("Travel taken time not entered for this route number and bus type yet.");
						RequestContext.getCurrentInstance().update("requiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

					}
				}

				else {
					saveBtnDisable = true;
					setErrorMsg("Already Entered");
					RequestContext.getCurrentInstance().update("requiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				}
			} else {

				setErrorMsg("Please select a bus category for search");
				RequestContext.getCurrentInstance().update("requiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		}

		else {

			setErrorMsg("Please select a route no. for search");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public void searchForDestinationToOrgin() throws ParseException {
		// show already saved data
		// showInsertedData=panelGenWithFixedTimeService.getInsertedData();

		saveBtnDisable = false;
		groupAndTripTypeDTO = panelGenWithFixedTimeService.isGroup(panelGenFixedTimeDTO.getTripRefNo(), "D");

		if (groupAndTripTypeDTO.contains("1")) {

			panelGenFixedTimeDTO.setDesToOrGroupOne("Y");
		}

		if (groupAndTripTypeDTO.contains("2")) {
			panelGenFixedTimeDTO.setDesToOrGroupTwo("Y");

		}
		if (groupAndTripTypeDTO.contains("3")) {
			panelGenFixedTimeDTO.setDesToOrGroupThree("Y");

		}

		if (panelGenFixedTimeDTO.getDesToOrGroupOne() == null) {

			panelGenFixedTimeDTO.setDesToOrGroupOne("N");
		}

		if (panelGenFixedTimeDTO.getDesToOrGroupTwo() == null) {

			panelGenFixedTimeDTO.setDesToOrGroupTwo("N");
		}

		if (panelGenFixedTimeDTO.getDesToOrGroupThree() == null) {

			panelGenFixedTimeDTO.setDesToOrGroupThree("N");
		}

		if (panelGenFixedTimeDTO.getDesToOrGroupOne().equalsIgnoreCase("Y")) {
			showTimeSlotDet1 = panelGenWithFixedTimeService
					.showTimeSlotDetForGroups(panelGenFixedTimeDTO.getTripRefNo(), "1", "D");

			for (TimeTableDTO TTDto : showTimeSlotDet1) {

				getDataForTime1Second(TTDto);

			}

			// 28/10/2019 change done bye Hasanga.s start
			/** get last value of time and add travel time to it and add to the List **/
			SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
			timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

			TimeTableDTO tempDTO = new TimeTableDTO();
			tempDTO = panelGenWithFixedTimeService.getLastEndTimeOfTimeRange(panelGenFixedTimeDTO.getTripRefNo(), "1",
					"D");

			Date date1 = timeFormat.parse(tempDTO.getEndTime());
			Date date2 = timeFormat.parse(travleTimeDTO.getTimeRangeEnd());

			travleTimeDTO = panelGenWithFixedTimeService.getTravleTimeForRouteBusCate(panelGenFixedTimeDTO.getRouteNo(),
					panelGenFixedTimeDTO.getBusCategoryDes());

			long sum = date1.getTime() + date2.getTime();

			String endTimeVal = timeFormat.format(new Date(sum));
			TimeTableDTO dto = new TimeTableDTO();
			dto.setTimeStartVal(tempDTO.getEndTime());
			dto.setTimeRangeStart(tempDTO.getEndTime());
			dto.setTimeEndVal(endTimeVal);
			timeStartListSecond.add(dto);
			// 28/10/2019 change done bye Hasanga.s end
		}

		if (panelGenFixedTimeDTO.getDesToOrGroupTwo().equalsIgnoreCase("Y")
				&& panelGenFixedTimeDTO.getDesToOrGroupTwo() != null) {
			showTimeSlotDet2 = panelGenWithFixedTimeService
					.showTimeSlotDetForGroups(panelGenFixedTimeDTO.getTripRefNo(), "2", "D");

			for (TimeTableDTO TTDto : showTimeSlotDet2) {

				getDataForTime2Second(TTDto);

			}
			// 28/10/2019 change done bye Hasanga.s start
			/** get last value of time and add travel time to it and add to the List **/
			SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
			timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

			TimeTableDTO tempDTO = new TimeTableDTO();
			tempDTO = panelGenWithFixedTimeService.getLastEndTimeOfTimeRange(panelGenFixedTimeDTO.getTripRefNo(), "2",
					"D");

			Date date1 = timeFormat.parse(tempDTO.getEndTime());
			Date date2 = timeFormat.parse(travleTimeDTO.getTimeRangeEnd());

			travleTimeDTO = panelGenWithFixedTimeService.getTravleTimeForRouteBusCate(panelGenFixedTimeDTO.getRouteNo(),
					panelGenFixedTimeDTO.getBusCategoryDes());

			long sum = date1.getTime() + date2.getTime();

			String endTimeVal = timeFormat.format(new Date(sum));
			TimeTableDTO dto = new TimeTableDTO();
			dto.setTimeStartVal(tempDTO.getEndTime());
			dto.setTimeRangeStart(tempDTO.getEndTime());
			dto.setTimeEndVal(endTimeVal);
			timeStartList2Second.add(dto);
			// 28/10/2019 change done bye Hasanga.s end

		}

		if (panelGenFixedTimeDTO.getDesToOrGroupThree().equalsIgnoreCase("Y")
				&& panelGenFixedTimeDTO.getDesToOrGroupThree() != null) {
			showTimeSlotDet3 = panelGenWithFixedTimeService
					.showTimeSlotDetForGroups(panelGenFixedTimeDTO.getTripRefNo(), "3", "D");

			for (TimeTableDTO TTDto : showTimeSlotDet3) {

				getDataForTime3Second(TTDto);

			}

			// 28/10/2019 change done bye Hasanga.s start
			/** get last value of time and add travel time to it and add to the List **/
			SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
			timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

			TimeTableDTO tempDTO = new TimeTableDTO();
			tempDTO = panelGenWithFixedTimeService.getLastEndTimeOfTimeRange(panelGenFixedTimeDTO.getTripRefNo(), "3",
					"D");

			Date date1 = timeFormat.parse(tempDTO.getEndTime());
			Date date2 = timeFormat.parse(travleTimeDTO.getTimeRangeEnd());

			travleTimeDTO = panelGenWithFixedTimeService.getTravleTimeForRouteBusCate(panelGenFixedTimeDTO.getRouteNo(),
					panelGenFixedTimeDTO.getBusCategoryDes());

			long sum = date1.getTime() + date2.getTime();

			String endTimeVal = timeFormat.format(new Date(sum));
			TimeTableDTO dto = new TimeTableDTO();
			dto.setTimeStartVal(tempDTO.getEndTime());
			dto.setTimeRangeStart(tempDTO.getEndTime());
			dto.setTimeEndVal(endTimeVal);
			timeStartList3Second.add(dto);
			// 28/10/2019 change done bye Hasanga.s end

		}

	}

	public void getDataForTime1(TimeTableDTO TTDto) throws ParseException {

		travleTimeDTO = panelGenWithFixedTimeService.getTravleTimeForRouteBusCate(panelGenFixedTimeDTO.getRouteNo(),
				panelGenFixedTimeDTO.getBusCategoryDes());
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		TimeTableDTO detDTO = new TimeTableDTO();
		TimeTableDTO detDTO1 = new TimeTableDTO();

		detDTO.setTimeRangeTo(TTDto.getTimeRangeTo());
		detDTO.setTimeRangeStart(TTDto.getTimeRangeStart());
		detDTO.setFrequency(TTDto.getFrequency());

		int addFrequncy = detDTO.getFrequency();
		String addFreqStr = String.valueOf(addFrequncy);
		String timeRange = detDTO.getTimeRangeStart();

		// add frequency for create start time
		timeConverteToString(addFrequncy);

		Date timeStart = timeFormat.parse(timeRange);
		Date dateWithFreq = timeFormat.parse(restTime);
		panelGenFixedTimeDTO.setNoOfBuses(TTDto.getNoOfBuses());
		int noOfBus = TTDto.getNoOfBuses();

		detDTO.setTimeRangeEnd(travleTimeDTO.getTimeRangeEnd());
		String addTimeString = detDTO.getTimeRangeEnd();

		// fixed
		String timeRangeCor = detDTO.getTimeRangeStart();
		Date timeStartCor = timeFormat.parse(timeRangeCor);
		// afterAdd=Long.parseLong(afterAddStr);
		long afterAddCor = timeStartCor.getTime() - dateWithFreq.getTime();

		afterAddStrCor = timeFormat.format(new Date(afterAddCor));
		detDTO.setTimeRangeStart(afterAddStrCor);
		// fixed end
		// ---
		String busAbbAtOrgin = panelGenFixedTimeDTO.getAbbriAtOrigin();

		for (int i = 0; i < noOfBus; i++) {

			j = j + 1;
			detDTO1 = new TimeTableDTO();
			String busName = busAbbAtOrgin + String.valueOf(j);
			detDTO1.setBusType(busName);
			busCatList.add(detDTO1);

			afterAddStr = detDTO.getTimeRangeStart();
			TimeTableDTO detDto = new TimeTableDTO();
			detDto.setTimeRangeStart(detDTO.getTimeRangeStart());

			String timeRange1 = detDTO.getTimeRangeStart();
			Date timeStart1 = timeFormat.parse(timeRange1);
			// afterAdd=Long.parseLong(afterAddStr);
			long afterAdd = timeStart1.getTime() + dateWithFreq.getTime();

			afterAddStr = timeFormat.format(new Date(afterAdd));

			// TimeTableDTO detDto = new TimeTableDTO();
			// detDto.setTimeRangeStart(detDTO.getTimeRangeStart());
			detDto.setFrequency(detDTO.getFrequency());
			// added
			//
			detDto.setTimeStartVal(afterAddStr);
			TimeStartVal = detDto.getTimeStartVal();
			detDto.setTimeRangeStart(TimeStartVal);

			detDTO.setTimeRangeStart(detDto.getTimeRangeStart());

			// --

			Date date1 = timeFormat.parse(TimeStartVal);
			Date date2 = timeFormat.parse(addTimeString);

			long sum = date1.getTime() + date2.getTime();

			String date3 = timeFormat.format(new Date(sum));

			detDTO.setTimeEndVal(date3);
			detDto.setTimeEndVal(date3);
			detDto.setBusType(detDTO.getBusType());
			// ---
			timeStartList.add(detDto);

			// detDTO.setTimeRangeStart(detDto.getTimeRangeStart());
			// add busses
			// timeStartList.addAll(busTypeList);

		}
		// when frequency are zero

		if (noOfBus == 0) {
			busTypeDisable1 = true;
			TimeTableDTO detDto = new TimeTableDTO();
			detDTO.getTimeRangeStart();
			detDto.setTimeStartVal(detDTO.getTimeRangeStart());
			detDTO.getTimeRangeTo();
			detDto.setTimeEndVal(detDTO.getTimeRangeTo());
			System.out.println("test" + detDto.getTimeRangeStart());
			System.out.println("test2" + detDto.getTimeRangeTo());
			timeStartList.add(detDto);

		}

	}

	public void getDataForTime2(TimeTableDTO TTDto) throws ParseException {
		travleTimeDTO = panelGenWithFixedTimeService.getTravleTimeForRouteBusCate(panelGenFixedTimeDTO.getRouteNo(),
				panelGenFixedTimeDTO.getBusCategoryDes());

		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		TimeTableDTO detDTO = new TimeTableDTO();
		TimeTableDTO detDTOG2 = new TimeTableDTO();
		detDTO.setTimeRangeTo(TTDto.getTimeRangeTo());
		detDTO.setTimeRangeStart(TTDto.getTimeRangeStart());
		detDTO.setFrequency(TTDto.getFrequency());

		int addFrequncy = detDTO.getFrequency();
		String addFreqStr = String.valueOf(addFrequncy);
		String timeRange = detDTO.getTimeRangeStart();

		// add frequency for create start time
		timeConverteToString(addFrequncy);

		Date timeStart = timeFormat.parse(timeRange);
		Date dateWithFreq = timeFormat.parse(restTime);
		int noOfBus = TTDto.getNoOfBuses();
		// ---
		detDTO.setTimeRangeEnd(travleTimeDTO.getTimeRangeEnd());
		String addTimeString = detDTO.getTimeRangeEnd();
		// ---

		// fixed
		String timeRangeCor = detDTO.getTimeRangeStart();
		Date timeStartCor = timeFormat.parse(timeRangeCor);
		// afterAdd=Long.parseLong(afterAddStr);
		long afterAddCor = timeStartCor.getTime() - dateWithFreq.getTime();

		afterAddStrCor = timeFormat.format(new Date(afterAddCor));
		detDTO.setTimeRangeStart(afterAddStrCor);
		// fixed end

		String busAbbAtOrgin = panelGenFixedTimeDTO.getAbbriAtOrigin();
		for (int i = 0; i < noOfBus; i++) {

			k = k + 1;
			detDTOG2 = new TimeTableDTO();
			String busName = busAbbAtOrgin + String.valueOf(k);
			detDTOG2.setBusType(busName);
			busCatList2.add(detDTOG2);

			afterAddStr = detDTO.getTimeRangeStart();

			String timeRange1 = detDTO.getTimeRangeStart();
			Date timeStart1 = timeFormat.parse(timeRange1);
			// afterAdd=Long.parseLong(afterAddStr);
			long afterAdd = timeStart1.getTime() + dateWithFreq.getTime();

			afterAddStr = timeFormat.format(new Date(afterAdd));

			TimeTableDTO detDto = new TimeTableDTO();
			detDto.setTimeRangeStart(detDTO.getTimeRangeStart());
			detDto.setFrequency(detDTO.getFrequency());
			detDto.setTimeStartVal(afterAddStr);
			TimeStartVal = detDto.getTimeStartVal();
			detDto.setTimeRangeStart(TimeStartVal);

			detDTO.setTimeRangeStart(detDto.getTimeRangeStart());

			// --

			Date date1 = timeFormat.parse(TimeStartVal);
			Date date2 = timeFormat.parse(addTimeString);

			long sum = date1.getTime() + date2.getTime();

			String date3 = timeFormat.format(new Date(sum));

			detDTO.setTimeEndVal(date3);
			detDto.setTimeEndVal(date3);
			detDto.setBusType(detDTO.getBusType());
			timeStartList2.add(detDto);

		}
		// when frequency are zero

		if (noOfBus == 0) {
			TimeTableDTO detDto = new TimeTableDTO();
			detDTO.getTimeRangeStart();
			detDto.setTimeStartVal(detDTO.getTimeRangeStart());
			detDTO.getTimeRangeTo();
			detDto.setTimeEndVal(detDTO.getTimeRangeTo());
			System.out.println("test" + detDto.getTimeRangeStart());
			System.out.println("test2" + detDto.getTimeRangeTo());
			timeStartList2.add(detDto);

			busTypeDisable2 = true;

		}

	}

	public void getDataForTime3(TimeTableDTO TTDto) throws ParseException {
		travleTimeDTO = panelGenWithFixedTimeService.getTravleTimeForRouteBusCate(panelGenFixedTimeDTO.getRouteNo(),
				panelGenFixedTimeDTO.getBusCategoryDes());

		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		TimeTableDTO detDTO = new TimeTableDTO();
		TimeTableDTO detDTO1 = new TimeTableDTO();
		detDTO.setTimeRangeStart(TTDto.getTimeRangeStart());
		detDTO.setFrequency(TTDto.getFrequency());

		int addFrequncy = detDTO.getFrequency();
		String addFreqStr = String.valueOf(addFrequncy);
		String timeRange = detDTO.getTimeRangeStart();

		// add frequency for create start time
		timeConverteToString(addFrequncy);

		Date timeStart = timeFormat.parse(timeRange);
		Date dateWithFreq = timeFormat.parse(restTime);
		int noOfBus = TTDto.getNoOfBuses();
		// ---
		detDTO.setTimeRangeEnd(travleTimeDTO.getTimeRangeEnd());
		String addTimeString = detDTO.getTimeRangeEnd();
		// fixed
		String timeRangeCor = detDTO.getTimeRangeStart();
		Date timeStartCor = timeFormat.parse(timeRangeCor);
		// afterAdd=Long.parseLong(afterAddStr);
		long afterAddCor = timeStartCor.getTime() - dateWithFreq.getTime();

		afterAddStrCor = timeFormat.format(new Date(afterAddCor));
		detDTO.setTimeRangeStart(afterAddStrCor);
		// fixed end
		// ---

		String busAbbAtOrgin = panelGenFixedTimeDTO.getAbbriAtOrigin();
		for (int i = 0; i < noOfBus; i++) {

			l = l + 1;
			detDTO1 = new TimeTableDTO();
			String busName = busAbbAtOrgin + String.valueOf(l);
			detDTO1.setBusType(busName);
			busCatList3.add(detDTO1);

			afterAddStr = detDTO.getTimeRangeStart();

			String timeRange1 = detDTO.getTimeRangeStart();
			Date timeStart1 = timeFormat.parse(timeRange1);
			// afterAdd=Long.parseLong(afterAddStr);
			long afterAdd = timeStart1.getTime() + dateWithFreq.getTime();

			afterAddStr = timeFormat.format(new Date(afterAdd));

			TimeTableDTO detDto = new TimeTableDTO();
			detDto.setTimeRangeStart(detDTO.getTimeRangeStart());
			detDto.setFrequency(detDTO.getFrequency());
			detDto.setTimeStartVal(afterAddStr);
			TimeStartVal = detDto.getTimeStartVal();
			detDto.setTimeRangeStart(TimeStartVal);

			detDTO.setTimeRangeStart(detDto.getTimeRangeStart());

			// --

			Date date1 = timeFormat.parse(TimeStartVal);
			Date date2 = timeFormat.parse(addTimeString);

			long sum = date1.getTime() + date2.getTime();

			String date3 = timeFormat.format(new Date(sum));

			detDTO.setTimeEndVal(date3);
			detDto.setTimeEndVal(date3);
			detDto.setBusType(detDTO.getBusType());
			// ---
			timeStartList3.add(detDto);

		}
		// when frequency are zero

		if (noOfBus == 0) {
			TimeTableDTO detDto = new TimeTableDTO();
			detDTO.getTimeRangeStart();
			detDto.setTimeStartVal(detDTO.getTimeRangeStart());
			detDTO.getTimeRangeTo();
			detDto.setTimeEndVal(detDTO.getTimeRangeTo());
			System.out.println("test" + detDto.getTimeRangeStart());
			System.out.println("test2" + detDto.getTimeRangeTo());
			timeStartList3.add(detDto);
			busTypeDisable3 = false;

		}

	}

	/// for destination to origin

	public void getDataForTime1Second(TimeTableDTO TTDto) throws ParseException {
		travleTimeDTO = panelGenWithFixedTimeService.getTravleTimeForRouteBusCate(panelGenFixedTimeDTO.getRouteNo(),
				panelGenFixedTimeDTO.getBusCategoryDes());

		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		TimeTableDTO detDTO = new TimeTableDTO();
		TimeTableDTO detDTO1 = new TimeTableDTO();
		detDTO.setTimeRangeTo(TTDto.getTimeRangeTo());
		detDTO.setTimeRangeStart(TTDto.getTimeRangeStart());
		detDTO.setFrequency(TTDto.getFrequency());

		int addFrequncy = detDTO.getFrequency();
		String addFreqStr = String.valueOf(addFrequncy);
		String timeRange = detDTO.getTimeRangeStart();

		// add frequency for create start time
		timeConverteToString(addFrequncy);

		Date timeStart = timeFormat.parse(timeRange);
		Date dateWithFreq = timeFormat.parse(restTime);
		panelGenFixedTimeDTO.setNoOfBuses(TTDto.getNoOfBuses());
		int noOfBus = TTDto.getNoOfBuses();

		detDTO.setTimeRangeEnd(travleTimeDTO.getTimeRangeEnd());
		String addTimeString = detDTO.getTimeRangeEnd();

		// fixed
		String timeRangeCor = detDTO.getTimeRangeStart();
		Date timeStartCor = timeFormat.parse(timeRangeCor);
		// afterAdd=Long.parseLong(afterAddStr);
		long afterAddCor = timeStartCor.getTime() - dateWithFreq.getTime();

		afterAddStrCor = timeFormat.format(new Date(afterAddCor));
		detDTO.setTimeRangeStart(afterAddStrCor);
		// fixed end
		// ---
		String busAbbAtDestination = panelGenFixedTimeDTO.getAbbriAtDestination();
		for (int i = 0; i < noOfBus; i++) {

			jD = jD + 1;
			detDTO1 = new TimeTableDTO();
			String busName = busAbbAtDestination + String.valueOf(jD);
			detDTO1.setBusType(busName);
			busCatListDtoO.add(detDTO1);

			afterAddStr = detDTO.getTimeRangeStart();
			TimeTableDTO detDto = new TimeTableDTO();
			detDto.setTimeRangeStart(detDTO.getTimeRangeStart());

			String timeRange1 = detDTO.getTimeRangeStart();
			Date timeStart1 = timeFormat.parse(timeRange1);
			// afterAdd=Long.parseLong(afterAddStr);
			long afterAdd = timeStart1.getTime() + dateWithFreq.getTime();

			afterAddStr = timeFormat.format(new Date(afterAdd));

			// TimeTableDTO detDto = new TimeTableDTO();
			// detDto.setTimeRangeStart(detDTO.getTimeRangeStart());
			detDto.setFrequency(detDTO.getFrequency());
			// added
			//
			detDto.setTimeStartVal(afterAddStr);
			TimeStartVal = detDto.getTimeStartVal();
			detDto.setTimeRangeStart(TimeStartVal);

			detDTO.setTimeRangeStart(detDto.getTimeRangeStart());

			// --

			Date date1 = timeFormat.parse(TimeStartVal);
			Date date2 = timeFormat.parse(addTimeString);

			long sum = date1.getTime() + date2.getTime();

			String date3 = timeFormat.format(new Date(sum));

			detDTO.setTimeEndVal(date3);
			detDto.setTimeEndVal(date3);
			detDto.setBusType(detDTO.getBusType());
			// ---
			timeStartListSecond.add(detDto);

		}
		// when frequency are zero

		if (noOfBus == 0) {
			busTypeDisableD1 = true;
			TimeTableDTO detDto = new TimeTableDTO();
			detDTO.getTimeRangeStart();
			detDto.setTimeStartVal(detDTO.getTimeRangeStart());
			detDTO.getTimeRangeTo();
			detDto.setTimeEndVal(detDTO.getTimeRangeTo());
			System.out.println("test" + detDto.getTimeRangeStart());
			System.out.println("test2" + detDto.getTimeRangeTo());
			timeStartListSecond.add(detDto);

		}

	}

	public void getDataForTime2Second(TimeTableDTO TTDto) throws ParseException {
		travleTimeDTO = panelGenWithFixedTimeService.getTravleTimeForRouteBusCate(panelGenFixedTimeDTO.getRouteNo(),
				panelGenFixedTimeDTO.getBusCategoryDes());

		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		TimeTableDTO detDTO = new TimeTableDTO();
		TimeTableDTO detDTO1 = new TimeTableDTO();
		detDTO.setTimeRangeTo(TTDto.getTimeRangeTo());
		detDTO.setTimeRangeStart(TTDto.getTimeRangeStart());
		detDTO.setFrequency(TTDto.getFrequency());

		int addFrequncy = detDTO.getFrequency();
		String addFreqStr = String.valueOf(addFrequncy);
		String timeRange = detDTO.getTimeRangeStart();

		// add frequency for create start time
		timeConverteToString(addFrequncy);

		Date timeStart = timeFormat.parse(timeRange);
		Date dateWithFreq = timeFormat.parse(restTime);
		int noOfBus = TTDto.getNoOfBuses();
		// ---
		detDTO.setTimeRangeEnd(travleTimeDTO.getTimeRangeEnd());
		String addTimeString = detDTO.getTimeRangeEnd();
		// ---

		// fixed
		String timeRangeCor = detDTO.getTimeRangeStart();
		Date timeStartCor = timeFormat.parse(timeRangeCor);
		// afterAdd=Long.parseLong(afterAddStr);
		long afterAddCor = timeStartCor.getTime() - dateWithFreq.getTime();

		afterAddStrCor = timeFormat.format(new Date(afterAddCor));
		detDTO.setTimeRangeStart(afterAddStrCor);
		// fixed end
		// j=0;
		String busAbbAtDestination = panelGenFixedTimeDTO.getAbbriAtDestination();
		for (int i = 0; i < noOfBus; i++) {

			kD = kD + 1;
			detDTO1 = new TimeTableDTO();
			String busName = busAbbAtDestination + String.valueOf(kD);
			detDTO1.setBusType(busName);
			busCatListDtoO2.add(detDTO1);
			afterAddStr = detDTO.getTimeRangeStart();

			String timeRange1 = detDTO.getTimeRangeStart();
			Date timeStart1 = timeFormat.parse(timeRange1);
			// afterAdd=Long.parseLong(afterAddStr);
			long afterAdd = timeStart1.getTime() + dateWithFreq.getTime();

			afterAddStr = timeFormat.format(new Date(afterAdd));

			TimeTableDTO detDto = new TimeTableDTO();
			detDto.setTimeRangeStart(detDTO.getTimeRangeStart());
			detDto.setFrequency(detDTO.getFrequency());
			detDto.setTimeStartVal(afterAddStr);
			TimeStartVal = detDto.getTimeStartVal();
			detDto.setTimeRangeStart(TimeStartVal);

			detDTO.setTimeRangeStart(detDto.getTimeRangeStart());

			// --

			Date date1 = timeFormat.parse(TimeStartVal);
			Date date2 = timeFormat.parse(addTimeString);

			long sum = date1.getTime() + date2.getTime();

			String date3 = timeFormat.format(new Date(sum));

			detDTO.setTimeEndVal(date3);
			detDto.setTimeEndVal(date3);
			detDto.setBusType(detDTO.getBusType());
			// ---
			timeStartList2Second.add(detDto);

		}
		// when frequency are zero

		if (noOfBus == 0) {
			TimeTableDTO detDto = new TimeTableDTO();
			detDTO.getTimeRangeStart();
			detDto.setTimeStartVal(detDTO.getTimeRangeStart());
			detDTO.getTimeRangeTo();
			detDto.setTimeEndVal(detDTO.getTimeRangeTo());
			System.out.println("test" + detDto.getTimeRangeStart());
			System.out.println("test2" + detDto.getTimeRangeTo());
			timeStartList2Second.add(detDto);
			// busTypeDisable2=true;

		}

	}

	public void getDataForTime3Second(TimeTableDTO TTDto) throws ParseException {
		travleTimeDTO = panelGenWithFixedTimeService.getTravleTimeForRouteBusCate(panelGenFixedTimeDTO.getRouteNo(),
				panelGenFixedTimeDTO.getBusCategoryDes());

		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		TimeTableDTO detDTO = new TimeTableDTO();
		TimeTableDTO detDTO1 = new TimeTableDTO();
		detDTO.setTimeRangeStart(TTDto.getTimeRangeStart());
		detDTO.setFrequency(TTDto.getFrequency());

		int addFrequncy = detDTO.getFrequency();
		String addFreqStr = String.valueOf(addFrequncy);
		String timeRange = detDTO.getTimeRangeStart();

		// add frequency for create start time
		timeConverteToString(addFrequncy);

		Date timeStart = timeFormat.parse(timeRange);
		Date dateWithFreq = timeFormat.parse(restTime);
		int noOfBus = TTDto.getNoOfBuses();
		// ---
		detDTO.setTimeRangeEnd(travleTimeDTO.getTimeRangeEnd());
		String addTimeString = detDTO.getTimeRangeEnd();
		// fixed
		String timeRangeCor = detDTO.getTimeRangeStart();
		Date timeStartCor = timeFormat.parse(timeRangeCor);
		// afterAdd=Long.parseLong(afterAddStr);
		long afterAddCor = timeStartCor.getTime() - dateWithFreq.getTime();

		afterAddStrCor = timeFormat.format(new Date(afterAddCor));
		detDTO.setTimeRangeStart(afterAddStrCor);
		// fixed end
		// ---
		// j=0;
		String busAbbAtDestination = panelGenFixedTimeDTO.getAbbriAtDestination();
		for (int i = 0; i < noOfBus; i++) {

			lD = lD + 1;
			detDTO1 = new TimeTableDTO();
			String busName = busAbbAtDestination + String.valueOf(lD);
			detDTO1.setBusType(busName);
			busCatListDtoO3.add(detDTO1);
			afterAddStr = detDTO.getTimeRangeStart();

			String timeRange1 = detDTO.getTimeRangeStart();
			Date timeStart1 = timeFormat.parse(timeRange1);
			// afterAdd=Long.parseLong(afterAddStr);
			long afterAdd = timeStart1.getTime() + dateWithFreq.getTime();

			afterAddStr = timeFormat.format(new Date(afterAdd));

			TimeTableDTO detDto = new TimeTableDTO();
			detDto.setTimeRangeStart(detDTO.getTimeRangeStart());
			detDto.setFrequency(detDTO.getFrequency());
			detDto.setTimeStartVal(afterAddStr);
			TimeStartVal = detDto.getTimeStartVal();
			detDto.setTimeRangeStart(TimeStartVal);

			detDTO.setTimeRangeStart(detDto.getTimeRangeStart());

			// --

			Date date1 = timeFormat.parse(TimeStartVal);
			Date date2 = timeFormat.parse(addTimeString);

			long sum = date1.getTime() + date2.getTime();

			String date3 = timeFormat.format(new Date(sum));

			detDTO.setTimeEndVal(date3);
			detDto.setTimeEndVal(date3);
			detDto.setBusType(detDTO.getBusType());
			// ---
			timeStartList3Second.add(detDto);

		}
		// when frequency are zero

		if (noOfBus == 0) {
			TimeTableDTO detDto = new TimeTableDTO();
			detDTO.getTimeRangeStart();
			detDto.setTimeStartVal(detDTO.getTimeRangeStart());
			detDTO.getTimeRangeTo();
			detDto.setTimeEndVal(detDTO.getTimeRangeTo());
			System.out.println("test" + detDto.getTimeRangeStart());
			System.out.println("test2" + detDto.getTimeRangeTo());
			timeStartList3Second.add(detDto);
			busTypeDisable3 = false;

		}

	}

	///// destination to origin finished
	public String timeConverteToString(int time) {

		int hours = time / 60;
		int minutes = time % 60;

		if (hours >= 0 && hours < 10) {

			if (minutes >= 0 && minutes < 10) {
				restTime = String.valueOf("0" + hours + ":" + "0" + minutes);
			} else {
				restTime = String.valueOf("0" + hours + ":" + minutes);
			}

		} else {

			if (minutes >= 0 && minutes < 10) {
				restTime = String.valueOf(hours + ":" + "0" + minutes);
			} else {
				restTime = String.valueOf(hours + ":" + minutes);
			}

		}

		return restTime;
	}

	public void getbusTypes() {

	}

	// not used
	public void getEndTImeList(List<TimeTableDTO> timeStartList2, TimeTableDTO tTDto) throws ParseException {
		travleTimeDTO = panelGenWithFixedTimeService.getTravleTimeForRouteBusCate(panelGenFixedTimeDTO.getRouteNo(),
				panelGenFixedTimeDTO.getBusCategoryDes());
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		// get travel time for end location
		TimeTableDTO detDTO = new TimeTableDTO();
		detDTO.setTimeRangeEnd(travleTimeDTO.getTimeRangeEnd());
		String addTimeString = detDTO.getTimeRangeEnd();
		int noOfBus = tTDto.getNoOfBuses();
		// for(TimeTableDTO TimeDTO :timeStartList2) {
		for (int i = 0; i < noOfBus; i++) {
			TimeTableDTO TimeStartValDTO = new TimeTableDTO();
			TimeStartValDTO = timeStartList2.get(i);
			TimeStartVal = TimeStartValDTO.getTimeStartVal();
			Date date1 = timeFormat.parse(TimeStartVal);
			Date date2 = timeFormat.parse(addTimeString);

			long sum = date1.getTime() + date2.getTime();

			String date3 = timeFormat.format(new Date(sum));

			detDTO.setTimeEndVal(date3);
			timeStartList.add(detDTO);
		}
	}

	public void ClearButton() {
		panelGenFixedTimeDTO.setRouteNo(null);
		panelGenFixedTimeDTO.setBusCategory(null);
		panelGenFixedTimeDTO.setBusCategoryDes(null);
		panelGenFixedTimeDTO.setDestination(null);
		panelGenFixedTimeDTO.setOrigin(null);

		saveBtnDisable = false;
		ClearAllRecords();
	}

	public void setBusType(TimeTableDTO dto1) {

		saveG1OrtoDe = true;

		System.out.println("set selected bus type");
		panelGenFixedTimeDTO.getBusType1();

		List<TimeTableDTO> tempBusTypeSetList = new ArrayList<>();

		for (TimeTableDTO dto : timeStartList) {
			if (dto.getTimeStartVal().equalsIgnoreCase(dto1.getTimeStartVal())
					&& dto.getTimeEndVal().equals(dto1.getTimeEndVal())) {
				// tempBusTypeSetList.remove(dto);
				TimeTableDTO tempDTO = new TimeTableDTO();
				tempDTO.setTimeStartVal(dto.getTimeStartVal());
				tempDTO.setTimeEndVal(dto.getTimeEndVal());
				tempDTO.setBusType(panelGenFixedTimeDTO.getBusType1());
				tempBusTypeSetList.add(tempDTO);
			} else {
				tempBusTypeSetList.add(dto);
			}
		}

		timeStartList = tempBusTypeSetList;

		int noOfTrips = timeStartList.size();
		System.out.println("size" + noOfTrips);

	}

	public void setBusType2(TimeTableDTO dto1) {

		saveG2OrtoDe = true;

		System.out.println("set selected bus type");
		panelGenFixedTimeDTO.getBusType();

		List<TimeTableDTO> tempBusTypeSetList2 = new ArrayList<>();

		for (TimeTableDTO dto : timeStartList2) {
			if (dto.getTimeStartVal().equalsIgnoreCase(dto1.getTimeStartVal())
					&& dto.getTimeEndVal().equals(dto1.getTimeEndVal())) {
				// tempBusTypeSetList.remove(dto);
				TimeTableDTO tempDTO = new TimeTableDTO();
				tempDTO.setTimeStartVal(dto.getTimeStartVal());
				tempDTO.setTimeEndVal(dto.getTimeEndVal());
				tempDTO.setBusType(panelGenFixedTimeDTO.getBusType2());
				tempBusTypeSetList2.add(tempDTO);
			} else {
				tempBusTypeSetList2.add(dto);
			}
		}

		timeStartList2 = tempBusTypeSetList2;

		int noOfTrips = timeStartList2.size();
		System.out.println("size" + noOfTrips);
	}

	public void setBusType3(TimeTableDTO dto1) {

		saveG3OrtoDe = true;

		System.out.println("set selected bus type");
		panelGenFixedTimeDTO.getBusType();

		List<TimeTableDTO> tempBusTypeSetList3 = new ArrayList<>();

		for (TimeTableDTO dto : timeStartList3) {
			if (dto.getTimeStartVal().equalsIgnoreCase(dto1.getTimeStartVal())
					&& dto.getTimeEndVal().equals(dto1.getTimeEndVal())) {
				// tempBusTypeSetList.remove(dto);
				TimeTableDTO tempDTO = new TimeTableDTO();
				tempDTO.setTimeStartVal(dto.getTimeStartVal());
				tempDTO.setTimeEndVal(dto.getTimeEndVal());
				tempDTO.setBusType(panelGenFixedTimeDTO.getBusType3());
				tempBusTypeSetList3.add(tempDTO);
			} else {
				tempBusTypeSetList3.add(dto);
			}
		}

		timeStartList3 = tempBusTypeSetList3;

		int noOfTrips = timeStartList3.size();
		System.out.println("size" + noOfTrips);
	}

	public void setBusTypeDtoO(TimeTableDTO dto1) {

		saveG1DetoOr = true;

		System.out.println("set selected bus type");
		panelGenFixedTimeDTO.getBusType();

		List<TimeTableDTO> tempBusTypeSetList = new ArrayList<>();

		for (TimeTableDTO dto : timeStartListSecond) {
			if (dto.getTimeStartVal().equalsIgnoreCase(dto1.getTimeStartVal())
					&& dto.getTimeEndVal().equals(dto1.getTimeEndVal())) {
				// tempBusTypeSetList.remove(dto);
				TimeTableDTO tempDTO = new TimeTableDTO();
				tempDTO.setTimeStartVal(dto.getTimeStartVal());
				tempDTO.setTimeEndVal(dto.getTimeEndVal());
				tempDTO.setBusType(panelGenFixedTimeDTO.getBusTypeDtoO1());
				tempBusTypeSetList.add(tempDTO);
			} else {
				tempBusTypeSetList.add(dto);
			}
		}

		timeStartListSecond = tempBusTypeSetList;

		int noOfTrips = timeStartListSecond.size();
		System.out.println("size" + noOfTrips);

	}

	public void setBusTypeDtoO2(TimeTableDTO dto1) {

		saveG2DetoOr = true;

		System.out.println("set selected bus type");
		panelGenFixedTimeDTO.getBusType();

		List<TimeTableDTO> tempBusTypeSetList = new ArrayList<>();

		for (TimeTableDTO dto : timeStartList2Second) {
			if (dto.getTimeStartVal().equalsIgnoreCase(dto1.getTimeStartVal())
					&& dto.getTimeEndVal().equals(dto1.getTimeEndVal())) {
				// tempBusTypeSetList.remove(dto);
				TimeTableDTO tempDTO = new TimeTableDTO();
				tempDTO.setTimeStartVal(dto.getTimeStartVal());
				tempDTO.setTimeEndVal(dto.getTimeEndVal());
				tempDTO.setBusType(panelGenFixedTimeDTO.getBusTypeDtoO2());
				tempBusTypeSetList.add(tempDTO);
			} else {
				tempBusTypeSetList.add(dto);
			}
		}

		timeStartList2Second = tempBusTypeSetList;

		int noOfTrips = timeStartList2Second.size();
		System.out.println("size" + noOfTrips);

	}

	public void setBusTypeDtoO3(TimeTableDTO dto1) {

		saveG3DetoOr = true;

		System.out.println("set selected bus type");
		panelGenFixedTimeDTO.getBusType();

		List<TimeTableDTO> tempBusTypeSetList = new ArrayList<>();

		for (TimeTableDTO dto : timeStartList3Second) {
			if (dto.getTimeStartVal().equalsIgnoreCase(dto1.getTimeStartVal())
					&& dto.getTimeEndVal().equals(dto1.getTimeEndVal())) {
				// tempBusTypeSetList.remove(dto);
				TimeTableDTO tempDTO = new TimeTableDTO();
				tempDTO.setTimeStartVal(dto.getTimeStartVal());
				tempDTO.setTimeEndVal(dto.getTimeEndVal());
				tempDTO.setBusType(panelGenFixedTimeDTO.getBusTypeDtoO3());
				tempBusTypeSetList.add(tempDTO);
			} else {
				tempBusTypeSetList.add(dto);
			}
		}

		timeStartList3Second = tempBusTypeSetList;

		int noOfTrips = timeStartList3Second.size();
		System.out.println("size" + noOfTrips);

	}

	public void clearBusesInList() {
		timeStartList = new ArrayList<TimeTableDTO>();
		timeStartList2 = new ArrayList<>();
		timeStartList3 = new ArrayList<>();
		timeStartListSecond = new ArrayList<>();
		timeStartList2Second = new ArrayList<>();
		timeStartList3Second = new ArrayList<>();
		busCatList = new ArrayList<>();
		busCatList2 = new ArrayList<>();
		busCatList3 = new ArrayList<>();
		busCatListDtoO = new ArrayList<>();
		busCatListDtoO2 = new ArrayList<>();
		busCatListDtoO3 = new ArrayList<>();

	}

	public void saveRecords() throws ParseException {

		// save data in master table
		ArrayList<String> etcBusesList = new ArrayList<>();
		ArrayList<String> sLTBBusesList = new ArrayList<>();
		ArrayList<String> pvtBusesList = new ArrayList<>();

		boolean dataSet1 = false;
		boolean dataSet2 = false;
		boolean dataSet3 = false;
		boolean dataSet4 = false;
		boolean dataSet5 = false;
		boolean dataSet6 = false;

		panelGenFixedTimeDTO.getBusType();
		busCategoryList.add(panelGenFixedTimeDTO);
//		String TimeTableRefNo = null;

		if (timeStartList != null && !timeStartList.isEmpty() && !timeStartList.contains(" ")) {

			dataSet1 = true;

			// get number of ETC and CTB busses for group1 origin
			System.out.println("trip ref no is" + panelGenFixedTimeDTO.getTripRefNo());
			nonPrivateBusDTO = panelGenWithFixedTimeService.getNoOfNonPrivateBusses(panelGenFixedTimeDTO.getTripRefNo(),
					"1", "O");

			nonSltbEtcBusDTO = panelGenWithFixedTimeService.getNoOfSltbEtcBusses(panelGenFixedTimeDTO.getTripRefNo(),
					"1");

			for (TimeTableDTO td : timeStartList) {
				if (td.getBusType() != null && !td.getBusType().trim().isEmpty()) {
					if (td.getBusType().equals("ETC-O") || td.getBusType().equals("ETC-D")) {
						String etcBuses = td.getBusType();
						etcBusesList.add(etcBuses);

					} else if (td.getBusType().equals("SLTB-O") || td.getBusType().equals("SLTB-D")) {
						String sLTBBuses = td.getBusType();
						sLTBBusesList.add(sLTBBuses);
					} else {
						String pvtBBuses = td.getBusType();
						pvtBusesList.add(pvtBBuses);
					}
				}
			}

		}

		etcBusesList = new ArrayList<>();
		sLTBBusesList = new ArrayList<>();
		pvtBusesList = new ArrayList<>();
		if (timeStartList2 != null) {
			dataSet2 = true;
			// get number of ETC and CTB busses for group1 origin
			System.out.println("trip ref no is" + panelGenFixedTimeDTO.getTripRefNo());
			nonPrivateBusDTO = panelGenWithFixedTimeService.getNoOfNonPrivateBusses(panelGenFixedTimeDTO.getTripRefNo(),
					"2", "O");
			nonSltbEtcBusDTO = panelGenWithFixedTimeService.getNoOfSltbEtcBusses(panelGenFixedTimeDTO.getTripRefNo(),
					"2");
			for (TimeTableDTO td : timeStartList2) {
				if (td.getBusType() != null && !td.getBusType().trim().isEmpty()) {
					if (td.getBusType().equals("ETC-O") || td.getBusType().equals("ETC-D")) {
						String etcBuses = td.getBusType();
						etcBusesList.add(etcBuses);

					} else if (td.getBusType().equals("SLTB-O") || td.getBusType().equals("SLTB-D")) {
						String sLTBBuses = td.getBusType();
						sLTBBusesList.add(sLTBBuses);
					} else {
						String pvtBBuses = td.getBusType();
						pvtBusesList.add(pvtBBuses);
					}
				}
			}

		}

		etcBusesList = new ArrayList<>();
		sLTBBusesList = new ArrayList<>();
		pvtBusesList = new ArrayList<>();
		if (timeStartList3 != null) {
			dataSet3 = true;
			// get number of ETC and CTB busses for group1 origin
			System.out.println("trip ref no is" + panelGenFixedTimeDTO.getTripRefNo());
			nonPrivateBusDTO = panelGenWithFixedTimeService.getNoOfNonPrivateBusses(panelGenFixedTimeDTO.getTripRefNo(),
					"3", "O");
			nonSltbEtcBusDTO = panelGenWithFixedTimeService.getNoOfSltbEtcBusses(panelGenFixedTimeDTO.getTripRefNo(),
					"3");
			for (TimeTableDTO td : timeStartList3) {
				if (td.getBusType() != null && !td.getBusType().trim().isEmpty()) {
					if (td.getBusType().equals("ETC-O") || td.getBusType().equals("ETC-D")) {
						String etcBuses = td.getBusType();
						etcBusesList.add(etcBuses);

					} else if (td.getBusType().equals("SLTB-O") || td.getBusType().equals("SLTB-D")) {
						String sLTBBuses = td.getBusType();
						sLTBBusesList.add(sLTBBuses);
					} else {
						String pvtBBuses = td.getBusType();
						pvtBusesList.add(pvtBBuses);
					}
				}
			}

		}

		etcBusesList = new ArrayList<>();
		sLTBBusesList = new ArrayList<>();
		pvtBusesList = new ArrayList<>();

		if (timeStartListSecond != null) {
			dataSet4 = true;
			// get number of ETC and CTB busses for group1 origin
			System.out.println("trip ref no is" + panelGenFixedTimeDTO.getTripRefNo());
			nonPrivateBusDTO = panelGenWithFixedTimeService.getNoOfNonPrivateBusses(panelGenFixedTimeDTO.getTripRefNo(),
					"1", "D");
			nonSltbEtcBusDTO = panelGenWithFixedTimeService.getNoOfSltbEtcBusses(panelGenFixedTimeDTO.getTripRefNo(),
					"1");
			for (TimeTableDTO td : timeStartListSecond) {
				if (td.getBusType() != null && !td.getBusType().trim().isEmpty()) {
					if (td.getBusType().equals("ETC-D") || td.getBusType().equals("ETC-O")) {
						String etcBuses = td.getBusType();
						etcBusesList.add(etcBuses);

					} else if (td.getBusType().equals("SLTB-D") || td.getBusType().equals("SLTB-O")) {
						String sLTBBuses = td.getBusType();
						sLTBBusesList.add(sLTBBuses);
					} else {
						String pvtBBuses = td.getBusType();
						pvtBusesList.add(pvtBBuses);
					}
				}
			}

		}

		etcBusesList = new ArrayList<>();
		sLTBBusesList = new ArrayList<>();
		pvtBusesList = new ArrayList<>();

		if (timeStartList2Second != null) {
			dataSet5 = true;
			// get number of ETC and CTB busses for group1 origin
			System.out.println("trip ref no is" + panelGenFixedTimeDTO.getTripRefNo());
			nonPrivateBusDTO = panelGenWithFixedTimeService.getNoOfNonPrivateBusses(panelGenFixedTimeDTO.getTripRefNo(),
					"2", "D");
			nonSltbEtcBusDTO = panelGenWithFixedTimeService.getNoOfSltbEtcBusses(panelGenFixedTimeDTO.getTripRefNo(),
					"2");
			for (TimeTableDTO td : timeStartList2Second) {
				if (td.getBusType() != null && !td.getBusType().trim().isEmpty()) {
					if (td.getBusType().equals("ETC-D") || td.getBusType().equals("ETC-O")) {
						String etcBuses = td.getBusType();
						etcBusesList.add(etcBuses);

					} else if (td.getBusType().equals("SLTB-D") || td.getBusType().equals("SLTB-O")) {
						String sLTBBuses = td.getBusType();
						sLTBBusesList.add(sLTBBuses);
					} else {
						String pvtBBuses = td.getBusType();
						pvtBusesList.add(pvtBBuses);
					}
				}
			}

		}

		etcBusesList = new ArrayList<>();
		sLTBBusesList = new ArrayList<>();
		pvtBusesList = new ArrayList<>();

		if (timeStartList3Second != null) {
			dataSet6 = true;
			// get number of ETC and CTB busses for group1 origin
			System.out.println("trip ref no is" + panelGenFixedTimeDTO.getTripRefNo());
			nonPrivateBusDTO = panelGenWithFixedTimeService.getNoOfNonPrivateBusses(panelGenFixedTimeDTO.getTripRefNo(),
					"3", "D");
			nonSltbEtcBusDTO = panelGenWithFixedTimeService.getNoOfSltbEtcBusses(panelGenFixedTimeDTO.getTripRefNo(),
					"3");
			for (TimeTableDTO td : timeStartList3Second) {
				if (td.getBusType() != null && !td.getBusType().trim().isEmpty()) {
					if (td.getBusType().equals("ETC-D") || td.getBusType().equals("ETC-O")) {
						String etcBuses = td.getBusType();
						etcBusesList.add(etcBuses);

					} else if (td.getBusType().equals("SLTB-D") || td.getBusType().equals("SLTB-O")) {
						String sLTBBuses = td.getBusType();
						sLTBBusesList.add(sLTBBuses);
					} else {
						String pvtBBuses = td.getBusType();
						pvtBusesList.add(pvtBBuses);
					}
				}
			}

		}

		if (dataSet1 || dataSet2 || dataSet3 || dataSet4 || dataSet5 || dataSet6) {

			if (dataSet1 && dataSet4) {

				// validate fixed bus counts on both sides | added by thilna.d on 14-10-2021
				if (!isValidFixedBusCountsOnBothSides(timeStartList, timeStartListSecond)) {
					setErrorMsg("Bus number count mismatch in origin and destination side.");
					RequestContext.getCurrentInstance().update("requiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					timeStartList = new ArrayList<>();
					timeStartListSecond = new ArrayList<>();
					Search();
					return;
				}

				String timeTableRefNoOrigin = panelGenWithFixedTimeService.generateTimeTableRefNo("1", "O");
				String timeTableRefNoDes = panelGenWithFixedTimeService.generateTimeTableRefNo("1", "D");
				
				panelGenWithFixedTimeService.savePanelGeneratorWithFixedTimeAllInOne(noOFTripsValGOne, panelGenFixedTimeDTO.getPanelGenNo(), "1", loginUser, timeTableRefNoOrigin, timeTableRefNoDes,
						timeStartList, timeStartListSecond);

				noOfBus = panelGenFixedTimeDTO.getNoOfBuses();
				
				// get number of ETC and CTB busses for group1 destination
				nonPrivateBusDTO = panelGenWithFixedTimeService.getNoOfNonPrivateBusses(panelGenFixedTimeDTO.getTripRefNo(), "1", "D");

				tripsG1O = panelGenWithFixedTimeService.getNoOfTripsavail(panelGenFixedTimeDTO.getPanelGenNo(), "1", "O");
				tripsG1D = panelGenWithFixedTimeService.getNoOfTripsavail(panelGenFixedTimeDTO.getPanelGenNo(), "1", "D");
			}
			
			if (dataSet2 && dataSet5) {

				// validate fixed bus counts on both sides | added by thilna.d on 14-10-2021
				if (!isValidFixedBusCountsOnBothSides(timeStartList2, timeStartList2Second)) {
					setErrorMsg("Bus number count mismatch in origin and destination side.");
					RequestContext.getCurrentInstance().update("requiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					timeStartList2 = new ArrayList<>();
					timeStartList2Second = new ArrayList<>();
					Search();
					return;
				}

				String timeTableRefNoOrigin = panelGenWithFixedTimeService.generateTimeTableRefNo("2", "O");
				String timeTableRefNoDes = panelGenWithFixedTimeService.generateTimeTableRefNo("2", "D");
				
				panelGenWithFixedTimeService.savePanelGeneratorWithFixedTimeAllInOne(noOFTripsValGOne, panelGenFixedTimeDTO.getPanelGenNo(), "2", loginUser, timeTableRefNoOrigin, timeTableRefNoDes,
						timeStartList2, timeStartList2Second);

				noOfBus = panelGenFixedTimeDTO.getNoOfBuses();
				
				// get number of ETC and CTB busses for group2 origin
				nonPrivateBusDTO = panelGenWithFixedTimeService.getNoOfNonPrivateBusses(panelGenFixedTimeDTO.getTripRefNo(), "2", "O");
				// get number of ETC and CTB busses for group2 destination
				nonPrivateBusDTO = panelGenWithFixedTimeService.getNoOfNonPrivateBusses(panelGenFixedTimeDTO.getTripRefNo(), "2", "D");
				
				tripsG2O = panelGenWithFixedTimeService.getNoOfTripsavail(panelGenFixedTimeDTO.getPanelGenNo(), "2", "O");
				tripsG2D = panelGenWithFixedTimeService.getNoOfTripsavail(panelGenFixedTimeDTO.getPanelGenNo(), "2", "D");
			}

			if (dataSet3 && dataSet6) {

				// validate fixed bus counts on both sides | added by thilna.d on 14-10-2021
				if (!isValidFixedBusCountsOnBothSides(timeStartList3, timeStartList3Second)) {
					setErrorMsg("Bus number count mismatch in origin and destination side.");
					RequestContext.getCurrentInstance().update("requiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					timeStartList3 = new ArrayList<>();
					timeStartList3Second = new ArrayList<>();
					Search();
					return;
				}

				String timeTableRefNoOrigin = panelGenWithFixedTimeService.generateTimeTableRefNo("3", "O");
				String timeTableRefNoDes = panelGenWithFixedTimeService.generateTimeTableRefNo("3", "D");
				
				panelGenWithFixedTimeService.savePanelGeneratorWithFixedTimeAllInOne(noOFTripsValGOne, panelGenFixedTimeDTO.getPanelGenNo(), "3", loginUser, timeTableRefNoOrigin, timeTableRefNoDes,
						timeStartList3, timeStartList3Second);

				noOfBus = panelGenFixedTimeDTO.getNoOfBuses();
				
				// get number of ETC and CTB busses for group3 origin
				nonPrivateBusDTO = panelGenWithFixedTimeService.getNoOfNonPrivateBusses(panelGenFixedTimeDTO.getTripRefNo(), "3", "O");
				// get number of ETC and CTB busses for group3 destination
				nonPrivateBusDTO = panelGenWithFixedTimeService.getNoOfNonPrivateBusses(panelGenFixedTimeDTO.getTripRefNo(), "3", "D");

				tripsG3O = panelGenWithFixedTimeService.getNoOfTripsavail(panelGenFixedTimeDTO.getPanelGenNo(), "3", "O");
				tripsG3D = panelGenWithFixedTimeService.getNoOfTripsavail(panelGenFixedTimeDTO.getPanelGenNo(), "3", "D");
			}

			saveBtnDisable = true;
			setSucessMsg("Saved successfully.");
			RequestContext.getCurrentInstance().update("successSve");
			RequestContext.getCurrentInstance().execute("PF('successSve').show()");
			ClearAllRecords();
		}

	}

	public boolean isValidFixedBusCountsOnBothSides(List<TimeTableDTO> originList, List<TimeTableDTO> destinList) {
		boolean returnValue = false;
		int originListCount = 0;
		int destinListCount = 0;
		for (TimeTableDTO td : originList) {
			if (td.getBusType() != null) {
				originListCount++;
			}
		}
		for (TimeTableDTO td : destinList) {
			if (td.getBusType() != null) {
				destinListCount++;
			}
		}
		if (originListCount == destinListCount) {
			returnValue = true;
		}
		return returnValue;
	}

	public void ClearAllRecords() {

		timeStartList = new ArrayList<>();
		timeStartList2 = new ArrayList<>();
		timeStartList3 = new ArrayList<>();
		timeStartListSecond = new ArrayList<>();
		timeStartList2Second = new ArrayList<>();
		timeStartList3Second = new ArrayList<>();
		busCatList = new ArrayList<>();
		busCatList2 = new ArrayList<>();
		busCatList3 = new ArrayList<>();
		busCatListDtoO = new ArrayList<>();
		busCatListDtoO2 = new ArrayList<>();
		busCatListDtoO3 = new ArrayList<>();
		panelGenFixedTimeDTO.setRouteNo(null);
		panelGenFixedTimeDTO.setBusCategory(null);
		panelGenFixedTimeDTO.setBusCategoryDes(null);
		panelGenFixedTimeDTO.setBusType(null);
		tripsG1O = 0;
		tripsG2O = 0;
		tripsG3O = 0;
		tripsG1D = 0;
		tripsG2D = 0;
		tripsG3D = 0;

		saveBtnDisable = false;

		panelGenFixedTimeDTO.setDestination(null);
		panelGenFixedTimeDTO.setOrigin(null);

		j = 0;
		k = 0;
		l = 0;
		jD = 0;
		kD = 0;
		lD = 0;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public TimeTableService getPanelGenWithFixedTimeService() {
		return panelGenWithFixedTimeService;
	}

	public void setPanelGenWithFixedTimeService(TimeTableService panelGenWithFixedTimeService) {
		this.panelGenWithFixedTimeService = panelGenWithFixedTimeService;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public TimeTableDTO getPanelGenFixedTimeDTO() {
		return panelGenFixedTimeDTO;
	}

	public void setPanelGenFixedTimeDTO(TimeTableDTO panelGenFixedTimeDTO) {
		this.panelGenFixedTimeDTO = panelGenFixedTimeDTO;
	}

	public List<TimeTableDTO> getRouteNoList() {
		return routeNoList;
	}

	public void setRouteNoList(List<TimeTableDTO> routeNoList) {
		this.routeNoList = routeNoList;
	}

	public List<TimeTableDTO> getBusCategoryList() {
		return busCategoryList;
	}

	public void setBusCategoryList(List<TimeTableDTO> busCategoryList) {
		this.busCategoryList = busCategoryList;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public List<TimeTableDTO> getShowTimeSlotDet1() {
		return showTimeSlotDet1;
	}

	public void setShowTimeSlotDet1(List<TimeTableDTO> showTimeSlotDet1) {
		this.showTimeSlotDet1 = showTimeSlotDet1;
	}

	public List<TimeTableDTO> getShowTimeSlotDet2() {
		return showTimeSlotDet2;
	}

	public void setShowTimeSlotDet2(List<TimeTableDTO> showTimeSlotDet2) {
		this.showTimeSlotDet2 = showTimeSlotDet2;
	}

	public List<TimeTableDTO> getShowTimeSlotDet3() {
		return showTimeSlotDet3;
	}

	public void setShowTimeSlotDet3(List<TimeTableDTO> showTimeSlotDet3) {
		this.showTimeSlotDet3 = showTimeSlotDet3;
	}

	public TimeTableDTO getTravleTimeDTO() {
		return travleTimeDTO;
	}

	public void setTravleTimeDTO(TimeTableDTO travleTimeDTO) {
		this.travleTimeDTO = travleTimeDTO;
	}

	public TimeTableDTO getBusCategoryVal() {
		return busCategoryVal;
	}

	public void setBusCategoryVal(TimeTableDTO busCategoryVal) {
		this.busCategoryVal = busCategoryVal;
	}

	public List<TimeTableDTO> getTimeStartList() {
		return timeStartList;
	}

	public void setTimeStartList(List<TimeTableDTO> timeStartList) {
		this.timeStartList = timeStartList;
	}

	public String getRestTime() {
		return restTime;
	}

	public void setRestTime(String restTime) {
		this.restTime = restTime;
	}

	public String getAfterAddStr() {
		return afterAddStr;
	}

	public void setAfterAddStr(String afterAddStr) {
		this.afterAddStr = afterAddStr;
	}

	public long getAfterAdd() {
		return afterAdd;
	}

	public void setAfterAdd(long afterAdd) {
		this.afterAdd = afterAdd;
	}

	public TimeTableDTO getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(TimeTableDTO selectedRow) {
		this.selectedRow = selectedRow;
	}

	public List<TimeTableDTO> getBusTypeList() {
		return busTypeList;
	}

	public void setBusTypeList(List<TimeTableDTO> busTypeList) {
		this.busTypeList = busTypeList;
	}

	public String getTimeStartVal() {
		return TimeStartVal;
	}

	public void setTimeStartVal(String timeStartVal) {
		TimeStartVal = timeStartVal;
	}

	public TimeTableDTO getRefNoDTO() {
		return refNoDTO;
	}

	public void setRefNoDTO(TimeTableDTO refNoDTO) {
		this.refNoDTO = refNoDTO;
	}

	public ArrayList<String> getGroupAndTripTypeDTO() {
		return groupAndTripTypeDTO;
	}

	public void setGroupAndTripTypeDTO(ArrayList<String> groupAndTripTypeDTO) {
		this.groupAndTripTypeDTO = groupAndTripTypeDTO;
	}

	public List<TimeTableDTO> getTimeStartList2() {
		return timeStartList2;
	}

	public void setTimeStartList2(List<TimeTableDTO> timeStartList2) {
		this.timeStartList2 = timeStartList2;
	}

	public List<TimeTableDTO> getTimeStartList3() {
		return timeStartList3;
	}

	public void setTimeStartList3(List<TimeTableDTO> timeStartList3) {
		this.timeStartList3 = timeStartList3;
	}

	public boolean isBusTypeDisable1() {
		return busTypeDisable1;
	}

	public void setBusTypeDisable1(boolean busTypeDisable1) {
		this.busTypeDisable1 = busTypeDisable1;
	}

	public boolean isBusTypeDisable2() {
		return busTypeDisable2;
	}

	public void setBusTypeDisable2(boolean busTypeDisable2) {
		this.busTypeDisable2 = busTypeDisable2;
	}

	public boolean isBusTypeDisable3() {
		return busTypeDisable3;
	}

	public void setBusTypeDisable3(boolean busTypeDisable3) {
		this.busTypeDisable3 = busTypeDisable3;
	}

	public List<TimeTableDTO> getTimeStartListSecond() {
		return timeStartListSecond;
	}

	public void setTimeStartListSecond(List<TimeTableDTO> timeStartListSecond) {
		this.timeStartListSecond = timeStartListSecond;
	}

	public List<TimeTableDTO> getTimeStartList2Second() {
		return timeStartList2Second;
	}

	public void setTimeStartList2Second(List<TimeTableDTO> timeStartList2Second) {
		this.timeStartList2Second = timeStartList2Second;
	}

	public List<TimeTableDTO> getTimeStartList3Second() {
		return timeStartList3Second;
	}

	public void setTimeStartList3Second(List<TimeTableDTO> timeStartList3Second) {
		this.timeStartList3Second = timeStartList3Second;
	}

	public List<TimeTableDTO> getBusTypeSetList() {
		return busTypeSetList;
	}

	public void setBusTypeSetList(List<TimeTableDTO> busTypeSetList) {
		this.busTypeSetList = busTypeSetList;
	}

	public int getNoOfBus() {
		return noOfBus;
	}

	public void setNoOfBus(int noOfBus) {
		this.noOfBus = noOfBus;
	}

	public List<TimeTableDTO> getBusCatList() {
		return busCatList;
	}

	public void setBusCatList(List<TimeTableDTO> busCatList) {
		this.busCatList = busCatList;
	}

	public List<TimeTableDTO> getBusTypeSetList2() {
		return busTypeSetList2;
	}

	public void setBusTypeSetList2(List<TimeTableDTO> busTypeSetList2) {
		this.busTypeSetList2 = busTypeSetList2;
	}

	public List<TimeTableDTO> getBusTypeSetList3() {
		return busTypeSetList3;
	}

	public void setBusTypeSetList3(List<TimeTableDTO> busTypeSetList3) {
		this.busTypeSetList3 = busTypeSetList3;
	}

	public String getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(String loginUser) {
		this.loginUser = loginUser;
	}

	public int getNoOFTripsValGOne() {
		return noOFTripsValGOne;
	}

	public void setNoOFTripsValGOne(int noOFTripsValGOne) {
		this.noOFTripsValGOne = noOFTripsValGOne;
	}

	public int getNoOFTripsValGTwo() {
		return noOFTripsValGTwo;
	}

	public void setNoOFTripsValGTwo(int noOFTripsValGTwo) {
		this.noOFTripsValGTwo = noOFTripsValGTwo;
	}

	public int getNoOFTripsValGThree() {
		return noOFTripsValGThree;
	}

	public void setNoOFTripsValGThree(int noOFTripsValGThree) {
		this.noOFTripsValGThree = noOFTripsValGThree;
	}

	public List<TimeTableDTO> getBusCatList2() {
		return busCatList2;
	}

	public void setBusCatList2(List<TimeTableDTO> busCatList2) {
		this.busCatList2 = busCatList2;
	}

	public List<TimeTableDTO> getBusCatList3() {
		return busCatList3;
	}

	public void setBusCatList3(List<TimeTableDTO> busCatList3) {
		this.busCatList3 = busCatList3;
	}

	public List<TimeTableDTO> getBusCatListDtoO() {
		return busCatListDtoO;
	}

	public void setBusCatListDtoO(List<TimeTableDTO> busCatListDtoO) {
		this.busCatListDtoO = busCatListDtoO;
	}

	public List<TimeTableDTO> getBusCatListDtoO2() {
		return busCatListDtoO2;
	}

	public void setBusCatListDtoO2(List<TimeTableDTO> busCatListDtoO2) {
		this.busCatListDtoO2 = busCatListDtoO2;
	}

	public List<TimeTableDTO> getBusCatListDtoO3() {
		return busCatListDtoO3;
	}

	public void setBusCatListDtoO3(List<TimeTableDTO> busCatListDtoO3) {
		this.busCatListDtoO3 = busCatListDtoO3;
	}

	public boolean isBusTypeDisableD1() {
		return busTypeDisableD1;
	}

	public void setBusTypeDisableD1(boolean busTypeDisableD1) {
		this.busTypeDisableD1 = busTypeDisableD1;
	}

	public List<TimeTableDTO> getBusCategoryListMain() {
		return busCategoryListMain;
	}

	public void setBusCategoryListMain(List<TimeTableDTO> busCategoryListMain) {
		this.busCategoryListMain = busCategoryListMain;
	}

	public List<TimeTableDTO> getShowInsertedDataG1O() {
		return showInsertedDataG1O;
	}

	public void setShowInsertedDataG1O(List<TimeTableDTO> showInsertedDataG1O) {
		this.showInsertedDataG1O = showInsertedDataG1O;
	}

	public List<TimeTableDTO> getShowInsertedDataG2O() {
		return showInsertedDataG2O;
	}

	public void setShowInsertedDataG2O(List<TimeTableDTO> showInsertedDataG2O) {
		this.showInsertedDataG2O = showInsertedDataG2O;
	}

	public List<TimeTableDTO> getShowInsertedDataG3O() {
		return showInsertedDataG3O;
	}

	public void setShowInsertedDataG3O(List<TimeTableDTO> showInsertedDataG3O) {
		this.showInsertedDataG3O = showInsertedDataG3O;
	}

	public List<TimeTableDTO> getShowInsertedDataG1D() {
		return showInsertedDataG1D;
	}

	public void setShowInsertedDataG1D(List<TimeTableDTO> showInsertedDataG1D) {
		this.showInsertedDataG1D = showInsertedDataG1D;
	}

	public List<TimeTableDTO> getShowInsertedDataG2D() {
		return showInsertedDataG2D;
	}

	public void setShowInsertedDataG2D(List<TimeTableDTO> showInsertedDataG2D) {
		this.showInsertedDataG2D = showInsertedDataG2D;
	}

	public List<TimeTableDTO> getShowInsertedDataG3D() {
		return showInsertedDataG3D;
	}

	public void setShowInsertedDataG3D(List<TimeTableDTO> showInsertedDataG3D) {
		this.showInsertedDataG3D = showInsertedDataG3D;
	}

	public String getSucessMsg() {
		return sucessMsg;
	}

	public void setSucessMsg(String sucessMsg) {
		this.sucessMsg = sucessMsg;
	}

	public String getOrgin() {
		return orgin;
	}

	public void setOrgin(String orgin) {
		this.orgin = orgin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public int getTripsG1O() {
		return tripsG1O;
	}

	public void setTripsG1O(int tripsG1O) {
		this.tripsG1O = tripsG1O;
	}

	public int getTripsG2O() {
		return tripsG2O;
	}

	public void setTripsG2O(int tripsG2O) {
		this.tripsG2O = tripsG2O;
	}

	public int getTripsG3O() {
		return tripsG3O;
	}

	public void setTripsG3O(int tripsG3O) {
		this.tripsG3O = tripsG3O;
	}

	public int getTripsG1D() {
		return tripsG1D;
	}

	public void setTripsG1D(int tripsG1D) {
		this.tripsG1D = tripsG1D;
	}

	public int getTripsG2D() {
		return tripsG2D;
	}

	public void setTripsG2D(int tripsG2D) {
		this.tripsG2D = tripsG2D;
	}

	public int getTripsG3D() {
		return tripsG3D;
	}

	public void setTripsG3D(int tripsG3D) {
		this.tripsG3D = tripsG3D;
	}

	public TimeTableDTO getNonPrivateBusDTO() {
		return nonPrivateBusDTO;
	}

	public void setNonPrivateBusDTO(TimeTableDTO nonPrivateBusDTO) {
		this.nonPrivateBusDTO = nonPrivateBusDTO;
	}

	public boolean isSaveBtnDisable() {
		return saveBtnDisable;
	}

	public void setSaveBtnDisable(boolean saveBtnDisable) {
		this.saveBtnDisable = saveBtnDisable;
	}

	public TimeTableDTO getNonSltbEtcBusDTO() {
		return nonSltbEtcBusDTO;
	}

	public void setNonSltbEtcBusDTO(TimeTableDTO nonSltbEtcBusDTO) {
		this.nonSltbEtcBusDTO = nonSltbEtcBusDTO;
	}

}
