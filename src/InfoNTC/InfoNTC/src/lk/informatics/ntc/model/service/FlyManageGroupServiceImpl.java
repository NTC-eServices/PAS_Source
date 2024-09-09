package lk.informatics.ntc.model.service;

import lk.informatics.ntc.model.dto.FlyingSquadActionPointsDTO;
import lk.informatics.ntc.model.dto.FlyingSquadGroupsDTO;
import lk.informatics.ntc.view.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class FlyManageGroupServiceImpl implements FlyManageGroupService {

	private static final long serialVersionUID = 1L;

	// insert flying squad groups
	public void insertGroups(String groupName, String groupStatus, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String groupcd = null;

		try {

			con = ConnectionManager.getConnection();
			java.util.Date date = new java.util.Date();
			Timestamp timeStamp = new Timestamp(date.getTime());
			groupcd = genarateGroupcode();

			String sql = "INSERT INTO public.nt_r_group\r\n"
					+ "(code, group_name, status, created_by, created_date)\r\n" + "VALUES(?,?, ?,?,?) ";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, groupcd);
			stmt.setString(2, groupName.trim());
			stmt.setString(3, groupStatus);
			stmt.setString(4, user);
			stmt.setTimestamp(5, timeStamp);

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

	public String insertGroupsNew(String groupName, String groupStatus, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String groupcd = null;
		String strGrpCode = null;

		try {

			con = ConnectionManager.getConnection();
			java.util.Date date = new java.util.Date();
			Timestamp timeStamp = new Timestamp(date.getTime());
			groupcd = genarateGroupcode();

			String sql = "INSERT INTO public.nt_r_group\r\n"
					+ "(code, group_name, status, created_by, created_date)\r\n" + "VALUES(?,?, ?,?,?) ";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, groupcd);
			stmt.setString(2, groupName.trim());
			stmt.setString(3, groupStatus);
			stmt.setString(4, user);
			stmt.setTimestamp(5, timeStamp);

			int i = stmt.executeUpdate();

			if (i > 0) {
				strGrpCode = groupcd;
			} else {
				strGrpCode = null;
			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
		return strGrpCode;

	}

	// Generate group code
	public String genarateGroupcode() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;
		String groupCd = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "select code from  nt_r_group  order by code desc limit 1 FOR UPDATE";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("code");
			}

			if (result != null) {

				String val = null;
				val = result.substring(1, 6);
				int returncountvalue = Integer.valueOf(val) + 1;
				groupCd = "G" + StringUtils.leftPad(String.valueOf(returncountvalue), 5, '0');

			}

			else {

				groupCd = "G" + "00001";

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return groupCd;
	}

	// search group name and status
	public FlyingSquadGroupsDTO search(String groupName) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		FlyingSquadGroupsDTO flyingSquadGroupsDTO = new FlyingSquadGroupsDTO();

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT code, group_name, status\r\n" + "FROM nt_r_group\r\n" + "where group_name = ?";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, groupName);
			rs = stmt.executeQuery();

			while (rs.next()) {
				flyingSquadGroupsDTO.setGroupCd(rs.getString("code"));
				flyingSquadGroupsDTO.setGroupName(rs.getString("group_name"));
				flyingSquadGroupsDTO.setStatus(rs.getString("status"));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return flyingSquadGroupsDTO;

	}

	// update details.
	public void updateGroups(String groupCd, String groupStatus, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();
			java.util.Date date = new java.util.Date();
			Timestamp timeStamp = new Timestamp(date.getTime());

			String sql = "UPDATE nt_r_group\r\n" + "SET  status= ?, modified_by=?, modified_date=?\r\n"
					+ "where code= ?";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, groupStatus);
			stmt.setString(2, user);
			stmt.setTimestamp(3, timeStamp);
			stmt.setString(4, groupCd);

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

	// get group details
	public List<FlyingSquadGroupsDTO> getGroupDetails() {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		List<FlyingSquadGroupsDTO> groupDetList = new ArrayList<FlyingSquadGroupsDTO>();

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT code,group_name,case when status = 'A' then 'Active' else 'Inactive' end as statusdesc, status"
					+ " FROM nt_r_group " + " ORDER BY code desc;";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				FlyingSquadGroupsDTO flyGroupDTO = new FlyingSquadGroupsDTO();
				flyGroupDTO.setGroupCd(rs.getString("code"));
				flyGroupDTO.setGroupName(rs.getString("group_name"));
				flyGroupDTO.setStatusDesc((rs.getString("statusdesc")));
				flyGroupDTO.setStatus(rs.getString("status"));
				groupDetList.add(flyGroupDTO);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return groupDetList;

	}

	// get action points details
	public List<FlyingSquadActionPointsDTO> getActionPointDetails() {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		List<FlyingSquadActionPointsDTO> actionPointsList = new ArrayList<FlyingSquadActionPointsDTO>();

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT code,description, case when status = 'A' then 'Active' else 'Inactive' end as statusDesc, status, "
					+ " starttime,endtime, case when is_allowforinvestigation = 'Y' then 'Yes' else 'No' end as is_allowforinvestigationDesc, is_allowforinvestigation "
					+ " FROM nt_r_actionpoints " + " ORDER BY code desc;";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				FlyingSquadActionPointsDTO flyActPointDTO = new FlyingSquadActionPointsDTO();
				flyActPointDTO.setActionCode(rs.getString("code"));
				flyActPointDTO.setActionDescription(rs.getString("description"));
				flyActPointDTO.setStatusDesc(rs.getString("statusDesc"));
				flyActPointDTO.setStatus((rs.getString("status")));
				flyActPointDTO.setStartTime(rs.getString("starttime"));
				flyActPointDTO.setEndTime(rs.getString("endtime"));
				flyActPointDTO.setAllowforInvestigationDesc(rs.getString("is_allowforinvestigationDesc"));
				flyActPointDTO.setAllowforInvestigation(rs.getString("is_allowforinvestigation"));
				actionPointsList.add(flyActPointDTO);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return actionPointsList;

	}

	// insert action points
	public boolean insertActionPoints(FlyingSquadActionPointsDTO flyingSquadActionPointsDTO, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isInserted = false;

		try {

			con = ConnectionManager.getConnection();
			java.util.Date date = new java.util.Date();
			Timestamp timeStamp = new Timestamp(date.getTime());

			String sql = "INSERT INTO public.nt_r_actionpoints "
					+ "(code, description, starttime, endtime, status, is_allowforinvestigation, created_by, created_date) "
					+ " VALUES(?,?,?,?,?,?,?,?)";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, flyingSquadActionPointsDTO.getActionCode());
			stmt.setString(2, flyingSquadActionPointsDTO.getActionDescription().trim());
			stmt.setString(3, flyingSquadActionPointsDTO.getStartTime());
			stmt.setString(4, flyingSquadActionPointsDTO.getEndTime());
			stmt.setString(5, flyingSquadActionPointsDTO.getStatus());
			stmt.setString(6, flyingSquadActionPointsDTO.getAllowforInvestigation());
			stmt.setString(7, user);
			stmt.setTimestamp(8, timeStamp);

			int i = stmt.executeUpdate();

			if (i > 0) {
				isInserted = true;
			} else {
				isInserted = false;
			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
		return isInserted;

	}

	// update action points
	public boolean updateActionPoints(FlyingSquadActionPointsDTO flyingSquadActionPointsDTO, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isInserted = false;

		try {

			con = ConnectionManager.getConnection();
			java.util.Date date = new java.util.Date();
			Timestamp timeStamp = new Timestamp(date.getTime());

			String sql = "UPDATE public.nt_r_actionpoints "
					+ " SET description= ?, status= ?, starttime= ?, endtime=?, is_allowforinvestigation=?, modified_by=?, modified_date=?"
					+ " WHERE code= ?";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, flyingSquadActionPointsDTO.getActionDescription());
			stmt.setString(2, flyingSquadActionPointsDTO.getStatus());
			stmt.setString(3, flyingSquadActionPointsDTO.getStartTime());
			stmt.setString(4, flyingSquadActionPointsDTO.getEndTime());
			stmt.setString(5, flyingSquadActionPointsDTO.getAllowforInvestigation());
			stmt.setString(6, user);
			stmt.setTimestamp(7, timeStamp);
			stmt.setString(8, flyingSquadActionPointsDTO.getActionCode());

			int i = stmt.executeUpdate();

			if (i > 0) {
				isInserted = true;
			} else {
				isInserted = false;
			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
		return isInserted;

	}

	// Check duplicate Action code
	public String checkDuplicate(String actionCode) {
		String result = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT code FROM nt_r_actionpoints WHERE code= ? ";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, actionCode);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("code");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
		return result;
	}
}
