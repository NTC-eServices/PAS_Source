package lk.informatics.ntc.model.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedProperty;

import lk.informatics.ntc.model.dto.AdvancedPaymentDTO;
import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.PaymentVoucherDTO;
import lk.informatics.ntc.view.beans.SessionBackingBean;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.ntc.view.util.SpringApplicationContex;
import lk.informatics.roc.utils.common.Utils;

public class PaymentVousherServiceImpl implements PaymentVoucherService {
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private CommonService commonService;
	private MigratedService migratedService;

	@Override
	public boolean updateTaskDetails(PaymentVoucherDTO dto, String taskCode, String status) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_t_task_det " + "SET tsd_status=? "
					+ "WHERE tsd_app_no=? AND tsd_task_code=? AND tsd_status=?; ";

			ps = con.prepareStatement(query);
			ps.setString(1, status);
			ps.setString(2, dto.getApplicationNo());
			ps.setString(3, taskCode);
			ps.setString(4, "O");
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
	public boolean changeTaskDetails(PaymentVoucherDTO dto, String oldTaskCode, String newtaskCode, String status,
			String preStatus) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_t_task_det SET tsd_status=? , tsd_task_code = ? "
					+ "WHERE tsd_app_no=? AND tsd_task_code=? AND tsd_status=?; ";

