package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import lk.informatics.ntc.model.dto.AccessPermissionDTO;
import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.roc.utils.common.Utils;

public class AccessPermissionServiceImpl implements AccessPermissionService {

	private static final long serialVersionUID = 1L;

	// Get Dept
	public List<AccessPermissionDTO> GetDeptToDropdown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<AccessPermissionDTO> returnList = new ArrayList<AccessPermissionDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description " + "FROM nt_r_department " + "WHERE active = 'A' "
					+ "ORDER BY description ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				AccessPermissionDTO accessPermissionDTO = new AccessPermissionDTO();
				accessPermissionDTO.setDeptCode(rs.getString("code"));
				accessPermissionDTO.setDeptDesc(rs.getString("description"));
				returnList.add(accessPermissionDTO);
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
	public List<AccessPermissionDTO> GetRoleToDropdown(String deptCode) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<AccessPermissionDTO> returnList = new ArrayList<AccessPermissionDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description " + "FROM nt_r_role " + "WHERE active = 'A' "
					+ "AND departmentcode = ?" + "ORDER BY description ";

			ps = con.prepareStatement(query);
			ps.setString(1, deptCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				AccessPermissionDTO accessPermissionDTO = new AccessPermissionDTO();
				accessPermissionDTO.setDeptCode(rs.getString("code"));
				accessPermissionDTO.setDeptDesc(rs.getString("description"));
				returnList.add(accessPermissionDTO);
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

	// Get All Active Function
	public List<CommonDTO> GetFunctionToDropdown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, code || ' - ' || description as des " + "FROM nt_r_function "
					+ "WHERE active = 'A' " + "ORDER BY code ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("code"));
				commonDTO.setDescription(rs.getString("des"));

				returnList.add(commonDTO);
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

	// Get All Activity
	public List<CommonDTO> GetActivityToDropdown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description " + "FROM nt_r_activity " + "ORDER BY code ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("code"));
				commonDTO.setDescription(rs.getString("description"));
				returnList.add(commonDTO);
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

	// Search Access Permission
	public List<AccessPermissionDTO> SearchAccessPermission(String deptCode, String roleCode, String funCode,
			String activityCode) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String WHERE_SQL = "";
		if (deptCode != null && !deptCode.isEmpty()) {
			WHERE_SQL = WHERE_SQL + " AND a.fra_dept_code = " + "'" + deptCode + "'";
		}
		if (roleCode != null && !roleCode.isEmpty()) {
			WHERE_SQL = WHERE_SQL + " AND a.fra_role_code = " + "'" + roleCode + "'";
		}
		if (funCode != null && !funCode.isEmpty()) {
			WHERE_SQL = WHERE_SQL + " AND a.fra_function_code = " + "'" + funCode + "'";
		}
		if (activityCode != null && !activityCode.isEmpty()) {
			WHERE_SQL = WHERE_SQL + " AND act.code = " + "'" + activityCode + "'";
		}

		List<AccessPermissionDTO> returnList = new ArrayList<AccessPermissionDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct a.fra_role_code,fra_function_code,f.description as function_name,act.description as activity"
					+ " FROM nt_r_fun_role_activity a , nt_r_function f, nt_r_activity act "
					+ " WHERE a.fra_function_code = f.code " + " AND act.code = a.fra_activity_code" + WHERE_SQL
					+ "ORDER BY a.fra_role_code,fra_function_code";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				AccessPermissionDTO accessPermissionDTO = new AccessPermissionDTO();
				accessPermissionDTO.setRoleCode(rs.getString("fra_role_code"));
				accessPermissionDTO.setFunCode(rs.getString("fra_function_code"));
				accessPermissionDTO.setFunDesc(rs.getString("function_name"));
				accessPermissionDTO.setActivityDesc(rs.getString("activity"));

				returnList.add(accessPermissionDTO);
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

	// Get All Access Permission
	public List<AccessPermissionDTO> GetAllAccessPermission() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<AccessPermissionDTO> returnList = new ArrayList<AccessPermissionDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct a.fra_role_code, fra_function_code,f.description as function_name,act.description as activity"
					+ " FROM nt_r_fun_role_activity a , nt_r_function f, nt_r_activity act "
					+ " WHERE a.fra_function_code = f.code " + " AND act.code = a.fra_activity_code"
					+ " ORDER BY  a.fra_role_code, fra_function_code";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				AccessPermissionDTO accessPermissionDTO = new AccessPermissionDTO();
				accessPermissionDTO.setRoleCode(rs.getString("fra_role_code"));
				accessPermissionDTO.setFunCode(rs.getString("fra_function_code"));
				accessPermissionDTO.setFunDesc(rs.getString("function_name"));
				accessPermissionDTO.setActivityDesc(rs.getString("activity"));

