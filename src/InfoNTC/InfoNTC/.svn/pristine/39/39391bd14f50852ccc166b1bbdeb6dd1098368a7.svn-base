package lk.informatics.ntc.model.service;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import edu.emory.mathcs.backport.java.util.Collections;
import lk.informatics.ntc.model.dto.RotationHistoryDTO;
import lk.informatics.ntc.model.dto.RouteScheduleDTO;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.roc.utils.common.Utils;

public class RouteScheduleServiceImpl implements RouteScheduleService {

	@Override
	public List<RouteScheduleDTO> getRouteNo() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<RouteScheduleDTO> routeNoList = new ArrayList<RouteScheduleDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select distinct routeno, b.rou_description from public.panel_generator_general_data a inner join public.nt_r_route b on b.rou_number = a.routeno where a.draftdata = 'S'";

			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();
			RouteScheduleDTO e;

			while (rs.next()) {
				e = new RouteScheduleDTO();
				e.setRouteNo(rs.getString("routeno"));
				e.setDes(rs.getString("rou_description"));
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
	public RouteScheduleDTO getRouteDetails(String routeNo, String busCategory) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		RouteScheduleDTO dto = new RouteScheduleDTO();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT nt_m_panelgenerator.ref_no, nt_m_panelgenerator.route_no,"
					+ "nt_m_panelgenerator.bus_category, nt_m_panelgenerator.status , nt_r_service_types.description ,"
					+ "nt_r_route.rou_service_origine, nt_r_route.rou_service_destination "
					+ "FROM public.nt_m_panelgenerator "
					+ "inner join public.nt_r_service_types on nt_r_service_types.code=nt_m_panelgenerator.bus_category "
					+ "inner join public.nt_r_route on nt_r_route.rou_number=nt_m_panelgenerator.route_no "
					+ "inner join public.nt_t_panelgenerator_det "
					+ "on nt_t_panelgenerator_det.seq_panelgenerator = nt_m_panelgenerator.seq "
					+ "where nt_m_panelgenerator.status='A'  and nt_m_panelgenerator.route_no=? "
					+ "and nt_m_panelgenerator.bus_category=?";

			ps = con.prepareStatement(query2);
			ps.setString(1, routeNo);
			ps.setString(2, busCategory);
			rs = ps.executeQuery();

			while (rs.next()) {
				dto.setGeneratedRefNo(rs.getString("ref_no"));
				dto.setBusCategory(rs.getString("bus_category"));
				dto.setBusCategoryDes(rs.getString("description"));
				dto.setOrigin(rs.getString("rou_service_origine"));
				dto.setDestination(rs.getString("rou_service_destination"));

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
	public RouteScheduleDTO getRouteDetailsGroup(String routeNo, String busCategory, String group) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		RouteScheduleDTO dto = new RouteScheduleDTO();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT nt_m_panelgenerator.ref_no, nt_m_panelgenerator.route_no,"
					+ "nt_m_panelgenerator.bus_category, nt_m_panelgenerator.status , nt_r_service_types.description ,"
					+ "nt_r_route.rou_service_origine, nt_r_route.rou_service_destination "
					+ "FROM public.nt_m_panelgenerator "
					+ "inner join public.nt_r_service_types on nt_r_service_types.code=nt_m_panelgenerator.bus_category "
					+ "inner join public.nt_r_route on nt_r_route.rou_number=nt_m_panelgenerator.route_no "
					+ "inner join public.nt_t_panelgenerator_det "
					+ "on nt_t_panelgenerator_det.seq_panelgenerator = nt_m_panelgenerator.seq "
					+ "where nt_m_panelgenerator.status='A'  and nt_m_panelgenerator.route_no=? "
					+ "and nt_m_panelgenerator.bus_category=? and nt_t_panelgenerator_det.group_no = ?";

			ps = con.prepareStatement(query2);
			ps.setString(1, routeNo);
			ps.setString(2, busCategory);
			ps.setString(3, group);
			rs = ps.executeQuery();

			while (rs.next()) {
				dto.setGeneratedRefNo(rs.getString("ref_no"));
				dto.setBusCategory(rs.getString("bus_category"));
				dto.setBusCategoryDes(rs.getString("description"));
				dto.setOrigin(rs.getString("rou_service_origine"));
				dto.setDestination(rs.getString("rou_service_destination"));

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
	public List<RouteScheduleDTO> getGroupNoList(String routeNo, String generatedRefNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<RouteScheduleDTO> routeNoList = new ArrayList<RouteScheduleDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select distinct group_no from nt_t_panelgenerator_det \r\n"
					+ "inner join nt_m_panelgenerator on nt_m_panelgenerator.seq=nt_t_panelgenerator_det.seq_panelgenerator \r\n"
					+ "where nt_m_panelgenerator.route_no=? and nt_t_panelgenerator_det.status='A' order by group_no";

			ps = con.prepareStatement(query2);
			ps.setString(1, routeNo);
			
			rs = ps.executeQuery();
			RouteScheduleDTO e;

			while (rs.next()) {
				e = new RouteScheduleDTO();
				e.setGroupNo(rs.getString("group_no"));
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
	public List<RouteScheduleDTO> getBusCategoryList(String routeNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<RouteScheduleDTO> routeNoList = new ArrayList<RouteScheduleDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select distinct bus_category,  nt_r_service_types.description FROM public.nt_m_panelgenerator "
					+ "inner join public.nt_r_service_types on nt_r_service_types.code=nt_m_panelgenerator.bus_category "
					+ "where status='A' and  route_no=? order by description";

			ps = con.prepareStatement(query2);
			ps.setString(1, routeNo);
			rs = ps.executeQuery();
			RouteScheduleDTO e;

			while (rs.next()) {
				e = new RouteScheduleDTO();
				e.setBusCategory(rs.getString("bus_category"));
				e.setBusCategoryDes(rs.getString("description"));
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
	public RouteScheduleDTO getNoOfBusesAndTripsForRoute(String routeNo, String refNo, String groupNo,
			String tripType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		RouteScheduleDTO dto = new RouteScheduleDTO();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT distinct nt_t_trips_generator_det02.no_of_trips, nt_t_trips_generator_det02.total_bus "
					+ "FROM public.nt_t_trips_generator_det02 "
					+ "inner join public.nt_m_trips_generator on nt_m_trips_generator.tg_trips_ref_code=nt_t_trips_generator_det02.trips_ref_code "
					+ "inner join nt_m_timetable_generator on nt_m_timetable_generator.generator_ref_no= nt_m_trips_generator.tg_generator_ref_no "
					+ "where nt_m_trips_generator.tg_route_no=? and nt_m_trips_generator.tg_generator_ref_no=? "
					+ "and nt_t_trips_generator_det02.group_no=? and nt_t_trips_generator_det02.trip_type=? ";

			ps = con.prepareStatement(query2);
			ps.setString(1, routeNo);
			ps.setString(2, refNo);
			ps.setString(3, groupNo);
			ps.setString(4, tripType);
			rs = ps.executeQuery();

			while (rs.next()) {

				dto.setTotalBuses(rs.getInt("total_bus"));
				dto.setTotaltrips(rs.getInt("no_of_trips"));

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
	public int getFixedOrWithoutFixedTripsCount(String refNo, String groupNo, String tripType, String fixType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int count = 0;

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select count( is_fixed_time) as busCount  FROM public.nt_m_timetable_generator_det "
					+ "WHERE is_fixed_time=? and group_no=? and trip_type=? and generator_ref_no=? and bus_num is not null and bus_num !=''";

			ps = con.prepareStatement(query2);
			ps.setString(1, fixType);
			ps.setString(2, groupNo);
			ps.setString(3, tripType);
			ps.setString(4, refNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				count = rs.getInt("busCount");
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return count;
	}

	@Override
	public List<RouteScheduleDTO> getBusNoList(String routeNo, String refNo, String groupNo, String tripType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<RouteScheduleDTO> busNoList = new ArrayList<RouteScheduleDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT distinct bus_num, generator_ref_no, group_no, trip_type FROM public.nt_m_timetable_generator_det "
					+ "where generator_ref_no=? and group_no=? and trip_type=? and is_fixed_time='N'and "
					+ "bus_num is not null and bus_num !='' order by bus_num ";

			ps = con.prepareStatement(query2);
			ps.setString(1, refNo);
			ps.setString(2, groupNo);
			ps.setString(3, tripType);
			rs = ps.executeQuery();
			RouteScheduleDTO e;

			for (int i = 0; rs.next(); i++) {

				e = new RouteScheduleDTO();
				String id = String.valueOf(i + 1);
				e.setTripId(id);

				e.setBusNo(rs.getString("bus_num"));

				busNoList.add(e);

			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return busNoList;
	}

	@Override
	public int getTotalBusesForRoute(String routeNo, String refNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int count = 0;

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select sum( nt_t_trips_generator_det02.total_bus ) as busCount "
					+ "FROM public.nt_t_trips_generator_det02  "
					+ "inner join public.nt_m_trips_generator on nt_m_trips_generator.tg_trips_ref_code=nt_t_trips_generator_det02.trips_ref_code  "
					+ "where nt_m_trips_generator.tg_route_no=? and nt_m_trips_generator.tg_generator_ref_no=? ";

			ps = con.prepareStatement(query2);
			ps.setString(1, routeNo);
			ps.setString(2, refNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				count = rs.getInt("busCount");
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return count;
	}

	@Override
	public int getTotalTripsForRoute(String refNo, String tripType, String groupNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int count = 0;

		try {
			con = ConnectionManager.getConnection();

			String query2 = "	 select no_of_trips+1  as busCount  \r\n"
					+ "	 from public.nt_t_trips_generator_det02 \r\n"
					+ "	 inner join public.nt_m_trips_generator a on a.tg_trips_ref_code=public.nt_t_trips_generator_det02.trips_ref_code\r\n"
					+ "	 where group_no=? and trip_type=? and a.tg_generator_ref_no=?";

			ps = con.prepareStatement(query2);

			ps.setString(1, groupNo);
			ps.setString(2, tripType);
			ps.setString(3, refNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				count = rs.getInt("busCount");
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return count;
	}

	@Override
	public int getTotalWithoutFixedBusForGroupAndType(String routeNo, String ref, String tripType, String group) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int count = 0;

		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT  sum(nt_t_trips_generator_det02.no_of_pvt_bus) as pvt_other_busCount "
					+ "FROM public.nt_t_trips_generator_det02  "
					+ "inner join public.nt_m_trips_generator on nt_m_trips_generator.tg_trips_ref_code=nt_t_trips_generator_det02.trips_ref_code "
					+ "where nt_m_trips_generator.tg_route_no=? and nt_m_trips_generator.tg_generator_ref_no=? and "
					+ "nt_t_trips_generator_det02.group_no=? and nt_t_trips_generator_det02.trip_type=? ";

			ps = con.prepareStatement(query2);
			ps.setString(1, routeNo);
			ps.setString(2, ref);
			ps.setString(3, group);
			ps.setString(4, tripType);

			rs = ps.executeQuery();

			while (rs.next()) {
				count = rs.getInt("pvt_other_busCount");
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return count;
	}

	@Override
	public int getTotalBusForGroup(String routeNo, String refNo, String groupNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int count = 0;

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select sum( nt_t_trips_generator_det02.no_of_pvt_bus)  as busCount  "
					+ "FROM public.nt_t_trips_generator_det02  "
					+ "inner join public.nt_m_trips_generator on nt_m_trips_generator.tg_trips_ref_code=nt_t_trips_generator_det02.trips_ref_code "
					+ "where nt_m_trips_generator.tg_route_no=? and nt_m_trips_generator.tg_generator_ref_no=? "
					+ "and nt_t_trips_generator_det02.group_no=? ";

			ps = con.prepareStatement(query2);
			ps.setString(1, routeNo);
			ps.setString(2, refNo);
			ps.setString(3, groupNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				count = rs.getInt("busCount");
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return count;
	}

	@Override
	public int getTotalTripsForGroup(String routeNo, String refNo, String groupNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int count = 0;

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select sum( nt_t_trips_generator_det02.no_of_trips)  as tripsCount  "
					+ "FROM public.nt_t_trips_generator_det02  "
					+ "inner join public.nt_m_trips_generator on nt_m_trips_generator.tg_trips_ref_code=nt_t_trips_generator_det02.trips_ref_code "
					+ "where nt_m_trips_generator.tg_route_no=? and nt_m_trips_generator.tg_generator_ref_no=? "
					+ "and nt_t_trips_generator_det02.group_no=? ";

			ps = con.prepareStatement(query2);
			ps.setString(1, routeNo);
			ps.setString(2, refNo);
			ps.setString(3, groupNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				count = rs.getInt("tripsCount");
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return count;
	}

	@Override
	public int getTotalLavesForGroupAndType(String ref, String tripType, String group) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int count = 0;

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select no_of_buses_on_leave FROM public.nt_m_timetable_generator "
					+ "where generator_ref_no=? and group_no=? and trip_type=? ";

			ps = con.prepareStatement(query2);

			ps.setString(1, ref);
			ps.setString(2, group);
			ps.setString(3, tripType);

			rs = ps.executeQuery();

			while (rs.next()) {
				count = rs.getInt("no_of_buses_on_leave");
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return count;
	}

	@Override
	public boolean insertRouteScheduleMasterTableData(RouteScheduleDTO dto, String rotationType, String trip_type,
			String groupNo, String generatedRef, int daysDiff, String user, String routeRefNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isDataSave = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

		try {

			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_route_schedule_generator");

			String query = "INSERT INTO public.nt_m_route_schedule_generator "
					+ "(rs_seq,rs_route_ref_no, rs_generator_ref_no,rs_rotation_type, rs_route_no, rs_bus_category_code, rs_is_swap, rs_trip_type, "
					+ "rs_group_no, rs_start_date, rs_end_date, rs_no_of_dates, created_by, created_date) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? );";

			ps = con.prepareStatement(query);

			try {
				ps.setLong(1, seqNo);
				ps.setString(2, routeRefNo);
				ps.setString(3, generatedRef);
				ps.setString(4, rotationType);
				ps.setString(5, dto.getRouteNo());
				ps.setString(6, dto.getBusCategory());

				if (trip_type.equals("O")) {
					ps.setString(7, "N");
				} else if (trip_type.equals("D")) {
					ps.setString(7, "Y");
				} else {
					ps.setString(7, null);
				}

				ps.setString(8, trip_type);
				ps.setString(9, groupNo);

				if (dto.getStartDate() != null) {
					String startDate = df.format(dto.getStartDate());
					ps.setString(10, startDate);
				} else {
					ps.setString(10, null);
				}

				if (dto.getEndDate() != null) {
					String endDate = df.format(dto.getEndDate());
					ps.setString(11, endDate);
				} else {
					ps.setString(11, null);
				}

				ps.setInt(12, daysDiff);
				ps.setString(13, user);
				ps.setTimestamp(14, timestamp);

				int insert = ps.executeUpdate();

				if (insert > 0) {
					isDataSave = true;
					dto.setRouteSeq(seqNo);
				} else {
					isDataSave = false;
				}

				con.commit();
			} catch (SQLException e) {
				con.rollback();
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return isDataSave;

	}

	@Override
	public boolean insertRouteScheduleDetOneTableData(List<RouteScheduleDTO> busNoList, String generatedRef, String user, String routeRefNo, boolean swap, int days) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		String tripType = null;
		boolean success;

		try {
			if (swap) {
				tripType = "D";
			} else {
				tripType = "O";
			}
			con = ConnectionManager.getConnection();
			
			String query = "INSERT INTO public.nt_t_route_schedule_generator_det01 "
			        + "(seq, route_ref_no, generator_ref_no, day_no, trip_id, bus_no, created_by, created_date, fixed_bus, trip_type) "
			        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			try {
			    ps = con.prepareStatement(query);
			    for (int i = 0; i < busNoList.size(); i++) {
			    	for(int count = 1; count <= days; count++) {
			    		long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_route_schedule_generator_det01");
				        ps.setLong(1, seqNo);
				        ps.setString(2, routeRefNo);
				        ps.setString(3, generatedRef);
				        ps.setInt(4, count);
				        ps.setString(5, busNoList.get(i).getTripId());
				        ps.setString(6, busNoList.get(i).getBusNoList().get(count - 1));
				        ps.setString(7, user);
				        ps.setTimestamp(8, timestamp);
				        ps.setString(9, "N");
				        ps.setString(10, tripType);
				        ps.addBatch();
			    	}
			    }
			    ps.executeBatch();
			    con.commit();
			    success = true;
			} catch (SQLException e) {
			    con.rollback();
			    e.printStackTrace();
			    return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(rs1);
			ConnectionManager.close(ps1);
			ConnectionManager.close(con);
		}

		return success;
	}

	@Override
	public boolean insertRouteScheduleDetOneTableDataTwoDay(List<RouteScheduleDTO> busNoList, String generatedRef, String user, String routeRefNo, boolean swap, int days) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		String tripType = null;

		try {
			if (swap) {
				tripType = "D";
			} else {
				tripType = "O";
			}

			con = ConnectionManager.getConnection();

			String query = "INSERT INTO public.nt_t_route_schedule_generator_det01_two_day "
			        + "(route_ref_no, generator_ref_no, day_no, trip_id, bus_no, created_by, created_date, fixed_bus, trip_type) "
			        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			try {
				ps1 = con.prepareStatement(query);

			    for (int i = 0; i < busNoList.size(); i++) {
			    	for(int count = 1; count <= days; count++) {
				        ps1.setString(1, routeRefNo);
				        ps1.setString(2, generatedRef);
				        ps1.setInt(3, count);
				        ps1.setString(4, busNoList.get(i).getTripId());
				        ps1.setString(5, busNoList.get(i).getBusNoList().get(count - 1));
				        ps1.setString(6, user);
				        ps1.setTimestamp(7, timestamp);
				        ps1.setString(8, "N");
				        ps1.setString(9, tripType);
				        ps1.addBatch();
			    	}
			    }

			    ps1.executeBatch();
			    con.commit();
			} catch (SQLException e) {
			    con.rollback();
			    e.printStackTrace();
			    return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(rs1);
			ConnectionManager.close(ps1);
			ConnectionManager.close(con);
		}
		
		return true;

	}
	
	
	
	@Override
	public boolean insertRouteScheduleDetTwoTableData(RouteScheduleDTO dto, List<String> leavePostionList,
			String generatedRef, String user, String routeRefNo, String tripType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			for (int i = 0; i < leavePostionList.size(); i++) {

				long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_route_schedule_generator_det02");

				String query = "INSERT INTO public.nt_t_route_schedule_generator_det02 "
						+ "(seq, route_ref_no, generator_ref_no, leave_position, created_by, created_date, trip_type) "
						+ "VALUES(?, ?, ?, ?, ?, ?, ?); ";

				ps = con.prepareStatement(query);

				try {
					ps.setLong(1, seqNo);
					ps.setString(2, routeRefNo);
					ps.setString(3, generatedRef);
					ps.setString(4, leavePostionList.get(i));
					ps.setString(5, user);
					ps.setTimestamp(6, timestamp);
					ps.setString(7, tripType);
					ps.addBatch();
					ps.executeBatch();
				} catch (SQLException e) {
					con.rollback();
					e.printStackTrace();
					return false;
				}	
			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return true;
	}
	
	@Override
	public boolean insertRouteScheduleDetTwoTableDataTwoDay(RouteScheduleDTO dto, List<String> leavePostionList,
			String generatedRef, String user, String routeRefNo, String tripType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			for (int i = 0; i < leavePostionList.size(); i++) {

				String query = "INSERT INTO public.nt_t_route_schedule_generator_det02_two_day "
						+ "(route_ref_no, generator_ref_no, leave_position, created_by, created_date, trip_type) "
						+ "VALUES(?, ?, ?, ?, ?, ?); ";

				ps = con.prepareStatement(query);

				try {
					ps.setString(1, routeRefNo);
					ps.setString(2, generatedRef);
					ps.setString(3, leavePostionList.get(i));
					ps.setString(4, user);
					ps.setTimestamp(5, timestamp);
					ps.setString(6, tripType);
					ps.addBatch();
					ps.executeBatch();
				} catch (SQLException e) {
					con.rollback();
					e.printStackTrace();
					return false;
				}	
			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return true;
	}

	@Override
	public String generateReferenceNo() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;

		int year = Year.now().getValue();
		String currYear = Integer.toString(year).substring(2, 4);
		String strAppNo = "";
		String lastyr = "00";

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT rs_route_ref_no " + " FROM public.nt_m_route_schedule_generator "
					+ " ORDER BY created_date desc " + " LIMIT 1";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("rs_route_ref_no");
			}

			if (result != null) {

				lastyr = result.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String ApprecordcountN = "00001";
					strAppNo = "RSN" + currYear + ApprecordcountN;
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
					strAppNo = "RSN" + currYear + ApprecordcountN;
				}
			} else
				strAppNo = "RSN" + currYear + "00001";

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return strAppNo;
	}

	@Override
	public RouteScheduleDTO getLastRouteScheduleData(String routeNo, String busCategory, String generatedRefNo,
			String group, String tripType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		RouteScheduleDTO dto = new RouteScheduleDTO();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select rs_start_date, rs_end_date FROM public.nt_m_route_schedule_generator "
					+ "WHERE rs_generator_ref_no=? and rs_route_no=? and rs_bus_category_code=? "
					+ "and rs_trip_type=? and rs_group_no=? ORDER BY created_date desc LIMIT 1 ";

			ps = con.prepareStatement(query2);
			ps.setString(1, generatedRefNo);
			ps.setString(2, routeNo);
			ps.setString(3, busCategory);
			ps.setString(4, tripType);
			ps.setString(5, group);
			rs = ps.executeQuery();

			while (rs.next()) {
				dto.setStartDateSTR(rs.getString("rs_start_date"));
				dto.setEndDateSTR(rs.getString("rs_end_date"));

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
	public boolean isRelatedDataFound(String routeNo, String busCategory, String generatedRefNo, String group,
			String tripType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isFound = false;

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select * FROM public.nt_m_route_schedule_generator "
					+ "WHERE rs_generator_ref_no=? and rs_route_no=? and rs_bus_category_code=? "
					+ "and rs_trip_type=? and rs_group_no=?; ";

			ps = con.prepareStatement(query2);
			ps.setString(1, generatedRefNo);
			ps.setString(2, routeNo);
			ps.setString(3, busCategory);
			ps.setString(4, tripType);
			ps.setString(5, group);
			rs = ps.executeQuery();

			if (rs.next()) {
				isFound = true;
			} else {
				isFound = false;
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return isFound;
	}

	@Override
	public int getNoOfDays(String routeNo, String busCategory, String generatedRefNo, String group, String tripType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int noOfDays = 0;

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select rs_no_of_dates FROM public.nt_m_route_schedule_generator "
					+ "WHERE rs_generator_ref_no=? and rs_route_no=? and rs_bus_category_code=? "
					+ "and rs_trip_type=? and rs_group_no=?; ";

			ps = con.prepareStatement(query2);
			ps.setString(1, generatedRefNo);
			ps.setString(2, routeNo);
			ps.setString(3, busCategory);
			ps.setString(4, tripType);
			ps.setString(5, group);
			rs = ps.executeQuery();

			while (rs.next()) {
				noOfDays = Integer.valueOf(rs.getString("rs_no_of_dates"));
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return noOfDays;
	}
	
	@Override
	public int getNoOfTrips(String refNo, String tripType, String table) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int noOfDays = 0;

		try {
			con = ConnectionManager.getConnection();
			String query2 = null;
			
			if(table.equals("m")) {
				query2 = "SELECT MAX(CAST(trip_id AS INTEGER)) AS max_trip_id "
						+ "FROM nt_t_route_schedule_generator_det01 "
						+ "WHERE generator_ref_no = ? "
						+ "  AND trip_type = ? "
						+ "  AND trip_id IS NOT NULL;";
			}else if(table.equals("t")) {
				query2 = "SELECT MAX(CAST(trip_id AS INTEGER)) AS max_trip_id "
						+ "FROM nt_t_route_schedule_generator_det01_two_day "
						+ "WHERE generator_ref_no = ? "
						+ "  AND trip_type = ? "
						+ "  AND trip_id IS NOT NULL;";
			}
			

			ps = con.prepareStatement(query2);
			ps.setString(1, refNo);
			ps.setString(2, tripType);

			rs = ps.executeQuery();

			while (rs.next()) {
				noOfDays = rs.getInt("max_trip_id");
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return noOfDays;
	}
	
	@Override
	public List<String> getNoOfTripsLeave(String refNo, String tripType, String table) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> noOfDays = new ArrayList<>();

		try {
			con = ConnectionManager.getConnection();
			String query2 = null;
			
			if(table.equals("m")) {
				query2 = "SELECT DISTINCT CAST(leave_trip_id AS INTEGER) AS order_by_column "
						+ "FROM nt_t_route_schedule_generator_det01  "
						+ "WHERE generator_ref_no = ? "
						+ "  AND trip_type = ?  "
						+ "  AND leave_trip_id IS NOT NULL "
						+ "ORDER BY order_by_column;";
			}else if(table.equals("t")) {
				query2 = "SELECT DISTINCT CAST(leave_trip_id AS INTEGER) AS order_by_column "
						+ "FROM nt_t_route_schedule_generator_det01_two_day  "
						+ "WHERE generator_ref_no = ? "
						+ "  AND trip_type = ?  "
						+ "  AND leave_trip_id IS NOT NULL "
						+ "ORDER BY order_by_column;";
				
			}
			

			ps = con.prepareStatement(query2);
			ps.setString(1, refNo);
			ps.setString(2, tripType);

			rs = ps.executeQuery();

			while (rs.next()) {
				noOfDays.add(rs.getString("order_by_column"));
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return noOfDays;
	}
	
	@Override
	public int getNoOfTripsForTwoDay(String refNo, String tripType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int noOfDays = 0;

		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT MAX(CAST(trip_id AS INTEGER)) AS max_trip_id "
					+ "FROM nt_t_route_schedule_generator_det01 "
					+ "WHERE generator_ref_no = ? "
					+ "  AND trip_type = ? "
					+ "  AND trip_id IS NOT NULL;";

			ps = con.prepareStatement(query2);
			ps.setString(1, refNo);
			ps.setString(2, tripType);

			rs = ps.executeQuery();

			while (rs.next()) {
				noOfDays = rs.getInt("max_trip_id");
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return noOfDays;
	}


	@Override
	public List<String> selectEditDate(String routeNo, String busCategory, String generatedRefNo, String group,
			String tripType) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String swap = null;

		List<String> busNoList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			if (tripType != null && !tripType.isEmpty() && !tripType.trim().equals("")) {
				if (tripType.equalsIgnoreCase("O")) {
					swap = "N";
				}
				if (tripType.equalsIgnoreCase("D")) {
					swap = "Y";
				}
			}

			String sql = "SELECT a.bus_no,a.seq as seq   FROM public.nt_t_route_schedule_generator_det01 a "
					+ "LEFT JOIN public.nt_m_route_schedule_generator b  on a.generator_ref_no=b.rs_generator_ref_no "
					+ "where a.generator_ref_no=? and b.rs_route_no=? and b.rs_bus_category_code=? and b.rs_is_swap=? and  b.rs_trip_type=? and b.rs_group_no=? and a.trip_type=?  "
					+ "order by a.seq ASC";

			ps = con.prepareStatement(sql);
			ps.setString(1, generatedRefNo);
			ps.setString(2, routeNo);
			ps.setString(3, busCategory);
			ps.setString(4, swap);
			ps.setString(5, tripType);
			ps.setString(6, group);
			ps.setString(7, tripType);

			rs = ps.executeQuery();

			while (rs.next()) {

				String assignedBus = getAssignedBusNumForSelectedBus(con, routeNo, busCategory, generatedRefNo, group,
						tripType, rs.getString("bus_no"));
				if (assignedBus != null && !assignedBus.isEmpty() && !assignedBus.trim().equals("")) {
					busNoList.add(assignedBus + "-" + rs.getLong("seq"));
				} else {
					busNoList.add(rs.getString("bus_no") + "-" + rs.getLong("seq"));
				}

			}

		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return busNoList;
	}
	
	@Override
	public List<String> getBusNoForEditLeave(String generatedRefNo, String tripId, String tripType, String table) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		List<String> busNoList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();
			String sql = null;

			if(table.equals("m")) {
				sql = "SELECT leave_bus , leave_trip_day  "
						+ "FROM nt_t_route_schedule_generator_det01 "
						+ "WHERE generator_ref_no = ? "
						+ "  AND trip_type = ? "
						+ "  AND leave_trip_id = ? "
						+ "ORDER BY CAST(leave_trip_day AS INTEGER);";
			}else if(table.equals("t")) {
				sql = "SELECT leave_bus , leave_trip_day  "
						+ "FROM nt_t_route_schedule_generator_det01_two_day "
						+ "WHERE generator_ref_no = ? "
						+ "  AND trip_type = ? "
						+ "  AND leave_trip_id = ? "
						+ "ORDER BY CAST(leave_trip_day AS INTEGER);";
			}
			

			ps = con.prepareStatement(sql);
			ps.setString(1, generatedRefNo);
			ps.setString(2, tripType);
			ps.setString(3, tripId);

			rs = ps.executeQuery();

			while (rs.next()) {
				busNoList.add(rs.getString("leave_bus"));
			}

		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return busNoList;
	}
	
	@Override
	public List<String> getBusNoForEdit(String generatedRefNo, String tripId, String tripType, String table) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		List<String> busNoList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();
			String sql = null;

			if(table.equals("m")) {
				sql = "select bus_no,day_no "
						+ "from nt_t_route_schedule_generator_det01 where generator_ref_no = ? and trip_type = ? "
						+ "and trip_id = ? order by day_no ";
			}else if(table.equals("t")) {
				sql = "select bus_no,day_no "
						+ "from nt_t_route_schedule_generator_det01_two_day where generator_ref_no = ? and trip_type = ? "
						+ "and trip_id = ? order by day_no ";
			}
			

			ps = con.prepareStatement(sql);
			ps.setString(1, generatedRefNo);
			ps.setString(2, tripType);
			ps.setString(3, tripId);

			rs = ps.executeQuery();

			while (rs.next()) {
				busNoList.add(rs.getString("bus_no"));
			}

		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return busNoList;
	}

	@Override
	public List<String> getBusNoForEditTwoDay(String generatedRefNo, String tripId, String tripType) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		List<String> busNoList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();


			String sql = "select bus_no,day_no "
					+ "from nt_t_route_schedule_generator_det01_two_day where generator_ref_no = ? and trip_type = ? "
					+ "and trip_id = ? order by day_no ";

			ps = con.prepareStatement(sql);
			ps.setString(1, generatedRefNo);
			ps.setString(2, tripType);
			ps.setString(3, tripId);

			rs = ps.executeQuery();

			while (rs.next()) {
				busNoList.add(rs.getString("bus_no"));
			}

		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return busNoList;
	}

	@Override
	public void updateEditedBusNumbersInRoute_schedule_generator_det01(String originalBusNum, String editedBusNum,
			String seqNum, String userName, String tripType) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {

			String sql = "UPDATE public.nt_t_route_schedule_generator_det01 SET  bus_no=?, modified_by=?, modified_date=? WHERE seq=? and bus_no=? and trip_type=?";
			con = ConnectionManager.getConnection();
			stmt = con.prepareStatement(sql);
			try {
				stmt.setString(1, editedBusNum);
				stmt.setString(2, userName);
				stmt.setTimestamp(3, new Timestamp(new Date().getTime()));
				stmt.setLong(4, Long.parseLong(seqNum));
				stmt.setString(5, originalBusNum);
				stmt.setString(6, tripType);
				stmt.executeUpdate();
				con.commit();
			} catch (SQLException e) {
				con.rollback();
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
	}
	
	@Override
	public void updateEditedBusNumbersInRoute_schedule_generator_det01ForEdit(String refNo, String tripType,
			String userName, List<RouteScheduleDTO> routeList,int loop,String table) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String sql = null;
			if(table.equals("m")) {
				sql = "UPDATE public.nt_t_route_schedule_generator_det01 SET  bus_no=?, modified_by=?, modified_date=? WHERE generator_ref_no=? and trip_id=? and trip_type=? and day_no=?";
			}else if(table.equals("t")) {
				sql = "UPDATE public.nt_t_route_schedule_generator_det01_two_day SET  bus_no=?, modified_by=?, modified_date=? WHERE generator_ref_no=? and trip_id=? and trip_type=? and day_no=?";
			}
			
			con = ConnectionManager.getConnection();
			stmt = con.prepareStatement(sql);
			try {
				for(int i = 0; i < routeList.size(); i++) {
					for(int count = 1; count <= loop; count++) {
						stmt.setString(1, routeList.get(i).getBusNoList().get(count - 1));
						stmt.setString(2, userName);
						stmt.setTimestamp(3, new Timestamp(new Date().getTime()));
						stmt.setString(4, refNo);
						stmt.setString(5, routeList.get(i).getTripId());
						stmt.setString(6, tripType);
						stmt.setInt(7, count);
						stmt.addBatch();
					}
				}
				stmt.executeBatch();
				con.commit();
			} catch (SQLException e) {
				con.rollback();
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
	}
	
	@Override
	public void updateEditedBusNumbersInRoute_schedule_generator_det01ForEditLeaves(String refNo, String tripType,
			String userName, List<RouteScheduleDTO> routeList,String table, int days) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String sql = null;
			
			if(table.equals("m")) {
				sql = "UPDATE public.nt_t_route_schedule_generator_det01 SET  leave_bus=?, modified_by=?, modified_date=? WHERE generator_ref_no=? and leave_trip_id=? and trip_type=? and leave_trip_day=?";
			}else if(table.equals("t")) {
				sql = "UPDATE public.nt_t_route_schedule_generator_det01_two_day SET  leave_bus=?, modified_by=?, modified_date=? WHERE generator_ref_no=? and leave_trip_id=? and trip_type=? and leave_trip_day=?";
			}
			con = ConnectionManager.getConnection();
			stmt = con.prepareStatement(sql);
			try {
				for(int i = 0; i < routeList.size(); i++) {
					for(int count = 1; count <= days; count++) {
						stmt.setString(1, routeList.get(i).getLeaveBusNoList().get(count - 1));
						stmt.setString(2, userName);
						stmt.setTimestamp(3, new Timestamp(new Date().getTime()));
						stmt.setString(4, refNo);
						stmt.setString(5, routeList.get(i).getTripId());
						stmt.setString(6, tripType);
						stmt.setString(7, String.valueOf(count));
						stmt.addBatch();
					}
				}
				stmt.executeBatch();
				con.commit();
			} catch (SQLException e) {
				con.rollback();
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
	}

	@Override
	public List<RouteScheduleDTO> getBusNoListWithoutCTBBus(String routeNo, String refNo, String groupNo,
			String tripType, String busCategory) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<RouteScheduleDTO> busNoList = new ArrayList<RouteScheduleDTO>();
		int privateBusOrigin = 0;
		int privateBusDestination = 0;
		String originAbbr = null;
		String destAbbr = null;
		int tempCount = 0;

		String busNumLike = null;

		try {
			con = ConnectionManager.getConnection();

			// get abbreviation for origin and destination start
			String abbrQuery = "SELECT rc_abbriviation_ltr_start, rc_abbriviation_ltr_end FROM public.nt_t_route_creator "
					+ "where rc_route_no=? and rc_bus_type=?";

			ps = con.prepareStatement(abbrQuery);
			ps.setString(1, routeNo);
			ps.setString(2, busCategory);
			rs = ps.executeQuery();

			while (rs.next()) {
				originAbbr = rs.getString("rc_abbriviation_ltr_start");
				destAbbr = rs.getString("rc_abbriviation_ltr_end");
			}

			try {
				if (rs != null)
					rs.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				if (ps != null)
					ps.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			// get abbreviation for origin and destination end

			// get private buses for origin start
			if (tripType != null && !tripType.isEmpty() && !tripType.trim().equals("")
					&& tripType.equalsIgnoreCase("O")) {
				String originPVTQuery = "select a.no_of_pvt_bus from public.nt_t_trips_generator_det02 a "
						+ "left join public.nt_m_trips_generator b " + "on a.trips_ref_code=b.tg_trips_ref_code "
						+ "where b.tg_generator_ref_no=? " + "and a.group_no=? and a.trip_type=?";

				ps = con.prepareStatement(originPVTQuery);
				ps.setString(1, refNo);
				ps.setString(2, groupNo);
				ps.setString(3, "O");
				rs = ps.executeQuery();

				while (rs.next()) {
					privateBusOrigin = rs.getInt("no_of_pvt_bus");
				}

				try {
					if (rs != null)
						rs.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				try {
					if (ps != null)
						ps.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			// get private buses for origin end

			// get private buses for destination start
			if (tripType != null && !tripType.isEmpty() && !tripType.trim().equals("")
					&& tripType.equalsIgnoreCase("D")) {
				String destinationPVTQuery = "select a.no_of_pvt_bus from public.nt_t_trips_generator_det02 a "
						+ "left join public.nt_m_trips_generator b " + "on a.trips_ref_code=b.tg_trips_ref_code "
						+ "where b.tg_generator_ref_no=? " + "and a.group_no=? and a.trip_type=?";

				ps = con.prepareStatement(destinationPVTQuery);
				ps.setString(1, refNo);
				ps.setString(2, groupNo);
				ps.setString(3, "D");
				rs = ps.executeQuery();

				while (rs.next()) {
					privateBusDestination = rs.getInt("no_of_pvt_bus");
				}

				try {
					if (rs != null)
						rs.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				try {
					if (ps != null)
						ps.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			// get private buses for destination end
			
//			added by thilna.d to remove fixed bus numbers on 12-10-2021
			// get fixed bus numbers start
			List<String> fixedBusNoList = new ArrayList<String>();
			String fixedBusNoQuery = "SELECT distinct bus_num FROM public.nt_m_timetable_generator_det\r\n"
					+ "WHERE generator_ref_no=? and group_no=? and is_fixed_time=?;";

			ps = con.prepareStatement(fixedBusNoQuery);
			ps.setString(1, refNo);
			ps.setString(2, groupNo);
			ps.setString(3, "Y");
			rs = ps.executeQuery();

			while (rs.next()) {
				fixedBusNoList.add(rs.getString("bus_num"));
			}
			
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			// get fixed bus numbers end

			/** create temp origin and destination bus numbers with leave buses start **/
			// origin buses
			List<String> originBusList = new ArrayList<String>();
			for (int i = 0; i < privateBusOrigin; i++) {
				int count = i + 1;
//				changed by thilna.d to remove fixed bus numbers on 12-10-2021
				if(!fixedBusNoList.contains(originAbbr + count)) {
					originBusList.add(originAbbr + count);
				}
			}

			// destination buses
			List<String> destBusList = new ArrayList<String>();
			for (int i = 0; i < privateBusDestination; i++) {
				int count = i + 1;
//				changed by thilna.d to remove fixed bus numbers on 12-10-2021
				if(!fixedBusNoList.contains(destAbbr + count)) {
					destBusList.add(destAbbr + count);
				}
			}
			/** create temp origin and destination bus numbers with leave buses end **/

			// add both lists together start
			List<String> tempAllBusNums = new ArrayList<String>();

			if (originBusList != null && originBusList.size() != 0) {
				tempAllBusNums.addAll(originBusList);
			}
			if (destBusList != null && destBusList.size() != 0) {
				tempAllBusNums.addAll(destBusList);
			}
			// add both lists together end

			if (tripType.equalsIgnoreCase("O")) {
				busNumLike = originAbbr + '%';

			} else {
				busNumLike = destAbbr + '%';
			}
			String query2 = "SELECT distinct bus_num, generator_ref_no, group_no, trip_type,is_pvt_bus "
					+ "FROM public.nt_m_timetable_generator_det "
					+ "where generator_ref_no=? and group_no=? and trip_type=? and is_fixed_time='N' "
					+ "and bus_num is not null and bus_num !='' and  bus_num like " + "'" + busNumLike + "' "
					+ " and bus_num not in('SLTB-O','SLTB-D','ETC-O','ETC-D') order by bus_num ";

			ps = con.prepareStatement(query2);
			ps.setString(1, refNo);
			ps.setString(2, groupNo);
			ps.setString(3, tripType);
			rs = ps.executeQuery();
			RouteScheduleDTO e;

			for (int i = 0; rs.next(); i++) {

				e = new RouteScheduleDTO();
				String id = String.valueOf(i + 1);
				tempCount = i + 1;
				e.setTripId(id);

				e.setBusNo(rs.getString("bus_num"));

				busNoList.add(e);

			}

			/** add missing busses to main bus list start **/
			List<String> missingBusses = new ArrayList<String>();
			boolean found = false;
			for (String s : tempAllBusNums) {
				found = false;
				for (RouteScheduleDTO dto : busNoList) {
					if (s.equals(dto.getBusNo())) {
						found = true;
						break;
					}
				}
				if (!found) {
					missingBusses.add(s);
				}
			}

			for (String s : missingBusses) {
				e = new RouteScheduleDTO();
				tempCount = tempCount + 1;
				String id = String.valueOf(tempCount);
				e.setTripId(id);
				e.setBusNo(s);
				busNoList.add(e);
			}

			final String oriAbb = originAbbr;
			final String desAbb = destAbbr;
			Collections.sort(busNoList, new Comparator<RouteScheduleDTO>() {
				@Override
				public int compare(RouteScheduleDTO u1, RouteScheduleDTO u2) {
					return extractInt(u2.getBusNo()) - extractInt(u1.getBusNo());
				}

				int extractInt(String s) {
					String num = "";
					if (s.contains(oriAbb)) {
						num = s.replaceAll(oriAbb, "");
					}
					if (s.contains(desAbb)) {
						num = s.replaceAll(desAbb, "");
					}

					return num.isEmpty() || !num.matches("[0-9]+") ? 0 : Integer.parseInt(num);
				}
			});

			Collections.reverse(busNoList);

			/*
			 * for(RouteScheduleDTO s :busNoList) { asddsdsd.out.println(s.getBusNo()); }
			 */

			Collections.sort(busNoList, new Comparator<RouteScheduleDTO>() {
				@Override
				public int compare(RouteScheduleDTO u1, RouteScheduleDTO u2) {
					String num1 = "";
					String num2 = "";

					if (u2.getBusNo().contains(oriAbb)) {
						num1 = oriAbb;
					}

					if (u2.getBusNo().contains(desAbb)) {
						num1 = desAbb;
					}
					if (u1.getBusNo().contains(oriAbb)) {
						num2 = oriAbb;
					}

					if (u1.getBusNo().contains(desAbb)) {
						num2 = desAbb;
					}

					return num1.compareTo(num2);

				}
			});

			/** add missing busses to main bus list end **/

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return busNoList;
	}

	@Override
	public List<RouteScheduleDTO> getBusNoListSecondTime(String routeNo, String refNo, String groupNo, String tripType,
			String busCategory) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<RouteScheduleDTO> busNoList = new ArrayList<RouteScheduleDTO>();
		int privateBusOrigin = 0;
		int privateBusDestination = 0;
		String originAbbr = null;
		String destAbbr = null;
		int tempCount = 0;
		try {
			con = ConnectionManager.getConnection();

			// get abbreviation for origin and destination start
			String abbrQuery = "SELECT rc_abbriviation_ltr_start, rc_abbriviation_ltr_end FROM public.nt_t_route_creator "
					+ "where rc_route_no=? and rc_bus_type=?";

			ps = con.prepareStatement(abbrQuery);
			ps.setString(1, routeNo);
			ps.setString(2, busCategory);
			rs = ps.executeQuery();

			while (rs.next()) {
				originAbbr = rs.getString("rc_abbriviation_ltr_start");
				destAbbr = rs.getString("rc_abbriviation_ltr_end");
			}

			try {
				if (rs != null)
					rs.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				if (ps != null)
					ps.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			// get abbreviation for origin and destination end

			// get private buses for origin start
			if (tripType != null && !tripType.isEmpty() && !tripType.trim().equals("")
					&& tripType.equalsIgnoreCase("O")) {
				String originPVTQuery = "select a.no_of_pvt_bus from public.nt_t_trips_generator_det02 a "
						+ "left join public.nt_m_trips_generator b " + "on a.trips_ref_code=b.tg_trips_ref_code "
						+ "where b.tg_generator_ref_no=? " + "and a.group_no=? and a.trip_type=?";

				ps = con.prepareStatement(originPVTQuery);
				ps.setString(1, refNo);
				ps.setString(2, groupNo);
				ps.setString(3, "O");
				rs = ps.executeQuery();

				while (rs.next()) {
					privateBusOrigin = rs.getInt("no_of_pvt_bus");
				}

				try {
					if (rs != null)
						rs.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				try {
					if (ps != null)
						ps.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			// get private buses for origin end

			// get private buses for destination start
			if (tripType != null && !tripType.isEmpty() && !tripType.trim().equals("")
					&& tripType.equalsIgnoreCase("D")) {
				String destinationPVTQuery = "select a.no_of_pvt_bus from public.nt_t_trips_generator_det02 a "
						+ "left join public.nt_m_trips_generator b " + "on a.trips_ref_code=b.tg_trips_ref_code "
						+ "where b.tg_generator_ref_no=? " + "and a.group_no=? and a.trip_type=?";

				ps = con.prepareStatement(destinationPVTQuery);
				ps.setString(1, refNo);
				ps.setString(2, groupNo);
				ps.setString(3, "D");
				rs = ps.executeQuery();

				while (rs.next()) {
					privateBusDestination = rs.getInt("no_of_pvt_bus");
				}

				try {
					if (rs != null)
						rs.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				try {
					if (ps != null)
						ps.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			// get private buses for destination end

			/** create temp origin and destination bus numbers with leave buses start **/
			// origin buses
			List<String> originBusList = new ArrayList<String>();
			for (int i = 0; i < privateBusOrigin; i++) {
				int count = i + 1;
				originBusList.add(originAbbr + count);
			}

			// destination buses
			List<String> destBusList = new ArrayList<String>();
			for (int i = 0; i < privateBusDestination; i++) {
				int count = i + 1;
				destBusList.add(destAbbr + count);
			}
			/** create temp origin and destination bus numbers with leave buses end **/

			// add both lists together start
			List<String> tempAllBusNums = new ArrayList<String>();

			if (originBusList != null && originBusList.size() != 0) {
				tempAllBusNums.addAll(originBusList);
			}
			if (destBusList != null && destBusList.size() != 0) {
				tempAllBusNums.addAll(destBusList);
			}
			// add both lists together end

			String query2 = "SELECT distinct bus_num, generator_ref_no, group_no, trip_type,is_pvt_bus,assigned_bus_no  "
					+ "FROM public.nt_m_timetable_generator_det "
					+ "where generator_ref_no=? and group_no=? and trip_type=? and is_fixed_time='N' "
					+ "and bus_num is not null and bus_num !='' "
					+ "and bus_num not in('SLTB-O','SLTB-D','ETC-O','ETC-D') order by bus_num ";

			ps = con.prepareStatement(query2);
			ps.setString(1, refNo);
			ps.setString(2, groupNo);
			ps.setString(3, tripType);
			rs = ps.executeQuery();
			RouteScheduleDTO e;

			for (int i = 0; rs.next(); i++) {

				e = new RouteScheduleDTO();
				String id = String.valueOf(i + 1);
				tempCount = i + 1;
				e.setTripId(id);

				/*
				 * if (rs.getString("assigned_bus_no") != null &&
				 * !rs.getString("assigned_bus_no").isEmpty() &&
				 * !rs.getString("assigned_bus_no").trim().equals("")) {
				 * e.setBusNo(rs.getString("assigned_bus_no")); } else {
				 */
				e.setBusNo(rs.getString("bus_num"));
				// }
				busNoList.add(e);

			}

			/** add missing busses to main bus list start **/
			List<String> missingBusses = new ArrayList<String>();
			boolean found = false;
			for (String s : tempAllBusNums) {
				found = false;
				for (RouteScheduleDTO dto : busNoList) {
					if (s.equals(dto.getBusNo())) {
						found = true;
						break;
					}
				}
				if (!found) {
					missingBusses.add(s);
				}
			}

			for (String s : missingBusses) {
				e = new RouteScheduleDTO();
				tempCount = tempCount + 1;
				String id = String.valueOf(tempCount);
				e.setTripId(id);
				e.setBusNo(s);
				busNoList.add(e);
			}

			final String oriAbb = originAbbr;
			final String desAbb = destAbbr;
			Collections.sort(busNoList, new Comparator<RouteScheduleDTO>() {
				@Override
				public int compare(RouteScheduleDTO u1, RouteScheduleDTO u2) {
					return extractInt(u2.getBusNo()) - extractInt(u1.getBusNo());
				}

				int extractInt(String s) {
					String num = "";
					if (s.contains(oriAbb)) {
						num = s.replaceAll(oriAbb, "");
					}
					if (s.contains(desAbb)) {
						num = s.replaceAll(desAbb, "");
					}

					return num.isEmpty() || !num.matches("[0-9]+") ? 0 : Integer.parseInt(num);
				}
			});

			Collections.reverse(busNoList);

			Collections.sort(busNoList, new Comparator<RouteScheduleDTO>() {
				@Override
				public int compare(RouteScheduleDTO u1, RouteScheduleDTO u2) {
					String num1 = "";
					String num2 = "";

					if (u2.getBusNo().contains(oriAbb)) {
						num1 = oriAbb;
					}

					if (u2.getBusNo().contains(desAbb)) {
						num1 = desAbb;
					}
					if (u1.getBusNo().contains(oriAbb)) {
						num2 = oriAbb;
					}

					if (u1.getBusNo().contains(desAbb)) {
						num2 = desAbb;
					}

					return num1.compareTo(num2);

				}
			});

			/** add missing busses to main bus list end **/

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return busNoList;
	}

	@Override
	public boolean checkBusAssignerDone(String busCategory, String generatedRefNo, String groupNo, String tripType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isFound = false;

		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT * FROM public.nt_m_timetable_generator_det "
					+ "WHERE generator_ref_no=? and group_no=? and trip_type=? and assigned_bus_no is not null";

			ps = con.prepareStatement(query2);
			ps.setString(1, generatedRefNo);
			ps.setString(2, groupNo);
			ps.setString(3, tripType);
			rs = ps.executeQuery();

			if (rs.next()) {
				isFound = true;
			} else {
				isFound = false;
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return isFound;
	}

	@Override
	public List<String> selectEditDateForAssignedBuses(String routeNo, String busCategory, String generatedRefNo,
			String group, String tripType) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		ResultSet rs1 = null;
		PreparedStatement ps1 = null;
		String swap = null;
		String originAbbr = null;
		String destAbbr = null;
		String busNumLike = null;

		List<String> busNoList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			if (tripType != null && !tripType.isEmpty() && !tripType.trim().equals("")) {
				if (tripType.equalsIgnoreCase("O")) {
					swap = "N";
				}
				if (tripType.equalsIgnoreCase("D")) {
					swap = "Y";
				}
			}

			// get abbreviation for origin and destination start
			String abbrQuery = "SELECT rc_abbriviation_ltr_start, rc_abbriviation_ltr_end FROM public.nt_t_route_creator "
					+ "where rc_route_no=? and rc_bus_type=?";

			ps1 = con.prepareStatement(abbrQuery);
			ps1.setString(1, routeNo);
			ps1.setString(2, busCategory);
			rs1 = ps1.executeQuery();

			while (rs1.next()) {
				originAbbr = rs1.getString("rc_abbriviation_ltr_start");
				destAbbr = rs1.getString("rc_abbriviation_ltr_end");
			}

			try {
				if (rs1 != null)
					rs1.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				if (ps1 != null)
					ps1.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			// get abbreviation for origin and destination end

			// get private buses for origin start

			if (tripType.equalsIgnoreCase("O")) {
				busNumLike = originAbbr + '%';

			} else {
				busNumLike = destAbbr + '%';
			}
			String sql = "SELECT a.bus_no,a.seq as seq   FROM public.nt_t_route_schedule_generator_det01 a "
					+ "LEFT JOIN public.nt_m_route_schedule_generator b  on a.generator_ref_no=b.rs_generator_ref_no "
					+ "where a.generator_ref_no=? and b.rs_route_no=? and b.rs_bus_category_code=? and b.rs_is_swap=? and  b.rs_trip_type=? and b.rs_group_no=? and a.trip_type=? "
					+ "and  bus_no like " + "'" + busNumLike + "' " + "order by a.seq ASC";

			ps = con.prepareStatement(sql);
			ps.setString(1, generatedRefNo);
			ps.setString(2, routeNo);
			ps.setString(3, busCategory);
			ps.setString(4, swap);
			ps.setString(5, tripType);
			ps.setString(6, group);
			ps.setString(7, tripType);

			rs = ps.executeQuery();

			while (rs.next()) {

				String assignedNum = getAssignedBusNumForSelectedBus(con, routeNo, busCategory, generatedRefNo, group,
						tripType, rs.getString("bus_no"));
				if (assignedNum != null && !assignedNum.isEmpty() && !assignedNum.trim().equals("")) {
					busNoList.add(assignedNum + "-" + rs.getLong("seq"));
				} else {
					busNoList.add(rs.getString("bus_no") + "-" + rs.getLong("seq"));
				}

			}

		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return busNoList;
	}

	public String getAssignedBusNumForSelectedBus(Connection con, String routeNo, String busCategory,
			String generatedRefNo, String group, String tripType, String busNo) {

		ResultSet rs = null;
		PreparedStatement ps = null;
		String assingedBus = null;

		ResultSet rs1 = null;
		PreparedStatement ps1 = null;

		String originAbbr = null;
		String destAbbr = null;
		String busNumLike = null;

		try {

			// get abbreviation for origin and destination start
			String abbrQuery = "SELECT rc_abbriviation_ltr_start, rc_abbriviation_ltr_end FROM public.nt_t_route_creator "
					+ "where rc_route_no=? and rc_bus_type=?";

			ps1 = con.prepareStatement(abbrQuery);
			ps1.setString(1, routeNo);
			ps1.setString(2, busCategory);
			rs1 = ps1.executeQuery();

			while (rs1.next()) {
				originAbbr = rs1.getString("rc_abbriviation_ltr_start");
				destAbbr = rs1.getString("rc_abbriviation_ltr_end");
			}

			try {
				if (rs1 != null)
					rs1.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				if (ps1 != null)
					ps1.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			// get abbreviation for origin and destination end

			// get private buses for origin start

			if (tripType.equalsIgnoreCase("O")) {
				busNumLike = originAbbr + '%';

			} else {
				busNumLike = destAbbr + '%';
			}

			String sql = "SELECT distinct bus_num, assigned_bus_no, generator_ref_no, group_no, trip_type, seq_master "
					+ "FROM public.nt_m_timetable_generator_det where generator_ref_no=? "
					+ "and group_no=? and trip_type=? and bus_num=? and bus_num is not null and bus_num !=''"
					+ "and  bus_num like " + "'" + busNumLike + "'" + " order by bus_num ";

			ps = con.prepareStatement(sql);
			ps.setString(1, generatedRefNo);
			ps.setString(2, group);
			ps.setString(3, tripType);
			ps.setString(4, busNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				assingedBus = rs.getString("assigned_bus_no");
			}

		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
		}

		return assingedBus;
	}

	@Override
	public List<RouteScheduleDTO> retrieveInsertedDataForEdit(String generatedRefNo, String triptype) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<RouteScheduleDTO> tempList = new ArrayList<RouteScheduleDTO>();
		int tripCount = 0;
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT count(distinct day_no) as tripcount FROM public.nt_t_route_schedule_generator_det01 WHERE  generator_ref_no = '"
					+ generatedRefNo + "' " + " and trip_type='" + triptype + "'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				tripCount = rs.getInt("tripcount");
			}

			ConnectionManager.close(ps);
			ConnectionManager.close(rs);

			String query2 = "SELECT bus_no,seq,trip_id FROM public.nt_t_route_schedule_generator_det01 WHERE  generator_ref_no = '"
					+ generatedRefNo + "' " + " and trip_type='" + triptype + "' ORDER BY seq";

			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();

			while (rs.next()) {
				RouteScheduleDTO dto = new RouteScheduleDTO();
				dto.setBusNo(rs.getString("bus_no"));
				dto.setRouteSeq(rs.getInt("seq"));
				dto.setTripId(rs.getString("trip_id"));
				dto.setTripCount(tripCount);
				tempList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return tempList;
	}

	@Override
	public List<RouteScheduleDTO> retrieveInsertedDataForEditTimeTable(String generatedRefNo, String busCategory,
			String groupNo, String tripType, String route) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<RouteScheduleDTO> tempList = new ArrayList<RouteScheduleDTO>();
		int tripCount = 0;
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT count(distinct day_no) as tripcount FROM public.nt_t_route_schedule_generator_det01 WHERE  generator_ref_no = '"
					+ generatedRefNo + "' " + "and trip_type='" + tripType + "'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				tripCount = rs.getInt("tripcount");
			}

			ConnectionManager.close(ps);
			ConnectionManager.close(rs);

			String query2 = "SELECT bus_no,seq,trip_id FROM public.nt_t_route_schedule_generator_det01 WHERE  generator_ref_no = '"
					+ generatedRefNo + "' " + " and trip_type='" + tripType + "' ORDER BY seq";

			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();

			while (rs.next()) {
				RouteScheduleDTO dto = new RouteScheduleDTO();
				String assignedBus = getAssignedBusNumForSelectedBus(con, route, busCategory, generatedRefNo, groupNo,
						tripType, rs.getString("bus_no"));
				if (assignedBus != null && !assignedBus.isEmpty() && !assignedBus.trim().equals("")) {
					dto.setBusNo(assignedBus);
				} else {
					dto.setBusNo(rs.getString("bus_no"));
				}
				dto.setRouteSeq(rs.getInt("seq"));
				dto.setTripId(rs.getString("trip_id"));
				dto.setTripCount(tripCount);
				tempList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return tempList;
	}

	@Override
	public void insertRouteGeneratorDetDataForHistory(String generatedRefNo, String routeRefNum, String originalBusNum,
			String editedBusNum, String refSeqNum, String loginUser, String tripId) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_h_route_schedule_generator_det01");

			String query = "INSERT INTO public.nt_h_route_schedule_generator_det01 "
					+ "(seq, route_ref_no, generator_ref_no, trip_id, bus_no, modified_by, modified_date, assigned_bus_no, ref_seq_num) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?) ";
			con = ConnectionManager.getConnection();
			ps = con.prepareStatement(query);

			ps.setLong(1, seqNo);
			if (routeRefNum != null && !routeRefNum.isEmpty() && !routeRefNum.trim().equals("")) {
				ps.setString(2, routeRefNum);
			} else {
				ps.setNull(2, java.sql.Types.VARCHAR);
			}
			ps.setString(3, generatedRefNo);
			ps.setString(4, tripId);
			ps.setString(5, originalBusNum);
			ps.setString(6, loginUser);
			ps.setTimestamp(7, timestamp);
			ps.setString(8, editedBusNum);
			ps.setLong(9, Integer.valueOf(refSeqNum));

			ps.executeUpdate();
			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

	}

	@Override
	public String retrieveLastBusNumOfRouteSchedule(String routeNo, String generatedRefNo, String groupNo,
			String tripType, String busCategory) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String lastBusNum = null;

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select a.bus_no from public.nt_t_route_schedule_generator_det01 a, public.nt_m_route_schedule_generator b "
					+ "where  b.rs_route_no=? and b.rs_group_no=? and b.rs_trip_type=? and b.rs_bus_category_code=? and a.generator_ref_no =? and a.trip_id ='1' and a.trip_type=? "
					+ "and b.rs_generator_ref_no=a.generator_ref_no order by a.seq DESC limit 1";

			ps = con.prepareStatement(query2);
			ps.setString(1, routeNo);
			ps.setString(2, groupNo);
			ps.setString(3, tripType);
			ps.setString(4, busCategory);
			ps.setString(5, generatedRefNo);
			ps.setString(6, tripType);
			rs = ps.executeQuery();

			while (rs.next()) {
				lastBusNum = rs.getString("bus_no");
			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return lastBusNum;
	}

	@Override
	public String retrieveReferenceNo(String routeNo, String busCategory, String generatedRefNo, String group,
			String tripType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String refNum = null;

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select * FROM public.nt_m_route_schedule_generator "
					+ "WHERE rs_generator_ref_no=? and rs_route_no=? and rs_bus_category_code=? "
					+ "and rs_trip_type=? and rs_group_no=?; ";

			ps = con.prepareStatement(query2);
			ps.setString(1, generatedRefNo);
			ps.setString(2, routeNo);
			ps.setString(3, busCategory);
			ps.setString(4, tripType);
			ps.setString(5, group);
			rs = ps.executeQuery();

			while (rs.next()) {
				refNum = rs.getString("rs_route_ref_no");
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return refNum;
	}

	@Override
	public void updateRouteScheduleGeneratorDates(Date lastPanelEndDate, String routeNo, String busCategory,
			String generatedRefNo, String group, String tripType, int dateDifference, Date lastPanelStartDateNew) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String startDate = null;
		String endDate = null;
		String routeRef = null;
		String rotationType = null;
		String swap = null;
		String numOfDays = null;
		int masSeq;
		try {
			con = ConnectionManager.getConnection();

			/** insert date former dates in to history table start **/
			String selectQuery = "SELECT * from public.nt_m_route_schedule_generator WHERE rs_generator_ref_no=? and rs_route_no=? and rs_bus_category_code=? and rs_trip_type=? and rs_group_no=? ";
			stmt = con.prepareStatement(selectQuery);

			stmt.setString(1, generatedRefNo);
			stmt.setString(2, routeNo);
			stmt.setString(3, busCategory);
			stmt.setString(4, tripType);
			stmt.setString(5, group);

			rs = stmt.executeQuery();

			while (rs.next()) {
				startDate = rs.getString("rs_start_date");
				endDate = rs.getString("rs_end_date");
				routeRef = rs.getString("rs_route_ref_no");
				rotationType = rs.getString("rs_rotation_type");
				swap = rs.getString("rs_is_swap");
				numOfDays = rs.getString("rs_no_of_dates");
				masSeq = rs.getInt("rs_seq");
			}
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_h_route_schedule_generator");

			String insert = "INSERT INTO public.nt_h_route_schedule_generator "
					+ "(rs_seq, rs_route_ref_no, rs_generator_ref_no, rs_rotation_type, rs_route_no, rs_bus_category_code, rs_is_swap, "
					+ "rs_trip_type, rs_group_no, rs_start_date, rs_end_date, rs_no_of_dates, created_date) "
					+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";

			stmt = con.prepareStatement(insert);

			stmt.setLong(1, seqNo);
			stmt.setString(2, routeRef);
			stmt.setString(3, generatedRefNo);
			stmt.setString(4, rotationType);
			stmt.setString(5, routeNo);
			stmt.setString(6, busCategory);
			stmt.setString(7, swap);
			stmt.setString(8, tripType);
			stmt.setString(9, group);
			stmt.setString(10, startDate);
			stmt.setString(11, endDate);
			stmt.setString(12, numOfDays);
			stmt.setTimestamp(13, new Timestamp(new Date().getTime()));

			stmt.executeUpdate();

			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			con.commit();
			/** insert date former dates in to history table end **/

			String sql = "UPDATE public.nt_m_route_schedule_generator "
					+ "SET rs_end_date=?,rs_no_of_dates=?  , rs_start_date=?"
					+ "WHERE rs_generator_ref_no=? and rs_route_no=? and rs_bus_category_code=? and rs_trip_type=? and rs_group_no=? ";

			try {
				stmt = con.prepareStatement(sql);
				stmt.setString(1, formatter.format(lastPanelEndDate));
				stmt.setString(2, Integer.toString(dateDifference));
				stmt.setString(3, formatter.format(lastPanelStartDateNew));
				stmt.setString(4, generatedRefNo);
				stmt.setString(5, routeNo);
				stmt.setString(6, busCategory);
				stmt.setString(7, tripType);
				stmt.setString(8, group);
				stmt.executeUpdate();
				con.commit();
			} catch (SQLException e) {
				con.rollback();
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
	}

	@Override
	public List<String> getDateRangesForRouteSchedule(String routeNo, String busCategory, String generatedRefNo,
			String groupNo, String tripType) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<String> dateList = new ArrayList<String>();
		String lastEndDate = null;
		try {
			con = ConnectionManager.getConnection();

			String selectQuery = "SELECT rs_start_date, rs_end_date " + "FROM public.nt_h_route_schedule_generator "
					+ "where rs_generator_ref_no=? and rs_route_no=? and rs_bus_category_code=? and rs_trip_type=? and rs_group_no=? ";
			stmt = con.prepareStatement(selectQuery);

			stmt.setString(1, generatedRefNo);
			stmt.setString(2, routeNo);
			stmt.setString(3, busCategory);
			stmt.setString(4, tripType);
			stmt.setString(5, groupNo);

			rs = stmt.executeQuery();

			while (rs.next()) {
				dateList.add(rs.getString("rs_start_date") + "-" + rs.getString("rs_end_date"));
				lastEndDate = rs.getString("rs_end_date");
			}

			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);

			String select = "SELECT rs_start_date,rs_end_date " + "FROM public.nt_m_route_schedule_generator "
					+ "where rs_generator_ref_no=? and rs_route_no=? and rs_bus_category_code=? and rs_trip_type=? and rs_group_no=?";
			stmt = con.prepareStatement(select);

			stmt.setString(1, generatedRefNo);
			stmt.setString(2, routeNo);
			stmt.setString(3, busCategory);
			stmt.setString(4, tripType);
			stmt.setString(5, groupNo);

			rs = stmt.executeQuery();

			while (rs.next()) {
				if (lastEndDate == null || lastEndDate.isEmpty() || lastEndDate.trim().equals("")) {
					lastEndDate = rs.getString("rs_start_date");
				}

				dateList.add(lastEndDate + "-" + rs.getString("rs_end_date"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return dateList;
	}

	@Override
	public List<String> selectEditDataForRouteSchedule(String routeNo, String busCategory, String generatedRefNo,
			String group, String tripType, int dateCount) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String swap = null;
		String selectedCreatedDate = null;
		List<String> busNoList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();
			if (tripType != null && !tripType.isEmpty() && !tripType.trim().equals("")) {
				if (tripType.equalsIgnoreCase("O")) {
					swap = "N";
				}
				if (tripType.equalsIgnoreCase("D")) {
					swap = "Y";
				}
			}

			String select = "SELECT distinct a.created_date FROM public.nt_t_route_schedule_generator_det01 a "
					+ "LEFT JOIN public.nt_m_route_schedule_generator b  on a.generator_ref_no=b.rs_generator_ref_no "
					+ "where a.generator_ref_no=? and b.rs_route_no=? and b.rs_bus_category_code=? and b.rs_is_swap=? and  b.rs_trip_type=? and b.rs_group_no=? and a.trip_type=? ";
			ps = con.prepareStatement(select);

			ps.setString(1, generatedRefNo);
			ps.setString(2, routeNo);
			ps.setString(3, busCategory);
			ps.setString(4, swap);
			ps.setString(5, tripType);
			ps.setString(6, group);
			ps.setString(7, tripType);

			rs = ps.executeQuery();

			List<String> dateListtemp = new ArrayList<>();
			while (rs.next()) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				String string = dateFormat.format(rs.getTimestamp("created_date"));
				dateListtemp.add(string);
			}

			int count = 0;
			for (String s : dateListtemp) {
				count = count + 1;
				if (count == dateCount) {
					selectedCreatedDate = s;
				}
			}

			ConnectionManager.close(rs);
			ConnectionManager.close(ps);

			String sql = "SELECT a.bus_no,a.seq as seq   FROM public.nt_t_route_schedule_generator_det01 a "
					+ "LEFT JOIN public.nt_m_route_schedule_generator b  on a.generator_ref_no=b.rs_generator_ref_no "
					+ "where a.generator_ref_no=? and b.rs_route_no=? and b.rs_bus_category_code=? and b.rs_is_swap=? and  b.rs_trip_type=? and b.rs_group_no=? and a.trip_type=? "
					+ "and to_char((a.created_date),'dd/MM/yyyy') like '" + selectedCreatedDate + "%' "
					+ "order by a.seq ASC";

			ps = con.prepareStatement(sql);
			ps.setString(1, generatedRefNo);
			ps.setString(2, routeNo);
			ps.setString(3, busCategory);
			ps.setString(4, swap);
			ps.setString(5, tripType);
			ps.setString(6, group);
			ps.setString(7, tripType);

			rs = ps.executeQuery();

			while (rs.next()) {

				String assignedBus = getAssignedBusNumForSelectedBus(con, routeNo, busCategory, generatedRefNo, group,
						tripType, rs.getString("bus_no"));
				if (assignedBus != null && !assignedBus.isEmpty() && !assignedBus.trim().equals("")) {
					busNoList.add(assignedBus + "-" + rs.getLong("seq"));
				} else {
					busNoList.add(rs.getString("bus_no") + "-" + rs.getLong("seq"));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return busNoList;
	}

	@Override
	public List<RouteScheduleDTO> getBusNoListForRotation(String routeNo, String refNo, String groupNo, String tripType,
			String busCategory) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<RouteScheduleDTO> busNoList = new ArrayList<RouteScheduleDTO>();
		int privateBuses = 0;
		String originAbbr = null;
		String destAbbr = null;
		try {
			con = ConnectionManager.getConnection();

			// get abbreviation for origin and destination start
			String abbrQuery = "SELECT rc_abbriviation_ltr_start, rc_abbriviation_ltr_end FROM public.nt_t_route_creator "
					+ "where rc_route_no=? and rc_bus_type=?";

			ps = con.prepareStatement(abbrQuery);
			ps.setString(1, routeNo);
			ps.setString(2, busCategory);
			rs = ps.executeQuery();

			while (rs.next()) {
				originAbbr = rs.getString("rc_abbriviation_ltr_start");
				destAbbr = rs.getString("rc_abbriviation_ltr_end");
			}

			try {
				if (rs != null)
					rs.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				if (ps != null)
					ps.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			// get abbreviation for origin and destination end

			// get private buses start

			String originPVTQuery = "select a.no_of_pvt_bus from public.nt_t_trips_generator_det02 a "
					+ "left join public.nt_m_trips_generator b " + "on a.trips_ref_code=b.tg_trips_ref_code "
					+ "where b.tg_generator_ref_no=? " + "and a.group_no=? and a.trip_type=?";

			ps = con.prepareStatement(originPVTQuery);
			ps.setString(1, refNo);
			ps.setString(2, groupNo);
			ps.setString(3, tripType);
			rs = ps.executeQuery();

			while (rs.next()) {
				privateBuses = rs.getInt("no_of_pvt_bus");
			}

			try {
				if (rs != null)
					rs.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				if (ps != null)
					ps.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			// get private buses end

			/** create temp origin and destination bus numbers with leave buses start **/
			busNoList = new ArrayList<RouteScheduleDTO>();
			for (int i = 0; i < privateBuses; i++) {
				RouteScheduleDTO dto = new RouteScheduleDTO();
				int count = i + 1;
				dto.setTripId(Integer.toString(count));
				if (tripType.equals("O")) {
					dto.setBusNo(originAbbr + count);
				} else {
					dto.setBusNo(destAbbr + count);
				}
				busNoList.add(dto);
			}
			/** create temp origin and destination bus numbers with leave buses end **/

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return busNoList;
	}

	@Override
	public int getNoOfTripsPerSide(String routeNo, String refNo, String groupNo, String tripType, String busCat) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		PreparedStatement ps2 = null;
		ResultSet rs2 = null;
		int noOftrips = 0;
		String originAbbr = null;
		String destAbbr = null;
		String busNumLike = null;
		int noOfLeaves = 0;
		String coupleCount = null;
		try {
			con = ConnectionManager.getConnection();
			String abbrQuery = "SELECT rc_abbriviation_ltr_start, rc_abbriviation_ltr_end FROM public.nt_t_route_creator "
					+ "where rc_route_no=? and rc_bus_type=?";

			ps = con.prepareStatement(abbrQuery);
			ps.setString(1, routeNo);
			ps.setString(2, busCat);
			rs = ps.executeQuery();

			while (rs.next()) {
				originAbbr = rs.getString("rc_abbriviation_ltr_start");
				destAbbr = rs.getString("rc_abbriviation_ltr_end");
			}
			if (tripType.equalsIgnoreCase("O")) {
				busNumLike = originAbbr + '%';

			} else {
				busNumLike = destAbbr + '%';
			}

			String query1 = "SELECT no_of_buses_on_leave, no_of_couples_per_one_bus \r\n"
					+ "FROM public.nt_m_timetable_generator\r\n" + "where generator_ref_no=?\r\n"
					+ "and  group_no=?\r\n" + "and trip_type=?";

			ps2 = con.prepareStatement(query1);
			ps2.setString(1, refNo);
			ps2.setString(2, groupNo);
			ps2.setString(3, tripType);

			rs2 = ps2.executeQuery();

			while (rs2.next()) {

				noOfLeaves = rs2.getInt("no_of_buses_on_leave");
				coupleCount = rs2.getString("no_of_couples_per_one_bus");

			}

			String query2 = "SELECT count(bus_num)as countOfTrips\r\n"
					+ "FROM public.nt_m_timetable_generator_det where generator_ref_no=? and group_no=?\r\n"
					+ "and trip_type=? and is_fixed_time='N' and bus_num is not null and bus_num !=''\r\n"
					+ "and  bus_num like " + "'" + busNumLike + "'"
					+ "and bus_num not in('SLTB-O','SLTB-D','ETC-O','ETC-D') ; ";

			ps1 = con.prepareStatement(query2);
			ps1.setString(1, refNo);
			ps1.setString(2, groupNo);
			ps1.setString(3, tripType);

			rs1 = ps1.executeQuery();

			while (rs1.next()) {
				if (noOfLeaves > 0) {
					if (coupleCount.equals("2")) {
						noOftrips = Integer.valueOf(rs1.getString("countOfTrips")) + (noOfLeaves * 2);
					}

					else {
						noOftrips = Integer.valueOf(rs1.getString("countOfTrips")) + noOfLeaves;
					}
				} else {
					noOftrips = Integer.valueOf(rs1.getString("countOfTrips"));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(rs1);
			ConnectionManager.close(ps);
			ConnectionManager.close(ps1);
			ConnectionManager.close(rs2);
			ConnectionManager.close(ps2);
			ConnectionManager.close(con);
		}

		return noOftrips;
	}

	@Override
	public int getNoOfTripsPerSideWithSltb(String routeNo, String refNo, String groupNo, String tripType,
			String busCat) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		PreparedStatement ps2 = null;
		int noOftrips = 0;
		String originAbbr = null;
		String destAbbr = null;
		String busNumLike = null;
		int noOfLeaves = 0;

		try {
			con = ConnectionManager.getConnection();
			String abbrQuery = "SELECT rc_abbriviation_ltr_start, rc_abbriviation_ltr_end FROM public.nt_t_route_creator "
					+ "where rc_route_no=? and rc_bus_type=?";

			ps = con.prepareStatement(abbrQuery);
			ps.setString(1, routeNo);
			ps.setString(2, busCat);
			rs = ps.executeQuery();

			while (rs.next()) {
				originAbbr = rs.getString("rc_abbriviation_ltr_start");
				destAbbr = rs.getString("rc_abbriviation_ltr_end");
			}
			if (tripType.equalsIgnoreCase("O")) {
				busNumLike = originAbbr + '%';

			} else {
				busNumLike = destAbbr + '%';
			}

			String query1 = "SELECT no_of_buses_on_leave\r\n" + "FROM public.nt_m_timetable_generator\r\n"
					+ "where generator_ref_no=?\r\n" + "and  group_no=?\r\n" + "and trip_type=?";

			ps2 = con.prepareStatement(query1);
			ps2.setString(1, refNo);
			ps2.setString(2, groupNo);
			ps2.setString(3, tripType);

			rs2 = ps2.executeQuery();

			while (rs2.next()) {

				noOfLeaves = Integer.valueOf(rs2.getString("no_of_buses_on_leave"));

			}
			String query2 = "SELECT count(bus_num) as countOfTrips\r\n"
					+ "FROM public.nt_m_timetable_generator_det where generator_ref_no=? and group_no=?\r\n"
					+ "and trip_type=? and is_fixed_time='N' and bus_num is not null and bus_num !=''\r\n"
					+ "and  bus_num like " + "'" + busNumLike + "'"
					+ "and bus_num  in('SLTB-O','SLTB-D','ETC-O','ETC-D') ; ";

			ps1 = con.prepareStatement(query2);
			ps1.setString(1, refNo);
			ps1.setString(2, groupNo);
			ps1.setString(3, tripType);

			rs1 = ps1.executeQuery();

			while (rs1.next()) {
				if (noOfLeaves > 0) {
					noOftrips = Integer.valueOf(rs1.getString("countOfTrips")) + noOfLeaves;
				} else {
					noOftrips = Integer.valueOf(rs1.getString("countOfTrips"));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(rs1);
			ConnectionManager.close(ps);
			ConnectionManager.close(ps1);
			ConnectionManager.close(rs2);
			ConnectionManager.close(ps2);
			ConnectionManager.close(con);
		}

		return noOftrips;
	}

	@Override
	public int getNoOfTripsPerSideForEdit(String routeNo, String refNo, String groupNo, String tripType,
			String busCat) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		PreparedStatement ps2 = null;
		ResultSet rs2 = null;
		int noOftrips = 0;
		String originAbbr = null;
		String destAbbr = null;
		String busNumLike = null;
		int noOfLeaves = 0;

		try {
			con = ConnectionManager.getConnection();
			String abbrQuery = "SELECT rc_abbriviation_ltr_start, rc_abbriviation_ltr_end FROM public.nt_t_route_creator "
					+ "where rc_route_no=? and rc_bus_type=?";

			ps = con.prepareStatement(abbrQuery);
			ps.setString(1, routeNo);
			ps.setString(2, busCat);
			rs = ps.executeQuery();

			while (rs.next()) {
				originAbbr = rs.getString("rc_abbriviation_ltr_start");
				destAbbr = rs.getString("rc_abbriviation_ltr_end");
			}
			if (tripType.equalsIgnoreCase("O")) {
				busNumLike = originAbbr + '%';

			} else {
				busNumLike = destAbbr + '%';
			}

			String query1 = "SELECT no_of_buses_on_leave\r\n" + "FROM public.nt_m_timetable_generator\r\n"
					+ "where generator_ref_no=?\r\n" + "and  group_no=?\r\n" + "and trip_type=?";

			ps2 = con.prepareStatement(query1);
			ps2.setString(1, refNo);
			ps2.setString(2, groupNo);
			ps2.setString(3, tripType);

			rs2 = ps2.executeQuery();

			while (rs2.next()) {

				noOfLeaves = rs2.getInt("no_of_buses_on_leave");

			}

			String query2 = "SELECT count(bus_num) as countOfTrips\r\n"
					+ "FROM public.nt_m_timetable_generator_det where generator_ref_no=? and group_no=?\r\n"
					+ "and trip_type=? and is_fixed_time='N' and bus_num is not null and bus_num !='' and start_time_slot is not null\r\n"
					+ "and  bus_num like " + "'" + busNumLike + "'";

			ps1 = con.prepareStatement(query2);
			ps1.setString(1, refNo);
			ps1.setString(2, groupNo);
			ps1.setString(3, tripType);

			rs1 = ps1.executeQuery();

			while (rs1.next()) {
				if (noOfLeaves > 0) {
					noOftrips = Integer.valueOf(rs1.getString("countOfTrips")) + noOfLeaves;
				} else {
					noOftrips = Integer.valueOf(rs1.getString("countOfTrips"));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(rs1);
			ConnectionManager.close(ps);
			ConnectionManager.close(ps1);
			ConnectionManager.close(con);
		}

		return noOftrips;
	}

	@Override
	public List<String> selectEditDataForRouteScheduleNew(String routeNo, String busCategory, String generatedRefNo,
			String group, String tripType, int dateCount) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String swap = null;
		String selectedCreatedDate = null;
		List<String> busNoList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();
			if (tripType != null && !tripType.isEmpty() && !tripType.trim().equals("")) {
				if (tripType.equalsIgnoreCase("O")) {
					swap = "N";
				}
				if (tripType.equalsIgnoreCase("D")) {
					swap = "Y";
				}
			}

			String select = "SELECT distinct a.created_date FROM public.nt_t_route_schedule_generator_det01 a "
					+ "LEFT JOIN public.nt_m_route_schedule_generator b  on a.generator_ref_no=b.rs_generator_ref_no "
					+ "where a.generator_ref_no=? and b.rs_route_no=? and b.rs_bus_category_code=? and b.rs_is_swap=? and  b.rs_trip_type=? and b.rs_group_no=? and a.trip_type=? ";
			ps = con.prepareStatement(select);

			ps.setString(1, generatedRefNo);
			ps.setString(2, routeNo);
			ps.setString(3, busCategory);
			ps.setString(4, swap);
			ps.setString(5, tripType);
			ps.setString(6, group);
			ps.setString(7, tripType);

			rs = ps.executeQuery();

			List<String> dateListtemp = new ArrayList<>();
			while (rs.next()) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				String string = dateFormat.format(rs.getTimestamp("created_date"));
				dateListtemp.add(string);
			}

			int count = 0;
			for (String s : dateListtemp) {
				count = count + 1;
				if (count == dateCount) {
					selectedCreatedDate = s;
				}
			}

			ConnectionManager.close(rs);
			ConnectionManager.close(ps);

			String sql = "SELECT a.bus_no,a.seq as seq   FROM public.nt_t_route_schedule_generator_det01 a "
					+ "LEFT JOIN public.nt_m_route_schedule_generator b  on a.generator_ref_no=b.rs_generator_ref_no "
					+ "where a.generator_ref_no=? and b.rs_route_no=? and b.rs_bus_category_code=? and b.rs_is_swap=? and  b.rs_trip_type=? and b.rs_group_no=? and a.trip_type=? "
					+ "and to_char((a.created_date),'dd/MM/yyyy') like '" + selectedCreatedDate + "%' "
					+ "order by a.seq ASC";

			ps = con.prepareStatement(sql);
			ps.setString(1, generatedRefNo);
			ps.setString(2, routeNo);
			ps.setString(3, busCategory);
			ps.setString(4, swap);
			ps.setString(5, tripType);
			ps.setString(6, group);
			ps.setString(7, tripType);

			rs = ps.executeQuery();

			while (rs.next()) {

				busNoList.add(rs.getString("bus_no") + "-" + rs.getLong("seq"));

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return busNoList;
	}

	@Override
	public boolean checkBusAssignerDoneLetEdit(String busCategory, String generatedRefNo, String groupNo,
			String tripType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean canEdit = false;

		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT * FROM public.nt_m_timetable_generator_det \r\n"
					+ "WHERE generator_ref_no=? and group_no=? and trip_type=? and assigned_bus_no is  null or assigned_bus_no=''";

			ps = con.prepareStatement(query2);
			ps.setString(1, generatedRefNo);
			ps.setString(2, groupNo);
			ps.setString(3, tripType);
			rs = ps.executeQuery();

			if (rs.next()) {
				canEdit = true;
			} else {
				canEdit = false;
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return canEdit;
	}

	@Override
	public int getFixedOrWithoutFixedTripsCountNew(String refNo, String groupNo, String tripType, String fixType,
			String routeNo, String busCat) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int count = 0;
		String originAbbr = null;
		String destAbbr = null;

		String busNumLike = null;

		try {
			con = ConnectionManager.getConnection();

			// get abbreviation for origin and destination start
			String abbrQuery = "SELECT rc_abbriviation_ltr_start, rc_abbriviation_ltr_end FROM public.nt_t_route_creator "
					+ "where rc_route_no=? and rc_bus_type=?";

			ps = con.prepareStatement(abbrQuery);
			ps.setString(1, routeNo);
			ps.setString(2, busCat);
			rs = ps.executeQuery();

			while (rs.next()) {
				originAbbr = rs.getString("rc_abbriviation_ltr_start");
				destAbbr = rs.getString("rc_abbriviation_ltr_end");
			}

			try {
				if (rs != null)
					rs.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				if (ps != null)
					ps.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			// get abbreviation for origin and destination end

			if (tripType.equalsIgnoreCase("O")) {
				busNumLike = originAbbr + '%';

			} else {
				busNumLike = destAbbr + '%';
			}
			String query2 = "select count( is_fixed_time) as busCount  FROM public.nt_m_timetable_generator_det "
					+ "WHERE is_fixed_time=? and group_no=? and trip_type=? and generator_ref_no=? and  bus_num like "
					+ "'" + busNumLike + "' and bus_num is not null and bus_num !=''";

			ps = con.prepareStatement(query2);
			ps.setString(1, fixType);
			ps.setString(2, groupNo);
			ps.setString(3, tripType);
			ps.setString(4, refNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				count = rs.getInt("busCount");
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return count;
	}

	@Override
	public List<RouteScheduleDTO> selectEditDataForRouteScheduleDTO(String routeNo, String busCategory,
			String generatedRefNo, String group, String tripType, int dateCount) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String swap = null;
		String selectedCreatedDate = null;
		List<RouteScheduleDTO> busNoList = new ArrayList<RouteScheduleDTO>();
		RouteScheduleDTO busNos = new RouteScheduleDTO();

		try {
			con = ConnectionManager.getConnection();
			if (tripType != null && !tripType.isEmpty() && !tripType.trim().equals("")) {
				if (tripType.equalsIgnoreCase("O")) {
					swap = "N";
				}
				if (tripType.equalsIgnoreCase("D")) {
					swap = "Y";
				}
			}

			/** :'D **/
			String select = "SELECT distinct a.created_date FROM public.nt_t_route_schedule_generator_det01 a "
					+ "LEFT JOIN public.nt_m_route_schedule_generator b  on a.generator_ref_no=b.rs_generator_ref_no "
					+ "where a.generator_ref_no=? and b.rs_route_no=? and b.rs_bus_category_code=? and b.rs_is_swap=? and  b.rs_trip_type=? and b.rs_group_no=? and a.trip_type=? ";
			ps = con.prepareStatement(select);

			ps.setString(1, generatedRefNo);
			ps.setString(2, routeNo);
			ps.setString(3, busCategory);
			ps.setString(4, swap);
			ps.setString(5, tripType);
			ps.setString(6, group);
			ps.setString(7, tripType);

			rs = ps.executeQuery();

			List<String> dateListtemp = new ArrayList<>();
			while (rs.next()) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				String string = dateFormat.format(rs.getTimestamp("created_date"));
				dateListtemp.add(string);
			}

			int count = 0;
			for (String s : dateListtemp) {
				count = count + 1;
				if (count == dateCount) {
					selectedCreatedDate = s;
				}
			}

			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			/** :'D **/

			String sql = "SELECT a.bus_no,a.seq as seq   FROM public.nt_t_route_schedule_generator_det01 a "
					+ "LEFT JOIN public.nt_m_route_schedule_generator b  on a.generator_ref_no=b.rs_generator_ref_no "
					+ "where a.generator_ref_no=? and b.rs_route_no=? and b.rs_bus_category_code=? and b.rs_is_swap=? and  b.rs_trip_type=? and b.rs_group_no=? and a.trip_type=? "
					+ "and to_char((a.created_date),'dd/MM/yyyy') like '" + selectedCreatedDate + "%' "
					+ "order by a.seq ASC";

			ps = con.prepareStatement(sql);
			ps.setString(1, generatedRefNo);
			ps.setString(2, routeNo);
			ps.setString(3, busCategory);
			ps.setString(4, swap);
			ps.setString(5, tripType);
			ps.setString(6, group);
			ps.setString(7, tripType);

			rs = ps.executeQuery();

			while (rs.next()) {
				busNos = new RouteScheduleDTO();
				busNos.setBusNo((rs.getString("bus_no") + "-" + rs.getLong("seq")));
				busNoList.add(busNos);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return busNoList;
	}

	@Override
	public boolean updateEditedBusNumbersAcordinTrip(String refNo, String tripType, String dayOne, String tripOne,
			String busOne) {

		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String sqlHistory = "INSERT into public.nt_h_route_schedule_generator_det01\r\n"
					+ "(SELECT * FROM public.nt_t_route_schedule_generator_det01 WHERE  generator_ref_no=? and day_no=? and trip_id=? and trip_type=?) ";

			stmt1 = con.prepareStatement(sqlHistory);
			stmt1.setString(1, refNo);
			stmt1.setInt(2, Integer.parseInt(dayOne));
			stmt1.setString(3, tripOne);
			stmt1.setString(4, tripType);
			stmt1.executeUpdate();
			con.commit();

			String sql = "update nt_t_route_schedule_generator_det01\r\n" + "set bus_no=?\r\n"
					+ "where generator_ref_no=?\r\n" + "and day_no=?\r\n" + "and trip_id=?\r\n" + "and trip_type=?";

			stmt = con.prepareStatement(sql);
			try {
				stmt.setString(1, busOne);
				stmt.setString(2, refNo);
				stmt.setInt(3, Integer.parseInt(dayOne));
				stmt.setString(4, tripOne);
				stmt.setString(5, tripType);
				stmt.executeUpdate();

				con.commit();
			} catch (SQLException e) {
				con.rollback();
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
			ConnectionManager.close(stmt1);
		}
		return true;
	}

	@Override
	public String getCoupleForRef(String refNo, String tripType, String groupNo) {
		Connection con = null;
		;

		PreparedStatement ps2 = null;
		ResultSet rs2 = null;

		String coupleCount = null;
		try {
			con = ConnectionManager.getConnection();

			String query1 = "SELECT no_of_buses_on_leave, no_of_couples_per_one_bus \r\n"
					+ "FROM public.nt_m_timetable_generator\r\n" + "where generator_ref_no=?\r\n"
					+ "and  group_no=?\r\n" + "and trip_type=?";

			ps2 = con.prepareStatement(query1);
			ps2.setString(1, refNo);
			ps2.setString(2, groupNo);
			ps2.setString(3, tripType);

			rs2 = ps2.executeQuery();

			while (rs2.next()) {

				coupleCount = rs2.getString("no_of_couples_per_one_bus");

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs2);
			ConnectionManager.close(ps2);
			ConnectionManager.close(con);

		}
		return coupleCount;
	}

	@Override
	public List<RouteScheduleDTO> getBusNoListForRotationWithCoupleTwo(String routeNo, String refNo, String groupNo,
			String tripType, String busCategory) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<RouteScheduleDTO> busNoList = new ArrayList<RouteScheduleDTO>();
		int privateBuses = 0;
		String originAbbr = null;
		String destAbbr = null;
		try {
			con = ConnectionManager.getConnection();

			// get abbreviation for origin and destination start
			String abbrQuery = "SELECT rc_abbriviation_ltr_start, rc_abbriviation_ltr_end FROM public.nt_t_route_creator "
					+ "where rc_route_no=? and rc_bus_type=?";

			ps = con.prepareStatement(abbrQuery);
			ps.setString(1, routeNo);
			ps.setString(2, busCategory);
			rs = ps.executeQuery();

			while (rs.next()) {
				originAbbr = rs.getString("rc_abbriviation_ltr_start");
				destAbbr = rs.getString("rc_abbriviation_ltr_end");
			}

			try {
				if (rs != null)
					rs.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				if (ps != null)
					ps.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			String originPVTQuery = "select sum(a.no_of_pvt_bus) as no_of_pvt_bus from public.nt_t_trips_generator_det02 a "
					+ "left join public.nt_m_trips_generator b " + "on a.trips_ref_code=b.tg_trips_ref_code "
					+ "where b.tg_generator_ref_no=? " + "and a.group_no=? ";

			ps = con.prepareStatement(originPVTQuery);
			ps.setString(1, refNo);
			ps.setString(2, groupNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				privateBuses = rs.getInt("no_of_pvt_bus");
			}

			try {
				if (rs != null)
					rs.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				if (ps != null)
					ps.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			// get private buses end

			/** create temp origin and destination bus numbers with leave buses start **/
			busNoList = new ArrayList<RouteScheduleDTO>();
			for (int i = 0; i < privateBuses; i++) {
				RouteScheduleDTO dto = new RouteScheduleDTO();
				int count = i + 1;
				dto.setTripId(Integer.toString(count));
				if (tripType.equals("O")) {
					dto.setBusNo(originAbbr + count);
				} else {
					dto.setBusNo(destAbbr + count);
				}
				busNoList.add(dto);
			}
			/** create temp origin and destination bus numbers with leave buses end **/

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return busNoList;
	}

	@Override
	public List<RouteScheduleDTO> getBusNoListWithoutCTBBusWithCoupleTwo(String routeNo, String refNo, String groupNo,
			String tripType, String busCategory) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<RouteScheduleDTO> busNoList = new ArrayList<RouteScheduleDTO>();
		int privateBusOrigin = 0;
		int privateBusDestination = 0;
		String originAbbr = null;
		String destAbbr = null;
		int tempCount = 0;

		String busNumLike = null;
		/** added by like by tharushi.e in line number1542 **/

		try {
			con = ConnectionManager.getConnection();

			// get abbreviation for origin and destination start
			String abbrQuery = "SELECT rc_abbriviation_ltr_start, rc_abbriviation_ltr_end FROM public.nt_t_route_creator "
					+ "where rc_route_no=? and rc_bus_type=?";

			ps = con.prepareStatement(abbrQuery);
			ps.setString(1, routeNo);
			ps.setString(2, busCategory);
			rs = ps.executeQuery();

			while (rs.next()) {
				originAbbr = rs.getString("rc_abbriviation_ltr_start");
				destAbbr = rs.getString("rc_abbriviation_ltr_end");
			}

			try {
				if (rs != null)
					rs.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				if (ps != null)
					ps.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			// get abbreviation for origin and destination end

			// get private buses for origin start
			if (tripType != null && !tripType.isEmpty() && !tripType.trim().equals("")
					&& tripType.equalsIgnoreCase("O")) {

				String originPVTQuery = "select sum(a.no_of_pvt_bus) as no_of_pvt_bus from public.nt_t_trips_generator_det02 a "
						+ "left join public.nt_m_trips_generator b " + "on a.trips_ref_code=b.tg_trips_ref_code "
						+ "where b.tg_generator_ref_no=? " + "and a.group_no=? and a.trip_type=?";

				ps = con.prepareStatement(originPVTQuery);
				ps.setString(1, refNo);
				ps.setString(2, groupNo);
				ps.setString(3, "O");
				rs = ps.executeQuery();

				while (rs.next()) {
					privateBusOrigin = rs.getInt("no_of_pvt_bus");
				}

				try {
					if (rs != null)
						rs.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				try {
					if (ps != null)
						ps.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			// get private buses for origin end

			// get private buses for destination start
			if (tripType != null && !tripType.isEmpty() && !tripType.trim().equals("")
					&& tripType.equalsIgnoreCase("D")) {
				String destinationPVTQuery = "select a.no_of_pvt_bus from public.nt_t_trips_generator_det02 a "
						+ "left join public.nt_m_trips_generator b " + "on a.trips_ref_code=b.tg_trips_ref_code "
						+ "where b.tg_generator_ref_no=? " + "and a.group_no=? and a.trip_type=?";

				ps = con.prepareStatement(destinationPVTQuery);
				ps.setString(1, refNo);
				ps.setString(2, groupNo);
				ps.setString(3, "D");
				rs = ps.executeQuery();

				while (rs.next()) {
					privateBusDestination = rs.getInt("no_of_pvt_bus");
				}

				try {
					if (rs != null)
						rs.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				try {
					if (ps != null)
						ps.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			// get private buses for destination end
			
//			added by thilna.d to remove fixed bus numbers on 12-10-2021
			// get fixed bus numbers start
			List<String> fixedBusNoList = new ArrayList<String>();
			String fixedBusNoQuery = "SELECT distinct bus_num FROM public.nt_m_timetable_generator_det\r\n"
					+ "WHERE generator_ref_no=? and group_no=? and is_fixed_time=?;";

			ps = con.prepareStatement(fixedBusNoQuery);
			ps.setString(1, refNo);
			ps.setString(2, groupNo);
			ps.setString(3, "Y");
			rs = ps.executeQuery();

			while (rs.next()) {
				fixedBusNoList.add(rs.getString("bus_num"));
			}
			
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			// get fixed bus numbers end

			/** create temp origin and destination bus numbers with leave buses start **/
			// origin buses
			List<String> originBusList = new ArrayList<String>();
			for (int i = 0; i < privateBusOrigin; i++) {
				int count = i + 1;
//				changed by thilna.d to remove fixed bus numbers on 12-10-2021
				if(!fixedBusNoList.contains(originAbbr + count)) {
					originBusList.add(originAbbr + count);
				}
			}

			// destination buses
			List<String> destBusList = new ArrayList<String>();
			for (int i = 0; i < privateBusDestination; i++) {
				int count = i + 1;
//				changed by thilna.d to remove fixed bus numbers on 12-10-2021
				if(!fixedBusNoList.contains(destAbbr + count)) {
					destBusList.add(destAbbr + count);
				}
			}
			/** create temp origin and destination bus numbers with leave buses end **/

			// add both lists together start
			List<String> tempAllBusNums = new ArrayList<String>();

			if (originBusList != null && originBusList.size() != 0) {
				tempAllBusNums.addAll(originBusList);
			}
			if (destBusList != null && destBusList.size() != 0) {
				tempAllBusNums.addAll(destBusList);
			}
			// add both lists together end

			if (tripType.equalsIgnoreCase("O")) {
				busNumLike = originAbbr + '%';

			} else {
				busNumLike = destAbbr + '%';
			}
			String query2 = "SELECT distinct bus_num, generator_ref_no, group_no, trip_type,is_pvt_bus "
					+ "FROM public.nt_m_timetable_generator_det "
					+ "where generator_ref_no=? and group_no=? and trip_type=? and is_fixed_time='N' "
					+ "and bus_num is not null and bus_num !='' and  bus_num like " + "'" + busNumLike + "' "
					+ " and bus_num not in('SLTB-O','SLTB-D','ETC-O','ETC-D') order by bus_num ";

			ps = con.prepareStatement(query2);
			ps.setString(1, refNo);
			ps.setString(2, groupNo);
			ps.setString(3, tripType);
			rs = ps.executeQuery();
			RouteScheduleDTO e;

			for (int i = 0; rs.next(); i++) {

				e = new RouteScheduleDTO();
				String id = String.valueOf(i + 1);
				tempCount = i + 1;
				e.setTripId(id);

				e.setBusNo(rs.getString("bus_num"));

				busNoList.add(e);

			}

			/** add missing busses to main bus list start **/
			List<String> missingBusses = new ArrayList<String>();
			boolean found = false;
			for (String s : tempAllBusNums) {
				found = false;
				for (RouteScheduleDTO dto : busNoList) {
					if (s.equals(dto.getBusNo())) {
						found = true;
						break;
					}
				}
				if (!found) {
					missingBusses.add(s);
				}
			}

			for (String s : missingBusses) {
				e = new RouteScheduleDTO();
				tempCount = tempCount + 1;
				String id = String.valueOf(tempCount);
				e.setTripId(id);
				e.setBusNo(s);
				busNoList.add(e);
			}

			final String oriAbb = originAbbr;
			final String desAbb = destAbbr;
			Collections.sort(busNoList, new Comparator<RouteScheduleDTO>() {
				@Override
				public int compare(RouteScheduleDTO u1, RouteScheduleDTO u2) {
					return extractInt(u2.getBusNo()) - extractInt(u1.getBusNo());
				}

				int extractInt(String s) {
					String num = "";
					if (s.contains(oriAbb)) {
						num = s.replaceAll(oriAbb, "");
					}
					if (s.contains(desAbb)) {
						num = s.replaceAll(desAbb, "");
					}

					return num.isEmpty() || !num.matches("[0-9]+") ? 0 : Integer.parseInt(num);
				}
			});

			Collections.reverse(busNoList);

			Collections.sort(busNoList, new Comparator<RouteScheduleDTO>() {
				@Override
				public int compare(RouteScheduleDTO u1, RouteScheduleDTO u2) {
					String num1 = "";
					String num2 = "";

					if (u2.getBusNo().contains(oriAbb)) {
						num1 = oriAbb;
					}

					if (u2.getBusNo().contains(desAbb)) {
						num1 = desAbb;
					}
					if (u1.getBusNo().contains(oriAbb)) {
						num2 = oriAbb;
					}

					if (u1.getBusNo().contains(desAbb)) {
						num2 = desAbb;
					}

					return num1.compareTo(num2);

				}
			});

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return busNoList;
	}

	@Override
	public int getFixedOrWithoutFixedTripsCountNewWithCoupleTwo(String refNo, String groupNo, String tripType,
			String fixType, String routeNo, String busCat) {
		Connection con = null;
		PreparedStatement ps = null, ps2 = null;
		ResultSet rs = null, rs2 = null;
		int count = 0;
		String originAbbr = null;
		String destAbbr = null;
		String noOfLeaves = null;

		String busNumLike = null;
		String query2 = null;
		try {
			con = ConnectionManager.getConnection();

			String query1 = "SELECT no_of_buses_on_leave\r\n" + "FROM public.nt_m_timetable_generator\r\n"
					+ "where generator_ref_no=?\r\n" + "and  group_no=?\r\n" + "and trip_type=?";

			ps2 = con.prepareStatement(query1);
			ps2.setString(1, refNo);
			ps2.setString(2, groupNo);
			ps2.setString(3, tripType);

			rs2 = ps2.executeQuery();

			while (rs2.next()) {

				noOfLeaves = rs2.getString("no_of_buses_on_leave");

			}

			try {
				if (rs2 != null)
					rs2.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				if (ps2 != null)
					ps2.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			// get abbreviation for origin and destination start
			String abbrQuery = "SELECT rc_abbriviation_ltr_start, rc_abbriviation_ltr_end FROM public.nt_t_route_creator "
					+ "where rc_route_no=? and rc_bus_type=?";

			ps = con.prepareStatement(abbrQuery);
			ps.setString(1, routeNo);
			ps.setString(2, busCat);
			rs = ps.executeQuery();

			while (rs.next()) {
				originAbbr = rs.getString("rc_abbriviation_ltr_start");
				destAbbr = rs.getString("rc_abbriviation_ltr_end");
			}

			try {
				if (rs != null)
					rs.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				if (ps != null)
					ps.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			// get abbreviation for origin and destination end

			if (tripType.equalsIgnoreCase("O")) {
				busNumLike = originAbbr + '%';

			} else {
				busNumLike = destAbbr + '%';

			}

			if (noOfLeaves != null && !noOfLeaves.trim().isEmpty()) {

				query2 = "select count( is_fixed_time) as busCount  FROM public.nt_m_timetable_generator_det \r\n"
						+ "WHERE is_fixed_time=? and group_no=?  and trip_type=?\r\n"
						+ "and generator_ref_no=? and bus_num is not null and bus_num !='';";
			}

			else {
				query2 = "select count( is_fixed_time) as busCount  FROM public.nt_m_timetable_generator_det "
						+ "WHERE is_fixed_time=? and group_no=? and trip_type=? and generator_ref_no=? and  bus_num like "
						+ "'" + busNumLike + "' and bus_num is not null and bus_num !=''";

			}

			ps = con.prepareStatement(query2);
			ps.setString(1, fixType);
			ps.setString(2, groupNo);
			ps.setString(3, tripType);
			ps.setString(4, refNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				count = rs.getInt("busCount");
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return count;
	}

	@Override
	public boolean checkLeaveInTimeTableDet(String refNo, String groupNo, String tripType) {

		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;
		boolean leave = false;
		int leaveNo = 0;
		try {
			con = ConnectionManager.getConnection();

			String sql = "select no_of_buses_on_leave from public.nt_m_timetable_generator where generator_ref_no=? and group_no=? and  trip_type=? ;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, refNo);
			stmt.setString(2, groupNo);
			stmt.setString(3, tripType);

			rs = stmt.executeQuery();

			while (rs.next()) {
				leaveNo = rs.getInt("no_of_buses_on_leave");
				if (leaveNo > 0) {

					leave = true;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
			ConnectionManager.close(stmt1);
		}
		return leave;
	}

	@Override
	public List<RouteScheduleDTO> getBusNoListForRotationWithCoupleTwoNew(String routeNo, String refNo, String groupNo,
			String tripType, String busCategory) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<RouteScheduleDTO> busNoList = new ArrayList<RouteScheduleDTO>();
		int privateBuses = 0;
		String originAbbr = null;
		String destAbbr = null;
		try {
			con = ConnectionManager.getConnection();

			// get abbreviation for origin and destination start
			String abbrQuery = "SELECT rc_abbriviation_ltr_start, rc_abbriviation_ltr_end FROM public.nt_t_route_creator "
					+ "where rc_route_no=? and rc_bus_type=? ";

			ps = con.prepareStatement(abbrQuery);
			ps.setString(1, routeNo);
			ps.setString(2, busCategory);

			rs = ps.executeQuery();

			while (rs.next()) {
				originAbbr = rs.getString("rc_abbriviation_ltr_start");
				destAbbr = rs.getString("rc_abbriviation_ltr_end");
			}

			try {
				if (rs != null)
					rs.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				if (ps != null)
					ps.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			String originPVTQuery = "select sum(a.no_of_pvt_bus) as no_of_pvt_bus from public.nt_t_trips_generator_det02 a "
					+ "left join public.nt_m_trips_generator b " + "on a.trips_ref_code=b.tg_trips_ref_code "
					+ "where b.tg_generator_ref_no=? " + "and a.group_no=? and trip_type = ?";

			ps = con.prepareStatement(originPVTQuery);
			ps.setString(1, refNo);
			ps.setString(2, groupNo);
			ps.setString(3, tripType);
			rs = ps.executeQuery();

			while (rs.next()) {
				privateBuses = rs.getInt("no_of_pvt_bus");
			}

			try {
				if (rs != null)
					rs.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				if (ps != null)
					ps.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			// get private buses end

			/** create temp origin and destination bus numbers with leave buses start **/
			busNoList = new ArrayList<RouteScheduleDTO>();
			for (int i = 0; i < privateBuses; i++) {
				RouteScheduleDTO dto = new RouteScheduleDTO();
				int count = i + 1;
				dto.setTripId(Integer.toString(count));
				if (tripType.equals("O")) {
					dto.setBusNo(originAbbr + count);
				} else {
					dto.setBusNo(destAbbr + count);
				}
				busNoList.add(dto);
			}
			/** create temp origin and destination bus numbers with leave buses end **/

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return busNoList;
	}

	@Override
	public List<String> getLeaveListPerSide(String refNo, String tripType) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String swap = null;
		String selectedCreatedDate = null;
		List<String> leaveList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String select = "SELECT leave_position FROM public.nt_t_route_schedule_generator_det02 x\r\n"
					+ "WHERE generator_ref_no = ? and trip_type = ?";
			ps = con.prepareStatement(select);

			ps.setString(1, refNo);
			ps.setString(2, tripType);

			rs = ps.executeQuery();

			while (rs.next()) {
				leaveList.add(rs.getString("leave_position"));
			}

			ConnectionManager.close(rs);
			ConnectionManager.close(ps);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return leaveList;
	}

	@Override
	public int getMasterSeq(String routeNo, String busCategory, String generatedRefNo, String group, String tripType) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		int masSeq = 0;
		try {
			con = ConnectionManager.getConnection();

			String selectQuery = "SELECT * from public.nt_h_route_schedule_generator WHERE rs_generator_ref_no=? and rs_route_no=? and rs_bus_category_code=? and rs_trip_type=? and rs_group_no=? order by rs_seq desc limit 1";
			stmt = con.prepareStatement(selectQuery);

			stmt.setString(1, generatedRefNo);
			stmt.setString(2, routeNo);
			stmt.setString(3, busCategory);
			stmt.setString(4, tripType);
			stmt.setString(5, group);

			rs = stmt.executeQuery();

			while (rs.next()) {

				masSeq = rs.getInt("rs_seq");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return masSeq;
	}

	@Override
	public boolean updateRouteScheduleDetOneTableData(List<RouteScheduleDTO> leaveForRouteList, String loginUser, String referenceNo, String generatedRef, String tripType, int days) {
	    Connection con = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    java.util.Date date = new java.util.Date();
	    Timestamp timestamp = new Timestamp(date.getTime());

	    try {
	        con = ConnectionManager.getConnection();
	        String updateQuery = "INSERT INTO public.nt_t_route_schedule_generator_det01 "
	                + "(seq, route_ref_no, generator_ref_no, leave_trip_day, leave_trip_id, leave_bus, created_by, created_date, trip_type) "
	                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	        try {
	            ps = con.prepareStatement(updateQuery);

	            for (RouteScheduleDTO dto1 : leaveForRouteList) {
	            	for(int count = 1; count <= days; count++) {
	            		long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_route_schedule_generator_det01");
	 	                ps.setLong(1, seqNo);
	 	                ps.setString(2, referenceNo);
	 	                ps.setString(3, generatedRef);
	 	                ps.setInt(4, count); 
	 	                ps.setString(5, dto1.getTripId());
	 	                ps.setString(6, dto1.getLeaveBusNoList().get(count - 1));
	 	                ps.setString(7, loginUser);
	 	                ps.setTimestamp(8, timestamp);
	 	                ps.setString(9, tripType);
	 	                ps.addBatch();
	            	}             
	            }
	            ps.executeBatch();
	            con.commit();
	        } catch (SQLException e) {
	            con.rollback();
	            e.printStackTrace();
	            return false;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    } finally {
	        ConnectionManager.close(rs);
	        ConnectionManager.close(ps);
	        ConnectionManager.close(con);
	    }
	    
	    return true;
	}

	@Override
	public boolean updateRouteScheduleDetOneTableDataTwoDay(List<RouteScheduleDTO> leaveForRouteList, String loginUser, String referenceNo, String generatedRef, String tripType, int days) {
	    Connection con = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    java.util.Date date = new java.util.Date();
	    Timestamp timestamp = new Timestamp(date.getTime());

	    try {
	        con = ConnectionManager.getConnection();
	        String updateQuery = "INSERT INTO public.nt_t_route_schedule_generator_det01_two_day "
	                + "(seq, route_ref_no, generator_ref_no, leave_trip_day, leave_trip_id, leave_bus, created_by, created_date, trip_type) "
	                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	        try {
	            ps = con.prepareStatement(updateQuery);

	            for (RouteScheduleDTO dto : leaveForRouteList) {
	            	for(int count = 1; count <= days; count++) {
	            		long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_route_schedule_generator_det01");
	 	                ps.setLong(1, seqNo);
	 	                ps.setString(2, referenceNo);
	 	                ps.setString(3, generatedRef);
	 	                ps.setInt(4, count); 
	 	                ps.setString(5, dto.getTripId());
	 	                ps.setString(6, dto.getLeaveBusNoList().get(count - 1));
	 	                ps.setString(7, loginUser);
	 	                ps.setTimestamp(8, timestamp);
	 	                ps.setString(9, tripType);
	 	                ps.addBatch();
	            	}     
	            }

	            ps.executeBatch();
	            con.commit();
	        } catch (SQLException e) {
	            con.rollback();
	            e.printStackTrace();
	            return false;
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    } finally {
	        ConnectionManager.close(rs);
	        ConnectionManager.close(ps);
	        ConnectionManager.close(con);
	    }
	    
	    return true;
	}
	
	// To add time table data to terminal use : added by dhananjika.d (30/04/2024)
	
	@Override
	public void insertTerminalData(List<RouteScheduleDTO> busNoList, List<RouteScheduleDTO> twoDayBusNoList, List<String> startTime, String route, String service, String terminal) throws SQLException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int nextId = 0;
		int time = 0;
		String query = "INSERT INTO public.nt_t_terminal_time_table_schedule (tr_day, tr_bus_no, tr_trip_id, tr_schedule_time_table_time,tr_route_no,tr_service_type,tr_terminal_code) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?);";
		try {
			con = ConnectionManager.getConnection();
			ps = con.prepareStatement(query);
			for (int i = 0; i < busNoList.size(); i++) {
				ps.setInt(1, busNoList.get(i).getDayNo());
				ps.setString(2, busNoList.get(i).getBusNo());
				ps.setInt(3, Integer.valueOf(busNoList.get(i).getTripId()));
				int currentTripId = Integer.valueOf(busNoList.get(i).getTripId());
				if(i + 1 != busNoList.size()) {
					nextId = Integer.valueOf(busNoList.get(i + 1).getTripId());
				}	
				if (currentTripId != nextId) {
					time++;
					
					if (time == startTime.size())
						time = 0;
			    }		
				ps.setString(4, startTime.get(time));
				ps.setString(5, route);
				ps.setString(6, service);
				ps.setString(7, terminal);
				ps.addBatch();
			}
			
			if(!twoDayBusNoList.isEmpty()) {
				for (int c = 0; c < twoDayBusNoList.size(); c++) {
					ps.setInt(1, twoDayBusNoList.get(c).getDayNo());
					ps.setString(2, twoDayBusNoList.get(c).getBusNo());
					ps.setInt(3, Integer.valueOf(busNoList.get(c).getTripId()));
					int currentTripId = Integer.valueOf(busNoList.get(c).getTripId());
					if(c + 1 != busNoList.size()) {
						nextId = Integer.valueOf(busNoList.get(c + 1).getTripId());
					}	
					if (currentTripId != nextId) {
						time++;
						
						if (time == startTime.size())
							time = 0;
				    }
					ps.setString(4, startTime.get(time));
					ps.setString(5, route);
					ps.setString(6, service);
					ps.setString(7, terminal);
					ps.addBatch();
				}
			}
			
			ps.executeBatch();
			con.commit();

		} catch (SQLException e) {
			con.rollback();
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
	}
	
	@Override
	public List<String> getTimes(String refNo,String trip) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String code = null;
		List<String> timeList = new ArrayList<>();
		try {
			con = ConnectionManager.getConnection();
			String query2 = null;
			
			if(trip.equals("O")) {
				query2 = "select start_time from public.panel_generator_origin_trip_details "
						+ "where ref_no = ? and abbreviation NOT LIKE 'SLTB%' AND abbreviation NOT LIKE 'ETC%' ORDER by id asc";
			}else if(trip.equals("D")) {
				query2 = "select start_time from public.panel_generator_destination_trip_details "
						+ "where ref_no = ? and abbreviation NOT LIKE 'SLTB%' AND abbreviation NOT LIKE 'ETC%' ORDER by id asc";
			}
			

			ps = con.prepareStatement(query2);
			ps.setString(1, refNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				timeList.add(rs.getString("start_time"));

			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return timeList;
	}
	
	@Override
	public List<String> getEndTimes(String refNo,String trip) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String code = null;
		List<String> timeList = new ArrayList<>();
		try {
			con = ConnectionManager.getConnection();
			String query2 = null;
			
			if(trip.equals("O")) {
				query2 = "select end_time from public.panel_generator_origin_trip_details "
						+ "where ref_no = ? and abbreviation NOT LIKE 'SLTB%' AND abbreviation NOT LIKE 'ETC%' ORDER by id asc";
			}else if(trip.equals("D")) {
				query2 = "select end_time from public.panel_generator_destination_trip_details "
						+ "where ref_no = ? and abbreviation NOT LIKE 'SLTB%' AND abbreviation NOT LIKE 'ETC%' ORDER by id asc";
			}
			

			ps = con.prepareStatement(query2);
			ps.setString(1, refNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				timeList.add(rs.getString("end_time"));

			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return timeList;
	}
	
	@Override
	public String getTerminalCode(String route, String service) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String code = null;
		try {
			con = ConnectionManager.getConnection();

			String query2 = "select br_station_code from public.nt_m_block_route "
					+ "inner join public.nt_t_block_route_det on nt_m_block_route.br_seq = nt_t_block_route_det.bd_block_route_seq "
					+ "where nt_t_block_route_det.bd_route_no = ? and nt_t_block_route_det.bd_service_type_code = ?";

			ps = con.prepareStatement(query2);
			ps.setString(1, route);
			ps.setString(2, service);
			rs = ps.executeQuery();

			while (rs.next()) {
				code = rs.getString("br_station_code");
			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return code;
	}

	
}
