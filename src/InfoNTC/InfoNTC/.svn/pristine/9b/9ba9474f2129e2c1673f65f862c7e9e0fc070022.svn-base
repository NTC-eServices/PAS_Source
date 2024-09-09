package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import lk.informatics.ntc.model.dto.BusAssignOnRoutesDTO;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.roc.utils.common.Utils;

public class BusAssignOnRoutesServiceImpl implements BusAssignOnRoutesService {

	@Override
	public List routeNoDropDown() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String value = "";
		List<String> returnList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = " select rou_number from nt_r_route order by rou_number";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				value = rs.getString("rou_number");
				returnList.add(value);

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
	public List busNoDropDown(String value) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String test = "";
		List<String> returnList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct pm_vehicle_regno " + "from nt_t_pm_application " + "where pm_route_no='"
					+ value + "'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				test = rs.getString("pm_vehicle_regno");
				returnList.add(test);
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
	public BusAssignOnRoutesDTO busCategoryDropDown(BusAssignOnRoutesDTO busDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct a.pm_service_type,b.description "
					+ "from nt_t_pm_application a left outer join public.nt_r_service_types b "
					+ "on a.pm_service_type=b.code " + "where pm_vehicle_regno='" + busDTO.getBusNo() + "'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				busDTO = new BusAssignOnRoutesDTO();
				busDTO.setBusCategory(rs.getString("description"));
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return busDTO;
	}

	@Override
	public BusAssignOnRoutesDTO originDestination(String busNo, String route) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		BusAssignOnRoutesDTO busDTO = new BusAssignOnRoutesDTO();
		boolean swap = checkSwap(busNo);

		try {
			con = ConnectionManager.getConnection();

			String query = " select rou_service_origine,rou_service_destination " + "from nt_r_route "
					+ "where rou_number='" + route + "'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				busDTO = new BusAssignOnRoutesDTO();

				if (swap == true) {
					busDTO.setDestination(rs.getString("rou_service_origine"));
					busDTO.setOrigin(rs.getString("rou_service_destination"));

				} else {
					busDTO.setOrigin(rs.getString("rou_service_origine"));
					busDTO.setDestination(rs.getString("rou_service_destination"));

				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return busDTO;
	}

	@Override
	public boolean checkSwap(String vehNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String test = "";
		boolean value = false;

		try {
			con = ConnectionManager.getConnection();

			String query = "select pm_route_flag " + "from nt_t_pm_application " + "where pm_vehicle_regno='" + vehNo
					+ "'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				test = rs.getString("pm_route_flag");

				if (test.equals("Y")) {
					value = true;
				} else {
					value = false;
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return value;
	}

	@Override
	public void add(BusAssignOnRoutesDTO busDTO, String user) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			java.util.Date date = new java.util.Date();
			Timestamp timeStamp = new Timestamp(date.getTime());
			long seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_m_bus_for_routes");

			String sql = "insert into nt_m_bus_for_routes "
					+ "(seq,bfr_route_no,bfr_bus_no,bfr_status,bfr_created_by,bfr_pmb_created_date,bfr_permit_no) "
					+ "values (?,?,?,?,?,?,?) ";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, busDTO.getRouteNo());
			stmt.setString(3, busDTO.getBusNo());
			stmt.setString(4, busDTO.getStatus());
			stmt.setString(5, user);
			stmt.setTimestamp(6, timeStamp);
			stmt.setString(7, busDTO.getLicenseNo());

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

	@Override
	public List table() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		BusAssignOnRoutesDTO busDTO;
		List<BusAssignOnRoutesDTO> data = new ArrayList<BusAssignOnRoutesDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select a.bfr_bus_no,a.bfr_status,b.pmb_production_date,b.pmb_seating_capacity,b.pmb_permit_no,c.rou_service_destination,c.rou_service_origine "
					+ "from public.nt_m_bus_for_routes a left outer join public.nt_t_pm_omini_bus1 b "
					+ "on a.bfr_bus_no = b.pmb_vehicle_regno " + "left outer join  public.nt_r_route c "
					+ "on a.bfr_route_no = c.rou_number ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				busDTO = new BusAssignOnRoutesDTO();
				busDTO.setBusNo(rs.getString("bfr_bus_no"));
				boolean swap = checkSwap(busDTO.getBusNo());
				busDTO.setManYear(rs.getString("pmb_production_date"));
				busDTO.setNoOfSeats(rs.getString("pmb_seating_capacity"));
				busDTO.setLicenseNo(rs.getString("pmb_permit_no"));
				busDTO.setStatus(rs.getString("bfr_status"));

				if (busDTO.getStatus().equals("A")) {
					busDTO.setStatus("ACTIVE");
				}

				if (busDTO.getStatus().equals("I")) {
					busDTO.setStatus("INACTIVE");
				}

				if (swap == true) {
					busDTO.setDestination(rs.getString("rou_service_origine"));
					busDTO.setOrigin(rs.getString("rou_service_destination"));

				} else {
					busDTO.setOrigin(rs.getString("rou_service_origine"));
					busDTO.setDestination(rs.getString("rou_service_destination"));

				}

				busDTO.setOrigin(rs.getString("rou_service_origine"));
				busDTO.setDestination(rs.getString("rou_service_destination"));

				data.add(busDTO);
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
	public boolean editTable(String user, String busNo, String status) {

		boolean check = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timeStamp = new Timestamp(date.getTime());
		try {
			con = ConnectionManager.getConnection();

			String query = "update nt_m_bus_for_routes " + "set bfr_status=?, bfr_modified_by=?,bfr_modified_date=?"
					+ "where bfr_bus_no=? ";

			ps = con.prepareStatement(query);
			ps.setString(1, status);
			ps.setString(2, user);
			ps.setTimestamp(3, timeStamp);
			ps.setString(4, busNo);
			ps.executeUpdate();
			con.commit();
			check = true;

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return check;
	}

	@Override
	public boolean addCheck(BusAssignOnRoutesDTO busDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean check = false;

		try {
			con = ConnectionManager.getConnection();

			String query = "select bfr_bus_no from nt_m_bus_for_routes " + "where bfr_bus_no='" + busDTO.getBusNo()
					+ "'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			if (!rs.isBeforeFirst()) {
				check = false;
			} else {
				check = true;
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return check;
	}

}
