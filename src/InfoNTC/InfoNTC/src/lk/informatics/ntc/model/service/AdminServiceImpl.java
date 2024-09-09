package lk.informatics.ntc.model.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.informatics.ntc.model.dto.AmendmentDTO;
import lk.informatics.ntc.model.dto.AssignModelDTO;
import lk.informatics.ntc.model.dto.AttorneyHolderDTO;
import lk.informatics.ntc.model.dto.BusOwnerDTO;
import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.OminiBusDTO;
import lk.informatics.ntc.model.dto.PermitDTO;
import lk.informatics.ntc.model.dto.PermitPaymentDTO;
import lk.informatics.ntc.model.dto.PermitRenewalsDTO;
import lk.informatics.ntc.model.dto.RouteDTO;
import lk.informatics.ntc.model.dto.TokenDTO;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.roc.utils.common.Utils;

public class AdminServiceImpl implements AdminService {

	private static final long serialVersionUID = 1L;

	@Override
	public BigDecimal completeBusFare(String routeNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		BigDecimal value = null;
		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT rou_number, rou_tot_busfare FROM public.nt_r_route where rou_number=? ;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, routeNo);
			rs = stmt.executeQuery();

			value = new BigDecimal(0);

			if (rs.next() == true) {

				value = rs.getBigDecimal("rou_tot_busfare");
			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return value;
	}

	@Override
	public boolean deleteModel(String code) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isDeleted = false;

		try {
			con = ConnectionManager.getConnection();

			String sql = "DELETE FROM public.nt_r_model WHERE mod_code=? ;";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, code);

			int i = stmt.executeUpdate();

			if (i > 0) {
				isDeleted = true;
			} else {
				isDeleted = false;
			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return isDeleted;
	}

	@Override
	public boolean checkModel(AssignModelDTO assignModelDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isFound = false;

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT mod_code, mod_description, active FROM public.nt_r_model where mod_code=? and active='A';";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, assignModelDTO.getModelCode());

			rs = stmt.executeQuery();

			if (rs.next()) {
				isFound = true;
			} else {
				isFound = false;
			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return isFound;
	}

	@Override
	public boolean insertModel(AssignModelDTO assignModelDTO, String logInUser) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isModelSave = false;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "INSERT INTO public.nt_r_model "
					+ "(mod_code, mod_description, mod_make_code, active, created_by, created_date) "
					+ "VALUES(?, ?, ?, ?, ?, ?); ";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, assignModelDTO.getModelCode());
			stmt.setString(2, assignModelDTO.getModelDescription());
			stmt.setString(3, assignModelDTO.getMakeCode());
			stmt.setString(4, "A");
			stmt.setString(5, logInUser);
			stmt.setTimestamp(6, timestamp);

			int i = stmt.executeUpdate();

			if (i > 0) {
				isModelSave = true;
			} else {
				isModelSave = false;
			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return isModelSave;
	}

	@Override
	public List<AssignModelDTO> getModelDetails(AssignModelDTO assignModelDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String WHERE_SQL = "";

		if (assignModelDTO.getMakeCode() != null && !assignModelDTO.getMakeCode().equals("")) {

			WHERE_SQL = WHERE_SQL + "Where nt_r_model.active='A' and mod_make_code= '" + assignModelDTO.getMakeCode()
					+ "';";
		} else {
			WHERE_SQL = WHERE_SQL + "Where nt_r_model.active='A'";

		}

		List<AssignModelDTO> modelList = new ArrayList<AssignModelDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT mod_code, mod_description, mod_make_code, nt_r_make.description as makeDes, nt_r_model.active as modelActive "
					+ "FROM public.nt_r_model "
					+ "inner join public.nt_r_make on nt_r_make.code= nt_r_model.mod_make_code " + WHERE_SQL;

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				AssignModelDTO dto = new AssignModelDTO();

				dto.setMakeCode(rs.getString("mod_make_code"));
				dto.setMakeDescription(rs.getString("makeDes"));
				dto.setModelCode(rs.getString("mod_code"));
				dto.setModelDescription(rs.getString("mod_description"));
				dto.setModelStatus(rs.getString("modelActive"));

				modelList.add(dto);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return modelList;

	}

