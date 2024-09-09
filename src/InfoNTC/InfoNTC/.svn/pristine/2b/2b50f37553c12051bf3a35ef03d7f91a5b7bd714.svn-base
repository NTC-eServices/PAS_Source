package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.informatics.ntc.model.dto.CommittedOffencesDTO;
import lk.informatics.ntc.model.dto.ComplaintRequestDTO;
import lk.informatics.ntc.model.dto.FlyingManageInvestigationLogDTO;
import lk.informatics.ntc.model.dto.ManageInquiryDTO;
import lk.informatics.ntc.model.dto.ParamerDTO;
import lk.informatics.ntc.model.dto.SimRegistrationDTO;
import lk.informatics.ntc.model.dto.VehicleInspectionDTO;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.ntc.view.util.PropertyReader;
import lk.informatics.roc.utils.common.Utils;

public class ManageInquiryServiceImpl implements ManageInquiryService {

	@Override
	public VehicleInspectionDTO retrieveVehicleInfo(String vehicleNum) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		VehicleInspectionDTO dto = new VehicleInspectionDTO();
		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT a.pm_permit_no,a.pm_permit_expire_date,a.pm_renewal_period,a.pm_valid_from,a.pm_valid_to,b.pmo_name_with_initial,c.description FROM  "
					+ "public.nt_t_pm_application a,public.nt_t_pm_vehi_owner b,public.nt_r_service_types c where a.pm_vehicle_regno='"
					+ vehicleNum + "' and a.pm_status='A' "
					+ "and b.pmo_application_no=a.pm_application_no and a.pm_service_type=c.code order by a.seq desc limit 1";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				dto.setPermitOwner(rs.getString("pmo_name_with_initial"));
				dto.setPermitNo(rs.getString("pm_permit_no"));
				dto.setExpireDate(rs.getString("pm_permit_expire_date"));
				dto.setRenewalPeriod(rs.getInt("pm_renewal_period"));
				dto.setValidFrom(rs.getString("pm_valid_from"));
				dto.setValidTo(rs.getString("pm_valid_to"));
				dto.setServiceType(rs.getString("description"));
				dto.setVehicleNo(vehicleNum);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return dto;
	}

	@Override
	public List<String> retrieveComplaintNumbers() {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		List<String> complaintNumList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT DISTINCT complain_no FROM public.nt_m_complain_request order by complain_no";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				complaintNumList.add(rs.getString("complain_no"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return complaintNumList;
	}
	
	@Override
	public List<String> retrieveComplaintNumbersForManageInquiry() {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		List<String> complaintNumList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT DISTINCT complain_no FROM public.nt_m_complain_request where type_of_contact in ('C','F') order by complain_no";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				complaintNumList.add(rs.getString("complain_no"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return complaintNumList;
	}

	@Override
	public List<String> retrieveComplaintNumbersForManageInquiry(ManageInquiryDTO dto) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		List<String> complaintNumList = new ArrayList<String>();
		String WHERE_SQL = "";

		if (dto.getAuthority() != null && !dto.getAuthority().trim().equals("")) {
			WHERE_SQL = WHERE_SQL + " and permit_authority='" + dto.getAuthority() + "' ";
		}

		if (dto.getProvince() != null && !dto.getProvince().trim().equals("")) {
			WHERE_SQL = WHERE_SQL + " and province='" + dto.getProvince() + "' ";
		}

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT DISTINCT complain_no FROM public.nt_m_complain_request WHERE complain_no is not null and complain_no !='' and type_of_contact in ('C','F') "
					+ WHERE_SQL + " order by complain_no";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				complaintNumList.add(rs.getString("complain_no"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return complaintNumList;
	}

	@Override
	public List<ManageInquiryDTO> retrieveSeverity() {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		List<ManageInquiryDTO> severityList = new ArrayList<ManageInquiryDTO>();

		try {

			con = ConnectionManager.getConnection();

			String sql = "SELECT code, description FROM public.nt_r_severity where active='A'";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				ManageInquiryDTO dto = new ManageInquiryDTO();
				dto.setSeverityCode(rs.getString("code"));
				dto.setSeverityDesc(rs.getString("description"));
				severityList.add(dto);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return severityList;
	}

	@Override
	public List<ManageInquiryDTO> retrievePriorityOrderList() {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		List<ManageInquiryDTO> priorityList = new ArrayList<ManageInquiryDTO>();

		try {

			con = ConnectionManager.getConnection();

			String sql = "SELECT code, description FROM public.nt_r_priority_order ORDER BY code;";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				ManageInquiryDTO dto = new ManageInquiryDTO();
				dto.setPrioirtyOrderCode(rs.getString("code"));
				dto.setPriorityOrderDesc(rs.getString("description"));
				priorityList.add(dto);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return priorityList;
	}

	@Override
	public List<ManageInquiryDTO> retrieveActionDepartmentsList() {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		List<ManageInquiryDTO> priorityList = new ArrayList<ManageInquiryDTO>();

		try {

			con = ConnectionManager.getConnection();

			String sql = "SELECT code, description FROM public.nt_r_action_department WHERE status='A' ORDER BY description;";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				ManageInquiryDTO dto = new ManageInquiryDTO();
				dto.setActionDepartment(rs.getString("code"));
				dto.setActionDepartment_desc(rs.getString("description"));
				priorityList.add(dto);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return priorityList;
	}

	@Override
	public List<ManageInquiryDTO> retrieveInquiryComplaintData(ManageInquiryDTO manageInquiryDTO, String user){

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		List<ManageInquiryDTO> complaintInfoDataList = new ArrayList<ManageInquiryDTO>();

		String where = "";
		
		java.util.Date date1 = new java.util.Date();
		Timestamp timestamp = new Timestamp(date1.getTime());

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		try {
			con = ConnectionManager.getConnection();

			if (manageInquiryDTO.getPrioirtyOrderCode() != null && !manageInquiryDTO.getPrioirtyOrderCode().isEmpty()
					&& !manageInquiryDTO.getPrioirtyOrderCode().equals("")) {
				where = " and priority_order='" + manageInquiryDTO.getPrioirtyOrderCode() + "'";
			}

			if (manageInquiryDTO.getSeverityCode() != null && !manageInquiryDTO.getSeverityCode().isEmpty()
					&& !manageInquiryDTO.getSeverityCode().equals("")) {
				if (where != null && !where.isEmpty() && !where.equals("")) {
					where = where + " and severity_no='" + manageInquiryDTO.getSeverityCode() + "'";
				} else {
					where = " and severity_no='" + manageInquiryDTO.getSeverityCode() + "'";
				}
			}

			if (manageInquiryDTO.getComplainNo() != null && !manageInquiryDTO.getComplainNo().isEmpty()
					&& !manageInquiryDTO.getComplainNo().trim().equals("")) {
				if (where != null && !where.isEmpty() && !where.equals("")) {
					where = where + " and complain_no='" + manageInquiryDTO.getComplainNo() + "'";
				} else {
					where = " and complain_no='" + manageInquiryDTO.getComplainNo() + "'";
				}
			}

			if (manageInquiryDTO.getVehicleNum() != null && !manageInquiryDTO.getVehicleNum().isEmpty()
					&& !manageInquiryDTO.getVehicleNum().trim().equals("")) {
				if (where != null && !where.isEmpty() && !where.equals("")) {
					where = where + " and vehicle_no='" + manageInquiryDTO.getVehicleNum() + "'";
				} else {
					where = " and vehicle_no='" + manageInquiryDTO.getVehicleNum() + "'";
				}
			}

			if (manageInquiryDTO.getStartDate() != null && manageInquiryDTO.getEndDate() != null) {
				String startDate = dateFormat.format(manageInquiryDTO.getStartDate());
				String endDate = dateFormat.format(manageInquiryDTO.getEndDate());
				if (where != null && !where.isEmpty() && !where.equals("")) {

					where = where + " and ((to_char((nt_m_complain_request.created_date),'yyyy-MM-dd') >= '"
							+ startDate + "' " + "AND to_char((nt_m_complain_request.created_date),'yyyy-MM-dd') < '"
							+ endDate + "') " + "or (to_char((nt_m_complain_request.created_date),'yyyy-MM-dd') = '"
							+ endDate + "' " + "or to_char((nt_m_complain_request.created_date),'yyyy-MM-dd') = '"
							+ endDate + "')) ";
				} else {

					where = " and ((to_char((nt_m_complain_request.created_date),'yyyy-MM-dd') >= '" + startDate
							+ "' " + "AND to_char((nt_m_complain_request.created_date),'yyyy-MM-dd') < '" + endDate
							+ "') " + "or (to_char((nt_m_complain_request.created_date),'yyyy-MM-dd') = '" + endDate
							+ "' " + "or to_char((nt_m_complain_request.created_date),'yyyy-MM-dd') = '" + endDate
							+ "')) ";
				}
			}
     /***event_datetime filed change into created_date field according to Pramitha's request on 30/03/2022***/
			if (manageInquiryDTO.getAuthority() != null && !manageInquiryDTO.getAuthority().trim().equals("")) {
				if (manageInquiryDTO.getAuthority().equals("CTB")) {
					where = where + " and nt_m_complain_request.permit_authority='" + manageInquiryDTO.getAuthority()
							+ "' and nt_m_complain_request.process_status='P' ";
				} else {
					where = where + " and nt_m_complain_request.permit_authority='" + manageInquiryDTO.getAuthority()
							+ "' ";
				}
			}

			if (manageInquiryDTO.getProvince() != null && !manageInquiryDTO.getProvince().trim().equals("")) {
				where = where + " and nt_m_complain_request.province='" + manageInquiryDTO.getProvince() + "' ";
			}

			String sql = "select b.description,c.description as prio_desc,d.status as action_status,e.description as province_desc,*\r\n"
					+ "from public.nt_m_complain_request " + "left join public.nt_r_severity b on severity_no = b.code "
					+ "left join public.nt_r_priority_order c on priority_order = c.code "
					+ "left join public.nt_m_complain_action d on complain_no = d.complaint_no "
					+ "left join public.nt_r_province e on province = e.code " + "where type_of_contact in ('C','F') "
					+ where + "order by nt_m_complain_request.event_datetime desc ";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				ManageInquiryDTO dto = new ManageInquiryDTO();
				dto.setAuthority(rs.getString("permit_authority"));
				dto.setPrioirtyOrderCode(rs.getString("priority_order"));
				dto.setPriorityOrderDesc(rs.getString("prio_desc"));
				dto.setSeverityCode(rs.getString("severity_no"));
				dto.setSeverityDesc(rs.getString("description"));
				dto.setVehicleNum(rs.getString("vehicle_no"));
				dto.setPermitNum(rs.getString("permit_no"));
				dto.setComplainNo(rs.getString("complain_no"));
				dto.setProvince(rs.getString("province"));
				dto.setProvince_desc(rs.getString("province_desc"));
				dto.setActionDepartment(rs.getString("action_department_code"));
				dto.setSpecialRemark(rs.getString("special_remark"));
				dto.setClosedDate(rs.getString("closed_date"));
				
				if (rs.getTimestamp("event_datetime") != null) {
					Timestamp ts = rs.getTimestamp("event_datetime");
					Date eventDate = new Date(ts.getTime());
					dto.setEventDate(eventDate);
				}

				if (rs.getDate("investigation_date") != null) {
					SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
					String dateString = format.format(rs.getDate("investigation_date"));
					Date date = format.parse(dateString);
					dto.setInvestigationDate(date);
					dto.setInvestigationDateStr(dateString);
				}

				if (rs.getString("inq_reserve_start_date") != null && !rs.getString("inq_reserve_start_date").isEmpty()
						&& !rs.getString("inq_reserve_start_date").trim().equals("")
						&& rs.getString("inq_reserve_end_date") != null
						&& !rs.getString("inq_reserve_end_date").isEmpty()
						&& !rs.getString("inq_reserve_end_date").trim().equals("")) {
					dto.setInvestigationTime(
							rs.getString("inq_reserve_start_date") + " : " + rs.getString("inq_reserve_end_date"));
				}

				dto.setProcess_status(rs.getString("process_status"));
				if (rs.getString("process_status") != null && !rs.getString("process_status").trim().isEmpty()) {
					if (rs.getString("process_status").equalsIgnoreCase("O")) {
						dto.setProcess_status_des("Ongoing");
					} else if (rs.getString("process_status").equalsIgnoreCase("C")) {
						dto.setProcess_status_des("Closed");
					} else if (rs.getString("process_status").equalsIgnoreCase("P")) {
						dto.setProcess_status_des("Pending");
					}
				}

				dto.setActionStatus(rs.getString("action_status"));
				if (rs.getString("action_status") != null && !rs.getString("action_status").trim().isEmpty()) {
					if (rs.getString("action_status").equalsIgnoreCase("O")) {
						dto.setActionStatus_desc("Ongoing");
					} else if (rs.getString("action_status").equalsIgnoreCase("C")) {
						dto.setActionStatus_desc("Closed");
					} else if (rs.getString("action_status").equalsIgnoreCase("P")) {
						dto.setActionStatus_desc("Pending");
					}
				}

				complaintInfoDataList.add(dto);
			}
			
		

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return complaintInfoDataList;
	}

	@Override
	public List<ManageInquiryDTO> retreiveReservedTimes(String selectedDate) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		List<ManageInquiryDTO> dataList = new ArrayList<ManageInquiryDTO>();

		try {
			con = ConnectionManager.getConnection();

			String sql = "select inq_reserve_end_date, inq_reserve_start_date from public.nt_m_complain_request where "
					+ "to_char((nt_m_complain_request.investigation_date),'yyyy-MM-dd') = '" + selectedDate + "' "
					+ "and inq_reserve_end_date is not null and inq_reserve_start_date is not null ";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				ManageInquiryDTO dto = new ManageInquiryDTO();

				dto.setStartTime(rs.getString("inq_reserve_start_date"));
				dto.setEndTime(rs.getString("inq_reserve_end_date"));

				dataList.add(dto);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return dataList;
	}

	@Override
	public void updateInvetigationData(ManageInquiryDTO selectedData, String startTime, String endTime,
			String loginUser) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null, stmt1 = null;
		String where = "";
		try {
			con = ConnectionManager.getConnection();

			if (selectedData.getAuthority() != null && !selectedData.getAuthority().isEmpty()
					&& !selectedData.getAuthority().trim().equals("")) {
				where = " and permit_authority='" + selectedData.getAuthority() + "'";
			}

			if (selectedData.getPermitNum() != null && !selectedData.getPermitNum().isEmpty()
					&& !selectedData.getPermitNum().trim().equals("")) {
				if (where != null && !where.isEmpty() && !where.trim().equals("")) {
					where = where + " and permit_no='" + selectedData.getPermitNum() + "'";
				} else {
					where = " and permit_no='" + selectedData.getPermitNum() + "'";
					;
				}
			}

			if (selectedData.getVehicleNum() != null && !selectedData.getVehicleNum().isEmpty()
					&& !selectedData.getVehicleNum().trim().equals("")) {
				if (where != null && !where.isEmpty() && !where.trim().equals("")) {
					where = where + " and vehicle_no='" + selectedData.getVehicleNum() + "'";
				} else {
					where = " and vehicle_no='" + selectedData.getVehicleNum() + "'";
					;
				}
			}

			if (selectedData.getPrioirtyOrderCode() != null && !selectedData.getPrioirtyOrderCode().isEmpty()
					&& !selectedData.getPrioirtyOrderCode().trim().equals("")) {
				if (where != null && !where.isEmpty() && !where.trim().equals("")) {
					where = where + " and priority_order='" + selectedData.getPrioirtyOrderCode() + "'";
				} else {
					where = " and priority_order='" + selectedData.getPrioirtyOrderCode() + "'";
					;
				}
			}

			if (selectedData.getSeverityCode() != null && !selectedData.getSeverityCode().isEmpty()
					&& !selectedData.getSeverityCode().trim().equals("")) {
				if (where != null && !where.isEmpty() && !where.trim().equals("")) {
					where = where + " and severity_no='" + selectedData.getSeverityCode() + "'";
				} else {
					where = " and severity_no='" + selectedData.getSeverityCode() + "'";
					;
				}
			}

			java.util.Date date = new java.util.Date();
			Timestamp timestampDt = new Timestamp(date.getTime());

			/** added by dinushi.r on 27-05-2020 **/
			String sql1 = "INSERT into public.nt_h_complain_request  "
					+ " (SELECT * FROM public.nt_m_complain_request WHERE complain_no = ?) ";

			stmt1 = con.prepareStatement(sql1);
			stmt1.setString(1, selectedData.getComplainNo());
			stmt1.executeUpdate();
			/** end modification **/

			String sql = "UPDATE public.nt_m_complain_request SET investigation_date=?, inq_reserve_start_date=?, inq_reserve_end_date=?, modified_by=?, modified_date=?, process_status=? "
					+ "WHERE complain_no=? " + where;

			ps = con.prepareStatement(sql);
			ps.setDate(1, new java.sql.Date(selectedData.getAvailableDateSelect().getTime()));
			ps.setString(2, startTime);
			ps.setString(3, endTime);
			ps.setString(4, loginUser);
			ps.setTimestamp(5, timestampDt);
			ps.setString(6, "O"); // added by dinushi.r on 27-05-2020
			ps.setString(7, selectedData.getComplainNo());

			ps.executeUpdate();

			con.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(stmt1);
			ConnectionManager.close(con);
		}
	}

	@Override
	public void deleteInvetigationDataAndTime(ManageInquiryDTO selectedData, String startTime, String endTime,
			String loginUser) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String where = "";
		
		java.util.Date date1 = new java.util.Date();
		Timestamp timestamp = new Timestamp(date1.getTime());
		try {
			con = ConnectionManager.getConnection();

			if (selectedData.getAuthority() != null && !selectedData.getAuthority().isEmpty()
					&& !selectedData.getAuthority().trim().equals("")) {
				where = " and permit_authority='" + selectedData.getAuthority() + "'";
			}

			if (selectedData.getPermitNum() != null && !selectedData.getPermitNum().isEmpty()
					&& !selectedData.getPermitNum().trim().equals("")) {
				if (where != null && !where.isEmpty() && !where.trim().equals("")) {
					where = where + " and permit_no='" + selectedData.getPermitNum() + "'";
				} else {
					where = " and permit_no='" + selectedData.getPermitNum() + "'";
					;
				}
			}

			if (selectedData.getVehicleNum() != null && !selectedData.getVehicleNum().isEmpty()
					&& !selectedData.getVehicleNum().trim().equals("")) {
				if (where != null && !where.isEmpty() && !where.trim().equals("")) {
					where = where + " and vehicle_no='" + selectedData.getVehicleNum() + "'";
				} else {
					where = " and vehicle_no='" + selectedData.getVehicleNum() + "'";
					;
				}
			}

			if (selectedData.getPrioirtyOrderCode() != null && !selectedData.getPrioirtyOrderCode().isEmpty()
					&& !selectedData.getPrioirtyOrderCode().trim().equals("")) {
				if (where != null && !where.isEmpty() && !where.trim().equals("")) {
					where = where + " and priority_order='" + selectedData.getPrioirtyOrderCode() + "'";
				} else {
					where = " and priority_order='" + selectedData.getPrioirtyOrderCode() + "'";
					;
				}
			}

			if (selectedData.getSeverityCode() != null && !selectedData.getSeverityCode().isEmpty()
					&& !selectedData.getSeverityCode().trim().equals("")) {
				if (where != null && !where.isEmpty() && !where.trim().equals("")) {
					where = where + " and severity_no='" + selectedData.getSeverityCode() + "'";
				} else {
					where = " and severity_no='" + selectedData.getSeverityCode() + "'";
					;
				}
			}

			java.util.Date date = new java.util.Date();
			Timestamp timestampDt = new Timestamp(date.getTime());

			String sql = "UPDATE public.nt_m_complain_request SET investigation_date=?, inq_reserve_start_date=?, inq_reserve_end_date=?, modified_by=?, modified_date=? "
					+ "WHERE complain_no=? " + where;

			ps = con.prepareStatement(sql);
			ps.setNull(1, java.sql.Types.TIMESTAMP);
			ps.setNull(2, java.sql.Types.VARCHAR);
			ps.setNull(3, java.sql.Types.VARCHAR);
			ps.setString(4, loginUser);
			ps.setTimestamp(5, timestampDt);
			ps.setString(6, selectedData.getComplainNo());

			ps.executeUpdate();

			con.commit();
			
			

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
	}

	@Override
	public List<ManageInquiryDTO> retrieveBusDriverConductorData(String vehicleNum, String permitNum) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		List<ManageInquiryDTO> driverConductorList = new ArrayList<ManageInquiryDTO>();

		String where = "";

		try {
			con = ConnectionManager.getConnection();

			if (vehicleNum != null && !vehicleNum.isEmpty() && !vehicleNum.equals("")) {
				where = " dcr_vehicle_reg_no='" + vehicleNum + "'";
			}

			if (permitNum != null && !permitNum.isEmpty() && !permitNum.equals("")) {
				if (where != null && !where.isEmpty() && !where.equals("")) {
					where = where + " and dcr_permit_no='" + permitNum + "'";
				} else {
					where = " and dcr_permit_no='" + permitNum + "'";
				}
			}

			String sql = "SELECT distinct dcr_full_name_eng,dcr_driver_conductor_id,dcr_contact_no, dcr_add_1_eng, dcr_add_2_eng, dcr_city_eng "
					+ "FROM public.nt_t_driver_conductor_registration " + "where " + where;

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				ManageInquiryDTO dto = new ManageInquiryDTO();
				dto.setContactName(rs.getString("dcr_full_name_eng"));
				dto.setDriverConductorId(rs.getString("dcr_driver_conductor_id"));
				dto.setContactNum(rs.getString("dcr_contact_no"));
				dto.setContactAddress(rs.getString("dcr_add_1_eng") + ", " + rs.getString("dcr_add_2_eng") + ", "
						+ rs.getString("dcr_city_eng"));
				driverConductorList.add(dto);
			}

			ConnectionManager.close(rs);
			ConnectionManager.close(ps);

			where = "";
			if (vehicleNum != null && !vehicleNum.isEmpty() && !vehicleNum.equals("")) {
				where = " where pmo_vehicle_regno='" + vehicleNum + "'";
			}

			if (permitNum != null && !permitNum.isEmpty() && !permitNum.equals("")) {
				if (where != null && !where.isEmpty() && !where.equals("")) {
					where = where + " and pmo_permit_no='" + permitNum + "'";
				} else {
					where = " and pmo_permit_no='" + permitNum + "'";
				}
			}

			/**
			 * for show contact names which contact numbers are empty
			 **/

			String sql2 = "SELECT distinct pmo_full_name,pmo_mobile_no, pmo_address1, pmo_address2, pmo_address3, pmo_city "
					+ "FROM public.nt_t_pm_vehi_owner " + where;

			ps = con.prepareStatement(sql2);
			rs = ps.executeQuery();

			while (rs.next()) {
				ManageInquiryDTO dto = new ManageInquiryDTO();
				dto.setContactName(rs.getString("pmo_full_name"));
				dto.setContactNum(rs.getString("pmo_mobile_no"));
				dto.setContactAddress(rs.getString("pmo_address1") + ", " + rs.getString("pmo_address2") + ", "
						+ rs.getString("pmo_address3") + ", " + rs.getString("pmo_city"));
				driverConductorList.add(dto);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return driverConductorList;
	}

	@Override
	public String retrieveBusDriverConductorAddress(String contactName) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String address = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "select dcr_add_1_eng, dcr_add_2_eng, dcr_city_eng from public.nt_t_driver_conductor_registration where dcr_driver_conductor_id='"
					+ contactName + "'";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				if (rs.getString("dcr_add_1_eng") != null && !rs.getString("dcr_add_1_eng").isEmpty()
						&& !rs.getString("dcr_add_1_eng").trim().equals("")) {
					address = rs.getString("dcr_add_1_eng") + ", ";
				}
				if (rs.getString("dcr_add_2_eng") != null && !rs.getString("dcr_add_2_eng").isEmpty()
						&& !rs.getString("dcr_add_2_eng").trim().equals("")) {
					address = address + rs.getString("dcr_add_2_eng") + ", ";
				}
				if (rs.getString("dcr_city_eng") != null && !rs.getString("dcr_city_eng").isEmpty()
						&& !rs.getString("dcr_city_eng").trim().equals("")) {
					address = address + rs.getString("dcr_city_eng");
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return address;
	}

	@Override
	public boolean savePrintLetterData(ManageInquiryDTO sendMailDTO, String loginUser) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		boolean saved = false;

		try {
			con = ConnectionManager.getConnection();

			boolean letterExist = checkLetterDataExist(sendMailDTO.getContactName());

			if (letterExist) {

				String sql = "UPDATE public.nt_t_grievance_inquiry_letters "
						+ "SET receipient_adresss=?, notify_letter_sentence_one=?, notify_letter_sentence_two=?, notify_letter_sentence_three=?, modified_date=?, modified_by=? "
						+ "WHERE receipient_id=? ";

				ps = con.prepareStatement(sql);
				ps.setString(1, sendMailDTO.getContactAddress());
				ps.setString(2, sendMailDTO.getNotifySentenceOne());
				ps.setString(3, sendMailDTO.getNotifySentenceTwo());
				ps.setString(4, sendMailDTO.getNotifySentenceThree());
				ps.setTimestamp(5, timestamp);
				ps.setString(6, loginUser);
				ps.setString(7, sendMailDTO.getContactName());

				int savedCount = ps.executeUpdate();
				if (savedCount > 0) {
					saved = true;// updated
				} else {
					saved = false;// not updated
				}

			} else {
				long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_grievance_inquiry_letters");

				String q = "INSERT INTO public.nt_t_grievance_inquiry_letters "
						+ "(seq, receipient_id, receipient_adresss, notify_letter_sentence_one, notify_letter_sentence_two, notify_letter_sentence_three, created_date, created_by) "
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

				ps = con.prepareStatement(q);

				ps.setLong(1, seqNo);
				ps.setString(2, sendMailDTO.getContactName());
				ps.setString(3, sendMailDTO.getContactAddress());
				ps.setString(4, sendMailDTO.getNotifySentenceOne());
				ps.setString(5, sendMailDTO.getNotifySentenceTwo());
				ps.setString(6, sendMailDTO.getNotifySentenceThree());
				ps.setTimestamp(7, timestamp);
				ps.setString(8, loginUser);

				int savedCount = ps.executeUpdate();
				if (savedCount > 0) {
					saved = true;// saved
				} else {
					saved = false;// not saved
				}
			}
			ConnectionManager.close(ps);

			// check investigation alerts table
			boolean exist = checkDataAvailableInNt_r_investigation_alerts(sendMailDTO.getComplainNo(), true);
			if (!exist) {
				long seqNum = Utils.getNextValBySeqName(con, "seq_nt_r_investigation_alerts");

				String query = "INSERT INTO public.nt_r_investigation_alerts (seq, receipient_address, notify_mail_body_one, notify_mail_body_two, "
						+ "notify_mail_body_three, create_date, create_by, alert_type, complain_number, recepient_name) "
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				ps = con.prepareStatement(query);

				ps.setLong(1, seqNum);
				ps.setString(2, sendMailDTO.getContactAddress());
				ps.setString(3, sendMailDTO.getNotifySentenceOne());
				ps.setString(4, sendMailDTO.getNotifySentenceTwo());
				ps.setString(5, sendMailDTO.getNotifySentenceThree());
				ps.setTimestamp(6, timestamp);
				ps.setString(7, loginUser);
				ps.setString(8, "letter");
				ps.setString(9, sendMailDTO.getComplainNo());
				ps.setString(10, sendMailDTO.getContactName());

				ps.executeUpdate();
			} else {
				// update investigation alerts table
				String query = "UPDATE public.nt_r_investigation_alerts "
						+ "SET receipient_address=?, notify_mail_body_one=?, notify_mail_body_two=?, notify_mail_body_three=?, recepient_name=? "
						+ "WHERE complain_number=? and alert_type='letter'";

				ps = con.prepareStatement(query);
				ps.setString(1, sendMailDTO.getContactAddress());
				ps.setString(2, sendMailDTO.getNotifySentenceOne());
				ps.setString(3, sendMailDTO.getNotifySentenceTwo());
				ps.setString(4, sendMailDTO.getNotifySentenceThree());
				ps.setString(5, sendMailDTO.getContactName());
				ps.setString(6, sendMailDTO.getComplainNo());

				ps.executeUpdate();
			}

			con.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return saved;
	}

	@Override
	public boolean checkLetterDataExist(String contactName) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean found = false;

		try {
			con = ConnectionManager.getConnection();

			String sql = "select * from public.nt_t_driver_conductor_registration where dcr_driver_conductor_id='"
					+ contactName + "'";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				found = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return found;
	}

	@Override
	public ManageInquiryDTO retrieveLetterDate(String contactName) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		ManageInquiryDTO dto = new ManageInquiryDTO();

		try {
			con = ConnectionManager.getConnection();

			String sql = "select * from public.nt_t_grievance_inquiry_letters where receipient_id='" + contactName
					+ "'";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				dto.setNotifySentenceOne(rs.getString("notify_letter_sentence_one"));
				dto.setNotifySentenceTwo(rs.getString("notify_letter_sentence_two"));
				dto.setNotifySentenceThree(rs.getString("notify_letter_sentence_three"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return dto;
	}

	@Override
	public String retrieveBusDriverConductorContactNumber(String contactName) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String contactNum = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "select dcr_contact_no from public.nt_t_driver_conductor_registration where dcr_driver_conductor_id='"
					+ contactName + "'";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				contactNum = rs.getString("dcr_contact_no");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return contactNum;
	}

	@Override
	public boolean checkSMSDataExist(String contactNumber) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean found = false;

		try {
			con = ConnectionManager.getConnection();

			String sql = "select * from public.nt_t_pending_alerts where receipient_mobileno='" + contactNumber
					+ "' and status is not null ";// TODO check status of SMS

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				found = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return found;
	}

	@Override
	public ManageInquiryDTO retrieveSMSDate(String contactNumber) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		ManageInquiryDTO messageDTO = new ManageInquiryDTO();

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT sms_message FROM public.nt_t_pending_alerts WHERE receipient_mobileno='"
					+ contactNumber + "' and status='P'";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				messageDTO.setNotifySentenceOne(rs.getString("sms_message"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return messageDTO;
	}

	@Override
	public boolean saveSMSData(ManageInquiryDTO sendMailDTO, String loginUser) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		boolean saved = false;

		try {
			con = ConnectionManager.getConnection();

			boolean messageExist = checkSMSDataExist(sendMailDTO.getContactNum());

			if (messageExist) {

				String sql = "UPDATE public.nt_t_pending_alerts " + "SET sms_message=? "
						+ "WHERE receipient_mobileno=? and status='P'";

				ps = con.prepareStatement(sql);
				ps.setString(1, sendMailDTO.getNotifySentenceOne());
				ps.setString(2, sendMailDTO.getContactNum());

				int savedCount = ps.executeUpdate();
				if (savedCount > 0) {
					saved = true;// updated
				} else {
					saved = false;// not updated
				}

			} else {
				long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_pending_alerts");

				String q = "INSERT INTO public.nt_t_pending_alerts "
						+ "(seq, receipient_mobileno, sms_message, submited_date, status, message_subject, alert_type) "
						+ "VALUES(?, ?, ?, ?, ?, ?, ?)";

				ps = con.prepareStatement(q);

				ps.setLong(1, seqNo);
				ps.setString(2, sendMailDTO.getContactNum());
				ps.setString(3, sendMailDTO.getNotifySentenceOne());
				ps.setTimestamp(4, timestamp);
				ps.setString(5, "P");
				ps.setString(6, "Inquiry Investigation");
				ps.setString(7, "sms");

				int savedCount = ps.executeUpdate();
				if (savedCount > 0) {
					saved = true;// saved
				} else {
					saved = false;// not saved
				}

			}
			ConnectionManager.close(ps);

			// check exist in nt_r_investigation_alerts table
			boolean dataExist = checkDataAvailableInNt_r_investigation_alerts(sendMailDTO.getComplainNo(), false);
			if (dataExist) {
				// update investigation alers table
				String query = "UPDATE public.nt_r_investigation_alerts "
						+ "SET notify_sms=?, recepient_name=?, receipient_contactnum=? " + "WHERE complain_number=? ";

				ps = con.prepareStatement(query);
				ps.setString(1, sendMailDTO.getNotifySentenceOne());
				ps.setString(2, sendMailDTO.getContactName());
				ps.setString(3, sendMailDTO.getContactNum());
				ps.setString(4, sendMailDTO.getComplainNo());

				ps.executeUpdate();

			} else {
				// insert into investigation alerts table
				long seqNum = Utils.getNextValBySeqName(con, "seq_nt_r_investigation_alerts");

				String query = "INSERT INTO public.nt_r_investigation_alerts (seq, receipient_contactnum, notify_sms, "
						+ "create_date, create_by, alert_type, complain_number, recepient_name) "
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

				ps = con.prepareStatement(query);

				ps.setLong(1, seqNum);
				ps.setString(2, sendMailDTO.getContactNum());
				ps.setString(3, sendMailDTO.getNotifySentenceOne());
				ps.setTimestamp(4, timestamp);
				ps.setString(5, loginUser);
				ps.setString(6, "sms");
				ps.setString(7, sendMailDTO.getComplainNo());
				ps.setString(8, sendMailDTO.getContactName());

				ps.executeUpdate();
			}

			con.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return saved;
	}

	@Override
	public boolean checkDataAvailableInNt_r_investigation_alerts(String complainNo, boolean letter) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean exist = false;
		String where = "";
		try {
			con = ConnectionManager.getConnection();

			if (letter) {
				where = " and alert_type in('letter')";
			} else {
				where = " and alert_type in('sms','e-mail')";
			}

			String sql = "select * from public.nt_r_investigation_alerts where complain_number='" + complainNo + "' "
					+ where;// TODO check status of SMS

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				exist = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return exist;
	}

	@Override
	public ComplaintRequestDTO getComplaintDetails(String complaintNo, String vehicleNo, String permitNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		ComplaintRequestDTO dto = new ComplaintRequestDTO();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM nt_m_complain_request WHERE complain_no = ? and vehicle_no = ? and permit_no = ? order by seq";
			ps = con.prepareStatement(query);
			ps.setString(1, complaintNo);
			ps.setString(2, vehicleNo);
			ps.setString(3, permitNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				dto = new ComplaintRequestDTO();
				dto.setComplainSeq(rs.getLong("seq"));
				dto.setPermitAuthority(rs.getString("permit_authority"));
				dto.setPermitNo(rs.getString("permit_no"));
				dto.setVehicleNo(rs.getString("vehicle_no"));
				dto.setComplainTypeCode(rs.getString("type_of_contact"));
				dto.setComplainMedia(rs.getString("complaint_media"));
				dto.setComplaintNo(rs.getString("complain_no"));
				dto.setPriorityOrder(rs.getString("priority_order"));
				dto.setSeverityNo(rs.getString("severity_no"));
				dto.setRouteNo(rs.getString("route_no"));
				dto.setOrigin(rs.getString("origin"));
				dto.setDestination(rs.getString("destination"));
				dto.setDepot(rs.getString("depot"));
				dto.setProvince(rs.getString("province"));
				dto.setEventPlace(rs.getString("event_place"));
				dto.setEventDateTime(rs.getString("event_datetime"));
				dto.setComplainerName(rs.getString("complainer_name"));
				dto.setComplainerName_si(rs.getString("complainer_name_si"));
				dto.setComplainerName_ta(rs.getString("complainer_name_ta"));
				dto.setAddress1(rs.getString("address1"));
				dto.setAddress1_si(rs.getString("address1_si"));
				dto.setAddress1_ta(rs.getString("address1_ta"));
				dto.setAddress2(rs.getString("address2"));
				dto.setAddress2_si(rs.getString("address2_si"));
				dto.setAddress2_ta(rs.getString("address2_ta"));
				dto.setCity(rs.getString("city"));
				dto.setCity_si(rs.getString("city_si"));
				dto.setCity_ta(rs.getString("city_ta"));
				dto.setContact1(rs.getString("contact_no1"));
				dto.setContact2(rs.getString("contact_no2"));
				String participation = rs.getString("is_participation_on_inquiry");
				if (participation != null && participation.equalsIgnoreCase("Y"))
					dto.setComplainerParticipation(true);

				String proof = rs.getString("is_participation_on_inquiry");
				if (proof != null && proof.equalsIgnoreCase("Y"))
					dto.setHasWrittenProof(true);

				dto.setOtherOffence(rs.getString("other_commited_offences"));
				dto.setDetailOfComplain(rs.getString("details_of_complain"));
				dto.setCommittedOffences(getCommittedOffencesByComplaint(dto.getComplainSeq()));
				dto.setCreatedDate(rs.getTimestamp("created_date"));
				dto.setProcessStatus(rs.getString("process_status"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return dto;
	}

	@Override
	public ComplaintRequestDTO getComplaintDetailsByComplainNo(String complainNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		ComplaintRequestDTO dto = new ComplaintRequestDTO();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM nt_m_complain_request WHERE complain_no = ? order by seq";
			ps = con.prepareStatement(query);
			ps.setString(1, complainNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				dto = new ComplaintRequestDTO();
				dto.setComplainSeq(rs.getLong("seq"));
				dto.setPermitAuthority(rs.getString("permit_authority"));
				dto.setPermitNo(rs.getString("permit_no"));
				dto.setVehicleNo(rs.getString("vehicle_no"));
				dto.setComplainTypeCode(rs.getString("type_of_contact"));
				dto.setComplainMedia(rs.getString("complaint_media"));
				dto.setComplaintNo(rs.getString("complain_no"));
				dto.setPriorityOrder(rs.getString("priority_order"));
				dto.setSeverityNo(rs.getString("severity_no"));
				dto.setRouteNo(rs.getString("route_no"));
				dto.setOrigin(rs.getString("origin"));
				dto.setDestination(rs.getString("destination"));
				dto.setDepot(rs.getString("depot"));
				dto.setProvince(rs.getString("province"));
				dto.setEventPlace(rs.getString("event_place"));
				dto.setEventDateTime(rs.getString("event_datetime"));
				dto.setComplainerName(rs.getString("complainer_name"));
				dto.setComplainerName_si(rs.getString("complainer_name_si"));
				dto.setComplainerName_ta(rs.getString("complainer_name_ta"));
				dto.setAddress1(rs.getString("address1"));
				dto.setAddress1_si(rs.getString("address1_si"));
				dto.setAddress1_ta(rs.getString("address1_ta"));
				dto.setAddress2(rs.getString("address2"));
				dto.setAddress2_si(rs.getString("address2_si"));
				dto.setAddress2_ta(rs.getString("address2_ta"));
				dto.setCity(rs.getString("city"));
				dto.setCity_si(rs.getString("city_si"));
				dto.setCity_ta(rs.getString("city_ta"));
				dto.setContact1(rs.getString("contact_no1"));
				dto.setContact2(rs.getString("contact_no2"));
				String participation = rs.getString("is_participation_on_inquiry");
				if (participation != null && participation.equalsIgnoreCase("Y"))
					dto.setComplainerParticipation(true);

				String proof = rs.getString("is_participation_on_inquiry");
				if (proof != null && proof.equalsIgnoreCase("Y"))
					dto.setHasWrittenProof(true);

				dto.setOtherOffence(rs.getString("other_commited_offences"));
				dto.setDetailOfComplain(rs.getString("details_of_complain"));
				dto.setCommittedOffences(getCommittedOffencesByComplaint(dto.getComplainSeq()));
				dto.setCreatedDate(rs.getTimestamp("created_date"));
				dto.setProcessStatus(rs.getString("process_status"));
				dto.setServiceTypeDescription(rs.getString("service_type"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return dto;
	}

	public ComplaintRequestDTO getComplaintDetailsForEmails(String complaintNo, Connection con) {
		PreparedStatement ps = null;
		ResultSet rs = null;

		ComplaintRequestDTO dto = new ComplaintRequestDTO();
		try {

			String query = "SELECT *,a.description as province_des FROM nt_m_complain_request left join nt_r_province a on a.code=nt_m_complain_request.province WHERE complain_no = ? order by seq";
			ps = con.prepareStatement(query);
			ps.setString(1, complaintNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				dto = new ComplaintRequestDTO();
				dto.setComplainSeq(rs.getLong("seq"));
				dto.setPermitAuthority(rs.getString("permit_authority"));
				dto.setPermitNo(rs.getString("permit_no"));
				dto.setVehicleNo(rs.getString("vehicle_no"));
				dto.setComplainTypeCode(rs.getString("type_of_contact"));
				dto.setComplainMedia(rs.getString("complaint_media"));
				dto.setComplaintNo(rs.getString("complain_no"));
				dto.setPriorityOrder(rs.getString("priority_order"));
				dto.setSeverityNo(rs.getString("severity_no"));
				dto.setRouteNo(rs.getString("route_no"));
				dto.setOrigin(rs.getString("origin"));
				dto.setDestination(rs.getString("destination"));
				dto.setDepot(rs.getString("depot"));
				dto.setProvince(rs.getString("province"));
				dto.setProvince_des(rs.getString("province_des"));
				dto.setEventPlace(rs.getString("event_place"));
				dto.setEventDateTime(rs.getString("event_datetime"));
				dto.setComplainerName(rs.getString("complainer_name"));
				dto.setComplainerName_si(rs.getString("complainer_name_si"));
				dto.setComplainerName_ta(rs.getString("complainer_name_ta"));
				dto.setAddress1(rs.getString("address1"));
				dto.setAddress1_si(rs.getString("address1_si"));
				dto.setAddress1_ta(rs.getString("address1_ta"));
				dto.setAddress2(rs.getString("address2"));
				dto.setAddress2_si(rs.getString("address2_si"));
				dto.setAddress2_ta(rs.getString("address2_ta"));
				dto.setCity(rs.getString("city"));
				dto.setCity_si(rs.getString("city_si"));
				dto.setCity_ta(rs.getString("city_ta"));
				dto.setContact1(rs.getString("contact_no1"));
				dto.setContact2(rs.getString("contact_no2"));
				String participation = rs.getString("is_participation_on_inquiry");
				if (participation != null && participation.equalsIgnoreCase("Y"))
					dto.setComplainerParticipation(true);

				String proof = rs.getString("is_participation_on_inquiry");
				if (proof != null && proof.equalsIgnoreCase("Y"))
					dto.setHasWrittenProof(true);

				dto.setOtherOffence(rs.getString("other_commited_offences"));
				dto.setDetailOfComplain(rs.getString("details_of_complain"));
				dto.setCommittedOffences(getCommittedOffencesByComplaintForEmail(dto.getComplainSeq(),con));
				dto.setCreatedDate(rs.getTimestamp("created_date"));
				dto.setProcessStatus(rs.getString("process_status"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
		}
		return dto;
	}

	public List<CommittedOffencesDTO> getCommittedOffencesByComplaint(Long complainSeq) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CommittedOffencesDTO> commitedDTOList = new ArrayList<CommittedOffencesDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM nt_t_grievance_commited_offence a, nt_m_offence_management b"
					+ " WHERE a.offence_type_code = b.ofm_offence_code AND a.complain_seq = ?;";

			ps = con.prepareStatement(query);
			ps.setLong(1, complainSeq);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommittedOffencesDTO dto = new CommittedOffencesDTO();
				dto.setSeq(rs.getLong("seq"));
				dto.setCode(rs.getString("ofm_offence_code"));
				dto.setDescription(rs.getString("ofm_offence_desc"));
				dto.setRemark(rs.getString("remarks"));
				dto.setApplicable(
						rs.getString("is_applicable") == null || rs.getString("is_applicable").equals("N") ? false
								: true);
				commitedDTOList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return commitedDTOList;
	}
	
	public List<CommittedOffencesDTO> getCommittedOffencesByComplaintForEmail(Long complainSeq, Connection con) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CommittedOffencesDTO> commitedDTOList = new ArrayList<CommittedOffencesDTO>();

		try {

			String query = "SELECT * FROM nt_t_grievance_commited_offence a, nt_m_offence_management b"
					+ " WHERE a.offence_type_code = b.ofm_offence_code AND a.complain_seq = ? AND is_applicable='Y';";

			ps = con.prepareStatement(query);
			ps.setLong(1, complainSeq);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommittedOffencesDTO dto = new CommittedOffencesDTO();
				dto.setSeq(rs.getLong("seq"));
				dto.setCode(rs.getString("ofm_offence_code"));
				dto.setDescription(rs.getString("ofm_offence_desc"));
				dto.setRemark(rs.getString("remarks"));
				commitedDTOList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
		}
		return commitedDTOList;
	}

	@Override
	public void sendMail(ManageInquiryDTO sendMailDTO, String loginUser) {
		Connection con = null;
		PreparedStatement stmt = null;
		String message = null;

		try {

			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_pending_emails");

			String sql = "INSERT INTO nt_t_pending_emails(seq,alert_type,mail_message,receipient_email,status,message_subject) VALUES (?,?,?,?,?,?)";

			stmt = con.prepareStatement(sql);

			message = "<p>Dear Customer,</p>" + "<p>" + sendMailDTO.getNotifySentenceTwo() + "</p><p>"
					+ sendMailDTO.getNotifySentenceThree() + "</p><p>" + sendMailDTO.getNotifySentencFour()
					+ "</p><p>Regards,</p><p> NTC asasasas Administrator</p>";

			stmt.setLong(1, seqNo);
			stmt.setString(2, "email");
			stmt.setString(3, message);
			stmt.setString(4, sendMailDTO.getEmail());
			stmt.setString(5, "P");
			stmt.setString(6, "Complaint Investigation");

			stmt.executeUpdate();

			ConnectionManager.close(stmt);

			// save data in investigation alerts table
			java.util.Date date = new java.util.Date();
			Timestamp timestampDt = new Timestamp(date.getTime());

			boolean exist = checkDataAvailableInNt_r_investigation_alerts(sendMailDTO.getComplainNo(), false);

			if (!exist) {
				long seqNum = Utils.getNextValBySeqName(con, "seq_nt_r_investigation_alerts");

				String query = "INSERT INTO public.nt_r_investigation_alerts (seq, receipient_email, notify_mail_subject, notify_mail_body_one, notify_mail_body_two, "
						+ "notify_mail_body_three, create_date, create_by, alert_type, complain_number) "
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				stmt = con.prepareStatement(query);

				stmt.setLong(1, seqNum);
				stmt.setString(2, sendMailDTO.getEmail());
				stmt.setString(3, "Complaint Investigation");
				stmt.setString(4, sendMailDTO.getNotifySentenceTwo());
				stmt.setString(5, sendMailDTO.getNotifySentenceThree());
				stmt.setString(6, sendMailDTO.getNotifySentencFour());
				stmt.setTimestamp(7, timestampDt);
				stmt.setString(8, loginUser);
				stmt.setString(9, "e-mail");
				stmt.setString(10, sendMailDTO.getComplainNo());

				stmt.executeUpdate();
			} else {
				// update investigation alerts table
				String query = "UPDATE public.nt_r_investigation_alerts "
						+ "SET receipient_email=?, notify_mail_body_one=?, notify_mail_body_two=?, notify_mail_body_three=?, receipient_contactnum=? "
						+ "WHERE complain_number=? and  alert_type in('sms','e-mail')";

				stmt = con.prepareStatement(query);
				stmt.setString(1, sendMailDTO.getEmail());
				stmt.setString(2, sendMailDTO.getNotifySentenceTwo());
				stmt.setString(3, sendMailDTO.getNotifySentenceThree());
				stmt.setString(4, sendMailDTO.getNotifySentencFour());
				stmt.setString(5, sendMailDTO.getContactNum());
				stmt.setString(6, sendMailDTO.getComplainNo());

				stmt.executeUpdate();
			}

			con.commit();

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

	}

	@Override
	public ManageInquiryDTO retrieveSMSDetails(String complainNo) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		ManageInquiryDTO manageInquiryDTO = new ManageInquiryDTO();

		try {
			con = ConnectionManager.getConnection();

			String sql = "select * from public.nt_r_investigation_alerts where complain_number='" + complainNo
					+ "' and alert_type in('sms','e-mail') ";// TODO check status of SMS

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				manageInquiryDTO.setComplainNo(complainNo);
				manageInquiryDTO.setContactName(rs.getString("recepient_name"));
				manageInquiryDTO.setContactNum(rs.getString("receipient_contactnum"));
				manageInquiryDTO.setNotifySentenceOne(rs.getString("notify_sms"));

				if (rs.getString("receipient_email") != null && !rs.getString("receipient_email").isEmpty()
						&& !rs.getString("receipient_email").trim().equalsIgnoreCase("")) {
					manageInquiryDTO.setEmail(rs.getString("receipient_email"));
				}
				if (rs.getString("notify_mail_body_one") != null && !rs.getString("notify_mail_body_one").isEmpty()
						&& !rs.getString("notify_mail_body_one").trim().equalsIgnoreCase("")) {
					manageInquiryDTO.setNotifySentenceTwo(rs.getString("notify_mail_body_one"));
				} else {
					ParamerDTO sentenceDTO = retrieveParameterValuesForParamName(con,
							"GRIEVANCE_MNG_INQ_LETTER_SENTENCE_ONE");
					manageInquiryDTO.setNotifySentenceTwo(sentenceDTO.getStringValue());
				}
				if (rs.getString("notify_mail_body_two") != null && !rs.getString("notify_mail_body_two").isEmpty()
						&& !rs.getString("notify_mail_body_two").trim().equalsIgnoreCase("")) {
					manageInquiryDTO.setNotifySentenceThree(rs.getString("notify_mail_body_two"));
				} else {
					ParamerDTO sentenceDTO = retrieveParameterValuesForParamName(con,
							"GRIEVANCE_MNG_INQ_LETTER_SENTENCE_TWO");
					manageInquiryDTO.setNotifySentenceThree(sentenceDTO.getStringValue());
				}
				if (rs.getString("notify_mail_body_three") != null && !rs.getString("notify_mail_body_three").isEmpty()
						&& !rs.getString("notify_mail_body_three").trim().equalsIgnoreCase("")) {
					manageInquiryDTO.setNotifySentencFour(rs.getString("notify_mail_body_three"));
				} else {
					ParamerDTO sentenceDTO = retrieveParameterValuesForParamName(con,
							"GRIEVANCE_MNG_INQ_LETTER_SENTENCE_THREE");
					manageInquiryDTO.setNotifySentencFour(sentenceDTO.getStringValue());
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return manageInquiryDTO;
	}

	@Override
	public ManageInquiryDTO retrieveLetterDetails(String complainNo) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		ManageInquiryDTO manageInquiryDTO = new ManageInquiryDTO();

		try {
			con = ConnectionManager.getConnection();

			String sql = "select * from public.nt_r_investigation_alerts where complain_number='" + complainNo
					+ "' and alert_type in('letter') ";// TODO check status of SMS

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				manageInquiryDTO.setComplainNo(complainNo);
				manageInquiryDTO.setContactName(rs.getString("recepient_name"));
				manageInquiryDTO.setContactAddress(rs.getString("receipient_address"));

				if (rs.getString("notify_mail_body_one") != null && !rs.getString("notify_mail_body_one").isEmpty()
						&& !rs.getString("notify_mail_body_one").trim().equalsIgnoreCase("")) {
					manageInquiryDTO.setNotifySentenceOne(rs.getString("notify_mail_body_one"));
				}
				if (rs.getString("notify_mail_body_two") != null && !rs.getString("notify_mail_body_two").isEmpty()
						&& !rs.getString("notify_mail_body_two").trim().equalsIgnoreCase("")) {
					manageInquiryDTO.setNotifySentenceTwo(rs.getString("notify_mail_body_two"));
				}
				if (rs.getString("notify_mail_body_three") != null && !rs.getString("notify_mail_body_three").isEmpty()
						&& !rs.getString("notify_mail_body_three").trim().equalsIgnoreCase("")) {
					manageInquiryDTO.setNotifySentenceThree(rs.getString("notify_mail_body_three"));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return manageInquiryDTO;
	}

	public ParamerDTO retrieveParameterValuesForParamName(Connection con, String paramName) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		ParamerDTO parameterDto = null;

		try {
			String sql1 = "SELECT string_value,number_value,date_value,type FROM public.nt_r_parameters WHERE param_name=?";
			ps = con.prepareStatement(sql1);

			ps.setString(1, paramName);
			rs = ps.executeQuery();

			while (rs.next()) {
				parameterDto = new ParamerDTO();
				parameterDto.setStringValue(rs.getString("string_value"));
				parameterDto.setIntValue(rs.getInt("number_value"));
				parameterDto.setDateValue(rs.getString("date_value"));
				parameterDto.setType(rs.getString("type"));

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
		}

		return parameterDto;
	}

	@Override
	public List<ComplaintRequestDTO> getComplaintDetailsForPublicComplain(String permitNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;
		List<ComplaintRequestDTO> dtoList = new ArrayList<>();
		try {
			con = ConnectionManager.getConnection();

			query = "SELECT *,a.description FROM nt_m_complain_request \r\n"
					+ "inner join public.nt_r_priority_order a on a.code=nt_m_complain_request.priority_order\r\n"
					+ "WHERE  permit_no =?  and a.active='A' order by seq";
			ps = con.prepareStatement(query);

			ps.setString(1, permitNo);

			rs = ps.executeQuery();
			ComplaintRequestDTO dto = new ComplaintRequestDTO();
			while (rs.next()) {
				dto = new ComplaintRequestDTO();
				dto.setComplainSeq(rs.getLong("seq"));
				dto.setPermitAuthority(rs.getString("permit_authority"));
				dto.setPermitNo(rs.getString("permit_no"));
				dto.setVehicleNo(rs.getString("vehicle_no"));
				dto.setComplainTypeCode(rs.getString("type_of_contact"));
				dto.setComplainMedia(rs.getString("complaint_media"));
				dto.setComplaintNo(rs.getString("complain_no"));
				dto.setPriorityOrder(rs.getString("description"));
				dto.setSeverityNo(rs.getString("severity_no"));
				dto.setRouteNo(rs.getString("route_no"));
				dto.setOrigin(rs.getString("origin"));
				dto.setDestination(rs.getString("destination"));
				dto.setDepot(rs.getString("depot"));
				dto.setProvince(rs.getString("province"));
				dto.setEventPlace(rs.getString("event_place"));
				dto.setEventDateTime(rs.getString("event_datetime"));
				dto.setComplainerName(rs.getString("complainer_name"));
				dto.setComplainerName_si(rs.getString("complainer_name_si"));
				dto.setComplainerName_ta(rs.getString("complainer_name_ta"));
				dto.setAddress1(rs.getString("address1"));
				dto.setAddress1_si(rs.getString("address1_si"));
				dto.setAddress1_ta(rs.getString("address1_ta"));
				dto.setAddress2(rs.getString("address2"));
				dto.setAddress2_si(rs.getString("address2_si"));
				dto.setAddress2_ta(rs.getString("address2_ta"));
				dto.setCity(rs.getString("city"));
				dto.setCity_si(rs.getString("city_si"));
				dto.setCity_ta(rs.getString("city_ta"));
				dto.setContact1(rs.getString("contact_no1"));
				dto.setContact2(rs.getString("contact_no2"));
				String participation = rs.getString("is_participation_on_inquiry");
				if (participation != null && participation.equalsIgnoreCase("Y"))
					dto.setComplainerParticipation(true);

				String proof = rs.getString("is_participation_on_inquiry");
				if (proof != null && proof.equalsIgnoreCase("Y"))
					dto.setHasWrittenProof(true);

				dto.setOtherOffence(rs.getString("other_commited_offences"));
				dto.setDetailOfComplain(rs.getString("details_of_complain"));
				dto.setCommittedOffences(getCommittedOffencesByComplaint(dto.getComplainSeq()));
				dto.setCreatedDate(rs.getTimestamp("created_date"));
				if (rs.getString("process_status") != null) {
					if (rs.getString("process_status").equalsIgnoreCase("C")) {
						dto.setProcessStatus("Closed");
					} else if (rs.getString("process_status").equalsIgnoreCase("P")) {
						dto.setProcessStatus("Pending");
					} else if (rs.getString("process_status").equalsIgnoreCase("O")) {
						dto.setProcessStatus("Ongoing");
					}
				} else {

					dto.setProcessStatus("-");
				}
				dtoList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return dtoList;
	}

	@Override
	public List<FlyingManageInvestigationLogDTO> getInvestigationDetails(String permitNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;
		List<FlyingManageInvestigationLogDTO> dtoList = new ArrayList<>();
		try {
			con = ConnectionManager.getConnection();

			// Modified By Dinushi.R on 18-07-2020

			query = "SELECT inv_investigation_no, inv_det_ref_no,inv_det_report_no,inv_permit_owner, to_char(inv_det_time, 'dd/MM/yyyy') as inv_det_time"
					+ " FROM public.nt_t_flying_investigation_log_det inner join nt_t_investigation_charge_master "
					+ " on nt_t_investigation_charge_master.charge_ref_no = nt_t_flying_investigation_log_det.inv_investigation_no "
					+ " WHERE inv_permit_no =? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, permitNo);

			rs = ps.executeQuery();
			FlyingManageInvestigationLogDTO dto = new FlyingManageInvestigationLogDTO();
			while (rs.next()) {
				dto = new FlyingManageInvestigationLogDTO();
				dto.setInvesNo(rs.getString("inv_investigation_no"));
				dto.setRefNo(rs.getString("inv_det_ref_no"));
				dto.setReportNo(rs.getString("inv_det_report_no"));
				dto.setPermitowner(rs.getString("inv_permit_owner"));
				dto.setDateOfInvestigation(rs.getString("inv_det_time"));
				
//				String currStatus = getApplicationCurrentStatus(dto.getInvesNo()) == null? "P": getApplicationCurrentStatus(dto.getInvesNo());

				dto.setStatus(getApplicationCurrentStatus(dto.getInvesNo()) == null? "P": getApplicationCurrentStatus(dto.getInvesNo()));
				dto.setStatusDesc(getStatusDesc(dto.getStatus()));

				dtoList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return dtoList;
	}
	
	@Override
	public String getStatusDesc(String status) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String statusDesc = null;

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT inv_status_desc FROM public.nt_r_investigation_status "
					+ "WHERE inv_status_code = ? ";

			ps = con.prepareStatement(query);
			ps.setString(1, status);

			rs = ps.executeQuery();

			while (rs.next()) {
				statusDesc = rs.getString("inv_status_desc");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return statusDesc;
	}
	
	@Override
	public String getApplicationCurrentStatus(String chargeRef) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String status = null;

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT charge_app_current_status FROM public.nt_t_investigation_charge_master "
					+ "WHERE charge_ref_no = ? ";

			ps = con.prepareStatement(query);
			ps.setString(1, chargeRef);

			rs = ps.executeQuery();

			while (rs.next()) {
				status = rs.getString("charge_app_current_status");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return status;
	}

	@Override
	public SimRegistrationDTO getGPSDetails(String permitNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;
		SimRegistrationDTO dto = new SimRegistrationDTO();
		try {
			con = ConnectionManager.getConnection();

			query = "SELECT sim_reg_no,sim_no,sim_receiver_name,sim_issue_date,"
					+ " sim_valid_until,case when sim_status = 'P' then 'Pending' when sim_status = 'A' then 'Active' when sim_status = 'I' then 'Inactive' else 'N/A' end as sim_status,"
					+ " sim_remarks,sim_receipt_no " + " FROM public.nt_m_sim_registration "
					+ " WHERE sim_permit_no =? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, permitNo);

			rs = ps.executeQuery();
			while (rs.next()) {
				dto = new SimRegistrationDTO();
				dto.setSimRegNo(rs.getString("sim_reg_no"));
				dto.setSimNo(rs.getString("sim_no"));
				dto.setReceiversName(rs.getString("sim_receiver_name"));
				dto.setIssueDate(rs.getDate("sim_issue_date"));
				dto.setValidUntilDate(rs.getDate("sim_valid_until"));
				dto.setEmiStatus(rs.getString("sim_status"));
				dto.setRemarks(rs.getString("sim_remarks"));
				dto.setReceiptNo(rs.getString("sim_receipt_no"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return dto;
	}

	@Override
	public List<SimRegistrationDTO> getEmiDetails(String permitNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;
		List<SimRegistrationDTO> dtoList = new ArrayList<>();
		try {
			con = ConnectionManager.getConnection();

			query = "select emi_bus_no,"
					+ " case when emi_status = 'P' then 'Pending' when emi_status = 'A' then 'Active' when emi_status = 'I' then 'Inactive' else 'N/A' end as emi_status ,"
					+ " emi_re_issue_date from nt_t_emi_det where emi_sim_reg_no=? " + " order by emi_status; ";

			ps = con.prepareStatement(query);
			ps.setString(1, permitNo);

			rs = ps.executeQuery();
			SimRegistrationDTO dto = new SimRegistrationDTO();
			while (rs.next()) {
				dto = new SimRegistrationDTO();
				dto.setEmiBusNo(rs.getString("emi_bus_no"));
				dto.setEmiStatus(rs.getString("emi_status"));
				dto.setEmiReIssueDate(rs.getDate("emi_re_issue_date"));

				dtoList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return dtoList;
	}

	@Override
	public boolean updateComplaintStatus(ManageInquiryDTO complaint, String status, String user) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null, stmt1 = null;
		PreparedStatement ps3 = null;
		boolean updated = false;

		try {
			con = ConnectionManager.getConnection();
			java.util.Date date = new java.util.Date();
			Timestamp timestamp = new Timestamp(date.getTime());
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			String currentDate = dateFormat.format(timestamp);

			String sql1 = "INSERT into public.nt_h_complain_request  "
					+ " (SELECT * FROM public.nt_m_complain_request WHERE complain_no = ?) ";

			stmt1 = con.prepareStatement(sql1);
			stmt1.setString(1, complaint.getComplainNo());
			stmt1.executeUpdate();

			String sql = "UPDATE nt_m_complain_request SET process_status =? , modified_by = ?, modified_date = ?, closed_date=?, closed_user=?, special_remark=?  "
					+ " WHERE complain_no = ?";

			ps = con.prepareStatement(sql);
			ps.setString(1, status);
			ps.setString(2, user);
			ps.setTimestamp(3, timestamp);
			ps.setString(4, currentDate);
			ps.setString(5, user);
			ps.setString(6, complaint.getSpecialRemark());
			ps.setString(7, complaint.getComplainNo());
			ps.executeUpdate();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_grievancetask_det");

			// insert to nt_t_grievancetask_det table
			String query3 = "INSERT INTO public.nt_t_grievancetask_det "
					+ "(tgt_seq, tgt_complain_no, tgt_vehicle_no, tgt_task_code, tgt_status, tgt_created_by, tgt_created_date) "
					+ "VALUES(?,?,?,?,?,?,?); ";

			ps3 = con.prepareStatement(query3);
			ps3.setLong(1, seqNo);
			ps3.setString(2, complaint.getComplainNo());
			ps3.setString(3, complaint.getVehicleNum());
			ps3.setString(4, "GM106");
			ps3.setString(5, "C");
			ps3.setString(6, user);
			ps3.setTimestamp(7, timestamp);
			ps3.executeUpdate();

			ConnectionManager.commit(con);
			;
			updated = true;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(stmt1);
			ConnectionManager.close(ps3);
			ConnectionManager.close(con);
		}
		return updated;
	}

	@Override

	public boolean chekDataFromReport(String complainNo) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean dataHave = false;
		try {
			con = ConnectionManager.getConnection();

			String sql = "select distinct  permit_no,vehicle_no,details_of_complain,(TO_CHAR(event_datetime,'dd-MM-yyyy')) as event_datetime,origin,destination,\r\n"
					+ "a.pm_service_type,b.description_si,c.pmo_full_name_sinhala,c.pmo_address1_sinhala,c.pmo_address2_sinhala,c.pmo_address3_sinhala\r\n"
					+ "from public.nt_m_complain_request\r\n"
					+ "inner join  public.nt_t_pm_application a on a.pm_permit_no=public.nt_m_complain_request.permit_no\r\n"
					+ "inner join public.nt_r_service_types b on b.code=a.pm_service_type\r\n"
					+ "inner join public.nt_t_pm_vehi_owner c on c.pmo_permit_no=public.nt_m_complain_request.permit_no\r\n"
					+ "where b.active='A'\r\n" + "and a.pm_status in ('A','O')\r\n" + "and complain_no=?";

			ps = con.prepareStatement(sql);
			ps.setString(1, complainNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				dataHave = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return dataHave;
	}

	@Override
	public List<String> retrieveVehicleNumbers() {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		List<String> vehicleNumList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT DISTINCT vehicle_no FROM public.nt_m_complain_request where vehicle_no is not null and vehicle_no!='' order by vehicle_no";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				vehicleNumList.add(rs.getString("vehicle_no"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return vehicleNumList;
	}

	@Override
	public String getOffenceInSinhala(String complainNo) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String offence = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "select a.seq,string_agg(c.ofm_offence_desc_sinhala,', ') as ofm_offence_desc_sinhala \r\n"
					+ "from public.nt_m_complain_request a\r\n"
					+ "inner join public.nt_t_grievance_commited_offence b on a.seq=b.complain_seq\r\n"
					+ "inner join public.nt_m_offence_management c on c.ofm_offence_code=b.offence_type_code\r\n"
					+ "where a.complain_no=? and b.is_applicable='Y'\r\n" + "group by a.seq ";

			ps = con.prepareStatement(sql);
			ps.setString(1, complainNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				offence = rs.getString("ofm_offence_desc_sinhala");

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return offence;
	}

	@Override
	public boolean checkGrievanceTask(String complainNo, String taskCode, String taskStatus) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean found = false;

		try {
			con = ConnectionManager.getConnection();

			String sql = "select * from public.nt_t_grievancetask_det where tgt_complain_no=? and tgt_task_code=? and tgt_status=?; ";

			ps = con.prepareStatement(sql);
			ps.setString(1, complainNo);
			ps.setString(2, taskCode);
			ps.setString(3, taskStatus);

			rs = ps.executeQuery();

			while (rs.next()) {
				found = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return found;
	}

	@Override
	public boolean sendPtaCtbMail(List<ManageInquiryDTO> sendMailDTOList, String loginUser,ManageInquiryDTO selectedData) {
		Connection con = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		PreparedStatement ps4 = null;
		ResultSet rs = null;
		String content = null;
		String emailAddress = null;
		String emailSubject = null;
		
		java.util.Date date1 = new java.util.Date();
		Timestamp timestamp1 = new Timestamp(date1.getTime());

		try {

			con = ConnectionManager.getConnection();
			if (sendMailDTOList.get(0).getAuthority() != null && sendMailDTOList.get(0).getAuthority().equals("PTA")) {
				// find the email address of province

				String query4 = "SELECT tgt_province_email FROM public.nt_r_province_det WHERE tgt_province_code=?;";

				ps4 = con.prepareStatement(query4);
				ps4.setString(1, sendMailDTOList.get(0).getProvince());
				rs = ps4.executeQuery();

				while (rs.next()) {
					emailAddress = rs.getString("tgt_province_email");
				}

				emailSubject = "Complaint related PTA";
			} else if (sendMailDTOList.get(0).getAuthority() != null
					&& sendMailDTOList.get(0).getAuthority().equals("CTB")) {
				emailAddress = PropertyReader.getPropertyValue("grievance.management.email.address.ctb");
				emailSubject = "Complaint related CTB";
			}

			for (ManageInquiryDTO complaint : sendMailDTOList) {

				// send email only for non - closed complaints
				if (!complaint.getProcess_status().equals("C")) {

					// save email data to nt_t_pending_emails table
					long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_pending_emails");

					String query1 = "INSERT INTO nt_t_pending_emails(seq,alert_type,mail_message,receipient_email,status,message_subject) VALUES (?,?,?,?,?,?)";

					ps1 = con.prepareStatement(query1);

					content = formatPtaCtbEmailContent(complaint, con);

					ps1.setLong(1, seqNo);
					ps1.setString(2, "email");
					ps1.setString(3, content);
					ps1.setString(4, emailAddress);
					ps1.setString(5, "P");
					ps1.setString(6, emailSubject);

					ps1.executeUpdate();

					java.util.Date date = new java.util.Date();
					Timestamp timestamp = new Timestamp(date.getTime());
					DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
					String currentDate = dateFormat.format(timestamp);

					// update NT_M_COMPLAIN_REQUEST table
					String query2 = "update public.nt_m_complain_request set process_status = 'C', closed_date=? where complain_no=?";

					ps2 = con.prepareStatement(query2);
					ps2.setString(1, currentDate);
					ps2.setString(2, complaint.getComplainNo());

					ps2.executeUpdate();

					// insert to nt_t_grievancetask_det table
					long seqNoTask = Utils.getNextValBySeqName(con, "seq_nt_t_grievancetask_det");

					String query3 = "INSERT INTO public.nt_t_grievancetask_det "
							+ "(tgt_seq, tgt_complain_no, tgt_vehicle_no, tgt_task_code, tgt_status, tgt_created_by, tgt_created_date) "
							+ "	VALUES(?,?,?,?,?,?,?); ";

					ps3 = con.prepareStatement(query3);
					ps3.setLong(1, seqNoTask);
					ps3.setString(2, complaint.getComplainNo());
					ps3.setString(3, complaint.getVehicleNum());
					ps3.setString(4, "GM101");
					ps3.setString(5, "C");
					ps3.setString(6, loginUser);
					ps3.setTimestamp(7, timestamp);

					ps3.executeUpdate();

				}

			}

			con.commit();
		

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			ConnectionManager.close(ps1);
			ConnectionManager.close(ps2);
			ConnectionManager.close(ps3);
			ConnectionManager.close(ps4);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return true;
	}

	private String formatPtaCtbEmailContent(ManageInquiryDTO complaint, Connection con) {
		String content = "";
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String currentDate = dateFormat.format(timestamp);
		
		String emailSubectString="";
		if(complaint.getAuthority().equals("PTA")) {
			emailSubectString = "Complaint related PTA";
		} else if(complaint.getAuthority().equals("CTB")) {
			emailSubectString = "Complaint related CTB";
		}
		
		ComplaintRequestDTO complaintDetails = getComplaintDetailsForEmails(complaint.getComplainNo(), con);
		
		// format committed offenses string
		String committedOffencesString="";
		if(complaintDetails.getCommittedOffences().size()>0) {
			committedOffencesString="";
			for (CommittedOffencesDTO offence : complaintDetails.getCommittedOffences()) {
				committedOffencesString = committedOffencesString
										+ "  <tr>\r\n"
										+ "    <td>"+offence.getDescription()+"</td>\r\n"
										+ "    <td>"+offence.getRemark()+"</td>\r\n"
										+ "  </tr>\r\n";
			}
		}
		
		// format committed offenses string
		String hasWrittenProofString="";
		if(complaintDetails.isHasWrittenProof()) {
			hasWrittenProofString="YES";
		} else {
			hasWrittenProofString="NO";
		}
		// format committed offenses string
		String complainerParticipationString="";
		if(complaintDetails.isComplainerParticipation()) {
			complainerParticipationString="YES";
		} else {
			complainerParticipationString="NO";
		}
		
		// format committed offenses string
		String complainerAddress="";
		if(complaintDetails.getAddress1()!=null&&!complaintDetails.getAddress1().equals("")) {
			complainerAddress=complaintDetails.getAddress1();
		}
		if(complaintDetails.getAddress2()!=null&&!complaintDetails.getAddress2().equals("")) {
			complainerAddress=complainerAddress+", "+complaintDetails.getAddress2();
		}
		if(complaintDetails.getCity()!=null&&!complaintDetails.getCity().equals("")) {
			complainerAddress=complainerAddress+", "+complaintDetails.getCity()+".";
		}

		content = "<html>\r\n"
				+ "<p>Subject- "+emailSubectString+"</p> \r\n"
				+ "<p>Date- "+currentDate+"</p>\r\n"
				+ "<p style=\"font-size:20px;\"><b>Complaint No - "+complaint.getComplainNo()+"</b></p>\r\n"
				+ " <table style=\"width:100%\">\r\n"
				+ "  <tr>\r\n"
				+ "    <td>\r\n"
				+ "		<p style=\"font-size:18px;\"><b>Complainer Details</b></p>\r\n"
				+ "		<p>Name of Complainer - "+complaintDetails.getComplainerName()+"</p>\r\n"
				+ "		<p>Address - "+complainerAddress+"</p>\r\n"
				+ "		<p>Contact No. - "+complaintDetails.getContact1()+" "+complaintDetails.getContact2()+"</p>\r\n"
				+ "	</td>\r\n"
				+ "	<td>\r\n"
				+ "		<p style=\"font-size:18px;\"><b>Bus Details</b></p>\r\n"
				+ "		<p>Vehicle No. - "+complaintDetails.getVehicleNo()+"</p>\r\n"
				+ "		<p>Province - "+complaintDetails.getProvince_des()+"</p>\r\n"
				+ "		<p></p>\r\n"
				+ "	</td>\r\n"
				+ "  </tr>\r\n"
				+ "</table>\r\n"
				+ "\r\n"
				+ "<p style=\"font-size:18px;\"><b>Complaint Details</b></p> \r\n"
				+ "<p>Event Place - "+complaintDetails.getEventPlace()+"</p> \r\n"
				+ "<p>Event Date/Time - "+complaintDetails.getEventDateTime()+"</p> \r\n"
				+ "<p>Has Written Proof - "+hasWrittenProofString+"</p> \r\n"
				+ "<p>Complainer Participation - "+complainerParticipationString+"</p> \r\n"
				+ "<p>Details of the Complaint - "+complaintDetails.getDetailOfComplain()+"</p> \r\n"
				+ "\r\n"
				+ "<p style=\"font-size:18px;\"><b>Committed Offences</b></p> \r\n"
				+ "<table style=\"width:100%\">\r\n"
				+ "  <tr>\r\n"
				+ "    <th style=\"text-align: left;width:60%\"><b>Offence</b></th>\r\n"
				+ "    <th style=\"text-align: left;width:40%\"><b>Remark</b></th>\r\n"
				+ "  </tr>\r\n"
				+ committedOffencesString
				+ "</table>\r\n"
				+ "</html>";

		return content;
	}

	@Override
	public boolean assignToActionDepartment(ManageInquiryDTO selectedData, List<ManageInquiryDTO> complaintDTOList,
			String loginUser) {
		Connection con = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		PreparedStatement ps4 = null;
		PreparedStatement ps5 = null;
		ResultSet rs5 = null;

		try {
			con = ConnectionManager.getConnection();

			java.util.Date date = new java.util.Date();
			Timestamp timestamp = new Timestamp(date.getTime());
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			String currentDate = dateFormat.format(timestamp);

			for (ManageInquiryDTO complaint : complaintDTOList) {

				// send email only for non - closed complaints
				if (!complaint.getProcess_status().equals("C")) {

					// update NT_M_COMPLAIN_REQUEST table
					String query2 = "update public.nt_m_complain_request set department_status=?, action_department_code=?, assigned_date =?, assigned_by=?, special_remark=? "
							+ "where complain_no=?";

					ps2 = con.prepareStatement(query2);
					ps2.setString(1, "P");
					ps2.setString(2, complaint.getActionDepartment());
					ps2.setString(3, currentDate);
					ps2.setString(4, loginUser);
					ps2.setString(5, complaint.getSpecialRemark());
					ps2.setString(6, complaint.getComplainNo());
					ps2.executeUpdate();

					// save data to NT_M_COMPLAIN_ACTION table
					long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_complain_action");

					String query1 = "INSERT INTO nt_m_complain_action(seq,assigned_department_code,complaint_no,status,created_by,created_date) VALUES (?,?,?,?,?,?)";

					ps1 = con.prepareStatement(query1);
					ps1.setLong(1, seqNo);
					ps1.setString(2, complaint.getActionDepartment());
					ps1.setString(3, complaint.getComplainNo());
					ps1.setString(4, "O");
					ps1.setString(5, loginUser);
					ps1.setString(6, currentDate);
					ps1.executeUpdate();

					// get transferring department des
					String transferedToString = "";
					String query5 = "SELECT a.description as department_desc FROM nt_r_action_department a WHERE a.code =?";
					ps5 = con.prepareStatement(query5);
					ps5.setString(1, complaint.getActionDepartment());
					rs5 = ps5.executeQuery();
					while (rs5.next()) {
						transferedToString = "Transfer to " + rs5.getString("department_desc");
					}

					// save data to nt_m_action_taken table
					long seqNoTaskTaken = Utils.getNextValBySeqName(con, "seq_nt_m_action_taken");

					String query4 = "INSERT INTO public.nt_m_action_taken "
							+ "(seq, complaint_no, department_code, action_taken_by_assign_department, action_taken_date_by_assign_department) "
							+ "VALUES(?,?,?,?,?); ";

					ps4 = con.prepareStatement(query4);
					ps4.setLong(1, seqNoTaskTaken);
					ps4.setString(2, complaint.getComplainNo());
					ps4.setString(3, "GM");
					ps4.setString(4, transferedToString);
					ps4.setTimestamp(5, timestamp);
					ps4.executeUpdate();

					// insert to nt_t_grievancetask_det table
					long seqNoTask = Utils.getNextValBySeqName(con, "seq_nt_t_grievancetask_det");

					String query3 = "INSERT INTO public.nt_t_grievancetask_det "
							+ "(tgt_seq, tgt_complain_no, tgt_vehicle_no, tgt_task_code, tgt_status, tgt_created_by, tgt_created_date) "
							+ "	VALUES(?,?,?,?,?,?,?); ";

					ps3 = con.prepareStatement(query3);
					ps3.setLong(1, seqNoTask);
					ps3.setString(2, complaint.getComplainNo());
					ps3.setString(3, complaint.getVehicleNum());
					ps3.setString(4, "GM102");
					ps3.setString(5, "C");
					ps3.setString(6, loginUser);
					ps3.setTimestamp(7, timestamp);
					ps3.executeUpdate();

				}

				con.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			ConnectionManager.close(ps1);
			ConnectionManager.close(ps2);
			ConnectionManager.close(ps3);
			ConnectionManager.close(ps4);
			ConnectionManager.close(ps5);
			ConnectionManager.close(rs5);
			ConnectionManager.close(con);
		}
		return true;
	}

	@Override
	public List<ManageInquiryDTO> getActionDetails(String complaintNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm");

		List<ManageInquiryDTO> dtoList = new ArrayList<ManageInquiryDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT *, a.description as department_desc FROM nt_m_action_taken left join nt_r_action_department a on a.code=department_code WHERE complaint_no =? order by seq";
			ps = con.prepareStatement(query);
			ps.setString(1, complaintNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				ManageInquiryDTO dto = new ManageInquiryDTO();
				dto.setComplainNo(rs.getString("complaint_no"));
				dto.setActionDepartment(rs.getString("department_code"));
				dto.setActionDepartment_desc(rs.getString("department_desc"));
				dto.setActionTakenByGM(rs.getString("action_taken_by_assign_department"));

				if(rs.getTimestamp("action_taken_date_by_assign_department")!=null) {
					String dateString1 = dateformat.format(rs.getTimestamp("action_taken_date_by_assign_department"));
					dto.setActionTakenByGMDate(dateString1);
				} else {
					dto.setActionTakenByGMDate(null);
				}
				
				dto.setActionTakenByDepartment(rs.getString("action_taken_by_assigned_department"));

				if(rs.getTimestamp("action_taken_date_by_assigned_department")!=null) {
					String dateString2 = dateformat.format(rs.getTimestamp("action_taken_date_by_assigned_department"));
					dto.setActionTakenByDepartmentDate(dateString2);
				} else {
					dto.setActionTakenByDepartmentDate(null);
				}
				

				dtoList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return dtoList;
	}
	
	@Override
	public boolean insertGrievanceTask(ManageInquiryDTO complaint, String loginUser, String taskCode,
			String taskStatus) {
		Connection con = null;
		PreparedStatement ps3 = null;

		try {
			con = ConnectionManager.getConnection();

			java.util.Date date = new java.util.Date();
			Timestamp timestamp = new Timestamp(date.getTime());

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_grievancetask_det");

			// insert to nt_t_grievancetask_det table
			String query3 = "INSERT INTO public.nt_t_grievancetask_det "
					+ "(tgt_seq, tgt_complain_no, tgt_vehicle_no, tgt_task_code, tgt_status, tgt_created_by, tgt_created_date) "
					+ "VALUES(?,?,?,?,?,?,?); ";

			ps3 = con.prepareStatement(query3);
			ps3.setLong(1, seqNo);
			ps3.setString(2, complaint.getComplainNo());
			ps3.setString(3, complaint.getVehicleNum());
			ps3.setString(4, taskCode);
			ps3.setString(5, taskStatus);
			ps3.setString(6, loginUser);
			ps3.setTimestamp(7, timestamp);

			ps3.executeUpdate();

			ConnectionManager.commit(con);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			ConnectionManager.close(ps3);
			ConnectionManager.close(con);
		}
		return true;

	}
	@Override
	public boolean chekDataFromReportForTP(String complainNo) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean dataHave = false;
		try {
			con = ConnectionManager.getConnection();

			String sql = "select distinct  d.permit_no,d.vehicle_no,details_of_complain,(TO_CHAR(d.event_datetime,'dd-MM-yyyy')) as event_datetime,\r\n" + 
					"d.origin,d.destination,d.seq,d.other_commited_offences,d.other_commited_offences,d.complainer_name ,d.complainer_name_si ,\r\n" + 
					"d.address1_si ,d.address2_si ,d.city_si ,(TO_CHAR(d.created_date ,'yyyy-MM-dd')) as created_date,\r\n" + 
					"(CASE WHEN d.investigation_date IS NULL THEN '' ELSE TO_CHAR(d.investigation_date ,'yyyy-MM-dd')end) as investigation_date  , COALESCE( d.inq_reserve_start_date ,  ' ')as inq_reserve_start_date ,\r\n" + 
					"a.pm_service_type,b.description_si,c.pmo_full_name_sinhala,c.pmo_address1_sinhala,c.pmo_address2_sinhala,c.pmo_address3_sinhala\r\n" + 
					"from public.nt_m_complain_request d\r\n" + 
					"inner join  public.nt_t_pm_application a on  a.pm_vehicle_regno =d.vehicle_no \r\n" + 
					"inner join public.nt_r_service_types b on b.code=a.pm_service_type\r\n" + 
					"inner join public.nt_t_pm_vehi_owner c on c.pmo_vehicle_regno =d.vehicle_no\r\n" + 
					"inner join public.nt_t_grievance_commited_offence x on d.seq=x.complain_seq\r\n" + 
					"inner join public.nt_m_offence_management y on y.ofm_offence_code=x.offence_type_code\r\n" + 
					"inner join  public.nt_t_pm_vehi_owner e on a.pm_application_no = c.pmo_application_no  \r\n" + 
					"where b.active='A'\r\n" + 
					"and a.pm_status in ('A','O')"+
					"and complain_no=?";

			ps = con.prepareStatement(sql);
			ps.setString(1, complainNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				dataHave = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return dataHave;
	}
	
	private void insertTaskInquiryRecord(Connection con, ManageInquiryDTO selectedData,
			Timestamp timestamp, String status, String function, String user, String funDes) {
		
		try {
			String insertSQL = null;
			PreparedStatement psInsert = null;

			insertSQL = "INSERT INTO public.nt_t_task_inquiry (created_by, created_date, status, bus_no, complaint_no, permit_no,function_name,function_des) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

			psInsert = con.prepareStatement(insertSQL);

			psInsert.setString(1, user);
			psInsert.setTimestamp(2, timestamp);
			psInsert.setString(3, status);
			psInsert.setString(4, selectedData.getVehicleNum());
			psInsert.setString(5, selectedData.getComplainNo());
			psInsert.setString(6, selectedData.getPermitNum());
			psInsert.setString(7, function);
			psInsert.setString(8, function);

			psInsert.executeUpdate();
			con.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public void beanLinkMethod(ManageInquiryDTO selectedData,String user,String des,String funDes) {
		Connection con = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		
		try {
			con = ConnectionManager.getConnection();
			
			insertTaskInquiryRecord(con, selectedData, timestamp, des, "Grievances Management",user,funDes);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}


}
