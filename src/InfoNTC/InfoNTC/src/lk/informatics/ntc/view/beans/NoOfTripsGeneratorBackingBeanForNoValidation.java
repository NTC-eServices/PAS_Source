package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.TabChangeEvent;

import lk.informatics.ntc.model.dto.TimeTableDTO;
import lk.informatics.ntc.model.dto.VehicleInspectionDTO;
import lk.informatics.ntc.model.service.RouteScheduleService;
import lk.informatics.ntc.model.service.TimeTableService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "noOfTripsGeneratorBackingBeanForNoValidation")
@ViewScoped
public class NoOfTripsGeneratorBackingBeanForNoValidation {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	private List<TimeTableDTO> busRouteList = new ArrayList<>(0);
	private List<TimeTableDTO> busCategoryList = new ArrayList<>(0);
	private List<TimeTableDTO> routeDetailsList, originGroupOneList, originGroupTwoList, originGroupThreeList,
			destinationGroupOneList, destinationGroupTwoList, destinationGroupThreeList;
	private List<String> timeList;
	private TimeTableService timeTableService;
	private int activeTabIndex;
	private TimeTableDTO timeTableDTO, groupOneDTO, groupTwoDTO, groupThreeDTO, tripsDTO, routeDTO;
	private String alertMSG, successMessage, errorMessage, restTimeString, referenceNo, savedReferenceNo;
	private int groupCount, pvtBusCountOrigin, pvtBusCountDestination, originOneBuses, destinationOneBuses,
			originTwoBuses, destinationTwoBuses, originThreeBuses, destinationThreeBuses;
	private int restTime;
	private boolean disabledGroupTwo, disabledGroupThree, editMode, disabledWithFixedTime, disabledWithOutFixedTime,
			disabledWithFixedBuses, disableBusCategory;
	private RouteScheduleService routeScheduleService;
	private List<VehicleInspectionDTO> originPVTBusesList;
	private List<VehicleInspectionDTO> destinatinoPVTBusesList;

	@PostConstruct
	public void init() {
		timeTableService = (TimeTableService) SpringApplicationContex.getBean("timeTableService");
		routeScheduleService = (RouteScheduleService) SpringApplicationContex.getBean("routeScheduleService");
		loadValue();
	}

	public void loadValue() {
		routeDetailsList = new ArrayList<>();
		timeTableDTO = new TimeTableDTO();
		groupOneDTO = new TimeTableDTO();
		groupTwoDTO = new TimeTableDTO();
		groupThreeDTO = new TimeTableDTO();
		tripsDTO = new TimeTableDTO();
		routeDTO = new TimeTableDTO();
		disabledWithFixedTime = true;
		disabledWithOutFixedTime = true;
		disableBusCategory = true;
		disabledWithFixedBuses = true;
		busRouteList = timeTableService.getRouteNoList();
		originPVTBusesList = new ArrayList<VehicleInspectionDTO>();
		destinatinoPVTBusesList = new ArrayList<VehicleInspectionDTO>();
	}

	public void buttonHandler() {

		TimeTableDTO dto = timeTableService.getPanelStageStatus(timeTableDTO.getGenereatedRefNo(),
				timeTableDTO.getRouteNo());

		if (dto.getWithFixedTimeCode() == null && dto.getWithOutFixedTimeCode() == null
				&& dto.getWithFixedBusesCode() == null) {

			disabledWithFixedTime = false;

		} else if (dto.getWithFixedTimeCode() != null && dto.getWithOutFixedTimeCode() == null
				&& dto.getWithFixedBusesCode() == null) {

			disabledWithOutFixedTime = false;
		} else if (dto.getWithFixedTimeCode() != null && dto.getWithOutFixedTimeCode() != null
				&& dto.getWithFixedBusesCode() == null) {

			disabledWithFixedBuses = false;
		} else {
			disabledWithFixedTime = true;
			disabledWithOutFixedTime = true;
			disabledWithFixedBuses = true;
		}

	}

	public void ajaxFillBusCategory() {

		busCategoryList = timeTableService.getBusCategoryList(timeTableDTO.getRouteNo());
		disableBusCategory = false;

	}

	public void ajaxFillRouteDetails() {

		routeDTO = timeTableService.getRouteData(timeTableDTO.getRouteNo(), timeTableDTO.getBusCategory());

		if (routeDTO.getOrigin() != null && !routeDTO.getOrigin().trim().equalsIgnoreCase("")) {
			timeTableDTO.setOrigin(routeDTO.getOrigin());
		}

		if (routeDTO.getDestination() != null && !routeDTO.getDestination().trim().equalsIgnoreCase("")) {
			timeTableDTO.setDestination(routeDTO.getDestination());
		}

		if (routeDTO.getGenereatedRefNo() != null && !routeDTO.getGenereatedRefNo().trim().equalsIgnoreCase("")) {
			timeTableDTO.setGenereatedRefNo(routeDTO.getGenereatedRefNo());
			timeTableDTO.setTripRefNo(routeDTO.getGenereatedRefNo());
		}

		buttonHandler();
	}

