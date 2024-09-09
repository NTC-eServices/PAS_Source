package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import lk.informatics.ntc.model.dto.VehicleInspectionDTO;
import lk.informatics.ntc.view.util.ConnectionManager;

public class TemporaryHoldServiceImpl implements TemporaryHoldService {
	private static final long serialVersionUID = 1L;

	public static Logger logger = Logger.getLogger("TemporaryHoldServiceImpl");

	@Override
	public List<VehicleInspectionDTO> getPermitNoList() {
		logger.info("getPermitNoList start");
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<VehicleInspectionDTO> returnList = new ArrayList<VehicleInspectionDTO>();

		try {
			con = ConnectionManager.getConnection();
			String query = "select distinct pm_permit_no FROM public.nt_t_pm_application WHERE pm_status in('P','O','H') and pm_permit_no is not null  order by pm_permit_no;";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				VehicleInspectionDTO printInspectionDTO = new VehicleInspectionDTO();
				printInspectionDTO.setPermitNo(rs.getString("pm_permit_no"));
				returnList.add(printInspectionDTO);
			}

		} catch (Exception e) {
			logger.info("getPermitNoList error");
			logger.info(e);
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		logger.info("getPermitNoList end");
		return returnList;
	}

	@Override
	public List<VehicleInspectionDTO> getApplicationNoList(String selectedPermitNo) {
		logger.info("getApplicationNoList start");
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<VehicleInspectionDTO> returnList = new ArrayList<VehicleInspectionDTO>();

		try {
			con = ConnectionManager.getConnection();
			String query = "select distinct pm_permit_no,pm_application_no,pm_status FROM public.nt_t_pm_application WHERE pm_status in('P','O','H') and pm_permit_no=? order by pm_application_no;";
			ps = con.prepareStatement(query);
			ps.setString(1, selectedPermitNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				VehicleInspectionDTO printInspectionDTO = new VehicleInspectionDTO();
				printInspectionDTO.setPermitNo(rs.getString("pm_permit_no"));
				printInspectionDTO.setApplicationNo(rs.getString("pm_application_no"));
				printInspectionDTO.setApplicationStatus(rs.getString("pm_status"));
				returnList.add(printInspectionDTO);
			}

		} catch (Exception e) {
			logger.info("getApplicationNoList error");
			logger.info(e);
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		logger.info("getApplicationNoList end");
		return returnList;
	}

	@Override
	public String getApplicationStatus(String selectedApplicationNo) {
		logger.info("getApplicationStatus start");
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String returnStatus = null;

		try {
			con = ConnectionManager.getConnection();
			String query = "select distinct pm_permit_no,pm_application_no,pm_status FROM public.nt_t_pm_application WHERE pm_status in('P','O','H') and pm_application_no=?;";
			ps = con.prepareStatement(query);
			ps.setString(1, selectedApplicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				returnStatus = rs.getString("pm_status");
			}

		} catch (Exception e) {
			logger.info("getApplicationStatus error");
			logger.info(e);
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		logger.info("getApplicationStatus end");
		return returnStatus;
	}

	@Override
	public boolean temporaryHold(String selectedApplicationNo, String loginUser) {
		logger.info("temporaryHold start");
		boolean returnVal = false;

		Connection con = null;
		try {
			con = ConnectionManager.getConnection();

			boolean success_1 = insertToTempApplication(selectedApplicationNo, con);
			boolean success_2 = updateStatusInApplication(selectedApplicationNo, con);
			boolean success_3 = removeQueueRecordsInQueueMaster(selectedApplicationNo, con);
			boolean success_4 = removeTaskRecordsInsertNewHistoryRecord(selectedApplicationNo, loginUser, con);

			con.commit();
			returnVal = true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("temporaryHold error");
			logger.error(e);
		} finally {

			ConnectionManager.close(con);
		}
		logger.info("temporaryHold end");
		return returnVal;
	}

	private boolean insertToTempApplication(String selectedApplicationNo, Connection con) throws Exception {
		logger.info("insertToTempApplication start");
		boolean returnVal = false;
		int success = 0;
		
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;
		
		String query = "select * from public.nt_t_pm_application where pm_application_no =?";
		ps = con.prepareStatement(query);
		ps.setString(1, selectedApplicationNo);
		rs = ps.executeQuery();
		
		while (rs.next()) {
			String query2 = "INSERT INTO public.nt_temp_pm_application "
					+ "(seq, pm_application_no, pm_queue_no, pm_permit_no, pm_vehicle_regno, pm_permit_issue_date, pm_permit_issue_by, pm_inspect_date, pm_tender_ref_no, "
					+ "pm_status, pm_tender_annual_fee, pm_installments, pm_is_permit_print, pm_reject_reason, pm_valid_from, pm_valid_to, pm_approved_by, pm_appoved_date, "
					+ "pm_special_remark, pm_is_tender_permit, pm_service_type, pm_route_no, pm_tot_bus_fare, pm_is_backlog_app, pm_created_by, pm_created_date, pm_modified_by, "
					+ "pm_modified_date, pm_permit_expire_date, pm_renewal_period, pm_next_ins_date_sec1_2, pm_next_ins_date_sec3, pm_inspect_remark, pm_route_flag, "
					+ "pm_isnew_permit, pm_reinspec_status, pm_new_permit_expiry_date, pm_temp_permit_no, pm_is_third_notice, pm_old_application_no, pm_skipped_inspection, "
					+ "pm_is_first_notice, pm_is_second_notice, pm_old_permit_no, pm_tran_type, pm_inspection_status, proceed_status, inspection_type, inspection_location, "
					+ "proceed_given_by, proceed_given_date, proceed_remark) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

			ps2 = con.prepareStatement(query2);
			ps2.setLong(1, rs.getLong("seq"));
			ps2.setString(2, rs.getString("pm_application_no"));
			ps2.setString(3, rs.getString("pm_queue_no"));
			ps2.setString(4, rs.getString("pm_permit_no"));
			ps2.setString(5, rs.getString("pm_vehicle_regno"));
			ps2.setString(6, rs.getString("pm_permit_issue_date"));
			ps2.setString(7, rs.getString("pm_permit_issue_by"));
			ps2.setString(8, rs.getString("pm_inspect_date"));
			ps2.setString(9, rs.getString("pm_tender_ref_no"));
			ps2.setString(10, rs.getString("pm_status"));
			ps2.setBigDecimal(11, rs.getBigDecimal("pm_tender_annual_fee"));
			ps2.setBigDecimal(12, rs.getBigDecimal("pm_installments"));
			ps2.setString(13, rs.getString("pm_is_permit_print"));
			ps2.setString(14, rs.getString("pm_reject_reason"));
			ps2.setString(15, rs.getString("pm_valid_from"));
			ps2.setString(16, rs.getString("pm_valid_to"));
			ps2.setString(17, rs.getString("pm_approved_by"));
			ps2.setString(18, rs.getString("pm_appoved_date"));
			ps2.setString(19, rs.getString("pm_special_remark"));
			ps2.setString(20, rs.getString("pm_is_tender_permit"));
			ps2.setString(21, rs.getString("pm_service_type"));
			ps2.setString(22, rs.getString("pm_route_no"));
			ps2.setBigDecimal(23, rs.getBigDecimal("pm_tot_bus_fare"));
			ps2.setString(24, rs.getString("pm_is_backlog_app"));
			ps2.setString(25, rs.getString("pm_created_by"));
			ps2.setTimestamp(26, rs.getTimestamp("pm_created_date"));
			ps2.setString(27, rs.getString("pm_modified_by"));
			ps2.setTimestamp(28, rs.getTimestamp("pm_modified_date"));
			ps2.setString(29, rs.getString("pm_permit_expire_date"));
			ps2.setInt(30, rs.getInt("pm_renewal_period"));
			ps2.setString(31, rs.getString("pm_next_ins_date_sec1_2"));
			ps2.setString(32, rs.getString("pm_next_ins_date_sec3"));
			ps2.setString(33, rs.getString("pm_inspect_remark"));
			ps2.setString(34, rs.getString("pm_route_flag"));
			ps2.setString(35, rs.getString("pm_isnew_permit"));
			ps2.setString(36, rs.getString("pm_reinspec_status"));
			ps2.setString(37, rs.getString("pm_new_permit_expiry_date"));
			ps2.setString(38, rs.getString("pm_temp_permit_no"));
			ps2.setString(39, rs.getString("pm_is_third_notice"));
			ps2.setString(40, rs.getString("pm_old_application_no"));
			ps2.setString(41, rs.getString("pm_skipped_inspection"));
			ps2.setString(42, rs.getString("pm_is_first_notice"));
			ps2.setString(43, rs.getString("pm_is_second_notice"));
			ps2.setString(44, rs.getString("pm_old_permit_no"));
			ps2.setString(45, rs.getString("pm_tran_type"));
			ps2.setString(46, rs.getString("pm_inspection_status"));
			ps2.setString(47, rs.getString("proceed_status"));
			ps2.setString(48, rs.getString("inspection_type"));
			ps2.setString(49, rs.getString("inspection_location"));
			ps2.setString(50, rs.getString("proceed_given_by"));
			ps2.setTimestamp(51, rs.getTimestamp("proceed_given_date"));
			ps2.setString(52, rs.getString("proceed_remark"));

			success = ps2.executeUpdate();
		}
		
		ConnectionManager.close(ps);
		ConnectionManager.close(ps2);

		if (success > 0) {
			returnVal = true;
		}

		logger.info("insertToTempApplication end - success: " + returnVal);
		return returnVal;
	}

	private boolean updateStatusInApplication(String selectedApplicationNo, Connection con) throws Exception {
		logger.info("updateStatusInApplication start");
		boolean returnVal = false;

		PreparedStatement ps = null;
		String sql = "update nt_t_pm_application set pm_status ='H' where pm_application_no =?";

		ps = con.prepareStatement(sql);
		ps.setString(1, selectedApplicationNo);
		int success = ps.executeUpdate();
		ConnectionManager.close(ps);

		if (success > 0) {
			returnVal = true;
		}

		logger.info("updateStatusInApplication end - success: " + returnVal);
		return returnVal;
	}

	private boolean removeQueueRecordsInQueueMaster(String selectedApplicationNo, Connection con) throws Exception {
		logger.info("removeQueueRecordsInQueueMaster start");
		boolean returnVal = false;

		// insert in to temp queue master table
		PreparedStatement ps1 = null;
		String sql1 = "insert into public.nt_temp_queue_master "
				+ "(select * from public.nt_m_queue_master where que_application_no =?)";

		ps1 = con.prepareStatement(sql1);
		ps1.setString(1, selectedApplicationNo);
		int success1 = ps1.executeUpdate();
		ConnectionManager.close(ps1);

		// remove the record from queue master table
		PreparedStatement ps2 = null;
		String sql2 = "delete from public.nt_m_queue_master where que_application_no =?;";

		ps2 = con.prepareStatement(sql2);
		ps2.setString(1, selectedApplicationNo);
		int success2 = ps2.executeUpdate();
		ConnectionManager.close(ps2);

		if (success1 > 0 && success2 > 0) {
			returnVal = true;
		}

		logger.info("removeQueueRecordsInQueueMaster end - success: " + returnVal);
		return returnVal;
	}

	private boolean removeTaskRecordsInsertNewHistoryRecord(String selectedApplicationNo, String loginUser,
			Connection con) throws Exception {
		logger.info("removeTaskRecords start");
		boolean returnVal = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		// store vehicle number and seq for later use
		PreparedStatement ps = null;
		ResultSet rs = null;
		Long seq = null;
		String vehicleNo = null;

		String sql = "select tsd_seq,tsd_vehicle_no from public.nt_t_task_det where tsd_app_no =?;";
		ps = con.prepareStatement(sql);
		ps.setString(1, selectedApplicationNo);
		rs = ps.executeQuery();

		int success = 0;
		while (rs.next()) {
			seq = rs.getLong("tsd_seq");
			vehicleNo = rs.getString("tsd_vehicle_no");
			success = 1;
		}
		ConnectionManager.close(rs);
		ConnectionManager.close(ps);

		// insert in to temp task det table
		PreparedStatement ps1 = null;
		String sql1 = "insert into public.nt_temp_t_task_det "
				+ "(select * from public.nt_t_task_det where tsd_app_no =?)";

		ps1 = con.prepareStatement(sql1);
		ps1.setString(1, selectedApplicationNo);
		int success1 = ps1.executeUpdate();
		ConnectionManager.close(ps1);

		// remove the record from task det table
		PreparedStatement ps2 = null;
		String sql2 = "delete from public.nt_t_task_det where tsd_app_no =?;";

		ps2 = con.prepareStatement(sql2);
		ps2.setString(1, selectedApplicationNo);
		int success2 = ps2.executeUpdate();
		ConnectionManager.close(ps2);
		// insert in to temp task his table
		PreparedStatement ps3 = null;
		String sql3 = "insert into public.nt_temp_h_task_his "
				+ "(select * from public.nt_h_task_his where tsd_app_no =?)";

		ps3 = con.prepareStatement(sql3);
		ps3.setString(1, selectedApplicationNo);
		int success3 = ps3.executeUpdate();
		ConnectionManager.close(ps3);

		// remove the record from task his table
		PreparedStatement ps4 = null;
		String sql4 = "delete from public.nt_h_task_his where tsd_app_no =?;";

		ps4 = con.prepareStatement(sql4);
		ps4.setString(1, selectedApplicationNo);
		int success4 = ps4.executeUpdate();
		ConnectionManager.close(ps4);

		// insert in to history table
		PreparedStatement ps5 = null;
		String sql5 = "INSERT INTO public.nt_h_task_his (tsd_seq, tsd_vehicle_no, tsd_app_no, tsd_task_code, tsd_status, created_by, created_date) "
				+ "VALUES (?,?,?,?,?,?,?);";

		ps5 = con.prepareStatement(sql5);
		ps5.setLong(1, seq);
		ps5.setString(2, vehicleNo);
		ps5.setString(3, selectedApplicationNo);
		ps5.setString(4, "HO100");
		ps5.setString(5, "C");
		ps5.setString(6, loginUser);
		ps5.setTimestamp(7, timestamp);
		int success5 = ps5.executeUpdate();
		ConnectionManager.close(ps5);

		if (success > 0 && success1 > 0 && success2 > 0 && success3 > 0 && success4 > 0 && success5 > 0) {
			returnVal = true;
		}

		logger.info("removeTaskRecords end - success: " + returnVal);
		return returnVal;
	}

	@Override
	public boolean removeTemporaryHold(String selectedApplicationNo, String loginUser) {
		logger.info("removeTemporaryHold start");
		boolean returnVal = false;

		Connection con = null;
		try {
			con = ConnectionManager.getConnection();

			boolean success_1 = updatePreviousStatus(selectedApplicationNo, con);
			boolean success_2 = setQueueRecordsAsPrevious(selectedApplicationNo, con);
			boolean success_3 = setTaskRecordsAsPreviousInsertNewHistoryRecord(selectedApplicationNo, loginUser, con);

			con.commit();
			returnVal = true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("removeTemporaryHold error");
			logger.error(e);
		} finally {
			ConnectionManager.close(con);
		}
		logger.info("removeTemporaryHold end");
		return returnVal;
	}

	private boolean updatePreviousStatus(String selectedApplicationNo, Connection con) throws Exception {
		logger.info("updatePreviousStatus start");
		boolean returnVal = false;

		// store previous status for later use
		PreparedStatement ps = null;
		ResultSet rs = null;
		String prevStatus = null;

		String sql = "select pm_status as oldStatus from public.nt_temp_pm_application where pm_application_no =?";
		ps = con.prepareStatement(sql);
		ps.setString(1, selectedApplicationNo);
		rs = ps.executeQuery();

		int success = 0;
		while (rs.next()) {
			prevStatus = rs.getString("oldStatus");
			success = 1;
		}
		ConnectionManager.close(rs);
		ConnectionManager.close(ps);

		
		// insert into new history application table
		PreparedStatement ps4 = null;
		ResultSet rs4 = null;
		int success1 = 0;
		
		String query = "select * from public.nt_t_pm_application where pm_application_no =?";
		ps4 = con.prepareStatement(query);
		ps4.setString(1, selectedApplicationNo);
		rs4 = ps4.executeQuery();

		while (rs4.next()) {
			PreparedStatement ps1 = null;
			String sql1 = "INSERT INTO public.nt_h_pm_application_new "
					+ "(seq, pm_application_no, pm_queue_no, pm_permit_no, pm_vehicle_regno, pm_permit_issue_date, pm_permit_issue_by, pm_inspect_date, "
					+ "pm_tender_ref_no, pm_status, pm_tender_annual_fee, pm_installments, pm_is_permit_print, pm_reject_reason, pm_valid_from, pm_valid_to, "
					+ "pm_approved_by, pm_appoved_date, pm_special_remark, pm_is_tender_permit, pm_service_type, pm_route_no, pm_tot_bus_fare, pm_is_backlog_app, "
					+ "pm_created_by, pm_created_date, pm_modified_by, pm_modified_date, pm_permit_expire_date, pm_renewal_period, pm_next_ins_date_sec1_2, "
					+ "pm_next_ins_date_sec3, pm_inspect_remark, pm_route_flag, pm_isnew_permit, pm_reinspec_status, pm_old_permit_no, pm_temp_permit_no, "
					+ "pm_is_first_notice, pm_is_second_notice, pm_is_third_notice, pm_new_permit_expiry_date, pm_old_application_no, pm_skipped_inspection, "
					+ "pm_tran_type, pm_inspection_status, proceed_status, inspection_type, inspection_location, proceed_given_by, proceed_given_date, proceed_remark)"
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

			ps1 = con.prepareStatement(sql1);
			ps1.setLong(1, rs4.getLong("seq"));
			ps1.setString(2, rs4.getString("pm_application_no"));
			ps1.setString(3, rs4.getString("pm_queue_no"));
			ps1.setString(4, rs4.getString("pm_permit_no"));
			ps1.setString(5, rs4.getString("pm_vehicle_regno"));
			ps1.setString(6, rs4.getString("pm_permit_issue_date"));
			ps1.setString(7, rs4.getString("pm_permit_issue_by"));
			ps1.setString(8, rs4.getString("pm_inspect_date"));
			ps1.setString(9, rs4.getString("pm_tender_ref_no"));
			ps1.setString(10, rs4.getString("pm_status"));
			ps1.setBigDecimal(11, rs4.getBigDecimal("pm_tender_annual_fee"));
			ps1.setBigDecimal(12, rs4.getBigDecimal("pm_installments"));
			ps1.setString(13, rs4.getString("pm_is_permit_print"));
			ps1.setString(14, rs4.getString("pm_reject_reason"));
			ps1.setString(15, rs4.getString("pm_valid_from"));
			ps1.setString(16, rs4.getString("pm_valid_to"));
			ps1.setString(17, rs4.getString("pm_approved_by"));
			ps1.setString(18, rs4.getString("pm_appoved_date"));
			ps1.setString(19, rs4.getString("pm_special_remark"));
			ps1.setString(20, rs4.getString("pm_is_tender_permit"));
			ps1.setString(21, rs4.getString("pm_service_type"));
			ps1.setString(22, rs4.getString("pm_route_no"));
			ps1.setBigDecimal(23, rs4.getBigDecimal("pm_tot_bus_fare"));
			ps1.setString(24, rs4.getString("pm_is_backlog_app"));
			ps1.setString(25, rs4.getString("pm_created_by"));
			ps1.setTimestamp(26, rs4.getTimestamp("pm_created_date"));
			ps1.setString(27, rs4.getString("pm_modified_by"));
			ps1.setTimestamp(28, rs4.getTimestamp("pm_modified_date"));
			ps1.setString(29, rs4.getString("pm_permit_expire_date"));
			ps1.setInt(30, rs4.getInt("pm_renewal_period"));
			ps1.setString(31, rs4.getString("pm_next_ins_date_sec1_2"));
			ps1.setString(32, rs4.getString("pm_next_ins_date_sec3"));
			ps1.setString(33, rs4.getString("pm_inspect_remark"));
			ps1.setString(34, rs4.getString("pm_route_flag"));
			ps1.setString(35, rs4.getString("pm_isnew_permit"));
			ps1.setString(36, rs4.getString("pm_reinspec_status"));
			ps1.setString(37, rs4.getString("pm_old_permit_no"));
			ps1.setString(38, rs4.getString("pm_temp_permit_no"));
			ps1.setString(39, rs4.getString("pm_is_first_notice"));
			ps1.setString(40, rs4.getString("pm_is_second_notice"));
			ps1.setString(41, rs4.getString("pm_is_third_notice"));
			ps1.setString(42, rs4.getString("pm_new_permit_expiry_date"));
			ps1.setString(43, rs4.getString("pm_old_application_no"));
			ps1.setString(44, rs4.getString("pm_skipped_inspection"));
			ps1.setString(45, rs4.getString("pm_tran_type"));
			ps1.setString(46, rs4.getString("pm_inspection_status"));
			ps1.setString(47, rs4.getString("proceed_status"));
			ps1.setString(48, rs4.getString("inspection_type"));
			ps1.setString(49, rs4.getString("inspection_location"));
			ps1.setString(50, rs4.getString("proceed_given_by"));
			ps1.setTimestamp(51, rs4.getTimestamp("proceed_given_date"));
			ps1.setString(52, rs4.getString("proceed_remark"));
			success1 = ps1.executeUpdate();

			ConnectionManager.close(ps1);
		}
		
		ConnectionManager.close(rs4);
		ConnectionManager.close(ps4);
		
		// update application
		PreparedStatement ps2 = null;
		String sql2 = "update nt_t_pm_application set pm_status=? where pm_application_no=?";

		ps2 = con.prepareStatement(sql2);
		ps2.setString(1, prevStatus);
		ps2.setString(2, selectedApplicationNo);
		int success2 = ps2.executeUpdate();
		ConnectionManager.close(ps2);

		// delete from temp application table
		PreparedStatement ps3 = null;
		String sql3 = "delete from public.nt_temp_pm_application where pm_application_no =?;";

		ps3 = con.prepareStatement(sql3);
		ps3.setString(1, selectedApplicationNo);
		int success3 = ps3.executeUpdate();
		ConnectionManager.close(ps3);

		if (success > 0 && success1 > 0 && success2 > 0 && success3 > 0) {
			returnVal = true;
		}

		logger.info("updatePreviousStatus end - success: " + returnVal);
		return returnVal;
	}

	private boolean setQueueRecordsAsPrevious(String selectedApplicationNo, Connection con) throws Exception {
		logger.info("setQueueRecordsAsPrevious start");
		boolean returnVal = false;

		// insert in to queue master table
		PreparedStatement ps1 = null;
		String sql1 = "insert into public.nt_m_queue_master (select * from public.nt_temp_queue_master where que_application_no=?)";

		ps1 = con.prepareStatement(sql1);
		ps1.setString(1, selectedApplicationNo);
		int success1 = ps1.executeUpdate();
		ConnectionManager.close(ps1);

		// delete from temp queue master table
		PreparedStatement ps2 = null;
		String sql2 = "delete from public.nt_temp_queue_master where que_application_no=?;";

		ps2 = con.prepareStatement(sql2);
		ps2.setString(1, selectedApplicationNo);
		int success2 = ps2.executeUpdate();
		ConnectionManager.close(ps2);

		if (success1 > 0 && success2 > 0) {
			returnVal = true;
		}

		logger.info("setQueueRecordsAsPrevious end - success: " + returnVal);
		return returnVal;
	}

	private boolean setTaskRecordsAsPreviousInsertNewHistoryRecord(String selectedApplicationNo, String loginUser,
			Connection con) throws Exception {
		logger.info("setQueueRecordsAsPrevious start");
		boolean returnVal = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		// store vehicle number and seq for later use
		PreparedStatement ps = null;
		ResultSet rs = null;
		Long seq = null;
		String vehicleNo = null;

		String sql = "select tsd_seq,tsd_vehicle_no from public.nt_temp_t_task_det where tsd_app_no =?;";
		ps = con.prepareStatement(sql);
		ps.setString(1, selectedApplicationNo);
		rs = ps.executeQuery();

		int success = 0;
		while (rs.next()) {
			seq = rs.getLong("tsd_seq");
			vehicleNo = rs.getString("tsd_vehicle_no");
			success = 1;
		}
		ConnectionManager.close(rs);
		ConnectionManager.close(ps);

		// insert in to task detail table
		PreparedStatement ps1 = null;
		String sql1 = "insert into public.nt_t_task_det (select * from public.nt_temp_t_task_det where tsd_app_no=?)";

		ps1 = con.prepareStatement(sql1);
		ps1.setString(1, selectedApplicationNo);
		int success1 = ps1.executeUpdate();
		ConnectionManager.close(ps1);

		// delete from temp task detail table
		PreparedStatement ps2 = null;
		String sql2 = "delete from public.nt_temp_t_task_det where tsd_app_no=?;";

		ps2 = con.prepareStatement(sql2);
		ps2.setString(1, selectedApplicationNo);
		int success2 = ps2.executeUpdate();
		ConnectionManager.close(ps2);

		// insert in to task his table
		PreparedStatement ps3 = null;
		String sql3 = "insert into public.nt_h_task_his (select * from public.nt_temp_h_task_his where tsd_app_no=?)";

		ps3 = con.prepareStatement(sql3);
		ps3.setString(1, selectedApplicationNo);
		int success3 = ps3.executeUpdate();
		ConnectionManager.close(ps3);

		// delete from temp task his table
		PreparedStatement ps4 = null;
		String sql4 = "delete from public.nt_temp_h_task_his where tsd_app_no=?;";

		ps4 = con.prepareStatement(sql4);
		ps4.setString(1, selectedApplicationNo);
		int success4 = ps4.executeUpdate();
		ConnectionManager.close(ps4);
		// insert in to history table
		PreparedStatement ps5 = null;
		String sql5 = "INSERT INTO public.nt_h_task_his (tsd_seq, tsd_vehicle_no, tsd_app_no, tsd_task_code, tsd_status, created_by, created_date) "
				+ "VALUES (?,?,?,?,?,?,?);";

		ps5 = con.prepareStatement(sql5);
		ps5.setLong(1, seq);
		ps5.setString(2, vehicleNo);
		ps5.setString(3, selectedApplicationNo);
		ps5.setString(4, "UH100");
		ps5.setString(5, "C");
		ps5.setString(6, loginUser);
		ps5.setTimestamp(7, timestamp);
		int success5 = ps5.executeUpdate();
		ConnectionManager.close(ps5);

		if (success > 0 && success1 > 0 && success2 > 0 && success3 > 0 && success4 > 0 && success5 > 0) {
			returnVal = true;
		}

		logger.info("setQueueRecordsAsPrevious end - success: " + returnVal);
		return returnVal;
	}

}
