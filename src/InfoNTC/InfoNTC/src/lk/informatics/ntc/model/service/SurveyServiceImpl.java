package lk.informatics.ntc.model.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.informatics.ntc.model.dto.CommitteeOrBoardApprovalDTO;
import lk.informatics.ntc.model.dto.EmployeeDTO;
import lk.informatics.ntc.model.dto.FormDTO;
import lk.informatics.ntc.model.dto.GenerateReceiptDTO;
import lk.informatics.ntc.model.dto.IndicatorsDTO;
import lk.informatics.ntc.model.dto.SurveyDTO;
import lk.informatics.ntc.model.dto.SurveyLocationTeamDTO;
import lk.informatics.ntc.model.dto.TrafficProposalDTO;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.roc.utils.common.Utils;

public class SurveyServiceImpl implements SurveyService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public List<SurveyDTO> getSurveyReqOnGoingNoDropDown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SurveyDTO> requstNo = new ArrayList<SurveyDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "Select ini_isu_requestno,ini_request_date From public.nt_t_initiate_survey Where ini_status='I' order by ini_isu_requestno;";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				SurveyDTO surveyDTO = new SurveyDTO();
				surveyDTO.setRequestNo(rs.getString("ini_isu_requestno"));
				surveyDTO.setRequestDate(rs.getString("ini_request_date"));

				requstNo.add(surveyDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return requstNo;

	}

	@Override
	public List<SurveyDTO> getSurveyReqNoDropDown() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SurveyDTO> requstNoList = new ArrayList<SurveyDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "Select ini_isu_requestno, ini_surveyno, ini_request_date From public.nt_t_initiate_survey Where ini_status='S' order by ini_isu_requestno;";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				SurveyDTO surveyDTO = new SurveyDTO();
				surveyDTO.setRequestNo(rs.getString("ini_isu_requestno"));
				surveyDTO.setSurveyNo(rs.getString("ini_surveyno"));
				surveyDTO.setRequestDate(rs.getString("ini_request_date"));

				requstNoList.add(surveyDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return requstNoList;
	}

	@Override
	public List<SurveyDTO> getSurveyTypeToDropDown() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SurveyDTO> requstTypeList = new ArrayList<SurveyDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "Select code, description FROM public.nt_r_survey_types where active='A' order by description;";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				SurveyDTO surveyDTO = new SurveyDTO();
				surveyDTO.setSurveyTypeCode(rs.getString("code"));
				surveyDTO.setSurveyTypeDescription(rs.getString("description"));

				requstTypeList.add(surveyDTO);

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

	@Override
	public List<SurveyDTO> getSurveyMethodToDropDown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SurveyDTO> surveyMethodList = new ArrayList<SurveyDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "Select code, description FROM public.nt_r_survey_methods where active='A' order by description;";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				SurveyDTO surveyDTO = new SurveyDTO();
				surveyDTO.setSurveyMethodCode(rs.getString("code"));
				surveyDTO.setSurveyMethodDescription(rs.getString("description"));

				surveyMethodList.add(surveyDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return surveyMethodList;

	}

	@Override
	public String saveSurveyData(SurveyDTO surveyDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String returnSurveyNo = null;
		String surveyNo = null;

		try {
			con = ConnectionManager.getConnection();

			if (surveyDTO.getSurveyNo() != null && !surveyDTO.getSurveyNo().trim().isEmpty()) {
				surveyNo = surveyDTO.getSurveyNo();
			} else {
				surveyNo = generateSurveyNo();
			}

			java.util.Date date = new java.util.Date();
			Timestamp timestamp = new Timestamp(date.getTime());

			String sql = "UPDATE public.nt_t_initiate_survey SET ini_surveyno=?,ini_survey_type=?, "
					+ "ini_survey_method=?, ini_remark=?,ini_status=?,ini_modified_date=?,ini_modified_by=? WHERE  ini_isu_requestno=?;";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, surveyNo);
			stmt.setString(2, surveyDTO.getSurveyType());
			stmt.setString(3, surveyDTO.getSurveyMethod());
			stmt.setString(4, surveyDTO.getRemarks());
			stmt.setString(5, "S");
			stmt.setTimestamp(6, timestamp);
			stmt.setString(7, surveyDTO.getLoginuser());
			stmt.setString(8, surveyDTO.getRequestNo());

			int data = stmt.executeUpdate();
			if (data > 0) {
				returnSurveyNo = surveyNo;
			}

			con.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return returnSurveyNo;
	}

	// generate Survey No
	@Override
	public String generateSurveyNo() {

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

			String sql = "SELECT ini_surveyno  FROM public.nt_t_initiate_survey WHERE ini_surveyno IS NOT NULL ORDER BY ini_surveyno desc LIMIT 1 ";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("ini_surveyno");
			}

			if (result != null) {

				lastyr = result.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String ApprecordcountN = "00001";
					strSurveyNo = "SIP" + currYear + ApprecordcountN;
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
					strSurveyNo = "SIP" + currYear + ApprecordcountN;
				}
			} else
				strSurveyNo = "SIP" + currYear + "00001";

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
	public String generateSurveyRequestNo() {

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

			String sql = "SELECT ini_isu_requestno FROM public.nt_t_initiate_survey WHERE ini_isu_requestno IS NOT NULL ORDER BY ini_isu_requestno desc LIMIT 1";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("ini_isu_requestno");
			}

			if (result != null) {

				lastyr = result.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String ApprecordcountN = "00001";
					strSurveyRequestNo = "SIR" + currYear + ApprecordcountN;
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
					strSurveyRequestNo = "SIR" + currYear + ApprecordcountN;
				}
			} else
				strSurveyRequestNo = "SIR" + currYear + "00001";

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return strSurveyRequestNo;

	}

	@Override
	public String fillOrga(String requestNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String organisationCode = null;

		String WHERE_SQL = "";
		if (requestNo != null && !requestNo.equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE ini_isu_requestno = " + "'" + requestNo + "'";
		}

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT b.description , a.ini_organisation_code FROM public.nt_t_initiate_survey a inner join nt_r_organization b ON a.ini_organisation_code=b.code "
					+ WHERE_SQL;

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				organisationCode = rs.getString("description");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return organisationCode;
	}

	@Override
	public String fillDepart(String requestNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String department = null;

		String WHERE_SQL = "";
		if (requestNo != null && !requestNo.equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE ini_isu_requestno = " + "'" + requestNo + "'";
		}

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT b.description , a.ini_department_code FROM public.nt_t_initiate_survey a inner join nt_r_department b ON a.ini_department_code=b.code "
					+ WHERE_SQL;

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				department = rs.getString("description");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return department;

	}

	@Override
	public String fillRequestType(String requestNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String requestType = null;

		String WHERE_SQL = "";
		if (requestNo != null && !requestNo.equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE ini_isu_requestno = " + "'" + requestNo + "'";
		}

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT b.description , a.ini_request_type FROM public.nt_t_initiate_survey a inner join nt_r_request_types b ON a.ini_request_type=b.code "
					+ WHERE_SQL;

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				requestType = rs.getString("description");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return requestType;

	}

	@Override
	public SurveyDTO fillSUrveyReason(String requestNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		SurveyDTO surveyDTO = new SurveyDTO();

		String WHERE_SQL = "";
		if (requestNo != null && !requestNo.equals("")) {
			WHERE_SQL = WHERE_SQL + " where ini_isu_requestno = " + "'" + requestNo + "'";
		}

		try {
			con = ConnectionManager.getConnection();

			String query = "select a.ini_surveyreason,a.ini_one_way_fare ,b.description from public.nt_t_initiate_survey a left outer join public.nt_r_service_types b on a.ini_service_type=b.code"
					+ WHERE_SQL;

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				surveyDTO.setSurveyReason(rs.getString("ini_surveyreason"));
				surveyDTO.setServiceType(rs.getString("description"));
				surveyDTO.setBusFare(rs.getBigDecimal("ini_one_way_fare"));

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
	public String fillRouteNo(String requestNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String routeNo = null;

		String WHERE_SQL = "";
		if (requestNo != null && !requestNo.equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE ini_isu_requestno = " + "'" + requestNo + "'";
		}

		try {
			con = ConnectionManager.getConnection();

			String query = "select ini_routeno from public.nt_t_initiate_survey" + WHERE_SQL;

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				routeNo = rs.getString("ini_routeno");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return routeNo;

	}

	@Override
	public SurveyDTO fillRouteDetails(String requestNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		SurveyDTO surveyDTO = new SurveyDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT a.rou_service_origine,a.rou_service_destination,a.rou_via,b.ini_isroute_available ,c.description FROM public.nt_r_route a inner join public.nt_t_initiate_survey b on a.rou_number=b.ini_routeno left outer join public.nt_r_service_types c on b.ini_service_type=c.code WHERE b.ini_isu_requestno=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, requestNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				surveyDTO.setOrigin(rs.getString("rou_service_origine"));
				surveyDTO.setDestination(rs.getString("rou_service_destination"));
				surveyDTO.setVia(rs.getString("rou_via"));
				surveyDTO.setRouteAvailable(rs.getString("ini_isroute_available"));
				surveyDTO.setServiceType(rs.getString("description"));
				if (rs.getString("ini_isroute_available") != null
						&& !rs.getString("ini_isroute_available").trim().isEmpty()) {
					if (rs.getString("ini_isroute_available").equals("Y")) {
						surveyDTO.setRouteAvailable("true");
					} else if (rs.getString("ini_isroute_available").equals("N")) {
						surveyDTO.setRouteAvailable("false");
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

		return surveyDTO;
	}

	@Override
	public SurveyDTO fillNewRouteDetails(String requestNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		SurveyDTO surveyDTO = new SurveyDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT a.rou_service_origine,a.rou_service_destination,a.rou_via,b.ini_new_route FROM public.nt_r_route a,public.nt_t_initiate_survey b where a.rou_number=b.ini_new_route and b.ini_isu_requestno=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, requestNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				surveyDTO.setOriginNew(rs.getString("rou_service_origine"));
				surveyDTO.setDestinationNew(rs.getString("rou_service_destination"));
				surveyDTO.setViaNew(rs.getString("rou_via"));
				surveyDTO.setRouteNoNew(rs.getString("ini_new_route"));

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
	public List<SurveyDTO> getSurveyNoDropDown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SurveyDTO> surveyNoList = new ArrayList<SurveyDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "Select ini_surveyno From public.nt_t_initiate_survey Where ini_status='S' and ini_surveyno is not null ;";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				SurveyDTO surveyDTO = new SurveyDTO();
				surveyDTO.setSurveyNo(rs.getString("ini_surveyno"));

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
	public SurveyDTO fillSurReq(String surveyNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		SurveyDTO surveyDTO = new SurveyDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "select ini_isu_requestno,ini_request_date  from public.nt_t_initiate_survey where ini_surveyno=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, surveyNo);
			rs = ps.executeQuery();

			while (rs.next()) {

				surveyDTO.setRequestNo(rs.getString("ini_isu_requestno"));
				surveyDTO.setRequestDate(rs.getString("ini_request_date"));

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
	public SurveyDTO fillSurNo(String requestNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		SurveyDTO surveyDTO = new SurveyDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "select ini_surveyno,ini_request_date  from public.nt_t_initiate_survey where ini_isu_requestno=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, requestNo);
			rs = ps.executeQuery();

			while (rs.next()) {

				surveyDTO.setSurveyNo(rs.getString("ini_surveyno"));
				surveyDTO.setRequestDate(rs.getString("ini_request_date"));

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

	// fill searched data for Edit Survey Initiate Page
	@Override
	public SurveyDTO showDetails(String requestNo, String surveyNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String WHERE_SQL = "";
		boolean whereadded = false;

		if (requestNo != null && !requestNo.equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE ini_isu_requestno = " + "'" + requestNo + "'";
			whereadded = true;
		}
		if (surveyNo != null && !surveyNo.equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " OR ini_surveyno = " + "'" + surveyNo + "'";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE ini_surveyno = " + "'" + surveyNo + "'";

				whereadded = true;
			}
		}

		SurveyDTO surveyDTO = new SurveyDTO();

		try {
			con = ConnectionManager.getConnection();
			String query2 = " SELECT a.ini_surveyreason,a.ini_address,a.ini_address_sin,a.ini_address_tamil,a.ini_organisation_code,a.ini_department_code,a.ini_routeno,a.ini_isroute_available,a.ini_request_type,a.ini_new_route,a.ini_special_remark ,case when a.ini_status='A'then 'APPROVED'else'REJECTED' end as ini_status,b.description as organization , c.description as deparment,"
					+ " d.description as request_type,e.rou_service_origine,e.rou_service_destination,e.rou_via,f.rou_service_origine as neworigine,f.rou_service_destination as newdestination,f.rou_via as newvia,a.ini_survey_type,a.ini_survey_method,a.ini_remark,ini_service_type,ini_one_way_fare,g.description as service"
					+ " FROM public.nt_t_initiate_survey a "
					+ " left outer join public.nt_r_service_types g on a.ini_service_type=g.code"
					+ "  inner join nt_r_organization b ON a.ini_organisation_code=b.code "
					+ "  inner join nt_r_department c ON a.ini_department_code=c.code"
					+ "  inner join nt_r_request_types d ON a.ini_request_type=d.code "
					+ "  left join public.nt_r_route e ON a.ini_routeno=e.rou_number "
					+ "  left join public.nt_r_route f ON a.ini_new_route=f.rou_number " + WHERE_SQL;

			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();

			while (rs.next()) {

				surveyDTO.setSurveyReason(rs.getString("ini_surveyreason"));
				surveyDTO.setRouteNo(rs.getString("ini_routeno"));
				if (rs.getString("ini_isroute_available") != null
						&& !rs.getString("ini_isroute_available").trim().isEmpty()) {
					if (rs.getString("ini_isroute_available").equals("Y")) {
						surveyDTO.setRouteAvailable("true");
					} else if (rs.getString("ini_isroute_available").equals("N")) {
						surveyDTO.setRouteAvailable("false");
					}
				}
				surveyDTO.setOrganisationCode(rs.getString("ini_organisation_code"));
				surveyDTO.setDepartmentCode(rs.getString("ini_department_code"));
				surveyDTO.setOrganisationDescription(rs.getString("organization"));
				surveyDTO.setDepartmentDescription(rs.getString("deparment"));
				surveyDTO.setRequestType(rs.getString("ini_request_type"));
				surveyDTO.setRequestTypeDescription(rs.getString("request_type"));
				surveyDTO.setAddressEnglish(rs.getString("ini_address"));
				surveyDTO.setAddressSinhala(rs.getString("ini_address_sin"));
				surveyDTO.setAddressTamil(rs.getString("ini_address_tamil"));
				surveyDTO.setOrigin(rs.getString("rou_service_origine"));
				surveyDTO.setDestination(rs.getString("rou_service_destination"));
				surveyDTO.setVia(rs.getString("rou_via"));
				surveyDTO.setRouteNoNew(rs.getString("ini_new_route"));
				surveyDTO.setOriginNew(rs.getString("neworigine"));
				surveyDTO.setDestinationNew(rs.getString("newdestination"));
				surveyDTO.setViaNew(rs.getString("newvia"));
				surveyDTO.setSurveyTypeCode(rs.getString("ini_survey_type"));
				surveyDTO.setSurveyMethodCode(rs.getString("ini_survey_method"));
				surveyDTO.setRemarks(rs.getString("ini_remark"));
				surveyDTO.setSpecialRemarks(rs.getString("ini_special_remark"));
				surveyDTO.setStatus(rs.getString("ini_status"));
				surveyDTO.setServiceType(rs.getString("service"));
				surveyDTO.setServiceTypeCode(rs.getString("ini_service_type"));
				surveyDTO.setBusFare(rs.getBigDecimal("ini_one_way_fare"));
			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return surveyDTO;
	}

	// save Edited data from Edit survey Initiate process page
	@Override
	public void saveEditedSurveyData(String selectSurveyType, String selectSurveyMethod, String selectremarks,
			String selectrequestNo, String selectSurveyNo, String loginUser) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			java.util.Date date = new java.util.Date();
			Timestamp timestamp = new Timestamp(date.getTime());

			String sql = "UPDATE public.nt_t_initiate_survey SET ini_survey_type=?, ini_survey_method=?, ini_remark=?,ini_modified_date=? , ini_modified_by=?  WHERE   ini_isu_requestno=? and ini_surveyno=?  ;";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, selectSurveyType);
			stmt.setString(2, selectSurveyMethod);
			stmt.setString(3, selectremarks);

			stmt.setTimestamp(4, timestamp);
			stmt.setString(5, loginUser);
			stmt.setString(6, selectrequestNo);
			stmt.setString(7, selectSurveyNo);

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
	public boolean approveSurveyProcessRequest(SurveyDTO surveyDTO, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean returnFlag = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(timestamp);
		try {
			con = ConnectionManager.getConnection();
			String sql = "UPDATE public.nt_t_initiate_survey "
					+ "SET ini_status = 'A', ini_special_remark = ?, ini_isapproved='Y',ini_survey_approved_by=?, ini_survey_approved_date=? "
					+ "WHERE ini_isu_requestno=?";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, surveyDTO.getSpecialRemarks());
			stmt.setString(2, user);
			stmt.setString(3, formattedDate);
			stmt.setString(4, surveyDTO.getRequestNo());
			int flag = stmt.executeUpdate();
			con.commit();
			if (flag > 0) {
				returnFlag = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return returnFlag;
	}

	@Override
	public boolean rejectSurveyProcessRequest(SurveyDTO surveyDTO, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean returnFlag = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(timestamp);
		try {
			con = ConnectionManager.getConnection();
			String sql = "UPDATE public.nt_t_initiate_survey "
					+ "SET ini_status = 'R', ini_special_remark = ?, ini_isapproved='N',ini_survey_approved_by=?, ini_survey_approved_date=?  "
					+ "WHERE ini_isu_requestno=?";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, surveyDTO.getSpecialRemarks());
			stmt.setString(2, user);
			stmt.setString(3, formattedDate);
			stmt.setString(4, surveyDTO.getRequestNo());
			int flag = stmt.executeUpdate();
			con.commit();
			if (flag > 0) {
				returnFlag = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return returnFlag;
	}

	@Override
	public List<SurveyDTO> getOrganisationToDropDown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SurveyDTO> organisationTypeList = new ArrayList<SurveyDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select code,description from public.nt_r_organization where active='A';";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				SurveyDTO surveyMgtDTO = new SurveyDTO();
				surveyMgtDTO.setOrganisationCode(rs.getString("code"));
				surveyMgtDTO.setOrganisationDescription(rs.getString("description"));

				organisationTypeList.add(surveyMgtDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return organisationTypeList;

	}

	@Override
	public List<SurveyDTO> getDepartmentToDropDown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SurveyDTO> departmentTypeList = new ArrayList<SurveyDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select code, description from nt_r_department where active='A'; ";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				SurveyDTO surveyMgtDTO = new SurveyDTO();
				surveyMgtDTO.setDepartmentCode(rs.getString("code"));
				surveyMgtDTO.setDepartmentDescription(rs.getString("description"));

				departmentTypeList.add(surveyMgtDTO);

			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return departmentTypeList;
	}

	@Override
	public List<SurveyDTO> getRouteNoListToDropDown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SurveyDTO> routeList = new ArrayList<SurveyDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT rou_number FROM nt_r_route WHERE active = 'Y'  order by rou_number;;";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				SurveyDTO surDTO = new SurveyDTO();

				surDTO.setRouteNo(rs.getString("rou_number"));
				routeList.add(surDTO);

			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return routeList;
	}

	@Override
	public String saveSurveyRequestData(SurveyDTO surveyMgtDTO, String user) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String returnSurveyReqNo = null;
		String surveyReqNo = null;
		try {
			con = ConnectionManager.getConnection();

			if (surveyMgtDTO.getRequestNo() != null && !surveyMgtDTO.getRequestNo().trim().isEmpty()) {
				surveyReqNo = surveyMgtDTO.getRequestNo();
			} else {
				surveyReqNo = generateSurveyRequestNo();
			}
			LocalDate localDate = LocalDate.now();// For reference
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			String currentDate = localDate.format(formatter);

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_initiate_survey");

			String sql = "INSERT INTO public.nt_t_initiate_survey "
					+ "(ini_seq, ini_isu_requestno, ini_request_date, ini_organisation_code,ini_department_code, "
					+ " ini_request_type, ini_isroute_available, ini_routeno, ini_surveyreason, ini_address, "
					+ " ini_address_sin, ini_address_tamil, ini_new_route,ini_status,ini_service_type,ini_one_way_fare,ini_created_date,ini_created_by, "
					+ " ini_survey_initiated_date,ini_survey_initiated_by,ini_survey_approved_date) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'I', ?, ?,? ,?, ?, ?, ?); ";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, surveyReqNo);
			stmt.setString(3, currentDate);
			stmt.setString(4, surveyMgtDTO.getOrganisationCode());
			stmt.setString(5, surveyMgtDTO.getDepartmentCode());
			stmt.setString(6, surveyMgtDTO.getRequestType());
			if (surveyMgtDTO.getRouteAvailable() != null && surveyMgtDTO.getRouteAvailable().trim().equals("true")) {
				stmt.setString(7, "Y");
			} else if (surveyMgtDTO.getRouteAvailable() != null
					&& surveyMgtDTO.getRouteAvailable().trim().equals("false")) {
				stmt.setString(7, "N");
			} else {
				stmt.setString(7, "");
			}
			stmt.setString(8, surveyMgtDTO.getRouteNo());
			stmt.setString(9, surveyMgtDTO.getSurveyReason());
			stmt.setString(10, surveyMgtDTO.getAddressEnglish());
			stmt.setString(11, surveyMgtDTO.getAddressSinhala());
			stmt.setString(12, surveyMgtDTO.getAddressTamil());
			stmt.setString(13, surveyMgtDTO.getRouteNoNew());
			stmt.setString(14, surveyMgtDTO.getServiceType());
			stmt.setBigDecimal(15, surveyMgtDTO.getBusFare());
			java.util.Date date = new java.util.Date();
			Timestamp timestamp = new Timestamp(date.getTime());
			stmt.setTimestamp(16, timestamp);
			stmt.setString(17, user);
			stmt.setString(18, currentDate);
			stmt.setString(19, user);
			stmt.setString(20, currentDate);

			int data = stmt.executeUpdate();

			if (data > 0) {
				returnSurveyReqNo = surveyReqNo;
			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return returnSurveyReqNo;
	}

	@Override
	public boolean saveTempRouteDetails1(SurveyDTO routeDetailsDTO, String user) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Boolean flag = false;

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_r_route");

			String sql = "INSERT INTO public.nt_r_route "
					+ "(seqno, rou_number, rou_service_origine, rou_service_destination, rou_distance, rou_via, active, created_by, rou_description) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?); ";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, routeDetailsDTO.getRouteNoNew());
			stmt.setString(3, routeDetailsDTO.getOriginNew());
			stmt.setString(4, routeDetailsDTO.getDestinationNew());
			stmt.setInt(5, routeDetailsDTO.getDistanceNew());
			stmt.setString(6, routeDetailsDTO.getViaNew());
			stmt.setString(7, "T");
			stmt.setString(8, user);
			stmt.setString(9, routeDetailsDTO.getOriginNew() + "-" + routeDetailsDTO.getDestinationNew());

			int data = stmt.executeUpdate();

			if (data > 0) {
				flag = true;
			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return flag;
	}

	@Override
	public SurveyDTO fillRouteDetailsFromRouteNo(String routeNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		SurveyDTO surveyDTO = new SurveyDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT a.rou_service_origine,a.rou_service_destination,a.rou_via, a.rou_tot_busfare FROM public.nt_r_route a where a.rou_number=?; ";

			ps = con.prepareStatement(query);
			ps.setString(1, routeNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				surveyDTO.setOrigin(rs.getString("rou_service_origine"));
				surveyDTO.setDestination(rs.getString("rou_service_destination"));
				surveyDTO.setVia(rs.getString("rou_via"));
				surveyDTO.setBusFare(rs.getBigDecimal("rou_tot_busfare"));
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
	public List<SurveyDTO> getRequestTypeToDropDown() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SurveyDTO> requstTypeList = new ArrayList<SurveyDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "Select code, description FROM public.nt_r_request_types where active='A';";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				SurveyDTO surveyDTO = new SurveyDTO();
				surveyDTO.setRequestType(rs.getString("code"));
				surveyDTO.setRequestTypeDescription(rs.getString("description"));

				requstTypeList.add(surveyDTO);
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

	@Override
	public boolean updateSurveyRequestData(SurveyDTO surveyMgtDTO, String user) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Boolean success = false;
		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_initiate_survey SET " + " ini_organisation_code=?,ini_department_code=?, "
					+ " ini_request_type=?, ini_isroute_available=?, ini_routeno=?, ini_surveyreason=?, ini_address=?, "
					+ " ini_address_sin=?, ini_address_tamil=?, ini_new_route=?,ini_service_type=?,ini_one_way_fare=?, "
					+ " ini_modified_by=?,ini_modified_date=?  " + " WHERE ini_isu_requestno=? and ini_status='I' ";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, surveyMgtDTO.getOrganisationCode());
			stmt.setString(2, surveyMgtDTO.getDepartmentCode());
			stmt.setString(3, surveyMgtDTO.getRequestType());
			if (surveyMgtDTO.getRouteAvailable() != null && surveyMgtDTO.getRouteAvailable().trim().equals("true")) {
				stmt.setString(4, "Y");
			} else if (surveyMgtDTO.getRouteAvailable() != null
					&& surveyMgtDTO.getRouteAvailable().trim().equals("false")) {
				stmt.setString(4, "N");
			} else {
				stmt.setString(4, "");
			}
			stmt.setString(5, surveyMgtDTO.getRouteNo());
			stmt.setString(6, surveyMgtDTO.getSurveyReason());
			stmt.setString(7, surveyMgtDTO.getAddressEnglish());
			stmt.setString(8, surveyMgtDTO.getAddressSinhala());
			stmt.setString(9, surveyMgtDTO.getAddressTamil());
			stmt.setString(10, surveyMgtDTO.getRouteNoNew());
			stmt.setString(11, surveyMgtDTO.getServiceType());
			stmt.setBigDecimal(12, surveyMgtDTO.getBusFare());
			stmt.setString(13, user);
			java.util.Date date = new java.util.Date();
			Timestamp timestamp = new Timestamp(date.getTime());
			stmt.setTimestamp(14, timestamp);
			stmt.setString(15, surveyMgtDTO.getRequestNo());

			int data = stmt.executeUpdate();

			if (data > 0) {
				success = true;
			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return success;
	}

	@Override
	public boolean isDuplicateRouteNo(SurveyDTO routeDetailsDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Boolean isDuplicate = false;
		try {
			con = ConnectionManager.getConnection();

			String query = "Select rou_number FROM public.nt_r_route WHERE rou_number=? ;";
			ps = con.prepareStatement(query);
			ps.setString(1, routeDetailsDTO.getRouteNoNew());
			rs = ps.executeQuery();

			while (rs.next()) {
				isDuplicate = true;
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isDuplicate;
	}

	@Override
	public List<SurveyDTO> getApprandRejDropDown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SurveyDTO> requstNoList = new ArrayList<SurveyDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "Select ini_isu_requestno, ini_surveyno, ini_request_date From public.nt_t_initiate_survey Where ini_status='A' or ini_status='R' order by ini_isu_requestno;";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				SurveyDTO surveyDTO = new SurveyDTO();
				surveyDTO.setRequestNo(rs.getString("ini_isu_requestno"));
				surveyDTO.setSurveyNo(rs.getString("ini_surveyno"));
				surveyDTO.setRequestDate(rs.getString("ini_request_date"));

				requstNoList.add(surveyDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return requstNoList;

	}

	public boolean saveTempRouteDetails(SurveyDTO routeDetailsDTO, String user) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Boolean flag = false;

		try {
			con = ConnectionManager.getConnection();

			java.util.Date date = new java.util.Date();
			Timestamp timestamp = new Timestamp(date.getTime());

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_r_route");

			String sql = "INSERT INTO public.nt_r_route "
					+ "(seqno, rou_number, rou_service_origine, rou_service_destination, rou_distance, rou_via, active, created_by, rou_description, created_date) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?); ";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, routeDetailsDTO.getRouteNoNew());
			stmt.setString(3, routeDetailsDTO.getOriginNew());
			stmt.setString(4, routeDetailsDTO.getDestinationNew());
			stmt.setInt(5, routeDetailsDTO.getDistanceNew());
			stmt.setString(6, routeDetailsDTO.getViaNew());
			stmt.setString(7, "T");
			stmt.setString(8, user);
			stmt.setString(9, routeDetailsDTO.getOriginNew() + "-" + routeDetailsDTO.getDestinationNew());
			stmt.setTimestamp(10, timestamp);

			int data = stmt.executeUpdate();

			if (data > 0) {
				flag = true;
			}
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return flag;
	}

	/**
	 * This method provide survey numbers. condition : status = "A" :
	 * public.nt_t_initiate_survey table
	 * 
	 * @return List surveyNoList: SurveyDTO (surveyNo)
	 * 
	 */
	@Override
	public List<SurveyDTO> getApprovedSurveyNoDropDown() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SurveyDTO> surveyNoList = new ArrayList<SurveyDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "Select ini_surveyno " + "From public.nt_t_initiate_survey "
					+ "WHERE ini_status='A' AND ini_isapproved='Y' AND ini_surveyno IS NOT NULL "
					+ "ORDER BY ini_surveyno;";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				SurveyDTO surveyDTO = new SurveyDTO();
				surveyDTO.setSurveyNo(rs.getString("ini_surveyno"));
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

	/**
	 * This method provide locations. condition : active = ‘Y’ :public.nt_r_location
	 * Table
	 * 
	 * @return List locationList : SurveyDTO (location)
	 * 
	 */
	@Override
	public List<SurveyDTO> getLocationDropDown() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SurveyDTO> locationList = new ArrayList<SurveyDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code,description FROM public.nt_r_location WHERE active='A'";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				SurveyDTO surveyDTO = new SurveyDTO();
				surveyDTO.setCode(rs.getString("code"));
				surveyDTO.setLocation(rs.getString("description"));
				locationList.add(surveyDTO);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return locationList;

	}

	/**
	 * This method provide Survey Type, Survey Method related to Survey No
	 * 
	 * @param surveyNo
	 * @return SurveyDTO surveyDTO :
	 *         (surveyNo,surveyType,surveyMethod,departmentCode,methodCode)
	 * 
	 */
	public SurveyDTO getDetailsByServeyNo(String surveyNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		SurveyDTO surveyDTO = new SurveyDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT A.ini_surveyno AS ini_surveyno,A.ini_survey_type AS ini_survey_type,A.ini_department_code AS ini_department_code,A.ini_survey_method AS ini_survey_method_Code,A.int_cost_est_status AS approveStatus,A.ini_isu_requestno AS requestNo,A.int_cost_esti_remark as remark,"
					+ " B.description AS tyDescription,C.description AS meDescription,D.tsd_task_code AS taskCode,D.tsd_request_no AS surveyReqNo FROM"
					+ " public.nt_t_initiate_survey A \r\n"
					+ "INNER JOIN public.nt_r_survey_types B on A.ini_survey_type = B.code\r\n"
					+ "INNER JOIN public.nt_r_survey_methods C ON A.ini_survey_method = C.code\r\n"
					+ "INNER join public.nt_t_survey_task_det D ON A.ini_surveyno = D.tsd_survey_no\r\n"
					+ "WHERE A.ini_surveyno = ? ";
			ps = con.prepareStatement(query);
			ps.setString(1, surveyNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				surveyDTO.setSurveyNo(rs.getString("ini_surveyno"));
				if (rs.getString("approveStatus") != null) {
					surveyDTO.setStatus(rs.getString("approveStatus"));
				} else {
					surveyDTO.setStatus("N");
				}
				surveyDTO.setRequestNo(rs.getString("requestNo"));
				surveyDTO.setSurveyType(rs.getString("tyDescription"));
				surveyDTO.setSurveyMethod(rs.getString("meDescription"));
				surveyDTO.setDepartmentCode(rs.getString("ini_department_code"));
				surveyDTO.setSurveyMethodCode(rs.getString("ini_survey_method_Code"));
				surveyDTO.setSurveyTypeCode(rs.getString("taskCode"));
				surveyDTO.setRemarks(rs.getString("remark"));
				surveyDTO.setRequestNo(rs.getString("surveyReqNo"));

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

	/**
	 * This method add all values to the public.nt_t_survey_location table
	 * 
	 * @param SurveyDTO surveyDTO ()
	 */
	@Override
	public void addLoactionTime(SurveyDTO surveyDTO) {
		Connection con = null;
		PreparedStatement ps = null;

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat timeFormat = new SimpleDateFormat("HH:mm");
		Date today = new Date();
		String dateStr = dateFormat.format(today);

		java.util.Date date;
		Timestamp timestamp = null;

		try {

			date = dateFormat.parse(dateStr);
			timestamp = new Timestamp(date.getTime());

			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_t_survey_location");

			String query = "INSERT INTO public.nt_t_survey_location\r\n"
					+ "(sul_seq, sul_survey_no, sul_survey_type, sul_survey_method, sul_location, ini_department_code, sul_start_date, sul_end_date, sul_start_time, sul_end_time, sul_created_by, sul_created_date, sul_modified_by, sul_modified_date)\r\n"
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			ps = con.prepareStatement(query);
			ps.setLong(1, seqNo);
			ps.setString(2, surveyDTO.getSurveyNo());
			ps.setString(3, surveyDTO.getSurveyType());
			ps.setString(4, surveyDTO.getSurveyMethodCode());
			ps.setString(5, surveyDTO.getLocation());
			ps.setString(6, surveyDTO.getDepartmentCode());

			String getStartDate = (dateFormat.format(surveyDTO.getStartDate()));
			ps.setString(7, getStartDate);

			String getEndDate = (dateFormat.format(surveyDTO.getEndDate()));
			ps.setString(8, getEndDate);

			String getStartTime = (timeFormat.format(surveyDTO.getStartTime()));
			ps.setString(9, getStartTime);

			String getEndTime = (timeFormat.format(surveyDTO.getEndTime()));
			ps.setString(10, getEndTime);

			ps.setString(11, surveyDTO.getLoginUser());
			ps.setTimestamp(12, timestamp);
			ps.setString(13, surveyDTO.getLoginUser());
			ps.setTimestamp(14, timestamp);

			ps.executeUpdate();

			con.commit();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

	}

	/**
	 * This method retrieve data for location time table
	 * 
	 * @return List retriveTableData : SurveyDTO
	 *         (location,startDate,startTime,endDate,endTime)
	 */
	@Override
	public List<SurveyDTO> getLocationTimeTable(String surveyNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SurveyDTO> retriveTableData = new ArrayList<SurveyDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT A.sul_seq AS locationSeqNo,A.sul_start_date AS startDate,A.sul_start_time AS startTime, A.sul_end_date AS endDate,A.sul_end_time AS endTime,B.description AS location "
					+ "FROM public.nt_t_survey_location A "
					+ "INNER JOIN public.nt_r_location B on A.sul_location = B.code " + "WHERE A.sul_survey_no = ? "
					+ "ORDER BY locationSeqNo ;";

			ps = con.prepareStatement(query);

			ps.setString(1, surveyNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				SurveyDTO surveyDTO = new SurveyDTO();
				surveyDTO.setLocationSeqNo(String.valueOf(rs.getString("locationSeqNo")));
				surveyDTO.setLocation(rs.getString("location"));
				surveyDTO.setStrStartDate(rs.getString("startDate"));
				surveyDTO.setStrStartTime(rs.getString("startTime"));
				surveyDTO.setStrEndDate(rs.getString("endDate"));
				surveyDTO.setStrEndTime(rs.getString("endTime"));
				
				retriveTableData.add(surveyDTO);

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

	/**
	 * This method provide user details condition:emp_status = ‘A’:nt_m_employee
	 * Table
	 * 
	 * @return List userIdList : EmployeeDTO (userId,fullName)
	 */
	@Override
	public List<EmployeeDTO> getUserIdDropDown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<EmployeeDTO> userIdList = new ArrayList<EmployeeDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT emp_user_id FROM public.nt_m_employee WHERE emp_status='A' AND emp_user_id IS NOT NULL  ORDER BY emp_user_id";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				EmployeeDTO employeeDTO = new EmployeeDTO();
				employeeDTO.setUserId(rs.getString("emp_user_id"));
				userIdList.add(employeeDTO);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return userIdList;
	}

	/**
	 * This method returns full name related to user id
	 * 
	 * @param String userId
	 * @return fullName
	 */
	@Override
	public String returnName(String userId) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String fullName = "Name not found.";

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT emp_fullname FROM public.nt_m_employee WHERE emp_user_id = ? ";
			ps = con.prepareStatement(query);
			ps.setString(1, userId);
			rs = ps.executeQuery();

			while (rs.next()) {
				fullName = rs.getString("emp_fullname");
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return fullName;
	}

	/**
	 * This method provide list of organizations condition : active = ‘Y’:
	 * nt_r_organization Table
	 * 
	 * @return List organizationList : SurveyDTO (code,description)
	 */
	@Override
	public List<SurveyDTO> getOrganizationListDropDown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SurveyDTO> organizationList = new ArrayList<SurveyDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code,description FROM nt_r_organization WHERE active='A'";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				SurveyDTO surveyDTO = new SurveyDTO();
				surveyDTO.setOrganisationCode(rs.getString("code"));
				surveyDTO.setOrganisationDescription(rs.getString("description"));
				organizationList.add(surveyDTO);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return organizationList;

	}

	/**
	 * This method provides list of banks
	 * 
	 * @return List bankList : GenerateReceiptDTO (code,description)
	 */
	@Override
	public List<GenerateReceiptDTO> getBankListDropDown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<GenerateReceiptDTO> bankList = new ArrayList<GenerateReceiptDTO>();

		try {
			con = ConnectionManager.getConnection();
			String query = "SELECT code,description FROM public.nt_r_bank WHERE active='A'";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				GenerateReceiptDTO generateReceiptDTO = new GenerateReceiptDTO();
				generateReceiptDTO.setBankCode(rs.getString("code"));
				generateReceiptDTO.setBankDescription(rs.getString("description"));
				bankList.add(generateReceiptDTO);
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

	public List<GenerateReceiptDTO> getBranchListDropDown(String bankCode) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<GenerateReceiptDTO> bankBranchList = new ArrayList<GenerateReceiptDTO>();

		try {
			con = ConnectionManager.getConnection();
			String query = "SELECT branch_code,branch_name FROM public.nt_r_bank_branch WHERE bank_code =? AND active = 'A' ORDER BY bank_code";
			ps = con.prepareStatement(query);
			ps.setString(1, bankCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				GenerateReceiptDTO generateReceiptDTO = new GenerateReceiptDTO();
				generateReceiptDTO.setBranchCode(rs.getString("branch_code"));
				generateReceiptDTO.setBranchDescription(rs.getString("branch_name"));
				generateReceiptDTO.setBranchCodeDescription(generateReceiptDTO.getBranchCode() + " - "+generateReceiptDTO.getBranchDescription());
				bankBranchList.add(generateReceiptDTO);
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

	/**
	 * This method provides list of responsibilities condition : active = ‘Y’ :
	 * nt_r_responsibilities table
	 * 
	 */
	@Override
	public List<SurveyLocationTeamDTO> getResponsibilitiesList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SurveyLocationTeamDTO> responsibilitiesList = new ArrayList<SurveyLocationTeamDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code,description FROM public.nt_r_responsibilities WHERE active='A'";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				SurveyLocationTeamDTO surveyLocationTeamDTO = new SurveyLocationTeamDTO();
				surveyLocationTeamDTO.setResposibilitiesCode(rs.getString("code"));
				surveyLocationTeamDTO.setResposibilitiesDescription(rs.getString("description"));
				responsibilitiesList.add(surveyLocationTeamDTO);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return responsibilitiesList;

	}

	public GenerateReceiptDTO getBranchListByCodeDropDown(String branchCode, String bankCode) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		GenerateReceiptDTO bankBranchDTO = new GenerateReceiptDTO();

		try {
			con = ConnectionManager.getConnection();
			String query = "SELECT branch_code,branch_name FROM public.nt_r_bank_branch WHERE bank_code = ? AND branch_code= ?  AND active = 'A' ORDER BY bank_code";
			ps = con.prepareStatement(query);
			ps.setString(1, bankCode);
			ps.setString(2, branchCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				bankBranchDTO.setBranchCode(rs.getString("branch_code"));
				bankBranchDTO.setBranchDescription(rs.getString("branch_name"));
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return bankBranchDTO;
	}

	/**
	 * This method provides list of branches related to bank
	 * 
	 * @param bankCode
	 */
	@Override
	public List<GenerateReceiptDTO> getBankBranchDropDown(String bankCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<GenerateReceiptDTO> bankBranchList = new ArrayList<GenerateReceiptDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT branch_code,branch_name FROM public.nt_r_bank_branch WHERE bank_code=? and active='A' ";
			ps = con.prepareStatement(query);
			ps.setString(1, bankCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				GenerateReceiptDTO generateReceiptDTO = new GenerateReceiptDTO();
				generateReceiptDTO.setBranchCode(rs.getString("branch_code"));
				generateReceiptDTO.setBranchDescription(rs.getString("branch_name"));
				generateReceiptDTO.setBranchCodeDescription(generateReceiptDTO.getBranchCode()+" - " +generateReceiptDTO.getBranchDescription());
				bankBranchList.add(generateReceiptDTO);
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

	/**
	 * This method add all values to the public.nt_t_survey_loc_member
	 * 
	 * @param surveyDTO
	 * @param surveyLocationTeamDTO
	 */
	@Override
	public void addTeamByLocation(SurveyDTO surveyDTO, SurveyLocationTeamDTO surveyLocationTeamDTO) {

		Connection con = null;
		PreparedStatement ps = null;

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date today = new Date();
		String dateStr = dateFormat.format(today);

		java.util.Date date;
		Timestamp timestamp = null;

		try {

			date = dateFormat.parse(dateStr);
			timestamp = new Timestamp(date.getTime());

			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_t_survey_loc_member");

			String query = "INSERT INTO public.nt_t_survey_loc_member\r\n"
					+ "(slm_seq, slm_survey_no, slm_loc_seq, slm_organization, slm_user_id, slm_name, slm_nic, slm_responsibility, slm_bank, slm_branch,"
					+ "slm_account_no, slm_created_by, slm_created_date, slm_modified_by, slm_modified_date)\r\n"
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
			ps = con.prepareStatement(query);
			ps.setLong(1, seqNo);
			ps.setString(2, surveyDTO.getSurveyNo());
			ps.setInt(3, Integer.parseInt(surveyDTO.getLocationSeqNo()));
			ps.setString(4, surveyLocationTeamDTO.getOrganization());
			ps.setString(5, surveyLocationTeamDTO.getUserId());
			ps.setString(6, surveyLocationTeamDTO.getFullName());
			ps.setString(7, surveyLocationTeamDTO.getNic());
			ps.setString(8, surveyLocationTeamDTO.getResponsibilities());

			if (surveyLocationTeamDTO.getBank() != null && !surveyLocationTeamDTO.getBank().isEmpty()
					&& !surveyLocationTeamDTO.getBank().equalsIgnoreCase("")) {
				ps.setString(9, surveyLocationTeamDTO.getBank());
			} else {
				ps.setNull(9, java.sql.Types.VARCHAR);
			}

			if (surveyLocationTeamDTO.getBranch() != null && !surveyLocationTeamDTO.getBranch().isEmpty()
					&& !surveyLocationTeamDTO.getBranch().equalsIgnoreCase("")) {
				ps.setString(10, surveyLocationTeamDTO.getBranch());
			} else {
				ps.setNull(10, java.sql.Types.VARCHAR);
			}

			if (surveyLocationTeamDTO.getAccountNo() != null && !surveyLocationTeamDTO.getAccountNo().isEmpty()
					&& !surveyLocationTeamDTO.getAccountNo().equalsIgnoreCase("")) {
				ps.setString(11, surveyLocationTeamDTO.getAccountNo());
			} else {
				ps.setNull(11, java.sql.Types.VARCHAR);
			}

			ps.setString(12, surveyDTO.getLoginUser());
			ps.setTimestamp(13, timestamp);
			ps.setString(14, surveyDTO.getLoginUser());
			ps.setTimestamp(15, timestamp);

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
	public List<SurveyLocationTeamDTO> getTeamDetailsTableList(SurveyDTO surveyDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SurveyLocationTeamDTO> retriveTableData = new ArrayList<SurveyLocationTeamDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT A.slm_seq as slm_seq,A.slm_organization as slm_organization, A.slm_user_id as slm_user_id, A.slm_name as slm_name, A.slm_nic as slm_nic, A.slm_responsibility as slm_responsibility, \r\n"
					+ "B.description as organizationName, C.description as res_Des \r\n"
					+ "FROM public.nt_t_survey_loc_member A\r\n"
					+ "inner join public.nt_r_organization B on A.slm_organization = B.code inner join public.nt_r_responsibilities C on A.slm_responsibility=C.code\r\n"
					+ "WHERE slm_survey_no = ? and slm_loc_seq = ?  \r\n" + "ORDER BY slm_seq ;";

			ps = con.prepareStatement(query);
			ps.setString(1, surveyDTO.getSurveyNo());
			ps.setInt(2, Integer.parseInt(surveyDTO.getLocationSeqNo()));
			rs = ps.executeQuery();

			while (rs.next()) {
				SurveyLocationTeamDTO surveyLocationTeamDTO = new SurveyLocationTeamDTO();
				surveyLocationTeamDTO.setMemberSeqNo(rs.getString("slm_seq"));
				surveyLocationTeamDTO.setOrganization(rs.getString("organizationName"));
				surveyLocationTeamDTO.setUserId(rs.getString("slm_user_id"));
				surveyLocationTeamDTO.setFullName(rs.getString("slm_name"));
				surveyLocationTeamDTO.setNic(rs.getString("slm_nic"));
				surveyLocationTeamDTO.setResponsibilities(rs.getString("slm_responsibility"));
				surveyLocationTeamDTO.setResposibilitiesDescription(rs.getString("res_Des"));
				retriveTableData.add(surveyLocationTeamDTO);

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
	public void deleteRawFromLocationTable(String locationSeqNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "DELETE FROM public.nt_t_survey_location WHERE sul_seq = ? ";

			ps = con.prepareStatement(query);
			ps.setLong(1, Integer.parseInt(locationSeqNo));
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
	public void deleteRawFromTeamDetailsTable(String memberSeqNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "DELETE FROM public.nt_t_survey_loc_member WHERE slm_seq = ? ";

			ps = con.prepareStatement(query);
			ps.setLong(1, Integer.parseInt(memberSeqNo));
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
	public void deleteTeamDetailsByLocation(String locationSeqNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "DELETE FROM public.nt_t_survey_loc_member WHERE slm_loc_seq = ? ";

			ps = con.prepareStatement(query);
			ps.setLong(1, Integer.parseInt(locationSeqNo));
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
	public List<String> getCostCodeDropDown() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<String> retriveCostData = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code " + "FROM public.nt_r_survey_cost " + "WHERE active = 'A' " + "ORDER BY code ;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				
				retriveCostData.add(rs.getString("code"));
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return retriveCostData;
	}

	@Override
	public String getCodeDescription(String costCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String description = "";

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT description " + "FROM public.nt_r_survey_cost " + "WHERE code = ? ";

			ps = con.prepareStatement(query);
			ps.setString(1, costCode);
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

	@Override
	public void addCostEstimation(SurveyLocationTeamDTO surveyLocationTeamDTO, SurveyDTO surveyDTO) {

		Connection con = null;
		PreparedStatement ps = null;

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date today = new Date();
		String dateStr = dateFormat.format(today);

		java.util.Date date;
		Timestamp timestamp = null;

		try {

			date = dateFormat.parse(dateStr);
			timestamp = new Timestamp(date.getTime());

			con = ConnectionManager.getConnection();

			long ced_seq = Utils.getNextValBySeqName(con, "seq_nt_t_cost_estimation_det");


			String query = "INSERT INTO public.nt_t_cost_estimation_det\r\n"
					+ "(ced_seq,ced_survey_no,ced_code, ced_description, ced_amount, coe_created_by, coe_created_date, coe_modified_by, coe_modified_date)\r\n"
					+ "VALUES(?,?,?,?,?,?,?,?,?);";
			ps = con.prepareStatement(query);
			ps.setLong(1, ced_seq);
			ps.setString(2, surveyDTO.getSurveyNo());
			ps.setString(3, surveyLocationTeamDTO.getSelectedCostCode());
			ps.setString(4, surveyLocationTeamDTO.getCostCodeDescription());
			ps.setBigDecimal(5, surveyLocationTeamDTO.getAmount());
			ps.setString(6, surveyDTO.getLoginUser());
			ps.setTimestamp(7, timestamp);
			ps.setString(8, surveyDTO.getLoginUser());
			ps.setTimestamp(9, timestamp);

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
	public List<SurveyLocationTeamDTO> getCostEstimationTableList(SurveyDTO surveyDTO) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SurveyLocationTeamDTO> retriveTableData = new ArrayList<SurveyLocationTeamDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT ced_seq, ced_survey_no, ced_code, ced_description, ced_amount \r\n"
					+ "FROM public.nt_t_cost_estimation_det " + "WHERE ced_survey_no = ? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, surveyDTO.getSurveyNo());
			rs = ps.executeQuery();

			while (rs.next()) {

				SurveyLocationTeamDTO surveyLocationTeamDTO = new SurveyLocationTeamDTO();
				surveyLocationTeamDTO.setCostCodeSeqNo(rs.getString("ced_seq"));
				surveyLocationTeamDTO.setCostCode(rs.getString("ced_code"));
				surveyLocationTeamDTO.setCostCodeDescription(rs.getString("ced_description"));
				surveyLocationTeamDTO.setAmount(rs.getBigDecimal("ced_amount"));

				retriveTableData.add(surveyLocationTeamDTO);

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

	public BigDecimal getTotal(SurveyDTO surveyDTO) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		BigDecimal total = null;

		try {

			con = ConnectionManager.getConnection();
			String query = "SELECT SUM(ced_amount) AS total " + "FROM public.nt_t_cost_estimation_det "
					+ "WHERE ced_survey_no = ? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, surveyDTO.getSurveyNo());
			rs = ps.executeQuery();
			while (rs.next()) {
				total = rs.getBigDecimal("total");
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return total;
	}

	@Override
	public void deleteRawFromCostEstimateTable(String seqNO) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "DELETE FROM public.nt_t_cost_estimation_det WHERE ced_seq = ? ";

			ps = con.prepareStatement(query);
			ps.setLong(1, Integer.parseInt(seqNO));
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
	public void updateTaskOnDetTable(String surveyNo) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "update public.nt_t_survey_task_det \r\n"
					+ "SET tsd_task_code = 'SU005' , tsd_status = 'O'  \r\n" + "WHERE tsd_survey_no = ?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, surveyNo);
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

	// notDone - change query
	@Override
	public void deleteTaskOnDetTable(String surveyNo) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "update public.nt_t_survey_task_det \r\n"
					+ "SET tsd_task_code = 'SU005' , tsd_status = 'O'  \r\n" + "WHERE tsd_survey_no = ?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, surveyNo);
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

	// notDone - change query
	@Override
	public void updateTaskOnHisTable(String surveyNo) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "update public.nt_t_survey_task_det \r\n"
					+ "SET tsd_task_code = 'SU005' , tsd_status = 'O'  \r\n" + "WHERE tsd_survey_no = ?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, surveyNo);
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
	public void approveRejectCostEstimation(String surveyNo, String isApproved,
			SurveyLocationTeamDTO surveyLocationTeamDTO) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "update public.nt_t_initiate_survey \r\n"
					+ "SET int_cost_est_status = ?,int_cost_esti_tot_cost = ?,int_cost_esti_remark = ? \r\n"
					+ "WHERE ini_surveyno = ?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, isApproved);
			stmt.setBigDecimal(2, surveyLocationTeamDTO.getTotalAmount());
			stmt.setString(3, surveyLocationTeamDTO.getSpecialRemark());
			stmt.setString(4, surveyNo);
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

	public String getCostEstimationApproveStatus(String surveyNo) {
		String approveStatus = "notapproved";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();
			String query = "SELECT int_cost_est_status " + "FROM public.nt_t_initiate_survey "
					+ "WHERE ini_surveyno = ? AND int_cost_est_status IS NOT NULL ;";

			ps = con.prepareStatement(query);
			ps.setString(1, surveyNo);
			rs = ps.executeQuery();
			while (rs.next()) {
				approveStatus = rs.getString("int_cost_est_status");
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return approveStatus;
	}

	@Override
	public List<TrafficProposalDTO> getRequestNoList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<TrafficProposalDTO> returnList = new ArrayList<TrafficProposalDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT tsd_request_no FROM public.nt_t_survey_task_det where (tsd_task_code='SU008' and tsd_status='C') or (tsd_task_code='SU009' and tsd_status='O') or (tsd_task_code='SU011' and tsd_status='C')  or (tsd_task_code='SU012' and tsd_status='C')  order by tsd_request_no;";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				TrafficProposalDTO trafficProposalDTO = new TrafficProposalDTO();
				trafficProposalDTO.setRequestNo(rs.getString("tsd_request_no"));
				returnList.add(trafficProposalDTO);

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
	public TrafficProposalDTO displayValuesForSelectedReqNo(String requestNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		TrafficProposalDTO trafficProposalDTO = new TrafficProposalDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  a.ini_surveyno as surveyno ,a.ini_survey_type as survey_type_code,a.ini_survey_method as survey_method_code, b.description as survey_type_des, c.description as survey_method_des FROM public.nt_t_initiate_survey a, public.nt_r_survey_types b, public.nt_r_survey_methods c where a.ini_isu_requestno=? and b.code=a.ini_survey_type and c.code=a.ini_survey_method;";
			ps = con.prepareStatement(query);
			ps.setString(1, requestNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				trafficProposalDTO.setSurveyNo(rs.getString("surveyno"));
				trafficProposalDTO.setSurveyTypeCode(rs.getString("survey_type_code"));
				trafficProposalDTO.setSurveyMethodCode(rs.getString("survey_method_code"));
				trafficProposalDTO.setSurveyTypeDes(rs.getString("survey_type_des"));
				trafficProposalDTO.setSurveyMethodDes(rs.getString("survey_method_des"));
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
	public List<TrafficProposalDTO> getRouteDetailsForSelectedSurveyNo(String selectedSurveyNo,
			String selectedRequestNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<TrafficProposalDTO> returnList = new ArrayList<TrafficProposalDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT seqno, srd_survey_no, srd_route_no, srd_origin, srd_destination, srd_via,  srd_effective_route, srd_no_of_permits FROM public.nt_t_survey_route_detail where srd_survey_no=? order by srd_route_no;";
			ps = con.prepareStatement(query);
			ps.setString(1, selectedSurveyNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				TrafficProposalDTO trafficProposalDTO = new TrafficProposalDTO();
				trafficProposalDTO.setSurvey_route_seq(rs.getLong("seqno"));
				trafficProposalDTO.setSurveyNo(rs.getString("srd_survey_no"));
				trafficProposalDTO.setRouteNo(rs.getString("srd_route_no"));
				trafficProposalDTO.setOrigin(rs.getString("srd_origin"));
				trafficProposalDTO.setDestination(rs.getString("srd_destination"));
				trafficProposalDTO.setVia(rs.getString("srd_via"));
				trafficProposalDTO.setEffectRoute(rs.getString("srd_effective_route"));
				trafficProposalDTO.setNoOfPermitsReqToIssue(rs.getInt("srd_no_of_permits"));
				returnList.add(trafficProposalDTO);

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
	public int updateRouteDet(TrafficProposalDTO selectedRow) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_survey_route_detail SET  srd_no_of_permits=?, srd_modify_by=?, srd_modify_date=? where seqno=? and srd_survey_no=? and srd_route_no=?;";

			stmt = con.prepareStatement(sql);
			stmt.setInt(1, selectedRow.getNoOfPermitsReqToIssue());
			stmt.setString(2, selectedRow.getModifyBy());
			stmt.setTimestamp(3, timestamp);
			stmt.setLong(4, selectedRow.getSurvey_route_seq());
			stmt.setString(5, selectedRow.getSurveyNo());
			stmt.setString(6, selectedRow.getRouteNo());
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

	@Override
	public boolean checkTaskCodeForCurrentSurveyNo(String selectedRequestNo, String selectedSurveyNo, String taskCode,
			String taskStatus) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isTaskAvailable = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT  tsd_survey_no, tsd_task_code, tsd_status FROM public.nt_t_survey_task_det where tsd_request_no=? and tsd_survey_no=? and tsd_task_code=? and tsd_status=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedRequestNo);
			ps.setString(2, selectedSurveyNo);
			ps.setString(3, taskCode);
			ps.setString(4, taskStatus);
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

	@Override
	public void insertTaskDetails(String selectedRequestNo, String selectedSurveyNo, String loginUser, String taskCode,
			String taskStatus) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_survey_task_det");

			String sql = "INSERT INTO public.nt_t_survey_task_det (tsd_seq, tsd_request_no, tsd_survey_no, tsd_task_code, tsd_status, created_by, created_date) VALUES(?, ?, ?, ?, ?, ?, ?);";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, selectedRequestNo);
			stmt.setString(3, selectedSurveyNo);
			stmt.setString(4, taskCode);
			stmt.setString(5, taskStatus);
			stmt.setString(6, loginUser);
			stmt.setTimestamp(7, timestamp);

			stmt.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
		return;
	}

	@Override
	public boolean checkTaskDetails(String selectedRequestNo, String selectedSurveyNo, String taskCode,
			String taskStatus) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isTaskAvailable = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT  tsd_survey_no, tsd_task_code, tsd_status FROM public.nt_t_survey_task_det where tsd_request_no=? and tsd_survey_no=? and tsd_task_code=? and tsd_status=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedRequestNo);
			ps.setString(2, selectedSurveyNo);
			ps.setString(3, taskCode);
			ps.setString(4, taskStatus);
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

	@Override
	public boolean CopyTaskDetailsANDinsertTaskHistory(String selectedRequestNo, String selectedSurveyNo,
			String loginUser, String taskCode) {
		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		TrafficProposalDTO trafficProposalDTO = null;

		boolean isUpdate = false;

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT tsd_seq, tsd_request_no, tsd_survey_no, tsd_task_code, tsd_status, created_by, created_date FROM public.nt_t_survey_task_det where tsd_request_no=? and tsd_survey_no=? and tsd_task_code=? ;";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, selectedRequestNo);
			stmt.setString(2, selectedSurveyNo);
			stmt.setString(3, taskCode);
			rs = stmt.executeQuery();

			if (rs.next()) {

				isUpdate = true;

				trafficProposalDTO = new TrafficProposalDTO();
				trafficProposalDTO.setTaskSeq(rs.getLong("tsd_seq"));
				trafficProposalDTO.setRequestNo(rs.getString("tsd_request_no"));
				trafficProposalDTO.setSurveyNo(rs.getString("tsd_survey_no"));
				trafficProposalDTO.setTaskCode(rs.getString("tsd_task_code"));
				trafficProposalDTO.setTaskStatus(rs.getString("tsd_status"));
				trafficProposalDTO.setCreatedBy(rs.getString("created_by"));
				trafficProposalDTO.setCreateDate(rs.getTimestamp("created_date"));

				String sql2 = "INSERT INTO public.nt_h_survey_task_his (tsd_seq, tsd_request_no, tsd_survey_no, tsd_task_code, tsd_status, created_by, created_date) VALUES(?, ?, ?, ?, ?, ?, ?);";

				stmt2 = con.prepareStatement(sql2);

				stmt2.setLong(1, trafficProposalDTO.getTaskSeq());
				stmt2.setString(2, trafficProposalDTO.getRequestNo());
				stmt2.setString(3, trafficProposalDTO.getSurveyNo());
				stmt2.setString(4, trafficProposalDTO.getTaskCode());
				stmt2.setString(5, trafficProposalDTO.getTaskStatus());
				stmt2.setString(6, trafficProposalDTO.getCreatedBy());
				stmt2.setTimestamp(7, trafficProposalDTO.getCreateDate());

				stmt2.executeUpdate();

			} else {
				isUpdate = false;
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
		return isUpdate;
	}

	@Override
	public boolean deleteTaskDetails(String selectedRequestNo, String selectedSurveyNo, String taskCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isTaskPM101Delete = false;

		try {
			con = ConnectionManager.getConnection();

			String query = "DELETE FROM public.nt_t_survey_task_det WHERE tsd_request_no=? and tsd_survey_no=? and tsd_task_code=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedRequestNo);
			ps.setString(2, selectedSurveyNo);
			ps.setString(3, taskCode);
			int i = ps.executeUpdate();

			if (i > 0) {
				isTaskPM101Delete = true;
			} else {
				isTaskPM101Delete = false;
			}

			con.commit();

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isTaskPM101Delete;
	}

	@Override
	public String generateReferenceNo() {
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

			String sql = "SELECT  proposal_no FROM public.nt_t_traffic_proposal order by created_date desc limit 1;";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("proposal_no");
			}

			if (result != null) {

				lastyr = result.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String ApprecordcountN = "00001";
					strAppNo = "TPN" + currYear + ApprecordcountN;
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
					strAppNo = "TPN" + currYear + ApprecordcountN;
				}
			} else
				strAppNo = "TPN" + currYear + "00001";

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
	public int insertDataIntoTrafficProTb(TrafficProposalDTO trafficProposalDTO, String createdBy) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();
			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_traffic_proposal");
			String sql = "INSERT INTO public.nt_t_traffic_proposal (seqno, proposal_no, survey_no, summary_approve_remarks, trafic_pro_print_note_remark, trafic_pro_suggestions, created_by, created_date, status) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, trafficProposalDTO.getTrafficProposalNo());
			stmt.setString(3, trafficProposalDTO.getSurveyNo());
			stmt.setString(4, trafficProposalDTO.getSpecialRemark());
			stmt.setString(5, trafficProposalDTO.getSpecialPrintNote());
			stmt.setString(6, trafficProposalDTO.getSuggestions());
			stmt.setString(7, createdBy);
			stmt.setTimestamp(8, timestamp);
			stmt.setString(9, "O");

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

	@Override
	public int updateTaskCode(String selectedRequestNo, String selectedSurveyNo, String loginUser, String taskCode,
			String taskStatus) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_survey_task_det SET  tsd_status=? where tsd_request_no=? and tsd_survey_no=? and tsd_task_code=?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, taskStatus);
			stmt.setString(2, selectedRequestNo);
			stmt.setString(3, selectedSurveyNo);
			stmt.setString(4, taskCode);
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

	@Override
	public boolean checkTrafficProposalNo(String selectedRequestNo, String selectedSurveyNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isTaskAvailable = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT  proposal_no FROM public.nt_t_traffic_proposal where survey_no=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedSurveyNo);
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

	@Override
	public int updateDataInTrafficProTb(TrafficProposalDTO trafficProposalDTO, String createdBy) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_traffic_proposal SET  summary_approve_remarks=?, trafic_pro_print_note_remark=?, trafic_pro_suggestions=?, modify_by=?, nodify_date=? where proposal_no=? and survey_no=?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, trafficProposalDTO.getSpecialRemark());
			stmt.setString(2, trafficProposalDTO.getSpecialPrintNote());
			stmt.setString(3, trafficProposalDTO.getSuggestions());
			stmt.setString(4, createdBy);
			stmt.setTimestamp(5, timestamp);
			stmt.setString(6, trafficProposalDTO.getTrafficProposalNo());
			stmt.setString(7, trafficProposalDTO.getSurveyNo());
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

	@Override
	public String getTrafficProposalNoForSelectedSurveyNo(String selectedSurveyNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT  proposal_no FROM public.nt_t_traffic_proposal where survey_no=?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, selectedSurveyNo);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("proposal_no");
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

	// for Survey Summary Details page
	@Override
	public List<SurveyDTO> getSurveySummarySurveyNo() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SurveyDTO> surveyNo = new ArrayList<SurveyDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct ini_surveyno "
					+ " from public.nt_t_initiate_survey inner join nt_t_survey_task_det on "
					+ " nt_t_initiate_survey.ini_surveyno = nt_t_survey_task_det.tsd_survey_no "
					+ " where (nt_t_survey_task_det.tsd_task_code='SU007' and nt_t_survey_task_det.tsd_status='C') or "
					+ " (nt_t_survey_task_det.tsd_task_code='SU008' and nt_t_survey_task_det.tsd_status='O')"
					+ " and ini_surveyno is not null order by ini_surveyno";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				SurveyDTO surveyDTO = new SurveyDTO();
				surveyDTO.setSurveyNo(rs.getString("ini_surveyno"));

				surveyNo.add(surveyDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return surveyNo;

	}

	@Override
	public SurveyDTO fillSurveyTypeMethod(String surveyNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		SurveyDTO surveyDTO = new SurveyDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "select a.ini_isu_requestno,a.ini_survey_method,a.ini_survey_type,b.description as type,c.description from nt_t_initiate_survey a,public.nt_r_survey_types b,public.nt_r_survey_methods c where a.ini_survey_type=b.code and a.ini_survey_method=c.code and ini_surveyno=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, surveyNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				surveyDTO.setRequestNo(rs.getString("ini_isu_requestno"));
				surveyDTO.setSurveyType(rs.getString("type"));
				surveyDTO.setSurveyMethod(rs.getString("description"));

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
	public List<SurveyDTO> showRouteDetails(String surveyNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SurveyDTO> returnList = new ArrayList<SurveyDTO>();
		String WHERE_SQL = "";

		if (surveyNo != null && !surveyNo.equals("")) {

			WHERE_SQL = WHERE_SQL + " WHERE srd_survey_no = " + "'" + surveyNo + "'";
		}

		try {
			con = ConnectionManager.getConnection();
			String query2 = " select  seqno,srd_route_no,srd_effective_route,srd_no_of_permits,srd_origin,srd_destination,srd_via from public.nt_t_survey_route_detail "
					+ WHERE_SQL;

			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();

			while (rs.next()) {
				SurveyDTO surveyDTO = new SurveyDTO();
				surveyDTO.setRouteNo(rs.getString("srd_route_no"));
				surveyDTO.setSeq(rs.getInt("seqno"));
				surveyDTO.setEffectiveRoute(rs.getString("srd_effective_route"));
				surveyDTO.setNoOfPermits(rs.getInt("srd_no_of_permits"));
				surveyDTO.setOrigin(rs.getString("srd_origin"));
				surveyDTO.setDestination(rs.getString("srd_destination"));
				surveyDTO.setVia(rs.getString("srd_via"));

				returnList.add(surveyDTO);
			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return returnList;
	}

	@Override
	public List<SurveyDTO> showAddDetails(SurveyDTO surveyDTO, String loginUser) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_survey_route_detail");
			String query2 = "INSERT INTO  public.nt_t_survey_route_detail (seqno,srd_survey_request_no,srd_survey_no,srd_route_no,srd_origin,srd_destination,srd_via,srd_effective_route,srd_no_of_permits,srd_created_by ,srd_created_date) VALUES(?,?,?,?,?,?,?,?,?,?,?) ; ";

			ps = con.prepareStatement(query2);
			ps.setLong(1, seqNo);
			ps.setString(2, surveyDTO.getRequestNo());
			ps.setString(3, surveyDTO.getSurveyNo());
			ps.setString(4, surveyDTO.getRouteNo());
			ps.setString(5, surveyDTO.getOrigin());
			ps.setString(6, surveyDTO.getDestination());
			ps.setString(7, surveyDTO.getVia());
			ps.setString(8, surveyDTO.getEffectiveRoute());
			ps.setInt(9, surveyDTO.getNoOfPermits());
			ps.setString(10, loginUser);
			ps.setTimestamp(11, timestamp);
			ps.executeUpdate();

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return null;
	}

	@Override
	public List<SurveyDTO> addDetailsToGrid(SurveyDTO surveyDTO) {

		Connection con = null;
		PreparedStatement pss = null;
		ResultSet rs = null;

		List<SurveyDTO> showDetails = new ArrayList<SurveyDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select srd_route_no,srd_origin,srd_destination,srd_via,srd_effective_route,srd_no_of_permits from public.nt_t_survey_route_detail where srd_survey_no=?; ";
			SurveyDTO e;
			pss = con.prepareStatement(query);
			pss.setString(1, surveyDTO.getSurveyNo());
			rs = pss.executeQuery();

			while (rs.next()) {
				e = new SurveyDTO();

				e.setRouteNo(rs.getString("srd_route_no"));
				e.setOrigin(rs.getString("srd_origin"));
				e.setDestination(rs.getString("srd_destination"));
				e.setVia(rs.getString("srd_via"));
				e.setEffectiveRoute(rs.getString("srd_effective_route"));
				e.setNoOfPermits(rs.getInt("srd_no_of_permits"));
				showDetails.add(e);

			}
			return showDetails;
		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(pss);
			ConnectionManager.close(con);
		}

	}

	@Override
	public List<SurveyDTO> getSurveyRouteNo(String surveyNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SurveyDTO> surveyRouteList = new ArrayList<SurveyDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct nt_t_indicator_values.indicator_val as sud_route_no "
					+ " from nt_m_form inner join nt_t_indicators on nt_m_form.form_id = nt_t_indicators.form_id "
					+ " inner join nt_t_indicator_values on nt_t_indicators.seqno = nt_t_indicator_values.indicators_seqno"
					+ " where nt_m_form.survey_no = ? " + " and nt_t_indicators.field_name = 'Route No.'";

			ps = con.prepareStatement(query);

			ps.setString(1, surveyNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				SurveyDTO surDTO = new SurveyDTO();

				surDTO.setRouteNo(rs.getString("sud_route_no"));
				surveyRouteList.add(surDTO);

			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return surveyRouteList;
	}

	@Override
	public List<SurveyDTO> updateSurveyData(SurveyDTO surveyDTO, String loginUser) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		String WHERE_SQL = "";

		if (surveyDTO.getSurveyNo() != null && !surveyDTO.getSurveyNo().equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE ini_surveyno = " + "'" + surveyDTO.getSurveyNo() + "'";
		}

		try {
			con = ConnectionManager.getConnection();
			String query2 = "Update  public.nt_t_initiate_survey SET survey_summary_remarks=?,ini_modified_by =?,ini_modified_date =?  "
					+ WHERE_SQL;

			ps = con.prepareStatement(query2);
			ps.setString(1, surveyDTO.getSpecialRemarks());
			ps.setString(2, loginUser);
			ps.setTimestamp(3, timestamp);
			ps.executeUpdate();

			if (ps != null) {
				ps.close();
			}
			con.commit();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return null;
	}

	public int getCurrentNoOfPermitsReqToIssue(String selectedSurveyNo, String selectedRouteNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int currentValue = 0;

		TrafficProposalDTO dto = new TrafficProposalDTO();

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT srd_no_of_permits FROM public.nt_t_survey_route_detail where srd_survey_no=? and srd_route_no=?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, selectedSurveyNo);
			stmt.setString(2, selectedRouteNo);
			rs = stmt.executeQuery();

			while (rs.next()) {
				dto.setNoOfPermitsReqToIssue(rs.getInt("srd_no_of_permits"));
				currentValue = dto.getNoOfPermitsReqToIssue();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return currentValue;
	}

	public List<CommitteeOrBoardApprovalDTO> getSurveyNoForCommitteeApproval() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommitteeOrBoardApprovalDTO> surveyNoList = new ArrayList<CommitteeOrBoardApprovalDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct tsd_survey_no FROM public.nt_t_survey_task_det where tsd_survey_no like 'SIP%' and (tsd_task_code='SU009' and tsd_status='C'\r\n"
					+ "or tsd_task_code='SU010' and tsd_status='O'\r\n"
					+ "or tsd_task_code='SU010' and tsd_status='C'\r\n"
					+ "or tsd_task_code='SU011' and tsd_status='O') \r\n" + "order by tsd_survey_no";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				CommitteeOrBoardApprovalDTO committeeApprovalDTO = new CommitteeOrBoardApprovalDTO();

				committeeApprovalDTO.setCommitteeSurveyNo(rs.getString("tsd_survey_no"));

				surveyNoList.add(committeeApprovalDTO);

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

	public List<CommitteeOrBoardApprovalDTO> getViewSurveyNoForCommitteeApprovalStatus() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommitteeOrBoardApprovalDTO> surveyNoList = new ArrayList<CommitteeOrBoardApprovalDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT ini_surveyno FROM public.nt_t_initiate_survey where ini_surveyno like 'SIP%' and (trp_is_committe_approve_proccess='A' or trp_is_board_approve='A') order by ini_surveyno ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommitteeOrBoardApprovalDTO committeeApprovalDTO = new CommitteeOrBoardApprovalDTO();

				committeeApprovalDTO.setCommitteeSurveyNo(rs.getString("ini_surveyno"));

				surveyNoList.add(committeeApprovalDTO);

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

	public String getSurveyType(String surveyNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String surveyType = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "select b.description  from nt_t_initiate_survey  a, nt_r_survey_types b\r\n"
					+ "where a.ini_survey_type = b.code and a.ini_surveyno='" + surveyNo + "'";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {

				surveyType = rs.getString("description");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return surveyType;

	}

	public String getSurveyMethod(String surveyNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String surveyMethod = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "select nt_r_survey_methods.description "
					+ " from nt_t_initiate_survey inner join nt_r_survey_methods on "
					+ " nt_t_initiate_survey.ini_survey_method = nt_r_survey_methods.code "
					+ " where nt_t_initiate_survey.ini_surveyno='" + surveyNo + "'";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {

				surveyMethod = rs.getString("description");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return surveyMethod;

	}

	public String getRequestNo(String surveyNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String requestNo = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT ini_isu_requestno FROM public.nt_t_initiate_survey where ini_surveyno='" + surveyNo
					+ "'";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {

				requestNo = rs.getString("ini_isu_requestno");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return requestNo;

	}

	public String getGamiRequestNo(String surveyNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String requestNo = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT gsi_survey_request_no FROM public.nt_m_gamisari_survey_initiate where gsi_survey_no='"
					+ surveyNo + "'";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {

				requestNo = rs.getString("gsi_survey_request_no");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return requestNo;

	}

	public String getTaskStatusForApproval(String requestNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String task = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT tsd_status FROM public.nt_h_survey_task_his where tsd_request_no='" + requestNo
					+ "' and tsd_task_code='SU010'";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {

				task = rs.getString("tsd_status");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return task;

	}

	public String getBoardTaskStatusForApproval(String requestNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String task = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT tsd_status FROM public.nt_h_survey_task_his where tsd_request_no='" + requestNo
					+ "' and tsd_task_code='SU011'";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {

				task = rs.getString("tsd_status");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return task;

	}

	public List<CommitteeOrBoardApprovalDTO> getTransactionTypeDropDown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommitteeOrBoardApprovalDTO> transaction = new ArrayList<CommitteeOrBoardApprovalDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT description FROM public.nt_r_transaction_type where code IN ('01','08','14','19','21','22','23','15','16') order by description ;";
			
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				CommitteeOrBoardApprovalDTO committeeOrBoardApprovalDTO = new CommitteeOrBoardApprovalDTO();
				committeeOrBoardApprovalDTO.setTransactionType(rs.getString("description"));
				transaction.add(committeeOrBoardApprovalDTO);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return transaction;

	}

	public String getRefNo(String transactionCode, String comType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String refNo = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT com_refno FROM public.nt_m_committee where com_type = '" + comType
					+ "' and com_transactiontype_code = '" + transactionCode + "' and com_status = 'A'";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {

				refNo = rs.getString("com_refno");

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

	public List<CommitteeOrBoardApprovalDTO> getDataToCommitteBoardApprovalList(String refNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		Date todaysDate = new Date();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String date = df.format(todaysDate);

		List<CommitteeOrBoardApprovalDTO> approvalList = new ArrayList<CommitteeOrBoardApprovalDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT cod_userid,cod_responsibility,cod_status,cod_created_date,cod_full_name FROM public.nt_t_committee_det where cod_committee_refno='"
					+ refNo + "'";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				CommitteeOrBoardApprovalDTO committeeOrBoardApprovalDTO = new CommitteeOrBoardApprovalDTO();

				committeeOrBoardApprovalDTO.setUserName(rs.getString("cod_userid"));

				if (committeeOrBoardApprovalDTO.getUserName() == null
						|| committeeOrBoardApprovalDTO.getUserName().equals("")) {

					committeeOrBoardApprovalDTO.setUserName(rs.getString("cod_full_name"));
				}

				committeeOrBoardApprovalDTO.setResponsibility(rs.getString("cod_responsibility"));
				committeeOrBoardApprovalDTO.setStatus("Active");
				committeeOrBoardApprovalDTO.setSelectedRemark("");
				committeeOrBoardApprovalDTO.setApproveRejectStatus("");
				committeeOrBoardApprovalDTO.setTransactionDate(date);

				approvalList.add(committeeOrBoardApprovalDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return approvalList;

	}

	public String checkCommitteeApproval(String requestNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String check = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT trp_is_committe_approve_proccess FROM public.nt_t_initiate_survey where ini_isu_requestno = '"
					+ requestNo + "';";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {

				check = rs.getString("trp_is_committe_approve_proccess");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return check;

	}

	public int insertCommitteeApprovalData(String surveyNo, String refNo, String remark, String status, String user,
			CommitteeOrBoardApprovalDTO committeeApprovalDTO) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			java.util.Date date = new java.util.Date();
			Timestamp timestamp = new Timestamp(date.getTime());

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_committee_approval");

			committeeApprovalDTO.setCommitteApprovalSeqNo(seqNo);

			String sql = "INSERT INTO public.nt_m_committee_approval\r\n"
					+ "(seqno, application_no, committee_ref_no, remark, status, created_by, created_date)\r\n"
					+ "VALUES(" + seqNo + ", '" + surveyNo + "', '" + refNo + "', '" + remark + "', '" + status + "', '"
					+ user + "', '" + timestamp + "');";

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

	public int insertCommitteeApprovalDetailData(long committeeSeqNo, String userName, String status, String date,
			String remark, String user) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			java.util.Date dates = new java.util.Date();
			Timestamp timestamp = new Timestamp(dates.getTime());

			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			// you can change format of date
			Date tranDateObj = formatter.parse(date);
			Timestamp timeStampDate = new Timestamp(tranDateObj.getTime());

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_committee_approval_det");

			String sql = "INSERT INTO public.nt_t_committee_approval_det\r\n"
					+ "(seqno, cad_committee_approval_seq, cad_user_id, cad_status, cad_transaction_date, cad_remark, created_by, created_date)\r\n"
					+ "VALUES(" + seqNo + ", '" + committeeSeqNo + "', '" + userName + "', '" + status + "', '"
					+ timeStampDate + "', '" + remark + "', '" + user + "','" + timestamp + "');";

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

	public int insertCommitteeApprovalDetailDataNoUserName(long committeeSeqNo, String status, String date,
			String remark, String user) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			java.util.Date dates = new java.util.Date();
			Timestamp timestamp = new Timestamp(dates.getTime());

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_committee_approval_det");

			String sql = "INSERT INTO public.nt_t_committee_approval_det\r\n"
					+ "(seqno, cad_committee_approval_seq, cad_status, cad_transaction_date, cad_remark, created_by, created_date)\r\n"
					+ "VALUES(" + seqNo + ", '" + committeeSeqNo + "', '" + status + "', '" + date + "', '" + remark
					+ "', '" + user + "','" + timestamp + "');";

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

	public int insertApproveStatus(String requestNo, String proccessStatus, String proccessBy, String remark) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_initiate_survey\r\n" + "SET trp_is_committe_approve_proccess = '"
					+ proccessStatus + "', trp_is_committe_approved_proccess_by='" + proccessBy
					+ "',trp_is_committe_approved_proccess_date='" + timestamp + "', ini_committee_remark='" + remark
					+ "'\r\n" + "WHERE ini_isu_requestno='" + requestNo + "';";

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

	public int insertBoardRejectStatus(String requestNo, String proccessStatus, String proccessBy, String remark) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_initiate_survey\r\n" + "SET trp_is_board_approve = '" + proccessStatus
					+ "', trp_is_board_approved_by='" + proccessBy + "',trp_is_board_approved_date='" + timestamp
					+ "',ini_board_rejected_remark='" + remark + "'\r\n" + "WHERE ini_isu_requestno='" + requestNo
					+ "';";

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

	public int insertBoardApproveStatus(String requestNo, String proccessStatus, String proccessBy) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_initiate_survey\r\n" + "SET trp_is_board_approve = '" + proccessStatus
					+ "', trp_is_board_approved_by='" + proccessBy + "',trp_is_board_approved_date='" + timestamp
					+ "'\r\n" + "WHERE ini_isu_requestno='" + requestNo + "';";

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

	public int checkBoardApproveStatus(long cadSeq) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		int i = 0;
		int j = 0;
		int f = 0;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT cad_status FROM public.nt_t_committee_approval_det where cad_committee_approval_seq='"
					+ cadSeq + "'";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {

				String status = rs.getString("cad_status");

				if (status.equals("R")) {

					i++;
				}

				if (status.equals("A")) {

					j++;
				}

			}

			if (i > j) {

				f = 1;
			}

			if (i <= j) {

				f = 0;
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return f;

	}

	@Override
	public List<SurveyDTO> removeEditedData(SurveyDTO surveyDTO, String string1, Integer intg, String string2,
			String string3, String string4) {
		Connection con = null;
		PreparedStatement ps = null, ps1 = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query1 = "DELETE from  public.nt_t_survey_route_detail where srd_survey_no=? and  srd_route_no=? and srd_no_of_permits=? and srd_effective_route=? and srd_destination=? and srd_origin=?";
			ps1 = con.prepareStatement(query1);
			ps1.setString(1, surveyDTO.getSurveyNo());
			ps1.setString(2, string1);
			ps1.setInt(3, intg);
			ps1.setString(4, string2);
			ps1.setString(5, string3);
			ps1.setString(6, string4);
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
		return null;
	}

	@Override
	public SurveyDTO showSeqNo(SurveyDTO surveyDTO, String string1, Integer intg, String string2, String string3,
			String string4) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		SurveyDTO surveyDTO1 = new SurveyDTO();
		String WHERE_SQL = "";

		if (surveyDTO.getSurveyNo() != null && !surveyDTO.getSurveyNo().equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE srd_survey_no  = " + "'" + surveyDTO.getSurveyNo() + "'";
		}
		if (string1 != null && !string1.equals("")) {
			WHERE_SQL = WHERE_SQL + " AND srd_route_no  = " + "'" + string1 + "'";
		}
		if (intg != null && !intg.equals("")) {
			WHERE_SQL = WHERE_SQL + " AND srd_no_of_permits  = " + "'" + intg + "'";
		}
		if (string2 != null && !string2.equals("")) {
			WHERE_SQL = WHERE_SQL + " AND srd_effective_route  = " + "'" + string2 + "'";
		}
		if (string3 != null && !string3.equals("")) {
			WHERE_SQL = WHERE_SQL + " AND srd_destination  = " + "'" + string3 + "'";
		}
		if (string4 != null && !string4.equals("")) {
			WHERE_SQL = WHERE_SQL + " AND srd_origin  = " + "'" + string4 + "'";
		}

		try {
			con = ConnectionManager.getConnection();
			String query2 = " select  seqno from public.nt_t_survey_route_detail " + WHERE_SQL;

			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();

			while (rs.next()) {
				surveyDTO1.setSeq(rs.getInt("seqno"));
			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return surveyDTO1;
	}

	@Override
	public List<SurveyDTO> updateEditedData(SurveyDTO surveyDTO, String string1, Integer intg, String string2,
			String string3, String string4) {
		Connection con = null;
		PreparedStatement ps = null, ps1 = null;
		ResultSet rs = null;

		String WHERE_SQL = "";

		if (surveyDTO.getSurveyNo() != null && !surveyDTO.getSurveyNo().equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE srd_survey_no  = " + "'" + surveyDTO.getSurveyNo() + "'";
			
		}
		if (string1 != null && !string1.equals("")) {
			WHERE_SQL = WHERE_SQL + " AND srd_route_no  = " + "'" + string1 + "'";
			
		}
		if (intg != null && !intg.equals("")) {
			WHERE_SQL = WHERE_SQL + " AND srd_no_of_permits  = " + "'" + intg + "'";
			
		}
		if (string2 != null && !string2.equals("")) {
			WHERE_SQL = WHERE_SQL + " AND srd_effective_route  = " + "'" + string2 + "'";
			
		}
		if (string3 != null && !string3.equals("")) {
			WHERE_SQL = WHERE_SQL + " AND srd_destination  = " + "'" + string3 + "'";
			
		}
		if (string4 != null && !string4.equals("")) {
			WHERE_SQL = WHERE_SQL + " AND srd_origin  = " + "'" + string4 + "'";
			
		}

		try {
			con = ConnectionManager.getConnection();

			String query1 = "Update  public.nt_t_survey_route_detail SET  srd_route_no=? , srd_no_of_permits=? , srd_effective_route=?, srd_destination=? , srd_origin=?"
					+ WHERE_SQL;

			ps1 = con.prepareStatement(query1);
			ps1.setString(1, surveyDTO.getRouteNo());
			ps1.setInt(2, surveyDTO.getNoOfPermits());
			ps1.setString(3, surveyDTO.getEffectiveRoute());
			ps1.setString(4, surveyDTO.getDestination());
			ps1.setString(5, surveyDTO.getOrigin());
			ps1.executeUpdate();

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
				ConnectionManager.close(rs);
				ConnectionManager.close(ps);
				ConnectionManager.close(ps1);
				ConnectionManager.close(con);
		}
		return null;
	}

	@Override
	public SurveyDTO filldet(String surveyNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		SurveyDTO surveyDTO = new SurveyDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "select survey_summary_remarks ,ini_survey_summary_status_remark,ini_tender_process_require from public.nt_t_initiate_survey where  ini_surveyno=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, surveyNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				surveyDTO.setSpecialRemarks(rs.getString("survey_summary_remarks"));
				surveyDTO.setApproveRejectStatus(rs.getString("ini_survey_summary_status_remark"));
				if (rs.getString("ini_tender_process_require") != null
						&& !rs.getString("ini_tender_process_require").trim().isEmpty()) {
					if (rs.getString("ini_tender_process_require").equals("Y")) {
						surveyDTO.setTenderRequire("true");
					} else if (rs.getString("ini_tender_process_require").equals("N")) {
						surveyDTO.setTenderRequire("false");
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

		return surveyDTO;
	}

	@Override
	public boolean checkIsBoardApproved(java.lang.String selectedSurveyNo, java.lang.String selectedRequestNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean boardApproved = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT  tsd_task_code, tsd_status FROM public.nt_t_survey_task_det where tsd_request_no=? and tsd_survey_no=? and tsd_task_code='SU011' and tsd_status='C';";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedRequestNo);
			ps.setString(2, selectedSurveyNo);
			rs = ps.executeQuery();

			if (rs.next() == true) {
				boardApproved = true;
			} else {
				boardApproved = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return boardApproved;
	}

	@Override
	public List<String> get_Drpd_FormIdList() {
		{

			Connection con = null;
			PreparedStatement ps = null;
			ResultSet rs = null;

			List<String> formIdList = new ArrayList<String>();

			try {
				con = ConnectionManager.getConnection();

				String query = "select distinct a.form_id,b.tsd_survey_no\r\n" + "from public.nt_m_form a \r\n"
						+ "inner join public.nt_h_survey_task_his b on a.survey_no = b.tsd_survey_no\r\n"
						+ "where (b.tsd_task_code = 'SU005' and b.tsd_status = 'C') \r\n"
						+ "and tsd_survey_no not in (select tsd_survey_no from nt_h_survey_task_his where tsd_task_code = 'SU009' and tsd_status ='O')\r\n"
						+ "order by b.tsd_survey_no";
				ps = con.prepareStatement(query);

				rs = ps.executeQuery();

				while (rs.next()) {
					formIdList.add(rs.getString("form_id"));
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
	}

	@Override
	public FormDTO getDetailsByFormId(FormDTO formDTO) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select A.form_id as formId,A.form_description as formDescription,A.survey_no as surveyNo,A.form_no as formNo, \r\n"
					+ "B.ini_survey_type as surveyType,B.ini_survey_method as surveyMethod, B.ini_isu_requestno as reqNo\r\n"
					+ "from public.nt_m_form A \r\n"
					+ "inner join public.nt_t_initiate_survey B on A.survey_no = B.ini_surveyno \r\n"
					+ "where A.form_id = ? ";

			ps = con.prepareStatement(query);

			ps.setString(1, formDTO.getFormId());
			rs = ps.executeQuery();

			while (rs.next()) {
				formDTO.setFormDescription(rs.getString("formDescription"));
				formDTO.setSurveyNo(rs.getString("surveyNo").substring(0, 10));
				formDTO.setSurveyType(rs.getString("surveyType"));
				formDTO.setSurveyMethod(rs.getString("surveyMethod"));
				formDTO.setSurveyFormNo(rs.getString("formNo"));
				formDTO.setSurveyReqNo(rs.getString("reqNo"));

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return formDTO;
	}

	@Override
	public List<FormDTO> get_drpd_FormulaNameList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<FormDTO> formulaNameList = new ArrayList<FormDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select code,description \r\n" + "from nt_r_formula_variable;";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				FormDTO formula = new FormDTO();
				formula.setFormulaNameCode(rs.getString("code"));
				formula.setFormulaNameDescription(rs.getString("description"));
				formulaNameList.add(formula);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return formulaNameList;
	}

	@Override
	public List<String> get_drpd_FieldNameList(String formId) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<String> fieldNameList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select field_name \r\n" + "from public.nt_t_indicators\r\n" + "where form_id = ? ;";
			ps = con.prepareStatement(query);

			ps.setString(1, formId);
			rs = ps.executeQuery();

			while (rs.next()) {
				fieldNameList.add(rs.getString("field_name"));
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return fieldNameList;
	}

	@Override
	public String getTaskStatus(String surveyNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String taskStatus = null;
		try {
			con = ConnectionManager.getConnection();

			String query = " select tsd_task_code,tsd_status from public.nt_t_survey_task_det where tsd_survey_no=? and tsd_task_code=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, surveyNo);
			ps.setString(2, "SU008");
			rs = ps.executeQuery();

			while (rs.next()) {

				taskStatus = rs.getString("tsd_status");
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return taskStatus;
	}

	@Override
	public List<String> get_drpd_OperatorList(String formId) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<String> operatorList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select frm_ru_numarator\r\n" + "from public.nt_t_formula_rules\r\n"
					+ "where form_id = = ? ;";
			ps = con.prepareStatement(query);

			ps.setString(1, formId);
			rs = ps.executeQuery();

			while (rs.next()) {
				operatorList.add(rs.getString("frm_ru_numarator"));
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return operatorList;
	}

	@Override
	public List<String> get_drpd_FormulaIdList(String formId) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<String> formIdList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select A.form_id \r\n" + "from public.nt_m_form A\r\n"
					+ "inner join public.nt_t_survey_task_det B on A.survey_no =B.tsd_survey_no\r\n"
					+ "where B.tsd_task_code = 'SU007' and B.tsd_status = 'C' \r\n" + "order by A.form_id;";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				formIdList.add(rs.getString("form_id"));
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

	// check task status for create survey summary page
	@Override
	public String addFormulaToGrid(FormDTO formDTO) {

		String status = "invalid";
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;

		try {

			con = ConnectionManager.getConnection();
			String checkDuplicate = "select frm_ru_formula_code\r\n" + "from public.nt_t_formula_rules\r\n"
					+ "where frm_ru_formula_code = ? and survey_no= ? ; ";
			ps1 = con.prepareStatement(checkDuplicate);
			ps1.setString(1, formDTO.getFormulaNameCode());
			ps1.setString(2, formDTO.getSurveyNo());
			rs1 = ps1.executeQuery();
			boolean results = false;
			while (rs1.next()) {
				results = true;
			}

			if (results) {
				status = "D";
			} else {
				status = "S";

				long seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_t_formula_rules");

				String query = "INSERT INTO public.nt_t_formula_rules\r\n"
						+ "(seqno, form_id, survey_no, frm_ru_formula, frm_ru_temp_id,frm_ru_formula_code)\r\n"
						+ "VALUES(?,?,?,?,?,?);";
				ps = con.prepareStatement(query);

				ps.setLong(1, seqNo);
				ps.setString(2, formDTO.getFormId());
				ps.setString(3, formDTO.getSurveyNo());
				ps.setString(4, formDTO.getDisplayFormulaName());

				if (formDTO.getFormulaTemplateId() != null && !formDTO.getFormulaTemplateId().isEmpty()
						&& !formDTO.getFormulaTemplateId().equalsIgnoreCase("")) {
					ps.setString(5, formDTO.getFormulaTemplateId());
				} else {
					ps.setNull(5, java.sql.Types.VARCHAR);
				}

				ps.setString(6, formDTO.getFormulaNameCode());

				ps.executeUpdate();
			}

			con.commit();
		} catch (Exception e) {
			status = "I";
			e.printStackTrace();

		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return status;
	}

	@Override
	public List<FormDTO> get_tbl_formulaList(FormDTO formDTO) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<FormDTO> formulaList = new ArrayList<FormDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT A.frm_ru_formula_code as formulaCode,A.frm_ru_formula as seqNo, A.form_id as formId, A.survey_no as surveyNo, A.frm_ru_formula as formula, A.frm_ru_temp_id as templateId, \r\n"
					+ "B.description as formulaName\r\n" + "FROM public.nt_t_formula_rules A\r\n"
					+ "inner join public.nt_r_formula_variable B on A.frm_ru_formula_code = B.code\r\n"
					+ "where A.form_id = ?  and A.survey_no = ?;";
			ps = con.prepareStatement(query);
			ps.setString(1, formDTO.getFormId());
			ps.setString(2, formDTO.getSurveyNo());
			rs = ps.executeQuery();

			while (rs.next()) {

				FormDTO formula = new FormDTO();
				formula.setFormulaNameCode(rs.getString("formulaCode"));
				formula.setFormulaName(rs.getString("formulaName"));
				formula.setFormula(rs.getString("formula"));
				formulaList.add(formula);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return formulaList;

	}

	// update approve reject initiate_survey table
	@Override
	public List<SurveyDTO> updateApproveRejectStatus(SurveyDTO surveyDTO, String loginUser) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		String WHERE_SQL = "";

		if (surveyDTO.getSurveyNo() != null && !surveyDTO.getSurveyNo().equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE ini_surveyno = " + "'" + surveyDTO.getSurveyNo() + "'";
		}

		try {
			con = ConnectionManager.getConnection();
			String query2 = "Update  public.nt_t_initiate_survey SET ini_survey_summary_status=?,ini_survey_summary_status_remark=?,ini_tender_process_require=?,ini_modified_by =?,ini_modified_date =?  "
					+ WHERE_SQL;

			ps = con.prepareStatement(query2);
			ps.setString(1, "A");
			ps.setString(2, surveyDTO.getApproveRejectStatus());
			if (surveyDTO.getTenderRequire() != null && surveyDTO.getTenderRequire().trim().equals("true")) {
				ps.setString(3, "Y");
			} else if (surveyDTO.getTenderRequire() != null && surveyDTO.getTenderRequire().trim().equals("false")) {
				ps.setString(3, "N");
			} else {
				ps.setString(3, "");
			}
			ps.setString(4, loginUser);
			ps.setTimestamp(5, timestamp);
			ps.executeUpdate();

			if (ps != null) {
				ps.close();
			}
			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return null;
	}

	@Override
	public String getApproveRejectStatus(String surveyNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String status = null;
		try {
			con = ConnectionManager.getConnection();

			String query = " select ini_survey_summary_status from public.nt_t_initiate_survey where ini_surveyno=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, surveyNo);

			rs = ps.executeQuery();

			while (rs.next()) {

				status = rs.getString("ini_survey_summary_status");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return status;
	}
	// update reject statu

	@Override
	public List<SurveyDTO> updateRejectStatus(SurveyDTO surveyDTO, String loginUser) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		String WHERE_SQL = "";

		if (surveyDTO.getSurveyNo() != null && !surveyDTO.getSurveyNo().equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE ini_surveyno = " + "'" + surveyDTO.getSurveyNo() + "'";
		}

		try {
			con = ConnectionManager.getConnection();
			String query2 = "Update  public.nt_t_initiate_survey SET ini_survey_summary_status=?,ini_survey_summary_status_remark=?,ini_tender_process_require=?,ini_modified_by =?,ini_modified_date =?  "
					+ WHERE_SQL;

			ps = con.prepareStatement(query2);
			ps.setString(1, "R");
			ps.setString(2, surveyDTO.getApproveRejectStatus());
			if (surveyDTO.getTenderRequire() != null && surveyDTO.getTenderRequire().trim().equals("true")) {
				ps.setString(3, "Y");
			} else if (surveyDTO.getTenderRequire() != null && surveyDTO.getTenderRequire().trim().equals("false")) {
				ps.setString(3, "N");
			} else {
				ps.setString(3, "");
			}
			ps.setString(4, loginUser);
			ps.setTimestamp(5, timestamp);
			ps.executeUpdate();

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return null;
	}

	@Override
	public boolean removeSelectedFormula(String formulaId) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "DELETE FROM public.nt_t_formula_rules WHERE frm_ru_formula_code = ? ";

			ps = con.prepareStatement(query);
			ps.setString(1, formulaId);
			ps.executeUpdate();
			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return true;
	}

	@Override
	public boolean getSurManApprovalStatus(String loginUser) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean hasPermission = false;

		try {

			con = ConnectionManager.getConnection();

			String query = "select nt_r_fun_role_activity.fra_function_code,nt_r_fun_role_activity.fra_activity_code \r\n"
					+ "from nt_t_granted_user_role inner join nt_r_fun_role_activity on \r\n"
					+ "nt_r_fun_role_activity.fra_role_code = nt_t_granted_user_role.gur_role_code\r\n"
					+ "where fra_activity_code = 'A' \r\n" + "and fra_function_code='FN5_4'\r\n"
					+ "and gur_user_id = ? \r\n" + ";";

			ps = con.prepareStatement(query);
			ps.setString(1, loginUser);

			rs = ps.executeQuery();

			if (rs.next() == true) {
				hasPermission = true;
			} else {
				hasPermission = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return hasPermission;
	}

	@Override
	public TrafficProposalDTO getDetailsForTrafficProNo(String selectedSurveyNo, String selectedRequestNo,
			String selectedProposalNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		TrafficProposalDTO trafficProposalDTO = new TrafficProposalDTO();

		try {
			con = ConnectionManager.getConnection();
			
			String query = "SELECT  summary_approve_remarks, trafic_pro_print_note_remark, trafic_pro_suggestions FROM public.nt_t_traffic_proposal where survey_no=? and proposal_no=?;";
			ps = con.prepareStatement(query);
			ps.setString(1, selectedSurveyNo);
			ps.setString(2, selectedProposalNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				trafficProposalDTO.setSpecialRemark(rs.getString("summary_approve_remarks"));
				trafficProposalDTO.setSpecialPrintNote(rs.getString("trafic_pro_print_note_remark"));
				trafficProposalDTO.setSuggestions(rs.getString("trafic_pro_suggestions"));
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
	public String insertInToTblCostEstimation(SurveyDTO surveyDTO, SurveyLocationTeamDTO surveyLocationTeamDTO) {// TODO
																													// Auto-generated
		// method stub
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String returnSurveyNo = null;
		String surveyNo = null;

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_m_cost_estimation");

			java.util.Date date = new java.util.Date();
			Timestamp timestamp = new Timestamp(date.getTime());
			String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(timestamp);

			String sql = "INSERT INTO public.nt_m_cost_estimation\r\n"
					+ "(coe_seq, coe_survey_no, coe_total_cost, coe_remark, coe_isapproved, coe_approvedby, coe_approveddate, coe_created_by, coe_created_date)\r\n"
					+ "VALUES( ?, ? , ? , ? , ? , ? , ? , ? , ? );";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, surveyDTO.getSurveyNo());
			stmt.setBigDecimal(3, surveyLocationTeamDTO.getTotalAmount());
			stmt.setString(4, surveyLocationTeamDTO.getSpecialRemark());
			stmt.setString(5, "Y");
			stmt.setString(6, surveyDTO.getLoginUser());
			stmt.setString(7, formattedDate);
			stmt.setString(8, surveyDTO.getLoginUser());
			stmt.setTimestamp(9, timestamp);

			int data = stmt.executeUpdate();
			if (data > 0) {
				returnSurveyNo = surveyNo;
			}

			con.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return returnSurveyNo;
	}

	@Override
	public boolean getCostApprovedStatus(String surveyNo, String status) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isApproved = false;

		try {

			con = ConnectionManager.getConnection();

			String query = "select coe_survey_no\r\n" + "from public.nt_m_cost_estimation\r\n"
					+ "where coe_survey_no = ? and coe_isapproved = 'Y'";

			ps = con.prepareStatement(query);
			ps.setString(1, surveyNo);

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

	// analyze survey data
	@Override
	public List<IndicatorsDTO> get_drp_FieldNamesList(String formId) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<IndicatorsDTO> fieldNamesList = new ArrayList<IndicatorsDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = " select seqno,field_name\r\n" + " from public.nt_t_indicators\r\n" + " where form_id = ?;";
			ps = con.prepareStatement(query);
			ps.setString(1, formId);
			rs = ps.executeQuery();

			while (rs.next()) {

				IndicatorsDTO indicatorsDTO = new IndicatorsDTO();

				indicatorsDTO.setFieldSeqNo(rs.getInt("seqno"));
				indicatorsDTO.setFieldNameEn(rs.getString("field_name"));
				fieldNamesList.add(indicatorsDTO);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return fieldNamesList;
	}

	@Override
	public String getSelectedFormulaName(String formulaCode) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String formulaName = "invalid";

		try {
			con = ConnectionManager.getConnection();

			String sql = "select description\r\n" + "from public.nt_r_formula_variable\r\n"
					+ "where code = ? and description is not null;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, formulaCode);
			rs = stmt.executeQuery();

			while (rs.next()) {
				formulaName = rs.getString("description");
			}
			con.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return formulaName;
	}

	@Override
	public String checkDuplicateRouteNo(SurveyDTO surveyDTO) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String routeNo = null;
		String checkrouteNo = surveyDTO.getRouteNo();
		try {
			con = ConnectionManager.getConnection();

			String sql = "select srd_route_no  from public.nt_t_survey_route_detail where srd_survey_no=? and srd_route_no like '%"
					+ checkrouteNo + "%';";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, surveyDTO.getSurveyNo());
			rs = stmt.executeQuery();

			while (rs.next()) {
				routeNo = rs.getString("srd_route_no");
			}

			con.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return routeNo;
	}

	@Override
	public String getBoardApproveStatus(String surveyNo) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String status = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT trp_is_board_approve FROM public.nt_t_initiate_survey where ini_surveyno='" + surveyNo
					+ "'";

			stmt = con.prepareStatement(sql);

			rs = stmt.executeQuery();

			while (rs.next()) {
				status = rs.getString("trp_is_board_approve");
			}

			con.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return status;
	}

	@Override
	public String getBoardApproveStatusForAmendments(String applicationNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String status = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT amd_board_approved FROM public.nt_m_amendments where amd_application_no='"
					+ applicationNo + "'";

			stmt = con.prepareStatement(sql);

			rs = stmt.executeQuery();

			while (rs.next()) {
				status = rs.getString("amd_board_approved");
			}

			con.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return status;
	}

	@Override
	public int getSeqNo(String refNo, String applicationNo) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int seqNo = 0;

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT seqno FROM public.nt_m_committee_approval where committee_ref_no='" + refNo
					+ "' and application_no='" + applicationNo + "'";

			stmt = con.prepareStatement(sql);

			rs = stmt.executeQuery();

			while (rs.next()) {
				seqNo = rs.getInt("seqno");
			}

			con.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return seqNo;
	}

	@Override
	public String getMainRemark(String refNo, String applicationNo) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String remark = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT remark FROM public.nt_m_committee_approval where committee_ref_no='" + refNo
					+ "' and application_no='" + applicationNo + "'";

			stmt = con.prepareStatement(sql);

			rs = stmt.executeQuery();

			while (rs.next()) {
				remark = rs.getString("remark");
			}

			con.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return remark;
	}

	@Override
	public List<CommitteeOrBoardApprovalDTO> getDetails01(String refNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommitteeOrBoardApprovalDTO> detailList = new ArrayList<CommitteeOrBoardApprovalDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select cod_userid,cod_responsibility,cod_created_date from nt_t_committee_det\r\n"
					+ "where cod_committee_refno='" + refNo + "' order by cod_userid";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {

				CommitteeOrBoardApprovalDTO approvalDTO = new CommitteeOrBoardApprovalDTO();
				approvalDTO.setUserName(rs.getString("cod_userid"));
				approvalDTO.setResponsibility(rs.getString("cod_responsibility"));
				String time = rs.getString("cod_created_date");
				time = time.substring(0, 10);
				approvalDTO.setTransactionDate(time);

				approvalDTO.setStatus("Active");

				detailList.add(approvalDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return detailList;
	}

	@Override
	public List<CommitteeOrBoardApprovalDTO> getDetails02(int seqNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommitteeOrBoardApprovalDTO> detailList = new ArrayList<CommitteeOrBoardApprovalDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT cad_status,cad_remark FROM public.nt_t_committee_approval_det\r\n"
					+ "where cad_committee_approval_seq='" + seqNo + "' order by cad_user_id";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {

				CommitteeOrBoardApprovalDTO approvalDTO = new CommitteeOrBoardApprovalDTO();

				approvalDTO.setApproveRejectStatus(rs.getString("cad_status"));
				approvalDTO.setSelectedRemark(rs.getString("cad_remark"));

				detailList.add(approvalDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return detailList;
	}

	public String checkAuthStatus(String refNo) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		String status = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT com_isauthorized FROM public.nt_m_committee where com_refno = '" + refNo + "'";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				status = rs.getString("com_isauthorized");
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

	// survey analyze data

	@Override
	public int getFormulaCount(FormDTO formDTO) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int formulaCount = 0;

		try {
			con = ConnectionManager.getConnection();

			String sql = "select count (A.frm_ru_formula_code) as fcount\r\n" + "FROM public.nt_t_formula_rules A\r\n"
					+ "inner join public.nt_r_formula_variable B on A.frm_ru_formula_code = B.code\r\n"
					+ "where A.form_id = ? and A.survey_no = ? ";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, formDTO.getFormId());
			stmt.setString(2, formDTO.getSurveyNo());
			rs = stmt.executeQuery();

			while (rs.next()) {
				formulaCount = rs.getInt("fcount");
			}

			con.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return formulaCount;

	}

	@Override
	public boolean updateRouteBusFare(SurveyDTO surveyMgtDTO, String loginUser) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Boolean success = false;
		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_r_route SET  rou_tot_busfare=? WHERE rou_number=?; ";

			stmt = con.prepareStatement(sql);

			stmt.setBigDecimal(1, surveyMgtDTO.getBusFare());
			stmt.setString(2, surveyMgtDTO.getRouteNoNew());
			int data = stmt.executeUpdate();

			if (data > 0) {
				success = true;
			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
		return success;
	}

	@Override
	public boolean updateNewRouteDetails(SurveyDTO routeDetailsDTO, String user) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Boolean flag = false;

		try {
			con = ConnectionManager.getConnection();

			java.util.Date date = new java.util.Date();
			Timestamp timestamp = new Timestamp(date.getTime());

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_r_route");

			String sql = "UPDATE public.nt_r_route "
					+ "SET seqno=?, rou_number=?, rou_service_origine=?, rou_service_destination=?, rou_distance=?, rou_via=?, active=?, created_by=?, rou_description=?, created_date=?"
					+ "WHERE rou_number=?; ";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, routeDetailsDTO.getRouteNoNew());
			stmt.setString(3, routeDetailsDTO.getOriginNew());
			stmt.setString(4, routeDetailsDTO.getDestinationNew());
			stmt.setInt(5, routeDetailsDTO.getDistanceNew());
			stmt.setString(6, routeDetailsDTO.getViaNew());
			stmt.setString(7, "T");
			stmt.setString(8, user);
			stmt.setString(9, routeDetailsDTO.getOriginNew() + "-" + routeDetailsDTO.getDestinationNew());
			stmt.setTimestamp(10, timestamp);

			int data = stmt.executeUpdate();

			if (data > 0) {
				flag = true;
			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return flag;
	}

	@Override
	public boolean getTaskBoolean(String surveyNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isTaskSU008C = false;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  tsd_task_code, tsd_status FROM public.nt_t_survey_task_det where tsd_survey_no=? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, surveyNo);
			rs = ps.executeQuery();

			if (rs.next()) {
				String taskCode = rs.getString("tsd_task_code");
				String taskStatus = rs.getString("tsd_status");
				if (taskCode.equals("SU008") && taskStatus.equals("C")) {
					isTaskSU008C = true;

				} else {
					isTaskSU008C = false;
				}
			}
			con.commit();

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isTaskSU008C;
	}

	@Override
	public String returnNICNo(String userId) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String nicNO = "NIC No not found.";

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT emp_nic_no FROM public.nt_m_employee WHERE emp_user_id = ? ";
			ps = con.prepareStatement(query);
			ps.setString(1, userId);
			rs = ps.executeQuery();

			while (rs.next()) {
				nicNO = rs.getString("emp_nic_no");
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return nicNO;
	}

	@Override
	public String returnSurveyTypeDes(String surveyType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String surveyTypeDes = "survey type not found.";

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  description FROM public.nt_r_survey_types where code=?;";
			ps = con.prepareStatement(query);
			ps.setString(1, surveyType);
			rs = ps.executeQuery();

			while (rs.next()) {
				surveyTypeDes = rs.getString("description");
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return surveyTypeDes;
	}

	@Override
	public String returnSurveyMethodDes(String surveyMethod) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String surveyMethodDes = "survey method not found.";

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT description FROM public.nt_r_survey_methods where code=?;";
			ps = con.prepareStatement(query);
			ps.setString(1, surveyMethod);
			rs = ps.executeQuery();

			while (rs.next()) {
				surveyMethodDes = rs.getString("description");
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return surveyMethodDes;
	}

	public List<CommitteeOrBoardApprovalDTO> getGamiSurveyNoForCommitteeApproval() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommitteeOrBoardApprovalDTO> surveyNoList = new ArrayList<CommitteeOrBoardApprovalDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct tsd_survey_no FROM public.nt_t_survey_task_det where tsd_survey_no like 'GSN%' and (tsd_task_code='SU009' and tsd_status='C'\r\n"
					+ "or tsd_task_code='SU010' and tsd_status='O'\r\n"
					+ "or tsd_task_code='SU010' and tsd_status='C'\r\n"
					+ "or tsd_task_code='SU011' and tsd_status='O') \r\n" + "order by tsd_survey_no";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				CommitteeOrBoardApprovalDTO committeeApprovalDTO = new CommitteeOrBoardApprovalDTO();

				committeeApprovalDTO.setCommitteeSurveyNo(rs.getString("tsd_survey_no"));

				surveyNoList.add(committeeApprovalDTO);

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

	public List<CommitteeOrBoardApprovalDTO> getGamiViewSurveyNoForCommitteeApprovalStatus() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommitteeOrBoardApprovalDTO> surveyNoList = new ArrayList<CommitteeOrBoardApprovalDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT gsi_survey_no FROM public.nt_m_gamisari_survey_initiate where gsi_survey_no like 'GSN%' and (gsi_is_committe_approve='A' or gsi_is_board_approve='A') order by gsi_survey_no ";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				CommitteeOrBoardApprovalDTO committeeApprovalDTO = new CommitteeOrBoardApprovalDTO();

				committeeApprovalDTO.setCommitteeSurveyNo(rs.getString("gsi_survey_no"));

				surveyNoList.add(committeeApprovalDTO);

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
	public String getGamiSurveyType(String surveyNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String surveyType = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "select b.description  from public.nt_m_gamisari_survey_initiate  a, nt_r_survey_types b\r\n"
					+ "where a.gsi_survey_type = b.code and a.gsi_survey_no='" + surveyNo + "'";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {

				surveyType = rs.getString("description");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return surveyType;

	}

	@Override
	public String getGamiSurveyMethod(String surveyNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String surveyMethod = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "select nt_r_survey_methods.description \r\n"
					+ " from public.nt_m_gamisari_survey_initiate a inner join nt_r_survey_methods on \r\n"
					+ " a.gsi_survey_method = nt_r_survey_methods.code \r\n" + " where a.gsi_survey_no='" + surveyNo
					+ "'";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {

				surveyMethod = rs.getString("description");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return surveyMethod;

	}

	@Override
	public String checkCommitteeApprovalForGamiSariya(String surveyNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String check = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT gsi_is_committe_approve FROM public.nt_m_gamisari_survey_initiate where gsi_survey_no= '"
					+ surveyNo + "';";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {

				check = rs.getString("gsi_is_committe_approve");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return check;

	}

	@Override
	public int insertApproveStatusInGamisariya(String surveyNo, String proccessStatus, String proccessBy,
			String remark) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_gamisari_survey_initiate\r\n" + "SET gsi_is_committe_approve = '"
					+ proccessStatus + "', gsi_committee_approve_rejected_by='" + proccessBy
					+ "',gsi_committee_approve_rejected_date='" + timestamp + "', gsi_committe_remarks='" + remark
					+ "'\r\n" + "WHERE gsi_survey_no='" + surveyNo + "';";

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

	@Override
	public int insertBoardRejectStatusInGamiSariya(String surveyNo, String proccessStatus, String proccessBy,
			String remark) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_gamisari_survey_initiate\r\n" + "SET gsi_is_board_approve = '"
					+ proccessStatus + "', gsi_board_approve_rejected_by='" + proccessBy
					+ "',gsi_board_approve_rejected_date='" + timestamp + "',gsi_board_remarks='" + remark + "'\r\n"
					+ "WHERE gsi_survey_no='" + surveyNo + "';";

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

	public String getGamiBoardApproveStatus(String surveyNo) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String status = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT gsi_is_board_approve FROM public.nt_m_gamisari_survey_initiate where gsi_survey_no='"
					+ surveyNo + "'";

			stmt = con.prepareStatement(sql);

			rs = stmt.executeQuery();

			while (rs.next()) {
				status = rs.getString("gsi_is_board_approve");
			}

			con.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return status;
	}
	
	@Override
	public List<SurveyLocationTeamDTO> getCostCodeDropDownForGSSurveyManagement() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SurveyLocationTeamDTO> retriveCostData = new ArrayList<SurveyLocationTeamDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code,description " + "FROM public.nt_r_survey_cost " + "WHERE active = 'A' " + "ORDER BY code ;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				SurveyLocationTeamDTO dto = new SurveyLocationTeamDTO();
				dto.setCostCode(rs.getString("code"));
				dto.setCostCodeDescription(rs.getString("description"));
				retriveCostData.add(dto);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return retriveCostData;
	}
}
