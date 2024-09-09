package lk.informatics.ntc.model.service;

import java.net.*;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.informatics.ntc.model.dto.ManageUserDTO;
import lk.informatics.ntc.model.exception.ApplicationException;
import lk.informatics.ntc.view.util.AES;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.ntc.view.util.PasswordGenerator;
import lk.informatics.ntc.view.util.PropertyReader;
import lk.informatics.roc.utils.common.Utils;

public class ManageUserImpl implements ManageUserService {

	private static final long serialVersionUID = 1L;

	// Get Roles
	public List<ManageUserDTO> GetRolesToDropdown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<ManageUserDTO> returnList = new ArrayList<ManageUserDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description " + "FROM nt_r_role " + "WHERE active = 'A' "
					+ "ORDER BY description ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				ManageUserDTO manageDTO = new ManageUserDTO();
				manageDTO.setRoleCode(rs.getString("code"));
				manageDTO.setRoleName(rs.getString("description"));

				returnList.add(manageDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return returnList;

	}

	// Get status
	public List<ManageUserDTO> GetStatusToDropdown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<ManageUserDTO> returnList = new ArrayList<ManageUserDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description " + "FROM nt_r_role_status " + "WHERE active = 'A' "
					+ "ORDER BY description ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				ManageUserDTO manageUserDTO = new ManageUserDTO();
				manageUserDTO.setRoleCode(rs.getString("code"));
				manageUserDTO.setRoleName(rs.getString("description"));

				returnList.add(manageUserDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return returnList;

	}

	@Override
	public List<ManageUserDTO> getDeptToDropdown() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<ManageUserDTO> returnList = new ArrayList<ManageUserDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description " + "FROM nt_r_department " + "WHERE active = 'A' "
					+ "ORDER BY description ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				ManageUserDTO manageUserDTO = new ManageUserDTO();
				manageUserDTO.setDeptCode(rs.getString("code"));
				manageUserDTO.setDeptDesc(rs.getString("description"));
				returnList.add(manageUserDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return returnList;
	}

	@Override
	public List<ManageUserDTO> getUsers() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<ManageUserDTO> returnList = new ArrayList<ManageUserDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT DISTINCT A.emp_no AS emp_no," + " A.emp_fullname AS emp_fullname,"
					+ " A.emp_nic_no AS emp_nic_no," + " A.emp_epf_no AS emp_epf_no," + " A.emp_user_id AS emp_user_id,"
					+ " CASE WHEN A.emp_status = 'A' THEN 'ACTIVE' WHEN A.emp_status = 'P' THEN 'PENDING' WHEN A.emp_status = 'R' THEN 'REJECT'  WHEN A.emp_status = 'I' THEN 'INACTIVE' ELSE A.emp_status END AS emp_status,"
					+ " CASE WHEN A.emp_user_status = 'A' THEN 'ACTIVE' WHEN A.emp_user_status = 'P' THEN 'PENDING' WHEN A.emp_user_status = 'R' THEN 'REJECT'  WHEN A.emp_user_status = 'I' THEN 'INACTIVE' ELSE A.emp_user_status END AS emp_user_status,"
					+ " B.dep_dept_code AS dep_dept_code" + " FROM nt_m_employee A"
					+ " INNER JOIN nt_m_emp_department B ON A.emp_no=B.dep_emp_no"
					+ " WHERE A.emp_req_account ='y' AND A.emp_status ='A' AND A.emp_user_status='P' AND  B.dep_active ='A'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				ManageUserDTO manageUserDTO = new ManageUserDTO();
				manageUserDTO.setEmpNo(rs.getString("emp_no"));
				manageUserDTO.setName(rs.getString("emp_fullname"));
				manageUserDTO.setDeptCode(rs.getString("dep_dept_code"));
				manageUserDTO.setNicNo(rs.getString("emp_nic_no"));
				manageUserDTO.setEpfNo(rs.getString("emp_epf_no"));
				manageUserDTO.setUserId(rs.getString("emp_user_id"));
				manageUserDTO.setEmpStatus(rs.getString("emp_status"));
				manageUserDTO.setUserStatus(rs.getString("emp_user_status"));
				returnList.add(manageUserDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return returnList;
	}

	public void rejectUser(String selectedUserRadio, String rejectReason) {
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "UPDATE nt_m_employee" + " SET emp_reject_reason = ? , emp_user_status = ?"
					+ " WHERE emp_no = ?";

			ps = con.prepareStatement(query);
			ps.setString(1, rejectReason);
			ps.setString(2, "R");
			ps.setString(3, selectedUserRadio);
			ps.executeUpdate();

			con.commit();

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

	}

