package lk.informatics.ntc.model.service;

import java.math.BigDecimal;
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

import org.apache.commons.lang.StringUtils;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.CommonInquiryDTO;
import lk.informatics.ntc.model.dto.ComplaintRequestDTO;
import lk.informatics.ntc.model.dto.DriverConductorBlacklistDTO;
import lk.informatics.ntc.model.dto.DriverConductorRegistrationDTO;
import lk.informatics.ntc.model.dto.DriverConductorTrainingDTO;
import lk.informatics.ntc.model.dto.DropDownDTO;
import lk.informatics.ntc.model.dto.MaintainTrainingScheduleDTO;
import lk.informatics.ntc.model.dto.PermitDTO;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.roc.utils.common.Utils;

public class DriverConductorTrainingServiceImpl implements DriverConductorTrainingService {

	@Override
	public List<CommonDTO> GetGenderToDropdown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description " + "FROM nt_r_gender " + "WHERE active = 'A' ";

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
	public List<CommonDTO> GetTrainingTypesByMode(String strMode) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description, points, type " + "FROM nt_r_training_type " + "WHERE mode =? ";

			ps = con.prepareStatement(query);
			ps.setString(1, strMode);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("code"));
				commonDTO.setDescription(rs.getString("description"));
				commonDTO.setPoints(rs.getInt("points"));
				commonDTO.setType(rs.getString("type"));

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

	public List<PermitDTO> getActivePermitList() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PermitDTO> returnList = new ArrayList<PermitDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct a.pm_application_no, a.pm_permit_no, a.pm_vehicle_regno,a.pm_route_no, b.rou_description,b.rou_service_origine,b.rou_service_destination"
					+ "  from nt_t_pm_application a, nt_r_route b"
					+ " where a.pm_route_no=b.rou_number and  a.pm_status='A' ";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				PermitDTO permitDTO = new PermitDTO();
				permitDTO.setApplicationNo(rs.getString("pm_application_no"));
				permitDTO.setPermitNo(rs.getString("pm_permit_no"));
				permitDTO.setBusRegNo(rs.getString("pm_vehicle_regno"));
				permitDTO.setRouteNo(rs.getString("pm_route_no"));
				permitDTO.setOrigin(rs.getString("rou_service_origine"));
				permitDTO.setDestination(rs.getString("rou_service_destination"));

				returnList.add(permitDTO);

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
	public boolean insertDriverConductorReg(DriverConductorRegistrationDTO dto) {
		boolean ret = false;

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_driver_conductor_registration");

			String appNo = generateAppNo(seqNo, dto.getCreatedBy());
			String driverCondId = generateDriverCondId(dto.getTrainingType().getType(), seqNo, dto.getCreatedBy());
			String dob = dateFormat.format(dto.getDob());
			String dateofissue = "";
			if (dto.getDateOfIssue() != null) {
				dateofissue = dateFormat.format(dto.getDateOfIssue());
			}
			String dateofexpiry = "";
			if (dto.getDateOfExpiry() != null) {
				dateofexpiry = dateFormat.format(dto.getDateOfExpiry());
			}

			String sql = "INSERT INTO public.nt_t_driver_conductor_registration("
					+ "dcr_seq, dcr_app_no, dcr_training_type, dcr_driver_conductor_id, "
					+ "dcr_nic, dcr_name_with_initials, dcr_full_name_eng, dcr_full_name_sin, "
					+ "dcr_full_name_tam, dcr_gender, dcr_dob, dcr_district, dcr_contact_no, "
					+ "dcr_license_no, dcr_date_of_issue, dcr_date_of_expiry, dcr_education, "
					+ "dcr_gs_certificate_no, dcr_police_clearance_station, dcr_vehicle_reg_no, "
					+ "dcr_permit_no, dcr_route_number, " + "dcr_status, dcr_special_remarks, "
					+ "dcr_add_1_eng, dcr_add_2_eng, dcr_city_eng, "
					+ "dcr_add_1_sin, dcr_add_2_sin, dcr_city_sin, dcr_add_1_tam, dcr_add_2_tam, "
					+ "dcr_city_tam, dcr_created_by, dcr_created_date, dcr_points) " + "VALUES (?, ?, ?, ?, "
					+ "?, ?, ?, ?, " + "?, ?, ?, ?, ?, " + "?, ?, ?, ?, " + "?, ?, ?, " + "?, ?, " + "?, ?, "
					+ "?, ?, ?, " + "?, ?, ?, ?, ?, " + "?, ?, ?, ?);";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, appNo);
			stmt.setString(3, dto.getTrainingType().getCode());
			stmt.setString(4, driverCondId);

			stmt.setString(5, dto.getNic());
			stmt.setString(6, dto.getNameWithInitials());
			stmt.setString(7, dto.getFullNameEng());
			stmt.setString(8, dto.getFullNameSin());

			stmt.setString(9, dto.getFullNameTam());
			stmt.setString(10, dto.getGender().getCode());
			stmt.setString(11, dob);
			stmt.setString(12, dto.getDistrict().getCode());
			stmt.setString(13, dto.getContactNo());

			stmt.setString(14, dto.getLicenseNo());
			stmt.setString(15, dateofissue);
			stmt.setString(16, dateofexpiry);
			stmt.setString(17, dto.getEducation());

			stmt.setString(18, dto.getVehicleRegNo());
			stmt.setString(19, dto.getGsCertificateNo());
			stmt.setString(20, dto.getPoliceClearanceStation());

			stmt.setString(21, dto.getPermitNo());
			stmt.setString(22, dto.getRouteNo());

			stmt.setString(23, "P");
			stmt.setString(24, dto.getSpecialRemarks());

			stmt.setString(25, dto.getAdd1Eng());
			stmt.setString(26, dto.getAdd2Eng());
			stmt.setString(27, dto.getCityEng());

			stmt.setString(28, dto.getAdd1Sin());
			stmt.setString(29, dto.getAdd2Sin());
			stmt.setString(30, dto.getCitySin());
			stmt.setString(31, dto.getAdd1Tam());
			stmt.setString(32, dto.getAdd2Tam());

			stmt.setString(33, dto.getCityTam());
			stmt.setString(34, dto.getCreatedBy());
			stmt.setTimestamp(35, timestamp);
			stmt.setInt(36, dto.getPoints());

			stmt.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
		return ret;
	}

	private String generateAppNo(long seqNo, String loginUser) {

		String last = retrieveLastNoForNumberGeneration("DCR");
		long no = Long.parseLong(last.substring(3));
		String next = String.valueOf((no + 1));
		String appNo = "DCR" + StringUtils.leftPad(next, 5, "0");

		updateNumberGeneration("DCR", appNo, loginUser);

		return appNo;
	}

	private String generateDriverCondId(String trainingtype, long seqNo, String loginUser) {

		String code = "";
		if (trainingtype.equalsIgnoreCase("D")) {
			code = "DRI";
		} else if (trainingtype.equalsIgnoreCase("C")) {
			code = "CON";
		}

		String last = retrieveLastNoForNumberGeneration(code);

		long no = Long.parseLong(last.substring(3));

		String next = String.valueOf((no + 1));

		String appNo = code + StringUtils.leftPad(next, 5, "0");

		updateNumberGeneration(code, appNo, loginUser);

		return appNo;
	}

	@Override
	public String retrieveLastNoForNumberGeneration(String code) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String appNo = null;

		try {
			con = ConnectionManager.getConnection();

			String sql1 = "SELECT app_no FROM public.nt_r_number_generation WHERE code=?";
			ps = con.prepareStatement(sql1);

			ps.setString(1, code);
			rs = ps.executeQuery();

			while (rs.next()) {
				appNo = rs.getString("app_no");

			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return appNo;
	}

	public void updateNumberGeneration(String code, String appNo, String loginUser) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql3 = "UPDATE public.nt_r_number_generation SET app_no=?,modified_by=?,modified_date=? WHERE code=? ";

			ps = con.prepareStatement(sql3);

			ps.setString(1, appNo);
			ps.setString(2, loginUser);
			ps.setTimestamp(3, timestamp);
			ps.setString(4, code);

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

	public DriverConductorRegistrationDTO insertDriverConductorRegNew(DriverConductorRegistrationDTO dto, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		DriverConductorRegistrationDTO driverConductorRegistrationDTO = new DriverConductorRegistrationDTO();

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_driver_conductor_registration");

			String appNo = generateAppNo(seqNo, dto.getCreatedBy());
			String driverCondId = generateDriverCondId(dto.getTrainingType().getType(), seqNo, dto.getCreatedBy());
			String dob = dateFormat.format(dto.getDob());

			String gsIssueDate = "";
			if (dto.getGscissuedate() != null) {
				gsIssueDate = dateFormat.format(dto.getGscissuedate());
			}
			String pcIssueDate = "";
			if (dto.getPcissuedate() != null) {
				pcIssueDate = dateFormat.format(dto.getPcissuedate());
			}
			String mediReIssueDate = "";
			if (dto.getMrissuedate() != null) {
				mediReIssueDate = dateFormat.format(dto.getMrissuedate());
			}
			String mediReportExpireDate = "";
			if (dto.getMrexpiredate() != null) {
				mediReportExpireDate = dateFormat.format(dto.getMrexpiredate());
			}

			String dateofissue = "";
			if (dto.getDateOfIssue() != null) {
				dateofissue = dateFormat.format(dto.getDateOfIssue());
			}
			String dateofexpiry = "";
			if (dto.getDateOfExpiry() != null) {
				dateofexpiry = dateFormat.format(dto.getDateOfExpiry());
			}

			String firstDateOfIssue = "";
			if (dto.getFirstDateOfIssue() != null) {
				firstDateOfIssue = dateFormat.format(dto.getFirstDateOfIssue());
			}
			String dateOfIssueLicence = "";
			if (dto.getDateOfIssueLicence() != null) {
				dateOfIssueLicence = dateFormat.format(dto.getDateOfIssueLicence());
			}
			String dateOfExpiryLicence = "";
			if (dto.getDateOfExpiryLicence() != null) {
				dateOfExpiryLicence = dateFormat.format(dto.getDateOfExpiryLicence());
			}

			String sql = "INSERT INTO public.nt_t_driver_conductor_registration("
					+ "dcr_seq, dcr_app_no, dcr_training_type, dcr_driver_conductor_id, "
					+ "dcr_nic, dcr_name_with_initials, dcr_full_name_eng, dcr_full_name_sin, "
					+ "dcr_full_name_tam, dcr_gender, dcr_dob, dcr_district, dcr_contact_no, "
					+ "dcr_license_no, dcr_date_of_issue, dcr_date_of_expiry, dcr_education, "
					+ "dcr_gs_certificate_no, dcr_police_clearance_station, dcr_vehicle_reg_no, "
					+ "dcr_permit_no, dcr_route_number, " + "dcr_status, dcr_special_remarks, "
					+ "dcr_add_1_eng, dcr_add_2_eng, dcr_city_eng, "
					+ "dcr_add_1_sin, dcr_add_2_sin, dcr_city_sin, dcr_add_1_tam, dcr_add_2_tam, "
					+ "dcr_city_tam, dcr_created_by, dcr_created_date, dcr_points,dcr_from,dcr_to,dcr_status_type, "
					+ "dcr_old_app_no, dcr_is_duplicate,dcr_gs_issue_date,dcr_police_cl_issue_date,dcr_medi_cer_no,dcr_medi_cer_issue_date,dcr_medi_cer_expire_date, "
					+ "dcr_training_language, dcr_first_issue_date, dcr_license_issue_date, dcr_license_expire_date, dcr_remarks ) "
					+ "VALUES (?, ?, ?, ?, " + "?, ?, ?, ?, " + "?, ?, ?, ?, ?, " + "?, ?, ?, ?, " + "?, ?, ?, "
					+ "?, ?, " + "?, ?, " + "?, ?, ?, " + "?, ?, ?, ?, ?, "
					+ "?, ?, ?, ?,?,?,?,?,?, ?, ?, ?, ?, ?,?,?,?,?,?);";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, appNo);
			stmt.setString(3, dto.getTrainingType().getCode());
			stmt.setString(4, driverCondId);

			stmt.setString(5, dto.getNic());
			stmt.setString(6, dto.getNameWithInitials());
			stmt.setString(7, dto.getFullNameEng());
			stmt.setString(8, dto.getFullNameSin());

			stmt.setString(9, dto.getFullNameTam());
			stmt.setString(10, dto.getGender().getCode());
			stmt.setString(11, dob);
			stmt.setString(12, dto.getDistrict().getCode());
			stmt.setString(13, dto.getContactNo());

			stmt.setString(14, dto.getLicenseNo());
			stmt.setString(15, dateofissue);
			stmt.setString(16, dateofexpiry);
			stmt.setString(17, dto.getEducation());

			stmt.setString(18, dto.getGsCertificateNo());
			stmt.setString(19, dto.getPoliceClearanceStation());
			stmt.setString(20, dto.getVehicleRegNo());

			stmt.setString(21, dto.getPermitNo());
			stmt.setString(22, dto.getRouteNo());

			stmt.setString(23, "P");
			stmt.setString(24, dto.getSpecialRemarks());

			stmt.setString(25, dto.getAdd1Eng());
			stmt.setString(26, dto.getAdd2Eng());
			stmt.setString(27, dto.getCityEng());

			stmt.setString(28, dto.getAdd1Sin());
			stmt.setString(29, dto.getAdd2Sin());
			stmt.setString(30, dto.getCitySin());
			stmt.setString(31, dto.getAdd1Tam());
			stmt.setString(32, dto.getAdd2Tam());

			stmt.setString(33, dto.getCityTam());
			stmt.setString(34, dto.getCreatedBy());
			stmt.setTimestamp(35, timestamp);
			stmt.setInt(36, dto.getPoints());
			stmt.setString(37, dto.getFrom());
			stmt.setString(38, dto.getTo());

			stmt.setString(39, "P");
			stmt.setString(40, dto.getOldApp());
			stmt.setString(41, dto.getIsDuplicate());

			stmt.setString(42, gsIssueDate);
			stmt.setString(43, pcIssueDate);
			stmt.setString(44, dto.getMedicalcertificateNo());
			stmt.setString(45, mediReIssueDate);
			stmt.setString(46, mediReportExpireDate);

			stmt.setString(47, dto.getTrainingLanguage());
			stmt.setString(48, firstDateOfIssue);
			stmt.setString(49, dateOfIssueLicence);
			stmt.setString(50, dateOfExpiryLicence);
			stmt.setString(51, dto.getRemarks());

			int i = stmt.executeUpdate();

			if (i > 0) {
				driverConductorRegistrationDTO.setAppNo(appNo);
				driverConductorRegistrationDTO.setDriverConductorId(driverCondId);

			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return driverConductorRegistrationDTO;
	}

	public boolean updateDriverConductorRegistration(DriverConductorRegistrationDTO driverConductorRegistrationDTO, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isUpdated = false;
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		java.util.Date date1 = new java.util.Date();
		Timestamp timestamp = new Timestamp(date1.getTime());
	
		try {
			con = ConnectionManager.getConnection();
			java.util.Date date = new java.util.Date();
			Timestamp timeStamp = new Timestamp(date.getTime());

			String dob = dateFormat.format(driverConductorRegistrationDTO.getDob());

			String gsIssueDate = "";
			if (driverConductorRegistrationDTO.getDateOfIssue() != null) {
				gsIssueDate = dateFormat.format(driverConductorRegistrationDTO.getGscissuedate());
			}
			String pcIssueDate = "";
			if (driverConductorRegistrationDTO.getPcissuedate() != null) {
				pcIssueDate = dateFormat.format(driverConductorRegistrationDTO.getPcissuedate());
			}
			String mediReIssueDate = "";
			if (driverConductorRegistrationDTO.getMrissuedate() != null) {
				mediReIssueDate = dateFormat.format(driverConductorRegistrationDTO.getMrissuedate());
			}
			String mediReportExpireDate = "";
			if (driverConductorRegistrationDTO.getMrexpiredate() != null) {
				mediReportExpireDate = dateFormat.format(driverConductorRegistrationDTO.getMrexpiredate());
			}

			String dateofissue = "";
			if (driverConductorRegistrationDTO.getDateOfIssue() != null) {
				dateofissue = dateFormat.format(driverConductorRegistrationDTO.getDateOfIssue());
			}
			String dateofexpiry = "";
			if (driverConductorRegistrationDTO.getDateOfExpiry() != null) {
				dateofexpiry = dateFormat.format(driverConductorRegistrationDTO.getDateOfExpiry());
			}

			String firstDateOfIssue = "";
			if (driverConductorRegistrationDTO.getFirstDateOfIssue() != null) {
				firstDateOfIssue = dateFormat.format(driverConductorRegistrationDTO.getFirstDateOfIssue());
			}
			String dateOfIssueLicence = "";
			if (driverConductorRegistrationDTO.getDateOfIssueLicence() != null) {
				dateOfIssueLicence = dateFormat.format(driverConductorRegistrationDTO.getDateOfIssueLicence());
			}
			String dateOfExpiryLicence = "";
			if (driverConductorRegistrationDTO.getDateOfExpiryLicence() != null) {
				dateOfExpiryLicence = dateFormat.format(driverConductorRegistrationDTO.getDateOfExpiryLicence());
			}

			String sql = "UPDATE public.nt_t_driver_conductor_registration "
					+ " SET  dcr_driver_conductor_id=?, dcr_nic=?, "
					+ " dcr_name_with_initials=?, dcr_full_name_eng=?, dcr_full_name_sin=?, dcr_full_name_tam=?, "
					+ " dcr_gender=?, dcr_dob=?, dcr_district=?, dcr_contact_no=?, dcr_license_no=?, dcr_date_of_issue=?,"
					+ " dcr_date_of_expiry=?, dcr_education=?, dcr_gs_certificate_no=?, dcr_police_clearance_station=?, "
					+ " dcr_vehicle_reg_no=?, dcr_permit_no=?, dcr_route_number=?, dcr_from=?, dcr_to=?, "
					+ " dcr_status=?, dcr_special_remarks=?, dcr_photo_uploaded=?, dcr_schedule_id=?, "
					+ " dcr_add_1_eng=?, dcr_add_2_eng=?, dcr_city_eng=?, dcr_add_1_sin=?, dcr_add_2_sin=?, "
					+ "	dcr_city_sin=?, dcr_add_1_tam=?, dcr_add_2_tam=?, dcr_city_tam=?, "
					+ " dcr_modified_date=?, dcr_modified_by=?, dcr_points=?,dcr_old_app_no=?, dcr_status_type=?, dcr_is_duplicate =? "
					+ " ,dcr_gs_issue_date=? ,dcr_police_cl_issue_date=? ,dcr_medi_cer_no=? ,dcr_medi_cer_issue_date=? ,dcr_medi_cer_expire_date=?, "
					+ " dcr_training_language=?, dcr_first_issue_date=?, dcr_license_issue_date=?, dcr_license_expire_date=?, dcr_remarks=? "
					+ " WHERE dcr_app_no=? ";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, driverConductorRegistrationDTO.getDriverConductorId());
			stmt.setString(2, driverConductorRegistrationDTO.getNic());
			stmt.setString(3, driverConductorRegistrationDTO.getNameWithInitials());
			stmt.setString(4, driverConductorRegistrationDTO.getFullNameEng());
			stmt.setString(5, driverConductorRegistrationDTO.getFullNameSin());
			stmt.setString(6, driverConductorRegistrationDTO.getFullNameTam());
			stmt.setString(7, driverConductorRegistrationDTO.getNewGender());
			stmt.setString(8, dob);
			stmt.setString(9, driverConductorRegistrationDTO.getDistrict().getCode());
			stmt.setString(10, driverConductorRegistrationDTO.getContactNo());
			stmt.setString(11, driverConductorRegistrationDTO.getLicenseNo());
			stmt.setString(12, dateofissue);
			stmt.setString(13, dateofexpiry);
			stmt.setString(14, driverConductorRegistrationDTO.getEducation());
			stmt.setString(15, driverConductorRegistrationDTO.getGsCertificateNo());
			stmt.setString(16, driverConductorRegistrationDTO.getPoliceClearanceStation());
			stmt.setString(17, driverConductorRegistrationDTO.getVehicleRegNo());
			stmt.setString(18, driverConductorRegistrationDTO.getPermitNo());
			stmt.setString(19, driverConductorRegistrationDTO.getRouteNo());
			stmt.setString(20, driverConductorRegistrationDTO.getFrom());
			stmt.setString(21, driverConductorRegistrationDTO.getTo());
			stmt.setString(22, driverConductorRegistrationDTO.getStatus());
			stmt.setString(23, driverConductorRegistrationDTO.getSpecialRemarks());
			stmt.setString(24, driverConductorRegistrationDTO.getPhotoUploaded());
			stmt.setLong(25, driverConductorRegistrationDTO.getScheduleId());
			stmt.setString(26, driverConductorRegistrationDTO.getAdd1Eng());
			stmt.setString(27, driverConductorRegistrationDTO.getAdd2Eng());
			stmt.setString(28, driverConductorRegistrationDTO.getCityEng());
			stmt.setString(29, driverConductorRegistrationDTO.getAdd1Sin());
			stmt.setString(30, driverConductorRegistrationDTO.getAdd2Sin());
			stmt.setString(31, driverConductorRegistrationDTO.getCitySin());
			stmt.setString(32, driverConductorRegistrationDTO.getAdd1Tam());
			stmt.setString(33, driverConductorRegistrationDTO.getAdd2Tam());
			stmt.setString(34, driverConductorRegistrationDTO.getCityTam());
			stmt.setTimestamp(35, timeStamp);
			stmt.setString(36, driverConductorRegistrationDTO.getModifiedBy());
			stmt.setInt(37, driverConductorRegistrationDTO.getPoints());
			stmt.setString(38, driverConductorRegistrationDTO.getOldApp());
			stmt.setString(39, driverConductorRegistrationDTO.getStatusType());
			stmt.setString(40, driverConductorRegistrationDTO.getIsDuplicate());

			stmt.setString(41, gsIssueDate);
			stmt.setString(42, pcIssueDate);
			stmt.setString(43, driverConductorRegistrationDTO.getMedicalcertificateNo());
			stmt.setString(44, mediReIssueDate);
			stmt.setString(45, mediReportExpireDate);

			stmt.setString(46, driverConductorRegistrationDTO.getTrainingLanguage());
			stmt.setString(47, firstDateOfIssue);
			stmt.setString(48, dateOfIssueLicence);
			stmt.setString(49, dateOfExpiryLicence);
			stmt.setString(50, driverConductorRegistrationDTO.getRemarks());

			stmt.setString(51, driverConductorRegistrationDTO.getAppNo());

			int i = stmt.executeUpdate();

			if (i > 0) {
				isUpdated = true;
			} else {
				isUpdated = false;
			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
		return isUpdated;

	}

	public List<CommonDTO> getVehicleDetails(String vehicleNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CommonDTO> data = new ArrayList<CommonDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct pm_permit_no, nt_r_route.rou_service_origine,"
					+ " nt_r_route.rou_service_destination,nt_t_pm_application.pm_route_no "
					+ " from nt_t_pm_application inner join nt_r_route on "
					+ " nt_t_pm_application.pm_route_no = nt_r_route.rou_number "
					+ " where nt_t_pm_application.pm_vehicle_regno = ?";

			ps = con.prepareStatement(query);
			ps.setString(1, vehicleNo);
			rs = ps.executeQuery();

			CommonDTO p;

			while (rs.next()) {
				p = new CommonDTO();
				p.setPermitNo(rs.getString("pm_permit_no"));
				p.setOrigin(rs.getString("rou_service_origine"));
				p.setDestination(rs.getString("rou_service_destination"));
				p.setRouteNo(rs.getString("pm_route_no"));
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

	public List<CommonDTO> GetAllTrainingTypesToDropdown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description, points, type " + "FROM nt_r_training_type "
					+ "WHERE status ='A' and code not in ('ND','NC')  ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("code"));
				commonDTO.setDescription(rs.getString("description"));
				commonDTO.setPoints(rs.getInt("points"));
				commonDTO.setType(rs.getString("type"));

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

	public String checkDuplicateNICforSameTraining(String nicNo, String traingType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String strName = null;

		try {
			con = ConnectionManager.getConnection();

			String sql1 = "SELECT dcr_full_name_eng " + " FROM nt_t_driver_conductor_registration "
					+ " WHERE dcr_training_type = ? " + " AND dcr_nic = ? ";
			ps = con.prepareStatement(sql1);

			ps.setString(1, traingType);
			ps.setString(2, nicNo);
			rs = ps.executeQuery();
			while (rs.next()) {
				strName = rs.getString("dcr_full_name_eng");
			}
			con.commit();
		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return strName;
	}

	/*********/
	// Edit View
	/*********/
	public List<CommonDTO> getPendingIDList() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT dcr_nic FROM nt_t_driver_conductor_registration WHERE dcr_status ='P' ";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("dcr_nic"));

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

	public List<CommonDTO> getPendingIDListByTrainingType(String trainingType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT dcr_nic FROM nt_t_driver_conductor_registration WHERE dcr_status ='P' AND dcr_training_type = ?  ";

			ps = con.prepareStatement(query);
			ps.setString(1, trainingType);

			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("dcr_nic"));

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

	public List<CommonDTO> getPendingDCIDList() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT dcr_driver_conductor_id FROM nt_t_driver_conductor_registration WHERE dcr_status ='P' ORDER BY dcr_driver_conductor_id ";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("dcr_driver_conductor_id"));

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

	public List<CommonDTO> getPendingDCIDListByTrainingType(String trainingType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT dcr_driver_conductor_id FROM nt_t_driver_conductor_registration WHERE dcr_status ='P' AND dcr_training_type = ? ORDER BY dcr_driver_conductor_id ";

			ps = con.prepareStatement(query);
			ps.setString(1, trainingType);

			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("dcr_driver_conductor_id"));

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

	public List<CommonDTO> getPendingDCIDListByTrainingandID(String trainingType, String idNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String WHERE_SQL = "";

		if (trainingType != null && !trainingType.equals("")) {
			WHERE_SQL = " AND dcr_training_type = " + "'" + trainingType + "'";
		}
		if (idNo != null && !idNo.equals("")) {
			WHERE_SQL = " AND dcr_nic = " + "'" + idNo + "'";
		}
		if (trainingType != null && !trainingType.equals("") && idNo != null && !idNo.equals("")) {
			WHERE_SQL = " AND dcr_training_type = " + "'" + trainingType + "'" + " AND dcr_nic = " + "'" + idNo + "'";
		}

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT dcr_driver_conductor_id FROM nt_t_driver_conductor_registration WHERE dcr_status ='P' "
					+ WHERE_SQL;

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("dcr_driver_conductor_id"));

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

	public DriverConductorRegistrationDTO getDetailsByDCId(String strDCId) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		DriverConductorRegistrationDTO driverConductorRegistrationDTO = new DriverConductorRegistrationDTO();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT dcr_first_issue_date,dcr_license_issue_date,dcr_license_expire_date,dcr_seq, dcr_app_no, nt_r_training_type.description as  dcr_training_type, dcr_driver_conductor_id, dcr_nic, dcr_name_with_initials,"
					+ " dcr_full_name_eng, dcr_full_name_sin, dcr_full_name_tam, dcr_gender, dcr_dob, dcr_district, dcr_contact_no,"
					+ " dcr_license_no, dcr_date_of_issue, dcr_date_of_expiry, dcr_education, dcr_gs_certificate_no, "
					+ " dcr_police_clearance_station, dcr_vehicle_reg_no, dcr_permit_no, dcr_route_number, dcr_from, dcr_to, "
					+ " dcr_status, dcr_special_remarks, dcr_add_1_eng, "
					+ " dcr_add_2_eng, dcr_city_eng, dcr_add_1_sin, dcr_add_2_sin, dcr_city_sin, dcr_add_1_tam, dcr_add_2_tam, "
					+ " dcr_city_tam, dcr_points, dcr_status_type,"
					+ " dcr_old_app_no, dcr_is_duplicate, dcr_gs_issue_date,dcr_police_cl_issue_date,dcr_medi_cer_no,dcr_medi_cer_issue_date,dcr_medi_cer_expire_date  "
					+ " FROM public.nt_t_driver_conductor_registration inner join nt_r_training_type on nt_t_driver_conductor_registration.dcr_training_type = nt_r_training_type.code "
					+ " WHERE dcr_driver_conductor_id = ? order by dcr_created_date desc limit 1 ";

			ps = con.prepareStatement(query);
			ps.setString(1, strDCId);

			rs = ps.executeQuery();

			while (rs.next()) {
				driverConductorRegistrationDTO.setViewDemeritPoint(rs.getInt("dcr_points"));
				driverConductorRegistrationDTO.setFirstDateOfIssueN(rs.getString("dcr_first_issue_date"));
				driverConductorRegistrationDTO.setDateOfIssueLicenceN(rs.getString("dcr_license_issue_date"));
				driverConductorRegistrationDTO.setDateOfExpiryLicenceN(rs.getString("dcr_license_expire_date"));

				driverConductorRegistrationDTO.setSeq(rs.getLong("dcr_seq"));
				driverConductorRegistrationDTO.setAppNo(rs.getString("dcr_app_no"));
				driverConductorRegistrationDTO.setNewtrainingType(rs.getString("dcr_training_type"));
				driverConductorRegistrationDTO.setDriverConductorId(rs.getString("dcr_driver_conductor_id"));
				driverConductorRegistrationDTO.setNic(rs.getString("dcr_nic"));
				driverConductorRegistrationDTO.setNameWithInitials(rs.getString("dcr_name_with_initials"));
				driverConductorRegistrationDTO.setFullNameEng(rs.getString("dcr_full_name_eng"));
				driverConductorRegistrationDTO.setFullNameSin(rs.getString("dcr_full_name_sin"));
				driverConductorRegistrationDTO.setFullNameTam(rs.getString("dcr_full_name_tam"));
				driverConductorRegistrationDTO.setNewGender(rs.getString("dcr_gender"));

				Date dateDob = formatter.parse(rs.getString("dcr_dob"));
				if (dateDob != null) {
					driverConductorRegistrationDTO.setDob(dateDob);
				}
				driverConductorRegistrationDTO.setNewDistrict(rs.getString("dcr_district"));
				driverConductorRegistrationDTO.setContactNo(rs.getString("dcr_contact_no"));
				driverConductorRegistrationDTO.setLicenseNo(rs.getString("dcr_license_no"));

				driverConductorRegistrationDTO.setDateOfIssueN(rs.getString("dcr_date_of_issue")); // dateOfIssue
				driverConductorRegistrationDTO.setExpiryDateN(rs.getString("dcr_date_of_expiry"));// dateOfExpiry

				driverConductorRegistrationDTO.setEducation(rs.getString("dcr_education"));
				driverConductorRegistrationDTO.setGsCertificateNo(rs.getString("dcr_gs_certificate_no"));
				driverConductorRegistrationDTO.setPoliceClearanceStation(rs.getString("dcr_police_clearance_station"));
				driverConductorRegistrationDTO.setVehicleRegNo(rs.getString("dcr_vehicle_reg_no"));
				driverConductorRegistrationDTO.setPermitNo(rs.getString("dcr_permit_no"));
				driverConductorRegistrationDTO.setRouteNo(rs.getString("dcr_route_number"));
				driverConductorRegistrationDTO.setFrom(rs.getString("dcr_from"));
				driverConductorRegistrationDTO.setTo(rs.getString("dcr_to"));
				driverConductorRegistrationDTO.setStatus(rs.getString("dcr_status"));
				driverConductorRegistrationDTO.setSpecialRemarks(rs.getString("dcr_special_remarks"));

				driverConductorRegistrationDTO.setAdd1Eng(rs.getString("dcr_add_1_eng"));
				driverConductorRegistrationDTO.setAdd2Eng(rs.getString("dcr_add_2_eng"));
				driverConductorRegistrationDTO.setCityEng(rs.getString("dcr_city_eng"));
				driverConductorRegistrationDTO.setAdd1Sin(rs.getString("dcr_add_1_sin"));
				driverConductorRegistrationDTO.setAdd2Sin(rs.getString("dcr_add_2_sin"));
				driverConductorRegistrationDTO.setCitySin(rs.getString("dcr_city_sin"));
				driverConductorRegistrationDTO.setAdd1Tam(rs.getString("dcr_add_1_tam"));
				driverConductorRegistrationDTO.setAdd2Tam(rs.getString("dcr_add_2_tam"));
				driverConductorRegistrationDTO.setCityTam(rs.getString("dcr_city_tam"));
				driverConductorRegistrationDTO.setPoints(rs.getInt("dcr_points"));
				driverConductorRegistrationDTO.setStatusType(rs.getString("dcr_status_type"));
				driverConductorRegistrationDTO.setOldApp(rs.getString("dcr_old_app_no"));
				driverConductorRegistrationDTO.setIsDuplicate(rs.getString("dcr_is_duplicate"));
				if (rs.getString("dcr_gs_issue_date") != null && !rs.getString("dcr_gs_issue_date").isEmpty()) {
					Date gsIssueDate = formatter.parse(rs.getString("dcr_gs_issue_date"));
					if (gsIssueDate != null) {
						driverConductorRegistrationDTO.setGscissuedate(gsIssueDate);
					}
				}
				if (rs.getString("dcr_police_cl_issue_date") != null
						&& !rs.getString("dcr_police_cl_issue_date").isEmpty()) {
					Date pcIssueDate = formatter.parse(rs.getString("dcr_police_cl_issue_date"));
					if (pcIssueDate != null) {
						driverConductorRegistrationDTO.setPcissuedate(pcIssueDate);
					}
				}

				driverConductorRegistrationDTO.setMedicalcertificateNo(rs.getString("dcr_medi_cer_no"));
				if (rs.getString("dcr_medi_cer_issue_date") != null
						&& !rs.getString("dcr_medi_cer_issue_date").isEmpty()) {
					Date mcIssueDate = formatter.parse(rs.getString("dcr_medi_cer_issue_date"));
					if (mcIssueDate != null) {
						driverConductorRegistrationDTO.setMrissuedate(mcIssueDate);
					}
				}
				if (rs.getString("dcr_medi_cer_expire_date") != null
						&& !rs.getString("dcr_medi_cer_expire_date").isEmpty()) {
					Date mcExpireDate = formatter.parse(rs.getString("dcr_medi_cer_expire_date"));
					if (mcExpireDate != null) {
						driverConductorRegistrationDTO.setMrexpiredate(mcExpireDate);
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
		return driverConductorRegistrationDTO;
	}

	public boolean updateDriverConductorRegistrationEV(DriverConductorRegistrationDTO driverConductorRegistrationDTO,
			String strSelectedType, String loginUser) {

		boolean isUpdated = false;
		Connection con = null;
		PreparedStatement stmt = null, stmt1 = null;
		ResultSet rs = null;

		String dateofissueN = null;
		String dateofexpiry = null;
		String dateofFirstissueN = null;
		String gsIssueDate = null;
		String pcIssueDate = null;
		String mediReIssueDate = null;
		String mediReportExpireDate = null;
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date date = new java.util.Date();
		Timestamp timeStamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			String dob = dateFormat.format(driverConductorRegistrationDTO.getDob());
			if (driverConductorRegistrationDTO.getGscissuedate() != null) {
				gsIssueDate = dateFormat.format(driverConductorRegistrationDTO.getGscissuedate());
			}
			if (driverConductorRegistrationDTO.getPcissuedate() != null) {
				pcIssueDate = dateFormat.format(driverConductorRegistrationDTO.getPcissuedate());
			}
			if (driverConductorRegistrationDTO.getMrissuedate() != null) {
				mediReIssueDate = dateFormat.format(driverConductorRegistrationDTO.getMrissuedate());
			}
			if (driverConductorRegistrationDTO.getMrexpiredate() != null) {
				mediReportExpireDate = dateFormat.format(driverConductorRegistrationDTO.getMrexpiredate());
			}

			if (driverConductorRegistrationDTO.getDateOfIssue() != null) {
				dateofissueN = dateFormat.format(driverConductorRegistrationDTO.getDateOfIssue());
			}

			if (driverConductorRegistrationDTO.getExpiryDate() != null) {
				dateofexpiry = dateFormat.format(driverConductorRegistrationDTO.getExpiryDate());
			}
			if (driverConductorRegistrationDTO.getFirstDateOfIssue() != null) {
				dateofFirstissueN = dateFormat.format(driverConductorRegistrationDTO.getFirstDateOfIssue());
			}

			String sql1 = "INSERT into public.nt_h_driver_conductor_registration  "
					+ " (SELECT * FROM public.nt_t_driver_conductor_registration WHERE dcr_driver_conductor_id = ? AND dcr_training_type =?) ";

			stmt1 = con.prepareStatement(sql1);
			stmt1.setString(1, driverConductorRegistrationDTO.getDriverConductorId());
			stmt1.setString(2, strSelectedType);
			stmt1.executeUpdate();

			String sql = "UPDATE public.nt_t_driver_conductor_registration "
					+ " SET  dcr_driver_conductor_id=?, dcr_nic=?, "
					+ " dcr_name_with_initials=?, dcr_full_name_eng=?, dcr_full_name_sin=?, dcr_full_name_tam=?, "
					+ " dcr_gender=?, dcr_dob=?, dcr_district=?, dcr_contact_no=?, dcr_license_no=?, dcr_date_of_issue=?,"
					+ " dcr_date_of_expiry=?, dcr_education=?, dcr_gs_certificate_no=?, dcr_police_clearance_station=?, "
					+ " dcr_vehicle_reg_no=?, dcr_permit_no=?, dcr_route_number=?, dcr_from=?, dcr_to=?, "
					+ " dcr_status=?, dcr_special_remarks=?, dcr_photo_uploaded=?, dcr_schedule_id=?, "
					+ " dcr_add_1_eng=?, dcr_add_2_eng=?, dcr_city_eng=?, dcr_add_1_sin=?, dcr_add_2_sin=?, "
					+ "	dcr_city_sin=?, dcr_add_1_tam=?, dcr_add_2_tam=?, dcr_city_tam=?, "
					+ " dcr_modified_date=?, dcr_modified_by=?, dcr_points=?, dcr_status_type=?"
					+ " ,dcr_gs_issue_date=? ,dcr_police_cl_issue_date=? ,dcr_medi_cer_no=? ,dcr_medi_cer_issue_date=? ,dcr_medi_cer_expire_date=?, dcr_first_issue_date =?,dcr_training_language= ?,dcr_update_by_high_auth_officer =?"
					+ " WHERE dcr_app_no=? ";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, driverConductorRegistrationDTO.getDriverConductorId());
			stmt.setString(2, driverConductorRegistrationDTO.getNic());
			stmt.setString(3, driverConductorRegistrationDTO.getNameWithInitials());
			stmt.setString(4, driverConductorRegistrationDTO.getFullNameEng());
			stmt.setString(5, driverConductorRegistrationDTO.getFullNameSin());
			stmt.setString(6, driverConductorRegistrationDTO.getFullNameTam());
			stmt.setString(7, driverConductorRegistrationDTO.getNewGender());
			stmt.setString(8, dob);
			stmt.setString(9, driverConductorRegistrationDTO.getNewDistrict());
			stmt.setString(10, driverConductorRegistrationDTO.getContactNo());
			stmt.setString(11, driverConductorRegistrationDTO.getLicenseNo());
			stmt.setString(12, dateofissueN);
			stmt.setString(13, dateofexpiry);
			stmt.setString(14, driverConductorRegistrationDTO.getEducation());
			stmt.setString(15, driverConductorRegistrationDTO.getGsCertificateNo());
			stmt.setString(16, driverConductorRegistrationDTO.getPoliceClearanceStation());
			stmt.setString(17, driverConductorRegistrationDTO.getVehicleRegNo());
			stmt.setString(18, driverConductorRegistrationDTO.getPermitNo());
			stmt.setString(19, driverConductorRegistrationDTO.getRouteNo());
			stmt.setString(20, driverConductorRegistrationDTO.getFrom());
			stmt.setString(21, driverConductorRegistrationDTO.getTo());
			stmt.setString(22, driverConductorRegistrationDTO.getStatus());
			stmt.setString(23, driverConductorRegistrationDTO.getSpecialRemarks());
			stmt.setString(24, driverConductorRegistrationDTO.getPhotoUploaded());
			stmt.setLong(25, driverConductorRegistrationDTO.getScheduleId());
			stmt.setString(26, driverConductorRegistrationDTO.getAdd1Eng());
			stmt.setString(27, driverConductorRegistrationDTO.getAdd2Eng());
			stmt.setString(28, driverConductorRegistrationDTO.getCityEng());
			stmt.setString(29, driverConductorRegistrationDTO.getAdd1Sin());
			stmt.setString(30, driverConductorRegistrationDTO.getAdd2Sin());
			stmt.setString(31, driverConductorRegistrationDTO.getCitySin());
			stmt.setString(32, driverConductorRegistrationDTO.getAdd1Tam());
			stmt.setString(33, driverConductorRegistrationDTO.getAdd2Tam());
			stmt.setString(34, driverConductorRegistrationDTO.getCityTam());
			stmt.setTimestamp(35, timeStamp);
			stmt.setString(36, driverConductorRegistrationDTO.getModifiedBy());
			stmt.setInt(37, driverConductorRegistrationDTO.getPoints());
			stmt.setString(38, driverConductorRegistrationDTO.getStatusType());

			stmt.setString(39, gsIssueDate);
			stmt.setString(40, pcIssueDate);
			stmt.setString(41, driverConductorRegistrationDTO.getMedicalcertificateNo());
			stmt.setString(42, mediReIssueDate);
			stmt.setString(43, mediReportExpireDate);
			stmt.setString(44, dateofFirstissueN);
			stmt.setString(45, driverConductorRegistrationDTO.getTrainingLanguageCode());
			stmt.setString(46, driverConductorRegistrationDTO.getUpdateByHighAuth());
			stmt.setString(47, driverConductorRegistrationDTO.getAppNo());

			int i = stmt.executeUpdate();

			if (i > 0) {
				isUpdated = true;
			} else {
				isUpdated = false;
			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
		return isUpdated;

	}

	public List<CommonDTO> GetAllTrainingTypes() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description, points, type " + "FROM nt_r_training_type ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("code"));
				commonDTO.setDescription(rs.getString("description"));
				commonDTO.setPoints(rs.getInt("points"));
				commonDTO.setType(rs.getString("type"));

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

	public DriverConductorRegistrationDTO getDetailsByDCIdandType(String strDCId, String strType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		DriverConductorRegistrationDTO driverConductorRegistrationDTO = new DriverConductorRegistrationDTO();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT l.code,l.description,dcr_first_issue_date , dcr_license_expire_date  ,dcr_license_issue_date , dcr_seq, dcr_app_no, nt_r_training_type.description as  dcr_training_type, dcr_driver_conductor_id, dcr_nic, dcr_name_with_initials,"
					+ " dcr_full_name_eng, dcr_full_name_sin, dcr_full_name_tam, dcr_gender, dcr_dob, dcr_district, dcr_contact_no,"
					+ " dcr_license_no, dcr_date_of_issue, dcr_date_of_expiry, dcr_education, dcr_gs_certificate_no, "
					+ " dcr_police_clearance_station, dcr_vehicle_reg_no, dcr_permit_no, dcr_route_number, dcr_from, dcr_to, "
					+ " dcr_status, dcr_special_remarks,  dcr_add_1_eng, "
					+ " dcr_add_2_eng, dcr_city_eng, dcr_add_1_sin, dcr_add_2_sin, dcr_city_sin, dcr_add_1_tam, dcr_add_2_tam, "
					+ " dcr_city_tam, dcr_points, dcr_status_type,dcr_training_language,"
					+ " dcr_old_app_no, dcr_is_duplicate, dcr_gs_issue_date, dcr_police_cl_issue_date, dcr_medi_cer_no, dcr_medi_cer_issue_date,dcr_medi_cer_expire_date "
					+ " FROM public.nt_t_driver_conductor_registration inner join nt_r_training_type on nt_t_driver_conductor_registration.dcr_training_type = nt_r_training_type.code left outer join public.nt_r_language l on  nt_t_driver_conductor_registration.dcr_training_language = l.code "
					+ " WHERE dcr_driver_conductor_id = ? AND dcr_training_type = ? order by dcr_created_date desc limit 1; ";

			ps = con.prepareStatement(query);
			ps.setString(1, strDCId);
			ps.setString(2, strType);

			rs = ps.executeQuery();

			while (rs.next()) {
				driverConductorRegistrationDTO.setTrainingLanguageCode(rs.getString("dcr_training_language"));
				driverConductorRegistrationDTO.setTrainingLanguageCode(rs.getString("code"));
				driverConductorRegistrationDTO.setTrainingLanguageDes(rs.getString("description"));
				driverConductorRegistrationDTO.setFirstDateOfIssueN(rs.getString("dcr_first_issue_date"));
				driverConductorRegistrationDTO.setDateOfIssueLicenceN(rs.getString("dcr_license_issue_date"));
				driverConductorRegistrationDTO.setDateOfExpiryLicenceN(rs.getString("dcr_license_expire_date"));
				driverConductorRegistrationDTO.setSeq(rs.getLong("dcr_seq"));
				driverConductorRegistrationDTO.setAppNo(rs.getString("dcr_app_no"));
				driverConductorRegistrationDTO.setNewtrainingType(rs.getString("dcr_training_type"));
				driverConductorRegistrationDTO.setDriverConductorId(rs.getString("dcr_driver_conductor_id"));
				driverConductorRegistrationDTO.setNic(rs.getString("dcr_nic"));
				driverConductorRegistrationDTO.setNameWithInitials(rs.getString("dcr_name_with_initials"));
				driverConductorRegistrationDTO.setFullNameEng(rs.getString("dcr_full_name_eng"));
				driverConductorRegistrationDTO.setFullNameSin(rs.getString("dcr_full_name_sin"));
				driverConductorRegistrationDTO.setFullNameTam(rs.getString("dcr_full_name_tam"));
				driverConductorRegistrationDTO.setNewGender(rs.getString("dcr_gender"));

				Date dateDob = formatter.parse(rs.getString("dcr_dob"));
				if (dateDob != null) {
					driverConductorRegistrationDTO.setDob(dateDob);
				}
				driverConductorRegistrationDTO.setNewDistrict(rs.getString("dcr_district"));
				driverConductorRegistrationDTO.setContactNo(rs.getString("dcr_contact_no"));
				driverConductorRegistrationDTO.setLicenseNo(rs.getString("dcr_license_no"));

				driverConductorRegistrationDTO.setDateOfIssueN(rs.getString("dcr_date_of_issue")); // dateOfIssue
				driverConductorRegistrationDTO.setExpiryDateN(rs.getString("dcr_date_of_expiry"));// dateOfExpiry

				driverConductorRegistrationDTO.setEducation(rs.getString("dcr_education"));
				driverConductorRegistrationDTO.setGsCertificateNo(rs.getString("dcr_gs_certificate_no"));
				driverConductorRegistrationDTO.setPoliceClearanceStation(rs.getString("dcr_police_clearance_station"));
				driverConductorRegistrationDTO.setVehicleRegNo(rs.getString("dcr_vehicle_reg_no"));
				driverConductorRegistrationDTO.setPermitNo(rs.getString("dcr_permit_no"));
				driverConductorRegistrationDTO.setRouteNo(rs.getString("dcr_route_number"));
				driverConductorRegistrationDTO.setFrom(rs.getString("dcr_from"));
				driverConductorRegistrationDTO.setTo(rs.getString("dcr_to"));
				driverConductorRegistrationDTO.setStatus(rs.getString("dcr_status"));
				driverConductorRegistrationDTO.setSpecialRemarks(rs.getString("dcr_special_remarks"));

				driverConductorRegistrationDTO.setAdd1Eng(rs.getString("dcr_add_1_eng"));
				driverConductorRegistrationDTO.setAdd2Eng(rs.getString("dcr_add_2_eng"));
				driverConductorRegistrationDTO.setCityEng(rs.getString("dcr_city_eng"));
				driverConductorRegistrationDTO.setAdd1Sin(rs.getString("dcr_add_1_sin"));
				driverConductorRegistrationDTO.setAdd2Sin(rs.getString("dcr_add_2_sin"));
				driverConductorRegistrationDTO.setCitySin(rs.getString("dcr_city_sin"));
				driverConductorRegistrationDTO.setAdd1Tam(rs.getString("dcr_add_1_tam"));
				driverConductorRegistrationDTO.setAdd2Tam(rs.getString("dcr_add_2_tam"));
				driverConductorRegistrationDTO.setCityTam(rs.getString("dcr_city_tam"));
				driverConductorRegistrationDTO.setPoints(rs.getInt("dcr_points"));
				driverConductorRegistrationDTO.setStatusType(rs.getString("dcr_status_type"));
				driverConductorRegistrationDTO.setOldApp(rs.getString("dcr_old_app_no"));
				driverConductorRegistrationDTO.setIsDuplicate(rs.getString("dcr_is_duplicate"));
				if (rs.getString("dcr_gs_issue_date") != null && !rs.getString("dcr_gs_issue_date").isEmpty()) {
					Date gsIssueDate = formatter.parse(rs.getString("dcr_gs_issue_date"));
					if (gsIssueDate != null) {
						driverConductorRegistrationDTO.setGscissuedate(gsIssueDate);
					}
				}
				if (rs.getString("dcr_police_cl_issue_date") != null
						&& !rs.getString("dcr_police_cl_issue_date").isEmpty()) {
					Date pcIssueDate = formatter.parse(rs.getString("dcr_police_cl_issue_date"));
					if (pcIssueDate != null) {
						driverConductorRegistrationDTO.setPcissuedate(pcIssueDate);
					}
				}
				driverConductorRegistrationDTO.setMedicalcertificateNo(rs.getString("dcr_medi_cer_no"));
				if (rs.getString("dcr_medi_cer_issue_date") != null
						&& !rs.getString("dcr_medi_cer_issue_date").isEmpty()) {
					Date mediCerIssueDate = formatter.parse(rs.getString("dcr_medi_cer_issue_date"));
					if (mediCerIssueDate != null) {
						driverConductorRegistrationDTO.setMrissuedate(mediCerIssueDate);
					}
				}
				if (rs.getString("dcr_medi_cer_expire_date") != null
						&& !rs.getString("dcr_medi_cer_expire_date").isEmpty()) {
					Date mediCerExpireDate = formatter.parse(rs.getString("dcr_medi_cer_expire_date"));
					if (mediCerExpireDate != null) {
						driverConductorRegistrationDTO.setMrexpiredate(mediCerExpireDate);
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
		return driverConductorRegistrationDTO;
	}

	@Override
	public List<CommonDTO> GetTrainingTypesByModeForRe(String strMode) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description, points, type " + "FROM nt_r_training_type " + "WHERE mode !=? ";

			ps = con.prepareStatement(query);
			ps.setString(1, strMode);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("code"));
				commonDTO.setDescription(rs.getString("description"));
				commonDTO.setPoints(rs.getInt("points"));
				commonDTO.setType(rs.getString("type"));

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
	public List<DriverConductorRegistrationDTO> getNicNumberList() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<DriverConductorRegistrationDTO> returnIdList = new ArrayList<DriverConductorRegistrationDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct dcr_nic,dcr_driver_conductor_id from public.nt_t_driver_conductor_registration\r\n"
					+ "where dcr_status not in('I')  ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				DriverConductorRegistrationDTO dcDTO = new DriverConductorRegistrationDTO();
				dcDTO.setNic(rs.getString("dcr_nic"));
				dcDTO.setDriverConductorId(rs.getString("dcr_driver_conductor_id"));

				returnIdList.add(dcDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return returnIdList;

	}

	@Override
	public List<DriverConductorRegistrationDTO> getDCIDListByID(String id) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String WHERE_SQL = "";

		List<DriverConductorRegistrationDTO> returnDcidList = new ArrayList<DriverConductorRegistrationDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct dcr_driver_conductor_id FROM nt_t_driver_conductor_registration WHERE dcr_status not in('I') and dcr_nic=?  "
					+ WHERE_SQL;

			ps = con.prepareStatement(query);
			ps.setString(1, id);
			rs = ps.executeQuery();

			while (rs.next()) {
				DriverConductorRegistrationDTO commonDTO = new DriverConductorRegistrationDTO();
				commonDTO.setDriverConductorId(rs.getString("dcr_driver_conductor_id"));

				returnDcidList.add(commonDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return returnDcidList;
	}

	@Override
	public DriverConductorRegistrationDTO insertDriverConductorReReg(DriverConductorRegistrationDTO dto) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		DriverConductorRegistrationDTO driverConductorRegistrationDTO = new DriverConductorRegistrationDTO();

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_driver_conductor_registration");

			String appNo = generateAppNo(seqNo, dto.getCreatedBy());
			String dob = dateFormat.format(dto.getDob());

			String gsIssueDate = dateFormat.format(dto.getGscissuedate());
			String pcIssueDate = dateFormat.format(dto.getPcissuedate());
			String mediReIssueDate = null;
			String mediReportExpireDate = null;
			if (dto.getMrissuedate() != null) {
				mediReIssueDate = dateFormat.format(dto.getMrissuedate());
			}
			if (dto.getMrexpiredate() != null) {
				mediReportExpireDate = dateFormat.format(dto.getMrexpiredate());

			}
			String dateofissue = "";
			if (dto.getDateOfIssue() != null) {
				dateofissue = dateFormat.format(dto.getDateOfIssue());
			}
			String dateofexpiry = "";
			if (dto.getDateOfExpiry() != null) {
				dateofexpiry = dateFormat.format(dto.getDateOfExpiry());
			}

			String firstIssueDate = null;
			if (dto.getFirstDateOfIssue() != null) {
				firstIssueDate = dateFormat.format(dto.getFirstDateOfIssue());
			}

			String liceneIssueDate = null;
			if (dto.getDateOfIssueLicence() != null) {
				liceneIssueDate = dateFormat.format(dto.getDateOfIssueLicence());
			}

			String liceneExpireDate = null;
			if (dto.getDateOfExpiryLicence() != null) {
				liceneExpireDate = dateFormat.format(dto.getDateOfExpiryLicence());
			}

			String sql = "INSERT INTO public.nt_t_driver_conductor_registration("
					+ "dcr_seq, dcr_app_no, dcr_training_type, dcr_driver_conductor_id, "
					+ "dcr_nic, dcr_name_with_initials, dcr_full_name_eng, dcr_full_name_sin, "
					+ "dcr_full_name_tam, dcr_gender, dcr_dob, dcr_district, dcr_contact_no, "
					+ "dcr_license_no, dcr_date_of_issue, dcr_date_of_expiry, dcr_education, "
					+ "dcr_gs_certificate_no, dcr_police_clearance_station, dcr_vehicle_reg_no, "
					+ "dcr_permit_no, dcr_route_number, " + "dcr_status, dcr_special_remarks, "
					+ "dcr_add_1_eng, dcr_add_2_eng, dcr_city_eng, "
					+ "dcr_add_1_sin, dcr_add_2_sin, dcr_city_sin, dcr_add_1_tam, dcr_add_2_tam, "
					+ "dcr_city_tam, dcr_created_by, dcr_created_date, dcr_points,dcr_from,dcr_to,dcr_status_type,"
					+ " dcr_old_app_no, dcr_is_duplicate ,dcr_gs_issue_date,dcr_police_cl_issue_date,dcr_medi_cer_no,dcr_medi_cer_issue_date,dcr_medi_cer_expire_date ,dcr_first_issue_date,dcr_license_issue_date ,dcr_license_expire_date,dcr_training_language ) "
					+ "VALUES (?, ?, ?, ?, " + "?, ?, ?, ?, " + "?, ?, ?, ?, ?, " + "?, ?, ?, ?, " + "?, ?, ?, "
					+ "?, ?, " + "?, ?, " + "?, ?, ?, " + "?, ?, ?, ?, ?, "
					+ "?, ?, ?, ?,?,?,?,?,?, ?, ?, ?, ?, ? ,?,?,?,?);";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, appNo);
			stmt.setString(3, dto.getTrainingType().getCode());
			stmt.setString(4, dto.getDriverConductorId());

			stmt.setString(5, dto.getNic());
			stmt.setString(6, dto.getNameWithInitials());
			stmt.setString(7, dto.getFullNameEng());
			stmt.setString(8, dto.getFullNameSin());

			stmt.setString(9, dto.getFullNameTam());
			stmt.setString(10, dto.getNewGender());
			stmt.setString(11, dob);
			stmt.setString(12, dto.getNewDistrict());
			stmt.setString(13, dto.getContactNo());

			stmt.setString(14, dto.getLicenseNo());
			stmt.setString(15, dateofissue);
			stmt.setString(16, dateofexpiry);
			stmt.setString(17, dto.getEducation());

			stmt.setString(18, dto.getGsCertificateNo());
			stmt.setString(19, dto.getPoliceClearanceStation());
			stmt.setString(20, dto.getVehicleRegNo());

			stmt.setString(21, dto.getPermitNo());
			stmt.setString(22, dto.getRouteNo());

			stmt.setString(23, "P");
			stmt.setString(24, dto.getSpecialRemarks());

			stmt.setString(25, dto.getAdd1Eng());
			stmt.setString(26, dto.getAdd2Eng());
			stmt.setString(27, dto.getCityEng());

			stmt.setString(28, dto.getAdd1Sin());
			stmt.setString(29, dto.getAdd2Sin());
			stmt.setString(30, dto.getCitySin());
			stmt.setString(31, dto.getAdd1Tam());
			stmt.setString(32, dto.getAdd2Tam());

			stmt.setString(33, dto.getCityTam());
			stmt.setString(34, dto.getCreatedBy());
			stmt.setTimestamp(35, timestamp);
			stmt.setInt(36, dto.getPoints());
			stmt.setString(37, dto.getFrom());
			stmt.setString(38, dto.getTo());

			stmt.setString(39, "P");
			stmt.setString(40, dto.getOldApp());
			stmt.setString(41, dto.getIsDuplicate());

			stmt.setString(42, gsIssueDate);
			stmt.setString(43, pcIssueDate);
			stmt.setString(44, dto.getMedicalcertificateNo());
			stmt.setString(45, mediReIssueDate);
			stmt.setString(46, mediReportExpireDate);
			stmt.setString(47, firstIssueDate);
			stmt.setString(48, liceneIssueDate);
			stmt.setString(49, liceneExpireDate);
			stmt.setString(50, dto.getTrainingLanguage());
			int i = stmt.executeUpdate();

			if (i > 0) {
				driverConductorRegistrationDTO.setAppNo(appNo);
				driverConductorRegistrationDTO.setDriverConductorId(dto.getDriverConductorId());

			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
		return driverConductorRegistrationDTO;
	}

	@Override
	public List<DriverConductorRegistrationDTO> getTrainingTypeList() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<DriverConductorRegistrationDTO> returnList = new ArrayList<DriverConductorRegistrationDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description, points, type " + "FROM nt_r_training_type ";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				DriverConductorRegistrationDTO trainingType = new DriverConductorRegistrationDTO();
				trainingType.setTrainingTypeCode(rs.getString("code"));
				trainingType.setTrainingTYpeDes(rs.getString("description"));
				trainingType.setPoints(rs.getInt("points"));

				returnList.add(trainingType);

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
	public String getOldAppNo(String driverConducID, String nicNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String appNo = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select dcr_app_no  from public.nt_t_driver_conductor_registration where  dcr_driver_conductor_id =? and dcr_nic =? order by dcr_created_date desc limit 1";

			ps = con.prepareStatement(query);
			ps.setString(1, driverConducID);
			ps.setString(2, nicNo);
			rs = ps.executeQuery();

			while (rs.next()) {

				appNo = rs.getString("dcr_app_no");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return appNo;

	}

	@Override
	public List<DriverConductorRegistrationDTO> getAppNolistForselectedTraining(String trainingType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<DriverConductorRegistrationDTO> appNoList = new ArrayList<DriverConductorRegistrationDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select dcr_app_no from public.nt_t_driver_conductor_registration \r\n"
					+ "where dcr_training_type=? and dcr_status not in ('I')\r\n" + "order by dcr_app_no; ";

			ps = con.prepareStatement(query);
			ps.setString(1, trainingType);
			rs = ps.executeQuery();

			while (rs.next()) {
				DriverConductorRegistrationDTO appNo = new DriverConductorRegistrationDTO();
				appNo.setAppNo(rs.getString("dcr_app_no"));

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
	public List<DriverConductorRegistrationDTO> getIdNoListForSelectedAppNo(String trainingType, String appNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<DriverConductorRegistrationDTO> idNoList = new ArrayList<DriverConductorRegistrationDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select dcr_nic from public.nt_t_driver_conductor_registration \r\n"
					+ "where dcr_training_type=? and dcr_app_no=? and dcr_status not in ('I')\r\n"
					+ "order by dcr_nic; ";

			ps = con.prepareStatement(query);
			ps.setString(1, trainingType);
			ps.setString(2, appNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				DriverConductorRegistrationDTO idNoDto = new DriverConductorRegistrationDTO();
				idNoDto.setNic(rs.getString("dcr_nic"));

				idNoList.add(idNoDto);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return idNoList;

	}

	@Override
	public DriverConductorRegistrationDTO getDriverConducByID(String appNo, String idNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		DriverConductorRegistrationDTO commonDTO = new DriverConductorRegistrationDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct dcr_driver_conductor_id FROM nt_t_driver_conductor_registration WHERE dcr_status not in('I') and dcr_nic=? and dcr_app_no=?  ";

			ps = con.prepareStatement(query);
			ps.setString(1, idNo);
			ps.setString(2, appNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				commonDTO.setDriverConductorId(rs.getString("dcr_driver_conductor_id"));

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return commonDTO;
	}

	/******************/
	// Maintain Training
	/******************/
	public List<CommonDTO> GetAllMonths() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description, non " + "FROM nt_r_month WHERE active = 'A' ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("code"));
				commonDTO.setDescription(rs.getString("description"));
				commonDTO.setNon(rs.getString("non"));

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

	private String generateAppNoforCommon(String code, String loginUser) {

		String last = retrieveLastNoForNumberGeneration(code);
		long no = Long.parseLong(last.substring(3));
		String next = String.valueOf((no + 1));
		String appNo = code + StringUtils.leftPad(next, 7, "0");

		updateNumberGeneration(code, appNo, loginUser);

		return appNo;
	}

	public String insertTrainingSchedule(MaintainTrainingScheduleDTO dto, String loginUser) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		String trainingScheduleCode = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_training_schedule");

			String scheduleCode = generateAppNoforCommon("MTS", loginUser);

			String sql = "INSERT INTO public.nt_t_training_schedule "
					+ " (seqno, trs_schedule_code, trs_year, trs_month_code, trs_training_date, trs_start_time, trs_end_time, trs_training_type_code, trs_location, trs_no_of_trainees, trs_status, trs_created_by, trs_created_date)"
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, scheduleCode);
			stmt.setString(3, dto.getYearN());
			stmt.setString(4, dto.getMonthCode());
			String trainingDate = "";
			if (dto.getTrainingDate() != null) {
				trainingDate = dateFormat.format(dto.getTrainingDate());
			}
			stmt.setString(5, trainingDate);
			stmt.setString(6, dto.getStartTime());
			stmt.setString(7, dto.getEndTime());
			stmt.setString(8, dto.getTrainingTypeCode());
			stmt.setString(9, dto.getLocation());
			stmt.setBigDecimal(10, dto.getNoofTrainees());
			stmt.setString(11, dto.getStaus());
			stmt.setString(12, loginUser);
			stmt.setTimestamp(13, timestamp);

			int i = stmt.executeUpdate();

			if (i > 0) {
				trainingScheduleCode = scheduleCode;

			} else {
				trainingScheduleCode = null;
			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
		return trainingScheduleCode;
	}

	public List<MaintainTrainingScheduleDTO> getScheduleDetails() {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		List<MaintainTrainingScheduleDTO> scheduleList = new ArrayList<MaintainTrainingScheduleDTO>();

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT seqno, trs_schedule_code, trs_year, trs_month_code, trs_training_date, trs_start_time, trs_end_time, trs_training_type_code, trs_location, trs_no_of_trainees,"
					+ " nt_r_month.description as monthDesc,nt_r_training_type.description as trainingdesc, trs_status "
					+ " FROM public.nt_t_training_schedule inner join nt_r_training_type on nt_t_training_schedule.trs_training_type_code = nt_r_training_type.code "
					+ " inner join nt_r_month on nt_t_training_schedule.trs_month_code = nt_r_month.code "
					+ " WHERE trs_status = 'A' AND trs_training_type_code not in ('DD','DC') ORDER BY trs_schedule_code ";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				MaintainTrainingScheduleDTO maintainTrainingScheduleDTO = new MaintainTrainingScheduleDTO();
				maintainTrainingScheduleDTO.setScheduleCode(rs.getString("trs_schedule_code"));
				maintainTrainingScheduleDTO.setYearN(rs.getString("trs_year"));
				maintainTrainingScheduleDTO.setMonthCode(rs.getString("trs_month_code"));

				String startDateString = rs.getString("trs_training_date");
				if (startDateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date datestart = originalFormat.parse(startDateString);
					maintainTrainingScheduleDTO.setTrainingDate(datestart);

					maintainTrainingScheduleDTO.setStrtrainingDate(startDateString);
				}

				maintainTrainingScheduleDTO.setStartTime(rs.getString("trs_start_time"));
				maintainTrainingScheduleDTO.setEndTime(rs.getString("trs_end_time"));
				maintainTrainingScheduleDTO.setTrainingTypeCode(rs.getString("trs_training_type_code"));
				maintainTrainingScheduleDTO.setLocation(rs.getString("trs_location"));
				maintainTrainingScheduleDTO.setNoofTrainees(rs.getBigDecimal("trs_no_of_trainees"));
				maintainTrainingScheduleDTO.setMonthDesc(rs.getString("monthDesc"));
				maintainTrainingScheduleDTO.setTypeDesc(rs.getString("trainingdesc"));
				maintainTrainingScheduleDTO.setStaus(rs.getString("trs_status"));
				scheduleList.add(maintainTrainingScheduleDTO);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return scheduleList;

	}

	public boolean updateTrainingSchedule(MaintainTrainingScheduleDTO dto, String loginUser) {
		boolean isUpdated = false;
		Connection con = null;
		PreparedStatement stmt = null, stmt1 = null, stmt2 = null;
		ResultSet rs = null, rs1 = null, rs2 = null;

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		MaintainTrainingScheduleDTO selectedDTO = new MaintainTrainingScheduleDTO();
		java.util.Date date = new java.util.Date();
		Timestamp timeStamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			String sql1 = "SELECT seqno, trs_schedule_code, trs_year, trs_month_code, trs_training_date, trs_start_time, "
					+ " trs_end_time, trs_training_type_code, trs_location, trs_no_of_trainees, trs_status, trs_created_by, "
					+ " trs_created_date, trs_modified_by, trs_modified_date " + " FROM public.nt_t_training_schedule "
					+ " WHERE trs_schedule_code= ?";

			stmt1 = con.prepareStatement(sql1);
			stmt1.setString(1, dto.getScheduleCode());
			rs1 = stmt1.executeQuery();

			while (rs1.next()) {
				selectedDTO.setSeq(rs1.getLong("seqno"));
				selectedDTO.setScheduleCode(rs1.getString("trs_schedule_code"));
				selectedDTO.setYearN(rs1.getString("trs_year"));
				selectedDTO.setMonthCode(rs1.getString("trs_month_code"));
				String startDateString = rs1.getString("trs_training_date");
				if (startDateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date datestart = originalFormat.parse(startDateString);
					selectedDTO.setTrainingDate(datestart);

					selectedDTO.setStrtrainingDate(startDateString);
				}
				selectedDTO.setStartTime(rs1.getString("trs_start_time"));
				selectedDTO.setEndTime(rs1.getString("trs_end_time"));
				selectedDTO.setTrainingTypeCode(rs1.getString("trs_training_type_code"));
				selectedDTO.setLocation(rs1.getString("trs_location"));
				selectedDTO.setNoofTrainees(rs1.getBigDecimal("trs_no_of_trainees"));
				selectedDTO.setStaus(rs1.getString("trs_status"));
				selectedDTO.setCreatedBy(rs1.getString("trs_created_by"));
				selectedDTO.setCreatedDate(rs1.getTimestamp("trs_created_date"));
				selectedDTO.setModifiedBy(rs1.getString("trs_modified_by"));
				selectedDTO.setModifiedDate(rs1.getTimestamp("trs_modified_date"));
			}

			String sql2 = "INSERT INTO public.nt_h_training_schedule "
					+ " (seqno, trs_schedule_code, trs_year, trs_month_code, trs_training_date, trs_start_time, trs_end_time, trs_training_type_code, trs_location, trs_no_of_trainees, trs_status, trs_created_by, trs_created_date, trs_modified_by, trs_modified_date )"
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,? ,?);";

			stmt2 = con.prepareStatement(sql2);

			stmt2.setLong(1, selectedDTO.getSeq());
			stmt2.setString(2, selectedDTO.getScheduleCode());
			stmt2.setString(3, selectedDTO.getYearN());
			stmt2.setString(4, selectedDTO.getMonthCode());
			String trainingDate = "";
			if (dto.getTrainingDate() != null) {
				trainingDate = dateFormat.format(dto.getTrainingDate());
			}
			stmt2.setString(5, trainingDate);
			stmt2.setString(6, selectedDTO.getStartTime());
			stmt2.setString(7, selectedDTO.getEndTime());
			stmt2.setString(8, selectedDTO.getTrainingTypeCode());
			stmt2.setString(9, selectedDTO.getLocation());
			stmt2.setBigDecimal(10, selectedDTO.getNoofTrainees());
			stmt2.setString(11, selectedDTO.getStaus());
			stmt2.setString(12, selectedDTO.getCreatedBy());
			stmt2.setTimestamp(13, selectedDTO.getCreatedDate());
			stmt2.setString(14, selectedDTO.getModifiedBy());
			stmt2.setTimestamp(15, selectedDTO.getModifiedDate());

			stmt2.executeUpdate();

			String sql = "UPDATE public.nt_t_training_schedule "
					+ " SET trs_year=?, trs_month_code=?, trs_training_date=?, trs_start_time=?,"
					+ " trs_end_time=?, trs_training_type_code=?, trs_location=?, trs_no_of_trainees=?, trs_status=?, "
					+ " trs_modified_by=?, trs_modified_date=?" + " WHERE trs_schedule_code= ?";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, dto.getYearN());
			stmt.setString(2, dto.getMonthCode());
			String trainingDateN = "";
			if (dto.getTrainingDate() != null) {
				trainingDateN = dateFormat.format(dto.getTrainingDate());
			}
			stmt.setString(3, trainingDateN);
			stmt.setString(4, dto.getStartTime());
			stmt.setString(5, dto.getEndTime());
			stmt.setString(6, dto.getTrainingTypeCode());
			stmt.setString(7, dto.getLocation());
			stmt.setBigDecimal(8, dto.getNoofTrainees());
			stmt.setString(9, dto.getStaus());
			stmt.setString(10, loginUser);
			stmt.setTimestamp(11, timeStamp);
			stmt.setString(12, dto.getScheduleCode());

			int i = stmt.executeUpdate();

			if (i > 0) {
				isUpdated = true;
			} else {
				isUpdated = false;
			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(rs1);
			ConnectionManager.close(rs2);
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt1);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(con);
		}
		return isUpdated;

	}

	/***********************/
	// Setup Temporary Period
	/***********************/
	@Override
	public String getNamebyNICOrDCId(String nicNo, String idNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String WHERE_SQL = "";
		String rtnValue = "";

		if (nicNo != null && !nicNo.equals("")) {
			WHERE_SQL = " dcr_nic = " + "'" + nicNo + "'";
		}
		if (idNo != null && !idNo.equals("")) {
			WHERE_SQL = " dcr_driver_conductor_id = " + "'" + idNo + "'";
		}
		if (nicNo != null && !nicNo.equals("") && idNo != null && !idNo.equals("")) {
			WHERE_SQL = " dcr_nic = " + "'" + nicNo + "'" + " AND dcr_driver_conductor_id = " + "'" + idNo + "'";
		}

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT dcr_full_name_eng FROM nt_t_driver_conductor_registration WHERE " + WHERE_SQL + " order by dcr_seq ASC";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				rtnValue = rs.getString("dcr_full_name_eng");
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return rtnValue;
	}

	public List<DriverConductorRegistrationDTO> getPaymentCompletedDC() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<DriverConductorRegistrationDTO> data = new ArrayList<DriverConductorRegistrationDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT dcr_app_no,dcr_driver_conductor_id,dcr_full_name_eng, dcr_nic "
					+ " FROM public.nt_t_driver_conductor_registration " + " WHERE dcr_status_type = 'R' "
					+ " AND dcr_status = 'TA'" + " and dcr_training_type not in ('DD','DC') " + " ORDER BY dcr_app_no";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				DriverConductorRegistrationDTO dcDTO = new DriverConductorRegistrationDTO();
				dcDTO.setAppNo(rs.getString("dcr_app_no"));
				dcDTO.setDriverConductorId(rs.getString("dcr_driver_conductor_id"));
				dcDTO.setFullName(rs.getString("dcr_full_name_eng"));
				dcDTO.setNic(rs.getString("dcr_nic"));
				data.add(dcDTO);

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

	public boolean InsertDCTempPeriod(DriverConductorRegistrationDTO dcDto, String user,
			List<DriverConductorRegistrationDTO> dcTempPeriodList) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean success = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		PreparedStatement ps = null;

		try {
			con = ConnectionManager.getConnection();

			for (DriverConductorRegistrationDTO list : dcTempPeriodList) {
				long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_driver_conductor_temp_period");

				String sql = "INSERT INTO public.nt_t_driver_conductor_temp_period "
						+ "(dcp_seq, dcp_app_no, dcp_driver_conductor_id, dcp_nic, dcp_date_from, dcp_date_to, dcp_remark, dcp_created_by, dcp_created_date)"
						+ " VALUES(?,?,?,?,?,?,?,?,?);";

				stmt = con.prepareStatement(sql);

				stmt.setLong(1, seqNo);
				stmt.setString(2, list.getAppNo());
				stmt.setString(3, list.getDriverConductorId());
				stmt.setString(4, list.getNic());

				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				String fromDate = df.format(list.getFromDate());
				stmt.setString(5, fromDate);

				DateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
				String toDate = dt.format(list.getToDate());
				stmt.setString(6, toDate);
				stmt.setString(7, null);
				stmt.setString(8, user);
				stmt.setTimestamp(9, timestamp);

				int data = stmt.executeUpdate();

				if (data > 0) {
					success = true;
				} else {
					success = false;
				}

				/***/
				if (success) {
					String sql2 = " UPDATE nt_t_driver_conductor_registration" + " SET dcr_status_type = 'T' "
							+ " WHERE dcr_app_no = ? ";

					ps = con.prepareStatement(sql2);
					ps.setString(1, list.getAppNo());
					ps.executeUpdate();
				}
				/*****/

				con.commit();

			}
		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return success;
	}

	public List<CommonDTO> getTempANICList() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct dcr_nic FROM nt_t_driver_conductor_registration WHERE  dcr_status_type in ('T','M') and dcr_status = 'TA' ";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("dcr_nic"));

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

	public List<CommonDTO> getTempANICListByTrainingType(String trainingType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT dcr_nic FROM nt_t_driver_conductor_registration WHERE  dcr_status_type in ('T','M') and dcr_status = 'TA' AND dcr_training_type = ?  ";

			ps = con.prepareStatement(query);
			ps.setString(1, trainingType);

			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("dcr_nic"));

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

	public List<CommonDTO> getTempANICListByTrainingTypeN(String trainingType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT dcr_nic FROM nt_t_driver_conductor_registration WHERE  dcr_status_type = 'R' and dcr_status = 'TA' AND dcr_training_type = ?  ";

			ps = con.prepareStatement(query);
			ps.setString(1, trainingType);

			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("dcr_nic"));

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

	public List<CommonDTO> getTempDCIDListByTrainingandID(String trainingType, String idNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String WHERE_SQL = "";

		if (trainingType != null && !trainingType.equals("")) {
			WHERE_SQL = " AND dcr_training_type = " + "'" + trainingType + "'";
		}
		if (idNo != null && !idNo.equals("")) {
			WHERE_SQL = " AND dcr_nic = " + "'" + idNo + "'";
		}
		if (trainingType != null && !trainingType.equals("") && idNo != null && !idNo.equals("")) {
			WHERE_SQL = " AND dcr_training_type = " + "'" + trainingType + "'" + " AND dcr_nic = " + "'" + idNo + "'";
		}

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT dcr_driver_conductor_id FROM nt_t_driver_conductor_registration WHERE dcr_status ='TA' "
					+ WHERE_SQL;

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("dcr_driver_conductor_id"));

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
	public List<DriverConductorRegistrationDTO> getChargeTypeDetails(String trainingType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<DriverConductorRegistrationDTO> chargeTypeDataList = new ArrayList<DriverConductorRegistrationDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select tct_code,tct_trn_type_code,tct_charge_type_code,tct_account_no,tct_amount,a.description\r\n"
					+ "from public.nt_r_trn_charge_type\r\n"
					+ "inner join  public.nt_r_charge_type a on a.code=public.nt_r_trn_charge_type.tct_charge_type_code\r\n"
					+ "where tct_trn_type_code=?\r\n" + "and tct_status='A'\r\n" + "and a.active='A'; ";

			ps = con.prepareStatement(query);
			ps.setString(1, trainingType);

			rs = ps.executeQuery();

			while (rs.next()) {
				DriverConductorRegistrationDTO chargeType = new DriverConductorRegistrationDTO();
				chargeType.setAccountNO(rs.getString("tct_account_no"));
				chargeType.setAmmount(rs.getBigDecimal("tct_amount"));
				chargeType.setChargeTypeDes(rs.getString("description"));
				chargeType.setChargeTypeCode(rs.getString("tct_charge_type_code"));

				chargeTypeDataList.add(chargeType);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return chargeTypeDataList;

	}

	@Override
	public List<DriverConductorRegistrationDTO> getChargeType() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<DriverConductorRegistrationDTO> data = new ArrayList<DriverConductorRegistrationDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT code, description FROM nt_r_charge_type WHERE active = 'A' order by description;";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				DriverConductorRegistrationDTO chargeTypeDTO = new DriverConductorRegistrationDTO();
				chargeTypeDTO.setChargeTypeCode(rs.getString("code"));
				chargeTypeDTO.setChargeTypeDes(rs.getString("description"));
				data.add(chargeTypeDTO);

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
	public DriverConductorRegistrationDTO getAccountAndAmountbyChargeType(String chargeType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		DriverConductorRegistrationDTO accountAmountDTO = new DriverConductorRegistrationDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "select tct_account_no ,tct_amount  from  public.nt_r_trn_charge_type where tct_charge_type_code =? and tct_status ='A'";

			ps = con.prepareStatement(query);
			ps.setString(1, chargeType);

			rs = ps.executeQuery();

			while (rs.next()) {
				accountAmountDTO.setAccountNO(rs.getString("tct_account_no"));
				accountAmountDTO.setAmmount(rs.getBigDecimal("tct_amount"));

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return accountAmountDTO;
	}

	@Override
	public String getChargeTypeDes(String code) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String description = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select description from  public.nt_r_charge_type where code =? and active ='A'";

			ps = con.prepareStatement(query);
			ps.setString(1, code);
			rs = ps.executeQuery();

			while (rs.next()) {
				description = rs.getString("description");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return description;

	}

	@Override
	public boolean alreadyGenerate(String appNo) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;

		boolean wasVoucherGenerated = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM public.nt_m_driver_conduc_payment_voucher where dcv_application_no =? and dcv_approved_status !='R' ;";

			ps = con.prepareStatement(query);
			ps.setString(1, appNo);
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
	public String generateVoucherNo() {
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

			String sql = "select dcv_voucher_no  from  public.nt_m_driver_conduc_payment_voucher order by dcv_created_date  desc  LIMIT 1";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("dcv_voucher_no");
			}

			if (result != null) {

				lastyr = result.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String ApprecordcountN = "00001";
					strAppNo = "DCV" + currYear + ApprecordcountN;
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
					strAppNo = "DCV" + currYear + ApprecordcountN;
				}
			} else
				strAppNo = "DCV" + currYear + "00001";

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
	public boolean insertVoucherDetInMaster(DriverConductorRegistrationDTO driverConducDTO, String loginUSer,
			String value, BigDecimal totalfee) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isVousherGenerated = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			insertDriverConductorVoucherToHistory(driverConducDTO, con);

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_driver_conduc_payment_voucher");

			String query = "INSERT INTO public.nt_m_driver_conduc_payment_voucher\r\n"
					+ "(dcv_seq_no, dcv_training_type, dcv_application_no, dcv_id_no, dcv_driver_conduc_id_no, dcv_voucher_no,  dcv_total_amount , dcv_created_by, dcv_created_date, dcv_approved_status)\r\n"
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?,?);\r\n" + "; ";

			ps = con.prepareStatement(query);

			ps.setLong(1, seqNo);
			ps.setString(2, driverConducDTO.getTrainingTypeCode());
			ps.setString(3, driverConducDTO.getAppNo());
			ps.setString(4, driverConducDTO.getNic());
			ps.setString(5, driverConducDTO.getDriverConductorId());
			ps.setString(6, value);
			ps.setBigDecimal(7, totalfee);
			ps.setString(8, loginUSer);
			ps.setTimestamp(9, timestamp);
			ps.setString(10, "P");
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

	private boolean insertDriverConductorVoucherToHistory(DriverConductorRegistrationDTO driverConducDTO,
			Connection con) throws Exception {

		boolean returnVal = false;

		PreparedStatement ps0 = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		PreparedStatement ps4 = null;
		ResultSet rs0 = null;

		String query = "select dcv_seq_no from public.nt_m_driver_conduc_payment_voucher where dcv_application_no =? and dcv_approved_status ='R'";
		ps0 = con.prepareStatement(query);
		ps0.setString(1, driverConducDTO.getAppNo());
		rs0 = ps0.executeQuery();

		int success1 = 0;
		int success2 = 0;
		int success3 = 0;
		int success4 = 0;
		while (rs0.next()) {

			String sql = "insert into public.nt_h_driver_conduc_payment_voucher "
					+ "(select * from public.nt_m_driver_conduc_payment_voucher where dcv_seq_no =?)";
			ps1 = con.prepareStatement(sql);
			ps1.setLong(1, rs0.getLong("dcv_seq_no"));
			success1 = ps1.executeUpdate();

			String sql2 = "insert into public.nt_h_driver_conduc_payment_voucher_detail "
					+ "(select * from public.nt_t_driver_conduc_payment_voucher where dcvd_payment_vou_seq =?)";
			ps2 = con.prepareStatement(sql2);
			ps2.setString(1, String.valueOf(rs0.getLong("dcv_seq_no")));
			success2 = ps2.executeUpdate();

			// delete details table first
			String sql4 = "delete from public.nt_t_driver_conduc_payment_voucher where dcvd_payment_vou_seq =?";
			ps4 = con.prepareStatement(sql4);
			ps4.setString(1, String.valueOf(rs0.getLong("dcv_seq_no")));
			success4 = ps4.executeUpdate();

			// delete master table second
			String sql3 = "delete from public.nt_m_driver_conduc_payment_voucher where dcv_seq_no =?";
			ps3 = con.prepareStatement(sql3);
			ps3.setLong(1, rs0.getLong("dcv_seq_no"));
			success3 = ps3.executeUpdate();

		}

		ConnectionManager.close(ps0);
		ConnectionManager.close(ps1);
		ConnectionManager.close(ps2);
		ConnectionManager.close(ps3);
		ConnectionManager.close(ps4);
		ConnectionManager.close(rs0);

		if (success1 > 0 && success2 > 0 && success3 > 0 && success4 > 0) {
			returnVal = true;
		}

		return returnVal;
	}

	@Override
	public boolean insertVoucherDetailsInDetTable(DriverConductorRegistrationDTO driverDTO,
			List<DriverConductorRegistrationDTO> chargeTypeDetList, String loginUser, String voucherNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		long masterSeq = 0;
		boolean isUpdateTask = false;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();
			String query1 = "select dcv_seq_no  from  public.nt_m_driver_conduc_payment_voucher where dcv_application_no =?";

			stmt1 = con.prepareStatement(query1);
			stmt1.setString(1, driverDTO.getAppNo());
			rs1 = stmt1.executeQuery();

			while (rs1.next()) {

				masterSeq = rs1.getLong("dcv_seq_no");

			}

			for (int i = 0; i < chargeTypeDetList.size(); i++) {

				long seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_t_driver_conduc_payment_voucher");

				String query2 = "INSERT INTO public.nt_t_driver_conduc_payment_voucher\r\n"
						+ "(dcvd_seq_no, dcvd_payment_vou_seq, dcvd_voucher_no, dcvd_charge_type, dcvd_account_no, dcvd_amount, dcvd_created_by, dcvd_created_date, dcvd_application_no)\r\n"
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?); ;";

				stmt = con.prepareStatement(query2);

				stmt.setLong(1, seqNo);
				stmt.setLong(2, masterSeq);
				stmt.setString(3, voucherNo);
				stmt.setString(4, chargeTypeDetList.get(i).getChargeTypeCode());
				stmt.setString(5, chargeTypeDetList.get(i).getAccountNO());
				if (chargeTypeDetList.get(i).getAmmount() != null) {
					BigDecimal bigDecimalAmount = chargeTypeDetList.get(i).getAmmount();
					stmt.setBigDecimal(6, bigDecimalAmount);
				}

				stmt.setString(7, loginUser);
				stmt.setTimestamp(8, timestamp);
				stmt.setString(9, driverDTO.getAppNo());

				stmt.executeUpdate();
			}

			con.commit();

		} catch (Exception ex) {

			ex.printStackTrace();
			return isUpdateTask;

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(rs1);
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt1);
			ConnectionManager.close(con);

		}

		return isUpdateTask;
	}

	@Override
	public List<DriverConductorRegistrationDTO> getAppNoForApproveByTrainingType(String trainingCode) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<DriverConductorRegistrationDTO> appNoList = new ArrayList<DriverConductorRegistrationDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select dcr_app_no \r\n" + "from public.nt_t_driver_conductor_registration a\r\n"
					+ "inner join public.nt_m_driver_conduc_payment_voucher b \r\n"
					+ "on a.dcr_app_no=b.dcv_application_no\r\n"
					+ "where a.dcr_training_type=? and a.dcr_status not in ('I')  \r\n"
					+ "and b.dcv_voucher_no is not null\r\n" + "order by dcr_app_no; ; ";

			ps = con.prepareStatement(query);
			ps.setString(1, trainingCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				DriverConductorRegistrationDTO appNo = new DriverConductorRegistrationDTO();
				appNo.setAppNo(rs.getString("dcr_app_no"));

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
	public List<DriverConductorRegistrationDTO> getVoucherNoList() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<DriverConductorRegistrationDTO> returnVoucherList = new ArrayList<DriverConductorRegistrationDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select dcv_voucher_no FROM public.nt_m_driver_conduc_payment_voucher where dcv_voucher_no is not null order by dcv_voucher_no  ";

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
	public List<DriverConductorRegistrationDTO> getPaymentDetailsOnGrid(DriverConductorRegistrationDTO dcDto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<DriverConductorRegistrationDTO> paymentVoucherLIST = new ArrayList<DriverConductorRegistrationDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT  dcv_voucher_no,dcv_training_type,dcv_driver_conduc_id_no,dcv_application_no,\r\n"
					+ "dcv_receipt_no,dcv_approved_status,dcv_total_amount,nt_r_training_type.description \r\n"
					+ "FROM public.nt_m_driver_conduc_payment_voucher\r\n" + "inner join public.nt_r_training_type\r\n"
					+ "on nt_r_training_type.code=nt_m_driver_conduc_payment_voucher.dcv_training_type \r\n"
					+ "where dcv_approved_status='P' ";

			DriverConductorRegistrationDTO e;
			ps = con.prepareStatement(query2);

			rs = ps.executeQuery();

			while (rs.next()) {
				e = new DriverConductorRegistrationDTO();
				e.setVoucher(rs.getString("dcv_voucher_no"));
				e.setTrainingTypeCode(rs.getString("dcv_training_type"));
				e.setTrainingTYpeDes(rs.getString("description"));
				e.setDriverConductorId(rs.getString("dcv_driver_conduc_id_no"));
				e.setAppNo(rs.getString("dcv_application_no"));
				e.setReceiptNo(rs.getString("dcv_receipt_no"));

				String approveStatusCode = rs.getString("dcv_approved_status");
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

				e.setTotalAmount(rs.getBigDecimal("dcv_total_amount"));

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
	public DriverConductorRegistrationDTO getVoucherSatus(String voucherNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		DriverConductorRegistrationDTO dcDTO = new DriverConductorRegistrationDTO();

		try {
			con = ConnectionManager.getConnection();

			String sql = "select dcv_approved_status ,dcv_receipt_no  from  public.nt_m_driver_conduc_payment_voucher where dcv_voucher_no =?";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, voucherNo);
			rs = stmt.executeQuery();

			while (rs.next()) {
				dcDTO.setApproveStatus(rs.getString("dcv_approved_status"));
				dcDTO.setReceiptNo(rs.getString("dcv_receipt_no"));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return dcDTO;
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

			String sql3 = "UPDATE public.nt_m_driver_conduc_payment_voucher SET dcv_approved_status =?,dcv_reject_reason =?,dcv_voucher_approve_reject_by =?,dcv_voucher_approve_reject_date =? WHERE dcv_voucher_no =? ";

			ps = con.prepareStatement(sql3);

			ps.setString(1, status);
			ps.setString(2, reason);
			ps.setString(3, loginUser);
			ps.setTimestamp(4, timestamp);
			ps.setString(5, voucherNo);

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
	public List<DriverConductorRegistrationDTO> getPaymentDetailsOnGridBySearch(DriverConductorRegistrationDTO dcDto) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String WHERE_SQL = "";
		boolean whereadded = false;
		if (dcDto.getTrainingTypeCode() != null && !dcDto.getTrainingTypeCode().equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE dcv_training_type  = " + "'" + dcDto.getTrainingTypeCode() + "' ";

			whereadded = true;
		}

		if (dcDto.getReceiptNo() != null && !dcDto.getReceiptNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " and dcv_receipt_no  = " + "'" + dcDto.getReceiptNo() + "' ";

			}

		}
		if (dcDto.getVoucher() != null && !dcDto.getVoucher().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " and dcv_voucher_no  = " + "'" + dcDto.getVoucher() + "' ";

			}

		}
		if (dcDto.getAppNo() != null && !dcDto.getAppNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " and dcv_application_no  = " + "'" + dcDto.getAppNo() + "' ";

			}

		}

		List<DriverConductorRegistrationDTO> paymentVoucherLIST = new ArrayList<DriverConductorRegistrationDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT  dcv_voucher_no,dcv_training_type,dcv_driver_conduc_id_no,dcv_application_no, "
					+ "dcv_receipt_no,dcv_approved_status,dcv_total_amount,nt_r_training_type.description "
					+ "FROM public.nt_m_driver_conduc_payment_voucher " + "inner join public.nt_r_training_type "
					+ "on nt_r_training_type.code=nt_m_driver_conduc_payment_voucher.dcv_training_type " + WHERE_SQL;

			DriverConductorRegistrationDTO e;
			ps = con.prepareStatement(query2);

			rs = ps.executeQuery();

			while (rs.next()) {
				e = new DriverConductorRegistrationDTO();
				e.setVoucher(rs.getString("dcv_voucher_no"));
				e.setTrainingTypeCode(rs.getString("dcv_training_type"));
				e.setTrainingTYpeDes(rs.getString("description"));
				e.setDriverConductorId(rs.getString("dcv_driver_conduc_id_no"));
				e.setAppNo(rs.getString("dcv_application_no"));
				e.setReceiptNo(rs.getString("dcv_receipt_no"));

				String approveStatusCode = rs.getString("dcv_approved_status");
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

				e.setTotalAmount(rs.getBigDecimal("dcv_total_amount"));

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
	public DriverConductorRegistrationDTO updateStatusType(String statusType, String appNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String sql3 = "UPDATE public.nt_t_driver_conductor_registration SET dcr_status_type =? WHERE dcr_app_no  =? ";

			ps = con.prepareStatement(sql3);

			ps.setString(1, statusType);
			ps.setString(2, appNo);

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
	public List<DriverConductorRegistrationDTO> getsearchDataByEnterdVal(String trainingType, String nicNo, String dcId,
			String name) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String WHERE_SQL = "";
		if (trainingType != null && !trainingType.equals("")) {
			WHERE_SQL = WHERE_SQL + " and a.dcr_training_type  = " + "'" + trainingType + "' ";
		}

		if (nicNo != null && !nicNo.equals("")) {

			WHERE_SQL = WHERE_SQL + " and a.dcr_nic  = " + "'" + nicNo + "' ";

		}
		if (dcId != null && !dcId.equals("")) {

			WHERE_SQL = WHERE_SQL + " and a.dcr_driver_conductor_id  = " + "'" + dcId + "' ";

		}
		if (name != null && !name.equals("")) {

			WHERE_SQL = WHERE_SQL + " and a.dcr_full_name_eng  = " + "'" + name + "' ";

		}

		List<DriverConductorRegistrationDTO> showDataList = new ArrayList<DriverConductorRegistrationDTO>();
		SimpleDateFormat date1 = new SimpleDateFormat("dd/MM/yyyy");
		try {
			con = ConnectionManager.getConnection();

			String query2 = "select b.dcp_app_no,b.dcp_driver_conductor_id,b.dcp_nic,a.dcr_full_name_eng,b.dcp_date_from,b.dcp_date_to,a.dcr_training_type,c.description from nt_t_driver_conductor_registration a inner join nt_t_driver_conductor_temp_period b on "
					+ "b.dcp_app_no = a.dcr_app_no inner join public.nt_r_training_type c on c.code=a.dcr_training_type "
					+ "where a.dcr_status_type in ('R','T','M') and a.dcr_status = 'TA' and c.status='A' " + WHERE_SQL;

			DriverConductorRegistrationDTO e;
			ps = con.prepareStatement(query2);

			rs = ps.executeQuery();

			while (rs.next()) {
				e = new DriverConductorRegistrationDTO();
				e.setTrainingTYpeDes(rs.getString("description"));
				e.setTrainingTypeCode(rs.getString("dcr_training_type"));
				e.setNic(rs.getString("dcp_nic"));
				e.setDriverConductorId(rs.getString("dcp_driver_conductor_id"));
				e.setFullName(rs.getString("dcr_full_name_eng"));
				e.setAppNo(rs.getString("dcp_app_no"));
				Date toDate = date1.parse(rs.getString("dcp_date_to"));
				Date fromDate = date1.parse(rs.getString("dcp_date_from"));
				e.setToDate(toDate);
				e.setFromDate(fromDate);

				showDataList.add(e);
			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return showDataList;
	}

	@Override
	public boolean updateDCTempPeriod(DriverConductorRegistrationDTO dcDto, String user) {

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();
			String sql1 = "select dcp_seq,dcp_app_no,dcp_driver_conductor_id,dcp_nic,dcp_date_from,dcp_date_to,dcp_remark,dcp_created_by,dcp_created_date from public.nt_t_driver_conductor_temp_period where dcp_app_no=?";
			ps1 = con.prepareStatement(sql1);
			ps1.setString(1, dcDto.getAppNo());
			rs1 = ps1.executeQuery();
			DriverConductorRegistrationDTO tempPeriod = new DriverConductorRegistrationDTO();
			while (rs1.next()) {
				tempPeriod.setSeq(rs1.getLong("dcp_seq"));
				tempPeriod.setAppNo(rs1.getString("dcp_app_no"));
				tempPeriod.setDriverConductorId(rs1.getString("dcp_driver_conductor_id"));
				tempPeriod.setNic(rs1.getString("dcp_nic"));
				tempPeriod.setFrom(rs1.getString("dcp_date_from"));
				tempPeriod.setTo(rs1.getString("dcp_date_to"));
				tempPeriod.setSpecialRemarks(rs1.getString("dcp_remark"));
				tempPeriod.setEnteredUser(rs1.getString("dcp_created_by"));
				tempPeriod.setEnterDate(rs1.getTimestamp("dcp_created_date"));

			}

			String sql2 = "INSERT INTO public.nt_h_driver_conductor_temp_period\r\n"
					+ "(dcp_seq, dcp_app_no, dcp_driver_conductor_id, dcp_nic, dcp_date_from, dcp_date_to, dcp_remark, dcp_created_by, dcp_created_date)\r\n"
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);";
			ps2 = con.prepareStatement(sql2);
			ps2.setLong(1, tempPeriod.getSeq());
			ps2.setString(2, tempPeriod.getAppNo());
			ps2.setString(3, tempPeriod.getDriverConductorId());
			ps2.setString(4, tempPeriod.getNic());
			ps2.setString(5, tempPeriod.getFrom());
			ps2.setString(6, tempPeriod.getTo());
			ps2.setString(7, tempPeriod.getSpecialRemarks());
			ps2.setString(8, tempPeriod.getEnteredUser());
			ps2.setTimestamp(9, tempPeriod.getEnterDate());

			ps2.executeUpdate();

			String sql3 = "UPDATE public.nt_t_driver_conductor_temp_period SET dcp_date_from=? ,dcp_date_to=?,dcp_modified_by =?,dcp_modified_date =? WHERE dcp_app_no =? ";

			ps = con.prepareStatement(sql3);
			String strDateFrom = dateFormat.format(dcDto.getFromDate());
			String strDateTo = dateFormat.format(dcDto.getToDate());
			ps.setString(1, strDateFrom);
			ps.setString(2, strDateTo);
			ps.setString(3, user);
			ps.setTimestamp(4, timestamp);
			ps.setString(5, dcDto.getAppNo());

			ps.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(rs1);
			ConnectionManager.close(ps);
			ConnectionManager.close(ps1);
			ConnectionManager.close(ps2);
			ConnectionManager.close(con);

		}
		return true;

	}

	@Override
	public List<String> getScheduledTrainingDates(String typeOfTraining) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<String> scheduleDateList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT trs_training_date FROM public.nt_t_training_schedule WHERE trs_status = 'A' AND trs_training_type_code = ?;";

			ps = con.prepareStatement(query);
			ps.setString(1, typeOfTraining);
			rs = ps.executeQuery();

			while (rs.next()) {
				String date = rs.getString("trs_training_date");
				scheduleDateList.add(date);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return scheduleDateList;
	}

	@Override
	public List<DriverConductorTrainingDTO> getDriverConductorTrainings(boolean pendingOnly, String typeOfTraining,
			Date regStartDate, Date regEndDate, String appNo, String name, String driverId, String conductorId,
			String languageCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<DriverConductorTrainingDTO> trainingDateList = new ArrayList<DriverConductorTrainingDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM nt_t_driver_conductor_registration WHERE dcr_training_type = ? AND dcr_training_type not in ('DD','DC')";

			if (regStartDate != null) {
				query += " AND TO_DATE(dcr_date_of_issue, 'dd/MM/yyyy') >= ? ";
			}
			if (regEndDate != null) {
				query += " AND TO_DATE(dcr_date_of_issue, 'dd/MM/yyyy') <= ? ";
			}
			if (appNo != null && !appNo.trim().isEmpty()) {
				query += " AND dcr_app_no = ? ";
			}
			if (name != null && !name.trim().isEmpty()) {
				query += " AND dcr_full_name_eng LIKE ? ";
			}
			if ((driverId != null && !driverId.trim().isEmpty())
					|| (conductorId != null && !conductorId.trim().isEmpty())) {
				query += " AND dcr_driver_conductor_id IN(?,?) ";
			}

			if (languageCode != null && !languageCode.trim().isEmpty()) {
				query += " AND dcr_training_language = ? ";
			}

			if (pendingOnly)
				query += " AND dcr_status_type IN ('T') ";
			else
				query += " AND dcr_status_type IN ('T', 'M') ";

//			query += " ORDER by dcr_app_no;";
			query += " ORDER BY dcr_created_date DESC;";

			int i = 1;

			ps = con.prepareStatement(query);
			ps.setString(i, typeOfTraining);

			if (regStartDate != null) {
				java.util.Date utilStartDate = regStartDate;
				java.sql.Date sqlStartDate = new java.sql.Date(utilStartDate.getTime());
				ps.setDate(++i, sqlStartDate);
			}
			if (regEndDate != null) {
				java.util.Date utilEndDate = regEndDate;
				java.sql.Date sqlEndDate = new java.sql.Date(utilEndDate.getTime());
				ps.setDate(++i, sqlEndDate);
			}
			if (appNo != null && !appNo.trim().isEmpty()) {
				ps.setString(++i, appNo);
			}
			if (name != null && !name.trim().isEmpty()) {
				ps.setString(++i, "%" + name + "%");
			}
			if ((driverId != null && !driverId.trim().isEmpty())
					|| (conductorId != null && !conductorId.trim().isEmpty())) {
				ps.setString(++i, driverId);
				ps.setString(++i, conductorId);
			}

			if (languageCode != null && !languageCode.trim().isEmpty()) {
				ps.setString(++i, languageCode);
			}

			rs = ps.executeQuery();

			while (rs.next()) {
				DriverConductorTrainingDTO dto = new DriverConductorTrainingDTO();
				dto.setAppNo(rs.getString("dcr_app_no"));
				dto.setDriverConductorId(rs.getString("dcr_driver_conductor_id"));
				dto.setFullName(rs.getString("dcr_full_name_eng"));
				dto.setRegisteredDate(rs.getString("dcr_date_of_issue"));
				if (rs.getString("dcr_training_date") != null)
					dto.setTrainingDate(rs.getString("dcr_training_date"));
				dto.setStatusTypeCode(rs.getString("dcr_status_type"));
				dto.setStatusCode(rs.getString("dcr_status"));

				String lanCode = rs.getString("dcr_training_language");

				if (lanCode != null) {
					if (lanCode.equals("S")) {
						dto.setLanguage("SINHALA");
					} else if (lanCode.equals("E")) {
						dto.setLanguage("ENGLISH");
					} else if (lanCode.equals("T")) {
						dto.setLanguage("TAMIL");
					}
				}

				trainingDateList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return trainingDateList;
	}

	@Override
	public boolean saveTraingingDates(DriverConductorTrainingDTO dcTrainingDateDTO, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		boolean result = false;

		try {
			con = ConnectionManager.getConnection();

			// TODO update history

			String sql = "UPDATE nt_t_driver_conductor_registration SET dcr_training_date = ?, dcr_status_type = 'M', dcr_modified_by = ?, dcr_modified_date = ? "
					+ " WHERE dcr_app_no = ?";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, dcTrainingDateDTO.getTrainingDate());
			stmt.setString(2, user);
			stmt.setTimestamp(3, timestamp);
			stmt.setString(4, dcTrainingDateDTO.getAppNo());
			stmt.executeUpdate();

			con.commit();
			result = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return result;
	}

	@Override
	public boolean updateStatus(String statusType, String status, String oldStatusType, String oldStatus, String appNo,
			String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt_h = null;
		PreparedStatement stmt_P = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		boolean result = false;

		try {
			con = ConnectionManager.getConnection();

			// update history
			String query_h = "UPDATE nt_h_driver_conductor_registration SET  ";

			if (oldStatusType != null)
				query_h += " dcr_status_type = ?, ";

			if (oldStatus != null)
				query_h += " dcr_status = ?, ";

			query_h += " dcr_modified_by = ?, dcr_modified_date = ? WHERE dcr_app_no = ?";

			stmt_h = con.prepareStatement(query_h);
			int i = 0;

			if (oldStatusType != null)
				stmt_h.setString(++i, oldStatusType);
			if (oldStatus != null)
				stmt_h.setString(++i, oldStatus);
			stmt_h.setString(++i, user);
			stmt_h.setTimestamp(++i, timestamp);
			stmt_h.setString(++i, appNo);
			stmt_h.executeUpdate();

			String query = "UPDATE nt_t_driver_conductor_registration SET  ";

			if (statusType != null)
				query += " dcr_status_type = ?, ";

			if (status != null)
				query += " dcr_status = ?, ";

			query += " dcr_modified_by = ?, dcr_modified_date = ? WHERE dcr_app_no = ?";

			stmt = con.prepareStatement(query);
			i = 0;

			if (statusType != null)
				stmt.setString(++i, statusType);
			if (status != null)
				stmt.setString(++i, status);
			stmt.setString(++i, user);
			stmt.setTimestamp(++i, timestamp);
			stmt.setString(++i, appNo);
			stmt.executeUpdate();

			con.commit();
			result = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt_P);
			ConnectionManager.close(stmt_h);
			ConnectionManager.close(con);
		}
		return result;
	}

	@Override
	public List<DriverConductorTrainingDTO> getAttendanceList(Date trainingDate, String selectedTrainingType,
			Date startTime, Date endTime, String location) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

		List<DriverConductorTrainingDTO> attendaceList = new ArrayList<DriverConductorTrainingDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM nt_t_driver_conductor_registration "
					+ " LEFT JOIN nt_t_training_schedule on trs_status = 'A' AND dcr_training_type = trs_training_type_code AND dcr_training_date = trs_training_date "
					+ " WHERE dcr_training_type = ? " + " AND dcr_training_date = ? ";

			if (startTime != null)
				query += " AND trs_start_time = ? ";

			if (endTime != null)
				query += " AND trs_end_time = ? ";

			if (location != null && !location.trim().isEmpty())
				query += " AND trs_location LIKE ? ";

			query += " ORDER BY dcr_nic;";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedTrainingType);
			ps.setString(2, dateFormat.format(trainingDate));

			int i = 2;

			if (startTime != null)
				ps.setString(++i, timeFormat.format(startTime));

			if (endTime != null)
				ps.setString(++i, timeFormat.format(endTime));

			if (location != null && !location.trim().isEmpty())
				ps.setString(++i, "%" + location + "%");

			rs = ps.executeQuery();

			while (rs.next()) {
				DriverConductorTrainingDTO dto = new DriverConductorTrainingDTO();
				dto.setAppNo(rs.getString("dcr_app_no"));
				dto.setDriverConductorId(rs.getString("dcr_driver_conductor_id"));
				dto.setNic(rs.getString("dcr_nic"));
				dto.setFullName(rs.getString("dcr_full_name_eng"));
				dto.setRegisteredDate(rs.getString("dcr_date_of_issue"));
				dto.setExpiryDate(rs.getString("dcr_date_of_expiry"));
				dto.setAttendance(rs.getString("dcr_training_present") != null
						&& rs.getString("dcr_training_present").equalsIgnoreCase("Y"));
				dto.setAttPrint(rs.getString("dcr_training_attendance_print") != null
						&& rs.getString("dcr_training_attendance_print").equalsIgnoreCase("Y"));
				dto.setAttRemark(rs.getString("dcr_training_attendance_remark"));
				attendaceList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return attendaceList;
	}

	@Override
	public List<DriverConductorRegistrationDTO> getDriverConducFilterList(String trainingType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;

		List<DriverConductorRegistrationDTO> driverConducList = new ArrayList<DriverConductorRegistrationDTO>();

		try {
			con = ConnectionManager.getConnection();
			if (trainingType.equals("ND") || trainingType.equals("RD") || trainingType.equals("RRD")
					|| trainingType.equals("RRRD") || trainingType.equals("DD") || trainingType.equals("FD")) {
				query = "select distinct a.dcr_driver_conductor_id from (select distinct dcr_driver_conductor_id,dcr_training_type from  public.nt_t_driver_conductor_registration\r\n"
						+ "where dcr_status not in('I')\r\n"
						+ "and dcr_training_type in ('ND','RD','RRD','RRRD','DD','FD') order by dcr_driver_conductor_id)a";
			} else if (trainingType.equals("NC") || trainingType.equals("RC") || trainingType.equals("RRC")
					|| trainingType.equals("RRRC") || trainingType.equals("DC") || trainingType.equals("FC")) {

				query = "select distinct a.dcr_driver_conductor_id from (select distinct dcr_driver_conductor_id,dcr_training_type from  public.nt_t_driver_conductor_registration\r\n"
						+ "where dcr_status not in('I')\r\n"
						+ "and dcr_training_type in ('NC','RC','RRC','RRRC','DC','FC') order by dcr_driver_conductor_id)a";

			}
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				DriverConductorRegistrationDTO dcDTO = new DriverConductorRegistrationDTO();

				dcDTO.setDriverConductorId(rs.getString("dcr_driver_conductor_id"));

				driverConducList.add(dcDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return driverConducList;

	}

	@Override
	public boolean saveAttendance(List<DriverConductorTrainingDTO> attendanceList, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		boolean result = false;

		try {
			con = ConnectionManager.getConnection();

			// TODO update history

			String sql = "UPDATE nt_t_driver_conductor_registration SET dcr_training_present = ?, dcr_training_attendance_print = ?, dcr_training_attendance_remark = ?, dcr_modified_by = ?, dcr_modified_date = ? "
					+ " WHERE dcr_app_no = ?";

			for (DriverConductorTrainingDTO a : attendanceList) {
				stmt = con.prepareStatement(sql);

				stmt.setString(1, a.isAttendance() ? "Y" : "N");
				stmt.setString(2, a.isAttPrint() ? "Y" : "N");
				stmt.setString(3, a.getAttRemark());
				stmt.setString(4, user);
				stmt.setTimestamp(5, timestamp);
				stmt.setString(6, a.getAppNo());
				stmt.executeUpdate();
			}

			con.commit();
			result = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return result;
	}

	@Override
	public List<DriverConductorTrainingDTO> getCertificateInfo(boolean listAll, String selectedTrainingType,
			Date trainingDate, String applicationNo, String driverId, String conductorId) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String query = null;
		List<DriverConductorTrainingDTO> dataList = new ArrayList<DriverConductorTrainingDTO>();

		try {
			con = ConnectionManager.getConnection();

			if (listAll) {
				query = "SELECT *\r\n" + "FROM nt_t_driver_conductor_registration \r\n"
						+ "where ( dcr_training_attendance_print = 'Y' and dcr_training_present = 'Y'  or (dcr_training_type in ('DC','DD') \r\n"
						+ "and dcr_status in ('TA','A') and dcr_status_type in('R','CP'))) \r\n"
						+ "and dcr_status  not in ('I','B')";
			} else {
				query = "SELECT *\r\n" + "FROM nt_t_driver_conductor_registration \r\n"
						+ "where ( dcr_training_attendance_print = 'Y' and dcr_training_present = 'Y'  or (dcr_training_type in ('DC','DD') \r\n"
						+ "and dcr_status in ('TA') and dcr_status_type in('R'))) \r\n"
						+ "and dcr_status  not in ('I','B')";
			}
			if (selectedTrainingType != null && !selectedTrainingType.trim().isEmpty())
				query += " AND dcr_training_type = ? ";

			if (trainingDate != null)
				query += " AND TO_DATE(dcr_training_date, 'dd/MM/yyyy') = ? ";

			if (applicationNo != null && !applicationNo.trim().isEmpty())
				query += " AND dcr_app_no = ? ";

			if ((driverId != null && !driverId.trim().isEmpty())
					|| (conductorId != null && !conductorId.trim().isEmpty()))
				query += " AND dcr_driver_conductor_id IN (?,?) ";

			query += " ORDER by dcr_app_no;";

			int i = 0;

			ps = con.prepareStatement(query);

			if (selectedTrainingType != null && !selectedTrainingType.trim().isEmpty())
				ps.setString(++i, selectedTrainingType);

			if (trainingDate != null)
				ps.setString(++i, dateFormat.format(trainingDate));

			if (applicationNo != null && !applicationNo.trim().isEmpty())
				ps.setString(++i, applicationNo);

			if ((driverId != null && !driverId.trim().isEmpty())
					|| (conductorId != null && !conductorId.trim().isEmpty())) {
				ps.setString(++i, driverId);
				ps.setString(++i, conductorId);
			}
			rs = ps.executeQuery();

			while (rs.next()) {
				DriverConductorTrainingDTO dto = new DriverConductorTrainingDTO();
				dto.setAppNo(rs.getString("dcr_app_no"));
				dto.setDriverConductorId(rs.getString("dcr_driver_conductor_id"));
				dto.setFullName(rs.getString("dcr_full_name_eng"));
				dto.setExpiryDate(rs.getString("dcr_date_of_expiry"));
				dto.setFirstIssueDate(rs.getString("dcr_first_issue_date"));
				dto.setIssueDate(rs.getString("dcr_date_of_issue"));
				if (rs.getString("dcr_training_date") != null)
					dto.setTrainingDate(rs.getString("dcr_training_date"));
				dto.setAttRemark(rs.getString("dcr_training_attendance_remark"));
				dto.setCertificateNo(rs.getString("dcr_gs_certificate_no"));

				dto.setStatusTypeCode(rs.getString("dcr_status_type"));
				dto.setStatusCode(rs.getString("dcr_status"));

				dataList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return dataList;
	}

	@Override
	public byte[] getDriverConductorPhoto(String appNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		byte[] b = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT dcp_photo FROM nt_t_driver_conductor_photoupload WHERE dcp_app_no = ?;";

			ps = con.prepareStatement(query);
			ps.setString(1, appNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				b = rs.getBytes("dcp_photo");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return b;
	}

	@Override
	public DriverConductorBlacklistDTO getBlacklisterInfo(String nic, String drivercondId) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		DriverConductorBlacklistDTO dataDTO = new DriverConductorBlacklistDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT dcr_nic, dcr_app_no, dcr_driver_conductor_id, dcr_full_name_eng,dcr_status,dcr_points,dcr_training_type FROM nt_t_driver_conductor_registration "
					+ "WHERE dcr_status <> 'PB' ";

			if (nic != null && !nic.trim().isEmpty())
				query += " AND dcr_nic = ? ";

			if (drivercondId != null && !drivercondId.trim().isEmpty())
				query += " AND dcr_driver_conductor_id = ? ";

			int i = 0;
			ps = con.prepareStatement(query);

			if (nic != null && !nic.trim().isEmpty())
				ps.setString(++i, nic);

			if (drivercondId != null && !drivercondId.trim().isEmpty())
				ps.setString(++i, drivercondId);

			rs = ps.executeQuery();

			while (rs.next()) {
				dataDTO.setNic(rs.getString("dcr_nic"));
				dataDTO.setFullName(rs.getString("dcr_full_name_eng"));
				dataDTO.setTrainingType(rs.getString("dcr_training_type"));
				/*
				 * if (rs.getString("dcr_driver_conductor_id") != null &&
				 * rs.getString("dcr_driver_conductor_id").startsWith("DRI")) {
				 */
				if (dataDTO.getTrainingType().equals("ND") || dataDTO.getTrainingType().equals("RD")
						|| dataDTO.getTrainingType().equals("RRD") || dataDTO.getTrainingType().equals("RRRD")
						|| dataDTO.getTrainingType().equals("DD") || dataDTO.getTrainingType().equals("FD")) {
					dataDTO.setDriverAppNo(rs.getString("dcr_app_no"));
					dataDTO.setDriverId(rs.getString("dcr_driver_conductor_id"));
					dataDTO.setDriverStatus(rs.getString("dcr_status"));
					dataDTO.setDriverPoints(rs.getString("dcr_points"));
				} else {
					dataDTO.setConductorAppNo(rs.getString("dcr_app_no"));
					dataDTO.setConductorId(rs.getString("dcr_driver_conductor_id"));
					dataDTO.setConductorStatus(rs.getString("dcr_status"));
					dataDTO.setConductorPoints(rs.getString("dcr_points"));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return dataDTO;
	}

	@Override
	public List<DropDownDTO> getNonBlackListedDriCond() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<DropDownDTO> drpdwnDTOList = new ArrayList<DropDownDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT DISTINCT dcr_driver_conductor_id FROM nt_t_driver_conductor_registration a"
					+ " WHERE a.dcr_status <> 'PB' ORDER BY dcr_driver_conductor_id;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				DropDownDTO dto = new DropDownDTO();
				dto.setCode(rs.getString("dcr_driver_conductor_id"));
				dto.setDescription(rs.getString("dcr_driver_conductor_id"));
				drpdwnDTOList.add(dto);
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
	public List<DropDownDTO> getPendingBlackListedDriCond() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<DropDownDTO> drpdwnDTOList = new ArrayList<DropDownDTO>();

		try {
			con = ConnectionManager.getConnection();
			/**
			 * commented by tharushi.e and added query for get driver conductor id which are
			 * not in'I' in blacklist table
			 **/

			String query = " SELECT DISTINCT dcr_driver_conductor_id \r\n"
					+ " FROM nt_t_driver_conductor_registration a \r\n"
					+ " inner join nt_t_blacklisted_driver_conductor b on b.b_driver_conductor_id=a.dcr_driver_conductor_id\r\n"
					+ " WHERE a.dcr_status = 'PB' and b.b_status not in('I')\r\n" + " ORDER BY dcr_driver_conductor_id";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				DropDownDTO dto = new DropDownDTO();
				dto.setCode(rs.getString("dcr_driver_conductor_id"));
				dto.setDescription(rs.getString("dcr_driver_conductor_id"));
				drpdwnDTOList.add(dto);
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
	public List<DropDownDTO> getBlackListedDriCond() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<DropDownDTO> drpdwnDTOList = new ArrayList<DropDownDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT DISTINCT dcr_driver_conductor_id FROM nt_t_driver_conductor_registration a"
					+ " WHERE a.dcr_status IN ('PB','B') ORDER BY dcr_driver_conductor_id;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				DropDownDTO dto = new DropDownDTO();
				dto.setCode(rs.getString("dcr_driver_conductor_id"));
				dto.setDescription(rs.getString("dcr_driver_conductor_id"));
				drpdwnDTOList.add(dto);
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
	public List<DropDownDTO> getBlacklistTypes() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<DropDownDTO> drpdwnDTOList = new ArrayList<DropDownDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM nt_r_blacklist_types WHERE status = 'A' ORDER BY code;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				DropDownDTO dto = new DropDownDTO();
				dto.setCode(rs.getString("code"));
				dto.setDescription(rs.getString("description"));
				drpdwnDTOList.add(dto);
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
	public boolean createBlacklister(DriverConductorBlacklistDTO blacklisterDTO, String driConId, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt_status = null;
		ResultSet rs_status = null;
		boolean result = false;

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		String dateStr = dateFormat.format(date);

		try {
			con = ConnectionManager.getConnection();

			// insert blacklist data
			String sql = "INSERT INTO public.nt_t_blacklisted_driver_conductor "
					+ "(b_seq, b_nic, b_driver_conductor_id, b_blacklist_type, b_blacklist_reason, b_blacklist_date, b_created_by, b_created_date, b_status) "
					+ " VALUES(?,?,?,?,?,?,?,?,?);";
			long seq = Utils.getNextValBySeqName(con, "seq_nt_t_blacklisted_driver_conductor");

			stmt = con.prepareStatement(sql);
			stmt.setLong(1, seq);
			stmt.setString(2, blacklisterDTO.getNic());
			stmt.setString(3, driConId);
			stmt.setString(4, blacklisterDTO.getBlacklistType());
			stmt.setString(5, blacklisterDTO.getBlacklistReason());
			stmt.setString(6, dateStr);
			stmt.setString(7, user);
			stmt.setTimestamp(8, timestamp);
			stmt.setString(9, "A");
			stmt.executeUpdate();

			// update Status
			String sql_status = "SELECT * FROM nt_t_driver_conductor_registration WHERE dcr_nic = ?;";

			stmt_status = con.prepareStatement(sql_status);
			stmt_status.setString(1, blacklisterDTO.getNic());
			rs_status = stmt_status.executeQuery();

			if (rs_status.next()) {
				DriverConductorTrainingDTO dto = new DriverConductorTrainingDTO();
				dto.setAppNo(rs_status.getString("dcr_app_no"));
				dto.setStatusTypeCode(rs_status.getString("dcr_status_type"));
				dto.setStatusCode(rs_status.getString("dcr_status"));

				updateStatus(null, "PB", dto.getStatusTypeCode(), dto.getStatusCode(), dto.getAppNo(), user);
			}

			con.commit();
			result = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt_status);
			ConnectionManager.close(rs_status);
			ConnectionManager.close(con);
		}
		return result;
	}

	@Override
	public List<DriverConductorBlacklistDTO> getPendingBlacklister(String status, String nic, String drivercondId) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<DriverConductorBlacklistDTO> dataList = new ArrayList<DriverConductorBlacklistDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM nt_t_blacklisted_driver_conductor b, nt_t_driver_conductor_registration r  "
					+ " WHERE r.dcr_nic = b.b_nic AND r.dcr_driver_conductor_id = b.b_driver_conductor_id "
					+ " AND b.b_status='A' ";
			;

			if (nic != null && !nic.trim().isEmpty())
				query += " AND b_nic = ? ";

			if (drivercondId != null && !drivercondId.trim().isEmpty())
				query += " AND b_driver_conductor_id = ? ";

			if (status != null && !status.isEmpty())
				query += "AND r.dcr_status = ? ";
			else
				query += "AND r.dcr_status = 'PB' ";

			query += " ORDER BY b_blacklist_date; ";

			int i = 0;
			ps = con.prepareStatement(query);

			if (nic != null && !nic.trim().isEmpty())
				ps.setString(++i, nic);

			if (drivercondId != null && !drivercondId.trim().isEmpty())
				ps.setString(++i, drivercondId);

			if (status != null && !status.isEmpty())
				ps.setString(++i, status);

			rs = ps.executeQuery();

			while (rs.next()) {
				DriverConductorBlacklistDTO dto = new DriverConductorBlacklistDTO();
				dto.setB_seq(rs.getLong("b_seq"));
				dto.setNic(rs.getString("b_nic"));
				dto.setDriverId(rs.getString("b_driver_conductor_id"));
				dto.setFullName(rs.getString("dcr_full_name_eng"));
				dto.setBlacklistType(rs.getString("b_blacklist_type"));
				dto.setBlacklistReason(rs.getString("b_blacklist_reason"));
				dto.setBlacklistDate(rs.getString("b_blacklist_date"));
				dto.setStatus(rs.getString("dcr_status"));
				dataList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return dataList;
	}

	@Override
	public boolean approvalBlacklister(String nic, String user) {
		Connection con = null;
		PreparedStatement stmt_status = null;
		ResultSet rs_status = null;
		boolean result = false;

		try {
			con = ConnectionManager.getConnection();

			// get application nos of according to nic
			String sql_status = "SELECT dcr_app_no,dcr_status  FROM nt_t_driver_conductor_registration WHERE dcr_nic = ?;";
			/**
			 * get dcr_status for update old status in blackListDeatil table by tharushi.e
			 **/
			stmt_status = con.prepareStatement(sql_status);
			stmt_status.setString(1, nic);
			rs_status = stmt_status.executeQuery();

			if (rs_status.next()) {
				String appNo = rs_status.getString("dcr_app_no");
				updateStatus(null, "B", null, "PB", appNo, user);

			}

			con.commit();
			result = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(stmt_status);
			ConnectionManager.close(rs_status);
			ConnectionManager.close(con);
		}
		return result;
	}

	@Override
	public boolean rejectBlacklisters(DriverConductorBlacklistDTO blacklisterDTO, String rejectReason, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt_status = null;
		ResultSet rs_status = null;
		boolean result = false;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			// invalidated from nt_t_blacklisted_driver_conductor A > I
			String sql = "UPDATE nt_t_blacklisted_driver_conductor SET b_status='I', b_reject_reason = ?, b_modified_by=?, b_modified_date=? WHERE b_nic = ? AND b_status = 'A';";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, rejectReason);
			stmt.setString(2, user);
			stmt.setTimestamp(3, timestamp);
			stmt.setString(4, blacklisterDTO.getNic());
			stmt.executeUpdate();

			// update Status in nt_t_driver_conductor_registration PB > previous status
			// TODO get previous status?
			String sql_status = "SELECT * FROM nt_t_driver_conductor_registration WHERE dcr_nic= ?;";

			stmt_status = con.prepareStatement(sql_status);
			stmt_status.setString(1, blacklisterDTO.getNic());
			rs_status = stmt_status.executeQuery();

			if (rs_status.next()) {
				DriverConductorTrainingDTO dto = new DriverConductorTrainingDTO();
				dto.setAppNo(rs_status.getString("dcr_app_no"));
				dto.setStatusTypeCode(rs_status.getString("dcr_status_type"));
				dto.setStatusCode(rs_status.getString("dcr_status"));

				updateStatus(null, "A", dto.getStatusTypeCode(), dto.getStatusCode(), dto.getAppNo(), user);
			}

			con.commit();
			result = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt_status);
			ConnectionManager.close(rs_status);
			ConnectionManager.close(con);
		}
		return result;
	}

	@Override
	public List<DriverConductorBlacklistDTO> getBlacklistedList(String status, String nic, String drivercondId) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<DriverConductorBlacklistDTO> dataList = new ArrayList<DriverConductorBlacklistDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM nt_t_blacklisted_driver_conductor b, nt_t_driver_conductor_registration r "
					+ " WHERE r.dcr_nic = b.b_nic AND r.dcr_driver_conductor_id = b.b_driver_conductor_id "
					+ " AND b.b_status='A' ";

			if (nic != null && !nic.trim().isEmpty())
				query += " AND b_nic = ? ";

			if (drivercondId != null && !drivercondId.trim().isEmpty())
				query += " AND b_driver_conductor_id = ? ";

			if (status != null && !status.isEmpty())
				query += "AND r.dcr_status = ? ";
			else
				query += "AND r.dcr_status = 'B' ";

			query += " ORDER BY b_blacklist_date; ";

			int i = 0;
			ps = con.prepareStatement(query);

			if (nic != null && !nic.trim().isEmpty())
				ps.setString(++i, nic);

			if (drivercondId != null && !drivercondId.trim().isEmpty())
				ps.setString(++i, drivercondId);

			if (status != null && !status.isEmpty())
				ps.setString(++i, status);

			rs = ps.executeQuery();

			while (rs.next()) {
				DriverConductorBlacklistDTO dto = new DriverConductorBlacklistDTO();
				dto.setB_seq(rs.getLong("b_seq"));
				dto.setNic(rs.getString("b_nic"));
				dto.setDriverId(rs.getString("b_driver_conductor_id"));
				dto.setFullName(rs.getString("dcr_full_name_eng"));
				dto.setBlacklistType(rs.getString("b_blacklist_type"));
				dto.setBlacklistReason(rs.getString("b_blacklist_reason"));
				dto.setBlacklistDate(rs.getString("b_blacklist_date"));
				dto.setStatus(rs.getString("dcr_status"));
				dataList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return dataList;
	}

	@Override
	public boolean clearanceBlacklist(String currentStatus, String nic, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt_status = null;
		ResultSet rs_status = null;
		boolean result = false;
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE nt_t_blacklisted_driver_conductor SET b_status='I', b_cleared_date = ?, b_modified_by=?, b_modified_date=? WHERE b_nic = ? AND b_status='A';";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, dateFormat.format(date));
			stmt.setString(2, user);
			stmt.setTimestamp(3, timestamp);
			stmt.setString(4, nic);
			stmt.executeUpdate();

			// get application numbers using nic and update status
			String sql_status = "SELECT dcr_app_no FROM nt_t_driver_conductor_registration WHERE dcr_nic = ?;";

			stmt_status = con.prepareStatement(sql_status);
			stmt_status.setString(1, nic);
			rs_status = stmt_status.executeQuery();

			if (rs_status.next()) {
				String appNo = rs_status.getString("dcr_app_no");

				updateStatus(null, "A", null, currentStatus, appNo, user);
			}

			con.commit();
			result = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt_status);
			ConnectionManager.close(rs_status);
			ConnectionManager.close(con);
		}
		return result;
	}

	@Override
	public String getVoucherNo(String appNo, String trainingType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String val = null;

		try {
			con = ConnectionManager.getConnection();

			String sql3 = "select dcv_voucher_no  from public.nt_m_driver_conduc_payment_voucher where dcv_application_no =? and dcv_training_type =? and dcv_approved_status!='R' ";

			ps = con.prepareStatement(sql3);
			ps.setString(1, appNo);
			ps.setString(2, trainingType);

			rs = ps.executeQuery();

			while (rs.next()) {

				val = rs.getString("dcv_voucher_no");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return val;
	}

	@Override
	public void updateStatusandStatusType(String statusType, String status, String appNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String sql3 = "UPDATE public.nt_t_driver_conductor_registration SET dcr_status_type =? , dcr_status = ? WHERE dcr_app_no  =? ";

			ps = con.prepareStatement(sql3);

			ps.setString(1, statusType);
			ps.setString(2, status);
			ps.setString(3, appNo);

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
	public String getApproveStatus(String dcId) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String status = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT dcr_status  FROM public.nt_t_driver_conductor_registration where dcr_driver_conductor_id =?";

			ps = con.prepareStatement(query);

			ps.setString(1, dcId);

			rs = ps.executeQuery();

			while (rs.next()) {

				status = rs.getString("dcr_status");

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
	public String getAppNoByDriverID(String driverID) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String status = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT dcr_app_no   FROM public.nt_t_driver_conductor_registration where dcr_driver_conductor_id =?";

			ps = con.prepareStatement(query);

			ps.setString(1, driverID);

			rs = ps.executeQuery();

			while (rs.next()) {

				status = rs.getString("dcr_app_no");

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
	public String getTrainingTypeDes(String trainingCode) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String status = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select description from nt_r_training_type where code = ?;";

			ps = con.prepareStatement(query);

			ps.setString(1, trainingCode);

			rs = ps.executeQuery();

			while (rs.next()) {

				status = rs.getString("description");

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
	public String getTransactionTypeDes(String code) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String status = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select description  from  public.nt_r_transaction_type where code =? and active ='Y';";

			ps = con.prepareStatement(query);

			ps.setString(1, code);

			rs = ps.executeQuery();

			while (rs.next()) {

				status = rs.getString("description");

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
	public List<DriverConductorRegistrationDTO> getDriverConducFilterListForReReg(String trainingType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;
		String Where_sql = null;

		List<DriverConductorRegistrationDTO> driverConducList = new ArrayList<DriverConductorRegistrationDTO>();

		try {
			con = ConnectionManager.getConnection();

			if (trainingType.equalsIgnoreCase("RD") || trainingType.equalsIgnoreCase("RRD")
					|| trainingType.equalsIgnoreCase("RRRD") || trainingType.equalsIgnoreCase("DD")
					|| trainingType.equalsIgnoreCase("FD")) {

				Where_sql = "and dcr_training_type in ('RD','RRD','RRRD','DD','FD','ND') ";
			}

			else {
				Where_sql = "and dcr_training_type in ('RC','RRC','RRRC','DC','FC','NC')";
			}

			query = "select distinct a.dcr_driver_conductor_id from (select distinct dcr_driver_conductor_id,dcr_training_type from  public.nt_t_driver_conductor_registration\r\n"
					+ "where dcr_status not in('I')\r\n" + Where_sql + " order by dcr_driver_conductor_id)a";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				DriverConductorRegistrationDTO dcDTO = new DriverConductorRegistrationDTO();

				dcDTO.setDriverConductorId(rs.getString("dcr_driver_conductor_id"));

				driverConducList.add(dcDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return driverConducList;

	}

	@Override
	public String getDriConducIdByNIC(String nic) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String rtnValue = "";

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT dcr_driver_conductor_id  FROM nt_t_driver_conductor_registration WHERE  dcr_nic =?";

			ps = con.prepareStatement(query);
			ps.setString(1, nic);
			rs = ps.executeQuery();

			while (rs.next()) {
				rtnValue = rs.getString("dcr_driver_conductor_id");
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return rtnValue;
	}

	public List<DriverConductorRegistrationDTO> getBySearcgPaymentCompletedDC(String trainingType, String nicNo,
			String dcId, String name) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<DriverConductorRegistrationDTO> data = new ArrayList<DriverConductorRegistrationDTO>();

		String WHERE_SQL = "";

		if (trainingType != null && !trainingType.equals("")) {
			WHERE_SQL = WHERE_SQL + " and a.dcr_training_type  = " + "'" + trainingType + "' ";
		}

		if (nicNo != null && !nicNo.equals("")) {

			WHERE_SQL = WHERE_SQL + " and a.dcr_nic  = " + "'" + nicNo + "' ";

		}
		if (dcId != null && !dcId.equals("")) {

			WHERE_SQL = WHERE_SQL + " and a.dcr_driver_conductor_id  = " + "'" + dcId + "' ";

		}
		if (name != null && !name.equals("")) {
			WHERE_SQL = WHERE_SQL + " and a.dcr_full_name_eng  = " + "'" + name + "' ";

		}
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT dcr_app_no,dcr_driver_conductor_id,dcr_full_name_eng, dcr_nic "
					+ " FROM public.nt_t_driver_conductor_registration a " + " WHERE a.dcr_status_type = 'R' "
					+ " AND a.dcr_status = 'TA'" + WHERE_SQL + " ORDER BY a.dcr_app_no";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				DriverConductorRegistrationDTO dcDTO = new DriverConductorRegistrationDTO();
				dcDTO.setAppNo(rs.getString("dcr_app_no"));
				dcDTO.setDriverConductorId(rs.getString("dcr_driver_conductor_id"));
				dcDTO.setFullName(rs.getString("dcr_full_name_eng"));
				dcDTO.setNic(rs.getString("dcr_nic"));
				data.add(dcDTO);

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

	public List<CommonDTO> getTempANICListN() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct dcr_nic FROM nt_t_driver_conductor_registration WHERE  dcr_status_type = 'R' and dcr_status = 'TA' ";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("dcr_nic"));

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
	public boolean getDataInDriverConductortemp(String nicNo, String dcId) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean dataHave = false;

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM public.nt_t_driver_conductor_temp_period where dcp_driver_conductor_id =? and dcp_nic =?";
			ps = con.prepareStatement(query);
			ps.setString(1, dcId);
			ps.setString(2, nicNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				dataHave = true;

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return dataHave;
	}

	@Override
	public String getTrainingTypeByAppNo(String appNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String trainingType = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT dcr_training_type   FROM public.nt_t_driver_conductor_registration where dcr_app_no =?";

			ps = con.prepareStatement(query);

			ps.setString(1, appNo);

			rs = ps.executeQuery();

			while (rs.next()) {

				trainingType = rs.getString("dcr_training_type");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return trainingType;

	}

	@Override
	public boolean receiptGenerated(String appNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean dataHave = false;

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT rec_receipt_no FROM public.nt_t_driver_conductor_receipt where   rec_application_no  =?";
			ps = con.prepareStatement(query);
			ps.setString(1, appNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				dataHave = true;

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return dataHave;
	}

	@Override
	public List<DriverConductorRegistrationDTO> getTrainingLanguageList() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;

		List<DriverConductorRegistrationDTO> trainingLanguage = new ArrayList<DriverConductorRegistrationDTO>();

		try {
			con = ConnectionManager.getConnection();

			query = "SELECT * FROM public.nt_r_language where active ='A'";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				DriverConductorRegistrationDTO dcDTO = new DriverConductorRegistrationDTO();

				dcDTO.setTrainingLanguageCode(rs.getString("code"));
				dcDTO.setTrainingLanguageDes(rs.getString("description"));
				trainingLanguage.add(dcDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return trainingLanguage;

	}

	@Override
	public List<DriverConductorRegistrationDTO> getReceiptNoForDriverConductor(String trainingType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;

		List<DriverConductorRegistrationDTO> receiptNoList = new ArrayList<DriverConductorRegistrationDTO>();

		try {
			con = ConnectionManager.getConnection();

			query = "SELECT * FROM public.nt_t_driver_conductor_receipt where rec_training_type=?";

			ps = con.prepareStatement(query);
			ps.setString(1, trainingType);
			rs = ps.executeQuery();

			while (rs.next()) {
				DriverConductorRegistrationDTO dcDTO = new DriverConductorRegistrationDTO();

				dcDTO.setReceiptNo(rs.getString("rec_receipt_no"));

				receiptNoList.add(dcDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return receiptNoList;

	}

	@Override
	public DriverConductorRegistrationDTO getDetailsByDCIdandTypeForView(String strDCId, String strType,
			String receiptNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query1 = null;
		String query2 = null;
		DriverConductorRegistrationDTO driverConductorRegistrationDTO = new DriverConductorRegistrationDTO();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();
			if (receiptNo != null && !receiptNo.isEmpty()) {
				query1 = "SELECT l.code,l.description,dcr_first_issue_date , dcr_license_expire_date  ,dcr_license_issue_date , dcr_seq, dcr_app_no, nt_r_training_type.description as  dcr_training_type, dcr_driver_conductor_id, dcr_nic, dcr_name_with_initials,"
						+ " dcr_full_name_eng, dcr_full_name_sin, dcr_full_name_tam, dcr_gender, dcr_dob, dcr_district, dcr_contact_no,"
						+ " dcr_license_no, dcr_date_of_issue, dcr_date_of_expiry, dcr_education, dcr_gs_certificate_no, "
						+ " dcr_police_clearance_station, dcr_vehicle_reg_no, dcr_permit_no, dcr_route_number, dcr_from, dcr_to, "
						+ " dcr_status, dcr_special_remarks,  dcr_add_1_eng, "
						+ " dcr_add_2_eng, dcr_city_eng, dcr_add_1_sin, dcr_add_2_sin, dcr_city_sin, dcr_add_1_tam, dcr_add_2_tam, "
						+ " dcr_city_tam, dcr_points, dcr_status_type,dcr_training_language,"
						+ " dcr_old_app_no, dcr_is_duplicate, dcr_gs_issue_date, dcr_police_cl_issue_date, dcr_medi_cer_no, dcr_medi_cer_issue_date,dcr_medi_cer_expire_date "
						+ " FROM public.nt_t_driver_conductor_registration inner join nt_r_training_type on nt_t_driver_conductor_registration.dcr_training_type = nt_r_training_type.code left outer join public.nt_r_language l on  nt_t_driver_conductor_registration.dcr_training_language = l.code "
						+ " inner join public.nt_t_driver_conductor_receipt r on nt_t_driver_conductor_registration.dcr_app_no = r.rec_application_no "
						+ " WHERE  dcr_training_type = ? and   r.rec_receipt_no ='" + receiptNo + "' ";

				ps = con.prepareStatement(query1);
				ps.setString(1, strType);

				rs = ps.executeQuery();
			} else {
				query2 = "SELECT l.code,l.description,dcr_first_issue_date , dcr_license_expire_date  ,dcr_license_issue_date , dcr_seq, dcr_app_no, nt_r_training_type.description as  dcr_training_type, dcr_driver_conductor_id, dcr_nic, dcr_name_with_initials,"
						+ " dcr_full_name_eng, dcr_full_name_sin, dcr_full_name_tam, dcr_gender, dcr_dob, dcr_district, dcr_contact_no,"
						+ " dcr_license_no, dcr_date_of_issue, dcr_date_of_expiry, dcr_education, dcr_gs_certificate_no, "
						+ " dcr_police_clearance_station, dcr_vehicle_reg_no, dcr_permit_no, dcr_route_number, dcr_from, dcr_to, "
						+ " dcr_status, dcr_special_remarks,  dcr_add_1_eng, "
						+ " dcr_add_2_eng, dcr_city_eng, dcr_add_1_sin, dcr_add_2_sin, dcr_city_sin, dcr_add_1_tam, dcr_add_2_tam, "
						+ " dcr_city_tam, dcr_points, dcr_status_type,dcr_training_language,"
						+ " dcr_old_app_no, dcr_is_duplicate, dcr_gs_issue_date, dcr_police_cl_issue_date, dcr_medi_cer_no, dcr_medi_cer_issue_date,dcr_medi_cer_expire_date "
						+ " FROM public.nt_t_driver_conductor_registration inner join nt_r_training_type on nt_t_driver_conductor_registration.dcr_training_type = nt_r_training_type.code left outer join public.nt_r_language l on  nt_t_driver_conductor_registration.dcr_training_language = l.code "
						+ " WHERE dcr_driver_conductor_id = ? AND dcr_training_type = ? order by dcr_created_date desc limit 1; ";

				ps = con.prepareStatement(query2);
				ps.setString(1, strDCId);
				ps.setString(2, strType);

				rs = ps.executeQuery();

			}

			while (rs.next()) {
				driverConductorRegistrationDTO.setTrainingLanguageCode(rs.getString("dcr_training_language"));
				driverConductorRegistrationDTO.setTrainingLanguageCode(rs.getString("code"));
				driverConductorRegistrationDTO.setTrainingLanguageDes(rs.getString("description"));
				driverConductorRegistrationDTO.setFirstDateOfIssueN(rs.getString("dcr_first_issue_date"));
				driverConductorRegistrationDTO.setDateOfIssueLicenceN(rs.getString("dcr_license_issue_date"));
				driverConductorRegistrationDTO.setDateOfExpiryLicenceN(rs.getString("dcr_license_expire_date"));
				driverConductorRegistrationDTO.setSeq(rs.getLong("dcr_seq"));
				driverConductorRegistrationDTO.setAppNo(rs.getString("dcr_app_no"));
				driverConductorRegistrationDTO.setNewtrainingType(rs.getString("dcr_training_type"));
				driverConductorRegistrationDTO.setDriverConductorId(rs.getString("dcr_driver_conductor_id"));
				driverConductorRegistrationDTO.setNic(rs.getString("dcr_nic"));
				driverConductorRegistrationDTO.setNameWithInitials(rs.getString("dcr_name_with_initials"));
				driverConductorRegistrationDTO.setFullNameEng(rs.getString("dcr_full_name_eng"));
				driverConductorRegistrationDTO.setFullNameSin(rs.getString("dcr_full_name_sin"));
				driverConductorRegistrationDTO.setFullNameTam(rs.getString("dcr_full_name_tam"));
				driverConductorRegistrationDTO.setNewGender(rs.getString("dcr_gender"));

				Date dateDob = formatter.parse(rs.getString("dcr_dob"));
				if (dateDob != null) {
					driverConductorRegistrationDTO.setDob(dateDob);
				}
				driverConductorRegistrationDTO.setNewDistrict(rs.getString("dcr_district"));
				driverConductorRegistrationDTO.setContactNo(rs.getString("dcr_contact_no"));
				driverConductorRegistrationDTO.setLicenseNo(rs.getString("dcr_license_no"));

				driverConductorRegistrationDTO.setDateOfIssueN(rs.getString("dcr_date_of_issue")); // dateOfIssue
				driverConductorRegistrationDTO.setExpiryDateN(rs.getString("dcr_date_of_expiry"));// dateOfExpiry

				driverConductorRegistrationDTO.setEducation(rs.getString("dcr_education"));
				driverConductorRegistrationDTO.setGsCertificateNo(rs.getString("dcr_gs_certificate_no"));
				driverConductorRegistrationDTO.setPoliceClearanceStation(rs.getString("dcr_police_clearance_station"));
				driverConductorRegistrationDTO.setVehicleRegNo(rs.getString("dcr_vehicle_reg_no"));
				driverConductorRegistrationDTO.setPermitNo(rs.getString("dcr_permit_no"));
				driverConductorRegistrationDTO.setRouteNo(rs.getString("dcr_route_number"));
				driverConductorRegistrationDTO.setFrom(rs.getString("dcr_from"));
				driverConductorRegistrationDTO.setTo(rs.getString("dcr_to"));
				driverConductorRegistrationDTO.setStatus(rs.getString("dcr_status"));
				driverConductorRegistrationDTO.setSpecialRemarks(rs.getString("dcr_special_remarks"));

				driverConductorRegistrationDTO.setAdd1Eng(rs.getString("dcr_add_1_eng"));
				driverConductorRegistrationDTO.setAdd2Eng(rs.getString("dcr_add_2_eng"));
				driverConductorRegistrationDTO.setCityEng(rs.getString("dcr_city_eng"));
				driverConductorRegistrationDTO.setAdd1Sin(rs.getString("dcr_add_1_sin"));
				driverConductorRegistrationDTO.setAdd2Sin(rs.getString("dcr_add_2_sin"));
				driverConductorRegistrationDTO.setCitySin(rs.getString("dcr_city_sin"));
				driverConductorRegistrationDTO.setAdd1Tam(rs.getString("dcr_add_1_tam"));
				driverConductorRegistrationDTO.setAdd2Tam(rs.getString("dcr_add_2_tam"));
				driverConductorRegistrationDTO.setCityTam(rs.getString("dcr_city_tam"));
				driverConductorRegistrationDTO.setPoints(rs.getInt("dcr_points"));
				driverConductorRegistrationDTO.setStatusType(rs.getString("dcr_status_type"));
				driverConductorRegistrationDTO.setOldApp(rs.getString("dcr_old_app_no"));
				driverConductorRegistrationDTO.setIsDuplicate(rs.getString("dcr_is_duplicate"));
				if (rs.getString("dcr_gs_issue_date") != null) {
					Date gsIssueDate = formatter.parse(rs.getString("dcr_gs_issue_date"));

					if (gsIssueDate != null) {
						driverConductorRegistrationDTO.setGscissuedate(gsIssueDate);
					}
				}
				if (rs.getString("dcr_police_cl_issue_date") != null) {
					Date pcIssueDate = formatter.parse(rs.getString("dcr_police_cl_issue_date"));
					if (pcIssueDate != null) {
						driverConductorRegistrationDTO.setPcissuedate(pcIssueDate);
					}
				}
				driverConductorRegistrationDTO.setMedicalcertificateNo(rs.getString("dcr_medi_cer_no"));
				if (rs.getString("dcr_medi_cer_issue_date") != null) {
					Date mediCerIssueDate = formatter.parse(rs.getString("dcr_medi_cer_issue_date"));
					if (mediCerIssueDate != null) {
						driverConductorRegistrationDTO.setMrissuedate(mediCerIssueDate);
					}
				}
				if (rs.getString("dcr_medi_cer_expire_date") != null) {
					Date mediCerExpireDate = formatter.parse(rs.getString("dcr_medi_cer_expire_date"));
					if (mediCerExpireDate != null) {
						driverConductorRegistrationDTO.setMrexpiredate(mediCerExpireDate);
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
		return driverConductorRegistrationDTO;
	}

	@Override
	public String getNoOfDuplicateTraining(String trainingType, String nicNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String rtnValue = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select count(dcr_training_type) as dcr_training_type from public.nt_t_driver_conductor_registration where dcr_training_type =? and dcr_nic =? ";

			ps = con.prepareStatement(query);
			ps.setString(1, trainingType);
			ps.setString(2, nicNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				rtnValue = rs.getString("dcr_training_type");
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return rtnValue;
	}

	@Override
	public List<CommonDTO> getPendingIDListForView() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct dcr_nic FROM nt_t_driver_conductor_registration  ";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("dcr_nic"));

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
	public List<CommonDTO> getPendingIDListForViewByTrainingType(String trainingType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT dcr_nic FROM nt_t_driver_conductor_registration WHERE  dcr_training_type = ?  ";

			ps = con.prepareStatement(query);
			ps.setString(1, trainingType);

			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("dcr_nic"));

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
	public List<CommonDTO> getPendingDCIDListForView() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT dcr_driver_conductor_id FROM nt_t_driver_conductor_registration  ORDER BY dcr_driver_conductor_id ";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("dcr_driver_conductor_id"));

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
	public List<CommonDTO> getPendingDCIDListByTrainingTypeForView(String trainingType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT dcr_driver_conductor_id FROM nt_t_driver_conductor_registration WHERE  dcr_training_type = ? ORDER BY dcr_driver_conductor_id ";

			ps = con.prepareStatement(query);
			ps.setString(1, trainingType);

			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("dcr_driver_conductor_id"));

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
	public List<CommonDTO> getPendingDCIDListByTrainingandIDForView(String trainingType, String idNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String WHERE_SQL = "";

		if (trainingType != null && !trainingType.equals("") && (idNo == null || idNo.equals(""))) {
			WHERE_SQL = " WHERE dcr_training_type = " + "'" + trainingType + "'";
		}
		if (idNo != null && !idNo.equals("") && (trainingType == null || trainingType.equals(""))) {
			WHERE_SQL = " AND dcr_nic = " + "'" + idNo + "'";
		}
		if (trainingType != null && !trainingType.equals("") && idNo != null && !idNo.equals("")) {
			WHERE_SQL = " WHERE dcr_training_type = " + "'" + trainingType + "'" + " AND dcr_nic = " + "'" + idNo + "'";
		}

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT dcr_driver_conductor_id FROM nt_t_driver_conductor_registration  " + WHERE_SQL;

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("dcr_driver_conductor_id"));

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

	public List<CommonDTO> GetAllTrainingTypesWithoutDuplicate() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description, points, type "
					+ "FROM nt_r_training_type WHERE code not in ('DD','DC') ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("code"));
				commonDTO.setDescription(rs.getString("description"));
				commonDTO.setPoints(rs.getInt("points"));
				commonDTO.setType(rs.getString("type"));

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
	public void updateNewNic(String nic, String oldAppno, String oldNic, String loginUser, boolean photoHave) {

		Connection con = null;
		PreparedStatement stmt = null, stmt1 = null, stmt2 = null, stmt3 = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timeStamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			String sql1 = "INSERT into public.nt_h_driver_conductor_registration  "
					+ " (SELECT * FROM public.nt_t_driver_conductor_registration WHERE dcr_nic = ? AND dcr_app_no =?) ";

			stmt1 = con.prepareStatement(sql1);
			stmt1.setString(1, oldNic);
			stmt1.setString(2, oldAppno);
			stmt1.executeUpdate();

			String sql = "UPDATE public.nt_t_driver_conductor_registration "
					+ " SET  dcr_nic=?, dcr_modified_by = ? , dcr_modified_date =?  " + " WHERE dcr_app_no=? ";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, nic);
			stmt.setString(2, loginUser);
			stmt.setTimestamp(3, timeStamp);
			stmt.setString(4, oldAppno);

			stmt.executeUpdate();
			if (photoHave) {

				String sql2 = "INSERT into public.nt_h_driver_conductor_photoupload  "
						+ " (SELECT * FROM public.nt_t_driver_conductor_photoupload WHERE dcp_nic_no = ? AND  dcp_app_no =?) ";

				stmt2 = con.prepareStatement(sql2);
				stmt2.setString(1, oldNic);
				stmt2.setString(2, oldAppno);
				stmt2.executeUpdate();

				String sql3 = "UPDATE public.nt_t_driver_conductor_photoupload "
						+ " SET  dcp_nic_no=?,  created_by  = ? , created_date  =?  " + " WHERE dcp_app_no=? ";

				stmt3 = con.prepareStatement(sql3);

				stmt3.setString(1, nic);
				stmt3.setString(2, loginUser);
				stmt3.setTimestamp(3, timeStamp);
				stmt3.setString(4, oldAppno);

				stmt3.executeUpdate();

			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt1);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(stmt3);
			ConnectionManager.close(con);
		}

	}

	@Override
	public void updateNICInPhotoUpload(String oldNIC, String newNIC, String appNo, String loginUser) {

		Connection con = null;
		PreparedStatement stmt = null, stmt1 = null, stmt2 = null, stmt3 = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timeStamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			String sql2 = "INSERT into public.nt_h_driver_conductor_photoupload  "
					+ " (SELECT * FROM public.nt_t_driver_conductor_photoupload WHERE dcp_nic_no = ? AND  dcp_app_no =?) ";

			stmt2 = con.prepareStatement(sql2);
			stmt2.setString(1, oldNIC);
			stmt2.setString(2, appNo);
			stmt2.executeUpdate();

			String sql3 = "UPDATE public.nt_t_driver_conductor_photoupload "
					+ " SET  dcp_nic_no=?,  created_by  = ? , created_date  =?  " + " WHERE dcp_app_no=? ";

			stmt3 = con.prepareStatement(sql3);

			stmt3.setString(1, newNIC);
			stmt3.setString(2, loginUser);
			stmt3.setTimestamp(3, timeStamp);
			stmt3.setString(4, appNo);

			stmt3.executeUpdate();

			con.commit();
			

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt1);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(stmt3);
			ConnectionManager.close(con);
		}

	}

	@Override
	public boolean updateStatusAndInactivePrevious(String statusType, String status, String oldStatusType,
			String oldStatus, String appNo, String user, String driverConducId) {
		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt_h = null;
		PreparedStatement stmt_P = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		boolean result = false;

		try {
			con = ConnectionManager.getConnection();

			// update history
			String query_h = "UPDATE nt_h_driver_conductor_registration SET  ";

			if (oldStatusType != null)
				query_h += " dcr_status_type = ?, ";

			if (oldStatus != null)
				query_h += " dcr_status = ?, ";

			query_h += " dcr_modified_by = ?, dcr_modified_date = ? WHERE dcr_app_no = ?";

			stmt_h = con.prepareStatement(query_h);
			int i = 0;

			if (oldStatusType != null)
				stmt_h.setString(++i, oldStatusType);
			if (oldStatus != null)
				stmt_h.setString(++i, oldStatus);
			stmt_h.setString(++i, user);
			stmt_h.setTimestamp(++i, timestamp);
			stmt_h.setString(++i, appNo);
			stmt_h.executeUpdate();

			if (status != null && status.equals("A")) {

				String query_P = "UPDATE nt_t_driver_conductor_registration SET dcr_status = ?,dcr_modified_by = ?, dcr_modified_date = ? WHERE dcr_driver_conductor_id = ? and dcr_status= 'A'  ";

				stmt_P = con.prepareStatement(query_P);
				i = 0;

				if (status != null)
					stmt_P.setString(1, "I");
				stmt_P.setString(2, user);
				stmt_P.setTimestamp(3, timestamp);
				stmt_P.setString(4, driverConducId);
				stmt_P.executeUpdate();
			}

			String query = "UPDATE nt_t_driver_conductor_registration SET  ";

			if (statusType != null)
				query += " dcr_status_type = ?, ";

			if (status != null)
				query += " dcr_status = ?, ";

			query += " dcr_modified_by = ?, dcr_modified_date = ? WHERE dcr_app_no = ?";

			stmt = con.prepareStatement(query);
			i = 0;

			if (statusType != null)
				stmt.setString(++i, statusType);
			if (status != null)
				stmt.setString(++i, status);
			stmt.setString(++i, user);
			stmt.setTimestamp(++i, timestamp);
			stmt.setString(++i, appNo);
			stmt.executeUpdate();

			con.commit();
			result = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt_P);
			ConnectionManager.close(stmt_h);
			ConnectionManager.close(con);
		}
		return result;
	}

	@Override
	public List<CommonDTO> getTrainingTypeByNic(String nic) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct m.dcr_training_type as code, r.description as description,points, type \r\n"
					+ "FROM public.nt_t_driver_conductor_registration m\r\n"
					+ "inner join nt_r_training_type r on r.code  = m.dcr_training_type \r\n" + "WHERE dcr_nic =?";

			ps = con.prepareStatement(query);
			ps.setString(1, nic);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("code"));
				commonDTO.setDescription(rs.getString("description"));
				commonDTO.setPoints(rs.getInt("points"));
				commonDTO.setType(rs.getString("type"));

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
	public List<CommonDTO> getAllIDListForViewByTrainingType(String trainingType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct dcr_nic FROM nt_t_driver_conductor_registration WHERE dcr_training_type = ?  ";

			ps = con.prepareStatement(query);
			ps.setString(1, trainingType);

			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("dcr_nic"));

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
	public List<CommonDTO> getAllDCIDListByTrainingType(String trainingType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct dcr_driver_conductor_id FROM nt_t_driver_conductor_registration WHERE  dcr_training_type = ? ORDER BY dcr_driver_conductor_id ";

			ps = con.prepareStatement(query);
			ps.setString(1, trainingType);

			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("dcr_driver_conductor_id"));

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
	public List<CommonDTO> getDCIDListByTrainingandID(String trainingType, String idNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String WHERE_SQL = "";

		if (trainingType != null && !trainingType.equals("")) {
			WHERE_SQL = " WHERE dcr_training_type = " + "'" + trainingType + "'";
		}
		if (idNo != null && !idNo.equals("")) {
			WHERE_SQL = " WHERE dcr_nic = " + "'" + idNo + "'";
		}
		if (trainingType != null && !trainingType.equals("") && idNo != null && !idNo.equals("")) {
			WHERE_SQL = " WHERE dcr_training_type = " + "'" + trainingType + "'" + " AND dcr_nic = " + "'" + idNo + "'";
		}

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT dcr_driver_conductor_id FROM nt_t_driver_conductor_registration  " + WHERE_SQL;

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("dcr_driver_conductor_id"));

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
	public boolean updateDriverConductorByHighAuthOfficer(DriverConductorRegistrationDTO driverConductorRegistrationDTO,
			String strSelectedType) {

		boolean isUpdated = false;
		Connection con = null;
		PreparedStatement stmt = null, stmt1 = null;
		ResultSet rs = null;

		String dateofissueN = null;
		String dateofexpiry = null;
		String dateofFirstissueN = null;
		String gsIssueDate = null;
		String pcIssueDate = null;
		String mediReIssueDate = null;
		String mediReportExpireDate = null;
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date date = new java.util.Date();
		Timestamp timeStamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			String dob = dateFormat.format(driverConductorRegistrationDTO.getDob());
			if (driverConductorRegistrationDTO.getGscissuedate() != null) {
				gsIssueDate = dateFormat.format(driverConductorRegistrationDTO.getGscissuedate());
			}
			if (driverConductorRegistrationDTO.getPcissuedate() != null) {
				pcIssueDate = dateFormat.format(driverConductorRegistrationDTO.getPcissuedate());
			}
			if (driverConductorRegistrationDTO.getMrissuedate() != null) {
				mediReIssueDate = dateFormat.format(driverConductorRegistrationDTO.getMrissuedate());
			}
			if (driverConductorRegistrationDTO.getMrexpiredate() != null) {
				mediReportExpireDate = dateFormat.format(driverConductorRegistrationDTO.getMrexpiredate());
			}

			if (driverConductorRegistrationDTO.getDateOfIssue() != null) {
				dateofissueN = dateFormat.format(driverConductorRegistrationDTO.getDateOfIssue());
			}

			if (driverConductorRegistrationDTO.getExpiryDate() != null) {
				dateofexpiry = dateFormat.format(driverConductorRegistrationDTO.getExpiryDate());
			}
			if (driverConductorRegistrationDTO.getFirstDateOfIssue() != null) {
				dateofFirstissueN = dateFormat.format(driverConductorRegistrationDTO.getFirstDateOfIssue());
			}

			String sql1 = "INSERT into public.nt_h_driver_conductor_registration  "
					+ " (SELECT * FROM public.nt_t_driver_conductor_registration WHERE dcr_driver_conductor_id = ? AND dcr_training_type =?) ";

			stmt1 = con.prepareStatement(sql1);
			stmt1.setString(1, driverConductorRegistrationDTO.getDriverConductorId());
			stmt1.setString(2, strSelectedType);
			stmt1.executeUpdate();

			String sql = "UPDATE public.nt_t_driver_conductor_registration "
					+ " SET  dcr_driver_conductor_id=?, dcr_nic=?, "
					+ " dcr_name_with_initials=?, dcr_full_name_eng=?, dcr_full_name_sin=?, dcr_full_name_tam=?, "
					+ " dcr_gender=?, dcr_dob=?, dcr_district=?, dcr_contact_no=?, dcr_license_no=?, dcr_date_of_issue=?,"
					+ " dcr_date_of_expiry=?, dcr_education=?, dcr_gs_certificate_no=?, dcr_police_clearance_station=?, "
					+ " dcr_vehicle_reg_no=?, dcr_permit_no=?, dcr_route_number=?, dcr_from=?, dcr_to=?, "
					+ " dcr_special_remarks=?, dcr_photo_uploaded=?, dcr_schedule_id=?, "
					+ " dcr_add_1_eng=?, dcr_add_2_eng=?, dcr_city_eng=?, dcr_add_1_sin=?, dcr_add_2_sin=?, "
					+ "	dcr_city_sin=?, dcr_add_1_tam=?, dcr_add_2_tam=?, dcr_city_tam=?, "
					+ " dcr_modified_date=?, dcr_modified_by=?, dcr_points=?,"
					+ " dcr_gs_issue_date=? ,dcr_police_cl_issue_date=? ,dcr_medi_cer_no=? ,dcr_medi_cer_issue_date=? ,dcr_medi_cer_expire_date=?, dcr_first_issue_date =?,dcr_training_language= ?,dcr_update_by_high_auth_officer =?"
					+ " WHERE dcr_app_no=? ";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, driverConductorRegistrationDTO.getDriverConductorId());
			stmt.setString(2, driverConductorRegistrationDTO.getNic());
			stmt.setString(3, driverConductorRegistrationDTO.getNameWithInitials());
			stmt.setString(4, driverConductorRegistrationDTO.getFullNameEng());
			stmt.setString(5, driverConductorRegistrationDTO.getFullNameSin());
			stmt.setString(6, driverConductorRegistrationDTO.getFullNameTam());
			stmt.setString(7, driverConductorRegistrationDTO.getNewGender());
			stmt.setString(8, dob);
			stmt.setString(9, driverConductorRegistrationDTO.getNewDistrict());
			stmt.setString(10, driverConductorRegistrationDTO.getContactNo());
			stmt.setString(11, driverConductorRegistrationDTO.getLicenseNo());
			stmt.setString(12, dateofissueN);
			stmt.setString(13, dateofexpiry);
			stmt.setString(14, driverConductorRegistrationDTO.getEducation());
			stmt.setString(15, driverConductorRegistrationDTO.getGsCertificateNo());
			stmt.setString(16, driverConductorRegistrationDTO.getPoliceClearanceStation());
			stmt.setString(17, driverConductorRegistrationDTO.getVehicleRegNo());
			stmt.setString(18, driverConductorRegistrationDTO.getPermitNo());
			stmt.setString(19, driverConductorRegistrationDTO.getRouteNo());
			stmt.setString(20, driverConductorRegistrationDTO.getFrom());
			stmt.setString(21, driverConductorRegistrationDTO.getTo());
			stmt.setString(22, driverConductorRegistrationDTO.getSpecialRemarks());
			stmt.setString(23, driverConductorRegistrationDTO.getPhotoUploaded());
			stmt.setLong(24, driverConductorRegistrationDTO.getScheduleId());
			stmt.setString(25, driverConductorRegistrationDTO.getAdd1Eng());
			stmt.setString(26, driverConductorRegistrationDTO.getAdd2Eng());
			stmt.setString(27, driverConductorRegistrationDTO.getCityEng());
			stmt.setString(28, driverConductorRegistrationDTO.getAdd1Sin());
			stmt.setString(29, driverConductorRegistrationDTO.getAdd2Sin());
			stmt.setString(30, driverConductorRegistrationDTO.getCitySin());
			stmt.setString(31, driverConductorRegistrationDTO.getAdd1Tam());
			stmt.setString(32, driverConductorRegistrationDTO.getAdd2Tam());
			stmt.setString(33, driverConductorRegistrationDTO.getCityTam());
			stmt.setTimestamp(34, timeStamp);
			stmt.setString(35, driverConductorRegistrationDTO.getModifiedBy());
			stmt.setInt(36, driverConductorRegistrationDTO.getPoints());

			stmt.setString(37, gsIssueDate);
			stmt.setString(38, pcIssueDate);
			stmt.setString(39, driverConductorRegistrationDTO.getMedicalcertificateNo());
			stmt.setString(40, mediReIssueDate);
			stmt.setString(41, mediReportExpireDate);
			stmt.setString(42, dateofFirstissueN);
			stmt.setString(43, driverConductorRegistrationDTO.getTrainingLanguageCode());
			stmt.setString(44, driverConductorRegistrationDTO.getUpdateByHighAuth());
			stmt.setString(45, driverConductorRegistrationDTO.getAppNo());

			int i = stmt.executeUpdate();

			if (i > 0) {
				isUpdated = true;
			} else {
				isUpdated = false;
			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
		return isUpdated;

	}

	@Override
	public int checkAlreadyActiveTrainingForNIC(String nicNo) {
		int count = 0;

		String countQuery = "SELECT COUNT(*)  FROM nt_t_driver_conductor_registration "
				+ " WHERE dcr_nic = ? and dcr_status not in('TA', 'A', 'I', 'PB', 'B')  ";

		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(countQuery);) {

			preparedStatement.setString(1, nicNo);

			ResultSet resultSet = preparedStatement.executeQuery();
			connection.commit();

			if (resultSet.next()) {
				count = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			count= -1;
		}

		return count;
	}

	@Override
	public int checkBlackListCount(String nicNo, String status) {
		int count = 0;

		String countQuery = "SELECT COUNT(*)  FROM nt_t_driver_conductor_registration "
				+ " WHERE dcr_nic = ? and dcr_status in('" + status + "')  ";

		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(countQuery);) {

			preparedStatement.setString(1, nicNo);

			ResultSet resultSet = preparedStatement.executeQuery();
			connection.commit();

			if (resultSet.next()) {
				count = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			count= -1;
		}

		return count;
	}

	private void insertTaskInquiryRecord(Connection con, DriverConductorRegistrationDTO registrationDto,
			Timestamp timestamp, String status, String function,String user, String functiondes) {
		
		try {
			String insertSQL = null;
			PreparedStatement psInsert = null;

			insertSQL = "INSERT INTO public.nt_t_task_inquiry (created_by, created_date, status, bus_no, dc_id_num, permit_no,function_name,function_des,nic_no,app_no) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			psInsert = con.prepareStatement(insertSQL);

			psInsert.setString(1, user);
			psInsert.setTimestamp(2, timestamp);
			psInsert.setString(3, status);
			psInsert.setString(4, registrationDto.getVehicleRegNo());
			psInsert.setString(5, registrationDto.getDriverConductorId());
			psInsert.setString(6, registrationDto.getPermitNo());
			psInsert.setString(7, function);
			psInsert.setString(8, functiondes);
			psInsert.setString(9, registrationDto.getNic());
			psInsert.setString(10, registrationDto.getAppNo());

			psInsert.executeUpdate();
			con.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public void beanLinkMethod(DriverConductorRegistrationDTO registrationDto,String user,String des,String funDes) {
		Connection con = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();
			
			insertTaskInquiryRecord(con, registrationDto, timestamp, des, "Driver Conductor Management",user,funDes);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	private void insertTaskInquiryRecord(Connection con, MaintainTrainingScheduleDTO  trainingScheduleDTO,
			Timestamp timestamp, String status, String function,String user, String functiondes) {
		
		try {
			String insertSQL = null;
			PreparedStatement psInsert = null;

			insertSQL = "INSERT INTO public.nt_t_task_inquiry (created_by, created_date, status, schedule_date,function_name,function_des) "
					+ "VALUES(?, ?, ?, ?, ?, ?)";

			psInsert = con.prepareStatement(insertSQL);

			psInsert.setString(1, user);
			psInsert.setTimestamp(2, timestamp);
			psInsert.setString(3, status);
			psInsert.setTimestamp(4, new java.sql.Timestamp(trainingScheduleDTO.getTrainingDate().getTime()));
			psInsert.setString(5, function);
			psInsert.setString(6, functiondes);

			psInsert.executeUpdate();
			con.commit();

		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public void beanLinkMethod(MaintainTrainingScheduleDTO  trainingScheduleDTO,String user,String des,String funDes) {
		Connection con = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		
		try {
			con = ConnectionManager.getConnection();
			
			insertTaskInquiryRecord(con, trainingScheduleDTO, timestamp, des, "Driver Conductor Management",user,funDes);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	private void insertTaskInquiryRecord(Connection con, DriverConductorTrainingDTO trainingDto,
			Timestamp timestamp, String status, String function,String user, String functiondes) {
		
		try {
			String insertSQL = null;
			PreparedStatement psInsert = null;

			insertSQL = "INSERT INTO public.nt_t_task_inquiry (created_by, created_date, status, dc_id_num,function_name,function_des,nic_no,app_no) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

			psInsert = con.prepareStatement(insertSQL);

			psInsert.setString(1, user);
			psInsert.setTimestamp(2, timestamp);
			psInsert.setString(3, status);
			psInsert.setString(4, trainingDto.getDriverConductorId());
			psInsert.setString(5, function);
			psInsert.setString(6, functiondes);
			psInsert.setString(7, trainingDto.getNic());
			psInsert.setString(8, trainingDto.getAppNo());

			psInsert.executeUpdate();
			con.commit();

		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public void beanLinkMethod(DriverConductorTrainingDTO  trainingDto,String user,String des,String funDes) {
		Connection con = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		
		try {
			con = ConnectionManager.getConnection();
			
			insertTaskInquiryRecord(con, trainingDto, timestamp, des, "Driver Conductor Management",user,funDes);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	private void insertTaskInquiryRecord(Connection con, DriverConductorBlacklistDTO blacklisterDTO,
			Timestamp timestamp, String status, String function,String user, String functiondes) {
		
		try {
			String insertSQL = null;
			PreparedStatement psInsert = null;

			insertSQL = "INSERT INTO public.nt_t_task_inquiry (created_by, created_date, status, function_name,function_des,nic_no) "
					+ "VALUES(?, ?, ?, ?, ?, ?)";

			psInsert = con.prepareStatement(insertSQL);

			psInsert.setString(1, user);
			psInsert.setTimestamp(2, timestamp);
			psInsert.setString(3, status);
			psInsert.setString(4, function);
			psInsert.setString(5, functiondes);
			psInsert.setString(6, blacklisterDTO.getNic());

			psInsert.executeUpdate();
			con.commit();

		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public void beanLinkMethod(DriverConductorBlacklistDTO blacklisterDTO, String user, String des, String funDes) {
		Connection con = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		
		try {
			con = ConnectionManager.getConnection();
			
			insertTaskInquiryRecord(con, blacklisterDTO, timestamp, des, "Driver Conductor Management",user,funDes);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	@Override
	public List<CommonInquiryDTO> getNICNoListForCommonInquiry(){
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CommonInquiryDTO> dataList = new ArrayList<CommonInquiryDTO>();
		
		try {
			con = ConnectionManager.getConnection();
			
			String sql = null;
			sql = "SELECT DISTINCT nic_no FROM public.nt_t_task_inquiry WHERE function_name = 'Driver Conductor Management'";
			
			ps = con.prepareStatement(sql);
			
			rs = ps.executeQuery();
		
			while(rs.next()) {
				CommonInquiryDTO data = new CommonInquiryDTO();
				if(rs.getString("nic_no") != null && !rs.getString("nic_no").isEmpty()) {
					data.setNicNo(rs.getString("nic_no"));
					dataList.add(data);
				}
				
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		
		
		return dataList;
	}
	
	
	@Override
	public List<CommonInquiryDTO> getAppNoListForCommonInquiry(){
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CommonInquiryDTO> dataList = new ArrayList<CommonInquiryDTO>();
		
		try {
			con = ConnectionManager.getConnection();
			
			String sql = null;
			sql = "SELECT DISTINCT app_no FROM public.nt_t_task_inquiry WHERE function_name = 'Driver Conductor Management'";
			
			ps = con.prepareStatement(sql);
			
			rs = ps.executeQuery();
		
			while(rs.next()) {
				CommonInquiryDTO data = new CommonInquiryDTO();
				if(rs.getString("app_no") != null && !rs.getString("app_no").isEmpty()) {
					data.setAppNo(rs.getString("app_no"));
					dataList.add(data);
				}
				
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		
		return dataList;
	}
	
	@Override
	public List<CommonInquiryDTO> getDriConNoListForCommonInquiry(){
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CommonInquiryDTO> dataList = new ArrayList<CommonInquiryDTO>();
		
		try {
			con = ConnectionManager.getConnection();
			
			String sql = null;
			sql = "SELECT DISTINCT dc_id_num FROM public.nt_t_task_inquiry WHERE function_name = 'Driver Conductor Management'";
			
			ps = con.prepareStatement(sql);
			
			rs = ps.executeQuery();
		
			while(rs.next()) {
				CommonInquiryDTO data = new CommonInquiryDTO();
				if(rs.getString("dc_id_num") != null && !rs.getString("dc_id_num").isEmpty()) {
					data.setDriverConductorId(rs.getString("dc_id_num"));
					dataList.add(data);
				}
				
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		
		return dataList;
	}
	
	
	@Override
	public List<CommonInquiryDTO> getBusNoListForCommonInquiry(){
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CommonInquiryDTO> dataList = new ArrayList<CommonInquiryDTO>();
		
		try {
			con = ConnectionManager.getConnection();
			
			String sql = null;
			sql = "SELECT DISTINCT bus_no FROM public.nt_t_task_inquiry WHERE function_name = 'Driver Conductor Management'";
			
			ps = con.prepareStatement(sql);
			
			rs = ps.executeQuery();
		
			while(rs.next()) {
				CommonInquiryDTO data = new CommonInquiryDTO();
				if(rs.getString("bus_no") != null && !rs.getString("bus_no").isEmpty()) {
					data.setBusNo(rs.getString("bus_no"));
					dataList.add(data);
				}
				
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		
		return dataList;
	}
	
	
	@Override
	public List<CommonInquiryDTO> searchDataForCommonInquiry(String NIC, String appNo, String busNo, String dcId) {
		Connection con = null;
		PreparedStatement ps= null;
		ResultSet rs = null;
		List<CommonInquiryDTO> dataList = new ArrayList<CommonInquiryDTO>();
		
		try {
			con = ConnectionManager.getConnection();

			if((NIC != null && appNo != "" && busNo != "" && dcId !="" && NIC != "")) {
				String sql = null;
				sql = "SELECT * FROM public.nt_t_task_inquiry WHERE function_name = 'Driver Conductor Management' "
						+ "AND (nic_no = ? AND app_no = ? AND bus_no = ? AND dc_id_num = ?)";
				
				ps = con.prepareStatement(sql);
				ps.setString(1, NIC);
				ps.setString(2, appNo);
				ps.setString(3, busNo);
				ps.setString(4, dcId);
				
				rs = ps.executeQuery();
				
				while(rs.next()) {
					CommonInquiryDTO data = new CommonInquiryDTO();
					
					data.setPermitNo(rs.getString("permit_no"));
					data.setBusNo(rs.getString("bus_no"));
					data.setNicNo(rs.getString("nic_no"));
					data.setStatusDes(rs.getString("status"));
					data.setCreatedBy(rs.getString("created_by"));
					data.setCreatedDate(rs.getTimestamp("created_date"));
					data.setFunctionDes(rs.getString("function_des"));
					data.setDriverConductorId(rs.getString("dc_id_num"));
					data.setAppNo(rs.getString("app_no"));
					
					dataList.add(data);
				}
			}else if((NIC != null && appNo !="" && busNo !=""  && NIC != "")){
				String sql = null;
				sql = "SELECT * FROM public.nt_t_task_inquiry WHERE function_name = 'Driver Conductor Managementt' "
						+ "AND (nic_no = ? AND app_no = ? AND bus_no = ?)";
				
				ps = con.prepareStatement(sql);
				ps.setString(1, NIC);
				ps.setString(2, appNo);
				ps.setString(3, busNo);
				
				rs = ps.executeQuery();
				
				while(rs.next()) {
					CommonInquiryDTO data = new CommonInquiryDTO();
					
					data.setPermitNo(rs.getString("permit_no"));
					data.setBusNo(rs.getString("bus_no"));
					data.setNicNo(rs.getString("nic_no"));
					data.setStatusDes(rs.getString("status"));
					data.setCreatedBy(rs.getString("created_by"));
					data.setCreatedDate(rs.getTimestamp("created_date"));
					data.setFunctionDes(rs.getString("function_des"));
					data.setDriverConductorId(rs.getString("dc_id_num"));
					data.setAppNo(rs.getString("app_no"));
					
					dataList.add(data);
				}
			}else if((NIC != null && appNo != "" && dcId !=""  && NIC != "")){
				String sql = null;
				sql = "SELECT * FROM public.nt_t_task_inquiry WHERE function_name = 'Driver Conductor Management' "
						+ "AND (nic_no = ? AND app_no = ? AND dc_id_num = ?)";
				
				ps = con.prepareStatement(sql);
				ps.setString(1, NIC);
				ps.setString(2, appNo);
				ps.setString(3, dcId);
				
				rs = ps.executeQuery();
				
				while(rs.next()) {
					CommonInquiryDTO data = new CommonInquiryDTO();
					
					data.setPermitNo(rs.getString("permit_no"));
					data.setBusNo(rs.getString("bus_no"));
					data.setNicNo(rs.getString("nic_no"));
					data.setStatusDes(rs.getString("status"));
					data.setCreatedBy(rs.getString("created_by"));
					data.setCreatedDate(rs.getTimestamp("created_date"));
					data.setFunctionDes(rs.getString("function_des"));
					data.setDriverConductorId(rs.getString("dc_id_num"));
					data.setAppNo(rs.getString("app_no"));
					
					dataList.add(data);
				}
			}else if((NIC != null && busNo != "" && dcId !=""  && NIC != "")){
				String sql = null;
				sql = "SELECT * FROM public.nt_t_task_inquiry WHERE function_name = 'Driver Conductor Management' "
						+ "AND (nic_no = ? AND bus_no = ? AND dc_id_num = ?)";
				
				ps = con.prepareStatement(sql);
				ps.setString(1, NIC);
				ps.setString(2, busNo);
				ps.setString(3, dcId);
				
				rs = ps.executeQuery();
				
				while(rs.next()) {
					CommonInquiryDTO data = new CommonInquiryDTO();
					
					data.setPermitNo(rs.getString("permit_no"));
					data.setBusNo(rs.getString("bus_no"));
					data.setNicNo(rs.getString("nic_no"));
					data.setStatusDes(rs.getString("status"));
					data.setCreatedBy(rs.getString("created_by"));
					data.setCreatedDate(rs.getTimestamp("created_date"));
					data.setFunctionDes(rs.getString("function_des"));
					data.setDriverConductorId(rs.getString("dc_id_num"));
					data.setAppNo(rs.getString("app_no"));
					
					dataList.add(data);
				}
			}else if((NIC != null && appNo != ""  && NIC != "")){
				String sql = null;
				sql = "SELECT * FROM public.nt_t_task_inquiry WHERE function_name = 'Driver Conductor Management' "
						+ "AND (nic_no = ? AND app_no = ?)";
				
				ps = con.prepareStatement(sql);
				ps.setString(1, NIC);
				ps.setString(2, appNo);
				
				rs = ps.executeQuery();
				
				while(rs.next()) {
					CommonInquiryDTO data = new CommonInquiryDTO();
					
					data.setPermitNo(rs.getString("permit_no"));
					data.setBusNo(rs.getString("bus_no"));
					data.setNicNo(rs.getString("nic_no"));
					data.setStatusDes(rs.getString("status"));
					data.setCreatedBy(rs.getString("created_by"));
					data.setCreatedDate(rs.getTimestamp("created_date"));
					data.setFunctionDes(rs.getString("function_des"));
					data.setDriverConductorId(rs.getString("dc_id_num"));
					data.setAppNo(rs.getString("app_no"));
					
					dataList.add(data);
				}
			}else if((NIC != null && dcId != ""  && NIC != "")){
				String sql = null;
				sql = "SELECT * FROM public.nt_t_task_inquiry WHERE function_name = 'Driver Conductor Management' "
						+ "AND (nic_no = ? AND dc_id_num = ?)";
				
				ps = con.prepareStatement(sql);
				ps.setString(1, NIC);
				ps.setString(2, dcId);
				
				rs = ps.executeQuery();
				
				while(rs.next()) {
					CommonInquiryDTO data = new CommonInquiryDTO();
					
					data.setPermitNo(rs.getString("permit_no"));
					data.setBusNo(rs.getString("bus_no"));
					data.setNicNo(rs.getString("nic_no"));
					data.setStatusDes(rs.getString("status"));
					data.setCreatedBy(rs.getString("created_by"));
					data.setCreatedDate(rs.getTimestamp("created_date"));
					data.setFunctionDes(rs.getString("function_des"));
					data.setDriverConductorId(rs.getString("dc_id_num"));
					data.setAppNo(rs.getString("app_no"));
					
					dataList.add(data);
				}
			}
			
			else if((NIC != null && busNo != "" && NIC != "")){
				String sql = null;
				sql = "SELECT * FROM public.nt_t_task_inquiry WHERE function_name = 'Driver Conductor Management' "
						+ "AND (nic_no = ? AND bus_no = ?)";
				
				ps = con.prepareStatement(sql);
				ps.setString(1, NIC);
				ps.setString(2, busNo);
				
				rs = ps.executeQuery();
				
				while(rs.next()) {
					CommonInquiryDTO data = new CommonInquiryDTO();
					
					data.setPermitNo(rs.getString("permit_no"));
					data.setBusNo(rs.getString("bus_no"));
					data.setNicNo(rs.getString("nic_no"));
					data.setStatusDes(rs.getString("status"));
					data.setCreatedBy(rs.getString("created_by"));
					data.setCreatedDate(rs.getTimestamp("created_date"));
					data.setFunctionDes(rs.getString("function_des"));
					data.setDriverConductorId(rs.getString("dc_id_num"));
					data.setAppNo(rs.getString("app_no"));
					
					dataList.add(data);
				}
			}else if((busNo != "")){
				String sql = null;
				sql = "SELECT * FROM public.nt_t_task_inquiry WHERE function_name = 'Driver Conductor Management' "
						+ "AND (bus_no = ?)";
				
				ps = con.prepareStatement(sql);
				ps.setString(1, busNo);
				
				rs = ps.executeQuery();
				
				while(rs.next()) {
					CommonInquiryDTO data = new CommonInquiryDTO();
					
					data.setPermitNo(rs.getString("permit_no"));
					data.setBusNo(rs.getString("bus_no"));
					data.setNicNo(rs.getString("nic_no"));
					data.setStatusDes(rs.getString("status"));
					data.setCreatedBy(rs.getString("created_by"));
					data.setCreatedDate(rs.getTimestamp("created_date"));
					data.setFunctionDes(rs.getString("function_des"));
					data.setDriverConductorId(rs.getString("dc_id_num"));
					data.setAppNo(rs.getString("app_no"));
					
					dataList.add(data);
				}
			}else if((NIC != "")){
				String sql = null;
				sql = "SELECT * FROM public.nt_t_task_inquiry WHERE function_name = 'Driver Conductor Management' "
						+ "AND (nic_no = ?)";
				
				ps = con.prepareStatement(sql);
				ps.setString(1, NIC);
				
				rs = ps.executeQuery();
				
				while(rs.next()) {
					CommonInquiryDTO data = new CommonInquiryDTO();
					
					data.setPermitNo(rs.getString("permit_no"));
					data.setBusNo(rs.getString("bus_no"));
					data.setNicNo(rs.getString("nic_no"));
					data.setStatusDes(rs.getString("status"));
					data.setCreatedBy(rs.getString("created_by"));
					data.setCreatedDate(rs.getTimestamp("created_date"));
					data.setFunctionDes(rs.getString("function_des"));
					data.setDriverConductorId(rs.getString("dc_id_num"));
					data.setAppNo(rs.getString("app_no"));
					
					dataList.add(data);
				}
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		
		
		return dataList;
	}
	
	@Override
	public List<CommonInquiryDTO> searchDateDataForCommonInquiry(Date sheduleDate){
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CommonInquiryDTO> dataList = new ArrayList<CommonInquiryDTO>();
		
		java.sql.Date sqlDate = new java.sql.Date(sheduleDate.getTime());
		try {
			con = ConnectionManager.getConnection();
			
			String sql = null;
			sql = "SELECT DISTINCT * FROM public.nt_t_task_inquiry WHERE function_name = 'Driver Conductor Management'"
					+ "AND schedule_date = ?";
			
			ps = con.prepareStatement(sql);
			ps.setDate(1, sqlDate);
			
			rs = ps.executeQuery();
		
			while(rs.next()) {
				CommonInquiryDTO data = new CommonInquiryDTO();
					data.setSheduleDate(rs.getDate("schedule_date"));
					data.setStatusDes(rs.getString("status"));
					data.setCreatedBy(rs.getString("created_by"));
					data.setCreatedDate(rs.getTimestamp("created_date"));
					data.setFunctionDes(rs.getString("function_des"));
					dataList.add(data);
			}

			
		}catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		
		return dataList;
	}
	
}
