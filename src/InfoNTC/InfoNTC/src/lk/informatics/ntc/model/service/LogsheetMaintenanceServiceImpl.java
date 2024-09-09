package lk.informatics.ntc.model.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import lk.informatics.ntc.model.dto.LogSheetMaintenanceDTO;
import lk.informatics.ntc.model.dto.SisuSeriyaDTO;
import lk.informatics.ntc.view.util.ConnectionManager;

public class LogsheetMaintenanceServiceImpl implements LogsheetMaintenanceService {

	public LogSheetMaintenanceDTO search(LogSheetMaintenanceDTO logDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select sps_service_ref_no, sps_operator_name,sps_origin,sps_service_start_date,sps_service_end_date,sps_destination, "
					+ "sps_via,sps_status,sps_distance,sps_bus_no, sps_is_checked  " + "from public.nt_m_sisu_permit_hol_service_info "
					+ "where sps_status ='A' and sps_service_no ='" + logDTO.getServiceNo() + "'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				logDTO.setNameOfOperator(rs.getString("sps_operator_name"));
				logDTO.setEndOfStartDate(rs.getString("sps_service_start_date"));

				if (logDTO.getEndOfStartDate() != null) {
					logDTO.setEndOfStartDate(logDTO.getEndOfStartDate().substring(0, 10));
				}
				logDTO.setOrigin(rs.getString("sps_origin"));
				logDTO.setServiceEndDate(rs.getString("sps_service_end_date"));

				if (logDTO.getServiceEndDate() != null) {

					logDTO.setServiceEndDate(logDTO.getServiceEndDate().substring(0, 10));
				}
				logDTO.setStatus(rs.getString("sps_status"));

				if (logDTO.getStatus().equals("A")) {
					logDTO.setStatus("ACTIVE");
				}

				else if (logDTO.getStatus().equals("I")) {
					logDTO.setStatus("INACTIVE");
				}

				logDTO.setDestination(rs.getString("sps_destination"));
				logDTO.setVia(rs.getString("sps_via"));
				logDTO.setDistance(rs.getString("sps_distance"));
				logDTO.setBusNo(rs.getString("sps_bus_no"));
				logDTO.setReferenceNo(rs.getString("sps_service_ref_no"));
				logDTO.setIsChecked(rs.getString("sps_is_checked"));
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return logDTO;
	}

	public List serviceTypeDropDown() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<LogSheetMaintenanceDTO> returnList = new ArrayList<LogSheetMaintenanceDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct st_code,st_description " + "from nt_r_subsidy_service_type "
					+ "order by st_description";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				LogSheetMaintenanceDTO logDTO = new LogSheetMaintenanceDTO();
				logDTO.setServiceCode(rs.getString("st_code"));
				logDTO.setServiceDescription(rs.getString("st_description"));

				returnList.add(logDTO);

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
	public List serviceRefNoDropDown() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String value = "";
		List<String> returnList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT sps_service_ref_no " + " FROM public.nt_m_sisu_permit_hol_service_info "
					+ " where sps_service_ref_no is not null " + " order by sps_service_ref_no ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				value = rs.getString("sps_service_ref_no");

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
	public List serviceNoDropDown() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String value = "";
		List<String> returnList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct sps_service_no " + "from public.nt_m_sisu_permit_hol_service_info "
					+ "where sps_service_no is not null " + "order by sps_service_no ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				value = rs.getString("sps_service_no");

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

