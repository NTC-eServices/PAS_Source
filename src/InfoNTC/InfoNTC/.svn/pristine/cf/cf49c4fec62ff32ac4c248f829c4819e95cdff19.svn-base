/**
 * 
 */
package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lk.informatics.ntc.model.dto.SimRegistrationDTO;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.roc.utils.common.Utils;

/**
 * @author viraj.k
 *
 */
public class SimRegEditServiceImpl implements SimRegEditService {

	@Override
	public List<SimRegistrationDTO> getSIMDetails() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		SimRegistrationDTO simRegistrationDTO;
		List<SimRegistrationDTO> simRegistrationDTOs = new ArrayList<>();

		try {
			con = ConnectionManager.getConnection();

			String query = null;

			query = " SELECT sim_reg_no , sim_permit_no , sim_bus_no "
					+ " FROM public.nt_m_sim_registration ORDER BY sim_reg_no ;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				simRegistrationDTO = new SimRegistrationDTO();

				simRegistrationDTO.setSimRegNo(rs.getString("sim_reg_no"));
				simRegistrationDTO.setPermitNo(rs.getString("sim_permit_no"));
				simRegistrationDTO.setBusNo(rs.getString("sim_bus_no"));

				simRegistrationDTOs.add(simRegistrationDTO);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return simRegistrationDTOs;

	}

