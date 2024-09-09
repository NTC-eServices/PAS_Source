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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lk.informatics.ntc.model.dto.BusOwnerDTO;
import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.IssuePermitDTO;
import lk.informatics.ntc.model.dto.OminiBusDTO;
import lk.informatics.ntc.model.dto.PermitRenewalsDTO;
import lk.informatics.ntc.model.dto.RouteDTO;
import lk.informatics.ntc.model.exception.ApplicationException;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.ntc.view.util.PropertyReader;
import lk.informatics.roc.utils.common.Utils;

public class IssuePermitServiceImpl implements IssuePermitService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This method returns data related to bus owner from nt_t_pm_vehi_owner table
	 * 
	 * @param NicNo
	 * @return busOwnerDTO
	 */
	@Override
	public BusOwnerDTO getOwnerDetailsByNicNo(String nicNo) {
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
					+ " pmo_city_tamil,pmo_province,pmo_district,pmo_div_sec  " + " FROM public.nt_t_pm_vehi_owner "
					+ " WHERE pmo_nic =  ? ";

			ps = con.prepareStatement(query);
			ps.setString(1, nicNo);
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

	/**
	 * This method returns data related to bus owner from nt_t_pm_vehi_owner table
	 * 
	 * @param strApplicationNo
	 * @return busOwnerDTO
	 */
	@Override
	public BusOwnerDTO getOwnerDetailsByApplicationNo(String strApplicationNo) {
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
					+ " pmo_city_tamil,pmo_province,pmo_district,pmo_div_sec " + " FROM public.nt_t_pm_vehi_owner "
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

	/**
	 * @param strApplicationNo condition: isBacklogApp = "N"
	 * @return IssuePermitDTO
	 *         (routeFlag,routeNo,permitNo,tenderReferanceNo,applicationNo,queueNo,serviceType,busRegistrationNo)
	 */
	@Override
	public IssuePermitDTO getApplicationDetailsByApplicationNo(String strApplicationNo) {
		Connection con = null;
		PreparedStatement ps = null, ps1 = null;
		ResultSet rs = null, rs1 = null;

		IssuePermitDTO issuePermitDTO = new IssuePermitDTO();

		try {
			con = ConnectionManager.getConnection();
			String query1 = "SELECT pm_permit_no AS pm_permit_no, pm_application_no AS pm_application_no"
					+ " FROM public.nt_t_pm_application " + " WHERE pm_application_no =  ? and pm_isnew_permit ='Y' ";
			ps1 = con.prepareStatement(query1);
			ps1.setString(1, strApplicationNo);

			rs1 = ps1.executeQuery();
			while (rs1.next()) {

				issuePermitDTO.setPermitNo(rs1.getString("pm_permit_no"));

			}
			if (issuePermitDTO.getPermitNo() != null) {

				String query = "SELECT A.pm_route_flag AS pm_route_flag,A.pm_route_no AS pm_route_no,A.pm_permit_no AS pm_permit_no,A.pm_queue_no AS pm_queue_no, A.pm_application_no AS pm_application_no,A.pm_vehicle_regno AS pm_vehicle_regno,B.description AS description,A.pm_tender_ref_no AS pm_tender_ref_no"
						+ " FROM public.nt_t_pm_application A INNER JOIN nt_r_service_types B ON A.pm_service_type=B.code"
						+ " WHERE A.pm_application_no =  ? AND A.pm_is_backlog_app=?";

				ps = con.prepareStatement(query);
				ps.setString(1, strApplicationNo);
				ps.setString(2, "N");
				rs = ps.executeQuery();

				while (rs.next()) {

					issuePermitDTO.setRouteFlag(rs.getString("pm_route_flag"));
					issuePermitDTO.setRouteNo(rs.getString("pm_route_no"));
					issuePermitDTO.setPermitNo(rs.getString("pm_permit_no"));
					issuePermitDTO.setQueueNo(rs.getString("pm_queue_no"));
					issuePermitDTO.setApplicationNo(rs.getString("pm_application_no"));
					issuePermitDTO.setBusRegNo(rs.getString("pm_vehicle_regno"));
					issuePermitDTO.setServiceType(rs.getString("description"));
					issuePermitDTO.setTenderRefNo(rs.getString("pm_tender_ref_no"));

				}
			} else {
				String query = "SELECT A.pm_route_flag AS pm_route_flag,A.pm_route_no AS pm_route_no,A.pm_temp_permit_no  AS pm_permit_no,A.pm_queue_no AS pm_queue_no, A.pm_application_no AS pm_application_no,A.pm_vehicle_regno AS pm_vehicle_regno,B.description AS description,A.pm_tender_ref_no AS pm_tender_ref_no"
						+ " FROM public.nt_t_pm_application A INNER JOIN nt_r_service_types B ON A.pm_service_type=B.code"
						+ " WHERE A.pm_application_no =  ? AND A.pm_is_backlog_app=?";

				ps = con.prepareStatement(query);
				ps.setString(1, strApplicationNo);
				ps.setString(2, "N");
				rs = ps.executeQuery();

				while (rs.next()) {

					issuePermitDTO.setRouteFlag(rs.getString("pm_route_flag"));
					issuePermitDTO.setRouteNo(rs.getString("pm_route_no"));
					issuePermitDTO.setPermitNo(rs.getString("pm_permit_no"));
					issuePermitDTO.setQueueNo(rs.getString("pm_queue_no"));
					issuePermitDTO.setApplicationNo(rs.getString("pm_application_no"));
					issuePermitDTO.setBusRegNo(rs.getString("pm_vehicle_regno"));
					issuePermitDTO.setServiceType(rs.getString("description"));
					issuePermitDTO.setTenderRefNo(rs.getString("pm_tender_ref_no"));

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return issuePermitDTO;

	}

	/**
	 * @param strApplicationNo condition: isBacklogApp = "N"
	 * @return IssuePermitDTO
	 *         (routeFlag,permitNo,tenderReferanceNo,applicationNo,queueNo,serviceType,busRegistrationNo)
	 */
	@Override
	public IssuePermitDTO getApplicationDetailsByVehicleNo(String strBusRegNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		IssuePermitDTO issuePermitDTO = new IssuePermitDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT A.pm_route_flag AS pm_route_flag,A.pm_permit_no AS pm_permit_no,A.pm_queue_no AS pm_queue_no, A.pm_application_no AS pm_application_no,A.pm_vehicle_regno AS pm_vehicle_regno,B.description AS description,A.pm_tender_ref_no AS pm_tender_ref_no"
					+ " FROM public.nt_t_pm_application A INNER JOIN nt_r_service_types B ON A.pm_service_type=B.code"
					+ " WHERE pm_vehicle_regno =  ? AND pm_is_backlog_app=?";

			ps = con.prepareStatement(query);
			ps.setString(1, strBusRegNo);
			ps.setString(2, "N");
			rs = ps.executeQuery();

			while (rs.next()) {

				issuePermitDTO.setRouteFlag(rs.getString("pm_route_flag"));
				issuePermitDTO.setPermitNo(rs.getString("pm_permit_no"));
				issuePermitDTO.setQueueNo(rs.getString("pm_queue_no"));
				issuePermitDTO.setApplicationNo(rs.getString("pm_application_no"));
				issuePermitDTO.setBusRegNo(rs.getString("pm_vehicle_regno"));
				issuePermitDTO.setServiceType(rs.getString("description"));
				issuePermitDTO.setTenderRefNo(rs.getString("pm_tender_ref_no"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return issuePermitDTO;

	}

	/**
	 * @param strQueueNo condition: permitNo NOT NULL And ( isBacklog != ('Y') or
	 *                   isBacklog != null) And createdDate = today
	 * @return IssuePermitDTO
	 *         (inspectionDate1,inspectionDate2,isNewPermit,applicationNo,busRegistrationNo,serviceType,tenderReferanceNo,queueNo)
	 */
	@Override
	public IssuePermitDTO getApplicationDetailsByQueueNo(String strQueueNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		IssuePermitDTO issuePermitDTO = new IssuePermitDTO();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		String dateStr = formatter.format(today);
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT B.pm_next_ins_date_sec1_2,B.pm_next_ins_date_sec3,B.pm_isnew_permit,A.que_application_no,A.que_vehicle_no,C.description,A.tender_ref_no,A.que_number"
					+ " FROM public.nt_m_queue_master A INNER JOIN public.nt_t_pm_application B ON A.que_application_no=B.pm_application_no "
					+ "INNER JOIN nt_r_service_types C ON B.pm_service_type=C.code"
					+ " WHERE A.que_number =  ? AND B.pm_permit_no IS NULL AND A.que_permit_no IS NULL AND (B.pm_is_backlog_app not in('Y') or B.pm_is_backlog_app is null) "
					+ " and to_char(A.created_date,'yyyy-MM-dd')=?";

			ps = con.prepareStatement(query);
			ps.setString(1, strQueueNo);
			ps.setString(2, dateStr);
			rs = ps.executeQuery();

			while (rs.next()) {
				issuePermitDTO.setInspectionDate1(rs.getString("pm_next_ins_date_sec1_2"));
				issuePermitDTO.setInspectionDate2(rs.getString("pm_next_ins_date_sec3"));
				issuePermitDTO.setIsNewPermit(rs.getString("pm_isnew_permit"));
				issuePermitDTO.setApplicationNo(rs.getString("que_application_no"));
				issuePermitDTO.setBusRegNo(rs.getString("que_vehicle_no"));
				issuePermitDTO.setServiceType(rs.getString("description"));
				issuePermitDTO.setTenderRefNo(rs.getString("tender_ref_no"));
				issuePermitDTO.setQueueNo(rs.getString("que_number"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return issuePermitDTO;

	}

	// Save Bus Owner Details
	public String saveBusOwnerDetails(BusOwnerDTO busOwnerDTO) {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		String permitNo;

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();

			permitNo = generateTmpPermitNo();

			String sql = "UPDATE public.nt_t_pm_vehi_owner "
					+ " SET pmo_preferred_language =?, pmo_title =?, pmo_full_name=?, pmo_full_name_sinhala =?, "
					+ " pmo_full_name_tamil=?, pmo_name_with_initial=?, pmo_nic=?, pmo_gender=?, pmo_dob =?, "
					+ " pmo_marital_status=?, pmo_telephone_no=?, pmo_mobile_no=?, pmo_province=?, pmo_district=?, "
					+ " pmo_div_sec=?, pmo_address1=?, pmo_address1_sinhala=?, pmo_address1_tamil=?, pmo_address2=?, "
					+ " pmo_address2_sinhala=?, pmo_address2_tamil=?, pmo_address3=?, pmo_address3_sinhala=?, "
					+ " pmo_address3_tamil=?, pmo_city=?, pmo_city_sinhala=?, pmo_city_tamil=?, pm_modified_by=?,pm_modified_date=?,pmo_permit_no=? "
					+ " WHERE pmo_application_no= ?;";

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
			stmt.setString(30, permitNo);
			stmt.setString(31, busOwnerDTO.getApplicationNo());

			stmt.executeUpdate();

			con.commit();

			updateApplicationPermitDetails(busOwnerDTO.getPermitPeroid(), permitNo, busOwnerDTO.getApplicationNo(),
					busOwnerDTO.getCreatedBy());

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return permitNo;
	}

	public int updateApplicationPermitDetails(int expirePeriod, String permitNo, String applicationNo, String user) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		String validityYears = null;
		try {
			validityYears = PropertyReader.getPropertyValue("newPermit.validityYears");
		} catch (ApplicationException e) {

			e.printStackTrace();
		}

		String today = dateFormat.format(date);

		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);

		calendar.add(Calendar.YEAR, Integer.parseInt(validityYears));

		String futureDate = dateFormat.format(calendar.getTime());

		calendar.setTime(date);

		calendar.add(Calendar.MONTH, expirePeriod);

		String expireDate = dateFormat.format(calendar.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_pm_application "
					+ " SET pm_temp_permit_no=?,pm_permit_issue_date =?, pm_permit_issue_by =?, pm_valid_from=?, pm_valid_to =?, "
					+ " pm_permit_expire_date=?, pm_modified_by=?,pm_modified_date=?,pm_isnew_permit=? "
					+ " WHERE pm_application_no= ?;";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, permitNo);
			stmt.setString(2, today);
			stmt.setString(3, user);
			stmt.setString(4, today);
			stmt.setString(5, futureDate);
			stmt.setString(6, expireDate);
			stmt.setString(7, user);
			stmt.setTimestamp(8, timestamp);
			stmt.setString(9, "Y");
			stmt.setString(10, applicationNo);

			stmt.executeUpdate();
			con.commit();

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

	// Owner Details Search By Permit No
	public BusOwnerDTO ownerDetailsByPermitNo(String permitNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		BusOwnerDTO busOwnerDTO = new BusOwnerDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT seq,pmo_preferred_language,pmo_title,pmo_gender,pmo_dob,pmo_nic,pmo_full_name,"
					+ " pmo_full_name_sinhala, pmo_full_name_tamil,pmo_name_with_initial,pmo_marital_status,"
					+ " pmo_telephone_no,pmo_mobile_no,pmo_address1,pmo_address1_sinhala,pmo_address1_tamil,"
					+ " pmo_address2,pmo_address2_sinhala,pmo_address2_tamil,pmo_city,pmo_city_sinhala,"
					+ " pmo_city_tamil,pmo_province,pmo_district,pmo_div_sec  " + " FROM public.nt_t_pm_vehi_owner "
					+ " WHERE pmo_permit_no =  ? ";

			ps = con.prepareStatement(query);
			ps.setString(1, permitNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				busOwnerDTO.setSeq(rs.getLong("seq"));
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

				busOwnerDTO.setNicNo(rs.getString("pmo_nic"));
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

	// Update Bus Owner Details
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

	// Check duplicate applications
	public String checkDuplicateApplicationNo(String applicationNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT pmo_permit_no " + " FROM public.nt_t_pm_vehi_owner "
					+ " WHERE pmo_application_no = ? ";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, applicationNo);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("pmo_permit_no");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return result;
	}

	@Override
	public String generatePermitNo() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;

		int year = Year.now().getValue();
		String currYear = Integer.toString(year).substring(2, 4);
		String strPermitNo = "";
		String lastyr = "00";

		try {
			con = ConnectionManager.getConnection();

			String sql = "select pm_permit_no  from nt_t_pm_application "
					+ "where pm_permit_no like'NTC%' AND pm_permit_no is not null order by pm_permit_no desc limit 1";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("pm_permit_no");
			}

			if (result != null) {

				lastyr = result.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String PermitrecordcountN = "00001";
					strPermitNo = "NTC" + currYear + PermitrecordcountN;
				} else {
					String number = result.substring(5, 10);
					int returncountvalue = Integer.valueOf(number) + 1;

					String PermitrecordcountN = "";

					if (returncountvalue >= 10 && returncountvalue < 100) {
						PermitrecordcountN = "000" + returncountvalue;
					} else if (returncountvalue >= 100 && returncountvalue < 1000) {
						PermitrecordcountN = "00" + returncountvalue;
					} else if (returncountvalue >= 1000 && returncountvalue < 10000) {
						PermitrecordcountN = "0" + returncountvalue;
					} else if (returncountvalue >= 10000 && returncountvalue < 100000) {
						PermitrecordcountN = Integer.toString(returncountvalue);
					} else {
						PermitrecordcountN = "0000" + returncountvalue;
					}
					strPermitNo = "NTC" + currYear + PermitrecordcountN;
				}
			} else
				strPermitNo = "NTC" + currYear + "00001";

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return strPermitNo;
	}

	public String generatePermitNoNew(String applicationNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		String result = null;
		String result2 = null;
		String strPermitNo = null;
		int permitSeq = 0;

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT pm_is_tender_permit FROM nt_t_pm_application WHERE pm_application_no= ? LIMIT 1;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, applicationNo);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("pm_is_tender_permit");
			}
			if (result != null) {
				String sql2 = "SELECT number_value FROM nt_r_parameters WHERE param_name= 'Permit_Seq';";
				stmt1 = con.prepareStatement(sql2);
				rs1 = stmt1.executeQuery();

				while (rs1.next()) {
					result2 = rs1.getString("number_value");
					permitSeq = Integer.valueOf(result2);
				}

				if (result2 != null && result.equalsIgnoreCase("Y")) {
					String strTender = "TF";
					int newpermitSeq = (permitSeq + 1);
					strPermitNo = strTender + newpermitSeq;
					updatePermitNoSeq(newpermitSeq);
				} else if (result2 != null && result.equalsIgnoreCase("N")) {
					String strFormalization = "F";
					int newpermitSeq = (permitSeq + 1);
					strPermitNo = strFormalization + newpermitSeq;
					updatePermitNoSeq(newpermitSeq);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(rs1);
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt1);
			ConnectionManager.close(con);
		}

		return strPermitNo;
	}

	public void updatePermitNoSeq(int permitSeq) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String sql3 = "UPDATE public.nt_r_parameters SET number_value=? WHERE param_name= 'Permit_Seq' ";

			ps = con.prepareStatement(sql3);

			ps.setInt(1, permitSeq);
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

	public String generateTmpPermitNo() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;

		int year = Year.now().getValue();
		String currYear = Integer.toString(year).substring(2, 4);
		String strTmpPermitNo = "";
		String lastyr = "00";

		try {
			con = ConnectionManager.getConnection();

			String sql = "select pm_temp_permit_no  from nt_t_pm_application "
					+ "where pm_is_backlog_app ='N' AND pm_temp_permit_no like'TP%' AND pm_temp_permit_no is not null order by pm_temp_permit_no desc limit 1";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("pm_temp_permit_no");
			}

			if (result != null) {

				lastyr = result.substring(2, 4);
				if (!lastyr.equals(currYear)) {
					String PermitrecordcountN = "00001";
					strTmpPermitNo = "TP" + currYear + PermitrecordcountN;
				} else {
					String number = result.substring(4, 9);
					int returncountvalue = Integer.valueOf(number) + 1;

					String PermitrecordcountN = "";

					if (returncountvalue >= 10 && returncountvalue < 100) {
						PermitrecordcountN = "000" + returncountvalue;
					} else if (returncountvalue >= 100 && returncountvalue < 1000) {
						PermitrecordcountN = "00" + returncountvalue;
					} else if (returncountvalue >= 1000 && returncountvalue < 10000) {
						PermitrecordcountN = "0" + returncountvalue;
					} else if (returncountvalue >= 10000 && returncountvalue < 100000) {
						PermitrecordcountN = Integer.toString(returncountvalue);
					} else {
						PermitrecordcountN = "0000" + returncountvalue;
					}
					strTmpPermitNo = "TP" + currYear + PermitrecordcountN;
				}
			} else
				strTmpPermitNo = "TP" + currYear + "00001";

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return strTmpPermitNo;
	}

	// Omnibus Search By Permit No
	public OminiBusDTO ominiBusByVehicleNo(String vehicleNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		OminiBusDTO ominiBusDTO = new OminiBusDTO();
		try {
			con = ConnectionManager.getConnection();
			String query =

					"SELECT seq, pmb_application_no, pmb_vehicle_regno,"
							+ "pmb_seating_capacity, pmb_no_of_doors, pmb_height,"
							+ "pmb_production_date, pmb_weight, pmb_serial_no, pmb_certificate_date, pmb_garage_reg_no,"
							+ "pmb_insurance_company, pmb_insurance_cat, pmb_insurance_expire_date, pmb_bus_price,"
							+ "pmb_is_loan_obtain, pmb_finance_company, pmb_bank_loan_amt, pmb_due_amt, pmb_date_obtain,"
							+ "pmb_lapsed_installment, pmb_revenue_license_exp_date, pmb_first_reg_date,"
							+ "pmb_permit_no,pmb_garage_name,pmb_policy_no,pmb_emission_test_exp_date "
							+ " FROM public.nt_t_pm_omini_bus1 WHERE pmb_vehicle_regno =  ? order by seq desc limit 1 ; ";

			ps = con.prepareStatement(query);
			ps.setString(1, vehicleNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				ominiBusDTO.setSeq(rs.getLong("seq"));
				ominiBusDTO.setApplicationNo(rs.getString("pmb_application_no"));
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
				ominiBusDTO.setGarageName(rs.getString("pmb_garage_reg_no"));
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

	// Update Omnibus Details
	@Override
	public int updateNewPermitOminiBus(OminiBusDTO ominiBusDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		PreparedStatement stmt3 = null;
		PreparedStatement stmt4 = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();
			
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
			stmt.setString(26, ominiBusDTO.getGarageName());
			stmt.setString(27, ominiBusDTO.getPolicyNo());
			if (ominiBusDTO.getEmissionExpDate() != null) {
				String getEmissionExpDateVal = (dateFormat.format(ominiBusDTO.getEmissionExpDate()));
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
			
			
			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(stmt3);
			ConnectionManager.close(stmt4);
			ConnectionManager.close(con);
		}

		return 0;
	}

	// Save Backlog Omnibus Details
	@Override
	public int saveNewPermitOminiBus(OminiBusDTO ominiBusDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_pm_omini_bus1" + " SET pmb_application_no=?, pmb_vehicle_regno=?,"
					+ "pmb_seating_capacity=?, pmb_no_of_doors=?,"
					+ "pmb_height=?, pmb_production_date=?, pmb_weight=?,"
					+ "pmb_serial_no=?, pmb_certificate_date=?, pmb_garage_reg_no=?, "
					+ "pmb_insurance_company=?, pmb_insurance_cat=?, pmb_insurance_expire_date=?, "
					+ "pmb_bus_price=?, pmb_is_loan_obtain=?, pmb_finance_company=?,"
					+ "pmb_bank_loan_amt=?, pmb_due_amt=?, pmb_date_obtain=?,"
					+ "pmb_lapsed_installment=?, pmb_revenue_license_exp_date=?,"
					+ "pmb_first_reg_date=?, pmb_is_backlog_app=?,"
					+ " pmb_modified_by=?, pmb_modified_date=?,pmb_permit_no=?,pmb_garage_name=?,pmb_policy_no=? "
					+ " WHERE pmb_application_no= ?";

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
			stmt.setString(27, ominiBusDTO.getGarageName());
			stmt.setString(28, ominiBusDTO.getPolicyNo());
			stmt.setString(29, ominiBusDTO.getApplicationNo());

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

	// Get Route details By RouteNo
	public RouteDTO getDetailsbyRouteNo(String routeNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		RouteDTO routeDTO = new RouteDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT rou_service_origine,rou_service_destination,rou_via,rou_distance"
					+ " FROM public.nt_r_route " + " WHERE rou_number =  ? ";

			ps = con.prepareStatement(query);
			ps.setString(1, routeNo);
			rs = ps.executeQuery();

			while (rs.next()) {

				routeDTO.setOrigin(rs.getString("rou_service_origine"));
				routeDTO.setDestination(rs.getString("rou_service_destination"));
				routeDTO.setVia(rs.getString("rou_via"));
				routeDTO.setDistance(rs.getBigDecimal("rou_distance"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return routeDTO;

	}

	// Get Routes
	public List<CommonDTO> getRoutesToDropdown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT rou_number,rou_description " + " FROM public.nt_r_route "
					+ " WHERE active in('A','T') " + " ORDER BY rou_number";

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
	public int updateService(IssuePermitDTO issuePermitDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_pm_service" + " SET ser_application_no=?, ser_vehicle_regno=?,"
					+ "ser_routeno=?, ser_service_div_sec=?,"
					+ "ser_route_length=?, ser_no_of_trips=?, ser_parking_place=?,"
					+ "ser_parking_div_sec=?, ser_parking_des_length=?, ser_modified_by=?, " + "ser_modified_date=?"
					+ " WHERE seq= ?";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, issuePermitDTO.getApplicationNo());
			stmt.setString(2, issuePermitDTO.getBusRegNo());
			stmt.setString(3, issuePermitDTO.getRouteNo());
			stmt.setString(4, issuePermitDTO.getRouteDivSec());
			stmt.setBigDecimal(5, issuePermitDTO.getDistance());
			stmt.setInt(6, issuePermitDTO.getTrips());
			stmt.setString(7, issuePermitDTO.getParkingPlace());
			stmt.setString(8, issuePermitDTO.getParkingDivSec());
			stmt.setBigDecimal(9, issuePermitDTO.getDistanceParkingOrigin());
			stmt.setString(10, issuePermitDTO.getModifiedBy());
			stmt.setTimestamp(11, timestamp);
			stmt.setLong(12, issuePermitDTO.getServiceSeq());

			stmt.executeUpdate();
			con.commit();

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
	public int saveService(IssuePermitDTO issuePermitDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_pm_service");

			String sql = "INSERT INTO public.nt_t_pm_service" + "(ser_application_no, ser_vehicle_regno,ser_routeno,"
					+ " ser_service_div_sec, ser_route_length, ser_no_of_trips, ser_parking_place,"
					+ " ser_parking_div_sec, ser_parking_des_length, ser_created_by,"
					+ " ser_pmb_created_date,ser_permit_no,seq)" + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, issuePermitDTO.getApplicationNo());
			stmt.setString(2, issuePermitDTO.getBusRegNo());
			stmt.setString(3, issuePermitDTO.getRouteNo());
			stmt.setString(4, issuePermitDTO.getRouteDivSec());
			stmt.setBigDecimal(5, issuePermitDTO.getDistance());
			stmt.setInt(6, issuePermitDTO.getTrips());
			stmt.setString(7, issuePermitDTO.getParkingPlace());
			stmt.setString(8, issuePermitDTO.getParkingDivSec());
			stmt.setBigDecimal(9, issuePermitDTO.getDistanceParkingOrigin());
			stmt.setString(10, issuePermitDTO.getCreatedBy());
			stmt.setTimestamp(11, timestamp);
			stmt.setString(12, issuePermitDTO.getPermitNo());
			stmt.setLong(13, seqNo);

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
	public IssuePermitDTO serviceDetailsByPermitNo(String permitNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		IssuePermitDTO issuePermitDTO = new IssuePermitDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT b.pm_route_flag,a.seq,a.ser_application_no,a.ser_vehicle_regno,a.ser_routeno,a.ser_service_div_sec,a.ser_route_length,a.ser_no_of_trips,"
					+ " a.ser_parking_place, a.ser_parking_div_sec,a.ser_parking_des_length,a.ser_permit_no"
					+ " FROM public.nt_t_pm_service a INNER JOIN nt_t_pm_application b ON a.ser_application_no = b.pm_application_no "
					+ " WHERE a.ser_permit_no =  ? ";

			ps = con.prepareStatement(query);
			ps.setString(1, permitNo);
			rs = ps.executeQuery();

			while (rs.next()) {

				issuePermitDTO.setRouteFlag(rs.getString("pm_route_flag"));
				issuePermitDTO.setServiceSeq(rs.getLong("seq"));
				issuePermitDTO.setApplicationNo(rs.getString("ser_application_no"));
				issuePermitDTO.setBusRegNo(rs.getString("ser_vehicle_regno"));
				issuePermitDTO.setRouteNo(rs.getString("ser_routeno"));
				issuePermitDTO.setRouteDivSec(rs.getString("ser_service_div_sec"));
				issuePermitDTO.setDistance(rs.getBigDecimal("ser_route_length"));
				issuePermitDTO.setTrips(rs.getInt("ser_no_of_trips"));
				issuePermitDTO.setParkingPlace(rs.getString("ser_parking_place"));
				issuePermitDTO.setParkingDivSec(rs.getString("ser_parking_div_sec"));
				issuePermitDTO.setDistanceParkingOrigin(rs.getBigDecimal("ser_parking_des_length"));
				issuePermitDTO.setPermitNo(rs.getString("ser_permit_no"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return issuePermitDTO;

	}

	@Override
	public List<PermitRenewalsDTO> getAllRecordsForDocumentsCheckings() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PermitRenewalsDTO> returnList = new ArrayList<PermitRenewalsDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select doc_code, doc_document_des FROM public.nt_m_document where doc_transaction_type='03' ORDER BY doc_code ASC;";

			ps = con.prepareStatement(query);
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

	@Override
	public boolean checkIsSumbiited(String currentDocCode, String applicationNo, String permitNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isDocCodeHasRecord = false;
		PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT seqno FROM public.nt_t_application_document where apd_application_no=? and apd_permit_no=? and apd_transaction_type='03' and apd_doc_code=?;";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, applicationNo);
			stmt.setString(2, permitNo);
			stmt.setString(3, currentDocCode);
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
	public PermitRenewalsDTO getRemarkDetails(String currentDocCode, String applicationNo, String permitNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT seqno, apd_file_path, apd_remark FROM public.nt_t_application_document where apd_application_no=? and apd_permit_no=? and apd_transaction_type='03' and apd_doc_code=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, applicationNo);
			ps.setString(2, permitNo);
			ps.setString(3, currentDocCode);
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

	@Override
	public int updateDocumentRemark(Long currentRecordSeqNo, String currentUploadFilePath, String modifyBy,
			String currentRemark) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		try {
			con = ConnectionManager.getConnection();
			String sql = "UPDATE public.nt_t_application_document SET  apd_modified_by=?, apd_modified_date=?, apd_remark=? where seqno=? ;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, modifyBy);
			stmt.setTimestamp(2, timestamp);
			stmt.setString(3, currentRemark);
			stmt.setLong(4, currentRecordSeqNo);

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

	/*
	 * This method check for application is incomplete or not. Check permit no is
	 * empty and isBacklogApp equals "N" in nt_t_pm_application table
	 * 
	 * @param applicationNo
	 * 
	 * @return String false : If application is complete. Otherwise return permit no
	 */
	public String checkForIncompleteApplication(String applicationNo) {

		String permitNo = "false";

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionManager.getConnection();

			// If task status is true
			if (taskStatusIs(con, applicationNo).equalsIgnoreCase("true")) {

				String sql1 = "select pm_temp_permit_no ,pm_is_backlog_app from nt_t_pm_application where pm_application_no = ? AND pm_temp_permit_no IS NOT NULL AND pm_is_backlog_app = 'N' ";
				ps = con.prepareStatement(sql1);
				ps.setString(1, applicationNo);
				rs = ps.executeQuery();

				boolean value = false;
				while (rs.next()) {
					value = true;
					// If permit no is empty and isBacklogApp equals "N"

					if (!rs.getString("pm_temp_permit_no").isEmpty()) {
						permitNo = rs.getString("pm_temp_permit_no");

					} else if (rs.getString("pm_temp_permit_no") == null) {
						permitNo = "false";
					}

				}

				if (!value) {
					permitNo = "false";
				}

				// If task status is false
			} else if (taskStatusIs(con, applicationNo).equalsIgnoreCase("false")) {
				permitNo = "completed";
			} else if (taskStatusIs(con, applicationNo).equalsIgnoreCase("before")) {
				permitNo = "before";
			}

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return permitNo;
	}

	/**
	 * This is a support method for checkForIncompleteApplication method. validate
	 * task status : Check task status code = PM200 for related application number
	 * 
	 * @param con Current connection
	 * @applicationNo Application number in nt_t_task_det table
	 * @return boolean True : If application is incomplete
	 */

	public String taskStatusIs(Connection con, String applicationNo) {
		// true : pm200 ongoing
		// complete : complete

		ResultSet rs = null;
		PreparedStatement ps = null;
		String taskStatus = "invalid";
		String taskStatusCode = "00000";
		String isTrue = "false";

		try {
			String sql = "SELECT tsd_task_code , tsd_status FROM nt_t_task_det WHERE tsd_app_no = ? LIMIT 1";

			ps = con.prepareStatement(sql);
			ps.setString(1, applicationNo);
			rs = ps.executeQuery();
			boolean nullStatus = true;
			while (rs.next()) {
				taskStatusCode = rs.getString("tsd_task_code");
				taskStatus = rs.getString("tsd_status");
				nullStatus = false;
			}

			if (taskStatusCode.equalsIgnoreCase("PM200")) {
				isTrue = (taskStatus.equalsIgnoreCase("O") || taskStatus.equalsIgnoreCase("C")) ? "true" : "false";
			} else if (Integer.parseInt(taskStatusCode.substring(2, 5)) > 201) {
				isTrue = "complete";
			} else if (Integer.parseInt(taskStatusCode.substring(2, 5)) < 200) {
				isTrue = "before";
			} else {
				isTrue = "invalid";
			}
			if (nullStatus) {
				isTrue = "invalid";
			}
			if (taskStatusCode.equalsIgnoreCase("PM201")) {
				isTrue = (taskStatus.equalsIgnoreCase("C")) ? "false" : "true";
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
		}

		return isTrue;
	}

	/**
	 * This method check for which tab completed
	 * 
	 */
	public boolean getCompletedTab(String tabIndex, String applicationNo) {
		boolean completed = false;

		switch (tabIndex) {
		case "one":
			completed = IsTabOneCompleted(applicationNo);
			break;
		case "two":
			completed = IsTabTwoCompleted(applicationNo);
			break;
		case "three":
			completed = IsTabThreeCompleted(applicationNo);
			break;
		default:
			break;
		}

		return completed;
	}

	/**
	 * This method check tab one already inserted or not. check Application number
	 * is null in nt_t_pm_vehi_owner table
	 * 
	 * @param applicationNo application no
	 * @return True IF Application number is null
	 */
	public boolean IsTabOneCompleted(String applicationNo) {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean completed = false;

		try {
			con = ConnectionManager.getConnection();

			// If task status is true
			String sql1 = "SELECT pmo_application_no FROM nt_t_pm_vehi_owner WHERE pmo_application_no = ?";
			ps = con.prepareStatement(sql1);
			ps.setString(1, applicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				// If Application number is empty
				if (!rs.getString("pmo_application_no").isEmpty()) {
					completed = true;
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return completed;
	}

	/**
	 * This method check tab two already inserted or not. check Application number
	 * and permit number is null in public.nt_t_pm_omini_bus1 table
	 * 
	 * @param applicationNo application no
	 * @return True IF Application number and Permit number is null
	 */
	public boolean IsTabTwoCompleted(String applicationNo) {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean inCompleted = false;

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT pmb_application_no ,  pmb_permit_no FROM nt_t_pm_omini_bus1 WHERE pmb_application_no = ? AND pmb_permit_no IS NOT NULL";
			ps = con.prepareStatement(sql);
			ps.setString(1, applicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {

				inCompleted = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return inCompleted;
	}

	/**
	 * This method check tab two already inserted or not. check Application number
	 * and permit number is null in nt_t_pm_service table
	 * 
	 * @param applicationNo application no
	 * @return True IF Application number and Permit number is null
	 */
	public boolean IsTabThreeCompleted(String applicationNo) {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean inCompleted = false;

		try {
			con = ConnectionManager.getConnection();

			String sql1 = "SELECT ser_application_no , ser_permit_no FROM nt_t_pm_service WHERE ser_application_no = ? AND ser_permit_no IS NOT NULL";
			ps = con.prepareStatement(sql1);
			ps.setString(1, applicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				// If application no is empty and permit number are not null
				inCompleted = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return inCompleted;
	}

	/**
	 * This method check tab two already inserted or not. check Application number
	 * and permit number is null in nt_t_pm_service table
	 * 
	 * @param applicationNo application no
	 * @return True IF Application number and Permit number is null
	 */
	public String checkForApplicationNo(String applicationNo) {

		// hasValue : true => application no
		// hasValue : invalid => invalid application
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String hasValue = "invalid";

		try {
			con = ConnectionManager.getConnection();

			String sql1 = "SELECT pm_application_no FROM nt_t_pm_application WHERE pm_application_no = ? AND pm_application_no IS NOT NULL  ";
			ps = con.prepareStatement(sql1);
			ps.setString(1, applicationNo);
			rs = ps.executeQuery();
			boolean getAppNo = false;

			while (rs.next()) {
				hasValue = rs.getString("pm_application_no");
				getAppNo = true;
			}

			if (getAppNo && taskStatusIs(con, applicationNo).equals("invalid")) {
				hasValue = "false";
			}

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return hasValue;
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

	/**
	 * This method return the application number related to the bus registration
	 * number in the application table
	 * 
	 * @param vehicleNo Bus registration number
	 * @return application number
	 */
	public String getApplicationNoByVehicleNo(String vehicleNo) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String appNo = "invalid";

		try {
			con = ConnectionManager.getConnection();

			String sql1 = "SELECT pm_application_no FROM nt_t_pm_application WHERE pm_vehicle_regno = ? AND pm_status != ? AND pm_application_no IS NOT NULL  ";
			ps = con.prepareStatement(sql1);
			ps.setString(1, vehicleNo);
			ps.setString(2, "I");
			rs = ps.executeQuery();

			while (rs.next()) {
				appNo = rs.getString("pm_application_no");
			}

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return appNo;
	}

	@Override
	public IssuePermitDTO getSelectedValuesForUploadPhotos(String applicationNo, String permitNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		IssuePermitDTO issuePermitDTO = new IssuePermitDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  pmo_vehicle_regno, pmo_full_name FROM public.nt_t_pm_vehi_owner where pmo_application_no=? or pmo_permit_no=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, applicationNo);
			ps.setString(2, permitNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				issuePermitDTO.setBusRegNo(rs.getString("pmo_vehicle_regno"));
				issuePermitDTO.setVehicleOwner(rs.getString("pmo_full_name"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return issuePermitDTO;
	}

	@Override
	public String checkPermitNoFromQueueNo(String queueNo) {
		String permitNo = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String today = dateFormat.format(timestamp);

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT A.que_application_no,A.que_number,B.pm_permit_no,A.created_date "
					+ "FROM public.nt_m_queue_master A "
					+ "INNER JOIN public.nt_t_pm_application B ON A.que_application_no=B.pm_application_no "
					+ "WHERE A.que_number =  '" + queueNo + "' " + "AND to_char(A.created_date,'yyyy-MM-dd')='" + today
					+ "' ";

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
	public String checkTsakStatusPM101FromQueueNo(String queueNo) {
		String taskStatus = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String today = dateFormat.format(timestamp);

		try {
			con = ConnectionManager.getConnection();

			String query = "select a.que_application_no as que_application_no " + "from nt_m_queue_master a "
					+ "inner join nt_h_task_his b on b.tsd_app_no = a.que_application_no " + "where a.que_number = '"
					+ queueNo + "' " + "and b.tsd_task_code = 'PM101' " + "and b.tsd_status = 'O' "
					+ "and to_char(a.created_date,'yyyy-MM-dd')= '" + today + "'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				taskStatus = rs.getString("que_application_no");

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

	public String getTempPermit(String app) {
		String permitNo = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select pm_temp_permit_no FROM public.nt_t_pm_application where pm_application_no='" + app
					+ "' ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				permitNo = rs.getString("pm_temp_permit_no");

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

	/* For check Mandatory and Physically Exits Doc */
	@Override
	public boolean isMandatory(String currentDocCode, String applicationNo, String permitNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean ismandatory = false;

		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT  distinct * FROM public.nt_m_document "
					+ "where doc_transaction_type='03' and doc_code=? and doc_mandatory_doc='true'";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, currentDocCode);
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
					+ "where  dck_application_no=?  and dck_transaction_type='03' and dck_code=? and dck_physicaly_exist='true'; ";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, applicationNo);
			stmt.setString(2, currentDocCode);
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
			String permitNo, String logUser) {
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
					+ "where dck_transaction_type='03' and dck_code=? and dck_application_no=? "
					+ "and dck_physicaly_exist='true';";

			stmt2 = con.prepareStatement(selectSQL);

			stmt2.setString(1, currentDocCode);
			stmt2.setString(2, applicationNo);

			rs = stmt2.executeQuery();

			if (rs.next() == true) {

			} else {

				String sql = "INSERT INTO public.nt_m_document_check "
						+ "(seqno, dck_transaction_type, dck_code, dck_application_no, dck_permit_no, dck_physicaly_exist, dck_created_by, dck_created_date) "
						+ "VALUES(?, ?, ?,?, ?, ?, ?, ? );";

				long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_document_check");

				stmt = con.prepareStatement(sql);
				stmt.setLong(1, seqNo);
				stmt.setString(2, "03");
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
			ConnectionManager.close(stmt2);
			ConnectionManager.close(con);

		}

		return isPhysicallyExits;
	}

	@Override
	public boolean deleteDocumentPhysicallyExitsStatus(boolean exits, String currentDocCode, String applicationNo,
			String permitNo, String logUser) {
		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		boolean isDeletePhysicallyExits = false;

		try {
			con = ConnectionManager.getConnection();

			String selectSQL = "SELECT dck_transaction_type, dck_code, dck_application_no, dck_permit_no, dck_physicaly_exist "
					+ "FROM public.nt_m_document_check "
					+ "where dck_transaction_type='03' and dck_code=? and dck_application_no=? "
					+ "and dck_physicaly_exist='true';";

			stmt2 = con.prepareStatement(selectSQL);

			stmt2.setString(1, currentDocCode);
			stmt2.setString(2, applicationNo);

			rs = stmt2.executeQuery();

			if (rs.next() == false) {

			} else {

				String sql = "DELETE FROM public.nt_m_document_check "
						+ "WHERE dck_transaction_type='03' and dck_code=? and dck_application_no=? and dck_physicaly_exist='true'";

				stmt = con.prepareStatement(sql);

				stmt.setString(1, currentDocCode);
				stmt.setString(2, applicationNo);

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
	public RouteDTO getTempRouteDetails(String appNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		RouteDTO routeDTO = new RouteDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "select trr_origin,trr_destination,trr_via from public.nt_temp_route_request where trr_application_no = ? ";

			ps = con.prepareStatement(query);
			ps.setString(1, appNo);
			rs = ps.executeQuery();

			while (rs.next()) {

				routeDTO.setOrigin(rs.getString("trr_origin"));
				routeDTO.setDestination(rs.getString("trr_destination"));
				routeDTO.setVia(rs.getString("trr_via"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return routeDTO;

	}

}
