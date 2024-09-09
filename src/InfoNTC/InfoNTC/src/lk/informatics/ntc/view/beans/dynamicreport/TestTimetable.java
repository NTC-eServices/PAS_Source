package lk.informatics.ntc.view.beans.dynamicreport;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.core.layout.LayoutManager;
import ar.com.fdvs.dj.domain.CustomExpression;
import ar.com.fdvs.dj.domain.DJGroupLabel;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.builders.GroupBuilder;
import ar.com.fdvs.dj.domain.constants.Border;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.GroupLayout;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.LabelPosition;
import ar.com.fdvs.dj.domain.constants.Page;
import ar.com.fdvs.dj.domain.constants.Transparency;
import ar.com.fdvs.dj.domain.constants.VerticalAlign;
import ar.com.fdvs.dj.domain.entities.DJGroup;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import ar.com.fdvs.dj.domain.entities.columns.PropertyColumn;
import ar.com.fdvs.dj.domain.entities.conditionalStyle.ConditionStyleExpression;
import ar.com.fdvs.dj.domain.entities.conditionalStyle.ConditionalStyle;
import javafx.scene.text.TextBoundsType;
//import junit.framework.TestCase;
import lk.informatics.ntc.model.dto.TimeTableDTO;
import lk.informatics.ntc.model.service.ReportService;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.ntc.view.util.SpringApplicationContex;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.view.JasperViewer;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.lowagie.text.pdf.fonts.cmaps.CMap;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author rve
 */
public class TestTimetable implements Serializable {

	private static final long serialVersionUID = 1L;
	private DynamicReportBuilder drb;
	private int year;
	private int startMonth;
	private int endMonth;
	private int total_no_of_columns;
	private int no_of_trip_per_day;

	private ReportService reportService;
	private TimeTableDTO timeTableDTO;

	public TestTimetable() {
	}

	public TestTimetable(int year, int startMonth, int endMonth, int total_no_of_columns) {
		super();
		this.year = year;
		this.startMonth = startMonth;
		this.endMonth = endMonth;
		this.total_no_of_columns = total_no_of_columns;
		this.timeTableDTO = this.getTimetableDet("176", "001", "O", "1");

		// reportService =
		// (ReportService)SpringApplicationContex.getBean("reportService");

		// reportService =
		// (ReportService)SpringApplicationContex.getBean("reportService");
		// List<String> startingTimeList =
		// reportService.getStartingTimeList("PGR2000002", "()", "1");

		try {
			drb = new DynamicReportBuilder();
			drb.setGrandTotalLegend("Total");
			drb.setPageSizeAndOrientation(new Page(585, 842));
			drb.setUseFullPageWidth(true);
			drb.setAllowDetailSplit(false);
			drb.setWhenNoData("No data", this.getGrayStyle(), true, true);
			// drb.setReportName("Test inner crosstab");
			ArrayList listCondStyle = getConditonalStyles();
			AbstractColumn columnState1 = ColumnBuilder.getNew().setColumnProperty("Month", String.class.getName())
					// .setTitle("Sales")
					.setWidth(50).setFixedWidth(true).setHeaderStyle(getHeaderStyle()).setStyle(getDataStyle())
					// .addConditionalStyles(listCondStyle)
					.build();
			drb.addColumn(columnState1);

			for (int i = 1; i < (this.total_no_of_columns + 1); i++) {
				List cslist = new ArrayList();
				cslist.add(listCondStyle.get(i - 1));
				AbstractColumn columnState2 = ColumnBuilder.getNew()
						.setColumnProperty("Col" + i, String.class.getName())
						// .setTitle("Col "+i)
						.setWidth(60).setFixedWidth(false).setHeaderStyle(getHeaderStyle()).setStyle(getDataStyle())
						.addConditionalStyles(cslist).build();
				drb.addColumn(columnState2);

			}
			AbstractColumn columnState3 = ColumnBuilder.getNew().setColumnProperty("Month2", String.class.getName())
					.setWidth(60).setFixedWidth(true).setHeaderStyle(getHeaderStyle()).setStyle(getDataStyle()).build();
			drb.addColumn(columnState3);
			AbstractColumn columnState4 = ColumnBuilder.getNew().setColumnProperty("Year", String.class.getName())
					.setWidth(60).setFixedWidth(true).setHeaderStyle(getHeaderStyle()).setStyle(getDataStyle()).build();
			drb.addColumn(columnState4);

			/*
			 * GroupBuilder gb1 = new GroupBuilder(); PropertyColumn col =
			 * (PropertyColumn) drb.getColumn(12); gb1.setCriteriaColumn(col);
			 * 
			 * 
			 * // ColumnsGroup g1 = gb1.addCriteriaColumn((PropertyColumn)
			 * columnState3) //
			 * .addGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS) //
			 * .build();
			 */

			GroupBuilder gb = new GroupBuilder();
			gb = gb.setCriteriaColumn((PropertyColumn) drb.getColumn(12));

			gb.setGroupLayout(GroupLayout.DEFAULT);
			gb.setAllowHeaderSplit(false);

			// Set footer label
			// DJGroupLabel ghLabel = new DJGroupLabel();
			// ghLabel.setText("Group Header Label");
			// ghLabel.setLabelPosition(LabelPosition.LEFT);
			// ghLabel.setStyle(this.getGreenStyle());

			// Set footer label
			DJGroupLabel gfLabel = new DJGroupLabel();
			gfLabel.setText("Group Label");
			gfLabel.setLabelPosition(LabelPosition.LEFT);
			gfLabel.setStyle(this.getGreenStyle());
			gb.setFooterLabel(gfLabel);

			// gb.setAllowFooterSplit(false);
			gb.setHeaderHeight(100);
			gb.setFooterHeight(new Integer(160));
			DJGroup g = gb.setGroupLayout(GroupLayout.DEFAULT_WITH_HEADER).build();

			drb.addGroup(g);

		} catch (ColumnBuilderException ex) {
			ex.printStackTrace();
			drb = null;
		}

	}

