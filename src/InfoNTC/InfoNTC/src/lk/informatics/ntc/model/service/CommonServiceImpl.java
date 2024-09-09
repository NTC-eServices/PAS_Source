package lk.informatics.ntc.model.service;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.PageFormatDTO;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.ntc.view.util.SpringApplicationContex;
import lk.informatics.roc.utils.common.Utils;

public class CommonServiceImpl implements CommonService, Serializable {

	private static final long serialVersionUID = 1L;

	public static Logger logger = Logger.getLogger("CommonServiceImpl");

	private MigratedService migratedService;

	@Override
	public String getVoucherNo(CommonDTO commonDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String voucherNo = null;

		try {
			con = ConnectionManager.getConnection();
			String query = "SELECT pav_permit_no, pav_application_no, pav_voucher_no FROM public.nt_m_payment_voucher "
					+ "WHERE pav_application_no=? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, commonDTO.getApplicationNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				voucherNo = rs.getString("pav_voucher_no");
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (ps != null)
					ps.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return voucherNo;
	}

	@Override
	public String getPermitNo(CommonDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String permitNo = null;

		String WHERE_SQL = "";
		boolean whereadded = false;
		if (dto.getApplicationNo() != null && !dto.getApplicationNo().equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE pm_application_no = " + "'" + dto.getApplicationNo() + "'";
			whereadded = true;
		}
		if (dto.getPermitNo() != null && !dto.getPermitNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND pm_permit_no = " + "'" + dto.getPermitNo() + "'";
			} else {
				WHERE_SQL = WHERE_SQL + " WHERE pm_permit_no = " + "'" + dto.getPermitNo() + "'";
				whereadded = true;
			}
		}

		if (dto.getVehicleNo() != null && !dto.getVehicleNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND pm_vehicle_regno = " + "'" + dto.getVehicleNo() + "'";
			} else {
				WHERE_SQL = WHERE_SQL + " WHERE pm_vehicle_regno = " + "'" + dto.getVehicleNo() + "'";
				whereadded = true;
			}
		}

