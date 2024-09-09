package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.informatics.ntc.model.dto.EmployeeDTO;
import lk.informatics.ntc.model.dto.PermitDTO;
import lk.informatics.ntc.model.dto.PermitRenewalsDTO;
import lk.informatics.ntc.model.dto.VehicleInspectionDTO;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.ntc.view.util.SpringApplicationContex;
import lk.informatics.roc.utils.common.Utils;

public class PermitServiceImpl implements PermitService {

	private static final long serialVersionUID = 1L;

	private CommonService commonService;
	private MigratedService migratedService;

	// for New PermitPrintingBacking Bean
	@Override
	public List<PermitDTO> getAppNoToDropdown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PermitDTO> returnList = new ArrayList<PermitDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct a.pm_application_no, a.pm_vehicle_regno "
					+ " from nt_t_pm_application a inner join nt_t_task_det c on a.pm_application_no = c.tsd_app_no "
					+ " where (c.tsd_task_code = 'PM200' and c.tsd_status='C' or c.tsd_task_code = 'PM302' and c.tsd_status='C' or c.tsd_task_code = 'PM400' and c.tsd_status='C'"
					+ " or c.tsd_task_code = 'PM400' and c.tsd_status='O') and a.pm_tran_type ='03' ";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				PermitDTO permitDTO = new PermitDTO();
				permitDTO.setApplicationNo(rs.getString("pm_application_no"));
				permitDTO.setBusRegNo(rs.getString("pm_vehicle_regno"));

				if (getAppNoList(permitDTO.getBusRegNo()) < 2) {
					returnList.add(permitDTO);
				}

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

	public int getAppNoList(String vehicleNo) {
		int count = 0;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT count(pm_vehicle_regno)as count FROM public.nt_t_pm_application  where pm_vehicle_regno=?";

			ps = con.prepareStatement(query);
			ps.setString(1, vehicleNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				count = rs.getInt("count");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return count;
	}

	@Override
	public List<VehicleInspectionDTO> getServiceTypeToDropdown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<VehicleInspectionDTO> returnList = new ArrayList<VehicleInspectionDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code,description FROM public.nt_r_service_types ;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				VehicleInspectionDTO vehicleInspectionDTO = new VehicleInspectionDTO();
				vehicleInspectionDTO.setServiceTypeCode(rs.getString("code"));
				vehicleInspectionDTO.setServiceTypeDescription(rs.getString("description"));

				returnList.add(vehicleInspectionDTO);

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
	public List<String> getTaskApplicationNo() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<String> applicationNoList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct nt_t_pm_application.pm_permit_no,nt_t_pm_application.pm_application_no , nt_t_task_det.tsd_app_no from nt_t_task_det "
					+ "inner join public.nt_t_pm_application on public.nt_t_pm_application.pm_application_no=nt_t_task_det.tsd_app_no "
					+ "inner join public.nt_h_task_his on public.nt_h_task_his.tsd_app_no=nt_t_task_det.tsd_app_no "
					+ "where ((nt_h_task_his.tsd_task_code='PR200' and nt_h_task_his.tsd_status='C' "
					+ "or nt_h_task_his.tsd_task_code='PM302' and nt_h_task_his.tsd_status='C' "
					+ "or nt_t_task_det.tsd_task_code='PM302' and nt_t_task_det.tsd_status='C' "
					+ "or nt_t_task_det.tsd_task_code='PM201' and nt_t_task_det.tsd_status='O' "
					+ "or nt_t_task_det.tsd_task_code='PR200' and nt_t_task_det.tsd_status='C') "
					+ "and (nt_t_pm_application.pm_permit_no is not null ) "
					+ "and nt_t_task_det.tsd_app_no LIKE '%PAP%' "
					+ ") order by nt_t_pm_application.pm_application_no  ";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {

				applicationNoList.add(rs.getString("tsd_app_no"));

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return applicationNoList;
	}

	@Override
	public List<String> getApplicationNo() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<String> applicationNoList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT pm_application_no FROM public.nt_t_pm_application where pm_application_no "
					+ "IS NOT NULL and pm_application_no != '' and pm_status='O'  ORDER BY pm_application_no ;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				applicationNoList.add(rs.getString("pm_application_no"));

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return applicationNoList;
	}

	@Override
	public boolean checkTaskDetails(PermitDTO dto, String taskCode, String status) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isTaskAvailable = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "select  tsd_app_no, tsd_task_code, tsd_status " + "FROM public.nt_t_task_det "
					+ "where tsd_task_code=? and tsd_status=? and tsd_app_no=? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, taskCode);
			ps.setString(2, status);
			ps.setString(3, dto.getApplicationNo());
			rs = ps.executeQuery();

			if (rs.next() == true) {
				isTaskAvailable = true;
			} else {
				isTaskAvailable = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isTaskAvailable;
	}

	@Override
	public boolean CopyTaskDetailsANDinsertTaskHistory(PermitDTO dto, String loginUSer, String taskCode) {
		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		PermitDTO permitDTO = null;

		boolean isUpdate = false;

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT tsd_seq, tsd_vehicle_no, tsd_app_no, tsd_task_code, tsd_status, created_by, created_date "
					+ "FROM public.nt_t_task_det " + "WHERE tsd_app_no=? AND tsd_task_code=? ;";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, dto.getApplicationNo());
			stmt.setString(2, taskCode);

			rs = stmt.executeQuery();

			if (rs.next()) {

				isUpdate = true;

				permitDTO = new PermitDTO();
				permitDTO.setTaskSeq(rs.getLong("tsd_seq"));
				permitDTO.setBusRegNo(rs.getString("tsd_vehicle_no"));
				permitDTO.setApplicationNo(rs.getString("tsd_app_no"));
				permitDTO.setTaskCode(rs.getString("tsd_task_code"));
				permitDTO.setStatus(rs.getString("tsd_status"));
				permitDTO.setCreatedBy(rs.getString("created_by"));
				permitDTO.setCratedDate(rs.getTimestamp("created_date"));

				String sql2 = "INSERT INTO public.nt_h_task_his "
						+ "(tsd_seq, tsd_vehicle_no, tsd_app_no, tsd_task_code, tsd_status, created_by, created_date) "
						+ "VALUES(? , ?, ?, ?, ?, ?, ?); " + "";

				stmt2 = con.prepareStatement(sql2);

				stmt2.setLong(1, permitDTO.getTaskSeq());
				stmt2.setString(2, permitDTO.getBusRegNo());
				stmt2.setString(3, permitDTO.getApplicationNo());
				stmt2.setString(4, permitDTO.getTaskCode());
				stmt2.setString(5, permitDTO.getStatus());
				stmt2.setString(6, permitDTO.getCreatedBy());
				stmt2.setTimestamp(7, permitDTO.getCratedDate());

				stmt2.executeUpdate();

			} else {
				isUpdate = false;

			}

