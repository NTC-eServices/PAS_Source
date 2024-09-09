package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import lk.informatics.ntc.model.dto.FlyingSquadApprovalInvestigationDTO;
import lk.informatics.ntc.view.util.ConnectionManager;

public class FlyingSquadApprovalInvestigationServiceImpl implements FlyingSquadApprovalInvestigationService {

	public ArrayList<FlyingSquadApprovalInvestigationDTO> getDetails(Date startDate, Date EndDate, String refNo) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<FlyingSquadApprovalInvestigationDTO> ApprovalInvestigationList = new ArrayList<FlyingSquadApprovalInvestigationDTO>();
		String whereSql = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		try {

			if (refNo != null && !refNo.equalsIgnoreCase("") && !refNo.isEmpty()) {
				if (whereSql == null || whereSql.equalsIgnoreCase("") || whereSql.isEmpty()) {
					whereSql = "and inv_refno =" + "'" + refNo + "'";

				}
			}

			if (startDate != null) {
				if (whereSql == null || whereSql.equalsIgnoreCase("") || whereSql.isEmpty()) {
					whereSql = " and to_char(inv_date,'yyyy/MM/DD') >=" + "'" + sdf.format(startDate) + "'";

				}

				else {
					whereSql = whereSql + " and to_char(inv_date,'yyyy/MM/dd') >=" + "'" + sdf.format(startDate) + "'";
				}

			}

			if (EndDate != null) {
				if (whereSql == null || whereSql.equalsIgnoreCase("") || whereSql.isEmpty()) {
					whereSql = "  and to_char(inv_date,'yyyy/MM/dd') <=" + "'" + sdf.format(EndDate) + "'";

				}

				else {
					whereSql = whereSql + "  and to_char(inv_date,'yyyy/MM/DD') <=" + "'" + sdf.format(EndDate) + "'";
				}

			}

			con = ConnectionManager.getConnection();
			String sql = "SELECT inv_refno, inv_date, inv_group_cd,inv_status,group_name,inv_reject_reason, inv_recomended_reason, inv_approve_reason\r\n"
					+ "from nt_t_flying_investigation_master,nt_r_group\r\n"
					+ "where  nt_t_flying_investigation_master.inv_group_cd = nt_r_group.code  " + whereSql
					+ " order by inv_refno";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				FlyingSquadApprovalInvestigationDTO ApprovalInvestigationDTO = new FlyingSquadApprovalInvestigationDTO();
				String status = rs.getString("inv_status");
				ApprovalInvestigationDTO.setReferenceNo(rs.getString("inv_refno"));
				ApprovalInvestigationDTO.setInvestigationDate(rs.getDate("inv_date"));
				ApprovalInvestigationDTO.setGroupcd(rs.getString("inv_group_cd"));
				ApprovalInvestigationDTO.setGroupDes(rs.getString("group_name"));
				ApprovalInvestigationDTO.setRejectReason(rs.getString("inv_reject_reason"));
				ApprovalInvestigationDTO.setRecomendedReason(rs.getString("inv_recomended_reason"));
				ApprovalInvestigationDTO.setFinalizedReason(rs.getString("inv_approve_reason"));
				if (status.equalsIgnoreCase("P") || status == "P") {
					ApprovalInvestigationDTO.setStatus("PENDING");
				} else if (status.equalsIgnoreCase("E") || status == "E") {
					ApprovalInvestigationDTO.setStatus("REJECT");
				} else if (status.equalsIgnoreCase("R") || status == "R") {
					ApprovalInvestigationDTO.setStatus("RECOMMENDED");
				}

				else if (status.equalsIgnoreCase("A") || status == "A") {
					ApprovalInvestigationDTO.setStatus("FINALIZED");
				}

				ApprovalInvestigationDTO.setIsallowed(false);

				ApprovalInvestigationList.add(ApprovalInvestigationDTO);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return ApprovalInvestigationList;

	}

	// --update reject reason
	public void updatereasons(boolean reject, boolean recomended, boolean finalized, boolean cancel, String remark,
			String user, String refNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String whereSql = null;

		try {

			java.util.Date date = new java.util.Date();
			Timestamp timeStamp = new Timestamp(date.getTime());

			if (reject == true) {
				whereSql = "inv_reject_reason=" + "'" + remark + "'" + "," + "inv_status=" + "'" + "E" + "'" + ","
						+ "inv_rejected_by=" + "'" + user + "'" + "," + "inv_rejected_date=" + "'" + timeStamp + "'";

			} else if (recomended == true) {
				whereSql = "inv_recomended_reason =" + "'" + remark + "'" + "," + "inv_status=" + "'" + "R" + "'" + ","
						+ "inv_recommended_by=" + "'" + user + "'" + "," + "inv_recommended_date=" + "'" + timeStamp
						+ "'";

			} else {

				whereSql = "inv_approve_reason=" + "'" + remark + "'" + "," + "inv_status=" + "'" + "A" + "'" + ","
						+ "inv_approved_by=" + "'" + user + "'" + "," + "inv_approved_date=" + "'" + timeStamp + "'";
			}

			con = ConnectionManager.getConnection();
			String sql = "UPDATE public.nt_t_flying_investigation_master\r\n"
					+ "SET inv_modified_by=?, inv_modified_date=?, \r\n" + whereSql + "WHERE inv_refno=?";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, user);
			stmt.setTimestamp(2, timeStamp);
			stmt.setString(3, refNo);

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

	// --------select Reference No------
	public ArrayList<FlyingSquadApprovalInvestigationDTO> getReferenceNo(Date startDate, Date endDate) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<FlyingSquadApprovalInvestigationDTO> referenceNoList = new ArrayList<FlyingSquadApprovalInvestigationDTO>();
		String whereSql = null;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

		try {

			if (startDate != null) {
				whereSql = " to_char(inv_date,'yyyy/MM/DD') >=" + "'" + sdf.format(startDate) + "'";

			}

			if (endDate != null) {
				if (whereSql == null || whereSql.equalsIgnoreCase("") || whereSql.isEmpty()) {
					whereSql = "to_char(inv_end_time,'yyyy/MM/DD') <=" + "'" + sdf.format(endDate) + "'";
				} else {
					whereSql = whereSql + " and to_char(inv_end_time,'yyyy/MM/DD') <=" + "'" + sdf.format(endDate)
							+ "'";
				}

			}

			if (endDate == null && startDate == null) {

				whereSql = " inv_status <>" + "'" + "A" + "'";

			}

			con = ConnectionManager.getConnection();
			String sql = "SELECT inv_refno\r\n" + "from nt_t_flying_investigation_master\r\n" + "where" + whereSql
					+ " order by inv_refno";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				FlyingSquadApprovalInvestigationDTO ApprovalInvestigationDTO = new FlyingSquadApprovalInvestigationDTO();
				ApprovalInvestigationDTO.setReferenceNo(rs.getString("inv_refno"));
				referenceNoList.add(ApprovalInvestigationDTO);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
		return referenceNoList;

	}

}
