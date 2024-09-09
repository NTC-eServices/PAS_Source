package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import lk.informatics.ntc.model.dto.SubSidyDTO;
import lk.informatics.ntc.view.util.ConnectionManager;

public class SubsidyReportsServiceImpl implements SubsidyReportsService {

	private static final long serialVersionUID = 1L;

	@Override
	public List<SubSidyDTO> getprovinceList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SubSidyDTO> provinceList = new ArrayList<SubSidyDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT * FROM public.nt_r_province ";

			ps = con.prepareStatement(query2);

			rs = ps.executeQuery();
			SubSidyDTO e;

			while (rs.next()) {
				e = new SubSidyDTO();
				e.setProvinceCode(rs.getString("code"));
				e.setProvinceDes(rs.getString("description"));

				provinceList.add(e);

			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return provinceList;
	}

	@Override
	public List<SubSidyDTO> getDistrictList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SubSidyDTO> districtList = new ArrayList<SubSidyDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT * FROM public.nt_r_district ";

			ps = con.prepareStatement(query2);

			rs = ps.executeQuery();
			SubSidyDTO e;

			while (rs.next()) {
				e = new SubSidyDTO();
				e.setDistrictCode(rs.getString("code"));
				e.setDistrictDes(rs.getString("description"));

				districtList.add(e);

			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return districtList;
	}

	@Override
	public List<SubSidyDTO> getDistrictByProvince(String provinceCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SubSidyDTO> returnList = new ArrayList<SubSidyDTO>();
		try {
			con = ConnectionManager.getConnection();
			String query = " SELECT code,description " + " FROM public.nt_r_district " + " WHERE active = 'A' "
					+ " AND province_code = ? " + " ORDER BY code ";

			ps = con.prepareStatement(query);
			ps.setString(1, provinceCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				SubSidyDTO commonDTO = new SubSidyDTO();
				commonDTO.setDistrictCode(rs.getString("code"));
				commonDTO.setDistrictDes(rs.getString("description"));

				returnList.add(commonDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return returnList;

	}

	@Override
	public List<SubSidyDTO> getSchoolNamesList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SubSidyDTO> returnList = new ArrayList<SubSidyDTO>();
		try {
			con = ConnectionManager.getConnection();
			String query = "select distinct a.ssc_school_name \r\n"
					+ "from public.nt_m_sisu_permit_hol_school_info a\r\n"
					+ "inner join public.nt_m_sisu_permit_hol_service_info b on a.ssc_service_ref_no=b.sps_service_ref_no\r\n"
					+ "and b.sps_service_no is not null\r\n" + "and a.ssc_school_name is not null";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				SubSidyDTO dto = new SubSidyDTO();
				dto.setSchool(rs.getString("ssc_school_name"));

				returnList.add(dto);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return returnList;

	}

	@Override
	public List<SubSidyDTO> getSchoolByProvince(String provinceCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SubSidyDTO> filterSchool = new ArrayList<SubSidyDTO>();
		try {
			con = ConnectionManager.getConnection();
			String query = " select distinct a.ssc_school_name \r\n"
					+ "from public.nt_m_sisu_permit_hol_school_info a\r\n"
					+ "inner join public.nt_m_sisu_permit_hol_service_info b on a.ssc_service_ref_no=b.sps_service_ref_no\r\n"
					+ "and b.sps_service_no is not null\r\n" + "and a.ssc_school_name is not null\r\n"
					+ "and a.ssc_school_province_code=? ";

			ps = con.prepareStatement(query);
			ps.setString(1, provinceCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				SubSidyDTO DTO = new SubSidyDTO();
				DTO.setSchool(rs.getString("ssc_school_name"));

				filterSchool.add(DTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return filterSchool;

	}

	@Override
	public List<SubSidyDTO> getSchoolByDistrict(String districtCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SubSidyDTO> filterSchool = new ArrayList<SubSidyDTO>();
		try {
			con = ConnectionManager.getConnection();
			String query = " select distinct a.ssc_school_name \r\n"
					+ "from public.nt_m_sisu_permit_hol_school_info a\r\n"
					+ "inner join public.nt_m_sisu_permit_hol_service_info b on a.ssc_service_ref_no=b.sps_service_ref_no\r\n"
					+ "and b.sps_service_no is not null\r\n" + "and a.ssc_school_name is not null\r\n"
					+ "and a.ssc_school_district_code=? ";

			ps = con.prepareStatement(query);
			ps.setString(1, districtCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				SubSidyDTO DTO = new SubSidyDTO();
				DTO.setSchool(rs.getString("ssc_school_name"));

				filterSchool.add(DTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return filterSchool;

	}

}
