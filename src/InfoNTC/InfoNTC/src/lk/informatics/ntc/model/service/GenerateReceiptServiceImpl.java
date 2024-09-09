package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import lk.informatics.ntc.model.dto.AdvancedPaymentDTO;
import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.DriverConductorRegistrationDTO;
import lk.informatics.ntc.model.dto.GenerateReceiptDTO;
import lk.informatics.ntc.model.dto.PaymentVoucherDTO;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.roc.utils.common.Utils;

public class GenerateReceiptServiceImpl implements GenerateReceiptService {

	@Override
	public List<GenerateReceiptDTO> getTransactionType() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<GenerateReceiptDTO> data = new ArrayList<GenerateReceiptDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT code, description FROM public.nt_r_transaction_type WHERE active = 'Y' order by description;";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			GenerateReceiptDTO p;

			while (rs.next()) {
				p = new GenerateReceiptDTO();
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
	public List<GenerateReceiptDTO> getUnit() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<GenerateReceiptDTO> data = new ArrayList<GenerateReceiptDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT code, description FROM public.nt_r_sub_unit WHERE active ='A'  order by description";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			GenerateReceiptDTO p;

			while (rs.next()) {
				p = new GenerateReceiptDTO();
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
	public List<GenerateReceiptDTO> getDepartment() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<GenerateReceiptDTO> data = new ArrayList<GenerateReceiptDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT code, description FROM public.nt_r_department WHERE active = 'A' order by description";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			GenerateReceiptDTO p;

			while (rs.next()) {
				p = new GenerateReceiptDTO();
				p.setDepartmentCode(rs.getString("code"));
				p.setDepartmentDescription(rs.getString("description"));
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
	public List<GenerateReceiptDTO> getVoucherNo() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<GenerateReceiptDTO> data = new ArrayList<GenerateReceiptDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT pav_voucher_no FROM public.nt_m_payment_voucher WHERE pav_voucher_no is not null and pav_approved_status='A' and coalesce(pav_receipt_no,'X') <> 'SKIP' order by pav_approved_date desc ;";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			GenerateReceiptDTO p;

			while (rs.next()) {
				p = new GenerateReceiptDTO();
				p.setVoucherNo(rs.getString("pav_voucher_no"));

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
	public List<GenerateReceiptDTO> getReceiptNo() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<GenerateReceiptDTO> data = new ArrayList<GenerateReceiptDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT pav_receipt_no FROM public.nt_m_payment_voucher WHERE pav_receipt_no is not null and coalesce(pav_receipt_no,'X') <> 'SKIP' ORDER BY pav_receipt_no; ";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			GenerateReceiptDTO p;

			while (rs.next()) {
				p = new GenerateReceiptDTO();
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
	public List<GenerateReceiptDTO> getApplicationNo() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<GenerateReceiptDTO> data = new ArrayList<GenerateReceiptDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT distinct pav_application_no , pav_receipt_no FROM public.nt_m_payment_voucher "
					+ "WHERE pav_application_no is not null and pav_approved_status='A' and coalesce(pav_receipt_no,'X') <> 'SKIP' ORDER BY pav_application_no;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			GenerateReceiptDTO p;

			while (rs.next()) {
				p = new GenerateReceiptDTO();
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
	public List<GenerateReceiptDTO> getGenerateReceiptList(GenerateReceiptDTO dto) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String WHERE_SQL = "";
		boolean whereadded = false;

		if (dto.getVoucherNo() != null && !dto.getVoucherNo().equals("")) {

			WHERE_SQL = WHERE_SQL + " WHERE pav_voucher_no = " + "'" + dto.getVoucherNo() + "' ";

			whereadded = true;
		}
		if (dto.getTransactionCode() != null && !dto.getTransactionCode().equals("") && dto.getDepartmentCode() != null
				&& !dto.getDepartmentCode().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " and pav_tran_type = " + "'" + dto.getTransactionCode() + "' "
						+ " and pav_deparment = " + "'" + dto.getDepartmentCode() + "' ";
			}
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
		if (dto.getSearchDate() != null && !dto.getSearchDate().equals("")) {
			if (whereadded) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				String date = dateFormat.format(dto.getSearchDate());

				WHERE_SQL = WHERE_SQL + " and to_char(pav_created_date,'dd-MM-yyyy') = " + "'" + date + "' ";

			}
		}
		if (dto.getUnitCode() != null && !dto.getUnitCode().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " and pav_unit = " + "'" + dto.getUnitCode() + "' ";

			}
		}

		if (whereadded == false) {
			WHERE_SQL = " WHERE pav_receipt_no is null ";
		}

		List<GenerateReceiptDTO> generateReceiptList = new ArrayList<GenerateReceiptDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT pav_permit_no, pav_voucher_no, pav_tran_type, nt_r_transaction_type.description as transDes, nt_r_department.description as departDes, pav_application_no, "
					+ "pav_receipt_no, pav_approved_status,pav_created_date, pav_total_amount, pav_payment_type "
					+ "FROM public.nt_m_payment_voucher "
					+ "inner join nt_r_transaction_type on nt_r_transaction_type.code= nt_m_payment_voucher.pav_tran_type "
					+ "inner join nt_r_department  on nt_r_department.code= nt_m_payment_voucher.pav_deparment "
					+ WHERE_SQL + "and pav_approved_status='A' order by  pav_voucher_no desc;";

			GenerateReceiptDTO e;
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				e = new GenerateReceiptDTO();
				e.setVoucherNo(rs.getString("pav_voucher_no"));
				e.setPermitNo(rs.getString("pav_permit_no"));
				e.setTransactionCode(rs.getString("pav_tran_type"));
				e.setTransactionDescription(rs.getString("transDes"));
				e.setDepartmentDescription(rs.getString("departDes"));
				e.setApplicationNo(rs.getString("pav_application_no"));
				e.setReceiptNo(rs.getString("pav_receipt_no"));
				e.setTotalAmount(rs.getBigDecimal("pav_total_amount"));

				String approveStatus = "";
				String approveStatusCode = rs.getString("pav_approved_status");
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
				e.setPaymentType(rs.getString("pav_payment_type"));

				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				String date = dateFormat.format(rs.getTimestamp("pav_created_date"));

				e.setPaymentDate(date);

				generateReceiptList.add(e);
			}

			return generateReceiptList;

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
	public boolean checkDepartment(GenerateReceiptDTO dto) {
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
	public boolean checkTransaction(GenerateReceiptDTO dto) {
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
	public List<GenerateReceiptDTO> getPaymentMode() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<GenerateReceiptDTO> data = new ArrayList<GenerateReceiptDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT code, description FROM nt_r_payment_mode WHERE active = 'A' order by description";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			GenerateReceiptDTO p;

			while (rs.next()) {
				p = new GenerateReceiptDTO();
				p.setPaymentModeCode(rs.getString("code"));
				p.setPaymentModeDescription(rs.getString("description"));

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
	public List<GenerateReceiptDTO> getBankName() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<GenerateReceiptDTO> data = new ArrayList<GenerateReceiptDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT code, description FROM nt_r_bank WHERE active = 'A' order by description";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			GenerateReceiptDTO p;

			while (rs.next()) {
				p = new GenerateReceiptDTO();
				p.setBankCode(rs.getString("code"));
				p.setBankDescription(rs.getString("description"));

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
	public List<GenerateReceiptDTO> getBranchName() {
		List<GenerateReceiptDTO> data = new ArrayList<GenerateReceiptDTO>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT branch_code, description FROM nt_r_bank_branch "
					+ "inner join nt_r_branch on nt_r_bank_branch.branch_code= nt_r_branch.code "
					+ "WHERE nt_r_branch.active = 'A' " + "order by description";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			GenerateReceiptDTO p;

			while (rs.next()) {
				p = new GenerateReceiptDTO();
				p.setBranchCode(rs.getString("branch_code"));
				p.setBranchDescription(rs.getString("description"));

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
	public List<GenerateReceiptDTO> getBranchName(String bankCode) {
		List<GenerateReceiptDTO> data = new ArrayList<GenerateReceiptDTO>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT code, description FROM public.nt_r_branch " + "WHERE bank_code=? "
					+ "order by description";

			ps = con.prepareStatement(query);
			ps.setString(1, bankCode);
			rs = ps.executeQuery();

			GenerateReceiptDTO p;

			while (rs.next()) {
				p = new GenerateReceiptDTO();
				p.setBranchCode(rs.getString("code"));
				p.setBranchDescription(rs.getString("description"));

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
	public List<AdvancedPaymentDTO> getAdvancedPaymentDetailsList(String voucherNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<AdvancedPaymentDTO> data = new ArrayList<AdvancedPaymentDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT apd_details_of_service, apd_no_of_unit, apd_amount, apd_created_date FROM nt_t_advance_payment_det "
					+ "WHERE apd_voucher_no=? " + "order by apd_created_date";

			ps = con.prepareStatement(query);
			ps.setString(1, voucherNo);
			rs = ps.executeQuery();

			AdvancedPaymentDTO p;

			while (rs.next()) {
				p = new AdvancedPaymentDTO();
				p.setServiceDetails(rs.getString("apd_details_of_service"));
				p.setNoofUnits(rs.getBigDecimal("apd_no_of_unit"));
				p.setAmount(rs.getBigDecimal("apd_amount"));
				p.setCratedDate(rs.getTimestamp("apd_created_date"));

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
	public List<PaymentVoucherDTO> getVoucherDetailsList(String voucherNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<PaymentVoucherDTO> data = new ArrayList<PaymentVoucherDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT nt_r_charge_type.code as chargeCode, nt_r_charge_type.description as codeDescription, nt_t_voucher_details.vod_account_no as vodAccount, nt_t_voucher_details.vod_amount as vodAmount FROM nt_t_voucher_details "
					+ "inner join nt_r_charge_type on nt_t_voucher_details.vod_charge_type= nt_r_charge_type.code "
					+ "WHERE vod_voucher_no=? " + "order by nt_r_charge_type.description";

			ps = con.prepareStatement(query);
			ps.setString(1, voucherNo);
			rs = ps.executeQuery();

			PaymentVoucherDTO p;

			while (rs.next()) {
				p = new PaymentVoucherDTO();
				p.setChargeCode(rs.getString("chargeCode"));
				p.setChargeDescription(rs.getString("codeDescription"));
				p.setAmount(rs.getBigDecimal("vodAmount"));
				p.setAccountNO(rs.getString("vodAccount"));

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
	public void saveReceipt(GenerateReceiptDTO selectedRow, String loginUser) {

		String receiptNo = generateReceiptNo();
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		try {

			con = ConnectionManager.getConnection();
			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_receipt");
			String query = "INSERT INTO public.nt_t_receipt (rec_permit_no, rec_application_no, rec_voucher_no, rec_payment_mode, rec_is_diposit_to_branch, "
					+ "rec_receipt_no, rec_back_code, rec_branch_code, rec_cheque_no, rec_total_fee, rec_remarks, rec_is_receipt_generated, rec_re_printed_no, rec_seq_no, rep_created_date, rep_created_by) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

			ps = con.prepareStatement(query);
			if (selectedRow.getPermitNo() != null && !selectedRow.getPermitNo().trim().isEmpty()) {
				ps.setString(1, selectedRow.getPermitNo());
			} else {
				ps.setString(1, "");
			}
			ps.setString(2, selectedRow.getApplicationNo());
			ps.setString(3, selectedRow.getVoucherNo());
			ps.setString(4, selectedRow.getPaymentModeCode());

			String depositToBank = selectedRow.getDepositToBank() ? "Y" : "N";

			ps.setString(5, depositToBank);
			ps.setString(6, receiptNo);
			ps.setString(7, selectedRow.getBankCode());
			ps.setString(8, selectedRow.getBranchCode());
			ps.setString(9, selectedRow.getChequeOrBankReceipt());
			ps.setBigDecimal(10, selectedRow.getTotalAmount());
			ps.setString(11, selectedRow.getRemarks());
			ps.setString(12, "Y");
			ps.setString(13, selectedRow.getRePrintedNo());
			ps.setLong(14, seqNo);
			ps.setTimestamp(15, timestamp);
			ps.setString(16, loginUser);
			ps.executeUpdate();

			// update nt_m_payment_voucher table
			String query2 = "UPDATE public.nt_m_payment_voucher " + "SET pav_receipt_no='" + receiptNo + "' "
					+ "WHERE pav_voucher_no=?";
			ps2 = con.prepareStatement(query2);
			ps2.setString(1, selectedRow.getVoucherNo());
			ps2.executeUpdate();

			con.commit();

			if (selectedRow.getPaymentType().equals("V")) {
				if (selectedRow.getTransactionCode().equals("01")) {
					updateTenderTaskStatusAfterGenerateReceipt(receiptNo, selectedRow.getApplicationNo(),
							selectedRow.getVoucherNo(), loginUser);
				} else if (selectedRow.getTransactionCode().equals("18")) {
					updateSisuSariyaTaskStatusAfterGenerateReceipt(receiptNo, selectedRow.getApplicationNo(),
							selectedRow.getVoucherNo(), loginUser);
				} else {
					updateAfterGenerateReceipt(receiptNo, selectedRow.getApplicationNo(), selectedRow.getVoucherNo(),
							loginUser);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(con);
		}

	}

	@Override
	public void updateTaskStatus(String applicationNo, String user) {

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
				if ((rs.getString("tsd_task_code").equals("PM301")) && (rs.getString("tsd_status").equals("C"))) {

					// insert to history table
					String q = "INSERT INTO public.nt_h_task_his "
							+ "(tsd_seq, tsd_vehicle_no, tsd_app_no, tsd_task_code, tsd_status, created_by, created_date) "
							+ "VALUES(?, ?, ?, 'PM301', 'C', ?, ?); ";

					ps2 = con.prepareStatement(q);
					ps2.setInt(1, rs.getInt("tsd_seq"));
					ps2.setString(2, rs.getString("tsd_vehicle_no"));
					ps2.setString(3, applicationNo);
					ps2.setString(4, rs.getString("created_by"));
					ps2.setTimestamp(5, rs.getTimestamp("created_date"));
					ps2.executeUpdate();
					con.commit();
					try {
						if (ps2 != null)
							ps2.close();
					} catch (Exception e) {
						e.printStackTrace();
					}

					// update task details table
					String q2 = "UPDATE public.nt_t_task_det "
							+ "SET tsd_task_code='PM302', tsd_status='O', created_by=?,  created_date=? "
							+ "WHERE tsd_app_no=?";
					ps3 = con.prepareStatement(q2);
					ps3.setString(1, user);
					ps3.setTimestamp(2, timestamp);
					ps3.setString(3, applicationNo);
					ps3.executeUpdate();
					con.commit();
					try {
						if (ps3 != null)
							ps3.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				con.commit();
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
	}

	public String generateReceiptNo() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;

		int year = Year.now().getValue();
		String currYear = Integer.toString(year).substring(2, 4);
		String strRecNo = "";
		String lastyr = "00";

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT rec_receipt_no FROM public.nt_t_receipt ORDER BY rep_created_date desc  LIMIT 1";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("rec_receipt_no");
			}

			if (result != null) {

				lastyr = result.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String ApprecordcountN = "00001";
					strRecNo = "REC" + currYear + ApprecordcountN;
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
					strRecNo = "REC" + currYear + ApprecordcountN;
				}
			} else {
				strRecNo = "REC" + currYear + "00001";
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return strRecNo;
	}

	@Override
	public boolean isReceiptgenerated(String voucherNo) {
		boolean isReceiptgenerated = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT  rec_is_receipt_generated " + "FROM public.nt_t_receipt "
					+ "WHERE rec_voucher_no=? LIMIT 1;";

			ps = con.prepareStatement(query);
			ps.setString(1, voucherNo);
			rs = ps.executeQuery();

			if (rs.next() && rs.getString("rec_is_receipt_generated").equals("Y")) {
				isReceiptgenerated = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isReceiptgenerated;
	}

	public void updateAfterGenerateReceipt(String receiptNo, String applicationNo, String voucherNo, String user) {

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
						+ "VALUES(?, ?, ?, 'PM302', 'O', ?, ?); ";

				ps3 = con.prepareStatement(q3);
				ps3.setInt(1, rs.getInt("tsd_seq"));
				ps3.setString(2, rs.getString("tsd_vehicle_no"));
				ps3.setString(3, applicationNo);
				ps3.setString(4, rs.getString("created_by"));
				ps3.setTimestamp(5, rs.getTimestamp("created_date"));

				ps3.executeUpdate();
				con.commit();
				ConnectionManager.close(ps3);
			}
			ConnectionManager.close(ps2);

			// update task details table
			String q4 = "UPDATE public.nt_t_task_det "
					+ "SET tsd_task_code='PM302', tsd_status='C', created_by=?, created_date=? " + "WHERE tsd_app_no=?";
			ps4 = con.prepareStatement(q4);
			ps4.setString(1, user);
			ps4.setTimestamp(2, timestamp);
			ps4.setString(3, applicationNo);
			ps4.executeUpdate();
			con.commit();
			ConnectionManager.close(ps4);

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps2);
			ConnectionManager.close(ps3);
			ConnectionManager.close(ps4);
			ConnectionManager.close(con);
		}

	}

	@Override
	public void rePrintUpdate(String voucherNo) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT  rec_re_printed_no " + "FROM public.nt_t_receipt " + "WHERE rec_voucher_no=?";

			ps = con.prepareStatement(query);
			ps.setString(1, voucherNo);
			rs = ps.executeQuery();

			int rePrintedNo = 0;
			int curRePrintedNo = 0;

			while (rs.next()) {
				if (rs.getString("rec_re_printed_no") == null) {
					curRePrintedNo = 0;
				} else {
					curRePrintedNo = Integer.parseInt(rs.getString("rec_re_printed_no"));
				}

				if (curRePrintedNo == 0) {
					rePrintedNo = 1;
				} else {
					rePrintedNo = curRePrintedNo + 1;
				}
			}

			String rePrintedNoUpdate = String.valueOf(rePrintedNo);
			String q2 = "UPDATE public.nt_t_receipt SET rec_re_printed_no='" + rePrintedNoUpdate
					+ "' WHERE rec_voucher_no=?";
			ps2 = con.prepareStatement(q2);
			ps2.setString(1, voucherNo);
			ps2.executeUpdate();
			con.commit();
			try {
				if (ps2 != null)
					ps2.close();
			} catch (Exception e) {
				e.printStackTrace();
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
	public List<CommonDTO> counterdropdown() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  cou_counter_id, cou_counter_name " + "FROM public.nt_r_counter "
					+ "WHERE cou_status='I' AND cou_trn_type='06'";
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
	public void counterStatus(String counterId, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_r_counter "
					+ "SET cou_status='A',cou_serving_queueno=NULL, cou_assigned_userid='" + user + "' "
					+ "WHERE cou_counter_id='" + counterId + "'";
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

	public GenerateReceiptDTO getApplicationDetailsByQueueNo(String queueNo) {
		GenerateReceiptDTO generateReceiptDTO = new GenerateReceiptDTO();

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		String applicationNumber = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT que_application_no,que_service_type,que_number " + "FROM public.nt_m_queue_master "
					+ "WHERE que_number = ? ";

			ps = con.prepareStatement(query);
			ps.setString(1, queueNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				applicationNumber = rs.getString("que_application_no");
			}

			String query2 = "SELECT pav_permit_no, pav_voucher_no, pav_tran_type, nt_r_transaction_type.description as transDes,pav_deparment, nt_r_department.description as departDes, pav_application_no, "
					+ "pav_receipt_no, pav_created_date, pav_unit " + "FROM public.nt_m_payment_voucher "
					+ "inner join public.nt_r_transaction_type on nt_r_transaction_type.code= nt_m_payment_voucher.pav_tran_type "
					+ "inner join public.nt_r_department on nt_r_department.code= nt_m_payment_voucher.pav_deparment "
					+ "WHERE pav_application_no = ? ";

			ps2 = con.prepareStatement(query2);
			ps2.setString(1, applicationNumber);
			rs2 = ps2.executeQuery();

			while (rs2.next()) {
				generateReceiptDTO.setTransactionCode(rs2.getString("pav_tran_type"));
				generateReceiptDTO.setTransactionDescription(rs2.getString("transDes"));
				generateReceiptDTO.setDepartmentCode(rs2.getString("pav_deparment"));
				generateReceiptDTO.setDepartmentDescription(rs2.getString("departDes"));
				generateReceiptDTO.setPermitNo(rs2.getString("pav_permit_no"));
				generateReceiptDTO.setVoucherNo(rs2.getString("pav_voucher_no"));
				generateReceiptDTO.setApplicationNo(rs2.getString("pav_application_no"));
				generateReceiptDTO.setSearchDate(rs2.getDate("pav_created_date"));
				generateReceiptDTO.setUnitCode(rs2.getString("pav_unit"));
				if (rs2.getString("pav_receipt_no") != null) {
					generateReceiptDTO.setReceiptNo(rs2.getString("pav_receipt_no"));
				}
				generateReceiptDTO.setQueueNo(queueNo);
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
		return generateReceiptDTO;

	}

	@Override
	public int getParaTotalAmount() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int data = 0;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT param_name, number_value FROM public.nt_r_parameters WHERE param_name = 'GENERATE_RECEIPT_TOTAL_AMOUNT';";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				data = rs.getInt("number_value");
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
	public boolean isPrintCompleted(String voucherNo) {
		boolean isPrintCompleted = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT  pav_isprint " + "FROM public.nt_m_payment_voucher " + "WHERE pav_voucher_no=? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, voucherNo);
			rs = ps.executeQuery();

			if (rs.next()) {
				String isPrint = rs.getString("pav_isprint");
				if (isPrint != null) {
					if (isPrint.equals("Y")) {
						isPrintCompleted = true;
					} else {
						isPrintCompleted = false;
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
		return isPrintCompleted;
	}

	@Override
	public void printUpdate(String voucherNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_m_payment_voucher SET pav_isprint='Y' WHERE pav_voucher_no=?";
			ps = con.prepareStatement(query);
			ps.setString(1, voucherNo);
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
	public String getReceiptNoForPrint(String voucherNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String receiptNoForPrint = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT pav_receipt_no FROM public.nt_m_payment_voucher WHERE pav_voucher_no = ?;";

			ps = con.prepareStatement(query);
			ps.setString(1, voucherNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				receiptNoForPrint = rs.getString("pav_receipt_no");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return receiptNoForPrint;
	}

	@Override
	public List<GenerateReceiptDTO> getVoucherNo(String transTypeCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<GenerateReceiptDTO> data = new ArrayList<GenerateReceiptDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT pav_voucher_no FROM public.nt_m_payment_voucher WHERE pav_voucher_no is not null and pav_approved_status='A' and pav_tran_type=? order by pav_approved_date desc ";
			ps = con.prepareStatement(query);
			ps.setString(1, transTypeCode);
			rs = ps.executeQuery();

			GenerateReceiptDTO p;

			while (rs.next()) {
				p = new GenerateReceiptDTO();
				p.setVoucherNo(rs.getString("pav_voucher_no"));

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
	public GenerateReceiptDTO getReceiptDetails(String voucherNo) {
		GenerateReceiptDTO receiptDetails = new GenerateReceiptDTO();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps2 = null;
		ResultSet rs2 = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT rec_payment_mode, rec_back_code, rec_branch_code, rec_is_diposit_to_branch, rec_cheque_no, rec_remarks "
					+ "FROM public.nt_t_receipt " + "WHERE rec_voucher_no=? LIMIT 1;";

			ps = con.prepareStatement(query);
			ps.setString(1, voucherNo);
			rs = ps.executeQuery();

			if (rs.next()) {
				receiptDetails.setPaymentModeCode(rs.getString("rec_payment_mode"));
				if (rs.getString("rec_back_code") != null) {
					receiptDetails.setBankCode(rs.getString("rec_back_code"));
					receiptDetails.setBranchCode(rs.getString("rec_branch_code"));

					// get bank and branch description
					String query2 = "SELECT b.description as bankDes, c.description as branchDes "
							+ "FROM public.nt_t_receipt r " + "INNER JOIN nt_r_bank b on b.code= r.rec_back_code "
							+ "INNER JOIN nt_r_branch c on c.code= r.rec_branch_code " + "WHERE rec_voucher_no=? "
							+ "LIMIT 1;";
					ps2 = con.prepareStatement(query2);
					ps2.setString(1, voucherNo);
					rs2 = ps2.executeQuery();
					if (rs2.next()) {
						receiptDetails.setBankDescription(rs2.getString("bankDes"));
						receiptDetails.setBranchDescription(rs2.getString("branchDes"));
					}
				}
				if (rs.getString("rec_is_diposit_to_branch").equals("Y")) {
					receiptDetails.setDepositToBank(true);
				} else if (rs.getString("rec_is_diposit_to_branch").equals("N")) {
					receiptDetails.setDepositToBank(false);
				}
				receiptDetails.setChequeOrBankReceipt(rs.getString("rec_cheque_no"));
				receiptDetails.setRemarks(rs.getString("rec_remarks"));
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
		return receiptDetails;
	}

	@Override
	public GenerateReceiptDTO getSearchFields(String voucherNo) {
		GenerateReceiptDTO searchFields = new GenerateReceiptDTO();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT pav_tran_type,pav_deparment,pav_receipt_no,pav_application_no, nt_r_transaction_type.description as transDes, nt_r_department.description as departDes, pav_permit_no, pav_created_date, pav_unit "
					+ "FROM public.nt_m_payment_voucher "
					+ "inner join nt_r_transaction_type on nt_r_transaction_type.code= nt_m_payment_voucher.pav_tran_type "
					+ "inner join nt_r_department  on nt_r_department.code= nt_m_payment_voucher.pav_deparment "
					+ "WHERE pav_voucher_no=? ;";
			ps = con.prepareStatement(query);
			ps.setString(1, voucherNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				searchFields.setTransactionCode(rs.getString("pav_tran_type"));
				searchFields.setDepartmentCode(rs.getString("pav_deparment"));
				searchFields.setTransactionDescription(rs.getString("transDes"));
				searchFields.setDepartmentDescription(rs.getString("departDes"));
				searchFields.setReceiptNo(rs.getString("pav_receipt_no"));
				searchFields.setApplicationNo(rs.getString("pav_application_no"));
				searchFields.setPermitNo(rs.getString("pav_permit_no"));
				searchFields.setUnitCode(rs.getString("pav_unit"));
				searchFields.setSearchDate(rs.getDate("pav_created_date"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return searchFields;

	}

	@Override
	public void updateTenderTaskStatus(String applicationNo, String user) {

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
				if ((rs.getString("tsd_task_code").equals("TD008")) && (rs.getString("tsd_status").equals("C"))) {

					// insert to history table
					String q = "INSERT INTO public.nt_h_task_his "
							+ "(tsd_seq, tsd_vehicle_no, tsd_app_no, tsd_task_code, tsd_status, created_by, created_date) "
							+ "VALUES(?, ?, ?, 'TD008', 'C', ?, ?); ";

					ps2 = con.prepareStatement(q);
					ps2.setInt(1, rs.getInt("tsd_seq"));
					ps2.setString(2, rs.getString("tsd_vehicle_no"));
					ps2.setString(3, applicationNo);
					ps2.setString(4, rs.getString("created_by"));
					ps2.setTimestamp(5, rs.getTimestamp("created_date"));
					ps2.executeUpdate();
					con.commit();
					try {
						if (ps2 != null)
							ps2.close();
					} catch (Exception e) {
						e.printStackTrace();
					}

					// update task details table
					String q2 = "UPDATE public.nt_t_task_det "
							+ "SET tsd_task_code='TD009', tsd_status='O', created_by=?, created_date=? "
							+ "WHERE tsd_app_no=?";
					ps3 = con.prepareStatement(q2);
					ps3.setString(1, user);
					ps3.setTimestamp(2, timestamp);
					ps3.setString(3, applicationNo);
					ps3.executeUpdate();
					con.commit();
					try {
						if (ps3 != null)
							ps3.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				con.commit();
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

	public void updateTenderTaskStatusAfterGenerateReceipt(String receiptNo, String applicationNo, String voucherNo,
			String user) {

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
						+ "VALUES(?, ?, ?, 'TD009', 'O', ?, ?); ";

				ps3 = con.prepareStatement(q3);
				ps3.setInt(1, rs.getInt("tsd_seq"));
				ps3.setString(2, rs.getString("tsd_vehicle_no"));
				ps3.setString(3, applicationNo);
				ps3.setString(4, rs.getString("created_by"));
				ps3.setTimestamp(5, rs.getTimestamp("created_date"));

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
					+ "SET tsd_task_code='TD009', tsd_status='C', created_by=?,  created_date=? "
					+ "WHERE tsd_app_no=?";
			ps4 = con.prepareStatement(q4);
			ps4.setString(1, user);
			ps4.setTimestamp(2, timestamp);
			ps4.setString(3, applicationNo);
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
	public void updateSisuSariyaTaskStatus(String applicationNo, String user,String serviceNo) {

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT * " + "FROM public.nt_t_subsidy_task_det " + "where tsd_reference_no=? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, applicationNo);
			rs = ps.executeQuery();

			if (rs.next() == true) {
				if ((rs.getString("tsd_task_code").equals("SM002")) && (rs.getString("tsd_status").equals("C"))) {

					// insert to history table
					String q = "INSERT INTO public.nt_h_subsidy_task_his "
							+ "(tsd_seq, tsd_request_no, tsd_service_no, tsd_reference_no,tsd_task_code, tsd_status, created_by, created_date) "
							+ "VALUES(?, ?, ?, ?, 'SM002', 'C', ?, ?); ";

					ps2 = con.prepareStatement(q);
					ps2.setInt(1, rs.getInt("tsd_seq"));
					ps2.setString(2, rs.getString("tsd_request_no"));
					ps2.setString(3, serviceNo);
					ps2.setString(4, rs.getString("tsd_reference_no"));
					ps2.setString(5, rs.getString("created_by"));
					ps2.setTimestamp(6, rs.getTimestamp("created_date"));
					ps2.executeUpdate();
					//con.commit();
					try {
						if (ps2 != null)
							ps2.close();
					} catch (Exception e) {
						e.printStackTrace();
					}

					// update task details table
					String q2 = "UPDATE public.nt_t_subsidy_task_det "
							+ "SET tsd_task_code='SM003', tsd_status='O', created_by=?, created_date=? "
							+ "WHERE tsd_reference_no =?";
					ps3 = con.prepareStatement(q2);
					ps3.setString(1, user);
					ps3.setTimestamp(2, timestamp);
					ps3.setString(3, applicationNo);
					ps3.executeUpdate();
					//con.commit();
					try {
						if (ps3 != null)
							ps3.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				con.commit();
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

	public void updateSisuSariyaTaskStatusAfterGenerateReceipt(String receiptNo, String applicationNo, String voucherNo,
			String user) {

		Connection con = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		PreparedStatement ps4 = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		con = ConnectionManager.getConnection();

		try {

			con = ConnectionManager.getConnection();

			/*String q2 = "SELECT * " + "FROM public.nt_t_subsidy_task_det " + "where tsd_service_no=? ;";

			ps2 = con.prepareStatement(q2);
			ps2.setString(1, applicationNo);
			rs = ps2.executeQuery();

			if (rs.next() == true) {
				// insert in to task history table
				String q3 = "INSERT INTO public.nt_h_subsidy_task_his "
						+ "(tsd_seq, tsd_request_no, tsd_service_no,tsd_reference_no,  tsd_task_code, tsd_status, created_by, created_date) "
						+ "VALUES(?, ?, ?, ?, ?,?, ?, ?); ";

				ps3 = con.prepareStatement(q3);
				ps3.setInt(1, rs.getInt("tsd_seq"));
				ps3.setString(2, rs.getString("tsd_request_no"));
				ps3.setString(3, applicationNo);
				ps3.setString(4, rs.getString("tsd_reference_no"));
				ps3.setString(5, "SM002");
				ps3.setString(6, "C");
				ps3.setString(7, rs.getString("created_by"));
				ps3.setTimestamp(8, rs.getTimestamp("created_date"));

				ps3.executeUpdate();
				con.commit();
				try {
					if (ps3 != null)
						ps3.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}*/
			try {
				if (ps2 != null)
					ps2.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			// update task details table
			String q4 = "UPDATE public.nt_t_subsidy_task_det "
					+ "SET tsd_task_code='SM003', tsd_status='C', created_by=?,  created_date=? "
					+ "WHERE tsd_reference_no =?";
			ps4 = con.prepareStatement(q4);
			ps4.setString(1, user);
			ps4.setTimestamp(2, timestamp);
			ps4.setString(3, applicationNo);
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
	public List<DriverConductorRegistrationDTO> getVoucherNoListForDc() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<DriverConductorRegistrationDTO> returnVoucherList = new ArrayList<DriverConductorRegistrationDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select dcv_voucher_no FROM public.nt_m_driver_conduc_payment_voucher where dcv_voucher_no is not null and dcv_approved_status ='A' order by nt_m_driver_conduc_payment_voucher desc  ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				DriverConductorRegistrationDTO voucherDTO = new DriverConductorRegistrationDTO();
				voucherDTO.setVoucher(rs.getString("dcv_voucher_no"));

				returnVoucherList.add(voucherDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return returnVoucherList;

	}

	@Override
	public List<DriverConductorRegistrationDTO> getApplicationNoListForDC() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<DriverConductorRegistrationDTO> appNoList = new ArrayList<DriverConductorRegistrationDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select dcv_application_no  FROM public.nt_m_driver_conduc_payment_voucher where dcv_voucher_no is not null and dcv_approved_status ='A' order by dcv_application_no   ";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				DriverConductorRegistrationDTO appNo = new DriverConductorRegistrationDTO();
				appNo.setAppNo(rs.getString("dcv_application_no"));

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
	public List<DriverConductorRegistrationDTO> getDriverConductorIdListForDC() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<DriverConductorRegistrationDTO> dcIdList = new ArrayList<>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select dcv_driver_conduc_id_no   FROM public.nt_m_driver_conduc_payment_voucher where dcv_voucher_no is not null and dcv_approved_status ='A' order by dcv_driver_conduc_id_no   ";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				DriverConductorRegistrationDTO dcIdDTO1 = new DriverConductorRegistrationDTO();
				dcIdDTO1.setDriverConductorId(rs.getString("dcv_driver_conduc_id_no"));
				dcIdList.add(dcIdDTO1);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return dcIdList;
	}

	@Override
	public DriverConductorRegistrationDTO getValuesAccordingtoVoucherNo(String voucherNo) {
		DriverConductorRegistrationDTO dcreceiptDTO = new DriverConductorRegistrationDTO();

		Connection con = null;

		PreparedStatement ps2 = null;

		ResultSet rs2 = null;

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select dcv_training_type,a.description,dcv_application_no,dcv_id_no,dcv_driver_conduc_id_no,dcv_receipt_no \r\n"
					+ "from public.nt_m_driver_conduc_payment_voucher \r\n"
					+ "inner join public.nt_r_training_type a on a.code=nt_m_driver_conduc_payment_voucher.dcv_training_type\r\n"
					+ "where dcv_voucher_no=? \r\n" + "and a.status='A'\r\n" + "";

			ps2 = con.prepareStatement(query2);
			ps2.setString(1, voucherNo);
			rs2 = ps2.executeQuery();

			while (rs2.next()) {
				dcreceiptDTO.setAppNo(rs2.getString("dcv_application_no"));
				dcreceiptDTO.setNic(rs2.getString("dcv_id_no"));
				dcreceiptDTO.setDriverConductorId(rs2.getString("dcv_driver_conduc_id_no"));
				dcreceiptDTO.setTrainingTYpeDes(rs2.getString("description"));
				dcreceiptDTO.setReceiptNo(rs2.getString("dcv_receipt_no"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs2);
			ConnectionManager.close(ps2);
			ConnectionManager.close(con);
		}
		return dcreceiptDTO;

	}

	@Override
	public DriverConductorRegistrationDTO getValuesAccordingToApplicationNo(String appNo) {
		DriverConductorRegistrationDTO dcreceiptDTO1 = new DriverConductorRegistrationDTO();

		Connection con = null;

		PreparedStatement ps2 = null;

		ResultSet rs2 = null;

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select dcv_training_type,a.description,dcv_voucher_no,dcv_id_no,dcv_driver_conduc_id_no,dcv_receipt_no\r\n"
					+ "from public.nt_m_driver_conduc_payment_voucher \r\n"
					+ "inner join public.nt_r_training_type a on a.code=nt_m_driver_conduc_payment_voucher.dcv_training_type\r\n"
					+ "where dcv_application_no =? \r\n" + "and a.status='A'\r\n" + "";

			ps2 = con.prepareStatement(query2);
			ps2.setString(1, appNo);
			rs2 = ps2.executeQuery();

			while (rs2.next()) {
				dcreceiptDTO1.setVoucher(rs2.getString("dcv_voucher_no"));
				dcreceiptDTO1.setNic(rs2.getString("dcv_id_no"));
				dcreceiptDTO1.setDriverConductorId(rs2.getString("dcv_driver_conduc_id_no"));
				dcreceiptDTO1.setTrainingTYpeDes(rs2.getString("description"));
				dcreceiptDTO1.setReceiptNo(rs2.getString("dcv_receipt_no"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs2);
			ConnectionManager.close(ps2);
			ConnectionManager.close(con);
		}
		return dcreceiptDTO1;

	}

	@Override
	public DriverConductorRegistrationDTO getValuesAccordingToDcIdNo(String dcIdNo) {
		DriverConductorRegistrationDTO dcreceiptDTO2 = new DriverConductorRegistrationDTO();

		Connection con = null;

		PreparedStatement ps2 = null;

		ResultSet rs2 = null;

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select dcv_training_type,a.description,dcv_application_no,dcv_id_no,dcv_voucher_no,dcv_receipt_no \r\n"
					+ "from public.nt_m_driver_conduc_payment_voucher \r\n"
					+ "inner join public.nt_r_training_type a on a.code=nt_m_driver_conduc_payment_voucher.dcv_training_type\r\n"
					+ "where  dcv_driver_conduc_id_no=? \r\n" + "and a.status='A'\r\n" + "";

			ps2 = con.prepareStatement(query2);
			ps2.setString(1, dcIdNo);
			rs2 = ps2.executeQuery();

			while (rs2.next()) {
				dcreceiptDTO2.setVoucher(rs2.getString("dcv_voucher_no"));
				dcreceiptDTO2.setAppNo(rs2.getString("dcv_application_no"));
				dcreceiptDTO2.setNic(rs2.getString("dcv_id_no"));
				dcreceiptDTO2.setTrainingTYpeDes(rs2.getString("description"));
				dcreceiptDTO2.setReceiptNo(rs2.getString("dcv_receipt_no"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs2);
			ConnectionManager.close(ps2);
			ConnectionManager.close(con);
		}
		return dcreceiptDTO2;

	}

	@Override
	public List<DriverConductorRegistrationDTO> getGenerateReceiptListForDriverConductor(
			DriverConductorRegistrationDTO dcDTo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		

		List<DriverConductorRegistrationDTO> returnReceiptDetList = new ArrayList<DriverConductorRegistrationDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select dcv_training_type,dcv_voucher_no,dcv_application_no,dcv_driver_conduc_id_no,dcv_receipt_no,\r\n"
					+ "dcv_total_amount,a.description,dcv_approved_status\r\n"
					+ "from public.nt_m_driver_conduc_payment_voucher\r\n"
					+ "inner join public.nt_r_training_type a on a.code=nt_m_driver_conduc_payment_voucher.dcv_training_type\r\n"
					+ "where a.status='A'\r\n" + "and dcv_voucher_no=?\r\n" + "and dcv_application_no=? order by dcv_voucher_approve_reject_date desc ";

			ps = con.prepareStatement(query);
			ps.setString(1, dcDTo.getVoucher());
			ps.setString(2, dcDTo.getAppNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				DriverConductorRegistrationDTO voucherDTO = new DriverConductorRegistrationDTO();
				voucherDTO.setVoucher(rs.getString("dcv_voucher_no"));
				voucherDTO.setTrainingTYpeDes(rs.getString("description"));
				voucherDTO.setTrainingTypeCode(rs.getString("dcv_training_type"));
				voucherDTO.setAppNo(rs.getString("dcv_application_no"));
				voucherDTO.setDriverConductorId(rs.getString("dcv_driver_conduc_id_no"));
				voucherDTO.setAppNo(rs.getString("dcv_application_no"));
				voucherDTO.setReceiptNo(rs.getString("dcv_receipt_no"));
				if (rs.getString("dcv_approved_status").equalsIgnoreCase("A")) {
					voucherDTO.setApproveStatus("Approved");
				} else if (rs.getString("dcv_approved_status").equalsIgnoreCase("R")) {
					voucherDTO.setApproveStatus("Reject");
				} else {
					voucherDTO.setApproveStatus("Pending");
				}
				returnReceiptDetList.add(voucherDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return returnReceiptDetList;

	}

	@Override
	public boolean isReceiptgeneratedForDriverConducctor(String voucherNo) {
		boolean isReceiptgenerated = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT  rec_is_receipt_generated " + "FROM public.nt_t_driver_conductor_receipt "
					+ "WHERE rec_voucher_no=? LIMIT 1;";

			ps = con.prepareStatement(query);
			ps.setString(1, voucherNo);
			rs = ps.executeQuery();

			if (rs.next() && rs.getString("rec_is_receipt_generated").equals("Y")) {
				isReceiptgenerated = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isReceiptgenerated;
	}

	@Override
	public boolean isPrintCompletedForDriverConductor(String voucherNo) {
		boolean isPrintCompleted = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT  dcv_isprint " + "FROM public.nt_m_driver_conduc_payment_voucher "
					+ "WHERE dcv_voucher_no  =? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, voucherNo);
			rs = ps.executeQuery();

			if (rs.next()) {
				String isPrint = rs.getString("dcv_isprint");
				if (isPrint != null) {
					if (isPrint.equals("Y")) {
						isPrintCompleted = true;
					} else {
						isPrintCompleted = false;
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
		return isPrintCompleted;
	}

	@Override
	public List<DriverConductorRegistrationDTO> getVoucherDetailsListForDriverCOnductor(String voucherNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<DriverConductorRegistrationDTO> data = new ArrayList<DriverConductorRegistrationDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT nt_r_charge_type.code as chargeCode, nt_r_charge_type.description as codeDescription,\r\n"
					+ "nt_t_driver_conduc_payment_voucher.dcvd_account_no as vodAccount, \r\n"
					+ "nt_t_driver_conduc_payment_voucher.dcvd_amount as vodAmount \r\n"
					+ "FROM public.nt_t_driver_conduc_payment_voucher\r\n"
					+ "inner join nt_r_charge_type on public.nt_t_driver_conduc_payment_voucher.dcvd_charge_type= nt_r_charge_type.code \r\n"
					+ "WHERE dcvd_voucher_no=?order by nt_r_charge_type.description";

			ps = con.prepareStatement(query);
			ps.setString(1, voucherNo);
			rs = ps.executeQuery();

			DriverConductorRegistrationDTO p;

			while (rs.next()) {
				p = new DriverConductorRegistrationDTO();
				p.setChargeTypeCode(rs.getString("chargeCode"));
				p.setChargeTypeDes(rs.getString("codeDescription"));
				p.setAmmount(rs.getBigDecimal("vodAmount"));
				p.setAccountNO(rs.getString("vodAccount"));

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
	public void saveReceiptForDriverCOnductor(DriverConductorRegistrationDTO selectedRow, String loginUser,
			String statusType, String status, String appNo) {

		String receiptNo = generateReceiptNoForDriverConductor();
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		try {

			con = ConnectionManager.getConnection();
			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_driver_conductor_receipt");
			String query = "INSERT INTO public.nt_t_driver_conductor_receipt\r\n"
					+ "(rec_seq_no, rec_training_type, rec_driver_conductor_id,  rec_application_no,\r\n"
					+ "rec_voucher_no, rec_payment_mode, rec_is_diposit_to_branch, rec_receipt_no, rec_back_code,\r\n"
					+ "rec_branch_code, rec_cheque_no, rec_total_fee, rec_remarks, rec_is_receipt_generated, rec_re_printed_no, \r\n"
					+ "rep_created_by, rep_created_date)\r\n"
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

			ps = con.prepareStatement(query);

			ps.setLong(1, seqNo);
			ps.setString(2, selectedRow.getTrainingTypeCode());
			ps.setString(3, selectedRow.getDriverConductorId());

			ps.setString(4, selectedRow.getAppNo());
			ps.setString(5, selectedRow.getVoucher());
			ps.setString(6, selectedRow.getPaymentModeCodeForDC());
			String depositToBank = selectedRow.getDepositToBankForDC() ? "Y" : "N";

			ps.setString(7, depositToBank);
			ps.setString(8, receiptNo);
			ps.setString(9, selectedRow.getBankCodeForDC());
			ps.setString(10, selectedRow.getBranchCodeForDC());
			ps.setString(11, selectedRow.getChequeOrBankReceipt());
			ps.setBigDecimal(12, selectedRow.getTotalAmount());
			ps.setString(13, selectedRow.getReceiptRemarks());
			ps.setString(14, "Y");
			ps.setString(15, "TEST");
			ps.setString(16, loginUser);
			ps.setTimestamp(17, timestamp);

			ps.executeUpdate();

			// update nt_m_payment_voucher table
			String query2 = "UPDATE public.nt_m_driver_conduc_payment_voucher " + "SET dcv_receipt_no ='" + receiptNo
					+ "',dcv_isprint ='Y' " + "WHERE dcv_voucher_no =?";
			ps2 = con.prepareStatement(query2);
			ps2.setString(1, selectedRow.getVoucher());
			ps2.executeUpdate();
			
			//update nt_t_driver_conductor registration table
			
			String sql3 = "UPDATE public.nt_t_driver_conductor_registration SET dcr_status_type =? , dcr_status = ? WHERE dcr_app_no  =? ";

			ps3 = con.prepareStatement(sql3);

			ps3.setString(1, statusType);
			ps3.setString(2, status);
			ps3.setString(3, appNo);

			ps3.executeUpdate();
			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(con);
		}

	}

	public String generateReceiptNoForDriverConductor() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;

		int year = Year.now().getValue();
		String currYear = Integer.toString(year).substring(2, 4);
		String strRecNo = "";
		String lastyr = "00";

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT rec_receipt_no FROM public.nt_t_driver_conductor_receipt ORDER BY rep_created_date desc  LIMIT 1";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("rec_receipt_no");
			}

			if (result != null) {

				lastyr = result.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String ApprecordcountN = "00001";
					strRecNo = "RDC" + currYear + ApprecordcountN;
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
					strRecNo = "RDC" + currYear + ApprecordcountN;
				}
			} else {
				strRecNo = "RDC" + currYear + "00001";
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return strRecNo;
	}

	@Override
	public DriverConductorRegistrationDTO getReceiptDetailsForDriverConductor(String voucherNo) {
		DriverConductorRegistrationDTO receiptDetails = new DriverConductorRegistrationDTO();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps2 = null;
		ResultSet rs2 = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT rec_payment_mode, rec_back_code, rec_branch_code, rec_is_diposit_to_branch, rec_cheque_no, rec_remarks "
					+ "FROM public.nt_t_driver_conductor_receipt " + "WHERE rec_voucher_no=? LIMIT 1;";

			ps = con.prepareStatement(query);
			ps.setString(1, voucherNo);
			rs = ps.executeQuery();

			if (rs.next()) {
				receiptDetails.setPaymentModeCodeForDC(rs.getString("rec_payment_mode"));
				if (rs.getString("rec_back_code") != null) {
					receiptDetails.setBankCodeForDC(rs.getString("rec_back_code"));
					receiptDetails.setBranchCodeForDC(rs.getString("rec_branch_code"));

					// get bank and branch description
					String query2 = "SELECT b.description as bankDes, c.description as branchDes "
							+ "FROM public.nt_t_driver_conductor_receipt r "
							+ "INNER JOIN nt_r_bank b on b.code= r.rec_back_code "
							+ "INNER JOIN nt_r_branch c on c.code= r.rec_branch_code " + "WHERE rec_voucher_no=? "
							+ "LIMIT 1;";
					ps2 = con.prepareStatement(query2);
					ps2.setString(1, voucherNo);
					rs2 = ps2.executeQuery();
					if (rs2.next()) {
						receiptDetails.setBankDesForDC(rs2.getString("bankDes"));
						receiptDetails.setBranchDesForDC(rs2.getString("branchDes"));
					}
				}
				if (rs.getString("rec_is_diposit_to_branch").equals("Y")) {
					receiptDetails.setDepositToBankForDC(true);
				} else if (rs.getString("rec_is_diposit_to_branch").equals("N")) {
					receiptDetails.setDepositToBankForDC(false);
				}
				receiptDetails.setChequeOrBankReceipt(rs.getString("rec_cheque_no"));
				receiptDetails.setReceiptRemarks(rs.getString("rec_remarks"));
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
		return receiptDetails;
	}

	@Override
	public String getReceiptNoForPrintForDriverConductor(String voucherNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String receiptNoForPrint = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT dcv_receipt_no FROM public.nt_m_driver_conduc_payment_voucher WHERE dcv_voucher_no = ?;";

			ps = con.prepareStatement(query);
			ps.setString(1, voucherNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				receiptNoForPrint = rs.getString("dcv_receipt_no");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return receiptNoForPrint;
	}

	@Override
	public List<DriverConductorRegistrationDTO> getpendingReceiptListForDriverConductor() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<DriverConductorRegistrationDTO> returnReceiptDetList = new ArrayList<DriverConductorRegistrationDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select dcv_training_type,dcv_voucher_no,dcv_application_no,dcv_driver_conduc_id_no,dcv_receipt_no,\r\n"
					+ "dcv_total_amount,a.description,dcv_approved_status\r\n"
					+ "from public.nt_m_driver_conduc_payment_voucher\r\n"
					+ "inner join public.nt_r_training_type a on a.code=nt_m_driver_conduc_payment_voucher.dcv_training_type\r\n"
					+ "where a.status='A' and  public.nt_m_driver_conduc_payment_voucher.dcv_approved_status='A'\r\n"
					+ "and public.nt_m_driver_conduc_payment_voucher.dcv_isprint is null or public.nt_m_driver_conduc_payment_voucher.dcv_isprint='' order by dcv_voucher_no desc ";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				DriverConductorRegistrationDTO voucherDTO = new DriverConductorRegistrationDTO();
				voucherDTO.setVoucher(rs.getString("dcv_voucher_no"));
				voucherDTO.setTrainingTYpeDes(rs.getString("description"));
				voucherDTO.setTrainingTypeCode(rs.getString("dcv_training_type"));
				voucherDTO.setAppNo(rs.getString("dcv_application_no"));
				voucherDTO.setDriverConductorId(rs.getString("dcv_driver_conduc_id_no"));
				voucherDTO.setAppNo(rs.getString("dcv_application_no"));
				voucherDTO.setReceiptNo(rs.getString("dcv_receipt_no"));
				if (rs.getString("dcv_approved_status").equalsIgnoreCase("A")) {
					voucherDTO.setApproveStatus("Approved");
				} else if (rs.getString("dcv_approved_status").equalsIgnoreCase("R")) {
					voucherDTO.setApproveStatus("Reject");
				} else {
					voucherDTO.setApproveStatus("Pending");
				}
				returnReceiptDetList.add(voucherDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return returnReceiptDetList;

	}

}
