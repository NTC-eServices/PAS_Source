package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.bean.ViewScoped;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Picture;
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
import org.primefaces.PrimeFaces;
import org.primefaces.component.column.Column;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.json.JSONArray;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.BusDetailsDTO;
import lk.informatics.ntc.model.dto.CombinePanelGenaratorDTO;
import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.RouteCreationDTO;
import lk.informatics.ntc.model.dto.RouteDTO;
import lk.informatics.ntc.model.dto.SetUpMidPointsDTO;
import lk.informatics.ntc.model.dto.TimeTableDTO;
import lk.informatics.ntc.model.service.AdminService;
import lk.informatics.ntc.model.service.CombinePanelGenaratorService;
import lk.informatics.ntc.model.service.SetUpMidPointsService;
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
import net.sf.jasperreports.engine.xml.JRXmlLoader;

@ManagedBean(name = "combinePanelGenaratorBean")
@ViewScoped
public class CombinePanelGenaratorBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private CombinePanelGenaratorService combinePanelService;
	private SetUpMidPointsService setUpMidPointsService;
	private TimeTableService timeTableService;
	private AdminService adminService;
	private List<CombinePanelGenaratorDTO> busRouteList, originDestinationList, detailsList;
	private List<SetUpMidPointsDTO> midPointList;
	private List<SetUpMidPointsDTO> midPointTimeListOd;
	private List<SetUpMidPointsDTO> midPointTimeListDo;
	private List<String> selectedMidPointTimeList;
	private List<SetUpMidPointsDTO> selectedList;
	private List<SetUpMidPointsDTO> selectedMidPointList;
	private List<SetUpMidPointsDTO> tableDataList;
	private List<SetUpMidPointsDTO> selectedDataList;
	private List<SetUpMidPointsDTO> busTimeDetailsList;
	private List<String> permitNoList;
	private List<String> busNoList;
	private List<String> timeRangeList;
	private List<BusDetailsDTO> busDetailsList;
	private CombinePanelGenaratorDTO combinePanelDTO, routeOriginDestination, dropDownDataStore;
	private SetUpMidPointsDTO setUpMidPointsDTO;
	private SetUpMidPointsDTO tableDetails;
	private CombinePanelGenaratorDTO dataDetails;
	private BusDetailsDTO busDetails;
	private String routeNo;
	private String serviceType;
	private String groupNo;
	private String defineSide;
	private String errorMessage, alertMSG, successMessage;
	private String genarateTimeSlote;
	private String midPointTable;
	private boolean visible;
	private boolean visibleDiv;
	private boolean visibleTable;
	private boolean isSearch;
	private boolean disabledOne;
	private boolean saveClicked;
	private boolean disableSelectMenu;
	private List<CommonDTO> drpdServiceTypeList;
	private List<SetUpMidPointsDTO> addedAllTimeTaken;
	private List<CombinePanelGenaratorDTO> busNoPermitNoList;
	private List<Column> generatedColumns;
	private long deviceNo;

	private UIComponent addRouteComponent;
	private StreamedContent files;

	private Map<String, String> midTimeNameMap = new HashMap<>();
	private Set<String> uniqueMidtimes = new LinkedHashSet<>();
	private List<String> combinedMidtimes = new ArrayList<>();
	private Set<String> uniqueStartTimes = new LinkedHashSet<>();
	private List<String> combinedStartTimes = new ArrayList<>();
	private Set<String> uniqueEndTimes = new LinkedHashSet<>();
	private List<String> combinedEndTimes = new ArrayList<>();

	private String permitNo;
	private String busNo;

	private String origin;
	private String destination;
	private String midPointName;
	private String timeRange;
	private List<CombinePanelGenaratorDTO> timesWithRoute;
	private List<CombinePanelGenaratorDTO> timesWithRouteDes;

	private LinkedHashMap<String, LinkedHashMap<String, List<String>>> timesWithSides = new LinkedHashMap<>();
	private List<String> originToDestinationTimeList;
	private List<String> destinationToOriginTimeList;
	private LinkedHashMap<String, List<String>> times = new LinkedHashMap<>();
	private LinkedHashMap<String, List<String>> timesDes = new LinkedHashMap<>();
	private Map<String, String> selectedTimes = new HashMap<>();
	private List<String> routeUsed = new ArrayList<>();
	private List<String> runningNoList = new ArrayList<>();
	private List<String> runningNoDetailsList = new ArrayList<>();

	private List<RouteCreationDTO> selectedRouteList = new ArrayList<RouteCreationDTO>();

	private StreamedContent excelSheet;
	private boolean disableSave;

	@PostConstruct
	public void init() {
		combinePanelService = (CombinePanelGenaratorService) SpringApplicationContex.getBean("combinePanelService");
		setUpMidPointsService = (SetUpMidPointsService) SpringApplicationContex.getBean("setUpMidPointsService");
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");
		timeTableService = (TimeTableService) SpringApplicationContex.getBean("timeTableService");
		loadValue();
	}

	public void loadValue() {
		dropDownDataStore = new CombinePanelGenaratorDTO();
		combinePanelDTO = new CombinePanelGenaratorDTO();
		busRouteList = new ArrayList<CombinePanelGenaratorDTO>();
		originDestinationList = new ArrayList<CombinePanelGenaratorDTO>();
		detailsList = new ArrayList<CombinePanelGenaratorDTO>();
		selectedList = new ArrayList<SetUpMidPointsDTO>();
		tableDataList = new ArrayList<SetUpMidPointsDTO>();
		selectedMidPointList = new ArrayList<SetUpMidPointsDTO>();
		addedAllTimeTaken = new ArrayList<SetUpMidPointsDTO>();
		selectedDataList = new ArrayList<SetUpMidPointsDTO>();
		busRouteList = combinePanelService.getRouteNoList();
		busDetailsList = new ArrayList<BusDetailsDTO>();
		drpdServiceTypeList = adminService.getServiceTypeToDropdown();
		busNoPermitNoList = new ArrayList<CombinePanelGenaratorDTO>();
		generatedColumns = new ArrayList<>();
		selectedMidPointTimeList = new ArrayList<>();
		timeRangeList = setTimeRange();
		originToDestinationTimeList = new ArrayList<>();
		destinationToOriginTimeList = new ArrayList<>();
		timesWithRoute = new ArrayList<>();
		timesWithRouteDes = new ArrayList<>();
		times = new LinkedHashMap<>();
		timesDes = new LinkedHashMap<>();
		selectedTimes = new HashMap<>();
		routeUsed = new ArrayList<>();
		uniqueStartTimes = new LinkedHashSet<>();
		combinedStartTimes = new ArrayList<>();
		uniqueEndTimes = new LinkedHashSet<>();
		combinedEndTimes = new ArrayList<>();
		permitNoList = new ArrayList<>();
		busNoList = new ArrayList<>();
		runningNoDetailsList = new ArrayList<>();

		visibleTable = false;
		visible = false;
		routeNo = null;
		serviceType = null;
		groupNo = null;
		defineSide = null;
		disableSelectMenu = false;
		origin = null;
		destination = null;
		midPointName = null;

		deviceNo = combinePanelService.getDeviceNo();
		runningNoList = new ArrayList<>();
		disableSave = false;
	}

	public void ajaxFillOriginDestination() {
		routeOriginDestination = combinePanelService.getOriginDestination(combinePanelDTO.getRouteNo());
		combinePanelDTO.setOrigin(routeOriginDestination.getOrigin());
		combinePanelDTO.setDestination(routeOriginDestination.getDestination());
		visible = true;
	}

	public void searchAction() {
		selectedList = new ArrayList<SetUpMidPointsDTO>();
		if ((combinePanelDTO.getRouteNo() == null || combinePanelDTO.getRouteNo().trim().equalsIgnoreCase(""))) {

			setErrorMessage("Please Select the Route No");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		} else {
			ajaxFillOriginDestination();

			if ((combinePanelDTO.getServiceType() == null
					|| combinePanelDTO.getServiceType().trim().equalsIgnoreCase(""))) {

				setErrorMessage("Please Select the Service Type");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			} else {

				if ((combinePanelDTO.getGroupNo() == null
						|| combinePanelDTO.getGroupNo().trim().equalsIgnoreCase(""))) {

					setErrorMessage("Please Select the Group No.");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				} else {
					if ((combinePanelDTO.getDefineSide() == null
							|| combinePanelDTO.getDefineSide().trim().equalsIgnoreCase(""))) {

						setErrorMessage("Please Define the Side.");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					} else {
						visibleDiv = true;
						RequestContext.getCurrentInstance().update("searchForm:shwTableDetail");
						RequestContext.getCurrentInstance().update("searchForm:btn-generate");

						if (combinePanelDTO.getDefineSide().equals("Origin to Destination")) {
							midPointList = combinePanelService.getAllMidPointsOrgToDes(combinePanelDTO.getRouteNo(),
									combinePanelDTO.getServiceType(), combinePanelDTO.getGroupNo());

						} else {
							midPointList = combinePanelService.getAllMidPointsDesToOri(combinePanelDTO.getRouteNo(),
									combinePanelDTO.getServiceType(), combinePanelDTO.getGroupNo());
						}

						permitNoList.addAll(setUpMidPointsService.getPermitNoToDropDown(combinePanelDTO.getRouteNo()));
						busNoList.addAll(setUpMidPointsService.getBusNoToDropDown(combinePanelDTO.getRouteNo()));
						
						selectedList.addAll(midPointList);
						if (midPointList.isEmpty()) {
							setErrorMessage("No Data Found.");
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						} else {
							setSearch(true);
						}
					}

				}
			}
		}
	}

	public void clearOne() {
		dropDownDataStore = new CombinePanelGenaratorDTO();
		combinePanelDTO = new CombinePanelGenaratorDTO();
		originDestinationList = new ArrayList<CombinePanelGenaratorDTO>();
		detailsList = new ArrayList<CombinePanelGenaratorDTO>();
		selectedList = new ArrayList<SetUpMidPointsDTO>();
		tableDataList = new ArrayList<SetUpMidPointsDTO>();
		selectedMidPointList = new ArrayList<SetUpMidPointsDTO>();
		addedAllTimeTaken = new ArrayList<SetUpMidPointsDTO>();
		selectedDataList = new ArrayList<SetUpMidPointsDTO>();
		busDetailsList = new ArrayList<BusDetailsDTO>();

		visibleDiv = false;
		visible = false;

		midPointList = new ArrayList<>();
		tableDetails = null;
		uniqueMidtimes = new LinkedHashSet<>();
		combinedMidtimes = new ArrayList<>();

		busNoPermitNoList = new ArrayList<CombinePanelGenaratorDTO>();
		generatedColumns = new ArrayList<>();
		selectedMidPointTimeList = new ArrayList<>();
		timeRangeList = setTimeRange();
		originToDestinationTimeList = new ArrayList<>();
		destinationToOriginTimeList = new ArrayList<>();
		timesWithRoute = new ArrayList<>();
		timesWithRouteDes = new ArrayList<>();
		times = new LinkedHashMap<>();
		timesDes = new LinkedHashMap<>();
		selectedTimes = new HashMap<>();
		routeUsed = new ArrayList<>();
		uniqueStartTimes = new LinkedHashSet<>();
		combinedStartTimes = new ArrayList<>();
		uniqueEndTimes = new LinkedHashSet<>();
		combinedEndTimes = new ArrayList<>();
		permitNoList = new ArrayList<>();
		busNoList = new ArrayList<>();
		runningNoDetailsList = new ArrayList<>();
		runningNoList = new ArrayList<>();

		visibleTable = false;
		routeNo = null;
		serviceType = null;
		groupNo = null;
		defineSide = null;
		disableSelectMenu = false;
		origin = null;
		destination = null;
		midPointName = null;

		combinePanelService.clearPreviousData(deviceNo + 1);

		selectedRouteList = new ArrayList<RouteCreationDTO>();
		disableSave = false;

	}

	public List<String> setTimeRange() {
		List<String> times = new ArrayList<>();
		times.add("00:00 - 00:59");
		times.add("01:00 - 01:59");
		times.add("02:00 - 02:59");
		times.add("03:00 - 03:59");
		times.add("04:00 - 04:59");
		times.add("05:00 - 05:59");
		times.add("06:00 - 06:59");
		times.add("07:00 - 07:59");
		times.add("08:00 - 08:59");
		times.add("09:00 - 09:59");
		times.add("10:00 - 10:59");
		times.add("11:00 - 11:59");
		times.add("12:00 - 12:59");
		times.add("13:00 - 13:59");
		times.add("14:00 - 14:59");
		times.add("15:00 - 15:59");
		times.add("16:00 - 16:59");
		times.add("17:00 - 17:59");
		times.add("18:00 - 18:59");
		times.add("19:00 - 19:59");
		times.add("20:00 - 20:59");
		times.add("21:00 - 21:59");
		times.add("22:00 - 22:59");
		times.add("23:00 - 23:59");

		return times;
	}

	public void addAction() {
		combinePanelDTO.setRouteNo(null);
		combinePanelDTO.setServiceType(null);
		combinePanelDTO.setGroupNo(null);
		combinePanelDTO.setDefineSide(null);
		midPointList = null;
		visible = false;

		uniqueMidtimes = new LinkedHashSet<String>();
		combinedMidtimes = new ArrayList<String>();
		disableSave = false;
	}

	public void saveAction() {
		if (!selectedList.isEmpty()) {
			List<String> listMid = new ArrayList<>();
			List<String> listMidTime = new ArrayList<>();
			Map<String, String> midpoints = new LinkedHashMap<>();
			String startTime = null;
			String endTime = null;
			tableDetails = new SetUpMidPointsDTO();
			selectedDataList = new ArrayList<>();

			for (SetUpMidPointsDTO data : selectedList) {
				listMid.add(data.getMidPointName());
			}

			try {
//				listMidTime = setUpMidPointsService.calculateArrivalTimesOrgList(startTime, selectedList, midPointList);

//			for(int i = 0; i < listMid.size();i++) {
//				midpoints.put(listMid.get(i), listMidTime.get(i));
//			}

			} catch (Exception e) {
				e.printStackTrace();
				setErrorMessage("An error occurred while calculating the data");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

			tableDetails = combinePanelService.getAllDetailsOrgToDes(combinePanelDTO.getRouteNo(),
					combinePanelDTO.getServiceType(), combinePanelDTO.getServiceType());

			runningNoDetailsList.add(combinePanelService.getAllRunningDetailsNo(combinePanelDTO.getRouteNo(),
					combinePanelDTO.getServiceType()));

			RouteCreationDTO routeCreationDTO = new RouteCreationDTO();
			routeCreationDTO.setRouteNo(combinePanelDTO.getRouteNo());
			routeCreationDTO.setServiceTypeCode(combinePanelDTO.getServiceType());
			selectedRouteList.add(routeCreationDTO);

			tableDetails.setSelectedMidName(listMid);
			tableDetails.setSelectedMidTime(listMidTime);
			tableDetails.setMidpoints(midpoints);
			tableDetails.setStartTime(startTime);
			tableDetails.setEndTime(endTime);
			tableDetails.setPermitNoList(permitNoList);
			tableDetails.setBusNoList(busNoList);
			tableDetails.setCode(combinePanelDTO.getServiceType());
			tableDetails.setTripType(combinePanelDTO.getDefineSide());
			tableDetails.setGroup(combinePanelDTO.getGroupNo());

//			dataDetails = combinePanelService.getBusData(combinePanelDTO.getRouteNo(),
//					combinePanelDTO.getServiceType());

			if (combinePanelDTO.getGroupNo().equals("1")) {
				tableDetails.setGroupNo("Group No 1");
			} else if (combinePanelDTO.getGroupNo().equals("2")) {
				tableDetails.setGroupNo("Group No 2");
			} else {
				tableDetails.setGroupNo("Group No 3");
			}

			tableDataList.add(tableDetails);
			busTimeDetailsList.addAll(tableDataList);
			disableSave = true;
		} else {
			setErrorMessage("Please Select Row");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void genarateData() {
		if (!busTimeDetailsList.isEmpty()) {
			combinedMidtimes = new ArrayList<>();
			combinedStartTimes = new ArrayList<>();
			combinedEndTimes = new ArrayList<>();

			uniqueMidtimes = new LinkedHashSet<>();
			uniqueStartTimes = new LinkedHashSet<>();
			uniqueEndTimes = new LinkedHashSet<>();
			
			selectedDataList = new ArrayList<>();
			
			List<String> routes = new ArrayList<>();

			for (SetUpMidPointsDTO dto : busTimeDetailsList) {
				if (!routeUsed.contains(
						dto.getRouteNo() + "-" + dto.getCode() + "-" + dto.getGroup() + "-" + dto.getTripType())) {
					combinePanelService.getAllTimes(dto.getRouteNo(), dto.getCode(), dto.getGroup(), dto.getTripType(),
							deviceNo + 1);
					routeUsed.add(
							dto.getRouteNo() + "-" + dto.getCode() + "-" + dto.getGroup() + "-" + dto.getTripType());
				}
				
				routes.add(dto.getRouteNo());
			}

			runningNoList = combinePanelService.getAllRunningNo();
			List<SetUpMidPointsDTO>  selectDataList = combinePanelService.getAllTimeDataForTable(routeUsed, deviceNo + 1);

			for (SetUpMidPointsDTO dto : selectDataList) {
				if(routes.contains(dto.getRouteNo())) {
					uniqueStartTimes.addAll(dto.getStartTimes().keySet());
					uniqueEndTimes.addAll(dto.getEndTimes().keySet());
					uniqueMidtimes.addAll(dto.getMidpoints().keySet());
					
					selectedDataList.add(dto);
				}			
			}

			combinedStartTimes = new ArrayList<>(uniqueStartTimes);
			combinedEndTimes = new ArrayList<>(uniqueEndTimes);
			combinedMidtimes = new ArrayList<>(uniqueMidtimes);

			RequestContext.getCurrentInstance().execute("PF('genarateTimeSlote').show()");
		} else {
			setErrorMessage("Please Select Row");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

		disableSelectMenu = true;
	}

	public void saveActionTimeGenerator() {
		combinePanelService.clearPreviousData(deviceNo + 1);
		combinePanelService.setMidPointData(selectedDataList);

		setSuccessMessage("Saved Successfully");
		RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
//		combinePanelService.updateMidPointData(selectedDataList);

//		String check = null;
//		List<String> stringList = new ArrayList<>();
//		List<SetUpMidPointsDTO> employees = new ArrayList<SetUpMidPointsDTO>();
//		try { 
//			HashMap<String, Object> parameters = new HashMap<String, Object>();
//			for(SetUpMidPointsDTO data:selectedDataList) {
//				stringList = data.getSelectedMidName();
//			}
//			
//			TimeTableDTO dto = timeTableService.getDetailsForReport(combinePanelDTO.getRouteNo());
//			parameters.put("routeDesSinhala", dto.getDescription());
//	        parameters.put("routeNo", combinePanelDTO.getRouteNo());
//	        parameters.put("serviceType", combinePanelDTO.getServiceType());
//	        
//			String sourceFileName = "..//reports//CombinePanelGenerator.jrxml";
//	        JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));
//			JasperReport report = JasperCompileManager.compileReport(jasDes);		
//			//JRDataSource data = new JREmptyDataSource();
//			JRBeanCollectionDataSource data = new JRBeanCollectionDataSource(employees,false);
//			JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, data);	
//			
//			//JasperViewer.viewReport(jasperPrint, false);	
//			JasperExportManager.exportReportToPdfFile(jasperPrint, "d:/report.pdf");
//		}catch(Exception e) {
//			e.printStackTrace();
//		}

	}

//	public void getAllDetailsOfRoute(SetUpMidPointsDTO data) {
//		combinePanelService.getAllTimes(data.getRouteNo(), data.getCode(), data.getGroup(), data.getTripType());
//		
//		origin = busTimeDetailsList.get(0).getOrigin();
//		destination = busTimeDetailsList.get(0).getDestination();
//	}

	private int getColumnIndex(List<String> headerList, String value, String type) {

		int startTimesListSize = combinedStartTimes.size();
		int midTimesListSize = combinedMidtimes.size();
		int fixHeaderSize = 4;

		if (type.equals("S")) {

			for (int i = 0; i < headerList.size(); i++) {
				if (value.equals(headerList.get(i))) {
					return i + fixHeaderSize;
				}
			}
		} else if (type.equals("M")) {
			for (int i = 0; i < headerList.size(); i++) {
				if (value.equals(headerList.get(i))) {
					return i + fixHeaderSize + startTimesListSize;
				}
			}
		}else {
			for (int i = 0; i < headerList.size(); i++) {
				if (value.equals(headerList.get(i))) {
					return i + fixHeaderSize + startTimesListSize+ midTimesListSize;
				}
			}
		}

		return -1;
	}

	public void generateExcel() {

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Combine Panel Generator Sheet");
		sheet.getPrintSetup().setLandscape(true);

		Row row1 = sheet.createRow(0);
		row1.setHeight((short) 1000);
		int maxColumn = 0;

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

			Font boldFont = workbook.createFont();
			boldFont.setBold(true);

			CellStyle wrapStyle = workbook.createCellStyle();
			wrapStyle.setWrapText(true);

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
			tableHeaderStyle.setFont(boldFont);

			CellStyle boldFontStyle = workbook.createCellStyle();
			boldFontStyle.setFont(boldFont);

			String mainHeading = "NATIONAL TRANSPORT COMMISSION\nCombine Panel Generator";
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

			List<String> headers = new ArrayList<>(
					Arrays.asList("Service Type", "Running No.", "Route No.", "Start From"));
			headers.addAll(combinedStartTimes);
			headers.addAll(combinedMidtimes);
			headers.addAll(combinedEndTimes);
			headers.addAll(Arrays.asList("Route End"));
			maxColumn = headers.size();

			int maxOriginLength = 11;
			int maxDestinationLength = 10;
			int rowNum = 2;

			if (runningNoDetailsList != null && !runningNoDetailsList.isEmpty()) {
				for (String cell : runningNoDetailsList) {
					Row runningNoDetailsRow = sheet.createRow(rowNum);
					Cell runningNoDetailsRowCell = runningNoDetailsRow.createCell(0);
					runningNoDetailsRowCell.setCellValue(cell);
					sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, maxColumn - 1));
					rowNum++;
				}
			}

			// create and set table header row and cell values
			Row headerRow = sheet.createRow(++rowNum);
			for (int i = 0; i < headers.size(); i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers.get(i));
				cell.setCellStyle(tableHeaderStyle);
				if (i > 4 && i != (headers.size() - 1)) {
					sheet.setColumnWidth(i, 384 * headers.get(i).length());
				}
			}

			// create table body with empty cells
			int startRow = ++rowNum;
			int endRow = (rowNum + selectedDataList.size()) - 1;
			int startCol = 0;
			int endCol = maxColumn - 1;
			for (int i = startRow; i <= endRow; i++) {
				Row row = sheet.createRow(i);
				for (int j = startCol; j <= endCol; j++) {
					Cell cell = row.createCell(j);
					setBorder(workbook, cell);
				}
			}

			// set values to table body
			for (SetUpMidPointsDTO dto : selectedDataList) {
				Row row = sheet.getRow(rowNum++);
				if (row != null) {
					int cellNum = 0;
					Cell cell1 = row.getCell(cellNum++);
					cell1.setCellValue((String) dto.getServiceType());

					Cell cell2 = row.getCell(cellNum++);
					cell2.setCellValue((String) dto.getAbbreviation());

					Cell cell3 = row.getCell(cellNum++);
					cell3.setCellValue((String) dto.getRouteNo());

					Cell cell4 = row.getCell(cellNum++);
					cell4.setCellValue((String) dto.getOrigin());

					for (String startTime : combinedStartTimes) {
						for (Map.Entry<String, String> entry : dto.getStartTimes().entrySet()) {
							if (entry.getKey().equals(startTime)) {
								int index = getColumnIndex(combinedStartTimes, startTime, "S");
								Cell dyncamicCell = row.getCell(index);
								dyncamicCell.setCellValue((String) entry.getValue());
								break;
							}
						}
					}

					for (String midTimes : combinedMidtimes) {
						for (Map.Entry<String, String> entry : dto.getMidpoints().entrySet()) {
							if (entry.getKey().equals(midTimes)) {
								int index = getColumnIndex(combinedMidtimes, midTimes, "M");
								Cell dyncamicCell = row.getCell(index);
								dyncamicCell.setCellValue((String) entry.getValue());
								break;
							}

						}
					}

					for (String endTimes : combinedEndTimes) {
						for (Map.Entry<String, String> entry : dto.getEndTimes().entrySet()) {
							if (entry.getKey().equals(endTimes)) {
								int index = getColumnIndex(combinedEndTimes, endTimes, "E");
								Cell dyncamicCell = row.getCell(index);
								dyncamicCell.setCellValue((String) entry.getValue());
								break;
							}
						}
					}


					Cell cell5 = row.getCell(endCol);
					cell5.setCellValue(String.valueOf(dto.getDestination()));

					if (dto.getOrigin().length() > maxOriginLength) {
						maxOriginLength = dto.getOrigin().length();
					}

					if (dto.getDestination().length() > maxDestinationLength) {
						maxDestinationLength = dto.getDestination().length();
					}
				}

			}

			// set footer
			String ntcAct = "As stated in Section 169 of the Motor Vehicle Act No. 21 of 1981, the owner of the bus is fully responsible for the working hours of the driver employed in the service of the bus. Accordingly, the entire journey of this service exceeds 14 hours when the buses run, so two drivers must be used for both the up and down journeys.";
			int footerRowIndex = ++endRow;
			Row footerRow1 = sheet.createRow(++footerRowIndex);
			Cell footerRow1Cell1 = footerRow1.createCell(0);
			footerRow1Cell1.setCellValue(ntcAct);
			footerRow1Cell1.setCellStyle(wrapStyle);

			footerRow1.setHeightInPoints((short) 50);

			sheet.addMergedRegion(new CellRangeAddress(footerRowIndex, footerRowIndex, 0, maxColumn - 1));

			++footerRowIndex;

			Row footerRow2 = sheet.createRow(++footerRowIndex);
			Cell footerRow2Cell0 = footerRow2.createCell(0);
			footerRow2Cell0.setCellValue("Route");
			footerRow2Cell0.setCellStyle(boldFontStyle);

			Cell footerRow2Cell1 = footerRow2.createCell(1);
			footerRow2Cell1.setCellValue("Distance");
			footerRow2Cell1.setCellStyle(boldFontStyle);

			Cell footerRow2Cell2 = footerRow2.createCell(2);
			footerRow2Cell2.setCellValue("Travel Time");
			footerRow2Cell2.setCellStyle(boldFontStyle);

			if (selectedRouteList != null && !selectedRouteList.isEmpty()) {

				selectedRouteList = combinePanelService.getRouteInfo(selectedRouteList);

				for (RouteCreationDTO routeCreationDTO : selectedRouteList) {
					Row footerDynamicRow = sheet.createRow(++footerRowIndex);
					Cell footerDynamicRowCell = footerDynamicRow.createCell(0);
					footerDynamicRowCell.setCellValue(routeCreationDTO.getRouteNo());

					Cell footerDynamicRowCel2 = footerDynamicRow.createCell(1);
					footerDynamicRowCel2.setCellValue(routeCreationDTO.getDistance());

					Cell footerDynamicRowCel3 = footerDynamicRow.createCell(2);
					footerDynamicRowCel3.setCellValue(String.valueOf(routeCreationDTO.getTravelTimeStr()));
				}
			}

			Row footerRow3 = sheet.createRow(++footerRowIndex);
			Cell footerRow3Cell4 = footerRow3.createCell(4);
			footerRow3Cell4.setCellValue("Prepaired By:");
			sheet.addMergedRegion(new CellRangeAddress(footerRowIndex, footerRowIndex, 4, 5));

			Cell footerRow3Cell6 = footerRow3.createCell(6);
			footerRow3Cell6.setCellValue("");
			sheet.addMergedRegion(new CellRangeAddress(footerRowIndex, footerRowIndex, 6, 7));

			Row footerRow4 = sheet.createRow(++footerRowIndex);
			Cell footerRow4Cell4 = footerRow4.createCell(4);
			footerRow4Cell4.setCellValue("Created By:");
			sheet.addMergedRegion(new CellRangeAddress(footerRowIndex, footerRowIndex, 4, 5));

			Cell footerRow4Cell6 = footerRow4.createCell(6);
			footerRow4Cell6.setCellValue("");
			sheet.addMergedRegion(new CellRangeAddress(footerRowIndex, footerRowIndex, 6, 7));

			Row footerRow5 = sheet.createRow(++footerRowIndex);
			Cell footerRow5Cell4 = footerRow5.createCell(4);
			footerRow5Cell4.setCellValue("Recommended By:");
			sheet.addMergedRegion(new CellRangeAddress(footerRowIndex, footerRowIndex, 4, 5));

			Cell footerRow5Cell6 = footerRow5.createCell(6);
			footerRow5Cell6.setCellValue("");
			sheet.addMergedRegion(new CellRangeAddress(footerRowIndex, footerRowIndex, 6, 7));

			++footerRowIndex;
			Row footerRow6 = sheet.createRow(++footerRowIndex);
			Cell footerRow6Cell0 = footerRow6.createCell(0);
			footerRow6Cell0.setCellValue("Implement Date: ");
			footerRow6Cell0.setCellStyle(boldFontStyle);

			Cell footerRow6Cell1 = footerRow6.createCell(1);
			footerRow6Cell1.setCellValue(LocalDate.now().toString());
			footerRow6Cell1.setCellStyle(boldFontStyle);

			Cell footerRow6Cell6 = footerRow6.createCell(6);
			footerRow6Cell6.setCellValue("Chairman");
			sheet.addMergedRegion(new CellRangeAddress(footerRowIndex, footerRowIndex, 6, 8));
			footerRow6Cell6.setCellStyle(boldFontStyle);

			Row footerRow7 = sheet.createRow(++footerRowIndex);
			Cell footerRow7Cell6 = footerRow7.createCell(6);
			footerRow7Cell6.setCellValue("National Transport Commission");
			sheet.addMergedRegion(new CellRangeAddress(footerRowIndex, footerRowIndex, 6, 7));
			footerRow7Cell6.setCellStyle(boldFontStyle);

			// set logo
			ntcLogoAncor.setCol1(8);
			ntcLogoAncor.setCol2(9);
			ntcLogoAncor.setRow1(0);
			ntcLogoAncor.setRow2(1);

			nationalLogoAncor.setCol1(0);
			nationalLogoAncor.setCol2(1);
			nationalLogoAncor.setRow1(0);
			nationalLogoAncor.setRow2(1);

			Picture picturenationalLogo = drawing.createPicture(nationalLogoAncor, nationalLogo);
			Picture pictureNTCLogo = drawing.createPicture(ntcLogoAncor, ntcLogo);

			picturenationalLogo.resize(0.75, 1.0);
			pictureNTCLogo.resize(0.85, 1.0);

			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));

			sheet.setColumnWidth(0, 5000);
			sheet.setColumnWidth(1, 3000);
			sheet.setColumnWidth(2, 3000);
			sheet.setColumnWidth(3, 3000);
			sheet.setColumnWidth(4, 384 * maxOriginLength);
			sheet.setColumnWidth(maxColumn - 1, 384 * maxOriginLength);

			sheet.setRepeatingRows(new CellRangeAddress(0, 0, 0, sheet.getRow(0).getLastCellNum() - 1));

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			byte[] byteArray = bos.toByteArray();

			if (byteArray != null) {
				InputStream stream = new ByteArrayInputStream(byteArray);
				LocalDate currentDate = LocalDate.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				String dateStringWithHyphen = currentDate.format(formatter);
				excelSheet = new DefaultStreamedContent(stream,
						"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
						"CPG" + dateStringWithHyphen.replaceAll("-", "") + ".xlsx");
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

	public void getTimesForRouteMidPoint(String midPoint) {
		timesWithRoute = new ArrayList<>();
		timesWithRouteDes = new ArrayList<>();
		LinkedHashMap<String, List<String>> timesMap = new LinkedHashMap<>();
		originToDestinationTimeList = new ArrayList<>();
		destinationToOriginTimeList = new ArrayList<>();
		timesDes = new LinkedHashMap<>();
		times = new LinkedHashMap<>();
		selectedTimes = new HashMap<>();
		List<String> routes = new ArrayList<>();
		List<String> routesWithGroup = new ArrayList<>();
		if (!timeRange.equals(null) && !timeRange.equals("")) {
			midPointName = midPoint;
			String[] timeParts = timeRange.split(" - ");
			for (SetUpMidPointsDTO data : selectedDataList) {
				routes.add(data.getRouteNo() + " - " + data.getTripType());
				routesWithGroup.add(data.getRouteNo() + " - " + data.getGroup());
			}

			timesWithSides = combinePanelService.getAllTimesForMidPoint(midPoint, timeParts[0], timeParts[1],
					routesWithGroup);

			for (Map.Entry<String, LinkedHashMap<String, List<String>>> entry : timesWithSides.entrySet()) {
				String route = entry.getKey();

				if (routes.contains(route)) {
					timesMap = entry.getValue();

					if (route.contains("Origin to Destination")) {
						String[] routeNo = route.split(" - ");
						Map.Entry<String, List<String>> firstEntry = timesMap.entrySet().stream().findFirst().get();
						times.put(routeNo[0], firstEntry.getValue());
						CombinePanelGenaratorDTO comDto = new CombinePanelGenaratorDTO();
						comDto.setTimesWithRoute(times);
						comDto.setMidPointName(midPoint);
						comDto.setRowNo(firstEntry.getValue().size());
						timesWithRoute.add(comDto);
						originToDestinationTimeList.add(routeNo[0]);
					} else if (route.contains("Destination to Origin")) {
						String[] routeNo = route.split(" - ");
						Map.Entry<String, List<String>> firstEntry = timesMap.entrySet().stream().findFirst().get();
						timesDes.put(routeNo[0], firstEntry.getValue());
						CombinePanelGenaratorDTO comDto = new CombinePanelGenaratorDTO();
						comDto.setTimesWithRoute(timesDes);
						comDto.setMidPointName(midPoint);
						comDto.setRowNo(firstEntry.getValue().size());
						timesWithRouteDes.add(comDto);
						destinationToOriginTimeList.add(routeNo[0]);
					}
				}
			}

			PrimeFaces.current().executeScript("PF('timeSelector').show();");
			timeRange = null;
		}
	}

	public void submit(String midpoint) {
		for (Map.Entry<String, String> entry : selectedTimes.entrySet()) {
			String topic = entry.getKey();
			String selectedTime = entry.getValue();

			for (SetUpMidPointsDTO data : selectedDataList) {
				if (topic.equals(data.getRouteNo())) {
					if (data.getMidpoints().containsKey(midpoint)) {
						data.getMidpoints().put(midpoint, selectedTime);
					}
				}
			}
		}

		PrimeFaces.current().executeScript("PF('timeSelector').hide();");
	}

	public void changeStartTime(SetUpMidPointsDTO data, String table) {
		if (table.equalsIgnoreCase("s")) {
			combinePanelService.updateTables(data, table, deviceNo + 1);
		} else if (table.equalsIgnoreCase("m")) {
			combinePanelService.updateTables(data, table, deviceNo + 1);
		} else if (table.equalsIgnoreCase("e")) {
			combinePanelService.updateTables(data, table, deviceNo + 1);
		}

	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public CombinePanelGenaratorService getCombinePanelService() {
		return combinePanelService;
	}

	public void setCombinePanelService(CombinePanelGenaratorService combinePanelService) {
		this.combinePanelService = combinePanelService;
	}

	public List<CombinePanelGenaratorDTO> getBusRouteList() {
		return busRouteList;
	}

	public void setBusRouteList(List<CombinePanelGenaratorDTO> busRouteList) {
		this.busRouteList = busRouteList;
	}

	public List<CombinePanelGenaratorDTO> getOriginDestinationList() {
		return originDestinationList;
	}

	public void setOriginDestinationList(List<CombinePanelGenaratorDTO> originDestinationList) {
		this.originDestinationList = originDestinationList;
	}

	public CombinePanelGenaratorDTO getCombinePanelDTO() {
		return combinePanelDTO;
	}

	public void setCombinePanelDTO(CombinePanelGenaratorDTO combinePanelDTO) {
		this.combinePanelDTO = combinePanelDTO;
	}

	public String getRouteNo() {
		return routeNo;
	}

	public void setRouteNo(String routeNo) {
		this.routeNo = routeNo;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}

	public String getDefineSide() {
		return defineSide;
	}

	public void setDefineSide(String defineSide) {
		this.defineSide = defineSide;
	}

	public CombinePanelGenaratorDTO getRouteOriginDestination() {
		return routeOriginDestination;
	}

	public void setRouteOriginDestination(CombinePanelGenaratorDTO routeOriginDestination) {
		this.routeOriginDestination = routeOriginDestination;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public List<CombinePanelGenaratorDTO> getDetailsList() {
		return detailsList;
	}

	public void setDetailsList(List<CombinePanelGenaratorDTO> detailsList) {
		this.detailsList = detailsList;
	}

	public CombinePanelGenaratorDTO getDropDownDataStore() {
		return dropDownDataStore;
	}

	public void setDropDownDataStore(CombinePanelGenaratorDTO dropDownDataStore) {
		this.dropDownDataStore = dropDownDataStore;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
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

	public SetUpMidPointsDTO getSetUpMidPointsDTO() {
		return setUpMidPointsDTO;
	}

	public void setSetUpMidPointsDTO(SetUpMidPointsDTO setUpMidPointsDTO) {
		this.setUpMidPointsDTO = setUpMidPointsDTO;
	}

	public SetUpMidPointsService getSetUpMidPointsService() {
		return setUpMidPointsService;
	}

	public void setSetUpMidPointsService(SetUpMidPointsService setUpMidPointsService) {
		this.setUpMidPointsService = setUpMidPointsService;
	}

	public List<SetUpMidPointsDTO> getMidPointList() {
		return midPointList;
	}

	public void setMidPointList(List<SetUpMidPointsDTO> midPointList) {
		this.midPointList = midPointList;
	}

	public boolean isSearch() {
		return isSearch;
	}

	public void setSearch(boolean isSearch) {
		this.isSearch = isSearch;
	}

	public boolean isVisibleTable() {
		return visibleTable;
	}

	public void setVisibleTable(boolean visibleTable) {
		this.visibleTable = visibleTable;
	}

	public UIComponent getAddRouteComponent() {
		return addRouteComponent;
	}

	public void setAddRouteComponent(UIComponent addRouteComponent) {
		this.addRouteComponent = addRouteComponent;
	}

	public boolean isDisabledOne() {
		return disabledOne;
	}

	public void setDisabledOne(boolean disabledOne) {
		this.disabledOne = disabledOne;
	}

	public List<BusDetailsDTO> getBusDetailsList() {
		return busDetailsList;
	}

	public void setBusDetailsList(List<BusDetailsDTO> busDetailsList) {
		this.busDetailsList = busDetailsList;
	}

	public boolean isSaveClicked() {
		return saveClicked;
	}

	public void setSaveClicked(boolean saveClicked) {
		this.saveClicked = saveClicked;
	}

	public List<CommonDTO> getDrpdServiceTypeList() {
		return drpdServiceTypeList;
	}

	public void setDrpdServiceTypeList(List<CommonDTO> drpdServiceTypeList) {
		this.drpdServiceTypeList = drpdServiceTypeList;
	}

	public boolean isVisibleDiv() {
		return visibleDiv;
	}

	public void setVisibleDiv(boolean visibleDiv) {
		this.visibleDiv = visibleDiv;
	}

	public List<SetUpMidPointsDTO> getSelectedList() {
		return selectedList;
	}

	public void setSelectedList(List<SetUpMidPointsDTO> selectedList) {
		this.selectedList = selectedList;
	}

	public List<SetUpMidPointsDTO> getTableDataList() {
		return tableDataList;
	}

	public void setTableDataList(List<SetUpMidPointsDTO> tableDataList) {
		this.tableDataList = tableDataList;
	}

	public List<SetUpMidPointsDTO> getSelectedDataList() {
		return selectedDataList;
	}

	public void setSelectedDataList(List<SetUpMidPointsDTO> selectedDataList) {
		this.selectedDataList = selectedDataList;
	}

	public String getGenarateTimeSlote() {
		return genarateTimeSlote;
	}

	public void setGenarateTimeSlote(String genarateTimeSlote) {
		this.genarateTimeSlote = genarateTimeSlote;
	}

	public SetUpMidPointsDTO getTableDetails() {
		return tableDetails;
	}

	public void setTableDetails(SetUpMidPointsDTO tableDetails) {
		this.tableDetails = tableDetails;
	}

	public String getMidPointTable() {
		return midPointTable;
	}

	public void setMidPointTable(String midPointTable) {
		this.midPointTable = midPointTable;
	}

	public List<SetUpMidPointsDTO> getSelectedMidPointList() {
		return selectedMidPointList;
	}

	public void setSelectedMidPointList(List<SetUpMidPointsDTO> selectedMidPointList) {
		this.selectedMidPointList = selectedMidPointList;
	}

	public BusDetailsDTO getBusDetails() {
		return busDetails;
	}

	public void setBusDetails(BusDetailsDTO busDetails) {
		this.busDetails = busDetails;
	}

	public List<SetUpMidPointsDTO> getAddedAllTimeTaken() {
		return addedAllTimeTaken;
	}

	public void setAddedAllTimeTaken(List<SetUpMidPointsDTO> addedAllTimeTaken) {
		this.addedAllTimeTaken = addedAllTimeTaken;
	}

	public CombinePanelGenaratorDTO getDataDetails() {
		return dataDetails;
	}

	public void setDataDetails(CombinePanelGenaratorDTO dataDetails) {
		this.dataDetails = dataDetails;
	}

	public List<CombinePanelGenaratorDTO> getBusNoPermitNoList() {
		return busNoPermitNoList;
	}

	public void setBusNoPermitNoList(List<CombinePanelGenaratorDTO> busNoPermitNoList) {
		this.busNoPermitNoList = busNoPermitNoList;
	}

	public boolean isDisableSelectMenu() {
		return disableSelectMenu;
	}

	public void setDisableSelectMenu(boolean disableSelectMenu) {
		this.disableSelectMenu = disableSelectMenu;
	}

	public List<Column> getGeneratedColumns() {
		return generatedColumns;
	}

	public void setGeneratedColumns(List<Column> generatedColumns) {
		this.generatedColumns = generatedColumns;
	}

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	public List<SetUpMidPointsDTO> getMidPointTimeListOd() {
		return midPointTimeListOd;
	}

	public void setMidPointTimeListOd(List<SetUpMidPointsDTO> midPointTimeListOd) {
		this.midPointTimeListOd = midPointTimeListOd;
	}

	public List<SetUpMidPointsDTO> getMidPointTimeListDo() {
		return midPointTimeListDo;
	}

	public void setMidPointTimeListDo(List<SetUpMidPointsDTO> midPointTimeListDo) {
		this.midPointTimeListDo = midPointTimeListDo;
	}

	public List<String> getSelectedMidPointTimeList() {
		return selectedMidPointTimeList;
	}

	public void setSelectedMidPointTimeList(List<String> selectedMidPointTimeList) {
		this.selectedMidPointTimeList = selectedMidPointTimeList;
	}

	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
	}

	public TimeTableService getTimeTableService() {
		return timeTableService;
	}

	public void setTimeTableService(TimeTableService timeTableService) {
		this.timeTableService = timeTableService;
	}

	public Map<String, String> getMidTimeNameMap() {
		return midTimeNameMap;
	}

	public void setMidTimeNameMap(Map<String, String> midTimeNameMap) {
		this.midTimeNameMap = midTimeNameMap;
	}

	public Set<String> getUniqueMidtimes() {
		return uniqueMidtimes;
	}

	public void setUniqueMidtimes(Set<String> uniqueMidtimes) {
		this.uniqueMidtimes = uniqueMidtimes;
	}

	public List<String> getCombinedMidtimes() {
		return combinedMidtimes;
	}

	public void setCombinedMidtimes(List<String> combinedMidtimes) {
		this.combinedMidtimes = combinedMidtimes;
	}

	public String getPermitNo() {
		return permitNo;
	}

	public void setPermitNo(String permitNo) {
		this.permitNo = permitNo;
	}

	public String getBusNo() {
		return busNo;
	}

	public void setBusNo(String busNo) {
		this.busNo = busNo;
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

	public List<SetUpMidPointsDTO> getBusTimeDetailsList() {
		return busTimeDetailsList;
	}

	public void setBusTimeDetailsList(List<SetUpMidPointsDTO> busTimeDetailsList) {
		this.busTimeDetailsList = busTimeDetailsList;
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

	public List<String> getTimeRangeList() {
		return timeRangeList;
	}

	public void setTimeRangeList(List<String> timeRangeList) {
		this.timeRangeList = timeRangeList;
	}

	public String getTimeRange() {
		return timeRange;
	}

	public void setTimeRange(String timeRange) {
		this.timeRange = timeRange;
	}

	public LinkedHashMap<String, LinkedHashMap<String, List<String>>> getTimesWithSides() {
		return timesWithSides;
	}

	public void setTimesWithSides(LinkedHashMap<String, LinkedHashMap<String, List<String>>> timesWithSides) {
		this.timesWithSides = timesWithSides;
	}

	public List<String> getOriginToDestinationTimeList() {
		return originToDestinationTimeList;
	}

	public void setOriginToDestinationTimeList(List<String> originToDestinationTimeList) {
		this.originToDestinationTimeList = originToDestinationTimeList;
	}

	public List<String> getDestinationToOriginTimeList() {
		return destinationToOriginTimeList;
	}

	public void setDestinationToOriginTimeList(List<String> destinationToOriginTimeList) {
		this.destinationToOriginTimeList = destinationToOriginTimeList;
	}

	public List<CombinePanelGenaratorDTO> getTimesWithRoute() {
		return timesWithRoute;
	}

	public void setTimesWithRoute(List<CombinePanelGenaratorDTO> timesWithRoute) {
		this.timesWithRoute = timesWithRoute;
	}

	public String getMidPointName() {
		return midPointName;
	}

	public void setMidPointName(String midPointName) {
		this.midPointName = midPointName;
	}

	public List<CombinePanelGenaratorDTO> getTimesWithRouteDes() {
		return timesWithRouteDes;
	}

	public void setTimesWithRouteDes(List<CombinePanelGenaratorDTO> timesWithRouteDes) {
		this.timesWithRouteDes = timesWithRouteDes;
	}

	public LinkedHashMap<String, List<String>> getTimes() {
		return times;
	}

	public void setTimes(LinkedHashMap<String, List<String>> times) {
		this.times = times;
	}

	public LinkedHashMap<String, List<String>> getTimesDes() {
		return timesDes;
	}

	public void setTimesDes(LinkedHashMap<String, List<String>> timesDes) {
		this.timesDes = timesDes;
	}

	public Map<String, String> getSelectedTimes() {
		return selectedTimes;
	}

	public void setSelectedTimes(Map<String, String> selectedTimes) {
		this.selectedTimes = selectedTimes;
	}

	public List<String> getRouteUsed() {
		return routeUsed;
	}

	public void setRouteUsed(List<String> routeUsed) {
		this.routeUsed = routeUsed;
	}

	public Set<String> getUniqueStartTimes() {
		return uniqueStartTimes;
	}

	public void setUniqueStartTimes(Set<String> uniqueStartTimes) {
		this.uniqueStartTimes = uniqueStartTimes;
	}

	public List<String> getCombinedStartTimes() {
		return combinedStartTimes;
	}

	public void setCombinedStartTimes(List<String> combinedStartTimes) {
		this.combinedStartTimes = combinedStartTimes;
	}

	public Set<String> getUniqueEndTimes() {
		return uniqueEndTimes;
	}

	public void setUniqueEndTimes(Set<String> uniqueEndTimes) {
		this.uniqueEndTimes = uniqueEndTimes;
	}

	public List<String> getCombinedEndTimes() {
		return combinedEndTimes;
	}

	public void setCombinedEndTimes(List<String> combinedEndTimes) {
		this.combinedEndTimes = combinedEndTimes;
	}

	public long getDeviceNo() {
		return deviceNo;
	}

	public void setDeviceNo(long deviceNo) {
		this.deviceNo = deviceNo;
	}

	public StreamedContent getExcelSheet() {
		return excelSheet;
	}

	public void setExcelSheet(StreamedContent excelSheet) {
		this.excelSheet = excelSheet;
	}

	public List<String> getRunningNoList() {
		return runningNoList;
	}

	public void setRunningNoList(List<String> runningNoList) {
		this.runningNoList = runningNoList;
	}

	public List<String> getRunningNoDetailsList() {
		return runningNoDetailsList;
	}

	public void setRunningNoDetailsList(List<String> runningNoDetailsList) {
		this.runningNoDetailsList = runningNoDetailsList;
	}

	public boolean isDisableSave() {
		return disableSave;
	}

	public void setDisableSave(boolean disableSave) {
		this.disableSave = disableSave;
	}

}
