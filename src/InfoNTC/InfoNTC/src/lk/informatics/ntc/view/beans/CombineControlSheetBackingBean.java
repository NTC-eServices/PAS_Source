package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.context.RequestContext;
import org.primefaces.json.JSONArray;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.CombineControlSheetDTO;
import lk.informatics.ntc.model.dto.CombineControlSheetDetailsDTO;
import lk.informatics.ntc.model.dto.CombineControlSheetMasterDTO;
import lk.informatics.ntc.model.dto.RouteCreationDTO;
import lk.informatics.ntc.model.dto.RouteScheduleDetailsDTO;
import lk.informatics.ntc.model.dto.RouteScheduleMasterDTO;
import lk.informatics.ntc.model.dto.RouteScheduleMidPointDTO;
import lk.informatics.ntc.model.dto.ServiceTypeDTO;
import lk.informatics.ntc.model.dto.StationDetailsDTO;
import lk.informatics.ntc.model.dto.TimeTableDTO;
import lk.informatics.ntc.model.service.CombineControlSheetService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ViewScoped
@ManagedBean(name = "combineControlSheetBackingBean")
public class CombineControlSheetBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	private CombineControlSheetService combineControlSheetService;
	private String sucessMsg;
	private String errorMsg;
	private String alertMSG;

	private List<RouteCreationDTO> routeList;
	private List<ServiceTypeDTO> serviceTypeList;
	private List<StationDetailsDTO> stationList;

	private CombineControlSheetDTO combineControlSheetDTO = new CombineControlSheetDTO();
	private List<CombineControlSheetDTO> combineRouteList = new ArrayList<CombineControlSheetDTO>();

	private String routeInfo;
	private StationDetailsDTO selectedMidPoint;

	List<CombineControlSheetMasterDTO> masterSheetList = new ArrayList<CombineControlSheetMasterDTO>();

	List<RouteScheduleMasterDTO> scheduleList = new ArrayList<RouteScheduleMasterDTO>();

	private StreamedContent combineControlSheet;

	private boolean enableDownload = false;

	public CombineControlSheetBackingBean() {

		combineControlSheetService = (CombineControlSheetService) SpringApplicationContex
				.getBean("combineControlSheetService");
		routeList = combineControlSheetService.getAllActiveRoutes();
		serviceTypeList = combineControlSheetService.getAllServiceStypes();
		stationList = new ArrayList<StationDetailsDTO>();
		selectedMidPoint = new StationDetailsDTO();

	}

	public void addAction() {

		if (combineControlSheetDTO.getRouteNo() == null
				|| combineControlSheetDTO.getRouteNo().trim().equalsIgnoreCase("")) {

			setErrorMsg("Route No. is empty");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		} else if (combineControlSheetDTO.getServiceType() == null
				|| combineControlSheetDTO.getServiceType().trim().equalsIgnoreCase("")) {

			setErrorMsg("Service Type is empty.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		} else if (combineControlSheetDTO.getGroupNo() == null
				|| combineControlSheetDTO.getGroupNo().trim().equalsIgnoreCase("")) {

			setErrorMsg("Group No. is empty");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		} else if (combineControlSheetDTO.getSide() == null
				|| combineControlSheetDTO.getSide().trim().equalsIgnoreCase("")) {

			setErrorMsg("Difine the Side is empty.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		} else {

			for (ServiceTypeDTO serviceTypeDTO : serviceTypeList) {
				if (combineControlSheetDTO.getServiceType().equals(serviceTypeDTO.getCode())) {
					combineControlSheetDTO.setServiceTypeDes(serviceTypeDTO.getDescription());
				}
			}

			if (combineRouteList != null && combineRouteList.isEmpty()) {

				if (combineControlSheetDTO.getMidPointCode() != null
						&& !combineControlSheetDTO.getMidPointCode().trim().equalsIgnoreCase("")) {
					combineRouteList.add(combineControlSheetDTO);
					combineControlSheetDTO = new CombineControlSheetDTO();
					stationList = new ArrayList<StationDetailsDTO>();
					routeInfo = null;
				} else {
					setErrorMsg("Mid point is empty.");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			} else if (combineRouteList != null && !combineRouteList.isEmpty()) {

				if (checkMidPointAvailable(combineRouteList.get(0).getMidPointCode())) {

					if (!checkDuplicateRoutes()) {
						combineRouteList.add(combineControlSheetDTO);
						combineControlSheetDTO = new CombineControlSheetDTO();
						stationList = new ArrayList<StationDetailsDTO>();
						routeInfo = null;
					} else {
						setErrorMsg("Already found similar record.");
						RequestContext.getCurrentInstance().update("errorMSG");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}

				} else {
					String midPointLowerCase = selectedMidPoint.getStationNameEn().toLowerCase();
					setErrorMsg("Can not find " + midPointLowerCase.substring(0, 1).toUpperCase()
							+ midPointLowerCase.substring(1) + " as mid point for selected route and service type.");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			}

			enableDownload = false;

		}

	}

	public boolean checkMidPointAvailable(String selectedMidPointCode) {

		if (stationList != null && !stationList.isEmpty()) {
			for (StationDetailsDTO midPoint : stationList) {
				if (selectedMidPointCode.equals(midPoint.getStationCode())) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean checkDuplicateRoutes() {
		for (CombineControlSheetDTO ccs : combineRouteList) {
			if (combineControlSheetDTO.getRouteNo().equals(ccs.getRouteNo())
					&& combineControlSheetDTO.getServiceType().equals(ccs.getServiceType())
					&& combineControlSheetDTO.getGroupNo().equals(ccs.getGroupNo())
					&& combineControlSheetDTO.getSide().equals(ccs.getSide())) {
				return true;
			}
		}
		return false;
	}

	public void clearAction() {
		combineControlSheetDTO = new CombineControlSheetDTO();
		combineRouteList = new ArrayList<CombineControlSheetDTO>();
		routeInfo = null;
		selectedMidPoint = new StationDetailsDTO();
		scheduleList = new ArrayList<RouteScheduleMasterDTO>();
		enableDownload = false;
	}

	public void generateControlSheetAction() {

		if (combineControlSheetDTO.getStartDate() == null) {

			setErrorMsg("Start date is empty.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		} else if (combineControlSheetDTO.getEndDate() == null) {

			setErrorMsg("End date is empty.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		} else {

			if (combineRouteList != null && !combineRouteList.isEmpty()) {

				if (selectedMidPoint.getStationCode() != null
						&& !selectedMidPoint.getStationCode().trim().equalsIgnoreCase("")) {

					LocalDate startDate = combineControlSheetDTO.getStartDate().toInstant()
							.atZone(ZoneId.systemDefault()).toLocalDate();
					LocalDate endDate = combineControlSheetDTO.getEndDate().toInstant().atZone(ZoneId.systemDefault())
							.toLocalDate();
					boolean emptyScheduleFound = false;
					scheduleList = new ArrayList<RouteScheduleMasterDTO>();
					for (CombineControlSheetDTO ccsDTO : combineRouteList) {

						String routeNo = ccsDTO.getRouteNo();
						String serviceType = ccsDTO.getServiceType();
						String groupNo = ccsDTO.getGroupNo();
						String side = ccsDTO.getSide();

						RouteScheduleMasterDTO masterDTO = combineControlSheetService.getRouteScheduleInfo(routeNo,
								serviceType, groupNo, side, startDate, endDate);

						if (masterDTO != null) {
							scheduleList.add(masterDTO);
						} else {
							String sideStr = side.equals("O") ? "Origin to destination" : "Destination to origin";
							String serviceTypeStr = ccsDTO.getServiceTypeDes().toLowerCase();
							setErrorMsg("No route schedule found for selected period. Route no: " + routeNo
									+ ", ServiceType: " + serviceTypeStr.substring(0, 1).toUpperCase()
									+ serviceTypeStr.substring(1) + ", Group no: " + groupNo + ", Side: " + sideStr
									+ ".");
							RequestContext.getCurrentInstance().update("errorMSG");
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
							emptyScheduleFound = true;
							break;
						}
					}

					if (emptyScheduleFound == false) {
						enableDownload = true;

						setSucessMsg("Route schedule details found. Please download the report.");
						RequestContext.getCurrentInstance().update("successMSG");
						RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
					}
				} else {
					setErrorMsg("Mid point is empty.");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			} else {
				setErrorMsg("Atleast one route is required to generate report.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		}
	}

	public void buildCombineControlSheet() {

		masterSheetList = new ArrayList<CombineControlSheetMasterDTO>();
		LocalDate startDate = combineControlSheetDTO.getStartDate().toInstant().atZone(ZoneId.systemDefault())
				.toLocalDate();
		LocalDate endDate = combineControlSheetDTO.getEndDate().toInstant().atZone(ZoneId.systemDefault())
				.toLocalDate();

		LocalDate currentDate = startDate;
		while (!currentDate.isAfter(endDate)) {
			masterSheetList.add(new CombineControlSheetMasterDTO(currentDate));
			currentDate = currentDate.plusDays(1);
		}



		for (RouteScheduleMasterDTO routeScheduleMasterDTO : scheduleList) {

			List<RouteScheduleDetailsDTO> scheduleTripList = combineControlSheetService.getRouteScheduleTripDetailsByRef(routeScheduleMasterDTO.getRsRouteRefNo());
			List<RouteScheduleDetailsDTO> scheduleLeaveTripList = combineControlSheetService.getRouteScheduleLeaveTripDetailsByRef(routeScheduleMasterDTO.getRsRouteRefNo());
			List<RouteScheduleMidPointDTO> routeMidPointsForPrivate = combineControlSheetService.getRouteMidPointsForNoneSLTB(routeScheduleMasterDTO.getRsRouteNo(), routeScheduleMasterDTO.getRsServiceType(),routeScheduleMasterDTO.getRsGroupNO(), routeScheduleMasterDTO.getRsSide(), selectedMidPoint);
			List<RouteScheduleMidPointDTO> routeMidPointsForSLTB = combineControlSheetService.getRouteMidPointsForSLTB(routeScheduleMasterDTO.getRsRouteNo(), routeScheduleMasterDTO.getRsServiceType(),routeScheduleMasterDTO.getRsGroupNO(), routeScheduleMasterDTO.getRsSide(), selectedMidPoint);
				

			LocalDate routeScheduleStartDate = ddMMyyyyTOLocalDate(routeScheduleMasterDTO.getRsStartDate());
			LocalDate routeScheduleEndDate = ddMMyyyyTOLocalDate(routeScheduleMasterDTO.getRsEndDate());


			if (scheduleTripList != null && !scheduleTripList.isEmpty() && routeMidPointsForPrivate != null && !routeMidPointsForPrivate.isEmpty()) {
					
				for (CombineControlSheetMasterDTO masterDTO : masterSheetList) {

					LocalDate sheetDate = masterDTO.getDate();

					for (RouteScheduleDetailsDTO tripDTO : scheduleTripList) {

						int dayNo = Integer.valueOf(tripDTO.getDayNo()) - 1;

						if (sheetDate.equals(routeScheduleStartDate.plusDays(dayNo)) && (sheetDate.isEqual(routeScheduleStartDate) || sheetDate.isAfter(routeScheduleStartDate))
								&& (sheetDate.isEqual(routeScheduleEndDate) || sheetDate.isBefore(routeScheduleEndDate))) {

							CombineControlSheetDetailsDTO detailsDTO = new CombineControlSheetDetailsDTO();
							int midPointId = 1;
							for (RouteScheduleMidPointDTO midPointDTO : routeMidPointsForPrivate) {

								if (selectedMidPoint.getStationType().equals("O")) {

									if (selectedMidPoint.getStationNameEn().equals(midPointDTO.getMpOrigin()) && tripDTO.getTripId().equals(String.valueOf(midPointId))) {

										detailsDTO.setBusNo(tripDTO.getBusNo());
										detailsDTO.setTripId(tripDTO.getTripId());
										detailsDTO.setRouteNo(routeScheduleMasterDTO.getRsRouteNo());
										detailsDTO.setServiceType(routeScheduleMasterDTO.getRsServiceType());
										detailsDTO.setTime(midPointDTO.getMpStartTime());

										LocalTime time = LocalTime.parse(midPointDTO.getMpStartTime(),DateTimeFormatter.ofPattern("HH:mm"));
										LocalDateTime currentDateTime = LocalDateTime.now();
										LocalDateTime localDateTime = currentDateTime.with(time);
										detailsDTO.setLocalTime(localDateTime);

										break;
									}

								} else if (selectedMidPoint.getStationType().equals("D")) {

									if (selectedMidPoint.getStationNameEn().equals(midPointDTO.getMpDestination()) && tripDTO.getTripId().equals(String.valueOf(midPointId))) {
										detailsDTO.setBusNo(tripDTO.getBusNo());
										detailsDTO.setTripId(tripDTO.getTripId());
										detailsDTO.setRouteNo(routeScheduleMasterDTO.getRsRouteNo());
										detailsDTO.setServiceType(routeScheduleMasterDTO.getRsServiceType());
										detailsDTO.setTime(midPointDTO.getMpEndTime());

										LocalTime time = LocalTime.parse(midPointDTO.getMpEndTime(),DateTimeFormatter.ofPattern("HH:mm"));
										LocalDateTime currentDateTime = LocalDateTime.now();
										LocalDateTime localDateTime = currentDateTime.with(time);
										detailsDTO.setLocalTime(localDateTime);

										break;
									}
								} else {
									if (selectedMidPoint.getStationNameEn().equals(midPointDTO.getMpMidpoint()) && tripDTO.getTripId().equals(String.valueOf(midPointId))) {
										detailsDTO.setBusNo(tripDTO.getBusNo());
										detailsDTO.setTripId(tripDTO.getTripId());
										detailsDTO.setRouteNo(routeScheduleMasterDTO.getRsRouteNo());
										detailsDTO.setServiceType(routeScheduleMasterDTO.getRsServiceType());
										detailsDTO.setTime(midPointDTO.getMpMidpointTime());

										LocalTime time = LocalTime.parse(midPointDTO.getMpMidpointTime(),DateTimeFormatter.ofPattern("HH:mm"));
										LocalDateTime currentDateTime = LocalDateTime.now();
										LocalDateTime localDateTime = currentDateTime.with(time);
										detailsDTO.setLocalTime(localDateTime);
										break;
									}
								}
								midPointId++;
							}
							masterDTO.getDetailsList().add(detailsDTO);
						}
					}
				}
			
				for (CombineControlSheetMasterDTO masterDTO : masterSheetList) {
			if (routeMidPointsForSLTB != null && !routeMidPointsForSLTB.isEmpty()) {
					for (RouteScheduleMidPointDTO SLTB : routeMidPointsForSLTB) {			
						
						if (selectedMidPoint.getStationType().equals("O")) {
							
							if (selectedMidPoint.getStationNameEn().equals(SLTB.getMpOrigin())) {
								CombineControlSheetDetailsDTO ctbDetailsDTO = new CombineControlSheetDetailsDTO();
								ctbDetailsDTO.setBusNo(SLTB.getMpBusNo());
								ctbDetailsDTO.setTripId("F");
								ctbDetailsDTO.setRouteNo(routeScheduleMasterDTO.getRsRouteNo());
								ctbDetailsDTO.setServiceType(routeScheduleMasterDTO.getRsServiceType());
								ctbDetailsDTO.setTime(SLTB.getMpStartTime());							
								LocalTime time = LocalTime.parse(SLTB.getMpStartTime(),DateTimeFormatter.ofPattern("HH:mm"));
								LocalDateTime currentDateTime = LocalDateTime.now();
								LocalDateTime localDateTime = currentDateTime.with(time);
								ctbDetailsDTO.setLocalTime(localDateTime);
								masterDTO.getDetailsList().add(ctbDetailsDTO);
							
							}
						}else if (selectedMidPoint.getStationType().equals("D")) {
							if (selectedMidPoint.getStationNameEn().equals(SLTB.getMpDestination())) {
								CombineControlSheetDetailsDTO ctbDetailsDTO = new CombineControlSheetDetailsDTO();
								ctbDetailsDTO.setBusNo(SLTB.getMpBusNo());
								ctbDetailsDTO.setTripId("F");
								ctbDetailsDTO.setRouteNo(routeScheduleMasterDTO.getRsRouteNo());
								ctbDetailsDTO.setServiceType(routeScheduleMasterDTO.getRsServiceType());
								ctbDetailsDTO.setTime(SLTB.getMpEndTime());				
								LocalTime time = LocalTime.parse(SLTB.getMpEndTime(),DateTimeFormatter.ofPattern("HH:mm"));
								LocalDateTime currentDateTime = LocalDateTime.now();
								LocalDateTime localDateTime = currentDateTime.with(time);
								ctbDetailsDTO.setLocalTime(localDateTime);
								masterDTO.getDetailsList().add(ctbDetailsDTO);
							
							}
						}else {
							if (selectedMidPoint.getStationNameEn().equals(SLTB.getMpMidpoint())) {
								CombineControlSheetDetailsDTO ctbDetailsDTO = new CombineControlSheetDetailsDTO();
								ctbDetailsDTO.setBusNo(SLTB.getMpBusNo());
								ctbDetailsDTO.setTripId("F");
								ctbDetailsDTO.setRouteNo(routeScheduleMasterDTO.getRsRouteNo());
								ctbDetailsDTO.setServiceType(routeScheduleMasterDTO.getRsServiceType());
								ctbDetailsDTO.setTime(SLTB.getMpMidpointTime());					
								LocalTime time = LocalTime.parse(SLTB.getMpMidpointTime(),DateTimeFormatter.ofPattern("HH:mm"));
								LocalDateTime currentDateTime = LocalDateTime.now();
								LocalDateTime localDateTime = currentDateTime.with(time);
								ctbDetailsDTO.setLocalTime(localDateTime);
								masterDTO.getDetailsList().add(ctbDetailsDTO);
							}
						}
					}
				}
				}

				for (CombineControlSheetMasterDTO masterDTO : masterSheetList) {

					LocalDate sheetDate = masterDTO.getDate();
				if (scheduleLeaveTripList != null && !scheduleLeaveTripList.isEmpty()) {
					for (RouteScheduleDetailsDTO leaveDTO : scheduleLeaveTripList) {
						int leaveDayNo = Integer.valueOf(leaveDTO.getDayNo()) - 1;

						if (sheetDate.equals(routeScheduleStartDate.plusDays(leaveDayNo))&& (sheetDate.isEqual(routeScheduleStartDate)|| sheetDate.isAfter(routeScheduleStartDate))
								&& (sheetDate.isEqual(routeScheduleEndDate)|| sheetDate.isBefore(routeScheduleEndDate))) {

							CombineControlSheetDetailsDTO leaveDetailsDTO = new CombineControlSheetDetailsDTO();
							leaveDetailsDTO.setBusNo(leaveDTO.getBusNo());
							leaveDetailsDTO.setTripId(leaveDTO.getTripId());
							leaveDetailsDTO.setRouteNo(routeScheduleMasterDTO.getRsRouteNo());
							leaveDetailsDTO.setServiceType(routeScheduleMasterDTO.getRsServiceType());
							leaveDetailsDTO.setTime(null);
							masterDTO.getLeaveDetailsList().add(leaveDetailsDTO);
						}
					}
				}
				}
			}
		}


		JSONArray jsonArray = new JSONArray(masterSheetList);
		String filePath = "D:/example.txt";
		try {
			Files.write(Paths.get(filePath), jsonArray.toString().getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		generateRotationExcel();
	}

	public void addSLTBToMasterList(List<RouteScheduleMidPointDTO> sltbMidPointSchedule, String routeNo,
			String serviceType) {

		for (CombineControlSheetMasterDTO masterDTO : masterSheetList) {
			
	
			int detailsListSize = masterDTO.getDetailsList().size();

			for (int i = 0; i < sltbMidPointSchedule.size(); i++) {

				if (sltbMidPointSchedule.get(i).getMpBusNo().startsWith("SLTB")) {

					CombineControlSheetDetailsDTO sltbDetailsDTO = new CombineControlSheetDetailsDTO();
					sltbDetailsDTO.setBusNo(sltbMidPointSchedule.get(i).getMpBusNo());
					sltbDetailsDTO.setTripId(String.valueOf(i));
					sltbDetailsDTO.setRouteNo(routeNo);
					sltbDetailsDTO.setServiceType(serviceType);
					sltbDetailsDTO.setTime(sltbMidPointSchedule.get(i).getMpMidpointTime());

					LocalTime time = LocalTime.parse(sltbMidPointSchedule.get(i).getMpMidpointTime(),DateTimeFormatter.ofPattern("HH:mm"));
					LocalDateTime currentDateTime = LocalDateTime.now();
					LocalDateTime localDateTime = currentDateTime.with(time);
					sltbDetailsDTO.setLocalTime(localDateTime);

				
					masterDTO.getDetailsList().add(i, sltbDetailsDTO);
				}

			}
		}
	}

	public void generateRotationExcel() {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Combine Control Sheet Sheet");
		sheet.getPrintSetup().setLandscape(true);

		Row row0 = sheet.createRow(0);
		row0.setHeight((short) 1200);

		JSONArray s = new JSONArray(masterSheetList);
		System.out.println(s.toString());

		try {

			InputStream nlStream = TimeTableReportsBackingBean.class
					.getResourceAsStream("..//reports//nationalLogo.png");
			InputStream ntcStream = TimeTableReportsBackingBean.class.getResourceAsStream("..//reports//ntclogo.jpg");
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
			tableHeaderStyle.setBorderTop(BorderStyle.THIN);
			tableHeaderStyle.setBorderBottom(BorderStyle.THIN);
			tableHeaderStyle.setBorderLeft(BorderStyle.THIN);
			tableHeaderStyle.setBorderRight(BorderStyle.THIN);
			Font fontTableHeading = workbook.createFont();
			fontTableHeading.setBold(true);
			tableHeaderStyle.setFont(fontTableHeading);

			String mainHeading = "NATIONAL TRANSPORT COMMISSION\nCombine Control Sheet";
			Cell headerCell = row0.createCell(0);

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

			Row row2 = sheet.createRow(2);
			Cell row2Cell1 = row2.createCell(0);
			row2Cell1.setCellValue("Mid Point: ");

			Cell row2Cell3 = row2.createCell(3);
			row2Cell3.setCellValue(selectedMidPoint.getStationNameEn());

			Row row3 = sheet.createRow(3);
			Cell row3Cell1 = row3.createCell(0);
			row3Cell1.setCellValue("Date Ranage: ");

			Cell row3Cell3 = row3.createCell(3);
			String dateRange = dateFormat.format(combineControlSheetDTO.getStartDate()) + " - "
					+ dateFormat.format(combineControlSheetDTO.getEndDate());
			row3Cell3.setCellValue(dateRange);

			// generate table data

			String[] subheading = { "Bus No", "Route No", "Time", "Remark" };

			int dynamicStartRowNumber = 5;
			int dynamicRowNumber = 5;
			int dynamicMaxRowNumber = 5;
			int dynamicCellNumber = 0;

			int columnBreakIndex = 0;

			for (int i = 0; i < masterSheetList.size(); i++) {

				dynamicRowNumber = dynamicStartRowNumber;
				columnBreakIndex++;

				Row headerRow = sheet.getRow(dynamicRowNumber);
				if (headerRow == null) {
					headerRow = sheet.createRow(dynamicRowNumber);
				}
				for (int j = 0; j < 4; j++) {
					Cell tableHeaderCell = headerRow.createCell(dynamicCellNumber + j);
					tableHeaderCell.setCellValue(masterSheetList.get(i).getDate().format(dateFormatter));
					tableHeaderCell.setCellStyle(tableHeaderStyle);
				}

				Row subHeaderRow = sheet.getRow(dynamicRowNumber + 1);
				if (subHeaderRow == null) {
					subHeaderRow = sheet.createRow(dynamicRowNumber + 1);
				}
				for (int j = 0; j < subheading.length; j++) {
					Cell tableSubHeaderCell = subHeaderRow.createCell(dynamicCellNumber + j);
					tableSubHeaderCell.setCellValue(subheading[j]);
					setBorder(workbook, tableSubHeaderCell);
				}

				// Merge table heading row cells
				sheet.addMergedRegion(new CellRangeAddress(dynamicRowNumber, dynamicRowNumber, dynamicCellNumber,
						dynamicCellNumber + 3));

				if (masterSheetList.get(i).getDetailsList() != null
						&& !masterSheetList.get(i).getDetailsList().isEmpty()) {

					for (int j = 0; j < masterSheetList.get(i).orderListByLocalTime().size(); j++) {

						Row row = sheet.getRow(dynamicRowNumber + 2);
						if (row == null) {
							row = sheet.createRow(dynamicRowNumber + 2);
						}
						Cell cell1 = row.createCell(dynamicCellNumber);
						cell1.setCellValue(masterSheetList.get(i).orderListByLocalTime().get(j).getBusNo());
						setBorder(workbook, cell1);

						Cell cell2 = row.createCell(dynamicCellNumber + 1);
						cell2.setCellValue(masterSheetList.get(i).orderListByLocalTime().get(j).getRouteNo());
						setBorder(workbook, cell2);

						Cell cell3 = row.createCell(dynamicCellNumber + 2);
						cell3.setCellValue(masterSheetList.get(i).orderListByLocalTime().get(j).getTime());
						setBorder(workbook, cell3);

						Cell cell4 = row.createCell(dynamicCellNumber + 3);
						cell4.setCellValue("");
						setBorder(workbook, cell4);

						dynamicRowNumber++;

						// get maximum row for a block
						if (dynamicRowNumber > dynamicMaxRowNumber) {
							dynamicMaxRowNumber = dynamicRowNumber;
						}
					}
				}

				if (masterSheetList.get(i).getLeaveDetailsList() != null
						&& !masterSheetList.get(i).getLeaveDetailsList().isEmpty()) {

					Row leaveRow = sheet.getRow(dynamicRowNumber + 2);
					if (leaveRow == null) {
						leaveRow = sheet.createRow(dynamicRowNumber + 2);
					}
					Cell leaveCell = leaveRow.createCell(dynamicCellNumber);
					leaveCell.setCellValue("Leave Buses");
					setBorder(workbook, leaveCell);

					sheet.addMergedRegion(new CellRangeAddress(dynamicRowNumber + 2, dynamicRowNumber + 2,
							dynamicCellNumber, dynamicCellNumber + 3));

					for (int j = 0; j < masterSheetList.get(i).getLeaveDetailsList().size(); j++) {

						Row row = sheet.getRow(dynamicRowNumber + 3);
						if (row == null) {
							row = sheet.createRow(dynamicRowNumber + 3);
						}
						Cell cell1 = row.createCell(dynamicCellNumber);
						cell1.setCellValue(masterSheetList.get(i).getLeaveDetailsList().get(j).getBusNo());
						setBorder(workbook, cell1);

						Cell cell2 = row.createCell(dynamicCellNumber + 1);
						cell2.setCellValue(masterSheetList.get(i).getLeaveDetailsList().get(j).getRouteNo());
						setBorder(workbook, cell2);

						Cell cell3 = row.createCell(dynamicCellNumber + 2);
						cell3.setCellValue("");
						setBorder(workbook, cell3);

						Cell cell4 = row.createCell(dynamicCellNumber + 3);
						cell4.setCellValue("");
						setBorder(workbook, cell4);

						dynamicRowNumber++;

						// get maximum row for a block
						if (dynamicRowNumber > dynamicMaxRowNumber) {
							dynamicMaxRowNumber = dynamicRowNumber;
						}
					}
				}

				dynamicCellNumber = dynamicCellNumber + 4;

				if (columnBreakIndex == 3) { // break table after every three days
					dynamicCellNumber = 0;
					dynamicStartRowNumber = dynamicMaxRowNumber + 4; // increase this to add more empty row after 4
																		// block
					columnBreakIndex = 0;
				}

			}

			// set logo
			ntcLogoAncor.setCol1(11);
			ntcLogoAncor.setCol2(12);
			ntcLogoAncor.setRow1(0);
			ntcLogoAncor.setRow2(1);

			nationalLogoAncor.setCol1(0);
			nationalLogoAncor.setCol2(1);
			nationalLogoAncor.setRow1(0);
			nationalLogoAncor.setRow2(1);

			drawing.createPicture(nationalLogoAncor, nationalLogo);
			drawing.createPicture(ntcLogoAncor, ntcLogo);

			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 11));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 11));

			sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 2));
			sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 2));

			sheet.addMergedRegion(new CellRangeAddress(2, 2, 3, 11));
			sheet.addMergedRegion(new CellRangeAddress(3, 3, 3, 11));

			sheet.addMergedRegion(new CellRangeAddress(4, 4, 0, 12));

			sheet.setRepeatingRows(new CellRangeAddress(0, 0, 0, sheet.getRow(0).getLastCellNum() - 1));

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			byte[] byteArray = bos.toByteArray();

			if (byteArray != null) {
				InputStream stream = new ByteArrayInputStream(byteArray);
				String date = dateFormat.format(combineControlSheetDTO.getStartDate()) + "-"
						+ dateFormat.format(combineControlSheetDTO.getEndDate());
				combineControlSheet = new DefaultStreamedContent(stream,
						"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
						"CCS" + "-" + selectedMidPoint.getStationNameEn().toUpperCase() + ".xlsx" + ".xlsx");
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

	private static void setBorder(Workbook workbook, Cell cell) {
		CellStyle style = workbook.createCellStyle();
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		cell.setCellStyle(style);
	}

	private static void setBorderButtom(Workbook workbook, Cell cell) {
		CellStyle style = workbook.createCellStyle();
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderBottom(BorderStyle.THIN);
		cell.setCellStyle(style);
	}

	private LocalDate ddMMyyyyTOLocalDate(String dateString) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return LocalDate.parse(dateString, formatter);
	}

	private List<String> getDistinctRouteRefNos(List<RouteScheduleDetailsDTO> scheduleList) {

		Set<String> uniquebusNo = new HashSet<>();
		List<String> distinctBusNo = new ArrayList<>();

		for (RouteScheduleDetailsDTO routeSchedule : scheduleList) {
			String busNo = routeSchedule.getBusNo();

			if (uniquebusNo.add(busNo)) {
				distinctBusNo.add(busNo);
			}
		}
		return distinctBusNo;
	}

	public void routeChange() {

		if (combineControlSheetDTO.getRouteNo() != null) {

			if (routeList != null && !routeList.isEmpty()) {
				for (RouteCreationDTO routeCreationDTO : routeList) {
					if (combineControlSheetDTO.getRouteNo().equals(routeCreationDTO.getRouteNo())) {

						if (combineControlSheetDTO.getSide() == null) {

							combineControlSheetDTO.setStartFrom(routeCreationDTO.getStartFrom());
							combineControlSheetDTO.setEndAt(routeCreationDTO.getEndAt());
							routeInfo = routeCreationDTO.getStartFrom() + " to " + routeCreationDTO.getEndAt();

						} else if (combineControlSheetDTO.getSide().equals("O")) {

							combineControlSheetDTO.setStartFrom(routeCreationDTO.getStartFrom());
							combineControlSheetDTO.setEndAt(routeCreationDTO.getEndAt());
							routeInfo = routeCreationDTO.getStartFrom() + " to " + routeCreationDTO.getEndAt();

						} else if (combineControlSheetDTO.getSide().equals("D")) {

							combineControlSheetDTO.setStartFrom(routeCreationDTO.getEndAt());
							combineControlSheetDTO.setEndAt(routeCreationDTO.getStartFrom());
							routeInfo = routeCreationDTO.getEndAt() + " to " + routeCreationDTO.getStartFrom();
						}

						break;
					}
				}
			}

			if (combineControlSheetDTO.getRouteNo() != null && combineControlSheetDTO.getServiceType() != null) {

				stationList = new ArrayList<StationDetailsDTO>();
				stationList = combineControlSheetService.getStationsByRouteAndServiceType(
						combineControlSheetDTO.getRouteNo(), combineControlSheetDTO.getServiceType());
				stationList.add(new StationDetailsDTO(combineControlSheetDTO.getStartFrom(),
						combineControlSheetDTO.getStartFrom(), "O"));
				stationList.add(new StationDetailsDTO(combineControlSheetDTO.getEndAt(),
						combineControlSheetDTO.getEndAt(), "D"));

			}

		}
	}

	public void serviceTypeChange() {
		if (combineControlSheetDTO.getRouteNo() != null && combineControlSheetDTO.getServiceType() != null) {

			stationList = new ArrayList<StationDetailsDTO>();
			stationList = combineControlSheetService.getStationsByRouteAndServiceType(
					combineControlSheetDTO.getRouteNo(), combineControlSheetDTO.getServiceType());
			stationList.add(new StationDetailsDTO(combineControlSheetDTO.getStartFrom(),
					combineControlSheetDTO.getStartFrom(), "O"));
			stationList.add(
					new StationDetailsDTO(combineControlSheetDTO.getEndAt(), combineControlSheetDTO.getEndAt(), "D"));
		}
	}

	public void midPointChange() {
		if (selectedMidPoint.getStationCode() == null && combineControlSheetDTO.getMidPointCode() != null) {
			if (stationList != null && !stationList.isEmpty()) {
				for (StationDetailsDTO midDTO : stationList) {
					if (combineControlSheetDTO.getMidPointCode().equals(midDTO.getStationCode())) {
						selectedMidPoint = midDTO;
					}
				}
			}
		}
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public String getSucessMsg() {
		return sucessMsg;
	}

	public void setSucessMsg(String sucessMsg) {
		this.sucessMsg = sucessMsg;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public CombineControlSheetService getCombineControlSheetService() {
		return combineControlSheetService;
	}

	public void setCombineControlSheetService(CombineControlSheetService combineControlSheetService) {
		this.combineControlSheetService = combineControlSheetService;
	}

	public CombineControlSheetDTO getCombineControlSheetDTO() {
		return combineControlSheetDTO;
	}

	public void setCombineControlSheetDTO(CombineControlSheetDTO combineControlSheetDTO) {
		this.combineControlSheetDTO = combineControlSheetDTO;
	}

	public List<RouteCreationDTO> getRouteList() {
		return routeList;
	}

	public void setRouteList(List<RouteCreationDTO> routeList) {
		this.routeList = routeList;
	}

	public List<ServiceTypeDTO> getServiceTypeList() {
		return serviceTypeList;
	}

	public void setServiceTypeList(List<ServiceTypeDTO> serviceTypeList) {
		this.serviceTypeList = serviceTypeList;
	}

	public String getAlertMSG() {
		return alertMSG;
	}

	public void setAlertMSG(String alertMSG) {
		this.alertMSG = alertMSG;
	}

	public List<CombineControlSheetDTO> getCombineRouteList() {
		return combineRouteList;
	}

	public void setCombineRouteList(List<CombineControlSheetDTO> combineRouteList) {
		this.combineRouteList = combineRouteList;
	}

	public String getRouteInfo() {
		return routeInfo;
	}

	public void setRouteInfo(String routeInfo) {
		this.routeInfo = routeInfo;
	}

	public List<StationDetailsDTO> getStationList() {
		return stationList;
	}

	public void setStationList(List<StationDetailsDTO> stationList) {
		this.stationList = stationList;
	}

	public StationDetailsDTO getSelectedMidPoint() {
		return selectedMidPoint;
	}

	public void setSelectedMidPoint(StationDetailsDTO selectedMidPoint) {
		this.selectedMidPoint = selectedMidPoint;
	}

	public List<CombineControlSheetMasterDTO> getMasterSheetList() {
		return masterSheetList;
	}

	public void setMasterSheetList(List<CombineControlSheetMasterDTO> masterSheetList) {
		this.masterSheetList = masterSheetList;
	}

	public StreamedContent getCombineControlSheet() {
		return combineControlSheet;
	}

	public void setCombineControlSheet(StreamedContent combineControlSheet) {
		this.combineControlSheet = combineControlSheet;
	}

	public List<RouteScheduleMasterDTO> getScheduleList() {
		return scheduleList;
	}

	public void setScheduleList(List<RouteScheduleMasterDTO> scheduleList) {
		this.scheduleList = scheduleList;
	}

	public boolean isEnableDownload() {
		return enableDownload;
	}

	public void setEnableDownload(boolean enableDownload) {
		this.enableDownload = enableDownload;
	}

}
