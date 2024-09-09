package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import lk.informatics.ntc.model.dto.AccidentDTO;
import lk.informatics.ntc.model.dto.AmendmentDTO;
import lk.informatics.ntc.model.dto.AmendmentServiceDTO;
import lk.informatics.ntc.model.dto.IssuePermitDTO;
import lk.informatics.ntc.model.dto.OminiBusDTO;
import lk.informatics.ntc.model.dto.PermitRenewalsDTO;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.roc.utils.common.Utils;

public class HistoryServiceImpl implements HistoryService {

	@Override
	public PermitRenewalsDTO getVehicleOwnerTableData(String applicationNo, String user) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PermitRenewalsDTO dto = new PermitRenewalsDTO();
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT seq, pmo_application_no, pmo_permit_no, pmo_vehicle_regno, pmo_preferred_language, pmo_title, "
					+ "pmo_full_name, pmo_full_name_sinhala, pmo_full_name_tamil, pmo_name_with_initial, pmo_nic, pmo_gender, "
					+ "pmo_dob, pmo_marital_status, pmo_telephone_no, pmo_mobile_no, pmo_province, pmo_district, pmo_div_sec, "
					+ "pmo_address1, pmo_address1_sinhala, pmo_address1_tamil, pmo_address2, pmo_address2_sinhala, pmo_address2_tamil, "
					+ "pmo_address3, pmo_address3_sinhala, pmo_address3_tamil, pmo_city, pmo_city_sinhala, pmo_city_tamil, "
					+ "pmo_is_backlog_app, pm_created_by, pm_created_date, pm_modified_by, pm_modified_date "
					+ "FROM public.nt_t_pm_vehi_owner " + "where pmo_application_no=? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, applicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				dto.setApplicationNo(rs.getString("pmo_application_no"));
				dto.setPermitNo(rs.getString("pmo_permit_no"));
				dto.setRegNoOfBus(rs.getString("pmo_vehicle_regno"));
				dto.setPreferedLanguageCode(rs.getString("pmo_preferred_language"));
				dto.setTitleCode(rs.getString("pmo_title"));

				dto.setFullName(rs.getString("pmo_full_name"));
				dto.setFullNameSinhala(rs.getString("pmo_full_name_sinhala"));
				dto.setFullNameTamil(rs.getString("pmo_full_name_tamil"));
				dto.setNameWithInitials(rs.getString("pmo_name_with_initial"));
				dto.setNic(rs.getString("pmo_nic"));
				dto.setGenderCode(rs.getString("pmo_gender"));

				dto.setDob(rs.getString("pmo_dob"));
				dto.setMaterialStatusId(rs.getString("pmo_marital_status"));
				dto.setTeleNo(rs.getString("pmo_telephone_no"));
				dto.setMobileNo(rs.getString("pmo_mobile_no"));
				dto.setProvinceCode(rs.getString("pmo_province"));
				dto.setDistrictCode(rs.getString("pmo_district"));
				dto.setDivisionalSecretariatDivision(rs.getString("pmo_div_sec"));

				dto.setAddressOne(rs.getString("pmo_address1"));
				dto.setAddressOneSinhala(rs.getString("pmo_address1_sinhala"));
				dto.setAddressOneTamil(rs.getString("pmo_address1_tamil"));
				dto.setAddressTwo(rs.getString("pmo_address2"));
				dto.setAddressTwoSinhala(rs.getString("pmo_address2_sinhala"));
				dto.setAddressTwoTamil(rs.getString("pmo_address2_tamil"));

				dto.setAddressThree(rs.getString("pmo_address3"));
				dto.setAddressThreeSinhala(rs.getString("pmo_address3_sinhala"));
				dto.setAddressThreeTamil(rs.getString("pmo_address3_tamil"));
				dto.setCity(rs.getString("pmo_city"));
				dto.setCitySinhala(rs.getString("pmo_city_sinhala"));
				dto.setCityTamil(rs.getString("pmo_city_tamil"));

				dto.setBacklogAppValue(rs.getString("pmo_is_backlog_app"));
				dto.setCreateBy(rs.getString("pm_created_by"));
				dto.setCreateDate(rs.getTimestamp("pm_created_date"));
				dto.setModifyBy(user);
				dto.setModifyByTimestamp(timestamp);
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
	public void insertVehicleOwnerHistoryData(PermitRenewalsDTO dto) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_h_pm_vehi_owner");

			String sql = "INSERT INTO public.nt_h_pm_vehi_owner "
					+ "(seq, pmo_application_no, pmo_permit_no, pmo_vehicle_regno, pmo_preferred_language, pmo_title,"
					+ " pmo_full_name, pmo_full_name_sinhala, pmo_full_name_tamil, pmo_name_with_initial, pmo_nic, pmo_gender,"
					+ " pmo_dob, pmo_marital_status, pmo_telephone_no, pmo_mobile_no, pmo_province, pmo_district, pmo_div_sec,"
					+ " pmo_address1, pmo_address1_sinhala, pmo_address1_tamil, pmo_address2, pmo_address2_sinhala, pmo_address2_tamil, "
					+ " pmo_address3, pmo_address3_sinhala, pmo_address3_tamil, pmo_city, pmo_city_sinhala, pmo_city_tamil, "
					+ " pmo_is_backlog_app, pm_created_by, pm_created_date, pm_modified_by, pm_modified_date) "
					+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

			try {
				stmt = con.prepareStatement(sql);
	
				stmt.setLong(1, seqNo);
	
				stmt.setString(2, dto.getApplicationNo());
				stmt.setString(3, dto.getPermitNo());
				stmt.setString(4, dto.getRegNoOfBus());
				stmt.setString(5, dto.getPreferedLanguageCode());
				stmt.setString(6, dto.getTitleCode());
	
				stmt.setString(7, dto.getFullName());
				stmt.setString(8, dto.getFullNameSinhala());
				stmt.setString(9, dto.getFullNameTamil());
				stmt.setString(10, dto.getNameWithInitials());
				stmt.setString(11, dto.getNic());
				stmt.setString(12, dto.getGenderCode());
	
				stmt.setString(13, dto.getDob());
				stmt.setString(14, dto.getMaterialStatusId());
				stmt.setString(15, dto.getTeleNo());
				stmt.setString(16, dto.getMobileNo());
				stmt.setString(17, dto.getProvinceCode());
				stmt.setString(18, dto.getDistrictCode());
				stmt.setString(19, dto.getDivisionalSecretariatDivision());
	
				stmt.setString(20, dto.getAddressOne());
				stmt.setString(21, dto.getAddressOneSinhala());
				stmt.setString(22, dto.getAddressOneTamil());
				stmt.setString(23, dto.getAddressTwo());
				stmt.setString(24, dto.getAddressTwoSinhala());
				stmt.setString(25, dto.getAddressTwoTamil());
	
				stmt.setString(26, dto.getAddressThree());
				stmt.setString(27, dto.getAddressThreeSinhala());
				stmt.setString(28, dto.getAddressThreeTamil());
				stmt.setString(29, dto.getCity());
				stmt.setString(30, dto.getCitySinhala());
				stmt.setString(31, dto.getCityTamil());
	
				stmt.setString(32, dto.getBacklogAppValue());
				stmt.setString(33, dto.getCreateBy());
				stmt.setTimestamp(34, dto.getCreateDate());
				stmt.setString(35, dto.getModifyBy());
				stmt.setTimestamp(36, dto.getModifyByTimestamp());
	
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

	}

