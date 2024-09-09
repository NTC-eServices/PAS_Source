package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.PanelGeneratorDTO;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.roc.utils.common.Utils;

public class PanelGeneratorServiceImpl implements PanelGeneratorService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1372554334563918248L;

	public int insertPanel1(PanelGeneratorDTO panelGeneratorDTO, String user, String refNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_panelgenerator");
			panelGeneratorDTO.setSeqNo(seqNo);

			String sql = "INSERT INTO public.nt_m_panelgenerator\r\n"
					+ "(seq, route_no, bus_category, no_of_bus_perweek,ref_no,status,created_by, created_date)\r\n"
					+ "VALUES(?,?,?,?,?,?,?,?);\r\n" + "";

			
			stmt = con.prepareStatement(sql);

			try {
				stmt.setLong(1, seqNo);
				stmt.setString(2, panelGeneratorDTO.getRouteNo());
				stmt.setString(3, panelGeneratorDTO.getBusCategory());
				stmt.setString(4, panelGeneratorDTO.getNoOfTimeTables());
				stmt.setString(5, refNo);
				stmt.setString(6, panelGeneratorDTO.getPanel1Status());
				stmt.setString(7, user);
				stmt.setTimestamp(8, timestamp);

				stmt.executeUpdate();

				con.commit();
			} catch (SQLException e) {
				con.rollback();
			}

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

	public int group01_details(PanelGeneratorDTO panelGeneratorDTO, String user, List<PanelGeneratorDTO> group1_OD_List,
			List<PanelGeneratorDTO> group1_DO_List, List<String> dateList_Group1) {
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
					+ "(seq, seq_panelgenerator, group_no, bus_category, origin_start_time, origin_end_time,  destination_start_time, destination_end_time,  is_route_start, is_route_end, d_sunday, d_monday, d_tuesday, d_wednesday, d_thursday, d_friday, d_saturday, status, created_by, created_date)\r\n"
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);\r\n" + "" + "";

			stmt = con.prepareStatement(sql);
			try {
				stmt.setLong(1, seqNo);
				stmt.setLong(2, panelGeneratorDTO.getSeqNo());
				stmt.setString(3, "1");
				stmt.setString(4, panelGeneratorDTO.getBusCategory());
				SimpleDateFormat localDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				String startTimeOD = localDateFormat.format(group1_OD_List.get(0).getGroup01_OD_StartTime());
				String endTimeOD = localDateFormat.format(group1_OD_List.get(0).getGroup01_OD_EndTime());
				stmt.setString(5, startTimeOD);
				stmt.setString(6, endTimeOD);
				String startTimeDO = localDateFormat.format(group1_DO_List.get(0).getGroup01_DO_StartTime());
				String endTimeDO = localDateFormat.format(group1_DO_List.get(0).getGroup01_DO_EndTime());
				stmt.setString(7, startTimeDO);
				stmt.setString(8, endTimeDO);
				stmt.setString(9, null);
				stmt.setString(10, null);
				stmt.setString(11, sun);
				stmt.setString(12, mon);
				stmt.setString(13, tue);
				stmt.setString(14, wed);
				stmt.setString(15, thu);
				stmt.setString(16, fri);
				stmt.setString(17, sat);
				stmt.setString(18, "A");
				stmt.setString(19, user);
				stmt.setTimestamp(20, timestamp);
				
				stmt.executeUpdate();
				con.commit();				
			} catch (SQLException e) {
				con.rollback();
				e.printStackTrace();
			}
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

	public int group02_details(PanelGeneratorDTO panelGeneratorDTO, String user, List<PanelGeneratorDTO> group2_OD_List,
			List<PanelGeneratorDTO> group2_DO_List, List<String> dateList_Group2) {
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
					+ "(seq, seq_panelgenerator, group_no, bus_category, origin_start_time, origin_end_time,  destination_start_time, destination_end_time, is_route_start, is_route_end, d_sunday, d_monday, d_tuesday, d_wednesday, d_thursday, d_friday, d_saturday, status, created_by, created_date)\r\n"
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);\r\n" + "" + "";

			stmt = con.prepareStatement(sql);
			try {
				stmt.setLong(1, seqNo);
				stmt.setLong(2, panelGeneratorDTO.getSeqNo());
				stmt.setString(3, "2");
				stmt.setString(4, panelGeneratorDTO.getBusCategory());

				SimpleDateFormat localDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				String startTimeOD = localDateFormat.format(group2_OD_List.get(0).getGroup02_OD_StartTime());
				String endTimeOD = localDateFormat.format(group2_OD_List.get(0).getGroup02_OD_EndTime());

				stmt.setString(5, startTimeOD);
				stmt.setString(6, endTimeOD);

				String startTimeDO = localDateFormat.format(group2_DO_List.get(0).getGroup02_DO_StartTime());
				String endTimeDO = localDateFormat.format(group2_DO_List.get(0).getGroup02_DO_EndTime());

				stmt.setString(7, startTimeDO);
				stmt.setString(8, endTimeDO);

				stmt.setString(9, null);
				stmt.setString(10, null);

				// Days
				stmt.setString(11, sun);
				stmt.setString(12, mon);
				stmt.setString(13, tue);
				stmt.setString(14, wed);
				stmt.setString(15, thu);
				stmt.setString(16, fri);
				stmt.setString(17, sat);

				stmt.setString(18, "A");

				stmt.setString(19, user);
				stmt.setTimestamp(20, timestamp);

				stmt.executeUpdate();

				con.commit();
			} catch (SQLException e) {
				con.rollback();
				e.printStackTrace();
			}
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

	public int group03_details(PanelGeneratorDTO panelGeneratorDTO, String user, List<PanelGeneratorDTO> group3_OD_List,
			List<PanelGeneratorDTO> group3_DO_List, List<String> dateList_Group3) {
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
					+ "(seq, seq_panelgenerator, group_no, bus_category, origin_start_time, origin_end_time, destination_start_time, destination_end_time, is_route_start, is_route_end, d_sunday, d_monday, d_tuesday, d_wednesday, d_thursday, d_friday, d_saturday, status, created_by, created_date)\r\n"
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);\r\n" + "" + "";

			
			stmt = con.prepareStatement(sql);

			try {
				stmt.setLong(1, seqNo);
				stmt.setLong(2, panelGeneratorDTO.getSeqNo());
				stmt.setString(3, "3");
				stmt.setString(4, panelGeneratorDTO.getBusCategory());
				SimpleDateFormat localDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				String startTimeOD = localDateFormat.format(group3_OD_List.get(0).getGroup03_OD_StartTime());
				String endTimeOD = localDateFormat.format(group3_OD_List.get(0).getGroup03_OD_EndTime());
				stmt.setString(5, startTimeOD);
				stmt.setString(6, endTimeOD);
				String startTimeDO = localDateFormat.format(group3_DO_List.get(0).getGroup03_DO_StartTime());
				String endTimeDO = localDateFormat.format(group3_DO_List.get(0).getGroup03_DO_EndTime());
				stmt.setString(7, startTimeDO);
				stmt.setString(8, endTimeDO);
				stmt.setString(9, null);
				stmt.setString(10, null);
				stmt.setString(11, sun);
				stmt.setString(12, mon);
				stmt.setString(13, tue);
				stmt.setString(14, wed);
				stmt.setString(15, thu);
				stmt.setString(16, fri);
				stmt.setString(17, sat);
				stmt.setString(18, "A");
				stmt.setString(19, user);
				stmt.setTimestamp(20, timestamp);
				stmt.executeUpdate();
				con.commit();
			} catch (SQLException e) {
				con.rollback();
				e.printStackTrace();
			}
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

	public List<PanelGeneratorDTO> getRefNoList() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PanelGeneratorDTO> returnList = new ArrayList<PanelGeneratorDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT ref_no FROM public.nt_m_panelgenerator order by ref_no desc";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				PanelGeneratorDTO panelGeneratorDTO = new PanelGeneratorDTO();

				panelGeneratorDTO.setRefNo(rs.getString("ref_no"));

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


	@Override
	public List<String> getRefNoListForDelete() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<String> returnList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT ref_no FROM public.nt_m_panelgenerator order by ref_no desc";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				returnList.add(rs.getString("ref_no"));

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

	
	public List<PanelGeneratorDTO> getTripGeneratorList() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PanelGeneratorDTO> returnList = new ArrayList<PanelGeneratorDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT tg_generator_ref_no FROM public.nt_m_trips_generator";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				PanelGeneratorDTO panelGeneratorDTO = new PanelGeneratorDTO();

				panelGeneratorDTO.setRefNo(rs.getString("tg_generator_ref_no"));

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

	public PanelGeneratorDTO getDetails(String refNo, PanelGeneratorDTO panelGeneratorDTO) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT nt_m_panelgenerator.seq,nt_m_panelgenerator.route_no,nt_m_panelgenerator.bus_category,nt_m_panelgenerator.no_of_bus_perweek,nt_m_panelgenerator.status,"
					+ "nt_t_panelgenerator_det.group_no,nt_t_panelgenerator_det.status as groupStatus "
					+ "FROM public.nt_m_panelgenerator "
					+ "inner join public.nt_t_panelgenerator_det on nt_t_panelgenerator_det.seq_panelgenerator = nt_m_panelgenerator.seq  where ref_no='"
					+ refNo + "'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				panelGeneratorDTO.setSeqNo(rs.getLong("seq"));
				panelGeneratorDTO.setRouteNo(rs.getString("route_no"));
				panelGeneratorDTO.setNoOfTimeTables(rs.getString("no_of_bus_perweek"));
				panelGeneratorDTO.setBusCategory(rs.getString("bus_category"));

//				if (rs.getString("bus_category").equals("001")) {
//
//					panelGeneratorDTO.setBusCategory("NORMAL");
//				}
//
//				else if (rs.getString("bus_category").equals("002")) {
//					panelGeneratorDTO.setBusCategory("LUXURY");
//
//				}
//
//				else if (rs.getString("bus_category").equals("003")) {
//					panelGeneratorDTO.setBusCategory("SUPER LUXURY");
//
//				} else if (rs.getString("bus_category").equals("004")) {
//					panelGeneratorDTO.setBusCategory("SEMI-LUXURY");
//
//				} else if (rs.getString("bus_category").equals("EB")) {
//					panelGeneratorDTO.setBusCategory("EXPRESSWAY BUS");
//
//				}
				panelGeneratorDTO.setPanel1Status(rs.getString("status"));
				panelGeneratorDTO.setSelectGroup(rs.getString("group_no"));
				panelGeneratorDTO.setPanel2Status(rs.getString("groupStatus"));

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return panelGeneratorDTO;

	}

	public List<String> getDateList_Group1(Long seqNo,String refNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		long seq = 0;

		List<String> returnList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();
			
			String sql = "SELECT seq  FROM public.nt_m_panelgenerator where ref_no = ?";
			
			ps1 = con.prepareStatement(sql);
			ps1.setString(1, refNo);
			rs1 = ps1.executeQuery();

			while (rs1.next()) {
				seqNo = rs1.getLong("seq");
			}
			
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
			ConnectionManager.close(rs1);
			ConnectionManager.close(ps1);
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return returnList;

	}

	public List<String> getDateList_Group2(Long seqNo,String refNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;

		List<String> returnList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();
			
			String sql = "SELECT seq  FROM public.nt_m_panelgenerator where ref_no = ?";
			
			ps1 = con.prepareStatement(sql);
			ps1.setString(1, refNo);
			rs1 = ps1.executeQuery();

			while (rs1.next()) {
				seqNo = rs1.getLong("seq");
			}

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
			ConnectionManager.close(rs1);
			ConnectionManager.close(ps1);
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return returnList;

	}

	public List<String> getDateList_Group3(Long seqNo,String refNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;

		List<String> returnList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT seq  FROM public.nt_m_panelgenerator where ref_no = ?";
			
			ps1 = con.prepareStatement(sql);
			ps1.setString(1, refNo);
			rs1 = ps1.executeQuery();

			while (rs1.next()) {
				seqNo = rs1.getLong("seq");
			}

			
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
			ConnectionManager.close(rs1);
			ConnectionManager.close(ps1);
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return returnList;

	}

	public List<PanelGeneratorDTO> getODList_Group1(Long seqNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PanelGeneratorDTO> returnList = new ArrayList<PanelGeneratorDTO>();
		PanelGeneratorDTO panelGeneratorDTO = new PanelGeneratorDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT origin_start_time,origin_end_time FROM public.nt_t_panelgenerator_det where seq_panelgenerator='"
					+ seqNo + "' and group_no='1' and status='A'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				SimpleDateFormat localDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				String startTime = rs.getString("origin_start_time");
				String endTime = rs.getString("origin_end_time");

				panelGeneratorDTO.setGroup01_OD_StartTime(localDateFormat.parse(startTime));
				panelGeneratorDTO.setGroup01_OD_EndTime(localDateFormat.parse(endTime));

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

	public List<PanelGeneratorDTO> getODList_Group2(Long seqNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PanelGeneratorDTO> returnList = new ArrayList<PanelGeneratorDTO>();
		PanelGeneratorDTO panelGeneratorDTO = new PanelGeneratorDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT origin_start_time,origin_end_time FROM public.nt_t_panelgenerator_det where seq_panelgenerator='"
					+ seqNo + "' and group_no='2' and status='A'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				SimpleDateFormat localDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				String startTime = rs.getString("origin_start_time");
				String endTime = rs.getString("origin_end_time");

				panelGeneratorDTO.setGroup02_OD_StartTime(localDateFormat.parse(startTime));
				panelGeneratorDTO.setGroup02_OD_EndTime(localDateFormat.parse(endTime));

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

	public List<PanelGeneratorDTO> getODList_Group3(Long seqNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PanelGeneratorDTO> returnList = new ArrayList<PanelGeneratorDTO>();
		PanelGeneratorDTO panelGeneratorDTO = new PanelGeneratorDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT origin_start_time,origin_end_time FROM public.nt_t_panelgenerator_det where seq_panelgenerator='"
					+ seqNo + "' and group_no='3' and status='A'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				SimpleDateFormat localDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				String startTime = rs.getString("origin_start_time");
				String endTime = rs.getString("origin_end_time");

				panelGeneratorDTO.setGroup03_OD_StartTime(localDateFormat.parse(startTime));
				panelGeneratorDTO.setGroup03_OD_EndTime(localDateFormat.parse(endTime));

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

	public List<PanelGeneratorDTO> getDOList_Group1(Long seqNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PanelGeneratorDTO> returnList = new ArrayList<PanelGeneratorDTO>();
		PanelGeneratorDTO panelGeneratorDTO = new PanelGeneratorDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT destination_start_time,destination_end_time FROM public.nt_t_panelgenerator_det where seq_panelgenerator='"
					+ seqNo + "' and group_no='1' and status='A'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				SimpleDateFormat localDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				String startTime = rs.getString("destination_start_time");
				String endTime = rs.getString("destination_end_time");

				panelGeneratorDTO.setGroup01_DO_StartTime(localDateFormat.parse(startTime));
				panelGeneratorDTO.setGroup01_DO_EndTime(localDateFormat.parse(endTime));

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

	public List<PanelGeneratorDTO> getDOList_Group2(Long seqNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PanelGeneratorDTO> returnList = new ArrayList<PanelGeneratorDTO>();
		PanelGeneratorDTO panelGeneratorDTO = new PanelGeneratorDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT destination_start_time,destination_end_time FROM public.nt_t_panelgenerator_det where seq_panelgenerator='"
					+ seqNo + "' and group_no='2' and status='A'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				SimpleDateFormat localDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				String startTime = rs.getString("destination_start_time");
				String endTime = rs.getString("destination_end_time");

				panelGeneratorDTO.setGroup02_DO_StartTime(localDateFormat.parse(startTime));
				panelGeneratorDTO.setGroup02_DO_EndTime(localDateFormat.parse(endTime));

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

	public List<PanelGeneratorDTO> getDOList_Group3(Long seqNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PanelGeneratorDTO> returnList = new ArrayList<PanelGeneratorDTO>();
		PanelGeneratorDTO panelGeneratorDTO = new PanelGeneratorDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT destination_start_time,destination_end_time FROM public.nt_t_panelgenerator_det where seq_panelgenerator='"
					+ seqNo + "' and group_no='3' and status='A'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				SimpleDateFormat localDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				String startTime = rs.getString("destination_start_time");
				String endTime = rs.getString("destination_end_time");

				panelGeneratorDTO.setGroup03_DO_StartTime(localDateFormat.parse(startTime));
				panelGeneratorDTO.setGroup03_DO_EndTime(localDateFormat.parse(endTime));

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

	public int updateGroup1_OriginToDestination(Long seqNo, String user, List<PanelGeneratorDTO> group1_OD_List) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		SimpleDateFormat localDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		String startTimeOD = localDateFormat.format(group1_OD_List.get(0).getGroup01_OD_StartTime());
		String endTimeOD = localDateFormat.format(group1_OD_List.get(0).getGroup01_OD_EndTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_panelgenerator_det\r\n" + "SET origin_start_time='" + startTimeOD
					+ "', origin_end_time='" + endTimeOD + "',modified_by='" + user + "', modified_date='" + timestamp + "' where seq_panelgenerator='"
					+ seqNo + "' and group_no='1'";

			stmt = con.prepareStatement(sql);

			stmt.executeUpdate();
			con.commit();

		
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return 0;
	}

	public int updateGroup2_OriginToDestination(Long seqNo, String user, List<PanelGeneratorDTO> group2_OD_List) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		SimpleDateFormat localDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		String startTimeOD = localDateFormat.format(group2_OD_List.get(0).getGroup02_OD_StartTime());
		String endTimeOD = localDateFormat.format(group2_OD_List.get(0).getGroup02_OD_EndTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_panelgenerator_det\r\n" + "SET origin_start_time='" + startTimeOD
					+ "', origin_end_time='" + endTimeOD + "',modified_by='" + user + "', modified_date='" + timestamp + "' where seq_panelgenerator='"
					+ seqNo + "' and group_no='2'";

			stmt = con.prepareStatement(sql);

			stmt.executeUpdate();
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return 0;
	}

	public int updateGroup3_OriginToDestination(Long seqNo, String user, List<PanelGeneratorDTO> group3_OD_List) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		SimpleDateFormat localDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		String startTimeOD = localDateFormat.format(group3_OD_List.get(0).getGroup03_OD_StartTime());
		String endTimeOD = localDateFormat.format(group3_OD_List.get(0).getGroup03_OD_EndTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_panelgenerator_det\r\n" + "SET origin_start_time='" + startTimeOD
					+ "', origin_end_time='" + endTimeOD + "',modified_by='" + user + "', modified_date='" + timestamp + "' where seq_panelgenerator='"
					+ seqNo + "' and group_no='3'";

			stmt = con.prepareStatement(sql);

			stmt.executeUpdate();
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return 0;
	}

	public int updateGroup1_DestinationToOrigin(Long seqNo, String user, List<PanelGeneratorDTO> group1_DO_List) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		SimpleDateFormat localDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		String startTimeDO = localDateFormat.format(group1_DO_List.get(0).getGroup01_DO_StartTime());
		String endTimeDO = localDateFormat.format(group1_DO_List.get(0).getGroup01_DO_EndTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_panelgenerator_det\r\n" + "SET destination_start_time='" + startTimeDO
					+ "', destination_end_time='" + endTimeDO + "',modified_by='" + user + "', modified_date='" + timestamp + "' where seq_panelgenerator='"
					+ seqNo + "' and group_no='1'";

			stmt = con.prepareStatement(sql);

			stmt.executeUpdate();
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return 0;
	}

	public int updateGroup2_DestinationToOrigin(Long seqNo, String user, List<PanelGeneratorDTO> group2_DO_List) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		SimpleDateFormat localDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		String startTimeDO = localDateFormat.format(group2_DO_List.get(0).getGroup02_DO_StartTime());
		String endTimeDO = localDateFormat.format(group2_DO_List.get(0).getGroup02_DO_EndTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_panelgenerator_det\r\n" + "SET destination_start_time='" + startTimeDO
					+ "', destination_end_time='" + endTimeDO + "',modified_by='" + user + "', modified_date='" + timestamp + "' where seq_panelgenerator='"
					+ seqNo + "' and group_no='2'";

			stmt = con.prepareStatement(sql);

			stmt.executeUpdate();
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return 0;
	}

	public int updateGroup3_DestinationToOrigin(Long seqNo, String user, List<PanelGeneratorDTO> group3_DO_List) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		SimpleDateFormat localDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		String startTimeDO = localDateFormat.format(group3_DO_List.get(0).getGroup03_DO_StartTime());
		String endTimeDO = localDateFormat.format(group3_DO_List.get(0).getGroup03_DO_EndTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_panelgenerator_det\r\n" + "SET destination_start_time='" + startTimeDO
					+ "', destination_end_time='" + endTimeDO + "',modified_by='" + user + "', modified_date='" + timestamp + "' where seq_panelgenerator='"
					+ seqNo + "' and group_no='3'";

			stmt = con.prepareStatement(sql);

			stmt.executeUpdate();
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
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
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
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
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
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
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
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

			String query = "SELECT  distinct rou_number FROM public.nt_r_route where active='A'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("rou_number"));

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
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
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
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
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
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return 0;
	}

	public int updateStatus(String status, String user, String refNo, String route, String service) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_panelgenerator\r\n" + "SET status='" + status + "', modified_by='" + user
				    + "', modified_date='" + timestamp + "'\r\n" + ", route_no='" + route + "'\r\n"
				    + ", bus_category='" + service + "'\r\n" + "WHERE ref_no='" + refNo + "';\r\n" + "";

			stmt = con.prepareStatement(sql);

			stmt.executeUpdate();
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
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
	public String getActiveRefNo(String routeNo, String serviceType, String group,String noOfTimeTable) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps2 = null;
		ResultSet rs2 = null;
		String refNo = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select ref_no "
					+ "from public.nt_m_panelgenerator "
					+ "inner join public.nt_t_panelgenerator_det "
					+ "on nt_m_panelgenerator.seq = nt_t_panelgenerator_det.seq_panelgenerator "
					+ "where nt_m_panelgenerator.status='A' and nt_m_panelgenerator.route_no = ? "
					+ "and nt_m_panelgenerator.bus_category = ? and nt_t_panelgenerator_det.group_no = ?";

			ps = con.prepareStatement(query);
			ps.setString(1, routeNo);
			ps.setString(2, serviceType);
			ps.setString(3, group);
			rs = ps.executeQuery();

			while (rs.next()) {
				refNo = rs.getString("ref_no");
			}			

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs2);
			ConnectionManager.close(ps2);
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return refNo;

	}
	
	
	@Override
	public boolean getActiveDataRecord(String routeNo, String serviceType, String group,String noOfTimeTable) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps2 = null;
		ResultSet rs2 = null;
		boolean dataHave = false;
		List<Long> seqNoList = new ArrayList<>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select count(nt_m_panelgenerator.seq) as noOfSeq "
					+ "from public.nt_m_panelgenerator "
					+ "inner join public.nt_t_panelgenerator_det "
					+ "on nt_m_panelgenerator.seq = nt_t_panelgenerator_det.seq_panelgenerator "
					+ "where nt_m_panelgenerator.status='A' and nt_m_panelgenerator.route_no = ? "
					+ "and nt_m_panelgenerator.bus_category = ? and nt_t_panelgenerator_det.group_no = ? "
					+ "group by nt_m_panelgenerator.seq";

			ps = con.prepareStatement(query);
			ps.setString(1, routeNo);
			ps.setString(2, serviceType);
			ps.setString(3, group);
			rs = ps.executeQuery();

			while (rs.next()) {
				dataHave = true;
			}			

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs2);
			ConnectionManager.close(ps2);
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return dataHave;

	}
	
	@Override
	public boolean UpdateStatus(String refNo) {
		boolean haveData = false;

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		PreparedStatement psSelect = null;
		ResultSet rs = null;
		long seq = 0;

		try {
			String sql = null;
			con = ConnectionManager.getConnection();
			
			String query = "select seq from public.nt_m_panelgenerator WHERE ref_no = ?";
			psSelect = con.prepareStatement(query);
			psSelect.setString(1, refNo);
			rs = psSelect.executeQuery();
			
			while (rs.next()) {
				seq = rs.getLong("seq");
			}		
			

			sql = "UPDATE public.nt_m_panelgenerator SET status = 'I' WHERE ref_no = ?";
			String sql1 = "UPDATE public.nt_t_panelgenerator_det  SET status = 'I' WHERE seq_panelgenerator = ?";

			ps = con.prepareStatement(sql);
			ps1 = con.prepareStatement(sql1);
			try {
				ps.setString(1, refNo);

				int updateCount = ps.executeUpdate();

				if (updateCount != 0) {
					haveData = true;
				}
				
				
				ps1.setLong(1, seq);

				int updateCount1 = ps1.executeUpdate();

				if (updateCount1 != 0) {
					haveData = true;
				}

			} catch (SQLException e) {
				con.rollback();
				e.printStackTrace();
				haveData = false;
			}

			con.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			haveData = false;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(psSelect);
			ConnectionManager.close(ps1);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return haveData;
	}
	
	@Override
	public boolean UpdateStatusByRoute(String routeNo, String serviceType, String group) {
		boolean haveData = false;

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		PreparedStatement ps4 = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();
			
			String query = "UPDATE public.nt_m_midpoint_o_to_d SET group_no = '0' WHERE mp_route_no = ? AND mp_service_type = ? AND group_no = ?;";

			ps = con.prepareStatement(query);
			ps.setString(1, routeNo);
			ps.setString(2, serviceType);
			ps.setString(3, group);
			ps.executeUpdate();

			ConnectionManager.close(ps);

			String query2 = "UPDATE public.nt_m_midpoint_d_to_o SET group_no = '0' WHERE mp_route_no = ? AND mp_service_type = ? AND group_no = ?;";

			ps2 = con.prepareStatement(query2);
			ps2.setString(1, routeNo);
			ps2.setString(2, serviceType);
			ps2.setString(3, group);
			ps2.executeUpdate();

			ConnectionManager.close(ps2);

			String query3 = "DELETE  FROM public.nt_m_combinepanal_temp WHERE mp_route_no = ?"
					+ " AND mp_service_type = ? AND group_no = ?";

			ps3 = con.prepareStatement(query3);
			ps3.setString(1, routeNo);
			ps3.setString(2, serviceType);
			ps3.setString(3, group);
			ps3.executeUpdate();

			ConnectionManager.close(ps3);

			String query4 = "DELETE FROM public.nt_t_combine_panel_generator WHERE route_no = ? AND service_type = ?";

			ps4 = con.prepareStatement(query4);
			ps4.setString(1, routeNo);
			ps4.setString(2, serviceType);
			ps4.executeUpdate();

			ConnectionManager.close(ps4);
			con.commit();
			haveData = true;

		} catch (SQLException e) {
			try {
	            if (con != null) {
	                con.rollback();
	            }
	        } catch (SQLException rollbackException) {
	            rollbackException.printStackTrace();
	        }
	        e.printStackTrace();
	        haveData = false;
		} catch (Exception e) {
			e.printStackTrace();
			haveData = false;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(ps3);
			ConnectionManager.close(ps4);
			ConnectionManager.close(con);
		}
		return haveData;
	}

}