	@Override
	public String getMakesDescription(AssignModelDTO assignModelDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT description FROM public.nt_r_make where code=? ;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, assignModelDTO.getMakeCode());
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("description");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return result;
	}

	// Get Makes
	@Override
	public List<AssignModelDTO> getMakes() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<AssignModelDTO> makesList = new ArrayList<AssignModelDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description FROM public.nt_r_make "
					+ "where active='A' and description != '' and description is not null order by code; ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				AssignModelDTO assignModelDTO = new AssignModelDTO();
				assignModelDTO.setMakeCode(rs.getString("code"));
				assignModelDTO.setMakeDescription(rs.getString("description"));

				makesList.add(assignModelDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return makesList;

	}

	// Get Status
	public List<CommonDTO> GetStatusToDropdown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description " + "FROM nt_r_route_status " + "WHERE active = 'A' "
					+ "ORDER BY description ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("code"));
				commonDTO.setDescription(rs.getString("description"));

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

	// Get All Route Details
	public List<RouteDTO> getRouteDetails() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<RouteDTO> routeDetList = new ArrayList<RouteDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT seqno, rou_description, rou_number, "
					+ " rou_service_origine, rou_service_destination, rou_via, "
					+ " case when active = 'A' then 'Active' when active = 'I' then 'Inactive' when active='T' then 'Temporary' else active end, "
					+ " created_by, created_date,rou_distance, active as status_code,rou_tot_busfare, rou_service_origine_sin,"
					+ " rou_service_destination_sin, rou_via_sin , "
					+ " rou_service_origine_tamil, rou_service_destination_tamil, rou_via_tamil "
					+ " FROM public.nt_r_route WHERE active not in ('T') " + " ORDER BY rou_number";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				RouteDTO routeDTO = new RouteDTO();
				routeDTO.setSeq(rs.getLong("seqno"));
				routeDTO.setRouteDes(rs.getString("rou_description"));
				routeDTO.setRouteNo(rs.getString("rou_number"));
				routeDTO.setOrigin(rs.getString("rou_service_origine"));
				routeDTO.setDestination(rs.getString("rou_service_destination"));
				routeDTO.setVia(rs.getString("rou_via"));
				routeDTO.setStatus(rs.getString("active"));
				routeDTO.setCreatedBy(rs.getString("created_by"));
				routeDTO.setCratedDate(rs.getTimestamp("created_date"));
				routeDTO.setDistance(rs.getBigDecimal("rou_distance"));
				routeDTO.setStatusCode(rs.getString("status_code"));
				routeDTO.setBusFare(rs.getBigDecimal("rou_tot_busfare"));
				routeDTO.setOriginS(rs.getString("rou_service_origine_sin"));
				routeDTO.setDestinationS(rs.getString("rou_service_destination_sin"));
				routeDTO.setViaS(rs.getString("rou_via_sin"));
				routeDTO.setOriginT(rs.getString("rou_service_origine_tamil"));
				routeDTO.setDestinationT(rs.getString("rou_service_destination_tamil"));
				routeDTO.setViaT(rs.getString("rou_via_tamil"));
				routeDetList.add(routeDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return routeDetList;

	}

	// Save Route Details
	public int saveRoute(RouteDTO routeDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_r_route");

			String sql = "INSERT INTO public.nt_r_route" + "(seqno, rou_description, rou_number, rou_service_origine,"
					+ " rou_service_destination, rou_via, active, created_by, created_date, rou_tot_busfare, "
					+ " rou_distance, rou_service_origine_sin, rou_service_destination_sin, rou_via_sin, "
					+ " rou_service_origine_tamil, rou_service_destination_tamil, rou_via_tamil "
					+ ")" + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, routeDTO.getRouteDes());
			stmt.setString(3, routeDTO.getRouteNo());
			stmt.setString(4, routeDTO.getOrigin());
			stmt.setString(5, routeDTO.getDestination());
			stmt.setString(6, routeDTO.getVia());
			stmt.setString(7, routeDTO.getStatus());
			stmt.setString(8, routeDTO.getCreatedBy());
			stmt.setTimestamp(9, timestamp);
			stmt.setBigDecimal(10, routeDTO.getBusFare());
			stmt.setBigDecimal(11, routeDTO.getDistance());
			stmt.setString(12, routeDTO.getOriginS());
			stmt.setString(13, routeDTO.getDestinationS());
			stmt.setString(14, routeDTO.getViaS());
			stmt.setString(15, routeDTO.getOriginT());
			stmt.setString(16, routeDTO.getDestinationT());
			stmt.setString(17, routeDTO.getViaT());

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

	// Update Route Details
	public int updateRouteDetails(RouteDTO routeDTO) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = " UPDATE public.nt_r_route" + " SET rou_description= ?, rou_service_origine= ?, "
					+ " rou_service_destination= ?, rou_via= ?, active= ?, "
					+ " modified_by= ?, modified_date= ?, rou_distance= ?, "
					+ " rou_tot_busfare =?, rou_service_origine_sin= ?, rou_service_destination_sin= ?, rou_via_sin= ?, "
					+ " rou_service_origine_tamil= ? , rou_service_destination_tamil= ?, rou_via_tamil= ? "					
					+ " WHERE seqno= ?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, routeDTO.getRouteDes());
			stmt.setString(2, routeDTO.getOrigin());
			stmt.setString(3, routeDTO.getDestination());
			stmt.setString(4, routeDTO.getVia());
			stmt.setString(5, routeDTO.getStatus());
			stmt.setString(6, routeDTO.getModifiedBy());
			stmt.setTimestamp(7, timestamp);
			stmt.setBigDecimal(8, routeDTO.getDistance());
			stmt.setBigDecimal(9, routeDTO.getBusFare());
			stmt.setString(10, routeDTO.getOriginS());
			stmt.setString(11, routeDTO.getDestinationS());
			stmt.setString(12, routeDTO.getViaS());
			stmt.setString(13, routeDTO.getOriginT());
			stmt.setString(14, routeDTO.getDestinationT());
			stmt.setString(15, routeDTO.getViaT());
			stmt.setLong(16, routeDTO.getSeq());

			stmt.executeUpdate();
			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return 0;
	}

	// Get user Activity
	public String getUserActivity(String userName, String functionId) {

		String userActivity = null;
		Connection con;
		PreparedStatement ps = null;
		ResultSet rs = null;

		con = ConnectionManager.getConnection();
		try {

			String sql = "SELECT b.fra_activity_code"
					+ " FROM nt_t_granted_user_role a inner join nt_r_fun_role_activity b "
					+ " on a.gur_role_code = b.fra_role_code " + " WHERE  a.gur_status = 'A' "
					+ " and b.fra_function_code = ? " + " and b.fra_activity_code = 'E'" + " and a.gur_user_id = ? ;";

			ps = con.prepareStatement(sql);
			ps.setString(1, functionId);
			ps.setString(2, userName);

			rs = ps.executeQuery();

			while (rs.next()) {
				userActivity = rs.getString("fra_activity_code");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return userActivity;
	}

	// Get All Route Details
	public List<RouteDTO> getRouteDetailsbyLoginUser(String userId) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<RouteDTO> routeDetList = new ArrayList<RouteDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT seqno, rou_description, rou_number, "
					+ " rou_service_origine, rou_service_destination, rou_via, "
					+ " case when active = 'A' then 'Active' when active = 'I' then 'Inactive' when active='T' then 'Temporary' else active end, "
					+ " created_by, created_date,rou_distance, active as status_code, rou_tot_busfare, rou_service_origine_sin,"
					+ " rou_service_destination_sin, rou_via_sin , "
					+ " rou_service_origine_tamil, rou_service_destination_tamil, rou_via_tamil "
					+ " FROM public.nt_r_route " + " WHERE created_by = ? and active not in ('T') " + " ORDER BY rou_number";

			ps = con.prepareStatement(query);
			ps.setString(1, userId);
			rs = ps.executeQuery();

			while (rs.next()) {
				RouteDTO routeDTO = new RouteDTO();
				routeDTO.setSeq(rs.getLong("seqno"));
				routeDTO.setRouteDes(rs.getString("rou_description"));
				routeDTO.setRouteNo(rs.getString("rou_number"));
				routeDTO.setOrigin(rs.getString("rou_service_origine"));
				routeDTO.setDestination(rs.getString("rou_service_destination"));
				routeDTO.setVia(rs.getString("rou_via"));
				routeDTO.setStatus(rs.getString("active"));
				routeDTO.setCreatedBy(rs.getString("created_by"));
				routeDTO.setCratedDate(rs.getTimestamp("created_date"));
				routeDTO.setDistance(rs.getBigDecimal("rou_distance"));
				routeDTO.setStatusCode(rs.getString("status_code"));
				routeDTO.setBusFare(rs.getBigDecimal("rou_tot_busfare"));
				routeDTO.setOriginS(rs.getString("rou_service_origine_sin"));
				routeDTO.setDestinationS(rs.getString("rou_service_destination_sin"));
				routeDTO.setViaS(rs.getString("rou_via_sin"));
				routeDTO.setOriginT(rs.getString("rou_service_origine_tamil"));
				routeDTO.setDestinationT(rs.getString("rou_service_destination_tamil"));
				routeDTO.setViaT(rs.getString("rou_via_tamil"));
				routeDetList.add(routeDTO);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return routeDetList;

	}

	// Check Duplicate Route No.
	public String chkDuplicates(String routeNo) {

		String strRouteNo = null;
		Connection con;
		PreparedStatement ps = null;
		ResultSet rs = null;

		con = ConnectionManager.getConnection();
		try {

			String sql = "SELECT rou_number " + " FROM public.nt_r_route " + " WHERE rou_number = ? ;";

			ps = con.prepareStatement(sql);

			ps.setString(1, routeNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				strRouteNo = rs.getString("rou_number");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return strRouteNo;
	}

	// Get All Route Details by Route No
	public List<RouteDTO> getRouteDetailsByRouteNo(String routeNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<RouteDTO> routeDetList = new ArrayList<RouteDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT seqno, rou_description, rou_number, "
					+ " rou_service_origine, rou_service_destination, rou_via, "
					+ " case when active = 'A' then 'Active' when active = 'I' then 'Inactive' when active='T' then 'Temporary' else active end, "
					+ " created_by, created_date,rou_distance, active as status_code, rou_tot_busfare, rou_service_origine_sin,"
					+ " rou_service_destination_sin, rou_via_sin , "
					+ " rou_service_origine_tamil, rou_service_destination_tamil, rou_via_tamil  "
					+ " FROM public.nt_r_route " + " WHERE rou_number = ? and active not in ('T') ";

			ps = con.prepareStatement(query);
			ps.setString(1, routeNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				RouteDTO routeDTO = new RouteDTO();
				routeDTO.setSeq(rs.getLong("seqno"));
				routeDTO.setRouteDes(rs.getString("rou_description"));
				routeDTO.setRouteNo(rs.getString("rou_number"));
				routeDTO.setOrigin(rs.getString("rou_service_origine"));
				routeDTO.setDestination(rs.getString("rou_service_destination"));
				routeDTO.setVia(rs.getString("rou_via"));
				routeDTO.setStatus(rs.getString("active"));
				routeDTO.setCreatedBy(rs.getString("created_by"));
				routeDTO.setCratedDate(rs.getTimestamp("created_date"));
				routeDTO.setDistance(rs.getBigDecimal("rou_distance"));
				routeDTO.setStatusCode(rs.getString("status_code"));
				routeDTO.setBusFare(rs.getBigDecimal("rou_tot_busfare"));
				routeDTO.setOriginS(rs.getString("rou_service_origine_sin"));
				routeDTO.setDestinationS(rs.getString("rou_service_destination_sin"));
				routeDTO.setViaS(rs.getString("rou_via_sin"));
				routeDTO.setOriginT(rs.getString("rou_service_origine_tamil"));
				routeDTO.setDestinationT(rs.getString("rou_service_destination_tamil"));
				routeDTO.setViaT(rs.getString("rou_via_tamil"));
				routeDetList.add(routeDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return routeDetList;

	}

	// Get Route details By RouteNo
	public RouteDTO getDetailsbyRouteNoX(String routeNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		RouteDTO routeDTO = new RouteDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT a.rou_service_origine as origine,a.rou_service_destination as destination,a.rou_via as via, b.pm_route_flag as routeflag FROM public.nt_r_route a, nt_t_pm_application b WHERE a.rou_number = ? and a.rou_number=b.pm_route_no;";

			ps = con.prepareStatement(query);
			ps.setString(1, routeNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				routeDTO.setOrigin(rs.getString("origine"));
				routeDTO.setDestination(rs.getString("destination"));
				routeDTO.setVia(rs.getString("via"));
				routeDTO.setRouteFlagVal(rs.getString("routeflag"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return routeDTO;

	}

	// Get Route details By RouteNo
	public RouteDTO getDetailsbyRouteNo(String routeNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		RouteDTO routeDTO = new RouteDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT rou_service_origine,rou_service_destination,rou_via,rou_tot_busfare "
					+ " FROM public.nt_r_route " + " WHERE rou_number =  ? ";

			ps = con.prepareStatement(query);
			ps.setString(1, routeNo);
			rs = ps.executeQuery();

			while (rs.next()) {

				routeDTO.setOrigin(rs.getString("rou_service_origine"));
				routeDTO.setDestination(rs.getString("rou_service_destination"));
				routeDTO.setVia(rs.getString("rou_via"));
				// ---Add tot bus fare
				routeDTO.setBusFare(rs.getBigDecimal("rou_tot_busfare"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return routeDTO;

	}

	// Get Routes
	public List<CommonDTO> getRoutesToDropdown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT rou_number,rou_description " + " FROM public.nt_r_route "
					+ " WHERE active in('A','T') " + " ORDER BY rou_number";

			ps = con.prepareStatement(query);
			// ps.setString(1, LangId);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("rou_number"));
				commonDTO.setDescription(rs.getString("rou_description"));

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

	// Get Status
	public List<CommonDTO> getServiceTypeToDropdown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code,description " + " FROM public.nt_r_service_types " + " WHERE active = 'A' "
					+ " ORDER BY code ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("code"));
				commonDTO.setDescription(rs.getString("description"));

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

	// Get Province
	public List<CommonDTO> getProvinceToDropdown() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CommonDTO> returnList = new ArrayList<CommonDTO>();
		try {
			con = ConnectionManager.getConnection();
			String query = "SELECT code,description " + " FROM public.nt_r_province " + " WHERE active = 'A' "
					+ " ORDER BY description ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("code"));
				commonDTO.setDescription(rs.getString("description"));

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

	// Get District
	public List<CommonDTO> getDistrictToDropdown() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CommonDTO> returnList = new ArrayList<CommonDTO>();
		try {
			con = ConnectionManager.getConnection();
			String query = "SELECT code,description " + " FROM public.nt_r_district " + " WHERE active = 'A' "
					+ " ORDER BY description ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("code"));
				commonDTO.setDescription(rs.getString("description"));

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

	// Get DevSec
	public List<CommonDTO> getDivSecToDropdown() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CommonDTO> returnList = new ArrayList<CommonDTO>();
		try {
			con = ConnectionManager.getConnection();
			String query = "SELECT ds_code,ds_description " + " FROM public.nt_r_div_sec " + " WHERE ds_active = 'A' "
					+ " ORDER BY ds_description ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("ds_code"));
				commonDTO.setDescription(rs.getString("ds_description"));

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

	// Get District By Province
	public List<CommonDTO> getDistrictByProvinceToDropdown(String provinceCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CommonDTO> returnList = new ArrayList<CommonDTO>();
		try {
			con = ConnectionManager.getConnection();
			String query = " SELECT code,description " + " FROM public.nt_r_district " + " WHERE active = 'A' "
					+ " AND province_code = ? " + " ORDER BY description ";

			ps = con.prepareStatement(query);
			ps.setString(1, provinceCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("code"));
				commonDTO.setDescription(rs.getString("description"));

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

	// Get DevSec By District
	public List<CommonDTO> getDivSecByDistrictToDropdown(String districtCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CommonDTO> returnList = new ArrayList<CommonDTO>();
		try {
			con = ConnectionManager.getConnection();
			String query = " SELECT ds_code,ds_description " + " FROM nt_r_div_sec " + " WHERE ds_active = 'A' "
					+ " AND ds_district_code = ? " + " ORDER BY ds_description ";

			ps = con.prepareStatement(query);
			ps.setString(1, districtCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("ds_code"));
				commonDTO.setDescription(rs.getString("ds_description"));

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

	// Get Makes
	public List<CommonDTO> getMakesToDropdown() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CommonDTO> returnList = new ArrayList<CommonDTO>();
		try {
			con = ConnectionManager.getConnection();
			String query = "SELECT code,description " + " FROM public.nt_r_make " + " WHERE active = 'A' "
					+ " ORDER BY description ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("code"));
				commonDTO.setDescription(rs.getString("description"));

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

	// Get Models
	public List<CommonDTO> getModelsToDropdown() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CommonDTO> returnList = new ArrayList<CommonDTO>();
		try {
			con = ConnectionManager.getConnection();
			String query = "SELECT mod_code,mod_description " + " FROM public.nt_r_model " + " WHERE active = 'A' "
					+ " ORDER BY mod_description ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("mod_code"));
				commonDTO.setDescription(rs.getString("mod_description"));

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

	// Get Models By Make
	public List<CommonDTO> getModelsByMakeToDropdown(String make) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CommonDTO> returnList = new ArrayList<CommonDTO>();
		try {
			con = ConnectionManager.getConnection();
			String query = " SELECT mod_code,mod_description " + " FROM nt_r_model " + " WHERE active = 'A' "
					+ " AND mod_make_code = ? " + " ORDER BY mod_description ";

			ps = con.prepareStatement(query);
			ps.setString(1, make);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("mod_code"));
				commonDTO.setDescription(rs.getString("mod_description"));

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

	// Save Backlog Permit Details
	public int saveBacklogPermit(PermitDTO permitDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();

			String applicationNo = generateApplicationNo();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_pm_application");

			String sql = "INSERT INTO public.nt_t_pm_application"
					+ "(seq, pm_application_no, pm_permit_no, pm_vehicle_regno, pm_status,pm_valid_to, "
					+ " pm_permit_expire_date, pm_is_tender_permit, pm_service_type, pm_route_no, "
					+ " pm_tot_bus_fare, pm_is_backlog_app, pm_created_by, pm_created_date,pm_route_flag,pm_permit_issue_date,pm_special_remark)"
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, applicationNo);
			stmt.setString(3, permitDTO.getPermitNo());
			stmt.setString(4, permitDTO.getBusRegNo());
			stmt.setString(5, permitDTO.getStatus());

			if (permitDTO.getPermitEndDate() != null) {
				String permitEnd = (dateFormat.format(permitDTO.getPermitEndDate()));
				stmt.setString(6, permitEnd);
			} else {
				stmt.setString(6, null);
			}
			if (permitDTO.getPermitExpire() != null) {
				String permitExpire = (dateFormat.format(permitDTO.getPermitExpire()));
				stmt.setString(7, permitExpire);
			} else {
				stmt.setString(7, null);
			}

			stmt.setString(8, permitDTO.getIsTenderPermit());
			stmt.setString(9, permitDTO.getServiceType());
			stmt.setString(10, permitDTO.getRouteNo());
			stmt.setBigDecimal(11, permitDTO.getBusFare());
			stmt.setString(12, permitDTO.getIsBacklogApp());
			stmt.setString(13, permitDTO.getCreatedBy());
			stmt.setTimestamp(14, timestamp);
			stmt.setString(15, permitDTO.getRouteFlag());

			if (permitDTO.getPermitIssueDate() != null) {
				String permitIssueDate = (dateFormat.format(permitDTO.getPermitIssueDate()));
				stmt.setString(16, permitIssueDate);
			} else {
				stmt.setString(16, null);
			}
			stmt.setString(17, permitDTO.getRemarks());

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

	// Search By Permit No
	public PermitDTO searchByPermitNo(String permitNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		PermitDTO permitDTO = new PermitDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT a.pm_route_flag,a.seq,a.pm_permit_no,a.pm_vehicle_regno,a.pm_valid_to,a.pm_is_tender_permit,"
					+ " a.pm_service_type,a.pm_permit_expire_date, pm_route_no,a.pm_special_remark,a.pm_permit_issue_date, "
					+ " b.rou_service_origine,b.rou_service_destination,b.rou_via,a.pm_tot_bus_fare, a.pm_application_no  "
					+ " FROM nt_t_pm_application a inner join nt_r_route b on a.pm_route_no = b.rou_number "
					+ " WHERE a.pm_permit_no =  ? ";

			ps = con.prepareStatement(query);
			ps.setString(1, permitNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				permitDTO.setRouteFlag(rs.getString("pm_route_flag"));
				permitDTO.setSeq(rs.getLong("seq"));
				permitDTO.setPermitNo(rs.getString("pm_permit_no"));
				permitDTO.setBusRegNo(rs.getString("pm_vehicle_regno"));

				String permitEndDateString = rs.getString("pm_valid_to");
				if (permitEndDateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date permitEndDate = originalFormat.parse(permitEndDateString);
					permitDTO.setPermitEndDate(permitEndDate);
					permitDTO.setStrPermitEndDate(permitEndDateString);
				}

				permitDTO.setIsTenderPermit(rs.getString("pm_is_tender_permit"));
				permitDTO.setServiceType(rs.getString("pm_service_type"));
				String expiteDateString = rs.getString("pm_permit_expire_date");
				if (expiteDateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date expireEndDate = originalFormat.parse(expiteDateString);
					permitDTO.setPermitExpire(expireEndDate);
					permitDTO.setStrExpireDate(expiteDateString);
				}
				permitDTO.setRouteNo(rs.getString("pm_route_no"));
				permitDTO.setOrigin(rs.getString("rou_service_origine"));
				permitDTO.setDestination(rs.getString("rou_service_destination"));
				permitDTO.setVia(rs.getString("rou_via"));
				permitDTO.setBusFare(rs.getBigDecimal("pm_tot_bus_fare"));
				permitDTO.setApplicationNo(rs.getString("pm_application_no"));
				permitDTO.setRemarks(rs.getString("pm_special_remark"));
				
				String permitIssueDateString = rs.getString("pm_permit_issue_date");
				if (permitIssueDateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date permitIssueDate = originalFormat.parse(permitIssueDateString);
					permitDTO.setPermitIssueDate(permitIssueDate);
					permitDTO.setPermitIssueDateVal(permitIssueDateString);
				}
				 
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return permitDTO;

	}
	
	

	@Override
	public PermitDTO searchBySeq(long seq) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		PermitDTO permitDTO = new PermitDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT a.pm_route_flag,a.seq,a.pm_permit_no,a.pm_vehicle_regno,a.pm_valid_to,a.pm_is_tender_permit,"
					+ " a.pm_service_type,a.pm_permit_expire_date, pm_route_no,a.pm_special_remark,a.pm_permit_issue_date, "
					+ " b.rou_service_origine,b.rou_service_destination,b.rou_via,a.pm_tot_bus_fare, a.pm_application_no  "
					+ " FROM nt_t_pm_application a inner join nt_r_route b on a.pm_route_no = b.rou_number "
					+ " WHERE a.seq =  ? ";

			ps = con.prepareStatement(query);
			ps.setLong(1, seq);
			rs = ps.executeQuery();

			while (rs.next()) {
				permitDTO.setRouteFlag(rs.getString("pm_route_flag"));
				permitDTO.setSeq(rs.getLong("seq"));
				permitDTO.setPermitNo(rs.getString("pm_permit_no"));
				permitDTO.setBusRegNo(rs.getString("pm_vehicle_regno"));

				String permitEndDateString = rs.getString("pm_valid_to");
				if (permitEndDateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date permitEndDate = originalFormat.parse(permitEndDateString);
					permitDTO.setPermitEndDate(permitEndDate);
					permitDTO.setStrPermitEndDate(permitEndDateString);
				}

				permitDTO.setIsTenderPermit(rs.getString("pm_is_tender_permit"));
				permitDTO.setServiceType(rs.getString("pm_service_type"));
				String expiteDateString = rs.getString("pm_permit_expire_date");
				if (expiteDateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date expireEndDate = originalFormat.parse(expiteDateString);
					permitDTO.setPermitExpire(expireEndDate);
					permitDTO.setStrExpireDate(expiteDateString);
				}
				permitDTO.setRouteNo(rs.getString("pm_route_no"));
				permitDTO.setOrigin(rs.getString("rou_service_origine"));
				permitDTO.setDestination(rs.getString("rou_service_destination"));
				permitDTO.setVia(rs.getString("rou_via"));
				permitDTO.setBusFare(rs.getBigDecimal("pm_tot_bus_fare"));
				permitDTO.setApplicationNo(rs.getString("pm_application_no"));
				permitDTO.setRemarks(rs.getString("pm_special_remark"));
				
				String permitIssueDateString = rs.getString("pm_permit_issue_date");
				if (permitIssueDateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date permitIssueDate = originalFormat.parse(permitIssueDateString);
					permitDTO.setPermitIssueDate(permitIssueDate);
					permitDTO.setPermitIssueDateVal(permitIssueDateString);
				}
				 
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return permitDTO;
	}

	// Update Backlog Permit Details
	public int updateBacklogPermit(PermitDTO permitDTO) {

		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();

			String sql = " UPDATE public.nt_t_pm_application"
					+ " SET pm_permit_no = ?, pm_vehicle_regno = ? ,pm_is_tender_permit= ?, "
					+ " pm_permit_expire_date =?,pm_valid_to=? ,"
					+ " pm_service_type=?, pm_route_no=?, pm_tot_bus_fare=?, "
					+ " pm_modified_by=?, pm_modified_date=?,pm_route_flag=?,pm_special_remark=? " + " WHERE seq= ?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, permitDTO.getPermitNo());
			stmt.setString(2, permitDTO.getBusRegNo());
			stmt.setString(3, permitDTO.getIsTenderPermit());
			if (permitDTO.getPermitExpire() != null) {
				String expireDate = (dateFormat.format(permitDTO.getPermitExpire()));
				stmt.setString(4, expireDate);
			} else {
				stmt.setString(4, null);
			}
			if (permitDTO.getPermitEndDate() != null) {
				String endDate = (dateFormat.format(permitDTO.getPermitEndDate()));
				stmt.setString(5, endDate);
			} else {
				stmt.setString(5, null);
			}
			stmt.setString(6, permitDTO.getServiceType());
			stmt.setString(7, permitDTO.getRouteNo());
			stmt.setBigDecimal(8, permitDTO.getBusFare());
			stmt.setString(9, permitDTO.getModifiedBy());
			stmt.setTimestamp(10, timestamp);
			stmt.setString(11, permitDTO.getRouteFlag());
			stmt.setString(12, permitDTO.getRemarks());
			stmt.setLong(13, permitDTO.getSeq());
			stmt.executeUpdate();

			String sql1 = "UPDATE public.nt_t_pm_vehi_owner " + " SET pmo_permit_no = ? , pmo_vehicle_regno = ? "
					+ " WHERE pmo_application_no = ?; ";

			stmt1 = con.prepareStatement(sql1);
			stmt1.setString(1, permitDTO.getPermitNo());
			stmt1.setString(2, permitDTO.getBusRegNo());
			stmt1.setString(3, permitDTO.getApplicationNo());
			stmt1.executeUpdate();

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(rs1);
			ConnectionManager.close(stmt1);
			ConnectionManager.close(con);
		}

		return 0;
	}

	// Save Backlog Bus Owner Details
	public int saveBusOwnerDetails(BusOwnerDTO busOwnerDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_pm_vehi_owner");

			String sql = "INSERT INTO public.nt_t_pm_vehi_owner"
					+ "(seq, pmo_application_no, pmo_permit_no, pmo_vehicle_regno, "
					+ " pmo_preferred_language, pmo_title, pmo_full_name, pmo_full_name_sinhala, "
					+ " pmo_full_name_tamil, pmo_name_with_initial, pmo_nic, pmo_gender, pmo_dob, "
					+ " pmo_marital_status, pmo_telephone_no, pmo_mobile_no, pmo_province, pmo_district, "
					+ " pmo_div_sec, pmo_address1, pmo_address1_sinhala, pmo_address1_tamil, pmo_address2, "
					+ " pmo_address2_sinhala, pmo_address2_tamil, pmo_address3, pmo_address3_sinhala, "
					+ " pmo_address3_tamil, pmo_city, pmo_city_sinhala, pmo_city_tamil, "
					+ " pmo_is_backlog_app, pm_created_by, pm_created_date)"
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, busOwnerDTO.getApplicationNo());
			stmt.setString(3, busOwnerDTO.getPermitNo());
			stmt.setString(4, busOwnerDTO.getBusRegNo());
			stmt.setString(5, busOwnerDTO.getPerferedLanguage());
			stmt.setString(6, busOwnerDTO.getTitle());
			stmt.setString(7, busOwnerDTO.getFullName());
			stmt.setString(8, busOwnerDTO.getFullNameSinhala());
			stmt.setString(9, busOwnerDTO.getFullNameTamil());
			stmt.setString(10, busOwnerDTO.getNameWithInitials());
			stmt.setString(11, busOwnerDTO.getNicNo());
			stmt.setString(12, busOwnerDTO.getGender());
			if (busOwnerDTO.getDob() != null) {
				String dob = (dateFormat.format(busOwnerDTO.getDob()));
				stmt.setString(13, dob);
			} else {
				stmt.setString(13, null);
			}
			stmt.setString(14, busOwnerDTO.getMaritalStatus());
			stmt.setString(15, busOwnerDTO.getTelephoneNo());
			stmt.setString(16, busOwnerDTO.getMobileNo());
			stmt.setString(17, busOwnerDTO.getProvince());
			stmt.setString(18, busOwnerDTO.getDistrict());
			stmt.setString(19, busOwnerDTO.getDivSec());
			stmt.setString(20, busOwnerDTO.getAddress1());
			stmt.setString(21, busOwnerDTO.getAddress1Sinhala());
			stmt.setString(22, busOwnerDTO.getAddress1Tamil());
			stmt.setString(23, busOwnerDTO.getAddress2());
			stmt.setString(24, busOwnerDTO.getAddress2Sinhala());
			stmt.setString(25, busOwnerDTO.getAddress2Tamil());
			stmt.setString(26, busOwnerDTO.getAddress3());
			stmt.setString(27, busOwnerDTO.getAddress3Sinhala());
			stmt.setString(28, busOwnerDTO.getAddress3Tamil());
			stmt.setString(29, busOwnerDTO.getCity());
			stmt.setString(30, busOwnerDTO.getCitySinhala());
			stmt.setString(31, busOwnerDTO.getCityTamil());
			stmt.setString(32, busOwnerDTO.getIsBacklogApp());
			stmt.setString(33, busOwnerDTO.getCreatedBy());
			stmt.setTimestamp(34, timestamp);
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

	// Owner Details Search By Permit No
	public BusOwnerDTO ownerDetailsByPermitNo(String permitNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		BusOwnerDTO busOwnerDTO = new BusOwnerDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT seq,pmo_preferred_language,pmo_title,pmo_gender,pmo_dob,pmo_nic,pmo_full_name,"
					+ " pmo_full_name_sinhala, pmo_full_name_tamil,pmo_name_with_initial,pmo_marital_status,"
					+ " pmo_telephone_no,pmo_mobile_no,pmo_address1,pmo_address1_sinhala,pmo_address1_tamil,"
					+ " pmo_address2,pmo_address2_sinhala,pmo_address2_tamil,pmo_city,pmo_city_sinhala,"
					+ " pmo_city_tamil,pmo_province,pmo_district,pmo_div_sec  " + " FROM public.nt_t_pm_vehi_owner "
					+ " WHERE pmo_permit_no =  ? ";

			ps = con.prepareStatement(query);
			ps.setString(1, permitNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				busOwnerDTO.setSeq(rs.getLong("seq"));
				busOwnerDTO.setPerferedLanguage(rs.getString("pmo_preferred_language"));
				busOwnerDTO.setTitle(rs.getString("pmo_title"));
				busOwnerDTO.setGender(rs.getString("pmo_gender"));
				String dobString = rs.getString("pmo_dob");
				if (dobString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date dob = originalFormat.parse(dobString);
					busOwnerDTO.setDob(dob);
					busOwnerDTO.setStrStringDob(dobString);
				}
				busOwnerDTO.setNicNo(rs.getString("pmo_nic"));
				busOwnerDTO.setFullName(rs.getString("pmo_full_name"));
				busOwnerDTO.setFullNameSinhala(rs.getString("pmo_full_name_sinhala"));
				busOwnerDTO.setFullNameTamil(rs.getString("pmo_full_name_tamil"));
				busOwnerDTO.setNameWithInitials(rs.getString("pmo_name_with_initial"));
				busOwnerDTO.setMaritalStatus(rs.getString("pmo_marital_status"));
				busOwnerDTO.setTelephoneNo(rs.getString("pmo_telephone_no"));
				busOwnerDTO.setMobileNo(rs.getString("pmo_mobile_no"));
				busOwnerDTO.setAddress1(rs.getString("pmo_address1"));
				busOwnerDTO.setAddress1Sinhala(rs.getString("pmo_address1_sinhala"));
				busOwnerDTO.setAddress1Tamil(rs.getString("pmo_address1_tamil"));
				busOwnerDTO.setAddress2(rs.getString("pmo_address2"));
				busOwnerDTO.setAddress2Sinhala(rs.getString("pmo_address2_sinhala"));
				busOwnerDTO.setAddress2Tamil(rs.getString("pmo_address2_tamil"));
				busOwnerDTO.setCity(rs.getString("pmo_city"));
				busOwnerDTO.setCitySinhala(rs.getString("pmo_city_sinhala"));
				busOwnerDTO.setCityTamil(rs.getString("pmo_city_tamil"));
				busOwnerDTO.setProvince(rs.getString("pmo_province"));
				busOwnerDTO.setDistrict(rs.getString("pmo_district"));
				busOwnerDTO.setDivSec(rs.getString("pmo_div_sec"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return busOwnerDTO;

	}

	// Update Bus Owner Details
	public int updateBusOwner(BusOwnerDTO busOwnerDTO) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();

			String sql = " UPDATE public.nt_t_pm_vehi_owner "
					+ " SET pmo_preferred_language =?, pmo_title =?, pmo_full_name=?, pmo_full_name_sinhala =?, "
					+ " pmo_full_name_tamil=?, pmo_name_with_initial=?, pmo_nic=?, pmo_gender=?, pmo_dob =?, "
					+ " pmo_marital_status=?, pmo_telephone_no=?, pmo_mobile_no=?, pmo_province=?, pmo_district=?, "
					+ " pmo_div_sec=?, pmo_address1=?, pmo_address1_sinhala=?, pmo_address1_tamil=?, pmo_address2=?, "
					+ " pmo_address2_sinhala=?, pmo_address2_tamil=?, pmo_address3=?, pmo_address3_sinhala=?, "
					+ " pmo_address3_tamil=?, pmo_city=?, pmo_city_sinhala=?, pmo_city_tamil=?, pm_modified_by=?,pm_modified_date=? , pmo_permit_no = ? "
					+ " WHERE seq= ?";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, busOwnerDTO.getPerferedLanguage());
			stmt.setString(2, busOwnerDTO.getTitle());
			stmt.setString(3, busOwnerDTO.getFullName());
			stmt.setString(4, busOwnerDTO.getFullNameSinhala());
			stmt.setString(5, busOwnerDTO.getFullNameTamil());
			stmt.setString(6, busOwnerDTO.getNameWithInitials());
			stmt.setString(7, busOwnerDTO.getNicNo());
			stmt.setString(8, busOwnerDTO.getGender());
			if (busOwnerDTO.getDob() != null) {
				String dob = (dateFormat.format(busOwnerDTO.getDob()));
				stmt.setString(9, dob);
			} else {
				stmt.setString(9, null);
			}
			stmt.setString(10, busOwnerDTO.getMaritalStatus());
			stmt.setString(11, busOwnerDTO.getTelephoneNo());
			stmt.setString(12, busOwnerDTO.getMobileNo());
			stmt.setString(13, busOwnerDTO.getProvince());
			stmt.setString(14, busOwnerDTO.getDistrict());
			stmt.setString(15, busOwnerDTO.getDivSec());
			stmt.setString(16, busOwnerDTO.getAddress1());
			stmt.setString(17, busOwnerDTO.getAddress1Sinhala());
			stmt.setString(18, busOwnerDTO.getAddress1Tamil());
			stmt.setString(19, busOwnerDTO.getAddress2());
			stmt.setString(20, busOwnerDTO.getAddress2Sinhala());
			stmt.setString(21, busOwnerDTO.getAddress2Tamil());
			stmt.setString(22, busOwnerDTO.getAddress3());
			stmt.setString(23, busOwnerDTO.getAddress3Sinhala());
			stmt.setString(24, busOwnerDTO.getAddress3Tamil());
			stmt.setString(25, busOwnerDTO.getCity());
			stmt.setString(26, busOwnerDTO.getCitySinhala());
			stmt.setString(27, busOwnerDTO.getCityTamil());
			stmt.setString(28, busOwnerDTO.getModifiedBy());
			stmt.setTimestamp(29, timestamp);
			stmt.setString(30, busOwnerDTO.getPermitNo());
			stmt.setLong(31, busOwnerDTO.getSeq());

			stmt.executeUpdate();
			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return 0;
	}

	@Override
	public int saveBacklogOminiBusWithApplicationNo(OminiBusDTO ominiBusDTO, String generatedApllicationNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "INSERT INTO nt_t_pm_omini_bus1(seq, pmb_application_no, pmb_vehicle_regno, pmb_engine_no, pmb_chassis_no, pmb_make, pmb_model, pmb_seating_capacity, pmb_no_of_doors, pmb_height, pmb_production_date, pmb_weight, pmb_serial_no, pmb_certificate_date, pmb_garage_reg_no, pmb_insurance_company, pmb_insurance_cat, pmb_insurance_expire_date, pmb_bus_price, pmb_is_loan_obtain, pmb_finance_company, pmb_bank_loan_amt, pmb_due_amt, pmb_date_obtain, pmb_lapsed_installment, pmb_revenue_license_exp_date, pmb_first_reg_date, pmb_is_backlog_app, pmb_created_by, pmb_created_date, pmb_permit_no, pmb_policy_no, pmb_emission_test_exp_date, pmb_garage_name"
					+ "					)SELECT nextval('seq_nt_t_pm_omini_bus1'), "
					+ "				   ?, pmb_vehicle_regno, pmb_engine_no, ?, pmb_make, pmb_model, pmb_seating_capacity, pmb_no_of_doors, pmb_height, ?, pmb_weight, pmb_serial_no, pmb_certificate_date, pmb_garage_reg_no, pmb_insurance_company, pmb_insurance_cat, pmb_insurance_expire_date, pmb_bus_price, pmb_is_loan_obtain, pmb_finance_company, pmb_bank_loan_amt, pmb_due_amt, pmb_date_obtain, pmb_lapsed_installment, pmb_revenue_license_exp_date, pmb_first_reg_date, pmb_is_backlog_app, pmb_created_by, pmb_created_date, pmb_permit_no, pmb_policy_no, pmb_emission_test_exp_date, pmb_garage_name "
					+ "				FROM nt_t_pm_omini_bus1 WHERE pmb_application_no=?";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, generatedApllicationNo);
			stmt.setString(2, ominiBusDTO.getChassisNo());
			stmt.setString(3, ominiBusDTO.getManufactureDate());
			stmt.setString(4, ominiBusDTO.getApplicationNo());

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

	// Save Backlog Omnibus Details
	public int saveBacklogOminiBus(OminiBusDTO ominiBusDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_pm_omini_bus1");

			String sql = "INSERT INTO public.nt_t_pm_omini_bus1"
					+ "(seq, pmb_application_no, pmb_vehicle_regno, pmb_engine_no,"
					+ " pmb_chassis_no, pmb_make, pmb_model,pmb_production_date, "
					+ " pmb_first_reg_date, pmb_seating_capacity, pmb_no_of_doors,"
					+ " pmb_weight,pmb_is_backlog_app,pmb_permit_no,pmb_created_by, pmb_created_date)"
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, ominiBusDTO.getApplicationNo());
			stmt.setString(3, ominiBusDTO.getVehicleRegNo());
			stmt.setString(4, ominiBusDTO.getEngineNo());
			stmt.setString(5, ominiBusDTO.getChassisNo());
			stmt.setString(6, ominiBusDTO.getMake());
			stmt.setString(7, ominiBusDTO.getModel());
			stmt.setString(8, ominiBusDTO.getManufactureDate());

			if (ominiBusDTO.getRegistrationDate() != null) {
				String registrationDate = (dateFormat.format(ominiBusDTO.getRegistrationDate()));
				stmt.setString(9, registrationDate);
			} else {
				stmt.setString(9, null);
			}
			stmt.setString(10, ominiBusDTO.getSeating());
			stmt.setString(11, ominiBusDTO.getNoofDoors());
			stmt.setString(12, ominiBusDTO.getWeight());
			stmt.setString(13, ominiBusDTO.getIsBacklogApp());
			stmt.setString(14, ominiBusDTO.getPermitNo());
			stmt.setString(15, ominiBusDTO.getCreatedBy());
			stmt.setTimestamp(16, timestamp);

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

	// Omnibus Search By Permit No
	public OminiBusDTO ominiBusByVehicleNo(String vehicleNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		OminiBusDTO ominiBusDTO = new OminiBusDTO();
		try {
			con = ConnectionManager.getConnection();
			String query = "SELECT  seq,pmb_application_no, pmb_vehicle_regno, pmb_engine_no, "
					+ " pmb_chassis_no, pmb_make, pmb_model, pmb_production_date,pmb_first_reg_date, "
					+ " pmb_seating_capacity, pmb_no_of_doors, pmb_weight,pmb_is_backlog_app, pmb_permit_no  "
					+ " FROM public.nt_t_pm_omini_bus1 " + " WHERE pmb_vehicle_regno =  ? ";

			ps = con.prepareStatement(query);
			ps.setString(1, vehicleNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				ominiBusDTO.setSeq(rs.getLong("seq"));
				ominiBusDTO.setApplicationNo(rs.getString("pmb_application_no"));
				ominiBusDTO.setVehicleRegNo(rs.getString("pmb_vehicle_regno"));
				ominiBusDTO.setEngineNo(rs.getString("pmb_engine_no"));
				ominiBusDTO.setChassisNo(rs.getString("pmb_chassis_no"));
				ominiBusDTO.setMake(rs.getString("pmb_make"));
				ominiBusDTO.setModel(rs.getString("pmb_model"));

				String productionDateString = rs.getString("pmb_production_date");
				if (productionDateString != null) {
					ominiBusDTO.setManufactureDate(productionDateString);
					ominiBusDTO.setStrStringManufactureDate(productionDateString);

				}
				String regDateString = rs.getString("pmb_first_reg_date");
				if (regDateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date regDate = originalFormat.parse(regDateString);
					ominiBusDTO.setRegistrationDate(regDate);
					ominiBusDTO.setStrStringRegistrationDate(regDateString);

				}
				ominiBusDTO.setSeating(rs.getString("pmb_seating_capacity"));
				ominiBusDTO.setNoofDoors(rs.getString("pmb_no_of_doors"));
				ominiBusDTO.setWeight(rs.getString("pmb_weight"));
				ominiBusDTO.setIsBacklogApp(rs.getString("pmb_is_backlog_app"));
				ominiBusDTO.setPermitNo(rs.getString("pmb_permit_no"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return ominiBusDTO;

	}

	// Update Omnibus Details
	public int updateOminiBus(OminiBusDTO ominiBusDTO) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();

			String sql = " UPDATE public.nt_t_pm_omini_bus1 " + " SET pmb_engine_no=?,"
					+ " pmb_chassis_no=?, pmb_make=?, pmb_model=?,pmb_production_date=?, "
					+ " pmb_first_reg_date=?, pmb_seating_capacity=?, pmb_no_of_doors=?,"
					+ " pmb_weight=?, pmb_modified_by=?,pmb_modified_date=?, pmb_permit_no=? " + " WHERE seq= ?";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, ominiBusDTO.getEngineNo());
			stmt.setString(2, ominiBusDTO.getChassisNo());
			stmt.setString(3, ominiBusDTO.getMake());
			stmt.setString(4, ominiBusDTO.getModel());
			stmt.setString(5, ominiBusDTO.getManufactureDate());

			if (ominiBusDTO.getRegistrationDate() != null) {
				String registrationDate = (dateFormat.format(ominiBusDTO.getRegistrationDate()));
				stmt.setString(6, registrationDate);
			} else {
				stmt.setString(6, null);
			}
			stmt.setString(7, ominiBusDTO.getSeating());
			stmt.setString(8, ominiBusDTO.getNoofDoors());
			stmt.setString(9, ominiBusDTO.getWeight());

			stmt.setString(10, ominiBusDTO.getModifiedBy());
			stmt.setTimestamp(11, timestamp);
			stmt.setString(12, ominiBusDTO.getPermitNo());
			stmt.setLong(13, ominiBusDTO.getSeq());

			stmt.executeUpdate();
			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return 0;
	}

	// Save BacklogPayment Details
	public int saveBacklogPayments(PermitPaymentDTO permitPaymentDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_pm_payment");

			String sql = "INSERT INTO public.nt_t_pm_payment"
					+ "(seq, pay_application_no, pay_vehicle_regno, pay_permit_no, pay_tot_permit_amt,"
					+ " pay_excess_amt, pay_special_remark, pay_renewal_amt, pay_penalty_amt, "
					+ " pay_tender_fee, pay_service_fee, pay_other_fee, pay_total_fee, "
					+ " pay_is_backlog_app, pay_created_by, pay_created_date)"
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, permitPaymentDTO.getApplicationNo());
			stmt.setString(3, permitPaymentDTO.getVehicleRegNo());
			stmt.setString(4, permitPaymentDTO.getPermitNo());
			stmt.setBigDecimal(5, permitPaymentDTO.getTotalPermitAmt());
			stmt.setBigDecimal(6, permitPaymentDTO.getExcessAmt());
			stmt.setString(7, permitPaymentDTO.getSpecialRemark());

			stmt.setBigDecimal(8, permitPaymentDTO.getRenewalAmt());
			stmt.setBigDecimal(9, permitPaymentDTO.getPenaltyAmt());
			stmt.setBigDecimal(10, permitPaymentDTO.getTenderFee());
			stmt.setBigDecimal(11, permitPaymentDTO.getServiceFee());
			stmt.setBigDecimal(12, permitPaymentDTO.getOtherFee());
			stmt.setBigDecimal(13, permitPaymentDTO.getTotalFee());

			stmt.setString(14, permitPaymentDTO.getIsBacklogApp());
			stmt.setString(15, permitPaymentDTO.getCreatedBy());
			stmt.setTimestamp(16, timestamp);

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

	// Payment Search By Permit No
	public PermitPaymentDTO paymentBypermitNo(String permitNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		PermitPaymentDTO permitPaymentDTO = new PermitPaymentDTO();
		try {
			con = ConnectionManager.getConnection();
			String query = "SELECT  seq, pay_application_no, pay_vehicle_regno, pay_permit_no,"
					+ " pay_tot_permit_amt, pay_excess_amt, "
					+ " pay_special_remark, pay_renewal_amt, pay_penalty_amt, pay_tender_fee, "
					+ " pay_service_fee, pay_other_fee, pay_total_fee, pay_is_backlog_app  "
					+ " FROM public.nt_t_pm_payment " + " WHERE pay_permit_no =  ? ";

			ps = con.prepareStatement(query);
			ps.setString(1, permitNo);
			rs = ps.executeQuery();

			while (rs.next()) {

				permitPaymentDTO.setSeq(rs.getLong("seq"));
				permitPaymentDTO.setApplicationNo(rs.getString("pay_application_no"));
				permitPaymentDTO.setVehicleRegNo(rs.getString("pay_vehicle_regno"));
				permitPaymentDTO.setPermitNo(rs.getString("pay_permit_no"));
				permitPaymentDTO.setTotalPermitAmt(rs.getBigDecimal("pay_tot_permit_amt"));
				permitPaymentDTO.setExcessAmt(rs.getBigDecimal("pay_excess_amt"));
				permitPaymentDTO.setSpecialRemark(rs.getString("pay_special_remark"));
				permitPaymentDTO.setRenewalAmt(rs.getBigDecimal("pay_renewal_amt"));
				permitPaymentDTO.setPenaltyAmt(rs.getBigDecimal("pay_penalty_amt"));
				permitPaymentDTO.setTenderFee(rs.getBigDecimal("pay_tender_fee"));
				permitPaymentDTO.setServiceFee(rs.getBigDecimal("pay_service_fee"));
				permitPaymentDTO.setOtherFee(rs.getBigDecimal("pay_other_fee"));
				permitPaymentDTO.setTotalFee(rs.getBigDecimal("pay_total_fee"));
				permitPaymentDTO.setIsBacklogApp(rs.getString("pay_is_backlog_app"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return permitPaymentDTO;

	}

	// Update BacklogPayment Details
	public int updateBacklogPayments(PermitPaymentDTO permitPaymentDTO) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = " UPDATE public.nt_t_pm_payment " + " SET pay_tot_permit_amt=?,"
					+ " pay_excess_amt=?, pay_special_remark=?, pay_renewal_amt=?, pay_penalty_amt=?, "
					+ " pay_tender_fee=?, pay_service_fee=?, pay_other_fee=?, "
					+ " pay_total_fee=?,pay_modified_by=?,pay_modified_date=? " + " WHERE seq= ?;";

			stmt = con.prepareStatement(sql);

			stmt.setBigDecimal(1, permitPaymentDTO.getTotalPermitAmt());
			stmt.setBigDecimal(2, permitPaymentDTO.getExcessAmt());
			stmt.setString(3, permitPaymentDTO.getSpecialRemark());

			stmt.setBigDecimal(4, permitPaymentDTO.getRenewalAmt());
			stmt.setBigDecimal(5, permitPaymentDTO.getPenaltyAmt());
			stmt.setBigDecimal(6, permitPaymentDTO.getTenderFee());
			stmt.setBigDecimal(7, permitPaymentDTO.getServiceFee());
			stmt.setBigDecimal(8, permitPaymentDTO.getOtherFee());
			stmt.setBigDecimal(9, permitPaymentDTO.getTotalFee());
			stmt.setString(10, permitPaymentDTO.getModifiedBy());
			stmt.setTimestamp(11, timestamp);
			stmt.setLong(12, permitPaymentDTO.getSeq());

			stmt.executeUpdate();
			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return 0;
	}

	// Check Vehicle No
	public String checkVehiNo(String vehicleNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT pm_permit_no " + " FROM public.nt_t_pm_application " + " WHERE pm_vehicle_regno = ? "
					+ " AND pm_status in ('A','P','O')";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, vehicleNo);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("pm_permit_no");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return result;
	}

	// Check duplicate permits
	public String checkDuplicatePermitNo(String permitNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT pm_permit_no " + " FROM public.nt_t_pm_application " + " WHERE pm_permit_no = ? ";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, permitNo);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("pm_permit_no");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return result;
	}

	public String generateApplicationNo() {
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

			String sql = "SELECT pm_application_no "
					+ " FROM public.nt_t_pm_application WHERE pm_application_no LIKE 'BAP%'"
					+ " ORDER BY pm_created_date desc " + " LIMIT 1";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("pm_application_no");
			}

			if (result != null) {

				lastyr = result.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String ApprecordcountN = "00001";
					strAppNo = "BAP" + currYear + ApprecordcountN;
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
					strAppNo = "BAP" + currYear + ApprecordcountN;
				}
			} else
				strAppNo = "BAP" + currYear + "00001";

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return strAppNo;
	}

	// Get Active PermitNos
	public List<String> getAllActivePermits() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> data = new ArrayList<String>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT pm_permit_no FROM nt_t_pm_application WHERE pm_permit_no IS NOT NULL AND pm_status = 'A' ORDER BY pm_permit_no";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				data.add(rs.getString("pm_permit_no"));

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

	// Get Active ApplicationNos
	public List<String> getAllActiveApplications() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> data = new ArrayList<String>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT pm_application_no FROM nt_t_pm_application WHERE pm_application_no IS NOT NULL AND pm_status = 'A' ORDER BY pm_application_no";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				data.add(rs.getString("pm_application_no"));

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

	// Get Active BusRegNos
	public List<String> getAllActiveBusRegNos() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> data = new ArrayList<String>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT pm_vehicle_regno FROM nt_t_pm_application WHERE pm_vehicle_regno IS NOT NULL AND pm_status = 'A' ORDER BY pm_vehicle_regno";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				data.add(rs.getString("pm_vehicle_regno"));

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

	public PermitDTO searchByPermitNo(String permitNo, String appNo, String busRegNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String WHERE_SQL = "";
		if (permitNo != null && !permitNo.isEmpty()) {
			WHERE_SQL = WHERE_SQL + " AND a.pm_permit_no = " + "'" + permitNo + "'";
		}
		if (appNo != null && !appNo.isEmpty()) {
			WHERE_SQL = WHERE_SQL + " AND a.pm_application_no = " + "'" + appNo + "'";
		}
		if (busRegNo != null && !busRegNo.isEmpty()) {
			WHERE_SQL = WHERE_SQL + " AND a.pm_vehicle_regno = " + "'" + busRegNo + "'";
		}

		PermitDTO permitDTO = new PermitDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT a.pm_route_flag,a.pm_permit_issue_date,a.seq,a.pm_permit_no,a.pm_vehicle_regno,a.pm_valid_to,a.pm_is_tender_permit,"
					+ " a.pm_service_type,a.pm_permit_expire_date, pm_route_no,a.pm_special_remark, "
					+ " b.rou_service_origine,b.rou_service_destination,b.rou_via,a.pm_tot_bus_fare, a.pm_application_no  "
					+ " FROM nt_t_pm_application a ,nt_r_route b " + " WHERE a.pm_route_no = b.rou_number " + WHERE_SQL;

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				permitDTO.setRouteFlag(rs.getString("pm_route_flag"));
				permitDTO.setSeq(rs.getLong("seq"));
				permitDTO.setPermitNo(rs.getString("pm_permit_no"));
				permitDTO.setBusRegNo(rs.getString("pm_vehicle_regno"));

				String permitEndDateString = rs.getString("pm_valid_to");
				if (permitEndDateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date permitEndDate = originalFormat.parse(permitEndDateString);
					permitDTO.setPermitEndDate(permitEndDate);

					permitDTO.setStrPermitEndDate(permitEndDateString);

				}

				String permitIssueDate = rs.getString("pm_permit_issue_date");
				if (permitIssueDate != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date permitIssued = originalFormat.parse(permitIssueDate);
					permitDTO.setPermitIssueDate(permitIssued);

				}

				permitDTO.setIsTenderPermit(rs.getString("pm_is_tender_permit"));
				permitDTO.setServiceType(rs.getString("pm_service_type"));
				String expiteDateString = rs.getString("pm_permit_expire_date");
				if (expiteDateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date expireEndDate = originalFormat.parse(expiteDateString);
					permitDTO.setPermitExpire(expireEndDate);

					permitDTO.setStrExpireDate(expiteDateString);

				}
				permitDTO.setRouteNo(rs.getString("pm_route_no"));
				permitDTO.setOrigin(rs.getString("rou_service_origine"));
				permitDTO.setDestination(rs.getString("rou_service_destination"));
				permitDTO.setVia(rs.getString("rou_via"));
				permitDTO.setBusFare(rs.getBigDecimal("pm_tot_bus_fare"));
				permitDTO.setApplicationNo(rs.getString("pm_application_no"));
				permitDTO.setRemarks(rs.getString("pm_special_remark"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return permitDTO;

	}

	/* Edit,View Attorney Holder */

	// Get Active PermitNos
	public List<String> getAllActivePermitsforAttorney() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> data = new ArrayList<String>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT ath_permit_no FROM public.nt_m_atterny_holder WHERE ath_status = 'A' ORDER BY ath_permit_no";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				data.add(rs.getString("ath_permit_no"));

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

	// Get Active ApplicationNos
	public List<String> getAllActiveApplicationsforAttorney() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> data = new ArrayList<String>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT ath_application_no FROM public.nt_m_atterny_holder WHERE ath_status = 'A' ORDER BY ath_application_no";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				data.add(rs.getString("ath_application_no"));

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

	// Get Active BusRegNos
	public List<String> getAllActiveBusRegNosforAttorney() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> data = new ArrayList<String>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT ath_vehicle_regno FROM public.nt_m_atterny_holder WHERE ath_status = 'A' ORDER BY ath_vehicle_regno";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				data.add(rs.getString("ath_vehicle_regno"));

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

	// Search By Permit No,Vehicle No, Application No
	public AttorneyHolderDTO searchAttorneyDetails(String permitNo, String applicationNo, String vehicleNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String WHERE_SQL = "";
		boolean whereadded = false;
		if (permitNo != null && !permitNo.equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE ath_permit_no = " + "'" + permitNo + "'";
			whereadded = true;
		}
		if (applicationNo != null && !applicationNo.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND ath_application_no = " + "'" + applicationNo + "'";
			} else {
				WHERE_SQL = WHERE_SQL + " WHERE ath_application_no = " + "'" + applicationNo + "'";
				whereadded = true;
			}
		}
		if (vehicleNo != null && !vehicleNo.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND ath_vehicle_regno = " + "'" + vehicleNo + "'";
			} else {
				WHERE_SQL = WHERE_SQL + " WHERE ath_vehicle_regno = " + "'" + vehicleNo + "'";
				whereadded = true;
			}
		}

		AttorneyHolderDTO attorneyHolderDTO = new AttorneyHolderDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT seq,ath_permit_no, ath_application_no, ath_vehicle_regno,"
					+ " ath_preferred_lan, ath_curr_owner_full_name, ath_curr_owner_add_1, "
					+ " ath_curr_owner_add_2, ath_curr_owner_city, ath_title, ath_gender, "
					+ " ath_dob, ath_nic_no, ath_fullname, ath_full_name_sinhala, ath_full_name_tamil,"
					+ " ath_name_with_initial, ath_tel_no, ath_mobile_no, "
					+ " ath_status, ath_address1, ath_address1_sinhala, ath_address1_tamil, "
					+ " ath_address2, ath_address2_sinhala, ath_address2_tamil," + " ath_city, ath_city_sinhala, "
					+ " ath_city_tamil,ath_valid_from, ath_valid_to" + " FROM public.nt_m_atterny_holder " + WHERE_SQL
					+ " and ath_status='A' ;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				attorneyHolderDTO.setSeq(rs.getLong("seq"));
				attorneyHolderDTO.setPermit_No(rs.getString("ath_permit_no"));
				attorneyHolderDTO.setApplication_No(rs.getString("ath_application_no"));
				attorneyHolderDTO.setVehicle_No(rs.getString("ath_vehicle_regno"));
				attorneyHolderDTO.setLanguage(rs.getString("ath_preferred_lan"));
				attorneyHolderDTO.setCo_Name(rs.getString("ath_curr_owner_full_name"));
				attorneyHolderDTO.setCo_Address1(rs.getString("ath_curr_owner_add_1"));
				attorneyHolderDTO.setCo_Address2(rs.getString("ath_curr_owner_add_2"));
				attorneyHolderDTO.setCo_City(rs.getString("ath_curr_owner_city"));
				attorneyHolderDTO.setTitle(rs.getString("ath_title"));
				attorneyHolderDTO.setGender(rs.getString("ath_gender"));
				String dobString = rs.getString("ath_dob");
				if (dobString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date dob = originalFormat.parse(dobString);
					attorneyHolderDTO.setDob(dob);
					attorneyHolderDTO.setStrDob(dobString);
				}
				attorneyHolderDTO.setPa_Nic_or_Org(rs.getString("ath_nic_no"));
				attorneyHolderDTO.setPa_FullName_Eng(rs.getString("ath_fullname"));
				attorneyHolderDTO.setPa_FullName_Sin(rs.getString("ath_full_name_sinhala"));
				attorneyHolderDTO.setPa_FullName_Tamil(rs.getString("ath_full_name_tamil"));
				attorneyHolderDTO.setPa_NameWithInitial(rs.getString("ath_name_with_initial"));
				attorneyHolderDTO.setPa_TeleNum(rs.getString("ath_tel_no"));
				attorneyHolderDTO.setPa_MobileNum(rs.getString("ath_mobile_no"));
				attorneyHolderDTO.setStatus(rs.getString("ath_status"));
				attorneyHolderDTO.setPa_Address1_Eng(rs.getString("ath_address1"));
				attorneyHolderDTO.setPa_Address1_Sin(rs.getString("ath_address1_sinhala"));
				attorneyHolderDTO.setPa_Address1_Tamil(rs.getString("ath_address1_tamil"));
				attorneyHolderDTO.setPa_Address2_Eng(rs.getString("ath_address2"));
				attorneyHolderDTO.setPa_Address2_Sin(rs.getString("ath_address2_sinhala"));
				attorneyHolderDTO.setPa_Address2_Tamil(rs.getString("ath_address2_tamil"));
				attorneyHolderDTO.setPa_City_Eng(rs.getString("ath_city"));
				attorneyHolderDTO.setPa_City_Sin(rs.getString("ath_city_sinhala"));
				attorneyHolderDTO.setPa_City_Tamil(rs.getString("ath_city_tamil"));

				String validFromString = rs.getString("ath_valid_from");
				if (validFromString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date validFrom = originalFormat.parse(validFromString);
					attorneyHolderDTO.setValidFrom(validFrom);

					attorneyHolderDTO.setStrValidFrom(validFromString);

				}

				String validToString = rs.getString("ath_valid_to");
				if (validToString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date validTo = originalFormat.parse(validToString);
					attorneyHolderDTO.setValidTo(validTo);

					attorneyHolderDTO.setStrValidTo(validToString);

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return attorneyHolderDTO;

	}

	// Update Attorney Holder
	public int updateAttorneyHolder(AttorneyHolderDTO attorneyHolderDTO) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_atterny_holder "
					+ " SET ath_preferred_lan=?, ath_fullname=?, ath_full_name_sinhala=?, "
					+ " ath_full_name_tamil=?, ath_name_with_initial=?, ath_tel_no=?, ath_mobile_no=?, "
					+ " ath_status=?, ath_address1=?, ath_address1_sinhala=?, ath_address1_tamil=?, "
					+ " ath_address2=?, ath_address2_sinhala=?, ath_address2_tamil=?, ath_city=?,"
					+ " ath_city_sinhala=?, ath_city_tamil=?, ath_valid_from=?, ath_valid_to=?, "
					+ " ath_modified_date=?, ath_modified_by=?" + " WHERE seq=?;";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, attorneyHolderDTO.getLanguage());
			stmt.setString(2, attorneyHolderDTO.getPa_FullName_Eng());
			stmt.setString(3, attorneyHolderDTO.getPa_FullName_Sin());
			stmt.setString(4, attorneyHolderDTO.getPa_FullName_Tamil());
			stmt.setString(5, attorneyHolderDTO.getPa_NameWithInitial());
			stmt.setString(6, attorneyHolderDTO.getPa_TeleNum());
			stmt.setString(7, attorneyHolderDTO.getPa_MobileNum());
			stmt.setString(8, attorneyHolderDTO.getStatus());
			stmt.setString(9, attorneyHolderDTO.getPa_Address1_Eng());
			stmt.setString(10, attorneyHolderDTO.getPa_Address1_Sin());
			stmt.setString(11, attorneyHolderDTO.getPa_Address1_Tamil());
			stmt.setString(12, attorneyHolderDTO.getPa_Address2_Eng());
			stmt.setString(13, attorneyHolderDTO.getPa_Address2_Sin());
			stmt.setString(14, attorneyHolderDTO.getPa_Address2_Tamil());
			stmt.setString(15, attorneyHolderDTO.getPa_City_Eng());
			stmt.setString(16, attorneyHolderDTO.getPa_City_Sin());
			stmt.setString(17, attorneyHolderDTO.getPa_City_Tamil());
			if (attorneyHolderDTO.getValidFrom() != null) {
				String validFrom = (dateFormat.format(attorneyHolderDTO.getValidFrom()));
				stmt.setString(18, validFrom);
			} else {
				stmt.setString(18, null);
			}

			if (attorneyHolderDTO.getValidTo() != null) {
				String validTo = (dateFormat.format(attorneyHolderDTO.getValidTo()));
				stmt.setString(19, validTo);
			} else {
				stmt.setString(19, null);
			}

			stmt.setTimestamp(20, timestamp);
			stmt.setString(21, attorneyHolderDTO.getModifiedBy());
			stmt.setLong(22, attorneyHolderDTO.getSeq());

			stmt.executeUpdate();
			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return 0;
	}

	@Override
	public BusOwnerDTO ownerDetailsByNicNo(String nicNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		BusOwnerDTO busOwnerDTO = new BusOwnerDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT pmo_preferred_language,pmo_title,pmo_gender,pmo_dob,pmo_nic,pmo_full_name,"
					+ " pmo_full_name_sinhala, pmo_full_name_tamil,pmo_name_with_initial,pmo_marital_status,"
					+ " pmo_telephone_no,pmo_mobile_no,pmo_address1,pmo_address1_sinhala,pmo_address1_tamil,"
					+ " pmo_address2,pmo_address2_sinhala,pmo_address2_tamil,pmo_city,pmo_city_sinhala,"
					+ " pmo_city_tamil,pmo_province,pmo_district,pmo_div_sec  " + " FROM public.nt_t_pm_vehi_owner "
					+ " WHERE pmo_nic =  ? ORDER BY seq desc limit 1 ";

			ps = con.prepareStatement(query);
			ps.setString(1, nicNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				busOwnerDTO.setPerferedLanguage(rs.getString("pmo_preferred_language"));
				busOwnerDTO.setTitle(rs.getString("pmo_title"));
				busOwnerDTO.setGender(rs.getString("pmo_gender"));
				String dobString = rs.getString("pmo_dob");
				if (dobString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date dob = originalFormat.parse(dobString);
					busOwnerDTO.setDob(dob);
					busOwnerDTO.setStrStringDob(dobString);
				}
				busOwnerDTO.setNicNo(rs.getString("pmo_nic"));
				busOwnerDTO.setFullName(rs.getString("pmo_full_name"));
				busOwnerDTO.setFullNameSinhala(rs.getString("pmo_full_name_sinhala"));
				busOwnerDTO.setFullNameTamil(rs.getString("pmo_full_name_tamil"));
				busOwnerDTO.setNameWithInitials(rs.getString("pmo_name_with_initial"));
				busOwnerDTO.setMaritalStatus(rs.getString("pmo_marital_status"));
				busOwnerDTO.setTelephoneNo(rs.getString("pmo_telephone_no"));
				busOwnerDTO.setMobileNo(rs.getString("pmo_mobile_no"));
				busOwnerDTO.setAddress1(rs.getString("pmo_address1"));
				busOwnerDTO.setAddress1Sinhala(rs.getString("pmo_address1_sinhala"));
				busOwnerDTO.setAddress1Tamil(rs.getString("pmo_address1_tamil"));
				busOwnerDTO.setAddress2(rs.getString("pmo_address2"));
				busOwnerDTO.setAddress2Sinhala(rs.getString("pmo_address2_sinhala"));
				busOwnerDTO.setAddress2Tamil(rs.getString("pmo_address2_tamil"));
				busOwnerDTO.setCity(rs.getString("pmo_city"));
				busOwnerDTO.setCitySinhala(rs.getString("pmo_city_sinhala"));
				busOwnerDTO.setCityTamil(rs.getString("pmo_city_tamil"));
				busOwnerDTO.setProvince(rs.getString("pmo_province"));
				busOwnerDTO.setDistrict(rs.getString("pmo_district"));
				busOwnerDTO.setDivSec(rs.getString("pmo_div_sec"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return busOwnerDTO;
	}

	@Override
	public String getParaCodeForTitle() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String data = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT param_name, string_value FROM public.nt_r_parameters WHERE param_name = 'BACKLOG_TITLE_CODE';";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				data = rs.getString("string_value");
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

	public String getApplicationNoFromPermitNo(String permitNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT pm_application_no FROM public.nt_t_pm_application where pm_status='A' and pm_permit_no='"
					+ permitNo + "'";

			stmt = con.prepareStatement(sql);

			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("pm_application_no");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return result;
	}

	public String getBusNoFromPermitNo(String permitNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT pm_vehicle_regno FROM public.nt_t_pm_application where pm_status ='A' and pm_permit_no='"
					+ permitNo + "'";

			stmt = con.prepareStatement(sql);

			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("pm_vehicle_regno");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return result;
	}

	public String getPermitNoFromAppNo(String appNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT pm_permit_no FROM public.nt_t_pm_application where pm_application_no='" + appNo + "'";

			stmt = con.prepareStatement(sql);

			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("pm_permit_no");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return result;
	}

	public String getBusNoFromAppNo(String appNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT pm_vehicle_regno FROM public.nt_t_pm_application where pm_application_no='" + appNo
					+ "'";

			stmt = con.prepareStatement(sql);

			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("pm_vehicle_regno");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return result;
	}

	public String getPermitNoFromBusNo(String busNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT pm_permit_no FROM public.nt_t_pm_application where pm_vehicle_regno='" + busNo + "'";

			stmt = con.prepareStatement(sql);

			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("pm_permit_no");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return result;
	}

	public String getAppNoFromBusNo(String busNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT pm_application_no FROM public.nt_t_pm_application where pm_vehicle_regno='" + busNo
					+ "'";

			stmt = con.prepareStatement(sql);

			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("pm_application_no");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return result;
	}

	@Override
	public Long checkDuplicatePermitNo_Edit(String permitNo, Long seq) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Long result = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT seq " + " FROM public.nt_t_pm_application " + " WHERE pm_permit_no = ? and seq!=?";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, permitNo);
			stmt.setLong(2, seq);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getLong("seq");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return result;
	}

	@Override
	public String checkVehiNo_Edit(String busNo, Long seq) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT pm_vehicle_regno " + " FROM public.nt_t_pm_application "
					+ " WHERE pm_vehicle_regno = ? " + " AND pm_status = 'A' AND seq !=?";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, busNo);
			stmt.setLong(2, seq);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("pm_vehicle_regno");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return result;
	}

	@Override
	public int updateBacklogOminiBusRegNo(PermitDTO permitDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_pm_omini_bus1 SET  pmb_vehicle_regno=?, pmb_permit_no=? WHERE pmb_application_no=?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, permitDTO.getBusRegNo());
			stmt.setString(2, permitDTO.getPermitNo());
			stmt.setString(3, permitDTO.getApplicationNo());
			stmt.executeUpdate();
			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return 0;
	}

	@Override
	public BusOwnerDTO ownerDetailsByApplicationNo(String strSelectedApplicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		BusOwnerDTO busOwnerDTO = new BusOwnerDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT seq,pmo_preferred_language,pmo_title,pmo_gender,pmo_dob,pmo_nic,pmo_full_name,"
					+ " pmo_full_name_sinhala, pmo_full_name_tamil,pmo_name_with_initial,pmo_marital_status,"
					+ " pmo_telephone_no,pmo_mobile_no,pmo_address1,pmo_address1_sinhala,pmo_address1_tamil,"
					+ " pmo_address2,pmo_address2_sinhala,pmo_address2_tamil,pmo_city,pmo_city_sinhala,"
					+ " pmo_city_tamil,pmo_province,pmo_district,pmo_div_sec  " + " FROM public.nt_t_pm_vehi_owner "
					+ " WHERE pmo_application_no =  ? ";

			ps = con.prepareStatement(query);
			ps.setString(1, strSelectedApplicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				busOwnerDTO.setSeq(rs.getLong("seq"));
				busOwnerDTO.setPerferedLanguage(rs.getString("pmo_preferred_language"));
				busOwnerDTO.setTitle(rs.getString("pmo_title"));
				busOwnerDTO.setGender(rs.getString("pmo_gender"));
				String dobString = rs.getString("pmo_dob");
				if (dobString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date dob = originalFormat.parse(dobString);
					busOwnerDTO.setDob(dob);

					busOwnerDTO.setStrStringDob(dobString);

				}
				busOwnerDTO.setNicNo(rs.getString("pmo_nic"));
				busOwnerDTO.setFullName(rs.getString("pmo_full_name"));
				busOwnerDTO.setFullNameSinhala(rs.getString("pmo_full_name_sinhala"));
				busOwnerDTO.setFullNameTamil(rs.getString("pmo_full_name_tamil"));
				busOwnerDTO.setNameWithInitials(rs.getString("pmo_name_with_initial"));
				busOwnerDTO.setMaritalStatus(rs.getString("pmo_marital_status"));
				busOwnerDTO.setTelephoneNo(rs.getString("pmo_telephone_no"));
				busOwnerDTO.setMobileNo(rs.getString("pmo_mobile_no"));
				busOwnerDTO.setAddress1(rs.getString("pmo_address1"));
				busOwnerDTO.setAddress1Sinhala(rs.getString("pmo_address1_sinhala"));
				busOwnerDTO.setAddress1Tamil(rs.getString("pmo_address1_tamil"));
				busOwnerDTO.setAddress2(rs.getString("pmo_address2"));
				busOwnerDTO.setAddress2Sinhala(rs.getString("pmo_address2_sinhala"));
				busOwnerDTO.setAddress2Tamil(rs.getString("pmo_address2_tamil"));
				busOwnerDTO.setCity(rs.getString("pmo_city"));
				busOwnerDTO.setCitySinhala(rs.getString("pmo_city_sinhala"));
				busOwnerDTO.setCityTamil(rs.getString("pmo_city_tamil"));
				busOwnerDTO.setProvince(rs.getString("pmo_province"));
				busOwnerDTO.setDistrict(rs.getString("pmo_district"));
				busOwnerDTO.setDivSec(rs.getString("pmo_div_sec"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return busOwnerDTO;
	}

	@Override
	public OminiBusDTO ominiBusByApplicationNo(String strSelectedApplicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		OminiBusDTO ominiBusDTO = new OminiBusDTO();
		try {
			con = ConnectionManager.getConnection();
			String query = "SELECT  seq,pmb_application_no, pmb_vehicle_regno, pmb_engine_no, "
					+ " pmb_chassis_no, pmb_make, pmb_model, pmb_production_date,pmb_first_reg_date, "
					+ " pmb_seating_capacity, pmb_no_of_doors, pmb_weight,pmb_is_backlog_app, pmb_permit_no  "
					+ " FROM public.nt_t_pm_omini_bus1 " + " WHERE pmb_application_no =  ? ";

			ps = con.prepareStatement(query);
			ps.setString(1, strSelectedApplicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				ominiBusDTO.setSeq(rs.getLong("seq"));
				ominiBusDTO.setApplicationNo(rs.getString("pmb_application_no"));
				ominiBusDTO.setVehicleRegNo(rs.getString("pmb_vehicle_regno"));
				ominiBusDTO.setEngineNo(rs.getString("pmb_engine_no"));
				ominiBusDTO.setChassisNo(rs.getString("pmb_chassis_no"));
				ominiBusDTO.setMake(rs.getString("pmb_make"));
				ominiBusDTO.setModel(rs.getString("pmb_model"));

				String productionDateString = rs.getString("pmb_production_date");
				if (productionDateString != null) {
					ominiBusDTO.setManufactureDate(productionDateString);
					ominiBusDTO.setStrStringManufactureDate(productionDateString);

				}
				String regDateString = rs.getString("pmb_first_reg_date");
				if (regDateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date regDate = originalFormat.parse(regDateString);
					ominiBusDTO.setRegistrationDate(regDate);
					ominiBusDTO.setStrStringRegistrationDate(regDateString);

				}
				ominiBusDTO.setSeating(rs.getString("pmb_seating_capacity"));
				ominiBusDTO.setNoofDoors(rs.getString("pmb_no_of_doors"));
				ominiBusDTO.setWeight(rs.getString("pmb_weight"));
				ominiBusDTO.setIsBacklogApp(rs.getString("pmb_is_backlog_app"));
				ominiBusDTO.setPermitNo(rs.getString("pmb_permit_no"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return ominiBusDTO;
	}

	@Override
	public PermitPaymentDTO paymentByApplicationNo(String strSelectedApplicationNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		PermitPaymentDTO permitPaymentDTO = new PermitPaymentDTO();
		try {
			con = ConnectionManager.getConnection();
			String query = "SELECT  seq, pay_application_no, pay_vehicle_regno, pay_permit_no,"
					+ " pay_tot_permit_amt, pay_excess_amt, "
					+ " pay_special_remark, pay_renewal_amt, pay_penalty_amt, pay_tender_fee, "
					+ " pay_service_fee, pay_other_fee, pay_total_fee, pay_is_backlog_app  "
					+ " FROM public.nt_t_pm_payment " + " WHERE pay_application_no =  ? ";

			ps = con.prepareStatement(query);
			ps.setString(1, strSelectedApplicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {

				permitPaymentDTO.setSeq(rs.getLong("seq"));
				permitPaymentDTO.setApplicationNo(rs.getString("pay_application_no"));
				permitPaymentDTO.setVehicleRegNo(rs.getString("pay_vehicle_regno"));
				permitPaymentDTO.setPermitNo(rs.getString("pay_permit_no"));
				permitPaymentDTO.setTotalPermitAmt(rs.getBigDecimal("pay_tot_permit_amt"));
				permitPaymentDTO.setExcessAmt(rs.getBigDecimal("pay_excess_amt"));
				permitPaymentDTO.setSpecialRemark(rs.getString("pay_special_remark"));
				permitPaymentDTO.setRenewalAmt(rs.getBigDecimal("pay_renewal_amt"));
				permitPaymentDTO.setPenaltyAmt(rs.getBigDecimal("pay_penalty_amt"));
				permitPaymentDTO.setTenderFee(rs.getBigDecimal("pay_tender_fee"));
				permitPaymentDTO.setServiceFee(rs.getBigDecimal("pay_service_fee"));
				permitPaymentDTO.setOtherFee(rs.getBigDecimal("pay_other_fee"));
				permitPaymentDTO.setTotalFee(rs.getBigDecimal("pay_total_fee"));
				permitPaymentDTO.setIsBacklogApp(rs.getString("pay_is_backlog_app"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return permitPaymentDTO;
	}

	@Override
	public List<String> getAllApplicationsWithoutCheckingStatus() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> data = new ArrayList<String>();
		try {

			con = ConnectionManager.getConnection();
			
			//Modified By Dinushi on 03/11/2021 as per the Request from Damith
			//Reason : Provide facility to modify the backlog inactive applications in the system using the Application Modify function.
			
			//String query = "SELECT pm_application_no FROM nt_t_pm_application WHERE pm_application_no IS NOT NULL and pm_application_no not like 'BAP%'  ORDER BY pm_application_no";
			String query = "SELECT pm_application_no FROM nt_t_pm_application WHERE pm_application_no IS NOT NULL and pm_application_no not like 'BAP%'"
					+ " union all SELECT pm_application_no FROM nt_t_pm_application where (pm_application_no like 'BAP%' and pm_status ='I') order by pm_application_no;";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				data.add(rs.getString("pm_application_no"));

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
	public boolean CopyOwnerDetailsANDinsertOwnerHistory(String strSelectedApplicationNo, String loginUser) {
		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		BusOwnerDTO busOwnerDTO = null;
		boolean isUpdate = false;

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_pm_vehi_owner");

			String sql = "SELECT seq, pmo_application_no, pmo_permit_no, pmo_vehicle_regno, pmo_preferred_language, pmo_title, pmo_full_name, pmo_full_name_sinhala, pmo_full_name_tamil, pmo_name_with_initial, pmo_nic, pmo_gender, pmo_dob, pmo_marital_status, pmo_telephone_no, pmo_mobile_no, pmo_province, pmo_district, pmo_div_sec, pmo_address1, pmo_address1_sinhala, pmo_address1_tamil, pmo_address2, pmo_address2_sinhala, pmo_address2_tamil, pmo_address3, pmo_address3_sinhala, pmo_address3_tamil, pmo_city, pmo_city_sinhala, pmo_city_tamil, pmo_is_backlog_app, pm_created_by, pm_created_date, pm_modified_by, pm_modified_date FROM public.nt_t_pm_vehi_owner where pmo_application_no=?;";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, strSelectedApplicationNo);

			rs = stmt.executeQuery();

			if (rs.next()) {

				busOwnerDTO = new BusOwnerDTO();
				busOwnerDTO.setSeq(rs.getLong("seq"));
				busOwnerDTO.setApplicationNo(rs.getString("pmo_application_no"));
				busOwnerDTO.setPermitNo(rs.getString("pmo_permit_no"));
				busOwnerDTO.setBusRegNo(rs.getString("pmo_vehicle_regno"));
				busOwnerDTO.setPerferedLanguage(rs.getString("pmo_preferred_language"));
				busOwnerDTO.setTitle(rs.getString("pmo_title"));
				busOwnerDTO.setFullName(rs.getString("pmo_full_name"));
				busOwnerDTO.setFullNameSinhala(rs.getString("pmo_full_name_sinhala"));
				busOwnerDTO.setFullNameTamil(rs.getString("pmo_full_name_tamil"));
				busOwnerDTO.setNameWithInitials(rs.getString("pmo_name_with_initial"));
				busOwnerDTO.setNicNo(rs.getString("pmo_nic"));
				busOwnerDTO.setGender(rs.getString("pmo_gender"));
				busOwnerDTO.setDobVal(rs.getString("pmo_dob"));
				busOwnerDTO.setMaritalStatus(rs.getString("pmo_marital_status"));
				busOwnerDTO.setTelephoneNo(rs.getString("pmo_telephone_no"));
				busOwnerDTO.setMobileNo(rs.getString("pmo_mobile_no"));
				busOwnerDTO.setProvince(rs.getString("pmo_province"));
				busOwnerDTO.setDistrict(rs.getString("pmo_district"));
				busOwnerDTO.setDivSec(rs.getString("pmo_div_sec"));
				busOwnerDTO.setAddress1(rs.getString("pmo_address1"));
				busOwnerDTO.setAddress1Sinhala(rs.getString("pmo_address1_sinhala"));
				busOwnerDTO.setAddress1Tamil(rs.getString("pmo_address1_tamil"));
				busOwnerDTO.setAddress2(rs.getString("pmo_address2"));
				busOwnerDTO.setAddress2Sinhala(rs.getString("pmo_address2_sinhala"));
				busOwnerDTO.setAddress2Tamil(rs.getString("pmo_address2_tamil"));
				busOwnerDTO.setCity(rs.getString("pmo_city"));
				busOwnerDTO.setCitySinhala(rs.getString("pmo_city_sinhala"));
				busOwnerDTO.setCityTamil(rs.getString("pmo_city_tamil"));
				busOwnerDTO.setIsBacklogApp(rs.getString("pmo_is_backlog_app"));
				busOwnerDTO.setCreatedBy(rs.getString("pm_created_by"));
				busOwnerDTO.setCratedDate(rs.getTimestamp("pm_created_date"));
				busOwnerDTO.setModifiedBy(rs.getString("pm_modified_by"));
				busOwnerDTO.setModifiedDate(rs.getTimestamp("pm_modified_date"));

				String sql2 = "INSERT INTO public.nt_h_pm_vehi_owner (seq, pmo_application_no, pmo_permit_no, pmo_vehicle_regno, pmo_preferred_language, pmo_title, pmo_full_name, pmo_full_name_sinhala, pmo_full_name_tamil, pmo_name_with_initial, pmo_nic, pmo_gender, pmo_dob, pmo_marital_status, pmo_telephone_no, pmo_mobile_no, pmo_province, pmo_district, pmo_div_sec, pmo_address1, pmo_address1_sinhala, pmo_address1_tamil, pmo_address2, pmo_address2_sinhala, pmo_address2_tamil,  pmo_city, pmo_city_sinhala, pmo_city_tamil, pmo_is_backlog_app, pm_created_by, pm_created_date, pm_modified_by, pm_modified_date) VALUES "
						+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?" + ", ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
						+ " ?,? , ?, ?, ?, ?, ?, ?, ?, ?," + " ?, ?, ?) ;";

				stmt2 = con.prepareStatement(sql2);

				stmt2.setLong(1, seqNo);
				stmt2.setString(2, busOwnerDTO.getApplicationNo());
				stmt2.setString(3, busOwnerDTO.getPermitNo());
				stmt2.setString(4, busOwnerDTO.getBusRegNo());
				stmt2.setString(5, busOwnerDTO.getPerferedLanguage());
				stmt2.setString(6, busOwnerDTO.getTitle());
				stmt2.setString(7, busOwnerDTO.getFullName());
				stmt2.setString(8, busOwnerDTO.getFullNameSinhala());
				stmt2.setString(9, busOwnerDTO.getFullNameTamil());
				stmt2.setString(10, busOwnerDTO.getNameWithInitials());
				stmt2.setString(11, busOwnerDTO.getNicNo());
				stmt2.setString(12, busOwnerDTO.getGender());
				stmt2.setString(13, busOwnerDTO.getDobVal());
				stmt2.setString(14, busOwnerDTO.getMaritalStatus());
				stmt2.setString(15, busOwnerDTO.getTelephoneNo());
				stmt2.setString(16, busOwnerDTO.getMobileNo());
				stmt2.setString(17, busOwnerDTO.getProvince());
				stmt2.setString(18, busOwnerDTO.getDistrict());
				stmt2.setString(19, busOwnerDTO.getDivSec());
				stmt2.setString(20, busOwnerDTO.getAddress1());
				stmt2.setString(21, busOwnerDTO.getAddress1Sinhala());
				stmt2.setString(22, busOwnerDTO.getAddress1Tamil());
				stmt2.setString(23, busOwnerDTO.getAddress2());
				stmt2.setString(24, busOwnerDTO.getAddress2Sinhala());
				stmt2.setString(25, busOwnerDTO.getAddress2Tamil());
				stmt2.setString(26, busOwnerDTO.getCity());
				stmt2.setString(27, busOwnerDTO.getCitySinhala());
				stmt2.setString(28, busOwnerDTO.getCityTamil());
				stmt2.setString(29, busOwnerDTO.getIsBacklogApp());
				stmt2.setString(30, busOwnerDTO.getCreatedBy());
				stmt2.setTimestamp(31, busOwnerDTO.getCratedDate());
				stmt2.setString(32, busOwnerDTO.getModifiedBy());
				stmt2.setTimestamp(33, busOwnerDTO.getModifiedDate());
				stmt2.executeUpdate();
				isUpdate = true;
			} else {
				isUpdate = false;

			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(con);

		}
		return isUpdate;
	}

	@Override
	public boolean deletePrevOwnerRecord(String strSelectedApplicationNo, String loginUser) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isDeletedOwnerRd = false;

		try {
			con = ConnectionManager.getConnection();

			String query = "DELETE FROM public.nt_t_pm_vehi_owner WHERE pmo_application_no=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, strSelectedApplicationNo);

			int i = ps.executeUpdate();

			if (i > 0) {
				isDeletedOwnerRd = true;
			} else {
				isDeletedOwnerRd = false;
			}

			con.commit();

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isDeletedOwnerRd;
	}

	@Override
	public boolean CopyOwnerDetailsANDinsertOminiBusHistory(String strSelectedApplicationNo, String loginUser) {
		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		OminiBusDTO ominiBusDTO = null;
		boolean isUpdate = false;

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_h_pm_omini_bus1");

			String sql = "SELECT seq, pmb_application_no, pmb_vehicle_regno, pmb_engine_no, pmb_chassis_no, pmb_make, pmb_model, pmb_seating_capacity, pmb_no_of_doors, pmb_height, pmb_production_date, pmb_weight, pmb_serial_no, pmb_certificate_date, pmb_garage_reg_no, pmb_insurance_company, pmb_insurance_cat, pmb_insurance_expire_date, pmb_bus_price, pmb_is_loan_obtain, pmb_finance_company, pmb_bank_loan_amt, pmb_due_amt, pmb_date_obtain, pmb_lapsed_installment, pmb_revenue_license_exp_date, pmb_first_reg_date, pmb_is_backlog_app, pmb_created_by, pmb_created_date, pmb_modified_by, pmb_modified_date, pmb_permit_no, pmb_policy_no, pmb_garage_name FROM public.nt_t_pm_omini_bus1 where pmb_application_no=? ;";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, strSelectedApplicationNo);

			rs = stmt.executeQuery();

			if (rs.next()) {
				ominiBusDTO = new OminiBusDTO();
				ominiBusDTO.setSeq(rs.getLong("seq"));
				ominiBusDTO.setApplicationNo(rs.getString("pmb_application_no"));
				ominiBusDTO.setVehicleRegNo(rs.getString("pmb_vehicle_regno"));
				ominiBusDTO.setEngineNo(rs.getString("pmb_engine_no"));
				ominiBusDTO.setChassisNo(rs.getString("pmb_chassis_no"));
				ominiBusDTO.setMake(rs.getString("pmb_make"));
				ominiBusDTO.setModel(rs.getString("pmb_model"));
				ominiBusDTO.setSeating(rs.getString("pmb_seating_capacity"));
				ominiBusDTO.setNoofDoors(rs.getString("pmb_no_of_doors"));
				ominiBusDTO.setHeight(rs.getString("pmb_height"));
				ominiBusDTO.setStrStringManufactureDate(rs.getString("pmb_production_date"));
				ominiBusDTO.setWeight(rs.getString("pmb_weight"));
				ominiBusDTO.setSerialNo(rs.getString("pmb_serial_no"));
				ominiBusDTO.setFitnessCarficateDateVal(rs.getString("pmb_certificate_date"));
				ominiBusDTO.setGarageRegNo(rs.getString("pmb_garage_reg_no"));
				ominiBusDTO.setInsuCompName(rs.getString("pmb_insurance_company"));
				ominiBusDTO.setInsuCat(rs.getString("pmb_insurance_cat"));
				ominiBusDTO.setInsuExpiryDateVal(rs.getString("pmb_insurance_expire_date"));
				ominiBusDTO.setPriceValOfBus(rs.getBigDecimal("pmb_bus_price"));
				ominiBusDTO.setIsLoanObtained(rs.getString("pmb_is_loan_obtain"));
				ominiBusDTO.setFinanceCompany(rs.getString("pmb_finance_company"));
				ominiBusDTO.setBankLoan(rs.getBigDecimal("pmb_bank_loan_amt"));
				ominiBusDTO.setDueAmount(rs.getBigDecimal("pmb_due_amt"));
				ominiBusDTO.setDateObtainedVal(rs.getString("pmb_date_obtain"));
				ominiBusDTO.setLapsedInstall(rs.getString("pmb_lapsed_installment"));
				ominiBusDTO.setExpiryDateRevLicVal(rs.getString("pmb_revenue_license_exp_date"));
				ominiBusDTO.setDateOfFirstRegVal(rs.getString("pmb_first_reg_date"));
				ominiBusDTO.setIsBacklogApp(rs.getString("pmb_is_backlog_app"));
				ominiBusDTO.setCreatedBy(rs.getString("pmb_created_by"));
				ominiBusDTO.setCratedDate(rs.getTimestamp("pmb_created_date"));
				ominiBusDTO.setModifiedBy(rs.getString("pmb_modified_by"));
				ominiBusDTO.setModifiedDate(rs.getTimestamp("pmb_modified_date"));
				ominiBusDTO.setPermitNo(rs.getString("pmb_permit_no"));
				ominiBusDTO.setPolicyNo(rs.getString("pmb_policy_no"));
				ominiBusDTO.setGarageName(rs.getString("pmb_garage_name"));

				String sql2 = "INSERT INTO public.nt_h_pm_omini_bus1 (seq, pmb_application_no, pmb_vehicle_regno, pmb_engine_no, pmb_chassis_no, pmb_make, pmb_model, pmb_seating_capacity, pmb_no_of_doors, pmb_height, pmb_production_date, pmb_weight, pmb_serial_no, pmb_certificate_date, pmb_garage_reg_no, pmb_insurance_company, pmb_insurance_cat, pmb_insurance_expire_date, pmb_bus_price, pmb_is_loan_obtain, pmb_finance_company, pmb_bank_loan_amt, pmb_due_amt, pmb_date_obtain, pmb_lapsed_installment, pmb_revenue_license_exp_date, pmb_first_reg_date, pmb_is_backlog_app, pmb_created_by, pmb_created_date, pmb_modified_by, pmb_modified_date, pmb_permit_no, pmb_policy_no, pmb_garage_name) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

				stmt2 = con.prepareStatement(sql2);

				stmt2.setLong(1, seqNo);
				stmt2.setString(2, ominiBusDTO.getApplicationNo());
				stmt2.setString(3, ominiBusDTO.getVehicleRegNo());
				stmt2.setString(4, ominiBusDTO.getEngineNo());
				stmt2.setString(5, ominiBusDTO.getChassisNo());
				stmt2.setString(6, ominiBusDTO.getMake());
				stmt2.setString(7, ominiBusDTO.getModel());
				stmt2.setString(8, ominiBusDTO.getSeating());
				stmt2.setString(9, ominiBusDTO.getNoofDoors());
				stmt2.setString(10, ominiBusDTO.getHeight());
				stmt2.setString(11, ominiBusDTO.getStrStringManufactureDate());
				stmt2.setString(12, ominiBusDTO.getWeight());
				stmt2.setString(13, ominiBusDTO.getSerialNo());
				stmt2.setString(14, ominiBusDTO.getFitnessCarficateDateVal());
				stmt2.setString(15, ominiBusDTO.getGarageRegNo());
				stmt2.setString(16, ominiBusDTO.getInsuCompName());
				stmt2.setString(17, ominiBusDTO.getInsuCat());
				stmt2.setString(18, ominiBusDTO.getInsuExpiryDateVal());
				stmt2.setBigDecimal(19, ominiBusDTO.getPriceValOfBus());
				stmt2.setString(20, ominiBusDTO.getIsLoanObtained());
				stmt2.setString(21, ominiBusDTO.getFinanceCompany());
				stmt2.setBigDecimal(22, ominiBusDTO.getBankLoan());
				stmt2.setBigDecimal(23, ominiBusDTO.getDueAmount());
				stmt2.setString(24, ominiBusDTO.getDateObtainedVal());
				stmt2.setString(25, ominiBusDTO.getLapsedInstall());
				stmt2.setString(26, ominiBusDTO.getExpiryDateRevLicVal());
				stmt2.setString(27, ominiBusDTO.getDateOfFirstRegVal());
				stmt2.setString(28, ominiBusDTO.getIsBacklogApp());
				stmt2.setString(29, ominiBusDTO.getCreatedBy());
				stmt2.setTimestamp(30, ominiBusDTO.getCratedDate());
				stmt2.setString(31, ominiBusDTO.getModifiedBy());
				stmt2.setTimestamp(32, ominiBusDTO.getModifiedDate());
				stmt2.setString(33, ominiBusDTO.getPermitNo());
				stmt2.setString(34, ominiBusDTO.getPolicyNo());
				stmt2.setString(35, ominiBusDTO.getGarageName());
				stmt2.executeUpdate();
				isUpdate = true;
			} else {
				isUpdate = false;
			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(con);
		}
		return isUpdate;
	}

	@Override
	public boolean CopyPaymentDetailsANDinsertPaymentHistory(String strSelectedApplicationNo, String loginUser) {
		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		PermitPaymentDTO permitPaymentDTO = null;
		boolean isUpdate = false;

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_pm_payment");

			String sql = "SELECT seq, pay_application_no, pay_vehicle_regno, pay_permit_no, pay_tot_permit_amt, pay_excess_amt, pay_special_remark, pay_renewal_amt, pay_penalty_amt, pay_tender_fee, pay_service_fee, pay_other_fee, pay_total_fee, pay_is_backlog_app, pay_created_by, pay_created_date, pay_modified_by, pay_modified_date FROM public.nt_t_pm_payment where pay_application_no=? ;";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, strSelectedApplicationNo);

			rs = stmt.executeQuery();

			if (rs.next()) {

				permitPaymentDTO = new PermitPaymentDTO();

				permitPaymentDTO.setSeq(rs.getLong("seq"));
				permitPaymentDTO.setApplicationNo(rs.getString("pay_application_no"));
				permitPaymentDTO.setVehicleRegNo(rs.getString("pay_vehicle_regno"));
				permitPaymentDTO.setPermitNo(rs.getString("pay_permit_no"));
				permitPaymentDTO.setTotalPermitAmt(rs.getBigDecimal("pay_tot_permit_amt"));
				permitPaymentDTO.setExcessAmt(rs.getBigDecimal("pay_excess_amt"));
				permitPaymentDTO.setSpecialRemark(rs.getString("pay_special_remark"));
				permitPaymentDTO.setRenewalAmt(rs.getBigDecimal("pay_renewal_amt"));
				permitPaymentDTO.setPenaltyAmt(rs.getBigDecimal("pay_penalty_amt"));
				permitPaymentDTO.setTenderFee(rs.getBigDecimal("pay_tender_fee"));
				permitPaymentDTO.setServiceFee(rs.getBigDecimal("pay_service_fee"));
				permitPaymentDTO.setOtherFee(rs.getBigDecimal("pay_other_fee"));
				permitPaymentDTO.setTotalFee(rs.getBigDecimal("pay_total_fee"));
				permitPaymentDTO.setIsBacklogApp(rs.getString("pay_is_backlog_app"));
				permitPaymentDTO.setCreatedBy(rs.getString("pay_created_by"));
				permitPaymentDTO.setCratedDate(rs.getTimestamp("pay_created_date"));
				permitPaymentDTO.setModifiedBy(rs.getString("pay_modified_by"));
				permitPaymentDTO.setModifiedDate(rs.getTimestamp("pay_modified_date"));

				String sql2 = "INSERT INTO public.nt_h_pm_payment (seq, pay_application_no, pay_vehicle_regno, pay_permit_no, pay_tot_permit_amt, pay_excess_amt, pay_special_remark, pay_renewal_amt, pay_penalty_amt, pay_tender_fee, pay_service_fee, pay_other_fee, pay_total_fee, pay_is_backlog_app, pay_created_by, pay_created_date, pay_modified_by, pay_modified_date) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

				stmt2 = con.prepareStatement(sql2);

				stmt2.setLong(1, seqNo);
				stmt2.setString(2, permitPaymentDTO.getApplicationNo());
				stmt2.setString(3, permitPaymentDTO.getVehicleRegNo());
				stmt2.setString(4, permitPaymentDTO.getPermitNo());
				stmt2.setBigDecimal(5, permitPaymentDTO.getTotalPermitAmt());
				stmt2.setBigDecimal(6, permitPaymentDTO.getExcessAmt());
				stmt2.setString(7, permitPaymentDTO.getSpecialRemark());
				stmt2.setBigDecimal(8, permitPaymentDTO.getRenewalAmt());
				stmt2.setBigDecimal(9, permitPaymentDTO.getPenaltyAmt());
				stmt2.setBigDecimal(10, permitPaymentDTO.getTenderFee());
				stmt2.setBigDecimal(11, permitPaymentDTO.getServiceFee());
				stmt2.setBigDecimal(12, permitPaymentDTO.getOtherFee());
				stmt2.setBigDecimal(13, permitPaymentDTO.getTotalFee());
				stmt2.setString(14, permitPaymentDTO.getIsBacklogApp());
				stmt2.setString(15, permitPaymentDTO.getCreatedBy());
				stmt2.setTimestamp(16, permitPaymentDTO.getCratedDate());
				stmt2.setString(17, permitPaymentDTO.getModifiedBy());
				stmt2.setTimestamp(18, permitPaymentDTO.getModifiedDate());

				stmt2.executeUpdate();
				isUpdate = true;
			} else {
				isUpdate = false;
			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(con);
		}
		return isUpdate;
	}

	@Override
	public boolean CopyPermitDetailsANDinsertPermitHistory(String strSelectedApplicationNo, String loginUser) {
		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		PermitDTO permitDTO = null;
		boolean isUpdate = false;

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_h_pm_application");

			String sql = "SELECT seq, pm_application_no, pm_queue_no, pm_permit_no, pm_vehicle_regno, pm_permit_issue_date, pm_permit_issue_by, pm_inspect_date, pm_tender_ref_no, pm_status, pm_tender_annual_fee, pm_installments, pm_is_permit_print, pm_reject_reason, pm_valid_from, pm_valid_to, pm_approved_by, pm_appoved_date, pm_special_remark, pm_is_tender_permit, pm_service_type, pm_route_no, pm_tot_bus_fare, pm_is_backlog_app, pm_created_by, pm_created_date, pm_modified_by, pm_modified_date, pm_permit_expire_date, pm_renewal_period, pm_next_ins_date_sec1_2, pm_next_ins_date_sec3, pm_inspect_remark, pm_route_flag, pm_isnew_permit, pm_reinspec_status, pm_old_permit_no, pm_temp_permit_no, pm_is_first_notice, pm_is_second_notice, pm_is_third_notice, pm_new_permit_expiry_date, pm_old_application_no FROM public.nt_t_pm_application where pm_application_no=? ;";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, strSelectedApplicationNo);

			rs = stmt.executeQuery();

			if (rs.next()) {

				permitDTO = new PermitDTO();

				permitDTO.setSeq(rs.getLong("seq"));
				permitDTO.setApplicationNo(rs.getString("pm_application_no"));
				permitDTO.setQueueNo(rs.getString("pm_queue_no"));
				permitDTO.setPermitNo(rs.getString("pm_permit_no"));
				permitDTO.setBusRegNo(rs.getString("pm_vehicle_regno"));
				permitDTO.setPermitIssueDateVal(rs.getString("pm_permit_issue_date"));
				permitDTO.setPermitIssueBy(rs.getString("pm_permit_issue_by"));
				permitDTO.setInspectDateVal(rs.getString("pm_inspect_date"));
				permitDTO.setTenderRefNo(rs.getString("pm_tender_ref_no"));
				permitDTO.setStatus(rs.getString("pm_status"));
				permitDTO.setTenderAnnualFee(rs.getBigDecimal("pm_tender_annual_fee"));
				permitDTO.setInstallments(rs.getBigDecimal("pm_installments"));
				permitDTO.setIsPermitPrint(rs.getString("pm_is_permit_print"));
				permitDTO.setRejectReason(rs.getString("pm_reject_reason"));
				permitDTO.setValidFromDateVal(rs.getString("pm_valid_from"));
				permitDTO.setValidToDateVal(rs.getString("pm_valid_to"));
				permitDTO.setApprovedBy(rs.getString("pm_approved_by"));
				permitDTO.setApprovedDateVal(rs.getString("pm_appoved_date"));
				permitDTO.setSpecialRemark(rs.getString("pm_special_remark"));
				permitDTO.setIsTenderPermit(rs.getString("pm_is_tender_permit"));
				permitDTO.setServiceType(rs.getString("pm_service_type"));
				permitDTO.setRouteNo(rs.getString("pm_route_no"));
				permitDTO.setTotBusFare(rs.getBigDecimal("pm_tot_bus_fare"));
				permitDTO.setIsBacklogApp(rs.getString("pm_is_backlog_app"));
				permitDTO.setCreatedBy(rs.getString("pm_created_by"));
				permitDTO.setCratedDate(rs.getTimestamp("pm_created_date"));
				permitDTO.setModifiedBy(rs.getString("pm_modified_by"));
				permitDTO.setModifiedDate(rs.getTimestamp("pm_modified_date"));
				permitDTO.setPermitExpiredDateVal(rs.getString("pm_permit_expire_date"));
				permitDTO.setRenewalPeriod(rs.getInt("pm_renewal_period"));
				permitDTO.setNextInsDateSec1_2Val(rs.getString("pm_next_ins_date_sec1_2"));
				permitDTO.setPmNextInsDateSec3Val(rs.getString("pm_next_ins_date_sec3"));
				permitDTO.setInspectionRemark(rs.getString("pm_inspect_remark"));
				permitDTO.setRouteFlag(rs.getString("pm_route_flag"));
				permitDTO.setIsNewPermit(rs.getString("pm_isnew_permit"));
				permitDTO.setReInspectStatus(rs.getString("pm_reinspec_status"));
				permitDTO.setOldPermitNo(rs.getString("pm_old_permit_no"));
				permitDTO.setTempPermitNo(rs.getString("pm_temp_permit_no"));
				permitDTO.setIsFirstNotice(rs.getString("pm_is_first_notice"));
				permitDTO.setIsSecondNotice(rs.getString("pm_is_second_notice"));
				permitDTO.setIsThirdNotice(rs.getString("pm_is_third_notice"));
				permitDTO.setNewExpiryDateVal(rs.getString("pm_new_permit_expiry_date"));
				permitDTO.setOldApplicationNo(rs.getString("pm_old_application_no"));

				String sql2 = "INSERT INTO public.nt_h_pm_application_new (seq, pm_application_no, pm_queue_no, pm_permit_no, pm_vehicle_regno, pm_permit_issue_date, pm_permit_issue_by, pm_inspect_date, pm_tender_ref_no, pm_status, pm_tender_annual_fee, pm_installments, pm_is_permit_print, pm_reject_reason, pm_valid_from, pm_valid_to, pm_approved_by, pm_appoved_date, pm_special_remark, pm_is_tender_permit, pm_service_type, pm_route_no, pm_tot_bus_fare, pm_is_backlog_app, pm_created_by, pm_created_date, pm_modified_by, pm_modified_date, pm_permit_expire_date, pm_renewal_period, pm_next_ins_date_sec1_2, pm_next_ins_date_sec3, pm_inspect_remark, pm_route_flag, pm_isnew_permit, pm_reinspec_status, pm_old_permit_no, pm_temp_permit_no, pm_is_first_notice, pm_is_second_notice, pm_is_third_notice, pm_new_permit_expiry_date, pm_old_application_no) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

				stmt2 = con.prepareStatement(sql2);

				stmt2.setLong(1, seqNo);
				stmt2.setString(2, permitDTO.getApplicationNo());
				stmt2.setString(3, permitDTO.getQueueNo());
				stmt2.setString(4, permitDTO.getPermitNo());
				stmt2.setString(5, permitDTO.getBusRegNo());
				stmt2.setString(6, permitDTO.getPermitIssueDateVal());
				stmt2.setString(7, permitDTO.getPermitIssueBy());
				stmt2.setString(8, permitDTO.getInspectDateVal());
				stmt2.setString(9, permitDTO.getTenderRefNo());
				stmt2.setString(10, permitDTO.getStatus());
				stmt2.setBigDecimal(11, permitDTO.getTenderAnnualFee());
				stmt2.setBigDecimal(12, permitDTO.getInstallments());
				stmt2.setString(13, permitDTO.getIsPermitPrint());
				stmt2.setString(14, permitDTO.getRejectReason());
				stmt2.setString(15, permitDTO.getValidFromDateVal());
				stmt2.setString(16, permitDTO.getValidToDateVal());
				stmt2.setString(17, permitDTO.getApprovedBy());
				stmt2.setString(18, permitDTO.getApprovedDateVal());
				stmt2.setString(19, permitDTO.getSpecialRemark());
				stmt2.setString(20, permitDTO.getIsTenderPermit());
				stmt2.setString(21, permitDTO.getServiceType());
				stmt2.setString(22, permitDTO.getRouteNo());
				stmt2.setBigDecimal(23, permitDTO.getTotBusFare());
				stmt2.setString(24, permitDTO.getIsBacklogApp());
				stmt2.setString(25, permitDTO.getCreatedBy());
				stmt2.setTimestamp(26, permitDTO.getCratedDate());
				stmt2.setString(27, permitDTO.getModifiedBy());
				stmt2.setTimestamp(28, permitDTO.getModifiedDate());
				stmt2.setString(29, permitDTO.getPermitExpiredDateVal());
				stmt2.setInt(30, permitDTO.getRenewalPeriod());
				stmt2.setString(31, permitDTO.getNextInsDateSec1_2Val());
				stmt2.setString(32, permitDTO.getPmNextInsDateSec3Val());
				stmt2.setString(33, permitDTO.getInspectionRemark());
				stmt2.setString(34, permitDTO.getRouteFlag());
				stmt2.setString(35, permitDTO.getIsNewPermit());
				stmt2.setString(36, permitDTO.getReInspectStatus());
				stmt2.setString(37, permitDTO.getOldPermitNo());
				stmt2.setString(38, permitDTO.getTempPermitNo());
				stmt2.setString(39, permitDTO.getIsFirstNotice());
				stmt2.setString(40, permitDTO.getIsSecondNotice());
				stmt2.setString(41, permitDTO.getIsThirdNotice());
				stmt2.setString(42, permitDTO.getNewExpiryDateVal());
				stmt2.setString(43, permitDTO.getOldApplicationNo());

				stmt2.executeUpdate();
				isUpdate = true;
			} else {
				isUpdate = false;
			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(con);

		}
		return isUpdate;
	}

	@Override
	public PermitRenewalsDTO renewalsByApplicationNo(String strSelectedApplicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();

		try {
			con = ConnectionManager.getConnection();
			String query = "SELECT pm_special_remark ,  pm_permit_expire_date , pm_renewal_period ,pm_valid_to ,pm_valid_from , pm_is_backlog_app  FROM public.nt_t_pm_application where  pm_application_no=? ; ";

			ps = con.prepareStatement(query);
			ps.setString(1, strSelectedApplicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				permitRenewalsDTO.setSpecialRemark(rs.getString("pm_special_remark"));
				permitRenewalsDTO.setPermitExpiryDate(rs.getString("pm_permit_expire_date"));
				permitRenewalsDTO.setRequestRenewPeriod(rs.getInt("pm_renewal_period"));
				permitRenewalsDTO.setValidToDate(rs.getString("pm_valid_to"));
				permitRenewalsDTO.setFromToDate(rs.getString("pm_valid_from"));
				permitRenewalsDTO.setBacklogAppValue(rs.getString("pm_is_backlog_app"));
				permitRenewalsDTO.setNewPermitExpirtDate(rs.getString("pm_permit_expire_date"));
				String backlogValue = permitRenewalsDTO.getBacklogAppValue();
				if (backlogValue == null || backlogValue.isEmpty() || backlogValue.equals(null)) {
					permitRenewalsDTO.setCheckBacklogValue(true);
				} else if (backlogValue.equals("Y")) {
					permitRenewalsDTO.setCheckBacklogValue(true);
				} else if (backlogValue.equals("N")) {
					permitRenewalsDTO.setCheckBacklogValue(false);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return permitRenewalsDTO;
	}

	@Override
	public int updatePermitRenewalRecord(PermitRenewalsDTO permitRenewalsDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_pm_application  SET pm_special_remark=?, pm_permit_expire_date=?, pm_renewal_period=?,  pm_modified_by=?, pm_modified_date=?,pm_valid_from=?,pm_valid_to=?,  pm_isnew_permit=?  WHERE pm_application_no=?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, permitRenewalsDTO.getSpecialRemark());
			stmt.setString(2, permitRenewalsDTO.getNewPermitExpirtDate());
			stmt.setInt(3, permitRenewalsDTO.getRequestRenewPeriod());
			stmt.setString(4, permitRenewalsDTO.getModifyBy());
			stmt.setTimestamp(5, timestamp);
			stmt.setString(6, permitRenewalsDTO.getFromToDate());
			stmt.setString(7, permitRenewalsDTO.getValidToDate());
			stmt.setString(8, "N");
			stmt.setString(9, permitRenewalsDTO.getApplicationNo());

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

	@Override
	public int updateBacklogVehiOwner(PermitDTO permitDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_pm_vehi_owner SET pmo_permit_no=?, pmo_vehicle_regno=?, pm_modified_by=?, pm_modified_date=? WHERE pmo_application_no=?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, permitDTO.getPermitNo());
			stmt.setString(2, permitDTO.getBusRegNo());
			stmt.setString(3, permitDTO.getModifiedBy());
			stmt.setTimestamp(4, timestamp);
			stmt.setString(5, permitDTO.getApplicationNo());
			stmt.executeUpdate();
			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return 0;
	}

	@Override
	public int updatePermitRenewalRecordForAmmendments(PermitRenewalsDTO permitRenewalsDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_pm_application  SET pm_special_remark=?, pm_permit_expire_date=?, pm_renewal_period=?,  pm_modified_by=?, pm_modified_date=?,pm_valid_from=?,pm_valid_to=?, pm_new_permit_expiry_date=?  WHERE pm_application_no=?;";

			try {
				stmt = con.prepareStatement(sql);
				stmt.setString(1, permitRenewalsDTO.getSpecialRemark());
				stmt.setString(2, permitRenewalsDTO.getPermitExpiryDate());
				stmt.setInt(3, permitRenewalsDTO.getRequestRenewPeriod());
				stmt.setString(4, permitRenewalsDTO.getModifyBy());
				stmt.setTimestamp(5, timestamp);
				stmt.setString(6, permitRenewalsDTO.getFromToDate());
				stmt.setString(7, permitRenewalsDTO.getValidToDate());
				stmt.setString(8, permitRenewalsDTO.getNewPermitExpirtDate());
				stmt.setString(9, permitRenewalsDTO.getApplicationNo());

				stmt.executeUpdate();
				con.commit();
			}catch (SQLException e) {
				con.rollback();
				return -1;
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

	@Override
	public boolean updatePermitDate(PermitRenewalsDTO permitRenewalsDTO, String loginUser) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		boolean isApproved = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_t_pm_application SET  pm_status='A',pm_permit_expire_date=? , pm_new_permit_expiry_date=? , pm_modified_by=?, pm_modified_date=? "
					+ "WHERE pm_application_no=? ;";

			ps = con.prepareStatement(query);

			if (permitRenewalsDTO.getNewPermitExpirtDate() != null) {
				ps.setString(1, permitRenewalsDTO.getNewPermitExpirtDate());
			} else {
				ps.setString(1, null);

			}
			ps.setNull(2, java.sql.Types.VARCHAR);
			ps.setString(3, loginUser);
			ps.setTimestamp(4, timestamp);
			ps.setString(5, permitRenewalsDTO.getApplicationNo());
			int a = ps.executeUpdate();
			if (a > 0) {
				isApproved = true;
			} else {
				isApproved = false;
			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isApproved;
	}

	@Override
	public boolean updatePermitDateN(PermitRenewalsDTO permitRenewalsDTO, String loginUser) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		boolean isApproved = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_t_pm_application SET pm_permit_expire_date=? , pm_new_permit_expiry_date=? , pm_modified_by=?, pm_modified_date=? "
					+ "WHERE pm_application_no=? ;";

			ps = con.prepareStatement(query);

			if (permitRenewalsDTO.getNewPermitExpirtDate() != null) {
				ps.setString(1, permitRenewalsDTO.getNewPermitExpirtDate());
			} else {
				ps.setString(1, null);

			}
			ps.setNull(2, java.sql.Types.VARCHAR);
			ps.setString(3, loginUser);
			ps.setTimestamp(4, timestamp);
			ps.setString(5, permitRenewalsDTO.getApplicationNo());
			int a = ps.executeUpdate();
			if (a > 0) {
				isApproved = true;
			} else {
				isApproved = false;
			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isApproved;
	}

	@Override
	public PermitRenewalsDTO renewalsByApplicationNoWithNewExpiredDate(String selectedAppNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();

		try {
			con = ConnectionManager.getConnection();
			String query = "SELECT pm_special_remark ,  pm_permit_expire_date , pm_renewal_period ,pm_valid_to ,pm_valid_from , pm_is_backlog_app , pm_new_permit_expiry_date  FROM public.nt_t_pm_application where  pm_application_no=? ; ";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedAppNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				permitRenewalsDTO.setSpecialRemark(rs.getString("pm_special_remark"));
				permitRenewalsDTO.setPermitExpiryDate(rs.getString("pm_permit_expire_date"));
				permitRenewalsDTO.setRequestRenewPeriod(rs.getInt("pm_renewal_period"));
				permitRenewalsDTO.setValidToDate(rs.getString("pm_valid_to"));
				permitRenewalsDTO.setFromToDate(rs.getString("pm_valid_from"));
				permitRenewalsDTO.setBacklogAppValue(rs.getString("pm_is_backlog_app"));
				permitRenewalsDTO.setNewPermitExpirtDate(rs.getString("pm_new_permit_expiry_date"));
				String backlogValue = permitRenewalsDTO.getBacklogAppValue();
				if (backlogValue == null || backlogValue.isEmpty() || backlogValue.equals(null)) {
					permitRenewalsDTO.setCheckBacklogValue(true);
				} else if (backlogValue.equals("Y")) {
					permitRenewalsDTO.setCheckBacklogValue(true);
				} else if (backlogValue.equals("N")) {
					permitRenewalsDTO.setCheckBacklogValue(false);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return permitRenewalsDTO;
	}

	@Override
	public int updateEditPermitRecord(PermitDTO permitDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = " UPDATE public.nt_t_pm_application"
					+ " SET pm_permit_no = ?, pm_vehicle_regno = ? ,pm_is_tender_permit= ?, "
					+ " pm_service_type=?, pm_route_no=?, pm_tot_bus_fare=?, "
					+ " pm_modified_by=?, pm_modified_date=?,pm_route_flag=? " + " WHERE seq= ?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, permitDTO.getPermitNo());
			stmt.setString(2, permitDTO.getBusRegNo());
			stmt.setString(3, permitDTO.getIsTenderPermit());
			stmt.setString(4, permitDTO.getServiceType());
			stmt.setString(5, permitDTO.getRouteNo());
			stmt.setBigDecimal(6, permitDTO.getBusFare());
			stmt.setString(7, permitDTO.getModifiedBy());
			stmt.setTimestamp(8, timestamp);
			stmt.setString(9, permitDTO.getRouteFlag());
			stmt.setLong(10, permitDTO.getSeq());

			stmt.executeUpdate();
			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return 0;
	}

	@Override
	public int updateAmendmentDataForModifyPermitData(PermitDTO permitDTO, String loggedUser) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;
		AmendmentDTO dto = new AmendmentDTO();

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			// get current data in amendment table
			String sql = "SELECT seq, amd_queue_no, amd_permit_no, amd_application_no, amd_remarks, amd_service_change_type, "
					+ " amd_commity_approved, amd_board_approved, amd_status, amd_existing_bus_remark, amd_is_bus_running, "
					+ " amd_is_print, amd_hv_legal_case, amd_details_of_case, amd_committee_remark, amd_board_reject_reason, "
					+ " amd_busowner_remarks, amd_curr_expire_date, amd_new_expire_date, amd_relationship_with_transferor, "
					+ " amd_transferor_remarks, amd_reasonfor_ownerchange, amd_is_omnibus, amd_new_busno, amd_existing_busno, "
					+ " amd_created_by, amd_created_date, amd_modified_by, amd_modified_date, amd_trn_type, "
					+ " amd_first_reject_remarks, amd_pta, amd_time_approval FROM public.nt_m_amendments where amd_application_no=? ;";

			ps = con.prepareStatement(sql);
			ps.setString(1, permitDTO.getApplicationNo());
			rs = ps.executeQuery();

			while (rs.next()) {

				dto.setQueueNo(rs.getString("amd_queue_no"));
				dto.setPermitNo(rs.getString("amd_permit_no"));
				dto.setApplicationNo(rs.getString("amd_application_no"));
				dto.setRemarks(rs.getString("amd_remarks"));
				dto.setServiceChangeType(rs.getString("amd_service_change_type"));

				dto.setCommitteeApproveStatus(rs.getString("amd_commity_approved"));
				dto.setBoardApproveStatus(rs.getString("amd_board_approved"));
				dto.setStatus(rs.getString("amd_status"));
				dto.setExistingBusRemark(rs.getString("amd_existing_bus_remark"));
				dto.setIsBusRunning(rs.getString("amd_is_bus_running"));

				dto.setIsPrint(rs.getString("amd_is_print"));
				dto.setHaveLegalCase(rs.getString("amd_hv_legal_case"));
				dto.setLegalCaseDetails(rs.getString("amd_details_of_case"));
				dto.setCommitteeReamrk(rs.getString("amd_committee_remark"));
				dto.setBoarderRejectReason(rs.getString("amd_board_reject_reason"));

				dto.setOwnerRemark(rs.getString("amd_busowner_remarks"));
				dto.setCurrentExpiryDate(rs.getString("amd_curr_expire_date"));
				dto.setNewExpiryDate(rs.getString("amd_new_expire_date"));
				dto.setRelationshipWithTransferor(rs.getString("amd_relationship_with_transferor"));

				dto.setRelationshipWithTransferorRemarks(rs.getString("amd_transferor_remarks"));
				dto.setReasonForOwnerChange(rs.getString("amd_reasonfor_ownerchange"));
				dto.setIsOminiBus(rs.getString("amd_is_omnibus"));
				dto.setNewBusNo(rs.getString("amd_new_busno"));
				dto.setExisitingBusNo(rs.getString("amd_existing_busno"));

				dto.setCreatedBy(rs.getString("amd_created_by"));
				dto.setCreatedDate(rs.getTimestamp("amd_created_date"));
				dto.setModifiedBy(rs.getString("amd_modified_by"));
				dto.setModifiedDate(rs.getTimestamp("amd_modified_date"));
				dto.setTranCode(rs.getString("amd_trn_type"));

				dto.setFirstRejectRemark(rs.getString("amd_first_reject_remarks"));
				dto.setPtaStatus(rs.getString("amd_pta"));
				dto.setTimeApprovalStatus(rs.getString("amd_time_approval"));

			}

			// save current data to history
			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_h_amendments");

			String sql1 = "INSERT INTO public.nt_h_amendments "
					+ "(seq, amd_queue_no, amd_permit_no, amd_application_no, amd_remarks, amd_service_change_type,"
					+ " amd_commity_approved, amd_board_approved, amd_status, amd_existing_bus_remark, amd_is_bus_running,"
					+ " amd_is_print, amd_hv_legal_case, amd_details_of_case, amd_committee_remark, amd_board_reject_reason,"
					+ " amd_busowner_remarks, amd_curr_expire_date, amd_new_expire_date, amd_relationship_with_transferor,"
					+ " amd_transferor_remarks, amd_reasonfor_ownerchange, amd_is_omnibus, amd_new_busno, amd_existing_busno,"
					+ " amd_created_by, amd_created_date, amd_modified_by, amd_modified_date, amd_trn_type, "
					+ " amd_first_reject_remarks, amd_pta, amd_time_approval) "
					+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

			ps1 = con.prepareStatement(sql1);

			ps1.setLong(1, seqNo);
			ps1.setString(2, dto.getQueueNo());
			ps1.setString(3, dto.getPermitNo());
			ps1.setString(4, dto.getApplicationNo());
			ps1.setString(5, dto.getRemarks());
			ps1.setString(6, dto.getServiceChangeType());

			ps1.setString(7, dto.getCommitteeApproveStatus());
			ps1.setString(8, dto.getBoardApproveStatus());
			ps1.setString(9, dto.getStatus());
			ps1.setString(10, dto.getExistingBusRemark());

			ps1.setString(11, dto.getIsBusRunning());
			ps1.setString(12, dto.getIsPrint());
			ps1.setString(13, dto.getHaveLegalCase());

			ps1.setString(14, dto.getLegalCaseDetails());
			ps1.setString(15, dto.getCommitteeReamrk());
			ps1.setString(16, dto.getBoarderRejectReason());

			ps1.setString(17, dto.getOwnerRemark());
			ps1.setString(18, dto.getCurrentExpiryDate());
			ps1.setString(19, dto.getNewExpiryDate());
			ps1.setString(20, dto.getRelationshipWithTransferor());

			ps1.setString(21, dto.getRelationshipWithTransferorRemarks());
			ps1.setString(22, dto.getReasonForOwnerChange());
			ps1.setString(23, dto.getIsOminiBus());

			ps1.setString(24, dto.getNewBusNo());
			ps1.setString(25, dto.getExisitingBusNo());

			ps1.setString(26, dto.getCreatedBy());
			ps1.setTimestamp(27, dto.getCratedDate());
			ps1.setString(28, dto.getModifiedBy());
			ps1.setTimestamp(29, dto.getModifiedDate());
			ps1.setString(30, dto.getTranCode());

			ps1.setString(31, dto.getFirstRejectRemark());
			ps1.setString(32, dto.getPtaStatus());
			ps1.setString(33, dto.getTimeApprovalStatus());

			ps1.executeUpdate();

			// update amendment table
			String sql2 = "UPDATE public.nt_m_amendments SET amd_permit_no=?, amd_modified_by=?, amd_modified_date=? WHERE amd_application_no=?;";

			ps2 = con.prepareStatement(sql2);

			ps2.setString(1, permitDTO.getPermitNo());
			ps2.setString(2, loggedUser);
			ps2.setTimestamp(3, timestamp);
			ps2.setString(4, dto.getApplicationNo());

			ps2.executeUpdate();

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(ps1);
			ConnectionManager.close(ps2);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return 0;
	}

	@Override
	public boolean hasValidAmendmentApplication(String applicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean hasAmendment = false;
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT seq FROM public.nt_m_amendments WHERE amd_application_no=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, applicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				hasAmendment = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return hasAmendment;
	}

	@Override
	public String getPreviousPermitNo(String strSelectedApplicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String currentPermitNo = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  pm_permit_no FROM public.nt_t_pm_application where pm_application_no=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, strSelectedApplicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				currentPermitNo = rs.getString("pm_permit_no");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return currentPermitNo;
	}

	@Override
	public String getPreviousVehicleNo(String strSelectedApplicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String currentVehicleNo = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  pm_vehicle_regno FROM public.nt_t_pm_application where pm_application_no=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, strSelectedApplicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				currentVehicleNo = rs.getString("pm_vehicle_regno");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return currentVehicleNo;
	}

	public List<TokenDTO> getUnporcessedTokenDet() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<TokenDTO> tokenDetList = new ArrayList<TokenDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select que_seq,que_number,nt_r_transaction_type.description as tranDes,que_vehicle_no,que_permit_no,que_application_no,nt_m_queue_master.created_by as createdby"
					+ " from nt_m_queue_master inner join nt_r_transaction_type "
					+ "on nt_m_queue_master.que_trn_type_code = nt_r_transaction_type.code "
					+ "where to_char((nt_m_queue_master.created_date),'yyyy-MM-dd') = to_char(now(),'yyyy-MM-dd') "
					+ "and que_task_code is null and que_task_status is null";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				TokenDTO tokenDTO = new TokenDTO();
				tokenDTO.setSeq(rs.getLong("que_seq"));
				tokenDTO.setQueueNo(rs.getString("que_number"));
				tokenDTO.setTranDescription(rs.getString("tranDes"));
				tokenDTO.setVehicleNo(rs.getString("que_vehicle_no"));
				tokenDTO.setPermitNo(rs.getString("que_permit_no"));
				tokenDTO.setApplicationNo(rs.getString("que_application_no"));
				tokenDTO.setCreatedBy(rs.getString("createdby"));
				tokenDetList.add(tokenDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return tokenDetList;

	}

	public boolean deleteToken(long seq) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isDeleted = false;

		try {
			con = ConnectionManager.getConnection();

			String sql = "DELETE FROM public.nt_m_queue_master WHERE que_seq=? ;";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seq);

			int i = stmt.executeUpdate();

			if (i > 0) {
				isDeleted = true;
			} else {
				isDeleted = false;
			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return isDeleted;
	}

	// Remove Bus process
	public boolean removeBusProcess(PermitDTO permitDTO, String newBusNo, String strUser) {

		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmtO = null;
		PreparedStatement stmtV = null;
		PreparedStatement stmtA = null;
		ResultSet rs = null;
		boolean isUpdated = false;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();
			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_removed_bus_details");

			String sql = "INSERT INTO public.nt_t_removed_bus_details "
					+ " (rbd_seq, rbd_app_no, rbd_permit_no, rbd_bus_no, rbd_modified_bus_no, rbd_route_no, rbd_service_type, rbd_created_by, rbd_created_date) "
					+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, permitDTO.getApplicationNo());
			stmt.setString(3, permitDTO.getPermitNo());
			stmt.setString(4, permitDTO.getBusRegNo());
			stmt.setString(5, newBusNo);
			stmt.setString(6, permitDTO.getRouteNo());
			stmt.setString(7, permitDTO.getServiceType());
			stmt.setString(8, strUser);
			stmt.setTimestamp(9, timestamp);

			int i = stmt.executeUpdate();

			if (i > 0) {
				isUpdated = true;
			} else {
				isUpdated = false;
			}

			if (isUpdated) {
				String sqlO = " UPDATE public.nt_t_pm_omini_bus1 " + " SET pmb_vehicle_regno=? "
						+ " WHERE pmb_application_no = ?";

				stmtO = con.prepareStatement(sqlO);

				stmtO.setString(1, newBusNo);
				stmtO.setString(2, permitDTO.getApplicationNo());

				stmtO.executeUpdate();

				String sqlV = " UPDATE public.nt_t_pm_vehi_owner " + " SET pmo_vehicle_regno=? "
						+ " WHERE pmo_application_no = ?";

				stmtV = con.prepareStatement(sqlV);

				stmtV.setString(1, newBusNo);
				stmtV.setString(2, permitDTO.getApplicationNo());

				stmtV.executeUpdate();

				String sqlA = " UPDATE public.nt_t_pm_application " + " SET pm_vehicle_regno=? "
						+ " WHERE pm_application_no = ?";

				stmtA = con.prepareStatement(sqlA);

				stmtA.setString(1, newBusNo);
				stmtA.setString(2, permitDTO.getApplicationNo());

				stmtA.executeUpdate();

			}
			con.commit();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmtO);
			ConnectionManager.close(stmtV);
			ConnectionManager.close(stmtA);
			ConnectionManager.close(con);
		}

		return isUpdated;
	}

	public List<String> getAllActiveApp() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> data = new ArrayList<String>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT pm_application_no FROM nt_t_pm_application "
					+ " WHERE pm_application_no IS NOT NULL " + " AND pm_status = 'A' " + " ORDER BY pm_application_no";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				data.add(rs.getString("pm_application_no"));

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

	public List<CommonDTO> getAllActivePermit() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CommonDTO> data = new ArrayList<CommonDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT distinct pm_permit_no FROM nt_t_pm_application " + " WHERE pm_permit_no IS NOT NULL "
					+ " AND pm_status = 'A' " + " ORDER BY pm_permit_no";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("pm_permit_no"));
				commonDTO.setDescription(rs.getString("pm_permit_no"));
				data.add(commonDTO);
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

	public List<TokenDTO> getRenewalTokenDet() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<TokenDTO> tokenDetList = new ArrayList<TokenDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT que_seq,que_number,que_vehicle_no,que_permit_no,que_application_no,"
					+ " nt_m_queue_master.created_by as createdby,created_date" + " FROM nt_m_queue_master "
					+ " WHERE to_char((nt_m_queue_master.created_date),'yyyy-MM-dd') = to_char(now(),'yyyy-MM-dd')"
					+ " AND  que_number like 'RN%' " + " ORDER BY que_number";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				TokenDTO tokenDTO = new TokenDTO();
				tokenDTO.setSeq(rs.getLong("que_seq"));
				tokenDTO.setQueueNo(rs.getString("que_number"));
				tokenDTO.setVehicleNo(rs.getString("que_vehicle_no"));
				tokenDTO.setPermitNo(rs.getString("que_permit_no"));
				tokenDTO.setApplicationNo(rs.getString("que_application_no"));
				tokenDTO.setCreatedBy(rs.getString("createdby"));
				tokenDTO.setCreatedDate(rs.getDate("created_date"));
				tokenDetList.add(tokenDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return tokenDetList;

	}

	@Override
	public int updatePermitRenewalRecordsInOmniForAmmendments(PermitRenewalsDTO permitRenewalsDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		String dateFormatS = "dd/MM/yyyy";
		SimpleDateFormat frm = new SimpleDateFormat(dateFormatS);

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_pm_omini_bus1  SET pmb_policy_no=?, pmb_insurance_cat=?, pmb_insurance_expire_date=?,  pmb_modified_by=?,  pmb_modified_date=?,pmb_insurance_company=?,pmb_emission_test_exp_date=?, pmb_revenue_license_exp_date=? ,pmb_garage_reg_no =?, pmb_garage_name =?, pmb_serial_no =?, pmb_certificate_date=?   WHERE pmb_application_no=?;";

			try {
				stmt = con.prepareStatement(sql);
				stmt.setString(1, permitRenewalsDTO.getPolicyNo());

				if (permitRenewalsDTO.getInsuExpDate() != null) {
					stmt.setString(2, permitRenewalsDTO.getInsuCat());
				} else {
					stmt.setString(2, null);
				}

				if (permitRenewalsDTO.getInsuExpDate() != null) {
					stmt.setString(3, frm.format(permitRenewalsDTO.getInsuExpDate()));
				} else {
					stmt.setString(3, null);
				}
				stmt.setString(4, permitRenewalsDTO.getModifyBy());
				stmt.setTimestamp(5, timestamp);

				if (permitRenewalsDTO.getInsuExpDate() != null) {
					stmt.setString(6, permitRenewalsDTO.getInsuCompName());
				} else {
					stmt.setString(6, null);
				}

				if (permitRenewalsDTO.getEmissionExpDate() != null) {
					stmt.setString(7, frm.format(permitRenewalsDTO.getEmissionExpDate()));
				} else {
					stmt.setString(7, null);
				}
				if (permitRenewalsDTO.getRevenueExpDate() != null) {
					stmt.setString(8, frm.format(permitRenewalsDTO.getRevenueExpDate()));
				} else {
					stmt.setString(8, null);
				}
				stmt.setString(9, permitRenewalsDTO.getGarageRegNo());
				stmt.setString(10, permitRenewalsDTO.getGarageName());
				stmt.setString(11, permitRenewalsDTO.getSerialNo());
				if (permitRenewalsDTO.getFitnessCertiDate() != null) {
					stmt.setString(12, frm.format(permitRenewalsDTO.getFitnessCertiDate()));
				} else {
					stmt.setString(12, null);
				}
				stmt.setString(13, permitRenewalsDTO.getApplicationNo());

				stmt.executeUpdate();
				con.commit();

			}catch (SQLException e) {
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
	
	// Get All Nisi Route Details By Login User
		public List<RouteDTO> getNisiRouteDetailsbyLoginUser(String userId) {
			Connection con = null;
			PreparedStatement ps = null;
			ResultSet rs = null;

			List<RouteDTO> routeDetList = new ArrayList<RouteDTO>();
			try {
				con = ConnectionManager.getConnection();

				String query = "SELECT seqno, rou_description, rou_number, "
						+ " rou_service_origine, rou_service_destination, rou_via, "
						+ " case when active='T' then 'Temporary' else active end, "
						+ " created_by, created_date,rou_distance, active as status_code, rou_tot_busfare, rou_service_origine_sin,"
						+ " rou_service_destination_sin, rou_via_sin , "
						+ " rou_service_origine_tamil, rou_service_destination_tamil, rou_via_tamil "
						+ " FROM public.nt_r_route " + " WHERE created_by = ? and active='T' " 
						+ " ORDER BY rou_number";

				ps = con.prepareStatement(query);
				ps.setString(1, userId);
				rs = ps.executeQuery();

				while (rs.next()) {
					RouteDTO routeDTO = new RouteDTO();
					routeDTO.setSeq(rs.getLong("seqno"));
					routeDTO.setRouteDes(rs.getString("rou_description"));
					routeDTO.setRouteNo(rs.getString("rou_number"));
					routeDTO.setOrigin(rs.getString("rou_service_origine"));
					routeDTO.setDestination(rs.getString("rou_service_destination"));
					routeDTO.setVia(rs.getString("rou_via"));
					routeDTO.setStatus(rs.getString("active"));
					routeDTO.setCreatedBy(rs.getString("created_by"));
					routeDTO.setCratedDate(rs.getTimestamp("created_date"));
					routeDTO.setDistance(rs.getBigDecimal("rou_distance"));
					routeDTO.setStatusCode(rs.getString("status_code"));
					routeDTO.setBusFare(rs.getBigDecimal("rou_tot_busfare"));
					routeDTO.setOriginS(rs.getString("rou_service_origine_sin"));
					routeDTO.setDestinationS(rs.getString("rou_service_destination_sin"));
					routeDTO.setViaS(rs.getString("rou_via_sin"));
					routeDTO.setOriginT(rs.getString("rou_service_origine_tamil"));
					routeDTO.setDestinationT(rs.getString("rou_service_destination_tamil"));
					routeDTO.setViaT(rs.getString("rou_via_tamil"));
					routeDetList.add(routeDTO);
				}

			} catch (Exception e) {

				e.printStackTrace();
			} finally {
				ConnectionManager.close(rs);
				ConnectionManager.close(ps);
				ConnectionManager.close(con);
			}

			return routeDetList;

		}
		
		// Get All Nisi Route Details
		public List<RouteDTO> getNisiRouteDetails() {
			Connection con = null;
			PreparedStatement ps = null;
			ResultSet rs = null;

			List<RouteDTO> routeDetList = new ArrayList<RouteDTO>();
			try {
				con = ConnectionManager.getConnection();

				String query = "SELECT seqno, rou_description, rou_number, "
						+ " rou_service_origine, rou_service_destination, rou_via, "
						+ " case when active='T' then 'Temporary' else active end, "
						+ " created_by, created_date,rou_distance, active as status_code,rou_tot_busfare, rou_service_origine_sin,"
						+ " rou_service_destination_sin, rou_via_sin , "
						+ " rou_service_origine_tamil, rou_service_destination_tamil, rou_via_tamil "
						+ " FROM public.nt_r_route WHERE active='T' " 
						+ " ORDER BY rou_number";

				ps = con.prepareStatement(query);
				rs = ps.executeQuery();

				while (rs.next()) {
					RouteDTO routeDTO = new RouteDTO();
					routeDTO.setSeq(rs.getLong("seqno"));
					routeDTO.setRouteDes(rs.getString("rou_description"));
					routeDTO.setRouteNo(rs.getString("rou_number"));
					routeDTO.setOrigin(rs.getString("rou_service_origine"));
					routeDTO.setDestination(rs.getString("rou_service_destination"));
					routeDTO.setVia(rs.getString("rou_via"));
					routeDTO.setStatus(rs.getString("active"));
					routeDTO.setCreatedBy(rs.getString("created_by"));
					routeDTO.setCratedDate(rs.getTimestamp("created_date"));
					routeDTO.setDistance(rs.getBigDecimal("rou_distance"));
					routeDTO.setStatusCode(rs.getString("status_code"));
					routeDTO.setBusFare(rs.getBigDecimal("rou_tot_busfare"));
					routeDTO.setOriginS(rs.getString("rou_service_origine_sin"));
					routeDTO.setDestinationS(rs.getString("rou_service_destination_sin"));
					routeDTO.setViaS(rs.getString("rou_via_sin"));
					routeDTO.setOriginT(rs.getString("rou_service_origine_tamil"));
					routeDTO.setDestinationT(rs.getString("rou_service_destination_tamil"));
					routeDTO.setViaT(rs.getString("rou_via_tamil"));
					routeDetList.add(routeDTO);

				}

			} catch (Exception e) {

				e.printStackTrace();
			} finally {
				ConnectionManager.close(rs);
				ConnectionManager.close(ps);
				ConnectionManager.close(con);
			}

			return routeDetList;

		}
	
		// Get All Nisi Route Details by Route No
		public List<RouteDTO> getNisiRouteDetailsByRouteNo(String routeNo) {
			Connection con = null;
			PreparedStatement ps = null;
			ResultSet rs = null;

			List<RouteDTO> routeDetList = new ArrayList<RouteDTO>();
			try {
				con = ConnectionManager.getConnection();

				String query = "SELECT seqno, rou_description, rou_number, "
						+ " rou_service_origine, rou_service_destination, rou_via, "
						+ " case when active = 'A' then 'Active' when active = 'I' then 'Inactive' when active='T' then 'Temporary' else active end, "
						+ " created_by, created_date,rou_distance, active as status_code, rou_tot_busfare, rou_service_origine_sin,"
						+ " rou_service_destination_sin, rou_via_sin , "
						+ " rou_service_origine_tamil, rou_service_destination_tamil, rou_via_tamil  "
						+ " FROM public.nt_r_route " + " WHERE rou_number = ? and active = 'T' ";

				ps = con.prepareStatement(query);
				ps.setString(1, routeNo);
				rs = ps.executeQuery();

				while (rs.next()) {
					RouteDTO routeDTO = new RouteDTO();
					routeDTO.setSeq(rs.getLong("seqno"));
					routeDTO.setRouteDes(rs.getString("rou_description"));
					routeDTO.setRouteNo(rs.getString("rou_number"));
					routeDTO.setOrigin(rs.getString("rou_service_origine"));
					routeDTO.setDestination(rs.getString("rou_service_destination"));
					routeDTO.setVia(rs.getString("rou_via"));
					routeDTO.setStatus(rs.getString("active"));
					routeDTO.setCreatedBy(rs.getString("created_by"));
					routeDTO.setCratedDate(rs.getTimestamp("created_date"));
					routeDTO.setDistance(rs.getBigDecimal("rou_distance"));
					routeDTO.setStatusCode(rs.getString("status_code"));
					routeDTO.setBusFare(rs.getBigDecimal("rou_tot_busfare"));
					routeDTO.setOriginS(rs.getString("rou_service_origine_sin"));
					routeDTO.setDestinationS(rs.getString("rou_service_destination_sin"));
					routeDTO.setViaS(rs.getString("rou_via_sin"));
					routeDTO.setOriginT(rs.getString("rou_service_origine_tamil"));
					routeDTO.setDestinationT(rs.getString("rou_service_destination_tamil"));
					routeDTO.setViaT(rs.getString("rou_via_tamil"));
					routeDetList.add(routeDTO);

				}

			} catch (Exception e) {

				e.printStackTrace();
			} finally {
				ConnectionManager.close(rs);
				ConnectionManager.close(ps);
				ConnectionManager.close(con);
			}

			return routeDetList;

		}

}
