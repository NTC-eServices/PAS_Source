package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import lk.informatics.ntc.model.dto.FlyingManageInvestigationLogDTO;
import lk.informatics.ntc.model.dto.FlyingSquadInvestiMasterDTO;
import lk.informatics.ntc.model.dto.FlyngSquadAttendanceDTO;
import lk.informatics.ntc.view.util.ConnectionManager;

public class FlyingSquadInvestigationLogServiceImpl implements FlyingSquadInvestigationLogService {

	// get reference no list
	public ArrayList<FlyingSquadInvestiMasterDTO> getrefNo() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		ArrayList<FlyingSquadInvestiMasterDTO> refnoList = new ArrayList<FlyingSquadInvestiMasterDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT inv_refno\r\n" + "FROM public.nt_t_flying_investigation_master\r\n"
					+ "where inv_status not in ('P','R')\r\n" + "order by inv_refno";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {

				FlyingSquadInvestiMasterDTO flyingSquadInvestiMasterDTO = new FlyingSquadInvestiMasterDTO();
				flyingSquadInvestiMasterDTO.setRefNo(rs.getString("inv_refno"));

				refnoList.add(flyingSquadInvestiMasterDTO);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return refnoList;

	}

	// --- get the report no list;
	public ArrayList<FlyingManageInvestigationLogDTO> getreportNo(String refNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		ArrayList<FlyingManageInvestigationLogDTO> reportNoList = new ArrayList<FlyingManageInvestigationLogDTO>();
		try {
			con = ConnectionManager.getConnection();

			if (refNo != null) {
				String sql = "SELECT  inv_report_no\r\n" + "FROM public.nt_t_flying_investigation_log_master\r\n"
						+ "where inv_referenceno = ? \r\n";

				stmt = con.prepareStatement(sql);
				stmt.setString(1, refNo);
				rs = stmt.executeQuery();

				while (rs.next()) {

					FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
					flyingManageInvestigationLogDTO.setReportNo(rs.getString("inv_report_no"));

					reportNoList.add(flyingManageInvestigationLogDTO);
				}
			} else {

				String sql = "SELECT  inv_report_no\r\n" + "FROM public.nt_t_flying_investigation_log_master\r\n";

				stmt = con.prepareStatement(sql);
				rs = stmt.executeQuery();

				while (rs.next()) {

					FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
					flyingManageInvestigationLogDTO.setReportNo(rs.getString("inv_report_no"));

					reportNoList.add(flyingManageInvestigationLogDTO);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return reportNoList;

	}

	// get master details
	public FlyingManageInvestigationLogDTO getmasterData(String refNo, String reportNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();

		try {
			con = ConnectionManager.getConnection();
			String sql = null;
			if (reportNo != null && !reportNo.equalsIgnoreCase("") && !reportNo.isEmpty()) {
				sql = "SELECT inv_date, inv_group_cd, inv_start_time,inv_end_time, inv_vehicle_no, inv_places_investigation,group_name\r\n"
						+ "FROM public.nt_t_flying_investigation_log_master ,public.nt_r_group\r\n"
						+ "where inv_referenceno = ?\r\n" + "and inv_report_no = ?\r\n"
						+ "and nt_t_flying_investigation_log_master.inv_group_cd = nt_r_group.code";

				stmt = con.prepareStatement(sql);
				stmt.setString(1, refNo);
				stmt.setString(2, reportNo);
				rs = stmt.executeQuery();

				while (rs.next()) {

					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
					String strDate = formatter.format(rs.getTimestamp("inv_start_time"));
					String endDate = formatter.format(rs.getTimestamp("inv_end_time"));

					flyingManageInvestigationLogDTO.setInvestigationDate(rs.getDate("inv_date"));
					flyingManageInvestigationLogDTO.setGroupCd(rs.getString("inv_group_cd"));
					flyingManageInvestigationLogDTO.setStartTime(formatter.parse(strDate));
					flyingManageInvestigationLogDTO.setEndtime(formatter.parse(endDate));
					flyingManageInvestigationLogDTO.setVehicleNo(rs.getString("inv_vehicle_no"));
					flyingManageInvestigationLogDTO.setStartPlace(rs.getString("inv_places_investigation"));
					flyingManageInvestigationLogDTO.setGroupName(rs.getString("group_name"));
					flyingManageInvestigationLogDTO.setRefNo(refNo);
					flyingManageInvestigationLogDTO.setReportNo(reportNo);
				}

			} else {
				sql = "SELECT inv_date, inv_group_cd, inv_start_time,inv_end_time, \r\n"
						+ "inv_vehicle_no, inv_start_place,group_name \r\n"
						+ "FROM public.nt_t_flying_investigation_master,public.nt_r_group\r\n"
						+ "where inv_refno = ?\r\n"
						+ "and nt_t_flying_investigation_master.inv_group_cd = nt_r_group.code";

				stmt = con.prepareStatement(sql);
				stmt.setString(1, refNo);
				rs = stmt.executeQuery();

				while (rs.next()) {

					java.util.Date date = new java.util.Date();

					flyingManageInvestigationLogDTO.setInvestigationDate(rs.getDate("inv_date"));
					flyingManageInvestigationLogDTO.setGroupCd(rs.getString("inv_group_cd"));
					flyingManageInvestigationLogDTO.setStartTime(date);
					flyingManageInvestigationLogDTO.setEndtime(date);
					flyingManageInvestigationLogDTO.setVehicleNo(rs.getString("inv_vehicle_no"));
					flyingManageInvestigationLogDTO.setStartPlace(rs.getString("inv_start_place"));
					flyingManageInvestigationLogDTO.setGroupName(rs.getString("group_name"));
					flyingManageInvestigationLogDTO.setRefNo(refNo);
					flyingManageInvestigationLogDTO.setReportNo(reportNo);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return flyingManageInvestigationLogDTO;

	}

	// save master Table
	public String savemasterdata(FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		String reportno = null;
		try {
			java.sql.Date sqlDate = new java.sql.Date(flyingManageInvestigationLogDTO.getInvestigationDate().getTime());
			java.util.Date date = new java.util.Date();
			Timestamp timeStamp = new Timestamp(date.getTime());
			Timestamp timeStampstart = new Timestamp(flyingManageInvestigationLogDTO.getStartTime().getTime());
			Timestamp timeStampend = new Timestamp(flyingManageInvestigationLogDTO.getEndtime().getTime());
			con = ConnectionManager.getConnection();

			if (flyingManageInvestigationLogDTO.getReportNo() == null
					|| flyingManageInvestigationLogDTO.getReportNo().equalsIgnoreCase("")) {
				reportno = getReportNo(flyingManageInvestigationLogDTO.getRefNo());
				flyingManageInvestigationLogDTO.setReportNo(reportno);
				String sql = "INSERT INTO public.nt_t_flying_investigation_log_master\r\n"
						+ "(inv_referenceno, inv_report_no, inv_date, inv_group_cd, inv_start_time,\r\n"
						+ "inv_end_time, inv_vehicle_no, inv_places_investigation, inv_created_date, inv_created_by)\r\n"
						+ "VALUES(?,?, ?,?,?,?,?,?,?,?)";

				stmt = con.prepareStatement(sql);
				stmt.setString(1, flyingManageInvestigationLogDTO.getRefNo());
				stmt.setString(2, flyingManageInvestigationLogDTO.getReportNo());
				stmt.setDate(3, sqlDate);
				stmt.setString(4, flyingManageInvestigationLogDTO.getGroupCd());
				stmt.setTimestamp(5, timeStampstart);
				stmt.setTimestamp(6, timeStampend);
				stmt.setString(7, flyingManageInvestigationLogDTO.getVehicleNo());
				stmt.setString(8, flyingManageInvestigationLogDTO.getStartPlace());
				stmt.setTimestamp(9, timeStamp);
				stmt.setString(10, user);

				stmt.executeUpdate();
				con.commit();

			}

			else {
				String sql = "UPDATE public.nt_t_flying_investigation_log_master\r\n"
						+ "SET inv_referenceno= ?, inv_date=?, inv_group_cd=?, \r\n"
						+ "inv_start_time=?, inv_end_time=?, inv_vehicle_no=?, \r\n"
						+ "inv_places_investigation=?,  inv_modified_date=?, inv_modified_by=?\r\n"
						+ "WHERE inv_report_no=?";

				stmt = con.prepareStatement(sql);
				stmt.setString(1, flyingManageInvestigationLogDTO.getRefNo());
				stmt.setDate(2, sqlDate);
				stmt.setString(3, flyingManageInvestigationLogDTO.getGroupCd());
				stmt.setTimestamp(4, timeStampstart);
				stmt.setTimestamp(5, timeStampend);
				stmt.setString(6, flyingManageInvestigationLogDTO.getVehicleNo());
				stmt.setString(7, flyingManageInvestigationLogDTO.getStartPlace());
				stmt.setTimestamp(8, timeStamp);
				stmt.setString(9, user);
				stmt.setString(10, flyingManageInvestigationLogDTO.getReportNo());

				reportno = flyingManageInvestigationLogDTO.getReportNo();
				stmt.executeUpdate();
				con.commit();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
		return reportno;
	}

	// Generate ReportNo
	public String getReportNo(String refNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String reportNo = null;
		int seqno = 0;
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT  inv_report_no\r\n" + "FROM public.nt_t_flying_investigation_log_master\r\n"
					+ "where inv_referenceno = ?\r\n" + "order by  inv_report_no desc limit 1";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, refNo);
			rs = stmt.executeQuery();

			if (rs.getFetchSize() == 0) {
				seqno = 01;
				reportNo = refNo + StringUtils.leftPad(String.valueOf(seqno), 2, '0');
			}
			while (rs.next()) {
				String no = rs.getString("inv_report_no");
				String no1 = no.substring(10, 12);
				seqno = Integer.parseInt(no1);
				seqno = seqno + 1;
				reportNo = refNo + StringUtils.leftPad(String.valueOf(seqno), 2, '0');
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return reportNo;
	}

	// get details
	public ArrayList<FlyingManageInvestigationLogDTO> getdetails(String refNo, String reportNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		ArrayList<FlyingManageInvestigationLogDTO> detailList = new ArrayList<FlyingManageInvestigationLogDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT  inv_det_route_no,inv_det_route_from, inv_det_route_to, inv_det_service_cd, inv_det_time, \r\n"
					+ "inv_det_fault, inv_det_documents, inv_det_invest_place, inv_det_remark,inv_investigation_no, inv_det_vehicle_no,description,inv_service_category,inv_permit_no  \r\n"
					+ "FROM public.nt_t_flying_investigation_log_det left outer join nt_r_service_types on inv_det_service_cd = code \r\n"
					+ "where inv_det_ref_no = ? \r\n" + "and inv_det_report_no =?\r\n"
					+ "and COALESCE(inv_det_delete, 'E') <> 'D' ";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, refNo);
			stmt.setString(2, reportNo);
			rs = stmt.executeQuery();

			while (rs.next()) {

				FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
				flyingManageInvestigationLogDTO.setRouteNo(rs.getString("inv_det_route_no"));
				flyingManageInvestigationLogDTO.setRouteFrom(rs.getString("inv_det_route_from"));
				flyingManageInvestigationLogDTO.setRouteTo(rs.getString("inv_det_route_to"));
				flyingManageInvestigationLogDTO.setServiceTypeCd(rs.getString("inv_det_service_cd"));
				flyingManageInvestigationLogDTO.setCurrentTime(rs.getTimestamp("inv_det_time"));
				flyingManageInvestigationLogDTO.setFault(rs.getString("inv_det_fault"));
				flyingManageInvestigationLogDTO.setDocuments(rs.getString("inv_det_documents"));
				flyingManageInvestigationLogDTO.setInvestigationPlace(rs.getString("inv_det_invest_place"));
				flyingManageInvestigationLogDTO.setRemarks(rs.getString("inv_det_remark"));
				flyingManageInvestigationLogDTO.setBusNo(rs.getString("inv_det_vehicle_no"));
				flyingManageInvestigationLogDTO.setInvesNo(rs.getString("inv_investigation_no"));
				flyingManageInvestigationLogDTO
						.setRoutedes(rs.getString("inv_det_route_from") + "-" + rs.getString("inv_det_route_to"));
				flyingManageInvestigationLogDTO.setServiceTypeDes(rs.getString("description"));
				flyingManageInvestigationLogDTO.setServiceCategory(rs.getString("inv_service_category"));
				flyingManageInvestigationLogDTO.setPermitNo(rs.getString("inv_permit_no"));
				detailList.add(flyingManageInvestigationLogDTO);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return detailList;

	}

	// routeno list
	public List<FlyingManageInvestigationLogDTO> routeNodropdown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<FlyingManageInvestigationLogDTO> returnList = new ArrayList<FlyingManageInvestigationLogDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT rou_number,rou_description" + " FROM nt_r_route" + " where active='A'"
					+ " order by rou_number";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
				flyingManageInvestigationLogDTO.setRouteNo(rs.getString("rou_number"));
				flyingManageInvestigationLogDTO.setRoutedes(rs.getString("rou_description"));

				returnList.add(flyingManageInvestigationLogDTO);

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

	// GET ROUTE DETAILS
	public FlyingManageInvestigationLogDTO getroutedata(String routeNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();

		try {
			con = ConnectionManager.getConnection();
			String sql = null;

			sql = "select rou_service_origine, rou_service_destination\r\n" + "FROM public.nt_r_route \r\n"
					+ "where rou_number =?\r\n";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, routeNo);
			rs = stmt.executeQuery();

			while (rs.next()) {
				flyingManageInvestigationLogDTO.setRouteFrom(rs.getString("rou_service_origine"));
				flyingManageInvestigationLogDTO.setRouteTo(rs.getString("rou_service_destination"));

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
		return flyingManageInvestigationLogDTO;

	}

	// --get vehicle list
	public List<FlyingManageInvestigationLogDTO> getVehicleDetail() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<FlyingManageInvestigationLogDTO> vehicleList = new ArrayList<FlyingManageInvestigationLogDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct(pm_vehicle_regno)\r\n" + "from public.nt_t_pm_application"
					+ " WHERE pm_status!='C' AND pm_status!='I' AND pm_permit_no is not null;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
				flyingManageInvestigationLogDTO.setBusNo(rs.getString("pm_vehicle_regno"));

				vehicleList.add(flyingManageInvestigationLogDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return vehicleList;

	}

	// get vehicle details
	public FlyingManageInvestigationLogDTO getVehicle(String vehicleNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();

		try {
			con = ConnectionManager.getConnection();
			String sql = null;

			sql = " select pm_route_no ,rou_service_origine, rou_service_destination,pm_service_type,description\r\n"
					+ "from public.nt_t_pm_application ,nt_r_route ,nt_r_service_types\r\n"
					+ "where pm_vehicle_regno =?\r\n"
					+ "and nt_r_route.rou_number = nt_t_pm_application.pm_route_no \r\n"
					+ "and nt_r_service_types.code = nt_t_pm_application.pm_service_type\r\n"
					+ "order by pm_application_no desc limit 1";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, vehicleNo);
			rs = stmt.executeQuery();

			while (rs.next()) {

				java.util.Date date = new java.util.Date();
				Timestamp timeStamp = new Timestamp(date.getTime());

				flyingManageInvestigationLogDTO.setRouteNo(rs.getString("pm_route_no"));
				flyingManageInvestigationLogDTO.setRouteFrom(rs.getString("rou_service_origine"));
				flyingManageInvestigationLogDTO.setRouteTo(rs.getString("rou_service_destination"));
				flyingManageInvestigationLogDTO.setServiceTypeCd(rs.getString("pm_service_type"));
				flyingManageInvestigationLogDTO.setServiceTypeDes(rs.getString("description"));
				flyingManageInvestigationLogDTO.setCurrentTime(timeStamp);
				flyingManageInvestigationLogDTO.setBusNo(vehicleNo);

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
		return flyingManageInvestigationLogDTO;

	}

	// save data
	public void savedetaildata(FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO, String user,
			String reportNo, String refNo) {
		Connection con = null;
		PreparedStatement stmt = null;

		try {
			java.util.Date date = new java.util.Date();
			Timestamp timeStamp = new Timestamp(date.getTime());
			String invesNo = null;
			invesNo = getInvesNo(reportNo);
			
		
			con = ConnectionManager.getConnection();
			String sql = "INSERT INTO public.nt_t_flying_investigation_log_det\r\n"
					+ "(inv_det_ref_no, inv_det_report_no, inv_det_route_no, inv_det_route_from, inv_det_route_to,\r\n"
					+ "inv_det_service_cd, inv_det_time, inv_det_fault, inv_det_documents, inv_det_invest_place,\r\n"
					+ "inv_det_remark, inv_investigation_no, inv_det_vehicle_no,inv_det_created_by, inv_det_created_date,inv_service_category, inv_permit_no )\r\n"
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?)";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, refNo);
			stmt.setString(2, reportNo);
			stmt.setString(3, flyingManageInvestigationLogDTO.getRouteNo());
			stmt.setString(4, flyingManageInvestigationLogDTO.getRouteFrom());
			stmt.setString(5, flyingManageInvestigationLogDTO.getRouteTo());
			stmt.setString(6, flyingManageInvestigationLogDTO.getServiceTypeCd());
			Timestamp investigationLogTime = new Timestamp(flyingManageInvestigationLogDTO.getCurrentTime().getTime());
			stmt.setTimestamp(7, investigationLogTime);
			/**inv_det_time value change as entered date , not only the current date. requested by Pramitha on 24/03/2022 over the phone**/
			stmt.setString(8, flyingManageInvestigationLogDTO.getFault());
			stmt.setString(9, flyingManageInvestigationLogDTO.getDocuments());
			stmt.setString(10, flyingManageInvestigationLogDTO.getInvestigationPlace());
			stmt.setString(11, flyingManageInvestigationLogDTO.getRemarks());
			stmt.setString(12, invesNo);
			stmt.setString(13, flyingManageInvestigationLogDTO.getBusNo());
			stmt.setString(14, user);
			stmt.setTimestamp(15, timeStamp);
			stmt.setString(16, flyingManageInvestigationLogDTO.getServiceCategory());
			stmt.setString(17, flyingManageInvestigationLogDTO.getPermitNo());

			stmt.executeUpdate();
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
	}

	// --genarate invesno
	public String getInvesNo(String reportNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String invesNo = null;
		int seqno = 0;
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT  inv_investigation_no\r\n" + "FROM public.nt_t_flying_investigation_log_det\r\n"
					+ "where inv_det_report_no = ? \r\n" + "order by  inv_investigation_no desc limit 1";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, reportNo);
			rs = stmt.executeQuery();

			if (rs.getFetchSize() == 0) {
				seqno = 001;
				invesNo = reportNo + StringUtils.leftPad(String.valueOf(seqno), 3, '0');
			}
			while (rs.next()) {
				String no = rs.getString("inv_investigation_no");
				String no1 = no.substring(12, 15);
				seqno = Integer.parseInt(no1);
				seqno = seqno + 1;
				invesNo = reportNo + StringUtils.leftPad(String.valueOf(seqno), 3, '0');
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return invesNo;
	}

	// update data
	public void updatedetaildata(FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO, String user,
			String reportNo, String refNo) {
		Connection con = null;
		PreparedStatement stmt = null;

		try {
			java.util.Date date = new java.util.Date();
			Timestamp timeStamp = new Timestamp(date.getTime());

			con = ConnectionManager.getConnection();
			String sql = "UPDATE public.nt_t_flying_investigation_log_det\r\n"
					+ "SET inv_det_ref_no=?, inv_det_report_no=?, inv_det_route_no=?,\r\n"
					+ "inv_det_route_from=?, inv_det_route_to=?, inv_det_service_cd=?, \r\n"
					+ "inv_det_time=?, inv_det_fault=?, inv_det_documents=?, inv_det_invest_place=?, inv_det_remark=?,  \r\n"
					+ "inv_det_vehicle_no=?,inv_det_modified_by=?, inv_modified_date=?, inv_service_category =? \r\n"
					+ "WHERE inv_investigation_no=?";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, refNo);
			stmt.setString(2, reportNo);
			stmt.setString(3, flyingManageInvestigationLogDTO.getRouteNo());
			stmt.setString(4, flyingManageInvestigationLogDTO.getRouteFrom());
			stmt.setString(5, flyingManageInvestigationLogDTO.getRouteTo());
			stmt.setString(6, flyingManageInvestigationLogDTO.getServiceTypeCd());
			Timestamp investigationLogTime = new Timestamp(flyingManageInvestigationLogDTO.getCurrentTime().getTime());
			/**inv_det_time value change as entered date , not only the current date. requested by Pramitha on 24/03/2022 over the phone**/
			stmt.setTimestamp(7, investigationLogTime);
			stmt.setString(8, flyingManageInvestigationLogDTO.getFault());
			stmt.setString(9, flyingManageInvestigationLogDTO.getDocuments());
			stmt.setString(10, flyingManageInvestigationLogDTO.getInvestigationPlace());
			stmt.setString(11, flyingManageInvestigationLogDTO.getRemarks());
			stmt.setString(12, flyingManageInvestigationLogDTO.getBusNo());
			stmt.setString(13, user);
			stmt.setTimestamp(14, timeStamp);
			stmt.setString(15, flyingManageInvestigationLogDTO.getServiceCategory());
			stmt.setString(16, flyingManageInvestigationLogDTO.getInvesNo());

			stmt.executeUpdate();
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
	}

	// update detail
	public void updatemasterdata(String user) {
		Connection con = null;
		PreparedStatement stmt = null;

		try {
			SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
			java.util.Date date = new java.util.Date();
			Timestamp timeStamp = new Timestamp(date.getTime());
			String ts = localDateFormat.format(timeStamp);

			con = ConnectionManager.getConnection();
			String sql = "UPDATE public.nt_t_flying_investigation_log_master\r\n"
					+ "SET  inv_end_time=?, inv_modified_date=?, inv_modified_by=?\r\n" + "WHERE inv_report_no=?";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, ts);
			stmt.setTimestamp(2, timeStamp);
			stmt.setString(3, user);

			stmt.executeUpdate();
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
	}

	public List<FlyingManageInvestigationLogDTO> getServiceTypeToDropdown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<FlyingManageInvestigationLogDTO> returnList = new ArrayList<FlyingManageInvestigationLogDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code,description " + " FROM public.nt_r_service_types " + " WHERE active = 'A' "
					+ " ORDER BY code ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
				flyingManageInvestigationLogDTO.setServiceTypeCd(rs.getString("code"));
				flyingManageInvestigationLogDTO.setServiceTypeDes(rs.getString("description"));

				returnList.add(flyingManageInvestigationLogDTO);

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

	// delete record
	public void updateStatus(String invesno, FlyingManageInvestigationLogDTO deletededflyingManageInvestigationLogDTO,
			String user) {
		Connection con = null;
		PreparedStatement stmt = null;

		try {
			java.util.Date date = new java.util.Date();
			Timestamp timeStamp = new Timestamp(date.getTime());
			Timestamp timeStampp = null;
			if (deletededflyingManageInvestigationLogDTO.getInvestigationDate() != null) {

				timeStampp = new Timestamp(deletededflyingManageInvestigationLogDTO.getInvestigationDate().getTime());
			}

			con = ConnectionManager.getConnection();
			String sql = "INSERT INTO public.nt_t_flying_investigation_log_delete\r\n"
					+ "(inv_del_ref_no, inv_del_report_no, inv_del_route_no, inv_det_route_from, \r\n"
					+ "inv_det_route_to, inv_det_service_cd, inv_det_time, inv_det_fault, \r\n"
					+ "inv_det_documents, inv_det_invest_place, inv_det_remark, \r\n" + " inv_investigation_no,\r\n"
					+ "inv_det_vehicle_no, inv_det_delete, inv_det_delete_by, inv_det_delete_date)\r\n"
					+ "VALUES(?, ?, ?, ?, ?, ?, ?,?, ?, ?,?,?,?, ?, ?, ?)";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, deletededflyingManageInvestigationLogDTO.getRefNo());
			stmt.setString(2, deletededflyingManageInvestigationLogDTO.getReportNo());
			stmt.setString(3, deletededflyingManageInvestigationLogDTO.getRouteNo());
			stmt.setString(4, deletededflyingManageInvestigationLogDTO.getRouteFrom());
			stmt.setString(5, deletededflyingManageInvestigationLogDTO.getRouteTo());
			stmt.setString(6, deletededflyingManageInvestigationLogDTO.getServiceTypeCd());
			stmt.setTimestamp(7, timeStampp);
			stmt.setString(8, deletededflyingManageInvestigationLogDTO.getFault());
			stmt.setString(9, deletededflyingManageInvestigationLogDTO.getDocuments());
			stmt.setString(10, deletededflyingManageInvestigationLogDTO.getInvestigationPlace());
			stmt.setString(11, deletededflyingManageInvestigationLogDTO.getRemarks());
			stmt.setString(12, deletededflyingManageInvestigationLogDTO.getInvesNo());
			stmt.setString(13, deletededflyingManageInvestigationLogDTO.getBusNo());
			stmt.setString(14, "D");
			stmt.setString(15, user);
			stmt.setTimestamp(16, timeStamp);

			stmt.executeUpdate();
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
	}
	// delete from det table

	public void delete(String invesno) {
		Connection con = null;
		PreparedStatement stmt = null;

		try {

			con = ConnectionManager.getConnection();
			String sql = "	DELETE FROM public.nt_t_flying_investigation_log_det\r\n"
					+ "		WHERE inv_investigation_no=?";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, invesno);

			stmt.executeUpdate();
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
	}

	// update enddate
	public void updateendDate(String reportNo, String user) {
		Connection con = null;
		PreparedStatement stmt = null;

		try {
			java.util.Date date = new java.util.Date();
			Timestamp timeStamp = new Timestamp(date.getTime());

			con = ConnectionManager.getConnection();
			String sql = "UPDATE public.nt_t_flying_investigation_log_master\r\n"
					+ "SET inv_end_time=?, inv_modified_date=?, inv_modified_by=?\r\n" + "WHERE inv_report_no=?";

			stmt = con.prepareStatement(sql);
			stmt.setTimestamp(1, timeStamp);
			stmt.setTimestamp(2, timeStamp);
			stmt.setString(3, user);
			stmt.setString(4, reportNo);

			stmt.executeUpdate();
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
	}

	// update log table
	public void updatemasterdet(FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO, String user,
			String refno, String reportNo) {
		Connection con = null;
		PreparedStatement stmt = null;

		try {
			java.util.Date date = new java.util.Date();
			Timestamp timeStamp = new Timestamp(date.getTime());

			con = ConnectionManager.getConnection();
			String sql = "UPDATE public.nt_t_flying_investigation_log_master\r\n"
					+ "SET  inv_modified_date=?, inv_modified_by=?, inv_observations=?,\r\n"
					+ "inv_changes=?, inv_defect_vehicle=?, inv_km_travelled=?\r\n" + "WHERE inv_report_no=?\r\n"
					+ "and inv_referenceno=?";

			stmt = con.prepareStatement(sql);
			stmt.setTimestamp(1, timeStamp);
			stmt.setString(2, user);
			stmt.setString(3, flyingManageInvestigationLogDTO.getObservations());
			stmt.setString(4, flyingManageInvestigationLogDTO.getChanges());
			stmt.setString(5, flyingManageInvestigationLogDTO.getDefects());
			stmt.setString(6, flyingManageInvestigationLogDTO.getKilometers());
			stmt.setString(7, reportNo);
			stmt.setString(8, refno);
			stmt.executeUpdate();
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
	}

	// get officers
	public ArrayList<FlyngSquadAttendanceDTO> getexsistingdetails(String referenceNo, Date invesDate) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<FlyngSquadAttendanceDTO> DetailList = new ArrayList<FlyngSquadAttendanceDTO>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

		try {

			con = ConnectionManager.getConnection();
			String sql = "SELECT inv_empno, inv_intime, inv_outtime,inv_det_name,inv_attended ,\r\n"
					+ "inv_created_by, inv_created_date, inv_modified_by, inv_modified_date ,inv_date,inv_det_designation\r\n"
					+ "FROM public.nt_t_flying_investigation_attendance ,nt_t_flying_investigation_details \r\n"
					+ "where inv_refno = ?\r\n"
					+ "and nt_t_flying_investigation_attendance.inv_refno=nt_t_flying_investigation_details.inv_det_refno\r\n"
					+ "and nt_t_flying_investigation_attendance.inv_empno = nt_t_flying_investigation_details.inv_det_empno\r\n"
					+ "and nt_t_flying_investigation_attendance.inv_attended = 'true'" + " order by inv_date";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, referenceNo);
			rs = stmt.executeQuery();

			while (rs.next()) {
				String startTime = null;
				startTime = rs.getString("inv_intime");
				String endTime = null;
				endTime = rs.getString("inv_outtime");

				FlyngSquadAttendanceDTO flyngSquadAttendanceDTO = new FlyngSquadAttendanceDTO();
				flyngSquadAttendanceDTO.setEpfNo(rs.getString("inv_empno"));
				flyngSquadAttendanceDTO.setStartTimen(startTime);
				flyngSquadAttendanceDTO.setEndtimen(endTime);
				flyngSquadAttendanceDTO.setEmpName(rs.getString("inv_det_name"));
				flyngSquadAttendanceDTO.setIsattend(rs.getBoolean("inv_attended"));
				flyngSquadAttendanceDTO.setIsupdate(true);
				flyngSquadAttendanceDTO.setCreatedBy(rs.getString("inv_created_by"));
				flyngSquadAttendanceDTO.setModifiedby(rs.getString("inv_modified_by"));
				flyngSquadAttendanceDTO.setCreatedDate(rs.getTimestamp("inv_created_date"));
				flyngSquadAttendanceDTO.setModifiedDate(rs.getTimestamp("inv_modified_date"));
				flyngSquadAttendanceDTO.setDesignation(rs.getString("inv_det_designation"));
				flyngSquadAttendanceDTO.setIvesDaten(sdf.format(rs.getTimestamp("inv_date")));
				DetailList.add(flyngSquadAttendanceDTO);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
		return DetailList;

	}

	// refno
	public ArrayList<FlyingSquadInvestiMasterDTO> getrefNon() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		ArrayList<FlyingSquadInvestiMasterDTO> refnoList = new ArrayList<FlyingSquadInvestiMasterDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT inv_refno\r\n" + "FROM public.nt_t_flying_investigation_master\r\n"
					+ "where inv_status not in ('P','R')\r\n" + "order by inv_refno";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {

				FlyingSquadInvestiMasterDTO flyingSquadInvestiMasterDTO = new FlyingSquadInvestiMasterDTO();
				flyingSquadInvestiMasterDTO.setRefNo(rs.getString("inv_refno"));

				refnoList.add(flyingSquadInvestiMasterDTO);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return refnoList;

	}

	public FlyingManageInvestigationLogDTO getdetail(String refno, String reportNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT inv_observations, inv_changes, inv_defect_vehicle, inv_km_travelled\r\n"
					+ "FROM public.nt_t_flying_investigation_log_master\r\n" + "where inv_referenceno = ? \r\n"
					+ "and inv_report_no = ?";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, refno);
			stmt.setString(2, reportNo);
			rs = stmt.executeQuery();

			while (rs.next()) {
				flyingManageInvestigationLogDTO.setObservations(rs.getString("inv_observations"));
				flyingManageInvestigationLogDTO.setChanges(rs.getString("inv_changes"));
				flyingManageInvestigationLogDTO.setDefects(rs.getString("inv_defect_vehicle"));
				flyingManageInvestigationLogDTO.setKilometers(rs.getString("inv_km_travelled"));

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
		return flyingManageInvestigationLogDTO;
	}

	public String getrefNoNew(String reportNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String refNo = null;

		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT inv_det_ref_no\r\n" + "FROM public.nt_t_flying_investigation_log_det\r\n"
					+ "where inv_det_report_no = ? \r\n" + "order by inv_det_ref_no";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, reportNo);
			rs = stmt.executeQuery();

			while (rs.next()) {

				refNo = rs.getString("inv_det_ref_no");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return refNo;

	}

	@Override
	public List<FlyingManageInvestigationLogDTO> getPermitNoByService(String serviceType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<FlyingManageInvestigationLogDTO> permitNoList = new ArrayList<FlyingManageInvestigationLogDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = null;

			// set Query for specific Service

			switch (serviceType) {
			case "NT":
				query = "SELECT DISTINCT  pm_vehicle_regno as busNo FROM public.nt_t_pm_application WHERE pm_status!='C' AND pm_status!='I' AND pm_permit_no is not null;";
				break;

			case "S":
				query = "SELECT DISTINCT  sps_bus_no as busNo FROM public.nt_m_sisu_permit_hol_service_info WHERE sps_status='A'AND sps_permit_no is not null;";
				break;

			case "G":
				query = "SELECT DISTINCT  gps_bus_no as busNo FROM public.nt_m_gami_permit_hol_service_info WHERE gps_status='A' AND gps_permit_no_prpta is not null;";
				break;

			case "N":
				query = "SELECT DISTINCT  nri_bus_no as busNo FROM public.nt_m_nri_requester_info WHERE nri_status='A'AND nri_permit_no is not null;";
				break;

			default:
				break;
			}

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
				flyingManageInvestigationLogDTO.setBusNo(rs.getString("busNo"));

				permitNoList.add(flyingManageInvestigationLogDTO);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return permitNoList;
	}

	@Override
	public FlyingManageInvestigationLogDTO getVehicle(String vehicleNo, String serviceType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = null;

			// set Query for specific Service

			switch (serviceType) {
			case "NT":
				query = "select pm_route_no as routeNo ,rou_service_origine as rouorigine, rou_service_destination as roudestination,pm_service_type as servicetype,description as description \r\n"
						+ "from public.nt_t_pm_application ,nt_r_route ,nt_r_service_types\r\n"
						+ "where pm_vehicle_regno =?\r\n"
						+ "and nt_r_route.rou_number = nt_t_pm_application.pm_route_no and nt_r_service_types.code = nt_t_pm_application.pm_service_type and pm_status != 'I' and pm_status!='C' order by pm_application_no desc limit 1;";
				break;

			case "S":
				query = "select '' as routeNo , sps_origin as rouorigine, sps_destination as roudestination,sps_service_type_code as servicetype,description as description "
						+ " from public.nt_m_sisu_permit_hol_service_info ,nt_r_route ,nt_r_service_types "
						+ " where sps_bus_no = ? "
						+ " and  nt_r_service_types.code = sps_service_type_code order by sps_request_no desc limit 1;";
				break;

			case "G":
				query = " select '' as routeNo , gps_origin as rouorigine, gps_destination as roudestination,gps_service_type_code as servicetype,description as description "
						+ " from public.nt_m_gami_permit_hol_service_info ,nt_r_route ,nt_r_service_types "
						+ " where gps_bus_no = ? "
						+ " and  nt_r_service_types.code = gps_service_type_code order by gps_service_ref_no desc limit 1;";
				break;

			case "N":
				query = "select '' as routeNo , nri_origin as rouorigine, nri_destination as roudestination,nri_service_type as servicetype,description as description "
						+ " from public.nt_m_nri_requester_info ,nt_r_route ,nt_r_service_types "
						+ "where nri_bus_no = ? "
						+ "and  nt_r_service_types.code = nri_service_type order by nri_service_ref_no desc limit 1;";
				break;

			default:
				break;
			}

			ps = con.prepareStatement(query);
			ps.setString(1, vehicleNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				java.util.Date date = new java.util.Date();
				Timestamp timeStamp = new Timestamp(date.getTime());

				flyingManageInvestigationLogDTO.setRouteNo(rs.getString("routeNo"));
				flyingManageInvestigationLogDTO.setRouteFrom(rs.getString("rouorigine"));
				flyingManageInvestigationLogDTO.setRouteTo(rs.getString("roudestination"));
				flyingManageInvestigationLogDTO.setServiceTypeCd(rs.getString("servicetype"));
				flyingManageInvestigationLogDTO.setServiceTypeDes(rs.getString("description"));
				flyingManageInvestigationLogDTO.setCurrentTime(timeStamp);
				flyingManageInvestigationLogDTO.setBusNo(vehicleNo);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return flyingManageInvestigationLogDTO;
	}

	@Override
	public Date getInvestigationDateAndTime(String refNo) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String time = null;
		Date investDate = null ;
		String investDateAndTimeString = null;
		Date investDateAndTime = null;
		Date date = Calendar.getInstance().getTime();  
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT inv_date ,inv_start_time FROM public.nt_t_flying_investigation_master \r\n" + 
					"WHERE inv_refno =?";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, refNo);
			rs = stmt.executeQuery();

			while (rs.next()) {
				time = rs.getString("inv_start_time");
				investDate = rs.getDate("inv_date");
			}
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
			DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");  
			String strDate = dateFormat.format(investDate);  
			investDateAndTimeString =strDate + " " +time;
			investDateAndTime = dateFormat2.parse(investDateAndTimeString);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return investDateAndTime;
	
	}

	@Override
	public List<String> getRouteDataNew(String routeNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		List<String> routeData = new ArrayList();
		try {
			
			con = ConnectionManager.getConnection();
			String sql = "select rou_service_origine, rou_service_destination\r\n" + "FROM public.nt_r_route \r\n"
					+ "where rou_number =?\r\n";
			
			ps = con.prepareStatement(sql);
			ps.setString(1,routeNo);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				
				routeData.add(rs.getString("rou_service_origine"));
				routeData.add(rs.getString("rou_service_destination"));
			}
			
	}catch(Exception ex) {
		ex.printStackTrace();
		}
		return routeData;
	}

}
