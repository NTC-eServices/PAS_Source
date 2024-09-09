package lk.informatics.ntc.model.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import lk.informatics.ntc.model.dto.GamiSeriyaDTO;
import lk.informatics.ntc.model.dto.LogSheetDTO;
import lk.informatics.ntc.model.dto.LogSheetMaintenanceDTO;
import lk.informatics.ntc.model.dto.NisiSeriyaDTO;
import lk.informatics.ntc.model.dto.SisuSeriyaDTO;
import lk.informatics.ntc.model.dto.SubSidyDTO;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.ntc.view.util.SpringApplicationContex;
import lk.informatics.roc.utils.common.Utils;

public class SisuSariyaServiceImpl implements SisuSariyaService {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private CommonService commonService;
	public static Logger logger = Logger.getLogger("SisuSariyaServiceImpl");

	// get request type for request for sisusariya page
	@Override
	public List<SisuSeriyaDTO> getRequestorTypeForDropDown() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SisuSeriyaDTO> requstTypeList = new ArrayList<SisuSeriyaDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select code,description from  public.nt_r_request_types;";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				SisuSeriyaDTO sisuSariyaDTO = new SisuSeriyaDTO();
				sisuSariyaDTO.setRequestorTypeCode(rs.getString("code"));
				sisuSariyaDTO.setRequestorTypeDes(rs.getString("description"));

				requstTypeList.add(sisuSariyaDTO);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return requstTypeList;
	}

	// get Prefer languages for Request for Sisusariya page
	@Override
	public List<SisuSeriyaDTO> getPrefLanguForDropDown() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SisuSeriyaDTO> prefLanguList = new ArrayList<SisuSeriyaDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select code,description from public.nt_r_language;";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				SisuSeriyaDTO sisuSariyaDTO = new SisuSeriyaDTO();
				sisuSariyaDTO.setLanguageCode(rs.getString("code"));
				sisuSariyaDTO.setLanguageDes(rs.getString("description"));

				prefLanguList.add(sisuSariyaDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return prefLanguList;
	}

	// save data from Request Sisusariya page
	@Override
	public boolean saveRequestSisusariData(SisuSeriyaDTO ssDTO, String loginUser) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String showRequestNo = null;
		boolean success = true;

		try {
			con = ConnectionManager.getConnection();

			if (ssDTO.getRequestNo() != null && !ssDTO.getRequestNo().isEmpty()) {
				showRequestNo = ssDTO.getRequestNo();

			} else {
				showRequestNo = generateSisiRequestNo();
				updateNumberGeneration("SSR", showRequestNo, loginUser);
				ssDTO.setRequestNo(showRequestNo);
			}

			java.util.Date date = new java.util.Date();
			Timestamp timestamp = new Timestamp(date.getTime());
			java.util.Date reqDate;
			reqDate = ssDTO.getRequestDate();
			Timestamp ReqDatetimestamp = new Timestamp(reqDate.getTime());
			long seqNo = Utils.getNextValBySeqName(con, " public.seq_nt_m_sisu_requestor_info");

			String sql = "INSERT INTO public.nt_m_sisu_requestor_info"
					+ "(sri_seq,sri_request_no,sri_request_date,sri_requestor_type,sri_id_no,sri_full_name,sri_full_name_si,sri_full_name_ta,sri_add1,"
					+ "sri_add1_si,sri_add1_ta,sri_add2,sri_add2_si,sri_add2_ta,sri_city,sri_city_si,sri_city_ta,sri_mobile_no"
					+ ",sri_tel_no,sri_email,sri_school_name,sri_school_name_si,sri_school_name_ta,sri_school_add1,sri_school_add1_si,sri_school_add1_ta"
					+ ",sri_school_add2,sri_school_add2_si,sri_school_add2_ta,sri_school_city,sri_school_city_si,sri_school_city_ta,sri_school_province"
					+ ",sri_school_district,sri_school_div_sec,sri_school_tel_no,sri_school_mobile,sri_school_email,sri_created_by,sri_created_date,sri_status,sri_language)"
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?,?,?, ?, ?, ?, ?, ?, ?, ?, ?,?,?, ?, ?, ?, ?, ?, ?, ?, ?,?,?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?)  ;";

			stmt = con.prepareStatement(sql);

			try {
				stmt.setLong(1, seqNo);
				stmt.setString(2, ssDTO.getRequestNo());
				stmt.setTimestamp(3, ReqDatetimestamp);
				stmt.setString(4, ssDTO.getRequestorTypeDes());
				stmt.setString(5, ssDTO.getNicNo());
				stmt.setString(6, ssDTO.getNameOfOperator());
				stmt.setString(7, ssDTO.getNameOfOperatorSin());
				stmt.setString(8, ssDTO.getNameOfOperatorTamil());
				stmt.setString(9, ssDTO.getAddressOne());
				stmt.setString(10, ssDTO.getAddressOneSin());
				stmt.setString(11, ssDTO.getAddressOneTamil());
				stmt.setString(12, ssDTO.getAddressTwo());
				stmt.setString(13, ssDTO.getAddressOneSin());
				stmt.setString(14, ssDTO.getAdressTwoTamil());
				stmt.setString(15, ssDTO.getCity());
				stmt.setString(16, ssDTO.getCitySin());
				stmt.setString(17, ssDTO.getCityTamil());
				stmt.setString(18, ssDTO.getMobileNo());
				stmt.setString(19, ssDTO.getTelNo());
				stmt.setString(20, ssDTO.getEmail());
				stmt.setString(21, ssDTO.getSchoolName());
				stmt.setString(22, ssDTO.getSchoolNameSin());
				stmt.setString(23, ssDTO.getSchoolNameTamil());
				stmt.setString(24, ssDTO.getSchoolAddressOne());
				stmt.setString(25, ssDTO.getSchoolAdrressOneSin());
				stmt.setString(26, ssDTO.getSchoolAddressOneTamil());
				stmt.setString(27, ssDTO.getSchoolAddressTwo());
				stmt.setString(28, ssDTO.getSchoolAddressTwoSin());
				stmt.setString(29, ssDTO.getSchoolAddressTwoTamil());
				stmt.setString(30, ssDTO.getSchoolCity());
				stmt.setString(31, ssDTO.getSchoolCitySin());
				stmt.setString(32, ssDTO.getSchoolCityTamil());
				stmt.setString(33, ssDTO.getSchoolProvinceCode());
				stmt.setString(34, ssDTO.getSchoolDistrictCode());
				stmt.setString(35, ssDTO.getSchoolDivisinSecCode());
				stmt.setString(36, ssDTO.getSchoolTelNo());
				stmt.setString(37, ssDTO.getSchoolMobileNo());
				stmt.setString(38, ssDTO.getSchoolEmailAdd());
				stmt.setString(39, loginUser);
				stmt.setTimestamp(40, timestamp);
				stmt.setString(41, "P");
				stmt.setString(42, ssDTO.getLanguageDes());

				stmt.executeUpdate();
				con.commit();
			} catch (SQLException e) {
				con.rollback();
				success = false;
				e.printStackTrace();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			success = false;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return success;
	}

	// generate request number for Request For Sisusariya page
	@Override
	public String generateSisiRequestNo() {

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

			String sql = "SELECT app_no FROM public.nt_r_number_generation WHERE code= 'SSR'";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("app_no");
			}

			if (result != null) {

				lastyr = result.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String ApprecordcountN = "00001";
					strSurveyRequestNo = "SSR" + currYear + ApprecordcountN;
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
					strSurveyRequestNo = "SSR" + currYear + ApprecordcountN;
				}
			} else
				strSurveyRequestNo = "SSR" + currYear + "00001";

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return strSurveyRequestNo;

	}

	// save route information data from Request Sisusariya Page
	@Override
	public boolean saveRouteInformationData(SisuSeriyaDTO ssDTO, String loginUser1) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean success = true;

		try {
			con = ConnectionManager.getConnection();

			java.util.Date date = new java.util.Date();
			Timestamp timestamp = new Timestamp(date.getTime());

			long seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_m_sisu_requestor_route_info");

			String sql = "INSERT INTO public.nt_m_sisu_requestor_route_info"
					+ "(srr_seq,srr_request_no,srr_origin,srr_origin_si,srr_origin_ta,srr_destination,srr_destination_si,srr_destination_ta,"
					+ " srr_via,srr_via_si,srr_via_ta,srr_no_of_passengers,srr_created_by,srr_created_date)"
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?,?,?, ?, ?, ?)  ;";

			stmt = con.prepareStatement(sql);

			try {
				stmt.setLong(1, seqNo);
				stmt.setString(2, ssDTO.getRequestNo());
				stmt.setString(3, ssDTO.getOriginDes());
				stmt.setString(4, ssDTO.getOriginDesSin());
				stmt.setString(5, ssDTO.getOriginDesTamil());
				stmt.setString(6, ssDTO.getDestinationDes());
				stmt.setString(7, ssDTO.getDestinationDesSin());
				stmt.setString(8, ssDTO.getDestinationDesTamil());
				stmt.setString(9, ssDTO.getVia());
				stmt.setString(10, ssDTO.getViaSin());
				stmt.setString(11, ssDTO.getViaTamil());
				stmt.setString(12, ssDTO.getNoOfPassengers());
				stmt.setString(13, loginUser1);
				stmt.setTimestamp(14, timestamp);

				stmt.executeUpdate();
				con.commit();
			} catch (SQLException e) {
				con.rollback();
				success = false;
				e.printStackTrace();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			success = false;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return success;

	}

	// show route information data on grid
	@Override
	public List<SisuSeriyaDTO> getDataonGrid(SisuSeriyaDTO ssDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SisuSeriyaDTO> dataOnGrid = new ArrayList<SisuSeriyaDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select * from public.nt_m_sisu_requestor_route_info" + " where srr_request_no =? ;";
			ps = con.prepareStatement(query);

			ps.setString(1, ssDTO.getRequestNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				SisuSeriyaDTO sisuSariyaDTO = new SisuSeriyaDTO();
				sisuSariyaDTO.setOriginDes(rs.getString("srr_origin"));
				sisuSariyaDTO.setDestinationDes(rs.getString("srr_destination"));
				sisuSariyaDTO.setVia(rs.getString("srr_via"));
				sisuSariyaDTO.setNoOfPassengers(rs.getString("srr_no_of_passengers"));
				// for view page
				sisuSariyaDTO.setRequestNo(rs.getString("srr_request_no"));
				sisuSariyaDTO.setViaSin(rs.getString("srr_via_si"));
				sisuSariyaDTO.setViaTamil(rs.getString("srr_via_ta"));
				sisuSariyaDTO.setOriginDesSin(rs.getString("srr_origin_si"));
				sisuSariyaDTO.setOriginDesTamil(rs.getString("srr_origin_ta"));
				sisuSariyaDTO.setDestinationDesSin(rs.getString("srr_destination_si"));
				sisuSariyaDTO.setDestinationDesTamil(rs.getString("srr_destination_ta"));

				dataOnGrid.add(sisuSariyaDTO);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return dataOnGrid;
	}

	@Override
	public boolean isAlreadyChecked(SisuSeriyaDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isChecked = false;

		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct sps_service_ref_no,sps_status from public.nt_m_sisu_permit_hol_service_info "
					+ "where sps_service_ref_no=? and sps_is_checked is not null";

			ps = con.prepareStatement(query);
			ps.setString(1, dto.getServiceRefNo());
			rs = ps.executeQuery();

			if (rs.next() == true) {
				isChecked = true;
			} else {
				isChecked = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isChecked;
	}

	@Override
	public boolean isAlreadyRecomended(SisuSeriyaDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isRecomended = false;

		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct sps_service_ref_no,sps_status from public.nt_m_sisu_permit_hol_service_info "
					+ "where sps_service_ref_no=?  and sps_is_recommended is not null";

			ps = con.prepareStatement(query);
			ps.setString(1, dto.getServiceRefNo());
			rs = ps.executeQuery();

			if (rs.next() == true) {
				isRecomended = true;
			} else {
				isRecomended = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isRecomended;
	}

	@Override
	public boolean isAlreadyApproved(SisuSeriyaDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isApproved = false;

		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct sps_service_ref_no,sps_status from public.nt_m_sisu_permit_hol_service_info "
					+ "where sps_service_ref_no=? and ((is_cancellation is null and sps_status='A') or (is_cancellation='Y' and sps_status='I'))";

			ps = con.prepareStatement(query);
			ps.setString(1, dto.getServiceRefNo());
			rs = ps.executeQuery();

			if (rs.next() == true) {
				isApproved = true;
			} else {
				isApproved = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isApproved;
	}

	@Override
	public boolean isAlreadyRejected(SisuSeriyaDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isRejected = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct sps_service_ref_no,sps_status from public.nt_m_sisu_permit_hol_service_info "
					+ "where sps_service_ref_no=? and sps_status='R'";

			ps = con.prepareStatement(query);
			ps.setString(1, dto.getServiceRefNo());
			rs = ps.executeQuery();

			if (rs.next() == true) {
				isRejected = true;
			} else {
				isRejected = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isRejected;
	}

	@Override
	public void deleteDataFromGrid(SisuSeriyaDTO ssDTO, String string1, String string2, String string3,
			String string4) {
		Connection con = null;
		PreparedStatement ps = null, ps1 = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query1 = "DELETE from  public.nt_m_sisu_requestor_route_info  where srr_origin=? and  srr_destination=? and srr_via=? and srr_no_of_passengers=? and srr_request_no=? ";
			ps1 = con.prepareStatement(query1);
			ps1.setString(1, string1);
			ps1.setString(2, string2);
			ps1.setString(3, string3);
			ps1.setString(4, string4);
			ps1.setString(5, ssDTO.getRequestNo());
			ps1.executeUpdate();

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
	}

	// View Edit Request sisusariya page
	// get request number list for drop down
	@Override
	public List<SisuSeriyaDTO> getRequesNoDropDown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SisuSeriyaDTO> requestNoList = new ArrayList<SisuSeriyaDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT sri_request_no FROM public.nt_m_sisu_requestor_info where sri_request_no is not null and sri_request_no != ' ' order by sri_request_no desc;";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				SisuSeriyaDTO ss = new SisuSeriyaDTO();

				ss.setRequestNo(rs.getString("sri_request_no"));
				requestNoList.add(ss);

			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return requestNoList;

	}

	@Override
	public List<SisuSeriyaDTO> getOperatorDepoNameDropDown() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> requestNoList = new ArrayList<SisuSeriyaDTO>();
		try {
			con = ConnectionManager.getConnection();
			String query = "select distinct sps_operator_name FROM public.nt_m_sisu_permit_hol_service_info where sps_operator_name is not null and sps_operator_name != '' order by sps_operator_name;";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				SisuSeriyaDTO ss = new SisuSeriyaDTO();
				ss.setNameOfOperator(rs.getString("sps_operator_name"));
				requestNoList.add(ss);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return requestNoList;
	}

	@Override
	public List<SisuSeriyaDTO> getOperatorDepoNameDropDownForRenewal() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> requestNoList = new ArrayList<SisuSeriyaDTO>();
		try {
			con = ConnectionManager.getConnection();
			String query = "select distinct sps_operator_name FROM public.nt_m_sisu_permit_hol_service_info where sps_operator_name is not null and sps_operator_name != '' and sps_status='A' order by sps_operator_name;";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				SisuSeriyaDTO ss = new SisuSeriyaDTO();
				ss.setNameOfOperator(rs.getString("sps_operator_name"));
				requestNoList.add(ss);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return requestNoList;
	}

	@Override
	public List<SisuSeriyaDTO> getOperatorDepoNameDropDownForSisuApproval() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> requestNoList = new ArrayList<SisuSeriyaDTO>();
		try {
			con = ConnectionManager.getConnection();
			String query = "select distinct sps_operator_name FROM public.nt_m_sisu_permit_hol_service_info where sps_operator_name is not null and sps_operator_name != '' and (sps_renewal_status is null or sps_renewal_status!='P') order by sps_operator_name;";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				SisuSeriyaDTO ss = new SisuSeriyaDTO();
				ss.setNameOfOperator(rs.getString("sps_operator_name"));
				requestNoList.add(ss);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return requestNoList;
	}

	@Override
	public List<SisuSeriyaDTO> getOperatorDepoNameListForCommonInquiry(SisuSeriyaDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> requestNoList = new ArrayList<SisuSeriyaDTO>();
		try {
			String WHERE_SQL = "";

			if (dto.getRequestNo() != null && !dto.getRequestNo().equals("")) {
				WHERE_SQL = WHERE_SQL + " AND sps_request_no = '" + dto.getRequestNo() + "' ";
			}
			if (dto.getBusRegNo() != null && !dto.getBusRegNo().equals("")) {
				WHERE_SQL = WHERE_SQL + " AND sps_bus_no = '" + dto.getBusRegNo() + "' ";
			}
			if (dto.getServiceRefNo() != null && !dto.getServiceRefNo().equals("")) {
				WHERE_SQL = WHERE_SQL + " AND sps_service_ref_no = '" + dto.getServiceRefNo() + "' ";
			}
			if (dto.getServiceNo() != null && !dto.getServiceNo().equals("")) {
				WHERE_SQL = WHERE_SQL + " AND sps_service_no = '" + dto.getServiceNo() + "' ";
			}

			con = ConnectionManager.getConnection();

			String query = "select distinct sps_operator_name FROM public.nt_m_sisu_permit_hol_service_info where sps_operator_name is not null and sps_operator_name != '' "
					+ WHERE_SQL + " order by sps_operator_name;";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				SisuSeriyaDTO ss = new SisuSeriyaDTO();
				ss.setNameOfOperator(rs.getString("sps_operator_name"));
				requestNoList.add(ss);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return requestNoList;
	}

	@Override
	public List<SisuSeriyaDTO> getOperatorDepoNameListForVoucherCreation() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> requestNoList = new ArrayList<SisuSeriyaDTO>();
		try {
			con = ConnectionManager.getConnection();
			String query = "select distinct sps_operator_name FROM public.nt_m_sisu_permit_hol_service_info where sps_status='A' and sps_operator_name is not null and sps_operator_name != '' order by sps_operator_name;";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				SisuSeriyaDTO ss = new SisuSeriyaDTO();
				ss.setNameOfOperator(rs.getString("sps_operator_name"));
				requestNoList.add(ss);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return requestNoList;
	}

	@Override
	public List<SisuSeriyaDTO> getRequestNo() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct sps_request_no from public.nt_m_sisu_permit_hol_service_info "
					+ "WHERE sps_request_no is not null and sps_request_no !='' and (sps_renewal_status is null or sps_renewal_status!='P') order by sps_request_no desc";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			SisuSeriyaDTO s;

			while (rs.next()) {
				s = new SisuSeriyaDTO();
				s.setRequestNo(rs.getString("sps_request_no"));
				data.add(s);
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
	public List<SisuSeriyaDTO> getServiceRefNoListForSisuApproval(SisuSeriyaDTO dto) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct sps_service_ref_no,sps_request_no from public.nt_m_sisu_permit_hol_service_info "
					+ "WHERE sps_service_ref_no is not null and sps_service_ref_no !='' "
					+ " and (sps_renewal_status is null or sps_renewal_status!='P') and sps_request_no=? order by sps_service_ref_no ";

			ps = con.prepareStatement(query);
			ps.setString(1, dto.getRequestNo());
			rs = ps.executeQuery();

			SisuSeriyaDTO s;

			while (rs.next()) {
				s = new SisuSeriyaDTO();
				s.setServiceRefNo(rs.getString("sps_service_ref_no"));
				data.add(s);
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
	public List<SisuSeriyaDTO> getServiceRefNoListForCommonInquiry(SisuSeriyaDTO dto) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		try {
			String WHERE_SQL = "";

			if (dto.getBusRegNo() != null && !dto.getBusRegNo().equals("")) {
				WHERE_SQL = WHERE_SQL + " AND sps_bus_no = '" + dto.getBusRegNo() + "' ";
			}
			if (dto.getRequestNo() != null && !dto.getRequestNo().equals("")) {
				WHERE_SQL = WHERE_SQL + " AND sps_request_no = '" + dto.getRequestNo() + "' ";
			}
			if (dto.getNameOfOperator() != null && !dto.getNameOfOperator().equals("")) {
				WHERE_SQL = WHERE_SQL + " AND sps_operator_name = '" + dto.getNameOfOperator() + "' ";
			}
			if (dto.getServiceNo() != null && !dto.getServiceNo().equals("")) {
				WHERE_SQL = WHERE_SQL + " AND sps_service_no = '" + dto.getServiceNo() + "' ";
			}

			con = ConnectionManager.getConnection();

			String query = "select distinct sps_service_ref_no from public.nt_m_sisu_permit_hol_service_info "
					+ "WHERE sps_service_ref_no is not null and sps_service_ref_no !='' " + " " + WHERE_SQL
					+ " order by sps_service_ref_no ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			SisuSeriyaDTO s;

			while (rs.next()) {
				s = new SisuSeriyaDTO();
				s.setServiceRefNo(rs.getString("sps_service_ref_no"));
				data.add(s);
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
	public List<SisuSeriyaDTO> getSisuSeriyaData(SisuSeriyaDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> sisuSeriyaList = new ArrayList<SisuSeriyaDTO>();
		String WHERE_SQL = "";
		boolean whereadded = false;

		if (dto.getRequestNo() != null && !dto.getRequestNo().equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE sps_request_no = " + "'" + dto.getRequestNo()
					+ "' and sps_service_ref_no is not null and sps_renewal_new_expire_date is null and sps_service_ref_no != '' and (sps_status='P' or sps_status='A') ";
			whereadded = true;
		}
		if (dto.getServiceRefNo() != null && !dto.getServiceRefNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND sps_service_ref_no = " + "'" + dto.getServiceRefNo() + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE sps_service_ref_no = " + "'" + dto.getServiceRefNo()
						+ "'and sps_service_ref_no is not null and sps_renewal_new_expire_date is null and sps_service_ref_no != '' and (sps_status='P' or sps_status='A') ";
				whereadded = true;
			}
		}

		if (dto.getRequestStartDate() != null && dto.getRequestEndDate() != null) {
			Timestamp startDate = new Timestamp(dto.getRequestStartDate().getTime());
			Timestamp endDate = new Timestamp(dto.getRequestEndDate().getTime());
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + "and nt_m_sisu_requestor_info.sri_request_date >= " + "' " + startDate
						+ "' and nt_m_sisu_requestor_info.sri_request_date <= " + "'" + endDate + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + "WHERE nt_m_sisu_requestor_info.sri_request_date >= " + "' " + startDate
						+ "' and nt_m_sisu_requestor_info.sri_request_date <= " + "'" + endDate
						+ "' and (sps_status='P' or sps_status='A') ";
				whereadded = true;
			}
		} else if (dto.getRequestEndDate() != null && dto.getRequestStartDate() == null) {

			Timestamp endDate = new Timestamp(dto.getRequestEndDate().getTime());
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + "and nt_m_sisu_requestor_info.sri_request_date <= " + "'" + endDate + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE  nt_m_sisu_requestor_info.sri_request_date <= " + "'" + endDate
						+ "' and (sps_status='P' or sps_status='A') ";
				whereadded = true;
			}
		} else if (dto.getRequestStartDate() != null && dto.getRequestEndDate() == null) {
			Timestamp startDate = new Timestamp(dto.getRequestStartDate().getTime());

			if (whereadded) {
				WHERE_SQL = WHERE_SQL + "and nt_m_sisu_requestor_info.sri_request_date >= " + "'" + startDate + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE nt_m_sisu_requestor_info.sri_request_date >= " + "'" + startDate
						+ "' and (sps_status='P' or sps_status='A') ";
				whereadded = true;
			}
		}

		if (dto.getServiceNo() != null && !dto.getServiceNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND sps_service_no = " + "'" + dto.getServiceNo() + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE sps_service_no = " + "'" + dto.getServiceNo()
						+ "'and sps_service_ref_no is not null and sps_renewal_new_expire_date is null and sps_service_ref_no != '' and (sps_status='P' or sps_status='A') ";
				whereadded = true;
			}
		}

		if (dto.getNameOfOperator() != null && !dto.getNameOfOperator().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND sps_operator_name = " + "'" + dto.getNameOfOperator() + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE sps_operator_name = " + "'" + dto.getNameOfOperator()
						+ "'and sps_service_ref_no is not null and sps_renewal_new_expire_date is null and sps_service_ref_no != '' and (sps_status='P' or sps_status='A') ";
				whereadded = true;
			}
		}

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select distinct sps_request_no, sps_service_ref_no,sps_service_no,"
					+ "sps_service_end_date ,sps_status, sps_operator_name, sps_is_checked, sps_is_recommended from public.nt_m_sisu_permit_hol_service_info "
					+ "inner join public.nt_m_sisu_requestor_info on nt_m_sisu_requestor_info.sri_request_no = nt_m_sisu_permit_hol_service_info.sps_request_no "
					+ WHERE_SQL;

			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();
			SisuSeriyaDTO e;

			while (rs.next()) {
				e = new SisuSeriyaDTO();
				e.setRequestNo(rs.getString("sps_request_no"));
				e.setServiceRefNo(rs.getString("sps_service_ref_no"));
				e.setServiceNo(rs.getString("sps_service_no"));
				e.setNameOfOperator(rs.getString("sps_operator_name"));

				Timestamp ts = rs.getTimestamp("sps_service_end_date");

				if (ts != null) {
					Date date = new Date();
					date.setTime(ts.getTime());
					String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
					e.setServiceEndDateVal(formattedDate);
				} else {

					e.setServiceEndDateVal("N/A");
				}

				String approveStatusCode = rs.getString("sps_status");
				String approveStatus = "";

				if (approveStatusCode != null) {
					if (approveStatusCode.equals("A")) {
						approveStatus = "APPROVED";
						e.setEditStatus(false);
					} else if (approveStatusCode.equals("R")) {
						approveStatus = "REJECTED";
						e.setEditStatus(false);
					} else if (approveStatusCode.equals("P")) {
						approveStatus = "PENDING";
						e.setEditStatus(true);
					}
				}

				e.setStatus(approveStatusCode);
				e.setStatusDes(approveStatus);

				e.setIsChecked(rs.getString("sps_is_checked"));
				e.setIsRecommended(rs.getString("sps_is_recommended"));

				sisuSeriyaList.add(e);
			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return sisuSeriyaList;
	}

	@Override
	public SisuSeriyaDTO showData(SisuSeriyaDTO ssDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		SisuSeriyaDTO dataOnGrid = new SisuSeriyaDTO();
		try {
			con = ConnectionManager.getConnection();

			String query = "select * from public.nt_m_sisu_requestor_route_info" + " where srr_request_no =? ;";
			ps = con.prepareStatement(query);

			ps.setString(1, ssDTO.getRequestNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				dataOnGrid.setOriginDes(rs.getString("srr_origin"));
				dataOnGrid.setDestinationDes(rs.getString("srr_destination"));
				dataOnGrid.setVia(rs.getString("srr_via"));
				dataOnGrid.setNoOfPassengers(rs.getString("srr_no_of_passengers"));
				// for view page
				dataOnGrid.setRequestNo(rs.getString("srr_request_no"));
				dataOnGrid.setViaSin(rs.getString("srr_via_si"));
				dataOnGrid.setViaTamil(rs.getString("srr_via_ta"));
				dataOnGrid.setOriginDesSin(rs.getString("srr_origin_si"));
				dataOnGrid.setOriginDesTamil(rs.getString("srr_origin_ta"));
				dataOnGrid.setDestinationDesSin(rs.getString("srr_destination_si"));
				dataOnGrid.setDestinationDesTamil(rs.getString("srr_destination_ta"));
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return dataOnGrid;
	}

	@Override
	public boolean checkSisuSariyaRequest(SisuSeriyaDTO dto, String checkedBy) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isChecked = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_m_sisu_permit_hol_service_info SET sps_is_checked='Y', sps_checked_by=?, "
					+ "sps_checked_date=?, sps_modified_by=?, sps_modified_date=? WHERE sps_service_ref_no=? ;";

			ps = con.prepareStatement(query);

			ps.setString(1, checkedBy);
			ps.setTimestamp(2, timestamp);
			ps.setString(3, checkedBy);
			ps.setTimestamp(4, timestamp);
			ps.setString(5, dto.getServiceRefNo());
			int a = ps.executeUpdate();

			if (a > 0) {
				isChecked = true;
			} else {
				isChecked = false;
			}

			updateTaskStatusCompletedSubsidyTaskTable(con, dto.getRequestNo(), null, dto.getServiceRefNo(), "SS005a",
					"C", checkedBy);

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isChecked;
	}

	@Override
	public boolean recommendSisuSariyaRequest(SisuSeriyaDTO dto, String approvedBy) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isRecommended = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_m_sisu_permit_hol_service_info SET sps_is_recommended='Y', sps_recommended_by=?, "
					+ "sps_recommended_date=?, sps_modified_by=?, sps_modified_date=? WHERE sps_service_ref_no=? ;";

			ps = con.prepareStatement(query);

			ps.setString(1, approvedBy);
			ps.setTimestamp(2, timestamp);
			ps.setString(3, approvedBy);
			ps.setTimestamp(4, timestamp);
			ps.setString(5, dto.getServiceRefNo());
			int a = ps.executeUpdate();

			if (a > 0) {
				isRecommended = true;
			} else {
				isRecommended = false;
			}

			updateTaskStatusCompletedSubsidyTaskTable(con, dto.getRequestNo(), null, dto.getServiceRefNo(), "SS005b",
					"C", approvedBy);

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isRecommended;
	}

	@Override
	public boolean approveSisuSeriyaRequest(SisuSeriyaDTO dto, String approvedBy, String servicesNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isApproved = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_m_sisu_permit_hol_service_info SET  sps_status='A', sps_renewal_status='O', sps_approve_reject_by=?, "
					+ "sps_approve_reject_date=?, sps_modified_by=?, sps_modified_date=? ,sps_service_no=? ,sps_service_agreement_no=? WHERE sps_service_ref_no=? ;";

			ps = con.prepareStatement(query);

			ps.setString(1, approvedBy);
			ps.setTimestamp(2, timestamp);
			ps.setString(3, approvedBy);
			ps.setTimestamp(4, timestamp);
			ps.setString(5, servicesNo);
			ps.setString(6, servicesNo);
			ps.setString(7, dto.getServiceRefNo());
			int a = ps.executeUpdate();

			if (a > 0) {
				isApproved = true;
			} else {
				isApproved = false;
			}

			updateTaskStatusCompletedSubsidyTaskTable(con, dto.getRequestNo(), servicesNo, dto.getServiceRefNo(),
					"SS006", "C", approvedBy);

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isApproved;
	}

	@Override
	public SisuSeriyaDTO showDataFirstTab(SisuSeriyaDTO ssDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		SisuSeriyaDTO dataOnFields = new SisuSeriyaDTO();
		try {
			con = ConnectionManager.getConnection();

			String query = "select sri_id_no,sri_full_name,sri_full_name_si,sri_full_name_ta,sri_add1,sri_add1_si,sri_add1_ta,sri_requestor_type"
					+ ",sri_add2,sri_add2_si,sri_add2_ta,sri_city,sri_city_si,sri_city_ta,sri_mobile_no,sri_tel_no,sri_email,"
					+ "sri_school_name,sri_school_name_si,sri_school_name_ta,sri_school_add1,sri_school_add1_si ,sri_school_add1_ta,"
					+ "sri_school_add2,sri_school_add2_si,sri_school_add2_ta,sri_school_city,sri_school_city_si,sri_school_city_ta,"
					+ "sri_school_province,sri_school_district,sri_school_div_sec,sri_school_tel_no,sri_school_mobile,sri_school_email,sri_language "
					+ "from public.nt_m_sisu_requestor_info where sri_request_no=? ;";
			ps = con.prepareStatement(query);

			ps.setString(1, ssDTO.getRequestNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				dataOnFields.setRequestorTypeDes(rs.getString("sri_requestor_type"));
				dataOnFields.setNicNo(rs.getString("sri_id_no"));
				dataOnFields.setNameOfOperator(rs.getString("sri_full_name"));
				dataOnFields.setNameOfOperatorSin(rs.getString("sri_full_name_si"));
				dataOnFields.setNameOfOperatorTamil(rs.getString("sri_full_name_ta"));
				dataOnFields.setAddressOne(rs.getString("sri_add1"));
				dataOnFields.setAddressOneSin(rs.getString("sri_add1_si"));
				dataOnFields.setAddressOneTamil(rs.getString("sri_add1_ta"));
				dataOnFields.setAddressTwo(rs.getString("sri_add2"));
				dataOnFields.setAdressTwoSin(rs.getString("sri_add2_si"));
				dataOnFields.setAdressTwoTamil(rs.getString("sri_add2_ta"));
				dataOnFields.setCity(rs.getString("sri_city"));
				dataOnFields.setCitySin(rs.getString("sri_city_si"));
				dataOnFields.setCityTamil(rs.getString("sri_city_ta"));
				dataOnFields.setMobileNo(rs.getString("sri_mobile_no"));
				dataOnFields.setTelNo(rs.getString("sri_tel_no"));
				dataOnFields.setEmail(rs.getString("sri_email"));
				dataOnFields.setLanguageDes(rs.getString("sri_language"));
				dataOnFields.setSchoolName(rs.getString("sri_school_name"));
				dataOnFields.setSchoolNameSin(rs.getString("sri_school_name_si"));
				dataOnFields.setSchoolNameTamil(rs.getString("sri_school_name_ta"));
				dataOnFields.setSchoolAddressOne(rs.getString("sri_school_add1"));
				dataOnFields.setSchoolAdrressOneSin(rs.getString("sri_school_add1_si"));
				dataOnFields.setSchoolAddressOneTamil(rs.getString("sri_school_add1_ta"));
				dataOnFields.setSchoolAddressTwo(rs.getString("sri_school_add2"));
				dataOnFields.setSchoolAddressTwoSin(rs.getString("sri_school_add2_si"));
				dataOnFields.setSchoolAddressTwoTamil(rs.getString("sri_school_add2_ta"));
				dataOnFields.setSchoolCity(rs.getString("sri_school_city"));
				dataOnFields.setSchoolCitySin(rs.getString("sri_school_city_si"));
				dataOnFields.setSchoolCityTamil(rs.getString("sri_school_city_ta"));
				dataOnFields.setSchoolMobileNo(rs.getString("sri_school_mobile"));
				dataOnFields.setSchoolTelNo(rs.getString("sri_school_tel_no"));
				dataOnFields.setSchoolEmailAdd(rs.getString("sri_school_email"));
				dataOnFields.setSchoolProvinceDes(rs.getString("sri_school_province"));
				dataOnFields.setSchoolDivisionSecDes(rs.getString("sri_school_div_sec"));
				dataOnFields.setSchoolDistrictDes(rs.getString("sri_school_district"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return dataOnFields;
	}

	@Override
	public void updateRequestSisusariData(SisuSeriyaDTO ssDTO) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_sisu_requestor_info SET sri_id_no=?,sri_full_name=?, sri_full_name_si=?,sri_full_name_ta=?,sri_add1=?,"
					+ "sri_add1_si=?,sri_add1_ta=?,sri_add2=?,sri_add2_si=?,sri_add2_ta=?,sri_city=?,sri_city_si=?,sri_city_ta=?,sri_mobile_no=?"
					+ ",sri_tel_no=?,sri_email=?,sri_school_name=?,sri_school_name_si=?,sri_school_name_ta=?,sri_school_add1=?,sri_school_add1_si=?,sri_school_add1_ta=?"
					+ ",sri_school_add2=?,sri_school_add2_si=?,sri_school_add2_ta=?,sri_school_city=?,sri_school_city_si=?,sri_school_city_ta=?,sri_school_province=?"
					+ ",sri_school_district=?,sri_school_div_sec=?,sri_school_tel_no=?,sri_school_mobile=?,sri_school_email=?"
					+ "  WHERE  sri_request_no=?;";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, ssDTO.getNicNo());
			stmt.setString(2, ssDTO.getNameOfOperator());
			stmt.setString(3, ssDTO.getNameOfOperatorSin());
			stmt.setString(4, ssDTO.getNameOfOperatorTamil());
			stmt.setString(5, ssDTO.getAddressOne());
			stmt.setString(6, ssDTO.getAddressOneSin());
			stmt.setString(7, ssDTO.getAddressOneTamil());
			stmt.setString(8, ssDTO.getAddressTwo());
			stmt.setString(9, ssDTO.getAddressTwo());
			stmt.setString(10, ssDTO.getAddressTwo());
			stmt.setString(11, ssDTO.getCity());
			stmt.setString(12, ssDTO.getCitySin());
			stmt.setString(13, ssDTO.getCityTamil());
			stmt.setString(14, ssDTO.getMobileNo());
			stmt.setString(15, ssDTO.getTelNo());
			stmt.setString(16, ssDTO.getEmail());
			stmt.setString(17, ssDTO.getSchoolName());
			stmt.setString(18, ssDTO.getSchoolNameSin());
			stmt.setString(19, ssDTO.getSchoolNameTamil());
			stmt.setString(20, ssDTO.getSchoolAddressOne());
			stmt.setString(21, ssDTO.getSchoolAdrressOneSin());
			stmt.setString(22, ssDTO.getSchoolAddressOneTamil());
			stmt.setString(23, ssDTO.getSchoolAddressTwo());
			stmt.setString(24, ssDTO.getSchoolAddressTwoSin());
			stmt.setString(25, ssDTO.getSchoolAddressTwoTamil());
			stmt.setString(26, ssDTO.getSchoolCity());
			stmt.setString(27, ssDTO.getSchoolCitySin());
			stmt.setString(28, ssDTO.getSchoolCityTamil());

			stmt.setString(29, ssDTO.getSchoolProvinceCode());
			stmt.setString(30, ssDTO.getSchoolDistrictCode());
			stmt.setString(31, ssDTO.getSchoolDivisinSecCode());
			stmt.setString(32, ssDTO.getSchoolTelNo());
			stmt.setString(33, ssDTO.getSchoolMobileNo());
			stmt.setString(34, ssDTO.getSchoolEmailAdd());
			stmt.setString(35, ssDTO.getRequestNo());

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

	@Override
	public void updateRouteInformationData(SisuSeriyaDTO ssDTO, String string1, String string2, String string3,
			String string4) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_sisu_requestor_route_info SET srr_origin=?,srr_origin_si=?, srr_origin_ta=?,srr_destination=?,srr_destination_si=?,"
					+ "srr_destination_ta=?,srr_via=?,srr_via_si=?,srr_via_ta=?,srr_no_of_passengers=?"
					+ "  WHERE  srr_request_no=? and srr_origin=?  and srr_destination=? "
					+ " and  srr_via=? and srr_no_of_passengers=? ;";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, ssDTO.getOriginDes());
			stmt.setString(2, ssDTO.getOriginDesSin());
			stmt.setString(3, ssDTO.getOriginDesTamil());
			stmt.setString(4, ssDTO.getDestinationDes());
			stmt.setString(5, ssDTO.getDestinationDesSin());
			stmt.setString(6, ssDTO.getDestinationDesTamil());
			stmt.setString(7, ssDTO.getVia());
			stmt.setString(8, ssDTO.getViaSin());
			stmt.setString(9, ssDTO.getViaTamil());
			stmt.setString(10, ssDTO.getNoOfPassengers());
			stmt.setString(11, ssDTO.getRequestNo());
			stmt.setString(12, string1);
			stmt.setString(13, string2);
			stmt.setString(14, string3);
			stmt.setString(15, string4);

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

	@Override
	public boolean rejectCheckBySisuSeriyaRequest(SisuSeriyaDTO selectDTO, String rejectedBy) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isReject = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();
			String query = "UPDATE public.nt_m_sisu_permit_hol_service_info SET sps_status='R', sps_is_checked='N', sps_checked_by=?, "
					+ "sps_checked_date=?, sps_modified_by=?, sps_modified_date=? WHERE sps_service_ref_no=? ;";

			ps = con.prepareStatement(query);

			ps.setString(1, rejectedBy);
			ps.setTimestamp(2, timestamp);
			ps.setString(3, rejectedBy);
			ps.setTimestamp(4, timestamp);
			ps.setString(5, selectDTO.getServiceRefNo());

			int a = ps.executeUpdate();

			if (a > 0) {
				isReject = true;
			} else {
				isReject = false;
			}

			updateTaskStatusCompletedSubsidyTaskTable(con, selectDTO.getRequestNo(), null, selectDTO.getServiceRefNo(),
					"SS005a", "C", rejectedBy);

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isReject;
	}

	@Override
	public boolean rejectRecommendSisuSeriyaRequest(SisuSeriyaDTO selectDTO, String rejectedBy) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isReject = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();
			String query = "UPDATE public.nt_m_sisu_permit_hol_service_info SET sps_status='R', sps_is_recommended='N', sps_recommended_by=?, "
					+ "sps_recommended_date=?, sps_modified_by=?, sps_modified_date=? WHERE sps_service_ref_no=? ;";

			ps = con.prepareStatement(query);

			ps.setString(1, rejectedBy);
			ps.setTimestamp(2, timestamp);
			ps.setString(3, rejectedBy);
			ps.setTimestamp(4, timestamp);
			ps.setString(5, selectDTO.getServiceRefNo());

			int a = ps.executeUpdate();

			if (a > 0) {
				isReject = true;
			} else {
				isReject = false;
			}
			updateTaskStatusCompletedSubsidyTaskTable(con, selectDTO.getRequestNo(), null, selectDTO.getServiceRefNo(),
					"SS005b", "C", rejectedBy);

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isReject;
	}

	@Override
	public boolean rejectSisuSeriyaRequest(SisuSeriyaDTO selectDTO, String rejectedBy, String rejectReason) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isReject = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();
			String query = "UPDATE public.nt_m_sisu_permit_hol_service_info SET  sps_status='R', sps_approve_reject_by=?, "
					+ "sps_approve_reject_date=?, sps_modified_by=?, sps_modified_date=?, sps_reject_reason=? WHERE sps_service_ref_no=? ;";

			ps = con.prepareStatement(query);

			ps.setString(1, rejectedBy);
			ps.setTimestamp(2, timestamp);
			ps.setString(3, rejectedBy);
			ps.setTimestamp(4, timestamp);
			ps.setString(5, rejectReason);
			ps.setString(6, selectDTO.getServiceRefNo());

			int a = ps.executeUpdate();

			if (a > 0) {
				isReject = true;
			} else {
				isReject = false;
			}

			updateTaskStatusCompletedSubsidyTaskTable(con, selectDTO.getRequestNo(), null, selectDTO.getServiceRefNo(),
					"SS006", "C", rejectedBy);

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isReject;
	}

	@Override
	public List<SisuSeriyaDTO> getDefaultSisuSeriyaDataForSisuApproval(String status) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> sisuSeriyaList = new ArrayList<SisuSeriyaDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select distinct sps_request_no, sps_service_ref_no,sps_service_no,"
					+ "sps_service_end_date,sps_service_start_date,sps_status, sps_operator_name, sps_is_checked, sps_is_recommended from public.nt_m_sisu_permit_hol_service_info where (sps_renewal_status is null or sps_renewal_status!='P') and sps_status="
					+ "'" + status + "' ";

			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();
			SisuSeriyaDTO e;

			while (rs.next()) {
				e = new SisuSeriyaDTO();
				e.setRequestNo(rs.getString("sps_request_no"));
				e.setServiceRefNo(rs.getString("sps_service_ref_no"));
				e.setServiceNo(rs.getString("sps_service_no"));
				e.setNameOfOperator(rs.getString("sps_operator_name"));

				Timestamp ts = rs.getTimestamp("sps_service_end_date");

				if (ts != null) {
					Date date = new Date();
					date.setTime(ts.getTime());
					e.setServiceEndDateObj(date);
					String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);

					e.setServiceEndDateVal(formattedDate);
				} else {
					e.setServiceEndDateVal("N/A");
				}

				Timestamp serviceStartDate = rs.getTimestamp("sps_service_start_date");

				if (serviceStartDate != null) {
					Date dateS = new Date();
					dateS.setTime(serviceStartDate.getTime());
					e.setServiceStartDateObj(dateS);
					String formattedSDate = new SimpleDateFormat("yyyy-MM-dd").format(dateS);

					e.setServiceStartDateVal(formattedSDate);
				} else {
					e.setServiceStartDateVal("N/A");
				}

				String approveStatusCode = rs.getString("sps_status");
				String approveStatus = "";

				if (approveStatusCode != null) {
					if (approveStatusCode.equals("A")) {
						approveStatus = "APPROVED";
						e.setEditStatus(false);
					} else if (approveStatusCode.equals("R")) {
						approveStatus = "REJECTED";
						e.setEditStatus(false);
					} else if (approveStatusCode.equals("P")) {
						approveStatus = "PENDING";
						e.setEditStatus(true);
					}
				}

				e.setStatus(approveStatusCode);
				e.setStatusDes(approveStatus);
				e.setIsChecked(rs.getString("sps_is_checked"));
				e.setIsRecommended(rs.getString("sps_is_recommended"));

				sisuSeriyaList.add(e);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return sisuSeriyaList;
	}

	@Override
	public String generateServiceNo() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;

		int year = Year.now().getValue();
		String currYear = Integer.toString(year).substring(2, 4);
		String strAppNo = "";
		String lastyr = "00";

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT sps_service_no  FROM public.nt_m_sisu_permit_hol_service_info  where sps_status='A' "
					+ "ORDER BY sps_service_no desc  LIMIT 1";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("sps_service_no");
			}

			if (result != null) {

				lastyr = result.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String ApprecordcountN = "00001";
					strAppNo = "SER" + currYear + ApprecordcountN;
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
					strAppNo = "SER" + currYear + ApprecordcountN;
				}
			} else
				strAppNo = "SER" + currYear + "00001";

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return strAppNo;
	}

	@Override
	public String generateServiceNoN(String loginUser, String type) {

		String last = retrieveLastNoForNumberGeneration(type);
		long no = Long.parseLong(last.substring(3));
		String next = String.valueOf((no + 1));
		String appNo = type + StringUtils.leftPad(next, 5, "0");

		updateNumberGeneration(type, appNo, loginUser);

		return appNo;
	}

	@Override
	public String retrieveLastNoForNumberGeneration(String code) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String appNo = null;

		try {
			con = ConnectionManager.getConnection();

			String sql1 = "SELECT app_no FROM public.nt_r_number_generation WHERE code=?";
			ps = con.prepareStatement(sql1);

			ps.setString(1, code);
			rs = ps.executeQuery();

			while (rs.next()) {
				appNo = rs.getString("app_no");

			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return appNo;
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

	@Override
	public List<SisuSeriyaDTO> getServiceNoList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct sps_service_no from public.nt_m_sisu_permit_hol_service_info "
					+ "WHERE sps_service_no is not null and sps_service_no !='' and (sps_renewal_status is null or sps_renewal_status!='P')  order by sps_service_no   ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			SisuSeriyaDTO s;

			while (rs.next()) {
				s = new SisuSeriyaDTO();
				s.setServiceNo(rs.getString("sps_service_no"));
				data.add(s);
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
	public List<SisuSeriyaDTO> getServiceNoListForCommonInquiry(SisuSeriyaDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		try {

			String WHERE_SQL = "";

			if (dto.getBusRegNo() != null && !dto.getBusRegNo().equals("")) {
				WHERE_SQL = WHERE_SQL + " AND sps_bus_no = '" + dto.getBusRegNo() + "' ";
			}
			if (dto.getRequestNo() != null && !dto.getRequestNo().equals("")) {
				WHERE_SQL = WHERE_SQL + " AND sps_request_no = '" + dto.getRequestNo() + "' ";
			}
			if (dto.getNameOfOperator() != null && !dto.getNameOfOperator().equals("")) {
				WHERE_SQL = WHERE_SQL + " AND sps_operator_name = '" + dto.getNameOfOperator() + "' ";
			}
			if (dto.getServiceRefNo() != null && !dto.getServiceRefNo().equals("")) {
				WHERE_SQL = WHERE_SQL + " AND sps_service_ref_no = '" + dto.getServiceRefNo() + "' ";
			}

			con = ConnectionManager.getConnection();

			String query = "select distinct sps_service_no from public.nt_m_sisu_permit_hol_service_info "
					+ "WHERE sps_service_no is not null and sps_service_no !='' " + WHERE_SQL
					+ " order by sps_service_no ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			SisuSeriyaDTO s;

			while (rs.next()) {
				s = new SisuSeriyaDTO();
				s.setServiceNo(rs.getString("sps_service_no"));
				data.add(s);
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
	public boolean isReqNoExist(SisuSeriyaDTO ssDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isExist = false;

		try {

			con = ConnectionManager.getConnection();

			String query = "select srr_request_no from public.nt_m_sisu_requestor_route_info "
					+ "where srr_request_no=? ";

			ps = con.prepareStatement(query);
			ps.setString(1, ssDTO.getRequestNo());
			rs = ps.executeQuery();

			if (rs.next() == true) {
				isExist = true;
			} else {
				isExist = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isExist;
	}

	@Override
	public List<SisuSeriyaDTO> getApprovedRequestNo() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct sps_request_no from public.nt_m_sisu_permit_hol_service_info "
					+ "WHERE sps_request_no is not null and sps_request_no !='' and sps_status='A'  and  (sps_service_agreement_issuedby IS NULL or sps_permit_sticker_issuedby IS NULL or sps_issue_logsheets_issuedby IS null) order by sps_request_no desc";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			SisuSeriyaDTO s;

			while (rs.next()) {
				s = new SisuSeriyaDTO();
				s.setRequestNo(rs.getString("sps_request_no"));
				data.add(s);
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
	public List<SisuSeriyaDTO> getRequestNoListForCommonInquiry(SisuSeriyaDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		try {

			String WHERE_SQL = "";

			if (dto.getBusRegNo() != null && !dto.getBusRegNo().equals("")) {
				WHERE_SQL = WHERE_SQL + " AND sps_bus_no = '" + dto.getBusRegNo() + "' ";
			}
			if (dto.getNameOfOperator() != null && !dto.getNameOfOperator().equals("")) {
				WHERE_SQL = WHERE_SQL + " AND sps_operator_name = '" + dto.getNameOfOperator() + "' ";
			}
			if (dto.getServiceRefNo() != null && !dto.getServiceRefNo().equals("")) {
				WHERE_SQL = WHERE_SQL + " AND sps_service_ref_no = '" + dto.getServiceRefNo() + "' ";
			}
			if (dto.getServiceNo() != null && !dto.getServiceNo().equals("")) {
				WHERE_SQL = WHERE_SQL + " AND sps_service_no = '" + dto.getServiceNo() + "' ";
			}

			con = ConnectionManager.getConnection();

			String query = "select distinct sps_request_no from public.nt_m_sisu_permit_hol_service_info "
					+ "WHERE sps_request_no is not null and sps_request_no !='' " + WHERE_SQL
					+ " order by sps_request_no ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			SisuSeriyaDTO s;

			while (rs.next()) {
				s = new SisuSeriyaDTO();
				s.setRequestNo(rs.getString("sps_request_no"));
				data.add(s);
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
	public List<SisuSeriyaDTO> getApprovedServiceRefNoList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct sps_service_ref_no " + " from public.nt_m_sisu_permit_hol_service_info "
					+ " WHERE sps_service_ref_no is not null and sps_service_ref_no !='' and sps_status='A' "
					+ " and sps_renewal_status not in ('I')   and  (sps_service_agreement_issuedby IS NULL or sps_permit_sticker_issuedby IS NULL or sps_issue_logsheets_issuedby IS null) "
					+ "order by sps_service_ref_no desc ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			SisuSeriyaDTO s;

			while (rs.next()) {
				s = new SisuSeriyaDTO();
				s.setServiceRefNo(rs.getString("sps_service_ref_no"));
				data.add(s);
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
	public List<SisuSeriyaDTO> getApprovedOperatorDepoNameList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct sps_operator_name from public.nt_m_sisu_permit_hol_service_info "
					+ "WHERE sps_operator_name is not null and sps_operator_name !='' and sps_status='A'  and  (sps_service_agreement_issuedby IS NULL or sps_permit_sticker_issuedby IS NULL or sps_issue_logsheets_issuedby IS null) order by sps_operator_name ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			SisuSeriyaDTO s;

			while (rs.next()) {
				s = new SisuSeriyaDTO();
				s.setNameOfOperator(rs.getString("sps_operator_name"));
				data.add(s);
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
	public List<SisuSeriyaDTO> getApprovedBusNoList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct sps_bus_no from public.nt_m_sisu_permit_hol_service_info "
					+ "WHERE sps_bus_no is not null and sps_bus_no !='' and sps_status='A'  and  (sps_service_agreement_issuedby IS NULL or sps_permit_sticker_issuedby IS NULL or sps_issue_logsheets_issuedby IS null) order by sps_bus_no ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			SisuSeriyaDTO s;

			while (rs.next()) {
				s = new SisuSeriyaDTO();
				s.setBusRegNo(rs.getString("sps_bus_no"));
				data.add(s);
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
	public List<SisuSeriyaDTO> getBusNoListForCommonInquiry(SisuSeriyaDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		try {

			String WHERE_SQL = "";

			if (dto.getRequestNo() != null && !dto.getRequestNo().equals("")) {
				WHERE_SQL = WHERE_SQL + " AND sps_request_no = '" + dto.getRequestNo() + "' ";
			}
			if (dto.getNameOfOperator() != null && !dto.getNameOfOperator().equals("")) {
				WHERE_SQL = WHERE_SQL + " AND sps_operator_name = '" + dto.getNameOfOperator() + "' ";
			}
			if (dto.getServiceRefNo() != null && !dto.getServiceRefNo().equals("")) {
				WHERE_SQL = WHERE_SQL + " AND sps_service_ref_no = '" + dto.getServiceRefNo() + "' ";
			}
			if (dto.getServiceNo() != null && !dto.getServiceNo().equals("")) {
				WHERE_SQL = WHERE_SQL + " AND sps_service_no = '" + dto.getServiceNo() + "' ";
			}

			con = ConnectionManager.getConnection();

			String query = "select distinct sps_bus_no from public.nt_m_sisu_permit_hol_service_info "
					+ "WHERE sps_bus_no is not null and sps_bus_no !='' " + WHERE_SQL + " order by sps_bus_no ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			SisuSeriyaDTO s;

			while (rs.next()) {
				s = new SisuSeriyaDTO();
				s.setBusRegNo(rs.getString("sps_bus_no"));
				data.add(s);
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
	public List<SisuSeriyaDTO> getApprovedServiceRefNoListForTeam() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct sps_service_ref_no " + " from public.nt_m_sisu_permit_hol_service_info "
					+ " WHERE sps_service_ref_no is not null and sps_service_ref_no !='' and sps_status='A' "
					+ " and sps_renewal_status not in ('I')   and  (sps_service_agreement_issuedby IS NOT NULL and sps_permit_sticker_issuedby IS NOT NULL and sps_issue_logsheets_issuedby IS NOT null) "
					+ "order by sps_service_ref_no desc ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			SisuSeriyaDTO s;

			while (rs.next()) {
				s = new SisuSeriyaDTO();
				s.setServiceRefNo(rs.getString("sps_service_ref_no"));
				data.add(s);
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
	public List<SisuSeriyaDTO> getApprovedOperatorDepoNameListForTeam() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct sps_operator_name from public.nt_m_sisu_permit_hol_service_info "
					+ "WHERE sps_operator_name is not null and sps_operator_name !='' and sps_status='A' and sps_renewal_status not in ('I')  and  "
					+ "(sps_service_agreement_issuedby IS NOT NULL and sps_permit_sticker_issuedby IS NOT NULL and sps_issue_logsheets_issuedby IS NOT null) order by sps_operator_name ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			SisuSeriyaDTO s;

			while (rs.next()) {
				s = new SisuSeriyaDTO();
				s.setNameOfOperator(rs.getString("sps_operator_name"));
				data.add(s);
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
	public List<SisuSeriyaDTO> getApprovedServiceNoList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct sps_service_no from public.nt_m_sisu_permit_hol_service_info "
					+ "WHERE sps_service_no is not null and sps_service_no !='' and  sps_status='A'   and  (sps_service_agreement_issuedby IS NULL or sps_permit_sticker_issuedby IS NULL or sps_issue_logsheets_issuedby IS null)   order by sps_service_no desc ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			SisuSeriyaDTO s;

			while (rs.next()) {
				s = new SisuSeriyaDTO();
				s.setServiceNo(rs.getString("sps_service_no"));
				data.add(s);
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
	public List<SisuSeriyaDTO> getApprovedSisuSeriyaData(SisuSeriyaDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> sisuSeriyaList = new ArrayList<SisuSeriyaDTO>();
		String WHERE_SQL = "";

		if (dto.getServiceNo() != null && !dto.getServiceNo().equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE sps_service_no = " + "'" + dto.getServiceNo()
					+ "'and sps_status='A' and sps_service_ref_no is not null and sps_service_ref_no != ''";
		}

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select distinct sps_request_no, sps_service_ref_no,sps_service_no,"
					+ "sps_service_end_date ,sps_status, sps_operator_name, sps_is_checked, sps_is_recommended from public.nt_m_sisu_permit_hol_service_info "
					+ "inner join public.nt_m_sisu_requestor_info on nt_m_sisu_requestor_info.sri_request_no = nt_m_sisu_permit_hol_service_info.sps_request_no "
					+ "inner join public.nt_t_subsidy_task_det on nt_t_subsidy_task_det.tsd_service_no=nt_m_sisu_permit_hol_service_info.sps_service_no "
					+ WHERE_SQL;

			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();
			SisuSeriyaDTO e;

			while (rs.next()) {
				e = new SisuSeriyaDTO();
				e.setRequestNo(rs.getString("sps_request_no"));
				e.setServiceRefNo(rs.getString("sps_service_ref_no"));
				e.setServiceNo(rs.getString("sps_service_no"));
				e.setNameOfOperator(rs.getString("sps_operator_name"));

				Timestamp ts = rs.getTimestamp("sps_service_end_date");

				if (ts != null) {
					Date date = new Date();
					date.setTime(ts.getTime());
					String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);

					e.setServiceEndDateVal(formattedDate);
				} else {
					e.setServiceEndDateVal("N/A");
				}

				String approveStatusCode = rs.getString("sps_status");
				String approveStatus = "";

				if (approveStatusCode != null) {
					if (approveStatusCode.equals("A")) {
						approveStatus = "APPROVED";
						e.setEditStatus(false);
					} else if (approveStatusCode.equals("R")) {
						approveStatus = "REJECTED";
						e.setEditStatus(false);
					} else if (approveStatusCode.equals("P")) {
						approveStatus = "PENDING";
						e.setEditStatus(true);
					}
				}

				e.setStatus(approveStatusCode);
				e.setStatusDes(approveStatus);
				e.setIsChecked(rs.getString("sps_is_checked"));
				e.setIsRecommended(rs.getString("sps_is_recommended"));

				sisuSeriyaList.add(e);
			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return sisuSeriyaList;
	}

	@Override
	public boolean generateLogSheetRef(SisuSeriyaDTO sisuSeriyaDTO, String logSheetYear, int logSheetCopies,
			String loginUser, List<LogSheetDTO> newLogSheetList) {
		long logRefSeq;

		logRefSeq = saveToLogSheets(sisuSeriyaDTO.getServiceRefNo(), logSheetYear, logSheetCopies, loginUser,
				sisuSeriyaDTO.getServiceTypeCode());
		
		if(logRefSeq != 0) {
			boolean success = saveToLogSheetSummary(logRefSeq, logSheetYear, loginUser, newLogSheetList);
			return success;
		}

		return false;
//		for (LogSheetDTO temp : newLogSheetList) {
//			if (temp.getLogSheetRefNo() != null && !temp.getLogSheetRefNo().isEmpty()
//					&& !temp.getLogSheetRefNo().equalsIgnoreCase("")) {
//				saveToLogSheetSummary(logRefSeq, logSheetYear, temp.getLogSheetRefNo(), loginUser);
//			}
//		}

	}

	private void updateToLogSheetsDet(LogSheetDTO logSheetDetDTO, int newNoOfCopies, String serviceRefNo,
			String logSheetYear, String loginUser, String serviceTypeCode) {
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();
			String sql = "UPDATE public.nt_m_log_sheets SET  los_no_of_copies=?,  los_modified_by=?, los_modified_date=? WHERE los_std_service_ref_no=?;";
			
			try {
				stmt = con.prepareStatement(sql);

				stmt.setInt(1, newNoOfCopies);
				stmt.setString(2, loginUser);
				stmt.setTimestamp(3, timestamp);
				stmt.setString(4, serviceRefNo);

				stmt.executeUpdate();

				con.commit();
			} catch (SQLException e) {
				con.rollback();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
	}

	private LogSheetDTO getLogSheetValuesForSelectedRefNo(SisuSeriyaDTO sisuSeriyaDTO, String year) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		LogSheetDTO data = new LogSheetDTO();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT los_seq, los_no_of_copies FROM public.nt_m_log_sheets where los_std_service_ref_no=? and los_subsidy_service_type_code=? and los_year =? ";

			ps = con.prepareStatement(query);
			ps.setString(1, sisuSeriyaDTO.getServiceRefNo());
			ps.setString(2, sisuSeriyaDTO.getServiceTypeCode());
			ps.setString(3, year);
			rs = ps.executeQuery();

			while (rs.next()) {
				data.setLogSheetDetSeq(rs.getLong("los_seq"));
				data.setNoOfCopies(rs.getInt("los_no_of_copies"));
				break;
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
	public long saveToLogSheets(String serviceRefNo, String logSheetYear, int logSheetCopies, String loginUser,
			String serviceType) {

		long seqNo = 0;

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			java.util.Date date = new java.util.Date();
			Timestamp timestamp = new Timestamp(date.getTime());

			seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_m_log_sheets");

			String sql = "INSERT INTO public.nt_m_log_sheets"
					+ "(los_seq,los_std_service_ref_no,los_year,los_no_of_copies,los_created_by,los_created_date,los_subsidy_service_type_code)"
					+ "VALUES(?, ?, ?, ?, ?, ?,?)";

			stmt = con.prepareStatement(sql);

			try {
				stmt.setLong(1, seqNo);
				stmt.setString(2, serviceRefNo);
				stmt.setString(3, logSheetYear);
				stmt.setInt(4, logSheetCopies);
				stmt.setString(5, loginUser);
				stmt.setTimestamp(6, timestamp);
				stmt.setString(7, serviceType);

				stmt.executeUpdate();
				con.commit();
			} catch (SQLException e) {
				con.rollback();
			}

			return seqNo;

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return seqNo;

	}

	public boolean saveToLogSheetSummary(long logRefSeq, String logSheetYear, String logRefNo, String loginUser) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			java.util.Date date = new java.util.Date();
			Timestamp timestamp = new Timestamp(date.getTime());

			long seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_m_log_sheet_summary");

			String sql = "INSERT INTO public.nt_m_log_sheet_summary"
					+ "(lss_seq,lss_log_sheet_master_seq,lss_log_ref_no,lss_created_by,lss_created_date,lss_year)"
					+ "VALUES(?, ?, ?, ?, ?, ?)";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setLong(2, logRefSeq);
			stmt.setString(3, logRefNo);
			stmt.setString(4, loginUser);
			stmt.setTimestamp(5, timestamp);
			stmt.setString(6, logSheetYear);

			int rowsAffected = stmt.executeUpdate();

			con.commit();

			return rowsAffected > 0;

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return false;

	}

	public boolean saveToLogSheetSummary(long logRefSeq, String logSheetYear, String loginUser,List<LogSheetDTO> newLogSheetList) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			java.util.Date date = new java.util.Date();
			Timestamp timestamp = new Timestamp(date.getTime());

			String sql = "INSERT INTO public.nt_m_log_sheet_summary"
					+ "(lss_seq,lss_log_sheet_master_seq,lss_log_ref_no,lss_created_by,lss_created_date,lss_year)"
					+ "VALUES(?, ?, ?, ?, ?, ?)";

			try {
				stmt = con.prepareStatement(sql);
				for (LogSheetDTO temp : newLogSheetList) {
					if (temp.getLogSheetRefNo() != null && !temp.getLogSheetRefNo().isEmpty()
							&& !temp.getLogSheetRefNo().equalsIgnoreCase("")) {
						long seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_m_log_sheet_summary");
						stmt.setLong(1, seqNo);
						stmt.setLong(2, logRefSeq);
						stmt.setString(3, temp.getLogSheetRefNo());
						stmt.setString(4, loginUser);
						stmt.setTimestamp(5, timestamp);
						stmt.setString(6, logSheetYear);
						stmt.addBatch();
					}
				}
			    stmt.executeBatch();
			    con.commit();
			    return true;
			} catch (SQLException e) {
			    con.rollback();
			    e.printStackTrace();
			    return false;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
	}
	
	public String generateLogSheetRefNo(int year) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;

		String currYear = Integer.toString(year).substring(2, 4);
		String strAppNo = "";
		String lastyr = "00";

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT lss_log_ref_no "
					+ " FROM public.nt_m_log_sheet_summary WHERE lss_log_ref_no LIKE 'LOG%'" + " ORDER BY lss_seq desc "
					+ " LIMIT 1";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("lss_log_ref_no");
			}
			if (result != null) {

				lastyr = result.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String ApprecordcountN = "00001";
					strAppNo = "LOG" + currYear + ApprecordcountN;
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
					strAppNo = "LOG" + currYear + ApprecordcountN;
				}
			} else
				strAppNo = "LOG" + currYear + "00001";

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return strAppNo;
	}

	@Override
	public List<LogSheetDTO> getLogSheetsByServiceRefNo(String serviceRefNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<LogSheetDTO> list = new ArrayList<LogSheetDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select A.lss_log_ref_no, A.lss_seq from public.nt_m_log_sheet_summary A "
					+ "inner join public.nt_m_log_sheets B on A.lss_log_sheet_master_seq = B.los_seq "
					+ "where B.los_std_service_ref_no = ? order by A.lss_log_ref_no DESC";

			ps = con.prepareStatement(query);
			ps.setString(1, serviceRefNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				LogSheetDTO logSheetDTO = new LogSheetDTO();

				logSheetDTO.setLogSheetRefNo(rs.getString("lss_log_ref_no"));
				logSheetDTO.setLogSheetSeqNo(rs.getInt("lss_seq"));
				list.add(logSheetDTO);
			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return list;
	}

	@Override
	public void updateIssueServiceAgreementPermitStickerLogSheets(SisuSeriyaDTO ssDTO) {
		java.util.Date date = new java.util.Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_sisu_permit_hol_service_info SET ";

			switch (ssDTO.getIssueType()) {
			case 1:
				String getServiceAgreementIssuedDate = (dateFormat.format(ssDTO.getServiceAgreementIssuedDate()));
				sql = sql + "sps_is_issue_service_agreement='Y',sps_service_agreement_issuedby='"
						+ ssDTO.getCurrentUser() + "',sps_service_agreement_issued_date='"
						+ getServiceAgreementIssuedDate + "',";
				break;
			case 2:
				String getPermitStickerIssuedDate = (dateFormat.format(ssDTO.getPermitStickerIssuedDate()));
				sql = sql + "sps_is_issue_permit_sticker='Y',sps_permit_sticker_issuedby='" + ssDTO.getCurrentUser()
						+ "',sps_permit_sticker_issued_date='" + getPermitStickerIssuedDate + "',";
				break;
			case 3:
				String getLogSheetsIssuedDate = (dateFormat.format(ssDTO.getLogSheetsIssuedDate()));
				sql = sql + "sps_is_issue_logsheets='Y',sps_issue_logsheets_issuedby='" + ssDTO.getCurrentUser()
						+ "',sps_issue_logsheets_issued_date='" + getLogSheetsIssuedDate + "',";
				break;
			}

			sql = sql + " sps_modified_by=?,sps_modified_date=? WHERE sps_service_ref_no=?";

			try {
				stmt = con.prepareStatement(sql);

				stmt.setString(1, ssDTO.getCurrentUser());

				stmt.setTimestamp(2, timestamp);

				stmt.setString(3, ssDTO.getServiceRefNo());

				stmt.executeUpdate();

				con.commit();
			}catch (SQLException e) {
				con.rollback();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

	}

	@Override
	public List<LogSheetDTO> getLogSheetsByServiceRefNoAndYear(String serviceRefNo, String logSheetYear) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<LogSheetDTO> list = new ArrayList<LogSheetDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select A.lss_log_ref_no from public.nt_m_log_sheet_summary A inner join public.nt_m_log_sheets B on A.lss_log_sheet_master_seq = B.los_seq where B.los_std_service_ref_no = ? and A.lss_year = ? order by A.lss_seq DESC";

			ps = con.prepareStatement(query);
			ps.setString(1, serviceRefNo);
			ps.setString(2, logSheetYear);
			rs = ps.executeQuery();

			while (rs.next()) {
				LogSheetDTO logSheetDTO = new LogSheetDTO();

				logSheetDTO.setLogSheetRefNo(rs.getString("lss_log_ref_no"));

				list.add(logSheetDTO);
			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return list;
	}

	@Override
	public List<LogSheetDTO> getLogSheetsByServiceRefNoYearNameOfOperatorBusNo(String serviceRefNo, String logSheetYear,
			String nameOfOperator, String busNo, String serviceTypeCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<LogSheetDTO> list = new ArrayList<LogSheetDTO>();

		try {
			con = ConnectionManager.getConnection();
			if (serviceTypeCode != null && !serviceTypeCode.trim().equals("")) {

				if (serviceTypeCode.equals("S01")) {
					String whereClause = "";
					if (nameOfOperator != null && !nameOfOperator.trim().equals("")) {
						whereClause = whereClause + " and c.sps_operator_name ='" + nameOfOperator + "' ";

					}
					if (busNo != null && !busNo.trim().equals("")) {
						whereClause = whereClause + " and c.sps_bus_no ='" + busNo + "' ";

					}

					String query = "select a.lss_log_ref_no from public.nt_m_log_sheet_summary a inner join public.nt_m_log_sheets b on a.lss_log_sheet_master_seq = b.los_seq left join public.nt_m_sisu_permit_hol_service_info c on b.los_std_service_ref_no = c.sps_service_ref_no where b.los_std_service_ref_no = ? and a.lss_year = ? and b.los_subsidy_service_type_code =? "
							+ whereClause + " order by a.lss_seq DESC";

					ps = con.prepareStatement(query);
					ps.setString(1, serviceRefNo);
					ps.setString(2, logSheetYear);
					ps.setString(3, serviceTypeCode);
					rs = ps.executeQuery();

					while (rs.next()) {
						LogSheetDTO logSheetDTO = new LogSheetDTO();
						logSheetDTO.setLogSheetRefNo(rs.getString("lss_log_ref_no"));
						list.add(logSheetDTO);
					}
				} else if (serviceTypeCode.equals("S02")) {
					String whereClause = "";
					if (nameOfOperator != null && !nameOfOperator.trim().equals("")) {
						whereClause = whereClause + "  and c.gps_operator_name ='" + nameOfOperator + "' ";

					}
					if (busNo != null && !busNo.trim().equals("")) {
						whereClause = whereClause + " and c.gps_bus_no ='" + busNo + "' ";

					}

					String query = "select a.lss_log_ref_no from public.nt_m_log_sheet_summary a inner join public.nt_m_log_sheets b on a.lss_log_sheet_master_seq = b.los_seq left join public.nt_m_gami_permit_hol_service_info c on b.los_std_service_ref_no = c.gps_service_ref_no where b.los_std_service_ref_no = ? and a.lss_year = ? and b.los_subsidy_service_type_code =? "
							+ whereClause + " order by a.lss_seq DESC";

					ps = con.prepareStatement(query);
					ps.setString(1, serviceRefNo);
					ps.setString(2, logSheetYear);
					ps.setString(3, serviceTypeCode);
					rs = ps.executeQuery();

					while (rs.next()) {
						LogSheetDTO logSheetDTO = new LogSheetDTO();
						logSheetDTO.setLogSheetRefNo(rs.getString("lss_log_ref_no"));
						list.add(logSheetDTO);
					}
				} else if (serviceTypeCode.equals("S03")) {
					String whereClause = "";
					if (nameOfOperator != null && !nameOfOperator.trim().equals("")) {
						whereClause = whereClause + "  and c.gps_operator_name ='" + nameOfOperator + "' ";

					}
					if (busNo != null && !busNo.trim().equals("")) {
						whereClause = whereClause + " and c.gps_bus_no ='" + busNo + "' ";

					}

					String query = "select a.lss_log_ref_no from public.nt_m_log_sheet_summary a inner join public.nt_m_log_sheets b on a.lss_log_sheet_master_seq = b.los_seq left join public.nt_m_nri_requester_info c on b.los_std_service_ref_no = c.nri_service_ref_no where b.los_std_service_ref_no = ? and a.lss_year = ? and b.los_subsidy_service_type_code =? "
							+ whereClause + " order by a.lss_seq DESC";

					ps = con.prepareStatement(query);
					ps.setString(1, serviceRefNo);
					ps.setString(2, logSheetYear);
					ps.setString(3, serviceTypeCode);
					rs = ps.executeQuery();

					while (rs.next()) {
						LogSheetDTO logSheetDTO = new LogSheetDTO();
						logSheetDTO.setLogSheetRefNo(rs.getString("lss_log_ref_no"));
						list.add(logSheetDTO);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return list;
	}

	@Override
	public List<SisuSeriyaDTO> getSisuSeriyaToIssue() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> sisuSeriyaList = new ArrayList<SisuSeriyaDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select distinct sps_request_no, sps_service_ref_no,sps_service_no,sps_bus_no,"
					+ " to_char(sps_service_end_date,'dd/MM/yyyy') as sps_service_end_date ,sps_status, sps_operator_name,sps_is_issue_service_agreement,"
					+ " sps_is_issue_permit_sticker," + " sps_is_issue_logsheets," + " sps_service_agreement_issuedby,"
					+ " sps_permit_sticker_issuedby," + " sps_issue_logsheets_issuedby,"
					+ " sps_service_agreement_issued_date," + " sps_permit_sticker_issued_date,"
					+ " sps_issue_logsheets_issued_date from public.nt_m_sisu_permit_hol_service_info where sps_status='A' and  (sps_service_agreement_issuedby IS NULL "
					+ "				or sps_permit_sticker_issuedby IS NULL or sps_issue_logsheets_issuedby IS null)";

			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();
			SisuSeriyaDTO e;

			while (rs.next()) {
				e = new SisuSeriyaDTO();
				e.setRequestNo(rs.getString("sps_request_no"));
				e.setServiceRefNo(rs.getString("sps_service_ref_no"));
				e.setServiceNo(rs.getString("sps_service_no"));
				e.setBusRegNo(rs.getString("sps_bus_no"));
				e.setNameOfOperator(rs.getString("sps_operator_name"));
				e.setServiceEndDateVal(rs.getString("sps_service_end_date"));
				String approveStatusCode = rs.getString("sps_status");
				String approveStatus = "";

				if (approveStatusCode != null) {
					if (approveStatusCode.equals("A")) {
						approveStatus = "APPROVED";
					} else if (approveStatusCode.equals("R")) {
						approveStatus = "REJECTED";
					} else if (approveStatusCode.equals("P")) {
						approveStatus = "PENDING";
					}
				}

				e.setStatus(approveStatus);

				e.setServiceAgreementIssuedBy(rs.getString("sps_service_agreement_issuedby"));
				e.setPermitStickerIssuedBy(rs.getString("sps_permit_sticker_issuedby"));
				e.setLogSheetsIssuedBy(rs.getString("sps_issue_logsheets_issuedby"));

				if (rs.getString("sps_is_issue_service_agreement") != null
						&& rs.getString("sps_is_issue_service_agreement").equalsIgnoreCase("Y")) {
					e.setIssuedServiceAgreement(true);
				} else {
					e.setIssuedServiceAgreement(false);
				}

				if (rs.getString("sps_is_issue_permit_sticker") != null
						&& rs.getString("sps_is_issue_permit_sticker").equalsIgnoreCase("Y")) {
					e.setIssuedPermitSticker(true);
				} else {
					e.setIssuedPermitSticker(false);
				}

				if (rs.getString("sps_is_issue_logsheets") != null
						&& rs.getString("sps_is_issue_logsheets").equalsIgnoreCase("Y")) {
					e.setIssuedLogSheets(true);
				} else {
					e.setIssuedLogSheets(false);
				}

				java.util.Date currentSystemDate = new java.util.Date();
				String sps_service_agreement_issued_dateString = rs.getString("sps_service_agreement_issued_date");
				if (sps_service_agreement_issued_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setServiceAgreementIssuedDate = originalFormat.parse(sps_service_agreement_issued_dateString);
					e.setServiceAgreementIssuedDate(setServiceAgreementIssuedDate);
				} else {
					// set to current system date
					e.setServiceAgreementIssuedDate(currentSystemDate);
				}

				String sps_permit_sticker_issued_dateString = rs.getString("sps_permit_sticker_issued_date");
				if (sps_permit_sticker_issued_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setPermitStickerIssuedDate = originalFormat.parse(sps_permit_sticker_issued_dateString);
					e.setPermitStickerIssuedDate(setPermitStickerIssuedDate);
				} else {
					// set to current system date
					e.setPermitStickerIssuedDate(currentSystemDate);
				}

				String sps_issue_logsheets_issued_dateString = rs.getString("sps_issue_logsheets_issued_date");
				if (sps_issue_logsheets_issued_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setLogSheetsIssuedDate = originalFormat.parse(sps_issue_logsheets_issued_dateString);
					e.setLogSheetsIssuedDate(setLogSheetsIssuedDate);
				} else {
					// set to current system date
					e.setLogSheetsIssuedDate(currentSystemDate);
				}

				sisuSeriyaList.add(e);
			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return sisuSeriyaList;
	}

	@Override
	public List<SisuSeriyaDTO> getSisuSeriyaToIssueBySearch(SisuSeriyaDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> sisuSeriyaList = new ArrayList<SisuSeriyaDTO>();
		String WHERE_SQL = "";
		boolean whereadded = false;

		if (dto.getRequestNo() != null && !dto.getRequestNo().equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE sps_request_no = " + "'" + dto.getRequestNo()
					+ "' and sps_status='A'  and  (sps_service_agreement_issuedby IS NULL "
					+ "				or sps_permit_sticker_issuedby IS NULL or sps_issue_logsheets_issuedby IS null) and sps_service_ref_no is not null and sps_service_ref_no != ''";
			whereadded = true;
		}
		if (dto.getServiceRefNo() != null && !dto.getServiceRefNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND sps_service_ref_no = " + "'" + dto.getServiceRefNo() + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE sps_service_ref_no = " + "'" + dto.getServiceRefNo()
						+ "' and sps_status='A'  and  (sps_service_agreement_issuedby IS NULL "
						+ "				or sps_permit_sticker_issuedby IS NULL or sps_issue_logsheets_issuedby IS null) and sps_service_ref_no is not null and sps_service_ref_no != ''";
				whereadded = true;
			}
		}

		if (dto.getRequestStartDate() != null && dto.getRequestEndDate() != null) {
			Timestamp startDate = new Timestamp(dto.getRequestStartDate().getTime());
			Timestamp endDate = new Timestamp(dto.getRequestEndDate().getTime());
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + "and nt_m_sisu_requestor_info.sri_request_date >= " + "' " + startDate
						+ "' and nt_m_sisu_requestor_info.sri_request_date <= " + "'" + endDate + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + "WHERE nt_m_sisu_requestor_info.sri_request_date >= " + "' " + startDate
						+ "' and nt_m_sisu_requestor_info.sri_request_date <= " + "'" + endDate
						+ "' and sps_status='A'  and  (sps_service_agreement_issuedby IS NULL "
						+ "				or sps_permit_sticker_issuedby IS NULL or sps_issue_logsheets_issuedby IS null) and sps_service_ref_no is not null and sps_service_ref_no != ''";
				whereadded = true;
			}
		} else if (dto.getRequestEndDate() != null) {

			Timestamp endDate = new Timestamp(dto.getRequestEndDate().getTime());
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " and nt_m_sisu_requestor_info.sri_request_date <= " + "'" + endDate + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE  nt_m_sisu_requestor_info.sri_request_date <= " + "'" + endDate
						+ "' and sps_status='A'  and  (sps_service_agreement_issuedby IS NULL "
						+ "				or sps_permit_sticker_issuedby IS NULL or sps_issue_logsheets_issuedby IS null) and sps_service_ref_no is not null and sps_service_ref_no != ''";
				whereadded = true;
			}
		} else if (dto.getRequestStartDate() != null) {
			Timestamp startDate = new Timestamp(dto.getRequestStartDate().getTime());

			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " and nt_m_sisu_requestor_info.sri_request_date >= " + "'" + startDate + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE nt_m_sisu_requestor_info.sri_request_date >= " + "'" + startDate
						+ "' and sps_status='A'  and  (sps_service_agreement_issuedby IS NULL "
						+ "				or sps_permit_sticker_issuedby IS NULL or sps_issue_logsheets_issuedby IS null) and sps_service_ref_no is not null and sps_service_ref_no != ''";
				whereadded = true;
			}
		}

		if (dto.getServiceNo() != null && !dto.getServiceNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND sps_service_no = " + "'" + dto.getServiceNo() + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE sps_service_no = " + "'" + dto.getServiceNo()
						+ "' and sps_status='A'  and  (sps_service_agreement_issuedby IS NULL "
						+ "				or sps_permit_sticker_issuedby IS NULL or sps_issue_logsheets_issuedby IS null) and sps_service_ref_no is not null and sps_service_ref_no != ''";
				whereadded = true;
			}
		}

		if (dto.getNameOfOperator() != null && !dto.getNameOfOperator().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND sps_operator_name = " + "'" + dto.getNameOfOperator() + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE sps_operator_name = " + "'" + dto.getNameOfOperator()
						+ "' and sps_status='A'  and  (sps_service_agreement_issuedby IS NULL "
						+ "				or sps_permit_sticker_issuedby IS NULL or sps_issue_logsheets_issuedby IS null) and sps_service_ref_no is not null and sps_service_ref_no != ''";
				whereadded = true;
			}
		}

		if (dto.getBusRegNo() != null && !dto.getBusRegNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND sps_bus_no = " + "'" + dto.getBusRegNo() + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE sps_bus_no = " + "'" + dto.getBusRegNo()
						+ "' and sps_status='A'  and  (sps_service_agreement_issuedby IS NULL "
						+ "				or sps_permit_sticker_issuedby IS NULL or sps_issue_logsheets_issuedby IS null) and sps_service_ref_no is not null and sps_service_ref_no != ''";
				whereadded = true;
			}
		}

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select distinct sps_request_no, sps_service_ref_no,sps_service_no,sps_bus_no,"
					+ " to_char(sps_service_end_date,'dd/MM/yyyy') as sps_service_end_date ,sps_status, sps_operator_name,"
					+ " sps_is_issue_service_agreement," + " sps_is_issue_permit_sticker," + " sps_is_issue_logsheets,"
					+ " sps_service_agreement_issuedby," + " sps_permit_sticker_issuedby,"
					+ " sps_issue_logsheets_issuedby," + " sps_service_agreement_issued_date,"
					+ " sps_permit_sticker_issued_date," + " sps_issue_logsheets_issued_date"
					+ " from public.nt_m_sisu_permit_hol_service_info "
					+ "inner join public.nt_m_sisu_requestor_info on nt_m_sisu_requestor_info.sri_request_no = nt_m_sisu_permit_hol_service_info.sps_request_no "
					+ WHERE_SQL;

			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();
			SisuSeriyaDTO e;

			while (rs.next()) {
				e = new SisuSeriyaDTO();
				e.setRequestNo(rs.getString("sps_request_no"));
				e.setServiceRefNo(rs.getString("sps_service_ref_no"));
				e.setServiceNo(rs.getString("sps_service_no"));
				e.setBusRegNo(rs.getString("sps_bus_no"));
				e.setNameOfOperator(rs.getString("sps_operator_name"));

				e.setServiceEndDateVal(rs.getString("sps_service_end_date"));

				String approveStatusCode = rs.getString("sps_status");
				String approveStatus = "";

				if (approveStatusCode != null) {
					if (approveStatusCode.equals("A")) {
						approveStatus = "APPROVED";
					} else if (approveStatusCode.equals("R")) {
						approveStatus = "REJECTED";
					} else if (approveStatusCode.equals("P")) {
						approveStatus = "PENDING";
					}
				}

				e.setStatus(approveStatus);

				e.setServiceAgreementIssuedBy(rs.getString("sps_service_agreement_issuedby"));
				e.setPermitStickerIssuedBy(rs.getString("sps_permit_sticker_issuedby"));
				e.setLogSheetsIssuedBy(rs.getString("sps_issue_logsheets_issuedby"));

				if (rs.getString("sps_is_issue_service_agreement") != null
						&& rs.getString("sps_is_issue_service_agreement").equalsIgnoreCase("Y")) {
					e.setIssuedServiceAgreement(true);
				} else {
					e.setIssuedServiceAgreement(false);
				}

				if (rs.getString("sps_is_issue_permit_sticker") != null
						&& rs.getString("sps_is_issue_permit_sticker").equalsIgnoreCase("Y")) {
					e.setIssuedPermitSticker(true);
				} else {
					e.setIssuedPermitSticker(false);
				}

				if (rs.getString("sps_is_issue_logsheets") != null
						&& rs.getString("sps_is_issue_logsheets").equalsIgnoreCase("Y")) {
					e.setIssuedLogSheets(true);
				} else {
					e.setIssuedLogSheets(false);
				}

				java.util.Date currentSystemDate = new java.util.Date();
				String sps_service_agreement_issued_dateString = rs.getString("sps_service_agreement_issued_date");
				if (sps_service_agreement_issued_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setServiceAgreementIssuedDate = originalFormat.parse(sps_service_agreement_issued_dateString);
					e.setServiceAgreementIssuedDate(setServiceAgreementIssuedDate);
				} else {
					// set to current system date
					e.setServiceAgreementIssuedDate(currentSystemDate);
				}

				String sps_permit_sticker_issued_dateString = rs.getString("sps_permit_sticker_issued_date");
				if (sps_permit_sticker_issued_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setPermitStickerIssuedDate = originalFormat.parse(sps_permit_sticker_issued_dateString);
					e.setPermitStickerIssuedDate(setPermitStickerIssuedDate);
				} else {
					// set to current system date
					e.setPermitStickerIssuedDate(currentSystemDate);
				}

				String sps_issue_logsheets_issued_dateString = rs.getString("sps_issue_logsheets_issued_date");
				if (sps_issue_logsheets_issued_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setLogSheetsIssuedDate = originalFormat.parse(sps_issue_logsheets_issued_dateString);
					e.setLogSheetsIssuedDate(setLogSheetsIssuedDate);
				} else {
					// set to current system date
					e.setLogSheetsIssuedDate(currentSystemDate);
				}

				sisuSeriyaList.add(e);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return sisuSeriyaList;
	}

	@Override
	public List<LogSheetMaintenanceDTO> getSisuSeriyaForBulkPayment() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<LogSheetMaintenanceDTO> sisuSeriyaList = new ArrayList<LogSheetMaintenanceDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select B.lss_seq,B.lss_log_sheet_master_seq, B.lss_log_ref_no,  A.los_std_service_ref_no, B.lss_year,  B.lss_month, B.lss_received_date as lss_received_date , B.lss_no_of_turns,  B.lss_no_of_turns_gps,  B.lss_balance_amt, B.lss_remarks from  public.nt_m_log_sheets A  inner join public.nt_m_log_sheet_summary B on   A.los_seq = B.lss_log_sheet_master_seq  where B.lss_balance_amt > 0 AND B.lss_is_approverej = 'Y'";

			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();
			LogSheetMaintenanceDTO e;

			while (rs.next()) {
				e = new LogSheetMaintenanceDTO();
				e.setLogMasterSeq(rs.getLong("lss_log_sheet_master_seq"));
				e.setLogRefNo(rs.getString("lss_log_ref_no"));
				e.setReferenceNo(rs.getString("los_std_service_ref_no"));
				e.setYear(rs.getString("lss_year"));
				e.setMonth(rs.getString("lss_month"));
				e.setReceivedDate(rs.getString("lss_received_date"));
				e.setNoOfTurns(rs.getInt("lss_no_of_turns"));
				e.setNoOfTurnsinGPS(rs.getInt("lss_no_of_turns_gps"));
				e.setPayment(rs.getInt("lss_balance_amt"));
				e.setSpecialRemark(rs.getString("lss_remarks"));
				e.setSeq(rs.getLong("lss_seq"));

				sisuSeriyaList.add(e);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return sisuSeriyaList;
	}

	@Override
	public List<LogSheetMaintenanceDTO> getSisuSeriyaForBulkPaymentBySearch(SisuSeriyaDTO dto, String year) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<LogSheetMaintenanceDTO> sisuSeriyaList = new ArrayList<LogSheetMaintenanceDTO>();
		String WHERE_SQL = "";
		boolean whereadded = false;

		if (dto.getRequestStartDate() != null && dto.getRequestEndDate() != null) {
			Timestamp startDate = new Timestamp(dto.getRequestStartDate().getTime());
			Timestamp endDate = new Timestamp(dto.getRequestEndDate().getTime());
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " and B.lss_received_date >= " + "' " + startDate
						+ "' and B.lss_received_date <= " + "'" + endDate + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + "WHERE B.lss_received_date >= " + "' " + startDate
						+ "' and B.lss_received_date <= " + "'" + endDate
						+ "' AND B.lss_balance_amt > 0 AND B.lss_is_approverej = 'Y' ";
				whereadded = true;
			}
		} else if (dto.getRequestEndDate() != null) {

			Timestamp endDate = new Timestamp(dto.getRequestEndDate().getTime());
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " and B.lss_received_date <= " + "'" + endDate + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE  B.lss_received_date <= " + "'" + endDate
						+ "' AND B.lss_balance_amt > 0 AND B.lss_is_approverej = 'Y' ";
				whereadded = true;
			}
		} else if (dto.getRequestStartDate() != null) {
			Timestamp startDate = new Timestamp(dto.getRequestStartDate().getTime());

			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " and B.lss_received_date >= " + "'" + startDate + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE B.lss_received_date >= " + "'" + startDate
						+ "' AND B.lss_balance_amt > 0 AND B.lss_is_approverej = 'Y' ";
				whereadded = true;
			}
		}

		if (dto.getServiceTypeCode() != null && !dto.getServiceTypeCode().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND A.los_subsidy_service_type_code = " + "'" + dto.getServiceTypeCode()
						+ "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE A.los_subsidy_service_type_code = " + "'" + dto.getServiceTypeCode()
						+ "' AND B.lss_balance_amt > 0 AND B.lss_is_approverej = 'Y' ";
				whereadded = true;
			}
		}

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select B.lss_seq,B.lss_log_sheet_master_seq, B.lss_log_ref_no,  A.los_std_service_ref_no, B.lss_year,  B.lss_month, B.lss_received_date as lss_received_date, B.lss_no_of_turns,  B.lss_no_of_turns_gps,  B.lss_balance_amt, B.lss_remarks from  public.nt_m_log_sheets A  inner join public.nt_m_log_sheet_summary B on   A.los_seq = B.lss_log_sheet_master_seq "
					+ WHERE_SQL + "AND B.lss_year = '" + year + "'";

			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();
			LogSheetMaintenanceDTO e;

			while (rs.next()) {
				e = new LogSheetMaintenanceDTO();
				e.setLogMasterSeq(rs.getLong("lss_log_sheet_master_seq"));
				e.setLogRefNo(rs.getString("lss_log_ref_no"));
				e.setReferenceNo(rs.getString("los_std_service_ref_no"));
				e.setYear(rs.getString("lss_year"));
				e.setMonth(rs.getString("lss_month"));
				e.setReceivedDate(rs.getString("lss_received_date"));
				e.setNoOfTurns(rs.getInt("lss_no_of_turns"));
				e.setNoOfTurnsinGPS(rs.getInt("lss_no_of_turns_gps"));
				e.setPayment(rs.getInt("lss_balance_amt"));
				e.setSpecialRemark(rs.getString("lss_remarks"));
				e.setSeq(rs.getLong("lss_seq"));

				sisuSeriyaList.add(e);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return sisuSeriyaList;
	}

	@Override
	public List<SisuSeriyaDTO> serviceTypeDropDown() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> returnList = new ArrayList<SisuSeriyaDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct st_code,st_description " + "from nt_r_subsidy_service_type "
					+ "order by st_description";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				SisuSeriyaDTO logDTO = new SisuSeriyaDTO();
				logDTO.setServiceTypeCode(rs.getString("st_code"));
				logDTO.setServiceTypeDes(rs.getString("st_description"));

				returnList.add(logDTO);
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
	public String bulkPaymentGenerateVoucher(SubSidyDTO subSidyDTO, List<LogSheetMaintenanceDTO> selectedLogSheetList,
			String loginUser) {
		String voucherNo = generatePaymentVoucherNo();

		long bulkMasterSeq = saveToBulkPaymentMaster(subSidyDTO, loginUser, voucherNo);

		System.out.println("Bulk sequence : " + bulkMasterSeq);
		logger.info("Bulk sequence : " + bulkMasterSeq);
		/*
		 * for (LogSheetMaintenanceDTO temp : selectedLogSheetList) {
		 * 
		 * saveToBulkPaymentDetail(bulkMasterSeq, subSidyDTO, loginUser, voucherNo,
		 * temp.getSeq(), temp.getReferenceNo(), temp.getVoucherAmount());
		 * 
		 * updateLogSheetSummary(loginUser, temp.getVoucherAmount(), temp.getSeq());
		 * 
		 * }
		 */

		/* changed method dhananjika.d (20/06/2024) */
		boolean success = false;
		boolean updateSuccess = false;

		if (bulkMasterSeq != 0) {
			success = saveToBulkPaymentDetail(bulkMasterSeq, subSidyDTO, loginUser, voucherNo, selectedLogSheetList);

			if (success) {
				updateSuccess = updateLogSheetSummary(loginUser, selectedLogSheetList);
			}
		}

		voucherNo = (success && updateSuccess) ? voucherNo : null;

		System.out.println("Voucher number : " + voucherNo);
		logger.info("Voucher number : " + voucherNo);
		return voucherNo;
	}

	/* created method dhananjika.d (20/06/2024) */
	private boolean updateLogSheetSummary(String loginUser, List<LogSheetMaintenanceDTO> selectList) {
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean success = true;
		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_log_sheet_summary SET lss_balance_amt = lss_balance_amt - ?, lss_pending_amt = lss_pending_amt + ?, lss_modified_by=?,lss_modified_date=? WHERE lss_seq=?";

			try {
				stmt = con.prepareStatement(sql);
				for (LogSheetMaintenanceDTO temp : selectList) {
					stmt.setBigDecimal(1, temp.getVoucherAmount());
					stmt.setBigDecimal(2, temp.getVoucherAmount());
					stmt.setString(3, loginUser);
					stmt.setTimestamp(4, timestamp);
					stmt.setLong(5, temp.getSeq());
					stmt.addBatch();
				}
				stmt.executeBatch();
				con.commit();
				System.out.println("Log sheet summery updated");
				logger.info("Log sheet summery updated");
			} catch (SQLException e) {
				con.rollback();
				success = false;
				System.out.println("Roll backed - nt_m_log_sheet_summary");
				logger.info("Roll backed - nt_m_log_sheet_summary");
				e.printStackTrace();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Error : " + ex.getMessage());
			success = false;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return success;

	}

	/*
	 * private void updateLogSheetSummary(String loginUser, BigDecimal
	 * voucherAmount, long logSeq) { java.util.Date date = new java.util.Date();
	 * Timestamp timestamp = new Timestamp(date.getTime());
	 * 
	 * Connection con = null; PreparedStatement stmt = null; ResultSet rs = null;
	 * 
	 * try { con = ConnectionManager.getConnection();
	 * 
	 * String sql =
	 * "UPDATE public.nt_m_log_sheet_summary SET lss_balance_amt = lss_balance_amt - ?, lss_pending_amt = lss_pending_amt + ?, lss_modified_by=?,lss_modified_date=? WHERE lss_seq=?"
	 * ;
	 * 
	 * stmt = con.prepareStatement(sql);
	 * 
	 * stmt.setBigDecimal(1, voucherAmount);
	 * 
	 * stmt.setBigDecimal(2, voucherAmount);
	 * 
	 * stmt.setString(3, loginUser);
	 * 
	 * stmt.setTimestamp(4, timestamp);
	 * 
	 * stmt.setLong(5, logSeq);
	 * 
	 * stmt.executeUpdate();
	 * 
	 * con.commit();
	 * 
	 * } catch (Exception ex) { ex.printStackTrace(); } finally {
	 * ConnectionManager.close(rs); ConnectionManager.close(stmt);
	 * ConnectionManager.close(con); }
	 * 
	 * }
	 */
	public String generatePaymentVoucherNo() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;

		int year = Year.now().getValue();
		String currYear = Integer.toString(year).substring(2, 4);
		String strAppNo = "";
		String lastyr = "00";

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT sbp_voucher_no " + " FROM public.nt_m_sm_bulk_payment "
					+ "INNER JOIN public.nt_t_sm_bulk_payment ON nt_t_sm_bulk_payment.sbpt_voucher_no = sbp_voucher_no "
					+ "ORDER BY sbp_created_date desc " + " LIMIT 1";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("sbp_voucher_no");
			}

			if (result != null) {

				lastyr = result.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String ApprecordcountN = "00001";
					strAppNo = "PAY" + currYear + ApprecordcountN;
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
					strAppNo = "PAY" + currYear + ApprecordcountN;
				}
			} else
				strAppNo = "PAY" + currYear + "00001";

			System.out.println("Voucher number " + strAppNo + " generated");
			logger.info("Voucher number " + strAppNo + " generated");
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Error : " + ex.getMessage());
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return strAppNo;
	}

	public long saveToBulkPaymentMaster(SubSidyDTO subSidyDTO, String loginUser, String voucherNo) {

		long seqNo = 0;

		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmtUpdate = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			java.util.Date date = new java.util.Date();
			Timestamp timestamp = new Timestamp(date.getTime());

			seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_m_sm_bulk_payment");

			String sql = "INSERT INTO public.nt_m_sm_bulk_payment"
					+ "(sbp_seq,sbp_owner_type,sbp_holding,sbp_voucher_no,sbp_tot_no_of_logs,sbp_tot_amt,sbp_voucher_amt,sbp_created_by,sbp_created_date,sbp_subsidy_type)"
					+ "VALUES(?, ?, ?, ?, ?, ?,?,?,?,?)";

			String updateSql = "UPDATE nt_m_sm_bulk_payment SET sbp_seq = ?, sbp_owner_type = ?,sbp_holding = ?,"
					+ "sbp_tot_no_of_logs = ?,sbp_tot_amt = ?,sbp_voucher_amt = ?,sbp_created_by = ?,sbp_created_date = ?,"
					+ "sbp_subsidy_type = ? WHERE sbp_voucher_no = ?;";

			stmt = con.prepareStatement(sql);
			stmtUpdate = con.prepareStatement(updateSql);

			try {
				stmtUpdate.setLong(1, seqNo);
				stmtUpdate.setString(2, subSidyDTO.getOwnerType());
				stmtUpdate.setBigDecimal(3, subSidyDTO.getHoldingPercentage());
				stmtUpdate.setInt(4, subSidyDTO.getTotalNoOfLogs());
				stmtUpdate.setInt(5, subSidyDTO.getTotalBalanceAmount());
				stmtUpdate.setBigDecimal(6, subSidyDTO.getVoucherAmount());
				stmtUpdate.setString(7, loginUser);
				stmtUpdate.setTimestamp(8, timestamp);
				stmtUpdate.setString(9, subSidyDTO.getServiceType());
				stmtUpdate.setString(10, voucherNo);

				int update = stmtUpdate.executeUpdate();

				System.out.println("Update count " + update);
				logger.info("Update count " + update);
				if (update < 1) {
					stmt.setLong(1, seqNo);
					stmt.setString(2, subSidyDTO.getOwnerType());
					stmt.setBigDecimal(3, subSidyDTO.getHoldingPercentage());
					stmt.setString(4, voucherNo);
					stmt.setInt(5, subSidyDTO.getTotalNoOfLogs());
					stmt.setInt(6, subSidyDTO.getTotalBalanceAmount());
					stmt.setBigDecimal(7, subSidyDTO.getVoucherAmount());
					stmt.setString(8, loginUser);
					stmt.setTimestamp(9, timestamp);
					stmt.setString(10, subSidyDTO.getServiceType());

					stmt.executeUpdate();

					System.out.println("Voucher data inserted");
					logger.info("Voucher data inserted");
				}
				con.commit();
			} catch (SQLException e) {
				con.rollback();
				seqNo = 0;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Error : " + ex.getMessage());
			seqNo = 0;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmtUpdate);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
		return seqNo;

	}

	/*
	 * private void saveToBulkPaymentDetail(long bulkMasterSeq, SubSidyDTO
	 * subSidyDTO, String loginUser, String voucherNo, long logSeq, String
	 * serviceRef, BigDecimal voucherAmount) {
	 * 
	 * long seqNo = 0;
	 * 
	 * Connection con = null; PreparedStatement stmt = null; ResultSet rs = null;
	 * 
	 * try { con = ConnectionManager.getConnection();
	 * 
	 * java.util.Date date = new java.util.Date(); Timestamp timestamp = new
	 * Timestamp(date.getTime());
	 * 
	 * seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_t_sm_bulk_payment");
	 * 
	 * String sql = "INSERT INTO public.nt_t_sm_bulk_payment" +
	 * "(sbpt_seq,sbpt_service_ref_no,sbpt_voucher_no,sbpt_log_summary_seq,sbpt_bulk_payment_m_seq,sbpt_created_by,sbpt_created_date,sbpt_voucher_amount)"
	 * + "VALUES(?, ?, ?, ?, ?, ?,?,?)";
	 * 
	 * stmt = con.prepareStatement(sql);
	 * 
	 * stmt.setLong(1, seqNo); stmt.setString(2, serviceRef); stmt.setString(3,
	 * voucherNo); stmt.setLong(4, logSeq); stmt.setLong(5, bulkMasterSeq);
	 * stmt.setString(6, loginUser); stmt.setTimestamp(7, timestamp);
	 * stmt.setBigDecimal(8, voucherAmount);
	 * 
	 * stmt.executeUpdate();
	 * 
	 * con.commit(); } catch (Exception ex) { ex.printStackTrace(); } finally {
	 * ConnectionManager.close(rs); ConnectionManager.close(stmt);
	 * ConnectionManager.close(con); } }
	 */

	/* created method dhananjika.d (20/06/2024) */
	private boolean saveToBulkPaymentDetail(long bulkMasterSeq, SubSidyDTO subSidyDTO, String loginUser,
			String voucherNo, List<LogSheetMaintenanceDTO> selectList) {

		long seqNo = 0;
		boolean success = true;

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			java.util.Date date = new java.util.Date();
			Timestamp timestamp = new Timestamp(date.getTime());

			String sql = "INSERT INTO public.nt_t_sm_bulk_payment"
					+ "(sbpt_seq,sbpt_service_ref_no,sbpt_voucher_no,sbpt_log_summary_seq,sbpt_bulk_payment_m_seq,sbpt_created_by,sbpt_created_date,sbpt_voucher_amount)"
					+ "VALUES(?, ?, ?, ?, ?, ?,?,?)";

			try {
				stmt = con.prepareStatement(sql);
				for (LogSheetMaintenanceDTO temp : selectList) {
					seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_t_sm_bulk_payment");
					stmt.setLong(1, seqNo);
					stmt.setString(2, temp.getReferenceNo());
					stmt.setString(3, voucherNo);
					stmt.setLong(4, temp.getSeq());
					stmt.setLong(5, bulkMasterSeq);
					stmt.setString(6, loginUser);
					stmt.setTimestamp(7, timestamp);
					stmt.setBigDecimal(8, temp.getVoucherAmount());
					stmt.addBatch();
				}
				stmt.executeBatch();
				con.commit();
				System.out.println("Voucher details inserted to the nt_t_sm_bulk_payment");
				logger.info("Voucher details inserted to the nt_t_sm_bulk_payment");
			} catch (SQLException e) {
				con.rollback();
				success = false;
				System.out.println("Roll backed - nt_t_sm_bulk_payment");
				logger.info("Roll backed - nt_t_sm_bulk_payment");
				e.printStackTrace();
			}
		} catch (Exception ex) {
			success = false;
			ex.printStackTrace();
			logger.error("Error : " + ex.getMessage());
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return success;
	}

	@Override
	public List<SisuSeriyaDTO> getDefaultValuesForRefNo() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SisuSeriyaDTO> returnList = new ArrayList<SisuSeriyaDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  sps_request_no, sps_service_ref_no, sps_operator_name, sps_service_end_date,  sps_service_no, sps_status, sps_renewal_status, sps_renewal_new_expire_date, sps_is_checked, sps_is_recommended, is_cancellation FROM public.nt_m_sisu_permit_hol_service_info where sps_renewal_status in ('A','P','R') and sps_service_no is not null and sps_service_no !='' order by sps_service_ref_no;";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				SisuSeriyaDTO sisuSeriyaDTO = new SisuSeriyaDTO();
				sisuSeriyaDTO.setRequestNo(rs.getString("sps_request_no"));
				sisuSeriyaDTO.setServiceRefNo(rs.getString("sps_service_ref_no"));
				sisuSeriyaDTO.setServiceNo(rs.getString("sps_service_no"));
				sisuSeriyaDTO.setNameOfOperator(rs.getString("sps_operator_name"));
				sisuSeriyaDTO.setExpiryDateVal(rs.getString("sps_service_end_date"));
				sisuSeriyaDTO.setServiceEndDateVal(rs.getString("sps_service_end_date"));
				sisuSeriyaDTO.setServiceRenewalStatus(rs.getString("sps_renewal_status"));
				sisuSeriyaDTO.setStatusCode(rs.getString("sps_status"));
				sisuSeriyaDTO.setStatus(rs.getString("sps_status"));
				String statusCode = sisuSeriyaDTO.getServiceRenewalStatus();
				if (statusCode.equals("A")) {
					sisuSeriyaDTO.setStatusDes("APPROVED");
					sisuSeriyaDTO.setDisabledNewExpiredDate(true);
				} else if (statusCode.equals("P")) {
					sisuSeriyaDTO.setStatusDes("PENDING");
					sisuSeriyaDTO.setDisabledNewExpiredDate(false);
				} else if (statusCode.equals("R")) {
					sisuSeriyaDTO.setStatusDes("REJECTED");
					sisuSeriyaDTO.setDisabledNewExpiredDate(true);
				}
				sisuSeriyaDTO.setNewExpiryDateObj(rs.getDate("sps_renewal_new_expire_date"));
				sisuSeriyaDTO.setIsChecked(rs.getString("sps_is_checked"));
				sisuSeriyaDTO.setIsRecommended(rs.getString("sps_is_recommended"));
				sisuSeriyaDTO.setCancellationStatus(rs.getString("is_cancellation"));
				returnList.add(sisuSeriyaDTO);
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
	public List<SisuSeriyaDTO> getRefNoList(Date renewalRequestStartDate, Date renewalRequestEndDate) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String dateFormat = "yyyy-MM-dd";
		SimpleDateFormat frm = new SimpleDateFormat(dateFormat);
		List<SisuSeriyaDTO> returnList = new ArrayList<SisuSeriyaDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT sps_service_ref_no, sps_service_end_date FROM public.nt_m_sisu_permit_hol_service_info where sps_renewal_status='P' and sps_service_no is not null order by sps_service_ref_no;";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				SisuSeriyaDTO sisuSeriyaDTO = new SisuSeriyaDTO();
				sisuSeriyaDTO.setServiceRefNo(rs.getString("sps_service_ref_no"));
				sisuSeriyaDTO.setExpiryDateVal(rs.getString("sps_service_end_date"));
				if (sisuSeriyaDTO.getExpiryDateVal() != null) {
					Date expiredDateObj = frm.parse(sisuSeriyaDTO.getExpiryDateVal());
					if (renewalRequestStartDate.compareTo(expiredDateObj) < 0) {
						if (renewalRequestEndDate.compareTo(expiredDateObj) > 0) {
							returnList.add(sisuSeriyaDTO);
						} else if (renewalRequestEndDate.compareTo(expiredDateObj) == 0) {
							returnList.add(sisuSeriyaDTO);
						}
					} else if (renewalRequestStartDate.compareTo(expiredDateObj) == 0) {
						returnList.add(sisuSeriyaDTO);
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
		return returnList;
	}

	@Override
	public List<SisuSeriyaDTO> getServiceNoList(Date renewalRequestStartDate, Date renewalRequestEndDate) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String dateFormat = "yyyy-MM-dd";
		SimpleDateFormat frm = new SimpleDateFormat(dateFormat);
		List<SisuSeriyaDTO> returnList = new ArrayList<SisuSeriyaDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  sps_service_end_date, sps_service_no  FROM public.nt_m_sisu_permit_hol_service_info where sps_renewal_status='P' and sps_service_no is not null order by sps_service_no;";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				SisuSeriyaDTO sisuSeriyaDTO = new SisuSeriyaDTO();
				sisuSeriyaDTO.setExpiryDateVal(rs.getString("sps_service_end_date"));
				sisuSeriyaDTO.setServiceAgreementNo(rs.getString("sps_service_no"));
				if (sisuSeriyaDTO.getExpiryDateVal() != null) {
					Date expiredDateObj = frm.parse(sisuSeriyaDTO.getExpiryDateVal());
					if (renewalRequestStartDate.compareTo(expiredDateObj) < 0) {
						if (renewalRequestEndDate.compareTo(expiredDateObj) > 0) {
							returnList.add(sisuSeriyaDTO);
						} else if (renewalRequestEndDate.compareTo(expiredDateObj) == 0) {
							returnList.add(sisuSeriyaDTO);
						}
					} else if (renewalRequestStartDate.compareTo(expiredDateObj) == 0) {
						returnList.add(sisuSeriyaDTO);
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
		return returnList;
	}

	@Override
	public String getCurrentServiceNo(String selectedServiceRefNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String serviceNo = "service no. not found.";

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  sps_service_no FROM public.nt_m_sisu_permit_hol_service_info where sps_service_ref_no=? and sps_renewal_status='P';";
			ps = con.prepareStatement(query);
			ps.setString(1, selectedServiceRefNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				serviceNo = rs.getString("sps_service_no");
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return serviceNo;
	}

	@Override
	public String getCurrentRefNo(String selectedServiceAgreementNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String refNo = "ref no. not found.";

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  sps_service_ref_no FROM public.nt_m_sisu_permit_hol_service_info where sps_service_no=? and sps_renewal_status='P';";
			ps = con.prepareStatement(query);
			ps.setString(1, selectedServiceAgreementNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				refNo = rs.getString("sps_service_ref_no");
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return refNo;
	}

	@Override
	public String getRequestNoByServiceNo(String serviceRefNo, String serviceNo) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT sps_request_no FROM public.nt_m_sisu_permit_hol_service_info\r\n"
					+ "where sps_service_ref_no='" + serviceRefNo + "' and sps_service_no='" + serviceNo + "'";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("sps_request_no");
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
	public List<SisuSeriyaDTO> getServiceTypeForDropdown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SisuSeriyaDTO> serviceTypeList = new ArrayList<SisuSeriyaDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select st_code, st_description  from nt_r_subsidy_service_type where st_active='A';";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				SisuSeriyaDTO sisuSeriyaDto = new SisuSeriyaDTO();
				sisuSeriyaDto.setServiceTypeCode(rs.getString("st_code"));
				sisuSeriyaDto.setServiceTypeDes(rs.getString("st_description"));

				serviceTypeList.add(sisuSeriyaDto);

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
	public boolean isTaskDone(String value, String taskCode, String status) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isDone = false;

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT FROM public.nt_t_subsidy_task_det WHERE  tsd_service_no=? AND tsd_task_code=? AND tsd_status=? ";

			ps = con.prepareStatement(query);
			ps.setString(1, value);
			ps.setString(2, taskCode);
			ps.setString(3, status);
			rs = ps.executeQuery();

			if (rs.next() == true) {
				isDone = true;
			} else {
				isDone = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isDone;
	}

	@Override
	public boolean isServiceNoFound(String value) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isDone = false;

		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct sps_service_no from public.nt_m_sisu_permit_hol_service_info "
					+ "WHERE sps_service_ref_no=? ";

			ps = con.prepareStatement(query);
			ps.setString(1, value);

			rs = ps.executeQuery();

			if (rs.next() == true) {
				isDone = true;
			} else {
				isDone = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isDone;
	}

	@Override
	public List<SisuSeriyaDTO> getVoucherPendingRequestNo() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct sps_request_no from public.nt_m_sisu_permit_hol_service_info "
					+ "inner join public.nt_t_subsidy_task_det on nt_t_subsidy_task_det.tsd_service_no=nt_m_sisu_permit_hol_service_info.sps_service_no "
					+ "where ((nt_t_subsidy_task_det.tsd_task_code='SS006' and nt_t_subsidy_task_det.tsd_status='C' and sps_status='A') or "
					+ "(nt_t_subsidy_task_det.tsd_task_code='SS012' and nt_t_subsidy_task_det.tsd_status='C' and sps_status='A') or ( "
					+ "nt_t_subsidy_task_det.tsd_task_code='SM001' and nt_t_subsidy_task_det.tsd_status='O')) and is_sltb !='Y' order by sps_request_no desc";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			SisuSeriyaDTO s;

			while (rs.next()) {
				s = new SisuSeriyaDTO();
				s.setRequestNo(rs.getString("sps_request_no"));
				data.add(s);
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
	public List<SisuSeriyaDTO> getVoucherPendingServiceRefNoList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct sps_service_ref_no from public.nt_m_sisu_permit_hol_service_info "
					+ "inner join public.nt_t_subsidy_task_det on nt_t_subsidy_task_det.tsd_service_no=nt_m_sisu_permit_hol_service_info.sps_service_no "
					+ "where ((nt_t_subsidy_task_det.tsd_task_code='SS006' and nt_t_subsidy_task_det.tsd_status='C' and sps_status='A') or "
					+ "(nt_t_subsidy_task_det.tsd_task_code='SS012' and nt_t_subsidy_task_det.tsd_status='C' and sps_status='A') or ( "
					+ "nt_t_subsidy_task_det.tsd_task_code='SM001' and nt_t_subsidy_task_det.tsd_status='O')) and is_sltb !='Y' order by sps_service_ref_no desc";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			SisuSeriyaDTO s;

			while (rs.next()) {
				s = new SisuSeriyaDTO();
				s.setServiceRefNo(rs.getString("sps_service_ref_no"));
				data.add(s);
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
	public List<SisuSeriyaDTO> getVoucherPendingServiceNoList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct sps_service_no from public.nt_m_sisu_permit_hol_service_info "
					+ "inner join public.nt_t_subsidy_task_det on nt_t_subsidy_task_det.tsd_service_no=nt_m_sisu_permit_hol_service_info.sps_service_no "
					+ "where ((nt_t_subsidy_task_det.tsd_task_code='SS006' and nt_t_subsidy_task_det.tsd_status='C' and sps_status='A') or "
					+ "(nt_t_subsidy_task_det.tsd_task_code='SS012' and nt_t_subsidy_task_det.tsd_status='C' and sps_status='A') or "
					+ "(nt_t_subsidy_task_det.tsd_task_code='SM001' and nt_t_subsidy_task_det.tsd_status='O')) and is_sltb !='Y'; ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			SisuSeriyaDTO s;

			while (rs.next()) {
				s = new SisuSeriyaDTO();
				s.setServiceNo(rs.getString("sps_service_no"));
				data.add(s);
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
	public List<SisuSeriyaDTO> getDefaultSisuSariyaVoucherData() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> sisuSeriyaList = new ArrayList<SisuSeriyaDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select distinct sps_request_no, sps_service_ref_no,sps_service_no,"
					+ "sps_service_end_date ,sps_status, sps_operator_name from public.nt_m_sisu_permit_hol_service_info "
					+ "inner join public.nt_t_subsidy_task_det on nt_t_subsidy_task_det.tsd_service_no=nt_m_sisu_permit_hol_service_info.sps_service_no "
					+ "where ((nt_t_subsidy_task_det.tsd_task_code='SS006' and nt_t_subsidy_task_det.tsd_status='C' and sps_status='A') or"
					+ "(nt_t_subsidy_task_det.tsd_task_code='SS012' and nt_t_subsidy_task_det.tsd_status='C' and sps_status='A') or ( "
					+ "nt_t_subsidy_task_det.tsd_task_code='SM001' and nt_t_subsidy_task_det.tsd_status='O')) and is_sltb !='Y'";

			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();
			SisuSeriyaDTO e;

			while (rs.next()) {
				e = new SisuSeriyaDTO();
				e.setRequestNo(rs.getString("sps_request_no"));
				e.setServiceRefNo(rs.getString("sps_service_ref_no"));

				e.setServiceNo(rs.getString("sps_service_no"));

				e.setNameOfOperator(rs.getString("sps_operator_name"));

				Timestamp ts = rs.getTimestamp("sps_service_end_date");

				if (ts != null) {
					Date date = new Date();
					date.setTime(ts.getTime());
					String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);

					e.setServiceEndDateVal(formattedDate);
				} else {

					e.setServiceEndDateVal("N/A");
				}

				String approveStatusCode = rs.getString("sps_status");
				String approveStatus = "";

				if (approveStatusCode != null) {
					if (approveStatusCode.equals("A")) {
						approveStatus = "APPROVED";
					} else if (approveStatusCode.equals("R")) {
						approveStatus = "REJECTED";
					} else if (approveStatusCode.equals("P")) {
						approveStatus = "PENDING";
					}
				}

				e.setStatus(approveStatus);

				sisuSeriyaList.add(e);
			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return sisuSeriyaList;
	}

	@Override
	public boolean checkSisuSariyaRenewal(SisuSeriyaDTO dto, String checkedBy) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isChecked = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_m_sisu_permit_hol_service_info SET sps_is_checked='Y', sps_checked_by=?, "
					+ "sps_checked_date=?, sps_modified_by=?, sps_modified_date=? WHERE sps_service_ref_no=? ;";

			ps = con.prepareStatement(query);

			ps.setString(1, checkedBy);
			ps.setTimestamp(2, timestamp);
			ps.setString(3, checkedBy);
			ps.setTimestamp(4, timestamp);
			ps.setString(5, dto.getServiceRefNo());
			int a = ps.executeUpdate();

			if (a > 0) {
				isChecked = true;

				if (dto.getCancellationStatus() != null && dto.getCancellationStatus().trim().equals("Y")) {
					updateTaskStatusCompletedSubsidyTaskTable(con, dto.getRequestNo(), null, dto.getServiceRefNo(),
							"SS008a", "C", checkedBy);
				} else {
					updateTaskStatusCompletedSubsidyTaskTable(con, dto.getRequestNo(), null, dto.getServiceRefNo(),
							"SS007a", "C", checkedBy);
				}

			} else {
				isChecked = false;
			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isChecked;
	}

	@Override
	public boolean recommendSisuSariyaRenewal(SisuSeriyaDTO dto, String approvedBy) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isRecommended = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_m_sisu_permit_hol_service_info SET sps_is_recommended='Y', sps_recommended_by=?, "
					+ "sps_recommended_date=?, sps_modified_by=?, sps_modified_date=? WHERE sps_service_ref_no=? ;";

			ps = con.prepareStatement(query);

			ps.setString(1, approvedBy);
			ps.setTimestamp(2, timestamp);
			ps.setString(3, approvedBy);
			ps.setTimestamp(4, timestamp);
			ps.setString(5, dto.getServiceRefNo());
			int a = ps.executeUpdate();

			if (a > 0) {
				isRecommended = true;

				if (dto.getCancellationStatus() != null && dto.getCancellationStatus().trim().equals("Y")) {
					updateTaskStatusCompletedSubsidyTaskTable(con, dto.getRequestNo(), null, dto.getServiceRefNo(),
							"SS008b", "C", approvedBy);
				} else {
					updateTaskStatusCompletedSubsidyTaskTable(con, dto.getRequestNo(), null, dto.getServiceRefNo(),
							"SS007b", "C", approvedBy);
				}

			} else {
				isRecommended = false;
			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isRecommended;
	}

	@Override
	public boolean renewalApprove(SisuSeriyaDTO selectDTO, String loginUser) {
		Connection con = null;
		boolean isApproved = false;
		try {

			con = ConnectionManager.getConnection();

			if (selectDTO.getCancellationStatus() != null && selectDTO.getCancellationStatus().trim().equals("Y")) {
				// if Cancellation
				cancelAgreement(con, selectDTO, loginUser);
			} else {
				// if Renewals/Amendment
				deactivatePreviousAndActivateCurrentAgreement(con, selectDTO, loginUser);
			}

			updateTaskStatusCompletedSubsidyTaskTable(con, selectDTO.getRequestNo(), null, selectDTO.getServiceRefNo(),
					"SS012", "C", loginUser);

			con.commit();
			isApproved = true;
		} catch (Exception e) {
			isApproved = false;
			e.printStackTrace();
		} finally {
			ConnectionManager.close(con);
		}
		return isApproved;
	}

	@Override
	public List<SisuSeriyaDTO> getListForSelectedRefNoOrServiceNo(String selectedServiceRefNo,
			String selectedServiceAgreementNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SisuSeriyaDTO> returnList = new ArrayList<SisuSeriyaDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  sps_request_no, sps_service_ref_no, sps_operator_name, sps_service_end_date,  sps_service_no, sps_status, sps_renewal_status, sps_renewal_new_expire_date, sps_is_checked, sps_is_recommended, is_cancellation FROM public.nt_m_sisu_permit_hol_service_info where sps_renewal_status in ('A','P','R') and sps_service_no is not null and sps_service_no !='' and sps_service_ref_no=? order by sps_service_ref_no;";
			ps = con.prepareStatement(query);
			ps.setString(1, selectedServiceRefNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				SisuSeriyaDTO sisuSeriyaDTO = new SisuSeriyaDTO();
				sisuSeriyaDTO.setRequestNo(rs.getString("sps_request_no"));
				sisuSeriyaDTO.setServiceRefNo(rs.getString("sps_service_ref_no"));
				sisuSeriyaDTO.setServiceNo(rs.getString("sps_service_no"));
				sisuSeriyaDTO.setNameOfOperator(rs.getString("sps_operator_name"));
				sisuSeriyaDTO.setExpiryDateVal(rs.getString("sps_service_end_date"));
				sisuSeriyaDTO.setServiceEndDateVal(rs.getString("sps_service_end_date"));
				sisuSeriyaDTO.setServiceRenewalStatus(rs.getString("sps_renewal_status"));
				sisuSeriyaDTO.setStatusCode(rs.getString("sps_status"));
				sisuSeriyaDTO.setStatus(rs.getString("sps_status"));
				String statusCode = sisuSeriyaDTO.getServiceRenewalStatus();
				if (statusCode.equals("A")) {
					sisuSeriyaDTO.setStatusDes("APPROVED");
					sisuSeriyaDTO.setDisabledNewExpiredDate(true);
				} else if (statusCode.equals("P")) {
					sisuSeriyaDTO.setStatusDes("PENDING");
					sisuSeriyaDTO.setDisabledNewExpiredDate(false);
				} else if (statusCode.equals("R")) {
					sisuSeriyaDTO.setStatusDes("REJECTED");
					sisuSeriyaDTO.setDisabledNewExpiredDate(true);
				}
				sisuSeriyaDTO.setNewExpiryDateObj(rs.getDate("sps_renewal_new_expire_date"));
				sisuSeriyaDTO.setIsChecked(rs.getString("sps_is_checked"));
				sisuSeriyaDTO.setIsRecommended(rs.getString("sps_is_recommended"));
				sisuSeriyaDTO.setCancellationStatus(rs.getString("is_cancellation"));
				returnList.add(sisuSeriyaDTO);
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
	public List<SisuSeriyaDTO> getListForSelectedRefNoOrServiceNoAfterApproved(String selectedServiceRefNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SisuSeriyaDTO> returnList = new ArrayList<SisuSeriyaDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  sps_request_no, sps_service_ref_no, sps_operator_name, sps_service_end_date,  sps_service_no, sps_status, sps_renewal_status, "
					+ "sps_renewal_new_expire_date, sps_is_checked, sps_is_recommended, is_cancellation FROM public.nt_m_sisu_permit_hol_service_info "
					+ "where sps_service_no is not null and sps_service_no !='' and sps_service_ref_no=? ";
			ps = con.prepareStatement(query);
			ps.setString(1, selectedServiceRefNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				SisuSeriyaDTO sisuSeriyaDTO = new SisuSeriyaDTO();
				sisuSeriyaDTO.setRequestNo(rs.getString("sps_request_no"));
				sisuSeriyaDTO.setServiceRefNo(rs.getString("sps_service_ref_no"));
				sisuSeriyaDTO.setServiceNo(rs.getString("sps_service_no"));
				sisuSeriyaDTO.setNameOfOperator(rs.getString("sps_operator_name"));
				sisuSeriyaDTO.setExpiryDateVal(rs.getString("sps_service_end_date"));
				sisuSeriyaDTO.setServiceEndDateVal(rs.getString("sps_service_end_date"));
				sisuSeriyaDTO.setServiceRenewalStatus(rs.getString("sps_renewal_status"));
				sisuSeriyaDTO.setStatusCode(rs.getString("sps_status"));
				sisuSeriyaDTO.setStatus(rs.getString("sps_status"));
				String statusCode = sisuSeriyaDTO.getServiceRenewalStatus();
				if (statusCode.equals("A")) {
					sisuSeriyaDTO.setStatusDes("APPROVED");
					sisuSeriyaDTO.setDisabledNewExpiredDate(true);
				} else if (statusCode.equals("P")) {
					sisuSeriyaDTO.setStatusDes("PENDING");
					sisuSeriyaDTO.setDisabledNewExpiredDate(false);
				} else if (statusCode.equals("R")) {
					sisuSeriyaDTO.setStatusDes("REJECTED");
					sisuSeriyaDTO.setDisabledNewExpiredDate(true);
				}
				sisuSeriyaDTO.setNewExpiryDateObj(rs.getDate("sps_renewal_new_expire_date"));
				sisuSeriyaDTO.setIsChecked(rs.getString("sps_is_checked"));
				sisuSeriyaDTO.setIsRecommended(rs.getString("sps_is_recommended"));
				sisuSeriyaDTO.setCancellationStatus(rs.getString("is_cancellation"));
				returnList.add(sisuSeriyaDTO);
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
	public boolean rejectCheckBySisuSeriyaRenewal(SisuSeriyaDTO selectDTO, String rejectedBy) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		ResultSet rs = null;
		boolean isReject = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();
			if (selectDTO.getCancellationStatus() != null && selectDTO.getCancellationStatus().equals("Y")) {

				String q2 = "SELECT * FROM public.nt_temp_sisu_permit_hol_service_info WHERE sps_service_ref_no=?;";

				ps2 = con.prepareStatement(q2);
				ps2.setString(1, selectDTO.getServiceRefNo());
				rs = ps2.executeQuery();

				if (rs.next() == true) {
					String query = "UPDATE public.nt_m_sisu_permit_hol_service_info "
							+ "SET sps_is_checked=?, sps_checked_by=?, sps_checked_date=?, sps_is_recommended=?, sps_recommended_by=?, sps_recommended_date=?, "
							+ "sps_approve_reject_by=?, sps_approve_reject_date=?,  sps_renewal_status=?, is_cancellation=null  WHERE sps_service_ref_no=?;";

					ps = con.prepareStatement(query);

					ps.setString(1, rs.getString("sps_is_checked"));
					ps.setString(2, rs.getString("sps_checked_by"));
					ps.setTimestamp(3, rs.getTimestamp("sps_checked_date"));
					ps.setString(4, rs.getString("sps_is_recommended"));
					ps.setString(5, rs.getString("sps_recommended_by"));
					ps.setTimestamp(6, rs.getTimestamp("sps_recommended_date"));
					ps.setString(7, rs.getString("sps_approve_reject_by"));
					ps.setTimestamp(8, rs.getTimestamp("sps_approve_reject_date"));
					ps.setString(9, rs.getString("sps_renewal_status"));
					ps.setString(10, selectDTO.getServiceRefNo());

					int a = ps.executeUpdate();

					// delete record from temp table
					String q3 = "DELETE from  public.nt_temp_sisu_permit_hol_service_info WHERE sps_service_ref_no=?;";

					ps3 = con.prepareStatement(q3);
					ps3.setString(1, selectDTO.getServiceRefNo());
					int b = ps3.executeUpdate();

					if (a > 0 && b > 0) {
						isReject = true;
						updateTaskStatusCompletedSubsidyTaskTable(con, selectDTO.getRequestNo(), null,
								selectDTO.getServiceRefNo(), "SS008a", "C", rejectedBy);
					} else {
						isReject = false;
					}
				}

			} else {

				String query = "UPDATE public.nt_m_sisu_permit_hol_service_info SET sps_status='R', sps_is_checked='N', sps_checked_by=?, "
						+ "sps_checked_date=? WHERE sps_service_ref_no=? ;";

				ps = con.prepareStatement(query);

				ps.setString(1, rejectedBy);
				ps.setTimestamp(2, timestamp);
				ps.setString(3, selectDTO.getServiceRefNo());

				int a = ps.executeUpdate();

				if (a > 0) {
					isReject = true;
					updateTaskStatusCompletedSubsidyTaskTable(con, selectDTO.getRequestNo(), null,
							selectDTO.getServiceRefNo(), "SS007a", "C", rejectedBy);
				} else {
					isReject = false;
				}
			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(ps3);
			ConnectionManager.close(con);
		}
		return isReject;
	}

	@Override
	public boolean rejectRecommendSisuSeriyaRenewal(SisuSeriyaDTO selectDTO, String rejectedBy) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		ResultSet rs = null;
		boolean isReject = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			if (selectDTO.getCancellationStatus() != null && selectDTO.getCancellationStatus().equals("Y")) {

				String q2 = "SELECT * FROM public.nt_temp_sisu_permit_hol_service_info WHERE sps_service_ref_no=?;";

				ps2 = con.prepareStatement(q2);
				ps2.setString(1, selectDTO.getServiceRefNo());
				rs = ps2.executeQuery();

				if (rs.next() == true) {
					String query = "UPDATE public.nt_m_sisu_permit_hol_service_info "
							+ "SET sps_is_checked=?, sps_checked_by=?, sps_checked_date=?, sps_is_recommended=?, sps_recommended_by=?, sps_recommended_date=?, "
							+ "sps_approve_reject_by=?, sps_approve_reject_date=?,  sps_renewal_status=?, is_cancellation=null  WHERE sps_service_ref_no=?;";

					ps = con.prepareStatement(query);

					ps.setString(1, rs.getString("sps_is_checked"));
					ps.setString(2, rs.getString("sps_checked_by"));
					ps.setTimestamp(3, rs.getTimestamp("sps_checked_date"));
					ps.setString(4, rs.getString("sps_is_recommended"));
					ps.setString(5, rs.getString("sps_recommended_by"));
					ps.setTimestamp(6, rs.getTimestamp("sps_recommended_date"));
					ps.setString(7, rs.getString("sps_approve_reject_by"));
					ps.setTimestamp(8, rs.getTimestamp("sps_approve_reject_date"));
					ps.setString(9, rs.getString("sps_renewal_status"));
					ps.setString(10, selectDTO.getServiceRefNo());

					int a = ps.executeUpdate();

					// delete record from temp table
					String q3 = "DELETE from  public.nt_temp_sisu_permit_hol_service_info WHERE sps_service_ref_no=?;";

					ps3 = con.prepareStatement(q3);
					ps3.setString(1, selectDTO.getServiceRefNo());
					int b = ps3.executeUpdate();

					if (a > 0 && b > 0) {
						isReject = true;
						updateTaskStatusCompletedSubsidyTaskTable(con, selectDTO.getRequestNo(), null,
								selectDTO.getServiceRefNo(), "SS008b", "C", rejectedBy);
					} else {
						isReject = false;
					}
				}

			} else {

				String query = "UPDATE public.nt_m_sisu_permit_hol_service_info SET sps_status='R', sps_is_recommended='N', sps_recommended_by=?, "
						+ "sps_recommended_date=? WHERE sps_service_ref_no=? ;";

				ps = con.prepareStatement(query);

				ps.setString(1, rejectedBy);
				ps.setTimestamp(2, timestamp);
				ps.setString(3, selectDTO.getServiceRefNo());

				int a = ps.executeUpdate();

				if (a > 0) {
					isReject = true;
					updateTaskStatusCompletedSubsidyTaskTable(con, selectDTO.getRequestNo(), null,
							selectDTO.getServiceRefNo(), "SS007b", "C", rejectedBy);
				} else {
					isReject = false;
				}

			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(ps3);
			ConnectionManager.close(con);
		}
		return isReject;
	}

	@Override
	public boolean renewalReject(SisuSeriyaDTO selectDTO, String loginUser, String rejectReasonVal) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		boolean isrejected = false;
		try {

			con = ConnectionManager.getConnection();

			if (selectDTO.getCancellationStatus() != null && selectDTO.getCancellationStatus().equals("Y")) {

				String q2 = "SELECT * FROM public.nt_temp_sisu_permit_hol_service_info WHERE sps_service_ref_no=?;";

				ps2 = con.prepareStatement(q2);
				ps2.setString(1, selectDTO.getServiceRefNo());
				rs = ps2.executeQuery();

				if (rs.next() == true) {
					String query = "UPDATE public.nt_m_sisu_permit_hol_service_info "
							+ "SET sps_is_checked=?, sps_checked_by=?, sps_checked_date=?, sps_is_recommended=?, sps_recommended_by=?, sps_recommended_date=?, "
							+ "sps_approve_reject_by=?, sps_approve_reject_date=?,  sps_renewal_status=?, sps_reject_reason=?, is_cancellation=null  WHERE sps_service_ref_no=?;";

					ps = con.prepareStatement(query);

					ps.setString(1, rs.getString("sps_is_checked"));
					ps.setString(2, rs.getString("sps_checked_by"));
					ps.setTimestamp(3, rs.getTimestamp("sps_checked_date"));
					ps.setString(4, rs.getString("sps_is_recommended"));
					ps.setString(5, rs.getString("sps_recommended_by"));
					ps.setTimestamp(6, rs.getTimestamp("sps_recommended_date"));
					ps.setString(7, rs.getString("sps_approve_reject_by"));
					ps.setTimestamp(8, rs.getTimestamp("sps_approve_reject_date"));
					ps.setString(9, rs.getString("sps_renewal_status"));
					ps.setString(10, rejectReasonVal);
					ps.setString(11, selectDTO.getServiceRefNo());

					int a = ps.executeUpdate();

					// delete record from temp table
					String q3 = "DELETE from  public.nt_temp_sisu_permit_hol_service_info WHERE sps_service_ref_no=?;";

					ps3 = con.prepareStatement(q3);
					ps3.setString(1, selectDTO.getServiceRefNo());
					int b = ps3.executeUpdate();

					if (a > 0 && b > 0) {
						isrejected = true;
					} else {
						isrejected = false;
					}
				}

			} else {

				String query = "UPDATE public.nt_m_sisu_permit_hol_service_info SET sps_approve_reject_by=?, "
						+ "sps_approve_reject_date=?,  sps_renewal_status=?, sps_reject_reason=?  WHERE sps_service_ref_no=?;";

				ps = con.prepareStatement(query);

				ps.setString(1, loginUser);
				ps.setTimestamp(2, timestamp);
				ps.setString(3, "R");
				ps.setString(4, rejectReasonVal);
				ps.setString(5, selectDTO.getServiceRefNo());

				int a = ps.executeUpdate();

				if (a > 0) {
					isrejected = true;
				} else {
					isrejected = false;
				}

			}

			updateTaskStatusCompletedSubsidyTaskTable(con, selectDTO.getRequestNo(), null, selectDTO.getServiceRefNo(),
					"SS012", "C", loginUser);

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(ps3);
			ConnectionManager.close(con);
		}
		return isrejected;
	}

	@Override
	public List<SisuSeriyaDTO> getSisuSariyaVoucherData(String serviceNo, SisuSeriyaDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		List<SisuSeriyaDTO> sisuSeriyaList = new ArrayList<SisuSeriyaDTO>();
		List<SisuSeriyaDTO> sisuSeriyaVoucherList = new ArrayList<SisuSeriyaDTO>();

		String WHERE_SQL = "";
		boolean whereadded = false;

		if (dto.getRequestNo() != null && !dto.getRequestNo().equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE sps_request_no = " + "'" + dto.getRequestNo()
					+ "' and sps_status='A' and is_sltb !='Y' ";
			whereadded = true;
		}
		if (dto.getServiceRefNo() != null && !dto.getServiceRefNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND sps_service_ref_no = " + "'" + dto.getServiceRefNo() + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE sps_service_ref_no = " + "'" + dto.getServiceRefNo()
						+ "'and sps_status='A' and is_sltb !='Y' ";
				whereadded = true;
			}
		}

		if (dto.getRequestStartDate() != null && dto.getRequestEndDate() != null) {
			Timestamp startDate = new Timestamp(dto.getRequestStartDate().getTime());
			Timestamp endDate = new Timestamp(dto.getRequestEndDate().getTime());
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + "and nt_m_sisu_requestor_info.sri_request_date >= " + "' " + startDate
						+ "' and nt_m_sisu_requestor_info.sri_request_date <= " + "'" + endDate + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + "WHERE nt_m_sisu_requestor_info.sri_request_date >= " + "' " + startDate
						+ "' and nt_m_sisu_requestor_info.sri_request_date <= " + "'" + endDate
						+ "' and sps_status='A' and is_sltb !='Y' ";
				whereadded = true;
			}
		} else if (dto.getRequestEndDate() != null && dto.getRequestStartDate() == null) {

			Timestamp endDate = new Timestamp(dto.getRequestEndDate().getTime());
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + "and nt_m_sisu_requestor_info.sri_request_date <= " + "'" + endDate + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE  nt_m_sisu_requestor_info.sri_request_date <= " + "'" + endDate
						+ "' and sps_status='A' and is_sltb !='Y' ";
				whereadded = true;
			}
		} else if (dto.getRequestStartDate() != null && dto.getRequestEndDate() == null) {
			Timestamp startDate = new Timestamp(dto.getRequestStartDate().getTime());

			if (whereadded) {
				WHERE_SQL = WHERE_SQL + "and nt_m_sisu_requestor_info.sri_request_date >= " + "'" + startDate + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE nt_m_sisu_requestor_info.sri_request_date >= " + "'" + startDate
						+ "' and sps_status='A' and is_sltb !='Y' ";
				whereadded = true;
			}
		}

		if (dto.getServiceNo() != null && !dto.getServiceNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND sps_service_no = " + "'" + dto.getServiceNo() + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE sps_service_no = " + "'" + dto.getServiceNo()
						+ "'and sps_status='A' and is_sltb !='Y' ";
				whereadded = true;
			}
		}

		if (dto.getNameOfOperator() != null && !dto.getNameOfOperator().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND sps_operator_name = " + "'" + dto.getNameOfOperator() + "' ";
			} else {
				WHERE_SQL = WHERE_SQL + " WHERE sps_operator_name = " + "'" + dto.getNameOfOperator()
						+ "'and sps_status='A' and is_sltb !='Y' ";
				whereadded = true;
			}
		}

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select distinct sps_service_no from public.nt_m_sisu_permit_hol_service_info "
					+ "inner join public.nt_m_sisu_requestor_info on nt_m_sisu_requestor_info.sri_request_no = nt_m_sisu_permit_hol_service_info.sps_request_no "
					+ "inner join public.nt_t_subsidy_task_det on nt_t_subsidy_task_det.tsd_service_no=nt_m_sisu_permit_hol_service_info.sps_service_no "
					+ WHERE_SQL
					+ " and ((nt_t_subsidy_task_det.tsd_task_code='SS006' and nt_t_subsidy_task_det.tsd_status='C' and sps_status='A') or "
					+ "(nt_t_subsidy_task_det.tsd_task_code='SS012' and nt_t_subsidy_task_det.tsd_status='C' and sps_status='A') or "
					+ "(nt_t_subsidy_task_det.tsd_task_code='SM001' and nt_t_subsidy_task_det.tsd_status='O' or nt_t_subsidy_task_det.tsd_task_code='SM002'and nt_t_subsidy_task_det.tsd_status='O'))";

			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();
			SisuSeriyaDTO e;

			while (rs.next()) {
				e = new SisuSeriyaDTO();

				e.setServiceNo(rs.getString("sps_service_no"));

				sisuSeriyaList.add(e);
			}

			if (!sisuSeriyaList.isEmpty()) {

				for (int i = 0; i < sisuSeriyaList.size(); i++) {
					/*
					 * String query3 = "select * from public.nt_t_subsidy_task_det " +
					 * "where (nt_t_subsidy_task_det.tsd_service_no=? and ((nt_t_subsidy_task_det.tsd_task_code='SS006' and nt_t_subsidy_task_det.tsd_status='C') or "
					 * +
					 * "(nt_t_subsidy_task_det.tsd_task_code='SM001' and nt_t_subsidy_task_det.tsd_status='O' or nt_t_subsidy_task_det.tsd_task_code='SM002'and nt_t_subsidy_task_det.tsd_status='O')))"
					 * ;
					 */
					// commented on 1/31/2022 requested by pramitha

					String query3 = "select * from public.nt_t_subsidy_task_det "
							+ "inner join public.nt_m_sisu_permit_hol_service_info s on s.sps_service_ref_no =nt_t_subsidy_task_det .tsd_reference_no "
							+ "where (nt_t_subsidy_task_det.tsd_service_no=? "
							+ "and ((nt_t_subsidy_task_det.tsd_task_code='SS006' and nt_t_subsidy_task_det.tsd_status='C') "
							+ "or (nt_t_subsidy_task_det.tsd_task_code='SM001' and nt_t_subsidy_task_det.tsd_status='O'"
							+ "or nt_t_subsidy_task_det.tsd_task_code='SM002'and nt_t_subsidy_task_det.tsd_status='O')"
							+ "or (nt_t_subsidy_task_det.tsd_task_code='SS012'and nt_t_subsidy_task_det.tsd_status='C')))"
							+ "and s.sps_status ='A';";

					ps2 = con.prepareStatement(query3);
					ps2.setString(1, sisuSeriyaList.get(i).getServiceNo());
					rs2 = ps2.executeQuery();

					if (rs2.next()) {

						String query4 = "select distinct sps_request_no, sps_service_ref_no,sps_service_no,"
								+ "sps_service_end_date ,sps_status, sps_operator_name from public.nt_m_sisu_permit_hol_service_info "
								+ "inner join public.nt_m_sisu_requestor_info on nt_m_sisu_requestor_info.sri_request_no = nt_m_sisu_permit_hol_service_info.sps_request_no "
								+ "inner join public.nt_t_subsidy_task_det on nt_t_subsidy_task_det.tsd_service_no=nt_m_sisu_permit_hol_service_info.sps_service_no "
								+ "where sps_service_no=? ";

						ps3 = con.prepareStatement(query4);
						ps3.setString(1, sisuSeriyaList.get(i).getServiceNo());
						rs3 = ps3.executeQuery();

						SisuSeriyaDTO e2;

						while (rs3.next()) {
							e2 = new SisuSeriyaDTO();
							e2.setRequestNo(rs3.getString("sps_request_no"));
							e2.setServiceRefNo(rs3.getString("sps_service_ref_no"));
							e2.setServiceNo(rs3.getString("sps_service_no"));
							e2.setNameOfOperator(rs3.getString("sps_operator_name"));

							Timestamp ts = rs3.getTimestamp("sps_service_end_date");

							if (ts != null) {
								Date date = new Date();
								date.setTime(ts.getTime());
								String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);

								e2.setServiceEndDateVal(formattedDate);
							} else {
								e2.setServiceEndDateVal("N/A");
							}

							String approveStatusCode = rs3.getString("sps_status");
							String approveStatus = "";

							if (approveStatusCode != null) {
								if (approveStatusCode.equals("A")) {
									approveStatus = "APPROVED";
								} else if (approveStatusCode.equals("R")) {
									approveStatus = "REJECTED";
								} else if (approveStatusCode.equals("P")) {
									approveStatus = "PENDING";
								} else if (approveStatusCode.equals("I")) {
									approveStatus = "INACTIVE";
								}
							}

							e2.setStatus(approveStatus);

							sisuSeriyaVoucherList.add(e2);
						}
					}

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(rs2);
			ConnectionManager.close(rs3);
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(ps3);
			ConnectionManager.close(con);

		}

		return sisuSeriyaVoucherList;
	}

	// added for Team Maintenance
	@Override
	public SisuSeriyaDTO fillSerrviceNO(SisuSeriyaDTO sisuSariyaTeamAuthDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		SisuSeriyaDTO sisuDTO = new SisuSeriyaDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct sps_service_no from public.nt_m_sisu_permit_hol_service_info\r\n"
					+ "where sps_service_ref_no=?; ";

			ps = con.prepareStatement(query);
			ps.setString(1, sisuSariyaTeamAuthDTO.getServiceRefNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				sisuDTO.setServiceNo(rs.getString("sps_service_no"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return sisuDTO;
	}

	@Override
	public List<SisuSeriyaDTO> showSearchedDataForAuthorizationTeamMaintenance(Date date1, Date date2,
			String serviceRefNo, String serviceNo, String nameOfOperator) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String WHERE_SQL = "";
		boolean whereadded = false;
		List<SisuSeriyaDTO> showDataList = new ArrayList<SisuSeriyaDTO>();

		if (date1 != null && date2 != null) {

			Timestamp startDate = new Timestamp(date1.getTime());
			Timestamp endDate = new Timestamp(date2.getTime());
			WHERE_SQL = " WHERE sps_created_date >= " + "' " + startDate + "' and sps_created_date <= " + "'" + endDate
					+ "'";
			whereadded = true;

		} else if (date2 != null) {

			Timestamp endDate = new Timestamp(date2.getTime());
			WHERE_SQL = " WHERE  sps_created_date <= " + "'" + endDate + "'";
			whereadded = true;

		} else if (date1 != null) {

			Timestamp startDate = new Timestamp(date1.getTime());
			WHERE_SQL = " WHERE sps_created_date >= " + "'" + startDate + "'";
			whereadded = true;

		}

		if (serviceRefNo != null && !serviceRefNo.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND sps_service_ref_no = " + "'" + serviceRefNo + "' ";
			} else {
				WHERE_SQL = " WHERE sps_service_ref_no = '" + serviceRefNo + "'";
				whereadded = true;
			}
		}

		if (serviceNo != null && !serviceNo.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND sps_service_no = " + "'" + serviceNo + "' ";
			} else {
				WHERE_SQL = " WHERE sps_service_no = '" + serviceNo + "'";
				whereadded = true;
			}
		}

		if (nameOfOperator != null && !nameOfOperator.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND sps_operator_name = " + "'" + nameOfOperator
						+ "' AND sps_status='A' and sps_renewal_status not in ('I')  and "
						+ "(sps_service_agreement_issuedby IS NOT NULL and sps_permit_sticker_issuedby IS NOT NULL and sps_issue_logsheets_issuedby IS NOT null) ";
			} else {
				WHERE_SQL = " WHERE sps_operator_name = '" + nameOfOperator
						+ "' AND sps_status='A' and sps_renewal_status not in ('I')  and "
						+ "(sps_service_agreement_issuedby IS NOT NULL and sps_permit_sticker_issuedby IS NOT NULL and sps_issue_logsheets_issuedby IS NOT null) ";
				whereadded = true;
			}
		}

		try {
			con = ConnectionManager.getConnection();

			String query = "select * from public.nt_m_sisu_permit_hol_service_info " + WHERE_SQL;
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				SisuSeriyaDTO searchData = new SisuSeriyaDTO();
				searchData.setNameOfOperator(rs.getString("sps_operator_name"));
				searchData.setServiceStartDateVal(rs.getString("sps_service_start_date"));

				String newStartDate = searchData.getServiceStartDateVal();
				if (newStartDate != null && !newStartDate.trim().equals("")) {
					DateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date date = inputFormatter.parse(newStartDate);

					DateFormat outputFormatter = new SimpleDateFormat("MM/dd/yyyy");
					String output = outputFormatter.format(date);
					searchData.setServiceStartDateVal(output);
				}

				searchData.setServiceEndDateVal(rs.getString("sps_service_end_date"));

				String newEndDate = searchData.getServiceEndDateVal();
				if (newEndDate != null && !newEndDate.trim().equals("")) {
					DateFormat inputFormatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date datemy = inputFormatter1.parse(newEndDate);

					DateFormat outputFormatter1 = new SimpleDateFormat("MM/dd/yyyy");
					String output1 = outputFormatter1.format(datemy);
					searchData.setServiceEndDateVal(output1);
				}

				searchData.setStatus(rs.getString("sps_status"));
				searchData.setServiceRefNo(rs.getString("sps_service_ref_no"));
				searchData.setServiceNo(rs.getString("sps_service_no"));

				showDataList.add(searchData);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return showDataList;
	}

	@Override
	public void saveTeamInformationData(SisuSeriyaDTO ssTeamDTO, String loginUser1) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			java.util.Date date = new java.util.Date();
			Timestamp timestamp = new Timestamp(date.getTime());

			long seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_t_sisu_team_details");

			String sql = "INSERT INTO public.nt_t_sisu_team_details\r\n"
					+ "(std_seq, std_service_ref_no, std_auth_person, std_designation, std_nic, std_telephone_no, std_status, std_created_by, std_created_date, std_start_date, std_end_date,std_active)\r\n"
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);\r\n" + "";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, ssTeamDTO.getServiceRefNo());
			stmt.setString(3, ssTeamDTO.getAuthPerson());
			stmt.setString(4, ssTeamDTO.getDesignationCode());
			stmt.setString(5, ssTeamDTO.getNicNo());
			stmt.setString(6, ssTeamDTO.getTelNo());
			stmt.setString(7, ssTeamDTO.getStatus());
			stmt.setString(8, loginUser1);
			stmt.setTimestamp(9, timestamp);
			stmt.setString(10, ssTeamDTO.getServiceStartDateVal());
			stmt.setString(11, ssTeamDTO.getServiceEndDateVal());
			if (ssTeamDTO.getIsActive().equals("false")) {
				stmt.setString(12, "N");
			} else {
				stmt.setString(12, "Y");
			}

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

	@Override
	public List<SisuSeriyaDTO> getTeamMainteanDataonGrid(SisuSeriyaDTO ssTeamDTO) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SisuSeriyaDTO> teamMainteandataOnGrid = new ArrayList<SisuSeriyaDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT std_auth_person, std_designation, std_nic, std_telephone_no, std_status, std_created_by, std_created_date, std_modified_by, std_modified_date, std_start_date, std_end_date,std_active\r\n"
					+ "FROM public.nt_t_sisu_team_details where std_service_ref_no=? ;";
			ps = con.prepareStatement(query);

			ps.setString(1, ssTeamDTO.getServiceRefNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				SisuSeriyaDTO sisuSariyaDTO = new SisuSeriyaDTO();
				sisuSariyaDTO.setAuthPerson(rs.getString("std_auth_person"));
				sisuSariyaDTO.setDesignationCode(rs.getString("std_designation"));
				sisuSariyaDTO.setNicNo(rs.getString("std_nic"));
				sisuSariyaDTO.setTelNo(rs.getString("std_telephone_no"));

				sisuSariyaDTO.setIsActive(rs.getString("std_active"));
				if (sisuSariyaDTO.getIsActive().equals("Y")) {

					sisuSariyaDTO.setIsActive("Active");
				}

				else {
					sisuSariyaDTO.setIsActive("Inactive");
				}

				teamMainteandataOnGrid.add(sisuSariyaDTO);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return teamMainteandataOnGrid;

	}

	@Override
	public List<SisuSeriyaDTO> getServiceRefNoList(SisuSeriyaDTO sisuTeamSeriyaDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SisuSeriyaDTO> serviceRefNoList = new ArrayList<SisuSeriyaDTO>();

		try {

			String whereClose = "";
			if (sisuTeamSeriyaDTO.getServiceStartDateVal() != null
					&& !sisuTeamSeriyaDTO.getServiceStartDateVal().isEmpty()
					&& !sisuTeamSeriyaDTO.getServiceStartDateVal().equalsIgnoreCase("")) {

				if (sisuTeamSeriyaDTO.getServiceEndDateVal() != null
						&& !sisuTeamSeriyaDTO.getServiceEndDateVal().isEmpty()
						&& !sisuTeamSeriyaDTO.getServiceEndDateVal().equalsIgnoreCase("")) {
					whereClose = " where sps_service_start_date >= '" + sisuTeamSeriyaDTO.getServiceStartDateVal()
							+ "' and sps_service_end_date <= '" + sisuTeamSeriyaDTO.getServiceEndDateVal() + "' ";
				} else {

					whereClose = " where sps_service_start_date >= '" + sisuTeamSeriyaDTO.getServiceStartDateVal()
							+ "' ";
				}

			} else {
				whereClose = " where sps_service_end_date <= '" + sisuTeamSeriyaDTO.getServiceEndDateVal() + "' ";
			}
			con = ConnectionManager.getConnection();

			String query = "select distinct sps_service_ref_no from public.nt_m_sisu_permit_hol_service_info  "
					+ whereClose + "and sps_service_ref_no is not null and sps_service_ref_no !='' and sps_status='A' "
					+ " and sps_renewal_status not in ('I')   and  (sps_service_agreement_issuedby IS NOT NULL and sps_permit_sticker_issuedby IS NOT NULL and sps_issue_logsheets_issuedby IS NOT null) "
					+ "order by sps_service_ref_no desc";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				SisuSeriyaDTO ss = new SisuSeriyaDTO();

				ss.setServiceRefNo(rs.getString("sps_service_ref_no"));
				serviceRefNoList.add(ss);

			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return serviceRefNoList;
	}

	@Override
	public void deleteTeamAuthData(SisuSeriyaDTO ssDTO, String string1, String string2, String string3,
			String string4) {
		Connection con = null;
		PreparedStatement ps = null, ps1 = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query1 = "DELETE from  public.nt_t_sisu_team_details  where std_service_ref_no=? and  std_auth_person=? and std_designation=? and std_nic=? and std_telephone_no=? ";
			ps1 = con.prepareStatement(query1);
			ps1.setString(1, ssDTO.getServiceRefNo());
			ps1.setString(2, string1);
			ps1.setString(3, string2);
			ps1.setString(4, string3);
			ps1.setString(5, string4);
			ps1.executeUpdate();

			if (ps1 != null) {
				ps1.close();
			}

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
	public void updateTeamInformationData(SisuSeriyaDTO ssTeamDTO, String loginUser1, String string1, String string2,
			String string3) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			java.util.Date date = new java.util.Date();
			Timestamp timestamp = new Timestamp(date.getTime());

			String sql = "UPDATE public.nt_t_sisu_team_details\r\n"
					+ "SET  std_auth_person=?, std_designation=?, std_nic=?, std_telephone_no=?, std_modified_by=?, std_modified_date=?, std_active=?\r\n"
					+ "WHERE std_service_ref_no=? and std_auth_person=? and std_designation=? and std_nic=?;";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, ssTeamDTO.getAuthPerson());
			stmt.setString(2, ssTeamDTO.getDesignationCode());
			stmt.setString(3, ssTeamDTO.getNicNo());
			stmt.setString(4, ssTeamDTO.getTelNo());
			stmt.setString(5, loginUser1);
			stmt.setTimestamp(6, timestamp);
			if (ssTeamDTO.getIsActive().equals("false")) {
				stmt.setString(7, "N");
			} else {
				stmt.setString(7, "Y");

			}
			stmt.setString(8, ssTeamDTO.getServiceRefNo());
			stmt.setString(9, string1);

			stmt.setString(10, string2);

			stmt.setString(11, string3);

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

	// team auth finished
	@Override
	public SisuSeriyaDTO getSearchedServiceInfo(String serviceRefNo, String serviceNo, String requestNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		SisuSeriyaDTO sisuSeriyaDTO = new SisuSeriyaDTO();
		String WHERE_SQL = "";
		boolean whereadded = false;

		if (requestNo != null && !requestNo.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND sps_request_no = " + "'" + requestNo + "' ";
			} else {
				WHERE_SQL = " WHERE sps_request_no = '" + requestNo + "'";
				whereadded = true;
			}
		}

		if (serviceRefNo != null && !serviceRefNo.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND sps_service_ref_no = " + "'" + serviceRefNo + "' ";
			} else {
				WHERE_SQL = " WHERE sps_service_ref_no = '" + serviceRefNo + "'";
				whereadded = true;
			}
		}

		if (serviceNo != null && !serviceNo.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND sps_service_no = " + "'" + serviceNo + "' ";
			} else {
				WHERE_SQL = " WHERE sps_service_no = '" + serviceNo + "'";
				whereadded = true;
			}
		}

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT   sps_request_no,sps_service_ref_no,sps_nic_no, sps_perfered_lan, sps_operator_name, sps_operator_name_si, sps_operator_name_ta, sps_add1, sps_add1_si, sps_add1_ta, sps_add2, sps_add2_si, sps_add2_ta, sps_city, sps_city_si, sps_city_ta, sps_service_type_code, sps_permit_no, sps_bus_no, sps_origin, sps_destination, sps_via, sps_distance, sps_tips_per_day, sps_service_start_date, sps_service_end_date, sps_province, sps_district_code, sps_dev_sec_code, sps_telephone_no, sps_mobile_no, sps_status, sps_account_no, sps_bank_name_code, sps_branch_code,  sps_service_no, sps_renewal_status, sps_renewal_new_expire_date, is_cancellation FROM public.nt_m_sisu_permit_hol_service_info "
					+ WHERE_SQL;

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				sisuSeriyaDTO.setNicNo(rs.getString("sps_nic_no"));
				sisuSeriyaDTO.setLanguageCode(rs.getString("sps_perfered_lan"));
				sisuSeriyaDTO.setNameOfOperator(rs.getString("sps_operator_name"));
				sisuSeriyaDTO.setNameOfOperatorSin(rs.getString("sps_operator_name_si"));
				sisuSeriyaDTO.setNameOfOperatorTamil(rs.getString("sps_operator_name_ta"));
				sisuSeriyaDTO.setAddressOne(rs.getString("sps_add1"));
				sisuSeriyaDTO.setAddressOneSin(rs.getString("sps_add1_si"));
				sisuSeriyaDTO.setAddressOneTamil(rs.getString("sps_add1_ta"));
				sisuSeriyaDTO.setAddressTwo(rs.getString("sps_add2"));
				sisuSeriyaDTO.setAdressTwoSin(rs.getString("sps_add2_si"));
				sisuSeriyaDTO.setAdressTwoTamil(rs.getString("sps_add2_ta"));
				sisuSeriyaDTO.setCity(rs.getString("sps_city"));
				sisuSeriyaDTO.setCitySin(rs.getString("sps_city_si"));
				sisuSeriyaDTO.setCityTamil(rs.getString("sps_city_ta"));
				sisuSeriyaDTO.setServiceTypeCode(rs.getString("sps_service_type_code"));
				sisuSeriyaDTO.setPermitNo(rs.getString("sps_permit_no"));
				sisuSeriyaDTO.setBusRegNo(rs.getString("sps_bus_no"));
				sisuSeriyaDTO.setOriginDes(rs.getString("sps_origin"));
				sisuSeriyaDTO.setDestinationDes(rs.getString("sps_destination"));
				sisuSeriyaDTO.setVia(rs.getString("sps_via"));
				sisuSeriyaDTO.setDistance(rs.getDouble("sps_distance"));
				sisuSeriyaDTO.setTripsPerDay(rs.getInt("sps_tips_per_day"));
				Timestamp serviceStartDateTs = rs.getTimestamp("sps_service_start_date");
				if (serviceStartDateTs != null) {
					Date serviceStartDateObj = new Date();
					serviceStartDateObj.setTime(serviceStartDateTs.getTime());
					String startDateVal = new SimpleDateFormat("yyyy-MM-dd").format(serviceStartDateObj);
					sisuSeriyaDTO.setServiceStartDateVal(startDateVal);
				} else {
					sisuSeriyaDTO.setServiceStartDateVal("N/A");
				}
				Timestamp serviceEndDateTs = rs.getTimestamp("sps_service_end_date");
				if (serviceEndDateTs != null) {
					Date serviceEndDateObj = new Date();
					serviceEndDateObj.setTime(serviceEndDateTs.getTime());
					String endDateVal = new SimpleDateFormat("yyyy-MM-dd").format(serviceEndDateObj);
					sisuSeriyaDTO.setServiceEndDateVal(endDateVal);
				} else {
					sisuSeriyaDTO.setServiceEndDateVal("N/A");
				}
				sisuSeriyaDTO.setProvinceCode(rs.getString("sps_province"));
				sisuSeriyaDTO.setDistrictCode(rs.getString("sps_district_code"));
				sisuSeriyaDTO.setDivisionalSecCode(rs.getString("sps_dev_sec_code"));
				sisuSeriyaDTO.setTelNo(rs.getString("sps_telephone_no"));
				sisuSeriyaDTO.setMobileNo(rs.getString("sps_mobile_no"));
				sisuSeriyaDTO.setAccountNo(rs.getString("sps_account_no"));
				sisuSeriyaDTO.setBankNameCode(rs.getString("sps_bank_name_code"));
				sisuSeriyaDTO.setBankBranchNameCode(rs.getString("sps_branch_code"));
				sisuSeriyaDTO.setStatusCode(rs.getString("sps_renewal_status"));
				String statusCode = sisuSeriyaDTO.getStatusCode();
				if (statusCode.equals("A")) {
					sisuSeriyaDTO.setServiceRenewalStatus("APPROVED");
				} else if (statusCode.equals("P")) {
					sisuSeriyaDTO.setServiceRenewalStatus("PENDING");
				} else if (statusCode.equals("R")) {
					sisuSeriyaDTO.setServiceRenewalStatus("REJECTED");
				}
				sisuSeriyaDTO.setCancellationStatus(rs.getString("is_cancellation"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return sisuSeriyaDTO;
	}

	@Override
	public boolean isServiceDataEnterd(SisuSeriyaDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isFound = false;

		try {

			con = ConnectionManager.getConnection();

			String query = "select * from public.nt_m_sisu_permit_hol_service_info "
					+ "where sps_service_ref_no=? and sps_service_start_date is not null and "
					+ "sps_service_end_date is not null ";

			ps = con.prepareStatement(query);
			ps.setString(1, dto.getServiceRefNo());

			rs = ps.executeQuery();

			if (rs.next() == true) {
				isFound = true;
			} else {
				isFound = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isFound;
	}

	@Override
	public List<SisuSeriyaDTO> getSelectSisuSariyaData(String serviceRefNo, SisuSeriyaDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> sisuSeriyaList = new ArrayList<SisuSeriyaDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select distinct sps_request_no, sps_service_ref_no,sps_service_no,"
					+ "sps_service_end_date ,sps_status, sps_operator_name, sps_is_checked, sps_is_recommended from public.nt_m_sisu_permit_hol_service_info where sps_service_ref_no="
					+ "'" + serviceRefNo + "' ";

			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();
			SisuSeriyaDTO e;

			while (rs.next()) {
				e = new SisuSeriyaDTO();
				e.setRequestNo(rs.getString("sps_request_no"));
				e.setServiceRefNo(rs.getString("sps_service_ref_no"));

				e.setServiceNo(rs.getString("sps_service_no"));

				e.setNameOfOperator(rs.getString("sps_operator_name"));

				Timestamp ts = rs.getTimestamp("sps_service_end_date");

				if (ts != null) {
					Date date = new Date();
					date.setTime(ts.getTime());
					String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);

					e.setServiceEndDateVal(formattedDate);
				} else {

					e.setServiceEndDateVal("N/A");
				}

				String approveStatusCode = rs.getString("sps_status");
				String approveStatus = "";

				if (approveStatusCode != null) {
					if (approveStatusCode.equals("A")) {
						approveStatus = "APPROVED";
						e.setEditStatus(false);
					} else if (approveStatusCode.equals("R")) {
						approveStatus = "REJECTED";
						e.setEditStatus(false);
					} else if (approveStatusCode.equals("P")) {
						approveStatus = "PENDING";
						e.setEditStatus(true);
					}
				}

				e.setStatus(approveStatusCode);
				e.setStatusDes(approveStatus);

				e.setIsChecked(rs.getString("sps_is_checked"));
				e.setIsRecommended(rs.getString("sps_is_recommended"));

				sisuSeriyaList.add(e);
			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return sisuSeriyaList;
	}

	@Override
	public String getProvincesDescription(String provinceCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String provinceDes = "province des not found.";

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT description FROM public.nt_r_province where code=?;";
			ps = con.prepareStatement(query);
			ps.setString(1, provinceCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				provinceDes = rs.getString("description");
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return provinceDes;
	}

	@Override
	public String getDistrictDescription(String districtCode, String provinceCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String districtDes = "district des not found.";

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT description FROM public.nt_r_district where code=? and province_code=?;";
			ps = con.prepareStatement(query);
			ps.setString(1, districtCode);
			ps.setString(2, provinceCode);
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
	public String getDivisionSectionDescription(String districtCode, String provinceCode, String divisionalSecCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String divDes = "district des not found.";

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT ds_description FROM public.nt_r_div_sec where ds_code=? and ds_district_code=?;";
			ps = con.prepareStatement(query);
			ps.setString(1, divisionalSecCode);
			ps.setString(2, districtCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				divDes = rs.getString("ds_description");
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return divDes;
	}

	@Override
	public String getServiceTypeDescription(String serviceTypeCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String serviceTypeDes = "service Type des not found.";

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT description FROM public.nt_r_service_types where code=?;";
			ps = con.prepareStatement(query);
			ps.setString(1, serviceTypeCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				serviceTypeDes = rs.getString("description");
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return serviceTypeDes;
	}

	@Override
	public SisuSeriyaDTO getSearchedSchoolInfo(String serviceRefNo, String serviceNo, String requestNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		SisuSeriyaDTO sisuSeriyaDTO = new SisuSeriyaDTO();

		String WHERE_SQL = "";
		boolean whereadded = false;
		if (requestNo != null && !requestNo.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND ssc_request_no = " + "'" + requestNo + "' ";
			} else {
				WHERE_SQL = " WHERE ssc_request_no = '" + requestNo + "'";
				whereadded = true;
			}
		}

		if (serviceRefNo != null && !serviceRefNo.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND ssc_service_ref_no = " + "'" + serviceRefNo + "' ";
			} else {
				WHERE_SQL = " WHERE ssc_service_ref_no = '" + serviceRefNo + "'";
				whereadded = true;
			}
		}

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT ssc_request_no, ssc_service_ref_no, ssc_school_name, ssc_school_name_si, ssc_school_name_ta, ssc_school_add1, ssc_school_add1_si, ssc_school_add1_ta, ssc_school_add2, ssc_school_add2_si, ssc_school_add2_ta, ssc_school_city, ssc_school_city_si, ssc_school_city_ta, ssc_school_province_code, ssc_school_district_code, ssc_school_div_sec_code, ssc_school_tel_no, ssc_school_mobile, ssc_school_email, ssc_status FROM public.nt_m_sisu_permit_hol_school_info "
					+ WHERE_SQL;

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				sisuSeriyaDTO.setSchoolName(rs.getString("ssc_school_name"));
				sisuSeriyaDTO.setSchoolNameSin(rs.getString("ssc_school_name_si"));
				sisuSeriyaDTO.setSchoolNameTamil(rs.getString("ssc_school_name_ta"));
				sisuSeriyaDTO.setSchoolAddressOne(rs.getString("ssc_school_add1"));
				sisuSeriyaDTO.setSchoolAdrressOneSin(rs.getString("ssc_school_add1_si"));
				sisuSeriyaDTO.setSchoolAddressOneTamil(rs.getString("ssc_school_add1_ta"));
				sisuSeriyaDTO.setSchoolAddressTwo(rs.getString("ssc_school_add2"));
				sisuSeriyaDTO.setSchoolAddressTwoSin(rs.getString("ssc_school_add2_si"));
				sisuSeriyaDTO.setSchoolAddressTwoTamil(rs.getString("ssc_school_add2_ta"));
				sisuSeriyaDTO.setSchoolCity(rs.getString("ssc_school_city"));
				sisuSeriyaDTO.setSchoolCitySin(rs.getString("ssc_school_city_si"));
				sisuSeriyaDTO.setSchoolCityTamil(rs.getString("ssc_school_city_ta"));
				sisuSeriyaDTO.setSchoolProvinceCode(rs.getString("ssc_school_province_code"));
				sisuSeriyaDTO.setSchoolDistrictCode(rs.getString("ssc_school_district_code"));
				sisuSeriyaDTO.setSchoolDivisinSecCode(rs.getString("ssc_school_div_sec_code"));
				sisuSeriyaDTO.setSchoolTelNo(rs.getString("ssc_school_tel_no"));
				sisuSeriyaDTO.setSchoolMobileNo(rs.getString("ssc_school_mobile"));
				sisuSeriyaDTO.setSchoolEmailAdd(rs.getString("ssc_school_email"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return sisuSeriyaDTO;
	}

	@Override
	public List<SisuSeriyaDTO> getBankList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SisuSeriyaDTO> bankList = new ArrayList<SisuSeriyaDTO>();

		try {
			con = ConnectionManager.getConnection();
			String query = "SELECT code,description FROM public.nt_r_bank WHERE active='A'";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				SisuSeriyaDTO seriyaDTO = new SisuSeriyaDTO();
				seriyaDTO.setBankNameCode(rs.getString("code"));
				seriyaDTO.setBankName(rs.getString("description"));
				bankList.add(seriyaDTO);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return bankList;
	}

	@Override
	public List<SisuSeriyaDTO> getBranchesForBanksList(String bankCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SisuSeriyaDTO> bankBranchList = new ArrayList<SisuSeriyaDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT branch_code,branch_name FROM public.nt_r_bank_branch WHERE bank_code=? and active='A' ";
			ps = con.prepareStatement(query);
			ps.setString(1, bankCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				SisuSeriyaDTO seriyaDTO = new SisuSeriyaDTO();
				seriyaDTO.setBankBranchNameCode(rs.getString("branch_code"));
				seriyaDTO.setBankBranchNameDes(rs.getString("branch_name"));
				bankBranchList.add(seriyaDTO);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return bankBranchList;
	}

	@Override
	public int updateBankInfoDTO(SisuSeriyaDTO bankInfoDTO, String loginUser, String selectedServiceNoInGrid,
			String selectedRefNoInGrid) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_sisu_permit_hol_service_info SET  sps_account_no=?, sps_bank_name_code=?, sps_branch_code=?, sps_modified_by=?, sps_modified_date=? WHERE sps_service_ref_no=? and sps_service_no=? ;";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, bankInfoDTO.getAccountNo());
			stmt.setString(2, bankInfoDTO.getBankNameCode());
			stmt.setString(3, bankInfoDTO.getBankBranchNameCode());
			stmt.setString(4, loginUser);
			stmt.setTimestamp(5, timestamp);
			stmt.setString(6, selectedRefNoInGrid);
			stmt.setString(7, selectedServiceNoInGrid);
			stmt.executeUpdate();
			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return 0;
	}

	@Override
	public SisuSeriyaDTO getBankDetDTO(String selectedServiceNoInGrid, String selectedRefNoInGrid) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		SisuSeriyaDTO sisuSeriyaDTO = new SisuSeriyaDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT   sps_account_no, sps_bank_name_code, sps_branch_code FROM public.nt_m_sisu_permit_hol_service_info where  sps_service_ref_no=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedRefNoInGrid);
			rs = ps.executeQuery();

			while (rs.next()) {
				sisuSeriyaDTO.setAccountNo(rs.getString("sps_account_no"));
				sisuSeriyaDTO.setBankNameCode(rs.getString("sps_bank_name_code"));
				sisuSeriyaDTO.setBankBranchNameCode(rs.getString("sps_branch_code"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return sisuSeriyaDTO;
	}

	// initiate sisu sariya
	@Override
	public List<SisuSeriyaDTO> drpdOriginList(String requestNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SisuSeriyaDTO> drpdOriginList = new ArrayList<SisuSeriyaDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct srr_origin " + "from NT_M_SISU_REQUESTOR_ROUTE_INFO "
					+ "where srr_request_no = ?;";

			ps = con.prepareStatement(query);
			ps.setString(1, requestNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				SisuSeriyaDTO ss = new SisuSeriyaDTO();

				ss.setOriginCode(rs.getString("srr_origin"));
				ss.setOriginCodeSin(rs.getString("srr_origin"));
				drpdOriginList.add(ss);

			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return drpdOriginList;
	}

	@Override
	public List<SisuSeriyaDTO> drpdDestinationList(String requestNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SisuSeriyaDTO> drpdDestinationList = new ArrayList<SisuSeriyaDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct srr_destination " + "from NT_M_SISU_REQUESTOR_ROUTE_INFO "
					+ "where srr_request_no = ? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, requestNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				SisuSeriyaDTO ss = new SisuSeriyaDTO();

				ss.setDestinationCode(rs.getString("srr_destination"));
				ss.setDestinationCodeSin(rs.getString("srr_destination"));
				drpdDestinationList.add(ss);

			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return drpdDestinationList;
	}

	@Override
	public List<SisuSeriyaDTO> drpdDistrictList(String requestNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SisuSeriyaDTO> drpdDestinationList = new ArrayList<SisuSeriyaDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select srr_destination " + "from NT_M_SISU_REQUESTOR_ROUTE_INFO " + "where request_no = ? ";

			ps = con.prepareStatement(query);
			ps.setString(1, requestNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				SisuSeriyaDTO ss = new SisuSeriyaDTO();

				ss.setDestinationCode(rs.getString("srr_destination"));
				drpdDestinationList.add(ss);

			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return drpdDestinationList;
	}

	// generate Service reference number for the sisu-sariya permit holder page
	@Override
	public String generateServiceReferenceNo() {

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

			String sql = "SELECT app_no FROM public.nt_r_number_generation WHERE code= 'REF'";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("app_no");
			}

			if (result != null) {

				lastyr = result.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String ApprecordcountN = "00001";
					strServiceReferenceNo = "REF" + currYear + ApprecordcountN;
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
					strServiceReferenceNo = "REF" + currYear + ApprecordcountN;
				}
			} else
				strServiceReferenceNo = "REF" + currYear + "00001";

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return strServiceReferenceNo;

	}

	// Insert service information service
	@Override
	public SisuSeriyaDTO insertServiceInformation(SisuSeriyaDTO sisuSeriyaDTO, String loginUser) {
		Connection con = null;
		PreparedStatement ps = null;

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

			long seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_m_sisu_permit_hol_service_info");

			String query = "INSERT INTO public.nt_m_sisu_permit_hol_service_info\r\n"
					+ "(sps_seq,sps_request_no,sps_service_ref_no,\r\n" + " sps_perfered_lan,sps_service_type_code,\r\n"
					+ " sps_bus_no,sps_permit_no,\r\n" + " sps_origin,sps_distance,\r\n"
					+ " sps_destination,sps_tips_per_day,\r\n" + " sps_via,sps_service_start_date,\r\n"
					+ " sps_nic_no,sps_service_end_date,\r\n"
					+ " sps_operator_name,sps_operator_name_si,sps_operator_name_ta,\r\n"
					+ " sps_add1,sps_add2,sps_city,\r\n" + " sps_add1_si,sps_add2_si,sps_city_si,\r\n"
					+ " sps_add1_ta,sps_add2_ta,sps_city_ta,\r\n" + " sps_status,sps_province,\r\n"
					+ " sps_telephone_no,sps_district_code,\r\n" + " sps_mobile_no,sps_dev_sec_code,\r\n"
					+ " sps_created_by,sps_modified_by,\r\n" + " sps_created_date,sps_modified_date,is_sltb \r\n"
					+ " )values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			ps = con.prepareStatement(query);

			try {
				ps.setLong(1, seqNo);
				ps.setString(2, sisuSeriyaDTO.getRequestNo());

				String generatedServiceReferenceNo = generateServiceReferenceNo();

				updateNumberGeneration("REF", generatedServiceReferenceNo, loginUser);

				sisuSeriyaDTO.setServiceRefNo(generatedServiceReferenceNo);
				ps.setString(3, generatedServiceReferenceNo);
				ps.setString(4, sisuSeriyaDTO.getLanguageCode());
				ps.setString(5, sisuSeriyaDTO.getServiceTypeCode());
				ps.setString(6, sisuSeriyaDTO.getBusRegNo());
				ps.setString(7, sisuSeriyaDTO.getPermitNo());
				ps.setString(8, sisuSeriyaDTO.getOriginCode());

				String strDistance = String.valueOf(sisuSeriyaDTO.getDistance());
				ps.setString(9, strDistance);

				ps.setString(10, sisuSeriyaDTO.getDestinationCode());
				ps.setInt(11, sisuSeriyaDTO.getTripsPerDay());
				ps.setString(12, sisuSeriyaDTO.getVia());

				Timestamp serviceStartDate = null;
				if (sisuSeriyaDTO.getServiceStartDateObj() != null) {
					serviceStartDate = new Timestamp(sisuSeriyaDTO.getServiceStartDateObj().getTime());
				}

				ps.setTimestamp(13, serviceStartDate);
				ps.setString(14, sisuSeriyaDTO.getNicNo());

				Timestamp serviceEndDate = null;
				if (sisuSeriyaDTO.getServiceEndDateObj() != null) {
					serviceEndDate = new Timestamp(sisuSeriyaDTO.getServiceEndDateObj().getTime());
				}

				ps.setTimestamp(15, serviceEndDate);
				ps.setString(16, sisuSeriyaDTO.getNameOfOperator());
				ps.setString(17, sisuSeriyaDTO.getNameOfOperatorSin());
				ps.setString(18, sisuSeriyaDTO.getNameOfOperatorTamil());
				ps.setString(19, sisuSeriyaDTO.getAddressOne());
				ps.setString(20, sisuSeriyaDTO.getAddressTwo());
				ps.setString(21, sisuSeriyaDTO.getCity());
				ps.setString(22, sisuSeriyaDTO.getAddressOneSin());
				ps.setString(23, sisuSeriyaDTO.getAdressTwoSin());
				ps.setString(24, sisuSeriyaDTO.getCitySin());
				ps.setString(25, sisuSeriyaDTO.getAddressOneTamil());
				ps.setString(26, sisuSeriyaDTO.getAdressTwoTamil());
				ps.setString(27, sisuSeriyaDTO.getCityTamil());
				ps.setString(28, "O");
				ps.setString(29, sisuSeriyaDTO.getProvinceCode());
				ps.setString(30, sisuSeriyaDTO.getTelNo());
				ps.setString(31, sisuSeriyaDTO.getDistrictCode());
				ps.setString(32, sisuSeriyaDTO.getMobileNo());
				ps.setString(33, sisuSeriyaDTO.getDivisionalSecCode());
				ps.setString(34, loginUser);
				ps.setString(35, loginUser);
				ps.setTimestamp(36, timestamp);
				ps.setTimestamp(37, timestamp);
				ps.setString(38, sisuSeriyaDTO.getSltbStatus());

				int i = ps.executeUpdate();

				if (i > 0) {
					sisuSeriyaDTO.setStatus("O");
					isModelSave = true;
				} else {
					isModelSave = false;
				}

				con.commit();
			} catch (SQLException e) {
				con.rollback();
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {

			if (!isModelSave) {
				sisuSeriyaDTO.setServiceRefNo("");
			}

			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return sisuSeriyaDTO;
	}

	// Insert school information
	@Override
	public boolean insertSchoolInformation(SisuSeriyaDTO sisuSeriyaDTO, String loginUser) {
		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		boolean isModelSave = false;
		boolean isModel2Save = false;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_m_sisu_permit_hol_school_info");

			String sql = "INSERT INTO public.nt_m_sisu_permit_hol_school_info\r\n"
					+ "(ssc_seq,ssc_request_no,ssc_service_ref_no,\r\n"
					+ " ssc_school_name,ssc_school_name_si,ssc_school_name_ta,\r\n"
					+ " ssc_school_add1,ssc_school_add2,ssc_school_city,\r\n"
					+ " ssc_school_add1_si,ssc_school_add2_si,ssc_school_city_si,\r\n"
					+ " ssc_school_add1_ta,ssc_school_add2_ta,ssc_school_city_ta,\r\n"
					+ " ssc_school_email,ssc_school_province_code,\r\n"
					+ " ssc_school_tel_no,ssc_school_district_code,\r\n"
					+ " ssc_school_mobile,ssc_school_div_sec_code,\r\n" + " ssc_created_by,ssc_modified_by,\r\n"
					+ " ssc_created_date,ssc_modified_date\r\n" + " ) values (\r\n"
					+ " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); ";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, sisuSeriyaDTO.getRequestNo());
			stmt.setString(3, sisuSeriyaDTO.getServiceRefNo());
			stmt.setString(4, sisuSeriyaDTO.getSchoolName());
			stmt.setString(5, sisuSeriyaDTO.getSchoolNameSin());
			stmt.setString(6, sisuSeriyaDTO.getSchoolNameTamil());

			stmt.setString(7, sisuSeriyaDTO.getSchoolAddressOne());
			stmt.setString(8, sisuSeriyaDTO.getSchoolAddressTwo());
			stmt.setString(9, sisuSeriyaDTO.getSchoolCity());

			stmt.setString(10, sisuSeriyaDTO.getSchoolAdrressOneSin());
			stmt.setString(11, sisuSeriyaDTO.getSchoolAddressTwoSin());
			stmt.setString(12, sisuSeriyaDTO.getSchoolCitySin());

			stmt.setString(13, sisuSeriyaDTO.getSchoolAddressOneTamil());
			stmt.setString(14, sisuSeriyaDTO.getSchoolAddressTwoTamil());
			stmt.setString(15, sisuSeriyaDTO.getSchoolCityTamil());

			stmt.setString(16, sisuSeriyaDTO.getSchoolEmailAdd());
			stmt.setString(17, sisuSeriyaDTO.getSchoolProvinceCode());

			stmt.setString(18, sisuSeriyaDTO.getSchoolTelNo());
			stmt.setString(19, sisuSeriyaDTO.getSchoolDistrictCode());

			stmt.setString(20, sisuSeriyaDTO.getSchoolMobileNo());
			stmt.setString(21, sisuSeriyaDTO.getSchoolDivisinSecCode());

			stmt.setString(22, loginUser);
			stmt.setString(23, loginUser);

			stmt.setTimestamp(24, timestamp);
			stmt.setTimestamp(25, timestamp);

			int i = stmt.executeUpdate();

			if (i > 0) {
				isModelSave = true;
			} else {
				isModelSave = false;
			}

			String sql1 = "UPDATE public.nt_m_sisu_permit_hol_service_info \r\n" + "set sps_status = 'P'\r\n"
					+ "where sps_service_ref_no = ?";

			stmt2 = con.prepareStatement(sql1);
			stmt2.setString(1, sisuSeriyaDTO.getServiceRefNo());

			int y = stmt2.executeUpdate();

			if (y > 0) {
				isModel2Save = true;
			} else {
				isModel2Save = false;
			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(con);
		}

		return isModelSave && isModel2Save;
	}

	// Insert bank details
	@Override
	public boolean insertBankDetails(SisuSeriyaDTO sisuSeriyaDTO, String loginUser) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		boolean isModelSave = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			// if the task is already available consider this as a update and add current
			// record to the history table
			if (checkTaskDetailsInSubsidyByServiceRefNo(sisuSeriyaDTO.getServiceRefNo(), "SS005", "C")
					|| checkTaskDetailsInSubsidyByServiceRefNo(sisuSeriyaDTO.getServiceRefNo(), "SS007", "C")) {

				String sql1 = "INSERT into public.nt_h_sisu_permit_hol_service_info  "
						+ " (SELECT * FROM public.nt_m_sisu_permit_hol_service_info WHERE sps_service_ref_no = ?) ";

				stmt1 = con.prepareStatement(sql1);
				stmt1.setString(1, sisuSeriyaDTO.getServiceRefNo());
				stmt1.executeUpdate();
			}

			// if status is "R" save the status as null
			String resetStatusQuery = "";
			if (sisuSeriyaDTO.getStatusCode() != null && sisuSeriyaDTO.getStatusCode().equalsIgnoreCase("R")) {
				resetStatusQuery = ", sps_status='P', sps_is_checked=null, sps_checked_by=null, sps_checked_date=null, sps_is_recommended=null, sps_recommended_by=null, sps_recommended_date=null ";
			}

			String sql = "UPDATE public.nt_m_sisu_permit_hol_service_info \r\n"
					+ "SET  sps_account_no=?, sps_bank_name_code=?, sps_branch_code=? \r\n" + resetStatusQuery
					+ "WHERE sps_service_ref_no= ? ;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, sisuSeriyaDTO.getAccountNo());
			stmt.setString(2, sisuSeriyaDTO.getBankNameCode());
			stmt.setString(3, sisuSeriyaDTO.getBankBranchNameCode());
			stmt.setString(4, sisuSeriyaDTO.getServiceRefNo());

			int i = stmt.executeUpdate();

			if (i > 0) {
				isModelSave = true;
				// if the task is already available update the task SS011 C
				if (checkTaskDetailsInSubsidyByServiceRefNo(sisuSeriyaDTO.getServiceRefNo(), "SS005", "C")
						|| checkTaskDetailsInSubsidyByServiceRefNo(sisuSeriyaDTO.getServiceRefNo(), "SS007", "C")) {
					/* Task Update */
					long seqNo = getSeqNoFromTaskDet(con, sisuSeriyaDTO.getRequestNo(), sisuSeriyaDTO.getServiceRefNo(),
							sisuSeriyaDTO.getServiceNo());

					String sql2 = "INSERT INTO public.nt_h_subsidy_task_his \r\n"
							+ "(tsd_seq, tsd_request_no, tsd_reference_no, tsd_service_no, tsd_task_code, tsd_status, created_by, created_date)\r\n"
							+ "VALUES(?, ?, ?, ?, ?, ?, ?, ? ); \r\n" + ";";

					stmt2 = con.prepareStatement(sql2);

					stmt2.setLong(1, seqNo);
					stmt2.setString(2, sisuSeriyaDTO.getRequestNo());
					stmt2.setString(3, sisuSeriyaDTO.getServiceRefNo());
					stmt2.setString(4, sisuSeriyaDTO.getServiceNo());
					stmt2.setString(5, "SS011");
					stmt2.setString(6, "C");
					stmt2.setString(7, loginUser);
					stmt2.setTimestamp(8, timestamp);

					stmt2.executeUpdate();
				}
			} else {
				isModelSave = false;
			}
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt1);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(con);
		}

		return isModelSave;
	}

	// Get permit holder info table
	@Override
	public List<SisuSeriyaDTO> getTblPermitHolderInfo(SisuSeriyaDTO sisuSeriyaDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SisuSeriyaDTO> retriveTableData = new ArrayList<SisuSeriyaDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select sps_request_no,sps_service_ref_no,sps_service_no,sps_operator_name,sps_status\r\n"
					+ "from nt_m_sisu_permit_hol_service_info\r\n"
					+ "where sps_request_no = ? order by sps_service_ref_no desc ;";

			ps = con.prepareStatement(query);

			ps.setString(1, sisuSeriyaDTO.getRequestNo());
			rs = ps.executeQuery();

			while (rs.next()) {

				sisuSeriyaDTO = new SisuSeriyaDTO();

				sisuSeriyaDTO.setRequestNo(rs.getString("sps_request_no"));
				sisuSeriyaDTO.setServiceRefNo(rs.getString("sps_service_ref_no"));
				sisuSeriyaDTO.setServiceNo(rs.getString("sps_service_no"));
				sisuSeriyaDTO.setNameOfOperator(rs.getString("sps_operator_name"));

				if (rs.getString("sps_status").equalsIgnoreCase("O")) {
					sisuSeriyaDTO.setStatusDes("ONGOING");
				} else if (rs.getString("sps_status").equalsIgnoreCase("P")) {
					sisuSeriyaDTO.setStatusDes("PENDING");
				} else if (rs.getString("sps_status").equalsIgnoreCase("A")) {
					sisuSeriyaDTO.setStatusDes("APPROVED");
				} else if (rs.getString("sps_status").equalsIgnoreCase("R")) {
					sisuSeriyaDTO.setStatusDes("REJECTED");
				} else if (rs.getString("sps_status").equalsIgnoreCase("I")) {
					sisuSeriyaDTO.setStatusDes("INACTIVE");
				}

				sisuSeriyaDTO.setStatus(rs.getString("sps_status"));
				retriveTableData.add(sisuSeriyaDTO);

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

	// get service information by service reference no
	@Override
	public SisuSeriyaDTO getServiceInformationByServiceRefNo(String requestNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		SisuSeriyaDTO sisuSeriyaDTO = new SisuSeriyaDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "select * \r\n" + "from public.nt_m_sisu_permit_hol_service_info \r\n"
					+ "where sps_service_ref_no = ? \r\n" + "order by sps_service_ref_no desc";

			ps = con.prepareStatement(query);
			ps.setString(1, requestNo);
			rs = ps.executeQuery();

			while (rs.next()) {

				sisuSeriyaDTO.setRequestNo(rs.getString("sps_request_no"));

				sisuSeriyaDTO.setServiceRefNo(rs.getString("sps_service_ref_no"));

				sisuSeriyaDTO.setLanguageCode(rs.getString("sps_perfered_lan"));
				sisuSeriyaDTO.setServiceTypeCode(rs.getString("sps_service_type_code"));
				sisuSeriyaDTO.setBusRegNo(rs.getString("sps_bus_no"));
				sisuSeriyaDTO.setPermitNo(rs.getString("sps_permit_no"));
				sisuSeriyaDTO.setOriginCode(rs.getString("sps_origin"));
				sisuSeriyaDTO.setDistance(rs.getDouble("sps_distance"));
				sisuSeriyaDTO.setDestinationCode(rs.getString("sps_destination"));
				sisuSeriyaDTO.setTripsPerDay(rs.getInt("sps_tips_per_day"));
				sisuSeriyaDTO.setVia(rs.getString("sps_via"));

				sisuSeriyaDTO.setServiceEndDateVal(rs.getString("sps_service_start_date"));
				if (rs.getTimestamp("sps_service_start_date") != null) {
					Date startDateValueObj = new Date(rs.getTimestamp("sps_service_start_date").getTime());
					sisuSeriyaDTO.setServiceStartDateObj(startDateValueObj);
				}

				sisuSeriyaDTO.setNicNo(rs.getString("sps_nic_no"));

				if (rs.getTimestamp("sps_service_end_date") != null) {
					Date endDateValueObj = new Date(rs.getTimestamp("sps_service_end_date").getTime());
					sisuSeriyaDTO.setServiceEndDateObj(endDateValueObj);
				}

				if (rs.getString("sps_renewal_status") != null && rs.getString("sps_renewal_status").equals("P")) {
					if (rs.getTimestamp("sps_renewal_new_expire_date") != null) {
						Date serviceExpireDate = new Date(rs.getTimestamp("sps_renewal_new_expire_date").getTime());
						sisuSeriyaDTO.setServiceExpireDate(serviceExpireDate);
					}

				}
				sisuSeriyaDTO.setServiceEndDateVal(rs.getString("sps_service_end_date"));

				sisuSeriyaDTO.setNameOfOperator(rs.getString("sps_operator_name"));
				sisuSeriyaDTO.setNameOfOperatorSin(rs.getString("sps_operator_name_si"));
				sisuSeriyaDTO.setNameOfOperatorTamil(rs.getString("sps_operator_name_ta"));
				sisuSeriyaDTO.setAddressOne(rs.getString("sps_add1"));
				sisuSeriyaDTO.setAddressTwo(rs.getString("sps_add2"));
				sisuSeriyaDTO.setCity(rs.getString("sps_city"));
				sisuSeriyaDTO.setAddressOneSin(rs.getString("sps_add1_si"));
				sisuSeriyaDTO.setAdressTwoSin(rs.getString("sps_add2_si"));
				sisuSeriyaDTO.setCitySin(rs.getString("sps_city_si"));
				sisuSeriyaDTO.setAddressOneTamil(rs.getString("sps_add1_ta"));
				sisuSeriyaDTO.setAdressTwoTamil(rs.getString("sps_add2_ta"));
				sisuSeriyaDTO.setCityTamil(rs.getString("sps_city_ta"));
				sisuSeriyaDTO.setBusRegNo(rs.getString("sps_bus_no"));
				sisuSeriyaDTO.setProvinceCode(rs.getString("sps_province"));
				sisuSeriyaDTO.setTelNo(rs.getString("sps_telephone_no"));
				sisuSeriyaDTO.setDistrictCode(rs.getString("sps_district_code"));
				sisuSeriyaDTO.setMobileNo(rs.getString("sps_mobile_no"));
				sisuSeriyaDTO.setDivisionalSecCode(rs.getString("sps_dev_sec_code"));
				sisuSeriyaDTO.setAccountNo(rs.getString("sps_account_no"));
				sisuSeriyaDTO.setBankNameCode(rs.getString("sps_bank_name_code"));
				sisuSeriyaDTO.setBankBranchNameCode(rs.getString("sps_branch_code"));
				sisuSeriyaDTO.setStatusCode(rs.getString("sps_status"));
				sisuSeriyaDTO.setServiceRenewalStatus(rs.getString("sps_renewal_status"));
				sisuSeriyaDTO.setServiceNo(rs.getString("sps_service_no"));
				sisuSeriyaDTO.setRequestNo(rs.getString("sps_request_no"));
				sisuSeriyaDTO.setSltbStatus(rs.getString("is_sltb"));
				sisuSeriyaDTO.setRemark(rs.getString("sps_remark"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return sisuSeriyaDTO;
	}

	// update service information by service reference no
	@Override
	public boolean updateServiceInformationByServiceRefNo(SisuSeriyaDTO sisuSeriyaDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isModelSave = false;

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_sisu_permit_hol_service_info SET \r\n"
					+ "sps_perfered_lan=?,sps_service_type_code=?,\r\n" + "sps_bus_no=?,sps_permit_no=?,\r\n"
					+ "sps_origin=?,sps_distance=?,\r\n" + "sps_destination=?,sps_tips_per_day=?,\r\n"
					+ "sps_via=?,sps_service_start_date=?,\r\n" + "sps_nic_no=?,sps_service_end_date=?,\r\n"
					+ "sps_operator_name=?,sps_operator_name_si=?,sps_operator_name_ta=?,\r\n"
					+ "sps_add1=?,sps_add2=?,sps_city=?,\r\n" + "sps_add1_si=?,sps_add2_si=?,sps_city_si=?,\r\n"
					+ "sps_add1_ta=?,sps_add2_ta=?,sps_city_ta=?,\r\n" + "sps_province=?,\r\n"
					+ "sps_telephone_no=?,sps_district_code=?,\r\n"
					+ "sps_mobile_no=?,sps_dev_sec_code=?,sps_renewal_new_expire_date=?,sps_renewal_status = ?,is_sltb=? \r\n"
					+ "WHERE sps_service_ref_no=?;";

			stmt = con.prepareStatement(sql);

			try {
				stmt.setString(1, sisuSeriyaDTO.getLanguageCode());
				stmt.setString(2, sisuSeriyaDTO.getServiceTypeCode());
				stmt.setString(3, sisuSeriyaDTO.getBusRegNo());
				stmt.setString(4, sisuSeriyaDTO.getPermitNo());
				stmt.setString(5, sisuSeriyaDTO.getOriginCode());
				String strDistance = String.valueOf(sisuSeriyaDTO.getDistance());
				stmt.setString(6, strDistance);
				stmt.setString(7, sisuSeriyaDTO.getDestinationCode());
				stmt.setInt(8, sisuSeriyaDTO.getTripsPerDay());
				stmt.setString(9, sisuSeriyaDTO.getVia());

				Timestamp serviceStartDate = null;
				if (sisuSeriyaDTO.getServiceStartDateObj() != null) {
					serviceStartDate = new Timestamp(sisuSeriyaDTO.getServiceStartDateObj().getTime());
				}

				stmt.setTimestamp(10, serviceStartDate);
				stmt.setString(11, sisuSeriyaDTO.getNicNo());

				Timestamp serviceEndDate = null;
				if (sisuSeriyaDTO.getServiceEndDateObj() != null) {
					serviceEndDate = new Timestamp(sisuSeriyaDTO.getServiceEndDateObj().getTime());
				}

				stmt.setTimestamp(12, serviceEndDate);

				stmt.setString(13, sisuSeriyaDTO.getNameOfOperator());
				stmt.setString(14, sisuSeriyaDTO.getNameOfOperatorSin());
				stmt.setString(15, sisuSeriyaDTO.getNameOfOperatorTamil());
				stmt.setString(16, sisuSeriyaDTO.getAddressOne());
				stmt.setString(17, sisuSeriyaDTO.getAddressTwo());
				stmt.setString(18, sisuSeriyaDTO.getCity());
				stmt.setString(19, sisuSeriyaDTO.getAddressOneSin());
				stmt.setString(20, sisuSeriyaDTO.getAdressTwoSin());
				stmt.setString(21, sisuSeriyaDTO.getCitySin());
				stmt.setString(22, sisuSeriyaDTO.getAddressOneTamil());
				stmt.setString(23, sisuSeriyaDTO.getAdressTwoTamil());
				stmt.setString(24, sisuSeriyaDTO.getCityTamil());
				stmt.setString(25, sisuSeriyaDTO.getProvinceCode());
				stmt.setString(26, sisuSeriyaDTO.getTelNo());
				stmt.setString(27, sisuSeriyaDTO.getDistrictCode());
				stmt.setString(28, sisuSeriyaDTO.getMobileNo());
				stmt.setString(29, sisuSeriyaDTO.getDivisionalSecCode());

				Timestamp serviceExpireDate = null;
				if (sisuSeriyaDTO.getServiceExpireDate() != null) {
					serviceExpireDate = new Timestamp(sisuSeriyaDTO.getServiceExpireDate().getTime());
				}

				stmt.setTimestamp(30, serviceExpireDate);

				stmt.setString(31, sisuSeriyaDTO.getServiceRenewalStatus());
				stmt.setString(32, sisuSeriyaDTO.getSltbStatus());
				stmt.setString(33, sisuSeriyaDTO.getServiceRefNo());

				int i = stmt.executeUpdate();

				if (i > 0) {
					isModelSave = true;
				} else {
					isModelSave = false;
				}

				con.commit();
			} catch (SQLException e) {
				con.rollback();
				e.printStackTrace();
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

	// get school information by service reference no
	@Override
	public SisuSeriyaDTO getSchoolInformationByServiceRefNo(SisuSeriyaDTO sisuSeriyaDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select * \r\n" + "from public.nt_m_sisu_permit_hol_school_info\r\n"
					+ "where ssc_service_ref_no = ?";

			ps = con.prepareStatement(query);
			ps.setString(1, sisuSeriyaDTO.getServiceRefNo());
			rs = ps.executeQuery();

			while (rs.next()) {

				sisuSeriyaDTO.setSchoolName(rs.getString("ssc_school_name"));
				sisuSeriyaDTO.setSchoolNameSin(rs.getString("ssc_school_name_si"));
				sisuSeriyaDTO.setSchoolNameTamil(rs.getString("ssc_school_name_ta"));
				sisuSeriyaDTO.setSchoolAddressOne(rs.getString("ssc_school_add1"));
				sisuSeriyaDTO.setSchoolAdrressOneSin(rs.getString("ssc_school_add1_si"));
				sisuSeriyaDTO.setSchoolAddressOneTamil(rs.getString("ssc_school_add1_ta"));
				sisuSeriyaDTO.setSchoolAddressTwo(rs.getString("ssc_school_add2"));
				sisuSeriyaDTO.setSchoolAddressTwoSin(rs.getString("ssc_school_add2_si"));
				sisuSeriyaDTO.setSchoolAddressTwoTamil(rs.getString("ssc_school_add2_ta"));
				sisuSeriyaDTO.setSchoolCity(rs.getString("ssc_school_city"));
				sisuSeriyaDTO.setSchoolCitySin(rs.getString("ssc_school_city_si"));
				sisuSeriyaDTO.setSchoolCityTamil(rs.getString("ssc_school_city_ta"));
				sisuSeriyaDTO.setSchoolProvinceCode(rs.getString("ssc_school_province_code"));
				sisuSeriyaDTO.setSchoolDistrictCode(rs.getString("ssc_school_district_code"));
				sisuSeriyaDTO.setSchoolDivisinSecCode(rs.getString("ssc_school_div_sec_code"));
				sisuSeriyaDTO.setSchoolTelNo(rs.getString("ssc_school_tel_no"));
				sisuSeriyaDTO.setSchoolMobileNo(rs.getString("ssc_school_mobile"));
				sisuSeriyaDTO.setSchoolEmailAdd(rs.getString("ssc_school_email"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return sisuSeriyaDTO;
	}

	// update school information by service reference no
	@Override
	public boolean updateSchoolInformationByRequestNo(SisuSeriyaDTO sisuSeriyaDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isModelSave = false;

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_sisu_permit_hol_school_info\r\n"
					+ "SET ssc_school_name=?, ssc_school_name_si=?, ssc_school_name_ta=?, \r\n"
					+ "ssc_school_add1=?, ssc_school_add1_si=?, ssc_school_add1_ta=?, \r\n"
					+ "ssc_school_add2=?, ssc_school_add2_si=?, ssc_school_add2_ta=?,\r\n"
					+ "ssc_school_city=?, ssc_school_city_si=?, ssc_school_city_ta=?, \r\n"
					+ "ssc_school_province_code=?, ssc_school_district_code=?, ssc_school_div_sec_code=?, \r\n"
					+ "ssc_school_tel_no=?, ssc_school_mobile=?, ssc_school_email=? \r\n" + "WHERE ssc_request_no=?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, sisuSeriyaDTO.getSchoolName());
			stmt.setString(2, sisuSeriyaDTO.getSchoolNameSin());
			stmt.setString(3, sisuSeriyaDTO.getSchoolNameTamil());
			stmt.setString(4, sisuSeriyaDTO.getSchoolAddressOne());
			stmt.setString(5, sisuSeriyaDTO.getSchoolAdrressOneSin());
			stmt.setString(6, sisuSeriyaDTO.getSchoolAddressOneTamil());
			stmt.setString(7, sisuSeriyaDTO.getSchoolAddressTwo());
			stmt.setString(8, sisuSeriyaDTO.getSchoolAddressTwoSin());
			stmt.setString(9, sisuSeriyaDTO.getSchoolAddressTwoTamil());
			stmt.setString(10, sisuSeriyaDTO.getSchoolCity());
			stmt.setString(11, sisuSeriyaDTO.getSchoolCitySin());
			stmt.setString(12, sisuSeriyaDTO.getSchoolCityTamil());
			stmt.setString(13, sisuSeriyaDTO.getSchoolProvinceCode());
			stmt.setString(14, sisuSeriyaDTO.getSchoolDistrictCode());
			stmt.setString(15, sisuSeriyaDTO.getSchoolDivisinSecCode());
			stmt.setString(16, sisuSeriyaDTO.getSchoolTelNo());
			stmt.setString(17, sisuSeriyaDTO.getSchoolMobileNo());
			stmt.setString(18, sisuSeriyaDTO.getSchoolEmailAdd());

			stmt.setString(19, sisuSeriyaDTO.getRequestNo());

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

	// retrieve data from request
	@Override
	public SisuSeriyaDTO retrieveSchoolInfoFromRequest(SisuSeriyaDTO sisuSeriyaDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  \r\n" + "sri_school_name, sri_school_name_si, sri_school_name_ta, \r\n"
					+ "sri_school_add1,sri_school_add1_si, sri_school_add1_ta, \r\n"
					+ "sri_school_add2, sri_school_add2_si, sri_school_add2_ta,\r\n"
					+ "sri_school_city, sri_school_city_si, sri_school_city_ta, \r\n"
					+ "sri_school_province, sri_school_district, sri_school_div_sec, \r\n"
					+ "sri_school_tel_no, sri_school_mobile, sri_school_email\r\n"
					+ "FROM public.nt_m_sisu_requestor_info\r\n" + "where sri_request_no = ?";

			ps = con.prepareStatement(query);
			ps.setString(1, sisuSeriyaDTO.getRequestNo());
			rs = ps.executeQuery();

			while (rs.next()) {

				sisuSeriyaDTO.setSchoolName(rs.getString("sri_school_name"));
				sisuSeriyaDTO.setSchoolNameSin(rs.getString("sri_school_name_si"));
				sisuSeriyaDTO.setSchoolNameTamil(rs.getString("sri_school_name_ta"));
				sisuSeriyaDTO.setSchoolAddressOne(rs.getString("sri_school_add1"));
				sisuSeriyaDTO.setSchoolAdrressOneSin(rs.getString("sri_school_add1_si"));
				sisuSeriyaDTO.setSchoolAddressOneTamil(rs.getString("sri_school_add1_ta"));
				sisuSeriyaDTO.setSchoolAddressTwo(rs.getString("sri_school_add2"));
				sisuSeriyaDTO.setSchoolAddressTwoSin(rs.getString("sri_school_add2_si"));
				sisuSeriyaDTO.setSchoolAddressTwoTamil(rs.getString("sri_school_add2_ta"));
				sisuSeriyaDTO.setSchoolCity(rs.getString("sri_school_city"));
				sisuSeriyaDTO.setSchoolCitySin(rs.getString("sri_school_city_si"));
				sisuSeriyaDTO.setSchoolCityTamil(rs.getString("sri_school_city_ta"));
				sisuSeriyaDTO.setSchoolProvinceCode(rs.getString("sri_school_province"));
				sisuSeriyaDTO.setSchoolDistrictCode(rs.getString("sri_school_district"));
				sisuSeriyaDTO.setSchoolDivisinSecCode(rs.getString("sri_school_div_sec"));
				sisuSeriyaDTO.setSchoolTelNo(rs.getString("sri_school_tel_no"));
				sisuSeriyaDTO.setSchoolMobileNo(rs.getString("sri_school_mobile"));
				sisuSeriyaDTO.setSchoolEmailAdd(rs.getString("sri_school_email"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return sisuSeriyaDTO;
	}

	@Override
	public List<SisuSeriyaDTO> drpdRequestNoList(SisuSeriyaDTO sisuSeriyaDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SisuSeriyaDTO> drpdRequestNoList = new ArrayList<SisuSeriyaDTO>();

		try {
			String whereClose = "";
			if (sisuSeriyaDTO.getRequestStartDateString() != null
					&& !sisuSeriyaDTO.getRequestStartDateString().isEmpty()
					&& !sisuSeriyaDTO.getRequestStartDateString().equalsIgnoreCase("")) {
				if (sisuSeriyaDTO.getRequestEndDateString() != null
						&& !sisuSeriyaDTO.getRequestStartDateString().isEmpty()
						&& !sisuSeriyaDTO.getRequestStartDateString().equalsIgnoreCase("")) {
					whereClose = " where sri_request_date >= '" + sisuSeriyaDTO.getRequestStartDateString()
							+ "' and sri_request_date <= '" + sisuSeriyaDTO.getRequestEndDateString() + "' and ";
				} else {
					whereClose = " where sri_request_date <= '" + sisuSeriyaDTO.getRequestStartDateString() + "' and";// >=
																														// change
																														// as
																														// <=
																														// Live
																														// issue
				}
			} else {
				if (sisuSeriyaDTO.getRequestEndDateString() != null
						&& !sisuSeriyaDTO.getRequestStartDateString().isEmpty()
						&& !sisuSeriyaDTO.getRequestStartDateString().equalsIgnoreCase("")) {
					whereClose = " where sri_request_date <= '" + sisuSeriyaDTO.getRequestEndDateString() + "' and ";
				} else {
					// when both req dates are empty - onOperatorDepoNameChange
					whereClose = " where ";
				}

			}
			con = ConnectionManager.getConnection();

			if (sisuSeriyaDTO.getNameOfOperator() != null && !sisuSeriyaDTO.getNameOfOperator().isEmpty()
					&& !sisuSeriyaDTO.getNameOfOperator().equalsIgnoreCase("")) {
				String query = "select distinct a.sri_request_no as srinrequestno from public.nt_m_sisu_requestor_info a inner join public.nt_m_sisu_permit_hol_service_info b on a.sri_request_no = b.sps_request_no "
						+ whereClose + " b.sps_operator_name = '" + sisuSeriyaDTO.getNameOfOperator()
						+ "' order by a.sri_request_no;";

				ps = con.prepareStatement(query);
				rs = ps.executeQuery();

				while (rs.next()) {
					SisuSeriyaDTO ss = new SisuSeriyaDTO();
					ss.setRequestNo(rs.getString("srinrequestno"));
					drpdRequestNoList.add(ss);
				}
			} else {
				String query = "select sri_request_no from public.nt_m_sisu_requestor_info" + whereClose
						+ " sri_request_no is not null and sri_request_no !='' order by sri_request_no;";

				ps = con.prepareStatement(query);
				rs = ps.executeQuery();

				while (rs.next()) {
					SisuSeriyaDTO ss = new SisuSeriyaDTO();
					ss.setRequestNo(rs.getString("sri_request_no"));
					drpdRequestNoList.add(ss);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return drpdRequestNoList;
	}

	@Override
	public List<SisuSeriyaDTO> drpdOperatorDepoNameList(SisuSeriyaDTO sisuSeriyaDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SisuSeriyaDTO> drpdOperatorDepoNameList = new ArrayList<SisuSeriyaDTO>();

		try {

			String whereClose = "";
			if (sisuSeriyaDTO.getRequestStartDateString() != null
					&& !sisuSeriyaDTO.getRequestStartDateString().isEmpty()
					&& !sisuSeriyaDTO.getRequestStartDateString().equalsIgnoreCase("")) {

				if (sisuSeriyaDTO.getRequestEndDateString() != null
						&& !sisuSeriyaDTO.getRequestStartDateString().isEmpty()
						&& !sisuSeriyaDTO.getRequestStartDateString().equalsIgnoreCase("")) {
					whereClose = " where a.sri_request_date >= '" + sisuSeriyaDTO.getRequestStartDateString()
							+ "' and a.sri_request_date <= '" + sisuSeriyaDTO.getRequestEndDateString() + "' ";
				} else {
					whereClose = " where a.sri_request_date >= '" + sisuSeriyaDTO.getRequestStartDateString() + "' ";
				}

			} else {
				whereClose = " where a.sri_request_date <= '" + sisuSeriyaDTO.getRequestEndDateString() + "' ";
			}
			con = ConnectionManager.getConnection();

			String query = "select distinct b.sps_operator_name as nameofoperator from public.nt_m_sisu_requestor_info a inner join public.nt_m_sisu_permit_hol_service_info b on a.sri_request_no = b.sps_request_no "
					+ whereClose
					+ " and b.sps_status='A' and b.sps_operator_name is not null and b.sps_operator_name != '' order by b.sps_operator_name;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				SisuSeriyaDTO ss = new SisuSeriyaDTO();
				ss.setNameOfOperator(rs.getString("nameofoperator"));
				drpdOperatorDepoNameList.add(ss);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return drpdOperatorDepoNameList;
	}

	@Override
	public List<SisuSeriyaDTO> getOperatorDepoNameDropDownForSisuApproval(SisuSeriyaDTO sisuSeriyaDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SisuSeriyaDTO> drpdOperatorDepoNameList = new ArrayList<SisuSeriyaDTO>();

		try {

			String whereClose = "";
			if (sisuSeriyaDTO.getRequestStartDateString() != null
					&& !sisuSeriyaDTO.getRequestStartDateString().isEmpty()
					&& !sisuSeriyaDTO.getRequestStartDateString().equalsIgnoreCase("")) {

				if (sisuSeriyaDTO.getRequestEndDateString() != null
						&& !sisuSeriyaDTO.getRequestStartDateString().isEmpty()
						&& !sisuSeriyaDTO.getRequestStartDateString().equalsIgnoreCase("")) {
					whereClose = " where a.sri_request_date >= '" + sisuSeriyaDTO.getRequestStartDateString()
							+ "' and a.sri_request_date <= '" + sisuSeriyaDTO.getRequestEndDateString() + "' ";
				} else {
					whereClose = " where a.sri_request_date >= '" + sisuSeriyaDTO.getRequestStartDateString() + "' ";
				}

			} else {
				whereClose = " where a.sri_request_date <= '" + sisuSeriyaDTO.getRequestEndDateString() + "' ";
			}
			con = ConnectionManager.getConnection();

			String query = "select distinct b.sps_operator_name as nameofoperator from public.nt_m_sisu_requestor_info a inner join public.nt_m_sisu_permit_hol_service_info b on a.sri_request_no = b.sps_request_no "
					+ whereClose
					+ " and (b.sps_renewal_status is null or b.sps_renewal_status!='P') and b.sps_operator_name is not null and b.sps_operator_name != '' order by b.sps_operator_name;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				SisuSeriyaDTO ss = new SisuSeriyaDTO();
				ss.setNameOfOperator(rs.getString("nameofoperator"));
				drpdOperatorDepoNameList.add(ss);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return drpdOperatorDepoNameList;
	}

	@Override
	public List<SisuSeriyaDTO> drpdOperatorDepoNameListForRenewal(String startDate, String endDate) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SisuSeriyaDTO> drpdOperatorDepoNameList = new ArrayList<SisuSeriyaDTO>();

		try {

			String whereClose = "";
			if (startDate != null && !startDate.isEmpty() && !startDate.equalsIgnoreCase("")) {

				if (endDate != null && !endDate.isEmpty() && !endDate.equalsIgnoreCase("")) {
					whereClose = " where b.sps_created_date >= '" + startDate + "' and b.sps_created_date <= '"
							+ endDate + "' ";
				} else {
					whereClose = " where b.sps_created_date >= '" + startDate + "' ";
				}

			} else {
				whereClose = " where b.sps_created_date <= '" + endDate + "' ";
			}
			con = ConnectionManager.getConnection();

			String query = "select distinct b.sps_operator_name as nameofoperator from public.nt_m_sisu_permit_hol_service_info b "
					+ whereClose
					+ " and sps_renewal_status in ('A','P','R') and b.sps_operator_name is not null and b.sps_operator_name != ''"
					+ " and b.sps_service_no is not null and b.sps_service_no != '' order by b.sps_operator_name;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				SisuSeriyaDTO ss = new SisuSeriyaDTO();
				ss.setNameOfOperator(rs.getString("nameofoperator"));
				drpdOperatorDepoNameList.add(ss);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return drpdOperatorDepoNameList;
	}

	@Override
	public List<SisuSeriyaDTO> getRefNoListForRenewal(String startDate, String endDate, String nameOfOperator) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SisuSeriyaDTO> drpdOperatorDepoNameList = new ArrayList<SisuSeriyaDTO>();

		try {

			String whereClose = "";
			boolean withoutAnd = false;
			if (startDate != null && !startDate.isEmpty() && !startDate.equalsIgnoreCase("")) {

				if (endDate != null && !endDate.isEmpty() && !endDate.equalsIgnoreCase("")) {
					whereClose = " where b.sps_created_date >= '" + startDate + "' and b.sps_created_date <= '"
							+ endDate + "' ";
				} else {
					whereClose = " where b.sps_created_date >= '" + startDate + "' ";
				}

			} else {
				if (endDate != null && !endDate.isEmpty() && !endDate.equalsIgnoreCase("")) {
					whereClose = " where b.sps_created_date <= '" + endDate + "' ";
				} else {
					whereClose = " where ";
					withoutAnd = true;
				}
			}

			if (nameOfOperator != null && !nameOfOperator.trim().equals("")) {
				if (withoutAnd) {
					whereClose = whereClose + " b.sps_operator_name = '" + nameOfOperator + "' ";
				} else {
					whereClose = whereClose + " and b.sps_operator_name = '" + nameOfOperator + "' ";
				}

			}
			con = ConnectionManager.getConnection();

			String query = "select distinct b.sps_service_ref_no as serviceref from public.nt_m_sisu_permit_hol_service_info b "
					+ whereClose
					+ " and sps_renewal_status in ('A','P','R') and b.sps_service_ref_no is not null and b.sps_service_ref_no != ''"
					+ " and b.sps_service_no is not null and b.sps_service_no != '' order by b.sps_service_ref_no;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				SisuSeriyaDTO ss = new SisuSeriyaDTO();
				ss.setServiceRefNo(rs.getString("serviceref"));
				drpdOperatorDepoNameList.add(ss);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return drpdOperatorDepoNameList;
	}

	@Override
	public List<SisuSeriyaDTO> getServiceNoListForRenewal(String startDate, String endDate, String nameOfOperator) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SisuSeriyaDTO> drpdOperatorDepoNameList = new ArrayList<SisuSeriyaDTO>();

		try {

			String whereClose = "";
			boolean withoutAnd = false;
			if (startDate != null && !startDate.isEmpty() && !startDate.equalsIgnoreCase("")) {

				if (endDate != null && !endDate.isEmpty() && !endDate.equalsIgnoreCase("")) {
					whereClose = " where b.sps_created_date >= '" + startDate + "' and b.sps_created_date <= '"
							+ endDate + "' ";
				} else {
					whereClose = " where b.sps_created_date >= '" + startDate + "' ";
				}

			} else {
				if (endDate != null && !endDate.isEmpty() && !endDate.equalsIgnoreCase("")) {
					whereClose = " where b.sps_created_date <= '" + endDate + "' ";
				} else {
					whereClose = " where ";
					withoutAnd = true;
				}

			}

			if (nameOfOperator != null && !nameOfOperator.trim().equals("")) {
				if (withoutAnd) {
					whereClose = whereClose + " b.sps_operator_name = '" + nameOfOperator + "' ";
				} else {
					whereClose = whereClose + " and b.sps_operator_name = '" + nameOfOperator + "' ";
				}

			}

			con = ConnectionManager.getConnection();

			String query = "select distinct b.sps_service_no as serviceno from public.nt_m_sisu_permit_hol_service_info b "
					+ whereClose
					+ " and sps_renewal_status in ('A','P','R') and b.sps_service_no is not null and b.sps_service_no != '' order by b.sps_service_no;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				SisuSeriyaDTO ss = new SisuSeriyaDTO();
				ss.setServiceNo(rs.getString("serviceno"));
				ss.setServiceAgreementNo(rs.getString("serviceno"));
				drpdOperatorDepoNameList.add(ss);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return drpdOperatorDepoNameList;
	}

	@Override
	public List<SisuSeriyaDTO> drpdOperatorDepoNameListByRequestNoServiceRefNo(SisuSeriyaDTO sisuSeriyaDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SisuSeriyaDTO> drpdOperatorDepoNameList = new ArrayList<SisuSeriyaDTO>();

		try {

			String whereClose = " where ";
			if (sisuSeriyaDTO.getRequestNo() != null && !sisuSeriyaDTO.getRequestNo().isEmpty()
					&& !sisuSeriyaDTO.getRequestNo().equalsIgnoreCase("") && sisuSeriyaDTO.getServiceRefNo() != null
					&& !sisuSeriyaDTO.getServiceRefNo().isEmpty()
					&& !sisuSeriyaDTO.getServiceRefNo().equalsIgnoreCase("")) {
				whereClose = whereClose + " sps_request_no='" + sisuSeriyaDTO.getRequestNo()
						+ "' and sps_service_ref_no='" + sisuSeriyaDTO.getServiceRefNo() + "' and ";
			} else if (sisuSeriyaDTO.getRequestNo() != null && !sisuSeriyaDTO.getRequestNo().isEmpty()
					&& !sisuSeriyaDTO.getRequestNo().equalsIgnoreCase("")) {
				whereClose = whereClose + " sps_request_no='" + sisuSeriyaDTO.getRequestNo() + "' and ";
			} else if (sisuSeriyaDTO.getServiceRefNo() != null && !sisuSeriyaDTO.getServiceRefNo().isEmpty()
					&& !sisuSeriyaDTO.getServiceRefNo().equalsIgnoreCase("")) {
				whereClose = whereClose + " sps_service_ref_no='" + sisuSeriyaDTO.getServiceRefNo() + "' and ";
			}
			con = ConnectionManager.getConnection();

			String query = "select distinct b.sps_operator_name as nameofoperator from public.nt_m_sisu_requestor_info a inner join public.nt_m_sisu_permit_hol_service_info b on a.sri_request_no = b.sps_request_no "
					+ whereClose
					+ " b.sps_status='A' and b.sps_operator_name is not null and b.sps_operator_name != '' order by b.sps_operator_name;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				SisuSeriyaDTO ss = new SisuSeriyaDTO();
				ss.setNameOfOperator(rs.getString("nameofoperator"));
				drpdOperatorDepoNameList.add(ss);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return drpdOperatorDepoNameList;
	}

	// permit renewals
	@Override
	public boolean updateRenewalStatus(String status, String serviceRefNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isModelSave = false;

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_sisu_permit_hol_service_info\r\n" + "SET sps_renewal_status = ? \r\n"
					+ "WHERE sps_service_ref_no = ? ;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, status);
			stmt.setString(2, serviceRefNo);

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
	public SisuSeriyaDTO insertRenewalServiceInformation(SisuSeriyaDTO sisuSeriyaDTO, String loginUser) {
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

			con = ConnectionManager.getConnection();

			if (sisuSeriyaDTO.getCancellationStatus() != null
					&& sisuSeriyaDTO.getCancellationStatus().trim().equals("Y")) {
				// insert current record to temp table
				String sql1 = "INSERT into public.nt_temp_sisu_permit_hol_service_info  "
						+ " (SELECT * FROM public.nt_m_sisu_permit_hol_service_info WHERE sps_service_ref_no = ?) ";

				ps2 = con.prepareStatement(sql1);
				ps2.setString(1, sisuSeriyaDTO.getServiceRefNo());
				ps2.executeUpdate();

				// Cancellation process
				String query1 = "UPDATE public.nt_m_sisu_permit_hol_service_info  SET is_cancellation='Y', sps_renewal_status='P', "
						+ "sps_is_checked=null, sps_checked_by=null, sps_checked_date=null, sps_is_recommended=null, sps_recommended_by=null, sps_recommended_date=null "
						+ "WHERE sps_service_ref_no=?;";

				ps = con.prepareStatement(query1);
				ps.setString(1, sisuSeriyaDTO.getServiceRefNo());

				int i = ps.executeUpdate();

				if (i > 0) {
					isModelSave = true;
					sisuSeriyaDTO.setDataSave(true);

					// update task table and add to task history
					// add task Sisu Sariya Cancellation Checked
					updateTaskStatusCompletedSubsidyTaskTable(con, sisuSeriyaDTO.getRequestNo(),
							sisuSeriyaDTO.getServiceNo(), sisuSeriyaDTO.getServiceRefNo(), "SS008", "C", loginUser);
				} else {
					isModelSave = false;
					sisuSeriyaDTO.setDataSave(false);
				}

			} else {
				// create a new application

				date = dateFormat.parse(dateStr);
				timestamp = new Timestamp(date.getTime());

				long seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_m_sisu_permit_hol_service_info");

				String query = "INSERT INTO public.nt_m_sisu_permit_hol_service_info "
						+ "(sps_seq,sps_request_no,sps_service_ref_no, sps_perfered_lan,sps_service_type_code,"
						+ " sps_bus_no,sps_permit_no,sps_origin,sps_distance,"
						+ " sps_destination,sps_tips_per_day, sps_via,sps_service_start_date,"
						+ " sps_nic_no,sps_service_end_date,"
						+ " sps_operator_name,sps_operator_name_si,sps_operator_name_ta,"
						+ " sps_add1,sps_add2,sps_city, sps_add1_si,sps_add2_si,sps_city_si,"
						+ " sps_add1_ta,sps_add2_ta,sps_city_ta, sps_status,sps_province,"
						+ " sps_telephone_no,sps_district_code, sps_mobile_no,sps_dev_sec_code,"
						+ " sps_created_by,sps_modified_by,sps_created_date,sps_modified_date,"
						+ " sps_renewal_status,sps_renewal_new_expire_date,sps_service_no, sps_account_no, sps_bank_name_code, sps_branch_code, is_cancellation, sps_remark, is_sltb ,sps_service_agreement_no"
						+ " )values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

				ps = con.prepareStatement(query);
				ps.setLong(1, seqNo);
				ps.setString(2, sisuSeriyaDTO.getRequestNo());

				sisuSeriyaDTO.setOldServiceRefNo(sisuSeriyaDTO.getServiceRefNo());

				String generatedServiceReferenceNo = generateServiceReferenceNo();

				updateNumberGeneration("REF", generatedServiceReferenceNo, loginUser);

				sisuSeriyaDTO.setServiceRefNo(generatedServiceReferenceNo);
				ps.setString(3, generatedServiceReferenceNo);
				ps.setString(4, sisuSeriyaDTO.getLanguageCode());
				ps.setString(5, sisuSeriyaDTO.getServiceTypeCode());
				ps.setString(6, sisuSeriyaDTO.getBusRegNo());
				ps.setString(7, sisuSeriyaDTO.getPermitNo());
				ps.setString(8, sisuSeriyaDTO.getOriginCode());
//				ps.setDouble(9, sisuSeriyaDTO.getDistance());

				String strDistance = String.valueOf(sisuSeriyaDTO.getDistance());
				ps.setString(9, strDistance);

				ps.setString(10, sisuSeriyaDTO.getDestinationCode());
				ps.setInt(11, sisuSeriyaDTO.getTripsPerDay());
				ps.setString(12, sisuSeriyaDTO.getVia());

				Timestamp serviceStartDate = null;
				if (sisuSeriyaDTO.getServiceStartDateObj() != null) {
					serviceStartDate = new Timestamp(sisuSeriyaDTO.getServiceStartDateObj().getTime());
				}

				ps.setTimestamp(13, serviceStartDate);
				ps.setString(14, sisuSeriyaDTO.getNicNo());

				Timestamp serviceEndDate = null;
				if (sisuSeriyaDTO.getServiceEndDateObj() != null) {
					serviceEndDate = new Timestamp(sisuSeriyaDTO.getServiceEndDateObj().getTime());
				}

				ps.setTimestamp(15, serviceEndDate);
				ps.setString(16, sisuSeriyaDTO.getNameOfOperator());
				ps.setString(17, sisuSeriyaDTO.getNameOfOperatorSin());
				ps.setString(18, sisuSeriyaDTO.getNameOfOperatorTamil());
				ps.setString(19, sisuSeriyaDTO.getAddressOne());
				ps.setString(20, sisuSeriyaDTO.getAddressTwo());
				ps.setString(21, sisuSeriyaDTO.getCity());
				ps.setString(22, sisuSeriyaDTO.getAddressOneSin());
				ps.setString(23, sisuSeriyaDTO.getAdressTwoSin());
				ps.setString(24, sisuSeriyaDTO.getCitySin());
				ps.setString(25, sisuSeriyaDTO.getAddressOneTamil());
				ps.setString(26, sisuSeriyaDTO.getAdressTwoTamil());
				ps.setString(27, sisuSeriyaDTO.getCityTamil());
				ps.setString(28, sisuSeriyaDTO.getStatusCode());
				ps.setString(29, sisuSeriyaDTO.getProvinceCode());
				ps.setString(30, sisuSeriyaDTO.getTelNo());
				ps.setString(31, sisuSeriyaDTO.getDistrictCode());
				ps.setString(32, sisuSeriyaDTO.getMobileNo());
				ps.setString(33, sisuSeriyaDTO.getDivisionalSecCode());
				ps.setString(34, loginUser);
				ps.setString(35, loginUser);
				ps.setTimestamp(36, timestamp);
				ps.setTimestamp(37, timestamp);
				ps.setString(38, sisuSeriyaDTO.getServiceRenewalStatus());

				Timestamp renewalEndDate = null;
				if (sisuSeriyaDTO.getServiceExpireDate() != null) {
					renewalEndDate = new Timestamp(sisuSeriyaDTO.getServiceExpireDate().getTime());
				}
				ps.setTimestamp(39, renewalEndDate);
				ps.setString(40, sisuSeriyaDTO.getServiceNo());

				ps.setString(41, sisuSeriyaDTO.getAccountNo());
				ps.setString(42, sisuSeriyaDTO.getBankNameCode());
				ps.setString(43, sisuSeriyaDTO.getBankBranchNameCode());
				ps.setString(44, sisuSeriyaDTO.getCancellationStatus());
				ps.setString(45, sisuSeriyaDTO.getRemark());
				ps.setString(46, sisuSeriyaDTO.getSltbStatus());
				ps.setString(47, sisuSeriyaDTO.getServiceNo());

				int i = ps.executeUpdate();

				if (i > 0) {
					isModelSave = true;
					sisuSeriyaDTO.setDataSave(true);
				} else {
					isModelSave = false;
					sisuSeriyaDTO.setDataSave(false);
				}

			}

			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (!isModelSave) {
				sisuSeriyaDTO.setServiceRefNo("");
				sisuSeriyaDTO.setDataSave(false);
			}

			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(con);
		}
		return sisuSeriyaDTO;
	}

	@Override
	public boolean updateRenewalServiceInformation(SisuSeriyaDTO sisuSeriyaDTO, String loginUser) {
		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		boolean isModelSave = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			// if status is "R" save the status as null
			String resetStatusQuery = "";
			if (sisuSeriyaDTO.getStatusCode().equalsIgnoreCase("R")) {
				resetStatusQuery = ", sps_status='P', sps_is_checked=null, sps_checked_by=null, sps_checked_date=null, sps_is_recommended=null, sps_recommended_by=null, sps_recommended_date=null ";
			}

			String sql2 = "UPDATE public.nt_m_sisu_permit_hol_service_info  SET sps_nic_no= ?, sps_perfered_lan= ?, "
					+ "sps_operator_name= ?, sps_operator_name_si= ?, sps_operator_name_ta= ?, sps_add1= ?, sps_add1_si= ?, "
					+ "sps_add1_ta= ?, sps_add2= ?, sps_add2_si= ?, sps_add2_ta= ?, sps_city= ?, sps_city_si= ?, sps_city_ta= ?, "
					+ "sps_service_type_code= ?, sps_permit_no= ?, sps_bus_no= ?, sps_origin= ?, sps_destination= ?, "
					+ "sps_via= ?, sps_distance= ?, sps_tips_per_day= ?, sps_service_start_date= ?, sps_service_end_date= ?, "
					+ "sps_province= ?, sps_district_code= ?,sps_dev_sec_code= ?, sps_telephone_no= ?, sps_mobile_no= ?, "
					+ "sps_account_no= ?, sps_bank_name_code= ?, sps_branch_code= ?,is_sltb= ?,"
					+ "sps_modified_by= ?, sps_modified_date= ?, is_cancellation=?, sps_remark=? ,sps_renewal_new_expire_date=? "
					+ resetStatusQuery + "WHERE sps_service_ref_no=?;";

			stmt = con.prepareStatement(sql2);

			stmt.setString(1, sisuSeriyaDTO.getNicNo());
			stmt.setString(2, sisuSeriyaDTO.getLanguageCode());
			stmt.setString(3, sisuSeriyaDTO.getNameOfOperator());
			stmt.setString(4, sisuSeriyaDTO.getNameOfOperatorSin());
			stmt.setString(5, sisuSeriyaDTO.getNameOfOperatorTamil());
			stmt.setString(6, sisuSeriyaDTO.getAddressOne());
			stmt.setString(7, sisuSeriyaDTO.getAddressOneSin());
			stmt.setString(8, sisuSeriyaDTO.getAddressOneTamil());
			stmt.setString(9, sisuSeriyaDTO.getAddressTwo());
			stmt.setString(10, sisuSeriyaDTO.getAdressTwoSin());
			stmt.setString(11, sisuSeriyaDTO.getAdressTwoTamil());
			stmt.setString(12, sisuSeriyaDTO.getCity());
			stmt.setString(13, sisuSeriyaDTO.getCitySin());
			stmt.setString(14, sisuSeriyaDTO.getCityTamil());
			stmt.setString(15, sisuSeriyaDTO.getServiceTypeCode());
			stmt.setString(16, sisuSeriyaDTO.getPermitNo());
			stmt.setString(17, sisuSeriyaDTO.getBusRegNo());
			stmt.setString(18, sisuSeriyaDTO.getOriginCode());
			stmt.setString(19, sisuSeriyaDTO.getDestinationCode());
			stmt.setString(20, sisuSeriyaDTO.getVia());
			stmt.setString(21, Double.toString(sisuSeriyaDTO.getDistance()));
			stmt.setString(22, Integer.toString(sisuSeriyaDTO.getTripsPerDay()));

			if (sisuSeriyaDTO.getServiceStartDateObj() != null) {
				Timestamp serviceStratDate = new Timestamp(sisuSeriyaDTO.getServiceStartDateObj().getTime());
				stmt.setTimestamp(23, serviceStratDate);
			} else {
				stmt.setTimestamp(23, null);
			}

			if (sisuSeriyaDTO.getServiceEndDateObj() != null) {
				Timestamp serviceExpireDate = new Timestamp(sisuSeriyaDTO.getServiceEndDateObj().getTime());
				stmt.setTimestamp(24, serviceExpireDate);
			} else {
				stmt.setTimestamp(24, null);
			}

			stmt.setString(25, sisuSeriyaDTO.getProvinceCode());
			stmt.setString(26, sisuSeriyaDTO.getDistrictCode());
			stmt.setString(27, sisuSeriyaDTO.getDivisionalSecCode());
			stmt.setString(28, sisuSeriyaDTO.getTelNo());
			stmt.setString(29, sisuSeriyaDTO.getMobileNo());
			stmt.setString(30, sisuSeriyaDTO.getAccountNo());
			stmt.setString(31, sisuSeriyaDTO.getBankNameCode());
			stmt.setString(32, sisuSeriyaDTO.getBankBranchNameCode());
			stmt.setString(33, sisuSeriyaDTO.getSltbStatus());

			stmt.setString(34, loginUser);
			stmt.setTimestamp(35, timestamp);

			stmt.setString(36, sisuSeriyaDTO.getCancellationStatus());
			stmt.setString(37, sisuSeriyaDTO.getRemark());

			if (sisuSeriyaDTO.getServiceExpireDate() != null) {
				Timestamp newRenewalExpireDate = new Timestamp(sisuSeriyaDTO.getServiceExpireDate().getTime());
				stmt.setTimestamp(38, newRenewalExpireDate);
			} else {
				stmt.setTimestamp(38, null);
			}
			stmt.setString(39, sisuSeriyaDTO.getServiceRefNo());

			int i = stmt.executeUpdate();

			if (i > 0) {
				isModelSave = true;
				long seqNo = getSeqNoFromTaskDet(con, sisuSeriyaDTO.getRequestNo(), sisuSeriyaDTO.getServiceRefNo(),
						sisuSeriyaDTO.getServiceNo());

				String sql = "INSERT INTO public.nt_h_subsidy_task_his \r\n"
						+ "(tsd_seq, tsd_request_no, tsd_reference_no, tsd_service_no, tsd_task_code, tsd_status, created_by, created_date)\r\n"
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ? ); \r\n" + ";";

				stmt2 = con.prepareStatement(sql);

				stmt2.setLong(1, seqNo);
				stmt2.setString(2, sisuSeriyaDTO.getRequestNo());
				stmt2.setString(3, sisuSeriyaDTO.getServiceRefNo());
				stmt2.setString(4, sisuSeriyaDTO.getServiceNo());
				stmt2.setString(5, "SS009");
				stmt2.setString(6, "C");
				stmt2.setString(7, loginUser);
				stmt2.setTimestamp(8, timestamp);

				stmt2.executeUpdate();

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
	public SisuSeriyaDTO getSchoolInformationByRequestNo(SisuSeriyaDTO sisuSeriyaDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select * \r\n" + "from public.nt_m_sisu_permit_hol_school_info\r\n"
					+ "where ssc_request_no = ?";

			ps = con.prepareStatement(query);
			ps.setString(1, sisuSeriyaDTO.getRequestNo());
			rs = ps.executeQuery();

			while (rs.next()) {

				sisuSeriyaDTO.setSchoolName(rs.getString("ssc_school_name"));
				sisuSeriyaDTO.setSchoolNameSin(rs.getString("ssc_school_name_si"));
				sisuSeriyaDTO.setSchoolNameTamil(rs.getString("ssc_school_name_ta"));
				sisuSeriyaDTO.setSchoolAddressOne(rs.getString("ssc_school_add1"));
				sisuSeriyaDTO.setSchoolAdrressOneSin(rs.getString("ssc_school_add1_si"));
				sisuSeriyaDTO.setSchoolAddressOneTamil(rs.getString("ssc_school_add1_ta"));
				sisuSeriyaDTO.setSchoolAddressTwo(rs.getString("ssc_school_add2"));
				sisuSeriyaDTO.setSchoolAddressTwoSin(rs.getString("ssc_school_add2_si"));
				sisuSeriyaDTO.setSchoolAddressTwoTamil(rs.getString("ssc_school_add2_ta"));
				sisuSeriyaDTO.setSchoolCity(rs.getString("ssc_school_city"));
				sisuSeriyaDTO.setSchoolCitySin(rs.getString("ssc_school_city_si"));
				sisuSeriyaDTO.setSchoolCityTamil(rs.getString("ssc_school_city_ta"));
				sisuSeriyaDTO.setSchoolProvinceCode(rs.getString("ssc_school_province_code"));
				sisuSeriyaDTO.setSchoolDistrictCode(rs.getString("ssc_school_district_code"));
				sisuSeriyaDTO.setSchoolDivisinSecCode(rs.getString("ssc_school_div_sec_code"));
				sisuSeriyaDTO.setSchoolTelNo(rs.getString("ssc_school_tel_no"));
				sisuSeriyaDTO.setSchoolMobileNo(rs.getString("ssc_school_mobile"));
				sisuSeriyaDTO.setSchoolEmailAdd(rs.getString("ssc_school_email"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return sisuSeriyaDTO;
	}

	@Override
	public List<SisuSeriyaDTO> getRenewalServiceNoList() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SisuSeriyaDTO> drpdServiceNoList = new ArrayList<SisuSeriyaDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct sps_service_no  from public.nt_m_sisu_permit_hol_service_info \r\n"
					+ "WHERE sps_service_ref_no is not null and sps_service_ref_no !='' \r\n"
					+ "and ((sps_status='A' and is_cancellation is null) or (sps_status='P' and sps_renewal_status in ('P','R'))) \r\n"
					+ "order by sps_service_no desc";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				SisuSeriyaDTO ss = new SisuSeriyaDTO();
				ss.setServiceNo(rs.getString("sps_service_no"));
				drpdServiceNoList.add(ss);

			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return drpdServiceNoList;
	}

	@Override
	public List<SisuSeriyaDTO> getRenewalServiceNoList(SisuSeriyaDTO sisuSeriyaDTO) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SisuSeriyaDTO> drpdServiceNoList = new ArrayList<SisuSeriyaDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct sps_service_no " + "from public.nt_m_sisu_permit_hol_service_info "
					+ "WHERE sps_service_ref_no is not null and sps_service_ref_no !='' and sps_service_no is not null and sps_service_no !='' "
					+ "and ((sps_status='A' and is_cancellation is null) or (sps_status='P' and sps_renewal_status in ('P','R'))) and sps_operator_name='"
					+ sisuSeriyaDTO.getNameOfOperator() + "' order by sps_service_no desc ;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				SisuSeriyaDTO ss = new SisuSeriyaDTO();
				ss.setServiceNo(rs.getString("sps_service_no"));
				drpdServiceNoList.add(ss);

			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return drpdServiceNoList;
	}

	@Override
	public List<SisuSeriyaDTO> getRenewalServiceReferenceNoList(SisuSeriyaDTO sisuSeriyaDTO) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String WHERE_SQL = "";

		List<SisuSeriyaDTO> drpdServiceReferenceNoList = new ArrayList<SisuSeriyaDTO>();

		if (sisuSeriyaDTO.getNameOfOperator() != null && !sisuSeriyaDTO.getNameOfOperator().equals("")) {
			WHERE_SQL = WHERE_SQL + " AND sps_operator_name = '" + sisuSeriyaDTO.getNameOfOperator() + "' ";
		}
		if (sisuSeriyaDTO.getServiceNo() != null && !sisuSeriyaDTO.getServiceNo().equals("")) {
			WHERE_SQL = WHERE_SQL + " AND sps_service_no = '" + sisuSeriyaDTO.getServiceNo() + "' ";
		}

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct sps_service_ref_no " + "from public.nt_m_sisu_permit_hol_service_info "
					+ "WHERE sps_service_ref_no is not null and sps_service_ref_no !='' and sps_service_no is not null and sps_service_no !='' "
					+ "and ((sps_status='A' and is_cancellation is null) or (sps_status='P' and sps_renewal_status in ('P','R'))) "
					+ WHERE_SQL + " order by sps_service_ref_no desc ;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				SisuSeriyaDTO ss = new SisuSeriyaDTO();

				ss.setServiceRefNo(rs.getString("sps_service_ref_no"));
				drpdServiceReferenceNoList.add(ss);

			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return drpdServiceReferenceNoList;
	}

	@Override
	public List<SisuSeriyaDTO> getRenewalRefNoList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct sps_service_ref_no from public.nt_m_sisu_permit_hol_service_info \r\n"
					+ "WHERE sps_service_ref_no is not null and sps_service_ref_no !='' \r\n"
					+ "and ((sps_status='A' and is_cancellation is null) or (sps_status='P' and sps_renewal_status in ('P','R'))) \r\n"
					+ "order by sps_service_ref_no desc";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			SisuSeriyaDTO s;

			while (rs.next()) {
				s = new SisuSeriyaDTO();
				s.setServiceRefNo(rs.getString("sps_service_ref_no"));
				data.add(s);
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
	public boolean updateSchoolInformationByServiceRefNo(SisuSeriyaDTO sisuSeriyaDTO, String loginUser) {
		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		PreparedStatement stmt3 = null;
		PreparedStatement stmt4 = null;
		boolean isModelSave = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			// insert current record to history table
			String sql1 = "INSERT into public.nt_h_sisu_permit_hol_school_info  "
					+ " (SELECT * FROM public.nt_m_sisu_permit_hol_school_info WHERE ssc_service_ref_no = ?) ";

			stmt1 = con.prepareStatement(sql1);
			stmt1.setString(1, sisuSeriyaDTO.getServiceRefNo());
			stmt1.executeUpdate();

			// if status is "R" save the status as null
			if (sisuSeriyaDTO.getStatusCode().equalsIgnoreCase("R")) {
				// add current record in nt_m_sisu_permit_hol_service_info to history table
				String sql3 = "INSERT into public.nt_h_sisu_permit_hol_service_info  "
						+ " (SELECT * FROM public.nt_m_sisu_permit_hol_service_info WHERE sps_service_ref_no = ?) ";

				stmt3 = con.prepareStatement(sql3);
				stmt3.setString(1, sisuSeriyaDTO.getServiceRefNo());
				stmt3.executeUpdate();

				// set current record status as null
				String sql4 = "UPDATE public.nt_m_sisu_permit_hol_service_info SET sps_status='P', sps_is_checked=null, sps_checked_by=null, sps_checked_date=null, sps_is_recommended=null, sps_recommended_by=null, sps_recommended_date=null WHERE sps_service_ref_no= ? ;";

				stmt4 = con.prepareStatement(sql4);
				stmt4.setString(1, sisuSeriyaDTO.getServiceRefNo());
				stmt4.executeUpdate();
			}

			String sql = "UPDATE public.nt_m_sisu_permit_hol_school_info\r\n"
					+ "SET ssc_school_name=?, ssc_school_name_si=?, ssc_school_name_ta=?, \r\n"
					+ "ssc_school_add1=?, ssc_school_add1_si=?, ssc_school_add1_ta=?, \r\n"
					+ "ssc_school_add2=?, ssc_school_add2_si=?, ssc_school_add2_ta=?,\r\n"
					+ "ssc_school_city=?, ssc_school_city_si=?, ssc_school_city_ta=?, \r\n"
					+ "ssc_school_province_code=?, ssc_school_district_code=?, ssc_school_div_sec_code=?, \r\n"
					+ "ssc_school_tel_no=?, ssc_school_mobile=?, ssc_school_email=?, ssc_modified_by=?, ssc_modified_date=? \r\n"
					+ "WHERE ssc_service_ref_no=?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, sisuSeriyaDTO.getSchoolName());
			stmt.setString(2, sisuSeriyaDTO.getSchoolNameSin());
			stmt.setString(3, sisuSeriyaDTO.getSchoolNameTamil());
			stmt.setString(4, sisuSeriyaDTO.getSchoolAddressOne());
			stmt.setString(5, sisuSeriyaDTO.getSchoolAdrressOneSin());
			stmt.setString(6, sisuSeriyaDTO.getSchoolAddressOneTamil());
			stmt.setString(7, sisuSeriyaDTO.getSchoolAddressTwo());
			stmt.setString(8, sisuSeriyaDTO.getSchoolAddressTwoSin());
			stmt.setString(9, sisuSeriyaDTO.getSchoolAddressTwoTamil());
			stmt.setString(10, sisuSeriyaDTO.getSchoolCity());
			stmt.setString(11, sisuSeriyaDTO.getSchoolCitySin());
			stmt.setString(12, sisuSeriyaDTO.getSchoolCityTamil());
			stmt.setString(13, sisuSeriyaDTO.getSchoolProvinceCode());
			stmt.setString(14, sisuSeriyaDTO.getSchoolDistrictCode());
			stmt.setString(15, sisuSeriyaDTO.getSchoolDivisinSecCode());
			stmt.setString(16, sisuSeriyaDTO.getSchoolTelNo());
			stmt.setString(17, sisuSeriyaDTO.getSchoolMobileNo());
			stmt.setString(18, sisuSeriyaDTO.getSchoolEmailAdd());

			stmt.setString(19, loginUser);
			stmt.setTimestamp(20, timestamp);

			stmt.setString(21, sisuSeriyaDTO.getServiceRefNo());

			int i = stmt.executeUpdate();

			if (i > 0) {
				isModelSave = true;

				/* Task Update */
				long seqNo = getSeqNoFromTaskDet(con, sisuSeriyaDTO.getRequestNo(), sisuSeriyaDTO.getServiceRefNo(),
						sisuSeriyaDTO.getServiceNo());

				String sql2 = "INSERT INTO public.nt_h_subsidy_task_his \r\n"
						+ "(tsd_seq, tsd_request_no, tsd_reference_no, tsd_service_no, tsd_task_code, tsd_status, created_by, created_date)\r\n"
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ? ); \r\n" + ";";

				stmt2 = con.prepareStatement(sql2);

				stmt2.setLong(1, seqNo);
				stmt2.setString(2, sisuSeriyaDTO.getRequestNo());
				stmt2.setString(3, sisuSeriyaDTO.getServiceRefNo());
				stmt2.setString(4, sisuSeriyaDTO.getServiceNo());
				stmt2.setString(5, "SS010");
				stmt2.setString(6, "C");
				stmt2.setString(7, loginUser);
				stmt2.setTimestamp(8, timestamp);

				stmt2.executeUpdate();
			} else {
				isModelSave = false;
			}
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt1);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(stmt3);
			ConnectionManager.close(stmt4);
			ConnectionManager.close(con);
		}

		return isModelSave;
	}

	@Override
	public boolean insertRenewalServiceToHistory(SisuSeriyaDTO sisuSeriyaDTO, String loginUser) {
		Connection con = null;
		PreparedStatement pss = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

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

			String selectQuery = "SELECT sps_seq, sps_request_no, sps_service_ref_no, sps_request_start_date, sps_request_end_date, "
					+ "sps_nic_no, sps_perfered_lan, sps_operator_name, sps_operator_name_si, sps_operator_name_ta, sps_add1, "
					+ "sps_add1_si, sps_add1_ta, sps_add2, sps_add2_si, sps_add2_ta, sps_city, sps_city_si, sps_city_ta, "
					+ "sps_service_type_code, sps_permit_no, sps_bus_no, sps_origin, sps_destination, sps_via, sps_distance, "
					+ "sps_tips_per_day, sps_service_start_date, sps_service_end_date, sps_province, sps_district_code, sps_dev_sec_code,"
					+ " sps_telephone_no, sps_mobile_no, sps_status, sps_account_no, sps_bank_name_code, sps_branch_code, "
					+ "sps_created_by, sps_created_date, sps_modified_by, sps_modified_date, sps_tender_ref_no,"
					+ " sps_unserved_portion_km, sps_tot_length, sps_agreed_amount, sps_subsidy_type, sps_approve_reject_by, "
					+ "sps_reject_reason, sps_approve_reject_date, sps_service_no, sps_renewal_status, sps_renewal_new_expire_date,"
					+ " sps_is_issue_service_agreement, sps_is_issue_permit_sticker, sps_is_issue_logsheets, "
					+ "sps_service_agreement_issuedby, sps_permit_sticker_issuedby, sps_issue_logsheets_issuedby, "
					+ "sps_service_agreement_issued_date, sps_permit_sticker_issued_date, sps_issue_logsheets_issued_date, "
					+ "is_sltb, sps_is_issue_logsheets_edited, sps_is_issue_service_agreement_edited, sps_is_issue_permit_sticker_edited,"
					+ " sps_service_agreement_no, sps_is_checked, sps_checked_by, sps_checked_date, sps_is_recommended, "
					+ "sps_recommended_by, sps_recommended_date, is_cancellation, sps_remark FROM public.nt_m_sisu_permit_hol_service_info "
					+ "where sps_service_ref_no=?";

			pss = con.prepareStatement(selectQuery);
			pss.setString(1, sisuSeriyaDTO.getServiceRefNo());
			rs = pss.executeQuery();

			if (rs.next()) {

				long seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_h_subsidy_task_his");

				String historyQuery = "INSERT INTO public.nt_h_sisu_permit_hol_service_info "
						+ "(sps_seq, sps_request_no, sps_service_ref_no, sps_request_start_date, sps_request_end_date, sps_nic_no, "
						+ "sps_perfered_lan, sps_operator_name, sps_operator_name_si, sps_operator_name_ta, sps_add1, sps_add1_si, "
						+ "sps_add1_ta, sps_add2, sps_add2_si, sps_add2_ta, sps_city, sps_city_si, sps_city_ta, sps_service_type_code,"
						+ " sps_permit_no, sps_bus_no, sps_origin, sps_destination, sps_via, sps_distance, sps_tips_per_day, "
						+ "sps_service_start_date, sps_service_end_date, sps_province, sps_district_code, sps_dev_sec_code, "
						+ "sps_telephone_no, sps_mobile_no, sps_status, sps_account_no, sps_bank_name_code, sps_branch_code, "
						+ "sps_created_by, sps_created_date, sps_modified_by, sps_modified_date, sps_tender_ref_no, sps_unserved_portion_km,"
						+ " sps_tot_length, sps_agreed_amount, sps_subsidy_type, sps_approve_reject_by, sps_reject_reason, "
						+ "sps_approve_reject_date, sps_service_no, sps_renewal_status, sps_renewal_new_expire_date, "
						+ "sps_is_issue_service_agreement, sps_is_issue_permit_sticker, sps_is_issue_logsheets, sps_service_agreement_issuedby,"
						+ " sps_permit_sticker_issuedby, sps_issue_logsheets_issuedby, sps_service_agreement_issued_date,"
						+ " sps_permit_sticker_issued_date, sps_issue_logsheets_issued_date, is_sltb, sps_is_issue_logsheets_edited,"
						+ " sps_is_issue_service_agreement_edited, sps_is_issue_permit_sticker_edited, sps_service_agreement_no, "
						+ "sps_is_checked, sps_checked_by, sps_checked_date, sps_is_recommended, sps_recommended_by, sps_recommended_date, is_cancellation, sps_remark) "
						+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
						+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

				ps = con.prepareStatement(historyQuery);

				ps.setLong(1, seqNo);
				ps.setString(2, rs.getString("sps_request_no"));
				ps.setString(3, rs.getString("sps_service_ref_no"));
				ps.setString(4, rs.getString("sps_request_start_date"));
				ps.setString(5, rs.getString("sps_request_end_date"));
				ps.setString(6, rs.getString("sps_nic_no"));
				ps.setString(7, rs.getString("sps_perfered_lan"));
				ps.setString(8, rs.getString("sps_operator_name"));
				ps.setString(9, rs.getString("sps_operator_name_si"));
				ps.setString(10, rs.getString("sps_operator_name_ta"));
				ps.setString(11, rs.getString("sps_add1"));
				ps.setString(12, rs.getString("sps_add1_si"));
				ps.setString(13, rs.getString("sps_add1_ta"));
				ps.setString(14, rs.getString("sps_add2"));
				ps.setString(15, rs.getString("sps_add2_si"));
				ps.setString(16, rs.getString("sps_add2_ta"));
				ps.setString(17, rs.getString("sps_city"));
				ps.setString(18, rs.getString("sps_city_si"));
				ps.setString(19, rs.getString("sps_city_ta"));
				ps.setString(20, rs.getString("sps_service_type_code"));
				ps.setString(21, rs.getString("sps_permit_no"));
				ps.setString(22, rs.getString("sps_bus_no"));
				ps.setString(23, rs.getString("sps_origin"));
				ps.setString(24, rs.getString("sps_destination"));
				ps.setString(25, rs.getString("sps_via"));
				ps.setString(26, rs.getString("sps_distance"));
				ps.setString(27, rs.getString("sps_tips_per_day"));
				ps.setTimestamp(28, rs.getTimestamp("sps_service_start_date"));
				ps.setTimestamp(29, rs.getTimestamp("sps_service_end_date"));
				ps.setString(30, rs.getString("sps_province"));
				ps.setString(31, rs.getString("sps_district_code"));
				ps.setString(32, rs.getString("sps_dev_sec_code"));
				ps.setString(33, rs.getString("sps_telephone_no"));
				ps.setString(34, rs.getString("sps_mobile_no"));
				ps.setString(35, rs.getString("sps_status"));
				ps.setString(36, rs.getString("sps_account_no"));
				ps.setString(37, rs.getString("sps_bank_name_code"));
				ps.setString(38, rs.getString("sps_branch_code"));
				ps.setString(39, rs.getString("sps_created_by"));
				ps.setTimestamp(40, rs.getTimestamp("sps_created_date"));
				date = new java.util.Date();
				timestamp = new Timestamp(date.getTime());
				ps.setString(41, loginUser);
				ps.setTimestamp(42, timestamp);
				ps.setString(43, rs.getString("sps_tender_ref_no"));
				ps.setString(44, rs.getString("sps_unserved_portion_km"));
				ps.setDouble(45, rs.getDouble("sps_tot_length"));
				ps.setBigDecimal(46, rs.getBigDecimal("sps_agreed_amount"));
				ps.setString(47, rs.getString("sps_subsidy_type"));
				ps.setString(48, rs.getString("sps_approve_reject_by"));
				ps.setString(49, rs.getString("sps_reject_reason"));
				ps.setTimestamp(50, rs.getTimestamp("sps_approve_reject_date"));
				ps.setString(51, rs.getString("sps_service_no"));
				ps.setString(52, rs.getString("sps_renewal_status"));
				ps.setTimestamp(53, rs.getTimestamp("sps_renewal_new_expire_date"));
				ps.setString(54, rs.getString("sps_is_issue_service_agreement"));
				ps.setString(55, rs.getString("sps_is_issue_permit_sticker"));
				ps.setString(56, rs.getString("sps_is_issue_logsheets"));
				ps.setString(57, rs.getString("sps_service_agreement_issuedby"));
				ps.setString(58, rs.getString("sps_permit_sticker_issuedby"));
				ps.setString(59, rs.getString("sps_issue_logsheets_issuedby"));
				ps.setString(60, rs.getString("sps_service_agreement_issued_date"));
				ps.setString(61, rs.getString("sps_permit_sticker_issued_date"));
				ps.setString(62, rs.getString("sps_issue_logsheets_issued_date"));
				ps.setString(63, rs.getString("is_sltb"));
				ps.setString(64, rs.getString("sps_is_issue_logsheets_edited"));
				ps.setString(65, rs.getString("sps_is_issue_service_agreement_edited"));
				ps.setString(66, rs.getString("sps_is_issue_permit_sticker_edited"));
				ps.setString(67, rs.getString("sps_service_agreement_no"));
				ps.setString(68, rs.getString("sps_is_checked"));
				ps.setString(69, rs.getString("sps_checked_by"));
				ps.setTimestamp(70, rs.getTimestamp("sps_checked_date"));
				ps.setString(71, rs.getString("sps_is_recommended"));
				ps.setString(72, rs.getString("sps_recommended_by"));
				ps.setTimestamp(73, rs.getTimestamp("sps_recommended_date"));
				ps.setString(74, rs.getString("is_cancellation"));
				ps.setString(75, rs.getString("sps_remark"));

				int i = ps.executeUpdate();

				if (i > 0) {
					isModelSave = true;
				} else {
					isModelSave = false;
				}

			}

			con.commit();
		} catch (Exception e) {
			e.printStackTrace();

		} finally {

			if (!isModelSave) {
				sisuSeriyaDTO.setServiceRefNo("");
			}

			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isModelSave;
	}

	@Override
	public boolean updateSchoolInformationByServiceRefNo(String PreServiceRefNo, String newServiceRefNo) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isModelSave = false;

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_sisu_permit_hol_school_info\r\n" + "set ssc_service_ref_no=? \r\n"
					+ "where ssc_service_ref_no=?;";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, newServiceRefNo);
			stmt.setString(2, PreServiceRefNo);

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
	public boolean insertSchoolInformationToHistory(SisuSeriyaDTO sisuSeriyaDTO, String loginUser) {
		Connection con = null;
		PreparedStatement ps = null;

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

			long seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_h_sisu_permit_hol_school_info");

			String query = "INSERT INTO public.nt_h_sisu_permit_hol_school_info\r\n"
					+ "(ssc_seq, ssc_request_no, ssc_service_ref_no, \r\n"
					+ "ssc_school_name, ssc_school_name_si, ssc_school_name_ta, \r\n"
					+ "ssc_school_add1, ssc_school_add1_si, ssc_school_add1_ta, \r\n"
					+ "ssc_school_add2, ssc_school_add2_si, ssc_school_add2_ta, \r\n"
					+ "ssc_school_city, ssc_school_city_si, ssc_school_city_ta, \r\n"
					+ "ssc_school_province_code, ssc_school_district_code, ssc_school_div_sec_code, \r\n"
					+ "ssc_school_tel_no, ssc_school_mobile, ssc_school_email, \r\n"
					+ "ssc_status, ssc_created_by, ssc_created_date, ssc_modified_by, ssc_modified_date)\r\n"
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?);";
			ps = con.prepareStatement(query);

			ps.setLong(1, seqNo);
			ps.setString(2, sisuSeriyaDTO.getRequestNo());
			ps.setString(3, sisuSeriyaDTO.getServiceRefNo());

			ps.setString(4, sisuSeriyaDTO.getSchoolName());
			ps.setString(5, sisuSeriyaDTO.getSchoolNameSin());
			ps.setString(6, sisuSeriyaDTO.getSchoolNameTamil());

			ps.setString(7, sisuSeriyaDTO.getSchoolAddressOne());
			ps.setString(8, sisuSeriyaDTO.getSchoolAddressOneTamil());
			ps.setString(9, sisuSeriyaDTO.getSchoolAdrressOneSin());

			ps.setString(10, sisuSeriyaDTO.getSchoolAddressTwo());
			ps.setString(11, sisuSeriyaDTO.getSchoolAddressTwoSin());
			ps.setString(12, sisuSeriyaDTO.getSchoolAddressTwoTamil());

			ps.setString(13, sisuSeriyaDTO.getSchoolCity());
			ps.setString(14, sisuSeriyaDTO.getSchoolCitySin());
			ps.setString(15, sisuSeriyaDTO.getSchoolCityTamil());

			ps.setString(16, sisuSeriyaDTO.getSchoolProvinceCode());
			ps.setString(17, sisuSeriyaDTO.getSchoolDistrictCode());
			ps.setString(18, sisuSeriyaDTO.getSchoolDivisinSecCode());

			ps.setString(19, sisuSeriyaDTO.getSchoolTelNo());
			ps.setString(20, sisuSeriyaDTO.getSchoolMobileNo());
			ps.setString(21, sisuSeriyaDTO.getSchoolEmailAdd());

			ps.setString(22, sisuSeriyaDTO.getStatus());

			ps.setString(23, loginUser);
			ps.setTimestamp(24, timestamp);
			ps.setString(25, loginUser);
			ps.setTimestamp(26, timestamp);

			int i = ps.executeUpdate();

			if (i > 0) {
				isModelSave = true;
			} else {
				isModelSave = false;
			}

			con.commit();
		} catch (Exception e) {
			e.printStackTrace();

		} finally {

			if (!isModelSave) {
				sisuSeriyaDTO.setServiceRefNo("");
			}

			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return isModelSave;
	}

	@Override
	public boolean checkSchoolInfo(SisuSeriyaDTO sisuSeriyaDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isFound = false;

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM public.nt_m_sisu_permit_hol_school_info WHERE  ssc_service_ref_no=?";

			ps = con.prepareStatement(query);
			ps.setString(1, sisuSeriyaDTO.getServiceRefNo());

			rs = ps.executeQuery();

			if (rs.next() == true) {
				isFound = true;
			} else {
				isFound = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isFound;
	}

	@Override
	public boolean saveSchoolInformationToHistory(SisuSeriyaDTO sisuSeriyaDTO, String refNo, String loginUser) {

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement pss = null;
		ResultSet rs = null;

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

			String selectQuery = "SELECT ssc_seq, ssc_request_no, ssc_service_ref_no, ssc_school_name,"
					+ " ssc_school_name_si, ssc_school_name_ta, ssc_school_add1, ssc_school_add1_si, "
					+ "ssc_school_add1_ta, ssc_school_add2, ssc_school_add2_si, ssc_school_add2_ta, "
					+ "ssc_school_city, ssc_school_city_si, ssc_school_city_ta, ssc_school_province_code, "
					+ "ssc_school_district_code, ssc_school_div_sec_code, ssc_school_tel_no, ssc_school_mobile, "
					+ "ssc_school_email, ssc_status, ssc_created_by, ssc_created_date, ssc_modified_by, "
					+ "ssc_modified_date FROM public.nt_m_sisu_permit_hol_school_info " + "where ssc_service_ref_no=?";

			ps = con.prepareStatement(selectQuery);

			ps.setString(1, refNo);
			rs = ps.executeQuery();

			if (rs.next()) {

				long seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_h_sisu_permit_hol_school_info");

				String query = "INSERT INTO public.nt_h_sisu_permit_hol_school_info\r\n"
						+ "(ssc_seq, ssc_request_no, ssc_service_ref_no, \r\n"
						+ "ssc_school_name, ssc_school_name_si, ssc_school_name_ta, \r\n"
						+ "ssc_school_add1, ssc_school_add1_si, ssc_school_add1_ta, \r\n"
						+ "ssc_school_add2, ssc_school_add2_si, ssc_school_add2_ta, \r\n"
						+ "ssc_school_city, ssc_school_city_si, ssc_school_city_ta, \r\n"
						+ "ssc_school_province_code, ssc_school_district_code, ssc_school_div_sec_code, \r\n"
						+ "ssc_school_tel_no, ssc_school_mobile, ssc_school_email, \r\n"
						+ "ssc_status, ssc_created_by, ssc_created_date, ssc_modified_by, ssc_modified_date)\r\n"
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?);";

				pss = con.prepareStatement(query);

				pss.setLong(1, seqNo);
				pss.setString(2, rs.getString("ssc_request_no"));
				pss.setString(3, rs.getString("ssc_service_ref_no"));

				pss.setString(4, rs.getString("ssc_school_name"));
				pss.setString(5, rs.getString("ssc_school_name_si"));
				pss.setString(6, rs.getString("ssc_school_name_ta"));

				pss.setString(7, rs.getString("ssc_school_add1"));
				pss.setString(8, rs.getString("ssc_school_add1_si"));
				pss.setString(9, rs.getString("ssc_school_add1_ta"));

				pss.setString(10, rs.getString("ssc_school_add2"));
				pss.setString(11, rs.getString("ssc_school_add2_si"));
				pss.setString(12, rs.getString("ssc_school_add2_ta"));

				pss.setString(13, rs.getString("ssc_school_city"));
				pss.setString(14, rs.getString("ssc_school_city_si"));
				pss.setString(15, rs.getString("ssc_school_city_ta"));

				pss.setString(16, rs.getString("ssc_school_province_code"));
				pss.setString(17, rs.getString("ssc_school_district_code"));
				pss.setString(18, rs.getString("ssc_school_div_sec_code"));

				pss.setString(19, rs.getString("ssc_school_tel_no"));
				pss.setString(20, rs.getString("ssc_school_mobile"));
				pss.setString(21, rs.getString("ssc_school_email"));

				pss.setString(22, rs.getString("ssc_status"));

				pss.setString(23, rs.getString("ssc_created_by"));
				pss.setTimestamp(24, rs.getTimestamp("ssc_created_date"));
				pss.setString(25, loginUser);
				pss.setTimestamp(26, timestamp);

				int i = pss.executeUpdate();

				if (i > 0) {
					isModelSave = true;
				} else {
					isModelSave = false;
				}

			} else {
				isModelSave = false;
			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {

			if (!isModelSave) {
				sisuSeriyaDTO.setServiceRefNo("");
			}

			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return isModelSave;
	}

	@Override
	public List<SisuSeriyaDTO> getServiceNoList1() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> data1 = new ArrayList<SisuSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct sps_service_no , sps_renewal_status,sps_status from public.nt_m_sisu_permit_hol_service_info \r\n"
					+ "WHERE sps_service_no is not null and sps_service_no !='' and  sps_status='A' and sps_renewal_status not in ('I')   order by sps_service_no    ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			SisuSeriyaDTO s1;

			while (rs.next()) {
				s1 = new SisuSeriyaDTO();
				s1.setServiceNo(rs.getString("sps_service_no"));
				data1.add(s1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return data1;

	}

	@Override
	public List<SisuSeriyaDTO> drpdViaList(SisuSeriyaDTO sisuSeriyaDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> viaList = new ArrayList<SisuSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select srr_via\r\n" + "from public.nt_m_sisu_requestor_route_info\r\n"
					+ "where srr_request_no=? and srr_origin=? and srr_destination=? and srr_via is not null";

			ps = con.prepareStatement(query);

			ps.setString(1, sisuSeriyaDTO.getRequestNo());
			ps.setString(2, sisuSeriyaDTO.getOriginCode());
			ps.setString(3, sisuSeriyaDTO.getDestinationCode());

			rs = ps.executeQuery();

			SisuSeriyaDTO s1;

			while (rs.next()) {
				s1 = new SisuSeriyaDTO();
				s1.setVia(rs.getString("srr_via"));
				viaList.add(s1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return viaList;
	}

	@Override
	public List<SisuSeriyaDTO> drpdDestinationListByOrigin(SisuSeriyaDTO sisuSeriyaDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> destinationList = new ArrayList<SisuSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select srr_destination\r\n" + "from public.nt_m_sisu_requestor_route_info\r\n"
					+ "where srr_request_no=? and srr_origin=? and srr_destination is not null";

			ps = con.prepareStatement(query);

			ps.setString(1, sisuSeriyaDTO.getRequestNo());
			ps.setString(2, sisuSeriyaDTO.getOriginCode());

			rs = ps.executeQuery();

			SisuSeriyaDTO s1;

			while (rs.next()) {
				s1 = new SisuSeriyaDTO();
				s1.setDestinationCodeSin(rs.getString("srr_destination"));
				s1.setDestinationCode(rs.getString("srr_destination"));
				destinationList.add(s1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return destinationList;
	}

	@Override
	public List<SisuSeriyaDTO> drpdOriginListByDestination(SisuSeriyaDTO sisuSeriyaDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> destinationList = new ArrayList<SisuSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select srr_origin\r\n" + "from public.nt_m_sisu_requestor_route_info\r\n"
					+ "where srr_request_no=? and srr_destination=? and srr_origin is not null";

			ps = con.prepareStatement(query);

			ps.setString(1, sisuSeriyaDTO.getRequestNo());
			ps.setString(2, sisuSeriyaDTO.getDestinationCode());

			rs = ps.executeQuery();

			SisuSeriyaDTO s1;

			while (rs.next()) {
				s1 = new SisuSeriyaDTO();
				s1.setOriginCodeSin(rs.getString("srr_origin"));
				s1.setOriginCode(rs.getString("srr_origin"));
				destinationList.add(s1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return destinationList;
	}

	@Override
	public boolean updateServiceStartEndDateInApproval(String serviceReferenceNo, Date startDate, Date endDate) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isModelSave = false;

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_sisu_permit_hol_service_info\r\n"
					+ "set sps_service_start_date=?,sps_service_end_date=?\r\n" + "where sps_service_ref_no=?;";

			stmt = con.prepareStatement(sql);

			Timestamp serviceStartDate = null;
			if (startDate != null) {
				serviceStartDate = new Timestamp(startDate.getTime());
			}

			Timestamp serviceEndtDate = null;
			if (endDate != null) {
				serviceEndtDate = new Timestamp(endDate.getTime());
			}

			stmt.setTimestamp(1, serviceStartDate);
			stmt.setTimestamp(2, serviceEndtDate);
			stmt.setString(3, serviceReferenceNo);

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
	public List<SisuSeriyaDTO> getFilterdVoucherPendingServiceRefNoList(String reqNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct sps_service_ref_no from public.nt_m_sisu_permit_hol_service_info "
					+ "inner join public.nt_t_subsidy_task_det on nt_t_subsidy_task_det.tsd_service_no=nt_m_sisu_permit_hol_service_info.sps_service_no "
					+ "where ((nt_t_subsidy_task_det.tsd_task_code='SS006' and nt_t_subsidy_task_det.tsd_status='C') or ( "
					+ "nt_t_subsidy_task_det.tsd_task_code='SM001' and nt_t_subsidy_task_det.tsd_status='O')) and is_sltb !='Y' and sps_request_no=?";
			ps = con.prepareStatement(query);
			ps.setString(1, reqNo);
			rs = ps.executeQuery();

			SisuSeriyaDTO s;

			while (rs.next()) {
				s = new SisuSeriyaDTO();
				s.setServiceRefNo(rs.getString("sps_service_ref_no"));
				data.add(s);
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
	public SisuSeriyaDTO getVoucherPendingServiceNo(String serviceRefNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		SisuSeriyaDTO data = new SisuSeriyaDTO();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct sps_service_no from public.nt_m_sisu_permit_hol_service_info "
					+ "inner join public.nt_t_subsidy_task_det on nt_t_subsidy_task_det.tsd_service_no=nt_m_sisu_permit_hol_service_info.sps_service_no "
					+ "where ((nt_t_subsidy_task_det.tsd_task_code='SS006' and nt_t_subsidy_task_det.tsd_status='C') or ( "
					+ "nt_t_subsidy_task_det.tsd_task_code='SM001' and nt_t_subsidy_task_det.tsd_status='O')) and is_sltb !='Y' and sps_service_ref_no =?";

			ps = con.prepareStatement(query);
			ps.setString(1, serviceRefNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				data.setServiceNo(rs.getString("sps_service_no"));
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
	public List<SisuSeriyaDTO> getApprovedRequestNoForEditMode() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct sps_request_no from public.nt_m_sisu_permit_hol_service_info WHERE sps_request_no is not null and sps_request_no !='' and sps_status='A' and  (sps_service_agreement_issuedby is not NULL and sps_permit_sticker_issuedby is not NULL and sps_issue_logsheets_issuedby is not NULL ) order by sps_request_no desc;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			SisuSeriyaDTO s;

			while (rs.next()) {
				s = new SisuSeriyaDTO();
				s.setRequestNo(rs.getString("sps_request_no"));
				data.add(s);
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
	public List<SisuSeriyaDTO> getApprovedServiceRefNoListForEditMode() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct sps_service_ref_no from public.nt_m_sisu_permit_hol_service_info WHERE sps_service_ref_no is not null and sps_service_ref_no !='' and sps_status='A' and sps_renewal_status not in ('I')   and  (sps_service_agreement_issuedby is not NULL and sps_permit_sticker_issuedby IS not NULL and  sps_issue_logsheets_issuedby is not null) order by sps_service_ref_no desc ;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			SisuSeriyaDTO s;

			while (rs.next()) {
				s = new SisuSeriyaDTO();
				s.setServiceRefNo(rs.getString("sps_service_ref_no"));
				data.add(s);
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

	/* method added: dhananjika.d (27/06/2024) */
	@Override
	public List<SisuSeriyaDTO> getApprovedServiceRefNo() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct sps_service_ref_no from public.nt_m_sisu_permit_hol_service_info WHERE sps_service_ref_no is not null and sps_service_ref_no !='' and sps_status='A' and sps_renewal_status not in ('I','P')   and  (sps_service_agreement_issuedby is not NULL and sps_permit_sticker_issuedby IS not NULL and  sps_issue_logsheets_issuedby is not null) order by sps_service_ref_no desc ;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			SisuSeriyaDTO s;

			while (rs.next()) {
				s = new SisuSeriyaDTO();
				s.setServiceRefNo(rs.getString("sps_service_ref_no"));
				data.add(s);
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
	public List<SisuSeriyaDTO> getApprovedServiceRefNoListForEditMode(SisuSeriyaDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		String WHERE_SQL = "";

		if (dto.getNameOfOperator() != null && !dto.getNameOfOperator().equals("")) {
			WHERE_SQL = WHERE_SQL + " AND sps_operator_name = '" + dto.getNameOfOperator() + "' ";
		}
		if (dto.getBusRegNo() != null && !dto.getBusRegNo().equals("")) {
			WHERE_SQL = WHERE_SQL + " AND sps_bus_no = '" + dto.getBusRegNo() + "' ";
		}
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct sps_service_ref_no from public.nt_m_sisu_permit_hol_service_info WHERE sps_service_ref_no is not null "
					+ " and sps_service_ref_no !='' and sps_status='A' and sps_renewal_status not in ('I')   and  "
					+ " (sps_service_agreement_issuedby is not NULL and sps_permit_sticker_issuedby IS not NULL and  sps_issue_logsheets_issuedby is not null) "
					+ WHERE_SQL + " order by sps_service_ref_no  ;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			SisuSeriyaDTO s;

			while (rs.next()) {
				s = new SisuSeriyaDTO();
				s.setServiceRefNo(rs.getString("sps_service_ref_no"));
				data.add(s);
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
	public List<SisuSeriyaDTO> getApprovedServiceNoListForEditMode() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct sps_service_no  from public.nt_m_sisu_permit_hol_service_info WHERE sps_service_no is not null and sps_service_no !=''  and  sps_status='A'   and sps_renewal_status not in ('I') and  (sps_service_agreement_issuedby is not NULL and sps_permit_sticker_issuedby is not NULL and sps_issue_logsheets_issuedby is not null) order by sps_service_no desc;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			SisuSeriyaDTO s;

			while (rs.next()) {
				s = new SisuSeriyaDTO();
				s.setServiceNo(rs.getString("sps_service_no"));
				data.add(s);
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
	public List<SisuSeriyaDTO> getOperatorDepoNameListForIssueLogSheets(String serviceTypeCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		try {
			con = ConnectionManager.getConnection();
			if (serviceTypeCode != null && !serviceTypeCode.trim().equals("")) {

				if (serviceTypeCode.equals("S01")) {
					String query = "select distinct sps_operator_name  from public.nt_m_sisu_permit_hol_service_info WHERE sps_operator_name is not null and sps_operator_name !=''  and  sps_status='A'   and sps_renewal_status not in ('I') and  (sps_service_agreement_issuedby is not NULL and sps_permit_sticker_issuedby is not NULL and sps_issue_logsheets_issuedby is not null) order by sps_operator_name ;";

					ps = con.prepareStatement(query);
					rs = ps.executeQuery();

					SisuSeriyaDTO s;

					while (rs.next()) {
						s = new SisuSeriyaDTO();
						s.setNameOfOperator(rs.getString("sps_operator_name"));
						data.add(s);
					}
				} else if (serviceTypeCode.equals("S02")) {
					String query = "select distinct gps_operator_name from public.nt_m_gami_permit_hol_service_info WHERE gps_operator_name is not null and gps_operator_name !='' and gps_status='A' and (gps_renewal_status not in ('I') or gps_renewal_status is null)   and  (gps_service_agreement_issuedby is not NULL and gps_permit_sticker_issuedby IS not NULL and  gps_issue_logsheets_issuedby is not null) order by gps_operator_name  ;";

					ps = con.prepareStatement(query);
					rs = ps.executeQuery();

					SisuSeriyaDTO s;

					while (rs.next()) {
						s = new SisuSeriyaDTO();
						s.setNameOfOperator(rs.getString("gps_operator_name"));
						data.add(s);
					}
				} else if (serviceTypeCode.equals("S03")) {
					String query = "select distinct nri_operator_name from public.nt_m_nri_requester_info WHERE nri_operator_name is not null and nri_operator_name !='' and nri_status='A' and  (nri_service_agreement_issuedby is not NULL and nri_permit_sticker_issuedby IS not NULL and  nri_issue_logsheets_issuedby is not null) order by nri_operator_name  ;";

					ps = con.prepareStatement(query);
					rs = ps.executeQuery();

					SisuSeriyaDTO s;

					while (rs.next()) {
						s = new SisuSeriyaDTO();
						s.setNameOfOperator(rs.getString("nri_operator_name"));
						data.add(s);
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
		return data;
	}

	@Override
	public List<SisuSeriyaDTO> getBusNoListForIssueLogSheets(String serviceTypeCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		try {
			con = ConnectionManager.getConnection();
			if (serviceTypeCode != null && !serviceTypeCode.trim().equals("")) {

				if (serviceTypeCode.equals("S01")) {
					String query = "select distinct sps_bus_no  from public.nt_m_sisu_permit_hol_service_info WHERE sps_bus_no is not null and sps_bus_no !=''  and  sps_status='A'   and sps_renewal_status not in ('I') and  (sps_service_agreement_issuedby is not NULL and sps_permit_sticker_issuedby is not NULL and sps_issue_logsheets_issuedby is not null) order by sps_bus_no ;";

					ps = con.prepareStatement(query);
					rs = ps.executeQuery();

					SisuSeriyaDTO s;

					while (rs.next()) {
						s = new SisuSeriyaDTO();
						s.setBusRegNo(rs.getString("sps_bus_no"));
						data.add(s);
					}
				} else if (serviceTypeCode.equals("S02")) {
					String query = "select distinct gps_bus_no from public.nt_m_gami_permit_hol_service_info WHERE gps_bus_no is not null and gps_bus_no !='' and gps_status='A' and (gps_renewal_status not in ('I') or gps_renewal_status is null)   and  (gps_service_agreement_issuedby is not NULL and gps_permit_sticker_issuedby IS not NULL and  gps_issue_logsheets_issuedby is not null) order by gps_bus_no  ;";

					ps = con.prepareStatement(query);
					rs = ps.executeQuery();

					SisuSeriyaDTO s;

					while (rs.next()) {
						s = new SisuSeriyaDTO();
						s.setBusRegNo(rs.getString("gps_bus_no"));
						data.add(s);
					}
				} else if (serviceTypeCode.equals("S03")) {
					String query = "select distinct nri_bus_no from public.nt_m_nri_requester_info WHERE nri_bus_no is not null and nri_bus_no !='' and nri_status='A' and  (nri_service_agreement_issuedby is not NULL and nri_permit_sticker_issuedby IS not NULL and  nri_issue_logsheets_issuedby is not null) order by nri_bus_no  ;";

					ps = con.prepareStatement(query);
					rs = ps.executeQuery();

					SisuSeriyaDTO s;

					while (rs.next()) {
						s = new SisuSeriyaDTO();
						s.setBusRegNo(rs.getString("nri_bus_no"));
						data.add(s);
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
		return data;
	}

	@Override
	public List<SisuSeriyaDTO> getSisuSeriyaToIssueBySearchForEditMode(SisuSeriyaDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> sisuSeriyaList = new ArrayList<SisuSeriyaDTO>();
		String WHERE_SQL = "";
		boolean whereadded = false;

		if (dto.getRequestNo() != null && !dto.getRequestNo().equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE sps_request_no = " + "'" + dto.getRequestNo()
					+ "' and sps_status='A'  and  (sps_service_agreement_issuedby is not NULL and sps_permit_sticker_issuedby is not NULL and sps_issue_logsheets_issuedby is not null) and sps_service_ref_no is not null and sps_service_ref_no != ''";
			whereadded = true;
		}
		if (dto.getServiceRefNo() != null && !dto.getServiceRefNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND sps_service_ref_no = " + "'" + dto.getServiceRefNo() + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE sps_service_ref_no = " + "'" + dto.getServiceRefNo()
						+ "' and sps_status='A'  and  (sps_service_agreement_issuedby is not NULL and sps_permit_sticker_issuedby is not NULL and sps_issue_logsheets_issuedby is not null) and sps_service_ref_no is not null and sps_service_ref_no != ''";
				whereadded = true;
			}
		}

		if (dto.getRequestStartDate() != null && dto.getRequestEndDate() != null) {
			Timestamp startDate = new Timestamp(dto.getRequestStartDate().getTime());
			Timestamp endDate = new Timestamp(dto.getRequestEndDate().getTime());
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + "and nt_m_sisu_requestor_info.sri_request_date >= " + "' " + startDate
						+ "' and nt_m_sisu_requestor_info.sri_request_date <= " + "'" + endDate + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + "WHERE nt_m_sisu_requestor_info.sri_request_date >= " + "' " + startDate
						+ "' and nt_m_sisu_requestor_info.sri_request_date <= " + "'" + endDate
						+ "' and sps_status='A'  and  (sps_service_agreement_issuedby is not NULL and sps_permit_sticker_issuedby is not NULL and sps_issue_logsheets_issuedby is not null) and sps_service_ref_no is not null and sps_service_ref_no != ''";
				whereadded = true;
			}
		} else if (dto.getRequestEndDate() != null) {

			Timestamp endDate = new Timestamp(dto.getRequestEndDate().getTime());
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " and nt_m_sisu_requestor_info.sri_request_date <= " + "'" + endDate + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE  nt_m_sisu_requestor_info.sri_request_date <= " + "'" + endDate
						+ "' and sps_status='A'  and  (sps_service_agreement_issuedby is not NULL and sps_permit_sticker_issuedby is not NULL and sps_issue_logsheets_issuedby is not null) and sps_service_ref_no is not null and sps_service_ref_no != ''";
				whereadded = true;
			}
		} else if (dto.getRequestStartDate() != null) {
			Timestamp startDate = new Timestamp(dto.getRequestStartDate().getTime());

			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " and nt_m_sisu_requestor_info.sri_request_date >= " + "'" + startDate + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE nt_m_sisu_requestor_info.sri_request_date >= " + "'" + startDate
						+ "' and sps_status='A'  and  (sps_service_agreement_issuedby is not NULL and sps_permit_sticker_issuedby is not NULL and sps_issue_logsheets_issuedby is not null) and sps_service_ref_no is not null and sps_service_ref_no != ''";
				whereadded = true;
			}
		}

		if (dto.getServiceNo() != null && !dto.getServiceNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND sps_service_no = " + "'" + dto.getServiceNo() + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE sps_service_no = " + "'" + dto.getServiceNo()
						+ "' and sps_status='A'  and  (sps_service_agreement_issuedby is not NULL and sps_permit_sticker_issuedby is not NULL and sps_issue_logsheets_issuedby is not null) and sps_service_ref_no is not null and sps_service_ref_no != ''";
				whereadded = true;
			}
		}

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select distinct sps_request_no, sps_service_ref_no,sps_service_no,"
					+ " to_char(sps_service_end_date,'dd/MM/yyyy') as sps_service_end_date ,sps_status, sps_operator_name,"
					+ " sps_is_issue_service_agreement," + " sps_is_issue_permit_sticker," + " sps_is_issue_logsheets,"
					+ " sps_service_agreement_issuedby," + " sps_permit_sticker_issuedby,"
					+ " sps_issue_logsheets_issuedby," + " sps_service_agreement_issued_date,"
					+ " sps_permit_sticker_issued_date," + " sps_issue_logsheets_issued_date"
					+ " from public.nt_m_sisu_permit_hol_service_info "
					+ "inner join public.nt_m_sisu_requestor_info on nt_m_sisu_requestor_info.sri_request_no = nt_m_sisu_permit_hol_service_info.sps_request_no "
					+ WHERE_SQL;

			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();
			SisuSeriyaDTO e;

			while (rs.next()) {
				e = new SisuSeriyaDTO();
				e.setRequestNo(rs.getString("sps_request_no"));
				e.setServiceRefNo(rs.getString("sps_service_ref_no"));
				e.setServiceNo(rs.getString("sps_service_no"));
				e.setNameOfOperator(rs.getString("sps_operator_name"));

				e.setServiceEndDateVal(rs.getString("sps_service_end_date"));

				String approveStatusCode = rs.getString("sps_status");
				String approveStatus = "";

				if (approveStatusCode != null) {
					if (approveStatusCode.equals("A")) {
						approveStatus = "APPROVED";
					} else if (approveStatusCode.equals("R")) {
						approveStatus = "REJECTED";
					} else if (approveStatusCode.equals("P")) {
						approveStatus = "PENDING";
					}
				}

				e.setStatus(approveStatus);

				e.setServiceAgreementIssuedBy(rs.getString("sps_service_agreement_issuedby"));
				e.setPermitStickerIssuedBy(rs.getString("sps_permit_sticker_issuedby"));
				e.setLogSheetsIssuedBy(rs.getString("sps_issue_logsheets_issuedby"));

				if (rs.getString("sps_is_issue_service_agreement") != null
						&& rs.getString("sps_is_issue_service_agreement").equalsIgnoreCase("Y")) {
				}

				if (rs.getString("sps_is_issue_permit_sticker") != null
						&& rs.getString("sps_is_issue_permit_sticker").equalsIgnoreCase("Y")) {
				}

				if (rs.getString("sps_is_issue_logsheets") != null
						&& rs.getString("sps_is_issue_logsheets").equalsIgnoreCase("Y")) {
				}

				String sps_service_agreement_issued_dateString = rs.getString("sps_service_agreement_issued_date");
				if (sps_service_agreement_issued_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setServiceAgreementIssuedDate = originalFormat.parse(sps_service_agreement_issued_dateString);
					e.setServiceAgreementIssuedDate(setServiceAgreementIssuedDate);
				}

				String sps_permit_sticker_issued_dateString = rs.getString("sps_permit_sticker_issued_date");
				if (sps_permit_sticker_issued_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setPermitStickerIssuedDate = originalFormat.parse(sps_permit_sticker_issued_dateString);
					e.setPermitStickerIssuedDate(setPermitStickerIssuedDate);
				}

				String sps_issue_logsheets_issued_dateString = rs.getString("sps_issue_logsheets_issued_date");
				if (sps_issue_logsheets_issued_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setLogSheetsIssuedDate = originalFormat.parse(sps_issue_logsheets_issued_dateString);
					e.setLogSheetsIssuedDate(setLogSheetsIssuedDate);
				}

				sisuSeriyaList.add(e);
			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return sisuSeriyaList;
	}

	@Override
	public List<SisuSeriyaDTO> getSisuSeriyaToIssueForEditMode() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> sisuSeriyaList = new ArrayList<SisuSeriyaDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select distinct sps_request_no, sps_service_ref_no,sps_service_no,"
					+ " to_char(sps_service_end_date,'dd/MM/yyyy') as sps_service_end_date ,sps_status, sps_operator_name,sps_is_issue_service_agreement,"
					+ " sps_is_issue_permit_sticker," + " sps_is_issue_logsheets," + " sps_service_agreement_issuedby,"
					+ " sps_permit_sticker_issuedby," + " sps_issue_logsheets_issuedby,"
					+ " sps_service_agreement_issued_date," + " sps_permit_sticker_issued_date,"
					+ " sps_issue_logsheets_issued_date from public.nt_m_sisu_permit_hol_service_info where sps_status='A' and  (sps_service_agreement_issuedby is not NULL and sps_permit_sticker_issuedby is not NULL and sps_issue_logsheets_issuedby is not null)";

			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();
			SisuSeriyaDTO e;

			while (rs.next()) {
				e = new SisuSeriyaDTO();
				e.setRequestNo(rs.getString("sps_request_no"));
				e.setServiceRefNo(rs.getString("sps_service_ref_no"));

				e.setServiceNo(rs.getString("sps_service_no"));

				e.setNameOfOperator(rs.getString("sps_operator_name"));
				e.setServiceEndDateVal(rs.getString("sps_service_end_date"));
				String approveStatusCode = rs.getString("sps_status");
				String approveStatus = "";

				if (approveStatusCode != null) {
					if (approveStatusCode.equals("A")) {
						approveStatus = "APPROVED";
					} else if (approveStatusCode.equals("R")) {
						approveStatus = "REJECTED";
					} else if (approveStatusCode.equals("P")) {
						approveStatus = "PENDING";
					}
				}

				e.setStatus(approveStatus);

				e.setServiceAgreementIssuedBy(rs.getString("sps_service_agreement_issuedby"));
				e.setPermitStickerIssuedBy(rs.getString("sps_permit_sticker_issuedby"));
				e.setLogSheetsIssuedBy(rs.getString("sps_issue_logsheets_issuedby"));

				if (rs.getString("sps_is_issue_service_agreement") != null
						&& rs.getString("sps_is_issue_service_agreement").equalsIgnoreCase("Y")) {
				}

				if (rs.getString("sps_is_issue_permit_sticker") != null
						&& rs.getString("sps_is_issue_permit_sticker").equalsIgnoreCase("Y")) {
				}

				if (rs.getString("sps_is_issue_logsheets") != null
						&& rs.getString("sps_is_issue_logsheets").equalsIgnoreCase("Y")) {
				}

				String sps_service_agreement_issued_dateString = rs.getString("sps_service_agreement_issued_date");
				if (sps_service_agreement_issued_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setServiceAgreementIssuedDate = originalFormat.parse(sps_service_agreement_issued_dateString);
					e.setServiceAgreementIssuedDate(setServiceAgreementIssuedDate);
				}

				String sps_permit_sticker_issued_dateString = rs.getString("sps_permit_sticker_issued_date");
				if (sps_permit_sticker_issued_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setPermitStickerIssuedDate = originalFormat.parse(sps_permit_sticker_issued_dateString);
					e.setPermitStickerIssuedDate(setPermitStickerIssuedDate);
				}

				String sps_issue_logsheets_issued_dateString = rs.getString("sps_issue_logsheets_issued_date");
				if (sps_issue_logsheets_issued_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setLogSheetsIssuedDate = originalFormat.parse(sps_issue_logsheets_issued_dateString);
					e.setLogSheetsIssuedDate(setLogSheetsIssuedDate);
				}

				sisuSeriyaList.add(e);
			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return sisuSeriyaList;
	}

	@Override
	public void updateIssueServiceAgreementPermitStickerLogSheetsForEditMode(SisuSeriyaDTO ssDTO) {
		java.util.Date date = new java.util.Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_sisu_permit_hol_service_info SET ";

			switch (ssDTO.getIssueType()) {
			case 1:
				String getServiceAgreementIssuedDate = (dateFormat.format(ssDTO.getServiceAgreementIssuedDate()));
				sql = sql + "sps_is_issue_service_agreement_edited='Y',sps_service_agreement_issuedby='"
						+ ssDTO.getCurrentUser() + "',sps_service_agreement_issued_date='"
						+ getServiceAgreementIssuedDate + "',";
				break;
			case 2:
				String getPermitStickerIssuedDate = (dateFormat.format(ssDTO.getPermitStickerIssuedDate()));
				sql = sql + "sps_is_issue_permit_sticker_edited='Y',sps_permit_sticker_issuedby='"
						+ ssDTO.getCurrentUser() + "',sps_permit_sticker_issued_date='" + getPermitStickerIssuedDate
						+ "',";
				break;
			case 3:
				String getLogSheetsIssuedDate = (dateFormat.format(ssDTO.getLogSheetsIssuedDate()));
				sql = sql + "sps_is_issue_logsheets_edited='Y',sps_issue_logsheets_issuedby='" + ssDTO.getCurrentUser()
						+ "',sps_issue_logsheets_issued_date='" + getLogSheetsIssuedDate + "',";
				break;
			}

			sql = sql + " sps_modified_by=?,sps_modified_date=? WHERE sps_service_ref_no=?";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, ssDTO.getCurrentUser());

			stmt.setTimestamp(2, timestamp);

			stmt.setString(3, ssDTO.getServiceRefNo());

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

	@Override
	public void insetOldRecordIntoHis(SisuSeriyaDTO selectDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		SisuSeriyaDTO sisuSeriyaDTO = null;

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_h_sisu_permit_hol_service_info");

			String sql = "SELECT sps_seq, sps_request_no, sps_service_ref_no, sps_request_start_date, sps_request_end_date, sps_nic_no, sps_perfered_lan, sps_operator_name, sps_operator_name_si, sps_operator_name_ta, sps_add1, sps_add1_si, sps_add1_ta, sps_add2, sps_add2_si, sps_add2_ta, sps_city, sps_city_si, sps_city_ta, sps_service_type_code, sps_permit_no, sps_bus_no, sps_origin, sps_destination, sps_via, sps_distance, sps_tips_per_day, sps_service_start_date, sps_service_end_date, sps_province, sps_district_code, sps_dev_sec_code, sps_telephone_no, sps_mobile_no, sps_status, sps_account_no, sps_bank_name_code, sps_branch_code, sps_created_by, sps_created_date, sps_modified_by, sps_modified_date, sps_tender_ref_no, sps_unserved_portion_km, sps_tot_length, sps_agreed_amount, sps_subsidy_type, sps_approve_reject_by, sps_reject_reason, sps_approve_reject_date, sps_service_no, sps_renewal_status, sps_renewal_new_expire_date, sps_is_issue_service_agreement, sps_is_issue_permit_sticker, sps_is_issue_logsheets, sps_service_agreement_issuedby, sps_permit_sticker_issuedby, sps_issue_logsheets_issuedby, sps_service_agreement_issued_date, sps_permit_sticker_issued_date, sps_issue_logsheets_issued_date, is_sltb, sps_is_issue_logsheets_edited, sps_is_issue_service_agreement_edited, sps_is_issue_permit_sticker_edited FROM public.nt_m_sisu_permit_hol_service_info where sps_service_ref_no=?;";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, selectDTO.getServiceRefNo());

			rs = stmt.executeQuery();

			if (rs.next()) {

				sisuSeriyaDTO = new SisuSeriyaDTO();
				sisuSeriyaDTO.setSeqNo(rs.getLong("sps_seq"));
				sisuSeriyaDTO.setRequestNo(rs.getString("sps_request_no"));
				sisuSeriyaDTO.setServiceRefNo(rs.getString("sps_service_ref_no"));
				sisuSeriyaDTO.setRequestStartDateVal(rs.getString("sps_request_start_date"));
				sisuSeriyaDTO.setRequestEndDateVal(rs.getString("sps_request_end_date"));
				sisuSeriyaDTO.setNicNo(rs.getString("sps_nic_no"));
				sisuSeriyaDTO.setLanguageCode(rs.getString("sps_perfered_lan"));
				sisuSeriyaDTO.setNameOfOperator(rs.getString("sps_operator_name"));
				sisuSeriyaDTO.setNameOfOperatorSin(rs.getString("sps_operator_name_si"));
				sisuSeriyaDTO.setNameOfOperatorTamil(rs.getString("sps_operator_name_ta"));
				sisuSeriyaDTO.setAddressOne(rs.getString("sps_add1"));
				sisuSeriyaDTO.setAddressOneSin(rs.getString("sps_add1_si"));
				sisuSeriyaDTO.setAddressOneTamil(rs.getString("sps_add1_ta"));
				sisuSeriyaDTO.setAddressTwo(rs.getString("sps_add2"));
				sisuSeriyaDTO.setAdressTwoSin(rs.getString("sps_add2_si"));
				sisuSeriyaDTO.setAdressTwoTamil(rs.getString("sps_add2_ta"));
				sisuSeriyaDTO.setCity(rs.getString("sps_city"));
				sisuSeriyaDTO.setCitySin(rs.getString("sps_city_si"));
				sisuSeriyaDTO.setCityTamil(rs.getString("sps_city_ta"));
				sisuSeriyaDTO.setServiceTypeCode(rs.getString("sps_service_type_code"));
				sisuSeriyaDTO.setPermitNo(rs.getString("sps_permit_no"));
				sisuSeriyaDTO.setBusRegNo(rs.getString("sps_bus_no"));
				sisuSeriyaDTO.setOriginCode(rs.getString("sps_origin"));
				sisuSeriyaDTO.setDesignationCode(rs.getString("sps_destination"));
				sisuSeriyaDTO.setVia(rs.getString("sps_via"));
				sisuSeriyaDTO.setDistance(rs.getInt("sps_distance"));
				sisuSeriyaDTO.setTripsPerDay(rs.getInt("sps_tips_per_day"));
				sisuSeriyaDTO.setServiceStartDateObj(rs.getTimestamp("sps_service_start_date"));
				sisuSeriyaDTO.setServiceEndDateObj(rs.getTimestamp("sps_service_end_date"));
				sisuSeriyaDTO.setProvinceCode(rs.getString("sps_province"));
				sisuSeriyaDTO.setDistrictCode(rs.getString("sps_district_code"));
				sisuSeriyaDTO.setDivisionalSecCode(rs.getString("sps_dev_sec_code"));
				sisuSeriyaDTO.setTelNo(rs.getString("sps_telephone_no"));
				sisuSeriyaDTO.setMobileNo(rs.getString("sps_mobile_no"));
				sisuSeriyaDTO.setStatus(rs.getString("sps_status"));
				sisuSeriyaDTO.setAccountNo(rs.getString("sps_account_no"));
				sisuSeriyaDTO.setBankName(rs.getString("sps_bank_name_code"));
				sisuSeriyaDTO.setBankBranchNameCode(rs.getString("sps_branch_code"));
				sisuSeriyaDTO.setCreatedBy(rs.getString("sps_created_by"));
				sisuSeriyaDTO.setCratedDate(rs.getTimestamp("sps_created_date"));
				sisuSeriyaDTO.setModifiedBy(rs.getString("sps_modified_by"));
				sisuSeriyaDTO.setModifiedDate(rs.getTimestamp("sps_modified_date"));
				sisuSeriyaDTO.setTenderRefNo(rs.getString("sps_tender_ref_no"));
				sisuSeriyaDTO.setUnservedPortionKM(rs.getString("sps_unserved_portion_km"));
				sisuSeriyaDTO.setTotalLength(rs.getDouble("sps_tot_length"));
				sisuSeriyaDTO.setAgreedAmount(rs.getDouble("sps_agreed_amount"));
				sisuSeriyaDTO.setSubsidyType(rs.getString("sps_subsidy_type"));
				sisuSeriyaDTO.setApproveRejectedBy(rs.getString("sps_approve_reject_by"));
				sisuSeriyaDTO.setRejectReason(rs.getString("sps_reject_reason"));
				sisuSeriyaDTO.setApproveRejectDate(rs.getTimestamp("sps_approve_reject_date"));
				sisuSeriyaDTO.setServiceNo(rs.getString("sps_service_no"));
				sisuSeriyaDTO.setServiceRenewalStatus(rs.getString("sps_renewal_status"));
				sisuSeriyaDTO.setNewExpiryDateObj(rs.getTimestamp("sps_renewal_new_expire_date"));
				sisuSeriyaDTO.setIssueServiceAgreementVal(rs.getString("sps_is_issue_service_agreement"));
				sisuSeriyaDTO.setIssuePermitStickerVal(rs.getString("sps_is_issue_permit_sticker"));
				sisuSeriyaDTO.setIssueLogSheetVal(rs.getString("sps_is_issue_logsheets"));
				sisuSeriyaDTO.setServiceAgreementIssuedBy(rs.getString("sps_service_agreement_issuedby"));
				sisuSeriyaDTO.setPermitStickerIssuedBy(rs.getString("sps_permit_sticker_issuedby"));
				sisuSeriyaDTO.setLogSheetsIssuedBy(rs.getString("sps_issue_logsheets_issuedby"));
				sisuSeriyaDTO.setServiceAgrrementIssuedDateVal(rs.getString("sps_service_agreement_issued_date"));
				sisuSeriyaDTO.setPermitStickerIssuedDateVal(rs.getString("sps_permit_sticker_issued_date"));
				sisuSeriyaDTO.setLogsheetIssuedDateVal(rs.getString("sps_issue_logsheets_issued_date"));
				sisuSeriyaDTO.setSltbStatus(rs.getString("is_sltb"));
				sisuSeriyaDTO.setIssueLogSheetsEditedVal(rs.getString("sps_is_issue_logsheets_edited"));
				sisuSeriyaDTO.setIssueServiceAgreementEdited(rs.getString("sps_is_issue_service_agreement_edited"));
				sisuSeriyaDTO.setIssuePermitStickerEdited(rs.getString("sps_is_issue_permit_sticker_edited"));

				String sql2 = "INSERT INTO public.nt_h_sisu_permit_hol_service_info (sps_seq, sps_request_no, sps_service_ref_no, sps_request_start_date, sps_request_end_date, sps_nic_no, sps_perfered_lan, sps_operator_name, sps_operator_name_si, sps_operator_name_ta, sps_add1, sps_add1_si, sps_add1_ta, sps_add2, sps_add2_si, sps_add2_ta, sps_city, sps_city_si, sps_city_ta, sps_service_type_code, sps_permit_no, sps_bus_no, sps_origin, sps_destination, sps_via, sps_distance, sps_tips_per_day, sps_service_start_date, sps_service_end_date, sps_province, sps_district_code, sps_dev_sec_code, sps_telephone_no, sps_mobile_no, sps_status, sps_account_no, sps_bank_name_code, sps_branch_code, sps_created_by, sps_created_date, sps_modified_by, sps_modified_date, sps_tender_ref_no,  sps_unserved_portion_km, sps_tot_length, sps_agreed_amount, sps_subsidy_type, sps_renewal_status, sps_approve_reject_by, sps_reject_reason, sps_approve_reject_date, sps_service_no, sps_renewal_new_expire_date, sps_is_issue_service_agreement, sps_is_issue_permit_sticker, sps_is_issue_logsheets, sps_service_agreement_issuedby, sps_permit_sticker_issuedby, sps_issue_logsheets_issuedby, sps_service_agreement_issued_date, sps_permit_sticker_issued_date, sps_issue_logsheets_issued_date, is_sltb, sps_is_issue_logsheets_edited, sps_is_issue_service_agreement_edited, sps_is_issue_permit_sticker_edited) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ;";

				stmt2 = con.prepareStatement(sql2);

				stmt2.setLong(1, seqNo);
				stmt2.setString(2, sisuSeriyaDTO.getRequestNo());
				stmt2.setString(3, sisuSeriyaDTO.getServiceRefNo());
				stmt2.setString(4, sisuSeriyaDTO.getRequestStartDateVal());
				stmt2.setString(5, sisuSeriyaDTO.getRequestEndDateVal());
				stmt2.setString(6, sisuSeriyaDTO.getNicNo());
				stmt2.setString(7, sisuSeriyaDTO.getLanguageCode());
				stmt2.setString(8, sisuSeriyaDTO.getNameOfOperator());
				stmt2.setString(9, sisuSeriyaDTO.getNameOfOperatorSin());
				stmt2.setString(10, sisuSeriyaDTO.getNameOfOperatorTamil());
				stmt2.setString(11, sisuSeriyaDTO.getAddressOne());
				stmt2.setString(12, sisuSeriyaDTO.getAddressOneSin());
				stmt2.setString(13, sisuSeriyaDTO.getAddressOneTamil());
				stmt2.setString(14, sisuSeriyaDTO.getAddressTwo());
				stmt2.setString(15, sisuSeriyaDTO.getAdressTwoSin());
				stmt2.setString(16, sisuSeriyaDTO.getAdressTwoTamil());
				stmt2.setString(17, sisuSeriyaDTO.getCity());
				stmt2.setString(18, sisuSeriyaDTO.getCitySin());
				stmt2.setString(19, sisuSeriyaDTO.getCityTamil());
				stmt2.setString(20, sisuSeriyaDTO.getServiceTypeCode());
				stmt2.setString(21, sisuSeriyaDTO.getPermitNo());
				stmt2.setString(22, sisuSeriyaDTO.getBusRegNo());
				stmt2.setString(23, sisuSeriyaDTO.getOriginCode());
				stmt2.setString(24, sisuSeriyaDTO.getDesignationCode());
				stmt2.setString(25, sisuSeriyaDTO.getVia());
				stmt2.setDouble(26, sisuSeriyaDTO.getDistance());
				stmt2.setInt(27, sisuSeriyaDTO.getTripsPerDay());
				stmt2.setTimestamp(28, (Timestamp) sisuSeriyaDTO.getServiceStartDateObj());
				stmt2.setTimestamp(29, (Timestamp) sisuSeriyaDTO.getServiceEndDateObj());
				stmt2.setString(30, sisuSeriyaDTO.getProvinceCode());
				stmt2.setString(31, sisuSeriyaDTO.getDistrictCode());
				stmt2.setString(32, sisuSeriyaDTO.getDivisionalSecCode());
				stmt2.setString(33, sisuSeriyaDTO.getTelNo());
				stmt2.setString(34, sisuSeriyaDTO.getMobileNo());
				stmt2.setString(35, sisuSeriyaDTO.getStatus());
				stmt2.setString(36, sisuSeriyaDTO.getAccountNo());
				stmt2.setString(37, sisuSeriyaDTO.getBankName());
				stmt2.setString(38, sisuSeriyaDTO.getBankBranchNameCode());
				stmt2.setString(39, sisuSeriyaDTO.getCreatedBy());
				stmt2.setTimestamp(40, sisuSeriyaDTO.getCratedDate());
				stmt2.setString(41, sisuSeriyaDTO.getModifiedBy());
				stmt2.setTimestamp(42, sisuSeriyaDTO.getModifiedDate());
				stmt2.setString(43, sisuSeriyaDTO.getTenderRefNo());
				stmt2.setString(44, sisuSeriyaDTO.getUnservedPortionKM());
				stmt2.setDouble(45, sisuSeriyaDTO.getTotalLength());
				stmt2.setDouble(46, sisuSeriyaDTO.getAgreedAmount());
				stmt2.setString(47, sisuSeriyaDTO.getSubsidyType());
				stmt2.setString(48, sisuSeriyaDTO.getServiceRenewalStatus());
				stmt2.setString(49, sisuSeriyaDTO.getApproveRejectedBy());
				stmt2.setString(50, sisuSeriyaDTO.getRejectReason());
				stmt2.setTimestamp(51, sisuSeriyaDTO.getApproveRejectDate());
				stmt2.setString(52, sisuSeriyaDTO.getServiceNo());
				stmt2.setTimestamp(53, rs.getTimestamp("sps_renewal_new_expire_date"));
				stmt2.setString(54, sisuSeriyaDTO.getIssueServiceAgreementVal());
				stmt2.setString(55, sisuSeriyaDTO.getIssuePermitStickerVal());
				stmt2.setString(56, sisuSeriyaDTO.getIssueLogSheetVal());
				stmt2.setString(57, sisuSeriyaDTO.getServiceAgreementIssuedBy());
				stmt2.setString(58, sisuSeriyaDTO.getPermitStickerIssuedBy());
				stmt2.setString(59, sisuSeriyaDTO.getLogSheetsIssuedBy());
				stmt2.setString(60, sisuSeriyaDTO.getServiceAgrrementIssuedDateVal());
				stmt2.setString(61, sisuSeriyaDTO.getPermitStickerIssuedDateVal());
				stmt2.setString(62, sisuSeriyaDTO.getLogsheetIssuedDateVal());
				stmt2.setString(63, sisuSeriyaDTO.getSltbStatus());
				stmt2.setString(64, sisuSeriyaDTO.getIssueLogSheetsEditedVal());
				stmt2.setString(65, sisuSeriyaDTO.getIssueServiceAgreementEdited());
				stmt2.setString(66, sisuSeriyaDTO.getIssuePermitStickerEdited());
				stmt2.executeUpdate();
			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(con);
		}

		return;
	}

	@Override
	public void updateLogSheetValues(int logSheetSeqNo, String logSheetRefNo, String userName) {
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_log_sheet_summary SET  lss_log_ref_no=?, lss_modified_by=?, lss_modified_date=? WHERE lss_seq=?;";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, logSheetRefNo);
			stmt.setString(2, userName);
			stmt.setTimestamp(3, timestamp);

			stmt.setLong(4, logSheetSeqNo);

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

	@Override
	public boolean updatePermitHolderServiceInfoStatus(String status, String serviceRefNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isModelSave = false;

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_sisu_permit_hol_service_info\r\n" + "SET sps_status = ? \r\n"
					+ "WHERE sps_service_ref_no = ? ;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, status);
			stmt.setString(2, serviceRefNo);

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
	public SisuSeriyaDTO getServiceDates(String serviceReferenceNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		SisuSeriyaDTO sisuSeriyaDTO = new SisuSeriyaDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT sps_service_start_date, sps_service_end_date FROM public.nt_m_sisu_permit_hol_service_info where sps_service_ref_no=?;";
			ps = con.prepareStatement(query);
			ps.setString(1, serviceReferenceNo);
			rs = ps.executeQuery();

			while (rs.next()) {

				if (rs.getTimestamp("sps_service_start_date") != null) {
					Timestamp startDateTS = rs.getTimestamp("sps_service_start_date");
					Date startDateObj = new Date(startDateTS.getTime());
					sisuSeriyaDTO.setServiceStartDateObj(startDateObj);
				}

				if (rs.getTimestamp("sps_service_end_date") != null) {
					Timestamp endDateTS = rs.getTimestamp("sps_service_end_date");
					Date endDateObj = new Date(endDateTS.getTime());
					sisuSeriyaDTO.setServiceEndDateObj(endDateObj);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return sisuSeriyaDTO;
	}

	@Override
	public boolean generateLogSheetRefUpadte(SisuSeriyaDTO sisuSeriyaDTO, String logSheetYear, int logSheetCopies,
			String loginUser, List<LogSheetDTO> newLogSheetList, String serviceCode) {

		LogSheetDTO logSheetDetDTO = new LogSheetDTO();
		logSheetDetDTO = getLogSheetValuesForSelectedRefNo(sisuSeriyaDTO, logSheetYear);
		boolean insertSuccess = false;
		int prevNoOfCopies = logSheetDetDTO.getNoOfCopies();

		int i = 0;
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");

		long logRefSeq = logSheetDetDTO.getLogSheetDetSeq();
		
		insertSuccess = saveToLogSheetSummary(logRefSeq, logSheetYear, loginUser, newLogSheetList);
		
//		for (LogSheetDTO temp : newLogSheetList) {
//
//			if (temp.getLogSheetRefNo() != null && !temp.getLogSheetRefNo().isEmpty()
//					&& !temp.getLogSheetRefNo().equalsIgnoreCase("")
//					&& !commonService.existingLogSheetReferenceNo(temp.getLogSheetRefNo(), serviceCode)) {
//				insertSuccess = saveToLogSheetSummary(logRefSeq, logSheetYear, temp.getLogSheetRefNo(), loginUser);
//				i++;
//			}
//		}
		int newNoOfCopies = prevNoOfCopies + i;
		updateToLogSheetsDet(logSheetDetDTO, newNoOfCopies, sisuSeriyaDTO.getServiceRefNo(), logSheetYear, loginUser,
				sisuSeriyaDTO.getServiceTypeCode());

		return insertSuccess;
	}

	@Override
	public List<GamiSeriyaDTO> getApprovedGamiSeriyaRequestNo() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<GamiSeriyaDTO> data = new ArrayList<GamiSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct gps_tender_ref_no FROM public.nt_m_gami_permit_hol_service_info where gps_tender_ref_no is not null and gps_tender_ref_no !='' and gps_status='A' and (gps_service_agreement_issuedby is null or gps_permit_sticker_issuedby is null or gps_issue_logsheets_issuedby is null) order by gps_tender_ref_no desc;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			GamiSeriyaDTO s;

			while (rs.next()) {
				s = new GamiSeriyaDTO();
				s.setTenderRefNo(rs.getString("gps_tender_ref_no"));
				data.add(s);
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
	public List<GamiSeriyaDTO> getApprovedGamiSeriyaServiceRefNoList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<GamiSeriyaDTO> data = new ArrayList<GamiSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct gps_service_ref_no FROM public.nt_m_gami_permit_hol_service_info where gps_service_ref_no is not null and gps_service_ref_no !='' and gps_status='A' and (gps_renewal_status not in ('I') or gps_renewal_status is null) and (gps_service_agreement_issuedby is null or gps_permit_sticker_issuedby is null or gps_issue_logsheets_issuedby is null) order by gps_service_ref_no desc;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			GamiSeriyaDTO s;

			while (rs.next()) {
				s = new GamiSeriyaDTO();
				s.setServiceRefNo(rs.getString("gps_service_ref_no"));
				data.add(s);
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
	public List<GamiSeriyaDTO> getApprovedGamiSeriyaServiceNoList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<GamiSeriyaDTO> data = new ArrayList<GamiSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct gps_service_no FROM public.nt_m_gami_permit_hol_service_info where gps_service_no is not null and gps_service_no !='' and gps_status='A' and (gps_service_agreement_issuedby is null or gps_permit_sticker_issuedby is null or gps_issue_logsheets_issuedby is null) order by gps_service_no desc;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			GamiSeriyaDTO s;

			while (rs.next()) {
				s = new GamiSeriyaDTO();
				s.setServiceNo(rs.getString("gps_service_no"));
				data.add(s);
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
	public List<GamiSeriyaDTO> getGamiSeriyaToIssue() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<GamiSeriyaDTO> gamiSeriyaList = new ArrayList<GamiSeriyaDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select distinct  gps_tender_ref_no, gps_service_ref_no, gps_service_no, to_char(gps_service_end_date,'dd/MM/yyyy') as gps_service_end_date, gps_status, gps_operator_name, "
					+ "gps_is_issue_service_agreement, gps_is_issue_permit_sticker, gps_is_issue_logsheets, gps_service_agreement_issuedby, gps_permit_sticker_issuedby, gps_issue_logsheets_issuedby, gps_service_agreement_issued_date, gps_permit_sticker_issued_date, gps_issue_logsheets_issued_date FROM public.nt_m_gami_permit_hol_service_info where gps_status='A' and (gps_service_agreement_issuedby is null or gps_permit_sticker_issuedby is null or gps_issue_logsheets_issuedby is null);";

			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();
			GamiSeriyaDTO e;

			while (rs.next()) {
				e = new GamiSeriyaDTO();
				e.setTenderRefNo(rs.getString("gps_tender_ref_no"));
				e.setServiceRefNo(rs.getString("gps_service_ref_no"));
				e.setServiceNo(rs.getString("gps_service_no"));
				e.setNameinFull(rs.getString("gps_operator_name"));
				e.setStrServiceEndDate(rs.getString("gps_service_end_date"));
				String approveStatusCode = rs.getString("gps_status");
				String approveStatus = "";

				if (approveStatusCode != null) {
					if (approveStatusCode.equals("A")) {
						approveStatus = "APPROVED";
					} else if (approveStatusCode.equals("R")) {
						approveStatus = "REJECTED";
					} else if (approveStatusCode.equals("P")) {
						approveStatus = "PENDING";
					} else if (approveStatusCode.equals("O")) {
						approveStatus = "ONGOING";
					}
				}
				e.setGpsStatus(approveStatus);
				e.setGpsStatusCode(approveStatusCode);

				e.setServiceAgreementIssuedBy(rs.getString("gps_service_agreement_issuedby"));
				e.setPermitStickerIssuedBy(rs.getString("gps_permit_sticker_issuedby"));
				e.setLogSheetsIssuedBy(rs.getString("gps_issue_logsheets_issuedby"));

				if (rs.getString("gps_is_issue_service_agreement") != null
						&& rs.getString("gps_is_issue_service_agreement").equalsIgnoreCase("Y")) {
					e.setIssuedServiceAgreement(true);
				} else {
					e.setIssuedServiceAgreement(false);
				}

				if (rs.getString("gps_is_issue_permit_sticker") != null
						&& rs.getString("gps_is_issue_permit_sticker").equalsIgnoreCase("Y")) {
					e.setIssuedPermitSticker(true);
				} else {
					e.setIssuedPermitSticker(false);
				}

				if (rs.getString("gps_is_issue_logsheets") != null
						&& rs.getString("gps_is_issue_logsheets").equalsIgnoreCase("Y")) {
					e.setIssuedLogSheets(true);
				} else {
					e.setIssuedLogSheets(false);
				}

				String sps_service_agreement_issued_dateString = rs.getString("gps_service_agreement_issued_date");
				if (sps_service_agreement_issued_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setServiceAgreementIssuedDate = originalFormat.parse(sps_service_agreement_issued_dateString);
					e.setServiceAgreementIssuedDate(setServiceAgreementIssuedDate);
				}

				String sps_permit_sticker_issued_dateString = rs.getString("gps_permit_sticker_issued_date");
				if (sps_permit_sticker_issued_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setPermitStickerIssuedDate = originalFormat.parse(sps_permit_sticker_issued_dateString);
					e.setPermitStickerIssuedDate(setPermitStickerIssuedDate);
				}

				String sps_issue_logsheets_issued_dateString = rs.getString("gps_issue_logsheets_issued_date");
				if (sps_issue_logsheets_issued_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setLogSheetsIssuedDate = originalFormat.parse(sps_issue_logsheets_issued_dateString);
					e.setLogSheetsIssuedDate(setLogSheetsIssuedDate);
				}

				gamiSeriyaList.add(e);
			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return gamiSeriyaList;
	}

	@Override
	public List<GamiSeriyaDTO> getGamiSeriyaToIssueBySearch(GamiSeriyaDTO gamiSeriyaDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<GamiSeriyaDTO> gamiSeriyaList = new ArrayList<GamiSeriyaDTO>();
		String WHERE_SQL = "";
		boolean whereadded = false;

		if (gamiSeriyaDTO.getTenderRefNo() != null && !gamiSeriyaDTO.getTenderRefNo().equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE gps_tender_ref_no = " + "'" + gamiSeriyaDTO.getTenderRefNo()
					+ "' and gps_status='A'  and  (gps_service_agreement_issuedby IS NULL "
					+ "				or gps_permit_sticker_issuedby IS NULL or gps_issue_logsheets_issuedby IS null) and gps_service_ref_no is not null and gps_service_ref_no != ''";
			whereadded = true;
		}
		if (gamiSeriyaDTO.getServiceRefNo() != null && !gamiSeriyaDTO.getServiceRefNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND gps_service_ref_no = " + "'" + gamiSeriyaDTO.getServiceRefNo() + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE gps_service_ref_no = " + "'" + gamiSeriyaDTO.getServiceRefNo()
						+ "' and gps_status='A'  and  (gps_service_agreement_issuedby IS NULL "
						+ "				or gps_permit_sticker_issuedby IS NULL or gps_issue_logsheets_issuedby IS null) and gps_service_ref_no is not null and gps_service_ref_no != ''";
				whereadded = true;
			}
		}

		if (gamiSeriyaDTO.getServiceNo() != null && !gamiSeriyaDTO.getServiceNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND gps_service_no = " + "'" + gamiSeriyaDTO.getServiceNo() + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE gps_service_no = " + "'" + gamiSeriyaDTO.getServiceNo()
						+ "' and gps_status='A'  and  (gps_service_agreement_issuedby IS NULL "
						+ "				or gps_permit_sticker_issuedby IS NULL or gps_issue_logsheets_issuedby IS null) and gps_service_ref_no is not null and gps_service_ref_no != ''";
				whereadded = true;
			}
		}

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select distinct gps_tender_ref_no, gps_service_ref_no,gps_service_no,"
					+ " to_char(gps_service_end_date,'dd/MM/yyyy') as gps_service_end_date ,gps_status, gps_operator_name,"
					+ " gps_is_issue_service_agreement," + " gps_is_issue_permit_sticker," + " gps_is_issue_logsheets,"
					+ " gps_service_agreement_issuedby," + " gps_permit_sticker_issuedby,"
					+ " gps_issue_logsheets_issuedby," + " gps_service_agreement_issued_date,"
					+ " gps_permit_sticker_issued_date," + " gps_issue_logsheets_issued_date"
					+ " from public.nt_m_gami_permit_hol_service_info " + WHERE_SQL;

			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();
			GamiSeriyaDTO e;

			while (rs.next()) {
				e = new GamiSeriyaDTO();
				e.setTenderRefNo(rs.getString("gps_tender_ref_no"));
				e.setServiceRefNo(rs.getString("gps_service_ref_no"));
				e.setServiceNo(rs.getString("gps_service_no"));
				e.setNameinFull(rs.getString("gps_operator_name"));
				e.setStrServiceEndDate(rs.getString("gps_service_end_date"));

				String approveStatusCode = rs.getString("gps_status");
				String approveStatus = "";

				if (approveStatusCode != null) {
					if (approveStatusCode.equals("A")) {
						approveStatus = "APPROVED";
					} else if (approveStatusCode.equals("R")) {
						approveStatus = "REJECTED";
					} else if (approveStatusCode.equals("P")) {
						approveStatus = "PENDING";
					} else if (approveStatusCode.equals("O")) {
						approveStatus = "ONGOING";
					}
				}
				e.setGpsStatus(approveStatus);
				e.setGpsStatusCode(approveStatusCode);

				e.setServiceAgreementIssuedBy(rs.getString("gps_service_agreement_issuedby"));
				e.setPermitStickerIssuedBy(rs.getString("gps_permit_sticker_issuedby"));
				e.setLogSheetsIssuedBy(rs.getString("gps_issue_logsheets_issuedby"));

				if (rs.getString("gps_is_issue_service_agreement") != null
						&& rs.getString("gps_is_issue_service_agreement").equalsIgnoreCase("Y")) {
					e.setIssuedServiceAgreement(true);
				} else {
					e.setIssuedServiceAgreement(false);
				}

				if (rs.getString("gps_is_issue_permit_sticker") != null
						&& rs.getString("gps_is_issue_permit_sticker").equalsIgnoreCase("Y")) {
					e.setIssuedPermitSticker(true);
				} else {
					e.setIssuedPermitSticker(false);
				}

				if (rs.getString("gps_is_issue_logsheets") != null
						&& rs.getString("gps_is_issue_logsheets").equalsIgnoreCase("Y")) {
					e.setIssuedLogSheets(true);
				} else {
					e.setIssuedLogSheets(false);
				}

				String sps_service_agreement_issued_dateString = rs.getString("gps_service_agreement_issued_date");
				if (sps_service_agreement_issued_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setServiceAgreementIssuedDate = originalFormat.parse(sps_service_agreement_issued_dateString);
					e.setServiceAgreementIssuedDate(setServiceAgreementIssuedDate);
				}

				String sps_permit_sticker_issued_dateString = rs.getString("gps_permit_sticker_issued_date");
				if (sps_permit_sticker_issued_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setPermitStickerIssuedDate = originalFormat.parse(sps_permit_sticker_issued_dateString);
					e.setPermitStickerIssuedDate(setPermitStickerIssuedDate);
				}

				String sps_issue_logsheets_issued_dateString = rs.getString("gps_issue_logsheets_issued_date");
				if (sps_issue_logsheets_issued_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setLogSheetsIssuedDate = originalFormat.parse(sps_issue_logsheets_issued_dateString);
					e.setLogSheetsIssuedDate(setLogSheetsIssuedDate);
				}

				gamiSeriyaList.add(e);
			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return gamiSeriyaList;
	}

	@Override
	public void updateIssueServiceAgreementPermitStickerLogSheetsGamiSeriya(GamiSeriyaDTO selectDTO) {
		java.util.Date date = new java.util.Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_gami_permit_hol_service_info SET ";

			switch (selectDTO.getIssueType()) {
			case 1:
				String getServiceAgreementIssuedDate = (dateFormat.format(selectDTO.getServiceAgreementIssuedDate()));
				sql = sql + "gps_is_issue_service_agreement='Y',gps_service_agreement_issuedby='"
						+ selectDTO.getCurrentUser() + "',gps_service_agreement_issued_date='"
						+ getServiceAgreementIssuedDate + "',";
				break;
			case 2:
				String getPermitStickerIssuedDate = (dateFormat.format(selectDTO.getPermitStickerIssuedDate()));
				sql = sql + "gps_is_issue_permit_sticker='Y',gps_permit_sticker_issuedby='" + selectDTO.getCurrentUser()
						+ "',gps_permit_sticker_issued_date='" + getPermitStickerIssuedDate + "',";
				break;
			case 3:
				String getLogSheetsIssuedDate = (dateFormat.format(selectDTO.getLogSheetsIssuedDate()));
				sql = sql + "gps_is_issue_logsheets='Y',gps_issue_logsheets_issuedby='" + selectDTO.getCurrentUser()
						+ "',gps_issue_logsheets_issued_date='" + getLogSheetsIssuedDate + "',";
				break;
			}

			sql = sql + " gps_modified_by=?,gps_modified_date=? WHERE gps_service_ref_no=?";

			try {
				stmt = con.prepareStatement(sql);

				stmt.setString(1, selectDTO.getCurrentUser());

				stmt.setTimestamp(2, timestamp);

				stmt.setString(3, selectDTO.getServiceRefNo());

				stmt.executeUpdate();

				con.commit();
			}catch (SQLException e) {
				con.rollback();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

	}

	@Override
	public boolean generateLogSheetRefGamiSeriya(GamiSeriyaDTO selectDTO, String logSheetYear, int logSheetCopies,
			String loginUser, List<LogSheetDTO> newLogSheetList) {
		long logRefSeq;

		logRefSeq = saveToLogSheets(selectDTO.getServiceRefNo(), logSheetYear, logSheetCopies, loginUser,
				selectDTO.getServiceTypeCode());

		if(logRefSeq != 0) {
			boolean success = saveToLogSheetSummary(logRefSeq, logSheetYear, loginUser, newLogSheetList);
			return success;
		}
		
		return false;
		
		/*
		 * for (LogSheetDTO temp : newLogSheetList) { if (temp.getLogSheetRefNo() !=
		 * null && !temp.getLogSheetRefNo().isEmpty() &&
		 * !temp.getLogSheetRefNo().equalsIgnoreCase("")) {
		 * saveToLogSheetSummary(logRefSeq, logSheetYear, temp.getLogSheetRefNo(),
		 * loginUser); } }
		 */
	}

	@Override
	public List<GamiSeriyaDTO> getApprovedTenderNoGamiSeriyaForEditMode() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<GamiSeriyaDTO> data = new ArrayList<GamiSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct gps_tender_ref_no from public.nt_m_gami_permit_hol_service_info WHERE gps_tender_ref_no is not null and gps_tender_ref_no !='' and gps_status='A' and  (gps_service_agreement_issuedby is not NULL and gps_permit_sticker_issuedby is not NULL and gps_issue_logsheets_issuedby is not NULL ) order by gps_tender_ref_no desc;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			GamiSeriyaDTO s;

			while (rs.next()) {
				s = new GamiSeriyaDTO();
				s.setTenderRefNo(rs.getString("gps_tender_ref_no"));
				data.add(s);
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
	public List<GamiSeriyaDTO> getApprovedServiceRefNoListGamiSeriyaForEditMode() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<GamiSeriyaDTO> data = new ArrayList<GamiSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct gps_service_ref_no from public.nt_m_gami_permit_hol_service_info WHERE gps_service_ref_no is not null and gps_service_ref_no !='' and gps_status='A' and (gps_renewal_status not in ('I') or gps_renewal_status is null)   and  (gps_service_agreement_issuedby is not NULL and gps_permit_sticker_issuedby IS not NULL and  gps_issue_logsheets_issuedby is not null) order by gps_service_ref_no desc ;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			GamiSeriyaDTO s;

			while (rs.next()) {
				s = new GamiSeriyaDTO();
				s.setServiceRefNo(rs.getString("gps_service_ref_no"));
				data.add(s);
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
	public List<GamiSeriyaDTO> getApprovedServiceNoListGamiSeriyaForEditMode() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<GamiSeriyaDTO> data = new ArrayList<GamiSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct gps_service_no  from public.nt_m_gami_permit_hol_service_info WHERE gps_service_no is not null and gps_service_no !=''  and  gps_status='A'   and (gps_renewal_status not in ('I') or gps_renewal_status is null) and  (gps_service_agreement_issuedby is not NULL and gps_permit_sticker_issuedby is not NULL and gps_issue_logsheets_issuedby is not null) order by gps_service_no desc;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			GamiSeriyaDTO s;

			while (rs.next()) {
				s = new GamiSeriyaDTO();
				s.setServiceNo(rs.getString("gps_service_no"));
				data.add(s);
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
	public List<GamiSeriyaDTO> getGamiSeriyaToIssueForEditMode() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<GamiSeriyaDTO> sisuSeriyaList = new ArrayList<GamiSeriyaDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select distinct gps_tender_ref_no, gps_service_ref_no,gps_service_no,"
					+ " to_char(gps_service_end_date,'dd/MM/yyyy') as gps_service_end_date ,gps_status, gps_operator_name,gps_is_issue_service_agreement,"
					+ " gps_is_issue_permit_sticker," + " gps_is_issue_logsheets," + " gps_service_agreement_issuedby,"
					+ " gps_permit_sticker_issuedby," + " gps_issue_logsheets_issuedby,"
					+ " gps_service_agreement_issued_date," + " gps_permit_sticker_issued_date,"
					+ " gps_issue_logsheets_issued_date from public.nt_m_gami_permit_hol_service_info where gps_status='A' and  (gps_service_agreement_issuedby is not NULL and gps_permit_sticker_issuedby is not NULL and gps_issue_logsheets_issuedby is not null)";

			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();
			GamiSeriyaDTO e;

			while (rs.next()) {
				e = new GamiSeriyaDTO();
				e.setTenderRefNo(rs.getString("gps_tender_ref_no"));
				e.setServiceRefNo(rs.getString("gps_service_ref_no"));

				e.setServiceNo(rs.getString("gps_service_no"));
				e.setNameinFull(rs.getString("gps_operator_name"));
				e.setStrServiceEndDate(rs.getString("gps_service_end_date"));

				String approveStatusCode = rs.getString("gps_status");
				String approveStatus = "";

				if (approveStatusCode != null) {
					if (approveStatusCode.equals("A")) {
						approveStatus = "APPROVED";
					} else if (approveStatusCode.equals("R")) {
						approveStatus = "REJECTED";
					} else if (approveStatusCode.equals("P")) {
						approveStatus = "PENDING";
					} else if (approveStatusCode.equals("O")) {
						approveStatus = "ONGOING";
					}
				}
				e.setGpsStatus(approveStatus);
				e.setGpsStatusCode(approveStatusCode);

				e.setServiceAgreementIssuedBy(rs.getString("gps_service_agreement_issuedby"));
				e.setPermitStickerIssuedBy(rs.getString("gps_permit_sticker_issuedby"));
				e.setLogSheetsIssuedBy(rs.getString("gps_issue_logsheets_issuedby"));

				if (rs.getString("gps_is_issue_service_agreement") != null
						&& rs.getString("gps_is_issue_service_agreement").equalsIgnoreCase("Y")) {
				}

				if (rs.getString("gps_is_issue_permit_sticker") != null
						&& rs.getString("gps_is_issue_permit_sticker").equalsIgnoreCase("Y")) {
				}

				if (rs.getString("gps_is_issue_logsheets") != null
						&& rs.getString("gps_is_issue_logsheets").equalsIgnoreCase("Y")) {
				}

				String sps_service_agreement_issued_dateString = rs.getString("gps_service_agreement_issued_date");
				if (sps_service_agreement_issued_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setServiceAgreementIssuedDate = originalFormat.parse(sps_service_agreement_issued_dateString);
					e.setServiceAgreementIssuedDate(setServiceAgreementIssuedDate);
				}

				String sps_permit_sticker_issued_dateString = rs.getString("gps_permit_sticker_issued_date");
				if (sps_permit_sticker_issued_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setPermitStickerIssuedDate = originalFormat.parse(sps_permit_sticker_issued_dateString);
					e.setPermitStickerIssuedDate(setPermitStickerIssuedDate);
				}

				String sps_issue_logsheets_issued_dateString = rs.getString("gps_issue_logsheets_issued_date");
				if (sps_issue_logsheets_issued_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setLogSheetsIssuedDate = originalFormat.parse(sps_issue_logsheets_issued_dateString);
					e.setLogSheetsIssuedDate(setLogSheetsIssuedDate);
				}

				sisuSeriyaList.add(e);
			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return sisuSeriyaList;
	}

	@Override
	public List<GamiSeriyaDTO> getGamiSeriyaToIssueBySearchForEditMode(GamiSeriyaDTO gamiSeriyaDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<GamiSeriyaDTO> gamiSeriyaList = new ArrayList<GamiSeriyaDTO>();
		String WHERE_SQL = "";
		boolean whereadded = false;

		if (gamiSeriyaDTO.getTenderRefNo() != null && !gamiSeriyaDTO.getTenderRefNo().equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE gps_tender_ref_no = " + "'" + gamiSeriyaDTO.getTenderRefNo()
					+ "' and gps_status='A'  and  (gps_service_agreement_issuedby is not NULL and gps_permit_sticker_issuedby is not NULL and gps_issue_logsheets_issuedby is not null) and gps_service_ref_no is not null and gps_service_ref_no != ''";
			whereadded = true;
		}
		if (gamiSeriyaDTO.getServiceRefNo() != null && !gamiSeriyaDTO.getServiceRefNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND gps_service_ref_no = " + "'" + gamiSeriyaDTO.getServiceRefNo() + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE gps_service_ref_no = " + "'" + gamiSeriyaDTO.getServiceRefNo()
						+ "' and gps_status='A'  and  (gps_service_agreement_issuedby is not NULL and gps_permit_sticker_issuedby is not NULL and gps_issue_logsheets_issuedby is not null) and gps_service_ref_no is not null and gps_service_ref_no != ''";
				whereadded = true;
			}
		}

		if (gamiSeriyaDTO.getServiceNo() != null && !gamiSeriyaDTO.getServiceNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND gps_service_no = " + "'" + gamiSeriyaDTO.getServiceNo() + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE gps_service_no = " + "'" + gamiSeriyaDTO.getServiceNo()
						+ "' and gps_status='A'  and  (gps_service_agreement_issuedby is not NULL and gps_permit_sticker_issuedby is not NULL and gps_issue_logsheets_issuedby is not null) and gps_service_ref_no is not null and gps_service_ref_no != ''";
				whereadded = true;
			}
		}

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select distinct gps_tender_ref_no, gps_service_ref_no, gps_service_no,"
					+ " to_char(gps_service_end_date,'dd/MM/yyyy') as gps_service_end_date ,gps_status, gps_operator_name,"
					+ " gps_is_issue_service_agreement," + " gps_is_issue_permit_sticker," + " gps_is_issue_logsheets,"
					+ " gps_service_agreement_issuedby," + " gps_permit_sticker_issuedby,"
					+ " gps_issue_logsheets_issuedby," + " gps_service_agreement_issued_date,"
					+ " gps_permit_sticker_issued_date," + " gps_issue_logsheets_issued_date"
					+ " from public.nt_m_gami_permit_hol_service_info " + WHERE_SQL;

			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();
			GamiSeriyaDTO e;

			while (rs.next()) {
				e = new GamiSeriyaDTO();
				e.setTenderRefNo(rs.getString("gps_tender_ref_no"));
				e.setServiceRefNo(rs.getString("gps_service_ref_no"));
				e.setServiceNo(rs.getString("gps_service_no"));
				e.setNameinFull(rs.getString("gps_operator_name"));
				e.setStrServiceEndDate(rs.getString("gps_service_end_date"));

				String approveStatusCode = rs.getString("gps_status");
				String approveStatus = "";

				if (approveStatusCode != null) {
					if (approveStatusCode.equals("A")) {
						approveStatus = "APPROVED";
					} else if (approveStatusCode.equals("R")) {
						approveStatus = "REJECTED";
					} else if (approveStatusCode.equals("P")) {
						approveStatus = "PENDING";
					} else if (approveStatusCode.equals("O")) {
						approveStatus = "ONGOING";
					}
				}
				e.setGpsStatus(approveStatus);
				e.setGpsStatusCode(approveStatusCode);

				e.setServiceAgreementIssuedBy(rs.getString("gps_service_agreement_issuedby"));
				e.setPermitStickerIssuedBy(rs.getString("gps_permit_sticker_issuedby"));
				e.setLogSheetsIssuedBy(rs.getString("gps_issue_logsheets_issuedby"));

				if (rs.getString("gps_is_issue_service_agreement") != null
						&& rs.getString("gps_is_issue_service_agreement").equalsIgnoreCase("Y")) {
				}

				if (rs.getString("gps_is_issue_permit_sticker") != null
						&& rs.getString("gps_is_issue_permit_sticker").equalsIgnoreCase("Y")) {
				}

				if (rs.getString("gps_is_issue_logsheets") != null
						&& rs.getString("gps_is_issue_logsheets").equalsIgnoreCase("Y")) {
				}

				String sps_service_agreement_issued_dateString = rs.getString("gps_service_agreement_issued_date");
				if (sps_service_agreement_issued_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setServiceAgreementIssuedDate = originalFormat.parse(sps_service_agreement_issued_dateString);
					e.setServiceAgreementIssuedDate(setServiceAgreementIssuedDate);
				}

				String sps_permit_sticker_issued_dateString = rs.getString("gps_permit_sticker_issued_date");
				if (sps_permit_sticker_issued_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setPermitStickerIssuedDate = originalFormat.parse(sps_permit_sticker_issued_dateString);
					e.setPermitStickerIssuedDate(setPermitStickerIssuedDate);
				}

				String sps_issue_logsheets_issued_dateString = rs.getString("gps_issue_logsheets_issued_date");
				if (sps_issue_logsheets_issued_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setLogSheetsIssuedDate = originalFormat.parse(sps_issue_logsheets_issued_dateString);
					e.setLogSheetsIssuedDate(setLogSheetsIssuedDate);
				}

				gamiSeriyaList.add(e);
			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return gamiSeriyaList;
	}

	@Override
	public void insetOldRecordIntoGamiSeriyaInfoHis(GamiSeriyaDTO selectDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		GamiSeriyaDTO gamiSeriyaDTO = null;

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_h_sisu_permit_hol_service_info");

			String sql = "SELECT gps_seq, gps_tender_ref_no, gps_service_ref_no, gps_permit_no_prpta, gps_perfered_lan, gps_service_type_code, gps_bus_no, gps_unserved_portion_km, gps_sequence_no, gps_origin, gps_total_length, gps_destination, gps_agreed_amount, gps_via, gps_service_start_date, gps_nic_no, gps_service_end_date, gps_operator_name, gps_operator_name_si, gps_operator_name_ta, gps_add1, gps_add1_si, gps_add1_ta, gps_add2, gps_add2_si, gps_add2_ta, gps_city, gps_city_si, gps_city_ta, gps_status, gps_province, gps_telephone_no, gps_district_code, gps_mobile_no, gps_dev_sec_code, gps_account_no, gps_bank_name_code, gps_branch_code, gps_created_by, gps_created_date, gps_modified_by, gps_modified_date, gps_subsidy_type, gps_approve_reject_by, gps_reject_reason, gps_approve_reject_date, gps_service_no, gps_renewal_status, gps_renewal_new_expire_date, gps_is_issue_service_agreement, gps_is_issue_permit_sticker, gps_is_issue_logsheets, gps_service_agreement_issuedby, gps_permit_sticker_issuedby, gps_issue_logsheets_issuedby, gps_service_agreement_issued_date, gps_permit_sticker_issued_date, gps_issue_logsheets_issued_date, gps_is_issue_logsheets_edited, gps_is_issue_service_agreement_edited, gps_is_issue_permit_sticker_edited FROM public.nt_m_gami_permit_hol_service_info where gps_service_ref_no=? ;";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, selectDTO.getServiceRefNo());

			rs = stmt.executeQuery();

			if (rs.next()) {

				gamiSeriyaDTO = new GamiSeriyaDTO();
				gamiSeriyaDTO.setSeqNum(rs.getLong("gps_seq"));
				gamiSeriyaDTO.setTenderRefNo(rs.getString("gps_tender_ref_no"));
				gamiSeriyaDTO.setServiceRefNo(rs.getString("gps_service_ref_no"));
				gamiSeriyaDTO.setPermitNo(rs.getString("gps_permit_no_prpta"));
				gamiSeriyaDTO.setPreferedLanguage(rs.getString("gps_perfered_lan"));
				gamiSeriyaDTO.setServiceTypeCode(rs.getString("gps_service_type_code"));
				gamiSeriyaDTO.setBusNo(rs.getString("gps_bus_no"));
				gamiSeriyaDTO.setUnservedPortion(rs.getDouble("gps_unserved_portion_km"));
				gamiSeriyaDTO.setSeqNo(rs.getString("gps_sequence_no"));
				gamiSeriyaDTO.setOrigin(rs.getString("gps_origin"));
				gamiSeriyaDTO.setTotalLength(rs.getDouble("gps_total_length"));
				gamiSeriyaDTO.setDestination(rs.getString("gps_destination"));
				gamiSeriyaDTO.setAgreedAmount(rs.getBigDecimal("gps_agreed_amount"));
				gamiSeriyaDTO.setVia(rs.getString("gps_via"));
				gamiSeriyaDTO.setServiceStartDateObj(rs.getTimestamp("gps_service_start_date"));
				gamiSeriyaDTO.setNicNo(rs.getString("gps_nic_no"));
				gamiSeriyaDTO.setServiceEndDateObj(rs.getTimestamp("gps_service_end_date"));
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
				gamiSeriyaDTO.setGpsStatusCode(rs.getString("gps_status"));
				gamiSeriyaDTO.setProvinceCode(rs.getString("gps_province"));
				gamiSeriyaDTO.setDistrictCode(rs.getString("gps_district_code"));
				gamiSeriyaDTO.setDivisionalSecCode(rs.getString("gps_dev_sec_code"));
				gamiSeriyaDTO.setTelephoneNo(rs.getString("gps_telephone_no"));
				gamiSeriyaDTO.setMobileNo(rs.getString("gps_mobile_no"));
				gamiSeriyaDTO.setAccountNo(rs.getString("gps_account_no"));
				gamiSeriyaDTO.setBankNameCode(rs.getString("gps_bank_name_code"));
				gamiSeriyaDTO.setBankBranchNameCode(rs.getString("gps_branch_code"));
				gamiSeriyaDTO.setCreatedBy(rs.getString("gps_created_by"));
				gamiSeriyaDTO.setCratedDate(rs.getTimestamp("gps_created_date"));
				gamiSeriyaDTO.setModifiedBy(rs.getString("gps_modified_by"));
				gamiSeriyaDTO.setModifiedDate(rs.getTimestamp("gps_modified_date"));
				gamiSeriyaDTO.setSubCityType(rs.getString("gps_subsidy_type"));
				gamiSeriyaDTO.setApproveRejectedBy(rs.getString("gps_approve_reject_by"));
				gamiSeriyaDTO.setRejectReason(rs.getString("gps_reject_reason"));
				gamiSeriyaDTO.setApproveRejectDate(rs.getTimestamp("gps_approve_reject_date"));
				gamiSeriyaDTO.setServiceNo(rs.getString("gps_service_no"));
				gamiSeriyaDTO.setServiceRenewalStatus(rs.getString("gps_renewal_status"));
				gamiSeriyaDTO.setNewExpiryDateObj(rs.getTimestamp("gps_renewal_new_expire_date"));
				gamiSeriyaDTO.setIssueServiceAgreementVal(rs.getString("gps_is_issue_service_agreement"));
				gamiSeriyaDTO.setIssuePermitStickerVal(rs.getString("gps_is_issue_permit_sticker"));
				gamiSeriyaDTO.setIssueLogSheetVal(rs.getString("gps_is_issue_logsheets"));
				gamiSeriyaDTO.setServiceAgreementIssuedBy(rs.getString("gps_service_agreement_issuedby"));
				gamiSeriyaDTO.setPermitStickerIssuedBy(rs.getString("gps_permit_sticker_issuedby"));
				gamiSeriyaDTO.setLogSheetsIssuedBy(rs.getString("gps_issue_logsheets_issuedby"));
				gamiSeriyaDTO.setServiceAgrrementIssuedDateVal(rs.getString("gps_service_agreement_issued_date"));
				gamiSeriyaDTO.setPermitStickerIssuedDateVal(rs.getString("gps_permit_sticker_issued_date"));
				gamiSeriyaDTO.setLogsheetIssuedDateVal(rs.getString("gps_issue_logsheets_issued_date"));
				gamiSeriyaDTO.setIssueLogSheetsEditedVal(rs.getString("gps_is_issue_logsheets_edited"));
				gamiSeriyaDTO.setIssueServiceAgreementEdited(rs.getString("gps_is_issue_service_agreement_edited"));
				gamiSeriyaDTO.setIssuePermitStickerEdited(rs.getString("gps_is_issue_permit_sticker_edited"));

				String sql2 = "INSERT INTO public.nt_h_gami_permit_hol_service_info (gps_seq, gps_tender_ref_no, gps_service_ref_no, gps_permit_no_prpta, gps_perfered_lan, gps_service_type_code, gps_bus_no, gps_unserved_portion_km, "
						+ "gps_sequence_no, gps_origin, gps_total_length, gps_destination, gps_agreed_amount, gps_via, gps_service_start_date, gps_nic_no, gps_service_end_date, "
						+ "gps_operator_name, gps_operator_name_si, gps_operator_name_ta, gps_add1, gps_add1_si, gps_add1_ta, gps_add2, gps_add2_si, gps_add2_ta, gps_city, "
						+ "gps_city_si, gps_city_ta, gps_status, gps_province, gps_telephone_no, gps_district_code, gps_mobile_no, gps_dev_sec_code, gps_account_no, gps_bank_name_code,"
						+ " gps_branch_code, gps_created_by, gps_created_date, gps_modified_by, gps_modified_date, gps_subsidy_type, gps_approve_reject_by, "
						+ "gps_reject_reason, gps_approve_reject_date, gps_service_no, gps_renewal_status, gps_renewal_new_expire_date, "
						+ "gps_is_issue_service_agreement, gps_is_issue_permit_sticker, gps_is_issue_logsheets, gps_service_agreement_issuedby, gps_permit_sticker_issuedby, gps_issue_logsheets_issuedby, gps_service_agreement_issued_date, gps_permit_sticker_issued_date, gps_issue_logsheets_issued_date, gps_is_issue_logsheets_edited, gps_is_issue_service_agreement_edited, gps_is_issue_permit_sticker_edited) "
						+ "VALUES(?, ?, ?, " + "?, ?, ?, " + "?, ?, ?, " + "?, ?, ?, " + "?, ?, ?, " + "?, ?, ?,"
						+ " ?, ?, ?, " + "?, ?, ?, " + "?, ?, ?," + " ?, ?, ?, " + "?, ?, ?," + " ?, ?, ?,"
						+ " ?, ?, ?, " + "?, ?, ?, " + "?, ?, ?, " + "?, ?, ?, " + "?, ?, ?, " + "?, ?, ?, "
						+ "?, ?, ?, " + "?, ?, ?, " + "? );";

				stmt2 = con.prepareStatement(sql2);

				stmt2.setLong(1, seqNo);
				stmt2.setString(2, gamiSeriyaDTO.getTenderRefNo());
				stmt2.setString(3, gamiSeriyaDTO.getServiceRefNo());
				stmt2.setString(4, gamiSeriyaDTO.getPermitNo());
				stmt2.setString(5, gamiSeriyaDTO.getPreferedLanguage());
				stmt2.setString(6, gamiSeriyaDTO.getServiceTypeCode());
				stmt2.setString(7, gamiSeriyaDTO.getBusNo());
				stmt2.setDouble(8, gamiSeriyaDTO.getUnservedPortion());
				stmt2.setString(9, gamiSeriyaDTO.getSeqNo());
				stmt2.setString(10, gamiSeriyaDTO.getOrigin());
				stmt2.setDouble(11, gamiSeriyaDTO.getTotalLength());
				stmt2.setString(12, gamiSeriyaDTO.getDestination());
				stmt2.setBigDecimal(13, gamiSeriyaDTO.getAgreedAmount());
				stmt2.setString(14, gamiSeriyaDTO.getVia());
				stmt2.setTimestamp(15, (Timestamp) gamiSeriyaDTO.getServiceStartDateObj());
				stmt2.setString(16, gamiSeriyaDTO.getNicNo());
				stmt2.setTimestamp(17, (Timestamp) gamiSeriyaDTO.getServiceEndDateObj());
				stmt2.setString(18, gamiSeriyaDTO.getNameinFull());
				stmt2.setString(19, gamiSeriyaDTO.getNameinFullSinhala());
				stmt2.setString(20, gamiSeriyaDTO.getNameinFullTamil());
				stmt2.setString(21, gamiSeriyaDTO.getAddress1());
				stmt2.setString(22, gamiSeriyaDTO.getAddress1sinhala());
				stmt2.setString(23, gamiSeriyaDTO.getAddress1Tamil());
				stmt2.setString(24, gamiSeriyaDTO.getAddress2());
				stmt2.setString(25, gamiSeriyaDTO.getAddress2Sinhala());
				stmt2.setString(26, gamiSeriyaDTO.getAddress2Tamil());
				stmt2.setString(27, gamiSeriyaDTO.getCity());
				stmt2.setString(28, gamiSeriyaDTO.getCitySinhala());
				stmt2.setString(29, gamiSeriyaDTO.getCityTamil());
				stmt2.setString(30, gamiSeriyaDTO.getGpsStatusCode());
				stmt2.setString(31, gamiSeriyaDTO.getProvinceCode());
				stmt2.setString(32, gamiSeriyaDTO.getTelephoneNo());
				stmt2.setString(33, gamiSeriyaDTO.getDistrictCode());
				stmt2.setString(34, gamiSeriyaDTO.getMobileNo());
				stmt2.setString(35, gamiSeriyaDTO.getDivisionalSecCode());
				stmt2.setString(36, gamiSeriyaDTO.getAccountNo());
				stmt2.setString(37, gamiSeriyaDTO.getBankNameCode());
				stmt2.setString(38, gamiSeriyaDTO.getBankBranchNameCode());
				stmt2.setString(39, gamiSeriyaDTO.getCreatedBy());
				stmt2.setTimestamp(40, gamiSeriyaDTO.getCratedDate());
				stmt2.setString(41, gamiSeriyaDTO.getModifiedBy());
				stmt2.setTimestamp(42, gamiSeriyaDTO.getModifiedDate());
				stmt2.setString(43, gamiSeriyaDTO.getSubCityType());
				stmt2.setString(44, gamiSeriyaDTO.getApproveRejectedBy());
				stmt2.setString(45, gamiSeriyaDTO.getRejectReason());
				stmt2.setTimestamp(46, gamiSeriyaDTO.getApproveRejectDate());
				stmt2.setString(47, gamiSeriyaDTO.getServiceNo());
				stmt2.setString(48, gamiSeriyaDTO.getServiceRenewalStatus());
				stmt2.setTimestamp(49, rs.getTimestamp("gps_renewal_new_expire_date"));
				stmt2.setString(50, gamiSeriyaDTO.getIssueServiceAgreementVal());
				stmt2.setString(51, gamiSeriyaDTO.getIssuePermitStickerVal());
				stmt2.setString(52, gamiSeriyaDTO.getIssueLogSheetVal());
				stmt2.setString(53, gamiSeriyaDTO.getServiceAgreementIssuedBy());
				stmt2.setString(54, gamiSeriyaDTO.getPermitStickerIssuedBy());
				stmt2.setString(55, gamiSeriyaDTO.getLogSheetsIssuedBy());
				stmt2.setString(56, gamiSeriyaDTO.getServiceAgrrementIssuedDateVal());
				stmt2.setString(57, gamiSeriyaDTO.getPermitStickerIssuedDateVal());
				stmt2.setString(58, gamiSeriyaDTO.getLogsheetIssuedDateVal());
				stmt2.setString(59, gamiSeriyaDTO.getIssueLogSheetsEditedVal());
				stmt2.setString(60, gamiSeriyaDTO.getIssueServiceAgreementEdited());
				stmt2.setString(61, gamiSeriyaDTO.getIssuePermitStickerEdited());
				stmt2.executeUpdate();
			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(con);
		}

		return;
	}

	@Override
	public void updateIssueServiceAgreementPermitStickerLogSheetsForGamiSeriyaEditMode(GamiSeriyaDTO selectDTO) {
		java.util.Date date = new java.util.Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_gami_permit_hol_service_info SET ";

			switch (selectDTO.getIssueType()) {
			case 1:
				String getServiceAgreementIssuedDate = (dateFormat.format(selectDTO.getServiceAgreementIssuedDate()));
				sql = sql + "gps_is_issue_service_agreement_edited='Y', gps_service_agreement_issuedby='"
						+ selectDTO.getCurrentUser() + "',gps_service_agreement_issued_date='"
						+ getServiceAgreementIssuedDate + "',";
				break;
			case 2:
				String getPermitStickerIssuedDate = (dateFormat.format(selectDTO.getPermitStickerIssuedDate()));
				sql = sql + "gps_is_issue_permit_sticker_edited='Y', gps_permit_sticker_issuedby='"
						+ selectDTO.getCurrentUser() + "',gps_permit_sticker_issued_date='" + getPermitStickerIssuedDate
						+ "',";
				break;
			case 3:
				String getLogSheetsIssuedDate = (dateFormat.format(selectDTO.getLogSheetsIssuedDate()));
				sql = sql + "gps_is_issue_logsheets_edited='Y', gps_issue_logsheets_issuedby='"
						+ selectDTO.getCurrentUser() + "',gps_issue_logsheets_issued_date='" + getLogSheetsIssuedDate
						+ "',";
				break;
			}

			sql = sql + " gps_modified_by=?,gps_modified_date=? WHERE gps_service_ref_no=?";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, selectDTO.getCurrentUser());

			stmt.setTimestamp(2, timestamp);

			stmt.setString(3, selectDTO.getServiceRefNo());

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

	@Override
	public List<NisiSeriyaDTO> getApprovedRequestNoForNisiSeriya() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<NisiSeriyaDTO> data = new ArrayList<NisiSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct	nri_request_no FROM public.nt_m_nri_requester_info	where nri_request_no is not null and nri_request_no !='' and nri_status='A' and  (nri_service_agreement_issuedby IS NULL or nri_permit_sticker_issuedby IS NULL or nri_issue_logsheets_issuedby IS null) order by nri_request_no desc; ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			NisiSeriyaDTO s;

			while (rs.next()) {
				s = new NisiSeriyaDTO();
				s.setRequestNo(rs.getString("nri_request_no"));
				data.add(s);
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
	public List<NisiSeriyaDTO> getApprovedServiceRefNoListForNisiSeriya() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<NisiSeriyaDTO> data = new ArrayList<NisiSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct nri_service_ref_no from public.nt_m_nri_requester_info WHERE nri_service_ref_no is not null and nri_service_ref_no !='' and nri_status='A' and  (nri_service_agreement_issuedby IS NULL or nri_permit_sticker_issuedby IS NULL or nri_issue_logsheets_issuedby IS null) order by nri_service_ref_no desc ;	";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			NisiSeriyaDTO s;

			while (rs.next()) {
				s = new NisiSeriyaDTO();
				s.setServiceRefNo(rs.getString("nri_service_ref_no"));
				data.add(s);
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
	public List<NisiSeriyaDTO> getApprovedServiceNoListForNisiSeriya() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<NisiSeriyaDTO> data = new ArrayList<NisiSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct nri_service_agreement_no from public.nt_m_nri_requester_info WHERE nri_service_agreement_no is not null and nri_service_agreement_no !='' and  nri_status='A' and  (nri_service_agreement_issuedby IS NULL or nri_permit_sticker_issuedby IS NULL or nri_issue_logsheets_issuedby IS null)   order by nri_service_agreement_no desc; ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			NisiSeriyaDTO s;

			while (rs.next()) {
				s = new NisiSeriyaDTO();
				s.setServiceNo(rs.getString("nri_service_agreement_no"));
				data.add(s);
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
	public List<NisiSeriyaDTO> getNisiSeriyaToIssue() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<NisiSeriyaDTO> nisiSeriyaList = new ArrayList<NisiSeriyaDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select distinct nri_request_no, nri_service_ref_no,nri_service_agreement_no, to_char(nri_service_end_date,'dd/MM/yyyy') as nri_service_end_date ,nri_status, nri_operator_name,nri_is_issue_service_agreement, nri_is_issue_permit_sticker, nri_is_issue_logsheets, nri_service_agreement_issuedby, nri_permit_sticker_issuedby, nri_issue_logsheets_issuedby, nri_service_agreement_issued_date, nri_permit_sticker_issued_date, nri_issue_logsheets_issued_date from public.nt_m_nri_requester_info where nri_status='A' and  (nri_service_agreement_issuedby IS NULL or nri_permit_sticker_issuedby IS NULL or nri_issue_logsheets_issuedby IS null);";

			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();
			NisiSeriyaDTO e;

			while (rs.next()) {
				e = new NisiSeriyaDTO();
				e.setRequestNo(rs.getString("nri_request_no"));
				e.setServiceRefNo(rs.getString("nri_service_ref_no"));

				e.setServiceNo(rs.getString("nri_service_agreement_no"));

				e.setNameOfOperator(rs.getString("nri_operator_name"));
				e.setServiceEndDateVal(rs.getString("nri_service_end_date"));
				String approveStatusCode = rs.getString("nri_status");
				String approveStatus = "";

				if (approveStatusCode != null) {
					if (approveStatusCode.equals("A")) {
						approveStatus = "APPROVED";
					} else if (approveStatusCode.equals("R")) {
						approveStatus = "REJECTED";
					} else if (approveStatusCode.equals("P")) {
						approveStatus = "PENDING";
					}
				}

				e.setStatus(approveStatus);

				e.setServiceAgreementIssuedBy(rs.getString("nri_service_agreement_issuedby"));
				e.setPermitStickerIssuedBy(rs.getString("nri_permit_sticker_issuedby"));
				e.setLogSheetsIssuedBy(rs.getString("nri_issue_logsheets_issuedby"));

				if (rs.getString("nri_is_issue_service_agreement") != null
						&& rs.getString("nri_is_issue_service_agreement").equalsIgnoreCase("Y")) {
					e.setIssuedServiceAgreement(true);
				} else {
					e.setIssuedServiceAgreement(false);
				}

				if (rs.getString("nri_is_issue_permit_sticker") != null
						&& rs.getString("nri_is_issue_permit_sticker").equalsIgnoreCase("Y")) {
					e.setIssuedPermitSticker(true);
				} else {
					e.setIssuedPermitSticker(false);
				}

				if (rs.getString("nri_is_issue_logsheets") != null
						&& rs.getString("nri_is_issue_logsheets").equalsIgnoreCase("Y")) {
					e.setIssuedLogSheets(true);
				} else {
					e.setIssuedLogSheets(false);
				}

				String sps_service_agreement_issued_dateString = rs.getString("nri_service_agreement_issued_date");
				if (sps_service_agreement_issued_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setServiceAgreementIssuedDate = originalFormat.parse(sps_service_agreement_issued_dateString);
					e.setServiceAgreementIssuedDate(setServiceAgreementIssuedDate);
				}

				String sps_permit_sticker_issued_dateString = rs.getString("nri_permit_sticker_issued_date");
				if (sps_permit_sticker_issued_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setPermitStickerIssuedDate = originalFormat.parse(sps_permit_sticker_issued_dateString);
					e.setPermitStickerIssuedDate(setPermitStickerIssuedDate);
				}

				String sps_issue_logsheets_issued_dateString = rs.getString("nri_issue_logsheets_issued_date");
				if (sps_issue_logsheets_issued_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setLogSheetsIssuedDate = originalFormat.parse(sps_issue_logsheets_issued_dateString);
					e.setLogSheetsIssuedDate(setLogSheetsIssuedDate);
				}

				nisiSeriyaList.add(e);
			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return nisiSeriyaList;
	}

	@Override
	public List<NisiSeriyaDTO> getNisiSeriyaToIssueBySearch(NisiSeriyaDTO nisiSeriyaDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<NisiSeriyaDTO> nisiSeriyaList = new ArrayList<NisiSeriyaDTO>();
		String WHERE_SQL = "";
		boolean whereadded = false;

		if (nisiSeriyaDTO.getRequestNo() != null && !nisiSeriyaDTO.getRequestNo().equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE nri_request_no = " + "'" + nisiSeriyaDTO.getRequestNo()
					+ "' and nri_status='A'  and  (nri_service_agreement_issuedby IS NULL "
					+ "				or nri_permit_sticker_issuedby IS NULL or nri_issue_logsheets_issuedby IS null) and nri_service_ref_no is not null and nri_service_ref_no != ''";
			whereadded = true;
		}
		if (nisiSeriyaDTO.getServiceRefNo() != null && !nisiSeriyaDTO.getServiceRefNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND nri_service_ref_no = " + "'" + nisiSeriyaDTO.getServiceRefNo() + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE nri_service_ref_no = " + "'" + nisiSeriyaDTO.getServiceRefNo()
						+ "' and nri_status='A'  and  (nri_service_agreement_issuedby IS NULL "
						+ "				or nri_permit_sticker_issuedby IS NULL or nri_issue_logsheets_issuedby IS null) and nri_service_ref_no is not null and nri_service_ref_no != ''";
				whereadded = true;
			}
		}

		if (nisiSeriyaDTO.getRequestStartDate() != null && nisiSeriyaDTO.getRequestEndDate() != null) {
			Timestamp startDate = new Timestamp(nisiSeriyaDTO.getRequestStartDate().getTime());
			Timestamp endDate = new Timestamp(nisiSeriyaDTO.getRequestEndDate().getTime());
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + "and nt_m_nri_requester_info.nri_request_date >= " + "' " + startDate
						+ "' and nt_m_nri_requester_info.nri_request_date <= " + "'" + endDate + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + "WHERE nt_m_nri_requester_info.nri_request_date >= " + "' " + startDate
						+ "' and nt_m_nri_requester_info.nri_request_date <= " + "'" + endDate
						+ "' and nri_status='A'  and  (nri_service_agreement_issuedby IS NULL "
						+ "				or nri_permit_sticker_issuedby IS NULL or nri_issue_logsheets_issuedby IS null) and nri_service_ref_no is not null and nri_service_ref_no != ''";
				whereadded = true;
			}
		} else if (nisiSeriyaDTO.getRequestEndDate() != null) {

			Timestamp endDate = new Timestamp(nisiSeriyaDTO.getRequestEndDate().getTime());
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " and nt_m_nri_requester_info.nri_request_date <= " + "'" + endDate + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE  nt_m_nri_requester_info.nri_request_date <= " + "'" + endDate
						+ "' and nri_status='A'  and  (nri_service_agreement_issuedby IS NULL "
						+ "				or nri_permit_sticker_issuedby IS NULL or nri_issue_logsheets_issuedby IS null) and nri_service_ref_no is not null and nri_service_ref_no != ''";
				whereadded = true;
			}
		} else if (nisiSeriyaDTO.getRequestStartDate() != null) {
			Timestamp startDate = new Timestamp(nisiSeriyaDTO.getRequestStartDate().getTime());

			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " and nt_m_nri_requester_info.nri_request_date >= " + "'" + startDate + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE nt_m_nri_requester_info.nri_request_date >= " + "'" + startDate
						+ "' and nri_status='A'  and  (nri_service_agreement_issuedby IS NULL "
						+ "				or nri_permit_sticker_issuedby IS NULL or nri_issue_logsheets_issuedby IS null) and nri_service_ref_no is not null and nri_service_ref_no != ''";
				whereadded = true;
			}
		}

		if (nisiSeriyaDTO.getServiceNo() != null && !nisiSeriyaDTO.getServiceNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND nri_service_agreement_no = " + "'" + nisiSeriyaDTO.getServiceNo() + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE nri_service_agreement_no = " + "'" + nisiSeriyaDTO.getServiceNo()
						+ "' and nri_status='A'  and  (nri_service_agreement_issuedby IS NULL "
						+ "				or nri_permit_sticker_issuedby IS NULL or nri_issue_logsheets_issuedby IS null) and nri_service_ref_no is not null and nri_service_ref_no != ''";
				whereadded = true;
			}
		}

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select distinct nri_request_no, nri_service_ref_no,nri_service_agreement_no, to_char(nri_service_end_date,'dd/MM/yyyy') as nri_service_end_date ,nri_status, nri_operator_name, nri_is_issue_service_agreement, nri_is_issue_permit_sticker, nri_is_issue_logsheets, nri_service_agreement_issuedby, nri_permit_sticker_issuedby, nri_issue_logsheets_issuedby, nri_service_agreement_issued_date, nri_permit_sticker_issued_date, nri_issue_logsheets_issued_date from public.nt_m_nri_requester_info "
					+ WHERE_SQL;

			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();
			NisiSeriyaDTO e;

			while (rs.next()) {
				e = new NisiSeriyaDTO();
				e.setRequestNo(rs.getString("nri_request_no"));
				e.setServiceRefNo(rs.getString("nri_service_ref_no"));
				e.setServiceNo(rs.getString("nri_service_agreement_no"));
				e.setNameOfOperator(rs.getString("nri_operator_name"));

				e.setServiceEndDateVal(rs.getString("nri_service_end_date"));

				String approveStatusCode = rs.getString("nri_status");
				String approveStatus = "";

				if (approveStatusCode != null) {
					if (approveStatusCode.equals("A")) {
						approveStatus = "APPROVED";
					} else if (approveStatusCode.equals("R")) {
						approveStatus = "REJECTED";
					} else if (approveStatusCode.equals("P")) {
						approveStatus = "PENDING";
					}
				}

				e.setStatus(approveStatus);

				e.setServiceAgreementIssuedBy(rs.getString("nri_service_agreement_issuedby"));
				e.setPermitStickerIssuedBy(rs.getString("nri_permit_sticker_issuedby"));
				e.setLogSheetsIssuedBy(rs.getString("nri_issue_logsheets_issuedby"));

				if (rs.getString("nri_is_issue_service_agreement") != null
						&& rs.getString("nri_is_issue_service_agreement").equalsIgnoreCase("Y")) {
					e.setIssuedServiceAgreement(true);
				} else {
					e.setIssuedServiceAgreement(false);
				}

				if (rs.getString("nri_is_issue_permit_sticker") != null
						&& rs.getString("nri_is_issue_permit_sticker").equalsIgnoreCase("Y")) {
					e.setIssuedPermitSticker(true);
				} else {
					e.setIssuedPermitSticker(false);
				}

				if (rs.getString("nri_is_issue_logsheets") != null
						&& rs.getString("nri_is_issue_logsheets").equalsIgnoreCase("Y")) {
					e.setIssuedLogSheets(true);
				} else {
					e.setIssuedLogSheets(false);
				}

				String sps_service_agreement_issued_dateString = rs.getString("nri_service_agreement_issued_date");
				if (sps_service_agreement_issued_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setServiceAgreementIssuedDate = originalFormat.parse(sps_service_agreement_issued_dateString);
					e.setServiceAgreementIssuedDate(setServiceAgreementIssuedDate);
				}

				String sps_permit_sticker_issued_dateString = rs.getString("nri_permit_sticker_issued_date");
				if (sps_permit_sticker_issued_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setPermitStickerIssuedDate = originalFormat.parse(sps_permit_sticker_issued_dateString);
					e.setPermitStickerIssuedDate(setPermitStickerIssuedDate);
				}

				String sps_issue_logsheets_issued_dateString = rs.getString("nri_issue_logsheets_issued_date");
				if (sps_issue_logsheets_issued_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setLogSheetsIssuedDate = originalFormat.parse(sps_issue_logsheets_issued_dateString);
					e.setLogSheetsIssuedDate(setLogSheetsIssuedDate);
				}

				nisiSeriyaList.add(e);
			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return nisiSeriyaList;
	}

	@Override
	public void updateIssueServiceAgreementPermitStickerLogSheetsForNisiSeriya(NisiSeriyaDTO selectDTO) {
		java.util.Date date = new java.util.Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_nri_requester_info SET ";

			switch (selectDTO.getIssueType()) {
			case 1:
				String getServiceAgreementIssuedDate = (dateFormat.format(selectDTO.getServiceAgreementIssuedDate()));
				sql = sql + "nri_is_issue_service_agreement='Y',nri_service_agreement_issuedby='"
						+ selectDTO.getCurrentUser() + "',nri_service_agreement_issued_date='"
						+ getServiceAgreementIssuedDate + "',";
				break;
			case 2:
				String getPermitStickerIssuedDate = (dateFormat.format(selectDTO.getPermitStickerIssuedDate()));
				sql = sql + "nri_is_issue_permit_sticker='Y', nri_permit_sticker_issuedby='"
						+ selectDTO.getCurrentUser() + "',nri_permit_sticker_issued_date='" + getPermitStickerIssuedDate
						+ "',";
				break;
			case 3:
				String getLogSheetsIssuedDate = (dateFormat.format(selectDTO.getLogSheetsIssuedDate()));
				sql = sql + " nri_is_issue_logsheets='Y',nri_issue_logsheets_issuedby='" + selectDTO.getCurrentUser()
						+ "', nri_issue_logsheets_issued_date='" + getLogSheetsIssuedDate + "',";
				break;
			}

			sql = sql + " modified_by=?,modified_date=? WHERE nri_service_ref_no=?";

			try {
				stmt = con.prepareStatement(sql);

				stmt.setString(1, selectDTO.getCurrentUser());

				stmt.setTimestamp(2, timestamp);

				stmt.setString(3, selectDTO.getServiceRefNo());

				stmt.executeUpdate();

				con.commit();
			}catch (SQLException e) {
				con.rollback();
				e.printStackTrace();
			}
			

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
	}

	@Override
	public boolean generateLogSheetRefNisiSeriya(NisiSeriyaDTO selectDTO, String logSheetYear, int logSheetCopies,
			String loginUser, List<LogSheetDTO> newLogSheetList) {

		long logRefSeq;

		logRefSeq = saveToLogSheets(selectDTO.getServiceRefNo(), logSheetYear, logSheetCopies, loginUser,
				selectDTO.getServiceTypeCode());

		if(logRefSeq != 0) {
			boolean success = saveToLogSheetSummary(logRefSeq, logSheetYear, loginUser,newLogSheetList);
			return success;
		}
		
		return false;
		/*
		 * for (LogSheetDTO temp : newLogSheetList) { if (temp.getLogSheetRefNo() !=
		 * null && !temp.getLogSheetRefNo().isEmpty() &&
		 * !temp.getLogSheetRefNo().equalsIgnoreCase("")) {
		 * saveToLogSheetSummary(logRefSeq, logSheetYear, temp.getLogSheetRefNo(),
		 * loginUser); } }
		 */
	}

	@Override
	public List<NisiSeriyaDTO> getApprovedRequestNoForEditModeNisiSeriya() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<NisiSeriyaDTO> data = new ArrayList<NisiSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct nri_request_no from public.nt_m_nri_requester_info WHERE nri_request_no is not null and nri_request_no !='' and nri_status='A' and  (nri_service_agreement_issuedby is not NULL and nri_permit_sticker_issuedby is not NULL and nri_issue_logsheets_issuedby is not NULL ) order by nri_request_no desc;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			NisiSeriyaDTO s;

			while (rs.next()) {
				s = new NisiSeriyaDTO();
				s.setRequestNo(rs.getString("nri_request_no"));
				data.add(s);
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
	public List<NisiSeriyaDTO> getApprovedServiceRefNoListForEditModeNisiSeriya() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<NisiSeriyaDTO> data = new ArrayList<NisiSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct nri_service_ref_no from public.nt_m_nri_requester_info WHERE nri_service_ref_no is not null and nri_service_ref_no !='' and nri_status='A' and  (nri_service_agreement_issuedby is not NULL and nri_permit_sticker_issuedby IS not NULL and  nri_issue_logsheets_issuedby is not null) order by nri_service_ref_no desc ;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			NisiSeriyaDTO s;

			while (rs.next()) {
				s = new NisiSeriyaDTO();
				s.setServiceRefNo(rs.getString("nri_service_ref_no"));
				data.add(s);
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
	public List<NisiSeriyaDTO> getApprovedServiceNoListForEditModeNisiSeriya() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<NisiSeriyaDTO> data = new ArrayList<NisiSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct nri_service_agreement_no  from public.nt_m_nri_requester_info WHERE nri_service_agreement_no is not null and nri_service_agreement_no !=''  and  nri_status='A' and  (nri_service_agreement_issuedby is not NULL and nri_permit_sticker_issuedby is not NULL and nri_issue_logsheets_issuedby is not null) order by nri_service_agreement_no desc;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			NisiSeriyaDTO s;

			while (rs.next()) {
				s = new NisiSeriyaDTO();
				s.setServiceNo(rs.getString("nri_service_agreement_no"));
				data.add(s);
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
	public List<NisiSeriyaDTO> getSisuSeriyaToIssueForEditModeNisiSeriya() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<NisiSeriyaDTO> nisiSeriyaList = new ArrayList<NisiSeriyaDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select distinct nri_request_no, nri_service_ref_no,nri_service_agreement_no, to_char(nri_service_end_date,'dd/MM/yyyy') as nri_service_end_date ,nri_status, nri_operator_name,nri_is_issue_service_agreement, nri_is_issue_permit_sticker,  nri_is_issue_logsheets, nri_service_agreement_issuedby, nri_permit_sticker_issuedby, nri_issue_logsheets_issuedby, nri_service_agreement_issued_date, nri_permit_sticker_issued_date, nri_issue_logsheets_issued_date from public.nt_m_nri_requester_info where nri_status='A' and  (nri_service_agreement_issuedby is not NULL and nri_permit_sticker_issuedby is not NULL and nri_issue_logsheets_issuedby is not null); ";

			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();
			NisiSeriyaDTO e;

			while (rs.next()) {
				e = new NisiSeriyaDTO();
				e.setRequestNo(rs.getString("nri_request_no"));
				e.setServiceRefNo(rs.getString("nri_service_ref_no"));

				e.setServiceNo(rs.getString("nri_service_agreement_no"));

				e.setNameOfOperator(rs.getString("nri_operator_name"));
				e.setServiceEndDateVal(rs.getString("nri_service_end_date"));
				String approveStatusCode = rs.getString("nri_status");
				String approveStatus = "";

				if (approveStatusCode != null) {
					if (approveStatusCode.equals("A")) {
						approveStatus = "APPROVED";
					} else if (approveStatusCode.equals("R")) {
						approveStatus = "REJECTED";
					} else if (approveStatusCode.equals("P")) {
						approveStatus = "PENDING";
					}
				}

				e.setStatus(approveStatus);

				e.setServiceAgreementIssuedBy(rs.getString("nri_service_agreement_issuedby"));
				e.setPermitStickerIssuedBy(rs.getString("nri_permit_sticker_issuedby"));
				e.setLogSheetsIssuedBy(rs.getString("nri_issue_logsheets_issuedby"));

				if (rs.getString("nri_is_issue_service_agreement") != null
						&& rs.getString("nri_is_issue_service_agreement").equalsIgnoreCase("Y")) {
				}

				if (rs.getString("nri_is_issue_permit_sticker") != null
						&& rs.getString("nri_is_issue_permit_sticker").equalsIgnoreCase("Y")) {
				}

				if (rs.getString("nri_is_issue_logsheets") != null
						&& rs.getString("nri_is_issue_logsheets").equalsIgnoreCase("Y")) {
				}

				String sps_service_agreement_issued_dateString = rs.getString("nri_service_agreement_issued_date");
				if (sps_service_agreement_issued_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setServiceAgreementIssuedDate = originalFormat.parse(sps_service_agreement_issued_dateString);
					e.setServiceAgreementIssuedDate(setServiceAgreementIssuedDate);
				}

				String sps_permit_sticker_issued_dateString = rs.getString("nri_permit_sticker_issued_date");
				if (sps_permit_sticker_issued_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setPermitStickerIssuedDate = originalFormat.parse(sps_permit_sticker_issued_dateString);
					e.setPermitStickerIssuedDate(setPermitStickerIssuedDate);
				}

				String sps_issue_logsheets_issued_dateString = rs.getString("nri_issue_logsheets_issued_date");
				if (sps_issue_logsheets_issued_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setLogSheetsIssuedDate = originalFormat.parse(sps_issue_logsheets_issued_dateString);
					e.setLogSheetsIssuedDate(setLogSheetsIssuedDate);
				}

				nisiSeriyaList.add(e);
			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return nisiSeriyaList;
	}

	@Override
	public List<NisiSeriyaDTO> getSisuSeriyaToIssueBySearchForEditModeNisiSeriya(NisiSeriyaDTO nisiSeriyaDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<NisiSeriyaDTO> nisiSeriyaList = new ArrayList<NisiSeriyaDTO>();
		String WHERE_SQL = "";
		boolean whereadded = false;

		if (nisiSeriyaDTO.getRequestNo() != null && !nisiSeriyaDTO.getRequestNo().equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE nri_request_no = " + "'" + nisiSeriyaDTO.getRequestNo()
					+ "' and nri_status='A'  and  (nri_service_agreement_issuedby is not NULL and nri_permit_sticker_issuedby is not NULL and nri_issue_logsheets_issuedby is not null) and nri_service_ref_no is not null and nri_service_ref_no != ''";
			whereadded = true;
		}
		if (nisiSeriyaDTO.getServiceRefNo() != null && !nisiSeriyaDTO.getServiceRefNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND nri_service_ref_no = " + "'" + nisiSeriyaDTO.getServiceRefNo() + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE nri_service_ref_no = " + "'" + nisiSeriyaDTO.getServiceRefNo()
						+ "' and nri_status='A'  and  (nri_service_agreement_issuedby is not NULL and nri_permit_sticker_issuedby is not NULL and nri_issue_logsheets_issuedby is not null) and nri_service_ref_no is not null and nri_service_ref_no != ''";
				whereadded = true;
			}
		}

		if (nisiSeriyaDTO.getRequestStartDate() != null && nisiSeriyaDTO.getRequestEndDate() != null) {
			Timestamp startDate = new Timestamp(nisiSeriyaDTO.getRequestStartDate().getTime());
			Timestamp endDate = new Timestamp(nisiSeriyaDTO.getRequestEndDate().getTime());
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + "and nt_m_nri_requester_info.nri_request_date >= " + "' " + startDate
						+ "' and nt_m_nri_requester_info.nri_request_date <= " + "'" + endDate + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + "WHERE nt_m_nri_requester_info.nri_request_date >= " + "' " + startDate
						+ "' and nt_m_nri_requester_info.nri_request_date <= " + "'" + endDate
						+ "' and nri_status='A'  and  (nri_service_agreement_issuedby is not NULL and nri_permit_sticker_issuedby is not NULL and nri_issue_logsheets_issuedby is not null) and nri_service_ref_no is not null and nri_service_ref_no != ''";
				whereadded = true;
			}
		} else if (nisiSeriyaDTO.getRequestEndDate() != null) {

			Timestamp endDate = new Timestamp(nisiSeriyaDTO.getRequestEndDate().getTime());
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " and nt_m_nri_requester_info.nri_request_date <= " + "'" + endDate + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE  nt_m_nri_requester_info.nri_request_date <= " + "'" + endDate
						+ "' and nri_status='A'  and  (nri_service_agreement_issuedby is not NULL and nri_permit_sticker_issuedby is not NULL and nri_issue_logsheets_issuedby is not null) and nri_service_ref_no is not null and nri_service_ref_no != ''";
				whereadded = true;
			}
		} else if (nisiSeriyaDTO.getRequestStartDate() != null) {
			Timestamp startDate = new Timestamp(nisiSeriyaDTO.getRequestStartDate().getTime());

			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " and nt_m_nri_requester_info.nri_request_date >= " + "'" + startDate + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE nt_m_nri_requester_info.nri_request_date >= " + "'" + startDate
						+ "' and nri_status='A'  and  (nri_service_agreement_issuedby is not NULL and nri_permit_sticker_issuedby is not NULL and nri_issue_logsheets_issuedby is not null) and nri_service_ref_no is not null and nri_service_ref_no != ''";
				whereadded = true;
			}
		}

		if (nisiSeriyaDTO.getServiceNo() != null && !nisiSeriyaDTO.getServiceNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND nri_service_agreement_no = " + "'" + nisiSeriyaDTO.getServiceNo() + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE nri_service_agreement_no = " + "'" + nisiSeriyaDTO.getServiceNo()
						+ "' and nri_status='A'  and  (nri_service_agreement_issuedby is not NULL and nri_permit_sticker_issuedby is not NULL and nri_issue_logsheets_issuedby is not null) and nri_service_ref_no is not null and nri_service_ref_no != ''";
				whereadded = true;
			}
		}

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select distinct nri_request_no, nri_service_ref_no,nri_service_agreement_no, to_char(nri_service_end_date,'dd/MM/yyyy') as nri_service_end_date ,nri_status, nri_operator_name, nri_is_issue_service_agreement, nri_is_issue_permit_sticker, nri_is_issue_logsheets, nri_service_agreement_issuedby, nri_permit_sticker_issuedby, nri_issue_logsheets_issuedby, nri_service_agreement_issued_date, nri_permit_sticker_issued_date, nri_issue_logsheets_issued_date from public.nt_m_nri_requester_info "
					+ WHERE_SQL;

			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();
			NisiSeriyaDTO e;

			while (rs.next()) {
				e = new NisiSeriyaDTO();
				e.setRequestNo(rs.getString("nri_request_no"));
				e.setServiceRefNo(rs.getString("nri_service_ref_no"));
				e.setServiceNo(rs.getString("nri_service_agreement_no"));
				e.setNameOfOperator(rs.getString("nri_operator_name"));

				e.setServiceEndDateVal(rs.getString("nri_service_end_date"));

				String approveStatusCode = rs.getString("nri_status");
				String approveStatus = "";

				if (approveStatusCode != null) {
					if (approveStatusCode.equals("A")) {
						approveStatus = "APPROVED";
					} else if (approveStatusCode.equals("R")) {
						approveStatus = "REJECTED";
					} else if (approveStatusCode.equals("P")) {
						approveStatus = "PENDING";
					}
				}

				e.setStatus(approveStatus);

				e.setServiceAgreementIssuedBy(rs.getString("nri_service_agreement_issuedby"));
				e.setPermitStickerIssuedBy(rs.getString("nri_permit_sticker_issuedby"));
				e.setLogSheetsIssuedBy(rs.getString("nri_issue_logsheets_issuedby"));

				if (rs.getString("nri_is_issue_service_agreement") != null
						&& rs.getString("nri_is_issue_service_agreement").equalsIgnoreCase("Y")) {
				}

				if (rs.getString("nri_is_issue_permit_sticker") != null
						&& rs.getString("nri_is_issue_permit_sticker").equalsIgnoreCase("Y")) {
				}

				if (rs.getString("nri_is_issue_logsheets") != null
						&& rs.getString("nri_is_issue_logsheets").equalsIgnoreCase("Y")) {
				}

				String sps_service_agreement_issued_dateString = rs.getString("nri_service_agreement_issued_date");
				if (sps_service_agreement_issued_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setServiceAgreementIssuedDate = originalFormat.parse(sps_service_agreement_issued_dateString);
					e.setServiceAgreementIssuedDate(setServiceAgreementIssuedDate);
				}

				String sps_permit_sticker_issued_dateString = rs.getString("nri_permit_sticker_issued_date");
				if (sps_permit_sticker_issued_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setPermitStickerIssuedDate = originalFormat.parse(sps_permit_sticker_issued_dateString);
					e.setPermitStickerIssuedDate(setPermitStickerIssuedDate);
				}

				String sps_issue_logsheets_issued_dateString = rs.getString("nri_issue_logsheets_issued_date");
				if (sps_issue_logsheets_issued_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setLogSheetsIssuedDate = originalFormat.parse(sps_issue_logsheets_issued_dateString);
					e.setLogSheetsIssuedDate(setLogSheetsIssuedDate);
				}

				nisiSeriyaList.add(e);
			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return nisiSeriyaList;
	}

	@Override
	public void updateIssueServiceAgreementPermitStickerLogSheetsForEditModeNisiSeriya(NisiSeriyaDTO selectDTO) {
		java.util.Date date = new java.util.Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_nri_requester_info SET ";

			switch (selectDTO.getIssueType()) {
			case 1:
				String getServiceAgreementIssuedDate = (dateFormat.format(selectDTO.getServiceAgreementIssuedDate()));
				sql = sql + "nri_is_issue_service_agreement_edited='Y',nri_service_agreement_issuedby='"
						+ selectDTO.getCurrentUser() + "',nri_service_agreement_issued_date='"
						+ getServiceAgreementIssuedDate + "',";
				break;
			case 2:
				String getPermitStickerIssuedDate = (dateFormat.format(selectDTO.getPermitStickerIssuedDate()));
				sql = sql + "nri_is_issue_permit_sticker_edited='Y',nri_permit_sticker_issuedby='"
						+ selectDTO.getCurrentUser() + "',nri_permit_sticker_issued_date='" + getPermitStickerIssuedDate
						+ "',";
				break;
			case 3:
				String getLogSheetsIssuedDate = (dateFormat.format(selectDTO.getLogSheetsIssuedDate()));
				sql = sql + "nri_is_issue_logsheets_edited='Y',nri_issue_logsheets_issuedby='"
						+ selectDTO.getCurrentUser() + "',nri_issue_logsheets_issued_date='" + getLogSheetsIssuedDate
						+ "',";
				break;
			}

			sql = sql + " modified_by=?,modified_date=? WHERE nri_service_ref_no=?";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, selectDTO.getCurrentUser());

			stmt.setTimestamp(2, timestamp);

			stmt.setString(3, selectDTO.getServiceRefNo());

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

	@Override
	public List<SisuSeriyaDTO> getApprovedServiceRefNoListGamiSeriyaForEditModeIssueLogSheet() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct gps_service_ref_no from public.nt_m_gami_permit_hol_service_info WHERE gps_service_ref_no is not null and gps_service_ref_no !='' and gps_status='A' and (gps_renewal_status not in ('I') or gps_renewal_status is null)   and  (gps_service_agreement_issuedby is not NULL and gps_permit_sticker_issuedby IS not NULL and  gps_issue_logsheets_issuedby is not null) order by gps_service_ref_no desc ;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			SisuSeriyaDTO s;

			while (rs.next()) {
				s = new SisuSeriyaDTO();
				s.setServiceRefNo(rs.getString("gps_service_ref_no"));
				data.add(s);
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
	public List<SisuSeriyaDTO> getApprovedServiceRefNoListGamiSeriyaLogSheet() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct gps_service_ref_no from public.nt_m_gami_permit_hol_service_info WHERE gps_service_ref_no is not null and gps_service_ref_no !='' and gps_status='A' and (gps_renewal_status not in ('I','P') or gps_renewal_status is null)   and  (gps_service_agreement_issuedby is not NULL and gps_permit_sticker_issuedby IS not NULL and  gps_issue_logsheets_issuedby is not null) order by gps_service_ref_no desc ;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			SisuSeriyaDTO s;

			while (rs.next()) {
				s = new SisuSeriyaDTO();
				s.setServiceRefNo(rs.getString("gps_service_ref_no"));
				data.add(s);
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
	public List<SisuSeriyaDTO> getApprovedServiceRefNoListGamiSeriyaForEditModeIssueLogSheet(SisuSeriyaDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		String WHERE_SQL = "";

		if (dto.getNameOfOperator() != null && !dto.getNameOfOperator().equals("")) {
			WHERE_SQL = WHERE_SQL + " AND gps_operator_name = '" + dto.getNameOfOperator() + "' ";
		}
		if (dto.getBusRegNo() != null && !dto.getBusRegNo().equals("")) {
			WHERE_SQL = WHERE_SQL + " AND gps_bus_no = '" + dto.getBusRegNo() + "' ";
		}
		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct gps_service_ref_no from public.nt_m_gami_permit_hol_service_info WHERE gps_service_ref_no is not null and "
					+ " gps_service_ref_no !='' and gps_status='A' and (gps_renewal_status not in ('I') or gps_renewal_status is null)   and  "
					+ " (gps_service_agreement_issuedby is not NULL and gps_permit_sticker_issuedby IS not NULL and  gps_issue_logsheets_issuedby is not null) "
					+ WHERE_SQL + " order by gps_service_ref_no  ;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			SisuSeriyaDTO s;

			while (rs.next()) {
				s = new SisuSeriyaDTO();
				s.setServiceRefNo(rs.getString("gps_service_ref_no"));
				data.add(s);
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
	public List<SisuSeriyaDTO> getApprovedServiceRefNoListForEditModeNisiSeriyaForIssueLogSheet() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct nri_service_ref_no from public.nt_m_nri_requester_info WHERE nri_service_ref_no is not null and nri_service_ref_no !='' and nri_status='A' and  (nri_service_agreement_issuedby is not NULL and nri_permit_sticker_issuedby IS not NULL and  nri_issue_logsheets_issuedby is not null) order by nri_service_ref_no desc ;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			SisuSeriyaDTO s;

			while (rs.next()) {
				s = new SisuSeriyaDTO();
				s.setServiceRefNo(rs.getString("nri_service_ref_no"));
				data.add(s);
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
	public List<String> pendingReferenceNumberList(String refNum) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> data = new ArrayList<>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select sps_service_ref_no from nt_m_sisu_permit_hol_service_info where sps_renewal_status = 'P' and sps_status = 'A'"
					+ "and sps_request_no = (select sps_request_no from nt_m_sisu_permit_hol_service_info where sps_service_ref_no = ?)";

			ps = con.prepareStatement(query);
			ps.setString(1, refNum);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				data.add(rs.getString("sps_service_ref_no"));
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

	// Added dhananjika.d 16/08/2024
	@Override
	public String isRefNoHasExpired(String refNum) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String result = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {

			con = ConnectionManager.getConnection();

			String query = "select sps_renewal_new_expire_date from public.nt_m_sisu_permit_hol_service_info where sps_service_ref_no = ?";

			ps = con.prepareStatement(query);
			ps.setString(1, refNum);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				Timestamp expireDate = rs.getTimestamp("sps_renewal_new_expire_date");

				 if (expireDate != null && (expireDate.equals(timestamp) || timestamp.after(expireDate))) {
		               result = "This reference number has expired on " + sdf.format(expireDate);
		         }
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return result;
	}
	
	@Override
	public List<SisuSeriyaDTO> getApprovedServiceRefNoListForEditModeNisiSeriyaForIssueLogSheet(SisuSeriyaDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		String WHERE_SQL = "";

		if (dto.getNameOfOperator() != null && !dto.getNameOfOperator().equals("")) {
			WHERE_SQL = WHERE_SQL + " AND nri_operator_name = '" + dto.getNameOfOperator() + "' ";
		}
		if (dto.getBusRegNo() != null && !dto.getBusRegNo().equals("")) {
			WHERE_SQL = WHERE_SQL + " AND nri_bus_no = '" + dto.getBusRegNo() + "' ";
		}
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct nri_service_ref_no from public.nt_m_nri_requester_info WHERE nri_service_ref_no is not null and nri_service_ref_no !='' "
					+ " and nri_status='A' and  (nri_service_agreement_issuedby is not NULL and nri_permit_sticker_issuedby IS not NULL and  "
					+ " nri_issue_logsheets_issuedby is not null) " + WHERE_SQL + " order by nri_service_ref_no  ;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			SisuSeriyaDTO s;

			while (rs.next()) {
				s = new SisuSeriyaDTO();
				s.setServiceRefNo(rs.getString("nri_service_ref_no"));
				data.add(s);
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
	public List<SisuSeriyaDTO> getViaList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT  distinct srr_via FROM public.nt_m_sisu_requestor_route_info order by srr_via;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			SisuSeriyaDTO s;

			while (rs.next()) {
				s = new SisuSeriyaDTO();
				s.setVia(rs.getString("srr_via"));
				data.add(s);
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
	public List<SisuSeriyaDTO> getRouteDetailsForVia(String selectedViaValue) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT distinct  a.srr_origin, a.srr_destination, a.srr_via, a.srr_no_of_passengers, b.sri_request_no,  b.sri_full_name FROM public.nt_m_sisu_requestor_route_info a, public.nt_m_sisu_requestor_info b where a.srr_request_no=b.sri_request_no and a.srr_via like '%"
					+ selectedViaValue + "%';";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			SisuSeriyaDTO s;

			while (rs.next()) {
				s = new SisuSeriyaDTO();
				s.setOriginDes(rs.getString("srr_origin"));
				s.setDesignationDes(rs.getString("srr_destination"));
				s.setVia(rs.getString("srr_via"));
				s.setNoOfPassengers(rs.getString("srr_no_of_passengers"));
				s.setRequestNo(rs.getString("sri_request_no"));
				s.setNameOfOperator(rs.getString("sri_full_name"));
				data.add(s);
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
	public List<SisuSeriyaDTO> getVehicleNoList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT distinct sps_bus_no FROM public.nt_m_sisu_permit_hol_service_info order by sps_bus_no;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			SisuSeriyaDTO s;

			while (rs.next()) {
				s = new SisuSeriyaDTO();
				s.setBusRegNo(rs.getString("sps_bus_no"));
				data.add(s);
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
	public List<SisuSeriyaDTO> getOwnerNameList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT distinct sps_operator_name FROM public.nt_m_sisu_permit_hol_service_info order by sps_operator_name;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			SisuSeriyaDTO s;

			while (rs.next()) {
				s = new SisuSeriyaDTO();
				s.setNameOfOperator(rs.getString("sps_operator_name"));
				data.add(s);
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
	public List<SisuSeriyaDTO> getServiceNoListForSelectedBusNo(String selectedVehicleNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT distinct sps_service_no FROM public.nt_m_sisu_permit_hol_service_info where sps_bus_no=? and sps_service_no is not null order by sps_service_no;";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedVehicleNo);
			rs = ps.executeQuery();

			SisuSeriyaDTO s;

			while (rs.next()) {
				s = new SisuSeriyaDTO();
				s.setServiceAgreementNo(rs.getString("sps_service_no"));
				data.add(s);
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
	public List<SisuSeriyaDTO> getServiceNoListForSelectedOwnerName(String selectedOwnerName) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT distinct sps_service_no FROM public.nt_m_sisu_permit_hol_service_info where sps_operator_name=? and sps_service_no is not null order by sps_service_no;";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedOwnerName);
			rs = ps.executeQuery();

			SisuSeriyaDTO s;

			while (rs.next()) {
				s = new SisuSeriyaDTO();
				s.setServiceAgreementNo(rs.getString("sps_service_no"));
				data.add(s);
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
	public List<SisuSeriyaDTO> getServiceDataListWithServiceNo(String selectedVehicleNo, String selectedServiceNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT  sps_operator_name, sps_bus_no, sps_service_start_date, sps_service_end_date,  sps_status, sps_service_no FROM public.nt_m_sisu_permit_hol_service_info where sps_bus_no=? and sps_service_no=? order by sps_service_no;";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedVehicleNo);
			ps.setString(2, selectedServiceNo);
			rs = ps.executeQuery();

			SisuSeriyaDTO s;

			while (rs.next()) {
				s = new SisuSeriyaDTO();
				s.setNameOfOperator(rs.getString("sps_operator_name"));
				s.setBusRegNo(rs.getString("sps_bus_no"));
				if (rs.getTimestamp("sps_service_start_date") != null) {
					Timestamp ts = rs.getTimestamp("sps_service_start_date");
					Date date = new Date(ts.getTime());
					String format = "dd/MM/yyyy";
					SimpleDateFormat dateFormat = new SimpleDateFormat(format);
					String serviceStartDateVal = dateFormat.format(date);
					s.setServiceStartDateVal(serviceStartDateVal);
				} else {
					s.setServiceStartDateVal("N/A");
				}

				if (rs.getTimestamp("sps_service_end_date") != null) {
					Timestamp ts = rs.getTimestamp("sps_service_end_date");
					Date date = new Date(ts.getTime());
					String format = "dd/MM/yyyy";
					SimpleDateFormat dateFormat = new SimpleDateFormat(format);
					String serviceEndDateVal = dateFormat.format(date);
					s.setServiceEndDateVal(serviceEndDateVal);
				} else {
					s.setServiceEndDateVal("N/A");
				}

				s.setStatusCode(rs.getString("sps_status"));
				if (s.getStatusCode() != null && !s.getStatusCode().isEmpty()
						&& !s.getStatusCode().equalsIgnoreCase("")) {
					if (s.getStatusCode().equals("A")) {
						s.setStatusDes("ACTIVE");
					} else if (s.getStatusCode().equals("O")) {
						s.setStatusDes("ONGOING");
					} else if (s.getStatusCode().equals("P")) {
						s.setStatusDes("PENDING");
					} else if (s.getStatusCode().equals("I")) {
						s.setStatusDes("INACTIVE");
					} else {
						s.setStatusDes("N/A");
					}
				} else {
					s.setStatusDes("N/A");
				}

				s.setServiceAgreementNo(rs.getString("sps_service_no"));

				data.add(s);
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
	public List<SisuSeriyaDTO> getServiceDataListWithBusNo(String selectedVehicleNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT  sps_operator_name, sps_bus_no, sps_service_start_date, sps_service_end_date,  sps_status, sps_service_no FROM public.nt_m_sisu_permit_hol_service_info where sps_bus_no=? order by sps_service_no;";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedVehicleNo);
			rs = ps.executeQuery();

			SisuSeriyaDTO s;

			while (rs.next()) {
				s = new SisuSeriyaDTO();
				s.setNameOfOperator(rs.getString("sps_operator_name"));
				s.setBusRegNo(rs.getString("sps_bus_no"));
				if (rs.getTimestamp("sps_service_start_date") != null) {
					Timestamp ts = rs.getTimestamp("sps_service_start_date");
					Date date = new Date(ts.getTime());
					String format = "dd/MM/yyyy";
					SimpleDateFormat dateFormat = new SimpleDateFormat(format);
					String serviceStartDateVal = dateFormat.format(date);
					s.setServiceStartDateVal(serviceStartDateVal);
				} else {
					s.setServiceStartDateVal("N/A");
				}

				if (rs.getTimestamp("sps_service_end_date") != null) {
					Timestamp ts = rs.getTimestamp("sps_service_end_date");
					Date date = new Date(ts.getTime());
					String format = "dd/MM/yyyy";
					SimpleDateFormat dateFormat = new SimpleDateFormat(format);
					String serviceEndDateVal = dateFormat.format(date);
					s.setServiceEndDateVal(serviceEndDateVal);
				} else {
					s.setServiceEndDateVal("N/A");
				}

				s.setStatusCode(rs.getString("sps_status"));
				if (s.getStatusCode() != null && !s.getStatusCode().isEmpty()
						&& !s.getStatusCode().equalsIgnoreCase("")) {
					if (s.getStatusCode().equals("A")) {
						s.setStatusDes("ACTIVE");
					} else if (s.getStatusCode().equals("O")) {
						s.setStatusDes("ONGOING");
					} else if (s.getStatusCode().equals("P")) {
						s.setStatusDes("PENDING");
					} else if (s.getStatusCode().equals("I")) {
						s.setStatusDes("INACTIVE");
					} else {
						s.setStatusDes("N/A");
					}
				} else {
					s.setStatusDes("N/A");
				}

				s.setServiceAgreementNo(rs.getString("sps_service_no"));

				data.add(s);
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
	public List<SisuSeriyaDTO> getServiceDataListWithServiceNoAndOwnerName(String selectedOwnerName,
			String selectedServiceNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT  sps_operator_name, sps_bus_no, sps_service_start_date, sps_service_end_date,  sps_status, sps_service_no FROM public.nt_m_sisu_permit_hol_service_info where sps_operator_name=? and sps_service_no=? order by sps_service_no;";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedOwnerName);
			ps.setString(2, selectedServiceNo);
			rs = ps.executeQuery();

			SisuSeriyaDTO s;

			while (rs.next()) {
				s = new SisuSeriyaDTO();
				s.setNameOfOperator(rs.getString("sps_operator_name"));
				s.setBusRegNo(rs.getString("sps_bus_no"));
				if (rs.getTimestamp("sps_service_start_date") != null) {
					Timestamp ts = rs.getTimestamp("sps_service_start_date");
					Date date = new Date(ts.getTime());
					String format = "dd/MM/yyyy";
					SimpleDateFormat dateFormat = new SimpleDateFormat(format);
					String serviceStartDateVal = dateFormat.format(date);
					s.setServiceStartDateVal(serviceStartDateVal);
				} else {
					s.setServiceStartDateVal("N/A");
				}

				if (rs.getTimestamp("sps_service_end_date") != null) {
					Timestamp ts = rs.getTimestamp("sps_service_end_date");
					Date date = new Date(ts.getTime());
					String format = "dd/MM/yyyy";
					SimpleDateFormat dateFormat = new SimpleDateFormat(format);
					String serviceEndDateVal = dateFormat.format(date);
					s.setServiceEndDateVal(serviceEndDateVal);
				} else {
					s.setServiceEndDateVal("N/A");
				}

				s.setStatusCode(rs.getString("sps_status"));
				if (s.getStatusCode() != null && !s.getStatusCode().isEmpty()
						&& !s.getStatusCode().equalsIgnoreCase("")) {
					if (s.getStatusCode().equals("A")) {
						s.setStatusDes("ACTIVE");
					} else if (s.getStatusCode().equals("O")) {
						s.setStatusDes("ONGOING");
					} else if (s.getStatusCode().equals("P")) {
						s.setStatusDes("PENDING");
					} else if (s.getStatusCode().equals("I")) {
						s.setStatusDes("INACTIVE");
					} else {
						s.setStatusDes("N/A");
					}
				} else {
					s.setStatusDes("N/A");
				}

				s.setServiceAgreementNo(rs.getString("sps_service_no"));

				data.add(s);
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
	public List<SisuSeriyaDTO> getServiceDataListWithOwnerName(String selectedOwnerName) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT  sps_operator_name, sps_bus_no, sps_service_start_date, sps_service_end_date,  sps_status, sps_service_no FROM public.nt_m_sisu_permit_hol_service_info where sps_operator_name=?  order by sps_service_no;";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedOwnerName);

			rs = ps.executeQuery();

			SisuSeriyaDTO s;

			while (rs.next()) {
				s = new SisuSeriyaDTO();
				s.setNameOfOperator(rs.getString("sps_operator_name"));
				s.setBusRegNo(rs.getString("sps_bus_no"));
				if (rs.getTimestamp("sps_service_start_date") != null) {
					Timestamp ts = rs.getTimestamp("sps_service_start_date");
					Date date = new Date(ts.getTime());
					String format = "dd/MM/yyyy";
					SimpleDateFormat dateFormat = new SimpleDateFormat(format);
					String serviceStartDateVal = dateFormat.format(date);
					s.setServiceStartDateVal(serviceStartDateVal);
				} else {
					s.setServiceStartDateVal("N/A");
				}

				if (rs.getTimestamp("sps_service_end_date") != null) {
					Timestamp ts = rs.getTimestamp("sps_service_end_date");
					Date date = new Date(ts.getTime());
					String format = "dd/MM/yyyy";
					SimpleDateFormat dateFormat = new SimpleDateFormat(format);
					String serviceEndDateVal = dateFormat.format(date);
					s.setServiceEndDateVal(serviceEndDateVal);
				} else {
					s.setServiceEndDateVal("N/A");
				}

				s.setStatusCode(rs.getString("sps_status"));
				if (s.getStatusCode() != null && !s.getStatusCode().isEmpty()
						&& !s.getStatusCode().equalsIgnoreCase("")) {
					if (s.getStatusCode().equals("A")) {
						s.setStatusDes("ACTIVE");
					} else if (s.getStatusCode().equals("O")) {
						s.setStatusDes("ONGOING");
					} else if (s.getStatusCode().equals("P")) {
						s.setStatusDes("PENDING");
					} else if (s.getStatusCode().equals("I")) {
						s.setStatusDes("INACTIVE");
					} else {
						s.setStatusDes("N/A");
					}
				} else {
					s.setStatusDes("N/A");
				}

				s.setServiceAgreementNo(rs.getString("sps_service_no"));

				data.add(s);
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
	public boolean updateServiceInformationByServiceRefNoEdit(SisuSeriyaDTO sisuSeriyaDTO, String user) {
		Connection con = null;
		PreparedStatement stmt = null, stmt1 = null, stmt2 = null;
		java.util.Date date = new java.util.Date();
		Timestamp timeStamp = new Timestamp(date.getTime());
		String strDistance = null;

		try {
			con = ConnectionManager.getConnection();

			String sql1 = "INSERT into public.nt_h_sisu_permit_hol_service_info  "
					+ " (SELECT * FROM public.nt_m_sisu_permit_hol_service_info WHERE sps_service_ref_no = ?) ";

			stmt1 = con.prepareStatement(sql1);
			stmt1.setString(1, sisuSeriyaDTO.getServiceRefNo());
			stmt1.executeUpdate();

			// if status is "R" save the status as null
			String resetStatusQuery = "";
			if (sisuSeriyaDTO.getStatusCode().equalsIgnoreCase("R")) {
				resetStatusQuery = ", sps_status='P', sps_is_checked=null, sps_checked_by=null, sps_checked_date=null, sps_is_recommended=null, sps_recommended_by=null, sps_recommended_date=null ";
			}

			String sql = "UPDATE public.nt_m_sisu_permit_hol_service_info SET \r\n"
					+ "sps_perfered_lan=?,sps_service_type_code=?,\r\n" + "sps_bus_no=?,sps_permit_no=?,\r\n"
					+ "sps_origin=?,sps_distance=?,\r\n" + "sps_destination=?,sps_tips_per_day=?,\r\n"
					+ "sps_via=?,sps_service_start_date=?,\r\n" + "sps_nic_no=?,sps_service_end_date=?,\r\n"
					+ "sps_operator_name=?,sps_operator_name_si=?,sps_operator_name_ta=?,\r\n"
					+ "sps_add1=?,sps_add2=?,sps_city=?,\r\n" + "sps_add1_si=?,sps_add2_si=?,sps_city_si=?,\r\n"
					+ "sps_add1_ta=?,sps_add2_ta=?,sps_city_ta=?,\r\n" + "sps_province=?,\r\n"
					+ "sps_telephone_no=?,sps_district_code=?,\r\n"
					+ "sps_mobile_no=?,sps_dev_sec_code=?,sps_renewal_new_expire_date=?,sps_renewal_status = ?,is_sltb=?, sps_modified_by = ? ,sps_modified_date=?\r\n"
					+ resetStatusQuery + "WHERE sps_service_ref_no=?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, sisuSeriyaDTO.getLanguageCode());
			stmt.setString(2, sisuSeriyaDTO.getServiceTypeCode());
			stmt.setString(3, sisuSeriyaDTO.getBusRegNo());
			stmt.setString(4, sisuSeriyaDTO.getPermitNo());
			stmt.setString(5, sisuSeriyaDTO.getOriginCode());

			strDistance = String.valueOf(sisuSeriyaDTO.getDistance());
			stmt.setString(6, strDistance);

			stmt.setString(7, sisuSeriyaDTO.getDestinationCode());
			stmt.setInt(8, sisuSeriyaDTO.getTripsPerDay());
			stmt.setString(9, sisuSeriyaDTO.getVia());

			Timestamp serviceStartDate = null;
			if (sisuSeriyaDTO.getServiceStartDateObj() != null) {
				serviceStartDate = new Timestamp(sisuSeriyaDTO.getServiceStartDateObj().getTime());
			}

			stmt.setTimestamp(10, serviceStartDate);
			stmt.setString(11, sisuSeriyaDTO.getNicNo());

			Timestamp serviceEndDate = null;
			if (sisuSeriyaDTO.getServiceEndDateObj() != null) {
				serviceEndDate = new Timestamp(sisuSeriyaDTO.getServiceEndDateObj().getTime());
			}

			stmt.setTimestamp(12, serviceEndDate);

			stmt.setString(13, sisuSeriyaDTO.getNameOfOperator());
			stmt.setString(14, sisuSeriyaDTO.getNameOfOperatorSin());
			stmt.setString(15, sisuSeriyaDTO.getNameOfOperatorTamil());
			stmt.setString(16, sisuSeriyaDTO.getAddressOne());
			stmt.setString(17, sisuSeriyaDTO.getAddressTwo());
			stmt.setString(18, sisuSeriyaDTO.getCity());
			stmt.setString(19, sisuSeriyaDTO.getAddressOneSin());
			stmt.setString(20, sisuSeriyaDTO.getAdressTwoSin());
			stmt.setString(21, sisuSeriyaDTO.getCitySin());
			stmt.setString(22, sisuSeriyaDTO.getAddressOneTamil());
			stmt.setString(23, sisuSeriyaDTO.getAdressTwoTamil());
			stmt.setString(24, sisuSeriyaDTO.getCityTamil());

			stmt.setString(25, sisuSeriyaDTO.getProvinceCode());
			stmt.setString(26, sisuSeriyaDTO.getTelNo());
			stmt.setString(27, sisuSeriyaDTO.getDistrictCode());
			stmt.setString(28, sisuSeriyaDTO.getMobileNo());
			stmt.setString(29, sisuSeriyaDTO.getDivisionalSecCode());

			Timestamp serviceExpireDate = null;
			if (sisuSeriyaDTO.getServiceExpireDate() != null) {
				serviceExpireDate = new Timestamp(sisuSeriyaDTO.getServiceExpireDate().getTime());
			}

			stmt.setTimestamp(30, serviceExpireDate);

			stmt.setString(31, sisuSeriyaDTO.getServiceRenewalStatus());
			stmt.setString(32, sisuSeriyaDTO.getSltbStatus());
			stmt.setString(33, user);
			stmt.setTimestamp(34, timeStamp);
			stmt.setString(35, sisuSeriyaDTO.getServiceRefNo());

			int i = stmt.executeUpdate();

			if (i > 0) {
				/* Task Update */
				long seqNo = getSeqNoFromTaskDet(con, sisuSeriyaDTO.getRequestNo(), sisuSeriyaDTO.getServiceRefNo(),
						sisuSeriyaDTO.getServiceNo());

				String sql2 = "INSERT INTO public.nt_h_subsidy_task_his \r\n"
						+ "(tsd_seq, tsd_request_no, tsd_reference_no, tsd_service_no, tsd_task_code, tsd_status, created_by, created_date)\r\n"
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ? ); \r\n" + ";";

				stmt2 = con.prepareStatement(sql2);

				stmt2.setLong(1, seqNo);
				stmt2.setString(2, sisuSeriyaDTO.getRequestNo());
				stmt2.setString(3, sisuSeriyaDTO.getServiceRefNo());
				stmt2.setString(4, sisuSeriyaDTO.getServiceNo());
				stmt2.setString(5, "SS009");
				stmt2.setString(6, "C");
				stmt2.setString(7, user);
				stmt2.setTimestamp(8, timeStamp);

				stmt2.executeUpdate();

			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			ConnectionManager.close(stmt2);
			ConnectionManager.close(stmt1);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return true;
	}

	@Override
	public List<SisuSeriyaDTO> getdropDownDataByService(String serviceType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		PreparedStatement ps2 = null;
		ResultSet rs2 = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		String query = null;
		try {

			con = ConnectionManager.getConnection();
			if (serviceType.equals("S01")) {
				query = "select  distinct sps_request_no,sps_service_ref_no,sps_service_agreement_no,sps_service_no\r\n"
						+ "from public.nt_m_sisu_permit_hol_service_info order by sps_service_agreement_no  ;";

				ps = con.prepareStatement(query);
				rs = ps.executeQuery();

				SisuSeriyaDTO s;

				while (rs.next()) {
					s = new SisuSeriyaDTO();

					s.setRequestNo(rs.getString("sps_request_no"));
					s.setServiceRefNo(rs.getString("sps_service_ref_no"));
					if (rs.getString("sps_service_no") != null && !rs.getString("sps_service_no").trim().isEmpty()) {
						s.setServiceAgreementNo(rs.getString("sps_service_no"));
					} else {
						s.setServiceAgreementNo("-");
					}
					data.add(s);
				}
			}

			if (serviceType.equals("S02")) {
				query = "select distinct gps_service_ref_no,gps_service_no,a.gri_request_no,a.gri_agreement_no\r\n"
						+ "from public.nt_m_gami_permit_hol_service_info\r\n"
						+ "inner join public.nt_m_gami_requestor_info a on a.gri_service_ref_no=public.nt_m_gami_permit_hol_service_info.gps_service_ref_no ;";

				ps1 = con.prepareStatement(query);
				rs1 = ps1.executeQuery();

				SisuSeriyaDTO s;

				while (rs1.next()) {
					s = new SisuSeriyaDTO();

					s.setRequestNo(rs1.getString("gri_request_no"));
					s.setServiceRefNo(rs1.getString("gps_service_ref_no"));
					if (rs1.getString("gri_agreement_no") != null
							&& !rs1.getString("gri_agreement_no").trim().isEmpty()) {
						s.setServiceAgreementNo(rs1.getString("gri_agreement_no"));
					}
					data.add(s);
				}
			}

			if (serviceType.equals("S03")) {
				query = "select distinct nri_request_no,nri_service_ref_no,nri_service_agreement_no\r\n"
						+ "from public.nt_m_nri_requester_info ;";

				ps2 = con.prepareStatement(query);
				rs2 = ps2.executeQuery();

				SisuSeriyaDTO s;

				while (rs2.next()) {
					s = new SisuSeriyaDTO();

					s.setRequestNo(rs2.getString("nri_request_no"));
					s.setServiceRefNo(rs2.getString("nri_service_ref_no"));
					if (rs2.getString("nri_service_agreement_no") != null
							&& !rs2.getString("nri_service_agreement_no").trim().isEmpty()) {
						s.setServiceAgreementNo(rs2.getString("nri_service_agreement_no"));
					}
					data.add(s);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(rs1);
			ConnectionManager.close(ps1);
			ConnectionManager.close(rs2);
			ConnectionManager.close(ps2);
			ConnectionManager.close(con);
		}
		return data;
	}

	@Override
	public List<SisuSeriyaDTO> getSearchedData(SisuSeriyaDTO ssDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		String query = null;
		String WHERE_SQL = null;
		boolean whereAdd = false;
		try {

			con = ConnectionManager.getConnection();
			if (ssDTO.getServiceTypeCode().equals("S01")) {
				if (ssDTO.getBusRegNo() != null && !ssDTO.getBusRegNo().trim().isEmpty()) {
					WHERE_SQL = "and sps_bus_no = " + "'" + ssDTO.getBusRegNo() + "' ";
					whereAdd = true;
				} else if (ssDTO.getNameOfOperator() != null && !ssDTO.getNameOfOperator().trim().isEmpty()) {

					WHERE_SQL = "and sps_operator_name = " + "'" + ssDTO.getNameOfOperator() + "' ";
					whereAdd = true;
				} else if (ssDTO.getRequestNo() != null && !ssDTO.getRequestNo().trim().isEmpty()) {

					WHERE_SQL = "and sps_request_no = " + "'" + ssDTO.getRequestNo() + "' ";
					whereAdd = true;

				} else if (ssDTO.getServiceRefNo() != null && !ssDTO.getServiceRefNo().trim().isEmpty()) {

					WHERE_SQL = "and sps_service_ref_no = " + "'" + ssDTO.getServiceRefNo() + "' ";
					whereAdd = true;
				}

				else if (ssDTO.getServiceAgreementNo() != null && !ssDTO.getServiceAgreementNo().trim().isEmpty()) {

					WHERE_SQL = "and sps_service_no = " + "'" + ssDTO.getServiceAgreementNo() + "' ";
					whereAdd = true;

				}
				if (whereAdd) {
					query = "	select sps_bus_no,sps_operator_name,sps_request_no,sps_service_ref_no,sps_service_agreement_no,sps_service_no,\r\n"
							+ "	sps_origin,sps_destination,sps_province,sps_status,p.description,s.ssc_school_name,sps_nic_no,sps_renewal_new_expire_date,sps_add1||sps_add2||sps_city as address\r\n"
							+ "	from public.nt_m_sisu_permit_hol_service_info\r\n"
							+ "	inner join public.nt_r_province p on p.code= public.nt_m_sisu_permit_hol_service_info.sps_province\r\n"
							+ "	inner join public.nt_m_sisu_permit_hol_school_info s on s.ssc_service_ref_no=public.nt_m_sisu_permit_hol_service_info.sps_service_ref_no\r\n"
							+ "	where p.active='A' " + WHERE_SQL + " order by sps_bus_no ";
				} else {
					query = "	select sps_bus_no,sps_operator_name,sps_request_no,sps_service_ref_no,sps_service_agreement_no,sps_service_no,\r\n"
							+ "	sps_origin,sps_destination,sps_province,sps_status,p.description,s.ssc_school_name,sps_nic_no,sps_renewal_new_expire_date,sps_add1||sps_add2||sps_city as address\r\n"
							+ "	from public.nt_m_sisu_permit_hol_service_info\r\n"
							+ "	inner join public.nt_r_province p on p.code= public.nt_m_sisu_permit_hol_service_info.sps_province\r\n"
							+ "	inner join public.nt_m_sisu_permit_hol_school_info s on s.ssc_service_ref_no=public.nt_m_sisu_permit_hol_service_info.sps_service_ref_no\r\n"
							+ "	where p.active='A';";
				}
			}
			/** For Gami Sariya **/
			if (ssDTO.getServiceTypeCode().equals("S02")) {
				if (ssDTO.getBusRegNo() != null && !ssDTO.getBusRegNo().trim().isEmpty()) {
					WHERE_SQL = "and gps_bus_no = " + "'" + ssDTO.getBusRegNo() + "' ";
					whereAdd = true;
				} else if (ssDTO.getNameOfOperator() != null && !ssDTO.getNameOfOperator().trim().isEmpty()) {

					WHERE_SQL = "and gps_operator_name = " + "'" + ssDTO.getNameOfOperator() + "' ";
					whereAdd = true;
				} else if (ssDTO.getRequestNo() != null && !ssDTO.getRequestNo().trim().isEmpty()) {

					WHERE_SQL = "and gri_request_no = " + "'" + ssDTO.getRequestNo() + "' ";
					whereAdd = true;

				} else if (ssDTO.getServiceRefNo() != null && !ssDTO.getServiceRefNo().trim().isEmpty()) {

					WHERE_SQL = "and gps_service_ref_no = " + "'" + ssDTO.getServiceRefNo() + "' ";
					whereAdd = true;
				}

				else if (ssDTO.getServiceAgreementNo() != null && !ssDTO.getServiceAgreementNo().trim().isEmpty()) {

					WHERE_SQL = "and gri_agreement_no = " + "'" + ssDTO.getServiceAgreementNo() + "' ";
					whereAdd = true;

				}
				if (whereAdd) {
					query = "select gps_bus_no as sps_bus_no,gps_operator_name as sps_operator_name,gps_service_ref_no as sps_service_ref_no,gps_service_no as sps_service_no,a.gri_request_no as sps_request_no,a.gri_agreement_no as sps_service_agreement_no\r\n"
							+ ",gps_origin as sps_origin,gps_destination as sps_destination,gps_nic_no as sps_nic_no,p.description,gps_renewal_new_expire_date as sps_renewal_new_expire_date,gps_renewal_status as sps_status,gps_add1||gps_add2||gps_city as address\r\n"
							+ "from public.nt_m_gami_permit_hol_service_info\r\n"
							+ "inner join public.nt_m_gami_requestor_info a on a.gri_service_ref_no=public.nt_m_gami_permit_hol_service_info.gps_service_ref_no\r\n"
							+ "inner join public.nt_r_province p on p.code= public.nt_m_gami_permit_hol_service_info.gps_province\r\n"
							+ "where p.active='A'" + WHERE_SQL + " order by gps_bus_no ";

				} else {
					query = "select gps_bus_no as sps_bus_no,gps_operator_name as sps_operator_name,gps_service_ref_no as sps_service_ref_no,gps_service_no as sps_service_no,a.gri_request_no as sps_request_no,a.gri_agreement_no as sps_service_agreement_no\r\n"
							+ ",gps_origin as sps_origin,gps_destination as sps_destination,gps_nic_no as sps_nic_no,p.description,gps_renewal_new_expire_date as sps_renewal_new_expire_date,gps_renewal_status as sps_status,gps_add1||gps_add2||gps_city as address\r\n"
							+ "from public.nt_m_gami_permit_hol_service_info\r\n"
							+ "inner join public.nt_m_gami_requestor_info a on a.gri_service_ref_no=public.nt_m_gami_permit_hol_service_info.gps_service_ref_no\r\n"
							+ "inner join public.nt_r_province p on p.code= public.nt_m_gami_permit_hol_service_info.gps_province\r\n"
							+ "where p.active='A'";

				}
			}
			/** Gami Sariya End **/

			/** For Nisi Sariya **/
			if (ssDTO.getServiceTypeCode().equals("S03")) {
				if (ssDTO.getBusRegNo() != null && !ssDTO.getBusRegNo().trim().isEmpty()) {
					WHERE_SQL = "and nri_bus_no = " + "'" + ssDTO.getBusRegNo() + "' ";
					whereAdd = true;
				} else if (ssDTO.getNameOfOperator() != null && !ssDTO.getNameOfOperator().trim().isEmpty()) {

					WHERE_SQL = "and nri_operator_name = " + "'" + ssDTO.getNameOfOperator() + "' ";
					whereAdd = true;
				} else if (ssDTO.getRequestNo() != null && !ssDTO.getRequestNo().trim().isEmpty()) {

					WHERE_SQL = "and nri_request_no = " + "'" + ssDTO.getRequestNo() + "' ";
					whereAdd = true;

				} else if (ssDTO.getServiceRefNo() != null && !ssDTO.getServiceRefNo().trim().isEmpty()) {

					WHERE_SQL = "and nri_service_ref_no = " + "'" + ssDTO.getServiceRefNo() + "' ";
					whereAdd = true;
				}

				else if (ssDTO.getServiceAgreementNo() != null && !ssDTO.getServiceAgreementNo().trim().isEmpty()) {

					WHERE_SQL = "and nri_service_agreement_no = " + "'" + ssDTO.getServiceAgreementNo() + "' ";
					whereAdd = true;

				}
				if (whereAdd) {

					query = "select nri_bus_no as sps_bus_no,nri_operator_name as sps_operator_name,nri_request_no as sps_request_no ,nri_service_ref_no as sps_service_ref_no, nri_service_agreement_no as sps_service_agreement_no,nri_origin as sps_origin\r\n"
							+ ",nri_destination as sps_destination,nri_nic_no as sps_nic_no ,nri_add1||nri_add2||nri_city as address,nri_province,nri_expire_date as sps_renewal_new_expire_date,nri_status as sps_status ,p.description\r\n"
							+ "from public.nt_m_nri_requester_info\r\n"
							+ "inner join public.nt_r_province p on p.code= public.nt_m_nri_requester_info.nri_province\r\n"
							+ "where p.active='A'" + WHERE_SQL + " order by nri_bus_no ";

				} else {
					query = "select nri_bus_no as sps_bus_no,nri_operator_name as sps_operator_name,nri_request_no as sps_request_no ,nri_service_ref_no as sps_service_ref_no, nri_service_agreement_no as sps_service_agreement_no,nri_origin as sps_origin\r\n"
							+ ",nri_destination as sps_destination,nri_nic_no as sps_nic_no ,nri_add1||nri_add2||nri_city as address,nri_province,nri_expire_date as sps_renewal_new_expire_date,nri_status as sps_status ,p.description\r\n"
							+ "from public.nt_m_nri_requester_info\r\n"
							+ "inner join public.nt_r_province p on p.code= public.nt_m_nri_requester_info.nri_province\r\n"
							+ "where p.active='A'";

				}
			}

			/** Nisi Sariya End **/
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			SisuSeriyaDTO s;

			while (rs.next()) {
				s = new SisuSeriyaDTO();
				if (ssDTO.getServiceTypeCode().equals("S01")) {
					s.setServiceTypeDes("Sisu Sariya");
				}
				if (ssDTO.getServiceTypeCode().equals("S02")) {
					s.setServiceTypeDes("Gami Sariya");
				}
				if (ssDTO.getServiceTypeCode().equals("S03")) {
					s.setServiceTypeDes("Nisi Sariya");
				}
				s.setNameOfOperator(rs.getString("sps_operator_name"));
				s.setBusRegNo(rs.getString("sps_bus_no"));
				s.setRequestNo(rs.getString("sps_request_no"));
				s.setServiceRefNo(rs.getString("sps_service_ref_no"));
				if (ssDTO.getServiceTypeCode().equals("S01") || ssDTO.getServiceTypeCode().equals("S02")) {
					if (rs.getString("sps_service_no") != null && !rs.getString("sps_service_no").trim().isEmpty()) {
						s.setServiceNo(rs.getString("sps_service_no"));
					}
				}
				if (rs.getString("sps_service_agreement_no") != null
						&& !rs.getString("sps_service_agreement_no").trim().isEmpty()) {
					s.setServiceAgreementNo(rs.getString("sps_service_agreement_no"));
				}
				s.setOriginDes(rs.getString("sps_origin"));
				s.setDestinationDes(rs.getString("sps_destination"));
				if (ssDTO.getServiceTypeCode().equals("S01")) {
					s.setSchoolName(rs.getString("ssc_school_name"));
				}
				s.setNicNo(rs.getString("sps_nic_no"));
				s.setAddressOne(rs.getString("address"));
				s.setProvinceDes(rs.getString("description"));

				// add expire date
				s.setExpiryDateObj(rs.getTimestamp("sps_renewal_new_expire_date"));
				if (rs.getString("sps_status") != null) {
					if (rs.getString("sps_status").equals("A")) {
						s.setStatus("ACTIVE");
					}
					if (rs.getString("sps_status").equals("O")) {
						s.setStatus("ONGOING");
					}
					if (rs.getString("sps_status").equals("P")) {
						s.setStatus("PENDING");
					}

					if (rs.getString("sps_status").equals("I")) {
						s.setStatus("INACTIVE");
					}
				}
				data.add(s);
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
	public List<SisuSeriyaDTO> getVehiNumByService(String serviceType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		PreparedStatement ps2 = null;
		ResultSet rs2 = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		String query = null;
		try {

			con = ConnectionManager.getConnection();
			if (serviceType.equals("S01")) {
				query = "select distinct sps_bus_no from public.nt_m_sisu_permit_hol_service_info where sps_bus_no is not null and sps_bus_no !='' order by sps_bus_no desc;";

				ps = con.prepareStatement(query);
				rs = ps.executeQuery();

				SisuSeriyaDTO s;

				while (rs.next()) {
					s = new SisuSeriyaDTO();

					s.setBusRegNo(rs.getString("sps_bus_no"));

					data.add(s);
				}
			}

			if (serviceType.equals("S02")) {
				query = "select distinct gps_bus_no from public.nt_m_gami_permit_hol_service_info where gps_bus_no is not null and gps_bus_no !='' order by gps_bus_no desc ;";

				ps1 = con.prepareStatement(query);
				rs1 = ps1.executeQuery();

				SisuSeriyaDTO s;

				while (rs1.next()) {
					s = new SisuSeriyaDTO();

					s.setBusRegNo(rs1.getString("gps_bus_no"));

					data.add(s);
				}
			}

			if (serviceType.equals("S03")) {
				query = "select distinct nri_bus_no from public.nt_m_nri_requester_info where nri_bus_no is not null and nri_bus_no !='' order by nri_bus_no desc;";

				ps2 = con.prepareStatement(query);
				rs2 = ps2.executeQuery();

				SisuSeriyaDTO s;

				while (rs2.next()) {
					s = new SisuSeriyaDTO();

					s.setBusRegNo(rs2.getString("nri_bus_no"));

					data.add(s);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(rs1);
			ConnectionManager.close(ps1);
			ConnectionManager.close(rs2);
			ConnectionManager.close(ps2);
			ConnectionManager.close(con);
		}
		return data;

	}

	@Override
	public List<SisuSeriyaDTO> getNamesByService(String serviceType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		PreparedStatement ps2 = null;
		ResultSet rs2 = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		String query = null;
		try {

			con = ConnectionManager.getConnection();
			if (serviceType.equals("S01")) {
				query = "select  distinct sps_operator_name from public.nt_m_sisu_permit_hol_service_info ;";

				ps = con.prepareStatement(query);
				rs = ps.executeQuery();

				SisuSeriyaDTO s;

				while (rs.next()) {
					s = new SisuSeriyaDTO();
					s.setNameOfOperator(rs.getString("sps_operator_name"));

					data.add(s);
				}
			}

			if (serviceType.equals("S02")) {
				query = "select distinct  gps_operator_name from public.nt_m_gami_permit_hol_service_info ";

				ps1 = con.prepareStatement(query);
				rs1 = ps1.executeQuery();

				SisuSeriyaDTO s;

				while (rs1.next()) {
					s = new SisuSeriyaDTO();
					s.setNameOfOperator(rs1.getString("gps_operator_name"));

					data.add(s);
				}
			}

			if (serviceType.equals("S03")) {
				query = "select distinct  nri_operator_name from public.nt_m_nri_requester_info ;";

				ps2 = con.prepareStatement(query);
				rs2 = ps2.executeQuery();

				SisuSeriyaDTO s;

				while (rs2.next()) {
					s = new SisuSeriyaDTO();
					s.setNameOfOperator(rs2.getString("nri_operator_name"));

					data.add(s);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(rs1);
			ConnectionManager.close(ps1);
			ConnectionManager.close(rs2);
			ConnectionManager.close(ps2);
			ConnectionManager.close(con);
		}
		return data;
	}

	@Override
	public List<SisuSeriyaDTO> getReqNoByService(String serviceType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		PreparedStatement ps2 = null;
		ResultSet rs2 = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		String query = null;
		try {

			con = ConnectionManager.getConnection();
			if (serviceType.equals("S01")) {
				query = "select  distinct sps_request_no from public.nt_m_sisu_permit_hol_service_info  where sps_request_no is not null order by sps_request_no desc  ;";

				ps = con.prepareStatement(query);
				rs = ps.executeQuery();

				SisuSeriyaDTO s;

				while (rs.next()) {
					s = new SisuSeriyaDTO();

					s.setRequestNo(rs.getString("sps_request_no"));

					data.add(s);
				}
			}

			if (serviceType.equals("S02")) {
				query = "select distinct gri_request_no from public.nt_m_gami_requestor_info where gri_request_no is not null order by gri_request_no desc ;";

				ps1 = con.prepareStatement(query);
				rs1 = ps1.executeQuery();

				SisuSeriyaDTO s;

				while (rs1.next()) {
					s = new SisuSeriyaDTO();

					s.setRequestNo(rs1.getString("gri_request_no"));

					data.add(s);
				}
			}

			if (serviceType.equals("S03")) {
				query = "select distinct nri_request_no,nri_service_ref_no,nri_service_agreement_no\r\n"
						+ "from public.nt_m_nri_requester_info where nri_request_no is not null order by nri_request_no desc ;";

				ps2 = con.prepareStatement(query);
				rs2 = ps2.executeQuery();

				SisuSeriyaDTO s;

				while (rs2.next()) {
					s = new SisuSeriyaDTO();

					s.setRequestNo(rs2.getString("nri_request_no"));

					data.add(s);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(rs1);
			ConnectionManager.close(ps1);
			ConnectionManager.close(rs2);
			ConnectionManager.close(ps2);
			ConnectionManager.close(con);
		}
		return data;
	}

	@Override
	public List<SisuSeriyaDTO> getRefNoByService(String serviceType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		PreparedStatement ps2 = null;
		ResultSet rs2 = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		String query = null;
		try {

			con = ConnectionManager.getConnection();
			if (serviceType.equals("S01")) {
				query = "select  distinct sps_service_ref_no from public.nt_m_sisu_permit_hol_service_info where sps_service_ref_no is not null order by sps_service_ref_no desc  ;";

				ps = con.prepareStatement(query);
				rs = ps.executeQuery();

				SisuSeriyaDTO s;

				while (rs.next()) {
					s = new SisuSeriyaDTO();

					s.setServiceRefNo(rs.getString("sps_service_ref_no"));

					data.add(s);
				}
			}

			if (serviceType.equals("S02")) {
				query = "select distinct gps_service_ref_no from public.nt_m_gami_permit_hol_service_info where gps_service_ref_no  is not null order by gps_service_ref_no desc";

				ps1 = con.prepareStatement(query);
				rs1 = ps1.executeQuery();

				SisuSeriyaDTO s;

				while (rs1.next()) {
					s = new SisuSeriyaDTO();

					s.setServiceRefNo(rs1.getString("gps_service_ref_no"));

					data.add(s);
				}
			}

			if (serviceType.equals("S03")) {
				query = "select distinct nri_service_ref_no from public.nt_m_nri_requester_info  where nri_service_ref_no is not null order by nri_service_ref_no desc;";

				ps2 = con.prepareStatement(query);
				rs2 = ps2.executeQuery();

				SisuSeriyaDTO s;

				while (rs2.next()) {
					s = new SisuSeriyaDTO();

					s.setServiceRefNo(rs2.getString("nri_service_ref_no"));

					data.add(s);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(rs1);
			ConnectionManager.close(ps1);
			ConnectionManager.close(rs2);
			ConnectionManager.close(ps2);
			ConnectionManager.close(con);
		}
		return data;
	}

	@Override
	public List<SisuSeriyaDTO> getAgreeNoByService(String serviceType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		PreparedStatement ps2 = null;
		ResultSet rs2 = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		String query = null;
		try {

			con = ConnectionManager.getConnection();
			if (serviceType.equals("S01")) {
				query = "select  distinct sps_service_agreement_no,sps_service_no\r\n"
						+ "from public.nt_m_sisu_permit_hol_service_info where sps_service_no is not null  order by sps_service_no desc  ;";

				ps = con.prepareStatement(query);
				rs = ps.executeQuery();

				SisuSeriyaDTO s;

				while (rs.next()) {
					s = new SisuSeriyaDTO();

					if (rs.getString("sps_service_no") != null && !rs.getString("sps_service_no").trim().isEmpty()) {
						s.setServiceAgreementNo(rs.getString("sps_service_no"));
					} else {
						s.setServiceAgreementNo("-");
					}
					data.add(s);
				}
			}

			if (serviceType.equals("S02")) {
				query = "select distinct gri_request_no,gri_agreement_no\r\n"
						+ "from public.nt_m_gami_requestor_info where gri_agreement_no is not null order by gri_agreement_no desc \r\n";

				ps1 = con.prepareStatement(query);
				rs1 = ps1.executeQuery();

				SisuSeriyaDTO s;

				while (rs1.next()) {
					s = new SisuSeriyaDTO();

					if (rs1.getString("gri_agreement_no") != null
							&& !rs1.getString("gri_agreement_no").trim().isEmpty()) {
						s.setServiceAgreementNo(rs1.getString("gri_agreement_no"));
					}
					data.add(s);
				}
			}

			if (serviceType.equals("S03")) {
				query = "select distinct nri_service_agreement_no\r\n"
						+ "from public.nt_m_nri_requester_info where nri_service_agreement_no is not null order by nri_service_agreement_no desc ;";

				ps2 = con.prepareStatement(query);
				rs2 = ps2.executeQuery();

				SisuSeriyaDTO s;

				while (rs2.next()) {
					s = new SisuSeriyaDTO();

					if (rs2.getString("nri_service_agreement_no") != null
							&& !rs2.getString("nri_service_agreement_no").trim().isEmpty()) {
						s.setServiceAgreementNo(rs2.getString("nri_service_agreement_no"));
					}
					data.add(s);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(rs1);
			ConnectionManager.close(ps1);
			ConnectionManager.close(rs2);
			ConnectionManager.close(ps2);
			ConnectionManager.close(con);
		}
		return data;
	}

	@Override
	public boolean updateSchoolInformationByServiceRefNoNew(SisuSeriyaDTO sisuSeriyaDTO, String user) {
		Connection con = null;
		PreparedStatement stmt = null, stmt1 = null, stmt2 = null;
		java.util.Date date = new java.util.Date();
		boolean isModelSave = false;
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql1 = "INSERT into public.nt_h_sisu_permit_hol_school_info  "
					+ " (SELECT * FROM public.nt_m_sisu_permit_hol_school_info WHERE ssc_service_ref_no = ?) ";

			stmt1 = con.prepareStatement(sql1);
			stmt1.setString(1, sisuSeriyaDTO.getServiceRefNo());
			stmt1.executeUpdate();

			String sql = "UPDATE public.nt_m_sisu_permit_hol_school_info\r\n"
					+ "SET ssc_school_name=?, ssc_school_name_si=?, ssc_school_name_ta=?, \r\n"
					+ "ssc_school_add1=?, ssc_school_add1_si=?, ssc_school_add1_ta=?, \r\n"
					+ "ssc_school_add2=?, ssc_school_add2_si=?, ssc_school_add2_ta=?,\r\n"
					+ "ssc_school_city=?, ssc_school_city_si=?, ssc_school_city_ta=?, \r\n"
					+ "ssc_school_province_code=?, ssc_school_district_code=?, ssc_school_div_sec_code=?, \r\n"
					+ "ssc_school_tel_no=?, ssc_school_mobile=?, ssc_school_email=?, ssc_modified_by =?, ssc_modified_date = ? \r\n"
					+ "WHERE ssc_service_ref_no=?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, sisuSeriyaDTO.getSchoolName());
			stmt.setString(2, sisuSeriyaDTO.getSchoolNameSin());
			stmt.setString(3, sisuSeriyaDTO.getSchoolNameTamil());
			stmt.setString(4, sisuSeriyaDTO.getSchoolAddressOne());
			stmt.setString(5, sisuSeriyaDTO.getSchoolAdrressOneSin());
			stmt.setString(6, sisuSeriyaDTO.getSchoolAddressOneTamil());
			stmt.setString(7, sisuSeriyaDTO.getSchoolAddressTwo());
			stmt.setString(8, sisuSeriyaDTO.getSchoolAddressTwoSin());
			stmt.setString(9, sisuSeriyaDTO.getSchoolAddressTwoTamil());
			stmt.setString(10, sisuSeriyaDTO.getSchoolCity());
			stmt.setString(11, sisuSeriyaDTO.getSchoolCitySin());
			stmt.setString(12, sisuSeriyaDTO.getSchoolCityTamil());
			stmt.setString(13, sisuSeriyaDTO.getSchoolProvinceCode());
			stmt.setString(14, sisuSeriyaDTO.getSchoolDistrictCode());
			stmt.setString(15, sisuSeriyaDTO.getSchoolDivisinSecCode());
			stmt.setString(16, sisuSeriyaDTO.getSchoolTelNo());
			stmt.setString(17, sisuSeriyaDTO.getSchoolMobileNo());
			stmt.setString(18, sisuSeriyaDTO.getSchoolEmailAdd());
			stmt.setString(19, user);
			stmt.setTimestamp(20, timestamp);
			stmt.setString(21, sisuSeriyaDTO.getServiceRefNo());

			int i = stmt.executeUpdate();

			if (i > 0) {
				isModelSave = true;
				/* Task Update */
				long seqNo = getSeqNoFromTaskDet(con, sisuSeriyaDTO.getRequestNo(), sisuSeriyaDTO.getServiceRefNo(),
						sisuSeriyaDTO.getServiceNo());

				String sql2 = "INSERT INTO public.nt_h_subsidy_task_his \r\n"
						+ "(tsd_seq, tsd_request_no, tsd_reference_no, tsd_service_no, tsd_task_code, tsd_status, created_by, created_date)\r\n"
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ? ); \r\n" + ";";

				stmt2 = con.prepareStatement(sql2);

				stmt2.setLong(1, seqNo);
				stmt2.setString(2, sisuSeriyaDTO.getRequestNo());
				stmt2.setString(3, sisuSeriyaDTO.getServiceRefNo());
				stmt2.setString(4, sisuSeriyaDTO.getServiceNo());
				stmt2.setString(5, "SS010");
				stmt2.setString(6, "C");
				stmt2.setString(7, user);
				stmt2.setTimestamp(8, timestamp);

				stmt2.executeUpdate();

			} else {
				isModelSave = false;
			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			ConnectionManager.close(stmt2);
			ConnectionManager.close(stmt1);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return isModelSave;
	}

	@Override
	public boolean insertBankDetailsNew(SisuSeriyaDTO sisuSeriyaDTO, String loginUser) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null, stmt1 = null, stmt2 = null;
		boolean isModelSave = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			// insert current record in to history table
			String sql1 = "INSERT into public.nt_h_sisu_permit_hol_service_info  "
					+ " (SELECT * FROM public.nt_m_sisu_permit_hol_service_info WHERE sps_service_ref_no = ?) ";

			stmt1 = con.prepareStatement(sql1);
			stmt1.setString(1, sisuSeriyaDTO.getServiceRefNo());
			stmt1.executeUpdate();

			String sql = "UPDATE public.nt_m_sisu_permit_hol_service_info \r\n"
					+ "SET  sps_account_no=?, sps_bank_name_code=?, sps_branch_code=?,sps_modified_by=?, sps_modified_date=? \r\n"
					+ "WHERE sps_service_ref_no= ? ;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, sisuSeriyaDTO.getAccountNo());
			stmt.setString(2, sisuSeriyaDTO.getBankNameCode());
			stmt.setString(3, sisuSeriyaDTO.getBankBranchNameCode());
			stmt.setString(4, loginUser);
			stmt.setTimestamp(5, timestamp);
			stmt.setString(6, sisuSeriyaDTO.getServiceRefNo());

			int i = stmt.executeUpdate();

			if (i > 0) {
				isModelSave = true;
				/* Task Update */
				long seqNo = getSeqNoFromTaskDet(con, sisuSeriyaDTO.getRequestNo(), sisuSeriyaDTO.getServiceRefNo(),
						sisuSeriyaDTO.getServiceNo());

				String sql2 = "INSERT INTO public.nt_h_subsidy_task_his \r\n"
						+ "(tsd_seq, tsd_request_no, tsd_reference_no, tsd_service_no, tsd_task_code, tsd_status, created_by, created_date)\r\n"
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ? ); \r\n" + ";";

				stmt2 = con.prepareStatement(sql2);

				stmt2.setLong(1, seqNo);
				stmt2.setString(2, sisuSeriyaDTO.getRequestNo());
				stmt2.setString(3, sisuSeriyaDTO.getServiceRefNo());
				stmt2.setString(4, sisuSeriyaDTO.getServiceNo());
				stmt2.setString(5, "SS011");
				stmt2.setString(6, "C");
				stmt2.setString(7, loginUser);
				stmt2.setTimestamp(8, timestamp);

				stmt2.executeUpdate();
				/**/
			} else {
				isModelSave = false;
			}
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt1);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(con);

		}

		return isModelSave;
	}

	@Override
	public SisuSeriyaDTO retrieveSchoolInfoFromRequestNew(SisuSeriyaDTO sisuSeriyaDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  ssc_school_name, ssc_school_name_si, ssc_school_name_ta,ssc_school_add1,ssc_school_add1_si, ssc_school_add1_ta,"
					+ "ssc_school_add2, ssc_school_add2_si, ssc_school_add2_ta, ssc_school_city, ssc_school_city_si, ssc_school_city_ta,"
					+ "ssc_school_province_code, ssc_school_district_code, ssc_school_div_sec_code, ssc_school_tel_no, ssc_school_mobile, ssc_school_email"
					+ " FROM public.nt_m_sisu_permit_hol_school_info " + " WHERE ssc_service_ref_no = ?";

			ps = con.prepareStatement(query);
			ps.setString(1, sisuSeriyaDTO.getServiceRefNo());
			rs = ps.executeQuery();

			while (rs.next()) {

				sisuSeriyaDTO.setSchoolName(rs.getString("ssc_school_name"));
				sisuSeriyaDTO.setSchoolNameSin(rs.getString("ssc_school_name_si"));
				sisuSeriyaDTO.setSchoolNameTamil(rs.getString("ssc_school_name_ta"));
				sisuSeriyaDTO.setSchoolAddressOne(rs.getString("ssc_school_add1"));
				sisuSeriyaDTO.setSchoolAdrressOneSin(rs.getString("ssc_school_add1_si"));
				sisuSeriyaDTO.setSchoolAddressOneTamil(rs.getString("ssc_school_add1_ta"));
				sisuSeriyaDTO.setSchoolAddressTwo(rs.getString("ssc_school_add2"));
				sisuSeriyaDTO.setSchoolAddressTwoSin(rs.getString("ssc_school_add2_si"));
				sisuSeriyaDTO.setSchoolAddressTwoTamil(rs.getString("ssc_school_add2_ta"));
				sisuSeriyaDTO.setSchoolCity(rs.getString("ssc_school_city"));
				sisuSeriyaDTO.setSchoolCitySin(rs.getString("ssc_school_city_si"));
				sisuSeriyaDTO.setSchoolCityTamil(rs.getString("ssc_school_city_ta"));
				sisuSeriyaDTO.setSchoolProvinceCode(rs.getString("ssc_school_province_code"));
				sisuSeriyaDTO.setSchoolDistrictCode(rs.getString("ssc_school_district_code"));
				sisuSeriyaDTO.setSchoolDivisinSecCode(rs.getString("ssc_school_div_sec_code"));
				sisuSeriyaDTO.setSchoolTelNo(rs.getString("ssc_school_tel_no"));
				sisuSeriyaDTO.setSchoolMobileNo(rs.getString("ssc_school_mobile"));
				sisuSeriyaDTO.setSchoolEmailAdd(rs.getString("ssc_school_email"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return sisuSeriyaDTO;
	}

	@Override
	public List<SisuSeriyaDTO> getRequesNoDropDownNew() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SisuSeriyaDTO> requestNoList = new ArrayList<SisuSeriyaDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct sri_request_no "
					+ " FROM public.nt_m_sisu_requestor_info INNER JOIN nt_m_sisu_permit_hol_service_info "
					+ " ON nt_m_sisu_permit_hol_service_info.sps_request_no = nt_m_sisu_requestor_info.sri_request_no "
					+ " INNER JOIN nt_h_subsidy_task_his ON nt_m_sisu_permit_hol_service_info.sps_service_ref_no = nt_h_subsidy_task_his.tsd_reference_no "
					+ " WHERE sps_service_ref_no is not null " + " AND sps_service_ref_no != ' ' "
					+ " AND tsd_task_code ='SS004' " + " ORDER BY sri_request_no desc;";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				SisuSeriyaDTO ss = new SisuSeriyaDTO();

				ss.setRequestNo(rs.getString("sri_request_no"));
				requestNoList.add(ss);

			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return requestNoList;

	}

	@Override
	public List<SisuSeriyaDTO> drpdRequestNoListNew(SisuSeriyaDTO sisuSeriyaDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SisuSeriyaDTO> drpdRequestNoList = new ArrayList<SisuSeriyaDTO>();

		try {

			String whereClose = "";
			if (sisuSeriyaDTO.getRequestStartDateString() != null
					&& !sisuSeriyaDTO.getRequestStartDateString().isEmpty()
					&& !sisuSeriyaDTO.getRequestStartDateString().equalsIgnoreCase("")) {

				if (sisuSeriyaDTO.getRequestEndDateString() != null
						&& !sisuSeriyaDTO.getRequestStartDateString().isEmpty()
						&& !sisuSeriyaDTO.getRequestStartDateString().equalsIgnoreCase("")) {
					whereClose = " where sri_request_date >= '" + sisuSeriyaDTO.getRequestStartDateString()
							+ "' and sri_request_date <= '" + sisuSeriyaDTO.getRequestEndDateString() + "' ";
				} else {

					whereClose = " where sri_request_date >= '" + sisuSeriyaDTO.getRequestStartDateString() + "' ";
				}

			} else {
				whereClose = " where sri_request_date <= '" + sisuSeriyaDTO.getRequestEndDateString() + "' ";
			}
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct sri_request_no FROM public.nt_m_sisu_requestor_info inner join nt_m_sisu_permit_hol_service_info "
					+ " on nt_m_sisu_permit_hol_service_info.sps_request_no = nt_m_sisu_requestor_info.sri_request_no "
					+ " inner join nt_h_subsidy_task_his on nt_m_sisu_permit_hol_service_info.sps_service_ref_no = nt_h_subsidy_task_his.tsd_reference_no"
					+ whereClose
					+ " and sri_request_no is not null and sri_request_no !='' and tsd_task_code ='SS004' order by sri_request_no";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				SisuSeriyaDTO ss = new SisuSeriyaDTO();

				ss.setRequestNo(rs.getString("sri_request_no"));
				drpdRequestNoList.add(ss);

			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return drpdRequestNoList;
	}

	@Override
	public List<SisuSeriyaDTO> getServiceHistoryDeatils(SisuSeriyaDTO sisuSeriyaDTO) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SisuSeriyaDTO> list = new ArrayList<SisuSeriyaDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT  * FROM public.nt_h_sisu_permit_hol_service_info where sps_service_no=? and sps_service_ref_no!=? order by sps_created_date;";

			SisuSeriyaDTO s;
			ps = con.prepareStatement(query2);
			ps.setString(1, sisuSeriyaDTO.getServiceNo());
			ps.setString(2, sisuSeriyaDTO.getServiceRefNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				s = new SisuSeriyaDTO();
				s.setServiceNo(rs.getString("sps_service_no"));
				s.setServiceRefNo(rs.getString("sps_service_ref_no"));
				s.setBusRegNo(rs.getString("sps_bus_no"));
				s.setPermitNo(rs.getString("sps_permit_no"));
				s.setModifiedBy(rs.getString("sps_modified_by"));
				s.setModifiedDate(rs.getTimestamp("sps_modified_date"));
				String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.format(rs.getTimestamp("sps_modified_date"));
				s.setModifiedDateString(formattedDate);
				list.add(s);
			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return list;
	}

	@Override
	public List<SisuSeriyaDTO> getSchoolHistoryDeatils(SisuSeriyaDTO sisuSeriyaDTO) {
		return null;
	}

	public List<LogSheetDTO> getLogSheetsByServiceRefNoYear(String serviceRefNo, String year) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<LogSheetDTO> list = new ArrayList<LogSheetDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select A.lss_log_ref_no, A.lss_seq from public.nt_m_log_sheet_summary A "
					+ "inner join public.nt_m_log_sheets B on A.lss_log_sheet_master_seq = B.los_seq "
					+ "where B.los_std_service_ref_no = ? and b.los_year = ? order by A.lss_log_ref_no DESC";

			ps = con.prepareStatement(query);
			ps.setString(1, serviceRefNo);
			ps.setString(2, year);
			rs = ps.executeQuery();

			while (rs.next()) {
				LogSheetDTO logSheetDTO = new LogSheetDTO();

				logSheetDTO.setLogSheetRefNo(rs.getString("lss_log_ref_no"));
				logSheetDTO.setLogSheetSeqNo(rs.getInt("lss_seq"));
				list.add(logSheetDTO);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return list;
	}

	private boolean checkTaskDetailsInSubsidyByServiceRefNo(String serviceRefNo, String taskCode, String status) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isTaskAvailable = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM public.nt_t_subsidy_task_det "
					+ "where (tsd_task_code=? and tsd_status=? and tsd_reference_no=?); ";

			ps = con.prepareStatement(query);
			ps.setString(1, taskCode);
			ps.setString(2, status);
			ps.setString(3, serviceRefNo);
			rs = ps.executeQuery();

			if (rs.next() == true) {
				isTaskAvailable = true;
			} else {
				isTaskAvailable = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isTaskAvailable;
	}

	private long getSeqNoFromTaskDet(Connection con, String requestNo, String referenceNo, String serviceNo)
			throws SQLException {
		long seq = 0;
		PreparedStatement ps2 = null;
		ResultSet rs = null;
		String WHERE_SQL = "";
		boolean whereadded = false;

		if (requestNo != null && !requestNo.equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE tsd_request_no = " + "'" + requestNo + "' ";
			whereadded = true;
		}
		if (serviceNo != null && !serviceNo.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND tsd_service_no = " + "'" + serviceNo + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE tsd_service_no = " + "'" + serviceNo + "' ";
				whereadded = true;
			}
		}
		if (referenceNo != null && !referenceNo.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND tsd_reference_no = " + "'" + referenceNo + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE tsd_reference_no = " + "'" + referenceNo + "' ";
				whereadded = true;
			}
		}

		String q2 = "SELECT * FROM public.nt_t_subsidy_task_det " + WHERE_SQL;

		ps2 = con.prepareStatement(q2);
		rs = ps2.executeQuery();

		if (rs.next() == true) {
			seq = rs.getInt("tsd_seq");
		}

		ConnectionManager.close(ps2);
		ConnectionManager.close(rs);

		return seq;
	}

	public void updateTaskStatusCompletedSubsidyTaskTable(Connection con, String requestNo, String serviceNo,
			String referenceNo, String currTask, String status, String loggedUser) throws SQLException {
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		PreparedStatement ps4 = null;
		ResultSet rs = null;
		String WHERE_SQL = "";
		boolean whereadded = false;

		if (requestNo != null && !requestNo.equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE tsd_request_no = " + "'" + requestNo + "' ";
			whereadded = true;
		}

		if (referenceNo != null && !referenceNo.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND tsd_reference_no = " + "'" + referenceNo + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE tsd_reference_no = " + "'" + referenceNo + "' ";
				whereadded = true;
			}
		}

		String q2 = "SELECT * FROM public.nt_t_subsidy_task_det " + WHERE_SQL;

		ps2 = con.prepareStatement(q2);
		rs = ps2.executeQuery();

		String serviceNoFromDatabase = null;

		if (rs.next() == true) {
			// insert in to history table
			String q3 = "INSERT INTO public.nt_h_subsidy_task_his "
					+ "(tsd_seq, tsd_request_no, tsd_reference_no, tsd_service_no, tsd_task_code, tsd_status, created_by, created_date) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?);";

			ps3 = con.prepareStatement(q3);

			ps3.setInt(1, rs.getInt("tsd_seq"));
			ps3.setString(2, rs.getString("tsd_request_no"));
			ps3.setString(3, rs.getString("tsd_reference_no"));
			ps3.setString(4, rs.getString("tsd_service_no"));
			ps3.setString(5, rs.getString("tsd_task_code"));
			ps3.setString(6, rs.getString("tsd_status"));
			ps3.setString(7, rs.getString("created_by"));
			ps3.setTimestamp(8, rs.getTimestamp("created_date"));

			ps3.executeUpdate();

			serviceNoFromDatabase = rs.getString("tsd_service_no");
		}

		// update task details table
		String q4 = "UPDATE public.nt_t_subsidy_task_det SET tsd_task_code=?, tsd_status=?, tsd_service_no=?, created_by=?, created_date=? "
				+ WHERE_SQL;

		ps4 = con.prepareStatement(q4);
		ps4.setString(1, currTask);
		ps4.setString(2, status);
		if (serviceNo != null && !serviceNo.trim().equals("")) {
			ps4.setString(3, serviceNo);
		} else {
			ps4.setString(3, serviceNoFromDatabase);
		}
		ps4.setString(4, loggedUser);
		ps4.setTimestamp(5, timestamp);

		ps4.executeUpdate();

		ConnectionManager.close(ps2);
		ConnectionManager.close(ps3);
		ConnectionManager.close(ps4);

	}

	// used for create voucher
	@Override
	public void updateTaskStatusOngoingSubsidyTaskTable(String requestNo, String serviceNo, String referenceNo,
			String currTask, String status, String loggedUser) {
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		PreparedStatement ps4 = null;
		ResultSet rs = null;
		String WHERE_SQL = "";
		boolean whereadded = false;

		Connection con = null;

		try {

			con = ConnectionManager.getConnection();
			if (requestNo != null && !requestNo.equals("")) {
				WHERE_SQL = WHERE_SQL + " WHERE tsd_request_no = " + "'" + requestNo + "' ";
				whereadded = true;
			}
			if (serviceNo != null && !serviceNo.equals("")) {
				if (whereadded) {
					WHERE_SQL = WHERE_SQL + " AND tsd_service_no = " + "'" + serviceNo + "' ";
				} else {
					WHERE_SQL = WHERE_SQL + " WHERE tsd_service_no = " + "'" + serviceNo + "' ";
					whereadded = true;
				}
			}

			if (referenceNo != null && !referenceNo.equals("")) {
				if (whereadded) {
					WHERE_SQL = WHERE_SQL + " AND tsd_reference_no = " + "'" + referenceNo + "' ";
				} else {
					WHERE_SQL = WHERE_SQL + " WHERE tsd_reference_no = " + "'" + referenceNo + "' ";
					whereadded = true;
				}
			}

			String q2 = "SELECT * FROM public.nt_t_subsidy_task_det " + WHERE_SQL;

			ps2 = con.prepareStatement(q2);
			rs = ps2.executeQuery();

			if (rs.next() == true) {
				// insert in to history table
				String q3 = "INSERT INTO public.nt_h_subsidy_task_his "
						+ "(tsd_seq, tsd_request_no, tsd_reference_no, tsd_service_no, tsd_task_code, tsd_status, created_by, created_date) "
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?);";

				ps3 = con.prepareStatement(q3);

				ps3.setInt(1, rs.getInt("tsd_seq"));
				ps3.setString(2, rs.getString("tsd_request_no"));
				ps3.setString(3, rs.getString("tsd_reference_no"));
				ps3.setString(4, rs.getString("tsd_service_no"));
				ps3.setString(5, rs.getString("tsd_task_code"));
				ps3.setString(6, rs.getString("tsd_status"));
				ps3.setString(7, rs.getString("created_by"));
				ps3.setTimestamp(8, rs.getTimestamp("created_date"));

				ps3.executeUpdate();

			}

			// update task details table
			String q4 = "UPDATE public.nt_t_subsidy_task_det SET tsd_task_code=?, tsd_status=?, created_by=?, created_date=? "
					+ WHERE_SQL;

			ps4 = con.prepareStatement(q4);
			ps4.setString(1, currTask);
			ps4.setString(2, status);
			ps4.setString(3, loggedUser);
			ps4.setTimestamp(4, timestamp);

			ps4.executeUpdate();

			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps2);
			ConnectionManager.close(ps3);
			ConnectionManager.close(ps4);
			ConnectionManager.close(con);
		}
	}

	@Override
	public List<SisuSeriyaDTO> searchDataForCommonInquiry(SisuSeriyaDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> list = new ArrayList<SisuSeriyaDTO>();

		try {
			String WHERE_SQL = "";

			if (dto.getRequestNo() != null && !dto.getRequestNo().equals("")) {
				WHERE_SQL = WHERE_SQL + " AND pm.sps_request_no = '" + dto.getRequestNo() + "' ";
			}
			if (dto.getBusRegNo() != null && !dto.getBusRegNo().equals("")) {
				WHERE_SQL = WHERE_SQL + " AND pm.sps_bus_no = '" + dto.getBusRegNo() + "' ";
			}
			if (dto.getServiceNo() != null && !dto.getServiceNo().equals("")) {
				WHERE_SQL = WHERE_SQL + " AND pm.sps_service_no = '" + dto.getServiceNo() + "' ";
			}
			if (dto.getNameOfOperator() != null && !dto.getNameOfOperator().equals("")) {
				WHERE_SQL = WHERE_SQL + " AND pm.sps_operator_name = '" + dto.getNameOfOperator() + "' ";
			}

			con = ConnectionManager.getConnection();

			String query = "select pm.sps_request_no, tsd_reference_no, pm.sps_service_no, pm.sps_bus_no as busno, pm.sps_operator_name as operatorname, tsd_task_code, "
					+ " ts.description as taskdescription, tsd_status, t.created_by as createdby, t.created_date as createddate, pm.sps_remark as remark from "
					+ " (select * FROM public.nt_h_subsidy_task_his  UNION select * FROM public.nt_t_subsidy_task_det) As t "
					+ " inner join public.nt_r_task ts on ts.code=t.tsd_task_code "
					+ " left join public.nt_m_sisu_permit_hol_service_info pm on pm.sps_service_ref_no = t.tsd_reference_no "
					+ " where t.tsd_reference_no=? and tsd_status='C' and tsd_task_code!='SS002' " + WHERE_SQL
					+ " order by t.created_date desc; ";

			ps = con.prepareStatement(query);
			ps.setString(1, dto.getServiceRefNo());
			rs = ps.executeQuery();
			SisuSeriyaDTO e;

			while (rs.next()) {
				e = new SisuSeriyaDTO();
				e.setRequestNo(rs.getString("sps_request_no"));
				e.setServiceRefNo(rs.getString("tsd_reference_no"));
				e.setServiceNo(rs.getString("sps_service_no"));
				e.setBusRegNo(rs.getString("busno"));
				e.setNameOfOperator(rs.getString("operatorname"));
				e.setTaskCode(rs.getString("tsd_task_code"));
				e.setTaskDescription(rs.getString("taskdescription"));
				e.setTaskStatus(rs.getString("tsd_status"));
				if (rs.getString("tsd_status") != null && rs.getString("tsd_status").trim().equals("C")) {
					e.setTaskStatusDescription("Completed");
				} else if (rs.getString("tsd_status") != null && rs.getString("tsd_status").trim().equals("O")) {
					e.setTaskStatusDescription("Ongoing");
				}
				e.setCreatedBy(rs.getString("createdby"));

				Timestamp ts = rs.getTimestamp("createddate");
				Date date = new Date();
				date.setTime(ts.getTime());
				String createdDateTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
				e.setCreatedDateTimeStr(createdDateTimeStr);
				e.setRemark(rs.getString("remark"));
				list.add(e);
			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return list;
	}

	public void deactivatePreviousAndActivateCurrentAgreement(Connection con, SisuSeriyaDTO selectDTO, String loginUser)
			throws SQLException {
		String commonServiceNo = selectDTO.getServiceNo();
		String renewalServiceRefNo = selectDTO.getServiceRefNo();

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;

		// add previous agreement to history and deactivate by commonServiceNo and
		// status ='A'

		// insert current record in to history table
		String sql1 = "INSERT into public.nt_h_sisu_permit_hol_service_info  "
				+ " (SELECT * FROM public.nt_m_sisu_permit_hol_service_info WHERE sps_service_no = ? and sps_status='A') ";

		ps1 = con.prepareStatement(sql1);
		ps1.setString(1, commonServiceNo);
		ps1.executeUpdate();

		// deactivate current agreement by by commonServiceNo and status ='A' | Set
		// sps_status to 'I': 'INACTIVE'
		String sql2 = "UPDATE public.nt_m_sisu_permit_hol_service_info SET sps_modified_by=?, sps_modified_date=?, "
				+ "  sps_status='I'  WHERE sps_service_no = ? and sps_status='A';";

		ps2 = con.prepareStatement(sql2);

		ps2.setString(1, loginUser);
		ps2.setTimestamp(2, timestamp);
		ps2.setString(3, commonServiceNo);
		ps2.executeUpdate();

		// activate current agreement by renewalServiceRefNo
		String sql3 = "UPDATE public.nt_m_sisu_permit_hol_service_info SET sps_service_end_date=?, sps_modified_by=?, sps_modified_date=?, "
				+ "sps_approve_reject_by=?, sps_approve_reject_date=?,  sps_renewal_status=?,  sps_renewal_new_expire_date=?, sps_status='A'  WHERE sps_service_ref_no=?;";

		ps3 = con.prepareStatement(sql3);

		Timestamp serviceEndDate = null;
		if (selectDTO.getNewExpiryDateObj() != null) {
			serviceEndDate = new Timestamp(selectDTO.getNewExpiryDateObj().getTime());
		}
		Timestamp newExDateTs = null;
		if (selectDTO.getNewExpiryDateObj() != null) {
			newExDateTs = new Timestamp(selectDTO.getNewExpiryDateObj().getTime());
		}

		ps3.setTimestamp(1, serviceEndDate);
		ps3.setString(2, loginUser);
		ps3.setTimestamp(3, timestamp);
		ps3.setString(4, loginUser);
		ps3.setTimestamp(5, timestamp);
		ps3.setString(6, "O");
		ps3.setTimestamp(7, newExDateTs);
		ps3.setString(8, renewalServiceRefNo);
		ps3.executeUpdate();

		ConnectionManager.close(ps1);
		ConnectionManager.close(ps2);
		ConnectionManager.close(ps3);

	}

	public void cancelAgreement(Connection con, SisuSeriyaDTO selectDTO, String loginUser) throws SQLException {
		String renewalServiceRefNo = selectDTO.getServiceRefNo();

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;

		// cancel current agreement by renewalServiceRefNo
		String sql2 = "UPDATE public.nt_m_sisu_permit_hol_service_info SET sps_service_end_date=?, sps_modified_by=?, sps_modified_date=?, "
				+ "sps_approve_reject_by=?, sps_approve_reject_date=?,  sps_renewal_new_expire_date=?, sps_status='I', sps_renewal_status='A'  WHERE sps_service_ref_no=?;";

		ps2 = con.prepareStatement(sql2);

		Timestamp serviceEndDate = null;
		if (selectDTO.getNewExpiryDateObj() != null) {
			serviceEndDate = new Timestamp(selectDTO.getNewExpiryDateObj().getTime());
		}
		Timestamp newExDateTs = null;
		if (selectDTO.getNewExpiryDateObj() != null) {
			newExDateTs = new Timestamp(selectDTO.getNewExpiryDateObj().getTime());
		}

		ps2.setTimestamp(1, serviceEndDate);
		ps2.setString(2, loginUser);
		ps2.setTimestamp(3, timestamp);
		ps2.setString(4, loginUser);
		ps2.setTimestamp(5, timestamp);
		ps2.setTimestamp(6, newExDateTs);
		ps2.setString(7, renewalServiceRefNo);
		ps2.executeUpdate();

		// delete record from temp table
		String q3 = "DELETE from  public.nt_temp_sisu_permit_hol_service_info WHERE sps_service_ref_no=?;";

		ps3 = con.prepareStatement(q3);
		ps3.setString(1, selectDTO.getServiceRefNo());
		ps3.executeUpdate();

		ConnectionManager.close(ps1);
		ConnectionManager.close(ps2);
		ConnectionManager.close(ps3);

	}

	@Override
	public List<SisuSeriyaDTO> getVoucherPendingAndVoucherCreatedRequestNo() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct sps_request_no from public.nt_m_sisu_permit_hol_service_info "
					+ "inner join public.nt_t_subsidy_task_det on nt_t_subsidy_task_det.tsd_service_no=nt_m_sisu_permit_hol_service_info.sps_service_no "
					+ "where ((nt_t_subsidy_task_det.tsd_task_code='SS006' and nt_t_subsidy_task_det.tsd_status='C' and sps_status='A') or "
					+ "(nt_t_subsidy_task_det.tsd_task_code='SS012' and nt_t_subsidy_task_det.tsd_status='C' and sps_status='A') or ( "
					+ "nt_t_subsidy_task_det.tsd_task_code='SM001' and nt_t_subsidy_task_det.tsd_status='O' "
					+ "or nt_t_subsidy_task_det.tsd_task_code='SM002'and nt_t_subsidy_task_det.tsd_status='O' )) and is_sltb !='Y' order by sps_request_no desc";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			SisuSeriyaDTO s;

			while (rs.next()) {
				s = new SisuSeriyaDTO();
				s.setRequestNo(rs.getString("sps_request_no"));
				data.add(s);
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
	public void updateTaskCompleteSubsidyTaskTableForPrintAgreement(String requestNo, String serviceNo,
			String serviceRefNo, String currTask, String status, String loginUser) throws SQLException {
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		Connection con = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		PreparedStatement ps4 = null;
		ResultSet rs = null;
		String WHERE_SQL = "";
		boolean whereadded = false;

		if (requestNo != null && !requestNo.equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE tsd_request_no = " + "'" + requestNo + "' ";
			whereadded = true;
		}

		if (serviceRefNo != null && !serviceRefNo.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND tsd_reference_no = " + "'" + serviceRefNo + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE tsd_reference_no = " + "'" + serviceRefNo + "' ";
				whereadded = true;
			}
		}

		con = ConnectionManager.getConnection();

		if (currTask.equals("SM004")) {
			String q2 = "SELECT * FROM public.nt_t_subsidy_task_det " + WHERE_SQL;

			ps2 = con.prepareStatement(q2);
			rs = ps2.executeQuery();

			if (rs.next() == true) {
				String q3 = "INSERT INTO public.nt_h_subsidy_task_his "
						+ "(tsd_seq, tsd_request_no, tsd_reference_no, tsd_service_no, tsd_task_code, tsd_status, created_by, created_date) "
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?);";

				try {
					ps3 = con.prepareStatement(q3);

					ps3.setInt(1, rs.getInt("tsd_seq"));
					ps3.setString(2, rs.getString("tsd_request_no"));
					ps3.setString(3, rs.getString("tsd_reference_no"));
					ps3.setString(4, rs.getString("tsd_service_no"));
					ps3.setString(5, "SM004");
					ps3.setString(6, "C");
					ps3.setString(7, rs.getString("created_by"));
					ps3.setTimestamp(8, rs.getTimestamp("created_date"));

					ps3.executeUpdate();
				}catch (SQLException e) {
					con.rollback();
				}
				
			}

		} else {
			String q2 = "SELECT * FROM public.nt_t_subsidy_task_det " + WHERE_SQL;

			ps2 = con.prepareStatement(q2);
			rs = ps2.executeQuery();

			if (rs.next() == true) {
				// insert in to history table
				String q3 = "INSERT INTO public.nt_h_subsidy_task_his "
						+ "(tsd_seq, tsd_request_no, tsd_reference_no, tsd_service_no, tsd_task_code, tsd_status, created_by, created_date) "
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?);";

				try {
					ps3 = con.prepareStatement(q3);

					ps3.setInt(1, rs.getInt("tsd_seq"));
					ps3.setString(2, rs.getString("tsd_request_no"));
					ps3.setString(3, rs.getString("tsd_reference_no"));
					ps3.setString(4, rs.getString("tsd_service_no"));
					ps3.setString(5, rs.getString("tsd_task_code"));
					ps3.setString(6, rs.getString("tsd_status"));
					ps3.setString(7, rs.getString("created_by"));
					ps3.setTimestamp(8, rs.getTimestamp("created_date"));

					ps3.executeUpdate();
				}catch (SQLException e) {
					con.rollback();
				}
				

			}

			// update task details table
			String q4 = "UPDATE public.nt_t_subsidy_task_det SET tsd_task_code=?, tsd_status=?, tsd_service_no=?, created_by=?, created_date=? "
					+ WHERE_SQL;

			try {
				ps4 = con.prepareStatement(q4);
				ps4.setString(1, currTask);
				ps4.setString(2, status);
				if (serviceNo != null && !serviceNo.trim().equals("")) {
					ps4.setString(3, serviceNo);
				} else {
					ps4.setString(3, rs.getString("tsd_service_no"));
				}
				ps4.setString(4, loginUser);
				ps4.setTimestamp(5, timestamp);

				ps4.executeUpdate();
			}catch (SQLException e) {
				con.rollback();
			}
			
		}

		con.commit();

		ConnectionManager.close(ps2);
		ConnectionManager.close(ps3);
		ConnectionManager.close(ps4);
		ConnectionManager.close(con);

	}

	@Override
	public String getBranchCodeByRequestNo(String requestNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String bankBranchCode = null;

		try {
			con = ConnectionManager.getConnection();

			String sql1 = "select sps_branch_code from  public.nt_m_sisu_permit_hol_service_info where sps_service_ref_no =?;";
			ps = con.prepareStatement(sql1);

			ps.setString(1, requestNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				bankBranchCode = rs.getString("sps_branch_code");

			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return bankBranchCode;
	}

	@Override
	public String getServiceRefNoByServiceNo(String serviceNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String refNo = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct sps_service_ref_no " + "from public.nt_m_sisu_permit_hol_service_info "
					+ "WHERE sps_service_ref_no is not null and sps_service_ref_no !='' and sps_service_no is not null and sps_service_no !='' "
					+ "and ((sps_status='A' and is_cancellation is null) or (sps_status='P' and sps_renewal_status in ('P','R'))) "
					+ "and sps_service_no =? order by sps_service_ref_no desc ;";

			ps = con.prepareStatement(query);
			ps.setString(1, serviceNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				SisuSeriyaDTO ss = new SisuSeriyaDTO();

				ss.setServiceRefNo(rs.getString("sps_service_ref_no"));
				refNo = ss.getServiceRefNo();

			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return refNo;
	}

	@Override
	public List<SisuSeriyaDTO> getServiceNoListForRenewalNew() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SisuSeriyaDTO> drpdOperatorDepoNameList = new ArrayList<SisuSeriyaDTO>();

		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct b.sps_service_no as serviceno from public.nt_m_sisu_permit_hol_service_info b "
					+ " where sps_renewal_status in ('A','P','R') and b.sps_service_no is not null and b.sps_service_no != '' order by b.sps_service_no desc;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				SisuSeriyaDTO ss = new SisuSeriyaDTO();
				ss.setServiceNo(rs.getString("serviceno"));
				ss.setServiceAgreementNo(rs.getString("serviceno"));
				drpdOperatorDepoNameList.add(ss);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return drpdOperatorDepoNameList;

	}

	@Override
	public List<SisuSeriyaDTO> getListForSelectedServiceAgreementNo(String selectedServiceAgreementNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SisuSeriyaDTO> returnList = new ArrayList<SisuSeriyaDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  sps_request_no, sps_service_ref_no, sps_operator_name, sps_service_end_date,  sps_service_no, sps_status, sps_renewal_status, sps_renewal_new_expire_date, sps_is_checked, sps_is_recommended, is_cancellation FROM public.nt_m_sisu_permit_hol_service_info where sps_renewal_status in ('A','P','R') and sps_service_no is not null and sps_service_no !='' and sps_service_no=? order by sps_service_ref_no;";
			ps = con.prepareStatement(query);
			ps.setString(1, selectedServiceAgreementNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				SisuSeriyaDTO sisuSeriyaDTO = new SisuSeriyaDTO();
				sisuSeriyaDTO.setRequestNo(rs.getString("sps_request_no"));
				sisuSeriyaDTO.setServiceRefNo(rs.getString("sps_service_ref_no"));
				sisuSeriyaDTO.setServiceNo(rs.getString("sps_service_no"));
				sisuSeriyaDTO.setNameOfOperator(rs.getString("sps_operator_name"));
				sisuSeriyaDTO.setExpiryDateVal(rs.getString("sps_service_end_date"));
				sisuSeriyaDTO.setServiceEndDateVal(rs.getString("sps_service_end_date"));
				sisuSeriyaDTO.setServiceRenewalStatus(rs.getString("sps_renewal_status"));
				sisuSeriyaDTO.setStatusCode(rs.getString("sps_status"));
				sisuSeriyaDTO.setStatus(rs.getString("sps_status"));
				String statusCode = sisuSeriyaDTO.getServiceRenewalStatus();
				if (statusCode.equals("A")) {
					sisuSeriyaDTO.setStatusDes("APPROVED");
					sisuSeriyaDTO.setDisabledNewExpiredDate(true);
				} else if (statusCode.equals("P")) {
					sisuSeriyaDTO.setStatusDes("PENDING");
					sisuSeriyaDTO.setDisabledNewExpiredDate(false);
				} else if (statusCode.equals("R")) {
					sisuSeriyaDTO.setStatusDes("REJECTED");
					sisuSeriyaDTO.setDisabledNewExpiredDate(true);
				} else if (statusCode.equals("O")) {
					sisuSeriyaDTO.setStatusDes("ONGOING");
					sisuSeriyaDTO.setDisabledNewExpiredDate(true);
				}
				sisuSeriyaDTO.setNewExpiryDateObj(rs.getDate("sps_renewal_new_expire_date"));
				sisuSeriyaDTO.setIsChecked(rs.getString("sps_is_checked"));
				sisuSeriyaDTO.setIsRecommended(rs.getString("sps_is_recommended"));
				sisuSeriyaDTO.setCancellationStatus(rs.getString("is_cancellation"));
				returnList.add(sisuSeriyaDTO);
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
	public List<SisuSeriyaDTO> getServiceNoListForIssueLogSheet() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SisuSeriyaDTO> data = new ArrayList<SisuSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			/* Query changed: dhananjika.d (27/06/2024) */
//			String query = "select distinct sps_service_no from public.nt_m_sisu_permit_hol_service_info WHERE sps_service_ref_no is not null and sps_service_ref_no !='' and sps_status='A' and sps_renewal_status not in ('I')   and  (sps_service_agreement_issuedby is not NULL and sps_permit_sticker_issuedby IS not NULL and  sps_issue_logsheets_issuedby is not null) order by sps_service_no desc ;";
			String query = "select distinct sps_service_no from public.nt_m_sisu_permit_hol_service_info WHERE sps_service_ref_no is not null and sps_service_ref_no !='' and sps_status='A' and sps_renewal_status not in ('I','P')   and  (sps_service_agreement_issuedby is not NULL and sps_permit_sticker_issuedby IS not NULL and  sps_issue_logsheets_issuedby is not null) order by sps_service_no desc ;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			SisuSeriyaDTO s;

			while (rs.next()) {
				s = new SisuSeriyaDTO();
				s.setServiceNo(rs.getString("sps_service_no"));
				data.add(s);
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
	public List<SisuSeriyaDTO> getServiceRefNumberByServiceNo(SisuSeriyaDTO ssDto) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String WHERE_SQL = "";

		List<SisuSeriyaDTO> drpdServiceReferenceNoList = new ArrayList<SisuSeriyaDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select sps_service_ref_no from public.nt_m_sisu_permit_hol_service_info where sps_service_no=?";

			ps = con.prepareStatement(query);
			ps.setString(1, ssDto.getServiceNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				SisuSeriyaDTO ss = new SisuSeriyaDTO();

				ss.setServiceRefNo(rs.getString("sps_service_ref_no"));
				drpdServiceReferenceNoList.add(ss);

			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return drpdServiceReferenceNoList;

	}

	@Override
	public boolean getRenewalStatusByRequestNo(String requestNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean ongoing = false;

		try {
			con = ConnectionManager.getConnection();

			String sql1 = "SELECT * FROM public.nt_m_sisu_permit_hol_service_info WHERE sps_request_no = ? and sps_status ='P'";
			ps = con.prepareStatement(sql1);

			ps.setString(1, requestNo);
			rs = ps.executeQuery();

			while (rs.next()) {

				ongoing = true;

			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return ongoing;

	}

	/* Added by dhanajika.d 06/03/2024 */
	@Override
	public boolean checkAlreadyHave(String serviceRefNo, String year, String serviceType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean ongoing = false;

		try {
			con = ConnectionManager.getConnection();

			String sql1 = "SELECT los_std_service_ref_no FROM public.nt_m_log_sheets WHERE los_std_service_ref_no = ? and los_year = ? and los_subsidy_service_type_code = ?";
			ps = con.prepareStatement(sql1);

			ps.setString(1, serviceRefNo);
			ps.setString(2, year);
			ps.setString(3, serviceType);
			rs = ps.executeQuery();

			while (rs.next()) {

				ongoing = true;

			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return ongoing;

	}

}