		try {
			con = ConnectionManager.getConnection();
			String query = "SELECT pm_application_no,pm_permit_no, pm_vehicle_regno FROM public.nt_t_pm_application "
					+ WHERE_SQL;

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
	public String getPaymentType(CommonDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String paymentType = null;

		String WHERE_SQL = "";
		boolean whereadded = false;
		if (dto.getApplicationNo() != null && !dto.getApplicationNo().equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE pav_application_no = " + "'" + dto.getApplicationNo() + "'";
			whereadded = true;
		}
		if (dto.getPermitNo() != null && !dto.getPermitNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND pav_permit_no = " + "'" + dto.getPermitNo() + "'";
			} else {
				WHERE_SQL = WHERE_SQL + " WHERE pav_permit_no = " + "'" + dto.getPermitNo() + "'";
				whereadded = true;
			}
		}

		try {
			con = ConnectionManager.getConnection();
			String query = "SELECT  pav_payment_type FROM public.nt_m_payment_voucher " + WHERE_SQL;
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				paymentType = rs.getString("pav_payment_type");
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return paymentType;
	}

	@Override
	public boolean checkQueueNo(String queueNo) {
		logger.info("checkQueueNo start");

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isQueueAvailable = false;

		java.util.Date date = new java.util.Date();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String today = df.format(date);

		try {

			con = ConnectionManager.getConnection();

			String sql = "select que_number, que_vehicle_no, que_application_no FROM public.nt_m_queue_master "
					+ "where que_number=? and que_date like '" + today + "%'; ";

			ps = con.prepareStatement(sql);
			ps.setString(1, queueNo);
			rs = ps.executeQuery();

			if (rs.next()) {
				isQueueAvailable = true;
			} else {
				isQueueAvailable = false;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("checkQueueNo error: " + ex.toString());
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		logger.info("checkQueueNo end");
		return isQueueAvailable;
	}

	@Override
	public CommonDTO getInquiryApplicationORVehicleNo(String queueNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		CommonDTO dto = new CommonDTO();

		java.util.Date date = new java.util.Date();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String today = df.format(date);

		try {
			con = ConnectionManager.getConnection();
			String query = "SELECT que_application_no, que_vehicle_no FROM public.nt_m_queue_master where que_number=? and que_date like '"
					+ today + "%' ";
			ps = con.prepareStatement(query);
			ps.setString(1, queueNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				dto.setApplicationNo(rs.getString("que_application_no"));
				dto.setVehicleNo(rs.getString("que_vehicle_no"));
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
	public List<CommonDTO> getInquiryDetails(CommonDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String status = null;

		String WHERE_SQL = "";
		boolean whereadded = false;
		if (dto.getApplicationNo() != null && !dto.getApplicationNo().equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE pm_application_no = " + "'" + dto.getApplicationNo() + "' ";
			whereadded = true;
		}

		if (dto.getVehicleNo() != null && !dto.getVehicleNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND pm_vehicle_regno = " + "'" + dto.getVehicleNo() + "' ";
			} else {
				WHERE_SQL = WHERE_SQL + " WHERE pm_vehicle_regno = " + "'" + dto.getVehicleNo() + "' ";
				whereadded = true;
			}
		}

		if (dto.getPermitNo() != null && !dto.getPermitNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND pm_permit_no = " + "'" + dto.getPermitNo() + "' ";
			} else {
				WHERE_SQL = WHERE_SQL + " WHERE pm_permit_no = " + "'" + dto.getPermitNo() + "' ";
				whereadded = true;
			}
		}

		List<CommonDTO> inquiryDetails = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct  pm_application_no, pm_permit_no, pm_vehicle_regno, pm_status, pm_route_no, pm_tender_ref_no, "
					+ "pm_permit_expire_date, pm_valid_to, nt_t_pm_vehi_owner.pmo_full_name "
					+ "FROM public.nt_t_pm_application "
					+ "left outer join nt_t_pm_vehi_owner on nt_t_pm_vehi_owner.pmo_application_no= nt_t_pm_application.pm_application_no "
					+ WHERE_SQL;

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();

				commonDTO.setApplicationNo(rs.getString("pm_application_no"));
				commonDTO.setVehicleNo(rs.getString("pm_vehicle_regno"));
				commonDTO.setPermitNo(rs.getString("pm_permit_no"));
				commonDTO.setTenderNo(rs.getString("pm_tender_ref_no"));
				commonDTO.setRouteNo(rs.getString("pm_route_no"));
				commonDTO.setExpireDate(rs.getString("pm_permit_expire_date"));
				commonDTO.setValidTodate(rs.getString("pm_valid_to"));
				commonDTO.setOwnerName(rs.getString("pmo_full_name"));

				status = rs.getString("pm_status");

				if (status.equals("O")) {
					commonDTO.setStatus("ONGOING");
				} else if (status.equals("A")) {
					commonDTO.setStatus("ACTIVE");
				} else if (status.equals("I")) {
					commonDTO.setStatus("INACTIVE");
				} else if (status.equals("R")) {
					commonDTO.setStatus("REJECT");
				} else if (status.equals("C")) {
					commonDTO.setStatus("CANCELLED");
				} else if (status.equals("P")) {
					commonDTO.setStatus("PENDING");
				} else if (status.equals("TC")) {
					commonDTO.setStatus("TEMPORARY CANCELLED");
				} else if (status.equals("H")) {
					commonDTO.setStatus("HOLD");
				} else if (status.equals("E")) {
					commonDTO.setStatus("EXPIRED");
				} else if (status.equals("G")) {
					commonDTO.setStatus("OTHER INSPECTION");
				} else {
					commonDTO.setStatus(null);
				}

				inquiryDetails.add(commonDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return inquiryDetails;
	}

	@Override
	public List<CommonDTO> getVehicleNo() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> vehicleNoList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  pm_vehicle_regno  " + "FROM public.nt_t_pm_application "
					+ "WHERE pm_status='A' and pm_vehicle_regno is not null and pm_vehicle_regno !='' order by pm_vehicle_regno ;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();

				commonDTO.setVehicleNo(rs.getString("pm_vehicle_regno"));
				vehicleNoList.add(commonDTO);

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

	public List<CommonDTO> counterdropdown() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = " select cou_counter_name,cou_counter_id from nt_r_counter where cou_status='I' and cou_trn_type='09'";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();

				commonDTO.setCounter(rs.getString("cou_counter_name"));
				commonDTO.setCounterId(rs.getString("cou_counter_id"));
				// make dto for code
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

	@Override
	public List<CommonDTO> getPermitNo() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> permitNoList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  pm_permit_no  " + "FROM public.nt_t_pm_application "
					+ "WHERE pm_status='A' and pm_permit_no is not null and pm_permit_no !='' order by pm_permit_no ;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();

				commonDTO.setPermitNo(rs.getString("pm_permit_no"));
				permitNoList.add(commonDTO);

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
	public List<CommonDTO> getApplicationNo() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> applicationNoList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  pm_application_no  " + "FROM public.nt_t_pm_application "
					+ "WHERE pm_status='A' and pm_application_no is not null and pm_application_no !='' order by pm_application_no ;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();

				commonDTO.setApplicationNo(rs.getString("pm_application_no"));
				applicationNoList.add(commonDTO);

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

	public void counterStatus(String counterId, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "update nt_r_counter "
					+ "set cou_status='A',cou_serving_queueno=NULL , cou_assigned_userid='" + user
					+ "' where cou_counter_id = '" + counterId + "'";
			ps = con.prepareStatement(query);
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

	public List<CommonDTO> counterDropdown(String trnType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select cou_counter_name,cou_counter_id " + "from nt_r_counter "
					+ "where cou_status='I' and cou_trn_type='" + trnType + "'";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();

				commonDTO.setCounter(rs.getString("cou_counter_name"));
				commonDTO.setCounterId(rs.getString("cou_counter_id"));
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

	@Override
	public void updateTaskStatus(String applicationNo, String prevTask, String currTask, String taskStatus,
			String user) {

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT * " + "FROM public.nt_t_task_det " + "where tsd_app_no=? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, applicationNo);
			rs = ps.executeQuery();

			if (rs.next() == true) {
				if (!(rs.getString("tsd_task_code").equals(currTask))) {

					// insert to history table
					String q = "INSERT INTO public.nt_h_task_his "
							+ "(tsd_seq, tsd_vehicle_no, tsd_app_no, tsd_task_code, tsd_status, created_by, created_date) "
							+ "VALUES(?, ?, ?, ?, ?, ?, ?); ";

					ps2 = con.prepareStatement(q);
					ps2.setInt(1, rs.getInt("tsd_seq"));
					ps2.setString(2, rs.getString("tsd_vehicle_no"));
					ps2.setString(3, applicationNo);
					ps2.setString(4, prevTask);
					ps2.setString(5, taskStatus);
					ps2.setString(6, rs.getString("created_by"));
					ps2.setTimestamp(7, rs.getTimestamp("created_date"));

					int i = ps2.executeUpdate();
					con.commit();
					try {
						if (ps2 != null)
							ps2.close();
					} catch (Exception e) {
						e.printStackTrace();
					}

					con.commit();

					if (i > 0) {
						// update task details table
						String q2 = "UPDATE public.nt_t_task_det "
								+ "SET tsd_task_code=?, tsd_status='O', created_by=?, created_date=? "
								+ "WHERE tsd_app_no=?";

						ps3 = con.prepareStatement(q2);
						ps3.setString(1, currTask);
						ps3.setString(4, applicationNo);
						ps3.setString(2, user);
						ps3.setTimestamp(3, timestamp);
						ps3.executeUpdate();
						con.commit();

						try {
							if (ps3 != null)
								ps3.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
						migratedService = (MigratedService) SpringApplicationContex.getBean("migratedService");
						String queueNumber = migratedService.findQueueNumberFromApplicationNo(applicationNo);
						if(applicationNo.startsWith("AAP")) {
							queueNumber =getQueueNumberFromQueMasterByAppNo(applicationNo);
						}

						if (queueNumber != null && !queueNumber.isEmpty() && !queueNumber.trim().equalsIgnoreCase("")) {
							migratedService.updateQueueNumberTaskInQueueMaster(queueNumber, applicationNo, currTask,
									"O");
						}
					}
				}
			}
		} catch (SQLException e) {
		    try {
		        if (con != null) {
		            con.rollback();
		        }
		    } catch (SQLException ex) {
		        ex.printStackTrace(); 
		    }
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(con);
		}
	}

	@Override
	public boolean updateTaskStatusCompletedForAmendments(String applicationNo, String currTask) {

		Connection con = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		PreparedStatement ps4 = null;
		ResultSet rs = null;
		boolean isUpdate = false;
		try {

			con = ConnectionManager.getConnection();

			String q2 = "SELECT * " + "FROM public.nt_t_task_det " + "where tsd_app_no=? ;";

			ps2 = con.prepareStatement(q2);
			ps2.setString(1, applicationNo);
			rs = ps2.executeQuery();

			if (rs.next() == true) {
				// insert in to history table
				String q3 = "INSERT INTO public.nt_h_task_his "
						+ "(tsd_seq, tsd_vehicle_no, tsd_app_no, tsd_task_code, tsd_status, created_by, created_date) "
						+ "VALUES(?, ?, ?, ?, 'O', ?, ?); ";

				ps3 = con.prepareStatement(q3);
				ps3.setInt(1, rs.getInt("tsd_seq"));
				ps3.setString(2, rs.getString("tsd_vehicle_no"));
				ps3.setString(3, applicationNo);
				ps3.setString(4, currTask);
				ps3.setString(5, rs.getString("created_by"));
				ps3.setTimestamp(6, rs.getTimestamp("created_date"));

				ps3.executeUpdate();
				con.commit();
				try {
					if (ps3 != null)
						ps3.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				isUpdate = true;
			}
			try {
				if (ps2 != null)
					ps2.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			// update task details table
			String q4 = "UPDATE public.nt_t_task_det " + "SET tsd_task_code=?, tsd_status='C' " + "WHERE tsd_app_no=?";
			ps4 = con.prepareStatement(q4);
			ps4.setString(1, currTask);
			ps4.setString(2, applicationNo);
			ps4.executeUpdate();

			con.commit();
			try {
				if (ps4 != null)
					ps4.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			migratedService = (MigratedService) SpringApplicationContex.getBean("migratedService");
			String queueNumber = migratedService.findQueueNumberFromApplicationNo(applicationNo);
			if (queueNumber != null && !queueNumber.isEmpty() && !queueNumber.trim().equalsIgnoreCase("")) {
				migratedService.updateQueueNumberTaskInQueueMaster(queueNumber, applicationNo, currTask, "C");
			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(con);
		}

		return isUpdate;

	}

	public void updateTaskStatusCompleted(String applicationNo, String currTask, String user) {

		Connection con = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		PreparedStatement ps4 = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			String q2 = "SELECT * " + "FROM public.nt_t_task_det " + "where tsd_app_no=? ;";

			ps2 = con.prepareStatement(q2);
			ps2.setString(1, applicationNo);
			rs = ps2.executeQuery();

			if (rs.next() == true) {
				// insert in to history table
				String q3 = "INSERT INTO public.nt_h_task_his "
						+ "(tsd_seq, tsd_vehicle_no, tsd_app_no, tsd_task_code, tsd_status, created_by, created_date) "
						+ "VALUES(?, ?, ?, ?, 'O', ?, ?); ";

				ps3 = con.prepareStatement(q3);
				ps3.setInt(1, rs.getInt("tsd_seq"));
				ps3.setString(2, rs.getString("tsd_vehicle_no"));
				ps3.setString(3, applicationNo);
				ps3.setString(4, currTask);
				//ps3.setString(5, rs.getString("created_by")); // commented and change as user due to live issue
				ps3.setString(5, user);
				//ps3.setTimestamp(6, rs.getTimestamp("created_date"));
				ps3.setTimestamp(6, timestamp);
				ps3.executeUpdate();
				con.commit();
				try {
					if (ps3 != null)
						ps3.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			try {
				if (ps2 != null)
					ps2.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			// update task details table

			String q4 = "UPDATE public.nt_t_task_det "
					+ "SET tsd_task_code=?, tsd_status='C', created_by=?, created_date=? " + "WHERE tsd_app_no=?";
			ps4 = con.prepareStatement(q4);
			ps4.setString(1, currTask);
			ps4.setString(2, user);
			ps4.setTimestamp(3, timestamp);
			ps4.setString(4, applicationNo);
			ps4.executeUpdate();
			con.commit();
			try {
				if (ps4 != null)
					ps4.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			migratedService = (MigratedService) SpringApplicationContex.getBean("migratedService");
			String queueNumber = migratedService.findQueueNumberFromApplicationNo(applicationNo);
			if (queueNumber != null && !queueNumber.isEmpty() && !queueNumber.trim().equalsIgnoreCase("")) {
				migratedService.updateQueueNumberTaskInQueueMaster(queueNumber, applicationNo, currTask, "C");
			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(con);
		}

	}

	@Override
	public void insertTaskDet(String vehicleNo, String appNo, String loginUSer, String taskCode, String taskStatus) {
		logger.info("insertTaskDet start");

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
					+ "VALUES(?, ?, ?, ?, ?, ?, ?)";

			try {
				stmt = con.prepareStatement(sql);

				stmt.setLong(1, seqNo);
				stmt.setString(2, vehicleNo);
				stmt.setString(3, appNo);
				stmt.setString(4, taskCode);
				stmt.setString(5, taskStatus);
				stmt.setString(6, loginUSer);
				stmt.setTimestamp(7, timestamp);

				stmt.executeUpdate();

				con.commit();
			}catch (SQLException e) {
				con.rollback();
			}
		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		logger.info("insertTaskDet end");
	}

	@Override
	public int updateDataInNT_T_TASK_DET(String vehicleNo, String appNo, String taskCode, String taskStatus) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		int tsdTaskSeq = 0;

		try {
			con = ConnectionManager.getConnection();

			String q2 = "UPDATE public.nt_t_task_det SET tsd_status=? WHERE tsd_app_no=? and tsd_vehicle_no=? and tsd_task_code=?";
			ps = con.prepareStatement(q2);
			ps.setString(1, taskStatus);
			ps.setString(2, appNo);
			ps.setString(3, vehicleNo);
			ps.setString(4, taskCode);

			ps.executeUpdate();
			con.commit();
			ConnectionManager.close(ps);

			String sql = "SELECT tsd_seq FROM public.nt_t_task_det WHERE tsd_app_no=? and tsd_vehicle_no=? and tsd_task_code=?";
			ps = con.prepareStatement(sql);
			ps.setString(1, appNo);
			ps.setString(2, vehicleNo);
			ps.setString(3, taskCode);

			rs = ps.executeQuery();

			while (rs.next()) {
				tsdTaskSeq = rs.getInt("tsd_seq");
			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return tsdTaskSeq;
	}

	@Override
	public void updateCounterQueueNo(String queueNo, String counterNo) {
		logger.info("updateCounterQueueNo start");

		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;

		try {

			con = ConnectionManager.getConnection();

			String sql3 = "UPDATE public.nt_r_counter SET cou_serving_queueno=? WHERE cou_counter_id=? ; ";

			ps = con.prepareStatement(sql3);

			ps.setString(1, queueNo);
			ps.setString(2, counterNo);

			ps.executeUpdate();

			con.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("updateCounterQueueNo error");
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		logger.info("updateCounterQueueNo end");
	}

	@Override
	public void updateCounterNo(String counterNo) {
		logger.info("updateCounterQueueNo start");

		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;

		try {

			con = ConnectionManager.getConnection();

			String sql3 = "UPDATE public.nt_r_counter SET cou_serving_queueno=null WHERE cou_counter_id=? ; ";

			ps = con.prepareStatement(sql3);

			ps.setString(1, counterNo);

			ps.executeUpdate();

			con.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("updateCounterQueueNo error");
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		logger.info("updateCounterQueueNo end");
	}

	public String taskStatus(String vehicleNo, String taskCode) {
		String taskStatus = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select tsd_status " + " from nt_h_task_his " + " where tsd_vehicle_no='" + vehicleNo + "' "
					+ " and tsd_task_code='" + taskCode + "'";

			ps = con.prepareStatement(query);
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
	public boolean checkApplicationNoAvailable(String applicationNo) {
		logger.info("checkApplicationNoAvailable start");

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			String sql = "select * from public.nt_t_pm_application where pm_application_no=?";

			ps = con.prepareStatement(sql);
			ps.setString(1, applicationNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				return true;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("findQueueNumberFromApplicationNo error: " + ex.toString());
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		logger.info("checkApplicationNoAvailable end");
		return false;
	}

	@Override
	public boolean checkTaskDetails(String applicationNo, String taskCodeOne) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isAvailable = false;

		try {
			con = ConnectionManager.getConnection();

			String query = "select DISTINCT tsd_app_no FROM public.nt_t_task_det "
					+ "where tsd_task_code=?  and tsd_status='C' and tsd_app_no=? ;";

			ps = con.prepareStatement(query);
			ps.setString(2, applicationNo);
			ps.setString(1, taskCodeOne);
			rs = ps.executeQuery();

			if (rs.next()) {
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
	public boolean checkTaskHistory(String applicationNo, String taskCodeOne) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isAvailable = false;

		try {
			con = ConnectionManager.getConnection();

			String query = "select DISTINCT tsd_app_no FROM public.nt_h_task_his "
					+ "where tsd_task_code=? and tsd_status='C' and tsd_app_no=? ;";

			ps = con.prepareStatement(query);
			ps.setString(2, applicationNo);
			ps.setString(1, taskCodeOne);
			rs = ps.executeQuery();

			if (rs.next()) {
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
	public List<CommonDTO> getHistoryData(CommonDTO commonDTO) {

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet rs2 = null;
		ResultSet rs = null;
		List<CommonDTO> inquiryDetails = new ArrayList<CommonDTO>();
		String status = null;
		String description = null;

		try {
			con = ConnectionManager.getConnection();

			String query1 = "select * from nt_t_pm_application where pm_application_no=? and pm_skipped_inspection='Y'";

			ps = con.prepareStatement(query1);
			ps.setString(1, commonDTO.getApplicationNo());
			rs = ps.executeQuery();

			if (rs.next() == false) {

				String query2 = "select tsd_app_no, tsd_task_code,tsd_status, nt_r_task.description, t1.created_by as created_by , t1.created_date as created_date  from "
						+ "(select * FROM public.nt_h_task_his  UNION select * FROM public.nt_t_task_det) As t1 "
						+ "inner join public.nt_r_task on nt_r_task.code=t1.tsd_task_code and (tsd_status='C' or tsd_status='I') "
						+ "where tsd_app_no=? order by  tsd_task_code; ";

				ps2 = con.prepareStatement(query2);
				ps2.setString(1, commonDTO.getApplicationNo());
				rs2 = ps2.executeQuery();

				while (rs2.next()) {

					CommonDTO dto = new CommonDTO();

					dto.setApplicationNo(rs2.getString("tsd_app_no"));
					dto.setTaskCode(rs2.getString("tsd_task_code"));
					dto.setCreatedBy(rs2.getString("created_by"));

					Timestamp ts = rs2.getTimestamp("created_date");
					Date date = new Date();
					date.setTime(ts.getTime());
					String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);

					dto.setCreatedDateString(formattedDate);
					status = rs2.getString("tsd_status");

					if (status.equals("O")) {
						dto.setTaskStatus("Ongoing");
					} else if (status.equals("C")) {
						dto.setTaskStatus("Complete");
					} else if (status.equals("I")) {
						dto.setTaskStatus("Incomplete");
					}

					description = rs2.getString("description");

					if (description.equals("New Permit Approval")) {

						dto.setTaskCodeDes("Permit Approval");

					} else {
						dto.setTaskCodeDes(rs2.getString("description"));
					}

					inquiryDetails.add(dto);
				}

			} else {

				String query2 = "select tsd_app_no, tsd_task_code,tsd_status, nt_r_task.description, t1.created_by as created_by , t1.created_date as created_date  from "
						+ "(select * FROM public.nt_h_task_his  UNION select * FROM public.nt_t_task_det) As t1 "
						+ "inner join public.nt_r_task on nt_r_task.code=t1.tsd_task_code and (tsd_status='C' or tsd_status='I') "
						+ "where tsd_app_no=? order by  tsd_task_code; ";

				ps2 = con.prepareStatement(query2);
				ps2.setString(1, commonDTO.getApplicationNo());
				rs2 = ps2.executeQuery();

				while (rs2.next()) {

					CommonDTO dto = new CommonDTO();

					dto.setApplicationNo(rs2.getString("tsd_app_no"));

					if (!rs2.getString("tsd_task_code").equals("PM101")) {

						dto.setTaskCode(rs2.getString("tsd_task_code"));
					} else {
						dto.setTaskCode("N/A");
					}
					dto.setCreatedBy(rs2.getString("created_by"));

					Timestamp ts = rs2.getTimestamp("created_date");
					Date date = new Date();
					date.setTime(ts.getTime());
					String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);

					dto.setCreatedDateString(formattedDate);
					status = rs2.getString("tsd_status");

					if (status.equals("O")) {
						dto.setTaskStatus("Ongoing");
					} else if (status.equals("C")) {
						dto.setTaskStatus("Complete");
					} else if (status.equals("I")) {
						dto.setTaskStatus("Incomplete");
					}

					description = rs2.getString("description");

					if (rs2.getString("tsd_task_code").equals("PM101")) {
						dto.setTaskCodeDes("Vehicle Inspection Skiped");
					} else {

						if (description.equals("New Permit Approval")) {

							dto.setTaskCodeDes("Permit Approval");

						} else {
							dto.setTaskCodeDes(rs2.getString("description"));
						}

					}

					inquiryDetails.add(dto);

				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(rs2);
			ConnectionManager.close(ps2);
			ConnectionManager.close(con);
		}
		return inquiryDetails;
	}

	@Override
	public void insertSurveyTaskDetails(String surveyReqNo, String taskCode, String taskStatus, String loginUser) {
		logger.info("insertSurveyTaskDet start");

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_survey_task_det");

			String sql = "INSERT INTO public.nt_t_survey_task_det "
					+ "(tsd_seq, tsd_request_no, tsd_task_code, tsd_status, created_by, created_date) "
					+ "VALUES(?, ?, ?, ?, ?, ?); ";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, surveyReqNo);
			stmt.setString(3, taskCode);
			stmt.setString(4, taskStatus);
			stmt.setString(5, loginUser);
			stmt.setTimestamp(6, timestamp);

			stmt.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		logger.info("insertSurveyTaskDet end");
	}

	@Override
	public void updateSurveyTaskDetails(String surveyReqNo, String surveyNo, String currTask, String taskStatus,
			String loginUser) {
		logger.info("updateSurveyTaskDetails start");
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT * " + "FROM public.nt_t_survey_task_det " + "where tsd_request_no=? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, surveyReqNo);
			rs = ps.executeQuery();

			if (rs.next() == true) {
				if (!rs.getString("tsd_task_code").equals(currTask) || !rs.getString("tsd_status").equals(taskStatus)) {
					// insert to history table
					String q = "INSERT INTO public.nt_h_survey_task_his "
							+ "(tsd_seq, tsd_request_no, tsd_survey_no, tsd_task_code, tsd_status, created_by, created_date) "
							+ "VALUES(?, ?, ?, ?, ?, ?, ?); ";

					ps2 = con.prepareStatement(q);
					ps2.setInt(1, rs.getInt("tsd_seq"));
					ps2.setString(2, surveyReqNo);
					ps2.setString(3, rs.getString("tsd_survey_no"));
					ps2.setString(4, rs.getString("tsd_task_code"));
					ps2.setString(5, rs.getString("tsd_status"));
					ps2.setString(6, rs.getString("created_by"));
					ps2.setTimestamp(7, rs.getTimestamp("created_date"));

					ps2.executeUpdate();
					con.commit();
					try {
						if (ps2 != null)
							ps2.close();
					} catch (Exception e) {
						e.printStackTrace();
					}

					// update task details table
					String q2 = "UPDATE public.nt_t_survey_task_det "
							+ "SET tsd_task_code=?, tsd_status=?, tsd_survey_no=?, created_by=?, created_date=? "
							+ "WHERE tsd_request_no=?; ";
					ps3 = con.prepareStatement(q2);
					ps3.setString(1, currTask);
					ps3.setString(2, taskStatus);
					if (surveyNo != null && !surveyNo.trim().isEmpty()) {
						ps3.setString(3, surveyNo);
					} else {
						ps3.setNull(3, Types.VARCHAR);
					}
					ps3.setString(4, loginUser);
					ps3.setTimestamp(5, timestamp);
					ps3.setString(6, surveyReqNo);

					ps3.executeUpdate();
					con.commit();
					try {
						if (ps3 != null)
							ps3.close();
					} catch (Exception e) {
						e.printStackTrace();
					}

					con.commit();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(con);
		}

		logger.info("updateSurveyTaskDetails end");

	}

	@Override
	public List<CommonDTO> countersDropdown(String trnType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select cou_counter_name,cou_counter_id " + "from nt_r_counter "
					+ "where cou_status='I' and cou_trn_type='" + trnType + "'";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();

				commonDTO.setCounter(rs.getString("cou_counter_name"));
				commonDTO.setCounterId(rs.getString("cou_counter_id"));
				// make dto for code
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

	@Override
	public boolean checkTaskOnTaskDetails(String applicationNo, String taskCode, String status) {
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
			ps.setString(3, applicationNo);
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
	public String taskStatusOnTaskDetail(String strVehicleNo, String taskCode, String applicationNo) {

		String taskStatus = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String today = df.format(date);

		try {
			con = ConnectionManager.getConnection();

			String query = "select tsd_status " + " from nt_t_task_det " + " where tsd_vehicle_no='" + strVehicleNo
					+ "' " + " and tsd_app_no='" + applicationNo + "'" + " and tsd_task_code='" + taskCode + "'"
					+ " and to_char(created_date,'dd/MM/yyyy') like '" + today + "%'";

			ps = con.prepareStatement(query);
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
	public void updateTenderTaskDetails(String tenderReferenceNo, String tskCode, String taskStatus) {
		logger.info("updateTenderTaskDetails start");
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT * " + "FROM public.nt_t_survey_task_det " + "where tsd_tender_refno=? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, tenderReferenceNo);
			rs = ps.executeQuery();

			if (rs.next() == true) {
				if (!rs.getString("tsd_task_code").equals(tskCode) || !rs.getString("tsd_status").equals(taskStatus)) {
					// insert to history table
					String q = "INSERT INTO public.nt_h_survey_task_his "
							+ "(tsd_seq, tsd_request_no, tsd_survey_no, tsd_task_code, tsd_status, created_by, created_date, tsd_tender_refno) "
							+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?); ";

					ps2 = con.prepareStatement(q);
					ps2.setInt(1, rs.getInt("tsd_seq"));
					ps2.setString(2, tenderReferenceNo);
					ps2.setString(3, rs.getString("tsd_survey_no"));
					ps2.setString(4, rs.getString("tsd_task_code"));
					ps2.setString(5, rs.getString("tsd_status"));
					ps2.setString(6, rs.getString("created_by"));
					ps2.setTimestamp(7, rs.getTimestamp("created_date"));
					ps2.setString(8, rs.getString("tsd_tender_refno"));

					ps2.executeUpdate();
					con.commit();

					try {
						if (ps2 != null)
							ps2.close();
					} catch (Exception e) {
						e.printStackTrace();
					}

					// update task details table
					String q2 = "UPDATE public.nt_t_survey_task_det " + "SET tsd_task_code=?, tsd_status=? "
							+ "WHERE tsd_tender_refno=?; ";
					ps3 = con.prepareStatement(q2);
					ps3.setString(1, tskCode);
					ps3.setString(2, taskStatus);
					ps3.setString(3, tenderReferenceNo);

					ps3.executeUpdate();
					con.commit();
					try {
						if (ps3 != null)
							ps3.close();
					} catch (Exception e) {
						e.printStackTrace();
					}

					con.commit();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(con);
		}
		logger.info("updateTenderTaskDetails end");
	}

	@Override
	public boolean checkTaskOnSurveyTaskDetails(String tenderReferanceNo, String taskCode, String status) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isTaskAvailable = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "select  tsd_tender_refno, tsd_task_code, tsd_status " + "FROM public.nt_t_survey_task_det "
					+ "where tsd_task_code=? and tsd_status=? and tsd_tender_refno=? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, taskCode);
			ps.setString(2, status);
			ps.setString(3, tenderReferanceNo);
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
	public void updatePhotoUploadStatus(String generatedApplicationNo, String photoUpload) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			String sql = "UPDATE nt_t_pm_application SET pm_skipped_inspection = ? WHERE pm_application_no = ?";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, photoUpload);
			stmt.setString(2, generatedApplicationNo);
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
	public String getPhotoUploadStatus(String generatedApplicationNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String photoUpload = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT pm_skipped_inspection FROM public.nt_t_pm_application WHERE pm_application_no = ?";

			ps = con.prepareStatement(query);
			ps.setString(1, generatedApplicationNo);

			rs = ps.executeQuery();

			while (rs.next()) {

				photoUpload = rs.getString("pm_skipped_inspection");

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return photoUpload;

	}

	@Override
	public void duplicatePhotoUpload(String generatedApplicationNo, String oldApplicationNo) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			String sql = "INSERT INTO nt_t_owner_vehi_images (ovi_seq_no, ovi_app_no, ovi_owner_image, ovi_image1, ovi_image2, ovi_image3, ovi_image4, ovi_image5, ovi_image6, ovi_created_by, ovi_created_date, ovi_modified_by, ovi_modified_date, ovi_status) "
					+

					"SELECT nextval('seq_nt_t_owner_vehi_images'), ?, ovi_owner_image, ovi_image1, ovi_image2, ovi_image3, ovi_image4, ovi_image5, ovi_image6, ovi_created_by, ovi_created_date, ovi_modified_by, ovi_modified_date, ovi_status FROM nt_t_owner_vehi_images "

					+ "WHERE ovi_app_no=?";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, generatedApplicationNo);
			stmt.setString(2, oldApplicationNo);

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
	public void duplicateActionPoints(String generatedApplicationNo, String oldApplicationNo) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			String sql = "INSERT INTO nt_t_vehicle_inspec_det (vid_seq_no, vid_app_no, vid_sec_seq, vid_remark, vid_exist, vid_created_by, vid_created_date, vid_modified_by, vid_modified_date) "
					+ "SELECT nextval('seq_nt_t_vehicle_inspec_det'), ?, vid_sec_seq, vid_remark, vid_exist, vid_created_by, vid_created_date, vid_modified_by, vid_modified_date FROM nt_t_vehicle_inspec_det WHERE vid_app_no=?";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, generatedApplicationNo);
			stmt.setString(2, oldApplicationNo);

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
	public boolean taskStatusOnSurveyTaskDetails(String surveyNo, String taskCode, String status) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isTaskAvailable = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "select  tsd_tender_refno, tsd_task_code, tsd_status " + "FROM public.nt_t_survey_task_det "
					+ "where tsd_task_code=? and tsd_status=? and tsd_request_no=? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, taskCode);
			ps.setString(2, status);
			ps.setString(3, surveyNo);
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
	public String applicationStatus(String applicationNo) {
		String taskStatus = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select pm_status from nt_t_pm_application where pm_application_no ='" + applicationNo + "'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				taskStatus = rs.getString("pm_status");

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
	public void insertPermitPrintInfo(String applicationNo, String PermitNo, String VehicleNo, String logedUser) {
		logger.info("insert permit print info start");

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_permit_printing_det");

			String sql = "INSERT INTO public.nt_m_permit_printing_det "
					+ "(seqno, ppd_permitno, ppd_vehicleno, ppd_applicationno, ppd_created_by, ppd_created_date,"
					+ "ppd_versionno) VALUES (?, ?, ?, ?, ?, ?, ?);";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, PermitNo);
			stmt.setString(3, VehicleNo);
			stmt.setString(4, applicationNo);
			stmt.setString(5, logedUser);
			stmt.setTimestamp(6, timestamp);
			int vesionNO = getPermitPrintedVersionNo(applicationNo); /* Getting new version no. */
			stmt.setInt(7, vesionNO);

			stmt.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		logger.info("insert permit print info end");

	}

	@Override
	public Integer getPermitPrintedVersionNo(String applicationNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int result = 0;
		int versionNo = 0;

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT ppd_versionno FROM public.nt_m_permit_printing_det where ppd_applicationno=? "
					+ "ORDER BY ppd_created_date desc LIMIT 1";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, applicationNo);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getInt("ppd_versionno");
			}

			if (result != 0) {
				versionNo = result + 1;
			} else {
				versionNo = 1;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return versionNo;
	}

	@Override
	public int dashboardPendingCount(String title) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int count = 0;

		try {
			con = ConnectionManager.getConnection();

			String query = null;

			switch (title) {
			case "approvePendingEmployee":
				query = "SELECT COUNT(distinct a.emp_no) AS count "
						+ " from public.nt_m_employee a inner join nt_m_emp_department on a.emp_no = nt_m_emp_department.dep_emp_no"
						+ " inner join nt_m_emp_designation on a.emp_no = nt_m_emp_designation.des_emp_no "
						+ " inner join nt_r_department  on  nt_m_emp_department.dep_dept_code= nt_r_department.code "
						+ " inner join nt_r_designation  on  nt_m_emp_designation.des_desig_code= nt_r_designation.code "
						+ " left outer join public.nt_r_location on a.emp_location = nt_r_location.code "
						+ " where emp_status=? ; ";
				ps = con.prepareStatement(query);
				ps.setString(1, "P");

				break;

			case "newPermitApproval":
				query = "SELECT COUNT(tsd_seq) AS count FROM public.nt_t_task_det WHERE tsd_task_code = ? AND tsd_status = ?";
				ps = con.prepareStatement(query);
				ps.setString(1, "PM200");
				ps.setString(2, "C");

				break;

			case "approveVoucher":
				query = "SELECT COUNT(tsd_seq) AS count FROM public.nt_t_task_det WHERE tsd_task_code = ? AND tsd_status = ?";
				ps = con.prepareStatement(query);
				ps.setString(1, "PM300");
				ps.setString(2, "C");

				break;

			case "printPermit":
				query = "SELECT COUNT(tsd_seq) AS count FROM public.nt_t_task_det WHERE tsd_task_code = ? AND tsd_status = ?";
				ps = con.prepareStatement(query);
				ps.setString(1, "PM302");
				ps.setString(2, "C");

				break;

			case "permitRenewalApproval":
				query = "SELECT COUNT(tsd_seq) AS count FROM public.nt_t_task_det WHERE tsd_task_code = ? AND tsd_status = ?";
				ps = con.prepareStatement(query);
				ps.setString(1, "PR200");
				ps.setString(2, "C");

				break;

			case "grantApprovalforAmmendments":
				query = "SELECT COUNT(tsd_seq) AS count FROM public.nt_t_task_det WHERE tsd_task_code = ? AND tsd_status = ?";
				ps = con.prepareStatement(query);
				ps.setString(1, "AM105");
				ps.setString(2, "C");

				break;

			case "grantApprovalforAmmendmentsDirector":
				query = "SELECT COUNT(amd_application_no) AS count FROM public.nt_t_task_det A inner join public.nt_m_amendments B on A.tsd_app_no=B.amd_application_no WHERE tsd_task_code = 'PM101' AND tsd_status = 'C' and B.amd_status='P'";
				ps = con.prepareStatement(query);

				break;

			case "grantApprovalforAmmendmentsChairman":
				query = "SELECT COUNT(amd_application_no) AS count FROM public.nt_m_amendments WHERE amd_status = 'SA'";
				ps = con.prepareStatement(query);

				break;

			case "approveSurveyProcessRequests":
				query = "SELECT COUNT(tsd_seq) AS count FROM public.nt_t_task_det WHERE tsd_task_code = ? AND tsd_status = ?";
				ps = con.prepareStatement(query);
				ps.setString(1, "SU002");
				ps.setString(2, "C");

				break;

			case "approveTenderAdvertisement":
				query = "SELECT COUNT(tsd_seq) AS count FROM public.nt_t_task_det WHERE tsd_task_code = ? AND tsd_status = ?";
				ps = con.prepareStatement(query);
				ps.setString(1, "TD002");
				ps.setString(2, "C");

				break;

			case "approveElectedBidder":
				query = "SELECT COUNT(tsd_seq) AS count FROM public.nt_t_task_det WHERE tsd_task_code = ? AND tsd_status = ?";
				ps = con.prepareStatement(query);
				ps.setString(1, "TD016");
				ps.setString(2, "C");

				break;

			case "committeeBoardAuthorization":
				query = "SELECT COUNT(seqno) AS count FROM public.nt_m_committee WHERE com_isauthorized != ?";
				ps = con.prepareStatement(query);
				ps.setString(1, "Y");

				break;
			case "committeeApproval":
				query = "SELECT COUNT(tsd_seq) AS count FROM public.nt_t_task_det WHERE tsd_task_code IN(?,?,?,?) AND tsd_status = ?";
				ps = con.prepareStatement(query);
				ps.setString(1, "AM100");
				ps.setString(2, "SU009");
				ps.setString(3, "TD013");
				ps.setString(4, "TD014");
				ps.setString(5, "C");

				break;

			case "boardApproval":
				query = "SELECT COUNT(tsd_seq) AS count FROM public.nt_t_task_det WHERE tsd_task_code IN(?,?,?,?) AND tsd_status = ?";
				ps = con.prepareStatement(query);
				ps.setString(1, "AM101");
				ps.setString(2, "SU010");
				ps.setString(3, "TD015");
				ps.setString(4, "TD016");
				ps.setString(5, "C");

				break;

			case "generateReceipt":
				query = "SELECT COUNT(tsd_seq) AS count FROM public.nt_t_task_det WHERE tsd_task_code = ? AND tsd_status = ?";
				ps = con.prepareStatement(query);
				ps.setString(1, "PM301");
				ps.setString(2, "C");

				break;

			case "cancelledPermits":
				query = "SELECT COUNT(tsd_seq) AS count FROM public.nt_t_task_det WHERE tsd_task_code = ? AND tsd_status = ?";
				ps = con.prepareStatement(query);
				ps.setString(1, "SU002");
				ps.setString(2, "C");

				break;
			}

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
	public boolean checkTaskOnSurveyHisDetails(String surveyNo, String taskCode, String status) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isTaskAvailable = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "select tsd_task_code,tsd_status\r\n" + "from public.nt_h_survey_task_his\r\n"
					+ "where tsd_survey_no = ? and tsd_task_code = ? and tsd_status = ?";

			ps = con.prepareStatement(query);
			ps.setString(1, surveyNo);
			ps.setString(2, taskCode);
			ps.setString(3, status);

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
	public void updateTaskStatusTenderInSurveyTaskTabel(String tenderRefNo, String prevTask, String currTask,
			String taskStatus, String userName) {

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT * " + "FROM public.nt_t_survey_task_det " + "where tsd_tender_refno=? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, tenderRefNo);
			rs = ps.executeQuery();

			if (rs.next() == true) {
				if (!(rs.getString("tsd_task_code").equals(currTask))) {

					// insert to history table
					String q = "INSERT INTO public.nt_h_survey_task_his "
							+ "(tsd_seq, tsd_task_code, tsd_status, created_by, created_date, tsd_tender_refno) "
							+ "VALUES(?, ?, ?, ?, ?, ?);";

					ps2 = con.prepareStatement(q);

					ps2.setInt(1, rs.getInt("tsd_seq"));
					ps2.setString(2, prevTask);
					ps2.setString(3, taskStatus);
					ps2.setString(4, rs.getString("created_by"));
					ps2.setTimestamp(5, rs.getTimestamp("created_date"));
					ps2.setString(6, tenderRefNo);
					int i = ps2.executeUpdate();
					con.commit();
					try {
						if (ps2 != null)
							ps2.close();
					} catch (Exception e) {
						e.printStackTrace();
					}

					con.commit();

					if (i > 0) {
						// update task details table
						String q2 = "UPDATE public.nt_t_survey_task_det "
								+ "SET tsd_task_code=?, tsd_status='O', created_by=?, created_date=? "
								+ "WHERE tsd_tender_refno=?";

						ps3 = con.prepareStatement(q2);
						ps3.setString(1, currTask);
						ps3.setString(4, tenderRefNo);
						ps3.setString(2, userName);
						ps3.setTimestamp(3, timestamp);
						ps3.executeUpdate();
						con.commit();

						try {
							if (ps3 != null)
								ps3.close();
						} catch (Exception e) {
							e.printStackTrace();
						}

						con.commit();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
	}

	@Override
	public void updateTaskStatusCompletedTenderInSurveyTaskTabel(String tenderRefNo, String currTask) {

		Connection con = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		PreparedStatement ps4 = null;
		ResultSet rs = null;
		try {

			con = ConnectionManager.getConnection();

			String q2 = "SELECT * FROM public.nt_t_survey_task_det where tsd_tender_refno=?  ;";

			ps2 = con.prepareStatement(q2);
			ps2.setString(1, tenderRefNo);
			rs = ps2.executeQuery();

			if (rs.next() == true) {
				// insert in to history table
				String q3 = "INSERT INTO public.nt_h_survey_task_his "
						+ "(tsd_seq, tsd_task_code, tsd_status, created_by, created_date, tsd_tender_refno ) "
						+ "VALUES(?, ?, 'O', ?, ?, ?); ";

				ps3 = con.prepareStatement(q3);
				ps3.setInt(1, rs.getInt("tsd_seq"));
				ps3.setString(2, currTask);
				ps3.setString(3, rs.getString("created_by"));
				ps3.setTimestamp(4, rs.getTimestamp("created_date"));
				ps3.setString(5, tenderRefNo);

				ps3.executeUpdate();
				con.commit();
				try {
					if (ps3 != null)
						ps3.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			try {
				if (ps2 != null)
					ps2.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			// update task details table
			String q4 = "UPDATE public.nt_t_survey_task_det " + "SET tsd_task_code=?, tsd_status='C' "
					+ "WHERE tsd_tender_refno=?";

			ps4 = con.prepareStatement(q4);
			ps4.setString(1, currTask);
			ps4.setString(2, tenderRefNo);
			ps4.executeUpdate();
			con.commit();
			try {
				if (ps4 != null)
					ps4.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(con);
		}

	}

	@Override
	public void updateSurveyTaskDet(String surveyReqNo, String surveyNo, String currTask, String taskStatus,
			String loginUser) {
		logger.info("updateSurveyTaskDetails start");
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();
			// update task details table
			String q2 = "UPDATE public.nt_t_survey_task_det "
					+ "SET tsd_task_code=?, tsd_status=?, tsd_survey_no=?, created_by=?, created_date=? "
					+ "WHERE tsd_request_no=?; ";
			ps3 = con.prepareStatement(q2);
			ps3.setString(1, currTask);
			ps3.setString(2, taskStatus);
			if (surveyNo != null && !surveyNo.trim().isEmpty()) {
				ps3.setString(3, surveyNo);
			} else {
				ps3.setNull(3, Types.VARCHAR);
			}
			ps3.setString(4, loginUser);
			ps3.setTimestamp(5, timestamp);
			ps3.setString(6, surveyReqNo);

			ps3.executeUpdate();
			con.commit();
			try {
				if (ps3 != null)
					ps3.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(con);
		}

		logger.info("updateSurveyTaskDetails end");
	}

	@Override
	public boolean checkTaskOnSurveyHisDetailsByTenderRefNo(String tenderRefNo, String taskCode, String status) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isTaskAvailable = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "select tsd_task_code,tsd_status\r\n" + "from public.nt_h_survey_task_his\r\n"
					+ "where tsd_tender_refno = ? and tsd_task_code = ? and tsd_status = ?";

			ps = con.prepareStatement(query);
			ps.setString(1, tenderRefNo);
			ps.setString(2, taskCode);
			ps.setString(3, status);

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
	public boolean checkTaskHisByApplication(String applicationNo, String taskCode, String taskStatus) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isTaskAvailable = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct tsd_app_no " + "from public.nt_h_task_his "
					+ "where tsd_app_no = ? and tsd_task_code = ? and tsd_status=? ";

			ps = con.prepareStatement(query);
			ps.setString(1, applicationNo);
			ps.setString(2, taskCode);
			ps.setString(3, taskStatus);

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
	public boolean checkTaskDetailsInSubsidy(String reqNo, String taskCode, String status) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isTaskAvailable = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM public.nt_t_subsidy_task_det "
					+ "where (tsd_task_code=? and tsd_status=?  and tsd_request_no=?); ";

			ps = con.prepareStatement(query);
			ps.setString(1, taskCode);
			ps.setString(2, status);

			ps.setString(3, reqNo);
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
	public boolean insertTaskDetailsSubsidy(String reqValue, String value, String loginUSer, String taskCode,
			String status, String surviceReferenceNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		boolean success = true;
		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_subsidy_task_det");

			String sql = "INSERT INTO public.nt_t_subsidy_task_det "
					+ "(tsd_seq, tsd_request_no,tsd_service_no, tsd_task_code, tsd_status, created_by, created_date,tsd_reference_no) "
					+ "VALUES(?, ?, ?, ?, ?, ?,?,?);";

			stmt = con.prepareStatement(sql);

			try {
				stmt.setLong(1, seqNo);
				stmt.setString(2, reqValue);
				stmt.setString(3, value);
				stmt.setString(4, taskCode);
				stmt.setString(5, status);
				stmt.setString(6, loginUSer);
				stmt.setTimestamp(7, timestamp);
				stmt.setString(8, surviceReferenceNo);
				stmt.executeUpdate();

				con.commit();
			}catch (SQLException e) {
				con.rollback();
				e.printStackTrace();
				success = false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			success = false;

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
		return success;

	}

	@Override
	public void updateTaskStatusSubsidyTaskTabel(String requestNo, String serviceNo, String referenceNo,
			String prevTask, String currTask, String taskStatus, String userName) {

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		String WHERE_SQL = "";
		boolean whereadded = false;

		if (requestNo != null && !requestNo.equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE tsd_request_no = " + "'" + requestNo + "' ";
			whereadded = true;
		}
		if (serviceNo != null && !serviceNo.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND tsd_service_no = " + "'" + serviceNo + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE tsd_service_no = " + "'" + serviceNo + "' ";
				whereadded = true;
			}
		}
		if (referenceNo != null && !referenceNo.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND tsd_reference_no = " + "'" + referenceNo + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE tsd_reference_no = " + "'" + referenceNo + "' ";
				whereadded = true;
			}
		}

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT * " + "FROM public.nt_t_subsidy_task_det " + WHERE_SQL;

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			if (rs.next() == true) {
				if (!(rs.getString("tsd_task_code").equals(currTask))) {

					// insert to history table
					String q = "INSERT INTO public.nt_h_subsidy_task_his "
							+ "(tsd_seq, tsd_request_no, tsd_reference_no, tsd_service_no, tsd_task_code, tsd_status, created_by, created_date) "
							+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?);";

					ps2 = con.prepareStatement(q);

					ps2.setInt(1, rs.getInt("tsd_seq"));

					if (requestNo != null && !requestNo.equals("")) {
						ps2.setString(2, requestNo);
					} else {
						ps2.setString(2, null);
					}
					if (referenceNo != null && !referenceNo.equals("")) {
						ps2.setString(3, referenceNo);
					} else {
						ps2.setString(3, null);
					}
					if (serviceNo != null && !serviceNo.equals("")) {
						ps2.setString(4, serviceNo);
					} else {
						ps2.setString(4, null);
					}
					ps2.setString(5, prevTask);
					ps2.setString(6, taskStatus);
					ps2.setString(7, rs.getString("created_by"));
					ps2.setTimestamp(8, rs.getTimestamp("created_date"));

					int i = ps2.executeUpdate();
					con.commit();
					try {
						if (ps2 != null)
							ps2.close();
					} catch (Exception e) {
						e.printStackTrace();
					}

					con.commit();

					if (i > 0) {
						// update task details table
						String q2 = "UPDATE public.nt_t_subsidy_task_det "
								+ "SET tsd_task_code=?, tsd_status='O', created_by=?, created_date=?, "
								+ " tsd_request_no=? , tsd_reference_no=? , tsd_service_no=? " + WHERE_SQL;

						ps3 = con.prepareStatement(q2);
						ps3.setString(1, currTask);
						ps3.setString(2, userName);
						ps3.setTimestamp(3, timestamp);
						if (requestNo != null && !requestNo.equals("")) {
							ps3.setString(4, requestNo);
						} else {
							ps3.setString(4, null);
						}
						if (referenceNo != null && !referenceNo.equals("")) {
							ps3.setString(5, referenceNo);
						} else {
							ps3.setString(5, null);
						}
						if (serviceNo != null && !serviceNo.equals("")) {
							ps3.setString(6, serviceNo);
						} else {
							ps3.setString(6, null);
						}

						ps3.executeUpdate();
						con.commit();

						try {
							if (ps3 != null)
								ps3.close();
						} catch (Exception e) {
							e.printStackTrace();
						}

						con.commit();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(con);
		}

	}

	@Override
	public boolean updateTaskStatusCompletedSubsidyTaskTabel(String requestNo, String serviceNo, String referenceNo,
			String currTask, String status) {
		Connection con = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		PreparedStatement ps4 = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		boolean success = true;

		String WHERE_SQL = "";
		boolean whereadded = false;

		if (requestNo != null && !requestNo.equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE tsd_request_no = " + "'" + requestNo + "' ";
			whereadded = true;
		}
		if (serviceNo != null && !serviceNo.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND tsd_service_no = " + "'" + serviceNo + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE tsd_service_no = " + "'" + serviceNo + "' ";
				whereadded = true;
			}
		}
		if (referenceNo != null && !referenceNo.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND tsd_reference_no = " + "'" + referenceNo + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE tsd_reference_no = " + "'" + referenceNo + "' ";
				whereadded = true;
			}
		}

		try {

			con = ConnectionManager.getConnection();

			String q2 = "SELECT * FROM public.nt_t_subsidy_task_det " + WHERE_SQL;

			ps2 = con.prepareStatement(q2);
			rs = ps2.executeQuery();

			if (rs.next() == true) {
				// insert in to history table
				String q3 = "INSERT INTO public.nt_h_subsidy_task_his "
						+ "(tsd_seq, tsd_request_no, tsd_reference_no, tsd_service_no, tsd_task_code, tsd_status, created_by, created_date) "
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?);";

				ps3 = con.prepareStatement(q3);

				try {
					ps3.setInt(1, rs.getInt("tsd_seq"));

					if (requestNo != null && !requestNo.equals("")) {
						ps3.setString(2, requestNo);
					} else {
						ps3.setString(2, null);
					}
					if (referenceNo != null && !referenceNo.equals("")) {
						ps3.setString(3, referenceNo);
					} else {
						ps3.setString(3, null);
					}
					if (serviceNo != null && !serviceNo.equals("")) {
						ps3.setString(4, serviceNo);
					} else {
						ps3.setString(4, null);
					}

					ps3.setString(5, rs.getString("tsd_task_code"));
					if (rs.getString("tsd_status") == null) {
						ps3.setString(6, "");
					} else {
						ps3.setString(6, rs.getString("tsd_status"));
					}
					ps3.setString(7, rs.getString("created_by"));
					ps3.setTimestamp(8, rs.getTimestamp("created_date"));

					ps3.executeUpdate();
					con.commit();
				}catch (SQLException e) {
					con.rollback();
					e.printStackTrace();
					success = false;
				}
			}

			// update task details table
			String q4 = "UPDATE public.nt_t_subsidy_task_det SET tsd_task_code=?, tsd_status=?, created_date=? "
					+ WHERE_SQL;

			ps4 = con.prepareStatement(q4);
			
			try {
				ps4.setString(1, currTask);
				ps4.setString(2, status);
				ps4.setTimestamp(3, timestamp);

				ps4.executeUpdate();
				con.commit();
			}catch (SQLException e) {
				con.rollback();
				e.printStackTrace();
				success = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps2);
			ConnectionManager.close(ps3);
			ConnectionManager.close(ps4);
			ConnectionManager.close(con);
		}
		return success;

	}

	@Override
	public boolean checkAccessPermission(String loginUser, String functionCode, String activityCode) {
		boolean status = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			int count = 0;
			String query1 = "select distinct count(b.gur_user_id) as numCount from nt_t_granted_user_role b, nt_r_fun_role_activity c,nt_r_function d "
					+ "				where b.gur_role_code = c.fra_role_code and d.code = c.fra_function_code "
					+ "				and b.gur_status = 'A' and b.gur_user_id = '" + loginUser
					+ "' and c.fra_function_code = '" + functionCode + "' and c.fra_activity_code = '" + activityCode
					+ "'";
			ps = con.prepareStatement(query1);

			rs = ps.executeQuery();

			while (rs.next()) {
				count = rs.getInt("numCount");
			}

			if (count > 0) {
				status = true;
			} else {
				status = false;
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

	@Override
	public int checkActionPointsCount(String appNo) {
		int count = 0;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query1 = "SELECT COUNT(vid_app_no) AS numCount FROM nt_t_vehicle_inspec_det WHERE vid_app_no='"
					+ appNo + "'";
			ps = con.prepareStatement(query1);
			rs = ps.executeQuery();

			while (rs.next()) {
				count = rs.getInt("numCount");
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
	public void sendEmail(String empNo, String message, String subject) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		String empEmail = null;

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT A.emp_moblile_no, A.emp_surname,A.emp_email_add,B.description AS emp_title"
					+ " FROM nt_m_employee A INNER JOIN nt_r_title B ON A.emp_title = B.code" + " WHERE A.emp_no =?";

			stmt = con.prepareStatement(query);
			stmt.setString(1, empNo);
			rs = stmt.executeQuery();

			while (rs.next()) {
				empEmail = rs.getString("emp_email_add");
			}
			ConnectionManager.close(stmt);

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_pending_emails");

			String sql = "INSERT INTO nt_t_pending_emails(seq,alert_type,mail_message,receipient_email,status,message_subject) VALUES (?,?,?,?,?,?)";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, "email");
			stmt.setString(3, message);
			stmt.setString(4, empEmail);
			stmt.setString(5, "P");
			stmt.setString(6, subject);

			stmt.executeUpdate();
			con.commit();

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

	}

	@Override
	public void sendSMS(String empNo, String message, String subject) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		String empMobile = null;

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT A.emp_moblile_no, A.emp_surname,A.emp_email_add,B.description AS emp_title"
					+ " FROM nt_m_employee A INNER JOIN nt_r_title B ON A.emp_title = B.code" + " WHERE A.emp_no =?";

			stmt = con.prepareStatement(query);
			stmt.setString(1, empNo);
			rs = stmt.executeQuery();

			while (rs.next()) {
				empMobile = rs.getString("emp_moblile_no");
			}
			ConnectionManager.close(stmt);

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_pending_alerts");

			String sql2 = "INSERT INTO nt_t_pending_alerts(seq,alert_type,sms_message,receipient_mobileno,status,message_subject) VALUES (?,?,?,?,?,?)";

			stmt = con.prepareStatement(sql2);

			stmt.setLong(1, seqNo);
			stmt.setString(2, "sms");
			stmt.setString(3, message);
			stmt.setString(4, empMobile);
			stmt.setString(5, "P");
			stmt.setString(6, subject);

			stmt.executeUpdate();
			con.commit();

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

	}

	@Override
	public void updateSurveyTaskDetailsBySurveyNo(String surveyReqNo, String surveyNo, String currTask,
			String taskStatus, String loginUser) {
		logger.info("updateSurveyTaskDetails start");
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT * " + "FROM public.nt_t_survey_task_det " + "where tsd_survey_no=? ;";
			ps = con.prepareStatement(query);
			ps.setString(1, surveyNo);

			rs = ps.executeQuery();

			if (rs.next() == true) {
				if (!rs.getString("tsd_task_code").equals(currTask) || !rs.getString("tsd_status").equals(taskStatus)) {
					// insert to history table
					String q = "INSERT INTO public.nt_h_survey_task_his "
							+ "(tsd_seq, tsd_request_no, tsd_survey_no, tsd_task_code, tsd_status, created_by, created_date) "
							+ "VALUES(?, ?, ?, ?, ?, ?, ?); ";

					ps2 = con.prepareStatement(q);
					ps2.setInt(1, rs.getInt("tsd_seq"));
					ps2.setString(2, surveyReqNo);
					ps2.setString(3, rs.getString("tsd_survey_no"));
					ps2.setString(4, rs.getString("tsd_task_code"));
					ps2.setString(5, rs.getString("tsd_status"));
					ps2.setString(6, rs.getString("created_by"));
					ps2.setTimestamp(7, rs.getTimestamp("created_date"));

					ps2.executeUpdate();
					con.commit();

					// update task details table
					String q2 = "UPDATE public.nt_t_survey_task_det "
							+ "SET tsd_task_code=?, tsd_status=?, tsd_survey_no=?, created_by=?, created_date=? "
							+ "WHERE tsd_survey_no=?; ";
					ps3 = con.prepareStatement(q2);
					ps3.setString(1, currTask);
					ps3.setString(2, taskStatus);
					if (surveyNo != null && !surveyNo.trim().isEmpty()) {
						ps3.setString(3, surveyNo);
					} else {
						ps3.setNull(3, Types.VARCHAR);
					}
					ps3.setString(4, loginUser);
					ps3.setTimestamp(5, timestamp);
					ps3.setString(6, surveyNo);

					ps3.executeUpdate();
					con.commit();

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(ps3);
			ConnectionManager.close(con);
		}

		logger.info("updateSurveyTaskDetailsBySurveyNo end");

	}

	@Override
	public boolean checkTaskDetailsInSubsidyByServiceRefNo(String serviceRefNo, String taskCode, String status) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isTaskAvailable = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM public.nt_t_subsidy_task_det "
					+ "where (tsd_task_code=? and tsd_status=? and tsd_reference_no=?); ";

			ps = con.prepareStatement(query);
			ps.setString(1, taskCode);
			ps.setString(2, status);
			ps.setString(3, serviceRefNo);
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
	public List<CommonDTO> routeRequest() {
		CommonDTO comDTO = new CommonDTO();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CommonDTO> data = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select seqno,trr_origin,trr_destination,trr_via "
					+ "from nt_temp_route_request where trr_status='R'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				comDTO = new CommonDTO();
				comDTO.setOrigin(rs.getString("trr_origin"));
				comDTO.setDestination(rs.getString("trr_destination"));
				comDTO.setVia(rs.getString("trr_via"));
				comDTO.setSeqNo(rs.getString("seqno"));
				data.add(comDTO);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return data;

	}

	@Override
	public void completed(String seqNo, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String query = "update nt_temp_route_request " + "set trr_status=?, modified_date=?, modified_by=? "
					+ "where seqno='" + seqNo + "'";

			ps = con.prepareStatement(query);
			ps.setString(1, "C");
			ps.setTimestamp(2, timestamp);
			ps.setString(3, user);

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
	public List<CommonDTO> timeApproval() {

		CommonDTO comDTO = new CommonDTO();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CommonDTO> data = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select amd_application_no,amd_permit_no,amd_new_busno " + "from nt_m_amendments "
					+ "where amd_time_approval='R'";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				comDTO = new CommonDTO();
				comDTO.setApplicationNo(rs.getString("amd_application_no"));
				comDTO.setPermitNo(rs.getString("amd_permit_no"));
				comDTO.setVehicleNo(rs.getString("amd_new_busno"));

				data.add(comDTO);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return data;

	}

	@Override
	public void timeapproveRequest(String user,String timeSlots, String appNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String query = "update nt_m_amendments "
					+ "set amd_time_approval=?,amd_timeapproval_service_change=?, amd_modified_date=?, amd_modified_by=? "
					+ "where amd_application_no=? ";

			ps = con.prepareStatement(query);
			ps.setString(1, "A");
			ps.setString(2, timeSlots);
			ps.setTimestamp(3, timestamp);
			ps.setString(4, user);
			ps.setString(5, appNo);

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
	public String selectPreviousTaskCodeSubsidyTaskTabel(String requestNo, String serviceNo, String referenceNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String WHERE_SQL = "";
		boolean whereadded = false;
		String prevTaskCode = "";

		if (requestNo != null && !requestNo.equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE tsd_request_no = " + "'" + requestNo + "' ";
			whereadded = true;
		}

		if (referenceNo != null && !referenceNo.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND tsd_reference_no = " + "'" + referenceNo + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE tsd_reference_no = " + "'" + referenceNo + "' ";
				whereadded = true;
			}
		}

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT * " + "FROM public.nt_t_subsidy_task_det " + WHERE_SQL;

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				prevTaskCode = rs.getString("tsd_task_code");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return prevTaskCode;
	}

	@Override
	public void updateQueMasterTableTask(String appno, String taskCode, String task) {

		Connection con = null;
		PreparedStatement ps4 = null;

		try {

			con = ConnectionManager.getConnection();

			String q4 = "update public.nt_m_queue_master set que_task_code=? ,que_task_status=? where que_application_no=?";
			ps4 = con.prepareStatement(q4);
			ps4.setString(1, taskCode);
			ps4.setString(2, task);
			ps4.setString(3, appno);
			ps4.executeUpdate();

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps4);
			ConnectionManager.close(con);
		}
	}

	@Override
	public String generateNewRefNo(String code) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String appno = null;
		String generatedCode = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM public.nt_r_number_generation WHERE code = ?;";

			ps = con.prepareStatement(query);
			ps.setString(1, code);
			rs = ps.executeQuery();

			while (rs.next()) {
				appno = rs.getString("app_no");
			}

			String prefix = appno.substring(0, appno.length() - 5);
			long num = Long.parseLong(appno.substring(appno.length() - 5));
			String next = String.valueOf((num + 1));
			generatedCode = prefix + StringUtils.leftPad(next, 5, "0");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return generatedCode;
	}

	@Override
	public String updateRefNoSeq(String code, String no, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		String generatedCode = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_r_number_generation SET app_no=?, modified_by=?, modified_date=? WHERE code=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, no);
			ps.setString(2, user);
			ps.setTimestamp(3, timestamp);
			ps.setString(4, code);
			ps.executeUpdate();

			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return generatedCode;
	}

	@Override
	public String[] getOwnerByVehicleNo(String vehicleNo) {
		Connection con = null;
		PreparedStatement ps_owner = null;
		ResultSet rs_owner = null;
		String data[] = new String[2];

		try {
			con = ConnectionManager.getConnection();

			String query_owner = "SELECT vehi.pmo_full_name, vehi.pmo_nic, vehi.pmo_address1, vehi.pmo_address2, vehi.pmo_address3, vehi.pmo_city, vehi.pmo_mobile_no, vehi.pmo_telephone_no "
					+ "FROM nt_t_pm_vehi_owner vehi, nt_t_pm_application app " + "WHERE vehi.pmo_vehicle_regno = ? "
					+ "AND vehi.pmo_application_no = app.pm_application_no " + "AND app.pm_status ='A'; ";

			ps_owner = con.prepareStatement(query_owner);
			ps_owner.setString(1, vehicleNo);
			rs_owner = ps_owner.executeQuery();

			while (rs_owner.next()) {
				data[0] = rs_owner.getString("pmo_full_name");
				data[1] = rs_owner.getString("pmo_nic");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps_owner);
			ConnectionManager.close(rs_owner);
			ConnectionManager.close(con);
		}
		return data;
	}

	@Override
	public List<String> getAllVehicle() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> drpdwnDTOList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT pm_vehicle_regno FROM nt_t_pm_application WHERE pm_status in ('A','O');";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				drpdwnDTOList.add(rs.getString(1));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return drpdwnDTOList;
	}

	@Override
	public List<String> getAllPermit() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> drpdwnDTOList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT pm_permit_no FROM nt_t_pm_application WHERE pm_status in ('A','O');";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				drpdwnDTOList.add(rs.getString(1));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return drpdwnDTOList;
	}

	/**
	 * added for bus removal and modify permit data task update
	 */

	@Override
	public void updateCommonTaskHistory(String busNo, String appNo, String task, String taskCode, String loginUser) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_task_det");

			String sql = "INSERT INTO public.nt_h_task_his\r\n"
					+ "(tsd_seq, tsd_vehicle_no, tsd_app_no, tsd_task_code, tsd_status, created_by, created_date)\r\n"
					+ "VALUES(?, ?, ?, ?, ?, ?, ?);\r\n" + ";";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, busNo);
			stmt.setString(3, appNo);
			stmt.setString(4, task);
			stmt.setString(5, taskCode);
			stmt.setString(6, loginUser);
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

	public List<CommonDTO> GetCourtGroups() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, group_name " + "FROM nt_r_court_group WHERE status = 'A' ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("code"));
				commonDTO.setDescription(rs.getString("group_name"));

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

	// get Page Formats list Implementation

	@Override
	public List<PageFormatDTO> getPageFormats() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PageFormatDTO> returnList = new ArrayList<PageFormatDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT format_description " + "FROM nt_r_page_format";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				PageFormatDTO pageFormatDTO = new PageFormatDTO();
				pageFormatDTO.setFormat(rs.getString("format_description"));

				returnList.add(pageFormatDTO);

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
	public Boolean IsAppAvailable(String appNo) {
		Connection con = null;
		PreparedStatement ps = null;
		Boolean available = false;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select pm_application_no from nt_t_pm_application inner join nt_t_task_det on pm_application_no = tsd_app_no where pm_application_no = ?  and (nt_t_task_det.tsd_task_code = 'PM400' and nt_t_task_det.tsd_status ='C');";

			ps = con.prepareStatement(query);
			ps.setString(1, appNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				available = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return available;
	}
	
	@Override
	public Boolean IsAppAvailableForDel(String appNo) {
		Connection con = null;
		PreparedStatement ps = null;
		Boolean available = false;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select pm_application_no from nt_t_pm_application inner join nt_t_task_det on pm_application_no = tsd_app_no where pm_application_no = ? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, appNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				available = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return available;
	}

	@Override
	public Boolean IsPaymentProcessOngoing(String appNo) {
		Connection con = null;
		PreparedStatement ps = null;
		Boolean available = false;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select tsd_app_no from nt_t_task_det "
					+ " where ((tsd_task_code = 'PM301' and tsd_status ='C') "
					+ " or (tsd_task_code = 'PM302' and tsd_status ='C')"
					+ " or (tsd_task_code = 'PM302' and tsd_status ='O')"
					+ " or (tsd_task_code = 'PM400' and tsd_status ='O')"
					+ " or (tsd_task_code = 'PM400' and tsd_status ='C'))" + " and tsd_app_no  = ?;";

			ps = con.prepareStatement(query);
			ps.setString(1, appNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				available = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return available;
	}

	@Override
	public boolean updateApplication(String strActAppNo, String strDetAppNo, String loginUser) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		PreparedStatement ps3 = null;
		PreparedStatement ps4 = null;
		PreparedStatement ps5 = null;
		PreparedStatement ps6 = null;
		PreparedStatement ps7 = null;
		PreparedStatement ps8 = null;
		PreparedStatement ps9 = null;
		PreparedStatement ps10 = null;
		PreparedStatement ps11 = null;
		PreparedStatement ps12 = null;
		PreparedStatement ps6_1 = null;
		PreparedStatement ps7_1 = null;
		PreparedStatement ps8_1 = null;
		PreparedStatement ps9_1 = null;
		PreparedStatement ps10_1 = null;
		PreparedStatement ps11_1 = null;
		PreparedStatement ps12_1 = null;
		Boolean available = false;
		ResultSet rs = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;

		String activeVehicle = null;
		String delVehicle = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			// get vehicle no for delete application
			String queryD = "SELECT pm_vehicle_regno FROM public.nt_t_pm_application "
					+ " WHERE pm_application_no =? ;";

			ps2 = con.prepareStatement(queryD);
			ps2.setString(1, strDetAppNo);
			rs2 = ps2.executeQuery();

			while (rs2.next()) {
				delVehicle = (rs2.getString("pm_vehicle_regno"));
			}

			// get vehicle no for active application
			String queryS = "SELECT pm_vehicle_regno FROM public.nt_t_pm_application "
					+ " WHERE pm_application_no =? ;";

			ps1 = con.prepareStatement(queryS);
			ps1.setString(1, strActAppNo);
			rs1 = ps1.executeQuery();

			while (rs1.next()) {
				activeVehicle = (rs1.getString("pm_vehicle_regno"));
			}

			/*String query = "select param2 from public.testnew(?,?);";

			ps = con.prepareStatement(query);
			ps.setString(1, strDetAppNo);
			ps.setString(2, strActAppNo);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				available = true;
			}
			*/
			
		
			//if (available) {
			
				// insert active application
				long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_task_det");

				String sql = "INSERT INTO public.nt_h_task_his\r\n"
						+ "(tsd_seq, tsd_vehicle_no, tsd_app_no, tsd_task_code, tsd_status, created_by, created_date)\r\n"
						+ "VALUES(?, ?, ?, ?, ?, ?, ?);\r\n" + ";";

				stmt = con.prepareStatement(sql);

				stmt.setLong(1, seqNo);
				stmt.setString(2, delVehicle);
				stmt.setString(3, strDetAppNo);
				stmt.setString(4, "D1001");
				stmt.setString(5, "C");
				stmt.setString(6, loginUser);
				stmt.setTimestamp(7, timestamp);

				stmt.executeUpdate();

				// insert delete application
				long seqNoD = Utils.getNextValBySeqName(con, "seq_nt_t_task_det");

				String sqlD = "INSERT INTO public.nt_d_task_his\r\n"
						+ "(tsd_seq, tsd_vehicle_no, tsd_app_no, tsd_task_code, tsd_status, created_by, created_date)\r\n"
						+ "VALUES(?, ?, ?, ?, ?, ?, ?);\r\n" + ";";

				stmt1 = con.prepareStatement(sqlD);

				stmt1.setLong(1, seqNoD);
				stmt1.setString(2, activeVehicle);
				stmt1.setString(3, strActAppNo);
				stmt1.setString(4, "D1002");
				stmt1.setString(5, "C");
				stmt1.setString(6, loginUser);
				stmt1.setTimestamp(7, timestamp);

				stmt1.executeUpdate();

			//}
				//Update Application table with Status='A'
				String queryActivate = "UPDATE public.nt_t_pm_application SET pm_status=?, pm_modified_by=?, pm_modified_date=? WHERE pm_application_no=?;";

				ps3 = con.prepareStatement(queryActivate);
				ps3.setString(1,"A");
				ps3.setString(2,loginUser);
				ps3.setTimestamp(3, timestamp);
				ps3.setString(4, strActAppNo);
				ps3.executeUpdate();
				
				//Removed DeleteApplication from Application table and insert into nt_d_pm_application table.
				
				String queryInsertHistory = "INSERT into public.nt_d_pm_application  "
						+ " (SELECT * FROM public.nt_t_pm_application WHERE pm_application_no = ?) ";

				ps4 = con.prepareStatement(queryInsertHistory);
				ps4.setString(1, strDetAppNo);
				ps4.executeUpdate();
				
				//--delete from main tables
				String queryInsertOminiHistory = "INSERT into public.nt_d_pm_omini_bus1  "
						+ " (SELECT * FROM public.nt_t_pm_omini_bus1 WHERE pmb_application_no = ?) ";
				ps6_1 = con.prepareStatement(queryInsertOminiHistory);
				ps6_1.setString(1, strDetAppNo);
				ps6_1.executeUpdate();
				
				String queryDeleteOmini = "delete from public.nt_t_pm_omini_bus1 where pmb_application_no =?;";
				ps6 = con.prepareStatement(queryDeleteOmini);
				ps6.setString(1, strDetAppNo);
				ps6.executeUpdate();
				
				
				String queryInsertOwnerHistory = "INSERT into public.nt_d_pm_vehi_owner  "
						+ " (SELECT * FROM public.nt_t_pm_vehi_owner WHERE pmo_application_no = ?) ";
				ps7_1 = con.prepareStatement(queryInsertOwnerHistory);
				ps7_1.setString(1, strDetAppNo);
				ps7_1.executeUpdate();
				
				String queryDeleteOwner = "delete from public.nt_t_pm_vehi_owner where pmo_application_no =?;";
				ps7 = con.prepareStatement(queryDeleteOwner);
				ps7.setString(1, strDetAppNo);
				ps7.executeUpdate();
				
				
				String queryInsertInsDecHistory = "INSERT into public.nt_d_vehicle_inspec_det  "
						+ " (SELECT * FROM public.nt_t_vehicle_inspec_det WHERE vid_app_no = ?) ";
				ps8_1 = con.prepareStatement(queryInsertInsDecHistory);
				ps8_1.setString(1, strDetAppNo);
				ps8_1.executeUpdate();
				
				String queryInspecDet = "delete from public.nt_t_vehicle_inspec_det where vid_app_no =?;";
				ps8 = con.prepareStatement(queryInspecDet);
				ps8.setString(1, strDetAppNo);
				ps8.executeUpdate();
				
				String queryInsertInspecImagesHistory = "INSERT into public.nt_d_owner_vehi_images  "
						+ " (SELECT * FROM public.nt_t_owner_vehi_images WHERE ovi_app_no = ?) ";
				ps9_1 = con.prepareStatement(queryInsertInspecImagesHistory);
				ps9_1.setString(1, strDetAppNo);
				ps9_1.executeUpdate();
				
				String queryInspecImages = "delete from public.nt_t_owner_vehi_images where ovi_app_no =?;";
				ps9 = con.prepareStatement(queryInspecImages);
				ps9.setString(1, strDetAppNo);
				ps9.executeUpdate();
				
				String queryQueueHistory = "INSERT into public.nt_d_queue_master  "
						+ " (SELECT * FROM public.nt_m_queue_master WHERE que_application_no = ?) ";
				ps10_1 = con.prepareStatement(queryQueueHistory);
				ps10_1.setString(1, strDetAppNo);
				ps10_1.executeUpdate();
				
				String queryQueue = "delete from public.nt_m_queue_master where que_application_no =?;";
				ps10 = con.prepareStatement(queryQueue);
				ps10.setString(1, strDetAppNo);
				ps10.executeUpdate();
				
				String queryTaskDetHistory = "INSERT into public.nt_d_task_det  "
						+ " (SELECT * FROM public.nt_t_task_det WHERE tsd_app_no = ?) ";
				ps11_1 = con.prepareStatement(queryTaskDetHistory);
				ps11_1.setString(1, strDetAppNo);
				ps11_1.executeUpdate();
				
				String queryTaskDet = "delete from public.nt_t_task_det where tsd_app_no =?;";
				ps11 = con.prepareStatement(queryTaskDet);
				ps11.setString(1, strDetAppNo);
				ps11.executeUpdate();
				
				String queryTaskHistory = "INSERT into public.nt_d_task_his  "
						+ " (SELECT * FROM public.nt_h_task_his WHERE tsd_app_no = ?) ";
				ps12_1 = con.prepareStatement(queryTaskHistory);
				ps12_1.setString(1, strDetAppNo);
				ps12_1.executeUpdate();
				
				String queryTaskHis = "delete from public.nt_h_task_his where tsd_app_no =?;";
				ps12 = con.prepareStatement(queryTaskHis);
				ps12.setString(1, strDetAppNo);
				ps12.executeUpdate();
				
								
				String queryDeleteApplication = "delete from public.nt_t_pm_application where pm_application_no =?;";

				ps5 = con.prepareStatement(queryDeleteApplication);
				ps5.setString(1, strDetAppNo);
				int updateCount=ps5.executeUpdate();
				
				if(updateCount>0){
					available=true;					
				}

			con.commit();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(ps1);
			ConnectionManager.close(ps2);
			ConnectionManager.close(ps3);
			ConnectionManager.close(ps4);
			ConnectionManager.close(ps5);
			ConnectionManager.close(ps6);
			ConnectionManager.close(ps7);
			ConnectionManager.close(ps8);
			ConnectionManager.close(ps9);
			ConnectionManager.close(ps10);
			ConnectionManager.close(ps11);
			ConnectionManager.close(ps12);
			ConnectionManager.close(ps6_1);
			ConnectionManager.close(ps7_1);
			ConnectionManager.close(ps8_1);
			ConnectionManager.close(ps9_1);
			ConnectionManager.close(ps10_1);
			ConnectionManager.close(ps11_1);
			ConnectionManager.close(ps12_1);
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt1);
			ConnectionManager.close(rs);
			ConnectionManager.close(rs1);
			ConnectionManager.close(rs2);
			ConnectionManager.close(con);
		}
		return available;
	}

	public Boolean IsSamePermitNo(String strActAppNo, String strDetAppNo) {
		Connection con = null;
		PreparedStatement ps = null;
		Boolean available = false;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select count( distinct pm_permit_no) as permit_count from nt_t_pm_application where pm_application_no in (?,?);";

			ps = con.prepareStatement(query);
			ps.setString(1, strActAppNo);
			ps.setString(2, strDetAppNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				int result_count = rs.getInt("permit_count");
				if (result_count > 1) {
					available = false;
				} else
					available = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return available;
	}

	@Override
	public CommonDTO vehicleNoValidation(String vehicleNo) {
		Connection con = null;
		PreparedStatement ps = null, ps1 = null, ps2 = null, ps3 = null;
		ResultSet rs = null, rs1 = null, rs2 = null, rs3 = null;
		CommonDTO dto = new CommonDTO();

		try {
			con = ConnectionManager.getConnection();
			// Check active complain
			String query = "SELECT complain_no FROM nt_m_complain_request WHERE vehicle_no = ? AND process_status != 'C' AND grant_permission_status is null LIMIT 1";

			ps = con.prepareStatement(query);
			ps.setString(1, vehicleNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				dto.setComplainNo(rs.getString("complain_no"));
			}

			// Check complain which have grant permission
			if(dto.getComplainNo() == null ||dto.getComplainNo().trim().isEmpty()) {
			String queryG = "SELECT complain_no, grant_permission_remark FROM nt_m_complain_request WHERE vehicle_no = ? AND process_status != 'C' AND grant_permission_status = 'TA' order by grant_permission_given_date desc LIMIT 1";

			ps3 = con.prepareStatement(queryG);
			ps3.setString(1, vehicleNo);
			rs3 = ps3.executeQuery();

			while (rs3.next()) {
				dto.setGrantComplainNo(rs3.getString("complain_no"));
				dto.setGrantPermissionRemark(rs3.getString("grant_permission_remark"));
			}
			}
			
			// Check expire SIM
			
			/*String query1 = "SELECT sim_reg_no,sim_status FROM nt_m_sim_registration WHERE sim_bus_no = ? AND sim_valid_until <= to_date(to_char(now(),'dd/MM/yyyy'),'dd/MM/yyyy') "
					+ " AND sim_status != 'R' ORDER BY sim_created_date LIMIT 1 ;";*/
			/*** query changed due to pramitha's requirement 10/14/2021 ***/
			
			String query1 ="SELECT sim_reg_no FROM nt_m_sim_registration WHERE sim_bus_no = ?\r\n" + 
					"AND (sim_valid_until <= to_date(to_char(now(),'dd/MM/yyyy'),'dd/MM/yyyy') or sim_status in ('P','I','R'))\r\n" + 
					"ORDER BY sim_created_date LIMIT 1;";
			ps1 = con.prepareStatement(query1);
			ps1.setString(1, vehicleNo);
			rs1 = ps1.executeQuery();

			while (rs1.next()) {
				
				dto.setSimRegNo(rs1.getString("sim_reg_no"));
				
			}

			// Check active Investigation
			String query2 = "SELECT charge_ref_no FROM public.nt_t_investigation_charge_master WHERE charge_vehicle = ? AND charge_app_status != 'C' "
					+ " ORDER BY created_date desc LIMIT 1";

			ps2 = con.prepareStatement(query2);
			ps2.setString(1, vehicleNo);
			rs2 = ps2.executeQuery();

			while (rs2.next()) {
				dto.setChargeRefNo(rs2.getString("charge_ref_no"));
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(rs1);
			ConnectionManager.close(ps1);
			ConnectionManager.close(rs2);
			ConnectionManager.close(ps2);
			ConnectionManager.close(rs3);
			ConnectionManager.close(ps3);
			ConnectionManager.close(con);
		}
		return dto;
	}

	@Override
	public void otherInspectionTasksUpdate(String applicationNo, String currentTask, String currentStatus,
			String createBy, String counterID, String vehicleNo) {

		Connection con = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		PreparedStatement ps4 = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			String queryOne = "SELECT tsd_seq, tsd_vehicle_no, tsd_app_no, tsd_task_code, tsd_status, "
					+ "created_by, created_date, tsd_counterid FROM public.nt_t_task_det where tsd_app_no=? ;";

			ps1 = con.prepareStatement(queryOne);
			ps1.setString(1, applicationNo);
			rs = ps1.executeQuery();

			if (rs.next() == true) {

				String historyQuery = "INSERT INTO public.nt_h_task_his "
						+ "(tsd_seq, tsd_vehicle_no, tsd_app_no, tsd_task_code, tsd_status, created_by, created_date, tsd_counterid)"
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?);";

				ps2 = con.prepareStatement(historyQuery);
				ps2.setLong(1, rs.getLong("tsd_seq"));
				ps2.setString(2, rs.getString("tsd_vehicle_no"));
				ps2.setString(3, rs.getString("tsd_app_no"));
				ps2.setString(4, rs.getString("tsd_task_code"));
				ps2.setString(5, rs.getString("tsd_status"));
				ps2.setString(6, rs.getString("created_by"));
				ps2.setTimestamp(7, rs.getTimestamp("created_date"));
				ps2.setString(8, rs.getString("tsd_counterid"));
				ps2.executeUpdate();

				String updateQuery = "UPDATE public.nt_t_task_det "
						+ "SET  tsd_task_code=?, tsd_status=?, created_by=?, created_date=? "
						+ "WHERE tsd_app_no=? and tsd_task_code=? and tsd_status=?";

				ps3 = con.prepareStatement(updateQuery);
				ps3.setString(1, currentTask);
				ps3.setString(2, currentStatus);
				ps3.setString(3, createBy);
				ps3.setTimestamp(4, timestamp);
				ps3.setString(5, rs.getString("tsd_app_no"));
				ps3.setString(6, rs.getString("tsd_task_code"));
				ps3.setString(7, rs.getString("tsd_status"));
				ps3.executeUpdate();

			} else {
				long detSeq = Utils.getNextValBySeqName(con, "seq_nt_t_task_det");

				String insertQuery = "INSERT INTO public.nt_t_task_det "
						+ "(tsd_seq, tsd_vehicle_no, tsd_app_no, tsd_task_code, tsd_status, created_by, created_date, tsd_counterid) "
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?);";

				ps4 = con.prepareStatement(insertQuery);

				ps4.setLong(1, detSeq);
				ps4.setString(2, vehicleNo);
				ps4.setString(3, applicationNo);
				ps4.setString(4, currentTask);
				ps4.setString(5, currentStatus);
				ps4.setString(6, createBy);
				ps4.setTimestamp(7, timestamp);
				ps4.setString(8, counterID);
				ps4.executeUpdate();
			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps1);
			ConnectionManager.close(ps2);
			ConnectionManager.close(ps3);
			ConnectionManager.close(ps4);
			ConnectionManager.close(con);
		}

	}

	@Override
	public Boolean IsHavingIncompleteInspection(String vehicleNo) {
		Connection con = null;
		PreparedStatement ps = null;
		Boolean available = false;
		ResultSet rs = null;
		CommonDTO dto = new CommonDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT pm_inspection_status, proceed_status " + " FROM  nt_t_pm_application "
					+ " WHERE pm_vehicle_regno= ? " + " ORDER BY pm_created_date  DESC LIMIT 1";

			ps = con.prepareStatement(query);
			ps.setString(1, vehicleNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				dto.setInspectionStatus(rs.getString("pm_inspection_status"));
				dto.setProceedStatus(rs.getString("proceed_status"));

				if (dto.getInspectionStatus().equals("I") && dto.getProceedStatus().equals("N")) {
					available = true;
				} else
					available = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return available;
	}

	@Override
	public void updateOtherInspectionSkipQueueNumber(String queueNumber) {

		logger.info("updateQueueNumberAfterCallNext start");

		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		java.util.Date date = new java.util.Date();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String today = df.format(date);

		int skipCount = 0;
		String skipCnt = null;
		try {

			con = ConnectionManager.getConnection();

			String sql = "SELECT que_skip_count FROM public.nt_m_queue_master WHERE que_number=? and que_date like '"
					+ today + "%'";
			ps = con.prepareStatement(sql);
			ps.setString(1, queueNumber);

			rs = ps.executeQuery();

			while (rs.next()) {
				skipCnt = rs.getString("que_skip_count");
			}

			if (skipCnt != null && !skipCnt.isEmpty() && !skipCnt.trim().equalsIgnoreCase("")) {
				skipCount = Integer.parseInt(skipCnt);
			}

			ConnectionManager.close(ps);

			if (skipCount == 0) {
				skipCount = 1;
			} else {
				skipCount = skipCount + 1;
			}

			String sql3 = "UPDATE public.nt_m_queue_master SET que_skip_count=? WHERE que_number=? and que_date like '"
					+ today + "%'";

			ps = con.prepareStatement(sql3);

			ps.setString(1, Integer.toString(skipCount));
			ps.setString(2, queueNumber);

			ps.executeUpdate();

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("updateQueueNumberAfterCallNext error: " + ex.toString());
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		logger.info("updateQueueNumberAfterCallNext end");

	}

	@Override
	public String getSisuRequstNoForServiceNoInTaskTable(String serviceNo) {
		String reqNo = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select tsd_request_no from public.nt_t_subsidy_task_det where tsd_service_no ='" + serviceNo
					+ "'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				reqNo = rs.getString("tsd_request_no");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return reqNo;

	}

	@Override
	public String getSisuRefNoForServiceNoInTaskTable(String serviceNo) {
		String refNo = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select tsd_reference_no  from public.nt_t_subsidy_task_det where tsd_service_no ='"
					+ serviceNo + "'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				refNo = rs.getString("tsd_reference_no");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return refNo;

	}

	@Override
	public boolean existingLogSheetReferenceNo(String referenceNo, String serviceCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isFound = false;
		try {
			con = ConnectionManager.getConnection();

//			String query = "select A.lss_log_ref_no from public.nt_m_log_sheet_summary A " +
//						   "inner join public.nt_m_log_sheets B on A.lss_log_sheet_master_seq = B.los_seq " +
//						   "where B.los_std_service_ref_no = ? and A.lss_log_ref_no = ? order by A.lss_log_ref_no desc";
			
			/* Query where condition changed : dhananjika.d (26/06/2024) */
			String query = "select A.lss_log_ref_no from public.nt_m_log_sheet_summary A " +
					   "inner join public.nt_m_log_sheets B on A.lss_log_sheet_master_seq = B.los_seq " +
					   "where B.los_subsidy_service_type_code = ? and A.lss_log_ref_no = ? order by A.lss_log_ref_no desc";
			ps = con.prepareStatement(query);
			ps.setString(1, serviceCode);
			ps.setString(2, referenceNo);
			rs = ps.executeQuery();

			if (rs.next()) {
				isFound = true;
			} else {
				isFound = false;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isFound;
	}
	
	@Override
	public boolean existingLogSheetReferenceNo(String referenceNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isFound = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "select lss_log_ref_no from public.nt_m_log_sheet_summary where lss_log_ref_no=? ";

			ps = con.prepareStatement(query);
			ps.setString(1, referenceNo);
			rs = ps.executeQuery();

			if (rs.next()) {
				isFound = true;
			} else {
				isFound = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isFound;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		CommonServiceImpl.logger = logger;
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
	public List<String> getAllTemporaryVehicle() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> drpdwnDTOList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct tep_vehicleno from nt_t_terminal_payment\r\n" + 
					"where tep_payment_type_code = '002'\r\n" + 
					"and tep_valid_to is null or date (tep_valid_to) >= current_date\r\n" + 
					"";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				drpdwnDTOList.add(rs.getString(1));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return drpdwnDTOList;
	
	}

	@Override
	public List<String> getAllTemporaryPermit() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> drpdwnDTOList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct tep_permit_no from nt_t_terminal_payment\r\n" + 
					"where tep_payment_type_code = '002'\r\n" + 
					"and tep_valid_to is null or date (tep_valid_to) >= current_date";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				drpdwnDTOList.add(rs.getString(1));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return drpdwnDTOList;
	}

	@Override
	public boolean isTemporaryActiveBusNumber(String vehiNo) {
		Connection con = null;
		PreparedStatement ps = null;
		Boolean available = false;
		ResultSet rs = null;
		

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM nt_m_complain_request\r\n" + 
					"WHERE grant_permission_status = 'TA'\r\n" + 
					"AND vehicle_no = ? \r\n" + 
					"";

			ps = con.prepareStatement(query);
			ps.setString(1, vehiNo);
			rs = ps.executeQuery();

			while (rs.next()) {
			
					available = true;
			
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return available;
	}

	@Override
	public boolean insertDataIntoComplainRequestHistoryAndUpdate(String busNo, String user) {
		Connection con = null;
		PreparedStatement stmt = null, stmt1 = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			con = ConnectionManager.getConnection();

			String sql1 = "INSERT into public.nt_h_complain_request  "
					+ " (SELECT * FROM public.nt_m_complain_request WHERE grant_permission_status = 'TA'\r\n" + 
					"AND vehicle_no = ?\r\n" + 
					") ";

			stmt1 = con.prepareStatement(sql1);
			stmt1.setString(1, busNo);
			stmt1.executeUpdate();

		

			String sql = "UPDATE nt_m_complain_request\r\n" + 
					"SET grant_permission_status = null,\r\n" + 
					"grant_permission_given_by = null,\r\n" + 
					"grant_permission_given_date = null,\r\n" + 
					"grant_permission_remark = null,modified_by = ?, modified_date = ? where grant_permission_status = 'TA' AND vehicle_no = ? \r\n" + 
					";";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, user);
			stmt.setTimestamp(2, timestamp);
			stmt.setString(3, busNo);
			
			stmt.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			ConnectionManager.close(stmt1);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return true;
	}

	@Override
	public String getSisurServiceNoInTaskTable(String refNo) {

		String serviceNo = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select tsd_service_no  from public.nt_t_subsidy_task_det where tsd_reference_no ='"
					+ refNo + "'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				serviceNo = rs.getString("tsd_service_no");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return serviceNo;

	
	}

	@Override
	public String getQueueNumberFromQueMasterByAppNo(String appNo) {
		Connection con = null;
		
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		String queueNo = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String sql1 = "select que_number,que_date from public.nt_m_queue_master where que_application_no =? order by que_date desc limit 1";

			ps = con.prepareStatement(sql1);
			ps.setString(1, appNo);
			rs = ps.executeQuery();
			
			
			while (rs.next()) {
				queueNo = rs.getString("que_number");

			}

			

		} catch (Exception ex) {
			ex.printStackTrace();
			
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return queueNo;
	}



}
