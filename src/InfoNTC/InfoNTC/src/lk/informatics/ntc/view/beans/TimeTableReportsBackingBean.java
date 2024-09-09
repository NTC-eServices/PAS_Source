package lk.informatics.ntc.view.beans;

import java.io.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.component.column.Column;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.CombinePanelGenaratorDTO;
import lk.informatics.ntc.model.dto.LogSheetMaintenanceDTO;
import lk.informatics.ntc.model.dto.RouteScheduleDTO;
import lk.informatics.ntc.model.dto.SetUpMidPointsDTO;
import lk.informatics.ntc.model.dto.TimeTableDTO;
import lk.informatics.ntc.model.service.CombineTimeTableGenerateService;
import lk.informatics.ntc.model.service.ReportService;
import lk.informatics.ntc.model.service.RouteScheduleService;
import lk.informatics.ntc.model.service.TimeTableService;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.ntc.view.util.SpringApplicationContex;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/**
 * @author tharushi.e
 *
 */
@ManagedBean(name = "timeTableReportsBackingBean")
@ViewScoped
public class TimeTableReportsBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	private List<String> routeList = new ArrayList<String>();
	private List<CombinePanelGenaratorDTO> fixBus;
	private List<LogSheetMaintenanceDTO> serviceList = new ArrayList<LogSheetMaintenanceDTO>();;
	private String route;

	private String serviceType;
	private ReportService reportService;
	private TimeTableService timeTableService;
	private StreamedContent files;
	private String orgin;
	private String destination, depSide, tripType, groupNo, refNo, serviceCode, serviceTypeDes;
	public String distance, travelTime, speed;

	private List<String> timeSlotList = new ArrayList<String>();
	private List<String> timeSlotListFix = new ArrayList<String>();
	private List<String> endTimeSlotList = new ArrayList<String>();
	private Map<String, List> busNoList;
	private List<String> groupList = new ArrayList<>();
	private List<String> departureList = new ArrayList<>();
	private List<String> refNoList = new ArrayList<>();
	private List<String> getServiceType = new ArrayList<>();
	private List<String> getRouteNo = new ArrayList<>();
	private boolean isControlSheetGet;
	private String mon;
	private String rptType;

	private String errorMsg, successMessage;
	private CombineTimeTableGenerateService combineTimeTableGenerateService;
	private String startMon;
	String startMonth = null;
	String lastMonth = null;
	private List<String> startMonthList;
	private List<String> endMonthList;
	private String dateRange;
	private List<String> stelectMonthList;
	private Date printedDate;
	private int monthCount = 0;
	private String service;
	private boolean isMoreThanThirty = false;
	private boolean isTwoDay;
	private boolean renderLeave;

	private List<TimeTableDTO> rotationList;

	java.util.Date date = new java.util.Date();
	Timestamp timestamp = new Timestamp(date.getTime());

	private List<Column> generatedColumns = new ArrayList<>();

	int monthDays = 0;

	private StreamedContent rotationSheet;

	private RouteScheduleService routeScheduleService;
	private List<Integer> dateList;
	
	/* Added & changed all method according to this variable dhananjika.d 20/08/2024 */
	private String reportRefNo;

	@PostConstruct
	public void init() {
		timeTableService = (TimeTableService) SpringApplicationContex.getBean("timeTableService");
		routeScheduleService = (RouteScheduleService) SpringApplicationContex.getBean("routeScheduleService");
		reportService = (ReportService) SpringApplicationContex.getBean("reportService");
		combineTimeTableGenerateService = (CombineTimeTableGenerateService) SpringApplicationContex
				.getBean("combineTimeTableGenerateService");
		routeList = reportService.routeNoDropdown();
		serviceList = reportService.serviceTypeDropDown();
		refNoList = reportService.getRefferenceNo();
		isControlSheetGet = false;
		stelectMonthList = new ArrayList<>();
		rotationList = new ArrayList<>();
		fixBus = new ArrayList<CombinePanelGenaratorDTO>();
		String service = null;

		loadValue();
	}

	private void loadValue() {
		renderLeave = false;
		isTwoDay = false;
		dateList = new ArrayList<>();
	}

	/**
	 * show origin and destination
	 */
	public void onRouteChange() {
		departureList = new ArrayList<>();
		orgin = reportService.getOrginByRoute(route);

		destination = reportService.getODestinationByRoute(route);
		departureList.add(orgin);
		departureList.add(destination);

	}

	/**
	 * end
	 */
	public void onRefChange() {
		groupList = reportService.getGroupNo(refNo);
		getServiceType = reportService.getServiceType(refNo);
		getRouteNo = reportService.getRouteNo(refNo);
		startMonth = reportService.getStartDateByRefNo(refNo);
		lastMonth = reportService.getEndDateByRefNo(refNo);
	}

	/* get month list according to date range */
	public void dateRangeChange() throws ParseException {
		boolean middleMonthHave = false;
		boolean onlyOneMonth = false;
		stelectMonthList = new ArrayList<>();
		if (dateRange != null && !dateRange.trim().isEmpty()) {
			String startDate = dateRange.trim().substring(0, 10);
			String endDate = dateRange.trim().substring(dateRange.lastIndexOf("-") + 1);

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date date = sdf.parse(startDate);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);

			Date date2 = sdf.parse(endDate);
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(date2);

			int monthsDiff = cal2.get(Calendar.MONTH) - cal.get(Calendar.MONTH);

			int count = 0;
			while (count <= monthsDiff) {
				count++;
				stelectMonthList.add(String.valueOf(count));
			}

		}
	}

	public void onDepartureSIdeChange() {

		if (depSide.equals(orgin)) {
			tripType = "O";
		} else if (depSide.equals(destination)) {
			tripType = "D";
		}
		startMonthList = reportService.getStartMonthList(refNo, tripType);
	}
	
	/* make refNo unique:  dhananjika.d 20/08/2024*/
	public String generateRandomString() {
		String generatedString = null;
		int length = 10;

		boolean useLetters = true;
		boolean useNumbers = true;

		generatedString = RandomStringUtils.random(length, useLetters, useNumbers);

		return generatedString + refNo;
	}

	/**
	 * 
	 * Generate Control Sheet
	 * 
	 * @throws ParseException
	 */

	/* print control sheet */
	public StreamedContent printControlSheet(ActionEvent ae) throws JRException, ParseException {

		boolean busAbreHave = false;
		boolean leaveHave = false;
		boolean busNumAssign = false;
		String originBus = null;
		String destinationBus = null;

		List<String> busAbbreviationOrderListForOrgin = new ArrayList<>();
		List<String> busAbbreviationOrderListForDestination = new ArrayList<>();

		List<String> busNoListForOrigin = new ArrayList<>();
		List<String> busNoListForDestination = new ArrayList<>();
		
		reportRefNo = generateRandomString();

		if (rptType != null && !rptType.trim().isEmpty()) {
			if (refNo != null && groupNo != null && route != null && serviceTypeDes != null && !refNo.trim().isEmpty()
					&& !groupNo.trim().isEmpty() && !route.trim().isEmpty() && !serviceTypeDes.trim().isEmpty()
					&& mon != null && !mon.trim().isEmpty() && dateRange != null && !dateRange.trim().isEmpty()) {

				if (depSide.equals(orgin)) {
					tripType = "O";
				} else if (depSide.equals(destination)) {
					tripType = "D";
				}

				if (serviceTypeDes.equalsIgnoreCase("NORMAL")) {

					service = "001";
				}
				if (serviceTypeDes.equalsIgnoreCase("LUXURY")) {

					service = "002";
				}
				if (serviceTypeDes.equalsIgnoreCase("SUPER LUXURY")) {

					service = "003";
				}
				if (serviceTypeDes.equalsIgnoreCase("SEMI-LUXURY")) {

					service = "004";
				}
				if (serviceTypeDes.equalsIgnoreCase("EXPRESSWAY BUS")) {

					service = "EB";
				}
				// create date for printed control sheet
				boolean isFromHistory = reportService.getselectedDateRangeFromHistory(refNo, tripType, dateRange);
				isTwoDay = reportService.getTwoDayRotation(refNo, tripType);

				if (isTwoDay) {
					renderLeave = true;
				}

				leaveHave = reportService.checkLeave(tripType, groupNo, route, service, refNo);
				int dateCount = getDaysForMonths();
				int previousDatesCount = getDaysForPreviousMonths();
				int datesForMon = 0;
				if (leaveHave) {

					timeSlotList = reportService.getTimeSlots(tripType, groupNo, route, service, refNo, false, null);
					timeSlotListFix = reportService.getFixTimeSlots(tripType, groupNo, route, service, refNo, false,
							null);

					originBus = reportService.getAbriviatiosForRoute(route, service, "O");
					destinationBus = reportService.getAbriviatiosForRoute(route, service, "D");
					/** add pvt bus as fixed bus abbrivation **/
					busAbbreviationOrderListForOrgin = reportService.getOriginBusListWithLeave(refNo, "O");
					busAbbreviationOrderListForDestination = reportService.getOriginBusListWithLeave(refNo, "D");

					busNoListForOrigin = reportService.busNoListWithLeave(originBus, destinationBus, refNo, "O",
							groupNo);
					busNoListForDestination = reportService.busNoListWithLeave(originBus, destinationBus, refNo, "D",
							groupNo);
					/** add pvt bus as fixed bus abbrivation end **/
					/** End get timeslots **/

					/** get bus nos **/

					reportService.insertLeavesInTable(refNo, tripType, groupNo, originBus, destinationBus, dateCount,
							null, previousDatesCount,reportRefNo);

					busNoList = reportService.getBusNOListNewWithLeave(refNo, tripType, groupNo, timeSlotList,
							busNoListForOrigin, busNoListForDestination, originBus, destinationBus, dateCount,
							isFromHistory, timeSlotListFix, busAbbreviationOrderListForOrgin,
							busAbbreviationOrderListForDestination, previousDatesCount,reportRefNo);

					distance = reportService.getDistanceForRoute(route, service);
					speed = reportService.getSpeedForROute(route, service);
					travelTime = reportService.getTravelTimeForROute(route, service);

					return download(ae);

				} else {

					/* Changed method by dhananjika.d 14/03/2024 */

					timeSlotList = reportService.getTimeSlots(tripType, groupNo, route, service, refNo, false, null);
					timeSlotListFix = reportService.getFixTimeSlots(tripType, groupNo, route, service, refNo, false,
							null);

					originBus = reportService.getAbriviatiosForRoute(route, service, "O");
					destinationBus = reportService.getAbriviatiosForRoute(route, service, "D");
					/** add pvt bus as fixed bus abbrivation **/
					busAbbreviationOrderListForOrgin = reportService.getOriginBusListWithLeave(refNo, "O");
					busAbbreviationOrderListForDestination = reportService.getOriginBusListWithLeave(refNo, "D");

					busNoListForOrigin = reportService.busNoListWithLeave(originBus, destinationBus, refNo, "O",
							groupNo);
					busNoListForDestination = reportService.busNoListWithLeave(originBus, destinationBus, refNo, "D",
							groupNo);

					busNoList = reportService.getBusNOListNew(refNo, tripType, groupNo, timeSlotList,
							busNoListForOrigin, busNoListForDestination, originBus, destinationBus, dateCount,
							isFromHistory, timeSlotListFix, busAbbreviationOrderListForOrgin,
							busAbbreviationOrderListForDestination, previousDatesCount,reportRefNo);

					distance = reportService.getDistanceForRoute(route, service);
					speed = reportService.getSpeedForROute(route, service);
					travelTime = reportService.getTravelTimeForROute(route, service);

					return download(ae);
				}
			} else {

				setErrorMsg("Please select  mandotory fields.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

			}
		} else {

			setErrorMsg("Please select  a report type.");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		}
		return null;
	}

	/* print control sheet for two day */
	public StreamedContent printControlSheetForTwoDay(ActionEvent ae) throws JRException, ParseException {

		boolean busAbreHave = false;
		boolean leaveHave = false;
		boolean busNumAssign = false;
		String originBus = null;
		String destinationBus = null;


		List<String> busAbbreviationOrderListForOrgin = new ArrayList<>();
		List<String> busAbbreviationOrderListForDestination = new ArrayList<>();

		List<String> busNoListForOrigin = new ArrayList<>();
		List<String> busNoListForDestination = new ArrayList<>();

		reportRefNo = generateRandomString();
		
		if (rptType != null && !rptType.trim().isEmpty()) {
			if (refNo != null && groupNo != null && route != null && serviceTypeDes != null && !refNo.trim().isEmpty()
					&& !groupNo.trim().isEmpty() && !route.trim().isEmpty() && !serviceTypeDes.trim().isEmpty()
					&& mon != null && !mon.trim().isEmpty() && dateRange != null && !dateRange.trim().isEmpty()) {

				if (depSide.equals(orgin)) {
					tripType = "O";
				} else if (depSide.equals(destination)) {
					tripType = "D";
				}

				if (serviceTypeDes.equalsIgnoreCase("NORMAL")) {

					service = "001";
				}
				if (serviceTypeDes.equalsIgnoreCase("LUXURY")) {

					service = "002";
				}
				if (serviceTypeDes.equalsIgnoreCase("SUPER LUXURY")) {

					service = "003";
				}
				if (serviceTypeDes.equalsIgnoreCase("SEMI-LUXURY")) {

					service = "004";
				}
				if (serviceTypeDes.equalsIgnoreCase("EXPRESSWAY BUS")) {

					service = "EB";
				}
				// create date for printed control sheet
				boolean isFromHistory = reportService.getselectedDateRangeFromHistory(refNo, tripType, dateRange);
				isTwoDay = reportService.getTwoDayRotation(refNo, tripType);

				if (isTwoDay) {
					renderLeave = true;
				}

				leaveHave = reportService.checkLeaveTwoDay(tripType, groupNo, route, service, refNo);
				int dateCount = getDaysForMonths();
				int previousDatesCount = getDaysForPreviousMonths();
				int datesForMon = 0;
				if (leaveHave) {

					timeSlotList = reportService.getTimeSlots(tripType, groupNo, route, service, refNo, false, null);
					timeSlotListFix = reportService.getFixTimeSlots(tripType, groupNo, route, service, refNo, false,
							null);

					originBus = reportService.getAbriviatiosForRoute(route, service, "D");
					destinationBus = reportService.getAbriviatiosForRoute(route, service, "O");
					/** add pvt bus as fixed bus abbrivation **/
					busAbbreviationOrderListForOrgin = reportService.getOriginBusListWithLeave(refNo, "D");
					busAbbreviationOrderListForDestination = reportService.getOriginBusListWithLeave(refNo, "O");

					busNoListForOrigin = reportService.busNoListWithLeave(originBus, destinationBus, refNo, "D",
							groupNo);
					busNoListForDestination = reportService.busNoListWithLeave(originBus, destinationBus, refNo, "O",
							groupNo);
					/** add pvt bus as fixed bus abbrivation end **/
					/** End get timeslots **/

					/** get bus nos **/

					reportService.insertLeavesInTableForTwoDay(refNo, tripType, groupNo, originBus, destinationBus,
							dateCount, null, previousDatesCount,reportRefNo);

					busNoList = reportService.getBusNOListNewWithLeaveTwoDay(refNo, tripType, groupNo, timeSlotList,
							busNoListForOrigin, busNoListForDestination, originBus, destinationBus, dateCount,
							isFromHistory, timeSlotListFix, busAbbreviationOrderListForOrgin,
							busAbbreviationOrderListForDestination, previousDatesCount,reportRefNo);

					distance = reportService.getDistanceForRoute(route, service);
					speed = reportService.getSpeedForROute(route, service);
					travelTime = reportService.getTravelTimeForROute(route, service);

					return download(ae);

				} else {

					timeSlotList = reportService.getTimeSlots(tripType, groupNo, route, service, refNo, false, null);
					timeSlotListFix = reportService.getFixTimeSlots(tripType, groupNo, route, service, refNo, false,
							null);

					/** End get timeslots **/

					originBus = reportService.getAbriviatiosForRoute(route, service, "O");
					destinationBus = reportService.getAbriviatiosForRoute(route, service, "D");

					busAbbreviationOrderListForOrgin = reportService.busAbbreviationOrder(originBus, destinationBus,
							refNo, "O", groupNo);
					busAbbreviationOrderListForDestination = reportService.busAbbreviationOrder(originBus,
							destinationBus, refNo, "D", groupNo);
					/** get bus nos **/

					busNoList = reportService.getBusNOListNewForTwoDay(refNo, tripType, groupNo, timeSlotList,
							busAbbreviationOrderListForOrgin, busAbbreviationOrderListForDestination, originBus,
							destinationBus, dateCount, isFromHistory, timeSlotListFix, previousDatesCount, reportRefNo);

					distance = reportService.getDistanceForRoute(route, service);
					speed = reportService.getSpeedForROute(route, service);
					travelTime = reportService.getTravelTimeForROute(route, service);

					return download(ae);
				}
			} else {

				setErrorMsg("Please select  mandotory fields.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

			}
		} else {

			setErrorMsg("Please select  a report type.");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		}
		return null;
	}
	
	/* To generate control sheet */
	public StreamedContent download(ActionEvent ae) throws JRException {
		if (ae.getComponent().getId().equals("ptintIDXLS")) {
			// Excel Generator
			int currentRow = 0;
			try {
				XSSFWorkbook workbook = new XSSFWorkbook();
				Sheet sheet = workbook.createSheet("Control Sheet");

				for (int pageCount = 0; pageCount < 5; pageCount++) {
					int maxWidth = 12;
					if (pageCount == 4) {
						maxWidth = 14;
					}

					currentRow += 3;
					createExcelHeader(workbook, sheet, currentRow, maxWidth, "ABC", "EFG");
					currentRow += 6;
					Row header = sheet.createRow(currentRow);

					CellStyle headerStyle = workbook.createCellStyle();
					XSSFFont font = workbook.createFont();
					font.setBold(true);
					headerStyle.setFont(font);

					Cell headerCell = header.createCell(0);
					headerCell.setCellValue(
							"From " + orgin + " To " + destination + " (Route No: " + route + ") " + serviceTypeDes);
					headerCell.setCellStyle(headerStyle);

					sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, maxWidth));
					currentRow++;

					header = sheet.createRow(currentRow);
					headerCell = header.createCell(0);
					headerCell.setCellValue("Way To Bus From " + depSide);
					headerCell.setCellStyle(headerStyle);
					sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, maxWidth));
					currentRow++;

					header = sheet.createRow(currentRow);
					headerCell = header.createCell(0);
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MMMM");
					headerCell.setCellValue("Date: " + format.format(printedDate) + "   |   Departure: " + depSide);
					headerCell.setCellStyle(headerStyle);
					sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, maxWidth));
					currentRow++;

					header = sheet.createRow(currentRow);
					headerCell = header.createCell(0);
					headerCell.setCellValue("");
					headerCell.setCellStyle(headerStyle);

					int dateColCount = 6;
					if (pageCount == 4) {
						dateColCount = 7;
					}
					for (int x = 1; x <= dateColCount; x++) {
						headerCell = header.createCell(x + (x - 1));
						headerCell.setCellValue(x + (pageCount * 6));
						headerCell.setCellStyle(headerStyle);

						headerCell = header.createCell(2 * x);
						headerCell.setCellValue(x + (pageCount * 6));
						headerCell.setCellStyle(headerStyle);
					}

					CellStyle style = workbook.createCellStyle();
					style.setWrapText(true);

					// Fetch and fill available bus
					try (Connection conn = ConnectionManager.getConnection()) {
						Map<String, Integer> timeRowMap = new HashedMap();
						String sql;
						if (pageCount == 0) {
							sql = "select * from public.nt_m_control_sheet;";
						} else {
							sql = "select * from public.nt_m_control_sheet" + (pageCount + 1) + ";";
						}
						currentRow++;

						try (PreparedStatement preparedStatement = conn.prepareStatement(sql);
								ResultSet rs = preparedStatement.executeQuery()) {

							while (rs.next()) {
								String timeSlot;
								if (pageCount != 4) {
									timeSlot = rs.getString("time_slot");
								} else {
									timeSlot = rs.getString("col7");
								}
								Row row;
								if (!timeRowMap.containsKey(timeSlot)) {
									row = sheet.createRow(currentRow);
									Cell cell = row.createCell(0);
									cell.setCellValue(timeSlot);
									cell.setCellStyle(headerStyle);
									timeRowMap.put(timeSlot, currentRow);
									currentRow++;
								} else {
									row = sheet.getRow(timeRowMap.get(timeSlot));
								}
								Cell cell;
								for (int y = 1; y <= 6; y++) {
									cell = row.createCell(y + (y - 1));
									String value = rs.getString("col" + y);
									if (value != null && value.length() > 1) {
										value = value.substring(0, value.length() - 1);
									}
									cell.setCellValue(value);
									cell.setCellStyle(style);

									cell = row.createCell(2 * y);
									cell.setCellValue("");
									cell.setCellStyle(style);
								}

								if (pageCount == 4) {
									cell = row.createCell(13);
									String value = rs.getString("time_slot");
									if (value != null && value.length() > 1) {
										value = value.substring(0, value.length() - 1);
									}
									cell.setCellValue(value);
									cell.setCellStyle(style);

									cell = row.createCell(14);
									cell.setCellValue("");
									cell.setCellStyle(style);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

					}

					// Fetch and fill leave bus
					try (Connection conn = ConnectionManager.getConnection()) {
						String sql = "SELECT * FROM public.nt_m_control_sheet_leavebus;";
						Row row = sheet.createRow(currentRow);
						Cell cell = row.createCell(0);
						sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, maxWidth));
						cell.setCellValue("LEAVE");
						cell.setCellStyle(headerStyle);

						try (PreparedStatement preparedStatement = conn.prepareStatement(sql);
								ResultSet rs = preparedStatement.executeQuery()) {
							int count = 1;
							while (rs.next()) {
								currentRow++;
								row = sheet.createRow(currentRow);

								cell = row.createCell(0);
								cell.setCellValue(count);
								cell.setCellStyle(headerStyle);
								for (int x = 1; x <= dateColCount; x++) {
									cell = row.createCell(x + (x - 1));
									String value = rs.getString("col" + (x + (pageCount * 6)));
									if (value != null && value.length() > 1) {
										value = value.substring(0, value.length() - 1);
									}
									cell.setCellValue(value);
									cell.setCellStyle(style);

									cell = row.createCell(2 * x);
									cell.setCellValue("");
									cell.setCellStyle(style);
								}
								count++;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					currentRow += 2;
					header = sheet.createRow(currentRow);
					headerCell = header.createCell(0);
					headerCell.setCellValue("Route Distance: " + distance + "km");
					headerCell.setCellStyle(headerStyle);
					sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, (maxWidth / 2)));

					currentRow++;
					header = sheet.createRow(currentRow);
					headerCell = header.createCell(0);
					headerCell.setCellValue("Travel Time: " + travelTime);
					headerCell.setCellStyle(headerStyle);
					sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, (maxWidth / 2)));

					currentRow++;
					header = sheet.createRow(currentRow);
					headerCell = header.createCell(0);
					headerCell.setCellValue("Speed: " + speed);
					headerCell.setCellStyle(headerStyle);
					sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, (maxWidth / 2)));

					currentRow++;
					header = sheet.createRow(currentRow);
					headerCell = header.createCell(((maxWidth / 2) + 1));
					headerCell.setCellValue("Chairman");
					headerCell.setCellStyle(headerStyle);
					sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, ((maxWidth / 2) + 1), maxWidth));

					currentRow++;
					header = sheet.createRow(currentRow);
					headerCell = header.createCell(0);
					headerCell.setCellValue("Executed Date: ..........................................");
					headerCell.setCellStyle(headerStyle);
					sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, (maxWidth / 2)));

					headerCell = header.createCell(((maxWidth / 2) + 1));
					headerCell.setCellValue("National Transport Commission");
					headerCell.setCellStyle(headerStyle);
					sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, ((maxWidth / 2) + 1), maxWidth));

				}

				for (int x = 0; x <= 11; x++) {
					sheet.autoSizeColumn(x);
				}

				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				workbook.write(outputStream);
				InputStream stream = new ByteArrayInputStream(outputStream.toByteArray());
				files = new DefaultStreamedContent(stream, "application/vnd.ms-excel", "Control_Sheet.xlsx");

				ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
				Map<String, Object> sessionMap = externalContext.getSessionMap();
				sessionMap.put("reportBytes", outputStream.toByteArray());
				sessionMap.put("docType", "xlsx");
				isControlSheetGet = true;
				if (isControlSheetGet) {
					reportService.removeTempTable(reportRefNo);
				}
			} catch (IOException | SQLException e) {
				e.printStackTrace();
			}

		} else {
			// PDF Generator
			String loginUser = sessionBackingBean.getLoginUser();

			files = null;
			String sourceFileName = null;

			Connection conn = null;

			sourceFileName = "..//reports//ControlSheet.jrxml";
			String subReportPath = "lk/informatics/ntc/view/reports/";
			try {
				conn = ConnectionManager.getConnection();
				String logopath = "//lk//informatics//ntc//view//reports//";

				// Parameters for report
				Map<String, Object> parameters = new HashMap<String, Object>();

				parameters.put("P_Route_No", route);
				parameters.put("P_Departure", depSide);

				parameters.put("P_Service_Type", serviceTypeDes);
				parameters.put("P_Service_Type_Num", service);
				parameters.put("P_Origin", orgin);
				parameters.put("P_Destination", destination);
				parameters.put("P_Speed", speed);
				parameters.put("P_Distance", distance);
				parameters.put("P_TravelTIme", travelTime);
				parameters.put("P_REf", refNo);
				if (depSide.equalsIgnoreCase(orgin)) {
					parameters.put("P_trip", "O");
				} else if (depSide.equalsIgnoreCase(destination)) {

					parameters.put("P_trip", "D");

				}
				parameters.put("P_Group_no", groupNo);
				parameters.put("P_national_logo", logopath);
				parameters.put("P_ntc_logo", logopath);
				parameters.put("P_Date", printedDate);

				parameters.put("SUBREPORT_PATH", subReportPath);
				parameters.put("Report_Ref", reportRefNo);

				JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

				JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

				byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
				InputStream stream = new ByteArrayInputStream(pdfByteArray);
				files = new DefaultStreamedContent(stream, "Application/pdf", "Control_Sheet.pdf");

				ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
				Map<String, Object> sessionMap = externalContext.getSessionMap();
				sessionMap.put("reportBytes", pdfByteArray);
				sessionMap.put("docType", "pdf");
			} catch (JRException e) {
				e.printStackTrace();
			} finally {
				reportService.removeTempTable(reportRefNo);
				try {
					if (conn != null)
						conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
		return files;
	}

	/* get days of selected month */
	public int getDaysForMonths() throws ParseException {
		int stopRange = Integer.parseInt(mon);
		int count = 0;
		dateList = new ArrayList<>();

		List<String> daysList = timeTableService.getDaysOfGroup(refNo);
		ArrayList<String> days = new ArrayList<>();

		String start = dateRange.trim().substring(0, 10);
		String end = dateRange.trim().substring(dateRange.lastIndexOf("-") + 1);

		Date start_date = new SimpleDateFormat("dd/MM/yyyy").parse(start);
		Date end_date = new SimpleDateFormat("dd/MM/yyyy").parse(end);

		LocalDate startLocalDate = start_date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate endLocalDate = end_date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		int startYear = startLocalDate.getYear();
		int endYear = endLocalDate.getYear();

		int startMonth = startLocalDate.getMonthValue();
		int endMonth = endLocalDate.getMonthValue();

		int startDate = startLocalDate.getDayOfMonth();
		int endDate = endLocalDate.getDayOfMonth();

		if (endYear <= startYear) {
			count = endMonth - startMonth;
		} else {
			count = (12 - startMonth) + endMonth;
		}

		if (stopRange != 1) {
			startDate = 1;
		}

		LocalDate firstDayOfMonth = LocalDate.of(startYear, startMonth + (stopRange - 1), startDate);
		LocalDate lastDayOfMonth = LocalDate.of(endYear, endMonth, endDate);

		if (count != 0) {
			lastDayOfMonth = firstDayOfMonth.with(TemporalAdjusters.lastDayOfMonth());
		} else {
			lastDayOfMonth = LocalDate.of(endYear, endMonth, endDate);
		}

		LocalDate currentDay = firstDayOfMonth;

		while (currentDay.isBefore(lastDayOfMonth) || currentDay.isEqual(lastDayOfMonth)) {
			if (daysList.contains("monday")) {
				LocalDate monday = currentDay.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
				if (!monday.isAfter(lastDayOfMonth)) {
					dateList.add(monday.getDayOfMonth());
					days.add(String.valueOf(monday.getDayOfMonth()));
				}
			}
			if (daysList.contains("tuesday")) {
				LocalDate tuesday = currentDay.with(TemporalAdjusters.nextOrSame(DayOfWeek.TUESDAY));
				if (!tuesday.isAfter(lastDayOfMonth)) {
					dateList.add(tuesday.getDayOfMonth());
					days.add(String.valueOf(tuesday.getDayOfMonth()));
				}
			}
			if (daysList.contains("wednesday")) {
				LocalDate wednesday = currentDay.with(TemporalAdjusters.nextOrSame(DayOfWeek.WEDNESDAY));
				if (!wednesday.isAfter(lastDayOfMonth)) {
					dateList.add(wednesday.getDayOfMonth());
					days.add(String.valueOf(wednesday.getDayOfMonth()));
				}
			}
			if (daysList.contains("thursday")) {
				LocalDate thursday = currentDay.with(TemporalAdjusters.nextOrSame(DayOfWeek.THURSDAY));
				if (!thursday.isAfter(lastDayOfMonth)) {
					dateList.add(thursday.getDayOfMonth());
					days.add(String.valueOf(thursday.getDayOfMonth()));
				}
			}
			if (daysList.contains("friday")) {
				LocalDate friday = currentDay.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
				if (!friday.isAfter(lastDayOfMonth)) {
					dateList.add(friday.getDayOfMonth());
					days.add(String.valueOf(friday.getDayOfMonth()));
				}
			}
			if (daysList.contains("saturday")) {
				LocalDate saturday = currentDay.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
				if (!saturday.isAfter(lastDayOfMonth)) {
					dateList.add(saturday.getDayOfMonth());
					days.add(String.valueOf(saturday.getDayOfMonth()));
				}
			}
			if (daysList.contains("sunday")) {
				LocalDate sunday = currentDay.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
				if (!sunday.isAfter(lastDayOfMonth)) {
					dateList.add(sunday.getDayOfMonth());
					days.add(String.valueOf(sunday.getDayOfMonth()));
				}
			}
			currentDay = currentDay.plusWeeks(1);

		}

		Collections.sort(dateList);
		Collections.sort(days, new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				// Parse strings to integers for comparison
				int num1 = Integer.parseInt(s1);
				int num2 = Integer.parseInt(s2);

				// Sort in ascending order
				return Integer.compare(num1, num2);
			}
		});

		reportService.insertDatesForControlSheet(days,refNo, reportRefNo);
		return days.size();
	}

	/* get no of days for previous month */
	public int getDaysForPreviousMonths() throws ParseException {
		int stopRange = Integer.parseInt(mon);

		List<String> daysList = timeTableService.getDaysOfGroup(refNo);
		ArrayList<String> days = new ArrayList<>();

		String start = dateRange.trim().substring(0, 10);
		String end = dateRange.trim().substring(dateRange.lastIndexOf("-") + 1);

		Date start_date = new SimpleDateFormat("dd/MM/yyyy").parse(start);
		Date end_date = new SimpleDateFormat("dd/MM/yyyy").parse(end);

		LocalDate startLocalDate = start_date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate endLocalDate = end_date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		int startYear = startLocalDate.getYear();
		int endYear = endLocalDate.getYear();

		int startMonth = startLocalDate.getMonthValue();
		int endMonth = endLocalDate.getMonthValue();

		int startDate = startLocalDate.getDayOfMonth();
		int endDate = endLocalDate.getDayOfMonth();

		for (int month = 1; month < stopRange; month++) {
			LocalDate firstDayOfMonth = LocalDate.of(startYear, startMonth, startDate);
			LocalDate lastDayOfMonth = firstDayOfMonth.with(TemporalAdjusters.lastDayOfMonth());
			List<String> daysForMonth = new ArrayList<>();
			LocalDate currentDay = firstDayOfMonth;

			while (currentDay.isBefore(lastDayOfMonth) || currentDay.isEqual(lastDayOfMonth)) {
				if (daysList.contains("monday")) {
					LocalDate monday = currentDay.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
					if (!monday.isAfter(lastDayOfMonth)) {
						daysForMonth.add(String.valueOf(monday.getDayOfMonth()));
					}
				}
				if (daysList.contains("tuesday")) {
					LocalDate tuesday = currentDay.with(TemporalAdjusters.nextOrSame(DayOfWeek.TUESDAY));
					if (!tuesday.isAfter(lastDayOfMonth)) {
						daysForMonth.add(String.valueOf(tuesday.getDayOfMonth()));
					}
				}
				if (daysList.contains("wednesday")) {
					LocalDate wednesday = currentDay.with(TemporalAdjusters.nextOrSame(DayOfWeek.WEDNESDAY));
					if (!wednesday.isAfter(lastDayOfMonth)) {
						daysForMonth.add(String.valueOf(wednesday.getDayOfMonth()));
					}
				}
				if (daysList.contains("thursday")) {
					LocalDate thursday = currentDay.with(TemporalAdjusters.nextOrSame(DayOfWeek.THURSDAY));
					if (!thursday.isAfter(lastDayOfMonth)) {
						daysForMonth.add(String.valueOf(thursday.getDayOfMonth()));
					}
				}
				if (daysList.contains("friday")) {
					LocalDate friday = currentDay.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
					if (!friday.isAfter(lastDayOfMonth)) {
						daysForMonth.add(String.valueOf(friday.getDayOfMonth()));
					}
				}
				if (daysList.contains("saturday")) {
					LocalDate saturday = currentDay.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
					if (!saturday.isAfter(lastDayOfMonth)) {
						daysForMonth.add(String.valueOf(saturday.getDayOfMonth()));
					}
				}
				if (daysList.contains("sunday")) {
					LocalDate sunday = currentDay.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
					if (!sunday.isAfter(lastDayOfMonth)) {
						daysForMonth.add(String.valueOf(sunday.getDayOfMonth()));
					}
				}
				currentDay = currentDay.plusWeeks(1);

			}
			Collections.sort(daysForMonth, new Comparator<String>() {
				@Override
				public int compare(String s1, String s2) {
					// Parse strings to integers for comparison
					int num1 = Integer.parseInt(s1);
					int num2 = Integer.parseInt(s2);

					// Sort in ascending order
					return Integer.compare(num1, num2);
				}
			});
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

		return days.size();
	}

	/**
	 * 
	 * Generate Control Sheet end
	 */

	public void updateTable(Map<String, List> busNoList) {

		reportService.insertTimeSlotsInTemp(timeSlotList, busNoList);

	}

	public StreamedContent downloadPermitOwner(ActionEvent ae) throws JRException {

		String loginUser = sessionBackingBean.getLoginUser();

		files = null;
		String sourceFileName = null;

		Connection conn = null;

		sourceFileName = "..//reports//BusOwnerControlSheet.jrxml";
		String subReportPath = "lk/informatics/ntc/view/reports/";
		try {
			conn = ConnectionManager.getConnection();
			String logopath = "//lk//informatics//ntc//view//reports//";

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_Route_No", route);
			parameters.put("P_Departure", depSide);

			parameters.put("P_Service_Type", serviceTypeDes);
			parameters.put("P_Service_Type_Num", service);
			parameters.put("P_Origin", orgin);
			parameters.put("P_Destination", destination);
			parameters.put("P_Speed", speed);
			parameters.put("P_Distance", distance);
			parameters.put("P_TravelTIme", travelTime);
			parameters.put("P_REf", refNo);
			if (depSide.equalsIgnoreCase(orgin)) {
				parameters.put("P_trip", "O");
			} else if (depSide.equalsIgnoreCase(destination)) {

				parameters.put("P_trip", "D");

			}
			parameters.put("P_Group_no", groupNo);
			parameters.put("P_national_logo", logopath);
			parameters.put("P_ntc_logo", logopath);

			parameters.put("SUBREPORT_PATH", subReportPath);

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", "Control_Sheet_For_Owner.pdf");

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("reportBytes", pdfByteArray);
			sessionMap.put("docType", "pdf");
			isControlSheetGet = true;
			if (isControlSheetGet) {
				reportService.removeTempTable(refNo);
			}
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

	private void createExcelHeader(XSSFWorkbook workbook, Sheet sheet, int row, int width, String data1, String data2)
			throws IOException {
		for (int x = 0; x < 10; x++) {
			sheet.createRow((row + x));
		}
		try (InputStream ntcLogo = TimeTableReportsBackingBean.class
				.getResourceAsStream("/lk/informatics/ntc/view/reports/nationalLogo.png")) {
			sheet.addMergedRegion(new CellRangeAddress(row, (row + 5), 0, 1));
			int pictureIndex = workbook.addPicture(IOUtils.toByteArray(ntcLogo), Workbook.PICTURE_TYPE_PNG);
			Drawing drawing = sheet.createDrawingPatriarch();
			XSSFClientAnchor anchor = new XSSFClientAnchor();
			anchor.setCol1(0);
			anchor.setCol2(2);
			anchor.setRow1(row);
			anchor.setRow2((row + 5));
			drawing.createPicture(anchor, pictureIndex);
		}
		try (InputStream ntcLogo = TimeTableReportsBackingBean.class
				.getResourceAsStream("/lk/informatics/ntc/view/reports/ntclogo.jpg")) {
			sheet.addMergedRegion(new CellRangeAddress(row, (row + 5), width - 1, width));
			int pictureIndex = workbook.addPicture(IOUtils.toByteArray(ntcLogo), Workbook.PICTURE_TYPE_JPEG);
			Drawing drawing = sheet.createDrawingPatriarch();
			XSSFClientAnchor anchor = new XSSFClientAnchor();
			anchor.setCol1(width - 2);
			anchor.setCol2(width);
			anchor.setRow1(row);
			anchor.setRow2((row + 5));
			drawing.createPicture(anchor, pictureIndex);
		}
	}

	

	public void clearControlSheet() {
		route = null;
		orgin = null;
		destination = null;
		serviceType = null;
		depSide = null;
		refNo = null;
		groupNo = null;
		serviceTypeDes = null;
		mon = null;
		startMonth = null;
		lastMonth = null;
		dateRange = null;
		rptType = null;
		mon = null;
		stelectMonthList = new ArrayList<>();
		rotationList = null;
		generatedColumns.clear();
		renderLeave = false;
	}

	// for new owner controll sheet
	public StreamedContent printOwnerControlSheetNew(ActionEvent ae) throws JRException, ParseException {

		ArrayList<String> thirdMonthArray = new ArrayList<>();
		ArrayList<String> secondMonthArray = new ArrayList<>();
		ArrayList<String> firstMonthArray = new ArrayList<>();
		ArrayList<String> monthArray = new ArrayList<>();

		String service = null;
		boolean middleMonthHave = false;
		boolean onlyOneMonth = false;
		int emptyIndexes = 0;
		boolean busRotationInsertSuccess = false;
		boolean datesInsertsSuccess = false;

		reportRefNo = generateRandomString();
		
		if (rptType != null && !rptType.trim().isEmpty()) {
			if (refNo != null && groupNo != null && route != null && serviceTypeDes != null && !refNo.trim().isEmpty()
					&& !groupNo.trim().isEmpty() && !route.trim().isEmpty() && !serviceTypeDes.trim().isEmpty()) {

				if (depSide.equals(orgin)) {
					tripType = "O";
				} else if (depSide.equals(destination)) {
					tripType = "D";
				}

				if (serviceTypeDes.equalsIgnoreCase("NORMAL")) {

					service = "001";
				}
				if (serviceTypeDes.equalsIgnoreCase("LUXURY")) {

					service = "002";
				}
				if (serviceTypeDes.equalsIgnoreCase("SUPER LUXURY")) {

					service = "003";
				}
				if (serviceTypeDes.equalsIgnoreCase("SEMI-LUXURY")) {

					service = "004";
				}
				if (serviceTypeDes.equalsIgnoreCase("EXPRESSWAY BUS")) {

					service = "EB";
				}

				startMonth = dateRange.trim().substring(0, 10);
				lastMonth = dateRange.trim().substring(dateRange.lastIndexOf("-") + 1);

				Date startMonthDateOwner = new SimpleDateFormat("dd/MM/yyyy").parse(startMonth);
				Calendar startMonthCalOwner = Calendar.getInstance();
				startMonthCalOwner.setTime(startMonthDateOwner);

				Date lastMonthDateOwner = new SimpleDateFormat("dd/MM/yyyy").parse(lastMonth);
				Calendar lastMonthCalOwner = Calendar.getInstance();
				lastMonthCalOwner.setTime(lastMonthDateOwner);

				Date lastMonthDateOwnerTemp = new SimpleDateFormat("dd/MM/yyyy").parse(lastMonth);
				Calendar lastMonthCalOwnerTemp = Calendar.getInstance();
				lastMonthCalOwnerTemp.setTime(lastMonthDateOwnerTemp);

				Date midleMonthOwner;

				Calendar c = new GregorianCalendar();
				c.setTime(new Date());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

				lastMonthCalOwnerTemp.add(lastMonthCalOwnerTemp.MONTH, -1);
				midleMonthOwner = lastMonthCalOwnerTemp.getTime();
				Calendar midleMonthCalOwner = Calendar.getInstance();
				midleMonthCalOwner.setTime(midleMonthOwner);

				int monthsDiff = lastMonthCalOwner.get(Calendar.MONTH) - startMonthCalOwner.get(Calendar.MONTH);
				if (monthsDiff == 2) {
					middleMonthHave = true;
				}
				// check for only one month timetable
				if (startMonthCalOwner.getDisplayName(startMonthCalOwner.MONTH, Calendar.LONG, Locale.UK)
						.equals(lastMonthCalOwner.getDisplayName(lastMonthCalOwner.MONTH, Calendar.LONG, Locale.UK))) {
					onlyOneMonth = true;
				}
				int firstMonthDates = startMonthCalOwner.getActualMaximum(startMonthCalOwner.DAY_OF_MONTH);// july 31
				int middleMonthDate = midleMonthCalOwner.getActualMaximum(midleMonthCalOwner.DAY_OF_MONTH); // aug 31
				int lastMonthDates = lastMonthCalOwner.getActualMaximum(lastMonthCalOwner.DAY_OF_MONTH); // sep 30

				String fMonth = startMonthCalOwner.getDisplayName(startMonthCalOwner.MONTH, Calendar.LONG, Locale.UK);
				String sMonth = midleMonthCalOwner.getDisplayName(midleMonthCalOwner.MONTH, Calendar.LONG, Locale.UK);
				String lMonth = lastMonthCalOwner.getDisplayName(lastMonthCalOwner.MONTH, Calendar.LONG, Locale.UK);

				if (lastMonthDates > 0) {
					for (int i = 1; i <= lastMonthDates; i++) {

						firstMonthArray.add(Integer.toString(i));
					}
				}

				if (middleMonthDate > 0) {
					for (int i = 1; i <= middleMonthDate; i++) {
						secondMonthArray.add(Integer.toString(i));
					}

				}

				if (firstMonthDates > 0) {
					for (int i = 1; i <= firstMonthDates; i++) {
						thirdMonthArray.add(Integer.toString(i));
					}

				}
				int totalBusCount = 0;

				TimeTableDTO routeDTO = timeTableService.getRouteDataForEditPanelGenerator(refNo);

				int numberOfLeaves = tripType.equalsIgnoreCase("O") ? routeDTO.getNoOfPvtLeaveBusesOrigin()
						: routeDTO.getNoOfPvtLeaveBusesDestination();

				int numberOfBusesFromOrigin = routeDTO.getNoOfTripsOrigin();
				int numberOfBusesFromDestination = routeDTO.getNoOfTripsDestination();

				// + added by danilka.j
				// System.out.println("tripType : " + tripType);
				int numberOfBuses = tripType.equalsIgnoreCase("O") ? routeDTO.getNoOfPvtBusesOrigin()
						: routeDTO.getNoOfPvtBusesDestination();
				// System.out.println("numberOfBuses : "+numberOfBuses);
				//

				if (numberOfBusesFromOrigin + numberOfBusesFromDestination > 31) {
					isMoreThanThirty = true;
					/** create rotation for total buses are more than 31 ***/
					totalBusCount = tripType.equalsIgnoreCase("O") ? routeDTO.getNoOfTripsOrigin()
							: routeDTO.getNoOfTripsDestination();

					
					datesInsertsSuccess = createDatesRotationForbusesMoreThanThirtyChange(firstMonthDates,
							middleMonthDate, lastMonthDates, fMonth, sMonth, lMonth, middleMonthHave, onlyOneMonth,
							tripType, totalBusCount, isMoreThanThirty);

					if (datesInsertsSuccess) {

						try {
							insertRidingDaysForOwnerSheet(reportService.getMonthListWithRidingDates(), numberOfBuses);

							createBusesRotation(service, true);
							List<String> datesColumn = new ArrayList<>();
							datesColumn = reportService.getDates(totalBusCount);

							printOwnerNewReportNew(ae, numberOfLeaves, false);
						} catch (Exception e) {
							e.printStackTrace();
						}finally {
							truncTempTable();
						}
						
					}
				} else {
					int totalBusCount1 = 0;

					TimeTableDTO routeDTO1 = timeTableService.getRouteDataForEditPanelGenerator(refNo);

					int numberOfLeaves1 = tripType.equalsIgnoreCase("O") ? routeDTO.getNoOfPvtLeaveBusesOrigin()
							: routeDTO.getNoOfPvtLeaveBusesDestination();

					int numberOfBusesFromOrigin1 = routeDTO.getNoOfTripsOrigin();
					int numberOfBusesFromDestination1 = routeDTO.getNoOfTripsDestination();

					/** create rotation for total buses are more than 31 ***/
					totalBusCount1 = tripType.equalsIgnoreCase("O") ? routeDTO.getNoOfTripsOrigin()
							: routeDTO.getNoOfTripsDestination();

					datesInsertsSuccess = createDatesRotationForbusesMoreThanThirtyChange(firstMonthDates,
							middleMonthDate, lastMonthDates, fMonth, sMonth, lMonth, middleMonthHave, onlyOneMonth,
							tripType, totalBusCount, isMoreThanThirty);

					if (datesInsertsSuccess) {					
						try {
							insertRidingDaysForOwnerSheet(reportService.getMonthListWithRidingDates(), numberOfBuses);
							createBusesRotation(service, false);
							printOwnerNewReportNew(ae, numberOfLeaves, false);
						} catch (Exception e) {
							e.printStackTrace();
						}finally {
							truncTempTable();
						}						
					}
				}
			} else {

				setErrorMsg("Please select  mandotory fields.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

			}
		} else {

			setErrorMsg("Please select  a report type.");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		}

		return files;

	}

	public StreamedContent printOwnerControlSheetNewTwoDay(ActionEvent ae) throws JRException, ParseException {

		ArrayList<String> thirdMonthArray = new ArrayList<>();
		ArrayList<String> secondMonthArray = new ArrayList<>();
		ArrayList<String> firstMonthArray = new ArrayList<>();
		ArrayList<String> monthArray = new ArrayList<>();

		String service = null;
		boolean middleMonthHave = false;
		boolean onlyOneMonth = false;
		int emptyIndexes = 0;
		boolean busRotationInsertSuccess = false;
		boolean datesInsertsSuccess = false;

		reportRefNo = generateRandomString();
		
		if (rptType != null && !rptType.trim().isEmpty()) {
			if (refNo != null && groupNo != null && route != null && serviceTypeDes != null && !refNo.trim().isEmpty()
					&& !groupNo.trim().isEmpty() && !route.trim().isEmpty() && !serviceTypeDes.trim().isEmpty()) {

				if (depSide.equals(orgin)) {
					tripType = "O";
				} else if (depSide.equals(destination)) {
					tripType = "D";
				}

				if (serviceTypeDes.equalsIgnoreCase("NORMAL")) {

					service = "001";
				}
				if (serviceTypeDes.equalsIgnoreCase("LUXURY")) {

					service = "002";
				}
				if (serviceTypeDes.equalsIgnoreCase("SUPER LUXURY")) {

					service = "003";
				}
				if (serviceTypeDes.equalsIgnoreCase("SEMI-LUXURY")) {

					service = "004";
				}
				if (serviceTypeDes.equalsIgnoreCase("EXPRESSWAY BUS")) {

					service = "EB";
				}

				startMonth = dateRange.trim().substring(0, 10);
				lastMonth = dateRange.trim().substring(dateRange.lastIndexOf("-") + 1);

				Date startMonthDateOwner = new SimpleDateFormat("dd/MM/yyyy").parse(startMonth);
				Calendar startMonthCalOwner = Calendar.getInstance();
				startMonthCalOwner.setTime(startMonthDateOwner);

				Date lastMonthDateOwner = new SimpleDateFormat("dd/MM/yyyy").parse(lastMonth);
				Calendar lastMonthCalOwner = Calendar.getInstance();
				lastMonthCalOwner.setTime(lastMonthDateOwner);

				Date lastMonthDateOwnerTemp = new SimpleDateFormat("dd/MM/yyyy").parse(lastMonth);
				Calendar lastMonthCalOwnerTemp = Calendar.getInstance();
				lastMonthCalOwnerTemp.setTime(lastMonthDateOwnerTemp);

				Date midleMonthOwner;

				Calendar c = new GregorianCalendar();
				c.setTime(new Date());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

				lastMonthCalOwnerTemp.add(lastMonthCalOwnerTemp.MONTH, -1);
				midleMonthOwner = lastMonthCalOwnerTemp.getTime();
				Calendar midleMonthCalOwner = Calendar.getInstance();
				midleMonthCalOwner.setTime(midleMonthOwner);

				int monthsDiff = lastMonthCalOwner.get(Calendar.MONTH) - startMonthCalOwner.get(Calendar.MONTH);
				if (monthsDiff == 2) {
					middleMonthHave = true;
				}
				// check for only one month timetable
				if (startMonthCalOwner.getDisplayName(startMonthCalOwner.MONTH, Calendar.LONG, Locale.UK)
						.equals(lastMonthCalOwner.getDisplayName(lastMonthCalOwner.MONTH, Calendar.LONG, Locale.UK))) {
					onlyOneMonth = true;
				}
				int firstMonthDates = startMonthCalOwner.getActualMaximum(startMonthCalOwner.DAY_OF_MONTH);// july 31
				int middleMonthDate = midleMonthCalOwner.getActualMaximum(midleMonthCalOwner.DAY_OF_MONTH); // aug 31
				int lastMonthDates = lastMonthCalOwner.getActualMaximum(lastMonthCalOwner.DAY_OF_MONTH); // sep 30

				String fMonth = startMonthCalOwner.getDisplayName(startMonthCalOwner.MONTH, Calendar.LONG, Locale.UK);
				String sMonth = midleMonthCalOwner.getDisplayName(midleMonthCalOwner.MONTH, Calendar.LONG, Locale.UK);
				String lMonth = lastMonthCalOwner.getDisplayName(lastMonthCalOwner.MONTH, Calendar.LONG, Locale.UK);

				if (lastMonthDates > 0) {
					for (int i = 1; i <= lastMonthDates; i++) {

						firstMonthArray.add(Integer.toString(i));
					}
				}

				if (middleMonthDate > 0) {
					for (int i = 1; i <= middleMonthDate; i++) {
						secondMonthArray.add(Integer.toString(i));
					}

				}

				if (firstMonthDates > 0) {
					for (int i = 1; i <= firstMonthDates; i++) {
						thirdMonthArray.add(Integer.toString(i));
					}

				}
				int totalBusCount = 0;

				TimeTableDTO routeDTO = timeTableService.getRouteDataForEditPanelGenerator(refNo);

				int numberOfLeaves = tripType.equalsIgnoreCase("O") ? routeDTO.getNoOfPvtLeaveBusesOrigin()
						: routeDTO.getNoOfPvtLeaveBusesDestination();

				int numberOfBusesFromOrigin = routeDTO.getNoOfTripsOrigin();
				int numberOfBusesFromDestination = routeDTO.getNoOfTripsDestination();

				// + added by danilka.j
				int numberOfBuses = tripType.equalsIgnoreCase("O") ? routeDTO.getNoOfPvtBusesOrigin()
						: routeDTO.getNoOfPvtBusesDestination();
				// -

				if (numberOfBusesFromOrigin + numberOfBusesFromDestination > 31) {
					isMoreThanThirty = true;
					/** create rotation for total buses are more than 31 ***/
					totalBusCount = tripType.equalsIgnoreCase("O") ? routeDTO.getNoOfTripsOrigin()
							: routeDTO.getNoOfTripsDestination();


					datesInsertsSuccess = createDatesRotationForbusesMoreThanThirtyChange(firstMonthDates,
							middleMonthDate, lastMonthDates, fMonth, sMonth, lMonth, middleMonthHave, onlyOneMonth,
							tripType, totalBusCount, isMoreThanThirty);

					if (datesInsertsSuccess) {
						try {
							insertRidingDaysForOwnerSheet(reportService.getMonthListWithRidingDates(), numberOfBuses);							createBusesRotationTwoDay(service, true);
							List<String> datesColumn = new ArrayList<>();
							datesColumn = reportService.getDates(totalBusCount);
							printOwnerNewReportNew(ae, numberOfLeaves, true);
						} catch (Exception e) {
							e.printStackTrace();							
						}finally {
							truncTempTable();
						}
					}
				} else {
					int totalBusCount1 = 0;

					TimeTableDTO routeDTO1 = timeTableService.getRouteDataForEditPanelGenerator(refNo);

					int numberOfLeaves1 = tripType.equalsIgnoreCase("O") ? routeDTO.getNoOfPvtLeaveBusesOrigin()
							: routeDTO.getNoOfPvtLeaveBusesDestination();

					int numberOfBusesFromOrigin1 = routeDTO.getNoOfTripsOrigin();
					int numberOfBusesFromDestination1 = routeDTO.getNoOfTripsDestination();

					/** create rotation for total buses are more than 31 ***/
					totalBusCount1 = tripType.equalsIgnoreCase("O") ? routeDTO.getNoOfTripsOrigin()
							: routeDTO.getNoOfTripsDestination();

					datesInsertsSuccess = createDatesRotationForbusesMoreThanThirtyChange(firstMonthDates,
							middleMonthDate, lastMonthDates, fMonth, sMonth, lMonth, middleMonthHave, onlyOneMonth,
							tripType, totalBusCount, isMoreThanThirty);

					if (datesInsertsSuccess) {

						try {
							insertRidingDaysForOwnerSheet(reportService.getMonthListWithRidingDates(), numberOfBuses);
							createBusesRotationTwoDay(service, false);
							printOwnerNewReportNew(ae, numberOfLeaves, true);
						} catch (Exception e) {
							e.printStackTrace();							
						}finally {
							truncTempTable();
						}
	
					}
				}
			} else {

				setErrorMsg("Please select  mandotory fields.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

			}
		} else {

			setErrorMsg("Please select  a report type.");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		}

		return files;

	}

	public boolean createDatesRotationForbusesMoreThanThirty(int firstMonthDates, int middleMonthDate,
			int lastMonthDates, String month1, String month2, String month3, boolean midleMonthHave,
			boolean onlyForOneMonth, String tripType, int totalBusCount, boolean isMoreThanThirty2) {

		ArrayList<String> arrayOne = new ArrayList<String>();
		ArrayList<String> arrayTwo = new ArrayList<String>();
		ArrayList<String> arrayThree = new ArrayList<String>();

		monthCount = 0;

		if (onlyForOneMonth) {
			monthCount = 1;
			List<String> dateList1 = new ArrayList<>();
			for (int i = 1; i <= totalBusCount; i++) {

				while (i <= firstMonthDates) {

					dateList1.add(Integer.toString(i));
					i++;
				}
				dateList1.add("null");

			}
			arrayOne.addAll(dateList1);
			System.out.println(arrayOne);

		}
		// for 2 months
		else if (midleMonthHave == false && onlyForOneMonth == false) {
			List<String> dateList1 = new ArrayList<>();
			for (int i = 1; i <= totalBusCount; i++) {

				while (i <= firstMonthDates) {

					dateList1.add(Integer.toString(i));
					i++;
				}
				dateList1.add("null");

			}
			arrayOne.addAll(dateList1);

			int firstStartEndAt = totalBusCount - firstMonthDates;
			int restDatesIndexesCount = lastMonthDates - firstStartEndAt;
			int spacesIndexcesCount = totalBusCount - lastMonthDates;

			List<String> firstStartList = new ArrayList<>();
			List<String> restDatesList = new ArrayList<>();
			List<String> spacesColumnListString = new ArrayList<>();
			List<String> spacesColumnList = new ArrayList<>();
			List<String> dateList2 = new ArrayList<>();

			for (int i = firstStartEndAt + 1; i <= lastMonthDates; i++) {
				restDatesList.add(Integer.toString(i));

			}
			for (int j = 1; j < spacesIndexcesCount; j++) {
				spacesColumnList.add("null");

			}

			for (int k = 1; k <= firstStartEndAt; k++) {
				firstStartList.add(Integer.toString(k));

			}
			dateList2.addAll(restDatesList);
			dateList2.addAll(spacesColumnList);
			dateList2.addAll(firstStartList);
			arrayTwo.addAll(dateList2);
			System.out.println(arrayTwo);
			System.out.println(arrayOne);
		} else {
			List<String> dateList1 = new ArrayList<>();
			List<String> dateList2 = new ArrayList<>();
			List<String> dateList3 = new ArrayList<>();
			for (int i = 1; i <= totalBusCount; i++) {

				while (i <= firstMonthDates) {

					dateList1.add(Integer.toString(i));
					i++;
				}
				dateList1.add("null");

			}
			arrayOne.addAll(dateList1);

			int firstStartEndAt = totalBusCount - firstMonthDates;
			int restDatesIndexesCount = lastMonthDates - firstStartEndAt;
			int spacesIndexcesCount = totalBusCount - middleMonthDate;

			List<String> firstStartList = new ArrayList<>();
			List<String> restDatesList = new ArrayList<>();
			List<String> spacesColumnList = new ArrayList<>();

			for (int i = firstStartEndAt + 1; i <= middleMonthDate; i++) {
				restDatesList.add(Integer.toString(i));

			}
			for (int j = 1; j <= spacesIndexcesCount; j++) {
				spacesColumnList.add("null");

			}

			for (int k = 1; k <= firstStartEndAt; k++) {
				firstStartList.add(Integer.toString(k));

			}
			dateList2.addAll(restDatesList);
			dateList2.addAll(spacesColumnList);
			dateList2.addAll(firstStartList);
			arrayTwo.addAll(dateList2);

			// for third month
			firstStartList = new ArrayList<>();
			restDatesList = new ArrayList<>();
			spacesColumnList = new ArrayList<>();

			int restDatesIndexesCountForThird = middleMonthDate - firstStartEndAt;
			int emptyStratAt = totalBusCount - (restDatesIndexesCountForThird + lastMonthDates);
			for (int i = 1; i <= restDatesIndexesCountForThird; i++) {
				spacesColumnList.add("null");

			}

			for (int j = 1; j <= lastMonthDates; j++) {
				firstStartList.add(Integer.toString(j));
			}

			for (int k = 1; k <= emptyStratAt; k++) {
				restDatesList.add("null");
			}

			dateList3.addAll(spacesColumnList);
			dateList3.addAll(firstStartList);
			dateList3.addAll(restDatesList);
			arrayThree.addAll(dateList3);
			System.out.println(arrayThree);
			System.out.println(arrayTwo);
			System.out.println(arrayOne);
		}
		if (isMoreThanThirty) {
//			boolean dateRotationInsert = reportService.insertDatesRotationsForBusesMoreThanThirty(arrayOne, arrayTwo,
//					arrayThree, month1, month2, month3, totalBusCount);

			boolean dateRotationInsert = reportService.insertDatesRotationsForBusesLessThanThirty(arrayOne, arrayTwo,
					arrayThree, month1, month2, month3, totalBusCount, refNo, null);

		} else {
			boolean dateRotationInsert = reportService.insertDatesRotationsForBusesLessThanThirty(arrayOne, arrayTwo,
					arrayThree, month1, month2, month3, totalBusCount, refNo, null);
		}

		return true;
	}

// dhananjika.d new date insert
	public boolean createDatesRotationForbusesMoreThanThirtyChange(int firstMonthDates, int middleMonthDate,
			int lastMonthDates, String month1, String month2, String month3, boolean midleMonthHave,
			boolean onlyForOneMonth, String tripType, int totalBusCount, boolean isMoreThanThirty2)
			throws ParseException {

		ArrayList<String> arrayOne = new ArrayList<String>();
		ArrayList<String> arrayTwo = new ArrayList<String>();
		ArrayList<String> arrayThree = new ArrayList<String>();

		LinkedHashMap<String, List<String>> fullMonths = new LinkedHashMap<>();

		monthCount = 0;

		List<String> daysList = timeTableService.getDaysOfGroup(refNo);
		ArrayList<String> days = new ArrayList<>();

		String start = dateRange.trim().substring(0, 10);
		String end = dateRange.trim().substring(dateRange.lastIndexOf("-") + 1);

		Date start_date = new SimpleDateFormat("dd/MM/yyyy").parse(start);
		Date end_date = new SimpleDateFormat("dd/MM/yyyy").parse(end);

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

			List<String> daysForMonth = new ArrayList<>();
			LocalDate currentDay = firstDayOfMonth;

			while (currentDay.isBefore(lastDayOfMonth) || currentDay.isEqual(lastDayOfMonth)) {
				if (daysList.contains("monday")) {
					LocalDate monday = currentDay.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
					if (!monday.isAfter(lastDayOfMonth)) {
						daysForMonth.add(String.valueOf(monday.getDayOfMonth()));
					}
				}
				if (daysList.contains("tuesday")) {
					LocalDate tuesday = currentDay.with(TemporalAdjusters.nextOrSame(DayOfWeek.TUESDAY));
					if (!tuesday.isAfter(lastDayOfMonth)) {
						daysForMonth.add(String.valueOf(tuesday.getDayOfMonth()));
					}
				}
				if (daysList.contains("wednesday")) {
					LocalDate wednesday = currentDay.with(TemporalAdjusters.nextOrSame(DayOfWeek.WEDNESDAY));
					if (!wednesday.isAfter(lastDayOfMonth)) {
						daysForMonth.add(String.valueOf(wednesday.getDayOfMonth()));
					}
				}
				if (daysList.contains("thursday")) {
					LocalDate thursday = currentDay.with(TemporalAdjusters.nextOrSame(DayOfWeek.THURSDAY));
					if (!thursday.isAfter(lastDayOfMonth)) {
						daysForMonth.add(String.valueOf(thursday.getDayOfMonth()));
					}
				}
				if (daysList.contains("friday")) {
					LocalDate friday = currentDay.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
					if (!friday.isAfter(lastDayOfMonth)) {
						daysForMonth.add(String.valueOf(friday.getDayOfMonth()));
					}
				}
				if (daysList.contains("saturday")) {
					LocalDate saturday = currentDay.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
					if (!saturday.isAfter(lastDayOfMonth)) {
						daysForMonth.add(String.valueOf(saturday.getDayOfMonth()));
					}
				}
				if (daysList.contains("sunday")) {
					LocalDate sunday = currentDay.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
					if (!sunday.isAfter(lastDayOfMonth)) {
						daysForMonth.add(String.valueOf(sunday.getDayOfMonth()));
					}
				}
				currentDay = currentDay.plusWeeks(1);

			}
			Collections.sort(daysForMonth, new Comparator<String>() {
				@Override
				public int compare(String s1, String s2) {
					// Parse strings to integers for comparison
					int num1 = Integer.parseInt(s1);
					int num2 = Integer.parseInt(s2);

					// Sort in ascending order
					return Integer.compare(num1, num2);
				}
			});
			String monthString = Month.of(startMonth).name().toLowerCase(); // Get month-string value
			monthString = monthString.substring(0, 1).toUpperCase() + monthString.substring(1);
			fullMonths.put(monthString, daysForMonth);
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

		if (isMoreThanThirty) {
//			boolean dateRotationInsert = reportService.insertDatesRotationsForBusesMoreThanThirty(arrayOne, arrayTwo,
//					arrayThree, month1, month2, month3, totalBusCount);

			boolean dateRotationInsert = reportService.insertDatesRotationsForBusesLessThanThirty(days, arrayTwo,
					arrayThree, month1, month2, month3, totalBusCount, reportRefNo, fullMonths);

		} else {
			boolean dateRotationInsert = reportService.insertDatesRotationsForBusesLessThanThirty(days, arrayTwo,
					arrayThree, month1, month2, month3, totalBusCount, reportRefNo, fullMonths);
		}

		monthDays = days.size();
		return true;
	}

	public boolean createDatesRotationNew(int firstMonthDates, int middleMonthDate, int lastMonthDates, String month1,
			String month2, String month3, boolean midleMonthHave, boolean onlyForOneMonth, String tripType,
			int totalBusCount) {
		ArrayList<ArrayList<Integer>> arrayOne = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> arrayTwo = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> arrayThree = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> arrayOneNew = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> arrayTwoNew = new ArrayList<ArrayList<Integer>>();

		TimeTableDTO routeDTO = timeTableService.getRouteDataForEditPanelGenerator(refNo);

		int numberOfBuses = tripType.equalsIgnoreCase("O") ? routeDTO.getNoOfPvtBusesOrigin()
				: routeDTO.getNoOfPvtBusesDestination();

		monthCount = 0;

		if (onlyForOneMonth) {
			monthCount = 1;
			for (int i = 1; i <= numberOfBuses; i++) {
				int x = i;
				List<Integer> dateList1 = new ArrayList<>();
				while (x <= firstMonthDates) {

					dateList1.add(x);
					x = x + numberOfBuses;
				}
				arrayOne.add((ArrayList<Integer>) dateList1);

			}

		}
		// Create array for two month
		else if (midleMonthHave == false && onlyForOneMonth == false) {

			month2 = month3;
			monthCount = 2;
			int emptySlotCountStratAtFirstMonth = 0;

			int listSplitIndex = 0;

			if (firstMonthDates % numberOfBuses > 0) {

				emptySlotCountStratAtFirstMonth = firstMonthDates % numberOfBuses;
				int j = emptySlotCountStratAtFirstMonth + 1;
				listSplitIndex = numberOfBuses - emptySlotCountStratAtFirstMonth;

				for (int i = 1; i <= numberOfBuses; i++) {
					int x = i;
					List<Integer> dateList1 = new ArrayList<>();
					while (x <= firstMonthDates) {

						dateList1.add(x);
						x = x + numberOfBuses;
					}
					arrayOne.add((ArrayList<Integer>) dateList1);

				}

				for (int i = 1; i <= numberOfBuses; i++) {
					int x = i;

					List<Integer> dateList2 = new ArrayList<>();
					while (x <= lastMonthDates) {

						dateList2.add(x);

						x = x + numberOfBuses;

					}

					arrayTwo.add((ArrayList<Integer>) dateList2);
				}

				// rearrange list
				ArrayList<ArrayList<Integer>> arrayTwoPartOne = new ArrayList<ArrayList<Integer>>();
				ArrayList<ArrayList<Integer>> arrayTwoPartTwo = new ArrayList<ArrayList<Integer>>();
				;
				for (int i = 0; i < listSplitIndex; i++) {
					arrayTwoPartTwo.add(arrayTwo.get(i));

				}

				for (int i = listSplitIndex; i < numberOfBuses; i++) {

					arrayTwoPartOne.add(arrayTwo.get(i));
				}

				arrayTwo = new ArrayList<ArrayList<Integer>>();
				arrayTwo.addAll(arrayTwoPartOne);
				arrayTwo.addAll(arrayTwoPartTwo);

			} else {

				for (int i = 1; i <= numberOfBuses; i++) {
					int x = i;
					List<Integer> dateList1 = new ArrayList<>();
					while (x <= firstMonthDates) {

						dateList1.add(x);
						x = x + numberOfBuses;
					}
					arrayOne.add((ArrayList<Integer>) dateList1);

				}

				for (int k3 = 1; k3 <= numberOfBuses; k3++) {
					int x = k3;
					List<Integer> dateList2 = new ArrayList<>();
					while (x <= lastMonthDates) {

						dateList2.add(x);
						x = x + numberOfBuses;
					}
					arrayTwo.add((ArrayList<Integer>) dateList2);

				}

			}

		}
		// Create array for three month
		else {
			monthCount = 3;
			int emptySlotCountStratAtFirstMonth = 0;
			int emptySlotCountStratAtMiddleMonth = 0;
			int listSplitIndex = 0;
			int listSlitIndexForThirdMonth = 0;
			emptySlotCountStratAtFirstMonth = firstMonthDates % numberOfBuses;
			emptySlotCountStratAtMiddleMonth = middleMonthDate % numberOfBuses;
			listSplitIndex = numberOfBuses - emptySlotCountStratAtFirstMonth;
			listSlitIndexForThirdMonth = numberOfBuses
					- (emptySlotCountStratAtMiddleMonth + emptySlotCountStratAtFirstMonth);

			if (firstMonthDates % numberOfBuses > 0) {

				for (int i = 1; i <= numberOfBuses; i++) {
					int x = i;
					List<Integer> dateList1 = new ArrayList<>();
					while (x <= firstMonthDates) {

						dateList1.add(x);
						x = x + numberOfBuses;
					}
					arrayOne.add((ArrayList<Integer>) dateList1);

				}

				for (int i = 1; i <= numberOfBuses; i++) {
					int x = i;

					List<Integer> dateList2 = new ArrayList<>();
					while (x <= middleMonthDate) {

						dateList2.add(x);

						x = x + numberOfBuses;

					}

					arrayTwo.add((ArrayList<Integer>) dateList2);
				}

				// rearrange list
				ArrayList<ArrayList<Integer>> arrayTwoPartOne = new ArrayList<ArrayList<Integer>>();
				ArrayList<ArrayList<Integer>> arrayTwoPartTwo = new ArrayList<ArrayList<Integer>>();

				for (int i = 0; i < listSplitIndex; i++) {
					arrayTwoPartTwo.add(arrayTwo.get(i));

				}

				for (int i = listSplitIndex; i < numberOfBuses; i++) {

					arrayTwoPartOne.add(arrayTwo.get(i));
				}

				arrayTwo = new ArrayList<ArrayList<Integer>>();
				arrayTwo.addAll(arrayTwoPartOne);
				arrayTwo.addAll(arrayTwoPartTwo);

			} else {

				for (int i = 1; i <= numberOfBuses; i++) {
					int x = i;
					List<Integer> dateList1 = new ArrayList<>();
					while (x <= firstMonthDates) {

						dateList1.add(x);
						x = x + numberOfBuses;
					}
					arrayOne.add((ArrayList<Integer>) dateList1);

				}

				for (int k3 = 1; k3 <= numberOfBuses; k3++) {
					int x = k3;
					List<Integer> dateList2 = new ArrayList<>();
					while (x <= middleMonthDate) {

						dateList2.add(x);
						x = x + numberOfBuses;
					}
					arrayTwo.add((ArrayList<Integer>) dateList2);

				}

			}

			/** create based on 2nd month for last month **/
			if ((emptySlotCountStratAtFirstMonth + emptySlotCountStratAtMiddleMonth) > 0) {

				for (int i = 1; i <= numberOfBuses; i++) {
					int x = i;

					List<Integer> dateList3 = new ArrayList<>();
					while (x <= lastMonthDates) {

						dateList3.add(x);

						x = x + numberOfBuses;

					}

					arrayThree.add((ArrayList<Integer>) dateList3);
				}

				// rearrange list
				ArrayList<ArrayList<Integer>> arrayThreePartOne = new ArrayList<ArrayList<Integer>>();
				ArrayList<ArrayList<Integer>> arrayThreePartTwo = new ArrayList<ArrayList<Integer>>();
				if (listSlitIndexForThirdMonth < 0) {
					listSlitIndexForThirdMonth = listSlitIndexForThirdMonth * -1;

					for (int i = 0; i < (numberOfBuses - listSlitIndexForThirdMonth); i++) {
						arrayThreePartTwo.add(arrayThree.get(i));

					}

					for (int i = (numberOfBuses - listSlitIndexForThirdMonth); i < numberOfBuses; i++) {

						arrayThreePartOne.add(arrayThree.get(i));
					}

					arrayThree = new ArrayList<ArrayList<Integer>>();
					arrayThree.addAll(arrayThreePartOne);
					arrayThree.addAll(arrayThreePartTwo);

				} else {

					for (int i = 0; i < listSlitIndexForThirdMonth; i++) {
						arrayThreePartTwo.add(arrayThree.get(i));

					}

					for (int i = listSlitIndexForThirdMonth; i < numberOfBuses; i++) {

						arrayThreePartOne.add(arrayThree.get(i));
					}

					arrayThree = new ArrayList<ArrayList<Integer>>();
					arrayThree.addAll(arrayThreePartOne);
					arrayThree.addAll(arrayThreePartTwo);

				}

			} else {

				for (int k3 = 1; k3 <= numberOfBuses; k3++) {
					int x = k3;
					List<Integer> dateList3 = new ArrayList<>();
					while (x <= lastMonthDates) {

						dateList3.add(x);
						x = x + numberOfBuses;
					}
					arrayThree.add((ArrayList<Integer>) dateList3);

				}

			}

			/** end create based on 2nd month **/

		}
//
//		boolean dateRotationInsert = reportService.insertDatesRotationsNew(arrayOne, arrayTwo, arrayThree, month1,
//				month2, month3);

		boolean dateRotationInsert = reportService.insertDatesRotationsForBusesMoreThanThirtyNormal(arrayOne, arrayTwo,
				arrayThree, month1, month2, month3, totalBusCount);

		return true;
	}

	public void truncTempTable() {
		reportService.truncTableInOwnersheetGenerate(reportRefNo);
	}

	public String getReportName(int numberOfBuses) {
		String sourceFileName = null;
		if (numberOfBuses == 31) {
			sourceFileName = "..//reports//OwnersheetsReports//ControlSheetForOwnerNew.jrxml";
		}

		if (numberOfBuses == 2) {
			sourceFileName = "..//reports//OwnersheetsReports//ControlSheetForOwner2.jrxml";
		}
		if (numberOfBuses == 3) {
			sourceFileName = "..//reports//OwnersheetsReports//ControlSheetForOwner3.jrxml";
		}
		if (numberOfBuses == 4) {
			sourceFileName = "..//reports//OwnersheetsReports//ControlSheetForOwner4.jrxml";
		}
		if (numberOfBuses == 5) {
			sourceFileName = "..//reports//OwnersheetsReports//ControlSheetForOwner5.jrxml";
		}
		if (numberOfBuses == 6) {
			sourceFileName = "..//reports//OwnersheetsReports//ControlSheetForOwnerSix.jrxml";
		}
		if (numberOfBuses == 7) {
			sourceFileName = "..//reports//OwnersheetsReports//ControlSheetForOwnerNew7.jrxml";
		}
		if (numberOfBuses == 8) {
			sourceFileName = "..//reports//OwnersheetsReports//ControlSheetForOwnerNew8.jrxml";
		}
		if (numberOfBuses == 9) {
			sourceFileName = "..//reports//OwnersheetsReports//ControlSheetForOwnerNew9.jrxml";
		}
		if (numberOfBuses == 10) {
			sourceFileName = "..//reports//OwnersheetsReports//ControlSheetForOwnerNew10.jrxml";
		}
		if (numberOfBuses == 11) {
			sourceFileName = "..//reports//OwnersheetsReports//ControlSheetForOwnerNew11.jrxml";
		}
		if (numberOfBuses == 12) {
			sourceFileName = "..//reports//OwnersheetsReports//ControlSheetForOwnerNew12.jrxml";
		}
		if (numberOfBuses == 13) {
			sourceFileName = "..//reports//OwnersheetsReports//ControlSheetForOwnerNew13.jrxml";
		}
		if (numberOfBuses == 14) {
			sourceFileName = "..//reports//OwnersheetsReports//ControlSheetForOwnerNew14.jrxml";
		}
		if (numberOfBuses == 15) {
			sourceFileName = "..//reports//OwnersheetsReports//ControlSheetForOwnerNew15.jrxml";
		}
		if (numberOfBuses == 16) {
			sourceFileName = "..//reports//OwnersheetsReports//ControlSheetForOwnerNew16.jrxml";
		}
		if (numberOfBuses == 17) {
			sourceFileName = "..//reports//OwnersheetsReports//ControlSheetForOwnerNew17.jrxml";
		}

		if (numberOfBuses == 18) {
			sourceFileName = "..//reports//OwnersheetsReports//ControlSheetForOwnerNew18.jrxml";
		}
		if (numberOfBuses == 19) {
			sourceFileName = "..//reports//OwnersheetsReports//ControlSheetForOwnerNew19.jrxml";
		}
		if (numberOfBuses == 20) {
			sourceFileName = "..//reports//OwnersheetsReports//ControlSheetForOwnerNew20.jrxml";
		}
		if (numberOfBuses == 21) {
			sourceFileName = "..//reports//OwnersheetsReports//ControlSheetForOwnerNew21.jrxml";
		}
		if (numberOfBuses == 22) {
			sourceFileName = "..//reports//OwnersheetsReports//ControlSheetForOwnerNew22.jrxml";
		}
		if (numberOfBuses == 23) {
			sourceFileName = "..//reports//OwnersheetsReports//ControlSheetForOwnerNew23.jrxml";
		}

		if (numberOfBuses == 24) {
			sourceFileName = "..//reports//OwnersheetsReports//ControlSheetForOwnerNew24.jrxml";
		}

		if (numberOfBuses == 25) {
			sourceFileName = "..//reports//OwnersheetsReports//ControlSheetForOwnerNew25.jrxml";
		}
		if (numberOfBuses == 26) {
			sourceFileName = "..//reports//OwnersheetsReports//ControlSheetForOwnerNew26.jrxml";
		}
		if (numberOfBuses == 27) {
			sourceFileName = "..//reports//OwnersheetsReports//ControlSheetForOwnerNew27.jrxml";
		}
		if (numberOfBuses == 28) {
			sourceFileName = "..//reports//OwnersheetsReports//ControlSheetForOwnerNew28.jrxml";
		}
		if (numberOfBuses == 29) {
			sourceFileName = "..//reports//OwnersheetsReports//ControlSheetForOwnerNew29.jrxml";
		}

		if (numberOfBuses == 30) {
			sourceFileName = "..//reports//OwnersheetsReports//ControlSheetForOwnerNew30.jrxml";
		}
		if (numberOfBuses >= 31) {
			sourceFileName = "..//reports//OwnersheetsReports//OwnerCardForMoreThanThirtyBuses.jrxml";
		}

		return sourceFileName;
	}

	public StreamedContent printOwnerNewReportNew(ActionEvent ae, int leaveBuses, boolean isTwoDay) {
		if (ae.getComponent().getId().equals("ptintOwnerIDXLS")) {
			int currentRow = 0;

			TimeTableDTO routeDTO = timeTableService.getRouteDataForEditPanelGenerator(refNo);

			int numberOfBuses = tripType.equalsIgnoreCase("O") ? routeDTO.getNoOfPvtBusesOrigin()
					: routeDTO.getNoOfPvtBusesDestination();
			int colWidth = (numberOfBuses + 1);
			try {
				XSSFWorkbook workbook = new XSSFWorkbook();
				Sheet sheet = workbook.createSheet("Owner Sheet");
				Row header = sheet.createRow(currentRow);

				CellStyle headerStyle = workbook.createCellStyle();
				XSSFFont font = workbook.createFont();
				font.setBold(true);
				headerStyle.setFont(font);

				Cell headerCell = header.createCell(0);
				headerCell.setCellValue("OWNER SHEET");
				headerCell.setCellStyle(headerStyle);
				sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, colWidth));
				currentRow++;

				header = sheet.createRow(currentRow);
				headerCell = header.createCell(0);
				headerCell.setCellValue(orgin + " To " + destination + " (Route No: " + route + ") " + serviceTypeDes);
				headerCell.setCellStyle(headerStyle);
				sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, colWidth));
				currentRow++;

				header = sheet.createRow(currentRow);
				headerCell = header.createCell(0);
				headerCell.setCellValue("Departure Times Of The Buses From " + depSide + " End ");
				headerCell.setCellStyle(headerStyle);
				sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, colWidth));
				currentRow++;

				header = sheet.createRow(currentRow);
				headerCell = header.createCell(0);
				headerCell.setCellValue("");
				headerCell.setCellStyle(headerStyle);
				currentRow++;

				SimpleDateFormat yearFormatter = new SimpleDateFormat("yyyy");
				header = sheet.createRow(currentRow);
				headerCell = header.createCell(0);
				headerCell.setCellValue(yearFormatter.format(new Date()));
				headerCell.setCellStyle(headerStyle);
				headerCell = header.createCell(1);
				headerCell.setCellValue("Date");
				headerCell.setCellStyle(headerStyle);

				headerCell = header.createCell(colWidth);
				headerCell.setCellValue(yearFormatter.format(new Date()));
				headerCell.setCellStyle(headerStyle);
				headerCell = header.createCell(colWidth - 1);
				headerCell.setCellValue("Date");
				headerCell.setCellStyle(headerStyle);
				currentRow++;

				CellStyle style = workbook.createCellStyle();
				style.setWrapText(true);

				try (Connection conn = ConnectionManager.getConnection()) {
					String sql = "SELECT * FROM public.nt_t_owner_sheet_dates2;";
					Row row = sheet.createRow(currentRow);
					try (PreparedStatement preparedStatement = conn.prepareStatement(sql);
							ResultSet rs = preparedStatement.executeQuery()) {
						while (rs.next()) {
							String value = rs.getString("month");
							Cell cell = row.createCell(0);
							cell.setCellValue(value);
							cell.setCellStyle(headerStyle);

							cell = row.createCell(colWidth);
							cell.setCellValue(value);
							cell.setCellStyle(headerStyle);

							double daysPerCol = Math.floor(31 / numberOfBuses);
							int remainDays = (31 % numberOfBuses);
							for (int x = 1; x <= numberOfBuses; x++) {
								cell = row.createCell(x);
								String dates = "";
								int base = x;
								if (x <= remainDays) {
									for (int n = 1; n <= (daysPerCol + 1); n++) {
										if (dates.isEmpty()) {
											dates += base;
										} else {
											dates += (", " + base);
										}
										base += numberOfBuses;
									}
								} else {
									for (int n = 1; n <= daysPerCol; n++) {
										if (dates.isEmpty()) {
											dates += base;
										} else {
											dates += (", " + base);
										}
										base += numberOfBuses;
									}
								}
								cell.setCellValue(dates);
								cell.setCellStyle(headerStyle);
							}
						}
						currentRow++;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				Row row = sheet.createRow(currentRow);
				Cell cell = row.createCell(0);
				cell.setCellValue("Departure");
				cell.setCellStyle(headerStyle);
				cell = row.createCell(colWidth);
				cell.setCellValue("Departure");
				cell.setCellStyle(headerStyle);
				currentRow++;

				row = sheet.createRow(currentRow);
				cell = row.createCell(0);
				cell.setCellValue(depSide);
				cell.setCellStyle(headerStyle);
				cell = row.createCell(colWidth);
				if (depSide.equalsIgnoreCase(orgin)) {
					cell.setCellValue(destination);
				} else if (depSide.equalsIgnoreCase(destination)) {
					cell.setCellValue(orgin);
				}
				cell.setCellStyle(headerStyle);
				currentRow++;

				// Fill bus data
				try (Connection conn = ConnectionManager.getConnection()) {
					String sql = "SELECT * FROM public.nt_t_owner_sheet_buses;";
					try (PreparedStatement preparedStatement = conn.prepareStatement(sql);
							ResultSet rs = preparedStatement.executeQuery()) {
						while (rs.next()) {
							row = sheet.createRow(currentRow);
							cell = row.createCell(0);
							cell.setCellValue(rs.getString("time_slot"));
							cell.setCellStyle(headerStyle);
							cell = row.createCell(colWidth);
							cell.setCellValue(rs.getString("time_slot_depart"));
							cell.setCellStyle(headerStyle);

							double daysPerCol = Math.floor(31 / numberOfBuses);
							int remainDays = (31 % numberOfBuses);
							for (int x = 1; x <= numberOfBuses; x++) {
								cell = row.createCell(x);
								String dates = "";
								int base = x;
								if (x <= remainDays) {
									for (int n = 1; n <= (daysPerCol + 1); n++) {
										if (dates.isEmpty()) {
											String data = rs.getString("col" + base);
											if (data != null) {
												dates += (data.trim().substring(0, data.length() - 1));
											}
										} else {
											String data = rs.getString("col" + base);
											if (data != null && !data.trim().isEmpty()) {
												data = (data.trim().substring(0, data.length() - 1));
												if (!dates.contains(data.trim())) {
													dates += ("\n" + data.trim());
												}
											}
										}
										base += numberOfBuses;
									}
								} else {
									for (int n = 1; n <= daysPerCol; n++) {
										if (dates.isEmpty()) {
											String data = rs.getString("col" + base);
											if (data != null) {
												dates += (data.trim().substring(0, data.length() - 1));
											}
										} else {
											String data = rs.getString("col" + base);
											if (data != null && !data.trim().isEmpty()) {
												data = (data.trim().substring(0, data.length() - 1));
												if (!dates.contains(data.trim())) {
													dates += ("\n" + data.trim());
												}
											}
										}
										base += numberOfBuses;
									}
								}
								cell.setCellValue(dates);
								cell.setCellStyle(style);
							}
							currentRow++;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				row = sheet.createRow(currentRow);
				cell = row.createCell(0);
				cell.setCellValue("Leave");
				cell.setCellStyle(headerStyle);
				cell = row.createCell(1);
				cell.setCellValue("Leave busses will be empty on that day");
				cell.setCellStyle(headerStyle);
				cell = row.createCell(colWidth);
				cell.setCellValue("Leave");
				cell.setCellStyle(headerStyle);
				sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 1, (colWidth - 1)));
				currentRow++;

				// Fill leave bus data
				try (Connection conn = ConnectionManager.getConnection()) {
					String sql = "SELECT * FROM public.nt_m_control_sheet_leavebus;";
					try (PreparedStatement preparedStatement = conn.prepareStatement(sql);
							ResultSet rs = preparedStatement.executeQuery()) {
						int count = 1;
						while (rs.next()) {
							row = sheet.createRow(currentRow);
							cell = row.createCell(0);
							cell.setCellValue(count);
							cell.setCellStyle(headerStyle);
							cell = row.createCell(colWidth);
							cell.setCellValue(count);
							cell.setCellStyle(headerStyle);
							count++;

							double daysPerCol = Math.floor(31 / numberOfBuses);
							int remainDays = (31 % numberOfBuses);
							for (int x = 1; x <= numberOfBuses; x++) {
								cell = row.createCell(x);
								String dates = "";
								int base = x;
								if (x <= remainDays) {
									for (int n = 1; n <= (daysPerCol + 1); n++) {
										if (dates.isEmpty()) {
											String data = rs.getString("col" + base);
											if (data != null) {
												dates += (data.trim().substring(0, data.length() - 1));
											}
										} else {
											String data = rs.getString("col" + base);
											if (data != null && !data.trim().isEmpty()) {
												data = (data.trim().substring(0, data.length() - 1));
												if (!dates.contains(data.trim())) {
													dates += ("\n" + data.trim());
												}
											}
										}
										base += numberOfBuses;
									}
								} else {
									for (int n = 1; n <= daysPerCol; n++) {
										if (dates.isEmpty()) {
											String data = rs.getString("col" + base);
											if (data != null) {
												dates += (data.trim().substring(0, data.length() - 1));
											}
										} else {
											String data = rs.getString("col" + base);
											if (data != null && !data.trim().isEmpty()) {
												data = (data.trim().substring(0, data.length() - 1));
												if (!dates.contains(data.trim())) {
													dates += ("\n" + data.trim());
												}
											}
										}
										base += numberOfBuses;
									}
								}
								cell.setCellValue(dates);
								cell.setCellStyle(style);
							}
							currentRow++;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				for (int x = 0; x <= colWidth; x++) {
					sheet.autoSizeColumn(x);
				}

				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				workbook.write(outputStream);
				InputStream stream = new ByteArrayInputStream(outputStream.toByteArray());
				files = new DefaultStreamedContent(stream, "application/vnd.ms-excel", "Control_Sheet.xlsx");

				ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
				Map<String, Object> sessionMap = externalContext.getSessionMap();
				sessionMap.put("reportBytes", outputStream.toByteArray());
				sessionMap.put("docType", "xlsx");
				isControlSheetGet = true;
				if (isControlSheetGet) {
					reportService.removeTempTable(refNo);
				}
			} catch (IOException | SQLException e) {
				e.printStackTrace();
			}

		} else {
			String loginUser = sessionBackingBean.getLoginUser();

			files = null;
			String sourceFileName = null;

			Connection conn = null;

			TimeTableDTO routeDTO = timeTableService.getRouteDataForEditPanelGenerator(refNo);

			int numberOfBuses = tripType.equalsIgnoreCase("O") ? routeDTO.getNoOfPvtBusesOrigin()
					: routeDTO.getNoOfPvtBusesDestination();

			try {
				conn = ConnectionManager.getConnection();
				String logopath = "//lk//informatics//ntc//view//reports//";
				// + modified by danilka.j
				sourceFileName = "..//reports//OwnersheetsReports//ControlSheetForOwnerNew10.jrxml";
				List<String> expiredPermitNUmber = reportService.getExpiredPermitNumber(route);

				String subReportPath = "lk/informatics/ntc/view/reports/OwnersheetsReports/";
				// Parameters for report
				Map<String, Object> parameters = new HashMap<String, Object>();

				parameters.put("P_Route_No", route);
				// System.out.println("isTwoDay : " + isTwoDay + " " + "orgin : " + orgin + " "
				// + "depSide : " + depSide);
				if (isTwoDay) {
					if (depSide.equalsIgnoreCase(orgin)) {
						parameters.put("P_Departure", destination);
					} else if (depSide.equalsIgnoreCase(destination)) {
						parameters.put("P_Departure", orgin);
					}

				} else {
					parameters.put("P_Departure", depSide);
				}

				parameters.put("P_Service_Type", serviceTypeDes);
				parameters.put("P_Origin", orgin);
				parameters.put("P_Destination", destination);
				parameters.put("P_Speed", speed);
				parameters.put("P_Distance", distance);
				parameters.put("P_TravelTIme", travelTime);
				parameters.put("P_REf", refNo);
				if (depSide.equalsIgnoreCase(orgin)) {
					parameters.put("P_trip", "O");
					parameters.put("P_OtherSide", destination);
					// System.out.println("2other side destination param: " + destination);

				} else if (depSide.equalsIgnoreCase(destination)) {

					parameters.put("P_trip", "D");
					parameters.put("P_OtherSide", orgin);
					// System.out.println("2other side origin param: " + orgin);

				}
				// System.out.println("departure : " + depSide);

				parameters.put("P_Group_no", groupNo);
				parameters.put("P_national_logo", logopath);
				parameters.put("P_ntc_logo", logopath);

				parameters.put("SUBREPORT_PATH", subReportPath);
				parameters.put("P_Month_Count", monthCount);
				parameters.put("P_ExpiredBusAndPermit", expiredPermitNUmber);
				parameters.put("Report_Ref", reportRefNo);
				System.out.println("Report_Ref: " + reportRefNo);
				// System.out.println("Ex" + expiredPermitNUmber);
				JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

				JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

				byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
				InputStream stream = new ByteArrayInputStream(pdfByteArray);
				files = new DefaultStreamedContent(stream, "Application/pdf", "Control_Sheet_For_Owner.pdf");

				ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
				Map<String, Object> sessionMap = externalContext.getSessionMap();
				sessionMap.put("reportBytes", pdfByteArray);
				sessionMap.put("docType", "pdf");
//				isControlSheetGet = true;
//				if (isControlSheetGet) {
//					reportService.removeTempTable(refNo);
//				}
			} catch (JRException e) {
				e.printStackTrace();
			} finally {
				reportService.removeTempTable(refNo);
				try {
					if (conn != null)
						conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return files;
	}

	public boolean createBusesRotation(String service, boolean busesMoreThanThirty) {
		boolean busRotaionInsert = false;

		boolean busAbreHave = false;
		boolean leaveHave = false;
		boolean busNumAssign = false;
		String originBus = null;
		String destinationBus = null;
		String selectedBusAbbriviation = null;
		int daysInMonth1 = 0;
		int daysInMonthMid = 0;
		;
		int daysInMonthLast = 0;

		String sDate1 = dateRange.trim().substring(0, 10);
		String sDate2 = dateRange.trim().substring(dateRange.lastIndexOf("-") + 1);

		DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		LocalDate currentDate1 = LocalDate.parse(sDate1.trim(), df);
		LocalDate currentDate2 = LocalDate.parse(sDate2.trim(), df);

		// Get month from date
		int monthF = currentDate1.getMonthValue();
		int monthL = currentDate2.getMonthValue();

		int yearF = currentDate1.getYear();
		int yearL = currentDate2.getYear();

		// + added by danilka.j
		TimeTableDTO routeDTO = timeTableService.getRouteDataForEditPanelGenerator(refNo);
		int numberOfBuses = tripType.equalsIgnoreCase("O") ? routeDTO.getNoOfPvtBusesOrigin()
				: routeDTO.getNoOfPvtBusesDestination();
		// -

		if (monthF == monthL) {
			int year = currentDate1.getYear();
			YearMonth yearMonthObject1 = YearMonth.of(year, monthF);
			daysInMonth1 = yearMonthObject1.lengthOfMonth();

		} else if (monthL - monthF > 0) { // same year for 1,2,3 or 1,2

			int year = currentDate1.getYear();

			YearMonth yearMonthObject1 = YearMonth.of(year, monthF);
			daysInMonth1 = yearMonthObject1.lengthOfMonth();

			YearMonth yearMonthObjectLast = YearMonth.of(year, monthL);
			daysInMonthLast = yearMonthObjectLast.lengthOfMonth();

			if (monthL - monthF > 1) {

				YearMonth yearMonthObjectMid = YearMonth.of(year, monthL - 1);
				daysInMonthMid = yearMonthObjectMid.lengthOfMonth();
			}

		} else if (yearL - yearF != 0) {

			daysInMonth1 = monthDays;
		}

		List<String> busAbbreviationOrderListForOrgin = new ArrayList<>();
		List<String> busAbbreviationOrderListForDestination = new ArrayList<>();
		if (refNo != null && groupNo != null && route != null && serviceTypeDes != null && !refNo.trim().isEmpty()
				&& !groupNo.trim().isEmpty() && !route.trim().isEmpty() && !serviceTypeDes.trim().isEmpty()) {
			/** get timeslots **/
			if (depSide.equals(orgin)) {
				tripType = "O";
			} else if (depSide.equals(destination)) {
				tripType = "D";
			}

			// checked selected date is in history or current table
			boolean isFromHistory = reportService.getselectedDateRangeFromHistory(refNo, tripType, dateRange);
			isTwoDay = reportService.getTwoDayRotation(refNo, tripType);
			// System.out.println("isTwoDay : " + isTwoDay);
			originBus = reportService.getAbriviatiosForRoute(route, service, "O");
			destinationBus = reportService.getAbriviatiosForRoute(route, service, "D");

			int numberOfBusesForSide = tripType.equalsIgnoreCase("O") ? routeDTO.getNoOfPvtBusesOrigin()
					: routeDTO.getNoOfPvtBusesDestination();

			if (numberOfBusesForSide != 0) {
				selectedBusAbbriviation = "N";
			} else {
				selectedBusAbbriviation = "Y";
			}
			leaveHave = reportService.checkLeave(tripType, groupNo, route, service, refNo);
			int datesForMon = 0;
			if (leaveHave) {

				originBus = reportService.getAbriviatiosForRoute(route, service, "O");
				destinationBus = reportService.getAbriviatiosForRoute(route, service, "D");
				busAbbreviationOrderListForOrgin = reportService.busAbbreviationOrderWithLeaveForOwner(originBus,
						destinationBus, refNo, "O", groupNo);
				busAbbreviationOrderListForDestination = reportService.busAbbreviationOrderWithLeaveForOwner(originBus,
						destinationBus, refNo, "D", groupNo);

				timeSlotList = reportService.getTimeSlots(tripType, groupNo, route, service, refNo, true,
						selectedBusAbbriviation);
				// System.out.println("isTwoDay before clear : " + isTwoDay);
				if (!isTwoDay) {
					// System.out.println("isTwoDay inside if(not) : " + !isTwoDay);

					endTimeSlotList = reportService.getEndTimeSlots(tripType, groupNo, route, service, refNo,
							selectedBusAbbriviation);
					// System.out.println("1");

				} else {
					// System.out.println("2");
					endTimeSlotList.clear();
					renderLeave = true;
				}

				/** End get timeslots **/

				/** get bus nos **/

				reportService.insertLeavesInTable(refNo, tripType, groupNo, originBus, destinationBus, daysInMonth1,
						selectedBusAbbriviation, 0,reportRefNo);
				busNoList = reportService.getBusNOListNewWithLeaveForOwnerSheer(refNo, tripType, groupNo, timeSlotList,
						busAbbreviationOrderListForOrgin, busAbbreviationOrderListForDestination, originBus,
						destinationBus, daysInMonth1, endTimeSlotList, isFromHistory, selectedBusAbbriviation,
						numberOfBuses,reportRefNo);

				distance = reportService.getDistanceForRoute(route, service);
				speed = reportService.getSpeedForROute(route, service);
				travelTime = reportService.getTravelTimeForROute(route, service);

			} else {

				timeSlotList = reportService.getTimeSlots(tripType, groupNo, route, service, refNo, true,
						selectedBusAbbriviation);
				endTimeSlotList = reportService.getEndTimeSlots(tripType, groupNo, route, service, refNo,
						selectedBusAbbriviation);
				originBus = reportService.getAbriviatiosForRoute(route, service, "O");
				destinationBus = reportService.getAbriviatiosForRoute(route, service, "D");
				busAbbreviationOrderListForOrgin = reportService.busAbbreviationOrder(originBus, destinationBus, refNo,
						"O", groupNo);
				busAbbreviationOrderListForDestination = reportService.busAbbreviationOrder(originBus, destinationBus,
						refNo, "D", groupNo);
				/** End get timeslots **/

				/** get bus nos **/

				// System.out.println("isTwoDay before clear : " + isTwoDay);
				if (!isTwoDay) {
					// System.out.println("isTwoDay inside if(not) : " + !isTwoDay);

					endTimeSlotList = reportService.getEndTimeSlots(tripType, groupNo, route, service, refNo,
							selectedBusAbbriviation);
					// System.out.println("1");

				} else {
					// System.out.println("2");
					endTimeSlotList.clear();
					renderLeave = true;
				}

				busNoList = reportService.getBusNOListNewForOwnerSheet(refNo, tripType, groupNo, timeSlotList,
						busAbbreviationOrderListForOrgin, busAbbreviationOrderListForDestination, originBus,
						destinationBus, daysInMonth1, endTimeSlotList, isFromHistory, selectedBusAbbriviation,
						busesMoreThanThirty, numberOfBuses,reportRefNo);

				distance = reportService.getDistanceForRoute(route, service);
				speed = reportService.getSpeedForROute(route, service);
				travelTime = reportService.getTravelTimeForROute(route, service);

			}

		} else {

			setErrorMsg("Please select  mandotory fields.");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		}

		return busRotaionInsert;
	}

	public boolean createBusesRotationTwoDay(String service, boolean busesMoreThanThirty) {
		boolean busRotaionInsert = false;

		boolean busAbreHave = false;
		boolean leaveHave = false;
		boolean busNumAssign = false;
		String originBus = null;
		String destinationBus = null;
		String selectedBusAbbriviation = null;
		int daysInMonth1 = 0;
		int daysInMonthMid = 0;
		;
		int daysInMonthLast = 0;

		String sDate1 = dateRange.trim().substring(0, 10);
		String sDate2 = dateRange.trim().substring(dateRange.lastIndexOf("-") + 1);

		DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		LocalDate currentDate1 = LocalDate.parse(sDate1.trim(), df);
		LocalDate currentDate2 = LocalDate.parse(sDate2.trim(), df);

		// Get month from date
		int monthF = currentDate1.getMonthValue();
		int monthL = currentDate2.getMonthValue();

		int yearF = currentDate1.getYear();
		int yearL = currentDate2.getYear();

		// + added by danilka.j
		TimeTableDTO routeDTO = timeTableService.getRouteDataForEditPanelGenerator(refNo);
		int numberOfBuses = tripType.equalsIgnoreCase("O") ? routeDTO.getNoOfPvtBusesOrigin()
				: routeDTO.getNoOfPvtBusesDestination();
		// -

		if (monthF == monthL) {
			int year = currentDate1.getYear();
			YearMonth yearMonthObject1 = YearMonth.of(year, monthF);
			daysInMonth1 = yearMonthObject1.lengthOfMonth();

		} else if (monthL - monthF > 0) { // same year for 1,2,3 or 1,2

			int year = currentDate1.getYear();

			YearMonth yearMonthObject1 = YearMonth.of(year, monthF);
			daysInMonth1 = yearMonthObject1.lengthOfMonth();

			YearMonth yearMonthObjectLast = YearMonth.of(year, monthL);
			daysInMonthLast = yearMonthObjectLast.lengthOfMonth();

			if (monthL - monthF > 1) {

				YearMonth yearMonthObjectMid = YearMonth.of(year, monthL - 1);
				daysInMonthMid = yearMonthObjectMid.lengthOfMonth();
			}

		} else if (yearL - yearF != 0) {

			daysInMonth1 = monthDays;
		}

		List<String> busAbbreviationOrderListForOrgin = new ArrayList<>();
		List<String> busAbbreviationOrderListForDestination = new ArrayList<>();
		if (refNo != null && groupNo != null && route != null && serviceTypeDes != null && !refNo.trim().isEmpty()
				&& !groupNo.trim().isEmpty() && !route.trim().isEmpty() && !serviceTypeDes.trim().isEmpty()) {
			/** get timeslots **/
			if (depSide.equals(orgin)) {
				tripType = "O";
			} else if (depSide.equals(destination)) {
				tripType = "D";
			}

			// checked selected date is in history or current table
			boolean isFromHistory = reportService.getselectedDateRangeFromHistory(refNo, tripType, dateRange);
			isTwoDay = reportService.getTwoDayRotation(refNo, tripType);

			if (isTwoDay) {
				renderLeave = true;
			}

			originBus = reportService.getAbriviatiosForRoute(route, service, "O");
			destinationBus = reportService.getAbriviatiosForRoute(route, service, "D");
			if (tripType.equals("O")) {
				selectedBusAbbriviation = originBus;
			} else {
				selectedBusAbbriviation = destinationBus;
			}
			leaveHave = reportService.checkLeave(tripType, groupNo, route, service, refNo);
			int datesForMon = 0;

			int numberOfBusesForSide = tripType.equalsIgnoreCase("D") ? routeDTO.getNoOfPvtBusesOrigin()
					: routeDTO.getNoOfPvtBusesDestination();

			if (numberOfBusesForSide != 0) {
				selectedBusAbbriviation = "N";
			} else {
				selectedBusAbbriviation = "Y";
			}

			if (leaveHave) {

				originBus = reportService.getAbriviatiosForRoute(route, service, "O");
				destinationBus = reportService.getAbriviatiosForRoute(route, service, "D");
				busAbbreviationOrderListForOrgin = reportService.busAbbreviationOrderWithLeaveForOwner(originBus,
						destinationBus, refNo, "O", groupNo);
				busAbbreviationOrderListForDestination = reportService.busAbbreviationOrderWithLeaveForOwner(originBus,
						destinationBus, refNo, "D", groupNo);

				if (tripType.equals("O")) {
					if (selectedBusAbbriviation.equalsIgnoreCase("N")) {
						timeSlotList = reportService.getTimeSlotsTwoDay("D", groupNo, route, service, refNo, true,
								selectedBusAbbriviation);
					} else {
						timeSlotList = reportService.getTimeSlots("D", groupNo, route, service, refNo, true,
								selectedBusAbbriviation);
					}
				} else if (tripType.equals("D")) {
					if (selectedBusAbbriviation.equalsIgnoreCase("N")) {
						timeSlotList = reportService.getTimeSlotsTwoDay("O", groupNo, route, service, refNo, true,
								selectedBusAbbriviation);
					} else {
						timeSlotList = reportService.getTimeSlots("O", groupNo, route, service, refNo, true,
								selectedBusAbbriviation);
					}

				}

				endTimeSlotList = reportService.getEndTimeSlots(tripType, groupNo, route, service, refNo,
						selectedBusAbbriviation);
				/** End get timeslots **/

				/** get bus nos **/

				// if (mon.equals("1")) {
				if (busesMoreThanThirty) {
					reportService.insertLeavesInTableForTwoDay(refNo, tripType, groupNo, originBus, destinationBus,
							daysInMonth1, selectedBusAbbriviation, 0,reportRefNo);
					busNoList = reportService.getBusNOListNewWithLeaveForOwnerSheerTwoDay(refNo, tripType, groupNo,
							timeSlotList, busAbbreviationOrderListForOrgin, busAbbreviationOrderListForDestination,
							originBus, destinationBus, daysInMonth1, endTimeSlotList, isFromHistory,
							selectedBusAbbriviation, numberOfBuses,reportRefNo);
				} else {
					reportService.insertLeavesInTableForTwoDay(refNo, tripType, groupNo, originBus, destinationBus,
							daysInMonth1, selectedBusAbbriviation, 0, reportRefNo);
					busNoList = reportService.getBusNOListNewWithLeaveForOwnerSheerTwoDay(refNo, tripType, groupNo,
							timeSlotList, busAbbreviationOrderListForOrgin, busAbbreviationOrderListForDestination,
							originBus, destinationBus, daysInMonth1, endTimeSlotList, isFromHistory,
							selectedBusAbbriviation, numberOfBuses,reportRefNo);
					// }
				}
				distance = reportService.getDistanceForRoute(route, service);
				speed = reportService.getSpeedForROute(route, service);
				travelTime = reportService.getTravelTimeForROute(route, service);

			} else {

				timeSlotList = reportService.getTimeSlots(tripType, groupNo, route, service, refNo, true,
						selectedBusAbbriviation);
				endTimeSlotList = new ArrayList<>();
				originBus = reportService.getAbriviatiosForRoute(route, service, "O");
				destinationBus = reportService.getAbriviatiosForRoute(route, service, "D");
				busAbbreviationOrderListForOrgin = reportService.busAbbreviationOrder(originBus, destinationBus, refNo,
						"O", groupNo);
				busAbbreviationOrderListForDestination = reportService.busAbbreviationOrder(originBus, destinationBus,
						refNo, "D", groupNo);
				/** End get timeslots **/

				/** get bus nos **/

				busNoList = reportService.getBusNOListNewForOwnerSheetTwoDay(refNo, tripType, groupNo, timeSlotList,
						busAbbreviationOrderListForOrgin, busAbbreviationOrderListForDestination, originBus,
						destinationBus, daysInMonth1, endTimeSlotList, isFromHistory, selectedBusAbbriviation,
						busesMoreThanThirty, numberOfBuses, reportRefNo);

				distance = reportService.getDistanceForRoute(route, service);
				speed = reportService.getSpeedForROute(route, service);
				travelTime = reportService.getTravelTimeForROute(route, service);

			}

		} else {

			setErrorMsg("Please select  mandotory fields.");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		}

		return busRotaionInsert;
	}

	public boolean createDatesRotation(List<String> thirdMonthArray, List<String> secondMonthArray,
			List<String> firstMonthArray, String month1, String month2, String month3, boolean midleMonthHave,
			boolean onlyForOneMonth) {

		int emptyIndexes = 0;

		List<String> dateList1 = new ArrayList<>();
		List<String> dateList2 = new ArrayList<>();
		List<String> dateList3 = new ArrayList<>();
		/** Dates Rotation start **/

		// for 1 month
		if (onlyForOneMonth) {
			for (String t : firstMonthArray) {
				dateList1.add(t);

			}

		}

		// for 2 months
		else if (midleMonthHave == false && secondMonthArray.size() != 0 && firstMonthArray.size() != 0) {
			month2 = month3;
			for (String t : thirdMonthArray) {
				dateList1.add(t);

			}
			/** if dateList1 filled 31 indexes **/
			if (dateList1.size() == 31) {
				for (String s : firstMonthArray) {
					dateList2.add(s);
				}

			}
			/** if dateList1 filled 31 indexes end **/
			/** if dateList1 filled 30 indexes **/
			if (dateList1.size() == 30) {
				emptyIndexes = 31 - dateList1.size();

				for (int i = emptyIndexes + 1; i <= firstMonthArray.size(); i++) {
					dateList2.add(Integer.toString(i));
				}
				for (int e = 1; e <= emptyIndexes; e++) {
					dateList2.add(Integer.toString(e));
					dateList1.add(null);
				}

			}
			/** if dateList1 filled 30 indexes end **/
			/** if dateList1 filled 29 indexes **/
			if (dateList1.size() == 29) {
				emptyIndexes = 31 - dateList1.size();

				for (int i = emptyIndexes + 1; i <= firstMonthArray.size(); i++) {
					dateList2.add(Integer.toString(i));
				}
				for (int e = 1; e <= emptyIndexes; e++) {
					dateList2.add(Integer.toString(e));
					dateList1.add(null);
				}
			}
			/** if dateList1 filled 29 indexes end **/
			/** if dateList1 filled 28 indexes **/
			if (dateList1.size() == 28) {
				emptyIndexes = 31 - dateList1.size();

				for (int i = emptyIndexes + 1; i <= firstMonthArray.size(); i++) {
					dateList2.add(Integer.toString(i));
				}
				for (int e = 1; e <= emptyIndexes; e++) {
					dateList2.add(Integer.toString(e));
					dateList1.add(null);
				}
			}
			/** if dateList1 filled 28 indexes end **/
		}
		// for 3 months
		else {
			for (String t : thirdMonthArray) {
				dateList1.add(t);

			}
			/** if dateList1 filled 31 indexes **/
			if (dateList1.size() == 31) {
				for (String s : secondMonthArray) {
					dateList2.add(s);
				}

				/** if dateList2 filled 31 indexes **/
				if (dateList2.size() == 31) {
					for (String f : firstMonthArray) {
						dateList3.add(f);
					}
				}
				/** if dateList2 filled 31 indexes end **/
				/** if dateList2 filled 30 indexes **/
				if (dateList2.size() == 30) {
					// dateList1 should fill from 2, 1 add into last index
					emptyIndexes = 31 - dateList2.size();

					for (int i = emptyIndexes + 1; i <= firstMonthArray.size(); i++) {
						dateList3.add(Integer.toString(i));
					}
					for (int e = 1; e <= emptyIndexes; e++) {
						dateList3.add(Integer.toString(e));
						dateList2.add(null);
					}

				}
				/** if dateList2 filled 30 indexes end **/
				/** if dateList2 filled 29 indexes **/
				if (dateList2.size() == 29) {
					// dateList1 should fill from 2, 1 add into last index
					emptyIndexes = 31 - dateList2.size();

					for (int i = emptyIndexes + 1; i <= firstMonthArray.size(); i++) {
						dateList3.add(Integer.toString(i));
					}
					for (int e = 1; e <= emptyIndexes; e++) {
						dateList3.add(Integer.toString(e));
						dateList2.add(null);
					}
				}
				/** if dateList2 filled 29 indexes end **/

				/** if dateList2 filled 28 indexes **/
				if (dateList2.size() == 28) {
					// dateList1 should fill from 2, 1 add into last index
					emptyIndexes = 31 - dateList2.size();

					for (int i = emptyIndexes + 1; i <= firstMonthArray.size(); i++) {
						dateList3.add(Integer.toString(i));
					}
					for (int e = 1; e <= emptyIndexes; e++) {
						dateList3.add(Integer.toString(e));
						dateList2.add(null);
					}
				}
				/** if dateList2 filled 28 indexes end **/
			}
			/** if dateList1 filled 31 indexes end **/
			/** if dateList1 filled 30 indexes **/
			if (dateList1.size() == 30) {
				emptyIndexes = 31 - dateList1.size();

				for (int i = emptyIndexes + 1; i <= secondMonthArray.size(); i++) {
					dateList2.add(Integer.toString(i));
				}
				for (int e = 1; e <= emptyIndexes; e++) {
					dateList2.add(Integer.toString(e));
					dateList1.add(null);
				}

				if (secondMonthArray.size() == 31) {
					for (int i = 2; i <= firstMonthArray.size(); i++) {
						dateList3.add(Integer.toString(i));
					}

				}

			}

			/** if dateList1 filled 30 indexes end **/
			/** if dateList1 filled 29 indexes **/
			if (dateList1.size() == 29) {
				emptyIndexes = 31 - dateList1.size();

				for (int i = emptyIndexes + 1; i <= secondMonthArray.size(); i++) {
					dateList2.add(Integer.toString(i));
				}
				for (int e = 1; e <= emptyIndexes; e++) {
					dateList2.add(Integer.toString(e));
					dateList1.add(null);
				}

				if (secondMonthArray.size() == 31) {
					for (int i = 2; i <= firstMonthArray.size(); i++) {
						dateList3.add(Integer.toString(i));
					}

				}

			}
			/** if dateList1 filled 29 indexes end **/
			/** if dateList1 filled 28 indexes **/
			if (dateList1.size() == 28) {
				emptyIndexes = 31 - dateList1.size();

				for (int i = emptyIndexes + 1; i <= secondMonthArray.size(); i++) {
					dateList2.add(Integer.toString(i));
				}
				for (int e = 1; e <= emptyIndexes; e++) {
					dateList2.add(Integer.toString(e));
					dateList1.add(null);
				}

				if (secondMonthArray.size() == 31) {
					for (int i = 2; i <= firstMonthArray.size(); i++) {
						dateList3.add(Integer.toString(i));
					}

				}

			}
			/** if dateList1 filled 28 indexes end **/

		}

		boolean dateRotationInsert = reportService.insertDatesRotations(dateList1, dateList2, dateList3, month1, month2,
				month3);

		return dateRotationInsert;

		/** Dates Rotation end **/
	}

	/*
	 * public void printRotationReport() { rotationList = new ArrayList<>(); if
	 * (serviceTypeDes.equalsIgnoreCase("NORMAL")) { service = "001"; } else if
	 * (serviceTypeDes.equalsIgnoreCase("LUXURY")) { service = "002"; } else if
	 * (serviceTypeDes.equalsIgnoreCase("SUPER LUXURY")) { service = "003"; } else
	 * if (serviceTypeDes.equalsIgnoreCase("SEMI-LUXURY")) { service = "004"; } else
	 * if (serviceTypeDes.equalsIgnoreCase("EXPRESSWAY BUS")) { service = "EB"; }
	 * generatedColumns.clear();
	 * 
	 * TimeTableDTO abbreviation = timeTableService.getAbriviatiosForRoute(route,
	 * service); List<TimeTableDTO> allData =
	 * timeTableService.getDetailsForControlSheetReport(refNo, tripType);
	 * Map<String, TimeTableDTO> displayData = new HashMap<>(); Map<String,
	 * List<Integer>> leaveData = new HashMap<>();
	 * 
	 * int rowCount = 1; int largestDate = 0; for (TimeTableDTO timeTableDTO :
	 * allData) { if (!displayData.containsKey(timeTableDTO.getBusNo())) {
	 * displayData.put(timeTableDTO.getBusNo(), timeTableDTO);
	 * leaveData.put(timeTableDTO.getBusNo(), new ArrayList<Integer>()); } if
	 * (timeTableDTO.getNumOfLeaves() > 0) { if (timeTableDTO.getNoOfDays() >
	 * largestDate) { largestDate = timeTableDTO.getNoOfDays(); }
	 * leaveData.get(timeTableDTO.getBusNo()).add(timeTableDTO.getNoOfDays()); } }
	 * List<String> orderedBusList = new ArrayList<>(); List<String> busList = new
	 * ArrayList<>(displayData.keySet()); for (int x = largestDate; x > 0; x--) {
	 * for (String busNum : busList) { if (!orderedBusList.contains(busNum) &&
	 * leaveData.get(busNum).contains(x)) { orderedBusList.add(busNum); } } if
	 * (busList.isEmpty()) { break; } }
	 * 
	 * // List<Integer> data =
	 * leaveData.get(orderedBusList.get(0)).stream().sorted(Comparator.reverseOrder(
	 * )).collect(Collectors.toList());
	 * 
	 * List<Integer> data = leaveData.get(orderedBusList.get(0)); data.sort(new
	 * Comparator<Integer>() {
	 * 
	 * @Override public int compare(Integer a, Integer b) { return b.compareTo(a); }
	 * });
	 * 
	 * int gap; if (data.size() > 1) { gap = data.get(data.size() - 2) -
	 * data.get(data.size() - 1); } else { gap = 1; }
	 * 
	 * int largestLeaveDay = 0; for (String key : orderedBusList) { // List<Integer>
	 * tempData =
	 * leaveData.get(key).stream().sorted(Comparator.reverseOrder()).collect(
	 * Collectors.toList());
	 * 
	 * List<Integer> tempData = leaveData.get(key); tempData.sort(new
	 * Comparator<Integer>() {
	 * 
	 * @Override public int compare(Integer a, Integer b) { return b.compareTo(a); }
	 * });
	 * 
	 * if (tempData.size() > gap) { tempData = tempData.subList(0, gap); } if
	 * (largestLeaveDay < tempData.get(0)) { largestLeaveDay = tempData.get(0); }
	 * leaveData.replace(key, tempData); } for (String key : orderedBusList) {
	 * List<Integer> tempData = leaveData.get(key); for (int x = 0; x <
	 * tempData.size(); x++) { tempData.set(x, (tempData.get(x) - largestLeaveDay +
	 * gap)); } List<String> tableData = new ArrayList<>(); int leaveCount = 1; int
	 * otherCount = 1; for (int x = 1; x <= gap; x++) { if (tempData.contains(x)) {
	 * tableData.add("RL" + leaveCount); leaveCount++; } else {
	 * tableData.add(abbreviation.getAbbriAtOrigin() + otherCount); otherCount++; }
	 * } String distance = reportService.getDistanceForRoute(route, service); long
	 * totalDistance = (Long.parseLong(distance) * (otherCount - 1));
	 * 
	 * TimeTableDTO timeTableDTO = displayData.get(key);
	 * timeTableDTO.setFullRotationList(new ArrayList<String>());
	 * timeTableDTO.setFullRotationList(new ArrayList<String>());
	 * timeTableDTO.setNoOfDays(otherCount);
	 * timeTableDTO.setTotalDistance(totalDistance);
	 * timeTableDTO.setRowNumber(String.valueOf(rowCount));
	 * timeTableDTO.setFullRotationList(tableData); rowCount++;
	 * rotationList.add(timeTableDTO); }
	 * 
	 * for (int x = 1; x <= gap; x++) { // Create a new column for each midpoint
	 * Column column = new Column(); column.setHeaderText(String.valueOf(x));
	 * generatedColumns.add(column); } }
	 * 
	 * public void printRotationReportOriginal() { List<String> abbreviationListDes
	 * = new ArrayList<>(); List<String> abbreviationListOri = new ArrayList<>();
	 * List<String> abbreviationListLeaveOri = new ArrayList<>(); List<String>
	 * abbreviationListLeaveDes = new ArrayList<>(); List<String> busNoList = new
	 * ArrayList<>(); String distance = null; rotationList = new ArrayList<>();
	 * 
	 * if (serviceTypeDes.equalsIgnoreCase("NORMAL")) {
	 * 
	 * service = "001"; } if (serviceTypeDes.equalsIgnoreCase("LUXURY")) {
	 * 
	 * service = "002"; } if (serviceTypeDes.equalsIgnoreCase("SUPER LUXURY")) {
	 * 
	 * service = "003"; } if (serviceTypeDes.equalsIgnoreCase("SEMI-LUXURY")) {
	 * 
	 * service = "004"; } if (serviceTypeDes.equalsIgnoreCase("EXPRESSWAY BUS")) {
	 * 
	 * service = "EB"; }
	 * 
	 * if (tripType.equalsIgnoreCase("O")) { int countOnGoing = 0; int countOnLeave
	 * = 0; int noOfDays = 0; long totalDistance = 0;
	 * 
	 * List<TimeTableDTO> panelGeneratorOriginRouteList =
	 * timeTableService.getOriginDtoListForSchedule(refNo); List<TimeTableDTO>
	 * panelGeneratorOriginRouteLeaveList =
	 * timeTableService.getLeaveBusesDtoListForEdit(refNo);
	 * 
	 * TimeTableDTO abbriDTO = timeTableService.getAbriviatiosForRoute(route,
	 * service);
	 * 
	 * for (TimeTableDTO panelGeneratorOriginRouteDto :
	 * panelGeneratorOriginRouteList) {
	 * 
	 * if (!panelGeneratorOriginRouteDto.getBusNoOrigin().contains("SLTB") &&
	 * !panelGeneratorOriginRouteDto.getBusNoOrigin().contains("ETC") &&
	 * panelGeneratorOriginRouteDto.getAbbreviationOrigin().contains(abbriDTO.
	 * getAbbriAtOrigin())) { String bus =
	 * panelGeneratorOriginRouteDto.getBusNoOrigin(); busNoList.add(bus);
	 * countOnGoing++; }
	 * 
	 * }
	 * 
	 * for (TimeTableDTO panelGeneratorOriginRouteDto :
	 * panelGeneratorOriginRouteLeaveList) {
	 * 
	 * if (!panelGeneratorOriginRouteDto.getBusNoLeave().contains("SLTB") &&
	 * !panelGeneratorOriginRouteDto.getBusNoLeave().contains("ETC") &&
	 * panelGeneratorOriginRouteDto.getAbbreviationLeave().contains(abbriDTO.
	 * getAbbriAtOrigin())) { String bus =
	 * panelGeneratorOriginRouteDto.getBusNoLeave(); busNoList.add(bus);
	 * countOnLeave++; }
	 * 
	 * }
	 * 
	 * for (int i = 1; i <= countOnGoing; i++) { String element =
	 * abbriDTO.getAbbriAtOrigin() + i; abbreviationListOri.add(element); }
	 * 
	 * for (int i = 1; i <= countOnLeave; i++) { String element = "RL" + i;
	 * abbreviationListLeaveOri.add(element); }
	 * 
	 * noOfDays = countOnGoing ; distance = reportService.getDistanceForRoute(route,
	 * service); totalDistance = Long.parseLong(distance) * noOfDays;
	 * 
	 * int daysCount = 0; for (int i = 0; i < busNoList.size(); i++) { TimeTableDTO
	 * timeTableDTO = new TimeTableDTO(); timeTableDTO.setBusNo(busNoList.get(i));
	 * timeTableDTO.setNoOfDays(noOfDays);
	 * timeTableDTO.setTotalDistance(totalDistance);
	 * 
	 * int rotationOffset = i % abbreviationListOri.size();
	 * 
	 * List<String> rotatedAbbreviationOri = new ArrayList<>(); List<String>
	 * rotatedAbbreviationLeaveOri = new ArrayList<>();
	 * 
	 * for (int j = 0; j < abbreviationListOri.size(); j++) { int newIndexOri = (j +
	 * rotationOffset) % abbreviationListOri.size();
	 * rotatedAbbreviationOri.add(abbreviationListOri.get(newIndexOri)); }
	 * 
	 * for (int j = 0; j < abbreviationListLeaveOri.size(); j++) { int
	 * newIndexLeaveOri = (j + rotationOffset) % abbreviationListLeaveOri.size();
	 * rotatedAbbreviationLeaveOri.add(abbreviationListLeaveOri.get(newIndexLeaveOri
	 * )); }
	 * 
	 * List<String> fullRotationList = new ArrayList<>(); if(i == 0) {
	 * fullRotationList.addAll(rotatedAbbreviationOri);
	 * fullRotationList.addAll(rotatedAbbreviationLeaveOri);
	 * timeTableDTO.setFullRotationList(fullRotationList);
	 * 
	 * }else { if(abbreviationListOri.size() > i) { fullRotationList = new
	 * ArrayList<>(); for(int co =0; co < (abbreviationListOri.size() - i); co++) {
	 * fullRotationList.add(rotatedAbbreviationOri.get(co)); }
	 * 
	 * if(!rotatedAbbreviationLeaveOri.isEmpty()) { for(int co =0; co <
	 * rotatedAbbreviationLeaveOri.size(); co++) {
	 * fullRotationList.add(rotatedAbbreviationLeaveOri.get(co)); }
	 * 
	 * }
	 * 
	 * for(int count = (abbreviationListOri.size() - i); count <
	 * abbreviationListOri.size(); count++) {
	 * fullRotationList.add(rotatedAbbreviationOri.get(count)); }
	 * 
	 * timeTableDTO.setFullRotationList(fullRotationList); } }
	 * 
	 * 
	 * daysCount ++ ; timeTableDTO.setRowNumber(String.valueOf(daysCount));
	 * rotationList.add(timeTableDTO); }
	 * 
	 * generatedColumns.clear();
	 * 
	 * for (TimeTableDTO midpoint : rotationList) { // Create a new column for each
	 * midpoint Column column = new Column();
	 * column.setHeaderText(midpoint.getRowNumber()); generatedColumns.add(column);
	 * }
	 * 
	 * }else { int countOnGoing = 0; int countOnLeave = 0; int noOfDays = 0; long
	 * totalDistance = 0; List<String> busNoListDes = new ArrayList<>(); distance =
	 * null; rotationList = new ArrayList<>();
	 * 
	 * List<TimeTableDTO> panelGeneratorDestinationRouteList = timeTableService
	 * .getDestinationDtoListForSchedule(refNo); List<TimeTableDTO>
	 * panelGeneratorDestinationRouteLeaveList = timeTableService
	 * .getLeaveBusesDtoListForEditDes(refNo);
	 * 
	 * TimeTableDTO abbriDTO = timeTableService.getAbriviatiosForRoute(route,
	 * service);
	 * 
	 * for (TimeTableDTO panelGeneratorDestinationRouteDto :
	 * panelGeneratorDestinationRouteList) {
	 * 
	 * if (!panelGeneratorDestinationRouteDto.getBusNoDestination().contains("SLTB")
	 * && !panelGeneratorDestinationRouteDto.getBusNoDestination().contains("ETC")
	 * && panelGeneratorDestinationRouteDto.getAbbreviationDestination()
	 * .contains(abbriDTO.getAbbriAtDestination())) { String bus =
	 * panelGeneratorDestinationRouteDto.getBusNoDestination();
	 * busNoListDes.add(bus); countOnGoing++; }
	 * 
	 * }
	 * 
	 * for (TimeTableDTO panelGeneratorDestinationRouteDto :
	 * panelGeneratorDestinationRouteLeaveList) {
	 * 
	 * if (!panelGeneratorDestinationRouteDto.getBusNoLeaveDes().contains("SLTB") &&
	 * !panelGeneratorDestinationRouteDto.getBusNoLeaveDes().contains("ETC") &&
	 * panelGeneratorDestinationRouteDto.getAbbreviationLeaveDes()
	 * .contains(abbriDTO.getAbbriAtDestination())) { String bus =
	 * panelGeneratorDestinationRouteDto.getBusNoLeaveDes(); busNoListDes.add(bus);
	 * countOnLeave++; }
	 * 
	 * }
	 * 
	 * for (int i = 1; i <= countOnGoing; i++) { String element =
	 * abbriDTO.getAbbriAtDestination() + i; abbreviationListDes.add(element); }
	 * 
	 * for (int i = 1; i <= countOnLeave; i++) { String element = "RL" + i;
	 * abbreviationListLeaveDes.add(element); }
	 * 
	 * noOfDays = countOnGoing ; distance = reportService.getDistanceForRoute(route,
	 * service); totalDistance = Long.parseLong(distance) * noOfDays;
	 * 
	 * int daysCount = 0; for (int i = 0; i < busNoListDes.size(); i++) {
	 * TimeTableDTO timeTableDTO = new TimeTableDTO();
	 * timeTableDTO.setBusNo(busNoListDes.get(i));
	 * timeTableDTO.setNoOfDays(noOfDays);
	 * timeTableDTO.setTotalDistance(totalDistance);
	 * 
	 * int rotationOffset = i % abbreviationListDes.size();
	 * 
	 * List<String> rotatedAbbreviationOri = new ArrayList<>(); List<String>
	 * rotatedAbbreviationLeaveOri = new ArrayList<>();
	 * 
	 * for (int j = 0; j < abbreviationListDes.size(); j++) { int newIndexOri = (j +
	 * rotationOffset) % abbreviationListDes.size();
	 * rotatedAbbreviationOri.add(abbreviationListDes.get(newIndexOri)); }
	 * 
	 * for (int j = 0; j < abbreviationListLeaveDes.size(); j++) { int
	 * newIndexLeaveOri = (j + rotationOffset) % abbreviationListLeaveDes.size();
	 * rotatedAbbreviationLeaveOri.add(abbreviationListLeaveDes.get(newIndexLeaveOri
	 * )); }
	 * 
	 * List<String> fullRotationList = new ArrayList<>(); if(i == 0) {
	 * fullRotationList.addAll(rotatedAbbreviationOri);
	 * fullRotationList.addAll(rotatedAbbreviationLeaveOri);
	 * timeTableDTO.setFullRotationList(fullRotationList);
	 * 
	 * }else { fullRotationList = new ArrayList<>(); for(int co =0; co <
	 * (abbreviationListDes.size() - i); co++) {
	 * fullRotationList.add(rotatedAbbreviationOri.get(co)); }
	 * 
	 * if(!rotatedAbbreviationLeaveOri.isEmpty()) { for(int co =0; co <
	 * rotatedAbbreviationLeaveOri.size(); co++) {
	 * fullRotationList.add(rotatedAbbreviationLeaveOri.get(co)); }
	 * 
	 * }
	 * 
	 * for(int count = (abbreviationListDes.size() - i); count <
	 * abbreviationListDes.size(); count++) {
	 * fullRotationList.add(rotatedAbbreviationOri.get(count)); }
	 * 
	 * timeTableDTO.setFullRotationList(fullRotationList); }
	 * 
	 * 
	 * daysCount ++ ; timeTableDTO.setRowNumber(String.valueOf(daysCount));
	 * rotationList.add(timeTableDTO); }
	 * 
	 * generatedColumns.clear();
	 * 
	 * for (TimeTableDTO midpoint : rotationList) { // Create a new column for each
	 * midpoint Column column = new Column();
	 * column.setHeaderText(midpoint.getRowNumber()); generatedColumns.add(column);
	 * } } }
	 */

	public void printRotationReport() {
		List<String> abbreviationListDes = new ArrayList<>();
		List<String> abbreviationListOri = new ArrayList<>();
		List<String> abbreviationListLeaveOri = new ArrayList<>();
		List<String> abbreviationListLeaveDes = new ArrayList<>();
		List<String> busNoList = new ArrayList<>();
		List<String> abbrivationList = new ArrayList<>();
		String distance = null;
		rotationList = new ArrayList<>();

		if (serviceTypeDes.equalsIgnoreCase("NORMAL")) {

			service = "001";
		}
		if (serviceTypeDes.equalsIgnoreCase("LUXURY")) {

			service = "002";
		}
		if (serviceTypeDes.equalsIgnoreCase("SUPER LUXURY")) {

			service = "003";
		}
		if (serviceTypeDes.equalsIgnoreCase("SEMI-LUXURY")) {

			service = "004";
		}
		if (serviceTypeDes.equalsIgnoreCase("EXPRESSWAY BUS")) {

			service = "EB";
		}

		if (tripType.equalsIgnoreCase("O")) {
			int countOnGoing = 0;
			int countOnLeave = 0;
			int noOfDays = 0;
			long totalDistance = 0;

			List<String> includedAbbreviation = new ArrayList<>();
			List<TimeTableDTO> panelGeneratorOriginRouteList = timeTableService.getOriginDtoListForSchedule(refNo);
			List<TimeTableDTO> panelGeneratorOriginRouteLeaveList = timeTableService.getLeaveBusesDtoListForEdit(refNo);

			TimeTableDTO abbriDTO = timeTableService.getAbriviatiosForRoute(route, service);

			for (TimeTableDTO panelGeneratorOriginRouteDto : panelGeneratorOriginRouteList) {

				if (!panelGeneratorOriginRouteDto.getBusNoOrigin().contains("SLTB")
						&& !panelGeneratorOriginRouteDto.getBusNoOrigin().contains("ETC")
						&& panelGeneratorOriginRouteDto.getAbbreviationOrigin().contains(abbriDTO.getAbbriAtOrigin())
						&& !panelGeneratorOriginRouteDto.isFixBusOrigin()
						&& !includedAbbreviation.contains(panelGeneratorOriginRouteDto.getAbbreviationOrigin())) {

					String bus = panelGeneratorOriginRouteDto.getBusNoOrigin();
					busNoList.add(bus);
					abbrivationList.add(panelGeneratorOriginRouteDto.getAbbreviationOrigin());
					includedAbbreviation.addAll(abbrivationList);
					countOnGoing++;

				}

			}

			for (TimeTableDTO panelGeneratorOriginRouteDto : panelGeneratorOriginRouteLeaveList) {

				if (!panelGeneratorOriginRouteDto.getBusNoLeave().contains("SLTB")
						&& !panelGeneratorOriginRouteDto.getBusNoLeave().contains("ETC")
						&& panelGeneratorOriginRouteDto.getAbbreviationLeave().contains(abbriDTO.getAbbriAtOrigin())) {
					String bus = panelGeneratorOriginRouteDto.getBusNoLeave();
					busNoList.add(bus);
					countOnLeave++;
					abbrivationList.add("RL" + countOnLeave);
				}

			}

			noOfDays = countOnGoing;
			distance = reportService.getDistanceForRoute(route, service);
			totalDistance = Long.parseLong(distance) * noOfDays;

			for (int i = 0; i < busNoList.size(); i++) {
				TimeTableDTO timeTableDTO = new TimeTableDTO();
				timeTableDTO.setBusNo(busNoList.get(i));
				timeTableDTO.setNoOfDays(noOfDays);
				timeTableDTO.setTotalDistance(totalDistance);
				int n = abbrivationList.size();
				int rotationOffset = i % n;

				List<String> rotatedBuses = new ArrayList<>();

				rotatedBuses.addAll(abbrivationList.subList(i, abbrivationList.size()));
				rotatedBuses.addAll(abbrivationList.subList(0, i));

				timeTableDTO.setFullRotationList(rotatedBuses);
				timeTableDTO.setRowNumber(String.valueOf(i + 1));
				rotationList.add(timeTableDTO);
			}

//			for (int i = 1; i <= countOnGoing; i++) {
//				String element = abbriDTO.getAbbriAtOrigin() + i;
//				abbreviationListOri.add(element);
//			}
//
//			for (int i = 1; i <= countOnLeave; i++) {
//				String element = "RL";
//				abbreviationListLeaveOri.add(element);
//			}
//
//			
//
//			
//			for (int i = 0; i < busNoList.size(); i++) {
//				TimeTableDTO timeTableDTO = new TimeTableDTO();
//				timeTableDTO.setBusNo(busNoList.get(i));
//				timeTableDTO.setNoOfDays(noOfDays);
//				timeTableDTO.setTotalDistance(totalDistance);
//
//				int rotationOffset = i % abbreviationListOri.size();
//
//				List<String> rotatedAbbreviationOri = new ArrayList<>();
//				List<String> rotatedAbbreviationLeaveOri = new ArrayList<>();
//
//				for (int j = 0; j < abbreviationListOri.size(); j++) {
//					int newIndexOri = (j + rotationOffset) % abbreviationListOri.size();
//					rotatedAbbreviationOri.add(abbreviationListOri.get(newIndexOri));
//				}
//
//				for (int j = 0; j < abbreviationListLeaveOri.size(); j++) {
//					int newIndexLeaveOri = (j + rotationOffset) % abbreviationListLeaveOri.size();
//					rotatedAbbreviationLeaveOri.add(abbreviationListLeaveOri.get(newIndexLeaveOri));
//				}
//
//				List<String> fullRotationList = new ArrayList<>();
//				if (i == 0) {
//					fullRotationList.addAll(rotatedAbbreviationOri);
//					fullRotationList.addAll(rotatedAbbreviationLeaveOri);
//					timeTableDTO.setFullRotationList(fullRotationList);
//
//				} else {
//					fullRotationList = new ArrayList<>();
//					for (int co = 0; co < (abbreviationListOri.size() - i); co++) {
//						fullRotationList.add(rotatedAbbreviationOri.get(co));
//					}
//
//					if (!rotatedAbbreviationLeaveOri.isEmpty()) {
//						fullRotationList.add(rotatedAbbreviationLeaveOri.get(0));
//					}
//
//					for (int count = (abbreviationListOri.size() - i); count < abbreviationListOri.size(); count++) {
//						fullRotationList.add(rotatedAbbreviationOri.get(count));
//					}
//
//					timeTableDTO.setFullRotationList(fullRotationList);
//				}
//
//				daysCount++;
//				timeTableDTO.setRowNumber(String.valueOf(daysCount));
//				rotationList.add(timeTableDTO);
//			}

			generatedColumns.clear();

			for (TimeTableDTO midpoint : rotationList) {
				// Create a new column for each midpoint
				Column column = new Column();
				column.setHeaderText(midpoint.getRowNumber());
				generatedColumns.add(column);
			}

		} else {
			int countOnGoing = 0;
			int countOnLeave = 0;
			int noOfDays = 0;
			long totalDistance = 0;
			List<String> busNoListDes = new ArrayList<>();
			distance = null;
			rotationList = new ArrayList<>();
			List<String> includedAbbreviation = new ArrayList<>();

			List<TimeTableDTO> panelGeneratorDestinationRouteList = timeTableService
					.getDestinationDtoListForSchedule(refNo);
			List<TimeTableDTO> panelGeneratorDestinationRouteLeaveList = timeTableService
					.getLeaveBusesDtoListForEditDes(refNo);

			TimeTableDTO abbriDTO = timeTableService.getAbriviatiosForRoute(route, service);

			for (TimeTableDTO panelGeneratorDestinationRouteDto : panelGeneratorDestinationRouteList) {

				if (!panelGeneratorDestinationRouteDto.getBusNoDestination().contains("SLTB")
						&& !panelGeneratorDestinationRouteDto.getBusNoDestination().contains("ETC")
						&& panelGeneratorDestinationRouteDto.getAbbreviationDestination()
								.contains(abbriDTO.getAbbriAtDestination())
						&& !panelGeneratorDestinationRouteDto.isFixBusDestination() && !includedAbbreviation
								.contains(panelGeneratorDestinationRouteDto.getAbbreviationDestination())) {
					String bus = panelGeneratorDestinationRouteDto.getBusNoDestination();
					abbrivationList.add(panelGeneratorDestinationRouteDto.getAbbreviationDestination());
					includedAbbreviation.addAll(abbrivationList);
					busNoListDes.add(bus);
					countOnGoing++;
				}

			}

			for (TimeTableDTO panelGeneratorDestinationRouteDto : panelGeneratorDestinationRouteLeaveList) {

				if (!panelGeneratorDestinationRouteDto.getBusNoLeaveDes().contains("SLTB")
						&& !panelGeneratorDestinationRouteDto.getBusNoLeaveDes().contains("ETC")
						&& panelGeneratorDestinationRouteDto.getAbbreviationLeaveDes()
								.contains(abbriDTO.getAbbriAtDestination())) {
					String bus = panelGeneratorDestinationRouteDto.getBusNoLeaveDes();
					busNoListDes.add(bus);
					countOnLeave++;
					abbrivationList.add("RL" + countOnLeave);
				}

			}

			noOfDays = countOnGoing;
			distance = reportService.getDistanceForRoute(route, service);
			totalDistance = Long.parseLong(distance) * noOfDays;

			for (int i = 0; i < busNoListDes.size(); i++) {
				TimeTableDTO timeTableDTO = new TimeTableDTO();
				timeTableDTO.setBusNo(busNoListDes.get(i));
				timeTableDTO.setNoOfDays(noOfDays);
				timeTableDTO.setTotalDistance(totalDistance);
				int n = abbrivationList.size();
				int rotationOffset = i % n;

				List<String> rotatedBuses = new ArrayList<>();

				rotatedBuses.addAll(abbrivationList.subList(i, abbrivationList.size()));
				rotatedBuses.addAll(abbrivationList.subList(0, i));

				timeTableDTO.setFullRotationList(rotatedBuses);
				timeTableDTO.setRowNumber(String.valueOf(i + 1));
				rotationList.add(timeTableDTO);
			}

//			for (int i = 1; i <= countOnGoing; i++) {
//				String element = abbriDTO.getAbbriAtDestination() + i;
//				abbreviationListDes.add(element);
//			}
//
//			for (int i = 1; i <= countOnLeave; i++) {
//				String element = "RL" + i;
//				abbreviationListLeaveDes.add(element);
//			}
//
//			noOfDays = countOnGoing;
//			distance = reportService.getDistanceForRoute(route, service);
//			totalDistance = Long.parseLong(distance) * noOfDays;
//
//			int daysCount = 0;
//			for (int i = 0; i < busNoListDes.size(); i++) {
//				TimeTableDTO timeTableDTO = new TimeTableDTO();
//				timeTableDTO.setBusNo(busNoListDes.get(i));
//				timeTableDTO.setNoOfDays(noOfDays);
//				timeTableDTO.setTotalDistance(totalDistance);
//
//				int rotationOffset = i % abbreviationListDes.size();
//
//				List<String> rotatedAbbreviationOri = new ArrayList<>();
//				List<String> rotatedAbbreviationLeaveOri = new ArrayList<>();
//
//				for (int j = 0; j < abbreviationListDes.size(); j++) {
//					int newIndexOri = (j + rotationOffset) % abbreviationListDes.size();
//					rotatedAbbreviationOri.add(abbreviationListDes.get(newIndexOri));
//				}
//
//				for (int j = 0; j < abbreviationListLeaveDes.size(); j++) {
//					int newIndexLeaveOri = (j + rotationOffset) % abbreviationListLeaveDes.size();
//					rotatedAbbreviationLeaveOri.add(abbreviationListLeaveDes.get(newIndexLeaveOri));
//				}
//
//				List<String> fullRotationList = new ArrayList<>();
//				if (i == 0) {
//					fullRotationList.addAll(rotatedAbbreviationOri);
//					fullRotationList.addAll(rotatedAbbreviationLeaveOri);
//					timeTableDTO.setFullRotationList(fullRotationList);
//
//				} else {
//					fullRotationList = new ArrayList<>();
//					for (int co = 0; co < (abbreviationListDes.size() - i); co++) {
//						fullRotationList.add(rotatedAbbreviationOri.get(co));
//					}
//
//					if (!rotatedAbbreviationLeaveOri.isEmpty()) {
//						fullRotationList.add(rotatedAbbreviationLeaveOri.get(0));
//					}
//
//					for (int count = (abbreviationListDes.size() - i); count < abbreviationListDes.size(); count++) {
//						fullRotationList.add(rotatedAbbreviationOri.get(count));
//					}
//
//					timeTableDTO.setFullRotationList(fullRotationList);
//				}
//
//				daysCount++;
//				timeTableDTO.setRowNumber(String.valueOf(daysCount));
//				rotationList.add(timeTableDTO);
//			}

			generatedColumns.clear();

			for (TimeTableDTO midpoint : rotationList) {
				// Create a new column for each midpoint
				Column column = new Column();
				column.setHeaderText(midpoint.getRowNumber());
				generatedColumns.add(column);
			}
		}

		generateRotationExcel();

	}

	// added by danilka.j
	public static OwnerSheetListMonthWrapper generateDatePatternForOwnerSheet(String[] ridingDays, int noOfBuses) {
		if (ridingDays == null || ridingDays.length == 0 || noOfBuses <= 0) {
			// System.out.println("ridingDays : " + ridingDays + " " + "noOfBuses : " +
			// noOfBuses + " " + "ridingDays.length : " + ridingDays.length);
			throw new IllegalArgumentException("Invalid input");
		}

		String month = ridingDays[ridingDays.length - 2];

		String[] columnData = new String[noOfBuses];
		for (int i = 0; i < noOfBuses; i++) {
			StringBuilder scheduleBuilder = new StringBuilder();
			columnData[i] = scheduleBuilder.toString();
		}

		for (int i = 0; i < ridingDays.length; i++) {
			// Check for null value and break the loop
			if (ridingDays[i] == null) {
				break;
			}
			columnData[i % noOfBuses] += ridingDays[i] + ",";
		}

		return new OwnerSheetListMonthWrapper(columnData, month);
	}

	// added by danilka.j
	public void insertRidingDaysForOwnerSheet(List<String[]> dateListByMonth, int numberOfBuses) {
		if (!dateListByMonth.isEmpty()) {
			for (String[] row : dateListByMonth) {
				timeTableService.insertRidingDaysForOwnershipReport(
						generateDatePatternForOwnerSheet(row, numberOfBuses), reportRefNo);
			}
		} else {
			// handle
		}

	}

	// added by gayathra.m
	public void generateRotationExcel() {

		if (rotationList != null && !rotationList.isEmpty()) {
			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet("Rotation Sheet");
			sheet.getPrintSetup().setLandscape(true);

			Row row1 = sheet.createRow(0);
			row1.setHeight((short) 1000);
			int maxColumn = 0;

			try {

				InputStream nlStream = TimeTableReportsBackingBean.class
						.getResourceAsStream("..//reports//nationalLogo.png");
				InputStream ntcStream = TimeTableReportsBackingBean.class
						.getResourceAsStream("..//reports//ntclogo.jpg");
				byte[] nlBytes = IOUtils.toByteArray(nlStream);
				byte[] ntcBytes = IOUtils.toByteArray(ntcStream);

				int nationalLogo = workbook.addPicture(nlBytes, Workbook.PICTURE_TYPE_PNG);
				int ntcLogo = workbook.addPicture(ntcBytes, Workbook.PICTURE_TYPE_PNG);

				XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();

				XSSFClientAnchor nationalLogoAncor = new XSSFClientAnchor();
				XSSFClientAnchor ntcLogoAncor = new XSSFClientAnchor();

				CellStyle topHeadingStyle = workbook.createCellStyle();
				topHeadingStyle.setAlignment(HorizontalAlignment.CENTER);
				topHeadingStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				topHeadingStyle.setWrapText(true);

				CellStyle tableHeaderStyle = workbook.createCellStyle();
				tableHeaderStyle.setAlignment(HorizontalAlignment.LEFT);
				tableHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				tableHeaderStyle.setBorderTop(BorderStyle.THIN);
				tableHeaderStyle.setBorderBottom(BorderStyle.THIN);
				tableHeaderStyle.setBorderLeft(BorderStyle.THIN);
				tableHeaderStyle.setBorderRight(BorderStyle.THIN);
				Font font = workbook.createFont();
				font.setBold(true);
				tableHeaderStyle.setFont(font);

				String mainHeading = "NATIONAL TRANSPORT COMMISSION\nRotaion Logic Report";
				Cell headerCell = row1.createCell(0);

				RichTextString richText = workbook.getCreationHelper().createRichTextString(mainHeading);
				Font fontFirstLine = workbook.createFont();
				fontFirstLine.setFontHeightInPoints((short) 16);
				fontFirstLine.setBold(true);
				richText.applyFont(0, mainHeading.indexOf('\n'), fontFirstLine);

				Font fontSecondLine = workbook.createFont();
				fontSecondLine.setFontHeightInPoints((short) 12);
				richText.applyFont(mainHeading.indexOf('\n') + 1, mainHeading.length(), fontSecondLine);

				headerCell.setCellValue(richText);
				headerCell.setCellStyle(topHeadingStyle);

				// report reference no and route details
				String lblPanelRef = "Panel Reference No:";
				String lblGroupNoAndServiceType = "Service Type & Group No:";
				String lblRoute = "Route Info:";
				String lblDepatureSide = "Departure Side:";

				Row row2 = sheet.createRow(2);
				Cell row2Cell1 = row2.createCell(0);
				row2Cell1.setCellValue(lblPanelRef);

				Cell row2Cell3 = row2.createCell(3);
				row2Cell3.setCellValue(refNo);

				Row row3 = sheet.createRow(3);
				Cell row3Cell1 = row3.createCell(0);
				row3Cell1.setCellValue(lblGroupNoAndServiceType);

				Cell row3Cell2 = row3.createCell(3);
				row3Cell2.setCellValue(serviceTypeDes + " - " + groupNo);

				Row row4 = sheet.createRow(4);
				Cell row4Cell1 = row4.createCell(0);
				row4Cell1.setCellValue(lblRoute);

				Cell row4Cell2 = row4.createCell(3);
				row4Cell2.setCellValue(route + " (" + orgin + " to " + destination + ")");

				Row row5 = sheet.createRow(5);
				Cell row5Cell1 = row5.createCell(0);
				row5Cell1.setCellValue(lblDepatureSide);

				Cell row5Cell2 = row5.createCell(3);
				row5Cell2.setCellValue(depSide);

				// generate table data
				Row headerRow = sheet.createRow(7);
				List<String> headers = new ArrayList<>(Arrays.asList("No", "Bus Number", "Number of Days", "Distance"));
				List<String> dynamicHeaders = new ArrayList<>();
				for (int i = 1; i <= rotationList.size(); i++) {
					dynamicHeaders.add(String.valueOf(i));
				}
				headers.addAll(2, dynamicHeaders);
				maxColumn = headers.size();

				for (int i = 0; i < headers.size(); i++) {
					Cell cell = headerRow.createCell(i);
					cell.setCellValue(headers.get(i));
					cell.setCellStyle(tableHeaderStyle);
				}

				int rowNum = 8;
				for (TimeTableDTO dto : rotationList) {
					Row row = sheet.createRow(rowNum++);

					int cellNum = 0;
					Cell cell1 = row.createCell(cellNum++);
					cell1.setCellValue((String) dto.getRowNumber());
					setBorder(workbook, cell1);

					Cell cell2 = row.createCell(cellNum++);
					cell2.setCellValue((String) dto.getBusNo());
					setBorder(workbook, cell2);

					if (dto.getFullRotationList() != null && !dto.getFullRotationList().isEmpty()) {
						for (String rotationDTO : dto.getFullRotationList()) {
							Cell dyncamicCell = row.createCell(cellNum++);
							dyncamicCell.setCellValue((String) rotationDTO);
							setBorder(workbook, dyncamicCell);
						}
					}

					Cell cell4 = row.createCell(cellNum++);
					cell4.setCellValue(String.valueOf(dto.getNoOfDays()));
					setBorder(workbook, cell4);

					Cell cell5 = row.createCell(cellNum++);
					cell5.setCellValue(String.valueOf(dto.getTotalDistance()));
					setBorder(workbook, cell5);

				}

				// set logo
				ntcLogoAncor.setCol1(maxColumn - 1);
				ntcLogoAncor.setCol2(maxColumn);
				ntcLogoAncor.setRow1(0);
				ntcLogoAncor.setRow2(1);

				nationalLogoAncor.setCol1(0);
				nationalLogoAncor.setCol2(1);
				nationalLogoAncor.setRow1(0);
				nationalLogoAncor.setRow2(1);

				drawing.createPicture(nationalLogoAncor, nationalLogo);
				drawing.createPicture(ntcLogoAncor, ntcLogo);

				sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, maxColumn - 1));
				sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, maxColumn));

				sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 2));
				sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 2));
				sheet.addMergedRegion(new CellRangeAddress(4, 4, 0, 2));
				sheet.addMergedRegion(new CellRangeAddress(5, 5, 0, 2));

				sheet.addMergedRegion(new CellRangeAddress(2, 2, 3, maxColumn - 1));
				sheet.addMergedRegion(new CellRangeAddress(3, 3, 3, maxColumn - 1));
				sheet.addMergedRegion(new CellRangeAddress(4, 4, 3, maxColumn - 1));
				sheet.addMergedRegion(new CellRangeAddress(5, 5, 3, maxColumn - 1));
				sheet.addMergedRegion(new CellRangeAddress(6, 6, 0, maxColumn - 1));

				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				workbook.write(bos);
				byte[] byteArray = bos.toByteArray();

				if (byteArray != null) {
					InputStream stream = new ByteArrayInputStream(byteArray);
					rotationSheet = new DefaultStreamedContent(stream,
							"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", refNo + ".xlsx");
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					workbook.close();
				} catch (IOException io) {
					io.printStackTrace();
				}
			}
		}
	}

	private static void setBorder(Workbook workbook, Cell cell) {
		CellStyle style = workbook.createCellStyle();
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		cell.setCellStyle(style);
	}

	
	/* created new method on (30/04/2024) by dhananjika.d 
	 * This method will insert data into nt_t_terminal_time_table_schedule table */
	
	public void saveTerminalData() throws ParseException {
		boolean busAbreHave = false;
		boolean leaveHave = false;
		boolean busNumAssign = false;
		String originBus = null;
		String destinationBus = null;

		List<String> busAbbreviationOrderListForOrgin = new ArrayList<>();
		List<String> busAbbreviationOrderListForDestination = new ArrayList<>();

		List<String> busNoListForOrigin = new ArrayList<>();
		List<String> busNoListForDestination = new ArrayList<>();

		if (depSide.equals(orgin)) {
			tripType = "O";
		} else if (depSide.equals(destination)) {
			tripType = "D";
		}

		if (serviceTypeDes.equalsIgnoreCase("NORMAL")) {

			service = "001";
		}
		if (serviceTypeDes.equalsIgnoreCase("LUXURY")) {

			service = "002";
		}
		if (serviceTypeDes.equalsIgnoreCase("SUPER LUXURY")) {

			service = "003";
		}
		if (serviceTypeDes.equalsIgnoreCase("SEMI-LUXURY")) {

			service = "004";
		}
		if (serviceTypeDes.equalsIgnoreCase("EXPRESSWAY BUS")) {

			service = "EB";
		}
		
		boolean alreadyInserted = reportService.alreadySaved(refNo);
		
		if (alreadyInserted) {
			setErrorMsg("This record has already inserted.");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

			return;
		}

		// create date for printed control sheet
		isTwoDay = reportService.getTwoDayRotation(refNo, tripType);
		String terminalCode = routeScheduleService.getTerminalCode(route, service);
		leaveHave = reportService.checkLeave(tripType, groupNo, route, service, refNo);
		int dateCount = getDaysForMonths();
		int previousDatesCount = getDaysForPreviousMonths();
		int datesForMon = 0;
		if (leaveHave) {

			timeSlotList = routeScheduleService.getTimes(refNo, "O");
			timeSlotListFix = routeScheduleService.getEndTimes(refNo, "O");

			originBus = reportService.getAbriviatiosForRoute(route, service, "O");
			destinationBus = reportService.getAbriviatiosForRoute(route, service, "D");

			busAbbreviationOrderListForOrgin = reportService.getOriginBusListWithLeave(refNo, "O");
			busAbbreviationOrderListForDestination = reportService.getOriginBusListWithLeave(refNo, "D");

			busNoListForOrigin = reportService.busNoListWithLeave(originBus, destinationBus, refNo, "O", groupNo);
			busNoListForDestination = reportService.busNoListWithLeave(originBus, destinationBus, refNo, "D", groupNo);

			busNoList = reportService.insertTerminalData(refNo, "O", service, timeSlotList, busNoListForOrigin,
					busNoListForDestination, terminalCode, route, dateCount, false, timeSlotListFix,
					busAbbreviationOrderListForOrgin, busAbbreviationOrderListForDestination, previousDatesCount,
					dateList);

			if (isTwoDay) {
				busNoList = reportService.insertTerminalDataTwoDay(refNo, "O", service, timeSlotList,
						busNoListForOrigin, busNoListForDestination, terminalCode, route, dateCount, false,
						timeSlotListFix, busAbbreviationOrderListForOrgin, busAbbreviationOrderListForDestination,
						previousDatesCount, dateList);
			}

			timeSlotList = routeScheduleService.getTimes(refNo, "D");
			timeSlotListFix = routeScheduleService.getEndTimes(refNo, "D");

			busNoList = reportService.insertTerminalData(refNo, "D", service, timeSlotList, busNoListForOrigin,
					busNoListForDestination, terminalCode, route, dateCount, false, timeSlotListFix,
					busAbbreviationOrderListForOrgin, busAbbreviationOrderListForDestination, previousDatesCount,
					dateList);

			if (isTwoDay) {
				busNoList = reportService.insertTerminalDataTwoDay(refNo, "D", service, timeSlotList,
						busNoListForOrigin, busNoListForDestination, terminalCode, route, dateCount, false,
						timeSlotListFix, busAbbreviationOrderListForOrgin, busAbbreviationOrderListForDestination,
						previousDatesCount, dateList);
			}
			
			setSuccessMessage("Data saved successfully.");
			RequestContext.getCurrentInstance().update("frmsuccess");
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
		} else {

			timeSlotList = routeScheduleService.getTimes(refNo, "O");
			timeSlotListFix = routeScheduleService.getEndTimes(refNo, "O");

			originBus = reportService.getAbriviatiosForRoute(route, service, "O");
			destinationBus = reportService.getAbriviatiosForRoute(route, service, "D");

			busAbbreviationOrderListForOrgin = reportService.getOriginBusListWithLeave(refNo, "O");
			busAbbreviationOrderListForDestination = reportService.getOriginBusListWithLeave(refNo, "D");

			busNoListForOrigin = reportService.busNoListWithLeave(originBus, destinationBus, refNo, "O", groupNo);
			busNoListForDestination = reportService.busNoListWithLeave(originBus, destinationBus, refNo, "D", groupNo);

			busNoList = reportService.insertTerminalDataWithoutLeave(refNo, "O", service, timeSlotList,
					busNoListForOrigin, busNoListForDestination, terminalCode, route, dateCount, false,
					timeSlotListFix, busAbbreviationOrderListForOrgin, busAbbreviationOrderListForDestination,
					previousDatesCount, dateList);
			
			if(isTwoDay) {
				busNoList = reportService.insertTerminalDataTwoDayWithoutLeave(refNo, "O", service, timeSlotList,
						busNoListForOrigin, busNoListForDestination, terminalCode, route, dateCount, false,
						timeSlotListFix, busAbbreviationOrderListForOrgin, busAbbreviationOrderListForDestination,
						previousDatesCount, dateList);
			}

			timeSlotList = routeScheduleService.getTimes(refNo, "D");
			timeSlotListFix = routeScheduleService.getEndTimes(refNo, "D");

			busNoList = reportService.insertTerminalDataWithoutLeave(refNo, "D", service, timeSlotList,
					busNoListForOrigin, busNoListForDestination, terminalCode, route, dateCount, false,
					timeSlotListFix, busAbbreviationOrderListForOrgin, busAbbreviationOrderListForDestination,
					previousDatesCount, dateList);
			
			if(isTwoDay) {
				busNoList = reportService.insertTerminalDataTwoDayWithoutLeave(refNo, "D", service, timeSlotList,
						busNoListForOrigin, busNoListForDestination, terminalCode, route, dateCount, false,
						timeSlotListFix, busAbbreviationOrderListForOrgin, busAbbreviationOrderListForDestination,
						previousDatesCount, dateList);
			}
			
		}

	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public List<String> getRouteList() {
		return routeList;
	}

	public void setRouteList(List<String> routeList) {
		this.routeList = routeList;
	}

	public List<LogSheetMaintenanceDTO> getServiceList() {
		return serviceList;
	}

	public void setServiceList(List<LogSheetMaintenanceDTO> serviceList) {
		this.serviceList = serviceList;
	}

	public ReportService getReportService() {
		return reportService;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public CombineTimeTableGenerateService getCombineTimeTableGenerateService() {
		return combineTimeTableGenerateService;
	}

	public void setCombineTimeTableGenerateService(CombineTimeTableGenerateService combineTimeTableGenerateService) {
		this.combineTimeTableGenerateService = combineTimeTableGenerateService;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
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

	public List<String> getDepartureList() {
		return departureList;
	}

	public void setDepartureList(List<String> departureList) {
		this.departureList = departureList;
	}

	public List<String> getTimeSlotList() {
		return timeSlotList;
	}

	public void setTimeSlotList(List<String> timeSlotList) {
		this.timeSlotList = timeSlotList;
	}

	public String getTripType() {
		return tripType;
	}

	public void setTripType(String tripType) {
		this.tripType = tripType;
	}

	public String getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}

	public List<String> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<String> groupList) {
		this.groupList = groupList;
	}

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public List<String> getRefNoList() {
		return refNoList;
	}

	public void setRefNoList(List<String> refNoList) {
		this.refNoList = refNoList;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getServiceTypeDes() {
		return serviceTypeDes;
	}

	public void setServiceTypeDes(String serviceTypeDes) {
		this.serviceTypeDes = serviceTypeDes;
	}

	public boolean isControlSheetGet() {
		return isControlSheetGet;
	}

	public void setControlSheetGet(boolean isControlSheetGet) {
		this.isControlSheetGet = isControlSheetGet;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getTravelTime() {
		return travelTime;
	}

	public void setTravelTime(String travelTime) {
		this.travelTime = travelTime;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public String getMon() {
		return mon;
	}

	public void setMon(String mon) {
		this.mon = mon;
	}

	public List<String> getGetServiceType() {
		return getServiceType;
	}

	public void setGetServiceType(List<String> getServiceType) {
		this.getServiceType = getServiceType;
	}

	public List<String> getGetRouteNo() {
		return getRouteNo;
	}

	public void setGetRouteNo(List<String> getRouteNo) {
		this.getRouteNo = getRouteNo;
	}

	public String getDepSide() {
		return depSide;

	}

	public void setDepSide(String depSide) {
		this.depSide = depSide;
	}

	public String getStartMon() {
		return startMon;
	}

	public void setStartMon(String startMon) {
		this.startMon = startMon;
	}

	public List<String> getEndTimeSlotList() {
		return endTimeSlotList;
	}

	public void setEndTimeSlotList(List<String> endTimeSlotList) {
		this.endTimeSlotList = endTimeSlotList;
	}

	public String getRptType() {
		return rptType;
	}

	public void setRptType(String rptType) {
		this.rptType = rptType;
	}

	public String getStartMonth() {
		return startMonth;
	}

	public void setStartMonth(String startMonth) {
		this.startMonth = startMonth;
	}

	public String getLastMonth() {
		return lastMonth;
	}

	public void setLastMonth(String lastMonth) {
		this.lastMonth = lastMonth;
	}

	public List<String> getStartMonthList() {
		return startMonthList;
	}

	public void setStartMonthList(List<String> startMonthList) {
		this.startMonthList = startMonthList;
	}

	public List<String> getEndMonthList() {
		return endMonthList;
	}

	public void setEndMonthList(List<String> endMonthList) {
		this.endMonthList = endMonthList;
	}

	public String getDateRange() {
		return dateRange;
	}

	public void setDateRange(String dateRange) {
		this.dateRange = dateRange;
	}

	public List<String> getStelectMonthList() {
		return stelectMonthList;
	}

	public void setStelectMonthList(List<String> stelectMonthList) {
		this.stelectMonthList = stelectMonthList;
	}

	public List<CombinePanelGenaratorDTO> getFixBus() {
		return fixBus;
	}

	public void setFixBus(List<CombinePanelGenaratorDTO> fixBus) {
		this.fixBus = fixBus;
	}

	public TimeTableService getTimeTableService() {
		return timeTableService;
	}

	public void setTimeTableService(TimeTableService timeTableService) {
		this.timeTableService = timeTableService;
	}

	public Map<String, List> getBusNoList() {
		return busNoList;
	}

	public void setBusNoList(Map<String, List> busNoList) {
		this.busNoList = busNoList;
	}

	public Date getPrintedDate() {
		return printedDate;
	}

	public void setPrintedDate(Date printedDate) {
		this.printedDate = printedDate;
	}

	public int getMonthCount() {
		return monthCount;
	}

	public void setMonthCount(int monthCount) {
		this.monthCount = monthCount;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public List<TimeTableDTO> getRotationList() {
		return rotationList;
	}

	public void setRotationList(List<TimeTableDTO> rotationList) {
		this.rotationList = rotationList;
	}

	public List<Column> getGeneratedColumns() {
		return generatedColumns;
	}

	public void setGeneratedColumns(List<Column> generatedColumns) {
		this.generatedColumns = generatedColumns;
	}

	public List<String> getTimeSlotListFix() {
		return timeSlotListFix;
	}

	public void setTimeSlotListFix(List<String> timeSlotListFix) {
		this.timeSlotListFix = timeSlotListFix;
	}

	public boolean isMoreThanThirty() {
		return isMoreThanThirty;
	}

	public void setMoreThanThirty(boolean isMoreThanThirty) {
		this.isMoreThanThirty = isMoreThanThirty;
	}

	public java.util.Date getDate() {
		return date;
	}

	public void setDate(java.util.Date date) {
		this.date = date;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public int getMonthDays() {
		return monthDays;
	}

	public void setMonthDays(int monthDays) {
		this.monthDays = monthDays;
	}

	public StreamedContent getRotationSheet() {
		return rotationSheet;
	}

	public void setRotationSheet(StreamedContent rotationSheet) {
		this.rotationSheet = rotationSheet;
	}

	public boolean isTwoDay() {
		return isTwoDay;
	}

	public void setTwoDay(boolean isTwoDay) {
		this.isTwoDay = isTwoDay;
	}

	public boolean isRenderLeave() {
		return renderLeave;
	}

	public void setRenderLeave(boolean renderLeave) {
		this.renderLeave = renderLeave;
	}

	public RouteScheduleService getRouteScheduleService() {
		return routeScheduleService;
	}

	public void setRouteScheduleService(RouteScheduleService routeScheduleService) {
		this.routeScheduleService = routeScheduleService;
	}

	public List<Integer> getDateList() {
		return dateList;
	}

	public void setDateList(List<Integer> dateList) {
		this.dateList = dateList;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	public String getReportRefNo() {
		return reportRefNo;
	}

	public void setReportRefNo(String reportRefNo) {
		this.reportRefNo = reportRefNo;
	}

	
}
