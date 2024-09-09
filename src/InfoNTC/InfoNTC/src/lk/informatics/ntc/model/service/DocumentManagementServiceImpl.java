package lk.informatics.ntc.model.service;

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

import lk.informatics.ntc.model.dto.DocumentManagementDTO;
import lk.informatics.ntc.model.dto.RevenueCollectionDTO;
import lk.informatics.ntc.model.exception.ApplicationException;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.ntc.view.util.PropertyReader;
import lk.informatics.roc.utils.common.Utils;

public class DocumentManagementServiceImpl implements DocumentManagementService {

	private static final long serialVersionUID = 1L;

	// Get Status
	public List<String> GetTrasactionType(String typeCode) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<String> transactionList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT description FROM public.nt_r_transaction_type where code IN ('03','04','05','13','14','22','23','21') order by description";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {

				transactionList.add(rs.getString("description"));

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return transactionList;

	}

	public List<String> GetAddDocumentTrasactionType() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<String> transactionList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT * FROM public.nt_r_transaction_type where active ='Y'  order by description\r\n" + "";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {

				transactionList.add(rs.getString("description"));

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return transactionList;

	}

	public List<String> GetDocCode() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<String> docCodeList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String sql = "select code from nt_r_document_type where active='A'";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {

				docCodeList.add(rs.getString("code"));

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return docCodeList;

	}

	public List<String> GetDocDescription() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<String> docDesList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String sql = "select description from nt_r_document_type where active='A'";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {

				docDesList.add(rs.getString("description"));

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return docDesList;

	}

	public String getDescriptionWithCode(DocumentManagementDTO documentManagement) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String code = documentManagement.getDoc_Code();
		String description = null;

		try {

			con = ConnectionManager.getConnection();

			String sql = "select description from nt_r_document_type where code = '" + code + "';";

			ps = con.prepareStatement(sql);

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

	public String getCodeWithDescription(DocumentManagementDTO documentManagement) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String description = documentManagement.getAdd_Doc_Description();
		String code = null;

		try {

			con = ConnectionManager.getConnection();

			String sql = "select code from nt_r_document_type where description = '" + description + "';";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {

				code = rs.getString("code");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return code;
	}

	public List<DocumentManagementDTO> check(DocumentManagementDTO documentManagement, String typeCode) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT doc_transaction_type,doc_code,doc_document_des,doc_mandatory_doc,doc_response_require FROM public.nt_m_document\r\n"
					+ "where doc_transaction_type='" + typeCode + "' and doc_code='" + documentManagement.getDoc_Code()
					+ "' ";

			ps = con.prepareStatement(sql);

			documentManagement.setCheckTrans(null);
			documentManagement.setCheckDocCode(null);
			rs = ps.executeQuery();

			while (rs.next()) {

				documentManagement.setCheckDocCode(rs.getString("doc_code"));
				documentManagement.setCheckDocDes(rs.getString("doc_document_des"));
				documentManagement.setCheckMandatory(rs.getBoolean("doc_mandatory_doc"));
				documentManagement.setCheckResponse(rs.getBoolean("doc_response_require"));
				documentManagement.setCheckTrans(rs.getString("doc_transaction_type"));

				searchList.add(documentManagement);

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

	public int saveDocumentTypes(DocumentManagementDTO documentManagement) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_document");
			documentManagement.setSeq(seqNo);

			String sql = "INSERT INTO public.nt_m_document\r\n"
					+ "(seqno, doc_transaction_type, doc_code, doc_document_des, doc_response_require, doc_mandatory_doc, des_created_by, des_created_date)\r\n"
					+ "VALUES(?,?,?,?,?,?,?,?);";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, documentManagement.getSeq());

			stmt.setString(2, documentManagement.getTransaction_Type_Code());
			stmt.setString(3, documentManagement.getDoc_Code());
			stmt.setString(4, documentManagement.getAdd_Doc_Description());
			stmt.setBoolean(5, documentManagement.isDoc_Respond());
			stmt.setBoolean(6, documentManagement.isDoc_Mandatory());
			stmt.setString(7, documentManagement.getCreated_by());
			stmt.setTimestamp(8, timestamp);

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

	public int updateNewDocTypes(DocumentManagementDTO documentManagementEdit, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		boolean mandatory = documentManagementEdit.isDoc_Mandatory();
		boolean response = documentManagementEdit.isDoc_Respond();
		Long seqNo = documentManagementEdit.getSeq();

		try {
			con = ConnectionManager.getConnection();

			String sql = "update nt_m_document set doc_response_require='" + response + "', doc_mandatory_doc='"
					+ mandatory + "' ,des_modified_by='" + user + "',des_modified_date='" + timestamp
					+ "' where seqno='" + seqNo + "';";

			stmt = con.prepareStatement(sql);

			stmt.executeUpdate();
			con.commit();

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

	public int DeleteDoc(DocumentManagementDTO documentManagementDelete) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		Long seqNo = documentManagementDelete.getSeq();

		try {
			con = ConnectionManager.getConnection();

			String sql = "DELETE FROM public.nt_m_document WHERE seqno='" + seqNo + "';";

			stmt = con.prepareStatement(sql);

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

	public List<DocumentManagementDTO> serachAllDetailsForDoc(String typeCode, String doc_Code, boolean response,
			boolean mandatory, String type) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT a.seqno,a.doc_transaction_type,a.doc_code,b.description, a.doc_document_des, a.doc_response_require, a.doc_mandatory_doc \r\n"
					+ "\r\n"
					+ "FROM public.nt_m_document a left outer join nt_r_transaction_type b on a.doc_transaction_type=b.code\r\n"
					+ "where a.doc_response_require='" + response + "' and a.doc_mandatory_doc='" + mandatory + "'";

			if (!typeCode.isEmpty() && !typeCode.equals(null)) {

				sql = sql + " and a.doc_transaction_type ='" + typeCode + "'";
			}
			if (!doc_Code.isEmpty() && !doc_Code.equals(null)) {
				sql = sql + " and a.doc_code ='" + doc_Code + "'";
			}

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO DocumentManagementSearch = new DocumentManagementDTO();

				DocumentManagementSearch.setSeq(rs.getLong("seqno"));
				DocumentManagementSearch.setTransaction_Type(rs.getString("description"));

				DocumentManagementSearch.setDoc_Code(rs.getString("doc_code"));
				DocumentManagementSearch.setAdd_Doc_Description(rs.getString("doc_document_des"));

				DocumentManagementSearch.setDoc_Mandatory(rs.getBoolean("doc_mandatory_doc"));
				DocumentManagementSearch.setDoc_Respond(rs.getBoolean("doc_response_require"));

				searchList.add(DocumentManagementSearch);

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

	public List<String> GetPermitNoListForNewPermit() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<String> permitList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT pm_permit_no,pm_isnew_permit FROM public.nt_t_pm_application\r\n"
					+ "WHERE  pm_application_no LIKE 'PAP%' and pm_isnew_permit='Y' and pm_permit_no is not null order by pm_permit_no";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {

				permitList.add(rs.getString("pm_permit_no"));
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return permitList;

	}

	public List<String> GetApplicationNoListForNewPermit() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<String> applicationList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT pm_application_no,pm_isnew_permit FROM public.nt_t_pm_application\r\n"
					+ "WHERE  pm_application_no LIKE 'PAP%' and pm_isnew_permit='Y' and pm_application_no is not null order by pm_application_no";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {

				applicationList.add(rs.getString("pm_application_no"));
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return applicationList;

	}

	public List<String> GetApplicationNoListForPermitRenewal() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<String> applicationList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT tsd_app_no FROM public.nt_h_task_his where tsd_task_code='PR200' and tsd_status='O'";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {

				applicationList.add(rs.getString("tsd_app_no"));
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return applicationList;

	}

	public List<String> GetVehicleNoListForPermitRenewal() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<String> applicationList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT tsd_vehicle_no FROM public.nt_h_task_his where tsd_task_code='PR200' and tsd_status='O'";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {

				applicationList.add(rs.getString("tsd_vehicle_no"));
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return applicationList;

	}

	public List<String> GetPermitNoListForPermitRenewal(List<String> applicationList) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<String> permitList = new ArrayList<String>();

		try {

			for (int i = 0; i < applicationList.size(); i++) {

				String applicationNo = applicationList.get(i);
				con = ConnectionManager.getConnection();

				String sql = "SELECT pm_permit_no FROM public.nt_t_pm_application where pm_application_no='"
						+ applicationNo + "'\r\n" + "";

				stmt = con.prepareStatement(sql);
				rs = stmt.executeQuery();
				while (rs.next()) {
					permitList.add(rs.getString("pm_permit_no"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return permitList;

	}

	public List<String> GetJoinApplicationNoListForPermitRenewal() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<String> applicationList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT tsd_app_no FROM public.nt_t_task_det where tsd_task_code='PR200' and tsd_status='O'";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {

				applicationList.add(rs.getString("tsd_app_no"));
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return applicationList;

	}

	public List<String> GetJoinVehicleNoListForPermitRenewal() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<String> vehicleList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT tsd_vehicle_no FROM public.nt_t_task_det where tsd_task_code='PR200' and tsd_status='O'";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {

				vehicleList.add(rs.getString("tsd_vehicle_no"));
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return vehicleList;

	}

	public List<DocumentManagementDTO> mandatoryDocs(String typeCode, String uploadPermitNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String filepath;

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();

			String sql = "select d.apd_document_des, d.apd_doc_code, d.apd_transaction_type, d.apd_file_path\r\n"
					+ "from (select A.apd_document_des,A.apd_doc_code,A.apd_transaction_type,apd_file_path as apd_file_path\r\n"
					+ "from public.nt_t_application_document A\r\n" + "where A.apd_permit_no = '" + uploadPermitNo
					+ "'\r\n" + "and A.apd_doc_type = 'M'\r\n" + "and A.apd_transaction_type = '" + typeCode + "'\r\n"
					+ "\r\n" + "union\r\n" + "\r\n"
					+ "select B.doc_document_des,B.doc_code,B.doc_transaction_type,'X' as apd_file_path\r\n"
					+ "from public.nt_m_document B\r\n"
					+ "WHERE not EXISTS( select * from public.nt_t_application_document A \r\n"
					+ "				where A.apd_doc_code = B.doc_code\r\n" + "				and A.apd_permit_no = '"
					+ uploadPermitNo + "'\r\n" + "				and A.apd_doc_type = 'M'\r\n"
					+ "				and A.apd_transaction_type = '" + typeCode + "' )			\r\n"
					+ "AND B.doc_mandatory_doc = 'true'\r\n" + "and B.doc_transaction_type = '" + typeCode
					+ "') as d order by d.apd_doc_code";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploadDocument = new DocumentManagementDTO();

				uploadDocument.setDoc_Code(rs.getString("apd_doc_code"));
				uploadDocument.setAdd_Doc_Description(rs.getString("apd_document_des"));

				filepath = rs.getString("apd_file_path");

				if (filepath.equals("X")) {
					uploadDocument.setUploadFilePath(null);

				} else {

					uploadDocument.setUploadFilePath(rs.getString("apd_file_path"));
				}
				uploadDocument.setTransaction_Type(rs.getString("apd_transaction_type"));

				searchList.add(uploadDocument);

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

	public List<DocumentManagementDTO> mandatoryDocsForAmendments(String typeCode, String uploadPermitNo,
			String uploadApplicationNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String filepath;

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();

			String sql = "select d.apd_document_des, d.apd_doc_code, d.apd_transaction_type, d.apd_file_path\r\n"
					+ "from (select A.apd_document_des,A.apd_doc_code,A.apd_transaction_type,apd_file_path as apd_file_path\r\n"
					+ "from public.nt_t_application_document A\r\n" + "where A.apd_permit_no = '" + uploadPermitNo
					+ "'\r\n" + "and A.apd_application_no = '" + uploadApplicationNo + "'\r\n"
					+ "and A.apd_doc_type = 'M'\r\n" + "and A.apd_transaction_type = '" + typeCode + "'\r\n" + "\r\n"
					+ "union\r\n" + "\r\n"
					+ "select B.doc_document_des,B.doc_code,B.doc_transaction_type,'X' as apd_file_path\r\n"
					+ "from public.nt_m_document B\r\n"
					+ "WHERE not EXISTS( select * from public.nt_t_application_document A \r\n"
					+ "				where A.apd_doc_code = B.doc_code\r\n" + "				and A.apd_permit_no = '"
					+ uploadPermitNo + "'\r\n" + "				and A.apd_application_no = '" + uploadApplicationNo
					+ "'\r\n" + "				and A.apd_doc_type = 'M'\r\n"
					+ "				and A.apd_transaction_type = '" + typeCode + "' )			\r\n"
					+ "AND B.doc_mandatory_doc = 'true'\r\n" + "and B.doc_transaction_type = '" + typeCode
					+ "') as d order by d.apd_doc_code";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploadDocument = new DocumentManagementDTO();

				uploadDocument.setDoc_Code(rs.getString("apd_doc_code"));
				uploadDocument.setAdd_Doc_Description(rs.getString("apd_document_des"));

				filepath = rs.getString("apd_file_path");

				if (filepath.equals("X")) {
					uploadDocument.setUploadFilePath(null);

				} else {

					uploadDocument.setUploadFilePath(rs.getString("apd_file_path"));
				}
				uploadDocument.setTransaction_Type(rs.getString("apd_transaction_type"));

				searchList.add(uploadDocument);

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

	public List<DocumentManagementDTO> mandatoryDocsForTender(String typeCode, String uploadPermitNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String filepath;

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();

			String sql = "select d.apd_document_des, d.apd_doc_code, d.apd_transaction_type, d.apd_file_path\r\n"
					+ "from (select A.apd_document_des,A.apd_doc_code,A.apd_transaction_type,apd_file_path as apd_file_path\r\n"
					+ "from public.nt_t_application_document A\r\n" + "where A.apd_application_no = '" + uploadPermitNo
					+ "'\r\n" + "and A.apd_doc_type = 'M'\r\n" + "and A.apd_transaction_type = '" + typeCode + "'\r\n"
					+ "\r\n" + "union\r\n" + "\r\n"
					+ "select B.doc_document_des,B.doc_code,B.doc_transaction_type,'X' as apd_file_path\r\n"
					+ "from public.nt_m_document B\r\n"
					+ "WHERE not EXISTS( select * from public.nt_t_application_document A \r\n"
					+ "				where A.apd_doc_code = B.doc_code\r\n"
					+ "				and A.apd_application_no = '" + uploadPermitNo + "'\r\n"
					+ "				and A.apd_doc_type = 'M'\r\n" + "				and A.apd_transaction_type = '"
					+ typeCode + "' )			\r\n" + "AND B.doc_mandatory_doc = 'true'\r\n"
					+ "and B.doc_transaction_type = '" + typeCode + "') as d order by d.apd_doc_code";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploadDocument = new DocumentManagementDTO();

				uploadDocument.setDoc_Code(rs.getString("apd_doc_code"));
				uploadDocument.setAdd_Doc_Description(rs.getString("apd_document_des"));

				filepath = rs.getString("apd_file_path");

				if (filepath.equals("X")) {
					uploadDocument.setUploadFilePath(null);

				} else {

					uploadDocument.setUploadFilePath(rs.getString("apd_file_path"));
				}
				uploadDocument.setTransaction_Type(rs.getString("apd_transaction_type"));

				searchList.add(uploadDocument);

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

	public List<DocumentManagementDTO> optionalDocsForTender(String typeCode, String uploadPermitNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String filepathOp;

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {

			con = ConnectionManager.getConnection();

			String sql = "select d.apd_document_des, d.apd_doc_code, d.apd_transaction_type, d.apd_file_path\r\n"
					+ "from (select A.apd_document_des,A.apd_doc_code,A.apd_transaction_type,apd_file_path as apd_file_path\r\n"
					+ "from public.nt_t_application_document A\r\n" + "where A.apd_application_no = '" + uploadPermitNo
					+ "'\r\n" + "and A.apd_doc_type = 'O'\r\n" + "and A.apd_transaction_type = '" + typeCode + "'\r\n"
					+ "\r\n" + "union\r\n" + "\r\n"
					+ "select B.doc_document_des,B.doc_code,B.doc_transaction_type,'X' as apd_file_path\r\n"
					+ "from public.nt_m_document B\r\n"
					+ "WHERE not EXISTS( select * from public.nt_t_application_document A \r\n"
					+ "				where A.apd_doc_code = B.doc_code\r\n"
					+ "				and A.apd_application_no = '" + uploadPermitNo + "'\r\n"
					+ "				and A.apd_doc_type = 'O'\r\n" + "				and A.apd_transaction_type = '"
					+ typeCode + "' )			\r\n" + "AND B.doc_mandatory_doc = 'false'\r\n"
					+ "and B.doc_transaction_type = '" + typeCode + "') as d order by d.apd_doc_code";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {

				DocumentManagementDTO uploadDocument = new DocumentManagementDTO();
				uploadDocument.setDoc_Code(rs.getString("apd_doc_code"));
				uploadDocument.setAdd_Doc_Description(rs.getString("apd_document_des"));

				filepathOp = rs.getString("apd_file_path");

				if (filepathOp.equals("X")) {
					uploadDocument.setUploadOptionalFilePath(null);
					uploadDocument.setUploadFilePath(null);
				} else {
					uploadDocument.setUploadFilePath(filepathOp);
					uploadDocument.setUploadOptionalFilePath(rs.getString("apd_file_path"));
				}
				uploadDocument.setTransaction_Type(rs.getString("apd_transaction_type"));

				searchList.add(uploadDocument);

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

	public List<DocumentManagementDTO> optionalDocs(String typeCode, String uploadPermitNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String filepathOp;

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {

			con = ConnectionManager.getConnection();

			String sql = "select d.apd_document_des, d.apd_doc_code, d.apd_transaction_type, d.apd_file_path\r\n"
					+ "from (select A.apd_document_des,A.apd_doc_code,A.apd_transaction_type,apd_file_path as apd_file_path\r\n"
					+ "from public.nt_t_application_document A\r\n" + "where A.apd_permit_no = '" + uploadPermitNo
					+ "'\r\n" + "and A.apd_doc_type = 'O'\r\n" + "and A.apd_transaction_type = '" + typeCode + "'\r\n"
					+ "\r\n" + "union\r\n" + "\r\n"
					+ "select B.doc_document_des,B.doc_code,B.doc_transaction_type,'X' as apd_file_path\r\n"
					+ "from public.nt_m_document B\r\n"
					+ "WHERE not EXISTS( select * from public.nt_t_application_document A \r\n"
					+ "				where A.apd_doc_code = B.doc_code\r\n" + "				and A.apd_permit_no = '"
					+ uploadPermitNo + "'\r\n" + "				and A.apd_doc_type = 'O'\r\n"
					+ "				and A.apd_transaction_type = '" + typeCode + "' )			\r\n"
					+ "AND B.doc_mandatory_doc = 'false'\r\n" + "and B.doc_transaction_type = '" + typeCode
					+ "') as d order by d.apd_doc_code";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {

				DocumentManagementDTO uploadDocument = new DocumentManagementDTO();
				uploadDocument.setDoc_Code(rs.getString("apd_doc_code"));
				uploadDocument.setAdd_Doc_Description(rs.getString("apd_document_des"));

				filepathOp = rs.getString("apd_file_path");

				if (filepathOp.equals("X")) {
					uploadDocument.setUploadOptionalFilePath(null);
					uploadDocument.setUploadFilePath(null);
				} else {
					uploadDocument.setUploadFilePath(filepathOp);
					uploadDocument.setUploadOptionalFilePath(rs.getString("apd_file_path"));
				}
				uploadDocument.setTransaction_Type(rs.getString("apd_transaction_type"));

				searchList.add(uploadDocument);

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

	public List<DocumentManagementDTO> optionalDocsForAmendments(String typeCode, String uploadPermitNo,
			String uploadApplicationNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String filepathOp;

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {

			con = ConnectionManager.getConnection();

			String sql = "select d.apd_document_des, d.apd_doc_code, d.apd_transaction_type, d.apd_file_path\r\n"
					+ "from (select A.apd_document_des,A.apd_doc_code,A.apd_transaction_type,apd_file_path as apd_file_path\r\n"
					+ "from public.nt_t_application_document A\r\n" + "where A.apd_permit_no = '" + uploadPermitNo
					+ "'\r\n" + "and A.apd_application_no = '" + uploadApplicationNo + "'\r\n"
					+ "and A.apd_doc_type = 'O'\r\n" + "and A.apd_transaction_type = '" + typeCode + "'\r\n" + "\r\n"
					+ "union\r\n" + "\r\n"
					+ "select B.doc_document_des,B.doc_code,B.doc_transaction_type,'X' as apd_file_path\r\n"
					+ "from public.nt_m_document B\r\n"
					+ "WHERE not EXISTS( select * from public.nt_t_application_document A \r\n"
					+ "				where A.apd_doc_code = B.doc_code\r\n" + "				and A.apd_permit_no = '"
					+ uploadPermitNo + "'\r\n" + "				and A.apd_application_no = '" + uploadApplicationNo
					+ "'\r\n" + "				and A.apd_doc_type = 'O'\r\n"
					+ "				and A.apd_transaction_type = '" + typeCode + "' )			\r\n"
					+ "AND B.doc_mandatory_doc = 'false'\r\n" + "and B.doc_transaction_type = '" + typeCode
					+ "') as d order by d.apd_doc_code";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {

				DocumentManagementDTO uploadDocument = new DocumentManagementDTO();
				uploadDocument.setDoc_Code(rs.getString("apd_doc_code"));
				uploadDocument.setAdd_Doc_Description(rs.getString("apd_document_des"));

				filepathOp = rs.getString("apd_file_path");

				if (filepathOp.equals("X")) {
					uploadDocument.setUploadOptionalFilePath(null);
					uploadDocument.setUploadFilePath(null);
				} else {
					uploadDocument.setUploadFilePath(filepathOp);
					uploadDocument.setUploadOptionalFilePath(rs.getString("apd_file_path"));
				}
				uploadDocument.setTransaction_Type(rs.getString("apd_transaction_type"));

				searchList.add(uploadDocument);

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

	public String getApplicationNoWithPermit(String uploadPermit) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String output = null;
		try {

			con = ConnectionManager.getConnection();

			String sql = "select pm_application_no from nt_t_pm_application where pm_permit_no = '" + uploadPermit
					+ "'";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {

				output = rs.getString("pm_application_no");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return output;
	}

	public String getApplicationNoWithPermitRenewal(String uploadPermit) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String output = null;
		try {

			con = ConnectionManager.getConnection();

			String sql = "select pm_application_no from nt_t_pm_application where pm_permit_no = '" + uploadPermit
					+ "' and pm_isnew_permit='N'";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {

				output = rs.getString("pm_application_no");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return output;
	}

	public String getApplicationNoWithPermitForAmendment(String uploadPermit) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String output = null;
		try {

			con = ConnectionManager.getConnection();

			String sql = "SELECT amd_application_no FROM public.nt_m_amendments where amd_permit_no='" + uploadPermit
					+ "'";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {

				output = rs.getString("amd_application_no");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return output;
	}

	public String getApplicationNoWithPermitForTender(String uploadPermit) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String output = null;
		try {

			con = ConnectionManager.getConnection();

			String sql = "SELECT amd_application_no FROM public.nt_m_amendments where amd_permit_no='" + uploadPermit
					+ "'";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {

				output = rs.getString("amd_application_no");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return output;
	}

	public String getSurveyRequestNoWithSurveyNoforSurvey(String uploadPermit) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String output = null;
		try {

			con = ConnectionManager.getConnection();

			String sql = "SELECT ini_isu_requestno FROM public.nt_t_initiate_survey where ini_surveyno='" + uploadPermit
					+ "'";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {

				output = rs.getString("ini_isu_requestno");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return output;
	}

	public String getPermitNoWithApplication(String uploadApplication) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String output = null;
		try {

			con = ConnectionManager.getConnection();

			String sql = "select pm_permit_no from nt_t_pm_application where pm_application_no = '" + uploadApplication
					+ "'";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {

				output = rs.getString("pm_permit_no");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return output;
	}

	public String getPermitNoWithApplicationforAmendments(String uploadApplication) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String output = null;
		try {

			con = ConnectionManager.getConnection();

			String sql = "SELECT amd_permit_no FROM public.nt_m_amendments where amd_application_no='"
					+ uploadApplication + "'";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {

				output = rs.getString("amd_permit_no");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return output;
	}

	public String getSurveyNoWithRequestNoforSurvey(String uploadApplication) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String output = null;
		try {

			con = ConnectionManager.getConnection();

			String sql = "SELECT ini_surveyno FROM public.nt_t_initiate_survey where ini_isu_requestno='"
					+ uploadApplication + "'";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {

				output = rs.getString("ini_surveyno");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return output;
	}

	public int saveaddnewDocument(String newDocCode, String newDocDes, String createdBy) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "INSERT INTO public.nt_r_document_type\r\n"
					+ "(code, description, active, created_by, created_date)\r\n" + "VALUES('" + newDocCode + "','"
					+ newDocDes + "', 'T','" + createdBy + "','" + timestamp + "');\r\n" + "";

			stmt = con.prepareStatement(sql);

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

	public int saveMandatoryUploads(String filePath, String doc_des, String doc_code, String permitNo,
			String applicationNo, String transaction_Code, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_application_document");

			String sql = "INSERT INTO public.nt_t_application_document\r\n"
					+ "(seqno, apd_transaction_type, apd_application_no, apd_permit_no, apd_doc_code, apd_document_des, apd_file_path, apd_doc_type, apd_created_by, apd_created_date)\r\n"
					+ "VALUES('" + seqNo + "', '" + transaction_Code + "', '" + applicationNo + "', '" + permitNo
					+ "', '" + doc_code + "', '" + doc_des + "', '" + filePath + "', 'M', '" + user + "', '" + timestamp
					+ "');";

			stmt = con.prepareStatement(sql);

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

	public int saveMandatoryUploadsForSisuSariya(String filePath, String doc_des, String doc_code, String permitNo,
			String applicationNo, String transaction_Code, String user, Date expiryDate) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_subsidy_document");

			String expiryDateString = "";
			if (expiryDate != null) {
				expiryDateString = (dateFormat.format(expiryDate));
			} else {
				expiryDateString = null;
			}

			String sql = "INSERT INTO public.nt_t_subsidy_document\r\n"
					+ "(seqno, sud_transaction_type, sud_request_no, sud_doc_code, sud_document_des, sud_file_path, sud_doc_type, sud_created_by, sud_created_date,sud_expiry_date)\r\n"
					+ "VALUES('" + seqNo + "', '" + transaction_Code + "', '" + permitNo + "', '" + doc_code + "', '"
					+ doc_des + "', '" + filePath + "', 'M', '" + user + "', '" + timestamp + "','" + expiryDateString
					+ "');";

			stmt = con.prepareStatement(sql);

			stmt.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
			ConnectionManager.close(stmt);
		}

		return 0;
	}

	public int saveMandatoryUploadsForSisuSariyaPermitHolder(String filePath, String doc_des, String doc_code,
			String permitNo, String applicationNo, String transaction_Code, String user, Date expiryDate) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_subsidy_document");

			String expiryDateString = "";
			if (expiryDate != null) {
				expiryDateString = (dateFormat.format(expiryDate));
			} else {
				expiryDateString = null;
			}

			String sql = "INSERT INTO public.nt_t_subsidy_document\r\n"
					+ "(seqno, sud_transaction_type, sud_request_no,sud_ref_no, sud_doc_code, sud_document_des, sud_file_path, sud_doc_type, sud_created_by, sud_created_date,sud_expiry_date)\r\n"
					+ "VALUES('" + seqNo + "', '" + transaction_Code + "', '" + permitNo + "','" + applicationNo + "','"
					+ doc_code + "', '" + doc_des + "', '" + filePath + "', 'M', '" + user + "', '" + timestamp + "','"
					+ expiryDateString + "');";

			stmt = con.prepareStatement(sql);

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

	public int saveMandatoryUploadsForSisuSariyaAgreementRenewals(String filePath, String doc_des, String doc_code,
			String permitNo, String applicationNo, String transaction_Code, String user, String serviceNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_subsidy_document");

			String sql = "INSERT INTO public.nt_t_subsidy_document\r\n"
					+ "(seqno, sud_transaction_type, sud_request_no,sud_ref_no,sud_service_no,sud_doc_code, sud_document_des, sud_file_path, sud_doc_type, sud_created_by, sud_created_date)\r\n"
					+ "VALUES('" + seqNo + "', '" + transaction_Code + "', '" + permitNo + "','" + applicationNo + "','"
					+ serviceNo + "','" + doc_code + "', '" + doc_des + "', '" + filePath + "', 'M', '" + user + "', '"
					+ timestamp + "');";

			stmt = con.prepareStatement(sql);

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

	public int saveOptionalUploads(String filePath, String doc_des, String doc_code, String permitNo,
			String applicationNo, String transaction_Code, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_application_document");

			String sql = "INSERT INTO public.nt_t_application_document\r\n"
					+ "(seqno, apd_transaction_type, apd_application_no, apd_permit_no, apd_doc_code, apd_document_des, apd_file_path, apd_doc_type, apd_created_by, apd_created_date)\r\n"
					+ "VALUES('" + seqNo + "', '" + transaction_Code + "', '" + applicationNo + "', '" + permitNo
					+ "', '" + doc_code + "', '" + doc_des + "', '" + filePath + "', 'O', '" + user + "', '" + timestamp
					+ "');";

			stmt = con.prepareStatement(sql);

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

	public int saveOptionalUploadsForSisuSariya(String filePath, String doc_des, String doc_code, String permitNo,
			String applicationNo, String transaction_Code, String user, Date expiryDate) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_subsidy_document");

			String expiryDateString = "";
			if (expiryDate != null) {
				expiryDateString = (dateFormat.format(expiryDate));
			} else {
				expiryDateString = null;
			}

			String sql = "INSERT INTO public.nt_t_subsidy_document \r\n"
					+ "(seqno, sud_transaction_type, sud_request_no, sud_doc_code, sud_document_des, sud_file_path, sud_doc_type, sud_created_by, sud_created_date,sud_expiry_date)\r\n"
					+ "VALUES('" + seqNo + "', '" + transaction_Code + "', '" + permitNo + "', '" + doc_code + "', '"
					+ doc_des + "', '" + filePath + "', 'O', '" + user + "', '" + timestamp + "','" + expiryDateString
					+ "');";

			stmt = con.prepareStatement(sql);

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

	public int saveOptionalUploadsForSisuSariyaPermitHolder(String filePath, String doc_des, String doc_code,
			String permitNo, String applicationNo, String transaction_Code, String user, Date expiryDate) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_subsidy_document");

			String expiryDateString = "";
			if (expiryDate != null) {
				expiryDateString = (dateFormat.format(expiryDate));
			} else {
				expiryDateString = null;
			}

			String sql = "INSERT INTO public.nt_t_subsidy_document \r\n"
					+ "(seqno, sud_transaction_type, sud_request_no,sud_ref_no, sud_doc_code, sud_document_des, sud_file_path, sud_doc_type, sud_created_by, sud_created_date,sud_expiry_date)\r\n"
					+ "VALUES('" + seqNo + "', '" + transaction_Code + "', '" + permitNo + "','" + applicationNo
					+ "', '" + doc_code + "', '" + doc_des + "', '" + filePath + "', 'O', '" + user + "', '" + timestamp
					+ "','" + expiryDateString + "');";

			stmt = con.prepareStatement(sql);

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

	public int saveOptionalUploadsForSisuSariyaAgreementRenewals(String filePath, String doc_des, String doc_code,
			String permitNo, String applicationNo, String transaction_Code, String user, String serviceNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_subsidy_document");

			String sql = "INSERT INTO public.nt_t_subsidy_document \r\n"
					+ "(seqno, sud_transaction_type, sud_request_no,sud_ref_no,sud_service_no, sud_doc_code, sud_document_des, sud_file_path, sud_doc_type, sud_created_by, sud_created_date)\r\n"
					+ "VALUES('" + seqNo + "', '" + transaction_Code + "', '" + permitNo + "','" + applicationNo + "','"
					+ serviceNo + "','" + doc_code + "', '" + doc_des + "', '" + filePath + "', 'O', '" + user + "', '"
					+ timestamp + "');";

			stmt = con.prepareStatement(sql);

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

	public String getTransactionCode(String transaction) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String output = null;
		try {

			con = ConnectionManager.getConnection();

			String sql = "select code from nt_r_transaction_type where description =?";

			ps = con.prepareStatement(sql);
			ps.setString(1, transaction.toUpperCase());
			rs = ps.executeQuery();

			while (rs.next()) {

				output = rs.getString("code");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return output;
	}

	public String getFilePath(String filePath, String doc_des, String permitNo, String transaction_Code,
			String doc_code, String appNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String output = null;
		try {

			con = ConnectionManager.getConnection();

			String sql = "select  a.doc_code, a.doc_document_des,a.doc_mandatory_doc,b.apd_file_path,b.apd_transaction_type,b.apd_permit_no from nt_m_document a left outer join nt_t_application_document b on a.doc_code = b.apd_doc_code where a.doc_mandatory_doc='true' and b.apd_transaction_type='"
					+ transaction_Code + "' and a.doc_code = '" + doc_code + "' and b.apd_permit_no = '" + permitNo
					+ "' and b.apd_application_no='" + appNo + "'";
			;

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {

				output = rs.getString("apd_file_path");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return output;
	}

	public String getFilePathForSisuSariya(String filePath, String doc_des, String permitNo, String transaction_Code,
			String doc_code, String appNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String output = null;
		try {

			con = ConnectionManager.getConnection();

			String sql = "select  a.doc_code, a.doc_document_des,a.doc_mandatory_doc,b.sud_file_path,b.sud_transaction_type,b.sud_request_no\r\n"
					+ "from nt_m_document a left outer join nt_t_subsidy_document_version b on a.doc_code = b.sud_doc_code \r\n"
					+ "where a.doc_mandatory_doc='true' and b.sud_transaction_type='" + transaction_Code
					+ "' and a.doc_code ='" + doc_code + "'  and b.sud_request_no ='" + permitNo + "'  ";
			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {

				output = rs.getString("sud_file_path");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return output;
	}

	public String getFilePathForSisuSariyaPermitHolder(String filePath, String doc_des, String permitNo,
			String transaction_Code, String doc_code, String appNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String output = null;
		try {

			con = ConnectionManager.getConnection();

			String sql = "select  a.doc_code, a.doc_document_des,a.doc_mandatory_doc,b.sud_file_path,b.sud_transaction_type,b.sud_request_no,b.sud_ref_no\r\n"
					+ "from nt_m_document a left outer join nt_t_subsidy_document_version b on a.doc_code = b.sud_doc_code \r\n"
					+ "where a.doc_mandatory_doc='true' and b.sud_transaction_type='" + transaction_Code
					+ "' and a.doc_code ='" + doc_code + "'  and b.sud_request_no ='" + permitNo
					+ "' and b.sud_ref_no='" + appNo + "'  ";
			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {

				output = rs.getString("sud_file_path");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return output;
	}

	public String getFilePathForSisuSariyaAgreementRenewals(String filePath, String doc_des, String permitNo,
			String transaction_Code, String doc_code, String appNo, String serviceNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String output = null;
		try {

			con = ConnectionManager.getConnection();

			String sql = "select  a.doc_code, a.doc_document_des,a.doc_mandatory_doc,b.sud_file_path,b.sud_transaction_type,b.sud_request_no,b.sud_ref_no\r\n"
					+ "from nt_m_document a left outer join nt_t_subsidy_document_version b on a.doc_code = b.sud_doc_code \r\n"
					+ "where a.doc_mandatory_doc='true' and b.sud_transaction_type='" + transaction_Code
					+ "' and a.doc_code ='" + doc_code + "'  and b.sud_request_no ='" + permitNo
					+ "' and b.sud_ref_no='" + appNo + "' and b.sud_service_no='" + serviceNo + "'  ";
			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {

				output = rs.getString("sud_file_path");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return output;
	}

	public String getOptionalFilePathForSisuSariya(String filePath, String doc_des, String permitNo,
			String transaction_Code, String doc_code, String appNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String output = null;
		try {

			con = ConnectionManager.getConnection();

			String sql = "select  a.doc_code, a.doc_document_des,a.doc_mandatory_doc,b.sud_file_path,b.sud_transaction_type,b.sud_request_no\r\n"
					+ "from nt_m_document a left outer join nt_t_subsidy_document_version b on a.doc_code = b.sud_doc_code \r\n"
					+ "where a.doc_mandatory_doc='false' and b.sud_transaction_type='" + transaction_Code
					+ "' and a.doc_code ='" + doc_code + "'  and b.sud_request_no ='" + permitNo + "'  ";
			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {

				output = rs.getString("sud_file_path");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return output;
	}

	public String getOptionalFilePathForSisuSariyaPermitHolder(String filePath, String doc_des, String permitNo,
			String transaction_Code, String doc_code, String appNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String output = null;
		try {

			con = ConnectionManager.getConnection();

			String sql = "select  a.doc_code, a.doc_document_des,a.doc_mandatory_doc,b.sud_file_path,b.sud_transaction_type,b.sud_request_no,b.sud_ref_no\r\n"
					+ "from nt_m_document a left outer join nt_t_subsidy_document_version b on a.doc_code = b.sud_doc_code \r\n"
					+ "where a.doc_mandatory_doc='false' and b.sud_transaction_type='" + transaction_Code
					+ "' and a.doc_code ='" + doc_code + "'  and b.sud_request_no ='" + permitNo
					+ "' and b.sud_ref_no='" + appNo + "'  ";
			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {

				output = rs.getString("sud_file_path");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return output;
	}

	public String getOptionalFilePathForSisuSariyaAgreementRenewals(String filePath, String doc_des, String permitNo,
			String transaction_Code, String doc_code, String appNo, String serviceNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String output = null;
		try {

			con = ConnectionManager.getConnection();

			String sql = "select  a.doc_code, a.doc_document_des,a.doc_mandatory_doc,b.sud_file_path,b.sud_transaction_type,b.sud_request_no,b.sud_ref_no\r\n"
					+ "from nt_m_document a left outer join nt_t_subsidy_document_version b on a.doc_code = b.sud_doc_code \r\n"
					+ "where a.doc_mandatory_doc='false' and b.sud_transaction_type='" + transaction_Code
					+ "' and a.doc_code ='" + doc_code + "'  and b.sud_request_no ='" + permitNo
					+ "' and b.sud_ref_no='" + appNo + "'and b.sud_service_no='" + serviceNo + "'  ";
			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {

				output = rs.getString("sud_file_path");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return output;
	}

	public String getOptionalFilePath(String filePath, String doc_des, String permitNo, String transaction_Code,
			String appNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String output = null;
		try {

			con = ConnectionManager.getConnection();

			String sql = "select  a.doc_code, a.doc_document_des,a.doc_mandatory_doc,b.apd_file_path,b.apd_transaction_type,b.apd_permit_no from nt_m_document a left outer join nt_t_application_document b on a.doc_code = b.apd_doc_code where a.doc_mandatory_doc='false' and b.apd_transaction_type='"
					+ transaction_Code + "' and a.doc_document_des = '" + doc_des + "' and b.apd_permit_no = '"
					+ permitNo + "'and b.apd_application_no='" + appNo + "'";
			;

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {

				output = rs.getString("apd_file_path");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return output;
	}

	public String getAddNewDocFilePath(String filePath, String doc_des, String permitNo, String transaction_Code) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String output = null;
		try {

			con = ConnectionManager.getConnection();

			String sql = "SELECT apd_file_path FROM public.nt_t_application_document where apd_permit_no='" + permitNo
					+ "' and apd_document_des='" + doc_des + "' and apd_transaction_type='" + transaction_Code
					+ "' and apd_doc_type='O'";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {

				output = rs.getString("apd_file_path");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return output;
	}

	public String getAddNewDocUserManagementFilePath(String filePath, String doc_des, String empNo,
			String transaction_Code) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String output = null;
		try {

			con = ConnectionManager.getConnection();

			String sql = "SELECT emp_file_path FROM public.nt_t_application_document where emp_ref_no='" + empNo
					+ "' and emp_document_des='" + doc_des + "' and emp_transaction_type='" + transaction_Code
					+ "' and emp_doc_type='O'";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {

				output = rs.getString("emp_file_path");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return output;
	}

	public int replaceDoc(String doc_des, String permitNo, String transaction_Code, String user, String docuString) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_application_document  \r\n" + "SET  apd_modified_by='" + user
					+ "', apd_modified_date='" + timestamp + "', apd_file_path = '" + docuString + "'  \r\n"
					+ "where apd_permit_no ='" + permitNo + "' and apd_transaction_type='" + transaction_Code
					+ "' and apd_document_des ='" + doc_des + "'and apd_doc_type='M'    ;\r\n" + "";

			stmt = con.prepareStatement(sql);

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

	public int replaceDocForSisuSariya(String doc_des, String permitNo, String transaction_Code, String user,
			String docuString, Date expiryDate) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();

			String expiryDateString = "";
			if (expiryDate != null) {
				expiryDateString = (dateFormat.format(expiryDate));
			} else {
				expiryDateString = null;
			}

			String sql = "UPDATE public.nt_t_subsidy_document  \r\n" + "SET  sud_modified_by='" + user
					+ "', sud_modified_date='" + timestamp + "', sud_expiry_date='" + expiryDateString
					+ "', sud_file_path = '" + docuString + "'  \r\n" + "where sud_request_no ='" + permitNo
					+ "' and sud_transaction_type='" + transaction_Code + "' and sud_document_des ='" + doc_des
					+ "'and sud_doc_type='M'    ;\r\n" + "";

			stmt = con.prepareStatement(sql);

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

	public int replaceDocForSisuSariyaPermitHolder(String doc_des, String permitNo, String transaction_Code,
			String user, String docuString, String refNo, Date expiryDate) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();

			String expiryDateString = "";
			if (expiryDate != null) {
				expiryDateString = (dateFormat.format(expiryDate));
			} else {
				expiryDateString = null;
			}

			String sql = "UPDATE public.nt_t_subsidy_document  \r\n" + "SET  sud_modified_by='" + user
					+ "', sud_modified_date='" + timestamp + "', sud_expiry_date='" + expiryDateString
					+ "' , sud_file_path = '" + docuString + "'  \r\n" + "where sud_request_no ='" + permitNo
					+ "'and sud_ref_no = '" + refNo + "' and sud_transaction_type='" + transaction_Code
					+ "' and sud_document_des ='" + doc_des + "'and sud_doc_type='M'    ;\r\n" + "";

			stmt = con.prepareStatement(sql);

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

	public int replaceDocForSisuSariyaAgreementRenwals(String doc_des, String permitNo, String transaction_Code,
			String user, String docuString, String refNo, String serviceNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_subsidy_document  \r\n" + "SET  sud_modified_by='" + user
					+ "', sud_modified_date='" + timestamp + "', sud_file_path = '" + docuString + "'  \r\n"
					+ "where sud_request_no ='" + permitNo + "'and sud_ref_no = '" + refNo + "' and sud_service_no='"
					+ serviceNo + "' and sud_transaction_type='" + transaction_Code + "' and sud_document_des ='"
					+ doc_des + "'and sud_doc_type='M'    ;\r\n" + "";

			stmt = con.prepareStatement(sql);

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

	public int replaceOptionalDoc(String doc_des, String permitNo, String transaction_Code, String user,
			String docuString) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_application_document  \r\n" + "SET  apd_modified_by='" + user
					+ "', apd_modified_date='" + timestamp + "',apd_file_path = '" + docuString + "'      \r\n"
					+ "where apd_permit_no ='" + permitNo + "' and apd_transaction_type='" + transaction_Code
					+ "' and apd_document_des ='" + doc_des + "' and apd_doc_type='O'  ;\r\n" + "";

			stmt = con.prepareStatement(sql);

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

	public int replaceOptionalDocForSisuSariya(String doc_des, String permitNo, String transaction_Code, String user,
			String docuString, Date expiryDate) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();

			String expiryDateString = "";
			if (expiryDate != null) {
				expiryDateString = (dateFormat.format(expiryDate));
			} else {
				expiryDateString = null;
			}

			String sql = "UPDATE public.nt_t_subsidy_document  \r\n" + "SET  sud_modified_by='" + user
					+ "', sud_modified_date='" + timestamp + "', sud_expiry_date='" + expiryDateString
					+ "',sud_file_path = '" + docuString + "'      \r\n" + "where sud_request_no ='" + permitNo
					+ "' and sud_transaction_type='" + transaction_Code + "' and sud_document_des ='" + doc_des
					+ "' and sud_doc_type='O'  ;\r\n" + "";

			stmt = con.prepareStatement(sql);

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

	public int replaceOptionalDocForSisuSariyaPermitHolder(String doc_des, String permitNo, String transaction_Code,
			String user, String docuString, String refNo, Date expiryDate) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();

			String expiryDateString = "";
			if (expiryDate != null) {
				expiryDateString = (dateFormat.format(expiryDate));
			} else {
				expiryDateString = null;
			}

			String sql = "UPDATE public.nt_t_subsidy_document  \r\n" + "SET  sud_modified_by='" + user
					+ "', sud_modified_date='" + timestamp + "', sud_expiry_date='" + expiryDateString
					+ "',sud_file_path = '" + docuString + "'      \r\n" + "where sud_request_no ='" + permitNo
					+ "'and sud_ref_no='" + refNo + "' and sud_transaction_type='" + transaction_Code
					+ "' and sud_document_des ='" + doc_des + "' and sud_doc_type='O'  ;\r\n" + "";

			stmt = con.prepareStatement(sql);

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

	public int replaceOptionalDocForSisuSariyaAgreementRenewals(String doc_des, String permitNo,
			String transaction_Code, String user, String docuString, String refNo, String serviceNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_subsidy_document  \r\n" + "SET  sud_modified_by='" + user
					+ "', sud_modified_date='" + timestamp + "',sud_file_path = '" + docuString + "'      \r\n"
					+ "where sud_request_no ='" + permitNo + "'and sud_ref_no='" + refNo + "'and sud_service_no='"
					+ serviceNo + "' and sud_transaction_type='" + transaction_Code + "' and sud_document_des ='"
					+ doc_des + "' and sud_doc_type='O'  ;\r\n" + "";

			stmt = con.prepareStatement(sql);

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

	public String getTransactionCodeForAddDocument(String type) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String output = null;
		try {

			con = ConnectionManager.getConnection();

			String sql = "select code from nt_r_transaction_type where description =?";

			ps = con.prepareStatement(sql);
			ps.setString(1, type.toUpperCase());
			rs = ps.executeQuery();

			while (rs.next()) {

				output = rs.getString("code");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return output;
	}

	public int DeleteOptionalDoc(String permitNo, String applicationNo, String transaction_Code, String docCode,
			String path) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "DELETE FROM public.nt_t_application_document\r\n" + "WHERE apd_transaction_type='"
					+ transaction_Code + "' AND apd_permit_no='" + permitNo + "' AND apd_doc_code='" + docCode
					+ "' AND apd_file_path='" + path + "' AND apd_doc_type='O';\r\n" + "";

			stmt = con.prepareStatement(sql);

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

	public List<DocumentManagementDTO> mandatoryViewDocs(String typeCode, String uploadPermitNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT apd_transaction_type, apd_permit_no, apd_doc_code, apd_document_des, apd_file_path, apd_doc_type\r\n"
					+ "FROM public.nt_t_application_document\r\n" + "where apd_transaction_type='" + typeCode
					+ "' and apd_permit_no='" + uploadPermitNo + "' and apd_doc_type='M' order by  apd_application_no desc; ";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploadDocument = new DocumentManagementDTO();

				uploadDocument.setDoc_Code(rs.getString("apd_doc_code"));
				uploadDocument.setAdd_Doc_Description(rs.getString("apd_document_des"));
				uploadDocument.setUploadFilePath(rs.getString("apd_file_path"));
				uploadDocument.setTransaction_Type(rs.getString("apd_transaction_type"));

				searchList.add(uploadDocument);

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

	public List<DocumentManagementDTO> RenewalApplicationList(String uploadPermitNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT pm_application_no FROM public.nt_t_pm_application where pm_permit_no='"
					+ uploadPermitNo + "'\r\n" + "";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploadDocument = new DocumentManagementDTO();

				uploadDocument.setDoc_App(rs.getString("pm_application_no"));

				searchList.add(uploadDocument);

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

	public List<DocumentManagementDTO> ConfirmRenewalApplicationList(List<DocumentManagementDTO> ApplicationList) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();

			String sql = "";

			for (int i = 0; i < ApplicationList.size(); i++) {

				sql = "select distinct tsd_task_code FROM public.nt_h_task_his \r\n" + "where tsd_app_no='"
						+ ApplicationList.get(i).getDoc_App() + "'";

				ps = con.prepareStatement(sql);

				rs = ps.executeQuery();

				while (rs.next()) {
					DocumentManagementDTO uploadDocument = new DocumentManagementDTO();

					String task_code = rs.getString("tsd_task_code");

					if (task_code.equals("PM101") || task_code.equals("PR200")) {

						uploadDocument.setDoc_App(ApplicationList.get(i).getDoc_App());
						searchList.add(uploadDocument);

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

		return searchList;
	}

	public List<DocumentManagementDTO> mandatoryUserManagementViewDocs(String typeCode, String uploadEmpNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT emp_transaction_type, emp_ref_no, emp_doc_code, emp_document_des, emp_file_path, emp_doc_type\r\n"
					+ "FROM public.nt_t_employee_document \r\n" + "where emp_transaction_type='" + typeCode
					+ "' and emp_ref_no='" + uploadEmpNo + "' and emp_doc_type='M';";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploadDocument = new DocumentManagementDTO();

				uploadDocument.setDoc_Code(rs.getString("emp_doc_code"));
				uploadDocument.setAdd_Doc_Description(rs.getString("emp_document_des"));
				uploadDocument.setUploadFilePath(rs.getString("emp_file_path"));
				uploadDocument.setTransaction_Type(rs.getString("emp_transaction_type"));

				searchList.add(uploadDocument);

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

	public List<DocumentManagementDTO> optionalUserManagementViewDocs(String typeCode, String uploadEmpNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT emp_transaction_type, emp_ref_no, emp_doc_code, emp_document_des, emp_file_path, emp_doc_type\r\n"
					+ "FROM public.nt_t_employee_document \r\n" + "where emp_transaction_type='" + typeCode
					+ "' and emp_ref_no='" + uploadEmpNo + "' and emp_doc_type='O';";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploadDocument = new DocumentManagementDTO();

				uploadDocument.setDoc_Code(rs.getString("emp_doc_code"));
				uploadDocument.setAdd_Doc_Description(rs.getString("emp_document_des"));
				uploadDocument.setUploadOptionalFilePath(rs.getString("emp_file_path"));
				uploadDocument.setUploadFilePath(rs.getString("emp_file_path"));
				uploadDocument.setTransaction_Type(rs.getString("emp_transaction_type"));

				searchList.add(uploadDocument);

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

	public List<DocumentManagementDTO> optionalViewDocs(String typeCode, String uploadPermitNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT apd_transaction_type, apd_permit_no, apd_doc_code, apd_document_des, apd_file_path, apd_doc_type\r\n"
					+ "FROM public.nt_t_application_document\r\n" + "where apd_transaction_type='" + typeCode
					+ "' and apd_permit_no='" + uploadPermitNo + "' and apd_doc_type='O' order by  apd_application_no desc; ";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploadDocument = new DocumentManagementDTO();

				uploadDocument.setDoc_Code(rs.getString("apd_doc_code"));
				uploadDocument.setAdd_Doc_Description(rs.getString("apd_document_des"));
				uploadDocument.setUploadOptionalFilePath(rs.getString("apd_file_path"));
				uploadDocument.setUploadFilePath(rs.getString("apd_file_path"));
				uploadDocument.setTransaction_Type(rs.getString("apd_transaction_type"));

				searchList.add(uploadDocument);

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

	public boolean getResponse(String doc_Code, String typeCode, boolean doc_Mandatory) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean output = false;
		try {

			con = ConnectionManager.getConnection();

			String sql = "SELECT doc_response_require FROM public.nt_m_document where doc_transaction_type='" + typeCode
					+ "' and doc_code='" + doc_Code + "'";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {

				output = rs.getBoolean("doc_response_require");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return output;
	}

	public List<DocumentManagementDTO> getPermitHolderInfo(String applicationNo,
			DocumentManagementDTO documentPermitInfo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<DocumentManagementDTO> permitInfo = new ArrayList<DocumentManagementDTO>();

		try {

			con = ConnectionManager.getConnection();

			String sql = "select pmo_application_no, pmo_permit_no, pmo_title, pmo_full_name,pmo_mobile_no FROM public.nt_t_pm_vehi_owner where pmo_application_no='"
					+ applicationNo + "';";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {

				documentPermitInfo.setPermitMobileNum(rs.getString("pmo_mobile_no"));
				documentPermitInfo.setPermitName(rs.getString("pmo_full_name"));
				documentPermitInfo.setPermitTitle(rs.getString("pmo_title"));

				permitInfo.add(documentPermitInfo);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return permitInfo;

	}

	public int saveAlertMessage(DocumentManagementDTO documentPermitInfo, String alertMessage) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_pending_alerts");

			String sql = "INSERT INTO public.nt_t_pending_alerts\r\n"
					+ "(seq, receipient_mobileno, sms_message, submited_date,message_subject, alert_type)\r\n"
					+ "VALUES('" + seqNo + "','" + documentPermitInfo.getPermitMobileNum() + "','" + alertMessage
					+ "','" + timestamp + "','Document Upload','sms');";

			stmt = con.prepareStatement(sql);

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

	public int getSelectedRow(Long seq, DocumentManagementDTO documentManagement) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			String sql = "SELECT seqno,doc_mandatory_doc,doc_response_require FROM public.nt_m_document\r\n"
					+ "where seqno='" + seq + "'";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				documentManagement.setDoc_Mandatory(rs.getBoolean("doc_mandatory_doc"));
				documentManagement.setDoc_Respond(rs.getBoolean("doc_response_require"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return 0;

	}

	public int getDocumentDetailsCode(DocumentManagementDTO documentManagement, String newDocCode, String newDocDes) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			String sql = "SELECT code,description FROM public.nt_r_document_type where code='" + newDocCode + "'";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {

				documentManagement.setAddCheckDocCode(rs.getString("code"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return 0;

	}

	public int getDocumentDetailsDes(DocumentManagementDTO documentManagement, String newDocCode, String newDocDes) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			String sql = "SELECT code,description FROM public.nt_r_document_type where description='" + newDocDes + "'";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {

				documentManagement.setAddCheckDocDes(rs.getString("description"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return 0;

	}

	public int updateAddNewDocument(String documentCode) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			String sql = "update nt_r_document_type set active='A' where code='" + documentCode + "'";

			stmt = con.prepareStatement(sql);

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

	public int DeleteTemp() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "delete from nt_r_document_type where active='T'";

			stmt = con.prepareStatement(sql);

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

	public String checkDocCode(String docCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String output = "";

		try {

			con = ConnectionManager.getConnection();

			String sql = "SELECT doc_document_des FROM public.nt_m_document where doc_code='" + docCode + "'";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {

				output = rs.getString("doc_document_des");

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return output;

	}

	public int DeleteTempInDelete(String docCode) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "delete from nt_r_document_type where code='" + docCode + "'";

			stmt = con.prepareStatement(sql);

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

	public String checkDocCodeWhenUploading(String documentCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String output = "";

		try {

			con = ConnectionManager.getConnection();

			String sql = "SELECT code,description FROM public.nt_r_document_type where code='" + documentCode + "'";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {

				output = rs.getString("description");

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return output;

	}

	public int saveaddnewDocumentWhenUploading(String code, String description, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "INSERT INTO public.nt_r_document_type\r\n"
					+ "(code, description, active, created_by, created_date)\r\n" + "VALUES('" + code + "','"
					+ description + "', 'A','" + user + "','" + timestamp + "');\r\n" + "";

			stmt = con.prepareStatement(sql);

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

	public List<DocumentManagementDTO> mandatoryDocsForUserManagement(String typeCode, String empNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String filepath;

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();

			String sql = "select d.emp_document_des, d.emp_doc_code, d.emp_transaction_type, d.emp_file_path \r\n"
					+ "from (select A.emp_document_des,A.emp_doc_code,A.emp_transaction_type,emp_file_path as emp_file_path\r\n"
					+ "from public.nt_t_employee_document A \r\n" + "where A.emp_ref_no = '" + empNo + "'\r\n"
					+ "and A.emp_doc_type = 'M'\r\n" + "and A.emp_transaction_type = '" + typeCode + "'\r\n" + "\r\n"
					+ "union\r\n" + "\r\n"
					+ "select B.doc_document_des,B.doc_code,B.doc_transaction_type,'X' as emp_file_path\r\n"
					+ "from public.nt_m_document B \r\n"
					+ "WHERE not EXISTS( select * from public.nt_t_employee_document A \r\n"
					+ "								where A.emp_doc_code = B.doc_code\r\n"
					+ "									and A.emp_ref_no = '" + empNo + "'\r\n"
					+ "							and A.emp_doc_type = 'M'\r\n"
					+ "								and A.emp_transaction_type = '" + typeCode + "' )			\r\n"
					+ "					AND B.doc_mandatory_doc = 'true'\r\n"
					+ "					and B.doc_transaction_type = '" + typeCode
					+ "') as d order by d.emp_doc_code\r\n" + "";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploadDocument = new DocumentManagementDTO();

				uploadDocument.setDoc_Code(rs.getString("emp_doc_code"));
				uploadDocument.setAdd_Doc_Description(rs.getString("emp_document_des"));

				filepath = rs.getString("emp_file_path");

				if (filepath.equals("X")) {
					uploadDocument.setUploadFilePath(null);

				} else {

					uploadDocument.setUploadFilePath(rs.getString("emp_file_path"));
				}
				uploadDocument.setTransaction_Type(rs.getString("emp_transaction_type"));

				searchList.add(uploadDocument);

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

	public List<DocumentManagementDTO> optionalDocsForUserManagement(String typeCode, String empNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String filepath;

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();

			String sql = "select d.emp_document_des, d.emp_doc_code, d.emp_transaction_type, d.emp_file_path \r\n"
					+ "from (select A.emp_document_des,A.emp_doc_code,A.emp_transaction_type,emp_file_path as emp_file_path\r\n"
					+ "from public.nt_t_employee_document A \r\n" + "where A.emp_ref_no = '" + empNo + "'\r\n"
					+ "and A.emp_doc_type = 'O'\r\n" + "and A.emp_transaction_type = '" + typeCode + "'\r\n" + "\r\n"
					+ "union\r\n" + "\r\n"
					+ "select B.doc_document_des,B.doc_code,B.doc_transaction_type,'X' as emp_file_path\r\n"
					+ "from public.nt_m_document B \r\n"
					+ "WHERE not EXISTS( select * from public.nt_t_employee_document A \r\n"
					+ "								where A.emp_doc_code = B.doc_code\r\n"
					+ "									and A.emp_ref_no = '" + empNo + "'\r\n"
					+ "							and A.emp_doc_type = 'O'\r\n"
					+ "								and A.emp_transaction_type = '" + typeCode + "' )			\r\n"
					+ "					AND B.doc_mandatory_doc = 'false'\r\n"
					+ "					and B.doc_transaction_type = '" + typeCode
					+ "') as d order by d.emp_doc_code\r\n" + "";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploadDocument = new DocumentManagementDTO();

				uploadDocument.setDoc_Code(rs.getString("emp_doc_code"));
				uploadDocument.setAdd_Doc_Description(rs.getString("emp_document_des"));

				filepath = rs.getString("emp_file_path");

				if (filepath.equals("X")) {
					uploadDocument.setUploadOptionalFilePath(null);
					uploadDocument.setUploadFilePath(null);
				} else {
					uploadDocument.setUploadFilePath(filepath);
					uploadDocument.setUploadOptionalFilePath(rs.getString("emp_file_path"));
				}
				uploadDocument.setTransaction_Type(rs.getString("emp_transaction_type"));

				searchList.add(uploadDocument);

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

	public List<DocumentManagementDTO> mandatoryDocsForSisuSariya(String typeCode, String empNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String filepath;

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {

			con = ConnectionManager.getConnection();

			String sql = "select d.sud_document_des, d.sud_doc_code, d.sud_transaction_type, d.sud_file_path \r\n"
					+ "from (select A.sud_document_des,A.sud_doc_code,A.sud_transaction_type,sud_file_path as sud_file_path\r\n"
					+ "from public.nt_t_subsidy_document A \r\n" + "where A.sud_request_no = '" + empNo + "'\r\n"
					+ "and A.sud_ref_no is null \r\n" + "and A.sud_service_no is null \r\n"
					+ "and A.sud_doc_type = 'M'\r\n" + "and A.sud_transaction_type = '" + typeCode + "'\r\n" + "\r\n"
					+ "union\r\n" + "\r\n"
					+ "select B.doc_document_des,B.doc_code,B.doc_transaction_type,'X' as sud_file_path\r\n"
					+ "from public.nt_m_document B \r\n"
					+ "WHERE not EXISTS( select * from public.nt_t_subsidy_document A \r\n"
					+ "								where A.sud_doc_code = B.doc_code\r\n"
					+ "									and A.sud_request_no = '" + empNo + "'\r\n"
					+ "									and A.sud_ref_no is null \r\n"
					+ "									and A.sud_service_no is null \r\n"
					+ "									and A.sud_doc_type = 'M'\r\n"
					+ "								and A.sud_transaction_type = '" + typeCode + "' )			\r\n"
					+ "					AND B.doc_mandatory_doc = 'true'\r\n"
					+ "					and B.doc_transaction_type = '" + typeCode
					+ "') as d order by d.sud_doc_code\r\n" + "";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploadDocument = new DocumentManagementDTO();

				uploadDocument.setDoc_Code(rs.getString("sud_doc_code"));
				uploadDocument.setAdd_Doc_Description(rs.getString("sud_document_des"));

				filepath = rs.getString("sud_file_path");

				if (filepath.equals("X")) {
					uploadDocument.setUploadFilePath(null);

				} else {
					
					uploadDocument.setUploadFilePath(rs.getString("sud_file_path"));
				}
				uploadDocument.setTransaction_Type(rs.getString("sud_transaction_type"));

				searchList.add(uploadDocument);

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

	public List<DocumentManagementDTO> optionalDocsForSisuSariya(String typeCode, String empNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String filepath;

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();

			String sql = "select d.sud_document_des, d.sud_doc_code, d.sud_transaction_type, d.sud_file_path \r\n"
					+ "from (select A.sud_document_des,A.sud_doc_code,A.sud_transaction_type,sud_file_path as sud_file_path\r\n"
					+ "from public.nt_t_subsidy_document A \r\n" + "where A.sud_request_no = '" + empNo + "'\r\n"
					+ "and A.sud_ref_no is null \r\n" + "and A.sud_service_no is null \r\n"
					+ "and A.sud_doc_type = 'O'\r\n" + "and A.sud_transaction_type = '" + typeCode + "'\r\n" + "\r\n"
					+ "union\r\n" + "\r\n"
					+ "select B.doc_document_des,B.doc_code,B.doc_transaction_type,'X' as sud_file_path\r\n"
					+ "from public.nt_m_document B \r\n"
					+ "WHERE not EXISTS( select * from public.nt_t_subsidy_document A \r\n"
					+ "								where A.sud_doc_code = B.doc_code\r\n"
					+ "									and A.sud_request_no = '" + empNo + "'\r\n"
					+ "									and A.sud_ref_no is null \r\n"
					+ "									and A.sud_service_no is null \r\n"
					+ "									and A.sud_doc_type = 'O'\r\n"
					+ "								and A.sud_transaction_type = '" + typeCode + "' )			\r\n"
					+ "					AND B.doc_mandatory_doc = 'false'\r\n"
					+ "					and B.doc_transaction_type = '" + typeCode + "') as d order by d.sud_doc_code"
					+ "";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploadDocument = new DocumentManagementDTO();

				uploadDocument.setDoc_Code(rs.getString("sud_doc_code"));
				uploadDocument.setAdd_Doc_Description(rs.getString("sud_document_des"));

				filepath = rs.getString("sud_file_path");

				if (filepath.equals("X")) {
					uploadDocument.setUploadOptionalFilePath(null);
					uploadDocument.setUploadFilePath(null);
				} else {
					uploadDocument.setUploadFilePath(filepath);
					uploadDocument.setUploadOptionalFilePath(rs.getString("sud_file_path"));
				}
				uploadDocument.setTransaction_Type(rs.getString("sud_transaction_type"));

				searchList.add(uploadDocument);

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

	public List<DocumentManagementDTO> mandatoryDocsForSisuSariyaPermitHolder(String typeCode, String empNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String filepath;

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			// changed by thilna.d on 27-10-2021
			String sql = "select case when (d.sud_expiry_date='null') then '' end as sud_expiry_date, d.sud_document_des, d.sud_doc_code, d.sud_transaction_type, d.sud_file_path \r\n"
					+ "from (select A.sud_expiry_date,A.sud_document_des,A.sud_doc_code,A.sud_transaction_type,sud_file_path as sud_file_path\r\n"
					+ "from public.nt_t_subsidy_document A \r\n" + "where A.sud_ref_no = '" + empNo + "'\r\n"
					+ "and A.sud_doc_type = 'M'\r\n" + "and A.sud_service_no is null \r\n"
					+ "and A.sud_transaction_type = '" + typeCode + "'\r\n" + "\r\n" + "union\r\n" + "\r\n"
					+ "select B.doc_expiry_date,B.doc_document_des,B.doc_code,B.doc_transaction_type,'X' as sud_file_path\r\n"
					+ "from public.nt_m_document B \r\n"
					+ "WHERE not EXISTS( select * from public.nt_t_subsidy_document A \r\n"
					+ "								where A.sud_doc_code = B.doc_code\r\n"
					+ "									and A.sud_ref_no = '" + empNo + "'\r\n"
					+ "									and A.sud_service_no is null \r\n"
					+ "									and A.sud_doc_type = 'M'\r\n"
					+ "									and A.sud_transaction_type = '" + typeCode
					+ "' )			\r\n" + "					AND B.doc_mandatory_doc = 'true'\r\n"
					+ "					and B.doc_transaction_type = '" + typeCode
					+ "') as d order by d.sud_doc_code\r\n" + "";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploadDocument = new DocumentManagementDTO();

				uploadDocument.setDoc_Code(rs.getString("sud_doc_code"));
				uploadDocument.setAdd_Doc_Description(rs.getString("sud_document_des"));

				filepath = rs.getString("sud_file_path");

				if (filepath.equals("X")) {
					uploadDocument.setUploadFilePath(null);

				} else {

					uploadDocument.setUploadFilePath(rs.getString("sud_file_path"));
				}
				uploadDocument.setTransaction_Type(rs.getString("sud_transaction_type"));

				String expiryDateString = rs.getString("sud_expiry_date");
				if (expiryDateString != null && !expiryDateString.trim().equals("")) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date expiryDate = originalFormat.parse(expiryDateString);
					uploadDocument.setExpiryDate(expiryDate);
				}

				searchList.add(uploadDocument);

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

	public List<DocumentManagementDTO> optionalDocsForSisuSariyaPermitHolder(String typeCode, String empNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String filepath;

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();

			// changed by thilna.d on 27-10-2021
			String sql = "select case when (d.sud_expiry_date='null') then '' end as sud_expiry_date, d.sud_document_des, d.sud_doc_code, d.sud_transaction_type, d.sud_file_path \r\n"
					+ "from (select A.sud_expiry_date,A.sud_document_des,A.sud_doc_code,A.sud_transaction_type,sud_file_path as sud_file_path\r\n"
					+ "from public.nt_t_subsidy_document A \r\n" + "where A.sud_ref_no = '" + empNo + "'\r\n"
					+ "and A.sud_service_no is null \r\n" + "and A.sud_doc_type = 'O'\r\n"
					+ "and A.sud_transaction_type = '" + typeCode + "'\r\n" + "\r\n" + "union\r\n" + "\r\n"
					+ "select B.doc_expiry_date,B.doc_document_des,B.doc_code,B.doc_transaction_type,'X' as sud_file_path\r\n"
					+ "from public.nt_m_document B \r\n"
					+ "WHERE not EXISTS( select * from public.nt_t_subsidy_document A \r\n"
					+ "								where A.sud_doc_code = B.doc_code\r\n"
					+ "									and A.sud_ref_no = '" + empNo + "'\r\n"
					+ "									and A.sud_service_no is null \r\n"
					+ "							and A.sud_doc_type = 'O'\r\n"
					+ "								and A.sud_transaction_type = '" + typeCode + "' )			\r\n"
					+ "					AND B.doc_mandatory_doc = 'false'\r\n"
					+ "					and B.doc_transaction_type = '" + typeCode + "') as d order by d.sud_doc_code"
					+ "";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploadDocument = new DocumentManagementDTO();

				uploadDocument.setDoc_Code(rs.getString("sud_doc_code"));
				uploadDocument.setAdd_Doc_Description(rs.getString("sud_document_des"));

				filepath = rs.getString("sud_file_path");

				if (filepath.equals("X")) {
					uploadDocument.setUploadOptionalFilePath(null);
					uploadDocument.setUploadFilePath(null);
				} else {
					uploadDocument.setUploadFilePath(filepath);
					uploadDocument.setUploadOptionalFilePath(rs.getString("sud_file_path"));
				}
				uploadDocument.setTransaction_Type(rs.getString("sud_transaction_type"));

				String expiryDateString = rs.getString("sud_expiry_date");
				if (expiryDateString != null && !expiryDateString.trim().equals("")) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date expiryDate = originalFormat.parse(expiryDateString);
					uploadDocument.setExpiryDate(expiryDate);
				}

				searchList.add(uploadDocument);

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

	public List<DocumentManagementDTO> mandatoryDocsForSisuSariyaAgreementRenewals(String typeCode, String empNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String filepath;

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();

			String sql = "select d.sud_document_des, d.sud_doc_code, d.sud_transaction_type, d.sud_file_path \r\n"
					+ "from (select A.sud_document_des,A.sud_doc_code,A.sud_transaction_type,sud_file_path as sud_file_path\r\n"
					+ "from public.nt_t_subsidy_document A \r\n" + "where A.sud_service_no = '" + empNo + "'\r\n"
					+ "and A.sud_doc_type = 'M'\r\n" + "and A.sud_transaction_type = '" + typeCode + "'\r\n" + "\r\n"
					+ "union\r\n" + "\r\n"
					+ "select B.doc_document_des,B.doc_code,B.doc_transaction_type,'X' as sud_file_path\r\n"
					+ "from public.nt_m_document B \r\n"
					+ "WHERE not EXISTS( select * from public.nt_t_subsidy_document A \r\n"
					+ "								where A.sud_doc_code = B.doc_code\r\n"
					+ "									and A.sud_service_no = '" + empNo + "'\r\n"
					+ "							and A.sud_doc_type = 'M'\r\n"
					+ "								and A.sud_transaction_type = '" + typeCode + "' )			\r\n"
					+ "					AND B.doc_mandatory_doc = 'true'\r\n"
					+ "					and B.doc_transaction_type = '" + typeCode
					+ "') as d order by d.sud_doc_code\r\n" + "";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploadDocument = new DocumentManagementDTO();

				uploadDocument.setDoc_Code(rs.getString("sud_doc_code"));
				uploadDocument.setAdd_Doc_Description(rs.getString("sud_document_des"));

				filepath = rs.getString("sud_file_path");

				if (filepath.equals("X")) {
					uploadDocument.setUploadFilePath(null);

				} else {

					uploadDocument.setUploadFilePath(rs.getString("sud_file_path"));
				}
				uploadDocument.setTransaction_Type(rs.getString("sud_transaction_type"));

				searchList.add(uploadDocument);

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

	public List<DocumentManagementDTO> optionalDocsForSisuSariyaAgreementRenewals(String typeCode, String empNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String filepath;

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();

			String sql = "select d.sud_document_des, d.sud_doc_code, d.sud_transaction_type, d.sud_file_path \r\n"
					+ "from (select A.sud_document_des,A.sud_doc_code,A.sud_transaction_type,sud_file_path as sud_file_path\r\n"
					+ "from public.nt_t_subsidy_document A \r\n" + "where A.sud_service_no = '" + empNo + "'\r\n"
					+ "and A.sud_doc_type = 'O'\r\n" + "and A.sud_transaction_type = '" + typeCode + "'\r\n" + "\r\n"
					+ "union\r\n" + "\r\n"
					+ "select B.doc_document_des,B.doc_code,B.doc_transaction_type,'X' as sud_file_path\r\n"
					+ "from public.nt_m_document B \r\n"
					+ "WHERE not EXISTS( select * from public.nt_t_subsidy_document A \r\n"
					+ "								where A.sud_doc_code = B.doc_code\r\n"
					+ "									and A.sud_service_no = '" + empNo + "'\r\n"
					+ "							and A.sud_doc_type = 'O'\r\n"
					+ "								and A.sud_transaction_type = '" + typeCode + "' )			\r\n"
					+ "					AND B.doc_mandatory_doc = 'false'\r\n"
					+ "					and B.doc_transaction_type = '" + typeCode + "') as d order by d.sud_doc_code"
					+ "";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploadDocument = new DocumentManagementDTO();

				uploadDocument.setDoc_Code(rs.getString("sud_doc_code"));
				uploadDocument.setAdd_Doc_Description(rs.getString("sud_document_des"));

				filepath = rs.getString("sud_file_path");

				if (filepath.equals("X")) {
					uploadDocument.setUploadOptionalFilePath(null);
					uploadDocument.setUploadFilePath(null);
				} else {
					uploadDocument.setUploadFilePath(filepath);
					uploadDocument.setUploadOptionalFilePath(rs.getString("sud_file_path"));
				}
				uploadDocument.setTransaction_Type(rs.getString("sud_transaction_type"));

				searchList.add(uploadDocument);

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

	public int saveUserManagementMandatoryUploads(String filePath, String doc_des, String doc_code, String empNo,
			String transaction_Code, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_employee_document");

			String sql = "INSERT INTO public.nt_t_employee_document\r\n"
					+ "(seqno, emp_transaction_type, emp_ref_no, emp_doc_code, emp_document_des, emp_file_path, emp_doc_type, emp_created_by, emp_created_date)\r\n"
					+ "VALUES('" + seqNo + "', '" + transaction_Code + "', '" + empNo + "', " + "'" + doc_code + "', '"
					+ doc_des + "', '" + filePath + "', 'M', '" + user + "', '" + timestamp + "');";

			stmt = con.prepareStatement(sql);

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

	public int saveUserManagementOptionalUploads(String filePath, String doc_des, String doc_code, String empNo,
			String transaction_Code, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_employee_document");

			String sql = "INSERT INTO public.nt_t_employee_document\r\n"
					+ "(seqno, emp_transaction_type, emp_ref_no, emp_doc_code, emp_document_des, emp_file_path, emp_doc_type, emp_created_by, emp_created_date)\r\n"
					+ "VALUES('" + seqNo + "', '" + transaction_Code + "', '" + empNo + "', " + "'" + doc_code + "', '"
					+ doc_des + "', '" + filePath + "', 'O', '" + user + "', '" + timestamp + "');";

			stmt = con.prepareStatement(sql);

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

	public String getUserManagementFilePath(String filePath, String doc_des, String empNo, String transaction_Code) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String output = null;
		try {

			con = ConnectionManager.getConnection();

			String sql = "select  a.doc_code, a.doc_document_des,a.doc_mandatory_doc,b.emp_file_path,b.emp_transaction_type,b.emp_ref_no from nt_m_document a left outer join nt_t_employee_document b on a.doc_code = b.emp_doc_code where a.doc_mandatory_doc='true' and b.emp_transaction_type='"
					+ transaction_Code + "' and a.doc_document_des = '" + doc_des + "' and b.emp_ref_no = '" + empNo
					+ "'";
			;

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {

				output = rs.getString("emp_file_path");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return output;
	}

	public int replaceUserManagementDoc(String doc_des, String empNo, String transaction_Code, String user,
			String docuString) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_employee_document  \r\n" + "SET  emp_modified_by='" + user
					+ "',emp_modified_date='" + timestamp + "', emp_file_path = '" + docuString + "'  \r\n"
					+ "where emp_ref_no ='" + empNo + "' and emp_transaction_type='" + transaction_Code
					+ "' and emp_document_des ='" + doc_des + "'and emp_doc_type='M'    ;\r\n" + "";

			stmt = con.prepareStatement(sql);

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

	public int replaceOptionalUserManagementDoc(String doc_des, String empNo, String transaction_Code, String user,
			String docuString) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_employee_document  \r\n" + "SET  emp_modified_by='" + user
					+ "',emp_modified_date='" + timestamp + "', emp_file_path = '" + docuString + "'  \r\n"
					+ "where emp_ref_no ='" + empNo + "' and emp_transaction_type='" + transaction_Code
					+ "' and emp_document_des ='" + doc_des + "'and emp_doc_type='O'    ;\r\n" + "";

			stmt = con.prepareStatement(sql);

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

	public List<DocumentManagementDTO> getEmployeeInfo(String empNo, DocumentManagementDTO documentPermitInfo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<DocumentManagementDTO> permitInfo = new ArrayList<DocumentManagementDTO>();

		try {

			con = ConnectionManager.getConnection();

			String sql = "SELECT emp_fullname,emp_title,emp_moblile_no FROM public.nt_m_employee where emp_no = '"
					+ empNo + "'";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {

				documentPermitInfo.setPermitMobileNum(rs.getString("emp_moblile_no"));
				documentPermitInfo.setPermitName(rs.getString("emp_fullname"));
				documentPermitInfo.setPermitTitle(rs.getString("emp_title"));

				permitInfo.add(documentPermitInfo);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return permitInfo;

	}

	public String getTitleName(String titleNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String output = null;
		try {

			con = ConnectionManager.getConnection();

			String sql = "SELECT description FROM public.nt_r_title\r\n" + "WHERE code = '" + titleNo + "'";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {

				output = rs.getString("description");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return output;
	}

	public int getVersionNumber(DocumentManagementDTO uploaddocumentManagementDTO, String typeCodeTrans,
			String docType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<DocumentManagementDTO> versionList = new ArrayList<DocumentManagementDTO>(0);
		int output = 0;
		try {

			con = ConnectionManager.getConnection();

			String sql = "SELECT  apd_transaction_type, apd_application_no, apd_permit_no, apd_doc_code, apd_document_des, apd_file_path,apd_versionno\r\n"
					+ "FROM public.nt_t_application_document_version\r\n" + "where apd_application_no='"
					+ uploaddocumentManagementDTO.getUpload_Application() + "' and apd_permit_no='"
					+ uploaddocumentManagementDTO.getUpload_Permit() + "' and apd_transaction_type='" + typeCodeTrans
					+ "' and apd_doc_type='" + docType + "' and apd_doc_code='"
					+ uploaddocumentManagementDTO.getDoc_Code() + "' order by apd_versionno;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			int count = 0;
			while (rs.next()) {
				DocumentManagementDTO dto = new DocumentManagementDTO();
				dto.setVersionNo(rs.getInt("apd_versionno"));
				count = dto.getVersionNo();
				versionList.add(dto);

			}

			if (count == 0) {
				count = 1;
				output = count;

			} else {
				count++;
				output = count;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return output;
	}

	public int getVersionNumberForSisuSariya(DocumentManagementDTO uploaddocumentManagementDTO, String typeCodeTrans,
			String docType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<DocumentManagementDTO> versionList = new ArrayList<DocumentManagementDTO>(0);
		int output = 0;
		try {

			con = ConnectionManager.getConnection();

			String sql = "SELECT  sud_transaction_type, sud_request_no,sud_doc_code, sud_document_des, sud_file_path,sud_versionno\r\n"
					+ "FROM public.nt_t_subsidy_document_version\r\n" + "where sud_request_no='"
					+ uploaddocumentManagementDTO.getUpload_Permit() + "'  and sud_transaction_type='" + typeCodeTrans
					+ "' and sud_doc_type='" + docType + "' and sud_doc_code='"
					+ uploaddocumentManagementDTO.getDoc_Code() + "' order by sud_versionno;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			int count = 0;
			while (rs.next()) {
				DocumentManagementDTO dto = new DocumentManagementDTO();
				dto.setVersionNo(rs.getInt("sud_versionno"));
				count = dto.getVersionNo();
				versionList.add(dto);

			}

			if (count == 0) {
				count = 1;
				output = count;

			} else {
				count++;
				output = count;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return output;
	}

	public int getVersionNumberForSisuSariyaPermitHolder(DocumentManagementDTO uploaddocumentManagementDTO,
			String typeCodeTrans, String docType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<DocumentManagementDTO> versionList = new ArrayList<DocumentManagementDTO>(0);
		int output = 0;
		try {

			con = ConnectionManager.getConnection();

			String sql = "SELECT  sud_transaction_type, sud_request_no,sud_doc_code, sud_document_des, sud_file_path,sud_versionno\r\n"
					+ "FROM public.nt_t_subsidy_document_version\r\n" + "where sud_request_no='"
					+ uploaddocumentManagementDTO.getUpload_Permit() + "' and sud_ref_no ='"
					+ uploaddocumentManagementDTO.getUpload_Application() + "'  and sud_transaction_type='"
					+ typeCodeTrans + "' and sud_doc_type='" + docType + "' and sud_doc_code='"
					+ uploaddocumentManagementDTO.getDoc_Code() + "' order by sud_versionno;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			int count = 0;
			while (rs.next()) {
				DocumentManagementDTO dto = new DocumentManagementDTO();
				dto.setVersionNo(rs.getInt("sud_versionno"));
				count = dto.getVersionNo();
				versionList.add(dto);

			}

			if (count == 0) {
				count = 1;
				output = count;

			} else {
				count++;
				output = count;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return output;
	}

	public int getVersionNumberForSisuSariyaAgreementRenewals(DocumentManagementDTO uploaddocumentManagementDTO,
			String typeCodeTrans, String docType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<DocumentManagementDTO> versionList = new ArrayList<DocumentManagementDTO>(0);
		int output = 0;
		try {

			con = ConnectionManager.getConnection();

			String sql = "SELECT  sud_transaction_type, sud_request_no,sud_doc_code, sud_document_des, sud_file_path,sud_versionno\r\n"
					+ "FROM public.nt_t_subsidy_document_version\r\n" + "where sud_service_no='"
					+ uploaddocumentManagementDTO.getServiceNo() + "' and sud_ref_no ='"
					+ uploaddocumentManagementDTO.getUpload_Application() + "'  and sud_transaction_type='"
					+ typeCodeTrans + "' and sud_doc_type='" + docType + "' and sud_doc_code='"
					+ uploaddocumentManagementDTO.getDoc_Code() + "' order by sud_versionno;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			int count = 0;
			while (rs.next()) {
				DocumentManagementDTO dto = new DocumentManagementDTO();
				dto.setVersionNo(rs.getInt("sud_versionno"));
				count = dto.getVersionNo();
				versionList.add(dto);

			}

			if (count == 0) {
				count = 1;
				output = count;

			} else {
				count++;
				output = count;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return output;
	}

	public int getVersionNumberForUserManagement(DocumentManagementDTO uploaddocumentManagementDTO,
			String typeCodeTrans, String docType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<DocumentManagementDTO> versionList = new ArrayList<DocumentManagementDTO>(0);
		int output = 0;
		try {

			con = ConnectionManager.getConnection();

			String sql = "SELECT  emp_transaction_type, emp_ref_no, emp_doc_code, emp_document_des, emp_file_path,emp_versionno\r\n"
					+ "FROM public.nt_t_employee_document_version\r\n" + "where emp_ref_no='"
					+ uploaddocumentManagementDTO.getUpload_empNo() + "'and emp_transaction_type='" + typeCodeTrans
					+ "' and emp_doc_type='" + docType + "' and emp_doc_code='"
					+ uploaddocumentManagementDTO.getDoc_Code() + "'order by emp_versionno;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			int count = 0;
			while (rs.next()) {
				DocumentManagementDTO dto = new DocumentManagementDTO();
				dto.setVersionNo(rs.getInt("emp_versionno"));
				count = dto.getVersionNo();
				versionList.add(dto);

			}

			if (count == 0) {
				count = 1;
				output = count;

			} else {
				count++;
				output = count;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return output;
	}

	public int getUserManagementVersionNumber(String empNo, String typeCodeTrans, String docType, String doc_code) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		int output = 0;
		try {

			con = ConnectionManager.getConnection();

			String sql = "SELECT  emp_transaction_type, emp_ref_no, emp_doc_code, emp_document_des, emp_file_path,emp_versionno\r\n"
					+ "FROM public.nt_t_employee_document_version\r\n" + "where emp_ref_no='" + empNo
					+ "'and emp_transaction_type='" + typeCodeTrans + "' and emp_doc_type='" + docType
					+ "' and emp_doc_code='" + doc_code + "';";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			int count = 0;
			while (rs.next()) {
				++count;

			}

			if (count == 0) {
				count = 1;
				output = count;

			} else {
				count++;
				output = count;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return output;
	}

	/* Driver Conductor */
	public String getFilePathForDriverConductor(String filePath, String doc_des, String permitNo,
			String transaction_Code, String doc_code, String appNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String output = null;
		try {

			con = ConnectionManager.getConnection();

			String sql = "select distinct a.doc_code, a.doc_document_des,a.doc_mandatory_doc,b.sud_file_path,b.sud_transaction_type,b.sud_driver_conductor_id,b.sud_application_no "
					+ " from nt_m_document a left outer join nt_t_driverconductor_document_version b on a.doc_code = b.sud_doc_code "
					+ " where a.doc_mandatory_doc='true' and b.sud_transaction_type='" + transaction_Code
					+ "' and a.doc_code ='" + doc_code + "'  and b.sud_driver_conductor_id ='" + permitNo
					+ "' and b.sud_application_no='" + appNo + "'  ";
			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {

				output = rs.getString("sud_file_path");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return output;
	}

	public int replaceDocForDriverConductor(String doc_des, String permitNo, String transaction_Code, String user,
			String docuString, String refNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_driverconductor_document " + "SET  sud_modified_by='" + user
					+ "', sud_modified_date='" + timestamp + "', sud_file_path = '" + docuString + "'  \r\n"
					+ "where sud_driver_conductor_id ='" + permitNo + "'and sud_application_no = '" + refNo
					+ "' and sud_transaction_type='" + transaction_Code + "' and sud_document_des ='" + doc_des
					+ "'and sud_doc_type='M'    ;\r\n" + "";

			stmt = con.prepareStatement(sql);

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

	public int saveMandatoryUploadsVersionsForDriverConductor(String filePath, String doc_des, String doc_code,
			String permitNo, String applicationNo, String transaction_Code, String user, int versionNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_driverconductor_document_version");

			String sql = "INSERT INTO public.nt_t_driverconductor_document_version \r\n"
					+ "(seqno, sud_transaction_type, sud_driver_conductor_id,sud_application_no,sud_doc_code, sud_document_des, sud_file_path, sud_doc_type, sud_created_by, sud_created_date,sud_versionno)\r\n"
					+ "VALUES('" + seqNo + "', '" + transaction_Code + "',  '" + permitNo + "','" + applicationNo
					+ "','" + doc_code + "', '" + doc_des + "', '" + filePath + "', 'M', '" + user + "', '" + timestamp
					+ "', '" + versionNo + "');\r\n" + "";

			stmt = con.prepareStatement(sql);

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

	public int getVersionNumberForDriverConductor(DocumentManagementDTO uploaddocumentManagementDTO,
			String typeCodeTrans, String docType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<DocumentManagementDTO> versionList = new ArrayList<DocumentManagementDTO>(0);
		int output = 0;
		try {

			con = ConnectionManager.getConnection();

			String sql = "SELECT  sud_transaction_type, sud_application_no,sud_doc_code, sud_document_des, sud_file_path,sud_versionno "
					+ " FROM public.nt_t_driverconductor_document_version " + " where sud_driver_conductor_id='"
					+ uploaddocumentManagementDTO.getUpload_Permit() + "' and sud_application_no ='"
					+ uploaddocumentManagementDTO.getUpload_Application() + "'  and sud_transaction_type='"
					+ typeCodeTrans + "' and sud_doc_type='" + docType + "' and sud_doc_code='"
					+ uploaddocumentManagementDTO.getDoc_Code() + "' order by sud_versionno;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			int count = 0;
			while (rs.next()) {
				DocumentManagementDTO dto = new DocumentManagementDTO();
				dto.setVersionNo(rs.getInt("sud_versionno"));
				count = dto.getVersionNo();
				versionList.add(dto);

			}

			if (count == 0) {
				count = 1;
				output = count;

			} else {
				count++;
				output = count;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return output;
	}

	public int saveMandatoryUploadsForDriverConductor(String filePath, String doc_des, String doc_code, String permitNo,
			String applicationNo, String transaction_Code, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_driverconductor_document");

			String sql = "INSERT INTO public.nt_t_driverconductor_document \r\n"
					+ "(seqno, sud_transaction_type, sud_driver_conductor_id,sud_application_no, sud_doc_code, sud_document_des, sud_file_path, sud_doc_type, sud_created_by, sud_created_date)\r\n"
					+ "VALUES('" + seqNo + "', '" + transaction_Code + "', '" + permitNo + "','" + applicationNo + "','"
					+ doc_code + "', '" + doc_des + "', '" + filePath + "', 'M', '" + user + "', '" + timestamp + "');";

			stmt = con.prepareStatement(sql);
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

	public String getOptionalFilePathForDriverConductor(String filePath, String doc_des, String permitNo,
			String transaction_Code, String doc_code, String appNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String output = null;
		try {

			con = ConnectionManager.getConnection();

			String sql = "select  a.doc_code, a.doc_document_des,a.doc_mandatory_doc,b.sud_file_path,b.sud_transaction_type,b.sud_driver_conductor_id\r\n"
					+ "from nt_m_document a left outer join nt_t_driverconductor_document_version b on a.doc_code = b.sud_doc_code \r\n"
					+ "where a.doc_mandatory_doc='false' and b.sud_transaction_type='" + transaction_Code
					+ "' and a.doc_code ='" + doc_code + "'  and b.sud_driver_conductor_id ='" + permitNo + "'  ";
			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {

				output = rs.getString("sud_file_path");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return output;
	}

	public int replaceOptionalDocForDriverConductor(String doc_des, String permitNo, String transaction_Code,
			String user, String docuString) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_driverconductor_document  \r\n" + "SET  sud_modified_by='" + user
					+ "', sud_modified_date='" + timestamp + "',sud_file_path = '" + docuString + "'      \r\n"
					+ "where sud_driver_conductor_id ='" + permitNo + "' and sud_transaction_type='" + transaction_Code
					+ "' and sud_document_des ='" + doc_des + "' and sud_doc_type='O'  ;\r\n" + "";

			stmt = con.prepareStatement(sql);

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

	public int saveOptionalUploadsVersionsForDriverConductor(String filePath, String doc_des, String doc_code,
			String permitNo, String applicationNo, String transaction_Code, String user, int versionNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_driverconductor_document_version");

			String sql = "INSERT INTO public.nt_t_driverconductor_document_version\r\n"
					+ "(seqno, sud_transaction_type,sud_application_no,sud_driver_conductor_id,sud_doc_code, sud_document_des, sud_file_path, sud_doc_type, sud_created_by, sud_created_date,sud_versionno)\r\n"
					+ "VALUES('" + seqNo + "', '" + transaction_Code + "', '" + applicationNo + "','" + permitNo
					+ "', '" + doc_code + "', '" + doc_des + "', '" + filePath + "', 'O', '" + user + "', '" + timestamp
					+ "', '" + versionNo + "');\r\n" + "";

			stmt = con.prepareStatement(sql);

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

	public int saveOptionalUploadsForDriverConductor(String filePath, String doc_des, String doc_code, String permitNo,
			String applicationNo, String transaction_Code, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_driverconductor_document");

			String sql = "INSERT INTO public.nt_t_driverconductor_document \r\n"
					+ "(seqno, sud_transaction_type,sud_application_no, sud_driver_conductor_id, sud_doc_code, sud_document_des, sud_file_path, sud_doc_type, sud_created_by, sud_created_date)\r\n"
					+ "VALUES('" + seqNo + "', '" + transaction_Code + "', '" + applicationNo + "', '" + permitNo
					+ "', '" + doc_code + "', '" + doc_des + "', '" + filePath + "', 'O', '" + user + "', '" + timestamp
					+ "');";

			stmt = con.prepareStatement(sql);

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

	public List<DocumentManagementDTO> driverConductorMandatoryList(String requestNo, String driverConductorId) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String transactionTypeCode = null;
		try {
			transactionTypeCode = PropertyReader.getPropertyValue("queue.transaction.type.code.newdriver"); // To Do
		} catch (ApplicationException e) {
			e.printStackTrace();
		}

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT sud_doc_code,sud_document_des,sud_file_path,sud_versionno FROM public.nt_t_driverconductor_document_version \r\n"
					+ "where sud_application_no='" + requestNo + "' and sud_driver_conductor_id='" + driverConductorId
					+ "' and sud_transaction_type=" + transactionTypeCode + " and sud_doc_type='M' \r\n"
					+ "order by sud_document_des, sud_versionno ;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();

				uploaddocumentManagementDTO.setDoc_Code(rs.getString("sud_doc_code"));
				uploaddocumentManagementDTO.setAdd_Doc_Description(rs.getString("sud_document_des"));
				uploaddocumentManagementDTO.setUploadFilePath(rs.getString("sud_file_path"));
				uploaddocumentManagementDTO.setVersionNo(rs.getInt("sud_versionno"));
				uploaddocumentManagementDTO.setDocumentName(uploaddocumentManagementDTO.getAdd_Doc_Description() + "_"
						+ uploaddocumentManagementDTO.getVersionNo());

				searchList.add(uploaddocumentManagementDTO);
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

	public List<DocumentManagementDTO> driverConductorOptionalList(String requestNo, String driverConductorId) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String transactionTypeCode = null;
		try {
			transactionTypeCode = PropertyReader.getPropertyValue("queue.transaction.type.code.newdriver"); // To Do
		} catch (ApplicationException e) {
			e.printStackTrace();
		}

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT sud_doc_code,sud_document_des,sud_file_path,sud_versionno FROM public.nt_t_driverconductor_document_version \r\n"
					+ "where sud_application_no='" + driverConductorId + "'and sud_driver_conductor_id = '" + requestNo
					+ "' and  sud_transaction_type=" + transactionTypeCode + " and sud_doc_type='O' \r\n"
					+ "order by sud_document_des, sud_versionno ;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();

				uploaddocumentManagementDTO.setDoc_Code(rs.getString("sud_doc_code"));
				uploaddocumentManagementDTO.setAdd_Doc_Description(rs.getString("sud_document_des"));
				uploaddocumentManagementDTO.setUploadFilePath(rs.getString("sud_file_path"));
				uploaddocumentManagementDTO.setVersionNo(rs.getInt("sud_versionno"));
				uploaddocumentManagementDTO.setDocumentName(uploaddocumentManagementDTO.getAdd_Doc_Description() + "_"
						+ uploaddocumentManagementDTO.getVersionNo());

				searchList.add(uploaddocumentManagementDTO);
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

	public List<DocumentManagementDTO> mandatoryDocsFordriverConductor(String typeCode, String driverConductorId) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String filepath;

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();

			String sql = "select d.sud_document_des, d.sud_doc_code, d.sud_transaction_type, d.sud_file_path \r\n"
					+ "from (select A.sud_document_des,A.sud_doc_code,A.sud_transaction_type,sud_file_path as sud_file_path\r\n"
					+ "from public.nt_t_driverconductor_document A \r\n" + "where A.sud_driver_conductor_id = '"
					+ driverConductorId + "'\r\n" + "and A.sud_doc_type = 'M'\r\n" + "and A.sud_transaction_type = '"
					+ typeCode + "'\r\n" + "\r\n" + "union\r\n" + "\r\n"
					+ "select B.doc_document_des,B.doc_code,B.doc_transaction_type,'X' as sud_file_path\r\n"
					+ "from public.nt_m_document B \r\n"
					+ "WHERE not EXISTS( select * from public.nt_t_driverconductor_document A \r\n"
					+ "								where A.sud_doc_code = B.doc_code\r\n"
					+ "									and A.sud_driver_conductor_id = '" + driverConductorId + "'\r\n"
					+ "									and A.sud_doc_type = 'M'\r\n"
					+ "									and A.sud_transaction_type = '" + typeCode
					+ "' )			\r\n" + "					AND B.doc_mandatory_doc = 'true'\r\n"
					+ "					and B.doc_transaction_type = '" + typeCode
					+ "') as d order by d.sud_doc_code\r\n" + "";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploadDocument = new DocumentManagementDTO();

				uploadDocument.setDoc_Code(rs.getString("sud_doc_code"));
				uploadDocument.setAdd_Doc_Description(rs.getString("sud_document_des"));

				filepath = rs.getString("sud_file_path");

				if (filepath.equals("X")) {
					uploadDocument.setUploadFilePath(null);

				} else {

					uploadDocument.setUploadFilePath(rs.getString("sud_file_path"));
				}
				uploadDocument.setTransaction_Type(rs.getString("sud_transaction_type"));

				searchList.add(uploadDocument);

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

	public List<DocumentManagementDTO> optionalDocsFordriverConductor(String typeCode, String driverConductorId) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String filepath;

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();

			String sql = "select d.sud_document_des, d.sud_doc_code, d.sud_transaction_type, d.sud_file_path \r\n"
					+ "from (select A.sud_document_des,A.sud_doc_code,A.sud_transaction_type,sud_file_path as sud_file_path\r\n"
					+ "from public.nt_t_driverconductor_document A \r\n" + "where A.sud_driver_conductor_id = '"
					+ driverConductorId + "'\r\n" + "and A.sud_doc_type = 'O'\r\n" + "and A.sud_transaction_type = '"
					+ typeCode + "'\r\n" + "\r\n" + "union\r\n" + "\r\n"
					+ "select B.doc_document_des,B.doc_code,B.doc_transaction_type,'X' as sud_file_path\r\n"
					+ "from public.nt_m_document B \r\n"
					+ "WHERE not EXISTS( select * from public.nt_t_driverconductor_document A \r\n"
					+ "								where A.sud_doc_code = B.doc_code\r\n"
					+ "									and A.sud_driver_conductor_id = '" + driverConductorId + "'\r\n"
					+ "							and A.sud_doc_type = 'O'\r\n"
					+ "								and A.sud_transaction_type = '" + typeCode + "' )			\r\n"
					+ "					AND B.doc_mandatory_doc = 'false'\r\n"
					+ "					and B.doc_transaction_type = '" + typeCode + "') as d order by d.sud_doc_code"
					+ "";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploadDocument = new DocumentManagementDTO();

				uploadDocument.setDoc_Code(rs.getString("sud_doc_code"));
				uploadDocument.setAdd_Doc_Description(rs.getString("sud_document_des"));

				filepath = rs.getString("sud_file_path");

				if (filepath.equals("X")) {
					uploadDocument.setUploadOptionalFilePath(null);
					uploadDocument.setUploadFilePath(null);
				} else {
					uploadDocument.setUploadFilePath(filepath);
					uploadDocument.setUploadOptionalFilePath(rs.getString("sud_file_path"));
				}
				uploadDocument.setTransaction_Type(rs.getString("sud_transaction_type"));

				searchList.add(uploadDocument);

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

	public List<DocumentManagementDTO> driverConductorMandatoryListM(String driverConductorId,
			String strTransactionType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT sud_doc_code,sud_document_des,sud_file_path,sud_versionno FROM public.nt_t_driverconductor_document_version \r\n"
					+ "where sud_driver_conductor_id='" + driverConductorId + "'  and sud_transaction_type='"
					+ strTransactionType + "' and sud_doc_type='M' \r\n" + "order by sud_document_des, sud_versionno ;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();

				uploaddocumentManagementDTO.setDoc_Code(rs.getString("sud_doc_code"));
				uploaddocumentManagementDTO.setAdd_Doc_Description(rs.getString("sud_document_des"));
				uploaddocumentManagementDTO.setUploadFilePath(rs.getString("sud_file_path"));
				uploaddocumentManagementDTO.setVersionNo(rs.getInt("sud_versionno"));
				uploaddocumentManagementDTO.setDocumentName(uploaddocumentManagementDTO.getAdd_Doc_Description() + "_"
						+ uploaddocumentManagementDTO.getVersionNo());

				searchList.add(uploaddocumentManagementDTO);
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

	public List<DocumentManagementDTO> driverConductorOptionalListM(String driverConductorId,
			String strTransactionType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT sud_doc_code,sud_document_des,sud_file_path,sud_versionno FROM public.nt_t_driverconductor_document_version \r\n"
					+ "where sud_driver_conductor_id='" + driverConductorId + "' and sud_transaction_type='"
					+ strTransactionType + "' and sud_doc_type='O' \r\n" + "order by sud_document_des, sud_versionno ;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();

				uploaddocumentManagementDTO.setDoc_Code(rs.getString("sud_doc_code"));
				uploaddocumentManagementDTO.setAdd_Doc_Description(rs.getString("sud_document_des"));
				uploaddocumentManagementDTO.setUploadFilePath(rs.getString("sud_file_path"));
				uploaddocumentManagementDTO.setVersionNo(rs.getInt("sud_versionno"));
				uploaddocumentManagementDTO.setDocumentName(uploaddocumentManagementDTO.getAdd_Doc_Description() + "_"
						+ uploaddocumentManagementDTO.getVersionNo());

				searchList.add(uploaddocumentManagementDTO);
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

	/**/

	/* Grievance Management */
	public List<DocumentManagementDTO> mandatoryDocsForGrievance(String typeCode, String complainNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String filepath;

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();

			String sql = "select d.sud_document_des, d.sud_doc_code, d.sud_transaction_type, d.sud_file_path \r\n"
					+ "from (select A.sud_document_des,A.sud_doc_code,A.sud_transaction_type,sud_file_path as sud_file_path\r\n"
					+ "from public.nt_t_grievance_document A \r\n" + "where A.sud_complain_no = '" + complainNo
					+ "'\r\n" + "and A.sud_doc_type = 'M'\r\n" + "and A.sud_transaction_type = '" + typeCode + "'\r\n"
					+ "\r\n" + "union\r\n" + "\r\n"
					+ "select B.doc_document_des,B.doc_code,B.doc_transaction_type,'X' as sud_file_path\r\n"
					+ "from public.nt_m_document B \r\n"
					+ "WHERE not EXISTS( select * from public.nt_t_grievance_document A \r\n"
					+ "								where A.sud_doc_code = B.doc_code\r\n"
					+ "									and A.sud_complain_no = '" + complainNo + "'\r\n"
					+ "									and A.sud_doc_type = 'M'\r\n"
					+ "									and A.sud_transaction_type = '" + typeCode
					+ "' )			\r\n" + "					AND B.doc_mandatory_doc = 'true'\r\n"
					+ "					and B.doc_transaction_type = '" + typeCode
					+ "') as d order by d.sud_doc_code\r\n" + "";
			
			/*String sql="select d.sud_document_des, d.sud_doc_code, d.sud_transaction_type, d.sud_file_path \r\n"
					+ "from public.nt_t_grievance_document d \r\n" + "where d.sud_complain_no = '" + complainNo
					+ "'\r\n" + "and d.sud_doc_type = 'M'\r\n" + "and d.sud_transaction_type = '" + typeCode + "'\r\n"
					+ " order by d.sud_created_date desc "
					+ "limit 1";*/

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploadDocument = new DocumentManagementDTO();

				uploadDocument.setDoc_Code(rs.getString("sud_doc_code"));
				uploadDocument.setAdd_Doc_Description(rs.getString("sud_document_des"));

				filepath = rs.getString("sud_file_path");

				if (filepath.equals("X")) {
					uploadDocument.setUploadFilePath(null);

				} else {

					uploadDocument.setUploadFilePath(rs.getString("sud_file_path"));
				}
				uploadDocument.setTransaction_Type(rs.getString("sud_transaction_type"));

				searchList.add(uploadDocument);

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

	public List<DocumentManagementDTO> optionalDocsForGrievance(String typeCode, String complainNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String filepath;

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();

			String sql = "select d.sud_document_des, d.sud_doc_code, d.sud_transaction_type, d.sud_file_path \r\n"
					+ "from (select A.sud_document_des,A.sud_doc_code,A.sud_transaction_type,sud_file_path as sud_file_path\r\n"
					+ "from public.nt_t_grievance_document A \r\n" + "where A.sud_complain_no = '" + complainNo
					+ "'\r\n" + "and A.sud_doc_type = 'O'\r\n" + "and A.sud_transaction_type = '" + typeCode + "'\r\n"
					+ "\r\n" + "union\r\n" + "\r\n"
					+ "select B.doc_document_des,B.doc_code,B.doc_transaction_type,'X' as sud_file_path\r\n"
					+ "from public.nt_m_document B \r\n"
					+ "WHERE not EXISTS( select * from public.nt_t_grievance_document A \r\n"
					+ "								where A.sud_doc_code = B.doc_code\r\n"
					+ "									and A.sud_complain_no = '" + complainNo + "'\r\n"
					+ "							and A.sud_doc_type = 'O'\r\n"
					+ "								and A.sud_transaction_type = '" + typeCode + "' )			\r\n"
					+ "					AND B.doc_mandatory_doc = 'false'\r\n"
					+ "					and B.doc_transaction_type = '" + typeCode + "') as d order by d.sud_doc_code"
					+ "";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploadDocument = new DocumentManagementDTO();

				uploadDocument.setDoc_Code(rs.getString("sud_doc_code"));
				uploadDocument.setAdd_Doc_Description(rs.getString("sud_document_des"));

				filepath = rs.getString("sud_file_path");

				if (filepath.equals("X")) {
					uploadDocument.setUploadOptionalFilePath(null);
					uploadDocument.setUploadFilePath(null);
				} else {
					uploadDocument.setUploadFilePath(filepath);
					uploadDocument.setUploadOptionalFilePath(rs.getString("sud_file_path"));
				}
				uploadDocument.setTransaction_Type(rs.getString("sud_transaction_type"));

				searchList.add(uploadDocument);

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

	public List<DocumentManagementDTO> grievanceMandatoryListM(String complainNo, String strTransactionType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT sud_doc_code,sud_document_des,sud_file_path,sud_versionno FROM public.nt_t_grievance_document_version \r\n"
					+ "where sud_complain_no ='" + complainNo + "'  and sud_transaction_type='" + strTransactionType
					+ "' and sud_doc_type='M' \r\n" + "order by sud_document_des, sud_versionno ;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();

				uploaddocumentManagementDTO.setDoc_Code(rs.getString("sud_doc_code"));
				uploaddocumentManagementDTO.setAdd_Doc_Description(rs.getString("sud_document_des"));
				uploaddocumentManagementDTO.setUploadFilePath(rs.getString("sud_file_path"));
				uploaddocumentManagementDTO.setVersionNo(rs.getInt("sud_versionno"));
				uploaddocumentManagementDTO.setDocumentName(uploaddocumentManagementDTO.getAdd_Doc_Description() + "_"
						+ uploaddocumentManagementDTO.getVersionNo());

				searchList.add(uploaddocumentManagementDTO);
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

	public List<DocumentManagementDTO> grievanceOptionalListM(String complainNo, String strTransactionType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT sud_doc_code,sud_document_des,sud_file_path,sud_versionno FROM public.nt_t_grievance_document_version \r\n"
					+ "where sud_complain_no ='" + complainNo + "' and sud_transaction_type='" + strTransactionType
					+ "' and sud_doc_type='O' \r\n" + "order by sud_document_des, sud_versionno ;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();

				uploaddocumentManagementDTO.setDoc_Code(rs.getString("sud_doc_code"));
				uploaddocumentManagementDTO.setAdd_Doc_Description(rs.getString("sud_document_des"));
				uploaddocumentManagementDTO.setUploadFilePath(rs.getString("sud_file_path"));
				uploaddocumentManagementDTO.setVersionNo(rs.getInt("sud_versionno"));
				uploaddocumentManagementDTO.setDocumentName(uploaddocumentManagementDTO.getAdd_Doc_Description() + "_"
						+ uploaddocumentManagementDTO.getVersionNo());

				searchList.add(uploaddocumentManagementDTO);
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

	public int getVersionNumberForGrievance(DocumentManagementDTO uploaddocumentManagementDTO, String typeCodeTrans,
			String docType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<DocumentManagementDTO> versionList = new ArrayList<DocumentManagementDTO>(0);
		int output = 0;
		try {

			con = ConnectionManager.getConnection();

			String sql = "SELECT  sud_transaction_type, sud_complain_no,sud_doc_code, sud_document_des, sud_file_path,sud_versionno "
					+ " FROM public.nt_t_grievance_document_version " + " where sud_complain_no ='"
					+ uploaddocumentManagementDTO.getUpload_Permit() + "' and sud_transaction_type='" + typeCodeTrans
					+ "' and sud_doc_type='" + docType + "' and sud_doc_code='"
					+ uploaddocumentManagementDTO.getDoc_Code() + "' order by sud_versionno;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			int count = 0;
			while (rs.next()) {
				DocumentManagementDTO dto = new DocumentManagementDTO();
				dto.setVersionNo(rs.getInt("sud_versionno"));
				count = dto.getVersionNo();
				versionList.add(dto);

			}

			if (count == 0) {
				count = 1;
				output = count;

			} else {
				count++;
				output = count;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return output;
	}

	public String getFilePathForGrievance(String filePath, String doc_des, String permitNo, String transaction_Code,
			String doc_code) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String output = null;
		try {

			con = ConnectionManager.getConnection();

			String sql = "select distinct a.doc_code, a.doc_document_des,a.doc_mandatory_doc,b.sud_file_path,b.sud_transaction_type,b.sud_complain_no "
					+ " from nt_m_document a left outer join nt_t_grievance_document_version b on a.doc_code = b.sud_doc_code "
					+ " where a.doc_mandatory_doc='true' and b.sud_transaction_type='" + transaction_Code
					+ "' and a.doc_code ='" + doc_code + "'"
					 + " and b.sud_complain_no ='" + permitNo + "'";
					
			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {

				output = rs.getString("sud_file_path");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return output;
	}

	public int replaceDocForGrievance(String doc_des, String permitNo, String transaction_Code, String user,
			String docuString) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_grievance_document " + "SET  sud_modified_by='" + user
					+ "', sud_modified_date='" + timestamp + "', sud_file_path = '" + docuString + "'  \r\n"
					+ "where sud_complain_no ='" + permitNo + "'and sud_transaction_type='" + transaction_Code
					+ "' and sud_document_des ='" + doc_des + "'and sud_doc_type='M'    ;\r\n" + "";

			stmt = con.prepareStatement(sql);

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

	public int saveMandatoryUploadsVersionsForGrievance(String filePath, String doc_des, String doc_code,
			String permitNo, String transaction_Code, String user, int versionNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_grievance_document_version");

			String sql = "INSERT INTO public.nt_t_grievance_document_version \r\n"
					+ "(seqno, sud_transaction_type, sud_complain_no,sud_doc_code, sud_document_des, sud_file_path, sud_doc_type, sud_created_by, sud_created_date,sud_versionno)\r\n"
					+ "VALUES('" + seqNo + "', '" + transaction_Code + "',  '" + permitNo + "', '" + doc_code + "', '"
					+ doc_des + "', '" + filePath + "', 'M', '" + user + "', '" + timestamp + "', '" + versionNo
					+ "');\r\n" + "";

			stmt = con.prepareStatement(sql);

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

	public int saveMandatoryUploadsForGrievance(String filePath, String doc_des, String doc_code, String permitNo,
			String transaction_Code, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_grievance_document");

			String sql = "INSERT INTO public.nt_t_grievance_document \r\n"
					+ "(seqno, sud_transaction_type, sud_complain_no, sud_doc_code, sud_document_des, sud_file_path, sud_doc_type, sud_created_by, sud_created_date)\r\n"
					+ "VALUES('" + seqNo + "', '" + transaction_Code + "', '" + permitNo + "', '" + doc_code + "', '"
					+ doc_des + "', '" + filePath + "', 'M', '" + user + "', '" + timestamp + "');";

			stmt = con.prepareStatement(sql);
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

	public String getOptionalFilePathForGrievance(String filePath, String doc_des, String permitNo,
			String transaction_Code, String doc_code) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String output = null;
		try {

			con = ConnectionManager.getConnection();

			String sql = "select  a.doc_code, a.doc_document_des,a.doc_mandatory_doc,b.sud_file_path,b.sud_transaction_type,b.sud_complain_no \r\n"
					+ "from nt_m_document a left outer join nt_t_grievance_document_version b on a.doc_code = b.sud_doc_code \r\n"
					+ "where a.doc_mandatory_doc='false' and b.sud_transaction_type='" + transaction_Code
					+ "' and a.doc_code ='" + doc_code + "'  and b.sud_complain_no ='" + permitNo + "'  ";
			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {

				output = rs.getString("sud_file_path");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return output;
	}

	public int replaceOptionalDocForGrievance(String doc_des, String permitNo, String transaction_Code, String user,
			String docuString) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_grievance_document  \r\n" + "SET  sud_modified_by='" + user
					+ "', sud_modified_date='" + timestamp + "',sud_file_path = '" + docuString + "'      \r\n"
					+ "where sud_complain_no ='" + permitNo + "' and sud_transaction_type='" + transaction_Code
					+ "' and sud_document_des ='" + doc_des + "' and sud_doc_type='O'  ;\r\n" + "";

			stmt = con.prepareStatement(sql);

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

	public int saveOptionalUploadsVersionsForGrievance(String filePath, String doc_des, String doc_code,
			String permitNo, String transaction_Code, String user, int versionNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_grievance_document_version");

			String sql = "INSERT INTO public.nt_t_grievance_document_version \r\n"
					+ "(seqno, sud_transaction_type,sud_complain_no,sud_doc_code, sud_document_des, sud_file_path, sud_doc_type, sud_created_by, sud_created_date,sud_versionno)\r\n"
					+ "VALUES('" + seqNo + "', '" + transaction_Code + "' ,'" + permitNo + "', '" + doc_code + "', '"
					+ doc_des + "', '" + filePath + "', 'O', '" + user + "', '" + timestamp + "', '" + versionNo
					+ "');\r\n" + "";

			stmt = con.prepareStatement(sql);

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

	public int saveOptionalUploadsForGrievance(String filePath, String doc_des, String doc_code, String permitNo,
			String transaction_Code, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_grievance_document");

			String sql = "INSERT INTO public.nt_t_grievance_document \r\n"
					+ "(seqno, sud_transaction_type, sud_complain_no, sud_doc_code, sud_document_des, sud_file_path, sud_doc_type, sud_created_by, sud_created_date)\r\n"
					+ "VALUES('" + seqNo + "', '" + transaction_Code + "', '" + permitNo + "', '" + doc_code + "', '"
					+ doc_des + "', '" + filePath + "', 'O', '" + user + "', '" + timestamp + "');";

			stmt = con.prepareStatement(sql);

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

	/*----*/

	/* SIM Registration */
	public List<DocumentManagementDTO> mandatoryDocsForSIMReg(String typeCode, String simRegNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String filepath;

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();

			String sql = "select d.srd_document_des, d.srd_doc_code, d.sud_file_path \r\n"
					+ "from (select A.srd_document_des,A.srd_doc_code, srd_file_path as sud_file_path\r\n"
					+ "from public.nt_t_sim_registration_document A \r\n" + "where A.srd_sim_reg_no = '" + simRegNo
					+ "'\r\n" + "and A.srd_doc_type = 'M'\r\n" + "\r\n" + "union\r\n" + "\r\n"
					+ "select B.doc_document_des,B.doc_code,'X' as sud_file_path\r\n"
					+ "from public.nt_m_document B \r\n"
					+ "WHERE not EXISTS( select * from public.nt_t_sim_registration_document A \r\n"
					+ "								where A.srd_doc_code = B.doc_code\r\n"
					+ "									and A.srd_sim_reg_no = '" + simRegNo + "'\r\n"
					+ "									and A.srd_doc_type = 'M')\r\n"
					+ "					AND B.doc_mandatory_doc = 'true'\r\n"
					+ "					and B.doc_transaction_type = '" + typeCode
					+ "') as d order by d.srd_doc_code\r\n" + "";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploadDocument = new DocumentManagementDTO();

				uploadDocument.setDoc_Code(rs.getString("srd_doc_code"));
				uploadDocument.setAdd_Doc_Description(rs.getString("srd_document_des"));

				filepath = rs.getString("sud_file_path");

				if (filepath.equals("X")) {
					uploadDocument.setUploadFilePath(null);

				} else {

					uploadDocument.setUploadFilePath(rs.getString("sud_file_path"));
				}

				searchList.add(uploadDocument);

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

	public List<DocumentManagementDTO> optionalDocsForSIMReg(String typeCode, String simRegNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String filepath;

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();

			String sql = "select d.srd_document_des, d.srd_doc_code, d.sud_file_path \r\n"
					+ "from (select A.srd_document_des,A.srd_doc_code, srd_file_path as sud_file_path\r\n"
					+ "from public.nt_t_sim_registration_document A \r\n" + "where A.srd_sim_reg_no = '" + simRegNo
					+ "'\r\n" + "and A.srd_doc_type = 'O'\r\n" + "\r\n" + "union\r\n" + "\r\n"
					+ "select B.doc_document_des,B.doc_code,'X' as sud_file_path\r\n"
					+ "from public.nt_m_document B \r\n"
					+ "WHERE not EXISTS( select * from public.nt_t_sim_registration_document A \r\n"
					+ "								where A.srd_doc_code = B.doc_code\r\n"
					+ "									and A.srd_sim_reg_no = '" + simRegNo + "'\r\n"
					+ "									and A.srd_doc_type = 'O')\r\n"
					+ "					AND B.doc_mandatory_doc = 'false'\r\n"
					+ "					and B.doc_transaction_type = '" + typeCode
					+ "') as d order by d.srd_doc_code\r\n" + "";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploadDocument = new DocumentManagementDTO();

				uploadDocument.setDoc_Code(rs.getString("srd_doc_code"));
				uploadDocument.setAdd_Doc_Description(rs.getString("srd_document_des"));

				filepath = rs.getString("sud_file_path");

				if (filepath.equals("X")) {
					uploadDocument.setUploadOptionalFilePath(null);
					uploadDocument.setUploadFilePath(null);
				} else {
					uploadDocument.setUploadFilePath(filepath);
					uploadDocument.setUploadOptionalFilePath(rs.getString("sud_file_path"));
				}

				searchList.add(uploadDocument);

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

	public List<DocumentManagementDTO> simRegMandatoryListM(String simRegNo, String strTransactionType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT srd_doc_code,srd_document_des,srd_file_path,sud_versionno FROM public.nt_t_sim_registration_document_version \r\n"
					+ "where srd_sim_reg_no ='" + simRegNo + "' " + " and srd_doc_type='M' \r\n"
					+ "order by srd_document_des, sud_versionno ;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();

				uploaddocumentManagementDTO.setDoc_Code(rs.getString("srd_doc_code"));
				uploaddocumentManagementDTO.setAdd_Doc_Description(rs.getString("srd_document_des"));
				uploaddocumentManagementDTO.setUploadFilePath(rs.getString("srd_file_path"));
				uploaddocumentManagementDTO.setVersionNo(rs.getInt("sud_versionno"));
				uploaddocumentManagementDTO.setDocumentName(uploaddocumentManagementDTO.getAdd_Doc_Description() + "_"
						+ uploaddocumentManagementDTO.getVersionNo());

				searchList.add(uploaddocumentManagementDTO);
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

	public List<DocumentManagementDTO> simRegOptionalListM(String simRegNo, String strTransactionType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT srd_doc_code,srd_document_des,srd_file_path,sud_versionno FROM public.nt_t_sim_registration_document_version \r\n"
					+ "where srd_sim_reg_no ='" + simRegNo + "'" + " and srd_doc_type='O' \r\n"
					+ "order by srd_document_des, sud_versionno ;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();

				uploaddocumentManagementDTO.setDoc_Code(rs.getString("srd_doc_code"));
				uploaddocumentManagementDTO.setAdd_Doc_Description(rs.getString("srd_document_des"));
				uploaddocumentManagementDTO.setUploadFilePath(rs.getString("srd_file_path"));
				uploaddocumentManagementDTO.setVersionNo(rs.getInt("sud_versionno"));
				uploaddocumentManagementDTO.setDocumentName(uploaddocumentManagementDTO.getAdd_Doc_Description() + "_"
						+ uploaddocumentManagementDTO.getVersionNo());

				searchList.add(uploaddocumentManagementDTO);
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

	public int getVersionNumberForSim(DocumentManagementDTO uploaddocumentManagementDTO, String typeCodeTrans,
			String docType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<DocumentManagementDTO> versionList = new ArrayList<DocumentManagementDTO>(0);
		int output = 0;
		try {

			con = ConnectionManager.getConnection();

			String sql = "SELECT  srd_sim_reg_no,srd_doc_code, srd_document_des, srd_file_path,sud_versionno "
					+ " FROM public.nt_t_sim_registration_document_version " + " where srd_sim_reg_no ='"
					+ uploaddocumentManagementDTO.getUpload_Permit() + "" + "' and srd_doc_type='" + docType
					+ "' and srd_doc_code='" + uploaddocumentManagementDTO.getDoc_Code() + "' order by sud_versionno;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			int count = 0;
			while (rs.next()) {
				DocumentManagementDTO dto = new DocumentManagementDTO();
				dto.setVersionNo(rs.getInt("sud_versionno"));
				count = dto.getVersionNo();
				versionList.add(dto);

			}

			if (count == 0) {
				count = 1;
				output = count;

			} else {
				count++;
				output = count;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return output;
	}

	public String getFilePathForSIM(String filePath, String doc_des, String permitNo, String transaction_Code,
			String doc_code) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String output = null;
		try {

			con = ConnectionManager.getConnection();

			String sql = "select distinct a.doc_code, a.doc_document_des,a.doc_mandatory_doc,b.srd_file_path,b.srd_sim_reg_no "
					+ " from nt_m_document a left outer join nt_t_sim_registration_document_version b on a.doc_code = b.srd_doc_code "
					+ " where a.doc_mandatory_doc='true' " + " and a.doc_code ='" + doc_code + "'  "
					+ " and b.srd_sim_reg_no ='" + permitNo + "'";
			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {

				output = rs.getString("srd_file_path");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return output;
	}

	public int replaceDocForSIM(String doc_des, String permitNo, String transaction_Code, String user,
			String docuString) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_sim_registration_document " + "SET  srd_modified_by='" + user
					+ "', srd_modified_date='" + timestamp + "', srd_file_path = '" + docuString + "'  \r\n"
					+ "where srd_sim_reg_no ='" + permitNo + "' and srd_document_des ='" + doc_des
					+ "' and srd_doc_type='M'    ;\r\n" + "";

			stmt = con.prepareStatement(sql);

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

	public int saveMandatoryUploadsVersionsForSIM(String filePath, String doc_des, String doc_code, String permitNo,
			String transaction_Code, String user, int versionNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_sim_registration_document_version");

			String sql = "INSERT INTO public.nt_t_sim_registration_document_version \r\n"
					+ "(seqno, srd_sim_reg_no,srd_doc_code, srd_document_des, srd_file_path, srd_doc_type, srd_created_by, srd_created_date,sud_versionno)\r\n"
					+ "VALUES('" + seqNo + "', '" + permitNo + "', '" + doc_code + "', '" + doc_des + "', '" + filePath
					+ "', 'M', '" + user + "', '" + timestamp + "', '" + versionNo + "');\r\n" + "";

			stmt = con.prepareStatement(sql);

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

	public int saveMandatoryUploadsForSIM(String filePath, String doc_des, String doc_code, String permitNo,
			String transaction_Code, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_sim_registration_document");

			String sql = "INSERT INTO public.nt_t_sim_registration_document \r\n"
					+ "(seqno,  srd_sim_reg_no, srd_doc_code, srd_document_des, srd_file_path, srd_doc_type, srd_created_by, srd_created_date)\r\n"
					+ "VALUES('" + seqNo + "', '" + permitNo + "', '" + doc_code + "', '" + doc_des + "', '" + filePath
					+ "', 'M', '" + user + "', '" + timestamp + "');";

			stmt = con.prepareStatement(sql);
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

	public String getOptionalFilePathForSIM(String filePath, String doc_des, String permitNo, String transaction_Code,
			String doc_code) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String output = null;
		try {

			con = ConnectionManager.getConnection();

			String sql = "select distinct a.doc_code, a.doc_document_des,a.doc_mandatory_doc,b.srd_file_path,b.srd_sim_reg_no "
					+ " from nt_m_document a left outer join nt_t_sim_registration_document_version b on a.doc_code = b.srd_doc_code "
					+ " where a.doc_mandatory_doc='false' " + " and a.doc_code ='" + doc_code
					+ "'  and b.srd_sim_reg_no ='" + permitNo + "' ";
			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {

				output = rs.getString("srd_file_path");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return output;
	}

	public int replaceOptionalDocForSIM(String doc_des, String permitNo, String transaction_Code, String user,
			String docuString) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_sim_registration_document " + "SET  srd_modified_by='" + user
					+ "', srd_modified_date='" + timestamp + "', srd_file_path = '" + docuString + "'  \r\n"
					+ "where srd_sim_reg_no ='" + permitNo + "' and srd_document_des ='" + doc_des
					+ "' and srd_doc_type='O'    ;\r\n" + "";

			stmt = con.prepareStatement(sql);

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

	public int saveOptionalUploadsVersionsForSIM(String filePath, String doc_des, String doc_code, String permitNo,
			String transaction_Code, String user, int versionNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_sim_registration_document_version");

			String sql = "INSERT INTO public.nt_t_sim_registration_document_version \r\n"
					+ "(seqno, srd_sim_reg_no,srd_doc_code, srd_document_des, srd_file_path, srd_doc_type, srd_created_by, srd_created_date,sud_versionno)\r\n"
					+ "VALUES('" + seqNo + "', '" + permitNo + "', '" + doc_code + "', '" + doc_des + "', '" + filePath
					+ "', 'O', '" + user + "', '" + timestamp + "', '" + versionNo + "');\r\n" + "";

			stmt = con.prepareStatement(sql);

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

	public int saveOptionalUploadsForSIM(String filePath, String doc_des, String doc_code, String permitNo,
			String transaction_Code, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_sim_registration_document");

			String sql = "INSERT INTO public.nt_t_sim_registration_document \r\n"
					+ "(seqno,  srd_sim_reg_no, srd_doc_code, srd_document_des, srd_file_path, srd_doc_type, srd_created_by, srd_created_date)\r\n"
					+ "VALUES('" + seqNo + "', '" + permitNo + "', '" + doc_code + "', '" + doc_des + "', '" + filePath
					+ "', 'O', '" + user + "', '" + timestamp + "');";

			stmt = con.prepareStatement(sql);
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

	/**/

	public int saveMandatoryUploadsVersions(String filePath, String doc_des, String doc_code, String permitNo,
			String applicationNo, String transaction_Code, String user, int versionNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_application_document_version");

			String sql = "INSERT INTO public.nt_t_application_document_version\r\n"
					+ "(seqno, apd_transaction_type, apd_application_no, apd_permit_no, apd_doc_code, apd_document_des, apd_file_path, apd_doc_type, apd_created_by, apd_created_date,apd_versionno)\r\n"
					+ "VALUES('" + seqNo + "', '" + transaction_Code + "', '" + applicationNo + "', '" + permitNo
					+ "', '" + doc_code + "', '" + doc_des + "', '" + filePath + "', 'M', '" + user + "', '" + timestamp
					+ "', '" + versionNo + "');\r\n" + "";

			stmt = con.prepareStatement(sql);

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

	public int saveMandatoryUploadsVersionsForSisuSariya(String filePath, String doc_des, String doc_code,
			String permitNo, String applicationNo, String transaction_Code, String user, int versionNo,
			Date expiryDate) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_subsidy_document_version ");

			String expiryDateString = "";
			if (expiryDate != null) {
				expiryDateString = (dateFormat.format(expiryDate));
			} else {
				expiryDateString = null;
			}

			String sql = "INSERT INTO public.nt_t_subsidy_document_version \r\n"
					+ "(seqno, sud_transaction_type, sud_request_no, sud_doc_code, sud_document_des, sud_file_path, sud_doc_type, sud_created_by, sud_created_date,sud_versionno,sud_expiry_date)\r\n"
					+ "VALUES('" + seqNo + "', '" + transaction_Code + "',  '" + permitNo + "', '" + doc_code + "', '"
					+ doc_des + "', '" + filePath + "', 'M', '" + user + "', '" + timestamp + "', '" + versionNo + "','"
					+ expiryDateString + "');\r\n" + "";

			stmt = con.prepareStatement(sql);

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

	public int saveMandatoryUploadsVersionsForSisuSariyaPermitHolder(String filePath, String doc_des, String doc_code,
			String permitNo, String applicationNo, String transaction_Code, String user, int versionNo,
			Date expiryDate) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_subsidy_document_version ");

			String expiryDateString = "";
			if (expiryDate != null) {
				expiryDateString = (dateFormat.format(expiryDate));
			} else {
				expiryDateString = null;
			}

			String sql = "INSERT INTO public.nt_t_subsidy_document_version \r\n"
					+ "(seqno, sud_transaction_type, sud_request_no,sud_ref_no,sud_doc_code, sud_document_des, sud_file_path, sud_doc_type, sud_created_by, sud_created_date,sud_versionno,sud_expiry_date)\r\n"
					+ "VALUES('" + seqNo + "', '" + transaction_Code + "',  '" + permitNo + "','" + applicationNo
					+ "','" + doc_code + "', '" + doc_des + "', '" + filePath + "', 'M', '" + user + "', '" + timestamp
					+ "', '" + versionNo + "', '" + expiryDateString + "');\r\n" + "";

			stmt = con.prepareStatement(sql);

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

	public int saveMandatoryUploadsVersionsForSisuSariyaAgreementRenewals(String filePath, String doc_des,
			String doc_code, String permitNo, String applicationNo, String transaction_Code, String user, int versionNo,
			String serviceNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_subsidy_document_version ");

			String sql = "INSERT INTO public.nt_t_subsidy_document_version \r\n"
					+ "(seqno, sud_transaction_type, sud_request_no,sud_ref_no,sud_service_no,sud_doc_code, sud_document_des, sud_file_path, sud_doc_type, sud_created_by, sud_created_date,sud_versionno)\r\n"
					+ "VALUES('" + seqNo + "', '" + transaction_Code + "',  '" + permitNo + "','" + applicationNo
					+ "','" + serviceNo + "','" + doc_code + "', '" + doc_des + "', '" + filePath + "', 'M', '" + user
					+ "', '" + timestamp + "', '" + versionNo + "');\r\n" + "";

			stmt = con.prepareStatement(sql);

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

	public int saveOptionalUploadsVersions(String filePath, String doc_des, String doc_code, String permitNo,
			String applicationNo, String transaction_Code, String user, int versionNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_application_document_version");

			String sql = "INSERT INTO public.nt_t_application_document_version\r\n"
					+ "(seqno, apd_transaction_type, apd_application_no, apd_permit_no, apd_doc_code, apd_document_des, apd_file_path, apd_doc_type, apd_created_by, apd_created_date,apd_versionno)\r\n"
					+ "VALUES('" + seqNo + "', '" + transaction_Code + "', '" + applicationNo + "', '" + permitNo
					+ "', '" + doc_code + "', '" + doc_des + "', '" + filePath + "', 'O', '" + user + "', '" + timestamp
					+ "', '" + versionNo + "');\r\n" + "";

			stmt = con.prepareStatement(sql);

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

	public int saveOptionalUploadsVersionsForSisuSariya(String filePath, String doc_des, String doc_code,
			String permitNo, String applicationNo, String transaction_Code, String user, int versionNo,
			Date expiryDate) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_subsidy_document_version");

			String expiryDateString = "";
			if (expiryDate != null) {
				expiryDateString = (dateFormat.format(expiryDate));
			} else {
				expiryDateString = null;
			}

			String sql = "INSERT INTO public.nt_t_subsidy_document_version\r\n"
					+ "(seqno, sud_transaction_type,sud_request_no,sud_doc_code, sud_document_des, sud_file_path, sud_doc_type, sud_created_by, sud_created_date,sud_versionno,sud_expiry_date)\r\n"
					+ "VALUES('" + seqNo + "', '" + transaction_Code + "','" + permitNo + "', '" + doc_code + "', '"
					+ doc_des + "', '" + filePath + "', 'O', '" + user + "', '" + timestamp + "', '" + versionNo + "','"
					+ expiryDateString + "');\r\n" + "";

			stmt = con.prepareStatement(sql);

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

	public int saveOptionalUploadsVersionsForSisuSariyaPermitHolder(String filePath, String doc_des, String doc_code,
			String permitNo, String applicationNo, String transaction_Code, String user, int versionNo,
			Date expiryDate) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_subsidy_document_version");

			String expiryDateString = "";
			if (expiryDate != null) {
				expiryDateString = (dateFormat.format(expiryDate));
			} else {
				expiryDateString = null;
			}

			String sql = "INSERT INTO public.nt_t_subsidy_document_version\r\n"
					+ "(seqno, sud_transaction_type,sud_request_no,sud_ref_no,sud_doc_code, sud_document_des, sud_file_path, sud_doc_type, sud_created_by, sud_created_date,sud_versionno,sud_expiry_date)\r\n"
					+ "VALUES('" + seqNo + "', '" + transaction_Code + "','" + permitNo + "','" + applicationNo + "','"
					+ doc_code + "', '" + doc_des + "', '" + filePath + "', 'O', '" + user + "', '" + timestamp + "', '"
					+ versionNo + "','" + expiryDateString + "');\r\n" + "";

			stmt = con.prepareStatement(sql);

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

	public int saveOptionalUploadsVersionsForSisuSariyaAgreementRenewals(String filePath, String doc_des,
			String doc_code, String permitNo, String applicationNo, String transaction_Code, String user, int versionNo,
			String serviceNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_subsidy_document_version");

			String sql = "INSERT INTO public.nt_t_subsidy_document_version\r\n"
					+ "(seqno, sud_transaction_type,sud_request_no,sud_ref_no,sud_service_no,sud_doc_code, sud_document_des, sud_file_path, sud_doc_type, sud_created_by, sud_created_date,sud_versionno)\r\n"
					+ "VALUES('" + seqNo + "', '" + transaction_Code + "','" + permitNo + "','" + applicationNo + "','"
					+ serviceNo + "','" + doc_code + "', '" + doc_des + "', '" + filePath + "', 'O', '" + user + "', '"
					+ timestamp + "', '" + versionNo + "');\r\n" + "";

			stmt = con.prepareStatement(sql);

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

	public int saveUserManagementMandatoryUploadsVersions(String transaction_Code, String empNo, String doc_code,
			String doc_des, String docuString, String user, int versionNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_employee_document_version");

			String sql = "INSERT INTO public.nt_t_employee_document_version\r\n"
					+ "(seqno, emp_transaction_type, emp_ref_no, emp_doc_code, emp_document_des, emp_file_path, emp_doc_type, emp_created_by, emp_created_date,  emp_versionno)\r\n"
					+ "VALUES('" + seqNo + "', '" + transaction_Code + "', '" + empNo + "', '" + doc_code + "', '"
					+ doc_des + "', '" + docuString + "', 'M', '" + user + "', '" + timestamp + "', '" + versionNo
					+ "');";

			stmt = con.prepareStatement(sql);

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

	public int saveUserManagementOptionalUploadsVersions(String transaction_Code, String empNo, String doc_code,
			String doc_des, String docuString, String user, int versionNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_employee_document_version");

			String sql = "INSERT INTO public.nt_t_employee_document_version\r\n"
					+ "(seqno, emp_transaction_type, emp_ref_no, emp_doc_code, emp_document_des, emp_file_path, emp_doc_type, emp_created_by, emp_created_date,  emp_versionno)\r\n"
					+ "VALUES('" + seqNo + "', '" + transaction_Code + "', '" + empNo + "', '" + doc_code + "', '"
					+ doc_des + "', '" + docuString + "', 'O', '" + user + "', '" + timestamp + "', '" + versionNo
					+ "');";

			stmt = con.prepareStatement(sql);

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

	public List<DocumentManagementDTO> ViewHistoryList(DocumentManagementDTO viewDocumentHistory) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<DocumentManagementDTO> historyList = new ArrayList<DocumentManagementDTO>();

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT apd_versionno,apd_document_des,apd_file_path FROM public.nt_t_application_document_version\r\n"
					+ "where apd_permit_no='" + viewDocumentHistory.getUpload_Permit() + "' and apd_transaction_type='"
					+ viewDocumentHistory.getTransaction_Type_Code() + "' and apd_doc_code='"
					+ viewDocumentHistory.getDoc_Code() + "'\r\n" + "ORDER BY apd_versionno";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				DocumentManagementDTO viewDocumentHistoryList = new DocumentManagementDTO();

				viewDocumentHistoryList.setVersionNo(rs.getInt("apd_versionno"));
				viewDocumentHistoryList.setUploadFilePath(rs.getString("apd_file_path"));
				viewDocumentHistoryList.setAdd_Doc_Description(rs.getString("apd_document_des"));

				if (viewDocumentHistory.getCommonUploadFilePath()
						.equalsIgnoreCase(viewDocumentHistoryList.getUploadFilePath())) {

					viewDocumentHistoryList.setCheckdelete(false);
				} else {

					viewDocumentHistoryList.setCheckdelete(true);
				}

				historyList.add(viewDocumentHistoryList);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return historyList;

	}

	public List<DocumentManagementDTO> ViewUserManagementHistoryList(
			DocumentManagementDTO viewUserManagementDocumentHistory) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<DocumentManagementDTO> historyList = new ArrayList<DocumentManagementDTO>();

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT emp_versionno,emp_document_des,emp_file_path FROM public.nt_t_employee_document_version\r\n"
					+ "where emp_ref_no='" + viewUserManagementDocumentHistory.getUpload_empNo()
					+ "' and emp_transaction_type='" + viewUserManagementDocumentHistory.getTransaction_Type_Code()
					+ "' and emp_doc_code='" + viewUserManagementDocumentHistory.getDoc_Code() + "'\r\n"
					+ "ORDER BY emp_versionno";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				DocumentManagementDTO viewDocumentHistoryList = new DocumentManagementDTO();

				viewDocumentHistoryList.setVersionNo(rs.getInt("emp_versionno"));
				viewDocumentHistoryList.setUploadFilePath(rs.getString("emp_file_path"));
				viewDocumentHistoryList.setAdd_Doc_Description(rs.getString("emp_document_des"));

				if (viewUserManagementDocumentHistory.getCommonUploadFilePath()
						.equalsIgnoreCase(viewDocumentHistoryList.getUploadFilePath())) {

					viewDocumentHistoryList.setCheckdelete(false);
				} else {

					viewDocumentHistoryList.setCheckdelete(true);
				}

				historyList.add(viewDocumentHistoryList);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return historyList;

	}

	public int DeleteVersionDoc(String path) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "DELETE FROM public.nt_t_application_document_version\r\n" + "WHERE apd_file_path='" + path
					+ "'";

			stmt = con.prepareStatement(sql);

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

	public int insertDeleteRecord(List<DocumentManagementDTO> deleteDoc, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_h_delete_application_document_version");

			String sql = "INSERT INTO public.nt_h_delete_application_document_version\r\n"
					+ "(seqno, apd_transaction_type, apd_application_no, apd_permit_no, apd_doc_code, apd_document_des, apd_file_path, apd_doc_type, apd_deleted_by, apd_deleted_date, apd_versionno)\r\n"
					+ "VALUES(" + seqNo + ", '" + deleteDoc.get(0).getTransaction_Type_Code() + "', '"
					+ deleteDoc.get(0).getDoc_App() + "', '" + deleteDoc.get(0).getPermitName() + "', '"
					+ deleteDoc.get(0).getCheckDocCode() + "', '" + deleteDoc.get(0).getCheckDocDes() + "', '"
					+ deleteDoc.get(0).getUploadFilePath_file() + "', '" + deleteDoc.get(0).getDocumentName() + "', '"
					+ user + "', '" + timestamp + "', '" + deleteDoc.get(0).getVersionNo() + "');";

			stmt = con.prepareStatement(sql);

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

	public List<DocumentManagementDTO> getDeleteDocumentInfo(String path) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<DocumentManagementDTO> deleteList = new ArrayList<DocumentManagementDTO>();

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT * FROM public.nt_t_application_document_version WHERE apd_file_path='" + path + "';";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				DocumentManagementDTO deleteListInfo = new DocumentManagementDTO();

				deleteListInfo.setTransaction_Type_Code(rs.getString("apd_transaction_type"));
				deleteListInfo.setDoc_App(rs.getString("apd_application_no"));
				deleteListInfo.setPermitName(rs.getString("apd_permit_no"));
				deleteListInfo.setCheckDocCode(rs.getString("apd_doc_code"));
				deleteListInfo.setCheckDocDes(rs.getString("apd_document_des"));
				deleteListInfo.setUploadFilePath_file(rs.getString("apd_file_path"));
				deleteListInfo.setDocumentName(rs.getString("apd_doc_type"));
				deleteListInfo.setVersionNo(rs.getInt("apd_versionno"));

				deleteList.add(deleteListInfo);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return deleteList;

	}

	public int DeleteUserManagementVersionDoc(String empNo, String transaction_Code, String docDes, int versionNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "DELETE FROM public.nt_t_employee_document_version\r\n" + "WHERE emp_ref_no='" + empNo
					+ "' and emp_transaction_type='" + transaction_Code + "' and emp_document_des='" + docDes
					+ "' and emp_versionno='" + versionNo + "';";

			stmt = con.prepareStatement(sql);

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

	public String getuploadfilepath(DocumentManagementDTO viewDocumentHistory) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String output = null;
		try {

			con = ConnectionManager.getConnection();

			String sql = "SELECT apd_file_path\r\n" + "FROM public.nt_t_application_document\r\n"
					+ "where apd_permit_no='" + viewDocumentHistory.getUpload_Permit() + "' and apd_transaction_type='"
					+ viewDocumentHistory.getTransaction_Type_Code() + "' and apd_doc_code='"
					+ viewDocumentHistory.getDoc_Code() + "';";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {

				output = rs.getString("apd_file_path");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return output;
	}

	public String getUserManagementuploadfilepath(DocumentManagementDTO viewUserManagementDocumentHistory) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String output = null;
		try {

			con = ConnectionManager.getConnection();

			String sql = "SELECT emp_file_path\r\n" + "FROM public.nt_t_employee_document\r\n" + "where emp_ref_no='"
					+ viewUserManagementDocumentHistory.getUpload_empNo() + "' and emp_transaction_type='"
					+ viewUserManagementDocumentHistory.getTransaction_Type_Code() + "' and emp_doc_code='"
					+ viewUserManagementDocumentHistory.getDoc_Code() + "';";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {

				output = rs.getString("emp_file_path");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return output;
	}

	public List<DocumentManagementDTO> userManagementMandatoryDocs(String typeCode, String uploadEmpNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String filepathOp;

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {

			con = ConnectionManager.getConnection();

			String sql = "select d.emp_document_des, d.emp_doc_code, d.emp_transaction_type, d.emp_file_path\r\n"
					+ "from (select A.emp_document_des,A.emp_doc_code,A.emp_transaction_type,emp_file_path as emp_file_path\r\n"
					+ "from public.nt_t_employee_document A\r\n" + "where A.emp_ref_no = '" + uploadEmpNo + "'\r\n"
					+ "and A.emp_doc_type = 'M'\r\n" + "and A.emp_transaction_type = '" + typeCode + "'\r\n" + "\r\n"
					+ "union\r\n" + "\r\n"
					+ "select B.doc_document_des,B.doc_code,B.doc_transaction_type,'X' as emp_file_path\r\n"
					+ "from public.nt_m_document B\r\n"
					+ "WHERE not EXISTS( select * from public.nt_t_employee_document A \r\n"
					+ "				where A.emp_doc_code = B.doc_code\r\n" + "				and A.emp_ref_no = '"
					+ uploadEmpNo + "'\r\n" + "				and A.emp_doc_type = 'M'\r\n"
					+ "				and A.emp_transaction_type = '" + typeCode + "' )			\r\n"
					+ "AND B.doc_mandatory_doc = 'true'\r\n" + "and B.doc_transaction_type = '" + typeCode
					+ "') as d order by d.emp_doc_code";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {

				DocumentManagementDTO uploadDocument = new DocumentManagementDTO();
				uploadDocument.setDoc_Code(rs.getString("emp_doc_code"));
				uploadDocument.setAdd_Doc_Description(rs.getString("emp_document_des"));

				filepathOp = rs.getString("emp_file_path");

				if (filepathOp.equals("X")) {
					uploadDocument.setUploadFilePath(null);

				} else {
					uploadDocument.setUploadFilePath(rs.getString("emp_file_path"));
				}
				uploadDocument.setTransaction_Type(rs.getString("emp_transaction_type"));

				searchList.add(uploadDocument);

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

	public List<DocumentManagementDTO> userManagementOptionalDocs(String typeCode, String uploadEmpNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String filepathOp;

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {

			con = ConnectionManager.getConnection();

			String sql = "select d.emp_document_des, d.emp_doc_code, d.emp_transaction_type, d.emp_file_path\r\n"
					+ "from (select A.emp_document_des,A.emp_doc_code,A.emp_transaction_type,emp_file_path as emp_file_path\r\n"
					+ "from public.nt_t_employee_document A\r\n" + "where A.emp_ref_no = '" + uploadEmpNo + "'\r\n"
					+ "and A.emp_doc_type = 'O'\r\n" + "and A.emp_transaction_type = '" + typeCode + "'\r\n" + "\r\n"
					+ "union\r\n" + "\r\n"
					+ "select B.doc_document_des,B.doc_code,B.doc_transaction_type,'X' as emp_file_path\r\n"
					+ "from public.nt_m_document B\r\n"
					+ "WHERE not EXISTS( select * from public.nt_t_employee_document A \r\n"
					+ "				where A.emp_doc_code = B.doc_code\r\n" + "				and A.emp_ref_no = '"
					+ uploadEmpNo + "'\r\n" + "				and A.emp_doc_type = 'O'\r\n"
					+ "				and A.emp_transaction_type = '" + typeCode + "' )			\r\n"
					+ "AND B.doc_mandatory_doc = 'false'\r\n" + "and B.doc_transaction_type = '" + typeCode
					+ "') as d order by d.emp_doc_code";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {

				DocumentManagementDTO uploadDocument = new DocumentManagementDTO();
				uploadDocument.setDoc_Code(rs.getString("emp_doc_code"));
				uploadDocument.setAdd_Doc_Description(rs.getString("emp_document_des"));

				filepathOp = rs.getString("emp_file_path");

				if (filepathOp.equals("X")) {
					uploadDocument.setUploadFilePath(null);

				} else {
					uploadDocument.setUploadFilePath(filepathOp);
					uploadDocument.setUploadOptionalFilePath(rs.getString("emp_file_path"));
				}
				uploadDocument.setTransaction_Type(rs.getString("emp_transaction_type"));

				searchList.add(uploadDocument);

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

	public List<DocumentManagementDTO> FilePathList(String ApplicationNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<DocumentManagementDTO> filePathList = new ArrayList<DocumentManagementDTO>();

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT seqno,apd_file_path,apd_application_no,apd_doc_code FROM public.nt_t_application_document where apd_application_no='"
					+ ApplicationNo + "' order by apd_doc_code";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				DocumentManagementDTO documentFilePath = new DocumentManagementDTO();

				documentFilePath.setFilePath(rs.getString("apd_file_path"));
				documentFilePath.setSeq(rs.getLong("seqno"));
				filePathList.add(documentFilePath);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return filePathList;

	}

	public int updateFilePaths(List<DocumentManagementDTO> newFilePathList, String showpermitNo, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			for (int i = 0; i < newFilePathList.size(); i++) {

				String sql = "UPDATE public.nt_t_application_document\r\n" + "SET apd_file_path = '"
						+ newFilePathList.get(i).getFilePath() + "',apd_permit_no='" + showpermitNo
						+ "',apd_modified_by='" + user + "',apd_modified_date='" + timestamp + "' \r\n"
						+ "where seqno = '" + newFilePathList.get(i).getSeq() + "' ";
				stmt = con.prepareStatement(sql);

				stmt.executeUpdate();
				con.commit();

			}

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

	public List<DocumentManagementDTO> FilePathVersionList(String ApplicationNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<DocumentManagementDTO> filePathList = new ArrayList<DocumentManagementDTO>();

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT seqno,apd_application_no,apd_doc_code,apd_file_path FROM public.nt_t_application_document_version where apd_application_no='"
					+ ApplicationNo + "' order by apd_doc_code";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				DocumentManagementDTO documentFileVersionPath = new DocumentManagementDTO();

				documentFileVersionPath.setFilePath(rs.getString("apd_file_path"));
				documentFileVersionPath.setSeq(rs.getLong("seqno"));
				filePathList.add(documentFileVersionPath);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return filePathList;

	}

	public int updateVersionFilePaths(List<DocumentManagementDTO> newFilePathVersionList, String showpermitNo,
			String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			for (int i = 0; i < newFilePathVersionList.size(); i++) {

				String sql = "UPDATE public.nt_t_application_document_version\r\n" + "SET apd_file_path = '"
						+ newFilePathVersionList.get(i).getFilePath() + "',apd_permit_no='" + showpermitNo
						+ "',apd_modified_by='" + user + "',apd_modified_date='" + timestamp + "' \r\n"
						+ "where seqno = '" + newFilePathVersionList.get(i).getSeq() + "' ";
				stmt = con.prepareStatement(sql);

				stmt.executeUpdate();
				con.commit();

			}

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

	public List<DocumentManagementDTO> newPermitMandatoryList(String permitNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String transactionTypeCode = null;
		try {
			transactionTypeCode = PropertyReader.getPropertyValue("queue.transaction.type.code.new.permit");
		} catch (ApplicationException e) {
			e.printStackTrace();
		}

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT apd_doc_code,apd_document_des,apd_file_path,apd_versionno FROM public.nt_t_application_document_version \r\n"
					+ "where apd_permit_no='" + permitNo + "' and apd_transaction_type='" + transactionTypeCode
					+ "' and apd_doc_type='M' \r\n" + "order by apd_document_des, apd_versionno ;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();

				uploaddocumentManagementDTO.setDoc_Code(rs.getString("apd_doc_code"));
				uploaddocumentManagementDTO.setAdd_Doc_Description(rs.getString("apd_document_des"));
				uploaddocumentManagementDTO.setUploadFilePath(rs.getString("apd_file_path"));
				uploaddocumentManagementDTO.setVersionNo(rs.getInt("apd_versionno"));
				uploaddocumentManagementDTO.setDocumentName(uploaddocumentManagementDTO.getAdd_Doc_Description() + "_"
						+ uploaddocumentManagementDTO.getVersionNo());

				searchList.add(uploaddocumentManagementDTO);
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

	public List<DocumentManagementDTO> newPermitOptionalList(String permitNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String transactionTypeCode = null;
		try {
			transactionTypeCode = PropertyReader.getPropertyValue("queue.transaction.type.code.new.permit");
		} catch (ApplicationException e) {
			e.printStackTrace();
		}

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT apd_doc_code,apd_document_des,apd_file_path,apd_versionno FROM public.nt_t_application_document_version \r\n"
					+ "where apd_permit_no='" + permitNo + "' and apd_transaction_type='" + transactionTypeCode
					+ "' and apd_doc_type='O' \r\n" + "order by apd_document_des, apd_versionno ;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();

				uploaddocumentManagementDTO.setDoc_Code(rs.getString("apd_doc_code"));
				uploaddocumentManagementDTO.setAdd_Doc_Description(rs.getString("apd_document_des"));
				uploaddocumentManagementDTO.setUploadOptionalFilePath(rs.getString("apd_file_path"));
				uploaddocumentManagementDTO.setVersionNo(rs.getInt("apd_versionno"));
				uploaddocumentManagementDTO.setDocumentName(uploaddocumentManagementDTO.getAdd_Doc_Description() + "_"
						+ uploaddocumentManagementDTO.getVersionNo());

				searchList.add(uploaddocumentManagementDTO);
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

	public List<DocumentManagementDTO> permitRenewalMandatoryList(String permitNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String transactionTypeCode = null;
		try {
			transactionTypeCode = PropertyReader.getPropertyValue("queue.transaction.type.code.renewal");
		} catch (ApplicationException e) {
			e.printStackTrace();
		}

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT apd_application_no,apd_doc_code,apd_document_des,apd_file_path,apd_versionno FROM public.nt_t_application_document_version \r\n"
					+ "where apd_permit_no='" + permitNo + "' and apd_transaction_type='" + transactionTypeCode
					+ "' and apd_doc_type='M' \r\n" + "order by apd_document_des, apd_versionno ;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();

				uploaddocumentManagementDTO.setDoc_App(rs.getString("apd_application_no"));
				uploaddocumentManagementDTO.setDoc_Code(rs.getString("apd_doc_code"));
				uploaddocumentManagementDTO.setAdd_Doc_Description(rs.getString("apd_document_des"));
				uploaddocumentManagementDTO.setUploadFilePath(rs.getString("apd_file_path"));
				uploaddocumentManagementDTO.setVersionNo(rs.getInt("apd_versionno"));
				uploaddocumentManagementDTO.setDocumentName(uploaddocumentManagementDTO.getAdd_Doc_Description() + "_"
						+ uploaddocumentManagementDTO.getVersionNo());

				searchList.add(uploaddocumentManagementDTO);
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

	public List<DocumentManagementDTO> permitRenewalMandatoryNewList(String permitNo,
			List<DocumentManagementDTO> RenewalApplicationList) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String transactionTypeCode = null;
		try {
			transactionTypeCode = PropertyReader.getPropertyValue("queue.transaction.type.code.renewal");
		} catch (ApplicationException e) {
			e.printStackTrace();
		}

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql;
			for (int i = 0; i < RenewalApplicationList.size(); i++) {

				String applicationNo = RenewalApplicationList.get(i).getDoc_App();

				sql = "SELECT apd_application_no,apd_doc_code,apd_document_des,apd_file_path,apd_versionno FROM public.nt_t_application_document_version \r\n"
						+ "where apd_permit_no='" + permitNo + "'and apd_application_no='" + applicationNo
						+ "' and apd_transaction_type='" + transactionTypeCode + "' and apd_doc_type='M' \r\n"
						+ "order by apd_document_des, apd_versionno desc ;";

				ps = con.prepareStatement(sql);

				rs = ps.executeQuery();

				while (rs.next()) {
					DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();

					uploaddocumentManagementDTO.setDoc_App(rs.getString("apd_application_no"));
					uploaddocumentManagementDTO.setDoc_Code(rs.getString("apd_doc_code"));
					uploaddocumentManagementDTO.setAdd_Doc_Description(rs.getString("apd_document_des"));
					uploaddocumentManagementDTO.setUploadFilePath(rs.getString("apd_file_path"));
					uploaddocumentManagementDTO.setVersionNo(rs.getInt("apd_versionno"));
					uploaddocumentManagementDTO.setDocumentName(uploaddocumentManagementDTO.getAdd_Doc_Description()
							+ "_" + uploaddocumentManagementDTO.getVersionNo());

					searchList.add(uploaddocumentManagementDTO);
				}
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

	public List<DocumentManagementDTO> permitRenewalOptionalList(String permitNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String transactionTypeCode = null;
		try {
			transactionTypeCode = PropertyReader.getPropertyValue("queue.transaction.type.code.renewal");
		} catch (ApplicationException e) {
			e.printStackTrace();
		}

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT apd_doc_code,apd_document_des,apd_file_path,apd_versionno FROM public.nt_t_application_document_version \r\n"
					+ "where apd_permit_no='" + permitNo + "' and apd_transaction_type='" + transactionTypeCode
					+ "' and apd_doc_type='O' \r\n" + "order by apd_document_des, apd_versionno ;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();

				uploaddocumentManagementDTO.setDoc_Code(rs.getString("apd_doc_code"));
				uploaddocumentManagementDTO.setAdd_Doc_Description(rs.getString("apd_document_des"));
				uploaddocumentManagementDTO.setUploadOptionalFilePath(rs.getString("apd_file_path"));
				uploaddocumentManagementDTO.setVersionNo(rs.getInt("apd_versionno"));
				uploaddocumentManagementDTO.setDocumentName(uploaddocumentManagementDTO.getAdd_Doc_Description() + "_"
						+ uploaddocumentManagementDTO.getVersionNo());

				searchList.add(uploaddocumentManagementDTO);
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

	public List<DocumentManagementDTO> permitRenewalOptionalNewList(String permitNo,
			List<DocumentManagementDTO> RenewalApplicationList) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String transactionTypeCode = null;
		try {
			transactionTypeCode = PropertyReader.getPropertyValue("queue.transaction.type.code.renewal");
		} catch (ApplicationException e) {
			e.printStackTrace();
		}

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql;
			for (int i = 0; i < RenewalApplicationList.size(); i++) {

				String applicationNo = RenewalApplicationList.get(i).getDoc_App();
				sql = "SELECT apd_application_no,apd_doc_code,apd_document_des,apd_file_path,apd_versionno FROM public.nt_t_application_document_version \r\n"
						+ "where apd_permit_no='" + permitNo + "'and apd_application_no='" + applicationNo
						+ "' and apd_transaction_type='" + transactionTypeCode + "' and apd_doc_type='O' \r\n"
						+ "order by apd_document_des, apd_versionno desc ;";

				ps = con.prepareStatement(sql);

				rs = ps.executeQuery();

				while (rs.next()) {
					DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();

					uploaddocumentManagementDTO.setDoc_App(rs.getString("apd_application_no"));
					uploaddocumentManagementDTO.setDoc_Code(rs.getString("apd_doc_code"));
					uploaddocumentManagementDTO.setAdd_Doc_Description(rs.getString("apd_document_des"));
					uploaddocumentManagementDTO.setUploadOptionalFilePath(rs.getString("apd_file_path"));
					uploaddocumentManagementDTO.setVersionNo(rs.getInt("apd_versionno"));
					uploaddocumentManagementDTO.setDocumentName(uploaddocumentManagementDTO.getAdd_Doc_Description()
							+ "_" + uploaddocumentManagementDTO.getVersionNo());

					searchList.add(uploaddocumentManagementDTO);
				}
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

	public List<DocumentManagementDTO> backlogManagementOptionalList(String permitNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String transactionTypeCode = null;
		try {
			transactionTypeCode = PropertyReader.getPropertyValue("queue.transaction.type.code.backlog");
		} catch (ApplicationException e) {
			e.printStackTrace();
		}

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT apd_doc_code,apd_document_des,apd_file_path,apd_versionno FROM public.nt_t_application_document_version \r\n"
					+ "where apd_permit_no='" + permitNo + "' and apd_transaction_type='" + transactionTypeCode
					+ "' and apd_doc_type='O' \r\n" + "order by apd_document_des, apd_versionno desc ;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				;
				DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();

				uploaddocumentManagementDTO.setDoc_Code(rs.getString("apd_doc_code"));
				uploaddocumentManagementDTO.setAdd_Doc_Description(rs.getString("apd_document_des"));
				uploaddocumentManagementDTO.setUploadFilePath(rs.getString("apd_file_path"));
				uploaddocumentManagementDTO.setVersionNo(rs.getInt("apd_versionno"));
				uploaddocumentManagementDTO.setDocumentName(uploaddocumentManagementDTO.getAdd_Doc_Description() + "_"
						+ uploaddocumentManagementDTO.getVersionNo());

				searchList.add(uploaddocumentManagementDTO);
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

	public List<DocumentManagementDTO> amendmentToBusOwnerMandatoryList(String permitNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String transactionTypeCode = null;
		try {
			transactionTypeCode = PropertyReader.getPropertyValue("queue.transaction.type.code.ammendment.bus.owner");
		} catch (ApplicationException e) {
			e.printStackTrace();
		}

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT apd_doc_code,apd_document_des,apd_file_path,apd_versionno FROM public.nt_t_application_document_version \r\n"
					+ "where apd_permit_no='" + permitNo + "' and apd_transaction_type='" + transactionTypeCode
					+ "' and apd_doc_type='M' \r\n" + "order by apd_document_des, apd_versionno ;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();

				uploaddocumentManagementDTO.setDoc_Code(rs.getString("apd_doc_code"));
				uploaddocumentManagementDTO.setAdd_Doc_Description(rs.getString("apd_document_des"));
				uploaddocumentManagementDTO.setUploadFilePath(rs.getString("apd_file_path"));
				uploaddocumentManagementDTO.setVersionNo(rs.getInt("apd_versionno"));
				uploaddocumentManagementDTO.setDocumentName(uploaddocumentManagementDTO.getAdd_Doc_Description() + "_"
						+ uploaddocumentManagementDTO.getVersionNo());

				searchList.add(uploaddocumentManagementDTO);
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

	public List<DocumentManagementDTO> amendmentToBusOwnerOptionalList(String permitNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String transactionTypeCode = null;
		try {
			transactionTypeCode = PropertyReader.getPropertyValue("queue.transaction.type.code.ammendment.bus.owner");
		} catch (ApplicationException e) {
			e.printStackTrace();
		}

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT apd_doc_code,apd_document_des,apd_file_path,apd_versionno FROM public.nt_t_application_document_version \r\n"
					+ "where apd_permit_no='" + permitNo + "' and apd_transaction_type='" + transactionTypeCode
					+ "' and apd_doc_type='O' \r\n" + "order by apd_document_des, apd_versionno ;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();

				uploaddocumentManagementDTO.setDoc_Code(rs.getString("apd_doc_code"));
				uploaddocumentManagementDTO.setAdd_Doc_Description(rs.getString("apd_document_des"));
				uploaddocumentManagementDTO.setUploadOptionalFilePath(rs.getString("apd_file_path"));
				uploaddocumentManagementDTO.setVersionNo(rs.getInt("apd_versionno"));
				uploaddocumentManagementDTO.setDocumentName(uploaddocumentManagementDTO.getAdd_Doc_Description() + "_"
						+ uploaddocumentManagementDTO.getVersionNo());

				searchList.add(uploaddocumentManagementDTO);
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

	public List<DocumentManagementDTO> amendmentToBusMandatoryList(String permitNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String transactionTypeCode = null;
		try {
			transactionTypeCode = PropertyReader.getPropertyValue("queue.transaction.type.code.ammendment.bus");
		} catch (ApplicationException e) {
			e.printStackTrace();
		}

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT apd_doc_code,apd_document_des,apd_file_path,apd_versionno FROM public.nt_t_application_document_version \r\n"
					+ "where apd_permit_no='" + permitNo + "' and apd_transaction_type='" + transactionTypeCode
					+ "' and apd_doc_type='M' \r\n" + "order by apd_document_des, apd_versionno ;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();

				uploaddocumentManagementDTO.setDoc_Code(rs.getString("apd_doc_code"));
				uploaddocumentManagementDTO.setAdd_Doc_Description(rs.getString("apd_document_des"));
				uploaddocumentManagementDTO.setUploadFilePath(rs.getString("apd_file_path"));
				uploaddocumentManagementDTO.setVersionNo(rs.getInt("apd_versionno"));
				uploaddocumentManagementDTO.setDocumentName(uploaddocumentManagementDTO.getAdd_Doc_Description() + "_"
						+ uploaddocumentManagementDTO.getVersionNo());

				searchList.add(uploaddocumentManagementDTO);
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

	public List<DocumentManagementDTO> viewAmendmentsMandatoryList(String permitNo, String transactionTypeCode) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT apd_doc_code,apd_document_des,apd_file_path FROM public.nt_t_application_document\r\n"
					+ "where apd_permit_no='" + permitNo + "' and apd_transaction_type='" + transactionTypeCode
					+ "' and apd_doc_type='M' ";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();

				uploaddocumentManagementDTO.setDoc_Code(rs.getString("apd_doc_code"));
				uploaddocumentManagementDTO.setAdd_Doc_Description(rs.getString("apd_document_des"));
				uploaddocumentManagementDTO.setUploadFilePath(rs.getString("apd_file_path"));

				searchList.add(uploaddocumentManagementDTO);
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

	public List<DocumentManagementDTO> viewAmendmentsOptionalList(String permitNo, String transactionTypeCode) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT apd_doc_code,apd_document_des,apd_file_path FROM public.nt_t_application_document\r\n"
					+ "where apd_permit_no='" + permitNo + "' and apd_transaction_type='" + transactionTypeCode
					+ "' and apd_doc_type='O' ";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();

				uploaddocumentManagementDTO.setDoc_Code(rs.getString("apd_doc_code"));
				uploaddocumentManagementDTO.setAdd_Doc_Description(rs.getString("apd_document_des"));
				uploaddocumentManagementDTO.setUploadFilePath(rs.getString("apd_file_path"));

				searchList.add(uploaddocumentManagementDTO);
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

	public List<DocumentManagementDTO> amendmentToBusOptionalList(String permitNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String transactionTypeCode = null;
		try {
			transactionTypeCode = PropertyReader.getPropertyValue("queue.transaction.type.code.ammendment.bus");
		} catch (ApplicationException e) {
			e.printStackTrace();
		}

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT apd_doc_code,apd_document_des,apd_file_path,apd_versionno FROM public.nt_t_application_document_version \r\n"
					+ "where apd_permit_no='" + permitNo + "' and apd_transaction_type='" + transactionTypeCode
					+ "' and apd_doc_type='O' \r\n" + "order by apd_document_des, apd_versionno ;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();

				uploaddocumentManagementDTO.setDoc_Code(rs.getString("apd_doc_code"));
				uploaddocumentManagementDTO.setAdd_Doc_Description(rs.getString("apd_document_des"));
				uploaddocumentManagementDTO.setUploadOptionalFilePath(rs.getString("apd_file_path"));
				uploaddocumentManagementDTO.setVersionNo(rs.getInt("apd_versionno"));
				uploaddocumentManagementDTO.setDocumentName(uploaddocumentManagementDTO.getAdd_Doc_Description() + "_"
						+ uploaddocumentManagementDTO.getVersionNo());

				searchList.add(uploaddocumentManagementDTO);
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

	public List<DocumentManagementDTO> amendmentToServiceMandatoryList(String permitNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String transactionTypeCode = null;
		try {
			transactionTypeCode = PropertyReader.getPropertyValue("queue.transaction.type.code.ammendment.service");
		} catch (ApplicationException e) {
			e.printStackTrace();
		}

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT apd_doc_code,apd_document_des,apd_file_path,apd_versionno FROM public.nt_t_application_document_version \r\n"
					+ "where apd_permit_no='" + permitNo + "' and apd_transaction_type='" + transactionTypeCode
					+ "' and apd_doc_type='M' \r\n" + "order by apd_document_des, apd_versionno ;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();

				uploaddocumentManagementDTO.setDoc_Code(rs.getString("apd_doc_code"));
				uploaddocumentManagementDTO.setAdd_Doc_Description(rs.getString("apd_document_des"));
				uploaddocumentManagementDTO.setUploadFilePath(rs.getString("apd_file_path"));
				uploaddocumentManagementDTO.setVersionNo(rs.getInt("apd_versionno"));
				uploaddocumentManagementDTO.setDocumentName(uploaddocumentManagementDTO.getAdd_Doc_Description() + "_"
						+ uploaddocumentManagementDTO.getVersionNo());

				searchList.add(uploaddocumentManagementDTO);
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

	public List<DocumentManagementDTO> amendmentToServiceOptionalList(String permitNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String transactionTypeCode = null;
		try {
			transactionTypeCode = PropertyReader.getPropertyValue("queue.transaction.type.code.ammendment.service");
		} catch (ApplicationException e) {
			e.printStackTrace();
		}

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT apd_doc_code,apd_document_des,apd_file_path,apd_versionno FROM public.nt_t_application_document_version \r\n"
					+ "where apd_permit_no='" + permitNo + "' and apd_transaction_type='" + transactionTypeCode
					+ "' and apd_doc_type='O' \r\n" + "order by apd_document_des, apd_versionno ;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();

				uploaddocumentManagementDTO.setDoc_Code(rs.getString("apd_doc_code"));
				uploaddocumentManagementDTO.setAdd_Doc_Description(rs.getString("apd_document_des"));
				uploaddocumentManagementDTO.setUploadOptionalFilePath(rs.getString("apd_file_path"));
				uploaddocumentManagementDTO.setVersionNo(rs.getInt("apd_versionno"));
				uploaddocumentManagementDTO.setDocumentName(uploaddocumentManagementDTO.getAdd_Doc_Description() + "_"
						+ uploaddocumentManagementDTO.getVersionNo());

				searchList.add(uploaddocumentManagementDTO);
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

	public List<DocumentManagementDTO> amendmentToOwnerBusMandatoryList(String permitNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String transactionTypeCode = null;
		try {
			transactionTypeCode = PropertyReader
					.getPropertyValue("queue.transaction.type.code.ammendment.owner.bus.owner");
		} catch (ApplicationException e) {
			e.printStackTrace();
		}

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT apd_doc_code,apd_document_des,apd_file_path,apd_versionno FROM public.nt_t_application_document_version \r\n"
					+ "where apd_permit_no='" + permitNo + "' and apd_transaction_type='" + transactionTypeCode
					+ "' and apd_doc_type='M' \r\n" + "order by apd_document_des, apd_versionno ;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();

				uploaddocumentManagementDTO.setDoc_Code(rs.getString("apd_doc_code"));
				uploaddocumentManagementDTO.setAdd_Doc_Description(rs.getString("apd_document_des"));
				uploaddocumentManagementDTO.setUploadFilePath(rs.getString("apd_file_path"));
				uploaddocumentManagementDTO.setVersionNo(rs.getInt("apd_versionno"));
				uploaddocumentManagementDTO.setDocumentName(uploaddocumentManagementDTO.getAdd_Doc_Description() + "_"
						+ uploaddocumentManagementDTO.getVersionNo());

				searchList.add(uploaddocumentManagementDTO);
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

	public List<DocumentManagementDTO> amendmentToOwnerBusOptionalList(String permitNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String transactionTypeCode = null;
		try {
			transactionTypeCode = PropertyReader
					.getPropertyValue("queue.transaction.type.code.ammendment.owner.bus.owner");
		} catch (ApplicationException e) {
			e.printStackTrace();
		}

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT apd_doc_code,apd_document_des,apd_file_path,apd_versionno FROM public.nt_t_application_document_version \r\n"
					+ "where apd_permit_no='" + permitNo + "' and apd_transaction_type='" + transactionTypeCode
					+ "' and apd_doc_type='O' \r\n" + "order by apd_document_des, apd_versionno ;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();

				uploaddocumentManagementDTO.setDoc_Code(rs.getString("apd_doc_code"));
				uploaddocumentManagementDTO.setAdd_Doc_Description(rs.getString("apd_document_des"));
				uploaddocumentManagementDTO.setUploadOptionalFilePath(rs.getString("apd_file_path"));
				uploaddocumentManagementDTO.setVersionNo(rs.getInt("apd_versionno"));
				uploaddocumentManagementDTO.setDocumentName(uploaddocumentManagementDTO.getAdd_Doc_Description() + "_"
						+ uploaddocumentManagementDTO.getVersionNo());

				searchList.add(uploaddocumentManagementDTO);
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

	public List<DocumentManagementDTO> amendmentToServiceBusMandatoryList(String permitNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String transactionTypeCode = null;
		try {
			transactionTypeCode = PropertyReader
					.getPropertyValue("queue.transaction.type.code.ammendment.service.bus.owner");
		} catch (ApplicationException e) {
			e.printStackTrace();
		}

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT apd_doc_code,apd_document_des,apd_file_path,apd_versionno FROM public.nt_t_application_document_version \r\n"
					+ "where apd_permit_no='" + permitNo + "' and apd_transaction_type='" + transactionTypeCode
					+ "' and apd_doc_type='M' \r\n" + "order by apd_document_des, apd_versionno ;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();

				uploaddocumentManagementDTO.setDoc_Code(rs.getString("apd_doc_code"));
				uploaddocumentManagementDTO.setAdd_Doc_Description(rs.getString("apd_document_des"));
				uploaddocumentManagementDTO.setUploadFilePath(rs.getString("apd_file_path"));
				uploaddocumentManagementDTO.setVersionNo(rs.getInt("apd_versionno"));
				uploaddocumentManagementDTO.setDocumentName(uploaddocumentManagementDTO.getAdd_Doc_Description() + "_"
						+ uploaddocumentManagementDTO.getVersionNo());

				searchList.add(uploaddocumentManagementDTO);
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

	public List<DocumentManagementDTO> amendmentToServiceBusOptionalList(String permitNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String transactionTypeCode = null;
		try {
			transactionTypeCode = PropertyReader
					.getPropertyValue("queue.transaction.type.code.ammendment.service.bus.owner");
		} catch (ApplicationException e) {
			e.printStackTrace();
		}

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT apd_doc_code,apd_document_des,apd_file_path,apd_versionno FROM public.nt_t_application_document_version \r\n"
					+ "where apd_permit_no='" + permitNo + "' and apd_transaction_type='" + transactionTypeCode
					+ "' and apd_doc_type='O' \r\n" + "order by apd_document_des, apd_versionno ;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();

				uploaddocumentManagementDTO.setDoc_Code(rs.getString("apd_doc_code"));
				uploaddocumentManagementDTO.setAdd_Doc_Description(rs.getString("apd_document_des"));
				uploaddocumentManagementDTO.setUploadOptionalFilePath(rs.getString("apd_file_path"));
				uploaddocumentManagementDTO.setVersionNo(rs.getInt("apd_versionno"));
				uploaddocumentManagementDTO.setDocumentName(uploaddocumentManagementDTO.getAdd_Doc_Description() + "_"
						+ uploaddocumentManagementDTO.getVersionNo());

				searchList.add(uploaddocumentManagementDTO);
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

	public List<DocumentManagementDTO> surveyMandatoryList(String surveyNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String transactionTypeCode = null;
		try {
			transactionTypeCode = PropertyReader.getPropertyValue("queue.transaction.type.code.survey");
		} catch (ApplicationException e) {
			e.printStackTrace();
		}

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT emp_doc_code,emp_document_des,emp_file_path,emp_versionno FROM public.nt_t_employee_document_version \r\n"
					+ "where emp_ref_no='" + surveyNo + "' and emp_transaction_type='" + transactionTypeCode
					+ "' and emp_doc_type='M' \r\n" + "order by emp_document_des, emp_versionno ;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();

				uploaddocumentManagementDTO.setDoc_Code(rs.getString("emp_doc_code"));
				uploaddocumentManagementDTO.setAdd_Doc_Description(rs.getString("emp_document_des"));
				uploaddocumentManagementDTO.setUploadFilePath(rs.getString("emp_file_path"));
				uploaddocumentManagementDTO.setVersionNo(rs.getInt("emp_versionno"));
				uploaddocumentManagementDTO.setDocumentName(uploaddocumentManagementDTO.getAdd_Doc_Description() + "_"
						+ uploaddocumentManagementDTO.getVersionNo());

				searchList.add(uploaddocumentManagementDTO);
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

	public List<DocumentManagementDTO> surveyOptionalList(String surveyNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String transactionTypeCode = null;
		try {
			transactionTypeCode = PropertyReader.getPropertyValue("queue.transaction.type.code.survey");
		} catch (ApplicationException e) {

			e.printStackTrace();
		}

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT emp_doc_code,emp_document_des,emp_file_path,emp_versionno FROM public.nt_t_employee_document_version \r\n"
					+ "where emp_ref_no='" + surveyNo + "' and emp_transaction_type='" + transactionTypeCode
					+ "' and emp_doc_type='O' \r\n" + "order by emp_document_des, emp_versionno ;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();

				uploaddocumentManagementDTO.setDoc_Code(rs.getString("emp_doc_code"));
				uploaddocumentManagementDTO.setAdd_Doc_Description(rs.getString("emp_document_des"));
				uploaddocumentManagementDTO.setUploadOptionalFilePath(rs.getString("emp_file_path"));
				uploaddocumentManagementDTO.setVersionNo(rs.getInt("emp_versionno"));
				uploaddocumentManagementDTO.setDocumentName(uploaddocumentManagementDTO.getAdd_Doc_Description() + "_"
						+ uploaddocumentManagementDTO.getVersionNo());

				searchList.add(uploaddocumentManagementDTO);
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

	public List<DocumentManagementDTO> sisuSariyaMandatoryList(String requestNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String transactionTypeCode = null;
		try {
			transactionTypeCode = PropertyReader.getPropertyValue("queue.transaction.type.code.sisusariya");
		} catch (ApplicationException e) {

			e.printStackTrace();
		}

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT sud_expiry_date,sud_doc_code,sud_document_des,sud_file_path,sud_versionno FROM public.nt_t_subsidy_document_version \r\n"
					+ "where sud_request_no='" + requestNo
					+ "'  and sud_ref_no is null and sud_service_no is null and sud_transaction_type='"
					+ transactionTypeCode + "' and sud_doc_type='M' \r\n"
					+ "order by sud_document_des, sud_versionno ;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();

				uploaddocumentManagementDTO.setDoc_Code(rs.getString("sud_doc_code"));
				uploaddocumentManagementDTO.setAdd_Doc_Description(rs.getString("sud_document_des"));
				String expiryDateString = rs.getString("sud_expiry_date");
				if (expiryDateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date expiryDate = originalFormat.parse(expiryDateString);
					uploaddocumentManagementDTO.setExpiryDate(expiryDate);
				}
				uploaddocumentManagementDTO.setUploadFilePath(rs.getString("sud_file_path"));
				uploaddocumentManagementDTO.setVersionNo(rs.getInt("sud_versionno"));
				uploaddocumentManagementDTO.setDocumentName(uploaddocumentManagementDTO.getAdd_Doc_Description() + "_"
						+ uploaddocumentManagementDTO.getVersionNo());

				searchList.add(uploaddocumentManagementDTO);
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

	public List<DocumentManagementDTO> sisuSariyaOptionalList(String requestNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String transactionTypeCode = null;
		try {
			transactionTypeCode = PropertyReader.getPropertyValue("queue.transaction.type.code.sisusariya");
		} catch (ApplicationException e) {

			e.printStackTrace();
		}

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT sud_expiry_date,sud_doc_code,sud_document_des,sud_file_path,sud_versionno FROM public.nt_t_subsidy_document_version \r\n"
					+ "where sud_request_no='" + requestNo
					+ "' and sud_ref_no is null and sud_service_no is null and sud_transaction_type='"
					+ transactionTypeCode + "' and sud_doc_type='O' \r\n"
					+ "order by sud_document_des, sud_versionno ;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();

				uploaddocumentManagementDTO.setDoc_Code(rs.getString("sud_doc_code"));
				uploaddocumentManagementDTO.setAdd_Doc_Description(rs.getString("sud_document_des"));
				String expiryDateString = rs.getString("sud_expiry_date");
				if (expiryDateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date expiryDate = originalFormat.parse(expiryDateString);
					uploaddocumentManagementDTO.setExpiryDate(expiryDate);
				}
				uploaddocumentManagementDTO.setUploadFilePath(rs.getString("sud_file_path"));
				uploaddocumentManagementDTO.setVersionNo(rs.getInt("sud_versionno"));
				uploaddocumentManagementDTO.setDocumentName(uploaddocumentManagementDTO.getAdd_Doc_Description() + "_"
						+ uploaddocumentManagementDTO.getVersionNo());

				searchList.add(uploaddocumentManagementDTO);
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

	public List<DocumentManagementDTO> sisuSariyaPermitHolderMandatoryList(String requestNo, String refNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String transactionTypeCode = null;
		try {
			transactionTypeCode = PropertyReader.getPropertyValue("queue.transaction.type.code.sisusariya");
		} catch (ApplicationException e) {

			e.printStackTrace();
		}

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT sud_expiry_date,sud_doc_code,sud_document_des,sud_file_path,sud_versionno FROM public.nt_t_subsidy_document_version \r\n"
					+ "where sud_request_no='" + requestNo + "' and sud_ref_no='" + refNo
					+ "' and sud_service_no is null and sud_transaction_type='" + transactionTypeCode
					+ "' and sud_doc_type='M' \r\n" + "order by sud_document_des, sud_versionno ;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();

				uploaddocumentManagementDTO.setDoc_Code(rs.getString("sud_doc_code"));
				uploaddocumentManagementDTO.setAdd_Doc_Description(rs.getString("sud_document_des"));
				String expiryDateString = rs.getString("sud_expiry_date");
				if (expiryDateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date expiryDate = originalFormat.parse(expiryDateString);
					uploaddocumentManagementDTO.setExpiryDate(expiryDate);
				}
				uploaddocumentManagementDTO.setUploadFilePath(rs.getString("sud_file_path"));
				uploaddocumentManagementDTO.setVersionNo(rs.getInt("sud_versionno"));
				uploaddocumentManagementDTO.setDocumentName(uploaddocumentManagementDTO.getAdd_Doc_Description() + "_"
						+ uploaddocumentManagementDTO.getVersionNo());

				searchList.add(uploaddocumentManagementDTO);
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

	public List<DocumentManagementDTO> sisuSariyaPermitHolderOptionalList(String requestNo, String refNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String transactionTypeCode = null;
		try {
			transactionTypeCode = PropertyReader.getPropertyValue("queue.transaction.type.code.sisusariya");
		} catch (ApplicationException e) {

			e.printStackTrace();
		}

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT sud_expiry_date,sud_doc_code,sud_document_des,sud_file_path,sud_versionno FROM public.nt_t_subsidy_document_version \r\n"
					+ "where sud_request_no='" + requestNo + "'and sud_ref_no = '" + refNo
					+ "' and  sud_service_no is null and sud_transaction_type='" + transactionTypeCode
					+ "' and sud_doc_type='O' \r\n" + "order by sud_document_des, sud_versionno ;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();

				uploaddocumentManagementDTO.setDoc_Code(rs.getString("sud_doc_code"));
				uploaddocumentManagementDTO.setAdd_Doc_Description(rs.getString("sud_document_des"));
				String expiryDateString = rs.getString("sud_expiry_date");
				if (expiryDateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date expiryDate = originalFormat.parse(expiryDateString);
					uploaddocumentManagementDTO.setExpiryDate(expiryDate);
				}
				uploaddocumentManagementDTO.setUploadFilePath(rs.getString("sud_file_path"));
				uploaddocumentManagementDTO.setVersionNo(rs.getInt("sud_versionno"));
				uploaddocumentManagementDTO.setDocumentName(uploaddocumentManagementDTO.getAdd_Doc_Description() + "_"
						+ uploaddocumentManagementDTO.getVersionNo());

				searchList.add(uploaddocumentManagementDTO);
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

	public List<DocumentManagementDTO> sisuSariyaAgreementRenewalsMandatoryList(String requestNo, String refNo,
			String serviceNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String transactionTypeCode = null;
		try {
			transactionTypeCode = PropertyReader.getPropertyValue("queue.transaction.type.code.sisusariya");
		} catch (ApplicationException e) {

			e.printStackTrace();
		}

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT sud_doc_code,sud_document_des,sud_file_path,sud_versionno FROM public.nt_t_subsidy_document_version \r\n"
					+ "where sud_request_no='" + requestNo + "' and sud_ref_no='" + refNo + "'and sud_service_no='"
					+ serviceNo + "' and sud_transaction_type='" + transactionTypeCode + "' and sud_doc_type='M' \r\n"
					+ "order by sud_document_des, sud_versionno ;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();

				uploaddocumentManagementDTO.setDoc_Code(rs.getString("sud_doc_code"));
				uploaddocumentManagementDTO.setAdd_Doc_Description(rs.getString("sud_document_des"));
				uploaddocumentManagementDTO.setUploadFilePath(rs.getString("sud_file_path"));
				uploaddocumentManagementDTO.setVersionNo(rs.getInt("sud_versionno"));
				uploaddocumentManagementDTO.setDocumentName(uploaddocumentManagementDTO.getAdd_Doc_Description() + "_"
						+ uploaddocumentManagementDTO.getVersionNo());

				searchList.add(uploaddocumentManagementDTO);
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

	public List<DocumentManagementDTO> sisuSariyaAgreementRenewalsOptionalList(String requestNo, String refNo,
			String serviceNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String transactionTypeCode = null;
		try {
			transactionTypeCode = PropertyReader.getPropertyValue("queue.transaction.type.code.sisusariya");
		} catch (ApplicationException e) {

			e.printStackTrace();
		}

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT sud_doc_code,sud_document_des,sud_file_path,sud_versionno FROM public.nt_t_subsidy_document_version \r\n"
					+ "where sud_request_no='" + requestNo + "'and sud_ref_no = '" + refNo + "'and sud_service_no='"
					+ serviceNo + "' and sud_transaction_type='" + transactionTypeCode + "' and sud_doc_type='O' \r\n"
					+ "order by sud_document_des, sud_versionno ;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();

				uploaddocumentManagementDTO.setDoc_Code(rs.getString("sud_doc_code"));
				uploaddocumentManagementDTO.setAdd_Doc_Description(rs.getString("sud_document_des"));
				uploaddocumentManagementDTO.setUploadFilePath(rs.getString("sud_file_path"));
				uploaddocumentManagementDTO.setVersionNo(rs.getInt("sud_versionno"));
				uploaddocumentManagementDTO.setDocumentName(uploaddocumentManagementDTO.getAdd_Doc_Description() + "_"
						+ uploaddocumentManagementDTO.getVersionNo());

				searchList.add(uploaddocumentManagementDTO);
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

	public List<DocumentManagementDTO> tenderMandatoryList(String permitNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String transactionTypeCode = null;
		try {
			transactionTypeCode = PropertyReader.getPropertyValue("queue.transaction.type.code.tender");
		} catch (ApplicationException e) {

			e.printStackTrace();
		}

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT apd_doc_code,apd_document_des,apd_file_path,apd_versionno FROM public.nt_t_application_document_version \r\n"
					+ "where apd_permit_no='" + permitNo + "' and apd_transaction_type='" + transactionTypeCode
					+ "' and apd_doc_type='M' \r\n" + "order by apd_document_des, apd_versionno ;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();

				uploaddocumentManagementDTO.setDoc_Code(rs.getString("apd_doc_code"));
				uploaddocumentManagementDTO.setAdd_Doc_Description(rs.getString("apd_document_des"));
				uploaddocumentManagementDTO.setUploadFilePath(rs.getString("apd_file_path"));
				uploaddocumentManagementDTO.setVersionNo(rs.getInt("apd_versionno"));
				uploaddocumentManagementDTO.setDocumentName(uploaddocumentManagementDTO.getAdd_Doc_Description() + "_"
						+ uploaddocumentManagementDTO.getVersionNo());

				searchList.add(uploaddocumentManagementDTO);
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

	public List<DocumentManagementDTO> tenderOptionalList(String permitNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String transactionTypeCode = null;
		try {
			transactionTypeCode = PropertyReader.getPropertyValue("queue.transaction.type.code.tender");
		} catch (ApplicationException e) {

			e.printStackTrace();
		}

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT apd_doc_code,apd_document_des,apd_file_path,apd_versionno FROM public.nt_t_application_document_version \r\n"
					+ "where apd_permit_no='" + permitNo + "' and apd_transaction_type='" + transactionTypeCode
					+ "' and apd_doc_type='O' \r\n" + "order by apd_document_des, apd_versionno ;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();

				uploaddocumentManagementDTO.setDoc_Code(rs.getString("apd_doc_code"));
				uploaddocumentManagementDTO.setAdd_Doc_Description(rs.getString("apd_document_des"));
				uploaddocumentManagementDTO.setUploadOptionalFilePath(rs.getString("apd_file_path"));
				uploaddocumentManagementDTO.setVersionNo(rs.getInt("apd_versionno"));
				uploaddocumentManagementDTO.setDocumentName(uploaddocumentManagementDTO.getAdd_Doc_Description() + "_"
						+ uploaddocumentManagementDTO.getVersionNo());

				searchList.add(uploaddocumentManagementDTO);
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

	public List<String> GetPermitNoListForAmendments(String transCode) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<String> permitList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String sql = "select amd_permit_no FROM public.nt_m_amendments where amd_trn_type='" + transCode
					+ "' and  amd_permit_no is not null order by amd_permit_no";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {

				permitList.add(rs.getString("amd_permit_no"));
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return permitList;

	}

	public List<String> GetApplicationNoListForAmendments(String transCode) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<String> permitList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String sql = "select amd_application_no FROM public.nt_m_amendments where amd_trn_type='" + transCode
					+ "' and  amd_application_no is not null order by amd_application_no";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {

				permitList.add(rs.getString("amd_application_no"));
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return permitList;

	}

	public List<String> GetSurveyNoListForSurvey() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<String> applicationList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT ini_surveyno FROM public.nt_t_initiate_survey where ini_surveyno is not null order by ini_surveyno";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {

				applicationList.add(rs.getString("ini_surveyno"));
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return applicationList;

	}

	public List<String> GetSurveyRequestNoListForSurvey() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<String> applicationList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT ini_isu_requestno FROM public.nt_t_initiate_survey where ini_isu_requestno is not null order by ini_isu_requestno";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {

				applicationList.add(rs.getString("ini_isu_requestno"));
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return applicationList;

	}

	public List<String> GetTenderApplicationNoListForTender() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<String> applicationList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT tap_application_no FROM public.nt_m_tender_applicant order by tap_application_no";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {

				applicationList.add(rs.getString("tap_application_no"));
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return applicationList;

	}

	public String getBacklogApplicationByPermitNo(String permitNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String output = null;
		try {

			con = ConnectionManager.getConnection();

			String sql = "SELECT pm_application_no FROM public.nt_t_pm_application where pm_permit_no='" + permitNo
					+ "' and pm_application_no LIKE 'BAP%'";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {

				output = rs.getString("pm_application_no");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return output;
	}

	public int updateBacklogDocumentAppNo(String permitNo, String applicationNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "update nt_t_application_document set apd_application_no='" + applicationNo
					+ "' where apd_permit_no='" + permitNo + "';";

			stmt = con.prepareStatement(sql);

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

	public int updateBacklogDocumentVersionAppNo(String permitNo, String applicationNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "update nt_t_application_document_version set apd_application_no='" + applicationNo
					+ "' where apd_permit_no='" + permitNo + "';";

			stmt = con.prepareStatement(sql);

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

	public List<DocumentManagementDTO> gamiSariyaMandatoryList(String requestNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String transactionTypeCode = null;
		try {
			transactionTypeCode = PropertyReader.getPropertyValue("queue.transaction.type.code.gamisariya");
		} catch (ApplicationException e) {

			e.printStackTrace();
		}

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT sud_doc_code,sud_document_des,sud_file_path,sud_versionno FROM public.nt_t_subsidy_document_version \r\n"
					+ "where sud_request_no='" + requestNo
					+ "'  and sud_ref_no is null and sud_service_no is null and sud_transaction_type='"
					+ transactionTypeCode + "' and sud_doc_type='M' \r\n"
					+ "order by sud_document_des, sud_versionno ;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();

				uploaddocumentManagementDTO.setDoc_Code(rs.getString("sud_doc_code"));
				uploaddocumentManagementDTO.setAdd_Doc_Description(rs.getString("sud_document_des"));
				uploaddocumentManagementDTO.setUploadFilePath(rs.getString("sud_file_path"));
				uploaddocumentManagementDTO.setVersionNo(rs.getInt("sud_versionno"));
				uploaddocumentManagementDTO.setDocumentName(uploaddocumentManagementDTO.getAdd_Doc_Description() + "_"
						+ uploaddocumentManagementDTO.getVersionNo());

				searchList.add(uploaddocumentManagementDTO);
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

	public List<DocumentManagementDTO> gamiSariyaOptionalList(String requestNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String transactionTypeCode = null;
		try {
			transactionTypeCode = PropertyReader.getPropertyValue("queue.transaction.type.code.gamisariya");
		} catch (ApplicationException e) {

			e.printStackTrace();
		}

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT sud_doc_code,sud_document_des,sud_file_path,sud_versionno FROM public.nt_t_subsidy_document_version \r\n"
					+ "where sud_request_no='" + requestNo
					+ "' and sud_ref_no is null and sud_service_no is null and sud_transaction_type='"
					+ transactionTypeCode + "' and sud_doc_type='O' \r\n"
					+ "order by sud_document_des, sud_versionno ;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();

				uploaddocumentManagementDTO.setDoc_Code(rs.getString("sud_doc_code"));
				uploaddocumentManagementDTO.setAdd_Doc_Description(rs.getString("sud_document_des"));
				uploaddocumentManagementDTO.setUploadFilePath(rs.getString("sud_file_path"));
				uploaddocumentManagementDTO.setVersionNo(rs.getInt("sud_versionno"));
				uploaddocumentManagementDTO.setDocumentName(uploaddocumentManagementDTO.getAdd_Doc_Description() + "_"
						+ uploaddocumentManagementDTO.getVersionNo());

				searchList.add(uploaddocumentManagementDTO);
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

	public List<DocumentManagementDTO> gamiSariyaSurveyRequestMandatoryList(String requestNo, String refNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String transactionTypeCode = null;
		try {
			transactionTypeCode = PropertyReader.getPropertyValue("queue.transaction.type.code.gamisariya");
		} catch (ApplicationException e) {

			e.printStackTrace();
		}

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT sud_doc_code,sud_document_des,sud_file_path,sud_versionno FROM public.nt_t_subsidy_document_version \r\n"
					+ "where sud_request_no='" + requestNo + "' and sud_ref_no='" + refNo
					+ "' and sud_service_no is null and sud_transaction_type='" + transactionTypeCode
					+ "' and sud_doc_type='M' \r\n" + "order by sud_document_des, sud_versionno ;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();

				uploaddocumentManagementDTO.setDoc_Code(rs.getString("sud_doc_code"));
				uploaddocumentManagementDTO.setAdd_Doc_Description(rs.getString("sud_document_des"));
				uploaddocumentManagementDTO.setUploadFilePath(rs.getString("sud_file_path"));
				uploaddocumentManagementDTO.setVersionNo(rs.getInt("sud_versionno"));
				uploaddocumentManagementDTO.setDocumentName(uploaddocumentManagementDTO.getAdd_Doc_Description() + "_"
						+ uploaddocumentManagementDTO.getVersionNo());

				searchList.add(uploaddocumentManagementDTO);
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

	public List<DocumentManagementDTO> gamiSariyaSurveyRequestOptionalList(String requestNo, String refNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String transactionTypeCode = null;
		try {
			transactionTypeCode = PropertyReader.getPropertyValue("queue.transaction.type.code.gamisariya");
		} catch (ApplicationException e) {

			e.printStackTrace();
		}

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT sud_doc_code,sud_document_des,sud_file_path,sud_versionno FROM public.nt_t_subsidy_document_version \r\n"
					+ "where sud_request_no='" + requestNo + "'and sud_ref_no = '" + refNo
					+ "' and  sud_service_no is null and sud_transaction_type='" + transactionTypeCode
					+ "' and sud_doc_type='O' \r\n" + "order by sud_document_des, sud_versionno ;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();

				uploaddocumentManagementDTO.setDoc_Code(rs.getString("sud_doc_code"));
				uploaddocumentManagementDTO.setAdd_Doc_Description(rs.getString("sud_document_des"));
				uploaddocumentManagementDTO.setUploadFilePath(rs.getString("sud_file_path"));
				uploaddocumentManagementDTO.setVersionNo(rs.getInt("sud_versionno"));
				uploaddocumentManagementDTO.setDocumentName(uploaddocumentManagementDTO.getAdd_Doc_Description() + "_"
						+ uploaddocumentManagementDTO.getVersionNo());

				searchList.add(uploaddocumentManagementDTO);
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

	public List<DocumentManagementDTO> gamiSariyaSurveyMandatoryList(String requestNo, String refNo, String serviceNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String transactionTypeCode = null;
		try {
			transactionTypeCode = PropertyReader.getPropertyValue("queue.transaction.type.code.gamisariya");
		} catch (ApplicationException e) {

			e.printStackTrace();
		}

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT sud_doc_code,sud_document_des,sud_file_path,sud_versionno FROM public.nt_t_subsidy_document_version \r\n"
					+ "where sud_request_no='" + requestNo + "' and sud_ref_no='" + refNo + "'and sud_service_no='"
					+ serviceNo + "' and sud_transaction_type='" + transactionTypeCode + "' and sud_doc_type='M' \r\n"
					+ "order by sud_document_des, sud_versionno ;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();

				uploaddocumentManagementDTO.setDoc_Code(rs.getString("sud_doc_code"));
				uploaddocumentManagementDTO.setAdd_Doc_Description(rs.getString("sud_document_des"));
				uploaddocumentManagementDTO.setUploadFilePath(rs.getString("sud_file_path"));
				uploaddocumentManagementDTO.setVersionNo(rs.getInt("sud_versionno"));
				uploaddocumentManagementDTO.setDocumentName(uploaddocumentManagementDTO.getAdd_Doc_Description() + "_"
						+ uploaddocumentManagementDTO.getVersionNo());

				searchList.add(uploaddocumentManagementDTO);
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

	public List<DocumentManagementDTO> gamiSariyaSurveyOptionalList(String requestNo, String refNo, String serviceNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String transactionTypeCode = null;
		try {
			transactionTypeCode = PropertyReader.getPropertyValue("queue.transaction.type.code.gamisariya");
		} catch (ApplicationException e) {

			e.printStackTrace();
		}

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT sud_doc_code,sud_document_des,sud_file_path,sud_versionno FROM public.nt_t_subsidy_document_version \r\n"
					+ "where sud_request_no='" + requestNo + "'and sud_ref_no = '" + refNo + "'and sud_service_no='"
					+ serviceNo + "' and sud_transaction_type='" + transactionTypeCode + "' and sud_doc_type='O' \r\n"
					+ "order by sud_document_des, sud_versionno ;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();

				uploaddocumentManagementDTO.setDoc_Code(rs.getString("sud_doc_code"));
				uploaddocumentManagementDTO.setAdd_Doc_Description(rs.getString("sud_document_des"));
				uploaddocumentManagementDTO.setUploadOptionalFilePath(rs.getString("sud_file_path"));
				uploaddocumentManagementDTO.setVersionNo(rs.getInt("sud_versionno"));
				uploaddocumentManagementDTO.setDocumentName(uploaddocumentManagementDTO.getAdd_Doc_Description() + "_"
						+ uploaddocumentManagementDTO.getVersionNo());

				searchList.add(uploaddocumentManagementDTO);
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
	public List<RevenueCollectionDTO> GetAllDocumentTrasactionTypes() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<RevenueCollectionDTO> transactionList = new ArrayList<RevenueCollectionDTO>();

		RevenueCollectionDTO revenueCollectionDTO;

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT * FROM public.nt_r_transaction_type where active ='Y'  order by description ";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {

				revenueCollectionDTO = new RevenueCollectionDTO();

				revenueCollectionDTO.setTypeCode(rs.getString("code"));
				revenueCollectionDTO.setTypeDes(rs.getString("description"));

				transactionList.add(revenueCollectionDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return transactionList;
	}

	@Override
	public List<String> getPermitNumbersForView() {

		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		PreparedStatement stmt3 = null;
		PreparedStatement stmt4 = null;
		PreparedStatement stmt5= null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		ResultSet rs4 = null;
		ResultSet rs5 = null;
		List<String> amendmendPermitList = new ArrayList<String>();
		List<String> newPermitList = new ArrayList<String>();
		List<String> backlogPermits = new ArrayList<String>();
		List<String>permitList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();
			//get permit numbers from amendments tables
			String sql = "select distinct amd_permit_no FROM public.nt_m_amendments where amd_permit_no is not null order by amd_permit_no";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {

				amendmendPermitList.add(rs.getString("amd_permit_no"));
			}
			// get permit numbers from new permits and renewal
			String sql2 = "SELECT distinct pm_permit_no FROM public.nt_t_pm_application\r\n"
					+ "WHERE  pm_application_no LIKE 'PAP%'  and pm_permit_no is not null order by pm_permit_no";

			stmt2 = con.prepareStatement(sql2);
			rs2 = stmt2.executeQuery();
			while (rs2.next()) {

				newPermitList.add(rs2.getString("pm_permit_no"));
			}
			// get permit numbers from backlog
						String sql3 = "SELECT distinct pm_permit_no FROM public.nt_t_pm_application\r\n"
								+ "WHERE  pm_application_no LIKE 'BAP%'  and pm_permit_no is not null order by pm_permit_no";

						stmt3 = con.prepareStatement(sql3);
						rs3 = stmt3.executeQuery();
						while (rs3.next()) {

							backlogPermits.add(rs3.getString("pm_permit_no"));
						}
						
		
			
			
			permitList.addAll(amendmendPermitList);
			permitList.addAll(newPermitList);
			permitList.addAll(backlogPermits);
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(rs2);
			ConnectionManager.close(rs3);
			ConnectionManager.close(rs4);
			ConnectionManager.close(rs5);
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(stmt3);
			ConnectionManager.close(stmt4);
			ConnectionManager.close(stmt5);
			ConnectionManager.close(con);
		}
		return permitList;

	
	}

	@Override
	public List<DocumentManagementDTO> getTransactionsByPermitNumber(String permitNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PreparedStatement stmt2 = null;
		ResultSet rs2 = null;
		List<DocumentManagementDTO> transactionList = new ArrayList<DocumentManagementDTO>();

		DocumentManagementDTO dto = new DocumentManagementDTO();

		try {
			con = ConnectionManager.getConnection();

			String sql = "select distinct a.apd_transaction_type ,t.description from public.nt_t_application_document a\r\n" + 
					"inner join public.nt_r_transaction_type t on t.code =a.apd_transaction_type inner join public.nt_t_pm_application ap on ap.pm_application_no =a.apd_application_no \r\n" + 
					"where a.apd_permit_no =? and t.active ='Y' and t.code !='02';";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, permitNo);
			rs = stmt.executeQuery();

			while (rs.next()) {

				dto = new DocumentManagementDTO();

				dto.setTransaction_Type_Code(rs.getString("apd_transaction_type"));
				dto.setTransaction_Type(rs.getString("description"));

				transactionList.add(dto);

			}
			
			/*String sql2 = "select distinct a.amd_trn_type,t.description from public.nt_m_amendments  a\r\n" + 
					"inner join public.nt_r_transaction_type t on t.code =a.amd_trn_type \r\n" + 
					"where amd_permit_no =? and t.active ='Y';;";

			stmt2 = con.prepareStatement(sql2);
			stmt2.setString(1, permitNo);
			rs2= stmt2.executeQuery();

			while (rs2.next()) {

				dto = new DocumentManagementDTO();

				dto.setTransaction_Type_Code(rs2.getString("amd_trn_type"));
				dto.setTransaction_Type(rs2.getString("description"));

				transactionList.add(dto);

			}*/

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(rs2);
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(con);
		}
		return transactionList;
	}

	@Override
	public List<String> GetApplicationNoListByPermitAndTransType(String transactionType, String permitNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<String> applicationList = new ArrayList<String>();
		String sql = null;

		try {
			con = ConnectionManager.getConnection();
			
		/*	if (transactionType.equals("05") || transactionType.equals("13") || transactionType.equals("14")
					|| transactionType.equals("15") || transactionType.equals("16") || transactionType.equals("22")
					|| transactionType.equals("21") || transactionType.equals("23")) {
				
				 sql = "select distinct  amd_application_no from public.nt_m_amendments   where amd_permit_no =? and amd_trn_type =? and amd_application_no is not null order by amd_application_no desc";
			}
			else {
			 sql = "select distinct  pm_application_no from public.nt_t_pm_application  where pm_permit_no =? and pm_tran_type =? and pm_application_no is not null order by pm_application_no desc";
			}*/
			
			 sql = "select distinct  a.apd_application_no from public.nt_t_application_document  a\r\n" + 
			 		"inner join public.nt_t_pm_application ap on ap.pm_application_no =a.apd_application_no \r\n" + 
			 		"where a.apd_permit_no =? and a.apd_transaction_type =? \r\n" + 
			 		"and a.apd_application_no is not null order by a.apd_application_no desc";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, permitNo);
			stmt.setString(2, transactionType);
			rs = stmt.executeQuery();
			while (rs.next()) {

				applicationList.add(rs.getString(1));
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return applicationList;

	}

	@Override
	public String getTranactionDescription(String code) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String output = null;
		try {

			con = ConnectionManager.getConnection();

			String sql = "select description from nt_r_transaction_type where code =?";

			ps = con.prepareStatement(sql);
			ps.setString(1, code);
			rs = ps.executeQuery();

			while (rs.next()) {

				output = rs.getString("description");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return output;
	}

	@Override
	public List<DocumentManagementDTO> optionalDocsForAmendmentsWithOutAppNo(String typeCode, String uploadPermitNo) {
		


		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String filepathOp;

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {

			con = ConnectionManager.getConnection();

			String sql = "select d.apd_document_des, d.apd_doc_code, d.apd_transaction_type, d.apd_file_path\r\n" + 
					"from (select A.apd_document_des,A.apd_doc_code,A.apd_transaction_type,apd_file_path as apd_file_path\r\n" + 
					"from public.nt_t_application_document A where A.apd_permit_no = '" + uploadPermitNo +"'\r\n" + 
					"and A.apd_doc_type = 'O' and A.apd_transaction_type = '" + typeCode + "'\r\n" + 
					"union\r\n" + 
					"select B.doc_document_des,B.doc_code,B.doc_transaction_type,'X' as apd_file_path\r\n" + 
					"from public.nt_m_document B\r\n" + 
					"WHERE not EXISTS( select * from public.nt_t_application_document A \r\n" + 
					"where A.apd_doc_code = B.doc_code and A.apd_permit_no = '"+uploadPermitNo + "'\r\n" + 
					"and A.apd_doc_type = 'O'\r\n" + 
					"and A.apd_transaction_type = '" + typeCode + "' )	\r\n" + 
					"AND B.doc_mandatory_doc = 'false' and B.doc_transaction_type = '" + typeCode+ "') as d order by  d.apd_doc_code ;";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {

				DocumentManagementDTO uploadDocument = new DocumentManagementDTO();
				uploadDocument.setDoc_Code(rs.getString("apd_doc_code"));
				uploadDocument.setAdd_Doc_Description(rs.getString("apd_document_des"));

				filepathOp = rs.getString("apd_file_path");

				if (filepathOp.equals("X")) {
					uploadDocument.setUploadOptionalFilePath(null);
					uploadDocument.setUploadFilePath(null);
				} else {
					uploadDocument.setUploadFilePath(filepathOp);
					uploadDocument.setUploadOptionalFilePath(rs.getString("apd_file_path"));
				}
				uploadDocument.setTransaction_Type(rs.getString("apd_transaction_type"));

				searchList.add(uploadDocument);

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
	public List<DocumentManagementDTO> mandatoryDocsForAmendmentsWithOutAppNo(String typeCode, String uploadPermitNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String filepath;

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();

			String sql = "select d.apd_document_des, d.apd_doc_code, d.apd_transaction_type, d.apd_file_path\r\n" + 
					"from (select A.apd_document_des,A.apd_doc_code,A.apd_transaction_type,apd_file_path as apd_file_path\r\n" + 
					"from public.nt_t_application_document A where A.apd_permit_no = '"+uploadPermitNo+"'\r\n" + 
					"and A.apd_doc_type = 'M'and A.apd_transaction_type = '"+typeCode+"'\r\n" + 
					"union\r\n" + 
					"select B.doc_document_des,B.doc_code,B.doc_transaction_type,'X' as apd_file_path\r\n" + 
					"from public.nt_m_document B\r\n" + 
					"WHERE not EXISTS( select * from public.nt_t_application_document A \r\n" + 
					"where A.apd_doc_code = B.doc_code and A.apd_permit_no = '"+uploadPermitNo+"'	\r\n" + 
					"and A.apd_doc_type = 'M'\r\n" + 
					"and A.apd_transaction_type = '"+typeCode+"' )	\r\n" + 
					"AND B.doc_mandatory_doc = 'true' and B.doc_transaction_type = '"+typeCode+"') as d order by d.apd_doc_code";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploadDocument = new DocumentManagementDTO();

				uploadDocument.setDoc_Code(rs.getString("apd_doc_code"));
				uploadDocument.setAdd_Doc_Description(rs.getString("apd_document_des"));

				filepath = rs.getString("apd_file_path");

				if (filepath.equals("X")) {
					uploadDocument.setUploadFilePath(null);

				} else {

					uploadDocument.setUploadFilePath(rs.getString("apd_file_path"));
				}
				uploadDocument.setTransaction_Type(rs.getString("apd_transaction_type"));

				searchList.add(uploadDocument);

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
	public List<DocumentManagementDTO> mandatoryViewDocsByAppNo(String typeCode, String uploadPermitNo, String appNo) {


		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT apd_transaction_type, apd_permit_no, apd_doc_code, apd_document_des, apd_file_path, apd_doc_type\r\n"
					+ "FROM public.nt_t_application_document\r\n" + "where apd_transaction_type='" + typeCode
					+ "' and apd_permit_no='" + uploadPermitNo + "' and apd_application_no='" + appNo + "' and apd_doc_type='M' order by  apd_application_no desc; ";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploadDocument = new DocumentManagementDTO();

				uploadDocument.setDoc_Code(rs.getString("apd_doc_code"));
				uploadDocument.setAdd_Doc_Description(rs.getString("apd_document_des"));
				uploadDocument.setUploadFilePath(rs.getString("apd_file_path"));
				uploadDocument.setTransaction_Type(rs.getString("apd_transaction_type"));

				searchList.add(uploadDocument);

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
	public List<DocumentManagementDTO> optionalViewDocsByAppNo(String typeCode, String uploadPermitNo, String appNo) {


		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<DocumentManagementDTO> searchList = new ArrayList<DocumentManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT apd_transaction_type, apd_permit_no, apd_doc_code, apd_document_des, apd_file_path, apd_doc_type\r\n"
					+ "FROM public.nt_t_application_document\r\n" + "where apd_transaction_type='" + typeCode
					+ "' and apd_permit_no='" + uploadPermitNo + "'  and apd_application_no='" + appNo + "' and apd_doc_type='O' order by  apd_application_no desc; ";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				DocumentManagementDTO uploadDocument = new DocumentManagementDTO();

				uploadDocument.setDoc_Code(rs.getString("apd_doc_code"));
				uploadDocument.setAdd_Doc_Description(rs.getString("apd_document_des"));
				uploadDocument.setUploadOptionalFilePath(rs.getString("apd_file_path"));
				uploadDocument.setUploadFilePath(rs.getString("apd_file_path"));
				uploadDocument.setTransaction_Type(rs.getString("apd_transaction_type"));

				searchList.add(uploadDocument);

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
	
	private void insertTaskInquiryRecord(Connection con, DocumentManagementDTO  registrationDto,
			Timestamp timestamp, String status, String function,String user, String functiondes) {
		
		try {
			String insertSQL = null;
			PreparedStatement psInsert = null;

			insertSQL = "INSERT INTO public.nt_t_task_inquiry (created_by, created_date, status,dc_id_num,function_name,function_des,nic_no) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?)";

			psInsert = con.prepareStatement(insertSQL);

			psInsert.setString(1, user);
			psInsert.setTimestamp(2, timestamp);
			psInsert.setString(3, status);
			psInsert.setString(4, registrationDto.getUpload_Permit());
			psInsert.setString(5, function);
			psInsert.setString(6, functiondes);
			psInsert.setString(7, registrationDto.getNicNo());

			psInsert.executeUpdate();
			con.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public void beanLinkMethod(DocumentManagementDTO  registrationDto,String user,String des,String funDes) {
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
	
}
