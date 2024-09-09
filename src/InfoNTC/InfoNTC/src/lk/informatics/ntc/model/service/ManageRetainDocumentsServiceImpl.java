package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import lk.informatics.ntc.model.dto.ManageInvestigationDTO;
import lk.informatics.ntc.model.dto.ManageRetainDocsDTO;
import lk.informatics.ntc.view.util.ConnectionManager;

public class ManageRetainDocumentsServiceImpl implements ManageRetainDocumentsService {

	// Get Charge Ref No List

	@Override
	public List<ManageInvestigationDTO> getChargeRefNoList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ManageInvestigationDTO> chargeRefNoList = new ArrayList<ManageInvestigationDTO>();
		String query;

		try {
			con = ConnectionManager.getConnection();

			query = "SELECT DISTINCT(inv_investigation_no) "
					+ " FROM nt_t_flying_investigation_log_det f, nt_t_flying_vio_documets v "
					+ " WHERE v.vio_doc_invesno = f.inv_investigation_no AND vio_release_status='R'  ORDER BY inv_investigation_no asc ";
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

	// Get Vehicle No. by charge ref No.

	@Override
	public String getVehicleNoByChargeRefNo(String chargeRefNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String vehicleNo = null;

		try {
			con = ConnectionManager.getConnection();

			String query = " select inv_det_vehicle_no,inv_permit_no,inv_permit_owner \r\n"
					+ " from public.nt_t_flying_investigation_log_det where inv_investigation_no=? ";
			ps = con.prepareStatement(query);
			ps.setString(1, chargeRefNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				vehicleNo = rs.getString("inv_det_vehicle_no");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return vehicleNo;
	}

	// Get Permit No. by charge ref No.

	@Override
	public String getPermitNoByChargeRefNo(String chargeRefNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String PermitNo = null;

		try {
			con = ConnectionManager.getConnection();

			String query = " select inv_det_vehicle_no,inv_permit_no,inv_permit_owner \r\n"
					+ " from public.nt_t_flying_investigation_log_det where inv_investigation_no=? ";
			ps = con.prepareStatement(query);
			ps.setString(1, chargeRefNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				PermitNo = rs.getString("inv_permit_no");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return PermitNo;
	}

	// Get Permit Owner Name by charge ref No.

	@Override
	public String getPermitOwnerByChargeRefNo(String chargeRefNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String PermitOwner = null;

		try {
			con = ConnectionManager.getConnection();

			String query = " select inv_det_vehicle_no,inv_permit_no,inv_permit_owner \r\n"
					+ " from public.nt_t_flying_investigation_log_det where inv_investigation_no=? ";
			ps = con.prepareStatement(query);
			ps.setString(1, chargeRefNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				PermitOwner = rs.getString("inv_permit_owner");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return PermitOwner;
	}

	// Add Release Details

	@Override
	public void addReleaseDetails(ManageInvestigationDTO manageInvestigationDTO, ManageRetainDocsDTO manageRetainDocsDTO) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String query = " UPDATE public.nt_t_flying_vio_documets "
					+ " SET chg_rels_date=? , chg_rels_release_to=? , chg_rels_status='R', "
					+ " chg_rels_id_no=?, chg_rels_special_remarks=?, "
					+ " vio_doc_modified_by=?, vio_doc_modified_date=? " + " where vio_doc_invesno=? AND vio_doc_cd=? ";

			ps = con.prepareStatement(query);
			ps.setString(1, manageRetainDocsDTO.getReleaseDate());
			ps.setString(2, manageRetainDocsDTO.getReleaseTo());
			ps.setString(3, manageRetainDocsDTO.getReleaseId());
			ps.setString(4, manageRetainDocsDTO.getSpecialRemarks());
			ps.setString(5, manageRetainDocsDTO.getModifiedBy());
			ps.setTimestamp(6, manageRetainDocsDTO.getModifiedDate());
			ps.setString(7, manageRetainDocsDTO.getChargeRefNo());
			ps.setString(8, manageRetainDocsDTO.getCode());

			ps.executeUpdate();
			con.commit();

			
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

	}

	// get Doc Details By Charge Ref No

	@Override
	public List<ManageRetainDocsDTO> getDocDetailsByChargeRefNo(ManageInvestigationDTO manageInvestigationDTO) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ManageRetainDocsDTO dto;
		List<ManageRetainDocsDTO> manageRetainDocsDTOList = null;

		try {
			con = ConnectionManager.getConnection();

			manageRetainDocsDTOList = new ArrayList<>();

			String query = "SELECT v.vio_doc_cd , v.vio_created_date, s.description,"
					+ " v.vio_doc_invesno, v.chg_rels_release_to, v.chg_rels_id_no, v.chg_rels_special_remarks, v.chg_rels_date  \r\n"
					+ "	FROM public.nt_t_flying_vio_documets v, nt_r_flying_squad_documents s\r\n"
					+ "	 WHERE v.vio_doc_cd = s.code AND v.vio_doc_invesno=? AND vio_release_status ='R'";

			ps = con.prepareStatement(query);
			ps.setString(1, manageInvestigationDTO.getChargeRefCode());

			rs = ps.executeQuery();

			while (rs.next()) {
				dto = new ManageRetainDocsDTO();

				dto.setCode(rs.getString("vio_doc_cd"));
				dto.setCodeDescription(rs.getString("description"));
				dto.setCollectedDate(rs.getString("vio_created_date"));
				dto.setChargeRefNo(rs.getString("vio_doc_invesno"));
				dto.setReleaseTo(rs.getString("chg_rels_release_to"));
				dto.setReleaseId(rs.getString("chg_rels_id_no"));
				dto.setSpecialRemarks(rs.getString("chg_rels_special_remarks"));
				dto.setReleaseDate(rs.getString("chg_rels_date"));

				manageRetainDocsDTOList.add(dto);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return manageRetainDocsDTOList;
	}

	@Override
	public boolean checkReleased(ManageRetainDocsDTO manageRetainDocsDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean check = false;

		try {
			con = ConnectionManager.getConnection();

			String query = " SELECT chg_rels_status " + " FROM public.nt_t_flying_vio_documets "
					+ " WHERE vio_doc_invesno =? AND vio_doc_cd=? ";

			ps = con.prepareStatement(query);
			ps.setString(1, manageRetainDocsDTO.getChargeRefNo());
			ps.setString(2, manageRetainDocsDTO.getCode());

			rs = ps.executeQuery();

			while (rs.next()) {
				if (rs.getString("chg_rels_status") == null) {
					check = false;
				} else {
					check = true;
				}

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return check;
	}

	@Override
	public ManageRetainDocsDTO getReleaseDetailsByDocDetails(ManageRetainDocsDTO manageRetainDocsDTO) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ManageRetainDocsDTO dto = null;

		try {
			con = ConnectionManager.getConnection();

			String query = " SELECT chg_rels_date, chg_rels_release_to, chg_rels_id_no, chg_rels_special_remarks "
					+ " FROM public.nt_t_flying_vio_documets " + " WHERE vio_doc_invesno =? AND vio_doc_cd=? ";

			ps = con.prepareStatement(query);
			ps.setString(1, manageRetainDocsDTO.getChargeRefNo());
			ps.setString(2, manageRetainDocsDTO.getCode());

			rs = ps.executeQuery();

			while (rs.next()) {
				dto = new ManageRetainDocsDTO();

				dto.setReleaseDate(rs.getString("chg_rels_date"));
				dto.setReleaseTo(rs.getString("chg_rels_release_to"));
				dto.setReleaseId(rs.getString("chg_rels_id_no"));
				dto.setSpecialRemarks(rs.getString("chg_rels_special_remarks"));
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
	
	private void insertTaskInquiryRecord(Connection con, ManageInvestigationDTO selectedInvestigation,
			Timestamp timestamp, String status, String function, String funDes) {
		
		try {
			String insertSQL = null;
			PreparedStatement psInsert = null;

			insertSQL = "INSERT INTO public.nt_t_task_inquiry (created_by, created_date, status, bus_no, ref_no, permit_no,function_name,function_des) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
			psInsert = con.prepareStatement(insertSQL);

			psInsert.setString(1, selectedInvestigation.getLoginUser());
			psInsert.setTimestamp(2, timestamp);
			psInsert.setString(3, status);
			psInsert.setString(4, selectedInvestigation.getVehicleNo());
			psInsert.setString(5, selectedInvestigation.getInvReferenceNo());
			psInsert.setString(6, selectedInvestigation.getPermitNo());
			psInsert.setString(7, function);
			psInsert.setString(8, funDes);

			psInsert.executeUpdate();
			con.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void beanLinkMethod(ManageInvestigationDTO manageInvestigationDTO, String des, String funDes) {
		Connection con = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		
		try {
			con = ConnectionManager.getConnection();
			
			insertTaskInquiryRecord(con, manageInvestigationDTO, timestamp, des, "Investigation Management",funDes);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}

}
