package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedProperty;

import lk.informatics.ntc.model.dto.AttorneyHolderDTO;
import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.EmployeeAddressDTO;
import lk.informatics.ntc.model.dto.EmployeeDTO;
import lk.informatics.ntc.model.dto.EmployeeDeptDTO;
import lk.informatics.ntc.model.dto.EmployeeDesignationDTO;
import lk.informatics.ntc.model.dto.ManageRoleDTO;
import lk.informatics.ntc.model.dto.SearchEmployeeDTO;
import lk.informatics.ntc.model.dto.UserRoleDTO;

//import lk.informatics.ntc.model.dto.attorneyHolderDTO;

import lk.informatics.ntc.view.beans.SearchBackingBean;
import lk.informatics.ntc.view.beans.SessionBackingBean;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.roc.utils.common.Utils;

public class EmployeeProfileServiceImpl implements EmployeeProfileService {
	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	// Get Titles
	public List<CommonDTO> GetTitleToDropdown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description " + "FROM nt_r_title " + "WHERE active = 'A' "
					+ "ORDER BY description ";

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

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	// Get Location
	public List<CommonDTO> GetLocationToDropdown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description " + "FROM nt_r_location " + "WHERE active = 'A' ";

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

	// Get Carder
	public List<CommonDTO> GetCarderToDropdown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description " + "FROM nt_r_emp_carder " + "WHERE active = 'A' ";

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

	// Get Qualification
	public List<CommonDTO> GetQualificationToDropdown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description " + "FROM nt_r_qualification " + "WHERE active = 'A' ";

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

	// Get Martial Status
	public List<CommonDTO> GetMartialToDropdown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description " + "FROM nt_r_marital_status " + "WHERE active = 'A' ";

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

	// Get Gender Status
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

	// Get Address Type
	public List<CommonDTO> GetAddressTypeToDropdown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description " + "FROM nt_r_address_type " + "WHERE active = 'A' ";

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

	// Get Cites
	public List<CommonDTO> GetCityToDropdown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description " + "FROM nt_r_postal_code " + "WHERE active = 'A' ";

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

	// Get Departments
	public List<CommonDTO> GetDepartmentsToDropdown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description " + "FROM nt_r_department " + "WHERE active = 'A' "
					+ "ORDER BY description ";

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

	// Get Sub Units
	public List<CommonDTO> GetUnitsToDropdown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description " + "FROM nt_r_sub_unit " + "WHERE active = 'A' "
					+ "ORDER BY description ";

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

	// Get Designation
	public List<CommonDTO> GetDesignationToDropdown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description " + "FROM nt_r_designation " + "WHERE active = 'A' "
					+ "ORDER BY description ";

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

	// Get Salary Category
	public List<CommonDTO> GetSalaryCatToDropdown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description " + "FROM nt_r_salary_cat " + "WHERE active = 'A' "
					+ "ORDER BY description ";

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

	// Get Employee Grade
	public List<CommonDTO> GetEmpGradeToDropdown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description " + "FROM nt_r_emp_grade " + "WHERE active = 'A' "
					+ "ORDER BY description ";

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

	// Get Roles
	public List<CommonDTO> GetRolesToDropdown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description " + "FROM nt_r_role " + "WHERE active = 'A' "
					+ "ORDER BY description ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setRoleCd(rs.getString("code"));
				commonDTO.setRoleName(rs.getString("description"));

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

	// Insert Employee Details
	public int saveEmployee(EmployeeDTO employeeDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();

			String sql = "INSERT INTO public.nt_m_employee"
					+ "(emp_no, emp_epf_no, emp_title, emp_first_name, emp_middle_name, emp_surname, emp_fullname, emp_gender, "
					+ " emp_marital_status, emp_dob, emp_nic_no, emp_qualification, emp_qualification_details, emp_recruitment_date, "
					+ " emp_appointment_date, emp_tel_no, emp_moblile_no, emp_email_add, emp_location, emp_carder, emp_permanent_date, "
					+ " emp_status, emp_created_date, emp_created_by, emp_req_account,emp_user_status, emp_is_initial,emp_image)"
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, employeeDTO.getEmpNo());
			stmt.setString(2, employeeDTO.getEmpEpfNo());
			stmt.setString(3, employeeDTO.getTitle());
			stmt.setString(4, employeeDTO.getFirstName());
			stmt.setString(5, employeeDTO.getMiddleName());
			stmt.setString(6, employeeDTO.getSureName());
			stmt.setString(7, employeeDTO.getFullName());
			stmt.setString(8, employeeDTO.getGender());
			stmt.setString(9, employeeDTO.getMaritalStatus());
			if (employeeDTO.getDob() != null) {
				String dob = (dateFormat.format(employeeDTO.getDob()));
				stmt.setString(10, dob);
			} else {
				stmt.setString(10, null);
			}

			stmt.setString(11, employeeDTO.getNicNo());
			stmt.setString(12, employeeDTO.getQualification());
			stmt.setString(13, employeeDTO.getQualificationDetails());
			if (employeeDTO.getRecruitmentDate() != null) {
				String recruitmentDate = (dateFormat.format(employeeDTO.getRecruitmentDate()));
				stmt.setString(14, recruitmentDate);
			} else {
				stmt.setString(14, null);
			}

			if (employeeDTO.getAppoinmentDate() != null) {
				String appoinmentDate = (dateFormat.format(employeeDTO.getAppoinmentDate()));
				stmt.setString(15, appoinmentDate);
			} else {
				stmt.setString(15, null);
			}

			stmt.setString(16, employeeDTO.getTelNo());
			stmt.setString(17, employeeDTO.getMobileNo());
			stmt.setString(18, employeeDTO.getEmail());
			stmt.setString(19, employeeDTO.getLocation());
			stmt.setString(20, employeeDTO.getCarder());
			if (employeeDTO.getPermanentDate() != null) {
				String permanentDate = (dateFormat.format(employeeDTO.getPermanentDate()));
				stmt.setString(21, permanentDate);
			} else {
				stmt.setString(21, null);
			}

			stmt.setString(22, employeeDTO.getStatus());
			stmt.setTimestamp(23, timestamp);
			stmt.setString(24, employeeDTO.getCreatedBy());
			stmt.setString(25, employeeDTO.getAccountNo());
			stmt.setString(26, employeeDTO.getUserStatus());
			stmt.setString(27, employeeDTO.getIsInitial());
			stmt.setBytes(28, employeeDTO.getImage());

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