	public ReportService getReportService() {
		return reportService;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public DynamicReport buildDynamicReport() {
		return drb.build();
	}

	private static Style getRedStyle() {
		Style alertStyle = new Style();
		alertStyle.setTransparency(Transparency.OPAQUE);
		alertStyle.setBackgroundColor(Color.RED);
		alertStyle.setTextColor(Color.YELLOW);
		alertStyle.setVerticalAlign(VerticalAlign.TOP);
		return alertStyle;
	}

	private static Style getBlueStyle() {
		Style alertStyle = new Style();
		alertStyle.setTransparency(Transparency.OPAQUE);
		alertStyle.setBackgroundColor(Color.BLUE);
		alertStyle.setTextColor(Color.BLACK);
		alertStyle.setVerticalAlign(VerticalAlign.TOP);
		return alertStyle;
	}

	private static Style getGrayStyle() {
		Style alertStyle = new Style();
		alertStyle.setTransparency(Transparency.OPAQUE);
		alertStyle.setBackgroundColor(Color.LIGHT_GRAY);
		alertStyle.setTextColor(Color.BLACK);
		alertStyle.setVerticalAlign(VerticalAlign.TOP);
		return alertStyle;
	}

	private static Style getGreenStyle() {
		Style alertStyle = new Style();
		alertStyle.setTransparency(Transparency.OPAQUE);
		alertStyle.setBackgroundColor(Color.GREEN);
		alertStyle.setTextColor(Color.BLACK);
		alertStyle.setVerticalAlign(VerticalAlign.TOP);
		return alertStyle;
	}

	private static Style getYellowStyle() {
		Style alertStyle = new Style();
		alertStyle.setTransparency(Transparency.OPAQUE);
		alertStyle.setBackgroundColor(Color.YELLOW);
		alertStyle.setTextColor(Color.BLACK);
		alertStyle.setVerticalAlign(VerticalAlign.TOP);
		return alertStyle;
	}

	private static Style getOrangeStyle() {
		Style alertStyle = new Style();
		alertStyle.setTransparency(Transparency.OPAQUE);
		alertStyle.setBackgroundColor(Color.ORANGE);
		alertStyle.setTextColor(Color.BLACK);
		alertStyle.setVerticalAlign(VerticalAlign.TOP);
		return alertStyle;
	}

	private static Style getHeaderStyle() {
		Style headerStyle = new Style();
		headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
		headerStyle.setTransparency(Transparency.OPAQUE);
		headerStyle.setBackgroundColor(Color.BLUE);
		headerStyle.setTextColor(Color.WHITE);
		headerStyle.setVerticalAlign(VerticalAlign.TOP);

		headerStyle.setHorizontalAlign(HorizontalAlign.CENTER);
		return headerStyle;
	}

	private static Style getDataStyle() {
		Style dataStyle = new Style();
		dataStyle.setTransparency(Transparency.TRANSPARENT);
		dataStyle.setTextColor(Color.BLACK);
		dataStyle.setBorder(Border.THIN());
		dataStyle.setVerticalAlign(VerticalAlign.TOP);
		return dataStyle;
	}

	private ArrayList getConditonalStyles() {
		ArrayList conditionalStyles = new ArrayList();

		for (int i = 0; i < (this.total_no_of_columns + 1); i++) {
			FicheCondition fc = new FicheCondition("Col" + (i + 1), "1");
			ConditionalStyle cs = new ConditionalStyle(fc, getRedStyle());
			conditionalStyles.add(cs);
		}

		return conditionalStyles;
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

	public static LayoutManager getLayoutManager() {
		return new ClassicLayoutManager();
	}

	public Calendar convertStringToCalendar(String strDate) {
		// Calendar calendarDate = null;
		// try {
		int month = Integer.parseInt(strDate.substring(0, 2));
		int date = Integer.parseInt(strDate.substring(3, 5));
		int year = Integer.parseInt(strDate.substring(6));

		// Date utilDate=new
		// SimpleDateFormat("dd/MM/yyyy").parse(timeTableDTO.getStartTime());
		Calendar calendarDate = (Calendar) Calendar.getInstance();
		calendarDate.set(Calendar.MONTH, month - 1);
		calendarDate.set(Calendar.YEAR, year);
		calendarDate.set(Calendar.DAY_OF_MONTH, date);

		// calendarDate.set(utilDate.getYear(), utilDate.getMonth(),
		// utilDate.getDate());
		// calendarDate.set(month-1, date);
		// } catch (ParseException e) {
		// e.printStackTrace();
		// }
		return calendarDate;
	}

	public Object[][] displayDate() {

		List<String> startingTimesFromOrigin = this.getStartingTimeList(timeTableDTO.getGenereatedRefNo(), "O", "1");
		List<String> startingTimesFromDest = this.getStartingTimeList(timeTableDTO.getGenereatedRefNo(), "D", "1");
		startingTimesFromDest.add("test");

		// Calendar fromDate =
		// this.convertStringToCalendar(timeTableDTO.getStartTime());
		String startingDate = timeTableDTO.getStartTime();
		int from_month = Integer.parseInt(startingDate.substring(0, 2));
		int from_date = Integer.parseInt(startingDate.substring(3, 5));
		int from_year = Integer.parseInt(startingDate.substring(6));

		GregorianCalendar fromDate = new GregorianCalendar(from_year, from_month - 1, from_date);

		int d1 = fromDate.get(Calendar.DATE);
		int m1 = fromDate.get(Calendar.MONTH);
		int y1 = fromDate.get(Calendar.YEAR);

		// Calendar fromDate = (Calendar)Calendar.getInstance();
		// fromDate.set(Calendar.MONTH, from_month-1);
		// fromDate.set(Calendar.YEAR, from_year);
		// fromDate.set(Calendar.DAY_OF_MONTH, from_date);

		String endingDate = timeTableDTO.getEndTime();
		int to_month = Integer.parseInt(endingDate.substring(0, 2));
		int to_date = Integer.parseInt(endingDate.substring(3, 5));
		int to_year = Integer.parseInt(endingDate.substring(6));

		// GregorianCalendar toDate = new GregorianCalendar(to_year, to_month-1,
		// to_date);
		GregorianCalendar toDate = new GregorianCalendar(2020, 11, 31);

		int d2 = toDate.get(Calendar.DATE);
		int m2 = toDate.get(Calendar.MONTH);
		int y2 = toDate.get(Calendar.YEAR);

		// Calendar toDate = (Calendar)Calendar.getInstance();
		// toDate.set(Calendar.MONTH, to_month-1);
		// toDate.set(Calendar.YEAR, to_year);
		// toDate.set(Calendar.DAY_OF_MONTH, to_date);

		// Calendar toDate = (Calendar)Calendar.getInstance();
		// toDate.set(Calendar.MONTH, 1);
		// toDate.set(Calendar.YEAR, 2020);
		// toDate.set(Calendar.DAY_OF_MONTH, 2);
		//
		// toDate.get(Calendar.DAY_OF_MONTH);

		Object[][] data = new Object[12][this.total_no_of_columns + 2];
		// String date = fromDate.get(Calendar.DAY_OF_MONTH) + "";
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

		no_of_trip_per_day = startingTimesFromOrigin.size();

		int no_of_month = endMonth - startMonth + 1;
		Object[][] selectedData = new Object[no_of_month + no_of_trip_per_day + 2][this.total_no_of_columns + 2];
		for (int i = endMonth; i >= startMonth; i--) {
			selectedData[endMonth - i] = data[i];
		}

		selectedData[no_of_month][0] = "Origin";
		selectedData[no_of_month][this.total_no_of_columns + 1] = "Destination";
		selectedData[no_of_month + 1][0] = "Colpmbo";
		selectedData[no_of_month + 1][this.total_no_of_columns + 1] = "Galle";

		for (int i = 0; i < no_of_trip_per_day - 1; i++) {
			selectedData[no_of_month + i + 2][0] = startingTimesFromOrigin.get(i);
			List<String> busNos = getBusNos(timeTableDTO.getGenereatedRefNo(), "O", i + "", 10);
			for (int j = 0; j < busNos.size(); j++) {
				selectedData[no_of_month + i + 2][j + 1] = busNos.get(j);
			}
			selectedData[no_of_month + i + 2][this.total_no_of_columns + 1] = startingTimesFromDest.get(i + 1);

		}

		List listODate = this.getFirstDatesOfCellsInMont(selectedData[0]);
		return selectedData;
	}

	private List<String> getFirstDatesOfCellsInMont(Object[] month) {
		List<String> dateList = new ArrayList<String>();

		int len = month.length;
		for (int i = 1; i < len - 1; i++) {
			Object obj = month[i];
			if (obj != null) {
				String date = (String) obj;
				if (date.contains(",")) {
					int index = date.indexOf(",");
					date = date.substring(0, index);
				}
				System.out.print(date + "  ");
				dateList.add(date);
			}
		}
		return dateList;
	}

	public List getList() {
		List list = new ArrayList();

		int no_of_month = endMonth - startMonth + 1;
		// Object[][] data = new
		// Object[no_of_month+1][this.total_no_of_columns+2];
		Object[][] data = displayDate();
		for (int i = 0; i < no_of_month + no_of_trip_per_day + 2; i++) {
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

		for (int i = 0; i < no_of_month + no_of_trip_per_day + 2; i++) {
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

	public void test() throws JRException, FileNotFoundException {

		List list = getList();
		DynamicReport dynamicReport = buildDynamicReport();
		dynamicReport.setTemplateFileName("lk/informatics/ntc/view/reports/template_landscape_A4.jrxml");
		dynamicReport.setTemplateImportDatasets(true);
		dynamicReport.setTemplateImportFields(true);
		dynamicReport.setTemplateImportParameters(true);
		dynamicReport.setTemplateImportVariables(true);

		JRDataSource ds = new JRBeanCollectionDataSource(list);
		JasperPrint jp = DynamicJasperHelper.generateJasperPrint(dynamicReport, getLayoutManager(), ds);

		JasperViewer.viewReport(jp);

	}

	public JasperPrint getJasperPrint() throws JRException, FileNotFoundException {

		List list = getList();
		DynamicReport dynamicReport = buildDynamicReport();
		dynamicReport.setTemplateFileName("lk/informatics/ntc/view/reports/template_landscape_A4.jrxml");

		JRDataSource ds = new JRBeanCollectionDataSource(list);
		JasperPrint jp = DynamicJasperHelper.generateJasperPrint(dynamicReport, getLayoutManager(), ds);

		JasperViewer.viewReport(jp);

		return jp;
	}

	public List<String> getStartingTimeList(String genRefNo, String tripType, String groupNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String value = "";
		List<String> returnList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT start_time_slot,end_time_slot " + "FROM public.nt_m_timetable_generator_det "
					+ "WHERE generator_ref_no=? " + "AND trip_type=? " + "AND group_no=? " + "ORDER BY seq_no";

			ps = con.prepareStatement(query);
			ps.setString(1, genRefNo);
			ps.setString(2, tripType);
			ps.setString(3, groupNo);

			rs = ps.executeQuery();

			while (rs.next()) {

				if (tripType.equalsIgnoreCase("O")) {
					value = rs.getString("start_time_slot");
					returnList.add(value);
				} else if (tripType.equalsIgnoreCase("D")) {
					value = rs.getString("end_time_slot");
					returnList.add(value);
				}

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (ps != null)
					ps.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return returnList;

	}

	public TimeTableDTO getTimetableDet(String routeNo, String busCatCode, String tripType, String groupNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String value = "";
		List<String> returnList = new ArrayList<String>();

		TimeTableDTO timeTableDTO = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT rs_generator_ref_no,rs_start_date,rs_end_date "
					+ "FROM public.nt_m_route_schedule_generator " + "WHERE rs_route_no=? "
					+ "AND rs_bus_category_code=? " + "AND rs_trip_type=? " + "AND rs_group_no=? ";

			ps = con.prepareStatement(query);
			ps.setString(1, routeNo);
			ps.setString(2, busCatCode);
			ps.setString(3, tripType);
			ps.setString(4, groupNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				timeTableDTO = new TimeTableDTO();
				timeTableDTO.setGenereatedRefNo(rs.getString("rs_generator_ref_no"));
				timeTableDTO.setStartTime(rs.getString("rs_start_date"));
				timeTableDTO.setEndTime(rs.getString("rs_end_date"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (ps != null)
					ps.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return timeTableDTO;

	}

	public List<String> getBusNos(String genRefNo, String tripType, String tripId, int noOfDays) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String value = "";
		List<String> returnList = new ArrayList<String>();

		TimeTableDTO timeTableDTO = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT bus_no \r\n" + "FROM nt_t_route_schedule_generator_det01 \r\n"
					+ "WHERE generator_ref_no='PGR2000002' \r\n" + "AND trip_type='O' \r\n" + "AND trip_id='1'\r\n"
					+ "AND day_no <= 10\r\n" + "ORDER BY seq";

			ps = con.prepareStatement(query);
			// ps.setString(1, genRefNo);
			// ps.setString(2, tripType);
			// ps.setString(3, tripId);
			// ps.setInt(4, noOfDays);

			rs = ps.executeQuery();

			while (rs.next()) {
				returnList.add(rs.getString("bus_no"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (ps != null)
					ps.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return returnList;

	}

	public static void main(String[] args) throws Exception {

		TestTimetable test = new TestTimetable(2019, 0, 6, 10);
		test.test();
	}
}