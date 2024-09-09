package lk.informatics.ntc.view.beans;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import ar.com.fdvs.dj.core.DJConstants;
import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.core.layout.LayoutManager;
import ar.com.fdvs.dj.domain.CustomExpression;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.constants.Border;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.GroupLayout;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.Transparency;
import ar.com.fdvs.dj.domain.constants.VerticalAlign;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import ar.com.fdvs.dj.domain.entities.conditionalStyle.ConditionStyleExpression;
import ar.com.fdvs.dj.domain.entities.conditionalStyle.ConditionalStyle;
import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.LogSheetMaintenanceDTO;
import lk.informatics.ntc.model.dto.PageFormatDTO;
import lk.informatics.ntc.model.dto.TimeTableDTO;
import lk.informatics.ntc.model.service.CombineTimeTableGenerateService;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.DriverConductorTrainingService;
import lk.informatics.ntc.model.service.ReportService;
import lk.informatics.ntc.view.beans.dynamicreport.TestTimetable;
//import lk.informatics.ntc.view.beans.dynamicreport.RunningSheet.FicheCondition;
import lk.informatics.ntc.view.util.SpringApplicationContex;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 * @author tharushi.e
 *
 */
@ManagedBean(name = "runningSheetBackingBean")
@ViewScoped
public class RunningSheetBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private List<String> routeList = new ArrayList<String>();
	private List<LogSheetMaintenanceDTO> serviceList = new ArrayList<LogSheetMaintenanceDTO>();
	private String[] pageOrientationList = new String[] { "Portrait", "Landscape" };

	private String route = null;

	private String serviceType;
	private ReportService reportService;
	private DriverConductorTrainingService driverConductorTrainingService;
	private CommonService commonService;
	private StreamedContent files;
	private String origin;
	private String destination;
	private String depSide, tripType, groupNo, refNo, serviceCode, serviceTypeDes;
	private String year;
	private int startMonth = -1;
	private int endMonth = -1;
	private int total_no_of_columns;
	private int no_of_trip_per_day;
	private TestTimetable testTimetable;
	private TimeTableDTO timeTableDTO;
	private String columnsCount = null;
	private String dataColumnsWidth = null;
	private String monthColumnsWidth = null;
	private String pageOrientation;
	private String pageFormat;
	private String rowHeight;
	private String fontSize;

	private String sucessMsg, errorMsg;

	public String distance, travelTime, speed;
	/** Control sheet List **/
	private List<String> timeSlotList = new ArrayList<String>();
	private Map<String, List> busNoList;
	private List<String> groupList = new ArrayList<>();
	private List<String> departureList = new ArrayList<>();
	private List<String> refNoList = new ArrayList<>();
	private List<CommonDTO> monthList = new ArrayList<>();
	private boolean isControlSheetGet;

	private Map<String, Object> params;

	private List<PageFormatDTO> pageFormatList = new ArrayList<>();

	public List<PageFormatDTO> getPageFormatList() {
		return pageFormatList;
	}

	public void setPageFormatList(List<PageFormatDTO> pageFormatList) {
		this.pageFormatList = pageFormatList;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public int getStartMonth() {
		return startMonth;
	}

	public void setStartMonth(int startMonth) {
		this.startMonth = startMonth;
	}

	public int getEndMonth() {
		return endMonth;
	}

	public void setEndMonth(int endMonth) {
		this.endMonth = endMonth;
	}

	public String getPageFormat() {
		return pageFormat;
	}

	public void setPageFormat(String pageFormat) {
		this.pageFormat = pageFormat;
	}

	public String getDepSide() {
		return depSide;

	}

	public void setDepSide(String depSide) {
		this.depSide = depSide;
	}

	private CombineTimeTableGenerateService combineTimeTableGenerateService;

	@PostConstruct
	public void init() {
		reportService = (ReportService) SpringApplicationContex.getBean("reportService");

		driverConductorTrainingService = (DriverConductorTrainingService) SpringApplicationContex
				.getBean("driverConductorTrainingService");

		commonService = (CommonService) SpringApplicationContex.getBean("commonService");

		routeList = reportService.routeNoDropdown();
		serviceList = reportService.serviceTypeDropDown();
		refNoList = reportService.getRefferenceNo();
		monthList = driverConductorTrainingService.GetAllMonths();
		pageFormatList = commonService.getPageFormats();

		total_no_of_columns = 10;
		columnsCount = "10";
		monthColumnsWidth = "80";
		dataColumnsWidth = "70";
		fontSize = "10";
		params = new HashMap<String, Object>();

	}

	/**
	 * show origin and destination
	 */
	public void onRouteChange() {
		origin = reportService.getOrginByRoute(route);
		destination = reportService.getODestinationByRoute(route);

		// set orgin and destination
		departureList = new ArrayList<>();

		departureList.add(origin);
		departureList.add(destination);

	}

	private Style getHeaderStyle() {
		Style headerStyle = new Style();
		headerStyle.setFont(Font.ARIAL_MEDIUM);
		headerStyle.setTransparency(Transparency.OPAQUE);
		headerStyle.setBackgroundColor(Color.BLUE);
		headerStyle.setTextColor(Color.WHITE);
		headerStyle.setVerticalAlign(VerticalAlign.TOP);

		headerStyle.setHorizontalAlign(HorizontalAlign.CENTER);
		return headerStyle;
	}

	private Style getDataStyle() {
		Style dataStyle = new Style();
		dataStyle.setFont(Font.ARIAL_SMALL);
		dataStyle.getFont().setFontSize(Integer.parseInt(fontSize));
		dataStyle.setTransparency(Transparency.TRANSPARENT);
		dataStyle.setTextColor(Color.BLACK);
		dataStyle.setBorder(Border.THIN());
		dataStyle.setVerticalAlign(VerticalAlign.TOP);
		return dataStyle;
	}

	private static Style getRedStyle() {
		Style alertStyle = new Style();
		alertStyle.setTransparency(Transparency.OPAQUE);
		alertStyle.setBackgroundColor(Color.RED);
		alertStyle.setTextColor(Color.YELLOW);
		alertStyle.setVerticalAlign(VerticalAlign.TOP);
		return alertStyle;
	}

	private class FicheCondition extends ConditionStyleExpression implements CustomExpression {
		private String fieldName;
		private String colorValue;

		public FicheCondition(String fieldName, String colorValue) {
			this.fieldName = fieldName;
			this.colorValue = colorValue;
		}

		public Object evaluate(Map fields, Map variables, Map parameters) {
			boolean condition = false;
			Object currentValue = fields.get(fieldName);
			if (currentValue instanceof String) {
				String s = (String) currentValue;
				condition = colorValue.equals(s) || s.trim().startsWith("1,");
			}
			return condition;
		}

		public String getClassName() {
			return Boolean.class.getName();
		}
	}

	private ArrayList getConditonalStyles() {

		int columnsCountInt = Integer.parseInt(columnsCount);
		this.total_no_of_columns = columnsCountInt;
		ArrayList conditionalStyles = new ArrayList();

		for (int i = 0; i < (this.total_no_of_columns + 1); i++) {
			FicheCondition fc = new FicheCondition("Col" + (i + 1), "1");
			ConditionalStyle cs = new ConditionalStyle(fc, getRedStyle());
			conditionalStyles.add(cs);
		}

		return conditionalStyles;
	}

	public DynamicReport buildReport() throws Exception {

		FastReportBuilder drb = new FastReportBuilder();
		drb.setMargins(5, 5, 20, 20).setUseFullPageWidth(true);

		int columnsWidthInt = Integer.parseInt(dataColumnsWidth);
		int monthColumnsWidthInt = Integer.parseInt(monthColumnsWidth);

		ArrayList listCondStyle = getConditonalStyles();
		AbstractColumn columnYear = ColumnBuilder.getNew().setColumnProperty("Year", String.class.getName())
				.setWidth(columnsWidthInt).setFixedWidth(true).setHeaderStyle(getHeaderStyle()).setStyle(getDataStyle())
				.build();
		drb.addColumn(columnYear);
		AbstractColumn columnMonth1 = ColumnBuilder.getNew().setColumnProperty("Month", String.class.getName())
				.setWidth(monthColumnsWidthInt).setFixedWidth(true).setHeaderStyle(getHeaderStyle())
				.setStyle(getDataStyle()).build();
		drb.addColumn(columnMonth1);

		for (int i = 1; i < (this.total_no_of_columns + 1); i++) {
			List cslist = new ArrayList();
			cslist.add(listCondStyle.get(i - 1));
			AbstractColumn columnState2 = ColumnBuilder.getNew().setColumnProperty("Col" + i, String.class.getName())
					// .setTitle("Col "+i)
					.setWidth(columnsWidthInt).setFixedWidth(true).setHeaderStyle(getHeaderStyle())
					.setStyle(getDataStyle()).addConditionalStyles(cslist).build();
			drb.addColumn(columnState2);

		}
		AbstractColumn columnMonth2 = ColumnBuilder.getNew().setColumnProperty("Month2", String.class.getName())
				.setWidth(monthColumnsWidthInt).setFixedWidth(true).setHeaderStyle(getHeaderStyle())
				.setStyle(getDataStyle()).build();
		drb.addColumn(columnMonth2);

		drb.addGroups(1);
		drb.addField("headerlist", List.class.getName());

		DynamicReport drHeaderSubreport = createHeaderSubreport();
		drb.addSubreportInGroupHeader(1, drHeaderSubreport, new ClassicLayoutManager(), "headerlist",
				DJConstants.DATA_SOURCE_ORIGIN_FIELD, DJConstants.DATA_SOURCE_TYPE_COLLECTION);

		drb.getGroup(0).setReprintHeaderOnEachPage(true);
		drb.getGroup(0).setLayout(GroupLayout.EMPTY);

		DynamicReport dr = drb.build();
		return dr;
	}

	public Object[][] displayDate() {

		timeTableDTO = reportService.getTimetableDet("176", "001", "O", "1");

		String startingDate = timeTableDTO.getStartTime();
		int from_month = Integer.parseInt(startingDate.substring(0, 2));
		int from_date = Integer.parseInt(startingDate.substring(3, 5));
		int from_year = Integer.parseInt(startingDate.substring(6));

		GregorianCalendar fromDate = new GregorianCalendar(from_year, from_month - 1, from_date);

		int d1 = fromDate.get(Calendar.DATE);
		int m1 = fromDate.get(Calendar.MONTH);
		int y1 = fromDate.get(Calendar.YEAR);

		String endingDate = timeTableDTO.getEndTime();
		int to_month = Integer.parseInt(endingDate.substring(0, 2));
		int to_date = Integer.parseInt(endingDate.substring(3, 5));
		int to_year = Integer.parseInt(endingDate.substring(6));

		GregorianCalendar toDate = new GregorianCalendar(2020, 11, 31);

		int d2 = toDate.get(Calendar.DATE);
		int m2 = toDate.get(Calendar.MONTH);
		int y2 = toDate.get(Calendar.YEAR);

		Object[][] data = new Object[12][this.total_no_of_columns + 2];
		int col = 1;
		Calendar cal = fromDate;
		while (cal.before(toDate)) {
			int month = cal.get(Calendar.MONTH);

			String strMonth = "";
			switch (month) {
			case 0:
				strMonth = "Jan";
				break;
			case 1:
				strMonth = "Feb";
				break;
			case 2:
				strMonth = "Mar";
				break;
			case 3:
				strMonth = "Apr";
				break;
			case 4:
				strMonth = "May";
				break;
			case 5:
				strMonth = "Jun";
				break;
			case 6:
				strMonth = "Jul";
				break;
			case 7:
				strMonth = "Aug";
				break;
			case 8:
				strMonth = "Sep";
				break;
			case 9:
				strMonth = "Oct";
				break;
			case 10:
				strMonth = "Nov";
				break;
			case 11:
				strMonth = "Dec";
				break;
			}

			data[month][0] = strMonth;

			while (month == cal.get(Calendar.MONTH) && (cal.before(toDate) || cal.equals(toDate))) {
				if (data[month][col] == null || data[month][col].equals("")) {
					data[month][col] = cal.get(Calendar.DAY_OF_MONTH) + "";
				} else {
					data[month][col] = data[month][col] + "," + cal.get(Calendar.DAY_OF_MONTH);
				}

				cal.add(Calendar.DAY_OF_MONTH, 1);
				col = col + 1;
				if (col == (this.total_no_of_columns + 1)) {
					col = 1;
				}
			}
			data[month][total_no_of_columns + 1] = strMonth;

		}

		int no_of_month = endMonth - startMonth + 1;
		Object[][] selectedData = new Object[no_of_month + 2][this.total_no_of_columns + 2];
		for (int i = endMonth; i >= startMonth; i--) {
			selectedData[endMonth - i] = data[i];
		}

		selectedData[no_of_month][0] = "Origin";
		selectedData[no_of_month][this.total_no_of_columns + 1] = "Destination";
		if (depSide.equalsIgnoreCase(origin)) {
			selectedData[no_of_month + 1][0] = origin;
			selectedData[no_of_month + 1][this.total_no_of_columns + 1] = destination;
		} else {
			selectedData[no_of_month + 1][0] = destination;
			selectedData[no_of_month + 1][this.total_no_of_columns + 1] = origin;
		}

		return selectedData;
	}

	public Object[][] getDetialDate() {

		Object[][] selectedData = null;

		try {

			int columnsCountInt = Integer.parseInt(columnsCount);
			this.total_no_of_columns = columnsCountInt;

			timeTableDTO = reportService.getTimetableDet(route, serviceType, tripType, groupNo); // ("176",
																									// "001",
																									// "O",
																									// "1");

			List<String> startingTimesFromOrigin = null;
			List<String> startingTimesFromDest = null;

			if (depSide.equalsIgnoreCase(origin)) {
				startingTimesFromOrigin = reportService.getStartingTimeList(timeTableDTO.getGenereatedRefNo(), "O",
						groupNo);
				startingTimesFromDest = reportService.getStartingTimeList(timeTableDTO.getGenereatedRefNo(), "D",
						groupNo);
			}
			if (depSide.equalsIgnoreCase(destination)) {
				startingTimesFromOrigin = reportService.getStartingTimeList(timeTableDTO.getGenereatedRefNo(), "D",
						groupNo);
				startingTimesFromDest = reportService.getStartingTimeList(timeTableDTO.getGenereatedRefNo(), "O",
						groupNo);
			} else {
			}

			int min = 0;
			int max = 0;
			if (startingTimesFromOrigin.size() > startingTimesFromDest.size()) {
				max = startingTimesFromOrigin.size();
				min = startingTimesFromDest.size();
				for (int i = min; i < max; i++) {
					startingTimesFromDest.add("");
				}
			} else if (startingTimesFromOrigin.size() < startingTimesFromDest.size()) {
				max = startingTimesFromDest.size();
				min = startingTimesFromOrigin.size();
				for (int i = min; i < max; i++) {
					startingTimesFromOrigin.add("");
				}
			}

			no_of_trip_per_day = startingTimesFromOrigin.size();

			selectedData = new Object[no_of_trip_per_day][this.total_no_of_columns + 2];
			for (int i = 0; i < no_of_trip_per_day - 1; i++) {
				selectedData[i][0] = startingTimesFromOrigin.get(i);
				List<String> busNos = reportService.getBusNos(timeTableDTO.getGenereatedRefNo(), "O", i + "",
						total_no_of_columns);
				for (int j = 0; j < busNos.size(); j++) {
					selectedData[i][j + 1] = busNos.get(j);
				}
				selectedData[i][this.total_no_of_columns + 1] = startingTimesFromDest.get(i + 1);

			}

		} catch (NullPointerException e) {
			setErrorMsg("Data not Found. Error : " + e.getMessage());
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			e.printStackTrace();

		}

		return selectedData;

	}

	public List getSubList(Object[][] dataArray) {

		List list = new ArrayList();

		Object[][] data = dataArray;
		int length = data.length;
		for (int i = 0; i < length; i++) {
			Map result = new HashMap();
			result.put("Month", data[i][0]);
			System.out.print(data[i][0] + "    ");
			for (int j = 1; j < (this.total_no_of_columns + 1); j++) {
				result.put("Col" + j, data[i][j]);
				System.out.print(data[i][j] + "    ");
			}
			result.put("Month2", data[i][this.total_no_of_columns + 1]);
			result.put("Year", "2020");

			list.add(result);

		}

		return list;
	}

	public List getList(Object[][] dataArray) {

		List list = new ArrayList();

		Object[][] headerdata = this.displayDate();
		List sublist = getSubList(headerdata);

		Object[][] data = dataArray;
		int length = data.length;
		for (int i = 0; i < length; i++) {
			Map result = new HashMap();
			result.put("Month", data[i][0]);
			System.out.print(data[i][0] + "    ");
			for (int j = 1; j < (this.total_no_of_columns + 1); j++) {
				result.put("Col" + j, data[i][j]);
				System.out.print(data[i][j] + "    ");
			}
			result.put("Month2", data[i][this.total_no_of_columns + 1]);
			result.put("Year", "2020");
			result.put("headerlist", sublist);
			list.add(result);

		}

		return list;
	}

	private DynamicReport createHeaderSubreport() throws Exception {

		int columnsWidthInt = Integer.parseInt(dataColumnsWidth);
		int monthColumnsWidthInt = Integer.parseInt(monthColumnsWidth);

		FastReportBuilder drb = new FastReportBuilder();
		drb.setMargins(5, 5, 20, 20)

				.setMargins(0, 0, 0, 0).setUseFullPageWidth(true);

		ArrayList listCondStyle = getConditonalStyles();
		AbstractColumn columnYear = ColumnBuilder.getNew().setColumnProperty("Year", String.class.getName())
				.setWidth(50).setFixedWidth(true).setHeaderStyle(getHeaderStyle()).setStyle(getDataStyle()).build();
		drb.addColumn(columnYear);
		AbstractColumn columnMonth1 = ColumnBuilder.getNew().setColumnProperty("Month", String.class.getName())
				.setWidth(monthColumnsWidthInt).setFixedWidth(true).setHeaderStyle(getHeaderStyle())
				.setStyle(getDataStyle()).build();
		drb.addColumn(columnMonth1);

		for (int i = 1; i < (this.total_no_of_columns + 1); i++) {
			List cslist = new ArrayList();
			cslist.add(listCondStyle.get(i - 1));
			AbstractColumn columnState2 = ColumnBuilder.getNew().setColumnProperty("Col" + i, String.class.getName())
					// .setTitle("Col "+i)
					.setWidth(columnsWidthInt).setFixedWidth(true).setHeaderStyle(getHeaderStyle())
					.setStyle(getDataStyle()).addConditionalStyles(cslist).build();
			drb.addColumn(columnState2);

		}
		AbstractColumn columnMonth2 = ColumnBuilder.getNew().setColumnProperty("Month2", String.class.getName())
				.setWidth(monthColumnsWidthInt).setFixedWidth(true).setHeaderStyle(getHeaderStyle())
				.setStyle(getDataStyle()).build();
		drb.addColumn(columnMonth2);
		drb.addGroups(1);
		drb.getGroup(0).setLayout(GroupLayout.EMPTY);

		DynamicReport dr = drb.build();
		return dr;
	}

	private LayoutManager getLayoutManager() {
		// TODO Auto-generated method stub
		return null;
	}

	public JasperPrint getJasperPrint() throws Exception {

		String templateName = "template_portrait_A4.jrxml";
		if (pageFormat.equalsIgnoreCase("A4")) {
			if (pageOrientation.equalsIgnoreCase("PORTRAIT")) {
				templateName = "template_portrait_A4.jrxml";
			} else {
				templateName = "template_landscape_A4.jrxml";
			}

		}
		Object[][] data = this.getDetialDate();

		if (data == null || data.length == 0) {
			setErrorMsg("Data not Found.");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

			return null;

		} else {
			params.clear();
			params.put("logo_path", "lk/informatics/ntc/view/reports/");
			params.put("year", new Integer(year));
			params.put("route_no", new Integer(route));
			params.put("origin", origin);
			params.put("destination", destination);

			List list = getList(data);
			DynamicReport dynamicReport = buildReport();

			dynamicReport.setTemplateFileName("lk/informatics/ntc/view/reports/" + templateName);

			JRDataSource ds = new JRBeanCollectionDataSource(list);
			JasperPrint jp = DynamicJasperHelper.generateJasperPrint(dynamicReport, new ClassicLayoutManager(), ds,
					params);

			JasperViewer.viewReport(jp);

			return jp;
		}

	}

	public void printSheet(ActionEvent ae) throws Exception {

		// validate mandatory fields
		if (route.isEmpty() || serviceType.isEmpty() || depSide.isEmpty() || year.isEmpty() || startMonth == -1
				|| endMonth == -1 || pageFormat.isEmpty() || pageOrientation.isEmpty() || columnsCount.isEmpty()
				|| dataColumnsWidth.isEmpty() || monthColumnsWidth.isEmpty() || fontSize.isEmpty()) {

			setErrorMsg("Mandotory fields should be filled.");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

		else if (Integer.parseInt(year) < 2015) {
			setErrorMsg("Please Enter a Valid Year.");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

		else if (Integer.parseInt(columnsCount) < 1 || Integer.parseInt(dataColumnsWidth) < 5
				|| Integer.parseInt(monthColumnsWidth) < 5 || Integer.parseInt(fontSize) < 5) {
			setErrorMsg("Please Enter Valid values.");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

		// validate start month and end month values
		else if (startMonth > endMonth) {
			setErrorMsg("Please set valid start month and end month.");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

		else {

			// TestTimetable test = new TestTimetable(2019, 0, 6,10);
			try {
				JasperPrint jasperPrint = this.getJasperPrint();
				byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
				InputStream stream = new ByteArrayInputStream(pdfByteArray);
				files = new DefaultStreamedContent(stream, "Application/pdf", "running-sheet.pdf");

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NullPointerException e) {
				setErrorMsg("Data not Found.");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

			}

		}

	}

	public void clearSheet() {

		route = null;
		origin = null;
		destination = null;
		serviceType = null;
		depSide = null;
		year = null;
		startMonth = -1;
		endMonth = -1;
		pageFormat = null;
		pageOrientation = null;
		columnsCount = null;
		dataColumnsWidth = null;
		monthColumnsWidth = null;
		rowHeight = null;
		fontSize = null;

		serviceTypeDes = null;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public List<String> getRouteList() {
		return routeList;
	}

	public void setRouteList(List<String> routeList) {
		this.routeList = routeList;
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

	public String getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
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

	public List<String> getTimeSlotList() {
		return timeSlotList;
	}

	public void setTimeSlotList(List<String> timeSlotList) {
		this.timeSlotList = timeSlotList;
	}

	public Map<String, List> getBusNoList() {
		return busNoList;
	}

	public void setBusNoList(Map<String, List> busNoList) {
		this.busNoList = busNoList;
	}

	public List<String> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<String> groupList) {
		this.groupList = groupList;
	}

	public List<String> getDepartureList() {
		return departureList;
	}

	public void setDepartureList(List<String> departureList) {
		this.departureList = departureList;
	}

	public List<String> getRefNoList() {
		return refNoList;
	}

	public void setRefNoList(List<String> refNoList) {
		this.refNoList = refNoList;
	}

	public boolean isControlSheetGet() {
		return isControlSheetGet;
	}

	public void setControlSheetGet(boolean isControlSheetGet) {
		this.isControlSheetGet = isControlSheetGet;
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

	public List<LogSheetMaintenanceDTO> getServiceList() {
		return serviceList;
	}

	public void setServiceList(List<LogSheetMaintenanceDTO> serviceList) {
		this.serviceList = serviceList;
	}

	public DriverConductorTrainingService getDriverConductorTrainingService() {
		return driverConductorTrainingService;
	}

	public void setDriverConductorTrainingService(DriverConductorTrainingService driverConductorTrainingService) {
		this.driverConductorTrainingService = driverConductorTrainingService;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public List<CommonDTO> getMonthList() {
		return monthList;
	}

	public void setMonthList(List<CommonDTO> monthList) {
		this.monthList = monthList;
	}

	public String getColumnsCount() {
		return columnsCount;
	}

	public void setColumnsCount(String columnsCount) {
		this.columnsCount = columnsCount;
	}

	public String getDataColumnsWidth() {
		return dataColumnsWidth;
	}

	public void setDataColumnsWidth(String dataColumnsWidth) {
		this.dataColumnsWidth = dataColumnsWidth;
	}

	public String getPageOrientation() {
		return pageOrientation;
	}

	public void setPageOrientation(String pageOrientation) {
		this.pageOrientation = pageOrientation;
	}

	public String[] getPageOrientationList() {
		return pageOrientationList;
	}

	public void setPageOrientationList(String[] pageOrientationList) {
		this.pageOrientationList = pageOrientationList;
	}

	public int getTotal_no_of_columns() {
		return total_no_of_columns;
	}

	public void setTotal_no_of_columns(int total_no_of_columns) {
		this.total_no_of_columns = total_no_of_columns;
	}

	public String getSucessMsg() {
		return sucessMsg;
	}

	public void setSucessMsg(String sucessMsg) {
		this.sucessMsg = sucessMsg;
	}

	public String getMonthColumnsWidth() {
		return monthColumnsWidth;
	}

	public void setMonthColumnsWidth(String monthColumnsWidth) {
		this.monthColumnsWidth = monthColumnsWidth;
	}

	public String getRowHeight() {
		return rowHeight;
	}

	public void setRowHeight(String rowHeight) {
		this.rowHeight = rowHeight;
	}

	public String getFontSize() {
		return fontSize;
	}

	public void setFontSize(String fontSize) {
		this.fontSize = fontSize;
	}

}