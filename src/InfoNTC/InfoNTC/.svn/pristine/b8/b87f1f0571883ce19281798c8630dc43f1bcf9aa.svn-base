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
import org.apache.log4j.Logger;
import lk.informatics.ntc.model.dto.ActivateCancelledPermitsDTO;
import lk.informatics.ntc.model.dto.PermitDTO;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.roc.utils.common.Utils;

public class CancellationsPermitServiceImpl implements CancellationsPermitService {

	public static Logger logger = Logger.getLogger("CancellationsPermitServiceImpl");

	@Override
	public PermitDTO getViewData(PermitDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		PermitDTO permitDTO = new PermitDTO();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct pm_route_flag, pm_application_no, pm_service_type,pm_special_remark, nt_r_route.rou_service_origine, pm_route_no, "
					+ "nt_r_route.rou_service_destination, pm_permit_expire_date,nt_t_pm_vehi_owner.pmo_full_name, nt_t_pm_vehi_owner.pmo_telephone_no, "
					+ "nt_t_pm_vehi_owner.pmo_nic, nt_t_pm_vehi_owner.pmo_mobile_no, nt_t_pm_vehi_owner.pmo_address1, nt_t_pm_vehi_owner.pmo_address2, "
					+ "nt_t_pm_vehi_owner.pmo_province, nt_t_pm_vehi_owner.pmo_district,nt_t_pm_vehi_owner.pmo_city,nt_t_pm_vehi_owner.pmo_div_sec "
					+ "from nt_t_pm_application "
					+ "left outer join nt_r_route on  nt_r_route.rou_number=nt_t_pm_application.pm_route_no "
					+ "left outer join nt_t_pm_vehi_owner on nt_t_pm_vehi_owner.pmo_application_no=nt_t_pm_application.pm_application_no "
					+ "where pm_application_no=?;";

			ps = con.prepareStatement(query);

			ps.setString(1, dto.getApplicationNo());

			rs = ps.executeQuery();

			while (rs.next()) {
				permitDTO.setApplicationNo(rs.getString("pm_application_no"));
				permitDTO.setServiceType(rs.getString("pm_service_type"));
				permitDTO.setSpecialRemark(rs.getString("pm_special_remark"));
				permitDTO.setRouteNo(rs.getString("pm_route_no"));

				String routeFlag = rs.getString("pm_route_flag");

				if (routeFlag == null) {

					permitDTO.setOrigin(rs.getString("rou_service_origine"));
					permitDTO.setDestination(rs.getString("rou_service_destination"));
				} else {

					if (routeFlag.equals("N")) {
						permitDTO.setOrigin(rs.getString("rou_service_origine"));
						permitDTO.setDestination(rs.getString("rou_service_destination"));
					} else if (routeFlag.equals("Y")) {
						permitDTO.setOrigin(rs.getString("rou_service_destination"));
						permitDTO.setDestination(rs.getString("rou_service_origine"));
					}

				}

				permitDTO.setExpirDate(rs.getString("pm_permit_expire_date"));
				permitDTO.setOrganizationName(rs.getString("pmo_full_name"));
				permitDTO.setTelephoneNo(rs.getString("pmo_telephone_no"));
				permitDTO.setNicNo(rs.getString("pmo_nic"));
				permitDTO.setMobileNo(rs.getString("pmo_mobile_no"));
				permitDTO.setAddressOne(rs.getString("pmo_address1"));
				permitDTO.setAddressTwo(rs.getString("pmo_address2"));
				permitDTO.setProvince(rs.getString("pmo_province"));
				permitDTO.setDistrict(rs.getString("pmo_district"));
				permitDTO.setCity(rs.getString("pmo_city"));
				permitDTO.setDivision(rs.getString("pmo_div_sec"));

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
	public boolean updatePermitCancleApproveDetails(PermitDTO dto, PermitDTO dto2, String logUer, boolean cancelType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isInsertToPermitCancellation = false;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_t_permit_cancel "
					+ "SET  pmc_cancel_type=? , pmc_status=?, pmc_cancel_from=?, pmc_cancel_to=?, pmc_cancel_reason=?, rep_modified_by=?, rep_modified_date=?"
					+ "WHERE pmc_application_no=?; ";

			try {
				ps = con.prepareStatement(query);

				if (cancelType == true) {

					ps.setString(1, "T");
					ps.setString(2, "C");
					ps.setDate(3, dto2.getCancelFrom());
					ps.setDate(4, dto2.getCancelTO());
					ps.setString(5, dto2.getTempcancelReason());

				} else {
					ps.setString(1, "P");
					ps.setString(2, "C");
					ps.setDate(3, null);
					ps.setDate(4, null);
					ps.setString(5, null);
				}

				ps.setString(6, logUer);
				ps.setTimestamp(7, timestamp);
				ps.setString(8, dto.getApplicationNo());
				int i = ps.executeUpdate();

				if (i > 0) {
					isInsertToPermitCancellation = true;
				} else {
					isInsertToPermitCancellation = false;
				}
				con.commit();
			}catch (SQLException e) {
				con.rollback();
				isInsertToPermitCancellation = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			isInsertToPermitCancellation = false;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isInsertToPermitCancellation;
	}

	@Override
	public PermitDTO completePermitNoData(PermitDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		PermitDTO permitDTO = new PermitDTO();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT pm_vehicle_regno, pm_permit_no FROM public.nt_t_pm_application "
					+ "WHERE pm_vehicle_regno=? ;";

			ps = con.prepareStatement(query);

			ps.setString(1, dto.getBusRegNo());

			rs = ps.executeQuery();

			while (rs.next()) {
				permitDTO.setPermitNo(rs.getString("pm_permit_no"));
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
	public boolean checkApplicationTabel(PermitDTO dto, String status) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isAvailable = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM public.nt_t_pm_application  WHERE pm_application_no=? and pm_status=? ;";

			ps = con.prepareStatement(query);

			ps.setString(1, dto.getApplicationNo());
			ps.setString(2, status);
			rs = ps.executeQuery();

			if (rs.next() == true) {
				isAvailable = true;
			} else {
				isAvailable = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isAvailable;
	}

	@Override
	public boolean checkDataInPermitCancellation(PermitDTO dto, String status) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isAvailable = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT pmc_application_no FROM public.nt_t_permit_cancel WHERE pmc_application_no=? and pmc_status=? ;";

			ps = con.prepareStatement(query);

			ps.setString(1, dto.getApplicationNo());
			ps.setString(2, status);
			rs = ps.executeQuery();

			if (rs.next() == true) {
				isAvailable = true;
			} else {
				isAvailable = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isAvailable;
	}

	@Override
	public boolean updatePermitCancleRejectDetails(PermitDTO dto, PermitDTO dto2, String logUer) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isUpdateToPermitCancellation = false;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_t_permit_cancel "
					+ "SET pmc_reject_reason=?, pmc_status='R', rep_modified_by=?, rep_modified_date=? "
					+ "WHERE pmc_application_no=? ;";

			ps = con.prepareStatement(query);

			ps.setString(1, dto2.getRejectReason());
			ps.setString(2, logUer);
			ps.setTimestamp(3, timestamp);
			ps.setString(4, dto.getApplicationNo());
			int i = ps.executeUpdate();

			if (i > 0) {
				isUpdateToPermitCancellation = true;
			} else {
				isUpdateToPermitCancellation = false;
			}

			con.commit();

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isUpdateToPermitCancellation;
	}

	@Override
	public boolean insertPermitCancleRejectDetails(PermitDTO dto, PermitDTO dto2, String logUer) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isInsertToPermitCancellation = false;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_permit_cancel");

			String query = "INSERT INTO public.nt_t_permit_cancel "
					+ "(pmc_seq_no, pmc_permit_no, pmc_application_no, pmc_status, pmc_reject_reason, rep_created_by, rep_created_date) "
					+ "VALUES(?,?,?,?,?,?,?);";

			ps = con.prepareStatement(query);
			ps.setLong(1, seqNo);
			ps.setString(3, dto.getApplicationNo());
			ps.setString(2, dto.getPermitNo());
			ps.setString(4, "R");
			ps.setString(5, dto2.getRejectReason());
			ps.setString(6, logUer);
			ps.setTimestamp(7, timestamp);
			int i = ps.executeUpdate();

			if (i > 0) {
				isInsertToPermitCancellation = true;
			} else {
				isInsertToPermitCancellation = false;
			}

			con.commit();

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isInsertToPermitCancellation;
	}

	@Override
	public boolean insertPermitCancleApproveDetails(PermitDTO dto, PermitDTO dto2, String logUer, boolean cancelType) {
		Connection con = null;
		PreparedStatement ps = null, stmtN = null;
		ResultSet rs = null;
		boolean isInsertToPermitCancellation = false;
		String prvAappStatus = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_permit_cancel");

			String sqlN = "SELECT pm_status FROM nt_t_pm_application WHERE pm_application_no = ? ";
			stmtN = con.prepareStatement(sqlN);
			stmtN.setString(1, dto.getApplicationNo());
			rs = stmtN.executeQuery();
			while (rs.next()) {
				prvAappStatus = rs.getString("pm_status");
			}

			String query = "INSERT INTO public.nt_t_permit_cancel "
					+ "(pmc_seq_no,pmc_application_no, pmc_permit_no, pmc_cancel_type, pmc_status,pmc_cancel_from, "
					+ "pmc_cancel_to, pmc_cancel_reason,rep_prv_app_status, rep_created_by, rep_created_date) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?);";

			try {
				ps = con.prepareStatement(query);
				ps.setLong(1, seqNo);
				ps.setString(2, dto.getApplicationNo());
				ps.setString(3, dto.getPermitNo());

				if (cancelType == true) {

					ps.setString(4, "T");
					ps.setString(5, "C");
					ps.setDate(6, dto2.getCancelFrom());
					ps.setDate(7, dto2.getCancelTO());
					ps.setString(8, dto2.getTempcancelReason());

				} else {
					ps.setString(4, "P");
					ps.setString(5, "C");
					ps.setDate(6, null);
					ps.setDate(7, null);
					ps.setString(8, null);
				}
				ps.setString(9, prvAappStatus);
				ps.setString(10, logUer);
				ps.setTimestamp(11, timestamp);
				int i = ps.executeUpdate();

				if (i > 0) {
					isInsertToPermitCancellation = true;
				} else {
					isInsertToPermitCancellation = false;
				}

				con.commit();
			} catch (SQLException e) {
				con.rollback();
				isInsertToPermitCancellation = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			isInsertToPermitCancellation = false;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isInsertToPermitCancellation;
	}

	@Override
	public boolean updateApplicationTableStatus(PermitDTO dto, String logingUser, boolean cancelType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isStatusChange = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_t_pm_application SET  pm_status=? , pm_modified_by=?, pm_modified_date=?  WHERE pm_application_no=? ;";

			ps = con.prepareStatement(query);
			if (cancelType == true) {
				ps.setString(1, "TC");
			} else {
				ps.setString(1, "C");
			}

			ps.setString(2, logingUser);
			ps.setTimestamp(3, timestamp);
			ps.setString(4, dto.getApplicationNo());
			int i = ps.executeUpdate();

			if (i > 0) {
				isStatusChange = true;
			} else {
				isStatusChange = false;
			}

			con.commit();

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isStatusChange;
	}

	@Override
	public boolean CopyTaskDetailsANDinsertTaskHistory(PermitDTO dto, String loginUSer, String taskCode) {
		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		PermitDTO permitDTO = null;

		boolean isUpdate = false;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

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
				permitDTO.setTaskStatus(rs.getString("tsd_status"));

				String sql2 = "INSERT INTO public.nt_h_task_his "
						+ "(tsd_seq, tsd_vehicle_no, tsd_app_no, tsd_task_code, tsd_status, created_by, created_date) "
						+ "VALUES(? , ?, ?, ?, ?, ?, ?); " + "";

				try {
					stmt2 = con.prepareStatement(sql2);

					stmt2.setLong(1, permitDTO.getTaskSeq());
					stmt2.setString(2, permitDTO.getBusRegNo());
					stmt2.setString(3, permitDTO.getApplicationNo());
					stmt2.setString(4, permitDTO.getTaskCode());
					stmt2.setString(5, permitDTO.getTaskStatus());
					stmt2.setString(6, loginUSer);
					stmt2.setTimestamp(7, timestamp);
					stmt2.executeUpdate();
					con.commit();
				} catch (SQLException e) {
					con.rollback();
					isUpdate = false;
				}

			} else {
				isUpdate = false;
			}		
		} catch (Exception ex) {
			ex.printStackTrace();
			isUpdate = false;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(con);
		}
		return isUpdate;
	}

	@Override
	public boolean deleteTaskDetails(PermitDTO dto, String taskCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isTaskDelete = false;

		try {
			con = ConnectionManager.getConnection();

			String query = "DELETE FROM public.nt_t_task_det WHERE tsd_app_no=? AND tsd_task_code=? ;";

			try {
				ps = con.prepareStatement(query);
				ps.setString(1, dto.getApplicationNo());
				ps.setString(2, taskCode);
				int i = ps.executeUpdate();

				if (i > 0) {
					isTaskDelete = true;
				} else {
					isTaskDelete = false;
				}
				con.commit();
			} catch (SQLException e) {
				con.rollback();
				isTaskDelete = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			isTaskDelete = false;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isTaskDelete;
	}

	@Override
	public boolean insertTaskDetails(PermitDTO dto, String loginUSer, String taskCode, String status) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		boolean isInsert = false;

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_task_det");

			String sql = "INSERT INTO public.nt_t_task_det "
					+ "(tsd_seq, tsd_vehicle_no, tsd_app_no, tsd_task_code, tsd_status, created_by, created_date) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?);";

			try {
				stmt = con.prepareStatement(sql);

				stmt.setLong(1, seqNo);
				stmt.setString(2, dto.getBusRegNo());
				stmt.setString(3, dto.getApplicationNo());
				stmt.setString(4, taskCode);
				stmt.setString(5, status);
				stmt.setString(6, loginUSer);
				stmt.setTimestamp(7, timestamp);

				int i = stmt.executeUpdate();

				if (i > 0) {
					isInsert = true;
				} else {
					isInsert = false;
				}

				con.commit();
			} catch (SQLException e) {
				con.rollback();
				isInsert = false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			isInsert = false;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return isInsert;
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
	public List<PermitDTO> getdefaultData() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String cancelReason = null;
		String status = null;
		List<PermitDTO> returnList = new ArrayList<PermitDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct pm_status, pmc_cancel_type, pm_application_no, pm_vehicle_regno,  pm_permit_no, pm_permit_expire_date , nt_t_pm_vehi_owner.pmo_full_name, nt_t_permit_cancel.pmc_cancel_reason "
					+ "FROM public.nt_t_pm_application  "
					+ "left outer join nt_t_pm_vehi_owner on nt_t_pm_vehi_owner.pmo_vehicle_regno = nt_t_pm_application.pm_vehicle_regno "
					+ "left outer join nt_t_permit_cancel on nt_t_permit_cancel.pmc_application_no = nt_t_pm_application.pm_application_no "
					+ "Where pm_status='E'; ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				PermitDTO dto = new PermitDTO();
				dto.setApplicationNo(rs.getString("pm_application_no"));
				dto.setCancelType(rs.getString("pmc_cancel_type"));
				dto.setBusRegNo(rs.getString("pm_vehicle_regno"));
				dto.setPermitNo(rs.getString("pm_permit_no"));
				dto.setExpirDate(rs.getString("pm_permit_expire_date"));
				dto.setVehicleOwner(rs.getString("pmo_full_name"));
				cancelReason = rs.getString("pmc_cancel_reason");
				if (cancelReason == null) {
					dto.setCancelReason("N/A");
				} else {
					dto.setCancelReason(cancelReason);
				}

				status = rs.getString("pm_status");

				if (status == null) {
					dto.setCancelStatus("N/A");
				} else if (status.equals("R")) {
					dto.setCancelStatus("REJECT");
				} else if (status.equals("TC")) {
					dto.setCancelStatus("TEMPORY CANCELLED");
				} else if (status.equals("A")) {
					dto.setCancelStatus("ACTIVE");
				} else if (status.equals("O")) {
					dto.setCancelStatus("ONGOING");
				} else if (status.equals("P")) {
					dto.setCancelStatus("PENDING");
				} else if (status.equals("C")) {
					dto.setCancelStatus("CANCELLED");
				} else if (status.equals("T")) {
					dto.setCancelStatus("TEMPORY");
				} else if (status.equals("E")) {
					dto.setCancelStatus("EXPIRED");
				} else if (status.equals("H")) {
					dto.setStatus("HOLD");
				} else {
					dto.setCancelStatus("N/A");
				}

				dto.setTemporary(true);
				dto.setCheckBoxClicked(false);
				returnList.add(dto);
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
	public List<PermitDTO> getCancellationData(PermitDTO permitDTO, Date date) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String cancelReason = null;
		String status = null;
		List<PermitDTO> returnList = new ArrayList<PermitDTO>();

		String WHERE_SQL = "";
		boolean whereadded = false;

		if (permitDTO.getPermitNo() != null && !permitDTO.getPermitNo().equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE pm_permit_no = " + "'" + permitDTO.getPermitNo()
					+ "' and pm_status not in ('I','C','G') ";
			whereadded = true;
		}
		if (permitDTO.getBusRegNo() != null && !permitDTO.getBusRegNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND pm_vehicle_regno = " + "'" + permitDTO.getBusRegNo() + "' ";
			} else {
				WHERE_SQL = WHERE_SQL + " WHERE pm_vehicle_regno = " + "'" + permitDTO.getBusRegNo()
						+ "' and pm_status not in ('I','C','G') ";
				whereadded = true;
			}
		}
		if (date != null) {

			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND to_date(pm_permit_expire_date,'dd/MM/yyyy') <= " + "'" + df.format(date)
						+ "' and pm_status not in ('I','C','G') ";
			} else {
				WHERE_SQL = WHERE_SQL + " WHERE to_date(pm_permit_expire_date,'dd/MM/yyyy') <= " + "'" + df.format(date)
						+ "' ";
				whereadded = true;
			}
		}

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct pm_status, pmc_status, pmc_cancel_type, pm_application_no, pm_vehicle_regno,  pm_permit_no, pm_permit_expire_date , nt_t_pm_vehi_owner.pmo_full_name, nt_t_permit_cancel.pmc_cancel_reason "
					+ "FROM public.nt_t_pm_application  "
					+ "left outer join nt_t_pm_vehi_owner on nt_t_pm_vehi_owner.pmo_application_no = nt_t_pm_application.pm_application_no "
					+ "left outer join nt_t_permit_cancel on nt_t_permit_cancel.pmc_application_no = nt_t_pm_application.pm_application_no "
					+ WHERE_SQL;

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				PermitDTO dto = new PermitDTO();
				dto.setApplicationNo(rs.getString("pm_application_no"));
				dto.setCancelType(rs.getString("pmc_cancel_type"));
				if (dto.getCancelType() == "T") {
					dto.setCheckBoxClicked(true);
				} else {
					dto.setCheckBoxClicked(false);
				}
				dto.setBusRegNo(rs.getString("pm_vehicle_regno"));
				dto.setPermitNo(rs.getString("pm_permit_no"));
				dto.setExpirDate(rs.getString("pm_permit_expire_date"));
				dto.setVehicleOwner(rs.getString("pmo_full_name"));
				cancelReason = rs.getString("pmc_cancel_reason");
				if (cancelReason == null) {
					dto.setCancelReason("N/A");
				} else {
					dto.setCancelReason(cancelReason);
				}

				status = rs.getString("pm_status");

				if (status == null) {
					dto.setCancelStatus("N/A");
				} else if (status.equals("R")) {
					dto.setCancelStatus("REJECT");
				} else if (status.equals("TC")) {
					dto.setCancelStatus("TEMPORY CANCELLED");
				} else if (status.equals("A")) {
					dto.setCancelStatus("ACTIVE");
				} else if (status.equals("E")) {
					dto.setCancelStatus("EXPIRED");
				} else if (status.equals("O")) {
					dto.setCancelStatus("ONGOING");
				} else if (status.equals("P")) {
					dto.setCancelStatus("PENDING");
				} else if (status.equals("C")) {
					dto.setCancelStatus("CANCELLED");
				} else if (status.equals("T")) {
					dto.setCancelStatus("TEMPORY");
				} else if (status.equals("H")) {
					dto.setStatus("HOLD");
				} else {
					dto.setCancelStatus("N/A");
				}

				dto.setTemporary(true);
				dto.setCheckBoxClicked(false);

				returnList.add(dto);
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
	public List<PermitDTO> getExpiredPermitNo() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PermitDTO> returnList = new ArrayList<PermitDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct  pm_permit_no FROM public.nt_t_pm_application "
					+ "WHERE  pm_permit_no !='' and pm_permit_no is not null and pm_status not in ('I','C','G') order by pm_permit_no;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				PermitDTO dto = new PermitDTO();
				dto.setPermitNo(rs.getString("pm_permit_no"));
				returnList.add(dto);
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
	public List<PermitDTO> getExpiredBusRegNo() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PermitDTO> returnList = new ArrayList<PermitDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct pm_vehicle_regno FROM public.nt_t_pm_application "
					+ "WHERE  pm_vehicle_regno !='' and pm_vehicle_regno is not null and pm_status not in ('I','C','G') order by pm_vehicle_regno; ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				PermitDTO dto = new PermitDTO();
				dto.setBusRegNo(rs.getString("pm_vehicle_regno"));
				returnList.add(dto);
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
	public List<ActivateCancelledPermitsDTO> getPermitNoList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<ActivateCancelledPermitsDTO> returnList = new ArrayList<ActivateCancelledPermitsDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  distinct pmc_permit_no FROM public.nt_t_permit_cancel where pmc_cancel_type='T' and pmc_active_status  is null  and pmc_permit_no is not null and pmc_permit_no !='' order by pmc_permit_no;";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				ActivateCancelledPermitsDTO activateCancelledPermitsDTO = new ActivateCancelledPermitsDTO();
				activateCancelledPermitsDTO.setPermitNo(rs.getString("pmc_permit_no"));
				returnList.add(activateCancelledPermitsDTO);

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
	public ActivateCancelledPermitsDTO getCurrentApplicationNo(String currentPermitNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ActivateCancelledPermitsDTO activateCancelledPermitsDTO = new ActivateCancelledPermitsDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  pmc_application_no FROM public.nt_t_permit_cancel where pmc_permit_no=? and pmc_cancel_type='T' and pmc_active_status  is null ;";
			ps = con.prepareStatement(query);
			ps.setString(1, currentPermitNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				activateCancelledPermitsDTO.setApplicationNo(rs.getString("pmc_application_no"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return activateCancelledPermitsDTO;
	}

	@Override
	public List<ActivateCancelledPermitsDTO> getCurrentVehicleNo(String currentApplicationNo, String currentPermitNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ActivateCancelledPermitsDTO> returnList = new ArrayList<ActivateCancelledPermitsDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT pm_vehicle_regno FROM public.nt_t_pm_application where pm_application_no=? and pm_permit_no=? ;";
			ps = con.prepareStatement(query);
			ps.setString(1, currentApplicationNo);
			ps.setString(2, currentPermitNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				ActivateCancelledPermitsDTO activateCancelledPermitsDTO = new ActivateCancelledPermitsDTO();
				activateCancelledPermitsDTO.setRegNoOfTheBus(rs.getString("pm_vehicle_regno"));
				returnList.add(activateCancelledPermitsDTO);
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
	public ActivateCancelledPermitsDTO getAllDetailsForSelectedDetails(String selectedPermitNo,
			String selectedVehicleNo, String cancelledDate) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ActivateCancelledPermitsDTO activateCancelledPermitsDTO = new ActivateCancelledPermitsDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT a.pmc_seq_no as seqno, a.pmc_permit_no as permitno, a.pmc_application_no as applicationno,  a.pmc_cancel_type as cancelstatus,   a.pmc_cancel_from as cancelfrom, a.pmc_cancel_to as cancelto, a.pmc_cancel_reason cancelreason, a.pmc_active_status as activestatus, a.pmc_active_remark as activeremark, a.pmc_is_active_ltr_printed as ltrprinted,to_char(a.rep_created_date,'dd/MM/yyyy') as rep_created_date, b.pm_vehicle_regno as appVehicleNo,c.pmo_full_name as ownername FROM public.nt_t_permit_cancel a,public.nt_t_pm_application b,public.nt_t_pm_vehi_owner c where (a.pmc_permit_no=? and b.pm_vehicle_regno=? and to_char(a.rep_created_date,'dd/MM/yyyy')=?)  and a.pmc_cancel_type='T' and a.pmc_application_no=b.pm_application_no and a.pmc_permit_no=b.pm_permit_no and a.pmc_application_no=c.pmo_application_no and a.pmc_permit_no=c.pmo_permit_no;";
			ps = con.prepareStatement(query);
			ps.setString(1, selectedPermitNo);
			ps.setString(2, selectedVehicleNo);
			ps.setString(3, cancelledDate);
			rs = ps.executeQuery();

			while (rs.next()) {
				activateCancelledPermitsDTO.setSeqNoCancelRecord(rs.getLong("seqno"));
				activateCancelledPermitsDTO.setPermitNo(rs.getString("permitno"));
				activateCancelledPermitsDTO.setApplicationNo(rs.getString("applicationno"));
				activateCancelledPermitsDTO.setCancelStatus(rs.getString("cancelstatus"));
				activateCancelledPermitsDTO.setCancelFromDate(rs.getString("cancelfrom"));
				activateCancelledPermitsDTO.setCancelToDate(rs.getString("cancelto"));
				activateCancelledPermitsDTO.setCancelledReason(rs.getString("cancelreason"));
				activateCancelledPermitsDTO.setActiveStatus(rs.getString("activestatus"));
				activateCancelledPermitsDTO.setSpecialRemarks(rs.getString("activeremark"));
				activateCancelledPermitsDTO.setIsActiveLetterPrinted(rs.getString("ltrprinted"));
				activateCancelledPermitsDTO.setCancelledDate(rs.getString("rep_created_date"));
				activateCancelledPermitsDTO.setRegNoOfTheBus(rs.getString("appVehicleNo"));
				activateCancelledPermitsDTO.setPermitOwner(rs.getString("ownername"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return activateCancelledPermitsDTO;
	}

	@Override
	public ActivateCancelledPermitsDTO getDefaultRecords(String currentPermitNo, String currentVehicleNo,
			String currentApplicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ActivateCancelledPermitsDTO activateCancelledPermitsDTO = new ActivateCancelledPermitsDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT a.pmc_seq_no as seqno, a.pmc_permit_no as permitno, a.pmc_application_no as applicationno,  a.pmc_cancel_type as cancelstatus,   a.pmc_cancel_from as cancelfrom, a.pmc_cancel_to as cancelto, a.pmc_cancel_reason cancelreason, a.pmc_active_status as activestatus, a.pmc_active_remark as activeremark, a.pmc_is_active_ltr_printed as ltrprinted,to_char(a.rep_created_date,'dd/MM/yyyy') as rep_created_date, b.pm_vehicle_regno as appVehicleNo,c.pmo_full_name as ownername FROM public.nt_t_permit_cancel a,public.nt_t_pm_application b,public.nt_t_pm_vehi_owner c where (a.pmc_permit_no=? and  b.pm_vehicle_regno=? and a.pmc_application_no=?)  and a.pmc_cancel_type='T' and a.pmc_application_no=b.pm_application_no and a.pmc_permit_no=b.pm_permit_no and a.pmc_application_no=c.pmo_application_no and a.pmc_permit_no=c.pmo_permit_no;";
			ps = con.prepareStatement(query);
			ps.setString(1, currentPermitNo);
			ps.setString(2, currentVehicleNo);
			ps.setString(3, currentApplicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				activateCancelledPermitsDTO.setSeqNoCancelRecord(rs.getLong("seqno"));
				activateCancelledPermitsDTO.setPermitNo(rs.getString("permitno"));
				activateCancelledPermitsDTO.setApplicationNo(rs.getString("applicationno"));
				activateCancelledPermitsDTO.setCancelStatus(rs.getString("cancelstatus"));
				activateCancelledPermitsDTO.setCancelFromDate(rs.getString("cancelfrom"));
				activateCancelledPermitsDTO.setCancelToDate(rs.getString("cancelto"));
				activateCancelledPermitsDTO.setCancelledReason(rs.getString("cancelreason"));
				activateCancelledPermitsDTO.setActiveStatus(rs.getString("activestatus"));
				activateCancelledPermitsDTO.setSpecialRemarks(rs.getString("activeremark"));
				activateCancelledPermitsDTO.setIsActiveLetterPrinted(rs.getString("ltrprinted"));
				activateCancelledPermitsDTO.setCancelledDate(rs.getString("rep_created_date"));
				activateCancelledPermitsDTO.setRegNoOfTheBus(rs.getString("appVehicleNo"));
				activateCancelledPermitsDTO.setPermitOwner(rs.getString("ownername"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return activateCancelledPermitsDTO;
	}

	@Override
	public List<ActivateCancelledPermitsDTO> getDefaultRecords() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ActivateCancelledPermitsDTO> activateCancelledPermitsList = new ArrayList<ActivateCancelledPermitsDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT a.pmc_seq_no as seqno, a.pmc_permit_no as permitno, a.pmc_application_no as applicationno,"
					+ " a.pmc_cancel_type as cancelstatus,   a.pmc_cancel_from as cancelfrom, a.pmc_cancel_to as cancelto,"
					+ " a.pmc_cancel_reason cancelreason, a.pmc_active_status as activestatus, a.pmc_active_remark as activeremark,"
					+ " a.pmc_is_active_ltr_printed as ltrprinted,to_char(a.rep_created_date,'dd/MM/yyyy') as rep_created_date,"
					+ " b.pm_vehicle_regno as appVehicleNo,c.pmo_full_name as ownername "
					+ " FROM public.nt_t_permit_cancel a,public.nt_t_pm_application b,public.nt_t_pm_vehi_owner c "
					+ " WHERE  a.pmc_cancel_type='T' and pmc_active_status  is null  and a.pmc_application_no=b.pm_application_no "
					+ " and a.pmc_application_no=c.pmo_application_no ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				ActivateCancelledPermitsDTO activateCancelledPermitsDTO = new ActivateCancelledPermitsDTO();
				activateCancelledPermitsDTO.setSeqNoCancelRecord(rs.getLong("seqno"));
				activateCancelledPermitsDTO.setPermitNo(rs.getString("permitno"));
				activateCancelledPermitsDTO.setApplicationNo(rs.getString("applicationno"));
				activateCancelledPermitsDTO.setCancelStatus(rs.getString("cancelstatus"));
				activateCancelledPermitsDTO.setCancelFromDate(rs.getString("cancelfrom"));
				activateCancelledPermitsDTO.setCancelToDate(rs.getString("cancelto"));
				activateCancelledPermitsDTO.setCancelledReason(rs.getString("cancelreason"));
				activateCancelledPermitsDTO.setActiveStatus(rs.getString("activestatus"));
				activateCancelledPermitsDTO.setSpecialRemarks(rs.getString("activeremark"));
				activateCancelledPermitsDTO.setIsActiveLetterPrinted(rs.getString("ltrprinted"));
				activateCancelledPermitsDTO.setCancelledDate(rs.getString("rep_created_date"));
				activateCancelledPermitsDTO.setRegNoOfTheBus(rs.getString("appVehicleNo"));
				activateCancelledPermitsDTO.setPermitOwner(rs.getString("ownername"));

				activateCancelledPermitsList.add(activateCancelledPermitsDTO);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return activateCancelledPermitsList;
	}

	@Override
	public ActivateCancelledPermitsDTO getLoadDetailsForSelectedPermitNo(String selectedPermitNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ActivateCancelledPermitsDTO activateCancelledPermitsDTO = new ActivateCancelledPermitsDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  a.pmc_application_no as appno, to_char(a.rep_created_date,'dd/MM/yyyy') as rep_created_date,b.pm_vehicle_regno as vehicleno FROM public.nt_t_permit_cancel a, public.nt_t_pm_application b where a.pmc_permit_no=b.pm_permit_no and a.pmc_application_no=b.pm_application_no and pmc_permit_no=? and a.pmc_cancel_type='T';";
			ps = con.prepareStatement(query);
			ps.setString(1, selectedPermitNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				activateCancelledPermitsDTO.setApplicationNo(rs.getString("appno"));
				activateCancelledPermitsDTO.setCancelledDate(rs.getString("rep_created_date"));
				activateCancelledPermitsDTO.setRegNoOfTheBus(rs.getString("vehicleno"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return activateCancelledPermitsDTO;
	}

	@Override
	public ActivateCancelledPermitsDTO getLoadDetailsForSelectedVehicleNo(String selectedVehicleNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ActivateCancelledPermitsDTO activateCancelledPermitsDTO = new ActivateCancelledPermitsDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  a.pmc_permit_no as permitno, pmc_application_no as appno, to_char(a.rep_created_date,'dd/MM/yyyy') as rep_created_date FROM public.nt_t_permit_cancel a, public.nt_t_pm_application b where a.pmc_permit_no=b.pm_permit_no and a.pmc_application_no=b.pm_application_no and b.pm_vehicle_regno=? and a.pmc_cancel_type='T';";
			ps = con.prepareStatement(query);
			ps.setString(1, selectedVehicleNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				activateCancelledPermitsDTO.setApplicationNo(rs.getString("appno"));
				activateCancelledPermitsDTO.setCancelledDate(rs.getString("rep_created_date"));
				activateCancelledPermitsDTO.setPermitNo(rs.getString("permitno"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return activateCancelledPermitsDTO;
	}

	@Override
	public List<ActivateCancelledPermitsDTO> getPermitNoListForSelectedDate(String cancelledDate) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ActivateCancelledPermitsDTO> returnList = new ArrayList<ActivateCancelledPermitsDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT pmc_permit_no, pmc_application_no FROM public.nt_t_permit_cancel where to_char(rep_created_date,'dd/MM/yyyy')=? and pmc_cancel_type='T' order by pmc_permit_no;";
			ps = con.prepareStatement(query);
			ps.setString(1, cancelledDate);
			rs = ps.executeQuery();

			while (rs.next()) {
				ActivateCancelledPermitsDTO activateCancelledPermitsDTO = new ActivateCancelledPermitsDTO();
				activateCancelledPermitsDTO.setPermitNo(rs.getString("pmc_permit_no"));
				activateCancelledPermitsDTO.setApplicationNo(rs.getString("pmc_application_no"));
				returnList.add(activateCancelledPermitsDTO);
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
	public int updateApplicationTableStatus(ActivateCancelledPermitsDTO activateCancelledPermitsDTO) {
		Connection con = null;
		PreparedStatement stmt = null, stmtN = null;
		ResultSet rs = null;
		String prvAappStatus = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sqlN = "SELECT rep_prv_app_status FROM nt_t_permit_cancel WHERE pmc_application_no = ? ";
			stmtN = con.prepareStatement(sqlN);
			stmtN.setString(1, activateCancelledPermitsDTO.getApplicationNo());
			rs = stmtN.executeQuery();
			while (rs.next()) {
				prvAappStatus = rs.getString("rep_prv_app_status");
			}

			String sql = "UPDATE public.nt_t_pm_application SET pm_status=?, pm_modified_by=?, pm_modified_date=? WHERE pm_application_no=? and pm_permit_no=? and  pm_vehicle_regno=?;";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, prvAappStatus);
			stmt.setString(2, activateCancelledPermitsDTO.getModifiedBy());
			stmt.setTimestamp(3, timestamp);
			stmt.setString(4, activateCancelledPermitsDTO.getApplicationNo());
			stmt.setString(5, activateCancelledPermitsDTO.getPermitNo());
			stmt.setString(6, activateCancelledPermitsDTO.getRegNoOfTheBus());
			stmt.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmtN);
			ConnectionManager.close(con);

		}
		return 0;
	}

	@Override
	public int updatePermitCancelTable(ActivateCancelledPermitsDTO activateCancelledPermitsDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();
			String sql = "UPDATE public.nt_t_permit_cancel SET  pmc_active_status=?, pmc_active_remark=?, rep_modified_by=?, rep_modified_date=? WHERE pmc_seq_no=?;";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, "A");
			stmt.setString(2, activateCancelledPermitsDTO.getSpecialRemarks());
			stmt.setString(3, activateCancelledPermitsDTO.getModifiedBy());
			stmt.setTimestamp(4, timestamp);
			stmt.setLong(5, activateCancelledPermitsDTO.getSeqNoCancelRecord());
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
	public List<ActivateCancelledPermitsDTO> getAllRecordsForSelectedCancelledDate(String cancelledDate) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<ActivateCancelledPermitsDTO> returnList = new ArrayList<ActivateCancelledPermitsDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT a.pmc_seq_no as seqno, a.pmc_permit_no as permitno, a.pmc_application_no as applicationno,  a.pmc_cancel_type as cancelstatus,   a.pmc_cancel_from as cancelfrom, a.pmc_cancel_to as cancelto, a.pmc_cancel_reason cancelreason, a.pmc_active_status as activestatus, a.pmc_active_remark as activeremark, a.pmc_is_active_ltr_printed as ltrprinted,to_char(a.rep_created_date,'dd/MM/yyyy') as rep_created_date, b.pm_vehicle_regno as appVehicleNo,c.pmo_full_name as ownername FROM public.nt_t_permit_cancel a,public.nt_t_pm_application b,public.nt_t_pm_vehi_owner c where  to_char(a.rep_created_date,'dd/MM/yyyy')=?  and a.pmc_cancel_type='T' and a.pmc_application_no=b.pm_application_no and a.pmc_permit_no=b.pm_permit_no and a.pmc_application_no=c.pmo_application_no and a.pmc_permit_no=c.pmo_permit_no;";
			ps = con.prepareStatement(query);
			ps.setString(1, cancelledDate);
			rs = ps.executeQuery();

			while (rs.next()) {
				ActivateCancelledPermitsDTO activateCancelledPermitsDTO = new ActivateCancelledPermitsDTO();
				activateCancelledPermitsDTO.setSeqNoCancelRecord(rs.getLong("seqno"));
				activateCancelledPermitsDTO.setPermitNo(rs.getString("permitno"));
				activateCancelledPermitsDTO.setApplicationNo(rs.getString("applicationno"));
				activateCancelledPermitsDTO.setCancelStatus(rs.getString("cancelstatus"));
				activateCancelledPermitsDTO.setCancelFromDate(rs.getString("cancelfrom"));
				activateCancelledPermitsDTO.setCancelToDate(rs.getString("cancelto"));
				activateCancelledPermitsDTO.setCancelledReason(rs.getString("cancelreason"));
				activateCancelledPermitsDTO.setActiveStatus(rs.getString("activestatus"));
				activateCancelledPermitsDTO.setSpecialRemarks(rs.getString("activeremark"));
				activateCancelledPermitsDTO.setIsActiveLetterPrinted(rs.getString("ltrprinted"));
				activateCancelledPermitsDTO.setCancelledDate(rs.getString("rep_created_date"));
				activateCancelledPermitsDTO.setRegNoOfTheBus(rs.getString("appVehicleNo"));
				activateCancelledPermitsDTO.setPermitOwner(rs.getString("ownername"));
				returnList.add(activateCancelledPermitsDTO);

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
	public ActivateCancelledPermitsDTO getAllDetailsForSelectedViewBtn(String applicationNo, String permitNo,
			String regNoOfTheBus) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ActivateCancelledPermitsDTO activateCancelledPermitsDTO = new ActivateCancelledPermitsDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT a.pm_service_type as servicetypecode, a.pm_route_no as routeno, a.pm_route_flag as routeflag,a.pm_permit_expire_date as permitexpirydate, b.rou_service_origine as routeorigine, b.rou_service_destination as routedestination,c.description as servicetypedes, d.pmc_expired_reason as expiredreason, d.pmc_cancel_type as statustemp, d.pmc_cancel_from as cancelfromdate, d.pmc_cancel_to as canceltodate, d.pmc_cancel_reason as cancelreason,  d.pmc_active_remark as specialremark, to_char(d.rep_created_date,'dd/MM/yyyy') as rep_created_date, d.pmc_permit_no as permitno, d.pmc_application_no as appno, e.pmo_full_name as ownername, e.pmo_nic as nicno, e.pmo_telephone_no as telno, e.pmo_mobile_no as mobileno, e.pmo_province as provincecode, e.pmo_district as districtcode, e.pmo_div_sec as divseccode, e.pmo_address1 as addressone, e.pmo_address2 as addresstwo, e.pmo_city as city, f.description as provincedes FROM public.nt_t_pm_application a, public.nt_r_route b,public.nt_r_service_types c, public.nt_t_permit_cancel d, public.nt_t_pm_vehi_owner e, public.nt_r_province f where a.pm_route_no=b.rou_number and a.pm_service_type=c.code  and a.pm_application_no=d.pmc_application_no and a.pm_permit_no=d.pmc_permit_no and d.pmc_permit_no=? and d.pmc_application_no=? and d.pmc_permit_no=e.pmo_permit_no and d.pmc_application_no=e.pmo_application_no and e.pmo_province=f.code and (d.pmc_cancel_type='A' or d.pmc_cancel_type='T');";
			ps = con.prepareStatement(query);
			ps.setString(1, permitNo);
			ps.setString(2, applicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				activateCancelledPermitsDTO.setServiceTypeCode(rs.getString("servicetypecode"));
				activateCancelledPermitsDTO.setRouteNo(rs.getString("routeno"));
				activateCancelledPermitsDTO.setRouteFlagCode(rs.getString("routeflag"));
				if (activateCancelledPermitsDTO.getRouteFlagCode() == null
						|| activateCancelledPermitsDTO.getRouteFlagCode().isEmpty()
						|| activateCancelledPermitsDTO.getRouteFlagCode().equals(null)) {
					activateCancelledPermitsDTO.setRouteFlagIsChecked(false);
				} else if (activateCancelledPermitsDTO.getRouteFlagCode().equals("Y")) {
					activateCancelledPermitsDTO.setRouteFlagIsChecked(true);
				} else if (activateCancelledPermitsDTO.getRouteFlagCode().equals("N")) {
					activateCancelledPermitsDTO.setRouteFlagIsChecked(false);
				}

				activateCancelledPermitsDTO.setPermitExpiryDate(rs.getString("permitexpirydate"));
				activateCancelledPermitsDTO.setRouteOrigine(rs.getString("routeorigine"));
				activateCancelledPermitsDTO.setRouteServiceDestination(rs.getString("routedestination"));
				activateCancelledPermitsDTO.setServiceTypeDes(rs.getString("servicetypedes"));
				activateCancelledPermitsDTO.setExpiredReason(rs.getString("expiredreason"));
				activateCancelledPermitsDTO.setCancelStatus(rs.getString("statustemp"));
				if (activateCancelledPermitsDTO.getCancelStatus().equals("T")) {
					activateCancelledPermitsDTO.setTempStatusChecked(true);
				} else {
					activateCancelledPermitsDTO.setTempStatusChecked(false);
				}
				activateCancelledPermitsDTO.setCancelFromDate(rs.getString("cancelfromdate"));
				activateCancelledPermitsDTO.setCancelToDate(rs.getString("canceltodate"));
				activateCancelledPermitsDTO.setCancelledReason(rs.getString("cancelreason"));
				activateCancelledPermitsDTO.setSpecialRemarks(rs.getString("specialremark"));
				activateCancelledPermitsDTO.setCancelledDate(rs.getString("rep_created_date"));
				activateCancelledPermitsDTO.setPermitNo(rs.getString("permitno"));
				activateCancelledPermitsDTO.setApplicationNo(rs.getString("appno"));
				activateCancelledPermitsDTO.setPermitOwner(rs.getString("ownername"));
				activateCancelledPermitsDTO.setNicNo(rs.getString("nicno"));
				activateCancelledPermitsDTO.setTelNo(rs.getString("telno"));
				activateCancelledPermitsDTO.setMobileNo(rs.getString("mobileno"));
				activateCancelledPermitsDTO.setProvinceCode(rs.getString("provincecode"));
				activateCancelledPermitsDTO.setDistrictCode(rs.getString("districtcode"));
				activateCancelledPermitsDTO.setDivSectionCode(rs.getString("divseccode"));
				activateCancelledPermitsDTO.setAddressOne(rs.getString("addressone"));
				activateCancelledPermitsDTO.setAddressTwo(rs.getString("addresstwo"));
				activateCancelledPermitsDTO.setCity(rs.getString("city"));
				activateCancelledPermitsDTO.setProvinceDes(rs.getString("provincedes"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return activateCancelledPermitsDTO;
	}

	@Override
	public ActivateCancelledPermitsDTO getDistrictDescription(String provinceCode, String districtCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ActivateCancelledPermitsDTO activateCancelledPermitsDTO = new ActivateCancelledPermitsDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT description FROM public.nt_r_district where code=? and province_code=?;";
			ps = con.prepareStatement(query);
			ps.setString(1, districtCode);
			ps.setString(2, provinceCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				activateCancelledPermitsDTO.setDistrictDes(rs.getString("description"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return activateCancelledPermitsDTO;
	}

	@Override
	public ActivateCancelledPermitsDTO getDivisionSectionDescription(String districtCode, String divSectionCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ActivateCancelledPermitsDTO activateCancelledPermitsDTO = new ActivateCancelledPermitsDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT ds_description FROM public.nt_r_div_sec where ds_code=? and ds_district_code=?;";
			ps = con.prepareStatement(query);
			ps.setString(1, divSectionCode);
			ps.setString(2, districtCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				activateCancelledPermitsDTO.setDivSectionDes(rs.getString("ds_description"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return activateCancelledPermitsDTO;
	}

	@Override
	public List<ActivateCancelledPermitsDTO> getAllRecordsForDocumentsCheckings() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<ActivateCancelledPermitsDTO> returnList = new ArrayList<ActivateCancelledPermitsDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select doc_code, doc_document_des FROM public.nt_m_document where doc_transaction_type='07' order by doc_code;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				ActivateCancelledPermitsDTO activateCancelledPermitsDTO = new ActivateCancelledPermitsDTO();
				activateCancelledPermitsDTO.setDocumentCode(rs.getString("doc_code"));
				activateCancelledPermitsDTO.setDocumentDescription(rs.getString("doc_document_des"));
				returnList.add(activateCancelledPermitsDTO);

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
	public int insertPM403RecordTaskDet(String applicationNo, String regNoOfTheBus, String previousTaskCode,
			String currentUpdatedTaskCode, String loginUser) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();
			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_task_det");
			String sql = "INSERT INTO public.nt_t_task_det (tsd_seq, tsd_vehicle_no, tsd_app_no, tsd_task_code, tsd_status, created_by, created_date) VALUES(?, ?, ?, ?, ?, ?, ?);";
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, seqNo);
			stmt.setString(2, regNoOfTheBus);
			stmt.setString(3, applicationNo);
			stmt.setString(4, currentUpdatedTaskCode);
			stmt.setString(5, "C");
			stmt.setString(6, loginUser);
			stmt.setTimestamp(7, timestamp);
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
	public PermitDTO completeVehicleNoData(PermitDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		PermitDTO permitDTO = new PermitDTO();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT pm_vehicle_regno, pm_permit_no FROM public.nt_t_pm_application "
					+ "WHERE pm_permit_no=? ;";

			ps = con.prepareStatement(query);

			ps.setString(1, dto.getPermitNo());

			rs = ps.executeQuery();

			while (rs.next()) {
				permitDTO.setBusRegNo(rs.getString("pm_vehicle_regno"));
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
	public boolean checkDataForReport(String Appno) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isAvailable = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct pmc_permit_no,pmc_cancel_from,pmc_cancel_to,rep_modified_date,a.pmo_full_name_sinhala\r\n"
					+ "from public.nt_t_permit_cancel \r\n"
					+ "inner join public.nt_t_pm_vehi_owner a on a.pmo_permit_no=public.nt_t_permit_cancel.pmc_permit_no\r\n"
					+ "where pmc_application_no=? and pmc_active_status='A';;";

			ps = con.prepareStatement(query);

			ps.setString(1, Appno);

			rs = ps.executeQuery();

			if (rs.next() == true) {
				isAvailable = true;
			} else {
				isAvailable = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isAvailable;
	}

	@Override
	public void updateLetterPrintStatus(String AppNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_t_permit_cancel  " + "SET   pmc_is_active_ltr_printed=?"
					+ "WHERE pmc_application_no=?; ";

			ps = con.prepareStatement(query);

			ps.setString(1, "Y");
			ps.setString(2, AppNo);

			ps.executeUpdate();

			con.commit();

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

	}

	@Override
	public void updateCancelLetterPrintStatus(String AppNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_t_permit_cancel  " + "SET   pmc_is_printed =?"
					+ "WHERE pmc_application_no=?; ";

			ps = con.prepareStatement(query);

			ps.setString(1, "Y");
			ps.setString(2, AppNo);

			ps.executeUpdate();

			con.commit();

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

	}

	@Override
	public int insertPM403RecordTaskHis(String applicationNo, String regNoOfTheBus, String previousTaskCode,
			String currentUpdatedTaskCode, String loginUser) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();
			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_task_det");
			String sql = "INSERT INTO public.nt_h_task_his (tsd_seq, tsd_vehicle_no, tsd_app_no, tsd_task_code, tsd_status, created_by, created_date) VALUES(?, ?, ?, ?, ?, ?, ?);";
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, seqNo);
			stmt.setString(2, regNoOfTheBus);
			stmt.setString(3, applicationNo);
			stmt.setString(4, currentUpdatedTaskCode);
			stmt.setString(5, "C");
			stmt.setString(6, loginUser);
			stmt.setTimestamp(7, timestamp);
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

}
