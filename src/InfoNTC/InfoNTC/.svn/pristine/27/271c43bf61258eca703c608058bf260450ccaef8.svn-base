package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.PanelGeneratorDTO;
import lk.informatics.ntc.model.dto.TimeTableDTO;
import lk.informatics.ntc.model.dto.VehicleInspectionDTO;
import lk.informatics.ntc.model.service.PanelGeneratorService;
import lk.informatics.ntc.model.service.RouteScheduleService;
import lk.informatics.ntc.model.service.TimeTableService;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.ntc.view.util.SpringApplicationContex;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

@ManagedBean(name = "editPanelGeneratorNewBean")
@ViewScoped
public class EditPanelGeneratorNewBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private List<TimeTableDTO> busRouteList = new ArrayList<>(0);
	private List<String> abbreviationList = new ArrayList<>();
	private List<TimeTableDTO> routeDetailsList, originGroupOneList, originGroupTwoList, originGroupThreeList,
			destinationGroupOneList, destinationGroupTwoList, destinationGroupThreeList, panelGeneratorOriginRouteList,
			panelGeneratorDestinationRouteList, panelGeneratorLeaveBusesList, panelGeneratorLeaveBusesDesList;
	private List<TimeTableDTO> panelGeneratorOriginRouteSelectedList, panelGeneratorDestinationRouteSelectList;
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

	private int noOfTripsOrigin, noOfPrivateBusesOrigin, noOfTemporaryBusesOrigin, noOfCtbOrigin, noOfEtcOrigin,
			noOfPrivateLeaveBusesOrigin, noOfDummyBusesOrigin, totalBusesOrigin;

	private int noOfTripsDestination, noOfPrivateBusesDestination, noOfTemporaryBusesDestination, noOfCtbDestination,
			noOfEtcDestination, noOfPrivateLeaveBusesDestination, noOfDummyBusesDestination, totalBusesDestination;

	String restTimeOrigin, restTimeDestination, startTimeOrigin, endTimeOrigin, startTimeDestination,
			endTimeDestination, abbreviationOrigin, abbreviationDestination, busNoOrigin, busNoDestination,
			permitNoOrigin, permitNoDestination, abbreviationLeave, busNoLeave, permitNoLeave, abbreviationLeaveDes,
			busNoLeaveDes, permitNoLeaveDes, noOfTimeTablesPerWeek, routeStatus, groupNo;
	private boolean isDailyRotation = false;
	private boolean isTwoDayRotation = false;
	private boolean isFixedOrigin, isFixedDestination, renderForm;

	private List<String> permitNoList, busNoList, refNoList;
	private List<String> permitNoListOrigin, busNoListOrigin;
	private List<String> permitNoListOriginFull, busNoListOriginFull;
	private List<String> permitNoListDestinationFull, busNoListDestinationFull;
	TimeTableDTO panelGeneratorFormData = new TimeTableDTO();

	private String clickMonday;
	private String clickTuesday;
	private String clickWednesday;
	private String clickThursday;
	private String clickFriday;
	private String clickSaturday;
	private String clickSunday;

	private boolean group1;
	private boolean group2;
	private boolean group3;

	private boolean daily;
	private boolean twoDay;

	private boolean disableBtn;
	private boolean disableSaveBtn;

	private List<String> abbreviationListNew = new ArrayList<>();
	private List<String> abbreviationListOri = new ArrayList<>();
	private List<String> abbreviationListDes = new ArrayList<>();
	private List<String> abbreviationListLeaveOri = new ArrayList<>();
	private List<String> abbreviationListLeaveDes = new ArrayList<>();
	private List<String> abbreviationListSltbOri = new ArrayList<>();
	private List<String> abbreviationListSltbDes = new ArrayList<>();
	private List<String> abbreviationListEtcOri = new ArrayList<>();
	private List<String> abbreviationListEtcDes = new ArrayList<>();

	// for edit
	List<String> refNoListForEdit;
	String refNo, routeNo, origin, destination, busCategory, busCategoryDes, noOfTablesPerWeek;
	private StreamedContent files;

	private PanelGeneratorDTO panelGeneratorDTO = new PanelGeneratorDTO();
	private PanelGeneratorService panelGeneratorService;

	private List<PanelGeneratorDTO> group1_OD_List = new ArrayList<PanelGeneratorDTO>();
	private List<PanelGeneratorDTO> group2_OD_List = new ArrayList<PanelGeneratorDTO>();
	private List<PanelGeneratorDTO> group3_OD_List = new ArrayList<PanelGeneratorDTO>();
	private List<PanelGeneratorDTO> group1_DO_List = new ArrayList<PanelGeneratorDTO>();
	private List<PanelGeneratorDTO> group2_DO_List = new ArrayList<PanelGeneratorDTO>();
	private List<PanelGeneratorDTO> group3_DO_List = new ArrayList<PanelGeneratorDTO>();
	
	private List<String> dateList_Group1 = new ArrayList<String>();
	private List<String> dateList_Group2 = new ArrayList<String>();
	private List<String> dateList_Group3 = new ArrayList<String>();
	
	private List<TimeTableDTO> busDetailsList = new ArrayList<>(0);

	@PostConstruct
	public void init() {
		timeTableService = (TimeTableService) SpringApplicationContex.getBean("timeTableService");
		routeScheduleService = (RouteScheduleService) SpringApplicationContex.getBean("routeScheduleService");
		panelGeneratorService = (PanelGeneratorService) SpringApplicationContex.getBean("panelGeneratorService");
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
		refNoListForEdit = timeTableService.getReferenceNumbersForEdit();
//		permitNoList = timeTableService.getPermitNoList(routeNo, true,groupNo);
//		busNoList = timeTableService.getBusNoList(routeNo, true,groupNo);
//		permitNoListOrigin = timeTableService.getPermitNoList(routeNo, false,groupNo);
//		busNoListOrigin = timeTableService.getBusNoList(routeNo, false,groupNo);
		panelGeneratorDestinationRouteSelectList = new ArrayList<TimeTableDTO>();
		panelGeneratorOriginRouteSelectedList = new ArrayList<TimeTableDTO>();

		permitNoListOriginFull = new ArrayList<>();
		busNoListOriginFull = new ArrayList<>();
		permitNoListDestinationFull = new ArrayList<>();
		busNoListDestinationFull = new ArrayList<>();

		group1 = true;
		group2 = true;
		group3 = true;

		daily = true;
		twoDay = true;

		renderForm = false;
		
		busDetailsList = new ArrayList<>(0);
		
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

		disableBusCategory = false;

	}

	public void ajaxFillRouteDetails() {
		try {

			String refNo = timeTableDTO.getTripRefNo();

			abbreviationListOri = new ArrayList<>();
			abbreviationListDes = new ArrayList<>();
			abbreviationListLeaveOri = new ArrayList<>();
			abbreviationListLeaveDes = new ArrayList<>();
			abbreviationListSltbOri = new ArrayList<>();
			abbreviationListSltbDes = new ArrayList<>();
			abbreviationListEtcOri = new ArrayList<>();
			abbreviationListEtcDes = new ArrayList<>();

			routeDTO = timeTableService.getRouteDataForEditPanelGenerator(refNo);
//			timeTableDTO = timeTableService.getRouteDataForEditPanelGenerator(refNo);

			panelGeneratorOriginRouteList = timeTableService.getOriginDtoListForEdit(refNo,routeDTO.getRouteNo(),routeDTO.getBusCategory());
			panelGeneratorDestinationRouteList = timeTableService.getDestinationDtoListForEdit(refNo,routeDTO.getRouteNo(),routeDTO.getBusCategory());
			panelGeneratorLeaveBusesList = timeTableService.getLeaveBusesDtoListForEdit(refNo);
			panelGeneratorLeaveBusesDesList = timeTableService.getLeaveBusesDtoListForEditDes(refNo);

			for (TimeTableDTO data : panelGeneratorOriginRouteList) {
				if (data.isFixBusOrigin()) {
					panelGeneratorOriginRouteSelectedList.add(data);
				}
			}

			for (TimeTableDTO data : panelGeneratorDestinationRouteList) {
				if (data.isFixBusDestination()) {
					panelGeneratorDestinationRouteSelectList.add(data);
				}
			}

			setOrigin(routeDTO.getOrigin());
			setDestination(routeDTO.getDestination());
			setBusCategory(routeDTO.getBusCategory());
			setBusCategoryDes(routeDTO.getBusCategoryDes());
			setNoOfTimeTablesPerWeek(routeDTO.getNoOfTimeTablesPerWeek());
			setRouteNo(routeDTO.getRouteNo());
			setRouteStatus(routeDTO.getStatus());
			setGroupNo(routeDTO.getGroupNo());


			TimeTableDTO abbriDTO = timeTableService.getAbriviatiosForRoute(routeNo, routeDTO.getBusCategory());

			for (int i = 1; i <= routeDTO.getNoOfPvtBusesOrigin(); i++) {
				String element = abbriDTO.getAbbriAtOrigin() + i;
				abbreviationListOri.add(element);
				abbreviationListLeaveOri.add(element);
			}

			for (int i = 1; i <= routeDTO.getNoOfCtbOrigin(); i++) {
				String element = "SLTB-O" + i;
				abbreviationListOri.add(element);
				abbreviationListSltbOri.add(element);
			}

			for (int i = 1; i <= routeDTO.getNoOfEtcOrigin(); i++) {
				String element = "ETC-O" + i;
				abbreviationListOri.add(element);
				abbreviationListEtcOri.add(element);
			}
			
			for (int i = 1; i <= routeDTO.getNoOfDummyBusesOrigin(); i++) {
				String element = "D-O" + i;
				abbreviationListOri.add(element);
				abbreviationListSltbOri.add(element);
			}

			// Destination
			for (int i = 1; i <= routeDTO.getNoOfPvtBusesDestination(); i++) {
				String element = abbriDTO.getAbbriAtDestination() + i;
				abbreviationListDes.add(element);
				abbreviationListLeaveDes.add(element);
			}

			for (int i = 1; i <= routeDTO.getNoOfCtbDestination(); i++) {
				String element = "SLTB-D" + i;
				abbreviationListDes.add(element);
				abbreviationListSltbDes.add(element);
			}

			for (int i = 1; i <= routeDTO.getNoOfEtcDestination(); i++) {
				String element = "ETC-D" + i;
				abbreviationListDes.add(element);
				abbreviationListEtcDes.add(element);
			}
			
			for (int i = 1; i <= routeDTO.getNoOfDummyBusesDestination(); i++) {
				String element = "D-D" + i;
				abbreviationListDes.add(element);
				abbreviationListSltbDes.add(element);
			}

			// Origin
			for (int i = 1; i <= routeDTO.getNoOfPvtBusesOrigin(); i++) {
				String element = abbriDTO.getAbbriAtOrigin() + i;
				abbreviationListDes.add(element);
				abbreviationListLeaveDes.add(element);
			}

			for (int i = 1; i <= routeDTO.getNoOfCtbOrigin(); i++) {
				String element = "SLTB-O" + i;
				abbreviationListDes.add(element);
			}

			for (int i = 1; i <= routeDTO.getNoOfEtcOrigin(); i++) {
				String element = "ETC-O" + i;
				abbreviationListDes.add(element);
			}

			for (int i = 1; i <= routeDTO.getNoOfDummyBusesOrigin(); i++) {
				String element = "D-O" + i;
				abbreviationListDes.add(element);
			}
			// Destination
			for (int i = 1; i <= routeDTO.getNoOfPvtBusesDestination(); i++) {
				String element = abbriDTO.getAbbriAtDestination() + i;
				abbreviationListOri.add(element);
				abbreviationListLeaveOri.add(element);
			}

			for (int i = 1; i <= routeDTO.getNoOfCtbDestination(); i++) {
				String element = "SLTB-D" + i;
				abbreviationListOri.add(element);
			}

			for (int i = 1; i <= routeDTO.getNoOfEtcDestination(); i++) {
				String element = "ETC-D" + i;
				abbreviationListOri.add(element);
			}

			for (int i = 1; i <= routeDTO.getNoOfDummyBusesDestination(); i++) {
				String element = "D-D" + i;
				abbreviationListOri.add(element);
			}

			dateList_Group1 = panelGeneratorService.getDateList_Group1(routeDTO.getSeq(),refNo);
			dateList_Group2 = panelGeneratorService.getDateList_Group2(routeDTO.getSeq(),refNo);
			dateList_Group3 = panelGeneratorService.getDateList_Group3(routeDTO.getSeq(),refNo);

			if (groupNo.equals("1")) {
				if(dateList_Group1.contains("Monday")) {
					setClickMonday("true");
				}
				if(dateList_Group1.contains("Tuesday")) {
					setClickTuesday("true");
				}
				if(dateList_Group1.contains("Wednesday")) {
					setClickWednesday("true");
				}
				if(dateList_Group1.contains("Thursday")) {
					setClickThursday("true");
				}
				if(dateList_Group1.contains("Friday")) {
					setClickFriday("true");
				}
				if(dateList_Group1.contains("Saturday")) {
					setClickSaturday("true");
				}
				if(dateList_Group1.contains("Sunday")) {
					setClickSunday("true");
				}

				group1 = false;
			} else if (groupNo.equals("2")) {
				if(dateList_Group2.contains("Monday")) {
					setClickMonday("true");
				}
				if(dateList_Group2.contains("Tuesday")) {
					setClickTuesday("true");
				}
				if(dateList_Group2.contains("Wednesday")) {
					setClickWednesday("true");
				}
				if(dateList_Group2.contains("Thursday")) {
					setClickThursday("true");
				}
				if(dateList_Group2.contains("Friday")) {
					setClickFriday("true");
				}
				if(dateList_Group2.contains("Saturday")) {
					setClickSaturday("true");
				}
				if(dateList_Group2.contains("Sunday")) {
					setClickSunday("true");
				}

				group2 = false;
			} else if (groupNo.equals("3")) {
				if(dateList_Group3.contains("Monday")) {
					setClickMonday("true");
				}
				if(dateList_Group3.contains("Tuesday")) {
					setClickTuesday("true");
				}
				if(dateList_Group3.contains("Wednesday")) {
					setClickWednesday("true");
				}
				if(dateList_Group3.contains("Thursday")) {
					setClickThursday("true");
				}
				if(dateList_Group3.contains("Friday")) {
					setClickFriday("true");
				}
				if(dateList_Group3.contains("Saturday")) {
					setClickSaturday("true");
				}
				if(dateList_Group3.contains("Sunday")) {
					setClickSunday("true");
				}

				group3 = false;
			}

			try {
				panelGeneratorDTO = panelGeneratorService.getDetails(refNo, panelGeneratorDTO);

				if (groupNo.equals("1")) {

					group1_OD_List = panelGeneratorService.getODList_Group1(panelGeneratorDTO.getSeqNo());
					group1_DO_List = panelGeneratorService.getDOList_Group1(panelGeneratorDTO.getSeqNo());

					panelGeneratorDTO.setGroup01_OD_StartTime(group1_OD_List.get(0).getGroup01_OD_StartTime());
					panelGeneratorDTO.setGroup01_OD_EndTime(group1_OD_List.get(0).getGroup01_OD_EndTime());

					panelGeneratorDTO.setGroup01_DO_StartTime(group1_DO_List.get(0).getGroup01_DO_StartTime());
					panelGeneratorDTO.setGroup01_DO_EndTime(group1_DO_List.get(0).getGroup01_DO_EndTime());
				}

				if (groupNo.equals("2")) {

//					group1_OD_List = panelGeneratorService.getODList_Group1(panelGeneratorDTO.getSeqNo());
//					group1_DO_List = panelGeneratorService.getDOList_Group1(panelGeneratorDTO.getSeqNo());
					group2_OD_List = panelGeneratorService.getODList_Group2(panelGeneratorDTO.getSeqNo());
					group2_DO_List = panelGeneratorService.getDOList_Group2(panelGeneratorDTO.getSeqNo());
					
//					if(!group1_OD_List.isEmpty() || !group1_DO_List.isEmpty()) {
//						panelGeneratorDTO.setGroup01_OD_StartTime(group1_OD_List.get(0).getGroup01_OD_StartTime());
//						panelGeneratorDTO.setGroup01_OD_EndTime(group1_OD_List.get(0).getGroup01_OD_EndTime());
//
//						panelGeneratorDTO.setGroup01_DO_StartTime(group1_DO_List.get(0).getGroup01_DO_StartTime());
//						panelGeneratorDTO.setGroup01_DO_EndTime(group1_DO_List.get(0).getGroup01_DO_EndTime());
//					}
					if(!group2_OD_List.isEmpty() || !group2_DO_List.isEmpty()) {
						panelGeneratorDTO.setGroup02_OD_StartTime(group2_OD_List.get(0).getGroup02_OD_StartTime());
						panelGeneratorDTO.setGroup02_OD_EndTime(group2_OD_List.get(0).getGroup02_OD_EndTime());

						panelGeneratorDTO.setGroup02_DO_StartTime(group2_DO_List.get(0).getGroup02_DO_StartTime());
						panelGeneratorDTO.setGroup02_DO_EndTime(group2_DO_List.get(0).getGroup02_DO_EndTime());
					}	


				}

				if (groupNo.equals("3")) {

//					group1_OD_List = panelGeneratorService.getODList_Group1(panelGeneratorDTO.getSeqNo());
//					group1_DO_List = panelGeneratorService.getDOList_Group1(panelGeneratorDTO.getSeqNo());
//					group2_OD_List = panelGeneratorService.getODList_Group2(panelGeneratorDTO.getSeqNo());
//					group2_DO_List = panelGeneratorService.getDOList_Group2(panelGeneratorDTO.getSeqNo());
					group3_OD_List = panelGeneratorService.getODList_Group3(panelGeneratorDTO.getSeqNo());
					group3_DO_List = panelGeneratorService.getDOList_Group3(panelGeneratorDTO.getSeqNo());

//					if(!group1_OD_List.isEmpty() || !group1_DO_List.isEmpty()) {
//						panelGeneratorDTO.setGroup01_OD_StartTime(group1_OD_List.get(0).getGroup01_OD_StartTime());
//						panelGeneratorDTO.setGroup01_OD_EndTime(group1_OD_List.get(0).getGroup01_OD_EndTime());
//
//						panelGeneratorDTO.setGroup01_DO_StartTime(group1_DO_List.get(0).getGroup01_DO_StartTime());
//						panelGeneratorDTO.setGroup01_DO_EndTime(group1_DO_List.get(0).getGroup01_DO_EndTime());
//					}
//					if(!group2_OD_List.isEmpty() || !group2_DO_List.isEmpty()) {
//						panelGeneratorDTO.setGroup02_OD_StartTime(group2_OD_List.get(0).getGroup02_OD_StartTime());
//						panelGeneratorDTO.setGroup02_OD_EndTime(group2_OD_List.get(0).getGroup02_OD_EndTime());
//
//						panelGeneratorDTO.setGroup02_DO_StartTime(group2_DO_List.get(0).getGroup02_DO_StartTime());
//						panelGeneratorDTO.setGroup02_DO_EndTime(group2_DO_List.get(0).getGroup02_DO_EndTime());
//					}
					if (!group3_OD_List.isEmpty() || !group3_DO_List.isEmpty()) {

						panelGeneratorDTO.setGroup03_OD_StartTime(group3_OD_List.get(0).getGroup03_OD_StartTime());
						panelGeneratorDTO.setGroup03_OD_EndTime(group3_OD_List.get(0).getGroup03_OD_EndTime());

						panelGeneratorDTO.setGroup03_DO_StartTime(group3_DO_List.get(0).getGroup03_DO_StartTime());
						panelGeneratorDTO.setGroup03_DO_EndTime(group3_DO_List.get(0).getGroup03_DO_EndTime());
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				setErrorMessage("An error occurred during processing of date");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

			setNoOfTripsOrigin(routeDTO.getNoOfTripsOrigin());
			setPvtBusCountOrigin(routeDTO.getNoOfPvtBusesOrigin());
			setNoOfTemporaryBusesOrigin(routeDTO.getNoOfTemporaryBusesOrigin());
			setNoOfCtbOrigin(routeDTO.getNoOfCtbOrigin());
			setNoOfEtcOrigin(routeDTO.getNoOfEtcOrigin());
			setNoOfPrivateLeaveBusesOrigin(routeDTO.getNoOfPvtLeaveBusesOrigin());
			setNoOfDummyBusesOrigin(routeDTO.getNoOfDummyBusesOrigin());
			setRestTimeOrigin(routeDTO.getRestTimeOrigin());
			setTotalBusesOrigin(routeDTO.getTotalBusesOrigin());

			setNoOfTripsDestination(routeDTO.getNoOfTripsDestination());
			setPvtBusCountDestination(routeDTO.getNoOfPvtBusesDestination());
			setNoOfTemporaryBusesDestination(routeDTO.getNoOfTemporaryBusesDestination());
			setNoOfCtbDestination(routeDTO.getNoOfCtbDestination());
			setNoOfEtcDestination(routeDTO.getNoOfEtcDestination());
			setNoOfPrivateLeaveBusesDestination(routeDTO.getNoOfPvtLeaveBusesDestination());
			setNoOfDummyBusesDestination(routeDTO.getNoOfDummyBusesDestination());
			setRestTimeDestination(routeDTO.getRestTimeDestination());
			setTotalBusesDestination(routeDTO.getTotalBusesDestination());

			setDailyRotation(routeDTO.getIsDailyRotation());
			setTwoDayRotation(routeDTO.getIsTwoDayRotation());

			permitNoList = timeTableService.getPermitNoList(routeNo, true,groupNo,routeDTO.getBusCategory());
			busNoList = timeTableService.getBusNoList(routeNo, true,groupNo,routeDTO.getBusCategory());
			permitNoListOrigin = timeTableService.getPermitNoList(routeNo, false,groupNo,routeDTO.getBusCategory());
			busNoListOrigin = timeTableService.getBusNoList(routeNo, false,groupNo,routeDTO.getBusCategory());

			permitNoListOriginFull.addAll(permitNoListOrigin);
			permitNoListOriginFull.addAll(abbreviationListSltbOri);
			permitNoListOriginFull.addAll(abbreviationListEtcOri);
			permitNoListOriginFull.addAll(permitNoList);
			permitNoListOriginFull.addAll(abbreviationListSltbDes);
			permitNoListOriginFull.addAll(abbreviationListEtcDes);

			permitNoListDestinationFull.addAll(permitNoList);
			permitNoListDestinationFull.addAll(abbreviationListSltbDes);
			permitNoListDestinationFull.addAll(abbreviationListEtcDes);
			permitNoListDestinationFull.addAll(permitNoListOrigin);
			permitNoListDestinationFull.addAll(abbreviationListSltbOri);
			permitNoListDestinationFull.addAll(abbreviationListEtcOri);

			busNoListOriginFull.addAll(busNoListOrigin);
			busNoListOriginFull.addAll(abbreviationListSltbOri);
			busNoListOriginFull.addAll(abbreviationListEtcOri);
			busNoListOriginFull.addAll(busNoList);
			busNoListOriginFull.addAll(abbreviationListSltbDes);
			busNoListOriginFull.addAll(abbreviationListEtcDes);

			busNoListDestinationFull.addAll(busNoList);
			busNoListDestinationFull.addAll(abbreviationListSltbDes);
			busNoListDestinationFull.addAll(abbreviationListEtcDes);
			busNoListDestinationFull.addAll(busNoListOrigin);
			busNoListDestinationFull.addAll(abbreviationListSltbOri);
			busNoListDestinationFull.addAll(abbreviationListEtcOri);

			buttonHandler();

			daily = false;
			twoDay = false;
			renderForm = true;
		} catch (Exception e) {
			e.printStackTrace();
			setErrorMessage("An error occurred during processing");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void updateGroup01_OriginToDestination() {

		group1_OD_List.clear();

		PanelGeneratorDTO insertGroup1_OD = new PanelGeneratorDTO();

		insertGroup1_OD.setSelectGroup(panelGeneratorDTO.getSelectGroup());
		insertGroup1_OD.setGroup01_OD_StartTime(panelGeneratorDTO.getGroup01_OD_StartTime());
		insertGroup1_OD.setGroup01_OD_EndTime(panelGeneratorDTO.getGroup01_OD_EndTime());

		group1_OD_List.add(insertGroup1_OD);

		Long seqNo = panelGeneratorDTO.getSeqNo();
		String user = sessionBackingBean.getLoginUser();

		int update = panelGeneratorService.updateGroup1_OriginToDestination(seqNo, user, group1_OD_List);

		if (update == 0) {
			setSuccessMessage("Successfully Saved");
			;
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
		}

	}

	public void clearGroup01_OriginToDestination() {
		panelGeneratorDTO.setGroup01_OD_StartTime(null);
		panelGeneratorDTO.setGroup01_OD_EndTime(null);
	}

	public void updateGroup02_OriginToDestination() {

		group2_OD_List.clear();

		PanelGeneratorDTO insertGroup2_OD = new PanelGeneratorDTO();

		insertGroup2_OD.setSelectGroup(panelGeneratorDTO.getSelectGroup());
		insertGroup2_OD.setGroup02_OD_StartTime(panelGeneratorDTO.getGroup02_OD_StartTime());
		insertGroup2_OD.setGroup02_OD_EndTime(panelGeneratorDTO.getGroup02_OD_EndTime());

		group2_OD_List.add(insertGroup2_OD);

		Long seqNo = panelGeneratorDTO.getSeqNo();
		String user = sessionBackingBean.getLoginUser();

		int update = panelGeneratorService.updateGroup2_OriginToDestination(seqNo, user, group2_OD_List);

		if (update == 0) {
			setSuccessMessage("Successfully Saved");
			;
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
		}
	}

	public void clearGroup02_OriginToDestination() {
		panelGeneratorDTO.setGroup02_OD_StartTime(null);
		panelGeneratorDTO.setGroup02_OD_EndTime(null);
	}

	public void updateGroup03_OriginToDestination() {

		group3_OD_List.clear();

		PanelGeneratorDTO insertGroup3_OD = new PanelGeneratorDTO();

		insertGroup3_OD.setSelectGroup(panelGeneratorDTO.getSelectGroup());
		insertGroup3_OD.setGroup03_OD_StartTime(panelGeneratorDTO.getGroup03_OD_StartTime());
		insertGroup3_OD.setGroup03_OD_EndTime(panelGeneratorDTO.getGroup03_OD_EndTime());

		group3_OD_List.add(insertGroup3_OD);

		Long seqNo = panelGeneratorDTO.getSeqNo();
		String user = sessionBackingBean.getLoginUser();

		int update = panelGeneratorService.updateGroup3_OriginToDestination(seqNo, user, group3_OD_List);

		if (update == 0) {
			setSuccessMessage("Successfully Saved");
			;
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
		}

	}

	public void clearGroup03_OriginToDestination() {
		panelGeneratorDTO.setGroup03_OD_StartTime(null);
		panelGeneratorDTO.setGroup03_OD_EndTime(null);
	}

	public void updateGroup01_DestinationToOrigin() {

		group1_DO_List.clear();

		PanelGeneratorDTO insertGroup1_DO = new PanelGeneratorDTO();

		insertGroup1_DO.setSelectGroup(panelGeneratorDTO.getSelectGroup());
		insertGroup1_DO.setGroup01_DO_StartTime(panelGeneratorDTO.getGroup01_DO_StartTime());
		insertGroup1_DO.setGroup01_DO_EndTime(panelGeneratorDTO.getGroup01_DO_EndTime());

		group1_DO_List.add(insertGroup1_DO);

		Long seqNo = panelGeneratorDTO.getSeqNo();
		String user = sessionBackingBean.getLoginUser();

		int update = panelGeneratorService.updateGroup1_DestinationToOrigin(seqNo, user, group1_DO_List);

		if (update == 0) {
			setSuccessMessage("Successfully Saved");
			;
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
		}

	}

	public void clearGroup01_DestinationToOrigin() {
		panelGeneratorDTO.setGroup01_DO_StartTime(null);
		panelGeneratorDTO.setGroup01_DO_EndTime(null);
	}

	public void updateGroup02_DestinationToOrigin() {

		group2_DO_List.clear();

		PanelGeneratorDTO insertGroup2_DO = new PanelGeneratorDTO();

		insertGroup2_DO.setSelectGroup(panelGeneratorDTO.getSelectGroup());
		insertGroup2_DO.setGroup02_DO_StartTime(panelGeneratorDTO.getGroup02_DO_StartTime());
		insertGroup2_DO.setGroup02_DO_EndTime(panelGeneratorDTO.getGroup02_DO_EndTime());

		group2_DO_List.add(insertGroup2_DO);

		Long seqNo = panelGeneratorDTO.getSeqNo();
		String user = sessionBackingBean.getLoginUser();

		int update = panelGeneratorService.updateGroup2_DestinationToOrigin(seqNo, user, group2_DO_List);

		if (update == 0) {
			setSuccessMessage("Successfully Saved");
			;
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
		}
	}

	public void clearGroup02_DestinationToOrigin() {
		panelGeneratorDTO.setGroup02_DO_StartTime(null);
		panelGeneratorDTO.setGroup02_DO_EndTime(null);
	}

	public void updateGroup03_DestinationToOrigin() {

		group3_DO_List.clear();

		PanelGeneratorDTO insertGroup3_DO = new PanelGeneratorDTO();

		insertGroup3_DO.setSelectGroup(panelGeneratorDTO.getSelectGroup());
		insertGroup3_DO.setGroup03_DO_StartTime(panelGeneratorDTO.getGroup03_DO_StartTime());
		insertGroup3_DO.setGroup03_DO_EndTime(panelGeneratorDTO.getGroup03_DO_EndTime());

		group3_DO_List.add(insertGroup3_DO);

		Long seqNo = panelGeneratorDTO.getSeqNo();
		String user = sessionBackingBean.getLoginUser();

		int update = panelGeneratorService.updateGroup3_DestinationToOrigin(seqNo, user, group3_DO_List);

		if (update == 0) {
			setSuccessMessage("Successfully Saved");
			;
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
		}
	}

	public void clearGroup03_DestinationToOrigin() {
		panelGeneratorDTO.setGroup03_DO_StartTime(null);
		panelGeneratorDTO.setGroup03_DO_EndTime(null);
	}

	public void checkCheckBox() {
		if (isDailyRotation) {
			if (routeDTO.getGroupNo().equals("1")) {

				Date startDate = panelGeneratorDTO.getGroup01_OD_StartTime();
				Date endDate = panelGeneratorDTO.getGroup01_OD_EndTime();

				String startDatePart = extractDatePart(startDate);
				String endDatePart = extractDatePart(endDate);

				if (!startDatePart.equalsIgnoreCase(endDatePart)) {
					setErrorMessage("Start date and End date must be Same");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

				Date startDateDes = panelGeneratorDTO.getGroup01_DO_StartTime();
				Date endDateDes = panelGeneratorDTO.getGroup01_DO_EndTime();

				String startDatePartDes = extractDatePart(startDateDes);
				String endDatePartDes = extractDatePart(endDateDes);

				if (!startDatePartDes.equalsIgnoreCase(endDatePartDes)) {
					setErrorMessage("Start date and End date must be Same");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			} else if (routeDTO.getGroupNo().equals("2")) {

				Date startDate = panelGeneratorDTO.getGroup02_OD_StartTime();
				Date endDate = panelGeneratorDTO.getGroup02_OD_EndTime();

				String startDatePart = extractDatePart(startDate);
				String endDatePart = extractDatePart(endDate);

				if (!startDatePart.equalsIgnoreCase(endDatePart)) {
					setErrorMessage("Start date and End date must be Same");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

				Date startDateDes = panelGeneratorDTO.getGroup02_DO_StartTime();
				Date endDateDes = panelGeneratorDTO.getGroup02_DO_EndTime();

				String startDatePartDes = extractDatePart(startDateDes);
				String endDatePartDes = extractDatePart(endDateDes);

				if (!startDatePartDes.equalsIgnoreCase(endDatePartDes)) {
					setErrorMessage("Start date and End date must be Same");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else {
				Date startDate = panelGeneratorDTO.getGroup03_OD_StartTime();
				Date endDate = panelGeneratorDTO.getGroup03_OD_EndTime();

				String startDatePart = extractDatePart(startDate);
				String endDatePart = extractDatePart(endDate);

				if (!startDatePart.equalsIgnoreCase(endDatePart)) {
					setErrorMessage("Start date and End date must be Same");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

				Date startDateDes = panelGeneratorDTO.getGroup03_DO_StartTime();
				Date endDateDes = panelGeneratorDTO.getGroup03_DO_EndTime();

				String startDatePartDes = extractDatePart(startDateDes);
				String endDatePartDes = extractDatePart(endDateDes);

				if (!startDatePartDes.equalsIgnoreCase(endDatePartDes)) {
					setErrorMessage("Start date and End date must be Same");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			}
			twoDay = true;

		} else if (isTwoDayRotation) {
			if (routeDTO.getGroupNo().equals("1")) {
				// Origin
				Date startDate = panelGeneratorDTO.getGroup01_OD_StartTime();
				Date endDate = panelGeneratorDTO.getGroup01_OD_EndTime();

				String startDatePart = extractDatePart(startDate);
				String endDatePart = extractDatePart(endDate);

				long intervalDays = intervalDatePart(startDatePart, endDatePart);

				if (!(intervalDays == 1)) {
					setErrorMessage("End date must be the day after the Start date");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

				// Destination
				Date startDateDes = panelGeneratorDTO.getGroup01_DO_StartTime();
				Date endDateDes = panelGeneratorDTO.getGroup01_DO_EndTime();

				String startDatePartDes = extractDatePart(startDateDes);
				String endDatePartDes = extractDatePart(endDateDes);

				long intervalDaysDes = intervalDatePart(startDatePartDes, endDatePartDes);

				if (!(intervalDaysDes == 1)) {
					setErrorMessage("End date must be the day after the Start date");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else if (routeDTO.getGroupNo().equals("2")) {
				// Origin
				Date startDate = panelGeneratorDTO.getGroup02_OD_StartTime();
				Date endDate = panelGeneratorDTO.getGroup02_OD_EndTime();

				String startDatePart = extractDatePart(startDate);
				String endDatePart = extractDatePart(endDate);

				long intervalDays = intervalDatePart(startDatePart, endDatePart);

				if (!(intervalDays == 1)) {
					setErrorMessage("End date must be the day after the Start date");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

				// Destination
				Date startDateDes = panelGeneratorDTO.getGroup02_DO_StartTime();
				Date endDateDes = panelGeneratorDTO.getGroup02_DO_EndTime();

				String startDatePartDes = extractDatePart(startDateDes);
				String endDatePartDes = extractDatePart(endDateDes);

				long intervalDaysDes = intervalDatePart(startDatePartDes, endDatePartDes);

				if (!(intervalDaysDes == 1)) {
					setErrorMessage("End date must be the day after the Start date");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else {
				// Origin
				Date startDate = panelGeneratorDTO.getGroup03_OD_StartTime();
				Date endDate = panelGeneratorDTO.getGroup03_OD_EndTime();

				String startDatePart = extractDatePart(startDate);
				String endDatePart = extractDatePart(endDate);

				long intervalDays = intervalDatePart(startDatePart, endDatePart);

				if (!(intervalDays == 1)) {
					setErrorMessage("End date must be the day after the Start date");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

				// Destination
				Date startDateDes = panelGeneratorDTO.getGroup03_DO_StartTime();
				Date endDateDes = panelGeneratorDTO.getGroup03_DO_EndTime();

				String startDatePartDes = extractDatePart(startDateDes);
				String endDatePartDes = extractDatePart(endDateDes);

				long intervalDaysDes = intervalDatePart(startDatePartDes, endDatePartDes);

				if (!(intervalDaysDes == 1)) {
					setErrorMessage("End date must be the day after the Start date");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			}

			daily = true;

		}

	}

	public String extractDatePart(Date date) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			return dateFormat.format(date);
		} catch (Exception e) {
			return null;
		}

	}

	public long intervalDatePart(String startDatePart, String endDatePart) {
		try {
			long inervalDay = 0;

			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date startDate = dateFormat.parse(startDatePart);
			Date endDate = dateFormat.parse(endDatePart);

			// Extract the day part of the dates
			Calendar startCal = Calendar.getInstance();
			startCal.setTime(startDate);
			int startYear = startCal.get(Calendar.YEAR);
			int startMonth = startCal.get(Calendar.MONTH);
			int startDay = startCal.get(Calendar.DAY_OF_MONTH);

			Calendar endCal = Calendar.getInstance();
			endCal.setTime(endDate);
			int endYear = startCal.get(Calendar.YEAR);
			int endMonth = startCal.get(Calendar.MONTH);
			int endDay = endCal.get(Calendar.DAY_OF_MONTH);

			int inervalYear = endYear - startYear;
			int inervalMonth = endMonth - startMonth;

			if (inervalYear == 0 && inervalMonth == 0) {
				inervalDay = endDay - startDay;
			}

			// Calculate the interval in days
			return inervalDay;
		} catch (ParseException e) {
			// Handle parsing exception
			e.printStackTrace();
			return -1; // Return -1 to indicate an error
		}

	}

	public void UpdateActionPanelGenerator() {
		routeDTO.setNoOfTripsOrigin(noOfTripsOrigin);
		routeDTO.setNoOfTripsDestination(noOfTripsDestination);
		routeDTO.setNoOfPvtBusesOrigin(pvtBusCountOrigin);
		routeDTO.setNoOfPvtBusesDestination(pvtBusCountDestination);
		routeDTO.setNoOfTemporaryBusesOrigin(noOfTemporaryBusesOrigin);
		routeDTO.setNoOfTemporaryBusesDestination(noOfTemporaryBusesDestination);
		routeDTO.setNoOfCtbOrigin(noOfCtbOrigin);
		routeDTO.setNoOfCtbDestination(noOfCtbDestination);
		routeDTO.setNoOfEtcOrigin(noOfEtcOrigin);
		routeDTO.setNoOfEtcDestination(noOfEtcDestination);
		routeDTO.setNoOfPvtLeaveBusesOrigin(noOfPrivateLeaveBusesOrigin);
		routeDTO.setNoOfPvtLeaveBusesDestination(noOfPrivateLeaveBusesDestination);
		routeDTO.setNoOfDummyBusesOrigin(noOfDummyBusesOrigin);
		routeDTO.setNoOfDummyBusesDestination(noOfDummyBusesDestination);
		routeDTO.setRestTimeOrigin(restTimeOrigin);
		routeDTO.setRestTimeDestination(restTimeDestination);
		routeDTO.setTotalBusesOrigin(totalBusesOrigin);
		routeDTO.setTotalBusesDestination(totalBusesDestination);

		for (TimeTableDTO data : panelGeneratorOriginRouteList) {
			if (panelGeneratorOriginRouteSelectedList.contains(data)) {
				data.setFixBusOrigin(true);
			} else {
				data.setFixBusOrigin(false);
			}
		}

		for (TimeTableDTO data : panelGeneratorDestinationRouteList) {
			if (panelGeneratorDestinationRouteSelectList.contains(data)) {
				data.setFixBusDestination(true);
			} else {
				data.setFixBusDestination(false);
			}
		}

		timeTableService.saveGeneralDetailsEditPanelGenerator(routeDTO, sessionBackingBean.getLoginUser(), true,
				timeTableDTO.getTripRefNo(), "D");

		timeTableService.editOriginTimeSlotTable(routeDTO, panelGeneratorOriginRouteList,
				sessionBackingBean.getLoginUser(), true, timeTableDTO.getTripRefNo(), "D");

		timeTableService.editDestinationTimeSlotTable(routeDTO, panelGeneratorDestinationRouteList,
				sessionBackingBean.getLoginUser(), true, timeTableDTO.getTripRefNo(), "D");

		timeTableService.editLeaveBusesTimeSlotTable(routeDTO, panelGeneratorLeaveBusesList,
				panelGeneratorLeaveBusesDesList, sessionBackingBean.getLoginUser(), true, timeTableDTO.getTripRefNo(),
				"D");

		setSuccessMessage("Saved Successfully");
		RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
	}

	public void onCellEdit(TimeTableDTO p, String side) {
		TimeTableDTO generalDTO = timeTableService.getDtoWithGeneralData(routeNo, busCategory);

		if (side.equals("O")) {
			p.setEndTimeOrigin(calculateTime(p.getStartTimeOrigin(), generalDTO.getTraveltime()));
		} else {
			p.setEndTimeDestination(calculateTime(p.getStartTimeDestination(), generalDTO.getTraveltime()));
		}

	}

	public String calculateTime(String startTime, String travelTime) {
		String endTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

		try {
			java.util.Date currentArrivalTime = sdf.parse(startTime);

			String[] timeParts = travelTime.split(":");
			int hours = Integer.parseInt(timeParts[0]);
			int minutes = Integer.parseInt(timeParts[1]);

			int minutesToAdd = (hours * 60) + minutes;

			currentArrivalTime = new Date(currentArrivalTime.getTime() + (minutesToAdd * 60000));
			endTime = sdf.format(currentArrivalTime);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return endTime;

	}

	public void ajaxFillAbbrivationNo(String side, TimeTableDTO p) {

		if ("O".equals(side)) {
			if (p.getAbbreviationOrigin().contains("SLTB") || p.getAbbreviationOrigin().contains("ETC") || p.getAbbreviationOrigin().contains("D-O") || p.getAbbreviationOrigin().contains("D-D")) {
				p.setPermitNoOrigin(p.getAbbreviationOrigin());
				p.setBusNoOrigin(p.getAbbreviationOrigin());
			}

		}
		if ("D".equals(side)) {
			if (p.getAbbreviationDestination().contains("SLTB") || p.getAbbreviationDestination().contains("ETC")|| p.getAbbreviationDestination().contains("D-O") || p.getAbbreviationDestination().contains("D-D")) {
				p.setPermitNoDestination(p.getAbbreviationDestination());
				p.setBusNoDestination(p.getAbbreviationDestination());
			}

		}

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
							timeTableDTO.getBusCategory(),timeTableDTO.getGroupNo());
					/** timeTableDTO.getBusCategory() added by tharushi.e **/
					originPVTBusesList = new ArrayList<VehicleInspectionDTO>();
					originPVTBusesList = timeTableService.getPVTbuses(timeTableDTO.getRouteNo(), "N");// origin
																										// PVT
																										// buses
					destinatinoPVTBusesList = new ArrayList<VehicleInspectionDTO>();
					destinatinoPVTBusesList = timeTableService.getPVTbuses(timeTableDTO.getRouteNo(), "Y");// destination
																											// PVT
																											// buses

					if (pvtBusCountOrigin > 0) {

						groupOneDTO.setNoOfPVTbuessOriginOne(pvtBusCountOrigin);
						groupTwoDTO.setNoOfPVTbuessOriginTwo(pvtBusCountOrigin);
						groupThreeDTO.setNoOfPVTbuessOriginThree(pvtBusCountOrigin);

						int PVT_O01 = groupOneDTO.getNoOfPVTbuessOriginOne();
						groupOneDTO.setTotalBusesOriginOne(PVT_O01);

						int PVT_O02 = groupTwoDTO.getNoOfPVTbuessOriginTwo();
						groupTwoDTO.setTotalBusesOriginTwo(PVT_O02);

						int PVT_O03 = groupThreeDTO.getNoOfPVTbuessOriginThree();
						groupThreeDTO.setTotalBusesOriginThree(PVT_O03);

						if (pvtBusCountDestination > 0) {

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

						} else {

							setErrorMessage("No. of PVT bus can not be zero for destination");
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						}
					} else {
						setErrorMessage("No. of PVT bus can not be zero for origin");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}

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
		System.out.println("start groupManager for edit");
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

	public void ajaxOnRowEditOrigin(RowEditEvent event) {
		TimeTableDTO dtoWithTravelTime = timeTableService.getDtoWithGeneralData(routeDTO.getRouteNo(), busCategory);
		TimeTableDTO editedTrip = (TimeTableDTO) event.getObject();

		editedTrip.setAbbreviationOrigin(abbreviationOrigin);
		editedTrip.setPermitNoOrigin(permitNoOrigin);
		editedTrip.setBusNoOrigin(busNoOrigin);

		String timeStringStart = startTimeOrigin.substring(11, 16);
		String travelTime = dtoWithTravelTime.getTraveltime();

		// Convert startTimeOrigin from String to LocalTime
		LocalTime startTimeOriginTime, endTimeLocal, travelTimeLocal;
		try {
			startTimeOriginTime = LocalTime.parse(timeStringStart, DateTimeFormatter.ofPattern("HH:mm"));
			travelTimeLocal = LocalTime.parse(travelTime, DateTimeFormatter.ofPattern("HH:mm"));

			endTimeLocal = startTimeOriginTime.plus(travelTimeLocal.getHour(), ChronoUnit.HOURS)
					.plus(travelTimeLocal.getMinute(), ChronoUnit.MINUTES);

		} catch (DateTimeParseException e) {
			// Handle parsing error if necessary
			e.printStackTrace();
			return;
		}

		// Formatting the time before setting it in the editedTrip object
		String formattedTimeStart = startTimeOriginTime.format(DateTimeFormatter.ofPattern("HH:mm"));
		String formattedTimeEnd = endTimeLocal.format(DateTimeFormatter.ofPattern("HH:mm"));

		editedTrip.setStartTimeOrigin(formattedTimeStart);
		editedTrip.setEndTimeOrigin(formattedTimeEnd);

	}

	public void ajaxOnRowEditDestination(RowEditEvent event) {

		TimeTableDTO dtoWithTravelTime = timeTableService.getDtoWithGeneralData(routeDTO.getRouteNo(), busCategory);
		TimeTableDTO editedTrip = (TimeTableDTO) event.getObject();

		editedTrip.setAbbreviationDestination(abbreviationDestination);
		editedTrip.setPermitNoDestination(permitNoDestination);
		editedTrip.setBusNoDestination(busNoDestination);

		String timeStringStart = startTimeDestination.substring(11, 16);
		String travelTime = dtoWithTravelTime.getTraveltime();

		// Convert startTimeOrigin from String to LocalTime
		LocalTime startTimeDestinationTime, endTimeLocal, travelTimeLocal;
		try {
			startTimeDestinationTime = LocalTime.parse(timeStringStart, DateTimeFormatter.ofPattern("HH:mm"));
			travelTimeLocal = LocalTime.parse(travelTime, DateTimeFormatter.ofPattern("HH:mm"));

			endTimeLocal = startTimeDestinationTime.plus(travelTimeLocal.getHour(), ChronoUnit.HOURS)
					.plus(travelTimeLocal.getMinute(), ChronoUnit.MINUTES);

		} catch (DateTimeParseException e) {
			// Handle parsing error if necessary
			e.printStackTrace();
			return;
		}

		// Formatting the time before setting it in the editedTrip object
		String formattedTimeStart = startTimeDestinationTime.format(DateTimeFormatter.ofPattern("HH:mm"));
		String formattedTimeEnd = endTimeLocal.format(DateTimeFormatter.ofPattern("HH:mm"));

		editedTrip.setStartTimeDestination(formattedTimeStart);
		editedTrip.setEndTimeDestination(formattedTimeEnd);

	}

	public void ajaxOnRowEditLeave(RowEditEvent event) {

		TimeTableDTO editedTrip = (TimeTableDTO) event.getObject();

		editedTrip.setAbbreviationLeave(abbreviationLeave);
		editedTrip.setPermitNoLeave(permitNoLeave);
		editedTrip.setBusNoLeave(busNoLeave);

	}

	public void ajaxUpdateBusNoLeave(TimeTableDTO p) {
		if (p.getPermitNoLeave() != null && !p.getPermitNoLeave().trim().isEmpty()
				&& !p.getPermitNoLeave().equalsIgnoreCase("")) {
			String newBusNo = timeTableService.updateBusNo(p.getPermitNoLeave(), routeNo);
			p.setBusNoLeave(newBusNo);

		}
		if (p.getPermitNoLeaveDes() != null && !p.getPermitNoLeaveDes().trim().isEmpty()
				&& !p.getPermitNoLeaveDes().equalsIgnoreCase("")) {

			String newBusNo = timeTableService.updateBusNo(p.getPermitNoLeaveDes(), routeNo);
			p.setBusNoLeaveDes(newBusNo);
		}

	}

	public void ajaxUpdatePermitNoLeave(TimeTableDTO p) {

		if (p.getBusNoLeave() != null && !p.getBusNoLeave().trim().isEmpty()
				&& !p.getBusNoLeave().equalsIgnoreCase("")) {

			String newPermitNo = timeTableService.updatePermitNo(p.getBusNoLeave(), routeNo);
			p.setPermitNoLeave(newPermitNo);
		}
		if (p.getBusNoLeaveDes() != null && !p.getBusNoLeaveDes().trim().isEmpty()
				&& !p.getBusNoLeaveDes().equalsIgnoreCase("")) {

			String routeNo = timeTableDTO.getRouteNo();
			String newPermitNo = timeTableService.updatePermitNo(p.getBusNoLeaveDes(), routeNo);
			p.setPermitNoLeave(newPermitNo);
		}

	}

	public void ajaxUpdatePermitNoOrigin(TimeTableDTO p) {

		if (p.getAbbreviationOrigin().contains("SLTB") || p.getAbbreviationOrigin().contains("ETC")|| p.getAbbreviationOrigin().contains("D-O") || p.getAbbreviationOrigin().contains("D-D")) {
			p.setPermitNoOrigin(p.getBusNoOrigin());
		} else {
			String busNoOrigin = p.getBusNoOrigin();
			String newPermitNo = timeTableService.updatePermitNo(busNoOrigin, routeNo);
			p.setPermitNoOrigin(newPermitNo);

			for (TimeTableDTO obj : panelGeneratorDestinationRouteList) {
				if (obj.getAbbreviationDestination().equalsIgnoreCase(p.getAbbreviationOrigin())) {
					obj.setBusNoDestination(busNoOrigin);
					obj.setPermitNoDestination(newPermitNo);
				}
			}
			
			for (TimeTableDTO obj : panelGeneratorOriginRouteList) {
				if (obj.getAbbreviationOrigin().equalsIgnoreCase(p.getAbbreviationOrigin())) {
					obj.setBusNoOrigin(busNoOrigin);
					obj.setPermitNoOrigin(newPermitNo);
				}
				if(obj.getBusNoOrigin() != null
						&& obj.getBusNoOrigin().equals(busNoOrigin) 
							&& !(obj.getAbbreviationOrigin().equalsIgnoreCase(p.getAbbreviationOrigin()))) {
					setErrorMessage("Bus number already assigned");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					
				}

			}
		}
		
		RequestContext context = RequestContext.getCurrentInstance();
		context.update("mainfrm:destinationTableGroupOne");

	}

	public void ajaxUpdateBusNoOrigin(TimeTableDTO p) {

		if (p.getAbbreviationOrigin().contains("SLTB") || p.getAbbreviationOrigin().contains("ETC")|| p.getAbbreviationOrigin().contains("D-O") || p.getAbbreviationOrigin().contains("D-D")) {
			p.setBusNoOrigin(p.getPermitNoOrigin());
		} else {
			String permitNoOrigin = p.getPermitNoOrigin();
			String newBusNo = timeTableService.updateBusNo(permitNoOrigin, routeNo);
			p.setBusNoOrigin(newBusNo);

			for (TimeTableDTO obj : panelGeneratorDestinationRouteList) {
				if (obj.getAbbreviationDestination().equalsIgnoreCase(p.getAbbreviationOrigin())) {
					obj.setBusNoDestination(newBusNo);
					obj.setPermitNoDestination(permitNoOrigin);
				}

			}
			
			for (TimeTableDTO obj : panelGeneratorOriginRouteList) {
				if (obj.getAbbreviationOrigin().equalsIgnoreCase(p.getAbbreviationOrigin())) {
					obj.setBusNoOrigin(newBusNo);
					obj.setPermitNoOrigin(permitNoOrigin);
				}
				
				if(obj.getPermitNoOrigin() != null 
						&& obj.getPermitNoOrigin().equals(permitNoOrigin) 
							&& !(obj.getAbbreviationOrigin().equalsIgnoreCase(p.getAbbreviationOrigin()))) {
					setErrorMessage("Permit number already assigned");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			}
		}
		
		RequestContext context = RequestContext.getCurrentInstance();
		context.update("mainfrm:destinationTableGroupOne");

	}

	public void ajaxUpdatePermitNoDestination(TimeTableDTO p) {

		if (p.getAbbreviationDestination().contains("SLTB") || p.getAbbreviationDestination().contains("ETC")|| p.getAbbreviationDestination().contains("D-O") || p.getAbbreviationDestination().contains("D-D")) {
			p.setPermitNoDestination(p.getBusNoDestination());
		} else {
			String busNoDestination = p.getBusNoDestination();
			String newPermitNo = timeTableService.updatePermitNo(busNoDestination, routeNo);
			p.setPermitNoDestination(newPermitNo);

			for (TimeTableDTO obj : panelGeneratorOriginRouteList) {
				if (obj.getAbbreviationOrigin().equalsIgnoreCase(p.getAbbreviationDestination())) {
					obj.setBusNoOrigin(busNoDestination);
					obj.setPermitNoOrigin(newPermitNo);
				}

			}
			
			for (TimeTableDTO obj : panelGeneratorDestinationRouteList) {
				if (obj.getAbbreviationDestination().equalsIgnoreCase(p.getAbbreviationDestination())) {
					obj.setBusNoDestination(busNoDestination);
					obj.setPermitNoDestination(newPermitNo);
				}
				if(obj.getBusNoDestination() != null 
						&& obj.getBusNoDestination().equals(busNoDestination) 
							&& !(obj.getAbbreviationDestination().equalsIgnoreCase(p.getAbbreviationDestination()))) {
					setErrorMessage("Bus number already assigned");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			}
		}

		RequestContext context = RequestContext.getCurrentInstance();
		context.update("mainfrm:originGroupOne");
	}

	public void ajaxUpdateBusNoDestination(TimeTableDTO p) {

		if (p.getAbbreviationDestination().contains("SLTB") || p.getAbbreviationDestination().contains("ETC")|| p.getAbbreviationDestination().contains("D-O") || p.getAbbreviationDestination().contains("D-D")) {
			p.setBusNoDestination(p.getPermitNoDestination());
		} else {
			String permitNoDestination = p.getPermitNoDestination();
			String newBusNo = timeTableService.updateBusNo(permitNoDestination, routeNo);
			p.setBusNoDestination(newBusNo);

			for (TimeTableDTO obj : panelGeneratorOriginRouteList) {
				if (obj.getAbbreviationOrigin().equalsIgnoreCase(p.getAbbreviationDestination())) {
					obj.setBusNoOrigin(newBusNo);
					obj.setPermitNoOrigin(permitNoDestination);
				}

			}
			
			for (TimeTableDTO obj : panelGeneratorDestinationRouteList) {
				if (obj.getAbbreviationDestination().equalsIgnoreCase(p.getAbbreviationDestination())) {
					obj.setBusNoDestination(newBusNo);
					obj.setPermitNoDestination(permitNoDestination);
				}
				if(obj.getPermitNoDestination() != null 
						&& obj.getPermitNoDestination().equals(permitNoDestination) 
							&& !(obj.getAbbreviationDestination().equalsIgnoreCase(p.getAbbreviationDestination()))) {
					setErrorMessage("Permit number already assigned");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			}
			
		}

		RequestContext context = RequestContext.getCurrentInstance();
		context.update("mainfrm:originGroupOne");
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

	public void ajaxCalculateTotalNoOfBusPanelGenerator() {

		setTotalBusesOrigin(
				pvtBusCountOrigin + noOfTemporaryBusesOrigin + noOfCtbOrigin + noOfEtcOrigin + noOfDummyBusesOrigin);
		setTotalBusesDestination(pvtBusCountDestination + noOfTemporaryBusesDestination + noOfCtbDestination
				+ noOfEtcDestination + noOfDummyBusesDestination);

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

		clearGroup01_DestinationToOrigin();
		clearGroup01_OriginToDestination();
		clearGroup02_DestinationToOrigin();
		clearGroup02_OriginToDestination();
		clearGroup03_DestinationToOrigin();
		clearGroup03_OriginToDestination();

		routeDTO = null;

		panelGeneratorOriginRouteList = new ArrayList<>();
		panelGeneratorDestinationRouteList = new ArrayList<>();
		panelGeneratorLeaveBusesList = new ArrayList<>();

		setOrigin(null);
		setDestination(null);
		setBusCategory(null);
		setBusCategoryDes(null);
		setNoOfTimeTablesPerWeek(null);
		setRouteNo(null);
		setRouteStatus(null);
		setGroupNo(null);

		setClickMonday("false");
		setClickTuesday("false");
		setClickWednesday("false");
		setClickThursday("false");
		setClickFriday("false");
		setClickSaturday("false");
		setClickSunday("false");

		setDailyRotation(false);
		setTwoDayRotation(false);

		renderForm = false;
		daily = true;
		twoDay = true;
		group1 = true;
		group2 = true;
		group3 = true;

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
										 * changed by hasanga.u 29/10/2019 -> check no of buses are null before save the
										 * record start
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
										 * changed by hasanga.u 29/10/2019 -> check no of buses are null before save the
										 * record start
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
											 * commented by tharushi.e bcoz when update No Of Trip Generator , in
											 * nt_t_trips_generator_det update Destination side data only
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
															 * changed by hasanga.u 29/10/2019 -> check no of buses are
															 * null before save the record start
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
															 * changed by hasanga.u 29/10/2019 -> check no of buses are
															 * null before save the record start
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
																 * History Update
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
																 * History Update End
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
																				 * changed by hasanga.u 29/10/2019 ->
																				 * check no of buses are null before
																				 * save the record start
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
																				 * check no of buses are null before
																				 * save the record start
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
																					 * History Update
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
																					 * History Update End
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

	public List<TimeTableDTO> timeRangeManagerForPanelGenerator(long begin, long end, int noOfTrips, boolean isOrigin,
			String routeNo) {
		TimeTableDTO generalDTO = timeTableService.getDtoWithGeneralData(routeNo, busCategory);
		List<String> valueList = intervalCalculatorForPanelGenerator(begin, end, noOfTrips, generalDTO.getTraveltime());
		System.out.println(valueList.size());
		List<TimeTableDTO> tripList = new ArrayList<>();

		int value = valueList.size() / 2;

		for (int i = 0; i < value; i++) {

			/* Validation For Index Out of */
			if (i != valueList.size() - 1) {
				String x = valueList.get(i * 2).toString();
				String y = valueList.get((i * 2) + 1).toString();
				String restTimeString = generalDTO.getRestTime();
				String travelTime = generalDTO.getTraveltime();

				if (isOrigin == true) {

					TimeTableDTO v = new TimeTableDTO(String.valueOf(i + 1), x, y, restTimeString, isFixedOrigin,
							isOrigin, generalDTO.getAbbreviationOrigin(), permitNoOrigin, busNoOrigin, travelTime);

					tripList.add(v);

				} else {

					TimeTableDTO v = new TimeTableDTO(String.valueOf(i + 1), x, y, restTimeString, isFixedDestination,
							isOrigin, generalDTO.getAbbreviationDestination(), permitNoDestination, busNoDestination,
							travelTime);
					tripList.add(v);
				}

			}

		}

		return tripList;
	}

	public List<TimeTableDTO> getLeaveBusDTOList(int noOfLeaveBuses) {
		List<TimeTableDTO> tripList = new ArrayList<>();
		for (int i = 0; i < noOfLeaveBuses; i++) {
			TimeTableDTO dto = new TimeTableDTO();
			tripList.add(dto);
		}
		return tripList;
	}

	public List<String> intervalCalculatorForPanelGenerator(long begin, long end, int noOfTrips, String travelTime) {
		System.out.println("entered intervalCalculatorForPanelGenerator");
		timeList = new ArrayList<>();
		String final_time_start = null;
		String final_time_end = null;
		String beginString = Long.toString(begin);
		String endString = Long.toString(end);

		long routeTotalMinutes = end - begin;
		long interval = routeTotalMinutes / noOfTrips;

		for (long time = begin; time <= end; time += interval) {

			final_time_start = String.format("%02d:%02d", time / 60, time % 60);
			final_time_end = calculateTime(final_time_start, travelTime);

			timeList.add(final_time_start);
			timeList.add(final_time_end);

			/*
			 * If time divide range not enough for same divide as pervious one
			 */
			if (time < end && end - time < interval) {

				String[] parts = final_time_start.split(":");

				long totalHours = Long.parseLong(parts[0]);
				long totalMinutes = Long.parseLong(parts[1]) + (end - time) % 60;

				if (totalMinutes >= 60) {
					totalHours = totalHours + 1;
					totalMinutes = totalMinutes % 60;
				}

				final_time_start = String.format("%02d:%02d", totalHours, totalMinutes);
				final_time_end = calculateTime(final_time_start, travelTime);

				timeList.add(final_time_start);
				timeList.add(final_time_end);
			}

		}

		for (int i = 0; i < timeList.size(); i++) {
			int checkSize = timeList.size() / 2;
			if (i > checkSize && checkSize > noOfTrips) {
				timeList.remove(i);
			}
		}

		return timeList;
	}

	public void generateTimeSlots() {
		// Putting data to the DTO
//		TimeTableDTO panelGeneratorFormData = new TimeTableDTO();

		if (noOfTripsOrigin <= 0 || noOfTripsDestination <= 0 || noOfPrivateLeaveBusesOrigin <= 0) {
			if (noOfTripsOrigin <= 0) {
				setErrorMessage("No of trips Origin must be greater than 0");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

			} else if (noOfTripsDestination <= 0) {
				setErrorMessage("No of trips Destination must be greater than 0");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			} else if (noOfPrivateLeaveBusesOrigin <= 0) {
				setErrorMessage("No of pvt leave buses origin must be greater than 0");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			permitNoListOriginFull = new ArrayList<>();
			busNoListOriginFull = new ArrayList<>();
			permitNoListDestinationFull = new ArrayList<>();
			busNoListDestinationFull = new ArrayList<>();

			panelGeneratorFormData.setGenereatedRefNo(routeDTO.getGenereatedRefNo());
			panelGeneratorFormData.setRouteNo(timeTableDTO.getRouteNo());
			panelGeneratorFormData.setBusCategory(timeTableDTO.getBusCategory());
			panelGeneratorFormData.setOrigin(timeTableDTO.getOrigin());
			panelGeneratorFormData.setDestination(timeTableDTO.getDestination());
			panelGeneratorFormData.setDailyRotation(isDailyRotation);
			panelGeneratorFormData.setTwoDayRotation(isTwoDayRotation);
			panelGeneratorFormData.setGroup(timeTableDTO.getGroup());

			// origin data
			panelGeneratorFormData.setNoOfTripsOrigin(noOfTripsOrigin);
			panelGeneratorFormData.setNoOfPvtBusesOrigin(noOfPrivateBusesOrigin);
			panelGeneratorFormData.setNoOfTemporaryBusesOrigin(noOfTemporaryBusesOrigin);
			panelGeneratorFormData.setNoOfCtbOrigin(noOfCtbOrigin);
			panelGeneratorFormData.setNoOfEtcOrigin(noOfEtcOrigin);
			panelGeneratorFormData.setNoOfPvtLeaveBusesOrigin(noOfPrivateLeaveBusesOrigin);
			panelGeneratorFormData.setNoOfDummyBusesOrigin(noOfDummyBusesOrigin);
			panelGeneratorFormData.setRestTimeOrigin(restTimeOrigin);
			panelGeneratorFormData.setTotalBusesOrigin(totalBusesOrigin);

			// Destination Data
			panelGeneratorFormData.setNoOfTripsDestination(noOfTripsDestination);
			panelGeneratorFormData.setNoOfPvtBusesDestination(noOfPrivateBusesDestination);
			panelGeneratorFormData.setNoOfTemporaryBusesDestination(noOfTemporaryBusesDestination);
			panelGeneratorFormData.setNoOfCtbDestination(noOfCtbDestination);
			panelGeneratorFormData.setNoOfEtcDestination(noOfEtcDestination);
			panelGeneratorFormData.setNoOfPvtLeaveBusesDestination(noOfPrivateLeaveBusesDestination);
			panelGeneratorFormData.setNoOfDummyBusesDestination(noOfDummyBusesDestination);
			panelGeneratorFormData.setRestTimeDestination(restTimeDestination);
			panelGeneratorFormData.setTotalBusesDestination(totalBusesDestination);

			String routeNo = timeTableDTO.getRouteNo();

			// Save Data to the Database
//			timeTableService.savePanelGeneratorFormData(panelGeneratorFormData, sessionBackingBean.getLoginUser());

			// create DTO list for data table
			routeDetailsList = timeTableService.getStartEndTimeForPanelGenerator(routeNo,
					timeTableDTO.getGenereatedRefNo());

			originPVTBusesList = new ArrayList<VehicleInspectionDTO>();
			originPVTBusesList = timeTableService.getPVTbuses(routeNo, "N");

			permitNoList = timeTableService.getPermitNoList(routeNo, true,groupNo,routeDTO.getBusCategory());
			busNoList = timeTableService.getBusNoList(routeNo, true,groupNo,routeDTO.getBusCategory());
			permitNoListOrigin = timeTableService.getPermitNoList(routeNo, false,groupNo,routeDTO.getBusCategory());
			busNoListOrigin = timeTableService.getBusNoList(routeNo, false,groupNo,routeDTO.getBusCategory());

			long beginOrigin = calculateTotalMinutesForStart(routeDetailsList.get(0).getOriginStartTimeString());
			long endOrigin = calculateTotalMinutesForEnd(routeDetailsList.get(0).getOriginEndTimeString());

			long beginDestination = calculateTotalMinutesForStart(
					routeDetailsList.get(0).getDestinationStartTimeString());
			long endDestination = calculateTotalMinutesForEnd(routeDetailsList.get(0).getDestinationEndTimeString());

			panelGeneratorOriginRouteList = timeRangeManagerForPanelGenerator(beginOrigin, endOrigin, noOfTripsOrigin,
					true, routeNo);
			panelGeneratorDestinationRouteList = timeRangeManagerForPanelGenerator(beginDestination, endDestination,
					noOfTripsDestination, false, routeNo);
			panelGeneratorLeaveBusesList = getLeaveBusDTOList(noOfPrivateLeaveBusesOrigin);
		}

	}

	public void submitActionPanelGenerator() {

		routeDTO.setNoOfTripsOrigin(noOfTripsOrigin);
		routeDTO.setNoOfTripsDestination(noOfTripsDestination);
		routeDTO.setNoOfPvtBusesOrigin(pvtBusCountOrigin);
		routeDTO.setNoOfPvtBusesDestination(pvtBusCountDestination);
		routeDTO.setNoOfTemporaryBusesOrigin(noOfTemporaryBusesOrigin);
		routeDTO.setNoOfTemporaryBusesDestination(noOfTemporaryBusesDestination);
		routeDTO.setNoOfCtbOrigin(noOfCtbOrigin);
		routeDTO.setNoOfCtbDestination(noOfCtbDestination);
		routeDTO.setNoOfEtcOrigin(noOfEtcOrigin);
		routeDTO.setNoOfEtcDestination(noOfEtcDestination);
		routeDTO.setNoOfPvtLeaveBusesOrigin(noOfPrivateLeaveBusesOrigin);
		routeDTO.setNoOfPvtLeaveBusesDestination(noOfPrivateLeaveBusesDestination);
		routeDTO.setNoOfDummyBusesOrigin(noOfDummyBusesOrigin);
		routeDTO.setNoOfDummyBusesDestination(noOfDummyBusesDestination);
		routeDTO.setRestTimeOrigin(restTimeOrigin);
		routeDTO.setRestTimeDestination(restTimeDestination);
		routeDTO.setTotalBusesOrigin(totalBusesOrigin);
		routeDTO.setTotalBusesDestination(totalBusesDestination);

		for (TimeTableDTO data : panelGeneratorOriginRouteList) {
			if (panelGeneratorOriginRouteSelectedList.contains(data)) {
				data.setFixBusOrigin(true);
			} else {
				data.setFixBusOrigin(false);
			}
		}

		for (TimeTableDTO data : panelGeneratorDestinationRouteList) {
			if (panelGeneratorDestinationRouteSelectList.contains(data)) {
				data.setFixBusDestination(true);
			} else {
				data.setFixBusDestination(false);
			}
		}

		timeTableService.saveGeneralDetailsEditPanelGenerator(routeDTO, sessionBackingBean.getLoginUser(), true,
				timeTableDTO.getTripRefNo(), "S");

		timeTableService.editOriginTimeSlotTable(routeDTO, panelGeneratorOriginRouteList,
				sessionBackingBean.getLoginUser(), true, timeTableDTO.getTripRefNo(), "S");

		timeTableService.editDestinationTimeSlotTable(routeDTO, panelGeneratorDestinationRouteList,
				sessionBackingBean.getLoginUser(), true, timeTableDTO.getTripRefNo(), "S");

		timeTableService.editLeaveBusesTimeSlotTable(routeDTO, panelGeneratorLeaveBusesList,
				panelGeneratorLeaveBusesDesList, sessionBackingBean.getLoginUser(), true, timeTableDTO.getTripRefNo(),
				"S");

		setSuccessMessage("Submitted Successfully");
		RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

	}

	public StreamedContent generateReport() {
		files = null;

		Connection conn = null;
		try {
			// Obtain a database connection (you need to implement ConnectionManager)
			conn = ConnectionManager.getConnection();

			// Load the JasperReport from the JRXML file
			String sourceFileName = "..//reports//ControlPanel.jrxml";
			String logopath = "//lk//informatics//ntc//view//reports//";
			
			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			// Set parameters for the report
			HashMap<String, Object> parameters = new HashMap<>();
			TimeTableDTO dto = timeTableService.getDetailsForReport(routeNo,busCategory);
			parameters.put("routeDesSinhala", dto.getDescription());
			parameters.put("routeNo", routeNo);
			parameters.put("serviceType", dto.getBusCategoryDes());
			parameters.put("busCategory", dto.getBusCategory());
			parameters.put("P_national_logo", logopath);
			parameters.put("P_ntc_logo", logopath);
			parameters.put("refNo",timeTableDTO.getTripRefNo());

			// Create a JasperPrint object by filling the report with data from the database
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

			// Export the report to PDF as a byte array
			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);

			// Stream the PDF directly to the user for download
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "application/pdf", "ControlPanel.pdf");

			// Store the report bytes and document type in the session map if needed
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("reportBytes", pdfByteArray);
			sessionMap.put("docType", "pdf");

			// Print the generated PDF byte array (for demonstration purposes)
			System.out.println("Generated PDF byte array: " + Arrays.toString(pdfByteArray));

		} catch (JRException e) {
			e.printStackTrace();
			System.err.println("Error generating report: " + e.getMessage());
		} finally {
			ConnectionManager.close(conn); // Make sure to close the connection
		}

		return files;
	}
	
	public void generateBusDetails() {
		 busDetailsList = new ArrayList<>();
		    Map<String, Integer> abbCountMap = new HashMap<>();
		    TimeTableDTO abbriDTO = timeTableService.getAbriviatiosForRoute(routeNo,busCategory);

		    for (TimeTableDTO data : panelGeneratorOriginRouteList) {
		        for (TimeTableDTO dataCheck : panelGeneratorDestinationRouteList) {
		        	 if (data.getAbbreviationOrigin().equals(dataCheck.getAbbreviationDestination())) {
		                 String abbreviationOrigin = data.getAbbreviationOrigin();
		                 int count = abbCountMap.getOrDefault(abbreviationOrigin, 0) + 1;
		                 abbCountMap.put(abbreviationOrigin, count);
		             }
		        }
		    }

		    // Process and add details to the list after counting
		    for (Map.Entry<String, Integer> entry : abbCountMap.entrySet()) {
		        String abbreviationOrigin = entry.getKey();
		        int count = entry.getValue();

		        if(count == 1) {
		        	count +=1;
		        }
		        
		        List<TimeTableDTO> originList = getOriginByAbbreviationList(abbreviationOrigin);
		        List<TimeTableDTO> destinationList = getDestinationByAbbreviationList(abbreviationOrigin);

		        if (!originList.isEmpty() && !destinationList.isEmpty()) {
		            TimeTableDTO busDetails = createBusDetails(count, abbriDTO,originList,destinationList);
		            busDetailsList.add(busDetails);
		        }
		    }

	    timeTableService.updateGeneralDetailsPanelGenerator(busDetailsList,timeTableDTO.getTripRefNo());
	    generateReportBus();
	}

	private  List<TimeTableDTO> getOriginByAbbreviationList(String abbreviation) {
		List<TimeTableDTO> list = new ArrayList<>();
	    for (TimeTableDTO data : panelGeneratorOriginRouteList) {
	        if (data.getAbbreviationOrigin().equals(abbreviation)) {
	        	list.add(data);
	        }
	    }
	    return list;
	}

	// Helper method to get the destination TimeTableDTO by abbreviation
	private  List<TimeTableDTO> getDestinationByAbbreviationList(String abbreviation) {
		List<TimeTableDTO> list = new ArrayList<>();
	    for (TimeTableDTO data : panelGeneratorDestinationRouteList) {
	        if (data.getAbbreviationDestination().equals(abbreviation)) {
	        	list.add(data);
	        }
	    }
	    return list;
	}

	 
	private TimeTableDTO createBusDetails(int count,TimeTableDTO abberiviation,List<TimeTableDTO> originList,List<TimeTableDTO> destinationList) {
		TimeTableDTO generalDTO = timeTableService.getDtoWithGeneralData(routeNo, busCategory);
	    TimeTableDTO busDetails = new TimeTableDTO();
	    
	    busDetails.setAbbreviation(originList.get(0).getAbbreviationOrigin());
	    busDetails.setAbbreviationDes(destinationList.get(0).getAbbreviationDestination());
	    busDetails.setBusNo(originList.get(0).getBusNoOrigin());
	    busDetails.setRouteNo(routeNo);
	    busDetails.setServiceType(generalDTO.getBusCategory());
	    busDetails.setTripno(count);
	    busDetails.setAbbno(originList.get(0).getAbbno());
	    
	    if(count == 1) {
	    	if (originList.get(0).getAbbreviationOrigin().contains(abberiviation.getAbbriAtOrigin())
					|| originList.get(0).getAbbreviationOrigin().contains("SLTB-O")
					|| originList.get(0).getAbbreviationOrigin().contains("ETC-O")) {

	    		busDetails.setOrigin(origin);
	    	    busDetails.setDestination(destination);
	    		busDetails.setArrival(getArrivalTime(originList.get(0).getStartTimeOrigin(), "00:10"));
		    	busDetails.setDepature(originList.get(0).getStartTimeOrigin());
		    	busDetails.setEndTime(originList.get(0).getEndTimeOrigin());	    	 
		    	
			    busDetails.setSteringHours(calculateTime(generalDTO.getTraveltime(), generalDTO.getTraveltime()));
			    busDetails.setServiceTime(getArrivalTime(busDetails.getEndTime(), busDetails.getDepature()));
		    }
		    else {
		    	busDetails.setOrigin(destination);
			    busDetails.setDestination(origin);
		    	busDetails.setArrival(getArrivalTime(destinationList.get(0).getStartTimeDestination(), "00:10"));
		    	busDetails.setDepature(destinationList.get(0).getStartTimeDestination());    	
		    	busDetails.setEndTime(originList.get(0).getEndTimeOrigin());
			    busDetails.setSteringHours(calculateTime(generalDTO.getTraveltime(), generalDTO.getTraveltime()));
			    busDetails.setServiceTime(getArrivalTime(busDetails.getEndTime(), busDetails.getDepature()));
			    
		    }
	    }else if(count == 2) {
	    	if (originList.get(0).getAbbreviationOrigin().contains(abberiviation.getAbbriAtOrigin())
					|| originList.get(0).getAbbreviationOrigin().contains("SLTB-O")
					|| originList.get(0).getAbbreviationOrigin().contains("ETC-O")) {

	    		busDetails.setOrigin(origin);
	    	    busDetails.setDestination(destination);
		        busDetails.setDepatureOne(destinationList.get(0).getStartTimeDestination());
		    	busDetails.setDepature(originList.get(0).getStartTimeOrigin());
			    busDetails.setArrivalOne(originList.get(0).getEndTimeOrigin());
		    	busDetails.setEndTime(destinationList.get(0).getEndTimeDestination());	    	 
		    	busDetails.setArrival(getArrivalTime(originList.get(0).getStartTimeOrigin(), "00:10"));
			    busDetails.setSteringHours(calculateTime(generalDTO.getTraveltime(), generalDTO.getTraveltime()));
			    busDetails.setServiceTime(getArrivalTime(busDetails.getEndTime(), busDetails.getDepature()));
			    busDetails.setRestTime(getArrivalTime(busDetails.getDepatureOne(), busDetails.getArrivalOne()));
		    }
		    else {
		    	busDetails.setOrigin(destination);
			    busDetails.setDestination(origin);
		        busDetails.setDepatureOne(originList.get(0).getStartTimeOrigin());
		    	busDetails.setDepature(destinationList.get(0).getStartTimeDestination());
		    	busDetails.setArrivalOne(destinationList.get(0).getEndTimeDestination());	    	
		    	busDetails.setEndTime(originList.get(0).getEndTimeOrigin());
		    	busDetails.setArrival(getArrivalTime(destinationList.get(0).getStartTimeDestination(), "00:10"));
			    busDetails.setSteringHours(calculateTime(generalDTO.getTraveltime(), generalDTO.getTraveltime()));
			    busDetails.setServiceTime(getArrivalTime(busDetails.getEndTime(), busDetails.getDepature()));
			    busDetails.setRestTime(getArrivalTime(busDetails.getDepatureOne(), busDetails.getArrivalOne()));
			    
		    }
	    }else if(count == 3) {
	    	if (originList.get(0).getAbbreviationOrigin().contains(abberiviation.getAbbriAtOrigin())
					|| originList.get(0).getAbbreviationOrigin().contains("SLTB-O")
					|| originList.get(0).getAbbreviationOrigin().contains("ETC-O")) {

	    		busDetails.setOrigin(origin);
	    	    busDetails.setDestination(destination);
	    		busDetails.setArrival(getArrivalTime(originList.get(0).getStartTimeOrigin(), "00:10"));
	    		busDetails.setDepature(originList.get(0).getStartTimeOrigin());
	    		busDetails.setArrivalOne(originList.get(0).getEndTimeOrigin());
		        busDetails.setDepatureOne(destinationList.get(0).getStartTimeDestination());
		    	busDetails.setArrival2(destinationList.get(0).getEndTimeDestination());
		    	busDetails.setDepature2(originList.get(1).getStartTimeOrigin());
		    	busDetails.setEndTime(originList.get(1).getEndTimeOrigin());
		    	String travel1 = calculateTime(generalDTO.getTraveltime(), generalDTO.getTraveltime());
			    busDetails.setSteringHours(calculateTime(travel1,generalDTO.getTraveltime()));
			    busDetails.setServiceTime(getArrivalTime(busDetails.getEndTime(), busDetails.getDepature()));
			    String restTime1 = getArrivalTime(busDetails.getDepatureOne(),busDetails.getArrivalOne());
			    String restTime2 = getArrivalTime(busDetails.getDepature2(),busDetails.getArrival2());
			    busDetails.setRestTime(calculateTime(restTime1,restTime2));
		    }
		    else {
		    	busDetails.setOrigin(destination);
			    busDetails.setDestination(origin);
		    	busDetails.setArrival(getArrivalTime(destinationList.get(0).getStartTimeDestination(), "00:10"));
		    	busDetails.setDepature(destinationList.get(0).getStartTimeDestination());
		    	busDetails.setArrivalOne(destinationList.get(0).getEndTimeDestination());
		        busDetails.setDepatureOne(originList.get(0).getStartTimeOrigin());
		        busDetails.setArrival2(originList.get(0).getEndTimeOrigin());
		        busDetails.setDepature2(destinationList.get(1).getStartTimeDestination());
		    	busDetails.setEndTime(destinationList.get(1).getEndTimeDestination());
		    	String travel1 = calculateTime(generalDTO.getTraveltime(), generalDTO.getTraveltime());
			    busDetails.setSteringHours(calculateTime(travel1,generalDTO.getTraveltime()));
			    busDetails.setServiceTime(getArrivalTime(busDetails.getEndTime(), busDetails.getDepature()));
			    String restTime1 = getArrivalTime(busDetails.getDepatureOne(),busDetails.getArrivalOne());
			    String restTime2 = getArrivalTime(busDetails.getDepature2(),busDetails.getArrival2());
			    busDetails.setRestTime(calculateTime(restTime1,restTime2));
			    
		    }
	    }
	    else if(count == 4) {
	    	if (originList.get(0).getAbbreviationOrigin().contains(abberiviation.getAbbriAtOrigin())
					|| originList.get(0).getAbbreviationOrigin().contains("SLTB-O")
					|| originList.get(0).getAbbreviationOrigin().contains("ETC-O")) {

	    		busDetails.setOrigin(origin);
	    	    busDetails.setDestination(destination);
	    		busDetails.setArrival(getArrivalTime(originList.get(0).getStartTimeOrigin(), "00:10"));
	    		busDetails.setDepature(originList.get(0).getStartTimeOrigin());
	    		busDetails.setArrivalOne(originList.get(0).getEndTimeOrigin());
		        busDetails.setDepatureOne(destinationList.get(0).getStartTimeDestination());
		    	busDetails.setArrival2(destinationList.get(0).getEndTimeDestination());
		    	busDetails.setDepature2(originList.get(1).getStartTimeOrigin());
		    	busDetails.setArrival3(originList.get(1).getEndTimeOrigin());
		    	busDetails.setDepature3(destinationList.get(1).getStartTimeDestination());
		    	busDetails.setEndTime(destinationList.get(1).getEndTimeDestination());	    	   	
		    	String travel1 = calculateTime(generalDTO.getTraveltime(), generalDTO.getTraveltime());
			    busDetails.setSteringHours(calculateTime(travel1,travel1));
			    busDetails.setServiceTime(getArrivalTime(busDetails.getEndTime(), busDetails.getDepature()));
			    String restTime1 = getArrivalTime(busDetails.getDepatureOne(),busDetails.getArrivalOne());
			    String restTime2 = getArrivalTime(busDetails.getDepature2(),busDetails.getArrival2());
			    String restTime3 = getArrivalTime(busDetails.getDepature3(),busDetails.getArrival3());
			    busDetails.setRestTime(calculateTime(calculateTime(restTime1,restTime2),restTime3));
		    }
		    else {
		    	busDetails.setOrigin(destination);
			    busDetails.setDestination(origin);
		    	busDetails.setArrival(getArrivalTime(destinationList.get(0).getStartTimeDestination(), "00:10"));
		    	busDetails.setDepature(destinationList.get(0).getStartTimeDestination());
		    	busDetails.setArrivalOne(destinationList.get(0).getEndTimeDestination());
		        busDetails.setDepatureOne(originList.get(0).getStartTimeOrigin());
		        busDetails.setArrival2(originList.get(0).getEndTimeOrigin());
		        busDetails.setDepature2(destinationList.get(1).getStartTimeDestination());
		        busDetails.setArrival3(destinationList.get(1).getEndTimeDestination());
		    	busDetails.setDepature3(originList.get(1).getStartTimeOrigin());
		    	busDetails.setEndTime(originList.get(1).getEndTimeOrigin());
		    	String travel1 = calculateTime(generalDTO.getTraveltime(), generalDTO.getTraveltime());
			    busDetails.setSteringHours(calculateTime(travel1,travel1));
			    busDetails.setServiceTime(getArrivalTime(busDetails.getEndTime(), busDetails.getDepature()));
			    String restTime1 = getArrivalTime(busDetails.getDepatureOne(),busDetails.getArrivalOne());
			    String restTime2 = getArrivalTime(busDetails.getDepature2(),busDetails.getArrival2());
			    String restTime3 = getArrivalTime(busDetails.getDepature3(),busDetails.getArrival3());
			    busDetails.setRestTime(calculateTime(calculateTime(restTime1,restTime2),restTime3));
			    
		    }
	    }
	    else if(count == 5) {
	    	if (originList.get(0).getAbbreviationOrigin().contains(abberiviation.getAbbriAtOrigin())
					|| originList.get(0).getAbbreviationOrigin().contains("SLTB-O")
					|| originList.get(0).getAbbreviationOrigin().contains("ETC-O")) {

	    		busDetails.setOrigin(origin);
	    	    busDetails.setDestination(destination);
	    		busDetails.setArrival(getArrivalTime(originList.get(0).getStartTimeOrigin(), "00:10"));
	    		busDetails.setDepature(originList.get(0).getStartTimeOrigin());
	    		busDetails.setArrivalOne(originList.get(0).getEndTimeOrigin());
		        busDetails.setDepatureOne(destinationList.get(0).getStartTimeDestination());
		    	busDetails.setArrival2(destinationList.get(0).getEndTimeDestination());
		    	busDetails.setDepature2(originList.get(1).getStartTimeOrigin());
		    	busDetails.setArrival3(originList.get(1).getEndTimeOrigin());
		    	busDetails.setDepature3(destinationList.get(1).getStartTimeDestination());
		    	busDetails.setArrival4(destinationList.get(1).getEndTimeDestination());	  
		    	busDetails.setDepature4(originList.get(2).getStartTimeOrigin());
		    	busDetails.setEndTime(originList.get(2).getEndTimeOrigin());
		    	String travel1 = calculateTime(generalDTO.getTraveltime(), generalDTO.getTraveltime());
			    busDetails.setSteringHours(calculateTime(calculateTime(travel1,travel1), generalDTO.getTraveltime()));
			    busDetails.setServiceTime(getArrivalTime(busDetails.getEndTime(), busDetails.getDepature()));
			    String restTime1 = getArrivalTime(busDetails.getDepatureOne(),busDetails.getArrivalOne());
			    String restTime2 = getArrivalTime(busDetails.getDepature2(),busDetails.getArrival2());
			    String restTime3 = getArrivalTime(busDetails.getDepature3(),busDetails.getArrival3());
			    String restTime4 = getArrivalTime(busDetails.getDepature4(),busDetails.getArrival4());
			    busDetails.setRestTime(calculateTime(restTime4,calculateTime(calculateTime(restTime1,restTime2),restTime3)));
		    }
		    else {
		    	busDetails.setOrigin(destination);
			    busDetails.setDestination(origin);
		    	busDetails.setArrival(getArrivalTime(destinationList.get(0).getStartTimeDestination(), "00:10"));
		    	busDetails.setDepature(destinationList.get(0).getStartTimeDestination());
		    	busDetails.setArrivalOne(destinationList.get(0).getEndTimeDestination());
		        busDetails.setDepatureOne(originList.get(0).getStartTimeOrigin());
		        busDetails.setArrival2(originList.get(0).getEndTimeOrigin());
		        busDetails.setDepature2(destinationList.get(1).getStartTimeDestination());
		        busDetails.setArrival3(destinationList.get(1).getEndTimeDestination());
		    	busDetails.setDepature3(originList.get(1).getStartTimeOrigin());
		    	busDetails.setArrival4(originList.get(1).getEndTimeOrigin());	  
		    	busDetails.setDepature4(destinationList.get(2).getStartTimeDestination());
		    	busDetails.setEndTime(destinationList.get(2).getEndTimeDestination());
		    	String travel1 = calculateTime(generalDTO.getTraveltime(), generalDTO.getTraveltime());
			    busDetails.setSteringHours(calculateTime(calculateTime(travel1,travel1), generalDTO.getTraveltime()));
			    busDetails.setServiceTime(getArrivalTime(busDetails.getEndTime(), busDetails.getDepature()));
			    String restTime1 = getArrivalTime(busDetails.getDepatureOne(),busDetails.getArrivalOne());
			    String restTime2 = getArrivalTime(busDetails.getDepature2(),busDetails.getArrival2());
			    String restTime3 = getArrivalTime(busDetails.getDepature3(),busDetails.getArrival3());
			    String restTime4 = getArrivalTime(busDetails.getDepature4(),busDetails.getArrival4());
			    busDetails.setRestTime(calculateTime(restTime4,calculateTime(calculateTime(restTime1,restTime2),restTime3)));
			    
		    }
	    }	
	    else if(count == 6) {
	    	if (originList.get(0).getAbbreviationOrigin().contains(abberiviation.getAbbriAtOrigin())
					|| originList.get(0).getAbbreviationOrigin().contains("SLTB-O")
					|| originList.get(0).getAbbreviationOrigin().contains("ETC-O")) {

	    		busDetails.setOrigin(origin);
	    	    busDetails.setDestination(destination);
	    		busDetails.setArrival(getArrivalTime(originList.get(0).getStartTimeOrigin(), "00:10"));
	    		busDetails.setDepature(originList.get(0).getStartTimeOrigin());
	    		busDetails.setArrivalOne(originList.get(0).getEndTimeOrigin());
		        
	    		busDetails.setDepatureOne(destinationList.get(0).getStartTimeDestination());
		    	busDetails.setArrival2(destinationList.get(0).getEndTimeDestination());
		    	
		    	busDetails.setDepature2(originList.get(1).getStartTimeOrigin());
		    	busDetails.setArrival3(originList.get(1).getEndTimeOrigin());
		    	
		    	busDetails.setDepature3(destinationList.get(1).getStartTimeDestination());
		    	busDetails.setArrival4(destinationList.get(1).getEndTimeDestination());	  
		    	
		    	busDetails.setDepature4(originList.get(2).getStartTimeOrigin());
		    	busDetails.setArrival5(originList.get(2).getEndTimeOrigin());
		    	
		    	busDetails.setDepature5(destinationList.get(3).getStartTimeDestination());
		    	busDetails.setEndTime(destinationList.get(3).getEndTimeDestination());
			    
		    	String travel1 = calculateTime(generalDTO.getTraveltime(), generalDTO.getTraveltime());
			    busDetails.setSteringHours(calculateTime(calculateTime(travel1,travel1), travel1));
			    busDetails.setServiceTime(getArrivalTime(busDetails.getEndTime(), busDetails.getDepature()));
			    String restTime1 = getArrivalTime(busDetails.getDepatureOne(),busDetails.getArrivalOne());
			    String restTime2 = getArrivalTime(busDetails.getDepature2(),busDetails.getArrival2());
			    String restTime3 = getArrivalTime(busDetails.getDepature3(),busDetails.getArrival3());
			    String restTime4 = getArrivalTime(busDetails.getDepature4(),busDetails.getArrival4());
			    String restTime5 = getArrivalTime(busDetails.getDepature4(),busDetails.getArrival4());
			    busDetails.setRestTime(calculateTime(restTime5,calculateTime(restTime4,calculateTime(calculateTime(restTime1,restTime2),restTime3))));
		    }
		    else {
		    	busDetails.setOrigin(destination);
			    busDetails.setDestination(origin);
		    	busDetails.setArrival(getArrivalTime(destinationList.get(0).getStartTimeDestination(), "00:10"));
		    	busDetails.setDepature(destinationList.get(0).getStartTimeDestination());
		    	busDetails.setArrivalOne(destinationList.get(0).getEndTimeDestination());
		        busDetails.setDepatureOne(originList.get(0).getStartTimeOrigin());
		        busDetails.setArrival2(originList.get(0).getEndTimeOrigin());
		        busDetails.setDepature2(destinationList.get(1).getStartTimeDestination());
		        busDetails.setArrival3(destinationList.get(1).getEndTimeDestination());
		    	busDetails.setDepature3(originList.get(1).getStartTimeOrigin());
		    	busDetails.setArrival4(originList.get(1).getEndTimeOrigin());	  
		    	busDetails.setDepature4(destinationList.get(2).getStartTimeDestination());
		    	busDetails.setArrival5(destinationList.get(2).getEndTimeDestination());	
		    	busDetails.setDepature5(originList.get(3).getStartTimeOrigin());
		    	busDetails.setEndTime(originList.get(3).getEndTimeOrigin());
			    busDetails.setSteringHours(calculateTime(generalDTO.getTraveltime(), generalDTO.getTraveltime()));
			    busDetails.setServiceTime(getArrivalTime(busDetails.getEndTime(), busDetails.getDepature()));
			    String restTime1 = getArrivalTime(busDetails.getDepatureOne(),busDetails.getArrivalOne());
			    String restTime2 = getArrivalTime(busDetails.getDepature2(),busDetails.getArrival2());
			    String restTime3 = getArrivalTime(busDetails.getDepature3(),busDetails.getArrival3());
			    String restTime4 = getArrivalTime(busDetails.getDepature4(),busDetails.getArrival4());
			    String restTime5 = getArrivalTime(busDetails.getDepature4(),busDetails.getArrival4());
			    busDetails.setRestTime(calculateTime(restTime5,calculateTime(restTime4,calculateTime(calculateTime(restTime1,restTime2),restTime3))));
			    
		    }
	    }	
	  
	    return busDetails;

	}

	public String getArrivalTime(String startTime,String time) {
		String arrivalTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

		try {
			java.util.Date currentArrivalTime = sdf.parse(startTime);
			
			if(!time.trim().isEmpty() || time != null) {
				String[] timeParts = time.split(":");
				int hours = Integer.parseInt(timeParts[0]);
				int minutes = Integer.parseInt(timeParts[1]);

				int minutesToAdd = (hours * 60) + minutes;

				currentArrivalTime = new Date(currentArrivalTime.getTime() - (minutesToAdd * 60000));
				arrivalTime = sdf.format(currentArrivalTime);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrivalTime;
	}
	
	public StreamedContent generateReportBus() {
		files = null;

		Connection conn = null;
		try {
			// Obtain a database connection (you need to implement ConnectionManager)
			conn = ConnectionManager.getConnection();

			// Load the JasperReport from the JRXML file
			String sourceFileName = "..//reports//ControlPanelBus.jrxml";
			String logopath = "//lk//informatics//ntc//view//reports//";
			
			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			// Set parameters for the report
			HashMap<String, Object> parameters = new HashMap<>();
			TimeTableDTO dto = timeTableService.getDetailsForReport(routeNo,busCategory);
			parameters.put("routeDesSinhala", dto.getDescription());
			parameters.put("routeNo", routeNo);
			parameters.put("serviceType", dto.getBusCategoryDes());
			parameters.put("busCategory", busCategory);
			parameters.put("P_national_logo", logopath);
			parameters.put("P_ntc_logo", logopath);
			parameters.put("refNo", timeTableDTO.getTripRefNo());


			// Create a JasperPrint object by filling the report with data from the database
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

			// Export the report to PDF as a byte array
			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);

			// Stream the PDF directly to the user for download
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "application/pdf", "BusTime.pdf");

			// Store the report bytes and document type in the session map if needed
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("reportBytes", pdfByteArray);
			sessionMap.put("docType", "pdf");

			// Print the generated PDF byte array (for demonstration purposes)
			System.out.println("Generated PDF byte array: " + Arrays.toString(pdfByteArray));

		} catch (JRException e) {
			e.printStackTrace();
			System.err.println("Error generating report: " + e.getMessage());
		} finally {
			ConnectionManager.close(conn); // Make sure to close the connection
		}

		return files;
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

	public int getNoOfTripsOrigin() {
		return noOfTripsOrigin;
	}

	public void setNoOfTripsOrigin(int noOfTripsOrigin) {
		this.noOfTripsOrigin = noOfTripsOrigin;
	}

	public int getNoOfPrivateBusesOrigin() {
		return noOfPrivateBusesOrigin;
	}

	public void setNoOfPrivateBusesOrigin(int noOfPrivateBusesOrigin) {
		this.noOfPrivateBusesOrigin = noOfPrivateBusesOrigin;
	}

	public int getNoOfTemporaryBusesOrigin() {
		return noOfTemporaryBusesOrigin;
	}

	public void setNoOfTemporaryBusesOrigin(int noOfTemporaryBusesOrigin) {
		this.noOfTemporaryBusesOrigin = noOfTemporaryBusesOrigin;
	}

	public int getNoOfCtbOrigin() {
		return noOfCtbOrigin;
	}

	public void setNoOfCtbOrigin(int noOfCtbOrigin) {
		this.noOfCtbOrigin = noOfCtbOrigin;
	}

	public int getNoOfEtcOrigin() {
		return noOfEtcOrigin;
	}

	public void setNoOfEtcOrigin(int noOfEtcOrigin) {
		this.noOfEtcOrigin = noOfEtcOrigin;
	}

	public int getNoOfPrivateLeaveBusesOrigin() {
		return noOfPrivateLeaveBusesOrigin;
	}

	public void setNoOfPrivateLeaveBusesOrigin(int noOfPrivateLeaveBusesOrigin) {
		this.noOfPrivateLeaveBusesOrigin = noOfPrivateLeaveBusesOrigin;
	}

	public int getTotalBusesOrigin() {
		return totalBusesOrigin;
	}

	public void setTotalBusesOrigin(int totalBusesOrigin) {
		this.totalBusesOrigin = totalBusesOrigin;
	}

	public int getNoOfDummyBusesOrigin() {
		return noOfDummyBusesOrigin;
	}

	public void setNoOfDummyBusesOrigin(int noOfDummyBusesOrigin) {
		this.noOfDummyBusesOrigin = noOfDummyBusesOrigin;
	}

	public String getRestTimeOrigin() {
		return restTimeOrigin;
	}

	public void setRestTimeOrigin(String restTimeOrigin) {
		this.restTimeOrigin = restTimeOrigin;
	}

	public int getNoOfTripsDestination() {
		return noOfTripsDestination;
	}

	public void setNoOfTripsDestination(int noOfTripsDestination) {
		this.noOfTripsDestination = noOfTripsDestination;
	}

	public int getNoOfPrivateBusesDestination() {
		return noOfPrivateBusesDestination;
	}

	public void setNoOfPrivateBusesDestination(int noOfPrivateBusesDestination) {
		this.noOfPrivateBusesDestination = noOfPrivateBusesDestination;
	}

	public int getNoOfTemporaryBusesDestination() {
		return noOfTemporaryBusesDestination;
	}

	public void setNoOfTemporaryBusesDestination(int noOfTemporaryBusesDestination) {
		this.noOfTemporaryBusesDestination = noOfTemporaryBusesDestination;
	}

	public int getNoOfCtbDestination() {
		return noOfCtbDestination;
	}

	public void setNoOfCtbDestination(int noOfCtbDestination) {
		this.noOfCtbDestination = noOfCtbDestination;
	}

	public int getNoOfEtcDestination() {
		return noOfEtcDestination;
	}

	public void setNoOfEtcDestination(int noOfEtcDestination) {
		this.noOfEtcDestination = noOfEtcDestination;
	}

	public int getNoOfPrivateLeaveBusesDestination() {
		return noOfPrivateLeaveBusesDestination;
	}

	public void setNoOfPrivateLeaveBusesDestination(int noOfPrivateLeaveBusesDestination) {
		this.noOfPrivateLeaveBusesDestination = noOfPrivateLeaveBusesDestination;
	}

	public int getNoOfDummyBusesDestination() {
		return noOfDummyBusesDestination;
	}

	public void setNoOfDummyBusesDestination(int noOfDummyBusesDestination) {
		this.noOfDummyBusesDestination = noOfDummyBusesDestination;
	}

	public int getTotalBusesDestination() {
		return totalBusesDestination;
	}

	public void setTotalBusesDestination(int totalBusesDestination) {
		this.totalBusesDestination = totalBusesDestination;
	}

	public String getRestTimeDestination() {
		return restTimeDestination;
	}

	public void setRestTimeDestination(String restTimeDestination) {
		this.restTimeDestination = restTimeDestination;
	}

	public boolean getIsDailyRotation() {
		return isDailyRotation;
	}

	public void setIsDailyRotation(boolean isDailyRotation) {
		this.isDailyRotation = isDailyRotation;
	}

	public boolean getIsTwoDayRotation() {
		return isTwoDayRotation;
	}

	public void setIsTwoDayRotation(boolean isTwoDayRotation) {
		this.isTwoDayRotation = isTwoDayRotation;
	}

	public List<TimeTableDTO> getPanelGeneratorOriginRouteList() {
		return panelGeneratorOriginRouteList;
	}

	public void setPanelGeneratorOriginRouteList(List<TimeTableDTO> panelGeneratorOriginRouteList) {
		this.panelGeneratorOriginRouteList = panelGeneratorOriginRouteList;
	}

	public String getStartTimeOrigin() {
		return startTimeOrigin;
	}

	public void setStartTimeOrigin(String startTimeOrigin) {
		this.startTimeOrigin = startTimeOrigin;
	}

	public String getEndTimeOrigin() {
		return endTimeOrigin;
	}

	public void setEndTimeOrigin(String endTimeOrigin) {
		this.endTimeOrigin = endTimeOrigin;
	}

	public String getStartTimeDestination() {
		return startTimeDestination;
	}

	public void setStartTimeDestination(String startTimeDestination) {
		this.startTimeDestination = startTimeDestination;
	}

	public String getEndTimeDestination() {
		return endTimeDestination;
	}

	public void setEndTimeDestination(String endTimeDestination) {
		this.endTimeDestination = endTimeDestination;
	}

	public List<String> getPermitNoList() {
		return permitNoList;
	}

	public void setPermitNoList(List<String> permitNoList) {
		this.permitNoList = permitNoList;
	}

	public List<String> getBusNoList() {
		return busNoList;
	}

	public void setBusNoList(List<String> busNoList) {
		this.busNoList = busNoList;
	}

	public List<TimeTableDTO> getPanelGeneratorDestinationRouteList() {
		return panelGeneratorDestinationRouteList;
	}

	public void setPanelGeneratorDestinationRouteList(List<TimeTableDTO> panelGeneratorDestinationRouteList) {
		this.panelGeneratorDestinationRouteList = panelGeneratorDestinationRouteList;
	}

	public TimeTableDTO getPanelGeneratorFormData() {
		return panelGeneratorFormData;
	}

	public void setPanelGeneratorFormData(TimeTableDTO panelGeneratorFormData) {
		this.panelGeneratorFormData = panelGeneratorFormData;
	}

	public boolean getIsFixedOrigin() {
		return isFixedOrigin;
	}

	public void setIsFixedOrigin(boolean isFixedOrigin) {
		this.isFixedOrigin = isFixedOrigin;
	}

	public boolean getIsFixedDestination() {
		return isFixedDestination;
	}

	public void setIsFixedDestination(boolean isFixedDestination) {
		this.isFixedDestination = isFixedDestination;
	}

	public String getAbbreviationOrigin() {
		return abbreviationOrigin;
	}

	public void setAbbreviationOrigin(String abbreviationOrigin) {
		this.abbreviationOrigin = abbreviationOrigin;
	}

	public String getAbbreviationDestination() {
		return abbreviationDestination;
	}

	public void setAbbreviationDestination(String abbreviationDestination) {
		this.abbreviationDestination = abbreviationDestination;
	}

	public String getBusNoOrigin() {
		return busNoOrigin;
	}

	public void setBusNoOrigin(String busNoOrigin) {
		this.busNoOrigin = busNoOrigin;
	}

	public String getBusNoDestination() {
		return busNoDestination;
	}

	public void setBusNoDestination(String busNoDestination) {
		this.busNoDestination = busNoDestination;
	}

	public String getPermitNoOrigin() {
		return permitNoOrigin;
	}

	public void setPermitNoOrigin(String permitNoOrigin) {
		this.permitNoOrigin = permitNoOrigin;
	}

	public String getPermitNoDestination() {
		return permitNoDestination;
	}

	public void setPermitNoDestination(String permitNoDestination) {
		this.permitNoDestination = permitNoDestination;
	}

	public List<String> getAbbreviationList() {
		return abbreviationList;
	}

	public void setAbbreviationList(List<String> abbreviationList) {
		this.abbreviationList = abbreviationList;
	}

	public List<String> getRefNoList() {
		return refNoList;
	}

	public void setRefNoList(List<String> refNoList) {
		this.refNoList = refNoList;
	}

	public String getClickMonday() {
		return clickMonday;
	}

	public void setClickMonday(String clickMonday) {
		this.clickMonday = clickMonday;
	}

	public String getClickTuesday() {
		return clickTuesday;
	}

	public void setClickTuesday(String clickTuesday) {
		this.clickTuesday = clickTuesday;
	}

	public String getClickWednesday() {
		return clickWednesday;
	}

	public void setClickWednesday(String clickWednesday) {
		this.clickWednesday = clickWednesday;
	}

	public String getClickThursday() {
		return clickThursday;
	}

	public void setClickThursday(String clickThursday) {
		this.clickThursday = clickThursday;
	}

	public String getClickFriday() {
		return clickFriday;
	}

	public void setClickFriday(String clickFriday) {
		this.clickFriday = clickFriday;
	}

	public String getClickSaturday() {
		return clickSaturday;
	}

	public void setClickSaturday(String clickSaturday) {
		this.clickSaturday = clickSaturday;
	}

	public String getClickSunday() {
		return clickSunday;
	}

	public void setClickSunday(String clickSunday) {
		this.clickSunday = clickSunday;
	}

	public List<TimeTableDTO> getPanelGeneratorLeaveBusesList() {
		return panelGeneratorLeaveBusesList;
	}

	public void setPanelGeneratorLeaveBusesList(List<TimeTableDTO> panelGeneratorLeaveBusesList) {
		this.panelGeneratorLeaveBusesList = panelGeneratorLeaveBusesList;
	}

	public String getAbbreviationLeave() {
		return abbreviationLeave;
	}

	public void setAbbreviationLeave(String abbreviationLeave) {
		this.abbreviationLeave = abbreviationLeave;
	}

	public String getBusNoLeave() {
		return busNoLeave;
	}

	public void setBusNoLeave(String busNoLeave) {
		this.busNoLeave = busNoLeave;
	}

	public String getPermitNoLeave() {
		return permitNoLeave;
	}

	public void setPermitNoLeave(String permitNoLeave) {
		this.permitNoLeave = permitNoLeave;
	}

	public boolean isRenderForm() {
		return renderForm;
	}

	public void setRenderForm(boolean renderForm) {
		this.renderForm = renderForm;
	}

	public List<String> getRefNoListForEdit() {
		return refNoListForEdit;
	}

	public void setRefNoListForEdit(List<String> refNoListForEdit) {
		this.refNoListForEdit = refNoListForEdit;
	}

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public String getRouteNo() {
		return routeNo;
	}

	public void setRouteNo(String routeNo) {
		this.routeNo = routeNo;
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

	public String getBusCategory() {
		return busCategory;
	}

	public void setBusCategory(String busCategory) {
		this.busCategory = busCategory;
	}

	public String getNoOfTablesPerWeek() {
		return noOfTablesPerWeek;
	}

	public void setNoOfTablesPerWeek(String noOfTablesPerWeek) {
		this.noOfTablesPerWeek = noOfTablesPerWeek;
	}

	public String getNoOfTimeTablesPerWeek() {
		return noOfTimeTablesPerWeek;
	}

	public void setNoOfTimeTablesPerWeek(String noOfTimeTablesPerWeek) {
		this.noOfTimeTablesPerWeek = noOfTimeTablesPerWeek;
	}

	public String getRouteStatus() {
		return routeStatus;
	}

	public void setRouteStatus(String routeStatus) {
		this.routeStatus = routeStatus;
	}

	public String getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}

	public PanelGeneratorDTO getPanelGeneratorDTO() {
		return panelGeneratorDTO;
	}

	public void setPanelGeneratorDTO(PanelGeneratorDTO panelGeneratorDTO) {
		this.panelGeneratorDTO = panelGeneratorDTO;
	}

	public PanelGeneratorService getPanelGeneratorService() {
		return panelGeneratorService;
	}

	public void setPanelGeneratorService(PanelGeneratorService panelGeneratorService) {
		this.panelGeneratorService = panelGeneratorService;
	}

	public boolean isGroup1() {
		return group1;
	}

	public void setGroup1(boolean group1) {
		this.group1 = group1;
	}

	public boolean isGroup2() {
		return group2;
	}

	public void setGroup2(boolean group2) {
		this.group2 = group2;
	}

	public boolean isGroup3() {
		return group3;
	}

	public void setGroup3(boolean group3) {
		this.group3 = group3;
	}

	public boolean isDaily() {
		return daily;
	}

	public void setDaily(boolean daily) {
		this.daily = daily;
	}

	public boolean isTwoDay() {
		return twoDay;
	}

	public void setTwoDay(boolean twoDay) {
		this.twoDay = twoDay;
	}

	public List<PanelGeneratorDTO> getGroup1_OD_List() {
		return group1_OD_List;
	}

	public void setGroup1_OD_List(List<PanelGeneratorDTO> group1_OD_List) {
		this.group1_OD_List = group1_OD_List;
	}

	public List<PanelGeneratorDTO> getGroup2_OD_List() {
		return group2_OD_List;
	}

	public void setGroup2_OD_List(List<PanelGeneratorDTO> group2_OD_List) {
		this.group2_OD_List = group2_OD_List;
	}

	public List<PanelGeneratorDTO> getGroup3_OD_List() {
		return group3_OD_List;
	}

	public void setGroup3_OD_List(List<PanelGeneratorDTO> group3_OD_List) {
		this.group3_OD_List = group3_OD_List;
	}

	public List<PanelGeneratorDTO> getGroup1_DO_List() {
		return group1_DO_List;
	}

	public void setGroup1_DO_List(List<PanelGeneratorDTO> group1_DO_List) {
		this.group1_DO_List = group1_DO_List;
	}

	public List<PanelGeneratorDTO> getGroup2_DO_List() {
		return group2_DO_List;
	}

	public void setGroup2_DO_List(List<PanelGeneratorDTO> group2_DO_List) {
		this.group2_DO_List = group2_DO_List;
	}

	public List<PanelGeneratorDTO> getGroup3_DO_List() {
		return group3_DO_List;
	}

	public void setGroup3_DO_List(List<PanelGeneratorDTO> group3_DO_List) {
		this.group3_DO_List = group3_DO_List;
	}

	public void setDailyRotation(boolean isDailyRotation) {
		this.isDailyRotation = isDailyRotation;
	}

	public void setTwoDayRotation(boolean isTwoDayRotation) {
		this.isTwoDayRotation = isTwoDayRotation;
	}

	public void setFixedOrigin(boolean isFixedOrigin) {
		this.isFixedOrigin = isFixedOrigin;
	}

	public void setFixedDestination(boolean isFixedDestination) {
		this.isFixedDestination = isFixedDestination;
	}

	public List<TimeTableDTO> getPanelGeneratorOriginRouteSelectedList() {
		return panelGeneratorOriginRouteSelectedList;
	}

	public void setPanelGeneratorOriginRouteSelectedList(List<TimeTableDTO> panelGeneratorOriginRouteSelectedList) {
		this.panelGeneratorOriginRouteSelectedList = panelGeneratorOriginRouteSelectedList;
	}

	public List<TimeTableDTO> getPanelGeneratorDestinationRouteSelectList() {
		return panelGeneratorDestinationRouteSelectList;
	}

	public void setPanelGeneratorDestinationRouteSelectList(
			List<TimeTableDTO> panelGeneratorDestinationRouteSelectList) {
		this.panelGeneratorDestinationRouteSelectList = panelGeneratorDestinationRouteSelectList;
	}

	public List<String> getAbbreviationListNew() {
		return abbreviationListNew;
	}

	public void setAbbreviationListNew(List<String> abbreviationListNew) {
		this.abbreviationListNew = abbreviationListNew;
	}

	public List<String> getAbbreviationListOri() {
		return abbreviationListOri;
	}

	public void setAbbreviationListOri(List<String> abbreviationListOri) {
		this.abbreviationListOri = abbreviationListOri;
	}

	public List<String> getAbbreviationListDes() {
		return abbreviationListDes;
	}

	public void setAbbreviationListDes(List<String> abbreviationListDes) {
		this.abbreviationListDes = abbreviationListDes;
	}

	public List<String> getAbbreviationListLeaveOri() {
		return abbreviationListLeaveOri;
	}

	public void setAbbreviationListLeaveOri(List<String> abbreviationListLeaveOri) {
		this.abbreviationListLeaveOri = abbreviationListLeaveOri;
	}

	public List<String> getAbbreviationListLeaveDes() {
		return abbreviationListLeaveDes;
	}

	public void setAbbreviationListLeaveDes(List<String> abbreviationListLeaveDes) {
		this.abbreviationListLeaveDes = abbreviationListLeaveDes;
	}

	public List<TimeTableDTO> getPanelGeneratorLeaveBusesDesList() {
		return panelGeneratorLeaveBusesDesList;
	}

	public void setPanelGeneratorLeaveBusesDesList(List<TimeTableDTO> panelGeneratorLeaveBusesDesList) {
		this.panelGeneratorLeaveBusesDesList = panelGeneratorLeaveBusesDesList;
	}

	public boolean isDisableBtn() {
		return disableBtn;
	}

	public void setDisableBtn(boolean disableBtn) {
		this.disableBtn = disableBtn;
	}

	public boolean isDisableSaveBtn() {
		return disableSaveBtn;
	}

	public void setDisableSaveBtn(boolean disableSaveBtn) {
		this.disableSaveBtn = disableSaveBtn;
	}

	public String getAbbreviationLeaveDes() {
		return abbreviationLeaveDes;
	}

	public void setAbbreviationLeaveDes(String abbreviationLeaveDes) {
		this.abbreviationLeaveDes = abbreviationLeaveDes;
	}

	public String getBusNoLeaveDes() {
		return busNoLeaveDes;
	}

	public void setBusNoLeaveDes(String busNoLeaveDes) {
		this.busNoLeaveDes = busNoLeaveDes;
	}

	public String getPermitNoLeaveDes() {
		return permitNoLeaveDes;
	}

	public void setPermitNoLeaveDes(String permitNoLeaveDes) {
		this.permitNoLeaveDes = permitNoLeaveDes;
	}

	public List<String> getPermitNoListOrigin() {
		return permitNoListOrigin;
	}

	public void setPermitNoListOrigin(List<String> permitNoListOrigin) {
		this.permitNoListOrigin = permitNoListOrigin;
	}

	public List<String> getBusNoListOrigin() {
		return busNoListOrigin;
	}

	public void setBusNoListOrigin(List<String> busNoListOrigin) {
		this.busNoListOrigin = busNoListOrigin;
	}

	public String getBusCategoryDes() {
		return busCategoryDes;
	}

	public void setBusCategoryDes(String busCategoryDes) {
		this.busCategoryDes = busCategoryDes;
	}

	public List<String> getPermitNoListOriginFull() {
		return permitNoListOriginFull;
	}

	public void setPermitNoListOriginFull(List<String> permitNoListOriginFull) {
		this.permitNoListOriginFull = permitNoListOriginFull;
	}

	public List<String> getBusNoListOriginFull() {
		return busNoListOriginFull;
	}

	public void setBusNoListOriginFull(List<String> busNoListOriginFull) {
		this.busNoListOriginFull = busNoListOriginFull;
	}

	public List<String> getPermitNoListDestinationFull() {
		return permitNoListDestinationFull;
	}

	public void setPermitNoListDestinationFull(List<String> permitNoListDestinationFull) {
		this.permitNoListDestinationFull = permitNoListDestinationFull;
	}

	public List<String> getBusNoListDestinationFull() {
		return busNoListDestinationFull;
	}

	public void setBusNoListDestinationFull(List<String> busNoListDestinationFull) {
		this.busNoListDestinationFull = busNoListDestinationFull;
	}

	public List<String> getAbbreviationListSltbOri() {
		return abbreviationListSltbOri;
	}

	public void setAbbreviationListSltbOri(List<String> abbreviationListSltbOri) {
		this.abbreviationListSltbOri = abbreviationListSltbOri;
	}

	public List<String> getAbbreviationListSltbDes() {
		return abbreviationListSltbDes;
	}

	public void setAbbreviationListSltbDes(List<String> abbreviationListSltbDes) {
		this.abbreviationListSltbDes = abbreviationListSltbDes;
	}

	public List<String> getAbbreviationListEtcOri() {
		return abbreviationListEtcOri;
	}

	public void setAbbreviationListEtcOri(List<String> abbreviationListEtcOri) {
		this.abbreviationListEtcOri = abbreviationListEtcOri;
	}

	public List<String> getAbbreviationListEtcDes() {
		return abbreviationListEtcDes;
	}

	public void setAbbreviationListEtcDes(List<String> abbreviationListEtcDes) {
		this.abbreviationListEtcDes = abbreviationListEtcDes;
	}

	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
	}

	public List<TimeTableDTO> getBusDetailsList() {
		return busDetailsList;
	}

	public void setBusDetailsList(List<TimeTableDTO> busDetailsList) {
		this.busDetailsList = busDetailsList;
	}

	public List<String> getDateList_Group1() {
		return dateList_Group1;
	}

	public void setDateList_Group1(List<String> dateList_Group1) {
		this.dateList_Group1 = dateList_Group1;
	}

	public List<String> getDateList_Group2() {
		return dateList_Group2;
	}

	public void setDateList_Group2(List<String> dateList_Group2) {
		this.dateList_Group2 = dateList_Group2;
	}

	public List<String> getDateList_Group3() {
		return dateList_Group3;
	}

	public void setDateList_Group3(List<String> dateList_Group3) {
		this.dateList_Group3 = dateList_Group3;
	}
	
}
