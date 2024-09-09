package lk.informatics.ntc.model.service;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import lk.informatics.ntc.model.dto.DriverConductorRegistrationDTO;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.roc.utils.common.Utils;

public class UploadDriverConductorPhotoServiceImpl implements UploadDriverConductorPhotoService, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1470776012983604599L;

	@Override
	public List<DriverConductorRegistrationDTO> getApplicationNoList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<DriverConductorRegistrationDTO> appNoList = new ArrayList<DriverConductorRegistrationDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select dcr_app_no,dcr_nic from public.nt_t_driver_conductor_registration where dcr_status not in('I') order by dcr_app_no ";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				DriverConductorRegistrationDTO appNo = new DriverConductorRegistrationDTO();
				appNo.setAppNo(rs.getString("dcr_app_no"));
				appNo.setNic(rs.getString("dcr_nic"));

				appNoList.add(appNo);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return appNoList;
	}

	@Override
	public List<DriverConductorRegistrationDTO> getDriverIdList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<DriverConductorRegistrationDTO> driverIDList = new ArrayList<DriverConductorRegistrationDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct dcr_driver_conductor_id from  public.nt_t_driver_conductor_registration \r\n"
					+ "where dcr_status not in('I')\r\n"
					+ "and dcr_training_type in ('ND','RD','RRD','RRRD','DD','FD')";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				DriverConductorRegistrationDTO driverId = new DriverConductorRegistrationDTO();
				driverId.setDriverId(rs.getString("dcr_driver_conductor_id"));

				driverIDList.add(driverId);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return driverIDList;
	}

	@Override
	public List<DriverConductorRegistrationDTO> getConductorIdList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<DriverConductorRegistrationDTO> conductorIDList = new ArrayList<DriverConductorRegistrationDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct dcr_driver_conductor_id from  public.nt_t_driver_conductor_registration \r\n"
					+ "where dcr_status not in('I')\r\n"
					+ "and dcr_training_type in ('NC','RC','RRC','RRRC','DC','FC')";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				DriverConductorRegistrationDTO conductorId = new DriverConductorRegistrationDTO();
				conductorId.setConductorId(rs.getString("dcr_driver_conductor_id"));

				conductorIDList.add(conductorId);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return conductorIDList;
	}

	@Override
	public boolean checkAvailabilityOfPhoto(DriverConductorRegistrationDTO uploadPhotoDTO) {

		boolean isPhotoHave = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		DriverConductorRegistrationDTO dto = new DriverConductorRegistrationDTO();
		try {
			con = ConnectionManager.getConnection();

			String sql = "select * from public.nt_t_driver_conductor_photoupload\r\n"
					+ "where dcp_photo is not null\r\n" + "and dcp_app_no=?\r\n" + "and dcp_nic_no=?;";

			ps = con.prepareStatement(sql);
			ps.setString(1, uploadPhotoDTO.getAppNo());
			ps.setString(2, uploadPhotoDTO.getNic());

			rs = ps.executeQuery();

			while (rs.next()) {
				dto.setAppNo(rs.getString("dcp_app_no"));
				dto.setImage(rs.getBytes("dcp_photo"));

			}
			if (dto.getImage() != null) {
				isPhotoHave = true;

			} else {

				isPhotoHave = false;
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return isPhotoHave;
	}

	@Override
	public int insertDataIntoPhotoUploadTable(DriverConductorRegistrationDTO uploadPhotoDTO, String loginUser) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();
			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_driver_conductor_photoupload");
			String sql = "INSERT INTO public.nt_t_driver_conductor_photoupload\r\n"
					+ "(dcp_seq, dcp_app_no, dcp_nic_no, dcp_driver_id, dcp_conductor_id, dcp_fullname, dcp_photo,created_by,created_date)\r\n"
					+ "VALUES(?, ?, ?, ?, ?, ?, ?,?,?);";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, uploadPhotoDTO.getAppNo());
			stmt.setString(3, uploadPhotoDTO.getNic());
			stmt.setString(4, uploadPhotoDTO.getDriverId());
			stmt.setString(5, uploadPhotoDTO.getConductorId());
			stmt.setString(6, uploadPhotoDTO.getFullName());
			stmt.setBytes(7, uploadPhotoDTO.getImage());
			stmt.setString(8, loginUser);
			stmt.setTimestamp(9, timestamp);

			stmt.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return 0;
	}

	@Override
	public DriverConductorRegistrationDTO showimage(String appNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		DriverConductorRegistrationDTO dto = new DriverConductorRegistrationDTO();
		try {
			con = ConnectionManager.getConnection();

			String sql = "select * from public.nt_t_driver_conductor_photoupload\r\n"
					+ "where dcp_photo is not null\r\n" + "and dcp_app_no=?;";

			ps = con.prepareStatement(sql);
			ps.setString(1, appNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				dto.setImage(rs.getBytes("dcp_photo"));
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return dto;
	}

	@Override
	public void deleteSelectedPhoto(DriverConductorRegistrationDTO uploadPhotoDTO, String loginUser) {

		Connection con = null;
		PreparedStatement stmt2 = null, stmt1 = null, stmt3 = null;
		ResultSet rs1 = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_h_driver_conductor_photoupload");

			String sql1 = "SELECT  dcp_app_no, dcp_nic_no, dcp_driver_id, dcp_conductor_id, dcp_fullname, dcp_photo\r\n"
					+ "FROM public.nt_t_driver_conductor_photoupload where dcp_app_no =? and dcp_nic_no=? and dcp_photo=? ;";

			stmt1 = con.prepareStatement(sql1);
			stmt1.setString(1, uploadPhotoDTO.getAppNo());
			stmt1.setString(2, uploadPhotoDTO.getNic());
			stmt1.setBytes(3, uploadPhotoDTO.getImage());
			rs1 = stmt1.executeQuery();
			DriverConductorRegistrationDTO insertDTO = new DriverConductorRegistrationDTO();
			while (rs1.next()) {
				insertDTO.setAppNo(rs1.getString("dcp_app_no"));
				insertDTO.setNic(rs1.getString("dcp_nic_no"));
				insertDTO.setDriverId(rs1.getString("dcp_driver_id"));
				insertDTO.setConductorId(rs1.getString("dcp_conductor_id"));
				insertDTO.setFullName(rs1.getString("dcp_fullname"));
				insertDTO.setImage(rs1.getBytes("dcp_photo"));
			}

			String sql2 = "INSERT INTO public.nt_h_driver_conductor_photoupload\r\n"
					+ "(dcp_seq, dcp_app_no, dcp_nic_no, dcp_driver_id, dcp_conductor_id, dcp_fullname, dcp_photo,created_by,created_date)\r\n"
					+ "VALUES(?, ?, ?, ?, ?, ?, ?,?,?);";

			stmt2 = con.prepareStatement(sql2);
			stmt2.setLong(1, seqNo);
			stmt2.setString(2, insertDTO.getAppNo());
			stmt2.setString(3, insertDTO.getNic());
			stmt2.setString(4, insertDTO.getDriverId());
			stmt2.setString(5, insertDTO.getConductorId());
			stmt2.setString(6, insertDTO.getFullName());
			stmt2.setBytes(7, insertDTO.getImage());
			stmt2.setString(8, loginUser);
			stmt2.setTimestamp(9, timestamp);

			stmt2.executeUpdate();
			con.commit();
			String sql3 = "DELETE FROM  public.nt_t_driver_conductor_photoupload"
					+ " WHERE dcp_app_no=? and dcp_nic_no=?;";

			stmt3 = con.prepareStatement(sql3);

			stmt3.setString(1, uploadPhotoDTO.getAppNo());
			stmt3.setString(2, uploadPhotoDTO.getNic());

			stmt3.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs1);
			ConnectionManager.close(stmt1);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(stmt3);
			ConnectionManager.close(con);

		}

		return;

	}

	@Override
	public DriverConductorRegistrationDTO getDataForFilters(DriverConductorRegistrationDTO uploadPhotoDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		DriverConductorRegistrationDTO dto = new DriverConductorRegistrationDTO();

		String WHERE_SQL = "";
		boolean whereadded = false;
		if (uploadPhotoDTO.getAppNo() != null && !uploadPhotoDTO.getAppNo().equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE dcr_app_no = " + "'" + uploadPhotoDTO.getAppNo() + "'";
			whereadded = true;
		}

		if (uploadPhotoDTO.getDriverId() != null && !uploadPhotoDTO.getDriverId().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND dcr_driver_conductor_id  = " + "'" + uploadPhotoDTO.getDriverId() + "'";
			} else {
				WHERE_SQL = WHERE_SQL + " WHERE dcr_driver_conductor_id  = " + "'" + uploadPhotoDTO.getDriverId() + "'";
				whereadded = true;
			}
		}

		if (uploadPhotoDTO.getConductorId() != null && !uploadPhotoDTO.getConductorId().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND dcr_driver_conductor_id  = " + "'" + uploadPhotoDTO.getConductorId()
						+ "'";
			} else {
				WHERE_SQL = WHERE_SQL + " WHERE dcr_driver_conductor_id  = " + "'" + uploadPhotoDTO.getConductorId()
						+ "'";
				whereadded = true;
			}
		}

		if (uploadPhotoDTO.getFullName() != null && !uploadPhotoDTO.getFullName().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND dcr_full_name_eng = " + "'" + uploadPhotoDTO.getFullName() + "'";
			} else {
				WHERE_SQL = WHERE_SQL + " WHERE dcr_full_name_eng = " + "'" + uploadPhotoDTO.getFullName() + "'";
				whereadded = true;
			}
		}

		try {

			con = ConnectionManager.getConnection();

			String query = "select dcr_app_no,dcr_nic,dcr_full_name_eng,dcr_driver_conductor_id,dcr_training_type "
					+ "from public.nt_t_driver_conductor_registration " + WHERE_SQL;

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				dto = new DriverConductorRegistrationDTO();

				dto.setAppNo(rs.getString("dcr_app_no"));

				dto.setNic(rs.getString("dcr_nic"));
				dto.setFullName(rs.getString("dcr_full_name_eng"));
				if ((rs.getString("dcr_training_type").equals("ND") || rs.getString("dcr_training_type").equals("FD")
						|| rs.getString("dcr_training_type").equals("DD")
						|| rs.getString("dcr_training_type").equals("RD")
						|| rs.getString("dcr_training_type").equals("RRD")
						|| rs.getString("dcr_training_type").equals("RRRD"))) {
					dto.setDriverId(rs.getString("dcr_driver_conductor_id"));
				} else if (rs.getString("dcr_training_type").equals("NC")
						|| rs.getString("dcr_training_type").equals("FC")
						|| rs.getString("dcr_training_type").equals("DC")
						|| rs.getString("dcr_training_type").equals("RC")
						|| rs.getString("dcr_training_type").equals("RRC")
						|| rs.getString("dcr_training_type").equals("RRRC")) {

					dto.setConductorId(rs.getString("dcr_driver_conductor_id"));
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return dto;

	}

	@Override
	public List<DriverConductorRegistrationDTO> getDriverIdListFromId(DriverConductorRegistrationDTO uploadPhotoDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<DriverConductorRegistrationDTO> driverIDList = new ArrayList<DriverConductorRegistrationDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct dcr_driver_conductor_id from  public.nt_t_driver_conductor_registration \r\n"
					+ "where dcr_status not in('I') and dcr_nic =?\r\n"
					+ "and dcr_training_type in ('ND','RD','RRD','RRRD','DD','FD')";
			ps = con.prepareStatement(query);
			ps.setString(1, uploadPhotoDTO.getNic());
			rs = ps.executeQuery();

			while (rs.next()) {
				DriverConductorRegistrationDTO driverId = new DriverConductorRegistrationDTO();
				driverId.setDriverId(rs.getString("dcr_driver_conductor_id"));

				driverIDList.add(driverId);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return driverIDList;
	}

	@Override
	public List<DriverConductorRegistrationDTO> getConductorIdListFromId(
			DriverConductorRegistrationDTO uploadPhotoDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<DriverConductorRegistrationDTO> conductorIDList = new ArrayList<DriverConductorRegistrationDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct dcr_driver_conductor_id from  public.nt_t_driver_conductor_registration \r\n"
					+ "where dcr_status not in('I') and dcr_nic =? \r\n"
					+ "and dcr_training_type in ('NC','RC','RRC','RRRC','DC','FC')";
			ps = con.prepareStatement(query);
			ps.setString(1, uploadPhotoDTO.getNic());
			rs = ps.executeQuery();

			while (rs.next()) {
				DriverConductorRegistrationDTO conductorId = new DriverConductorRegistrationDTO();
				conductorId.setConductorId(rs.getString("dcr_driver_conductor_id"));

				conductorIDList.add(conductorId);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return conductorIDList;
	}

	@Override
	public List<DriverConductorRegistrationDTO> getApplicationNoListFromId(
			DriverConductorRegistrationDTO uploadPhotoDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<DriverConductorRegistrationDTO> appNoList = new ArrayList<DriverConductorRegistrationDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select dcr_app_no,dcr_nic from public.nt_t_driver_conductor_registration where dcr_status not in('I') and dcr_nic=? order by dcr_app_no ";
			ps = con.prepareStatement(query);
			ps.setString(1, uploadPhotoDTO.getNic());
			rs = ps.executeQuery();

			while (rs.next()) {
				DriverConductorRegistrationDTO appNo = new DriverConductorRegistrationDTO();
				appNo.setAppNo(rs.getString("dcr_app_no"));
				appNo.setNic(rs.getString("dcr_nic"));

				appNoList.add(appNo);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return appNoList;
	}

	@Override
	public List<DriverConductorRegistrationDTO> getAppNoByDriverOrConduc(String s) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "";

		List<DriverConductorRegistrationDTO> appNoList = new ArrayList<DriverConductorRegistrationDTO>();
		try {
			con = ConnectionManager.getConnection();

			if (s.equalsIgnoreCase("d")) {
				query = "select dcr_app_no,dcr_nic from public.nt_t_driver_conductor_registration where dcr_status not in('I') and dcr_training_type in ('ND','RD','RRD','RRRD','DD','FD') order by dcr_app_no ";

			}

			else if (s.equalsIgnoreCase("c")) {
				query = "select dcr_app_no,dcr_nic from public.nt_t_driver_conductor_registration where dcr_status not in('I') and dcr_training_type in ('NC','RC','RRC','RRRC','DC','FC') order by dcr_app_no ";
			}
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				DriverConductorRegistrationDTO appNo = new DriverConductorRegistrationDTO();
				appNo.setAppNo(rs.getString("dcr_app_no"));
				appNo.setNic(rs.getString("dcr_nic"));

				appNoList.add(appNo);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return appNoList;
	}

	@Override
	public boolean checkAvailabilityOfPhotoByNIC(String id) {

		boolean isPhotoHave = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		DriverConductorRegistrationDTO dto = new DriverConductorRegistrationDTO();
		try {
			con = ConnectionManager.getConnection();

			String sql = "select * from public.nt_t_driver_conductor_photoupload\r\n"
					+ "where dcp_photo is not null\r\n" + "and dcp_nic_no=?;";

			ps = con.prepareStatement(sql);
			ps.setString(1, id);

			rs = ps.executeQuery();

			while (rs.next()) {

				dto.setImage(rs.getBytes("dcp_photo"));

			}
			if (dto.getImage() != null) {
				isPhotoHave = true;

			} else {

				isPhotoHave = false;
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return isPhotoHave;
	}

	@Override
	public String getAppNoByNIC(String id) {

		String appNo = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();

			String sql = "select * from public.nt_t_driver_conductor_photoupload\r\n"
					+ "where dcp_photo is not null\r\n" + "and dcp_nic_no=?;";

			ps = con.prepareStatement(sql);
			ps.setString(1, id);

			rs = ps.executeQuery();

			while (rs.next()) {

				appNo = rs.getString("dcp_app_no");

			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return appNo;
	}

	@Override
	public DriverConductorRegistrationDTO showimageByNIC(String nicNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		DriverConductorRegistrationDTO dto = new DriverConductorRegistrationDTO();
		try {
			con = ConnectionManager.getConnection();

			String sql = "select * from public.nt_t_driver_conductor_photoupload\r\n"
					+ "where dcp_photo is not null\r\n" + "and dcp_nic_no=?;";

			ps = con.prepareStatement(sql);
			ps.setString(1, nicNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				dto.setImage(rs.getBytes("dcp_photo"));
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return dto;
	}
	
	private void insertTaskInquiryRecord(Connection con, DriverConductorRegistrationDTO registrationDto,
			Timestamp timestamp, String status, String function,String user, String functiondes) {
		
		try {
			String insertSQL = null;
			PreparedStatement psInsert = null;

			insertSQL = "INSERT INTO public.nt_t_task_inquiry (created_by, created_date, status, bus_no, dc_id_num, permit_no,function_name,function_des,nic_no) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

			psInsert = con.prepareStatement(insertSQL);

			psInsert.setString(1, user);
			psInsert.setTimestamp(2, timestamp);
			psInsert.setString(3, status);
			psInsert.setString(4, registrationDto.getVehicleRegNo());
			psInsert.setString(5, registrationDto.getDriverConductorId());
			psInsert.setString(6, registrationDto.getPermitNo());
			psInsert.setString(7, function);
			psInsert.setString(8, functiondes);
			psInsert.setString(9, registrationDto.getNic());

			psInsert.executeUpdate();
			con.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public void beanLinkMethod(DriverConductorRegistrationDTO registrationDto,String user,String des,String funDes) {
		Connection con = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		
		try {
			con = ConnectionManager.getConnection();
			
			insertTaskInquiryRecord(con, registrationDto, timestamp, des, "Driver Conductor Management",user,funDes);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

}