	// Search By EmpNo
	public EmployeeDTO getEmployeeDetails(String empNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		EmployeeDTO employeeDTO = new EmployeeDTO();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT emp_no, emp_epf_no, emp_title, emp_first_name, emp_middle_name, emp_surname, emp_fullname, emp_gender, emp_marital_status, "
					+ " emp_dob, emp_nic_no, emp_qualification, emp_qualification_details, emp_recruitment_date, emp_appointment_date,"
					+ " emp_tel_no, emp_moblile_no, emp_email_add, emp_location, emp_carder, emp_permanent_date, "
					+ " case when emp_status = 'A' then 'Active' when emp_status = 'P' then 'Pending' when emp_status = 'I' then 'Inactive' else emp_status end,"
					+ " emp_req_account, emp_user_status, emp_is_initial,emp_image " + " FROM public.nt_m_employee "
					+ " WHERE emp_no = ? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, empNo);
			rs = ps.executeQuery();

			while (rs.next()) {

				employeeDTO.setEmpNo(rs.getString("emp_no"));
				employeeDTO.setEmpEpfNo(rs.getString("emp_epf_no"));
				employeeDTO.setTitle(rs.getString("emp_title"));
				employeeDTO.setFirstName(rs.getString("emp_first_name"));
				employeeDTO.setMiddleName(rs.getString("emp_middle_name"));
				employeeDTO.setSureName(rs.getString("emp_surname"));
				employeeDTO.setFullName(rs.getString("emp_fullname"));
				employeeDTO.setGender(rs.getString("emp_gender"));
				employeeDTO.setMaritalStatus(rs.getString("emp_marital_status"));

				Date dateDob = formatter.parse(rs.getString("emp_dob"));
				employeeDTO.setDob(dateDob);

				employeeDTO.setNicNo(rs.getString("emp_nic_no"));
				employeeDTO.setQualification(rs.getString("emp_qualification"));
				employeeDTO.setQualificationDetails(rs.getString("emp_qualification_details"));

				String recruString = rs.getString("emp_recruitment_date");
				if (recruString != null) {
					Date daterecru = formatter.parse(rs.getString("emp_recruitment_date"));
					employeeDTO.setRecruitmentDate(daterecru);
				}

				String appointString = rs.getString("emp_appointment_date");
				if (appointString != null) {
					Date dateappoint = formatter.parse(rs.getString("emp_appointment_date"));
					employeeDTO.setAppoinmentDate(dateappoint);
				}

				employeeDTO.setTelNo(rs.getString("emp_tel_no"));
				employeeDTO.setMobileNo(rs.getString("emp_moblile_no"));
				employeeDTO.setEmail(rs.getString("emp_email_add"));
				employeeDTO.setLocation(rs.getString("emp_location"));
				employeeDTO.setCarder(rs.getString("emp_carder"));

				String permanentString = rs.getString("emp_appointment_date");
				if (permanentString != null) {
					Date datepermanent = formatter.parse(rs.getString("emp_permanent_date"));
					employeeDTO.setPermanentDate(datepermanent);
				}

				employeeDTO.setStatus(rs.getString("emp_status"));
				employeeDTO.setAccountNo(rs.getString("emp_req_account"));
				employeeDTO.setUserStatus(rs.getString("emp_user_status"));
				employeeDTO.setIsInitial(rs.getString("emp_is_initial"));
				employeeDTO.setImage(rs.getBytes("emp_image"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return employeeDTO;

	}

	// Update Employee Details
	public int updateEmployee(EmployeeDTO employeeDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_employee " + " SET emp_qualification=?, emp_qualification_details=?, "
					+ " emp_tel_no=?, emp_moblile_no=?, emp_email_add=?, emp_location=?, "
					+ " emp_carder=?, emp_modified_date=? , emp_modified_by =?, emp_title=?, emp_surname=? ,"
					+ " emp_fullname =? , emp_marital_status =? , emp_permanent_date =?, "
					+ " emp_first_name=?, emp_middle_name=?, emp_gender=?, emp_dob=?, emp_nic_no=?, "
					+ " emp_recruitment_date=?, emp_appointment_date=?, emp_image=?, emp_epf_no=? "
					+ " WHERE emp_no= ?;";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, employeeDTO.getQualification());
			stmt.setString(2, employeeDTO.getQualificationDetails());
			stmt.setString(3, employeeDTO.getTelNo());
			stmt.setString(4, employeeDTO.getMobileNo());
			stmt.setString(5, employeeDTO.getEmail());
			stmt.setString(6, employeeDTO.getLocation());
			stmt.setString(7, employeeDTO.getCarder());
			stmt.setTimestamp(8, timestamp);
			stmt.setString(9, employeeDTO.getModifiedBy());
			stmt.setString(10, employeeDTO.getTitle());
			stmt.setString(11, employeeDTO.getSureName());
			stmt.setString(12, employeeDTO.getFullName());
			stmt.setString(13, employeeDTO.getMaritalStatus());
			if (employeeDTO.getPermanentDate() != null) {
				String permanentDate = (dateFormat.format(employeeDTO.getPermanentDate()));
				stmt.setString(14, permanentDate);
			} else {
				stmt.setString(14, null);
			}
			stmt.setString(15, employeeDTO.getFirstName());
			stmt.setString(16, employeeDTO.getMiddleName());
			stmt.setString(17, employeeDTO.getGender());
			if (employeeDTO.getDob() != null) {
				String dob = (dateFormat.format(employeeDTO.getDob()));
				stmt.setString(18, dob);
			} else {
				stmt.setString(18, null);
			}
			stmt.setString(19, employeeDTO.getNicNo());
			if (employeeDTO.getRecruitmentDate() != null) {
				String recdate = (dateFormat.format(employeeDTO.getRecruitmentDate()));
				stmt.setString(20, recdate);
			} else {
				stmt.setString(20, null);
			}
			if (employeeDTO.getAppoinmentDate() != null) {
				String appdate = (dateFormat.format(employeeDTO.getAppoinmentDate()));
				stmt.setString(21, appdate);
			} else {
				stmt.setString(21, null);
			}
			stmt.setBytes(22, employeeDTO.getImage());
			stmt.setString(23, employeeDTO.getEmpEpfNo());
			stmt.setString(24, employeeDTO.getEmpNo());

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

	// Insert Employee Address
	public int saveEmpAddress(EmployeeAddressDTO employeeAddressDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_emp_address");

			String sql = "INSERT INTO public.nt_m_emp_address "
					+ " (seqno, ead_emp_no, ead_add_type, ead_address1, ead_address2, ead_city, ead_active, ead_created_by, ead_created_date)"
					+ " VALUES(?,?,?,?,?,?,?,?,?)";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, employeeAddressDTO.getEmpNo());
			stmt.setString(3, employeeAddressDTO.getAddressType());
			stmt.setString(4, employeeAddressDTO.getAddress1());
			stmt.setString(5, employeeAddressDTO.getAddress2());
			stmt.setString(6, employeeAddressDTO.getCity());
			stmt.setString(7, employeeAddressDTO.getStatus());
			stmt.setString(8, employeeAddressDTO.getCreatedBy());
			stmt.setTimestamp(9, timestamp);

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

	// Address Details Search by EmpNo
	public List<EmployeeAddressDTO> findAddressDetByEmpNo(String empNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<EmployeeAddressDTO> empAddDetList = new ArrayList<EmployeeAddressDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT seqno, ead_emp_no, ead_add_type, ead_address1, ead_address2, ead_city, "
					+ " case when ead_active = 'A' then 'Active' when ead_active = 'P' then 'Pending' when ead_active = 'I' then 'Inactive' else ead_active end, "
					+ " ead_created_by,ead_created_date, "
					+ " b.description as add_type_desc, l.description as loc_type_desc"
					+ " FROM nt_m_emp_address inner join nt_r_address_type b on b.code = nt_m_emp_address.ead_add_type inner join nt_r_postal_code l "
					+ " on l.code = nt_m_emp_address.ead_city " + "  WHERE ead_emp_no= ? " + " AND ead_active = 'A' "
					+ " order by seqno ;";

			ps = con.prepareStatement(query);
			ps.setString(1, empNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				EmployeeAddressDTO employeeAddressDTO = new EmployeeAddressDTO();
				employeeAddressDTO.setSeq(rs.getLong("seqno"));
				employeeAddressDTO.setEmpNo(rs.getString("ead_emp_no"));
				employeeAddressDTO.setAddressType(rs.getString("ead_add_type"));
				employeeAddressDTO.setAddress1(rs.getString("ead_address1"));
				employeeAddressDTO.setAddress2(rs.getString("ead_address2"));
				employeeAddressDTO.setCity(rs.getString("ead_city"));
				employeeAddressDTO.setStatus(rs.getString("ead_active"));
				employeeAddressDTO.setAddressTypeDesc(rs.getString("add_type_desc"));
				employeeAddressDTO.setCityDesc(rs.getString("loc_type_desc"));
				employeeAddressDTO.setCreatedBy(rs.getString("ead_created_by"));
				employeeAddressDTO.setCratedDate(rs.getTimestamp("ead_created_date"));
				empAddDetList.add(employeeAddressDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return empAddDetList;

	}

	// Update Address Details
	public int updateAddressDetails(EmployeeAddressDTO empAddressDTO) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = " UPDATE public.nt_m_emp_address"
					+ " SET ead_add_type= ?, ead_address1= ?, ead_address2= ?, ead_city= ?, ead_active= ?,"
					+ " ead_created_by= ? , ead_created_date= ?, ead_modified_by= ?, ead_modified_date= ? "
					+ " WHERE seqno= ?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, empAddressDTO.getAddressType());
			stmt.setString(2, empAddressDTO.getAddress1());
			stmt.setString(3, empAddressDTO.getAddress2());
			stmt.setString(4, empAddressDTO.getCity());
			stmt.setString(5, empAddressDTO.getStatus());
			stmt.setString(6, empAddressDTO.getCreatedBy());
			stmt.setTimestamp(7, empAddressDTO.getCratedDate());
			stmt.setString(8, empAddressDTO.getModifiedBy());
			stmt.setTimestamp(9, timestamp);
			stmt.setLong(10, empAddressDTO.getSeq());

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

	// Update Status in Address Detail
	public int statusUpdate(long addressSeq) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_m_emp_address " + "SET ead_active = 'I' " + "WHERE seqno = ?;";

			ps = con.prepareStatement(query);
			ps.setLong(1, addressSeq);

			ps.executeUpdate();
			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return 0;

	}

	// Department Details Search by EmpNo
	public List<EmployeeDeptDTO> findDeparmentDetByEmpNo(String empNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<EmployeeDeptDTO> empDepDetList = new ArrayList<EmployeeDeptDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT seqno,dep_emp_no,dep_dept_code,dep_sub_unit_code,dep_start_date,dep_end_date,"
					+ " case when dep_active = 'A' then 'Active' when dep_active = 'P' then 'Pending' when dep_active = 'I' then 'Inactive' else dep_active end,"
					+ " dep_created_by,dep_created_date, d.description as depart_desc, s.description as unit_desc"
					+ " FROM nt_m_emp_department inner join nt_r_department d on d.code = nt_m_emp_department.dep_dept_code"
					+ " inner join nt_r_sub_unit s on s.code = nt_m_emp_department.dep_sub_unit_code "
					+ " WHERE nt_m_emp_department.dep_emp_no = ? " + " ORDER BY seqno;";

			//

			ps = con.prepareStatement(query);
			ps.setString(1, empNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				EmployeeDeptDTO employeeDeptDTO = new EmployeeDeptDTO();

				employeeDeptDTO.setSeq(rs.getLong("seqno"));
				employeeDeptDTO.setEmpNo(rs.getString("dep_emp_no"));
				employeeDeptDTO.setDeptCode(rs.getString("dep_dept_code"));
				employeeDeptDTO.setUnitCode(rs.getString("dep_sub_unit_code"));

				String startDateString = rs.getString("dep_start_date");

				if (startDateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date datestart = originalFormat.parse(startDateString);
					employeeDeptDTO.setStartDate(datestart);

					employeeDeptDTO.setStrStartDate(startDateString);

				}

				String endDateString = rs.getString("dep_end_date");

				if (endDateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date dateEnd = originalFormat.parse(endDateString);
					employeeDeptDTO.setEndDate(dateEnd);

					employeeDeptDTO.setStrEndDate(endDateString);

				}

				employeeDeptDTO.setStatus(rs.getString("dep_active"));
				employeeDeptDTO.setCreatedBy(rs.getString("dep_created_by"));
				employeeDeptDTO.setCratedDate(rs.getTimestamp("dep_created_date"));
				employeeDeptDTO.setDeptDesc(rs.getString("depart_desc"));
				employeeDeptDTO.setUnitDesc(rs.getString("unit_desc"));

				empDepDetList.add(employeeDeptDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return empDepDetList;

	}

	// Insert Department Details
	public int saveDepDetais(EmployeeDeptDTO employeeDeptDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_emp_department");

			String sql = "INSERT INTO public.nt_m_emp_department "
					+ " (seqno, dep_emp_no, dep_dept_code, dep_sub_unit_code, dep_start_date, dep_end_date, dep_active, dep_created_by, dep_created_date)"
					+ " VALUES(?,?,?,?,?,?,?,?,?)";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, employeeDeptDTO.getEmpNo());
			stmt.setString(3, employeeDeptDTO.getDeptCode());
			stmt.setString(4, employeeDeptDTO.getUnitCode());

			if (employeeDeptDTO.getStartDate() != null) {
				String startDate = (dateFormat.format(employeeDeptDTO.getStartDate()));
				stmt.setString(5, startDate);
			} else {
				stmt.setString(5, null);
			}

			if (employeeDeptDTO.getEndDate() != null) {
				String endDate = (dateFormat.format(employeeDeptDTO.getEndDate()));
				stmt.setString(6, endDate);
			} else {
				stmt.setString(6, null);
			}

			stmt.setString(7, employeeDeptDTO.getStatus());
			stmt.setString(8, employeeDeptDTO.getCreatedBy());
			stmt.setTimestamp(9, timestamp);

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

	// Update Department Details
	public int updateDepDetails(EmployeeDeptDTO employeeDeptDTO) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();

			String sql = " UPDATE public.nt_m_emp_department"
					+ " SET dep_emp_no= ?, dep_dept_code= ?, dep_sub_unit_code= ?, dep_start_date= ?, dep_end_date= ?, dep_active= ?, "
					+ " dep_modified_by= ?, dep_modified_date= ? " + " WHERE seqno= ?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, employeeDeptDTO.getEmpNo());
			stmt.setString(2, employeeDeptDTO.getDeptCode());
			stmt.setString(3, employeeDeptDTO.getUnitCode());

			if (employeeDeptDTO.getStartDate() != null) {
				String startDate = (dateFormat.format(employeeDeptDTO.getStartDate()));
				stmt.setString(4, startDate);
			} else {
				stmt.setString(4, null);
			}

			if (employeeDeptDTO.getEndDate() != null) {
				String endDate = (dateFormat.format(employeeDeptDTO.getEndDate()));
				stmt.setString(5, endDate);
			} else {
				stmt.setString(5, null);
			}

			stmt.setString(6, employeeDeptDTO.getStatus());
			stmt.setString(7, employeeDeptDTO.getModifiedBy());
			stmt.setTimestamp(8, timestamp);
			stmt.setLong(9, employeeDeptDTO.getSeq());

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

	// Update Status in Department Detail
	public int statusUpdateDept(long deptSeq) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_m_emp_department " + "SET dep_active = 'I' " + "WHERE seqno = ?;";

			ps = con.prepareStatement(query);
			ps.setLong(1, deptSeq);

			ps.executeUpdate();
			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return 0;

	}

	// Check Department Details
	public EmployeeDeptDTO isDeptDetAvilable(String empNo, String deptCode, String UnitCode, String Status) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		EmployeeDeptDTO employeeDeptDTO = new EmployeeDeptDTO();
		try {
			con = ConnectionManager.getConnection();

			String query = "select dep_emp_no" + " from nt_m_emp_department "
					+ " where nt_m_emp_department.dep_emp_no = ? " + " and dep_dept_code = ? "
					+ " and dep_sub_unit_code = ? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, empNo);
			ps.setString(2, deptCode);
			ps.setString(3, UnitCode);
			rs = ps.executeQuery();

			while (rs.next()) {

				employeeDeptDTO.setEmpNo(rs.getString("dep_emp_no"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return employeeDeptDTO;

	}

	// Designation Details Search by EmpNo
	public List<EmployeeDesignationDTO> findDesignationDetByEmpNo(String empNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<EmployeeDesignationDTO> empDesigDetList = new ArrayList<EmployeeDesignationDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT seqno,des_emp_no,des_desig_code,des_grade_code,des_sal_cat_code,"
					+ " des_start_date,des_end_date,"
					+ " case when des_active = 'A' then 'Active' when des_active = 'P' then 'Pending' when des_active = 'I' then 'Inactive' else des_active end,"
					+ " d.description as designation_des, s.description as sal_cat_desc"
					+ " FROM nt_m_emp_designation inner join nt_r_designation d on d.code = nt_m_emp_designation.des_desig_code"
					+ " inner join nt_r_salary_cat s on s.code = nt_m_emp_designation.des_sal_cat_code "
					+ " WHERE des_emp_no = ?  " + " ORDER BY seqno;";

			ps = con.prepareStatement(query);
			ps.setString(1, empNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				EmployeeDesignationDTO employeeDesignationDTO = new EmployeeDesignationDTO();

				employeeDesignationDTO.setSeq(rs.getLong("seqno"));
				employeeDesignationDTO.setEmpNo(rs.getString("des_emp_no"));
				employeeDesignationDTO.setDesignationCode(rs.getString("des_desig_code"));
				employeeDesignationDTO.setGradeCode(rs.getString("des_grade_code"));
				employeeDesignationDTO.setSalCatCode(rs.getString("des_sal_cat_code"));

				String startDateString = rs.getString("des_start_date");

				if (startDateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date datestart = originalFormat.parse(startDateString);
					employeeDesignationDTO.setStartDate(datestart);

					employeeDesignationDTO.setStrStartDate(startDateString);

				}

				String endDateString = rs.getString("des_end_date");

				if (endDateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date dateEnd = originalFormat.parse(endDateString);
					employeeDesignationDTO.setEndDate(dateEnd);

					employeeDesignationDTO.setStrEndDate(endDateString);

				}

				employeeDesignationDTO.setStatus(rs.getString("des_active"));
				employeeDesignationDTO.setDesignationDesc(rs.getString("designation_des"));
				employeeDesignationDTO.setSalCatDesc(rs.getString("sal_cat_desc"));

				empDesigDetList.add(employeeDesignationDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return empDesigDetList;

	}

	@Override
	public List<String> getAllEpfNubers() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> data = new ArrayList<String>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select emp_epf_no from public.nt_m_employee WHERE emp_epf_no IS NOT NULL order by emp_epf_no;";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				data.add(rs.getString("emp_epf_no"));

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
	public List<String> getAllEpfLocation() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> data = new ArrayList<String>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select description " + " from public.nt_r_location " + " where active = 'A' "
					+ " order by description;";

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
	public List<String> getAllEpfUserID() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> data = new ArrayList<String>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select emp_user_id " + "from public.nt_m_employee " + "where emp_user_id is not null;";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				data.add(rs.getString("emp_user_id"));
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
	public List<String> getEmpNo() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> data = new ArrayList<String>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select emp_no from public.nt_m_employee order by emp_no;";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				data.add(rs.getString("emp_no"));
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
	public List<String> getUserId() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> data = new ArrayList<String>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select * from public.nt_m_employee";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				data.add(rs.getString("emp_user_id"));
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
	public String getDepartment(String empNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String department = null;
		try {

			con = ConnectionManager.getConnection();

			String query3 = "select distinct c.description " + "from nt_m_employee a "
					+ "inner join nt_m_emp_department b on a.emp_no = b.dep_emp_no "
					+ "inner join nt_r_department c on b.dep_dept_code = c.code " + "where b.dep_active='A'"
					+ "and emp_no =?;";

			ps = con.prepareStatement(query3);
			ps.setString(1, empNo);
			rs = ps.executeQuery();

			while (rs.next()) {

				department = rs.getString("description");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return department;
	}

	@Override
	public String getLocation(String empNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String designation = null;
		try {

			con = ConnectionManager.getConnection();

			String query3 = "select distinct c.description " + "from nt_m_employee a "
					+ "inner join nt_m_emp_designation b on a.emp_no = b.des_emp_no "
					+ "inner join nt_r_designation c on b.des_desig_code = c.code " + "where b.des_active='A' "
					+ "and emp_no =?; ";

			ps = con.prepareStatement(query3);
			ps.setString(1, empNo);
			rs = ps.executeQuery();

			while (rs.next()) {

				designation = rs.getString("description");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return designation;

	}

	@Override
	public boolean checkIsDataAvailable(String epfNo, String nicNo, String userId, String status, String location,
			String empNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isdataAvailable = false;

		String WHERE_SQL = "";
		boolean whereadded = false;
		if (epfNo != null && !epfNo.equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE a.emp_epf_no = " + "'" + epfNo + "'";

			whereadded = true;
		}
		if (nicNo != null && !nicNo.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " OR emp_nic_no = " + "'" + nicNo + "'";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE emp_nic_no = " + "'" + nicNo + "'";

				whereadded = true;
			}
		}
		if (userId != null && !userId.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " OR emp_user_id = " + "'" + userId + "'";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE emp_user_id = " + "'" + userId + "'";

				whereadded = true;
			}
		}
		if (status != null && !status.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " OR emp_status = " + "'" + status + "'";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE emp_status = " + "'" + status + "'";

				whereadded = true;
			}
		}

		if (empNo != null && !empNo.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " OR emp_no = " + "'" + empNo + "'";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE emp_no = " + "'" + empNo + "'";

				whereadded = true;
			}
		}

		if (location != null && !location.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " OR nt_r_location.description = " + "'" + location + "'";
			} else {
				WHERE_SQL = WHERE_SQL + " WHERE nt_r_location.description = " + "'" + location + "'";
				whereadded = true;
			}
		}

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select distinct a.emp_no, a.emp_epf_no, "
					+ "a.emp_nic_no,  a.emp_user_id, a.emp_req_account, "
					+ "(CASE WHEN a.emp_status ='P'  THEN 'PENDING' " + "when a.emp_status = 'R' THEN 'REJECT' WHEN "
					+ "a.emp_status = 'A' THEN 'APPROVED' END) as emp_status, nt_r_location.description as location "
					+ "from public.nt_m_employee a "
					+ "left outer join public.nt_r_location on a.emp_location = nt_r_location.code " + WHERE_SQL;

			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();

			if (rs.next() == true) {
				isdataAvailable = true;
			} else {
				isdataAvailable = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			isdataAvailable = false;

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return isdataAvailable;
	}

	@Override
	public List<EmployeeDTO> getEmpDetails(String epfNo, String nicNo, String userId, String status, String location,
			String empNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isReqAccount = false;

		String WHERE_SQL = "";
		boolean whereadded = false;
		if (epfNo != null && !epfNo.equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE a.emp_epf_no = " + "'" + epfNo
					+ "' and nt_m_emp_designation.des_active = 'A' and nt_m_emp_department.dep_active = 'A'";
			whereadded = true;
		}
		if (nicNo != null && !nicNo.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " OR emp_nic_no = " + "'" + nicNo
						+ "' and nt_m_emp_designation.des_active = 'A' and nt_m_emp_department.dep_active = 'A'";
			} else {
				WHERE_SQL = WHERE_SQL + " WHERE emp_nic_no = " + "'" + nicNo
						+ "' and nt_m_emp_designation.des_active = 'A' and nt_m_emp_department.dep_active = 'A'";
				whereadded = true;
			}
		}
		if (userId != null && !userId.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " OR emp_user_id = " + "'" + userId
						+ "' and nt_m_emp_designation.des_active = 'A' and nt_m_emp_department.dep_active = 'A'";
			} else {
				WHERE_SQL = WHERE_SQL + " WHERE emp_user_id = " + "'" + userId
						+ "' and nt_m_emp_designation.des_active = 'A' and nt_m_emp_department.dep_active = 'A'";
				whereadded = true;
			}
		}
		if (status != null && !status.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " OR emp_status = " + "'" + status
						+ "' and nt_m_emp_designation.des_active = 'A' and nt_m_emp_department.dep_active = 'A'";
			} else {
				WHERE_SQL = WHERE_SQL + " WHERE emp_status = " + "'" + status
						+ "' and nt_m_emp_designation.des_active = 'A' and nt_m_emp_department.dep_active = 'A'";
				whereadded = true;
			}
		}

		if (empNo != null && !empNo.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " OR emp_no = " + "'" + empNo
						+ "' and nt_m_emp_designation.des_active = 'A' and nt_m_emp_department.dep_active = 'A'";
			} else {
				WHERE_SQL = WHERE_SQL + " WHERE emp_no = " + "'" + empNo
						+ "' and nt_m_emp_designation.des_active = 'A' and nt_m_emp_department.dep_active = 'A'";
				whereadded = true;
			}
		}

		if (location != null && !location.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " OR nt_r_location.description = " + "'" + location
						+ "' and nt_m_emp_designation.des_active = 'A' and nt_m_emp_department.dep_active = 'A'";
			} else {
				WHERE_SQL = WHERE_SQL + " WHERE nt_r_location.description = " + "'" + location
						+ "' and nt_m_emp_designation.des_active = 'A' and nt_m_emp_department.dep_active = 'A'";
				whereadded = true;
			}
		}

		List<EmployeeDTO> employeeDTO = new ArrayList<EmployeeDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select distinct a.emp_no, a.emp_epf_no, a.emp_fullname, a.emp_moblile_no, "
					+ "a.emp_nic_no,  a.emp_user_id, a.emp_req_account, nt_m_emp_department.dep_dept_code as depCode,nt_m_emp_department.dep_active, "
					+ "nt_m_emp_designation.des_desig_code,nt_m_emp_designation.des_active, "
					+ "(CASE WHEN a.emp_status ='P'  THEN 'PENDING' " + "when a.emp_status = 'R' THEN 'REJECT' WHEN "
					+ "a.emp_status = 'A' THEN 'APPROVED' END) as emp_status, nt_r_designation.description as designation, nt_r_department.description as department, nt_r_location.description as location "
					+ "from public.nt_m_employee a "
					+ "inner join nt_m_emp_department on a.emp_no = nt_m_emp_department.dep_emp_no "
					+ "inner join nt_m_emp_designation on a.emp_no = nt_m_emp_designation.des_emp_no "
					+ "inner join nt_r_department  on  nt_m_emp_department.dep_dept_code= nt_r_department.code "
					+ "inner join nt_r_designation  on  nt_m_emp_designation.des_desig_code= nt_r_designation.code "
					+ "left outer join public.nt_r_location on a.emp_location = nt_r_location.code "

					+ WHERE_SQL;

			EmployeeDTO e;
			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();

			while (rs.next()) {

				e = new EmployeeDTO();
				e.setEmpNo(rs.getString("emp_no"));
				e.setEmpEpfNo(rs.getString("emp_epf_no"));
				e.setFullName(rs.getString("emp_fullname"));
				e.setNicNo(rs.getString("emp_nic_no"));

				if (rs.getString("emp_req_account") != null) {

					if (rs.getString("emp_req_account").equals("y")) {
						isReqAccount = true;
					} else {
						isReqAccount = false;
					}
				} else {
					isReqAccount = false;
				}
				e.setReqBool(isReqAccount);
				e.setStatus(rs.getString("emp_status"));
				e.setDesignation(rs.getString("designation"));
				e.setDepartment(rs.getString("department"));
				e.setDepCode(rs.getString("depCode"));
				e.setMobileNo(rs.getString("emp_moblile_no"));
				employeeDTO.add(e);
			}

			return employeeDTO;

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
	public boolean isActiveEmployee(String data) {

		boolean isdesignation = false;
		Connection con = null;
		PreparedStatement pss = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct * from public.nt_m_emp_designation WHERE nt_m_emp_designation.des_emp_no=?";

			pss = con.prepareStatement(query);
			pss.setString(1, data);

			rs = pss.executeQuery();

			if (rs.next()) {
				isdesignation = true;
			} else {
				isdesignation = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return isdesignation;
	}

	@Override
	public boolean isDesignationAvailable(String empnumber, String epfnumber, String nicnumber) {
		boolean isdesignation = false;
		Connection con = null;
		PreparedStatement pss = null;
		ResultSet rs = null;

		String WHERE_SQL = "";
		boolean whereadded = false;
		if (empnumber != null && !empnumber.equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE nt_m_employee.emp_no = " + "'" + empnumber
					+ "' and nt_m_emp_designation.des_active = 'A'";
			whereadded = true;
		}
		if (epfnumber != null && !epfnumber.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " OR nt_m_employee.emp_epf_no = " + "'" + epfnumber
						+ "' and nt_m_emp_designation.des_active = 'A'";
			} else {
				WHERE_SQL = WHERE_SQL + " WHERE nt_m_employee.emp_epf_no = " + "'" + epfnumber
						+ "' and nt_m_emp_designation.des_active = 'A'";
				whereadded = true;
			}
		}
		if (nicnumber != null && !nicnumber.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " OR nt_m_employee.emp_nic_no = " + "'" + nicnumber
						+ "' and nt_m_emp_designation.des_active = 'A'";
			} else {
				WHERE_SQL = WHERE_SQL + " WHERE nt_m_employee.emp_nic_no = " + "'" + nicnumber
						+ "' and nt_m_emp_designation.des_active = 'A'";
				whereadded = true;
			}
		}

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct * from public.nt_m_emp_designation "
					+ "inner join public.nt_m_employee on nt_m_employee.emp_no = nt_m_emp_designation.des_emp_no "
					+ WHERE_SQL;

			pss = con.prepareStatement(query);
			rs = pss.executeQuery();

			if (rs.next()) {
				isdesignation = true;
			} else {
				isdesignation = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return isdesignation;

	}

	@Override
	public boolean checkReqType(List<EmployeeDTO> checkList) {
		boolean isReqAccount = false;
		Connection con = null;
		PreparedStatement pss = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			for (int i = 0; i < checkList.size(); i++) {

				String query = "select * from public.nt_m_employee where emp_no=? and  emp_req_account='y';";

				pss = con.prepareStatement(query);
				pss.setString(1, checkList.get(i).getEmpNo());

				rs = pss.executeQuery();

				if (rs.next()) {
					isReqAccount = true;
				} else {
					isReqAccount = false;
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			isReqAccount = false;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return isReqAccount;

	}

	@Override
	public boolean isDepartmentAvailable(String empnumber, String epfnumber, String nicnumber) {

		boolean isdataAvailable = false;
		Connection con = null;
		PreparedStatement pss = null;
		ResultSet rs = null;

		String WHERE_SQL = "";
		boolean whereadded = false;
		if (empnumber != null && !empnumber.equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE nt_m_employee.emp_no = " + "'" + empnumber
					+ "' and nt_m_emp_department.dep_active = 'A'";
			whereadded = true;
		}
		if (epfnumber != null && !epfnumber.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " OR nt_m_employee.emp_epf_no = " + "'" + epfnumber
						+ "' and nt_m_emp_department.dep_active = 'A'";
			} else {
				WHERE_SQL = WHERE_SQL + " WHERE nt_m_employee.emp_epf_no = " + "'" + epfnumber
						+ "' and nt_m_emp_department.dep_active = 'A'";
				whereadded = true;
			}
		}
		if (nicnumber != null && !nicnumber.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " OR nt_m_employee.emp_nic_no = " + "'" + nicnumber
						+ "' and nt_m_emp_department.dep_active = 'A'";
			} else {
				WHERE_SQL = WHERE_SQL + " WHERE nt_m_employee.emp_nic_no = " + "'" + nicnumber
						+ "' and nt_m_emp_department.dep_active = 'A'";
				whereadded = true;
			}
		}

		try {
			con = ConnectionManager.getConnection();

			String query = "select * from public.nt_m_emp_department "
					+ "inner join public.nt_m_employee on nt_m_employee.emp_no = nt_m_emp_department.dep_emp_no "
					+ WHERE_SQL;

			pss = con.prepareStatement(query);
			rs = pss.executeQuery();

			if (rs.next()) {
				isdataAvailable = true;
			} else {
				isdataAvailable = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return isdataAvailable;

	}

	@Override
	public boolean CheckUserForAssign(String empNumber) {

		boolean isReady = false;
		Connection con = null;
		PreparedStatement pss = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select * from public.nt_m_employee where emp_no=? and  Not emp_user_id is null and emp_status='A';";

			pss = con.prepareStatement(query);
			pss.setString(1, empNumber);

			rs = pss.executeQuery();

			if (rs.next()) {
				isReady = true;
			} else {
				isReady = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return isReady;
	}

	@Override
	public List<UserRoleDTO> assignRole(String empNo, String depCode) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<UserRoleDTO> userDTO = new ArrayList<UserRoleDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select distinct c.fra_role_code, nt_r_role.description " + "from nt_m_employee a "
					+ "inner join nt_m_emp_department b on a.emp_no = b.dep_emp_no "
					+ "inner join nt_r_fun_role_activity c on b.dep_dept_code = c.fra_dept_code "
					+ "inner join public.nt_r_role  on  c.fra_role_code= nt_r_role.code where emp_no =?  and nt_r_role.departmentcode=?; ";

			UserRoleDTO e;
			ps = con.prepareStatement(query2);
			ps.setString(1, empNo);
			ps.setString(2, depCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				e = new UserRoleDTO();

				e.setRoleCode(rs.getString("fra_role_code"));
				e.setRoleDescription(rs.getString("description"));

				userDTO.add(e);
			}

			return userDTO;

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
	public boolean approveUser(List<EmployeeDTO> approveList, String approveBy, String req) {

		boolean isUserApproved = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();

			java.util.Date date = new java.util.Date();
			Timestamp timestamp = new Timestamp(date.getTime());

			for (int i = 0; i < approveList.size(); i++) {

				String sql = " UPDATE public.nt_m_employee"
						+ " SET emp_status= ?,  emp_approved_by = ?, emp_approved_date=?, emp_req_account=?"
						+ " WHERE emp_no= ?;";

				ps = con.prepareStatement(sql);
				ps.setString(1, "A");
				ps.setString(2, approveBy);
				ps.setTimestamp(3, timestamp);
				ps.setString(4, req);
				ps.setString(5, approveList.get(i).getEmpNo());

				ps.executeUpdate();
				con.commit();

				isUserApproved = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			isUserApproved = false;

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return isUserApproved;
	}

	@Override
	public boolean rejectUser(List<EmployeeDTO> approveList, String approveBy, String val) {
		boolean isUserReject = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			java.util.Date date = new java.util.Date();
			Timestamp timestamp = new Timestamp(date.getTime());

			for (int i = 0; i < approveList.size(); i++) {

				String sql = " UPDATE public.nt_m_employee"
						+ " SET emp_status= ?, emp_approved_by = ?, emp_approved_date=?"
						+ " WHERE emp_no= ? AND emp_status='A';";

				ps = con.prepareStatement(sql);
				ps.setString(1, "R");

				ps.setString(2, approveBy);
				ps.setTimestamp(3, timestamp);
				ps.setString(4, approveList.get(i).getEmpNo());

				int a = ps.executeUpdate();
				ConnectionManager.close(ps);

				if (a > 0) {

					long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_emp_reject_reason");

					String query = "INSERT INTO public.nt_m_emp_reject_reason "
							+ "(err_seq_no, err_emp_id, err_reject_reason, err_created_by, err_created_date) "
							+ "VALUES(?,?,?,?,?); ";

					ps = con.prepareStatement(query);
					ps.setLong(1, seqNo);
					ps.setString(2, approveList.get(i).getEmpNo());
					ps.setString(3, val);
					ps.setString(4, approveBy);
					ps.setTimestamp(5, timestamp);

					ps.executeUpdate();
					isUserReject = true;
				}

				con.commit();

				isUserReject = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			isUserReject = false;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return isUserReject;

	}

	// Insert Designation Details
	public int saveDesigDetais(EmployeeDesignationDTO employeeDesignationDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_emp_designation");

			String sql = "INSERT INTO public.nt_m_emp_designation "
					+ " (seqno, des_emp_no, des_desig_code, des_grade_code, des_sal_cat_code, des_start_date, des_end_date, des_active, des_created_by, des_created_date)"
					+ " VALUES(?,?,?,?,?,?,?,?,?,?)";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, employeeDesignationDTO.getEmpNo());
			stmt.setString(3, employeeDesignationDTO.getDesignationCode());
			stmt.setString(4, employeeDesignationDTO.getGradeCode());
			stmt.setString(5, employeeDesignationDTO.getSalCatCode());

			if (employeeDesignationDTO.getStartDate() != null) {
				String startDate = (dateFormat.format(employeeDesignationDTO.getStartDate()));
				stmt.setString(6, startDate);
			} else {
				stmt.setString(6, null);
			}

			if (employeeDesignationDTO.getEndDate() != null) {
				String endDate = (dateFormat.format(employeeDesignationDTO.getEndDate()));
				stmt.setString(7, endDate);
			} else {
				stmt.setString(7, null);
			}

			stmt.setString(8, employeeDesignationDTO.getStatus());
			stmt.setString(9, employeeDesignationDTO.getCreatedBy());
			stmt.setTimestamp(10, timestamp);

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

	// Update Designation Details
	public int updateDesigDetails(EmployeeDesignationDTO employeeDesignationDTO) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();

			String sql = " UPDATE public.nt_m_emp_designation"
					+ " SET des_emp_no= ?, des_desig_code= ?, des_grade_code= ?, des_sal_cat_code=?,"
					+ " des_start_date= ?, des_end_date= ?, des_active= ?, des_modified_by= ?, des_modified_date = ?"
					+ " WHERE seqno= ?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, employeeDesignationDTO.getEmpNo());
			stmt.setString(2, employeeDesignationDTO.getDesignationCode());
			stmt.setString(3, employeeDesignationDTO.getGradeCode());
			stmt.setString(4, employeeDesignationDTO.getSalCatCode());

			if (employeeDesignationDTO.getStartDate() != null) {
				String startDate = (dateFormat.format(employeeDesignationDTO.getStartDate()));
				stmt.setString(5, startDate);
			} else {
				stmt.setString(5, null);
			}

			if (employeeDesignationDTO.getEndDate() != null) {
				String endDate = (dateFormat.format(employeeDesignationDTO.getEndDate()));
				stmt.setString(6, endDate);
			} else {
				stmt.setString(6, null);
			}

			stmt.setString(7, employeeDesignationDTO.getStatus());
			stmt.setString(8, employeeDesignationDTO.getModifiedBy());
			stmt.setTimestamp(9, timestamp);
			stmt.setLong(10, employeeDesignationDTO.getSeq());

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

	// Update Status in Designation Detail
	public int statusUpdateDesig(long deptSeq, String deptendDate) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_m_emp_designation " + "SET des_active = 'I' , des_end_date = ? "
					+ "WHERE seqno = ? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, deptendDate);
			ps.setLong(2, deptSeq);

			ps.executeUpdate();
			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return 0;

	}

	// Check Designation Details
	public EmployeeDeptDTO isDesigDetAvilable(String empNo, String desigCode, String grade, String salCat) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		EmployeeDeptDTO employeeDeptDTO = new EmployeeDeptDTO();
		try {
			con = ConnectionManager.getConnection();

			String query = "select des_emp_no" + " from nt_m_emp_designation " + " where des_emp_no = ? "
					+ " and des_desig_code = ? " + " and des_grade_code = ? " + " and des_sal_cat_code = ? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, empNo);
			ps.setString(2, desigCode);
			ps.setString(3, grade);
			ps.setString(4, salCat);
			rs = ps.executeQuery();

			while (rs.next()) {

				employeeDeptDTO.setEmpNo(rs.getString("des_emp_no"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return employeeDeptDTO;

	}

	// Check Employee No
	public EmployeeDeptDTO isEmpNoAvilable(String empNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		EmployeeDeptDTO employeeDeptDTO = new EmployeeDeptDTO();
		try {
			con = ConnectionManager.getConnection();

			String query = "select emp_no" + " from nt_m_employee " + " where emp_no = ? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, empNo);
			rs = ps.executeQuery();

			while (rs.next()) {

				employeeDeptDTO.setEmpNo(rs.getString("emp_no"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return employeeDeptDTO;

	}

	public List<CommonDTO> getAllRoleNamesList(String depcode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description  FROM nt_r_role WHERE active = 'A' AND departmentcode = ? order by description";

			ps = con.prepareStatement(query);
			ps.setString(1, depcode);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setRoleCd(rs.getString("code"));
				commonDTO.setRoleName(rs.getString("description"));
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
	public List<ManageRoleDTO> searchDetails(String depNo, String roleCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<ManageRoleDTO> searchList = new ArrayList<ManageRoleDTO>();
		try {
			con = ConnectionManager.getConnection();
			String query = "SELECT a.code as depcode,b.code as rolecode,a.description,b.description as roledes,b.active FROM public.nt_r_department a,nt_r_role b where a.code=b.departmentcode and a.code=? and b.code=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, depNo);
			ps.setString(2, roleCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				ManageRoleDTO manageRoleDTO1 = new ManageRoleDTO();

				manageRoleDTO1.setDepartmentDescription(rs.getString("description"));
				manageRoleDTO1.setRoleName(rs.getString("roledes"));
				manageRoleDTO1.setRoleStatus("Active");
				manageRoleDTO1.setRoleCode(rs.getString("rolecode"));
				manageRoleDTO1.setDepCode(rs.getString("depcode"));
				searchList.add(manageRoleDTO1);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return searchList;
	}

	// Get All Department & Roles
	public List<EmployeeDTO> GetDeptandRoles() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<EmployeeDTO> returnList = new ArrayList<EmployeeDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT r.description as role_desc, d.description as department_desc, "
					+ "	case when r.active = 'A' then 'Active' when r.active = 'I' then 'Inactive' "
					+ " else r.active end as status"
					+ " FROM nt_r_role r inner join nt_r_department d on r.departmentcode = d.code"
					+ " ORDER BY r.description ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				EmployeeDTO employeeDTO = new EmployeeDTO();
				employeeDTO.setUserRole(rs.getString("role_desc"));
				employeeDTO.setDepartment(rs.getString("department_desc"));
				employeeDTO.setStatus(rs.getString("status"));

				returnList.add(employeeDTO);

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

	// Search Department & Roles
	public List<EmployeeDTO> SearchDeptandRoles(String deptCode, String roleCode) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String WHERE_SQL = "";
		if (deptCode != null && !deptCode.isEmpty()) {
			WHERE_SQL = WHERE_SQL + " AND d.code = " + "'" + deptCode + "'";
		}
		if (roleCode != null && !roleCode.isEmpty()) {
			WHERE_SQL = WHERE_SQL + " AND r.code = " + "'" + roleCode + "'";
		}

		List<EmployeeDTO> returnList = new ArrayList<EmployeeDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT r.description as role_desc, d.description as department_desc, "
					+ "	case when r.active = 'A' then 'Active' when r.active = 'I' then 'Inactive' "
					+ " else r.active end as status" + " FROM nt_r_role r , nt_r_department d"
					+ " WHERE r.departmentcode = d.code" + WHERE_SQL + " ORDER BY r.description";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				EmployeeDTO employeeDTO = new EmployeeDTO();
				employeeDTO.setUserRole(rs.getString("role_desc"));
				employeeDTO.setDepartment(rs.getString("department_desc"));
				employeeDTO.setStatus(rs.getString("status"));

				returnList.add(employeeDTO);

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
	public int updateRoleManagement(String rolecode, String depcode, String roleName, String status,
			String beforerolecode, String modifyBy) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_r_role SET code=?, description=?, departmentcode=?, active=?, modified_by=?, modified_date=?  WHERE code=?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, rolecode);
			stmt.setString(2, roleName);
			stmt.setString(3, depcode);
			stmt.setString(4, status);
			stmt.setString(5, modifyBy);
			stmt.setTimestamp(6, timestamp);
			stmt.setString(7, beforerolecode);
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
	public boolean checkHaveFunction(String roleCode, String depCode) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isroleActived = false;
		ManageRoleDTO manageRoleDTO = new ManageRoleDTO();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT  fra_dept_code, fra_role_code FROM public.nt_r_fun_role_activity where fra_dept_code=? AND fra_role_code=? ";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, depCode);
			stmt.setString(2, roleCode);
			rs = stmt.executeQuery();

			while (rs.next()) {
				manageRoleDTO.setFunActivityDepCode(rs.getString("fra_dept_code"));
				manageRoleDTO.setFunActivityRoleCode(rs.getString("fra_role_code"));
			}

			if (manageRoleDTO.getFunActivityDepCode().isEmpty() && manageRoleDTO.getFunActivityRoleCode().isEmpty()) {
				isroleActived = false;
			} else {
				isroleActived = true;
			}

		} catch (Exception e) {
			isroleActived = false;

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return isroleActived;
	}

	@Override
	public int insertNewRole(String newRoleCode, String depcode, String newRoleName, String newStatus,
			String createdBy) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "INSERT INTO public.nt_r_role (code, description, departmentcode, active, created_by, created_date) VALUES(?,?, ?, ?, ?, ?);";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, newRoleCode);
			stmt.setString(2, newRoleName);
			stmt.setString(3, depcode);
			stmt.setString(4, "A");
			stmt.setString(5, createdBy);
			stmt.setTimestamp(6, timestamp);

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
	public ManageRoleDTO getCurrentDepartmentDes(String depcode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		ManageRoleDTO manageRoleDTO = new ManageRoleDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT description FROM public.nt_r_department where code=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, depcode);
			rs = ps.executeQuery();

			while (rs.next()) {
				manageRoleDTO.setDepartmentDescription(rs.getString("description"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return manageRoleDTO;
	}

	String searchFiled;

	public String getSearchFiled() {
		return searchFiled;
	}

	public void setSearchFiled(String searchFiled) {
		this.searchFiled = searchFiled;
	}

	public List<AttorneyHolderDTO> attorneyCheck(AttorneyHolderDTO attorneyDTO) {

		List<AttorneyHolderDTO> attorneyCheckList = new ArrayList<AttorneyHolderDTO>(0);

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT DISTINCT *" + "FROM nt_t_pm_vehi_owner";

			boolean whereadded = false;
			if (attorneyDTO.getPermit_No() != null && !attorneyDTO.getPermit_No().equals("")) {
				query = query + " where pmo_permit_no='" + attorneyDTO.getPermit_No() + "'";
				whereadded = true;
			}
			if (attorneyDTO.getApplication_No() != null && !attorneyDTO.getApplication_No().equals("")) {
				if (whereadded) {
					query = query + " and pmo_application_no='" + attorneyDTO.getApplication_No() + "'";
				} else {
					query = query + " where pmo_application_no='" + attorneyDTO.getApplication_No() + "'";
					whereadded = true;
				}
			}

			if (attorneyDTO.getVehicle_No() != null && !attorneyDTO.getVehicle_No().equals("")) {
				if (whereadded) {
					query = query + " and pmo_vehicle_regno='" + attorneyDTO.getVehicle_No() + "'";
				} else {
					query = query + " where pmo_vehicle_regno='" + attorneyDTO.getVehicle_No() + "'";
					whereadded = true;
				}
			}

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				attorneyDTO.setCheck_Permit_No(rs.getString("pmo_permit_no"));
				attorneyDTO.setCheck_Application_No(rs.getString("pmo_application_no"));
				;
				attorneyDTO.setCheck_Vehicle_No(rs.getString("pmo_vehicle_regno"));
				;

				attorneyCheckList.add(attorneyDTO);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return attorneyCheckList;

	}

	public int AttorneyHolderInfo(AttorneyHolderDTO attorneyDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date currentdate = new Date();

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_atterny_holder");

			String sql = "INSERT INTO public.nt_m_atterny_holder"
					+ "(seq, ath_permit_no, ath_application_no, ath_vehicle_regno, ath_preferred_lan, ath_curr_owner_full_name, ath_curr_owner_add_1, ath_curr_owner_add_2, ath_curr_owner_city, ath_title, ath_gender, ath_dob, ath_nic_no, ath_fullname, ath_full_name_sinhala, ath_full_name_tamil, ath_name_with_initial, ath_tel_no, ath_mobile_no, ath_status, ath_address1, ath_address1_sinhala, ath_address1_tamil, ath_address2, ath_address2_sinhala, ath_address2_tamil, ath_city, ath_city_sinhala, ath_city_tamil, ath_valid_from, ath_created_date, ath_created_by)"
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);

			stmt.setString(2, attorneyDTO.getPermit_No());
			stmt.setString(3, attorneyDTO.getApplication_No());
			stmt.setString(4, attorneyDTO.getVehicle_No());
			stmt.setString(5, attorneyDTO.getLanguage());
			stmt.setString(6, attorneyDTO.getCo_Name());
			stmt.setString(7, attorneyDTO.getCo_Address1());
			stmt.setString(8, attorneyDTO.getCo_Address2());
			stmt.setString(9, attorneyDTO.getCo_City());
			stmt.setString(10, attorneyDTO.getPa_Title());
			stmt.setString(11, attorneyDTO.getGender());
			if (attorneyDTO.getDob() != null) {
				String dob = (dateFormat.format(attorneyDTO.getDob()));
				stmt.setString(12, dob);
			} else {
				stmt.setString(12, null);
			}

			stmt.setString(2, attorneyDTO.getPermit_No());
			stmt.setString(3, attorneyDTO.getApplication_No());
			stmt.setString(4, attorneyDTO.getVehicle_No());
			stmt.setString(5, attorneyDTO.getLanguage());
			stmt.setString(6, attorneyDTO.getCo_Name());
			stmt.setString(7, attorneyDTO.getCo_Address1());
			stmt.setString(8, attorneyDTO.getCo_Address2());
			stmt.setString(9, attorneyDTO.getCo_City());
			stmt.setString(10, attorneyDTO.getPa_Title());
			stmt.setString(11, attorneyDTO.getGender());
			if (attorneyDTO.getDob() != null) {
				String dob = (dateFormat.format(attorneyDTO.getDob()));
				stmt.setString(12, dob);
			} else {
				stmt.setString(12, null);
			}

			stmt.setString(2, attorneyDTO.getPermit_No());
			stmt.setString(3, attorneyDTO.getApplication_No());
			stmt.setString(4, attorneyDTO.getVehicle_No());
			stmt.setString(5, attorneyDTO.getLanguage());
			stmt.setString(6, attorneyDTO.getCo_Name());
			stmt.setString(7, attorneyDTO.getCo_Address1());
			stmt.setString(8, attorneyDTO.getCo_Address2());
			stmt.setString(9, attorneyDTO.getCo_City());
			stmt.setString(10, attorneyDTO.getTitle());
			stmt.setString(11, attorneyDTO.getPa_Gender());
			if (attorneyDTO.getDob() != null) {
				String dob = (dateFormat.format(attorneyDTO.getDob()));
				stmt.setString(12, dob);
			} else {
				stmt.setString(12, null);
			}

			stmt.setString(13, attorneyDTO.getPa_Nic_or_Org());
			stmt.setString(14, attorneyDTO.getPa_FullName_Eng());
			stmt.setString(15, attorneyDTO.getPa_FullName_Sin());
			stmt.setString(16, attorneyDTO.getPa_FullName_Tamil());
			stmt.setString(17, attorneyDTO.getPa_NameWithInitial());
			stmt.setString(18, attorneyDTO.getPa_TeleNum());
			stmt.setString(19, attorneyDTO.getPa_MobileNum());
			stmt.setString(20, attorneyDTO.getStatus());
			stmt.setString(21, attorneyDTO.getPa_Address1_Eng());
			stmt.setString(22, attorneyDTO.getPa_Address1_Sin());
			stmt.setString(23, attorneyDTO.getPa_Address1_Tamil());
			stmt.setString(24, attorneyDTO.getPa_Address2_Eng());
			stmt.setString(25, attorneyDTO.getPa_Address2_Sin());
			stmt.setString(26, attorneyDTO.getPa_Address2_Tamil());
			stmt.setString(27, attorneyDTO.getPa_City_Eng());
			stmt.setString(28, attorneyDTO.getPa_City_Sin());
			stmt.setString(29, attorneyDTO.getPa_City_Tamil());
			stmt.setString(30, formatter.format(currentdate));
			stmt.setTimestamp(31, timestamp);
			stmt.setString(32, attorneyDTO.getUserName());

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

	public List<AttorneyHolderDTO> attorney(AttorneyHolderDTO attorneyDTO) {

		List<AttorneyHolderDTO> attorneyList = new ArrayList<AttorneyHolderDTO>(0);

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT DISTINCT *" + "FROM nt_t_pm_vehi_owner";

			boolean whereadded = false;
			if (attorneyDTO.getPermit_No() != null && !attorneyDTO.getPermit_No().equals("")) {
				query = query + " where pmo_permit_no='" + attorneyDTO.getPermit_No() + "'";
				whereadded = true;
			}
			if (attorneyDTO.getApplication_No() != null && !attorneyDTO.getApplication_No().equals("")) {
				if (whereadded) {
					query = query + " and pmo_application_no='" + attorneyDTO.getApplication_No() + "'";
				} else {
					query = query + " where pmo_application_no='" + attorneyDTO.getApplication_No() + "'";
					whereadded = true;
				}
			}

			if (attorneyDTO.getVehicle_No() != null && !attorneyDTO.getVehicle_No().equals("")) {
				if (whereadded) {
					query = query + " and pmo_vehicle_regno='" + attorneyDTO.getVehicle_No() + "'";
				} else {
					query = query + " where pmo_vehicle_regno='" + attorneyDTO.getVehicle_No() + "'";
					whereadded = true;
				}
			}

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				attorneyDTO.setCo_Name(rs.getString("pmo_full_name"));
				attorneyDTO.setCo_Address1(rs.getString("pmo_address1"));
				attorneyDTO.setCo_Address2(rs.getString("pmo_address2"));
				attorneyDTO.setCo_City(rs.getString("pmo_city"));

				attorneyList.add(attorneyDTO);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return attorneyList;

	}

	public List<SearchEmployeeDTO> searchEmployee(SearchEmployeeDTO searchEmployeeDTO) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SearchEmployeeDTO> searchList = new ArrayList<SearchEmployeeDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct a.emp_no as emp_no" + "    , a.emp_fullname as emp_fullname"
					+ "    , a.emp_no as emp_no" + "    , a.emp_nic_no as emp_nic_no"
					+ "    , case when a.emp_status = 'A' then 'ACTIVE'  when a.emp_status = 'P' then 'PENDING' when a.emp_status = 'R' then 'REJECT'  when a.emp_status = 'I' then 'INACTIVE' else a.emp_status end as emp_status "
					+ "   ,a.emp_epf_no as emp_epf_no" + "   ,a.emp_location as emp_location"
					+ "   ,a.emp_user_id as emp_user_id  " + "   ,a.emp_req_account as emp_req "
					+ "    ,h.description as locationname "

					+ " FROM public.nt_m_employee a " + "left outer join  public.nt_m_emp_designation e "
					+ "	on a.emp_no=e.des_emp_no " + "left outer join public.nt_r_location h "
					+ "   on a.emp_location=h.code ";

			boolean whereadded = false;
			if (searchEmployeeDTO.getEpfNo() != null && !searchEmployeeDTO.getEpfNo().equals("")) {
				query = query + " WHERE emp_epf_no='" + searchEmployeeDTO.getEpfNo() + "'";
				whereadded = true;
			}
			if (searchEmployeeDTO.getNicNo() != null && !searchEmployeeDTO.getNicNo().equals("")) {
				if (whereadded) {
					query = query + " and emp_nic_no='" + searchEmployeeDTO.getNicNo() + "'";
				} else {
					query = query + " where emp_nic_no='" + searchEmployeeDTO.getNicNo() + "'";
					whereadded = true;
				}
			}

			if (searchEmployeeDTO.getEmpLocation() != null && !searchEmployeeDTO.getEmpLocation().equals("")) {
				if (whereadded) {
					query = query + " and h.description='" + searchEmployeeDTO.getEmpLocation() + "'";
				} else {
					query = query + " where h.description='" + searchEmployeeDTO.getEmpLocation() + "'";
					whereadded = true;
				}
			}

			if (searchEmployeeDTO.getEmpNo() != null && !searchEmployeeDTO.getEmpNo().equals("")) {
				if (whereadded) {
					query = query + " and emp_no='" + searchEmployeeDTO.getEmpNo() + "'";
				} else {
					query = query + " where emp_no='" + searchEmployeeDTO.getEmpNo() + "'";
					whereadded = true;
				}
			}

			if (searchEmployeeDTO.getEmpStatus() != null && !searchEmployeeDTO.getEmpStatus().equals("")) {
				if (whereadded) {
					query = query + " and emp_status='" + searchEmployeeDTO.getEmpStatus() + "'";
				} else {
					query = query + " where emp_status='" + searchEmployeeDTO.getEmpStatus() + "'";
					whereadded = true;
				}
			}

			if (searchEmployeeDTO.getUserId() != null && !searchEmployeeDTO.getUserId().equals("")) {
				if (whereadded) {
					query = query + " and emp_user_id='" + searchEmployeeDTO.getUserId() + "'";
				} else {
					query = query + " where emp_user_id='" + searchEmployeeDTO.getUserId() + "'";
					whereadded = true;
				}
			}
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				String departmentName = getDepartment(rs.getString("emp_no"));
				String designationName = getDesignation(rs.getString("emp_no"));
				SearchEmployeeDTO searchDTO = new SearchEmployeeDTO();
				searchDTO.setEpfNo(rs.getString("emp_epf_no"));
				searchDTO.setFullName(rs.getString("emp_fullname"));
				searchDTO.setNicNo(rs.getString("emp_nic_no"));
				searchDTO.setEmpNo(rs.getString("emp_no"));
				searchDTO.setEmpStatus(rs.getString("emp_status"));

				searchDTO.setDepartment(departmentName);
				searchDTO.setDesignation(designationName);
				if (rs.getString("emp_req") != null) {
					searchDTO.setAccountstring(rs.getString("emp_req"));
				}

				searchList.add(searchDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return searchList;

	}

	@Override
	public List<SearchEmployeeDTO> getsearchEmployeeDetails(String empNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAllEmpNumbers() {
		// TODO Auto-generated method stub
		return null;
	}

	public String checkbutton() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();
			SearchBackingBean obj = new SearchBackingBean();
			String query = "select distinct b.fra_function_code,b.fra_activity_code as activity_code "
					+ "from nt_t_granted_user_role a, nt_r_fun_role_activity b "
					+ "where a.gur_role_code = b.fra_role_code " + " and a.gur_user_id='" + sessionBackingBean.loginUser
					+ "'" + "and b.fra_function_code = 'FN1_2' "

			;
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				if (rs.getString("activity_code").equals("E")) {
					obj.setEdit(true);

				}
				if (rs.getString("activity_code").equals("V")) {
					obj.setView(true);
				} else if (!(rs.getString("activity_code").equals("E") || rs.getString("activity_code").equals("V"))) {

				}

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return null;
	}

	@Override
	public int deleteCurrentRoleDetails(String deleteRoleCode) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "DELETE FROM public.nt_r_role WHERE code=?;";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, deleteRoleCode);

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
	public List<ManageRoleDTO> serachAllDetailsForDep(String dep) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<ManageRoleDTO> searchList = new ArrayList<ManageRoleDTO>();
		try {
			con = ConnectionManager.getConnection();
			String query = "SELECT a.code as depcode,b.code as rolecode,a.description,b.description as roledes,b.active FROM public.nt_r_department a,nt_r_role b where a.code=b.departmentcode and a.code=? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, dep);

			rs = ps.executeQuery();

			while (rs.next()) {
				ManageRoleDTO manageRoleDTO1 = new ManageRoleDTO();

				manageRoleDTO1.setDepartmentDescription(rs.getString("description"));
				manageRoleDTO1.setRoleName(rs.getString("roledes"));
				manageRoleDTO1.setRoleStatus("Active");
				manageRoleDTO1.setRoleCode(rs.getString("rolecode"));
				manageRoleDTO1.setDepCode(rs.getString("depcode"));
				searchList.add(manageRoleDTO1);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return searchList;
	}

	public String getDesignation(String empNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String department = null;
		try {

			con = ConnectionManager.getConnection();

			String query3 = "select c.description "
					+ "from nt_m_employee a inner join nt_m_emp_designation  b on a.emp_no = b.des_emp_no "
					+ "inner join nt_r_designation c on b.des_desig_code = c.code " + "where b.des_active='A' "
					+ "and emp_no =?;";

			ps = con.prepareStatement(query3);
			ps.setString(1, empNo);
			rs = ps.executeQuery();

			while (rs.next()) {

				department = rs.getString("description");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return department;
	}

	// Check EPF No
	public String isEpfNoAvilable(String epfNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String strEpfNo = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select emp_epf_no" + " from nt_m_employee " + " where emp_epf_no = ? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, epfNo);
			rs = ps.executeQuery();

			while (rs.next()) {

				strEpfNo = (rs.getString("emp_epf_no"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return strEpfNo;

	}

	// Check NIC No
	public String isNICNoAvilable(String nicNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String strEpfNo = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select emp_nic_no" + " from nt_m_employee " + " where emp_nic_no = ? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, nicNo);
			rs = ps.executeQuery();

			while (rs.next()) {

				strEpfNo = (rs.getString("emp_nic_no"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return strEpfNo;

	}

	public List<String> getAllVehiclePermit() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> data = new ArrayList<String>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT distinct pmo_permit_no FROM public.nt_t_pm_vehi_owner where pmo_permit_no is not null";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				data.add(rs.getString("pmo_permit_no"));

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

	public List<String> getAllApplicationNo() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> data = new ArrayList<String>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT distinct pmo_application_no FROM public.nt_t_pm_vehi_owner where pmo_application_no is not null";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				data.add(rs.getString("pmo_application_no"));

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

	public List<String> getAllVehicleNo() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> data = new ArrayList<String>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT distinct pmo_vehicle_regno FROM public.nt_t_pm_vehi_owner where pmo_vehicle_regno is not null";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				data.add(rs.getString("pmo_vehicle_regno"));

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

	public String getStatus(AttorneyHolderDTO attorneyDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String status = null;
		String permit_no = attorneyDTO.getPermit_No();
		try {

			con = ConnectionManager.getConnection();

			String query3 = "select ath_status from nt_m_atterny_holder where ath_permit_no='" + permit_no + "'";

			ps = con.prepareStatement(query3);

			rs = ps.executeQuery();

			while (rs.next()) {

				status = rs.getString("ath_status");

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

	public String getApplication_No_status(AttorneyHolderDTO attorneyDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String status = null;
		String application = attorneyDTO.getApplication_No();
		try {

			con = ConnectionManager.getConnection();

			String query3 = "select ath_status from nt_m_atterny_holder where ath_application_no='" + application + "'";

			ps = con.prepareStatement(query3);

			rs = ps.executeQuery();

			while (rs.next()) {

				status = rs.getString("ath_status");

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

	public String getVehicle_No_status(AttorneyHolderDTO attorneyDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String status = null;
		String vehicle = attorneyDTO.getVehicle_No();
		try {

			con = ConnectionManager.getConnection();

			String query3 = "select ath_status from nt_m_atterny_holder where ath_vehicle_regno='" + vehicle + "'";

			ps = con.prepareStatement(query3);

			rs = ps.executeQuery();

			while (rs.next()) {

				status = rs.getString("ath_status");

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
	public List<EmployeeDTO> getPendingEmpDetails() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isReqAccount = false;

		List<EmployeeDTO> employeeDTO = new ArrayList<EmployeeDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select distinct a.emp_no, a.emp_epf_no, a.emp_fullname, "
					+ "a.emp_nic_no,  a.emp_user_id, a.emp_req_account, nt_m_emp_department.dep_dept_code as depCode,nt_m_emp_department.dep_active, "
					+ "nt_m_emp_designation.des_desig_code,nt_m_emp_designation.des_active, "
					+ "(CASE WHEN a.emp_status ='P'  THEN 'PENDING' " + "when a.emp_status = 'R' THEN 'REJECT' WHEN "
					+ "a.emp_status = 'A' THEN 'APPROVED' END) as emp_status, nt_r_designation.description as designation, nt_r_department.description as department, nt_r_location.description as location "
					+ "from public.nt_m_employee a "
					+ "inner join nt_m_emp_department on a.emp_no = nt_m_emp_department.dep_emp_no "
					+ "inner join nt_m_emp_designation on a.emp_no = nt_m_emp_designation.des_emp_no "
					+ "inner join nt_r_department  on  nt_m_emp_department.dep_dept_code= nt_r_department.code "
					+ "inner join nt_r_designation  on  nt_m_emp_designation.des_desig_code= nt_r_designation.code "
					+ "left outer join public.nt_r_location on a.emp_location = nt_r_location.code "
					+ "where emp_status='P' and nt_m_emp_designation.des_active = 'A' and nt_m_emp_department.dep_active ='A' ; ";

			EmployeeDTO e;
			ps = con.prepareStatement(query2);

			rs = ps.executeQuery();

			while (rs.next()) {

				e = new EmployeeDTO();
				e.setEmpNo(rs.getString("emp_no"));
				e.setEmpEpfNo(rs.getString("emp_epf_no"));
				e.setFullName(rs.getString("emp_fullname"));
				e.setNicNo(rs.getString("emp_nic_no"));

				if (rs.getString("emp_req_account") != null) {

					if (rs.getString("emp_req_account").equals("y")) {
						isReqAccount = true;
					} else {
						isReqAccount = false;
					}
				} else {
					isReqAccount = false;
				}
				e.setReqBool(isReqAccount);
				e.setStatus(rs.getString("emp_status"));
				e.setDesignation(rs.getString("designation"));
				e.setDepartment(rs.getString("department"));
				e.setDepCode(rs.getString("depCode"));

				employeeDTO.add(e);
			}

			return employeeDTO;

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
	public String checkCurrentStatusForEmp(String employeeNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String currentStatus = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT emp_status FROM public.nt_m_employee where emp_no=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, employeeNo);
			rs = ps.executeQuery();

			while (rs.next()) {

				currentStatus = (rs.getString("emp_status"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return currentStatus;
	}

}
