package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import lk.informatics.ntc.model.dto.FlyingManageInvestigationLogDTO;
import lk.informatics.ntc.model.dto.FlyingSquadDocumentDTO;
import lk.informatics.ntc.model.dto.FlyingSquadInvestiMasterDTO;
import lk.informatics.ntc.model.dto.FlyingSquadVioDocumentsDTO;
import lk.informatics.ntc.view.util.ConnectionManager;

public class FlyingSquadRelaseDocumentServiceImpl implements FlyingSquadRelaseDocumentService {

	public ArrayList<FlyingSquadInvestiMasterDTO> getrefNo(String repNo, String invesNo, Date startDate, Date endDate) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

		ArrayList<FlyingSquadInvestiMasterDTO> refnoList = new ArrayList<FlyingSquadInvestiMasterDTO>();
		try {
			con = ConnectionManager.getConnection();

			if (repNo == null && invesNo == null && startDate == null && endDate == null) {
				String sql = "SELECT distinct(vio_doc_refno)\r\n" + "from nt_t_flying_vio_documets\r\n"
						+ "where vio_doc_refno IS NOT null\r\n" + "and vio_doc_status = 'true'"
						+ " order by vio_doc_refno ";

				stmt = con.prepareStatement(sql);
				rs = stmt.executeQuery();

				while (rs.next()) {

					FlyingSquadInvestiMasterDTO flyingSquadInvestiMasterDTO = new FlyingSquadInvestiMasterDTO();
					flyingSquadInvestiMasterDTO.setRefNo(rs.getString("vio_doc_refno"));

					refnoList.add(flyingSquadInvestiMasterDTO);
				}
			} else if (repNo != null) {
				String sql = "SELECT distinct(vio_doc_refno)\r\n" + "from nt_t_flying_vio_documets\r\n"
						+ "where vio_doc_status = 'true'\r\n" + "and vio_doc_reportno = ?";

				stmt = con.prepareStatement(sql);
				stmt.setString(1, repNo);
				rs = stmt.executeQuery();

				while (rs.next()) {

					FlyingSquadInvestiMasterDTO flyingSquadInvestiMasterDTO = new FlyingSquadInvestiMasterDTO();
					flyingSquadInvestiMasterDTO.setRefNo(rs.getString("vio_doc_refno"));

					refnoList.add(flyingSquadInvestiMasterDTO);
				}

			} else if (invesNo != null) {
				String sql = "SELECT distinct(vio_doc_refno)\r\n" + "from nt_t_flying_vio_documets\r\n"
						+ "where vio_doc_status = 'true'\r\n" + "and vio_doc_invesno = ?";

				stmt = con.prepareStatement(sql);
				stmt.setString(1, invesNo);
				rs = stmt.executeQuery();

				while (rs.next()) {

					FlyingSquadInvestiMasterDTO flyingSquadInvestiMasterDTO = new FlyingSquadInvestiMasterDTO();
					flyingSquadInvestiMasterDTO.setRefNo(rs.getString("vio_doc_refno"));

					refnoList.add(flyingSquadInvestiMasterDTO);
				}

			} else if (startDate != null && endDate != null) {
				String sql = "SELECT distinct(vio_doc_refno)\r\n"
						+ "from nt_t_flying_vio_documets,nt_t_flying_investigation_log_master\r\n"
						+ "where vio_doc_refno IS NOT null \r\n" + "and vio_doc_status = 'true'\r\n"
						+ "and vio_doc_refno =inv_referenceno\r\n"
						+ "and to_char(nt_t_flying_investigation_log_master.inv_date,'yyyy/MM/DD') >=?\r\n"
						+ "and to_char(nt_t_flying_investigation_log_master.inv_date,'yyyy/MM/DD') <=?";

				stmt = con.prepareStatement(sql);
				stmt.setString(1, sdf.format(startDate));
				stmt.setString(2, sdf.format(endDate));
				rs = stmt.executeQuery();

				while (rs.next()) {

					FlyingSquadInvestiMasterDTO flyingSquadInvestiMasterDTO = new FlyingSquadInvestiMasterDTO();
					flyingSquadInvestiMasterDTO.setRefNo(rs.getString("vio_doc_refno"));

					refnoList.add(flyingSquadInvestiMasterDTO);
				}

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return refnoList;

	}

	// --- get the report no list;
	public ArrayList<FlyingManageInvestigationLogDTO> getreportNo(String refNo, Date startDate, Date endDate) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		ArrayList<FlyingManageInvestigationLogDTO> reportNoList = new ArrayList<FlyingManageInvestigationLogDTO>();
		try {
			con = ConnectionManager.getConnection();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			if (refNo != null) {
				String sql = "SELECT distinct(vio_doc_reportno) \r\n" + "from nt_t_flying_vio_documets\r\n"
						+ "where vio_doc_refno = ?\r\n" + "and vio_doc_status = 'true'" + "order by vio_doc_reportno ";

				stmt = con.prepareStatement(sql);
				stmt.setString(1, refNo);
				rs = stmt.executeQuery();

				while (rs.next()) {

					FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
					flyingManageInvestigationLogDTO.setReportNo(rs.getString("vio_doc_reportno"));

					reportNoList.add(flyingManageInvestigationLogDTO);
				}

			}

			else if (startDate != null && endDate != null) {
				String sql = "SELECT distinct(vio_doc_reportno)\r\n" + "from nt_t_flying_vio_documets\r\n"
						+ "where vio_doc_status = 'true'\r\n" + "and vio_doc_reportno IS NOT null \r\n"
						+ "and vio_doc_refno in (SELECT distinct(vio_doc_refno)\r\n"
						+ "    				  from nt_t_flying_vio_documets,nt_t_flying_investigation_log_master\r\n"
						+ "					  where vio_doc_refno IS NOT null \r\n"
						+ "					  and vio_doc_status = 'true'\r\n"
						+ "					  and vio_doc_refno =inv_referenceno\r\n"
						+ "					  and to_char(nt_t_flying_investigation_log_master.inv_date,'yyyy/MM/DD') >=?\r\n"
						+ "                      and to_char(nt_t_flying_investigation_log_master.inv_date,'yyyy/MM/DD') <=?\r\n"
						+ "                      )";

				stmt = con.prepareStatement(sql);
				stmt.setString(1, sdf.format(startDate));
				stmt.setString(2, sdf.format(endDate));
				rs = stmt.executeQuery();

				while (rs.next()) {

					FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
					flyingManageInvestigationLogDTO.setReportNo(rs.getString("vio_doc_reportno"));

					reportNoList.add(flyingManageInvestigationLogDTO);
				}

			} else {
				String sql = "SELECT distinct(vio_doc_reportno) \r\n" + "from nt_t_flying_vio_documets\r\n"
						+ "where vio_doc_status = 'true'";

				stmt = con.prepareStatement(sql);
				rs = stmt.executeQuery();

				while (rs.next()) {

					FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
					flyingManageInvestigationLogDTO.setReportNo(rs.getString("vio_doc_reportno"));
					reportNoList.add(flyingManageInvestigationLogDTO);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return reportNoList;

	}

	public ArrayList<FlyingManageInvestigationLogDTO> getinvesnolist(String refNo, String reportNo, Date startDate,
			Date endDate) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		ArrayList<FlyingManageInvestigationLogDTO> noList = new ArrayList<FlyingManageInvestigationLogDTO>();
		try {
			con = ConnectionManager.getConnection();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			String sql = null;
			if (refNo == null && reportNo == null && startDate == null && endDate == null) {
				sql = "SELECT distinct(vio_doc_invesno)\r\n" + "FROM public.nt_t_flying_vio_documets\r\n"
						+ "where vio_doc_status = 'true'" + "order by vio_doc_invesno ";

				stmt = con.prepareStatement(sql);
				rs = stmt.executeQuery();

				while (rs.next()) {

					FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
					flyingManageInvestigationLogDTO.setInvesNo(rs.getString("vio_doc_invesno"));

					noList.add(flyingManageInvestigationLogDTO);
				}
			}

			else if (reportNo != null) {
				sql = "SELECT distinct(vio_doc_invesno)\r\n" + "FROM public.nt_t_flying_vio_documets\r\n"
						+ "where  vio_doc_reportno = ?\r\n" + "and vio_doc_status = 'true'";

				stmt = con.prepareStatement(sql);
				stmt.setString(1, reportNo);
				rs = stmt.executeQuery();

				while (rs.next()) {

					FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
					flyingManageInvestigationLogDTO.setInvesNo(rs.getString("vio_doc_invesno"));

					noList.add(flyingManageInvestigationLogDTO);
				}
			} else if (startDate != null && endDate != null && refNo == null) {

				sql = "SELECT distinct(vio_doc_invesno)\r\n" + "FROM public.nt_t_flying_vio_documets\r\n"
						+ "where vio_doc_status = 'true'\r\n"
						+ "and vio_doc_refno in (SELECT distinct(vio_doc_refno)\r\n"
						+ "    				  from nt_t_flying_vio_documets,nt_t_flying_investigation_log_master\r\n"
						+ "					  where vio_doc_refno IS NOT null \r\n"
						+ "					  and vio_doc_status = 'true'\r\n"
						+ "					  and vio_doc_refno =inv_referenceno\r\n"
						+ "					  and to_char(nt_t_flying_investigation_log_master.inv_date,'yyyy/MM/DD') >=?\r\n"
						+ "                      and to_char(nt_t_flying_investigation_log_master.inv_date,'yyyy/MM/DD') <=?\r\n"
						+ "                      )";

				stmt = con.prepareStatement(sql);
				stmt.setString(1, sdf.format(startDate));
				stmt.setString(2, sdf.format(endDate));
				rs = stmt.executeQuery();

				while (rs.next()) {

					FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
					flyingManageInvestigationLogDTO.setInvesNo(rs.getString("vio_doc_invesno"));

					noList.add(flyingManageInvestigationLogDTO);
				}

			}

			else {

				sql = "SELECT distinct(vio_doc_invesno)\r\n" + "FROM public.nt_t_flying_vio_documets\r\n"
						+ "where  vio_doc_refno = ?\r\n" + "and vio_doc_status = 'true'";

				stmt = con.prepareStatement(sql);
				stmt.setString(1, refNo);
				rs = stmt.executeQuery();

				while (rs.next()) {

					FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
					flyingManageInvestigationLogDTO.setInvesNo(rs.getString("vio_doc_invesno"));

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

	public ArrayList<FlyingSquadDocumentDTO> getmasterData(Date startDate, Date endDate, String refNo, String reportNo,
			String invesNo) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		String whereSql = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		ArrayList<FlyingSquadDocumentDTO> detailList = new ArrayList<FlyingSquadDocumentDTO>();
		try {

			if (refNo != null && !refNo.equalsIgnoreCase("") && !refNo.isEmpty()) {
				if (whereSql == null || whereSql.equalsIgnoreCase("") || whereSql.isEmpty()) {
					whereSql = "and nt_t_flying_vio_documets.vio_doc_refno =" + "'" + refNo + "'";

				}
			}

			if (reportNo != null && !reportNo.equalsIgnoreCase("") && !reportNo.isEmpty()) {
				if (whereSql == null || whereSql.equalsIgnoreCase("") || whereSql.isEmpty()) {
					whereSql = "and nt_t_flying_vio_documets.vio_doc_reportno =" + "'" + reportNo + "'";

				} else {
					whereSql = whereSql + "  and nt_t_flying_vio_documets.vio_doc_reportno =" + "'" + reportNo + "'";

				}

			}

			if (invesNo != null && !invesNo.equalsIgnoreCase("") && !invesNo.isEmpty()) {
				if (whereSql == null || whereSql.equalsIgnoreCase("") || whereSql.isEmpty()) {
					whereSql = "and inv_investigation_no =" + "'" + invesNo + "'";

				} else {
					whereSql = whereSql + "  and inv_investigation_no =" + "'" + invesNo + "'";

				}

			}
			if (startDate != null) {
				if (whereSql == null || whereSql.equalsIgnoreCase("") || whereSql.isEmpty()) {
					whereSql = " and to_char(nt_t_flying_investigation_log_master.inv_date,'yyyy/MM/DD') >=" + "'"
							+ sdf.format(startDate) + "'";

				}

				else {
					whereSql = whereSql + " and to_char(nt_t_flying_investigation_log_master.inv_date,'yyyy/MM/dd') >="
							+ "'" + sdf.format(startDate) + "'";
				}

			}

			if (endDate != null) {
				if (whereSql == null || whereSql.equalsIgnoreCase("") || whereSql.isEmpty()) {
					whereSql = "  and to_char(nt_t_flying_investigation_log_master.inv_date,'yyyy/MM/dd') <=" + "'"
							+ sdf.format(endDate) + "'";

				}

				else {
					whereSql = whereSql + "  and to_char(nt_t_flying_investigation_log_master.inv_date,'yyyy/MM/DD') <="
							+ "'" + sdf.format(endDate) + "'";
				}

			}

			con = ConnectionManager.getConnection();
			String sql = "select distinct(inv_investigation_no),inv_permit_no,inv_det_vehicle_no,inv_permit_owner,inv_date\r\n"
					+ "FROM nt_t_flying_vio_documets,nt_t_flying_investigation_log_master,nt_t_flying_investigation_log_det\r\n"
					+ "where nt_t_flying_vio_documets.vio_doc_reportno = nt_t_flying_investigation_log_master.inv_report_no\r\n"
					+ "and nt_t_flying_vio_documets.vio_doc_reportno = nt_t_flying_investigation_log_det.inv_det_report_no\r\n"
					+ "and nt_t_flying_investigation_log_det.inv_det_report_no = nt_t_flying_investigation_log_master.inv_report_no "
					+ " " + whereSql;

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				FlyingSquadDocumentDTO flyingSquadDocumentDTO = new FlyingSquadDocumentDTO();

				flyingSquadDocumentDTO.setPermitNo(rs.getString("inv_permit_no"));
				flyingSquadDocumentDTO.setVehicleNo(rs.getString("inv_det_vehicle_no"));
				flyingSquadDocumentDTO.setOwnerName(rs.getString("inv_permit_owner"));
				flyingSquadDocumentDTO.setInvesDate(rs.getDate("inv_date"));
				flyingSquadDocumentDTO.setInvesNo(rs.getString("inv_investigation_no"));

				String status = null;
				status = getStatus(flyingSquadDocumentDTO.getInvesNo());
				flyingSquadDocumentDTO.setStatus(status);

				detailList.add(flyingSquadDocumentDTO);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return detailList;
	}

	public ArrayList<FlyingSquadVioDocumentsDTO> getdocumentlist(String invesno) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		ArrayList<FlyingSquadVioDocumentsDTO> documentList = new ArrayList<FlyingSquadVioDocumentsDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT vio_doc_cd, vio_doc_remark, COALESCE (vio_release_status,'P') as vio_release_status ,description,vio_release_remark,vio_release_date \r\n"
					+ "FROM public.nt_t_flying_vio_documets,nt_r_flying_squad_documents \r\n"
					+ "where vio_doc_invesno = ?\r\n"
					+ "and nt_t_flying_vio_documets.vio_doc_cd=nt_r_flying_squad_documents.code\r\n"
					+ "and vio_doc_status = 'true'";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, invesno);
			rs = stmt.executeQuery();

			while (rs.next()) {
				String status = null;
				status = rs.getString("vio_release_status");
				FlyingSquadVioDocumentsDTO flyingSquadVioDocumentsDTO = new FlyingSquadVioDocumentsDTO();
				flyingSquadVioDocumentsDTO.setCode(rs.getString("vio_doc_cd"));
				flyingSquadVioDocumentsDTO.setDescription(rs.getString("description"));
				flyingSquadVioDocumentsDTO.setRemark(rs.getString("vio_doc_remark"));

				if (status.equalsIgnoreCase("R")) {
					flyingSquadVioDocumentsDTO.setIsreleased(true);
				} else {
					flyingSquadVioDocumentsDTO.setIsreleased(false);
				}

				flyingSquadVioDocumentsDTO.setReleaseremark(rs.getString("vio_release_remark"));
				flyingSquadVioDocumentsDTO.setReleaseDate(rs.getDate("vio_release_date"));

				documentList.add(flyingSquadVioDocumentsDTO);
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

	// update the released documents
	public void updateData(ArrayList<FlyingSquadVioDocumentsDTO> docList, String user, String invesNo) {
		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = ConnectionManager.getConnection();
			java.util.Date date = new java.util.Date();
			java.sql.Date sDate = new java.sql.Date(date.getTime());

			for (FlyingSquadVioDocumentsDTO flyingSquadVioDocumentsDTO : docList) {

				if (flyingSquadVioDocumentsDTO.isIsreleased() == true
						&& flyingSquadVioDocumentsDTO.getReleaseDate() == null) {

					String sql = "UPDATE public.nt_t_flying_vio_documets\r\n"
							+ "SET vio_release_status=? , vio_release_date=?, vio_release_by=?, vio_release_remark=?\r\n"
							+ "where  vio_doc_invesno= ?  and  vio_doc_cd=?";

					stmt = con.prepareStatement(sql);
					stmt.setString(1, "R");
					stmt.setDate(2, sDate);
					stmt.setString(3, user);
					stmt.setString(4, flyingSquadVioDocumentsDTO.getReleaseremark());
					stmt.setString(5, invesNo);
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

	public FlyingManageInvestigationLogDTO getmasterDetails(String invesNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();

		try {
			con = ConnectionManager.getConnection();
			String sql = null;

			sql = "SELECT inv_det_ref_no, inv_det_report_no, inv_det_route_no, inv_permit_no, inv_permit_owner ,inv_det_vehicle_no\r\n"
					+ "FROM public.nt_t_flying_investigation_log_det \r\n" + "where inv_investigation_no = ? ";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, invesNo);
			rs = stmt.executeQuery();

			while (rs.next()) {
				flyingManageInvestigationLogDTO.setRefNo(rs.getString("inv_det_ref_no"));
				flyingManageInvestigationLogDTO.setReportNo(rs.getString("inv_det_report_no"));
				flyingManageInvestigationLogDTO.setPermitNo(rs.getString("inv_permit_no"));
				flyingManageInvestigationLogDTO.setPermitowner(rs.getString("inv_permit_owner"));
				flyingManageInvestigationLogDTO.setBusNo(rs.getString("inv_det_vehicle_no"));

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

	// get Status
	public String getStatus(String invesNo) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int countdoc = 0;
		int size = 0;
		String status = null;

		try {
			size = getdocsize(invesNo);
			con = ConnectionManager.getConnection();
			String sql = null;

			sql = "select count(vio_doc_status) as count\r\n" + "from nt_t_flying_vio_documets\r\n"
					+ "where vio_doc_invesno = ?\r\n" + "and vio_release_status ='R' \r\n"
					+ "and vio_doc_status = 'true' ";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, invesNo);
			rs = stmt.executeQuery();

			while (rs.next()) {
				countdoc = rs.getInt("count");

				if (countdoc == 0) {
					status = "Pending";
				} else if (countdoc < size) {
					status = "Ongoing";
				} else {
					status = "Released";
				}

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return status;

	}

	public int getdocsize(String invesNo) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int countdoc = 0;

		try {
			con = ConnectionManager.getConnection();
			String sql = null;

			sql = "select count(vio_doc_status) as count \r\n" + "from nt_t_flying_vio_documets\r\n"
					+ "where vio_doc_invesno = ?\r\n" + "and vio_doc_status = 'true'";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, invesNo);
			rs = stmt.executeQuery();

			while (rs.next()) {
				countdoc = rs.getInt("count");

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return countdoc;

	}

	public FlyingManageInvestigationLogDTO getrefNoNew(String reportNo, String invesNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
		try {
			con = ConnectionManager.getConnection();

			if (reportNo != null && invesNo != null) {
				String sql = "SELECT vio_doc_refno,vio_doc_reportno\r\n" + "from nt_t_flying_vio_documets\r\n"
						+ "where  vio_doc_invesno = ?";

				stmt = con.prepareStatement(sql);
				stmt.setString(1, invesNo);
				rs = stmt.executeQuery();

				while (rs.next()) {
					flyingManageInvestigationLogDTO.setRefNo(rs.getString("vio_doc_refno"));
					flyingManageInvestigationLogDTO.setReportNo(rs.getString("vio_doc_reportno"));
					flyingManageInvestigationLogDTO.setInvesNo(invesNo);

				}

			} else if (reportNo == null && invesNo != null) {
				String sql = "SELECT vio_doc_refno,vio_doc_reportno\r\n" + "from nt_t_flying_vio_documets\r\n"
						+ "where  vio_doc_invesno = ?";

				stmt = con.prepareStatement(sql);
				stmt.setString(1, invesNo);
				rs = stmt.executeQuery();

				while (rs.next()) {
					flyingManageInvestigationLogDTO.setRefNo(rs.getString("vio_doc_refno"));
					flyingManageInvestigationLogDTO.setReportNo(rs.getString("vio_doc_reportno"));
					flyingManageInvestigationLogDTO.setInvesNo(invesNo);

				}

			} else if (reportNo != null && invesNo == null) {
				String sql = "SELECT vio_doc_refno,vio_doc_reportno\r\n" + "from nt_t_flying_vio_documets\r\n"
						+ "where  vio_doc_reportno = ?";

				stmt = con.prepareStatement(sql);
				stmt.setString(1, reportNo);
				rs = stmt.executeQuery();

				while (rs.next()) {
					flyingManageInvestigationLogDTO.setRefNo(rs.getString("vio_doc_refno"));
					flyingManageInvestigationLogDTO.setReportNo(rs.getString("vio_doc_reportno"));

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

}