	public List sheetTable(LogSheetMaintenanceDTO logDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<LogSheetMaintenanceDTO> data = new ArrayList<LogSheetMaintenanceDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select lss_year,lss_month,lss_log_ref_no,lss_received_date "
					+ "from nt_m_log_sheet_summary a left outer join public.nt_m_log_sheets b "
					+ "on a.lss_log_sheet_master_seq =b.los_seq "
					+ "where a.lss_received_date is not null and b.los_subsidy_service_type_code='"
					+ logDTO.getServiceType() + "' and b.los_std_service_ref_no='" + logDTO.getReferenceNo()
					+ "' and a.lss_year='" + logDTO.getYear() + "' and lss_is_approverej is null ";

			if (logDTO.getMonth() == null || logDTO.getMonth().isEmpty() || logDTO.getMonth().equals("")) {
			} else {
				query = query + " and lss_month='" + logDTO.getMonth() + "' ";
			}

			query = query + " order by lss_log_ref_no";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				logDTO = new LogSheetMaintenanceDTO();
				logDTO.setYear(rs.getString("lss_year"));
				logDTO.setMonth(rs.getString("lss_month"));
				logDTO.setReceivedDate(rs.getString("lss_received_date"));
				logDTO.setReceivedDate(logDTO.getReceivedDate().substring(0, 10));
				logDTO.setLogRefNo(rs.getString("lss_log_ref_no"));
				data.add(logDTO);
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

	public List sheetTableCheckBy(LogSheetMaintenanceDTO logDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<LogSheetMaintenanceDTO> data = new ArrayList<LogSheetMaintenanceDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select lss_year,lss_month,lss_log_ref_no,lss_received_date "
					+ "from nt_m_log_sheet_summary a left outer join public.nt_m_log_sheets b "
					+ "on a.lss_log_sheet_master_seq =b.los_seq "
					+ "where a.lss_received_date is not null and b.los_subsidy_service_type_code='"
					+ logDTO.getServiceType() + "' and b.los_std_service_ref_no='" + logDTO.getReferenceNo()
					+ "' and a.lss_year='" + logDTO.getYear() + "' and lss_is_approverej is null ";

			if (logDTO.getMonth() == null || logDTO.getMonth().isEmpty() || logDTO.getMonth().equals("")) {
			} else {
				query = query + " and lss_month='" + logDTO.getMonth() + "' ";
			}

			query = query
					+ " and (lss_no_of_turns is not null or lss_no_of_turns_gps is not null or lss_log_sheet_dealy is not null or lss_average_pessengers is not null or "
					+ "lss_school_req_running_days is not null or lss_late_tips is not null or lss_is_school_approval is not null or lss_arrears is not null or "
					+ "lss_deduction is not null or lss_remarks is not null or lss_log_type is not null or lss_payment_type is not null or lss_running_pre is not null or "
					+ "lss_total_length is not null or lss_subsidy_amt is not null or lss_late_fee is not null or lss_penalty_fee is not null or lss_payment_amt is not null) ";

			query = query + " order by lss_log_ref_no";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				logDTO = new LogSheetMaintenanceDTO();
				logDTO.setYear(rs.getString("lss_year"));
				logDTO.setMonth(rs.getString("lss_month"));
				logDTO.setReceivedDate(rs.getString("lss_received_date"));
				logDTO.setReceivedDate(logDTO.getReceivedDate().substring(0, 10));
				logDTO.setLogRefNo(rs.getString("lss_log_ref_no"));
				data.add(logDTO);
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
	public void insertToLogSheet(LogSheetMaintenanceDTO logDTO) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "UPDATE nt_m_log_sheet_summary "
					+ "set lss_no_of_turns=?,lss_no_of_turns_gps=?,lss_log_sheet_dealy=?,lss_average_pessengers=?,lss_school_req_running_days=?, "
					+ "lss_late_tips=?,lss_is_school_approval=?,lss_arrears=?,lss_deduction=?,lss_remarks=?,lss_log_type=?,lss_payment_type=?,lss_running_pre=?, "
					+ "lss_total_length=?,lss_subsidy_amt=?,lss_late_fee=?,lss_penalty_fee=?,lss_payment_amt=? "
					+ "WHERE lss_log_ref_no=?";

			ps = con.prepareStatement(query);
			ps.setInt(1, logDTO.getNoOfTurns());
			ps.setInt(2, logDTO.getNoOfTurnsinGPS());
			ps.setInt(3, logDTO.getLogSheetDelay());
			ps.setInt(4, logDTO.getAveragePasengers());
			ps.setInt(5, logDTO.getSchoolRequiredRunningDate());
			ps.setInt(6, logDTO.getLateTrips());
			ps.setString(7, logDTO.getSchoolApprovals());
			ps.setInt(8, logDTO.getArrears());
			ps.setInt(9, logDTO.getDeductions());
			ps.setString(10, logDTO.getSpecialRemark());
			ps.setString(11, logDTO.getLogType());
			ps.setString(12, logDTO.getPaymentType());
			ps.setInt(13, logDTO.getRunning());
			ps.setInt(14, logDTO.getTotalLength());
			ps.setInt(15, logDTO.getSubsidy());
			ps.setInt(16, logDTO.getLateFee());
			ps.setInt(17, logDTO.getPenaltyFee());
			ps.setInt(18, logDTO.getPayment());
			ps.setString(19, logDTO.getLogRefNo());
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
	public boolean deleteFromLogSheet(LogSheetMaintenanceDTO logDTO) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean success = false;
		try {
			con = ConnectionManager.getConnection();

			String query = "UPDATE nt_m_log_sheet_summary "
					+ "set lss_no_of_turns=null,lss_no_of_turns_gps=null,lss_log_sheet_dealy=null,lss_average_pessengers=null,lss_school_req_running_days=null, "
					+ "lss_late_tips=null,lss_is_school_approval=null,lss_arrears=null,lss_deduction=null,lss_remarks=null,lss_log_type=null,lss_payment_type=null,lss_running_pre=null, "
					+ "lss_total_length=null,lss_subsidy_amt=null,lss_late_fee=null,lss_penalty_fee=null,lss_payment_amt=null, lss_balance_amt=null, lss_tips_perday=null, lss_requested_pre=null,lss_recalculated_by =null,lss_recalculated_date =null "
					+ "WHERE lss_log_ref_no=?";

			try {
				ps = con.prepareStatement(query);

				ps.setString(1, logDTO.getLogRefNo());
				ps.executeUpdate();
				con.commit();
				success = true;
			}catch (SQLException e) {
				con.rollback();
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
		return success;
	}

	@Override
	public LogSheetMaintenanceDTO datafromLogSheet(LogSheetMaintenanceDTO logDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select lss_no_of_turns,lss_no_of_turns_gps,lss_log_sheet_dealy,lss_average_pessengers,lss_school_req_running_days,"
					+ "lss_late_tips,lss_is_school_approval,lss_arrears,lss_deduction,lss_remarks,lss_log_type,lss_payment_type,lss_running_pre, "
					+ "lss_total_length,lss_subsidy_amt,lss_late_fee,lss_penalty_fee,lss_payment_amt, lss_other_penalty_fee, lss_other_penalty_percentage, lss_tips_perday, lss_requested_pre "
					+ "from nt_m_log_sheet_summary " + "where lss_log_ref_no='" + logDTO.getLogRefNo() + "'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				logDTO.setNoOfTurns(rs.getInt("lss_no_of_turns"));
				logDTO.setNoOfTurnsinGPS(rs.getInt("lss_no_of_turns_gps"));
				logDTO.setLogSheetDelay(rs.getInt("lss_log_sheet_dealy"));
				logDTO.setAveragePasengers(rs.getInt("lss_average_pessengers"));
				logDTO.setSchoolRequiredRunningDate(rs.getInt("lss_school_req_running_days"));
				logDTO.setLateTrips(rs.getInt("lss_late_tips"));
				logDTO.setSchoolApprovals(rs.getString("lss_is_school_approval"));

				if (logDTO.getSchoolApprovals() == null || logDTO.getSchoolApprovals().isEmpty()
						|| logDTO.getSchoolApprovals().equals("")) {
					logDTO.setSchoolApprovals("");
				}

				if (logDTO.getSchoolApprovals().equals("Y")) {
					logDTO.setApprovalBoolean(true);
				} else {
					logDTO.setApprovalBoolean(false);
				}

				logDTO.setArrears(rs.getInt("lss_arrears"));
				logDTO.setDeductions(rs.getInt("lss_deduction"));
				logDTO.setSpecialRemark(rs.getString("lss_remarks"));
				logDTO.setLogType(rs.getString("lss_log_type"));
				logDTO.setPaymentType(rs.getString("lss_payment_type"));
				logDTO.setRunningD(rs.getBigDecimal("lss_running_pre")); // **
				logDTO.setTotalLengthD(rs.getBigDecimal("lss_total_length")); // **
				logDTO.setSubsidyD(rs.getBigDecimal("lss_subsidy_amt")); // **
				logDTO.setPenaltyFeeD(rs.getBigDecimal("lss_penalty_fee")); // **
				logDTO.setPaymentD(rs.getBigDecimal("lss_payment_amt")); // **
				logDTO.setLateFeeD(rs.getBigDecimal("lss_late_fee")); // **
				logDTO.setOtherPercentageCalculationValD(rs.getBigDecimal("lss_other_penalty_fee")); // **
				logDTO.setOtherPercentage(rs.getDouble("lss_other_penalty_percentage"));
				logDTO.setTripsPerDay(rs.getInt("lss_tips_perday"));
				logDTO.setRequested(rs.getInt("lss_requested_pre"));
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return logDTO;
	}

	@Override
	public boolean checkData(LogSheetMaintenanceDTO logDTO) {
		boolean value = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select lss_no_of_turns,lss_no_of_turns_gps,lss_log_sheet_dealy,lss_average_pessengers,lss_school_req_running_days, "
					+ "lss_late_tips,lss_is_school_approval,lss_arrears,lss_deduction,lss_remarks,lss_log_type,lss_payment_type,lss_running_pre "
					+ "lss_total_length,lss_subsidy_amt,lss_late_fee,lss_penalty_fee,lss_payment_amt "
					+ "from nt_m_log_sheet_summary " + "where lss_log_ref_no='" + logDTO.getLogRefNo()
					+ "' and (lss_no_of_turns is not null or lss_no_of_turns_gps is not null or lss_log_sheet_dealy is not null or lss_average_pessengers is not null or "
					+ "lss_school_req_running_days is not null or lss_late_tips is not null or lss_is_school_approval is not null or lss_arrears is not null or "
					+ "lss_deduction is not null or lss_remarks is not null or lss_log_type is not null or lss_payment_type is not null or lss_running_pre is not null or "
					+ "lss_total_length is not null or lss_subsidy_amt is not null or lss_late_fee is not null or lss_penalty_fee is not null or lss_payment_amt is not null) ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			if (!rs.isBeforeFirst()) {

				value = false;
			} else {

				value = true;
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
	public LogSheetMaintenanceDTO getRefNo(LogSheetMaintenanceDTO logDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT sps_service_ref_no " + "FROM nt_m_sisu_permit_hol_service_info "
					+ "where sps_status='A' and sps_service_no ='" + logDTO.getServiceNo() + "'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				logDTO.setReferenceNo(rs.getString("sps_service_ref_no"));
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return logDTO;

	}

	@Override
	public String getApprovedSchool(LogSheetMaintenanceDTO logDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String value = "";

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT ssc_school_name " + "FROM nt_m_sisu_permit_hol_school_info "
					+ "where ssc_service_ref_no ='" + logDTO.getReferenceNo() + "'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				value = rs.getString("ssc_school_name");
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

	public boolean approvalstatusupdate(String status, String user, String date, String logRefNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean success = false;
		
		try {
			con = ConnectionManager.getConnection();

			String query = "update nt_m_log_sheet_summary "
					+ "set lss_is_approverej=?,lss_approverej_by=?,lss_approverej_date=? " + "where lss_log_ref_no=?";
			
			try {
				ps = con.prepareStatement(query);
				ps.setString(1, status);
				ps.setString(2, user);
				ps.setString(3, date);
				ps.setString(4, logRefNo);
				ps.executeUpdate();
				con.commit();
				success = true;
			} catch (SQLException e) {
				con.rollback();
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
		return success;
	}

	@Override
	public boolean editFromgLogSheet(LogSheetMaintenanceDTO logDTO) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		boolean success = false;

		try {
			con = ConnectionManager.getConnection();

			String query = "UPDATE nt_m_log_sheet_summary "
					+ "set lss_no_of_turns=?,lss_no_of_turns_gps=?,lss_log_sheet_dealy=?,lss_average_pessengers=?,lss_school_req_running_days=?, "
					+ "lss_late_tips=?,lss_is_school_approval=?,lss_arrears=?,lss_deduction=?,lss_remarks=?,lss_log_type=?,lss_payment_type=?,lss_running_pre=?, "
					+ "lss_total_length=?,lss_subsidy_amt=?,lss_late_fee=?,lss_penalty_fee=?,lss_payment_amt=?,lss_balance_amt=?,lss_service_no=?,lss_pending_amt=0,lss_paid_amt=0, lss_other_penalty_fee=?, lss_other_penalty_percentage=?,lss_recalculated_by =?,lss_recalculated_date=? "
					+ "WHERE lss_log_ref_no=?";

			try {
				ps = con.prepareStatement(query);
				ps.setInt(1, logDTO.getNoOfTurns());
				ps.setInt(2, logDTO.getNoOfTurnsinGPS());
				ps.setInt(3, logDTO.getLogSheetDelay());
				ps.setInt(4, logDTO.getAveragePasengers());
				ps.setInt(5, logDTO.getSchoolRequiredRunningDate());
				ps.setInt(6, logDTO.getLateTrips());

				if (logDTO.isApprovalBoolean() == true) {
					logDTO.setSchoolApprovals("Y");
					ps.setString(7, logDTO.getSchoolApprovals());
				} else {
					logDTO.setSchoolApprovals("N");
					ps.setString(7, logDTO.getSchoolApprovals());
				}

				ps.setInt(8, logDTO.getArrears());
				ps.setInt(9, logDTO.getDeductions());
				ps.setString(10, logDTO.getSpecialRemark());
				ps.setString(11, logDTO.getLogType());
				ps.setString(12, logDTO.getPaymentType());
				ps.setBigDecimal(13, logDTO.getRunningD());// **
				ps.setBigDecimal(14, logDTO.getTotalLengthD());// **
				ps.setBigDecimal(15, logDTO.getSubsidyD());// **
				ps.setBigDecimal(16, logDTO.getLateFeeD());// **
				ps.setBigDecimal(17, logDTO.getPenaltyFeeD());// **
				ps.setBigDecimal(18, logDTO.getPaymentD()); // **
				ps.setBigDecimal(19, logDTO.getPaymentD());// **
				ps.setString(20, logDTO.getServiceNo());
				ps.setBigDecimal(21, logDTO.getOtherPercentageCalculationValD());// **
				ps.setDouble(22, logDTO.getOtherPercentage());
				ps.setString(23, logDTO.getReCalculateBy());
				if (logDTO.getReCalculateBy() != null) {
					ps.setTimestamp(24, timestamp);
				} else {
					ps.setTimestamp(24, null);
				}
				ps.setString(25, logDTO.getLogRefNo());
				ps.executeUpdate();
				con.commit();
				success = true;
			} catch (SQLException e) {
				con.rollback();
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
		return success;
	}

	@Override
	public String totalLength(String serviceNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String value = "";

		try {
			con = ConnectionManager.getConnection();

			String query = "select sps_distance " + "from nt_m_sisu_permit_hol_service_info "
					+ "where sps_status='A' and sps_service_no='" + serviceNo + "'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				value = rs.getString("sps_distance");
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
	public String paymentRate() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String value = "";

		try {
			con = ConnectionManager.getConnection();

			String query = "select number_value " + "from nt_r_parameters "
					+ "where param_name='PAYMENT_RATE_LOG_SHEET' ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				value = rs.getString("number_value");

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
	public List<SisuSeriyaDTO> getServiceTypeForDropdown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SisuSeriyaDTO> serviceTypeList = new ArrayList<SisuSeriyaDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct st_code,st_description " + "from nt_r_subsidy_service_type "
					+ "order by st_description";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				SisuSeriyaDTO sisuSeriyaDto = new SisuSeriyaDTO();
				sisuSeriyaDto.setServiceTypeCode(rs.getString("st_code"));
				sisuSeriyaDto.setServiceTypeDes(rs.getString("st_description"));

				serviceTypeList.add(sisuSeriyaDto);

			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return serviceTypeList;
	}

	public List<SisuSeriyaDTO> getServiceRefNoForDropdown(SisuSeriyaDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SisuSeriyaDTO> serviceRefNoList = new ArrayList<SisuSeriyaDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct los_std_service_ref_no  FROM public.nt_m_log_sheets "
					+ "inner join nt_r_subsidy_service_type on nt_r_subsidy_service_type.st_code = nt_m_log_sheets.los_subsidy_service_type_code  "
					+ "inner join nt_m_log_sheet_summary on nt_m_log_sheet_summary.lss_log_sheet_master_seq = nt_m_log_sheets.los_seq "
					+ "where los_std_service_ref_no is not null and los_subsidy_service_type_code=?  order by los_std_service_ref_no desc";

			ps = con.prepareStatement(query);

			ps.setString(1, dto.getServiceTypeCode());
			rs = ps.executeQuery();

			while (rs.next()) {
				SisuSeriyaDTO sisuSeriyaDto = new SisuSeriyaDTO();
				sisuSeriyaDto.setServiceRefNo(rs.getString("los_std_service_ref_no"));

				serviceRefNoList.add(sisuSeriyaDto);

			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return serviceRefNoList;
	}

	public List<SisuSeriyaDTO> getServiceAgtNoForDropdown(SisuSeriyaDTO dto) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SisuSeriyaDTO> serviceRefNoList = new ArrayList<SisuSeriyaDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct nt_m_sisu_permit_hol_service_info.sps_service_no  FROM public.nt_m_log_sheets "
					+ "inner join nt_r_subsidy_service_type on nt_r_subsidy_service_type.st_code = nt_m_log_sheets.los_subsidy_service_type_code "
					+ "inner join nt_m_log_sheet_summary on nt_m_log_sheet_summary.lss_log_sheet_master_seq = nt_m_log_sheets.los_seq "
					+ "inner join nt_m_sisu_permit_hol_service_info on nt_m_sisu_permit_hol_service_info.sps_service_ref_no=nt_m_log_sheets.los_std_service_ref_no "
					+ "where sps_service_no is not null and los_subsidy_service_type_code=?  order by sps_service_no ";

			ps = con.prepareStatement(query);

			ps.setString(1, dto.getServiceTypeCode());
			rs = ps.executeQuery();

			while (rs.next()) {
				SisuSeriyaDTO sisuSeriyaDto = new SisuSeriyaDTO();
				sisuSeriyaDto.setServiceNo(rs.getString("sps_service_no"));
				serviceRefNoList.add(sisuSeriyaDto);

			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return serviceRefNoList;
	}

	@Override
	public List<SisuSeriyaDTO> showSearchedData(SisuSeriyaDTO sisuSeriyaDto, String year) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> recieveDataOnGrid = new ArrayList<SisuSeriyaDTO>();

		String WHERE_SQL = "";
		boolean whereadded = false;

		if (sisuSeriyaDto.getServiceRefNo() != null && !sisuSeriyaDto.getServiceRefNo().equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE los_std_service_ref_no = " + "'" + sisuSeriyaDto.getServiceRefNo()
					+ "' and los_subsidy_service_type_code = " + "'" + sisuSeriyaDto.getServiceTypeCode() + "' ";
			whereadded = true;
		}
		if (sisuSeriyaDto.getServiceNo() != null && !sisuSeriyaDto.getServiceNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND sps_service_no = " + "'" + sisuSeriyaDto.getServiceNo() + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE sps_service_no = " + "'" + sisuSeriyaDto.getServiceNo()
						+ "' and los_subsidy_service_type_code = " + "'" + sisuSeriyaDto.getServiceTypeCode() + "' ";
				whereadded = true;
			}
		}

		if (year != null && !year.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND nt_m_log_sheet_summary.lss_year = " + "'" + year + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE nt_m_log_sheet_summary.lss_year = " + "'" + year
						+ "' and los_subsidy_service_type_code = " + "'" + sisuSeriyaDto.getServiceTypeCode() + "' ";
				whereadded = true;
			}
		}

		if (sisuSeriyaDto.getStringMonth() != null && !sisuSeriyaDto.getStringMonth().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND lss_month = " + "'" + sisuSeriyaDto.getStringMonth() + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE lss_month = " + "'" + sisuSeriyaDto.getStringMonth()
						+ "' and los_subsidy_service_type_code = " + "'" + sisuSeriyaDto.getServiceTypeCode() + "' ";
				whereadded = true;
			}
		}
		
		if (sisuSeriyaDto.getBusRegNo() != null && !sisuSeriyaDto.getBusRegNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND nt_m_sisu_permit_hol_service_info.sps_bus_no  = " + "'" + sisuSeriyaDto.getBusRegNo() + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE nt_m_sisu_permit_hol_service_info.sps_bus_no  = " + "'" + sisuSeriyaDto.getBusRegNo()
						+ "' and los_subsidy_service_type_code = " + "'" + sisuSeriyaDto.getServiceTypeCode() + "' ";
				whereadded = true;
			}
		}
		
		if (sisuSeriyaDto.getNameOfOperator()!= null && !sisuSeriyaDto.getNameOfOperator().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND nt_m_sisu_permit_hol_service_info.sps_operator_name  = " + "'" + sisuSeriyaDto.getNameOfOperator() + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE nt_m_sisu_permit_hol_service_info.sps_operator_name  = " + "'" + sisuSeriyaDto.getNameOfOperator()
						+ "' and los_subsidy_service_type_code = " + "'" + sisuSeriyaDto.getServiceTypeCode() + "' ";
				whereadded = true;
			}
		}

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct los_std_service_ref_no, nt_m_log_sheet_summary.lss_year, los_no_of_copies, nt_m_log_sheet_summary.lss_received_date, "
					+ "nt_m_log_sheet_summary.lss_month,nt_m_log_sheet_summary.lss_log_ref_no,nt_r_subsidy_service_type.st_description   from public.nt_m_log_sheets "
					+ "inner join nt_m_log_sheet_summary on nt_m_log_sheet_summary.lss_log_sheet_master_seq = nt_m_log_sheets.los_seq "
					+ "inner join nt_r_subsidy_service_type on nt_r_subsidy_service_type.st_code = nt_m_log_sheets.los_subsidy_service_type_code "
					+ "inner join nt_m_sisu_permit_hol_service_info on nt_m_sisu_permit_hol_service_info.sps_service_ref_no=nt_m_log_sheets.los_std_service_ref_no "
					+ WHERE_SQL;

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				SisuSeriyaDTO sisuSeriyaDto1 = new SisuSeriyaDTO();

				sisuSeriyaDto1.setServiceRefNo(rs.getString("los_std_service_ref_no"));
				sisuSeriyaDto1.setYear(rs.getInt("lss_year"));
				sisuSeriyaDto1.setLogRefCheck(false);
				sisuSeriyaDto.setTransactionDescription(rs.getString("st_description"));
				sisuSeriyaDto1.setStringMonth((rs.getString("lss_month")));
				sisuSeriyaDto1.setLogRefNo(rs.getString("lss_log_ref_no"));
				sisuSeriyaDto1.setReceivedDate(rs.getDate("lss_received_date"));

				recieveDataOnGrid.add(sisuSeriyaDto1);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return recieveDataOnGrid;

	}

	@Override
	public boolean updateLogSheetReceivedData(SisuSeriyaDTO sisuSeriyaDto, String user,
			List<SisuSeriyaDTO> sisuSariyaList) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean success = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		try {
			con = ConnectionManager.getConnection();

			for (SisuSeriyaDTO list : sisuSariyaList) {

				String sql = "UPDATE public.nt_m_log_sheet_summary SET lss_month= ?, lss_received_date =?, lss_modified_by= ?,lss_modified_date=? "
						+ "WHERE lss_log_ref_no=?;";

				try {
					stmt = con.prepareStatement(sql);

					stmt.setString(1, list.getStringMonth());

					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					String todayAsString = df.format(list.getReceivedDate());

					stmt.setString(2, todayAsString);
					stmt.setString(3, user);
					stmt.setTimestamp(4, timestamp);
					stmt.setString(5, list.getLogRefNo());

					int data = stmt.executeUpdate();

					if (data > 0) {
						success = true;
					} else {
						success = false;
					}

					con.commit();

				} catch (SQLException e) {
					con.rollback();
					success = false;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			success = false;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return success;
	}
	
	/* Added by dhananjika.d 29/02/2024 */

	@Override
	public String checkDataHaveForMonth(SisuSeriyaDTO list, String service) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String month = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "select lss_month,count(lss_month) countMonth " + "from public.nt_m_log_sheet_summary "
					+ "inner join public.nt_m_log_sheets on los_seq = lss_log_sheet_master_seq "
					+ "where los_std_service_ref_no = ? and  lss_month = ? and lss_year = ? "
					+ "and los_subsidy_service_type_code = ? group by lss_month";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, list.getServiceRefNo());
			stmt.setString(2, list.getStringMonth());
			stmt.setString(3, String.valueOf(list.getYear()));
			stmt.setString(4, service);
			rs = stmt.executeQuery();

			while (rs.next()) {
				if (rs.getInt("countMonth") >= 1) {
					month = list.getStringMonth();
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return month;
	}

	public SisuSeriyaDTO fillSerNo(String serviceNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		SisuSeriyaDTO sisuDTO = new SisuSeriyaDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "select sps_service_no from public.nt_m_sisu_permit_hol_service_info where sps_service_ref_no =?;";

			ps = con.prepareStatement(query);
			ps.setString(1, serviceNo);
			rs = ps.executeQuery();

			while (rs.next()) {

				sisuDTO.setServiceNo(rs.getString("sps_service_no"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return sisuDTO;

	}

	@Override
	public boolean initialSearch(LogSheetMaintenanceDTO logDTO) {
		boolean value = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct sps_operator_name,sps_origin,sps_service_start_date,sps_service_end_date,sps_destination, "
					+ "sps_via,sps_status,sps_distance " + "from public.nt_m_sisu_permit_hol_service_info "
					+ "where  sps_status !='I'and  sps_service_no ='" + logDTO.getServiceNo() + "'" ;

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			if (!rs.isBeforeFirst()) {

				value = false;
			} else {

				value = true;
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
	public List<SisuSeriyaDTO> showDefaultSearchedData() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> recieveDataOnGrid = new ArrayList<SisuSeriyaDTO>();

		try {
			con = ConnectionManager.getConnection();

			// For All Types ---- Gami Seriya, Sisu Seriya, Nisi Seriya ---- Added by Sasini
			String query = "select distinct los_std_service_ref_no, los_year, los_no_of_copies, nt_m_log_sheet_summary.lss_received_date, nt_m_log_sheet_summary.lss_month,nt_m_log_sheet_summary.lss_log_ref_no,nt_r_subsidy_service_type.st_description  from public.nt_m_log_sheets inner join nt_m_log_sheet_summary on nt_m_log_sheet_summary.lss_log_sheet_master_seq = nt_m_log_sheets.los_seq inner join nt_r_subsidy_service_type on nt_r_subsidy_service_type.st_code = nt_m_log_sheets.los_subsidy_service_type_code WHERE lss_received_date is null or lss_received_date =''  order by los_std_service_ref_no; ";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				SisuSeriyaDTO sisuSeriyaDto1 = new SisuSeriyaDTO();

				sisuSeriyaDto1.setServiceRefNo(rs.getString("los_std_service_ref_no"));
				sisuSeriyaDto1.setYear(rs.getInt("los_year"));
				sisuSeriyaDto1.setLogRefCheck(false);
				sisuSeriyaDto1.setTransactionDescription(rs.getString("st_description"));
				sisuSeriyaDto1.setStringMonth((rs.getString("lss_month")));
				sisuSeriyaDto1.setLogRefNo(rs.getString("lss_log_ref_no"));
				sisuSeriyaDto1.setReceivedDate(rs.getDate("lss_received_date"));

				recieveDataOnGrid.add(sisuSeriyaDto1);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return recieveDataOnGrid;
	}

	@Override
	public List<SisuSeriyaDTO> getServiceAgtNoForGamiSeriyaDropdown(SisuSeriyaDTO sisuSeriyaDto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SisuSeriyaDTO> serviceRefNoList = new ArrayList<SisuSeriyaDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct nt_m_gami_permit_hol_service_info.gps_service_no FROM public.nt_m_log_sheets inner join nt_r_subsidy_service_type on nt_r_subsidy_service_type.st_code = nt_m_log_sheets.los_subsidy_service_type_code inner join nt_m_log_sheet_summary on nt_m_log_sheet_summary.lss_log_sheet_master_seq = nt_m_log_sheets.los_seq inner join nt_m_gami_permit_hol_service_info  on nt_m_gami_permit_hol_service_info.gps_service_ref_no=nt_m_log_sheets.los_std_service_ref_no where gps_service_no is not null and los_subsidy_service_type_code=?  order by gps_service_no ;";

			ps = con.prepareStatement(query);

			ps.setString(1, sisuSeriyaDto.getServiceTypeCode());
			rs = ps.executeQuery();

			while (rs.next()) {
				SisuSeriyaDTO ssDto = new SisuSeriyaDTO();
				ssDto.setServiceNo(rs.getString("gps_service_no"));
				serviceRefNoList.add(ssDto);

			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return serviceRefNoList;
	}

	@Override
	public List<SisuSeriyaDTO> showSearchedDataForGamiSeriya(SisuSeriyaDTO sisuSeriyaDto, String logSheetYear) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> recieveDataOnGrid = new ArrayList<SisuSeriyaDTO>();

		String WHERE_SQL = "";
		boolean whereadded = false;

		if (sisuSeriyaDto.getServiceRefNo() != null && !sisuSeriyaDto.getServiceRefNo().equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE los_std_service_ref_no = " + "'" + sisuSeriyaDto.getServiceRefNo()
					+ "' and los_subsidy_service_type_code = " + "'" + sisuSeriyaDto.getServiceTypeCode() + "' ";
			whereadded = true;
		}
		if (sisuSeriyaDto.getServiceNo() != null && !sisuSeriyaDto.getServiceNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND gps_service_no = " + "'" + sisuSeriyaDto.getServiceNo() + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE gps_service_no = " + "'" + sisuSeriyaDto.getServiceNo()
						+ "' and los_subsidy_service_type_code = " + "'" + sisuSeriyaDto.getServiceTypeCode() + "' ";
				whereadded = true;
			}
		}

		if (logSheetYear != null && !logSheetYear.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND nt_m_log_sheet_summary.lss_year = " + "'" + logSheetYear + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE nt_m_log_sheet_summary.lss_year = " + "'" + logSheetYear
						+ "' and los_subsidy_service_type_code = " + "'" + sisuSeriyaDto.getServiceTypeCode() + "' ";
				whereadded = true;
			}
		}

		if (sisuSeriyaDto.getStringMonth() != null && !sisuSeriyaDto.getStringMonth().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND lss_month = " + "'" + sisuSeriyaDto.getStringMonth() + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE lss_month = " + "'" + sisuSeriyaDto.getStringMonth()
						+ "' and los_subsidy_service_type_code = " + "'" + sisuSeriyaDto.getServiceTypeCode() + "' ";
				whereadded = true;
			}
		}
		
		if (sisuSeriyaDto.getBusRegNo() != null && !sisuSeriyaDto.getBusRegNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND nt_m_gami_permit_hol_service_info.gps_bus_no  = " + "'" + sisuSeriyaDto.getBusRegNo() + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE nt_m_gami_permit_hol_service_info.gps_bus_no = " + "'" + sisuSeriyaDto.getBusRegNo()
						+ "' and los_subsidy_service_type_code = " + "'" + sisuSeriyaDto.getServiceTypeCode() + "' ";
				whereadded = true;
			}
		}
		
		if (sisuSeriyaDto.getNameOfOperator()!= null && !sisuSeriyaDto.getNameOfOperator().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND nt_m_gami_permit_hol_service_info.gps_operator_name  = " + "'" + sisuSeriyaDto.getNameOfOperator() + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE nt_m_gami_permit_hol_service_info.gps_operator_name  = " + "'" + sisuSeriyaDto.getNameOfOperator()
						+ "' and los_subsidy_service_type_code = " + "'" + sisuSeriyaDto.getServiceTypeCode() + "' ";
				whereadded = true;
			}
		}
		
		

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct los_std_service_ref_no, nt_m_log_sheet_summary.lss_year, los_no_of_copies, nt_m_log_sheet_summary.lss_received_date, "
					+ "nt_m_log_sheet_summary.lss_month,nt_m_log_sheet_summary.lss_log_ref_no,nt_r_subsidy_service_type.st_description   from public.nt_m_log_sheets "
					+ "inner join nt_m_log_sheet_summary on nt_m_log_sheet_summary.lss_log_sheet_master_seq = nt_m_log_sheets.los_seq "
					+ "inner join nt_r_subsidy_service_type on nt_r_subsidy_service_type.st_code = nt_m_log_sheets.los_subsidy_service_type_code "
					+ "inner join nt_m_gami_permit_hol_service_info on nt_m_gami_permit_hol_service_info.gps_service_ref_no=nt_m_log_sheets.los_std_service_ref_no "
					+ WHERE_SQL;

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				SisuSeriyaDTO sisuSeriyaDto1 = new SisuSeriyaDTO();

				sisuSeriyaDto1.setServiceRefNo(rs.getString("los_std_service_ref_no"));
				sisuSeriyaDto1.setYear(rs.getInt("lss_year"));
				sisuSeriyaDto1.setLogRefCheck(false);
				sisuSeriyaDto1.setTransactionDescription(rs.getString("st_description"));
				sisuSeriyaDto1.setStringMonth((rs.getString("lss_month")));
				sisuSeriyaDto1.setLogRefNo(rs.getString("lss_log_ref_no"));
				sisuSeriyaDto1.setReceivedDate(rs.getDate("lss_received_date"));

				recieveDataOnGrid.add(sisuSeriyaDto1);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return recieveDataOnGrid;
	}

	@Override
	public List<String> serviceNoSisuSeriyaDropDown(String serviceType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String value = "";
		List<String> returnList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct nt_m_sisu_permit_hol_service_info.sps_service_no  FROM public.nt_m_log_sheets inner join nt_r_subsidy_service_type on nt_r_subsidy_service_type.st_code = nt_m_log_sheets.los_subsidy_service_type_code inner join nt_m_log_sheet_summary on nt_m_log_sheet_summary.lss_log_sheet_master_seq = nt_m_log_sheets.los_seq inner join nt_m_sisu_permit_hol_service_info on nt_m_sisu_permit_hol_service_info.sps_service_ref_no=nt_m_log_sheets.los_std_service_ref_no where sps_service_no is not null and los_subsidy_service_type_code=? and sps_status='A' order by sps_service_no desc ;";

			ps = con.prepareStatement(query);
			ps.setString(1, serviceType);
			rs = ps.executeQuery();

			while (rs.next()) {
				value = rs.getString("sps_service_no");

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
	public List<String> serviceNoSisuSeriyaDropDown(LogSheetMaintenanceDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String value = "";
		List<String> returnList = new ArrayList<String>();

		String WHERE_SQL = "";

		if (dto.getNameOfOperator() != null && !dto.getNameOfOperator().equals("")) {
			WHERE_SQL = WHERE_SQL + " AND nt_m_sisu_permit_hol_service_info.sps_operator_name = '"
					+ dto.getNameOfOperator() + "' ";
		}

		try {
			con = ConnectionManager.getConnection();
			String query = "select distinct nt_m_sisu_permit_hol_service_info.sps_service_no  FROM public.nt_m_log_sheets "
					+ " inner join nt_r_subsidy_service_type on nt_r_subsidy_service_type.st_code = nt_m_log_sheets.los_subsidy_service_type_code "
					+ " inner join nt_m_log_sheet_summary on nt_m_log_sheet_summary.lss_log_sheet_master_seq = nt_m_log_sheets.los_seq "
					+ " inner join nt_m_sisu_permit_hol_service_info on nt_m_sisu_permit_hol_service_info.sps_service_ref_no=nt_m_log_sheets.los_std_service_ref_no "
					+ " where sps_service_no is not null and los_subsidy_service_type_code=? and sps_status='A' " + WHERE_SQL
					+ " order by sps_service_no ;";

			ps = con.prepareStatement(query);
			ps.setString(1, dto.getServiceType());
			rs = ps.executeQuery();

			while (rs.next()) {
				value = rs.getString("sps_service_no");
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
	public List<String> serviceNoGamiSeriyaDropDown(String serviceType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String value = "";
		List<String> returnList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct nt_m_gami_permit_hol_service_info.gps_service_no FROM public.nt_m_log_sheets inner join nt_r_subsidy_service_type on nt_r_subsidy_service_type.st_code = nt_m_log_sheets.los_subsidy_service_type_code inner join nt_m_log_sheet_summary on nt_m_log_sheet_summary.lss_log_sheet_master_seq = nt_m_log_sheets.los_seq inner join nt_m_gami_permit_hol_service_info  on nt_m_gami_permit_hol_service_info.gps_service_ref_no=nt_m_log_sheets.los_std_service_ref_no where gps_service_no is not null and los_subsidy_service_type_code=?  order by gps_service_no desc;";

			ps = con.prepareStatement(query);
			ps.setString(1, serviceType);
			rs = ps.executeQuery();

			while (rs.next()) {
				value = rs.getString("gps_service_no");

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
	public List<String> serviceNoGamiSeriyaDropDown(LogSheetMaintenanceDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String value = "";
		List<String> returnList = new ArrayList<String>();
		String WHERE_SQL = "";

		if (dto.getNameOfOperator() != null && !dto.getNameOfOperator().equals("")) {
			WHERE_SQL = WHERE_SQL + " AND nt_m_gami_permit_hol_service_info.gps_operator_name = '"
					+ dto.getNameOfOperator() + "' ";
		}
		try {
			con = ConnectionManager.getConnection();
			String query = "select distinct nt_m_gami_permit_hol_service_info.gps_service_no FROM public.nt_m_log_sheets "
					+ " inner join nt_r_subsidy_service_type on nt_r_subsidy_service_type.st_code = nt_m_log_sheets.los_subsidy_service_type_code "
					+ " inner join nt_m_log_sheet_summary on nt_m_log_sheet_summary.lss_log_sheet_master_seq = nt_m_log_sheets.los_seq "
					+ " inner join nt_m_gami_permit_hol_service_info  on nt_m_gami_permit_hol_service_info.gps_service_ref_no=nt_m_log_sheets.los_std_service_ref_no "
					+ " where gps_service_no is not null and los_subsidy_service_type_code=? " + WHERE_SQL
					+ " order by gps_service_no ;";
			ps = con.prepareStatement(query);
			ps.setString(1, dto.getServiceType());
			rs = ps.executeQuery();
			while (rs.next()) {
				value = rs.getString("gps_service_no");
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
	public boolean initialGamiSeriyaSearch(LogSheetMaintenanceDTO logDTO) {
		boolean value = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT gps_operator_name, gps_origin, gps_service_start_date, gps_service_end_date, gps_destination, gps_via, gps_status, gps_total_length FROM public.nt_m_gami_permit_hol_service_info where gps_service_no= ? ; ";

			ps = con.prepareStatement(query);
			ps.setString(1, logDTO.getServiceNo());
			rs = ps.executeQuery();

			if (!rs.isBeforeFirst()) {

				value = false;
			} else {

				value = true;
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
	public LogSheetMaintenanceDTO searchGamiSeriya(LogSheetMaintenanceDTO logDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select gps_operator_name,gps_origin,gps_service_start_date,gps_service_end_date,gps_destination, "
					+ "gps_via,gps_status,gps_total_length,gps_bus_no  "
					+ "from public.nt_m_gami_permit_hol_service_info " + "where gps_service_no ='"
					+ logDTO.getServiceNo() + "'";

			if (!(logDTO.getReferenceNo() == null || logDTO.getReferenceNo().isEmpty()
					|| logDTO.getReferenceNo().equals(""))) {
				query = query + " and gps_service_ref_no ";
			}

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				logDTO.setNameOfOperator(rs.getString("gps_operator_name"));
				logDTO.setEndOfStartDate(rs.getString("gps_service_start_date"));

				if (logDTO.getEndOfStartDate() != null) {
					logDTO.setEndOfStartDate(logDTO.getEndOfStartDate().substring(0, 10));
				}
				logDTO.setOrigin(rs.getString("gps_origin"));
				logDTO.setServiceEndDate(rs.getString("gps_service_end_date"));

				if (logDTO.getServiceEndDate() != null) {

					logDTO.setServiceEndDate(logDTO.getServiceEndDate().substring(0, 10));
				}
				logDTO.setStatus(rs.getString("gps_status"));

				if (logDTO.getStatus().equals("A")) {
					logDTO.setStatus("ACTIVE");
				}

				else if (logDTO.getStatus().equals("I")) {
					logDTO.setStatus("INACTIVE");
				}

				logDTO.setDestination(rs.getString("gps_destination"));
				logDTO.setVia(rs.getString("gps_via"));
				logDTO.setDistance(rs.getString("gps_total_length"));
				logDTO.setBusNo(rs.getString("gps_bus_no"));

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return logDTO;
	}

	@Override
	public LogSheetMaintenanceDTO getRefNoGamiSeriya(LogSheetMaintenanceDTO logDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT gps_service_ref_no " + "FROM nt_m_gami_permit_hol_service_info "
					+ "where gps_service_no ='" + logDTO.getServiceNo() + "'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				logDTO.setReferenceNo(rs.getString("gps_service_ref_no"));
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return logDTO;
	}

	@Override
	public String paymentRateGamiSeriya() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String value = "";

		try {
			con = ConnectionManager.getConnection();

			String query = "select number_value " + "from nt_r_parameters "
					+ "where param_name='PAYMENT_RATE_LOG_SHEET_GAMI' ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				value = rs.getString("number_value");

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
	public String paymentRateNisiSeriya() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String value = "";

		try {
			con = ConnectionManager.getConnection();

			String query = "select number_value " + "from nt_r_parameters "
					+ "where param_name='PAYMENT_RATE_LOG_SHEET_NISI' ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				value = rs.getString("number_value");

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
	public String totalLengthGamiSeriya(String serviceNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String value = "";

		try {
			con = ConnectionManager.getConnection();
			// Changed By : Dinushi Ranasinghe As Requested by NTC
			// Changed Date : 11-10-2019
			String query = "select gps_unserved_portion_km " + "from nt_m_gami_permit_hol_service_info "
					+ "where gps_status='A' and gps_service_no='" + serviceNo + "'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				value = rs.getString("gps_unserved_portion_km");

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
	public List<SisuSeriyaDTO> getServiceAgtNoForNisiSeriyaDropdown(SisuSeriyaDTO sisuSeriyaDto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SisuSeriyaDTO> serviceRefNoList = new ArrayList<SisuSeriyaDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct nt_m_nri_requester_info.nri_service_agreement_no FROM public.nt_m_log_sheets inner join nt_r_subsidy_service_type on nt_r_subsidy_service_type.st_code = nt_m_log_sheets.los_subsidy_service_type_code inner join nt_m_log_sheet_summary on nt_m_log_sheet_summary.lss_log_sheet_master_seq = nt_m_log_sheets.los_seq inner join nt_m_nri_requester_info  on nt_m_nri_requester_info.nri_service_ref_no=nt_m_log_sheets.los_std_service_ref_no where nri_service_agreement_no is not null and los_subsidy_service_type_code=?  order by nri_service_agreement_no ;";

			ps = con.prepareStatement(query);

			ps.setString(1, sisuSeriyaDto.getServiceTypeCode());
			rs = ps.executeQuery();

			while (rs.next()) {
				SisuSeriyaDTO ssDto = new SisuSeriyaDTO();
				ssDto.setServiceNo(rs.getString("nri_service_agreement_no"));
				serviceRefNoList.add(ssDto);

			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return serviceRefNoList;
	}

	@Override
	public List<SisuSeriyaDTO> showSearchedDataForNisiSeriya(SisuSeriyaDTO sisuSeriyaDto, String logSheetYear) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> recieveDataOnGrid = new ArrayList<SisuSeriyaDTO>();

		String WHERE_SQL = "";
		boolean whereadded = false;

		if (sisuSeriyaDto.getServiceRefNo() != null && !sisuSeriyaDto.getServiceRefNo().equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE los_std_service_ref_no = " + "'" + sisuSeriyaDto.getServiceRefNo()
					+ "' and los_subsidy_service_type_code = " + "'" + sisuSeriyaDto.getServiceTypeCode() + "' ";
			whereadded = true;
		}
		if (sisuSeriyaDto.getServiceNo() != null && !sisuSeriyaDto.getServiceNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND nri_service_agreement_no = " + "'" + sisuSeriyaDto.getServiceNo() + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE nri_service_agreement_no = " + "'" + sisuSeriyaDto.getServiceNo()
						+ "' and los_subsidy_service_type_code = " + "'" + sisuSeriyaDto.getServiceTypeCode() + "' ";
				whereadded = true;
			}
		}

		if (logSheetYear != null && !logSheetYear.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND nt_m_log_sheet_summary.lss_year = " + "'" + logSheetYear + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE nt_m_log_sheet_summary.lss_year = " + "'" + logSheetYear
						+ "' and los_subsidy_service_type_code = " + "'" + sisuSeriyaDto.getServiceTypeCode() + "' ";
				whereadded = true;
			}
		}

		if (sisuSeriyaDto.getStringMonth() != null && !sisuSeriyaDto.getStringMonth().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND lss_month = " + "'" + sisuSeriyaDto.getStringMonth() + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE lss_month = " + "'" + sisuSeriyaDto.getStringMonth()
						+ "' and los_subsidy_service_type_code = " + "'" + sisuSeriyaDto.getServiceTypeCode() + "' ";
				whereadded = true;
			}
		}
		
		if (sisuSeriyaDto.getBusRegNo() != null && !sisuSeriyaDto.getBusRegNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND nt_m_nri_requester_info.nri_bus_no  = " + "'" + sisuSeriyaDto.getBusRegNo() + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE nt_m_nri_requester_info.nri_bus_no = " + "'" + sisuSeriyaDto.getBusRegNo()
						+ "' and los_subsidy_service_type_code = " + "'" + sisuSeriyaDto.getServiceTypeCode() + "' ";
				whereadded = true;
			}
		}
		
		if (sisuSeriyaDto.getNameOfOperator()!= null && !sisuSeriyaDto.getNameOfOperator().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND nt_m_nri_requester_info.nri_operator_name  = " + "'" + sisuSeriyaDto.getNameOfOperator() + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE nt_m_nri_requester_info.nri_operator_name  = " + "'" + sisuSeriyaDto.getNameOfOperator()
						+ "' and los_subsidy_service_type_code = " + "'" + sisuSeriyaDto.getServiceTypeCode() + "' ";
				whereadded = true;
			}
		}
		
		

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct los_std_service_ref_no, nt_m_log_sheet_summary.lss_year, los_no_of_copies, nt_m_log_sheet_summary.lss_received_date, "
					+ "nt_m_log_sheet_summary.lss_month,nt_m_log_sheet_summary.lss_log_ref_no,nt_r_subsidy_service_type.st_description   from public.nt_m_log_sheets "
					+ "inner join nt_m_log_sheet_summary on nt_m_log_sheet_summary.lss_log_sheet_master_seq = nt_m_log_sheets.los_seq "
					+ "inner join nt_r_subsidy_service_type on nt_r_subsidy_service_type.st_code = nt_m_log_sheets.los_subsidy_service_type_code "
					+ "inner join nt_m_nri_requester_info on nt_m_nri_requester_info.nri_service_ref_no=nt_m_log_sheets.los_std_service_ref_no "
					+ WHERE_SQL;

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				SisuSeriyaDTO sisuSeriyaDto1 = new SisuSeriyaDTO();

				sisuSeriyaDto1.setServiceRefNo(rs.getString("los_std_service_ref_no"));
				sisuSeriyaDto1.setYear(rs.getInt("lss_year"));
				sisuSeriyaDto1.setLogRefCheck(false);
				sisuSeriyaDto.setTransactionDescription(rs.getString("st_description"));
				sisuSeriyaDto1.setStringMonth((rs.getString("lss_month")));
				sisuSeriyaDto1.setLogRefNo(rs.getString("lss_log_ref_no"));
				sisuSeriyaDto1.setReceivedDate(rs.getDate("lss_received_date"));

				recieveDataOnGrid.add(sisuSeriyaDto1);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return recieveDataOnGrid;
	}

	@Override
	public List<String> serviceNoNisiSeriyaDropDown(String serviceType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String value = "";
		List<String> returnList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct nt_m_nri_requester_info.nri_service_agreement_no  FROM public.nt_m_log_sheets inner join nt_r_subsidy_service_type on nt_r_subsidy_service_type.st_code = nt_m_log_sheets.los_subsidy_service_type_code inner join nt_m_log_sheet_summary on nt_m_log_sheet_summary.lss_log_sheet_master_seq = nt_m_log_sheets.los_seq inner join nt_m_nri_requester_info on nt_m_nri_requester_info.nri_service_ref_no=nt_m_log_sheets.los_std_service_ref_no where nri_service_agreement_no is not null and los_subsidy_service_type_code=?  order by nri_service_agreement_no desc;";

			ps = con.prepareStatement(query);
			ps.setString(1, serviceType);
			rs = ps.executeQuery();

			while (rs.next()) {
				value = rs.getString("nri_service_agreement_no");

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
	public List<String> serviceNoNisiSeriyaDropDown(LogSheetMaintenanceDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String value = "";
		List<String> returnList = new ArrayList<String>();
		String WHERE_SQL = "";

		if (dto.getNameOfOperator() != null && !dto.getNameOfOperator().equals("")) {
			WHERE_SQL = WHERE_SQL + " AND nt_m_nri_requester_info.nri_operator_name = '" + dto.getNameOfOperator()
					+ "' ";
		}
		try {
			con = ConnectionManager.getConnection();
			String query = "select distinct nt_m_nri_requester_info.nri_service_agreement_no FROM public.nt_m_log_sheets "
					+ " inner join nt_r_subsidy_service_type on nt_r_subsidy_service_type.st_code = nt_m_log_sheets.los_subsidy_service_type_code "
					+ " inner join nt_m_log_sheet_summary on nt_m_log_sheet_summary.lss_log_sheet_master_seq = nt_m_log_sheets.los_seq "
					+ " inner join nt_m_nri_requester_info on nt_m_nri_requester_info.nri_service_ref_no=nt_m_log_sheets.los_std_service_ref_no "
					+ " where nri_service_agreement_no is not null and los_subsidy_service_type_code=? " + WHERE_SQL
					+ " order by nri_service_agreement_no ;";
			ps = con.prepareStatement(query);
			ps.setString(1, dto.getServiceType());
			rs = ps.executeQuery();

			while (rs.next()) {
				value = rs.getString("nri_service_agreement_no");
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
	public boolean initialNisiSeriyaSearch(LogSheetMaintenanceDTO logDTO) {
		boolean value = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT nri_operator_name, nri_origin, nri_service_start_date, nri_service_end_date, nri_destination, nri_via, nri_status, nri_distance FROM public.nt_m_nri_requester_info where nri_service_agreement_no= ? ; ";

			ps = con.prepareStatement(query);
			ps.setString(1, logDTO.getServiceNo());
			rs = ps.executeQuery();

			if (!rs.isBeforeFirst()) {

				value = false;
			} else {

				value = true;
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
	public LogSheetMaintenanceDTO searchNisiSeriya(LogSheetMaintenanceDTO logDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select nri_operator_name,nri_origin,nri_service_start_date,nri_service_end_date,nri_destination, "
					+ "nri_via,nri_status,nri_distance,nri_bus_no " + "from public.nt_m_nri_requester_info "
					+ "where nri_service_agreement_no ='" + logDTO.getServiceNo() + "'";

			if (!(logDTO.getReferenceNo() == null || logDTO.getReferenceNo().isEmpty()
					|| logDTO.getReferenceNo().equals(""))) {
				query = query + " and nri_service_ref_no ";
			}

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				logDTO.setNameOfOperator(rs.getString("nri_operator_name"));
				logDTO.setEndOfStartDate(rs.getString("nri_service_start_date"));

				if (logDTO.getEndOfStartDate() != null) {
					logDTO.setEndOfStartDate(logDTO.getEndOfStartDate().substring(0, 10));
				}
				logDTO.setOrigin(rs.getString("nri_origin"));
				logDTO.setServiceEndDate(rs.getString("nri_service_end_date"));

				if (logDTO.getServiceEndDate() != null) {

					logDTO.setServiceEndDate(logDTO.getServiceEndDate().substring(0, 10));
				}
				logDTO.setStatus(rs.getString("nri_status"));

				if (logDTO.getStatus().equals("A")) {
					logDTO.setStatus("ACTIVE");
				}

				else if (logDTO.getStatus().equals("I")) {
					logDTO.setStatus("INACTIVE");
				}

				logDTO.setDestination(rs.getString("nri_destination"));
				logDTO.setVia(rs.getString("nri_via"));
				logDTO.setDistance(rs.getString("nri_distance"));
				logDTO.setBusNo(rs.getString("nri_bus_no"));

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return logDTO;
	}

	@Override
	public LogSheetMaintenanceDTO getRefNoNisiSeriya(LogSheetMaintenanceDTO logDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT nri_service_ref_no " + "FROM nt_m_nri_requester_info "
					+ "where nri_service_agreement_no ='" + logDTO.getServiceNo() + "'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				logDTO.setReferenceNo(rs.getString("nri_service_ref_no"));
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return logDTO;
	}

	@Override
	public String totalLengthNisiSeriya(String serviceNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String value = "";

		try {
			con = ConnectionManager.getConnection();

			String query = "select nri_distance " + "from nt_m_nri_requester_info "
					+ "where nri_status='A' and nri_service_agreement_no='" + serviceNo + "'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				value = rs.getString("nri_distance");

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
	public boolean editFromgLogSheetGamiAndNisi(LogSheetMaintenanceDTO logDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		boolean success = false;

		try {
			con = ConnectionManager.getConnection();

			String query = "UPDATE nt_m_log_sheet_summary "
					+ "set lss_no_of_turns=?,lss_no_of_turns_gps=?,lss_log_sheet_dealy=?,lss_average_pessengers=?,lss_school_req_running_days=?, "
					+ "lss_late_tips=?,lss_is_school_approval=?,lss_arrears=?,lss_deduction=?,lss_remarks=?,lss_log_type=?,lss_payment_type=?,lss_running_pre=?, "
					+ "lss_total_length=?,lss_subsidy_amt=?,lss_late_fee=?,lss_penalty_fee=?,lss_payment_amt=?,lss_balance_amt=?,lss_service_no=?,lss_pending_amt=0,lss_paid_amt=0, lss_other_penalty_fee=?, lss_other_penalty_percentage=?, lss_tips_perday=?, lss_requested_pre=?,lss_recalculated_by =?,lss_recalculated_date=?  "
					+ "WHERE lss_log_ref_no=?";

			try {
				ps = con.prepareStatement(query);
				ps.setInt(1, logDTO.getNoOfTurns());
				ps.setInt(2, logDTO.getNoOfTurnsinGPS());
				ps.setInt(3, logDTO.getLogSheetDelay());
				ps.setInt(4, logDTO.getAveragePasengers());
				ps.setInt(5, logDTO.getSchoolRequiredRunningDate());
				ps.setInt(6, logDTO.getLateTrips());

				if (logDTO.isApprovalBoolean() == true) {
					logDTO.setSchoolApprovals("Y");
					ps.setString(7, logDTO.getSchoolApprovals());
				} else {
					logDTO.setSchoolApprovals("N");
					ps.setString(7, logDTO.getSchoolApprovals());
				}

				ps.setInt(8, logDTO.getArrears());
				ps.setInt(9, logDTO.getDeductions());
				ps.setString(10, logDTO.getSpecialRemark());
				ps.setString(11, logDTO.getLogType());
				ps.setString(12, logDTO.getPaymentType());
				ps.setBigDecimal(13, logDTO.getRunningD());
				ps.setBigDecimal(14, logDTO.getTotalLengthD());
				ps.setBigDecimal(15, logDTO.getSubsidyD());
				ps.setBigDecimal(16, logDTO.getLateFeeD());
				ps.setBigDecimal(17, logDTO.getPenaltyFeeD());
				ps.setBigDecimal(18, logDTO.getPaymentD());
				ps.setBigDecimal(19, logDTO.getPaymentD());
				ps.setString(20, logDTO.getServiceNo());
				ps.setBigDecimal(21, logDTO.getOtherPercentageCalculationValD());
				ps.setDouble(22, logDTO.getOtherPercentage());
				ps.setInt(23, logDTO.getTripsPerDay());
				ps.setInt(24, logDTO.getRequested());
				
				ps.setString(25, logDTO.getReCalculateBy());
				if(logDTO.getReCalculateBy()!= null) {
				ps.setTimestamp(26, timestamp);
				}else {
					ps.setTimestamp(26, null);	
				}
				
				ps.setString(27, logDTO.getLogRefNo());
				ps.executeUpdate();
				con.commit();
				success = true;
			} catch (SQLException e) {
				con.rollback();
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
		return success;
	}

	@Override
	public List<SisuSeriyaDTO> getServiceAgtNoByRefNo(SisuSeriyaDTO dto) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;

		List<SisuSeriyaDTO> serviceRefNoList = new ArrayList<SisuSeriyaDTO>();
		try {
			con = ConnectionManager.getConnection();

			if(dto.getServiceTypeCode().equalsIgnoreCase("S03"))
			{
			 query = "select distinct nt_m_nri_requester_info.nri_service_agreement_no as sps_service_no"
					+ " FROM public.nt_m_log_sheets inner join nt_r_subsidy_service_type on nt_r_subsidy_service_type.st_code = nt_m_log_sheets.los_subsidy_service_type_code"
					+ " inner join nt_m_log_sheet_summary on nt_m_log_sheet_summary.lss_log_sheet_master_seq = nt_m_log_sheets.los_seq"
					+ " inner join nt_m_nri_requester_info on nt_m_nri_requester_info.nri_service_ref_no=nt_m_log_sheets.los_std_service_ref_no "
					+ " WHERE nri_service_agreement_no is not null and los_subsidy_service_type_code=? "
					+ " and los_std_service_ref_no =? "
					+ " ORDER BY sps_service_no ; ";
			}
			else
			{
			 query = "select distinct nt_m_sisu_permit_hol_service_info.sps_service_no  FROM public.nt_m_log_sheets "
					+ "inner join nt_r_subsidy_service_type on nt_r_subsidy_service_type.st_code = nt_m_log_sheets.los_subsidy_service_type_code "
					+ "inner join nt_m_log_sheet_summary on nt_m_log_sheet_summary.lss_log_sheet_master_seq = nt_m_log_sheets.los_seq "
					+ "inner join nt_m_sisu_permit_hol_service_info on nt_m_sisu_permit_hol_service_info.sps_service_ref_no=nt_m_log_sheets.los_std_service_ref_no "
					+ "where sps_service_no is not null and los_subsidy_service_type_code=? and los_std_service_ref_no=? order by sps_service_no ";
			}
			ps = con.prepareStatement(query);

			ps.setString(1, dto.getServiceTypeCode());
			ps.setString(2, dto.getServiceRefNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				SisuSeriyaDTO sisuSeriyaDto = new SisuSeriyaDTO();
				sisuSeriyaDto.setServiceNo(rs.getString("sps_service_no"));
				serviceRefNoList.add(sisuSeriyaDto);

			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return serviceRefNoList;
	}

	@Override
	public List<String> getBusNumberListForSisu(String subsidyType) {


		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<String> busNoList = new ArrayList<String>();
		String busNo = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct nt_m_sisu_permit_hol_service_info.sps_bus_no \r\n" + 
					"FROM public.nt_m_log_sheets \r\n" + 
					"inner join nt_r_subsidy_service_type on nt_r_subsidy_service_type.st_code = nt_m_log_sheets.los_subsidy_service_type_code \r\n" + 
					"inner join nt_m_log_sheet_summary on nt_m_log_sheet_summary.lss_log_sheet_master_seq = nt_m_log_sheets.los_seq \r\n" + 
					"inner join nt_m_sisu_permit_hol_service_info on nt_m_sisu_permit_hol_service_info.sps_service_ref_no=nt_m_log_sheets.los_std_service_ref_no \r\n" + 
					"where los_subsidy_service_type_code=? and sps_bus_no is not null and sps_bus_no!=''  order by sps_bus_no;";

			ps = con.prepareStatement(query);

			ps.setString(1, subsidyType);
			rs = ps.executeQuery();

			while (rs.next()) {
				
				busNo =rs.getString("sps_bus_no");
				busNoList.add(busNo);

			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return busNoList;
	
	}

	@Override
	public List<String> getBusNumberListForGami(String subsidyType) {



		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<String> busNoList = new ArrayList<String>();
		String busNo = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct nt_m_gami_permit_hol_service_info.gps_bus_no FROM public.nt_m_log_sheets\r\n" + 
					"inner join nt_r_subsidy_service_type on nt_r_subsidy_service_type.st_code = nt_m_log_sheets.los_subsidy_service_type_code\r\n" + 
					"inner join nt_m_log_sheet_summary on nt_m_log_sheet_summary.lss_log_sheet_master_seq = nt_m_log_sheets.los_seq\r\n" + 
					"inner join nt_m_gami_permit_hol_service_info  on nt_m_gami_permit_hol_service_info.gps_service_ref_no=nt_m_log_sheets.los_std_service_ref_no \r\n" + 
					"where gps_bus_no is not null and los_subsidy_service_type_code=? and gps_bus_no!=''   order by gps_bus_no ;";

			ps = con.prepareStatement(query);

			ps.setString(1, subsidyType);
			rs = ps.executeQuery();

			while (rs.next()) {
				
				busNo =rs.getString("gps_bus_no");
				busNoList.add(busNo);

			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return busNoList;
	
	
	}

	@Override
	public List<String> getBusNumberListForNisi(String subsidyType) {




		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<String> busNoList = new ArrayList<String>();
		String busNo = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct nt_m_nri_requester_info.nri_bus_no FROM public.nt_m_log_sheets \r\n" + 
					"inner join nt_r_subsidy_service_type on nt_r_subsidy_service_type.st_code = nt_m_log_sheets.los_subsidy_service_type_code \r\n" + 
					"inner join nt_m_log_sheet_summary on nt_m_log_sheet_summary.lss_log_sheet_master_seq = nt_m_log_sheets.los_seq\r\n" + 
					"inner join nt_m_nri_requester_info  on nt_m_nri_requester_info.nri_service_ref_no=nt_m_log_sheets.los_std_service_ref_no \r\n" + 
					"where nri_bus_no is not null and los_subsidy_service_type_code=?  and nri_bus_no!='' order by nri_bus_no ;";

			ps = con.prepareStatement(query);

			ps.setString(1, subsidyType);
			rs = ps.executeQuery();

			while (rs.next()) {
				
				busNo =rs.getString("nri_bus_no");
				busNoList.add(busNo);

			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return busNoList;
	
	
	
	}

	@Override
	public List<String> getNameListForSisu(String subsidyType) {



		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<String> operatorList = new ArrayList<String>();
		String operator = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct nt_m_sisu_permit_hol_service_info.sps_operator_name \r\n" + 
					"FROM public.nt_m_log_sheets \r\n" + 
					"inner join nt_r_subsidy_service_type on nt_r_subsidy_service_type.st_code = nt_m_log_sheets.los_subsidy_service_type_code \r\n" + 
					"inner join nt_m_log_sheet_summary on nt_m_log_sheet_summary.lss_log_sheet_master_seq = nt_m_log_sheets.los_seq \r\n" + 
					"inner join nt_m_sisu_permit_hol_service_info on nt_m_sisu_permit_hol_service_info.sps_service_ref_no=nt_m_log_sheets.los_std_service_ref_no \r\n" + 
					"where los_subsidy_service_type_code=? and sps_operator_name is not null  order by sps_operator_name;";

			ps = con.prepareStatement(query);

			ps.setString(1, subsidyType);
			rs = ps.executeQuery();

			while (rs.next()) {
				
				operator =rs.getString("sps_operator_name");
				operatorList.add(operator);

			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return operatorList;
	
	
	
	
	}

	@Override
	public List<String> getNameListForGami(String subsidyType) {


		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<String> operatorList = new ArrayList<String>();
		String operator = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct nt_m_gami_permit_hol_service_info.gps_operator_name FROM public.nt_m_log_sheets\r\n" + 
					"inner join nt_r_subsidy_service_type on nt_r_subsidy_service_type.st_code = nt_m_log_sheets.los_subsidy_service_type_code\r\n" + 
					"inner join nt_m_log_sheet_summary on nt_m_log_sheet_summary.lss_log_sheet_master_seq = nt_m_log_sheets.los_seq\r\n" + 
					"inner join nt_m_gami_permit_hol_service_info  on nt_m_gami_permit_hol_service_info.gps_service_ref_no=nt_m_log_sheets.los_std_service_ref_no \r\n" + 
					"where gps_operator_name is not null and los_subsidy_service_type_code=? and gps_operator_name!=''  order by gps_operator_name ;";

			ps = con.prepareStatement(query);

			ps.setString(1, subsidyType);
			rs = ps.executeQuery();

			while (rs.next()) {
				
				operator =rs.getString("gps_operator_name");
				operatorList.add(operator);

			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return operatorList;
	
	
	
	}

	@Override
	public List<String> getNameListForNisi(String subsidyType) {




		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<String> operatorList = new ArrayList<String>();
		String operator = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct nt_m_nri_requester_info.nri_operator_name FROM public.nt_m_log_sheets \r\n" + 
					"inner join nt_r_subsidy_service_type on nt_r_subsidy_service_type.st_code = nt_m_log_sheets.los_subsidy_service_type_code \r\n" + 
					"inner join nt_m_log_sheet_summary on nt_m_log_sheet_summary.lss_log_sheet_master_seq = nt_m_log_sheets.los_seq\r\n" + 
					"inner join nt_m_nri_requester_info  on nt_m_nri_requester_info.nri_service_ref_no=nt_m_log_sheets.los_std_service_ref_no \r\n" + 
					"where nri_operator_name is not null and los_subsidy_service_type_code=?  and nri_operator_name!='' order by nri_operator_name ;";

			ps = con.prepareStatement(query);

			ps.setString(1, subsidyType);
			rs = ps.executeQuery();

			while (rs.next()) {
				
				operator =rs.getString("nri_operator_name");
				operatorList.add(operator);

			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return operatorList;
	
	
	
	}

	@Override
	public BigDecimal getPaymentValueByRefNo(String refNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<String> operatorList = new ArrayList<String>();
		BigDecimal paymentDTemp = new BigDecimal(0);
		try {
			con = ConnectionManager.getConnection();

			String query = "select lss_subsidy_amt  FROM public.nt_m_log_sheet_summary " + 
					"WHERE lss_log_ref_no =?";

			ps = con.prepareStatement(query);

			ps.setString(1, refNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				
				paymentDTemp =rs.getBigDecimal("lss_subsidy_amt");
				

			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return paymentDTemp;
	
	
	
	
	}



}