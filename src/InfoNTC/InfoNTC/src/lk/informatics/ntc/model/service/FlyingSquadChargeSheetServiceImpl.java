package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import lk.informatics.ntc.model.dto.FluingSquadVioConditionDTO;
import lk.informatics.ntc.model.dto.FlyingManageInvestigationLogDTO;
import lk.informatics.ntc.model.dto.FlyingSquadVioDocumentsDTO;
import lk.informatics.ntc.view.util.ConnectionManager;

public class FlyingSquadChargeSheetServiceImpl implements FlyingSquadChargeSheetService {

	public FlyingManageInvestigationLogDTO getmasterDetails(String invesNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();

		try {
			con = ConnectionManager.getConnection();
			String sql = null;

			sql = "SELECT inv_det_ref_no, inv_det_report_no, inv_det_route_no, inv_det_route_from, inv_det_route_to, inv_det_service_cd,\r\n"
					+ "inv_det_driver_id, inv_det_driver_name, inv_det_conductor_id, inv_det_conductor_name, inv_permit_no,description,\r\n"
					+ "inv_permit_owner ,inv_det_vehicle_no\r\n"
					+ "FROM public.nt_t_flying_investigation_log_det ,nt_r_service_types\r\n"
					+ "where inv_investigation_no = ? \r\n"
					+ "and nt_t_flying_investigation_log_det.inv_det_service_cd=nt_r_service_types.code";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, invesNo);
			rs = stmt.executeQuery();

			while (rs.next()) {

				flyingManageInvestigationLogDTO.setRefNo(rs.getString("inv_det_ref_no"));
				flyingManageInvestigationLogDTO.setReportNo(rs.getString("inv_det_report_no"));
				flyingManageInvestigationLogDTO.setRouteNo(rs.getString("inv_det_route_no"));
				flyingManageInvestigationLogDTO.setRouteFrom(rs.getString("inv_det_route_from"));
				flyingManageInvestigationLogDTO.setRouteTo(rs.getString("inv_det_route_to"));
				flyingManageInvestigationLogDTO.setServiceTypeCd(rs.getString("inv_det_service_cd"));
				flyingManageInvestigationLogDTO.setServiceTypeDes(rs.getString("description"));
				flyingManageInvestigationLogDTO.setDriverID(rs.getString("inv_det_driver_id"));
				flyingManageInvestigationLogDTO.setDriverName(rs.getString("inv_det_driver_name"));
				flyingManageInvestigationLogDTO.setConductorID(rs.getString("inv_det_conductor_id"));
				flyingManageInvestigationLogDTO.setConductName(rs.getString("inv_det_conductor_name"));
				flyingManageInvestigationLogDTO.setPermitNo(rs.getString("inv_permit_no"));
				flyingManageInvestigationLogDTO.setPermitowner(rs.getString("inv_permit_owner"));
				flyingManageInvestigationLogDTO.setBusNo(rs.getString("inv_det_vehicle_no"));

				String permitno = null;
				permitno = flyingManageInvestigationLogDTO.getPermitNo();
				if (permitno == null || permitno.equalsIgnoreCase("") || permitno.isEmpty()) {
					FlyingManageInvestigationLogDTO flyingManageInvestigationvehicleLogDTO = new FlyingManageInvestigationLogDTO();
					flyingManageInvestigationvehicleLogDTO = getVehicleDetails(
							flyingManageInvestigationLogDTO.getBusNo());
					flyingManageInvestigationLogDTO.setPermitNo(flyingManageInvestigationvehicleLogDTO.getPermitNo());
					flyingManageInvestigationLogDTO
							.setPermitowner(flyingManageInvestigationvehicleLogDTO.getPermitowner());
				}

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return flyingManageInvestigationLogDTO;

	}

	// get vehicle No
	public FlyingManageInvestigationLogDTO getVehicleDetails(String vehicleno) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();

		try {
			con = ConnectionManager.getConnection();
			String sql = null;

			sql = "SELECT pm_permit_no,pmo_full_name\r\n" + "FROM public.nt_t_pm_application,nt_t_pm_vehi_owner\r\n"
					+ "where pm_vehicle_regno =?\r\n" + "and pm_permit_no = pmo_permit_no limit 1";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, vehicleno);
			rs = stmt.executeQuery();

			while (rs.next()) {

				flyingManageInvestigationLogDTO.setPermitNo(rs.getString("pm_permit_no"));
				flyingManageInvestigationLogDTO.setPermitowner(rs.getString("pmo_full_name"));

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return flyingManageInvestigationLogDTO;

	}

	// get driverlist
	public ArrayList<FlyingManageInvestigationLogDTO> getdriverList() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		ArrayList<FlyingManageInvestigationLogDTO> DriverList = new ArrayList<FlyingManageInvestigationLogDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT dcr_driver_conductor_id\r\n" + "FROM public.nt_t_driver_conductor_registration\r\n"
					+ "where dcr_driver_conductor_id like '%D%'\r\n" + "and  dcr_status in('TA','A')";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {

				FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
				flyingManageInvestigationLogDTO.setDriverID(rs.getString("dcr_driver_conductor_id"));

				DriverList.add(flyingManageInvestigationLogDTO);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return DriverList;

	}

	// conductor id
	public ArrayList<FlyingManageInvestigationLogDTO> getConductorList() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		ArrayList<FlyingManageInvestigationLogDTO> conductorList = new ArrayList<FlyingManageInvestigationLogDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT dcr_driver_conductor_id\r\n" + "FROM public.nt_t_driver_conductor_registration\r\n"
					+ "where dcr_driver_conductor_id like '%C%'\r\n" + "and  dcr_status in('TA','A')";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {

				FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
				flyingManageInvestigationLogDTO.setConductorID(rs.getString("dcr_driver_conductor_id"));

				conductorList.add(flyingManageInvestigationLogDTO);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return conductorList;

	}

	// Get Name
	public String getname(String id) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String name = null;

		try {
			con = ConnectionManager.getConnection();
			String sql = null;

			sql = "SELECT dcr_full_name_eng\r\n" + "FROM public.nt_t_driver_conductor_registration\r\n"
					+ "where dcr_driver_conductor_id =?\r\n" + "and  dcr_status in('TA','A') ";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, id);
			rs = stmt.executeQuery();

			while (rs.next()) {

				name = rs.getString("dcr_full_name_eng");

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return name;

	}

	// --violated Conditions
	public ArrayList<FluingSquadVioConditionDTO> getConditionList(String invesno) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		ArrayList<FluingSquadVioConditionDTO> conditionList = new ArrayList<FluingSquadVioConditionDTO>();
		ArrayList<FluingSquadVioConditionDTO> conditionListn = new ArrayList<FluingSquadVioConditionDTO>();
		ArrayList<FluingSquadVioConditionDTO> finalconditionList = new ArrayList<FluingSquadVioConditionDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT vio_condition_cd, vio_violated_status, remark,ofm_offence_desc\r\n"
					+ "FROM public.nt_t_flying_vio_conditions,nt_m_offence_management\r\n"
					+ "where vio_invesno = ? \r\n"
					+ "and nt_t_flying_vio_conditions.vio_condition_cd=nt_m_offence_management.ofm_offence_code";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, invesno);
			rs = stmt.executeQuery();