	public void searchAction() {

		if (timeTableDTO.getRouteNo() != null && !timeTableDTO.getRouteNo().trim().equalsIgnoreCase("")) {

			if (timeTableDTO.getBusCategory() != null && !timeTableDTO.getBusCategory().trim().equalsIgnoreCase("")) {

				if (timeTableService.checkRelatedDataInTripGeneratorTable(timeTableDTO.getRouteNo(),
						timeTableDTO.getGenereatedRefNo()) == false) {

					groupCount = timeTableService.getGroupCount(timeTableDTO.getRouteNo(),
							timeTableDTO.getGenereatedRefNo());

					boolean notAssigned = checkBusesAssigned(groupCount);
					if (!notAssigned) {
						sessionBackingBean.setMessage("Searched data is not eligible. Buses are already assigned");
						RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
						return;
					}

					pvtBusCountOrigin = timeTableService.getPVTbusCount(timeTableDTO.getRouteNo(), false,
							timeTableDTO.getBusCategory(),timeTableDTO.getGroupNo());
					pvtBusCountDestination = timeTableService.getPVTbusCount(timeTableDTO.getRouteNo(), true,
							timeTableDTO.getBusCategory(), timeTableDTO.getGroupNo());
					/** timeTableDTO.getBusCategory() added by tharushi.e **/
					originPVTBusesList = new ArrayList<VehicleInspectionDTO>();
					originPVTBusesList = timeTableService.getPVTbuses(timeTableDTO.getRouteNo(), "N");// origin
																										// PVT
																										// buses
					destinatinoPVTBusesList = new ArrayList<VehicleInspectionDTO>();
					destinatinoPVTBusesList = timeTableService.getPVTbuses(timeTableDTO.getRouteNo(), "Y");// destination
																											// PVT
																											// buses

					//if (pvtBusCountOrigin > 0) {

						groupOneDTO.setNoOfPVTbuessOriginOne(pvtBusCountOrigin);
						groupTwoDTO.setNoOfPVTbuessOriginTwo(pvtBusCountOrigin);
						groupThreeDTO.setNoOfPVTbuessOriginThree(pvtBusCountOrigin);

						int PVT_O01 = groupOneDTO.getNoOfPVTbuessOriginOne();
						groupOneDTO.setTotalBusesOriginOne(PVT_O01);

						int PVT_O02 = groupTwoDTO.getNoOfPVTbuessOriginTwo();
						groupTwoDTO.setTotalBusesOriginTwo(PVT_O02);

						int PVT_O03 = groupThreeDTO.getNoOfPVTbuessOriginThree();
						groupThreeDTO.setTotalBusesOriginThree(PVT_O03);

					//	if (pvtBusCountDestination > 0) {

							groupOneDTO.setNoOfPVTbuessDestinationOne(pvtBusCountDestination);
							groupTwoDTO.setNoOfPVTbuessDestinationTwo(pvtBusCountDestination);
							groupThreeDTO.setNoOfPVTbuessDestinationThree(pvtBusCountDestination);

							int PVT_D01 = groupOneDTO.getNoOfPVTbuessDestinationOne();
							groupOneDTO.setTotalBusesDestinationOne(PVT_D01);

							int PVT_D02 = groupTwoDTO.getNoOfPVTbuessDestinationTwo();
							groupTwoDTO.setTotalBusesDestinationTwo(PVT_D02);

							int PVT_D03 = groupThreeDTO.getNoOfPVTbuessDestinationThree();
							groupThreeDTO.setTotalBusesDestinationThree(PVT_D03);

							restTimeString = timeTableService.getRestTime(timeTableDTO.getRouteNo());

							if (restTimeString != null) {
								String[] parts = restTimeString.split(":");

								int hours = Integer.parseInt(parts[0]);
								int minute = Integer.parseInt(parts[1]);

								restTime = minute + (hours * 60);

								groupOneDTO.setRestTimeOriginOne(restTime);
								groupOneDTO.setRestTimeDestinationOne(restTime);

								groupTwoDTO.setRestTimeOriginTwo(restTime);
								groupTwoDTO.setRestTimeDestinationTwo(restTime);

								groupThreeDTO.setRestTimeOriginThree(restTime);
								groupThreeDTO.setRestTimeDestinationThree(restTime);

							}

							groupManagerForSave(groupCount);
							editMode = false;

//						} else {
//
//							setErrorMessage("No. of PVT bus can not be zero for destination");
//							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
//						}
				/*	} else {
						setErrorMessage("No. of PVT bus can not be zero for origin");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}*/

				} else {

					groupCount = timeTableService.getGroupCountForEdit(timeTableDTO.getRouteNo(),
							timeTableDTO.getGenereatedRefNo());

					if (groupCount > 0) {

						boolean notAssigned = checkBusesAssigned(groupCount);
						if (!notAssigned) {
							sessionBackingBean.setMessage("Searched data is not eligible. Buses are already assigned");
							RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
							return;
						}

						originPVTBusesList = new ArrayList<VehicleInspectionDTO>();
						originPVTBusesList = timeTableService.getPVTbuses(timeTableDTO.getRouteNo(), "N");// origin
																											// PVT
																											// buses
						destinatinoPVTBusesList = new ArrayList<VehicleInspectionDTO>();
						destinatinoPVTBusesList = timeTableService.getPVTbuses(timeTableDTO.getRouteNo(), "Y");// destination
																												// PVT
																												// buses

						groupManagerForEdit(groupCount);
						savedReferenceNo = timeTableService.getTripReferenceNo(timeTableDTO.getGenereatedRefNo());
						editMode = true;

					} else {
						setErrorMessage("Can not find group count for selected route no.");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}

				}

			} else {
				setErrorMessage("Please Select Bus Category");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {
			setErrorMessage("Please Select Route No.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

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

	public void groupManagerForEdit(int groupCount) {

		if (groupCount == 1) {

			disabledGroupTwo = true;
			disabledGroupThree = true;

			originGroupOneList = new ArrayList<>();
			destinationGroupOneList = new ArrayList<>();

			originGroupOneList = timeTableService.getSavedTripsTimeDetails(timeTableDTO.getRouteNo(),
					timeTableDTO.getGenereatedRefNo(), "O", "1");
			destinationGroupOneList = timeTableService.getSavedTripsTimeDetails(timeTableDTO.getRouteNo(),
					timeTableDTO.getGenereatedRefNo(), "D", "1");

			managesavedRouteAndBusInfo();

		} else if (groupCount == 2) {

			disabledGroupTwo = false;
			disabledGroupThree = true;

			originGroupOneList = new ArrayList<>();
			destinationGroupOneList = new ArrayList<>();

			originGroupTwoList = new ArrayList<>();
			destinationGroupTwoList = new ArrayList<>();

			originGroupOneList = timeTableService.getSavedTripsTimeDetails(timeTableDTO.getRouteNo(),
					timeTableDTO.getGenereatedRefNo(), "O", "1");
			destinationGroupOneList = timeTableService.getSavedTripsTimeDetails(timeTableDTO.getRouteNo(),
					timeTableDTO.getGenereatedRefNo(), "D", "1");

			originGroupTwoList = timeTableService.getSavedTripsTimeDetails(timeTableDTO.getRouteNo(),
					timeTableDTO.getGenereatedRefNo(), "O", "2");
			destinationGroupTwoList = timeTableService.getSavedTripsTimeDetails(timeTableDTO.getRouteNo(),
					timeTableDTO.getGenereatedRefNo(), "D", "2");

			managesavedRouteAndBusInfo();

		} else if (groupCount == 3) {

			disabledGroupTwo = false;
			disabledGroupThree = false;

			originGroupOneList = new ArrayList<>();
			destinationGroupOneList = new ArrayList<>();

			originGroupTwoList = new ArrayList<>();
			destinationGroupTwoList = new ArrayList<>();

			originGroupThreeList = new ArrayList<>();
			destinationGroupThreeList = new ArrayList<>();

			originGroupOneList = timeTableService.getSavedTripsTimeDetails(timeTableDTO.getRouteNo(),
					timeTableDTO.getGenereatedRefNo(), "O", "1");
			destinationGroupOneList = timeTableService.getSavedTripsTimeDetails(timeTableDTO.getRouteNo(),
					timeTableDTO.getGenereatedRefNo(), "D", "1");

			originGroupTwoList = timeTableService.getSavedTripsTimeDetails(timeTableDTO.getRouteNo(),
					timeTableDTO.getGenereatedRefNo(), "O", "2");
			destinationGroupTwoList = timeTableService.getSavedTripsTimeDetails(timeTableDTO.getRouteNo(),
					timeTableDTO.getGenereatedRefNo(), "D", "2");

			originGroupThreeList = timeTableService.getSavedTripsTimeDetails(timeTableDTO.getRouteNo(),
					timeTableDTO.getGenereatedRefNo(), "O", "3");
			destinationGroupThreeList = timeTableService.getSavedTripsTimeDetails(timeTableDTO.getRouteNo(),
					timeTableDTO.getGenereatedRefNo(), "D", "3");

			managesavedRouteAndBusInfo();

		} else {

		}
	}

	public void managesavedRouteAndBusInfo() {

		if (groupCount == 1) {

			TimeTableDTO dto_O1 = timeTableService.getSavedTripsRouteAndBusDetails(timeTableDTO.getRouteNo(),
					timeTableDTO.getGenereatedRefNo(), "O", "1");

			tripsDTO.setTotalTripsOriginOne(dto_O1.getTotalTrips());
			groupOneDTO.setNoOfPVTbuessOriginOne(dto_O1.getNoOfPVTbueses());
			groupOneDTO.setNoOfCTBbuessOriginOne(dto_O1.getNoOfCTBbueses());
			groupOneDTO.setNoOfOtherbuessOriginOne(dto_O1.getNoOfOtherbuses());
			groupOneDTO.setRestTimeOriginOne(dto_O1.getRestTimeInt());
			groupOneDTO.setTotalBusesOriginOne(dto_O1.getTotalBuses());

			TimeTableDTO dto_D1 = timeTableService.getSavedTripsRouteAndBusDetails(timeTableDTO.getRouteNo(),
					timeTableDTO.getGenereatedRefNo(), "D", "1");

			tripsDTO.setTotalTripsDestinationOne(dto_D1.getTotalTrips());
			groupOneDTO.setNoOfPVTbuessDestinationOne(dto_D1.getNoOfPVTbueses());
			groupOneDTO.setNoOfCTBbuessDestinationOne(dto_D1.getNoOfCTBbueses());
			groupOneDTO.setNoOfOtherbuessDestinationOne(dto_D1.getNoOfOtherbuses());
			groupOneDTO.setRestTimeDestinationOne(dto_D1.getRestTimeInt());
			groupOneDTO.setTotalBusesDestinationOne(dto_D1.getTotalBuses());

		} else if (groupCount == 2) {

			TimeTableDTO dto_O1 = timeTableService.getSavedTripsRouteAndBusDetails(timeTableDTO.getRouteNo(),
					timeTableDTO.getGenereatedRefNo(), "O", "1");

			tripsDTO.setTotalTripsOriginOne(dto_O1.getTotalTrips());
			groupOneDTO.setNoOfPVTbuessOriginOne(dto_O1.getNoOfPVTbueses());
			groupOneDTO.setNoOfCTBbuessOriginOne(dto_O1.getNoOfCTBbueses());
			groupOneDTO.setNoOfOtherbuessOriginOne(dto_O1.getNoOfOtherbuses());
			groupOneDTO.setRestTimeOriginOne(dto_O1.getRestTimeInt());
			groupOneDTO.setTotalBusesOriginOne(dto_O1.getTotalBuses());

			TimeTableDTO dto_D1 = timeTableService.getSavedTripsRouteAndBusDetails(timeTableDTO.getRouteNo(),
					timeTableDTO.getGenereatedRefNo(), "D", "1");

			tripsDTO.setTotalTripsDestinationOne(dto_D1.getTotalTrips());
			groupOneDTO.setNoOfPVTbuessDestinationOne(dto_D1.getNoOfPVTbueses());
			groupOneDTO.setNoOfCTBbuessDestinationOne(dto_D1.getNoOfCTBbueses());
			groupOneDTO.setNoOfOtherbuessDestinationOne(dto_D1.getNoOfOtherbuses());
			groupOneDTO.setRestTimeDestinationOne(dto_D1.getRestTimeInt());
			groupOneDTO.setTotalBusesDestinationOne(dto_D1.getTotalBuses());

			TimeTableDTO dto_O2 = timeTableService.getSavedTripsRouteAndBusDetails(timeTableDTO.getRouteNo(),
					timeTableDTO.getGenereatedRefNo(), "O", "2");

			tripsDTO.setTotalTripsOriginTwo(dto_O2.getTotalTrips());
			groupTwoDTO.setNoOfPVTbuessOriginTwo(dto_O2.getNoOfPVTbueses());
			groupTwoDTO.setNoOfCTBbuessOriginTwo(dto_O2.getNoOfCTBbueses());
			groupTwoDTO.setNoOfOtherbuessOriginTwo(dto_O2.getNoOfOtherbuses());
			groupTwoDTO.setRestTimeOriginTwo(dto_O2.getRestTimeInt());
			groupTwoDTO.setTotalBusesOriginTwo(dto_O2.getTotalBuses());

			TimeTableDTO dto_D2 = timeTableService.getSavedTripsRouteAndBusDetails(timeTableDTO.getRouteNo(),
					timeTableDTO.getGenereatedRefNo(), "D", "2");

			tripsDTO.setTotalTripsDestinationTwo(dto_D2.getTotalTrips());
			groupTwoDTO.setNoOfPVTbuessDestinationTwo(dto_D2.getNoOfPVTbueses());
			groupTwoDTO.setNoOfCTBbuessDestinationTwo(dto_D2.getNoOfCTBbueses());
			groupTwoDTO.setNoOfOtherbuessDestinationTwo(dto_D2.getNoOfOtherbuses());
			groupTwoDTO.setRestTimeDestinationTwo(dto_D2.getRestTimeInt());
			groupTwoDTO.setTotalBusesDestinationTwo(dto_D2.getTotalBuses());

		} else if (groupCount == 3) {

			TimeTableDTO dto_O1 = timeTableService.getSavedTripsRouteAndBusDetails(timeTableDTO.getRouteNo(),
					timeTableDTO.getGenereatedRefNo(), "O", "1");

			tripsDTO.setTotalTripsOriginOne(dto_O1.getTotalTrips());
			groupOneDTO.setNoOfPVTbuessOriginOne(dto_O1.getNoOfPVTbueses());
			groupOneDTO.setNoOfCTBbuessOriginOne(dto_O1.getNoOfCTBbueses());
			groupOneDTO.setNoOfOtherbuessOriginOne(dto_O1.getNoOfOtherbuses());
			groupOneDTO.setRestTimeOriginOne(dto_O1.getRestTimeInt());
			groupOneDTO.setTotalBusesOriginOne(dto_O1.getTotalBuses());

			TimeTableDTO dto_D1 = timeTableService.getSavedTripsRouteAndBusDetails(timeTableDTO.getRouteNo(),
					timeTableDTO.getGenereatedRefNo(), "D", "1");

			tripsDTO.setTotalTripsDestinationOne(dto_D1.getTotalTrips());
			groupOneDTO.setNoOfPVTbuessDestinationOne(dto_D1.getNoOfPVTbueses());
			groupOneDTO.setNoOfCTBbuessDestinationOne(dto_D1.getNoOfCTBbueses());
			groupOneDTO.setNoOfOtherbuessDestinationOne(dto_D1.getNoOfOtherbuses());
			groupOneDTO.setRestTimeDestinationOne(dto_D1.getRestTimeInt());
			groupOneDTO.setTotalBusesDestinationOne(dto_D1.getTotalBuses());

			TimeTableDTO dto_O2 = timeTableService.getSavedTripsRouteAndBusDetails(timeTableDTO.getRouteNo(),
					timeTableDTO.getGenereatedRefNo(), "O", "2");

			tripsDTO.setTotalTripsOriginTwo(dto_O2.getTotalTrips());
			groupTwoDTO.setNoOfPVTbuessOriginTwo(dto_O2.getNoOfPVTbueses());
			groupTwoDTO.setNoOfCTBbuessOriginTwo(dto_O2.getNoOfCTBbueses());
			groupTwoDTO.setNoOfOtherbuessOriginTwo(dto_O2.getNoOfOtherbuses());
			groupTwoDTO.setRestTimeOriginTwo(dto_O2.getRestTimeInt());
			groupTwoDTO.setTotalBusesOriginTwo(dto_O2.getTotalBuses());

			TimeTableDTO dto_D2 = timeTableService.getSavedTripsRouteAndBusDetails(timeTableDTO.getRouteNo(),
					timeTableDTO.getGenereatedRefNo(), "D", "2");

			tripsDTO.setTotalTripsDestinationTwo(dto_D2.getTotalTrips());
			groupTwoDTO.setNoOfPVTbuessDestinationTwo(dto_D2.getNoOfPVTbueses());
			groupTwoDTO.setNoOfCTBbuessDestinationTwo(dto_D2.getNoOfCTBbueses());
			groupTwoDTO.setNoOfOtherbuessDestinationTwo(dto_D2.getNoOfOtherbuses());
			groupTwoDTO.setRestTimeDestinationTwo(dto_D2.getRestTimeInt());
			groupTwoDTO.setTotalBusesDestinationTwo(dto_D2.getTotalBuses());

			TimeTableDTO dto_O3 = timeTableService.getSavedTripsRouteAndBusDetails(timeTableDTO.getRouteNo(),
					timeTableDTO.getGenereatedRefNo(), "O", "3");

			tripsDTO.setTotalTripsOriginThree(dto_O3.getTotalTrips());
			groupThreeDTO.setNoOfPVTbuessOriginThree(dto_O3.getNoOfPVTbueses());
			groupThreeDTO.setNoOfCTBbuessOriginThree(dto_O3.getNoOfCTBbueses());
			groupThreeDTO.setNoOfOtherbuessOriginThree(dto_O3.getNoOfOtherbuses());
			groupThreeDTO.setRestTimeOriginThree(dto_O3.getRestTimeInt());
			groupThreeDTO.setTotalBusesOriginThree(dto_O3.getTotalBuses());

			TimeTableDTO dto_D3 = timeTableService.getSavedTripsRouteAndBusDetails(timeTableDTO.getRouteNo(),
					timeTableDTO.getGenereatedRefNo(), "D", "3");

			tripsDTO.setTotalTripsDestinationThree(dto_D3.getTotalTrips());
			groupThreeDTO.setNoOfPVTbuessDestinationThree(dto_D3.getNoOfPVTbueses());
			groupThreeDTO.setNoOfCTBbuessDestinationThree(dto_D3.getNoOfCTBbueses());
			groupThreeDTO.setNoOfOtherbuessDestinationThree(dto_D3.getNoOfOtherbuses());
			groupThreeDTO.setRestTimeDestinationThree(dto_D3.getRestTimeInt());
			groupThreeDTO.setTotalBusesDestinationThree(dto_D3.getTotalBuses());
		}

	}

	public int groupManagerForSave(int groupCount) {

		routeDetailsList = timeTableService.getNewRouteDetails(timeTableDTO.getRouteNo(),
				timeTableDTO.getGenereatedRefNo());

		if (groupCount == 1) {

			disabledGroupTwo = true;
			disabledGroupThree = true;

			originGroupOneList = new ArrayList<>();
			destinationGroupOneList = new ArrayList<>();

			long beginOrigin = calculateTotalMinutesForStart(routeDetailsList.get(0).getOriginStartTimeString());
			long endOrigin = calculateTotalMinutesForEnd(routeDetailsList.get(0).getOriginEndTimeString());
			long intervalOrigin = calculateTotalInterval(routeDetailsList.get(0).getOriginDivideRangeString());

			long beginDestination = calculateTotalMinutesForStart(
					routeDetailsList.get(0).getDestinationStartTimeString());
			long endDestination = calculateTotalMinutesForEnd(routeDetailsList.get(0).getDestinationEndTimeString());
			long intervalDestination = calculateTotalInterval(
					routeDetailsList.get(0).getDestinationDivideRangeString());

			originGroupOneList = timeRangeManager(beginOrigin, endOrigin, intervalOrigin);
			destinationGroupOneList = timeRangeManager(beginDestination, endDestination, intervalDestination);

		} else if (groupCount == 2) {

			disabledGroupTwo = false;
			disabledGroupThree = true;

			originGroupOneList = new ArrayList<>();
			destinationGroupOneList = new ArrayList<>();

			long beginOrigin = calculateTotalMinutesForStart(routeDetailsList.get(0).getOriginStartTimeString());
			long endOrigin = calculateTotalMinutesForEnd(routeDetailsList.get(0).getOriginEndTimeString());
			long intervalOrigin = calculateTotalInterval(routeDetailsList.get(0).getOriginDivideRangeString());

			long beginDestination = calculateTotalMinutesForStart(
					routeDetailsList.get(0).getDestinationStartTimeString());
			long endDestination = calculateTotalMinutesForEnd(routeDetailsList.get(0).getDestinationEndTimeString());
			long intervalDestination = calculateTotalInterval(
					routeDetailsList.get(0).getDestinationDivideRangeString());

			originGroupOneList = timeRangeManager(beginOrigin, endOrigin, intervalOrigin);
			destinationGroupOneList = timeRangeManager(beginDestination, endDestination, intervalDestination);

			originGroupTwoList = new ArrayList<>();
			destinationGroupTwoList = new ArrayList<>();

			long beginOrigin02 = calculateTotalMinutesForStart(routeDetailsList.get(1).getOriginStartTimeString());
			long endOrigin02 = calculateTotalMinutesForEnd(routeDetailsList.get(1).getOriginEndTimeString());
			long intervalOrigin02 = calculateTotalInterval(routeDetailsList.get(1).getOriginDivideRangeString());

			long beginDestination02 = calculateTotalMinutesForStart(
					routeDetailsList.get(1).getDestinationStartTimeString());
			long endDestination02 = calculateTotalMinutesForEnd(routeDetailsList.get(1).getDestinationEndTimeString());
			long intervalDestination02 = calculateTotalInterval(
					routeDetailsList.get(1).getDestinationDivideRangeString());

			originGroupTwoList = timeRangeManager(beginOrigin02, endOrigin02, intervalOrigin02);
			destinationGroupTwoList = timeRangeManager(beginDestination02, endDestination02, intervalDestination02);

		} else if (groupCount == 3) {

			disabledGroupTwo = false;
			disabledGroupThree = false;

			originGroupOneList = new ArrayList<>();
			destinationGroupOneList = new ArrayList<>();

			long beginOrigin = calculateTotalMinutesForStart(routeDetailsList.get(0).getOriginStartTimeString());
			long endOrigin = calculateTotalMinutesForEnd(routeDetailsList.get(0).getOriginEndTimeString());
			long intervalOrigin = calculateTotalInterval(routeDetailsList.get(0).getOriginDivideRangeString());

			long beginDestination = calculateTotalMinutesForStart(
					routeDetailsList.get(0).getDestinationStartTimeString());
			long endDestination = calculateTotalMinutesForEnd(routeDetailsList.get(0).getDestinationEndTimeString());
			long intervalDestination = calculateTotalInterval(
					routeDetailsList.get(0).getDestinationDivideRangeString());

			originGroupOneList = timeRangeManager(beginOrigin, endOrigin, intervalOrigin);
			destinationGroupOneList = timeRangeManager(beginDestination, endDestination, intervalDestination);

			originGroupTwoList = new ArrayList<>();
			destinationGroupTwoList = new ArrayList<>();

			long beginOrigin02 = calculateTotalMinutesForStart(routeDetailsList.get(1).getOriginStartTimeString());
			long endOrigin02 = calculateTotalMinutesForEnd(routeDetailsList.get(1).getOriginEndTimeString());
			long intervalOrigin02 = calculateTotalInterval(routeDetailsList.get(1).getOriginDivideRangeString());

			long beginDestination02 = calculateTotalMinutesForStart(
					routeDetailsList.get(1).getDestinationStartTimeString());
			long endDestination02 = calculateTotalMinutesForEnd(routeDetailsList.get(1).getDestinationEndTimeString());
			long intervalDestination02 = calculateTotalInterval(
					routeDetailsList.get(1).getDestinationDivideRangeString());

			originGroupTwoList = timeRangeManager(beginOrigin02, endOrigin02, intervalOrigin02);
			destinationGroupTwoList = timeRangeManager(beginDestination02, endDestination02, intervalDestination02);

			originGroupThreeList = new ArrayList<>();
			destinationGroupThreeList = new ArrayList<>();

			long beginOrigin03 = calculateTotalMinutesForStart(routeDetailsList.get(2).getOriginStartTimeString());
			long endOrigin03 = calculateTotalMinutesForEnd(routeDetailsList.get(2).getOriginEndTimeString());
			long intervalOrigin03 = calculateTotalInterval(routeDetailsList.get(2).getOriginDivideRangeString());

			long beginDestination03 = calculateTotalMinutesForStart(
					routeDetailsList.get(2).getDestinationStartTimeString());
			long endDestination03 = calculateTotalMinutesForEnd(routeDetailsList.get(2).getDestinationEndTimeString());
			long intervalDestination03 = calculateTotalInterval(
					routeDetailsList.get(2).getDestinationDivideRangeString());

			originGroupThreeList = timeRangeManager(beginOrigin03, endOrigin03, intervalOrigin03);
			destinationGroupThreeList = timeRangeManager(beginDestination03, endDestination03, intervalDestination03);

		} else {
			setErrorMessage("Group Count Range Should be One To Three");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

		return 0;

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

	/* Have to edit for 00:45 time ranges */
	public List<String> intervalCalculator(long begin, long end, long interval) {

		timeList = new ArrayList<>();
		String final_time = null;

		for (long time = begin; time <= end; time += interval) {

			final_time = String.format("%02d:%02d", time / 60, time % 60);

			timeList.add(final_time);

			/*
			 * If time divide range not enough for same divide as pervious one
			 */
			if (time < end && end - time < interval) {

				String[] parts = final_time.split(":");

				long totalHours = Long.parseLong(parts[0]);
				long totalMinutes = Long.parseLong(parts[1]) + (end - time) % 60;

				if (totalMinutes >= 60) {
					totalHours = totalHours + 1;
					totalMinutes = totalMinutes % 60;
				}

				final_time = String.format("%02d:%02d", totalHours, totalMinutes);

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

		String id = ((TimeTableDTO) event.getObject()).getId();
		String firstValue = ((TimeTableDTO) event.getObject()).getFirstValue();
		String secondValue = ((TimeTableDTO) event.getObject()).getSecondValue();
		int newValueFrequency = ((TimeTableDTO) event.getObject()).getFrequency();

		if (newValueFrequency > 0) {

			long noOfBuses = calculateTotalMinute(firstValue, secondValue) / newValueFrequency;

			if (validate(newValueFrequency, 1, 1)) {

				for (int i = 0; i < this.originGroupOneList.size(); i++) {

					if (originGroupOneList.get(i).getId().equals(id)) {

						TimeTableDTO element = new TimeTableDTO(id, firstValue, secondValue, (int) noOfBuses,
								newValueFrequency);
						originGroupOneList.set(i, element);
					}

				}

				int buses = 0;
				for (int i = 0; i < this.originGroupOneList.size(); i++) {
					buses = this.originGroupOneList.get(i).getNoOfBuses() + buses;
				}

				tripsDTO.setTotalTripsOriginOne(buses);

			} else {

				for (int i = 0; i < this.originGroupOneList.size(); i++) {

					if (originGroupOneList.get(i).getId().equals(id)) {
						TimeTableDTO element = new TimeTableDTO(id, firstValue, secondValue, 0, 0);
						originGroupOneList.set(i, element);
					}

				}

				setErrorMessage("Enterd Frequency is not in the time range");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {
			setErrorMessage("Frequency Can Not Less Than Zero");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	/* Update Group One Destination Buses Details */
	public void ajaxOnRowEditDestinationOne(RowEditEvent event) {

		String id = ((TimeTableDTO) event.getObject()).getId();
		String firstValue = ((TimeTableDTO) event.getObject()).getFirstValue();
		String secondValue = ((TimeTableDTO) event.getObject()).getSecondValue();
		int newValueFrequency = ((TimeTableDTO) event.getObject()).getFrequency();

		if (newValueFrequency > 0) {

			long noOfBuses = calculateTotalMinute(firstValue, secondValue) / newValueFrequency;

			if (validate(newValueFrequency, 2, 1)) {

				for (int i = 0; i < this.destinationGroupOneList.size(); i++) {

					if (destinationGroupOneList.get(i).getId().equals(id)) {

						TimeTableDTO element = new TimeTableDTO(id, firstValue, secondValue, (int) noOfBuses,
								newValueFrequency);

						destinationGroupOneList.set(i, element);

					}

				}

				int buses = 0;
				for (int i = 0; i < this.destinationGroupOneList.size(); i++) {
					buses = this.destinationGroupOneList.get(i).getNoOfBuses() + buses;
				}

				tripsDTO.setTotalTripsDestinationOne(buses);

			} else {

				for (int i = 0; i < this.destinationGroupOneList.size(); i++) {

					if (destinationGroupOneList.get(i).getId().equals(id)) {
						TimeTableDTO element = new TimeTableDTO(id, firstValue, secondValue, 0, 0);
						destinationGroupOneList.set(i, element);
					}

				}

				setErrorMessage("Enterd Frequency is not in the time range");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Frequency Can Not Less Than Zero");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	/* Update Group Two Origin Buses Details */
	public void ajaxOnRowEditOriginTwo(RowEditEvent event) {

		String id = ((TimeTableDTO) event.getObject()).getId();
		String firstValue = ((TimeTableDTO) event.getObject()).getFirstValue();
		String secondValue = ((TimeTableDTO) event.getObject()).getSecondValue();
		int newValueFrequency = ((TimeTableDTO) event.getObject()).getFrequency();

		if (newValueFrequency > 0) {

			long noOfBuses = calculateTotalMinute(firstValue, secondValue) / newValueFrequency;

			if (validate(newValueFrequency, 1, 2)) {

				for (int i = 0; i < this.originGroupTwoList.size(); i++) {

					if (originGroupTwoList.get(i).getId().equals(id)) {

						TimeTableDTO element = new TimeTableDTO(id, firstValue, secondValue, (int) noOfBuses,
								newValueFrequency);

						originGroupTwoList.set(i, element);
					}

				}

				int buses = 0;
				for (int i = 0; i < this.originGroupTwoList.size(); i++) {
					buses = this.originGroupTwoList.get(i).getNoOfBuses() + buses;
				}

				tripsDTO.setTotalTripsOriginTwo(buses);

			} else {

				for (int i = 0; i < this.originGroupTwoList.size(); i++) {

					if (originGroupTwoList.get(i).getId().equals(id)) {
						TimeTableDTO element = new TimeTableDTO(id, firstValue, secondValue, 0, 0);
						originGroupTwoList.set(i, element);
					}

				}

				setErrorMessage("Enterd Frequency is not in the time range");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {
			setErrorMessage("Frequency Can Not Less Than Zero");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	/* Update Group Two Destination Buses Details */
	public void ajaxOnRowEditDestinationTwo(RowEditEvent event) {

		String id = ((TimeTableDTO) event.getObject()).getId();
		String firstValue = ((TimeTableDTO) event.getObject()).getFirstValue();
		String secondValue = ((TimeTableDTO) event.getObject()).getSecondValue();
		int newValueFrequency = ((TimeTableDTO) event.getObject()).getFrequency();

		if (newValueFrequency > 0) {
			long noOfBuses = calculateTotalMinute(firstValue, secondValue) / newValueFrequency;

			if (validate(newValueFrequency, 2, 2)) {

				for (int i = 0; i < this.destinationGroupTwoList.size(); i++) {

					if (destinationGroupTwoList.get(i).getId().equals(id)) {

						TimeTableDTO element = new TimeTableDTO(id, firstValue, secondValue, (int) noOfBuses,
								newValueFrequency);

						destinationGroupTwoList.set(i, element);
					}

				}

				int buses = 0;
				for (int i = 0; i < this.destinationGroupTwoList.size(); i++) {
					buses = this.destinationGroupTwoList.get(i).getNoOfBuses() + buses;
				}

				tripsDTO.setTotalTripsDestinationTwo(buses);

			} else {

				for (int i = 0; i < this.destinationGroupTwoList.size(); i++) {

					if (destinationGroupTwoList.get(i).getId().equals(id)) {
						TimeTableDTO element = new TimeTableDTO(id, firstValue, secondValue, 0, 0);
						destinationGroupTwoList.set(i, element);
					}

				}

				setErrorMessage("Enterd Frequency is not in the time range");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Frequency Can Not Less Than Zero");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void ajaxOnRowEditOriginThree(RowEditEvent event) {

		String id = ((TimeTableDTO) event.getObject()).getId();
		String firstValue = ((TimeTableDTO) event.getObject()).getFirstValue();
		String secondValue = ((TimeTableDTO) event.getObject()).getSecondValue();
		int newValueFrequency = ((TimeTableDTO) event.getObject()).getFrequency();

		if (newValueFrequency > 0) {

			long noOfBuses = calculateTotalMinute(firstValue, secondValue) / newValueFrequency;

			if (validate(newValueFrequency, 1, 2)) {

				for (int i = 0; i < this.originGroupThreeList.size(); i++) {

					if (originGroupThreeList.get(i).getId().equals(id)) {

						TimeTableDTO element = new TimeTableDTO(id, firstValue, secondValue, (int) noOfBuses,
								newValueFrequency);

						originGroupThreeList.set(i, element);
					}

				}

				int buses = 0;
				for (int i = 0; i < this.originGroupThreeList.size(); i++) {
					buses = this.originGroupThreeList.get(i).getNoOfBuses() + buses;
				}

				tripsDTO.setTotalTripsOriginThree(buses);

			} else {

				for (int i = 0; i < this.originGroupThreeList.size(); i++) {

					if (originGroupThreeList.get(i).getId().equals(id)) {
						TimeTableDTO element = new TimeTableDTO(id, firstValue, secondValue, 0, 0);
						originGroupThreeList.set(i, element);
					}

				}

				setErrorMessage("Enterd Frequency is not in the time range");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Frequency Can Not Less Than Zero");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void ajaxOnRowEditDestinationThree(RowEditEvent event) {

		String id = ((TimeTableDTO) event.getObject()).getId();
		String firstValue = ((TimeTableDTO) event.getObject()).getFirstValue();
		String secondValue = ((TimeTableDTO) event.getObject()).getSecondValue();
		int newValueFrequency = ((TimeTableDTO) event.getObject()).getFrequency();

		if (newValueFrequency > 0) {

			long noOfBuses = calculateTotalMinute(firstValue, secondValue) / newValueFrequency;

			if (validate(newValueFrequency, 2, 2)) {

				for (int i = 0; i < this.destinationGroupThreeList.size(); i++) {

					if (destinationGroupThreeList.get(i).getId().equals(id)) {

						TimeTableDTO element = new TimeTableDTO(id, firstValue, secondValue, (int) noOfBuses,
								newValueFrequency);

						destinationGroupThreeList.set(i, element);
					}

				}

				int buses = 0;
				for (int i = 0; i < this.destinationGroupThreeList.size(); i++) {
					buses = this.destinationGroupThreeList.get(i).getNoOfBuses() + buses;
				}

				tripsDTO.setTotalTripsDestinationThree(buses);

			} else {

				for (int i = 0; i < this.destinationGroupThreeList.size(); i++) {

					if (destinationGroupThreeList.get(i).getId().equals(id)) {
						TimeTableDTO element = new TimeTableDTO(id, firstValue, secondValue, 0, 0);
						destinationGroupThreeList.set(i, element);
					}

				}

				setErrorMessage("Enterd Frequency is not in the time range");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Frequency Can Not Less Than Zero");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
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

	public void clearOne() {

		routeDetailsList = new ArrayList<>();
		timeTableDTO = new TimeTableDTO();
		groupOneDTO = new TimeTableDTO();
		groupTwoDTO = new TimeTableDTO();
		groupThreeDTO = new TimeTableDTO();
		tripsDTO = new TimeTableDTO();
		busRouteList = timeTableService.getRouteNoList();

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
		editMode = false;

		disabledGroupTwo = true;
		disabledGroupThree = true;
		disabledWithFixedTime = true;
		disabledWithOutFixedTime = true;
		disabledWithFixedBuses = true;
		disableBusCategory = true;

	}

	public void onTabChange(TabChangeEvent event) {

	}

	public String timeConverteToString(int time) {

		String restTime = "00:00";
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

	public void saveAction() {
	
		if (timeTableService.checkProcessPath(timeTableDTO.getGenereatedRefNo(), null) == false) {
	
			if (timeTableService.isRecordFound(timeTableDTO) == false || editMode == true) {
	
				referenceNo = timeTableService.generateReferenceNo();
	
				List<TimeTableDTO> list;
	
				if (groupCount == 1) {
	
					if (tripsDTO.getTotalTripsOriginOne() != 0 && tripsDTO.getTotalTripsDestinationOne() != 0) {
	
						if (tripsDTO.getTotalTripsOriginOne() == tripsDTO.getTotalTripsDestinationOne()) {
	
							if (groupOneDTO.getNoOfPVTbuessOriginOne() != 0
									|| groupOneDTO.getNoOfPVTbuessDestinationOne() != 0) {
	
								if (groupOneDTO.getRestTimeOriginOne() != 0
										&& groupOneDTO.getRestTimeDestinationOne() != 0) {
	
									if (groupOneDTO.getTotalBusesOriginOne() != 0
											|| groupOneDTO.getTotalBusesDestinationOne() != 0) {
	
										/**
										 * changed by hasanga.u 29/10/2019 ->
										 * check no of buses are null before
										 * save the record start
										 **/
										for (TimeTableDTO dto : originGroupOneList) {
											if (dto.getNoOfBuses() == 0) {
												setErrorMessage("No. Of Buses cannot be zero in Group One");
												RequestContext.getCurrentInstance()
														.execute("PF('errorMessage').show()");
												return;
											}
										}
	
										for (TimeTableDTO dto : destinationGroupOneList) {
											if (dto.getNoOfBuses() == 0) {
												setErrorMessage("No. Of Buses cannot be zero in Group One");
												RequestContext.getCurrentInstance()
														.execute("PF('errorMessage').show()");
												return;
											}
										}
										/**
										 * changed by hasanga.u 29/10/2019 ->
										 * check no of buses are null before
										 * save the record start
										 **/
	
										if (editMode == false) {
	
											list = new ArrayList<>();
	
											TimeTableDTO dto = new TimeTableDTO("O", "1",
													tripsDTO.getTotalTripsOriginOne(),
													groupOneDTO.getNoOfPVTbuessOriginOne(),
													groupOneDTO.getNoOfCTBbuessOriginOne(),
													groupOneDTO.getNoOfOtherbuessOriginOne(),
													timeConverteToString(groupOneDTO.getRestTimeOriginOne()),
													groupOneDTO.getTotalBusesOriginOne());
	
											TimeTableDTO dto2 = new TimeTableDTO("D", "1",
													tripsDTO.getTotalTripsDestinationOne(),
													groupOneDTO.getNoOfPVTbuessDestinationOne(),
													groupOneDTO.getNoOfCTBbuessDestinationOne(),
													groupOneDTO.getNoOfOtherbuessDestinationOne(),
													timeConverteToString(groupOneDTO.getRestTimeDestinationOne()),
													groupOneDTO.getTotalBusesDestinationOne());
	
											list.add(0, dto);
											list.add(1, dto2);
	
											boolean isSaveData = timeTableService.insertTripsGenerateMasterData(
													timeTableDTO, referenceNo, sessionBackingBean.getLoginUser());
	
											timeTableService.insertTripsGenerateDetailsOneData(originGroupOneList,
													referenceNo, sessionBackingBean.getLoginUser(), "1", "O");
	
											timeTableService.insertTripsGenerateDetailsOneData(destinationGroupOneList,
													referenceNo, sessionBackingBean.getLoginUser(), "1", "D");
	
											timeTableService.insertTripsGenerateDetailsTwoData(list, referenceNo,
													sessionBackingBean.getLoginUser());
	
											if (isSaveData) {
												setSuccessMessage("Data save successfully");
												RequestContext.getCurrentInstance()
														.execute("PF('successMessage').show()");
												timeTableDTO.setTripRefNo(referenceNo);
											} else {
												setErrorMessage("Data is not save successfully");
												RequestContext.getCurrentInstance()
														.execute("PF('errorMessage').show()");
											}
	
										} else {
	
											/* History Update */
											List<TimeTableDTO> detOneList = timeTableService
													.getTripsTableDetOneDataForHistorySave(timeTableDTO.getRouteNo(),
															timeTableDTO.getGenereatedRefNo());
	
											List<TimeTableDTO> detTwoList = timeTableService
													.getTripsTableDetTwoDataForHistorySave(timeTableDTO.getRouteNo(),
															timeTableDTO.getGenereatedRefNo());
	
											timeTableService.deleteTripsGenerateDetailsOneData(savedReferenceNo);
											timeTableService.deleteTripsGenerateDetailsTwoData(savedReferenceNo);
	
											boolean isSaveOne = timeTableService
													.insertTripsGenerateHistoryDetailsOneData(detOneList,
															sessionBackingBean.getLoginUser());
											boolean isSaveTwo = timeTableService
													.insertTripsGenerateHistoryDetailsTwoData(detTwoList,
															sessionBackingBean.getLoginUser());
	
											/* History Update End */
	
											/* Save New Data Start Here */
											list = new ArrayList<>();
	
											TimeTableDTO dto = new TimeTableDTO("O", "1",
													tripsDTO.getTotalTripsOriginOne(),
													groupOneDTO.getNoOfPVTbuessOriginOne(),
													groupOneDTO.getNoOfCTBbuessOriginOne(),
													groupOneDTO.getNoOfOtherbuessOriginOne(),
													timeConverteToString(groupOneDTO.getRestTimeOriginOne()),
													groupOneDTO.getTotalBusesOriginOne());
	
											TimeTableDTO dto2 = new TimeTableDTO("D", "1",
													tripsDTO.getTotalTripsDestinationOne(),
													groupOneDTO.getNoOfPVTbuessDestinationOne(),
													groupOneDTO.getNoOfCTBbuessDestinationOne(),
													groupOneDTO.getNoOfOtherbuessDestinationOne(),
													timeConverteToString(groupOneDTO.getRestTimeDestinationOne()),
													groupOneDTO.getTotalBusesDestinationOne());
	
											list.add(0, dto);
											list.add(1, dto2);
	
											/**
											 * commented by tharushi.e bcoz when
											 * update No Of Trip Generator , in
											 * nt_t_trips_generator_det update
											 * Destination side data only
											 * "referenceNo---->savedReferenceNo"
											 **/
											timeTableService.insertTripsGenerateDetailsOneData(originGroupOneList,
													savedReferenceNo, sessionBackingBean.getLoginUser(), "1", "O");
	
											timeTableService.insertTripsGenerateDetailsOneData(destinationGroupOneList,
													savedReferenceNo, sessionBackingBean.getLoginUser(), "1", "D");
	
											timeTableService.insertTripsGenerateDetailsTwoData(list, savedReferenceNo,
													sessionBackingBean.getLoginUser());
	
											if (isSaveOne == true && isSaveTwo == true) {
												setSuccessMessage("Data update successfully");
												RequestContext.getCurrentInstance()
														.execute("PF('successMessage').show()");
	
											} else {
												setErrorMessage("Data is not update successfully");
												RequestContext.getCurrentInstance()
														.execute("PF('errorMessage').show()");
											}
	
											/* Save New Data End here */
	
										}
	
									} else {
										setErrorMessage("Total buses can not be zero in Group One");
										RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
									}
	
								} else {
									setErrorMessage("Rest time can not be zero in Group One");
									RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
								}
							} else {
								setErrorMessage("No. of PVT bus can not be zero in Group One");
								RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
							}
	
						} else {
							setErrorMessage("No. of trips should be equal for Group One");
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						}
	
					} else {
						setErrorMessage("No. of trips can not be zero in Group One");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}
				} else if (groupCount == 2) {
	
					if (tripsDTO.getTotalTripsOriginOne() != 0 && tripsDTO.getTotalTripsDestinationOne() != 0) {
	
						if (tripsDTO.getTotalTripsOriginOne() == tripsDTO.getTotalTripsDestinationOne()) {
	
							if (groupOneDTO.getNoOfPVTbuessOriginOne() != 0
									&& groupOneDTO.getNoOfPVTbuessDestinationOne() != 0) {
	
								if (groupOneDTO.getRestTimeOriginOne() != 0
										&& groupOneDTO.getRestTimeDestinationOne() != 0) {
	
									if (groupOneDTO.getTotalBusesOriginOne() != 0
											&& groupOneDTO.getTotalBusesDestinationOne() != 0) {
	
										if (tripsDTO.getTotalTripsOriginTwo() != 0
												&& tripsDTO.getTotalTripsDestinationTwo() != 0) {
	
											if (tripsDTO.getTotalTripsOriginTwo() == tripsDTO
													.getTotalTripsDestinationTwo()) {
	
												if (groupTwoDTO.getNoOfPVTbuessOriginTwo() != 0
														&& groupTwoDTO.getNoOfPVTbuessDestinationTwo() != 0) {
	
													if (groupTwoDTO.getRestTimeOriginTwo() != 0
															&& groupTwoDTO.getRestTimeDestinationTwo() != 0) {
	
														if (groupTwoDTO.getTotalBusesOriginTwo() != 0
																&& groupTwoDTO.getTotalBusesDestinationTwo() != 0) {
	
															/**
															 * changed by
															 * hasanga.u
															 * 29/10/2019 ->
															 * check no of buses
															 * are null before
															 * save the record
															 * start
															 **/
															for (TimeTableDTO dto : originGroupTwoList) {
																if (dto.getNoOfBuses() == 0) {
																	setErrorMessage(
																			"No. Of Buses cannot be zero in Group Two");
																	RequestContext.getCurrentInstance()
																			.execute("PF('errorMessage').show()");
																	return;
																}
															}
	
															for (TimeTableDTO dto : destinationGroupTwoList) {
																if (dto.getNoOfBuses() == 0) {
																	setErrorMessage(
																			"No. Of Buses cannot be zero in Group Two");
																	RequestContext.getCurrentInstance()
																			.execute("PF('errorMessage').show()");
																	return;
																}
															}
															/**
															 * changed by
															 * hasanga.u
															 * 29/10/2019 ->
															 * check no of buses
															 * are null before
															 * save the record
															 * start
															 **/
	
															if (editMode == false) {
	
																/* Save Data */
	
																list = new ArrayList<>();
	
																TimeTableDTO dto = new TimeTableDTO("O", "1",
																		tripsDTO.getTotalTripsOriginOne(),
																		groupOneDTO.getNoOfPVTbuessOriginOne(),
																		groupOneDTO.getNoOfCTBbuessOriginOne(),
																		groupOneDTO.getNoOfOtherbuessOriginOne(),
																		timeConverteToString(
																				groupOneDTO.getRestTimeOriginOne()),
																		groupOneDTO.getTotalBusesOriginOne());
	
																TimeTableDTO dto2 = new TimeTableDTO("D", "1",
																		tripsDTO.getTotalTripsDestinationOne(),
																		groupOneDTO.getNoOfPVTbuessDestinationOne(),
																		groupOneDTO.getNoOfCTBbuessDestinationOne(),
																		groupOneDTO.getNoOfOtherbuessDestinationOne(),
																		timeConverteToString(groupOneDTO
																				.getRestTimeDestinationOne()),
																		groupOneDTO.getTotalBusesDestinationOne());
	
																TimeTableDTO dto3 = new TimeTableDTO("O", "2",
																		tripsDTO.getTotalTripsOriginTwo(),
																		groupTwoDTO.getNoOfPVTbuessOriginTwo(),
																		groupTwoDTO.getNoOfCTBbuessOriginTwo(),
																		groupTwoDTO.getNoOfOtherbuessOriginTwo(),
																		timeConverteToString(
																				groupTwoDTO.getRestTimeOriginTwo()),
																		groupTwoDTO.getTotalBusesOriginTwo());
	
																TimeTableDTO dto4 = new TimeTableDTO("D", "2",
																		tripsDTO.getTotalTripsDestinationTwo(),
																		groupTwoDTO.getNoOfPVTbuessDestinationTwo(),
																		groupTwoDTO.getNoOfCTBbuessDestinationTwo(),
																		groupTwoDTO.getNoOfOtherbuessDestinationTwo(),
																		timeConverteToString(groupTwoDTO
																				.getRestTimeDestinationTwo()),
																		groupTwoDTO.getTotalBusesDestinationTwo());
	
																list.add(0, dto);
																list.add(1, dto2);
																list.add(2, dto3);
																list.add(3, dto4);
	
																boolean isSaveData = timeTableService
																		.insertTripsGenerateMasterData(timeTableDTO,
																				referenceNo,
																				sessionBackingBean.getLoginUser());
	
																timeTableService.insertTripsGenerateDetailsOneData(
																		originGroupOneList, referenceNo,
																		sessionBackingBean.getLoginUser(), "1", "O");
	
																timeTableService.insertTripsGenerateDetailsOneData(
																		destinationGroupOneList, referenceNo,
																		sessionBackingBean.getLoginUser(), "1", "D");
	
																timeTableService.insertTripsGenerateDetailsOneData(
																		originGroupTwoList, referenceNo,
																		sessionBackingBean.getLoginUser(), "2", "O");
	
																timeTableService.insertTripsGenerateDetailsOneData(
																		destinationGroupTwoList, referenceNo,
																		sessionBackingBean.getLoginUser(), "2", "D");
	
																timeTableService.insertTripsGenerateDetailsTwoData(list,
																		referenceNo, sessionBackingBean.getLoginUser());
	
																if (isSaveData) {
																	setSuccessMessage("Data save successfully");
																	RequestContext.getCurrentInstance()
																			.execute("PF('successMessage').show()");
																	timeTableDTO.setTripRefNo(referenceNo);
																} else {
																	setErrorMessage("Data is not save successfully");
																	RequestContext.getCurrentInstance()
																			.execute("PF('errorMessage').show()");
																}
	
															} else {
																/*
																 * History
																 * Update
																 */
																List<TimeTableDTO> detOneList = timeTableService
																		.getTripsTableDetOneDataForHistorySave(
																				timeTableDTO.getRouteNo(),
																				timeTableDTO.getGenereatedRefNo());
	
																List<TimeTableDTO> detTwoList = timeTableService
																		.getTripsTableDetTwoDataForHistorySave(
																				timeTableDTO.getRouteNo(),
																				timeTableDTO.getGenereatedRefNo());
	
																timeTableService.deleteTripsGenerateDetailsOneData(
																		savedReferenceNo);
																timeTableService.deleteTripsGenerateDetailsTwoData(
																		savedReferenceNo);
	
																boolean isSaveOne = timeTableService
																		.insertTripsGenerateHistoryDetailsOneData(
																				detOneList,
																				sessionBackingBean.getLoginUser());
																boolean isSaveTwo = timeTableService
																		.insertTripsGenerateHistoryDetailsTwoData(
																				detTwoList,
																				sessionBackingBean.getLoginUser());
	
																/*
																 * History
																 * Update End
																 */
	
																/* Save Data */
	
																list = new ArrayList<>();
	
																TimeTableDTO dto = new TimeTableDTO("O", "1",
																		tripsDTO.getTotalTripsOriginOne(),
																		groupOneDTO.getNoOfPVTbuessOriginOne(),
																		groupOneDTO.getNoOfCTBbuessOriginOne(),
																		groupOneDTO.getNoOfOtherbuessOriginOne(),
																		timeConverteToString(
																				groupOneDTO.getRestTimeOriginOne()),
																		groupOneDTO.getTotalBusesOriginOne());
	
																TimeTableDTO dto2 = new TimeTableDTO("D", "1",
																		tripsDTO.getTotalTripsDestinationOne(),
																		groupOneDTO.getNoOfPVTbuessDestinationOne(),
																		groupOneDTO.getNoOfCTBbuessDestinationOne(),
																		groupOneDTO.getNoOfOtherbuessDestinationOne(),
																		timeConverteToString(groupOneDTO
																				.getRestTimeDestinationOne()),
																		groupOneDTO.getTotalBusesDestinationOne());
	
																TimeTableDTO dto3 = new TimeTableDTO("O", "2",
																		tripsDTO.getTotalTripsOriginTwo(),
																		groupTwoDTO.getNoOfPVTbuessOriginTwo(),
																		groupTwoDTO.getNoOfCTBbuessOriginTwo(),
																		groupTwoDTO.getNoOfOtherbuessOriginTwo(),
																		timeConverteToString(
																				groupTwoDTO.getRestTimeOriginTwo()),
																		groupTwoDTO.getTotalBusesOriginTwo());
	
																TimeTableDTO dto4 = new TimeTableDTO("D", "2",
																		tripsDTO.getTotalTripsDestinationTwo(),
																		groupTwoDTO.getNoOfPVTbuessDestinationTwo(),
																		groupTwoDTO.getNoOfCTBbuessDestinationTwo(),
																		groupTwoDTO.getNoOfOtherbuessDestinationTwo(),
																		timeConverteToString(groupTwoDTO
																				.getRestTimeDestinationTwo()),
																		groupTwoDTO.getTotalBusesDestinationTwo());
	
																list.add(0, dto);
																list.add(1, dto2);
																list.add(2, dto3);
																list.add(3, dto4);
	
																timeTableService.insertTripsGenerateDetailsOneData(
																		originGroupOneList, savedReferenceNo,
																		sessionBackingBean.getLoginUser(), "1", "O");
	
																timeTableService.insertTripsGenerateDetailsOneData(
																		destinationGroupOneList, savedReferenceNo,
																		sessionBackingBean.getLoginUser(), "1", "D");
	
																timeTableService.insertTripsGenerateDetailsOneData(
																		originGroupTwoList, savedReferenceNo,
																		sessionBackingBean.getLoginUser(), "2", "O");
	
																timeTableService.insertTripsGenerateDetailsOneData(
																		destinationGroupTwoList, savedReferenceNo,
																		sessionBackingBean.getLoginUser(), "2", "D");
	
																timeTableService.insertTripsGenerateDetailsTwoData(list,
																		savedReferenceNo,
																		sessionBackingBean.getLoginUser());
	
																if (isSaveOne == true && isSaveTwo == true) {
																	setSuccessMessage("Data update successfully");
																	RequestContext.getCurrentInstance()
																			.execute("PF('successMessage').show()");
	
																} else {
																	setErrorMessage("Data is not update successfully");
																	RequestContext.getCurrentInstance()
																			.execute("PF('errorMessage').show()");
																}
															}
	
														} else {
															setErrorMessage("Total buses can not be zero in Group Two");
															RequestContext.getCurrentInstance()
																	.execute("PF('errorMessage').show()");
														}
	
													} else {
														setErrorMessage("Rest time can not be zero in Group Two");
														RequestContext.getCurrentInstance()
																.execute("PF('errorMessage').show()");
													}
												} else {
													setErrorMessage("No. of PVT bus can not be zero in Group Two");
													RequestContext.getCurrentInstance()
															.execute("PF('errorMessage').show()");
												}
	
											} else {
												setErrorMessage("No. of trips should be equal for Group Two");
												RequestContext.getCurrentInstance()
														.execute("PF('errorMessage').show()");
											}
	
										} else {
											setErrorMessage("No. of trips can not be zero in Group Two");
											RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
										}
	
									} else {
										setErrorMessage("Total buses can not be zero in Group One");
										RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
									}
	
								} else {
									setErrorMessage("Rest time can not be zero in Group One");
									RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
								}
							} else {
								setErrorMessage("No. of PVT bus can not be zero in Group One");
								RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
							}
						} else {
							setErrorMessage("No. of trips should be equal for Group One");
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						}
	
					} else {
						setErrorMessage("No. of trips can not be zero in Group One");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}
	
				} else if (groupCount == 3) {
	
					if (tripsDTO.getTotalTripsOriginOne() != 0 && tripsDTO.getTotalTripsDestinationOne() != 0) {
	
						if (tripsDTO.getTotalTripsOriginOne() == tripsDTO.getTotalTripsDestinationOne()) {
	
							if (groupOneDTO.getNoOfPVTbuessOriginOne() != 0
									&& groupOneDTO.getNoOfPVTbuessDestinationOne() != 0) {
	
								if (groupOneDTO.getRestTimeOriginOne() != 0
										&& groupOneDTO.getRestTimeDestinationOne() != 0) {
	
									if (groupOneDTO.getTotalBusesOriginOne() != 0
											&& groupOneDTO.getTotalBusesDestinationOne() != 0) {
	
										if (tripsDTO.getTotalTripsOriginTwo() != 0
												&& tripsDTO.getTotalTripsDestinationTwo() != 0) {
											if (tripsDTO.getTotalTripsOriginTwo() == tripsDTO
													.getTotalTripsDestinationTwo()) {
	
												if (groupTwoDTO.getNoOfPVTbuessOriginTwo() != 0
														&& groupTwoDTO.getNoOfPVTbuessDestinationTwo() != 0) {
	
													if (groupTwoDTO.getRestTimeOriginTwo() != 0
															&& groupTwoDTO.getRestTimeDestinationTwo() != 0) {
	
														if (groupTwoDTO.getTotalBusesOriginTwo() != 0
																&& groupTwoDTO.getTotalBusesDestinationTwo() != 0) {
	
															if (tripsDTO.getTotalTripsOriginThree() != 0
																	&& tripsDTO.getTotalTripsDestinationThree() != 0) {
	
																if (tripsDTO.getTotalTripsOriginThree() == tripsDTO
																		.getTotalTripsDestinationThree()) {
	
																	if (groupThreeDTO.getNoOfPVTbuessOriginThree() != 0
																			&& groupThreeDTO
																					.getNoOfPVTbuessDestinationThree() != 0) {
	
																		if (groupThreeDTO.getRestTimeOriginThree() != 0
																				&& groupThreeDTO
																						.getRestTimeDestinationThree() != 0) {
	
																			if (groupThreeDTO
																					.getTotalBusesOriginThree() != 0
																					&& groupThreeDTO
																							.getTotalBusesDestinationThree() != 0) {
	
																				/**
																				 * changed
																				 * by
																				 * hasanga.u
																				 * 29/10/2019
																				 * ->
																				 * check
																				 * no
																				 * of
																				 * buses
																				 * are
																				 * null
																				 * before
																				 * save
																				 * the
																				 * record
																				 * start
																				 **/
																				for (TimeTableDTO dto : originGroupThreeList) {
																					if (dto.getNoOfBuses() == 0) {
																						setErrorMessage(
																								"No. Of Buses cannot be zero in Group Three");
																						RequestContext
																								.getCurrentInstance()
																								.execute(
																										"PF('errorMessage').show()");
																						return;
																					}
																				}
	
																				for (TimeTableDTO dto : destinationGroupThreeList) {
																					if (dto.getNoOfBuses() == 0) {
																						setErrorMessage(
																								"No. Of Buses cannot be zero in Group Three");
																						RequestContext
																								.getCurrentInstance()
																								.execute(
																										"PF('errorMessage').show()");
																						return;
																					}
																				}
																				/**
																				 * check
																				 * no
																				 * of
																				 * buses
																				 * are
																				 * null
																				 * before
																				 * save
																				 * the
																				 * record
																				 * start
																				 **/
	
																				if (editMode == false) {
	
																					list = new ArrayList<>();
	
																					TimeTableDTO dto = new TimeTableDTO(
																							"O", "1",
																							tripsDTO.getTotalTripsOriginOne(),
																							groupOneDTO
																									.getNoOfPVTbuessOriginOne(),
																							groupOneDTO
																									.getNoOfCTBbuessOriginOne(),
																							groupOneDTO
																									.getNoOfOtherbuessOriginOne(),
																							timeConverteToString(
																									groupOneDTO
																											.getRestTimeOriginOne()),
																							groupOneDTO
																									.getTotalBusesOriginOne());
	
																					TimeTableDTO dto2 = new TimeTableDTO(
																							"D", "1",
																							tripsDTO.getTotalTripsDestinationOne(),
																							groupOneDTO
																									.getNoOfPVTbuessDestinationOne(),
																							groupOneDTO
																									.getNoOfCTBbuessDestinationOne(),
																							groupOneDTO
																									.getNoOfOtherbuessDestinationOne(),
																							timeConverteToString(
																									groupOneDTO
																											.getRestTimeDestinationOne()),
																							groupOneDTO
																									.getTotalBusesDestinationOne());
	
																					TimeTableDTO dto3 = new TimeTableDTO(
																							"O", "2",
																							tripsDTO.getTotalTripsOriginTwo(),
																							groupTwoDTO
																									.getNoOfPVTbuessOriginTwo(),
																							groupTwoDTO
																									.getNoOfCTBbuessOriginTwo(),
																							groupTwoDTO
																									.getNoOfOtherbuessOriginTwo(),
																							timeConverteToString(
																									groupTwoDTO
																											.getRestTimeOriginTwo()),
																							groupTwoDTO
																									.getTotalBusesOriginTwo());
	
																					TimeTableDTO dto4 = new TimeTableDTO(
																							"D", "2",
																							tripsDTO.getTotalTripsDestinationTwo(),
																							groupTwoDTO
																									.getNoOfPVTbuessDestinationTwo(),
																							groupTwoDTO
																									.getNoOfCTBbuessDestinationTwo(),
																							groupTwoDTO
																									.getNoOfOtherbuessDestinationTwo(),
																							timeConverteToString(
																									groupTwoDTO
																											.getRestTimeDestinationTwo()),
																							groupTwoDTO
																									.getTotalBusesDestinationTwo());
	
																					TimeTableDTO dto5 = new TimeTableDTO(
																							"O", "3",
																							tripsDTO.getTotalTripsOriginThree(),
																							groupThreeDTO
																									.getNoOfPVTbuessOriginThree(),
																							groupThreeDTO
																									.getNoOfCTBbuessOriginThree(),
																							groupThreeDTO
																									.getNoOfOtherbuessOriginThree(),
																							timeConverteToString(
																									groupThreeDTO
																											.getRestTimeOriginThree()),
																							groupThreeDTO
																									.getTotalBusesOriginThree());
	
																					TimeTableDTO dto6 = new TimeTableDTO(
																							"D", "3",
																							tripsDTO.getTotalTripsDestinationThree(),
																							groupThreeDTO
																									.getNoOfPVTbuessDestinationThree(),
																							groupThreeDTO
																									.getNoOfCTBbuessDestinationThree(),
																							groupThreeDTO
																									.getNoOfOtherbuessDestinationThree(),
																							timeConverteToString(
																									groupThreeDTO
																											.getRestTimeDestinationThree()),
																							groupThreeDTO
																									.getTotalBusesDestinationThree());
	
																					list.add(0, dto);
																					list.add(1, dto2);
																					list.add(2, dto3);
																					list.add(3, dto4);
																					list.add(4, dto5);
																					list.add(5, dto6);
	
																					boolean isSaveData = timeTableService
																							.insertTripsGenerateMasterData(
																									timeTableDTO,
																									referenceNo,
																									sessionBackingBean
																											.getLoginUser());
	
																					timeTableService
																							.insertTripsGenerateDetailsOneData(
																									originGroupOneList,
																									referenceNo,
																									sessionBackingBean
																											.getLoginUser(),
																									"1", "O");
	
																					timeTableService
																							.insertTripsGenerateDetailsOneData(
																									destinationGroupOneList,
																									referenceNo,
																									sessionBackingBean
																											.getLoginUser(),
																									"1", "D");
	
																					timeTableService
																							.insertTripsGenerateDetailsOneData(
																									originGroupTwoList,
																									referenceNo,
																									sessionBackingBean
																											.getLoginUser(),
																									"2", "O");
	
																					timeTableService
																							.insertTripsGenerateDetailsOneData(
																									destinationGroupTwoList,
																									referenceNo,
																									sessionBackingBean
																											.getLoginUser(),
																									"2", "D");
	
																					timeTableService
																							.insertTripsGenerateDetailsOneData(
																									originGroupThreeList,
																									referenceNo,
																									sessionBackingBean
																											.getLoginUser(),
																									"3", "O");
	
																					timeTableService
																							.insertTripsGenerateDetailsOneData(
																									destinationGroupThreeList,
																									referenceNo,
																									sessionBackingBean
																											.getLoginUser(),
																									"3", "D");
	
																					timeTableService
																							.insertTripsGenerateDetailsTwoData(
																									list, referenceNo,
																									sessionBackingBean
																											.getLoginUser());
	
																					if (isSaveData) {
																						setSuccessMessage(
																								"Data save successfully");
																						RequestContext
																								.getCurrentInstance()
																								.execute(
																										"PF('successMessage').show()");
																						timeTableDTO.setTripRefNo(
																								referenceNo);
																					} else {
																						setErrorMessage(
																								"Data is not save successfully");
																						RequestContext
																								.getCurrentInstance()
																								.execute(
																										"PF('errorMessage').show()");
																					}
	
																				} else {
																					/*
																					 * History
																					 * Update
																					 */
																					List<TimeTableDTO> detOneList = timeTableService
																							.getTripsTableDetOneDataForHistorySave(
																									timeTableDTO
																											.getRouteNo(),
																									timeTableDTO
																											.getGenereatedRefNo());
	
																					List<TimeTableDTO> detTwoList = timeTableService
																							.getTripsTableDetTwoDataForHistorySave(
																									timeTableDTO
																											.getRouteNo(),
																									timeTableDTO
																											.getGenereatedRefNo());
	
																					timeTableService
																							.deleteTripsGenerateDetailsOneData(
																									savedReferenceNo);
																					timeTableService
																							.deleteTripsGenerateDetailsTwoData(
																									savedReferenceNo);
	
																					boolean isSaveOne = timeTableService
																							.insertTripsGenerateHistoryDetailsOneData(
																									detOneList,
																									sessionBackingBean
																											.getLoginUser());
																					boolean isSaveTwo = timeTableService
																							.insertTripsGenerateHistoryDetailsTwoData(
																									detTwoList,
																									sessionBackingBean
																											.getLoginUser());
	
																					/*
																					 * History
																					 * Update
																					 * End
																					 */
	
																					list = new ArrayList<>();
	
																					TimeTableDTO dto = new TimeTableDTO(
																							"O", "1",
																							tripsDTO.getTotalTripsOriginOne(),
																							groupOneDTO
																									.getNoOfPVTbuessOriginOne(),
																							groupOneDTO
																									.getNoOfCTBbuessOriginOne(),
																							groupOneDTO
																									.getNoOfOtherbuessOriginOne(),
																							timeConverteToString(
																									groupOneDTO
																											.getRestTimeOriginOne()),
																							groupOneDTO
																									.getTotalBusesOriginOne());
	
																					TimeTableDTO dto2 = new TimeTableDTO(
																							"D", "1",
																							tripsDTO.getTotalTripsDestinationOne(),
																							groupOneDTO
																									.getNoOfPVTbuessDestinationOne(),
																							groupOneDTO
																									.getNoOfCTBbuessDestinationOne(),
																							groupOneDTO
																									.getNoOfOtherbuessDestinationOne(),
																							timeConverteToString(
																									groupOneDTO
																											.getRestTimeDestinationOne()),
																							groupOneDTO
																									.getTotalBusesDestinationOne());
	
																					TimeTableDTO dto3 = new TimeTableDTO(
																							"O", "2",
																							tripsDTO.getTotalTripsOriginTwo(),
																							groupTwoDTO
																									.getNoOfPVTbuessOriginTwo(),
																							groupTwoDTO
																									.getNoOfCTBbuessOriginTwo(),
																							groupTwoDTO
																									.getNoOfOtherbuessOriginTwo(),
																							timeConverteToString(
																									groupTwoDTO
																											.getRestTimeOriginTwo()),
																							groupTwoDTO
																									.getTotalBusesOriginTwo());
	
																					TimeTableDTO dto4 = new TimeTableDTO(
																							"D", "2",
																							tripsDTO.getTotalTripsDestinationTwo(),
																							groupTwoDTO
																									.getNoOfPVTbuessDestinationTwo(),
																							groupTwoDTO
																									.getNoOfCTBbuessDestinationTwo(),
																							groupTwoDTO
																									.getNoOfOtherbuessDestinationTwo(),
																							timeConverteToString(
																									groupTwoDTO
																											.getRestTimeDestinationTwo()),
																							groupTwoDTO
																									.getTotalBusesDestinationTwo());
	
																					TimeTableDTO dto5 = new TimeTableDTO(
																							"O", "3",
																							tripsDTO.getTotalTripsOriginThree(),
																							groupThreeDTO
																									.getNoOfPVTbuessOriginThree(),
																							groupThreeDTO
																									.getNoOfCTBbuessOriginThree(),
																							groupThreeDTO
																									.getNoOfOtherbuessOriginThree(),
																							timeConverteToString(
																									groupThreeDTO
																											.getRestTimeOriginThree()),
																							groupThreeDTO
																									.getTotalBusesOriginThree());
	
																					TimeTableDTO dto6 = new TimeTableDTO(
																							"D", "3",
																							tripsDTO.getTotalTripsDestinationThree(),
																							groupThreeDTO
																									.getNoOfPVTbuessDestinationThree(),
																							groupThreeDTO
																									.getNoOfCTBbuessDestinationThree(),
																							groupThreeDTO
																									.getNoOfOtherbuessDestinationThree(),
																							timeConverteToString(
																									groupThreeDTO
																											.getRestTimeDestinationThree()),
																							groupThreeDTO
																									.getTotalBusesDestinationThree());
	
																					list.add(0, dto);
																					list.add(1, dto2);
																					list.add(2, dto3);
																					list.add(3, dto4);
																					list.add(4, dto5);
																					list.add(5, dto6);
	
																					timeTableService
																							.insertTripsGenerateDetailsOneData(
																									originGroupOneList,
																									referenceNo,
																									sessionBackingBean
																											.getLoginUser(),
																									"1", "O");
	
																					timeTableService
																							.insertTripsGenerateDetailsOneData(
																									destinationGroupOneList,
																									referenceNo,
																									sessionBackingBean
																											.getLoginUser(),
																									"1", "D");
	
																					timeTableService
																							.insertTripsGenerateDetailsOneData(
																									originGroupTwoList,
																									referenceNo,
																									sessionBackingBean
																											.getLoginUser(),
																									"2", "O");
	
																					timeTableService
																							.insertTripsGenerateDetailsOneData(
																									destinationGroupTwoList,
																									referenceNo,
																									sessionBackingBean
																											.getLoginUser(),
																									"2", "D");
	
																					timeTableService
																							.insertTripsGenerateDetailsOneData(
																									originGroupThreeList,
																									referenceNo,
																									sessionBackingBean
																											.getLoginUser(),
																									"3", "O");
	
																					timeTableService
																							.insertTripsGenerateDetailsOneData(
																									destinationGroupThreeList,
																									referenceNo,
																									sessionBackingBean
																											.getLoginUser(),
																									"3", "D");
	
																					timeTableService
																							.insertTripsGenerateDetailsTwoData(
																									list, referenceNo,
																									sessionBackingBean
																											.getLoginUser());
	
																					if (isSaveOne == true
																							&& isSaveTwo == true) {
																						setSuccessMessage(
																								"Data save successfully");
																						RequestContext
																								.getCurrentInstance()
																								.execute(
																										"PF('successMessage').show()");
	
																					} else {
																						setErrorMessage(
																								"Data is not save successfully");
																						RequestContext
																								.getCurrentInstance()
																								.execute(
																										"PF('errorMessage').show()");
																					}
																				}
	
																			} else {
																				setErrorMessage(
																						"Total buses can not be zero in Group Three");
																				RequestContext.getCurrentInstance()
																						.execute(
																								"PF('errorMessage').show()");
																			}
	
																		} else {
																			setErrorMessage(
																					"Rest time can not be zero in Group Three");
																			RequestContext.getCurrentInstance().execute(
																					"PF('errorMessage').show()");
																		}
																	} else {
																		setErrorMessage(
																				"No. of PVT bus can not be zero in Group Three");
																		RequestContext.getCurrentInstance()
																				.execute("PF('errorMessage').show()");
																	}
	
																} else {
																	setErrorMessage(
																			"No. of trips should be equal Group Three");
																	RequestContext.getCurrentInstance()
																			.execute("PF('errorMessage').show()");
																}
	
															} else {
																setErrorMessage(
																		"No. of trips can not be zero in Group Three");
																RequestContext.getCurrentInstance()
																		.execute("PF('errorMessage').show()");
															}
	
														} else {
															setErrorMessage("Total buses can not be zero in Group Two");
															RequestContext.getCurrentInstance()
																	.execute("PF('errorMessage').show()");
														}
	
													} else {
														setErrorMessage("Rest time can not be zero in Group Two");
														RequestContext.getCurrentInstance()
																.execute("PF('errorMessage').show()");
													}
												} else {
													setErrorMessage("No. of PVT bus can not be zero in Group Two");
													RequestContext.getCurrentInstance()
															.execute("PF('errorMessage').show()");
												}
	
											} else {
												setErrorMessage("No. of trips should be equal for Group Two");
												RequestContext.getCurrentInstance()
														.execute("PF('errorMessage').show()");
											}
	
										} else {
											setErrorMessage("No. of trips can not be zero in Group Two");
											RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
										}
	
									} else {
										setErrorMessage("Total buses can not be zero in Group One");
										RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
									}
	
								} else {
									setErrorMessage("Rest time can not be zero in Group One");
									RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
								}
							} else {
								setErrorMessage("No. of PVT bus can not be zero in Group One");
								RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
							}
	
						} else {
							setErrorMessage("No. of trips should be equal for Group One");
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						}
	
					} else {
						setErrorMessage("No. of trips can not be zero in Group One");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}
	
				}
	
			} else {
				setErrorMessage("Similar recoard found");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
	
		} else
	
		{
			setErrorMessage("Panel Generate process is began. Can not edit this record. ");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	
	}

	public void clearTwo() {
		routeDetailsList = new ArrayList<>();
		timeTableDTO = new TimeTableDTO();
		groupOneDTO = new TimeTableDTO();
		groupTwoDTO = new TimeTableDTO();
		groupThreeDTO = new TimeTableDTO();
		tripsDTO = new TimeTableDTO();
		busRouteList = timeTableService.getRouteNoList();

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
		editMode = false;

		disabledGroupTwo = true;
		disabledGroupThree = true;
		disabledWithFixedTime = true;
		disabledWithOutFixedTime = true;
		disabledWithFixedBuses = true;
		disableBusCategory = true;
	}

	public void panelGeneratorWithFixedTime() {
		RequestContext.getCurrentInstance().execute("PF('fiedTimeVAR').show()");
	}

	public void panelGeneratorWithOutFixedTime() {
		RequestContext.getCurrentInstance().execute("PF('withOutFiedTimeVAR').show()");
	}

	public void panelGeneratorWithFixedBuses() {
		RequestContext.getCurrentInstance().execute("PF('fiedBusesVAR').show()");
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

	public String getRestTimeString() {
		return restTimeString;
	}

	public void setRestTimeString(String restTimeString) {
		this.restTimeString = restTimeString;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public String getSavedReferenceNo() {
		return savedReferenceNo;
	}

	public void setSavedReferenceNo(String savedReferenceNo) {
		this.savedReferenceNo = savedReferenceNo;
	}

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	public boolean isDisabledWithFixedTime() {
		return disabledWithFixedTime;
	}

	public void setDisabledWithFixedTime(boolean disabledWithFixedTime) {
		this.disabledWithFixedTime = disabledWithFixedTime;
	}

	public boolean isDisabledWithOutFixedTime() {
		return disabledWithOutFixedTime;
	}

	public void setDisabledWithOutFixedTime(boolean disabledWithOutFixedTime) {
		this.disabledWithOutFixedTime = disabledWithOutFixedTime;
	}

	public boolean isDisabledWithFixedBuses() {
		return disabledWithFixedBuses;
	}

	public void setDisabledWithFixedBuses(boolean disabledWithFixedBuses) {
		this.disabledWithFixedBuses = disabledWithFixedBuses;
	}

	public List<TimeTableDTO> getBusCategoryList() {
		return busCategoryList;
	}

	public void setBusCategoryList(List<TimeTableDTO> busCategoryList) {
		this.busCategoryList = busCategoryList;
	}

	public boolean isDisableBusCategory() {
		return disableBusCategory;
	}

	public void setDisableBusCategory(boolean disableBusCategory) {
		this.disableBusCategory = disableBusCategory;
	}

	public int getPvtBusCountOrigin() {
		return pvtBusCountOrigin;
	}

	public void setPvtBusCountOrigin(int pvtBusCountOrigin) {
		this.pvtBusCountOrigin = pvtBusCountOrigin;
	}

	public int getPvtBusCountDestination() {
		return pvtBusCountDestination;
	}

	public void setPvtBusCountDestination(int pvtBusCountDestination) {
		this.pvtBusCountDestination = pvtBusCountDestination;
	}

	public RouteScheduleService getRouteScheduleService() {
		return routeScheduleService;
	}

	public void setRouteScheduleService(RouteScheduleService routeScheduleService) {
		this.routeScheduleService = routeScheduleService;
	}

	public void viewPrivateBusesActionOrigin() {
		RequestContext.getCurrentInstance().execute("PF('originPVTBusesDlg').show()");
	}

	public void viewPrivateBusesActionDestination() {
		RequestContext.getCurrentInstance().execute("PF('destinationPVTBusesDlg').show()");
	}

	public List<VehicleInspectionDTO> getOriginPVTBusesList() {
		return originPVTBusesList;
	}

	public void setOriginPVTBusesList(List<VehicleInspectionDTO> originPVTBusesList) {
		this.originPVTBusesList = originPVTBusesList;
	}

	public List<VehicleInspectionDTO> getDestinatinoPVTBusesList() {
		return destinatinoPVTBusesList;
	}

	public void setDestinatinoPVTBusesList(List<VehicleInspectionDTO> destinatinoPVTBusesList) {
		this.destinatinoPVTBusesList = destinatinoPVTBusesList;
	}

}
