package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.informatics.ntc.model.dto.SubSidyDTO;
import lk.informatics.ntc.view.util.ConnectionManager;

public class SubSidyManagementServiceImpl implements SubSidyManagementService {

	@Override
	public List<SubSidyDTO> getServiceType() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SubSidyDTO> returnList = new ArrayList<SubSidyDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct st_code,st_description from nt_r_subsidy_service_type "
					+ "order by st_description";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				SubSidyDTO dto = new SubSidyDTO();
				dto.setServiceType(rs.getString("st_description"));
				dto.setServiceCode(rs.getString("st_code"));

				returnList.add(dto);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return returnList;
	}

	@Override
	public List<SubSidyDTO> getApprovedVoucherNo(String servicecode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SubSidyDTO> returnList = new ArrayList<SubSidyDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct sbp_voucher_no , sbp_subsidy_type from public.nt_m_sm_bulk_payment where sbp_is_dg_approval='A'  and sbp_subsidy_type=? order by sbp_voucher_no desc;";

			ps = con.prepareStatement(query);
			ps.setString(1, servicecode);
			rs = ps.executeQuery();

			while (rs.next()) {
				SubSidyDTO dto = new SubSidyDTO();
				dto.setVoucherNo(rs.getString("sbp_voucher_no"));

				returnList.add(dto);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return returnList;
	}

	// added for subsidy payment approval
	@Override
	public List<SubSidyDTO> getVoucherNoList(String selectedSubsidyService) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SubSidyDTO> showVoucherNoList = new ArrayList<SubSidyDTO>();

		try {
			con = ConnectionManager.getConnection();
			String query = "select sbp_voucher_no from public.nt_m_sm_bulk_payment where sbp_voucher_no is not null and sbp_subsidy_type=? order by sbp_voucher_no desc;";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedSubsidyService);
			rs = ps.executeQuery();

			while (rs.next()) {
				SubSidyDTO voucherNoDTO = new SubSidyDTO();
				voucherNoDTO.setVoucherNo(rs.getString("sbp_voucher_no"));

				showVoucherNoList.add(voucherNoDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return showVoucherNoList;
	}

	@Override
	public List<SubSidyDTO> getPaymentListForUpdating(SubSidyDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SubSidyDTO> paymentUpdateList = new ArrayList<SubSidyDTO>();
		String WHERE_SQL = "";
		boolean whereadded = false;

		if (dto.getVoucherNo() != null && !dto.getVoucherNo().equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE sbp_voucher_no = " + "'" + dto.getVoucherNo() + "' ";
			whereadded = true;
		}

		if (dto.getVoucherStartDate() != null && dto.getVoucherEndDate() != null) {
			Timestamp startDate = new Timestamp(dto.getVoucherStartDate().getTime());
			Timestamp endDate = new Timestamp(dto.getVoucherEndDate().getTime());
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + "and sbp_created_date >= " + "' " + startDate + "' and sbp_created_date <= "
						+ "'" + endDate + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + "WHERE sbp_created_date >= " + "' " + startDate + "' and sbp_created_date <= "
						+ "'" + endDate + "' and sbp_is_dg_approval='A' ";
				whereadded = true;
			}
		} else if (dto.getVoucherEndDate() != null && dto.getVoucherStartDate() == null) {

			Timestamp endDate = new Timestamp(dto.getVoucherEndDate().getTime());
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + "and sbp_created_date <= " + "'" + endDate + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE  sbp_created_date <= " + "'" + endDate
						+ "' and sbp_is_dg_approval='A' ";
				whereadded = true;
			}
		} else if (dto.getVoucherStartDate() != null && dto.getVoucherEndDate() == null) {
			Timestamp startDate = new Timestamp(dto.getVoucherStartDate().getTime());

			if (whereadded) {
				WHERE_SQL = WHERE_SQL + "and sbp_created_date >= " + "'" + startDate + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE sbp_created_date >= " + "'" + startDate
						+ "' and sbp_is_dg_approval='A' ";
				whereadded = true;
			}
		}

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select distinct nt_t_sm_bulk_payment.sbpt_service_ref_no,sbpt_bulk_payment_m_seq,sbp_payment_ref,sbp_voucher_amt, sbp_voucher_no, sbp_tot_no_of_logs, sbp_tot_amt, sbp_is_dg_approval, sbp_created_date "
					+ "FROM public.nt_m_sm_bulk_payment "
					+ "inner join public.nt_t_sm_bulk_payment on nt_t_sm_bulk_payment.sbpt_voucher_no = nt_m_sm_bulk_payment.sbp_voucher_no "
					+ WHERE_SQL;

			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();
			SubSidyDTO e;

			while (rs.next()) {
				e = new SubSidyDTO();
				e.setBulkSeq(rs.getInt("sbpt_bulk_payment_m_seq"));
				e.setPaymentRefNo(rs.getString("sbp_payment_ref"));
				e.setVoucherNo(rs.getString("sbp_voucher_no"));
				e.setReferenceNo(rs.getString("sbpt_service_ref_no"));
				Timestamp ts = rs.getTimestamp("sbp_created_date");

				if (ts != null) {
					Date date = new Date();
					date.setTime(ts.getTime());
					String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
					e.setTransationDateString(formattedDate);
				} else {

					e.setTransationDateString("N/A");
				}

				e.setNoOfLog(rs.getInt("sbp_tot_no_of_logs"));
				e.setTotalAmount(rs.getBigDecimal("sbp_voucher_amt"));

				String approveStatusCode = rs.getString("sbp_is_dg_approval");
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

				e.setStatus(approveStatus);

				paymentUpdateList.add(e);

			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return paymentUpdateList;
	}

	@Override
	public List<SubSidyDTO> SisuPaymentData(Date date1, Date date2, String voucherNo, String serviceType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SubSidyDTO> sisuSeriyaPayementList = new ArrayList<SubSidyDTO>();
		String WHERE_SQL = "";

		if (date1 != null && date2 != null && voucherNo != null && !voucherNo.trim().isEmpty()) {
			WHERE_SQL = WHERE_SQL + " and sbp_created_date  BETWEEN  " + "'" + date1 + "'" + "AND" + "'" + date2 + "'"
					+ "AND sbp_voucher_no  =  " + "'" + voucherNo + "'";

		} else if (date1 != null && date2 != null) {
			WHERE_SQL = WHERE_SQL + " and sbp_created_date  BETWEEN  " + "'" + date1 + "'" + "AND" + "'" + date2 + "'";

		} else if (voucherNo != null && !voucherNo.trim().isEmpty()) {

			WHERE_SQL = WHERE_SQL + " and sbp_voucher_no  =  " + "'" + voucherNo + "'";

		}

		try {

			con = ConnectionManager.getConnection();

			String query2 = "select * from public.nt_m_sm_bulk_payment where sbp_subsidy_type =? " + WHERE_SQL;

			ps = con.prepareStatement(query2);
			ps.setString(1, serviceType);
			rs = ps.executeQuery();
			SubSidyDTO e;

			while (rs.next()) {
				e = new SubSidyDTO();
				e.setVoucherNo(rs.getString("sbp_voucher_no"));
				e.setNoOfLogs(rs.getInt("sbp_tot_no_of_logs"));
				e.setTotAmount(rs.getInt("sbp_tot_amt"));
				Timestamp ts = rs.getTimestamp("sbp_created_date");
				Date date = new Date();

				if (ts != null) {
					date.setTime(ts.getTime());
					String formattedDate = new SimpleDateFormat("yyyy/MM/dd").format(date);
					e.setTransationDateString(formattedDate);
				} else {
					e.setTransationDateString("N/A");
				}
				if (rs.getString("sbp_is_dg_approval") == null) {
					e.setCertifyStatus("Ongoing");
				} else if (rs.getString("sbp_is_dg_approval").equalsIgnoreCase("A")) {
					e.setCertifyStatus("Completed");

				} else {
					e.setCertifyStatus("Ongoing");
				}
				if (rs.getString("sbp_is_certify") == null) {
					e.setCertifyTic("false");
				} else if (rs.getString("sbp_is_certify").equalsIgnoreCase("A")) {
					e.setCertifyTic("true");

				} else {
					e.setCertifyTic("false");
				}
				if (rs.getString("sbp_is_recommonded") == null) {
					e.setRecomndedTic("false");
				} else if (rs.getString("sbp_is_recommonded").equalsIgnoreCase("A")) {
					e.setRecomndedTic("true");

				} else {
					e.setRecomndedTic("false");
				}
				if (rs.getString("sbp_is_direcor_approval") == null) {
					e.setDirectorApprovalTic("false");
				} else if (rs.getString("sbp_is_direcor_approval").equalsIgnoreCase("A")) {
					e.setDirectorApprovalTic("true");

				} else {
					e.setDirectorApprovalTic("false");
				}
				if (rs.getString("sbp_is_dg_approval") == null) {
					e.setDgApprovalTic("false");
				} else if (rs.getString("sbp_is_dg_approval").equalsIgnoreCase("A")) {
					e.setDgApprovalTic("true");

				} else {
					e.setDgApprovalTic("false");
				}
				e.setBulkSeq(rs.getLong("sbp_seq"));
				e.setHoldingVal(rs.getString("sbp_holding"));
				e.setVoucherAmount(rs.getBigDecimal("sbp_voucher_amt"));

				sisuSeriyaPayementList.add(e);
			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return sisuSeriyaPayementList;
	}

	@Override
	public List<SubSidyDTO> getApprovedServiceRefNoList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SubSidyDTO> data = new ArrayList<SubSidyDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct sps_service_ref_no " + " from public.nt_m_sisu_permit_hol_service_info "
					+ " WHERE sps_service_ref_no is not null and sps_service_ref_no !='' and sps_status='A' "
					+ " and sps_renewal_status not in ('I')   and  (sps_service_agreement_issuedby IS NULL or sps_permit_sticker_issuedby IS NULL or sps_issue_logsheets_issuedby IS null) "
					+ "order by sps_service_ref_no desc ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			SubSidyDTO s;

			while (rs.next()) {
				s = new SubSidyDTO();
				s.setServiceRefNo(rs.getString("sps_service_ref_no"));
				data.add(s);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return data;
	}

	@Override
	public List<SubSidyDTO> getDefaultPaymentListForUpdating() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SubSidyDTO> paymentUpdateList = new ArrayList<SubSidyDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select distinct nt_t_sm_bulk_payment.sbpt_service_ref_no,sbpt_bulk_payment_m_seq,sbp_payment_ref,sbp_voucher_amt, sbp_voucher_no, sbp_tot_no_of_logs, sbp_tot_amt, sbp_is_dg_approval, sbp_created_date "
					+ "FROM public.nt_m_sm_bulk_payment "
					+ "inner join public.nt_t_sm_bulk_payment on nt_t_sm_bulk_payment.sbpt_voucher_no = nt_m_sm_bulk_payment.sbp_voucher_no "
					+ "where sbp_is_dg_approval='A' and sbp_voucher_status is null";

			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();
			SubSidyDTO e;

			while (rs.next()) {
				e = new SubSidyDTO();
				e.setBulkSeq(rs.getInt("sbpt_bulk_payment_m_seq"));
				e.setPaymentRefNo(rs.getString("sbp_payment_ref"));
				e.setVoucherNo(rs.getString("sbp_voucher_no"));
				e.setReferenceNo(rs.getString("sbpt_service_ref_no"));
				Timestamp ts = rs.getTimestamp("sbp_created_date");

				if (ts != null) {
					Date date = new Date();
					date.setTime(ts.getTime());
					String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
					e.setTransationDateString(formattedDate);
				} else {

					e.setTransationDateString("N/A");
				}

				e.setNoOfLog(rs.getInt("sbp_tot_no_of_logs"));
				e.setTotalAmount(rs.getBigDecimal("sbp_voucher_amt"));

				String approveStatusCode = rs.getString("sbp_is_dg_approval");
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

				e.setStatus(approveStatus);

				paymentUpdateList.add(e);

			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return paymentUpdateList;
	}

	@Override
	public List<SubSidyDTO> getSelectedPaymentListForUpdating(SubSidyDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SubSidyDTO> paymentUpdateList = new ArrayList<SubSidyDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT sbp_service_ref_no, sbp_voucher_no, sbp_tot_no_of_logs, sbp_tot_amt, sbp_is_dg_approval, sbp_created_date "
					+ "FROM public.nt_m_sm_bulk_payment where sbp_voucher_no=? ";

			ps = con.prepareStatement(query2);
			ps.setString(1, dto.getVoucherNo());
			rs = ps.executeQuery();
			SubSidyDTO e;

			while (rs.next()) {
				e = new SubSidyDTO();

				e.setVoucherNo(rs.getString("sbp_voucher_no"));
				e.setReferenceNo(rs.getString("sbp_service_ref_no"));
				Timestamp ts = rs.getTimestamp("sbp_created_date");

				if (ts != null) {
					Date date = new Date();
					date.setTime(ts.getTime());
					String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
					e.setTransationDateString(formattedDate);
				} else {

					e.setTransationDateString("N/A");
				}

				e.setNoOfLog(rs.getInt("sbp_tot_no_of_logs"));
				e.setTotalAmount(rs.getBigDecimal("sbp_tot_amt"));

				String approveStatusCode = rs.getString("sbp_is_dg_approval");
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

				e.setStatus(approveStatus);

				paymentUpdateList.add(e);

			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return paymentUpdateList;
	}

	@Override
	public boolean updateReferenceNo(String oldValue, String newValue, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		boolean isUpdate = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_m_sm_bulk_payment "
					+ "SET sbp_payment_ref=?, sbp_voucher_status='C', sbp_modified_by=?, sbp_modified_date=?  WHERE sbp_voucher_no=? ; ";

			ps = con.prepareStatement(query);

			ps.setString(1, newValue);
			ps.setString(2, user);
			ps.setTimestamp(3, timestamp);
			ps.setString(4, oldValue);

			int a = ps.executeUpdate();

			if (a > 0) {
				isUpdate = true;
			} else {
				isUpdate = false;
			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return isUpdate;
	}

	@Override
	public boolean isFoundReferenceNo(String oldValue) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isFound = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "select lss_log_ref_no from public.nt_m_log_sheet_summary where lss_log_ref_no=? ";

			ps = con.prepareStatement(query);
			ps.setString(1, oldValue);
			rs = ps.executeQuery();

			if (rs.next()) {
				isFound = true;
			} else {
				isFound = false;
			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return isFound;
	}

	@Override
	public String updateCertifyApproval(SubSidyDTO dto, String loginUser) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			java.util.Date date1 = new java.util.Date();
			Timestamp timestamp = new Timestamp(date1.getTime());

			String sql = "UPDATE public.nt_m_sm_bulk_payment\r\n" + "SET  sbp_is_certify=? ,sbp_is_certify_date=?, \r\n"
					+ "sbp_modified_date=?,sbp_modified_by=?\r\n" + "WHERE sbp_voucher_no=?;";

			try {
				stmt = con.prepareStatement(sql);

				stmt.setString(1, "A");
				stmt.setTimestamp(2, timestamp);
				stmt.setTimestamp(3, timestamp);
				stmt.setString(4, loginUser);
				stmt.setString(5, dto.getVoucherNo());

				stmt.executeUpdate();

				if (stmt != null) {
					stmt.close();
				}
				con.commit();
				return "Success";
			}catch (SQLException e) {
				con.rollback();
				e.printStackTrace();
				return null;
			}
			

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		
	}

	@Override
	public String updateRecomendedApproval(SubSidyDTO dto, String loginUser) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			java.util.Date date1 = new java.util.Date();
			Timestamp timestamp = new Timestamp(date1.getTime());

			String sql = "UPDATE public.nt_m_sm_bulk_payment\r\n"
					+ "SET sbp_is_recommonded=?, sbp_is_recommonded_date=?,\r\n"
					+ "sbp_modified_date=?,sbp_modified_by=?\r\n" + "WHERE sbp_voucher_no=?;";

			try {
				stmt = con.prepareStatement(sql);

				stmt.setString(1, "A");
				stmt.setTimestamp(2, timestamp);

				stmt.setTimestamp(3, timestamp);
				stmt.setString(4, loginUser);
				stmt.setString(5, dto.getVoucherNo());

				stmt.executeUpdate();

				con.commit();
				return "success";
			}catch (SQLException e){
				con.rollback();
				return null;
			}
			

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

	}

	@Override
	public String updateDirectorApproval(SubSidyDTO dto, String loginUser) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			java.util.Date date1 = new java.util.Date();
			Timestamp timestamp = new Timestamp(date1.getTime());

			String sql = "UPDATE public.nt_m_sm_bulk_payment\r\n"
					+ "SET  sbp_is_direcor_approval=?, sbp_is_direcor_approval_date=?,\r\n"
					+ "sbp_modified_date=?,sbp_modified_by=?\r\n" + "WHERE sbp_voucher_no=?;";

			try {
				stmt = con.prepareStatement(sql);

				stmt.setString(1, "A");

				stmt.setTimestamp(2, timestamp);
				stmt.setTimestamp(3, timestamp);
				stmt.setString(4, loginUser);
				stmt.setString(5, dto.getVoucherNo());

				stmt.executeUpdate();

				con.commit();

				return "success";
			} catch (SQLException e) {
				con.rollback();
				return null;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

	}

	@Override
	public String updateDGApproval(SubSidyDTO dto, String loginUser) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			java.util.Date date1 = new java.util.Date();
			Timestamp timestamp = new Timestamp(date1.getTime());

			String sql = "UPDATE public.nt_m_sm_bulk_payment\r\n"
					+ "SET  sbp_is_dg_approval=?, sbp_is_dg_approval_date=?,\r\n"
					+ "sbp_modified_date=?,sbp_modified_by=?\r\n" + "WHERE sbp_voucher_no=?;";

			try {
				stmt = con.prepareStatement(sql);

				stmt.setString(1, "A");
				stmt.setTimestamp(2, timestamp);
				stmt.setTimestamp(3, timestamp);
				stmt.setString(4, loginUser);
				stmt.setString(5, dto.getVoucherNo());

				stmt.executeUpdate();

				con.commit();
				return "success";
			} catch (SQLException e) {
				con.rollback();
				return null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);

		}
	}

	@Override
	public SubSidyDTO getApprovalStatus(String voucherNO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		SubSidyDTO approveStatusDTO = new SubSidyDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT sbp_is_certify, sbp_is_recommonded, sbp_is_direcor_approval, sbp_is_dg_approval\r\n"
					+ "FROM public.nt_m_sm_bulk_payment\r\n" + "where sbp_voucher_no=? ;; ";

			ps = con.prepareStatement(query);
			ps.setString(1, voucherNO);
			rs = ps.executeQuery();

			while (rs.next()) {
				if (rs.getString("sbp_is_certify") != null && !rs.getString("sbp_is_certify").trim().isEmpty()) {
					approveStatusDTO.setCertifyTic("A");
				} else {

					approveStatusDTO.setCertifyTic("NA");
				}
				if (rs.getString("sbp_is_recommonded") != null
						&& !rs.getString("sbp_is_recommonded").trim().isEmpty()) {
					approveStatusDTO.setRecomndedTic("A");
				} else {

					approveStatusDTO.setRecomndedTic("NA");
				}
				if (rs.getString("sbp_is_direcor_approval") != null
						&& !rs.getString("sbp_is_direcor_approval").trim().isEmpty()) {
					approveStatusDTO.setDirectorApprovalTic("A");
				} else {

					approveStatusDTO.setDirectorApprovalTic("NA");
				}
				if (rs.getString("sbp_is_dg_approval") != null
						&& !rs.getString("sbp_is_dg_approval").trim().isEmpty()) {
					approveStatusDTO.setDgApprovalTic("A");
				} else {

					approveStatusDTO.setDgApprovalTic("NA");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return approveStatusDTO;
	}

	public List<SubSidyDTO> showLogSheetDet(String voucherNo, long seq) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SubSidyDTO> sisuSeriyaLogSheetDet = new ArrayList<SubSidyDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select a.lss_log_ref_no,a.lss_year,lss_month,a.lss_school_req_running_days,a.lss_no_of_turns_gps,a.lss_total_length,a.lss_subsidy_amt,\r\n"
					+ "a.lss_late_fee,a.lss_penalty_fee,a.lss_service_no ,a.lss_paid_amt, a.lss_pending_amt\r\n"
					+ "from public.nt_m_log_sheet_summary a inner join public.nt_t_sm_bulk_payment b on b.sbpt_log_summary_seq=a.lss_seq where b.sbpt_bulk_payment_m_seq =?";

			ps = con.prepareStatement(query2);
			ps.setLong(1, seq);
			rs = ps.executeQuery();
			SubSidyDTO e;

			while (rs.next()) {
				e = new SubSidyDTO();
				e.setLogRefNo(rs.getString("lss_log_ref_no"));
				e.setYear(rs.getString("lss_year"));
				e.setMonth(rs.getString("lss_month"));
				e.setServiceNo(rs.getString("lss_service_no"));
				e.setSchoolDays(rs.getInt("lss_school_req_running_days"));
				e.setTurns(rs.getInt("lss_no_of_turns_gps"));
				e.setLength(rs.getInt("lss_total_length"));
				e.setSubsidyFee(rs.getInt("lss_subsidy_amt"));
				e.setLateFee(rs.getInt("lss_late_fee"));
				e.setPenaltyFee(rs.getInt("lss_penalty_fee"));
				e.setPaidAmmount(rs.getInt("lss_paid_amt"));
				e.setPendingFinalAmount(rs.getInt("lss_pending_amt"));
				sisuSeriyaLogSheetDet.add(e);
			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return sisuSeriyaLogSheetDet;
	}

	@Override
	public List<SubSidyDTO> getVoucherNoList(SubSidyDTO subSidyDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SubSidyDTO> voucherNoList = new ArrayList<SubSidyDTO>();

		try {

			String whereClose = "";
			if (subSidyDTO.getVouStartDate() != null) {

				if (subSidyDTO.getVouEndDate() != null) {
					whereClose = " where sbp_created_date >= '" + subSidyDTO.getVouStartDate()
							+ "' and sbp_created_date <= '" + subSidyDTO.getVouEndDate() + "' ";
				} else {

					whereClose = " where sbp_created_date >= '" + subSidyDTO.getVouStartDate() + "' ";
				}

			} else {
				whereClose = " where sbp_created_date <= '" + subSidyDTO.getVouEndDate() + "' ";
			}
			con = ConnectionManager.getConnection();

			String query = "SELECT sbp_voucher_no\r\n" + "FROM public.nt_m_sm_bulk_payment" + whereClose
					+ "and sbp_voucher_no is not null and sbp_voucher_no !='' order by sbp_voucher_no";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				SubSidyDTO ss = new SubSidyDTO();

				ss.setVoucherNo(rs.getString("sbp_voucher_no"));
				voucherNoList.add(ss);

			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return voucherNoList;
	}

	@Override
	public boolean updatePaymentDetails(String user, List<SubSidyDTO> list, String voucherNO) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		boolean isUpdated = true;

		try {
			con = ConnectionManager.getConnection();

			for (int a = 0; a < list.size(); a++) {

				String query = "SELECT sbpt_seq, sbpt_voucher_no, sbpt_log_summary_seq ,sbpt_voucher_amount FROM public.nt_t_sm_bulk_payment "
						+ "WHERE sbpt_log_summary_seq=? and sbpt_voucher_no=?";

				ps = con.prepareStatement(query);
				ps.setInt(1, Integer.parseInt(list.get(a).getLogSheetSummarySeq()));
				ps.setString(2, voucherNO);
				rs = ps.executeQuery();

				if (rs.next() == true) {

					/* Updating logsheet summary tables */

					String q = "update public.nt_m_log_sheet_summary SET lss_pending_amt = lss_pending_amt - ?, lss_paid_amt = lss_paid_amt + ?, lss_modified_by =? , lss_modified_date =?  "
							+ "where lss_seq=? ";

					try {
						ps2 = con.prepareStatement(q);
						ps2.setBigDecimal(1, rs.getBigDecimal("sbpt_voucher_amount"));
						ps2.setBigDecimal(2, rs.getBigDecimal("sbpt_voucher_amount"));
						ps2.setString(3, user);
						ps2.setTimestamp(4, timestamp);
						ps2.setInt(5, Integer.parseInt(list.get(a).getLogSheetSummarySeq()));

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
							isUpdated = true;
						} else {
							isUpdated = false;
						}
					} catch (SQLException e) {
						con.rollback();
						return false;
					}
					

				} else {
					isUpdated = false;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(ps2);
			ConnectionManager.close(con);
		}

		return isUpdated;
	}

	@Override
	public List<SubSidyDTO> getLogSheetSeq(String voucherNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SubSidyDTO> logsheetseqlist = new ArrayList<SubSidyDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select sbpt_voucher_no,sbpt_log_summary_seq from nt_t_sm_bulk_payment where sbpt_voucher_no=? ";

			ps = con.prepareStatement(query2);
			ps.setString(1, voucherNo);
			rs = ps.executeQuery();
			SubSidyDTO e;

			while (rs.next()) {
				e = new SubSidyDTO();
				e.setLogSheetSummarySeq(rs.getString("sbpt_log_summary_seq"));
				e.setVoucherNo(rs.getString("sbpt_voucher_no"));

				logsheetseqlist.add(e);

			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return logsheetseqlist;
	}

	@Override
	public List<SubSidyDTO> getVoucherNoListNew(SubSidyDTO subSidyDTO, String serviceType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SubSidyDTO> voucherNoList = new ArrayList<SubSidyDTO>();

		try {

			String whereClose = "";
			if (subSidyDTO.getVouStartDate() != null) {

				if (subSidyDTO.getVouEndDate() != null) {
					whereClose = " where sbp_created_date >= '" + subSidyDTO.getVouStartDate()
							+ "' and sbp_created_date <= '" + subSidyDTO.getVouEndDate() + "' ";
				} else {

					whereClose = " where sbp_created_date >= '" + subSidyDTO.getVouStartDate() + "' ";
				}

			} else {
				whereClose = " where sbp_created_date <= '" + subSidyDTO.getVouEndDate() + "' ";
			}
			con = ConnectionManager.getConnection();

			String query = "SELECT sbp_voucher_no\r\n" + "FROM public.nt_m_sm_bulk_payment" + whereClose
					+ "and sbp_voucher_no is not null and sbp_voucher_no !='' and sbp_subsidy_type=? order by sbp_voucher_no";

			ps = con.prepareStatement(query);
			ps.setString(1, serviceType);
			rs = ps.executeQuery();

			while (rs.next()) {
				SubSidyDTO ss = new SubSidyDTO();

				ss.setVoucherNo(rs.getString("sbp_voucher_no"));
				voucherNoList.add(ss);

			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return voucherNoList;
	}

}