			commonService = (CommonService) SpringApplicationContex.getBean("commonService");
			migratedService = (MigratedService) SpringApplicationContex.getBean("migratedService");
			String queueNumber = migratedService.findQueueNumberFromApplicationNo(permitDTO.getApplicationNo());
			migratedService.updateQueueNumberTaskInQueueMaster(queueNumber, permitDTO.getApplicationNo(),
					permitDTO.getTaskCode(), permitDTO.getStatus());

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(con);

		}
		return isUpdate;
	}

	@Override
	public boolean insertTaskHistory(PermitDTO dto, String loginUSer, String taskCode, String status) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateTaskDetails(PermitDTO dto, String taskCode, String status) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_t_task_det " + "SET tsd_status=? "
					+ "WHERE tsd_app_no=? AND tsd_task_code=?; ";

			ps = con.prepareStatement(query);
			ps.setString(1, status);
			ps.setString(2, dto.getApplicationNo());
			ps.setString(3, taskCode);
			ps.executeUpdate();

			con.commit();

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return false;
	}

	@Override
	public boolean insertTaskDetails(PermitDTO dto, String loginguser, String taskCode, String status) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_task_det");

			String sql = "INSERT INTO public.nt_t_task_det "
					+ "(tsd_seq, tsd_vehicle_no, tsd_app_no, tsd_task_code, tsd_status, created_by, created_date) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?);";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, dto.getBusRegNo());
			stmt.setString(3, dto.getApplicationNo());
			stmt.setString(4, taskCode);
			stmt.setString(5, status);
			stmt.setString(6, loginguser);
			stmt.setTimestamp(7, timestamp);

			stmt.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
		return false;
	}

	@Override
	public boolean deleteTaskDetails(PermitDTO dto, String taskCode) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isTaskPR200Delete = false;

		try {
			con = ConnectionManager.getConnection();

			String query = "DELETE FROM public.nt_t_task_det WHERE tsd_app_no=? AND tsd_task_code=? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, dto.getApplicationNo());
			ps.setString(2, taskCode);
			int i = ps.executeUpdate();

			if (i > 0) {
				isTaskPR200Delete = true;

			} else {
				isTaskPR200Delete = false;

			}

			con.commit();

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isTaskPR200Delete;
	}

	@Override
	public List<String> getTaskPermitNo() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<String> permitNoList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct nt_t_pm_application.pm_permit_no, nt_t_task_det.tsd_app_no from nt_t_task_det "
					+ "inner join public.nt_t_pm_application on public.nt_t_pm_application.pm_application_no=nt_t_task_det.tsd_app_no "
					+ "inner join public.nt_h_task_his on public.nt_h_task_his.tsd_app_no=nt_t_task_det.tsd_app_no "
					+ "where ((nt_h_task_his.tsd_task_code='PR200' and nt_h_task_his.tsd_status='C' "
					+ "or nt_h_task_his.tsd_task_code='PM302' and nt_h_task_his.tsd_status='C' "
					+ "or nt_t_task_det.tsd_task_code='PM302' and nt_t_task_det.tsd_status='C' "
					+ "or nt_t_task_det.tsd_task_code='PM201' and nt_t_task_det.tsd_status='O' "
					+ "or nt_t_task_det.tsd_task_code='PR200' and nt_t_task_det.tsd_status='C') "
					+ "and (nt_t_pm_application.pm_permit_no is not null) "
					+ ") order by nt_t_pm_application.pm_permit_no ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				String permitNo = rs.getString("pm_permit_no");

				if (permitNo != null && permitNo.trim().equalsIgnoreCase("")) {

				}

				permitNoList.add(permitNo);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return permitNoList;
	}

	@Override
	public List<String> getPermitNo() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<String> permitNoList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct pm_permit_no FROM public.nt_t_pm_application  where pm_permit_no is not null and pm_permit_no != '' ORDER BY pm_permit_no;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				String permitNo = rs.getString("pm_permit_no");

				if (permitNo != null && permitNo.trim().equalsIgnoreCase("")) {

				}

				permitNoList.add(permitNo);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return permitNoList;
	}

	@Override
	public String getBusRegNo(String applicationNO, String permitNo, String queueNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String regNo = null;

		String WHERE_SQL = "";
		boolean whereadded = false;
		if (permitNo != null && !permitNo.equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE  pm_permit_no = " + "'" + permitNo + "'";
			whereadded = true;
		}
		if (applicationNO != null && !applicationNO.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND pm_application_no = " + "'" + applicationNO + "'";
			} else {
				WHERE_SQL = WHERE_SQL + " WHERE pm_application_no = " + "'" + applicationNO + "'";
				whereadded = true;
			}
		}
		if (queueNo != null && !queueNo.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND pm_queue_no = " + "'" + queueNo + "'";
			} else {
				WHERE_SQL = WHERE_SQL + " WHERE  pm_queue_no = " + "'" + queueNo + "'";
				whereadded = true;
			}
		}

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT pm_vehicle_regno FROM public.nt_t_pm_application " + WHERE_SQL;

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				regNo = rs.getString("pm_vehicle_regno");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return regNo;
	}

	@Override
	public String BusRegNoUsingPermitNo(String PermitNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String regNo = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT pm_vehicle_regno FROM public.nt_t_pm_application WHERE pm_permit_no=? and pm_permit_no is not null  ;";

			ps = con.prepareStatement(query);
			ps.setString(1, PermitNo);
			rs = ps.executeQuery();

			while (rs.next()) {

				regNo = rs.getString("pm_vehicle_regno");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return regNo;
	}

	@Override
	public String completeApplicationNo(String permitNo, String queueNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String applicationNo = null;

		String WHERE_SQL = "";
		boolean whereadded = false;

		if (permitNo != null && !permitNo.equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE pm_permit_no = " + "'" + permitNo + "'";
			whereadded = true;
		}
		if (queueNo != null && !queueNo.equals("")) {
			if (whereadded) {

				WHERE_SQL = WHERE_SQL + " AND pm_queue_no = " + "'" + queueNo + "'";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE pm_queue_no = " + "'" + queueNo + "'";
				whereadded = true;
			}
		}

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT pm_application_no FROM public.nt_t_pm_application " + WHERE_SQL;

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				applicationNo = rs.getString("pm_application_no");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return applicationNo;
	}

	@Override
	public String completePermitNo(String applicationNo, String queueNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String permitNo = null;

		String WHERE_SQL = "";
		boolean whereadded = false;

		if (applicationNo != null && !applicationNo.equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE pm_application_no = " + "'" + applicationNo + "'";
			whereadded = true;
		}
		if (queueNo != null && !queueNo.equals("")) {
			if (whereadded) {

				WHERE_SQL = WHERE_SQL + " AND pm_queue_no = " + "'" + queueNo + "'";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE pm_queue_no = " + "'" + queueNo + "'";
				whereadded = true;
			}
		}

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT pm_permit_no FROM public.nt_t_pm_application   " + WHERE_SQL;

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				permitNo = rs.getString("pm_permit_no");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return permitNo;
	}

	@Override
	public boolean checkQueueNo(String queueNO) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isQueueNoValid = false;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT pm_queue_no FROM public.nt_t_pm_application WHERE pm_queue_no=? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, queueNO);
			rs = ps.executeQuery();

			if (rs.next() == true) {
				isQueueNoValid = true;
			} else {
				isQueueNoValid = false;
			}
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return isQueueNoValid;
	}

	@Override
	public String completeQueueNo(String applicationNo, String permitNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String queueNo = null;

		String WHERE_SQL = "";
		boolean whereadded = false;
		if (permitNo != null && !permitNo.equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE pm_permit_no = " + "'" + permitNo + "'";
			whereadded = true;
		}
		if (applicationNo != null && !applicationNo.equals("")) {
			if (whereadded) {

				WHERE_SQL = WHERE_SQL + " AND pm_application_no = " + "'" + applicationNo + "'";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE pm_application_no = " + "'" + applicationNo + "'";
				whereadded = true;
			}
		}

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT pm_queue_no FROM public.nt_t_pm_application " + WHERE_SQL;

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				queueNo = rs.getString("pm_queue_no");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return queueNo;
	}

	@Override
	public List<PermitDTO> getApprovedData(PermitDTO permitDTO2) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PermitDTO> permitDTO = new ArrayList<PermitDTO>();

		try {
			con = ConnectionManager.getConnection();
			String query2 = "select distinct pm_status , pm_renewal_period, pm_application_no, pm_permit_no, pm_vehicle_regno, pm_permit_expire_date, "
					+ "nt_t_pm_vehi_owner.pmo_full_name, pm_new_permit_expiry_date FROM public.nt_t_pm_application "
					+ "inner join  public.nt_t_pm_vehi_owner on nt_t_pm_application.pm_application_no = public.nt_t_pm_vehi_owner.pmo_application_no "
					+ "inner join nt_t_task_det on nt_t_task_det.tsd_app_no= nt_t_pm_application.pm_application_no "
					+ "Where pm_status= 'O' and pm_permit_no is not null and pm_permit_no != '' and "
					+ "((tsd_task_code='PR200' and tsd_status='C') or (tsd_task_code='PM201' and tsd_status='O')) ";

			PermitDTO p;
			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();

			while (rs.next()) {

				p = new PermitDTO();
				p.setPermitNo(rs.getString("pm_permit_no"));
				p.setApplicationNo(rs.getString("pm_application_no"));
				p.setBusRegNo(rs.getString("pm_vehicle_regno"));
				p.setExpirDate(rs.getString("pm_permit_expire_date"));
				p.setNewExpiryDateVal(rs.getString("pm_new_permit_expiry_date"));
				String testDate = p.getNewExpiryDateVal();
				if (testDate != null && !testDate.trim().equalsIgnoreCase("") && !testDate.isEmpty()) {
					DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
					Date date = formatter.parse(testDate);
					p.setNewExpirDate(date);
				} else {
					String testBeforeExpiryDate = p.getExpirDate();
					DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
					Date date = formatter.parse(testBeforeExpiryDate);
					p.setNewExpirDate(date);
				}
				p.setVehicleOwner(rs.getString("pmo_full_name"));
				p.setRePeriod(rs.getInt("pm_renewal_period"));

				String status = rs.getString("pm_status");
				String approveStatus = "";

				if (status != null) {
					if (status.equals("A")) {

						approveStatus = "APPROVED";

					} else if (status.equals("R")) {

						approveStatus = "REJECTED";

					} else if (status.equals("O")) {

						approveStatus = "ONGOING";

					} else if (status.equals("I")) {

						approveStatus = "INACTIVE";
					} else {
						approveStatus = null;
					}
				}

				p.setStatus(approveStatus);

				permitDTO.add(p);
			}

			return permitDTO;

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
	}

	@Override
	public boolean checkOwnerDetails(PermitDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isOwenerDetailsAvailable = false;

		try {

			con = ConnectionManager.getConnection();
			String query2 = "select pmo_full_name FROM public.nt_t_pm_vehi_owner WHERE pmo_application_no=?";

			ps = con.prepareStatement(query2);
			ps.setString(1, dto.getApplicationNo());
			rs = ps.executeQuery();

			if (rs.next() == true) {
				isOwenerDetailsAvailable = true;
			} else {
				isOwenerDetailsAvailable = false;
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isOwenerDetailsAvailable;
	}

	@Override
	public List<PermitDTO> getData(PermitDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String WHERE_SQL = "";
		boolean whereadded = false;

		if (dto.getPermitNo() != null && !dto.getPermitNo().equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE pm_permit_no = " + "'" + dto.getPermitNo()
					+ "' and pm_permit_no is not null and pm_permit_no != ''";
			whereadded = true;
		}
		if (dto.getApplicationNo() != null && !dto.getApplicationNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND pm_application_no = " + "'" + dto.getApplicationNo() + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE pm_application_no = " + "'" + dto.getApplicationNo()
						+ "'and pm_permit_no is not null and pm_permit_no != ''";

				whereadded = true;
			}
		}
		if (dto.getQueueNo() != null && !dto.getQueueNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND pm_queue_no = " + "'" + dto.getQueueNo() + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE pm_queue_no = " + "'" + dto.getQueueNo()
						+ "' and pm_permit_no is not null and pm_permit_no != ''";

				whereadded = true;
			}
		}

		List<PermitDTO> permitDTO = new ArrayList<PermitDTO>();

		try {
			con = ConnectionManager.getConnection();
			String query2 = "select distinct pm_status,  pm_renewal_period, pm_application_no, pm_permit_no, pm_vehicle_regno, pm_permit_expire_date, nt_t_pm_vehi_owner.pmo_full_name, pm_new_permit_expiry_date"
					+ " FROM public.nt_t_pm_application "
					+ "inner join  public.nt_t_pm_vehi_owner on nt_t_pm_application.pm_application_no = public.nt_t_pm_vehi_owner.pmo_application_no"
					+ WHERE_SQL;

			PermitDTO p;
			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();

			while (rs.next()) {

				p = new PermitDTO();
				p.setPermitNo(rs.getString("pm_permit_no"));
				p.setApplicationNo(rs.getString("pm_application_no"));
				p.setBusRegNo(rs.getString("pm_vehicle_regno"));
				p.setExpirDate(rs.getString("pm_permit_expire_date"));

				p.setNewExpiryDateVal(rs.getString("pm_new_permit_expiry_date"));
				String testDate = p.getNewExpiryDateVal();
				if (testDate != null && !testDate.trim().equalsIgnoreCase("") && !testDate.isEmpty()) {
					DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
					Date date = formatter.parse(testDate);
					p.setNewExpirDate(date);
				} else {
					String testBeforeExpiryDate = p.getExpirDate();
					DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
					Date date = formatter.parse(testBeforeExpiryDate);
					p.setNewExpirDate(date);
				}
				p.setVehicleOwner(rs.getString("pmo_full_name"));
				p.setRePeriod(rs.getInt("pm_renewal_period"));
				p.setStatus(rs.getString("pm_status"));

				String status = rs.getString("pm_status");
				String approveStatus = "";

				if (status != null) {
					if (status.equals("A")) {

						approveStatus = "APPROVED";

					} else if (status.equals("R")) {

						approveStatus = "REJECTED";

					} else if (status.equals("O")) {

						approveStatus = "ONGOING";

					} else if (status.equals("I")) {

						approveStatus = "INACTIVE";
					} else {
						approveStatus = null;
					}
				}

				p.setStatus(approveStatus);

				permitDTO.add(p);
			}

			return permitDTO;

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
	}

	@Override
	public String completeDate(String applicationNo, String permitNo, String queueNO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String date = null;

		String WHERE_SQL = "";
		boolean whereadded = false;
		if (permitNo != null && !permitNo.equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE pm_permit_no = " + "'" + permitNo + "'";
			whereadded = true;
		}
		if (applicationNo != null && !applicationNo.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND pm_application_no = " + "'" + applicationNo + "'";
			} else {
				WHERE_SQL = WHERE_SQL + " WHERE pm_application_no = " + "'" + applicationNo + "'";
				whereadded = true;
			}
		}
		if (queueNO != null && !queueNO.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND pm_queue_no = " + "'" + queueNO + "'";
			} else {
				WHERE_SQL = WHERE_SQL + " WHERE pm_queue_no = " + "'" + queueNO + "'";
				whereadded = true;
			}
		}

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT pm_permit_expire_date FROM public.nt_t_pm_application " + WHERE_SQL;

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				date = rs.getString("pm_permit_expire_date");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return date;
	}

	@Override
	public boolean isReceiptGenerated(PermitDTO permitDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isReceiptGenerated = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "select  nt_t_pm_application.pm_application_no, nt_t_pm_application.pm_status "
					+ "from nt_t_task_det "
					+ "inner join public.nt_t_pm_application on public.nt_t_pm_application.pm_application_no=nt_t_task_det.tsd_app_no "
					+ "inner join public.nt_h_task_his on public.nt_h_task_his.tsd_app_no=nt_t_task_det.tsd_app_no "
					+ "where("
					+ "( nt_h_task_his.tsd_task_code='PM302' and nt_h_task_his.tsd_status='C' and  nt_h_task_his.tsd_app_no=? "
					+ "or nt_t_task_det.tsd_task_code='PM302' and nt_t_task_det.tsd_status='C'and  nt_t_task_det.tsd_app_no=?) "
					+ "and (nt_t_pm_application.pm_application_no is not null) "
					+ "and nt_t_pm_application.pm_status='A' "
					+ "and nt_t_pm_application.pm_application_no=? ) order by nt_t_pm_application.pm_permit_no";

			ps = con.prepareStatement(query);
			ps.setString(1, permitDTO.getApplicationNo());
			ps.setString(2, permitDTO.getApplicationNo());
			ps.setString(3, permitDTO.getApplicationNo());
			rs = ps.executeQuery();

			if (rs.next() == true) {
				isReceiptGenerated = true;
			} else {
				isReceiptGenerated = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isReceiptGenerated;

	}

	@Override
	public boolean checkTaskPR200(String applicationNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isAbelToVoucherGenerate = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "select  tsd_app_no, tsd_task_code, tsd_status " + "FROM public.nt_h_task_his "
					+ "where tsd_task_code='PR200' and tsd_status='C' and tsd_app_no=? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, applicationNo);
			rs = ps.executeQuery();

			if (rs.next() == true) {
				isAbelToVoucherGenerate = true;
			} else {
				isAbelToVoucherGenerate = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isAbelToVoucherGenerate;
	}

	public List<PermitDTO> search(PermitDTO permitDTO) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PermitDTO> showDetails = new ArrayList<PermitDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = " SELECT DISTINCT A.pm_application_no AS applicationNo, A.pm_service_type AS serviceType, "
					+ "A.pm_tender_ref_no AS tender_ref_no, A.pm_vehicle_regno AS busRegNo"
					+ "FROM public.nt_t_pm_application A" + "where A.pm_application_no=?";
			boolean whereadded = false;
			if (permitDTO.getApplicationNo() != null && !permitDTO.getApplicationNo().equals("")) {
				query = query + " AND A.pm_application_no='" + permitDTO.getApplicationNo() + "'";
				whereadded = true;
			}
			if (permitDTO.getServiceType() != null && !permitDTO.getServiceType().equals("")) {
				if (whereadded) {
					query = query + " AND A.pm_service_type='" + permitDTO.getServiceType() + "'";
				} else {
					query = query + " AND A.pm_service_type='" + permitDTO.getServiceType() + "'";
					whereadded = true;
				}
			}

			if (permitDTO.getTenderRefNo() != null && !permitDTO.getTenderRefNo().equals("")) {
				if (whereadded) {
					query = query + " AND A.pm_tender_ref_no='" + permitDTO.getTenderRefNo() + "'";
				} else {
					query = query + " AND A.pm_tender_ref_no='" + permitDTO.getTenderRefNo() + "'";
					whereadded = true;
				}
			}

			if (permitDTO.getBusRegNo() != null && !permitDTO.getBusRegNo().equals("")) {
				if (whereadded) {
					query = query + " AND A.pm_vehicle_regno='" + permitDTO.getBusRegNo() + "'";
				} else {
					query = query + " AND A.pm_vehicle_regno='" + permitDTO.getBusRegNo() + "'";
					whereadded = true;
				}
			}

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				PermitDTO permiDTO01 = new PermitDTO();
				permiDTO01.setApplicationNo(rs.getString("applicationNo"));
				permiDTO01.setServiceType(rs.getString("serviceType"));
				permiDTO01.setTenderRefNo(rs.getString("tender_ref_no"));
				permiDTO01.setBusRegNo(rs.getString(" busRegNo"));

				showDetails.add(permiDTO01);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return showDetails;

	}

	@Override
	public boolean isPermitApprove(PermitDTO dto) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isPermitApproved = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM public.nt_t_pm_application "
					+ "WHERE pm_application_no=? and pm_status='A' ;";

			ps = con.prepareStatement(query);
			ps.setString(1, dto.getApplicationNo());
			rs = ps.executeQuery();

			if (rs.next() == true) {
				isPermitApproved = true;
			} else {
				isPermitApproved = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isPermitApproved;
	}

	@Override
	public boolean isPermitReject(PermitDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isPermitReject = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM public.nt_t_pm_application "
					+ "WHERE pm_application_no=? and pm_status='R' ;";

			ps = con.prepareStatement(query);
			ps.setString(1, dto.getApplicationNo());
			rs = ps.executeQuery();

			if (rs.next() == true) {
				isPermitReject = true;
			} else {
				isPermitReject = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isPermitReject;
	}

	@Override
	public boolean rejectPermit(PermitDTO dto, PermitDTO dto2) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isPermitReject = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_t_pm_application SET  pm_status='R',  pm_reject_reason=? "
					+ "WHERE pm_application_no=? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, dto2.getRejectReason());
			ps.setString(2, dto.getApplicationNo());
			int a = ps.executeUpdate();

			if (a > 0) {
				isPermitReject = true;
			} else {
				isPermitReject = false;
			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isPermitReject;
	}

	@Override
	public boolean approvePermit(PermitDTO dto, String user) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		boolean isApproved = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_t_pm_application SET  pm_status='A', pm_new_permit_expiry_date=? , pm_modified_by=?, pm_modified_date=? "
					+ "WHERE pm_application_no=? ;";

			ps = con.prepareStatement(query);

			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			String strDate = dateFormat.format(dto.getNewExpirDate());

			ps.setString(1, strDate);
			ps.setString(2, user);
			ps.setTimestamp(3, timestamp);
			ps.setString(4, dto.getApplicationNo());
			int a = ps.executeUpdate();

			if (a > 0) {
				isApproved = true;
			} else {
				isApproved = false;
			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isApproved;

	}

	@Override
	public void updateTaskPM201() {
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "";

			ps = con.prepareStatement(query);
			ps.setString(2, "R");

			ps.executeUpdate();

			con.commit();

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
	}

	public List<String> getTenderRefNoToDropdown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<String> returnList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  pm_tender_ref_no FROM public.nt_t_pm_application where pm_tender_ref_no is not null order by pm_tender_ref_no ;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				returnList.add(rs.getString("pm_tender_ref_no"));

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

	// added for get Application number when insert permit queue and tender
	// number
	@Override
	public String fillAppNo(String permitNo, String queueNo, String tenderRefNo) {
		// TODO Auto-generated method stub

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String applicationNo = null;

		String WHERE_SQL = "";
		boolean whereadded = false;

		if (permitNo != null && !permitNo.equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE pm_permit_no = " + "'" + permitNo + "'";
			whereadded = true;
		}
		if (queueNo != null && !queueNo.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " OR pm_queue_no = " + "'" + queueNo + "'";
			} else {
				WHERE_SQL = WHERE_SQL + " WHERE pm_queue_no = " + "'" + queueNo + "'";
				whereadded = true;
			}
		}
		if (tenderRefNo != null && !tenderRefNo.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " OR pm_tender_ref_no = " + "'" + tenderRefNo + "'";
			} else {
				WHERE_SQL = WHERE_SQL + " WHERE pm_tender_ref_no = " + "'" + tenderRefNo + "'";
				whereadded = true;
			}
		}

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT pm_application_no FROM public.nt_t_pm_application " + WHERE_SQL;

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				applicationNo = rs.getString("pm_application_no");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return applicationNo;
	}

	@Override
	public String fillQueueNo(String applicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String queueNo = null;

		String WHERE_SQL = "";
		boolean whereadded = false;

		if (applicationNo != null && !applicationNo.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + "  pm_application_no = " + "'" + applicationNo + "'";
			} else {
				WHERE_SQL = WHERE_SQL + " WHERE pm_application_no = " + "'" + applicationNo + "'"
						+ " and pm_status in ('O','A')";
				whereadded = true;
			}
		}

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT pm_queue_no FROM public.nt_t_pm_application " + WHERE_SQL;

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				queueNo = rs.getString("pm_queue_no");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return queueNo;
	}

	@Override
	public String fillTenderNumber(String permitNo, String applicationNo, String queueNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String tenderRefNo = null;

		String WHERE_SQL = "";
		boolean whereadded = false;
		if (permitNo != null && !permitNo.equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE pm_permit_no = " + "'" + permitNo + "'";
			whereadded = true;
		}
		if (applicationNo != null && !applicationNo.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " OR pm_application_no = " + "'" + applicationNo + "'";
			} else {
				WHERE_SQL = WHERE_SQL + " WHERE pm_application_no = " + "'" + applicationNo + "'";
				whereadded = true;
			}
		}

		if (queueNo != null && !queueNo.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " OR pm_queue_no = " + "'" + queueNo + "'";
			} else {
				WHERE_SQL = WHERE_SQL + " WHERE pm_queue_no = " + "'" + queueNo + "'";
				whereadded = true;
			}
		}

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT pm_tender_ref_no FROM public.nt_t_pm_application " + WHERE_SQL;

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				tenderRefNo = rs.getString("pm_tender_ref_no");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return tenderRefNo;
	}

	@Override
	public String fillServiceType(String permitNo, String applicationNo, String queueNo, String tenderRefNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String serviceType = null;

		String WHERE_SQL = "";
		boolean whereadded = false;
		if (permitNo != null && !permitNo.equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE pm_permit_no = " + "'" + permitNo + "'";
			whereadded = true;
		}
		if (applicationNo != null && !applicationNo.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " OR pm_application_no = " + "'" + applicationNo + "'";
			} else {
				WHERE_SQL = WHERE_SQL + " WHERE pm_application_no = " + "'" + applicationNo + "'";
				whereadded = true;
			}
		}

		if (queueNo != null && !queueNo.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " OR pm_queue_no = " + "'" + queueNo + "'";
			} else {
				WHERE_SQL = WHERE_SQL + " WHERE pm_queue_no = " + "'" + queueNo + "'";
				whereadded = true;
			}
		}

		if (tenderRefNo != null && !tenderRefNo.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " OR pm_tender_ref_no = " + "'" + tenderRefNo + "'";
			} else {
				WHERE_SQL = WHERE_SQL + " WHERE pm_tender_ref_no = " + "'" + tenderRefNo + "'";
				whereadded = true;
			}
		}

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT B.code AS code FROM public.nt_t_pm_application A INNER JOIN nt_r_service_types B ON A.pm_service_type=B.code"
					+ WHERE_SQL;

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				serviceType = rs.getString("code");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return serviceType;

	}

	@Override
	public String fillRegNo(String applicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String busRegNo = null;

		String WHERE_SQL = "";
		boolean whereadded = false;

		if (applicationNo != null && !applicationNo.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " pm_application_no = " + "'" + applicationNo + "'";
			} else {
				WHERE_SQL = WHERE_SQL + " WHERE pm_application_no = " + "'" + applicationNo + "'"
						+ " and pm_status in ('O','A')";
				whereadded = true;
			}
		}

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT pm_vehicle_regno   FROM public.nt_t_pm_application " + WHERE_SQL;

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				busRegNo = rs.getString("pm_vehicle_regno");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return busRegNo;
	}

	@Override
	public String fillPermitNo(String applicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String permitNo = null;

		String WHERE_SQL = "";
		if (applicationNo != null && !applicationNo.equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE pm_application_no = " + "'" + applicationNo + "'"
					+ " and pm_status in ('O','A')";

		}

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT pm_permit_no   FROM public.nt_t_pm_application " + WHERE_SQL;

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				permitNo = rs.getString("pm_permit_no");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return permitNo;
	}

	@Override
	public List<PermitDTO> showDetails(PermitDTO permitDTO_01) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String WHERE_SQL = "";

		if (permitDTO_01.getApplicationNo() != null && !permitDTO_01.getApplicationNo().equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE pm_application_no = " + "'" + permitDTO_01.getApplicationNo() + "'";

		}
		List<PermitDTO> permitDTO = new ArrayList<PermitDTO>();

		try {
			con = ConnectionManager.getConnection();
			String query2 = "select distinct pm_application_no,pm_permit_no ,nt_r_service_types.description as pm_service_type,pm_tender_ref_no, pm_vehicle_regno,to_char(nt_t_pm_application.pm_created_date,'MM/dd/yyyy') as pm_created_date, nt_t_pm_vehi_owner.pmo_full_name"
					+ " FROM nt_t_pm_application"
					+ " INNER JOIN nt_r_service_types ON nt_t_pm_application.pm_service_type=nt_r_service_types.code inner join  public.nt_t_pm_vehi_owner on nt_t_pm_application.pm_application_no = public.nt_t_pm_vehi_owner.pmo_application_no"
					+ WHERE_SQL;

			PermitDTO p;
			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();

			while (rs.next()) {

				p = new PermitDTO();

				p.setApplicationNo(rs.getString("pm_application_no"));
				p.setServiceType(rs.getString("pm_service_type"));
				p.setTenderRefNo(rs.getString("pm_tender_ref_no"));
				p.setBusRegNo(rs.getString("pm_vehicle_regno"));
				p.setAppdate(rs.getString("pm_created_date"));
				p.setPermitNo(rs.getString("pm_permit_no"));
				p.setVehicleOwner(rs.getString("pmo_full_name"));

				permitDTO.add(p);
			}

			return permitDTO;

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
	}

	// added for back log transaction form
	@Override
	public List<String> getVehicleNoDropDowm(String userId) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<String> vehicleNoList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct pm_vehicle_regno FROM public.nt_t_pm_application where pm_vehicle_regno is not null and pm_is_backlog_app='Y'  and pm_created_by=? ORDER BY pm_vehicle_regno ;";

			ps = con.prepareStatement(query);
			ps.setString(1, userId);
			rs = ps.executeQuery();

			while (rs.next()) {

				vehicleNoList.add(rs.getString("pm_vehicle_regno"));

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return vehicleNoList;
	}

	@Override
	public String fillAppliNo(String permitNo, String busRegNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String appliNo = null;

		String WHERE_SQL = "";
		boolean whereadded = false;

		if (permitNo != null && !permitNo.equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE pm_permit_no = " + "'" + permitNo + "'";
			whereadded = true;
		}
		if (busRegNo != null && !busRegNo.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " OR pm_vehicle_regno = " + "'" + busRegNo + "'";
			} else {
				WHERE_SQL = WHERE_SQL + " WHERE pm_vehicle_regno = " + "'" + busRegNo + "'";
				whereadded = true;
			}
		}

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT pm_application_no FROM public.nt_t_pm_application " + WHERE_SQL;

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				appliNo = rs.getString("pm_application_no");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return appliNo;
	}

	@Override
	public String fillVehicleNo(String permitNo, String applicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String vehiNo = null;

		String WHERE_SQL = "";
		boolean whereadded = false;

		if (permitNo != null && !permitNo.equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE pm_permit_no = " + "'" + permitNo + "'";
			whereadded = true;
		}
		if (applicationNo != null && !applicationNo.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " OR pm_application_no = " + "'" + applicationNo + "'";
			} else {
				WHERE_SQL = WHERE_SQL + " WHERE pm_application_no = " + "'" + applicationNo + "'";
				whereadded = true;
			}
		}

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT pm_vehicle_regno FROM public.nt_t_pm_application " + WHERE_SQL;

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				vehiNo = rs.getString("pm_vehicle_regno");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return vehiNo;
	}

	@Override
	public String fillPermit(String applicationNo, String busRegNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String permitNo = null;

		String WHERE_SQL = "";
		boolean whereadded = false;

		if (applicationNo != null && !applicationNo.equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE pm_application_no = " + "'" + applicationNo + "'";
			whereadded = true;
		}
		if (busRegNo != null && !busRegNo.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " OR pm_vehicle_regno = " + "'" + busRegNo + "'";
			} else {
				WHERE_SQL = WHERE_SQL + " WHERE pm_vehicle_regno = " + "'" + busRegNo + "'";
				whereadded = true;
			}
		}

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT pm_permit_no FROM public.nt_t_pm_application " + WHERE_SQL;

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				permitNo = rs.getString("pm_permit_no");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return permitNo;

	}

	// added for reject user
	public void rejectUser(String selectList, String rejectReason) {
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_t_pm_application" + " SET pm_reject_reason =?, pm_status=?"
					+ " WHERE pm_application_no = ?";

			ps = con.prepareStatement(query);
			ps.setString(1, rejectReason);
			ps.setString(2, "R");
			ps.setString(3, selectList);

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
	public String getPmStatus(String applicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String status = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT 	pm_status FROM public.nt_t_pm_application " + "where pm_application_no=?";

			ps = con.prepareStatement(query);
			ps.setString(1, applicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {

				status = rs.getString("pm_status");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return status;
	}

	// update Status In application table

	@Override
	public void updatePmStatus(String selectList) {
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionManager.getConnection();

			String query = " UPDATE public.nt_t_pm_application SET pm_status=? WHERE pm_application_no=?;";

			ps = con.prepareStatement(query);

			ps.setString(1, "A");
			ps.setString(2, selectList);

			ps.executeUpdate();

			con.commit();

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

	}

	// validate task id PM200 is completed.
	@Override
	public String getTaskStatus(String applicationNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String taskStatus = null;
		try {
			con = ConnectionManager.getConnection();

			String query = " select a.tsd_task_code, a.tsd_status "
					+ " FROM public.nt_t_task_det a,public.nt_r_task b,public.nt_t_pm_application c"
					+ " where a.tsd_task_code=b.code" + " and  b.code='PM200'" + " and a.tsd_app_no=c.pm_application_no"
					+ " and  c.pm_application_no=?";

			ps = con.prepareStatement(query);

			ps.setString(1, applicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {

				taskStatus = rs.getString("tsd_status");
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return taskStatus;
	}

	@Override
	public void insertTaskDet(String selectList, String selectList1, String loginUSer) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_task_det");

			String sql = "INSERT INTO public.nt_t_task_det "
					+ "(tsd_seq, tsd_vehicle_no, tsd_app_no, tsd_task_code, tsd_status, created_by, created_date) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?);";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, selectList);
			stmt.setString(3, selectList);
			stmt.setString(4, "PM201");
			stmt.setString(5, "C");
			stmt.setString(6, loginUSer);
			stmt.setTimestamp(7, timestamp);

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

	@Override
	public void updatehistory(String vehicleNum, String appNum, String loginUSer) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_task_det");

			String sql = "INSERT INTO public.nt_h_task_his (tsd_seq, tsd_vehicle_no, tsd_app_no, tsd_task_code, tsd_status, created_by, created_date)"
					+ "VALUES(?,?,?,?,?,?,?);";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, vehicleNum);
			stmt.setString(3, appNum);
			stmt.setString(4, "PM201");
			stmt.setString(5, "O");
			stmt.setString(6, loginUSer);
			stmt.setTimestamp(7, timestamp);

			stmt.executeUpdate();
			migratedService = (MigratedService) SpringApplicationContex.getBean("migratedService");
			String queueNumber = migratedService.findQueueNumberFromApplicationNo(appNum);
			migratedService.updateQueueNumberTaskInQueueMaster(queueNumber, appNum, "PM201", "C");

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

	}

	@Override
	public PermitDTO showData(String applicationNo, String permitNo, String queueNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String WHERE_SQL = "";
		boolean whereadded = false;

		if (permitNo != null && !permitNo.equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE pm_permit_no = " + "'" + permitNo + "'";
			whereadded = true;
		}
		if (applicationNo != null && !applicationNo.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " and pm_application_no = " + "'" + applicationNo + "'";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE pm_application_no = " + "'" + applicationNo + "'";

				whereadded = true;
			}
		}
		if (queueNo != null && !queueNo.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " and pm_queue_no = " + "'" + queueNo + "'";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE pm_queue_no = " + "'" + queueNo + "'";

				whereadded = true;
			}
		}

		PermitDTO permitDTO = new PermitDTO();

		try {
			con = ConnectionManager.getConnection();
			String query2 = "select nt_r_service_types.description as pm_service_type,pm_route_no,pm_permit_expire_date,pm_renewal_period,pm_special_remark,pm_route_flag, "
					+ " public.nt_r_route.rou_service_origine,public.nt_r_route.rou_service_destination,pm_permit_expire_date,pm_valid_to,pm_new_permit_expiry_date "
					+ " from public.nt_t_pm_application"
					+ "  INNER JOIN nt_r_service_types ON nt_t_pm_application.pm_service_type=nt_r_service_types.code "
					+ "  inner join nt_r_route on public.nt_t_pm_application.pm_route_no = public.nt_r_route.rou_number"
					+ WHERE_SQL;

			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();

			while (rs.next()) {

				permitDTO.setServiceType(rs.getString("pm_service_type"));
				permitDTO.setRouteNo(rs.getString("pm_route_no"));
				permitDTO.setExpirDate(rs.getString("pm_permit_expire_date"));
				permitDTO.setRePeriod(rs.getInt("pm_renewal_period"));
				permitDTO.setRemarks(rs.getString("pm_special_remark"));
				permitDTO.setNewPermitExpireDate(rs.getString("pm_new_permit_expiry_date"));
				permitDTO.setRouteFlag(rs.getString("pm_route_flag"));

				if (permitDTO.getRouteFlag() == null || permitDTO.getRouteFlag().isEmpty()
						|| permitDTO.getRouteFlag().equals(null)) {
					permitDTO.setOrigin("");
					permitDTO.setDestination("");
				} else if (permitDTO.getRouteFlag().equals("Y")) {
					permitDTO.setOrigin(rs.getString("rou_service_destination"));
					permitDTO.setDestination(rs.getString("rou_service_origine"));
				} else if (permitDTO.getRouteFlag().equals("N")) {
					permitDTO.setOrigin(rs.getString("rou_service_origine"));
					permitDTO.setDestination(rs.getString("rou_service_destination"));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return permitDTO;
	}

	@Override
	public PermitRenewalsDTO showDataFromVehiOwner(String permitNo, String applicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		PermitRenewalsDTO permitRenewDTO = new PermitRenewalsDTO();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select distinct a.seq as seqno, a.pm_application_no as applicationno, a.pm_queue_no as queueno, a.pm_permit_no as permitno,a.pm_vehicle_regno as busregno, nt_r_language.description as pmo_preferred_language,d.pmo_title as pmo_title, d.pmo_full_name as pmo_full_name, d.pmo_full_name_sinhala as pmo_full_name_sinhala, d.pmo_full_name_tamil as pmo_full_name_tamil,  d.pmo_name_with_initial as pmo_name_with_initial, d.pmo_nic as pmo_nic, d.pmo_gender  as pmo_gender, d.pmo_dob as pmo_dob,  d.pmo_telephone_no as pmo_telephone_no, d.pmo_mobile_no as pmo_mobile_no,  d.pmo_province as pmo_province, d.pmo_district as pmo_district, d.pmo_div_sec as pmo_div_sec,b.description, (case when d.pmo_marital_status is not null then (select g.description from nt_r_marital_status g, nt_t_pm_vehi_owner d where g.code = d.pmo_marital_status and d.pmo_application_no=a.pm_application_no) else '-' end )as pmo_marital_status, (case when d.pmo_district is not null then (select c.description  from nt_r_district c,nt_t_pm_vehi_owner d where d.pmo_district=c.code and d.pmo_application_no=a.pm_application_no ) else '-' end ) as distrct,(case when d.pmo_div_sec is not null then (select e.ds_description  from public.nt_r_div_sec e,nt_t_pm_vehi_owner d where d.pmo_div_sec=e.ds_code and d.pmo_application_no=a.pm_application_no ) else '-' end ) as ds_description,d.pmo_address1 as pmo_address1, d.pmo_address1_sinhala as pmo_address1_sinhala, d.pmo_address1_tamil as pmo_address1_tamil, d.pmo_address2 as pmo_address2, d.pmo_address2_sinhala as pmo_address2_sinhala, d.pmo_address2_tamil as pmo_address2_tamil,  d.pmo_city as pmo_city, d.pmo_city_sinhala as pmo_city_sinhala, d.pmo_city_tamil as pmo_city_tamil from nt_t_pm_application a inner join public.nt_t_pm_vehi_owner d on a.pm_application_no=d.pmo_application_no inner join public.nt_r_province b on d.pmo_province=b.code inner join nt_r_language on nt_r_language.code = d.pmo_preferred_language inner join nt_r_gender on nt_r_gender.code = d.pmo_gender and a.pm_application_no =?";

			ps = con.prepareStatement(query2);
			ps.setString(1, applicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {

				permitRenewDTO.setPreferedLanguageCode(rs.getString("pmo_preferred_language"));
				permitRenewDTO.setTitleCode(rs.getString("pmo_title"));
				permitRenewDTO.setGenderCode(rs.getString("pmo_gender"));
				permitRenewDTO.setNic(rs.getString("pmo_nic"));
				permitRenewDTO.setFullName(rs.getString("pmo_full_name"));
				permitRenewDTO.setFullNameSinhala(rs.getString("pmo_full_name_sinhala"));
				permitRenewDTO.setFullNameTamil(rs.getString("pmo_full_name_tamil"));
				permitRenewDTO.setAddressOneSinhala(rs.getString("pmo_address1_sinhala"));
				permitRenewDTO.setAddressOneTamil(rs.getString("pmo_address1_tamil"));
				permitRenewDTO.setAddressTwoSinhala(rs.getString("pmo_address2_sinhala"));
				permitRenewDTO.setAddressTwoTamil(rs.getString("pmo_address2_tamil"));
				permitRenewDTO.setCitySinhala(rs.getString("pmo_city_sinhala"));
				permitRenewDTO.setCityTamil(rs.getString("pmo_city_tamil"));
				permitRenewDTO.setNameWithInitials(rs.getString("pmo_name_with_initial"));
				permitRenewDTO.setMaterialStatusId(rs.getString("pmo_marital_status"));
				permitRenewDTO.setTeleNo(rs.getString("pmo_telephone_no"));
				permitRenewDTO.setMobileNo(rs.getString("pmo_mobile_no"));
				permitRenewDTO.setProvinceDescription(rs.getString("description"));
				permitRenewDTO.setDistrictCode(rs.getString("distrct"));
				permitRenewDTO.setAddressOne(rs.getString("pmo_address1"));
				permitRenewDTO.setAddressTwo(rs.getString("pmo_address2"));
				permitRenewDTO.setCity(rs.getString("pmo_city"));
				permitRenewDTO.setDivisionalSecretariatDivision(rs.getString("ds_description"));
				permitRenewDTO.setDob(rs.getString("pmo_dob"));
				permitRenewDTO.setApplicationNo(rs.getString("applicationno"));
				permitRenewDTO.setPermitNo(rs.getString("permitno"));
				permitRenewDTO.setQueueNo(rs.getString("queueno"));
				permitRenewDTO.setRegNoOfBus(rs.getString("busregno"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return permitRenewDTO;
	}

	@Override
	public List<PermitRenewalsDTO> getAppNo() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PermitRenewalsDTO> applicationNoList = new ArrayList<PermitRenewalsDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct tsd_app_no from nt_t_task_det "
					+ "inner join nt_t_pm_application on nt_t_task_det.tsd_app_no = nt_t_pm_application.pm_application_no "
					+ "where coalesce(nt_t_pm_application.pm_isnew_permit,'X') <> 'Y' and (tsd_task_code not in ('PM100','PM101'))  order by tsd_app_no";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();
				permitRenewalsDTO.setApplicationNo(rs.getString("tsd_app_no"));
				applicationNoList.add(permitRenewalsDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return applicationNoList;
	}

	@Override
	public List<String> getPermit() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<String> permitNoList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct pm_permit_no FROM public.nt_t_pm_application  where pm_permit_no is not null  and pm_permit_no != '' ORDER BY pm_permit_no;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				String permitNo = rs.getString("pm_permit_no");

				if (permitNo != null && permitNo.trim().equalsIgnoreCase("")) {

				}

				permitNoList.add(permitNo);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return permitNoList;
	}

	@Override
	public List<PermitDTO> showAppNo(String userId) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PermitDTO> returnList = new ArrayList<PermitDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct pm_application_no FROM public.nt_t_pm_application  where pm_is_backlog_app='Y' and pm_created_by=? order by pm_application_no  ;";

			ps = con.prepareStatement(query);
			ps.setString(1, userId);
			rs = ps.executeQuery();

			while (rs.next()) {
				PermitDTO permitDTO = new PermitDTO();
				permitDTO.setApplicationNo(rs.getString("pm_application_no"));

				returnList.add(permitDTO);

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
	public List<String> showPermitNo(String userId) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<String> permitNoList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct  pm_permit_no FROM public.nt_t_pm_application  where pm_permit_no is not null and pm_permit_no != ''and pm_is_backlog_app='Y' and pm_created_by=?  ORDER BY pm_permit_no;";

			ps = con.prepareStatement(query);
			ps.setString(1, userId);
			rs = ps.executeQuery();

			while (rs.next()) {
				String permitNo = rs.getString("pm_permit_no");

				if (permitNo != null && permitNo.trim().equalsIgnoreCase("")) {

				}

				permitNoList.add(permitNo);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return permitNoList;
	}

	@Override
	public List<EmployeeDTO> showUser() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<EmployeeDTO> returnUserList = new ArrayList<EmployeeDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  emp_user_id FROM public.nt_m_employee where emp_user_id is not null and  emp_user_status='A';";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				EmployeeDTO employeeDTO = new EmployeeDTO();
				employeeDTO.setUserId(rs.getString("emp_user_id"));

				returnUserList.add(employeeDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return returnUserList;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	@Override
	public List<PermitRenewalsDTO> getPermitNoForCurrentAppNo(String currentApplicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PermitRenewalsDTO> returnList = new ArrayList<PermitRenewalsDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct nt_t_pm_application.pm_permit_no  from nt_t_task_det inner join nt_t_pm_application on nt_t_task_det.tsd_app_no = nt_t_pm_application.pm_application_no where nt_t_pm_application.pm_isnew_permit is null and (tsd_task_code not in ('PM100','PM101','PM200')) ; ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();
				permitRenewalsDTO.setPermitNo(rs.getString("pm_permit_no"));
				returnList.add(permitRenewalsDTO);
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
	public String checkTaskForPayment(String applicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String taskStatus = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "select  tsd_app_no, tsd_task_code, tsd_status " + "FROM public.nt_t_task_det "
					+ "where tsd_task_code='PM302' and tsd_app_no=? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, applicationNo);
			rs = ps.executeQuery();
			while (rs.next()) {

				taskStatus = rs.getString("tsd_status");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return taskStatus;
	}

	@Override
	public String checkTaskForReprint(String applicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String taskStatus = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "select  tsd_app_no, tsd_task_code, tsd_status " + "FROM public.nt_t_task_det "
					+ "where tsd_task_code='PM400' and tsd_app_no=? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, applicationNo);
			rs = ps.executeQuery();
			while (rs.next()) {

				taskStatus = rs.getString("tsd_status");
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return taskStatus;
	}

	@Override
	public void updatePrintStatus(String selectList) {

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionManager.getConnection();

			String query = " UPDATE public.nt_t_pm_application SET pm_is_permit_print =? WHERE pm_application_no=?;";

			ps = con.prepareStatement(query);

			ps.setString(1, "Y");
			ps.setString(2, selectList);

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
	public void updatePermitNo(PermitDTO permitDTO) {

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionManager.getConnection();

			String query = " UPDATE public.nt_t_pm_application SET pm_permit_no=? WHERE pm_application_no=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, permitDTO.getPermitNo());
			ps.setString(2, permitDTO.getApplicationNo());

			ps.executeUpdate();

			query = " UPDATE public.nt_t_pm_vehi_owner SET pmo_permit_no =? WHERE pmo_application_no =?;";

			ps = con.prepareStatement(query);

			ps.setString(1, permitDTO.getPermitNo());
			ps.setString(2, permitDTO.getApplicationNo());

			ps.executeUpdate();

			query = " UPDATE public.nt_t_pm_omini_bus1 SET pmb_permit_no =? WHERE pmb_application_no =?;";

			ps = con.prepareStatement(query);
			ps.setString(1, permitDTO.getPermitNo());
			ps.setString(2, permitDTO.getApplicationNo());
			ps.executeUpdate();

			query = " UPDATE public.nt_t_pm_service SET ser_permit_no =? WHERE ser_application_no =?;";

			ps = con.prepareStatement(query);
			ps.setString(1, permitDTO.getPermitNo());
			ps.setString(2, permitDTO.getApplicationNo());
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
	public PermitDTO getLoadValuesForSelectedOne(String permitNo, String applicationNo, String queueNo,
			String busRegNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PermitDTO permitDTO = new PermitDTO();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct nt_t_pm_application.pm_permit_no ,nt_t_pm_application.pm_queue_no,nt_t_pm_application.pm_application_no,nt_t_pm_application.pm_vehicle_regno from nt_t_task_det inner join nt_t_pm_application on nt_t_task_det.tsd_app_no = nt_t_pm_application.pm_application_no where nt_t_pm_application.pm_isnew_permit is null and (tsd_task_code not in ('PM100','PM101','PM200')) and nt_t_pm_application.pm_permit_no = ? or pm_application_no=? or pm_queue_no=? or pm_vehicle_regno=?;";
			ps = con.prepareStatement(query);
			ps.setString(1, permitNo);
			ps.setString(2, applicationNo);
			ps.setString(3, queueNo);
			ps.setString(4, busRegNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				permitDTO.setPermitNo(rs.getString("pm_permit_no"));
				permitDTO.setApplicationNo(rs.getString("pm_application_no"));
				permitDTO.setBusRegNo(rs.getString("pm_vehicle_regno"));
				permitDTO.setQueueNo(rs.getString("pm_queue_no"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return permitDTO;
	}

	@Override
	public PermitDTO ManageAssignedFunction(String logedUser) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PermitDTO permitDTO = new PermitDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "select nt_r_fun_role_activity.fra_function_code, nt_r_fun_role_activity.fra_activity_code "
					+ "from nt_t_granted_user_role inner join nt_r_fun_role_activity on "
					+ "nt_r_fun_role_activity.fra_role_code = nt_t_granted_user_role.gur_role_code "
					+ "where  fra_function_code='FN2_5' and gur_user_id =? and nt_t_granted_user_role.gur_status='A' ;";

			ps = con.prepareStatement(query);
			ps.setString(1, logedUser);
			rs = ps.executeQuery();

			while (rs.next()) {
				permitDTO.setActivityCode(rs.getString("fra_activity_code"));
				permitDTO.setFunctionCode(rs.getString("fra_function_code"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return permitDTO;
	}

	@Override
	public boolean isVoucherGenerated(PermitDTO permitDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isVoucherGenerated = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "select  nt_t_pm_application.pm_application_no, nt_t_pm_application.pm_status "
					+ "from nt_t_task_det "
					+ "inner join public.nt_t_pm_application on public.nt_t_pm_application.pm_application_no=nt_t_task_det.tsd_app_no "
					+ "inner join public.nt_h_task_his on public.nt_h_task_his.tsd_app_no=nt_t_task_det.tsd_app_no "
					+ "where( "
					+ "( nt_h_task_his.tsd_task_code='PM300' and nt_h_task_his.tsd_status='O' and  nt_h_task_his.tsd_app_no=? "
					+ "or nt_t_task_det.tsd_task_code='PM300' and nt_t_task_det.tsd_status='O'and  nt_t_task_det.tsd_app_no=? ) "
					+ "and (nt_t_pm_application.pm_application_no is not null) "
					+ "and nt_t_pm_application.pm_status='A' "
					+ "and nt_t_pm_application.pm_application_no=? ) order by nt_t_pm_application.pm_application_no ";

			ps = con.prepareStatement(query);
			ps.setString(1, permitDTO.getApplicationNo());
			ps.setString(2, permitDTO.getApplicationNo());
			ps.setString(3, permitDTO.getApplicationNo());
			rs = ps.executeQuery();

			if (rs.next() == true) {
				isVoucherGenerated = true;
			} else {
				isVoucherGenerated = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isVoucherGenerated;

	}

	@Override
	public boolean isPaymentApprove(String applicationNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isPermitApproved = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM public.nt_m_payment_voucher "
					+ "WHERE pav_application_no=? and pav_approved_status='A' ;";

			ps = con.prepareStatement(query);
			ps.setString(1, applicationNo);
			rs = ps.executeQuery();

			if (rs.next() == true) {
				isPermitApproved = true;
			} else {
				isPermitApproved = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isPermitApproved;

	}

	@Override
	public String getVoucherNo(String applicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String voucherNo = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select  distinct nt_t_voucher_details.vod_voucher_no from public.nt_m_payment_voucher "
					+ "inner join public.nt_t_voucher_details on nt_t_voucher_details.vod_voucher_no = nt_m_payment_voucher.pav_voucher_no "
					+ "where nt_m_payment_voucher.pav_application_no=? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, applicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				voucherNo = rs.getString("vod_voucher_no");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return voucherNo;
	}

	@Override
	public boolean changePermitRenewalPeriod(PermitDTO dto, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		boolean isApproved = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_t_pm_application SET pm_new_permit_expiry_date=?, pm_modified_by=?, pm_modified_date=? "
					+ "WHERE pm_application_no=? ;";

			ps = con.prepareStatement(query);

			if (dto.getExpirDate() != null) {
				ps.setString(1, dto.getExpirDate());
			} else {
				ps.setString(1, null);
			}

			ps.setString(2, user);
			ps.setTimestamp(3, timestamp);
			ps.setString(4, dto.getApplicationNo());
			int a = ps.executeUpdate();

			if (a > 0) {
				isApproved = true;
			} else {
				isApproved = false;
			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isApproved;

	}

	@Override
	public List<PermitRenewalsDTO> getPermitNoList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PermitRenewalsDTO> returnList = new ArrayList<PermitRenewalsDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct nt_t_pm_application.pm_permit_no  from nt_t_task_det inner join nt_t_pm_application on nt_t_task_det.tsd_app_no = nt_t_pm_application.pm_application_no where nt_t_pm_application.pm_isnew_permit is null and (tsd_task_code not in ('PM100','PM101','PM200')) ; ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();
				permitRenewalsDTO.setPermitNo(rs.getString("pm_permit_no"));
				returnList.add(permitRenewalsDTO);
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

	// Description : Get all active permits
	@Override
	public List<PermitDTO> getActivePermitList() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PermitDTO> returnList = new ArrayList<PermitDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct a.pm_application_no, a.pm_permit_no, a.pm_vehicle_regno,a.pm_route_no, b.rou_description  from nt_t_pm_application a, nt_r_route b "
					+ "where a.pm_route_no=b.rou_number and  a.pm_status='A' ";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				PermitDTO permitDTO = new PermitDTO();
				permitDTO.setApplicationNo(rs.getString("pm_application_no"));
				permitDTO.setPermitNo(rs.getString("pm_permit_no"));
				permitDTO.setBusRegNo(rs.getString("pm_vehicle_regno"));
				permitDTO.setRouteNo(rs.getString("pm_vehicle_regno"));

				returnList.add(permitDTO);

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

	/**
	 * for update pm_expire_date by pm_new_expire_date
	 */
	@Override
	public boolean updateExpireDateByNewExpireDate(PermitDTO dto, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		boolean isApproved = false;
		try {

			con = ConnectionManager.getConnection();
			String query = "UPDATE public.nt_t_pm_application SET pm_permit_expire_date=? , pm_new_permit_expiry_date=? , pm_modified_by=?, pm_modified_date=? "
					+ "WHERE pm_application_no=? ;";

			ps = con.prepareStatement(query);

			if (dto.getExpirDate() != null) {
				ps.setString(1, dto.getExpirDate());
			} else {
				ps.setString(1, null);

			}
			ps.setNull(2, java.sql.Types.VARCHAR);

			ps.setString(3, user);
			ps.setTimestamp(4, timestamp);
			ps.setString(5, dto.getApplicationNo());
			int a = ps.executeUpdate();
			;

			if (a > 0) {
				isApproved = true;
			} else {
				isApproved = false;
			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isApproved;

	}

	@Override
	public List<PermitRenewalsDTO> getAppNoForViewPermitRen() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PermitRenewalsDTO> applicationNoList = new ArrayList<PermitRenewalsDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct tsd_app_no from nt_t_task_det \r\n"
					+ "inner join nt_t_pm_application on nt_t_task_det.tsd_app_no = nt_t_pm_application.pm_application_no\r\n"
					+ "where coalesce(nt_t_pm_application.pm_isnew_permit,'X') <> 'Y' \r\n"
					+ "and (tsd_task_code  in ('PR200'))  order by tsd_app_no";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();
				permitRenewalsDTO.setApplicationNo(rs.getString("tsd_app_no"));
				applicationNoList.add(permitRenewalsDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return applicationNoList;
	}

	public MigratedService getMigratedService() {
		return migratedService;
	}

	public void setMigratedService(MigratedService migratedService) {
		this.migratedService = migratedService;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public boolean approvePermitAndTaskUpdateAndInsertHistory(PermitDTO dto, String user, String taskCode,
			PermitRenewalsDTO pdto) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		PreparedStatement ps4 = null;
		PreparedStatement ps5 = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		boolean isApproved = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_t_pm_application SET  pm_status='A', pm_new_permit_expiry_date=? , pm_modified_by=?, pm_modified_date=? "
					+ "WHERE pm_application_no=? ;";

			ps = con.prepareStatement(query);

			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			String strDate = dateFormat.format(dto.getNewExpirDate());

			ps.setString(1, strDate);
			ps.setString(2, user);
			ps.setTimestamp(3, timestamp);
			ps.setString(4, dto.getApplicationNo());
			int a = ps.executeUpdate();

			if (a > 0) {
				isApproved = true;
			} else {
				isApproved = false;
			}
			
			// update old application number in amendments
			updateAmendmentsOldApplicationNo(dto, con);

			if (isApproved) {

				String q2 = "SELECT * " + "FROM public.nt_t_task_det " + "where tsd_app_no=? ;";

				ps2 = con.prepareStatement(q2);
				ps2.setString(1, dto.getApplicationNo());
				rs2 = ps2.executeQuery();

				if (rs2.next() == true) {
					// insert in to history table
					String q3 = "INSERT INTO public.nt_h_task_his "
							+ "(tsd_seq, tsd_vehicle_no, tsd_app_no, tsd_task_code, tsd_status, created_by, created_date) "
							+ "VALUES(?, ?, ?, ?, 'O', ?, ?); ";

					ps3 = con.prepareStatement(q3);
					ps3.setInt(1, rs2.getInt("tsd_seq"));
					ps3.setString(2, rs2.getString("tsd_vehicle_no"));
					ps3.setString(3, dto.getApplicationNo());
					ps3.setString(4, taskCode);
					ps3.setString(5, rs2.getString("created_by"));
					ps3.setTimestamp(6, rs2.getTimestamp("created_date"));
					ps3.executeUpdate();

					// update task details table

					String q4 = "UPDATE public.nt_t_task_det "
							+ "SET tsd_task_code=?, tsd_status='C', created_by=?, created_date=? "
							+ "WHERE tsd_app_no=?";
					ps4 = con.prepareStatement(q4);
					ps4.setString(1, taskCode);
					ps4.setString(2, user);
					ps4.setTimestamp(3, timestamp);
					ps4.setString(4, dto.getApplicationNo());
					ps4.executeUpdate();

					long seqNo = Utils.getNextValBySeqName(con, "seq_nt_h_pm_application");

					String sql = "INSERT INTO public.nt_h_pm_application_new "
							+ " (seq, pm_application_no, pm_queue_no, pm_permit_no, pm_vehicle_regno, pm_permit_issue_date, "
							+ " pm_permit_issue_by, pm_inspect_date, pm_tender_ref_no, pm_status, pm_tender_annual_fee, pm_installments,"
							+ " pm_is_permit_print, pm_reject_reason, pm_valid_from, pm_valid_to, pm_approved_by, pm_appoved_date,"
							+ " pm_special_remark, pm_is_tender_permit, pm_service_type, pm_route_no, pm_tot_bus_fare,"
							+ " pm_is_backlog_app, pm_created_by, pm_created_date, pm_modified_by, pm_modified_date, pm_permit_expire_date,"
							+ " pm_renewal_period, pm_next_ins_date_sec1_2, pm_next_ins_date_sec3, pm_inspect_remark, pm_route_flag,"
							+ " pm_isnew_permit, pm_reinspec_status, pm_old_permit_no, pm_temp_permit_no, pm_is_first_notice,"
							+ " pm_is_second_notice, pm_is_third_notice, pm_new_permit_expiry_date, pm_old_application_no,"
							+ " pm_skipped_inspection, pm_tran_type) "
							+ " VALUES(?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
							+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

					ps5 = con.prepareStatement(sql);

					ps5.setLong(1, seqNo);
					ps5.setString(2, pdto.getApplicationNo());
					ps5.setString(3, pdto.getQueueNo());
					ps5.setString(4, pdto.getPermitNo());
					ps5.setString(5, pdto.getRegNoOfBus());
					ps5.setString(6, pdto.getPermitIssueDateVal());

					ps5.setString(7, pdto.getPermitIssueBy());
					ps5.setString(8, pdto.getInspectionDate());
					ps5.setString(9, pdto.getTenderRefNo());
					ps5.setString(10, pdto.getStatus());
					ps5.setBigDecimal(11, pdto.getTenderAnualFee());
					ps5.setBigDecimal(12, pdto.getInstallment());

					ps5.setString(13, pdto.getPermitPrint());
					ps5.setString(14, pdto.getRejectReason());
					ps5.setString(15, pdto.getValidFromDate());
					ps5.setString(16, pdto.getValidToDate());
					ps5.setString(17, pdto.getApproveBy());
					ps5.setString(18, pdto.getApproveDate());

					ps5.setString(19, pdto.getSpecialRemark());
					ps5.setString(20, pdto.getIsTenderPermit());
					ps5.setString(21, pdto.getServiceTypeCode());
					ps5.setString(22, pdto.getRouteNo());
					ps5.setBigDecimal(23, pdto.getTotalBusFare());

					ps5.setString(24, pdto.getBacklogAppValue());
					ps5.setString(25, pdto.getCreateBy());
					ps5.setTimestamp(26, pdto.getCreateDate());
					ps5.setString(27, pdto.getModifyBy());
					ps5.setTimestamp(28, pdto.getModifyByTimestamp());
					ps5.setString(29, pdto.getPermitExpiryDate());

					ps5.setInt(30, pdto.getRequestRenewPeriod());
					ps5.setString(31, pdto.getInspectionDate1());
					ps5.setString(32, pdto.getInspectionDate2());
					ps5.setString(33, pdto.getInspectionRemark());
					ps5.setString(34, pdto.getRouteFlageValue());

					ps5.setString(35, pdto.getIsNewPermit());
					ps5.setString(36, pdto.getReInspectionStatus());
					ps5.setString(37, pdto.getOldPermitNo());
					ps5.setString(38, pdto.getTempPermitNo());
					ps5.setString(39, pdto.getFirstNotice());

					ps5.setString(40, pdto.getSecondNotice());
					ps5.setString(41, pdto.getThirdNotice());
					ps5.setString(42, pdto.getNewPermitExpirtDate());
					ps5.setString(43, pdto.getOldApplicationNo());
					ps5.setString(44, pdto.getIsSkipInspection());
					ps5.setString(45, pdto.getTranstractionTypeCode());

					ps5.executeUpdate();

					con.commit();
					migratedService = (MigratedService) SpringApplicationContex.getBean("migratedService");
					String queueNumber = migratedService.findQueueNumberFromApplicationNo(dto.getApplicationNo());
					if (queueNumber != null && !queueNumber.isEmpty() && !queueNumber.trim().equalsIgnoreCase("")) {
						migratedService.updateQueueNumberTaskInQueueMaster(queueNumber, dto.getApplicationNo(),
								taskCode, "C");
					}

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(ps3);
			ConnectionManager.close(ps4);
			ConnectionManager.close(con);
		}
		return isApproved;

	}
	
	private boolean updateAmendmentsOldApplicationNo(PermitDTO dto, Connection con) throws Exception {

		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;
		
		String query = "SELECT seq, pm_application_no, pm_queue_no, pm_permit_no, pm_vehicle_regno, pm_status "
				+ "FROM public.nt_t_pm_application "
				+ "WHERE pm_application_no LIKE 'AAP%' and (pm_status = 'P' or pm_status = 'H') and pm_permit_no = ?";

		ps = con.prepareStatement(query);
		ps.setString(1, dto.getPermitNo());
		rs = ps.executeQuery();
		
		while (rs.next()) {
			String query2 = "UPDATE public.nt_t_pm_application SET pm_old_application_no =? "
					+ "WHERE pm_application_no =?";

			ps2 = con.prepareStatement(query2);
			ps2.setString(1, dto.getApplicationNo());
			ps2.setString(2, rs.getString("pm_application_no"));
			ps2.executeUpdate();
		}
		
		ConnectionManager.close(rs);
		ConnectionManager.close(ps);
		ConnectionManager.close(ps2);

		return true;
	}

}
