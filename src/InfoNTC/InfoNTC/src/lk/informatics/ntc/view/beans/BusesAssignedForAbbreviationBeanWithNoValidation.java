package lk.informatics.ntc.view.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.RouteScheduleDTO;
import lk.informatics.ntc.model.dto.TimeTableDTO;
import lk.informatics.ntc.model.service.BusesAssignedForAbbreviationService;
import lk.informatics.ntc.model.service.RouteScheduleService;
import lk.informatics.ntc.model.service.TimeTableService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "busesAssignedForAbbreviationBeanWithNoValidation")
@ViewScoped
public class BusesAssignedForAbbreviationBeanWithNoValidation implements Serializable {

	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	private RouteScheduleService routeScheduleService;
	private BusesAssignedForAbbreviationService busesAssignedForAbbreviationService;
	private TimeTableService timeTableService;
	private RouteScheduleDTO routeScheduleDTO;
	private List<RouteScheduleDTO> busRouteList, groupNoList, selectLeavePositionList, busCategoryList, busForRouteList,
			columnKeys, abbreviationOriginList, abbreviationDestinationList, modifyList;
	private List<TimeTableDTO> originBusList, destinationBusList;
	private List<RouteScheduleDTO> leaveForRouteList, mainSaveList;

	private List<ColumnModel> columns, columnsLeaves;

	private boolean renderPanelTwo, disableGroupNo, disableBusCategory;
	private String alertMSG, successMessage, errorMessage, origin, destination, groupNo;
	private String tripType; // Type "O" for origin and "D" for destination
	private int noOfLeaves, noOfBuses, noOfTrips, noOfDaysFortimeTable, noOfTripsForSelectedSide, tripID;
	private boolean renderNormal, renderTableTwo;

	private final int maximumLeaveForDay = 9;
	private final int maximumDays = 92;
	private boolean saveBtnDisable;
	private boolean originBusSelect, destinationBusSelect;
	private boolean swapOriginDestination;
	private boolean originDataTblDisable;

	private List<String> leavePositionList;
	private List<TimeTableDTO> originCancelBuses;
	private List<TimeTableDTO> destinationCancelBuses;
	List<RouteScheduleDTO> dList = new ArrayList<>();
	private boolean disableApplyBtn;

	@PostConstruct
	public void init() {
		routeScheduleService = (RouteScheduleService) SpringApplicationContex.getBean("routeScheduleService");
		busesAssignedForAbbreviationService = (BusesAssignedForAbbreviationService) SpringApplicationContex
				.getBean("busesAssignedForAbbreviationService");
		timeTableService = (TimeTableService) SpringApplicationContex.getBean("timeTableService");
		disableGroupNo = true;
		loadValue();
	}

	private void loadValue() {
		routeScheduleDTO = new RouteScheduleDTO();
		busRouteList = new ArrayList<RouteScheduleDTO>();
		busCategoryList = new ArrayList<RouteScheduleDTO>();
		busRouteList = routeScheduleService.getRouteNo();
		leavePositionList = new ArrayList<String>();
		selectLeavePositionList = new ArrayList<RouteScheduleDTO>();
		originBusList = new ArrayList<TimeTableDTO>();
		destinationBusList = new ArrayList<TimeTableDTO>();
		abbreviationOriginList = new ArrayList<RouteScheduleDTO>();
		abbreviationDestinationList = new ArrayList<RouteScheduleDTO>();
		disableBusCategory = true;
		renderNormal = false;
		renderTableTwo = true;
		leaveForRouteList = new ArrayList<RouteScheduleDTO>();
		mainSaveList = new ArrayList<RouteScheduleDTO>();
		tripID = 0;
		saveBtnDisable = false;
		noOfBuses = 0;
		noOfLeaves = 0;
		noOfTrips = 0;
		originBusSelect = false;
		destinationBusSelect = false;
		swapOriginDestination = false;
		originCancelBuses = new ArrayList<TimeTableDTO>();
		destinationCancelBuses = new ArrayList<TimeTableDTO>();
		originDataTblDisable = false;
		modifyList = new ArrayList<RouteScheduleDTO>();
		disableApplyBtn = true;

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

		/* Save real origin and destination */
		origin = routeScheduleDTO.getOrigin();
		destination = routeScheduleDTO.getDestination();

		/* Fill the available groups related to route */
		groupNoList = new ArrayList<>();
		groupNoList = routeScheduleService.getGroupNoList(routeScheduleDTO.getRouteNo(),
				routeScheduleDTO.getGeneratedRefNo());
		

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
				routeScheduleDTO.setSwapEnds(true);
			} else {
				routeScheduleDTO.setOrigin(origin);
				routeScheduleDTO.setDestination(destination);
				tripType = "O";
				routeScheduleDTO.setSwapEnds(false);
			}

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

					/* Maximum leaves can be 18 for both end */
					/*
					 * if (totalLeaveManager() < maximumLeaveForDay * 2) {
					 * 
					 * if (totalLeaveManager() >= 0) {
					 */
					/** Swap issue fixing by tharushi.e **/

					if (routeScheduleDTO.isSwapEnds() == true) {
						tripType = "D";

					}

					else if (routeScheduleDTO.isSwapEnds() == false) {
						tripType = "O";
					}

					/** end **/

