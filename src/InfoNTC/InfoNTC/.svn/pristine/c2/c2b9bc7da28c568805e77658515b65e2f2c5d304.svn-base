package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.informatics.ntc.model.dto.RouteSetUpDTO;
import lk.informatics.ntc.model.dto.TerminalTimeTableDTO;
import lk.informatics.ntc.view.util.ConnectionManager;

public class RouteSetUpForTimeTableServiceImpl implements RouteSetUpForTimeTablePurposeService {

	@Override
	public List<RouteSetUpDTO> searchRouteDetails(String routeNo, String serviceType) {
		List<RouteSetUpDTO> routeDetails = new ArrayList<>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			String sql = null;
			con = ConnectionManager.getConnection();

			sql = "SELECT details.*,route.rou_service_origine,route.rou_service_destination,service.description FROM public.nt_t_pm_application details "
					+ "INNER JOIN public.nt_r_route route on details.pm_route_no = route.rou_number "
					+ "INNER JOIN public.nt_r_service_types service on details.pm_service_type = service.code"
					+ " WHERE pm_route_no = ? AND pm_service_type = ?  AND (pm_status = 'A' OR pm_status = 'O') order by pm_route_flag";

			ps = con.prepareStatement(sql);
			ps.setString(1, routeNo);
			ps.setString(2, serviceType);

			rs = ps.executeQuery();

			while (rs.next()) {
				RouteSetUpDTO routeSetUpDTO = new RouteSetUpDTO();
				routeSetUpDTO.setSeq(rs.getLong("seq"));
				routeSetUpDTO.setRouteNo(rs.getString("pm_route_no"));
				routeSetUpDTO.setServiceType(rs.getString("description"));
				routeSetUpDTO.setPermitAuthority("NTC");
				routeSetUpDTO.setPermitNo(rs.getString("pm_permit_no"));
				routeSetUpDTO.setBusNo(rs.getString("pm_vehicle_regno"));
				routeSetUpDTO.setExpiryDate(rs.getString("pm_permit_expire_date"));

				String routefalg = rs.getString("pm_route_flag");

				if (routefalg.equals("Y")) {
					routeSetUpDTO.setRouteFlag(true);
					routeSetUpDTO.setOrigin(rs.getString("rou_service_destination"));
					routeSetUpDTO.setDestination(rs.getString("rou_service_origine"));
				} else if (routefalg.equals("N")) {
					routeSetUpDTO.setRouteFlag(false);
					routeSetUpDTO.setOrigin(rs.getString("rou_service_origine"));
					routeSetUpDTO.setDestination(rs.getString("rou_service_destination"));
				}

				routeSetUpDTO.setStatus(rs.getString("pm_status"));

				if (routeSetUpDTO.getStatus().equals("A")) {

					routeSetUpDTO.setStatusDes("Active");

				} else if (routeSetUpDTO.getStatus().equals("O")) {

					routeSetUpDTO.setStatusDes("Ongoing");

				} else if (routeSetUpDTO.getStatus().equals("P")) {

					routeSetUpDTO.setStatusDes("Pending");

				} else if (routeSetUpDTO.getStatus().equals("I")) {

					routeSetUpDTO.setStatusDes("Inactive");

				} else if (routeSetUpDTO.getStatus().equals("E")) {

					routeSetUpDTO.setStatusDes("Expired");

				} else if (routeSetUpDTO.getStatus().equals("TC")) {

					routeSetUpDTO.setStatusDes("Temporary Cancelled");

				} else if (routeSetUpDTO.getStatus().equals("C")) {

					routeSetUpDTO.setStatusDes("Cancelled");

				} else if (routeSetUpDTO.getStatus().equals("H")) {

					routeSetUpDTO.setStatusDes("Hold");

				} else if (routeSetUpDTO.getStatus().equals("R")) {

					routeSetUpDTO.setStatusDes("Rejected");

				}

				routeSetUpDTO.setModifiedDate(rs.getTimestamp("pm_created_date"));
				routeSetUpDTO.setModifiedBy(rs.getString("pm_created_by"));
				routeDetails.add(routeSetUpDTO);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return routeDetails;
	}

	@Override
	public void setTimeTableData(List<RouteSetUpDTO> modifiedDataList) {
		Connection con = null;
		PreparedStatement psInsert = null;
		PreparedStatement psUpdate = null;
		PreparedStatement psTime = null;
		ResultSet rs = null;

		try {
			String updateSql = null;
			String insertSql = null;
			String timeSql = null;

			con = ConnectionManager.getConnection();

			insertSql = "INSERT INTO public.nt_t_route_setup_timetable "
					+ "(rs_route_no, rs_service_type, rs_permit_authority, rs_permit_no, rs_status, rs_origine, rs_destination, rs_created_by, rs_group_no, rs_permit_expire_date, rs_created_date, rs_vehicle_regno, seq, rs_route_flag, rs_use_time_table) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			updateSql = "UPDATE public.nt_t_route_setup_timetable SET rs_route_no = ?, rs_service_type = ?, rs_permit_authority = ?, rs_permit_no = ?, rs_status = ?, "
					+ "rs_origine = ?, rs_destination = ?, rs_created_by = ?, rs_group_no = ?, rs_permit_expire_date = ?, rs_created_date = ?, rs_vehicle_regno = ?, rs_route_flag = ?, rs_use_time_table = ? WHERE seq = ?";

			timeSql = "SELECT NOW()";

			psInsert = con.prepareStatement(insertSql);
			psUpdate = con.prepareStatement(updateSql);
			psTime = con.prepareStatement(timeSql);

			rs = psTime.executeQuery();
			Timestamp currentTimestamp = null;
			try {
				if (rs.next()) {
					currentTimestamp = rs.getTimestamp(1);
				}

				for (RouteSetUpDTO data : modifiedDataList) {

					psInsert.setString(1, data.getRouteNo());
					psInsert.setString(2, data.getServiceType());
					psInsert.setString(3, data.getPermitAuthority());
					psInsert.setString(4, data.getPermitNo());
					psInsert.setString(5, data.getStatus());
					psInsert.setString(6, data.getOrigin());
					psInsert.setString(7, data.getDestination());
					psInsert.setString(8, data.getModifiedBy());
					psInsert.setString(9, data.getGroupNo());
					psInsert.setString(10, data.getExpiryDate());
					psInsert.setTimestamp(11, currentTimestamp);
					psInsert.setString(12, data.getBusNo());
					psInsert.setLong(13, data.getSeq());
					psInsert.setBoolean(14, data.isRouteFlag());
					psInsert.setBoolean(15, data.isUseForTimeTable());

					psInsert.executeUpdate();
					con.commit();

				}
			} catch (SQLException e) {
				con.rollback();
				e.printStackTrace();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(psTime);
			ConnectionManager.close(psUpdate);
			ConnectionManager.close(psInsert);
			ConnectionManager.close(con);
		}

	}

	@Override
	public void editedTimeTableData(List<RouteSetUpDTO> modifiedDataListEdit) {
		Connection con = null;
		PreparedStatement psTime = null;
		PreparedStatement psUpdate = null;
		ResultSet rs = null;

		try {
			String updateSql = null;
			String timeSql = null;

			con = ConnectionManager.getConnection();

			updateSql = "UPDATE public.nt_t_route_setup_timetable SET rs_route_no = ?, rs_service_type = ?, rs_permit_authority = ?, "
					+ "rs_permit_no = ?, rs_status = ?, rs_origine = ?, rs_destination = ?, rs_created_by = ?, "
					+ "rs_permit_expire_date = ?, rs_created_date = ?, rs_vehicle_regno = ?, "
					+ "rs_route_flag = ?, rs_use_time_table = ? WHERE seq = ? and rs_group_no = ?";

			timeSql = "SELECT NOW()";

			psUpdate = con.prepareStatement(updateSql);
			psTime = con.prepareStatement(timeSql);
			try {
				for (RouteSetUpDTO data : modifiedDataListEdit) {
					psUpdate.setString(1, data.getRouteNo());
					psUpdate.setString(2, data.getServiceType());
					psUpdate.setString(3, data.getPermitAuthority());
					psUpdate.setString(4, data.getPermitNo());
					psUpdate.setString(5, data.getStatus());
					psUpdate.setString(6, data.getOrigin());
					psUpdate.setString(7, data.getDestination());
					psUpdate.setString(8, data.getModifiedBy());
					psUpdate.setString(9, data.getExpiryDate());
					rs = psTime.executeQuery();
					if (rs.next()) {
						Timestamp currentTimestamp = rs.getTimestamp(1);
						psUpdate.setTimestamp(10, currentTimestamp);
					}
					psUpdate.setString(11, data.getBusNo());
					psUpdate.setBoolean(12, data.isRouteFlag());
					psUpdate.setBoolean(13, data.isUseForTimeTable());
					psUpdate.setLong(14, data.getSeq());
					psUpdate.setString(15, data.getGroupNo());

					psUpdate.executeUpdate();
					con.commit();
				}
			} catch (SQLException e) {
				con.rollback();
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(psTime);
			ConnectionManager.close(psUpdate);
			ConnectionManager.close(con);
		}

	}

	@Override
	public List<RouteSetUpDTO> searchRouteDetailsEditView(String routeNo, String serviceType, String groupNo) {
		List<RouteSetUpDTO> routeDetails = new ArrayList<>();
		String modifiedDate = null;

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			String sql = null;
			con = ConnectionManager.getConnection();

			sql = "SELECT details.*, service.description FROM public.nt_t_route_setup_timetable details "
					+ "INNER JOIN public.nt_r_service_types service on details.rs_service_type = service.code"
					+ " WHERE rs_route_no = ? AND rs_service_type = ? AND rs_group_no = ? AND (rs_status = 'A' OR rs_status = 'O') "
					+ "order by rs_route_flag";

			ps = con.prepareStatement(sql);
			ps.setString(1, routeNo);
			ps.setString(2, serviceType);
			ps.setString(3, groupNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				RouteSetUpDTO routeSetUpDTO = new RouteSetUpDTO();
				Timestamp createdDate = rs.getTimestamp("rs_created_date");

				if (createdDate != null) {
					SimpleDateFormat timeFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date date = new Date(createdDate.getTime());
					modifiedDate = timeFormat.format(date);
				}

				routeSetUpDTO.setSeq(rs.getLong("seq"));
				routeSetUpDTO.setRouteNo(rs.getString("rs_route_no"));
				routeSetUpDTO.setServiceType(rs.getString("description"));
				routeSetUpDTO.setPermitAuthority("NTC");
				routeSetUpDTO.setPermitNo(rs.getString("rs_permit_no"));
				routeSetUpDTO.setBusNo(rs.getString("rs_vehicle_regno"));
				routeSetUpDTO.setExpiryDate(rs.getString("rs_permit_expire_date"));

				routeSetUpDTO.setStatus(rs.getString("rs_status"));

				if (routeSetUpDTO.getStatus().equals("A")) {

					routeSetUpDTO.setStatusDes("Active");

				} else if (routeSetUpDTO.getStatus().equals("O")) {

					routeSetUpDTO.setStatusDes("Ongoing");

				} else if (routeSetUpDTO.getStatus().equals("P")) {

					routeSetUpDTO.setStatusDes("Pending");

				} else if (routeSetUpDTO.getStatus().equals("I")) {

					routeSetUpDTO.setStatusDes("Inactive");

				} else if (routeSetUpDTO.getStatus().equals("E")) {

					routeSetUpDTO.setStatusDes("Expired");

				} else if (routeSetUpDTO.getStatus().equals("TC")) {

					routeSetUpDTO.setStatusDes("Temporary Cancelled");

				} else if (routeSetUpDTO.getStatus().equals("C")) {

					routeSetUpDTO.setStatusDes("Cancelled");

				} else if (routeSetUpDTO.getStatus().equals("H")) {

					routeSetUpDTO.setStatusDes("Hold");

				} else if (routeSetUpDTO.getStatus().equals("R")) {

					routeSetUpDTO.setStatusDes("Rejected");

				}
				routeSetUpDTO.setOrigin(rs.getString("rs_origine"));
				routeSetUpDTO.setDestination(rs.getString("rs_destination"));
				routeSetUpDTO.setModifiedDateSave(modifiedDate);
				routeSetUpDTO.setModifiedBy(rs.getString("rs_created_by"));
				routeSetUpDTO.setRouteFlag(rs.getBoolean("rs_route_flag"));
				routeSetUpDTO.setUseForTimeTable(rs.getBoolean("rs_use_time_table"));
				routeDetails.add(routeSetUpDTO);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return routeDetails;
	}

	@Override
	public List<RouteSetUpDTO> getRouteNoEdit() {
		List<RouteSetUpDTO> routeDetails = new ArrayList<>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			String sql = null;
			con = ConnectionManager.getConnection();

			sql = "SELECT distinct on (rs_route_no)  rs_route_no,nt_r_route.rou_description FROM public.nt_t_route_setup_timetable "
					+ "inner join public.nt_r_route on nt_r_route.rou_number  = nt_t_route_setup_timetable.rs_route_no";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				RouteSetUpDTO routeSetUpDTO = new RouteSetUpDTO();
				routeSetUpDTO.setRouteNo(rs.getString("rs_route_no"));
				routeSetUpDTO.setRouteNoDes(rs.getString("rou_description"));
				routeDetails.add(routeSetUpDTO);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return routeDetails;
	}

	@Override
	public boolean searchHistory(String routeNo, String serviceType, String group) {
		boolean haveData = true;

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			String sql = null;
			con = ConnectionManager.getConnection();

			sql = "SELECT details.*, service.description FROM public.nt_t_route_setup_timetable details "
					+ "INNER JOIN public.nt_r_service_types service on details.rs_service_type = service.code"
					+ " WHERE rs_route_no = ? AND rs_service_type = ? AND rs_group_no = ? AND (rs_status = 'A' OR rs_status = 'O') "
					+ "order by rs_route_flag";

			ps = con.prepareStatement(sql);
			ps.setString(1, routeNo);
			ps.setString(2, serviceType);
			ps.setString(3, group);

			rs = ps.executeQuery();

			while (rs.next()) {
				haveData = false;
				break;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return haveData;
	}

	@Override
	public boolean UpdateStatus(String routeNo, String serviceType, String group) {
		boolean haveData = false;

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			String sql = null;
			con = ConnectionManager.getConnection();

			sql = "UPDATE public.nt_t_route_setup_timetable SET rs_status = ?, rs_use_time_table = false WHERE rs_route_no = ? "
					+ "AND rs_service_type = ? AND rs_group_no = ?";

			ps = con.prepareStatement(sql);
			try {
				ps.setString(1, "I");
				ps.setString(2, routeNo);
				ps.setString(3, serviceType);
				ps.setString(4, group);

				int updateCount = ps.executeUpdate();

				if (updateCount != 0) {
					haveData = true;
				}

			} catch (SQLException e) {
				con.rollback();
				e.printStackTrace();
			}

			con.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return haveData;
	}

	/* Remove Time Table - methods added by dhananjika.d 12/03/2024 */

	@Override
	public List<RouteSetUpDTO> getRouteDetails(String refNo) {
		List<RouteSetUpDTO> routeDetails = new ArrayList<>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			String sql = null;
			con = ConnectionManager.getConnection();

			sql = "SELECT  nt_m_panelgenerator.route_no,"
					+ "nt_m_panelgenerator.bus_category, "
					+ "nt_r_route.rou_service_origine,"
					+ "nt_r_service_types.description, "
					+ "nt_r_route.rou_service_destination,"
					+ "nt_t_panelgenerator_det.group_no "
					+ "FROM nt_m_panelgenerator  "
					+ "inner join nt_r_route on nt_r_route.rou_number=nt_m_panelgenerator.route_no "
					+ "inner join nt_t_panelgenerator_det on nt_t_panelgenerator_det.seq_panelgenerator=nt_m_panelgenerator.seq "
					+ "inner join public.nt_r_service_types on nt_r_service_types.code = nt_m_panelgenerator.bus_category "
					+ "where nt_m_panelgenerator.ref_no= ?";

			ps = con.prepareStatement(sql);
			ps.setString(1, refNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				RouteSetUpDTO routeSetUpDTO = new RouteSetUpDTO();
				routeSetUpDTO.setRouteNo(rs.getString("route_no"));
				routeSetUpDTO.setServiceType(rs.getString("description"));
				routeSetUpDTO.setOrigin(rs.getString("rou_service_origine"));
				routeSetUpDTO.setDestination(rs.getString("rou_service_destination"));
				routeSetUpDTO.setCode(rs.getString("bus_category"));
				routeSetUpDTO.setGroupNo(rs.getString("group_no"));
				routeDetails.add(routeSetUpDTO);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return routeDetails;
	}

	@Override
	public boolean saveDeletedDetails(RouteSetUpDTO data, String reason, String user, String refNo) {
		Connection con = null;
		PreparedStatement psInsert = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		boolean success = false;

		try {
			String insertSql = null;

			con = ConnectionManager.getConnection();

			insertSql = "INSERT INTO public.nt_h_time_table_delete_history "
					+ "(ref_no,	route_no, service_type, group_no, deleted_by, deleted_date,	reason,	origin,	destination) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

			psInsert = con.prepareStatement(insertSql);

			try {

				psInsert.setString(1, refNo);
				psInsert.setString(2, data.getRouteNo());
				psInsert.setString(3, data.getCode());
				psInsert.setString(4, data.getGroupNo());
				psInsert.setString(5, user);
				psInsert.setTimestamp(6, timestamp);
				psInsert.setString(7, reason);
				psInsert.setString(8, data.getOrigin());
				psInsert.setString(9, data.getDestination());

				int insertCount = psInsert.executeUpdate();
				if (insertCount > 0) {
	                success = true;
	            }

			} catch (SQLException e) {
				con.rollback();
				e.printStackTrace();
			}

			con.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(psInsert);
			ConnectionManager.close(con);
		}
		return success;
	}
	
	@Override
	public boolean removeTimeTableData(String refNo) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		PreparedStatement ps4 = null;
		PreparedStatement ps5 = null;
		PreparedStatement ps6 = null;
		PreparedStatement ps7 = null;
		ResultSet rs = null;
		long seq = 0;
		int deleteCount = 0;
		boolean success = false;

		String[] queries = { "DELETE FROM public.nt_t_route_schedule_generator_det02 WHERE generator_ref_no = ?",
				"DELETE FROM public.nt_t_route_schedule_generator_det02_two_day WHERE generator_ref_no = ?",
				"DELETE FROM public.nt_t_route_schedule_generator_det01 WHERE generator_ref_no = ?",
				"DELETE FROM public.nt_t_route_schedule_generator_det01_two_day WHERE generator_ref_no = ?",
				"DELETE FROM public.nt_m_route_schedule_generator WHERE rs_generator_ref_no = ?",
				"SELECT seq FROM public.nt_m_panelgenerator WHERE ref_no = ?",
				"DELETE FROM public.nt_m_panelgenerator WHERE ref_no = ?",
				"DELETE FROM public.panel_generator_general_data WHERE refno = ?",
				"DELETE FROM public.panel_generator_origin_trip_details WHERE ref_no = ?",
				"DELETE FROM public.panel_generator_destination_trip_details WHERE ref_no = ?",
				"DELETE FROM public.panel_generator_leave_bus_details WHERE ref_no = ?",
				"DELETE FROM public.nt_t_control_panel_bus_details WHERE refno = ?" };

		try {
			con = ConnectionManager.getConnection();
			for (String query : queries) {
				ps = con.prepareStatement(query);

				if (query.contains("SELECT")) {
					ps.setString(1, refNo);
					rs = ps.executeQuery();

					while (rs.next()) {
						seq = rs.getLong("seq");
						PreparedStatement deletePs = con.prepareStatement(
								"DELETE FROM public.nt_t_panelgenerator_det WHERE seq_panelgenerator = ?");
						deletePs.setLong(1, seq);
						deleteCount = deletePs.executeUpdate();
						
						if(deleteCount > 0) {
							success = true;
						}
						
						ConnectionManager.close(deletePs);
					}
				} else {
					ps.setString(1, refNo);
					deleteCount = ps.executeUpdate();
					
					if(deleteCount > 0) {
						success = true;
					}
				}

				ConnectionManager.close(ps);
				
				con.commit();
			}
		} catch (SQLException e) {
			try {
				if (con != null) {
					con.rollback();
				}
			} catch (SQLException rollbackException) {
				rollbackException.printStackTrace();
			}
			e.printStackTrace();
			success = false;

		} catch (Exception e) {
			success = false;
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(ps3);
			ConnectionManager.close(ps4);
			ConnectionManager.close(ps5);
			ConnectionManager.close(ps5);
			ConnectionManager.close(ps6);
			ConnectionManager.close(ps7);
			ConnectionManager.close(con);
		}

		return success;
	}
	
	
	@Override
	public boolean removeTempTable(RouteSetUpDTO data) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		PreparedStatement ps4 = null;
		PreparedStatement ps5 = null;
		PreparedStatement ps6 = null;
		PreparedStatement ps7 = null;
		boolean success = false;
		int deleteCount = 0;
		try {

			con = ConnectionManager.getConnection();

			String query = "DELETE FROM public.nt_m_midpoint_o_to_d WHERE mp_route_no = ? "
					+ "AND mp_service_type = ? AND group_no = ?";

			ps = con.prepareStatement(query);
			ps.setString(1, data.getRouteNo());
			ps.setString(2, data.getCode());
			ps.setString(3, data.getGroupNo());
			ps.executeUpdate();

			ConnectionManager.close(ps);

			String query2 = "DELETE FROM public.nt_m_midpoint_d_to_o WHERE mp_route_no = ? "
					+ "AND mp_service_type = ? AND group_no = ?";

			ps2 = con.prepareStatement(query2);
			ps2.setString(1, data.getRouteNo());
			ps2.setString(2, data.getCode());
			ps2.setString(3, data.getGroupNo());
			ps2.executeUpdate();

			ConnectionManager.close(ps2);

			String query3 = "DELETE  FROM public.nt_m_combinepanal_temp WHERE mp_route_no = ?"
					+ " AND mp_service_type = ? AND group_no = ?";

			ps3 = con.prepareStatement(query3);
			ps3.setString(1, data.getRouteNo());
			ps3.setString(2, data.getCode());
			ps3.setString(3, data.getGroupNo());
			ps3.executeUpdate();

			ConnectionManager.close(ps3);

			String query4 = "DELETE FROM public.nt_t_combine_panel_generator WHERE route_no = ? AND service_type = ?";

			ps4 = con.prepareStatement(query4);
			ps4.setString(1, data.getRouteNo());
			ps4.setString(2, data.getCode());
			ps4.executeUpdate();

			ConnectionManager.close(ps4);

			con.commit();
			success = true;
		}catch (SQLException e) {
	        try {
	            if (con != null) {
	                con.rollback();
	            }
	        } catch (SQLException rollbackException) {
	            rollbackException.printStackTrace();
	        }
	        e.printStackTrace();
	        success = false;

	    } catch (Exception e) {
			e.printStackTrace();
			 success = false;
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(ps3);
			ConnectionManager.close(ps4);
			ConnectionManager.close(ps5);
			ConnectionManager.close(ps5);
			ConnectionManager.close(ps6);
			ConnectionManager.close(ps7);
			ConnectionManager.close(con);
		}
		
		return success;
	}
	
	
	/* Remove NFC Card Print Details - methods added by dhananjika.d 18/03/2024 */

	@Override
	public List<String> getPermitNoList() {
		List<String> routeDetails = new ArrayList<>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			String sql = null;
			con = ConnectionManager.getConnection();

			sql = "SELECT distinct nph_permit_no FROM public.nt_t_nfc_print_history where nph_permit_no is not null";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				routeDetails.add(rs.getString("nph_permit_no"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return routeDetails;
	}
	
	@Override
	public List<String> getBusNoList() {
		List<String> routeDetails = new ArrayList<>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			String sql = null;
			con = ConnectionManager.getConnection();

			sql = "SELECT distinct nph_vehicle_no FROM public.nt_t_nfc_print_history where nph_vehicle_no is not null";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				routeDetails.add(rs.getString("nph_vehicle_no"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return routeDetails;
	}
	
	@Override
	public List<RouteSetUpDTO> getNFCDetails(String permitNo, String busNo) {
		List<RouteSetUpDTO> routeDetails = new ArrayList<>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		long count = 1;

		try {
			String sql = null;
			String WHERE_SQL = null;
			con = ConnectionManager.getConnection();
			
			if(permitNo != null && !permitNo.isEmpty()) {
				WHERE_SQL = "WHERE nph_permit_no = '" + permitNo + "'";
			}else if(busNo != null && !busNo.isEmpty()) {
				WHERE_SQL = "WHERE nph_vehicle_no = '" + busNo + "'";
			}else if((permitNo != null && !permitNo.isEmpty()) || (busNo != null && !busNo.isEmpty())) {
				WHERE_SQL = "WHERE nph_vehicle_no = '" + busNo + "' AND nph_permit_no = '" + permitNo + "'";
			}

			sql = "SELECT * FROM public.nt_t_nfc_print_history " + WHERE_SQL + " ORDER BY seq;";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				RouteSetUpDTO routeSetUpDTO = new RouteSetUpDTO();
				routeSetUpDTO.setPermitNo(rs.getString("nph_permit_no"));
				routeSetUpDTO.setBusNo(rs.getString("nph_vehicle_no"));
				routeSetUpDTO.setDateFrom(rs.getString("nph_date_from"));
				routeSetUpDTO.setDateTo(rs.getString("nph_date_to"));
				routeSetUpDTO.setOwnerNIC(rs.getString("nph_owner_nic"));
				routeSetUpDTO.setCreatedDate(rs.getDate("nph_date_careated"));
				routeSetUpDTO.setIssueDate(rs.getDate("nph_issue_date"));
				routeSetUpDTO.setCreatedUser(rs.getString("nph_user_created"));
				routeSetUpDTO.setStatus(rs.getString("nph_status"));
				routeSetUpDTO.setRowNo(rs.getInt("seq"));
				routeDetails.add(routeSetUpDTO);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return routeDetails;
	}
	
	
	@Override
	public boolean removeNFCData(List<RouteSetUpDTO> dataList) {
		Connection con = null;
		PreparedStatement ps = null;
		boolean success = false;
		
		String query = "DELETE FROM public.nt_t_nfc_print_history WHERE seq = ?;";
		try {
			con = ConnectionManager.getConnection();
			ps = con.prepareStatement(query);
			

			for(RouteSetUpDTO data : dataList) {
				ps.setInt(1, data.getRowNo());
				ps.addBatch();
			}
			
			ps.executeBatch();
			con.commit();
			success = true;
		}catch (SQLException e) {
	        try {
	            if (con != null) {
	                con.rollback();
	            }
	        } catch (SQLException rollbackException) {
	            rollbackException.printStackTrace();
	        }
	        e.printStackTrace();
	        success = false;

	    } catch (Exception e) {
			e.printStackTrace();
			 success = false;
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		
		return success;
	}
	
	@Override
	public boolean saveNFCDeletedDetails(List<RouteSetUpDTO> dataList, String reason, String user) {
		Connection con = null;
		PreparedStatement psInsert = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		boolean success = false;

			String insertSql = null;

			con = ConnectionManager.getConnection();

			insertSql = "INSERT INTO public.nt_h_nfc_print_delete_history "
					+ "(nph_permit_no, nph_vehicle_no, nph_date_from, nph_date_to, nph_owner_nic, nph_issue_date, "
					+ "nph_date_careated, nph_user_created, nph_status, nph_user_deleted, nph_date_deleted, nph_reason_deleted) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

			try {
				con = ConnectionManager.getConnection();
				psInsert = con.prepareStatement(insertSql);
				

				for(RouteSetUpDTO data : dataList) {					
					psInsert.setString(1, data.getPermitNo());
					psInsert.setString(2, data.getBusNo());
					psInsert.setString(3, data.getDateFrom());
					psInsert.setString(4, data.getDateTo());
					psInsert.setString(5, data.getOwnerNIC());
					psInsert.setDate(6, data.getIssueDate());
					psInsert.setDate(7, data.getCreatedDate());
					psInsert.setString(8, data.getCreatedUser());
					psInsert.setString(9, data.getStatus());
					psInsert.setString(10, user);
					psInsert.setTimestamp(11, timestamp);
					psInsert.setString(12, reason);
					psInsert.addBatch();
				}
				
				psInsert.executeBatch();
				con.commit();
				success = true;
			}catch (SQLException e) {
		        try {
		            if (con != null) {
		                con.rollback();
		            }
		        } catch (SQLException rollbackException) {
		            rollbackException.printStackTrace();
		        }
		        e.printStackTrace();
		        success = false;

		    } catch (Exception e) {
				e.printStackTrace();
				 success = false;
			} finally {
				ConnectionManager.close(psInsert);
				ConnectionManager.close(con);
			}
		return success;
	}
	
	
	@Override
	public List<RouteSetUpDTO> getDeletedNFCDetails() {
		List<RouteSetUpDTO> routeDetails = new ArrayList<>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			String sql = null;
			con = ConnectionManager.getConnection();

			sql = "SELECT * FROM public.nt_h_nfc_print_delete_history ORDER BY seq;";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				RouteSetUpDTO routeSetUpDTO = new RouteSetUpDTO();
				routeSetUpDTO.setPermitNo(rs.getString("nph_permit_no"));
				routeSetUpDTO.setBusNo(rs.getString("nph_vehicle_no"));
				routeSetUpDTO.setDateFrom(rs.getString("nph_date_from"));
				routeSetUpDTO.setDateTo(rs.getString("nph_date_to"));
				routeSetUpDTO.setOwnerNIC(rs.getString("nph_owner_nic"));
				routeSetUpDTO.setCreatedDate(rs.getDate("nph_date_careated"));
				routeSetUpDTO.setIssueDate(rs.getDate("nph_issue_date"));
				routeSetUpDTO.setCreatedUser(rs.getString("nph_user_created"));
				routeSetUpDTO.setStatus(rs.getString("nph_status"));				
				routeSetUpDTO.setDeletedUser(rs.getString("nph_user_deleted"));
				routeSetUpDTO.setReason(rs.getString("nph_reason_deleted"));
				routeSetUpDTO.setRowNo(rs.getInt("seq"));
				if (rs.getTimestamp("nph_date_deleted") != null) {
					SimpleDateFormat timeFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date date = new Date(rs.getTimestamp("nph_date_deleted").getTime());
					String modifiedDate = timeFormat.format(date);
					routeSetUpDTO.setDeletedDateStr(modifiedDate);
				}
				routeDetails.add(routeSetUpDTO);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return routeDetails;
	}

	// To get details  from RouteSchedule function : added by dhananjika.d (26/04/2024)
	@Override
	public List<List<Object>> getTerminalTimeTableDetails(String refNo) {
		
		List<String> busNumbers = new ArrayList<>();
        List<Integer> dayNumbers = new ArrayList<>();
        List<Integer> tripIds = new ArrayList<>();
        
        List<List<Object>> resultList = new ArrayList<>();
        
        Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			String sql = null;
			con = ConnectionManager.getConnection();

			sql = "SELECT"
					+ "	* "
					+ "FROM"
					+ "	( "
					+ "	SELECT"
					+ "		DISTINCT nt_t_route_schedule_generator_det01.bus_no,\r\n"
					+ "		nt_t_route_schedule_generator_det01.day_no,\r\n"
					+ "		nt_t_route_schedule_generator_det01.trip_id,\r\n"
					+ "		nt_t_route_schedule_generator_det01.trip_type,\r\n"
					+ "		CASE\r\n"
					+ "			WHEN nt_t_route_schedule_generator_det01.trip_type = 'O' THEN 0\r\n"
					+ "			WHEN nt_t_route_schedule_generator_det01.trip_type = 'D' THEN 2\r\n"
					+ "			ELSE 1\r\n"
					+ "		END AS order_column\r\n"
					+ "	FROM\r\n"
					+ "		public.nt_t_route_schedule_generator_det01\r\n"
					+ "	WHERE\r\n"
					+ "		nt_t_route_schedule_generator_det01.generator_ref_no = ?\r\n"
					+ "		AND nt_t_route_schedule_generator_det01.bus_no IS NOT NULL\r\n"
					+ "	) AS subquery\r\n"
					+ "ORDER BY\r\n"
					+ "	order_column,\r\n"
					+ "	CAST(day_no AS INTEGER);";

			ps = con.prepareStatement(sql);
			ps.setString(1, refNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				busNumbers.add(rs.getString("refno"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		
		
		return resultList;	
	}

	@Override
	public List<String> getTerminalTimeTableStartTime(String refNo) {
		
		List<String> startTime = new ArrayList<>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;

		try {
			String sql = null;
			con = ConnectionManager.getConnection();

			sql = "select start_time from public.panel_generator_origin_trip_details where ref_no = ?";

			ps = con.prepareStatement(sql);
			ps.setString(1, refNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				startTime.add(rs.getString("start_time"));
			}
			
			sql = "select start_time from public.panel_generator_destination_trip_details where ref_no = ?";

			ps1 = con.prepareStatement(sql);
			ps1.setString(1, refNo);
			rs1 = ps1.executeQuery();

			while (rs1.next()) {
				startTime.add(rs1.getString("start_time"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs1);
			ConnectionManager.close(ps1);
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		
		return startTime;
	}

	@Override
	public List<String> getRefNoList(String route, String serviceNo) {
		List<String> referenceNum = new ArrayList<>();
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			String sql = null;
			con = ConnectionManager.getConnection();

			sql = "select refno from public.panel_generator_general_data where routeno = ? and buscategory = ? order by groupcount";

			ps = con.prepareStatement(sql);
			ps.setString(1, route);
			ps.setString(2, serviceNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				referenceNum.add(rs.getString("refno"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return referenceNum;
	}
	
	
	@Override
	public boolean checkRefNo(String ref) {
		boolean valid = false;
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			String sql = null;
			con = ConnectionManager.getConnection();

			sql = "select * from public.nt_m_route_schedule_generator where rs_generator_ref_no = ?";

			ps = con.prepareStatement(sql);
			ps.setString(1, ref);
			rs = ps.executeQuery();

			while (rs.next()) {
				valid = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			valid = false;
		} catch (Exception e) {
			e.printStackTrace();
			valid = false;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return valid;
	}
	
	
}