package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.informatics.ntc.model.dto.AccidentDTO;
import lk.informatics.ntc.model.dto.AfterAccidentDTO;
import lk.informatics.ntc.model.dto.AmendmentBusOwnerDTO;
import lk.informatics.ntc.model.dto.AmendmentDTO;
import lk.informatics.ntc.model.dto.AmendmentOminiBusDTO;
import lk.informatics.ntc.model.dto.AmendmentServiceDTO;
import lk.informatics.ntc.model.dto.BusOwnerDTO;
import lk.informatics.ntc.model.dto.CommitteeOrBoardApprovalDTO;
import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.OminiBusDTO;
import lk.informatics.ntc.model.dto.OrganizationDTO;
import lk.informatics.ntc.model.dto.PermitRenewalsDTO;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.roc.utils.common.Utils;

public class AmendmentServiceImpl implements AmendmentService {

	private static final long serialVersionUID = 1L;

	@Override
	public BusOwnerDTO getOwnerDetailsByVehicleNo(String strVehicleNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		BusOwnerDTO busOwnerDTO = new BusOwnerDTO();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT pmo_application_no,pmo_permit_no,pmo_preferred_language,pmo_title,pmo_gender,pmo_dob,pmo_nic,pmo_full_name,"
					+ " pmo_full_name_sinhala, pmo_full_name_tamil,pmo_name_with_initial,pmo_marital_status,"
					+ " pmo_telephone_no,pmo_mobile_no,pmo_address1,pmo_address1_sinhala,pmo_address1_tamil,"
					+ " pmo_address2,pmo_address2_sinhala,pmo_address2_tamil,pmo_city,pmo_city_sinhala,"
					+ " pmo_city_tamil,pmo_province,pmo_district,pmo_div_sec " + " FROM public.nt_t_pm_vehi_owner "
					+ " WHERE pmo_vehicle_regno =  ? ";

			ps = con.prepareStatement(query);
			ps.setString(1, strVehicleNo);
			rs = ps.executeQuery();

			while (rs.next()) {

				busOwnerDTO.setApplicationNo(rs.getString("pmo_application_no"));
				busOwnerDTO.setPermitNo(rs.getString("pmo_permit_no"));
				busOwnerDTO.setPerferedLanguage(rs.getString("pmo_preferred_language"));
				busOwnerDTO.setTitle(rs.getString("pmo_title"));
				busOwnerDTO.setGender(rs.getString("pmo_gender"));
				String dobString = rs.getString("pmo_dob");
				if (dobString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date dob = originalFormat.parse(dobString);
					busOwnerDTO.setDob(dob);
					busOwnerDTO.setStrStringDob(dobString);
				}

				busOwnerDTO.setFullName(rs.getString("pmo_full_name"));
				busOwnerDTO.setFullNameSinhala(rs.getString("pmo_full_name_sinhala"));
				busOwnerDTO.setFullNameTamil(rs.getString("pmo_full_name_tamil"));
				busOwnerDTO.setNameWithInitials(rs.getString("pmo_name_with_initial"));
				busOwnerDTO.setMaritalStatus(rs.getString("pmo_marital_status"));
				busOwnerDTO.setTelephoneNo(rs.getString("pmo_telephone_no"));
				busOwnerDTO.setMobileNo(rs.getString("pmo_mobile_no"));
				busOwnerDTO.setAddress1(rs.getString("pmo_address1"));
				busOwnerDTO.setAddress1Sinhala(rs.getString("pmo_address1_sinhala"));
				busOwnerDTO.setAddress1Tamil(rs.getString("pmo_address1_tamil"));
				busOwnerDTO.setAddress2(rs.getString("pmo_address2"));
				busOwnerDTO.setAddress2Sinhala(rs.getString("pmo_address2_sinhala"));
				busOwnerDTO.setAddress2Tamil(rs.getString("pmo_address2_tamil"));
				busOwnerDTO.setCity(rs.getString("pmo_city"));
				busOwnerDTO.setCitySinhala(rs.getString("pmo_city_sinhala"));
				busOwnerDTO.setCityTamil(rs.getString("pmo_city_tamil"));
				busOwnerDTO.setProvince(rs.getString("pmo_province"));
				busOwnerDTO.setDistrict(rs.getString("pmo_district"));
				busOwnerDTO.setDivSec(rs.getString("pmo_div_sec"));
				busOwnerDTO.setNicNo(rs.getString("pmo_nic"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return busOwnerDTO;

	}

	@Override
	public AmendmentBusOwnerDTO getApplicationDetailsByVehicleNo(String strVehicleNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		AmendmentBusOwnerDTO amendmentDTO = new AmendmentBusOwnerDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT C.pmo_is_backlog_app,A.pm_permit_no,A.pm_application_no,B.description,A.pm_vehicle_regno,A.pm_is_tender_permit\r\n"
					+ ",A.pm_permit_expire_date,A.pm_special_remark,\r\n"
					+ "C.pmo_full_name,C.pmo_telephone_no,C.pmo_address1,C.pmo_mobile_no,C.pmo_address2,D.description \r\n"
					+ "AS pmo_province,pmo_city,F.description AS pmo_district,C.pmo_nic,G.ds_description AS pmo_div_sec,\r\n"
					+ "A.pm_route_no,A.pm_route_flag,R.rou_service_origine,R.rou_service_destination,R.rou_via,A.pm_route_no,R.rou_distance\r\n"
					+ " FROM public.nt_t_pm_application A\r\n"
					+ " LEFT JOIN nt_r_service_types B ON A.pm_service_type=B.code \r\n"
					+ " LEFT JOIN nt_t_pm_vehi_owner C ON A.pm_application_no=C.pmo_application_no\r\n"
					+ " LEFT JOIN nt_r_province D ON C.pmo_province=D.code \r\n"
					+ " FULL OUTER JOIN nt_r_district F ON C.pmo_district=F.code \r\n"
					+ " FULL OUTER JOIN nt_r_div_sec G ON C.pmo_div_sec=G.ds_code \r\n"
					+ " inner join public.nt_r_route R on A.pm_route_no= R.rou_number\r\n"
					+ " WHERE pm_status='A' AND pm_vehicle_regno =? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, strVehicleNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				boolean isTenderPermit = false;

				amendmentDTO.setPermitNo(rs.getString("pm_permit_no"));
				amendmentDTO.setApplicationNo(rs.getString("pm_application_no"));
				amendmentDTO.setServiceType(rs.getString("description"));
				amendmentDTO.setBusRegNo(rs.getString("pm_vehicle_regno"));

				if (!(rs.getString("pm_is_tender_permit") == null) && !rs.getString("pm_is_tender_permit").isEmpty()
						&& rs.getString("pm_is_tender_permit").equalsIgnoreCase("Y")) {
					isTenderPermit = true;
				} else {
					isTenderPermit = false;
				}
				amendmentDTO.setTenderPermit(isTenderPermit);
				amendmentDTO.setExpireDate(rs.getString("pm_permit_expire_date"));
				amendmentDTO.setFinalRemark(rs.getString("pm_special_remark"));
				amendmentDTO.setFullName(rs.getString("pmo_full_name"));
				amendmentDTO.setTelephoneNo(rs.getString("pmo_telephone_no"));
				amendmentDTO.setAddress1(rs.getString("pmo_address1"));
				amendmentDTO.setMobileNo(rs.getString("pmo_mobile_no"));
				amendmentDTO.setAddress2(rs.getString("pmo_address2"));
				amendmentDTO.setProvince(rs.getString("pmo_province"));
				amendmentDTO.setCity(rs.getString("pmo_city"));
				amendmentDTO.setDistrict(rs.getString("pmo_district"));
				amendmentDTO.setNicNo(rs.getString("pmo_nic"));
				amendmentDTO.setDivSec(rs.getString("pmo_div_sec"));
				amendmentDTO.setIsBacklogApp(rs.getString("pmo_is_backlog_app"));
				amendmentDTO.setOrigin(rs.getString("rou_service_origine"));
				amendmentDTO.setDestination(rs.getString("rou_service_destination"));
				amendmentDTO.setVia(rs.getString("rou_via"));
				amendmentDTO.setRouteFlag(rs.getString("pm_route_flag"));
				amendmentDTO.setRouteNo(rs.getString("pm_route_no"));
				amendmentDTO.setDistance(rs.getBigDecimal("rou_distance"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return amendmentDTO;

	}

	@Override
	public AmendmentOminiBusDTO ominiBusByVehicleNo(String strVehicleNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		AmendmentOminiBusDTO amendmentDTO = new AmendmentOminiBusDTO();
		try {
			con = ConnectionManager.getConnection();
			
			
			
			String query =

					"SELECT B.pm_special_remark,B.pm_route_no,B.pm_route_flag,C.description AS pm_service_type_desc,B.pm_service_type as pm_service_type,pmb_application_no, pmb_vehicle_regno,"
							+ "pmb_seating_capacity, pmb_no_of_doors, pmb_height, pmb_policy_no, "
							+ "pmb_production_date, pmb_weight, pmb_serial_no, pmb_certificate_date, pmb_garage_reg_no,"
							+ "pmb_insurance_company, pmb_insurance_cat, pmb_insurance_expire_date, pmb_bus_price,"
							+ "pmb_is_loan_obtain, pmb_finance_company, pmb_bank_loan_amt, pmb_due_amt, pmb_date_obtain,"
							+ "pmb_lapsed_installment, pmb_revenue_license_exp_date, pmb_first_reg_date,"
							+ "pmb_permit_no,pmb_emission_test_exp_date"
							+ " FROM public.nt_t_pm_omini_bus1 A INNER JOIN nt_t_pm_application B ON A.pmb_application_no =B.pm_application_no  INNER JOIN nt_r_service_types C ON B.pm_service_type=C.code"
							+ " WHERE pmb_vehicle_regno =  ? and  B.pm_status ='A' limit 1";

			ps = con.prepareStatement(query);
			ps.setString(1, strVehicleNo);
			rs = ps.executeQuery();

			while (rs.next()) {

				amendmentDTO.setApplicationNo(rs.getString("pmb_application_no"));
				amendmentDTO.setVehicleRegNo(rs.getString("pmb_vehicle_regno"));
				amendmentDTO.setSeating(rs.getString("pmb_seating_capacity"));
				amendmentDTO.setNoofDoors(rs.getString("pmb_no_of_doors"));
				amendmentDTO.setHeight(rs.getString("pmb_height"));
				amendmentDTO.setManufactureDate(rs.getString("pmb_production_date"));
				amendmentDTO.setWeight(rs.getString("pmb_weight"));
				amendmentDTO.setSerialNo(rs.getString("pmb_serial_no"));

				String pmb_certificate_dateString = rs.getString("pmb_certificate_date");
				if (pmb_certificate_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setFitnessCertiDate = originalFormat.parse(pmb_certificate_dateString);
					amendmentDTO.setFitnessCertiDate(setFitnessCertiDate);
				}

				amendmentDTO.setGarageRegNo(rs.getString("pmb_garage_reg_no"));
				amendmentDTO.setGarageName(rs.getString("pmb_garage_reg_no"));
				amendmentDTO.setInsuCompName(rs.getString("pmb_insurance_company"));
				amendmentDTO.setInsuCat(rs.getString("pmb_insurance_cat"));
				amendmentDTO.setPolicyNo(rs.getString("pmb_policy_no"));

				String pmb_insurance_expire_dateString = rs.getString("pmb_insurance_expire_date");
				if (pmb_insurance_expire_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setInsuExpDate = originalFormat.parse(pmb_insurance_expire_dateString);
					amendmentDTO.setInsuExpDate(setInsuExpDate);
				}

				amendmentDTO.setPriceValOfBus(rs.getBigDecimal("pmb_bus_price"));
				amendmentDTO.setIsLoanObtained(rs.getString("pmb_is_loan_obtain"));
				amendmentDTO.setFinanceCompany(rs.getString("pmb_finance_company"));
				amendmentDTO.setBankLoan(rs.getBigDecimal("pmb_bank_loan_amt"));
				amendmentDTO.setDueAmount(rs.getBigDecimal("pmb_due_amt"));

				String pmb_date_obtainString = rs.getString("pmb_date_obtain");
				if (pmb_date_obtainString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setDateObtained = originalFormat.parse(pmb_date_obtainString);
					amendmentDTO.setDateObtained(setDateObtained);
				}

				amendmentDTO.setLapsedInstall(rs.getString("pmb_lapsed_installment"));

				String pmb_revenue_license_exp_dateString = rs.getString("pmb_revenue_license_exp_date");
				if (pmb_revenue_license_exp_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setExpiryDateRevLic = originalFormat.parse(pmb_revenue_license_exp_dateString);
					amendmentDTO.setExpiryDateRevLic(setExpiryDateRevLic);
				}

				String pmb_first_reg_dateString = rs.getString("pmb_first_reg_date");
				if (pmb_first_reg_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setDateOfFirstReg = originalFormat.parse(pmb_first_reg_dateString);
					amendmentDTO.setDateOfFirstReg(setDateOfFirstReg);
				}

				amendmentDTO.setPermitNo(rs.getString("pmb_permit_no"));
				amendmentDTO.setServiceType(rs.getString("pm_service_type"));
				amendmentDTO.setServiceTypeDesc(rs.getString("pm_service_type_desc"));
				amendmentDTO.setRouteNo(rs.getString("pm_route_no"));
				amendmentDTO.setRouteFlag(rs.getString("pm_route_flag"));
				amendmentDTO.setRemarks(rs.getString("pm_special_remark"));

				String pmb_emission_test_exp_dateString = rs.getString("pmb_emission_test_exp_date");
				if (pmb_emission_test_exp_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setExpiryDateEmissionTest = originalFormat.parse(pmb_emission_test_exp_dateString);
					amendmentDTO.setExpiryDateEmissionTest(setExpiryDateEmissionTest);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return amendmentDTO;

	}

	@Override
	public void saveOwnerDetails(BusOwnerDTO busOwnerDTO) {
		java.util.Date date = new java.util.Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "INSERT INTO nt_t_pm_vehi_owner (seq ,pmo_preferred_language ,pmo_permit_no"
					+ ",pmo_title ,pmo_gender ,pmo_dob ,pmo_nic ,pmo_full_name"
					+ ",pmo_full_name_sinhala ,pmo_full_name_tamil ,pmo_name_with_initial ,pmo_address1"
					+ ",pmo_address1_sinhala ,pmo_address1_tamil ,pmo_address2 ,pmo_address2_sinhala"
					+ ",pmo_address2_tamil ,pmo_address3 ,pmo_address3_sinhala ,pmo_address3_tamil"
					+ ",pmo_city ,pmo_city_sinhala ,pmo_city_tamil ,pmo_marital_status"
					+ ",pmo_telephone_no ,pmo_mobile_no ,pmo_province ,pmo_district ,pmo_div_sec,pm_created_by,pm_created_date,pmo_is_backlog_app,pmo_application_no,pmo_vehicle_regno)"
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			stmt = con.prepareStatement(sql);
			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_pm_vehi_owner");

			stmt.setLong(1, seqNo);
			stmt.setString(2, busOwnerDTO.getPerferedLanguage());
			stmt.setString(3, busOwnerDTO.getPermitNo());
			stmt.setString(4, busOwnerDTO.getTitle());
			stmt.setString(5, busOwnerDTO.getGender());

			if (busOwnerDTO.getDob() != null) {
				String dob = (dateFormat.format(busOwnerDTO.getDob()));
				stmt.setString(6, dob);
			} else {
				stmt.setString(6, null);
			}

			stmt.setString(7, busOwnerDTO.getNicNo());
			stmt.setString(8, busOwnerDTO.getFullName());
			stmt.setString(9, busOwnerDTO.getFullNameSinhala());
			stmt.setString(10, busOwnerDTO.getFullNameTamil());
			stmt.setString(11, busOwnerDTO.getNameWithInitials());
			stmt.setString(12, busOwnerDTO.getAddress1());
			stmt.setString(13, busOwnerDTO.getAddress1Sinhala());
			stmt.setString(14, busOwnerDTO.getAddress1Tamil());
			stmt.setString(15, busOwnerDTO.getAddress2());
			stmt.setString(16, busOwnerDTO.getAddress2Sinhala());
			stmt.setString(17, busOwnerDTO.getAddress2Tamil());
			stmt.setString(18, busOwnerDTO.getAddress3());
			stmt.setString(19, busOwnerDTO.getAddress3Sinhala());
			stmt.setString(20, busOwnerDTO.getAddress3Tamil());
			stmt.setString(21, busOwnerDTO.getCity());
			stmt.setString(22, busOwnerDTO.getCitySinhala());
			stmt.setString(23, busOwnerDTO.getCityTamil());
			stmt.setString(24, busOwnerDTO.getMaritalStatus());
			stmt.setString(25, busOwnerDTO.getTelephoneNo());
			stmt.setString(26, busOwnerDTO.getMobileNo());
			stmt.setString(27, busOwnerDTO.getProvince());
			stmt.setString(28, busOwnerDTO.getDistrict());
			stmt.setString(29, busOwnerDTO.getDivSec());
			stmt.setString(30, busOwnerDTO.getCreatedBy());
			stmt.setTimestamp(31, timestamp);
			stmt.setString(32, busOwnerDTO.getIsBacklogApp());
			stmt.setString(33, busOwnerDTO.getApplicationNo());
			stmt.setString(34, busOwnerDTO.getBusRegNo());

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
	public BusOwnerDTO getOwnerDetails(String strApplicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		BusOwnerDTO busOwnerDTO = new BusOwnerDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT pmo_preferred_language,pmo_title,pmo_gender,pmo_dob,pmo_nic,pmo_full_name,"
					+ " pmo_full_name_sinhala, pmo_full_name_tamil,pmo_name_with_initial,pmo_marital_status,"
					+ " pmo_telephone_no,pmo_mobile_no,pmo_address1,pmo_address1_sinhala,pmo_address1_tamil,"
					+ " pmo_address2,pmo_address2_sinhala,pmo_address2_tamil,pmo_city,pmo_city_sinhala,"
					+ " pmo_city_tamil,pmo_province,pmo_district,pmo_div_sec,seq " + " FROM public.nt_t_pm_vehi_owner "
					+ " WHERE pmo_application_no =  ? ";

			ps = con.prepareStatement(query);
			ps.setString(1, strApplicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				busOwnerDTO.setPerferedLanguage(rs.getString("pmo_preferred_language"));
				busOwnerDTO.setTitle(rs.getString("pmo_title"));
				busOwnerDTO.setGender(rs.getString("pmo_gender"));
				String dobString = rs.getString("pmo_dob");
				if (dobString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date dob = originalFormat.parse(dobString);
					busOwnerDTO.setDob(dob);
					busOwnerDTO.setStrStringDob(dobString);

				}
				busOwnerDTO.setFullName(rs.getString("pmo_full_name"));
				busOwnerDTO.setFullNameSinhala(rs.getString("pmo_full_name_sinhala"));
				busOwnerDTO.setFullNameTamil(rs.getString("pmo_full_name_tamil"));
				busOwnerDTO.setNameWithInitials(rs.getString("pmo_name_with_initial"));
				busOwnerDTO.setMaritalStatus(rs.getString("pmo_marital_status"));
				busOwnerDTO.setTelephoneNo(rs.getString("pmo_telephone_no"));
				busOwnerDTO.setMobileNo(rs.getString("pmo_mobile_no"));
				busOwnerDTO.setAddress1(rs.getString("pmo_address1"));
				busOwnerDTO.setAddress1Sinhala(rs.getString("pmo_address1_sinhala"));
				busOwnerDTO.setAddress1Tamil(rs.getString("pmo_address1_tamil"));
				busOwnerDTO.setAddress2(rs.getString("pmo_address2"));
				busOwnerDTO.setAddress2Sinhala(rs.getString("pmo_address2_sinhala"));
				busOwnerDTO.setAddress2Tamil(rs.getString("pmo_address2_tamil"));
				busOwnerDTO.setCity(rs.getString("pmo_city"));
				busOwnerDTO.setCitySinhala(rs.getString("pmo_city_sinhala"));
				busOwnerDTO.setCityTamil(rs.getString("pmo_city_tamil"));
				busOwnerDTO.setProvince(rs.getString("pmo_province"));
				busOwnerDTO.setDistrict(rs.getString("pmo_district"));
				busOwnerDTO.setDivSec(rs.getString("pmo_div_sec"));
				busOwnerDTO.setNicNo(rs.getString("pmo_nic"));
				busOwnerDTO.setSeq(rs.getLong("seq"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return busOwnerDTO;

	}

	public int updateBusOwner(BusOwnerDTO busOwnerDTO) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_pm_vehi_owner "
					+ " SET pmo_preferred_language =?, pmo_title =?, pmo_full_name=?, pmo_full_name_sinhala =?, "
					+ " pmo_full_name_tamil=?, pmo_name_with_initial=?, pmo_nic=?, pmo_gender=?, pmo_dob =?, "
					+ " pmo_marital_status=?, pmo_telephone_no=?, pmo_mobile_no=?, pmo_province=?, pmo_district=?, "
					+ " pmo_div_sec=?, pmo_address1=?, pmo_address1_sinhala=?, pmo_address1_tamil=?, pmo_address2=?, "
					+ " pmo_address2_sinhala=?, pmo_address2_tamil=?, pmo_address3=?, pmo_address3_sinhala=?, "
					+ " pmo_address3_tamil=?, pmo_city=?, pmo_city_sinhala=?, pmo_city_tamil=?, pm_modified_by=?,pm_modified_date=? "
					+ " WHERE seq= ?;";
			try {
				stmt = con.prepareStatement(sql);
				
				stmt.setString(1, busOwnerDTO.getPerferedLanguage());
				stmt.setString(2, busOwnerDTO.getTitle());
				stmt.setString(3, busOwnerDTO.getFullName());
				stmt.setString(4, busOwnerDTO.getFullNameSinhala());
				stmt.setString(5, busOwnerDTO.getFullNameTamil());
				stmt.setString(6, busOwnerDTO.getNameWithInitials());
				stmt.setString(7, busOwnerDTO.getNicNo());
				stmt.setString(8, busOwnerDTO.getGender());
				if (busOwnerDTO.getDob() != null) {
					String dob = (dateFormat.format(busOwnerDTO.getDob()));
					stmt.setString(9, dob);
				} else {
					stmt.setString(9, null);
				}
				stmt.setString(10, busOwnerDTO.getMaritalStatus());
				stmt.setString(11, busOwnerDTO.getTelephoneNo());
				stmt.setString(12, busOwnerDTO.getMobileNo());
				stmt.setString(13, busOwnerDTO.getProvince());
				stmt.setString(14, busOwnerDTO.getDistrict());
				stmt.setString(15, busOwnerDTO.getDivSec());
				stmt.setString(16, busOwnerDTO.getAddress1());
				stmt.setString(17, busOwnerDTO.getAddress1Sinhala());
				stmt.setString(18, busOwnerDTO.getAddress1Tamil());
				stmt.setString(19, busOwnerDTO.getAddress2());
				stmt.setString(20, busOwnerDTO.getAddress2Sinhala());
				stmt.setString(21, busOwnerDTO.getAddress2Tamil());
				stmt.setString(22, busOwnerDTO.getAddress3());
				stmt.setString(23, busOwnerDTO.getAddress3Sinhala());
				stmt.setString(24, busOwnerDTO.getAddress3Tamil());
				stmt.setString(25, busOwnerDTO.getCity());
				stmt.setString(26, busOwnerDTO.getCitySinhala());
				stmt.setString(27, busOwnerDTO.getCityTamil());
				stmt.setString(28, busOwnerDTO.getModifiedBy());
				stmt.setTimestamp(29, timestamp);
				stmt.setLong(30, busOwnerDTO.getSeq());
				
				stmt.executeUpdate();
				con.commit();
			}catch (SQLException e) {
				con.rollback();
				return -1;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return 0;
	}

	@Override
	public void saveDataApplication(BusOwnerDTO busOwnerDTO) {
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			String sql = "INSERT INTO nt_t_pm_application( "
					+ "seq, pm_application_no, pm_queue_no, pm_permit_no, pm_vehicle_regno, pm_permit_issue_date, pm_permit_issue_by, pm_inspect_date, pm_tender_ref_no, pm_status, pm_tender_annual_fee, pm_installments, pm_is_permit_print, pm_reject_reason, pm_valid_from, pm_valid_to, pm_approved_by, pm_appoved_date, pm_special_remark, pm_is_tender_permit, pm_service_type, pm_route_no, pm_tot_bus_fare, pm_is_backlog_app, pm_created_by, pm_created_date, pm_modified_by, pm_modified_date, pm_permit_expire_date, pm_renewal_period, pm_next_ins_date_sec1_2, pm_next_ins_date_sec3, pm_inspect_remark, pm_route_flag, pm_isnew_permit, pm_reinspec_status, pm_old_permit_no, pm_temp_permit_no"
					+ ")" + "SELECT nextval('seq_nt_t_pm_application'), "
					+ "      ?, ?, pm_permit_no, pm_vehicle_regno, pm_permit_issue_date, pm_permit_issue_by, pm_inspect_date, pm_tender_ref_no, ?, pm_tender_annual_fee, pm_installments, pm_is_permit_print, pm_reject_reason, pm_valid_from, pm_valid_to, pm_approved_by, pm_appoved_date, pm_special_remark, pm_is_tender_permit, pm_service_type, pm_route_no, pm_tot_bus_fare, pm_is_backlog_app, ?, ?, pm_modified_by, pm_modified_date, pm_permit_expire_date, pm_renewal_period, pm_next_ins_date_sec1_2, pm_next_ins_date_sec3, pm_inspect_remark,?, pm_isnew_permit, pm_reinspec_status, pm_old_permit_no, pm_temp_permit_no"
					+ " FROM nt_t_pm_application WHERE pm_vehicle_regno=? AND pm_status='A'"; // added by tharushi.e

			stmt = con.prepareStatement(sql);

			stmt.setString(1, busOwnerDTO.getApplicationNo());
			stmt.setString(2, busOwnerDTO.getQueueNo());
			stmt.setString(3, "P");
			stmt.setString(4, busOwnerDTO.getCreatedBy());
			stmt.setTimestamp(5, timestamp);
			stmt.setString(6, busOwnerDTO.getRouteFlag());
			stmt.setString(7, busOwnerDTO.getBusRegNo());

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
	public void saveDataVehicleOwner(String newApplicationNo, String oldApplicationNo, String createdBy,
			String strVehicleNo) {
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			String sql = "INSERT INTO nt_t_pm_vehi_owner(seq, pmo_application_no, pmo_permit_no, pmo_vehicle_regno, pmo_preferred_language, pmo_title, pmo_full_name, pmo_full_name_sinhala, pmo_full_name_tamil, pmo_name_with_initial, pmo_nic, pmo_gender, pmo_dob, pmo_marital_status, pmo_telephone_no, pmo_mobile_no, pmo_province, pmo_district, pmo_div_sec, pmo_address1, pmo_address1_sinhala, pmo_address1_tamil, pmo_address2, pmo_address2_sinhala, pmo_address2_tamil, pmo_address3, pmo_address3_sinhala, pmo_address3_tamil, pmo_city, pmo_city_sinhala, pmo_city_tamil, pmo_is_backlog_app, pm_created_by, pm_created_date"
					+ "					)SELECT nextval('seq_nt_t_pm_vehi_owner'), "
					+ "				   ?,pmo_permit_no, ?, pmo_preferred_language, pmo_title, pmo_full_name, pmo_full_name_sinhala, pmo_full_name_tamil, pmo_name_with_initial, pmo_nic, pmo_gender, pmo_dob, pmo_marital_status, pmo_telephone_no, pmo_mobile_no, pmo_province, pmo_district, pmo_div_sec, pmo_address1, pmo_address1_sinhala, pmo_address1_tamil, pmo_address2, pmo_address2_sinhala, pmo_address2_tamil, pmo_address3, pmo_address3_sinhala, pmo_address3_tamil, pmo_city, pmo_city_sinhala, pmo_city_tamil, pmo_is_backlog_app, ?, ?"
					+ "				FROM nt_t_pm_vehi_owner WHERE pmo_application_no=?";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, newApplicationNo);
			stmt.setString(2, strVehicleNo);
			stmt.setString(3, createdBy);
			stmt.setTimestamp(4, timestamp);
			stmt.setString(5, oldApplicationNo);

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
	public void saveDataOminiBus(String newApplicationNo, String oldApplicationNo, String createdBy) {
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			String sql = "INSERT INTO nt_t_pm_omini_bus1(seq, pmb_application_no, pmb_vehicle_regno, pmb_engine_no, pmb_chassis_no, pmb_make, pmb_model, pmb_seating_capacity, pmb_no_of_doors, pmb_height, pmb_production_date, pmb_weight, pmb_serial_no, pmb_certificate_date, pmb_garage_reg_no, pmb_insurance_company, pmb_insurance_cat, pmb_insurance_expire_date, pmb_bus_price, pmb_is_loan_obtain, pmb_finance_company, pmb_bank_loan_amt, pmb_due_amt, pmb_date_obtain, pmb_lapsed_installment, pmb_revenue_license_exp_date, pmb_first_reg_date, pmb_is_backlog_app, pmb_created_by, pmb_created_date, pmb_permit_no, pmb_policy_no, pmb_emission_test_exp_date, pmb_garage_name"
					+ "					)SELECT nextval('seq_nt_t_pm_omini_bus1'), "
					+ "				   ?, pmb_vehicle_regno, pmb_engine_no, pmb_chassis_no, pmb_make, pmb_model, pmb_seating_capacity, pmb_no_of_doors, pmb_height, pmb_production_date, pmb_weight, pmb_serial_no, pmb_certificate_date, pmb_garage_reg_no, pmb_insurance_company, pmb_insurance_cat, pmb_insurance_expire_date, pmb_bus_price, pmb_is_loan_obtain, pmb_finance_company, pmb_bank_loan_amt, pmb_due_amt, pmb_date_obtain, pmb_lapsed_installment, pmb_revenue_license_exp_date, pmb_first_reg_date, pmb_is_backlog_app, ?, ?, pmb_permit_no, pmb_policy_no, pmb_emission_test_exp_date, pmb_garage_name "
					+ "				FROM nt_t_pm_omini_bus1 WHERE pmb_application_no=?";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, newApplicationNo);
			stmt.setString(2, createdBy);
			stmt.setTimestamp(3, timestamp);
			stmt.setString(4, oldApplicationNo);

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
	public void saveNewOminiBusApplication(OminiBusDTO ominiBusDTO) {
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			String sql = "INSERT INTO nt_t_pm_application( "
					+ "seq, pm_application_no, pm_queue_no, pm_permit_no, pm_vehicle_regno, pm_permit_issue_date, pm_permit_issue_by, pm_inspect_date, pm_tender_ref_no, pm_status, pm_tender_annual_fee, pm_installments, pm_is_permit_print, pm_reject_reason, pm_valid_from, pm_valid_to, pm_approved_by, pm_appoved_date, pm_special_remark, pm_is_tender_permit, pm_service_type, pm_route_no, pm_tot_bus_fare, pm_is_backlog_app, pm_created_by, pm_created_date, pm_modified_by, pm_modified_date, pm_permit_expire_date, pm_renewal_period, pm_next_ins_date_sec1_2, pm_next_ins_date_sec3, pm_inspect_remark, pm_route_flag, pm_isnew_permit, pm_reinspec_status, pm_old_permit_no, pm_temp_permit_no"
					+ ")" + "SELECT nextval('seq_nt_t_pm_application'), "
					+ "      ?, ?, pm_permit_no, ?, pm_permit_issue_date, pm_permit_issue_by, pm_inspect_date, pm_tender_ref_no, ?, pm_tender_annual_fee, pm_installments, pm_is_permit_print, pm_reject_reason, pm_valid_from, pm_valid_to, pm_approved_by, pm_appoved_date, pm_special_remark, pm_is_tender_permit, pm_service_type, pm_route_no, pm_tot_bus_fare, pm_is_backlog_app, ?, ?, pm_modified_by, pm_modified_date, pm_permit_expire_date, pm_renewal_period, pm_next_ins_date_sec1_2, pm_next_ins_date_sec3, pm_inspect_remark, pm_route_flag, pm_isnew_permit, pm_reinspec_status, pm_old_permit_no, pm_temp_permit_no"
					+ " FROM nt_t_pm_application WHERE pm_permit_no=? AND pm_status='A'";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, ominiBusDTO.getApplicationNo());
			stmt.setString(2, ominiBusDTO.getQueueNo());
			stmt.setString(3, ominiBusDTO.getVehicleRegNo());
			stmt.setString(4, "P");
			stmt.setString(5, ominiBusDTO.getCreatedBy());
			stmt.setTimestamp(6, timestamp);
			stmt.setString(7, ominiBusDTO.getPermitNo());

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
	public int saveNewOminiBus(OminiBusDTO ominiBusDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_pm_omini_bus1");

			String sql = "INSERT INTO public.nt_t_pm_omini_bus1 (" + "pmb_application_no, pmb_vehicle_regno,"
					+ "pmb_seating_capacity, pmb_no_of_doors," + "pmb_height, pmb_production_date, pmb_weight,"
					+ "pmb_serial_no, pmb_certificate_date, pmb_garage_reg_no,"
					+ "pmb_insurance_company, pmb_insurance_cat, pmb_insurance_expire_date,"
					+ "pmb_bus_price, pmb_is_loan_obtain, pmb_finance_company,"
					+ "pmb_bank_loan_amt, pmb_due_amt, pmb_date_obtain,"
					+ "pmb_lapsed_installment, pmb_revenue_license_exp_date,"
					+ "pmb_first_reg_date, pmb_is_backlog_app,"
					+ "pmb_modified_by, pmb_modified_date,pmb_permit_no,seq,  pmb_policy_no,pmb_garage_name, pmb_emission_test_exp_date)"
					+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, ominiBusDTO.getApplicationNo());
			stmt.setString(2, ominiBusDTO.getVehicleRegNo());
			stmt.setString(3, ominiBusDTO.getSeating());
			stmt.setString(4, ominiBusDTO.getNoofDoors());
			stmt.setString(5, ominiBusDTO.getHeight());
			stmt.setString(6, ominiBusDTO.getManufactureDate());
			stmt.setString(7, ominiBusDTO.getWeight());
			stmt.setString(8, ominiBusDTO.getSerialNo());

			if (ominiBusDTO.getFitnessCertiDate() != null) {
				String getFitnessCertiDate = (dateFormat.format(ominiBusDTO.getFitnessCertiDate()));
				stmt.setString(9, getFitnessCertiDate);
			} else {
				stmt.setString(9, null);
			}

			stmt.setString(10, ominiBusDTO.getGarageRegNo());
			stmt.setString(11, ominiBusDTO.getInsuCompName());
			stmt.setString(12, ominiBusDTO.getInsuCat());

			if (ominiBusDTO.getInsuExpDate() != null) {
				String getInsuExpDate = (dateFormat.format(ominiBusDTO.getInsuExpDate()));
				stmt.setString(13, getInsuExpDate);
			} else {
				stmt.setString(13, null);
			}

			stmt.setBigDecimal(14, ominiBusDTO.getPriceValOfBus());
			stmt.setString(15, ominiBusDTO.getIsLoanObtained());
			stmt.setString(16, ominiBusDTO.getFinanceCompany());
			stmt.setBigDecimal(17, ominiBusDTO.getBankLoan());
			stmt.setBigDecimal(18, ominiBusDTO.getDueAmount());

			if (ominiBusDTO.getDateObtained() != null) {
				String getDateObtained = (dateFormat.format(ominiBusDTO.getDateObtained()));
				stmt.setString(19, getDateObtained);
			} else {
				stmt.setString(19, null);
			}

			stmt.setString(20, ominiBusDTO.getLapsedInstall());

			if (ominiBusDTO.getExpiryDateRevLic() != null) {
				String getExpiryDateRevLic = (dateFormat.format(ominiBusDTO.getExpiryDateRevLic()));
				stmt.setString(21, getExpiryDateRevLic);
			} else {
				stmt.setString(21, null);
			}

			if (ominiBusDTO.getDateOfFirstReg() != null) {
				String getDateOfFirstReg = (dateFormat.format(ominiBusDTO.getDateOfFirstReg()));
				stmt.setString(22, getDateOfFirstReg);
			} else {
				stmt.setString(22, null);
			}

			stmt.setString(23, ominiBusDTO.getIsBacklogApp());
			stmt.setString(24, ominiBusDTO.getModifiedBy());
			stmt.setTimestamp(25, timestamp);
			stmt.setString(26, ominiBusDTO.getPermitNo());
			stmt.setLong(27, seqNo);
			stmt.setString(28, ominiBusDTO.getPolicyNo());
			stmt.setString(29, ominiBusDTO.getGarageName());
			if (ominiBusDTO.getEmissionExpDate() != null) {
				String getEmissionExpDateVal = (dateFormat.format(ominiBusDTO.getEmissionExpDate()));
				stmt.setString(30, getEmissionExpDateVal);
			} else {
				stmt.setString(30, null);
			}
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
	public void saveAmendmentRelationshipInformation(AmendmentDTO amendmentDTO) {
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_amendments");

			String sql = "INSERT INTO nt_m_amendments "
					+ "(seq, amd_application_no,amd_relationship_with_transferor,amd_transferor_remarks,amd_permit_no,amd_queue_no,amd_created_by,amd_created_date,amd_status,amd_trn_type,amd_reasonfor_ownerchange,amd_new_busno, amd_existing_busno)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?)";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, amendmentDTO.getApplicationNo());
			stmt.setString(3, amendmentDTO.getRelationshipWithTransferor());
			stmt.setString(4, amendmentDTO.getRelationshipWithTransferorRemarks());
			stmt.setString(5, amendmentDTO.getPermitNo());
			stmt.setString(6, amendmentDTO.getQueueNo());
			stmt.setString(7, amendmentDTO.getCreatedBy());
			stmt.setTimestamp(8, timestamp);
			stmt.setString(9, "P");
			stmt.setString(10, amendmentDTO.getTrnType());
			stmt.setString(11, amendmentDTO.getReasonForOwnerChange());

			stmt.setString(12, amendmentDTO.getBusRegNo());
			stmt.setString(13, amendmentDTO.getOldBusNo());

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

	public String generateApplicationNo() {
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

			String sql = "SELECT pm_application_no "
					+ " FROM public.nt_t_pm_application WHERE pm_application_no LIKE 'AAP%'"
					+ " ORDER BY pm_created_date desc " + " LIMIT 1";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("pm_application_no");
			}

			if (result != null) {

				lastyr = result.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String ApprecordcountN = "00001";
					strAppNo = "AAP" + currYear + ApprecordcountN;
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
					strAppNo = "AAP" + currYear + ApprecordcountN;
				}
			} else
				strAppNo = "AAP" + currYear + "00001";

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return strAppNo;
	}

	public List<CommonDTO> getRelationships() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code,description " + " FROM public.nt_r_relationships " + " WHERE active = 'A' "
					+ " ORDER BY code ";

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

	@Override
	public String getTransactionType(String queueNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String today = df.format(date);

		String trnCode = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "select que_trn_type_code from nt_m_queue_master where que_number=? and que_date like '"
					+ today + "%'";

			ps = con.prepareStatement(query);
			ps.setString(1, queueNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				trnCode = (rs.getString("que_trn_type_code"));

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return trnCode;
	}

	@Override
	public String getTransactionTypeDesc(String strTrnCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String trnCode = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "select description from nt_r_transaction_type where code=?";

			ps = con.prepareStatement(query);
			ps.setString(1, strTrnCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				trnCode = (rs.getString("description"));

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return trnCode;
	}

	@Override
	public String getPermitNoByVehicleNo(String vehicleNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String permitNo = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT pm_permit_no FROM nt_t_pm_application WHERE pm_vehicle_regno=? AND pm_status='A'";

			ps = con.prepareStatement(query);
			ps.setString(1, vehicleNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				permitNo = (rs.getString("pm_permit_no"));

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
	public List<PermitRenewalsDTO> getChecklistDocuments(String trnType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PermitRenewalsDTO> returnList = new ArrayList<PermitRenewalsDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select doc_code, doc_document_des FROM public.nt_m_document where doc_transaction_type=? ORDER BY doc_code ASC;";

			ps = con.prepareStatement(query);
			ps.setString(1, trnType);
			rs = ps.executeQuery();

			while (rs.next()) {
				PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();
				permitRenewalsDTO.setDocumentCode(rs.getString("doc_code"));
				permitRenewalsDTO.setDocumentDescription(rs.getString("doc_document_des"));
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

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public boolean checkIsSubmitted(String currentDocCode, String applicationNo, String permitNo, String trnType) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isDocCodeHasRecord = false;
		PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT seqno FROM public.nt_t_application_document where apd_application_no=? and apd_permit_no=? and apd_transaction_type=? and apd_doc_code=?;";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, applicationNo);
			stmt.setString(2, permitNo);
			stmt.setString(3, trnType);
			stmt.setString(4, currentDocCode);
			rs = stmt.executeQuery();

			while (rs.next()) {
				permitRenewalsDTO.setDocSeqChecked(rs.getLong("seqno"));
			}

			if (permitRenewalsDTO.getDocSeqChecked().equals("") || permitRenewalsDTO.getDocSeqChecked() == null) {
				isDocCodeHasRecord = false;
			} else {
				isDocCodeHasRecord = true;
			}

		} catch (Exception e) {
			isDocCodeHasRecord = false;

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return isDocCodeHasRecord;
	}

	@Override
	public PermitRenewalsDTO getRemarkDetails(String currentDocCode, String applicationNo, String permitNo,
			String trnType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT seqno, apd_file_path, apd_remark FROM public.nt_t_application_document where apd_application_no=? and apd_permit_no=? and apd_transaction_type=? and apd_doc_code=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, applicationNo);
			ps.setString(2, permitNo);
			ps.setString(3, trnType);
			ps.setString(4, currentDocCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				permitRenewalsDTO.setDocSeqChecked(rs.getLong("seqno"));
				permitRenewalsDTO.setDocFilePath(rs.getString("apd_file_path"));
				permitRenewalsDTO.setRemark(rs.getString("apd_remark"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return permitRenewalsDTO;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public void saveAmendmentNewOminiBus(AmendmentDTO amendmentDTO) {
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_amendments");

			String sql = "INSERT INTO nt_m_amendments "
					+ "(seq, amd_queue_no, amd_permit_no, amd_application_no, amd_status, amd_is_bus_running, amd_hv_legal_case, amd_details_of_case,amd_new_busno, amd_existing_busno, amd_created_by, amd_created_date, amd_trn_type,amd_is_omnibus)"
					+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, amendmentDTO.getQueueNo());
			stmt.setString(3, amendmentDTO.getPermitNo());
			stmt.setString(4, amendmentDTO.getApplicationNo());
			stmt.setString(5, "P");

			if (amendmentDTO.getIsBusRunning() != null && amendmentDTO.getIsBusRunning().equalsIgnoreCase("true")) {
				stmt.setString(6, "Y");
			} else if (amendmentDTO.getIsBusRunning() != null
					&& amendmentDTO.getIsBusRunning().equalsIgnoreCase("false")) {
				stmt.setString(6, "N");
			} else {
				stmt.setString(6, null);
			}

			if (amendmentDTO.getHaveLegalCase() != null && amendmentDTO.getHaveLegalCase().equalsIgnoreCase("true")) {
				stmt.setString(7, "Y");
			} else if (amendmentDTO.getHaveLegalCase() != null
					&& amendmentDTO.getHaveLegalCase().equalsIgnoreCase("false")) {
				stmt.setString(7, "N");
			} else {
				stmt.setString(7, null);
			}

			stmt.setString(8, amendmentDTO.getLegalCaseDetails());
			stmt.setString(9, amendmentDTO.getBusRegNo());
			stmt.setString(10, amendmentDTO.getOldBusNo());
			stmt.setString(11, amendmentDTO.getCreatedBy());
			stmt.setTimestamp(12, timestamp);
			stmt.setString(13, amendmentDTO.getTrnType());

			if (amendmentDTO.getIsOminiBus() != null && amendmentDTO.getIsOminiBus().equalsIgnoreCase("true")) {
				stmt.setString(14, "Y");
			} else if (amendmentDTO.getIsOminiBus() != null && amendmentDTO.getIsOminiBus().equalsIgnoreCase("false")) {
				stmt.setString(14, "N");
			} else {
				stmt.setString(14, null);
			}

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
	public List<OrganizationDTO> getOtherOrgList(String generatedApplicationNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<OrganizationDTO> returnList = new ArrayList<OrganizationDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT A.seq , A.amendment_seq, B.description AS organization, A.route_no, A.permit_no, A.expiry_date, A.reason_for_not_renewed FROM public.nt_t_newomini_bus A INNER JOIN nt_r_organization B ON A.organization=B.code WHERE A.application_no = ? ORDER BY A.organization";

			ps = con.prepareStatement(query);
			ps.setString(1, generatedApplicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				OrganizationDTO organizationDTO = new OrganizationDTO();
				organizationDTO.setSeq(rs.getLong("seq"));
				organizationDTO.setAmendmentSeq(rs.getLong("amendment_seq"));
				organizationDTO.setOrganization(rs.getString("organization"));

				String expiry_dateString = rs.getString("expiry_date");
				if (expiry_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setExpiryDate = originalFormat.parse(expiry_dateString);
					organizationDTO.setExpiryDate(setExpiryDate);
					organizationDTO.setStrExpiryDate(expiry_dateString);
				}

				organizationDTO.setRouteNo(rs.getString("route_no"));
				organizationDTO.setPermitNo(rs.getString("permit_no"));
				organizationDTO.setReason(rs.getString("reason_for_not_renewed"));

				returnList.add(organizationDTO);

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
	public void saveOtherOrg(OrganizationDTO organizationDTO) {
		java.util.Date date = new java.util.Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_newomini_bus");

			String sql = "INSERT INTO nt_t_newomini_bus "
					+ "(seq, vehicle_no, application_no, amendment_seq, organization, route_no, permit_no, expiry_date, reason_for_not_renewed, created_by, created_date)"
					+ " VALUES (?,?,?,?,?,?,?,?,?,?,?)";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, organizationDTO.getVehicleNo());
			stmt.setString(3, organizationDTO.getApplicationNo());
			stmt.setLong(4, organizationDTO.getAmendmentSeq());
			stmt.setString(5, organizationDTO.getOrganization());
			stmt.setString(6, organizationDTO.getRouteNo());
			stmt.setString(7, organizationDTO.getPermitNo());
			// stmt.setString(8, organizationDTO.getExpiryDate());

			if (organizationDTO.getExpiryDate() != null) {
				String getExpiryDate = (dateFormat.format(organizationDTO.getExpiryDate()));
				stmt.setString(8, getExpiryDate);
			} else {
				stmt.setString(8, null);
			}

			stmt.setString(9, organizationDTO.getReason());
			stmt.setString(10, organizationDTO.getCreatedBy());
			stmt.setTimestamp(11, timestamp);

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
	public List<CommonDTO> getAccidentTypesList() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> accidentTypesList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code,description FROM nt_r_accident_type WHERE active='A'";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("code"));
				commonDTO.setDescription(rs.getString("description"));
				accidentTypesList.add(commonDTO);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return accidentTypesList;

	}

	@Override
	public void saveAccident(AccidentDTO accidentDTO) {
		java.util.Date date = new java.util.Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_accident_details");

			String sql = "INSERT INTO nt_t_accident_details "
					+ "(seqno, acd_accident_type_code, acd_accident_date, acd_description, acd_vehicleno, acd_created_by, acd_created_date)"
					+ " VALUES (?,?,?,?,?,?,?)";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, accidentDTO.getAccidentType());

			if (accidentDTO.getAccidentDate() != null) {
				String getAccidentDate = (dateFormat.format(accidentDTO.getAccidentDate()));
				stmt.setString(3, getAccidentDate);
			} else {
				stmt.setString(3, null);
			}

			stmt.setString(4, accidentDTO.getAccidentDesc());
			stmt.setString(5, accidentDTO.getVehicleNo());
			stmt.setString(6, accidentDTO.getCreatedBy());
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
	public List<AccidentDTO> getAccidentList(String vehicleNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<AccidentDTO> returnList = new ArrayList<AccidentDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT A.seqno, A.acd_accident_date, B.description AS acd_accident_type_code, A.acd_description, A.acd_vehicleno FROM public.nt_t_accident_details A INNER JOIN nt_r_accident_type B ON A.acd_accident_type_code=B.code WHERE A.acd_vehicleno = ? ORDER BY A.acd_accident_date";

			ps = con.prepareStatement(query);
			ps.setString(1, vehicleNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				AccidentDTO accidentDTO = new AccidentDTO();

				accidentDTO.setSeq(rs.getLong("seqno"));

				accidentDTO.setAccidentType(rs.getString("acd_accident_type_code"));

				String acd_accident_dateString = rs.getString("acd_accident_date");
				if (acd_accident_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setAccidentDate = originalFormat.parse(acd_accident_dateString);
					accidentDTO.setAccidentDate(setAccidentDate);
					accidentDTO.setStrAccidentDate(acd_accident_dateString);
				}

				accidentDTO.setAccidentDesc(rs.getString("acd_description"));

				accidentDTO.setVehicleNo(rs.getString("acd_vehicleno"));

				returnList.add(accidentDTO);

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
	public void saveAfterAccident(AfterAccidentDTO accidentDTO) {
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_accident_master");

			String sql = "INSERT INTO nt_t_accident_master "
					+ "(seqno, acm_vehicleno,acm_isface_for_medi_test, acm_is_paricipate_training, acm_is_licence_cancelled, acm_cancel_period,acm_any_legalcase_notclosed, acm_created_by, acm_created_date)"
					+ " VALUES (?,?,?,?,?,?,?,?,?)";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, accidentDTO.getVehicleNo());

			if (accidentDTO.getIsFacedMediTest() != null && accidentDTO.getIsFacedMediTest().equalsIgnoreCase("true")) {
				stmt.setString(3, "Y");
			} else if (accidentDTO.getIsFacedMediTest() != null
					&& accidentDTO.getIsFacedMediTest().equalsIgnoreCase("false")) {
				stmt.setString(3, "N");
			} else {
				stmt.setString(3, null);
			}

			if (accidentDTO.getIsParicipateTraining() != null
					&& accidentDTO.getIsParicipateTraining().equalsIgnoreCase("true")) {
				stmt.setString(4, "Y");
			} else if (accidentDTO.getIsParicipateTraining() != null
					&& accidentDTO.getIsParicipateTraining().equalsIgnoreCase("false")) {
				stmt.setString(4, "N");
			} else {
				stmt.setString(4, null);
			}

			if (accidentDTO.getIsLicenseCancelled() != null
					&& accidentDTO.getIsLicenseCancelled().equalsIgnoreCase("true")) {
				stmt.setString(5, "Y");
			} else if (accidentDTO.getIsLicenseCancelled() != null
					&& accidentDTO.getIsLicenseCancelled().equalsIgnoreCase("false")) {
				stmt.setString(5, "N");
			} else {
				stmt.setString(5, null);
			}

			stmt.setString(6, accidentDTO.getCancelPeriod());

			if (accidentDTO.getAnyLegalCaseNotClosed() != null
					&& accidentDTO.getAnyLegalCaseNotClosed().equalsIgnoreCase("true")) {
				stmt.setString(7, "Y");
			} else if (accidentDTO.getAnyLegalCaseNotClosed() != null
					&& accidentDTO.getAnyLegalCaseNotClosed().equalsIgnoreCase("false")) {
				stmt.setString(7, "N");
			} else {
				stmt.setString(7, null);
			}

			stmt.setString(8, accidentDTO.getCreatedBy());
			stmt.setTimestamp(9, timestamp);

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
	public void saveMoreAccident(AccidentDTO accidentDTO) {
		java.util.Date date = new java.util.Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_legal_cases");

			String sql = "INSERT INTO nt_t_legal_cases "
					+ "(seqno, lec_vehicleno, lec_accident_date, lec_natureof_accident, lec_damage_propertyval, lec_noofinjured, lec_noofdeath,lec_created_by,lec_created_date)"
					+ " VALUES (?,?,?,?,?,?,?,?,?)";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, accidentDTO.getVehicleNo());

			if (accidentDTO.getDateOfAccident() != null) {
				String getDateOfAccident = (dateFormat.format(accidentDTO.getDateOfAccident()));
				stmt.setString(3, getDateOfAccident);
			} else {
				stmt.setString(3, null);
			}

			stmt.setString(4, accidentDTO.getNatureOfAccident());

			stmt.setBigDecimal(5, accidentDTO.getValueOfPropertyDamaged());

			stmt.setInt(6, accidentDTO.getNoOfInjured());

			stmt.setInt(7, accidentDTO.getNoOfDeaths());

			stmt.setString(8, accidentDTO.getCreatedBy());
			stmt.setTimestamp(9, timestamp);

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
	public List<AccidentDTO> getMoreAccidentList(String vehicleRegNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<AccidentDTO> returnList = new ArrayList<AccidentDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT A.seqno, A.lec_vehicleno, A.lec_accident_date, B.description AS lec_natureof_accident, A.lec_damage_propertyval, A.lec_noofinjured, A.lec_noofdeath, A.lec_created_by, A.lec_created_date FROM nt_t_legal_cases A INNER JOIN nt_r_accident_type B ON A.lec_natureof_accident=B.code  WHERE A.lec_vehicleno = ? ORDER BY A.lec_accident_date";
			ps = con.prepareStatement(query);
			ps.setString(1, vehicleRegNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				AccidentDTO accidentDTO = new AccidentDTO();

				accidentDTO.setSeq(rs.getLong("seqno"));

				accidentDTO.setVehicleNo(rs.getString("lec_vehicleno"));

				String lec_accident_dateString = rs.getString("lec_accident_date");
				if (lec_accident_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setDateOfAccident = originalFormat.parse(lec_accident_dateString);
					accidentDTO.setDateOfAccident(setDateOfAccident);
					accidentDTO.setStrDateOfAccident(lec_accident_dateString);
				}

				accidentDTO.setNatureOfAccident(rs.getString("lec_natureof_accident"));

				accidentDTO.setValueOfPropertyDamaged(rs.getBigDecimal("lec_damage_propertyval"));

				accidentDTO.setNoOfInjured(rs.getInt("lec_noofinjured"));

				accidentDTO.setNoOfDeaths(rs.getInt("lec_noofdeath"));

				returnList.add(accidentDTO);

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
	public void removeOtherOrg(int selectedOtherOrg) {

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "DELETE " + "FROM nt_t_newomini_bus " + "WHERE seq = ?";

			ps = con.prepareStatement(query);
			ps.setInt(1, selectedOtherOrg);
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
	public void removeAccident(int selectedAccident) {

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "DELETE " + "FROM nt_t_accident_details " + "WHERE seqno = ?";

			ps = con.prepareStatement(query);
			ps.setInt(1, selectedAccident);
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
	public void removeMoreAccident(int selectedMoreAccident) {

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "DELETE " + "FROM nt_t_legal_cases " + "WHERE seqno = ?";

			ps = con.prepareStatement(query);
			ps.setInt(1, selectedMoreAccident);
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
	public List<AmendmentDTO> getApplicationNO(AmendmentDTO dtos) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<AmendmentDTO> data = new ArrayList<AmendmentDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct amd_application_no FROM public.nt_m_amendments "
					+ "WHERE amd_application_no != '' and amd_trn_type=? and amd_application_no is not null order by amd_application_no;";

			ps = con.prepareStatement(query);
			ps.setString(1, dtos.getTranCode());
			rs = ps.executeQuery();

			AmendmentDTO dto;

			while (rs.next()) {
				dto = new AmendmentDTO();
				dto.setApplicationNo(rs.getString("amd_application_no"));
				data.add(dto);

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
	public List<AmendmentDTO> getPermitNO(AmendmentDTO dtos) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<AmendmentDTO> data = new ArrayList<AmendmentDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct amd_permit_no FROM public.nt_m_amendments "
					+ "WHERE amd_permit_no != '' and amd_trn_type=? and  amd_permit_no is not null order by amd_permit_no;";

			ps = con.prepareStatement(query);
			ps.setString(1, dtos.getTranCode());
			rs = ps.executeQuery();

			AmendmentDTO dto;

			while (rs.next()) {
				dto = new AmendmentDTO();
				dto.setPermitNo(rs.getString("amd_permit_no"));
				data.add(dto);

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
	public AmendmentDTO ajaxFillData(AmendmentDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		AmendmentDTO amendmentDTO = new AmendmentDTO();
		try {

			con = ConnectionManager.getConnection();

			String query = "select  amd_permit_no, amd_application_no, amd_new_busno, amd_existing_busno FROM public.nt_m_amendments "
					+ "WHERE amd_application_no=? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, dto.getApplicationNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				amendmentDTO.setApplicationNo(rs.getString("amd_application_no"));
				amendmentDTO.setPermitNo(rs.getString("amd_permit_no"));
				amendmentDTO.setExisitingBusNo(rs.getString("amd_existing_busno"));
				amendmentDTO.setNewBusNo(rs.getString("amd_new_busno"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return amendmentDTO;
	}

	@Override
	public List<AmendmentDTO> getGrantApprovalDefaultDetails(AmendmentDTO amendmentDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<AmendmentDTO> data = new ArrayList<AmendmentDTO>();

		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct amd_pta,amd_time_approval, amd_permit_no,amd_commity_approved,amd_board_approved, amd_status, amd_application_no, amd_new_busno, amd_existing_busno, "
					+ "nt_r_transaction_type.description, nt_r_transaction_type.code, nt_t_pm_vehi_owner.pmo_full_name,case when amd_modified_date is null then amd_created_date else  amd_modified_date end as created_date,"
					+ "amd_timeapproval_service_change  "
					+ "FROM public.nt_m_amendments "
					+ "inner join public.nt_r_transaction_type on nt_r_transaction_type.code = nt_m_amendments.amd_trn_type "
					+ "left outer join public.nt_t_pm_vehi_owner on nt_t_pm_vehi_owner.pmo_application_no = nt_m_amendments.amd_application_no "
					+ "where (amd_status='P' and amd_trn_type !='13') or amd_status='AD' or amd_status='DG' or amd_status='FA'   order by created_date desc";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			AmendmentDTO dto;

			while (rs.next()) {
				dto = new AmendmentDTO();
				dto.setPermitNo(rs.getString("amd_permit_no"));
				dto.setApplicationNo(rs.getString("amd_application_no"));
				dto.setServiceType(null);
				dto.setTranCode(rs.getString("code"));
				dto.setTransactionType(rs.getString("description"));
				dto.setOwnerName(rs.getString("pmo_full_name"));
				dto.setExisitingBusNo(rs.getString("amd_existing_busno"));
				dto.setNewBusNo(rs.getString("amd_new_busno"));

				String approveStatusCode = rs.getString("amd_status");
				String committeeStatusCode = rs.getString("amd_commity_approved");
				String boardStatusCode = rs.getString("amd_board_approved");
				String ptaStatus = rs.getString("amd_pta");
				String timeApproval = rs.getString("amd_time_approval");
				String approveStatus = "";

				if (!approveStatusCode.equals("R")) {

					if (rs.getString("code").equals("13")) {

						if (approveStatusCode.equals("P")) {
							approveStatus = "PENDING";
						} else if (approveStatusCode.equals("DG")) {
							approveStatus = "DG / CHAIRMAN APPROVED";
						} else if (approveStatusCode.equals("FA")) {
							approveStatus = "APPROVED FOR PAYMENT";
						} else if (approveStatusCode.equals("AD")) {
							approveStatus = "AD / DIRECTOR APPROVED";
						} else if (approveStatusCode.equals("SA")) {
							approveStatus = "APPROVED FOR PRINTING";
						} else if (approveStatusCode.equals("C")) {
							approveStatus = "COMPLETE";
						} else {
							approveStatus = "N/A";
						}

					} else {
						if (committeeStatusCode == null && boardStatusCode == null && approveStatusCode.equals("P")) {
							approveStatus = "PENDING";
						} else if (committeeStatusCode.equals("A") && boardStatusCode == null
								&& approveStatusCode.equals("P") && ptaStatus == null && timeApproval == null) {
							approveStatus = "COMMITTEE APPROVED";
						} else if (committeeStatusCode.equals("A") && boardStatusCode == null
								&& approveStatusCode.equals("P") && ptaStatus.equals("P") && timeApproval == null) {
							approveStatus = "PTA REQUESTED";
						} else if (committeeStatusCode.equals("A") && boardStatusCode == null
								&& approveStatusCode.equals("P") && ptaStatus.equals("S") && timeApproval == null) {
							approveStatus = "PTA SKIPED";
						} else if (committeeStatusCode.equals("A") && boardStatusCode == null
								&& approveStatusCode.equals("P") && ptaStatus.equals("R") && timeApproval == null) {
							approveStatus = "PTA REJECTED";
						} else if (committeeStatusCode.equals("A") && boardStatusCode == null
								&& approveStatusCode.equals("P") && ptaStatus.equals("A") && timeApproval == null) {
							approveStatus = "PTA APPROVED";
						} else if (committeeStatusCode.equals("A") && boardStatusCode == null
								&& approveStatusCode.equals("P") && timeApproval.equals("R")) {
							approveStatus = "TIME APPROVAL REQUESTED";
						} else if (committeeStatusCode.equals("A") && boardStatusCode == null
								&& approveStatusCode.equals("P") && timeApproval.equals("A")) {
							approveStatus = "TIME APPROVAL APPROVED";
						} else if (committeeStatusCode.equals("A") && boardStatusCode.equals("A")
								&& approveStatusCode.equals("P")) {
							approveStatus = "BOARD APPROVED";
						} else if (committeeStatusCode.equals("A") && boardStatusCode.equals("A")
								&& approveStatusCode.equals("GR")) {
							approveStatus = "RESPONSE LETTER GENERATED";
						} else if (committeeStatusCode.equals("A") && boardStatusCode.equals("A")
								&& approveStatusCode.equals("DG")) {
							approveStatus = "DG / CHAIRMAN APPROVED";
						} else if (committeeStatusCode.equals("A") && boardStatusCode.equals("A")
								&& approveStatusCode.equals("FA")) {
							approveStatus = "APPROVED FOR PAYMENT";
						} else if (committeeStatusCode.equals("A") && boardStatusCode.equals("A")
								&& approveStatusCode.equals("AD")) {
							approveStatus = "AD / DIRECTOR APPROVED";
						} else if (committeeStatusCode.equals("A") && boardStatusCode.equals("A")
								&& approveStatusCode.equals("SA")) {
							approveStatus = "APPROVED FOR PRINTING";
						} else if (committeeStatusCode.equals("P") && boardStatusCode == null
								&& approveStatusCode.equals("P")) {
							approveStatus = "COMMITTEE APPROVAL ONGOING";
						} else if (committeeStatusCode.equals("A") && boardStatusCode.equals("P")
								&& approveStatusCode.equals("P")) {
							approveStatus = "BOARD APPROVAL ONGOING";
						} else if (committeeStatusCode.equals("A") && boardStatusCode.equals("A")
								&& approveStatusCode.equals("C")) {
							approveStatus = "COMPLETE";
						} else if (committeeStatusCode.equals("R") && boardStatusCode == null
								&& approveStatusCode.equals("P") && ptaStatus == null && timeApproval == null) {
							approveStatus = "REJECT BY COMMITTE";
						} else if (committeeStatusCode.equals("A") && boardStatusCode.equals("R")
								&& approveStatusCode.equals("P") && ptaStatus == null && timeApproval == null) {
							approveStatus = "REJECT BY BOARD";
						} else {
							approveStatus = "N/A";
						}
					}

				} else {
					approveStatus = "REJECTED";
				}

				dto.setStatus(approveStatus);

				Timestamp ts = rs.getTimestamp("created_date");

				if (ts != null) {
					Date date = new Date();
					date.setTime(ts.getTime());
					String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
					dto.setCreatedDateString(formattedDate);
				} else {
					dto.setCreatedDateString("N/A");
				}
				dto.setTimeApprovalForServiceChange(rs.getString("amd_timeapproval_service_change"));
				data.add(dto);

			}

		} catch (

		Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return data;
	}

	@Override
	public List<AmendmentDTO> getGrantApprovalDetails(AmendmentDTO amendmentDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<AmendmentDTO> data = new ArrayList<AmendmentDTO>();

		String WHERE_SQL = "";
		boolean whereadded = false;
		if (amendmentDTO.getTranCode() != null && !amendmentDTO.getTranCode().equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE amd_trn_type = " + "'" + amendmentDTO.getTranCode() + "'";
			whereadded = true;
		}

		if (amendmentDTO.getApplicationNo() != null && !amendmentDTO.getApplicationNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND amd_application_no = " + "'" + amendmentDTO.getApplicationNo() + "'";
			} else {
				WHERE_SQL = WHERE_SQL + " WHERE amd_application_no = " + "'" + amendmentDTO.getApplicationNo() + "'";
				whereadded = true;
			}
		}

		if (amendmentDTO.getPermitNo() != null && !amendmentDTO.getPermitNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND amd_permit_no = " + "'" + amendmentDTO.getPermitNo() + "'";
			} else {
				WHERE_SQL = WHERE_SQL + " WHERE amd_permit_no = " + "'" + amendmentDTO.getPermitNo() + "'";
				whereadded = true;
			}
		}

		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct amd_permit_no,amd_pta,amd_time_approval, amd_status, amd_commity_approved, amd_board_approved, amd_application_no, amd_new_busno, amd_existing_busno, "
					+ "nt_r_transaction_type.description, nt_r_transaction_type.code, nt_t_pm_vehi_owner.pmo_full_name, case when amd_modified_date is null then amd_created_date else  amd_modified_date end as created_date,"
					+ "amd_timeapproval_service_change  "
					+ "FROM public.nt_m_amendments "
					+ "inner join public.nt_r_transaction_type on nt_r_transaction_type.code = nt_m_amendments.amd_trn_type "
					+ "left outer join public.nt_t_pm_vehi_owner on nt_t_pm_vehi_owner.pmo_application_no = nt_m_amendments.amd_application_no "
					+ WHERE_SQL + " order by created_date desc";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			AmendmentDTO dto;

			while (rs.next()) {
				dto = new AmendmentDTO();
				dto.setPermitNo(rs.getString("amd_permit_no"));
				dto.setApplicationNo(rs.getString("amd_application_no"));
				dto.setServiceType(null);
				dto.setTranCode(rs.getString("code"));
				dto.setTransactionType(rs.getString("description"));
				dto.setOwnerName(rs.getString("pmo_full_name"));
				dto.setExisitingBusNo(rs.getString("amd_existing_busno"));
				dto.setNewBusNo(rs.getString("amd_new_busno"));

				String approveStatusCode = rs.getString("amd_status");
				String committeeStatusCode = rs.getString("amd_commity_approved");
				String boardStatusCode = rs.getString("amd_board_approved");
				String ptaStatus = rs.getString("amd_pta");
				String timeApproval = rs.getString("amd_time_approval");
				String approveStatus = "";

				if (!approveStatusCode.equals("R")) {

					if (rs.getString("code").equals("13")) {

						if (approveStatusCode.equals("P")) {
							approveStatus = "PENDING";
						} else if (approveStatusCode.equals("DG")) {
							approveStatus = "DG / CHAIRMAN APPROVED";
						} else if (approveStatusCode.equals("FA")) {
							approveStatus = "APPROVED FOR PAYMENT";
						} else if (approveStatusCode.equals("AD")) {
							approveStatus = "AD / DIRECTOR APPROVED";
						} else if (approveStatusCode.equals("SA")) {
							approveStatus = "APPROVED FOR PRINTING";
						} else if (approveStatusCode.equals("C")) {
							approveStatus = "COMPLETE";
						} else {
							approveStatus = "N/A";
						}

					} else {
						if (committeeStatusCode == null && boardStatusCode == null && approveStatusCode.equals("P")) {
							approveStatus = "PENDING";
						} else if (committeeStatusCode.equals("A") && boardStatusCode == null
								&& approveStatusCode.equals("P") && ptaStatus == null && timeApproval == null) {
							approveStatus = "COMMITTEE APPROVED";
						} else if (committeeStatusCode.equals("A") && boardStatusCode == null
								&& approveStatusCode.equals("P") && ptaStatus.equals("P") && timeApproval == null) {
							approveStatus = "PTA REQUESTED";
						} else if (committeeStatusCode.equals("A") && boardStatusCode == null
								&& approveStatusCode.equals("P") && ptaStatus.equals("S") && timeApproval == null) {
							approveStatus = "PTA SKIPED";
						} else if (committeeStatusCode.equals("A") && boardStatusCode == null
								&& approveStatusCode.equals("P") && ptaStatus.equals("R") && timeApproval == null) {
							approveStatus = "PTA REJECTED";
						} else if (committeeStatusCode.equals("A") && boardStatusCode == null
								&& approveStatusCode.equals("P") && ptaStatus.equals("A") && timeApproval == null) {
							approveStatus = "PTA APPROVED";
						} else if (committeeStatusCode.equals("A") && boardStatusCode == null
								&& approveStatusCode.equals("P") && timeApproval.equals("R")) {
							approveStatus = "TIME APPROVAL REQUESTED";
						} else if (committeeStatusCode.equals("A") && boardStatusCode == null
								&& approveStatusCode.equals("P") && timeApproval.equals("A")) {
							approveStatus = "TIME APPROVAL APPROVED";
						} else if (committeeStatusCode.equals("A") && boardStatusCode.equals("A")
								&& approveStatusCode.equals("P")) {
							approveStatus = "BOARD APPROVED";
						} else if (committeeStatusCode.equals("A") && boardStatusCode.equals("A")
								&& approveStatusCode.equals("GR")) {
							approveStatus = "RESPONSE LETTER GENERATED";
						} else if (committeeStatusCode.equals("A") && boardStatusCode.equals("A")
								&& approveStatusCode.equals("DG")) {
							approveStatus = "DG / CHAIRMAN APPROVED";
						} else if (committeeStatusCode.equals("A") && boardStatusCode.equals("A")
								&& approveStatusCode.equals("FA")) {
							approveStatus = "APPROVED FOR PAYMENT";
						} else if (committeeStatusCode.equals("A") && boardStatusCode.equals("A")
								&& approveStatusCode.equals("AD")) {
							approveStatus = "AD / DIRECTOR APPROVED";
						} else if (committeeStatusCode.equals("A") && boardStatusCode.equals("A")
								&& approveStatusCode.equals("SA")) {
							approveStatus = "APPROVED FOR PRINTING";
						} else if (committeeStatusCode.equals("P") && boardStatusCode == null
								&& approveStatusCode.equals("P")) {
							approveStatus = "COMMITTEE APPROVAL ONGOING";
						} else if (committeeStatusCode.equals("A") && boardStatusCode.equals("P")
								&& approveStatusCode.equals("P")) {
							approveStatus = "BOARD APPROVAL ONGOING";
						} else if (committeeStatusCode.equals("A") && boardStatusCode.equals("A")
								&& approveStatusCode.equals("C")) {
							approveStatus = "COMPLETE";
						} else if (committeeStatusCode.equals("R") && boardStatusCode == null
								&& approveStatusCode.equals("P") && ptaStatus == null && timeApproval == null) {
							approveStatus = "REJECT BY COMMITTE";
						} else if (committeeStatusCode.equals("A") && boardStatusCode.equals("R")
								&& approveStatusCode.equals("P") && ptaStatus == null && timeApproval == null) {
							approveStatus = "REJECT BY BOARD";

							/* Higher Authorization */
						} else if (approveStatusCode.equals("GR") && checkTaskDetails(dto, "PM101", "C")) {
							approveStatus = "PENDING";
						} else if (approveStatusCode.equals("GR") && checkTaskDetails(dto, "AM106", "C")) {
							approveStatus = "BALANCE TASK COMPLETED";
						} else if (approveStatusCode.equals("DG")) {
							approveStatus = "DG / CHAIRMAN APPROVED";
						} else if (approveStatusCode.equals("FA")) {
							approveStatus = "APPROVED FOR PAYMENT";
						} else if (approveStatusCode.equals("AD")) {
							approveStatus = "AD / DIRECTOR APPROVED";
						} else if (approveStatusCode.equals("SA")) {
							approveStatus = "APPROVED FOR PRINTING";
						} else {
							approveStatus = "N/A";
						}
					}

				} else {
					approveStatus = "REJECTED";
				}

				dto.setStatus(approveStatus);

				Timestamp ts = rs.getTimestamp("created_date");

				if (ts != null) {
					Date date = new Date();
					date.setTime(ts.getTime());
					String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
					dto.setCreatedDateString(formattedDate);
				} else {
					dto.setCreatedDateString("N/A");
				}
				dto.setTimeApprovalForServiceChange(rs.getString("amd_timeapproval_service_change"));
				data.add(dto);
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
	public boolean isBusChange(AmendmentDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isBusChange = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "select amd_commity_approved, amd_commity_approved FROM public.nt_m_amendments "
					+ "WHERE amd_application_no =? and amd_commity_approved ='Y' and amd_commity_approved is not null  and "
					+ "amd_board_approved ='Y' and amd_board_approved is not null ";

			ps = con.prepareStatement(query);
			ps.setString(1, dto.getApplicationNo());
			rs = ps.executeQuery();

			if (rs.next() == true) {

				isBusChange = false;
			} else {
				isBusChange = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isBusChange;
	}

	@Override
	public boolean checkTaskDetails(AmendmentDTO dto, String taskCode, String status) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isTaskAvailable = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "select  tsd_app_no, tsd_task_code, tsd_status FROM public.nt_t_task_det "
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
	public boolean checkTaskHistory(AmendmentDTO dto, String taskCode, String status) {
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
	public boolean insertTaskDetails(AmendmentDTO dto, String loginUSer, String taskCode, String status) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_task_det");

			String sql = "INSERT INTO public.nt_t_task_det "
					+ "(tsd_seq, tsd_app_no, tsd_task_code, tsd_status, created_by, created_date) "
					+ "VALUES(?, ?, ?, ?, ?, ?);";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, dto.getApplicationNo());
			stmt.setString(3, taskCode);
			stmt.setString(4, status);
			stmt.setString(5, loginUSer);
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
		return false;
	}

	@Override
	public boolean updateRejectData(AmendmentDTO dto, String rejectReason, String logedUser) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isRejected = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_amendments SET amd_first_reject_remarks=? , amd_status='R', amd_modified_by=?, amd_modified_date=? "
					+ "WHERE amd_application_no=? ;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, rejectReason);
			stmt.setString(2, logedUser);
			stmt.setTimestamp(3, timestamp);
			stmt.setString(4, dto.getApplicationNo());
			int i = stmt.executeUpdate();

			if (i > 0) {
				isRejected = true;
			} else {
				isRejected = false;
			}
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return isRejected;
	}

	@Override
	public void saveServiceChange(AmendmentServiceDTO amendmentServiceDTO, String logedUser) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			String sql = "UPDATE nt_t_pm_application SET pm_service_type = ?,pm_route_no = ?,pm_route_flag = ?, pm_modified_by=?, pm_modified_date=?  WHERE pm_application_no = ?";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, amendmentServiceDTO.getServiceType());
			stmt.setString(2, amendmentServiceDTO.getRouteNo());
			stmt.setString(3, amendmentServiceDTO.getRouteFlag());
			stmt.setString(4, logedUser);
			stmt.setTimestamp(5, timestamp);
			stmt.setString(6, amendmentServiceDTO.getApplicationNo());

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
	public void saveAmendmentServiceChange(AmendmentDTO amendmentDTO) {

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_amendments");

			String sql = "INSERT INTO nt_m_amendments "
					+ "(seq, amd_application_no,amd_service_change_type,amd_permit_no,amd_queue_no,amd_created_by,amd_created_date,amd_status,amd_trn_type,amd_remarks,amd_new_busno, amd_existing_busno)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?)";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, amendmentDTO.getApplicationNo());
			stmt.setString(3, amendmentDTO.getServiceChangeType());
			stmt.setString(4, amendmentDTO.getPermitNo());
			stmt.setString(5, amendmentDTO.getQueueNo());
			stmt.setString(6, amendmentDTO.getCreatedBy());
			stmt.setTimestamp(7, timestamp);
			stmt.setString(8, "P");
			stmt.setString(9, amendmentDTO.getTrnType());
			stmt.setString(10, amendmentDTO.getRemarks());

			stmt.setString(11, amendmentDTO.getBusRegNo());
			stmt.setString(12, amendmentDTO.getOldBusNo());

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
	public AfterAccidentDTO getAfterAccident(String vehicleRegNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		AfterAccidentDTO accidentDTO = new AfterAccidentDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT seqno, acm_vehicleno, acm_isface_for_medi_test, acm_is_paricipate_training, acm_is_licence_cancelled, acm_cancel_period, acm_any_legalcase_notclosed"
					+ " FROM public.nt_t_accident_master " + " WHERE acm_vehicleno =  ? ";

			ps = con.prepareStatement(query);
			ps.setString(1, vehicleRegNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				accidentDTO.setSeq(rs.getLong("seqno"));
				accidentDTO.setVehicleNo(rs.getString("acm_vehicleno"));

				if (rs.getString("acm_isface_for_medi_test") != null
						&& rs.getString("acm_isface_for_medi_test").equalsIgnoreCase("Y")) {
					accidentDTO.setIsFacedMediTest("true");
				} else if (rs.getString("acm_isface_for_medi_test") != null
						&& rs.getString("acm_isface_for_medi_test").equalsIgnoreCase("N")) {
					accidentDTO.setIsFacedMediTest("false");
				} else {
					accidentDTO.setIsFacedMediTest("false");
				}

				if (rs.getString("acm_is_paricipate_training") != null
						&& rs.getString("acm_is_paricipate_training").equalsIgnoreCase("Y")) {
					accidentDTO.setIsParicipateTraining("true");
				} else if (rs.getString("acm_is_paricipate_training") != null
						&& rs.getString("acm_is_paricipate_training").equalsIgnoreCase("N")) {
					accidentDTO.setIsParicipateTraining("false");
				} else {
					accidentDTO.setIsParicipateTraining("false");
				}

				if (rs.getString("acm_is_licence_cancelled") != null
						&& rs.getString("acm_is_licence_cancelled").equalsIgnoreCase("Y")) {
					accidentDTO.setIsLicenseCancelled("true");
				} else if (rs.getString("acm_is_licence_cancelled") != null
						&& rs.getString("acm_is_licence_cancelled").equalsIgnoreCase("N")) {
					accidentDTO.setIsLicenseCancelled("false");
				} else {
					accidentDTO.setIsLicenseCancelled("false");
				}

				accidentDTO.setCancelPeriod(rs.getString("acm_cancel_period"));

				if (rs.getString("acm_any_legalcase_notclosed") != null
						&& rs.getString("acm_any_legalcase_notclosed").equalsIgnoreCase("Y")) {
					accidentDTO.setAnyLegalCaseNotClosed("true");
				} else if (rs.getString("acm_any_legalcase_notclosed") != null
						&& rs.getString("acm_any_legalcase_notclosed").equalsIgnoreCase("N")) {
					accidentDTO.setAnyLegalCaseNotClosed("false");
				} else {
					accidentDTO.setAnyLegalCaseNotClosed("false");
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return accidentDTO;

	}

	@Override
	public void updateAfterAccident(AfterAccidentDTO accidentDTO) {
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			String sql = "UPDATE nt_t_accident_master SET acm_isface_for_medi_test = ? , acm_is_paricipate_training = ?, acm_is_licence_cancelled = ?, acm_cancel_period = ?,acm_any_legalcase_notclosed = ?, acm_modified_by=?, acm_modified_date=?"
					+ " WHERE seqno= ? ";

			stmt = con.prepareStatement(sql);

			if (accidentDTO.getIsFacedMediTest() != null && accidentDTO.getIsFacedMediTest().equalsIgnoreCase("true")) {
				stmt.setString(1, "Y");
			} else if (accidentDTO.getIsFacedMediTest() != null
					&& accidentDTO.getIsFacedMediTest().equalsIgnoreCase("false")) {
				stmt.setString(1, "N");
			} else {
				stmt.setString(1, null);
			}

			if (accidentDTO.getIsParicipateTraining() != null
					&& accidentDTO.getIsParicipateTraining().equalsIgnoreCase("true")) {
				stmt.setString(2, "Y");
			} else if (accidentDTO.getIsParicipateTraining() != null
					&& accidentDTO.getIsParicipateTraining().equalsIgnoreCase("false")) {
				stmt.setString(2, "N");
			} else {
				stmt.setString(2, null);
			}

			if (accidentDTO.getIsLicenseCancelled() != null
					&& accidentDTO.getIsLicenseCancelled().equalsIgnoreCase("true")) {
				stmt.setString(3, "Y");
			} else if (accidentDTO.getIsLicenseCancelled() != null
					&& accidentDTO.getIsLicenseCancelled().equalsIgnoreCase("false")) {
				stmt.setString(3, "N");
			} else {
				stmt.setString(3, null);
			}

			stmt.setString(4, accidentDTO.getCancelPeriod());

			if (accidentDTO.getAnyLegalCaseNotClosed() != null
					&& accidentDTO.getAnyLegalCaseNotClosed().equalsIgnoreCase("true")) {
				stmt.setString(5, "Y");
			} else if (accidentDTO.getAnyLegalCaseNotClosed() != null
					&& accidentDTO.getAnyLegalCaseNotClosed().equalsIgnoreCase("false")) {
				stmt.setString(5, "N");
			} else {
				stmt.setString(5, null);
			}

			stmt.setString(6, accidentDTO.getModifiedBy());

			stmt.setTimestamp(7, timestamp);

			stmt.setLong(8, accidentDTO.getSeq());

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
	public void updateAmendmentDetails(AmendmentDTO amendmentDTO) {
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE nt_m_amendments SET amd_is_bus_running  = ?, amd_hv_legal_case  = ?, amd_details_of_case = ?, amd_modified_by = ?, amd_modified_date = ?, amd_relationship_with_transferor = ?,amd_transferor_remarks = ?,amd_reasonfor_ownerchange = ?,amd_new_busno = ?, amd_existing_busno = ?, amd_service_change_type = ?,amd_remarks = ?,amd_is_omnibus = ?"
					+ " WHERE seq = ? ";

			stmt = con.prepareStatement(sql);

			if (amendmentDTO.getIsBusRunning() != null && amendmentDTO.getIsBusRunning().equalsIgnoreCase("true")) {
				stmt.setString(1, "Y");
			} else if (amendmentDTO.getIsBusRunning() != null
					&& amendmentDTO.getIsBusRunning().equalsIgnoreCase("false")) {
				stmt.setString(1, "N");
			} else {
				stmt.setString(1, null);
			}

			if (amendmentDTO.getHaveLegalCase() != null && amendmentDTO.getHaveLegalCase().equalsIgnoreCase("true")) {
				stmt.setString(2, "Y");
			} else if (amendmentDTO.getHaveLegalCase() != null
					&& amendmentDTO.getHaveLegalCase().equalsIgnoreCase("false")) {
				stmt.setString(2, "N");
			} else {
				stmt.setString(2, null);
			}

			stmt.setString(3, amendmentDTO.getLegalCaseDetails());

			stmt.setString(4, amendmentDTO.getModifiedBy());

			stmt.setTimestamp(5, timestamp);

			stmt.setString(6, amendmentDTO.getRelationshipWithTransferor());

			stmt.setString(7, amendmentDTO.getRelationshipWithTransferorRemarks());

			stmt.setString(8, amendmentDTO.getReasonForOwnerChange());

			stmt.setString(9, amendmentDTO.getBusRegNo());
			stmt.setString(10, amendmentDTO.getOldBusNo());
			stmt.setString(11, amendmentDTO.getServiceChangeType());
			stmt.setString(12, amendmentDTO.getRemarks());

			if (amendmentDTO.getIsOminiBus() != null && amendmentDTO.getIsOminiBus().equalsIgnoreCase("true")) {
				stmt.setString(13, "Y");
			} else if (amendmentDTO.getIsOminiBus() != null && amendmentDTO.getIsOminiBus().equalsIgnoreCase("false")) {
				stmt.setString(13, "N");
			} else {
				stmt.setString(13, null);
			}

			stmt.setLong(14, amendmentDTO.getSeq());

			stmt.executeUpdate();

			con.commit();

		}catch (SQLException e) {
			try {
				if(con != null) {
					con.rollback();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
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
	public AmendmentDTO getAmendmentDetails(String generatedApplicationNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		AmendmentDTO amendmentDTO = new AmendmentDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT amd_is_omnibus, seq, amd_is_bus_running,amd_hv_legal_case, amd_details_of_case,amd_reasonfor_ownerchange,amd_relationship_with_transferor,amd_transferor_remarks,amd_service_change_type,amd_remarks,amd_new_busno,amd_existing_busno FROM nt_m_amendments WHERE amd_application_no = ?";

			ps = con.prepareStatement(query);
			ps.setString(1, generatedApplicationNo);

			rs = ps.executeQuery();

			while (rs.next()) {

				if (rs.getString("amd_is_omnibus") != null && rs.getString("amd_is_omnibus").equalsIgnoreCase("Y")) {
					amendmentDTO.setIsOminiBus("true");
				} else if (rs.getString("amd_is_omnibus") != null
						&& rs.getString("amd_is_omnibus").equalsIgnoreCase("N")) {
					amendmentDTO.setIsOminiBus("false");
				} else {
					amendmentDTO.setIsOminiBus("false");
				}

				amendmentDTO.setSeq(rs.getLong("seq"));

				if (rs.getString("amd_is_bus_running") != null
						&& rs.getString("amd_is_bus_running").equalsIgnoreCase("Y")) {
					amendmentDTO.setIsBusRunning("true");
				} else if (rs.getString("amd_is_bus_running") != null
						&& rs.getString("amd_is_bus_running").equalsIgnoreCase("N")) {
					amendmentDTO.setIsBusRunning("false");
				} else {
					amendmentDTO.setIsBusRunning("false");
				}

				if (rs.getString("amd_hv_legal_case") != null
						&& rs.getString("amd_hv_legal_case").equalsIgnoreCase("Y")) {
					amendmentDTO.setHaveLegalCase("true");
				} else if (rs.getString("amd_hv_legal_case") != null
						&& rs.getString("amd_hv_legal_case").equalsIgnoreCase("N")) {
					amendmentDTO.setHaveLegalCase("false");
				} else {
					amendmentDTO.setHaveLegalCase("false");
				}

				amendmentDTO.setLegalCaseDetails(rs.getString("amd_details_of_case"));

				amendmentDTO.setRelationshipWithTransferor(rs.getString("amd_relationship_with_transferor"));
				amendmentDTO.setRelationshipWithTransferorRemarks(rs.getString("amd_transferor_remarks"));
				amendmentDTO.setReasonForOwnerChange(rs.getString("amd_reasonfor_ownerchange"));

				amendmentDTO.setServiceChangeType(rs.getString("amd_service_change_type"));

				amendmentDTO.setRemarks(rs.getString("amd_remarks"));

				amendmentDTO.setBusRegNo(rs.getString("amd_new_busno"));
				amendmentDTO.setOldBusNo(rs.getString("amd_existing_busno"));
				amendmentDTO.setExisitingBusNo(rs.getString("amd_existing_busno"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return amendmentDTO;

	}

	@Override
	public void updateVehicleOfApplication(String vehicleNo, String applicationNo, String logingUser) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			String sql = "UPDATE nt_t_pm_application SET pm_vehicle_regno = ?, pm_modified_by=?, pm_modified_date=? "
					+ "  WHERE pm_application_no = ?";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, vehicleNo);
			stmt.setString(2, logingUser);
			stmt.setTimestamp(3, timestamp);
			stmt.setString(4, applicationNo);
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
	public void updateOldApplicationNoOfApplication(String oldApplicationNo, String applicationNo, String logingUser) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			String sql = "UPDATE nt_t_pm_application SET pm_old_application_no = ?, pm_modified_by=?, pm_modified_date=? "
					+ " WHERE pm_application_no = ?";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, oldApplicationNo);
			stmt.setString(2, logingUser);
			stmt.setTimestamp(3, timestamp);
			stmt.setString(4, applicationNo);
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
	public AmendmentServiceDTO getServiceChange(String generatedApplicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		AmendmentServiceDTO amendmentDTO = new AmendmentServiceDTO();

		try {
			con = ConnectionManager.getConnection();

			// String query = "SELECT pm_service_type, pm_route_no,pm_route_flag FROM
			// public.nt_t_pm_application WHERE pm_status='P' AND pm_application_no = ?";
			String query = "SELECT pm_service_type, pm_route_no,pm_route_flag FROM public.nt_t_pm_application WHERE pm_application_no =  ?";
			ps = con.prepareStatement(query);
			ps.setString(1, generatedApplicationNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				amendmentDTO.setServiceType(rs.getString("pm_service_type"));
				amendmentDTO.setRouteNo(rs.getString("pm_route_no"));
				amendmentDTO.setRouteFlag(rs.getString("pm_route_flag"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return amendmentDTO;
	}

	@Override
	public String getOldVehicleNoFromAmendment(String generatedApplicationNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String vehicleNO = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT amd_existing_busno FROM nt_m_amendments WHERE amd_application_no=?";

			ps = con.prepareStatement(query);
			ps.setString(1, generatedApplicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				vehicleNO = (rs.getString("amd_existing_busno"));

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return vehicleNO;

	}

	@Override
	public String getNewVehicleNoFromAmendment(String generatedApplicationNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String vehicleNO = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT amd_new_busno FROM nt_m_amendments WHERE amd_application_no=?";

			ps = con.prepareStatement(query);
			ps.setString(1, generatedApplicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				vehicleNO = (rs.getString("amd_new_busno"));

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return vehicleNO;

	}

	@Override
	public String getTrnTypeFromAmendment(String generatedApplicationNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String trnType = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT amd_trn_type FROM nt_m_amendments WHERE amd_application_no=?";

			ps = con.prepareStatement(query);
			ps.setString(1, generatedApplicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				trnType = (rs.getString("amd_trn_type"));

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return trnType;

	}

	@Override
	public boolean isMandatory(String currentDocCode, String applicationNo, String permitNo, String strTrnCode) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean ismandatory = false;

		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT  distinct * FROM public.nt_m_document "
					+ "where doc_transaction_type=? and doc_code=? and doc_mandatory_doc='true'";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, strTrnCode);
			stmt.setString(2, currentDocCode);
			rs = stmt.executeQuery();

			if (rs.next()) {
				ismandatory = true;
			} else {
				ismandatory = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return ismandatory;
	}

	@Override
	public boolean isPhysicallyExit(String currentDocCode, String applicationNo, String permitNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isPhysicallyExits = false;

		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT * FROM public.nt_m_document_check "
					+ "where  dck_application_no=? and dck_permit_no =? and dck_code=? and dck_physicaly_exist='true'; ";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, applicationNo);
			stmt.setString(2, permitNo);
			stmt.setString(3, currentDocCode);
			rs = stmt.executeQuery();

			if (rs.next()) {
				isPhysicallyExits = true;
			} else {
				isPhysicallyExits = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return isPhysicallyExits;
	}

	@Override
	public boolean insertDocumentPhysicallyExitsStatus(boolean exits, String currentDocCode, String applicationNo,
			String permitNo, String logUser, String tranCode) {

		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		boolean isPhysicallyExits = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String selectSQL = "SELECT dck_transaction_type, dck_code, dck_application_no, dck_permit_no, dck_physicaly_exist "
					+ "FROM public.nt_m_document_check "
					+ "where dck_code=? and dck_application_no=? and dck_permit_no=? "
					+ "and dck_physicaly_exist='true';";

			stmt2 = con.prepareStatement(selectSQL);

			stmt2.setString(1, currentDocCode);
			stmt2.setString(2, applicationNo);
			stmt2.setString(3, permitNo);
			rs = stmt2.executeQuery();

			if (rs.next() == true) {

			} else {

				String sql = "INSERT INTO public.nt_m_document_check "
						+ "(seqno, dck_transaction_type, dck_code, dck_application_no, dck_permit_no, dck_physicaly_exist, dck_created_by, dck_created_date) "
						+ "VALUES(?, ?, ?,?, ?, ?, ?, ? );";

				long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_document_check");

				stmt = con.prepareStatement(sql);
				stmt.setLong(1, seqNo);
				stmt.setString(2, tranCode);
				stmt.setString(3, currentDocCode);
				stmt.setString(4, applicationNo);
				stmt.setString(5, permitNo);
				stmt.setString(6, "true");
				stmt.setString(7, logUser);
				stmt.setTimestamp(8, timestamp);
				int i = stmt.executeUpdate();

				if (i > 0) {
					isPhysicallyExits = true;
				} else {
					isPhysicallyExits = false;
				}

				con.commit();

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return isPhysicallyExits;
	}

	// Committee/Board Approval

	public List<CommitteeOrBoardApprovalDTO> getAmendmentsApplicationNoForCommitteeApproval(String code) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommitteeOrBoardApprovalDTO> amendmentsNoList = new ArrayList<CommitteeOrBoardApprovalDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct a.tsd_app_no FROM public.nt_t_task_det a left outer join nt_m_amendments b on a.tsd_app_no= b.amd_application_no where \r\n"
					+ "(a.tsd_task_code='PM302' and a.tsd_status='C'\r\n"
					+ "or a.tsd_task_code='AM101' and a.tsd_status='O'\r\n"
					+ "or a.tsd_task_code='AM101' and a.tsd_status='C'\r\n"
					+ "or a.tsd_task_code='AM102' and a.tsd_status='O') and b.amd_trn_type='" + code + "'\r\n"
					+ "order by a.tsd_app_no\r\n" + "";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				CommitteeOrBoardApprovalDTO committeeApprovalDTO = new CommitteeOrBoardApprovalDTO();

				committeeApprovalDTO.setCommitteeAmendmentsApplicationNo(rs.getString("tsd_app_no"));

				amendmentsNoList.add(committeeApprovalDTO);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return amendmentsNoList;
	}

	public List<CommitteeOrBoardApprovalDTO> getAmendmentsPermitNoForCommitteeApproval() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommitteeOrBoardApprovalDTO> amendmentsNoList = new ArrayList<CommitteeOrBoardApprovalDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct amd_permit_no FROM public.nt_m_amendments where amd_board_approved is null and amd_permit_no is not null order by amd_permit_no";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				CommitteeOrBoardApprovalDTO committeeApprovalDTO = new CommitteeOrBoardApprovalDTO();

				committeeApprovalDTO.setCommitteeAmendmentsPermitNo(rs.getString("amd_permit_no"));

				amendmentsNoList.add(committeeApprovalDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return amendmentsNoList;
	}

	public String getAmendmentPermitNo(String applicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String permitNo = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT amd_permit_no FROM public.nt_m_amendments where amd_application_no = '"
					+ applicationNo + "'";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {

				permitNo = rs.getString("amd_permit_no");

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

	public String getAmendmentApplicationNo(String permitNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String applicationNo = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT amd_application_no FROM public.nt_m_amendments where amd_permit_no = '" + permitNo
					+ "'";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {

				applicationNo = rs.getString("amd_application_no");

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

	public String getApprovalStatus(String applicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String status = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT amd_commity_approved FROM public.nt_m_amendments where amd_application_no='"
					+ applicationNo + "'";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {

				status = rs.getString("amd_commity_approved");

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

	public int insertApproveStatus(String applicationNo, String remark, String user) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_amendments SET amd_commity_approved='A', amd_committee_remark='" + remark
					+ "',amd_modified_by='" + user + "', amd_modified_date='" + timestamp + "'"
					+ "WHERE amd_application_no='" + applicationNo + "';";

			stmt = con.prepareStatement(sql);

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

	public int insertBoardRejectStatus(String applicationNo, String status, String remark, String user) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_amendments SET amd_board_approved='" + status
					+ "',amd_board_reject_reason='" + remark + "',amd_modified_by='" + user + "', amd_modified_date='"
					+ timestamp + "'" + "WHERE amd_application_no='" + applicationNo + "';";

			stmt = con.prepareStatement(sql);

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

	public int insertBoardApproveStatus(String applicationNo, String status, String user) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_amendments" + "SET amd_board_approved='" + status + "',amd_modified_by='"
					+ user + "', amd_modified_date='" + timestamp + "'" + "WHERE amd_application_no='" + applicationNo
					+ "';";

			stmt = con.prepareStatement(sql);

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
	public boolean updateNewPermitNoOnAmendmentForOwnerChange(AmendmentDTO dto, String newPermitNo, String logingUser) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isupdateed = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_amendments "
					+ "SET amd_permit_no=? , amd_status='C', amd_modified_by=?, amd_modified_date=? "
					+ "WHERE amd_application_no=? ;";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, newPermitNo);
			stmt.setString(2, logingUser);
			stmt.setTimestamp(3, timestamp);
			stmt.setString(4, dto.getApplicationNo());

			int i = stmt.executeUpdate();

			if (i > 0) {
				isupdateed = true;
			} else {
				isupdateed = false;
			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return isupdateed;
	}

	@Override
	public boolean updateApplicationTableOldRecordForOwnerChange(AmendmentDTO dto, String newPermitNo) {

		Connection con = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		ResultSet rs = null;
		boolean isUpdate = false;
		try {

			con = ConnectionManager.getConnection();

			String q2 = "select pm_old_application_no from public.nt_t_pm_application where pm_application_no=? ";

			ps2 = con.prepareStatement(q2);
			ps2.setString(1, dto.getApplicationNo());
			rs = ps2.executeQuery();

			if (rs.next() == true) {

				String q3 = "update public.nt_t_pm_application set pm_status='I' where pm_application_no=?";

				ps3 = con.prepareStatement(q3);
				ps3.setString(1, rs.getString("pm_old_application_no"));
				int i = ps3.executeUpdate();

				if (i > 0) {
					isUpdate = true;
				} else {
					isUpdate = false;
				}
				con.commit();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps2);
			ConnectionManager.close(ps3);
			ConnectionManager.close(con);
		}

		return isUpdate;

	}

	@Override
	public boolean updateApplicationTableNewRecordForOwnerChange(AmendmentDTO dto, String newPermitNo,
			String logingUser) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isupdateed = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "update public.nt_t_pm_application set pm_permit_no=? , pm_status='A', pm_modified_by=?, pm_modified_date=? "
					+ "where pm_application_no=? and pm_status='P'";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, newPermitNo);
			stmt.setString(2, logingUser);
			stmt.setTimestamp(3, timestamp);
			stmt.setString(4, dto.getApplicationNo());

			int i = stmt.executeUpdate();

			if (i > 0) {
				isupdateed = true;
			} else {
				isupdateed = false;
			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return isupdateed;
	}

	@Override
	public boolean updateAmendmentForServiceAndBusChange(AmendmentDTO dto, String logingUser) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isupdateed = false;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_amendments "
					+ "SET amd_status='C', amd_modified_by=?, amd_modified_date=? WHERE amd_application_no=? ;";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, logingUser);
			stmt.setTimestamp(2, timestamp);
			stmt.setString(3, dto.getApplicationNo());

			int i = stmt.executeUpdate();

			if (i > 0) {
				isupdateed = true;
			} else {
				isupdateed = false;
			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return isupdateed;
	}

	@Override
	public boolean updateApplicationTableOldRecordForServiceAndBusChange(AmendmentDTO dto) {

		Connection con = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		ResultSet rs = null;
		boolean isUpdate = false;
		String oldApplicationNo = null;
		try {

			con = ConnectionManager.getConnection();

			String q2 = "select pm_old_application_no from public.nt_t_pm_application where pm_application_no=? ";

			ps2 = con.prepareStatement(q2);
			ps2.setString(1, dto.getApplicationNo());
			rs = ps2.executeQuery();

			if (rs.next() == true) {
				oldApplicationNo = rs.getString("pm_old_application_no");
				String q3 = "update public.nt_t_pm_application set pm_status='I' where pm_application_no=?";

				ps3 = con.prepareStatement(q3);
				ps3.setString(1, oldApplicationNo);
				int i = ps3.executeUpdate();

				if (i > 0) {
					isUpdate = true;
				} else {
					isUpdate = false;
				}
				con.commit();

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps2);
			ConnectionManager.close(ps3);
			ConnectionManager.close(con);
		}

		return isUpdate;

	}

	@Override
	public boolean updateApplicationTableNewRecordForServiceAndBusChange(AmendmentDTO dto, String logingUser) {
		Connection con = null;
		PreparedStatement stmt = null, stmt1 = null, stmt2 = null, stmt0 = null;
		ResultSet rs = null;
		boolean isupdateed = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		String val = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "update public.nt_t_pm_application set pm_status='A', pm_modified_by=?, pm_modified_date=? "
					+ "where pm_application_no=? and pm_status='P'";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, logingUser);
			stmt.setTimestamp(2, timestamp);
			stmt.setString(3, dto.getApplicationNo());

			int i = stmt.executeUpdate();

			/* New Change For SIM Integration On 03/04/2020 */

			String sql0 = "select sim_bus_no from nt_m_sim_registration where sim_permit_no = ? and sim_status = 'A' ";
			stmt0 = con.prepareStatement(sql0);
			stmt0.setString(1, dto.getPermitNo());
			rs = stmt0.executeQuery();

			while (rs.next()) {
				val = rs.getString("sim_bus_no");
			}

			if (val != null) {
				String sql1 = "INSERT into public.nt_h_sim_registration "
						+ " (SELECT * FROM public.nt_m_sim_registration WHERE sim_permit_no = ? and sim_status = 'A') ";

				stmt1 = con.prepareStatement(sql1);
				stmt1.setString(1, dto.getPermitNo());
				stmt1.executeUpdate();

				String sql2 = "update nt_m_sim_registration " + " set sim_bus_no = ? "
						+ " where sim_permit_no = ? and sim_status = 'A'";

				stmt2 = con.prepareStatement(sql2);
				stmt2.setString(1, dto.getNewBusNo());
				stmt2.setString(2, dto.getPermitNo());
				stmt2.executeUpdate();

			}

			/* End Change */

			if (i > 0) {
				isupdateed = true;
			} else {
				isupdateed = false;
			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(stmt2);
			ConnectionManager.close(stmt1);
			ConnectionManager.close(stmt0);
			ConnectionManager.close(stmt);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);

		}

		return isupdateed;
	}

	@Override
	public boolean deleteDocumentPhysicallyExitsStatus(boolean exits, String currentDocCode, String applicationNo,
			String permitNo, String logUser, String tranCode) {

		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		boolean isDeletePhysicallyExits = false;

		try {
			con = ConnectionManager.getConnection();

			String selectSQL = "SELECT dck_transaction_type, dck_code, dck_application_no, dck_permit_no, dck_physicaly_exist "
					+ "FROM public.nt_m_document_check "
					+ "where dck_transaction_type=? and dck_code=? and dck_application_no=? "
					+ "and dck_physicaly_exist='true';";

			stmt2 = con.prepareStatement(selectSQL);

			stmt2.setString(1, tranCode);
			stmt2.setString(2, currentDocCode);
			stmt2.setString(3, applicationNo);

			rs = stmt2.executeQuery();

			if (rs.next() == false) {
			} else {

				String sql = "DELETE FROM public.nt_m_document_check "
						+ "WHERE dck_transaction_type=? and dck_code=? and dck_application_no=? and dck_physicaly_exist='true'";

				stmt = con.prepareStatement(sql);

				stmt.setString(1, tranCode);
				stmt.setString(2, currentDocCode);
				stmt.setString(3, applicationNo);

				int i = stmt.executeUpdate();

				if (i > 0) {
					isDeletePhysicallyExits = true;
				} else {
					isDeletePhysicallyExits = false;
				}

				con.commit();

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(con);
		}

		return isDeletePhysicallyExits;
	}

	@Override
	public List<AmendmentDTO> getTransactionType() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<AmendmentDTO> data = new ArrayList<AmendmentDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct amd_trn_type,  nt_r_transaction_type.description \r\n"
					+ "FROM public.nt_m_amendments \r\n"
					+ "inner join public.nt_r_transaction_type on nt_r_transaction_type.code=nt_m_amendments.amd_trn_type";
					

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			AmendmentDTO dto;

			while (rs.next()) {
				dto = new AmendmentDTO();
				dto.setTranCode(rs.getString("amd_trn_type"));
				dto.setTransactionType(rs.getString("description"));
				data.add(dto);

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
	public boolean availabelForFirstApproval(AmendmentDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isTaskAvailable = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "select * from public.nt_m_amendments "
					+ "inner join public.nt_t_pm_application on nt_t_pm_application.pm_application_no= nt_m_amendments.amd_application_no "
					+ "where nt_t_pm_application.pm_reinspec_status='C' and nt_t_pm_application.pm_status='P' and nt_m_amendments.amd_status='P' "
					+ "and nt_m_amendments.amd_application_no=? ";

			ps = con.prepareStatement(query);
			ps.setString(1, dto.getApplicationNo());
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
	public boolean updateAmendmentsTableStatus(AmendmentDTO dto, String status, String logedUser) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		boolean isTaskAvailable = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_m_amendments SET amd_status=?, amd_modified_by=?, amd_modified_date=? "
					+ " WHERE amd_application_no=? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, status);
			ps.setString(2, logedUser);
			ps.setTimestamp(3, timestamp);
			ps.setString(4, dto.getApplicationNo());
			int i = ps.executeUpdate();

			if (i > 0) {
				isTaskAvailable = true;
			} else {
				isTaskAvailable = false;
			}

			con.commit();

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
	public boolean updateAmendmentsTableCommitteeStatus(AmendmentDTO dto, String status, String logedUser) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isUpdate = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			String query = "update public.nt_m_amendments set amd_commity_approved=? ,amd_modified_by=?, amd_modified_date=? "
					+ "where amd_application_no=? ";

			ps = con.prepareStatement(query);
			ps.setString(1, status);
			ps.setString(2, logedUser);
			ps.setTimestamp(3, timestamp);
			ps.setString(4, dto.getApplicationNo());
			int i = ps.executeUpdate();

			if (i > 0) {
				isUpdate = true;
			} else {
				isUpdate = false;
			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isUpdate;
	}

	@Override
	public boolean updateAmendmentsTableBoardStatus(AmendmentDTO dto, String status, String logedUser) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		boolean isUpdate = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "update public.nt_m_amendments set amd_board_approved=?, amd_modified_by=?, amd_modified_date=? "
					+ "where amd_application_no=? ";

			ps = con.prepareStatement(query);
			ps.setString(1, status);
			ps.setString(2, logedUser);
			ps.setTimestamp(3, timestamp);
			ps.setString(4, dto.getApplicationNo());
			int i = ps.executeUpdate();

			if (i > 0) {
				isUpdate = true;
			} else {
				isUpdate = false;
			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isUpdate;
	}

	@Override
	public boolean CheckAmendmentsTableStatus(AmendmentDTO dto, String status) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isUpdate = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "select * from public.nt_m_amendments where amd_application_no=? and  amd_status= ? ;";

			ps = con.prepareStatement(query);

			ps.setString(1, dto.getApplicationNo());

			if (status != null) {
				ps.setString(2, status);
			} else {
				ps.setString(2, null);
			}

			rs = ps.executeQuery();

			if (rs.next()) {
				isUpdate = true;
			} else {
				isUpdate = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isUpdate;
	}

	@Override
	public boolean CheckAmendmentsTableCommitteeStatus(AmendmentDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isUpdate = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "select * from public.nt_m_amendments where "
					+ "(amd_application_no=? and ( amd_commity_approved ='A' or amd_commity_approved ='R' ));";

			ps = con.prepareStatement(query);
			ps.setString(1, dto.getApplicationNo());

			rs = ps.executeQuery();

			if (rs.next()) {
				isUpdate = true;
			} else {
				isUpdate = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isUpdate;
	}

	@Override
	public boolean CheckAmendmentsTableBoarderStatus(AmendmentDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isUpdate = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "select * from public.nt_m_amendments where "
					+ "(amd_application_no=? and ( amd_board_approved ='A' or amd_board_approved ='R' ));";

			ps = con.prepareStatement(query);
			ps.setString(1, dto.getApplicationNo());

			rs = ps.executeQuery();

			if (rs.next()) {
				isUpdate = true;
			} else {
				isUpdate = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isUpdate;
	}

	@Override
	public boolean checkTwoChanges(AmendmentDTO dto, String transActionOne, String transActionTwo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isUpdate = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "select * from public.nt_m_amendments "
					+ "where (amd_existing_busno=? and amd_status='P' and (amd_trn_type=? or amd_trn_type=?))";

			ps = con.prepareStatement(query);
			ps.setString(1, dto.getExisitingBusNo());
			ps.setString(2, transActionOne);
			ps.setString(3, transActionTwo);

			rs = ps.executeQuery();

			if (rs.next()) {
				isUpdate = true;
			} else {
				isUpdate = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isUpdate;
	}

	@Override
	public boolean checkOneChanges(AmendmentDTO dto, String transActionOne) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isUpdate = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "select * from public.nt_m_amendments "
					+ "where amd_existing_busno=? and amd_status='P' and amd_trn_type=? ";

			ps = con.prepareStatement(query);
			ps.setString(1, dto.getOldBusNo());
			ps.setString(2, transActionOne);

			rs = ps.executeQuery();

			if (rs.next()) {
				isUpdate = true;
			} else {
				isUpdate = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isUpdate;
	}

	public String getBoardApprovalStatus(String applicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String status = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT amd_board_approved FROM public.nt_m_amendments where amd_application_no='"
					+ applicationNo + "'";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				status = rs.getString("amd_board_approved");
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

	public List<CommitteeOrBoardApprovalDTO> getViewAmendmentsNoForCommitteeApprovalStatus(String transCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommitteeOrBoardApprovalDTO> amendmentNoList = new ArrayList<CommitteeOrBoardApprovalDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT amd_application_no FROM public.nt_m_amendments where amd_trn_type='" + transCode
					+ "' and (amd_commity_approved='A' or amd_board_approved='A') order by amd_application_no";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				CommitteeOrBoardApprovalDTO committeeApprovalDTO = new CommitteeOrBoardApprovalDTO();

				committeeApprovalDTO.setCommitteeAmendmentsApplicationNo(rs.getString("amd_application_no"));

				amendmentNoList.add(committeeApprovalDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return amendmentNoList;
	}

	public PermitRenewalsDTO renewalsByBusRegNo(String strVehicleNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();

		try {
			con = ConnectionManager.getConnection();
			String query = "SELECT pm_special_remark ,  pm_permit_expire_date , pm_renewal_period ,pm_valid_to ,pm_valid_from , pm_new_permit_expiry_date, pm_is_backlog_app  FROM public.nt_t_pm_application  where  pm_vehicle_regno=? ; ";

			ps = con.prepareStatement(query);
			ps.setString(1, strVehicleNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				permitRenewalsDTO.setSpecialRemark(rs.getString("pm_special_remark"));
				permitRenewalsDTO.setPermitExpiryDate(rs.getString("pm_permit_expire_date"));
				permitRenewalsDTO.setRequestRenewPeriod(rs.getInt("pm_renewal_period"));
				permitRenewalsDTO.setValidToDate(rs.getString("pm_valid_to"));
				permitRenewalsDTO.setFromToDate(rs.getString("pm_valid_from"));
				permitRenewalsDTO.setBacklogAppValue(rs.getString("pm_is_backlog_app"));
				permitRenewalsDTO.setNewPermitExpirtDate(rs.getString("pm_new_permit_expiry_date"));
				String backlogValue = permitRenewalsDTO.getBacklogAppValue();
				if (backlogValue == null || backlogValue.isEmpty() || backlogValue.equals(null)) {
					permitRenewalsDTO.setCheckBacklogValue(true);
				} else if (backlogValue.equals("Y")) {
					permitRenewalsDTO.setCheckBacklogValue(true);
				} else if (backlogValue.equals("N")) {
					permitRenewalsDTO.setCheckBacklogValue(false);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return permitRenewalsDTO;
	}

	public PermitRenewalsDTO renewalsByBusRegNoNew(String strVehicleNo, String strApplicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();

		try {
			con = ConnectionManager.getConnection();
			String query = "SELECT pm_special_remark ,  pm_permit_expire_date , pm_renewal_period ,pm_valid_to ,pm_valid_from , "
					+ " pm_new_permit_expiry_date, pm_is_backlog_app  FROM public.nt_t_pm_application  "
					+ " WHERE  pm_vehicle_regno=? and pm_application_no=? ; ";

			ps = con.prepareStatement(query);
			ps.setString(1, strVehicleNo);
			ps.setString(2, strApplicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				permitRenewalsDTO.setSpecialRemark(rs.getString("pm_special_remark"));
				permitRenewalsDTO.setPermitExpiryDate(rs.getString("pm_permit_expire_date"));
				permitRenewalsDTO.setRequestRenewPeriod(rs.getInt("pm_renewal_period"));
				permitRenewalsDTO.setValidToDate(rs.getString("pm_valid_to"));
				permitRenewalsDTO.setFromToDate(rs.getString("pm_valid_from"));
				permitRenewalsDTO.setBacklogAppValue(rs.getString("pm_is_backlog_app"));
				permitRenewalsDTO.setNewPermitExpirtDate(rs.getString("pm_new_permit_expiry_date"));
				String backlogValue = permitRenewalsDTO.getBacklogAppValue();
				if (backlogValue == null || backlogValue.isEmpty() || backlogValue.equals(null)) {
					permitRenewalsDTO.setCheckBacklogValue(true);
				} else if (backlogValue.equals("Y")) {
					permitRenewalsDTO.setCheckBacklogValue(true);
				} else if (backlogValue.equals("N")) {
					permitRenewalsDTO.setCheckBacklogValue(false);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return permitRenewalsDTO;
	}

	@Override
	public List<AmendmentDTO> getAmendmentsAuthorizationDefaultDetails(AmendmentDTO amendmentDTO, int accessLevel) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<AmendmentDTO> data = new ArrayList<AmendmentDTO>();
		String query = null;

		try {

			con = ConnectionManager.getConnection();

			/* Director Access Only */
			if (accessLevel == 1) {

				query = "select distinct amd_trn_type, amd_permit_no,amd_commity_approved,amd_board_approved, amd_status, amd_application_no, amd_new_busno, amd_existing_busno, "
						+ "nt_r_transaction_type.description, nt_r_transaction_type.code, nt_t_pm_vehi_owner.pmo_full_name, case when amd_modified_date is null then amd_created_date else  amd_modified_date end as created_date "
						+ "FROM public.nt_m_amendments "
						+ "inner join public.nt_r_transaction_type on nt_r_transaction_type.code = nt_m_amendments.amd_trn_type "
						+ "left outer join public.nt_t_pm_vehi_owner on nt_t_pm_vehi_owner.pmo_application_no = nt_m_amendments.amd_application_no "
						+ "inner join public.nt_t_task_det on nt_t_task_det.tsd_app_no=nt_m_amendments.amd_application_no "
						+ "where "
						+ "((amd_status='P' and nt_t_task_det.tsd_task_code='PM101' and nt_t_task_det.tsd_status='C') "
						+ "or (amd_status='GR' and nt_t_task_det.tsd_task_code='PM101' and nt_t_task_det.tsd_status='C' ))  "
						+ "order by created_date desc";

				/* Chairman Access Only */
			} else if (accessLevel == 2) {

				query = "select distinct amd_trn_type, amd_permit_no,amd_commity_approved,amd_board_approved, amd_status, amd_application_no, amd_new_busno, amd_existing_busno, "
						+ "nt_r_transaction_type.description, nt_r_transaction_type.code, nt_t_pm_vehi_owner.pmo_full_name, case when amd_modified_date is null then amd_created_date else  amd_modified_date end as created_date  "
						+ "FROM public.nt_m_amendments "
						+ "inner join public.nt_r_transaction_type on nt_r_transaction_type.code = nt_m_amendments.amd_trn_type "
						+ "left outer join public.nt_t_pm_vehi_owner on nt_t_pm_vehi_owner.pmo_application_no = nt_m_amendments.amd_application_no "
						+ "inner join public.nt_t_task_det on nt_t_task_det.tsd_app_no=nt_m_amendments.amd_application_no "
						+ "where amd_status='SA' order by created_date desc";

				/* Access For All */
			} else if (accessLevel == 3) {
				query = "select distinct amd_trn_type, amd_permit_no,amd_commity_approved,amd_board_approved, amd_status, amd_application_no, amd_new_busno, amd_existing_busno, "
						+ "nt_r_transaction_type.description, nt_r_transaction_type.code, nt_t_pm_vehi_owner.pmo_full_name, case when amd_modified_date is null then amd_created_date else  amd_modified_date end as created_date  "
						+ "FROM public.nt_m_amendments "
						+ "inner join public.nt_r_transaction_type on nt_r_transaction_type.code = nt_m_amendments.amd_trn_type "
						+ "left outer join public.nt_t_pm_vehi_owner on nt_t_pm_vehi_owner.pmo_application_no = nt_m_amendments.amd_application_no "
						+ "inner join public.nt_t_task_det on nt_t_task_det.tsd_app_no=nt_m_amendments.amd_application_no "
						+ "where (amd_status='SA' or "
						+ "((amd_status='P' and nt_t_task_det.tsd_task_code='PM101' and nt_t_task_det.tsd_status='C') "
						+ "or (amd_status='GR' and nt_t_task_det.tsd_task_code='PM101' and nt_t_task_det.tsd_status='C' )))  "
						+ "order by created_date desc";
			}

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			AmendmentDTO dto;

			while (rs.next()) {
				dto = new AmendmentDTO();
				dto.setPermitNo(rs.getString("amd_permit_no"));
				dto.setApplicationNo(rs.getString("amd_application_no"));
				dto.setServiceType(null);
				dto.setTranCode(rs.getString("code"));
				dto.setTransactionType(rs.getString("description"));
				dto.setOwnerName(rs.getString("pmo_full_name"));
				dto.setExisitingBusNo(rs.getString("amd_existing_busno"));
				dto.setNewBusNo(rs.getString("amd_new_busno"));

				String approveStatusCode = rs.getString("amd_status");
				String approveStatus = "";

				if (!approveStatusCode.equals("R")) {

					if (rs.getString("code").equals("13")) {

						if (approveStatusCode.equals("P")) {
							approveStatus = "PENDING";
						} else if (approveStatusCode.equals("DG")) {
							approveStatus = "DG / CHAIRMAN APPROVED";
						} else if (approveStatusCode.equals("FA")) {
							approveStatus = "APPROVED FOR PAYMENT";
						} else if (approveStatusCode.equals("AD")) {
							approveStatus = "AD / DIRECTOR APPROVED";
						} else if (approveStatusCode.equals("SA")) {
							approveStatus = "APPROVED FOR PRINTING";
						} else if (approveStatusCode.equals("C")) {
							approveStatus = "COMPLETE";
						} else {
							approveStatus = "N/A";
						}

					} else {
						if (approveStatusCode.equals("GR") && checkTaskDetails(dto, "PM101", "C")) {
							approveStatus = "PENDING";
						} else if (approveStatusCode.equals("GR") && checkTaskDetails(dto, "AM106", "C")) {
							approveStatus = "BALANCE TASK COMPLETED";
						} else if (approveStatusCode.equals("DG")) {
							approveStatus = "DG / CHAIRMAN APPROVED";
						} else if (approveStatusCode.equals("FA")) {
							approveStatus = "APPROVED FOR PAYMENT";
						} else if (approveStatusCode.equals("AD")) {
							approveStatus = "AD / DIRECTOR APPROVED";
						} else if (approveStatusCode.equals("SA")) {
							approveStatus = "APPROVED FOR PRINTING";
						} else if (approveStatusCode.equals("C")) {
							approveStatus = "COMPLETE";
						} else if (approveStatusCode.equals("GR")) {
							approveStatus = "RESPONSE LETTER GENERATED";
						} else {
							approveStatus = "N/A";
						}
					}

				} else {
					approveStatus = "REJECTED";
				}

				dto.setStatus(approveStatus);

				Timestamp ts = rs.getTimestamp("created_date");

				if (ts != null) {
					Date date = new Date();
					date.setTime(ts.getTime());
					String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
					dto.setCreatedDateString(formattedDate);
				} else {
					dto.setCreatedDateString("N/A");
				}

				data.add(dto);

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
	public List<AmendmentDTO> getAmendmentsHigherApporvalApplicationNO(AmendmentDTO dtos) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<AmendmentDTO> data = new ArrayList<AmendmentDTO>();
		String query = null;
		try {

			con = ConnectionManager.getConnection();

			query = "select distinct amd_application_no FROM public.nt_m_amendments "
					+ "WHERE amd_application_no != '' and  amd_trn_type=?  and amd_application_no is not null order by amd_application_no;";

			ps = con.prepareStatement(query);
			ps.setString(1, dtos.getTranCode());

			rs = ps.executeQuery();

			AmendmentDTO dto;

			while (rs.next()) {
				dto = new AmendmentDTO();
				dto.setApplicationNo(rs.getString("amd_application_no"));
				data.add(dto);

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
	public List<AmendmentDTO> getAmendmentsHigherApporvalPermitNO(AmendmentDTO dtos) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<AmendmentDTO> data = new ArrayList<AmendmentDTO>();
		String query = null;
		try {

			con = ConnectionManager.getConnection();

			query = "select distinct amd_permit_no FROM public.nt_m_amendments "
					+ "WHERE amd_permit_no != '' and amd_trn_type=?  and amd_permit_no is not null order by amd_permit_no;";

			ps = con.prepareStatement(query);
			ps.setString(1, dtos.getTranCode());

			rs = ps.executeQuery();

			AmendmentDTO dto;

			while (rs.next()) {
				dto = new AmendmentDTO();
				dto.setPermitNo(rs.getString("amd_permit_no"));
				data.add(dto);

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
	public boolean CheckAmendmentsTableCommitteeStatus(AmendmentDTO selectDTO, String status) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isUpdate = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "select * from public.nt_m_amendments where amd_application_no=? and  amd_commity_approved= ? ;";

			ps = con.prepareStatement(query);

			ps.setString(1, selectDTO.getApplicationNo());

			if (status != null) {

				ps.setString(2, status);

			} else {
				ps.setString(2, null);
			}

			rs = ps.executeQuery();

			if (rs.next()) {
				isUpdate = true;
			} else {
				isUpdate = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isUpdate;
	}

	@Override
	public boolean CheckAmendmentsTableBoardStatus(AmendmentDTO selectDTO, String status) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isUpdate = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "select * from public.nt_m_amendments where amd_application_no=? and  amd_board_approved= ? ;";

			ps = con.prepareStatement(query);

			ps.setString(1, selectDTO.getApplicationNo());

			if (status != null) {

				ps.setString(2, status);

			} else {
				ps.setString(2, null);
			}

			rs = ps.executeQuery();

			if (rs.next()) {
				isUpdate = true;
			} else {
				isUpdate = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isUpdate;
	}

	@Override
	public List<AmendmentDTO> getGrantApprovalDefaultDetailsForSelectedValue(AmendmentDTO amendmentDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<AmendmentDTO> data = new ArrayList<AmendmentDTO>();

		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct amd_pta,amd_time_approval,amd_permit_no,amd_commity_approved,amd_board_approved, amd_status, amd_application_no, amd_new_busno, amd_existing_busno, "
					+ "nt_r_transaction_type.description, nt_r_transaction_type.code, nt_t_pm_vehi_owner.pmo_full_name, amd_timeapproval_service_change "
					+ "FROM public.nt_m_amendments "
					+ "inner join public.nt_r_transaction_type on nt_r_transaction_type.code = nt_m_amendments.amd_trn_type "
					+ "left outer join public.nt_t_pm_vehi_owner on nt_t_pm_vehi_owner.pmo_application_no = nt_m_amendments.amd_application_no "
					+ "where amd_application_no=? ";

			ps = con.prepareStatement(query);
			ps.setString(1, amendmentDTO.getApplicationNo());
			rs = ps.executeQuery();

			AmendmentDTO dto;

			while (rs.next()) {
				dto = new AmendmentDTO();
				dto.setPermitNo(rs.getString("amd_permit_no"));
				dto.setApplicationNo(rs.getString("amd_application_no"));
				dto.setServiceType(null);
				dto.setTranCode(rs.getString("code"));
				dto.setTransactionType(rs.getString("description"));
				dto.setOwnerName(rs.getString("pmo_full_name"));
				dto.setExisitingBusNo(rs.getString("amd_existing_busno"));
				dto.setNewBusNo(rs.getString("amd_new_busno"));

				String approveStatusCode = rs.getString("amd_status");
				String committeeStatusCode = rs.getString("amd_commity_approved");
				String boardStatusCode = rs.getString("amd_board_approved");
				String ptaStatus = rs.getString("amd_pta");
				String timeApproval = rs.getString("amd_time_approval");
				String approveStatus = "";

				if (!approveStatusCode.equals("R")) {

					if (rs.getString("code").equals("13")) {

						if (approveStatusCode.equals("P")) {
							approveStatus = "PENDING";
						} else if (approveStatusCode.equals("DG")) {
							approveStatus = "DG / CHAIRMAN APPROVED";
						} else if (approveStatusCode.equals("FA")) {
							approveStatus = "APPROVED FOR PAYMENT";
						} else if (approveStatusCode.equals("AD")) {
							approveStatus = "AD / DIRECTOR APPROVED";
						} else if (approveStatusCode.equals("SA")) {
							approveStatus = "APPROVED FOR PRINTING";
						} else if (approveStatusCode.equals("C")) {
							approveStatus = "COMPLETE";
						} else {
							approveStatus = "N/A";
						}

					} else {
						if (committeeStatusCode == null && boardStatusCode == null && approveStatusCode.equals("P")) {
							approveStatus = "PENDING";
						} else if (committeeStatusCode.equals("A") && boardStatusCode == null
								&& approveStatusCode.equals("P") && ptaStatus == null && timeApproval == null) {
							approveStatus = "COMMITTEE APPROVED";
						} else if (committeeStatusCode.equals("A") && boardStatusCode == null
								&& approveStatusCode.equals("P") && ptaStatus.equals("P") && timeApproval == null) {
							approveStatus = "PTA REQUESTED";
						} else if (committeeStatusCode.equals("A") && boardStatusCode == null
								&& approveStatusCode.equals("P") && ptaStatus.equals("S") && timeApproval == null) {
							approveStatus = "PTA SKIPED";
						} else if (committeeStatusCode.equals("A") && boardStatusCode == null
								&& approveStatusCode.equals("P") && ptaStatus.equals("R") && timeApproval == null) {
							approveStatus = "PTA REJECTED";
						} else if (committeeStatusCode.equals("A") && boardStatusCode == null
								&& approveStatusCode.equals("P") && ptaStatus.equals("A") && timeApproval == null) {
							approveStatus = "PTA APPROVED";
						} else if (committeeStatusCode.equals("A") && boardStatusCode == null
								&& approveStatusCode.equals("P") && timeApproval.equals("R")) {
							approveStatus = "TIME APPROVAL REQUESTED";
						} else if (committeeStatusCode.equals("A") && boardStatusCode == null
								&& approveStatusCode.equals("P") && timeApproval.equals("A")) {
							approveStatus = "TIME APPROVAL APPROVED";
						} else if (committeeStatusCode.equals("A") && boardStatusCode.equals("A")
								&& approveStatusCode.equals("P")) {
							approveStatus = "BOARD APPROVED";
						} else if (committeeStatusCode.equals("A") && boardStatusCode.equals("A")
								&& approveStatusCode.equals("GR")) {
							approveStatus = "RESPONSE LETTER GENERATED";
						} else if (committeeStatusCode.equals("A") && boardStatusCode.equals("A")
								&& approveStatusCode.equals("DG")) {
							approveStatus = "DG / CHAIRMAN APPROVED";
						} else if (committeeStatusCode.equals("A") && boardStatusCode.equals("A")
								&& approveStatusCode.equals("FA")) {
							approveStatus = "APPROVED FOR PAYMENT";
						} else if (committeeStatusCode.equals("A") && boardStatusCode.equals("A")
								&& approveStatusCode.equals("AD")) {
							approveStatus = "AD / DIRECTOR APPROVED";
						} else if (committeeStatusCode.equals("A") && boardStatusCode.equals("A")
								&& approveStatusCode.equals("SA")) {
							approveStatus = "APPROVED FOR PRINTING";
						} else if (committeeStatusCode.equals("P") && boardStatusCode == null
								&& approveStatusCode.equals("P")) {
							approveStatus = "COMMITTEE APPROVAL ONGOING";
						} else if (committeeStatusCode.equals("A") && boardStatusCode.equals("P")
								&& approveStatusCode.equals("P")) {
							approveStatus = "BOARD APPROVAL ONGOING";
						} else if (committeeStatusCode.equals("A") && boardStatusCode.equals("A")
								&& approveStatusCode.equals("C")) {
							approveStatus = "COMPLETE";
						} else if (committeeStatusCode.equals("R") && boardStatusCode == null
								&& approveStatusCode.equals("P") && ptaStatus == null && timeApproval == null) {
							approveStatus = "REJECT BY COMMITTE";
						} else if (committeeStatusCode.equals("A") && boardStatusCode.equals("R")
								&& approveStatusCode.equals("P") && ptaStatus == null && timeApproval == null) {
							approveStatus = "REJECT BY BOARD";
							/* Higher Authorization */
						} else if (approveStatusCode.equals("GR") && checkTaskDetails(dto, "PM101", "C")) {
							approveStatus = "PENDING";
						} else if (approveStatusCode.equals("GR") && checkTaskDetails(dto, "AM106", "C")) {
							approveStatus = "BALANCE TASK COMPLETED";
						} else if (approveStatusCode.equals("DG")) {
							approveStatus = "DG / CHAIRMAN APPROVED";
						} else if (approveStatusCode.equals("FA")) {
							approveStatus = "APPROVED FOR PAYMENT";
						} else if (approveStatusCode.equals("AD")) {
							approveStatus = "AD / DIRECTOR APPROVED";
						} else if (approveStatusCode.equals("SA")) {
							approveStatus = "APPROVED FOR PRINTING";
						} else {
							approveStatus = "N/A";
						}
					}

				} else {
					approveStatus = "REJECTED";
				}

				dto.setStatus(approveStatus);
				dto.setTimeApprovalForServiceChange(rs.getString("amd_timeapproval_service_change"));
				data.add(dto);

			}

		} catch (

		Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return data;
	}

	public String getOldApplicationNo(AmendmentDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String oldApplicationNo = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT pm_old_application_no FROM public.nt_t_pm_application\r\n"
					+ "where pm_application_no='" + dto.getApplicationNo() + "'";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				oldApplicationNo = rs.getString("pm_old_application_no");
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return oldApplicationNo;

	}

	public String getOldPermitNo(String oldApplicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String oldPermit = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT pm_permit_no FROM public.nt_t_pm_application\r\n" + "where pm_application_no='"
					+ oldApplicationNo + "'";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {

				oldPermit = rs.getString("pm_permit_no");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return oldPermit;

	}

	public List<AmendmentDTO> getFilePaths(String oldPermit) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<AmendmentDTO> data = new ArrayList<AmendmentDTO>();

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT apd_file_path,apd_transaction_type,apd_doc_code,apd_document_des,apd_doc_type,apd_application_no FROM public.nt_t_application_document where apd_permit_no='"
					+ oldPermit + "'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				AmendmentDTO amendmentDTO2 = new AmendmentDTO();
				amendmentDTO2.setFilePath(rs.getString("apd_file_path"));

				amendmentDTO2.setDocApplicationNo(rs.getString("apd_application_no"));
				amendmentDTO2.setDocType(rs.getString("apd_doc_type"));
				amendmentDTO2.setDocCode(rs.getString("apd_doc_code"));
				amendmentDTO2.setDocDes(rs.getString("apd_document_des"));
				amendmentDTO2.setDocumentTransactionType(rs.getString("apd_transaction_type"));

				data.add(amendmentDTO2);

			}

		} catch (

		Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return data;
	}

	@Override
	public boolean checkPTAStatus(AmendmentDTO dto, String status) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isFound = false;
		try {

			con = ConnectionManager.getConnection();

			if (status != null) {

				String query = "select * from nt_m_amendments where amd_application_no=? and amd_pta=?";

				ps = con.prepareStatement(query);
				ps.setString(1, dto.getApplicationNo());
				ps.setString(2, status);

			} else {
				String query = "select * from nt_m_amendments where amd_application_no=? and amd_pta is null";
				ps = con.prepareStatement(query);
				ps.setString(1, dto.getApplicationNo());
			}

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
	public boolean checkTimeApprovalStatus(AmendmentDTO dto, String status) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isFound = false;
		try {

			con = ConnectionManager.getConnection();

			if (status != null) {

				String query = "select * from nt_m_amendments where amd_application_no=? and amd_time_approval=?";

				ps = con.prepareStatement(query);
				ps.setString(1, dto.getApplicationNo());
				ps.setString(2, status);

			} else {
				String query = "select * from nt_m_amendments where amd_application_no=? and amd_time_approval is null";

				ps = con.prepareStatement(query);
				ps.setString(1, dto.getApplicationNo());
			}

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
	public boolean updatePTAStatus(AmendmentDTO dto, String status, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isupdateed = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_amendments SET  amd_pta=?,  amd_modified_by=? , amd_modified_date=? "
					+ "WHERE amd_application_no=? ; ";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, status);
			stmt.setString(2, user);
			stmt.setTimestamp(3, timestamp);
			stmt.setString(4, dto.getApplicationNo());

			int i = stmt.executeUpdate();

			if (i > 0) {
				isupdateed = true;
			} else {
				isupdateed = false;
			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return isupdateed;
	}

	@Override
	public boolean updateTimeApprovalStatus(AmendmentDTO dto, String status, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isupdateed = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_amendments SET amd_time_approval=?, amd_modified_by=? , amd_modified_date=? "
					+ "WHERE amd_application_no=? ";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, status);
			stmt.setString(2, user);
			stmt.setTimestamp(3, timestamp);
			stmt.setString(4, dto.getApplicationNo());

			int i = stmt.executeUpdate();

			if (i > 0) {
				isupdateed = true;
			} else {
				isupdateed = false;
			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return isupdateed;
	}

	@Override
	public boolean checkTempRouteStatus(AmendmentDTO dto, String status) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isFound = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT trr_application_no, trr_status "
					+ "FROM public.nt_temp_route_request where trr_application_no=? and trr_status=? ";

			ps = con.prepareStatement(query);
			ps.setString(1, dto.getApplicationNo());
			ps.setString(2, status);

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
	public boolean updateTempRouteStatus(AmendmentDTO dto, String status, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isupdateed = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_temp_route_request SET  trr_status=? , modified_by=?, modified_date=? "
					+ "WHERE trr_application_no=? ";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, status);
			stmt.setString(2, user);
			stmt.setTimestamp(3, timestamp);
			stmt.setString(4, dto.getApplicationNo());

			int i = stmt.executeUpdate();

			if (i > 0) {
				isupdateed = true;
			} else {
				isupdateed = false;
			}

			con.commit();

		} catch (Exception e) {

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return isupdateed;
	}

	public List<AmendmentDTO> getVersionFilePaths(String oldPermit) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<AmendmentDTO> data = new ArrayList<AmendmentDTO>();

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT apd_file_path,apd_transaction_type,apd_doc_code,apd_document_des,apd_doc_type,apd_application_no,apd_versionno FROM public.nt_t_application_document_version where apd_permit_no='"
					+ oldPermit + "'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				AmendmentDTO amendmentDTO2 = new AmendmentDTO();
				amendmentDTO2.setFilePath(rs.getString("apd_file_path"));
				amendmentDTO2.setDocApplicationNo(rs.getString("apd_application_no"));
				amendmentDTO2.setDocType(rs.getString("apd_doc_type"));
				amendmentDTO2.setDocCode(rs.getString("apd_doc_code"));
				amendmentDTO2.setDocDes(rs.getString("apd_document_des"));
				amendmentDTO2.setDocumentTransactionType(rs.getString("apd_transaction_type"));
				amendmentDTO2.setDocVersionNo(rs.getString("apd_versionno"));

				data.add(amendmentDTO2);

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

	public void updateNewPermitDocuments(AmendmentDTO dto, String permitNo, String user, String filepath) {
		java.util.Date date = new java.util.Date();

		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		con = ConnectionManager.getConnection();
		long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_application_document");

		try {

			String sql = "INSERT INTO public.nt_t_application_document\r\n"
					+ "(seqno, apd_transaction_type, apd_application_no, apd_permit_no, apd_doc_code, apd_document_des, apd_file_path, apd_doc_type, apd_created_by, apd_created_date)\r\n"
					+ "VALUES(" + seqNo + ", '" + dto.getDocumentTransactionType() + "', '" + dto.getDocApplicationNo()
					+ "', '" + permitNo + "', '" + dto.getDocCode() + "', '" + dto.getDocDes() + "', '" + filepath
					+ "', '" + dto.getDocType() + "', '" + user + "', '" + timestamp + "');\r\n" + "";

			stmt = con.prepareStatement(sql);

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

	public void updateNewPermitDocumentsVersion(AmendmentDTO dto, String permitNo, String user, String filepath) {
		java.util.Date date = new java.util.Date();

		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		con = ConnectionManager.getConnection();
		long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_application_document_version");

		try {

			String sql = "INSERT INTO public.nt_t_application_document_version\r\n"
					+ "(seqno, apd_transaction_type, apd_application_no, apd_permit_no, apd_doc_code, apd_document_des, apd_file_path, apd_doc_type, apd_created_by, apd_created_date,apd_versionno)\r\n"
					+ "VALUES(" + seqNo + ", '" + dto.getDocumentTransactionType() + "', '" + dto.getDocApplicationNo()
					+ "', '" + permitNo + "', '" + dto.getDocCode() + "', '" + dto.getDocDes() + "', '" + filepath
					+ "', '" + dto.getDocType() + "', '" + user + "', '" + timestamp + "','" + dto.getDocVersionNo()
					+ "');\r\n" + "";

			stmt = con.prepareStatement(sql);

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
	public void saveNewRouteRequest(AmendmentServiceDTO tempNewRouteDTO) {
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_temp_route_request");

			String sql = "INSERT INTO public.nt_temp_route_request "
					+ "(seqno,trr_application_no,trr_origin,trr_destination,trr_via,trr_status,created_by,created_date)"
					+ " values (?,?,?,?,?,?,?,?)";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, tempNewRouteDTO.getApplicationNo());
			stmt.setString(3, tempNewRouteDTO.getOrigin());
			stmt.setString(4, tempNewRouteDTO.getDestination());
			stmt.setString(5, tempNewRouteDTO.getVia());
			stmt.setString(6, "P");
			stmt.setString(7, tempNewRouteDTO.getCreatedBy());
			stmt.setTimestamp(8, timestamp);

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
	public boolean updateNewRouteRequest(AmendmentServiceDTO tempNewRouteDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		boolean updated = false;

		try {

			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_temp_route_request SET trr_origin=? ,trr_destination=? ,trr_via=? , trr_status=?, modified_by=?, modified_date=? "
					+ "WHERE trr_application_no=?";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, tempNewRouteDTO.getOrigin());
			stmt.setString(2, tempNewRouteDTO.getDestination());
			stmt.setString(3, tempNewRouteDTO.getVia());
			stmt.setString(4, "P");
			stmt.setString(5, tempNewRouteDTO.getModifiedBy());
			stmt.setTimestamp(6, timestamp);
			stmt.setString(7, tempNewRouteDTO.getApplicationNo());

			int c = stmt.executeUpdate();

			if (c > 0) {
				updated = true;
			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
		return updated;
	}

	@Override
	public AmendmentServiceDTO getNewRouteRequest(String generatedApplicationNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		AmendmentServiceDTO amendmentServiceDTO = new AmendmentServiceDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM public.nt_temp_route_request WHERE trr_application_no = ?";

			ps = con.prepareStatement(query);
			ps.setString(1, generatedApplicationNo);

			rs = ps.executeQuery();

			while (rs.next()) {

				amendmentServiceDTO.setSeq(rs.getLong("seqno"));
				amendmentServiceDTO.setApplicationNo(rs.getString("trr_application_no"));
				amendmentServiceDTO.setOrigin(rs.getString("trr_origin"));
				amendmentServiceDTO.setDestination(rs.getString("trr_destination"));
				amendmentServiceDTO.setVia(rs.getString("trr_via"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return amendmentServiceDTO;

	}

	@Override
	public boolean checkFourChanges(AmendmentDTO dto, String transActionOne, String transActionTwo,
			String transActionThree, String transActionFour) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isUpdate = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "select * from public.nt_m_amendments "
					+ "where (amd_existing_busno=? and amd_status='P' and (amd_trn_type=? or amd_trn_type=? or amd_trn_type=? or amd_trn_type=?))";

			ps = con.prepareStatement(query);
			ps.setString(1, dto.getExisitingBusNo());
			ps.setString(2, transActionOne);
			ps.setString(3, transActionTwo);
			ps.setString(4, transActionThree);
			ps.setString(5, transActionFour);

			rs = ps.executeQuery();

			if (rs.next()) {
				isUpdate = true;
			} else {
				isUpdate = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isUpdate;
	}

	@Override
	public boolean updateVehicelOwnerTableOwnerChange(AmendmentDTO dto, String newPermitNo, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isupdateed = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_pm_vehi_owner "
					+ "SET pmo_permit_no=? , pm_modified_by=?, pm_modified_date=? WHERE pmo_application_no=? ";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, newPermitNo);
			stmt.setString(2, user);
			stmt.setTimestamp(3, timestamp);
			stmt.setString(4, dto.getApplicationNo());

			int i = stmt.executeUpdate();

			if (i > 0) {
				isupdateed = true;
			} else {
				isupdateed = false;
			}

			con.commit();

		} catch (Exception e) {

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return isupdateed;
	}

	@Override
	public boolean updateOminiBusTableOwnerChange(AmendmentDTO dto, String newPermitNo, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isupdateed = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_pm_omini_bus1 SET  pmb_permit_no=? , pmb_modified_by=?, pmb_modified_date=? "
					+ "WHERE pmb_application_no=? ";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, newPermitNo);
			stmt.setString(2, user);
			stmt.setTimestamp(3, timestamp);
			stmt.setString(4, dto.getApplicationNo());

			int i = stmt.executeUpdate();

			if (i > 0) {
				isupdateed = true;
			} else {
				isupdateed = false;
			}

			con.commit();

		} catch (Exception e) {

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return isupdateed;
	}

	@Override
	public AmendmentBusOwnerDTO getOldOriginDestination(String permitNo) {

		Connection con = null;
		PreparedStatement ps = null, ps1 = null;
		ResultSet rs = null, rs1 = null;
		String OldAppNo = null;
		AmendmentBusOwnerDTO amendmentOldRoutesDTO = new AmendmentBusOwnerDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "select pm_old_application_no  from public.nt_t_pm_application\r\n"
					+ "where pm_permit_no=? order by pm_created_date desc limit 1";

			ps = con.prepareStatement(query);
			ps.setString(1, permitNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				OldAppNo = rs.getString("pm_old_application_no");

			}

			String query1 = "select a.pm_route_no,b.rou_service_origine_sin as rou_service_origine_sin ,b.rou_service_destination_sin as rou_service_destination_sin \r\n"
					+ "from  public.nt_t_pm_application a\r\n"
					+ "inner join public.nt_r_route b on b.rou_number=a.pm_route_no\r\n" + "where pm_application_no= ?";

			ps1 = con.prepareStatement(query1);
			ps1.setString(1, OldAppNo);

			rs1 = ps1.executeQuery();
			while (rs1.next()) {
				amendmentOldRoutesDTO.setOldOrigin(rs1.getString("rou_service_origine_sin"));
				amendmentOldRoutesDTO.setOldDestination(rs1.getString("rou_service_destination_sin"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(rs1);
			ConnectionManager.close(ps1);
			ConnectionManager.close(con);
		}

		return amendmentOldRoutesDTO;

	}

	@Override
	public void updateOldPermitNoInAppTb(String newApplicationNo, String oldPermitNo, String permitNo) {
		Connection con = null;
		PreparedStatement ps3 = null;
		try {

			con = ConnectionManager.getConnection();

			String q3 = "UPDATE public.nt_t_pm_application SET  pm_old_permit_no=? WHERE pm_application_no=? and pm_permit_no=?;";

			ps3 = con.prepareStatement(q3);
			ps3.setString(1, oldPermitNo);
			ps3.setString(2, newApplicationNo);
			ps3.setString(3, permitNo);
			ps3.executeUpdate();

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps3);
			ConnectionManager.close(con);
		}

	}

	@Override
	public List<CommonDTO> getRoutesToDropdown() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();
			// Only need active route -- change
			String query = "SELECT rou_number,rou_description " + " FROM public.nt_r_route " + " WHERE active in('A') "
					+ " ORDER BY rou_number";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("rou_number"));
				commonDTO.setDescription(rs.getString("rou_description"));

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
	public boolean isPrintPermit(String appno) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isPm400Compl = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "select tsd_status from public.nt_t_task_det\r\n" + "where tsd_app_no=? and tsd_task_code=?";

			ps = con.prepareStatement(query);
			ps.setString(1, appno);
			ps.setString(2, "PM400");

			rs = ps.executeQuery();

			if (rs.next()) {
				isPm400Compl = true;
			} else {
				isPm400Compl = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isPm400Compl;
	}

	@Override
	public AmendmentServiceDTO getNewRouteRequestEndChange(String generatedApplicationNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		AmendmentServiceDTO amendmentServiceDTO = new AmendmentServiceDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "select pm_route_flag,pm_route_no,a.rou_service_origine,a.rou_service_destination,a.rou_via,a.rou_distance from public.nt_t_pm_application \r\n"
					+ "inner join public.nt_r_route a on a.rou_number=public.nt_t_pm_application.pm_route_no\r\n"
					+ "where pm_application_no=?\r\n" + "and a.active='A'";

			ps = con.prepareStatement(query);
			ps.setString(1, generatedApplicationNo);

			rs = ps.executeQuery();

			while (rs.next()) {

				amendmentServiceDTO.setRouteFlag(rs.getString("pm_route_flag"));
				if (amendmentServiceDTO.getRouteFlag().equalsIgnoreCase("Y")) {
					amendmentServiceDTO.setOrigin(rs.getString("rou_service_destination"));
					amendmentServiceDTO.setDestination(rs.getString("rou_service_origine"));
				} else if (amendmentServiceDTO.getRouteFlag().equalsIgnoreCase("N")) {
					amendmentServiceDTO.setOrigin(rs.getString("rou_service_origine"));
					amendmentServiceDTO.setDestination(rs.getString("rou_service_destination"));
				}

				amendmentServiceDTO.setVia(rs.getString("rou_via"));
				amendmentServiceDTO.setRouteNo(rs.getString("pm_route_no"));
				amendmentServiceDTO.setDistance(rs.getBigDecimal("rou_distance"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return amendmentServiceDTO;

	}

	@Override
	public AmendmentBusOwnerDTO getApplicationDetailsByVehicleNoAndAppNo(String strVehicleNo, String appNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		AmendmentBusOwnerDTO amendmentDTO = new AmendmentBusOwnerDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct C.pmo_is_backlog_app,A.pm_permit_no,A.pm_application_no,B.description,A.pm_vehicle_regno,A.pm_old_application_no,\r\n"
					+ "A.pm_is_tender_permit,A.pm_permit_expire_date,A.pm_special_remark,C.pmo_full_name,C.pmo_telephone_no,\r\n"
					+ "C.pmo_address1,C.pmo_mobile_no,C.pmo_address2,D.description AS pmo_province,pmo_city,\r\n"
					+ "F.description AS pmo_district,C.pmo_nic,G.ds_description AS pmo_div_sec,A.pm_route_no,A.pm_route_flag,R.rou_service_origine,R.rou_service_destination,R.rou_via,A.pm_route_no,R.rou_distance\r\n"
					+ "FROM public.nt_t_pm_application A \r\n"
					+ "LEFT JOIN nt_r_service_types B ON A.pm_service_type=B.code LEFT JOIN nt_t_pm_vehi_owner C\r\n"
					+ "ON A.pm_application_no  =C.pmo_application_no  LEFT JOIN nt_r_province D \r\n"
					+ "ON C.pmo_province=D.code FULL OUTER JOIN nt_r_district F\r\n"
					+ "ON C.pmo_district=F.code FULL OUTER JOIN nt_r_div_sec G \r\n"
					+ "ON C.pmo_div_sec=G.ds_code left outer join public.nt_r_route R on A.pm_route_no= R.rou_number\r\n"
					+ "WHERE pm_status='A' and  pm_vehicle_regno = ?\r\n";
					//+ "and A.pm_application_no =?";// added by tharushi.e
			 /**commented due to new edit function 07/01/2022**/
			ps = con.prepareStatement(query);
			ps.setString(1, strVehicleNo);
			//ps.setString(2, appNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				boolean isTenderPermit = false;

				amendmentDTO.setPermitNo(rs.getString("pm_permit_no"));
				amendmentDTO.setApplicationNo(rs.getString("pm_application_no"));
				amendmentDTO.setServiceType(rs.getString("description"));
				amendmentDTO.setBusRegNo(rs.getString("pm_vehicle_regno"));

				if (!(rs.getString("pm_is_tender_permit") == null) && !rs.getString("pm_is_tender_permit").isEmpty()
						&& rs.getString("pm_is_tender_permit").equalsIgnoreCase("Y")) {
					isTenderPermit = true;
				} else {
					isTenderPermit = false;
				}
				amendmentDTO.setTenderPermit(isTenderPermit);
				amendmentDTO.setExpireDate(rs.getString("pm_permit_expire_date"));
				amendmentDTO.setFinalRemark(rs.getString("pm_special_remark"));
				amendmentDTO.setFullName(rs.getString("pmo_full_name"));
				amendmentDTO.setTelephoneNo(rs.getString("pmo_telephone_no"));
				amendmentDTO.setAddress1(rs.getString("pmo_address1"));
				amendmentDTO.setMobileNo(rs.getString("pmo_mobile_no"));
				amendmentDTO.setAddress2(rs.getString("pmo_address2"));
				amendmentDTO.setProvince(rs.getString("pmo_province"));
				amendmentDTO.setCity(rs.getString("pmo_city"));
				amendmentDTO.setDistrict(rs.getString("pmo_district"));
				amendmentDTO.setNicNo(rs.getString("pmo_nic"));
				amendmentDTO.setDivSec(rs.getString("pmo_div_sec"));
				amendmentDTO.setIsBacklogApp(rs.getString("pmo_is_backlog_app"));amendmentDTO.setVia(rs.getString("rou_via"));
				amendmentDTO.setRouteFlag(rs.getString("pm_route_flag"));
				amendmentDTO.setOrigin(rs.getString("rou_service_origine"));
				amendmentDTO.setDestination(rs.getString("rou_service_destination"));
			
				amendmentDTO.setRouteNo(rs.getString("pm_route_no"));
				amendmentDTO.setDistance(rs.getBigDecimal("rou_distance"));
				amendmentDTO.setOldAppNo(rs.getString("pm_old_application_no"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return amendmentDTO;

	}

	@Override
	public boolean checkRouteStatus(String applicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps0 = null;
		ResultSet rs = null;
		ResultSet rs0 = null;
		boolean isTemp = false;
		String routeNo = null;
		try {

			con = ConnectionManager.getConnection();

			String queryN = "select pm_route_no from nt_t_pm_application where pm_application_no =? ";
			ps0 = con.prepareStatement(queryN);
			ps0.setString(1, applicationNo);
			rs0 = ps0.executeQuery();

			while (rs0.next()) {
				routeNo = (rs0.getString("pm_route_no"));
			}

			if (routeNo != null && !routeNo.trim().isEmpty()) {
				String query = "select rou_number  from nt_r_route where active ='T' and rou_number= ? ";

				ps = con.prepareStatement(query);
				ps.setString(1, routeNo);
				rs = ps.executeQuery();

				if (rs.next() == true) {
					isTemp = true;
				} else {
					isTemp = false;
				}
			} else
				isTemp = true;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs0);
			ConnectionManager.close(ps0);
			ConnectionManager.close(con);
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isTemp;
	}

	@Override
	public AmendmentDTO getRouteNO(String appNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		AmendmentDTO route = new AmendmentDTO();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct pm_route_no ,R.rou_service_origine,R.rou_service_destination,R.rou_via FROM public.nt_t_pm_application \r\n"
					+ "inner join public.nt_r_route R on R.rou_number= public.nt_t_pm_application.pm_route_no\r\n"
					+ "where pm_application_no= (select pm_old_application_no from  public.nt_t_pm_application where pm_application_no=?)\r\n"
					+ "";
			ps = con.prepareStatement(query);

			ps.setString(1, appNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				route = new AmendmentDTO();
				route.setRouteNo(rs.getString("pm_route_no"));
				route.setOrigin(rs.getString("rou_service_origine"));
				route.setDestination(rs.getString("rou_service_destination"));
				route.setVia(rs.getString("rou_via"));

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return route;

	}

	@Override
	public OminiBusDTO insuranceDetByBusRegNoNew(String strVehicleNo, String strApplicationNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		OminiBusDTO ominiBusDTO = new OminiBusDTO();
		try {
			con = ConnectionManager.getConnection();
			String query =

					"SELECT seq, pmb_vehicle_regno," + "pmb_seating_capacity, pmb_no_of_doors, pmb_height,"
							+ "pmb_production_date, pmb_weight, pmb_serial_no, pmb_certificate_date, pmb_garage_reg_no,"
							+ "pmb_insurance_company, pmb_insurance_cat, pmb_insurance_expire_date, pmb_bus_price,"
							+ "pmb_is_loan_obtain, pmb_finance_company, pmb_bank_loan_amt, pmb_due_amt, pmb_date_obtain,"
							+ "pmb_lapsed_installment, pmb_revenue_license_exp_date, pmb_first_reg_date,"
							+ "pmb_permit_no,pmb_garage_name,pmb_policy_no,pmb_emission_test_exp_date,pmb_serial_no, pmb_garage_reg_no,pmb_garage_name,pmb_certificate_date "
							+ " FROM public.nt_t_pm_omini_bus1"
							+ " WHERE pmb_vehicle_regno =  ? and pmb_application_no = ?";

			ps = con.prepareStatement(query);
			ps.setString(1, strVehicleNo);
			ps.setString(2, strApplicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				ominiBusDTO.setSeq(rs.getLong("seq"));

				ominiBusDTO.setVehicleRegNo(rs.getString("pmb_vehicle_regno"));
				ominiBusDTO.setSeating(rs.getString("pmb_seating_capacity"));
				ominiBusDTO.setNoofDoors(rs.getString("pmb_no_of_doors"));
				ominiBusDTO.setHeight(rs.getString("pmb_height"));
				ominiBusDTO.setManufactureDate(rs.getString("pmb_production_date"));
				ominiBusDTO.setWeight(rs.getString("pmb_weight"));
				ominiBusDTO.setSerialNo(rs.getString("pmb_serial_no"));

				String pmb_certificate_dateString = rs.getString("pmb_certificate_date");
				if (pmb_certificate_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setFitnessCertiDate = originalFormat.parse(pmb_certificate_dateString);
					ominiBusDTO.setFitnessCertiDate(setFitnessCertiDate);
				}

				ominiBusDTO.setGarageRegNo(rs.getString("pmb_garage_reg_no"));
				ominiBusDTO.setGarageName(rs.getString("pmb_garage_name"));
				ominiBusDTO.setInsuCompName(rs.getString("pmb_insurance_company"));
				ominiBusDTO.setInsuCat(rs.getString("pmb_insurance_cat"));

				String pmb_insurance_expire_dateString = rs.getString("pmb_insurance_expire_date");
				if (pmb_insurance_expire_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setInsuExpDate = originalFormat.parse(pmb_insurance_expire_dateString);
					ominiBusDTO.setInsuExpDate(setInsuExpDate);
				}

				ominiBusDTO.setPriceValOfBus(rs.getBigDecimal("pmb_bus_price"));
				ominiBusDTO.setIsLoanObtained(rs.getString("pmb_is_loan_obtain"));
				ominiBusDTO.setFinanceCompany(rs.getString("pmb_finance_company"));
				ominiBusDTO.setBankLoan(rs.getBigDecimal("pmb_bank_loan_amt"));
				ominiBusDTO.setDueAmount(rs.getBigDecimal("pmb_due_amt"));

				String pmb_date_obtainString = rs.getString("pmb_date_obtain");
				if (pmb_date_obtainString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setDateObtained = originalFormat.parse(pmb_date_obtainString);
					ominiBusDTO.setDateObtained(setDateObtained);
				}

				ominiBusDTO.setLapsedInstall(rs.getString("pmb_lapsed_installment"));

				String pmb_revenue_license_exp_dateString = rs.getString("pmb_revenue_license_exp_date");
				if (pmb_revenue_license_exp_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setExpiryDateRevLic = originalFormat.parse(pmb_revenue_license_exp_dateString);
					ominiBusDTO.setExpiryDateRevLic(setExpiryDateRevLic);
					ominiBusDTO.setExpiryDateRevLicNew(setExpiryDateRevLic);
				}

				String pmb_first_reg_dateString = rs.getString("pmb_first_reg_date");
				if (pmb_first_reg_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setDateOfFirstReg = originalFormat.parse(pmb_first_reg_dateString);
					ominiBusDTO.setDateOfFirstReg(setDateOfFirstReg);
				}

				ominiBusDTO.setPermitNo(rs.getString("pmb_permit_no"));
				ominiBusDTO.setGarageName(rs.getString("pmb_garage_name"));
				ominiBusDTO.setPolicyNo(rs.getString("pmb_policy_no"));

				String pmb_emission_test_exp_dateString = rs.getString("pmb_emission_test_exp_date");
				if (pmb_emission_test_exp_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setExpiryDateEmissionTest = originalFormat.parse(pmb_emission_test_exp_dateString);
					ominiBusDTO.setEmissionExpDate(setExpiryDateEmissionTest);
					ominiBusDTO.setEmmissionTestExpireDate(setExpiryDateEmissionTest);
				}
				
				ominiBusDTO.setSerialNo(rs.getString("pmb_serial_no"));
				ominiBusDTO.setGarageRegNo(rs.getString("pmb_garage_reg_no"));
				ominiBusDTO.setGarageName(rs.getString("pmb_garage_name"));
				String fitDtateVal = rs.getString("pmb_certificate_date");
				String dateFormat = "dd/MM/yyyy";
				SimpleDateFormat frm = new SimpleDateFormat(dateFormat);
				if (fitDtateVal != null) {
				Date fitnessDateObj = frm.parse(rs.getString("pmb_certificate_date"));
				ominiBusDTO.setFitnessCertiDate(fitnessDateObj);
				} 
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return ominiBusDTO;

	}

	@Override
	public String getTransactionTypeFromAmendmendTable(String appNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String trnCode = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "select a.amd_trn_type from public.nt_m_amendments a  where a.amd_application_no =?";

			ps = con.prepareStatement(query);
			ps.setString(1, appNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				trnCode = (rs.getString("amd_trn_type"));

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return trnCode;
	}

	@Override
	public boolean updateApplicationTableRemarkOldRecordForOwnerChange(AmendmentDTO dto, String newPermitNo) {

		Connection con = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		PreparedStatement ps4 = null;
		PreparedStatement ps5 = null;
		ResultSet rs = null;
		ResultSet rs4 = null;
		boolean isUpdate = false;
		try {

			con = ConnectionManager.getConnection();

			String q2 = "select pm_old_application_no from public.nt_t_pm_application where pm_application_no=? ";

			ps2 = con.prepareStatement(q2);
			ps2.setString(1, dto.getApplicationNo());
			rs = ps2.executeQuery();

			if (rs.next() == true) {
				int i2 = 0;

				// get old permit number
				String q4 = "select pm_permit_no  from public.nt_t_pm_application where pm_application_no=? ";

				ps4 = con.prepareStatement(q4);
				ps4.setString(1, rs.getString("pm_old_application_no"));
				rs4 = ps4.executeQuery();
				// update new permit number in old application number
				String q3 = "update public.nt_t_pm_application set pm_special_remark =? where pm_application_no=?";

				ps3 = con.prepareStatement(q3);
				ps3.setString(1, newPermitNo);
				ps3.setString(2, rs.getString("pm_old_application_no"));
				int i = ps3.executeUpdate();

				if (rs4.next() == true) {
					// update old permit number in new application number
					String q5 = "update public.nt_t_pm_application set pm_special_remark =? where pm_application_no=?";

					ps5 = con.prepareStatement(q5);
					ps5.setString(1, rs4.getString("pm_permit_no"));
					ps5.setString(2, dto.getApplicationNo());
					i2 = ps5.executeUpdate();
				}
				if (i > 0 && i2 > 0) {

					isUpdate = true;

				} else {
					isUpdate = false;
				}
				con.commit();

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(rs4);
			ConnectionManager.close(ps2);
			ConnectionManager.close(ps3);
			ConnectionManager.close(ps4);
			ConnectionManager.close(ps5);
			ConnectionManager.close(con);
		}

		return isUpdate;

	}
	
	@Override
	public List<AmendmentDTO> getTransactionTypeForTimeApproval() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<AmendmentDTO> data = new ArrayList<AmendmentDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select DISTINCT amd_trn_type,  nt_r_transaction_type.description "
					+ "from public.nt_m_amendments "
					+ "inner join public.nt_r_transaction_type on nt_r_transaction_type.code=nt_m_amendments.amd_trn_type "
					+ "where  amd_trn_type not in ('15','14','13')";
					

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			AmendmentDTO dto;

			while (rs.next()) {
				dto = new AmendmentDTO();
				dto.setTranCode(rs.getString("amd_trn_type"));
				dto.setTransactionType(rs.getString("description"));
				data.add(dto);

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
	public String generateAmendmendApplicationNo() {
	
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

			String sql = "SELECT app_no FROM public.nt_r_number_generation where code='AAP' ;";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("app_no");
			}

			if (result != null) {

				lastyr = result.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String ApprecordcountN = "00001";
					strAppNo = "AAP" + currYear + ApprecordcountN;
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
					strAppNo = "AAP" + currYear + ApprecordcountN;
				}
			} else
				strAppNo = "AAP" + currYear + "00001";

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
	public void updateNumberGeneration(String generateApplicationNumber,String loggingUser, String type) {

		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		Connection con = null;
		try {
			con = ConnectionManager.getConnection();
			String sql = "UPDATE public.nt_r_number_generation "
					+ "SET  app_no=?, modified_by=?, modified_date=? WHERE code=?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, generateApplicationNumber);
			stmt.setString(2, loggingUser);
			stmt.setTimestamp(3, timestamp);
			stmt.setString(4, type);
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
	public AmendmentBusOwnerDTO getApplicationDetailsByExistingVehicleNo(String strVehicleNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		AmendmentBusOwnerDTO amendmentDTO = new AmendmentBusOwnerDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct C.pmo_is_backlog_app,A.pm_permit_no,A.pm_application_no,B.description,A.pm_vehicle_regno,A.pm_old_application_no,\r\n"
					+ "A.pm_is_tender_permit,A.pm_permit_expire_date,A.pm_special_remark,C.pmo_full_name,C.pmo_telephone_no,\r\n"
					+ "C.pmo_address1,C.pmo_mobile_no,C.pmo_address2,D.description AS pmo_province,pmo_city,\r\n"
					+ "F.description AS pmo_district,C.pmo_nic,G.ds_description AS pmo_div_sec,A.pm_route_no,A.pm_route_flag,R.rou_service_origine,R.rou_service_destination,R.rou_via,A.pm_route_no,R.rou_distance\r\n"
					+ "FROM public.nt_t_pm_application A \r\n"
					+ "LEFT JOIN nt_r_service_types B ON A.pm_service_type=B.code LEFT JOIN nt_t_pm_vehi_owner C\r\n"
					+ "ON A.pm_vehicle_regno=C.pmo_vehicle_regno and A.pm_application_no =C.pmo_application_no  LEFT JOIN nt_r_province D \r\n"
					+ "ON C.pmo_province=D.code FULL OUTER JOIN nt_r_district F\r\n"
					+ "ON C.pmo_district=F.code FULL OUTER JOIN nt_r_div_sec G \r\n"
					+ "ON C.pmo_div_sec=G.ds_code left outer join public.nt_r_route R on A.pm_route_no= R.rou_number\r\n"
					+ "WHERE  pm_vehicle_regno = ? and pm_status ='A'\r\n" ;
			ps = con.prepareStatement(query);
			ps.setString(1, strVehicleNo);
			

			rs = ps.executeQuery();

			while (rs.next()) {
				boolean isTenderPermit = false;

				amendmentDTO.setPermitNo(rs.getString("pm_permit_no"));
				amendmentDTO.setApplicationNo(rs.getString("pm_application_no"));
				amendmentDTO.setServiceType(rs.getString("description"));
				amendmentDTO.setBusRegNo(rs.getString("pm_vehicle_regno"));

				if (!(rs.getString("pm_is_tender_permit") == null) && !rs.getString("pm_is_tender_permit").isEmpty()
						&& rs.getString("pm_is_tender_permit").equalsIgnoreCase("Y")) {
					isTenderPermit = true;
				} else {
					isTenderPermit = false;
				}
				amendmentDTO.setTenderPermit(isTenderPermit);
				amendmentDTO.setExpireDate(rs.getString("pm_permit_expire_date"));
				amendmentDTO.setFinalRemark(rs.getString("pm_special_remark"));
				amendmentDTO.setFullName(rs.getString("pmo_full_name"));
				amendmentDTO.setTelephoneNo(rs.getString("pmo_telephone_no"));
				amendmentDTO.setAddress1(rs.getString("pmo_address1"));
				amendmentDTO.setMobileNo(rs.getString("pmo_mobile_no"));
				amendmentDTO.setAddress2(rs.getString("pmo_address2"));
				amendmentDTO.setProvince(rs.getString("pmo_province"));
				amendmentDTO.setCity(rs.getString("pmo_city"));
				amendmentDTO.setDistrict(rs.getString("pmo_district"));
				amendmentDTO.setNicNo(rs.getString("pmo_nic"));
				amendmentDTO.setDivSec(rs.getString("pmo_div_sec"));
				amendmentDTO.setIsBacklogApp(rs.getString("pmo_is_backlog_app"));
				amendmentDTO.setOrigin(rs.getString("rou_service_origine"));
				amendmentDTO.setDestination(rs.getString("rou_service_destination"));
				amendmentDTO.setVia(rs.getString("rou_via"));
				amendmentDTO.setRouteFlag(rs.getString("pm_route_flag"));
				amendmentDTO.setRouteNo(rs.getString("pm_route_no"));
				amendmentDTO.setDistance(rs.getBigDecimal("rou_distance"));
				amendmentDTO.setOldAppNo(rs.getString("pm_old_application_no"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return amendmentDTO;

	}

	@Override
	public boolean checkInspectionStatus(String appNo) {

		Connection con = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;
		boolean dataHave = false;
		try {

			con = ConnectionManager.getConnection();

			String q2 = "select distinct *\r\n" + 
					"from public.nt_t_task_det d\r\n" + 
					"left outer join public.nt_h_task_his h on h.tsd_app_no =d.tsd_app_no \r\n" + 
					"where d.tsd_app_no =?\r\n" + 
					"and ( ( d.tsd_task_code ='PM100' and d.tsd_status ='O')\r\n" + 
					"or  ( d.tsd_task_code ='PM100' and d.tsd_status ='C')\r\n" + 
					"or  ( h.tsd_task_code ='PM100' and h.tsd_status ='O')\r\n" + 
					"or  ( h.tsd_task_code ='PM100' and h.tsd_status ='C') ); ";

			ps2 = con.prepareStatement(q2);
			ps2.setString(1, appNo);
			rs = ps2.executeQuery();

			if (rs.next() == true) {
				dataHave = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps2);
			ConnectionManager.close(con);
		}

		return dataHave;

	}

	@Override
	public String getRouteFlag(String appNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String flag = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "select pm_route_flag from public.nt_t_pm_application where pm_application_no =?";

			ps = con.prepareStatement(query);
			ps.setString(1, appNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				flag = (rs.getString("pm_route_flag"));

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return flag;
	}

	@Override
	public boolean checkAM113Exists(String generatedApplicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean returnVal = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "select tsd_task_code from public.nt_h_task_his where tsd_app_no =? and tsd_task_code='AM113'; ";

			ps = con.prepareStatement(query);
			ps.setString(1, generatedApplicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				returnVal =true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return returnVal;
	}

	@Override
	public AmendmentOminiBusDTO ominiBusByVehicleNoOldData(String strVehicleNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		AmendmentOminiBusDTO amendmentDTO = new AmendmentOminiBusDTO();
		try {
			con = ConnectionManager.getConnection();
			
			
			
			String query =

					"SELECT B.pm_special_remark,B.pm_route_no,B.pm_route_flag,C.description AS pm_service_type_desc,B.pm_service_type as pm_service_type,pmb_application_no, pmb_vehicle_regno,"
							+ "pmb_seating_capacity, pmb_no_of_doors, pmb_height, pmb_policy_no, "
							+ "pmb_production_date, pmb_weight, pmb_serial_no, pmb_certificate_date, pmb_garage_reg_no,"
							+ "pmb_insurance_company, pmb_insurance_cat, pmb_insurance_expire_date, pmb_bus_price,"
							+ "pmb_is_loan_obtain, pmb_finance_company, pmb_bank_loan_amt, pmb_due_amt, pmb_date_obtain,"
							+ "pmb_lapsed_installment, pmb_revenue_license_exp_date, pmb_first_reg_date,"
							+ "pmb_permit_no,pmb_emission_test_exp_date"
							+ " FROM public.nt_h_pm_omini_bus1 A INNER JOIN nt_h_pm_application_new B ON A.pmb_application_no =B.pm_application_no  INNER JOIN nt_r_service_types C ON B.pm_service_type=C.code"
							+ " WHERE pmb_vehicle_regno =  ? order by pmb_modified_date desc limit 1";

			ps = con.prepareStatement(query);
			ps.setString(1, strVehicleNo);
			rs = ps.executeQuery();

			while (rs.next()) {

				amendmentDTO.setApplicationNo(rs.getString("pmb_application_no"));
				amendmentDTO.setVehicleRegNo(rs.getString("pmb_vehicle_regno"));
				amendmentDTO.setSeating(rs.getString("pmb_seating_capacity"));
				amendmentDTO.setNoofDoors(rs.getString("pmb_no_of_doors"));
				amendmentDTO.setHeight(rs.getString("pmb_height"));
				amendmentDTO.setManufactureDate(rs.getString("pmb_production_date"));
				amendmentDTO.setWeight(rs.getString("pmb_weight"));
				amendmentDTO.setSerialNo(rs.getString("pmb_serial_no"));

				String pmb_certificate_dateString = rs.getString("pmb_certificate_date");
				if (pmb_certificate_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setFitnessCertiDate = originalFormat.parse(pmb_certificate_dateString);
					amendmentDTO.setFitnessCertiDate(setFitnessCertiDate);
				}

				amendmentDTO.setGarageRegNo(rs.getString("pmb_garage_reg_no"));
				amendmentDTO.setGarageName(rs.getString("pmb_garage_reg_no"));
				amendmentDTO.setInsuCompName(rs.getString("pmb_insurance_company"));
				amendmentDTO.setInsuCat(rs.getString("pmb_insurance_cat"));
				amendmentDTO.setPolicyNo(rs.getString("pmb_policy_no"));

				String pmb_insurance_expire_dateString = rs.getString("pmb_insurance_expire_date");
				if (pmb_insurance_expire_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setInsuExpDate = originalFormat.parse(pmb_insurance_expire_dateString);
					amendmentDTO.setInsuExpDate(setInsuExpDate);
				}

				amendmentDTO.setPriceValOfBus(rs.getBigDecimal("pmb_bus_price"));
				amendmentDTO.setIsLoanObtained(rs.getString("pmb_is_loan_obtain"));
				amendmentDTO.setFinanceCompany(rs.getString("pmb_finance_company"));
				amendmentDTO.setBankLoan(rs.getBigDecimal("pmb_bank_loan_amt"));
				amendmentDTO.setDueAmount(rs.getBigDecimal("pmb_due_amt"));

				String pmb_date_obtainString = rs.getString("pmb_date_obtain");
				if (pmb_date_obtainString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setDateObtained = originalFormat.parse(pmb_date_obtainString);
					amendmentDTO.setDateObtained(setDateObtained);
				}

				amendmentDTO.setLapsedInstall(rs.getString("pmb_lapsed_installment"));

				String pmb_revenue_license_exp_dateString = rs.getString("pmb_revenue_license_exp_date");
				if (pmb_revenue_license_exp_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setExpiryDateRevLic = originalFormat.parse(pmb_revenue_license_exp_dateString);
					amendmentDTO.setExpiryDateRevLic(setExpiryDateRevLic);
				}

				String pmb_first_reg_dateString = rs.getString("pmb_first_reg_date");
				if (pmb_first_reg_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setDateOfFirstReg = originalFormat.parse(pmb_first_reg_dateString);
					amendmentDTO.setDateOfFirstReg(setDateOfFirstReg);
				}

				amendmentDTO.setPermitNo(rs.getString("pmb_permit_no"));
				amendmentDTO.setServiceType(rs.getString("pm_service_type"));
				amendmentDTO.setServiceTypeDesc(rs.getString("pm_service_type_desc"));
				amendmentDTO.setRouteNo(rs.getString("pm_route_no"));
				amendmentDTO.setRouteFlag(rs.getString("pm_route_flag"));
				amendmentDTO.setRemarks(rs.getString("pm_special_remark"));

				String pmb_emission_test_exp_dateString = rs.getString("pmb_emission_test_exp_date");
				if (pmb_emission_test_exp_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setExpiryDateEmissionTest = originalFormat.parse(pmb_emission_test_exp_dateString);
					amendmentDTO.setExpiryDateEmissionTest(setExpiryDateEmissionTest);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return amendmentDTO;

	}

	@Override
	public boolean checkTempRoute(String generatedApplicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean returnVal = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "select * from public.nt_temp_route_request where (trr_status ='C' or trr_status='P') and trr_application_no=?; ";

			ps = con.prepareStatement(query);
			ps.setString(1, generatedApplicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				returnVal =true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return returnVal;
	}
	
	
	@Override
	public int insertHistoryAndUpdateTable(OminiBusDTO ominiBusDTO, AmendmentDTO amendmentDTO,AmendmentDTO dto, String vehiNo,
			String generatedApplicationNo, String loggingUser, PermitRenewalsDTO applicationHistoryDTO,
			String string1) {

		Connection con = null;
		//vehi owner/ omini
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		PreparedStatement stmt3 = null;
		PreparedStatement stmt4 = null;
		ResultSet rs = null;
		
		//amendment
		PreparedStatement stmt5 = null;
		
		PreparedStatement ps = null;
		
		
		//application
		PreparedStatement stmt6 = null;
		

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();
			
			
			/***Omini bus, Vehi Owner history Insert and table update Start***/
			
			String omniHistory ="insert into public.nt_h_pm_omini_bus1 (select * from public.nt_t_pm_omini_bus1 where pmb_application_no =?);";
			
			stmt2 = con.prepareStatement(omniHistory);

			stmt2.setString(1, ominiBusDTO.getApplicationNo());
			stmt2.executeUpdate();
			
			
			String vehiOwnerHistory ="insert into public.nt_h_pm_vehi_owner (select * from public.nt_t_pm_vehi_owner where pmo_application_no =?);";
			
			stmt3 = con.prepareStatement(vehiOwnerHistory);

			stmt3.setString(1, ominiBusDTO.getApplicationNo());
			stmt3.executeUpdate();
			
			
			/*Update Omni Bus  table**/	

			String sql = "UPDATE public.nt_t_pm_omini_bus1" + " SET pmb_application_no=?, pmb_vehicle_regno=?,"
					+ "pmb_seating_capacity=?, pmb_no_of_doors=?,"
					+ "pmb_height=?, pmb_production_date=?, pmb_weight=?,"
					+ "pmb_serial_no=?, pmb_certificate_date=?, pmb_garage_reg_no=?, "
					+ "pmb_insurance_company=?, pmb_insurance_cat=?, pmb_insurance_expire_date=?, "
					+ "pmb_bus_price=?, pmb_is_loan_obtain=?, pmb_finance_company=?,"
					+ "pmb_bank_loan_amt=?, pmb_due_amt=?, pmb_date_obtain=?,"
					+ "pmb_lapsed_installment=?, pmb_revenue_license_exp_date=?,"
					+ "pmb_first_reg_date=?, pmb_is_backlog_app=?,"
					+ " pmb_modified_by=?, pmb_modified_date=?, pmb_garage_name=?, pmb_policy_no=?, pmb_emission_test_exp_date=? "
					+ " WHERE seq= ?";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, ominiBusDTO.getApplicationNo());
			stmt.setString(2, ominiBusDTO.getVehicleRegNo());
			stmt.setString(3, ominiBusDTO.getSeating());
			stmt.setString(4, ominiBusDTO.getNoofDoors());
			stmt.setString(5, ominiBusDTO.getHeight());
			stmt.setString(6, ominiBusDTO.getManufactureDate());
			stmt.setString(7, ominiBusDTO.getWeight());
			stmt.setString(8, ominiBusDTO.getSerialNo());

			if (ominiBusDTO.getFitnessCertiDate() != null) {
				String getFitnessCertiDate = (dateFormat.format(ominiBusDTO.getFitnessCertiDate()));
				stmt.setString(9, getFitnessCertiDate);
			} else {
				stmt.setString(9, null);
			}

			stmt.setString(10, ominiBusDTO.getGarageRegNo());
			stmt.setString(11, ominiBusDTO.getInsuCompName());
			stmt.setString(12, ominiBusDTO.getInsuCat());

			if (ominiBusDTO.getInsuExpDate() != null) {
				String getInsuExpDate = (dateFormat.format(ominiBusDTO.getInsuExpDate()));
				stmt.setString(13, getInsuExpDate);
			} else {
				stmt.setString(13, null);
			}

			stmt.setBigDecimal(14, ominiBusDTO.getPriceValOfBus());
			stmt.setString(15, ominiBusDTO.getIsLoanObtained());
			stmt.setString(16, ominiBusDTO.getFinanceCompany());
			stmt.setBigDecimal(17, ominiBusDTO.getBankLoan());
			stmt.setBigDecimal(18, ominiBusDTO.getDueAmount());

			if (ominiBusDTO.getDateObtained() != null) {
				String getDateObtained = (dateFormat.format(ominiBusDTO.getDateObtained()));
				stmt.setString(19, getDateObtained);
			} else {
				stmt.setString(19, null);
			}

			stmt.setString(20, ominiBusDTO.getLapsedInstall());

		/*	if (ominiBusDTO.getExpiryDateRevLic() != null) {
				String getExpiryDateRevLic = (dateFormat.format(ominiBusDTO.getExpiryDateRevLic()));
				stmt.setString(21, getExpiryDateRevLic);
			} else {
				stmt.setString(21, null);
			}*/
			
			
			if (ominiBusDTO.getExpiryDateRevLicNew() != null) {
				String getExpiryDateRevLic = (dateFormat.format(ominiBusDTO.getExpiryDateRevLicNew()));
				stmt.setString(21, getExpiryDateRevLic);
			} else {
				stmt.setString(21, null);
			}

			if (ominiBusDTO.getDateOfFirstReg() != null) {
				String getDateOfFirstReg = (dateFormat.format(ominiBusDTO.getDateOfFirstReg()));
				stmt.setString(22, getDateOfFirstReg);
			} else {
				stmt.setString(22, null);
			}

			stmt.setString(23, ominiBusDTO.getIsBacklogApp());
			stmt.setString(24, ominiBusDTO.getModifiedBy());
			stmt.setTimestamp(25, timestamp);
			stmt.setString(26, ominiBusDTO.getGarageName());
			stmt.setString(27, ominiBusDTO.getPolicyNo());
			if (ominiBusDTO.getEmmissionTestExpireDate() != null) {
				String getEmissionExpDateVal = (dateFormat.format(ominiBusDTO.getEmmissionTestExpireDate()));
				stmt.setString(28, getEmissionExpDateVal);
			} else {
				stmt.setString(28, null);
			}
			stmt.setLong(29, ominiBusDTO.getSeq());

			stmt.executeUpdate();
			
		/*Update Vehicle Owner table**/	
			
			String sql2 = "UPDATE public.nt_t_pm_vehi_owner" + " SET  pmo_vehicle_regno=?,"
					+ " pm_modified_by=? , pm_modified_date =? "
					+ " WHERE pmo_application_no= ?";

			stmt4 = con.prepareStatement(sql2);

			
			stmt4.setString(1, ominiBusDTO.getVehicleRegNo());
			stmt4.setString(2, ominiBusDTO.getModifiedBy());
			stmt4.setTimestamp(3, timestamp);
			stmt4.setString(4, ominiBusDTO.getApplicationNo());
			stmt4.executeUpdate();
			
			/***Omini bus, Vehi Owner history Insert and table update finished***/
			
			
			/***Update amendmend table and insert history ***/
			
			String sqlAmend = "UPDATE nt_m_amendments SET amd_is_bus_running  = ?, amd_hv_legal_case  = ?, amd_details_of_case = ?, amd_modified_by = ?, amd_modified_date = ?, amd_relationship_with_transferor = ?,amd_transferor_remarks = ?,amd_reasonfor_ownerchange = ?,amd_new_busno = ?, amd_existing_busno = ?, amd_service_change_type = ?,amd_remarks = ?,amd_is_omnibus = ?"
					+ " WHERE seq = ? ";

			stmt5 = con.prepareStatement(sqlAmend);

			if (amendmentDTO.getIsBusRunning() != null && amendmentDTO.getIsBusRunning().equalsIgnoreCase("true")) {
				stmt5.setString(1, "Y");
			} else if (amendmentDTO.getIsBusRunning() != null
					&& amendmentDTO.getIsBusRunning().equalsIgnoreCase("false")) {
				stmt5.setString(1, "N");
			} else {
				stmt5.setString(1, null);
			}

			if (amendmentDTO.getHaveLegalCase() != null && amendmentDTO.getHaveLegalCase().equalsIgnoreCase("true")) {
				stmt5.setString(2, "Y");
			} else if (amendmentDTO.getHaveLegalCase() != null
					&& amendmentDTO.getHaveLegalCase().equalsIgnoreCase("false")) {
				stmt5.setString(2, "N");
			} else {
				stmt5.setString(2, null);
			}

			stmt5.setString(3, amendmentDTO.getLegalCaseDetails());

			stmt5.setString(4, amendmentDTO.getModifiedBy());

			stmt5.setTimestamp(5, timestamp);

			stmt5.setString(6, amendmentDTO.getRelationshipWithTransferor());

			stmt5.setString(7, amendmentDTO.getRelationshipWithTransferorRemarks());

			stmt5.setString(8, amendmentDTO.getReasonForOwnerChange());

			stmt5.setString(9, amendmentDTO.getBusRegNo());
			stmt5.setString(10, amendmentDTO.getOldBusNo());
			stmt5.setString(11, amendmentDTO.getServiceChangeType());
			stmt5.setString(12, amendmentDTO.getRemarks());

			if (amendmentDTO.getIsOminiBus() != null && amendmentDTO.getIsOminiBus().equalsIgnoreCase("true")) {
				stmt5.setString(13, "Y");
			} else if (amendmentDTO.getIsOminiBus() != null && amendmentDTO.getIsOminiBus().equalsIgnoreCase("false")) {
				stmt5.setString(13, "N");
			} else {
				stmt5.setString(13, null);
			}

			stmt5.setLong(14, amendmentDTO.getSeq());

			stmt5.executeUpdate();

			
			
			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_h_amendments");

			String sqlAmendHistory = "INSERT INTO public.nt_h_amendments "
					+ "(seq, amd_queue_no, amd_permit_no, amd_application_no, amd_remarks, amd_service_change_type,"
					+ " amd_commity_approved, amd_board_approved, amd_status, amd_existing_bus_remark, amd_is_bus_running,"
					+ " amd_is_print, amd_hv_legal_case, amd_details_of_case, amd_committee_remark, amd_board_reject_reason,"
					+ " amd_busowner_remarks, amd_curr_expire_date, amd_new_expire_date, amd_relationship_with_transferor,"
					+ " amd_transferor_remarks, amd_reasonfor_ownerchange, amd_is_omnibus, amd_new_busno, amd_existing_busno,"
					+ " amd_created_by, amd_created_date, amd_modified_by, amd_modified_date, amd_trn_type, "
					+ " amd_first_reject_remarks, amd_pta, amd_time_approval,amd_timeapproval_service_change) "
					+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

			ps = con.prepareStatement(sqlAmendHistory);

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

			
			/***Update amendmend table and insert history end***/
			
			/***Update application table and insert history start***/
	

			

				String sqlApplication = "UPDATE nt_t_pm_application SET pm_vehicle_regno = ?, pm_modified_by=?, pm_modified_date=? "
						+ "  WHERE pm_application_no = ?";
				
				stmt6 = con.prepareStatement(sqlApplication);
				stmt6.setString(1, vehiNo);
				stmt6.setString(2, loggingUser);
				stmt6.setTimestamp(3, timestamp);
				stmt6.setString(4, generatedApplicationNo);
				stmt6.executeUpdate();

		
		
			
			
			
			
		con.commit();

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(stmt3);
			ConnectionManager.close(ps);
			ConnectionManager.close(stmt4);
			ConnectionManager.close(stmt5);
			ConnectionManager.close(stmt6);
			ConnectionManager.close(con);
		}

	
		return 0;
	
		
	}

	@Override
	public int insertDataAppVehiOminiAmend(OminiBusDTO ominiBusDTO, String oldApplicationNo,
			String generatedApplicationNo, String loginUser, String ominiVehiNumber, String numGennCode,
			AmendmentDTO amendmentDTO, String strVehicleNo, AmendmentDTO dto) {
		
		
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		
		PreparedStatement stmt2 = null;
		ResultSet rs2 = null;
		
		
		PreparedStatement stmt3 = null;
		ResultSet rs3 = null;
		
		PreparedStatement stmt4 = null;
		
		PreparedStatement stmt5 = null;
		ResultSet rs4 = null;
		
		PreparedStatement stmt6 = null;
		
		PreparedStatement stmt7 = null;
		PreparedStatement ps = null; // Amendment history insert

		java.util.Date dateOmini = new java.util.Date();
		Timestamp timestampOmini = new Timestamp(dateOmini.getTime());
		DateFormat dateFormatOmini = new SimpleDateFormat("dd/MM/yyyy");

		try {

			con = ConnectionManager.getConnection();
//insert into application table with  new appNo
			String sql = "INSERT INTO nt_t_pm_application( "
					+ "seq, pm_application_no, pm_queue_no, pm_permit_no, pm_vehicle_regno, pm_permit_issue_date, pm_permit_issue_by, pm_inspect_date, pm_tender_ref_no, pm_status, pm_tender_annual_fee, pm_installments, pm_is_permit_print, pm_reject_reason, pm_valid_from, pm_valid_to, pm_approved_by, pm_appoved_date, pm_special_remark, pm_is_tender_permit, pm_service_type, pm_route_no, pm_tot_bus_fare, pm_is_backlog_app, pm_created_by, pm_created_date, pm_modified_by, pm_modified_date, pm_permit_expire_date, pm_renewal_period, pm_next_ins_date_sec1_2, pm_next_ins_date_sec3, pm_inspect_remark, pm_route_flag, pm_isnew_permit, pm_reinspec_status, pm_old_permit_no, pm_temp_permit_no"
					+ ")" + "SELECT nextval('seq_nt_t_pm_application'), "
					+ "      ?, ?, pm_permit_no, ?, pm_permit_issue_date, pm_permit_issue_by, pm_inspect_date, pm_tender_ref_no, ?, pm_tender_annual_fee, pm_installments, pm_is_permit_print, pm_reject_reason, pm_valid_from, pm_valid_to, pm_approved_by, pm_appoved_date, pm_special_remark, pm_is_tender_permit, pm_service_type, pm_route_no, pm_tot_bus_fare, pm_is_backlog_app, ?, ?, pm_modified_by, pm_modified_date, pm_permit_expire_date, pm_renewal_period, pm_next_ins_date_sec1_2, pm_next_ins_date_sec3, pm_inspect_remark, pm_route_flag, pm_isnew_permit, pm_reinspec_status, pm_old_permit_no, pm_temp_permit_no"
					+ " FROM nt_t_pm_application WHERE pm_permit_no=? AND pm_status='A'";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, ominiBusDTO.getApplicationNo());
			stmt.setString(2, ominiBusDTO.getQueueNo());
			stmt.setString(3, ominiBusDTO.getVehicleRegNo());
			stmt.setString(4, "P");
			stmt.setString(5, ominiBusDTO.getCreatedBy());
			stmt.setTimestamp(6, timestamp);
			stmt.setString(7, ominiBusDTO.getPermitNo());

			stmt.executeUpdate();
			
			
		//update old application number

			String sql2 = "UPDATE nt_t_pm_application SET pm_old_application_no = ?, pm_modified_by=?, pm_modified_date=? "
					+ " WHERE pm_application_no = ?";

			stmt2 = con.prepareStatement(sql2);
			stmt2.setString(1, oldApplicationNo);
			stmt2.setString(2, loginUser);
			stmt2.setTimestamp(3, timestamp);
			stmt2.setString(4, generatedApplicationNo);
			stmt2.executeUpdate();
// insert into vehi Owner with new app no

			String sql3 = "INSERT INTO nt_t_pm_vehi_owner(seq, pmo_application_no, pmo_permit_no, pmo_vehicle_regno, pmo_preferred_language, pmo_title, pmo_full_name, pmo_full_name_sinhala, pmo_full_name_tamil, pmo_name_with_initial, pmo_nic, pmo_gender, pmo_dob, pmo_marital_status, pmo_telephone_no, pmo_mobile_no, pmo_province, pmo_district, pmo_div_sec, pmo_address1, pmo_address1_sinhala, pmo_address1_tamil, pmo_address2, pmo_address2_sinhala, pmo_address2_tamil, pmo_address3, pmo_address3_sinhala, pmo_address3_tamil, pmo_city, pmo_city_sinhala, pmo_city_tamil, pmo_is_backlog_app, pm_created_by, pm_created_date"
					+ "					)SELECT nextval('seq_nt_t_pm_vehi_owner'), "
					+ "				   ?,pmo_permit_no, ?, pmo_preferred_language, pmo_title, pmo_full_name, pmo_full_name_sinhala, pmo_full_name_tamil, pmo_name_with_initial, pmo_nic, pmo_gender, pmo_dob, pmo_marital_status, pmo_telephone_no, pmo_mobile_no, pmo_province, pmo_district, pmo_div_sec, pmo_address1, pmo_address1_sinhala, pmo_address1_tamil, pmo_address2, pmo_address2_sinhala, pmo_address2_tamil, pmo_address3, pmo_address3_sinhala, pmo_address3_tamil, pmo_city, pmo_city_sinhala, pmo_city_tamil, pmo_is_backlog_app, ?, ?"
					+ "				FROM nt_t_pm_vehi_owner WHERE pmo_application_no=?";

			stmt3 = con.prepareStatement(sql3);

			stmt3.setString(1, generatedApplicationNo);
			stmt3.setString(2, ominiVehiNumber);
			stmt3.setString(3, loginUser);
			stmt3.setTimestamp(4, timestamp);
			stmt3.setString(5, oldApplicationNo);

			stmt3.executeUpdate();
			
			
	//Omini bus save
			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_pm_omini_bus1");
			String sql5 = "INSERT INTO public.nt_t_pm_omini_bus1 (" + "pmb_application_no, pmb_vehicle_regno,"
					+ "pmb_seating_capacity, pmb_no_of_doors," + "pmb_height, pmb_production_date, pmb_weight,"
					+ "pmb_serial_no, pmb_certificate_date, pmb_garage_reg_no,"
					+ "pmb_insurance_company, pmb_insurance_cat, pmb_insurance_expire_date,"
					+ "pmb_bus_price, pmb_is_loan_obtain, pmb_finance_company,"
					+ "pmb_bank_loan_amt, pmb_due_amt, pmb_date_obtain,"
					+ "pmb_lapsed_installment, pmb_revenue_license_exp_date,"
					+ "pmb_first_reg_date, pmb_is_backlog_app,"
					+ "pmb_modified_by, pmb_modified_date,pmb_permit_no,seq,  pmb_policy_no,pmb_garage_name, pmb_emission_test_exp_date)"
					+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			stmt5 = con.prepareStatement(sql5);

			stmt5.setString(1, ominiBusDTO.getApplicationNo());
			stmt5.setString(2, ominiBusDTO.getVehicleRegNo());
			stmt5.setString(3, ominiBusDTO.getSeating());
			stmt5.setString(4, ominiBusDTO.getNoofDoors());
			stmt5.setString(5, ominiBusDTO.getHeight());
			stmt5.setString(6, ominiBusDTO.getManufactureDate());
			stmt5.setString(7, ominiBusDTO.getWeight());
			stmt5.setString(8, ominiBusDTO.getSerialNo());

			if (ominiBusDTO.getFitnessCertiDate() != null) {
				String getFitnessCertiDate = (dateFormatOmini.format(ominiBusDTO.getFitnessCertiDate()));
				stmt5.setString(9, getFitnessCertiDate);
			} else {
				stmt5.setString(9, null);
			}

			stmt5.setString(10, ominiBusDTO.getGarageRegNo());
			stmt5.setString(11, ominiBusDTO.getInsuCompName());
			stmt5.setString(12, ominiBusDTO.getInsuCat());

			if (ominiBusDTO.getInsuExpDate() != null) {
				String getInsuExpDate = (dateFormatOmini.format(ominiBusDTO.getInsuExpDate()));
				stmt5.setString(13, getInsuExpDate);
			} else {
				stmt5.setString(13, null);
			}

			stmt5.setBigDecimal(14, ominiBusDTO.getPriceValOfBus());
			stmt5.setString(15, ominiBusDTO.getIsLoanObtained());
			stmt5.setString(16, ominiBusDTO.getFinanceCompany());
			stmt5.setBigDecimal(17, ominiBusDTO.getBankLoan());
			stmt5.setBigDecimal(18, ominiBusDTO.getDueAmount());

			if (ominiBusDTO.getDateObtained() != null) {
				String getDateObtained = (dateFormatOmini.format(ominiBusDTO.getDateObtained()));
				stmt5.setString(19, getDateObtained);
			} else {
				stmt5.setString(19, null);
			}

			stmt5.setString(20, ominiBusDTO.getLapsedInstall());

			if (ominiBusDTO.getExpiryDateRevLicNew() != null) {
				String getExpiryDateRevLic = (dateFormatOmini.format(ominiBusDTO.getExpiryDateRevLicNew()));
				stmt5.setString(21, getExpiryDateRevLic);
			} else {
				stmt5.setString(21, null);
			}

			if (ominiBusDTO.getDateOfFirstReg() != null) {
				String getDateOfFirstReg = (dateFormatOmini.format(ominiBusDTO.getDateOfFirstReg()));
				stmt5.setString(22, getDateOfFirstReg);
			} else {
				stmt5.setString(22, null);
			}

			stmt5.setString(23, ominiBusDTO.getIsBacklogApp());
			stmt5.setString(24, ominiBusDTO.getModifiedBy());
			stmt5.setTimestamp(25, timestamp);
			stmt5.setString(26, ominiBusDTO.getPermitNo());
			stmt5.setLong(27, seqNo);
			stmt5.setString(28, ominiBusDTO.getPolicyNo());
			stmt5.setString(29, ominiBusDTO.getGarageName());
			if (ominiBusDTO.getEmmissionTestExpireDate() != null) {
				String getEmissionExpDateVal = (dateFormatOmini.format(ominiBusDTO.getEmmissionTestExpireDate()));
				stmt5.setString(30, getEmissionExpDateVal);
			} else {
				stmt5.setString(30, null);
			}
			stmt5.executeUpdate();
			
		// number generation table update	
			String sql4 = "UPDATE public.nt_r_number_generation "
					+ "SET  app_no=?, modified_by=?, modified_date=? WHERE code=?;";

			stmt4 = con.prepareStatement(sql4);
			stmt4.setString(1, generatedApplicationNo);
			stmt4.setString(2, loginUser);
			stmt4.setTimestamp(3, timestamp);
			stmt4.setString(4, numGennCode);
			stmt4.executeUpdate();
			
			
	// insert into amendment table 
			if(amendmentDTO.getSeq() == 0){
			long seqNoAmend = Utils.getNextValBySeqName(con, "seq_nt_m_amendments");

			String sql6 = "INSERT INTO nt_m_amendments "
					+ "(seq, amd_queue_no, amd_permit_no, amd_application_no, amd_status, amd_is_bus_running, amd_hv_legal_case, amd_details_of_case,amd_new_busno, amd_existing_busno, amd_created_by, amd_created_date, amd_trn_type,amd_is_omnibus)"
					+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			stmt6 = con.prepareStatement(sql6);

			stmt6.setLong(1, seqNoAmend);
			stmt6.setString(2, amendmentDTO.getQueueNo());
			stmt6.setString(3, amendmentDTO.getPermitNo());
			stmt6.setString(4, amendmentDTO.getApplicationNo());
			stmt6.setString(5, "P");

			if (amendmentDTO.getIsBusRunning() != null && amendmentDTO.getIsBusRunning().equalsIgnoreCase("true")) {
				stmt.setString(6, "Y");
			} else if (amendmentDTO.getIsBusRunning() != null
					&& amendmentDTO.getIsBusRunning().equalsIgnoreCase("false")) {
				stmt6.setString(6, "N");
			} else {
				stmt6.setString(6, null);
			}

			if (amendmentDTO.getHaveLegalCase() != null && amendmentDTO.getHaveLegalCase().equalsIgnoreCase("true")) {
				stmt6.setString(7, "Y");
			} else if (amendmentDTO.getHaveLegalCase() != null
					&& amendmentDTO.getHaveLegalCase().equalsIgnoreCase("false")) {
				stmt6.setString(7, "N");
			} else {
				stmt6.setString(7, null);
			}

			stmt6.setString(8, amendmentDTO.getLegalCaseDetails());
			stmt6.setString(9, amendmentDTO.getBusRegNo());
			stmt6.setString(10, amendmentDTO.getOldBusNo());
			stmt6.setString(11, amendmentDTO.getCreatedBy());
			stmt6.setTimestamp(12, timestamp);
			stmt6.setString(13, amendmentDTO.getTrnType());

			if (amendmentDTO.getIsOminiBus() != null && amendmentDTO.getIsOminiBus().equalsIgnoreCase("true")) {
				stmt6.setString(14, "Y");
			} else if (amendmentDTO.getIsOminiBus() != null && amendmentDTO.getIsOminiBus().equalsIgnoreCase("false")) {
				stmt6.setString(14, "N");
			} else {
				stmt6.setString(14, null);
			}

			stmt6.executeUpdate();
			}
			
			else {
				// insert history and update amendment
				
				amendmentDTO.setOldBusNo(strVehicleNo);

				amendmentDTO
						.setBusRegNo(ominiBusDTO.getVehicleRegNo());
				
				String sql7 = "UPDATE nt_m_amendments SET amd_is_bus_running  = ?, amd_hv_legal_case  = ?, amd_details_of_case = ?, amd_modified_by = ?, amd_modified_date = ?, amd_relationship_with_transferor = ?,amd_transferor_remarks = ?,amd_reasonfor_ownerchange = ?,amd_new_busno = ?, amd_existing_busno = ?, amd_service_change_type = ?,amd_remarks = ?,amd_is_omnibus = ?"
						+ " WHERE seq = ? ";

				stmt7 = con.prepareStatement(sql7);

				if (amendmentDTO.getIsBusRunning() != null && amendmentDTO.getIsBusRunning().equalsIgnoreCase("true")) {
					stmt7.setString(1, "Y");
				} else if (amendmentDTO.getIsBusRunning() != null
						&& amendmentDTO.getIsBusRunning().equalsIgnoreCase("false")) {
					stmt7.setString(1, "N");
				} else {
					stmt7.setString(1, null);
				}

				if (amendmentDTO.getHaveLegalCase() != null && amendmentDTO.getHaveLegalCase().equalsIgnoreCase("true")) {
					stmt7.setString(2, "Y");
				} else if (amendmentDTO.getHaveLegalCase() != null
						&& amendmentDTO.getHaveLegalCase().equalsIgnoreCase("false")) {
					stmt7.setString(2, "N");
				} else {
					stmt7.setString(2, null);
				}

				stmt7.setString(3, amendmentDTO.getLegalCaseDetails());

				stmt7.setString(4, amendmentDTO.getModifiedBy());

				stmt7.setTimestamp(5, timestamp);

				stmt7.setString(6, amendmentDTO.getRelationshipWithTransferor());

				stmt7.setString(7, amendmentDTO.getRelationshipWithTransferorRemarks());

				stmt7.setString(8, amendmentDTO.getReasonForOwnerChange());

				stmt7.setString(9, amendmentDTO.getBusRegNo());
				stmt7.setString(10, amendmentDTO.getOldBusNo());
				stmt7.setString(11, amendmentDTO.getServiceChangeType());
				stmt7.setString(12, amendmentDTO.getRemarks());

				if (amendmentDTO.getIsOminiBus() != null && amendmentDTO.getIsOminiBus().equalsIgnoreCase("true")) {
					stmt7.setString(13, "Y");
				} else if (amendmentDTO.getIsOminiBus() != null && amendmentDTO.getIsOminiBus().equalsIgnoreCase("false")) {
					stmt7.setString(13, "N");
				} else {
					stmt7.setString(13, null);
				}

				stmt7.setLong(14, amendmentDTO.getSeq());

				stmt7.executeUpdate();

				long seqNoAmendHis = Utils.getNextValBySeqName(con, "seq_nt_h_amendments");

				String sql8 = "INSERT INTO public.nt_h_amendments "
						+ "(seq, amd_queue_no, amd_permit_no, amd_application_no, amd_remarks, amd_service_change_type,"
						+ " amd_commity_approved, amd_board_approved, amd_status, amd_existing_bus_remark, amd_is_bus_running,"
						+ " amd_is_print, amd_hv_legal_case, amd_details_of_case, amd_committee_remark, amd_board_reject_reason,"
						+ " amd_busowner_remarks, amd_curr_expire_date, amd_new_expire_date, amd_relationship_with_transferor,"
						+ " amd_transferor_remarks, amd_reasonfor_ownerchange, amd_is_omnibus, amd_new_busno, amd_existing_busno,"
						+ " amd_created_by, amd_created_date, amd_modified_by, amd_modified_date, amd_trn_type, "
						+ " amd_first_reject_remarks, amd_pta, amd_time_approval,amd_timeapproval_service_change) "
						+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

				ps = con.prepareStatement(sql8);

				ps.setLong(1, seqNoAmendHis);
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

			}
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(stmt3);
			ConnectionManager.close(stmt4);
			ConnectionManager.close(stmt5);
			ConnectionManager.close(stmt6);
			ConnectionManager.close(stmt7);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);

		}
		return 0;
	}

	@Override
	public int insertDataAppVehiOminiAmend(String ominiVehicleNo, String generatedApplicationNo, String loginUser,
			String numGennCode, OminiBusDTO ominiBusDTO, AmendmentDTO amendmentDTO, String strVehicleNo,
			AmendmentDTO dto) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		
		
		PreparedStatement stmt2 = null;
		ResultSet rs2 = null;

		PreparedStatement stmt3 = null;
		
		PreparedStatement stmt4 = null;
		PreparedStatement ps = null;
		
		java.util.Date dateOmini = new java.util.Date();
		Timestamp timestampOmini = new Timestamp(dateOmini.getTime());
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {

			con = ConnectionManager.getConnection();

			String sql = "UPDATE nt_t_pm_application SET pm_vehicle_regno = ?, pm_modified_by=?, pm_modified_date=? "
					+ "  WHERE pm_application_no = ?";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, ominiVehicleNo);
			stmt.setString(2, loginUser);
			stmt.setTimestamp(3, timestamp);
			stmt.setString(4, generatedApplicationNo);
			stmt.executeUpdate();
			
			
			//insert ominitable
			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_pm_omini_bus1");

			String sql2 = "INSERT INTO public.nt_t_pm_omini_bus1 (" + "pmb_application_no, pmb_vehicle_regno,"
					+ "pmb_seating_capacity, pmb_no_of_doors," + "pmb_height, pmb_production_date, pmb_weight,"
					+ "pmb_serial_no, pmb_certificate_date, pmb_garage_reg_no,"
					+ "pmb_insurance_company, pmb_insurance_cat, pmb_insurance_expire_date,"
					+ "pmb_bus_price, pmb_is_loan_obtain, pmb_finance_company,"
					+ "pmb_bank_loan_amt, pmb_due_amt, pmb_date_obtain,"
					+ "pmb_lapsed_installment, pmb_revenue_license_exp_date,"
					+ "pmb_first_reg_date, pmb_is_backlog_app,"
					+ "pmb_modified_by, pmb_modified_date,pmb_permit_no,seq,  pmb_policy_no,pmb_garage_name, pmb_emission_test_exp_date)"
					+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			stmt2 = con.prepareStatement(sql2);

			stmt2.setString(1, ominiBusDTO.getApplicationNo());
			stmt2.setString(2, ominiBusDTO.getVehicleRegNo());
			stmt2.setString(3, ominiBusDTO.getSeating());
			stmt2.setString(4, ominiBusDTO.getNoofDoors());
			stmt2.setString(5, ominiBusDTO.getHeight());
			stmt2.setString(6, ominiBusDTO.getManufactureDate());
			stmt2.setString(7, ominiBusDTO.getWeight());
			stmt2.setString(8, ominiBusDTO.getSerialNo());

			if (ominiBusDTO.getFitnessCertiDate() != null) {
				String getFitnessCertiDate = (dateFormat.format(ominiBusDTO.getFitnessCertiDate()));
				stmt2.setString(9, getFitnessCertiDate);
			} else {
				stmt2.setString(9, null);
			}

			stmt2.setString(10, ominiBusDTO.getGarageRegNo());
			stmt2.setString(11, ominiBusDTO.getInsuCompName());
			stmt2.setString(12, ominiBusDTO.getInsuCat());

			if (ominiBusDTO.getInsuExpDate() != null) {
				String getInsuExpDate = (dateFormat.format(ominiBusDTO.getInsuExpDate()));
				stmt2.setString(13, getInsuExpDate);
			} else {
				stmt2.setString(13, null);
			}

			stmt2.setBigDecimal(14, ominiBusDTO.getPriceValOfBus());
			stmt2.setString(15, ominiBusDTO.getIsLoanObtained());
			stmt2.setString(16, ominiBusDTO.getFinanceCompany());
			stmt2.setBigDecimal(17, ominiBusDTO.getBankLoan());
			stmt2.setBigDecimal(18, ominiBusDTO.getDueAmount());

			if (ominiBusDTO.getDateObtained() != null) {
				String getDateObtained = (dateFormat.format(ominiBusDTO.getDateObtained()));
				stmt2.setString(19, getDateObtained);
			} else {
				stmt2.setString(19, null);
			}

			stmt2.setString(20, ominiBusDTO.getLapsedInstall());

			if (ominiBusDTO.getExpiryDateRevLicNew() != null) {
				String getExpiryDateRevLic = (dateFormat.format(ominiBusDTO.getExpiryDateRevLicNew()));
				stmt2.setString(21, getExpiryDateRevLic);
			} else {
				stmt2.setString(21, null);
			}

			if (ominiBusDTO.getDateOfFirstReg() != null) {
				String getDateOfFirstReg = (dateFormat.format(ominiBusDTO.getDateOfFirstReg()));
				stmt2.setString(22, getDateOfFirstReg);
			} else {
				stmt2.setString(22, null);
			}

			stmt2.setString(23, ominiBusDTO.getIsBacklogApp());
			stmt2.setString(24, ominiBusDTO.getModifiedBy());
			stmt2.setTimestamp(25, timestamp);
			stmt2.setString(26, ominiBusDTO.getPermitNo());
			stmt2.setLong(27, seqNo);
			stmt2.setString(28, ominiBusDTO.getPolicyNo());
			stmt2.setString(29, ominiBusDTO.getGarageName());
			if (ominiBusDTO.getEmmissionTestExpireDate() != null) {
				String getEmissionExpDateVal = (dateFormat.format(ominiBusDTO.getEmmissionTestExpireDate()));
				stmt2.setString(30, getEmissionExpDateVal);
			} else {
				stmt2.setString(30, null);
			}
			stmt2.executeUpdate();
			
			//insert/update amendment table
			if (amendmentDTO.getSeq() == 0) {
				long seqNoAmendHis = Utils.getNextValBySeqName(con, "seq_nt_m_amendments");

				String sql3 = "INSERT INTO nt_m_amendments "
						+ "(seq, amd_queue_no, amd_permit_no, amd_application_no, amd_status, amd_is_bus_running, amd_hv_legal_case, amd_details_of_case,amd_new_busno, amd_existing_busno, amd_created_by, amd_created_date, amd_trn_type,amd_is_omnibus)"
						+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				stmt3 = con.prepareStatement(sql3);

				stmt3.setLong(1, seqNoAmendHis);
				stmt3.setString(2, amendmentDTO.getQueueNo());
				stmt3.setString(3, amendmentDTO.getPermitNo());
				stmt3.setString(4, amendmentDTO.getApplicationNo());
				stmt3.setString(5, "P");

				if (amendmentDTO.getIsBusRunning() != null && amendmentDTO.getIsBusRunning().equalsIgnoreCase("true")) {
					stmt3.setString(6, "Y");
				} else if (amendmentDTO.getIsBusRunning() != null
						&& amendmentDTO.getIsBusRunning().equalsIgnoreCase("false")) {
					stmt3.setString(6, "N");
				} else {
					stmt3.setString(6, null);
				}

				if (amendmentDTO.getHaveLegalCase() != null && amendmentDTO.getHaveLegalCase().equalsIgnoreCase("true")) {
					stmt3.setString(7, "Y");
				} else if (amendmentDTO.getHaveLegalCase() != null
						&& amendmentDTO.getHaveLegalCase().equalsIgnoreCase("false")) {
					stmt3.setString(7, "N");
				} else {
					stmt3.setString(7, null);
				}

				stmt3.setString(8, amendmentDTO.getLegalCaseDetails());
				stmt3.setString(9, amendmentDTO.getBusRegNo());
				stmt3.setString(10, amendmentDTO.getOldBusNo());
				stmt3.setString(11, amendmentDTO.getCreatedBy());
				stmt3.setTimestamp(12, timestamp);
				stmt3.setString(13, amendmentDTO.getTrnType());

				if (amendmentDTO.getIsOminiBus() != null && amendmentDTO.getIsOminiBus().equalsIgnoreCase("true")) {
					stmt3.setString(14, "Y");
				} else if (amendmentDTO.getIsOminiBus() != null && amendmentDTO.getIsOminiBus().equalsIgnoreCase("false")) {
					stmt3.setString(14, "N");
				} else {
					stmt3.setString(14, null);
				}

				stmt3.executeUpdate();

			}
			else {
				amendmentDTO.setOldBusNo(strVehicleNo);

				amendmentDTO
						.setBusRegNo(ominiBusDTO.getVehicleRegNo());
				
				String sql4 = "UPDATE nt_m_amendments SET amd_is_bus_running  = ?, amd_hv_legal_case  = ?, amd_details_of_case = ?, amd_modified_by = ?, amd_modified_date = ?, amd_relationship_with_transferor = ?,amd_transferor_remarks = ?,amd_reasonfor_ownerchange = ?,amd_new_busno = ?, amd_existing_busno = ?, amd_service_change_type = ?,amd_remarks = ?,amd_is_omnibus = ?"
						+ " WHERE seq = ? ";

				stmt4 = con.prepareStatement(sql4);

				if (amendmentDTO.getIsBusRunning() != null && amendmentDTO.getIsBusRunning().equalsIgnoreCase("true")) {
					stmt4.setString(1, "Y");
				} else if (amendmentDTO.getIsBusRunning() != null
						&& amendmentDTO.getIsBusRunning().equalsIgnoreCase("false")) {
					stmt4.setString(1, "N");
				} else {
					stmt4.setString(1, null);
				}

				if (amendmentDTO.getHaveLegalCase() != null && amendmentDTO.getHaveLegalCase().equalsIgnoreCase("true")) {
					stmt4.setString(2, "Y");
				} else if (amendmentDTO.getHaveLegalCase() != null
						&& amendmentDTO.getHaveLegalCase().equalsIgnoreCase("false")) {
					stmt4.setString(2, "N");
				} else {
					stmt4.setString(2, null);
				}

				stmt4.setString(3, amendmentDTO.getLegalCaseDetails());

				stmt4.setString(4, amendmentDTO.getModifiedBy());

				stmt.setTimestamp(5, timestamp);

				stmt4.setString(6, amendmentDTO.getRelationshipWithTransferor());

				stmt4.setString(7, amendmentDTO.getRelationshipWithTransferorRemarks());

				stmt4.setString(8, amendmentDTO.getReasonForOwnerChange());

				stmt4.setString(9, amendmentDTO.getBusRegNo());
				stmt4.setString(10, amendmentDTO.getOldBusNo());
				stmt4.setString(11, amendmentDTO.getServiceChangeType());
				stmt4.setString(12, amendmentDTO.getRemarks());

				if (amendmentDTO.getIsOminiBus() != null && amendmentDTO.getIsOminiBus().equalsIgnoreCase("true")) {
					stmt4.setString(13, "Y");
				} else if (amendmentDTO.getIsOminiBus() != null && amendmentDTO.getIsOminiBus().equalsIgnoreCase("false")) {
					stmt4.setString(13, "N");
				} else {
					stmt4.setString(13, null);
				}

				stmt4.setLong(14, amendmentDTO.getSeq());

				stmt4.executeUpdate();
				
				
				
				
				//insert amendment history
				long seqNoAmendHis = Utils.getNextValBySeqName(con, "seq_nt_h_amendments");

				String sql5 = "INSERT INTO public.nt_h_amendments "
						+ "(seq, amd_queue_no, amd_permit_no, amd_application_no, amd_remarks, amd_service_change_type,"
						+ " amd_commity_approved, amd_board_approved, amd_status, amd_existing_bus_remark, amd_is_bus_running,"
						+ " amd_is_print, amd_hv_legal_case, amd_details_of_case, amd_committee_remark, amd_board_reject_reason,"
						+ " amd_busowner_remarks, amd_curr_expire_date, amd_new_expire_date, amd_relationship_with_transferor,"
						+ " amd_transferor_remarks, amd_reasonfor_ownerchange, amd_is_omnibus, amd_new_busno, amd_existing_busno,"
						+ " amd_created_by, amd_created_date, amd_modified_by, amd_modified_date, amd_trn_type, "
						+ " amd_first_reject_remarks, amd_pta, amd_time_approval,amd_timeapproval_service_change) "
						+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

				ps = con.prepareStatement(sql5);

				ps.setLong(1, seqNoAmendHis);
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
			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(stmt3);
			ConnectionManager.close(stmt4);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);

		}
	
	return 0;
	}

	@Override
	public int insertAppOminiVehiAmendInOwnerChange(BusOwnerDTO busOwnerDTO, String oldApplicationNo,
			String generatedApplicationNo, String loginUser, String numGennCode, AmendmentDTO amendmentDTO,
			String strVehicleNo,String ominiBusNumForAmenment, AmendmentDTO dto) {

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		
	
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		PreparedStatement stmt2 = null;
		
		PreparedStatement stmt3 = null;
		PreparedStatement stmt4 = null;
		PreparedStatement stmt5 = null;
		PreparedStatement stmt6 = null;
		PreparedStatement stmt7 = null;
		PreparedStatement ps = null;

		try {

			con = ConnectionManager.getConnection();
			// insert into application table
			String sql = "INSERT INTO nt_t_pm_application( "
					+ "seq, pm_application_no, pm_queue_no, pm_permit_no, pm_vehicle_regno, pm_permit_issue_date, pm_permit_issue_by, pm_inspect_date, pm_tender_ref_no, pm_status, pm_tender_annual_fee, pm_installments, pm_is_permit_print, pm_reject_reason, pm_valid_from, pm_valid_to, pm_approved_by, pm_appoved_date, pm_special_remark, pm_is_tender_permit, pm_service_type, pm_route_no, pm_tot_bus_fare, pm_is_backlog_app, pm_created_by, pm_created_date, pm_modified_by, pm_modified_date, pm_permit_expire_date, pm_renewal_period, pm_next_ins_date_sec1_2, pm_next_ins_date_sec3, pm_inspect_remark, pm_route_flag, pm_isnew_permit, pm_reinspec_status, pm_old_permit_no, pm_temp_permit_no"
					+ ")" + "SELECT nextval('seq_nt_t_pm_application'), "
					+ "      ?, ?, pm_permit_no, pm_vehicle_regno, pm_permit_issue_date, pm_permit_issue_by, pm_inspect_date, pm_tender_ref_no, ?, pm_tender_annual_fee, pm_installments, pm_is_permit_print, pm_reject_reason, pm_valid_from, pm_valid_to, pm_approved_by, pm_appoved_date, pm_special_remark, pm_is_tender_permit, pm_service_type, pm_route_no, pm_tot_bus_fare, pm_is_backlog_app, ?, ?, pm_modified_by, pm_modified_date, pm_permit_expire_date, pm_renewal_period, pm_next_ins_date_sec1_2, pm_next_ins_date_sec3, pm_inspect_remark,?, pm_isnew_permit, pm_reinspec_status, pm_old_permit_no, pm_temp_permit_no"
					+ " FROM nt_t_pm_application WHERE pm_vehicle_regno=? AND pm_status='A'"; // added by tharushi.e

			stmt = con.prepareStatement(sql);

			stmt.setString(1, busOwnerDTO.getApplicationNo());
			stmt.setString(2, busOwnerDTO.getQueueNo());
			stmt.setString(3, "P");
			stmt.setString(4, busOwnerDTO.getCreatedBy());
			stmt.setTimestamp(5, timestamp);
			stmt.setString(6, busOwnerDTO.getRouteFlag());
			stmt.setString(7, busOwnerDTO.getBusRegNo());

			stmt.executeUpdate();
			
			//update old application number
			String sqlUpdateApp = "UPDATE nt_t_pm_application SET pm_old_application_no = ?, pm_modified_by=?, pm_modified_date=? "
					+ " WHERE pm_application_no = ?";

			stmt2 = con.prepareStatement(sqlUpdateApp);
			stmt2.setString(1, oldApplicationNo);
			stmt2.setString(2, loginUser);
			stmt2.setTimestamp(3, timestamp);
			stmt2.setString(4, generatedApplicationNo);
			stmt2.executeUpdate();
			
			//insert into omini 
			String sqlForOmini = "INSERT INTO nt_t_pm_omini_bus1(seq, pmb_application_no, pmb_vehicle_regno, pmb_engine_no, pmb_chassis_no, pmb_make, pmb_model, pmb_seating_capacity, pmb_no_of_doors, pmb_height, pmb_production_date, pmb_weight, pmb_serial_no, pmb_certificate_date, pmb_garage_reg_no, pmb_insurance_company, pmb_insurance_cat, pmb_insurance_expire_date, pmb_bus_price, pmb_is_loan_obtain, pmb_finance_company, pmb_bank_loan_amt, pmb_due_amt, pmb_date_obtain, pmb_lapsed_installment, pmb_revenue_license_exp_date, pmb_first_reg_date, pmb_is_backlog_app, pmb_created_by, pmb_created_date, pmb_permit_no, pmb_policy_no , pmb_emission_test_exp_date, pmb_garage_name"
					+ "					)SELECT nextval('seq_nt_t_pm_omini_bus1'), "
					+ "				   ?, pmb_vehicle_regno, pmb_engine_no, pmb_chassis_no, pmb_make, pmb_model, pmb_seating_capacity, pmb_no_of_doors, pmb_height, pmb_production_date, pmb_weight, pmb_serial_no, pmb_certificate_date, pmb_garage_reg_no, pmb_insurance_company, pmb_insurance_cat, pmb_insurance_expire_date, pmb_bus_price, pmb_is_loan_obtain, pmb_finance_company, pmb_bank_loan_amt, pmb_due_amt, pmb_date_obtain, pmb_lapsed_installment, pmb_revenue_license_exp_date, pmb_first_reg_date, pmb_is_backlog_app, ?, ?, pmb_permit_no, pmb_policy_no, pmb_emission_test_exp_date, pmb_garage_name "
					+ "				FROM nt_t_pm_omini_bus1 WHERE pmb_application_no=?";

			stmt3 = con.prepareStatement(sqlForOmini);

			stmt3.setString(1, generatedApplicationNo);
			stmt3.setString(2, loginUser);
			stmt3.setTimestamp(3, timestamp);
			stmt3.setString(4, oldApplicationNo);

			stmt3.executeUpdate();

			//update amendment application number in number generation table
			String sqlNumberGeneration = "UPDATE public.nt_r_number_generation "
					+ "SET  app_no=?, modified_by=?, modified_date=? WHERE code=?;";

			stmt4 = con.prepareStatement(sqlNumberGeneration);
			stmt4.setString(1, generatedApplicationNo);
			stmt4.setString(2, loginUser);
			stmt4.setTimestamp(3, timestamp);
			stmt4.setString(4, numGennCode);
			stmt4.executeUpdate();
			
			// insert into vehicle_owner table
			
			String sqlForVehiOwner = "INSERT INTO nt_t_pm_vehi_owner (seq ,pmo_preferred_language ,pmo_permit_no"
					+ ",pmo_title ,pmo_gender ,pmo_dob ,pmo_nic ,pmo_full_name"
					+ ",pmo_full_name_sinhala ,pmo_full_name_tamil ,pmo_name_with_initial ,pmo_address1"
					+ ",pmo_address1_sinhala ,pmo_address1_tamil ,pmo_address2 ,pmo_address2_sinhala"
					+ ",pmo_address2_tamil ,pmo_address3 ,pmo_address3_sinhala ,pmo_address3_tamil"
					+ ",pmo_city ,pmo_city_sinhala ,pmo_city_tamil ,pmo_marital_status"
					+ ",pmo_telephone_no ,pmo_mobile_no ,pmo_province ,pmo_district ,pmo_div_sec,pm_created_by,pm_created_date,pmo_is_backlog_app,pmo_application_no,pmo_vehicle_regno)"
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			stmt5 = con.prepareStatement(sqlForVehiOwner);
			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_pm_vehi_owner");

			stmt5.setLong(1, seqNo);
			stmt5.setString(2, busOwnerDTO.getPerferedLanguage());
			stmt5.setString(3, busOwnerDTO.getPermitNo());
			stmt5.setString(4, busOwnerDTO.getTitle());
			stmt5.setString(5, busOwnerDTO.getGender());

			if (busOwnerDTO.getDob() != null) {
				String dob = (dateFormat.format(busOwnerDTO.getDob()));
				stmt5.setString(6, dob);
			} else {
				stmt5.setString(6, null);
			}

			stmt5.setString(7, busOwnerDTO.getNicNo());
			stmt5.setString(8, busOwnerDTO.getFullName());
			stmt5.setString(9, busOwnerDTO.getFullNameSinhala());
			stmt5.setString(10, busOwnerDTO.getFullNameTamil());
			stmt5.setString(11, busOwnerDTO.getNameWithInitials());
			stmt5.setString(12, busOwnerDTO.getAddress1());
			stmt5.setString(13, busOwnerDTO.getAddress1Sinhala());
			stmt5.setString(14, busOwnerDTO.getAddress1Tamil());
			stmt5.setString(15, busOwnerDTO.getAddress2());
			stmt5.setString(16, busOwnerDTO.getAddress2Sinhala());
			stmt5.setString(17, busOwnerDTO.getAddress2Tamil());
			stmt5.setString(18, busOwnerDTO.getAddress3());
			stmt5.setString(19, busOwnerDTO.getAddress3Sinhala());
			stmt5.setString(20, busOwnerDTO.getAddress3Tamil());
			stmt5.setString(21, busOwnerDTO.getCity());
			stmt5.setString(22, busOwnerDTO.getCitySinhala());
			stmt5.setString(23, busOwnerDTO.getCityTamil());
			stmt5.setString(24, busOwnerDTO.getMaritalStatus());
			stmt5.setString(25, busOwnerDTO.getTelephoneNo());
			stmt5.setString(26, busOwnerDTO.getMobileNo());
			stmt5.setString(27, busOwnerDTO.getProvince());
			stmt5.setString(28, busOwnerDTO.getDistrict());
			stmt5.setString(29, busOwnerDTO.getDivSec());
			stmt5.setString(30, busOwnerDTO.getCreatedBy());
			stmt5.setTimestamp(31, timestamp);
			stmt5.setString(32, busOwnerDTO.getIsBacklogApp());
			stmt5.setString(33, busOwnerDTO.getApplicationNo());
			stmt5.setString(34, busOwnerDTO.getBusRegNo());

			stmt5.executeUpdate();

			//insert into amendment or update amendment
			if (amendmentDTO.getSeq() == 0) {
			
			long seqNoAmend = Utils.getNextValBySeqName(con, "seq_nt_m_amendments");

			String sqlAmendment = "INSERT INTO nt_m_amendments "
					+ "(seq, amd_application_no,amd_relationship_with_transferor,amd_transferor_remarks,amd_permit_no,amd_queue_no,amd_created_by,amd_created_date,amd_status,amd_trn_type,amd_reasonfor_ownerchange,amd_new_busno, amd_existing_busno)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?)";

			stmt6 = con.prepareStatement(sqlAmendment);

			stmt6.setLong(1, seqNoAmend);
			stmt6.setString(2, amendmentDTO.getApplicationNo());
			stmt6.setString(3, amendmentDTO.getRelationshipWithTransferor());
			stmt6.setString(4, amendmentDTO.getRelationshipWithTransferorRemarks());
			stmt6.setString(5, amendmentDTO.getPermitNo());
			stmt6.setString(6, amendmentDTO.getQueueNo());
			stmt6.setString(7, amendmentDTO.getCreatedBy());
			stmt6.setTimestamp(8, timestamp);
			stmt6.setString(9, "P");
			stmt6.setString(10, amendmentDTO.getTrnType());
			stmt6.setString(11, amendmentDTO.getReasonForOwnerChange());

			stmt6.setString(12, amendmentDTO.getBusRegNo());
			stmt6.setString(13, amendmentDTO.getOldBusNo());

			stmt6.executeUpdate();
			}
			else {
				amendmentDTO.setModifiedBy(loginUser);
				amendmentDTO.setOldBusNo(strVehicleNo);

				amendmentDTO.setBusRegNo(ominiBusNumForAmenment);
			
			
			String sqlUpdateAmendment = "UPDATE nt_m_amendments SET amd_is_bus_running  = ?, amd_hv_legal_case  = ?, amd_details_of_case = ?, amd_modified_by = ?, amd_modified_date = ?, amd_relationship_with_transferor = ?,amd_transferor_remarks = ?,amd_reasonfor_ownerchange = ?,amd_new_busno = ?, amd_existing_busno = ?, amd_service_change_type = ?,amd_remarks = ?,amd_is_omnibus = ?"
					+ " WHERE seq = ? ";

			stmt7 = con.prepareStatement(sqlUpdateAmendment);

			if (amendmentDTO.getIsBusRunning() != null && amendmentDTO.getIsBusRunning().equalsIgnoreCase("true")) {
				stmt7.setString(1, "Y");
			} else if (amendmentDTO.getIsBusRunning() != null
					&& amendmentDTO.getIsBusRunning().equalsIgnoreCase("false")) {
				stmt7.setString(1, "N");
			} else {
				stmt7.setString(1, null);
			}

			if (amendmentDTO.getHaveLegalCase() != null && amendmentDTO.getHaveLegalCase().equalsIgnoreCase("true")) {
				stmt7.setString(2, "Y");
			} else if (amendmentDTO.getHaveLegalCase() != null
					&& amendmentDTO.getHaveLegalCase().equalsIgnoreCase("false")) {
				stmt7.setString(2, "N");
			} else {
				stmt7.setString(2, null);
			}

			stmt7.setString(3, amendmentDTO.getLegalCaseDetails());

			stmt7.setString(4, amendmentDTO.getModifiedBy());

			stmt7.setTimestamp(5, timestamp);

			stmt.setString(6, amendmentDTO.getRelationshipWithTransferor());

			stmt7.setString(7, amendmentDTO.getRelationshipWithTransferorRemarks());

			stmt7.setString(8, amendmentDTO.getReasonForOwnerChange());

			stmt7.setString(9, amendmentDTO.getBusRegNo());
			stmt7.setString(10, amendmentDTO.getOldBusNo());
			stmt7.setString(11, amendmentDTO.getServiceChangeType());
			stmt7.setString(12, amendmentDTO.getRemarks());

			if (amendmentDTO.getIsOminiBus() != null && amendmentDTO.getIsOminiBus().equalsIgnoreCase("true")) {
				stmt7.setString(13, "Y");
			} else if (amendmentDTO.getIsOminiBus() != null && amendmentDTO.getIsOminiBus().equalsIgnoreCase("false")) {
				stmt7.setString(13, "N");
			} else {
				stmt7.setString(13, null);
			}

			stmt7.setLong(14, amendmentDTO.getSeq());

			stmt7.executeUpdate();

			//insert history into amendment
			
			long seqNoAmendHistory = Utils.getNextValBySeqName(con, "seq_nt_h_amendments");

			String sqlAmendHis = "INSERT INTO public.nt_h_amendments "
					+ "(seq, amd_queue_no, amd_permit_no, amd_application_no, amd_remarks, amd_service_change_type,"
					+ " amd_commity_approved, amd_board_approved, amd_status, amd_existing_bus_remark, amd_is_bus_running,"
					+ " amd_is_print, amd_hv_legal_case, amd_details_of_case, amd_committee_remark, amd_board_reject_reason,"
					+ " amd_busowner_remarks, amd_curr_expire_date, amd_new_expire_date, amd_relationship_with_transferor,"
					+ " amd_transferor_remarks, amd_reasonfor_ownerchange, amd_is_omnibus, amd_new_busno, amd_existing_busno,"
					+ " amd_created_by, amd_created_date, amd_modified_by, amd_modified_date, amd_trn_type, "
					+ " amd_first_reject_remarks, amd_pta, amd_time_approval,amd_timeapproval_service_change) "
					+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

			ps = con.prepareStatement(sqlAmendHis);

			ps.setLong(1, seqNoAmendHistory);
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

			
			}
			con.commit();

		}  catch (SQLException ex) {
			try {
				if(con != null) {
					con.rollback();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return -1;

		}catch (Exception ex) {
			ex.printStackTrace();
			return -1;

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(stmt3);
			ConnectionManager.close(stmt4);
			ConnectionManager.close(stmt5);
			ConnectionManager.close(stmt6);
			ConnectionManager.close(stmt7);
			ConnectionManager.close(con);

		}
		return 0;
	
	}

	@Override
	public int updateInsertForServiceChange(BusOwnerDTO busOwnerDTO, String oldApplicationNo,
			String generatedApplicationNo, String loginUser, String strVehicleNo, String numGennCode,
			String busOwnerRouteFlag, AmendmentServiceDTO amendmentServiceDTO, PermitRenewalsDTO dto) {

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		PreparedStatement stmt3 = null;
		PreparedStatement stmt4 = null;
		PreparedStatement stmt5 = null;
		PreparedStatement stmt6 = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();
			// insert into application table
			String sql = "INSERT INTO nt_t_pm_application( "
					+ "seq, pm_application_no, pm_queue_no, pm_permit_no, pm_vehicle_regno, pm_permit_issue_date, pm_permit_issue_by, pm_inspect_date, pm_tender_ref_no, pm_status, pm_tender_annual_fee, pm_installments, pm_is_permit_print, pm_reject_reason, pm_valid_from, pm_valid_to, pm_approved_by, pm_appoved_date, pm_special_remark, pm_is_tender_permit, pm_service_type, pm_route_no, pm_tot_bus_fare, pm_is_backlog_app, pm_created_by, pm_created_date, pm_modified_by, pm_modified_date, pm_permit_expire_date, pm_renewal_period, pm_next_ins_date_sec1_2, pm_next_ins_date_sec3, pm_inspect_remark, pm_route_flag, pm_isnew_permit, pm_reinspec_status, pm_old_permit_no, pm_temp_permit_no"
					+ ")" + "SELECT nextval('seq_nt_t_pm_application'), "
					+ "      ?, ?, pm_permit_no, pm_vehicle_regno, pm_permit_issue_date, pm_permit_issue_by, pm_inspect_date, pm_tender_ref_no, ?, pm_tender_annual_fee, pm_installments, pm_is_permit_print, pm_reject_reason, pm_valid_from, pm_valid_to, pm_approved_by, pm_appoved_date, pm_special_remark, pm_is_tender_permit, pm_service_type, pm_route_no, pm_tot_bus_fare, pm_is_backlog_app, ?, ?, pm_modified_by, pm_modified_date, pm_permit_expire_date, pm_renewal_period, pm_next_ins_date_sec1_2, pm_next_ins_date_sec3, pm_inspect_remark,?, pm_isnew_permit, pm_reinspec_status, pm_old_permit_no, pm_temp_permit_no"
					+ " FROM nt_t_pm_application WHERE pm_vehicle_regno=? AND pm_status='A'"; // added by tharushi.e

			stmt = con.prepareStatement(sql);

			stmt.setString(1, busOwnerDTO.getApplicationNo());
			stmt.setString(2, busOwnerDTO.getQueueNo());
			stmt.setString(3, "P");
			stmt.setString(4, busOwnerDTO.getCreatedBy());
			stmt.setTimestamp(5, timestamp);
			stmt.setString(6, busOwnerDTO.getRouteFlag());
			stmt.setString(7, busOwnerDTO.getBusRegNo());

			stmt.executeUpdate();
			
			//update application table old application number
			String sqlUpdateOldAppNo = "UPDATE nt_t_pm_application SET pm_old_application_no = ?, pm_modified_by=?, pm_modified_date=? "
					+ " WHERE pm_application_no = ?";

			stmt2 = con.prepareStatement(sqlUpdateOldAppNo);
			stmt2.setString(1, oldApplicationNo);
			stmt2.setString(2, loginUser);
			stmt2.setTimestamp(3, timestamp);
			stmt2.setString(4, generatedApplicationNo);
			stmt2.executeUpdate();

			// insert into vehi owner
			String sqlVehiOwner = "INSERT INTO nt_t_pm_vehi_owner(seq, pmo_application_no, pmo_permit_no, pmo_vehicle_regno, pmo_preferred_language, pmo_title, pmo_full_name, pmo_full_name_sinhala, pmo_full_name_tamil, pmo_name_with_initial, pmo_nic, pmo_gender, pmo_dob, pmo_marital_status, pmo_telephone_no, pmo_mobile_no, pmo_province, pmo_district, pmo_div_sec, pmo_address1, pmo_address1_sinhala, pmo_address1_tamil, pmo_address2, pmo_address2_sinhala, pmo_address2_tamil, pmo_address3, pmo_address3_sinhala, pmo_address3_tamil, pmo_city, pmo_city_sinhala, pmo_city_tamil, pmo_is_backlog_app, pm_created_by, pm_created_date"
					+ "					)SELECT nextval('seq_nt_t_pm_vehi_owner'), "
					+ "				   ?,pmo_permit_no, ?, pmo_preferred_language, pmo_title, pmo_full_name, pmo_full_name_sinhala, pmo_full_name_tamil, pmo_name_with_initial, pmo_nic, pmo_gender, pmo_dob, pmo_marital_status, pmo_telephone_no, pmo_mobile_no, pmo_province, pmo_district, pmo_div_sec, pmo_address1, pmo_address1_sinhala, pmo_address1_tamil, pmo_address2, pmo_address2_sinhala, pmo_address2_tamil, pmo_address3, pmo_address3_sinhala, pmo_address3_tamil, pmo_city, pmo_city_sinhala, pmo_city_tamil, pmo_is_backlog_app, ?, ?"
					+ "				FROM nt_t_pm_vehi_owner WHERE pmo_application_no=?";

			stmt3 = con.prepareStatement(sqlVehiOwner);

			stmt3.setString(1, generatedApplicationNo);
			stmt3.setString(2, strVehicleNo);
			stmt3.setString(3, loginUser);
			stmt3.setTimestamp(4, timestamp);
			stmt3.setString(5, oldApplicationNo);

			stmt3.executeUpdate();
			// insert into omini bus
			String sqlInsertToOmini = "INSERT INTO nt_t_pm_omini_bus1(seq, pmb_application_no, pmb_vehicle_regno, pmb_engine_no, pmb_chassis_no, pmb_make, pmb_model, pmb_seating_capacity, pmb_no_of_doors, pmb_height, pmb_production_date, pmb_weight, pmb_serial_no, pmb_certificate_date, pmb_garage_reg_no, pmb_insurance_company, pmb_insurance_cat, pmb_insurance_expire_date, pmb_bus_price, pmb_is_loan_obtain, pmb_finance_company, pmb_bank_loan_amt, pmb_due_amt, pmb_date_obtain, pmb_lapsed_installment, pmb_revenue_license_exp_date, pmb_first_reg_date, pmb_is_backlog_app, pmb_created_by, pmb_created_date, pmb_permit_no, pmb_policy_no , pmb_emission_test_exp_date, pmb_garage_name"
					+ "					)SELECT nextval('seq_nt_t_pm_omini_bus1'), "
					+ "				   ?, pmb_vehicle_regno, pmb_engine_no, pmb_chassis_no, pmb_make, pmb_model, pmb_seating_capacity, pmb_no_of_doors, pmb_height, pmb_production_date, pmb_weight, pmb_serial_no, pmb_certificate_date, pmb_garage_reg_no, pmb_insurance_company, pmb_insurance_cat, pmb_insurance_expire_date, pmb_bus_price, pmb_is_loan_obtain, pmb_finance_company, pmb_bank_loan_amt, pmb_due_amt, pmb_date_obtain, pmb_lapsed_installment, pmb_revenue_license_exp_date, pmb_first_reg_date, pmb_is_backlog_app, ?, ?, pmb_permit_no, pmb_policy_no, pmb_emission_test_exp_date, pmb_garage_name "
					+ "				FROM nt_t_pm_omini_bus1 WHERE pmb_application_no=?";

			stmt4 = con.prepareStatement(sqlInsertToOmini);

			stmt4.setString(1, generatedApplicationNo);
			stmt4.setString(2, loginUser);
			stmt4.setTimestamp(3, timestamp);
			stmt4.setString(4, oldApplicationNo);

			stmt4.executeUpdate();
			//update number generation table
			String sqlUpdateNumberGeneration = "UPDATE public.nt_r_number_generation "
					+ "SET  app_no=?, modified_by=?, modified_date=? WHERE code=?;";

			stmt5 = con.prepareStatement(sqlUpdateNumberGeneration);
			stmt5.setString(1, generatedApplicationNo);
			stmt5.setString(2, loginUser);
			stmt5.setTimestamp(3, timestamp);
			stmt5.setString(4, numGennCode);
			stmt5.executeUpdate();
			
			
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(stmt3);
			ConnectionManager.close(stmt4);
			ConnectionManager.close(stmt5);
			ConnectionManager.close(stmt6);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);

		}

	return 0;
	}

	@Override
	public String getApplicationStatus(String applicationNo) {


		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String status = null;
		try {

			con = ConnectionManager.getConnection();

			String query = " select pm_status from public.nt_t_pm_application where pm_application_no =?";

			ps = con.prepareStatement(query);
			ps.setString(1, applicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				status = (rs.getString("pm_status"));

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
	public OminiBusDTO emmissionDetByAppNo(String appNo) {


		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		OminiBusDTO ominiBusDTO = new OminiBusDTO();
		try {
			con = ConnectionManager.getConnection();
			String query =

					"SELECT  pmb_serial_no,  pmb_garage_reg_no,pmb_garage_name,pmb_certificate_date ,pmb_revenue_license_exp_date, " + 
					"pmb_emission_test_exp_date " + 
					"FROM public.nt_t_pm_omini_bus1 " + 
					"WHERE  pmb_application_no =?;";

			ps = con.prepareStatement(query);
	
			ps.setString(1, appNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				
				ominiBusDTO.setSerialNo(rs.getString("pmb_serial_no"));

				String pmb_certificate_dateString = rs.getString("pmb_certificate_date");
				if (pmb_certificate_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setFitnessCertiDate = originalFormat.parse(pmb_certificate_dateString);
					ominiBusDTO.setFitnessCertiDate(setFitnessCertiDate);
				}

				ominiBusDTO.setGarageRegNo(rs.getString("pmb_garage_reg_no"));
				ominiBusDTO.setGarageName(rs.getString("pmb_garage_name"));
			


			
				String pmb_revenue_license_exp_dateString = rs.getString("pmb_revenue_license_exp_date");
				if (pmb_revenue_license_exp_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setExpiryDateRevLic = originalFormat.parse(pmb_revenue_license_exp_dateString);
					ominiBusDTO.setExpiryDateRevLic(setExpiryDateRevLic);
					ominiBusDTO.setExpiryDateRevLicNew(setExpiryDateRevLic);
				}

		
			
			

				String pmb_emission_test_exp_dateString = rs.getString("pmb_emission_test_exp_date");
				if (pmb_emission_test_exp_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setExpiryDateEmissionTest = originalFormat.parse(pmb_emission_test_exp_dateString);
					ominiBusDTO.setEmissionExpDate(setExpiryDateEmissionTest);
					ominiBusDTO.setEmmissionTestExpireDate(setExpiryDateEmissionTest);
				}
				
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return ominiBusDTO;

	
	}

	@Override
	public boolean checkSavedTaskAlreadyInTaskHistoryTable(String appNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean returnVal = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT tsd_task_code ,tsd_status FROM public.nt_h_task_his WHERE tsd_app_no =? and tsd_task_code ='AM100' and tsd_status = 'C' ";

			ps = con.prepareStatement(query);
			ps.setString(1, appNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				
				returnVal =true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return returnVal;
	}

}