	@Override
	public OminiBusDTO getOminiBusTableData(String applicationNo, String user) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		OminiBusDTO dto = new OminiBusDTO();
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT seq, pmb_application_no, pmb_vehicle_regno, pmb_engine_no, pmb_chassis_no, pmb_make, pmb_model,"
					+ " pmb_seating_capacity, pmb_no_of_doors, pmb_height, pmb_production_date, pmb_weight, pmb_serial_no,"
					+ " pmb_certificate_date, pmb_garage_reg_no, pmb_insurance_company, pmb_insurance_cat, pmb_insurance_expire_date,"
					+ " pmb_bus_price, pmb_is_loan_obtain, pmb_finance_company, pmb_bank_loan_amt, pmb_due_amt, pmb_date_obtain, "
					+ " pmb_lapsed_installment, pmb_revenue_license_exp_date, pmb_first_reg_date, pmb_is_backlog_app, pmb_created_by, "
					+ " pmb_created_date, pmb_modified_by, pmb_modified_date, pmb_permit_no, pmb_policy_no, pmb_garage_name,  pmb_emission_test_exp_date "
					+ " FROM public.nt_t_pm_omini_bus1 where pmb_application_no=?; ";

			ps = con.prepareStatement(query);
			ps.setString(1, applicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				dto.setApplicationNo(rs.getString("pmb_application_no"));
				dto.setVehicleRegNo(rs.getString("pmb_vehicle_regno"));
				dto.setEngineNo(rs.getString("pmb_engine_no"));
				dto.setChassisNo(rs.getString("pmb_chassis_no"));
				dto.setMake(rs.getString("pmb_make"));
				dto.setModel(rs.getString("pmb_model"));

				dto.setSeating(rs.getString("pmb_seating_capacity"));
				dto.setNoofDoors(rs.getString("pmb_no_of_doors"));
				dto.setHeight(rs.getString("pmb_height"));
				dto.setManufactureDate(rs.getString("pmb_production_date"));
				dto.setWeight(rs.getString("pmb_weight"));
				dto.setSerialNo(rs.getString("pmb_serial_no"));

				dto.setFitnessCarficateDateVal(rs.getString("pmb_certificate_date"));
				dto.setGarageRegNo(rs.getString("pmb_garage_reg_no"));
				dto.setInsuCompName(rs.getString("pmb_insurance_company"));
				dto.setInsuCat(rs.getString("pmb_insurance_cat"));
				dto.setInsuExpiryDateVal(rs.getString("pmb_insurance_expire_date"));

				dto.setPriceValOfBus(rs.getBigDecimal("pmb_bus_price"));
				dto.setIsLoanObtained(rs.getString("pmb_is_loan_obtain"));
				dto.setFinanceCompany(rs.getString("pmb_finance_company"));
				dto.setBankLoan(rs.getBigDecimal("pmb_bank_loan_amt"));
				dto.setDueAmount(rs.getBigDecimal("pmb_due_amt"));
				dto.setDateObtainedVal(rs.getString("pmb_date_obtain"));

				dto.setLapsedInstall(rs.getString("pmb_lapsed_installment"));
				dto.setExpiryDateRevLicVal(rs.getString("pmb_revenue_license_exp_date"));
				dto.setDateOfFirstRegVal(rs.getString("pmb_first_reg_date"));
				dto.setIsBacklogApp(rs.getString("pmb_is_backlog_app"));
				dto.setCreatedBy(rs.getString("pmb_created_by"));

				dto.setCratedDate(rs.getTimestamp("pmb_created_date"));
				dto.setModifiedBy(user);
				dto.setModifiedDate(timestamp);
				dto.setPermitNo(rs.getString("pmb_permit_no"));
				dto.setPolicyNo(rs.getString("pmb_policy_no"));
				dto.setGarageName(rs.getString("pmb_garage_name"));
				dto.setRevenueExpDateVal(rs.getString("pmb_revenue_license_exp_date"));
				dto.setEmissionExpDateVal(rs.getString("pmb_emission_test_exp_date"));
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
	public void insertOminiBusHistoryData(OminiBusDTO dto) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_h_pm_omini_bus1");

			String sql = "INSERT INTO public.nt_h_pm_omini_bus1 "
					+ "(seq, pmb_application_no, pmb_vehicle_regno, pmb_engine_no, pmb_chassis_no, pmb_make, pmb_model,"
					+ " pmb_seating_capacity, pmb_no_of_doors, pmb_height, pmb_production_date, pmb_weight, pmb_serial_no,"
					+ " pmb_certificate_date, pmb_garage_reg_no, pmb_insurance_company, pmb_insurance_cat, pmb_insurance_expire_date,"
					+ " pmb_bus_price, pmb_is_loan_obtain, pmb_finance_company, pmb_bank_loan_amt, pmb_due_amt, pmb_date_obtain,"
					+ " pmb_lapsed_installment, pmb_revenue_license_exp_date, pmb_first_reg_date, pmb_is_backlog_app, pmb_created_by,"
					+ " pmb_created_date, pmb_modified_by, pmb_modified_date, pmb_permit_no, pmb_policy_no, pmb_garage_name, pmb_emission_test_exp_date ) "
					+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, dto.getApplicationNo());
			stmt.setString(3, dto.getVehicleRegNo());
			stmt.setString(4, dto.getEngineNo());
			stmt.setString(5, dto.getChassisNo());
			stmt.setString(6, dto.getMake());
			stmt.setString(7, dto.getModel());

