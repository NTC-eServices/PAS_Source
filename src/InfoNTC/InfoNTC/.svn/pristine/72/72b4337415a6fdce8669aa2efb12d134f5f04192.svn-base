package lk.informatics.ntc.view.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import lk.informatics.roc.utils.common.Utils;

public class EOD {

	public static Logger logger = Logger.getLogger(EOD.class);

	public static int firstReminderMonths = 0;
	public static int secondReminderMonths = 0;
	public static int thirdReminderMonths = 0;

	public static String firstReminderStatus = "A";
	public static String secondReminderStatus = "E";
	public static String thirdReminderStatus = "TC";

	public static long EODProcessSeq = 0;

	private static String firstLetterCode = null;
	private static String secondLetterCode = null;
	private static String thirdLetterCode = null;

	public static void main(String[] args) {

		Connection con = null;

		con = ConnectionManager.getConnection();

		initiateConstants(con);

		initializeEODProcess(con);

		try {

			con.commit();
		} catch (SQLException e1) {
			updateEODProcess(con, "E");
			e1.printStackTrace();
		}

		ConnectionManager.close(con);
	}

	public static void initializeEODProcess(Connection con) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String processCode = "";
		try {

			String query = "SELECT code FROM public.nt_r_eod_process_maintenance WHERE status = 'A'";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				processCode = rs.getString("code");

				processEOD(con, processCode);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
		}
	}

	public static void processEOD(Connection con, String processCode) {

		switch (processCode) {
		case "001":
			EODProcessSeq = createEODProcess(con, processCode);
			firstReminders(con, processCode);
		case "002":
			EODProcessSeq = createEODProcess(con, processCode);
			secondReminders(con, processCode);
		case "003":
			EODProcessSeq = createEODProcess(con, processCode);
			thirdReminders(con, processCode);
		default:
			break;
		}
	}

	public static void firstReminders(Connection con, String processCode) {

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		Date today = null;

		try {
			today = dateFormat.parse(dateFormat.format(timestamp));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Date validToDate = null;

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			String query = "select pm_application_no, pm_valid_to" + " from nt_t_pm_application"
					+ " where pm_status ='A'" + " and pm_is_first_notice is null" + " and pm_is_second_notice is null"
					+ " and pm_is_third_notice is null"
					+ " and pm_valid_to is not null order by (TO_DATE(pm_valid_to,'dd/MM/yyyy')) asc";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				String applicationNo = rs.getString("pm_application_no");
				String validTo = rs.getString("pm_valid_to");

				try {
					validToDate = dateFormat.parse(validTo);
				} catch (ParseException e) {
					e.printStackTrace();
				}

				Calendar calendar = Calendar.getInstance();

				calendar.setTime(validToDate);

				calendar.add(Calendar.MONTH, firstReminderMonths);

				Date expireDate = calendar.getTime();

				if (expireDate.before(today) || expireDate.equals(today)) {
					updateApplicationStatus(con, "FIRST_REMIND", applicationNo, processCode);
					createReminderLetter(con, applicationNo, firstLetterCode, processCode);

				}
			}
			updateEODProcess(con, "C");
		} catch (Exception e) {
			updateEODProcess(con, "E");
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
		}

	}

	public static void secondReminders(Connection con, String processCode) {

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		Date today = null;

		try {
			today = dateFormat.parse(dateFormat.format(timestamp));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Date validToDate = null;

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			String query = "select pm_application_no, pm_valid_to" + " from nt_t_pm_application" + " where pm_status ='"
					+ firstReminderStatus + "'" + " and pm_is_first_notice  ='Y'" + " and pm_is_second_notice is null"
					+ " and pm_is_third_notice is null" + " and pm_valid_to is not null"
					+ " order by (TO_DATE(pm_valid_to,'dd/MM/yyyy')) asc";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				String applicationNo = rs.getString("pm_application_no");
				String validTo = rs.getString("pm_valid_to");

				try {
					validToDate = dateFormat.parse(validTo);
				} catch (ParseException e) {
					e.printStackTrace();
				}

				Calendar calendar = Calendar.getInstance();

				calendar.setTime(validToDate);

				calendar.add(Calendar.MONTH, secondReminderMonths);

				Date expireDate = calendar.getTime();

				Calendar calendar2 = Calendar.getInstance();

				calendar2.setTime(validToDate);

				calendar2.add(Calendar.MONTH, thirdReminderMonths);

				Date expireDate2 = calendar2.getTime();

				if ((today.after(expireDate) || validToDate.equals(expireDate))
						&& (expireDate2.after(expireDate) || expireDate2.equals(expireDate))) {

					updateApplicationStatus(con, "SECOND_REMIND", applicationNo, processCode);
					createReminderLetter(con, applicationNo, secondLetterCode, processCode);
				}
			}
			updateEODProcess(con, "C");
		} catch (Exception e) {
			updateEODProcess(con, "E");
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
		}

	}

	public static void thirdReminders(Connection con, String processCode) {

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		Date today = null;

		try {
			today = dateFormat.parse(dateFormat.format(timestamp));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Date validToDate = null;

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			String query = "select  *" + " from nt_t_pm_application" + " where pm_status ='" + secondReminderStatus
					+ "'" + " and pm_is_first_notice  ='Y'" + " and pm_is_second_notice  ='Y'"
					+ " and pm_is_third_notice is null"
					+ " and pm_valid_to is not null order by (TO_DATE(pm_valid_to,'dd/MM/yyyy')) asc";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				String applicationNo = rs.getString("pm_application_no");
				String validTo = rs.getString("pm_valid_to");
				try {
					validToDate = dateFormat.parse(validTo);
				} catch (ParseException e) {
					e.printStackTrace();
				}

				Calendar calendar = Calendar.getInstance();

				calendar.setTime(validToDate);

				calendar.add(Calendar.MONTH, thirdReminderMonths);

				Date expireDate = calendar.getTime();

				if (today.after(expireDate) || expireDate.equals(today)) {

					updateApplicationStatus(con, "THIRD_REMIND", applicationNo, processCode);
					createReminderLetter(con, applicationNo, thirdLetterCode, processCode);
				}
			}
			updateEODProcess(con, "C");
		} catch (Exception e) {
			updateEODProcess(con, "E");
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
		}

	}

	public static void initiateConstants(Connection con) {

		firstReminderMonths = getParaFirstReminderMonths(con);
		secondReminderMonths = getParaSecondReminderMonths(con);
		thirdReminderMonths = getParaThirdReminderMonths(con);

		firstReminderStatus = getParaFirstReminderStatus(con);
		secondReminderStatus = getParaSecondReminderStatus(con);
		thirdReminderStatus = getParaThirdReminderStatus(con);

		firstLetterCode = getParaFirstReminderLetterCode(con);
		secondLetterCode = getParaSecondReminderLetterCode(con);
		thirdLetterCode = getParaThirdReminderLetterCode(con);

	}

	public static void updateApplicationStatus(Connection con, String reminderType, String applicationNo,
			String processCode) {

		String sql = null;
		PreparedStatement ps = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			try {

				switch (reminderType) {
				case "FIRST_REMIND":
					sql = "UPDATE nt_t_pm_application SET pm_status='" + firstReminderStatus
							+ "', pm_modified_by='EOD', pm_modified_date='" + timestamp
							+ "',pm_is_first_notice='Y' WHERE pm_application_no='" + applicationNo + "'";
					break;
				case "SECOND_REMIND":
					sql = "UPDATE nt_t_pm_application SET pm_status='" + secondReminderStatus
							+ "', pm_modified_by='EOD', pm_modified_date='" + timestamp
							+ "',pm_is_second_notice='Y' WHERE pm_application_no='" + applicationNo + "'";

					break;
				case "THIRD_REMIND":
					sql = "UPDATE nt_t_pm_application SET pm_status='" + thirdReminderStatus
							+ "', pm_modified_by='EOD', pm_modified_date='" + timestamp
							+ "', pm_is_third_notice='Y' WHERE pm_application_no='" + applicationNo + "'";

					break;
				default:

					break;
				}

				ps = con.prepareStatement(sql);
				ps.executeUpdate();
			} catch (Exception e) {
				createEODErrorLog(con, processCode, applicationNo, e.toString());
				logger.error(e);
			}

		} catch (Exception e) {
			logger.error(e);
		} finally {
			ConnectionManager.close(ps);
		}

	}

	public static String getParaFirstReminderStatus(Connection con) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String data = "";
		try {

			String query = "SELECT param_name, string_value FROM public.nt_r_parameters WHERE param_name = 'FIRST_REMIND_STATUS'";
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
		}
		return data;
	}

	public static String getParaSecondReminderStatus(Connection con) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String data = "";
		try {

			String query = "SELECT param_name, string_value FROM public.nt_r_parameters WHERE param_name = 'SECOND_REMIND_STATUS'";
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
		}
		return data;
	}

	public static String getParaThirdReminderStatus(Connection con) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String data = "";
		try {

			String query = "SELECT param_name, string_value FROM public.nt_r_parameters WHERE param_name = 'THIRD_REMIND_STATUS'";
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
		}
		return data;
	}

	public static String getParaFirstReminderLetterCode(Connection con) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String data = "";
		try {

			String query = "SELECT param_name, string_value FROM public.nt_r_parameters WHERE param_name = 'FIRST_REMIND_LETTER_CODE'";
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
		}
		return data;
	}

	public static String getParaSecondReminderLetterCode(Connection con) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String data = "";
		try {

			String query = "SELECT param_name, string_value FROM public.nt_r_parameters WHERE param_name = 'SECOND_REMIND_LETTER_CODE'";
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
		}
		return data;
	}

	public static String getParaThirdReminderLetterCode(Connection con) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String data = "";
		try {

			String query = "SELECT param_name, string_value FROM public.nt_r_parameters WHERE param_name = 'THIRD_REMIND_LETTER_CODE'";
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
		}
		return data;
	}

	public static int getParaFirstReminderMonths(Connection con) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		int data = 0;
		try {

			String query = "SELECT param_name, number_value FROM public.nt_r_parameters WHERE param_name = 'FIRST_REMIND_MONTHS'";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				data = rs.getInt("number_value");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);

		}
		return data;
	}

	public static int getParaSecondReminderMonths(Connection con) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		int data = 0;
		try {

			String query = "SELECT param_name, number_value FROM public.nt_r_parameters WHERE param_name = 'SECOND_REMIND_MONTHS'";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				data = rs.getInt("number_value");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
		}
		return data;
	}

	public static int getParaThirdReminderMonths(Connection con) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		int data = 0;
		try {

			String query = "SELECT param_name, number_value FROM public.nt_r_parameters WHERE param_name = 'THIRD_REMIND_MONTHS'";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				data = rs.getInt("number_value");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
		}
		return data;
	}

	public static void createReminderLetter(Connection con, String applicationNo, String letterCode,
			String processCode) {

		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_reminder_letter");

			String sql = "INSERT INTO public.nt_t_reminder_letter "
					+ "(seqno, seq_of_eod_process, app_no, letter_type_code, processed_date, created_by, created_date, modify_by, modify_date) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setLong(2, EODProcessSeq);
			stmt.setString(3, applicationNo);
			stmt.setString(4, letterCode);
			stmt.setTimestamp(5, timestamp);
			stmt.setString(6, "EOD");
			stmt.setTimestamp(7, timestamp);
			stmt.setString(8, "EOD");
			stmt.setTimestamp(9, timestamp);

			stmt.executeUpdate();

		} catch (Exception ex) {
			createEODErrorLog(con, processCode, applicationNo, ex.toString());
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
		}

	}

	public static long createEODProcess(Connection con, String processCode) {

		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		long EODProcessSeq = 0;

		try {

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_eod_process");

			String sql = "INSERT INTO public.nt_m_eod_process "
					+ "(seqno, process_date, process_status, created_by, created_date, startdate_time,code) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?)";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setTimestamp(2, timestamp);
			stmt.setString(3, "O");
			stmt.setString(4, "EOD");
			stmt.setTimestamp(5, timestamp);
			stmt.setTimestamp(6, timestamp);
			stmt.setString(7, processCode);

			stmt.executeUpdate();

			EODProcessSeq = seqNo;

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
		}
		return EODProcessSeq;
	}

	public static void updateEODProcess(Connection con, String processStatus) {

		String sql = null;
		PreparedStatement ps = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			try {

				sql = "UPDATE nt_m_eod_process SET process_status='" + processStatus + "',enddate_time='" + timestamp
						+ "' WHERE seqno='" + EODProcessSeq + "'";

				ps = con.prepareStatement(sql);
				ps.executeUpdate();
			} catch (Exception e) {
				logger.error(e);
			}

		} catch (Exception e) {
			logger.error(e);
		} finally {
			ConnectionManager.close(ps);

		}

	}

	public static void createEODErrorLog(Connection con, String processCode, String applicationNo,
			String errorMessage) {

		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_eod_error_log");

			String sql = "INSERT INTO public.nt_t_eod_error_log "
					+ "(seqno, process_master_seq, process_code, app_no, error, created_by, created_date) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?)";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setLong(2, EODProcessSeq);
			stmt.setString(3, processCode);
			stmt.setString(4, applicationNo);
			stmt.setString(5, errorMessage);
			stmt.setString(6, "EOD");
			stmt.setTimestamp(7, timestamp);

			stmt.executeUpdate();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
		}

	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		EOD.logger = logger;
	}

	public static int getFirstReminderMonths() {
		return firstReminderMonths;
	}

	public static void setFirstReminderMonths(int firstReminderMonths) {
		EOD.firstReminderMonths = firstReminderMonths;
	}

	public static int getSecondReminderMonths() {
		return secondReminderMonths;
	}

	public static void setSecondReminderMonths(int secondReminderMonths) {
		EOD.secondReminderMonths = secondReminderMonths;
	}

	public static int getThirdReminderMonths() {
		return thirdReminderMonths;
	}

	public static void setThirdReminderMonths(int thirdReminderMonths) {
		EOD.thirdReminderMonths = thirdReminderMonths;
	}

	public static String getFirstReminderStatus() {
		return firstReminderStatus;
	}

	public static void setFirstReminderStatus(String firstReminderStatus) {
		EOD.firstReminderStatus = firstReminderStatus;
	}

	public static String getSecondReminderStatus() {
		return secondReminderStatus;
	}

	public static void setSecondReminderStatus(String secondReminderStatus) {
		EOD.secondReminderStatus = secondReminderStatus;
	}

	public static String getThirdReminderStatus() {
		return thirdReminderStatus;
	}

	public static void setThirdReminderStatus(String thirdReminderStatus) {
		EOD.thirdReminderStatus = thirdReminderStatus;
	}
}
