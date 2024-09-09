package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedProperty;

import lk.informatics.ntc.model.dto.PaymentTypeMaintenanceDTO;
import lk.informatics.ntc.view.beans.SessionBackingBean;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.roc.utils.common.Utils;

public class PaymentTypeMaintenanceImpl implements PaymentTypeMaintenanceService {
	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	@Override
	public List<PaymentTypeMaintenanceDTO> getAllTranstractionTypeList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PaymentTypeMaintenanceDTO> returnList = new ArrayList<PaymentTypeMaintenanceDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description FROM nt_r_transaction_type WHERE active ='Y' order by description;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				PaymentTypeMaintenanceDTO paymentTypeMaintenanceDTO = new PaymentTypeMaintenanceDTO();
				paymentTypeMaintenanceDTO.setTranstractionTypeCode(rs.getString("code"));
				paymentTypeMaintenanceDTO.setTranstractionDescription(rs.getString("description"));
				returnList.add(paymentTypeMaintenanceDTO);

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
	public PaymentTypeMaintenanceDTO getTranstractionDescription(String selectedTranstractionType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		PaymentTypeMaintenanceDTO paymentTypeMaintenanceDTO = new PaymentTypeMaintenanceDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = " SELECT  description FROM nt_r_transaction_type WHERE code=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedTranstractionType);
			rs = ps.executeQuery();

			while (rs.next()) {
				paymentTypeMaintenanceDTO.setTranstractionDescription(rs.getString("description"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return paymentTypeMaintenanceDTO;
	}

	@Override
	public List<PaymentTypeMaintenanceDTO> getAllPaymentTypeList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PaymentTypeMaintenanceDTO> returnList = new ArrayList<PaymentTypeMaintenanceDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description FROM nt_r_charge_type WHERE active = 'A' order by description;";

			ps = con.prepareStatement(query);
			
			rs = ps.executeQuery();

			while (rs.next()) {
				PaymentTypeMaintenanceDTO paymentTypeMaintenanceDTO = new PaymentTypeMaintenanceDTO();
				paymentTypeMaintenanceDTO.setPaymentTypeCode(rs.getString("code"));
				paymentTypeMaintenanceDTO.setPaymentTypeDescription(rs.getString("description"));
				returnList.add(paymentTypeMaintenanceDTO);

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
	public PaymentTypeMaintenanceDTO getPaymentTypeDescription(String selectedChargeType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		PaymentTypeMaintenanceDTO paymentTypeMaintenanceDTO = new PaymentTypeMaintenanceDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT description FROM nt_r_charge_type WHERE code=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedChargeType);
			rs = ps.executeQuery();

			while (rs.next()) {
				paymentTypeMaintenanceDTO.setPaymentTypeDescription(rs.getString("description"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return paymentTypeMaintenanceDTO;
	}

	@Override
	public List<PaymentTypeMaintenanceDTO> getAllAccountNoList(String selectedTranstractionType,
			String selectedChargeType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PaymentTypeMaintenanceDTO> returnList = new ArrayList<PaymentTypeMaintenanceDTO>();
		
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT tct_account_no FROM public.nt_r_trn_charge_type where tct_charge_type_code=? and tct_trn_type_code=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedChargeType);
			ps.setString(2, selectedTranstractionType);
			rs = ps.executeQuery();

			while (rs.next()) {
				PaymentTypeMaintenanceDTO paymentTypeMaintenanceDTO = new PaymentTypeMaintenanceDTO();
				paymentTypeMaintenanceDTO.setAccountNo(rs.getString("tct_account_no"));
				returnList.add(paymentTypeMaintenanceDTO);
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
	public PaymentTypeMaintenanceDTO getCurrentAmount(String transtractionTypeCode, String paymentTypeCode,
			String accountNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		PaymentTypeMaintenanceDTO paymentTypeMaintenanceDTO = new PaymentTypeMaintenanceDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT tct_amount FROM public.nt_r_trn_charge_type where tct_charge_type_code=? and tct_trn_type_code=? and tct_account_no=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, paymentTypeCode);
			ps.setString(2, transtractionTypeCode);
			ps.setString(3, accountNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				paymentTypeMaintenanceDTO.setAmount(rs.getDouble("tct_amount"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return paymentTypeMaintenanceDTO;
	}

	@Override
	public PaymentTypeMaintenanceDTO getStatusDescription(String selectedStatus) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		PaymentTypeMaintenanceDTO paymentTypeMaintenanceDTO = new PaymentTypeMaintenanceDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT description FROM nt_r_rec_status WHERE code=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedStatus);
			rs = ps.executeQuery();

			while (rs.next()) {
				paymentTypeMaintenanceDTO.setStatusDescription(rs.getString("description"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return paymentTypeMaintenanceDTO;
	}

	@Override
	public List<PaymentTypeMaintenanceDTO> getAllStatusList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PaymentTypeMaintenanceDTO> returnList = new ArrayList<PaymentTypeMaintenanceDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description FROM nt_r_rec_status WHERE active = 'A' order by description;";

			ps = con.prepareStatement(query);
			
			rs = ps.executeQuery();

			while (rs.next()) {
				PaymentTypeMaintenanceDTO paymentTypeMaintenanceDTO = new PaymentTypeMaintenanceDTO();
				paymentTypeMaintenanceDTO.setStatusCode(rs.getString("code"));
				paymentTypeMaintenanceDTO.setStatusDescription(rs.getString("description"));
				returnList.add(paymentTypeMaintenanceDTO);

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
	public List<PaymentTypeMaintenanceDTO> getDisplaySearchDetailsWithTranstractionType(
			String selectedTranstractionType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PaymentTypeMaintenanceDTO> returnList = new ArrayList<PaymentTypeMaintenanceDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT a.tct_code as tctcode, a.tct_trn_type_code as transtractiontypecode, a.tct_charge_type_code as chargetypecode, a.tct_account_no as accountno, a.tct_amount as tctamount,b.description as transtractiontypedes,c.description as chargetypedes,d.description as statusdes, d.code as statuscode FROM public.nt_r_trn_charge_type a,public.nt_r_transaction_type b,public.nt_r_charge_type c,public.nt_r_rec_status d where a.tct_trn_type_code=b.code and a.tct_charge_type_code=c.code and a.tct_status=d.code and a.tct_trn_type_code=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedTranstractionType);
			rs = ps.executeQuery();

			while (rs.next()) {
				PaymentTypeMaintenanceDTO paymentTypeMaintenanceDTO = new PaymentTypeMaintenanceDTO();
				paymentTypeMaintenanceDTO.setTectCode(rs.getString("tctcode"));
				paymentTypeMaintenanceDTO.setTranstractionTypeCode(rs.getString("transtractiontypecode"));
				paymentTypeMaintenanceDTO.setPaymentTypeCode(rs.getString("chargetypecode"));
				paymentTypeMaintenanceDTO.setAccountNo(rs.getString("accountno"));
				paymentTypeMaintenanceDTO.setAmount(rs.getDouble("tctamount"));
				paymentTypeMaintenanceDTO.setTranstractionDescription(rs.getString("transtractiontypedes"));
				paymentTypeMaintenanceDTO.setPaymentTypeDescription(rs.getString("chargetypedes"));
				paymentTypeMaintenanceDTO.setStatusDescription(rs.getString("statusdes"));
				paymentTypeMaintenanceDTO.setStatusCode(rs.getString("statuscode"));
				returnList.add(paymentTypeMaintenanceDTO);

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
	public List<PaymentTypeMaintenanceDTO> getDisplayRecordsTransAndCharge(String selectedTranstractionType,
			String selectedChargeType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PaymentTypeMaintenanceDTO> returnList = new ArrayList<PaymentTypeMaintenanceDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT a.tct_code as tctcode, a.tct_trn_type_code as transtractiontypecode, a.tct_charge_type_code as chargetypecode, a.tct_account_no as accountno, a.tct_amount as tctamount,b.description as transtractiontypedes,c.description as chargetypedes,d.description as statusdes, d.code as statuscode FROM public.nt_r_trn_charge_type a,public.nt_r_transaction_type b,public.nt_r_charge_type c,public.nt_r_rec_status d where a.tct_trn_type_code=b.code and a.tct_charge_type_code=c.code and a.tct_status=d.code and a.tct_trn_type_code=? and a.tct_charge_type_code=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedTranstractionType);
			ps.setString(2, selectedChargeType);
			rs = ps.executeQuery();

			while (rs.next()) {
				PaymentTypeMaintenanceDTO paymentTypeMaintenanceDTO = new PaymentTypeMaintenanceDTO();
				paymentTypeMaintenanceDTO.setTectCode(rs.getString("tctcode"));
				paymentTypeMaintenanceDTO.setTranstractionTypeCode(rs.getString("transtractiontypecode"));
				paymentTypeMaintenanceDTO.setPaymentTypeCode(rs.getString("chargetypecode"));
				paymentTypeMaintenanceDTO.setAccountNo(rs.getString("accountno"));
				paymentTypeMaintenanceDTO.setAmount(rs.getDouble("tctamount"));
				paymentTypeMaintenanceDTO.setTranstractionDescription(rs.getString("transtractiontypedes"));
				paymentTypeMaintenanceDTO.setPaymentTypeDescription(rs.getString("chargetypedes"));
				paymentTypeMaintenanceDTO.setStatusDescription(rs.getString("statusdes"));
				paymentTypeMaintenanceDTO.setStatusCode(rs.getString("statuscode"));
				returnList.add(paymentTypeMaintenanceDTO);

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
	public List<PaymentTypeMaintenanceDTO> getSearchRecordsWithStatus(String selectedStatus) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PaymentTypeMaintenanceDTO> returnList = new ArrayList<PaymentTypeMaintenanceDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT a.tct_code as tctcode, a.tct_trn_type_code as transtractiontypecode, a.tct_charge_type_code as chargetypecode, a.tct_account_no as accountno, a.tct_amount as tctamount,b.description as transtractiontypedes,c.description as chargetypedes,d.description as statusdes, d.code as statuscode FROM public.nt_r_trn_charge_type a,public.nt_r_transaction_type b,public.nt_r_charge_type c,public.nt_r_rec_status d where a.tct_trn_type_code=b.code and a.tct_charge_type_code=c.code and a.tct_status=d.code and a.tct_status=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedStatus);
			rs = ps.executeQuery();

			while (rs.next()) {
				PaymentTypeMaintenanceDTO paymentTypeMaintenanceDTO = new PaymentTypeMaintenanceDTO();
				paymentTypeMaintenanceDTO.setTectCode(rs.getString("tctcode"));
				paymentTypeMaintenanceDTO.setTranstractionTypeCode(rs.getString("transtractiontypecode"));
				paymentTypeMaintenanceDTO.setPaymentTypeCode(rs.getString("chargetypecode"));
				paymentTypeMaintenanceDTO.setAccountNo(rs.getString("accountno"));
				paymentTypeMaintenanceDTO.setAmount(rs.getDouble("tctamount"));
				paymentTypeMaintenanceDTO.setTranstractionDescription(rs.getString("transtractiontypedes"));
				paymentTypeMaintenanceDTO.setPaymentTypeDescription(rs.getString("chargetypedes"));
				paymentTypeMaintenanceDTO.setStatusDescription(rs.getString("statusdes"));
				paymentTypeMaintenanceDTO.setStatusCode(rs.getString("statuscode"));
				returnList.add(paymentTypeMaintenanceDTO);

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
	public List<PaymentTypeMaintenanceDTO> getSearchRecordsWithAll(String selectedTranstractionType,
			String selectedChargeType, String selectedAccountNo, Double displayAmount, String selectedStatus) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String WHERE_SQL = "";
		boolean whereadded = false;
		List<PaymentTypeMaintenanceDTO> returnList = new ArrayList<PaymentTypeMaintenanceDTO>();
		if (selectedStatus != null && !selectedStatus.equals("")) {
			WHERE_SQL = WHERE_SQL + " and a.tct_status=?";
			whereadded = true;
		} else {
			WHERE_SQL = WHERE_SQL + " ;";
			whereadded = false;
		}
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT a.tct_code as tctcode, a.tct_trn_type_code as transtractiontypecode, a.tct_charge_type_code as chargetypecode, a.tct_account_no as accountno, a.tct_amount as tctamount,b.description as transtractiontypedes,c.description as chargetypedes,d.description as statusdes, d.code as statuscode FROM public.nt_r_trn_charge_type a,public.nt_r_transaction_type b,public.nt_r_charge_type c,public.nt_r_rec_status d where a.tct_trn_type_code=b.code and a.tct_charge_type_code=c.code and a.tct_status=d.code and a.tct_trn_type_code=? and a.tct_charge_type_code=? and a.tct_account_no=? and a.tct_amount=? "
					+ WHERE_SQL;

			ps = con.prepareStatement(query);
			ps.setString(1, selectedTranstractionType);
			ps.setString(2, selectedChargeType);
			ps.setString(3, selectedAccountNo);
			ps.setDouble(4, displayAmount);
			if (whereadded == true) {
				ps.setString(5, selectedStatus);
			}
			rs = ps.executeQuery();

			while (rs.next()) {
				PaymentTypeMaintenanceDTO paymentTypeMaintenanceDTO = new PaymentTypeMaintenanceDTO();
				paymentTypeMaintenanceDTO.setTectCode(rs.getString("tctcode"));
				paymentTypeMaintenanceDTO.setTranstractionTypeCode(rs.getString("transtractiontypecode"));
				paymentTypeMaintenanceDTO.setPaymentTypeCode(rs.getString("chargetypecode"));
				paymentTypeMaintenanceDTO.setAccountNo(rs.getString("accountno"));
				paymentTypeMaintenanceDTO.setAmount(rs.getDouble("tctamount"));
				paymentTypeMaintenanceDTO.setTranstractionDescription(rs.getString("transtractiontypedes"));
				paymentTypeMaintenanceDTO.setPaymentTypeDescription(rs.getString("chargetypedes"));
				paymentTypeMaintenanceDTO.setStatusDescription(rs.getString("statusdes"));
				paymentTypeMaintenanceDTO.setStatusCode(rs.getString("statuscode"));
				returnList.add(paymentTypeMaintenanceDTO);

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
	public List<PaymentTypeMaintenanceDTO> getSearchRecordsWithTransAndStatus(String selectedTranstractionType,
			String selectedStatus) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PaymentTypeMaintenanceDTO> returnList = new ArrayList<PaymentTypeMaintenanceDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT a.tct_code as tctcode, a.tct_trn_type_code as transtractiontypecode, a.tct_charge_type_code as chargetypecode, a.tct_account_no as accountno, a.tct_amount as tctamount,b.description as transtractiontypedes,c.description as chargetypedes,d.description as statusdes, d.code as statuscode FROM public.nt_r_trn_charge_type a,public.nt_r_transaction_type b,public.nt_r_charge_type c,public.nt_r_rec_status d where a.tct_trn_type_code=b.code and a.tct_charge_type_code=c.code and a.tct_status=d.code and a.tct_trn_type_code=? and a.tct_status=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedTranstractionType);
			ps.setString(2, selectedStatus);
			rs = ps.executeQuery();

			while (rs.next()) {
				PaymentTypeMaintenanceDTO paymentTypeMaintenanceDTO = new PaymentTypeMaintenanceDTO();
				paymentTypeMaintenanceDTO.setTectCode(rs.getString("tctcode"));
				paymentTypeMaintenanceDTO.setTranstractionTypeCode(rs.getString("transtractiontypecode"));
				paymentTypeMaintenanceDTO.setPaymentTypeCode(rs.getString("chargetypecode"));
				paymentTypeMaintenanceDTO.setAccountNo(rs.getString("accountno"));
				paymentTypeMaintenanceDTO.setAmount(rs.getDouble("tctamount"));
				paymentTypeMaintenanceDTO.setTranstractionDescription(rs.getString("transtractiontypedes"));
				paymentTypeMaintenanceDTO.setPaymentTypeDescription(rs.getString("chargetypedes"));
				paymentTypeMaintenanceDTO.setStatusDescription(rs.getString("statusdes"));
				paymentTypeMaintenanceDTO.setStatusCode(rs.getString("statuscode"));
				returnList.add(paymentTypeMaintenanceDTO);

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
	public int updateTable(PaymentTypeMaintenanceDTO paymentTypeMaintenanceDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();
			String sql = "UPDATE public.nt_r_trn_charge_type SET tct_trn_type_code=?, tct_charge_type_code=?, tct_account_no=?, tct_amount=?, tct_status=?,  tct_modified_by=?, tct_modified_date=? WHERE tct_code=?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, paymentTypeMaintenanceDTO.getTranstractionTypeCode());
			stmt.setString(2, paymentTypeMaintenanceDTO.getPaymentTypeCode());
			stmt.setString(3, paymentTypeMaintenanceDTO.getAccountNo());
			stmt.setDouble(4, paymentTypeMaintenanceDTO.getAmount());
			stmt.setString(5, paymentTypeMaintenanceDTO.getStatusCode());
			stmt.setString(6, paymentTypeMaintenanceDTO.getModifyBy());
			stmt.setTimestamp(7, timestamp);
			stmt.setString(8, paymentTypeMaintenanceDTO.getTectCode());

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
	public int deleteRecord(String tectCode) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			con = ConnectionManager.getConnection();

			String sql = "DELETE FROM public.nt_r_trn_charge_type WHERE tct_code=?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, tectCode);
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
	public List<PaymentTypeMaintenanceDTO> getAllTctCodeList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PaymentTypeMaintenanceDTO> returnList = new ArrayList<PaymentTypeMaintenanceDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT tct_code FROM public.nt_r_trn_charge_type;";

			ps = con.prepareStatement(query);
			
			rs = ps.executeQuery();

			while (rs.next()) {
				PaymentTypeMaintenanceDTO paymentTypeMaintenanceDTO = new PaymentTypeMaintenanceDTO();
				paymentTypeMaintenanceDTO.setGeneratedTctCodes(rs.getString("tct_code"));
				returnList.add(paymentTypeMaintenanceDTO);
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
	public int insertNewRecordWithTechCode(PaymentTypeMaintenanceDTO paymentTypeMaintenanceDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();
			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_r_trn_charge_type");

			String sql = "INSERT INTO public.nt_r_trn_charge_type (tct_code, tct_trn_type_code, tct_charge_type_code, tct_account_no, tct_amount, tct_status, tct_created_by, tct_created_date, seq) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, paymentTypeMaintenanceDTO.getTectCode());
			stmt.setString(2, paymentTypeMaintenanceDTO.getTranstractionTypeCode());
			stmt.setString(3, paymentTypeMaintenanceDTO.getPaymentTypeCode());
			stmt.setString(4, paymentTypeMaintenanceDTO.getAccountNo());
			stmt.setDouble(5, paymentTypeMaintenanceDTO.getAmount());
			stmt.setString(6, paymentTypeMaintenanceDTO.getStatusCode());
			stmt.setString(7, paymentTypeMaintenanceDTO.getCreatedBy());
			stmt.setTimestamp(8, timestamp);
			stmt.setLong(9, seqNo);

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
	public boolean checkHaveFunction(String paymentTypeCode, String accountNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isChargeCodeActived = false;
		PaymentTypeMaintenanceDTO paymentTypeMaintenanceDTO = new PaymentTypeMaintenanceDTO();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT vod_seq_no, vod_account_no,vod_charge_code FROM public.nt_t_voucher_details where vod_charge_code=? and vod_account_no=?;";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, paymentTypeCode);
			stmt.setString(2, accountNo);

			rs = stmt.executeQuery();

			
			while (rs.next()) {
				paymentTypeMaintenanceDTO.setVoucherSeqNo(rs.getString("vod_seq_no"));
				paymentTypeMaintenanceDTO.setVoucherAccountNo(rs.getString("vod_account_no"));
				paymentTypeMaintenanceDTO.setVoucherChargeTypeCode(rs.getString("vod_charge_code"));
			}

			if (paymentTypeMaintenanceDTO.getVoucherChargeTypeCode().isEmpty()
					&& paymentTypeMaintenanceDTO.getVoucherSeqNo().isEmpty()
					&& paymentTypeMaintenanceDTO.getVoucherAccountNo().isEmpty()) {
				isChargeCodeActived = false;
			} else {
				isChargeCodeActived = true;
				
			}

		} catch (Exception e) {
			
			isChargeCodeActived = false;

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return isChargeCodeActived;
	}

	@Override
	public List<PaymentTypeMaintenanceDTO> getAllChargeTypeCodeList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PaymentTypeMaintenanceDTO> returnList = new ArrayList<PaymentTypeMaintenanceDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code,description FROM public.nt_r_charge_type;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				PaymentTypeMaintenanceDTO paymentTypeMaintenanceDTO = new PaymentTypeMaintenanceDTO();
				paymentTypeMaintenanceDTO.setGeneratedChargeTypeCodes(rs.getString("code"));
				paymentTypeMaintenanceDTO.setGeneratedChargeTypeCodeDes(rs.getString("description"));
				returnList.add(paymentTypeMaintenanceDTO);

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
	public int insertChargeType(PaymentTypeMaintenanceDTO paymentTypeMaintenanceDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "INSERT INTO public.nt_r_charge_type (code, description, active, created_by, created_date) VALUES(?,?, ?, ?, ?);";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, paymentTypeMaintenanceDTO.getPopUpChargeTypeCode());
			stmt.setString(2, paymentTypeMaintenanceDTO.getPopUpChargeTypeDescription());
			stmt.setString(3, paymentTypeMaintenanceDTO.getChargeTypeStatusCode());
			stmt.setString(4, paymentTypeMaintenanceDTO.getCreatedBy());
			stmt.setTimestamp(5, timestamp);
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
	public boolean checkHaveFunctionWithChargeTypeCode(String popUpChargeTypeCode) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isChargeCodeActived = false;
		PaymentTypeMaintenanceDTO paymentTypeMaintenanceDTO = new PaymentTypeMaintenanceDTO();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT tct_code,tct_charge_type_code FROM public.nt_r_trn_charge_type where tct_charge_type_code=?;";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, popUpChargeTypeCode);
			rs = stmt.executeQuery();

			while (rs.next()) {
				paymentTypeMaintenanceDTO.setTrnTectCode(rs.getString("tct_code"));
				paymentTypeMaintenanceDTO.setTrnChargeTypeCode(rs.getString("tct_charge_type_code"));
			}

			if (paymentTypeMaintenanceDTO.getTrnTectCode().isEmpty()
					&& paymentTypeMaintenanceDTO.getTrnChargeTypeCode().isEmpty()) {
				isChargeCodeActived = false;
			} else {
				isChargeCodeActived = true;

			}

		} catch (Exception e) {
			
			isChargeCodeActived = false;

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return isChargeCodeActived;
	}

	@Override
	public boolean checkHaveFunctionWithChargeTypeCodeOne(String popUpChargeTypeCode) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isChargeCodeActived = false;
		PaymentTypeMaintenanceDTO paymentTypeMaintenanceDTO = new PaymentTypeMaintenanceDTO();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT vod_seq_no, vod_charge_code FROM public.nt_t_voucher_details where vod_charge_code=?;";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, popUpChargeTypeCode);
			rs = stmt.executeQuery();

			
			while (rs.next()) {
				paymentTypeMaintenanceDTO.setVoucherSeqNo(rs.getString("vod_seq_no"));
				paymentTypeMaintenanceDTO.setVoucherChargeTypeCode(rs.getString("vod_charge_code"));
			}

			if (paymentTypeMaintenanceDTO.getVoucherSeqNo().isEmpty()
					&& paymentTypeMaintenanceDTO.getVoucherChargeTypeCode().isEmpty()) {
				isChargeCodeActived = false;
			} else {
				isChargeCodeActived = true;
				
			}

		} catch (Exception e) {
			
			isChargeCodeActived = false;

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return isChargeCodeActived;
	}

	@Override
	public int deleteChargeTypeRecord(String popUpChargeTypeCode) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			con = ConnectionManager.getConnection();

			String sql = "DELETE FROM public.nt_r_charge_type WHERE code=?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, popUpChargeTypeCode);
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
	public List<PaymentTypeMaintenanceDTO> getAllChargeTypesList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PaymentTypeMaintenanceDTO> returnList = new ArrayList<PaymentTypeMaintenanceDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description, active FROM public.nt_r_charge_type;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				PaymentTypeMaintenanceDTO paymentTypeMaintenanceDTO = new PaymentTypeMaintenanceDTO();
				paymentTypeMaintenanceDTO.setPopUpChargeTypeCode(rs.getString("code"));
				paymentTypeMaintenanceDTO.setPopUpChargeTypeDescription(rs.getString("description"));
				paymentTypeMaintenanceDTO.setStatusCode(rs.getString("active"));
				returnList.add(paymentTypeMaintenanceDTO);
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
	public int updateChargeType(PaymentTypeMaintenanceDTO paymentTypeMaintenanceDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_r_charge_type SET description=?, modified_by=?, modified_date=? WHERE code=?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, paymentTypeMaintenanceDTO.getPopUpChargeTypeDescription());
			stmt.setString(2, paymentTypeMaintenanceDTO.getModifyBy());
			stmt.setTimestamp(3, timestamp);
			stmt.setString(4, paymentTypeMaintenanceDTO.getPopUpChargeTypeCode());

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
	public List<PaymentTypeMaintenanceDTO> getAllChargeTypeCodesForCurrentTranstractionType(
			String selectedTranstractionType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PaymentTypeMaintenanceDTO> returnList = new ArrayList<PaymentTypeMaintenanceDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT   tct_charge_type_code FROM public.nt_r_trn_charge_type where tct_trn_type_code=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedTranstractionType);
			rs = ps.executeQuery();

			while (rs.next()) {
				PaymentTypeMaintenanceDTO paymentTypeMaintenanceDTO = new PaymentTypeMaintenanceDTO();
				paymentTypeMaintenanceDTO.setCheckingChargeTypeCodeListValue(rs.getString("tct_charge_type_code"));
				returnList.add(paymentTypeMaintenanceDTO);

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
}