			ps = con.prepareStatement(query);
			ps.setString(1, status);
			ps.setString(2, newtaskCode);
			ps.setString(3, dto.getApplicationNo());
			ps.setString(4, oldTaskCode);
			ps.setString(5, preStatus);
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
	public boolean deleteTaskDetails(PaymentVoucherDTO dto, String taskCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isTaskPM300Delete = false;

		try {
			con = ConnectionManager.getConnection();

			String query = "DELETE FROM public.nt_t_task_det WHERE tsd_app_no=? AND tsd_task_code=? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, dto.getApplicationNo());
			ps.setString(2, taskCode);
			int i = ps.executeUpdate();

			if (i > 0) {
				isTaskPM300Delete = true;

			} else {
				isTaskPM300Delete = false;

			}

			con.commit();

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isTaskPM300Delete;
	}

	@Override
	public boolean CopyTaskDetailsANDinsertTaskHistory(PaymentVoucherDTO dto, String loginUSer, String taskCode) {
		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		PaymentVoucherDTO paymentVoucherDTO = null;

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

				paymentVoucherDTO = new PaymentVoucherDTO();
				paymentVoucherDTO.setTaskSeq(rs.getLong("tsd_seq"));
				paymentVoucherDTO.setBusRegNo(rs.getString("tsd_vehicle_no"));
				paymentVoucherDTO.setApplicationNo(rs.getString("tsd_app_no"));
				paymentVoucherDTO.setTaskCode(rs.getString("tsd_task_code"));
				paymentVoucherDTO.setApproveStatus(rs.getString("tsd_status"));
				paymentVoucherDTO.setCreateBy(rs.getString("created_by"));
				paymentVoucherDTO.setCreateDate(rs.getTimestamp("created_date"));

				String sql2 = "INSERT INTO public.nt_h_task_his "
						+ "(tsd_seq, tsd_vehicle_no, tsd_app_no, tsd_task_code, tsd_status, created_by, created_date) "
						+ "VALUES(? , ?, ?, ?, ?, ?, ?); " + "";

				stmt2 = con.prepareStatement(sql2);

				stmt2.setLong(1, paymentVoucherDTO.getTaskSeq());
				stmt2.setString(2, paymentVoucherDTO.getBusRegNo());
				stmt2.setString(3, paymentVoucherDTO.getApplicationNo());
				stmt2.setString(4, paymentVoucherDTO.getTaskCode());
				stmt2.setString(5, paymentVoucherDTO.getApproveStatus());
				stmt2.setString(6, paymentVoucherDTO.getCreateBy());
				stmt2.setTimestamp(7, paymentVoucherDTO.getCreateDate());

				stmt2.executeUpdate();

			} else {
				isUpdate = false;

			}

			commonService = (CommonService) SpringApplicationContex.getBean("commonService");
			migratedService = (MigratedService) SpringApplicationContex.getBean("migratedService");
			String queueNumber = migratedService.findQueueNumberFromApplicationNo(paymentVoucherDTO.getApplicationNo());
			migratedService.updateQueueNumberTaskInQueueMaster(queueNumber, paymentVoucherDTO.getApplicationNo(),
					paymentVoucherDTO.getTaskCode(), paymentVoucherDTO.getApproveStatus());

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
	public boolean insertTaskDetails(PaymentVoucherDTO dto, String loginUSer, String taskCode, String status) {
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
		return false;
	}

	@Override
	public boolean checkTaskDetails(PaymentVoucherDTO dto, String taskCode, String status) {
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
	public boolean checkDepartment(PaymentVoucherDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String WHERE_SQL = "";
		boolean whereadded = false;

		if (dto.getReceiptNo() != null && !dto.getReceiptNo().equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE pav_receipt_no = " + "'" + dto.getReceiptNo()
					+ "' and pav_deparment is not null";
			whereadded = true;
		}

		if (dto.getPermitNo() != null && !dto.getPermitNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " OR pav_permit_no = " + "'" + dto.getPermitNo()
						+ "' and pav_deparment is not null";
			} else {
				WHERE_SQL = WHERE_SQL + " WHERE pav_permit_no = " + "'" + dto.getPermitNo()
						+ "' and pav_deparment is not null";
				whereadded = true;
			}
		}
		if (dto.getVoucherNo() != null && !dto.getVoucherNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " OR pav_voucher_no = " + "'" + dto.getVoucherNo()
						+ "' and pav_deparment is not null";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE pav_voucher_no = " + "'" + dto.getVoucherNo()
						+ "' and pav_deparment is not null";
				whereadded = true;
			}
		}
		if (dto.getApplicationNo() != null && !dto.getApplicationNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " OR pav_application_no = " + "'" + dto.getApplicationNo()
						+ "' and pav_deparment is not null";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE pav_application_no = " + "'" + dto.getApplicationNo()
						+ "' and pav_deparment is not null";
				whereadded = true;
			}
		}
		if (dto.getUnitCode() != null && !dto.getUnitCode().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " OR pav_unit = " + "'" + dto.getUnitCode() + "' and pav_deparment is not null";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE pav_unit = " + "'" + dto.getUnitCode()
						+ "' and pav_deparment is not null";
				whereadded = true;
			}
		}

		boolean isAvailabelDepartment = false;

		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT pav_voucher_no, nt_r_transaction_type.description as transDes, nt_r_department.description as departDes, pav_application_no, "
					+ "pav_receipt_no, pav_approved_status,pav_created_date, pav_total_amount "
					+ "FROM public.nt_m_payment_voucher "
					+ "inner join nt_r_transaction_type on nt_r_transaction_type.code= nt_m_payment_voucher.pav_tran_type "
					+ "inner join nt_r_department  on nt_r_department.code= nt_m_payment_voucher.pav_deparment "
					+ WHERE_SQL;

			ps = con.prepareStatement(query2);

			rs = ps.executeQuery();

			if (rs.next()) {
				isAvailabelDepartment = true;
			} else {
				isAvailabelDepartment = false;
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return isAvailabelDepartment;
	}

	@Override
	public boolean checkTransaction(PaymentVoucherDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String WHERE_SQL = "";
		boolean whereadded = false;

		if (dto.getReceiptNo() != null && !dto.getReceiptNo().equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE pav_receipt_no = " + "'" + dto.getReceiptNo()
					+ "' and pav_tran_type is not null";
			whereadded = true;
		}

		if (dto.getPermitNo() != null && !dto.getPermitNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " OR pav_permit_no = " + "'" + dto.getPermitNo()
						+ "' and pav_tran_type is not null";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE pav_permit_no = " + "'" + dto.getPermitNo()
						+ "' and pav_tran_type is not null";
				whereadded = true;
			}
		}
		if (dto.getVoucherNo() != null && !dto.getVoucherNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " OR pav_voucher_no = " + "'" + dto.getVoucherNo()
						+ "' and pav_tran_type is not null";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE pav_voucher_no = " + "'" + dto.getVoucherNo()
						+ "' and pav_tran_type is not null";
				whereadded = true;
			}
		}
		if (dto.getApplicationNo() != null && !dto.getApplicationNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " OR pav_application_no = " + "'" + dto.getApplicationNo()
						+ "' and pav_tran_type is not null";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE pav_application_no = " + "'" + dto.getApplicationNo()
						+ "' and pav_tran_type is not null";
				whereadded = true;
			}
		}
		if (dto.getUnitCode() != null && !dto.getUnitCode().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " OR pav_unit = " + "'" + dto.getUnitCode() + "' and pav_tran_type is not null";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE pav_unit = " + "'" + dto.getUnitCode()
						+ "' and pav_tran_type is not null";
				whereadded = true;
			}
		}

		boolean isAvailabelTransAction = false;

		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT pav_voucher_no, nt_r_transaction_type.description as transDes, nt_r_department.description as departDes, pav_application_no, "
					+ "pav_receipt_no, pav_approved_status,pav_created_date, pav_total_amount "
					+ "FROM public.nt_m_payment_voucher "
					+ "inner join nt_r_transaction_type on nt_r_transaction_type.code= nt_m_payment_voucher.pav_tran_type "
					+ "inner join nt_r_department  on nt_r_department.code= nt_m_payment_voucher.pav_deparment "
					+ WHERE_SQL;

			ps = con.prepareStatement(query2);

			rs = ps.executeQuery();

			if (rs.next()) {
				isAvailabelTransAction = true;
			} else {
				isAvailabelTransAction = false;
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return isAvailabelTransAction;

	}

	@Override
	public List<PaymentVoucherDTO> getAdvancePaymentDetails(PaymentVoucherDTO dto) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PaymentVoucherDTO> paymentVoucherLIST = new ArrayList<PaymentVoucherDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT   apd_details_of_service, apd_no_of_unit, apd_amount, apd_created_date "
					+ "FROM public.nt_t_advance_payment_det WHERE apd_voucher_no=? ";

			PaymentVoucherDTO e;
			ps = con.prepareStatement(query2);
			ps.setString(1, dto.getVoucherNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				e = new PaymentVoucherDTO();

				e.setDetailsofservice(rs.getString("apd_details_of_service"));
				e.setNoOfUnits(rs.getInt("apd_no_of_unit"));
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				String date = dateFormat.format(rs.getTimestamp("apd_created_date"));

				e.setPaymentDate(date);
				e.setAmount(rs.getBigDecimal("apd_amount"));

				paymentVoucherLIST.add(e);
			}

			return paymentVoucherLIST;

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
	public boolean saveAdvanceRemark(PaymentVoucherDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isSvaeAdvancePayment = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_m_payment_voucher " + "SET  pav_remarks=? " + "WHERE pav_voucher_no=? ;";

			ps = con.prepareStatement(query);

			ps.setString(1, dto.getSpeacialRemark());
			ps.setString(2, dto.getVoucherNo());
			int a = ps.executeUpdate();

			if (a > 0) {
				isSvaeAdvancePayment = true;
			} else {
				isSvaeAdvancePayment = false;
			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isSvaeAdvancePayment;
	}

	@Override
	public List<PaymentVoucherDTO> getApprovedPaymentDetails(PaymentVoucherDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PaymentVoucherDTO> paymentVoucherLIST = new ArrayList<PaymentVoucherDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT pav_permit_no, pav_voucher_no, nt_r_transaction_type.description as transDes, nt_r_department.description as departDes, pav_application_no, "
					+ "pav_receipt_no, pav_approved_status,pav_created_date,nt_t_pm_application.pm_vehicle_regno, pav_total_amount "
					+ "FROM public.nt_m_payment_voucher "
					+ "inner join nt_r_transaction_type on nt_r_transaction_type.code= nt_m_payment_voucher.pav_tran_type "
					+ "inner join nt_r_department  on nt_r_department.code= nt_m_payment_voucher.pav_deparment "
					+ "left outer join nt_t_pm_application on nt_t_pm_application.pm_application_no= nt_m_payment_voucher.pav_application_no "
					+ "where pav_approved_status='P' ";

			PaymentVoucherDTO e;
			ps = con.prepareStatement(query2);

			rs = ps.executeQuery();

			while (rs.next()) {
				e = new PaymentVoucherDTO();
				e.setVoucherNo(rs.getString("pav_voucher_no"));
				e.setPermitNo(rs.getString("pav_permit_no"));
				e.setTransactionDescription(rs.getString("transDes"));
				e.setDeaprtmentDiscription(rs.getString("departDes"));
				e.setApplicationNo(rs.getString("pav_application_no"));
				e.setReceiptNo(rs.getString("pav_receipt_no"));
				e.setBusRegNo(rs.getString("pm_vehicle_regno"));

				String approveStatusCode = rs.getString("pav_approved_status");
				String approveStatus = "";

				if (approveStatusCode != null) {
					if (approveStatusCode.equals("A")) {
						approveStatus = "APPROVED";
					} else if (approveStatusCode.equals("R")) {
						approveStatus = "REJECTED";
					} else if (approveStatusCode.equals("P")) {
						approveStatus = "PENDING";
					}
				}

				e.setApproveStatus(approveStatus);

				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				String date = dateFormat.format(rs.getTimestamp("pav_created_date"));

				e.setPaymentDate(date);
				e.setTotalAmount(rs.getBigDecimal("pav_total_amount"));

				paymentVoucherLIST.add(e);
			}

			return paymentVoucherLIST;

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
	public List<PaymentVoucherDTO> getPaymentDetails(PaymentVoucherDTO dto) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String WHERE_SQL = "";
		boolean whereadded = false;
		if (dto.getTransactionCode() != null && !dto.getTransactionCode().equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE pav_tran_type = " + "'" + dto.getTransactionCode() + "' ";

			whereadded = true;
		}

		if (dto.getReceiptNo() != null && !dto.getReceiptNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " and pav_receipt_no = " + "'" + dto.getReceiptNo() + "' ";

			}

		}
		if (dto.getPermitNo() != null && !dto.getPermitNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " and pav_permit_no = " + "'" + dto.getPermitNo() + "' ";

			}

		}
		if (dto.getVoucherNo() != null && !dto.getVoucherNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " and pav_voucher_no = " + "'" + dto.getVoucherNo() + "' ";

			}

		}
		if (dto.getApplicationNo() != null && !dto.getApplicationNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " and pav_application_no = " + "'" + dto.getApplicationNo() + "' ";

			}

		}
		if (dto.getUnitCode() != null && !dto.getUnitCode().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " and pav_unit = " + "'" + dto.getUnitCode() + "' ";

			}

		}

		List<PaymentVoucherDTO> paymentVoucherLIST = new ArrayList<PaymentVoucherDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT pav_permit_no, pav_voucher_no, nt_r_transaction_type.description as transDes, nt_r_department.description as departDes, pav_application_no, "
					+ "pav_receipt_no, pav_approved_status,pav_created_date,nt_t_pm_application.pm_vehicle_regno, pav_total_amount "
					+ "FROM public.nt_m_payment_voucher "
					+ "inner join nt_r_transaction_type on nt_r_transaction_type.code= nt_m_payment_voucher.pav_tran_type "
					+ "inner join nt_r_department  on nt_r_department.code= nt_m_payment_voucher.pav_deparment "
					+ "left outer join nt_t_pm_application on nt_t_pm_application.pm_application_no= nt_m_payment_voucher.pav_application_no"
					+ WHERE_SQL;

			PaymentVoucherDTO e;
			ps = con.prepareStatement(query2);

			rs = ps.executeQuery();

			while (rs.next()) {
				e = new PaymentVoucherDTO();
				e.setVoucherNo(rs.getString("pav_voucher_no"));
				e.setPermitNo(rs.getString("pav_permit_no"));
				e.setTransactionDescription(rs.getString("transDes"));
				e.setDeaprtmentDiscription(rs.getString("departDes"));
				e.setApplicationNo(rs.getString("pav_application_no"));
				e.setReceiptNo(rs.getString("pav_receipt_no"));
				e.setBusRegNo(rs.getString("pm_vehicle_regno"));

				String approveStatusCode = rs.getString("pav_approved_status");
				String approveStatus = "";

				if (approveStatusCode != null) {
					if (approveStatusCode.equals("A")) {
						approveStatus = "APPROVED";
					} else if (approveStatusCode.equals("R")) {
						approveStatus = "REJECTED";
					} else if (approveStatusCode.equals("P")) {
						approveStatus = "PENDING";
					}
				}

				e.setApproveStatus(approveStatus);

				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				String date = dateFormat.format(rs.getTimestamp("pav_created_date"));

				e.setPaymentDate(date);
				e.setTotalAmount(rs.getBigDecimal("pav_total_amount"));

				paymentVoucherLIST.add(e);
			}

			return paymentVoucherLIST;

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
	public boolean isVoucherPayment(PaymentVoucherDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isVoucherPayment = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM public.nt_m_payment_voucher WHERE pav_voucher_no=? and pav_payment_type='V' ;";

			ps = con.prepareStatement(query);

			ps.setString(1, dto.getVoucherNo());
			rs = ps.executeQuery();

			if (rs.next()) {
				isVoucherPayment = true;
			} else {
				isVoucherPayment = false;
			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isVoucherPayment;
	}

	@Override
	public boolean rejectPayment(PaymentVoucherDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isReject = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_m_payment_voucher SET  pav_approved_status='R', pav_reject_reason=? "
					+ "WHERE pav_voucher_no=? ;";

			ps = con.prepareStatement(query);

			ps.setString(1, dto.getRejectReason());
			ps.setString(2, dto.getVoucherNo());
			int a = ps.executeUpdate();

			if (a > 0) {
				isReject = true;
			} else {
				isReject = false;
			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isReject;
	}

	@Override
	public boolean approvePayment(PaymentVoucherDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		boolean isApproved = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_m_payment_voucher SET pav_approved_status='A', pav_approved_date=? "
					+ "WHERE pav_voucher_no=? ;";

			ps = con.prepareStatement(query);

			ps.setTimestamp(1, timestamp);
			ps.setString(2, dto.getVoucherNo());
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
	public boolean isPaymentReject(PaymentVoucherDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isPermitReject = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM public.nt_m_payment_voucher "
					+ "WHERE pav_voucher_no=? and pav_approved_status='R' ;";

			ps = con.prepareStatement(query);
			ps.setString(1, dto.getVoucherNo());
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
	public boolean isPaymentApprove(PaymentVoucherDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isPermitApproved = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM public.nt_m_payment_voucher "
					+ "WHERE pav_voucher_no=? and pav_approved_status='A' ;";

			ps = con.prepareStatement(query);
			ps.setString(1, dto.getVoucherNo());
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
	public List<PaymentVoucherDTO> getApplicationNo() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<PaymentVoucherDTO> data = new ArrayList<PaymentVoucherDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct pav_application_no FROM public.nt_m_payment_voucher WHERE pav_application_no is not null and  pav_application_no !=''  order by pav_application_no;";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			PaymentVoucherDTO p;

			while (rs.next()) {
				p = new PaymentVoucherDTO();
				p.setApplicationNo(rs.getString("pav_application_no"));

				data.add(p);

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
	public List<String> getVoucherNo() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String voucher = null;
		List<String> data = new ArrayList<String>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct pav_voucher_no FROM public.nt_m_payment_voucher WHERE pav_voucher_no is not null and  pav_voucher_no !=''  order by pav_voucher_no ;";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				new PaymentVoucherDTO();
				voucher = rs.getString("pav_voucher_no");

				data.add(voucher);

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
	public List<PaymentVoucherDTO> getReceiptNo() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<PaymentVoucherDTO> data = new ArrayList<PaymentVoucherDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT pav_receipt_no FROM public.nt_m_payment_voucher WHERE pav_receipt_no is not null and pav_receipt_no !='' order by pav_receipt_no ; ";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			PaymentVoucherDTO p;

			while (rs.next()) {
				p = new PaymentVoucherDTO();
				p.setReceiptNo(rs.getString("pav_receipt_no"));

				data.add(p);

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
	public List<PaymentVoucherDTO> getUnit() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<PaymentVoucherDTO> data = new ArrayList<PaymentVoucherDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT code, description FROM nt_r_sub_unit WHERE active ='A'  order by description";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			PaymentVoucherDTO p;

			while (rs.next()) {
				p = new PaymentVoucherDTO();
				p.setUnitCode(rs.getString("code"));
				p.setUnitDiscription(rs.getString("description"));
				data.add(p);

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
	public List<PaymentVoucherDTO> getDepartment() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<PaymentVoucherDTO> data = new ArrayList<PaymentVoucherDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT code, description FROM nt_r_department WHERE active = 'A' order by description";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			PaymentVoucherDTO p;

			while (rs.next()) {
				p = new PaymentVoucherDTO();
				p.setDepartmentCode(rs.getString("code"));
				p.setDeaprtmentDiscription(rs.getString("description"));
				data.add(p);

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
	public List<PaymentVoucherDTO> getTranactionType() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<PaymentVoucherDTO> data = new ArrayList<PaymentVoucherDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT code, description FROM nt_r_transaction_type "
					+ "WHERE (active = 'Y') and (code='04' or code= '03' or code= '01' or code= '13' or code= '14' or code= '15' or code= '16' or code='08' or code='18' or code='21' or code='22' or code='23' or code='19' ) order by description; ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			PaymentVoucherDTO p;

			while (rs.next()) {
				p = new PaymentVoucherDTO();
				p.setTransactionCode(rs.getString("code"));
				p.setTransactionDescription(rs.getString("description"));
				data.add(p);

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
	public List<String> getChargeType() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> data = new ArrayList<String>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT code, description FROM nt_r_charge_type WHERE active = 'A' order by description;";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				data.add(rs.getString("description"));

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
	public List<String> getAccountNo(String code, PaymentVoucherDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> data = new ArrayList<String>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT tct_charge_type_code, tct_account_no FROM public.nt_r_trn_charge_type "
					+ "WHERE tct_charge_type_code=? and tct_status='A';";

			ps = con.prepareStatement(query);
			ps.setString(1, code);
			rs = ps.executeQuery();

			while (rs.next()) {
				data.add(rs.getString("tct_account_no"));
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
	public String generateReferenceNo() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;

		int year = Year.now().getValue();
		String currYear = Integer.toString(year).substring(2, 4);
		String strAppNo = "";
		String lastyr = "00";

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT pav_voucher_no " + " FROM public.nt_m_payment_voucher "
					+ " ORDER BY pav_created_date desc " + " LIMIT 1";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("pav_voucher_no");
			}

			if (result != null) {

				lastyr = result.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String ApprecordcountN = "00001";
					strAppNo = "VRN" + currYear + ApprecordcountN;
				} else {
					String number = result.substring(5, 10);
					int returncountvalue = Integer.valueOf(number) + 1;

					String ApprecordcountN = "";

					if (returncountvalue >= 10 && returncountvalue < 100) {
						ApprecordcountN = "000" + returncountvalue;
					} else if (returncountvalue >= 100 && returncountvalue < 1000) {
						ApprecordcountN = "00" + returncountvalue;
					} else if (returncountvalue >= 1000 && returncountvalue < 10000) {
						ApprecordcountN = "0" + returncountvalue;
					} else if (returncountvalue >= 10000 && returncountvalue < 100000) {
						ApprecordcountN = Integer.toString(returncountvalue);
					} else {
						ApprecordcountN = "0000" + returncountvalue;
					}
					strAppNo = "VRN" + currYear + ApprecordcountN;
				}
			} else
				strAppNo = "VRN" + currYear + "00001";

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return strAppNo;
	}

	@Override
	public String getTranCode(PaymentVoucherDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String transCode = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT code " + "FROM public.nt_r_transaction_type " + "WHERE description=? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, dto.getTransactionDescription());
			rs = ps.executeQuery();

			while (rs.next()) {
				transCode = (rs.getString("code"));

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return transCode;
	}

	@Override
	public List<PaymentVoucherDTO> getGeneratedVoucherDetails(String value) {
		Connection con = null;
		PreparedStatement pss = null;
		ResultSet rs = null;

		List<PaymentVoucherDTO> paymentVoucherList = new ArrayList<PaymentVoucherDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT nt_r_charge_type.description,vod_charge_type,vod_voucher_no, vod_account_no, vod_amount,vod_charge_code "
					+ "FROM public.nt_t_voucher_details "
					+ "inner join public.nt_m_payment_voucher  on nt_m_payment_voucher.pav_voucher_no= nt_t_voucher_details.vod_voucher_no "
					+ "inner join public.nt_r_charge_type on nt_r_charge_type.code= nt_t_voucher_details.vod_charge_code "
					+ "where nt_m_payment_voucher.pav_application_no=? ;";

			pss = con.prepareStatement(query);
			pss.setString(1, value);
			rs = pss.executeQuery();

			while (rs.next()) {
				PaymentVoucherDTO e = new PaymentVoucherDTO();

				e.setChargeDescription(rs.getString("description"));
				e.setAccountNO(rs.getString("vod_account_no"));
				e.setAmount(rs.getBigDecimal("vod_amount"));
				e.setChargeCode(rs.getString("vod_charge_code"));
				e.setVoucherNo(rs.getString("vod_voucher_no"));
				paymentVoucherList.add(e);

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(pss);
			ConnectionManager.close(con);
		}

		return paymentVoucherList;
	}

	@Override
	public List<PaymentVoucherDTO> getVoucherDetails(String description) {
		Connection con = null;
		PreparedStatement pss = null;
		ResultSet rs = null;

		List<PaymentVoucherDTO> paymentVoucherList = new ArrayList<PaymentVoucherDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select nt_r_charge_type.description, "
					+ "nt_r_trn_charge_type.tct_account_no, nt_r_trn_charge_type.tct_amount, nt_r_trn_charge_type.tct_charge_type_code "
					+ "from public.nt_r_transaction_type "
					+ "left outer join public.nt_r_trn_charge_type  on nt_r_trn_charge_type.tct_trn_type_code = nt_r_transaction_type.code  "
					+ "inner join  public.nt_r_charge_type on nt_r_trn_charge_type.tct_charge_type_code = public.nt_r_charge_type.code "
					+ "where nt_r_transaction_type.description = ? and nt_r_trn_charge_type.tct_status='A' ";

			pss = con.prepareStatement(query);
			pss.setString(1, description);
			rs = pss.executeQuery();

			while (rs.next()) {
				PaymentVoucherDTO e = new PaymentVoucherDTO();

				e.setChargeDescription(rs.getString("description"));
				e.setAccountNO(rs.getString("tct_account_no"));
				e.setAmount(rs.getBigDecimal("tct_amount"));
				e.setChargeCode(rs.getString("tct_charge_type_code"));
				paymentVoucherList.add(e);

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(pss);
			ConnectionManager.close(con);
		}

		return paymentVoucherList;
	}

	@Override
	public boolean checkApplicationNo(PaymentVoucherDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isThere = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "select  pm_application_no FROM public.nt_t_pm_application where pm_application_no=? and pm_status='A'";
			ps = con.prepareStatement(query);
			ps.setString(1, dto.getApplicationNo());
			rs = ps.executeQuery();

			if (rs.next()) {
				isThere = true;
			} else {
				isThere = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isThere;
	}

	@Override
	public boolean checkTenderApplicationNumber(String ApplicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isThere = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT  tap_application_no FROM public.nt_m_tender_applicant WHERE tap_application_no=? ";
			ps = con.prepareStatement(query);
			ps.setString(1, ApplicationNo);
			rs = ps.executeQuery();

			if (rs.next()) {
				isThere = true;
			} else {
				isThere = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isThere;
	}

	@Override
	public boolean checkApplicationNumber(String ApplicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isThere = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "select  pm_application_no FROM public.nt_t_pm_application where pm_application_no=?  ";
			ps = con.prepareStatement(query);
			ps.setString(1, ApplicationNo);
			rs = ps.executeQuery();

			if (rs.next()) {
				isThere = true;
			} else {
				isThere = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isThere;
	}

	@Override
	public boolean checkSisuSariyaApplicationNumber(String ApplicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isThere = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "select  sps_service_no FROM public.nt_m_sisu_permit_hol_service_info where sps_service_no=? ";
			ps = con.prepareStatement(query);
			ps.setString(1, ApplicationNo);
			rs = ps.executeQuery();

			if (rs.next()) {
				isThere = true;
			} else {
				isThere = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isThere;
	}

	@Override
	public boolean checkPermitNumber(String permitNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isThere = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "select  pm_permit_no FROM public.nt_t_pm_application where pm_permit_no=? ";
			ps = con.prepareStatement(query);
			ps.setString(1, permitNo);
			rs = ps.executeQuery();

			if (rs.next()) {
				isThere = true;
			} else {
				isThere = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isThere;
	}

	@Override
	public String getApplicationNo(String permitNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String getApplicationNo = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "select pm_application_no,  pm_permit_no FROM public.nt_t_pm_application where pm_permit_no=? order by pm_application_no";
			ps = con.prepareStatement(query);
			ps.setString(1, permitNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				getApplicationNo = (rs.getString("pm_application_no"));

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return getApplicationNo;
	}

	@Override
	public String getPermitNo(String ApplicationNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String getPermitNo = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "select pm_application_no,  pm_permit_no FROM public.nt_t_pm_application where pm_application_no=? and pm_permit_no is not null";
			ps = con.prepareStatement(query);
			ps.setString(1, ApplicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				getPermitNo = (rs.getString("pm_permit_no"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return getPermitNo;
	}

	@Override
	public boolean generateVoucher(PaymentVoucherDTO paymentVoucherDTO, String logInUser, BigDecimal totalAmount,
			String voucherNo, boolean isTender, boolean isSkipVoucher, boolean isSiSuSariya) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isVousherGenerated = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_payment_voucher");

			String query = "INSERT INTO public.nt_m_payment_voucher "
					+ "(pav_seq_no, pav_permit_no, pav_application_no, " + "pav_voucher_no,  pav_is_voucher_generated, "
					+ "pav_total_amount,pav_tran_type, pav_created_by, pav_created_date,pav_payment_type,pav_deparment,pav_approved_status, pav_receipt_no) "
					+ "VALUES(? , ? , ? , ? , ? , ? , ? , ? , ? , ?, ?, ?, ?); ";

			ps = con.prepareStatement(query);

			ps.setLong(1, seqNo);

			if (paymentVoucherDTO.getPermitNo() != null) {
				ps.setString(2, paymentVoucherDTO.getPermitNo());
			} else {
				ps.setString(2, null);
			}

			if (isSiSuSariya == true) {
			/*	ps.setString(3, paymentVoucherDTO.getServiceNo());*/
				ps.setString(3, paymentVoucherDTO.getServiceRefNo());
			} else {
				ps.setString(3, paymentVoucherDTO.getApplicationNo());
			}

			ps.setString(4, voucherNo);

			ps.setString(5, "Y");
			ps.setBigDecimal(6, totalAmount);
			ps.setString(7, paymentVoucherDTO.getTransactionCode());
			ps.setString(8, logInUser);
			ps.setTimestamp(9, timestamp);

			if (isTender == true) {
				ps.setString(10, "V");
			} else {
				ps.setString(10, "V");
			}

			ps.setString(11, paymentVoucherDTO.getDepartmentCode());

			if (isSkipVoucher == true) {
				ps.setString(12, "A");
				ps.setString(13, "SKIP");
			} else {
				ps.setString(12, "P");
				ps.setString(13, null);
			}

			int insert = ps.executeUpdate();

			if (insert > 0) {
				isVousherGenerated = true;
			} else {
				isVousherGenerated = false;
			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return isVousherGenerated;
	}

	@Override
	public boolean checkPhotoUploadHistory(String applicatinNo, String taskStatus) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isphotoUploadComplete = false;
		try {

			con = ConnectionManager.getConnection();

			String query2 = " SELECT tsd_app_no, tsd_task_code, tsd_status " + "FROM public.nt_h_task_his "
					+ "WHERE tsd_task_code='PM101' and tsd_status=? and tsd_app_no=? ;";

			ps = con.prepareStatement(query2);
			ps.setString(1, taskStatus);
			ps.setString(2, applicatinNo);
			rs = ps.executeQuery();

			if (rs.next() == true) {
				isphotoUploadComplete = true;
			} else {
				isphotoUploadComplete = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isphotoUploadComplete;
	}

	@Override
	public boolean checkPhotoUploadTaskDetails(String applicatinNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isphotoUploadComplete = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "select  tsd_app_no, tsd_task_code, tsd_status " + "FROM public.nt_t_task_det "
					+ "where tsd_task_code='PM101' and tsd_status='C' and tsd_app_no=? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, applicatinNo);
			rs = ps.executeQuery();

			if (rs.next() == true) {
				isphotoUploadComplete = true;
			} else {
				isphotoUploadComplete = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isphotoUploadComplete;
	}

	@Override
	public boolean isTenderVoucherGenerated(String applicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		new Timestamp(date.getTime());

		boolean wasVoucherGenerated = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT pav_application_no, pav_voucher_no, pav_payment_type FROM public.nt_m_payment_voucher "
					+ "WHERE pav_application_no=? ;";
			ps = con.prepareStatement(query);
			ps.setString(1, applicationNo);
			rs = ps.executeQuery();

			if (rs.next() == true) {
				wasVoucherGenerated = true;

			} else {
				wasVoucherGenerated = false;

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(con);
		}
		return wasVoucherGenerated;
	}

	@Override
	public boolean alreadyGenerate(String applicationNo, String loginUser) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		new Timestamp(date.getTime());

		boolean wasVoucherGenerated = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT tsd_app_no, tsd_task_code, tsd_status " + "FROM public.nt_t_task_det "
					+ "where tsd_task_code='PM300' and tsd_app_no=? and tsd_status='C' ;";

			ps = con.prepareStatement(query);
			ps.setString(1, applicationNo);
			rs = ps.executeQuery();

			if (rs.next() == true) {
				wasVoucherGenerated = true;

			} else {
				wasVoucherGenerated = false;

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(con);
		}
		return wasVoucherGenerated;

	}

	@Override
	public Long getSeqNo(String applicationNo) {

		long seq = 0;

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT pav_seq_no, pav_application_no "
					+ "FROM public.nt_m_payment_voucher where pav_application_no=? and pav_approved_status not in('R');";

			ps = con.prepareStatement(query);
			ps.setString(1, applicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {

				seq = rs.getLong("pav_seq_no");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return seq;
	}

	@Override
	public boolean updateTenderApplicant(PaymentVoucherDTO paymentDetails, String value) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isUpdateTask = false;

		java.util.Date date = new java.util.Date();
		new Timestamp(date.getTime());
		try {

			con = ConnectionManager.getConnection();
			String query2 = "UPDATE public.nt_m_tender_applicant SET tap_voucher_no=? WHERE tap_application_no=?";

			stmt = con.prepareStatement(query2);

			stmt.setString(1, value);
			stmt.setString(2, paymentDetails.getApplicationNo());

			stmt.executeUpdate();

			con.commit();

		} catch (Exception ex) {

			ex.printStackTrace();
			return isUpdateTask;

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return isUpdateTask;
	}

	@Override
	public boolean updateVoucherDetails(PaymentVoucherDTO paymentVoucherDTO, BigDecimal totalFee, String logingUser,
			String voucherNo, List<PaymentVoucherDTO> voucherDetails, boolean isSisuSariya) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isUpdateTask = false;

		PaymentVousherServiceImpl impl = new PaymentVousherServiceImpl();
		long seq = 0;
		if (isSisuSariya == true) {
			seq = impl.getSeqNo(paymentVoucherDTO.getServiceNo());

		} else {
			seq = impl.getSeqNo(paymentVoucherDTO.getApplicationNo());

		}

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			for (int i = 0; i < voucherDetails.size(); i++) {

				long seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_t_voucher_details");

				String query2 = "INSERT INTO public.nt_t_voucher_details "
						+ "(vod_seq_no, vod_payment_vou_seq, vod_voucher_no, vod_charge_type, "
						+ " vod_account_no, vod_amount, vod_created_by, "
						+ "vod_created_date, vod_modified_by, vod_modified_date, vod_permitno,vod_charge_code) "
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, null, null, ?, ?) ;";

				stmt = con.prepareStatement(query2);

				stmt.setLong(1, seqNo);
				stmt.setLong(2, seq);
				stmt.setString(3, voucherNo);
				stmt.setString(4, voucherDetails.get(i).getChargeCode());
				stmt.setString(5, voucherDetails.get(i).getAccountNO());
				stmt.setBigDecimal(6, voucherDetails.get(i).getAmount());
				stmt.setString(7, logingUser);
				stmt.setTimestamp(8, timestamp);
				stmt.setString(9, paymentVoucherDTO.getPermitNo());
				stmt.setString(10, voucherDetails.get(i).getChargeCode());

				stmt.executeUpdate();

			}

			con.commit();

		} catch (Exception ex) {

			ex.printStackTrace();
			return isUpdateTask;

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return isUpdateTask;
	}

	@Override
	public String getChargeCode(String chargeDes) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String chargeCode = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT code " + "FROM public.nt_r_charge_type " + "WHERE description=? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, chargeDes);
			rs = ps.executeQuery();

			while (rs.next()) {
				chargeCode = (rs.getString("code"));

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return chargeCode;
	}

	public int insertPaymentVoucher(PaymentVoucherDTO paymentVoucherDTO, String logInUser, BigDecimal totalAmount,
			List<AdvancedPaymentDTO> paymentDetails) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_payment_voucher");
			String voucherNo = generateReferenceNo();
			paymentVoucherDTO.setVoucherNo(voucherNo);

			String query = "INSERT INTO public.nt_m_payment_voucher "
					+ "(pav_seq_no, pav_permit_no, pav_application_no, pav_voucher_no, "
					+ " pav_payment_type, pav_is_voucher_generated, pav_total_amount, pav_tran_type, "
					+ " pav_deparment, pav_unit, pav_created_by, pav_created_date,pav_approved_status) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,'P'); ";

			ps = con.prepareStatement(query);

			ps.setLong(1, seqNo);
			ps.setString(2, paymentVoucherDTO.getPermitNo());
			ps.setString(3, paymentVoucherDTO.getApplicationNo());
			ps.setString(4, voucherNo);
			ps.setString(5, "AP");
			ps.setString(6, "Y");
			ps.setBigDecimal(7, totalAmount);
			ps.setString(8, paymentVoucherDTO.getTransactionCode());
			ps.setString(9, paymentVoucherDTO.getDepartmentCode());
			ps.setString(10, paymentVoucherDTO.getUnitCode());
			ps.setString(11, logInUser);
			ps.setTimestamp(12, timestamp);

			ps.executeUpdate();
			ConnectionManager.close(ps);
			// insert advanced payment details
			for (int i = 0; i < paymentDetails.size(); i++) {
				long seqNoDet = Utils.getNextValBySeqName(con, "seq_nt_t_advance_payment_det");

				query = "INSERT INTO public.nt_t_advance_payment_det"
						+ " (apd_seq_no, apd_payment_vou_seq, apd_voucher_no, apd_details_of_service, apd_no_of_unit, "
						+ " apd_amount, apd_created_by, apd_created_date) " + " VALUES(?,?,?,?,?,?,?,?); ";

				ps = con.prepareStatement(query);
				ps.setLong(1, seqNoDet);
				ps.setLong(2, seqNo);
				ps.setString(3, voucherNo);
				ps.setString(4, paymentDetails.get(i).getServiceDetails());
				ps.setBigDecimal(5, paymentDetails.get(i).getNoofUnits());
				ps.setBigDecimal(6, paymentDetails.get(i).getAmount());
				ps.setString(7, logInUser);
				ps.setTimestamp(8, timestamp);

				ps.executeUpdate();

			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return 0;
	}

	public List<CommonDTO> GetTransactionToDropdown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description FROM nt_r_transaction_type WHERE active = 'Y' order by description;";

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

	public String getAppNoforVoucher(String voucherNo, String paymentType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String getApplicationNo = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct pav_application_no from nt_m_payment_voucher where pav_payment_type = ? and pav_voucher_no = ? ";
			ps = con.prepareStatement(query);
			ps.setString(1, paymentType);
			ps.setString(2, voucherNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				getApplicationNo = (rs.getString("pav_application_no"));

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return getApplicationNo;
	}

	public List<AdvancedPaymentDTO> getAdvancedPaymentDet(String voucherNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<AdvancedPaymentDTO> paymentDetList = new ArrayList<AdvancedPaymentDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select apd_details_of_service, apd_no_of_unit, apd_amount,  apd_created_date "
					+ " from  nt_t_advance_payment_det " + " where apd_voucher_no = ?;";

			ps = con.prepareStatement(query);
			ps.setString(1, voucherNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				AdvancedPaymentDTO advancedPaymentDTO = new AdvancedPaymentDTO();
				advancedPaymentDTO.setServiceDetails(rs.getString("apd_details_of_service"));
				advancedPaymentDTO.setNoofUnits(rs.getBigDecimal("apd_no_of_unit"));
				advancedPaymentDTO.setAmount(rs.getBigDecimal("apd_amount"));
				advancedPaymentDTO.setCratedDate(rs.getTimestamp("apd_created_date"));
				paymentDetList.add(advancedPaymentDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return paymentDetList;

	}

	public BigDecimal getTotAmtforVoucher(String voucherNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		BigDecimal getTotAmtforVoucher = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT pav_total_amount FROM nt_m_payment_voucher WHERE pav_voucher_no= ? ";
			ps = con.prepareStatement(query);
			ps.setString(1, voucherNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				getTotAmtforVoucher = (rs.getBigDecimal("pav_total_amount"));

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return getTotAmtforVoucher;

	}

	public List<PaymentVoucherDTO> getVoucherPaymentDet(String voucherNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PaymentVoucherDTO> paymentDetList = new ArrayList<PaymentVoucherDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT vod_voucher_no,vod_charge_type,nt_r_charge_type.description,vod_account_no,vod_amount,vod_created_date "
					+ " FROM nt_t_voucher_details INNER JOIN nt_r_charge_type "
					+ " ON nt_t_voucher_details.vod_charge_type = nt_r_charge_type.code" + " WHERE vod_voucher_no= ?;";

			ps = con.prepareStatement(query);
			ps.setString(1, voucherNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				PaymentVoucherDTO paymentVoucherDTO = new PaymentVoucherDTO();
				paymentVoucherDTO.setVoucherNo(rs.getString("vod_voucher_no"));
				paymentVoucherDTO.setChargeCode(rs.getString("vod_charge_type"));
				paymentVoucherDTO.setChargeDescription(rs.getString("description"));
				paymentVoucherDTO.setAccountNO(rs.getString("vod_account_no"));
				paymentVoucherDTO.setAmount(rs.getBigDecimal("vod_amount"));
				paymentVoucherDTO.setCreateDate(rs.getTimestamp("vod_created_date"));
				paymentDetList.add(paymentVoucherDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return paymentDetList;

	}

	@Override
	public List<PaymentVoucherDTO> getVoucherPaymentDetails(PaymentVoucherDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PaymentVoucherDTO> paymentVoucherLIST = new ArrayList<PaymentVoucherDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT vod_charge_type, vod_account_no, vod_amount,nt_r_charge_type.description  FROM public.nt_t_voucher_details "
					+ "inner join public.nt_r_charge_type on nt_r_charge_type.code= nt_t_voucher_details.vod_charge_type "
					+ "WHERE vod_voucher_no=? ";

			PaymentVoucherDTO e;
			ps = con.prepareStatement(query2);
			ps.setString(1, dto.getVoucherNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				e = new PaymentVoucherDTO();

				e.setChargeDescription(rs.getString("description"));
				e.setAccountNO(rs.getString("vod_account_no"));
				e.setAmount(rs.getBigDecimal("vod_amount"));

				paymentVoucherLIST.add(e);
			}

			return paymentVoucherLIST;

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	@Override
	public boolean checkTaskHistory(PaymentVoucherDTO dto, String taskCode, String status) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isTaskAvailable = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "select  tsd_app_no, tsd_task_code, tsd_status FROM public.nt_h_task_his "
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
	public List<PaymentVoucherDTO> getApplicationNoUsingTranactionType(PaymentVoucherDTO dto,
			String transactionTypeCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<PaymentVoucherDTO> data = new ArrayList<PaymentVoucherDTO>();
		String query = null;
		try {

			con = ConnectionManager.getConnection();

			if (transactionTypeCode.equals("21") || transactionTypeCode.equals("22") || transactionTypeCode.equals("23")
					|| transactionTypeCode.equals("14") || transactionTypeCode.equals("15")
					|| transactionTypeCode.equals("16")) {

				query = "select nt_t_pm_application.pm_application_no ,nt_t_task_det.tsd_task_code "
						+ "from nt_t_pm_application inner join nt_t_task_det on "
						+ "nt_t_pm_application.pm_application_no = nt_t_task_det.tsd_app_no "
						+ "where ((nt_t_task_det.tsd_task_code ='AM100' and nt_t_task_det.tsd_status='C') or "
						+ "(nt_t_task_det.tsd_task_code ='PM300' and nt_t_task_det.tsd_status='O')) or "
						+ "((nt_t_task_det.tsd_task_code ='AM104' and nt_t_task_det.tsd_status='C') or "
						+ "(nt_t_task_det.tsd_task_code ='PM301' and nt_t_task_det.tsd_status='O') or "
						+ "(nt_t_task_det.tsd_task_code ='PM300' and nt_t_task_det.tsd_status='O')) and pm_tran_type="
						+ "'" + transactionTypeCode + "'";

			} else if (transactionTypeCode.equals("13")) {
				query = "select nt_t_pm_application.pm_application_no ,nt_t_task_det.tsd_task_code "
						+ "from nt_t_pm_application inner join nt_t_task_det on "
						+ "nt_t_pm_application.pm_application_no = nt_t_task_det.tsd_app_no "
						+ "where (nt_t_task_det.tsd_task_code ='AM104' and nt_t_task_det.tsd_status='C') or "
						+ "(nt_t_task_det.tsd_task_code ='PM301' and nt_t_task_det.tsd_status='O') or "
						+ "(nt_t_task_det.tsd_task_code ='PM300' and nt_t_task_det.tsd_status='O')and pm_tran_type="
						+ "'" + transactionTypeCode + "'";

			} else if (transactionTypeCode.equals("04")) {
				query = "select distinct nt_t_pm_application.pm_application_no ,nt_t_task_det.tsd_task_code "
						+ "from nt_t_pm_application inner join nt_t_task_det on "
						+ "nt_t_pm_application.pm_application_no = nt_t_task_det.tsd_app_no "
						+ "where (nt_t_task_det.tsd_task_code ='PM201' and nt_t_task_det.tsd_status='C') or "
						+ "(nt_t_task_det.tsd_task_code ='PM301' and nt_t_task_det.tsd_status='O') or "
						+ "(nt_t_task_det.tsd_task_code ='PM300' and nt_t_task_det.tsd_status='O');";

			} else if (transactionTypeCode.equals("03")) {
				query = "select distinct nt_t_pm_application.pm_application_no ,nt_t_task_det.tsd_task_code "
						+ "from nt_t_pm_application inner join nt_t_task_det on "
						+ "nt_t_pm_application.pm_application_no = nt_t_task_det.tsd_app_no "
						+ "where (nt_t_task_det.tsd_task_code ='PM201' and nt_t_task_det.tsd_status='C') or "
						+ "(nt_t_task_det.tsd_task_code ='PM300' and nt_t_task_det.tsd_status='O') or "
						+ "(nt_t_task_det.tsd_task_code ='PM301' and nt_t_task_det.tsd_status='O') "
						+ "and nt_t_pm_application.pm_isnew_permit='Y';";

			} else {
				return null;
			}

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			PaymentVoucherDTO p;

			while (rs.next()) {
				p = new PaymentVoucherDTO();
				p.setApplicationNo(rs.getString("pm_application_no"));
				data.add(p);

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
	public List<PaymentVoucherDTO> getPermitNoUsingTranactionType(PaymentVoucherDTO dto, String transactionTypeCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<PaymentVoucherDTO> data = new ArrayList<PaymentVoucherDTO>();
		String query = null;
		try {

			con = ConnectionManager.getConnection();

			if (transactionTypeCode.equals("21") || transactionTypeCode.equals("22") || transactionTypeCode.equals("23")
					|| transactionTypeCode.equals("14") || transactionTypeCode.equals("15")
					|| transactionTypeCode.equals("16")) {

				query = "select nt_t_pm_application.pm_permit_no ,nt_t_task_det.tsd_task_code "
						+ "from nt_t_pm_application inner join nt_t_task_det on "
						+ "nt_t_pm_application.pm_application_no = nt_t_task_det.tsd_app_no "
						+ "where ((nt_t_task_det.tsd_task_code ='AM100' and nt_t_task_det.tsd_status='C') or "
						+ "(nt_t_task_det.tsd_task_code ='PM300' and nt_t_task_det.tsd_status='O')) or "
						+ "((nt_t_task_det.tsd_task_code ='AM104' and nt_t_task_det.tsd_status='C') or "
						+ "(nt_t_task_det.tsd_task_code ='PM300' and nt_t_task_det.tsd_status='O'));";

			} else if (transactionTypeCode.equals("13")) {
				query = "select nt_t_pm_application.pm_permit_no ,nt_t_task_det.tsd_task_code "
						+ "from nt_t_pm_application inner join nt_t_task_det on "
						+ "nt_t_pm_application.pm_application_no = nt_t_task_det.tsd_app_no "
						+ "where (nt_t_task_det.tsd_task_code ='AM104' and nt_t_task_det.tsd_status='C') or "
						+ "(nt_t_task_det.tsd_task_code ='PM300' and nt_t_task_det.tsd_status='O'); ";

			} else if (transactionTypeCode.equals("04")) {
				query = "select distinct nt_t_pm_application.pm_permit_no ,nt_t_task_det.tsd_task_code "
						+ "from nt_t_pm_application inner join nt_t_task_det on "
						+ "nt_t_pm_application.pm_application_no = nt_t_task_det.tsd_app_no "
						+ "where (nt_t_task_det.tsd_task_code ='PM201' and nt_t_task_det.tsd_status='C') or "
						+ "(nt_t_task_det.tsd_task_code ='PM300' and nt_t_task_det.tsd_status='O');";

			} else if (transactionTypeCode.equals("03")) {
				query = "select distinct nt_t_pm_application.pm_permit_no ,nt_t_task_det.tsd_task_code "
						+ "from nt_t_pm_application inner join nt_t_task_det on "
						+ "nt_t_pm_application.pm_application_no = nt_t_task_det.tsd_app_no "
						+ "where (nt_t_task_det.tsd_task_code ='PM201' and nt_t_task_det.tsd_status='C') or "
						+ "(nt_t_task_det.tsd_task_code ='PM300' and nt_t_task_det.tsd_status='O') "
						+ "and nt_t_pm_application.pm_isnew_permit='Y' and nt_t_pm_application.pm_permit_no is not null;";

			} else {
				return null;
			}

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			PaymentVoucherDTO p;

			while (rs.next()) {
				p = new PaymentVoucherDTO();
				p.setPermitNo(rs.getString("pm_permit_no"));
				data.add(p);

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
	public String getAccountNoForEdit(String code) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String no = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT tct_charge_type_code, tct_account_no FROM public.nt_r_trn_charge_type "
					+ "WHERE tct_charge_type_code=?;";
			ps = con.prepareStatement(query);
			ps.setString(1, code);
			rs = ps.executeQuery();

			while (rs.next()) {
				no = rs.getString("tct_account_no");

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return no;
	}

	@Override
	public boolean checkVehicleNumber(String VehicleNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isThere = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "select  pm_vehicle_regno FROM public.nt_t_pm_application where pm_vehicle_regno=? ";
			ps = con.prepareStatement(query);
			ps.setString(1, VehicleNo);
			rs = ps.executeQuery();

			if (rs.next()) {
				isThere = true;
			} else {
				isThere = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isThere;
	}

	@Override
	public boolean CopyTaskDetailsANDinsertTaskHistorySubsidy(PaymentVoucherDTO dto, String loginUSer,
			String taskCode) {
		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		PaymentVoucherDTO paymentVoucherDTO = null;

		boolean isUpdate = false;

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT tsd_seq, tsd_request_no, tsd_reference_no, tsd_service_no, tsd_task_code, tsd_status, created_by, created_date, tsd_counterid "
					+ " FROM public.nt_t_subsidy_task_det where tsd_service_no=? AND tsd_task_code=? ";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, dto.getApplicationNo());
			stmt.setString(2, taskCode);

			rs = stmt.executeQuery();

			if (rs.next()) {

				isUpdate = true;

				paymentVoucherDTO = new PaymentVoucherDTO();
				paymentVoucherDTO.setTaskSeq(rs.getLong("tsd_seq"));
				paymentVoucherDTO.setServiceNo(rs.getString("tsd_service_no"));
				paymentVoucherDTO.setTaskCode(rs.getString("tsd_task_code"));
				paymentVoucherDTO.setApproveStatus(rs.getString("tsd_status"));
				paymentVoucherDTO.setCreateBy(rs.getString("created_by"));
				paymentVoucherDTO.setCreateDate(rs.getTimestamp("created_date"));

				String sql2 = "INSERT INTO public.nt_t_subsidy_task_det "
						+ "(tsd_seq, tsd_service_no, tsd_task_code, tsd_status, created_by, created_date, tsd_counterid) "
						+ "VALUES(?, ?, ?, ?, ?, ?);";

				stmt2 = con.prepareStatement(sql2);

				stmt2.setLong(1, paymentVoucherDTO.getTaskSeq());
				stmt2.setString(2, paymentVoucherDTO.getServiceNo());
				stmt2.setString(3, paymentVoucherDTO.getTaskCode());
				stmt2.setString(4, paymentVoucherDTO.getApproveStatus());
				stmt2.setString(5, paymentVoucherDTO.getCreateBy());
				stmt2.setTimestamp(6, paymentVoucherDTO.getCreateDate());

				stmt2.executeUpdate();

			} else {
				isUpdate = false;

			}

			commonService = (CommonService) SpringApplicationContex.getBean("commonService");
			migratedService = (MigratedService) SpringApplicationContex.getBean("migratedService");
			String queueNumber = migratedService.findQueueNumberFromApplicationNo(paymentVoucherDTO.getApplicationNo());
			migratedService.updateQueueNumberTaskInQueueMaster(queueNumber, paymentVoucherDTO.getApplicationNo(),
					paymentVoucherDTO.getTaskCode(), paymentVoucherDTO.getApproveStatus());

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
	public boolean changeTaskDetailsSubsidy(PaymentVoucherDTO dto, String oldTaskCode, String newtaskCode,
			String status) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String q = "UPDATE public.nt_t_subsidy_task_det SET tsd_status=?, tsd_task_code=?"
					+ "WHERE tsd_service_no=? AND tsd_task_code=? AND tsd_status=?;";

			ps = con.prepareStatement(q);
			ps.setString(1, status);
			ps.setString(2, newtaskCode);
			ps.setString(3, dto.getApplicationNo());
			ps.setString(4, oldTaskCode);
			ps.setString(5, "O");
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
	public List<PaymentVoucherDTO> getTranactionTypeForRpt() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<PaymentVoucherDTO> data = new ArrayList<PaymentVoucherDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT code, description FROM nt_r_transaction_type \r\n" + "WHERE (active = 'Y') and\r\n"
					+ "(code='04' or code= '13'  or code= '22' or code= '23'  or code= '21' or code='14'  or code='17')\r\n"
					+ "order by description; ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			PaymentVoucherDTO p;

			while (rs.next()) {
				p = new PaymentVoucherDTO();
				p.setTransactionCode(rs.getString("code"));
				p.setTransactionDescription(rs.getString("description"));
				data.add(p);

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
	public boolean isReceiptGenerated(PaymentVoucherDTO dto) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isReceiptGenerated = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM public.nt_t_receipt where rec_voucher_no=? and rec_is_receipt_generated =?";

			ps = con.prepareStatement(query);
			ps.setString(1, dto.getVoucherNo());
			ps.setString(2, "Y");

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
	public boolean checkPowerOfAttorney(String permitNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isFound = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM public.nt_m_atterny_holder  where ath_permit_no=? and ath_status=?";

			ps = con.prepareStatement(query);
			ps.setString(1, permitNo);
			ps.setString(2, "A");
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

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public MigratedService getMigratedService() {
		return migratedService;
	}

	public void setMigratedService(MigratedService migratedService) {
		this.migratedService = migratedService;
	}

	

}
