package lk.informatics.ntc.model.service;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import lk.informatics.ntc.model.dto.ChangePWDTO;
import lk.informatics.ntc.model.dto.UserDTO;
import lk.informatics.ntc.model.dto.UserLoginHistoryDTO;
import lk.informatics.ntc.model.dto.UserRoleDTO;
import lk.informatics.ntc.model.exception.ApplicationException;
import lk.informatics.ntc.model.exception.UserNotFoundException;
import lk.informatics.ntc.view.util.AES;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.ntc.view.util.PasswordGenerator;
import lk.informatics.ntc.view.util.PropertyReader;
import lk.informatics.roc.utils.common.Utils;

public class UserService implements Serializable {

	private static final long serialVersionUID = 1L;

	public UserDTO findUser(String username) throws UsernameNotFoundException, UserNotFoundException {
		UserDTO tempUserDTO = null;

		if (username != null) {
			username = username.toUpperCase();
			tempUserDTO = findUserDTO(username);
		}

		UserDTO userDTO = new UserDTO();

		userDTO.setUsername(tempUserDTO.getUsername());
		userDTO.setPassword(tempUserDTO.getPassword());

		userDTO.setAccountNonExpired(tempUserDTO.isAccountNonExpired());
		userDTO.setAccountNonLocked(tempUserDTO.isAccountNonLocked());
		userDTO.setCredentialsNonExpired(tempUserDTO.isCredentialsNonExpired());
		userDTO.setEnabled(tempUserDTO.isEnabled());
		userDTO.setFunctionList(findFunctionByUserName(userDTO.getUsername()));

		Collection<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		GrantedAuthority grantedAuthority = null;

		grantedAuthority = new GrantedAuthority() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getAuthority() {
				return "ROLE_USER";
			}
		};
		authorities.add(grantedAuthority);

		userDTO.setAuthorities(authorities);
		userDTO.setEmailAddress(tempUserDTO.getEmailAddress());
		userDTO.setInitialPasswordChange(tempUserDTO.getInitialPasswordChange());

