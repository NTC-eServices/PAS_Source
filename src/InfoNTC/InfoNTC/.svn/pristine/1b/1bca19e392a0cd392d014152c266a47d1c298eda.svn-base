package lk.informatics.ntc.model.service;

import java.math.BigDecimal;
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

import javax.faces.bean.ManagedProperty;

import lk.informatics.ntc.model.dto.FormDTO;
import lk.informatics.ntc.model.dto.GamiAnalyzedDataDTO;
import lk.informatics.ntc.model.dto.GamiRouteDTO;
import lk.informatics.ntc.model.dto.GamiSeriyaDTO;
import lk.informatics.ntc.model.dto.GenerateSurveyFormDTO;
import lk.informatics.ntc.model.dto.IndicatorsDTO;
import lk.informatics.ntc.model.dto.MidPointSurveyDTO;
import lk.informatics.ntc.model.dto.SisuSeriyaDTO;
import lk.informatics.ntc.model.dto.SurveyDTO;
import lk.informatics.ntc.model.dto.SurveyLocationTeamDTO;
import lk.informatics.ntc.model.dto.TenderDTO;
import lk.informatics.ntc.model.dto.TrafficProposalDTO;
import lk.informatics.ntc.view.beans.SessionBackingBean;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.roc.utils.common.Utils;

public class GamiSariyaServiceImpl implements GamiSariyaService {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	private static final long serialVersionUID = 1L;

	public String generateGamiRequestNo() {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;

		int year = Year.now().getValue();
		String currYear = Integer.toString(year).substring(2, 4);
		String strSurveyRequestNo = "";
		String lastyr = "00";

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT app_no FROM public.nt_r_number_generation WHERE code= 'GRM'";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("app_no");
			}

