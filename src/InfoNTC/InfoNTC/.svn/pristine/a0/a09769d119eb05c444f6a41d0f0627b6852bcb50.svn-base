package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import lk.informatics.ntc.model.dto.FlyingSquadApprovalInvestigationDTO;
import lk.informatics.ntc.model.dto.FlyngSquadAttendanceDTO;
import lk.informatics.ntc.view.util.ConnectionManager;

public class FlyingSquadAttendanceServiceImpl implements FlyingSquadAttendanceService {

	public ArrayList<FlyingSquadApprovalInvestigationDTO> getReferenceNo() {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<FlyingSquadApprovalInvestigationDTO> referenceNoList = new ArrayList<FlyingSquadApprovalInvestigationDTO>();

		try {

			con = ConnectionManager.getConnection();
			String sql = "SELECT inv_refno \r\n" + "FROM public.nt_t_flying_investigation_master \r\n"
					+ "where inv_status ='A'\r\n" + "order by inv_refno ";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				FlyingSquadApprovalInvestigationDTO ApprovalInvestigationDTO = new FlyingSquadApprovalInvestigationDTO();
				ApprovalInvestigationDTO.setReferenceNo(rs.getString("inv_refno"));

				referenceNoList.add(ApprovalInvestigationDTO);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
		return referenceNoList;

	}

	// get date and group name
	public FlyngSquadAttendanceDTO getRefData(String refNo) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		FlyngSquadAttendanceDTO flyngSquadAttendanceDTO = new FlyngSquadAttendanceDTO();

		try {

			con = ConnectionManager.getConnection();
			String sql = "SELECT  inv_date, inv_group_cd,group_name\r\n"
					+ "FROM public.nt_t_flying_investigation_master,public.nt_r_group\r\n" + "where inv_refno = ? \r\n"
					+ "and nt_t_flying_investigation_master.inv_group_cd=nt_r_group.code";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, refNo);
			rs = stmt.executeQuery();

			while (rs.next()) {

				flyngSquadAttendanceDTO.setIvesDate(rs.getDate("inv_date"));
				flyngSquadAttendanceDTO.setGroupCd(rs.getString("inv_group_cd"));
				flyngSquadAttendanceDTO.setGroupdes(rs.getString("group_name"));

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
		return flyngSquadAttendanceDTO;

	}

//get details from attendance tables for exisiting records
	public ArrayList<FlyngSquadAttendanceDTO> getexsistingdetails(String referenceNo, Date invesDate) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<FlyngSquadAttendanceDTO> DetailList = new ArrayList<FlyngSquadAttendanceDTO>();
		java.sql.Date sqlDate = new java.sql.Date(invesDate.getTime());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

		try {

			con = ConnectionManager.getConnection();
			String sql = "SELECT inv_empno, inv_intime, inv_outtime,inv_det_name,inv_attended ,\r\n"
					+ "inv_created_by, inv_created_date, inv_modified_by, inv_modified_date ,inv_date\r\n"
					+ "FROM public.nt_t_flying_investigation_attendance ,nt_t_flying_investigation_details\r\n"
					+ "where inv_refno = ?\r\n"
					+ "and nt_t_flying_investigation_attendance.inv_refno=nt_t_flying_investigation_details.inv_det_refno\r\n"
					+ "and to_char(inv_date,'YYYY/MM/dd')= ?\r\n"
					+ "and nt_t_flying_investigation_attendance.inv_empno = nt_t_flying_investigation_details.inv_det_empno";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, referenceNo);
			stmt.setString(2, sdf.format(sqlDate));
			rs = stmt.executeQuery();

			while (rs.next()) {
				SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
				String startTime = null;
				startTime = rs.getString("inv_intime");
				String endTime = null;
				endTime = rs.getString("inv_outtime");
				Date startTimen = null;
				Date endTimen = null;

				if (startTime != null) {
					startTimen = (localDateFormat.parse(startTime));
				}

				if (endTime != null) {
					endTimen = localDateFormat.parse(endTime);
				}

				FlyngSquadAttendanceDTO flyngSquadAttendanceDTO = new FlyngSquadAttendanceDTO();
				flyngSquadAttendanceDTO.setEpfNo(rs.getString("inv_empno"));
				flyngSquadAttendanceDTO.setStartTime(startTimen);
				flyngSquadAttendanceDTO.setEndtime(endTimen);
				flyngSquadAttendanceDTO.setEmpName(rs.getString("inv_det_name"));
				flyngSquadAttendanceDTO.setIsattend(rs.getBoolean("inv_attended"));
				flyngSquadAttendanceDTO.setIsupdate(true);
				flyngSquadAttendanceDTO.setCreatedBy(rs.getString("inv_created_by"));
				flyngSquadAttendanceDTO.setModifiedby(rs.getString("inv_modified_by"));
				flyngSquadAttendanceDTO.setCreatedDate(rs.getTimestamp("inv_created_date"));
				flyngSquadAttendanceDTO.setModifiedDate(rs.getTimestamp("inv_modified_date"));
				// flyngSquadAttendanceDTO.setIvesDate(rs.getTimestamp("inv_date"));
				DetailList.add(flyngSquadAttendanceDTO);
			}

			if (DetailList.size() == 0) {
				DetailList = getnonexsistingdetails(referenceNo);
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

	@Override
	public String isAttended(String referenceNo, Date invesDate) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String isAttended = null;
		java.sql.Date sqlDate = new java.sql.Date(invesDate.getTime());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		try {

			con = ConnectionManager.getConnection();

			String query = "select inv_refno " + " from nt_t_flying_investigation_attendance "
					+ " where inv_refno = ? and to_char(inv_date,'YYYY/MM/dd')= ?";

			ps = con.prepareStatement(query);
			ps.setString(1, referenceNo);
			ps.setString(2, sdf.format(sqlDate));
			rs = ps.executeQuery();

			while (rs.next()) {
				isAttended = rs.getString("inv_refno");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isAttended;
	}

//get not existing for not existing date records
	public ArrayList<FlyngSquadAttendanceDTO> getnonexsistingdetails(String referenceNo) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<FlyngSquadAttendanceDTO> DetailListn = new ArrayList<FlyngSquadAttendanceDTO>();

		try {

			con = ConnectionManager.getConnection();
			String sql = "select inv_det_empno, inv_det_name\r\n" + "FROM public.nt_t_flying_investigation_details\r\n"
					+ "where inv_det_refno = ? and inv_det_flag = 'true' ";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, referenceNo);
			rs = stmt.executeQuery();

			while (rs.next()) {
				FlyngSquadAttendanceDTO flyngSquadAttendanceDTO = new FlyngSquadAttendanceDTO();
				flyngSquadAttendanceDTO.setEpfNo(rs.getString("inv_det_empno"));
				flyngSquadAttendanceDTO.setEmpName(rs.getString("inv_det_name"));
				flyngSquadAttendanceDTO.setIsattend(false);
				flyngSquadAttendanceDTO.setIsupdate(false);
				DetailListn.add(flyngSquadAttendanceDTO);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return DetailListn;

	}

//get refNo
	public String getRefNo(Date invesDate, String groupCd) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String refNo = null;
		java.sql.Date sqlDate = new java.sql.Date(invesDate.getTime());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		try {

			con = ConnectionManager.getConnection();
			String sql = "SELECT inv_refno\r\n" + "FROM public.nt_t_flying_investigation_master\r\n"
					+ "where to_char(inv_date,'yyyy/MM/DD') = ?\r\n" + "and inv_group_cd = ?";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, sdf.format(sqlDate));
			stmt.setString(2, groupCd);
			rs = stmt.executeQuery();

			while (rs.next()) {
				refNo = rs.getString("inv_refno");
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

//---insert table
	public void saveDetailDta(FlyngSquadAttendanceDTO flyngSquadAttendanceDTO, String user, String refNo,
			String groupcd, Date invDate) {
		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = ConnectionManager.getConnection();
			java.util.Date date = new java.util.Date();
			Timestamp timeStamp = new Timestamp(date.getTime());
			java.sql.Date sqlDate = new java.sql.Date(invDate.getTime());
			String startTime = null;
			String endTime = null;

			SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
			if (flyngSquadAttendanceDTO.getStartTime() == null) {
				startTime = null;
			} else {
				startTime = localDateFormat.format(flyngSquadAttendanceDTO.getStartTime());
			}

			if (flyngSquadAttendanceDTO.getEndtime() == null) {
				endTime = null;
			} else {
				endTime = localDateFormat.format(flyngSquadAttendanceDTO.getEndtime());
			}

			String sql = "INSERT INTO public.nt_t_flying_investigation_attendance\r\n"
					+ "(inv_refno, inv_date, inv_group_cd, inv_empno, inv_intime, inv_outtime, inv_attended, inv_created_by, inv_created_date)\r\n"
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, refNo);
			stmt.setDate(2, sqlDate);
			stmt.setString(3, groupcd);
			stmt.setString(4, flyngSquadAttendanceDTO.getEpfNo());
			stmt.setString(5, startTime);
			stmt.setString(6, endTime);
			stmt.setBoolean(7, flyngSquadAttendanceDTO.getIsattend());
			stmt.setString(8, user);
			stmt.setTimestamp(9, timeStamp);
			stmt.executeUpdate();
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

	}

//update table
	public void updateDetailDta(FlyngSquadAttendanceDTO flyngSquadAttendanceDTO, String user, String refNo,
			String groupcd, Date invDate) {
		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;

		try {
			con = ConnectionManager.getConnection();
			java.util.Date date = new java.util.Date();
			Timestamp timeStamp = new Timestamp(date.getTime());
			java.sql.Date sqlDate = new java.sql.Date(invDate.getTime());

			SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
			String startTime = null;
			String endTime = null;

			if (flyngSquadAttendanceDTO.getStartTime() != null) {
				startTime = localDateFormat.format(flyngSquadAttendanceDTO.getStartTime());
			}

			if (flyngSquadAttendanceDTO.getEndtime() != null) {
				endTime = localDateFormat.format(flyngSquadAttendanceDTO.getEndtime());
			}
			
			
			//select current data and insert into history
			String sql1 = "INSERT INTO public.nt_h_flying_investigation_attendance(select * from public.nt_t_flying_investigation_attendance where inv_refno =?);";

			stmt1 = con.prepareStatement(sql1);
			stmt1.setString(1, refNo);
			
			stmt1.executeUpdate();
			//select current data and insert into history
			
			
			

			String sql = "UPDATE public.nt_t_flying_investigation_attendance\r\n"
					+ "SET  inv_intime=?, inv_outtime=?, inv_attended=?,\r\n"
					+ "inv_modified_by=?, inv_modified_date=?\r\n" + "where inv_refno=?\r\n" + "and  inv_empno=?"
					+ " and inv_date = ? ";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, startTime);
			stmt.setString(2, endTime);
			stmt.setBoolean(3, flyngSquadAttendanceDTO.getIsattend());
			stmt.setString(4, user);
			stmt.setTimestamp(5, timeStamp);
			stmt.setString(6, refNo);
			stmt.setString(7, flyngSquadAttendanceDTO.getEpfNo());
			stmt.setDate(8, sqlDate);
			stmt.executeUpdate();
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(stmt1);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

	}

//-- add record to history table
	public void saveHistoryDetailDta(ArrayList<FlyngSquadAttendanceDTO> flyngSquadAttendanceDTOList, String user,
			String refNo, String groupcd, Date invDate) {
		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = ConnectionManager.getConnection();
			java.util.Date date = new java.util.Date();
			Timestamp timeStamp = new Timestamp(date.getTime());
			java.sql.Date sqlDate = new java.sql.Date(invDate.getTime());

			for (FlyngSquadAttendanceDTO flyngSquadAttendanceDTO : flyngSquadAttendanceDTOList) {

				SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
				String startTime = null;
				String endTime = null;

				if (flyngSquadAttendanceDTO.getStartTime() != null) {
					startTime = localDateFormat.format(flyngSquadAttendanceDTO.getStartTime());
				}

				if (flyngSquadAttendanceDTO.getEndtime() != null) {
					endTime = localDateFormat.format(flyngSquadAttendanceDTO.getEndtime());
				}

				String sql = "INSERT INTO public.nt_h_flying_investigation_attendance\r\n"
						+ "(inv_h_refno, inv_h_date, inv_h_group_cd, inv_h_empno, inv_h_intime, inv_h_outtime, inv_h_created_by, inv_h_created_date, inv_h_modified_by, inv_h_modified_date, created_by, created_date)\r\n"
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				stmt = con.prepareStatement(sql);
				stmt.setString(1, refNo);
				stmt.setDate(2, sqlDate);
				stmt.setString(3, groupcd);
				stmt.setString(4, flyngSquadAttendanceDTO.getEpfNo());
				stmt.setString(5, startTime);
				stmt.setString(6, endTime);
				stmt.setString(7, flyngSquadAttendanceDTO.getCreatedBy());
				stmt.setTimestamp(8, flyngSquadAttendanceDTO.getCreatedDate());
				stmt.setString(9, flyngSquadAttendanceDTO.getModifiedby());
				stmt.setTimestamp(10, flyngSquadAttendanceDTO.getModifiedDate());
				stmt.setString(11, user);
				stmt.setTimestamp(12, timeStamp);
				stmt.executeUpdate();
				con.commit();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

	}

	public ArrayList<FlyngSquadAttendanceDTO> getgroups() {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<FlyngSquadAttendanceDTO> groupList = new ArrayList<FlyngSquadAttendanceDTO>();

		try {

			con = ConnectionManager.getConnection();
			String sql = "SELECT code, group_name\r\n" + "FROM public.nt_r_group\r\n" + "where status ='A'\r\n";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				FlyngSquadAttendanceDTO flyngSquadAttendanceDTO = new FlyngSquadAttendanceDTO();
				flyngSquadAttendanceDTO.setGroupCd(rs.getString("code"));
				flyngSquadAttendanceDTO.setGroupdes(rs.getString("group_name"));
				groupList.add(flyngSquadAttendanceDTO);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
		return groupList;

	}

}
