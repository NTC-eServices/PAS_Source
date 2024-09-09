package lk.informatics.ntc.model.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.informatics.ntc.model.dto.ManageInvestigationDTO;
import lk.informatics.ntc.view.util.ConnectionManager;

public class ApproveActionChargesServiceImpl implements ApproveActionChargesService {

	@Override
	public List<ManageInvestigationDTO> getChargeRefNoList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ManageInvestigationDTO> chargeRefNoList = new ArrayList<ManageInvestigationDTO>();
		String query;

		try {
			con = ConnectionManager.getConnection();

			query = "SELECT charge_ref_no " + "FROM nt_t_investigation_charge_master "
					+ "WHERE charge_app_status IN ('O', 'A', 'R') " + "ORDER BY charge_ref_no asc ";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				ManageInvestigationDTO dto = new ManageInvestigationDTO();

				dto.setChargeRefCode(rs.getString("charge_ref_no"));

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
				query = "SELECT inv_det_ref_no, inv_det_report_no, "
						+ " inv_det_route_no, inv_det_route_from, inv_det_route_to, "
						+ " inv_det_service_cd, to_char(inv_det_time, 'dd/MM/yyyy') as inv_det_time, inv_det_fault, inv_det_documents, "
						+ " inv_det_invest_place, inv_det_remark, " + " D.driver_id, D.driver_name, D.driver_nic,"
						+ " D.conductor_id, D.conductor_name, D.conductor_nic, " + " inv_permit_no, inv_permit_owner, "
						+ " inv_investigation_no, inv_det_vehicle_no, " + " inv_det_created_by, inv_det_created_date, "
						+ " inv_det_modified_by, inv_modified_date, "
						+ " inv_det_delete, inv_det_delete_by, inv_det_delete_date, B.charge_app_current_status, B.charge_tot_amount, C.inv_status_desc, B.charge_special_remark "
						+ " FROM public.nt_t_flying_investigation_log_det A, nt_t_investigation_charge_master B, nt_r_investigation_status C, nt_t_investigation_driver_conductor_det D "
						+ " WHERE A.inv_investigation_no = B.charge_ref_no "
						+ " AND B.charge_app_current_status = C.inv_status_code "
						+ " AND A.inv_investigation_no = D.charge_ref_no "
						+ " AND B.charge_app_status IN ('O', 'A', 'R') " + " AND inv_det_delete is null "
						+ " AND inv_det_time >= DATE(?) " + " AND inv_det_time <= DATE(?) ";

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

					dto.setDriverId(rs.getString("driver_id"));
					dto.setDriverName(rs.getString("driver_name"));
					dto.setDriverNIC(rs.getString("driver_nic"));

					dto.setConductorId(rs.getString("conductor_id"));
					dto.setConductorName(rs.getString("conductor_name"));
					dto.setConductorNIC(rs.getString("conductor_nic"));

					dto.setPermitNo(rs.getString("inv_permit_no"));
					dto.setPermitOwnerName(rs.getString("inv_permit_owner"));

					dto.setChargeRefCode(rs.getString("inv_investigation_no"));
					dto.setVehicleNo(rs.getString("inv_det_vehicle_no"));
					dto.setCurrentStatus(rs.getString("charge_app_current_status"));
					dto.setFinalAmount(rs.getBigDecimal("charge_tot_amount"));
					dto.setCurrentStatusDesc(rs.getString("inv_status_desc"));
					dto.setSpecialRemark(rs.getString("charge_special_remark"));
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

			query = "SELECT inv_det_ref_no, inv_det_report_no, "
					+ "inv_det_route_no, inv_det_route_from, inv_det_route_to, "
					+ "inv_det_service_cd, to_char(inv_det_time, 'dd/MM/yyyy') as inv_det_time, inv_det_fault, inv_det_documents, "
					+ "inv_det_invest_place, inv_det_remark, " + "D.driver_id, D.driver_name, D.driver_nic,"
					+ "D.conductor_id, D.conductor_name, D.conductor_nic, " + "inv_permit_no, inv_permit_owner, "
					+ "inv_investigation_no, inv_det_vehicle_no, " + "inv_det_created_by, inv_det_created_date, "
					+ "inv_det_modified_by, inv_modified_date, "
					+ "inv_det_delete, inv_det_delete_by, inv_det_delete_date, B.charge_app_current_status, B.charge_tot_amount, C.inv_status_desc, B.charge_special_remark "
					+ "FROM public.nt_t_flying_investigation_log_det A, nt_t_investigation_charge_master B, nt_r_investigation_status C, nt_t_investigation_driver_conductor_det D "
					+ "WHERE A.inv_investigation_no = B.charge_ref_no "
					+ "AND B.charge_app_current_status = C.inv_status_code "
					+ "AND A.inv_investigation_no = D.charge_ref_no " + "AND B.charge_app_status IN ('O', 'A', 'R') "
					+ "AND inv_det_delete is null ";

			String chargeRef = null;
			String reportNo = null;
			String vehicleNo = null;
			String permitNo = null;
			String permitOwner = null;

			if (searchDTO.getChargeRefCode() != null && !searchDTO.getChargeRefCode().trim().isEmpty()) {
				chargeRef = searchDTO.getChargeRefCode();
				query = query + " and inv_investigation_no = ? order by inv_det_created_date ";
				ps = con.prepareStatement(query);
				ps.setString(1, chargeRef);

			} else if (searchDTO.getReportNo() != null && !searchDTO.getReportNo().trim().isEmpty()) {
				reportNo = searchDTO.getReportNo();
				query = query + " and inv_det_report_no = ? order by inv_det_created_date ";
				ps = con.prepareStatement(query);
				ps.setString(1, reportNo);
			} else if (searchDTO.getVehicleNo() != null && !searchDTO.getVehicleNo().trim().isEmpty()) {
				vehicleNo = searchDTO.getVehicleNo();
				query = query + " and inv_det_vehicle_no = ? order by inv_det_created_date ";
				ps = con.prepareStatement(query);
				ps.setString(1, vehicleNo);
			} else if (searchDTO.getPermitNo() != null && !searchDTO.getPermitNo().trim().isEmpty()) {
				permitNo = searchDTO.getPermitNo();
				query = query + " and inv_permit_no = ?order by inv_det_created_date ";
				ps = con.prepareStatement(query);
				ps.setString(1, permitNo);
			} else if (searchDTO.getPermitOwnerName() != null && !searchDTO.getPermitOwnerName().trim().isEmpty()) {
				permitOwner = searchDTO.getPermitOwnerName();
				query = query + " and inv_permit_owner = ? order by inv_det_created_date ";
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

				dto.setDriverId(rs.getString("driver_id"));
				dto.setDriverName(rs.getString("driver_name"));
				dto.setDriverNIC(rs.getString("driver_nic"));

				dto.setConductorId(rs.getString("conductor_id"));
				dto.setConductorName(rs.getString("conductor_name"));
				dto.setConductorNIC(rs.getString("conductor_nic"));

				dto.setPermitNo(rs.getString("inv_permit_no"));
				dto.setPermitOwnerName(rs.getString("inv_permit_owner"));

				dto.setChargeRefCode(rs.getString("inv_investigation_no"));
				dto.setVehicleNo(rs.getString("inv_det_vehicle_no"));
				dto.setCurrentStatus(rs.getString("charge_app_current_status"));
				dto.setFinalAmount(rs.getBigDecimal("charge_tot_amount"));
				dto.setCurrentStatusDesc(rs.getString("inv_status_desc"));
				dto.setSpecialRemark(rs.getString("charge_special_remark"));
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
	public List<ManageInvestigationDTO> searchSavedSuspensionsDetails() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ManageInvestigationDTO> investigationList = new ArrayList<ManageInvestigationDTO>();
		String query;

		try {
			con = ConnectionManager.getConnection();

			query = "SELECT inv_det_ref_no, inv_det_report_no, "
					+ "inv_det_route_no, inv_det_route_from, inv_det_route_to, "
					+ "inv_det_service_cd, to_char(inv_det_time, 'dd/MM/yyyy') as inv_det_time, inv_det_fault, inv_det_documents, "
					+ "inv_det_invest_place, inv_det_remark, " + "D.driver_id, D.driver_name, D.driver_nic,"
					+ "D.conductor_id, D.conductor_name, D.conductor_nic, " + "inv_permit_no, inv_permit_owner, "
					+ "inv_investigation_no, inv_det_vehicle_no, " + "inv_det_created_by, inv_det_created_date, "
					+ "inv_det_modified_by, inv_modified_date, "
					+ "inv_det_delete, inv_det_delete_by, inv_det_delete_date, B.charge_app_current_status, B.charge_tot_amount, C.inv_status_desc, B.charge_special_remark "
					+ "FROM public.nt_t_flying_investigation_log_det A, nt_t_investigation_charge_master B, nt_r_investigation_status C,nt_t_investigation_driver_conductor_det D "
					+ "WHERE A.inv_investigation_no = B.charge_ref_no "
					+ "AND B.charge_app_current_status = C.inv_status_code "
					+ "AND A.inv_investigation_no = D.charge_ref_no " + "AND B.charge_app_status IN ('O') "
					+ "AND B.charge_app_current_status IN ('CS', 'CA') " + "AND inv_det_delete is null ";

			ps = con.prepareStatement(query);

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

				dto.setDriverId(rs.getString("driver_id"));
				dto.setDriverName(rs.getString("driver_name"));
				dto.setDriverNIC(rs.getString("driver_nic"));

				dto.setConductorId(rs.getString("conductor_id"));
				dto.setConductorName(rs.getString("conductor_name"));
				dto.setConductorNIC(rs.getString("conductor_nic"));

				dto.setPermitNo(rs.getString("inv_permit_no"));
				dto.setPermitOwnerName(rs.getString("inv_permit_owner"));

				dto.setChargeRefCode(rs.getString("inv_investigation_no"));
				dto.setVehicleNo(rs.getString("inv_det_vehicle_no"));

				dto.setCurrentStatus(rs.getString("charge_app_current_status"));
				dto.setFinalAmount(rs.getBigDecimal("charge_tot_amount"));
				dto.setCurrentStatusDesc(rs.getString("inv_status_desc"));
				dto.setSpecialRemark(rs.getString("charge_special_remark"));

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
	public List<ManageInvestigationDTO> getChargeSheetByChargeRef(String chargeRef) {
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
	public boolean approveCharges(ManageInvestigationDTO actionChargesDTO, String chargeRef, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		boolean success = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();
			String query = "";

			query = "UPDATE public.nt_t_investigation_charge_master " + "SET charge_app_current_status = 'CA', "
					+ "modified_by = ?, " + "modified_date = ? " + "WHERE charge_ref_no = ? ";

			ps = con.prepareStatement(query);

			ps.setString(1, user);
			ps.setTimestamp(2, timestamp);
			ps.setString(3, chargeRef);

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
	public boolean rejectCharges(ManageInvestigationDTO actionChargesDTO,String chargeRef, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		boolean success = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();
			String query = "";

			query = "UPDATE public.nt_t_investigation_charge_master " + "SET charge_app_current_status = 'CR', "
					+ "modified_by = ?, " + "modified_date = ? " + "WHERE charge_ref_no = ? ";

			ps = con.prepareStatement(query);

			ps.setString(1, user);
			ps.setTimestamp(2, timestamp);
			ps.setString(3, chargeRef);

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
	public BigDecimal getDemeritPoints(String offenceCode, String attemptCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		BigDecimal demeritPoints = null;

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT omd_no_of_demerit_points FROM public.nt_t_offence_management_det "
					+ "WHERE omd_offence_code = ? AND omd_attempt_code = ? ";

			ps = con.prepareStatement(query);
			ps.setString(1, offenceCode);
			ps.setString(2, attemptCode);

			rs = ps.executeQuery();

			while (rs.next()) {
				demeritPoints = rs.getBigDecimal("omd_no_of_demerit_points");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return demeritPoints;
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
	public BigDecimal getLatePaymentFee(String chargeRefNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		BigDecimal lateFee = null;

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT  charge_late_payment_amount  FROM public.nt_t_investigation_charge_master x\r\n" + 
					"WHERE charge_ref_no =? ";

			ps = con.prepareStatement(query);
			ps.setString(1, chargeRefNo);

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

	@Override
	public BigDecimal getTotalAmount(String chargeRefNo) {


		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		BigDecimal totFee = null;

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT   charge_tot_amount  FROM public.nt_t_investigation_charge_master x\r\n" + 
					"WHERE charge_ref_no =? ";

			ps = con.prepareStatement(query);
			ps.setString(1, chargeRefNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				totFee = rs.getBigDecimal("charge_tot_amount");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return totFee;
	
	
	}

	private void insertTaskInquiryRecord(Connection con, ManageInvestigationDTO actionChargesDTO,
			Timestamp timestamp, String status, String function,String functiondes) {
		
		try {
			String insertSQL = null;
			PreparedStatement psInsert = null;

			insertSQL = "INSERT INTO public.nt_t_task_inquiry (created_by, created_date, status, bus_no, ref_no, permit_no,function_name, function_des) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

			psInsert = con.prepareStatement(insertSQL);

			psInsert.setString(1, actionChargesDTO.getLoginUser());
			psInsert.setTimestamp(2, timestamp);
			psInsert.setString(3, status);
			psInsert.setString(4, actionChargesDTO.getVehicleNo());
			psInsert.setString(5, actionChargesDTO.getInvReferenceNo());
			psInsert.setString(6, actionChargesDTO.getPermitNo());
			psInsert.setString(7, function);
			psInsert.setString(8, functiondes);

			psInsert.executeUpdate();
			con.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public void beanLinkMethod(ManageInvestigationDTO actionChargesDTO,String des) {
		Connection con = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		
		try {
			con = ConnectionManager.getConnection();
			
			insertTaskInquiryRecord(con, actionChargesDTO, timestamp, des, "Investigation Management","Approve Action / Charges");
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
}