	@Override
	public List<Object> getSearchedDTO(SimRegistrationDTO keyDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		SimRegistrationDTO simRegistrationDTO = null;

		List<Object> searchedItemArray = new ArrayList<>();

		try {
			con = ConnectionManager.getConnection();

			String query = null;

			String sim = keyDTO.getSimRegNo();
			String permit = keyDTO.getPermitNo();
			String bus = keyDTO.getBusNo();

			// add SIM registration details

			query = " SELECT * " + " FROM public.nt_m_sim_registration ";

			if (!keyDTO.getSimRegNo().isEmpty()) {
				query = query + "WHERE sim_reg_no = '" + sim + "' ";
				if (!keyDTO.getPermitNo().isEmpty()) {
					query = query + "AND sim_permit_no = '" + permit + "' ";
				}
				if (!keyDTO.getBusNo().isEmpty()) {
					query = query + "AND sim_bus_no = '" + bus + "' ";
				}
			} else if (!keyDTO.getPermitNo().isEmpty()) {
				query = query + "WHERE sim_permit_no = '" + permit + "' ";
				if (!keyDTO.getBusNo().isEmpty()) {
					query = query + "AND sim_bus_no = '" + bus + "' ";
				}
			} else {
				query = query + "WHERE sim_bus_no = '" + bus + "' ";
			}

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			if (!rs.next()) {
				return null;
			} else {

				simRegistrationDTO = new SimRegistrationDTO();

				simRegistrationDTO.setSimRegNo(rs.getString("sim_reg_no"));
				simRegistrationDTO.setServiceCategory(rs.getString("sim_service_cat"));
				simRegistrationDTO.setPermitNo(rs.getString("sim_permit_no"));
				simRegistrationDTO.setBusNo(rs.getString("sim_bus_no"));
				simRegistrationDTO.setSimNo(rs.getString("sim_no"));
				simRegistrationDTO.setReceiversName(rs.getString("sim_receiver_name"));
				simRegistrationDTO.setNicNo(rs.getString("sim_nic_no"));
				simRegistrationDTO.setIssueDate(rs.getTimestamp("sim_issue_date"));
				simRegistrationDTO.setValidUntilDate(rs.getTimestamp("sim_valid_until"));
				simRegistrationDTO.setRemarks(rs.getString("sim_remarks"));
				simRegistrationDTO.setPaymentReceiptNo(rs.getString("sim_receipt_no"));
				simRegistrationDTO.setEmiNo(rs.getString("sim_emi_no"));
				simRegistrationDTO.setSimStatusType(rs.getString("sim_status_type"));

				searchedItemArray.add(simRegistrationDTO);// index 0
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return searchedItemArray;
	}

	@Override
	public List<SimRegistrationDTO> getEmiListBySimNo(String simRegNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		SimRegistrationDTO emiDTO;
		List<SimRegistrationDTO> emiDTOList = new ArrayList<>();

		try {
			con = ConnectionManager.getConnection();

			String query = null;

			query = " SELECT emi_bus_no, emi_status , emi_re_issue_date ,emi_no" + " FROM public.nt_t_emi_det "
					+ " WHERE emi_sim_reg_no = ? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, simRegNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				emiDTO = new SimRegistrationDTO();

				emiDTO.setEmiBusNo(rs.getString("emi_bus_no"));
				emiDTO.setEmiStatus(rs.getString("emi_status"));
				emiDTO.setEmiReIssueDate(rs.getTimestamp("emi_re_issue_date"));
				emiDTO.setEmiNo(rs.getString("emi_no"));

				emiDTOList.add(emiDTO);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return emiDTOList;
	}

	@Override
	public SimRegistrationDTO getVoucherDetailsBySimNo(String simRegNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		SimRegistrationDTO voucherDTO = null;

		try {
			con = ConnectionManager.getConnection();

			String query = null;

			query = " SELECT spv_voucher_no , spv_total_amount , spv_receipt_no , spv_approved_status , spv_seq_no ,spv_isprint "
					+ " FROM public.nt_m_sim_payment_voucher "
					+ " WHERE spv_approved_status not in ('VC','R')and  spv_sim_no = '" + simRegNo + "';";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				voucherDTO = new SimRegistrationDTO();

				voucherDTO.setVouNo(rs.getString("spv_voucher_no"));
				voucherDTO.setVouTotalAmount(rs.getDouble("spv_total_amount"));
				voucherDTO.setReceiptNo(rs.getString("spv_receipt_no"));
				voucherDTO.setVoucherApprovedStatus(rs.getString("spv_approved_status"));
				voucherDTO.setVoucherSeqNo(rs.getString("spv_seq_no"));
				voucherDTO.setVouPrint(rs.getString("spv_isprint"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return voucherDTO;
	}

	@Override
	public List<SimRegistrationDTO> getChargeDetailsByVoucherSeq(String vouSeq) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		SimRegistrationDTO chargeDto;
		List<SimRegistrationDTO> chargeList = new ArrayList<>();

		try {
			con = ConnectionManager.getConnection();

			String query = null;

			query = " SELECT vod_seq_no, vod_charge_type , vod_account_no , vod_amount "
					+ " FROM public.nt_t_sim_voucher_details " + " WHERE vod_payment_vou_seq = ? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, vouSeq);
			rs = ps.executeQuery();

			while (rs.next()) {
				chargeDto = new SimRegistrationDTO();

				String chargeDesc = getChargeTypeDescByCode(rs.getString("vod_charge_type"));

				chargeDto.setVouChargeType(chargeDesc);
				chargeDto.setVouAccountNo(rs.getString("vod_account_no"));
				chargeDto.setVouAmmount(rs.getDouble("vod_amount"));

				chargeList.add(chargeDto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return chargeList;
	}

	@Override
	public String getChargeTypeDescByCode(String code) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String desc = null;

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT code, description FROM nt_r_charge_type WHERE code = ? ;";
			ps = con.prepareStatement(query);
			ps.setString(1, code);
			rs = ps.executeQuery();

			while (rs.next()) {
				desc = rs.getString("description");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return desc;
	}

	@Override
	public void deleteVoucher(String vouSeq) {

	}

	@Override
	public List<SimRegistrationDTO> getApprovedSIMDetails() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		SimRegistrationDTO simRegistrationDTO;
		List<SimRegistrationDTO> simRegistrationDTOs = new ArrayList<>();

		try {
			con = ConnectionManager.getConnection();

			String query = null;

			query = " SELECT sim_reg_no , sim_permit_no , sim_bus_no " + " FROM public.nt_m_sim_registration "
					+ "	WHERE sim_status = 'A' OR sim_status_type = 'RW' " + " ORDER BY sim_reg_no ;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				simRegistrationDTO = new SimRegistrationDTO();

				simRegistrationDTO.setSimRegNo(rs.getString("sim_reg_no"));
				simRegistrationDTO.setPermitNo(rs.getString("sim_permit_no"));
				simRegistrationDTO.setBusNo(rs.getString("sim_bus_no"));

				simRegistrationDTOs.add(simRegistrationDTO);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return simRegistrationDTOs;
	}

	@Override
	public String saveSimRenewal(SimRegistrationDTO simRegistrationDTO, List<SimRegistrationDTO> emiDtoList) {

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		PreparedStatement ps4 = null;
		PreparedStatement ps5 = null;
		PreparedStatement ps6 = null;
		PreparedStatement ps7 = null;
		PreparedStatement ps8 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;

		// Generate
		PreparedStatement ps_count = null;
		ResultSet rs_count = null;
		String format = null;
		long number = 0;
		String generatedCode = null;

		try {
			con = ConnectionManager.getConnection();

			// get EMI Details
			String q1 = "SELECT seq , emi_bus_no , emi_status, emi_re_issue_date , emi_created_by, emi_created_date, emi_modify_by, emi_modify_date,emi_no  "
					+ " FROM public.nt_t_emi_det WHERE emi_sim_reg_no = '" + simRegistrationDTO.getSimRegNo() + "' ";
			ps = con.prepareStatement(q1);
			rs = ps.executeQuery();

			while (rs.next()) {

				// 1. Insert EMI details to History table
				String query = "INSERT INTO public.nt_h_emi_det ( seq, emi_sim_reg_no , emi_bus_no , emi_status , emi_re_issue_date, emi_created_by, emi_created_date, emi_modify_by, emi_modify_date,emi_no) VALUES (?,?,?,?,?,?,?,?,?,?) ; "; // 9
				ps1 = con.prepareStatement(query);
				ps1.setLong(1, rs.getLong("seq"));
				ps1.setString(2, simRegistrationDTO.getSimRegNo());
				ps1.setString(3, rs.getString("emi_bus_no"));
				ps1.setString(4, rs.getString("emi_status"));
				ps1.setTimestamp(5, rs.getTimestamp("emi_re_issue_date"));
				ps1.setString(6, rs.getString("emi_created_by"));
				ps1.setTimestamp(7, rs.getTimestamp("emi_created_date"));
				ps1.setString(8, rs.getString("emi_modify_by"));
				ps1.setTimestamp(9, rs.getTimestamp("emi_modify_date"));
				ps1.setString(10, rs.getString("emi_no"));
				ps1.executeUpdate();
			}

			// 2. Delete EMI Details
			String q2 = "DELETE FROM public.nt_t_emi_det WHERE emi_sim_reg_no = '" + simRegistrationDTO.getSimRegNo()
					+ "' ";
			ps2 = con.prepareStatement(q2);
			ps2.executeUpdate();

			// get SIM Registration Details
			String q3 = "SELECT * FROM public.nt_m_sim_registration WHERE sim_reg_no = '"
					+ simRegistrationDTO.getSimRegNo() + "' ";
			ps3 = con.prepareStatement(q3);
			rs1 = ps3.executeQuery();

			while (rs1.next()) {

				// 3. Insert SIM Registration details to History table
				String q4 = "INSERT INTO public.nt_h_sim_registration ( seq , sim_reg_no , sim_service_cat , sim_permit_no , "
						+ " sim_bus_no , sim_no , sim_receiver_name , sim_nic_no , sim_issue_date , sim_valid_until , sim_remarks , "
						+ " sim_receipt_no , sim_status, sim_created_by , sim_created_date , sim_modify_by , sim_modify_date , sim_status_type,sim_emi_no) "
						+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ; "; // 19 , need EMI no
				ps4 = con.prepareStatement(q4);
				ps4.setLong(1, rs1.getLong("seq"));
				ps4.setString(2, rs1.getString("sim_reg_no"));
				ps4.setString(3, rs1.getString("sim_service_cat"));
				ps4.setString(4, rs1.getString("sim_permit_no"));
				ps4.setString(5, rs1.getString("sim_bus_no"));
				ps4.setString(6, rs1.getString("sim_no"));
				ps4.setString(7, rs1.getString("sim_receiver_name"));
				ps4.setString(8, rs1.getString("sim_nic_no"));
				ps4.setTimestamp(9, rs1.getTimestamp("sim_issue_date"));
				ps4.setTimestamp(10, rs1.getTimestamp("sim_valid_until"));
				ps4.setString(11, rs1.getString("sim_remarks"));
				ps4.setString(12, rs1.getString("sim_receipt_no"));
				ps4.setString(13, rs1.getString("sim_status"));
				ps4.setString(14, rs1.getString("sim_created_by"));
				ps4.setTimestamp(15, rs1.getTimestamp("sim_created_date"));
				ps4.setString(16, rs1.getString("sim_modify_by"));
				ps4.setTimestamp(17, rs1.getTimestamp("sim_modify_date"));
				ps4.setString(18, rs1.getString("sim_status_type"));
				ps4.setString(19, rs1.getString("sim_emi_no"));
				ps4.executeUpdate();
			}

			// 4. Delete SIM REG Details
			String q5 = "DELETE FROM nt_m_sim_registration WHERE sim_reg_no = '" + simRegistrationDTO.getSimRegNo()
					+ "' ";
			ps5 = con.prepareStatement(q5);
			ps5.executeUpdate();

			// 5. Generate SIM Number

			String query1 = "SELECT string_value, type FROM nt_r_parameters WHERE param_name = ?;";
			String param = "SIM_REGISTRATION_NUMBER_VALUE";

			ps6 = con.prepareStatement(query1);
			ps6.setString(1, param);
			rs2 = ps6.executeQuery();

			while (rs2.next()) {
				format = rs2.getString("string_value");
				number = rs2.getLong("type");
			}

			// Number FORMAT -> SIM-20-01-0001 (CN-year-month-SIM REG Number)
			// SIM REG Number should be start with 0001 in every new month

			DateFormat dfYear = new SimpleDateFormat("yy");
			String currntYear = dfYear.format(Calendar.getInstance().getTime());

			DateFormat dfMonth = new SimpleDateFormat("MM");
			String currntYearMonth = dfMonth.format(Calendar.getInstance().getTime());

			// check if it is the first SIM reg Number for the month
			String query2 = "SELECT COUNT(*) FROM public.nt_m_sim_registration WHERE sim_reg_no LIKE ? ;";
			ps_count = con.prepareStatement(query2);
			ps_count.setString(1, format + currntYear + currntYearMonth + "%");
			rs_count = ps_count.executeQuery();
			int recCount = 0;
			while (rs_count.next()) {
				recCount = rs_count.getInt(1);
			}

			// Generate number
			if (format != null) {
				if (recCount > 0)
					number += 1;
				else
					number = 0001;

				generatedCode = format + currntYear + currntYearMonth + String.format("%04d", number);

			}

			// set generatedSimNo to DTO
			simRegistrationDTO.setSimRegNo(generatedCode);

			// 6. Insert SIM REG Details

			// add SIM
			String query3;

			Timestamp issueDate, validUntilDate, createdDate;
			
			/*change is done request based on the Damith & Pramitha via call on 11/10/2021*/
			issueDate = new Timestamp(simRegistrationDTO.getIssueDate().getTime());
			//issueDate = new Timestamp(simRegistrationDTO.getValidUntilDate().getTime());
			/*---End Change---*/
			
			/**
			 * for prevent update issue date by validUntilDate req by Damith 202/07/09
			 **/
			validUntilDate = new Timestamp(simRegistrationDTO.getRenewalUntilDate().getTime());
			/**
			 * for prevent update valid until date by renewalUntilDate req by Damith
			 * 202/07/09
			 **/
			createdDate = new Timestamp(simRegistrationDTO.getSimCreatedDate().getTime());

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_sim_registration");

			String simStatus = "P";
			String simStatusType = "RW";

			// Added By VlaDimir on 31-03-2020
			query3 = "INSERT INTO public.nt_m_sim_registration "
					+ " (sim_reg_no , sim_service_cat , sim_permit_no , sim_bus_no , sim_no , sim_receiver_name ,"
					+ " sim_nic_no , sim_issue_date , sim_valid_until , sim_remarks , sim_created_by , sim_created_date , sim_status , seq , sim_status_type, sim_emi_no ) "
					+ " VALUES ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?, ?); ";
			ps7 = con.prepareStatement(query3);
			ps7.setString(1, simRegistrationDTO.getSimRegNo());
			ps7.setString(2, simRegistrationDTO.getServiceCategory());
			ps7.setString(3, simRegistrationDTO.getPermitNo());
			ps7.setString(4, simRegistrationDTO.getBusNo());
			ps7.setString(5, simRegistrationDTO.getSimNo());
			ps7.setString(6, simRegistrationDTO.getReceiversName());
			ps7.setString(7, simRegistrationDTO.getNicNo());
			ps7.setTimestamp(8, issueDate);
			ps7.setTimestamp(9, validUntilDate);
			ps7.setString(10, simRegistrationDTO.getRemarks());
			ps7.setString(11, simRegistrationDTO.getSimCreatedBy());
			ps7.setTimestamp(12, createdDate);
			ps7.setString(13, simStatus);
			ps7.setLong(14, seqNo);
			ps7.setString(15, simStatusType);
			ps7.setString(16, simRegistrationDTO.getEmiNo());
			ps7.executeUpdate();

			// set Generated key
			String query4;
			String param1 = "SIM_REGISTRATION_NUMBER_VALUE";

			String input = simRegistrationDTO.getSimRegNo();
			String lastFourDigits = input.substring(input.length() - 4);

			query4 = "UPDATE nt_r_parameters SET type = ? WHERE param_name = ?; ";
			ps8 = con.prepareStatement(query4);
			ps8.setString(1, String.valueOf(lastFourDigits));
			ps8.setString(2, param1);
			ps8.executeUpdate();

			// 7. Insert EMI Details

			String query5;

			Timestamp reIssueDate;

			for (SimRegistrationDTO emiDTO : emiDtoList) {

				reIssueDate = new Timestamp(emiDTO.getEmiReIssueDate().getTime());

				String status;
				if (emiDTO.getEmiStatus().equals("Active")) {
					status = "A";
				} else {
					status = "I";
				}

				long emiSeqNo = Utils.getNextValBySeqName(con, "seq_nt_t_emi_det");

				// Added By VlaDimir on 31-03-2020
				query5 = "INSERT INTO public.nt_t_emi_det ( emi_sim_reg_no , emi_bus_no, emi_status , emi_re_issue_date , emi_created_by , emi_created_date , seq ,emi_no )"
						+ "VALUES ( ? , ? , ? , ? , ? , ? , ?,? ) ; ";
				ps = con.prepareStatement(query5);
				ps.setString(1, simRegistrationDTO.getSimRegNo());
				ps.setString(2, emiDTO.getEmiBusNo());
				ps.setString(3, status);
				ps.setTimestamp(4, reIssueDate);
				ps.setString(5, simRegistrationDTO.getSimCreatedBy());
				ps.setTimestamp(6, createdDate);
				ps.setLong(7, emiSeqNo);
				ps.setString(8, emiDTO.getEmiNo());
				ps.executeUpdate();

			}

			con.commit();
			// End Modification

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return generatedCode;

	}

	@Override
	public void updateSimRenewal(SimRegistrationDTO simRegistrationDTO) {

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionManager.getConnection();

			String query;

			Timestamp issueDate, validUntilDate, modifiedDate;

			issueDate = new Timestamp(simRegistrationDTO.getIssueDate().getTime());
			validUntilDate = new Timestamp(simRegistrationDTO.getRenewalUntilDate().getTime());

			modifiedDate = new Timestamp(simRegistrationDTO.getSimModifiedDate().getTime());

			query = " UPDATE public.nt_m_sim_registration "
					+ " SET sim_issue_date = ? , sim_valid_until = ? , sim_remarks = ? , sim_modify_by = ? , sim_modify_date = ? , sim_emi_no = ? "
					+ " WHERE sim_reg_no = ? "; // 7
			ps = con.prepareStatement(query);
			ps.setTimestamp(1, issueDate);
			ps.setTimestamp(2, validUntilDate);
			ps.setString(3, simRegistrationDTO.getRemarks());
			ps.setString(4, simRegistrationDTO.getSimModifiedBy());
			ps.setTimestamp(5, modifiedDate);
			ps.setString(6, simRegistrationDTO.getEmiNo());
			ps.setString(7, simRegistrationDTO.getSimRegNo());

			ps.executeUpdate();

			con.commit();
			// End Modification

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

	}

	@Override
	public String updateSimRegStatus(String simRegNo, String status, String user) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql3 = "UPDATE public.nt_m_sim_registration SET sim_status_type =?, sim_modify_by =?,sim_modify_date  =? WHERE  sim_reg_no =? ";

			ps = con.prepareStatement(sql3);

			ps.setString(1, status);
			ps.setString(2, user);
			ps.setTimestamp(3, timestamp);
			ps.setString(4, simRegNo);

			ps.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return status;

	}

	@Override
	public List<SimRegistrationDTO> getSIMDetailsBySimStatus(String status) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		SimRegistrationDTO simRegistrationDTO;
		List<SimRegistrationDTO> simRegistrationDTOs = new ArrayList<>();

		try {
			con = ConnectionManager.getConnection();

			String query = null;

			query = " SELECT sim_reg_no , sim_permit_no , sim_bus_no "
					+ " FROM public.nt_m_sim_registration WHERE sim_status = '" + status + "' ORDER BY sim_reg_no ;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				simRegistrationDTO = new SimRegistrationDTO();

				simRegistrationDTO.setSimRegNo(rs.getString("sim_reg_no"));
				simRegistrationDTO.setPermitNo(rs.getString("sim_permit_no"));
				simRegistrationDTO.setBusNo(rs.getString("sim_bus_no"));

				simRegistrationDTOs.add(simRegistrationDTO);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return simRegistrationDTOs;
	}

	@Override
	public boolean cancelVoucher(String voucherNo, String voucherStatus, String reason, String loginUser,
			String simRegNo, String simRegStatus) {

		boolean returnVal = false;

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql3 = "UPDATE public.nt_m_sim_payment_voucher SET spv_approved_status =?,spv_reject_reason =?, spv_modified_by =?,spv_modified_date =?, spv_approved_date=? "
					+ "WHERE spv_voucher_no =? ";

			ps = con.prepareStatement(sql3);

			ps.setString(1, voucherStatus);
			ps.setString(2, reason);
			ps.setString(3, loginUser);
			ps.setTimestamp(4, timestamp);
			ps.setTimestamp(5, timestamp);
			ps.setString(6, voucherNo);
			
			ps.executeUpdate();
			
			String sql = "UPDATE public.nt_m_sim_registration SET sim_status_type =?, sim_modify_by =?,sim_modify_date  =? WHERE  sim_reg_no =? ";

			ps2 = con.prepareStatement(sql);

			ps2.setString(1, simRegStatus);
			ps2.setString(2, loginUser);
			ps2.setTimestamp(3, timestamp);
			ps2.setString(4, simRegNo);
			
			ps2.executeUpdate();

			con.commit();
			returnVal = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(con);
		}
		return returnVal;

	}

}
