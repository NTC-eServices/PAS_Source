package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lk.informatics.ntc.model.dto.SimRegistrationDTO;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.roc.utils.common.Utils;

/**
 * @author viraj.k Modified By: dinushi.r , tharushi.e, thilina.d
 *
 */
public class SimRegistrationServiceImpl implements SimRegistrationService {

	@Override
	public String generateSimRegNo() {

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps_count = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;
		ResultSet rs_count = null;
		String format = null;
		long number = 0;
		String generatedCode = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT string_value, type FROM nt_r_parameters WHERE param_name = ?;";
			String param = "SIM_REGISTRATION_NUMBER_VALUE";

			ps = con.prepareStatement(query);
			ps.setString(1, param);
			rs = ps.executeQuery();

			while (rs.next()) {
				format = rs.getString("string_value");
				number = rs.getLong("type");
			}

			// Number FORMAT -> SIM-20-01-0001 (CN-year-month-SIM Reg Number)
			// SIM Reg Number should be start with 0001 in every new month

			DateFormat dfYear = new SimpleDateFormat("yy");
			String currntYear = dfYear.format(Calendar.getInstance().getTime());

			DateFormat dfMonth = new SimpleDateFormat("MM");
			String currntYearMonth = dfMonth.format(Calendar.getInstance().getTime());

			// check if it is the first SIM reg Number for the month
			query = "SELECT COUNT(*) FROM public.nt_m_sim_registration WHERE sim_reg_no LIKE ? ;";
			ps_count = con.prepareStatement(query);
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

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(rs);
			ConnectionManager.close(ps_count);
			ConnectionManager.close(rs_count);
			ConnectionManager.close(con);
		}
		return generatedCode;

	}

	@Override
	public void setGeneratedSimRegNo(String number) {
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionManager.getConnection();

			String query;
			String param = "SIM_REGISTRATION_NUMBER_VALUE";

			query = "UPDATE nt_r_parameters SET type = ? WHERE param_name = ?; ";
			ps = con.prepareStatement(query);
			ps.setString(1, String.valueOf(number));
			ps.setString(2, param);
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
	public List<SimRegistrationDTO> getPermitBusByService(String serviceType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		SimRegistrationDTO simRegistrationDTO = new SimRegistrationDTO();
		List<SimRegistrationDTO> simRegistrationDTOs = new ArrayList<>();

		try {
			con = ConnectionManager.getConnection();

			String query = null;

			// set Query for specific Service

			switch (serviceType) {
			case "ntc":
				query = "SELECT pm_permit_no,pm_vehicle_regno FROM public.nt_t_pm_application WHERE pm_status!='C' AND pm_status!='I' ;";
				break;

			case "sisu":
				query = "SELECT sps_permit_no,sps_bus_no FROM public.nt_m_sisu_permit_hol_service_info WHERE sps_status='A';";
				break;

			case "gami":
				query = "SELECT gps_permit_no_prpta,gps_bus_no FROM public.nt_h_gami_permit_hol_service_info WHERE gps_status='A';";
				break;

			case "nisi":
				query = "SELECT nri_permit_no,nri_bus_no FROM public.nt_m_nri_requester_info WHERE nri_status='A';";
				break;

			default:
				break;
			}

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				simRegistrationDTO.setPermitNo(rs.getString(1));
				simRegistrationDTO.setBusNo(rs.getString(2));

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
	public List<String> getPermitNoByService(String serviceType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<String> permitNoList = new ArrayList<>();

		try {
			con = ConnectionManager.getConnection();

			String query = null;

			// set Query for specific Service

			switch (serviceType) {
			case "ntc":
				query = "SELECT DISTINCT  pm_permit_no,pm_vehicle_regno FROM public.nt_t_pm_application WHERE pm_status!='C' AND pm_status!='I' AND pm_permit_no is not null;";
				break;

			case "sisu":
				query = "SELECT DISTINCT  sps_permit_no,sps_bus_no FROM public.nt_m_sisu_permit_hol_service_info WHERE sps_status='A'AND sps_permit_no is not null;";
				break;

			case "gami":
				query = "SELECT DISTINCT  gps_permit_no_prpta,gps_bus_no FROM public.nt_m_gami_permit_hol_service_info WHERE gps_status='A' AND gps_permit_no_prpta is not null;";
				break;

			case "nisi":
				query = "SELECT DISTINCT  nri_permit_no,nri_bus_no FROM public.nt_m_nri_requester_info WHERE nri_status='A'AND nri_permit_no is not null;";
				break;

			default:
				break;
			}

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				permitNoList.add(rs.getString(1));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return permitNoList;
	}

	@Override
	public List<String> getBusNoByService(String serviceType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<String> busNoList = new ArrayList<>();

		try {
			con = ConnectionManager.getConnection();

			String query = null;

			// set Query for specific Service

			switch (serviceType) {
			case "ntc":
				query = "SELECT DISTINCT  pm_permit_no,pm_vehicle_regno FROM public.nt_t_pm_application WHERE pm_status!='C' AND pm_status!='I' AND pm_vehicle_regno is not null ;";
				break;

			case "sisu":
				query = "SELECT DISTINCT  sps_permit_no,sps_bus_no FROM public.nt_m_sisu_permit_hol_service_info WHERE sps_status='A' AND sps_bus_no is not null;";
				break;

			case "gami":
				query = "SELECT DISTINCT  gps_permit_no_prpta,gps_bus_no FROM public.nt_m_gami_permit_hol_service_info WHERE gps_status='A' AND gps_bus_no is not null;";
				break;

			case "nisi":
				query = "SELECT DISTINCT  nri_permit_no,nri_bus_no FROM public.nt_m_nri_requester_info WHERE nri_status='A' AND nri_bus_no is not null;";
				break;

			default:
				break;
			}

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				busNoList.add(rs.getString(2));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return busNoList;
	}

	@Override
	public String getBusNoByPermitNo(String service, String permitNum) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String busNo = null;

		try {
			con = ConnectionManager.getConnection();

			String query = null;

			// set Query for specific Service

			switch (service) {
			case "ntc":
				query = "SELECT DISTINCT pm_vehicle_regno FROM public.nt_t_pm_application WHERE pm_permit_no = ? and pm_status='A' ;";
				break;

			case "sisu":
				query = "SELECT DISTINCT sps_bus_no FROM public.nt_m_sisu_permit_hol_service_info WHERE sps_permit_no = ? ;";
				break;

			case "gami":
				query = "SELECT DISTINCT gps_bus_no FROM public.nt_h_gami_permit_hol_service_info WHERE gps_permit_no_prpta = ? ;";
				break;

			case "nisi":
				query = "SELECT DISTINCT nri_bus_no FROM public.nt_m_nri_requester_info WHERE nri_permit_no = ? ;";
				break;

			default:
				break;
			}

			ps = con.prepareStatement(query);
			ps.setString(1, permitNum);
			rs = ps.executeQuery();

			while (rs.next()) {
				busNo = rs.getString(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return busNo;
	}

	@Override
	public String getPermitNoByBusNo(String service, String busNum) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String permitNo = null;

		try {
			con = ConnectionManager.getConnection();

			String query = null;

			// set Query for specific Service

			switch (service) {
			case "ntc":
				query = "SELECT DISTINCT pm_permit_no FROM public.nt_t_pm_application WHERE pm_vehicle_regno = ? ;";
				break;

			case "sisu":
				query = "SELECT DISTINCT sps_permit_no FROM public.nt_m_sisu_permit_hol_service_info WHERE sps_bus_no = ? ;";
				break;

			case "gami":
				query = "SELECT DISTINCT gps_permit_no_prpta FROM public.nt_h_gami_permit_hol_service_info WHERE gps_bus_no = ? ;";
				break;

			case "nisi":
				query = "SELECT DISTINCT nri_permit_no FROM public.nt_m_nri_requester_info WHERE nri_bus_no = ? ;";
				break;

			default:
				break;
			}

			ps = con.prepareStatement(query);
			ps.setString(1, busNum);
			rs = ps.executeQuery();

			while (rs.next()) {
				permitNo = rs.getString(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return permitNo;
	}

	@Override
	public void addSimRegistration(SimRegistrationDTO simRegistrationDTO) {

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;

		try {
			con = ConnectionManager.getConnection();

			// add Sim
			String query;

			Timestamp issueDate, validUntilDate, createdDate;

			issueDate = new Timestamp(simRegistrationDTO.getIssueDate().getTime());
			validUntilDate = new Timestamp(simRegistrationDTO.getValidUntilDate().getTime());
			createdDate = new Timestamp(simRegistrationDTO.getSimCreatedDate().getTime());

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_sim_registration");

			String simStatus = "P";
			String simStatusType = "R";

			query = "INSERT INTO public.nt_m_sim_registration "
					+ " (sim_reg_no , sim_service_cat , sim_permit_no , sim_bus_no , sim_no , sim_receiver_name ,"
					+ " sim_nic_no , sim_issue_date , sim_valid_until , sim_remarks , sim_created_by , sim_created_date , sim_status , seq , sim_status_type, sim_emi_no ) "
					+ " VALUES ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?, ?); ";
			ps = con.prepareStatement(query);
			ps.setString(1, simRegistrationDTO.getSimRegNo());
			ps.setString(2, simRegistrationDTO.getServiceCategory());
			ps.setString(3, simRegistrationDTO.getPermitNo());
			ps.setString(4, simRegistrationDTO.getBusNo());
			ps.setString(5, simRegistrationDTO.getSimNo());
			ps.setString(6, simRegistrationDTO.getReceiversName());
			ps.setString(7, simRegistrationDTO.getNicNo());
			ps.setTimestamp(8, issueDate);
			ps.setTimestamp(9, validUntilDate);
			ps.setString(10, simRegistrationDTO.getRemarks());
			ps.setString(11, simRegistrationDTO.getSimCreatedBy());
			ps.setTimestamp(12, createdDate);
			ps.setString(13, simStatus);
			ps.setLong(14, seqNo);
			ps.setString(15, simStatusType);
			ps.setString(16, simRegistrationDTO.getEmiNo());

			ps.executeUpdate();

			// set Generated key
			String query2;
			String param = "SIM_REGISTRATION_NUMBER_VALUE";

			String input = simRegistrationDTO.getSimRegNo();
			String lastFourDigits = input.substring(input.length() - 4);

			query2 = "UPDATE nt_r_parameters SET type = ? WHERE param_name = ?; ";
			ps2 = con.prepareStatement(query2);
			ps2.setString(1, String.valueOf(lastFourDigits));
			ps2.setString(2, param);

			ps2.executeUpdate();

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(con);
		}

	}

	@Override
	public void addEmi(SimRegistrationDTO emiDTO) {
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionManager.getConnection();

			String query;

			Timestamp reIssueDate, createdDate;

			reIssueDate = new Timestamp(emiDTO.getEmiReIssueDate().getTime());
			createdDate = new Timestamp(emiDTO.getSimCreatedDate().getTime());

			String status;
			if (emiDTO.getEmiStatus().equals("Active")) {
				status = "A";
			} else {
				status = "I";
			}

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_emi_det");

			query = "INSERT INTO public.nt_t_emi_det ( emi_sim_reg_no , emi_bus_no, emi_status , emi_re_issue_date , emi_created_by , emi_created_date , seq, emi_no  )"
					+ "VALUES ( ? , ? , ? , ? , ? , ? , ?,? ) ; ";
			ps = con.prepareStatement(query);
			ps.setString(1, emiDTO.getSimRegNo());
			ps.setString(2, emiDTO.getEmiBusNo());
			ps.setString(3, status);
			ps.setTimestamp(4, reIssueDate);
			ps.setString(5, emiDTO.getSimCreatedBy());
			ps.setTimestamp(6, createdDate);
			ps.setLong(7, seqNo);
			ps.setString(8, emiDTO.getEmiNo());
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
	public void updateSimRegistration(SimRegistrationDTO simRegistrationDTO) {
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionManager.getConnection();

			String query;

			Timestamp issueDate, validUntilDate, modifiedDate;

			issueDate = new Timestamp(simRegistrationDTO.getIssueDate().getTime());
			validUntilDate = new Timestamp(simRegistrationDTO.getValidUntilDate().getTime());
			modifiedDate = new Timestamp(simRegistrationDTO.getSimModifiedDate().getTime());

			query = " UPDATE public.nt_m_sim_registration "
					+ " SET sim_service_cat = ? , sim_permit_no = ? , sim_bus_no = ? , sim_no = ? , sim_receiver_name = ? ,"
					+ " sim_nic_no = ? , sim_issue_date = ? , sim_valid_until = ? , sim_remarks = ? , sim_modify_by = ? , sim_modify_date = ? , sim_emi_no = ? "
					+ " WHERE sim_reg_no = ? "; // 13
			ps = con.prepareStatement(query);
			ps.setString(1, simRegistrationDTO.getServiceCategory());
			ps.setString(2, simRegistrationDTO.getPermitNo());
			ps.setString(3, simRegistrationDTO.getBusNo());
			ps.setString(4, simRegistrationDTO.getSimNo());
			ps.setString(5, simRegistrationDTO.getReceiversName());
			ps.setString(6, simRegistrationDTO.getNicNo());
			ps.setTimestamp(7, issueDate);
			ps.setTimestamp(8, validUntilDate);
			ps.setString(9, simRegistrationDTO.getRemarks());
			ps.setString(10, simRegistrationDTO.getSimModifiedBy());
			ps.setTimestamp(11, modifiedDate);
			ps.setString(12, simRegistrationDTO.getEmiNo());
			ps.setString(13, simRegistrationDTO.getSimRegNo());

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
	public List<SimRegistrationDTO> getChargeDetailsList() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		SimRegistrationDTO simRegistrationDTO;
		List<SimRegistrationDTO> dtoList = new ArrayList<>();

		try {
			con = ConnectionManager.getConnection();

			String query;

			query = "select nt_r_charge_type.description,tct_account_no,tct_amount "
					+ " from nt_r_trn_charge_type,nt_r_charge_type "
					+ " where nt_r_trn_charge_type.tct_charge_type_code = nt_r_charge_type.code and tct_trn_type_code ='SR';";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				simRegistrationDTO = new SimRegistrationDTO();
				simRegistrationDTO.setVouChargeType(rs.getString("description"));
				simRegistrationDTO.setVouAccountNo(rs.getString("tct_account_no"));
				simRegistrationDTO.setVouAmmount(rs.getDouble("tct_amount"));

				dtoList.add(simRegistrationDTO);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return dtoList;
	}

	@Override
	public void addVoucher(SimRegistrationDTO simRegistrationDTO, List<SimRegistrationDTO> voucherDTOList) {

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;

		try {
			con = ConnectionManager.getConnection();

			String query;
			String query2;
			String query3;

			Timestamp vouCreatedDate = new Timestamp(simRegistrationDTO.getVouCreatedDate().getTime());

			long simPaymentVoucherSeqNo = Utils.getNextValBySeqName(con, "seq_nt_m_sim_payment_voucher");

			String isVoucherGenerated = "Y";
			String paymentType = "V";
			String transactionType = "SR";
			String approvedStatus = "P";

			query = " INSERT INTO public.nt_m_sim_payment_voucher( spv_seq_no , spv_permit_no , spv_sim_no , spv_voucher_no , "
					+ " spv_payment_type , spv_is_voucher_generated , spv_total_amount, spv_tran_type , spv_created_by , spv_created_date , spv_approved_status ) "
					+ " VALUES ( ? , ? , ? , ? , ?, ? , ? , ? , ? , ? , ? ) ; "; // 11

			ps = con.prepareStatement(query);

			ps.setLong(1, simPaymentVoucherSeqNo);
			ps.setString(2, simRegistrationDTO.getPermitNo());
			ps.setString(3, simRegistrationDTO.getSimRegNo());
			ps.setString(4, simRegistrationDTO.getVouNo());
			ps.setString(5, paymentType);
			ps.setString(6, isVoucherGenerated);
			ps.setDouble(7, simRegistrationDTO.getVouTotalAmount());
			ps.setString(8, transactionType);
			ps.setString(9, simRegistrationDTO.getVouCreatedBy());
			ps.setTimestamp(10, vouCreatedDate);
			ps.setString(11, approvedStatus);

			ps.executeUpdate();

			con.commit();

			for (SimRegistrationDTO dto : voucherDTOList) {

				String chargeTypeCode = getChargeTypeCodeByDescription(dto.getVouChargeType());
				long simVoucherDetailsSeqNo = Utils.getNextValBySeqName(con, "seq_nt_t_sim_voucher_details");

				query2 = " INSERT INTO public.nt_t_sim_voucher_details( vod_seq_no , vod_payment_vou_seq , vod_charge_type , "
						+ " vod_account_no , vod_amount , vod_created_by , vod_created_date ) "
						+ " VALUES ( ? , ? , ? , ? , ?, ? , ? ) ; "; // 7

				ps2 = con.prepareStatement(query2);

				ps2.setLong(1, simVoucherDetailsSeqNo);
				ps2.setLong(2, simPaymentVoucherSeqNo);
				ps2.setString(3, chargeTypeCode);
				ps2.setString(4, dto.getVouAccountNo());
				ps2.setDouble(5, dto.getVouAmmount());
				ps2.setString(6, simRegistrationDTO.getVouCreatedBy());
				ps2.setTimestamp(7, vouCreatedDate);

				ps2.executeUpdate();

			}

			query3 = "UPDATE public.nt_m_sim_registration SET sim_status_type = 'V' , sim_modify_by = ? , sim_modify_date = ? "
					+ " WHERE sim_reg_no = ? ";

			ps3 = con.prepareStatement(query3);
			ps3.setString(1, simRegistrationDTO.getVouCreatedBy());
			ps3.setTimestamp(2, vouCreatedDate);
			ps3.setString(3, simRegistrationDTO.getSimRegNo());

			ps3.executeUpdate();

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

	}

	@Override
	public String generateVoucherNo() {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;

		// Format - VSR 20 00001
		int year = Year.now().getValue();
		String currYear = Integer.toString(year).substring(2, 4);
		String strAppNo = "";
		String lastyr = "00";

		try {
			con = ConnectionManager.getConnection();

			String sql = "select spv_voucher_no  from  public.nt_m_sim_payment_voucher order by spv_created_date desc  LIMIT 1";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("spv_voucher_no");
			}

			if (result != null) {

				lastyr = result.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String ApprecordcountN = "00001";
					strAppNo = "VSR" + currYear + ApprecordcountN;
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
					strAppNo = "VSR" + currYear + ApprecordcountN;
				}
			} else
				strAppNo = "VSR" + currYear + "00001";

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
	public String getChargeTypeCodeByDescription(String desc) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String code = null;

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT code, description FROM nt_r_charge_type WHERE description = ? ;";
			ps = con.prepareStatement(query);
			ps.setString(1, desc);
			rs = ps.executeQuery();

			while (rs.next()) {
				code = rs.getString("code");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return code;
	}

	@Override
	public List<SimRegistrationDTO> simRegNoList() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		SimRegistrationDTO simDTO;
		List<SimRegistrationDTO> dtoList = new ArrayList<>();

		try {
			con = ConnectionManager.getConnection();

			String query;

			query = "select * ,a.spv_voucher_no ,a.spv_approved_status from public.nt_m_sim_registration\r\n"
					+ "inner join public.nt_m_sim_payment_voucher a on a.spv_sim_no=public.nt_m_sim_registration.sim_reg_no\r\n"
					+ "where sim_status in ('P','A') and a.spv_approved_status not in ('VC')";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				simDTO = new SimRegistrationDTO();
				simDTO.setSimRegNo(rs.getString("sim_reg_no"));
				simDTO.setSimNo(rs.getString("sim_no"));
				simDTO.setPermitNo(rs.getString("sim_permit_no"));
				simDTO.setEmiBusNo(rs.getString("sim_bus_no"));
				simDTO.setReceiptNo(rs.getString("sim_receipt_no"));
				simDTO.setVouNo(rs.getString("spv_voucher_no"));

				dtoList.add(simDTO);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return dtoList;
	}

	@Override
	public List<SimRegistrationDTO> pendingList(String simRegNo, String simNo, String simRenewNo, String busnO,
			String permitNo, String voucherNo, String status) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		SimRegistrationDTO simDTO;
		List<SimRegistrationDTO> dtoList = new ArrayList<>();

		String WHERE_SQL = "";
		if (simRegNo != null && !simRegNo.isEmpty()) {
			WHERE_SQL = WHERE_SQL + " AND sim_reg_no = " + "'" + simRegNo + "'";
		}
		if (simNo != null && !simNo.isEmpty()) {
			WHERE_SQL = WHERE_SQL + " AND sim_no = " + "'" + simNo + "'";
		}
		
		if (busnO != null && !busnO.isEmpty()) {
			WHERE_SQL = WHERE_SQL + " AND sim_bus_no = " + "'" + busnO + "'";
		}
		if (permitNo != null && !permitNo.isEmpty()) {
			WHERE_SQL = WHERE_SQL + " AND sim_permit_no = " + "'" + permitNo + "'";
		}
		if (voucherNo != null && !voucherNo.isEmpty()) {
			WHERE_SQL = WHERE_SQL + " AND a.spv_voucher_no = " + "'" + voucherNo + "'";
		}
		if (status != null && !status.isEmpty()) {
			WHERE_SQL = WHERE_SQL + " AND a.spv_approved_status = " + "'" + status + "'";
		}

		try {
			con = ConnectionManager.getConnection();

			String query;
			query = "select * ,a.spv_voucher_no ,a.spv_approved_status,a.spv_created_date,a.spv_total_amount ,b.vod_account_no,b.vod_amount\r\n"
					+ "from public.nt_m_sim_registration\r\n"
					+ "inner join public.nt_m_sim_payment_voucher a on a.spv_sim_no=public.nt_m_sim_registration.sim_reg_no\r\n"
					+ "inner join public.nt_t_sim_voucher_details b on b.vod_payment_vou_seq=a.spv_seq_no\r\n"
					+ "where sim_status in ('P','A')" + WHERE_SQL;
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				simDTO = new SimRegistrationDTO();
				simDTO.setSimRegNo(rs.getString("sim_reg_no"));
				simDTO.setSimNo(rs.getString("sim_no"));
				simDTO.setPermitNo(rs.getString("sim_permit_no"));
				simDTO.setEmiBusNo(rs.getString("sim_bus_no"));
				simDTO.setReceiptNo(rs.getString("sim_receipt_no"));
				simDTO.setVouNo(rs.getString("spv_voucher_no"));
				if (rs.getString("spv_approved_status") != null) {
					if (rs.getString("spv_approved_status").equalsIgnoreCase("P")) {

						simDTO.setEmiStatus("Pending");
					} else if (rs.getString("spv_approved_status").equalsIgnoreCase("A")) {
						simDTO.setEmiStatus("Approved");
					} else if (rs.getString("spv_approved_status").equalsIgnoreCase("R")) {
						simDTO.setEmiStatus("Rejected");
					}
				}

				Date date = rs.getDate("spv_created_date");
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				String strDate = dateFormat.format(date);
				simDTO.setCreatedDateString(strDate);
				simDTO.setVouTotalAmount(rs.getDouble("spv_total_amount"));

				if (rs.getString("sim_service_cat") != null) {

					if (rs.getString("sim_service_cat").equals("ntc")) {

						simDTO.setServiceCategory("NTC");
					}

					else if (rs.getString("sim_service_cat").equals("sisu")) {

						simDTO.setServiceCategory("Sisu Sariya");
					} else if (rs.getString("sim_service_cat").equals("nisi")) {

						simDTO.setServiceCategory("Nisi Sariya");
					} else if (rs.getString("sim_service_cat").equals("gami")) {

						simDTO.setServiceCategory("Gami Sariya");
					} else if (rs.getString("sim_service_cat").equals("other")) {

						simDTO.setServiceCategory("Other");
					}
				}
				simDTO.setEmiNo(rs.getString("sim_emi_no"));
				simDTO.setReceiversName(rs.getString("sim_receiver_name"));
				simDTO.setNicNo(rs.getString("sim_nic_no"));

				Date dateI = rs.getDate("sim_issue_date");
				String strDateI = dateFormat.format(dateI);
				simDTO.setIssueDateN(strDateI);

				Date dateV = rs.getDate("sim_valid_until");
				String strDateV = dateFormat.format(dateV);
				simDTO.setValidN(strDateV);
				simDTO.setVouAccountNo(rs.getString("vod_account_no"));
				simDTO.setVouAmmount(rs.getDouble("vod_amount"));

				dtoList.add(simDTO);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return dtoList;
	}

	@Override
	public SimRegistrationDTO filterDTO(String simRegNo, String simNo, String simRenewNo, String busnO, String permitNo,
			String voucherNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		SimRegistrationDTO simDTO = new SimRegistrationDTO();

		String WHERE_SQL = "";
		if (simRegNo != null && !simRegNo.isEmpty()) {
			WHERE_SQL = WHERE_SQL + " AND sim_reg_no = " + "'" + simRegNo + "'";
		}
		if (simNo != null && !simNo.isEmpty()) {
			WHERE_SQL = WHERE_SQL + " AND sim_no = " + "'" + simNo + "'";
		}
		
		if (busnO != null && !busnO.isEmpty()) {
			WHERE_SQL = WHERE_SQL + " AND sim_bus_no = " + "'" + busnO + "'";
		}
		if (permitNo != null && !permitNo.isEmpty()) {
			WHERE_SQL = WHERE_SQL + " AND sim_permit_no = " + "'" + permitNo + "'";
		}
		if (voucherNo != null && !voucherNo.isEmpty()) {
			WHERE_SQL = WHERE_SQL + " AND a.spv_voucher_no = " + "'" + voucherNo + "'";
		}

		try {
			con = ConnectionManager.getConnection();

			String query;

			query = "select * ,a.spv_voucher_no ,a.spv_approved_status,a.spv_created_date,a.spv_total_amount  from public.nt_m_sim_registration\r\n"
					+ "inner join public.nt_m_sim_payment_voucher a on a.spv_sim_no=public.nt_m_sim_registration.sim_reg_no\r\n"
					+ "where sim_status in ('P','A')" + WHERE_SQL;
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				simDTO = new SimRegistrationDTO();
				simDTO.setSimRegNo(rs.getString("sim_reg_no"));
				simDTO.setSimNo(rs.getString("sim_no"));
				simDTO.setPermitNo(rs.getString("sim_permit_no"));
				simDTO.setEmiBusNo(rs.getString("sim_bus_no"));
				simDTO.setReceiptNo(rs.getString("sim_receipt_no"));
				simDTO.setVouNo(rs.getString("spv_voucher_no"));
				if (rs.getString("spv_approved_status") != null) {
					if (rs.getString("spv_approved_status").equalsIgnoreCase("P")) {

						simDTO.setEmiStatus("Pending");
					} else if (rs.getString("spv_approved_status").equalsIgnoreCase("A")) {
						simDTO.setEmiStatus("Approved");
					} else if (rs.getString("spv_approved_status").equalsIgnoreCase("R")) {
						simDTO.setEmiStatus("Rejected");
					}
				}

				Date date = rs.getDate("spv_created_date");
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				String strDate = dateFormat.format(date);
				simDTO.setCreatedDateString(strDate);
				simDTO.setVouTotalAmount(rs.getDouble("spv_total_amount"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return simDTO;
	}

	@Override
	public String updateApproveRejectVoucher(String voucherNo, String status, String reason, String loginUser) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql3 = "UPDATE public.nt_m_sim_payment_voucher SET spv_approved_status =?,spv_reject_reason  =?, spv_modified_by =?,spv_modified_date  =?, spv_approved_date=? WHERE  spv_voucher_no =? ";

			ps = con.prepareStatement(sql3);

			ps.setString(1, status);
			ps.setString(2, reason);
			ps.setString(3, loginUser);
			ps.setTimestamp(4, timestamp);
			ps.setTimestamp(5, timestamp);
			ps.setString(6, voucherNo);

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
	public SimRegistrationDTO updateStatusType(String statusType, String simRegNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String sql3 = "UPDATE public.nt_m_sim_registration SET sim_status_type =? WHERE  sim_reg_no=? ";

			ps = con.prepareStatement(sql3);

			ps.setString(1, statusType);
			ps.setString(2, simRegNo);

			ps.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return null;

	}

	@Override
	public void updateVoucher(SimRegistrationDTO simRegistrationDTO, List<SimRegistrationDTO> voucherDTOList) {

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps4 = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String q;
			String query;
			String query2;
			String query4;

			String spvSeqNo = null;

			Timestamp vouModifiedDate = new Timestamp(simRegistrationDTO.getVouModifiedDate().getTime());

			// select SEQ No by voucher No
			q = "SELECT spv_seq_no FROM public.nt_m_sim_payment_voucher WHERE spv_voucher_no = ? ;";

			ps4 = con.prepareStatement(q);
			ps4.setString(1, simRegistrationDTO.getVouNo());

			rs = ps4.executeQuery();

			while (rs.next()) {
				spvSeqNo = rs.getString("spv_seq_no");
			}

			query = "UPDATE public.nt_m_sim_payment_voucher "
					+ "SET spv_total_amount = ? , spv_modified_by = ? , spv_modified_date = ? "
					+ " WHERE spv_voucher_no = ? ";

			ps = con.prepareStatement(query);
			ps.setDouble(1, simRegistrationDTO.getVouTotalAmount());
			ps.setString(2, simRegistrationDTO.getVouModifiedBy());
			ps.setTimestamp(3, vouModifiedDate);
			ps.setString(4, simRegistrationDTO.getVouNo());

			ps.executeUpdate();

			// delete raws which are related with SPV SEQ No
			query4 = "DELETE FROM public.nt_t_sim_voucher_details WHERE vod_payment_vou_seq = ? ;";

			ps1 = con.prepareStatement(query4);
			ps1.setString(1, spvSeqNo);

			ps1.executeUpdate();

			// Add updated details
			for (SimRegistrationDTO dto : voucherDTOList) {

				String chargeTypeCode = getChargeTypeCodeByDescription(dto.getVouChargeType());
				long simVoucherDetailsSeqNo = Utils.getNextValBySeqName(con, "seq_nt_t_sim_voucher_details");

				query2 = " INSERT INTO public.nt_t_sim_voucher_details( vod_seq_no , vod_payment_vou_seq , vod_charge_type , "
						+ " vod_account_no , vod_amount , vod_created_by , vod_created_date , vod_modified_by , vod_modified_date ) "
						+ " VALUES ( ? , ? , ? , ? , ?, ? , ? , ?, ? ) ; "; // 9

				ps2 = con.prepareStatement(query2);

				ps2.setLong(1, simVoucherDetailsSeqNo);
				ps2.setString(2, spvSeqNo);
				ps2.setString(3, chargeTypeCode);
				ps2.setString(4, dto.getVouAccountNo());
				ps2.setDouble(5, dto.getVouAmmount());
				ps2.setString(6, simRegistrationDTO.getVouModifiedBy());
				ps2.setTimestamp(7, vouModifiedDate);
				ps2.setString(8, simRegistrationDTO.getVouModifiedBy());
				ps2.setTimestamp(9, vouModifiedDate);

				ps2.executeUpdate();

			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

	}

	@Override
	public List<SimRegistrationDTO> getApprovedVouNoList() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		SimRegistrationDTO simDTO;
		List<SimRegistrationDTO> dtoList = new ArrayList<>();

		try {
			con = ConnectionManager.getConnection();

			String query;

			query = "select spv_permit_no,spv_voucher_no,spv_sim_no\r\n" + "from public.nt_m_sim_payment_voucher\r\n"
					+ "where spv_approved_status='A' order by spv_approved_date desc ";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				simDTO = new SimRegistrationDTO();

				simDTO.setSimRegNo(rs.getString("spv_sim_no"));
				simDTO.setPermitNo(rs.getString("spv_permit_no"));
				simDTO.setVouNo(rs.getString("spv_voucher_no"));

				dtoList.add(simDTO);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return dtoList;
	}

	@Override
	public int getTotalAmount(String voucherNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int data = 0;
		try {

			con = ConnectionManager.getConnection();

			String query = "select spv_total_amount from public.nt_m_sim_payment_voucher where spv_voucher_no=?;";
			ps = con.prepareStatement(query);
			ps.setString(1, voucherNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				data = rs.getInt("spv_total_amount");
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
	public boolean isReceiptgeneratedForInves(String voucherNo) {
		boolean isReceiptgenerated = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "select spv_receipt_no from public.nt_m_sim_payment_voucher "
					+ "WHERE spv_voucher_no=? LIMIT 1;";

			ps = con.prepareStatement(query);
			ps.setString(1, voucherNo);
			rs = ps.executeQuery();

			if (rs.next() && rs.getString("spv_receipt_no") != null) {
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
	public SimRegistrationDTO getReceiptDetailsForInves(String voucherNo) {
		SimRegistrationDTO receiptDetails = new SimRegistrationDTO();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps2 = null;
		ResultSet rs2 = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT rec_payment_mode, rec_back_code, rec_branch_code, rec_is_diposit_to_branch,rec_receipt_no,\r\n"
					+ "rec_cheque_no, rec_remarks \r\n"
					+ "FROM public.nt_t_sim_receipt WHERE rec_voucher_no=? LIMIT 1;";

			ps = con.prepareStatement(query);
			ps.setString(1, voucherNo);
			rs = ps.executeQuery();

			if (rs.next()) {
				receiptDetails.setPaymentModeCodeForSimReg(rs.getString("rec_payment_mode"));
				if (rs.getString("rec_back_code") != null) {
					receiptDetails.setBankCodeForSimReg(rs.getString("rec_back_code"));
					receiptDetails.setBranchCodeForSimReg(rs.getString("rec_branch_code"));

					// get bank and branch description
					String query2 = "SELECT b.description as bankDes, c.description as branchDes \r\n"
							+ "FROM public.nt_t_sim_receipt r\r\n"
							+ "INNER JOIN nt_r_bank b on b.code= r.rec_back_code \r\n"
							+ "INNER JOIN nt_r_branch c on c.code= r.rec_branch_code \r\n"
							+ "WHERE rec_voucher_no=? \r\n" + "LIMIT 1;";
					ps2 = con.prepareStatement(query2);
					ps2.setString(1, voucherNo);
					rs2 = ps2.executeQuery();
					if (rs2.next()) {
						receiptDetails.setBankDesForSimReg(rs2.getString("bankDes"));
						receiptDetails.setBranchDesForSimReg(rs2.getString("branchDes"));
					}
				}
				if (rs.getString("rec_is_diposit_to_branch").equals("Y")) {
					receiptDetails.setDepositToBankForSimReg(true);
				} else if (rs.getString("rec_is_diposit_to_branch").equals("N")) {
					receiptDetails.setDepositToBankForSimReg(false);
				}
				receiptDetails.setChequeOrBankReceipt(rs.getString("rec_cheque_no"));
				receiptDetails.setReceiptRemarks(rs.getString("rec_remarks"));
				receiptDetails.setReceiptNo(rs.getString("rec_receipt_no"));
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
	public void deleteEmiDetails(String simRegNo) {

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionManager.getConnection();

			String query;

			query = "DELETE FROM public.nt_t_emi_det WHERE emi_sim_reg_no = ? ;";
			ps = con.prepareStatement(query);
			ps.setString(1, simRegNo);

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
	public boolean validateExistBusPermit(SimRegistrationDTO simRegistrationDTO) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean check = false;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query;

			query = " SELECT sim_reg_no FROM public.nt_m_sim_registration WHERE sim_permit_no = ? AND sim_bus_no = ? ;";
			ps = con.prepareStatement(query);
			ps.setString(1, simRegistrationDTO.getPermitNo());
			ps.setString(2, simRegistrationDTO.getBusNo());

			rs = ps.executeQuery();

			if (rs.next()) {
				check = false;
			} else {
				check = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return check;

	}

	@Override
	public void updateVoucherPrintReprintStatus(SimRegistrationDTO simRegistrationDTO, String isPrint,
			String isRePrint) {
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionManager.getConnection();

			String query;

			query = " UPDATE public.nt_m_sim_payment_voucher " + " SET spv_isprint  = ? "
					+ " WHERE spv_voucher_no  = ? ";
			ps = con.prepareStatement(query);
			ps.setString(1, isPrint);
			ps.setString(2, simRegistrationDTO.getVouNo());

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
	public List<SimRegistrationDTO> pendingDTO(String simRegNo, String simNo, String simRenewNo, String busnO,
			String permitNo, String voucherNo, String status) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		SimRegistrationDTO simDTO = new SimRegistrationDTO();
		List<SimRegistrationDTO> dtoList = new ArrayList<>();

		String WHERE_SQL = "";
		if (simRegNo != null && !simRegNo.isEmpty()) {
			WHERE_SQL = WHERE_SQL + " AND sim_reg_no = " + "'" + simRegNo + "'";
		}
		if (simNo != null && !simNo.isEmpty()) {
			WHERE_SQL = WHERE_SQL + " AND sim_no = " + "'" + simNo + "'";
		}
		
		if (busnO != null && !busnO.isEmpty()) {
			WHERE_SQL = WHERE_SQL + " AND sim_bus_no = " + "'" + busnO + "'";
		}
		if (permitNo != null && !permitNo.isEmpty()) {
			WHERE_SQL = WHERE_SQL + " AND sim_permit_no = " + "'" + permitNo + "'";
		}
		if (voucherNo != null && !voucherNo.isEmpty()) {
			WHERE_SQL = WHERE_SQL + " AND a.spv_voucher_no = " + "'" + voucherNo + "'";
		}
		if (status != null && !status.isEmpty()) {
			WHERE_SQL = WHERE_SQL + " AND a.spv_approved_status = " + "'" + status + "'";
		}

		try {
			con = ConnectionManager.getConnection();

			String query;

			query = "select * ,a.spv_voucher_no ,a.spv_approved_status,a.spv_created_date,a.spv_total_amount  from public.nt_m_sim_registration\r\n"
					+ "inner join public.nt_m_sim_payment_voucher a on a.spv_sim_no=public.nt_m_sim_registration.sim_reg_no\r\n"
					+ "where sim_status in ('P','A') and a.spv_receipt_no is  null" + WHERE_SQL
					+ "order by  a.spv_voucher_no desc";
					//+ "order by a.spv_approved_date desc";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				simDTO = new SimRegistrationDTO();
				simDTO.setSimRegNo(rs.getString("sim_reg_no"));
				simDTO.setSimNo(rs.getString("sim_no"));
				simDTO.setPermitNo(rs.getString("sim_permit_no"));
				simDTO.setEmiBusNo(rs.getString("sim_bus_no"));
				simDTO.setReceiptNo(rs.getString("sim_receipt_no"));
				simDTO.setVouNo(rs.getString("spv_voucher_no"));
				if (rs.getString("spv_approved_status") != null) {
					if (rs.getString("spv_approved_status").equalsIgnoreCase("P")) {

						simDTO.setEmiStatus("Pending");
					} else if (rs.getString("spv_approved_status").equalsIgnoreCase("A")) {
						simDTO.setEmiStatus("Approved");
					} else if (rs.getString("spv_approved_status").equalsIgnoreCase("R")) {
						simDTO.setEmiStatus("Rejected");
					}
				}

				Date date = rs.getDate("spv_created_date");
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				String strDate = dateFormat.format(date);
				simDTO.setCreatedDateString(strDate);
				simDTO.setVouTotalAmount(rs.getDouble("spv_total_amount"));

				if (rs.getString("sim_service_cat") != null) {

					if (rs.getString("sim_service_cat").equals("ntc")) {

						simDTO.setServiceCategory("NTC");
					}

					else if (rs.getString("sim_service_cat").equals("sisu")) {

						simDTO.setServiceCategory("Sisu Sariya");
					} else if (rs.getString("sim_service_cat").equals("nisi")) {

						simDTO.setServiceCategory("Nisi Sariya");
					} else if (rs.getString("sim_service_cat").equals("gami")) {

						simDTO.setServiceCategory("Gami Sariya");
					} else if (rs.getString("sim_service_cat").equals("other")) {

						simDTO.setServiceCategory("Other");
					}
				}
				simDTO.setEmiNo(rs.getString("sim_emi_no"));
				simDTO.setReceiversName(rs.getString("sim_receiver_name"));
				simDTO.setNicNo(rs.getString("sim_nic_no"));

				Date dateI = rs.getDate("sim_issue_date");
				String strDateI = dateFormat.format(dateI);
				simDTO.setIssueDateN(strDateI);

				Date dateV = rs.getDate("sim_valid_until");
				String strDateV = dateFormat.format(dateV);
				simDTO.setValidN(strDateV);

				dtoList.add(simDTO);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return dtoList;
	}

	@Override
	public void saveReceiptForSim(SimRegistrationDTO selectedRow, String loginUser) {

		String receiptNo = generateReceiptNoForSimReg();
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement ps = null;
		try {

			con = ConnectionManager.getConnection();
			long seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_t_sim_receipt");
			String query = "INSERT INTO public.nt_t_sim_receipt\r\n" + "(rec_seq_no, rec_sim_reg_no ,\r\n"
					+ "rec_voucher_no, rec_payment_mode, rec_is_diposit_to_branch, rec_receipt_no, rec_back_code,\r\n"
					+ "rec_branch_code, rec_cheque_no, rec_total_fee, rec_remarks, rec_is_receipt_generated, \r\n"
					+ "rep_created_by, rep_created_date)\r\n" + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

			ps = con.prepareStatement(query);

			ps.setLong(1, seqNo);
			ps.setString(2, selectedRow.getSimRegNo());

			ps.setString(3, selectedRow.getVouNo());
			ps.setString(4, selectedRow.getPaymentModeCodeForSimReg());
			String depositToBank = selectedRow.isDepositToBankForSimReg() ? "Y" : "N";

			ps.setString(5, depositToBank);
			ps.setString(6, receiptNo);
			ps.setString(7, selectedRow.getBankCodeForSimReg());
			ps.setString(8, selectedRow.getBranchCodeForSimReg());
			ps.setString(9, selectedRow.getChequeOrBankReceipt());
			ps.setBigDecimal(10, selectedRow.getTotalAmount());
			ps.setString(11, selectedRow.getReceiptRemarks());
			ps.setString(12, "Y");

			ps.setString(13, loginUser);
			ps.setTimestamp(14, timestamp);

			ps.executeUpdate();

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

	}

	public String generateReceiptNoForSimReg() {

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

			String sql = "SELECT rec_receipt_no FROM public.nt_t_sim_receipt ORDER BY rep_created_date desc  LIMIT 1";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("rec_receipt_no");
			}

			if (result != null) {

				lastyr = result.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String ApprecordcountN = "00001";
					strRecNo = "RSR" + currYear + ApprecordcountN;
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
					strRecNo = "RSR" + currYear + ApprecordcountN;
				}
			} else {
				strRecNo = "RSR" + currYear + "00001";
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
	public void updateEmi(SimRegistrationDTO emiDTO) {
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionManager.getConnection();

			String query;

			Timestamp reIssueDate, createdDate;

			reIssueDate = new Timestamp(emiDTO.getEmiReIssueDate().getTime());
			createdDate = new Timestamp(emiDTO.getSimCreatedDate().getTime());

			String status;
			if (emiDTO.getEmiStatus().equals("Active")) {
				status = "A";
			} else {
				status = "I";
			}

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_emi_det");

			query = "INSERT INTO public.nt_t_emi_det ( emi_sim_reg_no , emi_bus_no, emi_status , emi_re_issue_date , emi_modify_by , emi_modify_date , seq , emi_created_by , emi_created_date,emi_no  )"
					+ "VALUES ( ? , ? , ? , ? , ? , ? , ? , ?, ?,? ) ; "; // 9
			ps = con.prepareStatement(query);
			ps.setString(1, emiDTO.getSimRegNo());
			ps.setString(2, emiDTO.getEmiBusNo());
			ps.setString(3, status);
			ps.setTimestamp(4, reIssueDate);
			ps.setString(5, emiDTO.getSimCreatedBy());
			ps.setTimestamp(6, createdDate);
			ps.setLong(7, seqNo);
			ps.setString(8, emiDTO.getSimCreatedBy());
			ps.setTimestamp(9, createdDate);
			ps.setString(10, emiDTO.getEmiNo());
			ps.executeUpdate();

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
	}

	public String updateReceiptNo(String voucherNo, String receiptNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String sql3 = "UPDATE public.nt_m_sim_payment_voucher SET spv_receipt_no  =? WHERE  spv_voucher_no =? ";

			ps = con.prepareStatement(sql3);

			ps.setString(1, receiptNo);
			ps.setString(2, voucherNo);

			ps.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return receiptNo;

	}

	@Override
	public String getReceiptNo(String voucherNO) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String data = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "select rec_receipt_no from public.nt_t_sim_receipt where rec_voucher_no=?;";
			ps = con.prepareStatement(query);
			ps.setString(1, voucherNO);
			rs = ps.executeQuery();

			while (rs.next()) {
				data = rs.getString("rec_receipt_no");
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
	public List<SimRegistrationDTO> searchedList(String simRegNo, String simNo, String simRenewNo, String busnO,
			String permitNo, String voucherNo, String status) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		SimRegistrationDTO simDTO;
		List<SimRegistrationDTO> dtoList = new ArrayList<>();

		String WHERE_SQL = "";
		if (simRegNo != null && !simRegNo.isEmpty()) {
			WHERE_SQL = WHERE_SQL + " AND sim_reg_no = " + "'" + simRegNo + "'";
		}
		if (simNo != null && !simNo.isEmpty()) {
			WHERE_SQL = WHERE_SQL + " AND sim_no = " + "'" + simNo + "'";
		}
		
		if (busnO != null && !busnO.isEmpty()) {
			WHERE_SQL = WHERE_SQL + " AND sim_bus_no = " + "'" + busnO + "'";
		}
		if (permitNo != null && !permitNo.isEmpty()) {
			WHERE_SQL = WHERE_SQL + " AND sim_permit_no = " + "'" + permitNo + "'";
		}
		if (voucherNo != null && !voucherNo.isEmpty()) {
			WHERE_SQL = WHERE_SQL + " AND a.spv_voucher_no = " + "'" + voucherNo + "'";
		}
		if (status != null && !status.isEmpty()) {
			WHERE_SQL = WHERE_SQL + " AND a.spv_approved_status = " + "'" + status + "'";
		}

		try {
			con = ConnectionManager.getConnection();

			String query;
			query = "select * ,a.spv_voucher_no ,a.spv_approved_status,a.spv_created_date,a.spv_total_amount\r\n"
					+ "from public.nt_m_sim_registration\r\n"
					+ "inner join public.nt_m_sim_payment_voucher a on a.spv_sim_no=public.nt_m_sim_registration.sim_reg_no\r\n"
					+ "where sim_status in ('P','A') and a.spv_approved_status not in ('VC')" + WHERE_SQL
					+ "order by a.spv_approved_date desc";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				simDTO = new SimRegistrationDTO();
				simDTO.setSimRegNo(rs.getString("sim_reg_no"));
				simDTO.setSimNo(rs.getString("sim_no"));
				simDTO.setPermitNo(rs.getString("sim_permit_no"));
				simDTO.setEmiBusNo(rs.getString("sim_bus_no"));
				simDTO.setReceiptNo(rs.getString("sim_receipt_no"));
				simDTO.setVouNo(rs.getString("spv_voucher_no"));
				if (rs.getString("spv_approved_status") != null) {
					if (rs.getString("spv_approved_status").equalsIgnoreCase("P")) {

						simDTO.setEmiStatus("Pending");
					} else if (rs.getString("spv_approved_status").equalsIgnoreCase("A")) {
						simDTO.setEmiStatus("Approved");
					} else if (rs.getString("spv_approved_status").equalsIgnoreCase("R")) {
						simDTO.setEmiStatus("Rejected");
					}
				}

				Date date = rs.getDate("spv_created_date");
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				String strDate = dateFormat.format(date);
				simDTO.setCreatedDateString(strDate);
				simDTO.setVouTotalAmount(rs.getDouble("spv_total_amount"));

				if (rs.getString("sim_service_cat") != null) {

					if (rs.getString("sim_service_cat").equals("ntc")) {

						simDTO.setServiceCategory("NTC");
					}

					else if (rs.getString("sim_service_cat").equals("sisu")) {

						simDTO.setServiceCategory("Sisu Sariya");
					} else if (rs.getString("sim_service_cat").equals("nisi")) {

						simDTO.setServiceCategory("Nisi Sariya");
					} else if (rs.getString("sim_service_cat").equals("gami")) {

						simDTO.setServiceCategory("Gami Sariya");
					} else if (rs.getString("sim_service_cat").equals("other")) {

						simDTO.setServiceCategory("Other");
					}
				}
				simDTO.setEmiNo(rs.getString("sim_emi_no"));
				simDTO.setReceiversName(rs.getString("sim_receiver_name"));
				simDTO.setNicNo(rs.getString("sim_nic_no"));

				Date dateI = rs.getDate("sim_issue_date");
				String strDateI = dateFormat.format(dateI);
				simDTO.setIssueDateN(strDateI);

				Date dateV = rs.getDate("sim_valid_until");
				String strDateV = dateFormat.format(dateV);
				simDTO.setValidN(strDateV);

				dtoList.add(simDTO);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return dtoList;
	}

	@Override
	public String activeSim(String status, String simRegno, String receiptNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String sql3 = "UPDATE public.nt_m_sim_registration SET sim_status  =?,sim_receipt_no =? WHERE  sim_reg_no=? ";

			ps = con.prepareStatement(sql3);

			ps.setString(1, status);
			ps.setString(2, receiptNo);
			ps.setString(3, simRegno);

			ps.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return null;

	}

	@Override
	public int checkDuplicatePermit(String permitNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int data = 0;
		try {

			con = ConnectionManager.getConnection();

			String query = "select count(*) as dataCount from public.nt_m_sim_registration where sim_permit_no =? and sim_status_type not in ('R') ";
			ps = con.prepareStatement(query);
			ps.setString(1, permitNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				data = rs.getInt("dataCount");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);		}
		return data;
	}

	public int checkRegNoStatus(String regNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int data = 0;
		try {

			con = ConnectionManager.getConnection();

			String query = "select count(*) as dataCount from public.nt_m_sim_registration where sim_reg_no =? and sim_status_type not in ('R','V') ";
			ps = con.prepareStatement(query);
			ps.setString(1, regNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				data = rs.getInt("dataCount");
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
	public void insertInHistory(String voucherNO) {

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String vod_payment_vou_seq = null;

			String sql1 = "INSERT into public.nt_h_sim_payment_voucher  "
					+ " (SELECT * FROM public.nt_m_sim_payment_voucher WHERE spv_voucher_no  = ?) ";
			ps = con.prepareStatement(sql1);
			ps.setString(1, voucherNO);
			ps.executeUpdate();

			String sql3 = "SELECT spv_seq_no  FROM public.nt_m_sim_payment_voucher WHERE spv_voucher_no  = ?";
			ps3 = con.prepareStatement(sql3);
			ps3.setString(1, voucherNO);

			rs = ps3.executeQuery();
			while (rs.next()) {
				vod_payment_vou_seq = rs.getString("spv_seq_no");

			}

			String sql2 = "INSERT into public.nt_h_sim_voucher_details  "
					+ " (SELECT * FROM public.nt_t_sim_voucher_details WHERE vod_payment_vou_seq = ?) ";
			ps2 = con.prepareStatement(sql2);
			ps2.setString(1, vod_payment_vou_seq);
			ps2.executeUpdate();

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(ps3);
			ConnectionManager.close(con);
		}

	}

	@Override
	public void updateSimTransfer(SimRegistrationDTO simRegistrationDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String selectSimQuery = "SELECT * FROM public.nt_m_sim_registration WHERE sim_reg_no = '"
					+ simRegistrationDTO.getSimRegNo() + "' ; ";
			ps1 = con.prepareStatement(selectSimQuery);
			rs = ps1.executeQuery();

			while (rs.next()) {
				String q = "INSERT INTO public.nt_h_sim_registration ( seq , sim_reg_no , sim_service_cat , sim_permit_no , sim_bus_no , sim_no , sim_receiver_name , sim_nic_no , "
						+ " sim_issue_date, sim_valid_until , sim_remarks , sim_receipt_no , sim_status, sim_created_by , sim_created_date , sim_modify_by , sim_modify_date , "
						+ " sim_status_type , sim_emi_no  ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) "; // 19

				ps2 = con.prepareStatement(q);
				ps2.setLong(1, rs.getLong("seq"));
				ps2.setString(2, rs.getString("sim_reg_no"));
				ps2.setString(3, rs.getString("sim_service_cat"));
				ps2.setString(4, rs.getString("sim_permit_no"));
				ps2.setString(5, rs.getString("sim_bus_no"));
				ps2.setString(6, rs.getString("sim_no"));
				ps2.setString(7, rs.getString("sim_receiver_name"));
				ps2.setString(8, rs.getString("sim_nic_no"));
				ps2.setTimestamp(9, rs.getTimestamp("sim_issue_date"));
				ps2.setTimestamp(10, rs.getTimestamp("sim_valid_until"));
				ps2.setString(11, rs.getString("sim_remarks"));
				ps2.setString(12, rs.getString("sim_receipt_no"));
				ps2.setString(13, rs.getString("sim_status"));
				ps2.setString(14, rs.getString("sim_created_by"));
				ps2.setTimestamp(15, rs.getTimestamp("sim_created_date"));
				ps2.setString(16, rs.getString("sim_modify_by"));
				ps2.setTimestamp(17, rs.getTimestamp("sim_modify_date"));
				ps2.setString(18, rs.getString("sim_status_type"));
				ps2.setString(19, rs.getString("sim_emi_no"));

				ps2.executeUpdate();

			}

			String query;

			Timestamp modifiedDate;

			modifiedDate = new Timestamp(simRegistrationDTO.getSimModifiedDate().getTime());
			
			query = " UPDATE public.nt_m_sim_registration "
					+ " SET sim_modify_by = ? , sim_modify_date = ? , sim_emi_no = ? " + " WHERE sim_reg_no = ? "; // 13
			ps = con.prepareStatement(query);

			ps.setString(1, simRegistrationDTO.getSimModifiedBy());
			ps.setTimestamp(2, modifiedDate);
			ps.setString(3, simRegistrationDTO.getEmiNo());
			ps.setString(4, simRegistrationDTO.getSimRegNo());

			ps.executeUpdate();

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(ps1);
			ConnectionManager.close(ps2);
			ConnectionManager.close(con);
		}

	}

	@Override
	public List<SimRegistrationDTO> getOldPermitDetails(String permitNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		SimRegistrationDTO simDTO;
		List<SimRegistrationDTO> dtoList = new ArrayList<>();

		try {
			con = ConnectionManager.getConnection();

			String query;

			query = "select sim_permit_no, emi_bus_no,sim_reg_no,emi_no,sim_receiver_name, to_char(emi_re_issue_date,'yyyy-mm-dd') as issueDate,nt_m_sim_registration.sim_no\r\n"
					+ "from public.nt_t_emi_det inner join nt_m_sim_registration on nt_m_sim_registration.sim_reg_no = nt_t_emi_det.emi_sim_reg_no\r\n"
					+ "where emi_sim_reg_no =? order by emi_modify_date  Asc";

			ps = con.prepareStatement(query);
			ps.setString(1, permitNo); // in here not pass permit no it is sim reg number
			rs = ps.executeQuery();

			while (rs.next()) {
				simDTO = new SimRegistrationDTO();
				simDTO.setSimNo(rs.getString("sim_no"));
				simDTO.setPermitNo(rs.getString("sim_permit_no"));
				simDTO.setEmiBusNo(rs.getString("emi_bus_no"));
				simDTO.setReceiversName(rs.getString("sim_receiver_name"));
				simDTO.setEmiNo(rs.getString("emi_no"));
				simDTO.setIssueDateN(rs.getString("issueDate"));

				dtoList.add(simDTO);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return dtoList;
	}

	@Override
	public boolean insertSimAndEmiDetails(SimRegistrationDTO simRegistrationDTO, List<SimRegistrationDTO> emiDTOList) {

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		boolean save = false;
		try {
			con = ConnectionManager.getConnection();

			// add Sim
			String query;

			Timestamp issueDate, validUntilDate, createdDate;

			issueDate = new Timestamp(simRegistrationDTO.getIssueDate().getTime());
			validUntilDate = new Timestamp(simRegistrationDTO.getValidUntilDate().getTime());
			createdDate = new Timestamp(simRegistrationDTO.getSimCreatedDate().getTime());

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_sim_registration");

			String simStatus = "P";
			String simStatusType = "R";

			query = "INSERT INTO public.nt_m_sim_registration "
					+ " (sim_reg_no , sim_service_cat , sim_permit_no , sim_bus_no , sim_no , sim_receiver_name ,"
					+ " sim_nic_no , sim_issue_date , sim_valid_until , sim_remarks , sim_created_by , sim_created_date , sim_status , seq , sim_status_type, sim_emi_no ) "
					+ " VALUES ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?, ?); ";
			ps = con.prepareStatement(query);
			ps.setString(1, simRegistrationDTO.getSimRegNo());
			ps.setString(2, simRegistrationDTO.getServiceCategory());
			ps.setString(3, simRegistrationDTO.getPermitNo());
			ps.setString(4, simRegistrationDTO.getBusNo());
			ps.setString(5, simRegistrationDTO.getSimNo());
			ps.setString(6, simRegistrationDTO.getReceiversName());
			ps.setString(7, simRegistrationDTO.getNicNo());
			ps.setTimestamp(8, issueDate);
			ps.setTimestamp(9, validUntilDate);
			ps.setString(10, simRegistrationDTO.getRemarks());
			ps.setString(11, simRegistrationDTO.getSimCreatedBy());
			ps.setTimestamp(12, createdDate);
			ps.setString(13, simStatus);
			ps.setLong(14, seqNo);
			ps.setString(15, simStatusType);
			ps.setString(16, simRegistrationDTO.getEmiNo()); // added by dinushi

			int i = ps.executeUpdate();
			if (i > 0) {
				save = true;
			} else {
				save = false;
			}
			// set Generated key
			String query2;
			String param = "SIM_REGISTRATION_NUMBER_VALUE";

			String input = simRegistrationDTO.getSimRegNo();
			String lastFourDigits = input.substring(input.length() - 4);

			query2 = "UPDATE nt_r_parameters SET type = ? WHERE param_name = ?; ";
			ps2 = con.prepareStatement(query2);
			ps2.setString(1, String.valueOf(lastFourDigits));
			ps2.setString(2, param);

			ps2.executeUpdate();

			// Insert Emi details into public.nt_t_emi_det

			if (emiDTOList != null) {

				String query3;

				Timestamp reIssueDate2, createdDate2;

				for (SimRegistrationDTO dto : emiDTOList) {
					long seqNo2 = Utils.getNextValBySeqName(con, "seq_nt_t_emi_det");
					reIssueDate2 = new Timestamp(dto.getEmiReIssueDate().getTime());
					createdDate2 = new Timestamp(simRegistrationDTO.getSimCreatedDate().getTime());

					String status;
					if (dto.getEmiStatus().equals("Active")) {
						status = "A";
					} else {
						status = "I";
					}

					query3 = "INSERT INTO public.nt_t_emi_det ( emi_sim_reg_no , emi_bus_no, emi_status , emi_re_issue_date , emi_created_by , emi_created_date , seq, emi_no  )"
							+ "VALUES ( ? , ? , ? , ? , ? , ? , ?,? ) ; ";
					ps = con.prepareStatement(query3);
					ps.setString(1, simRegistrationDTO.getSimRegNo());
					ps.setString(2, dto.getEmiBusNo());
					ps.setString(3, status);
					ps.setTimestamp(4, reIssueDate2);
					ps.setString(5, simRegistrationDTO.getSimCreatedBy());
					ps.setTimestamp(6, createdDate2);
					ps.setLong(7, seqNo2);
					ps.setString(8, simRegistrationDTO.getEmiNo());
					ps.executeUpdate();
				}
			}
			// End Inserting Emi Details
			con.commit();

		} catch (Exception e) {
			save = false;
			e.printStackTrace();

		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(ps3);
			ConnectionManager.close(con);
		}
		return save;

	}

	@Override
	public boolean updateSimAndEmiDetails(SimRegistrationDTO simRegistrationDTO, List<SimRegistrationDTO> emiDTOList) {

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		boolean save = false;
		try {
			con = ConnectionManager.getConnection();

			// add Sim
			String query;

			Timestamp issueDate, validUntilDate, modifiedDate;

			issueDate = new Timestamp(simRegistrationDTO.getIssueDate().getTime());
			validUntilDate = new Timestamp(simRegistrationDTO.getValidUntilDate().getTime());
			modifiedDate = new Timestamp(simRegistrationDTO.getSimModifiedDate().getTime());

			query = " UPDATE public.nt_m_sim_registration "
					+ " SET sim_service_cat = ? , sim_permit_no = ? , sim_bus_no = ? , sim_no = ? , sim_receiver_name = ? ,"
					+ " sim_nic_no = ? , sim_issue_date = ? , sim_valid_until = ? , sim_remarks = ? , sim_modify_by = ? , sim_modify_date = ? , sim_emi_no = ? "
					+ " WHERE sim_reg_no = ? "; // 13
			ps = con.prepareStatement(query);
			ps.setString(1, simRegistrationDTO.getServiceCategory());
			ps.setString(2, simRegistrationDTO.getPermitNo());
			ps.setString(3, simRegistrationDTO.getBusNo());
			ps.setString(4, simRegistrationDTO.getSimNo());
			ps.setString(5, simRegistrationDTO.getReceiversName());
			ps.setString(6, simRegistrationDTO.getNicNo());
			ps.setTimestamp(7, issueDate);
			ps.setTimestamp(8, validUntilDate);
			ps.setString(9, simRegistrationDTO.getRemarks());
			ps.setString(10, simRegistrationDTO.getSimModifiedBy());
			ps.setTimestamp(11, modifiedDate);
			ps.setString(12, simRegistrationDTO.getEmiNo());
			ps.setString(13, simRegistrationDTO.getSimRegNo());

			ps.executeUpdate();

			int i = ps.executeUpdate();
			if (i > 0) {
				save = true;
			} else {
				save = false;
			}

			// update Emi details into public.nt_t_emi_det

			if (emiDTOList != null) {

				deleteEmiDetails(simRegistrationDTO.getSimRegNo());

				String query3;

				Timestamp reIssueDate2, createdDate2;

				for (SimRegistrationDTO dto : emiDTOList) {
					long seqNo2 = Utils.getNextValBySeqName(con, "seq_nt_t_emi_det");
					reIssueDate2 = new Timestamp(dto.getEmiReIssueDate().getTime());
					createdDate2 = new Timestamp(simRegistrationDTO.getSimCreatedDate().getTime());

					String status;
					if (dto.getEmiStatus().equals("Active")) {
						status = "A";
					} else {
						status = "I";
					}

					query3 = "INSERT INTO public.nt_t_emi_det ( emi_sim_reg_no , emi_bus_no, emi_status , emi_re_issue_date , emi_created_by , emi_created_date , seq, emi_no  )"
							+ "VALUES ( ? , ? , ? , ? , ? , ? , ?,? ) ; ";
					ps = con.prepareStatement(query3);
					ps.setString(1, simRegistrationDTO.getSimRegNo());
					ps.setString(2, dto.getEmiBusNo());
					ps.setString(3, status);
					ps.setTimestamp(4, reIssueDate2);
					ps.setString(5, simRegistrationDTO.getSimCreatedBy());
					ps.setTimestamp(6, createdDate2);
					ps.setLong(7, seqNo2);
					ps.setString(8, simRegistrationDTO.getEmiNo());
					ps.executeUpdate();
				}
			}
			// End Inserting Emi Details
			con.commit();

		} catch (Exception e) {
			save = false;
			e.printStackTrace();

		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(ps3);
			ConnectionManager.close(con);
		}
		return save;

	}
}