			stmt.setString(8, dto.getSeating());
			stmt.setString(9, dto.getNoofDoors());
			stmt.setString(10, dto.getHeight());
			stmt.setString(11, dto.getManufactureDate());
			stmt.setString(12, dto.getWeight());
			stmt.setString(13, dto.getSerialNo());

			stmt.setString(14, dto.getFitnessCarficateDateVal());
			stmt.setString(15, dto.getGarageRegNo());
			stmt.setString(16, dto.getInsuCompName());
			stmt.setString(17, dto.getInsuCat());
			stmt.setString(18, dto.getInsuExpiryDateVal());

			stmt.setBigDecimal(19, dto.getPriceValOfBus());
			stmt.setString(20, dto.getIsLoanObtained());
			stmt.setString(21, dto.getFinanceCompany());
			stmt.setBigDecimal(22, dto.getBankLoan());
			stmt.setBigDecimal(23, dto.getDueAmount());
			stmt.setString(24, dto.getDateObtainedVal());

			stmt.setString(25, dto.getLapsedInstall());
			stmt.setString(26, dto.getExpiryDateRevLicVal());
			stmt.setString(27, dto.getDateOfFirstRegVal());
			stmt.setString(28, dto.getIsBacklogApp());
			stmt.setString(29, dto.getCreatedBy());

			stmt.setTimestamp(30, dto.getCratedDate());
			stmt.setString(31, dto.getModifiedBy());
			stmt.setTimestamp(32, dto.getModifiedDate());
			stmt.setString(33, dto.getPermitNo());
			stmt.setString(34, dto.getPolicyNo());
			stmt.setString(35, dto.getGarageName());

