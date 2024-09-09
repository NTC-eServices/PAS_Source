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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lk.informatics.ntc.model.dto.CommittedOffencesDTO;
import lk.informatics.ntc.model.dto.ComplaintRequestDTO;
import lk.informatics.ntc.model.dto.DriverConductorRegistrationDTO;
import lk.informatics.ntc.model.dto.DropDownDTO;
import lk.informatics.ntc.model.dto.ManageInvestigationDTO;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.roc.utils.common.Utils;

public class ManageInvestigationServiceImpl implements ManageInvestigationService {

	@Override
	public List<ManageInvestigationDTO> getChargeRefNoList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ManageInvestigationDTO> chargeRefNoList = new ArrayList<ManageInvestigationDTO>();
		String query;

		try {
			con = ConnectionManager.getConnection();

			query = "SELECT distinct(inv_investigation_no) "
					+ " FROM public.nt_t_flying_investigation_log_det A, nt_t_flying_vio_conditions B "
					+ " WHERE A.inv_investigation_no = B.vio_invesno " + " AND inv_det_delete is null "
					+ "ORDER BY inv_investigation_no desc ";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				ManageInvestigationDTO dto = new ManageInvestigationDTO();

				dto.setChargeRefCode(rs.getString("inv_investigation_no"));

				chargeRefNoList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return chargeRefNoList;
	}

