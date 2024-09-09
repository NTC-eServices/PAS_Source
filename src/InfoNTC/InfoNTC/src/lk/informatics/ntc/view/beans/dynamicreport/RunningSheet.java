package lk.informatics.ntc.view.beans.dynamicreport;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.com.fdvs.dj.core.DJConstants;
import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.core.layout.LayoutManager;
import ar.com.fdvs.dj.domain.CustomExpression;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
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
import lk.informatics.ntc.model.dto.TimeTableDTO;
// import lk.informatics.ntc.view.beans.dynamicreport.TestTimetable.FicheCondition;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

public class RunningSheet implements Serializable {
	private static final long serialVersionUID = 1L;
	private DynamicReportBuilder drb;
	private int year;
	private int startMonth;
	private int endMonth;
	private int total_no_of_columns;
	private int no_of_trip_per_day;
	private TimeTableDTO timeTableDTO;
	private TestTimetable testTimetable;

	public RunningSheet() {
		super();
	}

	public RunningSheet(int year, int startMonth, int endMonth, int total_no_of_columns) {
		super();

		testTimetable = new TestTimetable();

		this.year = year;
		this.startMonth = startMonth;
		this.endMonth = endMonth;
		this.total_no_of_columns = total_no_of_columns;

		this.timeTableDTO = testTimetable.getTimetableDet("176", "001", "O", "1");

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
		drb.setMargins(5, 5, 20, 20).setTitle(this.year + " + Timetable Report")
				.setSubtitle("This report was generated at " + new Date()).setUseFullPageWidth(true);

		ArrayList listCondStyle = getConditonalStyles();
		AbstractColumn columnYear = ColumnBuilder.getNew().setColumnProperty("Year", String.class.getName())
				.setWidth(60).setFixedWidth(true).setHeaderStyle(getHeaderStyle()).setStyle(getDataStyle()).build();
		drb.addColumn(columnYear);
		AbstractColumn columnMonth1 = ColumnBuilder.getNew().setColumnProperty("Month", String.class.getName())

				.setWidth(50).setFixedWidth(true).setHeaderStyle(getHeaderStyle()).setStyle(getDataStyle()).build();
		drb.addColumn(columnMonth1);

		for (int i = 1; i < (this.total_no_of_columns + 1); i++) {
			List cslist = new ArrayList();
			cslist.add(listCondStyle.get(i - 1));
			AbstractColumn columnState2 = ColumnBuilder.getNew().setColumnProperty("Col" + i, String.class.getName())

					.setWidth(60).setFixedWidth(false).setHeaderStyle(getHeaderStyle()).setStyle(getDataStyle())
					.addConditionalStyles(cslist).build();
			drb.addColumn(columnState2);

		}
		AbstractColumn columnMonth2 = ColumnBuilder.getNew().setColumnProperty("Month2", String.class.getName())
				.setWidth(60).setFixedWidth(true).setHeaderStyle(getHeaderStyle()).setStyle(getDataStyle()).build();
		drb.addColumn(columnMonth2);

		drb.addGroups(1);
		drb.addField("headerlist", List.class.getName());

		DynamicReport drHeaderSubreport = createHeaderSubreport();
		drb.addSubreportInGroupHeader(1, drHeaderSubreport, new ClassicLayoutManager(), "headerlist",
				DJConstants.DATA_SOURCE_ORIGIN_FIELD, DJConstants.DATA_SOURCE_TYPE_COLLECTION);

		drb.getGroup(0).setReprintHeaderOnEachPage(true);
		drb.getGroup(0).setLayout(GroupLayout.EMPTY);

		drb.setUseFullPageWidth(true);

		DynamicReport dr = drb.build();
		return dr;
	}

	public Object[][] displayDate() {

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
		selectedData[no_of_month + 1][0] = "Colpmbo";
		selectedData[no_of_month + 1][this.total_no_of_columns + 1] = "Galle";

		return selectedData;
	}

