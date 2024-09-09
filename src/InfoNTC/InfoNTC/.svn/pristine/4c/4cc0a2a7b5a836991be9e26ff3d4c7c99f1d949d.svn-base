package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.commons.collections.CollectionUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.ChakreeyaPalaliAllDTO;
import lk.informatics.ntc.model.dto.ChakreeyaPalaliDTO;
import lk.informatics.ntc.model.dto.ChakreeyaPalaliDestiDTO;
import lk.informatics.ntc.model.dto.RouteScheduleDTO;
import lk.informatics.ntc.model.dto.TimeTableDTO;
import lk.informatics.ntc.model.dto.WithoutFixedTimeValidationDTO;
import lk.informatics.ntc.model.service.BusFareService;
import lk.informatics.ntc.model.service.BusesAssignedForAbbreviationService;
import lk.informatics.ntc.model.service.MigratedService;
import lk.informatics.ntc.model.service.PanelGeneratorWithoutFixedTimeService;
import lk.informatics.ntc.model.service.RouteScheduleService;
import lk.informatics.ntc.model.service.TimeTableService;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.ntc.view.util.SpringApplicationContex;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.StreamCompression;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

@ManagedBean(name = "editTimeTableManagementBean")
@ViewScoped
public class EditTimeTableManagementBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	private List<TimeTableDTO> busRouteList = new ArrayList<>(0);
	private List<TimeTableDTO> routeDetailsList, originGroupOneList, originGroupTwoList, originGroupThreeList,
			destinationGroupOneList, destinationGroupTwoList, destinationGroupThreeList;
	private List<String> timeList;
	private TimeTableService timeTableService;
	private MigratedService migratedService;
	private PanelGeneratorWithoutFixedTimeService panelGeneratorWithoutFixedTimeService;
	private int activeTabIndex;
	private TimeTableDTO timeTableDTO, groupOneDTO, groupTwoDTO, groupThreeDTO, tripsDTO, routeDTO;
	private String alertMSG, successMessage, errorMessage;
	private int groupCount, pvtBusCount, originOneBuses, destinationOneBuses, originTwoBuses, destinationTwoBuses,
			originThreeBuses, destinationThreeBuses;
	private int restTime;
	private boolean disabledGroupTwo, disabledGroupThree;

	private String groupOneDays, groupTwoDays, groupThreeDays;
	private boolean coupling;
	private String couplesPerBus;
	private boolean couplesPerBusDisable;
	private List<TimeTableDTO> busCategoryList;
	private String selectedBusCategory;
	private boolean editColRender;
	private RouteScheduleService routeScheduleService;
	/** panel generator without fixed time **/
	/** buses assigned for abbreviation **/
	private BusesAssignedForAbbreviationService busesAssignedForAbbreviationService;
	private RouteScheduleDTO routeScheduleDTO;
	private List<RouteScheduleDTO> busRouteListBusAssined, groupNoList, selectLeavePositionList,
			busCategoryListBusAssined, busForRouteList, columnKeys, abbreviationOriginList, abbreviationDestinationList;
	private List<TimeTableDTO> originBusList, destinationBusList;
	private List<RouteScheduleDTO> leaveForRouteList, mainSaveList;

	private List<ColumnModel> columns, columnsLeaves;

	private boolean renderPanelTwo, disableGroupNo, disableBusCategory;
	private String origin, destination, groupNo;
	private String tripType; // Type "O" for origin and "D" for destination
	private int noOfLeaves, noOfBuses, noOfTrips, noOfDaysFortimeTable, noOfTripsForSelectedSide, tripID;
	private boolean renderNormal, renderTableTwo;

	private final int maximumLeaveForDay = 9;
	private final int maximumDays = 92;
	private boolean saveBtnDisable;
	private boolean originBusSelect, destinationBusSelect;
	private boolean swapOriginDestination;

	private List<String> leavePositionList;
	private List<TimeTableDTO> originCancelBuses;
	private List<TimeTableDTO> destinationCancelBuses;
	private boolean originDataTblDisable;
	/** buses assigned for abbreviation **/

	/** route schedule generator **/
	private List<RouteScheduleDTO> editBusForRouteList, tempEditBusForRouteList;
	private List<ColumnModel> editColumns;
	private String rotationType; // Type "O" for origin and "D" for destination
	private int noOfDaysForEdit;
	private boolean disabledNoramlRotation, disabledZigzagRotation, renderTableOne, disabledgenerateReport,
			disabledClear, disabledSave, disabledSwap, disabledleavePositionList;

	private boolean edit;
	private boolean originDataVisible;
	private boolean destinationDataVisible;
	private List<RouteScheduleDTO> busRouteListRouteSchedGen;
	private List<RouteScheduleDTO> busCategoryListRouteSchedGen;
	private StreamedContent files;
	/** route schedule generator **/
	private ChakreeyaPalaliDTO chakreeyaPalaliDTO;
	@PostConstruct
	public void init() {
		timeTableService = (TimeTableService) SpringApplicationContex.getBean("timeTableService");
		migratedService = (MigratedService) SpringApplicationContex.getBean("migratedService");
		panelGeneratorWithoutFixedTimeService = (PanelGeneratorWithoutFixedTimeService) SpringApplicationContex
				.getBean("panelGeneratorWithoutFixedTimeService");
		routeScheduleService = (RouteScheduleService) SpringApplicationContex.getBean("routeScheduleService");
		busesAssignedForAbbreviationService = (BusesAssignedForAbbreviationService) SpringApplicationContex
				.getBean("busesAssignedForAbbreviationService");
		
		loadValue();
	}

	public void loadValue() {
		/** panel gen without fixed time **/
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
		busRouteList = timeTableService.getRouteNoList();
		coupling = false;
		couplesPerBus = null;
		couplesPerBusDisable = false;
		busCategoryList = new ArrayList<TimeTableDTO>();
		;
		selectedBusCategory = null;
		editColRender = false;
		/** panel gen without fixed time **/
		/** bus assigned for abbreviation **/
		disableGroupNo = true;
		routeScheduleDTO = new RouteScheduleDTO();
		busRouteListBusAssined = new ArrayList<RouteScheduleDTO>();
		busCategoryListBusAssined = new ArrayList<RouteScheduleDTO>();
		busRouteListBusAssined = routeScheduleService.getRouteNo();
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
		/** bus assigned for abbreviation **/

		/** route schedule generator **/
		disableGroupNo = true;
		routeScheduleDTO = new RouteScheduleDTO();
		busRouteListRouteSchedGen = new ArrayList<RouteScheduleDTO>();
		busCategoryListRouteSchedGen = new ArrayList<RouteScheduleDTO>();
		mainSaveList = new ArrayList<RouteScheduleDTO>();

		busRouteListRouteSchedGen = routeScheduleService.getRouteNo();
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
		/** route schedule generator **/
	}

	/** panel gen without fixed time methods **/
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

				String restTimeString = timeTableService.getRestTime(timeTableDTO.getRouteNo());

				String tempCouplesPerBus = panelGeneratorWithoutFixedTimeService
						.retrieveCouplesForRoute(timeTableDTO.getGenereatedRefNo(), String.valueOf(groupCount));
				if (tempCouplesPerBus != null && !tempCouplesPerBus.isEmpty()
						&& !tempCouplesPerBus.trim().equalsIgnoreCase("")) {

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

				if (!couplesPerBusDisable) {
					groupManager(groupCount, restTimeString);
				} else {
					if (groupCount == 1) {
						disabledGroupTwo = true;
						disabledGroupThree = true;
						originGroupOneList = panelGeneratorWithoutFixedTimeService
								.selectDataForGroups(timeTableDTO.getGenereatedRefNo(), "1", "O");
						List<TimeTableDTO> intList = new ArrayList<>();
						for (int i = 0; i < originGroupOneList.size(); i++) {
							if (originGroupOneList.get(i).getStartTime() == null
									|| originGroupOneList.get(i).getStartTime().isEmpty()
									|| originGroupOneList.get(i).getStartTime().trim().equals("")
									|| originGroupOneList.get(i).getEndTime() == null
									|| originGroupOneList.get(i).getEndTime().isEmpty()
									|| originGroupOneList.get(i).getEndTime().trim().equals("")) {
								intList.add(originGroupOneList.get(i));
							}
						}
						for (TimeTableDTO i : intList) {
							originGroupOneList.remove(i);
						}
						List<TimeTableDTO> tempOrOneList = new ArrayList<TimeTableDTO>();
						tempOrOneList = newColumnToCheckTimeDifference(originGroupOneList);
						originGroupOneList = new ArrayList<TimeTableDTO>();
						originGroupOneList = tempOrOneList;

						groupOneDTO.setBusesOnLeaveOriginOne(panelGeneratorWithoutFixedTimeService
								.retrieveNumOfLeaves(timeTableDTO.getGenereatedRefNo(), "1", "O"));
						destinationGroupOneList = panelGeneratorWithoutFixedTimeService
								.selectDataForGroups(timeTableDTO.getGenereatedRefNo(), "1", "D");
						groupOneDTO.setBusesOnLeaveDestinationOne(panelGeneratorWithoutFixedTimeService
								.retrieveNumOfLeaves(timeTableDTO.getGenereatedRefNo(), "1", "D"));
						List<TimeTableDTO> intListD = new ArrayList<>();
						for (int i = 0; i < destinationGroupOneList.size(); i++) {
							if (destinationGroupOneList.get(i).getStartTime() == null
									|| destinationGroupOneList.get(i).getStartTime().isEmpty()
									|| destinationGroupOneList.get(i).getStartTime().trim().equals("")
									|| destinationGroupOneList.get(i).getEndTime() == null
									|| destinationGroupOneList.get(i).getEndTime().isEmpty()
									|| destinationGroupOneList.get(i).getEndTime().trim().equals("")) {
								intListD.add(destinationGroupOneList.get(i));
							}
						}
						for (TimeTableDTO i : intListD) {
							destinationGroupOneList.remove(i);
						}
						List<TimeTableDTO> tempDesOneList = new ArrayList<TimeTableDTO>();
						tempDesOneList = newColumnToCheckTimeDifference(destinationGroupOneList);
						destinationGroupOneList = new ArrayList<TimeTableDTO>();
						destinationGroupOneList = tempDesOneList;

					} else if (groupCount == 2) {
						disabledGroupThree = true;
						originGroupOneList = panelGeneratorWithoutFixedTimeService
								.selectDataForGroups(timeTableDTO.getGenereatedRefNo(), "1", "O");
						List<TimeTableDTO> intList = new ArrayList<>();
						for (int i = 0; i < originGroupOneList.size(); i++) {
							if (originGroupOneList.get(i).getStartTime() == null
									|| originGroupOneList.get(i).getStartTime().isEmpty()
									|| originGroupOneList.get(i).getStartTime().trim().equals("")
									|| originGroupOneList.get(i).getEndTime() == null
									|| originGroupOneList.get(i).getEndTime().isEmpty()
									|| originGroupOneList.get(i).getEndTime().trim().equals("")) {
								intList.add(originGroupOneList.get(i));
							}
						}
						for (TimeTableDTO i : intList) {
							originGroupOneList.remove(i);
						}
						List<TimeTableDTO> tempOrOneList = new ArrayList<TimeTableDTO>();
						tempOrOneList = newColumnToCheckTimeDifference(originGroupOneList);
						originGroupOneList = new ArrayList<TimeTableDTO>();
						originGroupOneList = tempOrOneList;

						groupOneDTO.setBusesOnLeaveOriginOne(panelGeneratorWithoutFixedTimeService
								.retrieveNumOfLeaves(timeTableDTO.getGenereatedRefNo(), "1", "O"));
						destinationGroupOneList = panelGeneratorWithoutFixedTimeService
								.selectDataForGroups(timeTableDTO.getGenereatedRefNo(), "1", "D");
						List<TimeTableDTO> intListD = new ArrayList<>();
						for (int i = 0; i < destinationGroupOneList.size(); i++) {
							if (destinationGroupOneList.get(i).getStartTime() == null
									|| destinationGroupOneList.get(i).getStartTime().isEmpty()
									|| destinationGroupOneList.get(i).getStartTime().trim().equals("")
									|| destinationGroupOneList.get(i).getEndTime() == null
									|| destinationGroupOneList.get(i).getEndTime().isEmpty()
									|| destinationGroupOneList.get(i).getEndTime().trim().equals("")) {
								intListD.add(destinationGroupOneList.get(i));
							}
						}
						for (TimeTableDTO i : intListD) {
							destinationGroupOneList.remove(i);
						}
						List<TimeTableDTO> tempDesOneList = new ArrayList<TimeTableDTO>();
						tempDesOneList = newColumnToCheckTimeDifference(destinationGroupOneList);
						destinationGroupOneList = new ArrayList<TimeTableDTO>();
						destinationGroupOneList = tempDesOneList;

						groupOneDTO.setBusesOnLeaveDestinationOne(panelGeneratorWithoutFixedTimeService
								.retrieveNumOfLeaves(timeTableDTO.getGenereatedRefNo(), "1", "D"));
						originGroupTwoList = panelGeneratorWithoutFixedTimeService
								.selectDataForGroups(timeTableDTO.getGenereatedRefNo(), "2", "O");
						List<TimeTableDTO> intListO2 = new ArrayList<>();
						for (int i = 0; i < originGroupTwoList.size(); i++) {
							if (originGroupTwoList.get(i).getStartTime() == null
									|| originGroupTwoList.get(i).getStartTime().isEmpty()
									|| originGroupTwoList.get(i).getStartTime().trim().equals("")
									|| originGroupTwoList.get(i).getEndTime() == null
									|| originGroupTwoList.get(i).getEndTime().isEmpty()
									|| originGroupTwoList.get(i).getEndTime().trim().equals("")) {
								intListO2.add(originGroupTwoList.get(i));
							}
						}
						for (TimeTableDTO i : intListO2) {
							originGroupTwoList.remove(i);
						}
						List<TimeTableDTO> tempOrTwoList = new ArrayList<TimeTableDTO>();
						tempOrTwoList = newColumnToCheckTimeDifference(originGroupTwoList);
						originGroupTwoList = new ArrayList<TimeTableDTO>();
						originGroupTwoList = tempOrTwoList;

						groupOneDTO.setBusesOnLeaveOriginTwo(panelGeneratorWithoutFixedTimeService
								.retrieveNumOfLeaves(timeTableDTO.getGenereatedRefNo(), "2", "O"));
						destinationGroupTwoList = panelGeneratorWithoutFixedTimeService
								.selectDataForGroups(timeTableDTO.getGenereatedRefNo(), "2", "D");
						List<TimeTableDTO> intListD2 = new ArrayList<>();
						for (int i = 0; i < destinationGroupTwoList.size(); i++) {
							if (destinationGroupTwoList.get(i).getStartTime() == null
									|| destinationGroupTwoList.get(i).getStartTime().isEmpty()
									|| destinationGroupTwoList.get(i).getStartTime().trim().equals("")
									|| destinationGroupTwoList.get(i).getEndTime() == null
									|| destinationGroupTwoList.get(i).getEndTime().isEmpty()
									|| destinationGroupTwoList.get(i).getEndTime().trim().equals("")) {
								intListD2.add(destinationGroupTwoList.get(i));
							}
						}
						for (TimeTableDTO i : intListD2) {
							destinationGroupTwoList.remove(i);
						}
						List<TimeTableDTO> tempDesTwoList = new ArrayList<TimeTableDTO>();
						tempDesTwoList = newColumnToCheckTimeDifference(destinationGroupTwoList);
						destinationGroupTwoList = new ArrayList<TimeTableDTO>();
						destinationGroupTwoList = tempDesTwoList;

						groupOneDTO.setBusesOnLeaveDestinationTwo(panelGeneratorWithoutFixedTimeService
								.retrieveNumOfLeaves(timeTableDTO.getGenereatedRefNo(), "2", "D"));
					} else if (groupCount == 3) {
						originGroupOneList = panelGeneratorWithoutFixedTimeService
								.selectDataForGroups(timeTableDTO.getGenereatedRefNo(), "1", "O");
						groupOneDTO.setBusesOnLeaveOriginOne(panelGeneratorWithoutFixedTimeService
								.retrieveNumOfLeaves(timeTableDTO.getGenereatedRefNo(), "1", "O"));
						List<TimeTableDTO> intList = new ArrayList<>();
						for (int i = 0; i < originGroupOneList.size(); i++) {
							if (originGroupOneList.get(i).getStartTime() == null
									|| originGroupOneList.get(i).getStartTime().isEmpty()
									|| originGroupOneList.get(i).getStartTime().trim().equals("")
									|| originGroupOneList.get(i).getEndTime() == null
									|| originGroupOneList.get(i).getEndTime().isEmpty()
									|| originGroupOneList.get(i).getEndTime().trim().equals("")) {
								intList.add(originGroupOneList.get(i));
							}
						}
						for (TimeTableDTO i : intList) {
							originGroupOneList.remove(i);
						}
						List<TimeTableDTO> tempOrOneList = new ArrayList<TimeTableDTO>();
						tempOrOneList = newColumnToCheckTimeDifference(originGroupOneList);
						originGroupOneList = new ArrayList<TimeTableDTO>();
						originGroupOneList = tempOrOneList;

						destinationGroupOneList = panelGeneratorWithoutFixedTimeService
								.selectDataForGroups(timeTableDTO.getGenereatedRefNo(), "1", "D");
						List<TimeTableDTO> intListD = new ArrayList<>();
						for (int i = 0; i < destinationGroupOneList.size(); i++) {
							if (destinationGroupOneList.get(i).getStartTime() == null
									|| destinationGroupOneList.get(i).getStartTime().isEmpty()
									|| destinationGroupOneList.get(i).getStartTime().trim().equals("")
									|| destinationGroupOneList.get(i).getEndTime() == null
									|| destinationGroupOneList.get(i).getEndTime().isEmpty()
									|| destinationGroupOneList.get(i).getEndTime().trim().equals("")) {
								intListD.add(destinationGroupOneList.get(i));
							}
						}
						for (TimeTableDTO i : intListD) {
							destinationGroupOneList.remove(i);
						}
						List<TimeTableDTO> tempDesOneList = new ArrayList<TimeTableDTO>();
						tempDesOneList = newColumnToCheckTimeDifference(destinationGroupOneList);
						destinationGroupOneList = new ArrayList<TimeTableDTO>();
						destinationGroupOneList = tempDesOneList;

						groupOneDTO.setBusesOnLeaveDestinationOne(panelGeneratorWithoutFixedTimeService
								.retrieveNumOfLeaves(timeTableDTO.getGenereatedRefNo(), "1", "D"));
						originGroupTwoList = panelGeneratorWithoutFixedTimeService
								.selectDataForGroups(timeTableDTO.getGenereatedRefNo(), "2", "O");
						List<TimeTableDTO> intListO2 = new ArrayList<>();
						for (int i = 0; i < originGroupTwoList.size(); i++) {
							if (originGroupTwoList.get(i).getStartTime() == null
									|| originGroupTwoList.get(i).getStartTime().isEmpty()
									|| originGroupTwoList.get(i).getStartTime().trim().equals("")
									|| originGroupTwoList.get(i).getEndTime() == null
									|| originGroupTwoList.get(i).getEndTime().isEmpty()
									|| originGroupTwoList.get(i).getEndTime().trim().equals("")) {
								intListO2.add(originGroupTwoList.get(i));
							}
						}
						for (TimeTableDTO i : intListO2) {
							originGroupTwoList.remove(i);
						}
						List<TimeTableDTO> tempOrTwoList = new ArrayList<TimeTableDTO>();
						tempOrTwoList = newColumnToCheckTimeDifference(originGroupTwoList);
						originGroupTwoList = new ArrayList<TimeTableDTO>();
						originGroupTwoList = tempOrTwoList;

						groupOneDTO.setBusesOnLeaveOriginTwo(panelGeneratorWithoutFixedTimeService
								.retrieveNumOfLeaves(timeTableDTO.getGenereatedRefNo(), "2", "O"));
						destinationGroupTwoList = panelGeneratorWithoutFixedTimeService
								.selectDataForGroups(timeTableDTO.getGenereatedRefNo(), "2", "D");
						List<TimeTableDTO> intListD2 = new ArrayList<>();
						for (int i = 0; i < destinationGroupTwoList.size(); i++) {
							if (destinationGroupTwoList.get(i).getStartTime() == null
									|| destinationGroupTwoList.get(i).getStartTime().isEmpty()
									|| destinationGroupTwoList.get(i).getStartTime().trim().equals("")
									|| destinationGroupTwoList.get(i).getEndTime() == null
									|| destinationGroupTwoList.get(i).getEndTime().isEmpty()
									|| destinationGroupTwoList.get(i).getEndTime().trim().equals("")) {
								intListD2.add(destinationGroupTwoList.get(i));
							}
						}
						for (TimeTableDTO i : intListD2) {
							destinationGroupTwoList.remove(i);
						}
						List<TimeTableDTO> tempDesTwoList = new ArrayList<TimeTableDTO>();
						tempDesTwoList = newColumnToCheckTimeDifference(destinationGroupTwoList);
						destinationGroupTwoList = new ArrayList<TimeTableDTO>();
						destinationGroupTwoList = tempDesTwoList;

						groupOneDTO.setBusesOnLeaveDestinationTwo(panelGeneratorWithoutFixedTimeService
								.retrieveNumOfLeaves(timeTableDTO.getGenereatedRefNo(), "2", "D"));
						originGroupThreeList = panelGeneratorWithoutFixedTimeService
								.selectDataForGroups(timeTableDTO.getGenereatedRefNo(), "3", "O");
						List<TimeTableDTO> intListO3 = new ArrayList<>();
						for (int i = 0; i < originGroupThreeList.size(); i++) {
							if (originGroupThreeList.get(i).getStartTime() == null
									|| originGroupThreeList.get(i).getStartTime().isEmpty()
									|| originGroupThreeList.get(i).getStartTime().trim().equals("")
									|| originGroupThreeList.get(i).getEndTime() == null
									|| originGroupThreeList.get(i).getEndTime().isEmpty()
									|| originGroupThreeList.get(i).getEndTime().trim().equals("")) {
								intListO3.add(originGroupThreeList.get(i));
							}
						}
						for (TimeTableDTO i : intListO3) {
							originGroupThreeList.remove(i);
						}
						List<TimeTableDTO> tempOrThreeList = new ArrayList<TimeTableDTO>();
						tempOrThreeList = newColumnToCheckTimeDifference(originGroupThreeList);
						originGroupThreeList = new ArrayList<TimeTableDTO>();
						originGroupThreeList = tempOrThreeList;

						groupOneDTO.setBusesOnLeaveOriginThree(panelGeneratorWithoutFixedTimeService
								.retrieveNumOfLeaves(timeTableDTO.getGenereatedRefNo(), "3", "O"));
						destinationGroupThreeList = panelGeneratorWithoutFixedTimeService
								.selectDataForGroups(timeTableDTO.getGenereatedRefNo(), "3", "D");
						List<TimeTableDTO> intListD3 = new ArrayList<>();
						for (int i = 0; i < destinationGroupThreeList.size(); i++) {
							if (destinationGroupThreeList.get(i).getStartTime() == null
									|| destinationGroupThreeList.get(i).getStartTime().isEmpty()
									|| destinationGroupThreeList.get(i).getStartTime().trim().equals("")
									|| destinationGroupThreeList.get(i).getEndTime() == null
									|| destinationGroupThreeList.get(i).getEndTime().isEmpty()
									|| destinationGroupThreeList.get(i).getEndTime().trim().equals("")) {
								intListD3.add(destinationGroupThreeList.get(i));
							}
						}
						for (TimeTableDTO i : intListD3) {
							destinationGroupThreeList.remove(i);
						}
						List<TimeTableDTO> tempDesThreeList = new ArrayList<TimeTableDTO>();
						tempDesThreeList = newColumnToCheckTimeDifference(destinationGroupThreeList);
						destinationGroupThreeList = new ArrayList<TimeTableDTO>();
						destinationGroupThreeList = tempDesThreeList;

						groupOneDTO.setBusesOnLeaveDestinationThree(panelGeneratorWithoutFixedTimeService
								.retrieveNumOfLeaves(timeTableDTO.getGenereatedRefNo(), "3", "D"));
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
				/** create destination buses start end time from origin start **/
				groupOneDestinationBusesToOrigin(driverRestTime, busRideTime);
				/** create destination buses start end time from origin start **/

				/** create origin buses start end time from destination start **/
				groupOneOriginBusesToDestination(driverRestTime, busRideTime);
				/** create origin buses start end time from destination end **/

				/** check remaining empty time slots and fill bus numbers start **/
				if (coupling) {
					checkRemainingTimeSlotsForDestinationData(driverRestTime, busRideTime);
					checkRemainingTimeSlotsForOriginData(driverRestTime, busRideTime);
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
				/** create destination buses start end time from origin start **/
				groupOneDestinationBusesToOrigin(driverRestTime, busRideTime);
				/** create destination buses start end time from origin start **/

				/** create origin buses start end time from destination start **/
				groupOneOriginBusesToDestination(driverRestTime, busRideTime);
				/** create origin buses start end time from destination end **/

				/** check remaining empty time slots and fill bus numbers start **/
				if (coupling) {
					checkRemainingTimeSlotsForDestinationData(driverRestTime, busRideTime);
					checkRemainingTimeSlotsForOriginData(driverRestTime, busRideTime);
				}
				/** check remaining empty time slots and fill bus numbers end **/
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
				/** create destination buses start end time from origin start **/
				groupTwoDestinationBusesToOrigin(driverRestTime, busRideTime);
				/** create destination buses start end time from origin start **/

				/** create origin buses start end time from destination start **/
				groupTwoOriginBusesToDestination(driverRestTime, busRideTime);
				/** create origin buses start end time from destination end **/

				/** check remaining empty time slots and fill bus numbers start **/
				if (coupling) {
					checkRemainingTimeSlotsForDestinationDataGroupTwo(driverRestTime, busRideTime);
					checkRemainingTimeSlotsForOriginDataGroupTwo(driverRestTime, busRideTime);
				}

				/** check remaining empty time slots and fill bus numbers end **/

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
				/** create destination buses start end time from origin start **/
				groupOneDestinationBusesToOrigin(driverRestTime, busRideTime);
				/** create destination buses start end time from origin start **/

				/** create origin buses start end time from destination start **/
				groupOneOriginBusesToDestination(driverRestTime, busRideTime);
				/** create origin buses start end time from destination end **/

				/** check remaining empty time slots and fill bus numbers start **/
				if (coupling) {
					checkRemainingTimeSlotsForDestinationData(driverRestTime, busRideTime);
					checkRemainingTimeSlotsForOriginData(driverRestTime, busRideTime);
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
				/** create destination buses start end time from origin start **/
				groupTwoDestinationBusesToOrigin(driverRestTime, busRideTime);
				/** create destination buses start end time from origin start **/

				/** create origin buses start end time from destination start **/
				groupTwoOriginBusesToDestination(driverRestTime, busRideTime);
				/** create origin buses start end time from destination end **/

				/** check remaining empty time slots and fill bus numbers start **/
				if (coupling) {
					checkRemainingTimeSlotsForDestinationDataGroupTwo(driverRestTime, busRideTime);
					checkRemainingTimeSlotsForOriginDataGroupTwo(driverRestTime, busRideTime);
				}

				/** check remaining empty time slots and fill bus numbers end **/
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
				/** create destination buses start end time from origin start **/
				groupThreeDestinationBusesToOrigin(driverRestTime, busRideTime);
				/** create destination buses start end time from origin start **/

				/** create origin buses start end time from destination start **/
				groupThreeOriginBusesToDestination(driverRestTime, busRideTime);
				/** create origin buses start end time from destination end **/

				/** check remaining empty time slots and fill bus numbers start **/
				if (coupling) {
					checkRemainingTimeSlotsForDestinationDataGroupThree(driverRestTime, busRideTime);
					checkRemainingTimeSlotsForOriginDataGroupThree(driverRestTime, busRideTime);
				}
				/** check remaining empty time slots and fill bus numbers end **/

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
			if (validationDto.getNoOfLeavesOrigin() > 0) {
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

		if (originGroupThreeList.size() != destinationGroupThreeList.size()) {
			validationDto = new WithoutFixedTimeValidationDTO();
			validationDto.setError("Time slots are not equal - Group Three");
			return validationDto;
		}

		int originPVTBus = panelGeneratorWithoutFixedTimeService
				.retrievePrivateBusNumbers(timeTableDTO.getGenereatedRefNo(), "3", "O");// get private bus count
		int originfixedPVTbuses = panelGeneratorWithoutFixedTimeService
				.retrieveFixedBusNumbers(timeTableDTO.getGenereatedRefNo(), "3", "O", "Y");// get fixed private bus
																							// count
		int originfixedCTBbuses = panelGeneratorWithoutFixedTimeService
				.retrieveFixedBusNumbers(timeTableDTO.getGenereatedRefNo(), "3", "O", "N");// get fixed CTB bus count

		int remaininPVTBusOrigin = originPVTBus - originfixedPVTbuses;
		int allBusesOrigin = originPVTBus + originfixedCTBbuses;

		int destinaionPVTBus = panelGeneratorWithoutFixedTimeService
				.retrievePrivateBusNumbers(timeTableDTO.getGenereatedRefNo(), "3", "D");// get private bus count
		int destinationfixedPVTbuses = panelGeneratorWithoutFixedTimeService
				.retrieveFixedBusNumbers(timeTableDTO.getGenereatedRefNo(), "3", "D", "Y");// get fixed private bus
																							// count
		int destinationfixedCTBbuses = panelGeneratorWithoutFixedTimeService
				.retrieveFixedBusNumbers(timeTableDTO.getGenereatedRefNo(), "3", "D", "N");// get fixed CTB bus count

		int remaininPVTBusDestination = destinaionPVTBus - destinationfixedPVTbuses;
		int allBusesDestinaion = destinaionPVTBus + destinationfixedCTBbuses;

		int allBuses = allBusesOrigin + allBusesDestinaion;
		int allFixedBuses = originfixedPVTbuses + originfixedCTBbuses + destinationfixedPVTbuses
				+ destinationfixedCTBbuses;

		if (coupling) {

			int couplingTimeSlots = originGroupThreeList.size() / 2;

			if (allBuses != couplingTimeSlots) {
				int noOfLeaves = (((allBusesOrigin * 2) + (allBusesDestinaion * 2)) - originGroupThreeList.size()) / 2;
				if (noOfLeaves < 0) {

					validationDto = new WithoutFixedTimeValidationDTO();
					validationDto.setError("cannot perform the operation please check time slots and bus numbers");
					return validationDto;
				} else {

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

							// check whether origin can give this amount of leaves
							if (remaininPVTBusOrigin >= intInputOrigin) {
								// can give leaves
								intInputOrigin = intInputOrigin + remainLeaves;
							}

						} else if (intInputDestinaion < intInputOrigin) {

							// check whether destination can give this amount of leaves
							if (remaininPVTBusDestination >= intInputDestinaion) {
								// can give leaves
								intInputDestinaion = intInputDestinaion + remainLeaves;
							}
						} else if ((intInputOrigin == 0 && remainLeaves > 0)
								|| (intInputDestinaion == 0 && remainLeaves > 0)) {
							if (newInputOrigin > newInputDestinaion) {
								// check whether origin can give this amount of leaves
								if (remaininPVTBusOrigin >= intInputOrigin) {
									intInputOrigin = intInputOrigin + remainLeaves;
								} else if (remaininPVTBusDestination >= intInputDestinaion) {
									// if origin non fixed PVT buses are not enough to give leaves get leaves from
									// destination
									intInputDestinaion = intInputDestinaion + remainLeaves;
								}
							} else if (newInputDestinaion > newInputOrigin) {
								// check whether destination can give this amount of leaves
								if (remaininPVTBusDestination >= intInputDestinaion) {
									intInputDestinaion = intInputDestinaion + remainLeaves;
								} else if (remaininPVTBusOrigin >= intInputOrigin) {
									// if destination non fixed PVT buses are not enough to give leaves get leaves
									// from origin
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
						}

					}
					if ((intInputOrigin + intInputDestinaion) == noOfLeaves) {

						// check the leaves can be given
						if (intInputOrigin <= remaininPVTBusOrigin && intInputDestinaion <= remaininPVTBusDestination) {
							// valid
						} else {
							if (intInputOrigin > remaininPVTBusOrigin) {

								int extraLeaves = intInputOrigin - remaininPVTBusOrigin;

								// check whether these leaves can be given from destination start
								int remainDesLeaves = remaininPVTBusDestination - intInputDestinaion;
								if (remainDesLeaves >= extraLeaves) {
									intInputOrigin = intInputOrigin + extraLeaves;
									intInputDestinaion = intInputDestinaion - extraLeaves;
								} else {

									validationDto = new WithoutFixedTimeValidationDTO();
									validationDto.setError("cannnot perform coupling for selected route - Group Three");
									return validationDto;
								}
								// check whether these leaves can be given from destination end

							} else if (intInputDestinaion > remaininPVTBusDestination) {
								int extraLeaves = intInputDestinaion - remaininPVTBusDestination;

								// check whether these leaves can be given from origin start
								int remainOriLeaves = remaininPVTBusOrigin - intInputOrigin;
								if (remainOriLeaves >= extraLeaves) {
									intInputOrigin = intInputOrigin + extraLeaves;
									intInputDestinaion = intInputDestinaion - extraLeaves;
								} else {

									validationDto = new WithoutFixedTimeValidationDTO();
									validationDto.setError("cannnot perform coupling for selected route");
									return validationDto;
								}
								// check whether these leaves can be given from origin end
							}
						}
						// check the leaves can be given

						validationDto.setNoOfLeavesOrigin(intInputOrigin);
						validationDto.setNoOfLeavesDestination(intInputDestinaion);

						groupOneDTO.setBusesOnLeaveOriginThree(intInputOrigin);
						groupOneDTO.setBusesOnLeaveDestinationThree(intInputDestinaion);

						panelGeneratorWithoutFixedTimeService.updateLeaves(timeTableDTO.getGenereatedRefNo(), "3", intInputOrigin, intInputDestinaion,
								sessionBackingBean.getLoginUser());
					}

					return validationDto;
				}
			} else {
				// can perform coupling
				int leftBuses = originGroupThreeList.size() - (couplingTimeSlots * 2);

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

		} else {

			if (allBuses == originGroupThreeList.size()) {

				validationDto = new WithoutFixedTimeValidationDTO();
				validationDto.setSuccess(true);
				return validationDto;
			}
			// check all time slots can be filled end

			// give leave start
			DecimalFormat df2 = new DecimalFormat("#.##");
			if (allBuses > originGroupThreeList.size()) {

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

						// check whether origin can give this amount of leaves
						if (remaininPVTBusOrigin >= intInputOrigin) {
							// can give leaves
							intInputOrigin = intInputOrigin + remainLeaves;
						}

					} else if (intInputDestinaion < intInputOrigin) {

						// check whether origin can give this amount of leaves
						if (remaininPVTBusDestination >= intInputDestinaion) {
							// can give leaves
							intInputDestinaion = intInputDestinaion + remainLeaves;
						}
					}
				}

				if ((intInputOrigin + intInputDestinaion) == numberOfLeaves) {
					validationDto.setNoOfLeavesOrigin(intInputOrigin);
					validationDto.setNoOfLeavesDestination(intInputDestinaion);

					groupOneDTO.setBusesOnLeaveOriginThree(intInputOrigin);
					groupOneDTO.setBusesOnLeaveDestinationThree(intInputDestinaion);

					panelGeneratorWithoutFixedTimeService.updateLeaves(timeTableDTO.getGenereatedRefNo(), "3", intInputOrigin, intInputDestinaion,
							sessionBackingBean.getLoginUser());
				}

				return validationDto;
			}
			// give leave end

			int remainingTimeSlots = originGroupThreeList.size() - allBuses;

			if (remainingTimeSlots > 0) {

				validationDto = new WithoutFixedTimeValidationDTO();
				validationDto.setError("Please perform coupling for the route - Group Three");
				return validationDto;
			}

		}

		return null;
	}

	private boolean validateBusAndRecreateGroupTwoLists(String abbreviationOrigin, String abbreviationDestination) {
		WithoutFixedTimeValidationDTO validationDto = new WithoutFixedTimeValidationDTO();
		validationDto = validateBusCountGroupTwo();

		if (validationDto != null && !validationDto.isSuccess()) {
			if (validationDto.getError() != null && !validationDto.getError().isEmpty()
					&& !validationDto.getError().trim().equals("")) {
				
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
			if (validationDto.getNoOfLeavesOrigin() > 0) {
				// get non fixed destination buses and remove this amount of buses from
				// destination list
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
				// get non fixed origin buses and remove this amount of buses from destination
				// list
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
				.retrievePrivateBusNumbers(timeTableDTO.getGenereatedRefNo(), "2", "O");// get private bus count
		int originfixedPVTbuses = panelGeneratorWithoutFixedTimeService
				.retrieveFixedBusNumbers(timeTableDTO.getGenereatedRefNo(), "2", "O", "Y");// get fixed private bus
																							// count
		int originfixedCTBbuses = panelGeneratorWithoutFixedTimeService
				.retrieveFixedBusNumbers(timeTableDTO.getGenereatedRefNo(), "2", "O", "N");// get fixed CTB bus count

		int remaininPVTBusOrigin = originPVTBus - originfixedPVTbuses;
		int allBusesOrigin = originPVTBus + originfixedCTBbuses;
		// origin

		// destination
		int destinaionPVTBus = panelGeneratorWithoutFixedTimeService
				.retrievePrivateBusNumbers(timeTableDTO.getGenereatedRefNo(), "2", "D");// get private bus count
		int destinationfixedPVTbuses = panelGeneratorWithoutFixedTimeService
				.retrieveFixedBusNumbers(timeTableDTO.getGenereatedRefNo(), "2", "D", "Y");// get fixed private bus
																							// count
		int destinationfixedCTBbuses = panelGeneratorWithoutFixedTimeService
				.retrieveFixedBusNumbers(timeTableDTO.getGenereatedRefNo(), "2", "D", "N");// get fixed CTB bus count

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

							// check whether origin can give this amount of leaves
							if (remaininPVTBusOrigin >= intInputOrigin) {
								// can give leaves
								intInputOrigin = intInputOrigin + remainLeaves;
							}

						} else if (intInputDestinaion < intInputOrigin) {

							// check whether destination can give this amount of leaves
							if (remaininPVTBusDestination >= intInputDestinaion) {
								// can give leaves
								intInputDestinaion = intInputDestinaion + remainLeaves;
							}
						} else if ((intInputOrigin == 0 && remainLeaves > 0)
								|| (intInputDestinaion == 0 && remainLeaves > 0)) {
							if (newInputOrigin > newInputDestinaion) {
								// check whether origin can give this amount of leaves
								if (remaininPVTBusOrigin >= intInputOrigin) {
									intInputOrigin = intInputOrigin + remainLeaves;
								} else if (remaininPVTBusDestination >= intInputDestinaion) {
									// if origin non fixed PVT buses are not enough to give leaves get leaves from
									// destination
									intInputDestinaion = intInputDestinaion + remainLeaves;
								}
							} else if (newInputDestinaion > newInputOrigin) {
								// check whether destination can give this amount of leaves
								if (remaininPVTBusDestination >= intInputDestinaion) {
									intInputDestinaion = intInputDestinaion + remainLeaves;
								} else if (remaininPVTBusOrigin >= intInputOrigin) {
									// if destination non fixed PVT buses are not enough to give leaves get leaves
									// from origin
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
						}

					}
					if ((intInputOrigin + intInputDestinaion) == noOfLeaves) {

						// check the leaves can be given
						if (intInputOrigin <= remaininPVTBusOrigin && intInputDestinaion <= remaininPVTBusDestination) {
							// valid
						} else {
							if (intInputOrigin > remaininPVTBusOrigin) {

								int extraLeaves = intInputOrigin - remaininPVTBusOrigin;

								// check whether these leaves can be given from destination start
								int remainDesLeaves = remaininPVTBusDestination - intInputDestinaion;
								if (remainDesLeaves >= extraLeaves) {
									intInputOrigin = intInputOrigin + extraLeaves;
									intInputDestinaion = intInputDestinaion - extraLeaves;
								} else {

									validationDto = new WithoutFixedTimeValidationDTO();
									validationDto.setError("cannnot perform coupling for selected route - Group Two");
									return validationDto;
								}
								// check whether these leaves can be given from destination end

							} else if (intInputDestinaion > remaininPVTBusDestination) {
								int extraLeaves = intInputDestinaion - remaininPVTBusDestination;

								// check whether these leaves can be given from origin start
								int remainOriLeaves = remaininPVTBusOrigin - intInputOrigin;
								if (remainOriLeaves >= extraLeaves) {
									intInputOrigin = intInputOrigin + extraLeaves;
									intInputDestinaion = intInputDestinaion - extraLeaves;
								} else {

									validationDto = new WithoutFixedTimeValidationDTO();
									validationDto.setError("cannnot perform coupling for selected route - Group Two");
									return validationDto;
								}
								// check whether these leaves can be given from origin end
							}
						}
						// check the leaves can be given

						validationDto.setNoOfLeavesOrigin(intInputOrigin);
						validationDto.setNoOfLeavesDestination(intInputDestinaion);

						groupOneDTO.setBusesOnLeaveOriginTwo(intInputOrigin);
						groupOneDTO.setBusesOnLeaveDestinationTwo(intInputDestinaion);

						panelGeneratorWithoutFixedTimeService.updateLeaves(timeTableDTO.getGenereatedRefNo(), "2", intInputOrigin, intInputDestinaion,
								sessionBackingBean.getLoginUser());
					}

					return validationDto;
				}
			} else {
				// can perform coupling
				int leftBuses = originGroupTwoList.size() - (couplingTimeSlots * 2);

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

		} else {

			// if not coupling
			// check all time slots can be filled start
			if (allBuses == originGroupTwoList.size()) {

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

						// check whether origin can give this amount of leaves
						if (remaininPVTBusOrigin >= intInputOrigin) {
							// can give leaves
							intInputOrigin = intInputOrigin + remainLeaves;
						}

					} else if (intInputDestinaion < intInputOrigin) {

						// check whether origin can give this amount of leaves
						if (remaininPVTBusDestination >= intInputDestinaion) {
							// can give leaves
							intInputDestinaion = intInputDestinaion + remainLeaves;
						}
					}
				}

				if ((intInputOrigin + intInputDestinaion) == numberOfLeaves) {
					validationDto.setNoOfLeavesOrigin(intInputOrigin);
					validationDto.setNoOfLeavesDestination(intInputDestinaion);

					groupOneDTO.setBusesOnLeaveOriginTwo(intInputOrigin);
					groupOneDTO.setBusesOnLeaveDestinationTwo(intInputDestinaion);

					panelGeneratorWithoutFixedTimeService.updateLeaves(timeTableDTO.getGenereatedRefNo(), "2", intInputOrigin, intInputDestinaion,
							sessionBackingBean.getLoginUser());
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
		originGroupOneList = timeTableService.getDetailsOfFixedBuses(timeTableDTO, "1", "O"); // get time slots added
																								// and fixed buses

		int originPVTBus1 = panelGeneratorWithoutFixedTimeService
				.retrievePrivateBusNumbers(timeTableDTO.getGenereatedRefNo(), "1", "O");// get private bus count

		List<String> privateBusNumListO1 = new ArrayList<String>();
		for (int i = 0; i < originPVTBus1; i++) {
			int suffix = i + 1;
			privateBusNumListO1.add(abbreviationOrigin + suffix);
		}

		List<String> alreadyAddedprivateBusNumListO1 = new ArrayList<String>();
		alreadyAddedprivateBusNumListO1 = panelGeneratorWithoutFixedTimeService
				.retreivePrivateBusNumbersInFixedBuses(timeTableDTO, "1", "O", abbreviationOrigin);

		int remainingBusCountO1 = privateBusNumListO1.size() - alreadyAddedprivateBusNumListO1.size();// remaining
																										// private bus
																										// count to add
																										// to main list

		boolean busNumFoundO1 = false;
		List<String> tempBusNumListO1 = new ArrayList<String>();
		// fixed private buses are not in ascending order so have to check which bus
		// numbers are in fixed private buses and what to add to final list
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

		List<String> finalPVTBusesO1 = new ArrayList<String>();// private buses to add to main list
		for (int i = 0; i < remainingBusCountO1; i++) {
			finalPVTBusesO1.add(tempBusNumListO1.get(i));
		}

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
		destinationGroupOneList = timeTableService.getDetailsOfFixedBuses(timeTableDTO, "1", "D"); // get time slots
																									// added and fixed
																									// buses

		int destinationPVTBus1 = panelGeneratorWithoutFixedTimeService
				.retrievePrivateBusNumbers(timeTableDTO.getGenereatedRefNo(), "1", "D");// get private bus count

		List<String> privateBusNumListD1 = new ArrayList<String>();
		for (int i = 0; i < destinationPVTBus1; i++) {
			int suffix = i + 1;
			privateBusNumListD1.add(abbreviationDestination + suffix);
		}

		List<String> alreadyAddedprivateBusNumListD1 = new ArrayList<String>();// already added private bus numbers in
																				// fixed bus list
		alreadyAddedprivateBusNumListD1 = panelGeneratorWithoutFixedTimeService
				.retreivePrivateBusNumbersInFixedBuses(timeTableDTO, "1", "D", abbreviationDestination);

		int remainingBusCountD1 = privateBusNumListD1.size() - alreadyAddedprivateBusNumListD1.size();// remaining
																										// private bus
																										// count to add
																										// to main list

		boolean busNumFoundD1 = false;
		List<String> tempBusNumListD1 = new ArrayList<String>();
		// fixed private buses are not in ascending order so have to check which bus
		// numbers are in fixed private buses and what to add to final list
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

		List<String> finalPVTBusesD1 = new ArrayList<String>();// private buses to add to main list
		for (int i = 0; i < remainingBusCountD1; i++) {
			finalPVTBusesD1.add(tempBusNumListD1.get(i));
		}

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

		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		List<TimeTableDTO> destinationListFromOrigin = new ArrayList<TimeTableDTO>();
		for (TimeTableDTO destinationDTO : destinationGroupOneList) {
			destinationListFromOrigin.add(destinationDTO);
		}

		try {

			for (TimeTableDTO destinationDTO : destinationGroupOneList) {

				Date driverRestInDate = timeFormat.parse(driverRestTime);
				Date destinationEndTimeDate = timeFormat.parse(destinationDTO.getEndTime());
				long originStartTime = destinationEndTimeDate.getTime() + driverRestInDate.getTime();// destination bus
																										// end time +
																										// driver rest
																										// time
				String originStartTimeStr = timeFormat.format(new Date(originStartTime));

				Date originStartTimeDate = timeFormat.parse(originStartTimeStr);
				Date busRideTimeDate = timeFormat.parse(busRideTime);
				long originEndTime = originStartTimeDate.getTime() + busRideTimeDate.getTime();// origin start time +
																								// bus ride time
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

		List<TimeTableDTO> tempOriginGroupOneList = new ArrayList<TimeTableDTO>();
		for (TimeTableDTO originDTO : originGroupOneList) {
			tempOriginGroupOneList.add(originDTO);
		}

		boolean timeFound = false;
		List<TimeTableDTO> timeSlotNotFoundListD = new ArrayList<TimeTableDTO>();
		for (TimeTableDTO destinationDTO : destinationListFromOrigin) {
			timeFound = false;
			for (TimeTableDTO originDTO : originGroupOneList) {
				if (destinationDTO.getOriginStartTimeString().equals(originDTO.getStartTime())
						&& destinationDTO.getOriginEndTimeString().equals(originDTO.getEndTime())) {
					if (originDTO.isFixedTime()) {
						timeSlotNotFoundListD.add(destinationDTO); // a fixed bus is already added to the time slot
						timeFound = true;
					} else if (originDTO.getBusNo() != null && !originDTO.getBusNo().isEmpty()
							&& !originDTO.getBusNo().trim().equals("")) {
						timeSlotNotFoundListD.add(destinationDTO); // a bus is added from origin and cannot put another
																	// to the time slot
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
			Collections.sort(originGroupOneList, new Comparator<TimeTableDTO>() {
				@Override
				public int compare(TimeTableDTO u1, TimeTableDTO u2) {

					return Integer.valueOf(u2.getId()) - Integer.valueOf(u1.getId());
				}
			});

			Collections.reverse(originGroupOneList);

			for (TimeTableDTO noSlotsDTO : timeSlotNotFoundListD) {
				for (TimeTableDTO originDTO : originGroupOneList) {
					if (originDTO.getBusNo() == null || originDTO.getBusNo().isEmpty()) {

						Date startDate = timeFormat.parse(noSlotsDTO.getOriginStartTimeString());
						Date oStartDate = timeFormat.parse(originDTO.getStartTime());
						Date endDate = timeFormat.parse(noSlotsDTO.getOriginEndTimeString());
						Date oEndtDate = timeFormat.parse(originDTO.getEndTime());

						if (oStartDate.after(startDate) && oEndtDate.after(endDate)) {

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

			originGroupOneList = new ArrayList<TimeTableDTO>();
			originGroupOneList = tempOriginGroupOneList;

			// origin list order start
			Collections.sort(originGroupOneList, new Comparator<TimeTableDTO>() {
				@Override
				public int compare(TimeTableDTO u1, TimeTableDTO u2) {

					return Integer.valueOf(u2.getId()) - Integer.valueOf(u1.getId());
				}
			});

			Collections.reverse(originGroupOneList);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		/** add destination buses to origin list end **/
	}

	public void groupOneOriginBusesToDestination(String driverRestTime, String busRideTime) {
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		List<TimeTableDTO> originListFromDestination = new ArrayList<TimeTableDTO>();
		for (TimeTableDTO originDTO : originGroupOneList) {
			originListFromDestination.add(originDTO);
		}

		try {

			for (TimeTableDTO originDTO : originGroupOneList) {

				Date driverRestInDate = timeFormat.parse(driverRestTime);
				Date originEndTimeDate = timeFormat.parse(originDTO.getEndTime());
				long destinationStartTime = originEndTimeDate.getTime() + driverRestInDate.getTime();// origin bus end
																										// time + driver
																										// rest time
				String destinationStartTimeStr = timeFormat.format(new Date(destinationStartTime));

				Date destinationStartTimeDate = timeFormat.parse(destinationStartTimeStr);
				Date busRideTimeDate = timeFormat.parse(busRideTime);
				long destinationEndTime = destinationStartTimeDate.getTime() + busRideTimeDate.getTime();// destination
																											// start
																											// time +
																											// bus ride
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
						timeSlotNotFoundListO.add(originDTO); // a fixed bus is already added to the time slot
						timeFoundO = true;
					} else if (destinationDTO.getBusNo() != null && !destinationDTO.getBusNo().isEmpty()
							&& !destinationDTO.getBusNo().trim().equals("")) {
						timeSlotNotFoundListO.add(originDTO);// a bus is added from origin and cannot put another to the
																// time slot
						timeFoundO = true;
					} else {

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
			if (!timeFoundO) {
				if (originDTO.getTripType().equalsIgnoreCase("O") && originDTO.getBusNo() != null
						&& !originDTO.getBusNo().isEmpty()) {
					timeSlotNotFoundListO.add(originDTO);
				}
			}
		}

		// find time slots for not added bus numbers from origin list
		try {

			Collections.sort(destinationGroupOneList, new Comparator<TimeTableDTO>() {
				@Override
				public int compare(TimeTableDTO u1, TimeTableDTO u2) {

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

			destinationGroupOneList = new ArrayList<TimeTableDTO>();
			destinationGroupOneList = tempdestinationGroupOneList;

			// origin list order start
			Collections.sort(destinationGroupOneList, new Comparator<TimeTableDTO>() {
				@Override
				public int compare(TimeTableDTO u1, TimeTableDTO u2) {

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
		originGroupTwoList = timeTableService.getDetailsOfFixedBuses(timeTableDTO, "2", "O"); // get time slots added
																								// and fixed buses

		int originPVTBus1 = panelGeneratorWithoutFixedTimeService
				.retrievePrivateBusNumbers(timeTableDTO.getGenereatedRefNo(), "2", "O");// get private bus count
		List<String> privateBusNumListO2 = new ArrayList<String>();
		for (int i = 0; i < originPVTBus1; i++) {
			int suffix = i + 1;
			privateBusNumListO2.add(abbreviationOrigin + suffix);
		}

		List<String> alreadyAddedprivateBusNumListO2 = new ArrayList<String>();// already added private bus numbers in
																				// fixed bus list
		alreadyAddedprivateBusNumListO2 = panelGeneratorWithoutFixedTimeService
				.retreivePrivateBusNumbersInFixedBuses(timeTableDTO, "2", "O", abbreviationOrigin);

		int remainingBusCountO2 = privateBusNumListO2.size() - alreadyAddedprivateBusNumListO2.size();// remaining
																										// private bus
																										// count to add
																										// to main list

		boolean busNumFoundO2 = false;
		List<String> tempBusNumListO2 = new ArrayList<String>();
		// fixed private buses are not in ascending order so have to check which bus
		// numbers are in fixed private buses and what to add to final list
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

		List<String> finalPVTBusesO2 = new ArrayList<String>();// private buses to add to main list
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
		destinationGroupTwoList = timeTableService.getDetailsOfFixedBuses(timeTableDTO, "2", "D"); // get time slots
																									// added and fixed
																									// buses

		int destinationPVTBus1 = panelGeneratorWithoutFixedTimeService
				.retrievePrivateBusNumbers(timeTableDTO.getGenereatedRefNo(), "2", "D");// get private bus count
		List<String> privateBusNumListD2 = new ArrayList<String>();
		for (int i = 0; i < destinationPVTBus1; i++) {
			int suffix = i + 1;
			privateBusNumListD2.add(abbreviationDestination + suffix);
		}

		List<String> alreadyAddedprivateBusNumListD2 = new ArrayList<String>();// already added private bus numbers in
																				// fixed bus list
		alreadyAddedprivateBusNumListD2 = panelGeneratorWithoutFixedTimeService
				.retreivePrivateBusNumbersInFixedBuses(timeTableDTO, "2", "D", abbreviationDestination);

		int remainingBusCountD2 = privateBusNumListD2.size() - alreadyAddedprivateBusNumListD2.size();// remaining
																										// private bus
																										// count to add
																										// to main list

		boolean busNumFoundD2 = false;
		List<String> tempBusNumListD2 = new ArrayList<String>();
		// fixed private buses are not in ascending order so have to check which bus
		// numbers are in fixed private buses and what to add to final list
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

		List<String> finalPVTBusesD2 = new ArrayList<String>();// private buses to add to main list
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

			for (TimeTableDTO destinationDTO : destinationGroupTwoList) {

				Date driverRestInDate = timeFormat.parse(driverRestTime);
				Date destinationEndTimeDate = timeFormat.parse(destinationDTO.getEndTime());
				long originStartTime = destinationEndTimeDate.getTime() + driverRestInDate.getTime();// destination bus
																										// end time +
																										// driver rest
																										// time
				String originStartTimeStr = timeFormat.format(new Date(originStartTime));

				Date originStartTimeDate = timeFormat.parse(originStartTimeStr);
				Date busRideTimeDate = timeFormat.parse(busRideTime);
				long originEndTime = originStartTimeDate.getTime() + busRideTimeDate.getTime();// origin start time +
																								// bus ride time
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
						timeSlotNotFoundListD.add(destinationDTO); // a fixed bus is already added to the time slot
						timeFound = true;
					} else if (originDTO.getBusNo() != null && !originDTO.getBusNo().isEmpty()
							&& !originDTO.getBusNo().trim().equals("")) {
						timeSlotNotFoundListD.add(destinationDTO); // a bus is added from origin and cannot put another
																	// to the time slot
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

			for (TimeTableDTO originDTO : originGroupTwoList) {

				Date driverRestInDate = timeFormat.parse(driverRestTime);
				Date originEndTimeDate = timeFormat.parse(originDTO.getEndTime());
				long destinationStartTime = originEndTimeDate.getTime() + driverRestInDate.getTime();// origin bus end
																										// time + driver
																										// rest time
				String destinationStartTimeStr = timeFormat.format(new Date(destinationStartTime));

				Date destinationStartTimeDate = timeFormat.parse(destinationStartTimeStr);
				Date busRideTimeDate = timeFormat.parse(busRideTime);
				long destinationEndTime = destinationStartTimeDate.getTime() + busRideTimeDate.getTime();// destination
																											// start
																											// time +
																											// bus ride
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
						timeSlotNotFoundListO.add(originDTO); // a fixed bus is already added to the time slot
						timeFoundO = true;
					} else if (destinationDTO.getBusNo() != null && !destinationDTO.getBusNo().isEmpty()
							&& !destinationDTO.getBusNo().trim().equals("")) {
						timeSlotNotFoundListO.add(originDTO);// a bus is added from origin and cannot put another to the
																// time slot
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
		originGroupThreeList = timeTableService.getDetailsOfFixedBuses(timeTableDTO, "3", "O"); // get time slots added
																								// and fixed buses

		int originPVTBus = panelGeneratorWithoutFixedTimeService
				.retrievePrivateBusNumbers(timeTableDTO.getGenereatedRefNo(), "3", "O");// get private bus count
		List<String> privateBusNumListO3 = new ArrayList<String>();
		for (int i = 0; i < originPVTBus; i++) {
			int suffix = i + 1;
			privateBusNumListO3.add(abbreviationOrigin + suffix);
		}

		List<String> alreadyAddedprivateBusNumListO3 = new ArrayList<String>();// already added private bus numbers in
																				// fixed bus list
		alreadyAddedprivateBusNumListO3 = panelGeneratorWithoutFixedTimeService
				.retreivePrivateBusNumbersInFixedBuses(timeTableDTO, "3", "O", abbreviationOrigin);

		int remainingBusCountO3 = privateBusNumListO3.size() - alreadyAddedprivateBusNumListO3.size();// remaining
																										// private bus
																										// count to add
																										// to main list

		boolean busNumFoundO3 = false;
		List<String> tempBusNumListO3 = new ArrayList<String>();
		// fixed private buses are not in ascending order so have to check which bus
		// numbers are in fixed private buses and what to add to final list
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

		List<String> finalPVTBusesO3 = new ArrayList<String>();// private buses to add to main list
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
		destinationGroupThreeList = timeTableService.getDetailsOfFixedBuses(timeTableDTO, "3", "D"); // get time slots
																										// added and
																										// fixed buses

		int destinationPVTBus3 = panelGeneratorWithoutFixedTimeService
				.retrievePrivateBusNumbers(timeTableDTO.getGenereatedRefNo(), "3", "D");// get private bus count
		List<String> privateBusNumListD3 = new ArrayList<String>();
		for (int i = 0; i < destinationPVTBus3; i++) {
			int suffix = i + 1;
			privateBusNumListD3.add(abbreviationDestination + suffix);
		}

		List<String> alreadyAddedprivateBusNumListD3 = new ArrayList<String>();// already added private bus numbers in
																				// fixed bus list
		alreadyAddedprivateBusNumListD3 = panelGeneratorWithoutFixedTimeService
				.retreivePrivateBusNumbersInFixedBuses(timeTableDTO, "3", "D", abbreviationDestination);

		int remainingBusCountD3 = privateBusNumListD3.size() - alreadyAddedprivateBusNumListD3.size();// remaining
																										// private bus
																										// count to add
																										// to main list

		boolean busNumFoundD3 = false;
		List<String> tempBusNumListD3 = new ArrayList<String>();
		// fixed private buses are not in ascending order so have to check which bus
		// numbers are in fixed private buses and what to add to final list
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

		List<String> finalPVTBusesD3 = new ArrayList<String>();// private buses to add to main list
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

			for (TimeTableDTO destinationDTO : destinationGroupThreeList) {

				Date driverRestInDate = timeFormat.parse(driverRestTime);
				Date destinationEndTimeDate = timeFormat.parse(destinationDTO.getEndTime());
				long originStartTime = destinationEndTimeDate.getTime() + driverRestInDate.getTime();// destination bus
																										// end time +
																										// driver rest
																										// time
				String originStartTimeStr = timeFormat.format(new Date(originStartTime));

				Date originStartTimeDate = timeFormat.parse(originStartTimeStr);
				Date busRideTimeDate = timeFormat.parse(busRideTime);
				long originEndTime = originStartTimeDate.getTime() + busRideTimeDate.getTime();// origin start time +
																								// bus ride time
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
						timeSlotNotFoundListD.add(destinationDTO); // a fixed bus is already added to the time slot
						timeFound = true;
					} else if (originDTO.getBusNo() != null && !originDTO.getBusNo().isEmpty()
							&& !originDTO.getBusNo().trim().equals("")) {
						timeSlotNotFoundListD.add(destinationDTO); // a bus is added from origin and cannot put another
																	// to the time slot
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

					return Integer.valueOf(u2.getId()) - Integer.valueOf(u1.getId());
				}
			});

			Collections.reverse(originGroupThreeList);

			for (TimeTableDTO noSlotsDTO : timeSlotNotFoundListD) {
				for (TimeTableDTO originDTO : originGroupThreeList) {
					if (originDTO.getBusNo() == null || originDTO.getBusNo().isEmpty()) {

						Date startDate = timeFormat.parse(noSlotsDTO.getOriginStartTimeString());
						Date oStartDate = timeFormat.parse(originDTO.getStartTime());
						Date endDate = timeFormat.parse(noSlotsDTO.getOriginEndTimeString());
						Date oEndtDate = timeFormat.parse(originDTO.getEndTime());

						if (oStartDate.after(startDate) && oEndtDate.after(endDate)) {

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

			for (TimeTableDTO originDTO : originGroupThreeList) {

				Date driverRestInDate = timeFormat.parse(driverRestTime);
				Date originEndTimeDate = timeFormat.parse(originDTO.getEndTime());
				long destinationStartTime = originEndTimeDate.getTime() + driverRestInDate.getTime();// origin bus end
																										// time + driver
																										// rest time
				String destinationStartTimeStr = timeFormat.format(new Date(destinationStartTime));

				Date destinationStartTimeDate = timeFormat.parse(destinationStartTimeStr);
				Date busRideTimeDate = timeFormat.parse(busRideTime);
				long destinationEndTime = destinationStartTimeDate.getTime() + busRideTimeDate.getTime();// destination
																											// start
																											// time +
																											// bus ride
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
						timeSlotNotFoundListO.add(originDTO); // a fixed bus is already added to the time slot
						timeFoundO = true;
					} else if (destinationDTO.getBusNo() != null && !destinationDTO.getBusNo().isEmpty()
							&& !destinationDTO.getBusNo().trim().equals("")) {
						timeSlotNotFoundListO.add(originDTO);// a bus is added from origin and cannot put another to the
																// time slot
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
		// check destination buses in origin list and verify whether they can send to
		// origin and take back in given time slots

		Collections.sort(originGroupThreeList, new Comparator<TimeTableDTO>() {
			@Override
			public int compare(TimeTableDTO u1, TimeTableDTO u2) {

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

			TimeTableDTO tempDto = new TimeTableDTO();

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
		// check origin buses in destination list and verify whether they can send to
		// destination and take back in given time slots

		Collections.sort(destinationGroupThreeList, new Comparator<TimeTableDTO>() {
			@Override
			public int compare(TimeTableDTO u1, TimeTableDTO u2) {

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

			TimeTableDTO tempDto = new TimeTableDTO();

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
		// check destination buses in origin list and verify whether they can send to
		// origin and take back in given time slots

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
		int xxCount = coupleCount + 1;
		int yyCount = (xxCount / 2);
		boolean yyCountChange = false;
		for (int i = 0; i < coupleCount; i++) {

			TimeTableDTO tempDto = new TimeTableDTO();

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
		// check origin buses in destination list and verify whether they can send to
		// destination and take back in given time slots

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
		int xxCount = coupleCount + 1;
		int yyCount = (xxCount / 2);
		boolean yyCountChange = false;
		for (int i = 0; i < coupleCount; i++) {

			TimeTableDTO tempDto = new TimeTableDTO();

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
		// check destination buses in origin list and verify whether they can send to
		// origin and take back in given time slots

		Collections.sort(originGroupTwoList, new Comparator<TimeTableDTO>() {
			@Override
			public int compare(TimeTableDTO u1, TimeTableDTO u2) {

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

			TimeTableDTO tempDto = new TimeTableDTO();

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

		}

		for (TimeTableDTO dto : tempList1) {
			originGroupTwoList.add(dto);

		}

	}

	public void checkRemainingTimeSlotsForOriginDataGroupTwo(String driverRestTime, String busRideTime) {

		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		// check origin buses in destination list and verify whether they can send to
		// destination and take back in given time slots

		Collections.sort(destinationGroupTwoList, new Comparator<TimeTableDTO>() {
			@Override
			public int compare(TimeTableDTO u1, TimeTableDTO u2) {

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

			TimeTableDTO tempDto = new TimeTableDTO();

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

		}

		for (TimeTableDTO dto : tempList1) {
			destinationGroupTwoList.add(dto);

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

		if (validationDto != null && !validationDto.isSuccess()) {
			if (validationDto.getError() != null && !validationDto.getError().isEmpty()
					&& !validationDto.getError().trim().equals("")) {

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
			if (validationDto.getNoOfLeavesOrigin() > 0) {
				// get non fixed destination buses and remove this amount of buses from
				// destination list
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

				int keepPVTBuses = pvtList.size() - validationDto.getNoOfLeavesOrigin();
				List<TimeTableDTO> pvtListRemove = new ArrayList<TimeTableDTO>();
				int count = 0;
				for (TimeTableDTO dto : pvtList) {
					if (count >= keepPVTBuses) {
						pvtListRemove.add(pvtList.get(count));
					}
					count = count + 1;
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
				// get non fixed origin buses and remove this amount of buses from destination

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

				int keepPVTBuses = pvtList.size() - validationDto.getNoOfLeavesDestination();
				List<TimeTableDTO> pvtListRemove = new ArrayList<TimeTableDTO>();
				int count = 0;
				for (TimeTableDTO dto : pvtList) {
					if (count >= keepPVTBuses) {
						pvtListRemove.add(pvtList.get(count));
					}
					count = count + 1;
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
				.retrievePrivateBusNumbers(timeTableDTO.getGenereatedRefNo(), "1", "O");// get private bus count
		int originfixedPVTbuses = panelGeneratorWithoutFixedTimeService
				.retrieveFixedBusNumbers(timeTableDTO.getGenereatedRefNo(), "1", "O", "Y");// get fixed private bus
																							// count
		int originfixedCTBbuses = panelGeneratorWithoutFixedTimeService
				.retrieveFixedBusNumbers(timeTableDTO.getGenereatedRefNo(), "1", "O", "N");// get fixed CTB bus count

		int remaininPVTBusOrigin = originPVTBus - originfixedPVTbuses;
		int allBusesOrigin = originPVTBus + originfixedCTBbuses;
		// origin

		// destination
		int destinaionPVTBus = panelGeneratorWithoutFixedTimeService
				.retrievePrivateBusNumbers(timeTableDTO.getGenereatedRefNo(), "1", "D");// get private bus count
		int destinationfixedPVTbuses = panelGeneratorWithoutFixedTimeService
				.retrieveFixedBusNumbers(timeTableDTO.getGenereatedRefNo(), "1", "D", "Y");// get fixed private bus
																							// count
		int destinationfixedCTBbuses = panelGeneratorWithoutFixedTimeService
				.retrieveFixedBusNumbers(timeTableDTO.getGenereatedRefNo(), "1", "D", "N");// get fixed CTB bus count

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

							// check whether origin can give this amount of leaves
							if (remaininPVTBusOrigin >= intInputOrigin) {
								// can give leaves
								intInputOrigin = intInputOrigin + remainLeaves;
							}

						} else if (intInputDestinaion < intInputOrigin) {

							// check whether destination can give this amount of leaves
							if (remaininPVTBusDestination >= intInputDestinaion) {
								// can give leaves
								intInputDestinaion = intInputDestinaion + remainLeaves;
							}
						} else if ((intInputOrigin == 0 && remainLeaves > 0)
								|| (intInputDestinaion == 0 && remainLeaves > 0)) {
							if (newInputOrigin > newInputDestinaion) {
								// check whether origin can give this amount of leaves
								if (remaininPVTBusOrigin >= intInputOrigin) {
									intInputOrigin = intInputOrigin + remainLeaves;
								} else if (remaininPVTBusDestination >= intInputDestinaion) {
									// if origin non fixed PVT buses are not enough to give leaves get leaves from
									// destination
									intInputDestinaion = intInputDestinaion + remainLeaves;
								}
							} else if (newInputDestinaion > newInputOrigin) {
								// check whether destination can give this amount of leaves
								if (remaininPVTBusDestination >= intInputDestinaion) {
									intInputDestinaion = intInputDestinaion + remainLeaves;
								} else if (remaininPVTBusOrigin >= intInputOrigin) {
									// if destination non fixed PVT buses are not enough to give leaves get leaves
									// from origin
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

							// check extraLeaves are odd or even and remove buses from both origin and
							// destination
						}

					}
					if ((intInputOrigin + intInputDestinaion) == noOfLeaves) {

						// check the leaves can be given
						if (intInputOrigin <= remaininPVTBusOrigin && intInputDestinaion <= remaininPVTBusDestination) {
							// valid
						} else {
							if (intInputOrigin > remaininPVTBusOrigin) {

								int extraLeaves = intInputOrigin - remaininPVTBusOrigin;

								// check whether these leaves can be given from destination start
								int remainDesLeaves = remaininPVTBusDestination - intInputDestinaion;
								if (remainDesLeaves >= extraLeaves) {
									intInputOrigin = intInputOrigin + extraLeaves;
									intInputDestinaion = intInputDestinaion - extraLeaves;
								} else {

									validationDto = new WithoutFixedTimeValidationDTO();
									validationDto.setError("cannnot perform coupling for selected route - Group One");
									return validationDto;
								}
								// check whether these leaves can be given from destination end

							} else if (intInputDestinaion > remaininPVTBusDestination) {
								int extraLeaves = intInputDestinaion - remaininPVTBusDestination;

								// check whether these leaves can be given from origin start
								int remainOriLeaves = remaininPVTBusOrigin - intInputOrigin;
								if (remainOriLeaves >= extraLeaves) {
									intInputOrigin = intInputOrigin + extraLeaves;
									intInputDestinaion = intInputDestinaion - extraLeaves;
								} else {

									validationDto = new WithoutFixedTimeValidationDTO();
									validationDto.setError("cannnot perform coupling for selected route - Group One");
									return validationDto;
								}
								// check whether these leaves can be given from origin end
							}
						}
						// check the leaves can be given

						validationDto.setNoOfLeavesOrigin(intInputOrigin);
						validationDto.setNoOfLeavesDestination(intInputDestinaion);

						groupOneDTO.setBusesOnLeaveOriginOne(intInputOrigin);
						groupOneDTO.setBusesOnLeaveDestinationOne(intInputDestinaion);

						panelGeneratorWithoutFixedTimeService.updateLeaves(timeTableDTO.getGenereatedRefNo(), "1", intInputOrigin, intInputDestinaion,
								sessionBackingBean.getLoginUser());
					}

					return validationDto;
				}
			} else {
				// can perform coupling
				int leftBuses = originGroupOneList.size() - (couplingTimeSlots * 2);

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

				validationDto = new WithoutFixedTimeValidationDTO();
				validationDto.setSuccess(true);
				return validationDto;
			}
			// check all time slots can be filled end

			// give leave start
			DecimalFormat df2 = new DecimalFormat("#.##");
			if (allBuses > originGroupOneList.size()) {

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
				}

				if ((intInputOrigin + intInputDestinaion) == numberOfLeaves) {
					validationDto.setNoOfLeavesOrigin(intInputOrigin);
					validationDto.setNoOfLeavesDestination(intInputDestinaion);

					groupOneDTO.setBusesOnLeaveOriginOne(intInputOrigin);
					groupOneDTO.setBusesOnLeaveDestinationOne(intInputDestinaion);

					panelGeneratorWithoutFixedTimeService.updateLeaves(timeTableDTO.getGenereatedRefNo(), "1", intInputOrigin, intInputDestinaion,
							sessionBackingBean.getLoginUser());
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

		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

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

				String plusTime = travleTimeDTO.getTimeRangeEnd();// travel time

				Date startTimeDate = timeFormat.parse(startTime);
				Date plusTimeDate = timeFormat.parse(plusTime);

				long addTime = startTimeDate.getTime() + plusTimeDate.getTime();
				String finalAddedTime = timeFormat.format(new Date(addTime));

				((TimeTableDTO) event.getObject()).setEndTime(finalAddedTime);
				endTime = finalAddedTime;
			}
			/** set end time according to start time end **/

			migratedService.editTimeTableManagementBeanA(referanceNum, groupNum, tripType, duplicateBusNum, startTime,
					endTime, busNum, ((TimeTableDTO) event.getObject()).getTripIdInt(),
					((TimeTableDTO) event.getObject()).getWithFixedTimeCode(), sessionBackingBean.getLoginUser());

			/** highlight the corresponding bus data in destination start **/
			TimeTableDTO editedDestinationDTO = new TimeTableDTO();
			List<TimeTableDTO> tempDestBusListToUpdate = new ArrayList<TimeTableDTO>();
			List<TimeTableDTO> tempDestBusList = new ArrayList<TimeTableDTO>();

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

			/** change to reorder after time change start **/
			List<TimeTableDTO> tobeChangedList = new ArrayList<TimeTableDTO>();
			boolean found = false;
			for (int i = 0; i < originGroupOneList.size(); i++) {

				if (originGroupOneList.get(i).getStartTime().equals(dto.getStartTime())
						&& originGroupOneList.get(i).getEndTime().equals(dto.getEndTime())) {
					found = true;
				}

				if (found) {

					tobeChangedList.add(originGroupOneList.get(i));

				}
			}

			for (TimeTableDTO chagedDTO : tobeChangedList) {

				migratedService.editTimeTableManagementBeanB(referanceNum, groupNum, chagedDTO,
						sessionBackingBean.getLoginUser());

			}
			
			//update route schedule generator table added by thilna.d on 27-10-2021
			migratedService.updateRouteScheduleGeneratorTable(referanceNum, groupNum, duplicateBusNum, busNum, sessionBackingBean.getLoginUser());
			
			
			((TimeTableDTO) event.getObject()).setDuplicateBusNum(busNum);

		} catch (Exception ex) {
			ex.printStackTrace();
			sessionBackingBean.setMessage("Data cannot be saved");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
		} finally {

		}

		RequestContext.getCurrentInstance().update("@form,originGroupOne");
		RequestContext.getCurrentInstance().update("destinationTableGroupOne");

		RequestContext.getCurrentInstance().execute("PF('successUpdateMSG').show()");

	}

	/* Update Group One Destination Buses Details */
	public void ajaxOnRowEditDestinationOne(RowEditEvent event) {

		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
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

				String plusTime = travleTimeDTO.getTimeRangeEnd();// travel time

				Date startTimeDate = timeFormat.parse(startTime);
				Date plusTimeDate = timeFormat.parse(plusTime);

				long addTime = startTimeDate.getTime() + plusTimeDate.getTime();
				String finalAddedTime = timeFormat.format(new Date(addTime));

				((TimeTableDTO) event.getObject()).setEndTime(finalAddedTime);
				endTime = finalAddedTime;
			}
			/** set end time according to start time end **/
			
			// added by thilna.d on 28-10-2021
			migratedService.editTimeTableManagementBeanA(referanceNum, groupNum, tripType, duplicateBusNum, startTime,
					endTime, busNum, ((TimeTableDTO) event.getObject()).getTripIdInt(),
					((TimeTableDTO) event.getObject()).getWithFixedTimeCode(), sessionBackingBean.getLoginUser());
			
			/** remove highlight the corresponding bus data in destination start **/
			List<TimeTableDTO> tempDestBusList = new ArrayList<TimeTableDTO>();
			TimeTableDTO editedDestinationDTO = new TimeTableDTO();
			List<TimeTableDTO> tempDestBusListToUpdate = new ArrayList<TimeTableDTO>();

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

			/** change to reorder after time change start **/
			List<TimeTableDTO> tobeChangedList = new ArrayList<TimeTableDTO>();
			boolean found = false;
			for (int i = 0; i < destinationGroupOneList.size(); i++) {

				if (destinationGroupOneList.get(i).getStartTime().equals(dto.getStartTime())
						&& destinationGroupOneList.get(i).getEndTime().equals(dto.getEndTime())) {
					found = true;
				}

				if (found) {

					tobeChangedList.add(destinationGroupOneList.get(i));

				}
			}

			tobeChangedList.addAll(tempDestBusListToUpdate);

			for (TimeTableDTO changedDTO : tobeChangedList) {

				migratedService.editTimeTableManagementBeanB(referanceNum, "1", changedDTO,
						sessionBackingBean.getLoginUser());

			}
			
			//update route schedule generator table added by thilna.d on 27-10-2021
			migratedService.updateRouteScheduleGeneratorTable(referanceNum, groupNum, duplicateBusNum, busNum, sessionBackingBean.getLoginUser());
			
			((TimeTableDTO) event.getObject()).setDuplicateBusNum(busNum);

		} catch (Exception ex) {
			ex.printStackTrace();
			sessionBackingBean.setMessage("Data cannot be saved ");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
		} finally {

		}

		RequestContext.getCurrentInstance().update("destinationTableGroupOne");

		RequestContext.getCurrentInstance().execute("PF('successUpdateMSG').show()");
	}

	/* Update Group Two Origin Buses Details */
	public void ajaxOnRowEditOriginTwo(RowEditEvent event) {

		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
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

				String plusTime = travleTimeDTO.getTimeRangeEnd();// travel time

				Date startTimeDate = timeFormat.parse(startTime);
				Date plusTimeDate = timeFormat.parse(plusTime);

				long addTime = startTimeDate.getTime() + plusTimeDate.getTime();
				String finalAddedTime = timeFormat.format(new Date(addTime));

				((TimeTableDTO) event.getObject()).setEndTime(finalAddedTime);
				endTime = finalAddedTime;
			}
			/** set end time according to start time end **/
			
			// added by thilna.d on 28-10-2021
			migratedService.editTimeTableManagementBeanA(referanceNum, groupNum, tripType, duplicateBusNum, startTime,
					endTime, busNum, ((TimeTableDTO) event.getObject()).getTripIdInt(),
					((TimeTableDTO) event.getObject()).getWithFixedTimeCode(), sessionBackingBean.getLoginUser());

			/** highlight the corresponding bus data in destination start **/
			List<TimeTableDTO> tempDestBusList = new ArrayList<TimeTableDTO>();
			TimeTableDTO editedDestinationDTO = new TimeTableDTO();
			List<TimeTableDTO> tempDestBusListToUpdate = new ArrayList<TimeTableDTO>();

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

			/** change to reorder after time change start **/
			List<TimeTableDTO> tobeChangedList = new ArrayList<TimeTableDTO>();
			boolean found = false;
			for (int i = 0; i < originGroupTwoList.size(); i++) {

				if (originGroupTwoList.get(i).getStartTime().equals(dto.getStartTime())
						&& originGroupTwoList.get(i).getEndTime().equals(dto.getEndTime())) {
					found = true;
				}

				if (found) {

					tobeChangedList.add(originGroupTwoList.get(i));

				}
			}

			RequestContext.getCurrentInstance().update("originGroupTwo");

			for (TimeTableDTO changedDTO : tobeChangedList) {

				migratedService.editTimeTableManagementBeanB(referanceNum, groupNum, changedDTO,
						sessionBackingBean.getLoginUser());

			}
			
			//update route schedule generator table added by thilna.d on 27-10-2021
			migratedService.updateRouteScheduleGeneratorTable(referanceNum, groupNum, duplicateBusNum, busNum, sessionBackingBean.getLoginUser());
			
			((TimeTableDTO) event.getObject()).setDuplicateBusNum(busNum);
		} catch (Exception ex) {
			ex.printStackTrace();
			sessionBackingBean.setMessage("Data cannot be saved");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
		} finally {

		}

		RequestContext.getCurrentInstance().execute("PF('successUpdateMSG').show()");
	}

	public void ajaxOnRowEditDestinationTwo(RowEditEvent event) {

		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
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

				String plusTime = travleTimeDTO.getTimeRangeEnd();// travel time

				Date startTimeDate = timeFormat.parse(startTime);
				Date plusTimeDate = timeFormat.parse(plusTime);

				long addTime = startTimeDate.getTime() + plusTimeDate.getTime();
				String finalAddedTime = timeFormat.format(new Date(addTime));

				((TimeTableDTO) event.getObject()).setEndTime(finalAddedTime);
				endTime = finalAddedTime;
			}
			/** set end time according to start time end **/

			// added by thilna.d on 28-10-2021
			migratedService.editTimeTableManagementBeanA(referanceNum, groupNum, tripType, duplicateBusNum, startTime,
					endTime, busNum, ((TimeTableDTO) event.getObject()).getTripIdInt(),
					((TimeTableDTO) event.getObject()).getWithFixedTimeCode(), sessionBackingBean.getLoginUser());
						
			/** origin edit bus number according to change **/
			TimeTableDTO editedDestinationDTO = new TimeTableDTO();
			List<TimeTableDTO> tempDestBusListToUpdate = new ArrayList<TimeTableDTO>();

			for (TimeTableDTO dto : originGroupTwoList) {
				if (dto.getBusNo().equals(duplicateBusNum)) {
					editedDestinationDTO.setDuplicateBusNum(duplicateBusNum);
					editedDestinationDTO.setBusNo(busNum);
					editedDestinationDTO.setGenereatedRefNo(referanceNum);
					editedDestinationDTO.setTripType("O");
					editedDestinationDTO.setGroup("1");
					editedDestinationDTO.setStartTime(dto.getStartTime());
					editedDestinationDTO.setEndTime(dto.getEndTime());
					editedDestinationDTO.setTempStartTime(dto.getStartTime());
					editedDestinationDTO.setTempEndTime(dto.getEndTime());
					tempDestBusListToUpdate.add(editedDestinationDTO);
				}
			}

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

			/** change to reorder after time change start **/
			List<TimeTableDTO> tobeChangedList = new ArrayList<TimeTableDTO>();
			boolean found = false;
			for (int i = 0; i < destinationGroupTwoList.size(); i++) {

				if (destinationGroupTwoList.get(i).getStartTime().equals(dto.getStartTime())
						&& destinationGroupTwoList.get(i).getEndTime().equals(dto.getEndTime())) {
					found = true;
				}

				if (found) {

					tobeChangedList.add(destinationGroupTwoList.get(i));

				}
			}

			RequestContext.getCurrentInstance().update("originGroupThree");
			tobeChangedList.addAll(tempDestBusListToUpdate);

			for (TimeTableDTO changedDTO : tobeChangedList) {

				migratedService.editTimeTableManagementBeanB(referanceNum, groupNum, changedDTO,
						sessionBackingBean.getLoginUser());

			}
			
			//update route schedule generator table added by thilna.d on 27-10-2021
			migratedService.updateRouteScheduleGeneratorTable(referanceNum, groupNum, duplicateBusNum, busNum, sessionBackingBean.getLoginUser());
			
			((TimeTableDTO) event.getObject()).setDuplicateBusNum(busNum);

		} catch (Exception ex) {
			ex.printStackTrace();
			sessionBackingBean.setMessage("Data cannot be saved ");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
		} finally {

		}

		RequestContext.getCurrentInstance().execute("PF('successUpdateMSG').show()");
	}

	public void ajaxOnRowEditOriginThree(RowEditEvent event) {

		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
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

				String plusTime = travleTimeDTO.getTimeRangeEnd();// travel time

				Date startTimeDate = timeFormat.parse(startTime);
				Date plusTimeDate = timeFormat.parse(plusTime);

				long addTime = startTimeDate.getTime() + plusTimeDate.getTime();
				String finalAddedTime = timeFormat.format(new Date(addTime));

				((TimeTableDTO) event.getObject()).setEndTime(finalAddedTime);
				endTime = finalAddedTime;
			}
			/** set end time according to start time end **/

			// added by thilna.d on 28-10-2021
			migratedService.editTimeTableManagementBeanA(referanceNum, groupNum, tripType, duplicateBusNum, startTime,
					endTime, busNum, ((TimeTableDTO) event.getObject()).getTripIdInt(),
					((TimeTableDTO) event.getObject()).getWithFixedTimeCode(), sessionBackingBean.getLoginUser());
						
			/** highlight the corresponding bus data in destination start **/
			TimeTableDTO editedDestinationDTO = new TimeTableDTO();
			List<TimeTableDTO> tempDestBusListToUpdate = new ArrayList<TimeTableDTO>();
			List<TimeTableDTO> tempDestBusList = new ArrayList<TimeTableDTO>();

			/** highlight the corresponding bus data in destination start **/

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

			/** change to reorder after time change start **/
			List<TimeTableDTO> tobeChangedList = new ArrayList<TimeTableDTO>();
			boolean found = false;
			for (int i = 0; i < originGroupThreeList.size(); i++) {

				if (originGroupThreeList.get(i).getStartTime().equals(dto.getStartTime())
						&& originGroupThreeList.get(i).getEndTime().equals(dto.getEndTime())) {
					found = true;
				}

				if (found) {

					tobeChangedList.add(originGroupThreeList.get(i));

				}
			}

			RequestContext.getCurrentInstance().update("originGroupThree");

			tobeChangedList.addAll(tempDestBusListToUpdate);
			for (TimeTableDTO changeddto : tobeChangedList) {

				migratedService.editTimeTableManagementBeanB(referanceNum, groupNum, changeddto,
						sessionBackingBean.getLoginUser());

			}
			
			//update route schedule generator table added by thilna.d on 27-10-2021
			migratedService.updateRouteScheduleGeneratorTable(referanceNum, groupNum, duplicateBusNum, busNum, sessionBackingBean.getLoginUser());
			
			((TimeTableDTO) event.getObject()).setDuplicateBusNum(busNum);

		} catch (Exception ex) {
			ex.printStackTrace();
			sessionBackingBean.setMessage("Data cannot be saved ");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
		} finally {

		}

		RequestContext.getCurrentInstance().execute("PF('successUpdateMSG').show()");
	}

	public void ajaxOnRowEditDestinationThree(RowEditEvent event) {

		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
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

			if (!startTime.equals(tempStartTime)) {

				String plusTime = travleTimeDTO.getTimeRangeEnd();// travel time

				Date startTimeDate = timeFormat.parse(startTime);
				Date plusTimeDate = timeFormat.parse(plusTime);

				long addTime = startTimeDate.getTime() + plusTimeDate.getTime();
				String finalAddedTime = timeFormat.format(new Date(addTime));

				((TimeTableDTO) event.getObject()).setEndTime(finalAddedTime);
				endTime = finalAddedTime;
			}
			/** set end time according to start time end **/
			
			// added by thilna.d on 28-10-2021
			migratedService.editTimeTableManagementBeanA(referanceNum, groupNum, tripType, duplicateBusNum, startTime,
					endTime, busNum, ((TimeTableDTO) event.getObject()).getTripIdInt(),
					((TimeTableDTO) event.getObject()).getWithFixedTimeCode(), sessionBackingBean.getLoginUser());
						
			/** remove highlight the corresponding bus data in destination start **/
			List<TimeTableDTO> tempDestBusList = new ArrayList<TimeTableDTO>();

			TimeTableDTO editedDestinationDTO = new TimeTableDTO();
			List<TimeTableDTO> tempDestBusListToUpdate = new ArrayList<TimeTableDTO>();

			for (TimeTableDTO dto : originGroupTwoList) {
				if (dto.getBusNo().equals(duplicateBusNum)) {
					editedDestinationDTO.setDuplicateBusNum(duplicateBusNum);
					editedDestinationDTO.setBusNo(busNum);
					editedDestinationDTO.setGenereatedRefNo(referanceNum);
					editedDestinationDTO.setTripType("O");
					editedDestinationDTO.setGroup("1");
					editedDestinationDTO.setStartTime(dto.getStartTime());
					editedDestinationDTO.setEndTime(dto.getEndTime());
					editedDestinationDTO.setTempStartTime(dto.getStartTime());
					editedDestinationDTO.setTempEndTime(dto.getEndTime());
					tempDestBusListToUpdate.add(editedDestinationDTO);
				}
			}

			/** remove highlight the corresponding bus data in destination start **/

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

			/** change to reorder after time change start **/
			List<TimeTableDTO> tobeChangedList = new ArrayList<TimeTableDTO>();
			boolean found = false;
			for (int i = 0; i < destinationGroupThreeList.size(); i++) {

				if (destinationGroupThreeList.get(i).getStartTime().equals(dto.getStartTime())
						&& destinationGroupThreeList.get(i).getEndTime().equals(dto.getEndTime())) {
					found = true;
				}

				if (found) {

					tobeChangedList.add(destinationGroupThreeList.get(i));

				}
			}

			/** change to reorder after time change end **/

			RequestContext.getCurrentInstance().update("destinationTableGroupThree");
			tobeChangedList.addAll(tempDestBusListToUpdate);

			for (TimeTableDTO changedDto : tobeChangedList) {

				migratedService.editTimeTableManagementBeanB(referanceNum, groupNum, changedDto,
						sessionBackingBean.getLoginUser());

			}

			//update route schedule generator table added by thilna.d on 27-10-2021
			migratedService.updateRouteScheduleGeneratorTable(referanceNum, groupNum, duplicateBusNum, busNum, sessionBackingBean.getLoginUser());
			
			((TimeTableDTO) event.getObject()).setDuplicateBusNum(busNum);

		} catch (Exception ex) {
			ex.printStackTrace();
			sessionBackingBean.setMessage("Data cannot be saved ");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
		} finally {

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
		;
		selectedBusCategory = null;
		editColRender = false;

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
		routeScheduleDTO = new RouteScheduleDTO();

		originDataTblDisable = false;

	}

	public void saveAction() {

		if (groupCount == 1) {

			// 1
			panelGeneratorWithoutFixedTimeService.addDataIntoTimetableGenerator(originGroupOneList, destinationGroupOneList,
					timeTableDTO.getRouteNo(), "1", timeTableDTO.getGenereatedRefNo(), sessionBackingBean.getLoginUser(), coupling);

		} else if (groupCount == 2) {
			// 1
			panelGeneratorWithoutFixedTimeService.addDataIntoTimetableGenerator(originGroupOneList, destinationGroupOneList,
					timeTableDTO.getRouteNo(), "1", timeTableDTO.getGenereatedRefNo(), sessionBackingBean.getLoginUser(), coupling);

			// 2
			panelGeneratorWithoutFixedTimeService.addDataIntoTimetableGenerator(originGroupTwoList, destinationGroupTwoList,
					timeTableDTO.getRouteNo(), "2", timeTableDTO.getGenereatedRefNo(), sessionBackingBean.getLoginUser(), coupling);

		} else if (groupCount == 3) {
			// 1
			panelGeneratorWithoutFixedTimeService.addDataIntoTimetableGenerator(originGroupOneList, destinationGroupOneList,
					timeTableDTO.getRouteNo(), "1", timeTableDTO.getGenereatedRefNo(), sessionBackingBean.getLoginUser(), coupling);

			// 2
			panelGeneratorWithoutFixedTimeService.addDataIntoTimetableGenerator(originGroupTwoList, destinationGroupTwoList,
					timeTableDTO.getRouteNo(), "2", timeTableDTO.getGenereatedRefNo(), sessionBackingBean.getLoginUser(), coupling);

			// 3
			panelGeneratorWithoutFixedTimeService.addDataIntoTimetableGenerator(originGroupThreeList, destinationGroupThreeList,
					timeTableDTO.getRouteNo(), "3", timeTableDTO.getGenereatedRefNo(), sessionBackingBean.getLoginUser(), coupling);

		} else {

			sessionBackingBean.setMessage("Record cannot be updated.");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
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

		originDataTblDisable = false;
	}

	public List<TimeTableDTO> createAllDatesDataForStartEndTime(List<String> startEndTimes, int timeGap, String busRide,
			boolean origin) {
		List<TimeTableDTO> timeList = new ArrayList<TimeTableDTO>();

		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

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

			long diff = d2.getTime() - d1.getTime();

			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000) % 24;

			int gapInMinutes = timeGap;
			String addTimeString = busRide;//
			int loops = ((int) Duration.ofHours(diffHours).toMinutes() / gapInMinutes);
			List<LocalTime> times = new ArrayList<>(loops);
			LocalTime time = LocalTime.parse(dateStart);

			Date dt1 = timeFormat.parse(dateStart);
			Date dt2 = timeFormat.parse(busRide);
			long sumtest = dt1.getTime() + dt2.getTime();
			String endTimeFirstVal = timeFormat.format(new Date(sumtest));

			String date3 = null;
			for (int i = 1; i <= loops; i++) {
				TimeTableDTO dto = new TimeTableDTO();
				times.add(time);

				dto.setStartTime(time.toString());
				if (date3 != null && !date3.isEmpty()) {
					dto.setEndTime(date3);
				} else {
					dto.setEndTime(endTimeFirstVal);
				}

				time = time.plusMinutes(gapInMinutes);

				Date date1 = timeFormat.parse(time.toString());
				Date date2 = timeFormat.parse(addTimeString);
				long sum = date1.getTime() + date2.getTime();
				date3 = timeFormat.format(new Date(sum));

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

		// get already added data in panel genarator with fixed time for origin and
		// destination
		originGroupOneList = new ArrayList<TimeTableDTO>();
		originGroupOneList = timeTableService.getDetailsOfFixedBuses(timeTableDTO, "1", "O");

		// to generate temp bus number for without fixed time get last private bus
		// number of with fixed time
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

		// get already added data in panel genarator with fixed time for origin and
		// destination
		destinationGroupOneList = new ArrayList<TimeTableDTO>();
		destinationGroupOneList = timeTableService.getDetailsOfFixedBuses(timeTableDTO, "1", "D");

		// to generate temp bus number for without fixed time get last private bus
		// number of with fixed time
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

		// get already added data in panel genarator with fixed time for origin and
		// destination
		originGroupTwoList = new ArrayList<TimeTableDTO>();
		originGroupTwoList = timeTableService.getDetailsOfFixedBuses(timeTableDTO, "2", "O");

		// to generate temp bus number for without fixed time get last private bus
		// number of with fixed time
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

		// get already added data in panel genarator with fixed time for origin and
		// destination
		destinationGroupTwoList = new ArrayList<TimeTableDTO>();
		destinationGroupTwoList = timeTableService.getDetailsOfFixedBuses(timeTableDTO, "2", "D");

		// to generate temp bus number for without fixed time get last private bus
		// number of with fixed time
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

		// get already added data in panel genarator with fixed time for origin and
		// destination
		originGroupThreeList = new ArrayList<TimeTableDTO>();
		originGroupThreeList = timeTableService.getDetailsOfFixedBuses(timeTableDTO, "3", "O");

		// to generate temp bus number for without fixed time get last private bus
		// number of with fixed time
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

		// get already added data in panel genarator with fixed time for origin and
		// destination
		destinationGroupThreeList = new ArrayList<TimeTableDTO>();
		destinationGroupThreeList = timeTableService.getDetailsOfFixedBuses(timeTableDTO, "3", "D");

		// to generate temp bus number for without fixed time get last private bus
		// number of with fixed time
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

		// keep a duplicate temp list of finalTimeListDestination to add and remove DTOs
		// after new busNo added to destinationList
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

						LocalTime time = LocalTime.parse(oriDTO.getEndTime());
						time = time.plusMinutes(gapInMinutes);// originDTO end time + driver rest time = destination
																// departure time

						originStartTime = time.toString();

						Date busRdeTime = timeFormat.parse(addTimeString);// addTimeString is the time takes for bus
																			// ride
						Date destinationStartDate = timeFormat.parse(time.toString());
						long sum = busRdeTime.getTime() + destinationStartDate.getTime();
						originEndTime = timeFormat.format(new Date(sum));

						if (destDTO.getStartTime().equals(originStartTime)
								&& destDTO.getEndTime().equals(originEndTime)) {
							if (!destDTO.isFixedTime()) {
								TimeTableDTO tempDTO = new TimeTableDTO();
								tempDTO.setStartTime(originStartTime);
								tempDTO.setEndTime(originEndTime);
								tempDTO.setBusNo(oriDTO.getBusNo());
								tempDTO.setTripType(oriDTO.getTripType());
								tempDTO.setFixedTime(oriDTO.isFixedTime());

								tempOriginList.remove(destDTO);// remove DTO with tempBus num
								tempOriginList.add(tempDTO);// add new DTO for particular time slot
							} else {
								// TODO return the bus after this turn
								tempOriginList.add(destDTO);// TODO remove this later

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

			/** remove duplicates in list **/

			/** correction of bus numbers start **/
			int busNoSuffix = 0;// abbreviation
			List<TimeTableDTO> busNoCorrectionList = new ArrayList<TimeTableDTO>();
			List<String> busNoStringList = new ArrayList<String>();
			List<String> currBusNoList = new ArrayList<String>();
			for (TimeTableDTO data : finalAddedList) {

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

			/** if trip count=2 divide non fixed bus count send bus twice start **/
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
			/** if trip count=2 divide non fixed bus count send bus twice end **/

			// keep a duplicateBusNum
			originGroupOneList = new ArrayList<TimeTableDTO>();
			for (TimeTableDTO dto : busNoCorrectionList) {
				dto.setDuplicateBusNum(dto.getBusNo());
				dto.setGroup("1");
				dto.setTempStartTime(dto.getStartTime());
				dto.setTempEndTime(dto.getEndTime());
				originGroupOneList.add(dto);
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void detinationToOriginBusSendGroupOne(List<TimeTableDTO> finalTimeListDestination, String driverRestTime,
			String addTimeString, String abbreviation) {
		List<TimeTableDTO> tempDestinationListMissing = new ArrayList<TimeTableDTO>();

		// keep a duplicate temp list of finalTimeListDestination to add and remove DTOs
		// after new busNo added to destinationList
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

				for (TimeTableDTO destDTO : finalTimeListDestination) {

					int gapInMinutes = (int) calculateTotalMinutesForEnd(driverRestTime);

					LocalTime time = LocalTime.parse(oriDTO.getEndTime());
					time = time.plusMinutes(gapInMinutes);// originDTO end time + driver rest time = destination
															// departure time

					detinationStartTime = time.toString();

					Date busRdeTime = timeFormat.parse(addTimeString);// addTimeString is the time takes for bus ride
					Date destinationStartDate = timeFormat.parse(time.toString());
					long sum = busRdeTime.getTime() + destinationStartDate.getTime();
					destinationEndTime = timeFormat.format(new Date(sum));

					if (destDTO.getStartTime().equals(detinationStartTime)
							&& destDTO.getEndTime().equals(destinationEndTime)) {
						if (!destDTO.isFixedTime()) {
							TimeTableDTO tempDTO = new TimeTableDTO();
							tempDTO.setStartTime(detinationStartTime);
							tempDTO.setEndTime(destinationEndTime);
							tempDTO.setBusNo(oriDTO.getBusNo());
							tempDTO.setTripType(oriDTO.getTripType());
							tempDTO.setFixedTime(oriDTO.isFixedTime());
							tempDestinationList.remove(destDTO);// remove DTO with tempBus num
							tempDestinationList.add(tempDTO);// add new DTO for particular time slot

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

			// some data in tempList arent added to final list check that data and add to
			// final list
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
			// some data in tempList arent added to final list check that data and add to
			// final list

			/** remove duplicates in list **/
			List<TimeTableDTO> temp = new ArrayList<TimeTableDTO>();
			for (TimeTableDTO t : finalAddedList) {
				temp.add(t);
			}

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

			/** remove duplicates in list **/

			/** correction of bus numbers start **/
			int busNoSuffix = 0;// abbreviation
			List<TimeTableDTO> busNoCorrectionList = new ArrayList<TimeTableDTO>();
			List<String> busNoStringList = new ArrayList<String>();
			List<String> currBusNoList = new ArrayList<String>();
			for (TimeTableDTO data : finalAddedList) {

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

			/** if trip count=2 divide non fixed bus count send bus twice start **/
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
			/** if trip count=2 divide non fixed bus count send bus twice end **/

			// keep a duplicateBusNum
			destinationGroupOneList = new ArrayList<TimeTableDTO>();
			for (TimeTableDTO dto : busNoCorrectionList) {
				dto.setDuplicateBusNum(dto.getBusNo());
				dto.setGroup("1");
				dto.setTempStartTime(dto.getStartTime());
				dto.setTempEndTime(dto.getEndTime());
				destinationGroupOneList.add(dto);
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void origintoDetinationBusSendGroupTwo(List<TimeTableDTO> finalTimeListOrigin, String driverRestTime,
			String addTimeString, String abbreviation) {
		List<TimeTableDTO> tempOriginListMissing = new ArrayList<TimeTableDTO>();

		// keep a duplicate temp list of finalTimeListDestination to add and remove DTOs
		// after new busNo added to destinationList
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

					LocalTime time = LocalTime.parse(oriDTO.getEndTime());
					time = time.plusMinutes(gapInMinutes);// originDTO end time + driver rest time = destination
															// departure time

					originStartTime = time.toString();

					Date busRdeTime = timeFormat.parse(addTimeString);// addTimeString is the time takes for bus ride
					Date destinationStartDate = timeFormat.parse(time.toString());
					long sum = busRdeTime.getTime() + destinationStartDate.getTime();
					originEndTime = timeFormat.format(new Date(sum));

					if (destDTO.getStartTime().equals(originStartTime) && destDTO.getEndTime().equals(originEndTime)) {
						if (!destDTO.isFixedTime()) {
							TimeTableDTO tempDTO = new TimeTableDTO();
							tempDTO.setStartTime(originStartTime);
							tempDTO.setEndTime(originEndTime);
							tempDTO.setBusNo(oriDTO.getBusNo());
							tempDTO.setTripType(oriDTO.getTripType());
							tempDTO.setFixedTime(oriDTO.isFixedTime());

							tempOriginList.remove(destDTO);// remove DTO with tempBus num
							tempOriginList.add(tempDTO);// add new DTO for particular time slot
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

			/** remove duplicates in list **/

			/** correction of bus numbers start **/
			int busNoSuffix = 0;// abbreviation
			List<TimeTableDTO> busNoCorrectionList = new ArrayList<TimeTableDTO>();
			List<String> busNoStringList = new ArrayList<String>();
			List<String> currBusNoList = new ArrayList<String>();
			for (TimeTableDTO data : finalAddedList) {

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

			/** if trip count=2 divide non fixed bus count send bus twice start **/
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
			/** if trip count=2 divide non fixed bus count send bus twice end **/

			// keep a duplicateBusNum
			originGroupTwoList = new ArrayList<TimeTableDTO>();
			for (TimeTableDTO dto : busNoCorrectionList) {
				dto.setDuplicateBusNum(dto.getBusNo());
				dto.setGroup("2");
				dto.setTempStartTime(dto.getStartTime());
				dto.setTempEndTime(dto.getEndTime());
				originGroupTwoList.add(dto);
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void detinationToOriginBusSendGroupTwo(List<TimeTableDTO> finalTimeListDestination, String driverRestTime,
			String addTimeString, String abbreviation) {
		List<TimeTableDTO> tempDestinationListMissing = new ArrayList<TimeTableDTO>();

		// keep a duplicate temp list of finalTimeListDestination to add and remove DTOs
		// after new busNo added to destinationList
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

					LocalTime time = LocalTime.parse(oriDTO.getEndTime());
					time = time.plusMinutes(gapInMinutes);// originDTO end time + driver rest time = destination
															// departure time

					detinationStartTime = time.toString();

					Date busRdeTime = timeFormat.parse(addTimeString);// addTimeString is the time takes for bus ride
					Date destinationStartDate = timeFormat.parse(time.toString());
					long sum = busRdeTime.getTime() + destinationStartDate.getTime();
					destinationEndTime = timeFormat.format(new Date(sum));

					if (destDTO.getStartTime().equals(detinationStartTime)
							&& destDTO.getEndTime().equals(destinationEndTime)) {
						if (!destDTO.isFixedTime()) {
							TimeTableDTO tempDTO = new TimeTableDTO();
							tempDTO.setStartTime(detinationStartTime);
							tempDTO.setEndTime(destinationEndTime);
							tempDTO.setBusNo(oriDTO.getBusNo());
							tempDTO.setTripType(oriDTO.getTripType());
							tempDTO.setFixedTime(oriDTO.isFixedTime());
							tempDestinationList.remove(destDTO);// remove DTO with tempBus num
							tempDestinationList.add(tempDTO);// add new DTO for particular time slot
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

			// some data in tempList arent added to final list check that data and add to
			// final list
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
			// some data in tempList arent added to final list check that data and add to
			// final list

			/** remove duplicates in list **/
			List<TimeTableDTO> temp = new ArrayList<TimeTableDTO>();
			for (TimeTableDTO t : finalAddedList) {
				temp.add(t);
			}

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

			/** remove duplicates in list **/

			/** correction of bus numbers start **/
			int busNoSuffix = 0;// abbreviation
			List<TimeTableDTO> busNoCorrectionList = new ArrayList<TimeTableDTO>();
			List<String> busNoStringList = new ArrayList<String>();
			List<String> currBusNoList = new ArrayList<String>();
			for (TimeTableDTO data : finalAddedList) {

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
			/** if trip count=2 divide non fixed bus count send bus twice start **/
			if (coupling) {

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

				busNoCorrectionList = tempBusNoList;
			}
			/** if trip count=2 divide non fixed bus count send bus twice end **/

			// keep a duplicateBusNum
			destinationGroupTwoList = new ArrayList<TimeTableDTO>();
			for (TimeTableDTO dto : busNoCorrectionList) {
				dto.setDuplicateBusNum(dto.getBusNo());
				dto.setGroup("2");
				dto.setTempStartTime(dto.getStartTime());
				dto.setTempEndTime(dto.getEndTime());
				destinationGroupTwoList.add(dto);
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void origintoDetinationBusSendGroupThree(List<TimeTableDTO> finalTimeListOrigin, String driverRestTime,
			String addTimeString, String abbreviation) {
		List<TimeTableDTO> tempOriginListMissing = new ArrayList<TimeTableDTO>();

		// keep a duplicate temp list of finalTimeListDestination to add and remove DTOs
		// after new busNo added to destinationList
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

					LocalTime time = LocalTime.parse(oriDTO.getEndTime());
					time = time.plusMinutes(gapInMinutes);// originDTO end time + driver rest time = destination
															// departure time

					originStartTime = time.toString();

					Date busRdeTime = timeFormat.parse(addTimeString);// addTimeString is the time takes for bus ride
					Date destinationStartDate = timeFormat.parse(time.toString());
					long sum = busRdeTime.getTime() + destinationStartDate.getTime();
					originEndTime = timeFormat.format(new Date(sum));

					if (destDTO.getStartTime().equals(originStartTime) && destDTO.getEndTime().equals(originEndTime)) {
						if (!destDTO.isFixedTime()) {
							TimeTableDTO tempDTO = new TimeTableDTO();
							tempDTO.setStartTime(originStartTime);
							tempDTO.setEndTime(originEndTime);
							tempDTO.setBusNo(oriDTO.getBusNo());
							tempDTO.setTripType(oriDTO.getTripType());
							tempDTO.setFixedTime(oriDTO.isFixedTime());
							tempOriginList.remove(destDTO);// remove DTO with tempBus num
							tempOriginList.add(tempDTO);// add new DTO for particular time slot
						} else {
							// TODO return the bus after this turn
							tempOriginList.add(destDTO);// TODO remove this later

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

			/** remove duplicates in list **/

			/** correction of bus numbers start **/
			int busNoSuffix = 0;// abbreviation
			List<TimeTableDTO> busNoCorrectionList = new ArrayList<TimeTableDTO>();
			List<String> busNoStringList = new ArrayList<String>();
			List<String> currBusNoList = new ArrayList<String>();
			for (TimeTableDTO data : finalAddedList) {

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

			/** if trip count=2 divide non fixed bus count send bus twice start **/
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
			/** if trip count=2 divide non fixed bus count send bus twice end **/

			// keep a duplicateBusNum
			originGroupThreeList = new ArrayList<TimeTableDTO>();
			for (TimeTableDTO dto : busNoCorrectionList) {
				dto.setDuplicateBusNum(dto.getBusNo());
				dto.setGroup("3");
				dto.setTempStartTime(dto.getStartTime());
				dto.setTempEndTime(dto.getEndTime());
				originGroupThreeList.add(dto);
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void detinationToOriginBusSendGroupThree(List<TimeTableDTO> finalTimeListDestination, String driverRestTime,
			String addTimeString, String abbreviation) {
		List<TimeTableDTO> tempDestinationListMissing = new ArrayList<TimeTableDTO>();

		// keep a duplicate temp list of finalTimeListDestination to add and remove DTOs
		// after new busNo added to destinationList
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

					LocalTime time = LocalTime.parse(oriDTO.getEndTime());
					time = time.plusMinutes(gapInMinutes);// originDTO end time + driver rest time = destination
															// departure time

					detinationStartTime = time.toString();

					Date busRdeTime = timeFormat.parse(addTimeString);// addTimeString is the time takes for bus ride
					Date destinationStartDate = timeFormat.parse(time.toString());
					long sum = busRdeTime.getTime() + destinationStartDate.getTime();
					destinationEndTime = timeFormat.format(new Date(sum));

					if (destDTO.getStartTime().equals(detinationStartTime)
							&& destDTO.getEndTime().equals(destinationEndTime)) {
						if (!destDTO.isFixedTime()) {
							TimeTableDTO tempDTO = new TimeTableDTO();
							tempDTO.setStartTime(detinationStartTime);
							tempDTO.setEndTime(destinationEndTime);
							tempDTO.setBusNo(oriDTO.getBusNo());
							tempDTO.setTripType(oriDTO.getTripType());
							tempDTO.setFixedTime(oriDTO.isFixedTime());
							tempDestinationList.remove(destDTO);// remove DTO with tempBus num
							tempDestinationList.add(tempDTO);// add new DTO for particular time slot
						} else {
							// TODO return the bus after this turn
							tempDestinationList.add(destDTO);// TODO remove this later

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

			// some data in tempList arent added to final list check that data and add to
			// final list
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
			// some data in tempList arent added to final list check that data and add to
			// final list

			/** remove duplicates in list **/
			List<TimeTableDTO> temp = new ArrayList<TimeTableDTO>();
			for (TimeTableDTO t : finalAddedList) {
				temp.add(t);
			}

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

			/** remove duplicates in list **/

			/** correction of bus numbers start **/
			int busNoSuffix = 0;// abbreviation
			List<TimeTableDTO> busNoCorrectionList = new ArrayList<TimeTableDTO>();
			List<String> busNoStringList = new ArrayList<String>();
			List<String> currBusNoList = new ArrayList<String>();
			for (TimeTableDTO data : finalAddedList) {

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

			/** if trip count=2 divide non fixed bus count send bus twice start **/
			if (coupling) {

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

				busNoCorrectionList = tempBusNoList;
			}
			/** if trip count=2 divide non fixed bus count send bus twice end **/
			// keep a duplicateBusNum
			destinationGroupThreeList = new ArrayList<TimeTableDTO>();
			for (TimeTableDTO dto : busNoCorrectionList) {
				dto.setDuplicateBusNum(dto.getBusNo());
				dto.setGroup("3");
				dto.setTempStartTime(dto.getStartTime());
				dto.setTempEndTime(dto.getEndTime());
				destinationGroupThreeList.add(dto);
			}

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

	}

	public void removeDuplicatesInGroupTwoOrigin() {
		/** remove duplicates in list **/
		List<TimeTableDTO> temp = new ArrayList<TimeTableDTO>();
		for (TimeTableDTO t : originGroupTwoList) {
			temp.add(t);
		}

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

	}

	public void removeDuplicatesInGroupThreeOrigin() {
		/** remove duplicates in list **/
		List<TimeTableDTO> temp = new ArrayList<TimeTableDTO>();
		for (TimeTableDTO t : originGroupThreeList) {
			temp.add(t);
		}

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

	public List<TimeTableDTO> newColumnToCheckTimeDifference(List<TimeTableDTO> originalList) {
		List<TimeTableDTO> returnList = new ArrayList<TimeTableDTO>();
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		try {
			for (int i = 0; i < originalList.size(); i++) {
				if (i == 0) {
					TimeTableDTO dto = new TimeTableDTO();
					dto = originalList.get(i);
					dto.setTimeDifferece("00:00");
					returnList.add(dto);
				} else {
					String previousBusStartTime = originalList.get(i - 1).getStartTime();
					String currentBusStartTime = originalList.get(i).getStartTime();

					Date prevTimeDate = timeFormat.parse(previousBusStartTime);
					Date currTimeDate = timeFormat.parse(currentBusStartTime);

					long subTime = currTimeDate.getTime() - prevTimeDate.getTime();// i.e: 10.00-08:00 = 02:00;
					String finalSubTime = timeFormat.format(new Date(subTime));

					TimeTableDTO dto = new TimeTableDTO();
					dto = originalList.get(i);
					dto.setTimeDifferece(finalSubTime);
					returnList.add(dto);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return returnList;
	}

	/** panel gen without fixed time methods **/

	/** buses assigned for abbreviation methods **/
	public void ajaxFillbusCategoryBusAssignAbbr() {

		busCategoryListBusAssined = routeScheduleService.getBusCategoryList(routeScheduleDTO.getRouteNo());
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

		String swap = busesAssignedForAbbreviationService.retrieveOriginDestinationSwap(routeScheduleDTO.getRouteNo(),
				routeScheduleDTO.getGeneratedRefNo());
		if (swap != null && !swap.isEmpty() && !swap.trim().equalsIgnoreCase("") && swap.equals("Y")) {
			tripType = "D";
			routeScheduleDTO.setSwapEnds(true);
		} else {
			tripType = "O";
			routeScheduleDTO.setSwapEnds(false);
		}

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

	public void searchActionBusAssignedForAbb() {

		if (routeScheduleDTO.getRouteNo() != null && !routeScheduleDTO.getRouteNo().trim().equalsIgnoreCase("")) {

			if (routeScheduleDTO.getBusCategory() != null
					&& !routeScheduleDTO.getBusCategory().trim().equalsIgnoreCase("")) {

				if (routeScheduleDTO.getGroupNo() != null
						&& !routeScheduleDTO.getGroupNo().trim().equalsIgnoreCase("")) {

					if (displayOriginAndDestinationDetails() >= 0) {

						swapOriginDestination = true;

						originBusList = new ArrayList<TimeTableDTO>();
						destinationBusList = new ArrayList<TimeTableDTO>();

						if (tripType != null && !tripType.isEmpty() && !tripType.trim().equals("")) {

							originBusList = timeTableService.getAllBusNoForFixedBuses(routeScheduleDTO.getRouteNo(),
									routeScheduleDTO.getBusCategory(), "O");
							destinationBusList = timeTableService.getAllBusNoForFixedBuses(
									routeScheduleDTO.getRouteNo(), routeScheduleDTO.getBusCategory(), "D");

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

						noOfDaysFortimeTable = (int) calculateNoOfDays();

						createMainDataTable();
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

	private void tempApplyAction() {

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

		createMainDataTable();
		createLeavesDataTable();

		saveBtnDisable = true;

	}

	private void createLeavesDataTable() {

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

	private List<String> createMainDataTable() {

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

	private long calculateNoOfDays() {

		Date start_date = routeScheduleDTO.getStartDate();
		Date end_date = routeScheduleDTO.getEndDate();

		long diff = start_date.getTime() - end_date.getTime();

		long days = (diff / (1000 * 60 * 60 * 24));

		return Math.abs(days);
	}

	private void busNoManagerNormal(int totalTrips) {

		busForRouteList = new ArrayList<>();
		leaveForRouteList = new ArrayList<>();
		setTripID(0);

		int tripCounting = 0;
		List<RouteScheduleDTO> tempAllBusList = routeScheduleService
				.retrieveInsertedDataForEdit(routeScheduleDTO.getGeneratedRefNo(), tripType);
		int tempTripsNum = tempAllBusList.get(0).getTripCount();

		List<String> busNoList = new ArrayList<String>();
		busNoList = routeScheduleService.selectEditDateForAssignedBuses(routeScheduleDTO.getRouteNo(),
				routeScheduleDTO.getBusCategory(), routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(),
				tripType);

		// need to check noOfTripsForSelectedSide
		boolean firstDone = false;
		for (int nooftrips = 0; nooftrips < noOfTrips; nooftrips++) {

			List<String> stringBusNoList = new ArrayList<>();

			/*
			 * Rotate normally. Get the first number of list and send it to last index. then
			 * last index come to before the last one. likewise all index move
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

				/////// end////////

			}

			firstDone = true;
			if (tempBusNoList != null && !tempBusNoList.isEmpty() && tempBusNoList.size() != 0) {
				busNoList = tempBusNoList;
			}

			// if(stringBusNoList!=null && !stringBusNoList.isEmpty() &&
			// stringBusNoList.size()!=0) {
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
			// }

			/*****/
			for (int i = 0; i < tempTripsNum; i++) {
				if (tripCounting < tempAllBusList.size()) {

					stringBusNoList.set(i, tempAllBusList.get(tripCounting).getBusNo());
					tripCounting = tripCounting + 1;
				}
			}
			/****/

			RouteScheduleDTO dto = setBusToRouteScheduleDTO(stringBusNoList);
			busForRouteList.add(dto);
		}

		mainSaveList.addAll(busForRouteList);

		if (!busForRouteList.isEmpty()) {

			List<RouteScheduleDTO> removingListForLeaves = new ArrayList<>();

			/* Store leaves position array list object in removingListForLeaves */
			for (int c = 0; c < leavePositionList.size(); c++) {

				int e = Integer.valueOf(leavePositionList.get(c));
				int position = e - 1;
				RouteScheduleDTO dto = busForRouteList.get(position);
				removingListForLeaves.add(dto);
			}
			/* remove above DTO from busForRouteList and add to leaveForRouteList */
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

	public void onCellEdidAction() {

		getTempBusListForRouteList();

		for (RouteScheduleDTO original : tempEditBusForRouteList) {
			for (RouteScheduleDTO edited : editBusForRouteList) {

				if (original.getBusNoSeq1().equals(edited.getBusNoSeq1())) {
					if (original.getBusNo1().equalsIgnoreCase(edited.getBusNo1())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo1(), edited.getBusNo1(),
								original.getBusNoSeq1());

					}
				}

				if (original.getBusNoSeq2().equals(edited.getBusNoSeq2())) {
					if (original.getBusNo2().equalsIgnoreCase(edited.getBusNo2())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo2(), edited.getBusNo2(),
								original.getBusNoSeq2());

					}
				}

				if (original.getBusNoSeq3().equals(edited.getBusNoSeq3())) {
					if (original.getBusNo3().equalsIgnoreCase(edited.getBusNo3())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo3(), edited.getBusNo3(),
								original.getBusNoSeq3());

					}
				}

				if (original.getBusNoSeq4().equals(edited.getBusNoSeq4())) {
					if (original.getBusNo4().equalsIgnoreCase(edited.getBusNo4())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo4(), edited.getBusNo4(),
								original.getBusNoSeq4());

					}
				}

				if (original.getBusNoSeq5().equals(edited.getBusNoSeq5())) {
					if (original.getBusNo5().equalsIgnoreCase(edited.getBusNo5())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo5(), edited.getBusNo5(),
								original.getBusNoSeq5());

					}
				}

				if (original.getBusNoSeq6().equals(edited.getBusNoSeq6())) {
					if (original.getBusNo6().equalsIgnoreCase(edited.getBusNo6())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo6(), edited.getBusNo6(),
								original.getBusNoSeq6());

					}
				}

				if (original.getBusNoSeq7().equals(edited.getBusNoSeq7())) {
					if (original.getBusNo7().equalsIgnoreCase(edited.getBusNo7())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo7(), edited.getBusNo7(),
								original.getBusNoSeq7());

					}
				}

				if (original.getBusNoSeq8().equals(edited.getBusNoSeq8())) {
					if (original.getBusNo8().equalsIgnoreCase(edited.getBusNo8())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo8(), edited.getBusNo8(),
								original.getBusNoSeq8());

					}
				}

				if (original.getBusNoSeq9().equals(edited.getBusNoSeq9())) {
					if (original.getBusNo9().equalsIgnoreCase(edited.getBusNo9())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo9(), edited.getBusNo9(),
								original.getBusNoSeq9());

					}
				}

				if (original.getBusNoSeq10().equals(edited.getBusNoSeq10())) {
					if (original.getBusNo10().equalsIgnoreCase(edited.getBusNo10())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo10(), edited.getBusNo10(),
								original.getBusNoSeq10());

					}
				}

				if (original.getBusNoSeq11().equals(edited.getBusNoSeq11())) {
					if (original.getBusNo11().equalsIgnoreCase(edited.getBusNo11())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo11(), edited.getBusNo11(),
								original.getBusNoSeq11());

					}
				}

				if (original.getBusNoSeq12().equals(edited.getBusNoSeq12())) {
					if (original.getBusNo12().equalsIgnoreCase(edited.getBusNo12())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo12(), edited.getBusNo12(),
								original.getBusNoSeq12());

					}
				}

				if (original.getBusNoSeq13().equals(edited.getBusNoSeq13())) {
					if (original.getBusNo13().equalsIgnoreCase(edited.getBusNo13())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo13(), edited.getBusNo13(),
								original.getBusNoSeq13());

					}
				}

				if (original.getBusNoSeq14().equals(edited.getBusNoSeq14())) {
					if (original.getBusNo14().equalsIgnoreCase(edited.getBusNo14())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo14(), edited.getBusNo14(),
								original.getBusNoSeq14());

					}
				}

				if (original.getBusNoSeq15().equals(edited.getBusNoSeq15())) {
					if (original.getBusNo15().equalsIgnoreCase(edited.getBusNo15())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo15(), edited.getBusNo15(),
								original.getBusNoSeq15());

					}
				}

				if (original.getBusNoSeq16().equals(edited.getBusNoSeq16())) {
					if (original.getBusNo16().equalsIgnoreCase(edited.getBusNo16())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo16(), edited.getBusNo16(),
								original.getBusNoSeq16());

					}
				}

				if (original.getBusNoSeq17().equals(edited.getBusNoSeq17())) {
					if (original.getBusNo17().equalsIgnoreCase(edited.getBusNo17())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo17(), edited.getBusNo17(),
								original.getBusNoSeq17());

					}
				}

				if (original.getBusNoSeq18().equals(edited.getBusNoSeq18())) {
					if (original.getBusNo18().equalsIgnoreCase(edited.getBusNo18())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo18(), edited.getBusNo18(),
								original.getBusNoSeq18());

					}
				}

				if (original.getBusNoSeq19().equals(edited.getBusNoSeq19())) {
					if (original.getBusNo19().equalsIgnoreCase(edited.getBusNo19())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo19(), edited.getBusNo19(),
								original.getBusNoSeq19());

					}
				}

				if (original.getBusNoSeq20().equals(edited.getBusNoSeq20())) {
					if (original.getBusNo20().equalsIgnoreCase(edited.getBusNo20())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo20(), edited.getBusNo20(),
								original.getBusNoSeq20());

					}
				}

				if (original.getBusNoSeq21().equals(edited.getBusNoSeq21())) {
					if (original.getBusNo21().equalsIgnoreCase(edited.getBusNo21())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo21(), edited.getBusNo21(),
								original.getBusNoSeq21());

					}
				}

				if (original.getBusNoSeq22().equals(edited.getBusNoSeq22())) {
					if (original.getBusNo22().equalsIgnoreCase(edited.getBusNo22())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo22(), edited.getBusNo22(),
								original.getBusNoSeq22());

					}
				}

				if (original.getBusNoSeq23().equals(edited.getBusNoSeq23())) {
					if (original.getBusNo23().equalsIgnoreCase(edited.getBusNo23())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo23(), edited.getBusNo23(),
								original.getBusNoSeq23());

					}
				}

				if (original.getBusNoSeq24().equals(edited.getBusNoSeq24())) {
					if (original.getBusNo24().equalsIgnoreCase(edited.getBusNo24())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo24(), edited.getBusNo24(),
								original.getBusNoSeq24());

					}
				}

				if (original.getBusNoSeq25().equals(edited.getBusNoSeq25())) {
					if (original.getBusNo25().equalsIgnoreCase(edited.getBusNo25())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo25(), edited.getBusNo25(),
								original.getBusNoSeq25());

					}
				}

				if (original.getBusNoSeq26().equals(edited.getBusNoSeq26())) {
					if (original.getBusNo26().equalsIgnoreCase(edited.getBusNo26())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo26(), edited.getBusNo26(),
								original.getBusNoSeq26());

					}
				}

				if (original.getBusNoSeq27().equals(edited.getBusNoSeq27())) {
					if (original.getBusNo27().equalsIgnoreCase(edited.getBusNo27())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo27(), edited.getBusNo27(),
								original.getBusNoSeq27());

					}
				}

				if (original.getBusNoSeq28().equals(edited.getBusNoSeq28())) {
					if (original.getBusNo28().equalsIgnoreCase(edited.getBusNo28())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo28(), edited.getBusNo28(),
								original.getBusNoSeq28());

					}
				}

				if (original.getBusNoSeq1().equals(edited.getBusNoSeq1())) {
					if (original.getBusNo1().equalsIgnoreCase(edited.getBusNo1())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo1(), edited.getBusNo1(),
								original.getBusNoSeq1());

					}
				}

				if (original.getBusNoSeq29().equals(edited.getBusNoSeq29())) {
					if (original.getBusNo29().equalsIgnoreCase(edited.getBusNo29())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo29(), edited.getBusNo29(),
								original.getBusNoSeq29());

					}
				}

				if (original.getBusNoSeq30().equals(edited.getBusNoSeq30())) {
					if (original.getBusNo30().equalsIgnoreCase(edited.getBusNo30())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo30(), edited.getBusNo30(),
								original.getBusNoSeq30());

					}
				}

				if (original.getBusNoSeq31().equals(edited.getBusNoSeq31())) {
					if (original.getBusNo31().equalsIgnoreCase(edited.getBusNo31())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo31(), edited.getBusNo31(),
								original.getBusNoSeq31());

					}
				}

				if (original.getBusNoSeq32().equals(edited.getBusNoSeq32())) {
					if (original.getBusNo32().equalsIgnoreCase(edited.getBusNo32())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo32(), edited.getBusNo32(),
								original.getBusNoSeq32());

					}
				}

				if (original.getBusNoSeq33().equals(edited.getBusNoSeq33())) {
					if (original.getBusNo33().equalsIgnoreCase(edited.getBusNo33())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo33(), edited.getBusNo33(),
								original.getBusNoSeq33());

					}
				}

				if (original.getBusNoSeq34().equals(edited.getBusNoSeq34())) {
					if (original.getBusNo34().equalsIgnoreCase(edited.getBusNo34())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo34(), edited.getBusNo34(),
								original.getBusNoSeq34());

					}
				}

				if (original.getBusNoSeq35().equals(edited.getBusNoSeq35())) {
					if (original.getBusNo35().equalsIgnoreCase(edited.getBusNo35())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo35(), edited.getBusNo35(),
								original.getBusNoSeq35());

					}
				}

				if (original.getBusNoSeq36().equals(edited.getBusNoSeq36())) {
					if (original.getBusNo36().equalsIgnoreCase(edited.getBusNo36())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo36(), edited.getBusNo36(),
								original.getBusNoSeq36());

					}
				}

				if (original.getBusNoSeq37().equals(edited.getBusNoSeq37())) {
					if (original.getBusNo37().equalsIgnoreCase(edited.getBusNo37())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo37(), edited.getBusNo37(),
								original.getBusNoSeq37());

					}
				}

				if (original.getBusNoSeq38().equals(edited.getBusNoSeq38())) {
					if (original.getBusNo38().equalsIgnoreCase(edited.getBusNo38())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo38(), edited.getBusNo38(),
								original.getBusNoSeq38());

					}
				}

				if (original.getBusNoSeq39().equals(edited.getBusNoSeq39())) {
					if (original.getBusNo39().equalsIgnoreCase(edited.getBusNo39())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo39(), edited.getBusNo39(),
								original.getBusNoSeq39());

					}
				}

				if (original.getBusNoSeq40().equals(edited.getBusNoSeq40())) {
					if (original.getBusNo40().equalsIgnoreCase(edited.getBusNo40())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo40(), edited.getBusNo40(),
								original.getBusNoSeq40());

					}
				}

				if (original.getBusNoSeq41().equals(edited.getBusNoSeq41())) {
					if (original.getBusNo41().equalsIgnoreCase(edited.getBusNo41())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo41(), edited.getBusNo41(),
								original.getBusNoSeq41());

					}
				}

				if (original.getBusNoSeq42().equals(edited.getBusNoSeq42())) {
					if (original.getBusNo42().equalsIgnoreCase(edited.getBusNo42())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo42(), edited.getBusNo42(),
								original.getBusNoSeq42());

					}
				}

				if (original.getBusNoSeq43().equals(edited.getBusNoSeq43())) {
					if (original.getBusNo43().equalsIgnoreCase(edited.getBusNo43())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo43(), edited.getBusNo43(),
								original.getBusNoSeq43());

					}
				}

				if (original.getBusNoSeq44().equals(edited.getBusNoSeq44())) {
					if (original.getBusNo44().equalsIgnoreCase(edited.getBusNo44())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo44(), edited.getBusNo44(),
								original.getBusNoSeq44());

					}
				}

				if (original.getBusNoSeq45().equals(edited.getBusNoSeq45())) {
					if (original.getBusNo45().equalsIgnoreCase(edited.getBusNo45())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo45(), edited.getBusNo45(),
								original.getBusNoSeq45());

					}
				}

				if (original.getBusNoSeq46().equals(edited.getBusNoSeq46())) {
					if (original.getBusNo46().equalsIgnoreCase(edited.getBusNo46())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo46(), edited.getBusNo46(),
								original.getBusNoSeq46());

					}
				}

				if (original.getBusNoSeq46().equals(edited.getBusNoSeq46())) {
					if (original.getBusNo46().equalsIgnoreCase(edited.getBusNo46())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo46(), edited.getBusNo46(),
								original.getBusNoSeq46());

					}
				}

				if (original.getBusNoSeq47().equals(edited.getBusNoSeq47())) {
					if (original.getBusNo47().equalsIgnoreCase(edited.getBusNo47())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo47(), edited.getBusNo47(),
								original.getBusNoSeq47());

					}
				}

				if (original.getBusNoSeq48().equals(edited.getBusNoSeq48())) {
					if (original.getBusNo48().equalsIgnoreCase(edited.getBusNo48())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo48(), edited.getBusNo48(),
								original.getBusNoSeq48());

					}
				}

				if (original.getBusNoSeq49().equals(edited.getBusNoSeq49())) {
					if (original.getBusNo49().equalsIgnoreCase(edited.getBusNo49())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo49(), edited.getBusNo49(),
								original.getBusNoSeq49());

					}
				}

				if (original.getBusNoSeq50().equals(edited.getBusNoSeq50())) {
					if (original.getBusNo50().equalsIgnoreCase(edited.getBusNo50())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo50(), edited.getBusNo50(),
								original.getBusNoSeq50());

					}
				}

				if (original.getBusNoSeq51().equals(edited.getBusNoSeq51())) {
					if (original.getBusNo51().equalsIgnoreCase(edited.getBusNo51())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo51(), edited.getBusNo51(),
								original.getBusNoSeq51());

					}
				}

				if (original.getBusNoSeq52().equals(edited.getBusNoSeq52())) {
					if (original.getBusNo52().equalsIgnoreCase(edited.getBusNo52())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo52(), edited.getBusNo52(),
								original.getBusNoSeq52());

					}
				}

				if (original.getBusNoSeq53().equals(edited.getBusNoSeq53())) {
					if (original.getBusNo53().equalsIgnoreCase(edited.getBusNo53())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo53(), edited.getBusNo53(),
								original.getBusNoSeq53());

					}
				}

				if (original.getBusNoSeq54().equals(edited.getBusNoSeq54())) {
					if (original.getBusNo54().equalsIgnoreCase(edited.getBusNo54())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo54(), edited.getBusNo54(),
								original.getBusNoSeq54());

					}
				}

				if (original.getBusNoSeq55().equals(edited.getBusNoSeq55())) {
					if (original.getBusNo55().equalsIgnoreCase(edited.getBusNo55())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo55(), edited.getBusNo55(),
								original.getBusNoSeq55());

					}
				}

				if (original.getBusNoSeq56().equals(edited.getBusNoSeq56())) {
					if (original.getBusNo56().equalsIgnoreCase(edited.getBusNo56())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo56(), edited.getBusNo56(),
								original.getBusNoSeq56());

					}
				}

				if (original.getBusNoSeq57().equals(edited.getBusNoSeq57())) {
					if (original.getBusNo57().equalsIgnoreCase(edited.getBusNo57())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo57(), edited.getBusNo57(),
								original.getBusNoSeq57());

					}
				}

				if (original.getBusNoSeq58().equals(edited.getBusNoSeq58())) {
					if (original.getBusNo58().equalsIgnoreCase(edited.getBusNo58())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo58(), edited.getBusNo58(),
								original.getBusNoSeq58());

					}
				}

				if (original.getBusNoSeq59().equals(edited.getBusNoSeq59())) {
					if (original.getBusNo59().equalsIgnoreCase(edited.getBusNo59())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo59(), edited.getBusNo59(),
								original.getBusNoSeq59());

					}
				}

				if (original.getBusNoSeq60().equals(edited.getBusNoSeq60())) {
					if (original.getBusNo60().equalsIgnoreCase(edited.getBusNo60())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo60(), edited.getBusNo60(),
								original.getBusNoSeq60());

					}
				}

				if (original.getBusNoSeq61().equals(edited.getBusNoSeq61())) {
					if (original.getBusNo61().equalsIgnoreCase(edited.getBusNo61())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo61(), edited.getBusNo61(),
								original.getBusNoSeq61());

					}
				}

				if (original.getBusNoSeq62().equals(edited.getBusNoSeq62())) {
					if (original.getBusNo62().equalsIgnoreCase(edited.getBusNo62())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo62(), edited.getBusNo62(),
								original.getBusNoSeq62());

					}
				}

				if (original.getBusNoSeq63().equals(edited.getBusNoSeq63())) {
					if (original.getBusNo63().equalsIgnoreCase(edited.getBusNo63())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo63(), edited.getBusNo63(),
								original.getBusNoSeq63());

					}
				}

				if (original.getBusNoSeq64().equals(edited.getBusNoSeq64())) {
					if (original.getBusNo64().equalsIgnoreCase(edited.getBusNo64())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo64(), edited.getBusNo64(),
								original.getBusNoSeq64());

					}
				}

				if (original.getBusNoSeq65().equals(edited.getBusNoSeq65())) {
					if (original.getBusNo65().equalsIgnoreCase(edited.getBusNo65())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo65(), edited.getBusNo65(),
								original.getBusNoSeq65());

					}
				}

				if (original.getBusNoSeq66().equals(edited.getBusNoSeq66())) {
					if (original.getBusNo66().equalsIgnoreCase(edited.getBusNo66())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo66(), edited.getBusNo66(),
								original.getBusNoSeq66());

					}
				}

				if (original.getBusNoSeq67().equals(edited.getBusNoSeq67())) {
					if (original.getBusNo67().equalsIgnoreCase(edited.getBusNo67())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo67(), edited.getBusNo67(),
								original.getBusNoSeq67());

					}
				}

				if (original.getBusNoSeq68().equals(edited.getBusNoSeq68())) {
					if (original.getBusNo68().equalsIgnoreCase(edited.getBusNo68())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo68(), edited.getBusNo68(),
								original.getBusNoSeq68());

					}
				}

				if (original.getBusNoSeq69().equals(edited.getBusNoSeq69())) {
					if (original.getBusNo69().equalsIgnoreCase(edited.getBusNo69())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo69(), edited.getBusNo69(),
								original.getBusNoSeq69());

					}
				}

				if (original.getBusNoSeq70().equals(edited.getBusNoSeq70())) {
					if (original.getBusNo70().equalsIgnoreCase(edited.getBusNo70())) {

					} else {
						;
						editBusNumbers(original.getTripId(), original.getBusNo70(), edited.getBusNo70(),
								original.getBusNoSeq70());

					}
				}

				if (original.getBusNoSeq71().equals(edited.getBusNoSeq71())) {
					if (original.getBusNo71().equalsIgnoreCase(edited.getBusNo71())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo71(), edited.getBusNo71(),
								original.getBusNoSeq71());

					}
				}

				if (original.getBusNoSeq72().equals(edited.getBusNoSeq72())) {
					if (original.getBusNo72().equalsIgnoreCase(edited.getBusNo72())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo72(), edited.getBusNo72(),
								original.getBusNoSeq72());

					}
				}

				if (original.getBusNoSeq73().equals(edited.getBusNoSeq73())) {
					if (original.getBusNo73().equalsIgnoreCase(edited.getBusNo73())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo73(), edited.getBusNo73(),
								original.getBusNoSeq73());

					}
				}

				if (original.getBusNoSeq74().equals(edited.getBusNoSeq74())) {
					if (original.getBusNo74().equalsIgnoreCase(edited.getBusNo74())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo74(), edited.getBusNo74(),
								original.getBusNoSeq74());

					}
				}

				if (original.getBusNoSeq75().equals(edited.getBusNoSeq75())) {
					if (original.getBusNo75().equalsIgnoreCase(edited.getBusNo75())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo75(), edited.getBusNo75(),
								original.getBusNoSeq75());

					}
				}

				if (original.getBusNoSeq76().equals(edited.getBusNoSeq76())) {
					if (original.getBusNo76().equalsIgnoreCase(edited.getBusNo76())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo76(), edited.getBusNo76(),
								original.getBusNoSeq76());

					}
				}

				if (original.getBusNoSeq77().equals(edited.getBusNoSeq77())) {
					if (original.getBusNo77().equalsIgnoreCase(edited.getBusNo77())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo77(), edited.getBusNo77(),
								original.getBusNoSeq77());

					}
				}

				if (original.getBusNoSeq78().equals(edited.getBusNoSeq78())) {
					if (original.getBusNo78().equalsIgnoreCase(edited.getBusNo78())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo78(), edited.getBusNo78(),
								original.getBusNoSeq78());

					}
				}

				if (original.getBusNoSeq79().equals(edited.getBusNoSeq79())) {
					if (original.getBusNo79().equalsIgnoreCase(edited.getBusNo79())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo79(), edited.getBusNo79(),
								original.getBusNoSeq79());

					}
				}

				if (original.getBusNoSeq80().equals(edited.getBusNoSeq80())) {
					if (original.getBusNo80().equalsIgnoreCase(edited.getBusNo80())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo80(), edited.getBusNo80(),
								original.getBusNoSeq80());

					}
				}

				if (original.getBusNoSeq81().equals(edited.getBusNoSeq81())) {
					if (original.getBusNo81().equalsIgnoreCase(edited.getBusNo81())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo81(), edited.getBusNo81(),
								original.getBusNoSeq81());

					}
				}

				if (original.getBusNoSeq82().equals(edited.getBusNoSeq82())) {
					if (original.getBusNo82().equalsIgnoreCase(edited.getBusNo82())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo82(), edited.getBusNo82(),
								original.getBusNoSeq82());

					}
				}

				if (original.getBusNoSeq83().equals(edited.getBusNoSeq83())) {
					if (original.getBusNo83().equalsIgnoreCase(edited.getBusNo83())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo83(), edited.getBusNo83(),
								original.getBusNoSeq83());

					}
				}

				if (original.getBusNoSeq84().equals(edited.getBusNoSeq84())) {
					if (original.getBusNo84().equalsIgnoreCase(edited.getBusNo84())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo84(), edited.getBusNo84(),
								original.getBusNoSeq84());

					}
				}

				if (original.getBusNoSeq85().equals(edited.getBusNoSeq85())) {
					if (original.getBusNo85().equalsIgnoreCase(edited.getBusNo85())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo85(), edited.getBusNo85(),
								original.getBusNoSeq85());

					}
				}

				if (original.getBusNoSeq86().equals(edited.getBusNoSeq86())) {
					if (original.getBusNo86().equalsIgnoreCase(edited.getBusNo86())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo86(), edited.getBusNo86(),
								original.getBusNoSeq86());

					}
				}

				if (original.getBusNoSeq87().equals(edited.getBusNoSeq87())) {
					if (original.getBusNo87().equalsIgnoreCase(edited.getBusNo87())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo87(), edited.getBusNo87(),
								original.getBusNoSeq87());

					}
				}

				if (original.getBusNoSeq88().equals(edited.getBusNoSeq88())) {
					if (original.getBusNo88().equalsIgnoreCase(edited.getBusNo88())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo88(), edited.getBusNo88(),
								original.getBusNoSeq88());

					}
				}

				if (original.getBusNoSeq89().equals(edited.getBusNoSeq89())) {
					if (original.getBusNo89().equalsIgnoreCase(edited.getBusNo89())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo89(), edited.getBusNo89(),
								original.getBusNoSeq89());

					}
				}

				if (original.getBusNoSeq90().equals(edited.getBusNoSeq90())) {
					if (original.getBusNo90().equalsIgnoreCase(edited.getBusNo90())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo90(), edited.getBusNo90(),
								original.getBusNoSeq90());

					}
				}

				if (original.getBusNoSeq91().equals(edited.getBusNoSeq91())) {
					if (original.getBusNo91().equalsIgnoreCase(edited.getBusNo91())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo91(), edited.getBusNo91(),
								original.getBusNoSeq91());

					}
				}

				if (original.getBusNoSeq92().equals(edited.getBusNoSeq92())) {
					if (original.getBusNo92().equalsIgnoreCase(edited.getBusNo92())) {

					} else {

						editBusNumbers(original.getTripId(), original.getBusNo92(), edited.getBusNo92(),
								original.getBusNoSeq92());

					}
				}
			}
		}
	}

	private void editBusNumbers(String tripId, String originalBusNum, String editedBusNum, String seqNum) {

		/** add earlier data to history tablet start **/
		routeScheduleService.insertRouteGeneratorDetDataForHistory(routeScheduleDTO.getGeneratedRefNo(), null,
				originalBusNum, editedBusNum, seqNum, sessionBackingBean.getLoginUser(), tripId);
		/** add earlier data to history tablet start **/

		routeScheduleService.updateEditedBusNumbersInRoute_schedule_generator_det01(originalBusNum, editedBusNum,
				seqNum, sessionBackingBean.getLoginUser(), tripType);

	}

	public void getTempBusListForRouteList() {

		ArrayList<RouteScheduleDTO> tempTempList = new ArrayList<>();
		setTripID(0);

		int tripCounting = 0;
		noOfDaysForEdit = routeScheduleService.getNoOfDays(routeScheduleDTO.getRouteNo(),
				routeScheduleDTO.getBusCategory(), routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(),
				tripType);

		List<RouteScheduleDTO> tempAllBusList = routeScheduleService.retrieveInsertedDataForEditTimeTable(
				routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getBusCategory(), routeScheduleDTO.getGroupNo(),
				tripType, routeScheduleDTO.getRouteNo());
		int tempTripsNum = tempAllBusList.get(0).getTripCount();

		List<String> busNoList = new ArrayList<String>();
		busNoList = routeScheduleService.selectEditDateForAssignedBuses(routeScheduleDTO.getRouteNo(),
				routeScheduleDTO.getBusCategory(), routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(),
				tripType);

		tripID = 0;
		// need to check noOfTripsForSelectedSide
		boolean firstDone = false;
		for (int nooftrips = 0; nooftrips < noOfTrips; nooftrips++) {

			List<String> stringBusNoList = new ArrayList<>();

			/*
			 * Rotate normally. Get the first number of list and send it to last index. then
			 * last index come to before the last one. likewise all index move
			 */
			List<String> tempBusNoList = new ArrayList<String>();
			for (int i = 0; i < busNoList.size(); i++) {

				if (!firstDone) {
					/*
					 * String[] bus = busNoList.get(i).split("-"); String toMove = ""; boolean first
					 * = false; if(bus.length > 2) { for(int j=0;j<bus.length-1;j++) { if(!first) {
					 * first = true; toMove = bus[j]; }else { toMove = toMove +"-"+bus[j]; } } }else
					 * if(bus.length==2) { if(nooftrips>0) { toMove = bus[0] +"-"+bus[1]; }else {
					 * toMove = bus[0]; }
					 * 
					 * }else { toMove = bus[0]; }
					 */
					// tempBusNoList.add(busNoList.get(i));
					break;
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

				for (int a = 0; a < busNoList.size(); a++) { // check

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

					String bus = tempAllBusList.get(tripCounting).getBusNo() + "-"
							+ tempAllBusList.get(tripCounting).getRouteSeq();
					stringBusNoList.set(i, bus);
					tripCounting = tripCounting + 1;
				}
			}

			RouteScheduleDTO dto = setBusToRouteScheduleDTOs(stringBusNoList);
			tempTempList.add(dto);
		}

		tempEditBusForRouteList = new ArrayList<RouteScheduleDTO>();
		tempEditBusForRouteList.addAll(tempTempList);

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

	private int displayOriginAndDestinationDetails() {

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

		// if (origin_without_fixed_buses > 0) {

		// if (destination_without_fixed_buses > 0) {

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

		totalLeaveManager();

		// if (originTotalLeaves == destinationTotalLeaves) {

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

	private int totalLeaveManager() {

		RouteScheduleDTO dto = routeScheduleService.getNoOfBusesAndTripsForRoute(routeScheduleDTO.getRouteNo(),
				routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), "O");

		RouteScheduleDTO dto2 = routeScheduleService.getNoOfBusesAndTripsForRoute(routeScheduleDTO.getRouteNo(),
				routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), "D");

		if (dto.getTotalBuses() != 0 && dto.getTotaltrips() != 0) {

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

		} else {
			setErrorMessage("Can not continue the flow. No. of buses / trips are zero for origin");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

			noOfLeaves = -1; // end the flow
		}

		return noOfLeaves;

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
			/** check if bus has assidned bus no in time table gen table start **/
			String assignedBus = timeTableService.getAssignedBusnumber(dto.getBusNo(),
					routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), tripType);
			String tempBusNo = null;
			if (assignedBus != null && !assignedBus.isEmpty() && !assignedBus.trim().equals("")) {
				// dto.setBusNo(assignedBus);
				tempBusNo = assignedBus;
			} else {
				tempBusNo = dto.getBusNo();
			}
			/** check if bus has assidned bus no in time table gen table end **/
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
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo1()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo1()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo1(dto.getSelectedBusNum());
							routeDto.setFormerBusNo1(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo2()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo2()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo2(dto.getSelectedBusNum());
							routeDto.setFormerBusNo2(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo3()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo3()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo3(dto.getSelectedBusNum());
							routeDto.setFormerBusNo3(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo4()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo4()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo4(dto.getSelectedBusNum());
							routeDto.setFormerBusNo4(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo5()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo5()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo5(dto.getSelectedBusNum());
							routeDto.setFormerBusNo5(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo6()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo6()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo6(dto.getSelectedBusNum());
							routeDto.setFormerBusNo6(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo7()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo7()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo7(dto.getSelectedBusNum());
							routeDto.setFormerBusNo7(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo8()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo8()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo8(dto.getSelectedBusNum());
							routeDto.setFormerBusNo8(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo9()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo9()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo9(dto.getSelectedBusNum());
							routeDto.setFormerBusNo9(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo10()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo10()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo10(dto.getSelectedBusNum());
							routeDto.setFormerBusNo10(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo11()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo11()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo11(dto.getSelectedBusNum());
							routeDto.setFormerBusNo11(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo12()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo12()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo12(dto.getSelectedBusNum());
							routeDto.setFormerBusNo12(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo13()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo13()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo13(dto.getSelectedBusNum());
							routeDto.setFormerBusNo13(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo14()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo14()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo14(dto.getSelectedBusNum());
							routeDto.setFormerBusNo14(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo15()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo15()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo15(dto.getSelectedBusNum());
							routeDto.setFormerBusNo15(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo16()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo16()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo16(dto.getSelectedBusNum());
							routeDto.setFormerBusNo16(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo17()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo17()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo17(dto.getSelectedBusNum());
							routeDto.setFormerBusNo17(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo18()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo18()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo18(dto.getSelectedBusNum());
							routeDto.setFormerBusNo18(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo19()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo19()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo19(dto.getSelectedBusNum());
							routeDto.setFormerBusNo19(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo20()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo20()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;

							routeDto = dto2;
							routeDto.setBusNo20(dto.getSelectedBusNum());
							routeDto.setFormerBusNo20(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo21()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo21()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo21(dto.getSelectedBusNum());
							routeDto.setFormerBusNo21(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo22()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo22()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo22(dto.getSelectedBusNum());
							routeDto.setFormerBusNo22(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo23()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo23()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo23(dto.getSelectedBusNum());
							routeDto.setFormerBusNo23(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo24()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo24()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo24(dto.getSelectedBusNum());
							routeDto.setFormerBusNo24(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo25()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo25()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo25(dto.getSelectedBusNum());
							routeDto.setFormerBusNo25(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo26()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo26()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo26(dto.getSelectedBusNum());
							routeDto.setFormerBusNo26(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo27()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo27()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo27(dto.getSelectedBusNum());
							routeDto.setFormerBusNo27(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo28()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo28()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo28(dto.getSelectedBusNum());
							routeDto.setFormerBusNo28(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo29()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo29()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo29(dto.getSelectedBusNum());
							routeDto.setFormerBusNo29(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo30()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo30()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo30(dto.getSelectedBusNum());
							routeDto.setFormerBusNo30(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo31()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo31()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo31(dto.getSelectedBusNum());
							routeDto.setFormerBusNo31(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo32()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo32()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo32(dto.getSelectedBusNum());
							routeDto.setFormerBusNo32(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo33()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo33()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo33(dto.getSelectedBusNum());
							routeDto.setFormerBusNo33(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo34()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo34()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo34(dto.getSelectedBusNum());
							routeDto.setFormerBusNo34(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo35()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo35()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo35(dto.getSelectedBusNum());
							routeDto.setFormerBusNo35(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo36()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo36()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo36(dto.getSelectedBusNum());
							routeDto.setFormerBusNo36(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo37()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo37()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo37(dto.getSelectedBusNum());
							routeDto.setFormerBusNo37(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo38()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo38()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo38(dto.getSelectedBusNum());
							routeDto.setFormerBusNo38(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo39()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo39()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo39(dto.getSelectedBusNum());
							routeDto.setFormerBusNo39(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo40()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo40()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo40(dto.getSelectedBusNum());
							routeDto.setFormerBusNo40(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo41()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo41()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo41(dto.getSelectedBusNum());
							routeDto.setFormerBusNo41(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo42()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo42()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo42(dto.getSelectedBusNum());
							routeDto.setFormerBusNo42(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo43()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo43()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo43(dto.getSelectedBusNum());
							routeDto.setFormerBusNo43(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo44()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo44()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo44(dto.getSelectedBusNum());
							routeDto.setFormerBusNo44(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo45()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo45()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo45(dto.getSelectedBusNum());
							routeDto.setFormerBusNo45(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo46()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo46()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo46(dto.getSelectedBusNum());
							routeDto.setFormerBusNo46(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo47()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo47()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo47(dto.getSelectedBusNum());
							routeDto.setFormerBusNo47(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo48()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo48()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo48(dto.getSelectedBusNum());
							routeDto.setFormerBusNo48(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo49()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo49()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo49(dto.getSelectedBusNum());
							routeDto.setFormerBusNo49(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo50()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo50()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo50(dto.getSelectedBusNum());
							routeDto.setFormerBusNo50(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo51()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo51()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo51(dto.getSelectedBusNum());
							routeDto.setFormerBusNo51(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo52()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo52()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo52(dto.getSelectedBusNum());
							routeDto.setFormerBusNo52(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo53()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo53()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo53(dto.getSelectedBusNum());
							routeDto.setFormerBusNo53(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo54()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo54()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo54(dto.getSelectedBusNum());
							routeDto.setFormerBusNo54(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo55()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo55()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo55(dto.getSelectedBusNum());
							routeDto.setFormerBusNo55(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo56()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo56()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo56(dto.getSelectedBusNum());
							routeDto.setFormerBusNo56(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo57()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo57()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo57(dto.getSelectedBusNum());
							routeDto.setFormerBusNo57(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo58()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo58()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo58(dto.getSelectedBusNum());
							routeDto.setFormerBusNo58(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo59()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo59()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo59(dto.getSelectedBusNum());
							routeDto.setFormerBusNo59(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo60()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo60()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo60(dto.getSelectedBusNum());
							routeDto.setFormerBusNo60(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo61()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo61()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo61(dto.getSelectedBusNum());
							routeDto.setFormerBusNo61(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo62()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo62()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo62(dto.getSelectedBusNum());
							routeDto.setFormerBusNo62(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo63()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo63()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo63(dto.getSelectedBusNum());
							routeDto.setFormerBusNo63(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo64()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo64()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo64(dto.getSelectedBusNum());
							routeDto.setFormerBusNo64(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo65()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo65()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo65(dto.getSelectedBusNum());
							routeDto.setFormerBusNo65(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo66()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo66()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo66(dto.getSelectedBusNum());
							routeDto.setFormerBusNo66(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo67()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo67()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo67(dto.getSelectedBusNum());
							routeDto.setFormerBusNo67(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo68()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo68()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo68(dto.getSelectedBusNum());
							routeDto.setFormerBusNo68(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo69()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo69()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo69(dto.getSelectedBusNum());
							routeDto.setFormerBusNo69(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo70()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo70()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo70(dto.getSelectedBusNum());
							routeDto.setFormerBusNo70(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo71()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo71()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo71(dto.getSelectedBusNum());
							routeDto.setFormerBusNo71(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo72()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo72()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo72(dto.getSelectedBusNum());
							routeDto.setFormerBusNo72(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo73()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo73()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo73(dto.getSelectedBusNum());
							routeDto.setFormerBusNo73(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo74()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo74()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo74(dto.getSelectedBusNum());
							routeDto.setFormerBusNo74(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo75()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo75()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo75(dto.getSelectedBusNum());
							routeDto.setFormerBusNo75(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo76()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo76()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo76(dto.getSelectedBusNum());
							routeDto.setFormerBusNo76(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo77()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo77()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo77(dto.getSelectedBusNum());
							routeDto.setFormerBusNo77(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo78()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo78()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo78(dto.getSelectedBusNum());
							routeDto.setFormerBusNo78(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo79()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo79()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo79(dto.getSelectedBusNum());
							routeDto.setFormerBusNo79(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo80()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo80()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo80(dto.getSelectedBusNum());
							routeDto.setFormerBusNo80(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo81()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo81()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo81(dto.getSelectedBusNum());
							routeDto.setFormerBusNo81(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo82()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo82()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo82(dto.getSelectedBusNum());
							routeDto.setFormerBusNo82(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo83()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo83()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo83(dto.getSelectedBusNum());
							routeDto.setFormerBusNo83(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo84()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo84()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo84(dto.getSelectedBusNum());
							routeDto.setFormerBusNo84(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo85()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo85()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo85(dto.getSelectedBusNum());
							routeDto.setFormerBusNo85(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo86()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo86()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo86(dto.getSelectedBusNum());
							routeDto.setFormerBusNo86(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo87()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo87()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo87(dto.getSelectedBusNum());
							routeDto.setFormerBusNo87(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo88()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo88()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo88(dto.getSelectedBusNum());
							routeDto.setFormerBusNo88(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo89()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo89()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo89(dto.getSelectedBusNum());
							routeDto.setFormerBusNo89(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo90()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo90()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo90(dto.getSelectedBusNum());
							routeDto.setFormerBusNo90(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo91()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo91()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
							routeDto = dto2;
							routeDto.setBusNo91(dto.getSelectedBusNum());
							routeDto.setFormerBusNo91(formerBus);

							routeDto.setSwapEnds(dto.isSwapEnds());
							routeDto.setBusNoChanged(true);
							tempBusForRouteList.remove(dto2);
							tempBusForRouteList.add(routeDto);

						}
						if ((tempBusNo != null && !tempBusNo.isEmpty() && tempBusNo.equalsIgnoreCase(dto2.getBusNo92()))
								|| (dto.getCurrentSelectedBusNo() != null && !dto.getCurrentSelectedBusNo().isEmpty()
										&& dto.getCurrentSelectedBusNo().equalsIgnoreCase(dto2.getBusNo92()))) {
							RouteScheduleDTO routeDto = new RouteScheduleDTO();
							String formerBus = tempBusNo;
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

	}

	private List<String> createDataTable() {

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

	public void setBusType(RouteScheduleDTO dto1) {

		for (RouteScheduleDTO dto : abbreviationOriginList) {
			if (dto.getSelectedBusNum() != null && !dto.getSelectedBusNum().isEmpty()
					&& !dto.getSelectedBusNum().equals("") && dto.getSelectedBusNum().equals(dto1.getSelectedBusNum())
					&& !dto.getBusNo().equals(dto1.getBusNo())) {

				sessionBackingBean.setMessage("Selected Bus No. has been already assigned to an Abbreviation");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
			}
		}

		for (RouteScheduleDTO dto : abbreviationDestinationList) {
			if (dto.getSelectedBusNum() != null && !dto.getSelectedBusNum().isEmpty()
					&& !dto.getSelectedBusNum().equals("") && dto.getSelectedBusNum().equals(dto1.getSelectedBusNum())
					&& !dto.getBusNo().equals(dto1.getBusNo())) {

				sessionBackingBean.setMessage("Selected Bus No. has been already assigned to an Abbreviation");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
			}
		}
	}

	public void setBusTypeDest(RouteScheduleDTO dto1) {

		for (RouteScheduleDTO dto : abbreviationDestinationList) {
			if (dto.getSelectedBusNum() != null && !dto.getSelectedBusNum().isEmpty()
					&& !dto.getSelectedBusNum().equals("") && dto.getSelectedBusNum().equals(dto1.getSelectedBusNum())
					&& !dto.getBusNo().equals(dto1.getBusNo())) {

				sessionBackingBean.setMessage("Selected Bus No. has been already assigned to an Abbreviation");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
			}
		}
	}

	public void clearBusAssignAbbr() {
		loadValue();

		RequestContext.getCurrentInstance().update(":mainfrm");
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

	public void saveActionBusAssignAbbr() {
		swapOriginDestination = false;

		busesAssignedForAbbreviationService.updateBusNoInTimetableGeneratorDet(busForRouteList,
				sessionBackingBean.getLoginUser(), routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(),
				tripType);
		busesAssignedForAbbreviationService.updateBusNoInTimetableGeneratorDet(leaveForRouteList,
				sessionBackingBean.getLoginUser(), routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(),
				tripType);

		abbreviationOriginList = new ArrayList<RouteScheduleDTO>();
		abbreviationOriginList = busesAssignedForAbbreviationService.getBusNoListWithSelectedBuses(null,
				routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), "O", routeScheduleDTO.getRouteNo(),
				routeScheduleDTO.getBusCategory());

		abbreviationDestinationList = new ArrayList<RouteScheduleDTO>();
		abbreviationDestinationList = busesAssignedForAbbreviationService.getBusNoListWithSelectedBuses(null,
				routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), "D", routeScheduleDTO.getRouteNo(),
				routeScheduleDTO.getBusCategory());

		/**
		 * if D to O get all D buses and assigned busses and compare and put assigned
		 * buses to origin list selected buses start
		 **/
		if (tripType.equalsIgnoreCase("D")) {
			List<RouteScheduleDTO> tempAllDestBusesList = new ArrayList<RouteScheduleDTO>();
			Map<String, String> busNumsMap = new HashMap<String, String>();
			tempAllDestBusesList = busesAssignedForAbbreviationService.getBusNoListWithSelectedBusesDestination(null,
					routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), "D",
					routeScheduleDTO.getRouteNo(), routeScheduleDTO.getBusCategory());

			for (RouteScheduleDTO destDTO : tempAllDestBusesList) {
				for (RouteScheduleDTO oriDTO : abbreviationOriginList) {
					if (destDTO.getBusNo().equalsIgnoreCase(oriDTO.getBusNo()) && destDTO.getSelectedBusNum() != null
							&& !destDTO.getSelectedBusNum().isEmpty()) {
						busNumsMap.put(destDTO.getBusNo(), destDTO.getSelectedBusNum());
					}
				}
			}

			List<RouteScheduleDTO> tempAllOritBusesList = new ArrayList<RouteScheduleDTO>();
			for (RouteScheduleDTO oriDTO : abbreviationOriginList) {
				if (busNumsMap.containsKey(oriDTO.getBusNo())) {
					RouteScheduleDTO dto = new RouteScheduleDTO();
					dto.setBusNo(oriDTO.getBusNo());
					dto.setSelectedBusNum(busNumsMap.get(oriDTO.getBusNo()));
					tempAllOritBusesList.add(dto);
				} else {
					tempAllOritBusesList.add(oriDTO);
				}
			}

			abbreviationOriginList = new ArrayList<RouteScheduleDTO>();
			abbreviationOriginList = tempAllOritBusesList;
		}
		/**
		 * if D to O get all D buses and assigned busses and compare and put assigned
		 * buses to origin list selected buses end
		 **/

		/**
		 * if O to D get all O buses and assigned busses and compare and put assigned
		 * buses to destination list selected buses start
		 **/
		if (tripType.equalsIgnoreCase("O")) {
			List<RouteScheduleDTO> tempAllOriBusesList = new ArrayList<RouteScheduleDTO>();
			Map<String, String> busNumbsMap = new HashMap<String, String>();
			tempAllOriBusesList = busesAssignedForAbbreviationService.getBusNoListWithSelectedBusesDestination(null,
					routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), "O",
					routeScheduleDTO.getRouteNo(), routeScheduleDTO.getBusCategory());

			for (RouteScheduleDTO destDTO : tempAllOriBusesList) {
				for (RouteScheduleDTO oriDTO : abbreviationDestinationList) {
					if (destDTO.getBusNo().equalsIgnoreCase(oriDTO.getBusNo()) && destDTO.getSelectedBusNum() != null
							&& !destDTO.getSelectedBusNum().isEmpty()) {
						busNumbsMap.put(destDTO.getBusNo(), destDTO.getSelectedBusNum());
					}
				}
			}

			List<RouteScheduleDTO> tempAllDestiBusesList = new ArrayList<RouteScheduleDTO>();
			for (RouteScheduleDTO oriDTO : abbreviationDestinationList) {
				if (busNumbsMap.containsKey(oriDTO.getBusNo())) {
					RouteScheduleDTO dto = new RouteScheduleDTO();
					dto.setBusNo(oriDTO.getBusNo());
					dto.setSelectedBusNum(busNumbsMap.get(oriDTO.getBusNo()));
					tempAllDestiBusesList.add(dto);
				} else {
					tempAllDestiBusesList.add(oriDTO);
				}
			}

			abbreviationDestinationList = new ArrayList<RouteScheduleDTO>();
			abbreviationDestinationList = tempAllDestiBusesList;
		}
		/**
		 * if O to D get all O buses and assigned busses and compare and put assigned
		 * buses to destination list selected buses end
		 **/

		sessionBackingBean.setMessage("Data saved successfully");
		RequestContext.getCurrentInstance().execute("PF('dlgCommonSuccess').show()");
	}

	/** buses assigned for abbreviation methods **/

	/** route schedule generator methods **/
	public void searchRouteScheduleGenerator() {

		if (routeScheduleDTO.getRouteNo() != null && !routeScheduleDTO.getRouteNo().trim().equalsIgnoreCase("")) {

			if (routeScheduleDTO.getBusCategory() != null
					&& !routeScheduleDTO.getBusCategory().trim().equalsIgnoreCase("")) {

				if (routeScheduleDTO.getGroupNo() != null
						&& !routeScheduleDTO.getGroupNo().trim().equalsIgnoreCase("")) {

					if (displayOriginAndDestinationDetails() >= 0) {

						swapOriginDestination = true;

						originBusList = new ArrayList<TimeTableDTO>();
						destinationBusList = new ArrayList<TimeTableDTO>();

						if (tripType != null && !tripType.isEmpty() && !tripType.trim().equals("")) {

//							commented by thilna.d on 25-10-2021
//							originBusList = timeTableService.getAllBusNoForFixedBuses(routeScheduleDTO.getRouteNo(),
//									routeScheduleDTO.getBusCategory(), "O");
//							destinationBusList = timeTableService.getAllBusNoForFixedBuses(
//									routeScheduleDTO.getRouteNo(), routeScheduleDTO.getBusCategory(), "D");
							
//							added by thilna.d on 25-10-2021
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

								int totalTrips = routeScheduleService.getFixedOrWithoutFixedTripsCount(
										routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), "O", "N");
								int fixedTrips = routeScheduleService.getFixedOrWithoutFixedTripsCount(
										routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), "O", "Y");
								totalTrips = totalTrips + fixedTrips;
								busNoManagerNormalBusRouteSchedGen(totalTrips);
							}
							if (tripType.equalsIgnoreCase("D")) {

								int totalTrips = routeScheduleService.getFixedOrWithoutFixedTripsCount(
										routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), "D", "N");
								int fixedTrips = routeScheduleService.getFixedOrWithoutFixedTripsCount(
										routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(), "D", "Y");
								totalTrips = totalTrips + fixedTrips;
								busNoManagerNormalBusRouteSchedGen(totalTrips);
							}
						}

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

	private void busNoManagerNormalBusRouteSchedGen(int totalTrips) {

		busForRouteList = new ArrayList<>();
		leaveForRouteList = new ArrayList<>();
		setTripID(0);

		int tripCounting = 0;
		noOfDaysForEdit = routeScheduleService.getNoOfDays(routeScheduleDTO.getRouteNo(),
				routeScheduleDTO.getBusCategory(), routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(),
				tripType);

		List<RouteScheduleDTO> tempAllBusList = routeScheduleService.retrieveInsertedDataForEditTimeTable(
				routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getBusCategory(), routeScheduleDTO.getGroupNo(),
				tripType, routeScheduleDTO.getRouteNo());
		int tempTripsNum = tempAllBusList.get(0).getTripCount();

		List<String> busNoList = new ArrayList<String>();
		busNoList = routeScheduleService.selectEditDateForAssignedBuses(routeScheduleDTO.getRouteNo(),
				routeScheduleDTO.getBusCategory(), routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(),
				tripType);

		tripID = 0;
		// need to check noOfTripsForSelectedSide
		boolean firstDone = false;
		for (int nooftrips = 0; nooftrips < noOfTrips; nooftrips++) {

			List<String> stringBusNoList = new ArrayList<>();

			/*
			 * Rotate normally. Get the first number of list and send it to last index. then
			 * last index come to before the last one. likewise all index move
			 */
			List<String> tempBusNoList = new ArrayList<String>();
			for (int i = 0; i < busNoList.size(); i++) {

				if (!firstDone) {

					break;
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

					String bus = tempAllBusList.get(tripCounting).getBusNo() + "-"
							+ tempAllBusList.get(tripCounting).getRouteSeq();
					stringBusNoList.set(i, bus);
					tripCounting = tripCounting + 1;
				}
			}

			RouteScheduleDTO dto = setBusToRouteScheduleDTOs(stringBusNoList);
			busForRouteList.add(dto);
		}

		editBusForRouteList = new ArrayList<>();
		editBusForRouteList.addAll(busForRouteList);

		createEditDataTable();
	}

	private boolean displayOriginAndDestinationDetailsRouteSchedGen() {

		boolean continueFlow = false;

		/* Get no of without fixed buses for route/group/type */

		int origin_without_fixed_buses = routeScheduleService.getTotalWithoutFixedBusForGroupAndType(
				routeScheduleDTO.getRouteNo(), routeScheduleDTO.getGeneratedRefNo(), "O",
				routeScheduleDTO.getGroupNo());

		int destination_without_fixed_buses = routeScheduleService.getTotalWithoutFixedBusForGroupAndType(
				routeScheduleDTO.getRouteNo(), routeScheduleDTO.getGeneratedRefNo(), "D",
				routeScheduleDTO.getGroupNo());

		if (origin_without_fixed_buses > 0) {

			if (destination_without_fixed_buses > 0) {

				/* Get no of leaves buses for route/group/type */

				int originTotalLeaves = routeScheduleService.getTotalLavesForGroupAndType(
						routeScheduleDTO.getGeneratedRefNo(), "O", routeScheduleDTO.getGroupNo());

				int destinationTotalLeaves = routeScheduleService.getTotalLavesForGroupAndType(
						routeScheduleDTO.getGeneratedRefNo(), "D", routeScheduleDTO.getGroupNo());

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

										/* need check again," without_fixed buses mean number of trips " */
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

										routeScheduleDTO.setNoOfBusesOrigin(destination_without_fixed_buses);
										routeScheduleDTO.setNoOfLeavesOrigin(destinationTotalLeaves);

										routeScheduleDTO.setNoOfBusesDestination(origin_without_fixed_buses);
										routeScheduleDTO.setNoOfLeavesDestination(originTotalLeaves);

										/* need check again," without_fixed buses mean number of trips " */
										routeScheduleDTO.setNoOfTripsOrigin(destination_totalTrips);
										routeScheduleDTO.setNoOfTripsDestination(origin_totalTrips);

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
								setErrorMessage(
										"Can not continue the flow. No. of leaves for origin should be less than zero");
								RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

							}

						} else {
							setErrorMessage(
									"Can not continue the flow. No. of trip for destination can not less than zero");
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

			} else {
				setErrorMessage(
						"Can not continue the flow. Without fixed time buses can not less than zero for destination");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

			}

		} else {
			setErrorMessage("Can not continue the flow. Without fixed time buses can not less than zero for origin");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		}

		return continueFlow;

	}

	private boolean displayGroupData() {

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

	public void ajaxFillbusCategoryRouteSched() {
		busCategoryListRouteSchedGen = routeScheduleService.getBusCategoryList(routeScheduleDTO.getRouteNo());
		disableBusCategory = false;
	}

	public void ajaxSetGroupNoRouteSchedGen() {
		groupNo = routeScheduleDTO.getGroupNo();
	}

	public void ajaxFillRouteDetailsRouteSchedGen() {

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

		tripType = "O";

	}

	/* Swap Origin and Destination */
	public void ajaxSwapOriginDestinationRouteSchedGen() {

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

	public void editRouteScheduleData() {

		int tripCounting = 0;
		noOfDaysForEdit = routeScheduleService.getNoOfDays(routeScheduleDTO.getRouteNo(),
				routeScheduleDTO.getBusCategory(), routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(),
				tripType);

		List<String> busNoList = new ArrayList<String>();
		busNoList = routeScheduleService.selectEditDate(routeScheduleDTO.getRouteNo(),
				routeScheduleDTO.getBusCategory(), routeScheduleDTO.getGeneratedRefNo(), routeScheduleDTO.getGroupNo(),
				tripType);

		int totalTrips = 0;
		if (tripType.equalsIgnoreCase("O")) {

			totalTrips = routeScheduleService.getFixedOrWithoutFixedTripsCount(routeScheduleDTO.getGeneratedRefNo(),
					routeScheduleDTO.getGroupNo(), "O", "N");
			int fixedTrips = routeScheduleService.getFixedOrWithoutFixedTripsCount(routeScheduleDTO.getGeneratedRefNo(),
					routeScheduleDTO.getGroupNo(), "O", "Y");
			totalTrips = totalTrips + fixedTrips;
		} else if (tripType.equalsIgnoreCase("D")) {

			totalTrips = routeScheduleService.getFixedOrWithoutFixedTripsCount(routeScheduleDTO.getGeneratedRefNo(),
					routeScheduleDTO.getGroupNo(), "D", "N");
			int fixedTrips = routeScheduleService.getFixedOrWithoutFixedTripsCount(routeScheduleDTO.getGeneratedRefNo(),
					routeScheduleDTO.getGroupNo(), "D", "Y");
			totalTrips = totalTrips + fixedTrips;
		}

		List<RouteScheduleDTO> tempAllBusList = routeScheduleService
				.retrieveInsertedDataForEdit(routeScheduleDTO.getGeneratedRefNo(), tripType);
		int tempTripsNum = tempAllBusList.get(0).getTripCount();

		List<RouteScheduleDTO> tempList = new ArrayList<RouteScheduleDTO>();

		tripID = 0;
		boolean first = false;
		for (int nooftrips = 0; nooftrips < totalTrips; nooftrips++) {

			List<String> stringBusNoList = new ArrayList<>();

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

			for (int i = 0; i < tempTripsNum; i++) {
				if (tripCounting < tempAllBusList.size()) {

					stringBusNoList.set(i, busNoList.get(i));
					tripCounting = tripCounting + 1;
				}
			}

			RouteScheduleDTO dto = setBusToRouteScheduleDTOs(stringBusNoList);
			tempList.add(dto);
		}

		editBusForRouteList = new ArrayList<RouteScheduleDTO>();
		editBusForRouteList.addAll(tempList);

		createEditDataTable();

	}

	private void createEditDataTable() {

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

	public void saveEditRouteScheduleDate() {

		setSuccessMessage("Data is saved successfully");
		RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
	}

	/** route schedule generator methods **/

	/** route schedule generator methods **/

	public StreamedContent printReport() {
		String loginUser = sessionBackingBean.getLoginUser();

		files = null;
		String sourceFileName = null;

		Connection conn = null;

		sourceFileName = "..//reports//chakreeyaPalaliTimeTable.jrxml";
	
		try {
	        
	        List<ChakreeyaPalaliDTO> dataList1 = new ArrayList<>();
	        List<ChakreeyaPalaliDestiDTO> dataList2 = new ArrayList<>();
	        List<ChakreeyaPalaliAllDTO> dataList = new ArrayList<>();
	       ChakreeyaPalaliAllDTO s1 = new ChakreeyaPalaliAllDTO();
	     
	        List<ChakreeyaPalaliDTO> listO = new ArrayList<>();
	      
			conn = ConnectionManager.getConnection();
			
			String logopath = "//lk//informatics//ntc//view//reports//";
			dataList1 =timeTableService.getDataForReport(timeTableDTO.getGenereatedRefNo(),"O");
			dataList2 =timeTableService.getDataForReportDestination(timeTableDTO.getGenereatedRefNo(),"D");
			String busCategory = timeTableService.getBusCategoryDescription(timeTableDTO.getBusCategory());

		
		for(int i =0 ;i< dataList1.size() && i< dataList2.size();i++) {
			s1 = new ChakreeyaPalaliAllDTO();
			s1.setBusNoOrigin(dataList1.get(i).getBusNoOrigin());
			s1.setStartTimeOrigin(dataList1.get(i).getStartTimeOrigin());
			s1.setEndTImeOrigin(dataList1.get(i).getEndTImeOrigin());
			s1.setBusNoDestination(dataList2.get(i).getBusNoDestination());
			s1.setStartTimeDestination(dataList2.get(i).getStartTimeDestination());
			s1.setEndTImeDestination(dataList2.get(i).getEndTImeDestination());
			dataList.add(s1);
		}
		
			
			
			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			

			parameters.put("P_Panegenerator_ref",timeTableDTO.getGenereatedRefNo());
			parameters.put("Route_no", timeTableDTO.getRouteNo());

			parameters.put("Origin", timeTableDTO.getOrigin());
			parameters.put("Desination", timeTableDTO.getDestination());
			
			parameters.put("serviceType", busCategory);
			parameters.put("P_national_logo", logopath);
			parameters.put("P_ntc_logo", logopath);
			
			
			
			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);
			JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(dataList, false);
			

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, beanCollectionDataSource);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", "ChakreeyaPalali.pdf");

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("reportBytes", pdfByteArray);
			sessionMap.put("docType", "pdf");
			
		} catch (JRException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return files;

	
	}
	// getters setters
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

	public BusesAssignedForAbbreviationService getBusesAssignedForAbbreviationService() {
		return busesAssignedForAbbreviationService;
	}

	public void setBusesAssignedForAbbreviationService(
			BusesAssignedForAbbreviationService busesAssignedForAbbreviationService) {
		this.busesAssignedForAbbreviationService = busesAssignedForAbbreviationService;
	}

	public RouteScheduleDTO getRouteScheduleDTO() {
		return routeScheduleDTO;
	}

	public void setRouteScheduleDTO(RouteScheduleDTO routeScheduleDTO) {
		this.routeScheduleDTO = routeScheduleDTO;
	}

	public List<RouteScheduleDTO> getBusRouteListBusAssined() {
		return busRouteListBusAssined;
	}

	public void setBusRouteListBusAssined(List<RouteScheduleDTO> busRouteListBusAssined) {
		this.busRouteListBusAssined = busRouteListBusAssined;
	}

	public List<RouteScheduleDTO> getGroupNoList() {
		return groupNoList;
	}

	public void setGroupNoList(List<RouteScheduleDTO> groupNoList) {
		this.groupNoList = groupNoList;
	}

	public List<RouteScheduleDTO> getSelectLeavePositionList() {
		return selectLeavePositionList;
	}

	public void setSelectLeavePositionList(List<RouteScheduleDTO> selectLeavePositionList) {
		this.selectLeavePositionList = selectLeavePositionList;
	}

	public List<RouteScheduleDTO> getBusCategoryListBusAssined() {
		return busCategoryListBusAssined;
	}

	public void setBusCategoryListBusAssined(List<RouteScheduleDTO> busCategoryListBusAssined) {
		this.busCategoryListBusAssined = busCategoryListBusAssined;
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

	public List<RouteScheduleDTO> getLeaveForRouteList() {
		return leaveForRouteList;
	}

	public void setLeaveForRouteList(List<RouteScheduleDTO> leaveForRouteList) {
		this.leaveForRouteList = leaveForRouteList;
	}

	public List<RouteScheduleDTO> getMainSaveList() {
		return mainSaveList;
	}

	public void setMainSaveList(List<RouteScheduleDTO> mainSaveList) {
		this.mainSaveList = mainSaveList;
	}

	public List<ColumnModel> getColumns() {
		return columns;
	}

	public void setColumns(List<ColumnModel> columns) {
		this.columns = columns;
	}

	public List<ColumnModel> getColumnsLeaves() {
		return columnsLeaves;
	}

	public void setColumnsLeaves(List<ColumnModel> columnsLeaves) {
		this.columnsLeaves = columnsLeaves;
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

	public boolean isDisableBusCategory() {
		return disableBusCategory;
	}

	public void setDisableBusCategory(boolean disableBusCategory) {
		this.disableBusCategory = disableBusCategory;
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

	public String getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
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

	public int getTripID() {
		return tripID;
	}

	public void setTripID(int tripID) {
		this.tripID = tripID;
	}

	public boolean isRenderNormal() {
		return renderNormal;
	}

	public void setRenderNormal(boolean renderNormal) {
		this.renderNormal = renderNormal;
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

	public List<String> getLeavePositionList() {
		return leavePositionList;
	}

	public void setLeavePositionList(List<String> leavePositionList) {
		this.leavePositionList = leavePositionList;
	}

	public int getMaximumLeaveForDay() {
		return maximumLeaveForDay;
	}

	public int getMaximumDays() {
		return maximumDays;
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

	public List<RouteScheduleDTO> getEditBusForRouteList() {
		return editBusForRouteList;
	}

	public void setEditBusForRouteList(List<RouteScheduleDTO> editBusForRouteList) {
		this.editBusForRouteList = editBusForRouteList;
	}

	public List<RouteScheduleDTO> getTempEditBusForRouteList() {
		return tempEditBusForRouteList;
	}

	public void setTempEditBusForRouteList(List<RouteScheduleDTO> tempEditBusForRouteList) {
		this.tempEditBusForRouteList = tempEditBusForRouteList;
	}

	public List<ColumnModel> getEditColumns() {
		return editColumns;
	}

	public void setEditColumns(List<ColumnModel> editColumns) {
		this.editColumns = editColumns;
	}

	public String getRotationType() {
		return rotationType;
	}

	public void setRotationType(String rotationType) {
		this.rotationType = rotationType;
	}

	public int getNoOfDaysForEdit() {
		return noOfDaysForEdit;
	}

	public void setNoOfDaysForEdit(int noOfDaysForEdit) {
		this.noOfDaysForEdit = noOfDaysForEdit;
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

	public boolean isRenderTableOne() {
		return renderTableOne;
	}

	public void setRenderTableOne(boolean renderTableOne) {
		this.renderTableOne = renderTableOne;
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

	public boolean isDisabledleavePositionList() {
		return disabledleavePositionList;
	}

	public void setDisabledleavePositionList(boolean disabledleavePositionList) {
		this.disabledleavePositionList = disabledleavePositionList;
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

	public List<RouteScheduleDTO> getBusRouteListRouteSchedGen() {
		return busRouteListRouteSchedGen;
	}

	public void setBusRouteListRouteSchedGen(List<RouteScheduleDTO> busRouteListRouteSchedGen) {
		this.busRouteListRouteSchedGen = busRouteListRouteSchedGen;
	}

	public List<RouteScheduleDTO> getBusCategoryListRouteSchedGen() {
		return busCategoryListRouteSchedGen;
	}

	public void setBusCategoryListRouteSchedGen(List<RouteScheduleDTO> busCategoryListRouteSchedGen) {
		this.busCategoryListRouteSchedGen = busCategoryListRouteSchedGen;
	}

	public boolean isOriginDataTblDisable() {
		return originDataTblDisable;
	}

	public void setOriginDataTblDisable(boolean originDataTblDisable) {
		this.originDataTblDisable = originDataTblDisable;
	}
}