	@Override
	public List<ManageInvestigationDTO> searchInvestigationDetailsByDate(Date investigationFrom, Date investigationTo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ManageInvestigationDTO> investigationList = new ArrayList<ManageInvestigationDTO>();
		String query;
		String fromDate = null;
		String toDate = null;

		try {
			con = ConnectionManager.getConnection();

			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			if (investigationFrom != null)
				fromDate = df.format(investigationFrom);
			if (investigationTo != null)
				toDate = df.format(investigationTo);

			if (fromDate != null && toDate != null) {
				query = "SELECT distinct(nt_t_flying_vio_conditions.vio_invesno), inv_det_ref_no, inv_det_report_no, "
						+ " inv_det_route_no, inv_det_route_from, inv_det_route_to, "
						+ " inv_det_service_cd, to_char(inv_det_time, 'dd/MM/yyyy') as inv_det_time, inv_det_fault, inv_det_documents, "
						+ " inv_det_invest_place, inv_det_remark, " + " inv_det_driver_id, inv_det_driver_name, "
						+ " inv_det_conductor_id, inv_det_conductor_name, " + " inv_permit_no, inv_permit_owner, "
						+ " inv_investigation_no, inv_det_vehicle_no, " + " inv_det_created_by, inv_det_created_date, "
						+ " inv_det_modified_by, inv_modified_date, "
						+ " inv_det_delete, inv_det_delete_by, inv_det_delete_date "
						+ " FROM public.nt_t_flying_investigation_log_det inner join public.nt_t_flying_vio_conditions on nt_t_flying_investigation_log_det.inv_investigation_no = nt_t_flying_vio_conditions.vio_invesno" + " WHERE inv_det_delete is null "
						+ " AND TO_DATE(TO_CHAR(inv_det_time, 'MM/DD/YYYY'), 'MM/DD/YYYY') >= DATE(?) "
						+ " AND TO_DATE(TO_CHAR(inv_det_time, 'MM/DD/YYYY'), 'MM/DD/YYYY') <= DATE(?) ";

				query = query + " order by inv_det_created_date ";

				ps = con.prepareStatement(query);

				ps.setString(1, fromDate);
				ps.setString(2, toDate);

				rs = ps.executeQuery();

				while (rs.next()) {
					ManageInvestigationDTO dto = new ManageInvestigationDTO();

					dto.setInvReferenceNo(rs.getString("inv_det_ref_no"));

					dto.setReportNo(rs.getString("inv_det_report_no"));
					dto.setRouteNo(rs.getString("inv_det_route_no"));
					dto.setFrom(rs.getString("inv_det_route_from"));
					dto.setTo(rs.getString("inv_det_route_to"));
					dto.setServiceType(rs.getString("inv_det_service_cd"));
					dto.setServiceTypeDesc(getServiceTypeDesc(dto.getServiceType()));
					dto.setDateOfInvestigationStr(rs.getString("inv_det_time"));

					dto.setDriverId(rs.getString("inv_det_driver_id"));
					dto.setDriverName(rs.getString("inv_det_driver_name"));

					dto.setConductorId(rs.getString("inv_det_conductor_id"));
					dto.setConductorName(rs.getString("inv_det_conductor_name"));

					dto.setPermitNo(rs.getString("inv_permit_no"));
					dto.setPermitOwnerName(rs.getString("inv_permit_owner"));

					dto.setChargeRefCode(rs.getString("inv_investigation_no"));
					dto.setVehicleNo(rs.getString("inv_det_vehicle_no"));

					String status = getApplicationStatus(dto.getChargeRefCode());
					if (status == null) {
						status = "P";
					}
					dto.setStatus(status);

					String statusDesc = getStatusDesc(dto.getStatus());
					dto.setStatusDesc(statusDesc);

					String currStatus = getApplicationCurrentStatus(dto.getChargeRefCode());
					if (currStatus == null) {
						currStatus = "P";
					}

					dto.setCurrentStatus(currStatus);

					String currStatusDesc = getStatusDesc(dto.getCurrentStatus());
					dto.setCurrentStatusDesc(currStatusDesc);

					investigationList.add(dto);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return investigationList;
	}

	@Override
	public List<ManageInvestigationDTO> searchInvestigationDetails(ManageInvestigationDTO searchDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ManageInvestigationDTO> investigationList = new ArrayList<ManageInvestigationDTO>();
		String query;

		try {
			con = ConnectionManager.getConnection();

			query = "SELECT distinct(nt_t_flying_vio_conditions.vio_invesno), inv_det_ref_no, inv_det_report_no, "
					+ "inv_det_route_no, inv_det_route_from, inv_det_route_to, "
					+ "inv_det_service_cd, to_char(inv_det_time, 'dd/MM/yyyy') as inv_det_time, inv_det_fault, inv_det_documents, "
					+ "inv_det_invest_place, inv_det_remark, " + "inv_det_driver_id, inv_det_driver_name, "
					+ "inv_det_conductor_id, inv_det_conductor_name, " + "inv_permit_no, inv_permit_owner, "
					+ "inv_investigation_no, inv_det_vehicle_no, " + "inv_det_created_by, inv_det_created_date, "
					+ "inv_det_modified_by, inv_modified_date, "
					+ "inv_det_delete, inv_det_delete_by, inv_det_delete_date "
					+ " FROM public.nt_t_flying_investigation_log_det  inner join public.nt_t_flying_vio_conditions on nt_t_flying_investigation_log_det.inv_investigation_no = nt_t_flying_vio_conditions.vio_invesno " + " WHERE inv_det_delete is null ";

			String chargeRef = null;
			String reportNo = null;
			String vehicleNo = null;
			String permitNo = null;
			String permitOwner = null;

			if (searchDTO.getChargeRefCode() != null && !searchDTO.getChargeRefCode().trim().isEmpty()) {
				chargeRef = searchDTO.getChargeRefCode();
				query = query + " AND inv_investigation_no = ? ORDER BY inv_det_created_date ";
				ps = con.prepareStatement(query);
				ps.setString(1, chargeRef);

			} else if (searchDTO.getReportNo() != null && !searchDTO.getReportNo().trim().isEmpty()) {
				reportNo = searchDTO.getReportNo();
				query = query + " AND inv_det_report_no = ? ORDER BY inv_det_created_date ";
				ps = con.prepareStatement(query);
				ps.setString(1, reportNo);
			} else if (searchDTO.getVehicleNo() != null && !searchDTO.getVehicleNo().trim().isEmpty()) {
				vehicleNo = searchDTO.getVehicleNo();
				query = query + " AND inv_det_vehicle_no = ? ORDER BY inv_det_created_date ";
				ps = con.prepareStatement(query);
				ps.setString(1, vehicleNo);
			} else if (searchDTO.getPermitNo() != null && !searchDTO.getPermitNo().trim().isEmpty()) {
				permitNo = searchDTO.getPermitNo();
				query = query + " AND inv_permit_no = ? ORDER BY inv_det_created_date ";
				ps = con.prepareStatement(query);
				ps.setString(1, permitNo);
			} else if (searchDTO.getPermitOwnerName() != null && !searchDTO.getPermitOwnerName().trim().isEmpty()) {
				permitOwner = searchDTO.getPermitOwnerName();
				query = query + " AND inv_permit_owner = ? ORDER BY inv_det_created_date ";
				ps = con.prepareStatement(query);
				ps.setString(1, permitOwner);
			}

			rs = ps.executeQuery();

			while (rs.next()) {
				ManageInvestigationDTO dto = new ManageInvestigationDTO();

				dto.setInvReferenceNo(rs.getString("inv_det_ref_no"));

				dto.setReportNo(rs.getString("inv_det_report_no"));
				dto.setRouteNo(rs.getString("inv_det_route_no"));
				dto.setFrom(rs.getString("inv_det_route_from"));
				dto.setTo(rs.getString("inv_det_route_to"));
				dto.setServiceType(rs.getString("inv_det_service_cd"));
				dto.setServiceTypeDesc(getServiceTypeDesc(dto.getServiceType()));
				dto.setDateOfInvestigationStr(rs.getString("inv_det_time"));

				dto.setDriverId(rs.getString("inv_det_driver_id"));
				dto.setDriverName(rs.getString("inv_det_driver_name"));

				dto.setConductorId(rs.getString("inv_det_conductor_id"));
				dto.setConductorName(rs.getString("inv_det_conductor_name"));

				dto.setPermitNo(rs.getString("inv_permit_no"));
				dto.setPermitOwnerName(rs.getString("inv_permit_owner"));

				dto.setChargeRefCode(rs.getString("inv_investigation_no"));
				dto.setVehicleNo(rs.getString("inv_det_vehicle_no"));

				String status = getApplicationStatus(dto.getChargeRefCode());
				if (status == null) {
					status = "P";
				}
				dto.setStatus(status);

				String statusDesc = getStatusDesc(dto.getStatus());
				dto.setStatusDesc(statusDesc);

				String currStatus = getApplicationCurrentStatus(dto.getChargeRefCode());
				if (currStatus == null) {
					currStatus = "P";
				}

				dto.setCurrentStatus(currStatus);

				String currStatusDesc = getStatusDesc(dto.getCurrentStatus());
				dto.setCurrentStatusDesc(currStatusDesc);

				investigationList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return investigationList;
	}

	@Override
	public List<DropDownDTO> getDrivers() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<DropDownDTO> drpdwnDTOList = new ArrayList<DropDownDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct(dcr_driver_conductor_id) FROM nt_t_driver_conductor_registration a"
					+ " WHERE a.dcr_driver_conductor_id LIKE 'D%' AND a.dcr_status IN ('A','TA') ORDER BY dcr_driver_conductor_id;";

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
	public List<DropDownDTO> getConductors() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<DropDownDTO> drpdwnDTOList = new ArrayList<DropDownDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct(dcr_driver_conductor_id) FROM nt_t_driver_conductor_registration a"
					+ " WHERE a.dcr_driver_conductor_id LIKE 'C%' AND a.dcr_status IN ('A','TA') ORDER BY dcr_driver_conductor_id;";

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
	public DriverConductorRegistrationDTO getDriverConductorData(DriverConductorRegistrationDTO dcData) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM public.nt_t_driver_conductor_registration a WHERE a.dcr_driver_conductor_id = ? AND a.dcr_status IN ('A','TA') ORDER BY dcr_driver_conductor_id; ";

			ps = con.prepareStatement(query);
			ps.setString(1, dcData.getDriverConductorId());
			rs = ps.executeQuery();

			dcData.setDriverConductorId(null);
			dcData.setFullNameEng(null);
			dcData.setAdd1Eng(null);
			dcData.setAdd2Eng(null);
			dcData.setCityEng(null);

			while (rs.next()) {
				dcData.setDriverConductorId(rs.getString("dcr_driver_conductor_id"));
				dcData.setNic(rs.getString("dcr_nic"));
				dcData.setFullNameEng(rs.getString("dcr_full_name_eng"));
				dcData.setAdd1Eng(rs.getString("dcr_add_1_eng"));
				dcData.setAdd2Eng(rs.getString("dcr_add_2_eng"));
				dcData.setCityEng(rs.getString("dcr_city_eng"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return dcData;
	}

	@Override
	public List<ManageInvestigationDTO> getInvestigationChargesByRef(ManageInvestigationDTO selectedInvestigation,String chargeRefNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		List<ManageInvestigationDTO> chargesList = new ArrayList<ManageInvestigationDTO>();
		String query;

		try {
			con = ConnectionManager.getConnection();

			if (chargeRefNo != null) {

				query = "SELECT A.vio_condition_cd, B.ofm_offence_desc, C.omd_charge_amount, A.remark, C.omd_attempt_code, D.description "
						+ "FROM public.nt_t_flying_vio_conditions A, nt_m_offence_management B, nt_t_offence_management_det C, nt_r_attempts D "
						+ "WHERE A.vio_condition_cd = B.ofm_offence_code "
						+ "AND A.vio_condition_cd = C.omd_offence_code "
						+ "AND B.ofm_offence_code = C.omd_offence_code " + "AND C.omd_attempt_code = D.code "
						+ "AND A.vio_invesno = ? "
						/* + "AND C.omd_is_investigation = 'Y'" */
						+ "AND C.omd_attempt_code = 'FT' "
						+ "AND A.vio_violated_status = 'true' ";

				query = query + " order by vio_condition_cd ";

				ps = con.prepareStatement(query);

				ps.setString(1, chargeRefNo);

				rs = ps.executeQuery();

				while (rs.next()) {
					ManageInvestigationDTO dto = new ManageInvestigationDTO();

					dto.setChargeCode(rs.getString("vio_condition_cd"));
					dto.setChargeDesc(rs.getString("ofm_offence_desc"));
					dto.setAmount(rs.getBigDecimal("omd_charge_amount"));
					dto.setAttemptCode(rs.getString("omd_attempt_code"));
					dto.setAttemptDesc(rs.getString("description"));

					chargesList.add(dto);
				}
				

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return chargesList;
	}

	@Override
	public List<DropDownDTO> getAttempts() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<DropDownDTO> attemptsList = new ArrayList<DropDownDTO>();
		String query;

		try {
			con = ConnectionManager.getConnection();

			query = "SELECT code, description FROM public.nt_r_attempts ORDER BY code ";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				DropDownDTO dto = new DropDownDTO();

				dto.setCode(rs.getString("code"));
				dto.setDescription(rs.getString("description"));

				attemptsList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return attemptsList;
	}

	@Override
	public List<DropDownDTO> getActions() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<DropDownDTO> actionsList = new ArrayList<DropDownDTO>();
		String query;

		try {
			con = ConnectionManager.getConnection();

			query = "SELECT code, description FROM public.nt_r_action_types ORDER BY code ";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				DropDownDTO dto = new DropDownDTO();

				dto.setCode(rs.getString("code"));
				dto.setDescription(rs.getString("description"));

				actionsList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return actionsList;
	}

	@Override
	public boolean saveInvestigationMaster(ManageInvestigationDTO selectedInvestigation,BigDecimal latePayment) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean success = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String dupQuery = "SELECT count(*) as dupCheck FROM public.nt_t_investigation_charge_master WHERE charge_ref_no = ? ";

			ps = con.prepareStatement(dupQuery);
			ps.setString(1, selectedInvestigation.getChargeRefCode());

			rs = ps.executeQuery();

			int dupCheck = 0;
			while (rs.next()) {
				dupCheck = rs.getInt("dupCheck");
			}

			ConnectionManager.close(ps);
			ConnectionManager.close(rs);

			if (dupCheck == 0) {
				String instertQuery = "INSERT INTO public.nt_t_investigation_charge_master " + "(charge_ref_no, "
						+ "charge_permit, " + "charge_permit_owner, " + "charge_vehicle, " + "charge_inv_date, "
						+ "charge_app_status, " + "charge_app_current_status, " + "driver_conductor_confirmed,"
						+ "created_by, " + "created_date)" + " VALUES(?,?,?,?,?,?,?,?,?,?)";

				ps = con.prepareStatement(instertQuery);

				ps.setString(1, selectedInvestigation.getChargeRefCode());
				ps.setString(2, selectedInvestigation.getPermitNo());
				ps.setString(3, selectedInvestigation.getPermitOwnerName());
				ps.setString(4, selectedInvestigation.getVehicleNo());

				Date invDate = new SimpleDateFormat("dd/MM/yyyy")
						.parse(selectedInvestigation.getDateOfInvestigationStr());
				java.sql.Date sqlInvDate = new java.sql.Date(invDate.getTime());
				ps.setDate(5, sqlInvDate);

				ps.setString(6, "O");
				ps.setString(7, "MS");
				ps.setString(8, "Y");
				ps.setString(9, selectedInvestigation.getLoginUser());
				ps.setTimestamp(10, timestamp);

				ps.executeUpdate();

				con.commit();

			} else {

				String updateQuery = "UPDATE public.nt_t_investigation_charge_master " + "SET charge_tot_amount = ?, "
						+ "tot_driver_points = ?, " + "tot_conductor_points = ?," + "charge_special_remark = ? ,charge_late_payment_amount = ? "
						+ "WHERE charge_ref_no = ? ";

				ps = con.prepareStatement(updateQuery);

				ps.setBigDecimal(1, selectedInvestigation.getFinalAmount());
				ps.setBigDecimal(2, selectedInvestigation.getTotalDriverPoints());
				ps.setBigDecimal(3, selectedInvestigation.getTotalConductorPoints());
				ps.setString(4, selectedInvestigation.getSpecialRemark());
				ps.setBigDecimal(5, latePayment);
				ps.setString(6, selectedInvestigation.getChargeRefCode());

				ps.executeUpdate();

				con.commit();
				
				try {
					insertTaskInquiryRecord(con, selectedInvestigation, timestamp, "Edit Investigation Master", "Investigation Management","Manage Investigation / Charges");
					
				} catch (Exception e) {
					System.out.println("Data Insert Error");
				}
			}

			success = true;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return success;
	}

	@Override
	public boolean saveChargeSheet(ManageInvestigationDTO chargeFinalization,
			List<ManageInvestigationDTO> chargesList) {
		Connection con = null;
		PreparedStatement ps = null;
		boolean success = false;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		
		
		try {
			con = ConnectionManager.getConnection();
			String query = "";
			
			for (ManageInvestigationDTO dto : chargesList) {
				if(dto.getAttemptDesc().trim().equalsIgnoreCase("First Time") ) {
					dto.setAttemptCode("FT");
    			}
				if(dto.getAttemptDesc().trim().equalsIgnoreCase("Second Time") ) {
					dto.setAttemptCode("ST");
    			}
				if(dto.getAttemptDesc().trim().equalsIgnoreCase("Third Time")) {
					dto.setAttemptCode("TT");
    			}
				if(dto.getAttemptDesc().trim().equalsIgnoreCase("Fourth Time")) {
					dto.setAttemptCode("FOT");
    			}
				
				String dupQuery = "SELECT count(*) as dupCheck FROM public.nt_t_investigation_charge_sheet WHERE charge_ref_no = ? AND charge_offence = ? AND  charge_attempt = ?";

				ps = con.prepareStatement(dupQuery);
				ps.setString(1, chargeFinalization.getChargeRefCode());
				ps.setString(2, dto.getChargeCode());
				ps.setString(3, dto.getAttemptCode());
				
				rs = ps.executeQuery();

				int dupCheck = 0;
				while (rs.next()) {
					dupCheck = rs.getInt("dupCheck");
				}

				ConnectionManager.close(ps);
				ConnectionManager.close(rs);

				if (dupCheck == 0  ) {
				
					query = "INSERT INTO public.nt_t_investigation_charge_sheet " + "(charge_ref_no, "
							+ "charge_offence, " + "charge_attempt, " + "charge_amount, " + "charge_driver_id, "
							+ "charge_driver_points, " + "charge_conductor_id, " + "charge_conductor_points, "
							+ "created_by, " + "created_date)" + " VALUES(?,?,?,?,?,?,?,?,?,?)";

					ps = con.prepareStatement(query);

					ps.setString(1, chargeFinalization.getChargeRefCode());
					ps.setString(2, dto.getChargeCode());
					ps.setString(3, dto.getAttemptCode());
					ps.setBigDecimal(4, dto.getAmount());
					ps.setString(5, chargeFinalization.getDriverId());
					ps.setBigDecimal(6, dto.getDriverPoints());
					ps.setString(7, chargeFinalization.getConductorId());
					ps.setBigDecimal(8, dto.getConductorPoints());
					ps.setString(9, chargeFinalization.getLoginUser());
					ps.setTimestamp(10, timestamp);

					ps.executeUpdate();

					con.commit();

				} else {
					String updateQuery = "UPDATE public.nt_t_investigation_charge_sheet " + "SET charge_attempt = ?, "
							+ "charge_amount = ?, " + "charge_driver_points = ?," + "charge_conductor_points = ?,  "
							+ "modified_by = ?, " + "modified_date = ? " + "WHERE charge_ref_no = ? "
							+ "AND charge_offence = ? ";

					ps = con.prepareStatement(updateQuery);

					ps.setString(1, dto.getAttemptCode());
					ps.setBigDecimal(2, dto.getAmount());
					ps.setBigDecimal(3, dto.getDriverPoints());
					ps.setBigDecimal(4, dto.getConductorPoints());
					ps.setString(5, chargeFinalization.getLoginUser());
					ps.setTimestamp(6, timestamp);
					ps.setString(7, chargeFinalization.getChargeRefCode());
					ps.setString(8, dto.getChargeCode());

					ps.executeUpdate();

					con.commit();
				}

				success = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return success;
	}

	@Override
	public boolean updateCurrentStatus(ManageInvestigationDTO selectedInvestigation, String chargeRef, String status, String user,String des) {
		Connection con = null;
		PreparedStatement ps = null;
		boolean success = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();
			String query = "";

			query = "UPDATE public.nt_t_investigation_charge_master " + "SET charge_app_current_status = ?, "
					+ "modified_by = ?, " + "modified_date = ? " + "WHERE charge_ref_no = ? ";

			ps = con.prepareStatement(query);

			ps.setString(1, status);
			ps.setString(2, user);
			ps.setTimestamp(3, timestamp);
			ps.setString(4, chargeRef);

			ps.executeUpdate();

			con.commit();
			
			
			
			success = true;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return success;
	}
	
	@Override
	public boolean updateCurrentStatus(String chargeRef, String status, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		boolean success = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();
			String query = "";

			query = "UPDATE public.nt_t_investigation_charge_master " + "SET charge_app_current_status = ?, "
					+ "modified_by = ?, " + "modified_date = ? " + "WHERE charge_ref_no = ? ";

			ps = con.prepareStatement(query);

			ps.setString(1, status);
			ps.setString(2, user);
			ps.setTimestamp(3, timestamp);
			ps.setString(4, chargeRef);

			ps.executeUpdate();

			con.commit();
		
			success = true;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return success;
	}


	@Override
	public String getApplicationStatus(String chargeRef) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String status = null;

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT charge_app_status FROM public.nt_t_investigation_charge_master "
					+ "WHERE charge_ref_no = ? ";

			ps = con.prepareStatement(query);
			ps.setString(1, chargeRef);

			rs = ps.executeQuery();

			while (rs.next()) {
				status = rs.getString("charge_app_status");
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
	public String getServiceTypeDesc(String code) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String desc = null;

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT description FROM public.nt_r_service_types " + "WHERE code = ? ";

			ps = con.prepareStatement(query);
			ps.setString(1, code);

			rs = ps.executeQuery();

			while (rs.next()) {
				desc = rs.getString("description");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return desc;
	}

	@Override
	public List<ManageInvestigationDTO> getChargeSheetByChargeRef(ManageInvestigationDTO selectedInvestigation, String chargeRef) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		List<ManageInvestigationDTO> chargeSheetList = new ArrayList<ManageInvestigationDTO>();
		String query;
		String queryOffenceDesc;
		String queryAttemptDesc;
		
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			query = "SELECT * " + "FROM public.nt_t_investigation_charge_sheet " + "WHERE charge_ref_no = ? "
					+ "ORDER BY charge_offence asc ";

			ps = con.prepareStatement(query);

			ps.setString(1, chargeRef);

			rs = ps.executeQuery();

			while (rs.next()) {
				ManageInvestigationDTO dto = new ManageInvestigationDTO();

				dto.setChargeCode(rs.getString("charge_offence"));

				if (dto.getChargeCode() != null) {

					queryOffenceDesc = "SELECT ofm_offence_desc " + "FROM public.nt_m_offence_management "
							+ "WHERE ofm_offence_code = ? ";

					ps2 = con.prepareStatement(queryOffenceDesc);
					ps2.setString(1, dto.getChargeCode());
					rs2 = ps2.executeQuery();
					while (rs2.next()) {
						dto.setChargeDesc(rs2.getString("ofm_offence_desc"));
					}
				}

				dto.setAttemptCode(rs.getString("charge_attempt"));

				if (dto.getAttemptCode() != null) {

					queryAttemptDesc = "SELECT description " + "FROM public.nt_r_attempts " + "WHERE code = ? ";

					ps3 = con.prepareStatement(queryAttemptDesc);
					ps3.setString(1, dto.getAttemptCode());
					rs3 = ps3.executeQuery();
					while (rs3.next()) {
						dto.setAttemptDesc(rs3.getString("description"));
					}
				}

				dto.setAmount(rs.getBigDecimal("charge_amount"));
				dto.setDriverId(rs.getString("charge_driver_id"));
				dto.setDriverPoints(rs.getBigDecimal("charge_driver_points"));
				if (dto.getDriverPoints() != null) {
					dto.setDriverApplicable(true);
				}
				dto.setConductorId(rs.getString("charge_conductor_id"));
				dto.setConductorPoints(rs.getBigDecimal("charge_conductor_points"));
				if (dto.getConductorPoints() != null) {
					dto.setConductorApplicable(true);
				}

				chargeSheetList.add(dto);
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(ps2);
			ConnectionManager.close(rs2);
			ConnectionManager.close(con);
		}
		return chargeSheetList;
	}

	@Override
	public ManageInvestigationDTO getChargeSheetMaster(ManageInvestigationDTO chargeDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query;
		
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			query = "SELECT * " + "FROM public.nt_t_investigation_charge_master " + "WHERE charge_ref_no = ? ";

			ps = con.prepareStatement(query);

			ps.setString(1, chargeDTO.getChargeRefCode());

			rs = ps.executeQuery();

			while (rs.next()) {

				chargeDTO.setSpecialRemark(rs.getString("charge_special_remark"));

				chargeDTO.setPermitDepartment(rs.getString("permit_action_department"));
				chargeDTO.setPermitActionType(rs.getString("permit_action_type"));
				chargeDTO.setPermitActionDesc(rs.getString("permit_action_description"));

				chargeDTO.setDriverDepartment(rs.getString("driver_action_department"));
				chargeDTO.setDriverActionType(rs.getString("driver_action_type"));
				chargeDTO.setDriverActionDesc(rs.getString("driver_action_description"));

				chargeDTO.setConductorDepartment(rs.getString("conductor_action_department"));
				chargeDTO.setConductorActionType(rs.getString("conductor_action_type"));
				chargeDTO.setConductorActionDesc(rs.getString("conductor_action_description"));

			}
		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return chargeDTO;
	}

	@Override
	public boolean updateAppStatus(ManageInvestigationDTO selectedInvestigation, String chargeRef, String status, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		boolean success = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();
			String query = "";

			query = "UPDATE public.nt_t_investigation_charge_master " + "SET charge_app_status = ?, "
					+ "modified_by = ?, " + "modified_date = ? " + "WHERE charge_ref_no = ? ";

			ps = con.prepareStatement(query);

			ps.setString(1, status);
			ps.setString(2, user);
			ps.setTimestamp(3, timestamp);
			ps.setString(4, chargeRef);

			ps.executeUpdate();

			con.commit();
			
			success = true;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return success;
	}

	@Override
	public String createVoucher(ManageInvestigationDTO selectedInvestigation) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		String voucherNo = null;

		try {
			con = ConnectionManager.getConnection();

			String accountQuery = "select tct_account_no " + "from nt_r_trn_charge_type "
					+ "where tct_trn_type_code='IV' " + "and tct_charge_type_code = 'IV001' ";

			ps = con.prepareStatement(accountQuery);

			rs = ps.executeQuery();

			String accountNo = null;
			while (rs.next()) {
				accountNo = rs.getString("tct_account_no");
			}

			ConnectionManager.close(ps);
			ConnectionManager.close(rs);

			String dupQuery = "select count(*) as duplicate " + "from nt_t_investigation_charge_voucher "
					+ "where inv_charge_ref_no = ? ";

			ps = con.prepareStatement(dupQuery);
			ps.setString(1, selectedInvestigation.getChargeRefCode());

			rs = ps.executeQuery();

			int duplicate = 0;
			while (rs.next()) {
				duplicate = rs.getInt("duplicate");
			}

			ConnectionManager.close(ps);
			ConnectionManager.close(rs);

			String insQuery = "INSERT INTO public.nt_t_investigation_charge_voucher " + "(inv_voucher_no, "
					+ "inv_charge_ref_no, " + "inv_charge_amount_tot, " + "inv_created_by, " + "inv_created_date, "
					+ "inv_voucher_approved, " + "inv_approved_by," + "inv_approved_date," + "inv_account_no)"
					+ " VALUES(?,?,?,?,?,?,?,?,?)";

			ps = con.prepareStatement(insQuery);

			DateFormat dfYear = new SimpleDateFormat("yy");
			String currentYear = dfYear.format(Calendar.getInstance().getTime());

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_investigation_voucher");
			voucherNo = "INV" + currentYear + String.format("%05d", seqNo);

			ps.setString(1, voucherNo);
			ps.setString(2, selectedInvestigation.getChargeRefCode());
			ps.setBigDecimal(3, selectedInvestigation.getFinalAmount());
			ps.setString(4, selectedInvestigation.getLoginUser());
			ps.setTimestamp(5, timestamp);
			ps.setString(6, "Y");
			ps.setString(7, selectedInvestigation.getLoginUser());
			ps.setTimestamp(8, timestamp);
			ps.setString(9, accountNo);

			ps.executeUpdate();

			con.commit();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return voucherNo;

	}

	@Override
	public boolean approveVoucher(String voucherNo, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		boolean success = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();
			String query = "";

			query = "UPDATE public.nt_t_investigation_charge_voucher " + "SET inv_voucher_approved = ?, "
					+ "inv_approved_by = ?, " + "inv_approved_date = ? " + "WHERE inv_voucher_no = ? ";

			ps = con.prepareStatement(query);

			ps.setString(1, "Y");
			ps.setString(2, user);
			ps.setTimestamp(3, timestamp);
			ps.setString(4, voucherNo);

			ps.executeUpdate();

			con.commit();
			success = true;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return success;
	}

	@Override
	public boolean rejectVoucher(String voucherNo, String user, String rejectReason) {
		Connection con = null;
		PreparedStatement ps = null;
		boolean success = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();
			String query = "";

			query = "UPDATE public.nt_t_investigation_charge_voucher " + "SET inv_voucher_rejected = ?, "
					+ "inv_approved_reject_by = ?, " + "inv_approved_reject_date = ?," + "inv_reject_reason = ? "
					+ "WHERE inv_voucher_no = ? ";

			ps = con.prepareStatement(query);

			ps.setString(1, "Y");
			ps.setString(2, user);
			ps.setTimestamp(3, timestamp);
			ps.setString(4, rejectReason);
			ps.setString(5, voucherNo);

			ps.executeUpdate();

			con.commit();
			success = true;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return success;
	}

	@Override
	public BigDecimal getChargeTotAmount(String chargeRef) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		BigDecimal totAmount = null;

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT charge_tot_amount FROM public.nt_t_investigation_charge_master "
					+ "WHERE charge_ref_no = ? ";

			ps = con.prepareStatement(query);
			ps.setString(1, chargeRef);

			rs = ps.executeQuery();

			while (rs.next()) {
				totAmount = rs.getBigDecimal("charge_tot_amount");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return totAmount;
	}

	@Override
	public boolean updateAction(String category, String chargeRef, String department, String actionType,
			String actionDesc) {
		Connection con = null;
		PreparedStatement ps = null;
		boolean success = false;
		String queryP = null;
		String queryD = null;
		String queryC = null;

		try {
			con = ConnectionManager.getConnection();

			queryP = "UPDATE public.nt_t_investigation_charge_master " + "SET permit_action_department = ?, "
					+ "permit_action_type = ?, " + "permit_action_description = ? " + "WHERE charge_ref_no = ? ";

			queryD = "UPDATE public.nt_t_investigation_charge_master " + "SET driver_action_department = ?, "
					+ "driver_action_type = ?, " + "driver_action_description = ? " + "WHERE charge_ref_no = ? ";

			queryC = "UPDATE public.nt_t_investigation_charge_master " + "SET conductor_action_department = ?, "
					+ "conductor_action_type = ?, " + "conductor_action_description = ? " + "WHERE charge_ref_no = ? ";

			if (category.equalsIgnoreCase("O")) {
				ps = con.prepareStatement(queryP);
			} else if (category.equalsIgnoreCase("D")) {
				ps = con.prepareStatement(queryD);
			} else if (category.equalsIgnoreCase("C")) {
				ps = con.prepareStatement(queryC);
			}

			ps.setString(1, department);
			ps.setString(2, actionType);
			ps.setString(3, actionDesc);
			ps.setString(4, chargeRef);

			ps.executeUpdate();

			con.commit();
			success = true;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return success;
	}

	@Override
	public boolean checkDriverConductorDet(ManageInvestigationDTO selectedInvestigation) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean success = false;

		try {
			con = ConnectionManager.getConnection();

			String dupQuery = "SELECT count(*) as dupCheck FROM public.nt_t_investigation_driver_conductor_det WHERE charge_ref_no = ? ";

			ps = con.prepareStatement(dupQuery);
			ps.setString(1, selectedInvestigation.getChargeRefCode());

			rs = ps.executeQuery();

			int dupCheck = 0;
			while (rs.next()) {
				dupCheck = rs.getInt("dupCheck");
			}

			if (dupCheck == 0) {
				success = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return success;
	}

	@Override
	public boolean saveDriverConductorDet(ManageInvestigationDTO selectedInvestigation,
			DriverConductorRegistrationDTO driverData, DriverConductorRegistrationDTO conductorData) {
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean success = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String dupQuery = "SELECT count(*) as dupCheck FROM public.nt_t_investigation_driver_conductor_det WHERE charge_ref_no = ? ";

			ps = con.prepareStatement(dupQuery);
			ps.setString(1, selectedInvestigation.getChargeRefCode());

			rs = ps.executeQuery();

			int dupCheck = 0;
			while (rs.next()) {
				dupCheck = rs.getInt("dupCheck");
			}

			ConnectionManager.close(ps);
			ConnectionManager.close(rs);

			if (dupCheck == 0) {
				String insertQuery = "INSERT INTO public.nt_t_investigation_driver_conductor_det " + "(charge_ref_no, "
						+ "driver_id, " + "driver_nic, " + "driver_name, " + "driver_add_one, " + "driver_add_two, "
						+ "driver_add_three, " + "conductor_id, " + "conductor_nic, " + "conductor_name, "
						+ "conductor_add_one, " + "conductor_add_two, " + "conductor_add_three, " + "created_by, "
						+ "created_date)" + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

				ps = con.prepareStatement(insertQuery);

				ps.setString(1, selectedInvestigation.getChargeRefCode());
				ps.setString(2, driverData.getDriverConductorId());
				ps.setString(3, driverData.getNic());
				ps.setString(4, driverData.getFullNameEng());
				ps.setString(5, driverData.getAdd1Eng());
				ps.setString(6, driverData.getAdd2Eng());
				ps.setString(7, driverData.getCityEng());
				ps.setString(8, conductorData.getDriverConductorId());
				ps.setString(9, conductorData.getNic());
				ps.setString(10, conductorData.getFullNameEng());
				ps.setString(11, conductorData.getAdd1Eng());
				ps.setString(12, conductorData.getAdd2Eng());
				ps.setString(13, conductorData.getCityEng());

				ps.setString(14, selectedInvestigation.getLoginUser());
				ps.setTimestamp(15, timestamp);

				ps.executeUpdate();

				con.commit();
			
			} else {
				String updateQuery = "UPDATE public.nt_t_investigation_driver_conductor_det " + "SET driver_id = ?, "
						+ "driver_nic = ?, " + "driver_name = ?, " + "driver_add_one = ?, " + "driver_add_two = ?, "
						+ "driver_add_three = ?, " + "conductor_id = ?, " + "conductor_nic = ?, "
						+ "conductor_name = ?, " + "conductor_add_one = ?, " + "conductor_add_two = ?, "
						+ "conductor_add_three = ?, " + "modified_by = ?, " + "modified_date = ? "
						+ "WHERE charge_ref_no = ? ";

				ps = con.prepareStatement(updateQuery);

				ps.setString(1, driverData.getDriverConductorId());
				ps.setString(2, driverData.getNic());
				ps.setString(3, driverData.getFullNameEng());
				ps.setString(4, driverData.getAdd1Eng());
				ps.setString(5, driverData.getAdd2Eng());
				ps.setString(6, driverData.getCityEng());
				ps.setString(7, conductorData.getDriverConductorId());
				ps.setString(8, conductorData.getNic());
				ps.setString(9, conductorData.getFullNameEng());
				ps.setString(10, conductorData.getAdd1Eng());
				ps.setString(11, conductorData.getAdd2Eng());
				ps.setString(12, conductorData.getCityEng());

				ps.setString(13, selectedInvestigation.getLoginUser());
				ps.setTimestamp(14, timestamp);
				ps.setString(15, selectedInvestigation.getChargeRefCode());

				ps.executeUpdate();

				con.commit();
				
				try {
					insertTaskInquiryRecord(con, selectedInvestigation, timestamp, "Update Driver/Conductor Details", "Investigation Management","Manage Investigation / Charges");
					
				} catch (Exception e) {
					System.out.println("Data Insert Error");
				}
			}

			success = true;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return success;
	}

	@Override
	public DriverConductorRegistrationDTO getConfirmedDriver(ManageInvestigationDTO selectedInvestigation,String chargeRef) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		DriverConductorRegistrationDTO driver = null;
		
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * " + "FROM public.nt_t_investigation_driver_conductor_det "
					+ "WHERE charge_ref_no = ? ";

			ps = con.prepareStatement(query);

			ps.setString(1, chargeRef);

			rs = ps.executeQuery();

			while (rs.next()) {
				driver = new DriverConductorRegistrationDTO();

				driver.setDriverConductorId(rs.getString("driver_id"));
				driver.setNic(rs.getString("driver_nic"));
				driver.setFullNameEng(rs.getString("driver_name"));
				driver.setAdd1Eng(rs.getString("driver_add_one"));
				driver.setAdd2Eng(rs.getString("driver_add_two"));
				driver.setCityEng(rs.getString("driver_add_three"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return driver;
	}

	@Override
	public DriverConductorRegistrationDTO getConfirmedConductor(ManageInvestigationDTO selectedInvestigation, String chargeRef) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		DriverConductorRegistrationDTO conductor = null;
		
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * " + "FROM public.nt_t_investigation_driver_conductor_det "
					+ "WHERE charge_ref_no = ? ";

			ps = con.prepareStatement(query);

			ps.setString(1, chargeRef);

			rs = ps.executeQuery();

			while (rs.next()) {
				conductor = new DriverConductorRegistrationDTO();

				conductor.setDriverConductorId(rs.getString("conductor_id"));
				conductor.setNic(rs.getString("conductor_nic"));
				conductor.setFullNameEng(rs.getString("conductor_name"));
				conductor.setAdd1Eng(rs.getString("conductor_add_one"));
				conductor.setAdd2Eng(rs.getString("conductor_add_two"));
				conductor.setCityEng(rs.getString("conductor_add_three"));

			}
			
			try {
				insertTaskInquiryRecord(con, selectedInvestigation, timestamp, "Got Confirmed Conductor Details", "Investigation Management","Manage Investigation / Charges");
				
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return conductor;
	}

	@Override
	public List<ManageInvestigationDTO> approvedVoucherForInves() {
		Connection con = null;
		PreparedStatement ps = null;

		ResultSet rs = null;

		List<ManageInvestigationDTO> voucherNoList = new ArrayList<ManageInvestigationDTO>();
		String query;

		try {
			con = ConnectionManager.getConnection();

			query = "SELECT * FROM public.nt_t_investigation_charge_voucher where inv_voucher_approved='Y' order by inv_approved_date desc ";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				ManageInvestigationDTO dto = new ManageInvestigationDTO();

				dto.setVoucherNo(rs.getString("inv_voucher_no"));

				voucherNoList.add(dto);
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
	public List<ManageInvestigationDTO> getPendingGenerateReceiptListForInves() {
		Connection con = null;
		PreparedStatement ps = null;

		ResultSet rs = null;

		List<ManageInvestigationDTO> voucherNoList = new ArrayList<ManageInvestigationDTO>();
		String query;

		try {
			con = ConnectionManager.getConnection();

			query = "SELECT *,b.charge_vehicle as charge_vehicle FROM public.nt_t_investigation_charge_voucher a\r\n"
					+ "inner join public.nt_t_investigation_charge_master b on b.charge_ref_no=a.inv_charge_ref_no\r\n"
					+ "where inv_voucher_approved='Y' order by inv_voucher_no desc ";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				ManageInvestigationDTO dto = new ManageInvestigationDTO();

				dto.setVoucherNo(rs.getString("inv_voucher_no"));
				dto.setInvReferenceNo(rs.getString("inv_charge_ref_no"));
				dto.setVehicleNo(rs.getString("charge_vehicle"));
				dto.setVoucherApprovedStatus("Approved");

				voucherNoList.add(dto);
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
	public List<ManageInvestigationDTO> approvedChargeRefNO() {
		Connection con = null;
		PreparedStatement ps = null;

		ResultSet rs = null;

		List<ManageInvestigationDTO> chargeRefNoList = new ArrayList<ManageInvestigationDTO>();
		String query;

		try {
			con = ConnectionManager.getConnection();

			query = "SELECT * FROM public.nt_t_investigation_charge_voucher where inv_voucher_approved='Y' ";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				ManageInvestigationDTO dto = new ManageInvestigationDTO();

				dto.setInvReferenceNo(rs.getString("inv_charge_ref_no"));

				chargeRefNoList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);

			ConnectionManager.close(con);
		}
		return chargeRefNoList;
	}

	@Override
	public ManageInvestigationDTO showDataByVoucherOrChargeSheetNo(String voucherNo, String chargeRefNo) {
		Connection con = null;
		PreparedStatement ps = null;

		ResultSet rs = null;

		String query;
		ManageInvestigationDTO dto = new ManageInvestigationDTO();

		try {
			con = ConnectionManager.getConnection();
			if (voucherNo == null && chargeRefNo != null) {
				query = "SELECT *,b.charge_vehicle FROM public.nt_t_investigation_charge_voucher\r\n"
						+ "inner join public.nt_t_investigation_charge_master b on b.charge_ref_no=nt_t_investigation_charge_voucher.inv_charge_ref_no\r\n"
						+ "where inv_voucher_approved='Y' and  inv_charge_ref_no=?";

				ps = con.prepareStatement(query);

				ps.setString(1, chargeRefNo);
				rs = ps.executeQuery();
			}

			else if (voucherNo != null && chargeRefNo == null) {

				query = "SELECT *,b.charge_vehicle FROM public.nt_t_investigation_charge_voucher\r\n"
						+ "inner join public.nt_t_investigation_charge_master b on b.charge_ref_no=nt_t_investigation_charge_voucher.inv_charge_ref_no\r\n"
						+ "where inv_voucher_approved='Y' and  inv_voucher_no=?";

				ps = con.prepareStatement(query);

				ps.setString(1, voucherNo);
				rs = ps.executeQuery();
			}
			while (rs.next()) {
				dto = new ManageInvestigationDTO();

				dto.setInvReferenceNo(rs.getString("inv_charge_ref_no"));
				dto.setVoucherNo(rs.getString("inv_voucher_no"));
				dto.setVehicleNo(rs.getString("charge_vehicle"));

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
	public boolean isReceiptgeneratedForInves(String voucherNo) {
		boolean isReceiptgenerated = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT  rec_is_receipt_generated FROM public.nt_t_investigation_receipt "
					+ "WHERE rec_voucher_no=? LIMIT 1;";

			ps = con.prepareStatement(query);
			ps.setString(1, voucherNo);
			rs = ps.executeQuery();

			if (rs.next() && rs.getString("rec_is_receipt_generated").equals("Y")) {
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
	public ManageInvestigationDTO getReceiptDetailsForInves(String voucherNo) {
		ManageInvestigationDTO receiptDetails = new ManageInvestigationDTO();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps2 = null;
		ResultSet rs2 = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT rec_payment_mode, rec_back_code, rec_branch_code, rec_is_diposit_to_branch,rec_receipt_no,\r\n"
					+ "rec_cheque_no, rec_remarks \r\n"
					+ "FROM public.nt_t_investigation_receipt WHERE rec_voucher_no=? LIMIT 1;";

			ps = con.prepareStatement(query);
			ps.setString(1, voucherNo);
			rs = ps.executeQuery();

			if (rs.next()) {
				receiptDetails.setPaymentModeCodeForGrInves(rs.getString("rec_payment_mode"));
				if (rs.getString("rec_back_code") != null) {
					receiptDetails.setBankCodeForInves(rs.getString("rec_back_code"));
					receiptDetails.setBranchCodeForInves(rs.getString("rec_branch_code"));

					// get bank and branch description
					String query2 = "SELECT b.description as bankDes, c.description as branchDes \r\n"
							+ "FROM public.nt_t_investigation_receipt r\r\n"
							+ "INNER JOIN nt_r_bank b on b.code= r.rec_back_code \r\n"
							+ "INNER JOIN nt_r_branch c on c.code= r.rec_branch_code \r\n"
							+ "WHERE rec_voucher_no=? \r\n" + "LIMIT 1;";
					ps2 = con.prepareStatement(query2);
					ps2.setString(1, voucherNo);
					rs2 = ps2.executeQuery();
					if (rs2.next()) {
						receiptDetails.setBankDesForInves(rs2.getString("bankDes"));
						receiptDetails.setBranchDesForInves(rs2.getString("branchDes"));
					}
				}
				if (rs.getString("rec_is_diposit_to_branch").equals("Y")) {
					receiptDetails.setDepositToBankForInves(true);
				} else if (rs.getString("rec_is_diposit_to_branch").equals("N")) {
					receiptDetails.setDepositToBankForInves(false);
				}
				receiptDetails.setChequeOrBankReceipt(rs.getString("rec_cheque_no"));
				receiptDetails.setReceiptRemarks(rs.getString("rec_remarks"));
				receiptDetails.setReceiptNoForInves(rs.getString("rec_receipt_no"));
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
	public List<ManageInvestigationDTO> getVoucherDetailsListForInves(String chargeReffNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ManageInvestigationDTO> data = new ArrayList<ManageInvestigationDTO>();
		try {

			con = ConnectionManager.getConnection();

		/*	String query = "select  x.charge_ref_no,x.charge_offence,x.charge_attempt,x.charge_amount,x.charge_driver_id,x.charge_conductor_id,\r\n"
					+ "a.charge_permit,a.charge_vehicle,b.ofm_offence_desc,c.description,d.inv_voucher_no,d.inv_account_no,d.inv_charge_amount_tot \r\n"
					+ "from public.nt_t_investigation_charge_sheet x\r\n"
					+ "inner join public.nt_t_investigation_charge_master a on a.charge_ref_no= x.charge_ref_no\r\n"
					+ "inner join public.nt_m_offence_management b on b.ofm_offence_code=x.charge_offence\r\n"
					+ "inner join public.nt_r_attempts c on c.code=x.charge_attempt\r\n"
					+ "inner join public.nt_t_investigation_charge_voucher d on d.inv_charge_ref_no=x.charge_ref_no\r\n"
					+ "where c.active='A' and x.charge_ref_no= ?";*/
			
			String query ="select x.charge_ref_no,x.charge_offence,x.charge_attempt,x.charge_amount,x.charge_driver_id,x.charge_conductor_id,\r\n" + 
					"a.charge_permit,a.charge_vehicle,b.ofm_offence_desc,c.description,d.inv_voucher_no,d.inv_account_no\r\n" + 
					"from public.nt_t_investigation_charge_sheet x\r\n" + 
					"inner join public.nt_t_investigation_charge_master a on a.charge_ref_no= x.charge_ref_no\r\n" + 
					"inner join public.nt_m_offence_management b on b.ofm_offence_code=x.charge_offence\r\n" + 
					"inner join public.nt_r_attempts c on c.code=x.charge_attempt\r\n" + 
					"inner join public.nt_t_investigation_charge_voucher d on d.inv_charge_ref_no=x.charge_ref_no\r\n" + 
					"where c.active='A' and x.charge_ref_no= ?";

			ps = con.prepareStatement(query);
			ps.setString(1, chargeReffNo);
			rs = ps.executeQuery();

			ManageInvestigationDTO p;

			while (rs.next()) {
				p = new ManageInvestigationDTO();
				p.setOffenceDescription(rs.getString("ofm_offence_desc"));
				p.setDriverId(rs.getString("charge_driver_id"));
				p.setConductorId(rs.getString("charge_conductor_id"));
				p.setVouAccount(rs.getString("inv_account_no"));
				p.setVouAmmount(rs.getString("charge_amount"));

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

	@Override
	public void saveReceiptForInvestigation(ManageInvestigationDTO selectedRow, String loginUser) {

		String receiptNo = generateReceiptNoForInvestigation();
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement ps = null;
		try {

			con = ConnectionManager.getConnection();
			long seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_t_investigation_receipt");
			String query = "INSERT INTO public.nt_t_investigation_receipt\r\n" + "(rec_seq_no, rec_charge_ref_no ,\r\n"
					+ "rec_voucher_no, rec_payment_mode, rec_is_diposit_to_branch, rec_receipt_no, rec_back_code,\r\n"
					+ "rec_branch_code, rec_cheque_no, rec_total_fee, rec_remarks, rec_is_receipt_generated, \r\n"
					+ "rep_created_by, rep_created_date)\r\n" + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

			ps = con.prepareStatement(query);

			ps.setLong(1, seqNo);
			ps.setString(2, selectedRow.getInvReferenceNo());

			ps.setString(3, selectedRow.getVoucherNo());
			ps.setString(4, selectedRow.getPaymentModeCodeForGrInves());
			String depositToBank = selectedRow.isDepositToBankForInves() ? "Y" : "N";

			ps.setString(5, depositToBank);
			ps.setString(6, receiptNo);
			ps.setString(7, selectedRow.getBankCodeForInves());
			ps.setString(8, selectedRow.getBranchCodeForInves());
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

	public String generateReceiptNoForInvestigation() {

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

			String sql = "SELECT rec_receipt_no FROM public.nt_t_investigation_receipt ORDER BY rep_created_date desc  LIMIT 1";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("rec_receipt_no");
			}

			if (result != null) {

				lastyr = result.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String ApprecordcountN = "00001";
					strRecNo = "RIV" + currYear + ApprecordcountN;
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
					strRecNo = "RIV" + currYear + ApprecordcountN;
				}
			} else {
				strRecNo = "RIV" + currYear + "00001";
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
	public String getReceiptNoForPrintForInvestigation(String voucherNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String receiptNoForPrint = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "select rec_receipt_no  from  public.nt_t_investigation_receipt  where rec_voucher_no =?;";

			ps = con.prepareStatement(query);
			ps.setString(1, voucherNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				receiptNoForPrint = rs.getString("rec_receipt_no");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return receiptNoForPrint;
	}

	@Override
	public boolean updateVoucherStatus(ManageInvestigationDTO selectedInvestigation, String refNo, String status, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		ResultSet rs = null;
		boolean success = false;
		String vouNo = null;
		
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		try {
			con = ConnectionManager.getConnection();
			String query = "";

			String query1 = "";

			query1 = "select inv_voucher_no  from  public.nt_t_investigation_charge_voucher  where  inv_charge_ref_no =? and inv_voucher_approved not in('C') ;";

			ps1 = con.prepareStatement(query1);
			ps1.setString(1, refNo);
			rs = ps1.executeQuery();

			while (rs.next()) {
				vouNo = rs.getString("inv_voucher_no");
			}

			query = "UPDATE public.nt_t_investigation_charge_voucher " + "SET  inv_voucher_approved  = ? "
					+ "WHERE inv_voucher_no  = ? ";

			ps = con.prepareStatement(query);

			ps.setString(1, status);
			ps.setString(2, vouNo);

			ps.executeUpdate();

			con.commit();
			
			success = true;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return success;
	}

	@Override
	public List<ManageInvestigationDTO> getInvestigationChargesByRefAndAttempt(String chargeRefNo, String attempt) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ManageInvestigationDTO> chargesList = new ArrayList<ManageInvestigationDTO>();
		String query;

		try {
			con = ConnectionManager.getConnection();

			if (chargeRefNo != null) {

				query = "SELECT A.vio_condition_cd, B.ofm_offence_desc, C.omd_charge_amount, A.remark, C.omd_attempt_code, D.description "
						+ "FROM public.nt_t_flying_vio_conditions A, nt_m_offence_management B, nt_t_offence_management_det C, nt_r_attempts D "
						+ "WHERE A.vio_condition_cd = B.ofm_offence_code "
						+ "AND A.vio_condition_cd = C.omd_offence_code "
						+ "AND B.ofm_offence_code = C.omd_offence_code " + "AND C.omd_attempt_code = D.code "
						+ "AND A.vio_invesno = ? "
						/* + "AND C.omd_is_investigation = 'Y'" */
						+ "AND C.omd_attempt_code = ? "
						+ "AND A.vio_violated_status = 'true' ";

				query = query + " order by vio_condition_cd ";

				ps = con.prepareStatement(query);

				ps.setString(1, chargeRefNo);
				ps.setString(2, attempt);
				rs = ps.executeQuery();

				while (rs.next()) {
					ManageInvestigationDTO dto = new ManageInvestigationDTO();

					dto.setChargeCode(rs.getString("vio_condition_cd"));
					dto.setChargeDesc(rs.getString("ofm_offence_desc"));
					dto.setAmount(rs.getBigDecimal("omd_charge_amount"));
					dto.setAttemptCode(rs.getString("omd_attempt_code"));
					dto.setAttemptDesc(rs.getString("description"));

					chargesList.add(dto);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return chargesList;
	
	}

	@Override
	public BigDecimal getAmountPerAttempt(String offenceCode,String attemptCode) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		BigDecimal ammouont = new BigDecimal(0);

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT omd_charge_amount " 
					+ "FROM public.nt_t_offence_management_det " 
					+ "inner join public.nt_r_attempts on  public.nt_r_attempts.code=omd_attempt_code " 
					+ "where omd_offence_code=? " 
					+ "and public.nt_r_attempts.active='A' "
					+ "and omd_attempt_code =? " 
					+ "order by omd_seq; ";

			ps = con.prepareStatement(query);
			ps.setString(1, offenceCode);
			ps.setString(2, attemptCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				ammouont = rs.getBigDecimal("omd_charge_amount");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return ammouont;
	
	}

	@Override
	public BigDecimal getLatePaymentFeeByVoucherNumer(String voucherNumber) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		BigDecimal lateFee = null;

		try {

			con = ConnectionManager.getConnection();

			String query = "select charge_late_payment_amount  from public.nt_t_investigation_charge_master m\r\n" + 
					"inner join public.nt_t_investigation_charge_voucher v on v.inv_charge_ref_no =m.charge_ref_no \r\n" + 
					"where v.inv_voucher_no =?";

			ps = con.prepareStatement(query);
			ps.setString(1, voucherNumber);

			rs = ps.executeQuery();

			while (rs.next()) {
				lateFee = rs.getBigDecimal("charge_late_payment_amount");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return lateFee;
	
	}
	
	
	public void insertTaskInquiryRecord(Connection con, ManageInvestigationDTO selectedInvestigation,
			Timestamp timestamp, String status, String function,String functionDes) {
		
		try {
			String insertSQL = null;
			PreparedStatement psInsert = null;

			insertSQL = "INSERT INTO public.nt_t_task_inquiry (created_by, created_date, status, bus_no, ref_no, permit_no, function_name,function_des) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

			psInsert = con.prepareStatement(insertSQL);

			psInsert.setString(1, selectedInvestigation.getLoginUser());
			psInsert.setTimestamp(2, timestamp);
			psInsert.setString(3, status);
			psInsert.setString(4, selectedInvestigation.getVehicleNo());
			psInsert.setString(5, selectedInvestigation.getInvReferenceNo());
			psInsert.setString(6, selectedInvestigation.getPermitNo());
			psInsert.setString(7, function);
			psInsert.setString(8, functionDes);

			psInsert.executeUpdate();
			con.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void beanLinkMethod(ManageInvestigationDTO selectedInvestigation,String des) {
		Connection con = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		
		try {
			con = ConnectionManager.getConnection();
			
			insertTaskInquiryRecord(con, selectedInvestigation, timestamp, des, "Investigation Management","Manage Investigation / Charges");
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	@Override
	public List<ManageInvestigationDTO> searchDataForCommonInquiry(String refNo, String busNo, String permitNo, String offence) {
		Connection con = null;
		PreparedStatement ps= null;
		ResultSet rs = null;
		List<ManageInvestigationDTO> dataList = new ArrayList<ManageInvestigationDTO>();
		
		try {
			con = ConnectionManager.getConnection();

			if((refNo != null && busNo != "" && permitNo != "")) {
				String sql = null;
				sql = "SELECT * FROM public.nt_t_task_inquiry WHERE function_name = 'Investigation Management' "
						+ "AND (ref_no = ? AND bus_no = ? AND permit_no = ?)";
				
				ps = con.prepareStatement(sql);
				ps.setString(1, refNo);
				ps.setString(2, busNo);
				ps.setString(3, permitNo);
				
				rs = ps.executeQuery();
				
				while(rs.next()) {
					ManageInvestigationDTO data = new ManageInvestigationDTO();
					
					data.setPermitNo(rs.getString("permit_no"));
					data.setVehicleNo(rs.getString("bus_no"));
					data.setInvReferenceNo(rs.getString("ref_no"));
					data.setActionDesc(rs.getString("status"));
					data.setCreatedBy(rs.getString("created_by"));
					data.setCreatedDate(rs.getTimestamp("created_date"));
					data.setDepartmentType(rs.getString("function_des"));
					
					dataList.add(data);
				}
			}else if((refNo != null && busNo != "")){
				String sql = null;
				sql = "SELECT * FROM public.nt_t_task_inquiry WHERE function_name = 'Investigation Management' "
						+ "AND (ref_no = ? AND bus_no = ?)";
				
				ps = con.prepareStatement(sql);
				ps.setString(1, refNo);
				ps.setString(2, busNo);
				
				rs = ps.executeQuery();
				
				while(rs.next()) {
					ManageInvestigationDTO data = new ManageInvestigationDTO();
					
					data.setPermitNo(rs.getString("permit_no"));
					data.setVehicleNo(rs.getString("bus_no"));
					data.setInvReferenceNo(rs.getString("ref_no"));
					data.setActionDesc(rs.getString("status"));
					data.setCreatedBy(rs.getString("created_by"));
					data.setCreatedDate(rs.getTimestamp("created_date"));
					data.setDepartmentType(rs.getString("function_des"));
					
					dataList.add(data);
				}
				
			}else if((refNo != null && permitNo != "")) {
				String sql = null;
				sql = "SELECT * FROM public.nt_t_task_inquiry WHERE function_name = 'Investigation Management' "
						+ "AND (ref_no = ? AND permit_no = ?)";
				
				ps = con.prepareStatement(sql);
				ps.setString(1, refNo);
				ps.setString(2, permitNo);
				
				rs = ps.executeQuery();
				
				while(rs.next()) {
					ManageInvestigationDTO data = new ManageInvestigationDTO();
					
					data.setPermitNo(rs.getString("permit_no"));
					data.setVehicleNo(rs.getString("bus_no"));
					data.setInvReferenceNo(rs.getString("ref_no"));
					data.setActionDesc(rs.getString("status"));
					data.setCreatedBy(rs.getString("created_by"));
					data.setCreatedDate(rs.getTimestamp("created_date"));
					data.setDepartmentType(rs.getString("function_des"));
					
					dataList.add(data);
				}
			}else {
				String sql = null;
				sql = "SELECT * FROM public.nt_t_task_inquiry WHERE function_name = 'Investigation Management' "
						+ "AND (ref_no = ? OR bus_no = ? OR permit_no = ? OR ofm_offence_code = ?)";
				
				ps = con.prepareStatement(sql);
				ps.setString(1, refNo);
				ps.setString(2, busNo);
				ps.setString(3, permitNo);
				ps.setString(4, offence);
				
				rs = ps.executeQuery();
				
				while(rs.next()) {
					ManageInvestigationDTO data = new ManageInvestigationDTO();
					
					data.setPermitNo(rs.getString("permit_no"));
					data.setVehicleNo(rs.getString("bus_no"));
					data.setInvReferenceNo(rs.getString("ref_no"));
					data.setActionDesc(rs.getString("status"));
					data.setCreatedBy(rs.getString("created_by"));
					data.setCreatedDate(rs.getTimestamp("created_date"));
					data.setDepartmentType(rs.getString("function_des"));
					data.setOffenceName(rs.getString("ofm_offence_code"));
					
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
	public List<ManageInvestigationDTO> getRefNoListForCommonInquiry(){
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ManageInvestigationDTO> dataList = new ArrayList<ManageInvestigationDTO>();
		
		try {
			con = ConnectionManager.getConnection();
			
			String sql = null;
			sql = "SELECT DISTINCT ref_no FROM public.nt_t_task_inquiry WHERE function_name = 'Investigation Management'";
			
			ps = con.prepareStatement(sql);
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				ManageInvestigationDTO data = new ManageInvestigationDTO();
				
				if(rs.getString("ref_no") != null && !rs.getString("ref_no").isEmpty()) {
						data.setInvReferenceNo(rs.getString("ref_no"));
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
	public List<ManageInvestigationDTO> getBusNoListForCommonInquiry(){
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ManageInvestigationDTO> dataList = new ArrayList<ManageInvestigationDTO>();
		
		try {
			con = ConnectionManager.getConnection();
			
			String sql = null;
			sql = "SELECT DISTINCT bus_no FROM public.nt_t_task_inquiry WHERE function_name = 'Investigation Management'";
			
			ps = con.prepareStatement(sql);
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				ManageInvestigationDTO data = new ManageInvestigationDTO();
				if(rs.getString("bus_no") != null && !rs.getString("bus_no").isEmpty()) {
					data.setVehicleNo(rs.getString("bus_no"));
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
	public List<ManageInvestigationDTO> getPermitNoListForCommonInquiry(){
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ManageInvestigationDTO> dataList = new ArrayList<ManageInvestigationDTO>();
		
		try {
			con = ConnectionManager.getConnection();
			
			String sql = null;
			sql = "SELECT DISTINCT permit_no FROM public.nt_t_task_inquiry WHERE function_name = 'Investigation Management'";
			
			ps = con.prepareStatement(sql);
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				ManageInvestigationDTO data = new ManageInvestigationDTO();
				if(rs.getString("permit_no") != null && !rs.getString("permit_no").isEmpty()) {
					data.setPermitNo(rs.getString("permit_no"));
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
	public List<ManageInvestigationDTO> getOffNameListForCommonInquiry(){
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ManageInvestigationDTO> dataList = new ArrayList<ManageInvestigationDTO>();
		
		try {
			con = ConnectionManager.getConnection();
			
			String sql = null;
			sql = "SELECT DISTINCT ofm_offence_code FROM public.nt_t_task_inquiry WHERE function_name = 'Investigation Management'";
			
			ps = con.prepareStatement(sql);
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				ManageInvestigationDTO data = new ManageInvestigationDTO();
				if(rs.getString("ofm_offence_code") != null && !rs.getString("ofm_offence_code").isEmpty()) {
					data.setOffenceName(rs.getString("ofm_offence_code"));
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
	public List<ComplaintRequestDTO> getComplaintHistoryByOwner(String vehicleNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ComplaintRequestDTO> complaintDTOList = new ArrayList<ComplaintRequestDTO>();

		try {
			con = ConnectionManager.getConnection();

//			String query = "SELECT c.seq, c.complain_no, c.severity_no, c.event_datetime, c.other_commited_offences, c.details_of_complain, NULL, NULL  "
//					+ " FROM public.nt_m_complain_request c "
//					+ " WHERE c.vehicle_no = ? AND c.complain_no NOT IN (SELECT d.complain_ref_no FROM nt_t_action_owner_det d) "
//					+ " UNION "
//					+ " SELECT c.seq, c.complain_no, c.severity_no, c.event_datetime, c.other_commited_offences, c.details_of_complain, d.action_type_code, d.action_desc  "
//					+ " FROM nt_m_complain_request c, nt_t_action_owner_det d " + " WHERE c.vehicle_no = ? "
//					+ " AND c.complain_no = d.complain_ref_no " + " ORDER BY complain_no";
			
			String query = "SELECT inv_det_report_no, inv_det_time\r\n" + 
					"FROM public.nt_t_flying_investigation_log_det\r\n" + 
					"WHERE inv_det_vehicle_no = ?";
					

			ps = con.prepareStatement(query);
			ps.setString(1, vehicleNo);
			//ps.setString(2, vehicleNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				ComplaintRequestDTO dto = new ComplaintRequestDTO();
				//dto.setComplainSeq(rs.getLong("seq"));
				dto.setComplaintNo(rs.getString("inv_det_report_no"));
				//dto.setSeverityNo(rs.getString("severity_no"));
				dto.setEventDateTime(rs.getString("inv_det_time"));
				//dto.setOtherOffence(rs.getString("other_commited_offences"));
				//dto.setDetailOfComplain(rs.getString("details_of_complain"));

				//dto.setActionCode(rs.getString(7));
				//dto.setActionDesc(rs.getString(8));
				complaintDTOList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return complaintDTOList;
	}

	@Override
	public List<ComplaintRequestDTO> getComplaintHistoryByDriver(String driverId, String driverNic) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ComplaintRequestDTO> complaintDTOList = new ArrayList<ComplaintRequestDTO>();
		//System.out.println(driverId);
		if(driverId != null) {
			try {
				con = ConnectionManager.getConnection();
				String query = "";

				    query = "SELECT inv_det_report_no, inv_det_time\r\n" + 
							"FROM public.nt_t_flying_investigation_log_det\r\n" + 
							"WHERE inv_det_driver_id = ?";

				ps = con.prepareStatement(query);
				ps.setString(1, driverId);

				rs = ps.executeQuery();

				while (rs.next()) {
					ComplaintRequestDTO dto = new ComplaintRequestDTO();
					dto.setComplaintNo(rs.getString("inv_det_report_no"));
					dto.setEventDateTime(rs.getString("inv_det_time"));

					complaintDTOList.add(dto);
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				ConnectionManager.close(ps);
				ConnectionManager.close(rs);
				ConnectionManager.close(con);
			}
		}

		
		return complaintDTOList;
	}

	@Override
	public List<ComplaintRequestDTO> getComplaintHistoryByConductor(String conductorId, String conductorNic) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ComplaintRequestDTO> complaintDTOList = new ArrayList<ComplaintRequestDTO>();
		
		if(conductorId != null) {
			try {
				con = ConnectionManager.getConnection();
				String query = "";

				query = "SELECT inv_det_report_no, inv_det_time\r\n" + "FROM public.nt_t_flying_investigation_log_det\r\n"
						+ "WHERE inv_det_conductor_id = ?";

				ps = con.prepareStatement(query);
				ps.setString(1, conductorId);
				rs = ps.executeQuery();

				while (rs.next()) {
					ComplaintRequestDTO dto = new ComplaintRequestDTO();
					dto.setComplaintNo(rs.getString("inv_det_report_no"));
					dto.setEventDateTime(rs.getString("inv_det_time"));

					complaintDTOList.add(dto);
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				ConnectionManager.close(ps);
				ConnectionManager.close(rs);
				ConnectionManager.close(con);
			}
		}

		
		return complaintDTOList;
	}

	@Override
	public BigDecimal getOffenceCharge(String offenceCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		BigDecimal amount = new BigDecimal(0);
		int attempt_count = 0;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM nt_t_offence_management_det  "
					+ "WHERE omd_is_public_complain = 'Y' AND  omd_is_amount = 'Y' AND  omd_offence_code = ? ";

			// TODO

			if (attempt_count >= 3) {
				query += "AND omd_attempt_code = 'TT';";
			} else if (attempt_count == 2) {
				query += "AND omd_attempt_code = 'ST';";
			} else {
				query += "AND omd_attempt_code = 'FT';";
			}

			ps = con.prepareStatement(query);
			ps.setString(1, offenceCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				amount = rs.getBigDecimal("omd_charge_amount");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return amount;
	}

	public List<CommittedOffencesDTO> getCommittedOffencesById(Long complainSeq, String driverId, String conductorId) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CommittedOffencesDTO> commitedDTOList = new ArrayList<CommittedOffencesDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM nt_t_driver_conductor_points_det a, nt_t_offence_charge_sheet b, nt_t_grievance_commited_offence c "
					+ "WHERE a.p_complain_no = b.chr_complaint_no " + "AND a.p_offence_code = b.chr_offence_code "
					+ "AND a.p_complain_no IN (SELECT complain_no FROM nt_m_complain_request WHERE seq = c.complain_seq) "
					+ "AND a.p_offence_code = c.offence_type_code "
					+ "AND a.p_complain_no IN (SELECT complain_no FROM nt_m_complain_request WHERE seq = ?) ";

			if (driverId != null)
				query += " AND a.p_driver_id = ?;";
			else
				query += " AND a.p_conductor_id = ?;";

			ps = con.prepareStatement(query);
			ps.setLong(1, complainSeq);
			if (driverId != null)
				ps.setString(2, driverId);
			else
				ps.setString(2, conductorId);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommittedOffencesDTO dto = new CommittedOffencesDTO();
				dto.setCode(rs.getString("p_offence_code"));
				dto.setDescription(rs.getString("chr_offence_desc"));
				dto.setRemark(rs.getString("remarks"));
				dto.setChr_seq(rs.getLong("chr_seq"));
				dto.setCharge(rs.getBigDecimal("chr_charge_amount"));
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
	
	@Override
	public List<CommittedOffencesDTO> getViolationHistory(String reportNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CommittedOffencesDTO> commitedDTOList = new ArrayList<CommittedOffencesDTO>();

		try {
			con = ConnectionManager.getConnection();

//			String query ="SELECT * FROM  public.nt_t_flying_vio_conditions "+
//						  "WHERE vio_report_no = ?";
			
			String query = "SELECT vio_condition_cd, remark, ofm_offence_desc " +
					"FROM public.nt_t_flying_vio_conditions, nt_m_offence_management " +
					"WHERE  vio_report_no = ? " +
					"AND nt_t_flying_vio_conditions.vio_condition_cd = nt_m_offence_management.ofm_offence_code " +
					"AND vio_violated_status = 'true'";


			ps = con.prepareStatement(query);
			ps.setString(1, reportNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				CommittedOffencesDTO dto = new CommittedOffencesDTO();
				dto.setCode(rs.getString("vio_condition_cd"));
				dto.setDescription(rs.getString("ofm_offence_desc"));
				dto.setRemark(rs.getString("remark"));
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
	
	
}
