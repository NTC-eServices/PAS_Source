package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.TimeTableDTO;
import lk.informatics.ntc.model.service.PanelGeneratorWithoutFixedTimeService;
import lk.informatics.ntc.model.service.RouteScheduleService;
import lk.informatics.ntc.model.service.TimeTableService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "panelGeneratorWithFixedBusesBean")
@ViewScoped
public class PanelGeneratorWithFixedBusesBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private List<TimeTableDTO> busRouteList = new ArrayList<>(0);
	private List<TimeTableDTO> busNoList = new ArrayList<>(0);
	private List<TimeTableDTO> busNoListDestination = new ArrayList<>(0);

	private List<TimeTableDTO> busCategoryList = new ArrayList<>(0);
	private List<TimeTableDTO> routeDetailsList, originGroupOneList, originGroupTwoList, originGroupThreeList,
			destinationGroupOneList, destinationGroupTwoList, destinationGroupThreeList;
	private int activeTabIndex;
	private TimeTableDTO timeTableDTO, routeDTO;
	private String alertMSG, successMessage, errorMessage;
	private int groupCount;
	private boolean disabledGroupTwo, disabledGroupThree, disableBusCategory;
	private TimeTableService timeTableService;
	private PanelGeneratorWithoutFixedTimeService panelGeneratorWithoutFixedTimeService;
	private RouteScheduleService routeScheduleService;

	@PostConstruct
	public void init() {
		timeTableService = (TimeTableService) SpringApplicationContex.getBean("timeTableService");
		panelGeneratorWithoutFixedTimeService = (PanelGeneratorWithoutFixedTimeService) SpringApplicationContex
				.getBean("panelGeneratorWithoutFixedTimeService");
		routeScheduleService = (RouteScheduleService) SpringApplicationContex.getBean("routeScheduleService");
		loadValue();
	}

	private void loadValue() {

		routeDetailsList = new ArrayList<>();
		timeTableDTO = new TimeTableDTO();
		routeDTO = new TimeTableDTO();
		busRouteList = timeTableService.getRouteNoListForFixedBuses();

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
		}
	}

	public void searchAction() {

		if (timeTableDTO.getRouteNo() != null && !timeTableDTO.getRouteNo().trim().equalsIgnoreCase("")) {

			if (timeTableDTO.getBusCategory() != null && !timeTableDTO.getBusCategory().trim().equalsIgnoreCase("")) {

				groupCount = timeTableService.getGroupCount(timeTableDTO.getRouteNo(),
						timeTableDTO.getGenereatedRefNo());

				busNoList = new ArrayList<>(0);
				busNoListDestination = new ArrayList<>(0);

				busNoList = timeTableService.getBusNoByRouteAndCatForFixedBuses(timeTableDTO.getRouteNo(),
						timeTableDTO.getBusCategory(), "asc"); // removed route flag

				busNoListDestination = timeTableService.getBusNoByRouteAndCatForFixedBuses(timeTableDTO.getRouteNo(),
						timeTableDTO.getBusCategory(), "desc");// removed route flag

				TimeTableDTO timeTableObj = new TimeTableDTO();
				timeTableObj.setAssigneBusNo("SLTB-O");
				busNoList.add(timeTableObj);
				TimeTableDTO timeTableObjD = new TimeTableDTO();
				timeTableObjD.setAssigneBusNo("SLTB-D");
				busNoList.add(timeTableObjD);

				TimeTableDTO timeTableObj2 = new TimeTableDTO();
				timeTableObj2.setAssigneBusNo("SLTB-D");
				busNoListDestination.add(timeTableObj2);
				TimeTableDTO timeTableObj2D = new TimeTableDTO();
				timeTableObj2D.setAssigneBusNo("SLTB-O");
				busNoListDestination.add(timeTableObj2D);

				TimeTableDTO timeTableObj3 = new TimeTableDTO();
				timeTableObj3.setAssigneBusNo("ETC-O");
				busNoList.add(timeTableObj3);
				TimeTableDTO timeTableObj3D = new TimeTableDTO();
				timeTableObj3D.setAssigneBusNo("ETC-D");
				busNoList.add(timeTableObj3D);

				TimeTableDTO timeTableObj4 = new TimeTableDTO();
				timeTableObj4.setAssigneBusNo("ETC-D");
				busNoListDestination.add(timeTableObj4);
				TimeTableDTO timeTableObj4D = new TimeTableDTO();
				timeTableObj4D.setAssigneBusNo("ETC-O");
				busNoListDestination.add(timeTableObj4D);
				// added SLTB-O and D both in orgin and destination side bcoz of pramitha's
				// reqirement and ETC too
				boolean error = groupManager(groupCount);
				if (error == true) {
					setErrorMessage("Searched data is not eligible. Buses are already assigned");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
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

	public boolean groupManager(int groupCount) {

		if (groupCount == 1) {
			timeTableDTO.setGroupOneDays(panelGeneratorWithoutFixedTimeService
					.retrieveDaysForGroup(timeTableDTO.getRouteNo(), timeTableDTO.getGenereatedRefNo(), "1"));

			disabledGroupTwo = true;
			disabledGroupThree = true;

			originGroupOneList = timeTableService.getDetailsOfFixedBuses(timeTableDTO, "1", "O");
			destinationGroupOneList = timeTableService.getDetailsOfFixedBuses(timeTableDTO, "1", "D");

			boolean assigned = routeScheduleService.checkBusAssignerDoneLetEdit(timeTableDTO.getBusCategory(),
					timeTableDTO.getGenereatedRefNo(), "1", "O");

			if (assigned) {
				return false;
			}

			boolean assignedD = routeScheduleService.checkBusAssignerDoneLetEdit(timeTableDTO.getBusCategory(),
					timeTableDTO.getGenereatedRefNo(), "1", "D");

			if (assignedD) {
				return false;
			}

		} else if (groupCount == 2) {

			timeTableDTO.setGroupOneDays(panelGeneratorWithoutFixedTimeService
					.retrieveDaysForGroup(timeTableDTO.getRouteNo(), timeTableDTO.getGenereatedRefNo(), "1"));

			timeTableDTO.setGroupTwoDays(panelGeneratorWithoutFixedTimeService
					.retrieveDaysForGroup(timeTableDTO.getRouteNo(), timeTableDTO.getGenereatedRefNo(), "2"));

			disabledGroupTwo = false;
			disabledGroupThree = true;

			originGroupOneList = timeTableService.getDetailsOfFixedBuses(timeTableDTO, "1", "O");
			destinationGroupOneList = timeTableService.getDetailsOfFixedBuses(timeTableDTO, "1", "D");

			originGroupTwoList = timeTableService.getDetailsOfFixedBuses(timeTableDTO, "2", "O");
			destinationGroupTwoList = timeTableService.getDetailsOfFixedBuses(timeTableDTO, "2", "D");

			boolean assigned = routeScheduleService.checkBusAssignerDoneLetEdit(timeTableDTO.getBusCategory(),
					timeTableDTO.getGenereatedRefNo(), "1", "O");

			if (assigned) {
				return false;
			}

			boolean assignedD = routeScheduleService.checkBusAssignerDoneLetEdit(timeTableDTO.getBusCategory(),
					timeTableDTO.getGenereatedRefNo(), "1", "D");

			if (assignedD) {
				return false;
			}

			boolean assigned2 = routeScheduleService.checkBusAssignerDoneLetEdit(timeTableDTO.getBusCategory(),
					timeTableDTO.getGenereatedRefNo(), "2", "O");

			if (assigned2) {
				return false;
			}

			boolean assignedD2 = routeScheduleService.checkBusAssignerDoneLetEdit(timeTableDTO.getBusCategory(),
					timeTableDTO.getGenereatedRefNo(), "2", "D");

			if (assignedD2) {
				return false;
			}

		} else if (groupCount == 3) {

			disabledGroupTwo = false;
			disabledGroupThree = false;

			timeTableDTO.setGroupOneDays(panelGeneratorWithoutFixedTimeService
					.retrieveDaysForGroup(timeTableDTO.getRouteNo(), timeTableDTO.getGenereatedRefNo(), "1"));

			timeTableDTO.setGroupTwoDays(panelGeneratorWithoutFixedTimeService
					.retrieveDaysForGroup(timeTableDTO.getRouteNo(), timeTableDTO.getGenereatedRefNo(), "2"));

			timeTableDTO.setGroupThreeDays(panelGeneratorWithoutFixedTimeService
					.retrieveDaysForGroup(timeTableDTO.getRouteNo(), timeTableDTO.getGenereatedRefNo(), "3"));

			originGroupOneList = timeTableService.getDetailsOfFixedBuses(timeTableDTO, "1", "O");
			destinationGroupOneList = timeTableService.getDetailsOfFixedBuses(timeTableDTO, "1", "D");

			originGroupTwoList = timeTableService.getDetailsOfFixedBuses(timeTableDTO, "2", "O");
			destinationGroupTwoList = timeTableService.getDetailsOfFixedBuses(timeTableDTO, "2", "D");

			originGroupThreeList = timeTableService.getDetailsOfFixedBuses(timeTableDTO, "3", "O");
			destinationGroupThreeList = timeTableService.getDetailsOfFixedBuses(timeTableDTO, "3", "D");

			boolean assigned = routeScheduleService.checkBusAssignerDoneLetEdit(timeTableDTO.getBusCategory(),
					timeTableDTO.getGenereatedRefNo(), "1", "O");

			if (assigned) {
				return false;
			}

			boolean assignedD = routeScheduleService.checkBusAssignerDoneLetEdit(timeTableDTO.getBusCategory(),
					timeTableDTO.getGenereatedRefNo(), "1", "D");

			if (assignedD) {
				return false;
			}

			boolean assigned2 = routeScheduleService.checkBusAssignerDoneLetEdit(timeTableDTO.getBusCategory(),
					timeTableDTO.getGenereatedRefNo(), "2", "O");

			if (assigned2) {
				return false;
			}

			boolean assignedD2 = routeScheduleService.checkBusAssignerDoneLetEdit(timeTableDTO.getBusCategory(),
					timeTableDTO.getGenereatedRefNo(), "2", "D");

			if (assignedD2) {
				return false;
			}

			boolean assigned3 = routeScheduleService.checkBusAssignerDoneLetEdit(timeTableDTO.getBusCategory(),
					timeTableDTO.getGenereatedRefNo(), "3", "O");

			if (assigned3) {
				return false;
			}

			boolean assignedD3 = routeScheduleService.checkBusAssignerDoneLetEdit(timeTableDTO.getBusCategory(),
					timeTableDTO.getGenereatedRefNo(), "3", "D");

			if (assignedD3) {
				return false;
			}

		} else {
			setErrorMessage("Group Count Range Should be One To Three");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

		return true;
	}

	public void clearOne() {
		originGroupOneList = new ArrayList<>();
		originGroupTwoList = new ArrayList<>();
		originGroupThreeList = new ArrayList<>();
		destinationGroupOneList = new ArrayList<>();
		destinationGroupTwoList = new ArrayList<>();
		destinationGroupThreeList = new ArrayList<>();

		timeTableDTO = new TimeTableDTO();
		routeDTO = new TimeTableDTO();

		disableBusCategory = true;
		disabledGroupTwo = true;
		disabledGroupThree = true;

		busNoList = new ArrayList<>(0);
		busNoListDestination = new ArrayList<>(0);
	}

	public void saveAction() {

		if (groupCount == 1) {

			// added by thilna.d on 14-10-2021
			if (assignBusNullValuesForFixedBuses(originGroupOneList, destinationGroupOneList)) {
				setErrorMessage("Please assign bus no. for fixed buses.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				return;
			}

			List<TimeTableDTO> list = timeTableService
					.getTimeTableGeneratorDetDataForHistorySave(timeTableDTO.getGenereatedRefNo());

			boolean updateOne = timeTableService.updateTimeTableGeneratorDetData(originGroupOneList, destinationGroupOneList,
					sessionBackingBean.getLoginUser(), "1", timeTableDTO.getGenereatedRefNo());

			// add remaining bus numbers for matching abbreviations
			timeTableService.fillRemainingBusNumbers("1", timeTableDTO.getGenereatedRefNo());

			timeTableService.insertTimeTableGeneratorDetDataForHistorySave(list, sessionBackingBean.getLoginUser());

			if (updateOne == true) {
				originGroupOneList = timeTableService.getDetailsOfFixedBuses(timeTableDTO, "1", "O");
				destinationGroupOneList = timeTableService.getDetailsOfFixedBuses(timeTableDTO, "1", "D");
				setSuccessMessage("Saved successfully.");
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
			} else {
				setErrorMessage("Data not saved.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else if (groupCount == 2) {

			if (assignBusNullValuesForFixedBuses(originGroupTwoList, destinationGroupTwoList)) {
				setErrorMessage("Please assign bus no. for fixed buses.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				return;
			}

			List<TimeTableDTO> list = timeTableService
					.getTimeTableGeneratorDetDataForHistorySave(timeTableDTO.getGenereatedRefNo());

			boolean updateTwo = timeTableService.updateTimeTableGeneratorDetData(originGroupTwoList, destinationGroupTwoList,
					sessionBackingBean.getLoginUser(), "2", timeTableDTO.getGenereatedRefNo());

			// add remaining bus numbers for matching abbreviations
			timeTableService.fillRemainingBusNumbers("2", timeTableDTO.getGenereatedRefNo());

			timeTableService.insertTimeTableGeneratorDetDataForHistorySave(list, sessionBackingBean.getLoginUser());

			if (updateTwo == true) {
				originGroupOneList = timeTableService.getDetailsOfFixedBuses(timeTableDTO, "2", "O");
				destinationGroupOneList = timeTableService.getDetailsOfFixedBuses(timeTableDTO, "2", "D");
				setSuccessMessage("Saved successfully.");
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
			} else {
				setErrorMessage("Data not saved.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else if (groupCount == 3) {

			if (assignBusNullValuesForFixedBuses(originGroupThreeList, destinationGroupThreeList)) {
				setErrorMessage("Please assign bus no. for fixed buses.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				return;
			}

			/* Save Data Here */

			List<TimeTableDTO> list = timeTableService
					.getTimeTableGeneratorDetDataForHistorySave(timeTableDTO.getGenereatedRefNo());
			
			boolean updateThree = timeTableService.updateTimeTableGeneratorDetData(originGroupThreeList, destinationGroupThreeList,
					sessionBackingBean.getLoginUser(), "3", timeTableDTO.getGenereatedRefNo());

			// add remaining bus numbers for matching abbreviations
			timeTableService.fillRemainingBusNumbers("3", timeTableDTO.getGenereatedRefNo());

			timeTableService.insertTimeTableGeneratorDetDataForHistorySave(list, sessionBackingBean.getLoginUser());

			if (updateThree == true) {
				originGroupOneList = timeTableService.getDetailsOfFixedBuses(timeTableDTO, "3", "O");
				destinationGroupOneList = timeTableService.getDetailsOfFixedBuses(timeTableDTO, "3", "D");
				setSuccessMessage("Saved successfully.");
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
			} else {
				setErrorMessage("Data not saved.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		}

	}

	public boolean assignBusNullValues(List<TimeTableDTO> list) {
		boolean assignFixBus = false;
		for (TimeTableDTO fixedDto : list) {
			if (fixedDto.getBusNo().equals("SLTB-O") || fixedDto.getBusNo().equals("SLTB-D")
					|| fixedDto.getBusNo().equals("ETC-O") || fixedDto.getBusNo().equals("ETC-D")) {

				if (fixedDto.getAssigneBusNo() == null || fixedDto.getAssigneBusNo().trim().isEmpty()
						|| fixedDto.getAssigneBusNo() == "") {
					assignFixBus = true;

				}
			}

		}
		return assignFixBus;

	}

	public boolean assignBusNullValuesForFixedBuses(List<TimeTableDTO> originList, List<TimeTableDTO> destinList) {
		boolean assignFixBus = false;
		List<TimeTableDTO> combinedList = new ArrayList<>(0);
		combinedList.addAll(originList);
		combinedList.addAll(destinList);

		for (TimeTableDTO fixedDto : combinedList) {
			if (fixedDto.isFixedTime() && (fixedDto.getAssigneBusNo() == null
					|| fixedDto.getAssigneBusNo().trim().isEmpty() || fixedDto.getAssigneBusNo().equals(""))) {
				assignFixBus = true;
				for (TimeTableDTO fixedDtoInner : combinedList) {
					if (fixedDtoInner.getBusNo().equals(fixedDto.getBusNo()) && fixedDtoInner.getAssigneBusNo() != null
							&& !fixedDtoInner.getAssigneBusNo().trim().isEmpty()
							&& !fixedDtoInner.getAssigneBusNo().trim().equals("")) {
						assignFixBus = false;
					}
				}
				if (assignFixBus) {
					return assignFixBus; // return true
				}

			}
		}

		return assignFixBus;

	}

	public boolean nullValueCheckerForList(List<TimeTableDTO> list) {

		boolean nullValueFound = true;

		for (TimeTableDTO dto : list) {

			if (dto.getBusNo() != null && !dto.getBusNo().trim().equalsIgnoreCase("")) {

				if (dto.getBusNo().equals("SLTB") || dto.getBusNo().equals("ETC")) {

					nullValueFound = false;

				} else {
					String asString = dto.getAssigneBusNo();

					if (asString != null && !asString.trim().equalsIgnoreCase("")) {

						nullValueFound = false;

					} else {

						return nullValueFound = true;
					}
				}

			} else {

				String asString = dto.getAssigneBusNo();

				if (asString != null && !asString.trim().equalsIgnoreCase("")) {

					nullValueFound = false;

				} else {

					return nullValueFound = true;
				}

			}

		}

		return nullValueFound;
	}

	public void clearTwo() {
		originGroupOneList = new ArrayList<>();
		originGroupTwoList = new ArrayList<>();
		originGroupThreeList = new ArrayList<>();
		destinationGroupOneList = new ArrayList<>();
		destinationGroupTwoList = new ArrayList<>();
		destinationGroupThreeList = new ArrayList<>();

		timeTableDTO = new TimeTableDTO();
		routeDTO = new TimeTableDTO();

		disableBusCategory = true;
		disabledGroupTwo = true;
		disabledGroupThree = true;
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

	public TimeTableDTO getRouteDTO() {
		return routeDTO;
	}

	public void setRouteDTO(TimeTableDTO routeDTO) {
		this.routeDTO = routeDTO;
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

	public int getGroupCount() {
		return groupCount;
	}

	public void setGroupCount(int groupCount) {
		this.groupCount = groupCount;
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

	public TimeTableService getTimeTableService() {
		return timeTableService;
	}

	public void setTimeTableService(TimeTableService timeTableService) {
		this.timeTableService = timeTableService;
	}

	public List<TimeTableDTO> getBusNoList() {
		return busNoList;
	}

	public void setBusNoList(List<TimeTableDTO> busNoList) {
		this.busNoList = busNoList;
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

	public PanelGeneratorWithoutFixedTimeService getPanelGeneratorWithoutFixedTimeService() {
		return panelGeneratorWithoutFixedTimeService;
	}

	public void setPanelGeneratorWithoutFixedTimeService(
			PanelGeneratorWithoutFixedTimeService panelGeneratorWithoutFixedTimeService) {
		this.panelGeneratorWithoutFixedTimeService = panelGeneratorWithoutFixedTimeService;
	}

	public RouteScheduleService getRouteScheduleService() {
		return routeScheduleService;
	}

	public void setRouteScheduleService(RouteScheduleService routeScheduleService) {
		this.routeScheduleService = routeScheduleService;
	}

	public List<TimeTableDTO> getBusNoListDestination() {
		return busNoListDestination;
	}

	public void setBusNoListDestination(List<TimeTableDTO> busNoListDestination) {
		this.busNoListDestination = busNoListDestination;
	}

}
