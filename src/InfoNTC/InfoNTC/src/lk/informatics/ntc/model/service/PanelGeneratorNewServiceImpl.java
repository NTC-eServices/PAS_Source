package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.PanelGeneratorNewDTO;
import lk.informatics.ntc.model.dto.TimeTableDTO;
import lk.informatics.ntc.view.beans.PanelGeneratorNewBean;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.roc.utils.common.Utils;

public class PanelGeneratorNewServiceImpl implements PanelGeneratorNewService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1372554334563918248L;

	public int insertPanel1(PanelGeneratorNewDTO panelGeneratorNewDTO, String user, String refNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_panelgenerator");
			panelGeneratorNewDTO.setSeqNo(seqNo);

			String sql = "INSERT INTO public.nt_m_panelgenerator\r\n"
					+ "(seq, route_no, bus_category, no_of_bus_perweek,ref_no,status,created_by, created_date)\r\n"
					+ "VALUES(?,?,?,?,?,?,?,?);\r\n" + "";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);

			stmt.setString(2, panelGeneratorNewDTO.getRouteNo());
			stmt.setString(3, panelGeneratorNewDTO.getBusCategory());
			stmt.setString(4, panelGeneratorNewDTO.getNoOfTimeTables());
			stmt.setString(5, refNo);
			stmt.setString(6, panelGeneratorNewDTO.getPanel1Status());
			stmt.setString(7, user);
			stmt.setTimestamp(8, timestamp);

			stmt.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return 0;
	}

	public String generateRefNo() {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;

		int year = Year.now().getValue();
		String currYear = Integer.toString(year).substring(2, 4);
		String strSurveyRequestNo = "";
		String lastyr = "00";

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT ref_no FROM public.nt_m_panelgenerator ORDER BY created_date desc LIMIT 1";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("ref_no");
			}

			if (result != null) {

				lastyr = result.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String ApprecordcountN = "00001";
					strSurveyRequestNo = "PGR" + currYear + ApprecordcountN;
				} else {
					String number = result.substring(5, 10);
					int returncountvalue = Integer.valueOf(number) + 1;

					String ApprecordcountN = "";

					if (returncountvalue >= 10 && returncountvalue < 100) {
						ApprecordcountN = "000" + returncountvalue;
					} else if (returncountvalue >= 100 && returncountvalue < 1000) {
						ApprecordcountN = "00" + returncountvalue;
					} else if (returncountvalue >= 1000 && returncountvalue < 10000) {
						ApprecordcountN = "0" + returncountvalue;
					} else if (returncountvalue >= 10000 && returncountvalue < 100000) {
						ApprecordcountN = Integer.toString(returncountvalue);
					} else {
						ApprecordcountN = "0000" + returncountvalue;
					}
					strSurveyRequestNo = "PGR" + currYear + ApprecordcountN;
				}
			} else
				strSurveyRequestNo = "PGR" + currYear + "00001";

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return strSurveyRequestNo;

	}

	public int group01_details(PanelGeneratorNewDTO panelGeneratorDTO, String user,
			List<PanelGeneratorNewDTO> group1_OD_List, List<PanelGeneratorNewDTO> group1_DO_List,
			List<String> dateList_Group1) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		String mon = "N", tue = "N", wed = "N", thu = "N", fri = "N", sat = "N", sun = "N";

		for (int i = 0; i < dateList_Group1.size(); i++) {
			if (dateList_Group1.get(i).equals("Monday")) {
				mon = "Y";
			}
			if (dateList_Group1.get(i).equals("Tuesday")) {
				tue = "Y";
			}
			if (dateList_Group1.get(i).equals("Wednesday")) {
				wed = "Y";
			}
			if (dateList_Group1.get(i).equals("Thursday")) {
				thu = "Y";
			}
			if (dateList_Group1.get(i).equals("Friday")) {
				fri = "Y";
			}
			if (dateList_Group1.get(i).equals("Saturday")) {
				sat = "Y";
			}
			if (dateList_Group1.get(i).equals("Sunday")) {
				sun = "Y";
			}
		}

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_panelgenerator_det");

			String sql = "INSERT INTO public.nt_t_panelgenerator_det\r\n"
					+ "(seq, seq_panelgenerator, group_no, bus_category, origin_start_time, origin_end_time, origin_divide_range, destination_start_time, destination_end_time, destination_divide_range, is_route_start, is_route_end, d_sunday, d_monday, d_tuesday, d_wednesday, d_thursday, d_friday, d_saturday, status, created_by, created_date)\r\n"
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);\r\n" + "" + "";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setLong(2, panelGeneratorDTO.getSeqNo());
			stmt.setString(3, "1");
			stmt.setString(4, panelGeneratorDTO.getBusCategory());

			SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
			String startTimeOD = localDateFormat.format(group1_OD_List.get(0).getGroup01_OD_StartTime());
			String endTimeOD = localDateFormat.format(group1_OD_List.get(0).getGroup01_OD_EndTime());
			String divideRangeOD = localDateFormat.format(group1_OD_List.get(0).getGroup01_OD_DivideRange());

			stmt.setString(5, startTimeOD);
			stmt.setString(6, endTimeOD);
			stmt.setString(7, divideRangeOD);

			String startTimeDO = localDateFormat.format(group1_DO_List.get(0).getGroup01_DO_StartTime());
			String endTimeDO = localDateFormat.format(group1_DO_List.get(0).getGroup01_DO_EndTime());
			String divideRangeDO = localDateFormat.format(group1_DO_List.get(0).getGroup01_DO_DivideRange());

			stmt.setString(8, startTimeDO);
			stmt.setString(9, endTimeDO);
			stmt.setString(10, divideRangeDO);

			stmt.setString(11, null);
			stmt.setString(12, null);

			// Days
			stmt.setString(13, sun);
			stmt.setString(14, mon);
			stmt.setString(15, tue);
			stmt.setString(16, wed);
			stmt.setString(17, thu);
			stmt.setString(18, fri);
			stmt.setString(19, sat);

			stmt.setString(20, "A");

			stmt.setString(21, user);
			stmt.setTimestamp(22, timestamp);

			stmt.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return 0;
	}

	public int group02_details(PanelGeneratorNewDTO panelGeneratorDTO, String user,
			List<PanelGeneratorNewDTO> group2_OD_List, List<PanelGeneratorNewDTO> group2_DO_List,
			List<String> dateList_Group2) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		String mon = "N", tue = "N", wed = "N", thu = "N", fri = "N", sat = "N", sun = "N";

		for (int i = 0; i < dateList_Group2.size(); i++) {
			if (dateList_Group2.get(i).equals("Monday")) {
				mon = "Y";
			}
			if (dateList_Group2.get(i).equals("Tuesday")) {
				tue = "Y";
			}
			if (dateList_Group2.get(i).equals("Wednesday")) {
				wed = "Y";
			}
			if (dateList_Group2.get(i).equals("Thursday")) {
				thu = "Y";
			}
			if (dateList_Group2.get(i).equals("Friday")) {
				fri = "Y";
			}
			if (dateList_Group2.get(i).equals("Saturday")) {
				sat = "Y";
			}
			if (dateList_Group2.get(i).equals("Sunday")) {
				sun = "Y";
			}
		}

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_panelgenerator_det");

			String sql = "INSERT INTO public.nt_t_panelgenerator_det\r\n"
					+ "(seq, seq_panelgenerator, group_no, bus_category, origin_start_time, origin_end_time, origin_divide_range, destination_start_time, destination_end_time, destination_divide_range, is_route_start, is_route_end, d_sunday, d_monday, d_tuesday, d_wednesday, d_thursday, d_friday, d_saturday, status, created_by, created_date)\r\n"
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);\r\n" + "" + "";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setLong(2, panelGeneratorDTO.getSeqNo());
			stmt.setString(3, "2");
			stmt.setString(4, panelGeneratorDTO.getBusCategory());

			SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
			String startTimeOD = localDateFormat.format(group2_OD_List.get(0).getGroup02_OD_StartTime());
			String endTimeOD = localDateFormat.format(group2_OD_List.get(0).getGroup02_OD_EndTime());
			String divideRangeOD = localDateFormat.format(group2_OD_List.get(0).getGroup02_OD_DivideRange());

			stmt.setString(5, startTimeOD);
			stmt.setString(6, endTimeOD);
			stmt.setString(7, divideRangeOD);

			String startTimeDO = localDateFormat.format(group2_DO_List.get(0).getGroup02_DO_StartTime());
			String endTimeDO = localDateFormat.format(group2_DO_List.get(0).getGroup02_DO_EndTime());
			String divideRangeDO = localDateFormat.format(group2_DO_List.get(0).getGroup02_DO_DivideRange());

			stmt.setString(8, startTimeDO);
			stmt.setString(9, endTimeDO);
			stmt.setString(10, divideRangeDO);

			stmt.setString(11, null);
			stmt.setString(12, null);

			// Days
			stmt.setString(13, sun);
			stmt.setString(14, mon);
			stmt.setString(15, tue);
			stmt.setString(16, wed);
			stmt.setString(17, thu);
			stmt.setString(18, fri);
			stmt.setString(19, sat);

			stmt.setString(20, "A");

			stmt.setString(21, user);
			stmt.setTimestamp(22, timestamp);

			stmt.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return 0;
	}

	public int group03_details(PanelGeneratorNewDTO panelGeneratorDTO, String user,
			List<PanelGeneratorNewDTO> group3_OD_List, List<PanelGeneratorNewDTO> group3_DO_List,
			List<String> dateList_Group3) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		String mon = "N", tue = "N", wed = "N", thu = "N", fri = "N", sat = "N", sun = "N";

		for (int i = 0; i < dateList_Group3.size(); i++) {
			if (dateList_Group3.get(i).equals("Monday")) {
				mon = "Y";
			}
			if (dateList_Group3.get(i).equals("Tuesday")) {
				tue = "Y";
			}
			if (dateList_Group3.get(i).equals("Wednesday")) {
				wed = "Y";
			}
			if (dateList_Group3.get(i).equals("Thursday")) {
				thu = "Y";
			}
			if (dateList_Group3.get(i).equals("Friday")) {
				fri = "Y";
			}
			if (dateList_Group3.get(i).equals("Saturday")) {
				sat = "Y";
			}
			if (dateList_Group3.get(i).equals("Sunday")) {
				sun = "Y";
			}
		}

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_panelgenerator_det");

			String sql = "INSERT INTO public.nt_t_panelgenerator_det\r\n"
					+ "(seq, seq_panelgenerator, group_no, bus_category, origin_start_time, origin_end_time, origin_divide_range, destination_start_time, destination_end_time, destination_divide_range, is_route_start, is_route_end, d_sunday, d_monday, d_tuesday, d_wednesday, d_thursday, d_friday, d_saturday, status, created_by, created_date)\r\n"
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);\r\n" + "" + "";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setLong(2, panelGeneratorDTO.getSeqNo());
			stmt.setString(3, "3");
			stmt.setString(4, panelGeneratorDTO.getBusCategory());

			SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
			String startTimeOD = localDateFormat.format(group3_OD_List.get(0).getGroup03_OD_StartTime());
			String endTimeOD = localDateFormat.format(group3_OD_List.get(0).getGroup03_OD_EndTime());
			String divideRangeOD = localDateFormat.format(group3_OD_List.get(0).getGroup03_OD_DivideRange());

			stmt.setString(5, startTimeOD);
			stmt.setString(6, endTimeOD);
			stmt.setString(7, divideRangeOD);

			String startTimeDO = localDateFormat.format(group3_DO_List.get(0).getGroup03_DO_StartTime());
			String endTimeDO = localDateFormat.format(group3_DO_List.get(0).getGroup03_DO_EndTime());
			String divideRangeDO = localDateFormat.format(group3_DO_List.get(0).getGroup03_DO_DivideRange());

			stmt.setString(8, startTimeDO);
			stmt.setString(9, endTimeDO);
			stmt.setString(10, divideRangeDO);

			stmt.setString(11, null);
			stmt.setString(12, null);

			// Days
			stmt.setString(13, sun);
			stmt.setString(14, mon);
			stmt.setString(15, tue);
			stmt.setString(16, wed);
			stmt.setString(17, thu);
			stmt.setString(18, fri);
			stmt.setString(19, sat);

			stmt.setString(20, "A");

			stmt.setString(21, user);
			stmt.setTimestamp(22, timestamp);

			stmt.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return 0;
	}

	public List<PanelGeneratorNewDTO> getRefNoList() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PanelGeneratorNewDTO> returnList = new ArrayList<PanelGeneratorNewDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT ref_no FROM public.nt_m_panelgenerator order by ref_no";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				PanelGeneratorNewDTO panelGeneratorNewDTO = new PanelGeneratorNewDTO();

				panelGeneratorNewDTO.setRefNo(rs.getString("ref_no"));

				returnList.add(panelGeneratorNewDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return returnList;

	}

	public List<PanelGeneratorNewDTO> getTripGeneratorList() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PanelGeneratorNewDTO> returnList = new ArrayList<PanelGeneratorNewDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT tg_generator_ref_no FROM public.nt_m_trips_generator";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				PanelGeneratorNewDTO panelGeneratorNewDTO = new PanelGeneratorNewDTO();

				panelGeneratorNewDTO.setRefNo(rs.getString("tg_generator_ref_no"));

				returnList.add(panelGeneratorNewDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return returnList;

	}

	public PanelGeneratorNewDTO getDetails(String refNo, PanelGeneratorNewDTO panelGeneratorNewDTO) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT seq,route_no,bus_category,no_of_bus_perweek,status FROM public.nt_m_panelgenerator where ref_no='"
					+ refNo + "'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				panelGeneratorNewDTO.setSeqNo(rs.getLong("seq"));
				panelGeneratorNewDTO.setRouteNo(rs.getString("route_no"));
				panelGeneratorNewDTO.setNoOfTimeTables(rs.getString("no_of_bus_perweek"));

				if (rs.getString("bus_category").equals("001")) {

					panelGeneratorNewDTO.setBusCategory("NORMAL");
				}

				else if (rs.getString("bus_category").equals("002")) {
					panelGeneratorNewDTO.setBusCategory("LUXURY");

				}

				else if (rs.getString("bus_category").equals("003")) {
					panelGeneratorNewDTO.setBusCategory("SUPER LUXURY");

				} else if (rs.getString("bus_category").equals("004")) {
					panelGeneratorNewDTO.setBusCategory("SEMI-LUXURY");

				} else if (rs.getString("bus_category").equals("EB")) {
					panelGeneratorNewDTO.setBusCategory("EXPRESSWAY BUS");

				}
				panelGeneratorNewDTO.setPanel1Status(rs.getString("status"));

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return panelGeneratorNewDTO;

	}

	public List<String> getDateList_Group1(Long seqNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<String> returnList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT d_monday,d_tuesday,d_wednesday,d_thursday,d_friday,d_saturday,d_sunday FROM public.nt_t_panelgenerator_det where group_no='1' and status='A'\r\n"
					+ "and seq_panelgenerator='" + seqNo + "' ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				String mon = rs.getString("d_monday");
				if (mon.equals("Y")) {

					mon = "Monday";
					returnList.add(mon);
				}
				String tue = rs.getString("d_tuesday");
				if (tue.equals("Y")) {

					tue = "Tuesday";
					returnList.add(tue);
				}
				String wed = rs.getString("d_wednesday");
				if (wed.equals("Y")) {

					wed = "Wednesday";
					returnList.add(wed);
				}
				String thu = rs.getString("d_thursday");
				if (thu.equals("Y")) {

					thu = "Thursday";
					returnList.add(thu);
				}
				String fri = rs.getString("d_friday");
				if (fri.equals("Y")) {

					fri = "Friday";
					returnList.add(fri);
				}
				String sat = rs.getString("d_saturday");
				if (sat.equals("Y")) {

					sat = "Saturday";
					returnList.add(sat);
				}
				String sun = rs.getString("d_sunday");
				if (sun.equals("Y")) {

					sun = "Sunday";
					returnList.add(sun);
				}

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return returnList;

	}

	public List<String> getDateList_Group2(Long seqNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<String> returnList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT d_monday,d_tuesday,d_wednesday,d_thursday,d_friday,d_saturday,d_sunday FROM public.nt_t_panelgenerator_det where group_no='2' and status='A'\r\n"
					+ "and seq_panelgenerator='" + seqNo + "' ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				String mon = rs.getString("d_monday");
				if (mon.equals("Y")) {

					mon = "Monday";
					returnList.add(mon);
				}
				String tue = rs.getString("d_tuesday");
				if (tue.equals("Y")) {

					tue = "Tuesday";
					returnList.add(tue);
				}
				String wed = rs.getString("d_wednesday");
				if (wed.equals("Y")) {

					wed = "Wednesday";
					returnList.add(wed);
				}
				String thu = rs.getString("d_thursday");
				if (thu.equals("Y")) {

					thu = "Thursday";
					returnList.add(thu);
				}
				String fri = rs.getString("d_friday");
				if (fri.equals("Y")) {

					fri = "Friday";
					returnList.add(fri);
				}
				String sat = rs.getString("d_saturday");
				if (sat.equals("Y")) {

					sat = "Saturday";
					returnList.add(sat);
				}
				String sun = rs.getString("d_sunday");
				if (sun.equals("Y")) {

					sun = "Sunday";
					returnList.add(sun);
				}

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return returnList;

	}

	public List<String> getDateList_Group3(Long seqNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<String> returnList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT d_monday,d_tuesday,d_wednesday,d_thursday,d_friday,d_saturday,d_sunday FROM public.nt_t_panelgenerator_det where group_no='3' and status='A'\r\n"
					+ "and seq_panelgenerator='" + seqNo + "' ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				String mon = rs.getString("d_monday");
				if (mon.equals("Y")) {

					mon = "Monday";
					returnList.add(mon);
				}
				String tue = rs.getString("d_tuesday");
				if (tue.equals("Y")) {

					tue = "Tuesday";
					returnList.add(tue);
				}
				String wed = rs.getString("d_wednesday");
				if (wed.equals("Y")) {

					wed = "Wednesday";
					returnList.add(wed);
				}
				String thu = rs.getString("d_thursday");
				if (thu.equals("Y")) {

					thu = "Thursday";
					returnList.add(thu);
				}
				String fri = rs.getString("d_friday");
				if (fri.equals("Y")) {

					fri = "Friday";
					returnList.add(fri);
				}
				String sat = rs.getString("d_saturday");
				if (sat.equals("Y")) {

					sat = "Saturday";
					returnList.add(sat);
				}
				String sun = rs.getString("d_sunday");
				if (sun.equals("Y")) {

					sun = "Sunday";
					returnList.add(sun);
				}

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return returnList;

	}

	public List<PanelGeneratorNewDTO> getODList_Group1(Long seqNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PanelGeneratorNewDTO> returnList = new ArrayList<PanelGeneratorNewDTO>();
		PanelGeneratorNewDTO panelGeneratorDTO = new PanelGeneratorNewDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT origin_start_time,origin_end_time,origin_divide_range FROM public.nt_t_panelgenerator_det where seq_panelgenerator='"
					+ seqNo + "' and group_no='1' and status='A'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
				String startTime = rs.getString("origin_start_time");
				String endTime = rs.getString("origin_end_time");
				String divideRange = rs.getString("origin_divide_range");

				panelGeneratorDTO.setGroup01_OD_StartTime(localDateFormat.parse(startTime));
				panelGeneratorDTO.setGroup01_OD_EndTime(localDateFormat.parse(endTime));
				panelGeneratorDTO.setGroup01_OD_DivideRange(localDateFormat.parse(divideRange));

				returnList.add(panelGeneratorDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return returnList;

	}

	public List<PanelGeneratorNewDTO> getODList_Group2(Long seqNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PanelGeneratorNewDTO> returnList = new ArrayList<PanelGeneratorNewDTO>();
		PanelGeneratorNewDTO panelGeneratorDTO = new PanelGeneratorNewDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT origin_start_time,origin_end_time,origin_divide_range FROM public.nt_t_panelgenerator_det where seq_panelgenerator='"
					+ seqNo + "' and group_no='2' and status='A'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
				String startTime = rs.getString("origin_start_time");
				String endTime = rs.getString("origin_end_time");
				String divideRange = rs.getString("origin_divide_range");

				panelGeneratorDTO.setGroup02_OD_StartTime(localDateFormat.parse(startTime));
				panelGeneratorDTO.setGroup02_OD_EndTime(localDateFormat.parse(endTime));
				panelGeneratorDTO.setGroup02_OD_DivideRange(localDateFormat.parse(divideRange));

				returnList.add(panelGeneratorDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return returnList;

	}

	public List<PanelGeneratorNewDTO> getODList_Group3(Long seqNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PanelGeneratorNewDTO> returnList = new ArrayList<PanelGeneratorNewDTO>();
		PanelGeneratorNewDTO panelGeneratorNewDTO = new PanelGeneratorNewDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT origin_start_time,origin_end_time,origin_divide_range FROM public.nt_t_panelgenerator_det where seq_panelgenerator='"
					+ seqNo + "' and group_no='3' and status='A'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
				String startTime = rs.getString("origin_start_time");
				String endTime = rs.getString("origin_end_time");
				String divideRange = rs.getString("origin_divide_range");

				panelGeneratorNewDTO.setGroup03_OD_StartTime(localDateFormat.parse(startTime));
				panelGeneratorNewDTO.setGroup03_OD_EndTime(localDateFormat.parse(endTime));
				panelGeneratorNewDTO.setGroup03_OD_DivideRange(localDateFormat.parse(divideRange));

				returnList.add(panelGeneratorNewDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return returnList;

	}

	public List<PanelGeneratorNewDTO> getDOList_Group1(Long seqNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PanelGeneratorNewDTO> returnList = new ArrayList<PanelGeneratorNewDTO>();
		PanelGeneratorNewDTO panelGeneratorNewDTO = new PanelGeneratorNewDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT destination_start_time,destination_end_time,destination_divide_range FROM public.nt_t_panelgenerator_det where seq_panelgenerator='"
					+ seqNo + "' and group_no='1' and status='A'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
				String startTime = rs.getString("destination_start_time");
				String endTime = rs.getString("destination_end_time");
				String divideRange = rs.getString("destination_divide_range");

				panelGeneratorNewDTO.setGroup01_DO_StartTime(localDateFormat.parse(startTime));
				panelGeneratorNewDTO.setGroup01_DO_EndTime(localDateFormat.parse(endTime));
				panelGeneratorNewDTO.setGroup01_DO_DivideRange(localDateFormat.parse(divideRange));

				returnList.add(panelGeneratorNewDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return returnList;

	}

	public List<PanelGeneratorNewDTO> getDOList_Group2(Long seqNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PanelGeneratorNewDTO> returnList = new ArrayList<PanelGeneratorNewDTO>();
		PanelGeneratorNewDTO panelGeneratorDTO = new PanelGeneratorNewDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT destination_start_time,destination_end_time,destination_divide_range FROM public.nt_t_panelgenerator_det where seq_panelgenerator='"
					+ seqNo + "' and group_no='2' and status='A'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
				String startTime = rs.getString("destination_start_time");
				String endTime = rs.getString("destination_end_time");
				String divideRange = rs.getString("destination_divide_range");

				panelGeneratorDTO.setGroup02_DO_StartTime(localDateFormat.parse(startTime));
				panelGeneratorDTO.setGroup02_DO_EndTime(localDateFormat.parse(endTime));
				panelGeneratorDTO.setGroup02_DO_DivideRange(localDateFormat.parse(divideRange));

				returnList.add(panelGeneratorDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return returnList;

	}

	public List<PanelGeneratorNewDTO> getDOList_Group3(Long seqNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PanelGeneratorNewDTO> returnList = new ArrayList<PanelGeneratorNewDTO>();
		PanelGeneratorNewDTO panelGeneratorDTO = new PanelGeneratorNewDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT destination_start_time,destination_end_time,destination_divide_range FROM public.nt_t_panelgenerator_det where seq_panelgenerator='"
					+ seqNo + "' and group_no='3' and status='A'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
				String startTime = rs.getString("destination_start_time");
				String endTime = rs.getString("destination_end_time");
				String divideRange = rs.getString("destination_divide_range");

				panelGeneratorDTO.setGroup03_DO_StartTime(localDateFormat.parse(startTime));
				panelGeneratorDTO.setGroup03_DO_EndTime(localDateFormat.parse(endTime));
				panelGeneratorDTO.setGroup03_DO_DivideRange(localDateFormat.parse(divideRange));

				returnList.add(panelGeneratorDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return returnList;

	}

	public int updateGroup1_OriginToDestination(Long seqNo, String user, List<PanelGeneratorNewDTO> group1_OD_List) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
		String startTimeOD = localDateFormat.format(group1_OD_List.get(0).getGroup01_OD_StartTime());
		String endTimeOD = localDateFormat.format(group1_OD_List.get(0).getGroup01_OD_EndTime());
		String divideRangeOD = localDateFormat.format(group1_OD_List.get(0).getGroup01_OD_DivideRange());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_panelgenerator_det\r\n" + "SET origin_start_time='" + startTimeOD
					+ "', origin_end_time='" + endTimeOD + "', origin_divide_range='" + divideRangeOD
					+ "',modified_by='" + user + "', modified_date='" + timestamp + "' where seq_panelgenerator='"
					+ seqNo + "' and group_no='1'";

			stmt = con.prepareStatement(sql);

			stmt.executeUpdate();
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return 0;
	}

	public int updateGroup2_OriginToDestination(Long seqNo, String user, List<PanelGeneratorNewDTO> group2_OD_List) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
		String startTimeOD = localDateFormat.format(group2_OD_List.get(0).getGroup02_OD_StartTime());
		String endTimeOD = localDateFormat.format(group2_OD_List.get(0).getGroup02_OD_EndTime());
		String divideRangeOD = localDateFormat.format(group2_OD_List.get(0).getGroup02_OD_DivideRange());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_panelgenerator_det\r\n" + "SET origin_start_time='" + startTimeOD
					+ "', origin_end_time='" + endTimeOD + "', origin_divide_range='" + divideRangeOD
					+ "',modified_by='" + user + "', modified_date='" + timestamp + "' where seq_panelgenerator='"
					+ seqNo + "' and group_no='2'";

			stmt = con.prepareStatement(sql);

			stmt.executeUpdate();
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return 0;
	}

	public int updateGroup3_OriginToDestination(Long seqNo, String user, List<PanelGeneratorNewDTO> group3_OD_List) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
		String startTimeOD = localDateFormat.format(group3_OD_List.get(0).getGroup03_OD_StartTime());
		String endTimeOD = localDateFormat.format(group3_OD_List.get(0).getGroup03_OD_EndTime());
		String divideRangeOD = localDateFormat.format(group3_OD_List.get(0).getGroup03_OD_DivideRange());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_panelgenerator_det\r\n" + "SET origin_start_time='" + startTimeOD
					+ "', origin_end_time='" + endTimeOD + "', origin_divide_range='" + divideRangeOD
					+ "',modified_by='" + user + "', modified_date='" + timestamp + "' where seq_panelgenerator='"
					+ seqNo + "' and group_no='3'";

			stmt = con.prepareStatement(sql);

			stmt.executeUpdate();
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return 0;
	}

	public int updateGroup1_DestinationToOrigin(Long seqNo, String user, List<PanelGeneratorNewDTO> group1_DO_List) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
		String startTimeDO = localDateFormat.format(group1_DO_List.get(0).getGroup01_DO_StartTime());
		String endTimeDO = localDateFormat.format(group1_DO_List.get(0).getGroup01_DO_EndTime());
		String divideRangeDO = localDateFormat.format(group1_DO_List.get(0).getGroup01_DO_DivideRange());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_panelgenerator_det\r\n" + "SET destination_start_time='" + startTimeDO
					+ "', destination_end_time='" + endTimeDO + "', destination_divide_range='" + divideRangeDO
					+ "',modified_by='" + user + "', modified_date='" + timestamp + "' where seq_panelgenerator='"
					+ seqNo + "' and group_no='1'";

			stmt = con.prepareStatement(sql);

			stmt.executeUpdate();
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return 0;
	}

	public int updateGroup2_DestinationToOrigin(Long seqNo, String user, List<PanelGeneratorNewDTO> group2_DO_List) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
		String startTimeDO = localDateFormat.format(group2_DO_List.get(0).getGroup02_DO_StartTime());
		String endTimeDO = localDateFormat.format(group2_DO_List.get(0).getGroup02_DO_EndTime());
		String divideRangeDO = localDateFormat.format(group2_DO_List.get(0).getGroup02_DO_DivideRange());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_panelgenerator_det\r\n" + "SET destination_start_time='" + startTimeDO
					+ "', destination_end_time='" + endTimeDO + "', destination_divide_range='" + divideRangeDO
					+ "',modified_by='" + user + "', modified_date='" + timestamp + "' where seq_panelgenerator='"
					+ seqNo + "' and group_no='2'";

			stmt = con.prepareStatement(sql);

			stmt.executeUpdate();
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return 0;
	}

	public int updateGroup3_DestinationToOrigin(Long seqNo, String user, List<PanelGeneratorNewDTO> group3_DO_List) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
		String startTimeDO = localDateFormat.format(group3_DO_List.get(0).getGroup03_DO_StartTime());
		String endTimeDO = localDateFormat.format(group3_DO_List.get(0).getGroup03_DO_EndTime());
		String divideRangeDO = localDateFormat.format(group3_DO_List.get(0).getGroup03_DO_DivideRange());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_panelgenerator_det\r\n" + "SET destination_start_time='" + startTimeDO
					+ "', destination_end_time='" + endTimeDO + "', destination_divide_range='" + divideRangeDO
					+ "',modified_by='" + user + "', modified_date='" + timestamp + "' where seq_panelgenerator='"
					+ seqNo + "' and group_no='3'";

			stmt = con.prepareStatement(sql);

			stmt.executeUpdate();
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return 0;
	}

	public int updateDateList_Group01(Long seqNo, String user, List<String> dateList_Group1) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		String mon = "N", tue = "N", wed = "N", thu = "N", fri = "N", sat = "N", sun = "N";

		for (int i = 0; i < dateList_Group1.size(); i++) {
			if (dateList_Group1.get(i).equals("Monday")) {
				mon = "Y";
			}
			if (dateList_Group1.get(i).equals("Tuesday")) {
				tue = "Y";
			}
			if (dateList_Group1.get(i).equals("Wednesday")) {
				wed = "Y";
			}
			if (dateList_Group1.get(i).equals("Thursday")) {
				thu = "Y";
			}
			if (dateList_Group1.get(i).equals("Friday")) {
				fri = "Y";
			}
			if (dateList_Group1.get(i).equals("Saturday")) {
				sat = "Y";
			}
			if (dateList_Group1.get(i).equals("Sunday")) {
				sun = "Y";
			}
		}

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_panelgenerator_det\r\n" + "SET d_sunday='" + sun + "', d_monday='" + mon
					+ "', d_tuesday='" + tue + "', d_wednesday='" + wed + "', d_thursday='" + thu + "', d_friday='"
					+ fri + "', d_saturday='" + sat + "',modified_by='" + user + "', modified_date='" + timestamp
					+ "'\r\n" + "where seq_panelgenerator='" + seqNo + "' and group_no='1'";

			stmt = con.prepareStatement(sql);

			stmt.executeUpdate();
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return 0;
	}

	public int updateDateList_Group02(Long seqNo, String user, List<String> dateList_Group2) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		String mon = "N", tue = "N", wed = "N", thu = "N", fri = "N", sat = "N", sun = "N";

		for (int i = 0; i < dateList_Group2.size(); i++) {
			if (dateList_Group2.get(i).equals("Monday")) {
				mon = "Y";
			}
			if (dateList_Group2.get(i).equals("Tuesday")) {
				tue = "Y";
			}
			if (dateList_Group2.get(i).equals("Wednesday")) {
				wed = "Y";
			}
			if (dateList_Group2.get(i).equals("Thursday")) {
				thu = "Y";
			}
			if (dateList_Group2.get(i).equals("Friday")) {
				fri = "Y";
			}
			if (dateList_Group2.get(i).equals("Saturday")) {
				sat = "Y";
			}
			if (dateList_Group2.get(i).equals("Sunday")) {
				sun = "Y";
			}
		}

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_panelgenerator_det\r\n" + "SET d_sunday='" + sun + "', d_monday='" + mon
					+ "', d_tuesday='" + tue + "', d_wednesday='" + wed + "', d_thursday='" + thu + "', d_friday='"
					+ fri + "', d_saturday='" + sat + "',modified_by='" + user + "', modified_date='" + timestamp
					+ "'\r\n" + "where seq_panelgenerator='" + seqNo + "' and group_no='2'";

			stmt = con.prepareStatement(sql);

			stmt.executeUpdate();
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return 0;
	}

	public int updateDateList_Group03(Long seqNo, String user, List<String> dateList_Group3) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		String mon = "N", tue = "N", wed = "N", thu = "N", fri = "N", sat = "N", sun = "N";

		for (int i = 0; i < dateList_Group3.size(); i++) {
			if (dateList_Group3.get(i).equals("Monday")) {
				mon = "Y";
			}
			if (dateList_Group3.get(i).equals("Tuesday")) {
				tue = "Y";
			}
			if (dateList_Group3.get(i).equals("Wednesday")) {
				wed = "Y";
			}
			if (dateList_Group3.get(i).equals("Thursday")) {
				thu = "Y";
			}
			if (dateList_Group3.get(i).equals("Friday")) {
				fri = "Y";
			}
			if (dateList_Group3.get(i).equals("Saturday")) {
				sat = "Y";
			}
			if (dateList_Group3.get(i).equals("Sunday")) {
				sun = "Y";
			}
		}

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_panelgenerator_det\r\n" + "SET d_sunday='" + sun + "', d_monday='" + mon
					+ "', d_tuesday='" + tue + "', d_wednesday='" + wed + "', d_thursday='" + thu + "', d_friday='"
					+ fri + "', d_saturday='" + sat + "',modified_by='" + user + "', modified_date='" + timestamp
					+ "'\r\n" + "where seq_panelgenerator='" + seqNo + "' and group_no='3'";

			stmt = con.prepareStatement(sql);

			stmt.executeUpdate();
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return 0;
	}

	public List<CommonDTO> getRoutesToDropdown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  distinct rc_route_no FROM public.nt_t_route_creator where rc_status='A'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("rc_route_no"));

				returnList.add(commonDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return returnList;

	}

	public int inActiveGroup02(Long seqNo, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_panelgenerator_det\r\n" + "SET  status='I', modified_by='" + user
					+ "', modified_date='" + timestamp + "'\r\n" + "where seq_panelgenerator='" + seqNo
					+ "' and group_no='2'\r\n" + "";

			stmt = con.prepareStatement(sql);

			stmt.executeUpdate();
			con.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return 0;
	}

	public int inActiveGroup03(Long seqNo, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_panelgenerator_det\r\n" + "SET  status='I', modified_by='" + user
					+ "', modified_date='" + timestamp + "'\r\n" + "where seq_panelgenerator='" + seqNo
					+ "' and group_no='3'\r\n" + "";

			stmt = con.prepareStatement(sql);

			stmt.executeUpdate();
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return 0;
	}

	public int updatePanelGenerator(String timeTables, String user, String refNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_panelgenerator\r\n" + "SET no_of_bus_perweek='" + timeTables
					+ "', modified_by='" + user + "', modified_date='" + timestamp + "'\r\n" + "WHERE ref_no='" + refNo
					+ "'\r\n" + "";

			stmt = con.prepareStatement(sql);

			stmt.executeUpdate();
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return 0;
	}

	public int updateStatus(String status, String user, String refNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_panelgenerator\r\n" + "SET status='" + status + "', modified_by='" + user
					+ "', modified_date='" + timestamp + "'\r\n" + "WHERE ref_no='" + refNo + "';\r\n" + "";

			stmt = con.prepareStatement(sql);

			stmt.executeUpdate();
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return 0;
	}

	public List<CommonDTO> getSelectedRoutes() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT route_no FROM public.nt_m_panelgenerator where status='A' order by route_no";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("route_no"));

				returnList.add(commonDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return returnList;

	}

	@Override
	public boolean getActiveDataRecord(String routeNo, String serviceType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean dataHave = false;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM public.nt_m_panelgenerator where status='A' and  route_no =? and bus_category =?";

			ps = con.prepareStatement(query);
			ps.setString(1, routeNo);
			ps.setString(2, serviceType);
			rs = ps.executeQuery();

			while (rs.next()) {
				dataHave = true;

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return dataHave;

	}

	@Override
	public void saveOriginDestinationData(PanelGeneratorNewBean panelGeneratorNewBean) {
		System.out.println("Entered to save method");

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_panelgenerator");

			String sql = "INSERT INTO panel_generator_origin\r\n"
					+ "(noOfTrips, noOfPrivateBuses, noOfTemporaryBuses, noOfCtb,noOfEtc,noOfPvtLeaveBuses,noOfDummyBuses, restTime,totalBuses,Abbreviation,permitNo,busNo,startTime,endTime,fixBus)\r\n"
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);\r\n" + "";

			stmt = con.prepareStatement(sql);

			stmt.setInt(1, panelGeneratorNewBean.getNoOfTripsOrigin());
			stmt.setInt(2, panelGeneratorNewBean.getNoOfPvtBusesOrigin());
			stmt.setInt(3, panelGeneratorNewBean.getNoOfTemporaryBusesOrigin());
			stmt.setInt(4, panelGeneratorNewBean.getNoOfCtbOrigin());
			stmt.setInt(5, panelGeneratorNewBean.getNoOfEtcOrigin());
			stmt.setInt(6, panelGeneratorNewBean.getNoOfPvtLeaveBusesOrigin());
			stmt.setInt(7, panelGeneratorNewBean.getNoOfDummyBusesOrigin());
			stmt.setString(8, panelGeneratorNewBean.getRestTimeOrigin());
			stmt.setInt(9, panelGeneratorNewBean.getTotalBusesOrigin());
			stmt.setString(10, panelGeneratorNewBean.getAbbreviationOrigin());
			stmt.setString(11, panelGeneratorNewBean.getPermitNoOrigin());
			stmt.setString(12, panelGeneratorNewBean.getBusNoOrigin());
			stmt.setString(13, panelGeneratorNewBean.getStartTimeOrigin());
			stmt.setString(14, panelGeneratorNewBean.getEndTimeOrigin());
			stmt.setBoolean(15, panelGeneratorNewBean.isFixBusOrigin());

			stmt.executeUpdate();

			con.commit();
			
			getTrips();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

	}
	
	

	@Override
	public List<TimeTableDTO> getTrips() {
		System.out.println("started getTrips");
		List<TimeTableDTO> tripList = new ArrayList<>();

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
            con = ConnectionManager.getConnection();

            String sql = "SELECT * FROM panel_generator_origin";

            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
            	TimeTableDTO tripDTO = new TimeTableDTO();
            	
            	tripDTO.setAbbreviationOrigin(rs.getString("abbreviation"));
            	tripDTO.setPermitNoOrigin(rs.getString("permitno"));
            	tripDTO.setBusNoOrigin(rs.getString("busno"));
            	tripDTO.setStartTimeOrigin(rs.getString("starttime"));
            	tripDTO.setEndTimeOrigin(rs.getString("endtime"));
            	tripDTO.setFixBusOrigin(rs.getBoolean("fixbus"));
            	            
            	tripList.add(tripDTO);
            }
            
            System.out.println(tripList);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(rs);
            ConnectionManager.close(stmt);
            ConnectionManager.close(con);
        }

        return tripList;
    }


}