				returnList.add(accessPermissionDTO);
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

	public List<AccessPermissionDTO> getDeptToDropdown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<AccessPermissionDTO> returnList = new ArrayList<AccessPermissionDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description " + "FROM nt_r_department " + "WHERE active = 'A' "
					+ "ORDER BY description ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				AccessPermissionDTO accessPermissionDTO = new AccessPermissionDTO();
				accessPermissionDTO.setDeptCode(rs.getString("code"));
				accessPermissionDTO.setDeptDesc(rs.getString("description"));
				returnList.add(accessPermissionDTO);
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
	public List<AccessPermissionDTO> getRoleToDropdown(String deptCode) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<AccessPermissionDTO> returnList = new ArrayList<AccessPermissionDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description " + "FROM nt_r_role " + "WHERE active = 'A' "
					+ "AND departmentcode = ?" + "ORDER BY description ";

			ps = con.prepareStatement(query);
			ps.setString(1, deptCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				AccessPermissionDTO accessPermissionDTO = new AccessPermissionDTO();
				accessPermissionDTO.setRoleCode(rs.getString("code"));
				accessPermissionDTO.setRoleDesc(rs.getString("description"));
				returnList.add(accessPermissionDTO);
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
	public List<AccessPermissionDTO> getFunctionActivity(String deptCode, String roleCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<AccessPermissionDTO> functionActivityList = new ArrayList<AccessPermissionDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT A.fra_seq AS fra_seq, A.fra_function_code AS fra_function_code,D.description AS fra_activity_desc,E.description AS fra_function_desc,A.fra_role_code"
					+ " FROM nt_r_fun_role_activity A" + " INNER JOIN nt_r_department B on A.fra_dept_code=B.code"
					+ " INNER JOIN nt_r_role C on A.fra_role_code=C.code"
					+ " INNER JOIN nt_r_activity D on A.fra_activity_code=D.code"
					+ " INNER JOIN nt_r_function E on A.fra_function_code=E.code"
					+ " WHERE A.fra_dept_code = ? AND A.fra_role_code = ?" + " order by a.fra_function_code";

			ps = con.prepareStatement(query);
			ps.setString(1, deptCode);
			ps.setString(2, roleCode);
			rs = ps.executeQuery();

			while (rs.next()) {

				AccessPermissionDTO accessPermissionDTO = new AccessPermissionDTO();
				accessPermissionDTO.setFraSeq(rs.getInt("fra_seq"));
				accessPermissionDTO.setFunCode(rs.getString("fra_function_code"));
				accessPermissionDTO.setFunDesc(rs.getString("fra_function_desc"));
				accessPermissionDTO.setActDesc(rs.getString("fra_activity_desc"));
				accessPermissionDTO.setRoleCode(rs.getString("fra_role_code"));
				functionActivityList.add(accessPermissionDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return functionActivityList;

	}

	public List<AccessPermissionDTO> getFuncToDropdown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<AccessPermissionDTO> returnList = new ArrayList<AccessPermissionDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description " + "FROM nt_r_function " + "WHERE active = 'A' "
					+ "ORDER BY code ";

			ps = con.prepareStatement(query);
			// ps.setString(1, LangId);
			rs = ps.executeQuery();

			while (rs.next()) {
				AccessPermissionDTO accessPermissionDTO = new AccessPermissionDTO();
				accessPermissionDTO.setFunCode(rs.getString("code"));
				accessPermissionDTO.setFunDesc(rs.getString("description"));
				returnList.add(accessPermissionDTO);

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
	public String getFunDescToText(String selectedFunCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String funDesc = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT description " + "FROM nt_r_function " + "WHERE active = 'A' " + "AND code = ?"
					+ "ORDER BY description ";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedFunCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				funDesc = rs.getString("description");
			}
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return funDesc;
	}

	@Override
	public List<AccessPermissionDTO> getActivityToDropdown(String selectedFunCode) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<AccessPermissionDTO> returnList = new ArrayList<AccessPermissionDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT B.code AS code, B.description AS description "
					+ "FROM nt_r_function_activity A INNER JOIN nt_r_activity B ON A.activity_code=B.code "
					+ "WHERE A.function_code = ?";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedFunCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				AccessPermissionDTO accessPermissionDTO = new AccessPermissionDTO();
				accessPermissionDTO.setActCode(rs.getString("code"));
				accessPermissionDTO.setActDesc(rs.getString("description"));
				returnList.add(accessPermissionDTO);

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
	public List<AccessPermissionDTO> saveFunctionActivity(String selectedDept, String selectedRole,
			String selectedFunCode, String selectedActCode, String createdBy) {

		PreparedStatement stmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();

			java.util.Date date = new java.util.Date();
			Timestamp timestamp = new Timestamp(date.getTime());

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_r_fun_role_activity");

			String sql = "INSERT INTO nt_r_fun_role_activity(fra_seq,fra_dept_code,fra_role_code,fra_function_code,"
					+ "fra_activity_code, created_by, created_date )" + "VALUES(?,?,?,?,?,?,?)";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, selectedDept);
			stmt.setString(3, selectedRole);
			stmt.setString(4, selectedFunCode);
			stmt.setString(5, selectedActCode);
			stmt.setString(6, createdBy);
			stmt.setTimestamp(7, timestamp);

			stmt.executeUpdate();

			con.commit();

			return null;
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return null;
	}

	@Override
	public void removeFunctionActivity(int selectedFRA) {

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "DELETE " + "FROM nt_r_fun_role_activity " + "WHERE fra_seq = ?";

			ps = con.prepareStatement(query);
			ps.setInt(1, selectedFRA);
			ps.executeUpdate();

			con.commit();

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
	}

	// Check Duplicates.
	public String chkDuplicates(String selectedDept, String selectedRole, String selectedFunCode,
			String selectedActivity) {

		String strRouteNo = null;
		Connection con;
		PreparedStatement ps = null;
		ResultSet rs = null;

		con = ConnectionManager.getConnection();
		try {

			String sql = "select fra_function_code " + " from nt_r_fun_role_activity " + " where fra_dept_code= ? "
					+ " and fra_role_code = ? " + " and fra_function_code = ? " + " and fra_activity_code = ? ;";

			ps = con.prepareStatement(sql);

			ps.setString(1, selectedDept);
			ps.setString(2, selectedRole);
			ps.setString(3, selectedFunCode);
			ps.setString(4, selectedActivity);
			rs = ps.executeQuery();

			while (rs.next()) {
				strRouteNo = rs.getString("fra_function_code");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return strRouteNo;
	}

	@Override
	public List<AccessPermissionDTO> getFunctionActivityForOnlyDep(String selectedDept) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<AccessPermissionDTO> functionActivityList = new ArrayList<AccessPermissionDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT A.fra_seq AS fra_seq, A.fra_function_code AS fra_function_code,D.description AS fra_activity_desc,E.description AS fra_function_desc,A.fra_role_code"
					+ " FROM nt_r_fun_role_activity A" + " INNER JOIN nt_r_department B on A.fra_dept_code=B.code"
					+ " INNER JOIN nt_r_role C on A.fra_role_code=C.code"
					+ " INNER JOIN nt_r_activity D on A.fra_activity_code=D.code"
					+ " INNER JOIN nt_r_function E on A.fra_function_code=E.code" + " WHERE A.fra_dept_code = ?"
					+ " order by a.fra_function_code";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedDept);

			rs = ps.executeQuery();

			while (rs.next()) {

				AccessPermissionDTO accessPermissionDTO = new AccessPermissionDTO();
				accessPermissionDTO.setFraSeq(rs.getInt("fra_seq"));
				accessPermissionDTO.setFunCode(rs.getString("fra_function_code"));
				accessPermissionDTO.setFunDesc(rs.getString("fra_function_desc"));
				accessPermissionDTO.setActDesc(rs.getString("fra_activity_desc"));
				accessPermissionDTO.setRoleCode(rs.getString("fra_role_code"));
				functionActivityList.add(accessPermissionDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return functionActivityList;
	}

	@Override
	public String getRemovedFunctionId(int selectedFRA) {

		String functionCode = null;
		Connection con;
		PreparedStatement ps = null;
		ResultSet rs = null;

		con = ConnectionManager.getConnection();
		try {

			String sql = "select fra_function_code from nt_r_fun_role_activity  where fra_seq=?;";

			ps = con.prepareStatement(sql);

			ps.setInt(1, selectedFRA);
			rs = ps.executeQuery();

			while (rs.next()) {
				functionCode = rs.getString("fra_function_code");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return functionCode;
	}
}
