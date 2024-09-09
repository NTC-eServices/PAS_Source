package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

import lk.informatics.ntc.model.dto.FlyingSquadGroupsDTO;
import lk.informatics.ntc.model.dto.FlyingSquadInvestiDetailDTO;
import lk.informatics.ntc.model.dto.FlyingSquadInvestiMasterDTO;
import lk.informatics.ntc.view.util.ConnectionManager;

public class FlyingSquadInvestigationServiceImpl implements FlyingSquadInvestigationService {

	// get the groupcodes
	public ArrayList<FlyingSquadGroupsDTO> getGroupcode(int year, int month) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<FlyingSquadGroupsDTO> FlyingSquadGroupsDTOList = new ArrayList<FlyingSquadGroupsDTO>();

		try {
			con = ConnectionManager.getConnection();

			String sql = "select fly_sch_groupcd,group_name\r\n" + "from nt_t_flying_squad_shedule,nt_r_group\r\n"
					+ "where fly_sch_year = ? \r\n" + "and fly_sch_month = ? \r\n"
					+ "and nt_r_group.code = nt_t_flying_squad_shedule.fly_sch_groupcd";

			stmt = con.prepareStatement(sql);
			stmt.setInt(1, year);
			stmt.setInt(2, month);
			rs = stmt.executeQuery();

			while (rs.next()) {
				FlyingSquadGroupsDTO flyingSquadGroupsDTO = new FlyingSquadGroupsDTO();
				flyingSquadGroupsDTO.setGroupCd(rs.getString("fly_sch_groupcd"));
				flyingSquadGroupsDTO
						.setDisplayGroupName(rs.getString("fly_sch_groupcd") + "-" + rs.getString("group_name"));
				FlyingSquadGroupsDTOList.add(flyingSquadGroupsDTO);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return FlyingSquadGroupsDTOList;

	}

	// get officer detail List
	public ArrayList<FlyingSquadInvestiDetailDTO> groupDetail(String groupCd) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<FlyingSquadInvestiDetailDTO> flyingSquadInvestiDetailList = new ArrayList<FlyingSquadInvestiDetailDTO>();

		try {
			con = ConnectionManager.getConnection();

			String sql = "select  fly_emp_no,fly_officer_name,description\r\n"
					+ "from nt_m_emp_designation a,nt_t_flying_squad b,nt_r_designation c\r\n"
					+ "where a.des_desig_code = c.code\r\n" + "and b.fly_emp_no = a.des_emp_no\r\n"
					+ "and b.fly_group_cd =?";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, groupCd);
			rs = stmt.executeQuery();

			while (rs.next()) {
				FlyingSquadInvestiDetailDTO flyingSquadInvestiDetailDTO = new FlyingSquadInvestiDetailDTO();
				flyingSquadInvestiDetailDTO.setEmpNo(rs.getString("fly_emp_no"));
				flyingSquadInvestiDetailDTO.setName(rs.getString("fly_officer_name"));
				flyingSquadInvestiDetailDTO.setDesignation(rs.getString("description"));
				flyingSquadInvestiDetailDTO.setAllow(false);
				flyingSquadInvestiDetailList.add(flyingSquadInvestiDetailDTO);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return flyingSquadInvestiDetailList;

	}

	// get seqNo for genarate the reference no
	public String getSeqNo(int year) {
		int seqNo = 0;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String refNo = null;

		try {
			String y = Integer.toString(year).substring(2, 4);
			con = ConnectionManager.getConnection();
			String sql = "SELECT COALESCE(fly_inves_seq_no,0) as seq\r\n" + "from nt_p_flying_inves_seq\r\n"
					+ "where fly_inves_year = ?";

			stmt = con.prepareStatement(sql);
			stmt.setInt(1, year);
			rs = stmt.executeQuery();

			while (rs.next()) {

				seqNo = rs.getInt("seq");

				if (seqNo == 0) {

					initializeSequenceNo(year);
					seqNo = 000001;
					refNo = "FS" + y + StringUtils.leftPad(String.valueOf(seqNo), 6, '0');
				} else {
					seqNo = seqNo + 1;
					refNo = "FS" + y + StringUtils.leftPad(String.valueOf(seqNo), 6, '0');
					UpdateNextSequenceNo(year, seqNo);
				}

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

	// --Initialize the SeqNo
	public void initializeSequenceNo(int year) {
		Connection con = null;
		PreparedStatement stmt = null;

		try {

			con = ConnectionManager.getConnection();
			String sql = "INSERT INTO public.nt_p_flying_inves_seq\r\n" + "(fly_inves_year, fly_inves_seq_no)\r\n"
					+ "VALUES(?, '1')";

			stmt = con.prepareStatement(sql);
			stmt.setInt(1, year);
			stmt.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

	}

	// -----update nextSquenceNo-------------------
	public void UpdateNextSequenceNo(int year, int seqNo) {
		Connection con = null;
		PreparedStatement stmt = null;

		try {

			con = ConnectionManager.getConnection();
			String sql = "UPDATE public.nt_p_flying_inves_seq\r\n" + "SET  fly_inves_seq_no=?\r\n"
					+ "where fly_inves_year=?";

			stmt = con.prepareStatement(sql);
			stmt.setInt(1, seqNo);
			stmt.setInt(2, year);
			stmt.executeUpdate();

			con.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

	}

	// --Save master Details------------------------------------------------------
	public void saveMasterDta(FlyingSquadInvestiMasterDTO flyingSquadInvestiMasterDTO, String user) {
		Connection con = null;
		PreparedStatement stmt = null;

		try {

			java.util.Date date = new java.util.Date();
			Timestamp timeStamp = new Timestamp(date.getTime());

			java.sql.Date sqlDate = new java.sql.Date(flyingSquadInvestiMasterDTO.getInvestigationDate().getTime());
			SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
			String startTime = localDateFormat.format(flyingSquadInvestiMasterDTO.getStartTime());
			Timestamp ts = new Timestamp(flyingSquadInvestiMasterDTO.getEndtime().getTime());

			con = ConnectionManager.getConnection();
			String sql = "INSERT INTO public.nt_t_flying_investigation_master\r\n"
					+ "(inv_refno, inv_date, inv_group_cd, inv_start_time, inv_end_time, inv_vehicle_no, inv_driver_name, inv_start_place, inv_route, inv_end_place, inv_night_park_place, inv_remarks, inv_status, inv_created_by, inv_created_date)\r\n"
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, flyingSquadInvestiMasterDTO.getRefNo());
			stmt.setDate(2, sqlDate);
			stmt.setString(3, flyingSquadInvestiMasterDTO.getGroupCd());
			stmt.setString(4, startTime);
			stmt.setTimestamp(5, ts);
			stmt.setString(6, flyingSquadInvestiMasterDTO.getVehicleNo());
			stmt.setString(7, flyingSquadInvestiMasterDTO.getDriverName());
			stmt.setString(8, flyingSquadInvestiMasterDTO.getStartPlace());
			stmt.setString(9, flyingSquadInvestiMasterDTO.getInvestigationDetails());
			stmt.setString(10, flyingSquadInvestiMasterDTO.getEndPlace());
			stmt.setString(11, flyingSquadInvestiMasterDTO.getNightParkPlace());
			stmt.setString(12, flyingSquadInvestiMasterDTO.getRemarks());
			stmt.setString(13, "P");
			stmt.setString(14, user);
			stmt.setTimestamp(15, timeStamp);

			stmt.executeUpdate();
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

	}

	// ------save details------------
	public boolean saveDetailDta(ArrayList<FlyingSquadInvestiDetailDTO> flyingSquadInvestiDetailList, String user,
			String refNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		boolean success = false;

		try {

			java.util.Date date = new java.util.Date();
			Timestamp timeStamp = new Timestamp(date.getTime());

			for (FlyingSquadInvestiDetailDTO flyingSquadInvestiDetailDTO : flyingSquadInvestiDetailList) {

				con = ConnectionManager.getConnection();
				String sql = "INSERT INTO nt_t_flying_investigation_details\r\n"
						+ "(inv_det_refno, inv_det_name, inv_det_designation, inv_det_flag, inv_det_created_by, inv_det_created_date, inv_det_empno)\r\n"
						+ "VALUES(?,?,?,?,?,?,?)";

				stmt = con.prepareStatement(sql);
				stmt.setString(1, refNo);
				stmt.setString(2, flyingSquadInvestiDetailDTO.getName());
				stmt.setString(3, flyingSquadInvestiDetailDTO.getDesignation());
				stmt.setBoolean(4, flyingSquadInvestiDetailDTO.isAllow());
				stmt.setString(5, user);
				stmt.setTimestamp(6, timeStamp);
				stmt.setString(7, flyingSquadInvestiDetailDTO.getEmpNo());

				stmt.executeUpdate();
				con.commit();
				success = true;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			success = false;
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
		return success;

	}

	public FlyingSquadInvestiMasterDTO getmasterDetails(String refNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		FlyingSquadInvestiMasterDTO flyingSquadInvestiMasterDTO = new FlyingSquadInvestiMasterDTO();

		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT  inv_date, inv_group_cd, inv_start_time, inv_vehicle_no, inv_driver_name,\r\n"
					+ "inv_start_place, inv_route, inv_end_place, inv_night_park_place, inv_remarks, inv_end_time,inv_status,group_name\r\n"
					+ "FROM nt_t_flying_investigation_master,nt_r_group\r\n" + "where inv_refno =?\r\n"
					+ "and  nt_t_flying_investigation_master.inv_group_cd = nt_r_group.code";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, refNo);
			rs = stmt.executeQuery();

			while (rs.next()) {
				SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
				String startTime = rs.getString("inv_start_time");

				flyingSquadInvestiMasterDTO.setInvestigationDate(rs.getDate("inv_date"));
				flyingSquadInvestiMasterDTO.setGroupCd(rs.getString("inv_group_cd"));
				flyingSquadInvestiMasterDTO.setStartTime(localDateFormat.parse(startTime));
				flyingSquadInvestiMasterDTO.setVehicleNo(rs.getString("inv_vehicle_no"));
				flyingSquadInvestiMasterDTO.setDriverName(rs.getString("inv_driver_name"));
				flyingSquadInvestiMasterDTO.setStartPlace(rs.getString("inv_start_place"));
				flyingSquadInvestiMasterDTO.setInvestigationDetails(rs.getString("inv_route"));
				flyingSquadInvestiMasterDTO.setEndPlace(rs.getString("inv_end_place"));
				flyingSquadInvestiMasterDTO.setNightParkPlace(rs.getString("inv_night_park_place"));
				flyingSquadInvestiMasterDTO.setRemarks(rs.getString("inv_remarks"));
				flyingSquadInvestiMasterDTO.setEndtime(rs.getTimestamp("inv_end_time"));
				flyingSquadInvestiMasterDTO.setStatus(rs.getString("inv_status"));
				flyingSquadInvestiMasterDTO
						.setGroupName(rs.getString("inv_group_cd") + "-" + rs.getString("group_name"));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return flyingSquadInvestiMasterDTO;

	}

	// ----get details of group members---
	public ArrayList<FlyingSquadInvestiDetailDTO> getmemberDetails(String refNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		ArrayList<FlyingSquadInvestiDetailDTO> flyingSquadInvestiDetailList = new ArrayList<FlyingSquadInvestiDetailDTO>();

		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT inv_det_name, inv_det_designation, inv_det_flag,inv_det_empno\r\n"
					+ "FROM public.nt_t_flying_investigation_details\r\n" + "where inv_det_refno = ?";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, refNo);
			rs = stmt.executeQuery();

			while (rs.next()) {
				FlyingSquadInvestiDetailDTO flyingSquadInvestiDetailDTO = new FlyingSquadInvestiDetailDTO();
				flyingSquadInvestiDetailDTO.setName(rs.getString("inv_det_name"));
				flyingSquadInvestiDetailDTO.setDesignation(rs.getString("inv_det_designation"));
				flyingSquadInvestiDetailDTO.setAllow(rs.getBoolean("inv_det_flag"));
				flyingSquadInvestiDetailDTO.setEmpNo(rs.getString("inv_det_empno"));

				flyingSquadInvestiDetailList.add(flyingSquadInvestiDetailDTO);

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return flyingSquadInvestiDetailList;
	}

	// ---update save data---
	public void updatedetail(ArrayList<FlyingSquadInvestiDetailDTO> detail, String user, String refNo) {
		Connection con = null;
		PreparedStatement stmt = null;

		try {

			java.util.Date date = new java.util.Date();
			Timestamp timeStamp = new Timestamp(date.getTime());

			for (FlyingSquadInvestiDetailDTO flyingSquadInvestiDetailDTO : detail) {

				con = ConnectionManager.getConnection();
				String sql = "UPDATE public.nt_t_flying_investigation_details\r\n"
						+ "SET inv_det_flag=?,inv_det_modified_by=?, inv_det_modified_date=?\r\n"
						+ "where inv_det_refno = ?\r\n" + "and inv_det_empno = ?";

				stmt = con.prepareStatement(sql);
				stmt.setBoolean(1, flyingSquadInvestiDetailDTO.isAllow());
				stmt.setString(2, user);
				stmt.setTimestamp(3, timeStamp);
				stmt.setString(4, refNo);
				stmt.setString(5, flyingSquadInvestiDetailDTO.getEmpNo());

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

	public void updatemasterData(FlyingSquadInvestiMasterDTO flyingSquadInvestiMasterDTO, String user) {
		Connection con = null;
		PreparedStatement stmt = null;

		try {

			java.util.Date date = new java.util.Date();
			Timestamp timeStamp = new Timestamp(date.getTime());
			java.sql.Date sqlDate = new java.sql.Date(flyingSquadInvestiMasterDTO.getInvestigationDate().getTime());
			SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
			String startTime = localDateFormat.format(flyingSquadInvestiMasterDTO.getStartTime());
			Timestamp ts = new Timestamp(flyingSquadInvestiMasterDTO.getEndtime().getTime());

			con = ConnectionManager.getConnection();
			String sql = "UPDATE public.nt_t_flying_investigation_master\r\n"
					+ "SET  inv_date=?, inv_start_time=?, \r\n"
					+ "inv_vehicle_no=?, inv_driver_name=?, inv_start_place=?, inv_route=?, \r\n"
					+ "inv_end_place=?, inv_night_park_place=?, inv_remarks=?,\r\n"
					+ "inv_modified_by=?, inv_modified_date=?, inv_end_time=?\r\n" + "where inv_refno= ?";

			stmt = con.prepareStatement(sql);
			stmt.setDate(1, sqlDate);
			stmt.setString(2, startTime);
			stmt.setString(3, flyingSquadInvestiMasterDTO.getVehicleNo());
			stmt.setString(4, flyingSquadInvestiMasterDTO.getDriverName());
			stmt.setString(5, flyingSquadInvestiMasterDTO.getStartPlace());
			stmt.setString(6, flyingSquadInvestiMasterDTO.getInvestigationDetails());
			stmt.setString(7, flyingSquadInvestiMasterDTO.getEndPlace());
			stmt.setString(8, flyingSquadInvestiMasterDTO.getNightParkPlace());
			stmt.setString(9, flyingSquadInvestiMasterDTO.getRemarks());
			stmt.setString(10, user);
			stmt.setTimestamp(11, timeStamp);
			stmt.setTimestamp(12, ts);
			stmt.setString(13, flyingSquadInvestiMasterDTO.getRefNo());

			stmt.executeUpdate();
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

	}

	// get reference no list
	public ArrayList<FlyingSquadInvestiMasterDTO> getrefNo() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		ArrayList<FlyingSquadInvestiMasterDTO> refnoList = new ArrayList<FlyingSquadInvestiMasterDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT inv_refno\r\n" + "FROM public.nt_t_flying_investigation_master\r\n"
					+ "where inv_status in ('P','R')\r\n" + "order by inv_refno";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {

				FlyingSquadInvestiMasterDTO flyingSquadInvestiMasterDTO = new FlyingSquadInvestiMasterDTO();
				flyingSquadInvestiMasterDTO.setRefNo(rs.getString("inv_refno"));

				refnoList.add(flyingSquadInvestiMasterDTO);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return refnoList;

	}

	// get reference no list
	public ArrayList<FlyingSquadInvestiMasterDTO> getrefNoforInvestigation() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		ArrayList<FlyingSquadInvestiMasterDTO> refnoList = new ArrayList<FlyingSquadInvestiMasterDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT inv_refno\r\n" + "FROM public.nt_t_flying_investigation_master\r\n"
					+ "where inv_status in ('P','R','A')\r\n" + "order by inv_refno";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {

				FlyingSquadInvestiMasterDTO flyingSquadInvestiMasterDTO = new FlyingSquadInvestiMasterDTO();
				flyingSquadInvestiMasterDTO.setRefNo(rs.getString("inv_refno"));

				refnoList.add(flyingSquadInvestiMasterDTO);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return refnoList;

	}

	public String validateRefNo(String refNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String refNo1 = null;

		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT inv_refno " + " FROM public.nt_t_flying_investigation_master "
					+ " WHERE inv_refno = ? "
					+ " AND inv_end_time >=  to_date(to_char(now(),'dd/MM/yyyy'),'dd/MM/yyyy')";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, refNo);
			rs = stmt.executeQuery();

			while (rs.next()) {
				refNo1 = rs.getString("inv_refno");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return refNo1;
	}

}