					if (displayOriginAndDestinationDetails() >= 0) {

						swapOriginDestination = true;

						originBusList = new ArrayList<TimeTableDTO>();
						destinationBusList = new ArrayList<TimeTableDTO>();

						if (tripType != null && !tripType.isEmpty() && !tripType.trim().equals("")) {
							
//							changed by thilina.d on 13-10-2021
//							originBusList = timeTableService.getAllBusNoForFixedBuses(routeScheduleDTO.getRouteNo(),
//									routeScheduleDTO.getBusCategory(), "O");
//							destinationBusList = timeTableService.getAllBusNoForFixedBuses(
//									routeScheduleDTO.getRouteNo(), routeScheduleDTO.getBusCategory(), "D");
							
							
							originBusList = busesAssignedForAbbreviationService.getAllBusNoForFixedBusesWithoutAssignedNTCBuses(routeScheduleDTO.getRouteNo(),
									routeScheduleDTO.getBusCategory(), "O", routeScheduleDTO.getGeneratedRefNo(),
									routeScheduleDTO.getGroupNo());
							destinationBusList = busesAssignedForAbbreviationService.getAllBusNoForFixedBusesWithoutAssignedNTCBuses(
									routeScheduleDTO.getRouteNo(), routeScheduleDTO.getBusCategory(), "D", routeScheduleDTO.getGeneratedRefNo(),
									routeScheduleDTO.getGroupNo());

							/** origin add cancel buses start **/
							originCancelBuses = new ArrayList<TimeTableDTO>();
							for (int i = 0; i < originBusList.size(); i++) {
								TimeTableDTO dto = new TimeTableDTO();
								int count = i + 1;
								dto.setAssigneBusNo("EX-O-" + count);
								originCancelBuses.add(dto);
							}
							originBusList.addAll(originCancelBuses);
							/** origin add cancel buses end **/
							/** destination add cancel buses start **/
							destinationCancelBuses = new ArrayList<TimeTableDTO>();
							for (int i = 0; i < destinationBusList.size(); i++) {
								TimeTableDTO dto = new TimeTableDTO();
								int count = i + 1;
								dto.setAssigneBusNo("EX-D-" + count);
								destinationCancelBuses.add(dto);
							}
							destinationBusList.addAll(destinationCancelBuses);
							/** destination add cancel buses end **/

							if (tripType.equalsIgnoreCase("O")) {
								originDataTblDisable = true;
								
								int totalTrips = routeScheduleService.getFixedOrWithoutFixedTripsCount(
										routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), "O", "N");
								int fixedTrips = routeScheduleService.getFixedOrWithoutFixedTripsCount(
										routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), "O", "Y");
								totalTrips = totalTrips + fixedTrips;
								busNoManagerNormal(totalTrips);
							}
							if (tripType.equalsIgnoreCase("D")) {
								originDataTblDisable = false;
								
								int totalTrips = routeScheduleService.getFixedOrWithoutFixedTripsCount(
										routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), "D", "N");
								int fixedTrips = routeScheduleService.getFixedOrWithoutFixedTripsCount(
										routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), "D", "Y");
								totalTrips = totalTrips + fixedTrips;
								busNoManagerNormal(totalTrips);
							}
						}

						abbreviationOriginList = new ArrayList<RouteScheduleDTO>();
						abbreviationOriginList = busesAssignedForAbbreviationService.getBusNoListWithSelectedBuses(null,
								routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), "O",
								routeScheduleDTO.getRouteNo(), routeScheduleDTO.getBusCategory());

						abbreviationDestinationList = new ArrayList<RouteScheduleDTO>();
						abbreviationDestinationList = busesAssignedForAbbreviationService.getBusNoListWithSelectedBuses(
								null, routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), "D",
								routeScheduleDTO.getRouteNo(), routeScheduleDTO.getBusCategory());

						if (tripType.equalsIgnoreCase("O")) {
							
							dList = busesAssignedForAbbreviationService.getAssignedBuses(null,
									routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), "O",
									routeScheduleDTO.getRouteNo(), routeScheduleDTO.getBusCategory());

							for (int i = 0; i < abbreviationDestinationList.size(); i++) {
								for (RouteScheduleDTO dto : dList) {
									if (abbreviationDestinationList.get(i).getBusNo()
											.equalsIgnoreCase(dto.getBusNo())) {
										abbreviationDestinationList.get(i).setSelectedBusNum(dto.getSelectedBusNum());
									}
								}
							}
						}

						if (tripType.equalsIgnoreCase("D")) {
							List<RouteScheduleDTO> dList = new ArrayList<>();
							dList = busesAssignedForAbbreviationService.getAssignedBuses(null,
									routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), "D",
									routeScheduleDTO.getRouteNo(), routeScheduleDTO.getBusCategory());

							for (int i = 0; i < abbreviationOriginList.size(); i++) {
								for (RouteScheduleDTO dto : dList) {
									if (abbreviationOriginList.get(i).getBusNo().equalsIgnoreCase(dto.getBusNo())) {
										abbreviationOriginList.get(i).setSelectedBusNum(dto.getSelectedBusNum());
									}
								}
							}
						}

						/**
						 * if D to O get all D buses and assigned busses and
						 * compare and put assigned buses to origin list
						 * selected buses start
						 **/
						if (tripType.equalsIgnoreCase("D")) {
							}
						/**
						 * if D to O get all D buses and assigned busses and
						 * compare and put assigned buses to origin list
						 * selected buses end
						 **/

						/**
						 * if O to D get all O buses and assigned busses and
						 * compare and put assigned buses to destination list
						 * selected buses start
						 **/
						if (tripType.equalsIgnoreCase("O")) {
							}
						/**
						 * if O to D get all O buses and assigned busses and
						 * compare and put assigned buses to destination list
						 * selected buses end
						 **/

						noOfDaysFortimeTable = (int) calculateNoOfDays();

						
						createMainDataTable(dList);
						createLeavesDataTable();

						tempApplyAction();

						abbreviationOriginList = new ArrayList<RouteScheduleDTO>();
						abbreviationOriginList = busesAssignedForAbbreviationService.getBusNoListWithSelectedBuses(null,
								routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), "O",
								routeScheduleDTO.getRouteNo(), routeScheduleDTO.getBusCategory());

						abbreviationDestinationList = new ArrayList<RouteScheduleDTO>();
						abbreviationDestinationList = busesAssignedForAbbreviationService.getBusNoListWithSelectedBuses(
								null, routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), "D",
								routeScheduleDTO.getRouteNo(), routeScheduleDTO.getBusCategory());

						if (tripType.equalsIgnoreCase("O")) {
							List<RouteScheduleDTO> dList = new ArrayList<>();
							dList = busesAssignedForAbbreviationService.getAssignedBuses(null,
									routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), "O",
									routeScheduleDTO.getRouteNo(), routeScheduleDTO.getBusCategory());

							for (int i = 0; i < abbreviationDestinationList.size(); i++) {
								for (RouteScheduleDTO dto : dList) {
									if (abbreviationDestinationList.get(i).getBusNo()
											.equalsIgnoreCase(dto.getBusNo())) {
										abbreviationDestinationList.get(i).setSelectedBusNum(dto.getSelectedBusNum());
									}
								}
							}
						}

						if (tripType.equalsIgnoreCase("D")) {
							
							dList = busesAssignedForAbbreviationService.getAssignedBuses(null,
									routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), "D",
									routeScheduleDTO.getRouteNo(), routeScheduleDTO.getBusCategory());

							for (int i = 0; i < abbreviationOriginList.size(); i++) {
								for (RouteScheduleDTO dto : dList) {
									if (abbreviationOriginList.get(i).getBusNo().equalsIgnoreCase(dto.getBusNo())) {
										abbreviationOriginList.get(i).setSelectedBusNum(dto.getSelectedBusNum());
									}
								}
							}
						}
					}
					
					disableApplyBtn = false;
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

	public void tempApplyAction() {

		List<RouteScheduleDTO> tempBusForRouteList = new ArrayList<RouteScheduleDTO>();
		for (RouteScheduleDTO dto : busForRouteList) {
			tempBusForRouteList.add(dto);
		}

		List<RouteScheduleDTO> tempLeaveBusForRouteList = new ArrayList<RouteScheduleDTO>();
		for (RouteScheduleDTO dto : leaveForRouteList) {
			tempLeaveBusForRouteList.add(dto);
		}

		List<RouteScheduleDTO> tempAbbreviationOriginList = new ArrayList<RouteScheduleDTO>();
		for (RouteScheduleDTO dto : abbreviationOriginList) {
			tempAbbreviationOriginList.add(dto);
		}

		/** main table start **/
		boolean cancelBus = false;
		for (RouteScheduleDTO dto : abbreviationOriginList) {
			cancelBus = false;

			if (dto.getSelectedBusNum() != null && !dto.getSelectedBusNum().isEmpty()
					&& !dto.getSelectedBusNum().trim().equals("")) {
				for (TimeTableDTO dtoO : originCancelBuses) {
					if (dtoO.getAssigneBusNo().equalsIgnoreCase(dto.getSelectedBusNum())) {
						cancelBus = true;
					}
				}
				if (dto.getSelectedBusNum().contains("-") && !cancelBus) {
					String[] arr = dto.getSelectedBusNum().split("-");
					String busNo = "";
					boolean first = false;
					if (arr.length > 2) {
						for (int i = 0; i < arr.length - 1; i++) {
							if (!first) {
								first = true;
								busNo = arr[i];
							} else {
								busNo = busNo + "-" + arr[i];
							}
						}
					} else if (arr.length == 2) {
						busNo = arr[0];
					}
					dto.setSelectedBusNum(busNo);
				}
				if (dto.getSelectedBusNum() != null && !dto.getSelectedBusNum().isEmpty()
						&& !dto.getSelectedBusNum().trim().equalsIgnoreCase("")
						&& !dto.getSelectedBusNum().equalsIgnoreCase("null")) {
					for (RouteScheduleDTO dto2 : busForRouteList) {
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo1()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo1()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo1(dto.getSelectedBusNum());
							routeDto.setFormerBusNo1(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo2()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo2()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo2(dto.getSelectedBusNum());
							routeDto.setFormerBusNo2(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo3()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo3()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo3(dto.getSelectedBusNum());
							routeDto.setFormerBusNo3(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo4()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo4()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo4(dto.getSelectedBusNum());
							routeDto.setFormerBusNo4(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo5()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo5()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo5(dto.getSelectedBusNum());
							routeDto.setFormerBusNo5(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo6()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo6()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo6(dto.getSelectedBusNum());
							routeDto.setFormerBusNo6(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo7()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo7()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo7(dto.getSelectedBusNum());
							routeDto.setFormerBusNo7(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo8()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo8()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo8(dto.getSelectedBusNum());
							routeDto.setFormerBusNo8(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo9()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo9()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo9(dto.getSelectedBusNum());
							routeDto.setFormerBusNo9(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo10()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo10()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo10(dto.getSelectedBusNum());
							routeDto.setFormerBusNo10(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo11()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo11()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo11(dto.getSelectedBusNum());
							routeDto.setFormerBusNo11(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo12()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo12()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo12(dto.getSelectedBusNum());
							routeDto.setFormerBusNo12(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo13()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo13()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo13(dto.getSelectedBusNum());
							routeDto.setFormerBusNo13(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo14()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo14()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo14(dto.getSelectedBusNum());
							routeDto.setFormerBusNo14(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo15()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo15()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo15(dto.getSelectedBusNum());
							routeDto.setFormerBusNo15(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo16()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo16()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo16(dto.getSelectedBusNum());
							routeDto.setFormerBusNo16(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo17()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo17()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo17(dto.getSelectedBusNum());
							routeDto.setFormerBusNo17(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo18()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo18()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo18(dto.getSelectedBusNum());
							routeDto.setFormerBusNo18(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo19()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo19()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo19(dto.getSelectedBusNum());
							routeDto.setFormerBusNo19(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo20()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo20()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();

							routeDto = dto2;
							routeDto.setBusNo20(dto.getSelectedBusNum());
							routeDto.setFormerBusNo20(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo21()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo21()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo21(dto.getSelectedBusNum());
							routeDto.setFormerBusNo21(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo22()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo22()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo22(dto.getSelectedBusNum());
							routeDto.setFormerBusNo22(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo23()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo23()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo23(dto.getSelectedBusNum());
							routeDto.setFormerBusNo23(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo24()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo24()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo24(dto.getSelectedBusNum());
							routeDto.setFormerBusNo24(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo25()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo25()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo25(dto.getSelectedBusNum());
							routeDto.setFormerBusNo25(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo26()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo26()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo26(dto.getSelectedBusNum());
							routeDto.setFormerBusNo26(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo27()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo27()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo27(dto.getSelectedBusNum());
							routeDto.setFormerBusNo27(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo28()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo28()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo28(dto.getSelectedBusNum());
							routeDto.setFormerBusNo28(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo29()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo29()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo29(dto.getSelectedBusNum());
							routeDto.setFormerBusNo29(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo30()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo30()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo30(dto.getSelectedBusNum());
							routeDto.setFormerBusNo30(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo31()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo31()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo31(dto.getSelectedBusNum());
							routeDto.setFormerBusNo31(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo32()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo32()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo32(dto.getSelectedBusNum());
							routeDto.setFormerBusNo32(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo33()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo33()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo33(dto.getSelectedBusNum());
							routeDto.setFormerBusNo33(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo34()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo34()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo34(dto.getSelectedBusNum());
							routeDto.setFormerBusNo34(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo35()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo35()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo35(dto.getSelectedBusNum());
							routeDto.setFormerBusNo35(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo36()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo36()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo36(dto.getSelectedBusNum());
							routeDto.setFormerBusNo36(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo37()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo37()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo37(dto.getSelectedBusNum());
							routeDto.setFormerBusNo37(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo38()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo38()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo38(dto.getSelectedBusNum());
							routeDto.setFormerBusNo38(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo39()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo39()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo39(dto.getSelectedBusNum());
							routeDto.setFormerBusNo39(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo40()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo40()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo40(dto.getSelectedBusNum());
							routeDto.setFormerBusNo40(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo41()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo41()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo41(dto.getSelectedBusNum());
							routeDto.setFormerBusNo41(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo42()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo42()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo42(dto.getSelectedBusNum());
							routeDto.setFormerBusNo42(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo43()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo43()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo43(dto.getSelectedBusNum());
							routeDto.setFormerBusNo43(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo44()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo44()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo44(dto.getSelectedBusNum());
							routeDto.setFormerBusNo44(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo45()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo45()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo45(dto.getSelectedBusNum());
							routeDto.setFormerBusNo45(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo46()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo46()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo46(dto.getSelectedBusNum());
							routeDto.setFormerBusNo46(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo47()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo47()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo47(dto.getSelectedBusNum());
							routeDto.setFormerBusNo47(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo48()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo48()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo48(dto.getSelectedBusNum());
							routeDto.setFormerBusNo48(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo49()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo49()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo49(dto.getSelectedBusNum());
							routeDto.setFormerBusNo49(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo50()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo50()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo50(dto.getSelectedBusNum());
							routeDto.setFormerBusNo50(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo51()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo51()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo51(dto.getSelectedBusNum());
							routeDto.setFormerBusNo51(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo52()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo52()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo52(dto.getSelectedBusNum());
							routeDto.setFormerBusNo52(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo53()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo53()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo53(dto.getSelectedBusNum());
							routeDto.setFormerBusNo53(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo54()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo54()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo54(dto.getSelectedBusNum());
							routeDto.setFormerBusNo54(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo55()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo55()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo55(dto.getSelectedBusNum());
							routeDto.setFormerBusNo55(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo56()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo56()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo56(dto.getSelectedBusNum());
							routeDto.setFormerBusNo56(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo57()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo57()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo57(dto.getSelectedBusNum());
							routeDto.setFormerBusNo57(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo58()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo58()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo58(dto.getSelectedBusNum());
							routeDto.setFormerBusNo58(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo59()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo59()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo59(dto.getSelectedBusNum());
							routeDto.setFormerBusNo59(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo60()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo60()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo60(dto.getSelectedBusNum());
							routeDto.setFormerBusNo60(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo61()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo61()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo61(dto.getSelectedBusNum());
							routeDto.setFormerBusNo61(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo62()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo62()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo62(dto.getSelectedBusNum());
							routeDto.setFormerBusNo62(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo63()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo63()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo63(dto.getSelectedBusNum());
							routeDto.setFormerBusNo63(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo64()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo64()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo64(dto.getSelectedBusNum());
							routeDto.setFormerBusNo64(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo65()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo65()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo65(dto.getSelectedBusNum());
							routeDto.setFormerBusNo65(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo66()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo66()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo66(dto.getSelectedBusNum());
							routeDto.setFormerBusNo66(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo67()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo67()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo67(dto.getSelectedBusNum());
							routeDto.setFormerBusNo67(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo68()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo68()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo68(dto.getSelectedBusNum());
							routeDto.setFormerBusNo68(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo69()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo69()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo69(dto.getSelectedBusNum());
							routeDto.setFormerBusNo69(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo70()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo70()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo70(dto.getSelectedBusNum());
							routeDto.setFormerBusNo70(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo71()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo71()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo71(dto.getSelectedBusNum());
							routeDto.setFormerBusNo71(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo72()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo72()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo72(dto.getSelectedBusNum());
							routeDto.setFormerBusNo72(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo73()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo73()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo73(dto.getSelectedBusNum());
							routeDto.setFormerBusNo73(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo74()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo74()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo74(dto.getSelectedBusNum());
							routeDto.setFormerBusNo74(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo75()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo75()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo75(dto.getSelectedBusNum());
							routeDto.setFormerBusNo75(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo76()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo76()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo76(dto.getSelectedBusNum());
							routeDto.setFormerBusNo76(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo77()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo77()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo77(dto.getSelectedBusNum());
							routeDto.setFormerBusNo77(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo78()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo78()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo78(dto.getSelectedBusNum());
							routeDto.setFormerBusNo78(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo79()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo79()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo79(dto.getSelectedBusNum());
							routeDto.setFormerBusNo79(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo80()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo80()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo80(dto.getSelectedBusNum());
							routeDto.setFormerBusNo80(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo81()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo81()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo81(dto.getSelectedBusNum());
							routeDto.setFormerBusNo81(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo82()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo82()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo82(dto.getSelectedBusNum());
							routeDto.setFormerBusNo82(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo83()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo83()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo83(dto.getSelectedBusNum());
							routeDto.setFormerBusNo83(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo84()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo84()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo84(dto.getSelectedBusNum());
							routeDto.setFormerBusNo84(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo85()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo85()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo85(dto.getSelectedBusNum());
							routeDto.setFormerBusNo85(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo86()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo86()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo86(dto.getSelectedBusNum());
							routeDto.setFormerBusNo86(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo87()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo87()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo87(dto.getSelectedBusNum());
							routeDto.setFormerBusNo87(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo88()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo88()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo88(dto.getSelectedBusNum());
							routeDto.setFormerBusNo88(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo89()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo89()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo89(dto.getSelectedBusNum());
							routeDto.setFormerBusNo89(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo90()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo90()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo90(dto.getSelectedBusNum());
							routeDto.setFormerBusNo90(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo91()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo91()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo91(dto.getSelectedBusNum());
							routeDto.setFormerBusNo91(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo92()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo92()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo92(dto.getSelectedBusNum());
							routeDto.setFormerBusNo92(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
					}
				}
			}
		}

		cancelBus = false;
		for (RouteScheduleDTO dto : abbreviationDestinationList) {
			cancelBus = false;
			if (dto.getSelectedBusNum() != null && !dto.getSelectedBusNum().isEmpty()
					&& !dto.getSelectedBusNum().trim().equals("")
					&& !dto.getSelectedBusNum().equalsIgnoreCase("null")) {
				for (TimeTableDTO dtoO : destinationCancelBuses) {
					if (dtoO.getAssigneBusNo().equalsIgnoreCase(dto.getSelectedBusNum())) {
						cancelBus = true;
					}
				}
				if (dto.getSelectedBusNum().contains("-") && !cancelBus) {
					String[] arr = dto.getSelectedBusNum().split("-");
					String busNo = "";
					boolean first = false;
					if (arr.length > 2) {
						for (int i = 0; i < arr.length - 1; i++) {
							if (!first) {
								first = true;
								busNo = arr[i];
							} else {
								busNo = busNo + "-" + arr[i];
							}
						}
					} else if (arr.length == 2) {
						busNo = arr[0];
					}
					dto.setSelectedBusNum(busNo);
				}
				if (dto.getSelectedBusNum() != null && !dto.getSelectedBusNum().isEmpty()
						&& !dto.getSelectedBusNum().trim().equals("")) {
					for (RouteScheduleDTO dto2 : busForRouteList) {
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo1()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo1()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo1(dto.getSelectedBusNum());
							routeDto.setFormerBusNo1(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo2()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo2()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo2(dto.getSelectedBusNum());
							routeDto.setFormerBusNo2(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo3()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo3()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo3(dto.getSelectedBusNum());
							routeDto.setFormerBusNo3(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo4()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo4()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo4(dto.getSelectedBusNum());
							routeDto.setFormerBusNo4(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo5()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo5()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo5(dto.getSelectedBusNum());
							routeDto.setFormerBusNo5(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo6()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo6()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo6(dto.getSelectedBusNum());
							routeDto.setFormerBusNo6(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo7()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo7()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo7(dto.getSelectedBusNum());
							routeDto.setFormerBusNo7(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo8()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo8()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo8(dto.getSelectedBusNum());
							routeDto.setFormerBusNo8(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo9()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo9()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo9(dto.getSelectedBusNum());
							routeDto.setFormerBusNo9(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo10()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo10()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo10(dto.getSelectedBusNum());
							routeDto.setFormerBusNo10(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo11()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo11()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo11(dto.getSelectedBusNum());
							routeDto.setFormerBusNo11(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo12()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo12()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo12(dto.getSelectedBusNum());
							routeDto.setFormerBusNo12(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo13()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo13()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo13(dto.getSelectedBusNum());
							routeDto.setFormerBusNo13(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo14()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo14()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo14(dto.getSelectedBusNum());
							routeDto.setFormerBusNo14(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo15()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo15()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo15(dto.getSelectedBusNum());
							routeDto.setFormerBusNo15(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo16()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo16()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo16(dto.getSelectedBusNum());
							routeDto.setFormerBusNo16(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo17()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo17()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo17(dto.getSelectedBusNum());
							routeDto.setFormerBusNo17(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo18()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo18()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo18(dto.getSelectedBusNum());
							routeDto.setFormerBusNo18(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo19()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo19()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo19(dto.getSelectedBusNum());
							routeDto.setFormerBusNo19(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo20()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo20()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();

							routeDto = dto2;
							routeDto.setBusNo20(dto.getSelectedBusNum());
							routeDto.setFormerBusNo20(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo21()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo21()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo21(dto.getSelectedBusNum());
							routeDto.setFormerBusNo21(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo22()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo22()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo22(dto.getSelectedBusNum());
							routeDto.setFormerBusNo22(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo23()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo23()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo23(dto.getSelectedBusNum());
							routeDto.setFormerBusNo23(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo24()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo24()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo24(dto.getSelectedBusNum());
							routeDto.setFormerBusNo24(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo25()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo25()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo25(dto.getSelectedBusNum());
							routeDto.setFormerBusNo25(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo26()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo26()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo26(dto.getSelectedBusNum());
							routeDto.setFormerBusNo26(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo27()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo27()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo27(dto.getSelectedBusNum());
							routeDto.setFormerBusNo27(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo28()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo28()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo28(dto.getSelectedBusNum());
							routeDto.setFormerBusNo28(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo29()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo29()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo29(dto.getSelectedBusNum());
							routeDto.setFormerBusNo29(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo30()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo30()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo30(dto.getSelectedBusNum());
							routeDto.setFormerBusNo30(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo31()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo31()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo31(dto.getSelectedBusNum());
							routeDto.setFormerBusNo31(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo32()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo32()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo32(dto.getSelectedBusNum());
							routeDto.setFormerBusNo32(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo33()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo33()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo33(dto.getSelectedBusNum());
							routeDto.setFormerBusNo33(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo34()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo34()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo34(dto.getSelectedBusNum());
							routeDto.setFormerBusNo34(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo35()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo35()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo35(dto.getSelectedBusNum());
							routeDto.setFormerBusNo35(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo36()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo36()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo36(dto.getSelectedBusNum());
							routeDto.setFormerBusNo36(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo37()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo37()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo37(dto.getSelectedBusNum());
							routeDto.setFormerBusNo37(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo38()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo38()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo38(dto.getSelectedBusNum());
							routeDto.setFormerBusNo38(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo39()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo39()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo39(dto.getSelectedBusNum());
							routeDto.setFormerBusNo39(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo40()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo40()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo40(dto.getSelectedBusNum());
							routeDto.setFormerBusNo40(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo41()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo41()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo41(dto.getSelectedBusNum());
							routeDto.setFormerBusNo41(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo42()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo42()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo42(dto.getSelectedBusNum());
							routeDto.setFormerBusNo42(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo43()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo43()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo43(dto.getSelectedBusNum());
							routeDto.setFormerBusNo43(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo44()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo44()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo44(dto.getSelectedBusNum());
							routeDto.setFormerBusNo44(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo45()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo45()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo45(dto.getSelectedBusNum());
							routeDto.setFormerBusNo45(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo46()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo46()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo46(dto.getSelectedBusNum());
							routeDto.setFormerBusNo46(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo47()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo47()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo47(dto.getSelectedBusNum());
							routeDto.setFormerBusNo47(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo48()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo48()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo48(dto.getSelectedBusNum());
							routeDto.setFormerBusNo48(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo49()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo49()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo49(dto.getSelectedBusNum());
							routeDto.setFormerBusNo49(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo50()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo50()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo50(dto.getSelectedBusNum());
							routeDto.setFormerBusNo50(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo51()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo51()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo51(dto.getSelectedBusNum());
							routeDto.setFormerBusNo51(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo52()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo52()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo52(dto.getSelectedBusNum());
							routeDto.setFormerBusNo52(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo53()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo53()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo53(dto.getSelectedBusNum());
							routeDto.setFormerBusNo53(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo54()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo54()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo54(dto.getSelectedBusNum());
							routeDto.setFormerBusNo54(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo55()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo55()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo55(dto.getSelectedBusNum());
							routeDto.setFormerBusNo55(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo56()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo56()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo56(dto.getSelectedBusNum());
							routeDto.setFormerBusNo56(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo57()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo57()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo57(dto.getSelectedBusNum());
							routeDto.setFormerBusNo57(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo58()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo58()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo58(dto.getSelectedBusNum());
							routeDto.setFormerBusNo58(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo59()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo59()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo59(dto.getSelectedBusNum());
							routeDto.setFormerBusNo59(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo60()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo60()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo60(dto.getSelectedBusNum());
							routeDto.setFormerBusNo60(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo61()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo61()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo61(dto.getSelectedBusNum());
							routeDto.setFormerBusNo61(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo62()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo62()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo62(dto.getSelectedBusNum());
							routeDto.setFormerBusNo62(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo63()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo63()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo63(dto.getSelectedBusNum());
							routeDto.setFormerBusNo63(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo64()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo64()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo64(dto.getSelectedBusNum());
							routeDto.setFormerBusNo64(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo65()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo65()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo65(dto.getSelectedBusNum());
							routeDto.setFormerBusNo65(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo66()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo66()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo66(dto.getSelectedBusNum());
							routeDto.setFormerBusNo66(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo67()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo67()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo67(dto.getSelectedBusNum());
							routeDto.setFormerBusNo67(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo68()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo68()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo68(dto.getSelectedBusNum());
							routeDto.setFormerBusNo68(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo69()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo69()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo69(dto.getSelectedBusNum());
							routeDto.setFormerBusNo69(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo70()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo70()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo70(dto.getSelectedBusNum());
							routeDto.setFormerBusNo70(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo71()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo71()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo71(dto.getSelectedBusNum());
							routeDto.setFormerBusNo71(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo72()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo72()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo72(dto.getSelectedBusNum());
							routeDto.setFormerBusNo72(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo73()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo73()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo73(dto.getSelectedBusNum());
							routeDto.setFormerBusNo73(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo74()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo74()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo74(dto.getSelectedBusNum());
							routeDto.setFormerBusNo74(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo75()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo75()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo75(dto.getSelectedBusNum());
							routeDto.setFormerBusNo75(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo76()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo76()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo76(dto.getSelectedBusNum());
							routeDto.setFormerBusNo76(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo77()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo77()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo77(dto.getSelectedBusNum());
							routeDto.setFormerBusNo77(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo78()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo78()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo78(dto.getSelectedBusNum());
							routeDto.setFormerBusNo78(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo79()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo79()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo79(dto.getSelectedBusNum());
							routeDto.setFormerBusNo79(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo80()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo80()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo80(dto.getSelectedBusNum());
							routeDto.setFormerBusNo80(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo81()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo81()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo81(dto.getSelectedBusNum());
							routeDto.setFormerBusNo81(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo82()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo82()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo82(dto.getSelectedBusNum());
							routeDto.setFormerBusNo82(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo83()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo83()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo83(dto.getSelectedBusNum());
							routeDto.setFormerBusNo83(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo84()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo84()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo84(dto.getSelectedBusNum());
							routeDto.setFormerBusNo84(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo85()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo85()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo85(dto.getSelectedBusNum());
							routeDto.setFormerBusNo85(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo86()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo86()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo86(dto.getSelectedBusNum());
							routeDto.setFormerBusNo86(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo87()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo87()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo87(dto.getSelectedBusNum());
							routeDto.setFormerBusNo87(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo88()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo88()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo88(dto.getSelectedBusNum());
							routeDto.setFormerBusNo88(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo89()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo89()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo89(dto.getSelectedBusNum());
							routeDto.setFormerBusNo89(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo90()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo90()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo90(dto.getSelectedBusNum());
							routeDto.setFormerBusNo90(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo91()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo91()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo91(dto.getSelectedBusNum());
							routeDto.setFormerBusNo91(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo92()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo92()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo92(dto.getSelectedBusNum());
							routeDto.setFormerBusNo92(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
					}
				}
			}
		}

		busForRouteList = new ArrayList<RouteScheduleDTO>();
		busForRouteList = tempBusForRouteList;

		// main table end

		/** bus route order start **/

		Collections.sort(busForRouteList, new Comparator<RouteScheduleDTO>() {
			@Override
			public int compare(RouteScheduleDTO u1, RouteScheduleDTO u2) {

				return Integer.valueOf(u2.getTripId()) - Integer.valueOf(u1.getTripId());

			}
		});

		Collections.reverse(busForRouteList);

		/** bus route order end **/

		createMainDataTable(dList);
		createLeavesDataTable();

		saveBtnDisable = true;

		/*
		 * sessionBackingBean.setMessage("Bus No. applied successfully");
		 * RequestContext.getCurrentInstance().execute(
		 * "PF('dlgCommonSuccess').show()");
		 */
	}

	public void applyButtonAction() {

		List<RouteScheduleDTO> tempBusForRouteList = new ArrayList<RouteScheduleDTO>();
		for (RouteScheduleDTO dto : busForRouteList) {
			tempBusForRouteList.add(dto);
		}

		List<RouteScheduleDTO> tempLeaveBusForRouteList = new ArrayList<RouteScheduleDTO>();
		for (RouteScheduleDTO dto : leaveForRouteList) {
			tempLeaveBusForRouteList.add(dto);
		}

		List<RouteScheduleDTO> tempAbbreviationOriginList = new ArrayList<RouteScheduleDTO>();
		for (RouteScheduleDTO dto : abbreviationOriginList) {
			tempAbbreviationOriginList.add(dto);
		}

		/** main table start **/
		boolean cancelBus = false;
		for (RouteScheduleDTO dto : abbreviationOriginList) {
			cancelBus = false;

			if (dto.getSelectedBusNum() != null && !dto.getSelectedBusNum().isEmpty()
					&& !dto.getSelectedBusNum().trim().equals("")) {
				for (TimeTableDTO dtoO : originCancelBuses) {
					if (dtoO.getAssigneBusNo().equalsIgnoreCase(dto.getSelectedBusNum())) {
						cancelBus = true;
					}
				}
				if (dto.getSelectedBusNum().contains("-") && !cancelBus) {
					String[] arr = dto.getSelectedBusNum().split("-");
					String busNo = "";
					boolean first = false;
					if (arr.length > 2) {
						for (int i = 0; i < arr.length - 1; i++) {
							if (!first) {
								first = true;
								busNo = arr[i];
							} else {
								busNo = busNo + "-" + arr[i];
							}
						}
					} else if (arr.length == 2) {
						busNo = arr[0];
					}
					dto.setSelectedBusNum(busNo);
				}
				if (dto.getSelectedBusNum() != null && !dto.getSelectedBusNum().isEmpty()
						&& !dto.getSelectedBusNum().trim().equalsIgnoreCase("")) {
					for (RouteScheduleDTO dto2 : busForRouteList) {
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo1()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo1()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo1(dto.getSelectedBusNum());
							routeDto.setFormerBusNo1(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo2()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo2()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo2(dto.getSelectedBusNum());
							routeDto.setFormerBusNo2(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo3()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo3()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo3(dto.getSelectedBusNum());
							routeDto.setFormerBusNo3(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo4()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo4()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo4(dto.getSelectedBusNum());
							routeDto.setFormerBusNo4(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo5()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo5()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo5(dto.getSelectedBusNum());
							routeDto.setFormerBusNo5(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo6()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo6()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo6(dto.getSelectedBusNum());
							routeDto.setFormerBusNo6(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo7()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo7()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo7(dto.getSelectedBusNum());
							routeDto.setFormerBusNo7(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo8()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo8()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo8(dto.getSelectedBusNum());
							routeDto.setFormerBusNo8(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo9()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo9()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo9(dto.getSelectedBusNum());
							routeDto.setFormerBusNo9(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo10()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo10()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo10(dto.getSelectedBusNum());
							routeDto.setFormerBusNo10(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo11()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo11()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo11(dto.getSelectedBusNum());
							routeDto.setFormerBusNo11(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo12()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo12()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo12(dto.getSelectedBusNum());
							routeDto.setFormerBusNo12(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo13()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo13()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo13(dto.getSelectedBusNum());
							routeDto.setFormerBusNo13(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo14()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo14()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo14(dto.getSelectedBusNum());
							routeDto.setFormerBusNo14(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo15()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo15()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo15(dto.getSelectedBusNum());
							routeDto.setFormerBusNo15(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo16()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo16()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo16(dto.getSelectedBusNum());
							routeDto.setFormerBusNo16(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo17()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo17()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo17(dto.getSelectedBusNum());
							routeDto.setFormerBusNo17(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo18()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo18()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo18(dto.getSelectedBusNum());
							routeDto.setFormerBusNo18(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo19()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo19()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo19(dto.getSelectedBusNum());
							routeDto.setFormerBusNo19(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo20()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo20()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();

							routeDto = dto2;
							routeDto.setBusNo20(dto.getSelectedBusNum());
							routeDto.setFormerBusNo20(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo21()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo21()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo21(dto.getSelectedBusNum());
							routeDto.setFormerBusNo21(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo22()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo22()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo22(dto.getSelectedBusNum());
							routeDto.setFormerBusNo22(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo23()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo23()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo23(dto.getSelectedBusNum());
							routeDto.setFormerBusNo23(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo24()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo24()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo24(dto.getSelectedBusNum());
							routeDto.setFormerBusNo24(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo25()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo25()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo25(dto.getSelectedBusNum());
							routeDto.setFormerBusNo25(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo26()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo26()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo26(dto.getSelectedBusNum());
							routeDto.setFormerBusNo26(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo27()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo27()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo27(dto.getSelectedBusNum());
							routeDto.setFormerBusNo27(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo28()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo28()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo28(dto.getSelectedBusNum());
							routeDto.setFormerBusNo28(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo29()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo29()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo29(dto.getSelectedBusNum());
							routeDto.setFormerBusNo29(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo30()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo30()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo30(dto.getSelectedBusNum());
							routeDto.setFormerBusNo30(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo31()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo31()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo31(dto.getSelectedBusNum());
							routeDto.setFormerBusNo31(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo32()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo32()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo32(dto.getSelectedBusNum());
							routeDto.setFormerBusNo32(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo33()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo33()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo33(dto.getSelectedBusNum());
							routeDto.setFormerBusNo33(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo34()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo34()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo34(dto.getSelectedBusNum());
							routeDto.setFormerBusNo34(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo35()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo35()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo35(dto.getSelectedBusNum());
							routeDto.setFormerBusNo35(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo36()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo36()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo36(dto.getSelectedBusNum());
							routeDto.setFormerBusNo36(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo37()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo37()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo37(dto.getSelectedBusNum());
							routeDto.setFormerBusNo37(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo38()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo38()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo38(dto.getSelectedBusNum());
							routeDto.setFormerBusNo38(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo39()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo39()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo39(dto.getSelectedBusNum());
							routeDto.setFormerBusNo39(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo40()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo40()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo40(dto.getSelectedBusNum());
							routeDto.setFormerBusNo40(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo41()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo41()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo41(dto.getSelectedBusNum());
							routeDto.setFormerBusNo41(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo42()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo42()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo42(dto.getSelectedBusNum());
							routeDto.setFormerBusNo42(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo43()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo43()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo43(dto.getSelectedBusNum());
							routeDto.setFormerBusNo43(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo44()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo44()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo44(dto.getSelectedBusNum());
							routeDto.setFormerBusNo44(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo45()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo45()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo45(dto.getSelectedBusNum());
							routeDto.setFormerBusNo45(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo46()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo46()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo46(dto.getSelectedBusNum());
							routeDto.setFormerBusNo46(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo47()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo47()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo47(dto.getSelectedBusNum());
							routeDto.setFormerBusNo47(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo48()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo48()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo48(dto.getSelectedBusNum());
							routeDto.setFormerBusNo48(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo49()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo49()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo49(dto.getSelectedBusNum());
							routeDto.setFormerBusNo49(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo50()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo50()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo50(dto.getSelectedBusNum());
							routeDto.setFormerBusNo50(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo51()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo51()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo51(dto.getSelectedBusNum());
							routeDto.setFormerBusNo51(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo52()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo52()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo52(dto.getSelectedBusNum());
							routeDto.setFormerBusNo52(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo53()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo53()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo53(dto.getSelectedBusNum());
							routeDto.setFormerBusNo53(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo54()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo54()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo54(dto.getSelectedBusNum());
							routeDto.setFormerBusNo54(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo55()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo55()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo55(dto.getSelectedBusNum());
							routeDto.setFormerBusNo55(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo56()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo56()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo56(dto.getSelectedBusNum());
							routeDto.setFormerBusNo56(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo57()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo57()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo57(dto.getSelectedBusNum());
							routeDto.setFormerBusNo57(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo58()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo58()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo58(dto.getSelectedBusNum());
							routeDto.setFormerBusNo58(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo59()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo59()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo59(dto.getSelectedBusNum());
							routeDto.setFormerBusNo59(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo60()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo60()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo60(dto.getSelectedBusNum());
							routeDto.setFormerBusNo60(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo61()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo61()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo61(dto.getSelectedBusNum());
							routeDto.setFormerBusNo61(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo62()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo62()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo62(dto.getSelectedBusNum());
							routeDto.setFormerBusNo62(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo63()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo63()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo63(dto.getSelectedBusNum());
							routeDto.setFormerBusNo63(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo64()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo64()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo64(dto.getSelectedBusNum());
							routeDto.setFormerBusNo64(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo65()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo65()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo65(dto.getSelectedBusNum());
							routeDto.setFormerBusNo65(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo66()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo66()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo66(dto.getSelectedBusNum());
							routeDto.setFormerBusNo66(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo67()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo67()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo67(dto.getSelectedBusNum());
							routeDto.setFormerBusNo67(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo68()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo68()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo68(dto.getSelectedBusNum());
							routeDto.setFormerBusNo68(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo69()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo69()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo69(dto.getSelectedBusNum());
							routeDto.setFormerBusNo69(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo70()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo70()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo70(dto.getSelectedBusNum());
							routeDto.setFormerBusNo70(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo71()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo71()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo71(dto.getSelectedBusNum());
							routeDto.setFormerBusNo71(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo72()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo72()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo72(dto.getSelectedBusNum());
							routeDto.setFormerBusNo72(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo73()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo73()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo73(dto.getSelectedBusNum());
							routeDto.setFormerBusNo73(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo74()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo74()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo74(dto.getSelectedBusNum());
							routeDto.setFormerBusNo74(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo75()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo75()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo75(dto.getSelectedBusNum());
							routeDto.setFormerBusNo75(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo76()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo76()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo76(dto.getSelectedBusNum());
							routeDto.setFormerBusNo76(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo77()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo77()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo77(dto.getSelectedBusNum());
							routeDto.setFormerBusNo77(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo78()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo78()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo78(dto.getSelectedBusNum());
							routeDto.setFormerBusNo78(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo79()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo79()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo79(dto.getSelectedBusNum());
							routeDto.setFormerBusNo79(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo80()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo80()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo80(dto.getSelectedBusNum());
							routeDto.setFormerBusNo80(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo81()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo81()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo81(dto.getSelectedBusNum());
							routeDto.setFormerBusNo81(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo82()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo82()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo82(dto.getSelectedBusNum());
							routeDto.setFormerBusNo82(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo83()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo83()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo83(dto.getSelectedBusNum());
							routeDto.setFormerBusNo83(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo84()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo84()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo84(dto.getSelectedBusNum());
							routeDto.setFormerBusNo84(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo85()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo85()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo85(dto.getSelectedBusNum());
							routeDto.setFormerBusNo85(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo86()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo86()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo86(dto.getSelectedBusNum());
							routeDto.setFormerBusNo86(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo87()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo87()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo87(dto.getSelectedBusNum());
							routeDto.setFormerBusNo87(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo88()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo88()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo88(dto.getSelectedBusNum());
							routeDto.setFormerBusNo88(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo89()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo89()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo89(dto.getSelectedBusNum());
							routeDto.setFormerBusNo89(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo90()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo90()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo90(dto.getSelectedBusNum());
							routeDto.setFormerBusNo90(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo91()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo91()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo91(dto.getSelectedBusNum());
							routeDto.setFormerBusNo91(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo92()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo92()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo92(dto.getSelectedBusNum());
							routeDto.setFormerBusNo92(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
					}
				}
			}
		}

		cancelBus = false;
		for (RouteScheduleDTO dto : abbreviationDestinationList) {
			cancelBus = false;
			if (dto.getSelectedBusNum() != null && !dto.getSelectedBusNum().isEmpty()
					&& !dto.getSelectedBusNum().trim().equals("")) {
				for (TimeTableDTO dtoO : destinationCancelBuses) {
					if (dtoO.getAssigneBusNo().equalsIgnoreCase(dto.getSelectedBusNum())) {
						cancelBus = true;
					}
				}
				if (dto.getSelectedBusNum().contains("-") && !cancelBus) {
					String[] arr = dto.getSelectedBusNum().split("-");
					String busNo = "";
					boolean first = false;
					if (arr.length > 2) {
						for (int i = 0; i < arr.length - 1; i++) {
							if (!first) {
								first = true;
								busNo = arr[i];
							} else {
								busNo = busNo + "-" + arr[i];
							}
						}
					} else if (arr.length == 2) {
						busNo = arr[0];
					}
					dto.setSelectedBusNum(busNo);
				}
				if (dto.getSelectedBusNum() != null && !dto.getSelectedBusNum().isEmpty()
						&& !dto.getSelectedBusNum().trim().equals("")) {
					for (RouteScheduleDTO dto2 : busForRouteList) {
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo1()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo1()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo1(dto.getSelectedBusNum());
							routeDto.setFormerBusNo1(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo2()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo2()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo2(dto.getSelectedBusNum());
							routeDto.setFormerBusNo2(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo3()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo3()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo3(dto.getSelectedBusNum());
							routeDto.setFormerBusNo3(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo4()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo4()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo4(dto.getSelectedBusNum());
							routeDto.setFormerBusNo4(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo5()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo5()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo5(dto.getSelectedBusNum());
							routeDto.setFormerBusNo5(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo6()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo6()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo6(dto.getSelectedBusNum());
							routeDto.setFormerBusNo6(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo7()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo7()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo7(dto.getSelectedBusNum());
							routeDto.setFormerBusNo7(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo8()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo8()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo8(dto.getSelectedBusNum());
							routeDto.setFormerBusNo8(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo9()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo9()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo9(dto.getSelectedBusNum());
							routeDto.setFormerBusNo9(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo10()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo10()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo10(dto.getSelectedBusNum());
							routeDto.setFormerBusNo10(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo11()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo11()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo11(dto.getSelectedBusNum());
							routeDto.setFormerBusNo11(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo12()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo12()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo12(dto.getSelectedBusNum());
							routeDto.setFormerBusNo12(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo13()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo13()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo13(dto.getSelectedBusNum());
							routeDto.setFormerBusNo13(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo14()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo14()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo14(dto.getSelectedBusNum());
							routeDto.setFormerBusNo14(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo15()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo15()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo15(dto.getSelectedBusNum());
							routeDto.setFormerBusNo15(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo16()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo16()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo16(dto.getSelectedBusNum());
							routeDto.setFormerBusNo16(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo17()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo17()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo17(dto.getSelectedBusNum());
							routeDto.setFormerBusNo17(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo18()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo18()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo18(dto.getSelectedBusNum());
							routeDto.setFormerBusNo18(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo19()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo19()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo19(dto.getSelectedBusNum());
							routeDto.setFormerBusNo19(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo20()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo20()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();

							routeDto = dto2;
							routeDto.setBusNo20(dto.getSelectedBusNum());
							routeDto.setFormerBusNo20(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo21()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo21()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo21(dto.getSelectedBusNum());
							routeDto.setFormerBusNo21(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo22()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo22()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo22(dto.getSelectedBusNum());
							routeDto.setFormerBusNo22(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo23()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo23()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo23(dto.getSelectedBusNum());
							routeDto.setFormerBusNo23(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo24()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo24()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo24(dto.getSelectedBusNum());
							routeDto.setFormerBusNo24(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo25()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo25()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo25(dto.getSelectedBusNum());
							routeDto.setFormerBusNo25(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo26()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo26()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo26(dto.getSelectedBusNum());
							routeDto.setFormerBusNo26(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo27()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo27()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo27(dto.getSelectedBusNum());
							routeDto.setFormerBusNo27(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo28()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo28()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo28(dto.getSelectedBusNum());
							routeDto.setFormerBusNo28(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo29()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo29()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo29(dto.getSelectedBusNum());
							routeDto.setFormerBusNo29(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo30()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo30()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo30(dto.getSelectedBusNum());
							routeDto.setFormerBusNo30(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo31()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo31()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo31(dto.getSelectedBusNum());
							routeDto.setFormerBusNo31(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo32()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo32()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo32(dto.getSelectedBusNum());
							routeDto.setFormerBusNo32(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo33()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo33()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo33(dto.getSelectedBusNum());
							routeDto.setFormerBusNo33(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo34()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo34()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo34(dto.getSelectedBusNum());
							routeDto.setFormerBusNo34(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo35()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo35()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo35(dto.getSelectedBusNum());
							routeDto.setFormerBusNo35(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo36()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo36()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo36(dto.getSelectedBusNum());
							routeDto.setFormerBusNo36(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo37()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo37()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo37(dto.getSelectedBusNum());
							routeDto.setFormerBusNo37(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo38()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo38()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo38(dto.getSelectedBusNum());
							routeDto.setFormerBusNo38(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo39()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo39()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo39(dto.getSelectedBusNum());
							routeDto.setFormerBusNo39(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo40()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo40()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo40(dto.getSelectedBusNum());
							routeDto.setFormerBusNo40(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo41()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo41()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo41(dto.getSelectedBusNum());
							routeDto.setFormerBusNo41(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo42()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo42()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo42(dto.getSelectedBusNum());
							routeDto.setFormerBusNo42(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo43()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo43()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo43(dto.getSelectedBusNum());
							routeDto.setFormerBusNo43(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo44()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo44()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo44(dto.getSelectedBusNum());
							routeDto.setFormerBusNo44(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo45()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo45()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo45(dto.getSelectedBusNum());
							routeDto.setFormerBusNo45(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo46()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo46()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo46(dto.getSelectedBusNum());
							routeDto.setFormerBusNo46(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo47()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo47()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo47(dto.getSelectedBusNum());
							routeDto.setFormerBusNo47(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo48()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo48()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo48(dto.getSelectedBusNum());
							routeDto.setFormerBusNo48(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo49()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo49()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo49(dto.getSelectedBusNum());
							routeDto.setFormerBusNo49(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo50()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo50()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo50(dto.getSelectedBusNum());
							routeDto.setFormerBusNo50(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo51()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo51()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo51(dto.getSelectedBusNum());
							routeDto.setFormerBusNo51(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo52()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo52()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo52(dto.getSelectedBusNum());
							routeDto.setFormerBusNo52(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo53()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo53()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo53(dto.getSelectedBusNum());
							routeDto.setFormerBusNo53(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo54()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo54()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo54(dto.getSelectedBusNum());
							routeDto.setFormerBusNo54(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo55()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo55()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo55(dto.getSelectedBusNum());
							routeDto.setFormerBusNo55(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo56()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo56()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo56(dto.getSelectedBusNum());
							routeDto.setFormerBusNo56(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo57()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo57()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo57(dto.getSelectedBusNum());
							routeDto.setFormerBusNo57(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo58()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo58()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo58(dto.getSelectedBusNum());
							routeDto.setFormerBusNo58(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo59()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo59()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo59(dto.getSelectedBusNum());
							routeDto.setFormerBusNo59(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo60()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo60()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo60(dto.getSelectedBusNum());
							routeDto.setFormerBusNo60(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo61()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo61()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo61(dto.getSelectedBusNum());
							routeDto.setFormerBusNo61(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo62()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo62()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo62(dto.getSelectedBusNum());
							routeDto.setFormerBusNo62(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo63()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo63()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo63(dto.getSelectedBusNum());
							routeDto.setFormerBusNo63(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo64()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo64()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo64(dto.getSelectedBusNum());
							routeDto.setFormerBusNo64(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo65()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo65()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo65(dto.getSelectedBusNum());
							routeDto.setFormerBusNo65(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo66()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo66()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo66(dto.getSelectedBusNum());
							routeDto.setFormerBusNo66(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo67()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo67()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo67(dto.getSelectedBusNum());
							routeDto.setFormerBusNo67(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo68()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo68()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo68(dto.getSelectedBusNum());
							routeDto.setFormerBusNo68(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo69()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo69()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo69(dto.getSelectedBusNum());
							routeDto.setFormerBusNo69(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo70()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo70()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo70(dto.getSelectedBusNum());
							routeDto.setFormerBusNo70(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo71()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo71()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo71(dto.getSelectedBusNum());
							routeDto.setFormerBusNo71(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo72()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo72()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo72(dto.getSelectedBusNum());
							routeDto.setFormerBusNo72(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo73()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo73()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo73(dto.getSelectedBusNum());
							routeDto.setFormerBusNo73(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo74()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo74()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo74(dto.getSelectedBusNum());
							routeDto.setFormerBusNo74(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo75()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo75()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo75(dto.getSelectedBusNum());
							routeDto.setFormerBusNo75(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo76()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo76()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo76(dto.getSelectedBusNum());
							routeDto.setFormerBusNo76(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo77()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo77()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo77(dto.getSelectedBusNum());
							routeDto.setFormerBusNo77(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo78()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo78()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo78(dto.getSelectedBusNum());
							routeDto.setFormerBusNo78(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo79()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo79()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo79(dto.getSelectedBusNum());
							routeDto.setFormerBusNo79(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo80()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo80()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo80(dto.getSelectedBusNum());
							routeDto.setFormerBusNo80(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo81()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo81()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo81(dto.getSelectedBusNum());
							routeDto.setFormerBusNo81(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo82()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo82()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo82(dto.getSelectedBusNum());
							routeDto.setFormerBusNo82(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo83()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo83()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo83(dto.getSelectedBusNum());
							routeDto.setFormerBusNo83(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo84()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo84()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo84(dto.getSelectedBusNum());
							routeDto.setFormerBusNo84(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo85()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo85()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo85(dto.getSelectedBusNum());
							routeDto.setFormerBusNo85(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo86()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo86()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo86(dto.getSelectedBusNum());
							routeDto.setFormerBusNo86(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo87()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo87()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo87(dto.getSelectedBusNum());
							routeDto.setFormerBusNo87(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo88()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo88()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo88(dto.getSelectedBusNum());
							routeDto.setFormerBusNo88(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo89()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo89()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo89(dto.getSelectedBusNum());
							routeDto.setFormerBusNo89(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo90()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo90()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo90(dto.getSelectedBusNum());
							routeDto.setFormerBusNo90(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo91()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo91()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo91(dto.getSelectedBusNum());
							routeDto.setFormerBusNo91(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
						if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
								&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo92()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo92()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = dto.getBusNo();
							routeDto = dto2;
							routeDto.setBusNo92(dto.getSelectedBusNum());
							routeDto.setFormerBusNo92(formerBus);
							
							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);
							
						}
					}
				}
			}
		}

		busForRouteList = new ArrayList<RouteScheduleDTO>();
		busForRouteList = tempBusForRouteList;

		// main table end

		// leave table start
		for (RouteScheduleDTO dto : abbreviationOriginList) {
			if (dto.getSelectedBusNum() != null && !dto.getSelectedBusNum().isEmpty()
					&& !dto.getSelectedBusNum().trim().equals("")) {
				for (RouteScheduleDTO dto2 : leaveForRouteList) {
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo1()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo1()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo1(dto.getSelectedBusNum());
						routeDto.setFormerBusNo1(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo2()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo2()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo2(dto.getSelectedBusNum());
						routeDto.setFormerBusNo2(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo3()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo3()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo3(dto.getSelectedBusNum());
						routeDto.setFormerBusNo3(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo4()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo4()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo4(dto.getSelectedBusNum());
						routeDto.setFormerBusNo4(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo5()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo5()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo5(dto.getSelectedBusNum());
						routeDto.setFormerBusNo5(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo6()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo6()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo6(dto.getSelectedBusNum());
						routeDto.setFormerBusNo6(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo7()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo7()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo7(dto.getSelectedBusNum());
						routeDto.setFormerBusNo7(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo8()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo8()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo8(dto.getSelectedBusNum());
						routeDto.setFormerBusNo8(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo9()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo9()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo9(dto.getSelectedBusNum());
						routeDto.setFormerBusNo9(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo10()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo10()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo10(dto.getSelectedBusNum());
						routeDto.setFormerBusNo10(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo11()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo11()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo11(dto.getSelectedBusNum());
						routeDto.setFormerBusNo11(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo12()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo12()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo12(dto.getSelectedBusNum());
						routeDto.setFormerBusNo12(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo13()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo13()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo13(dto.getSelectedBusNum());
						routeDto.setFormerBusNo13(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo14()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo14()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo14(dto.getSelectedBusNum());
						routeDto.setFormerBusNo14(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo15()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo15()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo15(dto.getSelectedBusNum());
						routeDto.setFormerBusNo15(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo16()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo16()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo16(dto.getSelectedBusNum());
						routeDto.setFormerBusNo16(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo17()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo17()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo17(dto.getSelectedBusNum());
						routeDto.setFormerBusNo17(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo18()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo18()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo18(dto.getSelectedBusNum());
						routeDto.setFormerBusNo18(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo19()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo19()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo19(dto.getSelectedBusNum());
						routeDto.setFormerBusNo19(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo20()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo20()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();

						routeDto = dto2;
						routeDto.setBusNo20(dto.getSelectedBusNum());
						routeDto.setFormerBusNo20(formerBus);

						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);

					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo21()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo21()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo21(dto.getSelectedBusNum());
						routeDto.setFormerBusNo21(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo22()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo22()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo22(dto.getSelectedBusNum());
						routeDto.setFormerBusNo22(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo23()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo23()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo23(dto.getSelectedBusNum());
						routeDto.setFormerBusNo23(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo24()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo24()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo24(dto.getSelectedBusNum());
						routeDto.setFormerBusNo24(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo25()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo25()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo25(dto.getSelectedBusNum());
						routeDto.setFormerBusNo25(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo26()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo26()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo26(dto.getSelectedBusNum());
						routeDto.setFormerBusNo26(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo27()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo27()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo27(dto.getSelectedBusNum());
						routeDto.setFormerBusNo27(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo28()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo28()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo28(dto.getSelectedBusNum());
						routeDto.setFormerBusNo28(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo29()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo29()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo29(dto.getSelectedBusNum());
						routeDto.setFormerBusNo29(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo30()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo30()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo30(dto.getSelectedBusNum());
						routeDto.setFormerBusNo30(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo31()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo31()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo31(dto.getSelectedBusNum());
						routeDto.setFormerBusNo31(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo32()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo32()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo32(dto.getSelectedBusNum());
						routeDto.setFormerBusNo32(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo33()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo33()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo33(dto.getSelectedBusNum());
						routeDto.setFormerBusNo33(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo34()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo34()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo34(dto.getSelectedBusNum());
						routeDto.setFormerBusNo34(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo35()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo35()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo35(dto.getSelectedBusNum());
						routeDto.setFormerBusNo35(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo36()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo36()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo36(dto.getSelectedBusNum());
						routeDto.setFormerBusNo36(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo37()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo37()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo37(dto.getSelectedBusNum());
						routeDto.setFormerBusNo37(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo38()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo38()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo38(dto.getSelectedBusNum());
						routeDto.setFormerBusNo38(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo39()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo39()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo39(dto.getSelectedBusNum());
						routeDto.setFormerBusNo39(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo40()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo40()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo40(dto.getSelectedBusNum());
						routeDto.setFormerBusNo40(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo41()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo41()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo41(dto.getSelectedBusNum());
						routeDto.setFormerBusNo41(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo42()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo42()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo42(dto.getSelectedBusNum());
						routeDto.setFormerBusNo42(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo43()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo43()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo43(dto.getSelectedBusNum());
						routeDto.setFormerBusNo43(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo44()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo44()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo44(dto.getSelectedBusNum());
						routeDto.setFormerBusNo44(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo45()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo45()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo45(dto.getSelectedBusNum());
						routeDto.setFormerBusNo45(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo46()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo46()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo46(dto.getSelectedBusNum());
						routeDto.setFormerBusNo46(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo47()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo47()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo47(dto.getSelectedBusNum());
						routeDto.setFormerBusNo47(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo48()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo48()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo48(dto.getSelectedBusNum());
						routeDto.setFormerBusNo48(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo49()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo49()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo49(dto.getSelectedBusNum());
						routeDto.setFormerBusNo49(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo50()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo50()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo50(dto.getSelectedBusNum());
						routeDto.setFormerBusNo50(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo51()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo51()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo51(dto.getSelectedBusNum());
						routeDto.setFormerBusNo51(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo52()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo52()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo52(dto.getSelectedBusNum());
						routeDto.setFormerBusNo52(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo53()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo53()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo53(dto.getSelectedBusNum());
						routeDto.setFormerBusNo53(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo54()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo54()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo54(dto.getSelectedBusNum());
						routeDto.setFormerBusNo54(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo55()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo55()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo55(dto.getSelectedBusNum());
						routeDto.setFormerBusNo55(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo56()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo56()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo56(dto.getSelectedBusNum());
						routeDto.setFormerBusNo56(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo57()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo57()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo57(dto.getSelectedBusNum());
						routeDto.setFormerBusNo57(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo58()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo58()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo58(dto.getSelectedBusNum());
						routeDto.setFormerBusNo58(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo59()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo59()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo59(dto.getSelectedBusNum());
						routeDto.setFormerBusNo59(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo60()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo60()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo60(dto.getSelectedBusNum());
						routeDto.setFormerBusNo60(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo61()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo61()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo61(dto.getSelectedBusNum());
						routeDto.setFormerBusNo61(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo62()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo62()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo62(dto.getSelectedBusNum());
						routeDto.setFormerBusNo62(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo63()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo63()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo63(dto.getSelectedBusNum());
						routeDto.setFormerBusNo63(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo64()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo64()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo64(dto.getSelectedBusNum());
						routeDto.setFormerBusNo64(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo65()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo65()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo65(dto.getSelectedBusNum());
						routeDto.setFormerBusNo65(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo66()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo66()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo66(dto.getSelectedBusNum());
						routeDto.setFormerBusNo66(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo67()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo67()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo67(dto.getSelectedBusNum());
						routeDto.setFormerBusNo67(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo68()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo68()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo68(dto.getSelectedBusNum());
						routeDto.setFormerBusNo68(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo69()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo69()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo69(dto.getSelectedBusNum());
						routeDto.setFormerBusNo69(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo70()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo70()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo70(dto.getSelectedBusNum());
						routeDto.setFormerBusNo70(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo71()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo71()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo71(dto.getSelectedBusNum());
						routeDto.setFormerBusNo71(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo72()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo72()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo72(dto.getSelectedBusNum());
						routeDto.setFormerBusNo72(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo73()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo73()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo73(dto.getSelectedBusNum());
						routeDto.setFormerBusNo73(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo74()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo74()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo74(dto.getSelectedBusNum());
						routeDto.setFormerBusNo74(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo75()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo75()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo75(dto.getSelectedBusNum());
						routeDto.setFormerBusNo75(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo76()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo76()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo76(dto.getSelectedBusNum());
						routeDto.setFormerBusNo76(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo77()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo77()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo77(dto.getSelectedBusNum());
						routeDto.setFormerBusNo77(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo78()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo78()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo78(dto.getSelectedBusNum());
						routeDto.setFormerBusNo78(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo79()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo79()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo79(dto.getSelectedBusNum());
						routeDto.setFormerBusNo79(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo80()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo80()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo80(dto.getSelectedBusNum());
						routeDto.setFormerBusNo80(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo81()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo81()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo81(dto.getSelectedBusNum());
						routeDto.setFormerBusNo81(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo82()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo82()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo82(dto.getSelectedBusNum());
						routeDto.setFormerBusNo82(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo83()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo83()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo83(dto.getSelectedBusNum());
						routeDto.setFormerBusNo83(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo84()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo84()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo84(dto.getSelectedBusNum());
						routeDto.setFormerBusNo84(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo85()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo85()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo85(dto.getSelectedBusNum());
						routeDto.setFormerBusNo85(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo86()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo86()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo86(dto.getSelectedBusNum());
						routeDto.setFormerBusNo86(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo87()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo87()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo87(dto.getSelectedBusNum());
						routeDto.setFormerBusNo87(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo88()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo88()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo88(dto.getSelectedBusNum());
						routeDto.setFormerBusNo88(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo89()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo89()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo89(dto.getSelectedBusNum());
						routeDto.setFormerBusNo89(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo90()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo90()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo90(dto.getSelectedBusNum());
						routeDto.setFormerBusNo90(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo91()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo91()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo91(dto.getSelectedBusNum());
						routeDto.setFormerBusNo91(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo92()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo92()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo92(dto.getSelectedBusNum());
						routeDto.setFormerBusNo92(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
				}
			}
		}

		for (RouteScheduleDTO dto : abbreviationDestinationList) {
			if (dto.getSelectedBusNum() != null && !dto.getSelectedBusNum().isEmpty()
					&& !dto.getSelectedBusNum().trim().equals("")) {
				for (RouteScheduleDTO dto2 : leaveForRouteList) {
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo1()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo1()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo1(dto.getSelectedBusNum());
						routeDto.setFormerBusNo1(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo2()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo2()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo2(dto.getSelectedBusNum());
						routeDto.setFormerBusNo2(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo3()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo3()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo3(dto.getSelectedBusNum());
						routeDto.setFormerBusNo3(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo4()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo4()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo4(dto.getSelectedBusNum());
						routeDto.setFormerBusNo4(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo5()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo5()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo5(dto.getSelectedBusNum());
						routeDto.setFormerBusNo5(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo6()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo6()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo6(dto.getSelectedBusNum());
						routeDto.setFormerBusNo6(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo7()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo7()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo7(dto.getSelectedBusNum());
						routeDto.setFormerBusNo7(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo8()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo8()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo8(dto.getSelectedBusNum());
						routeDto.setFormerBusNo8(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo9()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo9()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo9(dto.getSelectedBusNum());
						routeDto.setFormerBusNo9(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo10()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo10()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo10(dto.getSelectedBusNum());
						routeDto.setFormerBusNo10(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo11()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo11()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo11(dto.getSelectedBusNum());
						routeDto.setFormerBusNo11(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo12()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo12()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo12(dto.getSelectedBusNum());
						routeDto.setFormerBusNo12(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo13()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo13()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo13(dto.getSelectedBusNum());
						routeDto.setFormerBusNo13(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo14()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo14()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo14(dto.getSelectedBusNum());
						routeDto.setFormerBusNo14(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo15()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo15()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo15(dto.getSelectedBusNum());
						routeDto.setFormerBusNo15(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo16()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo16()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo16(dto.getSelectedBusNum());
						routeDto.setFormerBusNo16(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo17()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo17()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo17(dto.getSelectedBusNum());
						routeDto.setFormerBusNo17(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo18()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo18()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo18(dto.getSelectedBusNum());
						routeDto.setFormerBusNo18(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo19()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo19()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo19(dto.getSelectedBusNum());
						routeDto.setFormerBusNo19(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);

					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo20()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo20()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();

						routeDto = dto2;
						routeDto.setBusNo20(dto.getSelectedBusNum());
						routeDto.setFormerBusNo20(formerBus);

						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);

					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo21()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo21()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo21(dto.getSelectedBusNum());
						routeDto.setFormerBusNo21(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo22()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo22()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo22(dto.getSelectedBusNum());
						routeDto.setFormerBusNo22(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo23()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo23()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo23(dto.getSelectedBusNum());
						routeDto.setFormerBusNo23(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo24()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo24()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo24(dto.getSelectedBusNum());
						routeDto.setFormerBusNo24(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo25()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo25()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo25(dto.getSelectedBusNum());
						routeDto.setFormerBusNo25(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo26()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo26()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo26(dto.getSelectedBusNum());
						routeDto.setFormerBusNo26(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo27()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo27()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo27(dto.getSelectedBusNum());
						routeDto.setFormerBusNo27(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo28()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo28()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo28(dto.getSelectedBusNum());
						routeDto.setFormerBusNo28(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo29()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo29()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo29(dto.getSelectedBusNum());
						routeDto.setFormerBusNo29(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo30()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo30()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo30(dto.getSelectedBusNum());
						routeDto.setFormerBusNo30(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo31()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo31()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo31(dto.getSelectedBusNum());
						routeDto.setFormerBusNo31(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo32()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo32()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo32(dto.getSelectedBusNum());
						routeDto.setFormerBusNo32(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo33()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo33()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo33(dto.getSelectedBusNum());
						routeDto.setFormerBusNo33(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo34()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo34()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo34(dto.getSelectedBusNum());
						routeDto.setFormerBusNo34(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo35()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo35()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo35(dto.getSelectedBusNum());
						routeDto.setFormerBusNo35(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo36()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo36()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo36(dto.getSelectedBusNum());
						routeDto.setFormerBusNo36(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo37()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo37()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo37(dto.getSelectedBusNum());
						routeDto.setFormerBusNo37(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo38()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo38()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo38(dto.getSelectedBusNum());
						routeDto.setFormerBusNo38(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo39()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo39()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo39(dto.getSelectedBusNum());
						routeDto.setFormerBusNo39(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo40()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo40()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo40(dto.getSelectedBusNum());
						routeDto.setFormerBusNo40(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo41()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo41()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo41(dto.getSelectedBusNum());
						routeDto.setFormerBusNo41(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo42()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo42()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo42(dto.getSelectedBusNum());
						routeDto.setFormerBusNo42(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo43()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo43()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo43(dto.getSelectedBusNum());
						routeDto.setFormerBusNo43(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo44()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo44()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo44(dto.getSelectedBusNum());
						routeDto.setFormerBusNo44(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo45()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo45()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo45(dto.getSelectedBusNum());
						routeDto.setFormerBusNo45(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo46()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo46()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo46(dto.getSelectedBusNum());
						routeDto.setFormerBusNo46(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo47()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo47()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo47(dto.getSelectedBusNum());
						routeDto.setFormerBusNo47(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo48()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo48()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo48(dto.getSelectedBusNum());
						routeDto.setFormerBusNo48(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo49()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo49()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo49(dto.getSelectedBusNum());
						routeDto.setFormerBusNo49(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo50()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo50()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo50(dto.getSelectedBusNum());
						routeDto.setFormerBusNo50(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo51()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo51()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo51(dto.getSelectedBusNum());
						routeDto.setFormerBusNo51(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo52()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo52()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo52(dto.getSelectedBusNum());
						routeDto.setFormerBusNo52(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo53()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo53()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo53(dto.getSelectedBusNum());
						routeDto.setFormerBusNo53(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo54()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo54()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo54(dto.getSelectedBusNum());
						routeDto.setFormerBusNo54(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo55()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo55()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo55(dto.getSelectedBusNum());
						routeDto.setFormerBusNo55(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo56()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo56()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo56(dto.getSelectedBusNum());
						routeDto.setFormerBusNo56(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo57()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo57()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo57(dto.getSelectedBusNum());
						routeDto.setFormerBusNo57(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo58()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo58()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo58(dto.getSelectedBusNum());
						routeDto.setFormerBusNo58(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo59()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo59()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo59(dto.getSelectedBusNum());
						routeDto.setFormerBusNo59(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo60()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo60()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo60(dto.getSelectedBusNum());
						routeDto.setFormerBusNo60(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo61()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo61()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo61(dto.getSelectedBusNum());
						routeDto.setFormerBusNo61(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo62()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo62()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo62(dto.getSelectedBusNum());
						routeDto.setFormerBusNo62(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo63()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo63()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo63(dto.getSelectedBusNum());
						routeDto.setFormerBusNo63(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo64()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo64()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo64(dto.getSelectedBusNum());
						routeDto.setFormerBusNo64(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo65()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo65()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo65(dto.getSelectedBusNum());
						routeDto.setFormerBusNo65(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo66()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo66()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo66(dto.getSelectedBusNum());
						routeDto.setFormerBusNo66(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo67()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo67()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo67(dto.getSelectedBusNum());
						routeDto.setFormerBusNo67(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo68()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo68()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo68(dto.getSelectedBusNum());
						routeDto.setFormerBusNo68(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo69()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo69()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo69(dto.getSelectedBusNum());
						routeDto.setFormerBusNo69(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo70()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo70()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo70(dto.getSelectedBusNum());
						routeDto.setFormerBusNo70(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo71()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo71()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo71(dto.getSelectedBusNum());
						routeDto.setFormerBusNo71(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo72()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo72()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo72(dto.getSelectedBusNum());
						routeDto.setFormerBusNo72(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo73()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo73()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo73(dto.getSelectedBusNum());
						routeDto.setFormerBusNo73(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo74()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo74()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo74(dto.getSelectedBusNum());
						routeDto.setFormerBusNo74(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo75()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo75()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo75(dto.getSelectedBusNum());
						routeDto.setFormerBusNo75(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo76()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo76()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo76(dto.getSelectedBusNum());
						routeDto.setFormerBusNo76(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo77()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo77()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo77(dto.getSelectedBusNum());
						routeDto.setFormerBusNo77(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo78()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo78()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo78(dto.getSelectedBusNum());
						routeDto.setFormerBusNo78(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo79()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo79()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo79(dto.getSelectedBusNum());
						routeDto.setFormerBusNo79(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo80()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo80()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo80(dto.getSelectedBusNum());
						routeDto.setFormerBusNo80(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo81()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo81()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo81(dto.getSelectedBusNum());
						routeDto.setFormerBusNo81(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo82()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo82()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo82(dto.getSelectedBusNum());
						routeDto.setFormerBusNo82(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo83()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo83()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo83(dto.getSelectedBusNum());
						routeDto.setFormerBusNo83(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo84()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo84()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo84(dto.getSelectedBusNum());
						routeDto.setFormerBusNo84(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo85()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo85()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo85(dto.getSelectedBusNum());
						routeDto.setFormerBusNo85(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo86()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo86()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo86(dto.getSelectedBusNum());
						routeDto.setFormerBusNo86(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo87()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo87()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo87(dto.getSelectedBusNum());
						routeDto.setFormerBusNo87(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo88()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo88()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo88(dto.getSelectedBusNum());
						routeDto.setFormerBusNo88(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo89()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo89()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo89(dto.getSelectedBusNum());
						routeDto.setFormerBusNo89(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo90()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo90()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo90(dto.getSelectedBusNum());
						routeDto.setFormerBusNo90(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo91()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo91()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo91(dto.getSelectedBusNum());
						routeDto.setFormerBusNo91(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
					if ((dto.getBusNo() != null && !dto.getBusNo().isEmpty()
							&& dto.getBusNo().equalsIgnoreCase(dto2.getBusNo92()))
							|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
									&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo92()))) {
						RouteScheduleDTO routeDto = new RouteScheduleDTO();
						String formerBus = dto.getBusNo();
						routeDto = dto2;
						routeDto.setBusNo92(dto.getSelectedBusNum());
						routeDto.setFormerBusNo92(formerBus);
						
						routeDto.setSwapEnds(dto.isSwapEnds());
						routeDto.setBusNoChanged(true);
						tempLeaveBusForRouteList.remove(dto2);
						tempLeaveBusForRouteList.add(routeDto);
						
					}
				}
			}
		}

		leaveForRouteList = tempLeaveBusForRouteList;
		// leave table end

		/** bus route order start **/

		Collections.sort(busForRouteList, new Comparator<RouteScheduleDTO>() {
			@Override
			public int compare(RouteScheduleDTO u1, RouteScheduleDTO u2) {

				return Integer.valueOf(u2.getTripId()) - Integer.valueOf(u1.getTripId());

			}
		});

		Collections.reverse(busForRouteList);
		/** bus route order end **/

		createDataTable();

		saveBtnDisable = true;

		sessionBackingBean.setMessage("Bus No. applied successfully");
		RequestContext.getCurrentInstance().execute("PF('dlgCommonSuccess').show()");

		disableApplyBtn = true;
	}

	public void setBusType(RouteScheduleDTO dto1) {

		modifyList.add(dto1);
		for (RouteScheduleDTO dto : abbreviationOriginList) {
			if (dto.getSelectedBusNum() != null && !dto.getSelectedBusNum().isEmpty()
					&& !dto.getSelectedBusNum().equals("") && dto.getSelectedBusNum().equals(dto1.getSelectedBusNum())
					&& !dto.getBusNo().equals(dto1.getBusNo())) {

				sessionBackingBean.setMessage("Selected Bus No. has been already assigned to an Abbreviation");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
			}
		}
	}

	public void setBusTypeDest(RouteScheduleDTO dto1) {

		modifyList.add(dto1);
		for (RouteScheduleDTO dto : abbreviationDestinationList) {
			if (dto.getSelectedBusNum() != null && !dto.getSelectedBusNum().isEmpty()
					&& !dto.getSelectedBusNum().equals("") && dto.getSelectedBusNum().equals(dto1.getSelectedBusNum())
					&& !dto.getBusNo().equals(dto1.getBusNo())) {

				sessionBackingBean.setMessage("Selected Bus No. has been already assigned to an Abbreviation");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
			}
		}
	}

	public int totalLeaveManagerOrigin() {

		RouteScheduleDTO dto = routeScheduleService.getNoOfBusesAndTripsForRoute(routeScheduleDTO.getRouteNo(),
				routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), "O");

		RouteScheduleDTO dto2 = routeScheduleService.getNoOfBusesAndTripsForRoute(routeScheduleDTO.getRouteNo(),
				routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), "D");

		if (dto.getTotalBuses() != 0 && dto.getTotaltrips() != 0) {

			noOfBuses = dto.getTotalBuses() + dto2.getTotalBuses();
			noOfTrips = dto.getTotaltrips() + dto2.getTotaltrips();

			int leaves = noOfBuses - noOfTrips;

			noOfLeaves = Math.abs(leaves);

		} else {
			setErrorMessage("Can not continue the flow. No. of buses / trips are zero for origin");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

			noOfLeaves = -1; // end the flow
		}

		return noOfLeaves;

	}

	public int totalLeaveManagerDestination() {

		RouteScheduleDTO dto = routeScheduleService.getNoOfBusesAndTripsForRoute(routeScheduleDTO.getRouteNo(),
				routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), "O");

		RouteScheduleDTO dto2 = routeScheduleService.getNoOfBusesAndTripsForRoute(routeScheduleDTO.getRouteNo(),
				routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), "D");

		if (dto2.getTotalBuses() != 0 && dto2.getTotaltrips() != 0) {

			noOfBuses = dto.getTotalBuses() + dto2.getTotalBuses();
			noOfTrips = dto.getTotaltrips() + dto2.getTotaltrips();

			int leaves = noOfBuses - noOfTrips;

			noOfLeaves = Math.abs(leaves);

		} else {
			setErrorMessage("Can not continue the flow. No. of buses / trips are zero for destination");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

			noOfLeaves = -1; // end the flow
		}

		return noOfLeaves;

	}

	public int displayOriginAndDestinationDetails() {

		int leaves = 0;
		/* Get Origin end route details */
		RouteScheduleDTO dto1 = routeScheduleService.getNoOfBusesAndTripsForRoute(routeScheduleDTO.getRouteNo(),
				routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), "O");

		RouteScheduleDTO dto2 = routeScheduleService.getNoOfBusesAndTripsForRoute(routeScheduleDTO.getRouteNo(),
				routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), "D");

		/* Get no of without fixed buses for route/group/type */

		int origin_without_fixed_buses = routeScheduleService.getTotalWithoutFixedBusForGroupAndType(
				routeScheduleDTO.getRouteNo(), routeScheduleDTO.getGeneratedRefNo(), "O",
				routeScheduleDTO.getGroupNo());

		int destination_without_fixed_buses = routeScheduleService.getTotalWithoutFixedBusForGroupAndType(
				routeScheduleDTO.getRouteNo(), routeScheduleDTO.getGeneratedRefNo(), "D",
				routeScheduleDTO.getGroupNo());

		/* Get no of fixed buses for route/group/type */

		int originTotalLeaves = routeScheduleService.getTotalLavesForGroupAndType(routeScheduleDTO.getGeneratedRefNo(),
				"O", routeScheduleDTO.getGroupNo());

		int destinationTotalLeaves = routeScheduleService
				.getTotalLavesForGroupAndType(routeScheduleDTO.getGeneratedRefNo(), "D", routeScheduleDTO.getGroupNo());

		/* Get no of leaves buses for route/group/type */

		int origin_totalTrips = routeScheduleService.getFixedOrWithoutFixedTripsCount(
				routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), "O", "N");

		int destination_totalTrips = routeScheduleService.getFixedOrWithoutFixedTripsCount(
				routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), "D", "N");

		RouteScheduleDTO startEndDateDTO = busesAssignedForAbbreviationService.retrieveStartEndDateOfTimeTableDateRange(
				routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), tripType);
		RouteScheduleDTO lastPanelStartEndDateDTO = busesAssignedForAbbreviationService
				.retrieveStartEndDateOfLastPanelDateRange(routeScheduleDTO.getGeneratedRefNo(),
						routeScheduleDTO.getGroupNo(), routeScheduleDTO.getRouteNo(), tripType);

		List<RouteScheduleDTO> routeScheduleList = new ArrayList<RouteScheduleDTO>();
		routeScheduleList = busesAssignedForAbbreviationService
				.retrieveLeavePositionDetails(routeScheduleDTO.getGeneratedRefNo(), tripType);

		if (tripType.equals("O")) {
			int leavs = totalLeaveManagerOrigin();
			if (leavs == -1) {
				busForRouteList = new ArrayList<RouteScheduleDTO>();
				leaveForRouteList = new ArrayList<RouteScheduleDTO>();
				abbreviationOriginList = new ArrayList<RouteScheduleDTO>();
				abbreviationDestinationList = new ArrayList<RouteScheduleDTO>();
				return -1;
			}
		}
		if (tripType.equals("D")) {
			int leavs = totalLeaveManagerDestination();
			if (leavs == -1) {
				busForRouteList = new ArrayList<RouteScheduleDTO>();
				leaveForRouteList = new ArrayList<RouteScheduleDTO>();
				abbreviationOriginList = new ArrayList<RouteScheduleDTO>();
				abbreviationDestinationList = new ArrayList<RouteScheduleDTO>();
				return -1;
			}
		}

		if (originTotalLeaves >= 0 && destinationTotalLeaves >= 0) {

			if (tripType.equals("O")) {

				originBusSelect = true;
				destinationBusSelect = false;

				routeScheduleDTO.setNoOfBusesOrigin(origin_without_fixed_buses);
				routeScheduleDTO.setNoOfLeavesOrigin(originTotalLeaves);

				routeScheduleDTO.setNoOfBusesDestination(destination_without_fixed_buses);
				routeScheduleDTO.setNoOfLeavesDestination(destinationTotalLeaves);

				routeScheduleDTO.setNoOfTripsOrigin(destination_totalTrips);

				routeScheduleDTO.setStartDate(startEndDateDTO.getStartDate());
				routeScheduleDTO.setEndDate(startEndDateDTO.getEndDate());

				routeScheduleDTO.setLastPanelStartDate(lastPanelStartEndDateDTO.getStartDate());
				routeScheduleDTO.setLastPanelEndDate(lastPanelStartEndDateDTO.getEndDate());

				leavePositionList = new ArrayList<String>();

				for (RouteScheduleDTO dto : routeScheduleList) {
					leavePositionList.add(Integer.toString(dto.getLeavePosition()));

				}

				leaves = originTotalLeaves;

				/* display leave position select menu */
				selectLeavePositionList = new ArrayList<>();

				for (int i = 1; i <= routeScheduleDTO.getNoOfTripsOrigin(); i++) {

					RouteScheduleDTO dtos = new RouteScheduleDTO(i);

					selectLeavePositionList.add(dtos);
				}

			} else {

				originBusSelect = false;
				destinationBusSelect = true;

				routeScheduleDTO.setNoOfBusesOrigin(destination_without_fixed_buses);
				routeScheduleDTO.setNoOfLeavesOrigin(destinationTotalLeaves);

				routeScheduleDTO.setNoOfBusesDestination(origin_without_fixed_buses);
				routeScheduleDTO.setNoOfLeavesDestination(originTotalLeaves);

				routeScheduleDTO.setNoOfTripsDestination(origin_totalTrips);

				routeScheduleDTO.setStartDate(startEndDateDTO.getStartDate());
				routeScheduleDTO.setEndDate(startEndDateDTO.getEndDate());

				leavePositionList = new ArrayList<String>();
				int count = 0;
				for (RouteScheduleDTO dto : routeScheduleList) {
					leavePositionList.add(Integer.toString(dto.getLeavePosition()));
					count = count + 1;
				}

				leaves = destinationTotalLeaves;

				/* display leave position select menu */
				selectLeavePositionList = new ArrayList<>();

				for (int i = 1; i <= routeScheduleDTO.getNoOfTripsDestination(); i++) {

					RouteScheduleDTO dtos = new RouteScheduleDTO(i);

					selectLeavePositionList.add(dtos);
				}
			}

		} else {

			setErrorMessage("Can not continue the flow. Origin and destination leaves can not less than zero");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

			noOfLeaves = -1; // end the flow
		}

		return leaves;

	}

	public long calculateNoOfDays() {

		Date start_date = routeScheduleDTO.getStartDate();
		Date end_date = routeScheduleDTO.getEndDate();

		long diff = start_date.getTime() - end_date.getTime();

		long days = (diff / (1000 * 60 * 60 * 24)) - 1;

		return Math.abs(days);
	}

	public void ajaxEndDateExpireDateValidator() {

		if (routeScheduleDTO.getEndDate() != null && routeScheduleDTO.getStartDate() != null
				&& routeScheduleDTO.getStartDate().after(routeScheduleDTO.getEndDate())) {

			setErrorMessage("End date should be greater than Start date");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

			routeScheduleDTO.setEndDate(null);

		}
	}

	public void ajaxStartDateExpireDateValidator() {

		if (routeScheduleDTO.getEndDate() != null && routeScheduleDTO.getStartDate() != null
				&& routeScheduleDTO.getEndDate().before(routeScheduleDTO.getStartDate())) {

			setErrorMessage("Start date should be less than end date");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

			routeScheduleDTO.setStartDate(null);

		}
	}

	public void clearOne() {
		loadValue();

		RequestContext.getCurrentInstance().update(":mainfrm");
	}

	public void busNoManagerNormal(int noOfTrips) {

		busForRouteList = new ArrayList<>();
		leaveForRouteList = new ArrayList<>();
		setTripID(0);
		int noOfTripsNew = 0;
		int tripCounting = 0;
		List<RouteScheduleDTO> tempAllBusList = routeScheduleService
				.retrieveInsertedDataForEdit(routeScheduleDTO.getGeneratedRefNo(), tripType);
		int tempTripsNum = tempAllBusList.get(0).getTripCount();

		List<String> busNoList = new ArrayList<String>();
		busNoList = routeScheduleService.selectEditDateForAssignedBuses(routeScheduleDTO.getRouteNo(),
				routeScheduleDTO.getBusCategory(), routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(),
				tripType);
		/** modify by tharushi.e **/

		// need to check noOfTripsForSelectedSide
		boolean firstDone = false;
		// for (int nooftrips = 0; nooftrips < noOfTrips; nooftrips++) {
		// /**tharushi.e**/
		noOfTripsNew = routeScheduleService.getNoOfTripsPerSideForEdit(routeScheduleDTO.getRouteNo(),
				routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), tripType,
				routeScheduleDTO.getBusCategory());
		for (int nooftrips = 0; nooftrips < noOfTripsNew; nooftrips++) {

			List<String> stringBusNoList = new ArrayList<>();

			/*
			 * Rotate normally. Get the first number of list and send it to last
			 * index. then last index come to before the last one. likewise all
			 * index move
			 */
			List<String> tempBusNoList = new ArrayList<String>();
			for (int i = 0; i < busNoList.size(); i++) {

				if (!firstDone) {
					String[] bus = busNoList.get(i).split("-");
					String toMove = "";
					boolean first = false;
					if (bus.length > 2) {
						for (int j = 0; j < bus.length - 1; j++) {
							if (!first) {
								first = true;
								toMove = bus[j];
							} else {
								toMove = toMove + "-" + bus[j];
							}
						}
					} else if (bus.length == 2) {
						if (nooftrips > 0) {
							toMove = bus[0] + "-" + bus[1];
						} else {
							toMove = bus[0];
						}

					} else {
						toMove = bus[0];
					}

					tempBusNoList.add(toMove);
				} else {
					if (i == 0) {
						tempBusNoList.add(busNoList.get(busNoList.size() - 1));
					} else if (i > 0) {
						tempBusNoList.add(busNoList.get(i - 1));
					}
				}

			}

			firstDone = true;
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

			for (int i = 0; i < tempTripsNum; i++) {

				if (tripCounting < tempAllBusList.size()) {

					stringBusNoList.set(i, tempAllBusList.get(tripCounting).getBusNo());
					tripCounting = tripCounting + 1;
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
			for (int c = 0; c < leavePositionList.size(); c++) {

				int e = Integer.valueOf(leavePositionList.get(c));
				int position = e - 1;
				RouteScheduleDTO dto = busForRouteList.get(position);
				removingListForLeaves.add(dto);
			}
			/*
			 * remove above DTO from busForRouteList and add to
			 * leaveForRouteList
			 */
			// busForRouteList.removeAll(removingListForLeaves);
			leaveForRouteList.addAll(removingListForLeaves);

			renderNormal = true;

			if (!leaveForRouteList.isEmpty()) {
				renderNormal = true;
				renderTableTwo = false;
			}

		} else {
			setErrorMessage("Data not found");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	private RouteScheduleDTO setBusToRouteScheduleDTO(List<String> stringBusNoList) {

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

	public List<String> createDataTable() {

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

	public void zigzagRotation() {

	}

	public void generateReport() {

	}

	public void saveAction() {
		swapOriginDestination = false;

		boolean dataSaved = false;

		dataSaved = busesAssignedForAbbreviationService.updateBusNoInTimetableGeneratorDetNew(modifyList,
				sessionBackingBean.getLoginUser(), routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(),
				tripType);

		/**
		 * if D to O get all D buses and assigned busses and compare and put
		 * assigned buses to origin list selected buses end
		 **/

		/**
		 * if O to D get all O buses and assigned busses and compare and put
		 * assigned buses to destination list selected buses start
		 **/

		/**
		 * if O to D get all O buses and assigned busses and compare and put
		 * assigned buses to destination list selected buses end
		 **/
		if (dataSaved) {
			sessionBackingBean.setMessage("Saved successfully.");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonSuccess').show()");

			modifyList = new ArrayList<RouteScheduleDTO>();

		}

		else {

			setErrorMessage("Data not saved");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

		disableApplyBtn = false;
	}

	public List<String> createMainDataTable(List<RouteScheduleDTO> dList) {

		// for(RouteScheduleDTO dto :busForRouteList) {
		for (RouteScheduleDTO dto : dList) {

		}

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

		for (RouteScheduleDTO dto : busForRouteList) {

		}

		return VALID_COLUMN_KEYS;
	}

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

	public List<RouteScheduleDTO> getBusForRouteList() {
		return busForRouteList;
	}

	public void setBusForRouteList(List<RouteScheduleDTO> busForRouteList) {
		this.busForRouteList = busForRouteList;
	}

	public List<RouteScheduleDTO> getColumnKeys() {
		return columnKeys;
	}

	public void setColumnKeys(List<RouteScheduleDTO> columnKeys) {
		this.columnKeys = columnKeys;
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

	public boolean isRenderNormal() {
		return renderNormal;
	}

	public void setRenderNormal(boolean renderNormal) {
		this.renderNormal = renderNormal;
	}

	public List<RouteScheduleDTO> getAbbreviationOriginList() {
		return abbreviationOriginList;
	}

	public void setAbbreviationOriginList(List<RouteScheduleDTO> abbreviationOriginList) {
		this.abbreviationOriginList = abbreviationOriginList;
	}

	public List<RouteScheduleDTO> getAbbreviationDestinationList() {
		return abbreviationDestinationList;
	}

	public void setAbbreviationDestinationList(List<RouteScheduleDTO> abbreviationDestinationList) {
		this.abbreviationDestinationList = abbreviationDestinationList;
	}

	public TimeTableService getTimeTableService() {
		return timeTableService;
	}

	public void setTimeTableService(TimeTableService timeTableService) {
		this.timeTableService = timeTableService;
	}

	public List<TimeTableDTO> getOriginBusList() {
		return originBusList;
	}

	public void setOriginBusList(List<TimeTableDTO> originBusList) {
		this.originBusList = originBusList;
	}

	public List<TimeTableDTO> getDestinationBusList() {
		return destinationBusList;
	}

	public void setDestinationBusList(List<TimeTableDTO> destinationBusList) {
		this.destinationBusList = destinationBusList;
	}

	public BusesAssignedForAbbreviationService getBusesAssignedForAbbreviationService() {
		return busesAssignedForAbbreviationService;
	}

	public void setBusesAssignedForAbbreviationService(
			BusesAssignedForAbbreviationService busesAssignedForAbbreviationService) {
		this.busesAssignedForAbbreviationService = busesAssignedForAbbreviationService;
	}

	public List<RouteScheduleDTO> getLeaveForRouteList() {
		return leaveForRouteList;
	}

	public void setLeaveForRouteList(List<RouteScheduleDTO> leaveForRouteList) {
		this.leaveForRouteList = leaveForRouteList;
	}

	public int getTripID() {
		return tripID;
	}

	public void setTripID(int tripID) {
		this.tripID = tripID;
	}

	public void setLeavePositionList(List<String> leavePositionList) {
		this.leavePositionList = leavePositionList;
	}

	public List<ColumnModel> getColumnsLeaves() {
		return columnsLeaves;
	}

	public void setColumnsLeaves(List<ColumnModel> columnsLeaves) {
		this.columnsLeaves = columnsLeaves;
	}

	public List<String> getLeavePositionList() {
		return leavePositionList;
	}

	public List<RouteScheduleDTO> getMainSaveList() {
		return mainSaveList;
	}

	public void setMainSaveList(List<RouteScheduleDTO> mainSaveList) {
		this.mainSaveList = mainSaveList;
	}

	public int getMaximumDays() {
		return maximumDays;
	}

	public boolean isRenderTableTwo() {
		return renderTableTwo;
	}

	public void setRenderTableTwo(boolean renderTableTwo) {
		this.renderTableTwo = renderTableTwo;
	}

	public boolean isSaveBtnDisable() {
		return saveBtnDisable;
	}

	public void setSaveBtnDisable(boolean saveBtnDisable) {
		this.saveBtnDisable = saveBtnDisable;
	}

	public boolean isOriginBusSelect() {
		return originBusSelect;
	}

	public void setOriginBusSelect(boolean originBusSelect) {
		this.originBusSelect = originBusSelect;
	}

	public boolean isDestinationBusSelect() {
		return destinationBusSelect;
	}

	public void setDestinationBusSelect(boolean destinationBusSelect) {
		this.destinationBusSelect = destinationBusSelect;
	}

	public boolean isSwapOriginDestination() {
		return swapOriginDestination;
	}

	public void setSwapOriginDestination(boolean swapOriginDestination) {
		this.swapOriginDestination = swapOriginDestination;
	}

	public List<TimeTableDTO> getOriginCancelBuses() {
		return originCancelBuses;
	}

	public void setOriginCancelBuses(List<TimeTableDTO> originCancelBuses) {
		this.originCancelBuses = originCancelBuses;
	}

	public List<TimeTableDTO> getDestinationCancelBuses() {
		return destinationCancelBuses;
	}

	public void setDestinationCancelBuses(List<TimeTableDTO> destinationCancelBuses) {
		this.destinationCancelBuses = destinationCancelBuses;
	}

	public boolean isOriginDataTblDisable() {
		return originDataTblDisable;
	}

	public void setOriginDataTblDisable(boolean originDataTblDisable) {
		this.originDataTblDisable = originDataTblDisable;
	}

	public List<RouteScheduleDTO> getdList() {
		return dList;
	}

	public void setdList(List<RouteScheduleDTO> dList) {
		this.dList = dList;
	}

	public List<RouteScheduleDTO> getModifyList() {
		return modifyList;
	}

	public void setModifyList(List<RouteScheduleDTO> modifyList) {
		this.modifyList = modifyList;
	}

	public boolean isDisableApplyBtn() {
		return disableApplyBtn;
	}

	public void setDisableApplyBtn(boolean disableApplyBtn) {
		this.disableApplyBtn = disableApplyBtn;
	}

}
