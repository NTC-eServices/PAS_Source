package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import lk.informatics.ntc.model.dto.WindscreenLabelDTO;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.roc.utils.common.Utils;

public class WindscreenLabelServiceImpl implements WindscreenLabelService {

	private static final long serialVersionUID = 7880419222121006327L;

	public List<WindscreenLabelDTO> getTrasactionType() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<WindscreenLabelDTO> transactionType = new ArrayList<WindscreenLabelDTO>();

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT description FROM public.nt_r_transaction_type where code IN ('01','03','04','05','08','13','14','15','16') order by description";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return transactionType;

	}

	// Fill Details based on Application No.
	public WindscreenLabelDTO fillDetailsFromAppNo(String applicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		WindscreenLabelDTO windDTO = new WindscreenLabelDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct a.code as trans_code, a.description as trans_des,\r\n"
					+ "b.pm_application_no as app_no, b.pm_permit_no as permit_no, b.pm_vehicle_regno as bus_no, b.pm_service_type as ser_type, \r\n"
					+ "b.pm_valid_to as validity, b.pm_route_no as route_no, b.pm_permit_expire_date as expiry_date,\r\n"
					+ "c.rou_number as rou_no, c.rou_service_origine as orgin, c.rou_service_destination as destination, c.rou_via as via,\r\n"
					+ "d.que_permit_no as per_no\r\n"
					+ "from public.nt_r_transaction_type a, public.nt_t_pm_application b,public.nt_r_route c,public.nt_m_queue_master d\r\n"
					+ "where a.description='NEW PERMIT'  and  b.pm_application_no='PAP1800021' \r\n"
					+ "and b.pm_route_no=c.rou_number and b.pm_permit_no=d.que_permit_no;";

			ps = con.prepareStatement(query);
			ps.setString(1, applicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				windDTO.setBusNo(rs.getString("pm_application_no"));
				windDTO.setServiceType(rs.getString("pm_service_type"));
				windDTO.setRouteNo(rs.getString("pm_route_no"));
				windDTO.setOrigin(rs.getString("rou_service_origine"));
				windDTO.setDestination(rs.getString("rou_service_destination"));
				windDTO.setVia(rs.getString("rou_via"));
				windDTO.setExpiryDate(rs.getString("pm_permit_expire_date"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return windDTO;

	}

	// Application No.
	@Override
	public List<WindscreenLabelDTO> getApplicationNo() {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<WindscreenLabelDTO> applicationNo = new ArrayList<WindscreenLabelDTO>();

		try {
			con = ConnectionManager.getConnection();

			String sql = "select distinct pm_application_no from public.nt_t_pm_application ;";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return applicationNo;

	}

	@Override
	public List<WindscreenLabelDTO> getPermitNo() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<WindscreenLabelDTO> permitNoList = new ArrayList<WindscreenLabelDTO>();

		try {
			con = ConnectionManager.getConnection();

			String sql = "select pm_permit_no from public.nt_t_pm_application where pm_permit_no is not null order by pm_application_no;";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			WindscreenLabelDTO permitdto;
			while (rs.next()) {
				permitdto = new WindscreenLabelDTO();
				permitdto.setPermitNo(rs.getString("pm_permit_no"));
				permitNoList.add(permitdto);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return permitNoList;
	}

	@Override
	public List<WindscreenLabelDTO> getBusNumbers() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<WindscreenLabelDTO> dataList = new ArrayList<WindscreenLabelDTO>();

		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct pm_vehicle_regno from public.nt_t_pm_application where pm_vehicle_regno is not null and pm_status='A'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			WindscreenLabelDTO dto;

			while (rs.next()) {
				dto = new WindscreenLabelDTO();
				dto.setBusNo(rs.getString("pm_vehicle_regno"));
				dataList.add(dto);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return dataList;
	}

	@Override
	public WindscreenLabelDTO showSearchedDetails(String busNo, String permitNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		WindscreenLabelDTO showdataList = new WindscreenLabelDTO();

		try {

			con = ConnectionManager.getConnection();

			String query = "select a. pm_application_no ,a.pm_permit_expire_date,a.pm_valid_to,a.pm_route_no,b.rou_service_origine as origin ,b.rou_service_destination as destination ,b.rou_via as via,\r\n"
					+ "b.rou_service_origine_sin as originSinhala,b.rou_service_destination_sin as detinationSinhala,b.rou_via_sin as viasinhala,\r\n"
					+ "a.pm_service_type,c.description as servicetype,c.description_si as serviceTypeSinhala,a.pm_route_flag\r\n"
					+ "from public.nt_t_pm_application a\r\n"
					+ "inner join nt_r_route b on a.pm_route_no=b.rou_number\r\n"
					+ "inner join public.nt_r_service_types c on a.pm_service_type=c.code\r\n"
					+ "where a.pm_permit_no=?\r\n" + "and a.pm_vehicle_regno=? and a.pm_status='A'";

			ps = con.prepareStatement(query);
			ps.setString(1, permitNo);
			ps.setString(2, busNo);
			rs = ps.executeQuery();

			while (rs.next()) {

				showdataList.setServiceType(rs.getString("servicetype"));
				showdataList.setRouteNo(rs.getString("pm_route_no"));
				if (rs.getString("pm_route_flag") != null) {
					if (rs.getString("pm_route_flag").equalsIgnoreCase("Y")) {
						showdataList.setOrigin(rs.getString("destination"));
						showdataList.setDestination(rs.getString("origin"));

					} else if (rs.getString("pm_route_flag").equalsIgnoreCase("N")) {

						showdataList.setOrigin(rs.getString("origin"));
						showdataList.setDestination(rs.getString("destination"));
					}

				} else {
					showdataList.setOrigin(rs.getString("origin"));
					showdataList.setDestination(rs.getString("destination"));
				}
				showdataList.setVia(rs.getString("via"));
				showdataList.setExpiryDate(rs.getString("pm_permit_expire_date"));
				showdataList.setApplicationNo(rs.getString("pm_application_no"));

			}

		} catch (

		Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return showdataList;

	}

	@Override
	public WindscreenLabelDTO getPermitNo(String busNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		WindscreenLabelDTO permitFilterDTo = new WindscreenLabelDTO();
		try {

			con = ConnectionManager.getConnection();

			// get active records
			String query = "select distinct pm_permit_no from public.nt_t_pm_application where pm_vehicle_regno=? and pm_status='A';";

			ps = con.prepareStatement(query);
			ps.setString(1, busNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				permitFilterDTo = new WindscreenLabelDTO();
				permitFilterDTo.setPermitNo(rs.getString("pm_permit_no"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return permitFilterDTo;

	}

	@Override
	public WindscreenLabelDTO getBusStatus(String Busno, String permitNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		WindscreenLabelDTO dto = new WindscreenLabelDTO();
		try {

			con = ConnectionManager.getConnection();

			String query = "select pm_status from public.nt_t_pm_application where pm_permit_no=? and pm_vehicle_regno=?  and pm_status='A'";

			ps = con.prepareStatement(query);
			ps.setString(1, permitNo);
			ps.setString(2, Busno);
			rs = ps.executeQuery();

			while (rs.next()) {

				dto.setStatus(rs.getString("pm_status"));

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
	public void updateWindscreenLabel(WindscreenLabelDTO windscreenDTO, String loginUser) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_task_det");

			String sql = "INSERT INTO public.nt_h_task_his\r\n"
					+ "(tsd_seq, tsd_vehicle_no, tsd_app_no, tsd_task_code, tsd_status, created_by, created_date)\r\n"
					+ "VALUES(?, ?, ?, ?, ?, ?, ?);\r\n" + ";";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, windscreenDTO.getBusNo());
			stmt.setString(3, windscreenDTO.getApplicationNo());
			stmt.setString(4, "WL001");
			stmt.setString(5, "C");
			stmt.setString(6, loginUser);
			stmt.setTimestamp(7, timestamp);

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

}