	public Object[][] getDetialDate() {

		List<String> startingTimesFromOrigin = testTimetable.getStartingTimeList(timeTableDTO.getGenereatedRefNo(), "O",
				"1");
		List<String> startingTimesFromDest = testTimetable.getStartingTimeList(timeTableDTO.getGenereatedRefNo(), "D",
				"1");
		startingTimesFromDest.add("test");

		no_of_trip_per_day = startingTimesFromOrigin.size();

		Object[][] selectedData = new Object[no_of_trip_per_day][this.total_no_of_columns + 2];
		for (int i = 0; i < no_of_trip_per_day - 1; i++) {
			selectedData[i][0] = startingTimesFromOrigin.get(i);
			List<String> busNos = testTimetable.getBusNos(timeTableDTO.getGenereatedRefNo(), "O", i + "", 10);
			for (int j = 0; j < busNos.size(); j++) {
				selectedData[i][j + 1] = busNos.get(j);
			}
			selectedData[i][this.total_no_of_columns + 1] = startingTimesFromDest.get(i + 1);

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
		FastReportBuilder drb = new FastReportBuilder();
		drb.setMargins(5, 5, 20, 20).setTitle(this.year + " + Timetable Report")
				.setSubtitle("This report was generated at " + new Date()).setUseFullPageWidth(true);

		ArrayList listCondStyle = getConditonalStyles();
		AbstractColumn columnYear = ColumnBuilder.getNew().setColumnProperty("Year", String.class.getName())
				.setWidth(60).setFixedWidth(true).setHeaderStyle(getHeaderStyle()).setStyle(getDataStyle()).build();
		drb.addColumn(columnYear);
		AbstractColumn columnMonth1 = ColumnBuilder.getNew().setColumnProperty("Month", String.class.getName())

				.setWidth(50).setFixedWidth(true).setHeaderStyle(getHeaderStyle()).setStyle(getDataStyle()).build();
		drb.addColumn(columnMonth1);

		for (int i = 1; i < (this.total_no_of_columns + 1); i++) {
			List cslist = new ArrayList();
			cslist.add(listCondStyle.get(i - 1));
			AbstractColumn columnState2 = ColumnBuilder.getNew().setColumnProperty("Col" + i, String.class.getName())

					.setWidth(60).setFixedWidth(false).setHeaderStyle(getHeaderStyle()).setStyle(getDataStyle())
					.addConditionalStyles(cslist).build();
			drb.addColumn(columnState2);

		}
		AbstractColumn columnMonth2 = ColumnBuilder.getNew().setColumnProperty("Month2", String.class.getName())
				.setWidth(60).setFixedWidth(true).setHeaderStyle(getHeaderStyle()).setStyle(getDataStyle()).build();
		drb.addColumn(columnMonth2);
		drb.addGroups(1);
		drb.getGroup(0).setLayout(GroupLayout.EMPTY);

		drb.setUseFullPageWidth(true);

		DynamicReport dr = drb.build();
		return dr;
	}

	public void test() throws Exception {

		Object[][] data = this.getDetialDate();
		List list = getList(data);
		DynamicReport dynamicReport = buildReport();
		dynamicReport.setTemplateFileName("lk/informatics/ntc/view/reports/template_landscape_A4.jrxml");
		dynamicReport.setTemplateImportDatasets(true);
		dynamicReport.setTemplateImportFields(true);
		dynamicReport.setTemplateImportParameters(true);
		dynamicReport.setTemplateImportVariables(true);

		JRDataSource ds = new JRBeanCollectionDataSource(list);
		JasperPrint jp = DynamicJasperHelper.generateJasperPrint(dynamicReport, new ClassicLayoutManager(), ds);

		JasperViewer.viewReport(jp);

	}

	private LayoutManager getLayoutManager() {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args) throws Exception {
		RunningSheet runningSheet = new RunningSheet(2019, 0, 6, 10);
		runningSheet.test();

	}

}
