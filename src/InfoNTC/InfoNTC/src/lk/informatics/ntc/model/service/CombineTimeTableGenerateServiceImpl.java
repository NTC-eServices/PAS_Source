package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import lk.informatics.ntc.model.dto.BusFareDTO;
import lk.informatics.ntc.model.dto.MidPointTimesDTO;
import lk.informatics.ntc.model.dto.MidpointUIDTO;
import lk.informatics.ntc.model.dto.RouteDTO;
import lk.informatics.ntc.model.dto.TimeTableDTO;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.roc.utils.common.Utils;

public class CombineTimeTableGenerateServiceImpl implements CombineTimeTableGenerateService {

	private static final long serialVersionUID = 1L;

	@Override
	public List<RouteDTO> getRouteNoList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<RouteDTO> routeNoList = new ArrayList<RouteDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT distinct  route_no, status FROM public.nt_m_panelgenerator where status='A'";

			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();
			RouteDTO e;

			while (rs.next()) {
				e = new RouteDTO();
				e.setRouteNo(rs.getString("route_no"));
				routeNoList.add(e);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return routeNoList;
	}

	@Override
	public TimeTableDTO getRouteData(String routeNO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		TimeTableDTO dto = new TimeTableDTO();
		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT  ref_no,route_no, nt_r_route.rou_service_origine,nt_r_route.rou_service_destination "
					+ "FROM public.nt_m_panelgenerator "
					+ "inner join public.nt_r_route on nt_r_route.rou_number=nt_m_panelgenerator.route_no "
					+ "where status='A' and route_no=? ";

			ps = con.prepareStatement(query2);
			ps.setString(1, routeNO);
			rs = ps.executeQuery();

			while (rs.next()) {
				dto.setDestination(rs.getString("rou_service_destination"));
				dto.setGenereatedRefNo(rs.getString("ref_no"));
				dto.setOrigin(rs.getString("rou_service_origine"));
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return dto;
	}

	@Override
	public List<TimeTableDTO> retireveStartEndTimes(String category, String selectedRouteNo, String selectedGroup) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TimeTableDTO> startEndTimeList = new ArrayList<TimeTableDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT a.start_time_slot, a.end_time_slot, a.bus_num, a.assigned_bus_no "
					+ "FROM public.nt_m_timetable_generator_det a where a.group_no=? and  "
					+ "generator_ref_no = (SELECT tg_generator_ref_no FROM public.nt_m_trips_generator "
					+ " where tg_route_no=? and tg_bus_category=?) order by a.seq_no";

			ps = con.prepareStatement(query2);
			ps.setString(1, selectedGroup);
			ps.setString(2, selectedRouteNo);
			ps.setString(3, category);
			rs = ps.executeQuery();

			while (rs.next()) {
				TimeTableDTO timeTableDTO = new TimeTableDTO();
				if (rs.getString("start_time_slot") != null && !rs.getString("start_time_slot").isEmpty()
						&& !rs.getString("start_time_slot").trim().equalsIgnoreCase("")
						&& rs.getString("end_time_slot") != null && !rs.getString("end_time_slot").isEmpty()
						&& !rs.getString("end_time_slot").trim().equalsIgnoreCase("")) {

					timeTableDTO.setStartTime(rs.getString("start_time_slot"));
					timeTableDTO.setEndTime(rs.getString("end_time_slot"));

					if (rs.getString("assigned_bus_no") != null && !rs.getString("assigned_bus_no").isEmpty()
							&& !rs.getString("assigned_bus_no").trim().equalsIgnoreCase("")) {
						timeTableDTO.setBusNo(rs.getString("assigned_bus_no"));
					} else {
						timeTableDTO.setBusNo(rs.getString("bus_num"));
					}

					startEndTimeList.add(timeTableDTO);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return startEndTimeList;
	}

	@Override
	public List<MidpointUIDTO> retrieveMidPointTimeTakenForRoute(String routeNo, String busType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<MidpointUIDTO> midPointTimeList = new ArrayList<MidpointUIDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = " SELECT mp_time_taken , mp_midpoint_code FROM public.nt_m_route_midpoint "
					+ "where  mp_route_no=? and mp_bus_type=? order by seq ASC";

			ps = con.prepareStatement(query2);
			ps.setString(1, routeNo);
			ps.setString(2, busType);
			rs = ps.executeQuery();

			while (rs.next()) {
				MidpointUIDTO midpointUIDTO = new MidpointUIDTO();
				midpointUIDTO.setTimeTakenStr(rs.getString("mp_time_taken"));
				midpointUIDTO.setMidPointCode(rs.getString("mp_midpoint_code"));

				midPointTimeList.add(midpointUIDTO);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return midPointTimeList;
	}

	@Override
	public List<String> retrieveMidPointNamesForRoute(String routeNo, String busType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> midPointNamesList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT a.mp_midpoint_code, b.midpoint_name "
					+ "FROM public.nt_m_route_midpoint a, public.nt_r_station b "
					+ "where  a.mp_route_no=? and a.mp_bus_type=? and a.mp_midpoint_code=b.code order by a.seq ASC";

			ps = con.prepareStatement(query2);
			ps.setString(1, routeNo);
			ps.setString(2, busType);
			rs = ps.executeQuery();

			while (rs.next()) {
				midPointNamesList.add(rs.getString("midpoint_name"));
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return midPointNamesList;

	}

	public List<MidpointUIDTO> getMidPointCodeDesc(String routeNo, String busType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<MidpointUIDTO> midPointCodesList = new ArrayList<MidpointUIDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT a.mp_midpoint_code, b.midpoint_name "
					+ "FROM public.nt_m_route_midpoint a, public.nt_r_station b "
					+ "where  a.mp_route_no=? and a.mp_bus_type=? and a.mp_midpoint_code=b.code order by a.seq ASC";

			ps = con.prepareStatement(query2);
			ps.setString(1, routeNo);
			ps.setString(2, busType);
			rs = ps.executeQuery();

			while (rs.next()) {
				MidpointUIDTO dto = new MidpointUIDTO();
				dto.setMidPointCode(rs.getString("mp_midpoint_code"));
				dto.setMidPointDesc(rs.getString("midpoint_name"));
				midPointCodesList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return midPointCodesList;

	}

	@Override
	public String retrieveBusTypeDesc(String busType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String busTypeDesc = null;

		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT description FROM public.nt_r_service_types where code = ?";

			ps = con.prepareStatement(query2);
			ps.setString(1, busType);
			rs = ps.executeQuery();

			while (rs.next()) {
				busTypeDesc = rs.getString("description");
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return busTypeDesc;

	}

	@Override
	public List<MidpointUIDTO> retrieveMidPointTimeTakenForRouteDtoO(String routeNo, String busType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<MidpointUIDTO> midPointTimeList = new ArrayList<MidpointUIDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = " SELECT mp_time_taken , mp_midpoint_code FROM public.nt_m_route_midpoint_reverse "
					+ "where  mp_route_no=? and mp_bus_type=? order by seq ASC";

			ps = con.prepareStatement(query2);
			ps.setString(1, routeNo);
			ps.setString(2, busType);
			rs = ps.executeQuery();

			while (rs.next()) {
				MidpointUIDTO midpointUIDTO = new MidpointUIDTO();
				midpointUIDTO.setTimeTakenStr(rs.getString("mp_time_taken"));
				midpointUIDTO.setMidPointCode(rs.getString("mp_midpoint_code"));

				midPointTimeList.add(midpointUIDTO);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return midPointTimeList;
	}

	@Override
	public List<String> retrieveMidPointNamesForRouteDtoO(String routeNo, String busType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> midPointNamesList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT a.mp_midpoint_code, b.midpoint_name "
					+ "FROM public.nt_m_route_midpoint_reverse a, public.nt_r_station b "
					+ "where  a.mp_route_no=? and a.mp_bus_type=? and a.mp_midpoint_code=b.code order by a.seq ASC";

			ps = con.prepareStatement(query2);
			ps.setString(1, routeNo);
			ps.setString(2, busType);
			rs = ps.executeQuery();

			while (rs.next()) {
				midPointNamesList.add(rs.getString("midpoint_name"));
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return midPointNamesList;

	}

	public List<MidpointUIDTO> getMidPointCodeDescDtoO(String routeNo, String busType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<MidpointUIDTO> midPointCodesList = new ArrayList<MidpointUIDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT a.mp_midpoint_code, b.midpoint_name "
					+ "FROM public.nt_m_route_midpoint_reverse a, public.nt_r_station b "
					+ "where  a.mp_route_no=? and a.mp_bus_type=? and a.mp_midpoint_code=b.code order by a.seq ASC";

			ps = con.prepareStatement(query2);
			ps.setString(1, routeNo);
			ps.setString(2, busType);
			rs = ps.executeQuery();

			while (rs.next()) {
				MidpointUIDTO dto = new MidpointUIDTO();
				dto.setMidPointCode(rs.getString("mp_midpoint_code"));
				dto.setMidPointDesc(rs.getString("midpoint_name"));
				midPointCodesList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return midPointCodesList;

	}

	@Override
	public List<RouteDTO> retrieveSimilarRoutes(String routeNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<RouteDTO> routeNoList = new ArrayList<RouteDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT rou_description,rou_number FROM public.nt_r_route where rou_number like '%"
					+ routeNo + "%' and rou_number!='" + routeNo + "'";

			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();
			RouteDTO e;

			while (rs.next()) {
				e = new RouteDTO();
				e.setRouteNo(rs.getString("rou_number"));
				e.setRouteDes(rs.getString("rou_description"));
				routeNoList.add(e);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return routeNoList;
	}

	@Override
	public void insertMidpointDataToNt_t_midpoint_timetable(List<MidPointTimesDTO> midPointsExpress, String routeNo,
			String category, String tripType, String group, String loginUser) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		long timeTableSeq = 0;
		String panelGenRefNo = null;
		try {

			con = ConnectionManager.getConnection();

			// get panel generator ref number start
			String selct = "SELECT tg_generator_ref_no FROM public.nt_m_trips_generator where tg_route_no=?  and tg_bus_category=? ";

			ps = con.prepareStatement(selct);
			ps.setString(1, routeNo);
			ps.setString(2, category);
			rs = ps.executeQuery();

			while (rs.next()) {
				panelGenRefNo = rs.getString("tg_generator_ref_no");
				break;
			}

			try {
				if (ps != null)
					ps.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// get panel generator ref number end

			// get timetable gen seq start
			String select = "select seq_no from public.nt_m_timetable_generator "
					+ "where  generator_ref_no=? and group_no=? and trip_type=?";

			ps = con.prepareStatement(select);
			ps.setString(1, panelGenRefNo);
			ps.setString(2, group);
			ps.setString(3, tripType);
			rs = ps.executeQuery();

			while (rs.next()) {
				timeTableSeq = rs.getLong("seq_no");
				break;
			}

			try {
				if (ps != null)
					ps.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// get timetable gen seq end

			// get midpoint codes start
			List<MidpointUIDTO> midPointCodesList = new ArrayList<MidpointUIDTO>();
			if (tripType != null && !tripType.isEmpty() && tripType.equalsIgnoreCase("O")) {
				midPointCodesList = getMidPointCodeDesc(routeNo, category); // O to D
			} else {
				midPointCodesList = getMidPointCodeDescDtoO(routeNo, category); // D to O
			}

			// get midpoint codes end

			/** insert data into nt_t_midpoint_timetable start **/
			int count = 1;
			int midpointCount = 0;
			for (MidPointTimesDTO dto : midPointsExpress) {

				if (dto.getMidPoint1() != null && !dto.getMidPoint1().isEmpty()
						&& !dto.getMidPoint1().trim().equalsIgnoreCase("")) {
					String midPointCode = midPointCodesList.get(midpointCount).getMidPointCode();
					insertQuery(con, routeNo, timeTableSeq, panelGenRefNo, dto.getMidPoint1(), tripType, group,
							Integer.toString(count), loginUser, midPointCode);
					midpointCount = midpointCount + 1;
				}
				if (dto.getMidPoint2() != null && !dto.getMidPoint2().isEmpty()
						&& !dto.getMidPoint2().trim().equalsIgnoreCase("")) {
					String midPointCode = midPointCodesList.get(midpointCount).getMidPointCode();
					insertQuery(con, routeNo, timeTableSeq, panelGenRefNo, dto.getMidPoint2(), tripType, group,
							Integer.toString(count), loginUser, midPointCode);
					midpointCount = midpointCount + 1;
				}
				if (dto.getMidPoint3() != null && !dto.getMidPoint3().isEmpty()
						&& !dto.getMidPoint3().trim().equalsIgnoreCase("")) {
					String midPointCode = midPointCodesList.get(midpointCount).getMidPointCode();
					insertQuery(con, routeNo, timeTableSeq, panelGenRefNo, dto.getMidPoint3(), tripType, group,
							Integer.toString(count), loginUser, midPointCode);
					midpointCount = midpointCount + 1;
				}
				if (dto.getMidPoint4() != null && !dto.getMidPoint4().isEmpty()
						&& !dto.getMidPoint4().trim().equalsIgnoreCase("")) {
					String midPointCode = midPointCodesList.get(midpointCount).getMidPointCode();
					insertQuery(con, routeNo, timeTableSeq, panelGenRefNo, dto.getMidPoint4(), tripType, group,
							Integer.toString(count), loginUser, midPointCode);
					midpointCount = midpointCount + 1;
				}
				if (dto.getMidPoint5() != null && !dto.getMidPoint5().isEmpty()
						&& !dto.getMidPoint5().trim().equalsIgnoreCase("")) {
					String midPointCode = midPointCodesList.get(midpointCount).getMidPointCode();
					insertQuery(con, routeNo, timeTableSeq, panelGenRefNo, dto.getMidPoint5(), tripType, group,
							Integer.toString(count), loginUser, midPointCode);
					midpointCount = midpointCount + 1;
				}
				if (dto.getMidPoint6() != null && !dto.getMidPoint6().isEmpty()
						&& !dto.getMidPoint6().trim().equalsIgnoreCase("")) {
					String midPointCode = midPointCodesList.get(midpointCount).getMidPointCode();
					insertQuery(con, routeNo, timeTableSeq, panelGenRefNo, dto.getMidPoint6(), tripType, group,
							Integer.toString(count), loginUser, midPointCode);
					midpointCount = midpointCount + 1;
				}
				if (dto.getMidPoint7() != null && !dto.getMidPoint7().isEmpty()
						&& !dto.getMidPoint7().trim().equalsIgnoreCase("")) {
					String midPointCode = midPointCodesList.get(midpointCount).getMidPointCode();
					insertQuery(con, routeNo, timeTableSeq, panelGenRefNo, dto.getMidPoint7(), tripType, group,
							Integer.toString(count), loginUser, midPointCode);
					midpointCount = midpointCount + 1;
				}
				if (dto.getMidPoint8() != null && !dto.getMidPoint8().isEmpty()
						&& !dto.getMidPoint8().trim().equalsIgnoreCase("")) {
					String midPointCode = midPointCodesList.get(midpointCount).getMidPointCode();
					insertQuery(con, routeNo, timeTableSeq, panelGenRefNo, dto.getMidPoint8(), tripType, group,
							Integer.toString(count), loginUser, midPointCode);
					midpointCount = midpointCount + 1;
				}
				if (dto.getMidPoint9() != null && !dto.getMidPoint9().isEmpty()
						&& !dto.getMidPoint9().trim().equalsIgnoreCase("")) {
					String midPointCode = midPointCodesList.get(midpointCount).getMidPointCode();
					insertQuery(con, routeNo, timeTableSeq, panelGenRefNo, dto.getMidPoint9(), tripType, group,
							Integer.toString(count), loginUser, midPointCode);
					midpointCount = midpointCount + 1;
				}
				if (dto.getMidPoint10() != null && !dto.getMidPoint10().isEmpty()
						&& !dto.getMidPoint10().trim().equalsIgnoreCase("")) {
					String midPointCode = midPointCodesList.get(midpointCount).getMidPointCode();
					insertQuery(con, routeNo, timeTableSeq, panelGenRefNo, dto.getMidPoint10(), tripType, group,
							Integer.toString(count), loginUser, midPointCode);
					midpointCount = midpointCount + 1;
				}
				if (dto.getMidPoint11() != null && !dto.getMidPoint11().isEmpty()
						&& !dto.getMidPoint11().trim().equalsIgnoreCase("")) {
					String midPointCode = midPointCodesList.get(midpointCount).getMidPointCode();
					insertQuery(con, routeNo, timeTableSeq, panelGenRefNo, dto.getMidPoint11(), tripType, group,
							Integer.toString(count), loginUser, midPointCode);
					midpointCount = midpointCount + 1;
				}
				if (dto.getMidPoint12() != null && !dto.getMidPoint12().isEmpty()
						&& !dto.getMidPoint12().trim().equalsIgnoreCase("")) {
					String midPointCode = midPointCodesList.get(midpointCount).getMidPointCode();
					insertQuery(con, routeNo, timeTableSeq, panelGenRefNo, dto.getMidPoint12(), tripType, group,
							Integer.toString(count), loginUser, midPointCode);
					midpointCount = midpointCount + 1;
				}
				if (dto.getMidPoint13() != null && !dto.getMidPoint13().isEmpty()
						&& !dto.getMidPoint13().trim().equalsIgnoreCase("")) {
					String midPointCode = midPointCodesList.get(midpointCount).getMidPointCode();
					insertQuery(con, routeNo, timeTableSeq, panelGenRefNo, dto.getMidPoint13(), tripType, group,
							Integer.toString(count), loginUser, midPointCode);
					midpointCount = midpointCount + 1;
				}
				if (dto.getMidPoint14() != null && !dto.getMidPoint14().isEmpty()
						&& !dto.getMidPoint14().trim().equalsIgnoreCase("")) {
					String midPointCode = midPointCodesList.get(midpointCount).getMidPointCode();
					insertQuery(con, routeNo, timeTableSeq, panelGenRefNo, dto.getMidPoint14(), tripType, group,
							Integer.toString(count), loginUser, midPointCode);
					midpointCount = midpointCount + 1;
				}
				if (dto.getMidPoint15() != null && !dto.getMidPoint15().isEmpty()
						&& !dto.getMidPoint15().trim().equalsIgnoreCase("")) {
					String midPointCode = midPointCodesList.get(midpointCount).getMidPointCode();
					insertQuery(con, routeNo, timeTableSeq, panelGenRefNo, dto.getMidPoint15(), tripType, group,
							Integer.toString(count), loginUser, midPointCode);
					midpointCount = midpointCount + 1;
				}
				if (dto.getMidPoint16() != null && !dto.getMidPoint16().isEmpty()
						&& !dto.getMidPoint16().trim().equalsIgnoreCase("")) {
					String midPointCode = midPointCodesList.get(midpointCount).getMidPointCode();
					insertQuery(con, routeNo, timeTableSeq, panelGenRefNo, dto.getMidPoint16(), tripType, group,
							Integer.toString(count), loginUser, midPointCode);
					midpointCount = midpointCount + 1;
				}
				if (dto.getMidPoint17() != null && !dto.getMidPoint17().isEmpty()
						&& !dto.getMidPoint17().trim().equalsIgnoreCase("")) {
					String midPointCode = midPointCodesList.get(midpointCount).getMidPointCode();
					insertQuery(con, routeNo, timeTableSeq, panelGenRefNo, dto.getMidPoint17(), tripType, group,
							Integer.toString(count), loginUser, midPointCode);
					midpointCount = midpointCount + 1;
				}
				if (dto.getMidPoint18() != null && !dto.getMidPoint18().isEmpty()
						&& !dto.getMidPoint18().trim().equalsIgnoreCase("")) {
					String midPointCode = midPointCodesList.get(midpointCount).getMidPointCode();
					insertQuery(con, routeNo, timeTableSeq, panelGenRefNo, dto.getMidPoint18(), tripType, group,
							Integer.toString(count), loginUser, midPointCode);
					midpointCount = midpointCount + 1;
				}
				if (dto.getMidPoint19() != null && !dto.getMidPoint9().isEmpty()
						&& !dto.getMidPoint19().trim().equalsIgnoreCase("")) {
					String midPointCode = midPointCodesList.get(midpointCount).getMidPointCode();
					insertQuery(con, routeNo, timeTableSeq, panelGenRefNo, dto.getMidPoint9(), tripType, group,
							Integer.toString(count), loginUser, midPointCode);
					midpointCount = midpointCount + 1;
				}
				if (dto.getMidPoint20() != null && !dto.getMidPoint20().isEmpty()
						&& !dto.getMidPoint20().trim().equalsIgnoreCase("")) {
					String midPointCode = midPointCodesList.get(midpointCount).getMidPointCode();
					insertQuery(con, routeNo, timeTableSeq, panelGenRefNo, dto.getMidPoint20(), tripType, group,
							Integer.toString(count), loginUser, midPointCode);
					midpointCount = midpointCount + 1;
				}
				if (dto.getMidPoint21() != null && !dto.getMidPoint21().isEmpty()
						&& !dto.getMidPoint21().trim().equalsIgnoreCase("")) {
					String midPointCode = midPointCodesList.get(midpointCount).getMidPointCode();
					insertQuery(con, routeNo, timeTableSeq, panelGenRefNo, dto.getMidPoint21(), tripType, group,
							Integer.toString(count), loginUser, midPointCode);
					midpointCount = midpointCount + 1;
				}
				if (dto.getMidPoint22() != null && !dto.getMidPoint22().isEmpty()
						&& !dto.getMidPoint22().trim().equalsIgnoreCase("")) {
					String midPointCode = midPointCodesList.get(midpointCount).getMidPointCode();
					insertQuery(con, routeNo, timeTableSeq, panelGenRefNo, dto.getMidPoint22(), tripType, group,
							Integer.toString(count), loginUser, midPointCode);
					midpointCount = midpointCount + 1;
				}
				if (dto.getMidPoint23() != null && !dto.getMidPoint23().isEmpty()
						&& !dto.getMidPoint23().trim().equalsIgnoreCase("")) {
					String midPointCode = midPointCodesList.get(midpointCount).getMidPointCode();
					insertQuery(con, routeNo, timeTableSeq, panelGenRefNo, dto.getMidPoint23(), tripType, group,
							Integer.toString(count), loginUser, midPointCode);
					midpointCount = midpointCount + 1;
				}
				if (dto.getMidPoint24() != null && !dto.getMidPoint24().isEmpty()
						&& !dto.getMidPoint24().trim().equalsIgnoreCase("")) {
					String midPointCode = midPointCodesList.get(midpointCount).getMidPointCode();
					insertQuery(con, routeNo, timeTableSeq, panelGenRefNo, dto.getMidPoint24(), tripType, group,
							Integer.toString(count), loginUser, midPointCode);
					midpointCount = midpointCount + 1;
				}
				if (dto.getMidPoint25() != null && !dto.getMidPoint25().isEmpty()
						&& !dto.getMidPoint25().trim().equalsIgnoreCase("")) {
					String midPointCode = midPointCodesList.get(midpointCount).getMidPointCode();
					insertQuery(con, routeNo, timeTableSeq, panelGenRefNo, dto.getMidPoint25(), tripType, group,
							Integer.toString(count), loginUser, midPointCode);
					midpointCount = midpointCount + 1;
				}
				if (dto.getMidPoint26() != null && !dto.getMidPoint26().isEmpty()
						&& !dto.getMidPoint26().trim().equalsIgnoreCase("")) {
					String midPointCode = midPointCodesList.get(midpointCount).getMidPointCode();
					insertQuery(con, routeNo, timeTableSeq, panelGenRefNo, dto.getMidPoint26(), tripType, group,
							Integer.toString(count), loginUser, midPointCode);
					midpointCount = midpointCount + 1;
				}
				if (dto.getMidPoint27() != null && !dto.getMidPoint27().isEmpty()
						&& !dto.getMidPoint27().trim().equalsIgnoreCase("")) {
					String midPointCode = midPointCodesList.get(midpointCount).getMidPointCode();
					insertQuery(con, routeNo, timeTableSeq, panelGenRefNo, dto.getMidPoint27(), tripType, group,
							Integer.toString(count), loginUser, midPointCode);
					midpointCount = midpointCount + 1;
				}
				if (dto.getMidPoint28() != null && !dto.getMidPoint28().isEmpty()
						&& !dto.getMidPoint28().trim().equalsIgnoreCase("")) {
					String midPointCode = midPointCodesList.get(midpointCount).getMidPointCode();
					insertQuery(con, routeNo, timeTableSeq, panelGenRefNo, dto.getMidPoint28(), tripType, group,
							Integer.toString(count), loginUser, midPointCode);
					midpointCount = midpointCount + 1;
				}
				if (dto.getMidPoint29() != null && !dto.getMidPoint29().isEmpty()
						&& !dto.getMidPoint29().trim().equalsIgnoreCase("")) {
					String midPointCode = midPointCodesList.get(midpointCount).getMidPointCode();
					insertQuery(con, routeNo, timeTableSeq, panelGenRefNo, dto.getMidPoint29(), tripType, group,
							Integer.toString(count), loginUser, midPointCode);
					midpointCount = midpointCount + 1;
				}
				if (dto.getMidPoint30() != null && !dto.getMidPoint30().isEmpty()
						&& !dto.getMidPoint30().trim().equalsIgnoreCase("")) {
					String midPointCode = midPointCodesList.get(midpointCount).getMidPointCode();
					insertQuery(con, routeNo, timeTableSeq, panelGenRefNo, dto.getMidPoint30(), tripType, group,
							Integer.toString(count), loginUser, midPointCode);
					midpointCount = midpointCount + 1;
				}
				if (dto.getMidPoint31() != null && !dto.getMidPoint31().isEmpty()
						&& !dto.getMidPoint31().trim().equalsIgnoreCase("")) {
					String midPointCode = midPointCodesList.get(midpointCount).getMidPointCode();
					insertQuery(con, routeNo, timeTableSeq, panelGenRefNo, dto.getMidPoint31(), tripType, group,
							Integer.toString(count), loginUser, midPointCode);
					midpointCount = midpointCount + 1;
				}
				if (dto.getMidPoint32() != null && !dto.getMidPoint32().isEmpty()
						&& !dto.getMidPoint32().trim().equalsIgnoreCase("")) {
					String midPointCode = midPointCodesList.get(midpointCount).getMidPointCode();
					insertQuery(con, routeNo, timeTableSeq, panelGenRefNo, dto.getMidPoint32(), tripType, group,
							Integer.toString(count), loginUser, midPointCode);
					midpointCount = midpointCount + 1;
				}
				if (dto.getMidPoint33() != null && !dto.getMidPoint33().isEmpty()
						&& !dto.getMidPoint33().trim().equalsIgnoreCase("")) {
					String midPointCode = midPointCodesList.get(midpointCount).getMidPointCode();
					insertQuery(con, routeNo, timeTableSeq, panelGenRefNo, dto.getMidPoint33(), tripType, group,
							Integer.toString(count), loginUser, midPointCode);
					midpointCount = midpointCount + 1;
				}
				if (dto.getMidPoint34() != null && !dto.getMidPoint34().isEmpty()
						&& !dto.getMidPoint34().trim().equalsIgnoreCase("")) {
					String midPointCode = midPointCodesList.get(midpointCount).getMidPointCode();
					insertQuery(con, routeNo, timeTableSeq, panelGenRefNo, dto.getMidPoint34(), tripType, group,
							Integer.toString(count), loginUser, midPointCode);
					midpointCount = midpointCount + 1;
				}
				if (dto.getMidPoint35() != null && !dto.getMidPoint35().isEmpty()
						&& !dto.getMidPoint35().trim().equalsIgnoreCase("")) {
					String midPointCode = midPointCodesList.get(midpointCount).getMidPointCode();
					insertQuery(con, routeNo, timeTableSeq, panelGenRefNo, dto.getMidPoint35(), tripType, group,
							Integer.toString(count), loginUser, midPointCode);
					midpointCount = midpointCount + 1;
				}
				if (dto.getMidPoint36() != null && !dto.getMidPoint36().isEmpty()
						&& !dto.getMidPoint36().trim().equalsIgnoreCase("")) {
					String midPointCode = midPointCodesList.get(midpointCount).getMidPointCode();
					insertQuery(con, routeNo, timeTableSeq, panelGenRefNo, dto.getMidPoint36(), tripType, group,
							Integer.toString(count), loginUser, midPointCode);
					midpointCount = midpointCount + 1;
				}
				if (dto.getMidPoint37() != null && !dto.getMidPoint37().isEmpty()
						&& !dto.getMidPoint37().trim().equalsIgnoreCase("")) {
					String midPointCode = midPointCodesList.get(midpointCount).getMidPointCode();
					insertQuery(con, routeNo, timeTableSeq, panelGenRefNo, dto.getMidPoint37(), tripType, group,
							Integer.toString(count), loginUser, midPointCode);
					midpointCount = midpointCount + 1;
				}
				if (dto.getMidPoint38() != null && !dto.getMidPoint38().isEmpty()
						&& !dto.getMidPoint38().trim().equalsIgnoreCase("")) {
					String midPointCode = midPointCodesList.get(midpointCount).getMidPointCode();
					insertQuery(con, routeNo, timeTableSeq, panelGenRefNo, dto.getMidPoint38(), tripType, group,
							Integer.toString(count), loginUser, midPointCode);
					midpointCount = midpointCount + 1;
				}
				if (dto.getMidPoint39() != null && !dto.getMidPoint39().isEmpty()
						&& !dto.getMidPoint39().trim().equalsIgnoreCase("")) {
					String midPointCode = midPointCodesList.get(midpointCount).getMidPointCode();
					insertQuery(con, routeNo, timeTableSeq, panelGenRefNo, dto.getMidPoint39(), tripType, group,
							Integer.toString(count), loginUser, midPointCode);
					midpointCount = midpointCount + 1;
				}
				if (dto.getMidPoint40() != null && !dto.getMidPoint40().isEmpty()
						&& !dto.getMidPoint40().trim().equalsIgnoreCase("")) {
					String midPointCode = midPointCodesList.get(midpointCount).getMidPointCode();
					insertQuery(con, routeNo, timeTableSeq, panelGenRefNo, dto.getMidPoint40(), tripType, group,
							Integer.toString(count), loginUser, midPointCode);
					midpointCount = midpointCount + 1;
				}

				count = count + 1;
				midpointCount = 0;
			}
			/** insert data into nt_t_midpoint_timetable end **/

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

	}

	public boolean insertQuery(Connection con, String routeNo, Long timeTableSeq, String panelGenRefNo, String midPoint,
			String tripType, String groupNo, String midpointId, String createdBy, String midPointCode)
			throws SQLException {

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		PreparedStatement ps = null;

		boolean isModelSave = false;

		long seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_t_midpoint_timetable");

		String query = "INSERT INTO public.nt_t_midpoint_timetable "
				+ "(seq_no, rotue_no, time_table_gen_seq, panel_generator_ref_no, midpoints, trip_type, group_no, midpoint_id, midpoint_code, created_by, created_date) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

		ps = con.prepareStatement(query);

		ps.setLong(1, seqNo);
		ps.setString(2, routeNo);
		ps.setLong(3, timeTableSeq);
		ps.setString(4, panelGenRefNo);
		ps.setString(5, midPoint);
		ps.setString(6, tripType);
		ps.setString(7, groupNo);
		ps.setString(8, midpointId);
		ps.setString(9, midPointCode);
		ps.setString(10, createdBy);
		ps.setTimestamp(11, timestamp);

		int i = ps.executeUpdate();
		if (i > 0) {
			isModelSave = true;
		} else {
			isModelSave = false;
			return isModelSave;
		}

		ConnectionManager.close(ps);

		return isModelSave;
	}

	@Override
	public boolean checkDataAvailableInNt_t_midpoint_timetable(String routeNo, String busCategory, String busType,
			String group) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean exist = false;
		try {

			con = ConnectionManager.getConnection();

			// get panel generator ref number start
			String selct = "SELECT * FROM public.nt_t_midpoint_timetable where 	rotue_no=? and trip_type=? and group_no=? and "
					+ "panel_generator_ref_no=(SELECT tg_generator_ref_no FROM public.nt_m_trips_generator where tg_route_no=? and tg_bus_category=?) ";

			ps = con.prepareStatement(selct);
			ps.setString(1, routeNo);
			ps.setString(2, busType);
			ps.setString(3, group);
			ps.setString(4, routeNo);
			ps.setString(5, busCategory);

			rs = ps.executeQuery();

			while (rs.next()) {
				exist = true;
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return exist;
	}

	@Override
	public List<MidPointTimesDTO> selectTimeTableData(String routeNo, String busCategory, String busType,
			String group) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<MidPointTimesDTO> dtoList = new ArrayList<MidPointTimesDTO>();
		int count = 0;
		try {

			con = ConnectionManager.getConnection();

			// get mid point code count start
			String select = "select count(distinct midpoint_code) as midpoint_count FROM public.nt_t_midpoint_timetable where  rotue_no=? and trip_type=? and group_no=? and "
					+ "panel_generator_ref_no=(SELECT tg_generator_ref_no FROM public.nt_m_trips_generator where tg_route_no=?  and tg_bus_category=?) ";

			ps = con.prepareStatement(select);
			ps.setString(1, routeNo);
			ps.setString(2, busType);
			ps.setString(3, group);
			ps.setString(4, routeNo);
			ps.setString(5, busCategory);

			rs = ps.executeQuery();

			while (rs.next()) {
				count = rs.getInt("midpoint_count");
			}

			try {
				if (ps != null)
					ps.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// get mid point code count end

			// get panel generator ref number start
			String selct = "SELECT midpoints FROM public.nt_t_midpoint_timetable where 	rotue_no=? and trip_type=? and group_no=? and "
					+ "panel_generator_ref_no=(SELECT tg_generator_ref_no FROM public.nt_m_trips_generator where tg_route_no=? and tg_bus_category=?) order by seq_no";

			ps = con.prepareStatement(selct);
			ps.setString(1, routeNo);
			ps.setString(2, busType);
			ps.setString(3, group);
			ps.setString(4, routeNo);
			ps.setString(5, busCategory);

			rs = ps.executeQuery();

			int tempCount = 0;
			MidPointTimesDTO dto = new MidPointTimesDTO();
			while (rs.next()) {

				if (tempCount == count) {
					dtoList.add(dto);
					tempCount = 0;
					dto = new MidPointTimesDTO();
				}

				if (tempCount == 0) {
					dto.setCategory(busCategory);
					dto.setMidPoint1(rs.getString("midpoints"));
				}
				if (tempCount == 1) {
					dto.setCategory(busCategory);
					dto.setMidPoint2(rs.getString("midpoints"));
				}
				if (tempCount == 2) {
					dto.setCategory(busCategory);
					dto.setMidPoint3(rs.getString("midpoints"));
				}
				if (tempCount == 3) {
					dto.setCategory(busCategory);
					dto.setMidPoint4(rs.getString("midpoints"));
				}
				if (tempCount == 4) {
					dto.setCategory(busCategory);
					dto.setMidPoint5(rs.getString("midpoints"));
				}
				if (tempCount == 5) {
					dto.setCategory(busCategory);
					dto.setMidPoint6(rs.getString("midpoints"));
				}
				if (tempCount == 6) {
					dto.setCategory(busCategory);
					dto.setMidPoint7(rs.getString("midpoints"));
				}
				if (tempCount == 7) {
					dto.setCategory(busCategory);
					dto.setMidPoint8(rs.getString("midpoints"));
				}
				if (tempCount == 8) {
					dto.setCategory(busCategory);
					dto.setMidPoint9(rs.getString("midpoints"));
				}
				if (tempCount == 9) {
					dto.setCategory(busCategory);
					dto.setMidPoint10(rs.getString("midpoints"));
				}
				if (tempCount == 10) {
					dto.setCategory(busCategory);
					dto.setMidPoint11(rs.getString("midpoints"));
				}
				if (tempCount == 11) {
					dto.setCategory(busCategory);
					dto.setMidPoint12(rs.getString("midpoints"));
				}
				if (tempCount == 12) {
					dto.setCategory(busCategory);
					dto.setMidPoint13(rs.getString("midpoints"));
				}
				if (tempCount == 13) {
					dto.setCategory(busCategory);
					dto.setMidPoint14(rs.getString("midpoints"));
				}
				if (tempCount == 14) {
					dto.setCategory(busCategory);
					dto.setMidPoint15(rs.getString("midpoints"));
				}
				if (tempCount == 15) {
					dto.setCategory(busCategory);
					dto.setMidPoint16(rs.getString("midpoints"));
				}
				if (tempCount == 16) {
					dto.setCategory(busCategory);
					dto.setMidPoint17(rs.getString("midpoints"));
				}
				if (tempCount == 17) {
					dto.setCategory(busCategory);
					dto.setMidPoint18(rs.getString("midpoints"));
				}
				if (tempCount == 18) {
					dto.setCategory(busCategory);
					dto.setMidPoint19(rs.getString("midpoints"));
				}
				if (tempCount == 19) {
					dto.setCategory(busCategory);
					dto.setMidPoint20(rs.getString("midpoints"));
				}
				if (tempCount == 20) {
					dto.setCategory(busCategory);
					dto.setMidPoint21(rs.getString("midpoints"));
				}
				if (tempCount == 21) {
					dto.setCategory(busCategory);
					dto.setMidPoint22(rs.getString("midpoints"));
				}
				if (tempCount == 22) {
					dto.setCategory(busCategory);
					dto.setMidPoint23(rs.getString("midpoints"));
				}
				if (tempCount == 23) {
					dto.setCategory(busCategory);
					dto.setMidPoint24(rs.getString("midpoints"));
				}
				if (tempCount == 24) {
					dto.setCategory(busCategory);
					dto.setMidPoint25(rs.getString("midpoints"));
				}
				if (tempCount == 25) {
					dto.setCategory(busCategory);
					dto.setMidPoint26(rs.getString("midpoints"));
				}
				if (tempCount == 26) {
					dto.setCategory(busCategory);
					dto.setMidPoint27(rs.getString("midpoints"));
				}
				if (tempCount == 27) {
					dto.setCategory(busCategory);
					dto.setMidPoint28(rs.getString("midpoints"));
				}
				if (tempCount == 28) {
					dto.setCategory(busCategory);
					dto.setMidPoint29(rs.getString("midpoints"));
				}
				if (tempCount == 29) {
					dto.setCategory(busCategory);
					dto.setMidPoint30(rs.getString("midpoints"));
				}
				if (tempCount == 30) {
					dto.setCategory(busCategory);
					dto.setMidPoint31(rs.getString("midpoints"));
				}
				if (tempCount == 31) {
					dto.setCategory(busCategory);
					dto.setMidPoint32(rs.getString("midpoints"));
				}
				if (tempCount == 32) {
					dto.setCategory(busCategory);
					dto.setMidPoint33(rs.getString("midpoints"));
				}
				if (tempCount == 33) {
					dto.setCategory(busCategory);
					dto.setMidPoint34(rs.getString("midpoints"));
				}
				if (tempCount == 34) {
					dto.setCategory(busCategory);
					dto.setMidPoint35(rs.getString("midpoints"));
				}
				if (tempCount == 35) {
					dto.setCategory(busCategory);
					dto.setMidPoint36(rs.getString("midpoints"));
				}
				if (tempCount == 36) {
					dto.setCategory(busCategory);
					dto.setMidPoint37(rs.getString("midpoints"));
				}
				if (tempCount == 37) {
					dto.setCategory(busCategory);
					dto.setMidPoint38(rs.getString("midpoints"));
				}
				if (tempCount == 38) {
					dto.setCategory(busCategory);
					dto.setMidPoint39(rs.getString("midpoints"));
				}
				if (tempCount == 39) {
					dto.setCategory(busCategory);
					dto.setMidPoint40(rs.getString("midpoints"));
				}

				tempCount = tempCount + 1;

			}

			// last DTO add to List
			dtoList.add(dto);

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return dtoList;
	}

	@Override
	public boolean updateNt_t_midpoint_timeTable(String routNum, MidPointTimesDTO editMidpointDTO, String category,
			String tripType, String group, String loginUser) {

		Connection con = null;

		try {
			con = ConnectionManager.getConnection();

			if (editMidpointDTO.getNewMidpoint1() != null && !editMidpointDTO.getNewMidpoint1().isEmpty()
					&& !editMidpointDTO.getNewMidpoint1().trim().equalsIgnoreCase("")) {
				try {

					updateTable(con, routNum, editMidpointDTO.getNewMidpoint1(), editMidpointDTO.getMidPoint1(),
							category, tripType, group, loginUser);

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			if (editMidpointDTO.getNewMidpoint2() != null && !editMidpointDTO.getNewMidpoint2().isEmpty()
					&& !editMidpointDTO.getNewMidpoint2().trim().equalsIgnoreCase("")) {
				try {

					updateTable(con, routNum, editMidpointDTO.getNewMidpoint2(), editMidpointDTO.getMidPoint2(),
							category, tripType, group, loginUser);

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			if (editMidpointDTO.getNewMidpoint3() != null && !editMidpointDTO.getNewMidpoint3().isEmpty()
					&& !editMidpointDTO.getNewMidpoint3().trim().equalsIgnoreCase("")) {
				try {

					updateTable(con, routNum, editMidpointDTO.getNewMidpoint3(), editMidpointDTO.getMidPoint3(),
							category, tripType, group, loginUser);

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			if (editMidpointDTO.getNewMidpoint4() != null && !editMidpointDTO.getNewMidpoint4().isEmpty()
					&& !editMidpointDTO.getNewMidpoint4().trim().equalsIgnoreCase("")) {
				try {

					updateTable(con, routNum, editMidpointDTO.getNewMidpoint4(), editMidpointDTO.getMidPoint4(),
							category, tripType, group, loginUser);

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			if (editMidpointDTO.getNewMidpoint5() != null && !editMidpointDTO.getNewMidpoint5().isEmpty()
					&& !editMidpointDTO.getNewMidpoint5().trim().equalsIgnoreCase("")) {
				try {

					updateTable(con, routNum, editMidpointDTO.getNewMidpoint5(), editMidpointDTO.getMidPoint5(),
							category, tripType, group, loginUser);

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			if (editMidpointDTO.getNewMidpoint6() != null && !editMidpointDTO.getNewMidpoint6().isEmpty()
					&& !editMidpointDTO.getNewMidpoint6().trim().equalsIgnoreCase("")) {
				try {

					updateTable(con, routNum, editMidpointDTO.getNewMidpoint6(), editMidpointDTO.getMidPoint6(),
							category, tripType, group, loginUser);

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			if (editMidpointDTO.getNewMidpoint7() != null && !editMidpointDTO.getNewMidpoint7().isEmpty()
					&& !editMidpointDTO.getNewMidpoint7().trim().equalsIgnoreCase("")) {
				try {

					updateTable(con, routNum, editMidpointDTO.getNewMidpoint7(), editMidpointDTO.getMidPoint7(),
							category, tripType, group, loginUser);

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			if (editMidpointDTO.getNewMidpoint8() != null && !editMidpointDTO.getNewMidpoint8().isEmpty()
					&& !editMidpointDTO.getNewMidpoint8().trim().equalsIgnoreCase("")) {
				try {

					updateTable(con, routNum, editMidpointDTO.getNewMidpoint8(), editMidpointDTO.getMidPoint8(),
							category, tripType, group, loginUser);

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			if (editMidpointDTO.getNewMidpoint9() != null && !editMidpointDTO.getNewMidpoint9().isEmpty()
					&& !editMidpointDTO.getNewMidpoint9().trim().equalsIgnoreCase("")) {
				try {

					updateTable(con, routNum, editMidpointDTO.getNewMidpoint9(), editMidpointDTO.getMidPoint9(),
							category, tripType, group, loginUser);

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			if (editMidpointDTO.getNewMidpoint10() != null && !editMidpointDTO.getNewMidpoint10().isEmpty()
					&& !editMidpointDTO.getNewMidpoint10().trim().equalsIgnoreCase("")) {
				try {

					updateTable(con, routNum, editMidpointDTO.getNewMidpoint10(), editMidpointDTO.getMidPoint10(),
							category, tripType, group, loginUser);

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			if (editMidpointDTO.getNewMidpoint11() != null && !editMidpointDTO.getNewMidpoint11().isEmpty()
					&& !editMidpointDTO.getNewMidpoint11().trim().equalsIgnoreCase("")) {
				try {

					updateTable(con, routNum, editMidpointDTO.getNewMidpoint11(), editMidpointDTO.getMidPoint11(),
							category, tripType, group, loginUser);

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			if (editMidpointDTO.getNewMidpoint12() != null && !editMidpointDTO.getNewMidpoint12().isEmpty()
					&& !editMidpointDTO.getNewMidpoint12().trim().equalsIgnoreCase("")) {
				try {

					updateTable(con, routNum, editMidpointDTO.getNewMidpoint12(), editMidpointDTO.getMidPoint12(),
							category, tripType, group, loginUser);

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			if (editMidpointDTO.getNewMidpoint13() != null && !editMidpointDTO.getNewMidpoint13().isEmpty()
					&& !editMidpointDTO.getNewMidpoint13().trim().equalsIgnoreCase("")) {
				try {

					updateTable(con, routNum, editMidpointDTO.getNewMidpoint13(), editMidpointDTO.getMidPoint13(),
							category, tripType, group, loginUser);

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			if (editMidpointDTO.getNewMidpoint14() != null && !editMidpointDTO.getNewMidpoint14().isEmpty()
					&& !editMidpointDTO.getNewMidpoint14().trim().equalsIgnoreCase("")) {
				try {

					updateTable(con, routNum, editMidpointDTO.getNewMidpoint14(), editMidpointDTO.getMidPoint14(),
							category, tripType, group, loginUser);

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			if (editMidpointDTO.getNewMidpoint15() != null && !editMidpointDTO.getNewMidpoint15().isEmpty()
					&& !editMidpointDTO.getNewMidpoint15().trim().equalsIgnoreCase("")) {
				try {

					updateTable(con, routNum, editMidpointDTO.getNewMidpoint15(), editMidpointDTO.getMidPoint15(),
							category, tripType, group, loginUser);

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			if (editMidpointDTO.getNewMidpoint16() != null && !editMidpointDTO.getNewMidpoint16().isEmpty()
					&& !editMidpointDTO.getNewMidpoint16().trim().equalsIgnoreCase("")) {
				try {

					updateTable(con, routNum, editMidpointDTO.getNewMidpoint16(), editMidpointDTO.getMidPoint16(),
							category, tripType, group, loginUser);

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			if (editMidpointDTO.getNewMidpoint17() != null && !editMidpointDTO.getNewMidpoint17().isEmpty()
					&& !editMidpointDTO.getNewMidpoint17().trim().equalsIgnoreCase("")) {
				try {

					updateTable(con, routNum, editMidpointDTO.getNewMidpoint17(), editMidpointDTO.getMidPoint17(),
							category, tripType, group, loginUser);

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			if (editMidpointDTO.getNewMidpoint18() != null && !editMidpointDTO.getNewMidpoint18().isEmpty()
					&& !editMidpointDTO.getNewMidpoint18().trim().equalsIgnoreCase("")) {
				try {

					updateTable(con, routNum, editMidpointDTO.getNewMidpoint18(), editMidpointDTO.getMidPoint18(),
							category, tripType, group, loginUser);

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			if (editMidpointDTO.getNewMidpoint19() != null && !editMidpointDTO.getNewMidpoint19().isEmpty()
					&& !editMidpointDTO.getNewMidpoint19().trim().equalsIgnoreCase("")) {
				try {

					updateTable(con, routNum, editMidpointDTO.getNewMidpoint19(), editMidpointDTO.getMidPoint19(),
							category, tripType, group, loginUser);

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			if (editMidpointDTO.getNewMidpoint20() != null && !editMidpointDTO.getNewMidpoint20().isEmpty()
					&& !editMidpointDTO.getNewMidpoint20().trim().equalsIgnoreCase("")) {
				try {

					updateTable(con, routNum, editMidpointDTO.getNewMidpoint20(), editMidpointDTO.getMidPoint20(),
							category, tripType, group, loginUser);

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			if (editMidpointDTO.getNewMidpoint21() != null && !editMidpointDTO.getNewMidpoint21().isEmpty()
					&& !editMidpointDTO.getNewMidpoint21().trim().equalsIgnoreCase("")) {
				try {

					updateTable(con, routNum, editMidpointDTO.getNewMidpoint21(), editMidpointDTO.getMidPoint21(),
							category, tripType, group, loginUser);

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			if (editMidpointDTO.getNewMidpoint22() != null && !editMidpointDTO.getNewMidpoint22().isEmpty()
					&& !editMidpointDTO.getNewMidpoint22().trim().equalsIgnoreCase("")) {
				try {

					updateTable(con, routNum, editMidpointDTO.getNewMidpoint22(), editMidpointDTO.getMidPoint22(),
							category, tripType, group, loginUser);

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			if (editMidpointDTO.getNewMidpoint23() != null && !editMidpointDTO.getNewMidpoint23().isEmpty()
					&& !editMidpointDTO.getNewMidpoint23().trim().equalsIgnoreCase("")) {
				try {

					updateTable(con, routNum, editMidpointDTO.getNewMidpoint23(), editMidpointDTO.getMidPoint23(),
							category, tripType, group, loginUser);

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			if (editMidpointDTO.getNewMidpoint24() != null && !editMidpointDTO.getNewMidpoint24().isEmpty()
					&& !editMidpointDTO.getNewMidpoint24().trim().equalsIgnoreCase("")) {
				try {

					updateTable(con, routNum, editMidpointDTO.getNewMidpoint24(), editMidpointDTO.getMidPoint24(),
							category, tripType, group, loginUser);

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			if (editMidpointDTO.getNewMidpoint25() != null && !editMidpointDTO.getNewMidpoint25().isEmpty()
					&& !editMidpointDTO.getNewMidpoint25().trim().equalsIgnoreCase("")) {
				try {

					updateTable(con, routNum, editMidpointDTO.getNewMidpoint25(), editMidpointDTO.getMidPoint25(),
							category, tripType, group, loginUser);

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			if (editMidpointDTO.getNewMidpoint26() != null && !editMidpointDTO.getNewMidpoint26().isEmpty()
					&& !editMidpointDTO.getNewMidpoint26().trim().equalsIgnoreCase("")) {
				try {

					updateTable(con, routNum, editMidpointDTO.getNewMidpoint26(), editMidpointDTO.getMidPoint26(),
							category, tripType, group, loginUser);

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			if (editMidpointDTO.getNewMidpoint27() != null && !editMidpointDTO.getNewMidpoint27().isEmpty()
					&& !editMidpointDTO.getNewMidpoint27().trim().equalsIgnoreCase("")) {
				try {

					updateTable(con, routNum, editMidpointDTO.getNewMidpoint27(), editMidpointDTO.getMidPoint27(),
							category, tripType, group, loginUser);

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			if (editMidpointDTO.getNewMidpoint28() != null && !editMidpointDTO.getNewMidpoint28().isEmpty()
					&& !editMidpointDTO.getNewMidpoint28().trim().equalsIgnoreCase("")) {
				try {

					updateTable(con, routNum, editMidpointDTO.getNewMidpoint28(), editMidpointDTO.getMidPoint28(),
							category, tripType, group, loginUser);

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			if (editMidpointDTO.getNewMidpoint29() != null && !editMidpointDTO.getNewMidpoint29().isEmpty()
					&& !editMidpointDTO.getNewMidpoint29().trim().equalsIgnoreCase("")) {
				try {

					updateTable(con, routNum, editMidpointDTO.getNewMidpoint29(), editMidpointDTO.getMidPoint29(),
							category, tripType, group, loginUser);

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			if (editMidpointDTO.getNewMidpoint30() != null && !editMidpointDTO.getNewMidpoint30().isEmpty()
					&& !editMidpointDTO.getNewMidpoint30().trim().equalsIgnoreCase("")) {
				try {

					updateTable(con, routNum, editMidpointDTO.getNewMidpoint30(), editMidpointDTO.getMidPoint30(),
							category, tripType, group, loginUser);

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			if (editMidpointDTO.getNewMidpoint31() != null && !editMidpointDTO.getNewMidpoint31().isEmpty()
					&& !editMidpointDTO.getNewMidpoint31().trim().equalsIgnoreCase("")) {
				try {

					updateTable(con, routNum, editMidpointDTO.getNewMidpoint31(), editMidpointDTO.getMidPoint31(),
							category, tripType, group, loginUser);

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			if (editMidpointDTO.getNewMidpoint32() != null && !editMidpointDTO.getNewMidpoint32().isEmpty()
					&& !editMidpointDTO.getNewMidpoint32().trim().equalsIgnoreCase("")) {
				try {

					updateTable(con, routNum, editMidpointDTO.getNewMidpoint32(), editMidpointDTO.getMidPoint32(),
							category, tripType, group, loginUser);

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			if (editMidpointDTO.getNewMidpoint33() != null && !editMidpointDTO.getNewMidpoint33().isEmpty()
					&& !editMidpointDTO.getNewMidpoint33().trim().equalsIgnoreCase("")) {
				try {

					updateTable(con, routNum, editMidpointDTO.getNewMidpoint33(), editMidpointDTO.getMidPoint33(),
							category, tripType, group, loginUser);

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			if (editMidpointDTO.getNewMidpoint34() != null && !editMidpointDTO.getNewMidpoint34().isEmpty()
					&& !editMidpointDTO.getNewMidpoint34().trim().equalsIgnoreCase("")) {
				try {

					updateTable(con, routNum, editMidpointDTO.getNewMidpoint34(), editMidpointDTO.getMidPoint34(),
							category, tripType, group, loginUser);

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			if (editMidpointDTO.getNewMidpoint35() != null && !editMidpointDTO.getNewMidpoint35().isEmpty()
					&& !editMidpointDTO.getNewMidpoint35().trim().equalsIgnoreCase("")) {
				try {

					updateTable(con, routNum, editMidpointDTO.getNewMidpoint35(), editMidpointDTO.getMidPoint35(),
							category, tripType, group, loginUser);

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			if (editMidpointDTO.getNewMidpoint36() != null && !editMidpointDTO.getNewMidpoint36().isEmpty()
					&& !editMidpointDTO.getNewMidpoint36().trim().equalsIgnoreCase("")) {
				try {

					updateTable(con, routNum, editMidpointDTO.getNewMidpoint36(), editMidpointDTO.getMidPoint36(),
							category, tripType, group, loginUser);

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			if (editMidpointDTO.getNewMidpoint37() != null && !editMidpointDTO.getNewMidpoint37().isEmpty()
					&& !editMidpointDTO.getNewMidpoint37().trim().equalsIgnoreCase("")) {
				try {

					updateTable(con, routNum, editMidpointDTO.getNewMidpoint37(), editMidpointDTO.getMidPoint37(),
							category, tripType, group, loginUser);

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			if (editMidpointDTO.getNewMidpoint38() != null && !editMidpointDTO.getNewMidpoint38().isEmpty()
					&& !editMidpointDTO.getNewMidpoint38().trim().equalsIgnoreCase("")) {
				try {

					updateTable(con, routNum, editMidpointDTO.getNewMidpoint38(), editMidpointDTO.getMidPoint38(),
							category, tripType, group, loginUser);

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			if (editMidpointDTO.getNewMidpoint39() != null && !editMidpointDTO.getNewMidpoint39().isEmpty()
					&& !editMidpointDTO.getNewMidpoint39().trim().equalsIgnoreCase("")) {
				try {

					updateTable(con, routNum, editMidpointDTO.getNewMidpoint39(), editMidpointDTO.getMidPoint39(),
							category, tripType, group, loginUser);

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			if (editMidpointDTO.getNewMidpoint40() != null && !editMidpointDTO.getNewMidpoint40().isEmpty()
					&& !editMidpointDTO.getNewMidpoint40().trim().equalsIgnoreCase("")) {
				try {

					updateTable(con, routNum, editMidpointDTO.getNewMidpoint40(), editMidpointDTO.getMidPoint40(),
							category, tripType, group, loginUser);

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			ConnectionManager.close(con);
		}

		return true;
	}

	public void updateTable(Connection con, String routNum, String newMidpoint, String oldMidPoint, String category,
			String tripType, String group, String loginUser) throws SQLException {

		PreparedStatement stmt = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		String newMP = oldMidPoint.substring(oldMidPoint.lastIndexOf("(") + 1);

		String sql = "UPDATE public.nt_t_midpoint_timetable " + "SET midpoints=?, modify_by=?, modify_date=? "
				+ "WHERE rotue_no=? and trip_type=? and group_no=? and midpoints=? "
				+ "and panel_generator_ref_no=(SELECT tg_generator_ref_no FROM public.nt_m_trips_generator where tg_route_no=?  and tg_bus_category=?)";

		stmt = con.prepareStatement(sql);

		stmt.setString(1, newMidpoint + " (" + newMP);
		stmt.setString(2, loginUser);
		stmt.setTimestamp(3, timestamp);
		stmt.setString(4, routNum);
		stmt.setString(5, tripType);
		stmt.setString(6, group);
		stmt.setString(7, oldMidPoint);
		stmt.setString(8, routNum);
		stmt.setString(9, category);

		stmt.executeUpdate();

		ConnectionManager.close(stmt);

	}

	@Override
	public List<BusFareDTO> retrieveCategoryList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<BusFareDTO> data = new ArrayList<BusFareDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT code,description FROM public.nt_r_service_types where  active='A'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			BusFareDTO dto;

			while (rs.next()) {
				dto = new BusFareDTO();

				dto.setTempBusCategory(rs.getString("description"));
				dto.setTempBusCategoryCode(rs.getString("code"));

				data.add(dto);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return data;
	}

	@Override
	public List<RouteDTO> retrieveViaList(String routeNum) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<RouteDTO> routeNoList = new ArrayList<RouteDTO>();
		List<RouteDTO> returnList = new ArrayList<RouteDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT rou_via FROM public.nt_r_route where rou_number=? and active='A'";

			ps = con.prepareStatement(query);
			ps.setString(1, routeNum);
			rs = ps.executeQuery();
			RouteDTO e;

			while (rs.next()) {
				e = new RouteDTO();
				e.setVia(rs.getString("rou_via"));
				routeNoList.add(e);
			}

			String where = null;
			boolean addOR = false;
			if (routeNoList != null && routeNoList.size() != 0) {
				for (RouteDTO dto : routeNoList) {
					if (!addOR) {
						addOR = true;
						where = "rou_via like '%" + dto.getVia() + "%'";
					} else {
						where = where + " or rou_via like '%" + dto.getVia() + "%'";
					}
				}
			}

			try {
				if (rs != null)
					rs.close();
			} catch (Exception exc) {
				exc.printStackTrace();
			}
			try {
				if (ps != null)
					ps.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			if (addOR) {
				String query2 = "SELECT distinct rou_description, rou_number FROM public.nt_r_route where rou_number in("
						+ "SELECT distinct rou_number FROM public.nt_r_route where " + where + " and active='A'"
						+ ")  and rou_number!='" + routeNum + "' and active='A'";

				ps = con.prepareStatement(query2);
				rs = ps.executeQuery();

				while (rs.next()) {
					RouteDTO routeDTO = new RouteDTO();
					routeDTO.setRouteNo(rs.getString("rou_number"));
					routeDTO.setRouteDes(rs.getString("rou_description"));
					returnList.add(routeDTO);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return returnList;

	}

	@Override
	public List<MidpointUIDTO> retrieveMidPointNameListForRoute(String routeNo, String busCategory) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<MidpointUIDTO> midPointNamesList = new ArrayList<MidpointUIDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT a.mp_midpoint_code, b.midpoint_name "
					+ "FROM public.nt_m_route_midpoint a, public.nt_r_station b "
					+ "where  a.mp_route_no=? and a.mp_bus_type=? and a.mp_midpoint_code=b.code order by a.seq ASC";

			ps = con.prepareStatement(query2);
			ps.setString(1, routeNo);
			ps.setString(2, busCategory);
			rs = ps.executeQuery();

			while (rs.next()) {
				MidpointUIDTO dto = new MidpointUIDTO();
				dto.setMidPointCode(rs.getString("mp_midpoint_code"));
				dto.setMidPointDesc(rs.getString("midpoint_name"));
				midPointNamesList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return midPointNamesList;

	}

	@Override
	public List<String> retrieveDataOfMidPoint(String midPointCode, String tripType, String group, String routeNum,
			String busCategory) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> midPointNamesListStr = new ArrayList<String>();

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM public.nt_t_midpoint_timetable "
					+ "where trip_type=? and group_no=? and rotue_no=? and midpoint_code=? and "
					+ "panel_generator_ref_no = (select tg_generator_ref_no FROM public.nt_m_trips_generator WHERE tg_route_no=? and tg_bus_category=?)";

			ps = con.prepareStatement(query);
			ps.setString(1, tripType);
			ps.setString(2, group);
			ps.setString(3, routeNum);
			ps.setString(4, midPointCode);
			ps.setString(5, routeNum);
			ps.setString(6, busCategory);

			rs = ps.executeQuery();

			while (rs.next()) {
				midPointNamesListStr.add(rs.getString("midpoints"));
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return midPointNamesListStr;
	}

	@Override
	public List<MidpointUIDTO> retrieveMidPointNameListForRouteDtoO(String routeNo, String busCategory) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<MidpointUIDTO> midPointNamesList = new ArrayList<MidpointUIDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT a.mp_midpoint_code, b.midpoint_name "
					+ "FROM public.nt_m_route_midpoint_reverse a, public.nt_r_station b "
					+ "where  a.mp_route_no=? and a.mp_bus_type=? and a.mp_midpoint_code=b.code order by a.seq ASC";

			ps = con.prepareStatement(query2);
			ps.setString(1, routeNo);
			ps.setString(2, busCategory);
			rs = ps.executeQuery();

			while (rs.next()) {
				MidpointUIDTO dto = new MidpointUIDTO();
				dto.setMidPointCode(rs.getString("mp_midpoint_code"));
				dto.setMidPointDesc(rs.getString("midpoint_name"));
				midPointNamesList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return midPointNamesList;

	}
}