			// add new two fields
			stmt.setString(36, dto.getEmissionExpDateVal());

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
	public PermitRenewalsDTO getApplicationTableData(String applicationNo, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PermitRenewalsDTO dto = new PermitRenewalsDTO();
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT seq, pm_application_no, pm_queue_no, pm_permit_no, pm_vehicle_regno, pm_permit_issue_date, "
					+ " pm_permit_issue_by, pm_inspect_date, pm_tender_ref_no, pm_status, pm_tender_annual_fee, pm_installments,"
					+ " pm_is_permit_print, pm_reject_reason, pm_valid_from, pm_valid_to, pm_approved_by, pm_appoved_date, "
					+ " pm_special_remark, pm_is_tender_permit, pm_service_type, pm_route_no, pm_tot_bus_fare, pm_is_backlog_app,"
					+ " pm_created_by, pm_created_date, pm_modified_by, pm_modified_date, pm_permit_expire_date, pm_renewal_period,"
					+ " pm_next_ins_date_sec1_2, pm_next_ins_date_sec3, pm_inspect_remark, pm_route_flag, pm_isnew_permit,"
					+ " pm_reinspec_status, pm_old_permit_no, pm_temp_permit_no, pm_is_first_notice, pm_is_second_notice, "
					+ " pm_is_third_notice, pm_new_permit_expiry_date, pm_old_application_no, pm_skipped_inspection, pm_tran_type  "
					+ " FROM public.nt_t_pm_application where pm_application_no=? ";

			ps = con.prepareStatement(query);
			ps.setString(1, applicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {

				dto.setApplicationNo(rs.getString("pm_application_no"));
				dto.setQueueNo(rs.getString("pm_queue_no"));
				dto.setPermitNo(rs.getString("pm_permit_no"));
				dto.setRegNoOfBus(rs.getString("pm_vehicle_regno"));
				dto.setPermitIssueDateVal(rs.getString("pm_permit_issue_date"));

				dto.setPermitIssueBy(rs.getString("pm_permit_issue_by"));
				dto.setInspectionDate(rs.getString("pm_inspect_date"));
				dto.setTenderRefNo(rs.getString("pm_tender_ref_no"));
				dto.setStatus(rs.getString("pm_status"));
				dto.setTenderAnualFee(rs.getBigDecimal("pm_tender_annual_fee"));
				dto.setInstallment(rs.getBigDecimal("pm_installments"));

				dto.setPermitPrint(rs.getString("pm_is_permit_print"));
				dto.setRejectReason(rs.getString("pm_reject_reason"));
				dto.setValidFromDate(rs.getString("pm_valid_from"));
				dto.setValidToDate(rs.getString("pm_valid_to"));
				dto.setApproveBy(rs.getString("pm_approved_by"));
				dto.setApproveDate(rs.getString("pm_appoved_date"));

				dto.setSpecialRemark(rs.getString("pm_special_remark"));
				dto.setIsTenderPermit(rs.getString("pm_is_tender_permit"));
				dto.setServiceTypeCode(rs.getString("pm_service_type"));
				dto.setRouteNo(rs.getString("pm_route_no"));
				dto.setTotalBusFare(rs.getBigDecimal("pm_tot_bus_fare"));
				dto.setBacklogAppValue(rs.getString("pm_is_backlog_app"));

				dto.setCreateBy(rs.getString("pm_created_by"));
				dto.setCreateDate(rs.getTimestamp("pm_created_date"));
				dto.setModifyBy(user);
				dto.setModifyByTimestamp(timestamp);
				dto.setPermitExpiryDate(rs.getString("pm_permit_expire_date"));
				dto.setRequestRenewPeriod(rs.getInt("pm_renewal_period"));

				dto.setInspectionDate1(rs.getString("pm_next_ins_date_sec1_2"));
				dto.setInspectionDate2(rs.getString("pm_next_ins_date_sec3"));
				dto.setInspectionRemark(rs.getString("pm_inspect_remark"));
				dto.setRouteFlageValue(rs.getString("pm_route_flag"));
				dto.setIsNewPermit(rs.getString("pm_isnew_permit"));

				dto.setReInspectionStatus(rs.getString("pm_reinspec_status"));
				dto.setOldPermitNo(rs.getString("pm_old_permit_no"));
				dto.setTempPermitNo(rs.getString("pm_temp_permit_no"));
				dto.setFirstNotice(rs.getString("pm_is_first_notice"));
				dto.setSecondNotice(rs.getString("pm_is_second_notice"));

				dto.setThirdNotice(rs.getString("pm_is_third_notice"));
				dto.setNewPermitExpirtDate(rs.getString("pm_new_permit_expiry_date"));
				dto.setOldApplicationNo(rs.getString("pm_old_application_no"));
				dto.setIsSkipInspection(rs.getString("pm_skipped_inspection"));
				dto.setTranstractionTypeCode(rs.getString("pm_tran_type"));
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
	public boolean insertApplicationHistoryData(PermitRenewalsDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean success = true;
		try {
			con = ConnectionManager.getConnection();

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
			try {
				ps = con.prepareStatement(sql);

				ps.setLong(1, seqNo);
				ps.setString(2, dto.getApplicationNo());
				ps.setString(3, dto.getQueueNo());
				ps.setString(4, dto.getPermitNo());
				ps.setString(5, dto.getRegNoOfBus());
				ps.setString(6, dto.getPermitIssueDateVal());

				ps.setString(7, dto.getPermitIssueBy());
				ps.setString(8, dto.getInspectionDate());
				ps.setString(9, dto.getTenderRefNo());
				ps.setString(10, dto.getStatus());
				ps.setBigDecimal(11, dto.getTenderAnualFee());
				ps.setBigDecimal(12, dto.getInstallment());

				ps.setString(13, dto.getPermitPrint());
				ps.setString(14, dto.getRejectReason());
				ps.setString(15, dto.getValidFromDate());
				ps.setString(16, dto.getValidToDate());
				ps.setString(17, dto.getApproveBy());
				ps.setString(18, dto.getApproveDate());

				ps.setString(19, dto.getSpecialRemark());
				ps.setString(20, dto.getIsTenderPermit());
				ps.setString(21, dto.getServiceTypeCode());
				ps.setString(22, dto.getRouteNo());
				ps.setBigDecimal(23, dto.getTotalBusFare());

				ps.setString(24, dto.getBacklogAppValue());
				ps.setString(25, dto.getCreateBy());
				ps.setTimestamp(26, dto.getCreateDate());
				ps.setString(27, dto.getModifyBy());
				ps.setTimestamp(28, dto.getModifyByTimestamp());
				ps.setString(29, dto.getPermitExpiryDate());

				ps.setInt(30, dto.getRequestRenewPeriod());
				ps.setString(31, dto.getInspectionDate1());
				ps.setString(32, dto.getInspectionDate2());
				ps.setString(33, dto.getInspectionRemark());
				ps.setString(34, dto.getRouteFlageValue());

				ps.setString(35, dto.getIsNewPermit());
				ps.setString(36, dto.getReInspectionStatus());
				ps.setString(37, dto.getOldPermitNo());
				ps.setString(38, dto.getTempPermitNo());
				ps.setString(39, dto.getFirstNotice());

				ps.setString(40, dto.getSecondNotice());
				ps.setString(41, dto.getThirdNotice());
				ps.setString(42, dto.getNewPermitExpirtDate());
				ps.setString(43, dto.getOldApplicationNo());
				ps.setString(44, dto.getIsSkipInspection());
				ps.setString(45, dto.getTranstractionTypeCode());

				ps.executeUpdate();

				con.commit();
			} catch (SQLException e) {
				con.rollback();
				success = false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			success = false;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return success;
	}

	@Override
	public AmendmentDTO getAmendmentTableData(String applicationNo, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		AmendmentDTO dto = new AmendmentDTO();
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT seq, amd_queue_no, amd_permit_no, amd_application_no, amd_remarks, amd_service_change_type, "
					+ " amd_commity_approved, amd_board_approved, amd_status, amd_existing_bus_remark, amd_is_bus_running, "
					+ " amd_is_print, amd_hv_legal_case, amd_details_of_case, amd_committee_remark, amd_board_reject_reason, "

					+ " amd_busowner_remarks, amd_curr_expire_date, amd_new_expire_date, amd_relationship_with_transferor, "
					+ " amd_transferor_remarks, amd_reasonfor_ownerchange, amd_is_omnibus, amd_new_busno, amd_existing_busno, "
					+ " amd_created_by, amd_created_date, amd_modified_by, amd_modified_date, amd_trn_type, "
					+ " amd_first_reject_remarks, amd_pta, amd_time_approval FROM public.nt_m_amendments where amd_application_no=? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, applicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {

				dto.setQueueNo(rs.getString("amd_queue_no"));
				dto.setPermitNo(rs.getString("amd_permit_no"));
				dto.setApplicationNo(rs.getString("amd_application_no"));
				dto.setRemarks(rs.getString("amd_remarks"));
				dto.setServiceChangeType(rs.getString("amd_service_change_type"));

				dto.setCommitteeApproveStatus(rs.getString("amd_commity_approved"));
				dto.setBoardApproveStatus(rs.getString("amd_board_approved"));
				dto.setStatus(rs.getString("amd_status"));
				dto.setExistingBusRemark(rs.getString("amd_existing_bus_remark"));
				dto.setIsBusRunning(rs.getString("amd_is_bus_running"));

				dto.setIsPrint(rs.getString("amd_is_print"));
				dto.setHaveLegalCase(rs.getString("amd_hv_legal_case"));
				dto.setLegalCaseDetails(rs.getString("amd_details_of_case"));
				dto.setCommitteeReamrk(rs.getString("amd_committee_remark"));
				dto.setBoarderRejectReason(rs.getString("amd_board_reject_reason"));

				dto.setOwnerRemark(rs.getString("amd_busowner_remarks"));
				dto.setCurrentExpiryDate(rs.getString("amd_curr_expire_date"));
				dto.setNewExpiryDate(rs.getString("amd_new_expire_date"));
				dto.setRelationshipWithTransferor(rs.getString("amd_relationship_with_transferor"));

				dto.setRelationshipWithTransferorRemarks(rs.getString("amd_transferor_remarks"));
				dto.setReasonForOwnerChange(rs.getString("amd_reasonfor_ownerchange"));
				dto.setIsOminiBus(rs.getString("amd_is_omnibus"));
				dto.setNewBusNo(rs.getString("amd_new_busno"));
				dto.setExisitingBusNo(rs.getString("amd_existing_busno"));

				dto.setCreatedBy(rs.getString("amd_created_by"));
				dto.setCreatedDate(rs.getTimestamp("amd_created_date"));
				dto.setModifiedBy(user);
				dto.setModifiedDate(timestamp);
				dto.setTranCode(rs.getString("amd_trn_type"));

				dto.setFirstRejectRemark(rs.getString("amd_first_reject_remarks"));
				dto.setPtaStatus(rs.getString("amd_pta"));
				dto.setTimeApprovalStatus(rs.getString("amd_time_approval"));

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
	public void insertAmendmentsHistoryData(AmendmentDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_h_amendments");

			String sql = "INSERT INTO public.nt_h_amendments "
					+ "(seq, amd_queue_no, amd_permit_no, amd_application_no, amd_remarks, amd_service_change_type,"
					+ " amd_commity_approved, amd_board_approved, amd_status, amd_existing_bus_remark, amd_is_bus_running,"
					+ " amd_is_print, amd_hv_legal_case, amd_details_of_case, amd_committee_remark, amd_board_reject_reason,"
					+ " amd_busowner_remarks, amd_curr_expire_date, amd_new_expire_date, amd_relationship_with_transferor,"
					+ " amd_transferor_remarks, amd_reasonfor_ownerchange, amd_is_omnibus, amd_new_busno, amd_existing_busno,"
					+ " amd_created_by, amd_created_date, amd_modified_by, amd_modified_date, amd_trn_type, "
					+ " amd_first_reject_remarks, amd_pta, amd_time_approval,amd_timeapproval_service_change) "
					+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

			ps = con.prepareStatement(sql);

			ps.setLong(1, seqNo);
			ps.setString(2, dto.getQueueNo());
			ps.setString(3, dto.getPermitNo());
			ps.setString(4, dto.getApplicationNo());
			ps.setString(5, dto.getRemarks());
			ps.setString(6, dto.getServiceChangeType());

			ps.setString(7, dto.getCommitteeApproveStatus());
			ps.setString(8, dto.getBoardApproveStatus());
			ps.setString(9, dto.getStatus());
			ps.setString(10, dto.getExistingBusRemark());

			if (dto.getIsBusRunning() != null) {
				if (dto.getIsBusRunning().equals("true")) {
					ps.setString(11, "Y");
				} else {
					ps.setString(11, "N");
				}
			} else {
				ps.setString(11, "N");
			}
			ps.setString(12, dto.getIsPrint());

			if (dto.getHaveLegalCase() != null) {

				if (dto.getHaveLegalCase().equals("true")) {
					ps.setString(13, "Y");
				} else {
					ps.setString(13, "N");
				}
			}

			else {
				ps.setString(13, "N");
			}
			ps.setString(14, dto.getLegalCaseDetails());
			ps.setString(15, dto.getCommitteeReamrk());
			ps.setString(16, dto.getBoarderRejectReason());

			ps.setString(17, dto.getOwnerRemark());
			ps.setString(18, dto.getCurrentExpiryDate());
			ps.setString(19, dto.getNewExpiryDate());
			ps.setString(20, dto.getRelationshipWithTransferor());

			ps.setString(21, dto.getRelationshipWithTransferorRemarks());
			ps.setString(22, dto.getReasonForOwnerChange());
			// ps.setString(23, dto.getIsOminiBus());

			if (dto.getIsOminiBus() != null) {
				if (dto.getIsOminiBus().equals("true")) {
					ps.setString(23, "Y");
				} else {
					ps.setString(23, "N");
				}
			} else {
				ps.setString(23, "N");
			}
			ps.setString(24, dto.getNewBusNo());
			ps.setString(25, dto.getExisitingBusNo());

			ps.setString(26, dto.getCreatedBy());
			ps.setTimestamp(27, dto.getCratedDate());
			ps.setString(28, dto.getModifiedBy());
			ps.setTimestamp(29, dto.getModifiedDate());
			ps.setString(30, dto.getTranCode());

			ps.setString(31, dto.getFirstRejectRemark());
			ps.setString(32, dto.getPtaStatus());
			ps.setString(33, dto.getTimeApprovalStatus());
			ps.setString(34, dto.getTimeApprovalForServiceChange());

			ps.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

	}

	@Override
	public AmendmentServiceDTO getRouteRequsetedTableData(String applicationNo, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		AmendmentServiceDTO dto = new AmendmentServiceDTO();
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT seqno, trr_via, created_by, created_date, modified_by, modified_date, trr_origin, "
					+ " trr_destination, trr_application_no, trr_status FROM public.nt_temp_route_request "
					+ " where trr_application_no=?";

			ps = con.prepareStatement(query);
			ps.setString(1, applicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {

				dto.setVia(rs.getString("trr_via"));
				dto.setCreatedBy(rs.getString("created_by"));
				dto.setCreatedDate(rs.getTimestamp("created_date"));
				dto.setModifiedBy(user);
				dto.setModifiedDate(timestamp);
				dto.setOrigin(rs.getString("trr_origin"));

				dto.setDestination(rs.getString("trr_destination"));
				dto.setApplicationNo(rs.getString("trr_application_no"));
				dto.setStatus(rs.getString("trr_status"));
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
	public void insertRouteRequsetedHistoryData(AmendmentServiceDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_h_route_request");

			String sql = "INSERT INTO public.nt_h_route_request "
					+ "(seqno, trr_via, created_by, created_date, modified_by, modified_date, trr_origin, trr_destination,"
					+ " trr_application_no, trr_status) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

			ps = con.prepareStatement(sql);

			ps.setLong(1, seqNo);
			ps.setString(2, dto.getVia());
			ps.setString(3, dto.getCreatedBy());
			ps.setTimestamp(4, dto.getCreatedDate());
			ps.setString(5, dto.getModifiedBy());
			ps.setTimestamp(6, dto.getModifiedDate());
			ps.setString(7, dto.getOrigin());
			ps.setString(8, dto.getDestination());
			ps.setString(9, dto.getApplicationNo());
			ps.setString(10, dto.getStatus());

			ps.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);

		}

	}

	@Override
	public AccidentDTO getAccidentMasterTableData(String vehicleNo, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		AccidentDTO dto = new AccidentDTO();
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT seqno, acm_vehicleno, acm_isface_for_medi_test, acm_is_paricipate_training, "
					+ "acm_is_licence_cancelled, acm_cancel_period, acm_any_legalcase_notclosed, acm_created_by, "
					+ "acm_created_date, acm_modified_by, acm_modified_date FROM public.nt_t_accident_master where acm_vehicleno=?";

			ps = con.prepareStatement(query);
			ps.setString(1, vehicleNo);
			rs = ps.executeQuery();

			while (rs.next()) {

				dto.setVehicleNo(rs.getString("acm_vehicleno"));
				dto.setIsFacedMediTest(rs.getString("acm_isface_for_medi_test"));
				dto.setIsParicipateTraining(rs.getString("acm_is_paricipate_training"));
				dto.setIsLicenseCancelled(rs.getString("acm_is_licence_cancelled"));
				dto.setCancelPeriod(rs.getString("acm_cancel_period"));
				dto.setAnyLegalCaseNotClosed(rs.getString("acm_any_legalcase_notclosed"));
				dto.setCreatedBy(rs.getString("acm_created_by"));
				dto.setCreatedDate(rs.getTimestamp("acm_created_date"));
				dto.setModifiedBy(user);
				dto.setModifiedDate(timestamp);
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
	public void insertAccidentMasterHistoryData(AccidentDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_h_accident_master");

			String sql = "INSERT INTO public.nt_h_accident_master "
					+ "(seqno, acm_vehicleno, acm_isface_for_medi_test, acm_is_paricipate_training, acm_is_licence_cancelled, "
					+ "acm_cancel_period, acm_any_legalcase_notclosed, acm_created_by, acm_created_date,"
					+ " acm_modified_by, acm_modified_date) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?); ";

			ps = con.prepareStatement(sql);

			ps.setLong(1, seqNo);
			ps.setString(2, dto.getVehicleNo());
			ps.setString(3, dto.getIsFacedMediTest());
			ps.setString(4, dto.getIsParicipateTraining());
			ps.setString(5, dto.getIsLicenseCancelled());
			ps.setString(6, dto.getCancelPeriod());
			ps.setString(7, dto.getAnyLegalCaseNotClosed());
			ps.setString(8, dto.getCreatedBy());
			ps.setTimestamp(9, dto.getCreatedDate());
			ps.setString(10, dto.getModifiedBy());
			ps.setTimestamp(11, dto.getModifiedDate());

			ps.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);

		}

	}

	@Override
	public AmendmentDTO getNewOminiBusTableData(int value, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		AmendmentDTO dto = new AmendmentDTO();
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT seq, vehicle_no, application_no, amendment_seq, organization, route_no, permit_no, "
					+ "expiry_date, reason_for_not_renewed, created_by, created_date, modified_by, modified_date "
					+ "FROM public.nt_t_newomini_bus where seq=? ";

			ps = con.prepareStatement(query);
			ps.setLong(1, value);
			rs = ps.executeQuery();

			while (rs.next()) {

				dto.setSeq(rs.getLong("amendment_seq"));
				dto.setVehicleRegNo(rs.getString("vehicle_no"));
				dto.setApplicationNo(rs.getString("application_no"));
				dto.setAmendmentSeq(rs.getLong("amendment_seq"));
				dto.setOrganization(rs.getString("organization"));
				dto.setRouteNo(rs.getString("route_no"));
				dto.setPermitNo(rs.getString("permit_no"));
				dto.setExpireDate(rs.getString("expiry_date"));
				dto.setReasonForNotRenewed(rs.getString("reason_for_not_renewed"));
				dto.setCreatedBy(rs.getString("created_by"));
				dto.setCreatedDate(rs.getTimestamp("created_date"));
				dto.setModifiedBy(user);
				dto.setModifiedDate(timestamp);
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
	public void insertNewOminiBusHistoryData(AmendmentDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			/* long seqNo = Utils.getNextValBySeqName(con, "seq_nt_h_newomini_bus"); */

			String sql = "INSERT INTO public.nt_h_newomini_bus "
					+ "(seq, vehicle_no, application_no, amendment_seq, organization, route_no, permit_no, expiry_date, "
					+ " reason_for_not_renewed, created_by, created_date, modified_by, modified_date) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? );";

			ps = con.prepareStatement(sql);

			ps.setLong(1, dto.getSeq());
			ps.setString(2, dto.getVehicleRegNo());
			ps.setString(3, dto.getApplicationNo());
			ps.setLong(4, dto.getAmendmentSeq());
			ps.setString(5, dto.getOrganization());
			ps.setString(6, dto.getRouteNo());
			ps.setString(7, dto.getPermitNo());
			ps.setString(8, dto.getExpireDate());
			ps.setString(9, dto.getReasonForNotRenewed());
			ps.setString(10, dto.getCreatedBy());
			ps.setTimestamp(11, dto.getCreatedDate());
			ps.setString(12, dto.getModifiedBy());
			ps.setTimestamp(13, dto.getModifiedDate());

			ps.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);

		}

	}

	@Override
	public AccidentDTO getAccidentDetailsTableData(int value, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		AccidentDTO dto = new AccidentDTO();
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT seqno, acd_vehicleno, acd_accident_type_code, acd_description, acd_accident_date, "
					+ " acd_created_by, acd_created_date, acd_modified_by, acd_modified_date, acd_accident_mas_seq "
					+ " FROM public.nt_t_accident_details where seqno=? ; " + "";

			ps = con.prepareStatement(query);
			ps.setLong(1, value);
			rs = ps.executeQuery();

			while (rs.next()) {

				dto.setVehicleNo(rs.getString("acd_vehicleno"));
				dto.setAccidentType(rs.getString("acd_accident_type_code"));
				dto.setAccidentDesc(rs.getString("acd_description"));
				dto.setStrDateOfAccident(rs.getString("acd_accident_date"));
				dto.setCreatedBy(rs.getString("acd_created_by"));
				dto.setCreatedDate(rs.getTimestamp("acd_created_date"));
				dto.setModifiedBy(user);
				dto.setModifiedDate(timestamp);
				dto.setSeq(rs.getLong("acd_accident_mas_seq"));
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
	public void insertAccidentDetailsHistoryData(AccidentDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_h_newomini_bus");

			String sql = "INSERT INTO public.nt_h_accident_details "
					+ "(seqno, acd_vehicleno, acd_accident_type_code, acd_description, acd_accident_date, acd_created_by, "
					+ "acd_created_date, acd_modified_by, acd_modified_date, acd_accident_mas_seq) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?); ";

			ps = con.prepareStatement(sql);

			ps.setLong(1, seqNo);
			ps.setString(2, dto.getVehicleNo());
			ps.setString(3, dto.getAccidentType());
			ps.setString(4, dto.getAccidentDesc());
			ps.setString(5, dto.getStrDateOfAccident());
			ps.setString(6, dto.getCreatedBy());
			ps.setTimestamp(7, dto.getCreatedDate());
			ps.setString(8, dto.getModifiedBy());
			ps.setTimestamp(9, dto.getModifiedDate());
			ps.setLong(10, dto.getSeq());

			ps.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);

		}

	}

	@Override
	public AccidentDTO getLegalCasesTableData(int value, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		AccidentDTO dto = new AccidentDTO();
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT seqno, lec_vehicleno, lec_accident_date, lec_natureof_accident, lec_damage_propertyval, "
					+ " lec_noofinjured, lec_noofdeath, lec_created_by, lec_created_date, lec_modified_by, lec_modified_date "
					+ " FROM public.nt_t_legal_cases where seqno=?;";

			ps = con.prepareStatement(query);
			ps.setLong(1, value);
			rs = ps.executeQuery();

			while (rs.next()) {
				dto.setVehicleNo(rs.getString("lec_vehicleno"));
				dto.setStrDateOfAccident(rs.getString("lec_accident_date"));
				dto.setNatureOfAccident(rs.getString("lec_natureof_accident"));
				dto.setValueOfPropertyDamaged(rs.getBigDecimal("lec_damage_propertyval"));
				dto.setNoOfInjured(rs.getInt("lec_noofinjured"));
				dto.setNoOfDeaths(rs.getInt("lec_noofdeath"));
				dto.setCreatedBy(rs.getString("lec_created_by"));
				dto.setCreatedDate(rs.getTimestamp("lec_created_date"));
				dto.setModifiedBy(user);
				dto.setModifiedDate(timestamp);

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
	public void insertLegalCasesHistoryData(AccidentDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_h_legal_cases");

			String sql = "INSERT INTO public.nt_h_legal_cases "
					+ "(seqno, lec_vehicleno, lec_accident_date, lec_natureof_accident, lec_damage_propertyval, lec_noofinjured,"
					+ " lec_noofdeath, lec_created_by, lec_created_date, lec_modified_by, lec_modified_date) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

			ps = con.prepareStatement(sql);

			ps.setLong(1, seqNo);
			ps.setString(2, dto.getVehicleNo());
			ps.setString(3, dto.getStrDateOfAccident());
			ps.setString(4, dto.getNatureOfAccident());
			ps.setBigDecimal(5, dto.getValueOfPropertyDamaged());
			ps.setInt(6, dto.getNoOfInjured());
			ps.setInt(7, dto.getNoOfDeaths());
			ps.setString(8, dto.getCreatedBy());
			ps.setTimestamp(9, dto.getCreatedDate());
			ps.setString(10, dto.getModifiedBy());
			ps.setTimestamp(11, dto.getModifiedDate());

			ps.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);

		}

	}

	@Override
	public IssuePermitDTO getServiceTableData(String applicationNO, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		IssuePermitDTO dto = new IssuePermitDTO();
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT seq, ser_application_no, ser_vehicle_regno, ser_routeno, ser_service_div_sec, "
					+ "ser_route_length, ser_no_of_trips, ser_parking_place, ser_parking_div_sec, ser_parking_des_length, "
					+ "ser_created_by, ser_pmb_created_date, ser_modified_by, ser_modified_date, ser_permit_no "
					+ "FROM public.nt_t_pm_service where ser_application_no=?";

			ps = con.prepareStatement(query);
			ps.setString(1, applicationNO);
			rs = ps.executeQuery();

			while (rs.next()) {

				dto.setApplicationNo(rs.getString("ser_application_no"));
				dto.setBusRegNo(rs.getString("ser_vehicle_regno"));
				dto.setRouteNo(rs.getString("ser_routeno"));
				dto.setServiceDivSec(rs.getString("ser_service_div_sec"));

				dto.setRouteLength(rs.getFloat("ser_route_length"));
				dto.setTrips(rs.getInt("ser_no_of_trips"));
				dto.setParkingPlace(rs.getString("ser_parking_place"));
				dto.setParkingDivSec(rs.getString("ser_parking_div_sec"));
				dto.setParkingDesLength(rs.getFloat("ser_parking_des_length"));

				dto.setCreatedBy(rs.getString("ser_created_by"));
				dto.setCreatedDate(rs.getTimestamp("ser_pmb_created_date"));
				dto.setModifiedBy(user);
				dto.setModifiedDate(timestamp);

				dto.setPermitNo(rs.getString("ser_permit_no"));

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
	public void insertServiceHistoryData(IssuePermitDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_h_pm_service");

			String sql = "INSERT INTO public.nt_h_pm_service "
					+ "(seq, ser_application_no, ser_vehicle_regno, ser_routeno, ser_service_div_sec, ser_route_length, "
					+ "ser_no_of_trips, ser_parking_place, ser_parking_div_sec, ser_parking_des_length, ser_created_by,"
					+ " ser_pmb_created_date, ser_modified_by, ser_modified_date, ser_permit_no) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?); ";

			ps = con.prepareStatement(sql);

			ps.setLong(1, seqNo);
			ps.setString(2, dto.getApplicationNo());
			ps.setString(3, dto.getBusRegNo());
			ps.setString(4, dto.getRouteNo());
			ps.setString(5, dto.getServiceDivSec());
			ps.setFloat(6, dto.getRouteLength());

			ps.setInt(7, dto.getTrips());
			ps.setString(8, dto.getParkingPlace());
			ps.setString(9, dto.getParkingDivSec());
			ps.setFloat(10, dto.getParkingDesLength());
			ps.setString(11, dto.getCreatedBy());

			ps.setTimestamp(12, dto.getCreatedDate());
			ps.setString(13, dto.getModifiedBy());
			ps.setTimestamp(14, dto.getModifiedDate());
			ps.setString(15, dto.getPermitNo());

			ps.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);

		}

	}

	@Override
	public void insertApplicationAndOminiBusHistory(PermitRenewalsDTO applicationHistoryDTO,
			OminiBusDTO ominiBusHistoryDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

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

			ps = con.prepareStatement(sql);

			ps.setLong(1, seqNo);
			ps.setString(2, applicationHistoryDTO.getApplicationNo());
			ps.setString(3, applicationHistoryDTO.getQueueNo());
			ps.setString(4, applicationHistoryDTO.getPermitNo());
			ps.setString(5, applicationHistoryDTO.getRegNoOfBus());
			ps.setString(6, applicationHistoryDTO.getPermitIssueDateVal());

			ps.setString(7, applicationHistoryDTO.getPermitIssueBy());
			ps.setString(8, applicationHistoryDTO.getInspectionDate());
			ps.setString(9, applicationHistoryDTO.getTenderRefNo());
			ps.setString(10, applicationHistoryDTO.getStatus());
			ps.setBigDecimal(11, applicationHistoryDTO.getTenderAnualFee());
			ps.setBigDecimal(12, applicationHistoryDTO.getInstallment());

			ps.setString(13, applicationHistoryDTO.getPermitPrint());
			ps.setString(14, applicationHistoryDTO.getRejectReason());
			ps.setString(15, applicationHistoryDTO.getValidFromDate());
			ps.setString(16, applicationHistoryDTO.getValidToDate());
			ps.setString(17, applicationHistoryDTO.getApproveBy());
			ps.setString(18, applicationHistoryDTO.getApproveDate());

			ps.setString(19, applicationHistoryDTO.getSpecialRemark());
			ps.setString(20, applicationHistoryDTO.getIsTenderPermit());
			ps.setString(21, applicationHistoryDTO.getServiceTypeCode());
			ps.setString(22, applicationHistoryDTO.getRouteNo());
			ps.setBigDecimal(23, applicationHistoryDTO.getTotalBusFare());

			ps.setString(24, applicationHistoryDTO.getBacklogAppValue());
			ps.setString(25, applicationHistoryDTO.getCreateBy());
			ps.setTimestamp(26, applicationHistoryDTO.getCreateDate());
			ps.setString(27, applicationHistoryDTO.getModifyBy());
			ps.setTimestamp(28, applicationHistoryDTO.getModifyByTimestamp());
			ps.setString(29, applicationHistoryDTO.getPermitExpiryDate());

			ps.setInt(30, applicationHistoryDTO.getRequestRenewPeriod());
			ps.setString(31, applicationHistoryDTO.getInspectionDate1());
			ps.setString(32, applicationHistoryDTO.getInspectionDate2());
			ps.setString(33, applicationHistoryDTO.getInspectionRemark());
			ps.setString(34, applicationHistoryDTO.getRouteFlageValue());

			ps.setString(35, applicationHistoryDTO.getIsNewPermit());
			ps.setString(36, applicationHistoryDTO.getReInspectionStatus());
			ps.setString(37, applicationHistoryDTO.getOldPermitNo());
			ps.setString(38, applicationHistoryDTO.getTempPermitNo());
			ps.setString(39, applicationHistoryDTO.getFirstNotice());

			ps.setString(40, applicationHistoryDTO.getSecondNotice());
			ps.setString(41, applicationHistoryDTO.getThirdNotice());
			ps.setString(42, applicationHistoryDTO.getNewPermitExpirtDate());
			ps.setString(43, applicationHistoryDTO.getOldApplicationNo());
			ps.setString(44, applicationHistoryDTO.getIsSkipInspection());
			ps.setString(45, applicationHistoryDTO.getTranstractionTypeCode());

			ps.executeUpdate();
			
			
//			insert omini bus history
			insertOminiBusHistoryData(ominiBusHistoryDTO, con);

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

	}
	
	private void insertOminiBusHistoryData(OminiBusDTO dto, Connection con) throws Exception {
		PreparedStatement stmtOminiBus = null;

		long seqNo = Utils.getNextValBySeqName(con, "seq_nt_h_pm_omini_bus1");

		String sql = "INSERT INTO public.nt_h_pm_omini_bus1 "
				+ "(seq, pmb_application_no, pmb_vehicle_regno, pmb_engine_no, pmb_chassis_no, pmb_make, pmb_model,"
				+ " pmb_seating_capacity, pmb_no_of_doors, pmb_height, pmb_production_date, pmb_weight, pmb_serial_no,"
				+ " pmb_certificate_date, pmb_garage_reg_no, pmb_insurance_company, pmb_insurance_cat, pmb_insurance_expire_date,"
				+ " pmb_bus_price, pmb_is_loan_obtain, pmb_finance_company, pmb_bank_loan_amt, pmb_due_amt, pmb_date_obtain,"
				+ " pmb_lapsed_installment, pmb_revenue_license_exp_date, pmb_first_reg_date, pmb_is_backlog_app, pmb_created_by,"
				+ " pmb_created_date, pmb_modified_by, pmb_modified_date, pmb_permit_no, pmb_policy_no, pmb_garage_name, pmb_emission_test_exp_date ) "
				+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

		stmtOminiBus = con.prepareStatement(sql);

		stmtOminiBus.setLong(1, seqNo);
		stmtOminiBus.setString(2, dto.getApplicationNo());
		stmtOminiBus.setString(3, dto.getVehicleRegNo());
		stmtOminiBus.setString(4, dto.getEngineNo());
		stmtOminiBus.setString(5, dto.getChassisNo());
		stmtOminiBus.setString(6, dto.getMake());
		stmtOminiBus.setString(7, dto.getModel());

		stmtOminiBus.setString(8, dto.getSeating());
		stmtOminiBus.setString(9, dto.getNoofDoors());
		stmtOminiBus.setString(10, dto.getHeight());
		stmtOminiBus.setString(11, dto.getManufactureDate());
		stmtOminiBus.setString(12, dto.getWeight());
		stmtOminiBus.setString(13, dto.getSerialNo());

		stmtOminiBus.setString(14, dto.getFitnessCarficateDateVal());
		stmtOminiBus.setString(15, dto.getGarageRegNo());
		stmtOminiBus.setString(16, dto.getInsuCompName());
		stmtOminiBus.setString(17, dto.getInsuCat());
		stmtOminiBus.setString(18, dto.getInsuExpiryDateVal());

		stmtOminiBus.setBigDecimal(19, dto.getPriceValOfBus());
		stmtOminiBus.setString(20, dto.getIsLoanObtained());
		stmtOminiBus.setString(21, dto.getFinanceCompany());
		stmtOminiBus.setBigDecimal(22, dto.getBankLoan());
		stmtOminiBus.setBigDecimal(23, dto.getDueAmount());
		stmtOminiBus.setString(24, dto.getDateObtainedVal());

		stmtOminiBus.setString(25, dto.getLapsedInstall());
		stmtOminiBus.setString(26, dto.getExpiryDateRevLicVal());
		stmtOminiBus.setString(27, dto.getDateOfFirstRegVal());
		stmtOminiBus.setString(28, dto.getIsBacklogApp());
		stmtOminiBus.setString(29, dto.getCreatedBy());

		stmtOminiBus.setTimestamp(30, dto.getCratedDate());
		stmtOminiBus.setString(31, dto.getModifiedBy());
		stmtOminiBus.setTimestamp(32, dto.getModifiedDate());
		stmtOminiBus.setString(33, dto.getPermitNo());
		stmtOminiBus.setString(34, dto.getPolicyNo());
		stmtOminiBus.setString(35, dto.getGarageName());

		stmtOminiBus.setString(36, dto.getEmissionExpDateVal());

		stmtOminiBus.executeUpdate();

		ConnectionManager.close(stmtOminiBus);

	}

}