		return userDTO;
	}

	public UserDTO findUserDTO(String username) throws UserNotFoundException {

		UserDTO user = getUserDTO(username);

		if (user == null) {
			throw new UserNotFoundException();
		}
		return user;
	}

	public UserDTO getUserDTO(String username) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		UserDTO userDTO = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * From nt_m_employee	where upper(emp_user_id)= ?"; /*
																						 * "SELECT * From cm_internal_user	where upper(user_name) = ?"
																						 * ;
																						 */
			ps = con.prepareStatement(query);
			ps.setString(1, username.toUpperCase());
			rs = ps.executeQuery();
			while (rs.next()) {
				userDTO = new UserDTO();
				userDTO.setUsername(username);
				userDTO.setPassword(rs.getString("emp_password"));
				userDTO.setAccountNonExpired(true);
				userDTO.setAccountNonLocked(true);
				userDTO.setCredentialsNonExpired(true);
				userDTO.setStatus(rs.getString("emp_user_status"));
				userDTO.setEmailAddress(rs.getString("emp_email_add"));
				userDTO.setInitialPasswordChange(rs.getString("emp_is_initial"));

				String status = rs.getString("emp_user_status");
				if (status == "A" || status.equals("A")) {
					userDTO.setEnabled(true);
				} else {
					userDTO.setEnabled(false);
				}
				break;

			}
			return userDTO;
		} catch (Exception e) {

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return null;
	}

	public long insertLoginHistory(UserLoginHistoryDTO userLoginHistoryDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			long nextseq = 0;
			con = ConnectionManager.getConnection();
			String sql = "SELECT nextval('seq_user_login_history')";
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				nextseq = rs.getLong(1);
			}

			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);

			sql = "INSERT INTO CM_H_LOGIN_HISTORY(login_seq,login_user,login_date,machine_ip,login_type) VALUES (?,?,?,?,?)";

			stmt = con.prepareStatement(sql);
			stmt.setLong(1, nextseq);
			stmt.setString(2, userLoginHistoryDTO.getUserName());
			stmt.setTimestamp(3, new java.sql.Timestamp(new Date().getTime()));
			stmt.setString(4, userLoginHistoryDTO.getMachineIp());
			stmt.setString(5, "I");// I=internal
			stmt.executeUpdate();
			con.commit();
			return nextseq;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return 0;

	}

	public void updateLogoutHistory(UserLoginHistoryDTO userLoginHistoryDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = ConnectionManager.getConnection();
			String sql = "UPDATE SMS_USER_LOGIN_HISTORY SET LOGOUT_DATE=? WHERE LOGIN_SEQ=?";
			stmt = con.prepareStatement(sql);
			stmt.setTimestamp(1, new java.sql.Timestamp(new Date().getTime()));
			stmt.setLong(2, userLoginHistoryDTO.getLoginHisSeq());
			stmt.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

	}

	public static String getUserRoleFromUserName(String userName) {

		String userRole = null;
		Connection con;
		PreparedStatement ps = null;
		ResultSet rs = null;

		con = ConnectionManager.getConnection();
		try {

			String sql = "SELECT user_role FROM cm_r_user_role where user_name=?";

			ps = con.prepareStatement(sql);

			ps.setString(1, userName);

			rs = ps.executeQuery();

			while (rs.next()) {
				userRole = rs.getString("user_role");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return userRole;
	}

	// --New Modification--
	public Map<String, String> findFunctionByUserName(String userName) {
		Connection con = null;

		ResultSet rs = null;
		PreparedStatement ps = null;
		Map<String, String> rolesMap = new HashMap<String, String>();

		try {
			String sql = null;
			con = ConnectionManager.getConnection();

			sql = "select distinct c.fra_function_code "
					+ "from nt_t_granted_user_role b, nt_r_fun_role_activity c,nt_r_function d "
					+ "where b.gur_role_code = c.fra_role_code " + "and d.code = c.fra_function_code "
					+ "and b.gur_status = 'A' " + "and b.gur_user_id = ? ";
			ps = con.prepareStatement(sql);
			ps.setString(1, userName);

			rs = ps.executeQuery();

			while (rs.next()) {
				String function = rs.getString("fra_function_code");
				rolesMap.put(function, function);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);

		}
		return rolesMap;
	}

	public static String getFunctionsFromUserName(String userName) {

		String userFunction = null;
		Connection con;
		PreparedStatement ps = null;
		ResultSet rs = null;

		con = ConnectionManager.getConnection();
		try {

			String sql = "select distinct c.fra_function_code "
					+ "from nt_t_granted_user_role b, nt_r_fun_role_activity c,nt_r_function d "
					+ "where b.gur_role_code = c.fra_role_code " + "and d.code = c.fra_function_code "
					+ "and b.gur_status = 'A' " + "and b.gur_user_id = ? ";

			ps = con.prepareStatement(sql);

			ps.setString(1, userName);

			rs = ps.executeQuery();

			while (rs.next()) {
				userFunction = rs.getString("fra_function_code");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return userFunction;
	}

	public String generatePassword() {

		String generatedPassword;
		String randomPassword = PasswordGenerator.generateRandomPassword(8);

		String secretKey = null;
		try {
			secretKey = PropertyReader.getPropertyValue("password.secretKey");
		} catch (ApplicationException e) {

			e.printStackTrace();
		}

		generatedPassword = AES.encrypt(randomPassword, secretKey);
		String decryptedPassword = AES.decrypt(generatedPassword, secretKey);

		return generatedPassword + " " + decryptedPassword;
	}

	public boolean resetPassword(String userID, String email, String mobile) {

		boolean isReset = false;
		Connection con = null;
		PreparedStatement pss = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		String[] password = generatePassword().split(" ");

		String encryptPassword = password[0];
		String decryptedPassword = password[1];

		String mailMessage = "<h1>Reset Password </h1><br/>\r\n" + "<p>Dear User,</p>\r\n" + "<p>Your new password is "
				+ decryptedPassword + ". You can use this password for login.</p>\r\n" + "<br/><p>Thank you!</p>";

		try {
			con = ConnectionManager.getConnection();

			String query = "select  emp_email_add, emp_moblile_no, emp_user_id FROM" + " public.nt_m_employee"
					+ " where emp_email_add= ? AND emp_user_id= ? ;";

			pss = con.prepareStatement(query);
			pss.setString(1, email);
			pss.setString(2, userID);

			rs = pss.executeQuery();

			if (!rs.next()) {
				isReset = false;
			} else {
				String sql = "update public.nt_m_employee set emp_is_initial='Y', emp_password=?, emp_moblile_no=? "
						+ "where emp_user_id=? and emp_email_add= ? ;";

				pss = con.prepareStatement(sql);
				pss.setString(1, encryptPassword);
				pss.setString(2, mobile);
				pss.setString(3, userID);
				pss.setString(4, email);

				pss.executeUpdate();
				con.commit();

				int i = pss.executeUpdate();
				isReset = true;
				if (i > 0) {

					long seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_t_pending_emails");

					String sql3 = "INSERT INTO public.nt_t_pending_emails "
							+ "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? , ? ,? ) " + "ON CONFLICT (seq) "
							+ "DO UPDATE SET mail_message = ?, submited_date= ?, expiry_date= ?, status = ?, "
							+ "retry_count= ?, error_message= ?  where public.nt_t_pending_emails.id=? ;";

					pss = con.prepareStatement(sql3);
					pss.setString(1, userID);
					pss.setString(2, email);
					pss.setString(3, mailMessage);
					pss.setTimestamp(4, timestamp);
					pss.setTimestamp(5, null);
					pss.setString(6, "P");
					pss.setInt(7, 3);
					pss.setString(8, null);
					pss.setLong(9, seqNo);
					pss.setString(10, "Reset Password");
					pss.setString(11, "email");

					pss.setString(12, mailMessage);
					pss.setTimestamp(13, timestamp);
					pss.setTimestamp(14, null);
					pss.setString(15, "P");
					pss.setInt(16, 12);
					pss.setString(17, null);
					pss.setString(18, userID);

					pss.executeUpdate();

					long seqNoAlert = Utils.getNextValBySeqName(con, "seq_nt_t_pending_alerts");

					String sqlAlert = "INSERT INTO public.nt_t_pending_alerts "
							+ "(seq, receipient_mobileno, sms_message, submited_date, status,  message_subject, alert_type) "
							+ "VALUES(?, ?, ?, ?, ?, ?, ?)";

					pss = con.prepareStatement(sqlAlert);

					pss.setLong(1, seqNoAlert);
					pss.setString(2, mobile);

					String message = "Dear " + userID + ", Your password is successfully reset. Your new password is "
							+ decryptedPassword + ". Regards, System Administrator";

					pss.setString(3, message);
					pss.setTimestamp(4, timestamp);
					pss.setString(5, "P");
					pss.setString(6, "Password Reset Succesfully");
					pss.setString(7, "sms");
					pss.executeUpdate();
					con.commit();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(pss);
			ConnectionManager.close(con);
		}
		return isReset;

	}

	static String usernameCounter;

	public String generatePassword(String password) {
		String generatedPassword;

		String secretKey = null;
		try {
			secretKey = PropertyReader.getPropertyValue("password.secretKey");
		} catch (ApplicationException e) {

			e.printStackTrace();
		}

		generatedPassword = AES.encrypt(password, secretKey);

		return generatedPassword;

	}

	public boolean ChangePassword(String username) {
		String userName = ChangePWDTO.userName;
		String newPassword = ChangePWDTO.newPassword;
		String confirmPassword = ChangePWDTO.confirmNewPW;

		UserService us = new UserService();
		String encriptPass = us.generatePassword(newPassword);

		boolean empty = false;

		Connection con;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = null;

		con = ConnectionManager.getConnection();
		try {

			String sql2 = "select * from nt_m_employee\r\n" + "where emp_user_id = '" + userName + "';\r\n";

			if (newPassword.equals(confirmPassword)) {
				sql = sql2 + "\r\n" + "update nt_m_employee set emp_password ='" + encriptPass
						+ "' , emp_is_initial='N'\r\n" + "where emp_user_id = '" + userName + "'\r\n" + ";\r\n";

				empty = true;

			}
			ps = con.prepareStatement(sql);

			ps.execute();

			con.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			empty = false;

		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return empty;

	}

	public void deleteRole(String empnumber) {

		Connection con;
		PreparedStatement ps = null;
		ResultSet rs = null;

		con = ConnectionManager.getConnection();
		try {

			String sql2 = "DELETE FROM nt_temp_emp_roles WHERE temp_emp_no=? ";

			ps = con.prepareStatement(sql2);
			ps.setString(1, empnumber);
			ps.executeUpdate();

			con.commit();

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
	}

	public boolean assignRoleToEmployee(List<UserRoleDTO> roleList, String empNumber, String logingUser) {
		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		PreparedStatement stmt3 = null;
		ResultSet rs = null;
		boolean isAssigned = false;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			deleteRole(empNumber);
			con = ConnectionManager.getConnection();

			for (int i = 0; i < roleList.size(); i++) {

				String query2 = "SELECT " + "FROM public.nt_temp_emp_roles "
						+ "WHERE temp_emp_no=? and temp_role_code=? ";

				stmt = con.prepareStatement(query2);
				stmt.setString(1, empNumber);
				stmt.setString(2, roleList.get(i).getRoleCode());

				rs = stmt.executeQuery();
				//ConnectionManager.close(stmt);

				if (rs.next() == false) {

					long seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_temp_emp_ro");

					String query = "INSERT INTO public.nt_temp_emp_roles "
							+ "(seqno, temp_emp_no, temp_role_code, temp_created_by, temp_created_date, temp_modified_by, temp_modified_date, temp_status) "
							+ "VALUES(? , ?, ?, ?, ?, ?, ?, ?);";

					stmt3 = con.prepareStatement(query);

					stmt3.setLong(1, seqNo);
					stmt3.setString(2, empNumber);
					stmt3.setString(3, roleList.get(i).getRoleCode());
					stmt3.setString(4, logingUser);
					stmt3.setTimestamp(5, timestamp);
					stmt3.setString(6, null);
					stmt3.setTimestamp(7, null);
					stmt3.setString(8, "P");

					int ti = stmt3.executeUpdate();

					if (ti > 0) {
						String q = "update public.nt_m_employee set emp_req_account='y' where emp_no=? ;";

						stmt2 = con.prepareStatement(q);
						stmt2.setString(1, empNumber);
						stmt2.executeUpdate();

						isAssigned = true;
					} else {
						isAssigned = false;
					}

				} else {
					continue;
				}
			}

			con.commit();

		} catch (Exception ex) {

			ex.printStackTrace();
			return isAssigned;

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(stmt3);
			ConnectionManager.close(con);
		}

		return isAssigned;
	}

	public void updateCounterStatusForUser(String userId) {

		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_r_counter SET cou_status='I' , cou_serving_queueno=NULL , cou_assigned_userid=NULL where cou_assigned_userid=?";

			ps = con.prepareStatement(sql);
			if (userId != null && !userId.isEmpty() && !userId.trim().equalsIgnoreCase("")) {
				ps.setString(1, userId);
			} else {
				ps.setNull(1, Types.VARCHAR);
			}

			ps.executeUpdate();

			con.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

	}

	public String getOldPasswordForUsername(String username) {
		String userFunction = null;
		Connection con;
		PreparedStatement ps = null;
		ResultSet rs = null;

		con = ConnectionManager.getConnection();
		try {

			String sql = "SELECT emp_password,emp_user_id FROM public.nt_m_employee where emp_user_id='" + username
					+ "'";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				userFunction = rs.getString("emp_password");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return userFunction;

	}

	public int updateChangePassword(String newPassword, String username) {

		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_employee\r\n" + "set emp_password='" + newPassword + "'\r\n"
					+ "WHERE emp_user_id='" + username + "';";

			ps = con.prepareStatement(sql);

			ps.executeUpdate();

			con.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return 0;
	}

	public String getStatus(String username) {
		String status = null;
		Connection con;
		PreparedStatement ps = null;
		ResultSet rs = null;

		con = ConnectionManager.getConnection();
		try {

			String sql = "SELECT emp_user_status FROM public.nt_m_employee where emp_user_id='" + username + "'";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				status = rs.getString("emp_user_status");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return status;

	}

	public void insertLoginDetails(String username, UserDTO userDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_user_login_detail");

			String sql = "INSERT INTO public.nt_t_user_login_detail (uld_seq_no, uld_user_id, uld_login_time, uld_session_id) VALUES(?, ?, ?, ?);";
			ps = con.prepareStatement(sql);
			ps.setLong(1, seqNo);
			ps.setString(2, username);
			ps.setTimestamp(3, timestamp);
			ps.setString(4, userDTO.getSessionId());

			ps.executeUpdate();

			con.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
	}

	public void updateUserDetails(String loginUser, String currentSessionId) {
		Connection con = null;
		PreparedStatement ps = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_user_login_detail SET   uld_logout_time=? where uld_user_id=? and uld_session_id=?;";

			ps = con.prepareStatement(sql);
			ps.setTimestamp(1, timestamp);
			ps.setString(2, loginUser);
			ps.setString(3, currentSessionId);

			ps.executeUpdate();

			con.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
	}

	// --End Modification--
	public boolean userHaveMobile(String userId) {

		boolean ishave = false;
		Connection con = null;
		PreparedStatement pss = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select emp_moblile_no FROM" + " public.nt_m_employee"
					+ " where  emp_user_id= ? AND emp_user_status='A' ;";

			pss = con.prepareStatement(query);

			pss.setString(1, userId);

			rs = pss.executeQuery();

			while (rs.next()) {
				if (rs.getString("emp_moblile_no") != null && !rs.getString("emp_moblile_no").isEmpty()) {
					ishave = true;
				} else {
					ishave = false;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(pss);
			ConnectionManager.close(con);
		}
		return ishave;

	}

	public boolean validMobileNoFOrUser(String userId, String mobileNo) {

		boolean isValid = false;
		Connection con = null;
		PreparedStatement pss = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select emp_moblile_no FROM" + " public.nt_m_employee"
					+ " where  emp_user_id= ? AND emp_moblile_no = ? AND emp_user_status='A';";

			pss = con.prepareStatement(query);

			pss.setString(1, userId);

			pss.setString(2, mobileNo);

			rs = pss.executeQuery();
			while (rs.next()) {
				if (rs.getString("emp_moblile_no") != null && !rs.getString("emp_moblile_no").isEmpty()) {
					isValid = true;
				} else {
					isValid = false;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(pss);
			ConnectionManager.close(con);
		}
		return isValid;

	}
	
	public void updateLoginAttempts(String loginUser, int count) {
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_employee SET emp_login_attempts=? where emp_user_id=?";

			ps = con.prepareStatement(sql);
			ps.setInt(1, count);
			ps.setString(2, loginUser);

			ps.executeUpdate();

			ConnectionManager.commit(con);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
	}
	
	public int getFaildLoginAttempts(String userId) {

		int count = 0;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select emp_login_attempts FROM public.nt_m_employee where emp_user_id=? AND emp_user_status='A' ;";
			
			ps = con.prepareStatement(query);
			ps.setString(1, userId);
			rs = ps.executeQuery();

			while (rs.next()) {
				count= rs.getInt("emp_login_attempts");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return count;

	}
	
	public void blockUser(String loginUser) {
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_employee SET emp_user_status=? where emp_user_id=? AND emp_user_status='A' ";

			ps = con.prepareStatement(sql);
			ps.setString(1, "B");
			ps.setString(2, loginUser);
			ps.executeUpdate();
			
			saveBlockedUserDetails(loginUser, con);

			ConnectionManager.commit(con);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
	}
	
	private void saveBlockedUserDetails(String username, Connection con) {
		PreparedStatement ps = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_user_attempts_block_detail");

			String sql = "INSERT INTO public.nt_user_attempts_block_detail "
					+ "(seq, user_id, blocked_date) VALUES(?,?,?);";
			ps = con.prepareStatement(sql);
			ps.setLong(1, seqNo);
			ps.setString(2, username);
			ps.setTimestamp(3, timestamp);
			ps.executeUpdate();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
		}
	}
	
}