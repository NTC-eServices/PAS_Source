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

import lk.informatics.ntc.model.dto.GenerateReceiptDTO;
import lk.informatics.ntc.model.dto.NisiSeriyaDTO;
import lk.informatics.ntc.model.dto.RouteDTO;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.roc.utils.common.Utils;

public class NisiSeriyaServiceImpl implements NisiSeriyaService {

	private static final long serialVersionUID = 1L;

	@Override
	public String generateNisiSeriyaRequestNo() {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String reqNum = null;

		int year = Year.now().getValue();
		String currYear = Integer.toString(year).substring(2, 4);
		String strRequestNo = "";
		String lastyr = "00";

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT app_no FROM public.nt_r_number_generation WHERE code= 'NSR'";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				reqNum = rs.getString("app_no");
			}

			if (reqNum != null) {

				lastyr = reqNum.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String ApprecordcountN = "00001";
					strRequestNo = "NSR" + currYear + ApprecordcountN;
				} else {
					String number = reqNum.substring(5, 10);
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
					strRequestNo = "NSR" + currYear + ApprecordcountN;
				}
			} else {
				strRequestNo = "NSR" + currYear + "00001";
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return strRequestNo;
	}

	@Override
	public List<RouteDTO> retrieveOriginList() {
		List<RouteDTO> routeDTOList = new ArrayList<RouteDTO>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct rou_number,rou_service_origine,rou_service_destination FROM public.nt_r_route where active = 'T' order by rou_service_origine";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				RouteDTO routeDto = new RouteDTO();

				routeDto.setRouteNo(rs.getString("rou_number"));
				routeDto.setOrigin(rs.getString("rou_service_origine"));
				routeDto.setDestination(rs.getString("rou_service_destination"));
				routeDTOList.add(routeDto);
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return routeDTOList;

	}

	@Override
	public List<RouteDTO> retrieveDestinationList(String originCode) {
		List<RouteDTO> routeDTOList = new ArrayList<RouteDTO>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			// temparary origin list -> from nt_r_route
			String query = "select distinct rou_number,rou_service_origine,rou_service_destination FROM public.nt_r_route where rou_number=? order by rou_service_origine";

			ps = con.prepareStatement(query);
			ps.setString(1, originCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				RouteDTO routeDto = new RouteDTO();

				routeDto.setRouteNo(rs.getString("rou_number"));
				routeDto.setOrigin(rs.getString("rou_service_origine"));
				routeDto.setDestination(rs.getString("rou_service_destination"));
				routeDTOList.add(routeDto);
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return routeDTOList;

	}

	@Override
	public List<RouteDTO> retrieveViaList(String originCode, String destinationCode) {
		List<RouteDTO> routeDTOList = new ArrayList<RouteDTO>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			// Temporary origin list -> from nt_r_route
			String query = "select distinct rou_number,rou_service_origine,rou_service_destination,rou_via FROM public.nt_r_route "
					+ "where rou_number=? and rou_service_destination=? order by rou_service_origine";

			ps = con.prepareStatement(query);
			ps.setString(1, originCode);
			ps.setString(2, destinationCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				RouteDTO routeDto = new RouteDTO();

				routeDto.setRouteNo(rs.getString("rou_number"));
				routeDto.setOrigin(rs.getString("rou_service_origine"));
				routeDto.setDestination(rs.getString("rou_service_destination"));
				routeDto.setVia(rs.getString("rou_via"));
				routeDTOList.add(routeDto);
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return routeDTOList;

	}

	@Override
	public String insertDataIntoNt_m_nri_requester_info(NisiSeriyaDTO nisiSeriyaDTO, String loginUser) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		String serviceRefNo = "";
		try {
			con = ConnectionManager.getConnection();

			/** create service reference number start **/
			int year = Year.now().getValue();
			String currYear = Integer.toString(year).substring(2, 4);
			String lastyr = "00";

			String select = "SELECT app_no FROM public.nt_r_number_generation WHERE code= 'NSN'";

			stmt = con.prepareStatement(select);
			rs = stmt.executeQuery();

			while (rs.next()) {
				serviceRefNo = rs.getString("app_no");
			}

			if (serviceRefNo != null && !serviceRefNo.isEmpty() && !serviceRefNo.trim().equalsIgnoreCase("")) {

				lastyr = serviceRefNo.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String ApprecordcountN = "00001";
					serviceRefNo = "NSN" + currYear + ApprecordcountN;
				} else {
					String number = serviceRefNo.substring(5, 10);
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
					serviceRefNo = "NSN" + currYear + ApprecordcountN;
				}
			} else {
				serviceRefNo = "NSN" + currYear + "00001";
			}

			ConnectionManager.close(stmt);

			/** create service reference number end **/

			/** insert data start **/

			updateNumberGeneration("NSN", serviceRefNo, loginUser);

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_nri_requester_info");

			String sql = "INSERT INTO public.nt_m_nri_requester_info "
					+ "(nri_seq, nri_request_no, nri_request_date, nri_service_ref_no, nri_permit_no, nri_prefer_language, nri_nic_no, "
					+ "nri_operator_name, nri_operator_name_si, nri_operator_name_ta, nri_add1, nri_add1_si, nri_add1_ta, nri_add2, nri_add2_si, nri_add2_ta, "
					+ "nri_city, nri_city_si, nri_city_ta, nri_service_type, nri_bus_no, nri_service_start_date, nri_service_end_date, "
					+ "nri_province, nri_district, nri_div_sec, nri_telephone_no, nri_mobile_no, nri_status,nri_origin,nri_destination,nri_distance,nri_tips_per_day,nri_via,created_by, created_date) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, nisiSeriyaDTO.getRequestNo());
			stmt.setTimestamp(3, timestamp);
			stmt.setString(4, serviceRefNo);
			stmt.setString(5, nisiSeriyaDTO.getPermitNo());
			stmt.setString(6, nisiSeriyaDTO.getLanguageCode());
			stmt.setString(7, nisiSeriyaDTO.getNicNo());
			stmt.setString(8, nisiSeriyaDTO.getNameOfOperator());
			stmt.setString(9, nisiSeriyaDTO.getNameOfOperatorSin());
			stmt.setString(10, nisiSeriyaDTO.getNameOfOperatorTamil());
			stmt.setString(11, nisiSeriyaDTO.getAddressOne());
			stmt.setString(12, nisiSeriyaDTO.getAddressOneSin());
			stmt.setString(13, nisiSeriyaDTO.getAddressOneTamil());
			stmt.setString(14, nisiSeriyaDTO.getAddressTwo());
			stmt.setString(15, nisiSeriyaDTO.getAdressTwoSin());
			stmt.setString(16, nisiSeriyaDTO.getAdressTwoTamil());
			stmt.setString(17, nisiSeriyaDTO.getCity());
			stmt.setString(18, nisiSeriyaDTO.getCitySin());
			stmt.setString(19, nisiSeriyaDTO.getCityTamil());
			stmt.setString(20, nisiSeriyaDTO.getServiceTypeCode());
			stmt.setString(21, nisiSeriyaDTO.getBusRegNo());
			if (nisiSeriyaDTO.getServiceStartDateObj() != null) {
				Timestamp serviceStartDate = new Timestamp(nisiSeriyaDTO.getServiceStartDateObj().getTime());
				stmt.setTimestamp(22, serviceStartDate);
			} else {
				stmt.setNull(22, java.sql.Types.TIMESTAMP);
			}
			if (nisiSeriyaDTO.getServiceEndDateObj() != null) {
				Timestamp serviceEndDate = new Timestamp(nisiSeriyaDTO.getServiceEndDateObj().getTime());
				stmt.setTimestamp(23, serviceEndDate);
			} else {
				stmt.setNull(23, java.sql.Types.TIMESTAMP);
			}
			stmt.setString(24, nisiSeriyaDTO.getProvinceCode());
			stmt.setString(25, nisiSeriyaDTO.getDistrictCode());
			stmt.setString(26, nisiSeriyaDTO.getDivisionalSecCode());
			stmt.setString(27, nisiSeriyaDTO.getTelNo());
			stmt.setString(28, nisiSeriyaDTO.getMobileNo());
			stmt.setString(29, "O");
			stmt.setString(30, nisiSeriyaDTO.getOriginCode());
			stmt.setString(31, nisiSeriyaDTO.getDestinationCode());

			stmt.setDouble(32, nisiSeriyaDTO.getDistance());

			if (nisiSeriyaDTO.getTripsPerDay() != 0) {
				stmt.setString(33, Integer.toString(nisiSeriyaDTO.getTripsPerDay()));
			} else {
				stmt.setNull(33, java.sql.Types.TIMESTAMP);
			}
			stmt.setString(34, nisiSeriyaDTO.getVia());
			stmt.setString(35, loginUser);
			stmt.setTimestamp(36, timestamp);

			stmt.executeUpdate();

			con.commit();

			/** insert data end **/
		} catch (Exception ex) {
			ex.printStackTrace();
			serviceRefNo = null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return serviceRefNo;
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

	@Override
	public boolean insertBankDetails(NisiSeriyaDTO nisiSeriyaDTO, String loginUser) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		boolean saved = false;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_nri_requester_info SET  nri_account_no=?, nri_bank_name=?, nri_branch=?, modified_by=?, modified_date=? WHERE nri_service_ref_no=? ";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, nisiSeriyaDTO.getAccountNo());
			stmt.setString(2, nisiSeriyaDTO.getBankNameCode());
			stmt.setString(3, nisiSeriyaDTO.getBankBranchNameCode());
			stmt.setString(4, loginUser);
			stmt.setTimestamp(5, timestamp);
			stmt.setString(6, nisiSeriyaDTO.getServiceRefNo());

			int i = stmt.executeUpdate();

			if (i > 0) {
				saved = true;
			} else {
				saved = false;
			}
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return saved;
	}

	@Override
	public boolean deleteRecordFromNt_m_nri_requester_info(String serviceRefNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean deleted = false;

		try {
			con = ConnectionManager.getConnection();

			String query1 = "DELETE from  public.nt_m_nri_requester_info  where nri_service_ref_no=?";
			ps = con.prepareStatement(query1);
			ps.setString(1, serviceRefNo);

			int i = ps.executeUpdate();

			if (i > 0) {
				deleted = true;
			} else {
				deleted = false;
			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
			deleted = false;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return deleted;
	}

	@Override
	public List<String> retrieveRequestNumbers() {
		List<String> nisiSeriyaList = new ArrayList<String>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct nri_request_no from public.nt_m_nri_requester_info order by nri_request_no desc;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				nisiSeriyaList.add(rs.getString("nri_request_no"));
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return nisiSeriyaList;
	}

	@Override
	public List<String> retrieveServiceRefNumbers(String requestedNo, String permitNum, String serviceAgreementNo) {

		List<String> nisiSeriyaList = new ArrayList<String>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String where = "";

			if (requestedNo != null && !requestedNo.isEmpty() && !requestedNo.trim().equalsIgnoreCase("")) {
				where = " nri_request_no='" + requestedNo + "'";
			}

			if (permitNum != null && !permitNum.isEmpty() && !permitNum.trim().equalsIgnoreCase("")) {
				if (where != null && !where.isEmpty()) {
					where = where + " and nri_permit_no='" + permitNum + "'";
				} else {
					where = " nri_permit_no='" + permitNum + "'";
				}
			}

			if (serviceAgreementNo != null && !serviceAgreementNo.isEmpty()
					&& !serviceAgreementNo.trim().equalsIgnoreCase("")) {
				if (where != null && !where.isEmpty()) {
					where = where + " and nri_service_agreement_no='" + serviceAgreementNo + "'";
				} else {
					where = " nri_service_agreement_no='" + serviceAgreementNo + "'";
				}
			}

			String query = "select nri_service_ref_no from public.nt_m_nri_requester_info where " + where;

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				nisiSeriyaList.add(rs.getString("nri_service_ref_no"));
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return nisiSeriyaList;
	}

	@Override
	public List<String> retrievePermitNumbers(String requestedNo, String serviceRefNum, String serviceAgreementNo) {

		List<String> nisiSeriyaList = new ArrayList<String>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String where = "";

		if (requestedNo != null && !requestedNo.isEmpty() && !requestedNo.trim().equalsIgnoreCase("")) {
			where = " and nri_request_no='" + requestedNo + "'";
		}

		if (serviceRefNum != null && !serviceRefNum.isEmpty() && !serviceRefNum.trim().equalsIgnoreCase("")) {
			if (where != null && !where.isEmpty()) {
				where = where + " and nri_service_ref_no='" + serviceRefNum + "'";
			} else {
				where = " and nri_service_ref_no='" + serviceRefNum + "'";
			}
		}

		if (serviceAgreementNo != null && !serviceAgreementNo.isEmpty()
				&& !serviceAgreementNo.trim().equalsIgnoreCase("")) {
			if (where != null && !where.isEmpty()) {
				where = where + " and nri_service_agreement_no='" + serviceAgreementNo + "'";
			} else {
				where = " and nri_service_agreement_no='" + serviceAgreementNo + "'";
			}
		}

		try {
			con = ConnectionManager.getConnection();

			String query = "select nri_permit_no from public.nt_m_nri_requester_info where nri_permit_no != '' and nri_permit_no is not null "
					+ where;

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				nisiSeriyaList.add(rs.getString("nri_permit_no"));
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return nisiSeriyaList;

	}

	@Override
	public List<String> retrieveServiceAgreementNumbers(String requestedNo, String serviceRefNum, String permitNo) {

		List<String> nisiSeriyaList = new ArrayList<String>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String where = "";

			if (requestedNo != null && !requestedNo.isEmpty() && !requestedNo.trim().equalsIgnoreCase("")) {
				where = " and nri_request_no='" + requestedNo + "'";
			}

			if (serviceRefNum != null && !serviceRefNum.isEmpty() && !serviceRefNum.trim().equalsIgnoreCase("")) {
				if (where != null && !where.isEmpty()) {
					where = where + " and nri_service_ref_no='" + serviceRefNum + "'";
				} else {
					where = " and nri_service_ref_no='" + serviceRefNum + "'";
				}
			}

			if (permitNo != null && !permitNo.isEmpty() && !permitNo.trim().equalsIgnoreCase("")) {
				if (where != null && !where.isEmpty()) {
					where = where + " and nri_permit_no='" + permitNo + "'";
				} else {
					where = " and nri_permit_no='" + permitNo + "'";
				}
			}

			String query = "select nri_service_agreement_no from public.nt_m_nri_requester_info where nri_service_agreement_no != '' and nri_service_agreement_no is not null"
					+ where;

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				nisiSeriyaList.add(rs.getString("nri_service_agreement_no"));
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return nisiSeriyaList;

	}

	@Override
	public List<NisiSeriyaDTO> searchDataFromNt_t_task_det(String requestedNo, String permitNo,
			String serviceAgreementNo, String serviceRefNum) {

		List<NisiSeriyaDTO> nisiSeriyaList = new ArrayList<NisiSeriyaDTO>();
		int approveCount = 0;

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String where = "";

			if (requestedNo != null && !requestedNo.isEmpty() && !requestedNo.trim().equalsIgnoreCase("")) {
				where = " nri_request_no='" + requestedNo + "'";
			}

			if (permitNo != null && !permitNo.isEmpty() && !permitNo.trim().equalsIgnoreCase("")) {
				if (where != null && !where.isEmpty()) {
					where = where + " and nri_permit_no='" + permitNo + "'";
				} else {
					where = " nri_permit_no='" + permitNo + "'";
				}
			}

			if (serviceAgreementNo != null && !serviceAgreementNo.isEmpty()
					&& !serviceAgreementNo.trim().equalsIgnoreCase("")) {
				if (where != null && !where.isEmpty()) {
					where = where + " and nri_service_agreement_no='" + serviceAgreementNo + "'";
				} else {
					where = " nri_service_agreement_no='" + serviceAgreementNo + "'";
				}
			}

			if (serviceRefNum != null && !serviceRefNum.isEmpty() && !serviceRefNum.trim().equalsIgnoreCase("")) {
				if (where != null && !where.isEmpty()) {
					where = where + " and nri_service_ref_no='" + serviceRefNum + "'";
				} else {
					where = " nri_service_ref_no='" + serviceRefNum + "'";
				}
			}

			String query = "select nri_request_no, nri_service_ref_no, nri_permit_no, nri_prefer_language, nri_nic_no, "
					+ "nri_operator_name, nri_operator_name_si, nri_operator_name_ta, "
					+ "nri_add1, nri_add1_si, nri_add1_ta, nri_add2, nri_add2_si, nri_add2_ta, nri_city, nri_city_si, nri_city_ta, "
					+ "nri_service_type, nri_bus_no, nri_service_start_date, nri_service_end_date, nri_province, nri_district, nri_div_sec, "
					+ "nri_telephone_no, nri_mobile_no, nri_status, nri_origin, nri_destination, nri_distance, nri_tips_per_day, nri_via, "
					+ "nri_account_no, nri_bank_name, nri_branch,nri_expire_date,nri_service_agreement_no "
					+ "from public.nt_m_nri_requester_info where " + where;

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				NisiSeriyaDTO dto = new NisiSeriyaDTO();

				dto.setRequestNo(rs.getString("nri_request_no"));
				dto.setServiceRefNo(rs.getString("nri_service_ref_no"));
				dto.setPermitNo(rs.getString("nri_permit_no"));
				dto.setLanguageCode(rs.getString("nri_prefer_language"));
				dto.setNicNo(rs.getString("nri_nic_no"));
				dto.setNameOfOperator(rs.getString("nri_operator_name"));
				dto.setNameOfOperatorSin(rs.getString("nri_operator_name_si"));
				dto.setNameOfOperatorTamil(rs.getString("nri_operator_name_ta"));
				dto.setAddressOne(rs.getString("nri_add1"));
				dto.setAddressOneSin(rs.getString("nri_add1_si"));
				dto.setAddressOneTamil(rs.getString("nri_add1_ta"));
				dto.setAddressTwo(rs.getString("nri_add2"));
				dto.setAdressTwoSin(rs.getString("nri_add2_si"));
				dto.setAdressTwoTamil(rs.getString("nri_add2_ta"));
				dto.setCity(rs.getString("nri_city"));
				dto.setCitySin(rs.getString("nri_city_si"));
				dto.setCityTamil(rs.getString("nri_city_ta"));
				dto.setServiceTypeCode(rs.getString("nri_service_type"));
				dto.setBusRegNo(rs.getString("nri_bus_no"));
				dto.setServiceStartDateObj(rs.getTimestamp("nri_service_start_date"));
				dto.setServiceEndDateObj(rs.getTimestamp("nri_service_end_date"));

				if (rs.getTimestamp("nri_service_end_date") != null) {
					DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
					dto.setServiceEndDateVal(f.format(rs.getTimestamp("nri_service_end_date")));
				}

				dto.setProvinceCode(rs.getString("nri_province"));
				dto.setDistrictCode(rs.getString("nri_district"));
				dto.setDivisionalSecCode(rs.getString("nri_div_sec"));
				dto.setTelNo(rs.getString("nri_telephone_no"));
				dto.setMobileNo(rs.getString("nri_mobile_no"));
				dto.setStatusCode(rs.getString("nri_status"));

				if (dto.getStatusCode() != null && !dto.getStatusCode().isEmpty()
						&& !dto.getStatusCode().trim().equalsIgnoreCase("")) {
					if (dto.getStatusCode().equalsIgnoreCase("O")) {
						dto.setStatusDes("ON GOING");
					}
					if (dto.getStatusCode().equalsIgnoreCase("P")) {
						dto.setStatusDes("PENDING");
					}
					if (dto.getStatusCode().equalsIgnoreCase("A")) {
						dto.setStatusDes("APPROVED");
						dto.setApprovedRecord(true);
						approveCount = approveCount + 1;
					}
					if (dto.getStatusCode().equalsIgnoreCase("SR")) {
						dto.setStatusDes("SERVICE REJECT");
					}
					if (dto.getStatusCode().equalsIgnoreCase("R")) {
						dto.setStatusDes("REJECTED");
					}
				}

				dto.setOriginCode(rs.getString("nri_origin"));
				dto.setDestinationCode(rs.getString("nri_destination"));
				if (rs.getString("nri_distance") != null && !rs.getString("nri_distance").isEmpty()
						&& !rs.getString("nri_distance").trim().equalsIgnoreCase("")) {
					dto.setDistance(Double.parseDouble(rs.getString("nri_distance")));
				}
				if (rs.getString("nri_tips_per_day") != null && !rs.getString("nri_tips_per_day").isEmpty()
						&& !rs.getString("nri_tips_per_day").trim().equalsIgnoreCase("")) {
					dto.setTripsPerDay(Integer.valueOf(rs.getString("nri_tips_per_day")));
				}
				dto.setVia(rs.getString("nri_via"));
				dto.setAccountNo(rs.getString("nri_account_no"));
				dto.setBankNameCode(rs.getString("nri_bank_name"));
				dto.setBankBranchNameCode(rs.getString("nri_branch"));
				dto.setExpiryDateObj(rs.getTimestamp("nri_expire_date"));
				dto.setServiceAgreementNo(rs.getString("nri_service_agreement_no"));
				dto.setApproveCount(approveCount);
				nisiSeriyaList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return nisiSeriyaList;

	}

	@Override
	public void updateDataIntoNt_m_nri_requester_info(NisiSeriyaDTO nisiSeriyaDTO, String loginUser) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			/** update data start **/
			String sql = "UPDATE public.nt_m_nri_requester_info SET nri_permit_no=?, nri_prefer_language=?, nri_nic_no=?, "
					+ " nri_operator_name=?, nri_operator_name_si=?, nri_operator_name_ta=?, nri_add1=?, nri_add1_si=?, "
					+ " nri_add1_ta=?, nri_add2=?, nri_add2_si=?, nri_add2_ta=?, nri_city=?, nri_city_si=?, nri_city_ta=?,"
					+ " nri_service_type=?, nri_bus_no=?, nri_service_start_date=?, nri_service_end_date=?, nri_province=?, nri_district=?, nri_div_sec=?, "
					+ " nri_telephone_no=?, nri_mobile_no=?, nri_origin=?, nri_destination=?, nri_distance=?, nri_tips_per_day=?, modified_by=?, modified_date=? "
					+ " where nri_service_ref_no=?";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, nisiSeriyaDTO.getPermitNo());
			stmt.setString(2, nisiSeriyaDTO.getLanguageCode());
			stmt.setString(3, nisiSeriyaDTO.getNicNo());
			stmt.setString(4, nisiSeriyaDTO.getNameOfOperator());
			stmt.setString(5, nisiSeriyaDTO.getNameOfOperatorSin());
			stmt.setString(6, nisiSeriyaDTO.getNameOfOperatorTamil());
			stmt.setString(7, nisiSeriyaDTO.getAddressOne());
			stmt.setString(8, nisiSeriyaDTO.getAddressOneSin());
			stmt.setString(9, nisiSeriyaDTO.getAddressOneTamil());
			stmt.setString(10, nisiSeriyaDTO.getAddressTwo());
			stmt.setString(11, nisiSeriyaDTO.getAdressTwoSin());
			stmt.setString(12, nisiSeriyaDTO.getAdressTwoTamil());
			stmt.setString(13, nisiSeriyaDTO.getCity());
			stmt.setString(14, nisiSeriyaDTO.getCitySin());
			stmt.setString(15, nisiSeriyaDTO.getCityTamil());
			stmt.setString(16, nisiSeriyaDTO.getServiceTypeCode());
			stmt.setString(17, nisiSeriyaDTO.getBusRegNo());
			if (nisiSeriyaDTO.getServiceStartDateObj() != null) {
				Timestamp serviceStartDate = new Timestamp(nisiSeriyaDTO.getServiceStartDateObj().getTime());
				stmt.setTimestamp(18, serviceStartDate);
			} else {
				stmt.setNull(18, java.sql.Types.TIMESTAMP);
			}
			if (nisiSeriyaDTO.getServiceEndDateObj() != null) {
				Timestamp serviceEndDate = new Timestamp(nisiSeriyaDTO.getServiceEndDateObj().getTime());
				stmt.setTimestamp(19, serviceEndDate);
			} else {
				stmt.setNull(19, java.sql.Types.TIMESTAMP);
			}
			stmt.setString(20, nisiSeriyaDTO.getProvinceCode());
			stmt.setString(21, nisiSeriyaDTO.getDistrictCode());
			stmt.setString(22, nisiSeriyaDTO.getDivisionalSecCode());
			stmt.setString(23, nisiSeriyaDTO.getTelNo());
			stmt.setString(24, nisiSeriyaDTO.getMobileNo());
			stmt.setString(25, nisiSeriyaDTO.getOriginCode());
			stmt.setString(26, nisiSeriyaDTO.getDestinationCode());

			stmt.setDouble(27, nisiSeriyaDTO.getDistance());

			if (nisiSeriyaDTO.getTripsPerDay() != 0) {
				stmt.setString(28, Integer.toString(nisiSeriyaDTO.getTripsPerDay()));
			} else {
				stmt.setNull(28, java.sql.Types.TIMESTAMP);
			}
			stmt.setString(29, loginUser);
			stmt.setTimestamp(30, timestamp);
			stmt.setString(31, nisiSeriyaDTO.getServiceRefNo());

			stmt.executeUpdate();

			con.commit();

			/** update data end **/
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

	}

	@Override
	public String generateServiceAgreementNumber(String serviceRefNum, String loginUser) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String agreementNum = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		int year = Year.now().getValue();
		String currYear = Integer.toString(year).substring(2, 4);
		String strServiceAgreemntNo = "";
		String oldServiceAgreemntNo = "";
		String lastyr = "00";

		try {

			/** check whether old service agreement num available start **/
			String selectSql = "SELECT nri_old_service_agreement_no from nt_m_nri_requester_info where nri_service_ref_no='"
					+ serviceRefNum + "'";
			con = ConnectionManager.getConnection();
			stmt = con.prepareStatement(selectSql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				oldServiceAgreemntNo = rs.getString("nri_old_service_agreement_no");
			}

			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			/** check whether old service agreement num available end **/

			if (oldServiceAgreemntNo != null && !oldServiceAgreemntNo.isEmpty()
					&& !oldServiceAgreemntNo.trim().equalsIgnoreCase("")) {
				strServiceAgreemntNo = oldServiceAgreemntNo;

			} else {

				String sql = "SELECT app_no FROM public.nt_r_number_generation WHERE code= 'NSA'";

				stmt = con.prepareStatement(sql);
				rs = stmt.executeQuery();

				while (rs.next()) {
					agreementNum = rs.getString("app_no");
				}

				if (agreementNum != null) {

					lastyr = agreementNum.substring(3, 5);
					if (!lastyr.equals(currYear)) {
						String ApprecordcountN = "00001";
						strServiceAgreemntNo = "NSA" + currYear + ApprecordcountN;
					} else {
						String number = agreementNum.substring(5, 10);
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
						strServiceAgreemntNo = "NSA" + currYear + ApprecordcountN;
					}
				} else {
					strServiceAgreemntNo = "NSA" + currYear + "00001";
				}
				ConnectionManager.close(stmt);
			}

			updateNumberGeneration("NSA", strServiceAgreemntNo, loginUser);

			/** update Service Agreemnt No for service ref num start **/
			String update = "UPDATE public.nt_m_nri_requester_info SET nri_service_agreement_no=?, nri_status=?, modified_by=?, modified_date=?, nri_service_approve_rej_by=?, nri_service_approve_rej_date=?, "
					+ " nri_old_service_agreement_no=?  WHERE nri_service_ref_no=? ";

			stmt = con.prepareStatement(update);
			stmt.setString(1, strServiceAgreemntNo);
			stmt.setString(2, "P");
			stmt.setString(3, loginUser);
			stmt.setTimestamp(4, timestamp);
			stmt.setString(5, loginUser);
			stmt.setTimestamp(6, timestamp);
			stmt.setNull(7, java.sql.Types.VARCHAR);
			stmt.setString(8, serviceRefNum);

			stmt.executeUpdate();
			con.commit();
			/** update Service Agreement No for service ref num end **/

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return strServiceAgreemntNo;
	}

	@Override
	public boolean serviceConfirmationReject(String serviceRefNo, String rejectReason, String loginUser,
			String oldSerAgreementNum) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		boolean updated = false;

		try {

			String update = "UPDATE public.nt_m_nri_requester_info SET nri_status=?, nri_reject_reason=?, modified_by=?, modified_date=?, "
					+ "nri_old_service_agreement_no=?, nri_service_agreement_no=? ,nri_service_approve_rej_by=?,nri_service_approve_rej_date=?  WHERE nri_service_ref_no=? ";
			con = ConnectionManager.getConnection();
			stmt = con.prepareStatement(update);
			stmt.setString(1, "SR");
			stmt.setString(2, rejectReason);
			stmt.setString(3, loginUser);
			stmt.setTimestamp(4, timestamp);
			if (oldSerAgreementNum != null && !oldSerAgreementNum.isEmpty()
					&& !oldSerAgreementNum.trim().equalsIgnoreCase("")) {
				stmt.setString(5, oldSerAgreementNum);
			} else {
				stmt.setNull(5, java.sql.Types.VARCHAR);
			}
			stmt.setNull(6, java.sql.Types.VARCHAR);

			stmt.setString(7, loginUser);
			stmt.setTimestamp(8, timestamp);
			stmt.setString(9, serviceRefNo);

			int i = stmt.executeUpdate();
			con.commit();
			if (i > 0) {
				updated = true;
			} else {
				updated = false;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return updated;
	}

	@Override
	public void approveServiceConfirmation(String serviceRefNo, String loginUser) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			String update = "UPDATE public.nt_m_nri_requester_info SET nri_status=?, modified_by=?, modified_date=? , nri_approve_rej_by =? ,nri_approve_rej_date= ? WHERE nri_service_ref_no=? ";
			con = ConnectionManager.getConnection();
			stmt = con.prepareStatement(update);
			stmt.setString(1, "A");
			stmt.setString(2, loginUser);
			stmt.setTimestamp(3, timestamp);
			stmt.setString(4, loginUser);
			stmt.setTimestamp(5, timestamp);
			stmt.setString(6, serviceRefNo);

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
	public void rejectServiceConfirmation(String serviceRefNo, String loginUser) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			String update = "UPDATE public.nt_m_nri_requester_info SET nri_status=?, modified_by=?, modified_date=? WHERE nri_service_ref_no=? ";
			con = ConnectionManager.getConnection();
			stmt = con.prepareStatement(update);
			stmt.setString(1, "R");
			stmt.setString(2, loginUser);
			stmt.setTimestamp(3, timestamp);
			stmt.setString(4, serviceRefNo);

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
	public boolean validatePermitNumberDuplicate(NisiSeriyaDTO nisiSeriyaDTO) {

		boolean permitNumExist = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select * from public.nt_m_nri_requester_info where nri_permit_no='"
					+ nisiSeriyaDTO.getPermitNo() + "'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				permitNumExist = true;
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return permitNumExist;
	}

	@Override
	public List<NisiSeriyaDTO> getTblGamiIssueLogSheetList(NisiSeriyaDTO nisiSeriyaDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<NisiSeriyaDTO> dataList = new ArrayList<NisiSeriyaDTO>();
		String WHERE_SQL = "";

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select nri_request_no,nri_service_ref_no,nri_service_agreement_no,nri_operator_name,nri_expire_date,nri_status,\r\n"
					+ "nri_service_agreement_issued_date,nri_permit_sticker_issued_date,nri_issue_logsheets_issued_date,\r\n"
					+ "nri_service_agreement_issuedby,nri_permit_sticker_issuedby,nri_issue_logsheets_issuedby,\r\n"
					+ "nri_is_issue_service_agreement,nri_is_issue_permit_sticker,nri_is_issue_logsheets\r\n"
					+ "from public.nt_m_nri_requester_info\r\n"
					+ "where nri_service_agreement_no is not null and nri_status='A' " + WHERE_SQL;

			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();
			NisiSeriyaDTO e;

			while (rs.next()) {
				e = new NisiSeriyaDTO();
				e.setRequestNo(rs.getString("nri_request_no"));
				e.setServiceRefNo(rs.getString("nri_service_ref_no"));
				e.setServiceNo(rs.getString("nri_service_agreement_no"));
				e.setNameOfOperator(rs.getString("nri_operator_name"));

				e.setServiceEndDateVal(rs.getString("nri_expire_date"));

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

				dataList.add(e);
			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return dataList;

	}

	@Override
	public List<NisiSeriyaDTO> getDrpdRequestNoListForNsIssueLogSheets() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<NisiSeriyaDTO> data = new ArrayList<NisiSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct nri_request_no\r\n" + "from public.nt_m_nri_requester_info\r\n"
					+ "where nri_service_agreement_no is not null and nri_request_no is not null\r\n"
					+ "order by nri_request_no desc";

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
	public List<NisiSeriyaDTO> getDrpdServiceRefNoListForNsIssueLogSheets() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<NisiSeriyaDTO> data = new ArrayList<NisiSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct nri_service_ref_no\r\n" + "from public.nt_m_nri_requester_info\r\n"
					+ "where nri_service_agreement_no is not null and nri_service_ref_no is not null\r\n"
					+ "order by nri_service_ref_no desc";

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
	public List<NisiSeriyaDTO> getDrpdServiceNoListForNsIssueLogSheets() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<NisiSeriyaDTO> data = new ArrayList<NisiSeriyaDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct nri_service_agreement_no\r\n" + "from public.nt_m_nri_requester_info\r\n"
					+ "where nri_service_agreement_no is not null \r\n" + "order by nri_service_agreement_no desc";

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
	public boolean checkServiceConfirmationAndRejectStatus(NisiSeriyaDTO dto) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String WHERE_SQL = "";
		boolean whereadded = false;
		boolean valid = false;

		if (dto.getRequestNo() != null && !dto.getRequestNo().equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE tsd_request_no = " + "'" + dto.getRequestNo() + "' ";
			whereadded = true;
		}
		if (dto.getServiceNo() != null && !dto.getServiceNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND tsd_service_no = " + "'" + dto.getServiceNo() + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE tsd_service_no = " + "'" + dto.getServiceNo() + "' ";
				whereadded = true;
			}
		}
		if (dto.getServiceRefNo() != null && !dto.getServiceRefNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND tsd_reference_no = " + "'" + dto.getServiceRefNo() + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE tsd_reference_no = " + "'" + dto.getServiceRefNo() + "' ";
				whereadded = true;
			}
		}

		if (whereadded) {
			WHERE_SQL = WHERE_SQL + " AND tsd_task_code='NS002' AND tsd_status='O' ";

		} else {
			WHERE_SQL = WHERE_SQL + " WHERE tsd_task_code='NS002' AND tsd_status='O'";
			whereadded = true;
		}

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT * " + "FROM public.nt_t_subsidy_task_det " + WHERE_SQL;

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				valid = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return valid;
	}

	@Override
	public boolean checkServiceApproveAndRejectStatus(NisiSeriyaDTO dto) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String WHERE_SQL = "";
		boolean whereadded = false;
		boolean valid = false;

		if (dto.getRequestNo() != null && !dto.getRequestNo().equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE tsd_request_no = " + "'" + dto.getRequestNo() + "' ";
			whereadded = true;
		}
		if (dto.getServiceNo() != null && !dto.getServiceNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND tsd_service_no = " + "'" + dto.getServiceNo() + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE tsd_service_no = " + "'" + dto.getServiceNo() + "' ";
				whereadded = true;
			}
		}
		if (dto.getServiceRefNo() != null && !dto.getServiceRefNo().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND tsd_reference_no = " + "'" + dto.getServiceRefNo() + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE tsd_reference_no = " + "'" + dto.getServiceRefNo() + "' ";
				whereadded = true;
			}
		}

		if (whereadded) {
			WHERE_SQL = WHERE_SQL + " AND tsd_task_code='NS003' AND tsd_status='O' ";

		} else {
			WHERE_SQL = WHERE_SQL + " WHERE tsd_task_code='NS003' AND tsd_status='O'";
			whereadded = true;
		}

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT * " + "FROM public.nt_t_subsidy_task_det " + WHERE_SQL;

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				valid = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return valid;
	}

	@Override
	public boolean checkServiceApproved(NisiSeriyaDTO dto) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean exist = false;
		try {
			con = ConnectionManager.getConnection();

			String update = "SELECT * FROM public.nt_m_nri_requester_info WHERE nri_service_ref_no=? AND nri_status='P'";

			stmt = con.prepareStatement(update);
			stmt.setString(1, dto.getServiceRefNo());

			rs = stmt.executeQuery();

			while (rs.next()) {
				exist = true;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return exist;
	}

}
