package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.informatics.ntc.model.dto.AssignDivSecDTO;
import lk.informatics.ntc.view.util.ConnectionManager;

public class AssignDivSecServiceImpl implements AssignDivSecService {

	@Override
	public List<AssignDivSecDTO> getDistrictCode() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<AssignDivSecDTO> data = new ArrayList<AssignDivSecDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT code, description FROM public.nt_r_district WHERE active = 'A' order by code";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			AssignDivSecDTO p;

			while (rs.next()) {
				p = new AssignDivSecDTO();
				p.setDistrictCode(rs.getString("code"));
				p.setDistrictDes(rs.getString("description"));
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
	public String getDivSecName(String districtCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String districtDes = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT description FROM public.nt_r_district WHERE code = ?";

			ps = con.prepareStatement(query);
			ps.setString(1, districtCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				districtDes = rs.getString("description");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return districtDes;
	}

	@Override
	public List<AssignDivSecDTO> getAssignedDivSecList(String districtCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<AssignDivSecDTO> data = new ArrayList<AssignDivSecDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT ds_code, ds_description, ds_description_sinhala, ds_description_tamil, ds_district_code FROM public.nt_r_div_sec WHERE ds_district_code=?";

			ps = con.prepareStatement(query);
			ps.setString(1, districtCode);
			rs = ps.executeQuery();

			AssignDivSecDTO p;

			while (rs.next()) {
				p = new AssignDivSecDTO();
				p.setDivSecCode(rs.getString("ds_code"));
				p.setDivSec_description_english(rs.getString("ds_description"));
				p.setDivSec_description_sinhala(rs.getString("ds_description_sinhala"));
				p.setDivSec_description_tamil(rs.getString("ds_description_tamil"));
				p.setDistrictCode(rs.getString("ds_district_code"));
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
	public boolean isCodeDuplicate(String districtCode, String divSecCode) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isCodeDuplicate = false;
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT ds_code FROM public.nt_r_div_sec WHERE ds_code=?";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, districtCode + "-" + divSecCode);
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
	public void saveRecord(AssignDivSecDTO assignDivSecDTO, String username) {
		
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			String sql = "INSERT INTO public.nt_r_div_sec "
					+ "(ds_code, ds_description, ds_active, ds_created_by, ds_created_date, ds_description_sinhala, ds_description_tamil, ds_district_code) "
					+ "VALUES(?, ?,'A', ?, ?, ?, ?, ?)";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, assignDivSecDTO.getDivSecCode());
			stmt.setString(2, assignDivSecDTO.getDivSec_description_english());
			stmt.setString(3, username);
			stmt.setTimestamp(4, new Timestamp(new Date().getTime()));
			stmt.setString(5, assignDivSecDTO.getDivSec_description_sinhala());
			stmt.setString(6, assignDivSecDTO.getDivSec_description_tamil());
			stmt.setString(7, assignDivSecDTO.getDistrictCode());

			stmt.executeUpdate();
			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
	}

	@Override
	public int deleteRecord(AssignDivSecDTO deleteAssignDivSecDTO, String districtCodeStr) {
		
		Connection con = null;
		PreparedStatement stmt = null;
		String sql = null;
		int result = -1;
		try {
			con = ConnectionManager.getConnection();
			sql = "DELETE FROM public.nt_r_div_sec WHERE ds_district_code=? and ds_code=?";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, districtCodeStr);
			stmt.setString(2, deleteAssignDivSecDTO.getDivSecCode());
			stmt.executeUpdate();
			
			con.commit();
			result = 1;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return result;
	}

	@Override
	public void editRecord(AssignDivSecDTO assignDivSecDTO, String username) {
		
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			String sql = "UPDATE public.nt_r_div_sec "
					+ "SET ds_description=?, ds_modified_by=?, ds_modified_date=?, ds_description_sinhala=?, ds_description_tamil=? "
					+ "WHERE ds_code=? and ds_district_code =?";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, assignDivSecDTO.getDivSec_description_english());
			stmt.setString(2, username);
			stmt.setTimestamp(3, new Timestamp(new Date().getTime()));
			stmt.setString(4, assignDivSecDTO.getDivSec_description_sinhala());
			stmt.setString(5, assignDivSecDTO.getDivSec_description_tamil());
			stmt.setString(6, assignDivSecDTO.getDivSecCode());
			stmt.setString(7, assignDivSecDTO.getDistrictCode());

			stmt.executeUpdate();
			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
	}

}