			while (rs.next()) {

				FluingSquadVioConditionDTO flyingManageInvestigationLogDTO = new FluingSquadVioConditionDTO();
				flyingManageInvestigationLogDTO.setCode(rs.getString("vio_condition_cd"));
				flyingManageInvestigationLogDTO.setDescription(rs.getString("ofm_offence_desc"));
				flyingManageInvestigationLogDTO.setRemarks(rs.getString("remark"));
				flyingManageInvestigationLogDTO.setStstus(rs.getBoolean("vio_violated_status"));
				flyingManageInvestigationLogDTO.setUpdated(true);

				conditionList.add(flyingManageInvestigationLogDTO);
			}

			conditionListn = getConditionListn(invesno);
			finalconditionList.addAll(conditionList);
			finalconditionList.addAll(conditionListn);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return finalconditionList;

	}

	// reference conditions
	public ArrayList<FluingSquadVioConditionDTO> getConditionListn(String invesNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		ArrayList<FluingSquadVioConditionDTO> conditionList = new ArrayList<FluingSquadVioConditionDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT distinct ofm_offence_code, ofm_offence_desc \r\n"
					+ "FROM public.nt_m_offence_management,nt_t_offence_management_det \r\n" + "where status = 'A'\r\n"
					+ "and  omd_offence_code = ofm_offence_code\r\n" + "and omd_is_investigation = 'Y'\r\n"
					+ "and ofm_offence_code not in (SELECT vio_condition_cd\r\n"
					+ "					          FROM public.nt_t_flying_vio_conditions,nt_m_offence_management \r\n"
					+ "					          where vio_invesno =?\r\n"
					+ "					          and nt_t_flying_vio_conditions.vio_condition_cd=nt_m_offence_management.ofm_offence_code\r\n"
					+ "                             )";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, invesNo);
			rs = stmt.executeQuery();

			while (rs.next()) {

				FluingSquadVioConditionDTO flyingManageInvestigationLogDTO = new FluingSquadVioConditionDTO();
				flyingManageInvestigationLogDTO.setCode(rs.getString("ofm_offence_code"));
				flyingManageInvestigationLogDTO.setDescription(rs.getString("ofm_offence_desc"));
				flyingManageInvestigationLogDTO.setStstus(false);
				flyingManageInvestigationLogDTO.setUpdated(false);

				conditionList.add(flyingManageInvestigationLogDTO);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return conditionList;

	}

	// violated document types
	public ArrayList<FlyingSquadVioDocumentsDTO> getdocumentlist(String invesno) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		ArrayList<FlyingSquadVioDocumentsDTO> documentList = new ArrayList<FlyingSquadVioDocumentsDTO>();
		ArrayList<FlyingSquadVioDocumentsDTO> documentListn = new ArrayList<FlyingSquadVioDocumentsDTO>();
		ArrayList<FlyingSquadVioDocumentsDTO> finaldocumentList = new ArrayList<FlyingSquadVioDocumentsDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT vio_doc_cd, vio_doc_remark, vio_doc_status,description\r\n"
					+ "FROM public.nt_t_flying_vio_documets,nt_r_flying_squad_documents\r\n"
					+ "where vio_doc_invesno = ?\r\n"
					+ "and nt_t_flying_vio_documets.vio_doc_cd=nt_r_flying_squad_documents.code";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, invesno);
			rs = stmt.executeQuery();

			while (rs.next()) {

				FlyingSquadVioDocumentsDTO flyingManageInvestigationLogDTO = new FlyingSquadVioDocumentsDTO();
				flyingManageInvestigationLogDTO.setCode(rs.getString("vio_doc_cd"));
				flyingManageInvestigationLogDTO.setDescription(rs.getString("description"));
				flyingManageInvestigationLogDTO.setRemark(rs.getString("vio_doc_remark"));
				flyingManageInvestigationLogDTO.setViolated(rs.getBoolean("vio_doc_status"));
				flyingManageInvestigationLogDTO.setUpdated(true);

				documentList.add(flyingManageInvestigationLogDTO);
			}

			documentListn = getdocumentListn(invesno);
			finaldocumentList.addAll(documentList);
			finaldocumentList.addAll(documentListn);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return finaldocumentList;

	}

	// reference documents
	public ArrayList<FlyingSquadVioDocumentsDTO> getdocumentListn(String invesno) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		ArrayList<FlyingSquadVioDocumentsDTO> documentList = new ArrayList<FlyingSquadVioDocumentsDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT code, description \r\n" + "FROM public.nt_r_flying_squad_documents\r\n"
					+ "where code not in (SELECT vio_doc_cd\r\n"
					+ "				   FROM public.nt_t_flying_vio_documets,nt_r_flying_squad_documents\r\n"
					+ "				   where vio_doc_invesno =?\r\n"
					+ "				   and nt_t_flying_vio_documets.vio_doc_cd=nt_r_flying_squad_documents.code)";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, invesno);
			rs = stmt.executeQuery();

			while (rs.next()) {

				FlyingSquadVioDocumentsDTO flyingManageInvestigationLogDTO = new FlyingSquadVioDocumentsDTO();
				flyingManageInvestigationLogDTO.setCode(rs.getString("code"));
				flyingManageInvestigationLogDTO.setDescription(rs.getString("description"));
				flyingManageInvestigationLogDTO.setViolated(false);
				flyingManageInvestigationLogDTO.setUpdated(false);

				documentList.add(flyingManageInvestigationLogDTO);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return documentList;

	}

	// insert record documents
	public void savemasterdata(ArrayList<FlyingSquadVioDocumentsDTO> doclist, String user, String refno,
			String reportNo, String vehicleno, String invesno, Boolean isInsert) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			java.util.Date date = new java.util.Date();
			Timestamp timeStamp = new Timestamp(date.getTime());
			con = ConnectionManager.getConnection();
			String sql = null;

			if (isInsert) {
				for (FlyingSquadVioDocumentsDTO flyingSquadVioDocumentsDTO : doclist) {
					sql = "INSERT INTO public.nt_t_flying_vio_documets\r\n"
							+ "(vio_doc_refno, vio_doc_reportno, vio_doc_vehicleno, vio_doc_cd, vio_doc_remark, \r\n"
							+ "vio_doc_status, vio_created_by, vio_created_date, vio_doc_invesno)\r\n"
							+ "VALUES(?, ?, ?, ?, ?,?, ?, ?, ?)";

					stmt = con.prepareStatement(sql);
					stmt.setString(1, refno);
					stmt.setString(2, reportNo);
					stmt.setString(3, vehicleno);
					stmt.setString(4, flyingSquadVioDocumentsDTO.getCode());
					stmt.setString(5, flyingSquadVioDocumentsDTO.getRemark());
					if (flyingSquadVioDocumentsDTO.isViolated() == true) {
						stmt.setString(6, "true");
					} else {
						stmt.setString(6, "false");
					}

					stmt.setString(7, user);
					stmt.setTimestamp(8, timeStamp);
					stmt.setString(9, invesno);

					stmt.executeUpdate();
					con.commit();
				}
			} else {
				for (FlyingSquadVioDocumentsDTO flyingSquadVioDocumentsDTO : doclist) {
					sql = "UPDATE public.nt_t_flying_vio_documets\r\n"
							+ "		    		SET vio_doc_remark=?, vio_doc_status=?, \r\n"
							+ "		    		 vio_doc_modified_by=?, vio_doc_modified_date=?\r\n"
							+ "		    		where vio_doc_invesno=?\r\n" + "		    		and vio_doc_cd= ?";

					stmt = con.prepareStatement(sql);
					stmt.setString(1, flyingSquadVioDocumentsDTO.getRemark());
					stmt.setBoolean(2, flyingSquadVioDocumentsDTO.isViolated());
					stmt.setString(3, user);
					stmt.setTimestamp(4, timeStamp);
					stmt.setString(5, invesno);
					stmt.setString(6, flyingSquadVioDocumentsDTO.getCode());

					stmt.executeUpdate();
					con.commit();
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

	}

	// insert conditions
	public void savecondition(ArrayList<FluingSquadVioConditionDTO> conditionlist, String user, String refno,
			String reportNo, String vehicleno, String invesno, Boolean isInsert) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			java.util.Date date = new java.util.Date();
			Timestamp timeStamp = new Timestamp(date.getTime());
			con = ConnectionManager.getConnection();
			String sql = null;
			if (isInsert) {
				for (FluingSquadVioConditionDTO fluingSquadVioConditionDTO : conditionlist) {
					sql = "INSERT INTO public.nt_t_flying_vio_conditions\r\n"
							+ "(vio_refno, vio_report_no, vio_vehicleno, vio_condition_cd, vio_violated_status,\r\n"
							+ "remark, vio_created_by, vio_created_date, vio_invesno)\r\n"
							+ "VALUES(?,?,?,?, ?, ?, ?, ?, ?)";

					stmt = con.prepareStatement(sql);
					stmt.setString(1, refno);
					stmt.setString(2, reportNo);
					stmt.setString(3, vehicleno);
					stmt.setString(4, fluingSquadVioConditionDTO.getCode());
					stmt.setBoolean(5, fluingSquadVioConditionDTO.isStstus());
					stmt.setString(6, fluingSquadVioConditionDTO.getRemarks());
					stmt.setString(7, user);
					stmt.setTimestamp(8, timeStamp);
					stmt.setString(9, invesno);

					stmt.executeUpdate();
					con.commit();
				}
			} else {
				for (FluingSquadVioConditionDTO fluingSquadVioConditionDTO : conditionlist) {
					sql = "UPDATE public.nt_t_flying_vio_conditions\r\n" + "SET vio_violated_status=?, remark=?,\r\n"
							+ "vio_modified_by=?, vio_modified_date=?\r\n" + "where vio_invesno=?\r\n"
							+ "and  vio_condition_cd=?";

					stmt = con.prepareStatement(sql);

					stmt.setBoolean(1, fluingSquadVioConditionDTO.isStstus());
					stmt.setString(2, fluingSquadVioConditionDTO.getRemarks());
					stmt.setString(3, user);
					stmt.setTimestamp(4, timeStamp);
					stmt.setString(5, invesno);
					stmt.setString(6, fluingSquadVioConditionDTO.getCode());
					stmt.executeUpdate();
					con.commit();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

	}

	// update master
	public void save(FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {

			java.util.Date date = new java.util.Date();
			Timestamp timeStamp = new Timestamp(date.getTime());
			con = ConnectionManager.getConnection();
			String sql = null;

			sql = "UPDATE public.nt_t_flying_investigation_log_det\r\n" + "SET inv_det_driver_id=?,\r\n"
					+ "inv_det_driver_name=?, inv_det_conductor_id=?, \r\n"
					+ "inv_det_conductor_name=?, inv_permit_no=?, inv_permit_owner=?,\r\n"
					+ "inv_det_modified_by=?, inv_modified_date=?\r\n" + "WHERE inv_investigation_no=?";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, flyingManageInvestigationLogDTO.getDriverID());
			stmt.setString(2, flyingManageInvestigationLogDTO.getDriverName());
			stmt.setString(3, flyingManageInvestigationLogDTO.getConductorID());
			stmt.setString(4, flyingManageInvestigationLogDTO.getConductName());
			stmt.setString(5, flyingManageInvestigationLogDTO.getPermitNo());
			stmt.setString(6, flyingManageInvestigationLogDTO.getPermitowner());
			stmt.setString(7, user);
			stmt.setTimestamp(8, timeStamp);
			stmt.setString(9, flyingManageInvestigationLogDTO.getInvesNo());

			stmt.executeUpdate();
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

	}

	public ArrayList<FlyingManageInvestigationLogDTO> getinvesnolist(String refNo, String reportNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		ArrayList<FlyingManageInvestigationLogDTO> noList = new ArrayList<FlyingManageInvestigationLogDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = null;
			if (refNo == null && reportNo == null) {
				sql = "select inv_investigation_no \r\n" + "FROM public.nt_t_flying_investigation_log_det\r\n"
						+ "where  COALESCE(inv_det_delete, 'E') <> 'D'" + " order by inv_investigation_no";

				stmt = con.prepareStatement(sql);
				rs = stmt.executeQuery();

				while (rs.next()) {

					FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
					flyingManageInvestigationLogDTO.setInvesNo(rs.getString("inv_investigation_no"));

					noList.add(flyingManageInvestigationLogDTO);
				}
			} else if (refNo != null && reportNo == null) {
				sql = "select inv_investigation_no \r\n" + "FROM public.nt_t_flying_investigation_log_det\r\n"
						+ "where  inv_det_ref_no = ? " + " order by inv_investigation_no";

				stmt = con.prepareStatement(sql);
				stmt.setString(1, refNo);
				rs = stmt.executeQuery();

				while (rs.next()) {

					FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
					flyingManageInvestigationLogDTO.setInvesNo(rs.getString("inv_investigation_no"));

					noList.add(flyingManageInvestigationLogDTO);
				}
			} else {
				sql = "select inv_investigation_no \r\n" + "FROM public.nt_t_flying_investigation_log_det\r\n"
						+ "where  inv_det_report_no = ? \r\n" + "and COALESCE(inv_det_delete, 'E') <> 'D'"
						+ " order by inv_investigation_no";

				stmt = con.prepareStatement(sql);
				stmt.setString(1, reportNo);
				rs = stmt.executeQuery();

				while (rs.next()) {

					FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
					flyingManageInvestigationLogDTO.setInvesNo(rs.getString("inv_investigation_no"));

					noList.add(flyingManageInvestigationLogDTO);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return noList;

	}

	public String getrefNoNew(String reportNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String refNo = null;

		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT inv_det_ref_no\r\n" + "FROM public.nt_t_flying_investigation_log_det\r\n"
					+ "where inv_det_report_no =?";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, reportNo);
			rs = stmt.executeQuery();

			while (rs.next()) {

				refNo = rs.getString("inv_det_ref_no");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return refNo;

	}

	@Override
	public FlyingManageInvestigationLogDTO getShowDetails(String permitNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();

		try {
			con = ConnectionManager.getConnection();
			String sql = null;

			sql = "SELECT inv_det_ref_no, inv_det_report_no, inv_det_route_no, inv_det_route_from, inv_det_route_to, inv_det_service_cd, \r\n"
					+ "inv_det_driver_id, inv_det_driver_name, inv_det_conductor_id, inv_det_conductor_name, inv_permit_no,description,\r\n"
					+ "inv_permit_owner ,inv_det_vehicle_no,inv_investigation_no \r\n"
					+ "FROM public.nt_t_flying_investigation_log_det ,nt_r_service_types\r\n"
					+ "where inv_permit_no = ?\r\n"
					+ "and nt_t_flying_investigation_log_det.inv_det_service_cd=nt_r_service_types.code";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, permitNo);
			rs = stmt.executeQuery();

			while (rs.next()) {

				flyingManageInvestigationLogDTO.setRefNo(rs.getString("inv_det_ref_no"));
				flyingManageInvestigationLogDTO.setReportNo(rs.getString("inv_det_report_no"));
				flyingManageInvestigationLogDTO.setRouteNo(rs.getString("inv_det_route_no"));
				flyingManageInvestigationLogDTO.setRouteFrom(rs.getString("inv_det_route_from"));
				flyingManageInvestigationLogDTO.setRouteTo(rs.getString("inv_det_route_to"));
				flyingManageInvestigationLogDTO.setServiceTypeCd(rs.getString("inv_det_service_cd"));
				flyingManageInvestigationLogDTO.setServiceTypeDes(rs.getString("description"));
				flyingManageInvestigationLogDTO.setDriverID(rs.getString("inv_det_driver_id"));
				flyingManageInvestigationLogDTO.setDriverName(rs.getString("inv_det_driver_name"));
				flyingManageInvestigationLogDTO.setConductorID(rs.getString("inv_det_conductor_id"));
				flyingManageInvestigationLogDTO.setConductName(rs.getString("inv_det_conductor_name"));
				flyingManageInvestigationLogDTO.setPermitNo(rs.getString("inv_permit_no"));
				flyingManageInvestigationLogDTO.setPermitowner(rs.getString("inv_permit_owner"));
				flyingManageInvestigationLogDTO.setBusNo(rs.getString("inv_det_vehicle_no"));
				flyingManageInvestigationLogDTO.setInvesNo(rs.getString("inv_investigation_no"));

				String permitno = null;
				permitno = flyingManageInvestigationLogDTO.getPermitNo();
				if (permitno == null || permitno.equalsIgnoreCase("") || permitno.isEmpty()) {
					FlyingManageInvestigationLogDTO flyingManageInvestigationvehicleLogDTO = new FlyingManageInvestigationLogDTO();
					flyingManageInvestigationvehicleLogDTO = getVehicleDetails(
							flyingManageInvestigationLogDTO.getBusNo());
					flyingManageInvestigationLogDTO.setPermitNo(flyingManageInvestigationvehicleLogDTO.getPermitNo());
					flyingManageInvestigationLogDTO
							.setPermitowner(flyingManageInvestigationvehicleLogDTO.getPermitowner());
				}

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return flyingManageInvestigationLogDTO;

	}

	@Override
	public FlyingManageInvestigationLogDTO getShowDetailsN(String investigationNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();

		try {
			con = ConnectionManager.getConnection();
			String sql = null;

			sql = "SELECT inv_det_ref_no, inv_det_report_no, inv_det_route_no, inv_det_route_from, inv_det_route_to, inv_det_service_cd, \r\n"
					+ "inv_det_driver_id, inv_det_driver_name, inv_det_conductor_id, inv_det_conductor_name, inv_permit_no,description,\r\n"
					+ "inv_permit_owner ,inv_det_vehicle_no,inv_investigation_no \r\n"
					+ "FROM public.nt_t_flying_investigation_log_det ,nt_r_service_types\r\n"
					+ "where inv_investigation_no = ?\r\n"
					+ "and nt_t_flying_investigation_log_det.inv_det_service_cd=nt_r_service_types.code";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, investigationNo);
			rs = stmt.executeQuery();

			while (rs.next()) {

				flyingManageInvestigationLogDTO.setRefNo(rs.getString("inv_det_ref_no"));
				flyingManageInvestigationLogDTO.setReportNo(rs.getString("inv_det_report_no"));
				flyingManageInvestigationLogDTO.setRouteNo(rs.getString("inv_det_route_no"));
				flyingManageInvestigationLogDTO.setRouteFrom(rs.getString("inv_det_route_from"));
				flyingManageInvestigationLogDTO.setRouteTo(rs.getString("inv_det_route_to"));
				flyingManageInvestigationLogDTO.setServiceTypeCd(rs.getString("inv_det_service_cd"));
				flyingManageInvestigationLogDTO.setServiceTypeDes(rs.getString("description"));
				flyingManageInvestigationLogDTO.setDriverID(rs.getString("inv_det_driver_id"));
				flyingManageInvestigationLogDTO.setDriverName(rs.getString("inv_det_driver_name"));
				flyingManageInvestigationLogDTO.setConductorID(rs.getString("inv_det_conductor_id"));
				flyingManageInvestigationLogDTO.setConductName(rs.getString("inv_det_conductor_name"));
				flyingManageInvestigationLogDTO.setPermitNo(rs.getString("inv_permit_no"));
				flyingManageInvestigationLogDTO.setPermitowner(rs.getString("inv_permit_owner"));
				flyingManageInvestigationLogDTO.setBusNo(rs.getString("inv_det_vehicle_no"));
				flyingManageInvestigationLogDTO.setInvesNo(rs.getString("inv_investigation_no"));

				String permitno = null;
				permitno = flyingManageInvestigationLogDTO.getPermitNo();
				if (permitno == null || permitno.equalsIgnoreCase("") || permitno.isEmpty()) {
					FlyingManageInvestigationLogDTO flyingManageInvestigationvehicleLogDTO = new FlyingManageInvestigationLogDTO();
					flyingManageInvestigationvehicleLogDTO = getVehicleDetails(
							flyingManageInvestigationLogDTO.getBusNo());
					flyingManageInvestigationLogDTO.setPermitNo(flyingManageInvestigationvehicleLogDTO.getPermitNo());
					flyingManageInvestigationLogDTO
							.setPermitowner(flyingManageInvestigationvehicleLogDTO.getPermitowner());
				}

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return flyingManageInvestigationLogDTO;

	}

	@Override
	public String checkDataAlreadyHv(String invNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String value = null;

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT vio_invesno FROM public.nt_t_flying_vio_conditions "
					+ " WHERE vio_invesno = ? limit 1 ";

			ps = con.prepareStatement(query);
			ps.setString(1, invNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				value = rs.getString("vio_invesno");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return value;
	}

	// --violated Conditions
	public ArrayList<FluingSquadVioConditionDTO> getConditionListN(String invesno) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		ArrayList<FluingSquadVioConditionDTO> conditionList = new ArrayList<FluingSquadVioConditionDTO>();
		ArrayList<FluingSquadVioConditionDTO> finalconditionList = new ArrayList<FluingSquadVioConditionDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT vio_condition_cd, vio_violated_status, remark,ofm_offence_desc\r\n"
					+ "FROM public.nt_t_flying_vio_conditions,nt_m_offence_management\r\n"
					+ "where vio_invesno = ? \r\n"
					+ "and nt_t_flying_vio_conditions.vio_condition_cd=nt_m_offence_management.ofm_offence_code";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, invesno);
			rs = stmt.executeQuery();

			while (rs.next()) {

				FluingSquadVioConditionDTO flyingManageInvestigationLogDTO = new FluingSquadVioConditionDTO();
				flyingManageInvestigationLogDTO.setCode(rs.getString("vio_condition_cd"));
				flyingManageInvestigationLogDTO.setDescription(rs.getString("ofm_offence_desc"));
				flyingManageInvestigationLogDTO.setRemarks(rs.getString("remark"));
				flyingManageInvestigationLogDTO.setStstus(rs.getBoolean("vio_violated_status"));
				flyingManageInvestigationLogDTO.setUpdated(true);

				conditionList.add(flyingManageInvestigationLogDTO);
			}

			finalconditionList.addAll(conditionList);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return finalconditionList;

	}

	public ArrayList<FlyingSquadVioDocumentsDTO> getdocumentlistN(String invesno) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		ArrayList<FlyingSquadVioDocumentsDTO> documentList = new ArrayList<FlyingSquadVioDocumentsDTO>();
		ArrayList<FlyingSquadVioDocumentsDTO> finaldocumentList = new ArrayList<FlyingSquadVioDocumentsDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT vio_doc_cd, vio_doc_remark, vio_doc_status,description\r\n"
					+ "FROM public.nt_t_flying_vio_documets,nt_r_flying_squad_documents\r\n"
					+ "where vio_doc_invesno = ?\r\n"
					+ "and nt_t_flying_vio_documets.vio_doc_cd=nt_r_flying_squad_documents.code";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, invesno);
			rs = stmt.executeQuery();

			while (rs.next()) {

				FlyingSquadVioDocumentsDTO flyingManageInvestigationLogDTO = new FlyingSquadVioDocumentsDTO();
				flyingManageInvestigationLogDTO.setCode(rs.getString("vio_doc_cd"));
				flyingManageInvestigationLogDTO.setDescription(rs.getString("description"));
				flyingManageInvestigationLogDTO.setRemark(rs.getString("vio_doc_remark"));
				flyingManageInvestigationLogDTO.setViolated(rs.getBoolean("vio_doc_status"));
				flyingManageInvestigationLogDTO.setUpdated(true);

				documentList.add(flyingManageInvestigationLogDTO);
			}

			finaldocumentList.addAll(documentList);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return finaldocumentList;

	}

	@Override
	public String getNIC(String id) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String name = null;

		try {
			con = ConnectionManager.getConnection();
			String sql = null;
			String where = " ";

			where = " like '%" + id + "%'";

			sql = "SELECT  distinct dcr_nic\r\n" + "FROM public.nt_t_driver_conductor_registration\r\n"
					+ "where  dcr_full_name_eng " + where;

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {

				name = rs.getString("dcr_nic");

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return name;

	}

	@Override
	public String getInspectionLocation(String reportNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String value = null;

		try {

			con = ConnectionManager.getConnection();

			String query = "select  inv_places_investigation  from  public.nt_t_flying_investigation_log_master where inv_report_no =? ";

			ps = con.prepareStatement(query);
			ps.setString(1, reportNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				value = rs.getString("inv_places_investigation");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return value;
	}

	@Override
	public String getInspectionLocationNew(String reportNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String value = null;

		try {

			con = ConnectionManager.getConnection();

			String query = "select  inv_det_invest_place   from  public.nt_t_flying_investigation_log_det where inv_det_report_no =? ";

			ps = con.prepareStatement(query);
			ps.setString(1, reportNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				value = rs.getString("inv_det_invest_place");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return value;
	}

	@Override
	public Date getInspectionDate(String reportNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Date value = null;

		try {

			con = ConnectionManager.getConnection();

			String query = "select  inv_date   from  public.nt_t_flying_investigation_log_master where inv_report_no =? ";

			ps = con.prepareStatement(query);
			ps.setString(1, reportNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				value = rs.getDate("inv_date");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return value;
	}

}
