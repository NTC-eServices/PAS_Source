package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.informatics.ntc.model.dto.AssignBranchesDTO;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.roc.utils.common.Utils;

public class AssignBranchesServiceImpl implements AssignBranchesService {

	@Override
	public List<AssignBranchesDTO> getBankCode() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<AssignBranchesDTO> data = new ArrayList<AssignBranchesDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT code, description FROM public.nt_r_bank WHERE active = 'A' order by code";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			AssignBranchesDTO p;

			while (rs.next()) {
				p = new AssignBranchesDTO();
				p.setBankCode(rs.getString("code"));
				p.setBankDes(rs.getString("description"));
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
	public String getBranchName(String bankCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String bankDes = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT description FROM public.nt_r_bank WHERE code = ?";

			ps = con.prepareStatement(query);
			ps.setString(1, bankCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				bankDes = rs.getString("description");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return bankDes;
	}

	@Override
	public List<AssignBranchesDTO> getAssignedBranchesList(String bankCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<AssignBranchesDTO> data = new ArrayList<AssignBranchesDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT code, description, description_si, description_ta FROM public.nt_r_branch WHERE bank_code=?";

			ps = con.prepareStatement(query);
			ps.setString(1, bankCode);
			rs = ps.executeQuery();

			AssignBranchesDTO p;

			while (rs.next()) {
				p = new AssignBranchesDTO();
				p.setBranchCode(rs.getString("code"));
				p.setBranch_description_english(rs.getString("description"));
				p.setBranch_description_sinhala(rs.getString("description_si"));
				p.setBranch_description_tamil(rs.getString("description_ta"));
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
	public boolean isCodeDuplicate(String bankCode, String branchCode) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isCodeDuplicate = false;
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT code FROM public.nt_r_branch WHERE code=?";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, branchCode);
			rs = stmt.executeQuery();
			while (rs.next()) {
				isCodeDuplicate = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return isCodeDuplicate;
	}

	@Override
	public void saveRecord(AssignBranchesDTO assignBranchesDTO, String username) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PreparedStatement stmt2 =  null;
		
		try {
			con = ConnectionManager.getConnection();
			
			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_r_bank_branch");
			String sql = "INSERT INTO public.nt_r_branch "
					+ "(code, description, created_by, created_date, description_si, description_ta, bank_code,active) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?,?)";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, assignBranchesDTO.getBranchCode());
			stmt.setString(2, assignBranchesDTO.getBranch_description_english());
			stmt.setString(3, username);
			stmt.setTimestamp(4, new Timestamp(new Date().getTime()));
			stmt.setString(5, assignBranchesDTO.getBranch_description_sinhala());
			stmt.setString(6, assignBranchesDTO.getBranch_description_tamil());
			stmt.setString(7, assignBranchesDTO.getBankCode());
			stmt.setString(8, "A");


			stmt.executeUpdate();
			
			
			
			String sql2 = "INSERT INTO public.nt_r_bank_branch\r\n" + 
					"(seq, bank_code, branch_code, active, created_by, created_date, modified_by, modified_date, branch_name)\r\n" + 
					"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
			stmt2 = con.prepareStatement(sql2);
			stmt2.setLong(1, seqNo);
			stmt2.setString(2, assignBranchesDTO.getBankCode());
			stmt2.setString(3, assignBranchesDTO.getBranchCode());
			stmt2.setString(4, "A");
			stmt2.setString(5, username);
			stmt2.setTimestamp(6, new Timestamp(new Date().getTime()));
			stmt2.setString(7, username);
			stmt2.setTimestamp(8, new Timestamp(new Date().getTime()));
			stmt2.setString(9, assignBranchesDTO.getBranch_description_english());
		

			stmt2.executeUpdate();
			
			
			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(con);
		}
	}

	@Override
	public int deleteRecord(AssignBranchesDTO deleteAssignBranchesDTO, String bankCodeStr) {
		Connection con = null;
		PreparedStatement stmt = null;
		String sql = null;
		PreparedStatement stmt2 = null;
		String sql2 = null;
		int result = -1;
		try {
			con = ConnectionManager.getConnection();
			sql = "DELETE FROM public.nt_r_branch WHERE bank_code=? and code=?";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, bankCodeStr);
			stmt.setString(2, deleteAssignBranchesDTO.getBranchCode());
			stmt.executeUpdate();
			
			sql2 = "DELETE FROM public.nt_r_bank_branch WHERE bank_code=? and branch_code=?";
			stmt2 = con.prepareStatement(sql2);
			stmt2.setString(1, bankCodeStr);
			stmt2.setString(2, deleteAssignBranchesDTO.getBranchCode());
			stmt2.executeUpdate();
			
			con.commit();
			result = 1;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(con);
		}
		return result;
	}

	@Override
	public void editRecord(AssignBranchesDTO assignBranchesDTO, String username) {
		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			String sql = "UPDATE public.nt_r_branch "
					+ "SET description=?, modified_by=?, modified_date=?, description_si=?, description_ta=? "
					+ "WHERE code=? and bank_code =?";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, assignBranchesDTO.getBranch_description_english());
			stmt.setString(2, username);
			stmt.setTimestamp(3, new Timestamp(new Date().getTime()));
			stmt.setString(4, assignBranchesDTO.getBranch_description_sinhala());
			stmt.setString(5, assignBranchesDTO.getBranch_description_tamil());
			stmt.setString(6, assignBranchesDTO.getBranchCode());
			stmt.setString(7, assignBranchesDTO.getBankCode());

			stmt.executeUpdate();
			
			
			String sql2 = "UPDATE public.nt_r_bank_branch\r\n" + 
					"SET  modified_by=?, modified_date=?, branch_name=? " + 
					"WHERE branch_code=? and bank_code=?;";

			stmt2 = con.prepareStatement(sql2);
			stmt2.setString(1, username);
			stmt2.setTimestamp(2, new Timestamp(new Date().getTime()));
			stmt2.setString(3, assignBranchesDTO.getBranch_description_english());
			stmt2.setString(4, assignBranchesDTO.getBranchCode());
			stmt2.setString(5, assignBranchesDTO.getBankCode());

			stmt2.executeUpdate();
			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(con);
		}
	}

}
