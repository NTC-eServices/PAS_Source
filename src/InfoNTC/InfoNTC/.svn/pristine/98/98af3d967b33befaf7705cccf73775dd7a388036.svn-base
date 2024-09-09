package lk.informatics.ntc.model.service;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lk.informatics.ntc.model.dto.MidpointUIDTO;
import lk.informatics.ntc.model.dto.RouteCreationDTO;

import lk.informatics.ntc.model.dto.RouteTypeDTO;
import lk.informatics.ntc.model.dto.StationDetailsDTO;
import lk.informatics.ntc.model.dto.VehicleInspectionDTO;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.roc.utils.common.Utils;

public class RouteCreatorServiceImpl implements RouteCreatorService, Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public List<StationDetailsDTO> getAllStations() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<StationDetailsDTO> data = new ArrayList<StationDetailsDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT seq, code, midpoint_name, midpoint_name_sin, midpoint_name_tamil, status FROM public.nt_r_station WHERE status='A' ";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				StationDetailsDTO p = new StationDetailsDTO();
				p.setStationCode(rs.getString("code"));
				p.setStationNameEn(rs.getString("midpoint_name"));
				p.setStationNameSin(rs.getString("midpoint_name_sin"));
				p.setStationNameTam(rs.getString("midpoint_name_tamil"));
				p.setStatus(rs.getString("status"));
				data.add(p);

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
	public List<RouteCreationDTO> getAllStationsForRouteCreaterEdit(String routeNo, String busType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<RouteCreationDTO> data = new ArrayList<RouteCreationDTO>();
		try {

			con = ConnectionManager.getConnection();

			/*
			 * String query =
			 * "select a.mp_midpoint_code ,a.mp_time_taken,b.midpoint_name\r\n" +
			 * "from public.nt_m_route_midpoint a\r\n" +
			 * "inner join public.nt_r_station b on b.code=a.mp_midpoint_code\r\n" +
			 * "where a.mp_route_no = ?\r\n" + "and a.mp_bus_type=?";
			 */
			
			String query = "select a.rs_station ,b.midpoint_name,b.code "
					+ "from public.nt_m_route_station a "
					+ "inner join public.nt_r_station b on b.code=a.rs_station "
					+ "where b.status='A' and a.rs_route_no= ?";
			
			ps = con.prepareStatement(query);
			ps.setString(1, routeNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				RouteCreationDTO p = new RouteCreationDTO();
				p.setStationCode(rs.getString("rs_station"));
				p.setStaionName(rs.getString("midpoint_name"));
				data.add(p);

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
	public List<VehicleInspectionDTO> getAllServiceTypes() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<VehicleInspectionDTO> returnList = new ArrayList<VehicleInspectionDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code,description" + " FROM nt_r_service_types" + " WHERE active = 'A'"
					+ " order by description";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				VehicleInspectionDTO vehicleDTO = new VehicleInspectionDTO();
				vehicleDTO.setServiceTypeCode(rs.getString("code"));
				vehicleDTO.setServiceTypeDescription(rs.getString("description"));
				returnList.add(vehicleDTO);
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
	public List<RouteCreationDTO> getAllRoutes() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<RouteCreationDTO> data = new ArrayList<RouteCreationDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT seqno, rou_description, rou_number, rou_service_origine, rou_service_destination, rou_via, rou_distance FROM public.nt_r_route WHERE active != 'I'order by rou_number asc ";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				RouteCreationDTO p = new RouteCreationDTO();
				p.setRouteNo(rs.getString("rou_number"));
				p.setStartFrom(rs.getString("rou_service_origine"));
				p.setEndAt(rs.getString("rou_service_destination"));
				p.setLength(rs.getBigDecimal("rou_distance"));
				p.setRouteVia(rs.getString("rou_via"));
				data.add(p);

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

	/* Assign Station to routes */
	@Override
	public List<RouteCreationDTO> getRouteDetails(RouteCreationDTO stationToRoute) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String WHERE_SQL = "";
		List<RouteCreationDTO> RouteData = new ArrayList<RouteCreationDTO>();

		if (stationToRoute.getRouteNo() != null && !stationToRoute.getRouteNo().trim().isEmpty()) {

			WHERE_SQL = "Where rc_route_no =" + "'" + stationToRoute.getRouteNo() + "'";

		} else {
			WHERE_SQL = "";

		}
		try {

			con = ConnectionManager.getConnection();

			String query = "select rc_route_no,rc_strat_from,rc_end_at,rc_travel_time,rc_length "
					+ "from public.nt_t_route_creator " + WHERE_SQL;

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			RouteCreationDTO p;

			while (rs.next()) {
				p = new RouteCreationDTO();
				p.setRouteNo(rs.getString("rc_route_no"));
				p.setStartFrom(rs.getString("rc_strat_from"));
				p.setEndAt(rs.getString("rc_end_at"));
				p.setLength(rs.getBigDecimal("rc_length"));
				p.setTravelTimeString(rs.getString("rc_travel_time"));
				RouteData.add(p);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return RouteData;
	}

	@Override
	public StationDetailsDTO getStationsInSinAndTamil(String string) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StationDetailsDTO RouteData = new StationDetailsDTO();

		try {

			con = ConnectionManager.getConnection();

			String query = "select midpoint_name_sin,midpoint_name_tamil from public.nt_r_station\r\n" + "where code=?";

			ps = con.prepareStatement(query);
			ps.setString(1, string);
			rs = ps.executeQuery();

			while (rs.next()) {
				StationDetailsDTO p = new StationDetailsDTO();
				p.setStationNameSin(rs.getString("midpoint_name_sin"));
				p.setStationNameTam(rs.getString("midpoint_name_tamil"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return RouteData;
	}

	@Override
	public List<RouteTypeDTO> getAllRouteTypes() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<RouteTypeDTO> data = new ArrayList<RouteTypeDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT code, description, description_si, description_ta, active FROM public.nt_r_route_type WHERE active='A' ";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				RouteTypeDTO p = new RouteTypeDTO();
				p.setRouteTypeCode(rs.getString("code"));
				p.setRouteTypeNameEn(rs.getString("description"));
				p.setRouteTypeNameSin(rs.getString("description_si"));
				p.setRouteTypeNameTam(rs.getString("description_ta"));
				p.setStatus(rs.getString("active"));
				data.add(p);

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
	public boolean saveRouteDetails(RouteCreationDTO routeCreationDTO, List<MidpointUIDTO> midPoints,
			List<MidpointUIDTO> midPointsSwap, String user) {

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		DateFormat timeFormat = new SimpleDateFormat("HH:mm");

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		int rs = 0;
		try {

			con = ConnectionManager.getConnection();
			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_route_creator");
			String query = "INSERT INTO public.nt_t_route_creator "
					+ "(seq, rc_route_no, rc_bus_type, rc_travel_time, rc_driver_rest_time, rc_bus_speed, rc_strat_from, rc_end_at, rc_abbriviation_ltr_start, rc_abbriviation_ltr_end, rc_length, rc_status, rc_created_by, rc_created_date) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?);";

			ps = con.prepareStatement(query);
			ps.setLong(1, seqNo);
			ps.setString(2, routeCreationDTO.getRouteNo());

			ps.setString(3, routeCreationDTO.getBusTypeStr());

			ps.setString(4, routeCreationDTO.getTimetaken());

			if (routeCreationDTO.getDriverRestTime() != null) {
				ps.setString(5, timeFormat.format(routeCreationDTO.getDriverRestTime()));
			} else {
				ps.setNull(5, Types.VARCHAR);
			}
			ps.setBigDecimal(6, routeCreationDTO.getBusSpeed());
			ps.setString(7, routeCreationDTO.getStartFrom());
			ps.setString(8, routeCreationDTO.getEndAt());
			ps.setString(9, routeCreationDTO.getAbbreviationStart());
			ps.setString(10, routeCreationDTO.getAbbreviationEnd());
			ps.setBigDecimal(11, routeCreationDTO.getLength());
			ps.setString(12, routeCreationDTO.getStatus());
			ps.setString(13, user);
			ps.setTimestamp(14, timestamp);

			rs = ps.executeUpdate();

			// insert MidPoints
			if (midPoints.size() > 0) {
				for (MidpointUIDTO md : midPoints) {
					if (md.getStationCode() != null && !md.getStationCode().equals("") && md.getTimeTaken() != null) {
						long seqNo2 = Utils.getNextValBySeqName(con, "seq_nt_m_route_midpoint");
						String query2 = "INSERT INTO public.nt_m_route_midpoint "
								+ "(seq, mp_route_no, mp_start_from, mp_end_at, mp_created_by, mp_created_date,mp_time_taken,mp_midpoint_code,mp_bus_type) "
								+ "VALUES(?,?,?,?,?,?,?,?,?);";
						ps2 = con.prepareStatement(query2);
						ps2.setLong(1, seqNo2);
						ps2.setString(2, routeCreationDTO.getRouteNo());
						ps2.setString(3, routeCreationDTO.getStartFrom());
						ps2.setString(4, routeCreationDTO.getEndAt());
						ps2.setString(5, user);
						ps2.setTimestamp(6, timestamp);
						ps2.setString(7, timeFormat.format(md.getTimeTaken()));
						ps2.setString(8, md.getStationCode());
						ps2.setString(9, routeCreationDTO.getBusTypeStr());
						ps2.executeUpdate();
						try {
							if (ps2 != null)
								ps2.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}

			// insert MidPoints Reverse
			if (midPointsSwap.size() > 0) {
				for (MidpointUIDTO md : midPointsSwap) {
					if (md.getStationCode() != null && !md.getStationCode().equals("") && md.getTimeTaken() != null) {
						long seqNo3 = Utils.getNextValBySeqName(con, "seq_nt_m_route_midpoint_reverse");
						String query3 = "INSERT INTO public.nt_m_route_midpoint_reverse "
								+ "(seq, mp_route_no, mp_start_from, mp_end_at, mp_created_by, mp_created_date,mp_time_taken,mp_midpoint_code,mp_bus_type) "
								+ "VALUES(?,?,?,?,?,?,?,?,?);";
						ps3 = con.prepareStatement(query3);
						ps3.setLong(1, seqNo3);
						ps3.setString(2, routeCreationDTO.getRouteNo());
						ps3.setString(3, routeCreationDTO.getStartFrom());
						ps3.setString(4, routeCreationDTO.getEndAt());
						ps3.setString(5, user);
						ps3.setTimestamp(6, timestamp);
						ps3.setString(7, timeFormat.format(md.getTimeTaken()));
						ps3.setString(8, md.getStationCode());
						ps3.setString(9, routeCreationDTO.getBusTypeStr());
						ps3.executeUpdate();
						try {
							if (ps3 != null)
								ps3.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(ps3);
			ConnectionManager.close(con);
		}
		if (rs > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<RouteCreationDTO> getRouteDetailsFromRouteNo(String routeNo, String status) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<RouteCreationDTO> data = new ArrayList<RouteCreationDTO>();
		String statusCode = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT seq, rc_route_no, rc_bus_type, rc_travel_time, rc_driver_rest_time, rc_bus_speed, \r\n"
					+ "rc_strat_from, rc_end_at, rc_abbriviation_ltr_start, rc_abbriviation_ltr_end, rc_length, rc_status ,rc_bus_type\r\n"
					+ ",b.description\r\n" + "FROM public.nt_t_route_creator\r\n"
					+ "inner join public.nt_r_service_types b on b.code=public.nt_t_route_creator.rc_bus_type ";
			boolean whereAdded = false;
			if (routeNo != null && !routeNo.equalsIgnoreCase("ALL")) {
				query = query + " WHERE rc_route_no ='" + routeNo + "' ";
				whereAdded = true;
			}
			if (status != null && !status.equals("")) {
				if (status.equalsIgnoreCase("ACTIVE")) {

					statusCode = "A";

				} else if (status.equalsIgnoreCase("INACTIVE")) {

					statusCode = "I";
				} else {
					statusCode = status;
				}
				if (whereAdded) {
					query = query + " and rc_status ='" + statusCode + "' ";
				} else {
					query = query + " WHERE rc_status ='" + statusCode + "' ";
					whereAdded = true;
				}
			}
			query = query + " order by rc_route_no; ";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				RouteCreationDTO p = new RouteCreationDTO();
				p.setRouteNo(rs.getString("rc_route_no"));

				String bustype = rs.getString("rc_bus_type");
				if (bustype != null) {
					p.setBusTypeStr(bustype);
				}
				if (rs.getString("rc_travel_time") != null) {
					Date date1 = new SimpleDateFormat("HH:mm").parse(rs.getString("rc_travel_time"));
					p.setTravelTime(date1);
				} else {
					p.setTravelTime(null);
				}
				if (rs.getString("rc_driver_rest_time") != null) {
					Date date2 = new SimpleDateFormat("HH:mm").parse(rs.getString("rc_driver_rest_time"));
					p.setDriverRestTime(date2);
				} else {
					p.setDriverRestTime(null);
				}

				p.setBusSpeed(rs.getBigDecimal("rc_bus_speed"));
				p.setStartFrom(rs.getString("rc_strat_from"));
				p.setEndAt(rs.getString("rc_end_at"));
				p.setAbbreviationStart(rs.getString("rc_abbriviation_ltr_start"));
				p.setAbbreviationEnd(rs.getString("rc_abbriviation_ltr_end"));
				p.setLength(rs.getBigDecimal("rc_length"));
				p.setStatus(rs.getString("rc_status"));

				p.setMidPoints(getMidPointsFromRouteNo(rs.getString("rc_route_no")));
				p.setMidPointsSwap(getMidPointsReverseFromRouteNo(rs.getString("rc_route_no")));
				p.setBusTypeStr(rs.getString("description"));
				data.add(p);
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

	public LinkedList<MidpointUIDTO> getMidPointsFromRouteNo(String routeNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		LinkedList<MidpointUIDTO> midpoints = new LinkedList<MidpointUIDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT seq, mp_route_no, mp_start_from, mp_end_at, mp_time_taken, mp_midpoint_code "
					+ "FROM public.nt_m_route_midpoint " + "WHERE mp_route_no=? order by seq;";

			ps = con.prepareStatement(query);
			ps.setString(1, routeNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				MidpointUIDTO m = new MidpointUIDTO();
				m.setSeq(rs.getLong("seq"));
				m.setStationCode(rs.getString("mp_midpoint_code"));
				Date date1 = new SimpleDateFormat("HH:mm").parse(rs.getString("mp_time_taken"));
				m.setTimeTaken(date1);
				midpoints.add(m);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return midpoints;
	}

	public LinkedList<MidpointUIDTO> getMidPointsReverseFromRouteNo(String routeNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		LinkedList<MidpointUIDTO> midpointsReverse = new LinkedList<MidpointUIDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT seq, mp_route_no, mp_start_from, mp_end_at, mp_time_taken, mp_midpoint_code "
					+ "FROM public.nt_m_route_midpoint_reverse " + "WHERE mp_route_no=? order by seq;";

			ps = con.prepareStatement(query);
			ps.setString(1, routeNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				MidpointUIDTO m = new MidpointUIDTO();
				m.setSeq(rs.getLong("seq"));
				m.setStationCode(rs.getString("mp_midpoint_code"));
				Date date1 = new SimpleDateFormat("HH:mm").parse(rs.getString("mp_time_taken"));
				m.setTimeTaken(date1);
				midpointsReverse.add(m);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return midpointsReverse;
	}

	@Override
	public boolean updateRouteDetails(RouteCreationDTO routeCreationDTO, List<MidpointUIDTO> midPoints,
			List<MidpointUIDTO> midPointsSwap, String user) {

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		DateFormat timeFormat = new SimpleDateFormat("HH:mm");

		Connection con = null;
		PreparedStatement ps = null;
		int rs = 0;
		String busType = null;
		try {

			con = ConnectionManager.getConnection();
			String query = "UPDATE public.nt_t_route_creator "
					+ "SET rc_bus_type=?, rc_travel_time=?, rc_driver_rest_time=?, rc_bus_speed=?, rc_strat_from=?, rc_end_at=?, rc_abbriviation_ltr_start=?, rc_abbriviation_ltr_end=?, rc_length=?, rc_status=?, rc_modified_by=?, rc_modified_date=? "
					+ "WHERE rc_route_no=?; ";

			ps = con.prepareStatement(query);

			if (routeCreationDTO.getBusTypeStr().equalsIgnoreCase("NORMAL")) {
				busType = "001";
			} else if (routeCreationDTO.getBusTypeStr().equalsIgnoreCase("LUXURY")) {
				busType = "002";
			} else if (routeCreationDTO.getBusTypeStr().equalsIgnoreCase("SUPER LUXURY")) {
				busType = "003";
			} else if (routeCreationDTO.getBusTypeStr().equalsIgnoreCase("SEMI-LUXURY")) {
				busType = "004";
			} else if (routeCreationDTO.getBusTypeStr().equalsIgnoreCase("EXPRESSWAY BUS")) {
				busType = "EB";
			} else {
				busType = routeCreationDTO.getBusTypeStr();
			}
			ps.setString(1, busType);
			if (routeCreationDTO.getTravelTime() != null) {
				ps.setString(2, timeFormat.format(routeCreationDTO.getTravelTime()));
			} else {
				ps.setNull(2, Types.VARCHAR);
			}
			if (routeCreationDTO.getDriverRestTime() != null) {
				ps.setString(3, timeFormat.format(routeCreationDTO.getDriverRestTime()));
			} else {
				ps.setNull(3, Types.VARCHAR);
			}
			ps.setBigDecimal(4, routeCreationDTO.getBusSpeed());
			ps.setString(5, routeCreationDTO.getStartFrom());
			ps.setString(6, routeCreationDTO.getEndAt());
			ps.setString(7, routeCreationDTO.getAbbreviationStart());
			ps.setString(8, routeCreationDTO.getAbbreviationEnd());
			ps.setBigDecimal(9, routeCreationDTO.getLength());
			ps.setString(10, routeCreationDTO.getStatus());
			ps.setString(11, user);
			ps.setTimestamp(12, timestamp);
			ps.setString(13, routeCreationDTO.getRouteNo());

			rs = ps.executeUpdate();

			boolean success1 = updateMidPoints(routeCreationDTO, midPoints, con, user, busType);
			boolean success2 = updateMidPointsReverse(routeCreationDTO, midPointsSwap, con, user, busType);

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		if (rs > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int generateStageValue(String routeNoEnterd) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;

		int strStage = 0;

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT rs_stage FROM public.nt_m_route_station WHERE rs_route_no=?\r\n"
					+ "and rs_stage IS NOT NULL ORDER BY rs_stage desc LIMIT 1 ";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, routeNoEnterd);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("rs_stage");

			}

			if (result != null) {

				strStage = Integer.valueOf(result) + 1;

			} else
				strStage = 0;

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return strStage;

	}

	private boolean updateMidPoints(RouteCreationDTO routeCreationDTO, List<MidpointUIDTO> updated_midPoints,
			Connection con, String user, String busType) {
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		DateFormat timeFormat = new SimpleDateFormat("HH:mm");

		List<MidpointUIDTO> old_midPoints = getMidPointsFromRouteNoAndBusType(routeCreationDTO.getRouteNo(), busType);
		if (updated_midPoints.size() > 0) {
			if (old_midPoints.size() > 0) {

				for (MidpointUIDTO old_midPoint : old_midPoints) {
					boolean exists = false;
					MidpointUIDTO existing_midPoint = null;
					for (MidpointUIDTO updated_midPoint : updated_midPoints) {
						if (old_midPoint.getSeq() == updated_midPoint.getSeq()) {
							exists = true;
							existing_midPoint = updated_midPoint;
							break;
						}
					}
					if (exists) {
						if (existing_midPoint != null) {
							// update
							PreparedStatement ps = null;
							try {
								String query = "UPDATE public.nt_m_route_midpoint "
										+ "SET mp_modified_by =?, mp_modified_date=?, mp_time_taken=?, mp_midpoint_code=? "
										+ "WHERE seq=?";

								ps = con.prepareStatement(query);

								ps.setString(1, user);
								ps.setTimestamp(2, timestamp);
								ps.setString(3, timeFormat.format(existing_midPoint.getTimeTaken()));
								ps.setString(4, existing_midPoint.getStationCode());
								ps.setLong(5, existing_midPoint.getSeq());
								ps.executeUpdate();
							} catch (SQLException e) {
								e.printStackTrace();
								return false;
							} finally {
								ConnectionManager.close(ps);
							}
						}
					} else {
						// delete
						PreparedStatement ps = null;
						try {
							String query = "DELETE FROM public.nt_m_route_midpoint WHERE seq=?";

							ps = con.prepareStatement(query);
							ps.setLong(1, old_midPoint.getSeq());
							ps.executeUpdate();
						} catch (SQLException e) {
							e.printStackTrace();
							return false;
						} finally {
							ConnectionManager.close(ps);
						}
					}
				}
				for (MidpointUIDTO updated_midPoint : updated_midPoints) {
					boolean fresh = true;
					for (MidpointUIDTO old_midPoint : old_midPoints) {
						if (old_midPoint.getSeq() == updated_midPoint.getSeq()) {
							fresh = false;
							break;
						}
					}
					if (fresh) {
						// insert
						PreparedStatement ps = null;
						try {
							long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_route_midpoint");
							String query = "INSERT INTO public.nt_m_route_midpoint "
									+ "(seq, mp_route_no, mp_start_from, mp_end_at, mp_created_by, mp_created_date,mp_time_taken,mp_midpoint_code,mp_bus_type) "
									+ "VALUES(?,?,?,?,?,?,?,?,?);";

							ps = con.prepareStatement(query);
							ps.setLong(1, seqNo);
							ps.setString(2, routeCreationDTO.getRouteNo());
							ps.setString(3, routeCreationDTO.getStartFrom());
							ps.setString(4, routeCreationDTO.getEndAt());
							ps.setString(5, user);
							ps.setTimestamp(6, timestamp);
							ps.setString(7, timeFormat.format(updated_midPoint.getTimeTaken()));
							ps.setString(8, updated_midPoint.getStationCode());
							ps.setString(9, busType);
							ps.executeUpdate();
						} catch (SQLException e) {
							e.printStackTrace();
							return false;
						} finally {
							ConnectionManager.close(ps);
						}
					}
				}
			} else {
				// if there are no existing midpoints insert full list of midpoints
				PreparedStatement ps = null;
				try {
					for (MidpointUIDTO md : updated_midPoints) {
						if (md.getStationCode() != null && !md.getStationCode().equals("")
								&& md.getTimeTaken() != null) {
							long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_route_midpoint");
							String query = "INSERT INTO public.nt_m_route_midpoint "
									+ "(seq, mp_route_no, mp_start_from, mp_end_at, mp_created_by, mp_created_date,mp_time_taken,mp_midpoint_code) "
									+ "VALUES(?,?,?,?,?,?,?,?);";
							ps = con.prepareStatement(query);
							ps.setLong(1, seqNo);
							ps.setString(2, routeCreationDTO.getRouteNo());
							ps.setString(3, routeCreationDTO.getStartFrom());
							ps.setString(4, routeCreationDTO.getEndAt());
							ps.setString(5, user);
							ps.setTimestamp(6, timestamp);
							ps.setString(7, timeFormat.format(md.getTimeTaken()));
							ps.setString(8, md.getStationCode());
							ps.executeUpdate();
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
					return false;
				} finally {
					ConnectionManager.close(ps);
				}
			}
		} else {
			// if midpoints are empty delete all existing midpoints for the route no
			PreparedStatement ps = null;
			try {
				String query = "DELETE FROM public.nt_m_route_midpoint WHERE mp_route_no=?";

				ps = con.prepareStatement(query);
				ps.setString(1, routeCreationDTO.getRouteNo());
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			} finally {
				ConnectionManager.close(ps);
			}

		}

		return true;
	}

	private boolean updateMidPointsReverse(RouteCreationDTO routeCreationDTO, List<MidpointUIDTO> updated_midPointsSwap,
			Connection con, String user, String busType) {
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		DateFormat timeFormat = new SimpleDateFormat("HH:mm");

		List<MidpointUIDTO> old_midPointsSwap = getMidPointsReverseFromRouteNoAndBusType(routeCreationDTO.getRouteNo(),
				busType);
		if (updated_midPointsSwap.size() > 0) {
			if (old_midPointsSwap.size() > 0) {

				for (MidpointUIDTO old_midPointSwap : old_midPointsSwap) {
					boolean exists = false;
					MidpointUIDTO existing_midPointSwap = null;
					for (MidpointUIDTO updated_midPointSwap : updated_midPointsSwap) {
						if (old_midPointSwap.getSeq() == updated_midPointSwap.getSeq()) {
							exists = true;
							existing_midPointSwap = updated_midPointSwap;
							break;
						}
					}
					if (exists) {
						// update
						if (existing_midPointSwap != null) {
							PreparedStatement ps = null;
							try {
								String query = "UPDATE public.nt_m_route_midpoint_reverse "
										+ "SET mp_modified_by =?, mp_modified_date=?, mp_time_taken=?, mp_midpoint_code=? "
										+ "WHERE seq=?;";

								ps = con.prepareStatement(query);

								ps.setString(1, user);
								ps.setTimestamp(2, timestamp);
								ps.setString(3, timeFormat.format(existing_midPointSwap.getTimeTaken()));
								ps.setString(4, existing_midPointSwap.getStationCode());
								ps.setLong(5, existing_midPointSwap.getSeq());
								ps.executeUpdate();
							} catch (SQLException e) {
								e.printStackTrace();
								return false;
							} finally {
								ConnectionManager.close(ps);
							}
						}
					} else {
						// delete
						PreparedStatement ps = null;
						try {
							String query = "DELETE FROM public.nt_m_route_midpoint_reverse WHERE seq=?";

							ps = con.prepareStatement(query);
							ps.setLong(1, old_midPointSwap.getSeq());
							ps.executeUpdate();
						} catch (SQLException e) {
							e.printStackTrace();
							return false;
						} finally {
							ConnectionManager.close(ps);
						}
					}
				}
				for (MidpointUIDTO updated_midPoint : updated_midPointsSwap) {
					boolean fresh = true;
					for (MidpointUIDTO old_midPoint : old_midPointsSwap) {
						if (old_midPoint.getSeq() == updated_midPoint.getSeq()) {
							fresh = false;
							break;
						}
					}
					if (fresh) {
						// insert
						PreparedStatement ps = null;
						try {
							long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_route_midpoint_reverse");
							String query = "INSERT INTO public.nt_m_route_midpoint_reverse "
									+ "(seq, mp_route_no, mp_start_from, mp_end_at, mp_created_by, mp_created_date,mp_time_taken,mp_midpoint_code,mp_bus_type) "
									+ "VALUES(?,?,?,?,?,?,?,?,?);";

							ps = con.prepareStatement(query);
							ps.setLong(1, seqNo);
							ps.setString(2, routeCreationDTO.getRouteNo());
							ps.setString(3, routeCreationDTO.getStartFrom());
							ps.setString(4, routeCreationDTO.getEndAt());
							ps.setString(5, user);
							ps.setTimestamp(6, timestamp);
							ps.setString(7, timeFormat.format(updated_midPoint.getTimeTaken()));
							ps.setString(8, updated_midPoint.getStationCode());
							ps.setString(9, busType);
							ps.executeUpdate();
						} catch (SQLException e) {
							e.printStackTrace();
							return false;
						} finally {
							ConnectionManager.close(ps);
						}
					}
				}
			} else {
				// if there are no existing midpoints insert full list of midpoints
				PreparedStatement ps = null;
				try {
					for (MidpointUIDTO md : updated_midPointsSwap) {
						if (md.getStationCode() != null && !md.getStationCode().equals("")
								&& md.getTimeTaken() != null) {
							long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_route_midpoint_reverse");
							String query = "INSERT INTO public.nt_m_route_midpoint_reverse "
									+ "(seq, mp_route_no, mp_start_from, mp_end_at, mp_created_by, mp_created_date,mp_time_taken,mp_midpoint_code) "
									+ "VALUES(?,?,?,?,?,?,?,?);";
							ps = con.prepareStatement(query);
							ps.setLong(1, seqNo);
							ps.setString(2, routeCreationDTO.getRouteNo());
							ps.setString(3, routeCreationDTO.getStartFrom());
							ps.setString(4, routeCreationDTO.getEndAt());
							ps.setString(5, user);
							ps.setTimestamp(6, timestamp);
							ps.setString(7, timeFormat.format(md.getTimeTaken()));
							ps.setString(8, md.getStationCode());
							ps.executeUpdate();
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
					return false;
				} finally {
					ConnectionManager.close(ps);
				}
			}
		} else {
			// if midpoints are empty delete all existing midpoints for the route no
			PreparedStatement ps = null;
			try {
				String query = "DELETE FROM public.nt_m_route_midpoint_reverse WHERE mp_route_no=?";

				ps = con.prepareStatement(query);
				ps.setString(1, routeCreationDTO.getRouteNo());
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			} finally {
				ConnectionManager.close(ps);
			}

		}

		return true;
	}

	@Override
	public List<StationDetailsDTO> getAllServiceType() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<StationDetailsDTO> serviceType = new ArrayList<StationDetailsDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM public.nt_r_service_types where active='A' ";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				StationDetailsDTO p = new StationDetailsDTO();
				p.setServiceTypeCode(rs.getString("code"));
				p.setServiceTypeDes(rs.getString("description"));

				serviceType.add(p);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return serviceType;
	}

	@Override
	public List<StationDetailsDTO> addAssignStationToRoute(StationDetailsDTO station, String routeNoEnterd,
			String loginUser, String status) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String showStageCost;

		try {
			con = ConnectionManager.getConnection();

			if (station.getStage() != null && !station.getStage().trim().isEmpty()) {
				showStageCost = station.getStage();

			} else {
				showStageCost = Integer.toString(generateStageValue(routeNoEnterd));
				station.setStage(showStageCost);
			}

			java.util.Date date = new java.util.Date();
			Timestamp timestamp = new Timestamp(date.getTime());

			long seqNo = Utils.getNextValBySeqName(con, " public.seq_nt_m_route_station");

			String sql = "INSERT INTO public.nt_m_route_station\r\n"
					+ "(seq,  rs_station, rs_stage, rs_created_by, rs_created_date, rs_modified_by, rs_modified_date, rs_status,rs_route_no,rs_service_type )\r\n"
					+ "VALUES(?,  ?, ?, ?,?, ?, ?, ?,?,?);";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);

			stmt.setString(2, station.getStationCode());
			stmt.setInt(3, Integer.parseInt(showStageCost));
			stmt.setString(4, loginUser);
			stmt.setTimestamp(5, timestamp);
			stmt.setString(6, loginUser);
			stmt.setTimestamp(7, timestamp);
			stmt.setString(8, status);
			stmt.setString(9, routeNoEnterd);
			stmt.setString(10, station.getServiceTypeCode());

			stmt.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return null;

	}

	@Override
	public List<StationDetailsDTO> showDataonGrid(String routeNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<StationDetailsDTO> data = new ArrayList<StationDetailsDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select b.rs_station,b.rs_stage,a.midpoint_name,a.midpoint_name_sin,a.midpoint_name_tamil,c.description,c.code,"
					+ "rs_is_normal,rs_is_luxuary,rs_is_semi_luxuary,rs_is_super_luxuary,rs_is_expressway "
					+ "from public.nt_m_route_station b " + "inner join public.nt_r_station a on  a.code=b.rs_station "
					+ "inner join public.nt_r_service_types c on  c.code=b. rs_service_type "
					+ "where a.status='A' and b.rs_route_no=? order by b.rs_stage";
			ps = con.prepareStatement(query);
			ps.setString(1, routeNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				StationDetailsDTO p = new StationDetailsDTO();
				p.setStationCode(rs.getString("rs_station"));
				p.setStationNameEn(rs.getString("midpoint_name"));
				p.setStationNameSin(rs.getString("midpoint_name_sin"));
				p.setStationNameTam(rs.getString("midpoint_name_tamil"));
				p.setStage(Integer.toString(rs.getInt("rs_stage")));
				p.setServiceTypeDes(rs.getString("description"));
				p.setServiceTypeCode(rs.getString("code"));
				String normal = rs.getString("rs_is_normal");
				if (normal != null && !normal.isEmpty() && !normal.trim().equalsIgnoreCase("")
						&& normal.equalsIgnoreCase("Y")) {
					p.setNormal(true);
				} else {
					p.setNormal(false);
				}
				String luxury = rs.getString("rs_is_luxuary");
				if (luxury != null && !luxury.isEmpty() && !luxury.trim().equalsIgnoreCase("")
						&& luxury.equalsIgnoreCase("Y")) {
					p.setLuxury(true);
				} else {
					p.setLuxury(false);
				}
				String semiLuxury = rs.getString("rs_is_semi_luxuary");
				if (semiLuxury != null && !semiLuxury.isEmpty() && !semiLuxury.trim().equalsIgnoreCase("")
						&& semiLuxury.equalsIgnoreCase("Y")) {
					p.setSemiLuxury(true);
				} else {
					p.setSemiLuxury(false);
				}
				String superLuxury = rs.getString("rs_is_super_luxuary");
				if (superLuxury != null && !superLuxury.isEmpty() && !superLuxury.trim().equalsIgnoreCase("")
						&& superLuxury.equalsIgnoreCase("Y")) {
					p.setSuperLuxury(true);
				} else {
					p.setSuperLuxury(false);
				}
				String express = rs.getString("rs_is_expressway");
				if (express != null && !express.isEmpty() && !express.trim().equalsIgnoreCase("")
						&& express.equalsIgnoreCase("Y")) {
					p.setExpress(true);
				} else {
					p.setExpress(false);
				}

				data.add(p);

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
	public List<RouteCreationDTO> getCreatedRouteData() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<RouteCreationDTO> routeCreationDTOList = new ArrayList<RouteCreationDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct rs_route_no,rou_description from public.nt_m_route_station a, public.nt_r_route b where a.rs_route_no=b.rou_number order by rs_route_no Asc";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				RouteCreationDTO routeCreationDTO = new RouteCreationDTO();
				routeCreationDTO.setRouteNo(rs.getString("rs_route_no"));
				routeCreationDTO.setRouteDesc(rs.getString("rou_description"));
				routeCreationDTOList.add(routeCreationDTO);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return routeCreationDTOList;
	}

	@Override
	public List<String> getStagesList(String selectedRoute) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> routeCreationDTOList = new ArrayList<String>();
		try {

			con = ConnectionManager.getConnection();
			String query = "select distinct rs_stage from public.nt_m_route_station where rs_route_no = '"
					+ selectedRoute + "' order by rs_stage asc";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				routeCreationDTOList.add(rs.getString("rs_stage"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return routeCreationDTOList;
	}

	@Override
	public List<RouteCreationDTO> getLocationList(String selectedRoute, String selectedStage, String selectedStatus) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<RouteCreationDTO> routeCreationDTOList = new ArrayList<RouteCreationDTO>();
		String orderBy = "";
		String stage = "";
		String status = "";
		try {

			con = ConnectionManager.getConnection();

			if (Integer.valueOf(selectedStage) > Integer.valueOf(selectedStatus)) {
				stage = selectedStatus;
				status = selectedStage;
				orderBy = "DESC";
			} else {
				stage = selectedStage;
				status = selectedStatus;
				orderBy = "ASC";
			}

			String query = "select a.rs_stage,a.rs_station,b.midpoint_name,b.midpoint_name_sin,b.midpoint_name_tamil, c.tfc_normal_round_fee "
					+ "from public.nt_m_route_station a, public.nt_r_station b, nt_t_fee_circle c "
					+ "where a.rs_route_no='" + selectedRoute + "' and a.rs_stage between " + stage + " and " + status
					+ " and a.rs_station=b.code and a.rs_stage=tfc_stage order by rs_stage " + orderBy;

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				RouteCreationDTO dto = new RouteCreationDTO();
				dto.setStage(rs.getString("rs_stage"));
				dto.setStation(rs.getString("rs_station"));
				dto.setStageFee(rs.getInt("tfc_normal_round_fee"));

				StationDetailsDTO stationDetailsDTO = new StationDetailsDTO();
				stationDetailsDTO.setStationNameEn(rs.getString("midpoint_name"));
				stationDetailsDTO.setStationNameSin(rs.getString("midpoint_name_sin"));
				stationDetailsDTO.setStationNameTam(rs.getString("midpoint_name_tamil"));

				dto.setStationDetailsDTO(stationDetailsDTO);

				routeCreationDTOList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return routeCreationDTOList;
	}

	@Override
	public StationDetailsDTO getStaInSinAndTamilSec(String string) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StationDetailsDTO RouteDataSec = new StationDetailsDTO();

		try {

			con = ConnectionManager.getConnection();

			String query = "select midpoint_name_sin,midpoint_name_tamil from public.nt_r_station\r\n" + "where code=?";

			ps = con.prepareStatement(query);
			ps.setString(1, string);
			rs = ps.executeQuery();

			while (rs.next()) {
				StationDetailsDTO stationsSecond = new StationDetailsDTO();
				stationsSecond.setStationNameSin(rs.getString("midpoint_name_sin"));
				stationsSecond.setStationNameTam(rs.getString("midpoint_name_tamil"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return RouteDataSec;

	}

	@Override
	public void updatedEditedData(String selectedstationCode, String oldStage, String loginUser,
			String selectedServiceType, String selectedStatus, String routeNo, String newStage,
			StationDetailsDTO stationToRouteSecondDTO) {

		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String formerStation = null;

		try {
			con = ConnectionManager.getConnection();

			java.util.Date date = new java.util.Date();
			Timestamp timestamp = new Timestamp(date.getTime());

			/** swap stages -> select former station of entered stage start **/
			if (newStage != null && !newStage.trim().isEmpty() && !newStage.trim().equalsIgnoreCase("")) {

				String sql2 = "SELECT rs_station from public.nt_m_route_station "
						+ " where rs_route_no=? and rs_stage=?";

				stmt = con.prepareStatement(sql2);

				stmt.setString(1, routeNo);
				stmt.setInt(2, Integer.parseInt(newStage));
				rs = stmt.executeQuery();

				while (rs.next()) {
					formerStation = rs.getString("rs_station");
				}

				if (stmt != null) {
					stmt.close();
				}

			}
			/** swap stages -> select former station of entered stage end **/

			String sql = "UPDATE public.nt_m_route_station "
					+ "SET  rs_station=?, rs_stage=?, rs_modified_by=?, rs_modified_date=?, rs_status=?, rs_service_type=?, rs_is_normal=?, rs_is_luxuary=?, rs_is_semi_luxuary=?,"
					+ "rs_is_super_luxuary=?, rs_is_expressway=?  "
					+ " where rs_route_no=? and rs_stage=?  and rs_station=?";

			stmt = con.prepareStatement(sql);

			if (selectedstationCode != null && !selectedstationCode.trim().isEmpty()) {
				stmt.setString(1, selectedstationCode);

			} else {

				stmt.setString(1, stationToRouteSecondDTO.getStationCode());
			}

			if (newStage != null && !newStage.trim().isEmpty()) {
				stmt.setInt(2, Integer.parseInt(newStage));
			} else {
				stmt.setInt(2, Integer.parseInt(oldStage));
			}
			stmt.setString(3, loginUser);
			stmt.setTimestamp(4, timestamp);
			if (selectedStatus != null && !selectedStatus.trim().isEmpty()) {
				stmt.setString(5, selectedStatus);
			} else {
				stmt.setString(5, stationToRouteSecondDTO.getStatus());
			}
			if (selectedServiceType != null && !selectedServiceType.trim().isEmpty()) {
				stmt.setString(6, selectedServiceType);
			} else {
				stmt.setString(6, stationToRouteSecondDTO.getServiceTypeCode());
			}
			if (stationToRouteSecondDTO.isNormal()) {
				stmt.setString(7, "Y");
			} else {
				stmt.setNull(7, java.sql.Types.VARCHAR);
			}
			if (stationToRouteSecondDTO.isLuxury()) {
				stmt.setString(8, "Y");
			} else {
				stmt.setNull(8, java.sql.Types.VARCHAR);
			}
			if (stationToRouteSecondDTO.isSemiLuxury()) {
				stmt.setString(9, "Y");
			} else {
				stmt.setNull(9, java.sql.Types.VARCHAR);
			}
			if (stationToRouteSecondDTO.isSuperLuxury()) {
				stmt.setString(10, "Y");
			} else {
				stmt.setNull(10, java.sql.Types.VARCHAR);
			}
			if (stationToRouteSecondDTO.isExpress()) {
				stmt.setString(11, "Y");
			} else {
				stmt.setNull(11, java.sql.Types.VARCHAR);
			}
			stmt.setString(12, routeNo);
			stmt.setInt(13, Integer.parseInt(oldStage));
			stmt.setString(14, stationToRouteSecondDTO.getStationCode());

			stmt.executeUpdate();

			if (stmt != null) {
				stmt.close();
			}

			/** swap stages start **/
			if (newStage != null && !newStage.trim().isEmpty() && !newStage.trim().equalsIgnoreCase("")) {

				String sql2 = "UPDATE public.nt_m_route_station "
						+ "SET rs_stage=?, rs_modified_by=?, rs_modified_date=?  "
						+ " where rs_route_no=? and rs_stage=?  and rs_station=?";

				stmt = con.prepareStatement(sql2);

				stmt.setInt(1, Integer.parseInt(oldStage));
				stmt.setString(2, loginUser);
				stmt.setTimestamp(3, timestamp);
				stmt.setString(4, routeNo);
				stmt.setInt(5, Integer.parseInt(newStage));
				stmt.setString(6, formerStation);

				stmt.executeUpdate();
				if (stmt != null) {
					stmt.close();
				}

			}
			/** swap stages end **/

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

	}

	// delete Function
	@Override
	public void updateStageSeq(String RouteNo, String Stage, String Station, StationDetailsDTO stationDTO,
			String loginUser) {

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps4 = null;
		ResultSet rs = null;
		ResultSet rs4 = null;

		String WHERE_SQL = "";
		boolean whereadded = false;

		if (Station != null && !Station.equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE rs_station  = " + "'" + Station + "' ";
			whereadded = true;
		}
		if (RouteNo != null && !RouteNo.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND rs_route_no = " + "'" + RouteNo + "' ";
			}

		}

		try {

			con = ConnectionManager.getConnection();

			ArrayList<StationDetailsDTO> reaminStageTempList = new ArrayList<>();
			String query = "Delete FROM public.nt_m_route_station " + WHERE_SQL;

			ps = con.prepareStatement(query);
			ps.executeUpdate();
			con.commit();
			try {
				if (ps != null)
					ps.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			// select remain stages
			String q1 = "Select  rs_stage,rs_station  from public.nt_m_route_station  where rs_route_no= ?; ";

			ps4 = con.prepareStatement(q1);

			ps4.setString(1, RouteNo);
			rs4 = ps4.executeQuery();
			ArrayList<StationDetailsDTO> reaminStageList = new ArrayList<>();

			while (rs4.next()) {
				StationDetailsDTO station1DTO = new StationDetailsDTO();
				station1DTO.setStage(Integer.toString(rs4.getInt("rs_stage")));
				station1DTO.setStationCode(rs4.getString("rs_station"));
				reaminStageList.add(station1DTO);
			}
			for (StationDetailsDTO dto : reaminStageList) {
				StationDetailsDTO station2DTO = new StationDetailsDTO();
				if (Integer.parseInt(dto.getStage()) > Integer.parseInt(Stage)) {
					station2DTO = dto;
					int stageInt = Integer.parseInt(dto.getStage()) - 1;

					station2DTO.setStage(Integer.toString(stageInt));
					reaminStageTempList.add(station2DTO);
					updateRemainStage(station2DTO.getStage(), RouteNo, loginUser, station2DTO.getStationCode());
				} else {
					reaminStageTempList.add(dto);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(rs4);
			ConnectionManager.close(ps4);
			ConnectionManager.close(con);

		}
	}

	// update remain stages
	public void updateRemainStage(String Stage, String RouteNo, String loginUser, String Station) {
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_route_station\r\n"
					+ "SET   rs_stage=?, rs_modified_by=?, rs_modified_date=? where rs_route_no='" + RouteNo
					+ "' and rs_station ='" + Station + "'";

			stmt = con.prepareStatement(sql);

			stmt.setInt(1, Integer.parseInt(Stage));
			stmt.setString(2, loginUser);
			stmt.setTimestamp(3, timestamp);

			stmt.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

	}
	// avoid add duplicate stations

	@Override
	public boolean checkDuplicateStation(String routeNo, String stationCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isStationAvailable = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT rs_station FROM public.nt_m_route_station  where rs_route_no=? and rs_station=? ";

			ps = con.prepareStatement(query);
			ps.setString(1, routeNo);
			ps.setString(2, stationCode);

			rs = ps.executeQuery();

			if (rs.next() == true) {
				isStationAvailable = true;
			} else {
				isStationAvailable = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isStationAvailable;
	}

	@Override
	public List<StationDetailsDTO> getAllStationsOnRoutes(String routeNo, String serviceType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;
		List<StationDetailsDTO> data = new ArrayList<StationDetailsDTO>();
		try {

			con = ConnectionManager.getConnection();
			if (serviceType.equals("001")) {
				query = "select a.rs_station ,b.midpoint_name,b.code,b.midpoint_name_sin,b.midpoint_name_tamil\r\n"
						+ "from public.nt_m_route_station a\r\n"
						+ "inner join public.nt_r_station b on b.code=a.rs_station\r\n" + "where b.status='A'\r\n"
						+ "and a.rs_route_no=? ";
			} else if (serviceType.equals("002")) {
				query = "select a.rs_station ,b.midpoint_name,b.code,b.midpoint_name_sin,b.midpoint_name_tamil\r\n"
						+ "from public.nt_m_route_station a\r\n"
						+ "inner join public.nt_r_station b on b.code=a.rs_station\r\n" + "where b.status='A'\r\n"
						+ "and a.rs_route_no=? and a.rs_is_luxuary='Y' ";

			} else if (serviceType.equals("003")) {
				query = "select a.rs_station ,b.midpoint_name,b.code,b.midpoint_name_sin,b.midpoint_name_tamil\r\n"
						+ "from public.nt_m_route_station a\r\n"
						+ "inner join public.nt_r_station b on b.code=a.rs_station\r\n" + "where b.status='A'\r\n"
						+ "and a.rs_route_no=? and a.rs_is_super_luxuary='Y' ";

			} else if (serviceType.equals("004")) {
				query = "select a.rs_station ,b.midpoint_name,b.code,b.midpoint_name_sin,b.midpoint_name_tamil\r\n"
						+ "from public.nt_m_route_station a\r\n"
						+ "inner join public.nt_r_station b on b.code=a.rs_station\r\n" + "where b.status='A'\r\n"
						+ "and a.rs_route_no=? and a.rs_is_semi_luxuary='Y' ";

			} else if (serviceType.equals("EB")) {
				query = "select a.rs_station ,b.midpoint_name,b.code,b.midpoint_name_sin,b.midpoint_name_tamil\r\n"
						+ "from public.nt_m_route_station a\r\n"
						+ "inner join public.nt_r_station b on b.code=a.rs_station\r\n" + "where b.status='A'\r\n"
						+ "and a.rs_route_no=? and a.rs_is_expressway='Y' ";

			}

			ps = con.prepareStatement(query);
			ps.setString(1, routeNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				StationDetailsDTO p = new StationDetailsDTO();
				p.setStationCode(rs.getString("code"));
				p.setStationNameEn(rs.getString("midpoint_name"));
				p.setStationNameSin(rs.getString("midpoint_name_sin"));
				p.setStationNameTam(rs.getString("midpoint_name_tamil"));
				data.add(p);

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
	public StationDetailsDTO retrieveRouteDataForRouteNum(String routeNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		StationDetailsDTO dto = new StationDetailsDTO();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT rou_service_destination,rou_service_origine FROM public.nt_r_route  where rou_number='"
					+ routeNo + "'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				dto = new StationDetailsDTO();
				dto.setOrigin(rs.getString("rou_service_origine"));
				dto.setDestination(rs.getString("rou_service_destination"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return dto;
	}

	@Override
	public String getStationDescFromStationCode(String stationCode) {
		String stationDesc = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT midpoint_name,midpoint_name_sin,midpoint_name_tamil FROM public.nt_r_station where code='"
					+ stationCode + "'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				stationDesc = rs.getString("midpoint_name");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return stationDesc;
	}

	@Override
	public List<StationDetailsDTO> insertAssignStationToRoute(List<StationDetailsDTO> stationList, String loginUser,
			String routeNum) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<StationDetailsDTO> tempStationList = new ArrayList<StationDetailsDTO>();
		try {
			con = ConnectionManager.getConnection();

			java.util.Date date = new java.util.Date();
			Timestamp timestamp = new Timestamp(date.getTime());

			for (StationDetailsDTO dto : stationList) {
				if (dto.isEdit()) {
					// insert
					long seqNo = Utils.getNextValBySeqName(con, " public.seq_nt_m_route_station");

					String sql = "INSERT INTO public.nt_m_route_station "
							+ "(seq,  rs_station, rs_stage, rs_created_by, rs_created_date, rs_status,rs_route_no,rs_service_type,rs_is_normal, "
							+ "rs_is_luxuary, rs_is_semi_luxuary, rs_is_super_luxuary, rs_is_expressway) "
							+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

					stmt = con.prepareStatement(sql);

					stmt.setLong(1, seqNo);
					stmt.setString(2, dto.getStationCode());
					stmt.setInt(3, Integer.valueOf(dto.getStage()));
					stmt.setString(4, loginUser);
					stmt.setTimestamp(5, timestamp);
					stmt.setString(6, dto.getStatus());
					stmt.setString(7, routeNum);
					stmt.setString(8, dto.getServiceTypeCode());
					if (dto.isNormal()) {
						stmt.setString(9, "Y");
					} else {
						stmt.setNull(9, java.sql.Types.VARCHAR);
					}
					if (dto.isLuxury()) {
						stmt.setString(10, "Y");
					} else {
						stmt.setNull(10, java.sql.Types.VARCHAR);
					}
					if (dto.isSemiLuxury()) {
						stmt.setString(11, "Y");
					} else {
						stmt.setNull(11, java.sql.Types.VARCHAR);
					}
					if (dto.isSuperLuxury()) {
						stmt.setString(12, "Y");
					} else {
						stmt.setNull(12, java.sql.Types.VARCHAR);
					}
					if (dto.isExpress()) {
						stmt.setString(13, "Y");
					} else {
						stmt.setNull(13, java.sql.Types.VARCHAR);
					}

					dto.setEdit(false);
					tempStationList.add(dto);

					stmt.executeUpdate();

					if (stmt != null) {
						stmt.close();
					}
				} else {
					// edit
					String sql = "UPDATE public.nt_m_route_station "
							+ " SET rs_stage=?, rs_modified_by=?, rs_modified_date=?, rs_status=?, rs_service_type=?, "
							+ "rs_is_normal=?, rs_is_luxuary=?, rs_is_semi_luxuary=?, rs_is_super_luxuary=?, rs_is_expressway=?"
							+ " where rs_route_no=? and rs_station=?";

					stmt = con.prepareStatement(sql);

					stmt.setInt(1, Integer.valueOf(dto.getStage()));
					stmt.setString(2, loginUser);
					stmt.setTimestamp(3, timestamp);
					stmt.setString(4, dto.getStatus());
					stmt.setString(5, dto.getServiceTypeCode());
					if (dto.isNormal()) {
						stmt.setString(6, "Y");
					} else {
						stmt.setNull(6, java.sql.Types.VARCHAR);
					}
					if (dto.isLuxury()) {
						stmt.setString(7, "Y");
					} else {
						stmt.setNull(7, java.sql.Types.VARCHAR);
					}
					if (dto.isSemiLuxury()) {
						stmt.setString(8, "Y");
					} else {
						stmt.setNull(8, java.sql.Types.VARCHAR);
					}
					if (dto.isSuperLuxury()) {
						stmt.setString(9, "Y");
					} else {
						stmt.setNull(9, java.sql.Types.VARCHAR);
					}
					if (dto.isExpress()) {
						stmt.setString(10, "Y");
					} else {
						stmt.setNull(10, java.sql.Types.VARCHAR);
					}

					stmt.setString(11, routeNum);
					stmt.setString(12, dto.getStationCode());

					dto.setEdit(false);
					tempStationList.add(dto);

					stmt.executeUpdate();

					if (stmt != null) {
						stmt.close();
					}
				}
			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return tempStationList;
	}

	@Override
	public boolean checkSelectedStageExist(String routeNo, String station, String newStage) {
		boolean stageExist = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT *  FROM public.nt_m_route_station where rs_route_no='" + routeNo + "' and rs_stage='"
					+ newStage + "'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				stageExist = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return stageExist;
	}

/////////////////////////////////////////////////////////////////////
	public List<RouteCreationDTO> getRouteDetailsByRouteandType(String routeNo, String serviceType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<RouteCreationDTO> RouteData = new ArrayList<RouteCreationDTO>();

		try {

			con = ConnectionManager.getConnection();

			String query = "select rc_route_no,rc_strat_from,rc_end_at,rc_length,rc_travel_time,rc_status,rc_abbriviation_ltr_start,rc_abbriviation_ltr_end "
					+ " from nt_t_route_creator " + " where rc_route_no= '" + routeNo + "' and rc_bus_type = '"
					+ serviceType + "'" + " and rc_status = 'A' ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			RouteCreationDTO p;

			while (rs.next()) {
				p = new RouteCreationDTO();
				p.setRouteNo(rs.getString("rc_route_no"));
				p.setStartFrom(rs.getString("rc_strat_from"));
				p.setEndAt(rs.getString("rc_end_at"));
				p.setLength(rs.getBigDecimal("rc_length"));
				p.setTimetaken(rs.getString("rc_travel_time"));
				p.setStatus(rs.getString("rc_status"));
				p.setAbbreviationStart(rs.getString("rc_abbriviation_ltr_start"));
				p.setAbbreviationEnd(rs.getString("rc_abbriviation_ltr_end"));
				RouteData.add(p);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return RouteData;
	}

	@Override
	public List<RouteCreationDTO> getRouteDetailsFromRouteNoForEdit(String route, String status, String busType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<RouteCreationDTO> data = new ArrayList<RouteCreationDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT a.seq, a.rc_route_no, a.rc_bus_type, a.rc_travel_time, a.rc_driver_rest_time, a.rc_bus_speed, \r\n"
					+ "a.rc_strat_from, a.rc_end_at, a.rc_abbriviation_ltr_start, a.rc_abbriviation_ltr_end, a.rc_length, a.rc_status ,a.rc_bus_type\r\n"
					+ ",b.description,d.midpoint_name ,c.mp_midpoint_code\r\n" + "FROM public.nt_t_route_creator a\r\n"
					+ "inner join public.nt_r_service_types b on b.code=a.rc_bus_type\r\n"
					+ "inner join public.nt_m_route_midpoint c on c.mp_bus_type=a.rc_bus_type\r\n"
					+ "inner join public.nt_r_station d on d.code=c.mp_midpoint_code\r\n" + "WHERE rc_route_no =?\r\n"
					+ "and rc_bus_type=? and rc_status=?\r\n" + "order by a.rc_route_no";
			ps = con.prepareStatement(query);
			ps.setString(1, route);
			ps.setString(2, busType);
			if (status.equalsIgnoreCase("ACTIVE")) {
				ps.setString(3, "A");
			} else if (status.equalsIgnoreCase("INACTIVE")) {
				ps.setString(3, "I");

			} else {
				ps.setString(3, status);

			}

			rs = ps.executeQuery();

			while (rs.next()) {
				RouteCreationDTO p = new RouteCreationDTO();

				if (rs.getString("rc_travel_time") != null) {
					Date date1 = new SimpleDateFormat("HH:mm").parse(rs.getString("rc_travel_time"));
					p.setTravelTime(date1);
				} else {
					p.setTravelTime(null);
				}

				p.setMidPoints(
						getMidPointsFromRouteNoAndBusType(rs.getString("rc_route_no"), rs.getString("rc_bus_type")));
				p.setMidPointsSwap(getMidPointsReverseFromRouteNoAndBusType(rs.getString("rc_route_no"),
						rs.getString("rc_bus_type")));
				data.add(p);
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

	public LinkedList<MidpointUIDTO> getMidPointsFromRouteNoAndBusType(String routeNo, String busType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		LinkedList<MidpointUIDTO> midpoints = new LinkedList<MidpointUIDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT seq, mp_route_no, mp_start_from, mp_end_at, mp_time_taken, mp_midpoint_code "
					+ "FROM public.nt_m_route_midpoint " + "WHERE mp_route_no=? and mp_bus_type=? order by seq;";

			ps = con.prepareStatement(query);
			ps.setString(1, routeNo);
			ps.setString(2, busType);
			rs = ps.executeQuery();

			while (rs.next()) {
				MidpointUIDTO m = new MidpointUIDTO();
				m.setSeq(rs.getLong("seq"));
				m.setStationCode(rs.getString("mp_midpoint_code"));
				Date date1 = new SimpleDateFormat("HH:mm").parse(rs.getString("mp_time_taken"));
				m.setTimeTaken(date1);
				midpoints.add(m);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return midpoints;
	}

	@Override
	public LinkedList<MidpointUIDTO> getMidPointsReverseFromRouteNoAndBusType(String routeNo, String busType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		LinkedList<MidpointUIDTO> midpointsReverse = new LinkedList<MidpointUIDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT seq, mp_route_no, mp_start_from, mp_end_at, mp_time_taken, mp_midpoint_code "
					+ "FROM public.nt_m_route_midpoint_reverse "
					+ "WHERE mp_route_no=? and mp_bus_type=? order by seq;";

			ps = con.prepareStatement(query);
			ps.setString(1, routeNo);
			ps.setString(2, busType);
			rs = ps.executeQuery();

			while (rs.next()) {
				MidpointUIDTO m = new MidpointUIDTO();
				m.setSeq(rs.getLong("seq"));
				m.setStationCode(rs.getString("mp_midpoint_code"));
				Date date1 = new SimpleDateFormat("HH:mm").parse(rs.getString("mp_time_taken"));
				m.setTimeTaken(date1);
				midpointsReverse.add(m);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return midpointsReverse;
	}

	@Override
	public boolean updateRouteDetailsForEdit(RouteCreationDTO routeCreationDTO, List<MidpointUIDTO> midPoints,
			List<MidpointUIDTO> midPointsSwap, String user) {

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		DateFormat timeFormat = new SimpleDateFormat("HH:mm");

		Connection con = null;
		PreparedStatement ps = null;
		int rs = 0;
		String busType = null;
		try {

			con = ConnectionManager.getConnection();
			String query = "UPDATE public.nt_t_route_creator "
					+ "SET  rc_travel_time=?, rc_driver_rest_time=?, rc_bus_speed=?, rc_strat_from=?, rc_end_at=?, rc_abbriviation_ltr_start=?, rc_abbriviation_ltr_end=?, rc_length=?, rc_status=?, rc_modified_by=?, rc_modified_date=? "
					+ "WHERE rc_route_no=? and  rc_bus_type=?; ";

			ps = con.prepareStatement(query);

			if (routeCreationDTO.getBusTypeStr().equalsIgnoreCase("NORMAL")) {
				busType = "001";
			} else if (routeCreationDTO.getBusTypeStr().equalsIgnoreCase("LUXURY")) {
				busType = "002";
			} else if (routeCreationDTO.getBusTypeStr().equalsIgnoreCase("SUPER LUXURY")) {
				busType = "003";
			} else if (routeCreationDTO.getBusTypeStr().equalsIgnoreCase("SEMI-LUXURY")) {
				busType = "004";
			} else if (routeCreationDTO.getBusTypeStr().equalsIgnoreCase("EXPRESSWAY BUS")) {
				busType = "EB";
			} else {
				busType = routeCreationDTO.getBusTypeStr();
			}

			if (routeCreationDTO.getTravelTime() != null) {
				ps.setString(1, timeFormat.format(routeCreationDTO.getTravelTime()));
			} else {
				ps.setNull(1, Types.VARCHAR);
			}
			if (routeCreationDTO.getDriverRestTime() != null) {
				ps.setString(2, timeFormat.format(routeCreationDTO.getDriverRestTime()));
			} else {
				ps.setNull(2, Types.VARCHAR);
			}
			ps.setBigDecimal(3, routeCreationDTO.getBusSpeed());
			ps.setString(4, routeCreationDTO.getStartFrom());
			ps.setString(5, routeCreationDTO.getEndAt());
			ps.setString(6, routeCreationDTO.getAbbreviationStart());
			ps.setString(7, routeCreationDTO.getAbbreviationEnd());
			ps.setBigDecimal(8, routeCreationDTO.getLength());
			if (routeCreationDTO.getStatus().equalsIgnoreCase("ACTIVE")) {
				ps.setString(9, "A");
			} else if (routeCreationDTO.getStatus().equalsIgnoreCase("ACTIVE")) {
				ps.setString(9, "I");
			}
			ps.setString(10, user);
			ps.setTimestamp(11, timestamp);
			ps.setString(12, routeCreationDTO.getRouteNo());
			ps.setString(13, busType);

			rs = ps.executeUpdate();

			updateMidPoints(routeCreationDTO, midPoints, con, user, busType);
			updateMidPointsReverse(routeCreationDTO, midPointsSwap, con, user, busType);

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		if (rs > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<RouteCreationDTO> getAllStationsForRouteCreaterEdit2(String routeNo, String busType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<RouteCreationDTO> data = new ArrayList<RouteCreationDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select a.mp_midpoint_code ,a.mp_time_taken,b.midpoint_name\r\n"
					+ "from public.nt_m_route_midpoint_reverse a\r\n"
					+ "inner join public.nt_r_station b on b.code=a.mp_midpoint_code\r\n"
					+ "where a.mp_route_no = ?\r\n" + "and a.mp_bus_type=?";
			ps = con.prepareStatement(query);
			ps.setString(1, routeNo);
			ps.setString(2, busType);
			rs = ps.executeQuery();

			while (rs.next()) {
				RouteCreationDTO p = new RouteCreationDTO();
				p.setStationCode(rs.getString("mp_midpoint_code"));
				p.setStaionName(rs.getString("midpoint_name"));
				p.setTimeTaken(rs.getString("mp_time_taken"));
				data.add(p);

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
	public Map<String, String> getDataForCheckDuplicate(String routeNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String,String> data = new HashMap<>() ;
		try {

			con = ConnectionManager.getConnection();

			String query = "select b.rs_station,b.rs_stage,a.midpoint_name,a.midpoint_name_sin,a.midpoint_name_tamil,c.description,c.code,"
					+ "rs_is_normal,rs_is_luxuary,rs_is_semi_luxuary,rs_is_super_luxuary,rs_is_expressway "
					+ "from public.nt_m_route_station b " + "inner join public.nt_r_station a on  a.code=b.rs_station "
					+ "inner join public.nt_r_service_types c on  c.code=b. rs_service_type "
					+ "where a.status='A' and b.rs_route_no=? order by b.rs_stage";
			ps = con.prepareStatement(query);
			ps.setString(1, routeNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				
				data.put(rs.getString("rs_stage"), rs.getString("rs_station"));

				

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
}