			if (result != null) {

				lastyr = result.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String ApprecordcountN = "00001";
					strSurveyRequestNo = "GRM" + currYear + ApprecordcountN;
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
					strSurveyRequestNo = "GRM" + currYear + ApprecordcountN;
				}
			} else
				strSurveyRequestNo = "GRM" + currYear + "00001";

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
		return strSurveyRequestNo;

	}

	public List requestorDropDown() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<GamiSeriyaDTO> returnList = new ArrayList<GamiSeriyaDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select code,description from nt_r_request_types";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				GamiSeriyaDTO gamiDTO = new GamiSeriyaDTO();
				gamiDTO.setRequestorCode(rs.getString("code"));
				gamiDTO.setRequestortype(rs.getString("description"));

				returnList.add(gamiDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return returnList;
	}

	@Override
	public GamiSeriyaDTO saveGamiRequestorInfo(GamiSeriyaDTO gamiDTO, String loginUser) {

		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String showRequestNo = null;

		try {
			con = ConnectionManager.getConnection();

			if (gamiDTO.getRequestNo() != null && !gamiDTO.getRequestNo().isEmpty()) {
				showRequestNo = gamiDTO.getRequestNo();

			} else {
				showRequestNo = generateGamiRequestNo();
				gamiDTO.setRequestNo(showRequestNo);

				updateNumberGeneration("GRM", showRequestNo, loginUser);
			}

			java.util.Date date = new java.util.Date();
			Timestamp timestamp = new Timestamp(date.getTime());
			java.util.Date reqDate;
			reqDate = gamiDTO.getRequestDate();
			Timestamp ReqDatetimestamp = new Timestamp(reqDate.getTime());
			long seqNo = Utils.getNextValBySeqName(con, " public.seq_nt_m_gami_requestor_info");

			String sql = "insert into public.nt_m_gami_requestor_info "
					+ "(gri_seq,gri_request_no,gri_language_code,gri_requestor_type,gri_id_no,gri_add1,gri_full_name,"
					+ "gri_add1_si,gri_full_name_si,gri_add1_ta,gri_full_name_ta,gri_add2,gri_tel_no,gri_add2_si,"
					+ "gri_mobile_no,gri_add2_ta,gri_email,gri_city,gri_city_si,gri_city_ta,gri_request_date,gri_createdby,gri_createddate)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, gamiDTO.getRequestNo());
			stmt.setString(3, gamiDTO.getPreferedLanguage());
			stmt.setString(4, gamiDTO.getRequestortype());
			stmt.setString(5, gamiDTO.getIdNo());
			stmt.setString(6, gamiDTO.getAddress1());
			stmt.setString(7, gamiDTO.getNameinFull());
			stmt.setString(8, gamiDTO.getAddress1sinhala());
			stmt.setString(9, gamiDTO.getNameinFullSinhala());
			stmt.setString(10, gamiDTO.getAddress1Tamil());
			stmt.setString(11, gamiDTO.getNameinFullTamil());
			stmt.setString(12, gamiDTO.getAddress2());
			stmt.setString(13, gamiDTO.getTelephoneNo());
			stmt.setString(14, gamiDTO.getAddress2Sinhala());
			stmt.setString(15, gamiDTO.getMobileNo());

			stmt.setString(16, gamiDTO.getAddress2Tamil());
			stmt.setString(17, gamiDTO.getEmail());
			stmt.setString(18, gamiDTO.getCity());
			stmt.setString(19, gamiDTO.getCitySinhala());
			stmt.setString(20, gamiDTO.getCityTamil());
			stmt.setTimestamp(21, ReqDatetimestamp);

			stmt.setString(22, loginUser);
			stmt.setTimestamp(23, timestamp);

			int i = stmt.executeUpdate();

			if (i > 0) {
				gamiDTO.setRequestNo(showRequestNo);
			} else {
				gamiDTO.setRequestNo(null);
			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return gamiDTO;
	}

	@Override
	public boolean saveRequstorRouteInfo(GamiSeriyaDTO gamiDTO, String loginUser) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		boolean isModelSave = false;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_m_gami_requestor_route_info");

			String sql = "insert into public.nt_m_gami_requestor_route_info "
					+ "(grr_seq,grr_request_no,grr_destination,grr_destination_sin,grr_destination_ta,grr_origin,grr_origin_sin,grr_origin_ta, "
					+ "grr_via,grr_via_si,grr_via_ta,grr_province,grr_tot_length,grr_unserved_portion,grr_createdby,grr_createddate) "
					+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, gamiDTO.getRequestNo());
			stmt.setString(3, gamiDTO.getDestination());
			stmt.setString(4, gamiDTO.getDestinationSinhala());
			stmt.setString(5, gamiDTO.getDestinationTamil());
			stmt.setString(6, gamiDTO.getOrigin());
			stmt.setString(7, gamiDTO.getOriginSinhala());
			stmt.setString(8, gamiDTO.getOriginTamil());
			stmt.setString(9, gamiDTO.getVia());
			stmt.setString(10, gamiDTO.getViaSinhala());
			stmt.setString(11, gamiDTO.getViaTamil());
			stmt.setString(12, gamiDTO.getProvince());
			stmt.setDouble(13, gamiDTO.getTotalLength());
			stmt.setDouble(14, gamiDTO.getUnservedPotion());
			stmt.setString(15, loginUser);
			stmt.setTimestamp(16, timestamp);

			int i = stmt.executeUpdate();

			if (i > 0) {
				isModelSave = true;
			} else {
				isModelSave = false;
			}
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return isModelSave;
	}

	@Override
	public boolean updateGamiRequestorStatus(GamiSeriyaDTO gamiDTO) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean modelSave = false;

		try {
			con = ConnectionManager.getConnection();

			String query = "UPDATE nt_m_gami_requestor_info " + "set gri_status='P' " + "WHERE gri_request_no=?";

			ps = con.prepareStatement(query);

			ps.setString(1, gamiDTO.getRequestNo());
			int i = ps.executeUpdate();

			if (i > 0) {
				modelSave = true;
			} else {
				modelSave = false;
			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return modelSave;

	}

	@Override
	public List<GamiSeriyaDTO> tblGamiRequestorRouteList(GamiSeriyaDTO gamiDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<GamiSeriyaDTO> data = new ArrayList<GamiSeriyaDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select grr_seq,grr_province,grr_tot_length,grr_unserved_portion,grr_origin_sin,grr_origin_ta,grr_via_si,grr_via_ta,grr_destination_sin,grr_destination_ta, "
					+ "grr_origin,grr_destination,grr_via from nt_m_gami_requestor_route_info "
					+ "where grr_request_no=?";

			ps = con.prepareStatement(query);
			ps.setString(1, gamiDTO.getRequestNo());
			rs = ps.executeQuery();

			while (rs.next()) {

				gamiDTO = new GamiSeriyaDTO();
				gamiDTO.setSeqNo(rs.getString("grr_seq"));
				gamiDTO.setProvince(rs.getString("grr_province"));
				gamiDTO.setTotalLength(rs.getDouble("grr_tot_length"));
				gamiDTO.setUnservedPotion(rs.getDouble("grr_unserved_portion"));

				gamiDTO.setVia(rs.getString("grr_via"));
				gamiDTO.setOrigin(rs.getString("grr_origin"));
				gamiDTO.setDestination(rs.getString("grr_destination"));

				gamiDTO.setViaSinhala(rs.getString("grr_via_si"));
				gamiDTO.setOriginSinhala(rs.getString("grr_origin_sin"));
				gamiDTO.setDestinationSinhala(rs.getString("grr_destination_sin"));

				gamiDTO.setViaTamil(rs.getString("grr_via_si"));
				gamiDTO.setOriginTamil(rs.getString("grr_origin_ta"));
				gamiDTO.setDestinationTamil(rs.getString("grr_destination_ta"));
				data.add(gamiDTO);

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

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	@Override
	public void delete(String value) {
		int result = Integer.parseInt(value);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "delete from public.nt_m_gami_requestor_route_info where grr_seq=?";

			ps = con.prepareStatement(query);

			ps.setInt(1, result);
			ps.executeUpdate();
			con.commit();

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

	}

	@Override
	public GamiSeriyaDTO searchForViewEdit(GamiSeriyaDTO gamiDTO) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select gri_request_no,gri_language_code,gri_requestor_type,gri_id_no,gri_add1,gri_full_name,gri_add1_si, "
					+ "gri_full_name_si,gri_add1_ta,gri_full_name_ta,gri_add2,gri_tel_no,gri_add2_si, "
					+ "gri_mobile_no,gri_add2_ta,gri_email,gri_city,gri_city_si,gri_city_ta,to_char(gri_request_date,'YYYY-MM-dd') as reqdate "
					+ "from nt_m_gami_requestor_info " + "where  gri_request_no=? ";

			ps = con.prepareStatement(query);

			ps.setString(1, gamiDTO.getRequestNo());

			rs = ps.executeQuery();

			while (rs.next()) {
				gamiDTO = new GamiSeriyaDTO();
				gamiDTO.setRequestNo(rs.getString("gri_request_no"));
				gamiDTO.setPreferedLanguage(rs.getString("gri_language_code"));
				gamiDTO.setRequestortype(rs.getString("gri_requestor_type"));
				gamiDTO.setIdNo(rs.getString("gri_id_no"));
				gamiDTO.setAddress1(rs.getString("gri_add1"));
				gamiDTO.setNameinFull(rs.getString("gri_full_name"));
				gamiDTO.setAddress1sinhala(rs.getString("gri_add1_si"));
				gamiDTO.setNameinFullSinhala(rs.getString("gri_full_name_si"));
				gamiDTO.setAddress1Tamil(rs.getString("gri_add1_ta"));
				gamiDTO.setNameinFullTamil(rs.getString("gri_full_name_ta"));
				gamiDTO.setAddress2(rs.getString("gri_add2"));
				gamiDTO.setTelephoneNo(rs.getString("gri_tel_no"));
				gamiDTO.setAddress2Sinhala(rs.getString("gri_add2_si"));
				gamiDTO.setMobileNo(rs.getString("gri_mobile_no"));
				gamiDTO.setAddress2Tamil(rs.getString("gri_add2_ta"));
				gamiDTO.setEmail(rs.getString("gri_email"));
				gamiDTO.setCity(rs.getString("gri_city"));
				gamiDTO.setCitySinhala(rs.getString("gri_city_si"));
				gamiDTO.setCityTamil(rs.getString("gri_city_ta"));
				// gamiDTO.setRequestDate(reqDate);
				gamiDTO.setRequestDate2(rs.getDate("reqDate"));

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return gamiDTO;
	}

	@Override
	public boolean checkViewEdit(GamiSeriyaDTO gamiDTO) {
		boolean value = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select gri_request_no,gri_language_code,gri_requestor_type,gri_id_no,gri_add1,gri_full_name,gri_add1_si, "
					+ "gri_full_name_si,gri_add1_ta,gri_full_name_ta,gri_add2,gri_tel_no,gri_add2_si, "
					+ "gri_mobile_no,gri_add2_ta,gri_email,gri_city,gri_city_si,gri_city_ta,to_char(gri_request_date,'YYYY-MM-dd') "
					+ "from nt_m_gami_requestor_info " + "where  gri_request_no=? ";

			ps = con.prepareStatement(query);

			ps.setString(1, gamiDTO.getRequestNo());
			rs = ps.executeQuery();

			if (!rs.isBeforeFirst()) {
				value = false;
			} else {
				value = true;
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
	public List refNodropDown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String value = "";
		List<String> returnList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select gri_request_no from nt_m_gami_requestor_info where  nt_m_gami_requestor_info.gri_request_no not in"
					+ " (select nt_m_gamisari_survey_initiate.gsi_request_no from nt_m_gamisari_survey_initiate) order by gri_request_no desc ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				value = rs.getString("gri_request_No");

				returnList.add(value);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return returnList;

	}

	@Override
	public void editReqInfo(GamiSeriyaDTO gamiDTO, String loginUser) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();

		Timestamp ReqDatetimestamp = new Timestamp(date.getTime());
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String query = "UPDATE nt_m_gami_requestor_info "
					+ "set gri_language_code=?,gri_requestor_type=?,gri_id_no=?,gri_add1=?,gri_full_name=?,gri_add1_si=?, "
					+ "gri_full_name_si=?,gri_add1_ta=?,gri_full_name_ta=?,gri_add2=?,gri_tel_no=?,gri_add2_si=?, "
					+ "gri_mobile_no=?,gri_add2_ta=?,gri_email=?,gri_city=?,gri_city_si=?,gri_city_ta=?,gri_request_date=? , gri_modifiedby =? , gri_modifieddate =? "
					+ "WHERE gri_request_no=? ";

			ps = con.prepareStatement(query);

			ps.setString(1, gamiDTO.getPreferedLanguage());
			ps.setString(2, gamiDTO.getRequestortype());
			ps.setString(3, gamiDTO.getIdNo());
			ps.setString(4, gamiDTO.getAddress1());
			ps.setString(5, gamiDTO.getNameinFull());
			ps.setString(6, gamiDTO.getAddress1sinhala());
			ps.setString(7, gamiDTO.getNameinFullSinhala());
			ps.setString(8, gamiDTO.getAddress1Tamil());
			ps.setString(9, gamiDTO.getNameinFullTamil());
			ps.setString(10, gamiDTO.getAddress2());
			ps.setString(11, gamiDTO.getTelephoneNo());
			ps.setString(12, gamiDTO.getAddress2Sinhala());
			ps.setString(13, gamiDTO.getMobileNo());

			ps.setString(14, gamiDTO.getAddress2Tamil());
			ps.setString(15, gamiDTO.getEmail());
			ps.setString(16, gamiDTO.getCity());
			ps.setString(17, gamiDTO.getCitySinhala());
			ps.setString(18, gamiDTO.getCityTamil());
			ps.setTimestamp(19, ReqDatetimestamp);
			ps.setString(20, loginUser);
			ps.setTimestamp(21, timestamp);

			ps.setString(22, gamiDTO.getRequestNo());

			ps.executeUpdate();
			con.commit();

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

	}

	@Override
	public void editRouteInfo(GamiSeriyaDTO gamiDTO, String loginUser) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();

		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String query = "update nt_m_gami_requestor_route_info "
					+ "set grr_origin=?,grr_destination=?,grr_origin_sin=?,grr_destination_sin=?,grr_origin_ta=?, "
					+ "grr_destination_ta=?,grr_via=?,grr_province=?,grr_via_si=?,grr_tot_length=?,grr_via_ta=?,grr_unserved_portion=?, grr_modifiedby=? , grr_modifieddate=? "
					+ "where grr_seq=?";

			ps = con.prepareStatement(query);

			ps.setString(1, gamiDTO.getOrigin());
			ps.setString(2, gamiDTO.getDestination());
			ps.setString(3, gamiDTO.getOriginSinhala());
			ps.setString(4, gamiDTO.getDestinationSinhala());
			ps.setString(5, gamiDTO.getOriginTamil());
			ps.setString(6, gamiDTO.getDestinationTamil());
			ps.setString(7, gamiDTO.getVia());
			ps.setString(8, gamiDTO.getProvince());
			ps.setString(9, gamiDTO.getViaSinhala());
			ps.setDouble(10, gamiDTO.getTotalLength());
			ps.setString(11, gamiDTO.getViaTamil());
			ps.setDouble(12, gamiDTO.getUnservedPotion());

			ps.setString(13, loginUser);
			ps.setTimestamp(14, timestamp);

			int result = Integer.parseInt(gamiDTO.getSeqNo());
			ps.setInt(15, result);
			ps.executeUpdate();
			con.commit();

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

	}

	// Initiate survey request

	/**
	 * 1) get request numbers status p
	 */
	@Override
	public List<GamiSeriyaDTO> drpdRequestNoList() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<GamiSeriyaDTO> RequestNoList = new ArrayList<GamiSeriyaDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct gri_request_no \r\n" + "from public.nt_m_gami_requestor_info\r\n"
					+ "where gri_request_no is not null and gri_status='P'\r\n" + "order by gri_request_no desc;";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				GamiSeriyaDTO gisuSariyaDTO = new GamiSeriyaDTO();
				gisuSariyaDTO.setRequestNo(rs.getString("gri_request_no"));

				RequestNoList.add(gisuSariyaDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return RequestNoList;
	}

	@Override
	public List<SisuSeriyaDTO> drpdSurveyRequestNoList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SisuSeriyaDTO> drpdOrganizationList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SisuSeriyaDTO> departmentList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<GamiSeriyaDTO> drpdRequestType() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<GamiSeriyaDTO> requestTypeList = new ArrayList<GamiSeriyaDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select code,description\r\n" + "		from public.nt_r_request_types\r\n"
					+ "		where active='A';";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				GamiSeriyaDTO gamiSeriyaDTO = new GamiSeriyaDTO();
				gamiSeriyaDTO.setRequestTypeCode(rs.getString("code"));
				gamiSeriyaDTO.setRequestTypeDesk(rs.getString("description"));

				requestTypeList.add(gamiSeriyaDTO);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return requestTypeList;
	}

	@Override
	public GamiSeriyaDTO getRequestDetailsForInitiateByRequestNo(GamiSeriyaDTO gamiSeriyaDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "	select gri_requestor_type,gri_request_date\r\n" + "	from nt_m_gami_requestor_info\r\n"
					+ "	where gri_request_no = ? limit 1";

			ps = con.prepareStatement(query);
			ps.setString(1, gamiSeriyaDTO.getRequestNo());
			rs = ps.executeQuery();

			while (rs.next()) {

				gamiSeriyaDTO.setRequestTypeCode(rs.getString("gri_requestor_type"));

				if (rs.getTimestamp("gri_request_date") != null) {
					Date requestDate = new Date(rs.getTimestamp("gri_request_date").getTime());
					gamiSeriyaDTO.setRequestDateObj(requestDate);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return gamiSeriyaDTO;
	}

	@Override
	public GamiSeriyaDTO getInitiateDetailsByServeyReqNo(GamiSeriyaDTO gamiSeriyaDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select A.gri_requestor_type as requesterType,A.gri_request_date as requestDate,\r\n"
					+ "B.gsi_department_code as departmentCode,B.gsi_organization_code as organizationCode,B.gsi_reason_for_survey as reasonForSurvey,\r\n"
					+ "B.gsi_survey_no as surveyNo,B.gsi_survey_type as surveyType,B.gsi_survey_method as surveyMethod,B.gsi_survey_info_remarks as surveyRemarks,B.gsi_special_remark as specialRemark,B.gsi_status as status,B.gsi_request_no as requestNo\r\n"
					+ "from public.nt_m_gami_requestor_info A inner join public.nt_m_gamisari_survey_initiate B \r\n"
					+ "on A.gri_request_no = B.gsi_request_no\r\n" + "where B.gsi_survey_request_no = ? ";

			ps = con.prepareStatement(query);
			ps.setString(1, gamiSeriyaDTO.getSurveyRequestNo());
			rs = ps.executeQuery();

			while (rs.next()) {

				gamiSeriyaDTO.setRequestTypeCode(rs.getString("requesterType"));

				if (rs.getTimestamp("requestDate") != null) {
					Date requestDate = new Date(rs.getTimestamp("requestDate").getTime());
					gamiSeriyaDTO.setRequestDateObj(requestDate);
				}

				gamiSeriyaDTO.setDepartmentCode(rs.getString("departmentCode"));
				gamiSeriyaDTO.setOrganizationCode(rs.getString("organizationCode"));
				gamiSeriyaDTO.setReasonForSurvey(rs.getString("reasonForSurvey"));

				gamiSeriyaDTO.setSurveyNo(rs.getString("surveyNo"));
				gamiSeriyaDTO.setSurviceTypeCode(rs.getString("surveyType"));
				gamiSeriyaDTO.setSurviceMethodCode(rs.getString("surveyMethod"));
				gamiSeriyaDTO.setRemark(rs.getString("surveyRemarks"));
				gamiSeriyaDTO.setApproveProcessRemark(rs.getString("specialRemark"));

				if (rs.getString("status") != null && rs.getString("status").equals("A")) {
					gamiSeriyaDTO.setStatusDesc("Approved");
				} else if (rs.getString("status") != null && rs.getString("status").equals("R")) {
					gamiSeriyaDTO.setStatusDesc("Rejected");
				} else {
					gamiSeriyaDTO.setStatusDesc(null);
				}

				gamiSeriyaDTO.setStatus(rs.getString("status"));
				gamiSeriyaDTO.setRequestNo(rs.getString("requestNo"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return gamiSeriyaDTO;
	}

	@Override
	public List<GamiSeriyaDTO> tblOriginDestionDetailsByRequestNo(GamiSeriyaDTO gamiSeriyaDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<GamiSeriyaDTO> tblOriginDestinationList = new ArrayList<GamiSeriyaDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select A.grr_seq as seqNo,A.grr_survey_req_no as surveyReqNo,A.grr_origin as origin,\r\n"
					+ "			A.grr_destination as destination,A.grr_via as via,A.grr_province as province,\r\n"
					+ "			A.grr_tot_length as totLength,A.grr_unserved_portion as unservedPortion,\r\n"
					+ "			B.gsi_status as status,B.gsi_survey_no as surveyNo\r\n"
					+ "			from public.nt_m_gami_requestor_route_info A inner join public.nt_m_gamisari_survey_initiate B \r\n"
					+ "			on A.grr_survey_req_no = B.gsi_survey_request_no\r\n"
					+ "			where A.grr_request_no = ?;";

			ps = con.prepareStatement(query);

			ps.setString(1, gamiSeriyaDTO.getRequestNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				GamiSeriyaDTO gamiSariyaDTO = new GamiSeriyaDTO();
				gamiSariyaDTO.setRouteSeqNo(rs.getString("seqNo"));
				gamiSariyaDTO.setSurveyRequestNo(rs.getString("surveyReqNo"));
				gamiSariyaDTO.setOriginCode(rs.getString("origin"));
				gamiSariyaDTO.setDestination(rs.getString("destination"));
				gamiSariyaDTO.setVia(rs.getString("via"));
				gamiSariyaDTO.setProvince(rs.getString("province"));
				gamiSariyaDTO.setTotalLength(rs.getDouble("totLength"));
				gamiSariyaDTO.setUnservedPortion(rs.getDouble("unservedPortion"));
				gamiSariyaDTO.setSurveyNo(rs.getString("surveyNo"));

				if (rs.getString("status") != null && rs.getString("status").equals("A")) {
					gamiSariyaDTO.setStatus("Approved");
				} else if (rs.getString("status") != null && rs.getString("status").equals("R")) {
					gamiSariyaDTO.setStatus("Rejected");
				}

				tblOriginDestinationList.add(gamiSariyaDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return tblOriginDestinationList;
	}

	@Override
	public boolean updateInitiateSurveyDetailsBySurveyReqNo(GamiSeriyaDTO gamiSeriyaDTO, String loginUser) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		boolean isModelSave = false;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "update public.nt_m_gamisari_survey_initiate\r\n"
					+ "set gsi_organization_code=?,gsi_department_code=?,gsi_reason_for_survey=?,\r\n"
					+ " gsi_modifiedby =?, gsi_modifieddate =? " + "where gsi_survey_request_no=?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, gamiSeriyaDTO.getOrganizationCode());
			stmt.setString(2, gamiSeriyaDTO.getDepartmentCode());
			stmt.setString(3, gamiSeriyaDTO.getReasonForSurvey());

			stmt.setString(4, loginUser);
			stmt.setTimestamp(5, timestamp);

			stmt.setString(6, gamiSeriyaDTO.getSurveyRequestNo());

			int i = stmt.executeUpdate();

			if (i > 0) {
				isModelSave = true;
			} else {
				isModelSave = false;
			}
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return isModelSave;
	}

	@Override
	public String generateGamiSurveyRequestNo() {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;

		int year = Year.now().getValue();
		String currYear = Integer.toString(year).substring(2, 4);
		String strServiceReferenceNo = "";
		String lastyr = "00";

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT app_no FROM public.nt_r_number_generation WHERE code= 'GSR'";
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("app_no");
			}

			if (result != null) {

				lastyr = result.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String ApprecordcountN = "00001";
					strServiceReferenceNo = "GSR" + currYear + ApprecordcountN;
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
					strServiceReferenceNo = "GSR" + currYear + ApprecordcountN;
				}
			} else
				strServiceReferenceNo = "GSR" + currYear + "00001";

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return strServiceReferenceNo;

	}

	@Override
	public boolean insertInitiateSurveyReqDet(GamiSeriyaDTO gamiSeriyaDTO, String loginUser) {

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date today = new Date();
		String dateStr = dateFormat.format(today);

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		boolean isModelSave = false;

		try {

			date = dateFormat.parse(dateStr);
			timestamp = new Timestamp(date.getTime());

			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_m_gamisari_survey_initiate");

			String query = "INSERT INTO public.nt_m_gamisari_survey_initiate\r\n"
					+ "(gsi_seq, gsi_request_no, gsi_survey_request_no, gsi_organization_code, gsi_department_code,gsi_reason_for_survey,gsi_createdby , gsi_createddate)\r\n"
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?);	";
			ps = con.prepareStatement(query);
			ps.setLong(1, seqNo);
			ps.setString(2, gamiSeriyaDTO.getRequestNo());

			String generatedServiceRequestNo = generateGamiSurveyRequestNo();
			gamiSeriyaDTO.setSurveyRequestNo(generatedServiceRequestNo);
			updateNumberGeneration("GSR", generatedServiceRequestNo, loginUser);

			ps.setString(3, generatedServiceRequestNo);
			ps.setString(4, gamiSeriyaDTO.getOrganizationCode());
			ps.setString(5, gamiSeriyaDTO.getDepartmentCode());
			ps.setString(6, gamiSeriyaDTO.getReasonForSurvey());
			ps.setString(7, loginUser);
			ps.setTimestamp(8, timestamp);

			int i = ps.executeUpdate();

			if (i > 0) {
				isModelSave = true;
			} else {
				isModelSave = false;
			}

			String q2 = "UPDATE public.nt_m_gami_requestor_route_info\r\n" + "set grr_survey_req_no = ?\r\n"
					+ "WHERE grr_seq=?;";

			ps2 = con.prepareStatement(q2);
			ps2.setString(1, gamiSeriyaDTO.getSurveyRequestNo());
			ps2.setInt(2, Integer.valueOf(gamiSeriyaDTO.getRouteSeqNo()));
			ps2.executeUpdate();

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {

			if (!isModelSave) {
				gamiSeriyaDTO.setSurveyRequestNo("");
			}

			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(con);
		}

		return isModelSave;
	}

	// initiate survey request process
	@Override
	public List<GamiSeriyaDTO> drpdSurveyProcessRequestNoList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<GamiSeriyaDTO> RequestNoList = new ArrayList<GamiSeriyaDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select gsi_survey_request_no ,gsi_survey_no\r\n"
					+ "from public.nt_m_gamisari_survey_initiate\r\n" + "where gsi_survey_request_no is not null\r\n"
					+ "order by gsi_survey_request_no desc;";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				GamiSeriyaDTO gisuSariyaDTO = new GamiSeriyaDTO();
				gisuSariyaDTO.setSurveyRequestNo(rs.getString("gsi_survey_request_no"));
				gisuSariyaDTO.setSurveyNo(rs.getString("gsi_survey_no"));
				RequestNoList.add(gisuSariyaDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return RequestNoList;
	}

	@Override
	public List<GamiSeriyaDTO> drpdServiceTypeList() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<GamiSeriyaDTO> serviceTypeList = new ArrayList<GamiSeriyaDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select code, description\r\n" + "from public.nt_r_survey_types\r\n" + "where active = 'A';";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				GamiSeriyaDTO gisuSariyaDTO = new GamiSeriyaDTO();
				gisuSariyaDTO.setSurviceTypeCode(rs.getString("code"));
				gisuSariyaDTO.setSurviceTypeDesc(rs.getString("description"));
				serviceTypeList.add(gisuSariyaDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return serviceTypeList;

	}

	@Override
	public List<GamiSeriyaDTO> drpdServiceMethodsList() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<GamiSeriyaDTO> serviceMethodsList = new ArrayList<GamiSeriyaDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select code,description\r\n" + "from public.nt_r_survey_methods\r\n"
					+ "where active = 'A';";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				GamiSeriyaDTO gisuSariyaDTO = new GamiSeriyaDTO();
				gisuSariyaDTO.setSurviceMethodCode(rs.getString("code"));
				gisuSariyaDTO.setSurviceMethodDesc(rs.getString("description"));
				serviceMethodsList.add(gisuSariyaDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return serviceMethodsList;

	}

	public GamiSeriyaDTO insertSurveyProcessDet(GamiSeriyaDTO gamiSeriyaDTO) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_gamisari_survey_initiate\r\n"
					+ "SET gsi_survey_type=?, gsi_survey_method=?,gsi_survey_info_remarks = ?, gsi_survey_no=?\r\n"
					+ "WHERE gsi_survey_request_no=?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, gamiSeriyaDTO.getSurviceTypeCode());
			stmt.setString(2, gamiSeriyaDTO.getSurviceMethodCode());
			stmt.setString(3, gamiSeriyaDTO.getRemark());

			String surveyNo = "";
			if (gamiSeriyaDTO.getSurveyNo() != null && !gamiSeriyaDTO.getSurveyNo().isEmpty()
					&& !gamiSeriyaDTO.getSurveyNo().equalsIgnoreCase("")) {

				stmt.setString(4, gamiSeriyaDTO.getSurveyNo());

			} else {
				surveyNo = generateGamiSurveyNo();
				gamiSeriyaDTO.setSurveyNo(surveyNo);
				stmt.setString(4, surveyNo);
			}

			stmt.setString(5, gamiSeriyaDTO.getSurveyRequestNo());

			int i = stmt.executeUpdate();

			if (i > 0) {

			} else {
				gamiSeriyaDTO.setSurveyNo("");

			}
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return gamiSeriyaDTO;

	}

	@Override
	public String generateGamiSurveyNo() {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;

		int year = Year.now().getValue();
		String currYear = Integer.toString(year).substring(2, 4);
		String strSurveyNo = "";
		String lastyr = "00";

		try {
			con = ConnectionManager.getConnection();

			String sql = " SELECT gsi_survey_no \r\n" + " FROM public.nt_m_gamisari_survey_initiate \r\n"
					+ " WHERE gsi_survey_no IS NOT NULL ORDER BY gsi_survey_no desc LIMIT 1";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("gsi_survey_no");
			}

			if (result != null) {

				lastyr = result.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String ApprecordcountN = "00001";
					strSurveyNo = "GSN" + currYear + ApprecordcountN;
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
					strSurveyNo = "GSN" + currYear + ApprecordcountN;
				}
			} else
				strSurveyNo = "GSN" + currYear + "00001";

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return strSurveyNo;
	}

	@Override
	public List<GamiSeriyaDTO> drpdSurveyNoList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<GamiSeriyaDTO> RequestNoList = new ArrayList<GamiSeriyaDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select gsi_survey_request_no ,gsi_survey_no\r\n"
					+ "from public.nt_m_gamisari_survey_initiate\r\n" + "where gsi_survey_no is not null\r\n"
					+ "order by gsi_survey_no desc;";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				GamiSeriyaDTO gisuSariyaDTO = new GamiSeriyaDTO();
				gisuSariyaDTO.setSurveyRequestNo(rs.getString("gsi_survey_request_no"));
				gisuSariyaDTO.setSurveyNo(rs.getString("gsi_survey_no"));
				RequestNoList.add(gisuSariyaDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return RequestNoList;
	}

	public boolean updateGamiSariyaStatus(GamiSeriyaDTO gamiSeriyaDTO, String loginUser) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		boolean isModelSave = false;

		java.util.Date date = new java.util.Date();
		Timestamp today = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_gamisari_survey_initiate\r\n"
					+ "set gsi_status=?,gsi_special_remark=?, gsi_survey_info_approve_reject_by=?, gsi_survey_info_approve_reject_date=?\r\n"
					+ "WHERE gsi_survey_request_no =?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, gamiSeriyaDTO.getStatus());
			stmt.setString(2, gamiSeriyaDTO.getApproveProcessRemark());
			stmt.setString(3, loginUser);
			stmt.setTimestamp(4, today);
			stmt.setString(5, gamiSeriyaDTO.getSurveyRequestNo());

			int i = stmt.executeUpdate();

			if (i > 0) {

				isModelSave = true;

			} else {
				isModelSave = false;
			}
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return isModelSave;
	}

	@Override
	public List<GamiSeriyaDTO> drpdSurveyReqNoListForApprove() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<GamiSeriyaDTO> surveyRequestNoList = new ArrayList<GamiSeriyaDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select gsi_survey_request_no\r\n" + "from public.nt_m_gamisari_survey_initiate\r\n"
					+ "where gsi_survey_no is not null and gsi_survey_request_no is not null order by gsi_survey_request_no desc;";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				GamiSeriyaDTO gisuSariyaDTO = new GamiSeriyaDTO();
				gisuSariyaDTO.setSurveyRequestNo(rs.getString("gsi_survey_request_no"));

				surveyRequestNoList.add(gisuSariyaDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return surveyRequestNoList;
	}

	@Override
	public List<GamiSeriyaDTO> tblGrantApprovalSurveyProcess(GamiSeriyaDTO gamiSeriyaDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<GamiSeriyaDTO> tblOriginDestinationList = new ArrayList<GamiSeriyaDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select A.grr_seq as seqNo,A.grr_survey_req_no as surveyReqNo,A.grr_origin as origin,\r\n"
					+ "A.grr_destination as destination,A.grr_via as via,A.grr_province as province,\r\n"
					+ "A.grr_tot_length as totLength,A.grr_unserved_portion as unservedPortion,\r\n"
					+ "B.gsi_status as status\r\n"
					+ "from public.nt_m_gami_requestor_route_info A inner join public.nt_m_gamisari_survey_initiate B \r\n"
					+ "on A.grr_survey_req_no = B.gsi_survey_request_no\r\n"
					+ "where A.grr_request_no = ? and B.gsi_survey_no is not null;";

			ps = con.prepareStatement(query);

			ps.setString(1, gamiSeriyaDTO.getRequestNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				GamiSeriyaDTO gamiSariyaDTO = new GamiSeriyaDTO();
				gamiSariyaDTO.setRouteSeqNo(rs.getString("seqNo"));
				gamiSariyaDTO.setSurveyRequestNo(rs.getString("surveyReqNo"));
				gamiSariyaDTO.setOriginCode(rs.getString("origin"));
				gamiSariyaDTO.setDestination(rs.getString("destination"));
				gamiSariyaDTO.setVia(rs.getString("via"));
				gamiSariyaDTO.setProvince(rs.getString("province"));
				gamiSariyaDTO.setTotalLength(rs.getDouble("totLength"));
				gamiSariyaDTO.setUnservedPortion(rs.getDouble("unservedPortion"));

				if (rs.getString("status") != null && rs.getString("status").equals("A")) {
					gamiSariyaDTO.setStatus("Approved");
				} else if (rs.getString("status") != null && rs.getString("status").equals("R")) {
					gamiSariyaDTO.setStatus("Rejected");
				}

				tblOriginDestinationList.add(gamiSariyaDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return tblOriginDestinationList;
	}

	@Override
	public List<GamiSeriyaDTO> drpdRequestNoListForSurveyProcess() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<GamiSeriyaDTO> RequestNoList = new ArrayList<GamiSeriyaDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct gsi_request_no\r\n" + "from public.nt_m_gamisari_survey_initiate\r\n"
					+ "where gsi_survey_request_no is not null and gsi_request_no is not null order by gsi_request_no desc ;";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				GamiSeriyaDTO gisuSariyaDTO = new GamiSeriyaDTO();
				gisuSariyaDTO.setRequestNo(rs.getString("gsi_request_no"));

				RequestNoList.add(gisuSariyaDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return RequestNoList;

	}

	@Override
	public List<GamiSeriyaDTO> tblInitiateSurveyRequest(GamiSeriyaDTO gamiSeriyaDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<GamiSeriyaDTO> tblOriginDestinationList = new ArrayList<GamiSeriyaDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "	select grr_seq as seqNo,grr_survey_req_no as surveyReqNo,grr_origin as origin,\r\n"
					+ "	grr_destination as destination,grr_via as via,grr_province as province,\r\n"
					+ "	grr_tot_length as totLength,grr_unserved_portion as unservedPortion\r\n"
					+ "	from public.nt_m_gami_requestor_route_info \r\n"
					+ "	where grr_request_no = ? order by seqNo ;";

			ps = con.prepareStatement(query);

			ps.setString(1, gamiSeriyaDTO.getRequestNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				GamiSeriyaDTO gamiSariyaDTO = new GamiSeriyaDTO();
				gamiSariyaDTO.setRouteSeqNo(rs.getString("seqNo"));
				gamiSariyaDTO.setSurveyRequestNo(rs.getString("surveyReqNo"));
				gamiSariyaDTO.setOriginCode(rs.getString("origin"));
				gamiSariyaDTO.setDestination(rs.getString("destination"));
				gamiSariyaDTO.setVia(rs.getString("via"));
				gamiSariyaDTO.setProvince(rs.getString("province"));
				gamiSariyaDTO.setTotalLength(rs.getDouble("totLength"));
				gamiSariyaDTO.setUnservedPortion(rs.getDouble("unservedPortion"));

				tblOriginDestinationList.add(gamiSariyaDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return tblOriginDestinationList;
	}

	// survey management
	@Override
	public List<SurveyDTO> drpdSurveyNoListForSurveyManagement() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SurveyDTO> surveyNoList = new ArrayList<SurveyDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select A.gsi_survey_no,A.gsi_survey_type,A.gsi_survey_method\r\n"
					+ "from public.nt_m_gamisari_survey_initiate A\r\n"
					+ "inner join public.nt_h_survey_task_his B on B.tsd_survey_no = A.gsi_survey_no\r\n"
					+ "where A.gsi_status='A' and B.tsd_task_code='SU004' and B.tsd_status='O' and A.gsi_survey_no is not null\r\n"
					+ "order by A.gsi_survey_no desc\r\n" + ";";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				SurveyDTO surveyDTO = new SurveyDTO();
				surveyDTO.setSurveyNo(rs.getString("gsi_survey_no"));

				surveyNoList.add(surveyDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return surveyNoList;
	}

	@Override
	public SurveyDTO getSurveyNoDet(SurveyDTO surveyDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select A.gsi_cost_esti_remark as approveCostRemark,A.gsi_cost_est_status as approveCostStatus, B.description as surveyType,C.description as surveyMethod\r\n"
					+ "from public.nt_m_gamisari_survey_initiate A \r\n" + "inner join public.nt_r_survey_types B\r\n"
					+ "on A.gsi_survey_type = B.code\r\n" + "inner join public.nt_r_survey_methods C\r\n"
					+ "on A.gsi_survey_method = C.code\r\n" + "where A.gsi_survey_no=?";

			ps = con.prepareStatement(query);
			ps.setString(1, surveyDTO.getSurveyNo());
			rs = ps.executeQuery();

			while (rs.next()) {

				surveyDTO.setRemarks(rs.getString("approveCostRemark"));
				surveyDTO.setSurveyTypeDescription(rs.getString("surveyType"));
				surveyDTO.setSurveyMethodDescription(rs.getString("surveyMethod"));
				surveyDTO.setStatus(rs.getString("approveCostStatus"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return surveyDTO;
	}

	@Override
	public boolean approveRejectCostEstimation(String surveyNo, String status, String loginUser,
			SurveyLocationTeamDTO surveyLocationTeamDTO) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;

		java.util.Date date = new java.util.Date();
		Timestamp today = new Timestamp(date.getTime());

		boolean isModelSave = false;

		try {
			con = ConnectionManager.getConnection();

			String sql = "update public.nt_m_gamisari_survey_initiate \r\n"
					+ "SET gsi_cost_est_status = ?,gsi_cost_esti_tot_cost = ?,gsi_cost_esti_remark = ?,gsi_approve_reject_by=?,gsi_approve_reject_date=? \r\n"
					+ "WHERE gsi_survey_no = ?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, status);
			stmt.setBigDecimal(2, surveyLocationTeamDTO.getTotalAmount());
			stmt.setString(3, surveyLocationTeamDTO.getSpecialRemark());
			stmt.setString(4, loginUser);
			stmt.setTimestamp(5, today);
			stmt.setString(6, surveyNo);
			int i = stmt.executeUpdate();

			if (i > 0) {

				isModelSave = true;

			} else {
				isModelSave = false;
			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return isModelSave;

	}

	@Override
	public boolean updateApproveCostEstimateStatus(String status, String surveyNo) {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;

		boolean isModelSave = false;

		try {
			con = ConnectionManager.getConnection();

			String sql = "update public.nt_m_gamisari_survey_initiate \r\n" + "SET gsi_cost_est_status = ? \r\n"
					+ "WHERE gsi_survey_no = ?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, status);
			stmt.setString(2, surveyNo);

			int i = stmt.executeUpdate();

			if (i > 0) {

				isModelSave = true;

			} else {
				isModelSave = false;
			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return isModelSave;
	}

	// generate survey form
	@Override
	public GenerateSurveyFormDTO getSurveyNoDetForGenerateSurveyForm(GenerateSurveyFormDTO generateSurveyFormDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select A.gsi_cost_esti_remark as approveCostRemark,A.gsi_cost_est_status as approveCostStatus, B.description as surveyType,C.description as surveyMethod\r\n"
					+ "from public.nt_m_gamisari_survey_initiate A \r\n" + "inner join public.nt_r_survey_types B\r\n"
					+ "on A.gsi_survey_type = B.code\r\n" + "inner join public.nt_r_survey_methods C\r\n"
					+ "on A.gsi_survey_method = C.code\r\n" + "where A.gsi_survey_no=?";

			ps = con.prepareStatement(query);
			ps.setString(1, generateSurveyFormDTO.getSurveyNo());
			rs = ps.executeQuery();

			while (rs.next()) {

				generateSurveyFormDTO.setSurveyType_des(rs.getString("surveyType"));
				generateSurveyFormDTO.setSurveyMethod_des(rs.getString("surveyMethod"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return generateSurveyFormDTO;
	}

	@Override
	public List<GenerateSurveyFormDTO> drpdSurveyNoListForGenerateSurveyForm(String taskCode, String taskStatus,
			String status) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<GenerateSurveyFormDTO> surveyNoList = new ArrayList<GenerateSurveyFormDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select gsi_survey_no\r\n" + "from public.nt_m_gamisari_survey_initiate\r\n"
					+ "where gsi_status=? and gsi_survey_no is not null\r\n" + "order by gsi_survey_no desc;";
			ps = con.prepareStatement(query);

			ps.setString(1, status);

			rs = ps.executeQuery();

			while (rs.next()) {
				GenerateSurveyFormDTO gisuSariyaDTO = new GenerateSurveyFormDTO();
				gisuSariyaDTO.setSurveyNo(rs.getString("gsi_survey_no"));
				surveyNoList.add(gisuSariyaDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return surveyNoList;
	}

	@Override
	public boolean gamiSeriyaSaveForm(GenerateSurveyFormDTO generateSurveyFormDTO,
			List<GenerateSurveyFormDTO> indicators_list, String user) {
		boolean success = false;
		Connection con = null;
		PreparedStatement ps = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			// save form details
			long seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_m_gami_form");
			String query = "INSERT INTO public.nt_m_gami_form (gmf_seqno, gmf_survey_no, gmf_form_id, gmf_temp_id, gmf_form_description, gmf_header_label, gmf_header_label_sinhala, gmf_header_label_tamil, gmf_created_by, gmf_created_date) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?);";

			ps = con.prepareStatement(query);
			ps.setLong(1, seqNo);
			ps.setString(2, generateSurveyFormDTO.getSurveyNo());
			ps.setString(3, generateSurveyFormDTO.getFormID());
			ps.setString(4, generateSurveyFormDTO.getCopyTemplateID());
			ps.setString(5, generateSurveyFormDTO.getFormDescription());
			ps.setString(6, generateSurveyFormDTO.getHeaderLabel());
			ps.setString(7, generateSurveyFormDTO.getHeaderLabel_sinhala());
			ps.setString(8, generateSurveyFormDTO.getHeaderLabel_tamil());
			ps.setString(9, user);
			ps.setTimestamp(10, timestamp);
			ps.executeUpdate();
			con.commit();

			// save indicators
			for (int i = 0; i < indicators_list.size(); i++) {
				long seqNo_indicator = Utils.getNextValBySeqName(con, "seq_nt_t_indicators");
				String query2 = "INSERT INTO public.nt_t_indicators(seqno, form_id, field_name, field_name_sinhala, field_name_tamil, field_type,"
						+ " validation_method,  field_length, mandatory_field,"
						+ " active, lov_field, created_by, created_date,display_after,field_definition,display_order) "
						+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

				ps = con.prepareStatement(query2);
				ps.setLong(1, seqNo_indicator);
				ps.setString(2, generateSurveyFormDTO.getFormID());
				ps.setString(3, indicators_list.get(i).getFieldName());
				ps.setString(4, indicators_list.get(i).getFieldName_sinhala());
				ps.setString(5, indicators_list.get(i).getFieldName_tamil());
				ps.setString(6, indicators_list.get(i).getFieldType());
				ps.setString(7, indicators_list.get(i).getValidationMethod());
				ps.setInt(8, indicators_list.get(i).getFieldLength());
				if (indicators_list.get(i).getMandatoryField() == true) {
					ps.setString(9, "Y");
				} else if (indicators_list.get(i).getMandatoryField() == false) {
					ps.setString(9, "N");
				}
				if (indicators_list.get(i).getActive() == true) {
					ps.setString(10, "Y");
				} else if (indicators_list.get(i).getActive() == false) {
					ps.setString(10, "N");
				}
				if (indicators_list.get(i).getFieldType().equals("FT01")) {
					ps.setString(11, "Y");
				} else if (!indicators_list.get(i).getFieldType().equals("FT01")) {
					ps.setString(11, "N");
				}
				ps.setString(12, user);
				ps.setTimestamp(13, timestamp);
				ps.setString(14, indicators_list.get(i).getDisplayAfter());
				ps.setString(15, indicators_list.get(i).getFieldDefinition());
				ps.setInt(16, indicators_list.get(i).getDisplayOrder());
				ps.executeUpdate();

				if (indicators_list.get(i).getFieldType().equals("FT01")) {
					// save indicator values
					for (int j = 0; j < indicators_list.get(i).getIndicator_values_list().size(); j++) {
						long seqNo_indicator_values = Utils.getNextValBySeqName(con, "seq_nt_t_indicator_lov");
						String query3 = "INSERT INTO public.nt_t_indicator_lov (seqno, form_id, code, description, description_sinhala, description_tamil, created_by, created_date, indicators_seqno) "
								+ "VALUES (?,?,?,?,?,?,?,?,?);";

						ps = con.prepareStatement(query3);
						ps.setLong(1, seqNo_indicator_values);
						ps.setString(2, generateSurveyFormDTO.getFormID());
						ps.setString(3, indicators_list.get(i).getIndicator_values_list().get(j).getLOV_code());
						ps.setString(4, indicators_list.get(i).getIndicator_values_list().get(j).getLOV_description());
						ps.setString(5,
								indicators_list.get(i).getIndicator_values_list().get(j).getLOV_description_sinhala());
						ps.setString(6,
								indicators_list.get(i).getIndicator_values_list().get(j).getLOV_description_tamil());
						ps.setString(7, user);
						ps.setTimestamp(8, timestamp);
						ps.setLong(9, seqNo_indicator);
						ps.executeUpdate();
					}
				}
			}
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
	public GenerateSurveyFormDTO getGamiFormDetails(GenerateSurveyFormDTO generateSurveyFormDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT gmf_seqno, gmf_form_id, gmf_survey_no, gmf_temp_id, gmf_form_description, gmf_header_label, gmf_header_label_sinhala, gmf_header_label_tamil\r\n"
					+ "FROM public.nt_m_gami_form WHERE gmf_survey_no=? order by gmf_seqno limit 1 ";

			ps = con.prepareStatement(query);
			ps.setString(1, generateSurveyFormDTO.getSurveyNo());
			rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getString("gmf_survey_no") != null && rs.getString("gmf_survey_no").isEmpty()
						&& rs.getString("gmf_survey_no").equals("")) {
					generateSurveyFormDTO.setSurveyNo(rs.getString("gmf_survey_no"));
				}

				generateSurveyFormDTO.setFormID(rs.getString("gmf_form_id"));
				generateSurveyFormDTO.setCopyTemplateID(rs.getString("gmf_temp_id"));
				generateSurveyFormDTO.setFormDescription(rs.getString("gmf_form_description"));
				generateSurveyFormDTO.setHeaderLabel(rs.getString("gmf_header_label"));
				generateSurveyFormDTO.setHeaderLabel_sinhala(rs.getString("gmf_header_label_sinhala"));
				generateSurveyFormDTO.setHeaderLabel_tamil(rs.getString("gmf_header_label_tamil"));

				GenerateSurveyFormDTO tempt = new GenerateSurveyFormDTO();
				tempt = getSurveyNoDetForGenerateSurveyForm(generateSurveyFormDTO);
				generateSurveyFormDTO.setSurveyType_des(tempt.getSurveyType_des());
				generateSurveyFormDTO.setSurveyMethod_des(tempt.getSurveyMethod_des());

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return generateSurveyFormDTO;
	}

	@Override
	public List<GenerateSurveyFormDTO> getGamiIndicatorsList(GenerateSurveyFormDTO generateSurveyFormDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<GenerateSurveyFormDTO> data = new ArrayList<GenerateSurveyFormDTO>();

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT a.seqno, a.form_id, b.survey_no, a.field_name, a.field_name_sinhala, a.field_name_tamil, a.field_type, c.description as field_type_des, a.validation_method, "
					+ "d.description as validation_method_des, a.field_length, a.mandatory_field, "
					+ "a.active, a.lov_field, a.display_after, a.display_order, a.field_definition, e.description as field_definition_des FROM public.nt_t_indicators a inner join nt_m_form b on a.form_id=b.form_id inner join nt_r_field_type c on a.field_type = c.code "
					+ "left outer join nt_r_field_validation d on a.validation_method = d.code left outer join nt_r_field_definition e on a.field_definition = e.code WHERE b.survey_no=?;";
			ps = con.prepareStatement(query);
			ps.setString(1, generateSurveyFormDTO.getSurveyNo());
			rs = ps.executeQuery();
			GenerateSurveyFormDTO p;

			while (rs.next()) {
				p = new GenerateSurveyFormDTO();
				p.setSeqNo(rs.getLong("seqno"));
				p.setFieldName(rs.getString("field_name"));
				p.setFieldName_sinhala(rs.getString("field_name_sinhala"));
				p.setFieldName_tamil(rs.getString("field_name_tamil"));
				p.setFieldType(rs.getString("field_type"));
				p.setFieldType_des(rs.getString("field_type_des"));
				p.setValidationMethod(rs.getString("validation_method"));
				p.setValidationMethod_des(rs.getString("validation_method_des"));
				p.setFieldDefinition(rs.getString("field_definition"));
				p.setFieldDefinition_des(rs.getString("field_definition_des"));
				p.setFieldLength(rs.getInt("field_length"));
				p.setDisplayAfter(rs.getString("display_after"));
				p.setDisplayOrder(rs.getInt("display_order"));

				if (rs.getString("mandatory_field").equals("Y")) {
					p.setMandatoryField(true);
				} else if (rs.getString("mandatory_field").equals("N")) {
					p.setMandatoryField(false);
				}
				if (rs.getString("active").equals("Y")) {
					p.setActive(true);
				} else if (rs.getString("active").equals("N")) {
					p.setActive(false);
				}
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
	public List<GenerateSurveyFormDTO> getGamiTblIndicatorsList(GenerateSurveyFormDTO generateSurveyFormDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<GenerateSurveyFormDTO> data = new ArrayList<GenerateSurveyFormDTO>();

		try {

			con = ConnectionManager.getConnection();

			boolean surveyNo = true;
			String where = " b.gmf_survey_no=?";

			if (generateSurveyFormDTO.getSurveyNo() != null && !generateSurveyFormDTO.getSurveyNo().trim().isEmpty()
					&& !generateSurveyFormDTO.getSurveyNo().trim().equals("")) {
				where = " b.gmf_survey_no=?";
			} else if (generateSurveyFormDTO.getFormID() != null && !generateSurveyFormDTO.getFormID().trim().isEmpty()
					&& !generateSurveyFormDTO.getFormID().trim().equals("")) {
				where = " a.form_id=?";
				surveyNo = false;
			}

			String query = "SELECT a.seqno, a.form_id, a.field_name, a.field_name_sinhala, a.field_name_tamil, a.field_type,a.validation_method,a.field_length, a.mandatory_field,\r\n"
					+ "a.active, a.lov_field, a.display_after, a.display_order, a.field_definition, e.description as field_definition_des, \r\n"
					+ "b.gmf_survey_no,c.description as field_type_des, \r\n"
					+ "d.description as validation_method_des\r\n" + "FROM public.nt_t_indicators a \r\n"
					+ "inner join public.nt_m_gami_form b on a.form_id=b.gmf_form_id\r\n"
					+ "inner join nt_r_field_type c on a.field_type = c.code \r\n"
					+ "left outer join nt_r_field_validation d on a.validation_method = d.code \r\n"
					+ "left outer join nt_r_field_definition e on a.field_definition = e.code \r\n" + "WHERE " + where;
			ps = con.prepareStatement(query);

			if (surveyNo) {
				ps.setString(1, generateSurveyFormDTO.getSurveyNo());
			} else {
				ps.setString(1, generateSurveyFormDTO.getFormID());
			}

			rs = ps.executeQuery();
			GenerateSurveyFormDTO p;

			while (rs.next()) {
				p = new GenerateSurveyFormDTO();
				p.setSeqNo(rs.getLong("seqno"));
				p.setFieldName(rs.getString("field_name"));
				p.setFieldName_sinhala(rs.getString("field_name_sinhala"));
				p.setFieldName_tamil(rs.getString("field_name_tamil"));
				p.setFieldType(rs.getString("field_type"));
				p.setFieldType_des(rs.getString("field_type_des"));
				p.setValidationMethod(rs.getString("validation_method"));
				p.setValidationMethod_des(rs.getString("validation_method_des"));
				p.setFieldDefinition(rs.getString("field_definition"));
				p.setFieldDefinition_des(rs.getString("field_definition_des"));
				p.setFieldLength(rs.getInt("field_length"));
				p.setDisplayAfter(rs.getString("display_after"));
				p.setDisplayOrder(rs.getInt("display_order"));

				if (rs.getString("mandatory_field").equals("Y")) {
					p.setMandatoryField(true);
				} else if (rs.getString("mandatory_field").equals("N")) {
					p.setMandatoryField(false);
				}
				if (rs.getString("active").equals("Y")) {
					p.setActive(true);
				} else if (rs.getString("active").equals("N")) {
					p.setActive(false);
				}
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
	public boolean gamiSeriyaUpdateGenerateSurveyForm(GenerateSurveyFormDTO generateSurveyFormDTO,
			List<GenerateSurveyFormDTO> indicators_list, String user) {
		boolean success = false;
		Connection con = null;
		PreparedStatement ps = null, ps2 = null, ps3 = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			// update form details
			String query = "UPDATE public.nt_m_gami_form\r\n"
					+ "SET gmf_temp_id=?, gmf_form_description=?, gmf_header_label=?, gmf_header_label_sinhala=?,\r\n"
					+ "gmf_header_label_tamil=?, gmf_modify_by=?, gmf_modify_date=? WHERE gmf_survey_no =? and gmf_form_id=?";

			ps = con.prepareStatement(query);
			ps.setString(1, generateSurveyFormDTO.getCopyTemplateID());
			ps.setString(2, generateSurveyFormDTO.getFormDescription());
			ps.setString(3, generateSurveyFormDTO.getHeaderLabel());
			ps.setString(4, generateSurveyFormDTO.getHeaderLabel_sinhala());
			ps.setString(5, generateSurveyFormDTO.getHeaderLabel_tamil());
			ps.setString(6, user);
			ps.setTimestamp(7, timestamp);
			ps.setString(8, generateSurveyFormDTO.getSurveyNo());
			ps.setString(9, generateSurveyFormDTO.getFormID());
			ps.executeUpdate();

			// delete indicator process
			List<GenerateSurveyFormDTO> dataIndicatorList = new ArrayList<GenerateSurveyFormDTO>();
			// get indicators from database
			dataIndicatorList = getGamiTblIndicatorsList(generateSurveyFormDTO);

			for (int x = 0; x < dataIndicatorList.size(); x++) {
				Boolean removed = true;
				for (int i = 0; i < indicators_list.size(); i++) {
					if (indicators_list.get(i).getSeqNo() != null) {
						if (dataIndicatorList.get(x).getSeqNo().equals(indicators_list.get(i).getSeqNo())) {
							removed = false;
							break;
						}
					}
				}
				if (removed) {
					// delete removed indicators from database
					String queryDel = "DELETE FROM public.nt_t_indicators WHERE seqno=?;";
					ps3 = con.prepareStatement(queryDel);
					ps3.setLong(1, dataIndicatorList.get(x).getSeqNo());
					ps3.executeUpdate();
					ConnectionManager.close(ps3);
					// delete indicators LOV from database
					String queryDel2 = "DELETE FROM public.nt_t_indicator_lov WHERE indicators_seqno=?;";
					ps3 = con.prepareStatement(queryDel2);
					ps3.setLong(1, dataIndicatorList.get(x).getSeqNo());
					ps3.executeUpdate();
				}
			}

			for (int i = 0; i < indicators_list.size(); i++) {

				// update indicators
				if (indicators_list.get(i).getSeqNo() != null) {
					String query2 = "Update public.nt_t_indicators SET field_name = ?, field_name_sinhala = ?, field_name_tamil = ?, field_type = ?, "
							+ "validation_method = ?, field_length = ?, mandatory_field = ?, "
							+ "active = ?, lov_field = ?, modify_by = ?, modify_date = ?, display_after = ?, field_definition = ?, display_order = ? where seqno = ? ";

					ps = con.prepareStatement(query2);
					ps.setString(1, indicators_list.get(i).getFieldName());
					ps.setString(2, indicators_list.get(i).getFieldName_sinhala());
					ps.setString(3, indicators_list.get(i).getFieldName_tamil());
					ps.setString(4, indicators_list.get(i).getFieldType());
					ps.setString(5, indicators_list.get(i).getValidationMethod());
					ps.setInt(6, indicators_list.get(i).getFieldLength());
					if (indicators_list.get(i).getMandatoryField() == true) {
						ps.setString(7, "Y");
					} else if (indicators_list.get(i).getMandatoryField() == false) {
						ps.setString(7, "N");
					}
					if (indicators_list.get(i).getActive() == true) {
						ps.setString(8, "Y");
					} else if (indicators_list.get(i).getActive() == false) {
						ps.setString(8, "N");
					}
					if (indicators_list.get(i).getFieldType().equals("FT01")) {
						ps.setString(9, "Y");
					} else if (!indicators_list.get(i).getFieldType().equals("FT01")) {
						ps.setString(9, "N");
					}
					ps.setString(10, user);
					ps.setTimestamp(11, timestamp);
					ps.setString(12, indicators_list.get(i).getDisplayAfter());
					ps.setString(13, indicators_list.get(i).getFieldDefinition());
					ps.setInt(14, indicators_list.get(i).getDisplayOrder());
					ps.setLong(15, indicators_list.get(i).getSeqNo());
					ps.executeUpdate();

					List<GenerateSurveyFormDTO> dataIndicatorValuesList = new ArrayList<GenerateSurveyFormDTO>();
					if (indicators_list.get(i).getFieldType().equals("FT01")) {
						// update indicator values function
						// get indicator values from database
						dataIndicatorValuesList = getGamiTblIndicatorsList(indicators_list.get(i));
						for (int x = 0; x < dataIndicatorValuesList.size(); x++) {
							Boolean removed = true;
							for (int y = 0; y < indicators_list.get(i).getIndicator_values_list().size(); y++) {
								if (indicators_list.get(i).getIndicator_values_list().get(y).getLOV_seqNo() != null) {
									if (dataIndicatorValuesList.get(x).getLOV_seqNo().equals(
											indicators_list.get(i).getIndicator_values_list().get(y).getLOV_seqNo())) {
										removed = false;
										break;
									}
								}
							}
							if (removed) {
								// delete removed indicator values from database
								String queryDel = "DELETE FROM public.nt_t_indicator_lov WHERE seqno=?;";
								ps3 = con.prepareStatement(queryDel);
								ps3.setLong(1, dataIndicatorValuesList.get(x).getLOV_seqNo());
								ps3.executeUpdate();
							}
						}

						for (int j = 0; j < indicators_list.get(i).getIndicator_values_list().size(); j++) {
							if (indicators_list.get(i).getIndicator_values_list().get(j).getLOV_seqNo() == null) {

								long seqNo_indicator_values = Utils.getNextValBySeqName(con, "seq_nt_t_indicator_lov");
								String query3 = "INSERT INTO public.nt_t_indicator_lov (seqno, form_id, code, description, description_sinhala, description_tamil, created_by, created_date, indicators_seqno) "
										+ "VALUES (?,?,?,?,?,?,?,?,?);";

								ps = con.prepareStatement(query3);
								ps.setLong(1, seqNo_indicator_values);
								ps.setString(2, generateSurveyFormDTO.getFormID());
								ps.setString(3, indicators_list.get(i).getIndicator_values_list().get(j).getLOV_code());
								ps.setString(4,
										indicators_list.get(i).getIndicator_values_list().get(j).getLOV_description());
								ps.setString(5, indicators_list.get(i).getIndicator_values_list().get(j)
										.getLOV_description_sinhala());
								ps.setString(6, indicators_list.get(i).getIndicator_values_list().get(j)
										.getLOV_description_tamil());
								ps.setString(7, user);
								ps.setTimestamp(8, timestamp);
								ps.setLong(9, indicators_list.get(i).getSeqNo());
								ps.executeUpdate();
							}
						}
					}

				} else {
					// insert new indicators
					long seqNo_indicator = Utils.getNextValBySeqName(con, "seq_nt_t_indicators");
					String query2 = "INSERT INTO public.nt_t_indicators(seqno, form_id, field_name, field_name_sinhala, field_name_tamil, field_type,"
							+ " validation_method, field_length, mandatory_field,"
							+ " active, lov_field, created_by, created_date,display_after,field_definition,display_order) "
							+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

					ps = con.prepareStatement(query2);
					ps.setLong(1, seqNo_indicator);
					ps.setString(2, generateSurveyFormDTO.getFormID());
					ps.setString(3, indicators_list.get(i).getFieldName());
					ps.setString(4, indicators_list.get(i).getFieldName_sinhala());
					ps.setString(5, indicators_list.get(i).getFieldName_tamil());
					ps.setString(6, indicators_list.get(i).getFieldType());
					ps.setString(7, indicators_list.get(i).getValidationMethod());
					ps.setInt(8, indicators_list.get(i).getFieldLength());
					if (indicators_list.get(i).getMandatoryField() == true) {
						ps.setString(9, "Y");
					} else if (indicators_list.get(i).getMandatoryField() == false) {
						ps.setString(9, "N");
					}
					if (indicators_list.get(i).getActive() == true) {
						ps.setString(10, "Y");
					} else if (indicators_list.get(i).getActive() == false) {
						ps.setString(10, "N");
					}
					if (indicators_list.get(i).getFieldType().equals("FT01")) {
						ps.setString(11, "Y");
					} else if (!indicators_list.get(i).getFieldType().equals("FT01")) {
						ps.setString(11, "N");
					}
					ps.setString(12, user);
					ps.setTimestamp(13, timestamp);
					ps.setString(14, indicators_list.get(i).getDisplayAfter());
					ps.setString(15, indicators_list.get(i).getFieldDefinition());
					ps.setInt(16, indicators_list.get(i).getDisplayOrder());
					ps.executeUpdate();

					if (indicators_list.get(i).getFieldType().equals("FT01")) {
						// save indicator values
						for (int j = 0; j < indicators_list.get(i).getIndicator_values_list().size(); j++) {
							long seqNo_indicator_values = Utils.getNextValBySeqName(con, "seq_nt_t_indicator_lov");
							String query3 = "INSERT INTO public.nt_t_indicator_lov (seqno, form_id, code, description, description_sinhala, description_tamil, created_by, created_date, indicators_seqno) "
									+ "VALUES (?,?,?,?,?,?,?,?,?);";

							ps = con.prepareStatement(query3);
							ps.setLong(1, seqNo_indicator_values);
							ps.setString(2, generateSurveyFormDTO.getFormID());
							ps.setString(3, indicators_list.get(i).getIndicator_values_list().get(j).getLOV_code());
							ps.setString(4,
									indicators_list.get(i).getIndicator_values_list().get(j).getLOV_description());
							ps.setString(5, indicators_list.get(i).getIndicator_values_list().get(j)
									.getLOV_description_sinhala());
							ps.setString(6, indicators_list.get(i).getIndicator_values_list().get(j)
									.getLOV_description_tamil());
							ps.setString(7, user);
							ps.setTimestamp(8, timestamp);
							ps.setLong(9, seqNo_indicator);
							ps.executeUpdate();
						}
					}
				}
			}

			con.commit();
			success = true;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(ps3);
			ConnectionManager.close(con);
		}
		return success;
	}

	@Override
	public List<GenerateSurveyFormDTO> getGamiTemplateIdFormList() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<GenerateSurveyFormDTO> formIDList = new ArrayList<GenerateSurveyFormDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select gmf_form_id,gmf_form_description\r\n" + "from public.nt_m_gami_form \r\n"
					+ "order by gmf_form_id desc";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				GenerateSurveyFormDTO data = new GenerateSurveyFormDTO();
				data.setFormID(rs.getString("gmf_form_id"));
				data.setFormDescription(rs.getString("gmf_form_description"));

				formIDList.add(data);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return formIDList;

	}

	@Override
	public List<IndicatorsDTO> tblQuestionsWithAnswers(String formId) {

		List<IndicatorsDTO> indicatorList = new ArrayList<IndicatorsDTO>();

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "select * from public.nt_t_indicators where form_id=? order by display_order";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, formId);
			rs = stmt.executeQuery();

			int count = 0;
			while (rs.next()) {
				count++;
				IndicatorsDTO dto = new IndicatorsDTO();
				dto.setFieldSeqNo(rs.getInt("seqno"));
				dto.setCount(count + " ). ");
				dto.setFieldNameEn(rs.getString("field_name"));
				dto.setFieldNameSin(rs.getString("field_name_sinhala"));
				dto.setFieldNameTam(rs.getString("field_name_tamil"));
				dto.setFieldType(rs.getString("field_type"));
				dto.setFieldLength(rs.getString("field_length"));
				dto.setMandatoryField(rs.getString("mandatory_field"));
				dto.setActive(rs.getString("active"));
				dto.setLovField(rs.getString("lov_field"));

				if (dto.getFieldType() != null && !dto.getFieldType().isEmpty()
						&& !dto.getFieldType().trim().equalsIgnoreCase("")) {
					if (dto.getFieldType().equalsIgnoreCase("FT03")) {// date field
						dto.setDateField(true);
						Date today = new Date();
						DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
						dto.setFieldInputVal(df.format(today));
					} else {
						dto.setDateField(false);
						dto.setFieldInputVal("");
					}
				}

				dto.setFieldSeqNo(rs.getInt("seqno"));
				if (dto.getLovField() != null && !dto.getLovField().isEmpty()
						&& dto.getLovField().equalsIgnoreCase("Y")) {

					List<IndicatorsDTO> indicatorLOVList = new ArrayList<IndicatorsDTO>();

					String sql2 = "select * from public.nt_t_indicator_lov where form_id=? and indicators_seqno=? order by seqno";
					stmt1 = con.prepareStatement(sql2);
					stmt1.setString(1, formId);
					stmt1.setInt(2, dto.getFieldSeqNo());
					rs1 = stmt1.executeQuery();

					while (rs1.next()) {
						IndicatorsDTO lovDto = new IndicatorsDTO();
						lovDto.setLovCode(rs1.getString("code"));
						lovDto.setFieldNameEn(rs1.getString("description"));
						lovDto.setFieldNameSin(rs1.getString("description_sinhala"));
						lovDto.setFieldNameTam(rs1.getString("description_tamil"));

						indicatorLOVList.add(lovDto);
					}

					dto.setIndicatorLOV(indicatorLOVList);
					dto.setLovFieldEnable(true);
				} else {
					dto.setLovFieldEnable(false);
				}

				dto.setDisplayAfter(rs.getString("display_after"));
				dto.setDisplayOrder(rs.getInt("display_order"));
				dto.setFieldIndicatorSeq(rs.getInt("seqno"));

				indicatorList.add(dto);
			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return indicatorList;

	}

	@Override
	public boolean validateGamiSeriyaFormId(String formID) {
		boolean FormID_duplicate = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT gmf_form_id FROM public.nt_m_gami_form WHERE gmf_form_id=? LIMIT 1;";

			ps = con.prepareStatement(query);
			ps.setString(1, formID);
			rs = ps.executeQuery();

			if (rs.next()) {
				FormID_duplicate = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return FormID_duplicate;
	}

	// Generate survey draft form
	@Override
	public List<FormDTO> drpdFormIdList(String taskCode, String taskStatus) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<FormDTO> formIdList = new ArrayList<FormDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct A.gmf_form_id as formId,A.gmf_survey_no as surveyNo \r\n"
					+ "from public.nt_m_gami_form A\r\n"
					+ "inner join public.nt_h_survey_task_his B on A.gmf_survey_no = B.tsd_survey_no\r\n"
					+ "inner join public.nt_m_payment_voucher C on C.pav_application_no = A.gmf_survey_no\r\n"
					+ "where B.tsd_task_code=? and B.tsd_status=? and  A.gmf_form_id is not null and C.pav_application_no is not null\r\n"
					+ "order by  A.gmf_survey_no desc;";
			ps = con.prepareStatement(query);

			ps.setString(1, taskCode);
			ps.setString(2, taskStatus);

			rs = ps.executeQuery();

			while (rs.next()) {
				FormDTO gm = new FormDTO();
				gm.setFormId(rs.getString("formId"));
				gm.setSurveyNo(rs.getString("surveyNo"));
				formIdList.add(gm);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return formIdList;

	}

	// Gami permit holder information
	@Override
	public void test() {
	}

	@Override
	public GamiSeriyaDTO saveGamiServiceInformation(GamiSeriyaDTO gamiSeriyaDTO, String loginUser) {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_gami_permit_hol_service_info");

			String sql = "INSERT INTO public.nt_m_gami_permit_hol_service_info\r\n"
					+ "(gps_seq, gps_tender_ref_no, gps_service_ref_no, gps_permit_no_prpta,\r\n"
					+ "gps_perfered_lan, gps_service_type_code, gps_bus_no, gps_unserved_portion_km, gps_sequence_no, gps_origin, gps_total_length, \r\n"
					+ "gps_destination, gps_agreed_amount, gps_via, gps_service_start_date, gps_nic_no, gps_service_end_date, \r\n"
					+ "gps_operator_name, gps_operator_name_si, gps_operator_name_ta, \r\n"
					+ "gps_add1, gps_add1_si, gps_add1_ta, gps_add2, gps_add2_si, gps_add2_ta, gps_city, gps_city_si, gps_city_ta, \r\n"
					+ "gps_status, gps_province, gps_telephone_no, gps_district_code, gps_mobile_no, gps_dev_sec_code, \r\n"
					+ "gps_created_by, gps_created_date, gps_subsidy_type) \r\n"
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, gamiSeriyaDTO.getTenderRefNo());

			gamiSeriyaDTO.setServiceRefNo(generateGamiSurviceReferenceNo());

			stmt.setString(3, gamiSeriyaDTO.getServiceRefNo());
			stmt.setString(4, gamiSeriyaDTO.getPermitNo());
			stmt.setString(5, gamiSeriyaDTO.getPreferedLanguage());
			stmt.setString(6, gamiSeriyaDTO.getServiceTypeCode());
			stmt.setString(7, gamiSeriyaDTO.getBusNo());
			stmt.setDouble(8, gamiSeriyaDTO.getUnservedPortion());
			stmt.setString(9, gamiSeriyaDTO.getSequenceNo());
			stmt.setString(10, gamiSeriyaDTO.getOriginDesc());

			stmt.setDouble(11, gamiSeriyaDTO.getTotalLength());
			stmt.setString(12, gamiSeriyaDTO.getDestinationDesc());

			if (gamiSeriyaDTO.getAgreedAmount() != null) {
				stmt.setBigDecimal(13, gamiSeriyaDTO.getAgreedAmount());
			} else {
				stmt.setBigDecimal(13, BigDecimal.ZERO);
			}

			stmt.setString(14, gamiSeriyaDTO.getVia());

			Timestamp serStartDatetimestamp = null;
			if (gamiSeriyaDTO.getServiceStartDate() != null) {
				java.util.Date serStartDate = gamiSeriyaDTO.getServiceStartDate();
				serStartDatetimestamp = new Timestamp(serStartDate.getTime());
			}
			stmt.setTimestamp(15, serStartDatetimestamp);

			stmt.setString(16, gamiSeriyaDTO.getIdNo());

			Timestamp serEndDateTimestamp = null;
			if (gamiSeriyaDTO.getServiceEndDate() != null) {
				java.util.Date serEndDate = gamiSeriyaDTO.getServiceEndDate();
				serEndDateTimestamp = new Timestamp(serEndDate.getTime());
			}

			stmt.setTimestamp(17, serEndDateTimestamp);

			stmt.setString(18, gamiSeriyaDTO.getNameinFull());
			stmt.setString(19, gamiSeriyaDTO.getNameinFullSinhala());
			stmt.setString(20, gamiSeriyaDTO.getNameinFullTamil());

			stmt.setString(21, gamiSeriyaDTO.getAddress1());
			stmt.setString(22, gamiSeriyaDTO.getAddress1sinhala());
			stmt.setString(23, gamiSeriyaDTO.getAddress1Tamil());
			stmt.setString(24, gamiSeriyaDTO.getAddress2());
			stmt.setString(25, gamiSeriyaDTO.getAddress2Sinhala());
			stmt.setString(26, gamiSeriyaDTO.getAddress2Tamil());
			stmt.setString(27, gamiSeriyaDTO.getCity());
			stmt.setString(28, gamiSeriyaDTO.getCitySinhala());
			stmt.setString(29, gamiSeriyaDTO.getCityTamil());
			stmt.setString(30, gamiSeriyaDTO.getStatus());

			stmt.setString(31, gamiSeriyaDTO.getProvinceCode());
			stmt.setString(32, gamiSeriyaDTO.getTelephoneNo());
			stmt.setString(33, gamiSeriyaDTO.getDistrictCode());
			stmt.setString(34, gamiSeriyaDTO.getMobileNo());
			stmt.setString(35, gamiSeriyaDTO.getDivisionalSecCode());

			stmt.setString(36, loginUser);
			stmt.setTimestamp(37, timestamp);
			stmt.setString(38, gamiSeriyaDTO.getSubCityType());

			stmt.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			gamiSeriyaDTO.setServiceRefNo(null); // set null to fire error message
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return gamiSeriyaDTO;
	}

	public String generateGamiSurviceReferenceNo() {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;

		int year = Year.now().getValue();
		String currYear = Integer.toString(year).substring(2, 4);
		String strSurviceRefNo = "";
		String lastyr = "00";

		try {
			con = ConnectionManager.getConnection();

			String sql = " select gps_service_ref_no\r\n" + "from public.nt_m_gami_permit_hol_service_info\r\n"
					+ "where gps_service_ref_no is not null order by gps_service_ref_no desc limit 1;";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("gps_service_ref_no");
			}

			if (result != null) {

				lastyr = result.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String ApprecordcountN = "00001";
					strSurviceRefNo = "GRN" + currYear + ApprecordcountN;
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
					strSurviceRefNo = "GRN" + currYear + ApprecordcountN;
				}
			} else
				strSurviceRefNo = "GRN" + currYear + "00001";

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return strSurviceRefNo;
	}

	@Override
	public List<GamiSeriyaDTO> getTblGamiServiceInformation(GamiSeriyaDTO gamiSeriyaDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<GamiSeriyaDTO> retriveTableData = new ArrayList<GamiSeriyaDTO>();

		String where = "gps_tender_ref_no = '" + gamiSeriyaDTO.getTenderRefNo() + "'";

		if (gamiSeriyaDTO.getTenderRefNo() != null && !gamiSeriyaDTO.getTenderRefNo().isEmpty()
				&& !gamiSeriyaDTO.getTenderRefNo().equalsIgnoreCase("")) {

			where = "gps_tender_ref_no = '" + gamiSeriyaDTO.getTenderRefNo() + "'";

		} else if (gamiSeriyaDTO.getServiceRefNo() != null && !gamiSeriyaDTO.getServiceRefNo().isEmpty()
				&& !gamiSeriyaDTO.getServiceRefNo().equalsIgnoreCase("")) {

			where = "gps_service_ref_no = '" + gamiSeriyaDTO.getServiceRefNo() + "'";

		} else if (gamiSeriyaDTO.getPermitNo() != null && !gamiSeriyaDTO.getPermitNo().isEmpty()
				&& !gamiSeriyaDTO.getPermitNo().equalsIgnoreCase("")) {

			where = "gps_permit_no_prpta = '" + gamiSeriyaDTO.getPermitNo() + "'";

		} else if (gamiSeriyaDTO.getServiceNo() != null && !gamiSeriyaDTO.getServiceNo().isEmpty()
				&& !gamiSeriyaDTO.getServiceNo().equalsIgnoreCase("")) {

			where = "gps_service_no = '" + gamiSeriyaDTO.getServiceNo() + "'";

		}

		try {
			con = ConnectionManager.getConnection();

			String query = "select gps_tender_ref_no as tenderRefNo,gps_service_ref_no as serviceRefNo,gps_permit_no_prpta as permitNo,\r\n"
					+ "gps_service_no as serviceNo,gps_operator_name as operatorName,gps_status as status,gps_service_end_date as serviceEndDate,gps_service_start_date as serviceStartDate\r\n"
					+ "from public.nt_m_gami_permit_hol_service_info\r\n" + "where " + where
					+ " order by gps_service_ref_no desc";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {

				gamiSeriyaDTO = new GamiSeriyaDTO();

				gamiSeriyaDTO.setTenderRefNo(rs.getString("tenderRefNo"));
				gamiSeriyaDTO.setServiceRefNo(rs.getString("serviceRefNo"));
				gamiSeriyaDTO.setPermitNo(rs.getString("permitNo"));
				gamiSeriyaDTO.setServiceNo(rs.getString("serviceNo"));

				gamiSeriyaDTO.setNameinFull(rs.getString("operatorName"));

				if (rs.getString("serviceEndDate") != null) {

				}

				if (rs.getTimestamp("serviceEndDate") != null) {
					gamiSeriyaDTO.setStrServiceEndDate(rs.getString("serviceEndDate").substring(0, 10));
				}

				if (rs.getTimestamp("serviceStartDate") != null) {
					gamiSeriyaDTO.setStrServiceEndDate(rs.getString("serviceStartDate").substring(0, 10));
				}

				if (rs.getString("status") != null) {
					if (rs.getString("status").equals("O")) {
						gamiSeriyaDTO.setStatusDesc("ON GOING");
					} else if (rs.getString("status").equals("P")) {
						gamiSeriyaDTO.setStatusDesc("PENDING");
					} else if (rs.getString("status").equals("A")) {
						gamiSeriyaDTO.setStatusDesc("APPROVED");
					} else if (rs.getString("status").equals("R")) {
						gamiSeriyaDTO.setStatusDesc("REJECTED");
					}
					gamiSeriyaDTO.setStatus(rs.getString("status"));
				}

				retriveTableData.add(gamiSeriyaDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return retriveTableData;
	}

	@Override
	public GamiSeriyaDTO viewGamiServiceInformation(GamiSeriyaDTO gamiSeriyaDTO) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String where = "gps_service_ref_no = '" + gamiSeriyaDTO.getServiceRefNo() + "'";

		if (gamiSeriyaDTO.getServiceRefNo() != null && !gamiSeriyaDTO.getServiceRefNo().isEmpty()
				&& !gamiSeriyaDTO.getServiceRefNo().equalsIgnoreCase("")) {

			where = "gps_service_ref_no = '" + gamiSeriyaDTO.getServiceRefNo() + "'";

		} else if (gamiSeriyaDTO.getPermitNo() != null && !gamiSeriyaDTO.getPermitNo().isEmpty()
				&& !gamiSeriyaDTO.getPermitNo().equalsIgnoreCase("")) {

			where = "gps_permit_no_prpta = '" + gamiSeriyaDTO.getPermitNo() + "'";

		} else if (gamiSeriyaDTO.getServiceNo() != null && !gamiSeriyaDTO.getServiceNo().isEmpty()
				&& !gamiSeriyaDTO.getServiceNo().equalsIgnoreCase("")) {

			where = "gps_service_no = '" + gamiSeriyaDTO.getServiceNo() + "'";

		}

		try {
			con = ConnectionManager.getConnection();

			String query = "select *\r\n" + "from public.nt_m_gami_permit_hol_service_info\r\n" + "where " + where;

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				gamiSeriyaDTO.setTenderRefNo(rs.getString("gps_tender_ref_no"));
				gamiSeriyaDTO.setServiceRefNo(rs.getString("gps_service_ref_no"));
				gamiSeriyaDTO.setPermitNo(rs.getString("gps_permit_no_prpta"));
				gamiSeriyaDTO.setPreferedLanguage(rs.getString("gps_perfered_lan"));
				gamiSeriyaDTO.setServiceTypeCode(rs.getString("gps_service_type_code"));
				gamiSeriyaDTO.setBusNo(rs.getString("gps_bus_no"));
				gamiSeriyaDTO.setUnservedPortion(rs.getDouble("gps_unserved_portion_km"));
				gamiSeriyaDTO.setSequenceNo(rs.getString("gps_sequence_no"));
				gamiSeriyaDTO.setOriginDesc(rs.getString("gps_origin"));
				gamiSeriyaDTO.setTotalLength(rs.getDouble("gps_total_length"));
				gamiSeriyaDTO.setDestinationDesc(rs.getString("gps_destination"));

				if (rs.getBigDecimal("gps_agreed_amount") != null) {
					gamiSeriyaDTO.setAgreedAmount(rs.getBigDecimal("gps_agreed_amount"));
				} else {
					gamiSeriyaDTO.setAgreedAmount(BigDecimal.ZERO);
				}

				gamiSeriyaDTO.setVia(rs.getString("gps_via"));

				if (rs.getTimestamp("gps_service_start_date") != null) {
					gamiSeriyaDTO.setServiceStartDate(new Date(rs.getTimestamp("gps_service_start_date").getTime()));
				}

				gamiSeriyaDTO.setIdNo(rs.getString("gps_nic_no"));

				if (rs.getTimestamp("gps_service_end_date") != null) {
					gamiSeriyaDTO.setServiceEndDate(new Date(rs.getTimestamp("gps_service_end_date").getTime()));
				}

				gamiSeriyaDTO.setNameinFull(rs.getString("gps_operator_name"));
				gamiSeriyaDTO.setNameinFullSinhala(rs.getString("gps_operator_name_si"));
				gamiSeriyaDTO.setNameinFullTamil(rs.getString("gps_operator_name_ta"));

				gamiSeriyaDTO.setAddress1(rs.getString("gps_add1"));
				gamiSeriyaDTO.setAddress1sinhala(rs.getString("gps_add1_si"));
				gamiSeriyaDTO.setAddress1Tamil(rs.getString("gps_add1_ta"));

				gamiSeriyaDTO.setAddress2(rs.getString("gps_add2"));
				gamiSeriyaDTO.setAddress2Sinhala(rs.getString("gps_add2_si"));
				gamiSeriyaDTO.setAddress2Tamil(rs.getString("gps_add2_ta"));
				gamiSeriyaDTO.setCity(rs.getString("gps_city"));
				gamiSeriyaDTO.setCitySinhala(rs.getString("gps_city_si"));
				gamiSeriyaDTO.setCityTamil(rs.getString("gps_city_ta"));

				gamiSeriyaDTO.setStatus(rs.getString("gps_status"));
				gamiSeriyaDTO.setProvinceCode(rs.getString("gps_province"));
				gamiSeriyaDTO.setTelephoneNo(rs.getString("gps_telephone_no"));
				gamiSeriyaDTO.setDistrictCode(rs.getString("gps_district_code"));
				gamiSeriyaDTO.setMobileNo(rs.getString("gps_mobile_no"));
				gamiSeriyaDTO.setDivisionalSecCode(rs.getString("gps_dev_sec_code"));
				gamiSeriyaDTO.setDivisionalSecDesc(rs.getString("gps_dev_sec_code"));
				gamiSeriyaDTO.setSubCityType(rs.getString("gps_subsidy_type"));

				gamiSeriyaDTO.setAccountNo(rs.getString("gps_account_no"));
				gamiSeriyaDTO.setBankNameCode(rs.getString("gps_bank_name_code"));
				gamiSeriyaDTO.setBankBranchNameCode(rs.getString("gps_branch_code"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);

		}

		return gamiSeriyaDTO;

	}

	@Override
	public boolean updateGamiServiceInformation(GamiSeriyaDTO gamiSeriyaDTO, String loginUser) {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		boolean isModelSave = false;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		String where = "gps_service_ref_no = '" + gamiSeriyaDTO.getServiceRefNo() + "'";

		if (gamiSeriyaDTO.getServiceRefNo() != null && !gamiSeriyaDTO.getServiceRefNo().isEmpty()
				&& !gamiSeriyaDTO.getServiceRefNo().equalsIgnoreCase("")) {

			where = "gps_service_ref_no = '" + gamiSeriyaDTO.getServiceRefNo() + "'";

		} else if (gamiSeriyaDTO.getPermitNo() != null && !gamiSeriyaDTO.getPermitNo().isEmpty()
				&& !gamiSeriyaDTO.getPermitNo().equalsIgnoreCase("")) {

			where = "gps_permit_no_prpta = '" + gamiSeriyaDTO.getPermitNo() + "'";

		} else if (gamiSeriyaDTO.getServiceNo() != null && !gamiSeriyaDTO.getServiceNo().isEmpty()
				&& !gamiSeriyaDTO.getServiceNo().equalsIgnoreCase("")) {

			where = "gps_service_no = '" + gamiSeriyaDTO.getServiceNo() + "'";

		}

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_gami_permit_hol_service_info\r\n"
					+ "SET  gps_permit_no_prpta=?, gps_perfered_lan=?, gps_service_type_code=?,\r\n"
					+ "gps_bus_no=?, gps_unserved_portion_km=?, gps_sequence_no=?, gps_origin=?, gps_total_length=?, gps_destination=?,  \r\n"
					+ "gps_agreed_amount=?, gps_via=?, gps_service_start_date=?, gps_nic_no=?, gps_service_end_date=?,     \r\n"
					+ "gps_operator_name=?, gps_operator_name_si=?, gps_operator_name_ta=?,  \r\n"
					+ "gps_add1=?, gps_add1_si=?, gps_add1_ta=?, gps_add2=?, gps_add2_si=?, gps_add2_ta=?, \r\n"
					+ "gps_city=?, gps_city_si=?, gps_city_ta=?, gps_status=?, gps_province=?, gps_telephone_no=?, \r\n"
					+ "gps_district_code=?, gps_mobile_no=?, gps_dev_sec_code=?, gps_modified_by=?, gps_modified_date=?, \r\n"
					+ "gps_subsidy_type=? " + "where " + where;

			stmt = con.prepareStatement(sql);

			stmt.setString(1, gamiSeriyaDTO.getPermitNo());
			stmt.setString(2, gamiSeriyaDTO.getPreferedLanguage());
			stmt.setString(3, gamiSeriyaDTO.getServiceTypeCode());
			stmt.setString(4, gamiSeriyaDTO.getBusNo());
			stmt.setDouble(5, gamiSeriyaDTO.getUnservedPortion());
			stmt.setString(6, gamiSeriyaDTO.getSequenceNo());
			stmt.setString(7, gamiSeriyaDTO.getOriginCode());

			stmt.setDouble(8, gamiSeriyaDTO.getTotalLength());
			stmt.setString(9, gamiSeriyaDTO.getDestinationCode());

			if (gamiSeriyaDTO.getAgreedAmount() != null) {
				stmt.setBigDecimal(10, gamiSeriyaDTO.getAgreedAmount());
			} else {
				stmt.setBigDecimal(10, BigDecimal.ZERO);
			}

			stmt.setString(11, gamiSeriyaDTO.getVia());

			Timestamp serStartDatetimestamp = null;
			if (gamiSeriyaDTO.getServiceStartDate() != null) {
				java.util.Date serStartDate = gamiSeriyaDTO.getServiceStartDate();
				serStartDatetimestamp = new Timestamp(serStartDate.getTime());
			}
			stmt.setTimestamp(12, serStartDatetimestamp);

			stmt.setString(13, gamiSeriyaDTO.getIdNo());

			Timestamp serEndDateTimestamp = null;
			if (gamiSeriyaDTO.getServiceEndDate() != null) {
				java.util.Date serEndDate = gamiSeriyaDTO.getServiceEndDate();
				serEndDateTimestamp = new Timestamp(serEndDate.getTime());
			}

			stmt.setTimestamp(14, serEndDateTimestamp);

			stmt.setString(15, gamiSeriyaDTO.getNameinFull());
			stmt.setString(16, gamiSeriyaDTO.getNameinFullSinhala());
			stmt.setString(17, gamiSeriyaDTO.getNameinFullTamil());

			stmt.setString(18, gamiSeriyaDTO.getAddress1());
			stmt.setString(19, gamiSeriyaDTO.getAddress1sinhala());
			stmt.setString(20, gamiSeriyaDTO.getAddress1Tamil());
			stmt.setString(21, gamiSeriyaDTO.getAddress2());
			stmt.setString(22, gamiSeriyaDTO.getAddress2Sinhala());
			stmt.setString(23, gamiSeriyaDTO.getAddress2Tamil());
			stmt.setString(24, gamiSeriyaDTO.getCity());
			stmt.setString(25, gamiSeriyaDTO.getCitySinhala());
			stmt.setString(26, gamiSeriyaDTO.getCityTamil());
			stmt.setString(27, gamiSeriyaDTO.getStatus());

			stmt.setString(28, gamiSeriyaDTO.getProvinceCode());
			stmt.setString(29, gamiSeriyaDTO.getTelephoneNo());
			stmt.setString(30, gamiSeriyaDTO.getDistrictCode());
			stmt.setString(31, gamiSeriyaDTO.getMobileNo());
			stmt.setString(32, gamiSeriyaDTO.getDivisionalSecCode());

			stmt.setString(33, loginUser);
			stmt.setTimestamp(34, timestamp);
			stmt.setString(35, gamiSeriyaDTO.getSubCityType());

			int i = stmt.executeUpdate();

			if (i > 0) {
				isModelSave = true;
			} else {
				isModelSave = false;
			}
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return isModelSave;
	}

	@Override
	public boolean updateBankInformation(GamiSeriyaDTO gamiSeriyaDTO, String loginUser) {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		boolean isModelSave = false;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		String where = "gps_service_ref_no = '" + gamiSeriyaDTO.getServiceRefNo() + "'";

		if (gamiSeriyaDTO.getServiceRefNo() != null && !gamiSeriyaDTO.getServiceRefNo().isEmpty()
				&& !gamiSeriyaDTO.getServiceRefNo().equalsIgnoreCase("")) {

			where = "gps_service_ref_no = '" + gamiSeriyaDTO.getServiceRefNo() + "'";

		} else if (gamiSeriyaDTO.getPermitNo() != null && !gamiSeriyaDTO.getPermitNo().isEmpty()
				&& !gamiSeriyaDTO.getPermitNo().equalsIgnoreCase("")) {

			where = "gps_permit_no_prpta = '" + gamiSeriyaDTO.getPermitNo() + "'";

		} else if (gamiSeriyaDTO.getServiceNo() != null && !gamiSeriyaDTO.getServiceNo().isEmpty()
				&& !gamiSeriyaDTO.getServiceNo().equalsIgnoreCase("")) {

			where = "gps_service_no = '" + gamiSeriyaDTO.getServiceNo() + "'";

		}

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_gami_permit_hol_service_info\r\n"
					+ "SET  gps_account_no=?,gps_bank_name_code=?,gps_branch_code=?,gps_modified_by=?, gps_modified_date=?\r\n"
					+ "WHERE " + where;

			stmt = con.prepareStatement(sql);

			stmt.setString(1, gamiSeriyaDTO.getAccountNo());
			stmt.setString(2, gamiSeriyaDTO.getBankNameCode());
			stmt.setString(3, gamiSeriyaDTO.getBankBranchNameCode());
			stmt.setString(4, loginUser);
			stmt.setTimestamp(5, timestamp);

			int i = stmt.executeUpdate();

			if (i > 0) {
				isModelSave = true;
			} else {
				isModelSave = false;
			}
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return isModelSave;
	}

	@Override
	public List<GamiSeriyaDTO> getDrpdTenderReferenceNoList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<GamiSeriyaDTO> dataList = new ArrayList<GamiSeriyaDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct gtm_tender_reference_no\r\n" + "from public.nt_m_gami_tender_management\r\n"
					+ "where gtm_tender_reference_no is not null and gtm_status='O'\r\n"
					+ "order by gtm_tender_reference_no desc ;";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				GamiSeriyaDTO gisuSariyaDTO = new GamiSeriyaDTO();
				gisuSariyaDTO.setTenderRefNo(rs.getString("gtm_tender_reference_no"));

				dataList.add(gisuSariyaDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return dataList;
	}

	@Override
	public List<GamiSeriyaDTO> getDrpdServiceReferenceNoList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<GamiSeriyaDTO> dataList = new ArrayList<GamiSeriyaDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct gps_service_ref_no\r\n"
					+ "from public.nt_m_gami_permit_hol_service_info\r\n" + "where gps_service_ref_no is not null \r\n"
					+ "order by gps_service_ref_no desc;";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				GamiSeriyaDTO gisuSariyaDTO = new GamiSeriyaDTO();
				gisuSariyaDTO.setServiceRefNo(rs.getString("gps_service_ref_no"));

				dataList.add(gisuSariyaDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return dataList;
	}

	@Override
	public List<GamiSeriyaDTO> getDrpdPermitNoList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<GamiSeriyaDTO> dataList = new ArrayList<GamiSeriyaDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct gps_permit_no_prpta\r\n"
					+ "from public.nt_m_gami_permit_hol_service_info\r\n" + "where gps_permit_no_prpta is not null \r\n"
					+ "order by gps_permit_no_prpta desc;";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				GamiSeriyaDTO gisuSariyaDTO = new GamiSeriyaDTO();
				gisuSariyaDTO.setPermitNo(rs.getString("gps_permit_no_prpta"));

				dataList.add(gisuSariyaDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return dataList;
	}

	@Override
	public List<GamiSeriyaDTO> getDrpdServiceNoList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<GamiSeriyaDTO> dataList = new ArrayList<GamiSeriyaDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct gps_service_no\r\n" + "from public.nt_m_gami_permit_hol_service_info\r\n"
					+ "where gps_service_no is not null \r\n" + "order by gps_service_no desc ;";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				GamiSeriyaDTO gisuSariyaDTO = new GamiSeriyaDTO();
				gisuSariyaDTO.setServiceNo(rs.getString("gps_service_no"));

				dataList.add(gisuSariyaDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return dataList;
	}

	// gami create tender
	@Override
	public GamiRouteDTO onTraficProposalChange(TenderDTO tenderDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		GamiRouteDTO infoDTO = new GamiRouteDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "select A.gtp_traffic_proposal_no as trafficPropsalNo,A.gtp_survey_no as surveyNo,\r\n"
					+ "B.gsi_survey_request_no as surveyRequestNo,\r\n"
					+ "C.gtm_tender_reference_no as tenderRefereceNo\r\n"
					+ "from public.nt_t_gami_traffic_proposal A\r\n"
					+ "inner join public.nt_m_gamisari_survey_initiate B on B.gsi_survey_no = A.gtp_survey_no\r\n"
					+ "left outer join public.nt_m_gami_tender_management C on C.gtm_traffic_proposal_no = A.gtp_traffic_proposal_no\r\n"
					+ "where A.gtp_traffic_proposal_no = ?;";

			ps = con.prepareStatement(query);
			ps.setString(1, tenderDTO.getTrafficProposalNo());
			rs = ps.executeQuery();

			while (rs.next()) {

				infoDTO.setTrafficProposalNo(rs.getString("trafficPropsalNo"));
				infoDTO.setSurveyNo(rs.getString("surveyNo"));
				infoDTO.setSurveyReqNo(rs.getString("surveyRequestNo"));
				infoDTO.setTenderReferenceNo(rs.getString("tenderRefereceNo"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return infoDTO;
	}

	@Override
	public List<TenderDTO> getDrpdTrafficProposalList(String taskCode, String taskStatus, String status) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<TenderDTO> list = new ArrayList<TenderDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select A.gtp_traffic_proposal_no as trafficProposalNo\r\n"
					+ "from nt_t_gami_traffic_proposal A \r\n"
					+ "inner join public.nt_m_gamisari_survey_initiate B on B.gsi_survey_no = A.gtp_survey_no\r\n"
					+ "where A.gtp_trafic_proposal_status=? and B.gsi_is_tender_process_require = 'Y' and B.gsi_is_board_approve='A' and gtp_traffic_proposal_no is not null\r\n"
					+ "order by A.gtp_traffic_proposal_no desc;";
			ps = con.prepareStatement(query);

			ps.setString(1, status);
			rs = ps.executeQuery();

			while (rs.next()) {
				TenderDTO gm = new TenderDTO();
				gm.setTrafficProposalNo(rs.getString("trafficProposalNo"));
				list.add(gm);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return list;

	}

	@Override
	public List<GamiRouteDTO> getTblTrafficProposalRouteInfo(TenderDTO tenderDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<GamiRouteDTO> list = new ArrayList<GamiRouteDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select *\r\n" + "from public.nt_t_gami_survey_route_det\r\n"
					+ "where gsr_traffic_pro_no = ? and gsr_is_traffic_proposal_selected='Y' order by gsr_sequence_no desc ;";
			ps = con.prepareStatement(query);

			ps.setString(1, tenderDTO.getTrafficProposalNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				GamiRouteDTO dataDto = new GamiRouteDTO();
				dataDto.setSeqNo(rs.getInt("gsr_seq_no"));
				dataDto.setOriginDesc(rs.getString("gsr_origin"));
				dataDto.setDestinationDesc(rs.getString("gsr_destination"));
				dataDto.setViaDesc(rs.getString("gsr_via"));
				dataDto.setProvinceDesc(rs.getString("gsr_province"));
				dataDto.setTotalLength(rs.getDouble("gsr_total_length"));
				dataDto.setUnservedPortion(rs.getDouble("gsr_unserved_portion"));
				dataDto.setNoOfPermitsRequired(rs.getInt("gsr_no_of_permits_to_issue"));
				dataDto.setSequenceNo(rs.getString("gsr_sequence_no"));

				list.add(dataDto);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return list;
	}

	@Override
	public boolean saveSelectedRouteInfo(GamiRouteDTO gamiRouteDTO, String loginUser) {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean isModelSave = false;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			if (gamiRouteDTO.getTrafficProposalNo() != null && !gamiRouteDTO.getTrafficProposalNo().isEmpty()
					&& !gamiRouteDTO.getTrafficProposalNo().equals("")) {

				String query = "UPDATE public.nt_t_gami_survey_route_det\r\n"
						+ "SET gsr_traffic_pro_no=?,gsr_is_tender_selected=?,gsr_no_of_permits_to_issue=?,gsr_modify_by=?, gsr_modify_date=?,gsr_is_traffic_proposal_selected=?\r\n"
						+ "WHERE gsr_seq_no=?;";

				ps = con.prepareStatement(query);

				ps.setString(1, gamiRouteDTO.getTrafficProposalNo());
				ps.setString(2, gamiRouteDTO.getIsTenderSelected());
				ps.setInt(3, gamiRouteDTO.getNoOfPermitsRequired());
				ps.setString(4, loginUser);
				ps.setTimestamp(5, timestamp);
				ps.setString(6, gamiRouteDTO.getIsTrfficProposalSelected());
				ps.setInt(7, gamiRouteDTO.getSeqNo());
			} else {

				String query = "UPDATE public.nt_t_gami_survey_route_det\r\n"
						+ "SET gsr_is_tender_selected=?,gsr_no_of_permits_to_issue=?,gsr_modify_by=?, gsr_modify_date=?,gsr_is_traffic_proposal_selected=?\r\n"
						+ "WHERE gsr_seq_no=?;";

				ps = con.prepareStatement(query);

				ps.setString(1, gamiRouteDTO.getIsTenderSelected());
				ps.setInt(2, gamiRouteDTO.getNoOfPermitsRequired());
				ps.setString(3, loginUser);
				ps.setTimestamp(4, timestamp);
				ps.setString(5, gamiRouteDTO.getIsTrfficProposalSelected());
				ps.setInt(6, gamiRouteDTO.getSeqNo());
			}

			int i = ps.executeUpdate();

			if (i > 0) {
				isModelSave = true;
			} else {
				isModelSave = false;
			}
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);

		}

		return isModelSave;
	}

	@Override
	public List<GamiRouteDTO> getTblTenderSelectedRouteInfo(GamiRouteDTO gamiRouteDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<GamiRouteDTO> list = new ArrayList<GamiRouteDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select *\r\n" + "from public.nt_t_gami_survey_route_det\r\n"
					+ "where gsr_traffic_pro_no = ? and gsr_is_tender_selected='Y';";
			ps = con.prepareStatement(query);

			ps.setString(1, gamiRouteDTO.getTrafficProposalNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				GamiRouteDTO dataDto = new GamiRouteDTO();
				dataDto.setSeqNo(rs.getInt("gsr_seq_no"));
				dataDto.setOriginDesc(rs.getString("gsr_origin"));
				dataDto.setDestinationDesc(rs.getString("gsr_destination"));
				dataDto.setViaDesc(rs.getString("gsr_via"));
				dataDto.setProvinceDesc(rs.getString("gsr_province"));
				dataDto.setTotalLength(rs.getDouble("gsr_total_length"));
				dataDto.setUnservedPortion(rs.getDouble("gsr_unserved_portion"));
				dataDto.setNoOfPermitsRequired(rs.getInt("gsr_no_of_permits_to_issue"));
				dataDto.setSequenceNo(rs.getString("gsr_sequence_no"));
				dataDto.setIsTrfficProposalSelected(rs.getString("gsr_is_traffic_proposal_selected"));
				dataDto.setIsTenderSelected(rs.getString("gsr_is_tender_selected"));
				list.add(dataDto);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return list;
	}

	public String generateGamiTenderReferenceNo() {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;

		int year = Year.now().getValue();
		String currYear = Integer.toString(year).substring(2, 4);
		String strGamiTenderReferenceNo = "";
		String lastyr = "00";

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT gtm_tender_reference_no\r\n" + "FROM public.nt_m_gami_tender_management\r\n"
					+ "WHERE gtm_tender_reference_no IS NOT NULL ORDER BY gtm_tender_reference_no desc LIMIT 1";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("gtm_tender_reference_no");
			}

			if (result != null) {

				lastyr = result.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String ApprecordcountN = "00001";
					strGamiTenderReferenceNo = "GTR" + currYear + ApprecordcountN;
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
					strGamiTenderReferenceNo = "GTR" + currYear + ApprecordcountN;
				}
			} else
				strGamiTenderReferenceNo = "GTR" + currYear + "00001";

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return strGamiTenderReferenceNo;

	}

	@Override
	public TenderDTO saveCreateTenderInfo(TenderDTO tenderDTO, String loginUser) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_gami_permit_hol_service_info");

			String sql = "INSERT INTO public.nt_m_gami_tender_management\r\n"
					+ "(seqno, gtm_traffic_proposal_no, gtm_tender_reference_no ,gtm_description, gtm_closing_date, gtm_time, gtm_status, gtm_created_by, gtm_created_date)\r\n"
					+ "VALUES(?,?,?,?,?,?,?,?,?);";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, tenderDTO.getTrafficProposalNo());
			tenderDTO.setTenderRefNo(generateGamiTenderReferenceNo());
			stmt.setString(3, tenderDTO.getTenderRefNo());
			stmt.setString(4, tenderDTO.getTenderDes());

			Timestamp closingDatetimestamp = null;
			if (tenderDTO.getTenderClosinDate() != null) {
				java.util.Date closingDate = tenderDTO.getTenderClosinDate();
				closingDatetimestamp = new Timestamp(closingDate.getTime());
			}
			stmt.setTimestamp(5, closingDatetimestamp);

			Timestamp closingTimetimestamp = null;
			if (tenderDTO.getTenderClosinTime() != null) {
				java.util.Date closingTime = tenderDTO.getTenderClosinTime();
				closingTimetimestamp = new Timestamp(closingTime.getTime());
			}
			stmt.setTimestamp(6, closingTimetimestamp);

			stmt.setString(7, tenderDTO.getTenderStatus());
			stmt.setString(8, loginUser);
			stmt.setTimestamp(9, timestamp);

			stmt.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return tenderDTO;

	}

	@Override
	public TenderDTO getGamiTenderDetails(TenderDTO tenderDTO) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select *\r\n" + "from public.nt_m_gami_tender_management \r\n"
					+ "where gtm_traffic_proposal_no= ?";

			ps = con.prepareStatement(query);
			ps.setString(1, tenderDTO.getTrafficProposalNo());
			rs = ps.executeQuery();

			while (rs.next()) {

				tenderDTO.setTenderRefNo(rs.getString("gtm_tender_reference_no"));
				Date closingDate = null;
				if (rs.getTimestamp("gtm_closing_date") != null) {
					String strDate = rs.getString("gtm_closing_date").substring(0, 10);
					DateFormat formatter = new SimpleDateFormat("yyyy-MM-DD");
					closingDate = (Date) formatter.parse(strDate);
				}
				tenderDTO.setTenderClosinDate(closingDate);

				String strClosingTime = rs.getString("gtm_time").substring(10);
				Date closingTime = null;
				if (rs.getString("gtm_time") != null) {
					DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
					closingTime = (Date) timeFormat.parse(strClosingTime);
				}
				tenderDTO.setTenderClosinTime(closingTime);
				tenderDTO.setTenderDes(rs.getString("gtm_description"));

				tenderDTO.setTenderRefNo(rs.getString("gtm_tender_reference_no"));
				tenderDTO.setTenderRefNo(rs.getString("gtm_tender_reference_no"));
				tenderDTO.setTenderRefNo(rs.getString("gtm_tender_reference_no"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return tenderDTO;
	}

	@Override
	public boolean updateCreateTenderInfo(TenderDTO tenderDTO, String loginUser) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		boolean isModelSave = false;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_gami_tender_management\r\n"
					+ "SET gtm_description=?, gtm_closing_date=?, gtm_time=?, \r\n"
					+ "gtm_modify_by=?, gtm_modify_date=?\r\n" + "WHERE gtm_tender_reference_no=?;";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, tenderDTO.getTenderDes());

			Timestamp closingDatetimestamp = null;
			if (tenderDTO.getTenderClosinDate() != null) {
				java.util.Date closingDate = tenderDTO.getTenderClosinDate();
				closingDatetimestamp = new Timestamp(closingDate.getTime());
			}
			stmt.setTimestamp(2, closingDatetimestamp);

			Timestamp closingTimetimestamp = null;
			if (tenderDTO.getTenderClosinTime() != null) {
				java.util.Date closingTime = tenderDTO.getTenderClosinTime();
				closingTimetimestamp = new Timestamp(closingTime.getTime());
			}
			stmt.setTimestamp(3, closingTimetimestamp);

			stmt.setString(4, loginUser);
			stmt.setTimestamp(5, timestamp);
			stmt.setString(6, tenderDTO.getTenderRefNo());

			int i = stmt.executeUpdate();

			if (i > 0) {
				isModelSave = true;
			} else {
				isModelSave = false;
			}
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return isModelSave;
	}

	@Override
	public List<GamiSeriyaDTO> gamiServiceAuthorization(List<GamiSeriyaDTO> selectedTenderApplications,
			String loginUser) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			for (GamiSeriyaDTO gamiSeriyaDTO2 : selectedTenderApplications) {

				String sql = "UPDATE public.nt_m_gami_permit_hol_service_info\r\n"
						+ "SET gps_status=?,gps_reject_reason=?,gps_approve_reject_by=?,gps_approve_reject_date=?,gps_service_no=?\r\n"
						+ "WHERE gps_service_ref_no=?;";

				stmt = con.prepareStatement(sql);

				stmt.setString(1, gamiSeriyaDTO2.getStatus());
				stmt.setString(2, gamiSeriyaDTO2.getAprroveRejectMark());
				stmt.setString(3, loginUser);
				stmt.setTimestamp(4, timestamp);
				String serviceNo = generateGamiServiceNo();
				gamiSeriyaDTO2.setServiceNo(serviceNo);
				stmt.setString(5, serviceNo);
				stmt.setString(6, gamiSeriyaDTO2.getServiceRefNo());

				stmt.executeUpdate();

				con.commit();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return selectedTenderApplications;

	}

	public String generateGamiServiceNo() {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;

		int year = Year.now().getValue();
		String currYear = Integer.toString(year).substring(2, 4);
		String strGamiServiceNo = "";
		String lastyr = "00";

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT gps_service_no\r\n" + "FROM public.nt_m_gami_permit_hol_service_info\r\n"
					+ "WHERE gps_service_no IS NOT NULL ORDER BY gps_service_no desc LIMIT 1";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("gps_service_no");
			}

			if (result != null) {

				lastyr = result.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String ApprecordcountN = "00001";
					strGamiServiceNo = "GSE" + currYear + ApprecordcountN;
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
					strGamiServiceNo = "GSE" + currYear + ApprecordcountN;
				}
			} else
				strGamiServiceNo = "GSE" + currYear + "00001";

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return strGamiServiceNo;

	}

	@Override
	public boolean getTenderStatus(TrafficProposalDTO trafficProposalDTO) {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		boolean isModelSave = false;

		try {
			con = ConnectionManager.getConnection();

			String sql = "select gtm_status\r\n" + "from public.nt_m_gami_tender_management\r\n"
					+ "where gtm_traffic_proposal_no=? and gtm_status=? and gtm_status is not null;";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, trafficProposalDTO.getTrafficProposalNo());
			stmt.setString(2, trafficProposalDTO.getStatus());

			rs = stmt.executeQuery();

			while (rs.next()) {
				isModelSave = true;
			}
			con.commit();

		} catch (Exception ex) {
			isModelSave = false;
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return isModelSave;
	}

	// verify survey information
	@Override
	public List<SurveyDTO> drpdSurveyNoListForVerifySurveyInfo(String taskCode, String taskStaus, String status) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SurveyDTO> surveyNoList = new ArrayList<SurveyDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct A.gsi_survey_no\r\n" + "from public.nt_m_gamisari_survey_initiate A\r\n"
					+ "inner join public.nt_h_survey_task_his B on B.tsd_survey_no = A.gsi_survey_no\r\n"
					+ "where A.gsi_status=? and A.gsi_survey_no is not null and B.tsd_task_code=? and B.tsd_status=?\r\n"
					+ "order by A.gsi_survey_no desc;";
			ps = con.prepareStatement(query);

			ps.setString(1, status);
			ps.setString(2, taskCode);
			ps.setString(3, taskStaus);
			rs = ps.executeQuery();

			while (rs.next()) {
				SurveyDTO gisuSariyaDTO = new SurveyDTO();
				gisuSariyaDTO.setSurveyNo(rs.getString("gsi_survey_no"));
				surveyNoList.add(gisuSariyaDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return surveyNoList;
	}

	@Override

	public SurveyDTO getGamiVerifySurveyInfo(SurveyDTO surveyDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select gsi_survey_request_no,gsi_survey_no,gsi_survey_type,gsi_survey_method,\r\n"
					+ "gsi_verify_survey_remarks,gsi_verify_survey_special_remarks,gsi_is_tender_process_require,gsi_verify_survey_status\r\n"
					+ "from nt_m_gamisari_survey_initiate\r\n" + "where gsi_survey_no = ?";

			ps = con.prepareStatement(query);
			ps.setString(1, surveyDTO.getSurveyNo());
			rs = ps.executeQuery();

			while (rs.next()) {

				surveyDTO.setRequestNo(rs.getString("gsi_survey_request_no"));
				surveyDTO.setSurveyTypeDescription(rs.getString("gsi_survey_type"));
				surveyDTO.setSurveyMethodDescription(rs.getString("gsi_survey_method"));
				surveyDTO.setRemarks(rs.getString("gsi_verify_survey_remarks"));
				surveyDTO.setSpecialRemarks(rs.getString("gsi_verify_survey_special_remarks"));

				String tenderReq = rs.getString("gsi_is_tender_process_require");
				if (tenderReq != null && tenderReq.equals("Y")) {
					surveyDTO.setTenderRequire("true");
				} else {
					surveyDTO.setTenderRequire("false");
				}

				surveyDTO.setStatus(rs.getString("gsi_verify_survey_status"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return surveyDTO;
	}

	@Override
	public GamiRouteDTO saveTblGamiRouteInfoRaw(GamiRouteDTO gamiRouteDTO, String loginUser) {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_m_gami_survey_route_det");

			String sql = "INSERT INTO public.nt_t_gami_survey_route_det\r\n"
					+ "(gsr_seq_no, gsr_survey_req_no, gsr_origin, \r\n" + "gsr_destination,  \r\n"
					+ "gsr_via, gsr_province, gsr_total_length, gsr_unserved_portion, \r\n"
					+ "gsr_no_of_permits_to_issue, gsr_created_by, gsr_created_date,gsr_sequence_no)\r\n"
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?,?);";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, gamiRouteDTO.getSurveyReqNo());
			stmt.setString(3, gamiRouteDTO.getOriginDesc());
			stmt.setString(4, gamiRouteDTO.getDestinationDesc());
			stmt.setString(5, gamiRouteDTO.getViaDesc());
			stmt.setString(6, gamiRouteDTO.getProvinceDesc());
			stmt.setDouble(7, gamiRouteDTO.getTotalLength());
			stmt.setDouble(8, gamiRouteDTO.getUnservedPortion());
			stmt.setDouble(9, gamiRouteDTO.getNoOfPermitsRequired());
			stmt.setString(10, loginUser);
			stmt.setTimestamp(11, timestamp);

			gamiRouteDTO.setSequenceNo(generateGamiRouteSequenceNo());
			stmt.setString(12, gamiRouteDTO.getSequenceNo());

			int i = stmt.executeUpdate();

			if (i > 0) {
			} else {
				gamiRouteDTO.setSequenceNo(null);
			}
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return gamiRouteDTO;
	}

	public String generateGamiRouteSequenceNo() {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;

		int year = Year.now().getValue();
		String currYear = Integer.toString(year).substring(2, 4);
		String strGamiServiceNo = "";
		String lastyr = "00";

		try {
			con = ConnectionManager.getConnection();

			String sql = "	SELECT gsr_sequence_no\r\n" + "	FROM nt_t_gami_survey_route_det\r\n"
					+ "	WHERE gsr_sequence_no IS NOT NULL \r\n" + "	ORDER BY gsr_sequence_no desc LIMIT 1";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("gsr_sequence_no");
			}

			if (result != null) {

				lastyr = result.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String ApprecordcountN = "00001";
					strGamiServiceNo = "SEQ" + currYear + ApprecordcountN;
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
					strGamiServiceNo = "SEQ" + currYear + ApprecordcountN;
				}
			} else
				strGamiServiceNo = "SEQ" + currYear + "00001";

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return strGamiServiceNo;

	}

	@Override
	public List<GamiRouteDTO> getTblGamiRouteInfo(SurveyDTO surveyDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<GamiRouteDTO> list = new ArrayList<GamiRouteDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "	select *\r\n" + "	from public.nt_t_gami_survey_route_det\r\n"
					+ "    where gsr_survey_req_no = ? ;";
			ps = con.prepareStatement(query);

			ps.setString(1, surveyDTO.getRequestNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				GamiRouteDTO dataDto = new GamiRouteDTO();
				dataDto.setSeqNo(rs.getInt("gsr_seq_no"));
				dataDto.setSequenceNo((rs.getString("gsr_sequence_no")));
				dataDto.setOriginDesc(rs.getString("gsr_origin"));
				dataDto.setDestinationDesc(rs.getString("gsr_destination"));
				dataDto.setViaDesc(rs.getString("gsr_via"));
				dataDto.setProvinceDesc(rs.getString("gsr_province"));
				dataDto.setTotalLength(rs.getDouble("gsr_total_length"));
				dataDto.setUnservedPortion(rs.getDouble("gsr_unserved_portion"));
				dataDto.setNoOfPermitsRequired(rs.getInt("gsr_no_of_permits_to_issue"));
				list.add(dataDto);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return list;
	}

	@Override
	public boolean updateGamiVerifyInfo(SurveyDTO surveyDTO, String loginUser) {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean isModelSave = false;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_m_gamisari_survey_initiate\r\n"
					+ "set gsi_verify_survey_remarks=? ,gsi_verify_survey_special_remarks=?,\r\n"
					+ "gsi_verify_survey_status=?,gsi_is_tender_process_require=?,gsi_survey_verified_by=?,gsi_survey_verified_date=?\r\n"
					+ "WHERE gsi_survey_request_no=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, surveyDTO.getRemarks());
			ps.setString(2, surveyDTO.getSpecialRemarks());
			ps.setString(3, surveyDTO.getStatus());

			if (surveyDTO.getTenderRequire() != null && surveyDTO.getTenderRequire().equals("true")) {
				surveyDTO.setTenderRequire("Y");
			} else if (surveyDTO.getTenderRequire() != null && surveyDTO.getTenderRequire().equals("false")) {
				surveyDTO.setTenderRequire("N");
			} else {
				surveyDTO.setTenderRequire("");
			}
			ps.setString(4, surveyDTO.getTenderRequire());

			ps.setString(5, loginUser);

			if (loginUser.equals("")) {
				ps.setTimestamp(6, null);
			} else {
				ps.setTimestamp(6, timestamp);
			}

			ps.setString(7, surveyDTO.getRequestNo());

			int i = ps.executeUpdate();

			if (i > 0) {
				isModelSave = true;
			} else {
				isModelSave = false;
			}
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);

		}

		return isModelSave;
	}

	@Override
	public boolean UpdateTblGamiRouteInfoRaw(GamiRouteDTO gamiRouteDTO, String loginUser) {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		boolean isModelSave = false;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_t_gami_survey_route_det\r\n"
					+ "SET gsr_origin=?, gsr_destination=?, gsr_via=?, \r\n"
					+ "gsr_province=?, gsr_total_length=?, gsr_unserved_portion=?, gsr_no_of_permits_to_issue=?, \r\n"
					+ "gsr_modify_by=?, gsr_modify_date=? \r\n" + "WHERE gsr_sequence_no=?;";

			stmt = con.prepareStatement(query);

			stmt.setString(1, gamiRouteDTO.getOriginDesc());
			stmt.setString(2, gamiRouteDTO.getDestinationDesc());
			stmt.setString(3, gamiRouteDTO.getViaDesc());
			stmt.setString(4, gamiRouteDTO.getProvinceDesc());
			stmt.setDouble(5, gamiRouteDTO.getTotalLength());
			stmt.setDouble(6, gamiRouteDTO.getUnservedPortion());
			stmt.setDouble(7, gamiRouteDTO.getNoOfPermitsRequired());
			stmt.setString(8, loginUser);
			stmt.setTimestamp(9, timestamp);
			stmt.setString(10, gamiRouteDTO.getSequenceNo());

			int i = stmt.executeUpdate();

			if (i > 0) {
				isModelSave = true;
			} else {
				isModelSave = false;
			}
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return isModelSave;
	}

	@Override
	public boolean deleteTblGamiRouteInfoRaw(GamiRouteDTO gamiRouteDTO, String loginUser) {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		boolean isModelSave = false;

		try {
			con = ConnectionManager.getConnection();

			String query = "	DELETE FROM public.nt_t_gami_survey_route_det\r\n" + "	WHERE gsr_sequence_no =?;";

			stmt = con.prepareStatement(query);

			stmt.setString(1, gamiRouteDTO.getSequenceNo());
			int i = stmt.executeUpdate();

			if (i > 0) {
				isModelSave = true;
			} else {
				isModelSave = false;
			}
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return isModelSave;
	}

	/** Generate traffic proposal number */
	@Override
	public TrafficProposalDTO saveTrafficProposalInfo(TrafficProposalDTO trafficProposalDTO, String loginUser) {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_m_gami_traffic_proposal");

			String sql = "INSERT INTO public.nt_t_gami_traffic_proposal\r\n"
					+ "(gtp_seqno, gtp_traffic_proposal_no, gtp_survey_no, gtp_trafic_pro_special_remarks, \r\n"
					+ " gtp_trafic_pro_special_print_note, gtp_trafic_pro_suggestions, gtp_created_by, gtp_created_date, \r\n"
					+ " gtp_trafic_proposal_status)\r\n" + "VALUES(?,?,?,?,?,?,?,?,?);";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);

			String trfficProposalNo = generateGamiTrafficProposalNo();
			trafficProposalDTO.setTrafficProposalNo(trfficProposalNo);
			stmt.setString(2, trfficProposalNo);
			stmt.setString(3, trafficProposalDTO.getSurveyNo());
			stmt.setString(4, trafficProposalDTO.getSpecialRemark());
			stmt.setString(5, trafficProposalDTO.getSpecialPrintNote());
			stmt.setString(6, trafficProposalDTO.getSuggestions());
			stmt.setString(7, loginUser);
			stmt.setTimestamp(8, timestamp);
			stmt.setString(9, "O");

			int i = stmt.executeUpdate();

			if (i > 0) {
			} else {
				trafficProposalDTO.setTrafficProposalNo(null);
			}
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return trafficProposalDTO;
	}

	public String generateGamiTrafficProposalNo() {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;

		int year = Year.now().getValue();
		String currYear = Integer.toString(year).substring(2, 4);
		String strGamiServiceNo = "";
		String lastyr = "00";

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT gtp_traffic_proposal_no\r\n" + "FROM public.nt_t_gami_traffic_proposal\r\n"
					+ "WHERE gtp_traffic_proposal_no IS NOT NULL ORDER BY gtp_traffic_proposal_no desc LIMIT 1";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("gtp_traffic_proposal_no");
			}

			if (result != null) {

				lastyr = result.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String ApprecordcountN = "00001";
					strGamiServiceNo = "GTP" + currYear + ApprecordcountN;
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
					strGamiServiceNo = "GTP" + currYear + ApprecordcountN;
				}
			} else
				strGamiServiceNo = "GTP" + currYear + "00001";

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return strGamiServiceNo;

	}

	@Override
	public TrafficProposalDTO getGamiTrafficProposalInfo(TrafficProposalDTO trafficProposalDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT A.gtp_seqno as seqNo, A.gtp_traffic_proposal_no as trafficProposalNo, A.gtp_survey_no as surveyNo, \r\n"
					+ "A.gtp_trafic_pro_special_remarks as specialRemarks, A.gtp_trafic_pro_special_print_note as printNote, \r\n"
					+ "A.gtp_trafic_pro_suggestions as suggetions, A.gtp_trafic_proposal_status as status,\r\n"
					+ "B.gsi_survey_type as surveyType ,B.gsi_survey_method as surveyMethod,B.gsi_survey_request_no as surveyRequestNo\r\n"
					+ "FROM public.nt_t_gami_traffic_proposal A \r\n"
					+ "right outer join public.nt_m_gamisari_survey_initiate B on A.gtp_survey_no =B.gsi_survey_no\r\n"
					+ "where B.gsi_survey_no = ?;";

			ps = con.prepareStatement(query);
			ps.setString(1, trafficProposalDTO.getSurveyNo());
			rs = ps.executeQuery();

			while (rs.next()) {

				trafficProposalDTO.setRequestNo(rs.getString("surveyRequestNo"));
				trafficProposalDTO.setSurveyTypeDes(rs.getString("surveyType"));
				trafficProposalDTO.setSurveyMethodDes(rs.getString("surveyMethod"));
				trafficProposalDTO.setTrafficProposalNo(rs.getString("trafficProposalNo"));
				trafficProposalDTO.setSpecialRemark(rs.getString("specialRemarks"));
				trafficProposalDTO.setSpecialPrintNote(rs.getString("printNote"));
				trafficProposalDTO.setSuggestions(rs.getString("suggetions"));
				trafficProposalDTO.setStatus(rs.getString("status"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return trafficProposalDTO;
	}

	@Override
	public boolean updateTrafficProposalInfo(TrafficProposalDTO trafficProposalDTO, String loginUser) {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		boolean isModelSave = false;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_t_gami_traffic_proposal\r\n"
					+ "SET gtp_trafic_pro_special_remarks=?, gtp_trafic_pro_special_print_note=?, gtp_trafic_pro_suggestions=?, \r\n"
					+ "gtp_modify_by=?, gtp_modify_date=?\r\n" + "WHERE gtp_traffic_proposal_no=?;";

			stmt = con.prepareStatement(query);

			stmt.setString(1, trafficProposalDTO.getSpecialRemark());
			stmt.setString(2, trafficProposalDTO.getSpecialPrintNote());
			stmt.setString(3, trafficProposalDTO.getSuggestions());
			stmt.setString(4, loginUser);
			stmt.setTimestamp(5, timestamp);
			stmt.setString(6, trafficProposalDTO.getTrafficProposalNo());

			int i = stmt.executeUpdate();

			if (i > 0) {
				isModelSave = true;
			} else {
				isModelSave = false;
			}
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return isModelSave;
	}

	@Override
	public boolean addTblTrafficProSelectedRouteRaw(GamiRouteDTO tblRouteInfo, String loginUser) {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		boolean isModelSave = false;

		try {
			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_t_gami_traffic_proposal\r\n"
					+ "SET gtp_trafic_pro_special_remarks=?, gtp_trafic_pro_special_print_note=?, gtp_trafic_pro_suggestions=?, \r\n"
					+ "gtp_modify_by=?, gtp_modify_date=?\r\n" + "WHERE gtp_traffic_proposal_no=?;";

			stmt = con.prepareStatement(query);

			int i = stmt.executeUpdate();

			if (i > 0) {
				isModelSave = true;
			} else {
				isModelSave = false;
			}
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return isModelSave;
	}

	@Override
	public List<GamiRouteDTO> getTblTrafficProposalSelectedRouteInfo(GamiRouteDTO gamiRouteDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<GamiRouteDTO> list = new ArrayList<GamiRouteDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select *\r\n" + "from public.nt_t_gami_survey_route_det\r\n"
					+ "where gsr_survey_req_no = ? and gsr_is_traffic_proposal_selected='Y';";
			ps = con.prepareStatement(query);

			ps.setString(1, gamiRouteDTO.getSurveyReqNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				GamiRouteDTO dataDto = new GamiRouteDTO();
				dataDto.setSequenceNo(rs.getString("gsr_sequence_no"));
				dataDto.setSeqNo(rs.getInt("gsr_seq_no"));
				dataDto.setOriginDesc(rs.getString("gsr_origin"));
				dataDto.setDestinationDesc(rs.getString("gsr_destination"));
				dataDto.setViaDesc(rs.getString("gsr_via"));
				dataDto.setProvinceDesc(rs.getString("gsr_province"));
				dataDto.setTotalLength(rs.getDouble("gsr_total_length"));
				dataDto.setUnservedPortion(rs.getDouble("gsr_unserved_portion"));
				dataDto.setNoOfPermitsRequired(rs.getInt("gsr_no_of_permits_to_issue"));
				dataDto.setIsTrfficProposalSelected(rs.getString("gsr_is_traffic_proposal_selected"));
				dataDto.setTrafficProposalNo(rs.getString("gsr_traffic_pro_no"));
				list.add(dataDto);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return list;
	}

	@Override
	public List<SurveyDTO> drpdSurveyNoListForTrafficProposal() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SurveyDTO> surveyNoList = new ArrayList<SurveyDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select A.gsi_survey_no as surveyNo,A.gsi_survey_request_no as requestNo\r\n"
					+ "from nt_m_gamisari_survey_initiate A \r\n" + "where gsi_verify_survey_status='A' \r\n"
					+ "order by  A.gsi_survey_no desc";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				SurveyDTO surveyDTO = new SurveyDTO();
				surveyDTO.setSurveyNo(rs.getString("surveyNo"));

				surveyNoList.add(surveyDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return surveyNoList;
	}

	@Override
	public boolean updateTrafficProposalNo(List<GamiRouteDTO> tblTrafficProposalSelectedRouteInfo,
			String trafficProposalNo) {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		boolean isModelSave = false;

		try {
			con = ConnectionManager.getConnection();

			for (int i = 0; i < tblTrafficProposalSelectedRouteInfo.size(); i++) {

				if ((tblTrafficProposalSelectedRouteInfo.get(i).getSequenceNo() != null
						&& !tblTrafficProposalSelectedRouteInfo.get(i).getSequenceNo().isEmpty()
						&& !tblTrafficProposalSelectedRouteInfo.get(i).getSequenceNo().equals(""))) {

					String query = "UPDATE public.nt_t_gami_survey_route_det\r\n" + "SET gsr_traffic_pro_no=?\r\n"
							+ "WHERE gsr_sequence_no=?;;";

					stmt = con.prepareStatement(query);

					stmt.setString(1, trafficProposalNo);
					stmt.setString(2, tblTrafficProposalSelectedRouteInfo.get(i).getSequenceNo());

					int j = stmt.executeUpdate();

					if (j > 0) {
						isModelSave = true;
					} else {
						isModelSave = false;
						break;
					}

				}

			}

			if (isModelSave) {
				con.commit();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return isModelSave;
	}

	/** request for gamiseriya */
	@Override
	public GamiSeriyaDTO getRequesterInfoByNicNo(GamiSeriyaDTO gamiDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select gri_id_no,gri_tel_no,gri_mobile_no,gri_email,gri_full_name,gri_full_name_si,gri_full_name_ta,\r\n"
					+ "gri_add1,gri_add1_si,gri_add1_ta,gri_add2,gri_add2_si,gri_add2_ta,gri_city,gri_city_si,gri_city_ta\r\n"
					+ "from public.nt_m_gami_requestor_info\r\n" + "where gri_id_no=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, gamiDTO.getIdNo());
			rs = ps.executeQuery();

			while (rs.next()) {

				gamiDTO.setIdNo(rs.getString("gri_id_no"));
				gamiDTO.setTelephoneNo(rs.getString("gri_tel_no"));
				gamiDTO.setMobileNo(rs.getString("gri_mobile_no"));
				gamiDTO.setEmail(rs.getString("gri_email"));
				gamiDTO.setNameinFull(rs.getString("gri_full_name"));
				gamiDTO.setNameinFullSinhala(rs.getString("gri_full_name_si"));
				gamiDTO.setNameinFullTamil(rs.getString("gri_full_name_ta"));

				gamiDTO.setAddress1(rs.getString("gri_add1"));
				gamiDTO.setAddress1sinhala(rs.getString("gri_add1_si"));
				gamiDTO.setAddress1Tamil(rs.getString("gri_add1_ta"));

				gamiDTO.setAddress2(rs.getString("gri_add2"));
				gamiDTO.setAddress2Sinhala(rs.getString("gri_add2_si"));
				gamiDTO.setAddress2Tamil(rs.getString("gri_add2_ta"));

				gamiDTO.setCity(rs.getString("gri_city"));
				gamiDTO.setCitySinhala(rs.getString("gri_city_si"));
				gamiDTO.setCityTamil(rs.getString("gri_city_ta"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return gamiDTO;
	}

	@Override
	public boolean insertGamiRouteHistory(GamiRouteDTO gamiRouteDTO, String loginUser) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean modelSaved = false;

		try {
			con = ConnectionManager.getConnection();

			java.util.Date date = new java.util.Date();
			Timestamp timestamp = new Timestamp(date.getTime());

			long seqNo = Utils.getNextValBySeqName(con, " public.seq_nt_h_gami_survey_route_det");

			String sql = "INSERT INTO public.nt_h_gami_survey_route_det\r\n"
					+ "(gsr_seq_no, gsr_survey_req_no, gsr_traffic_pro_no, gsr_origin, gsr_destination, gsr_via, gsr_province,"
					+ " gsr_total_length, gsr_unserved_portion, gsr_no_of_permits_to_issue, gsr_is_traffic_proposal_selected, "
					+ "gsr_is_tender_selected,gsr_sequence_no, gsr_modify_by, gsr_modify_date)\r\n"
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, gamiRouteDTO.getSurveyReqNo());
			stmt.setString(3, gamiRouteDTO.getTrafficProposalNo());
			stmt.setString(4, gamiRouteDTO.getOriginDesc());
			stmt.setString(5, gamiRouteDTO.getDestinationDesc());
			stmt.setString(6, gamiRouteDTO.getViaDesc());
			stmt.setString(7, gamiRouteDTO.getProvinceDesc());
			stmt.setDouble(8, gamiRouteDTO.getTotalLength());
			stmt.setDouble(9, gamiRouteDTO.getUnservedPortion());
			stmt.setInt(10, gamiRouteDTO.getNoOfPermitsRequired());
			stmt.setString(11, gamiRouteDTO.getIsTrfficProposalSelected());
			stmt.setString(12, gamiRouteDTO.getIsTenderSelected());
			stmt.setString(13, gamiRouteDTO.getSequenceNo());
			stmt.setString(14, loginUser);
			stmt.setTimestamp(15, timestamp);

			int i = stmt.executeUpdate();

			if (i > 0) {
				modelSaved = true;
			} else {
				modelSaved = false;
			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return modelSaved;
	}

	@Override
	public List<GamiRouteDTO> getTblTrafficProposalRouteInfoForTender(TenderDTO tenderDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<GamiRouteDTO> list = new ArrayList<GamiRouteDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select A.gtp_survey_no as surveyNo,A.gtp_traffic_proposal_no as trfficProposalNo,A.gtp_trafic_proposal_status as trfficProposalStatus,\r\n"
					+ "B.gsi_survey_request_no as surveyRequestNo,\r\n" + "c.*\r\n"
					+ "from nt_t_gami_traffic_proposal A\r\n"
					+ "inner join nt_m_gamisari_survey_initiate B on B.gsi_survey_no = A.gtp_survey_no\r\n"
					+ "inner join public.nt_t_gami_survey_route_det C on C.gsr_survey_req_no = B.gsi_survey_request_no\r\n"
					+ "where A.gtp_traffic_proposal_no=? and gtp_trafic_proposal_status='O' and C.gsr_sequence_no is not null\r\n"
					+ "order by C.gsr_sequence_no desc;";
			ps = con.prepareStatement(query);

			ps.setString(1, tenderDTO.getTrafficProposalNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				GamiRouteDTO dataDto = new GamiRouteDTO();
				dataDto.setSeqNo(rs.getInt("gsr_seq_no"));
				dataDto.setOriginDesc(rs.getString("gsr_origin"));
				dataDto.setDestinationDesc(rs.getString("gsr_destination"));
				dataDto.setViaDesc(rs.getString("gsr_via"));
				dataDto.setProvinceDesc(rs.getString("gsr_province"));
				dataDto.setTotalLength(rs.getDouble("gsr_total_length"));
				dataDto.setUnservedPortion(rs.getDouble("gsr_unserved_portion"));
				dataDto.setNoOfPermitsRequired(rs.getInt("gsr_no_of_permits_to_issue"));
				dataDto.setSequenceNo(rs.getString("gsr_sequence_no"));

				list.add(dataDto);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return list;
	}

	@Override
	public List<GamiSeriyaDTO> drpdOriginListForPermotHolderInfo(GamiSeriyaDTO gamiSeriyaDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<GamiSeriyaDTO> noList = new ArrayList<GamiSeriyaDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select A.gtm_tender_reference_no as tenderRefNo,A.gtm_traffic_proposal_no as trafficPropsalNo,\r\n"
					+ "B.*\r\n" + "from nt_m_gami_tender_management A\r\n"
					+ "inner join public.nt_t_gami_survey_route_det B on B.gsr_traffic_pro_no = A.gtm_traffic_proposal_no\r\n"
					+ "where A.gtm_tender_reference_no=? and gsr_is_tender_selected = 'Y'\r\n"
					+ "order by B.gsr_sequence_no";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				GamiSeriyaDTO tempt = new GamiSeriyaDTO();
				tempt.setOriginDesc(rs.getString("gsr_origin"));
				tempt.setSequenceNo(rs.getString("gsr_sequence_no"));
				noList.add(tempt);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return noList;
	}

	@Override
	public List<GamiRouteDTO> drpdSequenceNoListForPermotHolderInfo(GamiSeriyaDTO gamiSeriyaDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<GamiRouteDTO> noList = new ArrayList<GamiRouteDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select A.gtm_tender_reference_no as tenderRefNo,A.gtm_traffic_proposal_no as trafficPropsalNo,\r\n"
					+ "B.gsr_sequence_no as sequenceNo\r\n" + "from nt_m_gami_tender_management A\r\n"
					+ "inner join public.nt_t_gami_survey_route_det B on B.gsr_traffic_pro_no = A.gtm_traffic_proposal_no\r\n"
					+ "where A.gtm_tender_reference_no=? and gsr_is_tender_selected = 'Y'\r\n"
					+ "order by B.gsr_sequence_no";
			ps = con.prepareStatement(query);

			ps.setString(1, gamiSeriyaDTO.getTenderRefNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				GamiRouteDTO tempt = new GamiRouteDTO();
				tempt.setSequenceNo(rs.getString("sequenceNo"));
				tempt.setTrafficProposalNo(rs.getString("trafficPropsalNo"));
				noList.add(tempt);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return noList;
	}

	@Override
	public GamiSeriyaDTO getOriginDetails(GamiSeriyaDTO gamiSeriyaDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GamiRouteDTO getGamiRouteDetailsBySequenceNo(GamiSeriyaDTO gamiSeriyaDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		GamiRouteDTO route = new GamiRouteDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "select gsr_sequence_no,gsr_origin,gsr_via,gsr_destination,gsr_unserved_portion,gsr_total_length\r\n"
					+ "from public.nt_t_gami_survey_route_det\r\n" + "where gsr_sequence_no=?";

			ps = con.prepareStatement(query);
			ps.setString(1, gamiSeriyaDTO.getSequenceNo());
			rs = ps.executeQuery();

			while (rs.next()) {

				route.setSequenceNo(rs.getString("gsr_sequence_no"));
				route.setOriginDesc(rs.getString("gsr_origin"));
				route.setDestinationDesc(rs.getString("gsr_destination"));
				route.setViaDesc(rs.getString("gsr_via"));
				route.setUnservedPortion(rs.getDouble("gsr_unserved_portion"));
				route.setTotalLength(rs.getDouble("gsr_total_length"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return route;
	}

	@Override
	public List<GamiAnalyzedDataDTO> getGamiAnalyzedData(FormDTO formDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		PreparedStatement ps1 = null;
		ResultSet rs1 = null;

		List<GamiAnalyzedDataDTO> mainList = new ArrayList<GamiAnalyzedDataDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct A.question_no as question_no ,D.field_name as questionDesk\r\n"
					+ "from public.nt_t_gami_form_answers A\r\n"
					+ "inner join public.nt_t_gami_form_details B on B.survey_form_no = A.survey_form_no\r\n"
					+ "inner join public.nt_t_indicators D on D.seqno = A.question_seq_no\r\n"
					+ "where B.form_id=? and question_no is not null\r\n" + "order by A.question_no";
			ps = con.prepareStatement(query);

			ps.setString(1, formDTO.getFormId());
			rs = ps.executeQuery();

			while (rs.next()) {
				GamiAnalyzedDataDTO innerList = new GamiAnalyzedDataDTO();
				innerList.setQuetionNumber(rs.getString("question_no") + " ). ");
				innerList.setQuestionDescription(rs.getString("questionDesk"));
				/*String query1 = "select A.question_no as qnumber,A.answer as answerCode,C.description as answerDescription,count (A.question_no) as answerCount,A.fieldtype\r\n"
						+ "from public.nt_t_gami_form_answers A\r\n"
						+ "inner join public.nt_t_gami_form_details B on B.survey_form_no = A.survey_form_no\r\n"
						+ "inner join public.nt_t_indicator_lov C on C.code = A.answer\r\n"
						+ "where  A.question_no=? and A.fieldtype='FT01' and B.form_id=? and  A.answer!=''\r\n"
						+ "group by A.question_no,A.answer,A.fieldtype,C.description\r\n"
						+ "order by A.question_no,A.answer";*/
				
				
				String query1 = "select  A.question_no as qnumber,A.answer as answerCode,C.description as answerDescription, count(A.question_no) as answerCount,A.fieldtype from nt_t_gami_form_answers A \r\n" + 
						"inner join public.nt_t_gami_form_details B on B.survey_form_no = A.survey_form_no\r\n" + 
						"inner join public.nt_t_indicator_lov C on C.code = A.answer\r\n" + 
						"inner join public.nt_t_gami_form_details D on D.survey_form_no = A.survey_form_no \r\n" + 
						"where  A.question_no=? and A.fieldtype='FT01' and  A.answer!='' and B.form_id=?\r\n" + 
						"and C.form_id =D.form_id\r\n" + 
						"group by A.question_no,A.answer,A.fieldtype,C.description\r\n" + 
						"order by A.question_no,A.answer";
				ps1 = con.prepareStatement(query1);

				ps1.setString(1, rs.getString("question_no"));
				ps1.setString(2, formDTO.getFormId());
				rs1 = ps1.executeQuery();

				boolean hasLovFields = false;
				while (rs1.next()) {
					hasLovFields = true;
					GamiAnalyzedDataDTO answerList = new GamiAnalyzedDataDTO();
					answerList.setAnswer(rs1.getString("answerCode"));
					answerList.setAnswerCount(rs1.getInt("answerCount"));
					answerList.setAnswerDescription(rs1.getString("answerDescription"));
					innerList.getAnswerList().add(answerList);

				}

				if (hasLovFields) {
					mainList.add(innerList);
				}

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return mainList;
	}

	@Override
	public String savaGamiFormDetails(MidPointSurveyDTO midPointSurveyDTO, String loginUser) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		String surveyFormNo = null;

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_t_gami_form_details");

			String sql = "INSERT INTO public.nt_t_gami_form_details\r\n"
					+ "(seqno, survey_form_no, form_id, direction_from, direction_to, name_of_recorder, location, survey_date, survey_time, remarks, modify_by, modify_date)\r\n"
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?);";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);

			surveyFormNo = generateGamiSurveyFormNo();
			stmt.setString(2, surveyFormNo);
			stmt.setString(3, midPointSurveyDTO.getFormId());
			stmt.setString(4, midPointSurveyDTO.getDirectinFrom());
			stmt.setString(5, midPointSurveyDTO.getDirectionTo());
			stmt.setString(6, midPointSurveyDTO.getNameOfRecorder());
			stmt.setString(7, midPointSurveyDTO.getLocation());

			Timestamp surveyDate = new Timestamp(date.getTime());
			Timestamp surveyTime = new Timestamp(date.getTime());

			stmt.setTimestamp(8, surveyDate);
			stmt.setTimestamp(9, surveyTime);
			stmt.setString(10, midPointSurveyDTO.getRemarks());
			stmt.setString(11, loginUser);
			stmt.setTimestamp(12, timestamp);

			int i = stmt.executeUpdate();

			if (i > 0) {
			} else {
				surveyFormNo = null;
			}
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return surveyFormNo;

	}

	public String generateGamiSurveyFormNo() {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;

		int year = Year.now().getValue();
		String currYear = Integer.toString(year).substring(2, 4);
		String strGamiServiceNo = "";
		String lastyr = "00";

		try {
			con = ConnectionManager.getConnection();

			String sql = "	SELECT survey_form_no\r\n" + "	FROM public.nt_t_gami_form_details\r\n"
					+ "	WHERE survey_form_no IS NOT NULL ORDER BY survey_form_no desc LIMIT 1";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("survey_form_no");
			}

			if (result != null) {

				lastyr = result.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String ApprecordcountN = "00001";
					strGamiServiceNo = "GFN" + currYear + ApprecordcountN;
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
					strGamiServiceNo = "GFN" + currYear + ApprecordcountN;
				}
			} else
				strGamiServiceNo = "GFN" + currYear + "00001";

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return strGamiServiceNo;

	}

	@Override
	public boolean saveGamiFormAnswers(List<IndicatorsDTO> tableIndicatorList, MidPointSurveyDTO midPointSurveyDTO,
			String loginUser) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		boolean isModelSave = false;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();
			for (int i = 0; i < tableIndicatorList.size(); i++) {

				long seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_t_gami_form_answers");

				IndicatorsDTO questionDetails = tableIndicatorList.get(i);

				if (questionDetails.getCount() != null && !questionDetails.getCount().isEmpty()
						&& !questionDetails.getCount().equals("")) {

					String query = "INSERT INTO public.nt_t_gami_form_answers\r\n"
							+ "(seqno, survey_form_no, question_no, answer, fieldtype, created_by, created_date,question_seq_no)\r\n"
							+ "VALUES(?,?,?,?,?,?,?,?);";

					stmt = con.prepareStatement(query);

					stmt.setLong(1, seqNo);
					stmt.setString(2, midPointSurveyDTO.getFormId());
					stmt.setString(3, questionDetails.getCount().substring(0, 1));
					stmt.setString(4, questionDetails.getFieldInputVal());
					stmt.setString(5, questionDetails.getFieldType());
					stmt.setString(6, loginUser);
					stmt.setTimestamp(7, timestamp);
					stmt.setInt(8, questionDetails.getFieldSeqNo());

					int j = stmt.executeUpdate();

					if (j > 0) {
						isModelSave = true;
					} else {
						isModelSave = false;
						break;
					}

				}

			}
			if (isModelSave) {
				con.commit();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			isModelSave = false;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return isModelSave;

	}

	@Override
	public int getGamiNumberOfFormApplications(FormDTO formDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int numberOfApplications = 0;

		try {
			con = ConnectionManager.getConnection();

			String query = "select count(survey_form_no) as numberOfApplications \r\n"
					+ "from public.nt_t_gami_form_details\r\n" + "where form_id=?";

			ps = con.prepareStatement(query);
			ps.setString(1, formDTO.getFormId());
			rs = ps.executeQuery();

			while (rs.next()) {
				numberOfApplications = rs.getInt("numberOfApplications");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return numberOfApplications;
	}

	public String getGamiRequestNo(String surveyNo) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT gsi_request_no FROM public.nt_m_gamisari_survey_initiate where gsi_survey_no='"
					+ surveyNo + "'";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("gsi_request_no");
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

	public String getGamiSurveyRequestNo(String surveyNo) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT gsi_survey_request_no FROM public.nt_m_gamisari_survey_initiate where gsi_survey_no='"
					+ surveyNo + "'";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("gsi_survey_request_no");
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

	@Override
	public void updateNumberGeneration(String code, String appNo, String loginUser) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql3 = "UPDATE public.nt_r_number_generation SET app_no=?,modified_by=?,modified_date=? WHERE code=? ";

			ps = con.prepareStatement(sql3);

			ps.setString(1, appNo);
			ps.setString(2, loginUser);
			ps.setTimestamp(3, timestamp);
			ps.setString(4, code);

			ps.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

	}

}
