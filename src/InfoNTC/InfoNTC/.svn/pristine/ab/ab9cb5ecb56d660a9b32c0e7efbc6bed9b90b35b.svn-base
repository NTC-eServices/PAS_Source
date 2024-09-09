package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import lk.informatics.ntc.model.dto.PrintReminderLetterDTO;
import lk.informatics.ntc.view.util.ConnectionManager;

public class PrintLetterServiceImpl implements PrintLetterService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8900648528409597383L;

	@Override
	public List<PrintReminderLetterDTO> getLetterTypeDropDown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PrintReminderLetterDTO> letterType = new ArrayList<PrintReminderLetterDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct letter_type_code,b.lt_description,b.lt_code from public.nt_t_reminder_letter a inner join public.nt_r_letter_type b on  a.letter_type_code=b.lt_code;";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				PrintReminderLetterDTO printReminderLetter = new PrintReminderLetterDTO();
				printReminderLetter.setLetterTypeCode(rs.getString("lt_code"));
				printReminderLetter.setLetterType(rs.getString("lt_description"));

				letterType.add(printReminderLetter);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return letterType;

	}

	@Override
	public List<PrintReminderLetterDTO> getDataForSearch(String letterType, Timestamp startDt, Timestamp endDt) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PrintReminderLetterDTO> showGridValues = new ArrayList<PrintReminderLetterDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select letter_type_code,app_no ,print_date,b.lt_description,c.pm_is_first_notice,c.pm_is_second_notice,c.pm_is_third_notice,c.pm_permit_no"
					+ " from public.nt_t_reminder_letter a"
					+ " inner join public.nt_r_letter_type b on b.lt_code=a.letter_type_code"
					+ " inner join public.nt_t_pm_application c on c.pm_application_no=a.app_no"
					+ " where letter_type_code=?  and a.created_date  between ? and ?;";
			ps = con.prepareStatement(query);

			ps.setString(1, letterType);
			ps.setTimestamp(2, startDt);
			ps.setTimestamp(3, endDt);
			rs = ps.executeQuery();

			while (rs.next()) {
				PrintReminderLetterDTO printReminderLetter = new PrintReminderLetterDTO();
				printReminderLetter.setLetterType(rs.getString("lt_description"));
				printReminderLetter.setAppNo(rs.getString("app_no"));
				printReminderLetter.setPermitNo(rs.getString("pm_permit_no"));
				printReminderLetter.setFirstNoticeType(rs.getString("pm_is_first_notice"));
				printReminderLetter.setSecondNoticeType(rs.getString("pm_is_second_notice"));
				printReminderLetter.setThirdNoticeType(rs.getString("pm_is_third_notice"));
				if (printReminderLetter.getFirstNoticeType() == null
						|| printReminderLetter.getSecondNoticeType() == null
						|| printReminderLetter.getThirdNoticeType() == null) {

					printReminderLetter.setReminderType("");
				} else if (printReminderLetter.getFirstNoticeType().equals("Y")) {
					printReminderLetter.setReminderType("First Reminder");
					
				} else if (printReminderLetter.getSecondNoticeType().equals("Y")) {
					printReminderLetter.setReminderType("Second Reminder");
				} else if (printReminderLetter.getThirdNoticeType().equals("Y")) {
					printReminderLetter.setReminderType("Third Reminder");
				}

				printReminderLetter.setGenrateDate(rs.getDate("print_date"));
				showGridValues.add(printReminderLetter);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return showGridValues;

	}

	@Override
	public void updatePrinteStatus(String firstStatus, String secondStatus, String thirdStatus, String loginUser,
			String appNumber) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_reminder_letter SET  is_1st_notice_printed =?, is_2nd_notice_printed =?, is_3rd_notice_printed =?, modify_by=?, modify_date =? where app_no =?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, firstStatus);
			stmt.setString(2, secondStatus);
			stmt.setString(3, thirdStatus);
			stmt.setString(4, loginUser);
			stmt.setTimestamp(5, timestamp);
			stmt.setString(6, appNumber);

			stmt.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return;
	}

}
