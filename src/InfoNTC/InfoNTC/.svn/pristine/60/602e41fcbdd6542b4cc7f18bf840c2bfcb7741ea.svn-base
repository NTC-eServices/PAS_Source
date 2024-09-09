package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import lk.informatics.ntc.model.dto.FlyingSquadGroupsDTO;
import lk.informatics.ntc.model.dto.FlyingSquadSteupDTO;
import lk.informatics.ntc.model.dto.UserDTO;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.roc.utils.common.Utils;

public class FlySteupSquadServiceImpl implements FlySetupSquadService {

	// get group list
	public ArrayList<FlyingSquadGroupsDTO> getGroupList() {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<FlyingSquadGroupsDTO> groupList = new ArrayList<FlyingSquadGroupsDTO>(0);

		try {
			con = ConnectionManager.getConnection();
			stmt = con.prepareStatement("SELECT code, group_name\r\n" + "FROM public.nt_r_group \r\n"
					+ "where  status = 'A'\r\n" + "order by code");

			rs = stmt.executeQuery();

			while (rs.next()) {
				FlyingSquadGroupsDTO dto = new FlyingSquadGroupsDTO();
				dto.setGroupCd(rs.getString("code"));
				dto.setGroupName(rs.getString("group_name"));
				dto.setDisplayGroupName(rs.getString("code") + "-" + rs.getString("group_name"));
				groupList.add(dto);

			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
		return groupList;

	}

	// get userId
	public ArrayList<UserDTO> getUserList() {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<UserDTO> userList = new ArrayList<UserDTO>(0);

		try {
			con = ConnectionManager.getConnection();
			stmt = con.prepareStatement("\r\n" + "select  emp_user_id\r\n" + "from public.nt_m_employee\r\n"
					+ "where emp_user_id is not null\r\n" + "and emp_user_status ='A'");

			rs = stmt.executeQuery();

			while (rs.next()) {
				UserDTO dto = new UserDTO();
				dto.setUsername(rs.getString("emp_user_id"));
				userList.add(dto);

			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
		return userList;

	}

	// get EmployeeList
	public ArrayList<UserDTO> getEmpNoList() {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<UserDTO> empNoList = new ArrayList<UserDTO>(0);

		try {
			con = ConnectionManager.getConnection();
			stmt = con.prepareStatement(
					"select  emp_no\r\n" + "from public.nt_m_employee\r\n" + "where emp_user_status ='A'");

			rs = stmt.executeQuery();

			while (rs.next()) {
				UserDTO dto = new UserDTO();
				dto.setEmpNo(rs.getString("emp_no"));
				empNoList.add(dto);

			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
		return empNoList;

	}

	// get officer name
	public FlyingSquadSteupDTO getOfficerDetails(String userName) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String whereSql = null;

		try {
			con = ConnectionManager.getConnection();

			if (userName != null && !userName.equalsIgnoreCase("")) {

				whereSql = "where emp_user_id =" + "'" + userName + "'";

			}

			stmt = con.prepareStatement(
					"select  emp_no,emp_user_id,emp_fullname\r\n" + "from public.nt_m_employee \r\n" + whereSql);

			rs = stmt.executeQuery();

			while (rs.next()) {
				FlyingSquadSteupDTO dto = new FlyingSquadSteupDTO();
				dto.setEmpId(rs.getString("emp_no"));
				dto.setUserId(rs.getString("emp_user_id"));
				dto.setOfficerName(rs.getString("emp_fullname"));

				return dto;
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return null;
	}

	// insert details
	public void insertFlyingSquad(String groupCd, String UserId, String empId, String empName, String status,
			String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();
			java.util.Date date = new java.util.Date();
			Timestamp timeStamp = new Timestamp(date.getTime());
			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_flying_squad");

			String sql = "INSERT INTO public.nt_t_flying_squad\r\n"
					+ "(seq, fly_group_cd, fly_user_id, fly_emp_no, fly_officer_name, fly_status, fly_created_by, fly_created_date)\r\n"
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?)\r\n";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, groupCd);
			stmt.setString(3, UserId);
			stmt.setString(4, empId);
			stmt.setString(5, empName);
			stmt.setString(6, status);
			stmt.setString(7, user);
			stmt.setTimestamp(8, timeStamp);

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

//load active details.
	public ArrayList<FlyingSquadSteupDTO> getAllDetails() {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<FlyingSquadSteupDTO> flySquad = new ArrayList<FlyingSquadSteupDTO>(0);

		try {
			con = ConnectionManager.getConnection();
			stmt = con.prepareStatement(
					"SELECT a.seq, a.fly_group_cd, a.fly_user_id, a.fly_emp_no, a.fly_officer_name, a.fly_status, \r\n"
							+ "a.fly_created_by, a.fly_created_date, a.fly_modified_by, a.fly_modified_date ,b.group_name \r\n"
							+ "FROM public.nt_t_flying_squad a ,public.nt_r_group b\r\n"
							+ "where b.code = a.fly_group_cd");

			rs = stmt.executeQuery();

			while (rs.next()) {
				FlyingSquadSteupDTO dto = new FlyingSquadSteupDTO();
				dto.setSeqNo(rs.getLong("seq"));
				dto.setGroupcd(rs.getString("fly_group_cd"));
				dto.setUserId(rs.getString("fly_user_id"));
				dto.setEmpId(rs.getString("fly_emp_no"));
				dto.setOfficerName(rs.getString("fly_officer_name"));
				dto.setStatus(rs.getString("fly_status"));
				dto.setCreatedBy(rs.getString("fly_created_by"));
				dto.setCreatedDate(rs.getTimestamp("fly_created_date"));
				dto.setModifiedBy(rs.getString("fly_modified_by"));
				dto.setModifiedDate(rs.getTimestamp("fly_modified_date"));
				dto.setGroupName(rs.getString("group_name"));

				if (dto.getStatus().equalsIgnoreCase("A")) {
					dto.setStatusdes("Active");

				} else {

					dto.setStatusdes("Inactive");
				}

				flySquad.add(dto);

			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
		return flySquad;

	}

//update Details
	public void updteDetails(String groupCd, String userId, String status, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();
			java.util.Date date = new java.util.Date();
			Timestamp timeStamp = new Timestamp(date.getTime());

			String sql = "UPDATE public.nt_t_flying_squad\r\n"
					+ "SET   fly_status=?,fly_modified_by=?, fly_modified_date=?\r\n" + "WHERE fly_group_cd=?\r\n"
					+ "and  fly_user_id=?";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, status);
			stmt.setString(2, user);
			stmt.setTimestamp(3, timeStamp);
			stmt.setString(4, groupCd);
			stmt.setString(5, userId);
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

//insert history details
	public void insertFlyingSquadHistory(long seqNo, String groupCd, String UserId, String empId, String empName,
			String status, String createdBy, Timestamp createdDate, String modifiedby, Timestamp modififedDate) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			String sql = "INSERT INTO public.nt_h_flying_squad\r\n"
					+ "(fly_h_seq, fly_h_group_cd, fly_h_user_id, fly_h_emp_no, fly_h_officer_name, fly_h_status, fly_h_created_by, fly_h_created_date, fly_h_modified_by, fly_h_modified_date)\r\n"
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);\r\n";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, groupCd);
			stmt.setString(3, UserId);
			stmt.setString(4, empId);
			stmt.setString(5, empName);
			stmt.setString(6, status);
			stmt.setString(7, createdBy);
			stmt.setTimestamp(8, createdDate);
			stmt.setString(9, modifiedby);
			stmt.setTimestamp(10, modififedDate);
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

	// --------------------------------------------------------
	// change employeeID details

	public FlyingSquadSteupDTO getOfficerDetailsFromEmpId(String empNo) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String whereSql = null;

		try {
			con = ConnectionManager.getConnection();
			if (empNo != null && !empNo.equalsIgnoreCase("")) {

				whereSql = "where emp_no =" + "'" + empNo + "'";
			}

			stmt = con.prepareStatement(
					"select  emp_no,emp_user_id,emp_fullname\r\n" + "from public.nt_m_employee \r\n" + whereSql);

			rs = stmt.executeQuery();

			while (rs.next()) {
				FlyingSquadSteupDTO dto = new FlyingSquadSteupDTO();
				dto.setEmpId(rs.getString("emp_no"));
				dto.setUserId(rs.getString("emp_user_id"));
				dto.setOfficerName(rs.getString("emp_fullname"));

				return dto;
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return null;
	}

	// ----------------------------------------------------------------
	// --check duplicate Values---

	public boolean checkduplicateValues(String groupCd, String userId) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();
			stmt = con.prepareStatement("SELECT  fly_user_id\r\n" + "FROM public.nt_t_flying_squad\r\n"
					+ "where fly_group_cd = ?\r\n" + "and fly_user_id = ?");

			stmt.setString(1, groupCd);
			stmt.setString(2, userId);
			rs = stmt.executeQuery();

			while (rs.next()) {
				String user = null;
				user = rs.getString("fly_user_id");

				if (!user.isEmpty() || user != null || !user.equalsIgnoreCase("")) {
					return true;
				} else {
					return false;
				}

			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return false;
	}

}