	@Override
	public List<ManageUserDTO> saveEditStatus(String selectedUserStatus, String selectedInactRe) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "INSERT INTO public.nt_h_user_status_log (esl_old_status, esl_inactive_reason) VALUES(?, ?);";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, selectedUserStatus);
			stmt.setString(2, selectedInactRe);

			stmt.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return null;
	}

	@Override

	public List<ManageUserDTO> saveAsssignUserRoles(String strSelectedRoleCode, String startDate, String endDate,
			String strSelectedStatus, String userId) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String startDateVal = (dateFormat.format(startDate));
		String endDateVal = (dateFormat.format(endDate));
		try {
			con = ConnectionManager.getConnection();
			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_granted_user_role");

			String sql = "INSERT INTO public.nt_t_granted_user_role(seq, gur_role_code, gur_start_date, gur_end_date, gur_user_id, gur_user_name, gur_status, gur_created_by, gur_created_date)VALUES(?, ?, ?, ?,?,?, ?,null,null);";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, strSelectedRoleCode);
			stmt.setDate(3, java.sql.Date.valueOf(startDateVal));
			stmt.setDate(4, java.sql.Date.valueOf(endDateVal));
			stmt.setString(5, userId);
			stmt.setString(6, userId);
			stmt.setString(7, strSelectedStatus);
			stmt.executeUpdate();
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return null;
	}

	@Override
	public List<ManageUserDTO> getSelectUser(String strSelectedRoleCode, String startDate, String endDate,
			String strSelectedStatus) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		List<ManageUserDTO> returnList = new ArrayList<ManageUserDTO>();

		try {
			
			con = ConnectionManager.getConnection();

			String query = "select a.gur_role_code, a.gur_status, a.gur_start_date, "
					+ "a.gur_end_date from public.nt_t_granted_user_role a "
					+ "inner join public.nt_r_role b on a.gur_role_code = b.code "
					+ "where b.code=? or a.gur_status=? or a.gur_start_date=? or  a.gur_end_date=? ;";

			ps = con.prepareStatement(query);

			ps.setString(1, strSelectedRoleCode);
			ps.setString(2, strSelectedStatus);
			ps.setDate(3, java.sql.Date.valueOf("2013-09-04"));
			ps.setDate(4, java.sql.Date.valueOf("2013-09-06"));

			rs = ps.executeQuery();

			while (rs.next()) {
				ManageUserDTO manageDTO = new ManageUserDTO();
				manageDTO.setRoleCode(rs.getString("gur_role_code"));
				manageDTO.setUserStatus(rs.getString("gur_status"));
				manageDTO.setStartDate(rs.getString(""));
				manageDTO.setEndDate(rs.getString(""));

				returnList.add(manageDTO);

			}

			
		} catch (Exception e) {
			
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return returnList;

	}

	@Override
	public List<ManageUserDTO> getUserToDropdown() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<ManageUserDTO> returnList = new ArrayList<ManageUserDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT emp_user_id" + " FROM nt_m_employee"
					+ " WHERE emp_req_account ='y' AND emp_status ='A' AND emp_user_id IS NOT NULL ORDER BY emp_user_id";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				ManageUserDTO manageUserDTO = new ManageUserDTO();
				manageUserDTO.setUserId(rs.getString("emp_user_id"));
				returnList.add(manageUserDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return returnList;
	}

	@Override
	public List<ManageUserDTO> getEpfNoToDropdown() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<ManageUserDTO> returnList = new ArrayList<ManageUserDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT emp_epf_no FROM nt_m_employee WHERE emp_req_account ='y' AND emp_status ='A' AND emp_epf_no IS NOT NULL ORDER BY emp_epf_no";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				ManageUserDTO manageUserDTO = new ManageUserDTO();
				manageUserDTO.setEpfNo(rs.getString("emp_epf_no"));

				returnList.add(manageUserDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return returnList;
	}

	public List<ManageUserDTO> searchUser(ManageUserDTO manageUserDTO) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<ManageUserDTO> userList = new ArrayList<ManageUserDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT DISTINCT A.emp_no AS emp_no," + " A.emp_fullname AS emp_fullname,"
					+ " A.emp_nic_no AS emp_nic_no," + " A.emp_epf_no AS emp_epf_no," + " A.emp_user_id AS emp_user_id,"
					+ " CASE WHEN A.emp_status = 'A' THEN 'ACTIVE' WHEN A.emp_status = 'P' THEN 'PENDING' WHEN A.emp_status = 'R' THEN 'REJECT'  WHEN A.emp_status = 'I' THEN 'INACTIVE' ELSE A.emp_status END AS emp_status,"
					+ " CASE WHEN A.emp_user_status = 'A' THEN 'ACTIVE' WHEN A.emp_user_status = 'P' THEN 'PENDING' WHEN A.emp_user_status = 'R' THEN 'REJECT'  WHEN A.emp_user_status = 'B' THEN 'BLOCK' WHEN A.emp_user_status = 'I' THEN 'INACTIVE' ELSE A.emp_user_status  END AS emp_user_status,"
					+ " B.dep_dept_code AS dep_dept_code" + " FROM nt_m_employee A"
					+ " INNER JOIN nt_m_emp_department B ON A.emp_no=B.dep_emp_no WHERE B.dep_active ='A' AND A.emp_req_account ='y' AND A.emp_status ='A'";

			boolean whereadded = false;
			if (manageUserDTO.getEpfNo() != null && !manageUserDTO.getEpfNo().equals("")) {
				query = query + " AND A.emp_epf_no='" + manageUserDTO.getEpfNo() + "'";
				whereadded = true;
			}
			if (manageUserDTO.getNicNo() != null && !manageUserDTO.getNicNo().equals("")) {
				if (whereadded) {
					query = query + " AND A.emp_nic_no='" + manageUserDTO.getNicNo() + "'";
				} else {
					query = query + " AND A.emp_nic_no='" + manageUserDTO.getNicNo() + "'";
					whereadded = true;
				}
			}

			if (manageUserDTO.getUserId() != null && !manageUserDTO.getUserId().equals("")) {
				if (whereadded) {
					query = query + " AND A.emp_user_id='" + manageUserDTO.getUserId() + "'";
				} else {
					query = query + " AND A.emp_user_id='" + manageUserDTO.getUserId() + "'";
					whereadded = true;
				}
			}

			if (manageUserDTO.getDeptCode() != null && !manageUserDTO.getDeptCode().equals("")) {
				if (whereadded) {
					query = query + " AND B.dep_dept_code='" + manageUserDTO.getDeptCode() + "'";
				} else {
					query = query + " AND B.dep_dept_code='" + manageUserDTO.getDeptCode() + "'";
					whereadded = true;
				}
			}

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				ManageUserDTO manageUserDTO1 = new ManageUserDTO();
				manageUserDTO1.setEmpNo(rs.getString("emp_no"));
				manageUserDTO1.setName(rs.getString("emp_fullname"));
				manageUserDTO1.setDeptCode(rs.getString("dep_dept_code"));
				manageUserDTO1.setNicNo(rs.getString("emp_nic_no"));
				manageUserDTO1.setEpfNo(rs.getString("emp_epf_no"));
				manageUserDTO1.setUserId(rs.getString("emp_user_id"));
				manageUserDTO1.setEmpStatus(rs.getString("emp_status"));
				manageUserDTO1.setUserStatus(rs.getString("emp_user_status"));
				userList.add(manageUserDTO1);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return userList;

	}

	@Override
	public String generateUser(String empNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String firstName = null;
		String lastName = null;

		String userId = null;

		String userPassword = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT *" + " FROM nt_m_employee" + " WHERE emp_no =?";

			ps = con.prepareStatement(query);
			ps.setString(1, empNo);
			rs = ps.executeQuery();

			while (rs.next()) {

				firstName = rs.getString("emp_first_name");
				lastName = rs.getString("emp_surname");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		userId = this.generateUserId(firstName, lastName);
		userPassword = this.generatePassword();

		this.updateUser(userId, userPassword, empNo);

		this.grantUserRole(userId, empNo);

		this.sendEmail(userId, userPassword, empNo);

		userPassword = this.decryptPassword(userPassword);

		String usernamePassword = "User ID : " + userId + " / Password : " + userPassword;

		return usernamePassword;
	}

	private void grantUserRole(String userId, String empNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String roleCode;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT temp_role_code" + " FROM nt_temp_emp_roles"
					+ " WHERE temp_emp_no =? AND temp_status='P'";

			ps = con.prepareStatement(query);
			ps.setString(1, empNo);
			rs = ps.executeQuery();

			while (rs.next()) {

				roleCode = rs.getString("temp_role_code");
				if (roleCode != null) {
					this.saveGrantedUserRoles(roleCode, userId);
					this.activateGrantedUserRoles(roleCode, empNo);
				}

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}


	}

	public void saveGrantedUserRoles(String roleCode, String userId) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		Date todaysDate = new Date();

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String date = (dateFormat.format(todaysDate));

		try {
			con = ConnectionManager.getConnection();
			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_granted_user_role");

			String sql = "INSERT INTO nt_t_granted_user_role(seq, gur_role_code, gur_start_date,  gur_user_id,  gur_status,gur_user_name)VALUES(?, ?, ?, ?,?,?);";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, roleCode);
			stmt.setString(3, date);
			stmt.setString(4, userId);
			stmt.setString(5, "A");
			stmt.setString(6, userId);
			stmt.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

	}

	public void activateGrantedUserRoles(String roleCode, String empNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE nt_temp_emp_roles SET temp_status='A' WHERE temp_emp_no = ? AND temp_role_code = ?;";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, empNo);
			stmt.setString(2, roleCode);

			stmt.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);

		}

	}

	public String generateUserId(String firstName, String lastName) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String userId = null;
		char lastLetter;
		int totalUserId = 0;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT COUNT(emp_no) AS total_user_id" + " FROM nt_m_employee"
					+ " WHERE emp_first_name =? AND emp_surname = ?  AND  emp_req_account = 'y'"; // AND emp_user_id IS
																									// NOT NULL";

			ps = con.prepareStatement(query);
			ps.setString(1, firstName);
			ps.setString(2, lastName);
			rs = ps.executeQuery();

			while (rs.next()) {

				totalUserId = rs.getInt("total_user_id");

				totalUserId = totalUserId - 1;
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		if (totalUserId <= 0) {

			if (firstName.length() <= 8) {
				lastLetter = lastName.charAt(0);
				userId = firstName + "." + lastLetter;
			} else {
				lastLetter = lastName.charAt(0);
				firstName = firstName.substring(0, 8);
				userId = firstName + "." + lastLetter;
			}

		} else {

			if (firstName.length() <= 8) {
				lastLetter = lastName.charAt(0);

				if (totalUserId < 10) {
					userId = firstName + "." + lastLetter + "0" + totalUserId;
				} else {
					userId = firstName + "." + lastLetter + totalUserId;
				}
			} else {
				lastLetter = lastName.charAt(0);
				firstName = firstName.substring(0, 8);
				if (totalUserId < 10) {
					userId = firstName + "." + lastLetter + "0" + totalUserId;
				} else {
					userId = firstName + "." + lastLetter + totalUserId;
				}
			}

		}

		return userId;
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

		return generatedPassword;
	}

	public String decryptPassword(String generatedPassword) {

		String secretKey = null;
		try {
			secretKey = PropertyReader.getPropertyValue("password.secretKey");
		} catch (ApplicationException e) {

			e.printStackTrace();
		}

		String decryptedPassword = AES.decrypt(generatedPassword, secretKey);

		return decryptedPassword;

	}

	public void updateUser(String userId, String userPassword, String empNo) {
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "UPDATE nt_m_employee"
					+ " SET emp_user_id = ? , emp_password = ? , emp_user_status = ?, emp_is_initial = ?"
					+ " WHERE emp_no = ?";

			ps = con.prepareStatement(query);
			ps.setString(1, userId);
			ps.setString(2, userPassword);
			ps.setString(3, "A");
			ps.setString(4, "Y");
			ps.setString(5, empNo);
			ps.executeUpdate();

			con.commit();

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

	}

	public void sendEmail(String userId, String userPassword, String empNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		String message = null;
		String messageN = null;

		String empTitle = null;
		String empSurname = null;
		String empEmail = null;
		String empMobile = null;
		String sql = null;
		String sql2 = null;

		userPassword = this.decryptPassword(userPassword);

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT A.emp_moblile_no, A.emp_surname,A.emp_email_add,B.description AS emp_title"
					+ " FROM nt_m_employee A INNER JOIN nt_r_title B ON A.emp_title = B.code" + " WHERE A.emp_no =?";

			stmt = con.prepareStatement(query);
			stmt.setString(1, empNo);
			rs = stmt.executeQuery();

			while (rs.next()) {
				empTitle = rs.getString("emp_title");
				empSurname = rs.getString("emp_surname");
				empEmail = rs.getString("emp_email_add");
				empMobile = rs.getString("emp_moblile_no");
			}

			message = "<p>Dear " + empTitle + "." + empSurname
					+ ",</p><p>Your account is successfully created on NTC.</p><p>Your User ID is : " + userId
					+ "</p><p>Your Password is : " + userPassword + "</p><p>Regards,</p><p> Administrator</p>";

			String emailSubject = "Login Credentials";

			sql = "INSERT INTO nt_t_pending_emails(seq,alert_type,mail_message,submited_date,receipient_email,status,message_subject,id) VALUES (?,?,?,?,?,?,?,?)";

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_pending_emails");

			ps = con.prepareStatement(sql);
			ps.setLong(1, seqNo);
			ps.setString(2, "email");
			ps.setString(3, message);
			ps.setTimestamp(4, timestamp);
			ps.setString(5, empEmail);
			ps.setString(6, "P");
			ps.setString(7, emailSubject);
			ps.setString(8, userId);

			ps.executeUpdate();

			messageN = "Dear " + empTitle + "." + empSurname
					+ ", Your account is successfully created on NTC. Your User ID is : " + userId
					+ " , Your Password is : " + userPassword + " , Regards, Administrator";

			sql2 = "INSERT INTO nt_t_pending_alerts(seq,alert_type,sms_message,submited_date,receipient_mobileno,status,message_subject) VALUES (?,?,?,?,?,?,?)";

			long seqNo2 = Utils.getNextValBySeqName(con, "seq_nt_t_pending_alerts");

			ps2 = con.prepareStatement(sql2);
			ps2.setLong(1, seqNo2);
			ps2.setString(2, "sms");
			ps2.setString(3, messageN);
			ps2.setTimestamp(4, timestamp);
			ps2.setString(5, empMobile);
			ps2.setString(6, "P");
			ps2.setString(7, emailSubject);
			ps2.executeUpdate();

			con.commit();

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(stmt);
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(con);
		}

	}

	public void bulkSMS(String empMobile, String message) {
		try {
			// Construct data
			String data = "";
			empMobile = "+94" + empMobile.substring(1);

			data += "username=" + URLEncoder.encode("informatics", "ISO-8859-1");
			data += "&password=" + URLEncoder.encode("macr0m0b1le", "ISO-8859-1");
			data += "&message=" + URLEncoder.encode(message, "ISO-8859-1");
			data += "&msisdn=" + empMobile;

			// Send data
			// Please see the FAQ regarding HTTPS (port 443) and HTTP (port 80/5567)
			URL url = new URL("http://bulksms.vsms.net:5567/eapi/submission/send_sms/2/2.0");


			URLConnection con = url.openConnection();
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setConnectTimeout(1000000000);
			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			wr.write(data);
			wr.flush();

			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));

			wr.close();
			rd.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<ManageUserDTO> viewDetails(String userId) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		List<ManageUserDTO> returnDetails = new ArrayList<ManageUserDTO>();

		try {
			
			con = ConnectionManager.getConnection();

			String query = "SELECT gur_seq,gur_role_code, gur_start_date,gur_end_date,"
					+ " gur_status FROM public.nt_t_granted_user_role" + " WHERE gur_user_id=?";

			ps = con.prepareStatement(query);

			ps.setString(1, userId);

			rs = ps.executeQuery();

			while (rs.next()) {
				ManageUserDTO manageDTO = new ManageUserDTO();
				manageDTO.setRoleCode(rs.getString("gur_role_code"));
				manageDTO.setSeq(rs.getLong("gur_seq"));
				manageDTO.setStartDate(rs.getString("gur_start_date"));
				manageDTO.setEndDate(rs.getString("gur_end_date"));
				manageDTO.setUserStatus(rs.getString("gur_status"));
				if (rs.getString("gur_status").equals("A")) {
					
					manageDTO.setTableStatus("Active");
				} else if (rs.getString("gur_status").equals("I")) {
					
					manageDTO.setTableStatus("Inactive");
				} else if (rs.getString("gur_status").equals("T")) {
					
					manageDTO.setTableStatus("Temporary");
				}
				returnDetails.add(manageDTO);

			}

			
		} catch (Exception e) {
			
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return returnDetails;

	}

	@Override
	public List<ManageUserDTO> showFuncDetails(String roleCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		

		List<ManageUserDTO> returnFuncDetails = new ArrayList<ManageUserDTO>();

		try {
			
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct a.code,a.description" + " FROM public.nt_r_function a "
					+ "inner join public.nt_r_fun_role_activity b on " + "a.code = b.fra_function_code"
					+ " WHERE  b.fra_role_code =?" + "order by a.code";

			ps = con.prepareStatement(query);

			ps.setString(1, roleCode);

			rs = ps.executeQuery();

			while (rs.next()) {
				ManageUserDTO manageDTO = new ManageUserDTO();
				manageDTO.setCode(rs.getString("code"));

				manageDTO.setDescription(rs.getString("description"));

				returnFuncDetails.add(manageDTO);

			}

			
		} catch (Exception e) {
			
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return returnFuncDetails;

	}

	@Override
	public List<ManageUserDTO> viewUserRole(String selectedUserId) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<ManageUserDTO> returnList = new ArrayList<ManageUserDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT A.gur_role_code AS code, B.description AS description"
					+ " FROM nt_t_granted_user_role A INNER JOIN nt_r_role B ON A.gur_role_code=B.code INNER JOIN nt_m_employee C ON A.gur_user_id=C.emp_user_id"
					+ " WHERE C.emp_no = ? AND A.gur_status in('A' ,'T')" + " ORDER BY A.gur_role_code";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedUserId);
			rs = ps.executeQuery();

			while (rs.next()) {
				ManageUserDTO manageUserDTO = new ManageUserDTO();
				manageUserDTO.setRoleCode(rs.getString("code"));
				manageUserDTO.setRoleName(rs.getString("description"));

				returnList.add(manageUserDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return returnList;
	}

	@Override
	public List<ManageUserDTO> viewUserRoleFromTemp(String selectedUserId) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<ManageUserDTO> returnList = new ArrayList<ManageUserDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT A.temp_role_code AS code, B.description AS description"
					+ " FROM nt_temp_emp_roles A INNER JOIN nt_r_role B ON A.temp_role_code=B.code INNER JOIN nt_m_employee C ON A.temp_emp_no=C.emp_no"
					+ " WHERE C.emp_no = ? AND A.temp_status='P'" + " ORDER BY A.temp_role_code";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedUserId);
			rs = ps.executeQuery();

			while (rs.next()) {
				ManageUserDTO manageUserDTO = new ManageUserDTO();
				manageUserDTO.setRoleCode(rs.getString("code"));
				manageUserDTO.setRoleName(rs.getString("description"));

				returnList.add(manageUserDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return returnList;
	}

	@Override
	public ManageUserDTO showEditDetailsWithEditBtn(String roleCode, String userId) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		// List<ManageUserDTO> returnEditUserDetails = new ArrayList<ManageUserDTO>();
		ManageUserDTO manageUserDTO = new ManageUserDTO();
		Date start;
		Date end;
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT gur_start_date,gur_end_date,gur_status,gur_role_code,seq  FROM public.nt_t_granted_user_role  WHERE gur_role_code =? and gur_user_id=? order by gur_role_code";

			ps = con.prepareStatement(query);

			ps.setString(1, roleCode);
			ps.setString(2, userId);
			rs = ps.executeQuery();

			while (rs.next()) {
				
				start = new SimpleDateFormat("dd/MM/yyyy").parse(rs.getString("gur_start_date"));
				manageUserDTO.setStartDateVal(start);
				String end1 = rs.getString("gur_end_date");
				if (end1 != null) {
					end = new SimpleDateFormat("dd/MM/yyyy").parse(rs.getString("gur_end_date"));
					manageUserDTO.setEndDateVal(end);
				}

				manageUserDTO.setSeq(rs.getLong("seq"));
				manageUserDTO.setUserStatus(rs.getString("gur_status"));
				manageUserDTO.setRoleCode(rs.getString("gur_role_code"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return manageUserDTO;

	}

	@Override
	public void updateUserStatus(String userId, String userStatus, String userOldStatus, String inactiveReason) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String sql2 = "UPDATE nt_m_employee SET emp_user_status = ? WHERE emp_user_id = ?";

			stmt = con.prepareStatement(sql2);

			stmt.setString(1, userStatus);
			stmt.setString(2, userId);

			stmt.executeUpdate();
			ConnectionManager.close(stmt);
			con.commit();

			String sql = "INSERT INTO nt_h_user_status_log (esl_old_status, esl_inactive_reason,esl_user_id) VALUES(?, ?,?);";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, userOldStatus);
			stmt.setString(2, inactiveReason);
			stmt.setString(3, userId);

			stmt.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

	}

	@Override
	public int insertUserRoleAct(ManageUserDTO manageUserDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String startDateVal = dateFormat.format(manageUserDTO.getStartDateVal());
		String endDateVal = (dateFormat.format(manageUserDTO.getEndDateVal()));

		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();
			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_granted_user_role");
			String sql = "INSERT INTO public.nt_t_granted_user_role(seq, gur_role_code, gur_start_date, gur_end_date, gur_user_id, gur_user_name, gur_status, gur_created_by, gur_created_date)VALUES(?, ?, ?, ?,?,?, ?,?,?);";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, manageUserDTO.getRoleCode());
			stmt.setString(3, startDateVal);
			stmt.setString(4, endDateVal);
			stmt.setString(5, manageUserDTO.getUserId());
			stmt.setString(6, manageUserDTO.getUserId());
			stmt.setString(7, manageUserDTO.getEmpStatus());
			stmt.setString(8, manageUserDTO.getCreatedBy());
			
			stmt.setTimestamp(9, timestamp);

			stmt.executeUpdate();
			
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);

		}
		return 0;
	}

	@Override
	public ManageUserDTO getCurrentRoleName(String strSelectedRoleCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		ManageUserDTO manageUserDTO = new ManageUserDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT description FROM public.nt_r_role where code=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, strSelectedRoleCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				manageUserDTO.setRoleName(rs.getString("description"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return manageUserDTO;
	}

	@Override
	public int updateGrantedUserRole(ManageUserDTO manageUserDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Timestamp timestamp = new Timestamp(date.getTime());
		// String startDateVal = dateFormat.format(manageUserDTO.getStartDateVal());
		String endDateVal = (dateFormat.format(manageUserDTO.getEndDateVal()));

		try {
			con = ConnectionManager.getConnection();
			String sql = "UPDATE public.nt_t_granted_user_role SET gur_end_date=?, gur_status=?, gur_modified_by=?, gur_modified_date=? WHERE seq=?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, endDateVal);
			stmt.setString(2, manageUserDTO.getEmpStatus());
			stmt.setString(3, manageUserDTO.getModifyBy());
			stmt.setTimestamp(4, timestamp);
			stmt.setLong(5, manageUserDTO.getSeq());

			stmt.executeUpdate();
			
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return 0;
	}

	@Override
	public List<ManageUserDTO> getFuncRoleActivity(String roleCode, String functionCode) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<ManageUserDTO> activityList = new ArrayList<ManageUserDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT fra_activity_code " + "FROM nt_r_fun_role_activity " + "WHERE fra_role_code = ? "
					+ "AND fra_function_code = ? ";

			ps = con.prepareStatement(query);
			ps.setString(1, roleCode);
			ps.setString(2, functionCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				ManageUserDTO manageUserDTO = new ManageUserDTO();

				manageUserDTO.setActivityCode(rs.getString("fra_activity_code"));

				activityList.add(manageUserDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return activityList;
	}
	
	@Override
	public List<ManageUserDTO> getUserToDropdownUnblockUser() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<ManageUserDTO> returnList = new ArrayList<ManageUserDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT emp_user_id FROM public.nt_m_employee"
					+ " WHERE emp_user_status ='B' AND emp_user_id IS NOT NULL ORDER BY emp_user_id";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				ManageUserDTO manageUserDTO = new ManageUserDTO();
				manageUserDTO.setUserId(rs.getString("emp_user_id"));
				returnList.add(manageUserDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return returnList;
	}
	
	@Override
	public List<ManageUserDTO> getBlockedUsers() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<ManageUserDTO> returnList = new ArrayList<ManageUserDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT a.emp_user_id,a.emp_no, a.emp_epf_no, a.emp_fullname, a.emp_login_attempts, b.blocked_date "
					+ "FROM public.nt_m_employee a "
					+ "inner join nt_user_attempts_block_detail b on a.emp_user_id = b.user_id "
					+ "WHERE a.emp_user_status='B' and b.activated_by is null; ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				ManageUserDTO manageUserDTO = new ManageUserDTO();
				manageUserDTO.setEmpNo(rs.getString("emp_no"));
				manageUserDTO.setEpfNo(rs.getString("emp_epf_no"));
				manageUserDTO.setUserId(rs.getString("emp_user_id"));
				manageUserDTO.setName(rs.getString("emp_fullname"));
				manageUserDTO.setLoginAttempts(rs.getInt("emp_login_attempts"));
				if(rs.getTimestamp("blocked_date")!=null) {
					String pattern = "dd/MM/yyyy HH:mm";
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
					String date = simpleDateFormat.format(rs.getTimestamp("blocked_date"));
					manageUserDTO.setBlockedDate(date);
				}
				returnList.add(manageUserDTO);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return returnList;
	}
	
	@Override
	public List<ManageUserDTO> searchBlockedUsers(ManageUserDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<ManageUserDTO> returnList = new ArrayList<ManageUserDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT a.emp_no, a.emp_epf_no, a.emp_user_id, a.emp_fullname, a.emp_login_attempts, b.blocked_date "
					+ "FROM public.nt_m_employee a "
					+ "inner join nt_user_attempts_block_detail b on a.emp_user_id = b.user_id "
					+ "WHERE a.emp_user_status='B' and b.activated_by is null and a.emp_user_id=?; ";

			ps = con.prepareStatement(query);
			ps.setString(1, dto.getUserId());
			rs = ps.executeQuery();

			while (rs.next()) {

				ManageUserDTO manageUserDTO = new ManageUserDTO();
				manageUserDTO.setEmpNo(rs.getString("emp_no"));
				manageUserDTO.setEpfNo(rs.getString("emp_epf_no"));
				manageUserDTO.setUserId(rs.getString("emp_user_id"));
				manageUserDTO.setName(rs.getString("emp_fullname"));
				manageUserDTO.setLoginAttempts(rs.getInt("emp_login_attempts"));
				if(rs.getTimestamp("blocked_date")!=null) {
					String pattern = "dd/MM/yyyy HH:mm";
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
					String date = simpleDateFormat.format(rs.getTimestamp("blocked_date"));
					manageUserDTO.setBlockedDate(date);
				}
				returnList.add(manageUserDTO);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return returnList;
	}
	
	@Override
	public boolean unblockUser(ManageUserDTO blockedUser, String loginUser) {
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionManager.getConnection();
			
			String newUserPassword = this.generatePassword();

			String sql = "UPDATE public.nt_m_employee SET emp_user_status=?, emp_login_attempts=?, emp_password = ? , emp_is_initial = ? where emp_user_id=? AND emp_user_status='B' ";

			ps = con.prepareStatement(sql);
			ps.setString(1, "A");
			ps.setInt(2, 0);
			ps.setString(3, newUserPassword);
			ps.setString(4, "Y");
			ps.setString(5, blockedUser.getUserId());
			ps.executeUpdate();

			updateUnblockedUserDetails(blockedUser.getUserId(), loginUser, con);
			ConnectionManager.commit(con);

			this.sendPasswordResetEmail(blockedUser.getUserId(), newUserPassword, blockedUser.getEmpNo());
			
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return true;
	}
	
	private void updateUnblockedUserDetails(String blockedUser,String loginUser, Connection con) {
		PreparedStatement ps = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			String sql = "UPDATE public.nt_user_attempts_block_detail SET activated_by=?, activated_date=? where user_id=? AND activated_by is null; ";

			ps = con.prepareStatement(sql);
			ps.setString(1, loginUser);
			ps.setTimestamp(2, timestamp);
			ps.setString(3, blockedUser);
			ps.executeUpdate();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
		}
	}
	
	private void sendPasswordResetEmail(String userId, String userPassword, String empNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		String message = null;
		String messageN = null;

		String empTitle = null;
		String empSurname = null;
		String empEmail = null;
		String empMobile = null;
		String sql = null;
		String sql2 = null;

		userPassword = this.decryptPassword(userPassword);

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT A.emp_moblile_no, A.emp_surname,A.emp_email_add,B.description AS emp_title"
					+ " FROM nt_m_employee A INNER JOIN nt_r_title B ON A.emp_title = B.code" + " WHERE A.emp_no =?";

			stmt = con.prepareStatement(query);
			stmt.setString(1, empNo);
			rs = stmt.executeQuery();

			while (rs.next()) {
				empTitle = rs.getString("emp_title");
				empSurname = rs.getString("emp_surname");
				empEmail = rs.getString("emp_email_add");
				empMobile = rs.getString("emp_moblile_no");
			}

			message = "<p>Dear " + empTitle + "." + empSurname
					+ ",</p><p>Your password has been reset.</p><p>Your User ID is : " + userId
					+ "</p><p>Your New Password is : " + userPassword + "</p><p>Regards,</p><p>Administrator</p>";

			String emailSubject = "Login Credentials";

			sql = "INSERT INTO nt_t_pending_emails(seq,alert_type,mail_message,submited_date,receipient_email,status,message_subject,id) VALUES (?,?,?,?,?,?,?,?)";

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_pending_emails");

			ps = con.prepareStatement(sql);
			ps.setLong(1, seqNo);
			ps.setString(2, "email");
			ps.setString(3, message);
			ps.setTimestamp(4, timestamp);
			ps.setString(5, empEmail);
			ps.setString(6, "P");
			ps.setString(7, emailSubject);
			ps.setString(8, userId);

			ps.executeUpdate();

			messageN = "Dear " + empTitle + "." + empSurname
					+ ", Your password has been reset. Your User ID is : " + userId
					+ " , Your New Password is : " + userPassword + " , Regards, Administrator";

			sql2 = "INSERT INTO nt_t_pending_alerts(seq,alert_type,sms_message,submited_date,receipient_mobileno,status,message_subject) VALUES (?,?,?,?,?,?,?)";

			long seqNo2 = Utils.getNextValBySeqName(con, "seq_nt_t_pending_alerts");

			ps2 = con.prepareStatement(sql2);
			ps2.setLong(1, seqNo2);
			ps2.setString(2, "sms");
			ps2.setString(3, messageN);
			ps2.setTimestamp(4, timestamp);
			ps2.setString(5, empMobile);
			ps2.setString(6, "P");
			ps2.setString(7, emailSubject);
			ps2.executeUpdate();

			con.commit();

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(stmt);
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(con);
		}

	}

}
