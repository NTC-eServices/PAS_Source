package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.informatics.ntc.model.dto.SetupCommitteeBoardDTO;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.roc.utils.common.Utils;

public class SetupCommitteeBoardServiceImpl implements SetupCommitteeBoardService {

	@Override
	public List<SetupCommitteeBoardDTO> getTransactionTypeList() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SetupCommitteeBoardDTO> data = new ArrayList<SetupCommitteeBoardDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT code, description FROM public.nt_r_transaction_type WHERE active = 'Y'  and code in ('05','14','15','16','19','08','01','21','22','23') order by description;";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			SetupCommitteeBoardDTO p;

			while (rs.next()) {
				p = new SetupCommitteeBoardDTO();
				p.setTransactionTypeCode(rs.getString("code"));
				p.setTransactionTypeDes(rs.getString("description"));
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
	public List<SetupCommitteeBoardDTO> getOrganizationListList() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SetupCommitteeBoardDTO> data = new ArrayList<SetupCommitteeBoardDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT code, description FROM public.nt_r_organization WHERE active = 'A' order by description;";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			SetupCommitteeBoardDTO p;

			while (rs.next()) {
				p = new SetupCommitteeBoardDTO();
				p.setOrganizationCode(rs.getString("code"));
				p.setOrganizationDes(rs.getString("description"));
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
	public List<SetupCommitteeBoardDTO> getUserIDList() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SetupCommitteeBoardDTO> data = new ArrayList<SetupCommitteeBoardDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT a.emp_user_id as emp_user_id, a.emp_fullname as emp_fullname, b.des_desig_code as des_desig_code, c.description as designation "
					+ "from (( public.nt_m_employee a "
					+ "inner join nt_m_emp_designation b on b.des_emp_no = a.emp_no) "
					+ "inner join nt_r_designation c on c.code = b.des_desig_code) " + "WHERE a.emp_user_status = 'A' "
					+ "order by a.emp_user_id";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			SetupCommitteeBoardDTO p;

			while (rs.next()) {
				p = new SetupCommitteeBoardDTO();
				p.setUserID(rs.getString("emp_user_id"));
				p.setName(rs.getString("emp_fullname"));
				p.setDesignationCode(rs.getString("des_desig_code"));
				p.setDesignationDes(rs.getString("designation"));
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
	public List<SetupCommitteeBoardDTO> getDesignationList() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SetupCommitteeBoardDTO> data = new ArrayList<SetupCommitteeBoardDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT code, description FROM public.nt_r_designation WHERE active = 'A';";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			SetupCommitteeBoardDTO p;

			while (rs.next()) {
				p = new SetupCommitteeBoardDTO();
				p.setDesignationCode(rs.getString("code"));
				p.setDesignationDes(rs.getString("description"));
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
	public void saveMasterData(SetupCommitteeBoardDTO setupCommitteeBoardDTO, String referenceNo, String loginUser) {

		Connection con = null;
		PreparedStatement ps = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String activeFromDate = df.format(setupCommitteeBoardDTO.getActiveFromDate());
		String activeToDate = df.format(setupCommitteeBoardDTO.getActiveToDate());

		try {

			con = ConnectionManager.getConnection();
			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_committee");
			String query = "INSERT INTO public.nt_m_committee (seqno, com_type, com_refno, com_active_from, com_active_to, "
					+ "com_status, com_created_by, com_created_date,com_transactiontype_code) "
					+ "VALUES (?,?,?,?,?,?,?,?,?);";

			ps = con.prepareStatement(query);
			ps.setLong(1, seqNo);
			ps.setString(2, setupCommitteeBoardDTO.getType());
			ps.setString(3, referenceNo);
			ps.setString(4, activeFromDate);
			ps.setString(5, activeToDate);
			ps.setString(6, setupCommitteeBoardDTO.getStatus());
			ps.setString(7, loginUser);
			ps.setTimestamp(8, timestamp);
			ps.setString(9, setupCommitteeBoardDTO.getTransactionTypeCode());
			ps.executeUpdate();
			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
	}

	@Override
	public void saveMemberDetails(List<SetupCommitteeBoardDTO> membersList, String referenceNo, String loginUser) {

		Connection con = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		PreparedStatement ps = null;
		try {

			con = ConnectionManager.getConnection();

			for (int i = 0; i < membersList.size(); i++) {

				long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_committee_det");
				String query = "INSERT INTO public.nt_t_committee_det (seqno, cod_committee_refno, cod_organization_code, cod_userid, cod_responsibility, "
						+ "cod_designation_code, cod_full_name, cod_status, cod_created_by,cod_created_date) "
						+ "VALUES (?,?,?,?,?,?,?,?,?,?);";

				ps = con.prepareStatement(query);
				ps.setLong(1, seqNo);
				ps.setString(2, referenceNo);
				ps.setString(3, membersList.get(i).getOrganizationCode());
				ps.setString(4, membersList.get(i).getUserID());
				ps.setString(5, membersList.get(i).getResponsibilities());
				ps.setString(6, membersList.get(i).getDesignationCode());
				ps.setString(7, membersList.get(i).getName());
				ps.setString(8, membersList.get(i).getStatus());
				ps.setString(9, loginUser);
				ps.setTimestamp(10, timestamp);
				ps.executeUpdate();
				con.commit();

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
	}

	@Override
	public String generateReferenceNo() {
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

			String sql = "SELECT com_refno FROM public.nt_m_committee ORDER BY com_created_date desc  LIMIT 1";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("com_refno");
			}

			if (result != null) {

				lastyr = result.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String ApprecordcountN = "00001";
					strRecNo = "REF" + currYear + ApprecordcountN;
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
					strRecNo = "REF" + currYear + ApprecordcountN;
				}
			} else {
				strRecNo = "REF" + currYear + "00001";
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
	public int updateMasterData(SetupCommitteeBoardDTO setupCommitteeBoardDTO, String referenceNo, String loginUser) {
		int rs = 0;
		Connection con = null;
		PreparedStatement ps = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		// DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String activeFromDate = df.format(setupCommitteeBoardDTO.getActiveFromDate());
		String activeToDate = df.format(setupCommitteeBoardDTO.getActiveToDate());
		try {

			con = ConnectionManager.getConnection();
			String query = "UPDATE public.nt_m_committee "
					+ "SET com_active_from = ?, com_active_to = ?,com_modified_by = ?, com_modified_date = ?, com_status = ? "
					+ "WHERE com_refno=?; ";

			ps = con.prepareStatement(query);
			ps.setString(1, activeFromDate);
			ps.setString(2, activeToDate);
			ps.setString(3, loginUser);
			ps.setTimestamp(4, timestamp);
			ps.setString(5, setupCommitteeBoardDTO.getStatus());
			ps.setString(6, referenceNo);
			rs = ps.executeUpdate();
			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return rs;
	}

	@Override
	public void authorizedData(String referenceNo, String loginUser) {

		Connection con = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String authorizedDate = df.format(timestamp);
		PreparedStatement ps = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_m_committee "
					+ "SET com_authorizedby = ?, com_authorizeddate = ?, com_isauthorized='Y' "
					+ "WHERE com_refno=? and (com_isauthorized !='Y' or com_isauthorized is null);";

			ps = con.prepareStatement(query);
			ps.setString(1, loginUser);
			ps.setString(2, authorizedDate);
			ps.setString(3, referenceNo);
			int rs = ps.executeUpdate();
			con.commit();
			if (rs < 1) {

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
	}

	@Override
	public List<SetupCommitteeBoardDTO> getReferenceNoListforAuth() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SetupCommitteeBoardDTO> data = new ArrayList<SetupCommitteeBoardDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT com_refno FROM public.nt_m_committee WHERE com_isauthorized !='Y' or com_isauthorized is null order by com_refno;";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			SetupCommitteeBoardDTO p;

			while (rs.next()) {
				p = new SetupCommitteeBoardDTO();
				p.setReferenceNo(rs.getString("com_refno"));
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
	public SetupCommitteeBoardDTO getMasterDataFromReferenceNo(String referenceNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		SetupCommitteeBoardDTO data = new SetupCommitteeBoardDTO();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT com_type, com_active_from, com_active_to, com_status,com_transactiontype_code, com_isauthorized "
					+ "FROM public.nt_m_committee " + "WHERE com_refno = ?;";
			ps = con.prepareStatement(query);
			ps.setString(1, referenceNo);
			rs = ps.executeQuery();

			while (rs.next()) {

				data.setType(rs.getString("com_type"));
				data.setTransactionTypeCode(rs.getString("com_transactiontype_code"));
				data.setStatus(rs.getString("com_status"));
				data.setActiveFromDateStr(rs.getString("com_active_from"));
				data.setActiveToDateStr(rs.getString("com_active_to"));
				try {
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date parsedDate = dateFormat.parse(rs.getString("com_active_from"));
					Timestamp timestampActiveFrom = new java.sql.Timestamp(parsedDate.getTime());

					Date parsedDate2 = dateFormat.parse(rs.getString("com_active_to"));
					Timestamp timestampActiveTo = new java.sql.Timestamp(parsedDate2.getTime());

					data.setActiveFromDate(timestampActiveFrom);
					data.setActiveToDate(timestampActiveTo);

				} catch (Exception e) {
					e.printStackTrace();
				}

				if (rs.getString("com_isauthorized") != null && !rs.getString("com_isauthorized").equals("")) {
					data.setIsauthorized(rs.getString("com_isauthorized"));
				}
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
	public List<SetupCommitteeBoardDTO> getMemberDataFromReferenceNo(String referenceNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SetupCommitteeBoardDTO> data = new ArrayList<SetupCommitteeBoardDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT a.cod_organization_code as cod_organization_code, a.cod_userid as cod_userid, a.cod_responsibility as cod_responsibility, "
					+ "a.cod_designation_code as cod_designation_code,d.description as cod_designation_des, c.description as cod_organization_des,  a.cod_full_name as cod_full_name "
					+ "FROM ((public.nt_t_committee_det a "
					+ "inner join nt_r_organization c on c.code = a.cod_organization_code) "
					+ "inner join nt_r_designation d on d.code = a.cod_designation_code) "
					+ "WHERE cod_committee_refno=?;";
			ps = con.prepareStatement(query);
			ps.setString(1, referenceNo);
			rs = ps.executeQuery();

			SetupCommitteeBoardDTO p;

			while (rs.next()) {
				p = new SetupCommitteeBoardDTO();
				p.setOrganizationCode(rs.getString("cod_organization_code"));
				p.setDesignationCode(rs.getString("cod_designation_code"));
				p.setOrganizationDes(rs.getString("cod_organization_des"));
				p.setDesignationDes(rs.getString("cod_designation_des"));
				p.setUserID(rs.getString("cod_userid"));
				p.setName(rs.getString("cod_full_name"));
				p.setResponsibilities(rs.getString("cod_responsibility"));
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
	public List<SetupCommitteeBoardDTO> getReferenceNoListforEdit() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SetupCommitteeBoardDTO> data = new ArrayList<SetupCommitteeBoardDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT com_refno FROM public.nt_m_committee WHERE com_isauthorized ='Y' or com_isauthorized is null or com_isauthorized !='Y' order by com_refno;";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			SetupCommitteeBoardDTO p;

			while (rs.next()) {
				p = new SetupCommitteeBoardDTO();
				p.setReferenceNo(rs.getString("com_refno"));

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
	public List<SetupCommitteeBoardDTO> getReferenceNoListforView() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SetupCommitteeBoardDTO> data = new ArrayList<SetupCommitteeBoardDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT com_refno FROM public.nt_m_committee order by com_refno;";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			SetupCommitteeBoardDTO p;

			while (rs.next()) {
				p = new SetupCommitteeBoardDTO();
				p.setReferenceNo(rs.getString("com_refno"));
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

	public int getActiveStatus(String type, String transCode) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;
		int found = 0;

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT com_refno FROM public.nt_m_committee where com_type='" + type
					+ "' and com_status='A' and com_transactiontype_code='" + transCode + "'";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("com_refno");
			}

			if (result != null) {
				found = 1;
			} else {
				found = -1;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return found;
	}

	public int getRefNo(String type, String transCode) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;
		int found = 0;

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT com_refno FROM public.nt_m_committee where com_type='" + type
					+ "' and com_status='A' and com_transactiontype_code='" + transCode + "'";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("com_refno");
			}

			if (result != null) {
				found = 1;
			} else {
				found = -1;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return found;
	}

	public String getAuthorizedStatus(String refNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT com_isauthorized FROM public.nt_m_committee where com_refno='" + refNo + "'";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("com_isauthorized");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return result;
	}
}
